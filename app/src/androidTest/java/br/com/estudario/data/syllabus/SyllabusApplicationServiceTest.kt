package br.com.estudario.data.syllabus

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncOperation
import br.com.estudario.data.local.RemoteSyllabusSyncError
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.ai.AiSyllabusDraft
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.security.MessageDigest

@RunWith(AndroidJUnit4::class)
class SyllabusApplicationServiceTest {
    @Test
    fun appliesOfficialPackageKeepingTargetIdentityAndPendingOutbox() = runDatabase { database ->
            val targetId = database.dao().insertCompetition(CompetitionEntity(name = "Edital local", remoteSyllabusId = "remote-41"))
            val service = SyllabusApplicationService(database)
            val draft = draft(targetId, "Edital detectado")

            val result = service.applyReviewedSyllabus(targetId, draft, "job-41", now = 100L)

            assertEquals(targetId, result.localSyllabusId)
            assertTrue(result.created)
            assertEquals(targetId, database.dao().competitionsOnce().single().id)
            assertEquals("remote-41", database.dao().competitionsOnce().single().remoteSyllabusId)
            val outbox = database.dao().pendingRemoteSyllabusSync(100L).single()
            assertEquals("job-41", outbox.jobId)
            assertEquals("remote-41", outbox.remoteSyllabusId)
            assertEquals(RemoteSyllabusSyncState.PENDING, outbox.state)
            assertEquals(sha256(result.packageJson), outbox.payloadHash)
            assertEquals(result.packageJson, outbox.payloadJson)
    }

    @Test
    fun duplicateSourceJobDoesNotReapplyOrDuplicateOutbox() = runDatabase { database ->
            val targetId = database.dao().insertCompetition(CompetitionEntity(name = "Edital local"))
            val service = SyllabusApplicationService(database)
            val draft = draft(targetId, "Documento")

            val first = service.applyReviewedSyllabus(targetId, draft, "job-duplicate", now = 100L)
            val second = service.applyReviewedSyllabus(targetId, draft, "job-duplicate", now = 200L)

            assertTrue(second.alreadyApplied)
            assertEquals(first.outboxId, second.outboxId)
            assertEquals(1, database.dao().pendingRemoteSyllabusSync(200L).size)
            assertEquals(1, database.dao().subjectsFor(targetId).size)
            assertEquals(first.packageJson, second.packageJson)
    }

    @Test
    fun sameSourceJobWithDifferentPayloadIsRejectedWithoutMutation() = runDatabase { database ->
        val targetId = database.dao().insertCompetition(CompetitionEntity(name = "Edital local"))
        val service = SyllabusApplicationService(database)
        val first = service.applyReviewedSyllabus(targetId, draft(targetId, "Documento"), "job-conflict", now = 100L)
        val beforeTarget = database.dao().competitionsOnce().single()
        val beforeSubjects = database.dao().subjectsFor(targetId)
        val beforeOutbox = database.dao().remoteSyllabusSyncById(first.outboxId)

        assertThrows(SyllabusSourceJobConflictException::class.java) {
            runBlocking {
                service.applyReviewedSyllabus(targetId, draft(targetId, "Documento alterado"), "job-conflict", now = 200L)
            }
        }

        assertEquals(beforeTarget, database.dao().competitionsOnce().single())
        assertEquals(beforeSubjects, database.dao().subjectsFor(targetId))
        assertEquals(beforeOutbox, database.dao().remoteSyllabusSyncById(first.outboxId))
        assertEquals(1, database.dao().pendingRemoteSyllabusSync(200L).size)
    }

    @Test
    fun existingContentRequiresExplicitReplacement() = runDatabase { database ->
            val targetId = database.dao().insertCompetition(CompetitionEntity(name = "Edital local"))
            database.dao().insertSubject(SubjectEntity(competitionId = targetId, name = "Conteúdo antigo", externalId = "old-subject"))
            val service = SyllabusApplicationService(database)

            assertThrows(ExistingSyllabusContentException::class.java) {
                runBlocking { service.applyReviewedSyllabus(targetId, draft(targetId, "Novo"), "job-no-replace") }
            }
            assertEquals(listOf("Conteúdo antigo"), database.dao().subjectsFor(targetId).map { it.name })

            service.applyReviewedSyllabus(targetId, draft(targetId, "Novo"), "job-replace", replaceExisting = true)

            assertEquals(listOf("Direito Constitucional"), database.dao().subjectsFor(targetId).map { it.name })
    }

