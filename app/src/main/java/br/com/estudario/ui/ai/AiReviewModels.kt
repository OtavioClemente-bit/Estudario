package br.com.estudario.ui.ai

import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.domain.ai.AiSyllabusDraft

data class AiReviewTarget(val id: Long, val title: String)

data class AiReviewRequestIdentity(
    val requestId: String,
    val jobId: String,
    val idempotencyKey: String,
)

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
) {
    companion object {
        fun gate(targetId: Long, targetTitle: String) = AiReviewUiState(targetId, targetTitle, AiReviewContent.Gate)

        fun processing(targetId: Long, targetTitle: String, jobId: String, idempotencyKey: String) = AiReviewUiState(
            targetId,
            targetTitle,
            AiReviewContent.Processing(jobId, idempotencyKey),
        )

        fun review(targetId: Long, targetTitle: String, draft: AiSyllabusDraft, confirmReplacement: Boolean = false) = AiReviewUiState(
            targetId,
            targetTitle,
            AiReviewContent.Review(draft, confirmReplacement = confirmReplacement),
        )

        fun failure(targetId: Long, targetTitle: String, message: String) = AiReviewUiState(
            targetId,
            targetTitle,
            AiReviewContent.Failure(message),
        )

        fun applied(targetId: Long, targetTitle: String, syncState: RemoteSyllabusSyncState) = AiReviewUiState(
            targetId,
            targetTitle,
            AiReviewContent.Applied(syncState),
        )
    }
}
