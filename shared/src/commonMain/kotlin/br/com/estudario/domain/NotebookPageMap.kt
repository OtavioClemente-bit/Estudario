package br.com.estudario.domain

/**
 * A página do Caderno de estudo montada para estudar SEM abrir a teoria inteira. Três alunos testaram
 * a página antiga (Direito Constitucional, Contabilidade e Português) e acharam o mesmo: o resumo virava
 * uma lista solta cortada em 6 pontos (o que mais cai ficava escondido), os títulos e as tabelas da
 * matéria sumiam, só 3 pegadinhas apareciam e os cartões repetiam as dicas.
 *
 * Agora a página é o mapa do tópico:
 *  - [blocks]: o resumo dividido pelas partes do assunto (os títulos do resumo, ou os capítulos da teoria);
 *  - [exam]: os itens "Como cai na prova" que a teoria já traz no fim de cada capítulo;
 *  - [tables]: os quadros da teoria (comparações, estruturas), pequenos o bastante para a página;
 *  - [cards]: cartões sem repetir dica nem pegadinha, alternando flashcard e pergunta de memória.
 */
object NotebookPageMap {
    data class Block(val title: String?, val lines: List<String>)
    data class Table(val title: String?, val markdown: String)
    data class Map(val blocks: List<Block>, val exam: List<String>, val tables: List<Table>, val tips: List<String>, val traps: List<String>, val cards: List<Pair<String, String>>) {
        val pointCount get() = blocks.sumOf { it.lines.size }
    }

    private val HEADING = Regex("""^#{1,4}\s+(.+)$""")
    private val EXAM = Regex("""como\s+cai|na\s+prova|cai\s+em\s+prova|o\s+que\s+mais\s+cai""", RegexOption.IGNORE_CASE)
    private val SKIP = Regex("""o\s+problema\s+que|introdu|vocabul|gloss|fontes|refer[eê]ncias""", RegexOption.IGNORE_CASE)

    fun build(
        summary: String?,
        theory: List<String>,
        tips: List<String>,
        traps: List<String>,
        recall: List<Pair<String, String>>,
    ): Map {
        val blocks = blocks(summary, theory)
        val exam = exam(theory)
        val tables = tables(theory)
        // Dicas e pegadinhas: todas as boas (antes só 3 pegadinhas); sem repetir o que o "como cai" já disse.
        val shown = (exam + blocks.flatMap { it.lines }).map(::mix)
        fun fresh(text: String) = shown.none { ReviewMix.similar(it, mix(text)) }
        val goodTips = tips.map { it.trim() }.filter { it.length in 15..420 }.distinct().filter(::fresh).take(6)
        val goodTraps = traps.map { it.trim() }.filter { it.length in 15..420 }.distinct().take(8)
        // Cartões: sem repetir dica, pegadinha ou outro cartão; uma ideia por cartão.
        val used = (goodTips + goodTraps).map(::mix).toMutableList()
        val cards = mutableListOf<Pair<String, String>>()
        val deck = ReviewMix.cards(recall.map { MixSource(it.first, it.second) }, emptyList(), emptyList(), 999, 1, 0, shuffle = false)
        for (c in deck) {
            if (cards.size >= 12) break
            if (used.any { ReviewMix.similar(it, c) }) continue
            cards += c.front to c.back
            used += c
        }
        return Map(blocks, exam, tables, goodTips, goodTraps, cards)
    }

    private fun mix(text: String) = MixCard(MixKind.CARD, text, text, text)

    /** O resumo dividido pelas partes do assunto: cada título do resumo abre um bloco. */
    fun blocks(summary: String?, theory: List<String>): List<Block> {
        if (summary.isNullOrBlank()) return emptyList()
        val parts = mutableListOf<Pair<String?, StringBuilder>>(null to StringBuilder())
        summary.lines().forEach { raw ->
            val line = raw.trim()
            val heading = HEADING.find(line)?.groupValues?.get(1)?.trim()?.trim('*')
            if (heading != null) parts += heading to StringBuilder() else parts.last().second.append(line).append('\n')
        }
        val built = parts.mapIndexedNotNull { i, (title, text) ->
            val lines = StudyNotebook.summaryLines(clean(text.toString()), max = 10)
            // o título de abertura ("Balanço e resultado: revisão essencial") repete o nome do tópico
            if (lines.isEmpty()) null else Block(if (i == 1 && parts[0].second.isBlank() && parts.size > 2) null else title, lines)
        }
        // Resumo de um parágrafo só (sem títulos): um bloco único, sem corte em 6.
        return built
    }

