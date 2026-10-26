package br.com.estudario.domain

import kotlin.random.Random

enum class ReviewQuestionOrder { ERRORS_FIRST, UNSEEN_FIRST, MIXED }

data class ReviewPracticeOptions(
    val questionCount: Int = 5,
    val flashcardCount: Int = 8,
    val order: ReviewQuestionOrder = ReviewQuestionOrder.ERRORS_FIRST,
    val shuffleFlashcards: Boolean = true,
    val showSummary: Boolean = true,
    val offerHighlights: Boolean = true,
    val showHighlightNotes: Boolean = true,
) {
    fun bounded() = copy(questionCount = questionCount.coerceIn(0, 50), flashcardCount = flashcardCount.coerceIn(0, 30))
    fun encode(): String = bounded().let {
        listOf("1", it.questionCount, it.flashcardCount, it.order.name, it.shuffleFlashcards, it.showSummary, it.offerHighlights, it.showHighlightNotes).joinToString("|")
    }

    companion object {
        fun decode(value: String?): ReviewPracticeOptions {
            val parts = value?.split('|').orEmpty()
            val defaults = ReviewPracticeOptions()
            if (parts.firstOrNull() != "1") return defaults
            return ReviewPracticeOptions(
                questionCount = parts.getOrNull(1)?.toIntOrNull() ?: defaults.questionCount,
                flashcardCount = parts.getOrNull(2)?.toIntOrNull() ?: defaults.flashcardCount,
                order = ReviewQuestionOrder.entries.firstOrNull { it.name == parts.getOrNull(3) } ?: defaults.order,
                shuffleFlashcards = parts.getOrNull(4)?.toBooleanStrictOrNull() ?: defaults.shuffleFlashcards,
                showSummary = parts.getOrNull(5)?.toBooleanStrictOrNull() ?: defaults.showSummary,
                offerHighlights = parts.getOrNull(6)?.toBooleanStrictOrNull() ?: defaults.offerHighlights,
                showHighlightNotes = parts.getOrNull(7)?.toBooleanStrictOrNull() ?: defaults.showHighlightNotes,
            ).bounded()
        }
    }
}

data class ReviewPracticeQuestion(val id: Long, val answered: Int, val errors: Int)

object ReviewPractice {
    fun select(questions: List<ReviewPracticeQuestion>, options: ReviewPracticeOptions, seed: Long): List<Long> {
        val shuffled = questions.distinctBy { it.id }.shuffled(Random(seed))
        val ordered = when (options.order) {
            ReviewQuestionOrder.ERRORS_FIRST -> shuffled.sortedWith(compareBy({ if (it.errors > 0) 0 else if (it.answered == 0) 1 else 2 }, { -it.errors }))
            ReviewQuestionOrder.UNSEEN_FIRST -> shuffled.sortedWith(compareBy({ if (it.answered == 0) 0 else if (it.errors > 0) 1 else 2 }, { -it.errors }))
            ReviewQuestionOrder.MIXED -> shuffled
        }
        return ordered.take(options.bounded().questionCount).map { it.id }
    }
}
