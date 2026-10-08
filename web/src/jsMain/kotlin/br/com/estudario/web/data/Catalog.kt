package br.com.estudario.web.data

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
data class CatalogSubject(val name: String, val topics: List<String>)

@Serializable
private data class CatalogSubjectsRow(val subjects: List<CatalogSubject>)

object Catalog {
    private var cache: List<CatalogEntry>? = null

    private val headers get() = mapOf("apikey" to WebConfig.SUPABASE_KEY, "Authorization" to "Bearer ${WebConfig.SUPABASE_KEY}", "Accept" to "application/json")

    suspend fun entries(): List<CatalogEntry> {
        cache?.let { return it }
        val fields = "id,short_name,agency,role,board,year,search_norm,subject_count,topic_count,ready_topic_count"
        val response = httpRequest("GET", "${WebConfig.SUPABASE_URL}/rest/v1/exam_catalog?select=$fields&status=eq.PUBLISHED&order=short_name.asc,role.asc", headers)
        if (!response.ok) throw IllegalStateException("Não consegui carregar a lista de editais agora. Tente de novo.")
        return snapshotJson.decodeFromString(ListSerializer(CatalogEntry.serializer()), response.body).also { cache = it }
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
        subjects.forEachIndexed { index, subject ->
            val sid = currentSubjects.firstOrNull { it.name.equals(subject.name, true) }?.id
                ?: subjectId++.also { newSubjects += jsonOf("id" to it, "competitionId" to competitionId, "name" to subject.name, "position" to currentSubjects.size + index, "externalId" to null) }
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
        return next to competitionId
    }
}
