package br.com.estudario.web.data

import br.com.estudario.domain.ExamWeights
import br.com.estudario.domain.PriorityAssessment
import br.com.estudario.text.stripAccents
import br.com.estudario.web.WebConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.js.Date

/** Edital pronto do catálogo (tabela exam_catalog), igual ao ExamCatalogEntry do app. */
@Serializable
data class CatalogEntry(
    val id: String,
    @SerialName("short_name") val shortName: String,
    val agency: String,
    val role: String,
    val board: String? = null,
    val year: Int? = null,
    @SerialName("search_norm") val searchNorm: String = "",
    @SerialName("subject_count") val subjectCount: Int = 0,
    @SerialName("topic_count") val topicCount: Int = 0,
    @SerialName("ready_topic_count") val readyTopicCount: Int = 0,
) {
    val title: String get() = "$shortName · $role"
    val details: String get() = listOfNotNull(agency.takeIf { !it.equals(shortName, true) }, board, year?.toString()).joinToString(" · ")

    /** Parte do edital que já tem material pronto, de 0 a 100. */
    val readyPercent: Int get() = if (topicCount == 0) 0 else (readyTopicCount * 100 / topicCount).coerceIn(0, 100)

    val category: br.com.estudario.domain.catalog.ExamCategory get() = br.com.estudario.domain.catalog.ExamCategory.of(shortName, agency, role)
}

@Serializable
/** [questions]: questões da matéria na prova real (quadro do edital); null quando o edital não informa. */
data class CatalogSubject(
    val name: String,
    val topics: List<String>,
    val questions: Int? = null,
    /** Peso de cada questão da matéria na nota (1 quando o edital não diferencia). */
    val weight: Double? = null,
    /** Quando o edital só dá o total do bloco: a frase que explica a divisão. */
    val blockNote: String? = null,
) {
    val share: ExamWeights.Share? get() = questions?.let { ExamWeights.Share(it, weight ?: 1.0, blockNote) }
}

@Serializable
private data class CatalogSubjectsRow(val subjects: List<CatalogSubject>)

object Catalog {
    private var cache: List<CatalogEntry>? = null

    private val headers get() = mapOf("apikey" to WebConfig.SUPABASE_KEY, "Authorization" to "Bearer ${WebConfig.SUPABASE_KEY}", "Accept" to "application/json")

    suspend fun entries(): List<CatalogEntry> {
        cache?.let { return it }
        val fields = "id,short_name,agency,role,board,year,search_norm,subject_count,topic_count,ready_topic_count"
        // O servidor devolve no máximo 1.000 linhas por consulta: busca em páginas até acabar.
        val list = mutableListOf<CatalogEntry>()
        while (true) {
            val response = httpRequest("GET", "${WebConfig.SUPABASE_URL}/rest/v1/exam_catalog?select=$fields&status=eq.PUBLISHED&order=short_name.asc,role.asc,id.asc&limit=1000&offset=${list.size}", headers)
            if (!response.ok) throw IllegalStateException("Não consegui carregar a lista de editais agora. Tente de novo.")
            val page = snapshotJson.decodeFromString(ListSerializer(CatalogEntry.serializer()), response.body)
            list += page
            if (page.size < 1000) break
        }
        return list.also { cache = it }
    }

    suspend fun subjects(id: String): List<CatalogSubject> {
        val response = httpRequest("GET", "${WebConfig.SUPABASE_URL}/rest/v1/exam_catalog?select=subjects&id=eq.${js("encodeURIComponent")(id)}&status=eq.PUBLISHED&limit=1", headers)
        if (!response.ok) throw IllegalStateException("Não consegui baixar as matérias deste edital. Tente de novo.")
        val rows = snapshotJson.decodeFromString(ListSerializer(CatalogSubjectsRow.serializer()), response.body)
        return rows.firstOrNull()?.subjects.orEmpty()
            .map { subject -> subject.copy(name = subject.name.trim(), topics = subject.topics.map(String::trim).filter(String::isNotBlank)) }
            .filter { it.name.isNotBlank() && it.topics.isNotEmpty() }
            .ifEmpty { throw IllegalStateException("Este edital não está mais disponível no catálogo.") }
    }

