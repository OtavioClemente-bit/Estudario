package br.com.estudario.data.syllabus

import androidx.room.withTransaction
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncOperation
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.data.transfer.EstudoPackageService
import br.com.estudario.data.transfer.ImportMode
import br.com.estudario.domain.ai.AiSyllabusDraft
import br.com.estudario.domain.ai.AiSyllabusProposalValidator
import br.com.estudario.domain.ai.AiSyllabusToEstudoMapper
import java.security.MessageDigest
import java.util.UUID

class ExistingSyllabusContentException(message: String) : IllegalStateException(message)

class SyllabusSourceJobConflictException(message: String) : IllegalArgumentException(message)

data class ApplyResult(
    val localSyllabusId: Long,
    val sourceJobId: String,
    val packageJson: String,
    val payloadHash: String,
    val outboxId: Long,
    val remoteSyllabusId: String?,
    val state: RemoteSyllabusSyncState,
    val created: Boolean,
    val alreadyApplied: Boolean,
)

class SyllabusApplicationService(
    private val database: AppDatabase,
    private val afterLocalApply: suspend () -> Unit = {},
    private val beforeTransaction: suspend () -> Unit = {},
) {
    private val dao = database.dao()
    private val packageService = EstudoPackageService(database)

    suspend fun findAppliedSyllabus(targetSyllabusId: Long, sourceJobId: String): ApplyResult? {
        require(targetSyllabusId > 0L) { "targetSyllabusId must be positive" }
        val normalizedJobId = sourceJobId.trim()
        if (normalizedJobId.isEmpty()) return null
        val existing = dao.remoteSyllabusSyncByJobId(normalizedJobId) ?: return null
        if (existing.jobId != normalizedJobId ||
            existing.localSyllabusId != targetSyllabusId ||
            existing.operation != RemoteSyllabusSyncOperation.UPSERT
        ) {
            return null
        }
        return existing.toResult(normalizedJobId, alreadyApplied = true)
    }

    suspend fun applyReviewedSyllabus(
        targetSyllabusId: Long,
        draft: AiSyllabusDraft,
        sourceJobId: String,
        replaceExisting: Boolean = false,
        now: Long = System.currentTimeMillis(),
    ): ApplyResult {
        val normalizedJobId = sourceJobId.trim()
        require(normalizedJobId.isNotEmpty()) { "sourceJobId must be non-empty" }
        val checkedDraft = AiSyllabusProposalValidator.validateDraft(draft)
        require(checkedDraft.targetSyllabusId == targetSyllabusId) {
            "draft.targetSyllabusId must match targetSyllabusId"
        }

        beforeTransaction()
        return database.withTransaction {
            val target = dao.competitionsOnce().firstOrNull { it.id == targetSyllabusId }
                ?: throw IllegalArgumentException("The selected syllabus was not found.")
            val boundDraft = AiSyllabusProposalValidator.bindToTarget(checkedDraft, target.id, target.name)
            val packageJson = AiSyllabusToEstudoMapper.toOfficialPackage(boundDraft)
            val payloadHash = sha256(packageJson)

            dao.remoteSyllabusSyncByJobId(normalizedJobId)?.let { existing ->
                if (existing.localSyllabusId != target.id) {
                    throw IllegalArgumentException("sourceJobId is already associated with another syllabus.")
                }
                if (existing.payloadHash != payloadHash) {
                    throw SyllabusSourceJobConflictException("sourceJobId was already applied with a different payload hash.")
                }
                return@withTransaction existing.ensureCanonicalPayload(packageJson)
                    .toResult(normalizedJobId, alreadyApplied = true)
            }

            dao.remoteSyllabusSyncByPayload(
                localSyllabusId = target.id,
                operation = RemoteSyllabusSyncOperation.UPSERT,
                payloadHash = payloadHash,
            )?.let { existing ->
                return@withTransaction existing.ensureCanonicalPayload(packageJson)
                    .toResult(existing.jobId ?: normalizedJobId, alreadyApplied = true)
            }

            if (dao.subjectsFor(target.id).isNotEmpty()) {
                if (!replaceExisting) {
                    throw ExistingSyllabusContentException("The selected syllabus already has content; replacement must be explicit.")
                }
                clearExistingContent(target.id)
            }

            packageService.importInTransaction(
                text = packageJson,
                mode = ImportMode.SKIP,
                targetCompetitionId = target.id,
            )
            afterLocalApply()

            val currentTarget = dao.competitionsOnce().first { it.id == target.id }
            if (replaceExisting) {
                dao.supersedeRemoteSyllabusSync(
                    localSyllabusId = target.id,
                    supersessionToken = "superseded-${UUID.randomUUID()}",
                    updatedAt = now,
                )
            }
            val mutation = RemoteSyllabusSyncEntity(
                operation = RemoteSyllabusSyncOperation.UPSERT,
                localSyllabusId = target.id,
                remoteSyllabusId = currentTarget.remoteSyllabusId,
                jobId = normalizedJobId,
                payloadHash = payloadHash,
                payloadJson = packageJson,
                state = RemoteSyllabusSyncState.PENDING,
                nextAttemptAt = now,
                createdAt = now,
                updatedAt = now,
            )
            val insertedId = dao.enqueueRemoteSyllabusSync(mutation)
            val outbox = if (insertedId == -1L) {
                dao.remoteSyllabusSyncByPayload(target.id, RemoteSyllabusSyncOperation.UPSERT, payloadHash)
                    ?: error("The syllabus sync mutation was not persisted.")
            } else {
                dao.remoteSyllabusSyncById(insertedId) ?: error("The syllabus sync mutation was not persisted.")
            }
            outbox.toResult(normalizedJobId, alreadyApplied = false)
        }
    }

    private suspend fun RemoteSyllabusSyncEntity.ensureCanonicalPayload(candidate: String): RemoteSyllabusSyncEntity {
        if (payloadJson.isNotEmpty()) return this
        dao.persistRemoteSyllabusPayloadIfMissing(id, payloadHash, candidate)
        return dao.remoteSyllabusSyncById(id) ?: error("The syllabus sync mutation was not persisted.")
    }

    private suspend fun clearExistingContent(competitionId: Long) {
        val subjectIds = dao.subjectsFor(competitionId).map { it.id }
        val topicIds = dao.topicsOnce().filter { it.subjectId in subjectIds }.map { it.id }
        if (topicIds.isNotEmpty()) {
            dao.deleteQueueForTopics(topicIds)
            dao.deleteReviewHistoryForTopics(topicIds)
            dao.deleteStudySessionsForTopics(topicIds)
            dao.deleteNotesForTopics(topicIds)
            dao.deleteQuestionTagsForTopics(topicIds)
        }
        dao.deleteSubjectsForCompetition(competitionId)
    }

    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

    private fun RemoteSyllabusSyncEntity.toResult(sourceJobId: String, alreadyApplied: Boolean) = ApplyResult(
        localSyllabusId = localSyllabusId,
        sourceJobId = sourceJobId,
        packageJson = payloadJson,
        payloadHash = payloadHash,
        outboxId = id,
        remoteSyllabusId = remoteSyllabusId,
        state = state,
        created = !alreadyApplied,
        alreadyApplied = alreadyApplied,
    )
}
