package br.com.estudario.ui.ai

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.runtime.mutableStateOf
import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.ai.AiWarning
import br.com.estudario.data.ai.AiWarningCode
import br.com.estudario.data.ai.AiWarningSeverity
import br.com.estudario.data.local.RemoteSyllabusSyncState
import br.com.estudario.domain.ai.AiSyllabusDraft
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AiReviewScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun unauthenticatedGateKeepsSelectedTargetAndReturnsToItAfterLogin() {
        var loginRequested = false
        compose.setContent {
            EstudarioTheme(false) {
                AiReviewScreen(
                    state = AiReviewUiState.gate(targetId = 42L, targetTitle = "TRT-3"),
                    onLogin = { loginRequested = true },
                )
            }
        }

        compose.onNodeWithText("Edital selecionado: TRT-3").assertIsDisplayed()
        compose.onNodeWithText("Use a IA do Estudário").assertIsDisplayed()
        compose.onNodeWithText("Entrar para continuar").performClick()
        compose.runOnIdle { assertTrue(loginRequested) }
    }

    @Test
    fun processingShowsPersistedJobAndIdempotencyRecoveryIdentity() {
        compose.setContent {
            EstudarioTheme(false) {
                AiReviewScreen(
                    state = AiReviewUiState.processing(
                        targetId = 42L,
                        targetTitle = "TRT-3",
                        jobId = "job-42",
                        idempotencyKey = "idem-42",
                    ),
                )
            }
        }

        compose.onNodeWithText("PROCESSING").assertIsDisplayed()
        compose.onNodeWithText("jobId: job-42").assertIsDisplayed()
        compose.onNodeWithText("Aguardando a análise do edital").assertIsDisplayed()
    }

    @Test
    fun readyGateOffersTheRealSourceSelectionAction() {
        var picked = false
        compose.setContent {
            EstudarioTheme(false) {
                AiReviewScreen(
                    state = AiReviewUiState.gate(42L, "TRT-3", AiReviewAccessState.READY),
                    onPickSource = { picked = true },
                )
            }
        }

        compose.onNodeWithText("Pronto para analisar o edital").assertIsDisplayed()
        compose.onNodeWithText("Selecionar PDF do edital").performClick()
        compose.runOnIdle { assertTrue(picked) }
    }

    @Test
    fun reviewRendersWarningsCountsAndAllowsAddEditRemove() {
        val current = mutableStateOf(AiReviewUiState.review(42L, "TRT-3", draft()))
        compose.setContent {
            EstudarioTheme(false) {
                AiReviewScreen(
                    state = current.value,
                    onDraftChange = { current.value = current.value.copy(content = AiReviewContent.Review(it)) },
                )
            }
        }

        compose.onNodeWithText("1 matéria · 4 tópicos").assertIsDisplayed()
        compose.onNodeWithText("As páginas 58-60 não puderam ser interpretadas.", substring = true).assertIsDisplayed()
        compose.onNodeWithText("Edital selecionado: TRT-3").assertIsDisplayed()

        compose.onNodeWithTag("ai_add_subject_button").performScrollTo().performClick()
        compose.waitForIdle()
        compose.onAllNodesWithText("Adicionar matéria", useUnmergedTree = true).assertCountEquals(2)
        compose.onNodeWithTag("ai_add_subject_name", useUnmergedTree = true).performTextInput("Raciocínio lógico")
        compose.onNodeWithText("Adicionar").performClick()
        compose.runOnIdle { assertEquals(2, current.value.draft().subjects.size) }

        compose.onNodeWithText("Direito Constitucional").performTextInput(" aplicado")
        compose.onNodeWithTag("ai_remove_subject_0").performClick()
        compose.runOnIdle { assertEquals(listOf("Raciocínio lógico"), current.value.draft().subjects.map { it.name }) }
    }

    @Test
    fun applyRequiresExplicitReplacementAndShowsPendingSync() {
        var replacementRequested = false
        val state = mutableStateOf(AiReviewUiState.review(42L, "TRT-3", draft(), confirmReplacement = true))
        compose.setContent {
            EstudarioTheme(false) {
                AiReviewScreen(
                    state = state.value,
                    onConfirmReplacement = { replacementRequested = true },
                )
            }
        }

        compose.onNodeWithText("Substituir conteúdo do edital?").assertIsDisplayed()
        compose.onNodeWithText("Substituir e usar este edital").performClick()
        compose.runOnIdle { assertTrue(replacementRequested) }

        state.value = AiReviewUiState.applied(42L, "TRT-3", RemoteSyllabusSyncState.PENDING)
        compose.onNodeWithText("Salvo neste dispositivo; sincronização pendente.").assertIsDisplayed()
    }

    @Test
    fun providerFailureOffersSafeLocalFallback() {
        var fallback = false
        compose.setContent {
            EstudarioTheme(false) {
                AiReviewScreen(
                    state = AiReviewUiState.failure(42L, "TRT-3", "Não foi possível processar este edital."),
                    onFallback = { fallback = true },
                )
            }
        }

        compose.onNodeWithText("Não foi possível processar este edital.").assertIsDisplayed()
        compose.onNodeWithText("Importar .estudo ou montar manualmente").performClick()
        compose.runOnIdle { assertTrue(fallback) }
    }

    @Test
    fun acknowledgedApplicationCallsIntegratedSetupTransitionCallback() {
        var applied = false
        compose.setContent {
            EstudarioTheme(false) {
                AiReviewScreen(
                    state = AiReviewUiState.applied(42L, "TRT-3", RemoteSyllabusSyncState.SYNCED),
                    onApplied = { applied = true },
                )
            }
        }

        compose.runOnIdle { assertTrue(applied) }
    }

    private fun AiReviewUiState.draft(): AiSyllabusDraft = (content as AiReviewContent.Review).draft

    private fun draft(): AiSyllabusDraft {
        val warning = AiWarning(
            code = AiWarningCode.UNREADABLE_PAGES,
            severity = AiWarningSeverity.WARNING,
            message = "As páginas 58-60 não puderam ser interpretadas.",
            sourcePages = listOf(58, 59, 60),
            ambiguity = null,
        )
        val proposal = AiSyllabusProposal(
            schemaVersion = 1,
            promptVersion = "syllabus-v1",
            modelVersion = "fixture-model",
            documentTitle = "Edital detectado",
            subjects = listOf(
                AiSubjectProposal(
                    name = "Direito Constitucional",
                    position = 0,
                    suggestedPriority = AiPriority.NORMAL,
                    topics = listOf(
                        AiTopicProposal("Direitos fundamentais", 0, listOf(AiTopicProposal("Princípios", 0, listOf(AiTopicProposal("Aplicação", 0, emptyList(), listOf(44))), listOf(43))), listOf(42)),
                        AiTopicProposal("Controle de constitucionalidade", 1, emptyList(), listOf(43)),
                    ),
                    sourcePages = listOf(42, 43),
                ),
            ),
            warnings = listOf(warning),
            ambiguities = emptyList(),
        )
        return AiSyllabusDraft.fromProposal(42L, "TRT-3", proposal)
    }
}
