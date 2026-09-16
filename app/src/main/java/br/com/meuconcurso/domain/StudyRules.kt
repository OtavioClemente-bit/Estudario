package br.com.meuconcurso.domain

object ReviewIntervals {
    val days: List<Long> = listOf(1, 7, 30)
}

object StudyQueueRules {
    /** Concluir um bloco o envia ao fim; passagem de tempo não chama esta regra. */
    fun completedPosition(currentMaximum: Int): Int = currentMaximum + 1
    /** Faltar ou adiar registra o evento, mas mantém o bloco exatamente na mesma posição. */
    fun missedPosition(currentPosition: Int): Int = currentPosition
}
