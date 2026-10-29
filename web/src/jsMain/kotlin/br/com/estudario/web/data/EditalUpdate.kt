package br.com.estudario.web.data

import br.com.estudario.domain.EditalSync
import br.com.estudario.web.WebConfig
import kotlinx.browser.localStorage
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.put
import kotlin.js.Date

/**
 * O edital escolhido no catálogo se atualiza sozinho (o mesmo no app; a regra em EditalSync): uma vez
 * por dia o site confere a data de atualização dos editais da pessoa e, se a equipe corrigiu algum,
 * acrescenta tópicos e matérias novos, renomeia os que mudaram de nome e marca "saiu do edital" os que
 * o catálogo tirou (nunca apaga: progresso, matérias geradas e erros ficam; o tópico só sai do plano).
 *
 * No concurso ficam gravados o edital de origem ("catalogId"), a data da versão aplicada e a lista
 * de tópicos dela ("catalogBaseline"), que diz o que veio do catálogo e o que a pessoa criou.
 */
object EditalUpdate {
    private const val DAY_KEY = "estudario.edital.atualizado"

    data class Applied(val competition: String, val added: Int, val renamed: Int, val removed: Int)

    private val headers get() = mapOf("apikey" to WebConfig.SUPABASE_KEY, "Authorization" to "Bearer ${WebConfig.SUPABASE_KEY}", "Accept" to "application/json")
    private fun JsonObject.long(key: String): Long? = (this[key] as? JsonPrimitive)?.longOrNull
    private fun rows(data: Snapshot, key: String) = data.array(key).mapNotNull { it as? JsonObject }

    /** Os tópicos que saíram do edital (fora do plano, com tudo guardado). */
    fun outOfEdital(data: Snapshot): Set<Long> =
        rows(data, Keys.TOPICS).filter { (it["outOfEdital"] as? JsonPrimitive)?.contentOrNull == "true" }.mapNotNullTo(hashSetOf()) { it.long("id") }

    /** O edital do catálogo de onde o concurso veio: o gravado ou, nos antigos, o de mesmo nome. */
    private fun catalogIdOf(row: JsonObject, entries: List<CatalogEntry>): String? {
        if (row.str("catalogIgnored") != null) return null
        row.str("catalogId")?.let { return it }
        val name = EditalSync.norm(row.str("name").orEmpty())
        return entries.firstOrNull { EditalSync.norm(it.title) == name }?.id
            ?: entries.filter { EditalSync.norm(it.shortName) == name }.singleOrNull()?.id
    }

    suspend fun runDaily(force: Boolean = false): List<Applied> {
        if (Store.demo) return emptyList()
        val today = Date().toDateString()
        if (!force && runCatching { localStorage.getItem(DAY_KEY) }.getOrNull() == today) return emptyList()
        val data = Store.data
        val competitions = rows(data, Keys.COMPETITIONS)
        if (competitions.isEmpty()) return emptyList()
        val entries = runCatching { Catalog.entries() }.getOrNull() ?: return emptyList()
        val linked = competitions.mapNotNull { row -> catalogIdOf(row, entries)?.let { row to it } }
        if (linked.isEmpty()) { runCatching { localStorage.setItem(DAY_KEY, today) }; return emptyList() }
        // Só as datas de atualização (barato); o edital inteiro só quando mudou.
        val ids = linked.map { it.second }.distinct().joinToString(",") { "\"$it\"" }
        val response = runCatching {
            httpRequest("GET", "${WebConfig.SUPABASE_URL}/rest/v1/exam_catalog?select=id,updated_at&status=eq.PUBLISHED&id=in.(${js("encodeURIComponent")(ids)})", headers)
        }.getOrNull() ?: return emptyList()
        if (!response.ok) return emptyList()
        val stamps = snapshotJson.decodeFromString(ListSerializer(CatalogStamp.serializer()), response.body).associate { it.id to it.updatedAt }
        val applied = mutableListOf<Applied>()
        for ((row, catalogId) in linked) {
            val competitionId = row.long("id") ?: continue
            val stamp = stamps[catalogId] ?: continue
            if (row.str("catalogUpdatedAt") == stamp && row.str("catalogId") == catalogId) continue
            val subjects = runCatching { Catalog.subjects(catalogId) }.getOrNull() ?: continue
            var result: Applied? = null
            Store.update { snapshot -> apply(snapshot, competitionId, catalogId, stamp, subjects).also { result = it.second }.first }
            result?.takeIf { it.added + it.renamed + it.removed > 0 }?.let(applied::add)
        }
        runCatching { localStorage.setItem(DAY_KEY, today) }
        return applied
    }

