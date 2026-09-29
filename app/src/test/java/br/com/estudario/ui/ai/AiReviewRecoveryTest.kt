package br.com.estudario.ui.ai

import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.ai.AiTargetMatch
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

    @Test
    fun terminalTargetErrorsHaveSpecificSafeMessages() {
        assertEquals(
            "Não encontrei no PDF o conteúdo do edital para o cargo/área selecionado. Confira o nome do edital e tente novamente.",
            AiReviewRecovery.terminalFailureMessage("TARGET_NOT_FOUND"),
        )
        assertEquals(
            "Não consegui confirmar o cargo/área neste PDF. Use um nome de edital que indique o cargo e a especialidade e tente novamente.",
            AiReviewRecovery.terminalFailureMessage("TARGET_AMBIGUOUS"),
        )
        assertEquals(
            "A geração não foi concluída. Você pode tentar novamente.",
            AiReviewRecovery.terminalFailureMessage("PROVIDER_RESULT_UNAVAILABLE"),
        )
    }

    @Test
    fun v2TargetMatchSurvivesSavedDraftRoundTrip() {
        val draft = AiSyllabusDraft.fromProposal(
            7L,
            "TRT-3 Técnico Judiciário TI",
            AiSyllabusProposal(
                schemaVersion = 2,
                promptVersion = "syllabus-v2",
                modelVersion = "gpt-6-luna",
                documentTitle = "Edital TRT-3",
                subjects = listOf(AiSubjectProposal("Tecnologia da Informação", 0, AiPriority.NORMAL, emptyList(), listOf(2))),
                warnings = emptyList(),
                ambiguities = emptyList(),
                targetMatch = AiTargetMatch.MATCHED,
            ),
        )

        val restored = AiReviewDraftCodec.decode(AiReviewDraftCodec.encode(draft))

        assertEquals(AiTargetMatch.MATCHED, restored.proposal.targetMatch)
    }
}
