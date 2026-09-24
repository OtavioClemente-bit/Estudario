package br.com.estudario.data.transfer

import android.database.sqlite.SQLiteConstraintException
import androidx.room.withTransaction
import br.com.estudario.data.local.*
import br.com.estudario.domain.*
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

enum class ImportMode { SKIP, UPDATE, COPY }

data class SubjectImportPreview(
    val name: String,
    val topicCount: Int,
    val subtopicCount: Int,
    val theoryCount: Int,
    val summaryCount: Int,
    val questionCount: Int,
    val snippetCount: Int = 0,
    val errorConceptCount: Int = 0,
    val topics: List<TopicImportPreview> = emptyList(),
)

data class TopicImportPreview(val title: String, val depth: Int, val theoryCount: Int, val summaryCount: Int, val questionCount: Int, val snippetCount: Int)

data class EstudoPreview(
    val version: Int,
    val packageId: String,
    val competition: String,
    val subjects: List<SubjectImportPreview>,
    val topicCount: Int,
    val subtopicCount: Int,
    val theoryCount: Int,
    val summaryCount: Int,
    val questionCount: Int,
    val duplicateCount: Int,
    val snippetCount: Int = 0,
    val errorConceptCount: Int = 0,
    val packageAlreadyImported: Boolean = false,
    val downgradedQuestions: Int = 0,
    val sourceCount: Int = 0,
)

data class ImportResult(
    val subjectsCreated: Int,
    val topicsCreated: Int,
    val topicsUpdated: Int,
    val theories: Int,
    val summaries: Int,
    val questions: Int,
    val skipped: Int,
    val snippets: Int = 0,
    val errorConcepts: Int = 0,
    val updatedContent: Int = 0,
    val importedTopicIds: List<Long> = emptyList(),
    /** Questões que diziam ser de prova real sem apontar origem e foram tratadas como autorais. */
    val downgradedQuestions: Int = 0,
    val sources: Int = 0,
    val normalizedPriorityAssessments: Int = 0,
)

class EstudoPackageException(message: String) : IllegalArgumentException(message)

internal data class OptionPlan(val key: String, val text: String, val correct: Boolean)
internal data class SummaryPlan(val id: String, val title: String, val markdown: String, val kind: SummaryKind = SummaryKind.COMPLETO)
internal data class TheoryPlan(val id: String, val title: String, val markdown: String)
internal data class SnippetPlan(val id: String, val kind: SnippetKind, val text: String)
internal data class ErrorConceptPlan(val id: String, val title: String, val summary: String)
internal data class SourcePlan(
    val id: String?,
    val kind: SourceKind,
    val title: String,
    val publisher: String,
    val reference: String,
    val url: String?,
    val accessedAt: String,
)
internal data class QuestionPlan(
    val id: String,
    val board: String?,
    val agency: String?,
    val year: Int?,
    val difficulty: Difficulty?,
    val source: String?,
    val sourceType: QuestionSourceType,
    val sourceId: String?,
    val sourceUrl: String?,
    val statement: String,
    val explanation: String,
    val notes: String,
    val tags: List<String>,
    val options: List<OptionPlan>,
    val reviewAnchor: String? = null,
    /** Veio marcada como prova real sem nada que comprove; o app rebaixou para autoral. */
    val downgraded: Boolean = false,
    val errorConceptId: String? = null,
)
internal data class TopicPlan(
    val id: String,
    val title: String,
    val description: String,
    val notes: String,
    val position: Int,
    val priority: Priority,
    val priorityAssessment: PriorityAssessment? = null,
    val legacyPriorityProvided: Boolean = false,
    val originType: ContentOriginType,
    val theories: List<TheoryPlan>,
    val summaries: List<SummaryPlan>,
    val snippets: List<SnippetPlan>,
    val questions: List<QuestionPlan>,
    val errorConcepts: List<ErrorConceptPlan>,
    val children: List<TopicPlan>,
    val sources: List<SourcePlan> = emptyList(),
    val scopeCovers: String? = null,
    val scopeExcludes: String? = null,
    val externalId: String = id,
    val parentExternalId: String? = null,
    val sourcePages: List<Int> = emptyList(),
)
internal data class SubjectPlan(
    val id: String,
    val name: String,
    val position: Int,
    val topics: List<TopicPlan>,
    val priorityAssessment: PriorityAssessment? = null,
    val externalId: String = id,
    val priority: Priority = Priority.NORMAL,
    val sourcePages: List<Int> = emptyList(),
)
internal data class PackageWarning(
    val code: String,
    val severity: String,
    val message: String,
    val sourcePages: List<Int> = emptyList(),
    val ambiguity: String? = null,
)
internal data class PackagePlan(
    val version: Int,
    val packageId: String,
    val competitionId: String,
    val competitionName: String,
    val primary: Boolean,
    val subjects: List<SubjectPlan>,
    val priorityAssessment: PriorityAssessment? = null,
    val normalizedPriorityCount: Int = 0,
    /** Fontes declaradas no nível do pacote, quando não são de um tópico específico. */
    val sources: List<SourcePlan> = emptyList(),
    val schemaVersion: Int = 1,
    val packageVersion: String = "estudo-v$version",
    val metadata: JSONObject? = null,
    val warnings: List<PackageWarning> = emptyList(),
)

internal fun PackagePlan.allTopics(): List<TopicPlan> {
    fun flatten(topic: TopicPlan): List<TopicPlan> = listOf(topic) + topic.children.flatMap(::flatten)
    return subjects.flatMap { it.topics }.flatMap(::flatten)
}

internal fun PackagePlan.sourceCount(): Int = sources.size + allTopics().sumOf { it.sources.size }

internal object EstudoPackageParser {
    fun parse(text: String): PackagePlan {
        val root = try { JSONObject(text) } catch (_: Exception) { throw EstudoPackageException("O arquivo não contém JSON válido.") }
        return when (val version = root.optInt("version", 1)) {
            1 -> parseFlat(root, 1)
            2 -> if (root.optJSONArray("materias") != null) parseHierarchical(root) else parseFlat(root, 2)
            else -> throw EstudoPackageException("Versão não suportada: $version. Use 1 ou 2.")
        }
    }

