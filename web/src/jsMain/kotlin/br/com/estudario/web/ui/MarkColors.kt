package br.com.estudario.web.ui

/**
 * As cores do grifo, iguais às do app: amarelo para o que cai muito, verde para o que você já
 * entendeu e rosa para o que precisa revisar. O número fica no campo color do grifo.
 */
object MarkColors {
    data class Tone(val id: Int, val label: String, val css: String, val hex: String, val hint: String = "")
    // As três primeiras são as de sempre (grifos antigos continuam nelas); azul e lilás vieram depois.
    // Rosa (Revisar) é a que entra nas revisões: volta todo dia em "Grifos de hoje" até você lembrar.
    val all = listOf(
        Tone(0, "Cai muito", "amarelo", "#F2B300", "O que mais cai na prova"),
        Tone(1, "Entendi", "verde", "#22A06B", "Conceito que você já domina"),
        Tone(2, "Revisar", "rosa", "#E5487A", "Volta nas revisões até você lembrar"),
        Tone(3, "Lei e artigo", "azul", "#2F7BEA", "Texto de lei, artigo, prazo"),
        Tone(4, "Pegadinha", "lilas", "#8B5CF6", "Onde a banca costuma pegar"),
    )
    fun of(id: Int): Tone = all.firstOrNull { it.id == id } ?: all[0]
}
