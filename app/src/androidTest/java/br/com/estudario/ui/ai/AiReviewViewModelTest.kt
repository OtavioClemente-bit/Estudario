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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiReviewViewModelTest {
    @Test
    fun switchingAccountsInvalidatesUnconfirmedPreparationAndRechecksAccess() {
        val currentUser = MutableStateFlow("user-a")
        val jobs = FakeJobs()
        val viewModel = createViewModel(jobs = jobs, userIdProvider = { currentUser.value }, userIdFlow = currentUser,
            initialTarget = AiReviewTarget(42L, "PMMG", "content://user-a", "a.pdf", br.com.estudario.data.ai.AiSyllabusPreferences("PMMG", "Soldado")))
        await { viewModel.state.value.content is AiReviewContent.Confirmation && viewModel.state.value.access.kind == AiReviewAccessKind.READY }
        currentUser.value = "user-b"
        await { viewModel.state.value.content is AiReviewContent.Gate }
        assertEquals("", viewModel.preferences.value?.role)
        viewModel.confirmGeneration()
        assertEquals(0, jobs.startCalls)
    }

    @Test
    fun preparingAndEditingDoesNotStartAJobAndExplicitContextNeverLeaks() {
        val jobs = FakeJobs(blockStart = true)
        val oldSessions = FakeSessionStore(AiReviewPersistedSession(42L, "TRT 3 REGIAO TI", "old", "old-job", "old-key", ownerUserId = TEST_USER))
        val target = AiReviewTarget(42L, "PMMG", "content://pmmg", "pmmg.pdf", br.com.estudario.data.ai.AiSyllabusPreferences("PMMG", "SOLDADO"))
        val viewModel = createViewModel(jobs = jobs, sessions = oldSessions, initialTarget = target)
        await { viewModel.state.value.content is AiReviewContent.Confirmation && viewModel.state.value.access.kind == AiReviewAccessKind.READY }
        assertEquals(0, jobs.startCalls)
        assertTrue(jobs.recoveredRequests.isEmpty())
        assertEquals("PMMG", viewModel.preferences.value?.competitionName)
        viewModel.confirmGeneration()
        viewModel.confirmGeneration()
        await { jobs.startCalls == 1 }
        assertEquals("PMMG", jobs.sentPreferences?.competitionName)
        assertEquals("SOLDADO", jobs.sentPreferences?.role)
        jobs.release.complete(Unit)
        await { viewModel.state.value.content is AiReviewContent.Review }
    }

    @Test
    fun replacementPdfCreatesFreshConfirmationAndDoesNotRetryFailedSource() {
        val jobs = FakeJobs()
        val viewModel = createViewModel(jobs = jobs)
        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }
        generate(viewModel, "content://first", "first.pdf")
        await { viewModel.state.value.content is AiReviewContent.Review }
        val firstKey = jobs.confirmedAttempts.single()
        viewModel.start("content://replacement", "replacement.pdf")
        await { viewModel.state.value.content is AiReviewContent.Confirmation }
        assertEquals(1, jobs.startCalls)
        assertEquals("Analista", viewModel.preferences.value?.role)
        viewModel.confirmGeneration()
        await { jobs.startCalls == 2 }
        assertTrue(firstKey != jobs.confirmedAttempts.last())
        assertEquals(listOf("content://first", "content://replacement"), jobs.uris)
    }

    @Test
    fun failedGenerationCanPrepareAnotherPdfWithoutRetryingOldJob() {
        val jobs = FakeJobs()
        jobs.nextStatus = AiJobStatus.FAILED
        val viewModel = createViewModel(jobs = jobs)
        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }
        generate(viewModel, "content://broken", "broken.pdf")
        await { viewModel.state.value.content is AiReviewContent.Failure }
        val oldAttempt = jobs.confirmedAttempts.single()
        viewModel.start("content://correct", "correct.pdf")
        await { viewModel.state.value.content is AiReviewContent.Confirmation }
        assertEquals(1, jobs.startCalls)
        assertEquals("Analista", viewModel.preferences.value?.role)
        jobs.nextStatus = AiJobStatus.SUCCEEDED
        viewModel.confirmGeneration()
        await { viewModel.state.value.content is AiReviewContent.Review }
        assertTrue(oldAttempt != jobs.confirmedAttempts.last())
        assertEquals(listOf("content://broken", "content://correct"), jobs.uris)
    }

    @Test
    fun switchingAccountsHidesAndDoesNotRecoverThePreviousUsersJob() {
        val currentUser = MutableStateFlow("user-a")
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", "request-a", "job-a", "idem-a", ownerUserId = "user-a"),
        )
        val jobs = FakeJobs(identityOwner = "user-a")
        val viewModel = createViewModel(
            jobs = jobs,
            sessions = sessions,
            userIdProvider = { currentUser.value },
            userIdFlow = currentUser,
        )

        await { jobs.recoveredRequests.isNotEmpty() && (viewModel.state.value.content as? AiReviewContent.Processing)?.jobId == "job-1" }
        assertEquals("job-1", (viewModel.state.value.content as AiReviewContent.Processing).jobId)

        currentUser.value = "user-b"

        await { viewModel.state.value.content == AiReviewContent.Gate }
        assertEquals(listOf("request-a"), jobs.recoveredRequests.distinct())
        val recoveryCount = jobs.recoveredRequests.size
        Thread.sleep(100)
        assertEquals(recoveryCount, jobs.recoveredRequests.size)
    }

    @Test
    fun readySourceStartsExactlyOnceWithSelectedTargetAndSameSource() {
        val jobs = FakeJobs(blockStart = true)
        val sessions = FakeSessionStore()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }

        generate(viewModel, "content://edital", "edital.pdf")
        viewModel.confirmGeneration()
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
        val jobs = FakeJobs(beforeStart = { assertEquals("content://edital", targetStore.current?.sourceUri) })
        val viewModel = createViewModel(jobs = jobs, targetStore = targetStore)
        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }

        generate(viewModel, "content://edital", "edital.pdf")

        await { jobs.startCalls == 1 }
    }

    @Test
    fun restartRecoversPersistedJobWithoutStartingAnotherJob() {
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", "request-1", "job-1", "idem-1", ownerUserId = TEST_USER),
        )
        val jobs = FakeJobs()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)

        await { jobs.recoveredRequests == listOf("request-1") }
        assertEquals(0, jobs.startCalls)
        assertEquals("job-1", (viewModel.state.value.content as AiReviewContent.Processing).jobId)
        assertEquals("TRT-3", viewModel.state.value.targetTitle)
    }

    @Test
    fun timedOutStartKeepsRecoveringUntilRemoteFailureThenAllowsManualRetry() {
        val identity = AiReviewRequestIdentity("request-timeout", "job-timeout", "idem-timeout", TEST_USER)
        val sessions = FakeSessionStore()
        val jobs = TimeoutThenTerminalJobs(identity, terminalStatus = AiJobStatus.FAILED)
        val viewModel = createViewModel(jobs = jobs, sessions = sessions, jobRecoveryRetryDelayMillis = 1L)

        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }
        generate(viewModel, "content://edital", "edital.pdf")

        await { viewModel.state.value.content is AiReviewContent.Failure }
        val failure = viewModel.state.value.content as AiReviewContent.Failure
        assertEquals(AiJobStatus.FAILED, failure.terminalStatus)
        assertEquals("A geração não foi concluída. Você pode tentar novamente.", failure.message)
        assertEquals(2, jobs.recoverCalls)
        assertEquals(1, jobs.startCalls)
        await { sessions.currentSession() == null }
        assertEquals(null, sessions.currentSession())

        viewModel.retry()
        await { jobs.retryFailedCalls == 1 }
        assertEquals(1, jobs.startCalls)
    }

    @Test
    fun temporaryPollingNetworkErrorRetriesRecoveryWithoutStartingAnotherJob() {
        val identity = AiReviewRequestIdentity("request-network", "job-network", "idem-network", TEST_USER)
        val sessions = FakeSessionStore()
        val jobs = TimeoutThenTerminalJobs(identity, terminalStatus = AiJobStatus.EXPIRED, firstRecoveryError = true)
        val viewModel = createViewModel(jobs = jobs, sessions = sessions, jobRecoveryRetryDelayMillis = 1L)

        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }
        generate(viewModel, "content://edital", "edital.pdf")

        await { viewModel.state.value.content is AiReviewContent.Failure }
        val failure = viewModel.state.value.content as AiReviewContent.Failure
        assertEquals(AiJobStatus.EXPIRED, failure.terminalStatus)
        assertEquals("A geração expirou. Você pode tentar novamente.", failure.message)
        assertEquals(3, jobs.recoverCalls)
        assertEquals(1, jobs.startCalls)
        assertEquals(0, jobs.retryFailedCalls)
        await { sessions.currentSession() == null }
        assertEquals(null, sessions.currentSession())
    }

    @Test
    fun recreatedViewModelRendersPersistedFailedJobAsTerminalError() {
        val requestId = "request-already-failed"
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", requestId, "job-failed", "idem-failed", ownerUserId = TEST_USER),
        )
        val jobs = TerminalRecoveryJobs(AiJobStatus.FAILED)
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)

        await { viewModel.state.value.content is AiReviewContent.Failure }

        val failure = viewModel.state.value.content as AiReviewContent.Failure
        assertEquals(AiJobStatus.FAILED, failure.terminalStatus)
        assertEquals("A geração não foi concluída. Você pode tentar novamente.", failure.message)
        assertEquals(listOf(requestId), jobs.recoveredRequests)
        await { sessions.currentSession() == null }
        assertEquals(null, sessions.currentSession())
    }

    @Test
    fun cancelledAndExpiredTerminalJobsClearTheActiveSessionWithFriendlyMessages() {
        listOf(
            AiJobStatus.CANCELLED to "A geração foi cancelada. Você pode tentar novamente.",
            AiJobStatus.EXPIRED to "A geração expirou. Você pode tentar novamente.",
        ).forEach { (status, expectedMessage) ->
            val sessions = FakeSessionStore(
                AiReviewPersistedSession(42L, "TRT-3", "request-${status.name}", "job-${status.name}", "idem-${status.name}", ownerUserId = TEST_USER),
            )
            val jobs = TerminalRecoveryJobs(status)
            val viewModel = createViewModel(jobs = jobs, sessions = sessions)

            await { viewModel.state.value.content is AiReviewContent.Failure }

            val failure = viewModel.state.value.content as AiReviewContent.Failure
            assertEquals(status, failure.terminalStatus)
            assertEquals(expectedMessage, failure.message)
            await { sessions.currentSession() == null }
            assertEquals(null, sessions.currentSession())
        }
    }

    @Test
    fun succeededWithoutProposalNeverFallsBackToProcessing() {
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", "request-invalid-success", "job-success", "idem-success", ownerUserId = TEST_USER),
        )
        val jobs = TerminalRecoveryJobs(AiJobStatus.SUCCEEDED, includeProposal = false)
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)

        await { viewModel.state.value.content is AiReviewContent.Failure }

        val failure = viewModel.state.value.content as AiReviewContent.Failure
        assertEquals(AiJobStatus.SUCCEEDED, failure.terminalStatus)
        assertEquals(false, failure.canRetry)
        assertEquals(1, jobs.recoveredRequests.size)
        await { sessions.currentSession() == null }
        assertEquals(null, sessions.currentSession())
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
        generate(viewModel, "content://after-login", "edital.pdf")
        await { jobs.startCalls == 1 }
        assertEquals(42L, jobs.targetIds.single())
    }

    @Test
    fun genericFailureAfterCreatedRequestRecoversSameIdentityWithoutNewGeneration() {
        val identity = AiReviewRequestIdentity("request-after-upload", "job-after-upload", "idem-after-upload", TEST_USER)
        val jobs = FailOnceAfterCreatedRequestJobs(identity)
        val sessions = FakeSessionStore()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { viewModel.state.value.access.kind == AiReviewAccessKind.READY }

        generate(viewModel, "content://edital", "edital.pdf")
        await { viewModel.state.value.content is AiReviewContent.Review }
        await { sessions.saved.lastOrNull()?.jobId == identity.jobId }
        assertEquals(identity.idempotencyKey, sessions.saved.last().idempotencyKey)

        assertEquals(1, jobs.startCalls)
        assertEquals(listOf(identity.requestId), jobs.recoveredRequests)
        assertEquals(identity, jobs.recoveredIdentity)
    }

    @Test
    fun genericFailureBeforeJobIdStillPersistsRequestIdentityAcrossRestart() {
        val pending = AiReviewPendingRequestIdentity("request-before-job", "idem-before-job", ownerUserId = TEST_USER)
        val sessions = FakeSessionStore()
        val firstJobs = PendingIdentityFailureJobs(pending)
        val firstViewModel = createViewModel(jobs = firstJobs, sessions = sessions)
        await { firstViewModel.state.value.access.kind == AiReviewAccessKind.READY }

        generate(firstViewModel, "content://edital", "edital.pdf")
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
                AiReviewPersistedSession(42L, "TRT-3", requestId, "job-original", "idem-original", ownerUserId = TEST_USER),
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
            AiReviewPersistedSession(42L, "TRT-3", requestId, "job-original", "idem-original", ownerUserId = TEST_USER),
        )
        val jobs = NonterminalRetryJobs()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { jobs.recoveredRequests.size == 1 && viewModel.state.value.content is AiReviewContent.Processing }

        viewModel.retry()
        assertEquals(1, jobs.recoveredRequests.size)

        assertEquals(emptyList<String>(), jobs.retryFailedRequests)
        assertEquals(listOf(requestId), jobs.recoveredRequests)
        assertEquals("job-original", (viewModel.state.value.content as AiReviewContent.Processing).jobId)
        assertEquals("idem-original", (viewModel.state.value.content as AiReviewContent.Processing).idempotencyKey)
    }

    @Test
    fun retryFailedErrorRetainsTerminalRetryPathAndRequestIdentity() {
        val requestId = "request-terminal-retry"
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", requestId, "job-original", "idem-original", ownerUserId = TEST_USER),
        )
        val jobs = FailOnceTerminalRetryJobs()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { viewModel.state.value.content is AiReviewContent.Failure }

        viewModel.retry()
        await { (viewModel.state.value.content as? AiReviewContent.Failure)?.message == "temporary retry failure" }
        assertEquals(listOf(requestId), jobs.retryFailedRequests)

        viewModel.retry()
        await { jobs.retryFailedRequests.size > 1 || jobs.recoveredRequests.size > 1 }
        assertEquals(listOf(requestId), jobs.retryFailedRequests)
        assertEquals(listOf(requestId, requestId), jobs.recoveredRequests)
        await { viewModel.state.value.content is AiReviewContent.Processing }
        await { sessions.saved.lastOrNull()?.idempotencyKey == "idem-original" }
        assertEquals(requestId, sessions.saved.last().requestId)
    }

    @Test
    fun retryAfterPartialTerminalRetryUsesCurrentPersistedAttemptInsteadOfStaleFailureStatus() {
        val requestId = "request-partial-retry"
        val sessions = FakeSessionStore(
            AiReviewPersistedSession(42L, "TRT-3", requestId, "job-old", "idem-old", ownerUserId = TEST_USER),
        )
        val jobs = PartialTerminalRetryJobs()
        val viewModel = createViewModel(jobs = jobs, sessions = sessions)
        await { viewModel.state.value.content is AiReviewContent.Failure }
        assertEquals(AiJobStatus.CANCELLED, (viewModel.state.value.content as AiReviewContent.Failure).terminalStatus)

        viewModel.retry()
        await { (viewModel.state.value.content as? AiReviewContent.Failure)?.message == "process failed after reservation" }
        assertEquals(AiJobStatus.RESERVED, jobs.persistedStatus)

        viewModel.retry()
        await { jobs.recoveredRequests.size == 2 }
        await { viewModel.state.value.content is AiReviewContent.Processing }
        assertEquals(listOf(requestId), jobs.retryFailedRequests)
        assertEquals(listOf(requestId, requestId), jobs.recoveredRequests)
        assertEquals("job-new", (viewModel.state.value.content as AiReviewContent.Processing).jobId)
        assertEquals("idem-new", (viewModel.state.value.content as AiReviewContent.Processing).idempotencyKey)
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
            generate(viewModel, "content://edital", "edital.pdf")
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
        jobRecoveryRetryDelayMillis: Long = 2_000L,
        userIdProvider: () -> String? = { TEST_USER },
        userIdFlow: Flow<String?> = flowOf(TEST_USER),
        initialTarget: AiReviewTarget? = null,
    ) = AiReviewViewModel(
        application = ApplicationProvider.getApplicationContext(),
        targetId = 42L,
        targetTitle = initialTarget?.title ?: "TRT-3",
        jobs = jobs,
        applier = applier,
        sessions = sessions,
        targetStore = targetStore,
        accessGateway = access,
        loginLauncher = loginLauncher,
        syncState = syncState,
        syncPollDelayMillis = syncPollDelayMillis,
        jobRecoveryRetryDelayMillis = jobRecoveryRetryDelayMillis,
        userIdProvider = userIdProvider,
        userIdFlow = userIdFlow,
        initialTarget = initialTarget,
    )

    private fun generate(viewModel: AiReviewViewModel, uri: String, name: String) {
        viewModel.updatePreferences(br.com.estudario.data.ai.AiSyllabusPreferences("TRT-3", "Analista"))
        viewModel.start(uri, name)
        await { viewModel.state.value.content is AiReviewContent.Confirmation }
        viewModel.confirmGeneration()
    }

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
        private var current = initial?.copy(ownerUserId = initial.ownerUserId ?: TEST_USER)

        fun currentSession(): AiReviewPersistedSession? = current

        override suspend fun load(targetId: Long): AiReviewPersistedSession? = current?.takeIf { it.targetId == targetId }
        override suspend fun loadLatest(): AiReviewPersistedSession? = current
        override suspend fun load(targetId: Long, ownerUserId: String): AiReviewPersistedSession? =
            current?.takeIf { it.targetId == targetId && it.ownerUserId == ownerUserId }
        override suspend fun loadLatest(ownerUserId: String): AiReviewPersistedSession? =
            current?.takeIf { it.ownerUserId == ownerUserId && !it.applied }
        override suspend fun save(session: AiReviewPersistedSession) { current = session; saved += session }
        override suspend fun clear(targetId: Long) { if (current?.targetId == targetId) current = null }
        override suspend fun clear(targetId: Long, ownerUserId: String) {
            if (current?.targetId == targetId && current?.ownerUserId == ownerUserId) current = null
        }
    }

    private inner class TimeoutThenTerminalJobs(
        private val identity: AiReviewRequestIdentity,
        private val terminalStatus: AiJobStatus,
        private val firstRecoveryError: Boolean = false,
    ) : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        var startCalls = 0
        var recoverCalls = 0
        var retryFailedCalls = 0

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted {
            startCalls += 1
            throw AiReviewStartException(identity, br.com.estudario.data.ai.AiProcessTimeoutException(identity.jobId))
        }

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoverCalls += 1
            if (firstRecoveryError && recoverCalls == 1) throw br.com.estudario.data.ai.AiApiException("NETWORK_UNAVAILABLE", 503)
            val status = if (recoverCalls == 1 || (firstRecoveryError && recoverCalls == 2)) AiJobStatus.PROCESSING else terminalStatus
            val result = job(status).copy(
                jobId = identity.jobId,
                errorCode = if (status == terminalStatus) "PROVIDER_RESULT_UNAVAILABLE" else null,
                errorMessage = if (status == terminalStatus) "The AI job did not complete" else null,
            )
            return AiReviewStarted(result, identity)
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted {
            retryFailedCalls += 1
            return AiReviewStarted(job(AiJobStatus.PROCESSING).copy(jobId = "job-manual-retry"), identity.copy(jobId = "job-manual-retry"))
        }

        override suspend fun resumeOrRetry(requestId: String): AiReviewStarted = retryFailed(requestId)
        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = identity.takeIf { it.jobId == jobId }
    }

    private inner class TerminalRecoveryJobs(
        private val status: AiJobStatus,
        private val includeProposal: Boolean = true,
    ) : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        val recoveredRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = error("must not start another job")
        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            val value = job(status).copy(
                jobId = "job-terminal",
                proposal = if (status == AiJobStatus.SUCCEEDED && includeProposal) draft().proposal else null,
                errorCode = if (status == AiJobStatus.FAILED) "PROVIDER_RESULT_UNAVAILABLE" else null,
                errorMessage = if (status == AiJobStatus.FAILED) "The AI job did not complete" else null,
            )
            return AiReviewStarted(value, AiReviewRequestIdentity(requestId, "job-terminal", "idem-terminal", TEST_USER))
        }
        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("manual retry not expected")
        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
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
        private val identityOwner: String = TEST_USER,
    ) : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        var startCalls = 0
        val targetIds = mutableListOf<Long>()
        val uris = mutableListOf<String>()
        val recoveredRequests = mutableListOf<String>()
        val release = CompletableDeferred<Unit>()
        var sentPreferences: br.com.estudario.data.ai.AiSyllabusPreferences? = null
        val confirmedAttempts = mutableListOf<String>()
        var nextStatus = AiJobStatus.SUCCEEDED

        override suspend fun startPrepared(targetId: Long, targetTitle: String, source: br.com.estudario.data.ai.PreparedSyllabusSource, preferences: br.com.estudario.data.ai.AiSyllabusPreferences, onRequestPersisted: suspend (br.com.estudario.data.ai.PersistedAiJobRequest) -> Unit): AiReviewStarted {
            sentPreferences = preferences
            confirmedAttempts += source.attemptId
            return start(targetId, source.uri, source.fileName)
        }

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted {
            beforeStart()
            startCalls += 1
            targetIds += targetId
            uris += uri
            if (blockStart) release.await()
            return AiReviewStarted(job(nextStatus), AiReviewRequestIdentity("request-1", "job-1", "idem-1", identityOwner))
        }

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            return AiReviewStarted(job(AiJobStatus.PROCESSING), AiReviewRequestIdentity(requestId, "job-1", "idem-1", identityOwner))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("terminal retry not expected")

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = AiReviewRequestIdentity("request-1", jobId, "idem-1", identityOwner)
    }

    private inner class FailOnceAfterCreatedRequestJobs(
        private val identity: AiReviewRequestIdentity,
    ) : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
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
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        var startCalls = 0
        val recoveredRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted {
            startCalls += 1
            if (failStart) throw AiReviewStartException(pending, IllegalStateException("request persisted before transport"))
            return AiReviewStarted(job(AiJobStatus.SUCCEEDED), pending.copy(ownerUserId = TEST_USER).asStartedIdentity()!!)
        }

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            return AiReviewStarted(job(AiJobStatus.SUCCEEDED), pending.copy(ownerUserId = TEST_USER).asStartedIdentity() ?: AiReviewRequestIdentity(requestId, "job-recovered", pending.idempotencyKey, TEST_USER))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted = error("terminal retry not expected")

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private inner class RetryDispatchJobs(
        private val terminalStatus: AiJobStatus,
    ) : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        val recoveredRequests = mutableListOf<String>()
        val retryFailedRequests = mutableListOf<String>()
        private var currentStatus = terminalStatus

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = error("not expected")

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            val status = if (recoveredRequests.size == 1) terminalStatus else AiJobStatus.PROCESSING
            return AiReviewStarted(job(status).copy(jobId = "job-original"), AiReviewRequestIdentity(requestId, "job-original", "idem-original", TEST_USER))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted {
            retryFailedRequests += requestId
            currentStatus = AiJobStatus.PROCESSING
            return AiReviewStarted(job(AiJobStatus.PROCESSING).copy(jobId = "job-retry"), AiReviewRequestIdentity(requestId, "job-retry", "idem-retry", TEST_USER))
        }

        override suspend fun resumeOrRetry(requestId: String): AiReviewStarted =
            if (currentStatus in setOf(AiJobStatus.FAILED, AiJobStatus.EXPIRED, AiJobStatus.CANCELLED)) retryFailed(requestId)
            else recover(requestId)

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private inner class NonterminalRetryJobs : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        val recoveredRequests = mutableListOf<String>()
        val retryFailedRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = error("not expected")

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            return AiReviewStarted(job(AiJobStatus.PROCESSING).copy(jobId = "job-original"), AiReviewRequestIdentity(requestId, "job-original", "idem-original", TEST_USER))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted {
            retryFailedRequests += requestId
            error("nonterminal job must be recovered")
        }

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private inner class FailOnceTerminalRetryJobs : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        val recoveredRequests = mutableListOf<String>()
        val retryFailedRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = error("not expected")

        override suspend fun recover(requestId: String): AiReviewStarted {
            recoveredRequests += requestId
            val status = if (recoveredRequests.size == 1) AiJobStatus.FAILED else AiJobStatus.PROCESSING
            return AiReviewStarted(job(status).copy(jobId = "job-original"), AiReviewRequestIdentity(requestId, "job-original", "idem-original", TEST_USER))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted {
            retryFailedRequests += requestId
            if (retryFailedRequests.size == 1) throw IllegalStateException("temporary retry failure")
            return AiReviewStarted(job(AiJobStatus.PROCESSING).copy(jobId = "job-retry"), AiReviewRequestIdentity(requestId, "job-retry", "idem-retry", TEST_USER))
        }

        override suspend fun resumeOrRetry(requestId: String): AiReviewStarted =
            if (retryFailedRequests.size < 1) retryFailed(requestId) else recover(requestId)

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private inner class PartialTerminalRetryJobs : AiReviewJobs {
        override suspend fun prepare(target: AiReviewTarget) = preparedFixture(target)
        var persistedStatus = AiJobStatus.CANCELLED
        val recoveredRequests = mutableListOf<String>()
        val retryFailedRequests = mutableListOf<String>()

        override suspend fun start(targetId: Long, uri: String, fileName: String?): AiReviewStarted = error("not expected")

        override suspend fun recover(requestId: String): AiReviewStarted {
            if (recoveredRequests.isEmpty()) {
                recoveredRequests += requestId
                return AiReviewStarted(job(AiJobStatus.CANCELLED).copy(jobId = "job-old"), AiReviewRequestIdentity(requestId, "job-old", "idem-old", TEST_USER))
            }
            recoveredRequests += requestId
            return AiReviewStarted(job(AiJobStatus.PROCESSING).copy(jobId = "job-new"), AiReviewRequestIdentity(requestId, "job-new", "idem-new", TEST_USER))
        }

        override suspend fun retryFailed(requestId: String): AiReviewStarted {
            retryFailedRequests += requestId
            persistedStatus = AiJobStatus.RESERVED
            throw IllegalStateException("process failed after reservation")
        }

        override suspend fun resumeOrRetry(requestId: String): AiReviewStarted =
            if (persistedStatus in setOf(AiJobStatus.FAILED, AiJobStatus.EXPIRED, AiJobStatus.CANCELLED)) retryFailed(requestId)
            else recover(requestId)

        override suspend fun recoverPending(): List<AiReviewStarted> = emptyList()
        override suspend fun identityForJob(jobId: String): AiReviewRequestIdentity? = null
    }

    private companion object { const val TEST_USER = "review-test-user" }

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