    private fun parseFlat(root: JSONObject, version: Int): PackagePlan {
        val competition = firstText(root, "competition", "concurso") ?: throw EstudoPackageException("Informe competition (ou concurso).")
        val subject = firstText(root, "subject", "materia") ?: throw EstudoPackageException("Informe subject (ou materia).")
        val topic = firstText(root, "topic", "topico") ?: throw EstudoPackageException("Informe topic (ou topico).")
        val packageId = firstText(root, "packageId") ?: "flat-${slug(competition)}-${slug(subject)}-${slug(topic)}"
        val ids = IdSets()
        val topicJson = JSONObject(root.toString()).put("id", firstText(root, "topicId") ?: "topic-${slug(topic)}").put("titulo", topic)
        if (root.optJSONArray("questions") != null && root.optJSONArray("questoes") == null) topicJson.put("questoes", root.optJSONArray("questions"))
        if (root.optJSONArray("theories") != null && root.optJSONArray("teorias") == null) topicJson.put("teorias", root.optJSONArray("theories"))
        val plan = parseTopic(topicJson, topic, 0, QuestionDefaults(tags = root.optJSONArray("tags")?.strings().orEmpty()), ids, false)
        return PackagePlan(version, packageId, firstText(root, "competitionId", "concursoId") ?: "competition-${slug(competition)}", competition, root.optBoolean("primary", false), listOf(SubjectPlan("subject-${slug(subject)}", subject, 0, listOf(plan))), priorityAssessment = parsePriorityAssessment(root, "pacote", ids), sources = parseSources(root, packageId), normalizedPriorityCount = ids.normalizedPriorityCount, schemaVersion = schemaVersion(root), packageVersion = packageVersion(root, version), metadata = copyMetadata(root), warnings = parseWarnings(root.optJSONArray("warnings") ?: root.optJSONObject("metadata")?.optJSONArray("warnings")))
    }

    private fun parseHierarchical(root: JSONObject): PackagePlan {
        val packageId = requireText(root, "packageId", "raiz")
        val competition = root.optJSONObject("concurso") ?: throw EstudoPackageException("O campo concurso deve ser um objeto.")
        val materials = root.optJSONArray("materias") ?: throw EstudoPackageException("O campo materias deve ser uma lista.")
        if (materials.length() == 0) throw EstudoPackageException("Inclua pelo menos uma matéria.")
        val defaultsJson = root.optJSONObject("padroesQuestao")
        val defaults = QuestionDefaults(
            board = defaultsJson?.optNullableString("banca"),
            agency = defaultsJson?.optNullableString("orgao"),
            year = defaultsJson?.optInt("ano")?.takeIf { it > 0 },
            difficulty = parseDifficulty(defaultsJson?.optNullableString("dificuldade"), "padroesQuestao"),
            source = defaultsJson?.optNullableString("origem"),
            tags = defaultsJson?.optJSONArray("tags")?.strings().orEmpty(),
        )
        val ids = IdSets()
        val subjects = materials.objects().mapIndexed { index, item ->
            val path = "matéria ${index + 1}"
            val id = requireText(item, "id", path)
            if (!ids.subjects.add(id)) throw EstudoPackageException("$path: id de matéria duplicado: $id.")
            val externalId = firstText(item, "externalId") ?: id
            if (!ids.subjectExternalIds.add(externalId)) throw EstudoPackageException("$path: externalId de matéria duplicado: $externalId.")
            val name = requireText(item, "nome", path)
            val topics = item.optJSONArray("topicos") ?: throw EstudoPackageException("$path: topicos deve ser uma lista.")
            SubjectPlan(id, name, item.optInt("ordem", index), topics.objects().mapIndexed { i, topic ->
                parseTopic(topic, "$name › tópico ${i + 1}", i, defaults, ids, true)
            }, parsePriorityAssessment(item, path, ids), externalId, parsePriority(item, path), parseSourcePages(item))
        }
        val competitionName = requireText(competition, "nome", "concurso")
        return PackagePlan(
            version = 2,
            packageId = packageId,
            competitionId = firstText(competition, "id") ?: "competition-${slug(competitionName)}",
            competitionName = competitionName,
            primary = competition.optBoolean("principal"),
            subjects = subjects,
            priorityAssessment = parsePriorityAssessment(competition, "concurso", ids),
            normalizedPriorityCount = ids.normalizedPriorityCount,
            sources = parseSources(root, packageId),
            schemaVersion = schemaVersion(root),
            packageVersion = packageVersion(root, 2),
            metadata = copyMetadata(root),
            warnings = parseWarnings(root.optJSONArray("warnings") ?: root.optJSONObject("metadata")?.optJSONArray("warnings")),
        )
    }

    private fun parseTopic(item: JSONObject, path: String, defaultPosition: Int, defaults: QuestionDefaults, ids: IdSets, requireIds: Boolean): TopicPlan {
        val id = if (requireIds) requireText(item, "id", path) else item.optString("id", "topic-${slug(path)}")
        if (!ids.topics.add(id)) throw EstudoPackageException("$path: id de tópico duplicado: $id.")
        val externalId = firstText(item, "externalId") ?: id
        if (!ids.topicExternalIds.add(externalId)) throw EstudoPackageException("$path: externalId de tópico duplicado: $externalId.")
        val title = firstText(item, "titulo", "title", "topic", "topico") ?: throw EstudoPackageException("$path: título ausente.")
        val legacyPriorityProvided = item.has("prioridade")
        val priority = Priority.entries.firstOrNull { it.name == item.optString("prioridade", "NORMAL").uppercase() }
            ?: throw EstudoPackageException("$path: prioridade deve ser BAIXA, NORMAL ou ALTA.")
        val snippets = buildList {
            addAll(parseSnippets(item.optJSONArray("tips") ?: item.optJSONArray("bizus"), path, id, SnippetKind.BIZU, ids.snippets))
            addAll(parseSnippets(item.optJSONArray("traps") ?: item.optJSONArray("pegadinhas"), path, id, SnippetKind.PEGADINHA, ids.snippets))
            addAll(parseSnippets(item.optJSONArray("activeRecall") ?: item.optJSONArray("recuperacaoAtiva"), path, id, SnippetKind.RECUPERACAO, ids.snippets))
        }
        val children = (item.optJSONArray("subtopicos") ?: JSONArray()).objects().mapIndexed { i, child ->
            parseTopic(child, "$path › $title › subtópico ${i + 1}", i, defaults, ids, true)
        }
        return TopicPlan(
            id = id,
            title = title,
            description = firstText(item, "descricao", "description").orEmpty(),
            notes = firstText(item, "observacoes", "notes").orEmpty(),
            position = item.optInt("ordem", defaultPosition),
            priority = priority,
            priorityAssessment = parsePriorityAssessment(item, path, ids),
            legacyPriorityProvided = legacyPriorityProvided,
            originType = parseOriginType(firstText(item, "contentOriginType", "tipoOrigem")),
            theories = parseTheories(item.optJSONArray("teorias") ?: JSONArray(), path, ids.theories, requireIds),
            summaries = parseSummaries(item, path, ids.summaries, requireIds),
            snippets = snippets,
            questions = parseQuestions(item.optJSONArray("questoes") ?: JSONArray(), path, defaults.copy(tags = (defaults.tags + item.optJSONArray("tags")?.strings().orEmpty()).distinct()), ids.questions, requireIds),
            errorConcepts = parseErrorConcepts(item.optJSONArray("errorConcepts") ?: item.optJSONArray("conceitosDeErro"), path, id, ids.errorConcepts),
            children = children,
            sources = parseSources(item, id),
            scopeCovers = item.optJSONObject("escopo")?.let { firstText(it, "cobre", "covers") },
            scopeExcludes = item.optJSONObject("escopo")?.let { firstText(it, "naoCobre", "excludes") },
            externalId = externalId,
            parentExternalId = item.optNullableString("parentExternalId"),
            sourcePages = parseSourcePages(item),
        )
    }