    /**
     * Para cada concurso cujas matérias ainda não têm avaliação, acha o edital do catálogo de mesmo
     * nome e mesmas matérias e devolve a função que grava os pesos (null se não houver o que fazer).
     */
    suspend fun backfillWeights(data: Snapshot): ((Snapshot) -> Snapshot)? {
        val pending = data.competitions.filter { competition ->
            val own = data.subjects.filter { it.competitionId == competition.id }
            own.isNotEmpty() && own.none { it.hasAssessedPriority }
        }
        if (pending.isEmpty()) return null
        val all = entries()
        val updates = mutableListOf<Pair<Long, List<CatalogSubject>>>()
        for (competition in pending) {
            val names = data.subjects.filter { it.competitionId == competition.id }.mapTo(hashSetOf()) { it.name.lowercase() }
            val candidates = all.filter { it.shortName.equals(competition.name, true) || it.title.equals(competition.name, true) }.take(12)
            for (entry in candidates) {
                val subjects = runCatching { subjects(entry.id) }.getOrNull() ?: continue
                if (subjects.any { it.questions == null } || subjects.mapTo(hashSetOf()) { it.name.lowercase() } != names) continue
                updates += competition.id to subjects
                break
            }
        }
        if (updates.isEmpty()) return null
        return { snapshot -> updates.fold(snapshot) { acc, (id, subjects) -> withWeights(acc, id, subjects) } }
    }

    // ---- busca: mesma regra do ExamCatalogSearch do app ----
    private val expansions = mapOf(
        "pm" to "policia militar", "pf" to "policia federal", "prf" to "policia rodoviaria federal", "pc" to "policia civil",
        "cbm" to "corpo de bombeiros militar", "bm" to "bombeiro militar", "mg" to "minas gerais", "trt" to "tribunal regional do trabalho",
        "trf" to "tribunal regional federal", "tj" to "tribunal de justica", "inss" to "instituto nacional do seguro social", "eb" to "exercito brasileiro",
    )
    private val ignored = setOf("de", "da", "do", "das", "dos", "e", "a", "o", "para", "concurso", "edital", "prova")

    private fun normalize(value: String) = stripAccents(value.lowercase()).replace(Regex("[^a-z0-9]+"), " ").trim()
    private fun words(value: String) = normalize(value).split(' ').filter { it.isNotBlank() }

    fun search(entries: List<CatalogEntry>, query: String, limit: Int = 30): List<CatalogEntry> {
        val terms = words(query).filter { it !in ignored }
        if (terms.isEmpty()) return emptyList()
        return entries.mapNotNull { entry ->
            val haystack = words(listOf(entry.shortName, entry.agency, entry.role, entry.board.orEmpty(), entry.year?.toString().orEmpty(), entry.searchNorm).joinToString(" "))
            var total = 0
            for (term in terms) {
                val direct = haystack.any { it.startsWith(term) }
                val expanded = expansions[term]?.split(' ')?.filter { it !in ignored }?.all { part -> haystack.any { it.startsWith(part) } } == true
                if (!direct && !expanded) return@mapNotNull null
                total += when { haystack.any { it == term } -> 3; direct -> 2; else -> 1 }
            }
            val short = normalize(entry.shortName)
            val joined = terms.joinToString(" ")
            if (short == joined || short.replace(" ", "") == joined.replace(" ", "")) total += 10 else if (short.startsWith(joined)) total += 5
            entry to total
        }.sortedWith(compareByDescending<Pair<CatalogEntry, Int>> { it.second }.thenByDescending { it.first.year ?: 0 }.thenBy { it.first.role })
            .take(limit).map { it.first }
    }

