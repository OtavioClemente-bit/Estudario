package br.com.estudario.ui.ai

import br.com.estudario.data.ai.AiAccess
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.domain.ai.AiSyllabusDraft
import br.com.estudario.domain.ai.AiSyllabusDraftTopic

enum class AiReviewAccessKind { LOADING, UNAUTHENTICATED, DENIED, READY }

data class AiReviewAccessState(
    val kind: AiReviewAccessKind,
    val reasonCode: String? = null,
    val access: AiAccess? = null,
) {
    companion object {
        val LOADING = AiReviewAccessState(AiReviewAccessKind.LOADING)
        val UNAUTHENTICATED = AiReviewAccessState(AiReviewAccessKind.UNAUTHENTICATED, "UNAUTHENTICATED")
        val READY = AiReviewAccessState(AiReviewAccessKind.READY)

        fun denied(reasonCode: String?, access: AiAccess? = null) = AiReviewAccessState(
            AiReviewAccessKind.DENIED,
            reasonCode?.ifBlank { "ACCESS_DENIED" } ?: "ACCESS_DENIED",
            access,
        )
    }
}

data class AiReviewTarget(
    val id: Long,
    val title: String,
    val sourceUri: String? = null,
    val sourceName: String? = null,
)

data class AiReviewSource(val uri: String, val fileName: String?)

data class AiReviewRequestIdentity(
    val requestId: String,
    val jobId: String,
    val idempotencyKey: String,
)

data class AiReviewPendingRequestIdentity(
    val requestId: String,
    val idempotencyKey: String,
    val jobId: String? = null,
) {
    fun asStartedIdentity(): AiReviewRequestIdentity? = jobId?.let { AiReviewRequestIdentity(requestId, it, idempotencyKey) }
}

object AiReviewRecovery {
    fun afterTimeout(identity: AiReviewRequestIdentity): AiReviewRequestIdentity = identity
}

sealed interface AiReviewContent {
    data object Gate : AiReviewContent
    data class Processing(
        val jobId: String,
        val idempotencyKey: String,
    ) : AiReviewContent
    data class Review(
        val draft: AiSyllabusDraft,
        val validationError: String? = null,
        val confirmReplacement: Boolean = false,
    ) : AiReviewContent
    data class Failure(val message: String, val canRetry: Boolean = true) : AiReviewContent
    data class Applied(val syncState: RemoteSyllabusSyncState) : AiReviewContent
}

data class AiReviewUiState(
    val targetSyllabusId: Long,
    val targetTitle: String,
    val content: AiReviewContent,
    val access: AiReviewAccessState = AiReviewAccessState.UNAUTHENTICATED,
) {
    companion object {
        fun gate(
            targetId: Long,
            targetTitle: String,
            access: AiReviewAccessState = AiReviewAccessState.UNAUTHENTICATED,
        ) = AiReviewUiState(targetId, targetTitle, AiReviewContent.Gate, access)

        fun processing(
            targetId: Long,
            targetTitle: String,
            jobId: String,
            idempotencyKey: String,
            access: AiReviewAccessState = AiReviewAccessState.READY,
        ) = AiReviewUiState(
            targetId,
            targetTitle,
            AiReviewContent.Processing(jobId, idempotencyKey),
            access,
        )

        fun review(
            targetId: Long,
            targetTitle: String,
            draft: AiSyllabusDraft,
            confirmReplacement: Boolean = false,
            access: AiReviewAccessState = AiReviewAccessState.READY,
        ) = AiReviewUiState(
            targetId,
            targetTitle,
            AiReviewContent.Review(draft, confirmReplacement = confirmReplacement),
            access,
        )

        fun failure(
            targetId: Long,
            targetTitle: String,
            message: String,
            access: AiReviewAccessState = AiReviewAccessState.READY,
        ) = AiReviewUiState(
            targetId,
            targetTitle,
            AiReviewContent.Failure(message),
            access,
        )

        fun applied(targetId: Long, targetTitle: String, syncState: RemoteSyllabusSyncState) = AiReviewUiState(
            targetId,
            targetTitle,
            AiReviewContent.Applied(syncState),
        )
    }
}

fun AiSyllabusDraft.totalTopicCount(): Int = subjects.sumOf { subject ->
    subject.topics.sumOf { it.totalTopicCount() }
}

private fun AiSyllabusDraftTopic.totalTopicCount(): Int = 1 + children.sumOf { it.totalTopicCount() }