    private fun parseTheories(array: JSONArray, path: String, ids: MutableSet<String>, requireIds: Boolean) = array.objects().mapIndexed { index, item ->
        val label = "$path › teoria ${index + 1}"
        val id = if (requireIds) requireText(item, "id", label) else item.optString("id", "theory-$index")
        if (!ids.add(id)) throw EstudoPackageException("$label: id duplicado: $id.")
        val title = firstText(item, "titulo", "title") ?: "Teoria completa"
        val chapters = item.optJSONArray("capitulos") ?: item.optJSONArray("chapters")
        val markdown = if (chapters != null && chapters.length() > 0) chapters.objects().mapIndexed { i, chapter ->
            val chapterTitle = firstText(chapter, "titulo", "title") ?: "Capítulo ${i + 1}"
            val body = firstText(chapter, "markdown", "content") ?: throw EstudoPackageException("$label: capítulo sem conteúdo.")
            "## $chapterTitle\n\n$body"
        }.joinToString("\n\n") else firstText(item, "markdown", "content").orEmpty()
        if (markdown.isBlank()) throw EstudoPackageException("$label: informe markdown ou capítulos.")
        TheoryPlan(id, title, if (markdown.startsWith("# ")) markdown else "# $title\n\n$markdown")
    }

    private fun parseSummaries(item: JSONObject, path: String, ids: MutableSet<String>, requireIds: Boolean): List<SummaryPlan> = buildList {
        item.optJSONArray("resumos")?.objects()?.forEachIndexed { index, summary ->
            val label = "$path › resumo ${index + 1}"
            val id = if (requireIds) requireText(summary, "id", label) else summary.optString("id", "summary-$index")
            if (!ids.add(id)) throw EstudoPackageException("$label: id duplicado: $id.")
            val kind = if (firstText(summary, "tipo", "kind")?.uppercase() in setOf("RAPIDO", "QUICK")) SummaryKind.RAPIDO else SummaryKind.COMPLETO
            add(SummaryPlan(id, firstText(summary, "titulo", "title") ?: if (kind == SummaryKind.RAPIDO) "Revisão rápida" else "Resumo completo", requireAnyText(summary, label, "markdown", "content"), kind))
        }
        firstText(item, "summary", "resumo")?.let { text ->
            val id = "${item.optString("id", "topic")}-summary"
            if (ids.add(id)) add(SummaryPlan(id, "Resumo completo", text, SummaryKind.COMPLETO))
        }
        firstText(item, "quickReview", "revisaoRapida")?.let { text ->
            val id = "${item.optString("id", "topic")}-quick"
            if (ids.add(id)) add(SummaryPlan(id, "Revisão rápida", text, SummaryKind.RAPIDO))
        }
    }

    private fun parseSnippets(array: JSONArray?, path: String, topicId: String, kind: SnippetKind, ids: MutableSet<String>): List<SnippetPlan> {
        if (array == null) return emptyList()
        return (0 until array.length()).map { index ->
            val value = array.get(index)
            val obj = value as? JSONObject
            val id = obj?.optString("id")?.takeIf { it.isNotBlank() } ?: "$topicId-${kind.name.lowercase()}-$index"
            if (!ids.add(id)) throw EstudoPackageException("$path: id de item duplicado: $id.")
            val text = obj?.let { firstText(it, "text", "texto", "question", "pergunta") } ?: value.toString().trim()
            if (text.isNullOrBlank()) throw EstudoPackageException("$path: item ${index + 1} está vazio.")
            SnippetPlan(id, kind, text)
        }
    }

    private fun parseErrorConcepts(array: JSONArray?, path: String, topicId: String, ids: MutableSet<String>): List<ErrorConceptPlan> {
        if (array == null) return emptyList()
        return (0 until array.length()).map { index ->
            val value = array.get(index)
            val obj = value as? JSONObject
            val title = obj?.let { firstText(it, "title", "titulo") } ?: value.toString().trim()
            val id = obj?.optString("id")?.takeIf { it.isNotBlank() } ?: "$topicId-error-$index"
            if (!ids.add(id)) throw EstudoPackageException("$path: conceito de erro duplicado: $id.")
            ErrorConceptPlan(id, title, obj?.let { firstText(it, "summary", "resumo", "description", "descricao") }.orEmpty())
        }
    }

