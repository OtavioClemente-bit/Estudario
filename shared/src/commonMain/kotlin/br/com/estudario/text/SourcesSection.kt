package br.com.estudario.text

/**
 * Tira do texto de estudo a seção de fontes que a IA repete no fim ("### Fontes consultadas",
 * "**Fontes:**" etc.). As fontes já ficam guardadas à parte e aparecem na aba Fontes; no meio da
 * teoria elas só atrapalham a leitura.
 */
object SourcesSection {
    private val listItem = Regex("""^([-*+•]|\d+[.)])\s""")
    private val heading =Regex("""^(#{1,6})\s+(.*)$""")
    private const val NAME = """(fontes?|refer[êe]ncias)(\s+(consultadas|oficiais|prim[áa]rias|complementares|utilizadas|bibliogr[áa]ficas))?(\s*/\s*prim[áa]rias)?"""

    /** Título de seção que é só "Fontes", "Fontes consultadas", "Referências"... */
    private val sourcesTitle = Regex("""^[*_\s]*$NAME[*_\s:]*$""", RegexOption.IGNORE_CASE)

    /** Rótulo em negrito no começo da linha: "**Fontes:**", "**Fontes consultadas**". */
    private val sourcesLabel = Regex("""^[*_]{2}\s*$NAME\s*:?\s*[*_]{2}\s*:?""", RegexOption.IGNORE_CASE)

    fun strip(markdown: String): String {
        val lines = markdown.replace("\r\n", "\n").lines()
        val out = ArrayList<String>(lines.size)
        var skipLevel = 0 // > 0: dentro de uma seção de fontes aberta por título desse nível
        var skipLabel = false // dentro de um parágrafo/lista aberto por "**Fontes:**"
        for (line in lines) {
            val trimmed = line.trim()
            val match = heading.find(trimmed)
            if (match != null) {
                val level = match.groupValues[1].length
                val isSources = sourcesTitle.containsMatchIn(match.groupValues[2])
                skipLabel = false
                if (skipLevel > 0 && level > skipLevel) continue // subtítulo dentro das fontes
                if (isSources) { skipLevel = level; continue }
                skipLevel = 0
                out += line
                continue
            }
            if (skipLevel > 0) continue
            if (skipLabel) {
                // A lista de fontes logo abaixo do rótulo também sai; o primeiro parágrafo comum encerra.
                if (trimmed.isEmpty() || listItem.containsMatchIn(trimmed)) continue
                skipLabel = false
            }
            if (sourcesLabel.containsMatchIn(trimmed)) {
                skipLabel = true
                continue
            }
            out += line
        }
        return out.joinToString("\n").trimEnd()
    }
}