    /**
     * Grava o edital escolhido como o app (InitialSetupViewModel.chooseCatalogExam): concurso,
     * matérias e tópicos na ordem oficial, sem repetir o que já existe. Devolve (foto, id do concurso).
     */
    fun apply(data: Snapshot, entry: CatalogEntry, subjects: List<CatalogSubject>): Pair<Snapshot, Long> {
        val at = Date.now().toLong()
        val existing = data.competitions.firstOrNull { it.name.equals(entry.shortName, true) || it.name.equals(entry.title, true) }
        var next = data
        val competitionId = existing?.id ?: next.nextId(Keys.COMPETITIONS).also { id ->
            next = next
                .edit(Keys.COMPETITIONS) { it.with("primary" to JsonPrimitive(false)) }
                .append(Keys.COMPETITIONS, jsonOf("id" to id, "name" to (if (data.competitions.any { it.name.equals(entry.shortName, true) }) entry.title else entry.shortName).take(120), "primary" to true, "createdAt" to at, "externalId" to null))
        }
        if (existing != null) next = next.edit(Keys.COMPETITIONS) { it.with("primary" to JsonPrimitive((it["id"] as? JsonPrimitive)?.content == competitionId.toString())) }
        var subjectId = next.nextId(Keys.SUBJECTS)
        var topicId = next.nextId(Keys.TOPICS)
        val newSubjects = mutableListOf<JsonObject>()
        val newTopics = mutableListOf<JsonObject>()
        val currentSubjects = next.subjects.filter { it.competitionId == competitionId }
        // Peso real da prova vira a prioridade de cada matéria (só se o edital informa todas).
        val weights = ExamWeights.assessAll(subjects.map { it.share })
        subjects.forEachIndexed { index, subject ->
            val sid = currentSubjects.firstOrNull { it.name.equals(subject.name, true) }?.id
                ?: subjectId++.also { newSubjects += jsonOf("id" to it, "competitionId" to competitionId, "name" to subject.name, "position" to currentSubjects.size + index, "externalId" to null).let { row -> JsonObject(row + (weights.getOrNull(index)?.let(::priorityFields)?.mapValues { (_, v) -> v.toJsonElement() } ?: emptyMap())) } }
            val existingTitles = next.topics.filter { it.subjectId == sid }.mapTo(hashSetOf()) { it.title.lowercase() }
            var position = existingTitles.size
            subject.topics.forEach { title ->
                if (existingTitles.add(title.lowercase())) {
                    newTopics += jsonOf(
                        "id" to topicId++, "subjectId" to sid, "parentTopicId" to null, "title" to title, "description" to "", "position" to position++,
                        "status" to "NAO_ESTUDADO", "firstStudiedAt" to null, "lastStudiedAt" to null, "lastReviewedAt" to null, "notes" to "",
                        "priority" to "NORMAL", "externalId" to null, "contentOriginType" to "EDITAL", "scopeCovers" to null, "scopeExcludes" to null,
                    )
                }
            }
        }
        next = next.appendAll(mapOf(Keys.SUBJECTS to newSubjects, Keys.TOPICS to newTopics))
        if (weights.isNotEmpty()) next = withWeights(next, competitionId, subjects)
        return next to competitionId
    }

    /**
     * Grava o peso da prova nas matérias de um concurso já existente (quem escolheu o edital antes de
     * o catálogo ter os pesos). Nunca mexe na prioridade que a pessoa trocou nem numa avaliação já feita.
     */
    fun withWeights(data: Snapshot, competitionId: Long, subjects: List<CatalogSubject>): Snapshot {
        val weights = ExamWeights.assessAll(subjects.map { it.share })
        if (weights.isEmpty()) return data
        val byName = subjects.mapIndexed { index, subject -> subject.name.lowercase() to weights[index] }.toMap()
        val ids = data.subjects.filter { it.competitionId == competitionId && it.userPriorityOverride == null }.associate { it.id to it.name.lowercase() }
        return data.edit(Keys.SUBJECTS) { row ->
            val id = (row["id"] as? JsonPrimitive)?.content?.toLongOrNull()
            val name = ids[id] ?: return@edit row
            val source = (row["assessedPrioritySource"] as? JsonPrimitive)?.content
            val assessed = (row["hasAssessedPriority"] as? JsonPrimitive)?.content == "true"
            val weight = byName[name] ?: return@edit row
            if (assessed && source != "OFFICIAL_EXAM_STRUCTURE") return@edit row
            JsonObject(row + priorityFields(weight).mapValues { (_, v) -> v.toJsonElement() })
        }
    }

    private fun priorityFields(assessment: PriorityAssessment): Map<String, Any?> = mapOf(
        "assessedPriorityScore" to assessment.score,
        "assessedPrioritySource" to assessment.source.name,
        "assessedPriorityConfidence" to assessment.confidence.toDouble(),
        "assessedPriorityRationale" to assessment.rationale,
        "assessedPriorityEvidenceJson" to ExamWeights.evidenceJson(assessment.evidence),
        "hasAssessedPriority" to true,
    )
}