    private fun parseQuestions(array: JSONArray, path: String, defaults: QuestionDefaults, ids: MutableSet<String>, requireIds: Boolean) = array.objects().mapIndexed { index, item ->
        val label = "$path › questão ${index + 1}"
        val id = if (requireIds) requireText(item, "id", label) else item.optString("id", "question-$index")
        if (!ids.add(id)) throw EstudoPackageException("$label: id duplicado: $id.")
        val statement = firstText(item, "enunciado", "statement") ?: throw EstudoPackageException("$label: enunciado ausente.")
        val explanation = firstText(item, "explicacao", "explanation") ?: throw EstudoPackageException("$label: explicação ausente.")
        val optionsJson = item.optJSONArray("alternativas") ?: item.optJSONArray("options") ?: throw EstudoPackageException("$label: alternativas ausentes.")
        if (optionsJson.length() < 2) throw EstudoPackageException("$label: informe ao menos duas alternativas.")
        val options = optionsJson.objects().mapIndexed { i, option ->
            val key = firstText(option, "chave", "key")?.uppercase() ?: ('A' + i).toString()
            val optionText = firstText(option, "texto", "text") ?: throw EstudoPackageException("$label: alternativa $key vazia.")
            val correct = if (option.has("correta")) option.getBoolean("correta") else option.optBoolean("correct")
            OptionPlan(key, optionText, correct)
        }
        if (options.map { it.key }.distinct().size != options.size || options.count { it.correct } != 1) throw EstudoPackageException("$label: use chaves únicas e exatamente uma alternativa correta.")
        val declaredType = parseQuestionSourceType(firstText(item, "questionSourceType", "tipoQuestao"))
        val sourceId = firstText(item, "sourceId", "idOrigem")
        val sourceUrl = firstText(item, "sourceUrl", "url")
        // Diz-se de prova real, mas não aponta origem nenhuma: vira autoral e perde banca/órgão/ano.
        // Exibir procedência que ninguém consegue conferir é pior do que assumir que é autoral.
        val downgraded = declaredType != QuestionSourceType.AUTHORIAL && sourceId.isNullOrBlank() && sourceUrl.isNullOrBlank()
        val sourceType = if (downgraded) QuestionSourceType.AUTHORIAL else declaredType
        // Questão autoral não herda banca/órgão/ano do cabeçalho do pacote: herdar faria a questão
        // inventada aparecer no app com a cara de uma prova que existiu.
        val inheritsProvenance = sourceType != QuestionSourceType.AUTHORIAL
        QuestionPlan(
            id = id,
            board = if (downgraded) null else firstText(item, "banca", "board") ?: defaults.board.takeIf { inheritsProvenance },
            agency = if (downgraded) null else firstText(item, "orgao", "agency") ?: defaults.agency.takeIf { inheritsProvenance },
            year = if (downgraded) null else item.optInt("ano", item.optInt("year")).takeIf { it > 0 } ?: defaults.year?.takeIf { inheritsProvenance },
            difficulty = parseDifficulty(firstText(item, "dificuldade", "difficulty"), label) ?: defaults.difficulty,
            source = firstText(item, "origem", "source") ?: defaults.source,
            sourceType = sourceType,
            sourceId = sourceId,
            sourceUrl = sourceUrl,
            statement = statement,
            explanation = explanation,
            notes = firstText(item, "observacao", "notes").orEmpty(),
            tags = (defaults.tags + item.optJSONArray("tags")?.strings().orEmpty()).distinct(),
            options = options,
            reviewAnchor = firstText(item, "secao", "reviewAnchor", "ancora"),
            errorConceptId = firstText(item, "conceitoErro", "errorConceptId"),
            downgraded = downgraded,
        )
    }

    private fun requireText(json: JSONObject, key: String, path: String) = firstText(json, key) ?: throw EstudoPackageException("$path: campo obrigatório ausente ou vazio: $key.")
    private fun requireAnyText(json: JSONObject, path: String, vararg keys: String) = firstText(json, *keys) ?: throw EstudoPackageException("$path: conteúdo ausente.")
    /**
     * Fontes declaradas. Fonte sem título não entra: "consultei a legislação" não é conferível e
     * ocupar a tela com isso dá falsa sensação de rastreabilidade.
     */
    private fun parseSources(item: JSONObject, ownerId: String): List<SourcePlan> {
        val array = item.optJSONArray("fontes") ?: item.optJSONArray("sources") ?: return emptyList()
        return array.objects().mapIndexedNotNull { index, source ->
            val title = firstText(source, "titulo", "title") ?: return@mapIndexedNotNull null
            SourcePlan(
                id = firstText(source, "id") ?: "$ownerId-fonte-${index + 1}",
                kind = if (firstText(source, "tipo", "kind")?.uppercase()?.startsWith("OFICIAL") == true) SourceKind.OFICIAL else SourceKind.COMPLEMENTAR,
                title = title,
                publisher = firstText(source, "publicador", "publisher", "instituicao").orEmpty(),
                reference = firstText(source, "referencia", "reference", "trecho").orEmpty(),
                url = firstText(source, "url", "link"),
                accessedAt = firstText(source, "acessadoEm", "accessedAt", "data").orEmpty(),
            )
        }
    }

