package br.com.estudario.data.ai

/**
 * O texto do edital que vai para a IA, no lugar do PDF inteiro.
 *
 * Mandar o PDF faz a OpenAI ler cada página duas vezes (texto e imagem), e um edital de 64 páginas
 * passa de 120 mil tokens. Para montar o estudo só interessa o conteúdo programático, que costuma
 * ser um anexo de poucas páginas. Aqui o app lê o texto no próprio celular (sem limite de CPU do
 * servidor), acha esse anexo e manda só ele, mais a primeira página (concurso e cargo).
 */
data class AiSourceText(
    val text: String,
    /** Páginas enviadas, para a IA e para o registro, ex.: "1, 51-60". */
    val pages: String,
    val totalPages: Int,
    /** true quando achou o conteúdo programático; false quando mandou o texto todo. */
    val focused: Boolean,
)

object EditalSectionFinder {
    /** Teto de texto enviado (~110 mil tokens, abaixo do maior edital que já passou pelo servidor). */
    const val MAX_CHARS = 400_000

    /** Abaixo disso não é o conteúdo de verdade (é um título solto, uma citação). */
    private const val MIN_SECTION_CHARS = 1_500

    private val START = Regex(
        """conte[úu]dos?\s+program[áa]ticos?|objetos?\s+de\s+avalia[çc][ãa]o|conhecimentos\s+(?:b[áa]sicos|gerais|espec[íi]ficos)|programas?\s+(?:das?\s+(?:provas|disciplinas|mat[ée]rias)|de\s+mat[ée]rias)""",
        RegexOption.IGNORE_CASE,
    )

    /**
     * Título de anexo: "ANEXO III - ...", "ANEXO 2: ...", "ANEXO “B” - ..." (PMMG usa letras entre
     * aspas). O separador é obrigatório, para não confundir com "conforme o Anexo I deste edital".
     */
    private val ANNEX_HEADING = Regex(
        """\banexo\s+(?:[“"'][a-z]{1,2}[”"']|[ivxlc]+|\d+)\s*[-\u2013\u2014:.]\s*(\S.{0,120})""",
        RegexOption.IGNORE_CASE,
    )
    private val CONTENT_WORDS = Regex(
        """conte[úu]do|program[áa]tic|programa\s+d|conhecimentos|disciplinas|mat[ée]rias|objetos?\s+de\s+avalia|bibliografia""",
        RegexOption.IGNORE_CASE,
    )
    /** Linha de sumário ("ANEXO B - PROGRAMA ........ 95"): cita o anexo, mas não é ele. */
    private val TOC = Regex("""\.{4,}|…{2,}|_{4,}""")

    private data class Line(val page: Int, val text: String)

    private enum class Kind { CONTENT_ANNEX, CONTENT_TITLE, OTHER_ANNEX, NONE }

    private fun kind(line: String): Kind {
        if (TOC.containsMatchIn(line)) return Kind.NONE
        ANNEX_HEADING.find(line)?.let { match ->
            return if (CONTENT_WORDS.containsMatchIn(match.groupValues[1])) Kind.CONTENT_ANNEX else Kind.OTHER_ANNEX
        }
        // Título solto ("CONTEÚDO PROGRAMÁTICO", "CONHECIMENTOS BÁSICOS"): linha curta e em caixa alta.
        val trimmed = line.trim()
        if (trimmed.length in 8..90 && START.containsMatchIn(trimmed)) {
            val letters = trimmed.filter(Char::isLetter)
            if (letters.isNotEmpty() && letters.count(Char::isUpperCase) >= letters.length * 0.7) return Kind.CONTENT_TITLE
        }
        return Kind.NONE
    }

    /**
     * Escolhe o texto. Lê linha a linha: o conteúdo programático começa num título de anexo de
     * conteúdo ("ANEXO II - CONTEÚDO PROGRAMÁTICO", "ANEXO “B” - PROGRAMA DE MATÉRIAS") ou, se o
     * edital não usa anexos, num título solto em caixa alta, e vai até o próximo anexo que não é de
     * conteúdo (cronograma, teste físico, modelos). Começa e termina no meio da página quando
     * preciso, então as regras do edital antes dele não vão junto. Entre vários candidatos, fica o
     * trecho mais longo: o sumário e as citações nas regras são curtos.
     */
    fun select(pages: List<String>): AiSourceText? {
        if (pages.isEmpty() || pages.all { it.isBlank() }) return null
        val lines = pages.flatMapIndexed { page, text -> text.lines().map { Line(page, it) } }
        val kinds = lines.map { kind(it.text) }

        fun sectionFrom(start: Int): IntRange {
            val end = ((start + 1) until lines.size).firstOrNull { kinds[it] == Kind.OTHER_ANNEX } ?: lines.size
            return start until end
        }
        fun length(range: IntRange) = range.sumOf { lines[it].text.length + 1 }
        fun best(kind: Kind) = lines.indices.filter { kinds[it] == kind }.map(::sectionFrom)
            .filter { length(it) >= MIN_SECTION_CHARS }.maxByOrNull(::length)

        val section = best(Kind.CONTENT_ANNEX) ?: best(Kind.CONTENT_TITLE)
        if (section == null) {
            // Não achou: manda tudo, menos os anexos de formulário/modelo no fim (só atrapalham).
            val firstForm = lines.indices.firstOrNull { kinds[it] == Kind.OTHER_ANNEX && lines[it].page >= pages.size / 2 }
            val keep = if (firstForm == null) pages.indices.toList() else (0..lines[firstForm].page).toList()
            val keptLines = lines.indices.filter { lines[it].page in keep && (firstForm == null || it < firstForm) }
            return build(lines, keptLines, pages.size, focused = false)
        }
        val firstPage = lines.indices.filter { lines[it].page == 0 && lines[it].page != lines[section.first].page }
        return build(lines, firstPage + section.toList(), pages.size, focused = true)
    }

    private fun build(lines: List<Line>, chosen: List<Int>, totalPages: Int, focused: Boolean): AiSourceText {
        // Página que só entrou com o cabeçalho antes do próximo anexo não conta.
        val byPage = chosen.groupBy { lines[it].page }.filter { (page, indices) -> page == 0 || indices.sumOf { lines[it].text.trim().length } >= 120 }.toSortedMap()
        val text = byPage.entries.joinToString("\n\n") { (page, indices) ->
            "--- Página ${page + 1} ---\n" + indices.joinToString("\n") { lines[it].text }.trim()
        }
        return AiSourceText(
            text = if (text.length > MAX_CHARS) text.take(MAX_CHARS) else text,
            pages = ranges(byPage.keys.map { it + 1 }),
            totalPages = totalPages,
            focused = focused && byPage.size < totalPages,
        )
    }

    internal fun ranges(numbers: List<Int>): String {
        if (numbers.isEmpty()) return ""
        val out = mutableListOf<String>()
        var from = numbers.first(); var to = from
        for (n in numbers.drop(1)) {
            if (n == to + 1) to = n else { out += if (from == to) "$from" else "$from-$to"; from = n; to = n }
        }
        out += if (from == to) "$from" else "$from-$to"
        return out.joinToString(", ")
    }
}
