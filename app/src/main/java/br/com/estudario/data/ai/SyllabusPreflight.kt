package br.com.estudario.data.ai

import java.text.Normalizer
import java.util.Locale

enum class SyllabusPreflightKind { VALID, VALID_WITH_WARNING, NOT_AN_EDITAL, CONTENT_NOT_FOUND, CANNOT_VALIDATE }

data class SyllabusPreflightResult(
    val kind: SyllabusPreflightKind,
    val documentIdentification: String? = null,
    val warnings: List<String> = emptyList(),
    /** Páginas do conteúdo programático achado no celular, ex.: "95-103". null quando não achou com segurança. */
    val contentPages: String? = null,
    /** Matérias lidas nos títulos do conteúdo. Vazio quando a leitura não é confiável: aí não mostramos nada. */
    val subjects: List<String> = emptyList(),
) {
    val canGenerate: Boolean get() = kind !in setOf(SyllabusPreflightKind.NOT_AN_EDITAL, SyllabusPreflightKind.CONTENT_NOT_FOUND)
}

/** Fingerprint of a private local snapshot. Preparing it never creates a remote job. */
data class PreparedSyllabusSource(
    val uri: String,
    val fileName: String,
    val snapshotPath: String,
    val sha256: String,
    val sizeBytes: Long,
    val pages: List<String>,
    val attemptId: String = java.util.UUID.randomUUID().toString(),
) {
    fun preflight(context: AiSyllabusPreferences) = SyllabusPreflight.inspect(pages, context)
}

/** Conservative evidence checks; document identification is always an estimate. */
object SyllabusPreflight {
    private val official = Regex("\\b(edital|concurso publico|processo seletivo|selecao publica|curso de formacao|prova objetiva)\\b")
    private val program = Regex("conteudos? programaticos?|programa (?:de|das?) (?:materias|provas?|disciplinas)|objetos? de avaliacao|conhecimentos (?:basicos|especificos|gerais)")
    private val discipline = Regex("\\b(portugues|matematica|direito|informatica|raciocinio|historia|geografia|biologia|quimica|fisica|administracao|contabilidade|engenharia|legislacao)\\b")
    private val unrelated = Regex("\\b(boleto|linha digitavel|curriculum vitae|curriculo profissional|comprovante de pagamento|comprovante de transferencia|resumo academico|artigo cientifico|gabarito|caderno de questoes)\\b")
    private val administrative = Regex("\\b(cronograma|calendario|resultado|comunicado|inscricoes|retificacao|convocacao)\\b")
    private val stopWords = setOf("de", "da", "do", "das", "dos", "e", "para", "ao", "a", "o", "publico", "concurso", "edital", "curso", "formacao", "area", "cargo")

    fun inspect(pages: List<String>, context: AiSyllabusPreferences): SyllabusPreflightResult {
        val text = normalize(pages.joinToString("\n").take(EditalSectionFinder.MAX_CHARS))
        val header = pages.take(3).joinToString("\n")
        val identification = header.lines().map(String::trim).filter {
            val line = normalize(it)
            it.length in 8..240 && (official.containsMatchIn(line) || Regex("policia|tribunal|ministerio|universidade|prefeitura|secretaria|soldad|analista|tecnico").containsMatchIn(line))
        }.distinct().take(5).joinToString("\n").takeIf(String::isNotBlank)
        val section = runCatching { EditalSectionFinder.select(pages) }.getOrNull()?.takeIf { it.focused }
        val contentPages = section?.pages?.split(", ")?.filterNot { it == "1" }?.joinToString(", ")?.takeIf(String::isNotBlank)
        val subjects = section?.let { SubjectHeadings.find(it.text) }.orEmpty()
        fun result(kind: SyllabusPreflightKind, warnings: List<String> = emptyList()) =
            SyllabusPreflightResult(kind, identification, warnings, contentPages.takeIf { kind != SyllabusPreflightKind.NOT_AN_EDITAL }, subjects)
        // Missing page text makes absence of a section unreliable (including mixed/scanned PDFs).
        val incomplete = pages.isEmpty() || pages.any { it.count(Char::isLetter) < 40 } || pages.sumOf { it.length } > EditalSectionFinder.MAX_CHARS
        if (text.count(Char::isLetter) < 100) return result(SyllabusPreflightKind.CANNOT_VALIDATE)
        val officialEvidence = official.findAll(text).map { it.value }.toSet().size
        val programEvidence = program.containsMatchIn(text)
        val disciplines = discipline.findAll(text).map { it.value }.toSet().size
        val numberedItems = Regex("(?m)^\\s*\\d+[.)]\\s+\\S").findAll(pages.joinToString("\n")).count()
        val hasContent = (programEvidence && (disciplines >= 2 || numberedItems >= 3)) ||
            (officialEvidence >= 1 && disciplines >= 3 && numberedItems >= 3)
        if (!hasContent) {
            if (incomplete) return result(SyllabusPreflightKind.CANNOT_VALIDATE)
            if (unrelated.containsMatchIn(text) && officialEvidence == 0 && !programEvidence) return result(SyllabusPreflightKind.NOT_AN_EDITAL)
            if (!incomplete && officialEvidence >= 1 && !programEvidence && disciplines == 0 && administrative.containsMatchIn(text)) return result(SyllabusPreflightKind.CONTENT_NOT_FOUND)
            return result(SyllabusPreflightKind.CANNOT_VALIDATE)
        }
        val warnings = buildList {
            val identityText = identification ?: header
            if (likelyMismatch(context.competitionName, identityText)) add("O concurso informado (${context.competitionName}) pode ser diferente do documento identificado. Confira antes de gerar.")
            if (likelyMismatch(context.role, identityText)) add("O cargo/área informado (${context.role}) não foi identificado na capa. Confira se o documento contém o cargo desejado.")
            if (incomplete) add("Algumas páginas não têm texto suficiente para conferência local. Revise o resultado da análise.")
        }
        return result(if (warnings.isEmpty()) SyllabusPreflightKind.VALID else SyllabusPreflightKind.VALID_WITH_WARNING, warnings)
    }

