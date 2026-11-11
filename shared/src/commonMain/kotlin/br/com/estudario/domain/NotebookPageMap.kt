package br.com.estudario.domain

/**
 * A página do Caderno de estudo montada para estudar SEM abrir a teoria inteira.
 *
 * Testada por 13 "alunos" em matérias bem diferentes (Constitucional, Contabilidade, Português,
 * Matemática, Estatística, Penal, Administrativo, Inglês, AFO, Arquivologia, Enfermagem, Física).
 * O que eles pediram e esta montagem faz:
 *  - [blocks]: o resumo dividido pelas partes do assunto. Resumo com títulos usa os títulos; resumo
 *    corrido é distribuído pelos capítulos da teoria (cada frase vai para o capítulo que mais fala dela);
 *  - [formulas]: as fórmulas da teoria e do resumo, legíveis (antes o código LaTeX aparecia cru);
 *  - [exam]: o "Como cai na prova" de cada capítulo, agrupado por capítulo, sem repetir o mapa;
 *  - [tables]: um quadro por capítulo, na ordem da teoria, com título que diz o que é;
 *  - [tips]/[traps]/[cards]: dicas, todas as pegadinhas e cartões, sem repetir o que já está acima.
 * Frases cortadas no ";" (item começando com minúscula) voltam a ser uma só.
 */
object NotebookPageMap {
    data class Block(val title: String?, val lines: List<String>)
    data class Table(val title: String?, val markdown: String)
    data class Map(
        val blocks: List<Block>,
        val exam: List<Block>,
        val tables: List<Table>,
        val tips: List<String>,
        val traps: List<String>,
        val cards: List<Pair<String, String>>,
        val formulas: List<String> = emptyList(),
    ) {
        val pointCount get() = blocks.sumOf { it.lines.size }
        val examCount get() = exam.sumOf { it.lines.size }
    }

    private class Chapter(val title: String?, val body: String)

    private val HEADING = Regex("""^#{1,4}\s+(.+)$""")
    private val EXAM = Regex("""como\s+cai|na\s+prova|cai\s+em\s+prova|o\s+que\s+mais\s+cai""", RegexOption.IGNORE_CASE)
    private val GENERIC = Regex("""^(resumo|s[ií]ntese|quadro|tabela|exemplos?|revis[aã]o.*|como cai.*|o problema que.*|vocabul.*|gloss.*|.*revis[aã]o (essencial|organizada|r[aá]pida).*|.*vis[aã]o geral.*)$""", RegexOption.IGNORE_CASE)
    private val DISPLAY_MATH = Regex("""\$\$([\s\S]+?)\$\$""")

    fun build(
        summary: String?,
        theory: List<String>,
        tips: List<String>,
        traps: List<String>,
        recall: List<Pair<String, String>>,
    ): Map {
        val chapters = chapters(theory)
        val formulas = formulas(summary, chapters)
        val blocks = blocks(summary, chapters)
        val shownLines = blocks.flatMap { it.lines }.map(::mix).toMutableList()
        val exam = exam(chapters, shownLines)
        shownLines += exam.flatMap { it.lines }.map(::mix)
        val tables = tables(chapters)
        val tableText = tables.map { mix(it.markdown.replace('|', ' ')) }
        // Dicas: as que trazem algo novo. Pegadinhas: todas as boas (antes só 3).
        val goodTips = tips.map { Formula.inline(it.trim()) }.filter { it.length in 15..420 }.distinct()
            .filter { tip -> shownLines.none { ReviewMix.similar(it, mix(tip)) } }.take(6)
        val goodTraps = traps.map { Formula.inline(it.trim()) }.filter { it.length in 15..420 }.distinct().take(8)
        // Cartões: nada que a página já disse (mapa, como cai, quadros, dicas, pegadinhas) nem outro cartão.
        val used = (shownLines + tableText + (goodTips + goodTraps).map(::mix)).toMutableList()
        val cards = mutableListOf<Pair<String, String>>()
        val deck = ReviewMix.cards(recall.filter { it.first.isNotBlank() && it.second.isNotBlank() && it.first != "undefined" }
            .map { MixSource(Formula.inline(it.first), Formula.inline(it.second)) }, emptyList(), emptyList(), 999, 1, 0, shuffle = false)
        for (c in deck) {
            if (cards.size >= 12) break
            if (used.any { ReviewMix.similar(it, c) }) continue
            cards += c.front to c.back
            used += c
        }
        return Map(blocks, exam, tables, goodTips, goodTraps, cards, formulas)
    }

