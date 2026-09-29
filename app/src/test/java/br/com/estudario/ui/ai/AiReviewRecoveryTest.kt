package br.com.estudario.ui.ai

import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.domain.ai.AiSyllabusDraft
import org.junit.Assert.assertEquals
import org.junit.Test

class AiReviewRecoveryTest {
    @Test
    fun ordinaryTimeoutKeepsJobAndIdempotencyKeyInsteadOfCreatingAnotherRequest() {
        val original = AiReviewRequestIdentity(requestId = "request-1", jobId = "job-1", idempotencyKey = "idem-1")

        val recovered = AiReviewRecovery.afterTimeout(original)

        assertEquals(original, recovered)
    }

    @Test
    fun recursiveTopicCountIncludesEveryDescendant() {
        val draft = AiSyllabusDraft.fromProposal(
            42L,
            "TRT-3",
            AiSyllabusProposal(
                1,
                "syllabus-v1",
                "fixture-model",
                "Edital",
                listOf(
                    AiSubjectProposal(
                        "Direito",
                        0,
                        AiPriority.NORMAL,
                        listOf(
                            AiTopicProposal(
                                "Constituição",
                                0,
                                listOf(AiTopicProposal("Princípios", 0, listOf(AiTopicProposal("Aplicação", 0, emptyList(), listOf(3))), listOf(2))),
                                listOf(1),
                            ),
                        ),
                        listOf(1),
                    ),
                ),
                emptyList(),
                emptyList(),
            ),
        )

        assertEquals(3, draft.totalTopicCount())
    }
}