    internal fun normalize(value: String): String = Normalizer.normalize(value.lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "").replace(Regex("[^a-z0-9\\s]+"), " ").replace(Regex("\\s+"), " ").trim()

    private fun words(value: String) = normalize(value).split(' ').filter { it.isNotBlank() && it !in stopWords }

    private fun likelyMismatch(value: String, document: String): Boolean {
        val input = words(value).filter { it.any(Char::isLetter) }
        val source = words(document).filter { it.any(Char::isLetter) }
        if (input.isEmpty() || source.isEmpty()) return false
        val compact = input.joinToString("")
        // Short names and acronyms provide too little evidence for a strong mismatch.
        if (compact.length <= 4) return false
        if (normalize(document).contains(normalize(value))) return false
        val matched = input.count { a -> source.any { b -> similarWord(a, b) } }
        if (matched.toDouble() / input.size >= 0.6) return false
        val initials = source.filter { it.firstOrNull()?.isLetter() == true }.joinToString("") { it.take(1) }
        if (initials.contains(compact)) return false
        return input.size >= 2 || compact.length >= 6
    }

    private fun similarWord(a: String, b: String): Boolean {
        if (a == b || (minOf(a.length, b.length) >= 5 && a.take(5) == b.take(5))) return true
        if (minOf(a.length, b.length) < 5 || kotlin.math.abs(a.length - b.length) > 1) return false
        var i = 0; var j = 0; var differences = 0
        while (i < a.length && j < b.length) {
            if (a[i] == b[j]) { i++; j++; continue }
            if (++differences > 1) return false
            when { a.length > b.length -> i++; b.length > a.length -> j++; else -> { i++; j++ } }
        }
        return differences + (a.length - i) + (b.length - j) <= 1
    }
}

/**
 * Lê os títulos das matérias no conteúdo programático ("LÍNGUA PORTUGUESA", "1. DIREITO PENAL").
 * Prefere títulos em linha própria; se o edital escreve tudo corrido (PMMG), pega o título em caixa
 * alta antes do primeiro item. Só devolve algo quando o resultado parece uma lista de matérias.
 */
internal object SubjectHeadings {
    private const val UPPER = "A-ZÁÉÍÓÚÂÊÔÃÕÇÀ"
    private val OWN_LINE = Regex("""^\s*(?:\d{1,2}\s*[.)]?\s+)?([$UPPER][$UPPER\-,/ ]{3,80}[$UPPER])\s*:?\s*$""")
    private val INLINE = Regex("""(?:^|\s)(?:\d{1,2}\s*[.)]?\s+)?([$UPPER][$UPPER\-,/ ]{3,70}?[$UPPER])(?=\s*(?:[:.]|\d|\s[A-Z][a-zà-ú]|$))""", RegexOption.MULTILINE)
    private val NOT_SUBJECT = Regex("""^(ANEXO|EDITAL|PROGRAMA|CONTE[ÚU]DO|CONHECIMENTOS|PROVA|CARGO|NOTA|OBS|DATA|LOCAL|BIBLIOGRAFIA|REFER|P[ÁA]GINA)""")
    private val CONNECTORS = setOf("DE", "DA", "DO", "DAS", "DOS", "E", "AO", "À", "A", "EM")

    fun find(text: String): List<String> {
        // A capa (página 1) vai junto para a IA, mas os títulos dela são do concurso, não matérias.
        val withoutCover = if (text.startsWith("--- Página 1 ---")) text.substringAfter("\n--- Página ", "").let { if (it.isEmpty()) "" else "--- Página $it" } else text
        val lines = withoutCover.lines().filterNot { it.startsWith("--- Página") }
        val ownLine = lines.mapNotNull { OWN_LINE.matchEntire(it)?.groupValues?.get(1) }.mapNotNull(::clean).distinct()
        val found = if (ownLine.size >= 4) ownLine else INLINE.findAll(lines.joinToString("\n")).map { it.groupValues[1] }.mapNotNull(::clean).distinct().toList()
        return found.takeIf { it.size in 2..40 }.orEmpty()
    }

    private fun clean(raw: String): String? {
        val value = raw.replace(Regex("\\s+"), " ").trim().trimEnd(',', '-', '/')
        if (NOT_SUBJECT.containsMatchIn(value)) return null
        val words = value.split(' ')
        if (words.size > 8 || value.count(Char::isLetter) < 6) return null
        val meaningful = words.filter { it !in CONNECTORS }
        // Nome solto (assinatura, sigla): duas palavras curtas sem conector não é matéria.
        if (words.size == 2 && meaningful.none { it.length >= 7 }) return null
        if (meaningful.size == 1 && meaningful[0].length < 7) return null
        return value.lowercase().split(' ').mapIndexed { i, w -> if (i > 0 && w.uppercase() in CONNECTORS) w else w.replaceFirstChar(Char::uppercase) }.joinToString(" ")
    }
}