    /** Tira o que não é texto de ler: fórmula com $$, negrito colado no próximo item. */
    private fun clean(text: String): String = text
        .replace(Regex("""\$\$\s*(.+?)\s*\$\$"""), "**$1**")
        .replace(Regex("""\$(.+?)\$"""), "$1")
        // "pertinentes.** **Lucro bruto": o ponto ficava dentro do negrito e a frase não quebrava;
        // o negrito fecha antes do ponto e a quebra de frase volta a funcionar
        .replace(Regex("""([.;!?])\*\*(\s+)"""), "**$1$2")

    /** Os itens "Como cai na prova" de cada capítulo da teoria (lista ou frases), sem repetir. */
    fun exam(theory: List<String>, max: Int = 10): List<String> {
        val items = mutableListOf<String>()
        theory.forEach { md ->
            var inside = false
            val paragraph = StringBuilder()
            fun flush() { if (paragraph.isNotBlank()) items += StudyNotebook.summaryLines(paragraph.toString(), max = 6); paragraph.clear() }
            md.lines().forEach { raw ->
                val line = raw.trim()
                val heading = HEADING.find(line)?.groupValues?.get(1)
                if (heading != null) { if (inside) flush(); inside = EXAM.containsMatchIn(heading); return@forEach }
                if (!inside || line.isEmpty() || line.startsWith("|") || line.startsWith(">")) { if (inside && line.isEmpty()) flush(); return@forEach }
                val item = line.removePrefix("- ").removePrefix("* ").removePrefix("• ").trim()
                if (line.startsWith("- ") || line.startsWith("* ") || line.startsWith("• ") || Regex("""^\d+[.)]\s""").containsMatchIn(line)) {
                    flush(); items += item.replace(Regex("""^\d+[.)]\s+"""), "")
                } else paragraph.append(item).append(' ')
            }
            if (inside) flush()
        }
        val kept = mutableListOf<String>()
        for (item in items.map { it.trim() }.filter { it.length in 20..360 }) {
            if (kept.none { ReviewMix.similar(mix(it), mix(item)) }) kept += item
            if (kept.size >= max) break
        }
        return kept
    }

    /**
     * Os quadros da teoria que cabem na página: até 5 linhas de dados... 10, até 4 colunas. Prefere os que
     * comparam ou estruturam (título com "×", "x", "comparação", "estrutura", "diferença", "resumo").
     */
    fun tables(theory: List<String>, max: Int = 3): List<Table> {
        val found = mutableListOf<Pair<Table, Int>>()
        theory.forEach { md ->
            var title: String? = null
            val rows = mutableListOf<String>()
            fun close() {
                if (rows.size >= 3) {
                    val cols = rows.first().trim().trim('|').split('|').size
                    val dataRows = rows.size - 2
                    if (cols in 2..4 && dataRows in 2..10 && rows.all { it.length <= 260 }) {
                        val score = if (title != null && Regex("""×| x |compar|estrutura|diferen|resumo|quadro|esquema|regra""", RegexOption.IGNORE_CASE).containsMatchIn(title!!)) 0 else 1
                        found += Table(title?.takeUnless { SKIP.containsMatchIn(it) }, rows.joinToString("\n")) to score
                    }
                }
                rows.clear()
            }
            md.lines().forEach { raw ->
                val line = raw.trim()
                val heading = HEADING.find(line)?.groupValues?.get(1)?.trim()
                when {
                    line.startsWith("|") -> rows += line
                    else -> { close(); if (heading != null) title = heading.trim('*') }
                }
            }
            close()
        }
        return found.withIndex().sortedWith(compareBy({ it.value.second }, { it.index })).map { it.value.first }.distinctBy { it.markdown }.take(max)
    }
}
