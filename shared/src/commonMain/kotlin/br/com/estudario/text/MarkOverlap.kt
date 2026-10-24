package br.com.estudario.text

/**
 * Grifo refeito: quando a pessoa grifa de novo um trecho que já tinha grifo (porque faltou uma letra, ou
 * quer o trecho maior ou menor), o novo substitui os que encostam nele no mesmo parágrafo, em vez de
 * ficar um grifo velho do lado. O site e o app usam a mesma regra.
 */
object MarkOverlap {
    /**
     * Texto para comparar: sem marcação de Markdown, espaços juntos, minúsculo. Todo colchete escapado:
     * no site (Kotlin/JS) a expressão roda no modo Unicode, que recusa "]" solto e derrubava o grifo.
     */
    fun plain(s: String): String = s
        .replace(Regex("""!?\[([^\]]*)\]\([^)]*\)"""), "$1")
        .replace(Regex("""[*_`#>|~]"""), "")
        .replace(Regex("""\s+"""), " ")
        .trim()
        .lowercase()

    /** Os trechos [a] e [b] do parágrafo [block] se encostam (um dentro do outro ou um pedaço em comum). */
    fun overlaps(block: String, a: String, b: String): Boolean {
        val pa = plain(a); val pb = plain(b)
        if (pa.isEmpty() || pb.isEmpty()) return false
        if (pa.contains(pb) || pb.contains(pa)) return true
        val text = plain(block)
        val ia = text.indexOf(pa); val ib = text.indexOf(pb)
        if (ia < 0 || ib < 0) return false
        return ia < ib + pb.length && ib < ia + pa.length
    }
}