    @Test
    fun replacementRollsBackWhenOfficialApplicationFailsMidTransaction() = runDatabase { database ->
            val targetId = database.dao().insertCompetition(CompetitionEntity(name = "Edital local"))
            database.dao().insertSubject(SubjectEntity(competitionId = targetId, name = "Conteúdo antigo", externalId = "old-subject"))
            val otherId = database.dao().insertCompetition(CompetitionEntity(name = "Outro edital"))
            database.dao().insertSubject(SubjectEntity(competitionId = otherId, name = "Conflito", externalId = "conflicting-subject"))
            val conflictingDraft = draft(targetId, "Novo").copy(
                subjects = draft(targetId, "Novo").subjects.map { it.copy(externalId = "conflicting-subject") },
            )
            val service = SyllabusApplicationService(database)

            assertThrows(IllegalArgumentException::class.java) {
                runBlocking { service.applyReviewedSyllabus(targetId, conflictingDraft, "job-rollback", replaceExisting = true) }
            }

            assertEquals(listOf("Conteúdo antigo"), database.dao().subjectsFor(targetId).map { it.name })
            assertTrue(database.dao().pendingRemoteSyllabusSync(0L).isEmpty())
    }

    @Test
    fun replacementRollsBackAfterRealLocalMutationInsideTransaction() = runDatabase { database ->
        val target = CompetitionEntity(
            name = "Edital local",
            externalId = "competition-local",
            remoteSyllabusId = "remote-existing",
        )
        val targetId = database.dao().insertCompetition(target)
        val oldSubjectId = database.dao().insertSubject(
            SubjectEntity(competitionId = targetId, name = "Conteúdo antigo", externalId = "old-subject"),
        )
        val oldTopicId = database.dao().insertTopic(
            TopicEntity(subjectId = oldSubjectId, title = "Tópico antigo", externalId = "old-topic"),
        )
        val oldOutboxId = database.dao().enqueueRemoteSyllabusSync(
            RemoteSyllabusSyncEntity(
                operation = RemoteSyllabusSyncOperation.UPSERT,
                localSyllabusId = targetId,
                remoteSyllabusId = "remote-existing",
                jobId = "old-job",
                payloadHash = "old-hash",
                payloadJson = "{\"version\":2,\"old\":true}",
                nextAttemptAt = 0L,
            ),
        )
        val beforeTarget = database.dao().competitionsOnce().single()
        val beforeSubjects = database.dao().subjectsFor(targetId)
        val beforeTopics = database.dao().topicsOnce()
        val beforeOutbox = database.dao().remoteSyllabusSyncById(oldOutboxId)
        var hookReached = false
        val service = SyllabusApplicationService(database) {
            hookReached = true
            error("injected-after-local-mutation")
        }

        assertThrows(IllegalStateException::class.java) {
            runBlocking {
                service.applyReviewedSyllabus(
                    targetId,
                    draft(targetId, "Novo"),
                    "job-after-mutation-failure",
                    replaceExisting = true,
                    now = 300L,
                )
            }
        }

        assertTrue(hookReached)
        assertEquals(beforeTarget, database.dao().competitionsOnce().single())
        assertEquals(beforeSubjects, database.dao().subjectsFor(targetId))
        assertEquals(beforeTopics, database.dao().topicsOnce())
        assertEquals(oldTopicId, database.dao().topicsFor(oldSubjectId).single().id)
        assertEquals(beforeOutbox, database.dao().remoteSyllabusSyncById(oldOutboxId))
        assertEquals(1, database.dao().pendingRemoteSyllabusSync(300L).size)
    }

    @Test
    fun successfulLocalApplyStaysPendingUntilRemoteAcknowledgement() = runDatabase { database ->
            val targetId = database.dao().insertCompetition(CompetitionEntity(name = "Edital local", remoteSyllabusId = "remote-9"))
            val result = SyllabusApplicationService(database).applyReviewedSyllabus(targetId, draft(targetId, "Documento"), "job-pending")

            val row = database.dao().remoteSyllabusSyncById(result.outboxId)
            assertEquals(RemoteSyllabusSyncState.PENDING, row?.state)
            assertNotEquals(RemoteSyllabusSyncState.SYNCED, row?.state)
    }

