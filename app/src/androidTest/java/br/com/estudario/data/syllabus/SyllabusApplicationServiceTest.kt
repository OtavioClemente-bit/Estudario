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
import br.com.estudario.data.local.SubjectEntity
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
    fun successfulLocalApplyStaysPendingUntilRemoteAcknowledgement() = runDatabase { database ->
            val targetId = database.dao().insertCompetition(CompetitionEntity(name = "Edital local", remoteSyllabusId = "remote-9"))
            val result = SyllabusApplicationService(database).applyReviewedSyllabus(targetId, draft(targetId, "Documento"), "job-pending")

            val row = database.dao().remoteSyllabusSyncById(result.outboxId)
            assertEquals(RemoteSyllabusSyncState.PENDING, row?.state)
            assertNotEquals(RemoteSyllabusSyncState.SYNCED, row?.state)
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