    private fun firstText(json: JSONObject, vararg keys: String): String? = keys.firstNotNullOfOrNull { key -> json.optString(key).trim().takeIf { it.isNotBlank() && it != "null" } }
    private fun parsePriority(json: JSONObject, path: String): Priority = Priority.entries.firstOrNull { it.name == json.optString("prioridade", "NORMAL").uppercase() }
        ?: throw EstudoPackageException("$path: prioridade deve ser BAIXA, NORMAL ou ALTA.")
    private fun parseSourcePages(json: JSONObject): List<Int> = json.optJSONArray("sourcePages")?.let { array -> (0 until array.length()).mapNotNull { array.optInt(it).takeIf { value -> value > 0 } } }.orEmpty()
    private fun copyMetadata(root: JSONObject): JSONObject? = root.optJSONObject("metadata")?.let { JSONObject(it.toString()) }
    private fun schemaVersion(root: JSONObject): Int = root.optInt("schemaVersion", root.optJSONObject("metadata")?.optInt("schemaVersion", 1) ?: 1)
    private fun packageVersion(root: JSONObject, version: Int): String = firstText(root, "packageVersion")
        ?: root.optJSONObject("metadata")?.let { firstText(it, "packageVersion") }
        ?: "estudo-v$version"
    private fun parseWarnings(array: JSONArray?): List<PackageWarning> = array?.objects()?.map { warning ->
        PackageWarning(
            code = firstText(warning, "code") ?: "UNKNOWN",
            severity = firstText(warning, "severity") ?: "WARNING",
            message = firstText(warning, "message") ?: "",
            sourcePages = parseSourcePages(warning),
            ambiguity = warning.optNullableString("ambiguity"),
        )
    }.orEmpty()
    private fun parsePriorityAssessment(item: JSONObject, path: String, ids: IdSets): PriorityAssessment? {
        val json = item.optJSONObject("priorityAssessment") ?: return null
        var normalized = false
        val scoreValue = json.opt("score")
        val rawScore = (scoreValue as? Number)?.toInt() ?: 50.also { if (scoreValue != null) normalized = true }
        val score = rawScore.coerceIn(0, 100).also { if (it != rawScore) normalized = true }
        val rawSource = json.optString("source", "DEFAULT").uppercase()
        val source = PrioritySource.entries.firstOrNull { it.name == rawSource } ?: PrioritySource.DEFAULT.also { normalized = true }
        val confidenceValue = json.opt("confidence")
        val rawConfidence = (confidenceValue as? Number)?.toDouble() ?: 0.0.also { if (confidenceValue != null) normalized = true }
        val confidence = if (rawConfidence.isFinite()) rawConfidence.toFloat().coerceIn(0f, 1f) else 0f.also { normalized = true }
        if (rawConfidence != confidence.toDouble()) normalized = true
        val evidenceArray = json.optJSONArray("evidence") ?: json.optJSONArray("evidencias")
        val evidence = evidenceArray?.let { PriorityEvidenceCodec.decode(it.toString()) }.orEmpty()
        if (evidenceArray != null && evidence.size != evidenceArray.length()) normalized = true
        if (normalized) ids.normalizedPriorityCount++
        return PriorityAssessment(
            score = score,
            source = source,
            confidence = confidence,
            rationale = firstText(json, "rationale", "justificativa"),
            evidence = evidence,
        )
    }
    private fun parseDifficulty(value: String?, path: String): Difficulty? = value?.let { v -> Difficulty.entries.firstOrNull { it.name == v.uppercase() } ?: throw EstudoPackageException("$path: dificuldade deve ser FACIL, MEDIA ou DIFICIL.") }
    private fun parseOriginType(value: String?): ContentOriginType = when (value?.uppercase()) {
        "DIDACTIC_SUBDIVISION", "SUBDIVISAO_DIDATICA" -> ContentOriginType.DIDACTIC_SUBDIVISION
        "AUXILIARY_CONTENT", "CONTEUDO_AUXILIAR", "COMPLEMENTAR" -> ContentOriginType.AUXILIARY_CONTENT
        else -> ContentOriginType.EDITAL
    }
    private fun parseQuestionSourceType(value: String?): QuestionSourceType = when (value?.uppercase()) {
        "REAL" -> QuestionSourceType.REAL
        "REAL_ADAPTED", "REAL_ADAPTADA", "ADAPTADA" -> QuestionSourceType.REAL_ADAPTED
        else -> QuestionSourceType.AUTHORIAL
    }
    private fun slug(value: String) = value.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-').take(60).ifBlank { "conteudo" }
    private data class QuestionDefaults(val board: String? = null, val agency: String? = null, val year: Int? = null, val difficulty: Difficulty? = null, val source: String? = null, val tags: List<String> = emptyList())
    private data class IdSets(
        val subjects: MutableSet<String> = mutableSetOf(),
        val subjectExternalIds: MutableSet<String> = mutableSetOf(),
        val topics: MutableSet<String> = mutableSetOf(),
        val topicExternalIds: MutableSet<String> = mutableSetOf(),
        val theories: MutableSet<String> = mutableSetOf(),
        val summaries: MutableSet<String> = mutableSetOf(),
        val snippets: MutableSet<String> = mutableSetOf(),
        val questions: MutableSet<String> = mutableSetOf(),
        val errorConcepts: MutableSet<String> = mutableSetOf(),
        var normalizedPriorityCount: Int = 0,
    )
}

/** Serializes the official .estudo v2 representation used by EstudoPackageParser/Service. */
internal object EstudoPackageCodec {
    fun encode(plan: PackagePlan): String {
        val root = JSONObject()
            .put("format", "estudario-estudo")
            .put("version", plan.version)
            .put("schemaVersion", plan.schemaVersion)
            .put("packageVersion", plan.packageVersion)
            .put("packageId", plan.packageId)
            .put(
                "concurso",
                JSONObject()
                    .put("id", plan.competitionId)
                    .put("nome", plan.competitionName)
                    .put("principal", plan.primary),
            )
            .put("materias", JSONArray().also { subjects -> plan.subjects.sortedBy { it.position }.forEach { subjects.put(subjectJson(it)) } })
            .put("warnings", JSONArray().also { warnings -> plan.warnings.forEach { warnings.put(warningJson(it)) } })
        plan.metadata?.let { root.put("metadata", JSONObject(it.toString())) }
        return root.toString(2)
    }

    private fun subjectJson(subject: SubjectPlan): JSONObject = JSONObject()
        .put("id", subject.id)
        .put("externalId", subject.externalId)
        .put("nome", subject.name)
        .put("ordem", subject.position)
        .put("prioridade", subject.priority.name)
        .put("sourcePages", JSONArray(subject.sourcePages))
        .put("topicos", JSONArray().also { topics -> subject.topics.sortedBy { it.position }.forEach { topics.put(topicJson(it)) } })

    private fun topicJson(topic: TopicPlan): JSONObject = JSONObject()
        .put("id", topic.id)
        .put("externalId", topic.externalId)
        .put("titulo", topic.title)
        .put("descricao", topic.description)
        .put("observacoes", topic.notes)
        .put("ordem", topic.position)
        .put("prioridade", topic.priority.name)
        .put("parentExternalId", topic.parentExternalId ?: JSONObject.NULL)
        .put("sourcePages", JSONArray(topic.sourcePages))
        .put("subtopicos", JSONArray().also { children -> topic.children.sortedBy { it.position }.forEach { children.put(topicJson(it)) } })

    private fun warningJson(warning: PackageWarning): JSONObject = JSONObject()
        .put("code", warning.code)
        .put("severity", warning.severity)
        .put("message", warning.message)
        .put("sourcePages", JSONArray(warning.sourcePages))
        .put("ambiguity", warning.ambiguity ?: JSONObject.NULL)
}

class EstudoPackageService(private val db: AppDatabase) {
    private val dao = db.dao()

