package br.com.estudario.ui.ai

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.AiJob
import br.com.estudario.data.ai.AiJobStatus
import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.syllabus.SyllabusApplicationService
import br.com.estudario.domain.ai.AiSyllabusDraft
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiReviewViewModelTest {
    @Test
    fun readySourceStartsExactlyOnceWithSelectedTargetAndSameSource() {
        val jobs = FakeJobs(blockStart = true)
        val sessions = FakeSessionStore()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }

        viewModel.start("content://edital", "edital.pdf")
        viewModel.start("content://edital", "edital.pdf")
        await { jobs.startCalls == 1 }
        assertEquals(listOf(42L), jobs.targetIds)
        assertEquals(listOf("content://edital"), jobs.uris)

        jobs.release.complete(Unit)
        await { viewModel.state.value.content is AiReviewContent.Review }
        assertTrue(sessions.saved.isNotEmpty())
        assertEquals("job-1", sessions.saved.last().jobId)
    }

    @Test
    fun selectedTargetAndSourceArePersistedBeforeJobRepositoryStarts() {
        val targetStore = FakeTargetStore()
        val jobs = FakeJobs(beforeStart = { assertEquals(AiReviewTarget(42L, "TRT-3", "content://edital", "edital.pdf"), targetStore.current) })
        val viewModel = createViewModel(jobs = jobs, targetStore = targetStore)
        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }

        viewModel.start("content://edital", "edital.pdf")

        await { jobs.startCalls == 1 }
    }

    @Test
    fun restartRecoversPersistedJobWithoutStartingAnotherJob() {
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", "request-1", "job-1", "idem-1"),
        )
        val jobs = FakeJobs()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)

        await { jobs.recoveredRequests == listOf("request-1") }
        assertEquals(0, jobs.startCalls)
        assertEquals("job-1", (viewModel.state.value.content as AiReviewContent.Processing).jobId)
        assertEquals("TRT-3", viewModel.state.value.targetTitle)
    }

    @Test
    fun unauthenticatedGateUsesLoginBoundaryThenStartsAfterAccessReturns() {
        val access = FakeAccessGateway(AiReviewAccessResult(false, false, "UNAUTHENTICATED"))
        var loginLaunches = 0
        var returnedFromLogin: (() -> Unit)? = null
        val jobs = FakeJobs()
        val viewModel = createViewModel(
            jobs = jobs,
            access = access,
            loginLauncher = AiReviewLoginLauncher { onReturned ->
                loginLaunches += 1
                returnedFromLogin = onReturned
            },
        )

        await { viewModel.state.value.access.kind == AiReviewAccessKind.UNAUTHENTICATED }
        viewModel.requestLogin()
        assertEquals(1, loginLaunches)
        access.result = AiReviewAccessResult(true, true)
        returnedFromLogin!!.invoke()
        viewModel.start("content://after-login", "edital.pdf")
        await { jobs.startCalls == 1 }
        assertEquals(42L, jobs.targetIds.single())
    }

    @Test
    fun genericFailureAfterCreatedRequestKeepsIdentityForRetry() {
        val identity = AiReviewRequestIdentity("request-after-upload", "job-after-upload", "idem-after-upload")
        val jobs = FailOnceAfterCreatedRequestJobs(identity)
        val sessions = FakeSessionStore()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }

        viewModel.start("content://edital", "edital.pdf")
        await { viewModel.state.value.content is AiReviewContent.Processing }
        assertEquals(identity.jobId, (viewModel.state.value.content as AiReviewContent.Processing).jobId)
        assertEquals(identity.idempotencyKey, sessions.saved.last().idempotencyKey)

        viewModel.retry()
        await { viewModel.state.value.content is AiReviewContent.Review }
        assertEquals(1, jobs.startCalls)
        assertEquals(listOf(identity.requestId), jobs.recoveredRequests)
        assertEquals(identity, jobs.recoveredIdentity)
    }

    @Test
    fun genericFailureBeforeJobIdStillPersistsRequestIdentityAcrossRestart() {
        val pending = AiReviewPendingRequestIdentity("request-before-job", "idem-before-job")
        val sessions = FakeSessionStore()
        val firstJobs = PendingIdentityFailureJobs(pending)
        val firstViewModel = createViewModel(jobs = firstJobs, sessions = sessions)
        await { firstViewModel.state.value.access.kind == AiReviewAccessKind.READY }

        firstViewModel.start("content://edital", "edital.pdf")
        await { sessions.saved.any { it.requestId == pending.requestId } }
        assertEquals("", sessions.saved.last().jobId)
        assertEquals(pending.idempotencyKey, sessions.saved.last().idempotencyKey)

        val restartedJobs = PendingIdentityFailureJobs(pending, failStart = false)
        val restartedViewModel = createViewModel(jobs = restartedJobs, sessions = sessions)
        await { restartedViewModel.state.value.content is AiReviewContent.Review }
        assertEquals(0, restartedJobs.startCalls)
        assertEquals(listOf(pending.requestId), restartedJobs.recoveredRequests)
    }

    @Test
    fun retryingTerminalJobsStartsANewAttemptWhileNonterminalRecoveryKeepsIdentity() {
        listOf(AiJobStatus.FAILED, AiJobStatus.EXPIRED, AiJobStatus.CANCELLED).forEach { terminalStatus ->
            val requestId = "request-${terminalStatus.name.lowercase()}"
            val sessions = FakeSessionStore(
                AiReviewPersistedSession(42L, "TRT-3", requestId, "job-original", "idem-original"),
            )
            val jobs = RetryDispatchJobs(terminalStatus)
            val viewModel = createViewModel(jobs = jobs, sessions = sessions)
            await { viewModel.state.value.content is AiReviewContent.Failure }

            viewModel.retry()
            await { jobs.retryFailedRequests.isNotEmpty() || jobs.recoveredRequests.size > 1 }
            assertEquals(listOf(requestId), jobs.retryFailedRequests)
            await { viewModel.state.value.content is AiReviewContent.Processing }
            await { sessions.saved.lastOrNull()?.idempotencyKey == "idem-retry" }

            assertEquals(listOf(requestId), jobs.recoveredRequests)
            assertEquals("job-retry", (viewModel.state.value.content as AiReviewContent.Processing).jobId)
            assertEquals(requestId, sessions.saved.last().requestId)
            assertEquals("idem-retry", sessions.saved.last().idempotencyKey)
        }

        val requestId = "request-processing"
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", requestId, "job-original", "idem-original"),
        )
        val jobs = NonterminalRetryJobs()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { jobs.recoveredRequests.size == 1 }

        viewModel.retry()
        await { jobs.recoveredRequests.size == 2 }

        assertEquals(emptyList<String>(), jobs.retryFailedRequests)
        assertEquals(listOf(requestId, requestId), jobs.recoveredRequests)
        assertEquals("job-original", (viewModel.state.value.content as AiReviewContent.Processing).jobId)
        assertEquals("idem-original", (viewModel.state.value.content as AiReviewContent.Processing).idempotencyKey)
    }

    @Test
    fun retryFailedErrorRetainsTerminalRetryPathAndRequestIdentity() {
        val requestId = "request-terminal-retry"
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", requestId, "job-original", "idem-original"),
        )
        val jobs = FailOnceTerminalRetryJobs()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { viewModel.state.value.content is AiReviewContent.Failure }

        viewModel.retry()
        await { (viewModel.state.value.content as? AiReviewContent.Failure)?.message == "temporary retry failure" }
        assertEquals(listOf(requestId), jobs.retryFailedRequests)

        viewModel.retry()
        await { jobs.retryFailedRequests.size > 1 || jobs.recoveredRequests.size > 1 }
        assertEquals(listOf(requestId, requestId), jobs.retryFailedRequests)
        assertEquals(listOf(requestId), jobs.recoveredRequests)
        await { viewModel.state.value.content is AiReviewContent.Processing }
        await { sessions.saved.lastOrNull()?.idempotencyKey == "idem-retry" }
        assertEquals(requestId, sessions.saved.last().requestId)
    }

    @Test
    fun applyUsesSyllabusApplicationServiceAndPollsUntilAcknowledged() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        try {
            runBlocking { database.dao().insertCompetition(CompetitionEntity(id = 42L, name = "TRT-3")) }
            val service = SyllabusApplicationService(database)
            var polls = 0
            val viewModel = createViewModel(
                jobs = FakeJobs(),
                applier = AiReviewApplier { targetId, draft, jobId, replace ->
                    service.applyReviewedSyllabus(targetId, draft, jobId, replace)
                },
                syncState = { _ -> if (polls++ == 0) RemoteSyllabusSyncState.PENDING else RemoteSyllabusSyncState.SYNCED },
                syncPollDelayMillis = 1L,
            )
            viewModel.start("content://edital", "edital.pdf")
            await { viewModel.state.value.content is AiReviewContent.Review }

            viewModel.apply()
            await { viewModel.state.value.content == AiReviewContent.Applied(RemoteSyllabusSyncState.SYNCED) }
            runBlocking {
                assertTrue(database.dao().subjectsFor(42L).isNotEmpty())
                assertEquals(RemoteSyllabusSyncState.PENDING, database.dao().remoteSyllabusSyncById(1L)?.state)
            }
        } finally {
            database.close()
        }
    }

    private fun createViewModel(
        jobs: AiReviewJobs,
        sessions: FakeSessionStore = FakeSessionStore(),
        access: FakeAccessGateway = FakeAccessGateway(AiReviewAccessResult(true, true)),
        applier: AiReviewApplier = AiReviewApplier { _, _, _, _ -> error("unexpected apply") },
        loginLauncher: AiReviewLoginLauncher = AiReviewLoginLauncher {},
        targetStore: AiReviewTargetStore? = null,
        syncState: suspend (Long) -> RemoteSyllabusSyncState? = { null },
        syncPollDelayMillis: Long = 1L,
    ) = AiReviewViewModel(
        application = ApplicationProvider.getApplicationContext(),
        targetId = 42L,
        targetTitle = "TRT-3",
        jobs = jobs,
        applier = applier,
        sessions = sessions,
        targetStore = targetStore,
        accessGateway = access,
        loginLauncher = loginLauncher,
        syncState = syncState,
        syncPollDelayMillis = syncPollDelayMillis,
    )

    private fun await(condition: () -> Boolean) {
        repeat(200) {
            if (condition()) return
            Thread.sleep(10)
        }
        error("Condition was not reached")
    }

    private class FakeAccessGateway(var result: AiReviewAccessResult) : AiReviewAccessGateway {
        override suspend fun check(): AiReviewAccessResult = result
    }

    private class FakeSessionStore(initial: AiReviewPersistedSession? = null) : AiReviewSessionStore {
        val saved = mutableListOf<AiReviewPersistedSession>()
        private var current = initial

        override suspend fun load(targetId: Long): AiReviewPersistedSession? = current?.takeIf { it.targetId == targetId }
        override suspend fun loadLatest(): AiReviewPersistedSession? = current
        override suspend fun save(session: AiReviewPersistedSession) { current = session; saved += session }
        override suspend fun clear(targetId: Long) { if (current?.targetId == targetId) current = null }
    }

    private class FakeTargetStore : AiReviewTargetStore {
        var current: AiReviewTarget? = null

        override suspend fun load(): AiReviewTarget? = current
        override suspend fun save(target: AiReviewTarget) { current = target }
        override suspend fun clear() { current = null }
    }

    private inner class FakeJobs(
        private val blockStart: Boolean = false,
        private val beforeStart: () -> Unit = {},
    ) : AiReviewJobs {
        var startCalls = 0
        val targetIds = mutableListOf<Long>()
        val uris = mutableListOf<String>()
        val recoveredRequests = mutableListOf<String>()
        val release = CompletableDeferred<Unit>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted {
            beforeStart()
            startCalls += 1
            targetIds += targetId
            uris += uri
            if (blockStart) release.await()
            return AiReviewStarted(job(AiJobStatus.SUCCEEDED), AiReviewRequestIdentity("request-1", "job-1", "idem-1"))
        }

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            return AiReviewStarted(job(AiJobStatus.PROCESSING), AiReviewRequestIdentity(requestId, "job-1", "idem-1"))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("terminal retry not expected")

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = AiReviewRequestIdentity("request-1", jobId, "idem-1")
    }

    private inner class FailOnceAfterCreatedRequestJobs(
        private val identity: AiReviewRequestIdentity,
    ) : AiReviewJobs {
        var startCalls = 0
        val recoveredRequests = mutableListOf<String>()
        var recoveredIdentity: AiReviewRequestIdentity? = null

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted {
            startCalls += 1
            throw AiReviewStartException(identity, IllegalStateException("transport failed after upload"))
        }

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            recoveredIdentity = identity
            return AiReviewStarted(job(AiJobStatus.SUCCEEDED), identity)
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("terminal retry not expected")

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = identity.takeIf { it.jobId == jobId }
    }

    private inner class PendingIdentityFailureJobs(
        private val pending: AiReviewPendingRequestIdentity,
        private val failStart: Boolean = true,
    ) : AiReviewJobs {
        var startCalls = 0
        val recoveredRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted {
            startCalls += 1
            if (failStart) throw AiReviewStartException(pending, IllegalStateException("request persisted before transport"))
            return AiReviewStarted(job(AiJobStatus.SUCCEEDED), pending.asStartedIdentity()!!)
        }

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            return AiReviewStarted(job(AiJobStatus.SUCCEEDED), pending.asStartedIdentity() ?: AiReviewRequestIdentity(requestId, "job-recovered", pending.idempotencyKey))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("terminal retry not expected")

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private inner class RetryDispatchJobs(
        private val terminalStatus: AiJobStatus,
    ) : AiReviewJobs {
        val recoveredRequests = mutableListOf<String>()
        val retryFailedRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = error("not expected")

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            val status = if (recoveredRequests.size == 1) terminalStatus else AiJobStatus.PROCESSING
            return AiReviewStarted(job(status).copy(jobId = "job-original"), AiReviewRequestIdentity(requestId, "job-original", "idem-original"))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted {
            retryFailedRequests += requestId
            return AiReviewStarted(job(AiJobStatus.PROCESSING).copy(jobId = "job-retry"), AiReviewRequestIdentity(requestId, "job-retry", "idem-retry"))
        }

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private inner class NonterminalRetryJobs : AiReviewJobs {
        val recoveredRequests = mutableListOf<String>()
        val retryFailedRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = error("not expected")

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            return AiReviewStarted(job(AiJobStatus.PROCESSING).copy(jobId = "job-original"), AiReviewRequestIdentity(requestId, "job-original", "idem-original"))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted {
            retryFailedRequests += requestId
            error("nonterminal job must be recovered")
        }

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private inner class FailOnceTerminalRetryJobs : AiReviewJobs {
        val recoveredRequests = mutableListOf<String>()
        val retryFailedRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = error("not expected")

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            val status = if (recoveredRequests.size == 1) AiJobStatus.FAILED else AiJobStatus.PROCESSING
            return AiReviewStarted(job(status).copy(jobId = "job-original"), AiReviewRequestIdentity(requestId, "job-original", "idem-original"))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted {
            retryFailedRequests += requestId
            if (retryFailedRequests.size == 1) throw IllegalStateException("temporary retry failure")
            return AiReviewStarted(job(AiJobStatus.PROCESSING).copy(jobId = "job-retry"), AiReviewRequestIdentity(requestId, "job-retry", "idem-retry"))
        }

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private fun job(status: AiJobStatus): AiJob = AiJob(
        jobId = "job-1",
        feature = AiFeature.SYLLABUS_GENERATION,
        status = status,
        schemaVersion = if (status == AiJobStatus.SUCCEEDED) 1 else null,
        promptVersion = if (status == AiJobStatus.SUCCEEDED) "syllabus-v1" else null,
        modelVersion = if (status == AiJobStatus.SUCCEEDED) "fixture-model" else null,
        proposal = if (status == AiJobStatus.SUCCEEDED) draft().proposal else null,
        warnings = emptyList(),
        errorCode = null,
        errorMessage = null,
        createdAt = "2026-09-24T10:00:00Z",
        updatedAt = "2026-09-24T10:00:01Z",
        finishedAt = if (status == AiJobStatus.SUCCEEDED) "2026-09-24T10:00:01Z" else null,
        providerExecutionStartedAt = null,
    )

    private fun draft(): AiSyllabusDraft = AiSyllabusDraft.fromProposal(
        42L,
        "TRT-3",
        AiSyllabusProposal(
            1,
            "syllabus-v1",
            "fixture-model",
            "Edital",
            listOf(AiSubjectProposal("Direito", 0, AiPriority.NORMAL, listOf(AiTopicProposal("Constituição", 0, emptyList(), emptyList())), emptyList())),
            emptyList(),
            emptyList(),
        ),
    )
}