    private fun apply(data: Snapshot, competitionId: Long, catalogId: String, stamp: String, catalog: List<CatalogSubject>): Pair<Snapshot, Applied?> {
        val competition = rows(data, Keys.COMPETITIONS).firstOrNull { it.long("id") == competitionId } ?: return data to null
        val baseline = (competition["catalogBaseline"] as? JsonObject)?.mapValues { (_, v) -> (v as? JsonArray).orEmpty().mapNotNull { (it as? JsonPrimitive)?.contentOrNull } }
        val subjects = data.subjects.filter { it.competitionId == competitionId }
        val current = subjects.map { s -> EditalSync.Subject(s.id, s.name, data.topics.filter { it.subjectId == s.id && it.parentTopicId == null }.map { EditalSync.Topic(it.id, it.title) }) }
        val catalogSubjects = catalog.map { EditalSync.CatalogSubject(it.name, it.topics) }
        // Reconhecido só pelo nome (sem vínculo gravado): só se os tópicos dele forem mesmo deste edital.
        if (competition.str("catalogId") == null && baseline == null && !EditalSync.cameFrom(current, catalogSubjects)) {
            return data.edit(Keys.COMPETITIONS) { r -> if (r.long("id") == competitionId) r.with("catalogIgnored" to JsonPrimitive(catalogId)) else r } to null
        }
        val plan = EditalSync.plan(current, catalogSubjects, baseline)
        var next = data
        // Matérias renomeadas e tópicos renomeados: só o nome (o resto fica).
        val subjectNames = plan.renamedSubjects.toMap()
        if (subjectNames.isNotEmpty()) next = next.edit(Keys.SUBJECTS) { r -> subjectNames[r.long("id")]?.let { r.with("name" to JsonPrimitive(it)) } ?: r }
        val topicNames = plan.renamedTopics.toMap()
        next = next.edit(Keys.TOPICS) { r ->
            val id = r.long("id") ?: return@edit r
            var out = r
            topicNames[id]?.let { out = out.with("title" to JsonPrimitive(it)) }
            if (id in plan.removed) out = out.with("outOfEdital" to JsonPrimitive(true))
            else if (id in plan.inEdital && (r["outOfEdital"] as? JsonPrimitive)?.contentOrNull == "true") out = out.with("outOfEdital" to JsonPrimitive(false))
            out
        }
        // Novos: tópicos nas matérias que já existem e matérias inteiras.
        var topicId = next.nextId(Keys.TOPICS)
        var subjectId = next.nextId(Keys.SUBJECTS)
        val newTopics = mutableListOf<JsonObject>()
        val newSubjects = mutableListOf<JsonObject>()
        fun topicRow(sid: Long, title: String, position: Int) = jsonOf(
            "id" to topicId++, "subjectId" to sid, "parentTopicId" to null, "title" to title, "description" to "", "position" to position,
            "status" to "NAO_ESTUDADO", "firstStudiedAt" to null, "lastStudiedAt" to null, "lastReviewedAt" to null, "notes" to "",
            "priority" to "NORMAL", "externalId" to null, "contentOriginType" to "EDITAL", "scopeCovers" to null, "scopeExcludes" to null,
        )
        plan.newTopics.forEach { (sid, title, position) -> newTopics += topicRow(sid, title, position) }
        plan.newSubjects.forEachIndexed { index, subject ->
            val sid = subjectId++
            newSubjects += jsonOf("id" to sid, "competitionId" to competitionId, "name" to subject.name, "position" to subjects.size + index, "externalId" to null)
            subject.topics.forEachIndexed { position, title -> newTopics += topicRow(sid, title, position) }
        }
        next = next.appendAll(mapOf(Keys.SUBJECTS to newSubjects, Keys.TOPICS to newTopics))
        // Peso da prova atualizado (sem mexer no que a pessoa trocou à mão).
        next = Catalog.withWeights(next, competitionId, catalog)
        next = next.edit(Keys.COMPETITIONS) { r ->
            if (r.long("id") != competitionId) r
            else r.with(
                "catalogId" to JsonPrimitive(catalogId),
                "catalogUpdatedAt" to JsonPrimitive(stamp),
                "catalogBaseline" to buildJsonObject { plan.baseline.forEach { (k, v) -> put(k, buildJsonArray { v.forEach { add(JsonPrimitive(it)) } }) } },
            )
        }
        val added = plan.newTopics.size + plan.newSubjects.sumOf { it.topics.size }
        return next to Applied(competition.str("name").orEmpty(), added, plan.renamedTopics.size + plan.renamedSubjects.size, plan.removed.size)
    }
}

@kotlinx.serialization.Serializable
private data class CatalogStamp(val id: String, @kotlinx.serialization.SerialName("updated_at") val updatedAt: String)
