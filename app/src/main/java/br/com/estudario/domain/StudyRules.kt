package br.com.estudario.domain

import br.com.estudario.data.local.ReviewDifficulty

object ReviewIntervals {
    /** Os três primeiros estágios contam a partir do dia em que o tópico foi estudado. */
    val days: List<Long> = listOf(1, 7, 30)

    /** Teto do intervalo perpétuo: passar disso é praticamente esquecer o tópico de novo. */
    const val MAX_DAYS: Long = 240L

    /**
     * Próximo intervalo depois que a agenda fixa acabou: dobra o último intervalo usado, nunca
     * abaixo do maior estágio fixo e nunca acima do teto. É isso que faz a revisão não morrer —
     * concluir a última revisão sempre agenda a próxima. Funciona também com o ciclo intensivo,
     * porque parte do intervalo real do tópico, não de uma tabela fixa.
     */
    fun nextIntervalDays(previousIntervalDays: Long): Long =
        (previousIntervalDays.coerceIn(days.last(), MAX_DAYS) * 2).coerceAtMost(MAX_DAYS)

    /**
     * Ajusta esse intervalo pelo que a pessoa sentiu na revisão e por quanto acertou nas questões.
     * Difícil aproxima, fácil afasta — o mesmo motor que já ajustava os estágios fixos.
     */
    fun adjustedIntervalDays(previousIntervalDays: Long, difficulty: ReviewDifficulty, correct: Int = 0, total: Int = 0): Long {
        val base = nextIntervalDays(previousIntervalDays)
        val factor = when (difficulty) {
            ReviewDifficulty.DIFICIL -> 0.5
            ReviewDifficulty.NORMAL -> 1.0
            ReviewDifficulty.FACIL -> 1.35
        }
        // Errar mais da metade das questões vale como "difícil", mesmo que a pessoa tenha marcado normal.
        val performance = if (total > 0 && correct * 2 < total) 0.5 else 1.0
        return (base * factor * performance).toLong().coerceIn(1L, MAX_DAYS)
    }
}

/**
 * Escada de reencontro com a questão errada. Errou hoje, ela volta em 3 dias; acertou, 10; acertou
 * de novo, 30; acertou a terceira vez, sai da escada (a questão está dominada). Errar em qualquer
 * ponto zera e volta para 3 dias.
 */
object ErrorRetryLadder {
    val days: List<Long> = listOf(3, 10, 30)

    /** Dias até a questão voltar, ou null quando ela já passou pela escada inteira. */
    fun intervalDays(streak: Int): Long? = days.getOrNull(streak.coerceAtLeast(0))

    /** Sequência de acertos depois do erro: acertar avança, errar volta para o começo. */
    fun nextStreak(previousStreak: Int, correct: Boolean): Int = if (correct) (previousStreak + 1).coerceAtMost(days.size) else 0

    /** Momento em que a questão deve voltar, ou null se ela saiu da escada. */
    fun nextRetryAt(streak: Int, now: Long): Long? = intervalDays(streak)?.let { now + it * 86_400_000L }
}

object StudyQueueRules {
    /** Concluir um bloco o envia ao fim; passagem de tempo não chama esta regra. */
    fun completedPosition(currentMaximum: Int): Int = currentMaximum + 1
    /** Faltar ou adiar registra o evento, mas mantém o bloco exatamente na mesma posição. */
    fun missedPosition(currentPosition: Int): Int = currentPosition
}
