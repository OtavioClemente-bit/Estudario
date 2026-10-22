package br.com.estudario.ui.assistant

import br.com.estudario.domain.FolhaFalas

/**
 * O que o Folha fala: o banco é o mesmo do site (FolhaFalas, no módulo compartilhado). A escolha usa
 * uma semente estável (o id da questão, o dia, o número da sequência), então a fala não troca a cada
 * recomposição e também não é sempre a mesma.
 */
object FolhaLines {
    fun forAnswer(correct: Boolean, seed: Long): String = FolhaFalas.answer(correct, seed)

    /** Acertou logo depois de errar: o Folha solta o ar ("ufa"). */
    fun forRelief(seed: Long): String = FolhaFalas.relief(seed)

    /** Muitos acertos seguidos: o Folha pega fogo junto. */
    fun forFire(seed: Long): String = FolhaFalas.fire(seed)

    /** Fim da bateria: a fala depende de como foi. */
    fun forResult(percent: Int, seed: Long): String = FolhaFalas.result(percent, seed)

    /** Meta do dia batida: a sequência continua. */
    fun forStreak(days: Int, seed: Long): String = FolhaFalas.streak(days, seed)

    fun forBadge(seed: Long): String = FolhaFalas.badge(seed)

    /**
     * Frase do Folha no cartão "Agora". [seed] muda a cada vez que o Início abre; às vezes ele comenta o
     * horário, o dia da semana ou a prova chegando.
     */
    fun forHome(continuing: Boolean, needsMaterial: Boolean, hour: Int, seed: Long, daysToExam: Long? = null): String =
        FolhaFalas.home(continuing, needsMaterial, hour, seed, java.time.LocalDate.now().dayOfWeek.value, daysToExam)
}