    private fun mix(text: String) = MixCard(MixKind.CARD, text, text, text)

    /** A teoria em capítulos: cada "## Título" abre um (é assim que o app e o site gravam a matéria). */
    private fun chapters(theory: List<String>): List<Chapter> = theory.flatMap { md ->
        val parts = mutableListOf<Pair<String?, StringBuilder>>(null to StringBuilder())
        // Os capítulos costumam vir numerados ("## 1. Princípios"); aí os outros "##" são seções dentro deles.
        val numbered = Regex("""^##\s+\*{0,2}(cap[ií]tulo\s+)?\d+[.)\-–:]\s""", RegexOption.IGNORE_CASE)
        val onlyNumbered = md.lines().count { numbered.containsMatchIn(it.trim()) } >= 2
        md.lines().forEach { line ->
            val t = line.trim()
            val opens = t.startsWith("## ") && (!onlyNumbered || numbered.containsMatchIn(t))
            if (opens) parts += t.removePrefix("## ").trim().trim('*') to StringBuilder()
            else parts.last().second.append(line).append('\n')
        }
        parts.filter { it.second.isNotBlank() }.map { Chapter(it.first?.let(::chapterTitle), it.second.toString()) }
    }

    /** "1. Fundamentos, definições e tipologias" vira "Fundamentos, definições e tipologias". */
    private fun chapterTitle(title: String) = title.replace(Regex("""^(cap[ií]tulo\s+)?\d+[.)\-–:]?\s+""", RegexOption.IGNORE_CASE), "").trim()

    // ------------------------------------------------------------------ o mapa (resumo por partes)

    fun summaryBlocks(summary: String?, theory: List<String>): List<Block> = blocks(summary, chapters(theory))

    private fun blocks(summary: String?, chapters: List<Chapter>): List<Block> {
        if (summary.isNullOrBlank()) return emptyList()
        val parts = mutableListOf<Pair<String?, StringBuilder>>(null to StringBuilder())
        summary.lines().forEach { raw ->
            val line = raw.trim()
            val heading = HEADING.find(line)?.groupValues?.get(1)?.trim()?.trim('*')
            if (heading != null) parts += heading to StringBuilder() else parts.last().second.append(line).append('\n')
        }
        val built = parts.mapNotNull { (title, text) ->
            val lines = StudyNotebook.summaryLines(clean(text.toString()), max = 12)
            if (lines.isEmpty()) null else Block(title?.takeUnless { GENERIC.matches(it.trim()) || it.length > 70 }, lines)
        }
        // Resumo com partes de verdade: fica como está.
        if (built.count { it.title != null } >= 2) return built
        // Resumo corrido: cada frase vai para o capítulo da teoria que mais fala dela, na ordem da teoria.
        val titled = chapters.filter { it.title != null }
        val lines = built.flatMap { it.lines }
        if (titled.size < 2 || lines.size < 4) return listOf(Block(null, lines))
        // O título do capítulo pesa mais que o corpo (o corpo de todos fala um pouco de tudo).
        val titleWords = titled.map { ReviewMix.tokens(it.title.orEmpty()) }
        val bodyWords = titled.map { ch ->
            val counts = HashMap<String, Int>()
            ReviewMix.tokens(ch.body).forEach { counts[it] = (counts[it] ?: 0) + 1 }
            ch.body.lowercase().let { body -> counts.keys.associateWith { k -> Regex(Regex.escape(k)).findAll(body).count() } }
        }
        val home = lines.mapIndexed { index, line ->
            val t = ReviewMix.tokens(line)
            val scores = titled.indices.map { c -> t.count { it in titleWords[c] } * 3.0 + t.sumOf { minOf(bodyWords[c][it] ?: 0, 4) } / 4.0 }
            scores.withIndex().maxByOrNull { it.value }?.takeIf { it.value > 0 }?.index ?: -1
        }.let { raw ->
            // frase sem capítulo claro fica junto da anterior (o resumo segue a ordem da teoria)
            var last = 0
            raw.map { c -> if (c < 0) last else { last = c; c } }
        }
        // Mantém a ordem do resumo dentro de cada capítulo; capítulo sem frase não aparece.
        return titled.indices.mapNotNull { c ->
            val mine = lines.filterIndexed { i, _ -> home[i] == c }
            if (mine.isEmpty()) null else Block(titled[c].title, mine)
        }
    }

