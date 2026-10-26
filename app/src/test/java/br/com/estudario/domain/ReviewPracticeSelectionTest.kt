package br.com.estudario.domain

import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionWithOptions
import org.junit.Assert.assertEquals
import org.junit.Test

class ReviewPracticeSelectionTest {
    @Test fun selectionUsesOnlyVisiblePracticeQuestionsFromTheReviewTopic() {
        fun question(id: Long, topic: Long = 10, hidden: Boolean = false, simulation: Long? = null) = QuestionWithOptions(
            QuestionEntity(id = id, topicId = topic, statement = "Fixture", explanation = "", isHidden = hidden, simulationId = simulation), emptyList(),
        )
        val available = listOf(question(1), question(2, topic = 20), question(3, hidden = true), question(4, simulation = 9), question(5))

        val selected = reviewPracticeQuestions(available, 10, ReviewPracticeOptions(questionCount = 50), 42)

        assertEquals(setOf(1L, 5L), selected.map { it.question.id }.toSet())
    }
}
