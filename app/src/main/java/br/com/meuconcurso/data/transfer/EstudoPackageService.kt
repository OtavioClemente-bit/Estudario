package br.com.meuconcurso.data.transfer

import androidx.room.withTransaction
import br.com.meuconcurso.data.local.*
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
)

class EstudoPackageException(message: String) : IllegalArgumentException(message)

internal data class OptionPlan(val key: String, val text: String, val correct: Boolean)
internal data class SummaryPlan(val id: String, val title: String, val markdown: String, val kind: SummaryKind = SummaryKind.COMPLETO)
internal data class TheoryPlan(val id: String, val title: String, val markdown: String)
internal data class SnippetPlan(val id: String, val kind: SnippetKind, val text: String)
internal data class ErrorConceptPlan(val id: String, val title: String, val summary: String)
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
)
internal data class TopicPlan(
    val id: String,
    val title: String,
    val description: String,
    val notes: String,
    val position: Int,
    val priority: Priority,
    val originType: ContentOriginType,
    val theories: List<TheoryPlan>,
    val summaries: List<SummaryPlan>,
    val snippets: List<SnippetPlan>,
    val questions: List<QuestionPlan>,
    val errorConcepts: List<ErrorConceptPlan>,
    val children: List<TopicPlan>,
)
internal data class SubjectPlan(val id: String, val name: String, val position: Int, val topics: List<TopicPlan>)
internal data class PackagePlan(val version: Int, val packageId: String, val competitionId: String, val competitionName: String, val primary: Boolean, val subjects: List<SubjectPlan>)

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
        return PackagePlan(version, packageId, firstText(root, "competitionId", "concursoId") ?: "competition-${slug(competition)}", competition, root.optBoolean("primary", false), listOf(SubjectPlan("subject-${slug(subject)}", subject, 0, listOf(plan))))
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
            val name = requireText(item, "nome", path)
            val topics = item.optJSONArray("topicos") ?: throw EstudoPackageException("$path: topicos deve ser uma lista.")
            SubjectPlan(id, name, item.optInt("ordem", index), topics.objects().mapIndexed { i, topic ->
                parseTopic(topic, "$name › tópico ${i + 1}", i, defaults, ids, true)
            })
        }
        val competitionName = requireText(competition, "nome", "concurso")
        return PackagePlan(2, packageId, firstText(competition, "id") ?: "competition-${slug(competitionName)}", competitionName, competition.optBoolean("principal"), subjects)
    }

    private fun parseTopic(item: JSONObject, path: String, defaultPosition: Int, defaults: QuestionDefaults, ids: IdSets, requireIds: Boolean): TopicPlan {
        val id = if (requireIds) requireText(item, "id", path) else item.optString("id", "topic-${slug(path)}")
        if (!ids.topics.add(id)) throw EstudoPackageException("$path: id de tópico duplicado: $id.")
        val title = firstText(item, "titulo", "title", "topic", "topico") ?: throw EstudoPackageException("$path: título ausente.")
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
            originType = parseOriginType(firstText(item, "contentOriginType", "tipoOrigem")),
            theories = parseTheories(item.optJSONArray("teorias") ?: JSONArray(), path, ids.theories, requireIds),
            summaries = parseSummaries(item, path, ids.summaries, requireIds),
            snippets = snippets,
            questions = parseQuestions(item.optJSONArray("questoes") ?: JSONArray(), path, defaults.copy(tags = (defaults.tags + item.optJSONArray("tags")?.strings().orEmpty()).distinct()), ids.questions, requireIds),
            errorConcepts = parseErrorConcepts(item.optJSONArray("errorConcepts") ?: item.optJSONArray("conceitosDeErro"), path, id, ids.errorConcepts),
            children = children,
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
        QuestionPlan(
            id = id,
            board = firstText(item, "banca", "board") ?: defaults.board,
            agency = firstText(item, "orgao", "agency") ?: defaults.agency,
            year = item.optInt("ano", item.optInt("year")).takeIf { it > 0 } ?: defaults.year,
            difficulty = parseDifficulty(firstText(item, "dificuldade", "difficulty"), label) ?: defaults.difficulty,
            source = firstText(item, "origem", "source") ?: defaults.source,
            sourceType = parseQuestionSourceType(firstText(item, "questionSourceType", "tipoQuestao")),
            sourceId = firstText(item, "sourceId", "idOrigem"),
            sourceUrl = firstText(item, "sourceUrl", "url"),
            statement = statement,
            explanation = explanation,
            notes = firstText(item, "observacao", "notes").orEmpty(),
            tags = (defaults.tags + item.optJSONArray("tags")?.strings().orEmpty()).distinct(),
            options = options,
        )
    }

    private fun requireText(json: JSONObject, key: String, path: String) = firstText(json, key) ?: throw EstudoPackageException("$path: campo obrigatório ausente ou vazio: $key.")
    private fun requireAnyText(json: JSONObject, path: String, vararg keys: String) = firstText(json, *keys) ?: throw EstudoPackageException("$path: conteúdo ausente.")
    private fun firstText(json: JSONObject, vararg keys: String): String? = keys.firstNotNullOfOrNull { key -> json.optString(key).trim().takeIf { it.isNotBlank() && it != "null" } }
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
        val topics: MutableSet<String> = mutableSetOf(),
        val theories: MutableSet<String> = mutableSetOf(),
        val summaries: MutableSet<String> = mutableSetOf(),
        val snippets: MutableSet<String> = mutableSetOf(),
        val questions: MutableSet<String> = mutableSetOf(),
        val errorConcepts: MutableSet<String> = mutableSetOf(),
    )
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
        )
    }

    suspend fun import(text: String, mode: ImportMode = ImportMode.SKIP): ImportResult = db.withTransaction {
        val plan = EstudoPackageParser.parse(text)
        val contentHash = MessageDigest.getInstance("SHA-256").digest(text.toByteArray()).joinToString("") { "%02x".format(it) }
        val prefix = if (mode == ImportMode.COPY) "${plan.packageId}:copy:${contentHash.take(8)}" else plan.packageId
        val competitions = dao.competitionsOnce()
        val currentCompetition = dao.competitionByExternalId(plan.competitionId) ?: competitions.firstOrNull { it.name.equals(plan.competitionName, true) }
        val competitionId = currentCompetition?.id ?: dao.insertCompetition(CompetitionEntity(name = plan.competitionName, isPrimary = plan.primary || competitions.isEmpty(), externalId = plan.competitionId))
        if (currentCompetition != null && currentCompetition.externalId == null) dao.updateCompetition(currentCompetition.copy(externalId = plan.competitionId))
        if (plan.primary) dao.setPrimaryCompetition(competitionId)
        var subjectsCreated = 0; var topicsCreated = 0; var topicsUpdated = 0
        var theories = 0; var summaries = 0; var snippets = 0; var questions = 0; var concepts = 0; var skipped = 0; var updated = 0
        val importedTopicIds = linkedSetOf<Long>()

        plan.subjects.sortedBy { it.position }.forEach { subjectPlan ->
            val currentSubject = dao.subjectByExternalId(subjectPlan.id)?.takeIf { it.competitionId == competitionId }
                ?: dao.subjectsFor(competitionId).firstOrNull { it.name.equals(subjectPlan.name, true) }
            val subjectId = currentSubject?.id ?: dao.insertSubject(SubjectEntity(competitionId = competitionId, name = subjectPlan.name, position = subjectPlan.position, externalId = subjectPlan.id)).also { subjectsCreated++ }
            currentSubject?.let { dao.updateSubject(it.copy(name = subjectPlan.name, position = subjectPlan.position, externalId = it.externalId ?: subjectPlan.id)) }
            val knownTopics = dao.topicsFor(subjectId).toMutableList()

            suspend fun importTopic(p: TopicPlan, parentId: Long?) {
                val oldTopic = dao.topicByExternalId(p.id)?.takeIf { it.subjectId == subjectId }
                    ?: knownTopics.firstOrNull { it.parentTopicId == parentId && it.title.equals(p.title, true) }
                val topicId = oldTopic?.id ?: dao.insertTopic(TopicEntity(subjectId = subjectId, parentTopicId = parentId, title = p.title, description = p.description, position = p.position, notes = p.notes, priority = p.priority, externalId = p.id, contentOriginType = p.originType)).also { id ->
                    topicsCreated++
                    knownTopics += TopicEntity(id, subjectId, parentId, p.title, p.description, p.position, notes = p.notes, priority = p.priority, externalId = p.id, contentOriginType = p.originType)
                }
                oldTopic?.let {
                    dao.updateTopic(it.copy(parentTopicId = parentId, title = p.title, description = p.description.ifBlank { it.description }, position = p.position, notes = p.notes.ifBlank { it.notes }, priority = p.priority, externalId = it.externalId ?: p.id, contentOriginType = p.originType))
                    topicsUpdated++
                }
                if (p.theories.isNotEmpty() || p.summaries.isNotEmpty() || p.snippets.isNotEmpty() || p.questions.isNotEmpty() || p.errorConcepts.isNotEmpty()) importedTopicIds += topicId
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
        dao.insertImportPackage(ImportPackageEntity(packageId = prefix, schemaVersion = plan.version, contentHash = contentHash, createdCount = theories + summaries + snippets + questions + concepts, updatedCount = updated, ignoredCount = skipped))
        ImportResult(subjectsCreated, topicsCreated, topicsUpdated, theories, summaries, questions, skipped, snippets, concepts, updated, importedTopicIds.toList())
    }
}

private fun QuestionPlan.entity(topicId: Long, externalId: String) = QuestionEntity(topicId = topicId, externalId = externalId, board = board, agency = agency, year = year, difficulty = difficulty, source = source, statement = statement, explanation = explanation, notes = notes, tagsText = tags.joinToString(", "), questionSourceType = sourceType, sourceId = sourceId, sourceUrl = sourceUrl, normalizedHash = normalizedQuestionHash(statement))
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
internal fun JSONObject.optNullableString(key: String): String? = optString(key).takeIf { it.isNotBlank() }