    /** Fórmula solta (depois de ponto) sai do texto e vai para o quadro de fórmulas; no meio da frase vira texto legível. */
    private fun clean(text: String): String = text
        .replace(Regex("""(^|[.:;]\s*|\*\*\s*)(\$\$[\s\S]+?\$\$\s*)+"""), "$1")
        .replace(DISPLAY_MATH) { Formula.readable(it.groupValues[1]) }
        .replace(Regex("""\$([^$\n]+?)\$""")) { Formula.readable(it.groupValues[1]) }
        // "pertinentes.** **Lucro bruto": o ponto ficava dentro do negrito e a frase não quebrava
        .replace(Regex("""([.;!?])\*\*(\s+)"""), "**$1$2")
        .replace(Regex("""\s+>\s+"""), " ")

    // ------------------------------------------------------------------ fórmulas

    private fun formulas(summary: String?, chapters: List<Chapter>, max: Int = 10): List<String> {
        val raw = (listOfNotNull(summary) + chapters.map { it.body }).flatMap { md -> DISPLAY_MATH.findAll(md).map { it.groupValues[1] }.toList() }
        val out = mutableListOf<String>()
        for (latex in raw) {
            // várias fórmulas no mesmo bloco ("\qquad", "\\", ";") viram linhas separadas
            for (piece in latex.split(Regex("""\\qquad|\\\\|\\quad|;\s"""))) {
                val f = Formula.readable(piece).trim().trim(',', '.')
                if (f.length in 3..120 && f.any { it == '=' || it == '≤' || it == '≥' || it == '≈' } && f !in out) out += f
                if (out.size >= max) return out
            }
        }
        return out
    }

    // ------------------------------------------------------------------ como cai na prova

    /** Os itens "Como cai na prova" de cada capítulo, agrupados por capítulo, sem repetir o mapa. */
    private fun exam(chapters: List<Chapter>, shown: List<MixCard>, max: Int = 14): List<Block> {
        val perChapter = chapters.map { ch ->
            val items = mutableListOf<String>()
            var inside = false
            val paragraph = StringBuilder()
            fun flush() { if (paragraph.isNotBlank()) items += StudyNotebook.summaryLines(paragraph.toString(), max = 6); paragraph.clear() }
            ch.body.lines().forEach { raw ->
                val line = raw.trim()
                val heading = HEADING.find(line)?.groupValues?.get(1)
                if (heading != null) { if (inside) flush(); inside = EXAM.containsMatchIn(heading); return@forEach }
                if (!inside || line.isEmpty() || line.startsWith("|") || line.startsWith(">")) { if (inside && line.isEmpty()) flush(); return@forEach }
                val bullet = line.startsWith("- ") || line.startsWith("* ") || line.startsWith("• ") || Regex("""^\d+[.)]\s""").containsMatchIn(line)
                val item = line.removePrefix("- ").removePrefix("* ").removePrefix("• ").replace(Regex("""^\d+[.)]\s+"""), "").trim()
                if (bullet) { flush(); items += item } else paragraph.append(item).append(' ')
            }
            if (inside) flush()
            ch.title to StudyNotebook.joinFragments(items.map { Formula.inline(it.trim()) }).filter { it.length in 20..360 }
        }
        // Dois por capítulo primeiro (todo capítulo aparece), depois o resto, até [max]; sem repetir nada.
        val kept = perChapter.map { mutableListOf<String>() }
        val seen = shown.toMutableList()
        fun take(c: Int, item: String): Boolean {
            val m = mix(item)
            if (seen.any { ReviewMix.similar(it, m) }) return false
            kept[c] += item; seen += m; return true
        }
        var total = 0
        for (round in 0 until 2) perChapter.forEachIndexed { c, (_, items) ->
            items.firstOrNull { it !in kept[c] && total < max && take(c, it) }?.let { total++ }
        }
        perChapter.forEachIndexed { c, (_, items) -> items.forEach { if (total < max && it !in kept[c] && take(c, it)) total++ } }
        return perChapter.indices.mapNotNull { c -> kept[c].takeIf { it.isNotEmpty() }?.let { Block(perChapter[c].first, it) } }
    }

