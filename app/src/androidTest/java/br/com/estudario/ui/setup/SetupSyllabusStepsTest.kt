package br.com.estudario.ui.setup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.content.Context
import android.graphics.Bitmap
import java.io.File
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SetupSyllabusStepsTest {
    @get:Rule val compose = createComposeRule()

    @Composable
    private fun CompactScreen(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale = 1.3f)) {
            EstudarioTheme(darkTheme = false) { Box(Modifier.size(width = 320.dp, height = 640.dp)) { content() } }
        }
    }

    private fun saveScreenshot(name: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
        File(context.getExternalFilesDir(null), "$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test
    fun reviewShowsAllSubjectsAndFullTopicsOnCompactScreen() {
        val subjects = (1L..10L).map { SubjectEntity(id = it, competitionId = 1, name = "Matéria $it com nome completo") }
        val topics = (1L..6L).map { TopicEntity(id = it, subjectId = 10, title = "Tópico $it com descrição extensa que deve permanecer legível e sem cortes") }
        compose.setContent {
            CompactScreen {
                SyllabusReviewStep(InitialSetupUiState(subjects = subjects, topicEntities = topics), {}, {}, { _, _ -> }, {}, {})
            }
        }
        compose.onNodeWithTag("setup_scrollbar").assertIsDisplayed()
        compose.onNodeWithTag("setup_subject_10").performScrollTo().performClick()
        compose.onNodeWithText(topics.first().title).performScrollTo()
        saveScreenshot("setup-review-compact")
        topics.forEach { topic -> compose.onNodeWithText(topic.title).performScrollTo().assertIsDisplayed() }
        compose.onNodeWithText("Adicionar tópico").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Está certo, continuar").assertIsEnabled()
    }

    @Test
    fun reviewAddsAndRemovesSubjectsAndTopicsAndUpdatesTotals() {
        val subject = SubjectEntity(id = 1, competitionId = 1, name = "Português")
        val state = mutableStateOf(InitialSetupUiState(subjects = listOf(subject)))
        compose.setContent {
            CompactScreen {
                SyllabusReviewStep(
                    state.value, {},
                    onAddSubject = { state.value = state.value.copy(subjects = state.value.subjects + SubjectEntity(id = 2, competitionId = 1, name = it)) },
                    onAddTopic = { id, title -> state.value = state.value.copy(topicEntities = listOf(TopicEntity(id = 1, subjectId = id, title = title))) },
                    onRemoveSubject = { subjectToRemove -> state.value = state.value.copy(subjects = state.value.subjects.filterNot { it.id == subjectToRemove.id }) },
                    onRemoveTopic = { state.value = state.value.copy(topicEntities = emptyList()) },
                )
            }
        }
        compose.onNodeWithText("Adicionar matéria").performScrollTo().performClick()
        compose.onNodeWithText("Nome da matéria").performTextInput("Matemática")
        compose.onNodeWithText("Adicionar").performClick()
        compose.onNodeWithText("2 matérias").assertExists()
        compose.onNodeWithTag("setup_subject_1").performScrollTo().performClick()
        compose.onNodeWithText("Adicionar tópico").performScrollTo().performClick()
        compose.onNodeWithText("Nome do tópico").performTextInput("Interpretação de textos")
        compose.onNodeWithText("Adicionar").performClick()
        compose.onNodeWithText("Interpretação de textos").performScrollTo().assertIsDisplayed()
        compose.onNodeWithContentDescription("Remover tópico Interpretação de textos").performScrollTo().performClick()
        compose.onNodeWithText("Cancelar").performClick()
        compose.onNodeWithText("Interpretação de textos").assertExists()
        compose.onNodeWithContentDescription("Remover tópico Interpretação de textos").performClick()
        compose.onNodeWithText("Remover").performClick()
        compose.onNodeWithText("Interpretação de textos").assertDoesNotExist()
        compose.onNodeWithText("Remover matéria").performScrollTo().performClick()
        compose.onNodeWithText("Remover").performClick()
        compose.runOnIdle {
            assertEquals(listOf("Matemática"), state.value.subjects.map { it.name })
            assertTrue(state.value.topicEntities.isEmpty())
        }
    }

    @Test
    fun subjectProfileCanContinueWithNeutralDefaultsAndScrollsToEverySubject() {
        val subjects = (1L..3L).map { SubjectEntity(id = it, competitionId = 1, name = "Matéria $it") }
        val snapshot = mutableStateOf(InitialSetupSnapshot())
        var continued = false
        compose.setContent {
            CompactScreen {
                SubjectProfileStep(
                    subjects = subjects,
                    snapshot = snapshot.value,
                    officialPriorities = emptyMap(),
                    onDifficulty = { id, difficulty ->
                        snapshot.value = snapshot.value.copy(subjectDifficulties = snapshot.value.subjectDifficulties + (id to difficulty))
                    },
                    onKnowledge = { id, knowledge ->
                        snapshot.value = snapshot.value.copy(subjectKnowledge = snapshot.value.subjectKnowledge + (id to knowledge))
                    },
                    onContinue = { continued = true },
                )
            }
        }
        compose.onNodeWithText("Continuar").assertIsEnabled()
        compose.onNodeWithTag("setup_scrollbar").assertIsDisplayed()
        compose.onNodeWithTag("difficulty_1_NORMAL").assertIsSelected()
        compose.onNodeWithTag("knowledge_1_NONE").assertIsSelected()
        compose.onNodeWithTag("difficulty_3_NORMAL").performScrollTo().assertIsSelected()
        saveScreenshot("setup-subject-profile-compact")
        repeat(3) { compose.onNodeWithTag("setup_scroll_content").performTouchInput { swipeUp() } }
        compose.onAllNodes(hasScrollAction() and hasAnyDescendant(hasTestTag("difficulty_3_HARD"))).onLast()
            .performScrollToNode(hasTestTag("difficulty_3_HARD"))
        compose.onNodeWithTag("difficulty_3_HARD").assertIsDisplayed().performClick()
        compose.waitForIdle()
        compose.runOnIdle { assertEquals(PersonalDifficulty.HARD, snapshot.value.subjectDifficulties["3"]) }
        compose.onNodeWithTag("difficulty_3_HARD").assertIsSelected()
        compose.onNodeWithText("Continuar").performClick()
        compose.runOnIdle { assertTrue(continued) }
    }

    @Test
    fun addingAnotherSubjectKeepsPreviousChoicesAndUsesNeutralDefaults() {
        val subjects = mutableStateOf(listOf(SubjectEntity(id = 1, competitionId = 1, name = "Português")))
        val snapshot = InitialSetupSnapshot(subjectDifficulties = mapOf("1" to PersonalDifficulty.EASY, "removed" to PersonalDifficulty.VERY_HARD))
        compose.setContent {
            CompactScreen {
                SubjectProfileStep(subjects.value, snapshot, emptyMap(), { _, _ -> }, { _, _ -> }, {})
            }
        }
        compose.onNodeWithText("Continuar").assertIsEnabled()
        compose.runOnIdle { subjects.value += SubjectEntity(id = 2, competitionId = 1, name = "Matemática") }
        compose.onNodeWithText("Continuar").assertIsEnabled()
        compose.onNodeWithText("1 matéria(s) com resposta sua.").assertIsDisplayed()
        compose.onNodeWithTag("difficulty_1_EASY").assertIsSelected()
        compose.onNodeWithTag("difficulty_2_NORMAL").assertIsSelected()
    }

    @Test
    fun emptyReviewCannotContinue() {
        compose.setContent { CompactScreen { SyllabusReviewStep(InitialSetupUiState(), {}, {}, { _, _ -> }, {}, {}) } }
        compose.onNodeWithText("Está certo, continuar").assertIsNotEnabled()
        compose.onNodeWithText("Adicionar matéria").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun integratedAiAttachmentPickerRequestsOnlyPdf() {
        var requestedTypes: Array<String>? = null
        val launcher = object : ActivityResultLauncher<Array<String>>() {
            override fun launch(input: Array<String>, options: androidx.core.app.ActivityOptionsCompat?) { requestedTypes = input }
            override val contract: androidx.activity.result.contract.ActivityResultContract<Array<String>, *> get() =
                ActivityResultContracts.OpenDocument()
            override fun unregister() = Unit
        }
        val app = ApplicationProvider.getApplicationContext<br.com.estudario.EstudarioApplication>()
        val viewModel = InitialSetupViewModel(app)

        compose.setContent {
            CompactScreen {
                SyllabusMethodStep(
                    snapshot = InitialSetupSnapshot(syllabusMethod = br.com.estudario.domain.setup.SyllabusMethod.DIRECT_AI),
                    operation = SetupOperation.Idle,
                    viewModel = viewModel,
                    picker = launcher,
                    editalAttachment = null,
                    onPickEditalAttachment = { launcher.launch(InitialSetupAiPdfSource.PICKER_MIME_TYPES) },
                    onClearEditalAttachment = {},
                    onOpenIntegratedAi = {},
                )
            }
        }

        compose.onNodeWithText("Anexar edital (PDF)").performScrollTo().performClick()
        val uri = Uri.parse("content://fixture/edital.pdf")
        var persisted: Uri? = null
        val selected = InitialSetupAiPdfSource.persist(uri, AiPdfUriPermission { persisted = it })
        compose.runOnIdle {
            assertEquals(listOf("application/pdf"), requestedTypes?.toList())
            assertEquals(uri, persisted)
            assertEquals(uri, selected)
        }
    }
}
