package br.com.estudario.domain

import br.com.estudario.data.local.TopicStatus
import kotlin.math.roundToInt

data class MasteryInput(
    val status: TopicStatus,
    val answered: Int,
    val correct: Int,
    val recentErrors: Int,
    val completedReviews: Int,
    val recentAnswered: Int = 0,
    val recentCorrect: Int = 0,
    val recurringErrors: Int = 0,
    val overdueReviews: Int = 0,
    val daysSinceContact: Int = 0,
)

object MasteryCalculator {
    /**
     * Domínio V2 (0..100): base teórica/revisões 20, histórico 40, janela recente 25 e
     * retenção 15. Histórico exige 20 respostas e janela recente exige 10 para confiança
     * total; assim uma amostra de 1 a 2 acertos nunca produz domínio alto.
     */
    fun percent(input: MasteryInput): Int {
        val studied = when (input.status) {
            TopicStatus.NAO_ESTUDADO -> 0.0
            TopicStatus.EM_ESTUDO -> 0.05
            else -> 0.10
        }
        val reviews = (input.completedReviews / 3.0).coerceAtMost(1.0) * 0.10
        val lifetimeAccuracy = if (input.answered == 0) 0.0 else input.correct.toDouble() / input.answered
        val lifetimeConfidence = (input.answered / 20.0).coerceAtMost(1.0)
        val lifetime = lifetimeAccuracy * lifetimeConfidence * 0.40
        val recentAccuracy = if (input.recentAnswered == 0) lifetimeAccuracy else input.recentCorrect.toDouble() / input.recentAnswered
        val recentConfidence = (input.recentAnswered / 10.0).coerceAtMost(1.0)
        val recent = recentAccuracy * recentConfidence * 0.25
        val retentionBase = if (input.answered >= 10) 0.15 else 0.15 * (input.answered / 10.0)
        val penalties = (input.recurringErrors * 0.025).coerceAtMost(0.10) +
            (input.recentErrors * 0.01).coerceAtMost(0.05) +
            (input.overdueReviews * 0.025).coerceAtMost(0.075) +
            when {
                input.daysSinceContact > 90 -> 0.08
                input.daysSinceContact > 30 -> 0.04
                else -> 0.0
            }
        return ((studied + reviews + lifetime + recent + retentionBase - penalties).coerceIn(0.0, 1.0) * 100).roundToInt()
    }

    fun label(percent: Int): String = when {
        percent >= 80 -> "Forte"
        percent >= 50 -> "Médio"
        else -> "Fraco"
    }
}
