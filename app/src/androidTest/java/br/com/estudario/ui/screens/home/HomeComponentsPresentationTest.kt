package br.com.estudario.ui.screens.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Rule
import org.junit.Test

class HomeComponentsPresentationTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun coverageShowsAllLongSubjectNames() {
        val subjects = listOf(
            SubjectCoverageUi("LÍNGUA PORTUGUESA (NÍVEL MÉDIO/SUPERIOR)", 8, 10, 0),
            SubjectCoverageUi("ANALISTA JUDICIÁRIO – ÁREA APOIO ESPECIALIZADO – TECNOLOGIA DA INFORMAÇÃO", 7, 10, 1),
            SubjectCoverageUi("Direito Administrativo", 6, 10, 2),
            SubjectCoverageUi("Banco de Dados", 5, 10, 3),
            SubjectCoverageUi("Redes de Computadores", 4, 10, 4),
        )
        compose.setContent {
            EstudarioTheme(false) {
                SyllabusCoverage(SyllabusCoverageUi(60, 30, 50, subjects, null), onOpenSyllabus = {})
            }
        }

        subjects.forEach { compose.onNodeWithText(it.name).assertExists() }
        compose.onNodeWithText("e mais").assertDoesNotExist()
    }

    @Test
    fun emptyPerformanceAndStreakExplainWhatToDoNext() {
        compose.setContent {
            EstudarioTheme(false) {
                PerformanceAndStanding(
                    performance = null,
                    standing = StandingUi(0, 0, 1, "Iniciante", 0, 0f, 0, 100),
                    onOpenStatistics = {},
                    onOpenProfile = {},
                )
            }
        }

        compose.onNodeWithText("Ainda sem histórico de questões.").assertIsDisplayed()
        compose.onNodeWithText("Comece hoje.").assertIsDisplayed()
        compose.onNodeWithText("—").assertDoesNotExist()
    }

    @Test
    fun levelExplainsXpInsideCurrentLevel() {
        compose.setContent {
            EstudarioTheme(false) {
                LevelRow(
                    StandingUi(12, 21, 8, "Estudante", 420, 0.35f, 70, 200),
                    onOpenProfile = {},
                )
            }
        }

        compose.onNodeWithText("Nível 8").assertIsDisplayed()
        compose.onNodeWithText("70 / 200 XP").assertIsDisplayed()
    }
}