    @Test
    fun explicitReplacementSupersedesOlderPendingAndFailedOutboxRows() = runDatabase { database ->
        val targetId = database.dao().insertCompetition(
            CompetitionEntity(name = "Edital local", remoteSyllabusId = "remote-replacement"),
        )
        val service = SyllabusApplicationService(database)
        val first = service.applyReviewedSyllabus(targetId, draft(targetId, "Documento antigo"), "job-old", now = 100L)
        assertEquals(
            1,
            database.dao().markRemoteSyncAttempt(
                first.outboxId,
                expectedAttemptToken = "",
                attemptToken = "old-pending-token",
                nextAttemptAt = 200L,
                updatedAt = 150L,
            ),
        )
        val oldFailedId = database.dao().enqueueRemoteSyllabusSync(
            RemoteSyllabusSyncEntity(
                operation = RemoteSyllabusSyncOperation.UPSERT,
                localSyllabusId = targetId,
                remoteSyllabusId = "remote-replacement",
                jobId = "job-old-failed",
                payloadHash = "old-failed-hash",
                payloadJson = "{\"version\":2,\"old\":\"failed\"}",
                state = RemoteSyllabusSyncState.FAILED,
                attemptToken = "old-failed-token",
                lastError = RemoteSyllabusSyncError.NETWORK,
                nextAttemptAt = 100L,
            ),
        )

        val replacement = service.applyReviewedSyllabus(
            targetId,
            draft(targetId, "Documento novo"),
            "job-replacement",
            replaceExisting = true,
            now = 300L,
        )

        assertEquals(listOf(replacement.outboxId), database.dao().pendingRemoteSyllabusSync(300L).map { it.id })
        assertTrue(database.dao().failedRemoteSyllabusSync(300L).isEmpty())
        assertEquals(0, database.dao().markRemoteSyncSynced(first.outboxId, "old-pending-token", 400L))
        assertEquals(0, database.dao().requeueRemoteSync(oldFailedId, "old-failed-token", "late-retry", 500L, 500L))
        assertEquals(RemoteSyllabusSyncState.PENDING, database.dao().remoteSyllabusSyncById(replacement.outboxId)?.state)
        assertEquals(RemoteSyllabusSyncError.SUPERSEDED, database.dao().remoteSyllabusSyncById(first.outboxId)?.lastError)
        assertEquals(RemoteSyllabusSyncError.SUPERSEDED, database.dao().remoteSyllabusSyncById(oldFailedId)?.lastError)
        assertEquals(first.packageJson, database.dao().remoteSyllabusSyncById(first.outboxId)?.payloadJson)
        assertEquals("{\"version\":2,\"old\":\"failed\"}", database.dao().remoteSyllabusSyncById(oldFailedId)?.payloadJson)
    }

    @Test
    fun canonicalPayloadSurvivesDatabaseRestart() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val databaseName = "task-11-canonical-payload-restart.db"
        context.deleteDatabase(databaseName)
        val firstDatabase = Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
            .allowMainThreadQueries()
            .build()
        try {
            val targetId = firstDatabase.dao().insertCompetition(CompetitionEntity(name = "Edital local"))
            val result = SyllabusApplicationService(firstDatabase).applyReviewedSyllabus(
                targetId,
                draft(targetId, "Documento persistido"),
                "job-restart",
            )
            firstDatabase.close()

            val reopened = Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .allowMainThreadQueries()
                .build()
            try {
                val restored = reopened.dao().remoteSyllabusSyncById(result.outboxId)!!
                assertEquals(result.packageJson, restored.payloadJson)
                assertEquals(result.payloadHash, sha256(restored.payloadJson))
            } finally {
                reopened.close()
            }
        } finally {
            if (firstDatabase.isOpen) firstDatabase.close()
            context.deleteDatabase(databaseName)
        }
    }

    private fun runDatabase(block: suspend (AppDatabase) -> Unit) = runBlocking {
        val database = createDatabase()
        try {
            block(database)
        } finally {
            database.close()
        }
    }

    private fun createDatabase(): AppDatabase = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private fun draft(targetId: Long, documentTitle: String): AiSyllabusDraft = AiSyllabusDraft.fromProposal(
        targetSyllabusId = targetId,
        targetTitle = "Edital local",
        proposal = AiSyllabusProposal(
            schemaVersion = 1,
            promptVersion = "syllabus-v1",
            modelVersion = "gpt-6-luna",
            documentTitle = documentTitle,
            subjects = listOf(
                AiSubjectProposal(
                    name = "Direito Constitucional",
                    position = 0,
                    suggestedPriority = AiPriority.NORMAL,
                    topics = listOf(AiTopicProposal("Direitos fundamentais", 0, emptyList(), emptyList())),
                    sourcePages = emptyList(),
                ),
            ),
            warnings = emptyList(),
            ambiguities = emptyList(),
        ),
    )

    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray())
        .joinToString("") { "%02x".format(it) }
}