    // ------------------------------------------------------------------ quadros

    /**
     * Um quadro por capítulo, na ordem da teoria (antes eram os 3 primeiros da matéria inteira): o que
     * compara ou estrutura, pequeno o bastante para a página. O título é o subtítulo mais próximo; se
     * ele for genérico ("Resumo") ou repetir outro quadro, entra o nome do capítulo.
     */
    private fun tables(chapters: List<Chapter>, max: Int = 4): List<Table> {
        val picks = mutableListOf<Table>()
        val usedTitles = mutableSetOf<String>()
        for (ch in chapters) {
            val found = mutableListOf<Pair<Table, Int>>()
            var title: String? = null
            val rows = mutableListOf<String>()
            fun close() {
                if (rows.size >= 3) {
                    val cols = rows.first().trim().trim('|').split('|').size
                    val dataRows = rows.size - 2
                    if (cols in 2..4 && dataRows in 2..10 && rows.all { it.length <= 280 }) {
                        val t = title
                        val score = when {
                            t != null && Regex("""×| x |compar|estrutura|diferen|quadro|esquema|regra|tipos|classifica|f[oó]rmula|resumo""", RegexOption.IGNORE_CASE).containsMatchIn(t) -> 0
                            else -> 1
                        }
                        found += Table(t, rows.joinToString("\n") { Formula.inline(it) }) to score
                    }
                }
                rows.clear()
            }
            ch.body.lines().forEach { raw ->
                val line = raw.trim()
                val heading = HEADING.find(line)?.groupValues?.get(1)?.trim()?.trim('*')
                if (line.startsWith("|")) rows += line else { close(); if (heading != null) title = heading }
            }
            close()
            val best = found.withIndex().minWithOrNull(compareBy({ it.value.second }, { it.index }))?.value?.first ?: continue
            val named = best.title?.takeUnless { GENERIC.matches(it.trim()) || EXAM.containsMatchIn(it) || it.lowercase() in usedTitles } ?: ch.title
            named?.let { usedTitles += it.lowercase() }
            picks += Table(named, best.markdown)
            if (picks.size >= max) break
        }
        return picks
    }
}

/**
 * Fórmula LaTeX em texto legível na tela (sem renderizador de matemática): \frac{a}{b} vira a/b,
 * \binom{n}{k} vira C(n,k), \sqrt{x} vira √x, letras gregas e operadores viram os símbolos.
 */
