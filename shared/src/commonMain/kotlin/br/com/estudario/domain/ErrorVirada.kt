package br.com.estudario.domain

/**
 * "A Virada": o caderno de erros com propósito. O erro não fica guardado, ele vira acerto subindo a
 * escada de reencontro (ErrorRetryLadder: 3, 10 e 30 dias). Só VIROU quem acertou os três degraus;
 * antes disso está virando e volta no dia certo (antes, depois do primeiro acerto ele saía da fila e
 * os degraus de 10 e 30 dias nunca eram pedidos). Igual no app e no site.
 *
 * O motivo do erro ("li rápido", "confundi conceitos"...) vai na primeira linha da nota do erro
 * ("Motivo: Li rápido"), que já sincroniza: o caderno mostra o padrão da pessoa sem banco novo.
 */
object ErrorVirada {
    data class Entry(
        val id: Long,
        val questionId: Long,
        val topicId: Long?,
        val errorCount: Int,
        val retryStreak: Int,
        val nextRetryAt: Long?,
        val lastReviewedAt: Long?,
        val lastErrorAt: Long,
        val comment: String,
    )

    enum class Stage { TABLE, TURNING, TURNED }

    enum class Reason(val label: String, val tip: String) {
        NAO_SABIA("Não sabia", "o conteúdo ainda não está firme: leia a teoria do tópico antes de refazer, e a questão vira mais rápido."),
        CONFUNDI("Confundi conceitos", "você mistura ideias parecidas: monte uma tabelinha \"isso x aquilo\" e grife a diferença na teoria."),
        LI_RAPIDO("Li rápido", "sublinhe o comando da questão (\"exceto\", \"incorreta\", \"não\") antes de olhar as alternativas."),
        PEGADINHA("Caí na pegadinha", "leia as pegadinhas do tópico no Caderno: a banca repete as mesmas armadilhas."),
    }

    private const val DAY = 86_400_000L
    private val steps = ErrorRetryLadder.days.size

    fun stage(e: Entry): Stage = when {
        e.retryStreak >= steps -> Stage.TURNED
        e.retryStreak >= 1 -> Stage.TURNING
        else -> Stage.TABLE
    }

    /** Degrau atual: 0 = acabou de errar (próximo é o de 3 dias) … [steps] = virou. */
    fun step(e: Entry): Int = e.retryStreak.coerceIn(0, steps)

    /**
     * Os que dá para virar hoje: os que estão na mesa (ainda sem acerto, dá para tentar já) e os da
     * escada cujo degrau venceu; do que venceu antes para o que venceu depois.
     */
    fun due(entries: List<Entry>, now: Long): List<Entry> =
        entries.filter { stage(it) == Stage.TABLE || (stage(it) == Stage.TURNING && (it.nextRetryAt ?: it.lastErrorAt) <= now) }
            .sortedWith(compareBy<Entry>({ it.nextRetryAt ?: it.lastErrorAt }, { -it.errorCount }))

    /** A missão do dia: até [max] dos que vencem hoje. */
    fun mission(entries: List<Entry>, now: Long, max: Int = 5): List<Entry> = due(entries, now).take(max)

    /** Degraus subidos nos últimos 7 dias (acertos em erros). */
    fun stepsThisWeek(entries: List<Entry>, now: Long): Int =
        entries.count { it.retryStreak > 0 && (it.lastReviewedAt ?: 0) >= now - 7 * DAY }

    // ------------------------------------------------------------------ o motivo do erro

    private val reasonLine = Regex("""^Motivo:\s*(.+)$""", RegexOption.IGNORE_CASE)

    fun reasonOf(comment: String): Reason? {
        val first = comment.lineSequence().firstOrNull()?.trim().orEmpty()
        val label = reasonLine.find(first)?.groupValues?.get(1)?.trim() ?: return null
        return Reason.entries.firstOrNull { it.label.equals(label, ignoreCase = true) }
    }

    /** A nota sem a linha do motivo (o que a pessoa escreveu). */
    fun noteOf(comment: String): String =
        if (reasonOf(comment) != null) comment.lineSequence().drop(1).joinToString("\n").trim() else comment.trim()

    /** Grava o motivo na primeira linha da nota, trocando o anterior. */
    fun withReason(comment: String, reason: Reason): String =
        listOf("Motivo: ${reason.label}", noteOf(comment)).filter { it.isNotBlank() }.joinToString("\n")

    /** Quantos erros por motivo, dos que têm motivo. */
    fun reasonStats(entries: List<Entry>): List<Pair<Reason, Int>> =
        entries.mapNotNull { reasonOf(it.comment) }.groupingBy { it }.eachCount()
            .entries.sortedByDescending { it.value }.map { it.key to it.value }
}
