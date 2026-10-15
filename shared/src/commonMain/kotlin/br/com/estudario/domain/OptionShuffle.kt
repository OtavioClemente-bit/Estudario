package br.com.estudario.domain

import kotlin.random.Random

/**
 * Alternativas em ordem nova a cada vez que a questão aparece, para a pessoa não decorar a letra.
 * A resposta continua registrada pela alternativa original; só a tela muda: a letra mostrada (A–E)
 * segue a posição nova, e a explicação ganha as letras novas onde cita alternativa por letra.
 *
 * Não embaralha quando a ordem faz parte da questão: Certo/Errado (duas opções) e alternativas que
 * citam outras ("todas as anteriores", "nenhuma das alternativas", "A e B estão corretas").
 */
object OptionShuffle {
    private val letters = listOf("A", "B", "C", "D", "E", "F")

    private val referencesOthers = Regex(
        """(todas|nenhuma)\s+(as\s+|das\s+)?(anteriores|alternativas|acima|demais)|\b(apenas|somente|só)\s+(a|as|o|os)?\s*[A-E]\b|\b[A-E]\s*(,|e|ou)\s*[A-E]\b|alternativas?\s+[A-E]\b""",
        RegexOption.IGNORE_CASE,
    )

    /** Pode embaralhar estas alternativas (pelos textos delas)? */
    fun canShuffle(texts: List<String>): Boolean =
        texts.size >= 3 && texts.none { referencesOthers.containsMatchIn(it) } &&
            // "Certo"/"Errado" em mais de duas opções também é formato fixo.
            texts.none { it.trim().lowercase() in setOf("certo", "errado", "verdadeiro", "falso") }

    /**
     * A ordem de exibição: índices das alternativas originais, já embaralhados (ou na ordem original,
     * quando não pode). [seed] muda a cada vez que a questão aparece.
     */
    fun order(texts: List<String>, seed: Long): List<Int> {
        val indices = texts.indices.toList()
        if (!canShuffle(texts)) return indices
        val random = Random(seed)
        var shuffled = indices.shuffled(random)
        // Sair exatamente na mesma ordem não ajuda: tenta de novo algumas vezes.
        var tries = 0
        while (shuffled == indices && tries++ < 4) shuffled = indices.shuffled(random)
        return shuffled
    }

    /** Letra mostrada para a alternativa que estava na posição [originalIndex]. */
    fun displayLetter(order: List<Int>, originalIndex: Int): String = letters.getOrElse(order.indexOf(originalIndex)) { "?" }

    /**
     * A explicação com as letras novas onde ela cita alternativa por letra ("alternativa C", "letra B",
     * "(D)", "E)" no começo de linha ou depois de ponto). [originalKeys] são as letras originais, na ordem.
     */
    fun relabel(explanation: String, originalKeys: List<String>, order: List<Int>): String {
        if (order == originalKeys.indices.toList()) return explanation
        val map = originalKeys.mapIndexed { i, key -> key.uppercase() to displayLetter(order, i) }.toMap()
        fun swap(letter: String) = map[letter.uppercase()] ?: letter
        return explanation
            .replace(Regex("""\b(alternativas?|letras?|opç(?:ão|ões)|assertivas?|gabarito)(\s+)([A-E])\b""", RegexOption.IGNORE_CASE)) { m ->
                m.groupValues[1] + m.groupValues[2] + swap(m.groupValues[3])
            }
            .replace(Regex("""\(([A-E])\)""")) { m -> "(" + swap(m.groupValues[1]) + ")" }
            .replace(Regex("""(^|\n|[.;:]\s+|\*\*)([A-E])\)""")) { m -> m.groupValues[1] + swap(m.groupValues[2]) + ")" }
    }
}