    suspend fun preview(text: String): EstudoPreview {
        val plan = EstudoPackageParser.parse(text)
        val previews = plan.subjects.map { subject ->
            val roots = subject.topics
            val all = roots.flatMap { it.flatten() }
            SubjectImportPreview(subject.name, roots.size, all.size - roots.size, all.sumOf { it.theories.size }, all.sumOf { it.summaries.size }, all.sumOf { it.questions.size }, all.sumOf { it.snippets.size }, all.sumOf { it.errorConcepts.size }, subject.topics.flatMap { it.previewRows() })
        }
        val all = plan.subjects.flatMap { subject -> subject.topics.flatMap { it.flatten() } }
        var duplicates = 0
        all.forEach { topic ->
            topic.theories.forEach { if (dao.theoryByExternalId(it.id) != null || dao.theoryByExternalId("${plan.packageId}:${it.id}") != null) duplicates++ }
            topic.summaries.forEach { if (dao.summaryByExternalId(it.id) != null || dao.summaryByExternalId("${plan.packageId}:${it.id}") != null) duplicates++ }
            topic.snippets.forEach { if (dao.snippetByExternalId(it.id) != null || dao.snippetByExternalId("${plan.packageId}:${it.id}") != null) duplicates++ }
            topic.questions.forEach { if (dao.questionByExternalId(it.id) != null || dao.questionByExternalId("${plan.packageId}:${it.id}") != null || it.sourceId?.let { id -> dao.questionBySourceId(id) } != null || dao.questionByNormalizedHash(normalizedQuestionHash(it.statement)) != null) duplicates++ }
            topic.errorConcepts.forEach { if (dao.errorConceptByExternalId(it.id) != null || dao.errorConceptByExternalId("${plan.packageId}:${it.id}") != null) duplicates++ }
        }
        return EstudoPreview(
            plan.version, plan.packageId, plan.competitionName, previews,
            previews.sumOf { it.topicCount }, previews.sumOf { it.subtopicCount }, previews.sumOf { it.theoryCount },
            previews.sumOf { it.summaryCount }, previews.sumOf { it.questionCount }, duplicates,
            previews.sumOf { it.snippetCount }, previews.sumOf { it.errorConceptCount }, dao.importPackage(plan.packageId) != null,
            plan.allTopics().sumOf { topico -> topico.questions.count { it.downgraded } }, plan.sourceCount(),
        )
    }

    private suspend fun validateExternalIdConflicts(plan: PackagePlan, targetCompetitionId: Long?) {
        val subjects = dao.subjectsOnce()
        val subjectsByExternalId = subjects.filter { !it.externalId.isNullOrBlank() }.associateBy { it.externalId!! }
        plan.subjects.sortedBy { it.position }.forEach { subjectPlan ->
            val owner = subjectsByExternalId[subjectPlan.externalId]
            if (owner != null && owner.competitionId != targetCompetitionId) {
                throw EstudoPackageException("Conflito de externalId de matéria '${subjectPlan.externalId}': já pertence ao edital ${owner.competitionId}.")
            }
        }

        val subjectsById = subjects.associateBy { it.id }
        val topicsByExternalId = dao.topicsOnce().filter { !it.externalId.isNullOrBlank() }.associateBy { it.externalId!! }
        plan.allTopics().forEach { topicPlan ->
            val owner = topicsByExternalId[topicPlan.externalId]
            val ownerCompetitionId = owner?.let { subjectsById[it.subjectId]?.competitionId }
            if (owner != null && ownerCompetitionId != null && ownerCompetitionId != targetCompetitionId) {
                throw EstudoPackageException("Conflito de externalId de tópico '${topicPlan.externalId}': já pertence ao edital $ownerCompetitionId.")
            }
        }
    }

