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

    private val START = Regex(
        """conte[úu]dos?\s+program[áa]ticos?|objetos?\s+de\s+avalia[çc][ãa]o|conhecimentos\s+(?:b[áa]sicos|gerais|espec[íi]ficos)\s*[:\-\u2013]|programa\s+das?\s+(?:provas|disciplinas)""",
        RegexOption.IGNORE_CASE,
    )
    /** Título de anexo no topo da página ("ANEXO III \u2013 ..."). */
    private val ANNEX_HEADING = Regex("""\banexo\s+(?:[ivxlc]+|\d+)\b\s*[-\u2013\u2014:.]?\s*(.{0,120})""", RegexOption.IGNORE_CASE)
    private val CONTENT_WORDS = Regex("""conte[úu]do|program[áa]tic|conhecimentos|disciplinas|mat[ée]rias|objetos?\s+de\s+avalia""", RegexOption.IGNORE_CASE)

    /** Outro anexo começou (cronograma, tabelas do teste físico, modelos...): fim do conteúdo. */
    private fun startsOtherAnnex(page: String): Boolean {
        val head = page.take(300)
        val match = ANNEX_HEADING.find(head) ?: return false
        return !CONTENT_WORDS.containsMatchIn(match.groupValues[1])
    }

    /**
     * Escolhe as páginas. Procura o último bloco que se anuncia como conteúdo programático (o
     * sumário do começo também cita o nome, mas o anexo de verdade vem depois e é mais denso) e
     * segue até outro anexo começar.
     */
    fun select(pages: List<String>): AiSourceText? {
        if (pages.isEmpty() || pages.all { it.isBlank() }) return null
        val starts = pages.indices.filter { START.containsMatchIn(pages[it]) }
        val start = starts.lastOrNull { index -> isRealSection(pages, index) } ?: starts.firstOrNull()
        val chosen = if (start == null) pages.indices.toList() else {
            val end = ((start + 1) until pages.size).firstOrNull { startsOtherAnnex(pages[it]) } ?: pages.size
            (listOf(0) + (start until end)).distinct()
        }
        val focused = start != null && chosen.size < pages.size
        val text = chosen.joinToString("\n\n") { "--- Página ${it + 1} ---\n${pages[it].trim()}" }
        return AiSourceText(
            text = if (text.length > MAX_CHARS) text.take(MAX_CHARS) else text,
            pages = ranges(chosen.map { it + 1 }),
            totalPages = pages.size,
            focused = focused,
        )
    }

    /** Página de anexo de verdade: o título aparece no começo e a página tem conteúdo longo. */
    private fun isRealSection(pages: List<String>, index: Int): Boolean {
        val page = pages[index]
        val at = START.find(page)?.range?.first ?: return false
        return at < 600 && page.length > 800
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
