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
        // Uma passada só, letra por letra, para nenhuma ser trocada duas vezes.
        val out = StringBuilder(explanation.length)
        explanation.forEachIndexed { i, ch ->
            out.append(if (ch in 'A'..'E' && isLetterReference(explanation, i)) map[ch.toString()] ?: ch.toString() else ch.toString())
        }
        return out.toString()
    }

    // Verbos e palavras que, logo depois de "A", mostram que é a alternativa A e não o artigo ("A erra", "A e C").
    private val afterLetter = Regex(
        """^(\s*\)|\s*\(|\s*:|\s*[,;.]|\s+[–—-]\s|\s+(e|ou)\s+[A-E](?![A-Za-zÀ-ÿ\d])|\s+(é|são|está|estão|não|também|erra|erram|traz|trazem|troca|trocam|confunde|confundem|inverte|invertem|ignora|ignoram|usa|usam|atribui|atribuem|apresenta|descreve|combina|generaliza|soma|omite|omitem|considera|calcula|resulta|aplica|desloca|mantém|informa|expressa|enuncia|reúne|embaralha|inclui|incluem|decorre|nega|inventa|supõe|trata|limita|faz|leva|afirma|diz|contraria|contradiz|equipara|equiparam|exige|exigem|restringe|reduz|amplia|mistura|cita|indica|indicam|traduz|acerta|falha|falham|peca|pecam|extrapola|extrapolam|desconsidera|desconsideram|corresponde|correspondem|seria|seriam|fica|ficam|permanece|parte|partem|associa|associam|transforma|transformam|substitui|altera|alteram|retoma|recupera|presume|presumem|exagera|exageram|acrescenta|acrescentam|inverteu|errou)(?![A-Za-zÀ-ÿ\d]))""",
    )
    private val beforeLetter = Regex(
        """((alternativas?|letras?|opç(ão|ões)|assertivas?|gabarito|resposta)\s*:?\s*|[A-E]\s*(,|e|ou)\s*|[A-E],\s+[A-E]\s+e\s+)$""",
        RegexOption.IGNORE_CASE,
    )

    /**
     * A letra na posição [i] é uma alternativa citada? B a E sozinhas quase sempre são; "A" pode ser
     * o artigo, então só conta quando o contexto mostra a alternativa ("A erra", "A e C", "(A)", "Gabarito A").
     */
    private fun isLetterReference(text: String, i: Int): Boolean {
        val before = text.getOrNull(i - 1)
        val after = text.getOrNull(i + 1)
        if (before != null && (before.isLetterOrDigit() || before in "º°ª/-_'’")) return false
        if (after != null && (after.isLetterOrDigit() || after in "º°ª/'’")) return false
        // Letra seguida de ponto e outra letra/número é sigla ou abreviação ("E.U.A.", "D.O.U.").
        if (after == '.' && text.getOrNull(i + 2)?.isLetterOrDigit() == true) return false
        val head = text.substring(maxOf(0, i - 40), i)
        val tail = text.substring(i + 1, minOf(text.length, i + 40))
        if (beforeLetter.containsMatchIn(head)) return true
        return when (text[i]) {
            'A' -> afterLetter.containsMatchIn(tail)
            // "E" pode ser a conjunção no começo de frase ("E assim..."): só quando o contexto confirma.
            'E' -> afterLetter.containsMatchIn(tail) || !(tail.startsWith(" ") && tail.getOrNull(1)?.isLowerCase() == true)
            else -> true
        }
    }
}
