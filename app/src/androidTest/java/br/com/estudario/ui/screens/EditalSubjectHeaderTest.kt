package br.com.estudario.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditalSubjectHeaderTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun clickingSubjectNameExpandsTheSubject() {
        var expanded = false
        val subject = SubjectEntity(id = 1L, competitionId = 1L, name = "Direito Constitucional")
        val viewModel = AppViewModel(ApplicationProvider.getApplicationContext<EstudarioApplication>())

        compose.setContent {
            EstudarioTheme(darkTheme = false) {
                SubjectCard(
                    subject = subject,
                    topics = emptyList(),
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    viewModel = viewModel,
                    onTopic = {},
                    onAddTopic = {},
                    onGenerateContent = {},
                    onPriority = {},
                    onTopicPriority = { _, _ -> },
                    parentPriority = PriorityLevel.MEDIUM,
                )
            }
        }

        compose.onNodeWithText("Direito Constitucional").performClick()
        compose.runOnIdle { assertTrue(expanded) }
    }
}
