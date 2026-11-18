package br.com.estudario.ui.components

import androidx.compose.ui.graphics.Color

/**
 * As cores do grifo, iguais às do site: amarelo para o que cai muito, verde para o que você já
 * entendeu e rosa para o que precisa revisar. O número fica no campo color do grifo.
 */
object MarkColors {
    data class Tone(val id: Int, val label: String, val color: Color, val hint: String = "")
    // As três primeiras são as de sempre (grifos antigos continuam nelas); azul e lilás vieram depois.
    // Rosa (Revisar) é a que entra nas revisões: volta todo dia em "Grifos de hoje" até você lembrar.
    val all = listOf(
        Tone(0, "Cai muito", Color(0xFFF2B300), "O que mais cai na prova"),
        Tone(1, "Entendi", Color(0xFF22A06B), "Conceito que você já domina"),
        Tone(2, "Revisar", Color(0xFFE5487A), "Volta nas revisões até você lembrar"),
        Tone(3, "Lei e artigo", Color(0xFF2F7BEA), "Texto de lei, artigo, prazo"),
        Tone(4, "Pegadinha", Color(0xFF8B5CF6), "Onde a banca costuma pegar"),
    )
    fun of(id: Int): Tone = all.firstOrNull { it.id == id } ?: all[0]

    /** Os dias, contados do grifo, em que ele volta como cartão (revisão espaçada). */
    private val REVIEW_DAYS = setOf(1, 3, 7, 15, 30, 60, 90, 180)

    /** Grifos para revisar hoje: os marcados como "revisar" e os que fazem 1, 3, 7, 15, 30... dias. */
    fun dueToday(marks: List<br.com.estudario.data.local.TheoryMarkEntity>): List<br.com.estudario.data.local.TheoryMarkEntity> {
        val now = System.currentTimeMillis()
        return marks.filter { m -> m.color == 2 || ((now - m.createdAt) / 86_400_000L).toInt() in REVIEW_DAYS }
    }
}
