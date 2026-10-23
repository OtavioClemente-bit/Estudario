package br.com.estudario.web.ui

/**
 * As cores do grifo, iguais às do app: amarelo para o que cai muito, verde para o que você já
 * entendeu e rosa para o que precisa revisar. O número fica no campo color do grifo.
 */
object MarkColors {
    data class Tone(val id: Int, val label: String, val css: String, val hex: String)
    val all = listOf(
        Tone(0, "Cai muito", "amarelo", "#F2B300"),
        Tone(1, "Entendi", "verde", "#22A06B"),
        Tone(2, "Revisar", "rosa", "#E5487A"),
    )
    fun of(id: Int): Tone = all.firstOrNull { it.id == id } ?: all[0]
}