    suspend fun import(
        text: String,
        mode: ImportMode = ImportMode.SKIP,
        targetCompetitionId: Long? = null,
    ): ImportResult = try {
        db.withTransaction {
        val plan = EstudoPackageParser.parse(text)
        val contentHash = MessageDigest.getInstance("SHA-256").digest(text.toByteArray()).joinToString("") { "%02x".format(it) }
        val prefix = if (mode == ImportMode.COPY) "${plan.packageId}:copy:${contentHash.take(8)}" else plan.packageId
        val competitions = dao.competitionsOnce()
        val selectedCompetition = targetCompetitionId
            ?.takeIf { mode != ImportMode.COPY }
            ?.let { id -> competitions.firstOrNull { it.id == id } ?: throw EstudoPackageException("O edital selecionado não foi encontrado.") }
        val currentCompetition = selectedCompetition
            ?: dao.competitionByExternalId(plan.competitionId)
            ?: competitions.firstOrNull { it.name.equals(plan.competitionName, true) }
        validateExternalIdConflicts(plan, currentCompetition?.id)
        val competitionId = currentCompetition?.id ?: dao.insertCompetition(
            CompetitionEntity(
                name = plan.competitionName,
                isPrimary = plan.primary || competitions.isEmpty(),
                externalId = plan.competitionId,
                assessedPriorityScore = plan.priorityAssessment?.score ?: 50,
                assessedPrioritySource = plan.priorityAssessment?.source ?: PrioritySource.DEFAULT,
                assessedPriorityConfidence = plan.priorityAssessment?.confidence ?: 0f,
                assessedPriorityRationale = plan.priorityAssessment?.rationale,
                assessedPriorityEvidenceJson = plan.priorityAssessment?.evidenceJson() ?: "[]",
                hasAssessedPriority = plan.priorityAssessment != null,
            ),
        )
        currentCompetition?.let { current ->
            val assessment = plan.priorityAssessment
            dao.updateCompetition(
                current.copy(
                    externalId = current.externalId ?: plan.competitionId,
                    assessedPriorityScore = assessment?.score ?: current.assessedPriorityScore,
                    assessedPrioritySource = assessment?.source ?: current.assessedPrioritySource,
                    assessedPriorityConfidence = assessment?.confidence ?: current.assessedPriorityConfidence,
                    assessedPriorityRationale = assessment?.rationale ?: current.assessedPriorityRationale,
                    assessedPriorityEvidenceJson = assessment?.evidenceJson() ?: current.assessedPriorityEvidenceJson,
                    hasAssessedPriority = if (assessment != null) true else current.hasAssessedPriority,
                ),
            )
        }
        if (plan.primary) dao.setPrimaryCompetition(competitionId)
        var subjectsCreated = 0; var topicsCreated = 0; var topicsUpdated = 0
        var theories = 0; var summaries = 0; var snippets = 0; var questions = 0; var concepts = 0; var skipped = 0; var updated = 0
        val importedTopicIds = linkedSetOf<Long>()

        plan.subjects.sortedBy { it.position }.forEach { subjectPlan ->
            val currentSubject = dao.subjectByExternalId(subjectPlan.externalId)?.takeIf { it.competitionId == competitionId }
                ?: dao.subjectsFor(competitionId).firstOrNull { it.name.equals(subjectPlan.name, true) }
            val subjectId = currentSubject?.id ?: dao.insertSubject(
                SubjectEntity(
                    competitionId = competitionId,
                    name = subjectPlan.name,
                    position = subjectPlan.position,
                    externalId = subjectPlan.externalId,
                    assessedPriorityScore = subjectPlan.priorityAssessment?.score ?: 50,
                    assessedPrioritySource = subjectPlan.priorityAssessment?.source ?: PrioritySource.DEFAULT,
                    assessedPriorityConfidence = subjectPlan.priorityAssessment?.confidence ?: 0f,
                    assessedPriorityRationale = subjectPlan.priorityAssessment?.rationale,
                    assessedPriorityEvidenceJson = subjectPlan.priorityAssessment?.evidenceJson() ?: "[]",
                    hasAssessedPriority = subjectPlan.priorityAssessment != null,
                ),
            ).also { subjectsCreated++ }
            currentSubject?.let { current ->
                val assessment = subjectPlan.priorityAssessment
                dao.updateSubject(
                    current.copy(
                        name = subjectPlan.name,
                        position = subjectPlan.position,
                        externalId = subjectPlan.externalId,
                        assessedPriorityScore = assessment?.score ?: current.assessedPriorityScore,
                        assessedPrioritySource = assessment?.source ?: current.assessedPrioritySource,
                        assessedPriorityConfidence = assessment?.confidence ?: current.assessedPriorityConfidence,
                        assessedPriorityRationale = assessment?.rationale ?: current.assessedPriorityRationale,
                        assessedPriorityEvidenceJson = assessment?.evidenceJson() ?: current.assessedPriorityEvidenceJson,
                        hasAssessedPriority = if (assessment != null) true else current.hasAssessedPriority,
                    ),
                )
            }
            val knownTopics = dao.topicsFor(subjectId).toMutableList()

            suspend fun importTopic(p: TopicPlan, parentId: Long?) {
                val externalId = p.externalId
                val oldTopic = dao.topicByExternalId(externalId)?.takeIf { it.subjectId == subjectId }
                    ?: knownTopics.firstOrNull { it.parentTopicId == parentId && it.title.equals(p.title, true) }
                val legacyAssessment = p.priorityAssessment ?: p.priority.takeIf { p.legacyPriorityProvided }?.asAssessment()
                val topicId = oldTopic?.id ?: dao.insertTopic(TopicEntity(subjectId = subjectId, parentTopicId = parentId, title = p.title, description = p.description, position = p.position, notes = p.notes, priority = p.priority, externalId = externalId, contentOriginType = p.originType, scopeCovers = p.scopeCovers, scopeExcludes = p.scopeExcludes, assessedPriorityScore = legacyAssessment?.score ?: 50, assessedPrioritySource = legacyAssessment?.source ?: PrioritySource.DEFAULT, assessedPriorityConfidence = legacyAssessment?.confidence ?: 0f, assessedPriorityRationale = legacyAssessment?.rationale, assessedPriorityEvidenceJson = legacyAssessment?.evidenceJson() ?: "[]", hasAssessedPriority = legacyAssessment != null)).also { id ->
                    topicsCreated++
                    knownTopics += TopicEntity(id = id, subjectId = subjectId, parentTopicId = parentId, title = p.title, description = p.description, position = p.position, notes = p.notes, priority = p.priority, externalId = externalId, contentOriginType = p.originType, assessedPriorityScore = legacyAssessment?.score ?: 50, assessedPrioritySource = legacyAssessment?.source ?: PrioritySource.DEFAULT, assessedPriorityConfidence = legacyAssessment?.confidence ?: 0f, assessedPriorityRationale = legacyAssessment?.rationale, assessedPriorityEvidenceJson = legacyAssessment?.evidenceJson() ?: "[]", hasAssessedPriority = legacyAssessment != null)
                }
                oldTopic?.let {
                    dao.updateTopic(
                        it.copy(
                            parentTopicId = parentId,
                            title = p.title,
                            description = p.description.ifBlank { it.description },
                            position = p.position,
                            notes = p.notes.ifBlank { it.notes },
                            priority = if (p.legacyPriorityProvided) p.priority else it.priority,
                            externalId = externalId,
                            contentOriginType = p.originType,
                            scopeCovers = p.scopeCovers ?: it.scopeCovers,
                            scopeExcludes = p.scopeExcludes ?: it.scopeExcludes,
                            assessedPriorityScore = p.priorityAssessment?.score ?: it.assessedPriorityScore,
                            assessedPrioritySource = p.priorityAssessment?.source ?: it.assessedPrioritySource,
                            assessedPriorityConfidence = p.priorityAssessment?.confidence ?: it.assessedPriorityConfidence,
                            assessedPriorityRationale = p.priorityAssessment?.rationale ?: it.assessedPriorityRationale,
                            assessedPriorityEvidenceJson = p.priorityAssessment?.evidenceJson() ?: it.assessedPriorityEvidenceJson,
                            hasAssessedPriority = if (p.priorityAssessment != null) true else it.hasAssessedPriority,
                        ),
                    )
                    topicsUpdated++
                }
                if (p.theories.isNotEmpty() || p.summaries.isNotEmpty() || p.snippets.isNotEmpty() || p.questions.isNotEmpty() || p.errorConcepts.isNotEmpty()) importedTopicIds += topicId
                if (p.sources.isNotEmpty()) {
                    dao.insertSources(
                        p.sources.map { fonte ->
                            ContentSourceEntity(
                                topicId = topicId,
                                packageId = plan.packageId,
                                kind = fonte.kind,
                                title = fonte.title,
                                publisher = fonte.publisher,
                                reference = fonte.reference,
                                url = fonte.url,
                                accessedAt = fonte.accessedAt,
                                externalId = if (mode == ImportMode.COPY) "$prefix:${fonte.id}" else fonte.id,
                            )
                        },
                    )
                }
                p.theories.forEach { item ->
                    val externalId = if (mode == ImportMode.COPY) "$prefix:${item.id}" else item.id
                    val old = dao.theoryByExternalId(externalId) ?: dao.theoryByExternalId("${plan.packageId}:${item.id}")
                    if (old == null) { dao.insertTheory(TheoryDocumentEntity(topicId = topicId, title = item.title, markdown = item.markdown, externalId = externalId)); theories++ }
                    else if (mode == ImportMode.UPDATE) { dao.updateTheory(old.copy(topicId = topicId, title = item.title, markdown = item.markdown, updatedAt = System.currentTimeMillis())); updated++ }
                    else skipped++
                }
                p.summaries.forEach { item ->
                    val externalId = if (mode == ImportMode.COPY) "$prefix:${item.id}" else item.id
                    val old = dao.summaryByExternalId(externalId) ?: dao.summaryByExternalId("${plan.packageId}:${item.id}")
                    if (old == null) { dao.insertSummary(SummaryEntity(topicId = topicId, title = item.title, markdown = item.markdown, externalId = externalId, kind = item.kind)); summaries++ }
                    else if (mode == ImportMode.UPDATE) { dao.updateSummary(old.copy(topicId = topicId, title = item.title, markdown = item.markdown, kind = item.kind, updatedAt = System.currentTimeMillis())); updated++ }
                    else skipped++
                }
                p.snippets.forEachIndexed { index, item ->
                    val externalId = if (mode == ImportMode.COPY) "$prefix:${item.id}" else item.id
                    val old = dao.snippetByExternalId(externalId) ?: dao.snippetByExternalId("${plan.packageId}:${item.id}")
                    if (old == null) { dao.insertSnippet(TopicSnippetEntity(topicId = topicId, kind = item.kind, text = item.text, externalId = externalId, position = index)); snippets++ }
                    else if (mode == ImportMode.UPDATE) { dao.updateSnippet(old.copy(topicId = topicId, kind = item.kind, text = item.text, position = index, updatedAt = System.currentTimeMillis())); updated++ }
                    else skipped++
                }
                p.questions.forEach { item ->
                    val externalId = if (mode == ImportMode.COPY) "$prefix:${item.id}" else item.id
                    val old = dao.questionByExternalId(externalId)
                        ?: dao.questionByExternalId("${plan.packageId}:${item.id}")
                        ?: item.sourceId?.let { dao.questionBySourceId(it) }
                        ?: dao.questionByNormalizedHash(normalizedQuestionHash(item.statement))
                    if (old == null) {
                        val id = dao.insertQuestion(item.entity(topicId, externalId))
                        dao.insertOptions(item.options.entities(id)); questions++
                    } else if (mode == ImportMode.UPDATE) {
                        dao.updateQuestion(item.entity(topicId, externalId).copy(id = old.id, importedAt = old.importedAt, answerCount = old.answerCount, correctCount = old.correctCount, errorCount = old.errorCount, lastAnswer = old.lastAnswer, lastAnsweredAt = old.lastAnsweredAt, isFavorite = old.isFavorite))
                        dao.deleteOptionsFor(old.id)
                        dao.insertOptions(item.options.entities(old.id)); updated++
                    } else skipped++
                }
                p.errorConcepts.forEach { item ->
                    val externalId = if (mode == ImportMode.COPY) "$prefix:${item.id}" else item.id
                    val old = dao.errorConceptByExternalId(externalId) ?: dao.errorConceptByExternalId("${plan.packageId}:${item.id}")
                    if (old == null) { dao.insertErrorConcept(ErrorConceptEntity(topicId = topicId, title = item.title, summary = item.summary, externalId = externalId)); concepts++ }
                    else if (mode == ImportMode.UPDATE) { dao.updateErrorConcept(old.copy(topicId = topicId, title = item.title, summary = item.summary, updatedAt = System.currentTimeMillis())); updated++ }
                    else skipped++
                }
                p.children.sortedBy { it.position }.forEach { importTopic(it, topicId) }
            }
            subjectPlan.topics.sortedBy { it.position }.forEach { importTopic(it, null) }
        }
        if (plan.sources.isNotEmpty()) {
            dao.insertSources(
                plan.sources.map { fonte ->
                    ContentSourceEntity(
                        topicId = null,
                        packageId = plan.packageId,
                        kind = fonte.kind,
                        title = fonte.title,
                        publisher = fonte.publisher,
                        reference = fonte.reference,
                        url = fonte.url,
                        accessedAt = fonte.accessedAt,
                        externalId = if (mode == ImportMode.COPY) "$prefix:${fonte.id}" else fonte.id,
                    )
                },
            )
        }
        dao.insertImportPackage(ImportPackageEntity(packageId = prefix, schemaVersion = plan.version, contentHash = contentHash, createdCount = theories + summaries + snippets + questions + concepts, updatedCount = updated, ignoredCount = skipped))
        ImportResult(subjectsCreated, topicsCreated, topicsUpdated, theories, summaries, questions, skipped, snippets, concepts, updated, importedTopicIds.toList(), plan.allTopics().sumOf { topico -> topico.questions.count { it.downgraded } }, plan.sourceCount(), plan.normalizedPriorityCount)
        }
    } catch (_: SQLiteConstraintException) {
        throw EstudoPackageException("Conflito de externalId global no pacote oficial.")
    }
}

