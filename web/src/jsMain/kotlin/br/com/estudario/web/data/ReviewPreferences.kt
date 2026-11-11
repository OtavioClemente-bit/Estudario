package br.com.estudario.web.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.domain.ReviewPracticeOptions
import br.com.estudario.domain.ReviewPractice
import br.com.estudario.domain.ReviewPracticeQuestion
import kotlinx.browser.window

object ReviewPreferences {
    private const val KEY = "estudario.review.practice.v1"
    var value by mutableStateOf(ReviewPracticeOptions.decode(runCatching { window.localStorage.getItem(KEY) }.getOrNull()))
        private set

    fun save(options: ReviewPracticeOptions) {
        val next = options.bounded()
        window.localStorage.setItem(KEY, next.encode())
        value = next
    }
}

/**
 * Os cartões que a pessoa não lembrou na última revisão de cada tópico: eles voltam primeiro na próxima.
 * Fica só neste navegador (é um atalho de estudo, não um dado da conta).
 */
object ReviewHardCards {
    private const val KEY = "estudario.review.hard.v1."
    /** O que já passou nas revisões do tópico (os nunca vistos vêm antes) e o último aquecimento (para não repetir). */
    const val SEEN = "estudario.review.seen.v1."
    const val WARM = "estudario.review.warm.v1."

    fun get(topicId: Long, prefix: String = KEY): Set<String> =
        runCatching { window.localStorage.getItem(prefix + topicId) }.getOrNull()?.split(',')?.filter { it.isNotBlank() }?.toSet().orEmpty()

    fun save(topicId: Long, keys: Set<String>, prefix: String = KEY) {
        runCatching { if (keys.isEmpty()) window.localStorage.removeItem(prefix + topicId) else window.localStorage.setItem(prefix + topicId, keys.joinToString(",")) }
    }
}

fun reviewPracticeQuestions(data: Snapshot, topicId: Long, options: ReviewPracticeOptions, seed: Long): List<Question> {
    val available = data.questions.filter { it.topicId == topicId && !it.hidden && it.simulationId == null }
    val byId = available.associateBy { it.id }
    return ReviewPractice.select(available.map { ReviewPracticeQuestion(it.id, it.answerCount, it.errorCount) }, options, seed).mapNotNull(byId::get)
}
