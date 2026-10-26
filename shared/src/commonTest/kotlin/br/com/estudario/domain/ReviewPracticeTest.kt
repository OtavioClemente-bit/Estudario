package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReviewPracticeTest {
    private val questions = listOf(
        ReviewPracticeQuestion(1, answered = 4, errors = 0),
        ReviewPracticeQuestion(2, answered = 0, errors = 0),
        ReviewPracticeQuestion(3, answered = 5, errors = 3),
        ReviewPracticeQuestion(4, answered = 1, errors = 1),
        ReviewPracticeQuestion(5, answered = 0, errors = 0),
        ReviewPracticeQuestion(6, answered = 2, errors = 0),
    )

    @Test fun defaultReviewContainsFiveQuestions() {
        assertEquals(5, ReviewPractice.select(questions, ReviewPracticeOptions(), 42).size)
    }

    @Test fun customCountUsesAvailableQuestionsWithoutDuplicates() {
        val selected = ReviewPractice.select(questions, ReviewPracticeOptions(questionCount = 20), 42)
        assertEquals(6, selected.size)
        assertEquals(6, selected.toSet().size)
    }

    @Test fun questionsCanBeDisabled() {
        assertTrue(ReviewPractice.select(questions, ReviewPracticeOptions(questionCount = 0), 42).isEmpty())
    }

    @Test fun errorsFirstPutsWeakQuestionsBeforeUnseenAndAnsweredOnes() {
        val selected = ReviewPractice.select(questions, ReviewPracticeOptions(questionCount = 6), 42)
        assertEquals(listOf(3L, 4L), selected.take(2))
        assertEquals(setOf(2L, 5L), selected.drop(2).take(2).toSet())
    }

    @Test fun unseenFirstHonorsTheChosenPriority() {
        val selected = ReviewPractice.select(questions, ReviewPracticeOptions(questionCount = 2, order = ReviewQuestionOrder.UNSEEN_FIRST), 42)
        assertEquals(setOf(2L, 5L), selected.toSet())
    }

    @Test fun optionsAreBoundedAndZeroRemainsAnIntentionalChoice() {
        val options = ReviewPracticeOptions(questionCount = -5, flashcardCount = 900).bounded()
        assertEquals(0, options.questionCount)
        assertEquals(30, options.flashcardCount)
    }

    @Test fun storedOptionsRoundTripAllStudyChoices() {
        val options = ReviewPracticeOptions(20, 12, ReviewQuestionOrder.MIXED, false, false, true, false)
        assertEquals(options, ReviewPracticeOptions.decode(options.encode()))
        assertEquals(ReviewPracticeOptions(), ReviewPracticeOptions.decode("broken settings"))
        assertEquals(ReviewPracticeOptions(questionCount = 0, flashcardCount = 30), ReviewPracticeOptions.decode("1|-8|900"))
    }
}