private fun QuestionPlan.entity(topicId: Long, externalId: String) = QuestionEntity(topicId = topicId, externalId = externalId, board = board, agency = agency, year = year, difficulty = difficulty, source = source, statement = statement, explanation = explanation, notes = notes, tagsText = tags.joinToString(", "), questionSourceType = sourceType, sourceId = sourceId, sourceUrl = sourceUrl, normalizedHash = normalizedQuestionHash(statement), reviewAnchor = reviewAnchor, errorConceptExternalId = errorConceptId)
private fun Priority.asAssessment() = PriorityAssessment(
    score = when (this) {
        Priority.ALTA -> 70
        Priority.NORMAL -> 50
        Priority.BAIXA -> 30
    },
    source = PrioritySource.DEFAULT,
    confidence = 0f,
    rationale = "Prioridade legada do arquivo .estudo.",
    evidence = emptyList(),
)
private fun PriorityAssessment.evidenceJson(): String = PriorityEvidenceCodec.encode(evidence)
private fun normalizedQuestionHash(statement: String): String {
    val normalized = java.text.Normalizer.normalize(statement.lowercase(), java.text.Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "").replace(Regex("[^a-z0-9]+"), " ").trim()
    return MessageDigest.getInstance("SHA-256").digest(normalized.toByteArray()).joinToString("") { "%02x".format(it) }
}
private fun List<OptionPlan>.entities(questionId: Long) = mapIndexed { index, option -> QuestionOptionEntity(questionId = questionId, key = option.key, text = option.text, isCorrect = option.correct, position = index) }
private fun TopicPlan.flatten(): List<TopicPlan> = listOf(this) + children.flatMap { it.flatten() }
private fun TopicPlan.previewRows(depth: Int = 0): List<TopicImportPreview> = listOf(TopicImportPreview(title, depth, theories.size, summaries.size, questions.size, snippets.size)) + children.flatMap { it.previewRows(depth + 1) }
internal fun JSONArray.objects(): List<JSONObject> = (0 until length()).map { getJSONObject(it) }
internal fun JSONArray.strings(): List<String> = (0 until length()).map { getString(it) }
internal fun JSONObject.optNullableString(key: String): String? = optString(key).takeIf { it.isNotBlank() && it != "null" }