object Formula {
    private val SYMBOLS = listOf(
        "\\cdot" to "·", "\\times" to "×", "\\div" to "÷", "\\pm" to "±", "\\leq" to "≤", "\\geq" to "≥", "\\le" to "≤", "\\ge" to "≥",
        "\\neq" to "≠", "\\approx" to "≈", "\\infty" to "∞", "\\to" to "→", "\\rightarrow" to "→", "\\Rightarrow" to "⇒", "\\Delta" to "Δ",
        "\\delta" to "δ", "\\theta" to "θ", "\\pi" to "π", "\\alpha" to "α", "\\beta" to "β", "\\gamma" to "γ", "\\mu" to "μ", "\\sigma" to "σ",
        "\\Sigma" to "Σ", "\\lambda" to "λ", "\\omega" to "ω", "\\rho" to "ρ", "\\sum" to "Σ", "\\prod" to "Π", "\\ldots" to "…", "\\cdots" to "…",
        "\\dots" to "…", "\\%" to "%", "\\in" to "∈", "\\cap" to "∩", "\\cup" to "∪", "\\equiv" to "≡", "\\circ" to "°", "\\degree" to "°",
    )

    /** Texto com $...$ ou $$...$$ no meio: só as fórmulas viram legíveis. */
    fun inline(text: String): String =
        if ('$' !in text) text
        else text.replace(Regex("""\$\$([\s\S]+?)\$\$""")) { readable(it.groupValues[1]) }.replace(Regex("""\$([^$\n]+?)\$""")) { readable(it.groupValues[1]) }

    fun readable(latex: String): String {
        var s = latex.trim()
        s = s.replace(Regex("""\\(left|right|displaystyle|,|;|!|:)"""), " ").replace(Regex("""\\q?quad"""), "   ")
        s = s.replace(Regex("""\\(text|mathrm|mathbf|operatorname|textbf|mathit)\{([^{}]*)\}"""), "$2")
        // somatório com limites: Σ(k=0..n)
        s = s.replace(Regex("""\\(sum|prod)_\{([^{}]*)\}\^\{?([^{}\s]*)\}?""")) { (if (it.groupValues[1] == "sum") "Σ" else "Π") + "(" + it.groupValues[2] + ".." + it.groupValues[3] + ") " }
        // índice com vírgula vira argumento: A_{n,p} → A(n,p), como nas questões
        s = s.replace(Regex("""_\{([^{}]*,[^{}]*)\}""")) { "(" + it.groupValues[1] + ")" }
        // \frac e \binom podem estar um dentro do outro: repete até não sobrar
        repeat(4) {
            // de dentro para fora: expoentes e índices simples primeiro, depois fração, binomial e raiz
            s = s.replace(Regex("""\^\{([^{}]*)\}""")) { "^" + group(it.groupValues[1]) }
            s = s.replace(Regex("""_\{([^{},]*)\}""")) { "_" + it.groupValues[1] }
            s = s.replace(Regex("""\\[dt]?frac\{([^{}]*)\}\{([^{}]*)\}""")) { "${group(it.groupValues[1])}/${group(it.groupValues[2])}" }
            s = s.replace(Regex("""\\binom\{([^{}]*)\}\{([^{}]*)\}""")) { "C(${it.groupValues[1]},${it.groupValues[2]})" }
            s = s.replace(Regex("""\\sqrt\{([^{}]*)\}""")) { "√${group(it.groupValues[1])}" }
        }
        SYMBOLS.sortedByDescending { it.first.length }.forEach { (k, v) -> s = s.replace(Regex(Regex.escape(k) + """(?![a-zA-Z])"""), v) }
        s = s.replace(Regex("""\^\{([^{}]*)\}""")) { "^" + group(it.groupValues[1]) }
        s = s.replace(Regex("""_\{([^{}]*)\}""")) { "_" + it.groupValues[1] }
        s = s.replace(Regex("""\\([a-zA-Z]+)"""), "$1").replace("{", "").replace("}", "")
        return s.replace(Regex("""\s+"""), " ").replace(Regex("""\s*([·×÷])\s*"""), "$1").trim()
    }

    /** Parênteses só quando a parte tem mais de um termo: a/b, (a+b)/(c). */
    private fun group(part: String): String = part.trim().let {
        if (it.length <= 1 || Regex("""^[\w.!]+$""").matches(it) || (it.startsWith("(") && it.endsWith(")") && it.count { c -> c == '(' } == 1)) it else "($it)"
    }
}
