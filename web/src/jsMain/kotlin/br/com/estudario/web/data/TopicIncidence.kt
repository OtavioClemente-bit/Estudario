package br.com.estudario.web.data

import br.com.estudario.web.WebConfig
import kotlinx.browser.localStorage
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Quanto cada tópico cai nas provas de verdade (o mesmo TopicIncidence do app): questões oficiais da
 * banca do concurso no tópico; sem banca ou sem questão dela, de todas as bancas. Guardado neste
 * navegador e usado pelo montador do plano (o que mais cai ganha mais treino; o que quase não cai vai
 * para o fim de cada matéria). Atualiza uma vez por dia.
 */
object TopicIncidence {
    private const val KEY = "estudario.incidencia"
    private const val AT = "estudario.incidencia.em"
    private const val PATH = "/functions/v1/library-incidence"
    private const val DAY_MS = 86_400_000.0

    private const val HOT = "estudario.incidencia.alta"
    private var cache: Map<Long, Int>? = null
    private var hotCache: Set<Long>? = null

    /** Os tópicos que mais caem dentro de cada matéria (a mesma régua do plano): etiqueta "Cai muito". */
    fun hot(): Set<Long> = hotCache ?: run {
        val ids = runCatching { localStorage.getItem(HOT) }.getOrNull().orEmpty().split(",").mapNotNullTo(hashSetOf()) { it.toLongOrNull() }
        hotCache = ids
        ids
    }

    fun counts(): Map<Long, Int> = cache ?: run {
        val parsed = runCatching {
            val o = snapshotJson.parseToJsonElement(localStorage.getItem(KEY) ?: "{}").jsonObject
            o.entries.mapNotNull { (k, v) -> k.toLongOrNull()?.let { id -> v.jsonPrimitive.intOrNull?.let { id to it } } }.toMap()
        }.getOrDefault(emptyMap())
        cache = parsed
        parsed
    }

    suspend fun refresh(data: Snapshot) {
        val last = runCatching { localStorage.getItem(AT)?.toDoubleOrNull() }.getOrNull() ?: 0.0
        if (kotlin.js.Date.now() - last < DAY_MS) return
        val token = Auth.accessToken() ?: return
        val parents = data.topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val leaves = data.topics.filter { it.id !in parents }
        if (leaves.isEmpty()) return
        val byId = data.topics.associateBy { it.id }
        val subjects = data.subjects.associateBy { it.id }
        fun pathOf(topic: Topic): List<String> {
            val path = ArrayDeque<String>()
            var current: Topic? = topic
            val seen = HashSet<Long>()
            while (current != null && seen.add(current.id)) { path.addFirst(current.title.take(4000)); current = current.parentTopicId?.let { byId[it] } }
            return path.toList().takeLast(6)
        }
        val boards = HashMap<Long, String>()
        fun boardOf(topic: Topic): String = subjects[topic.subjectId]?.competitionId?.let { c ->
            boards.getOrPut(c) { runCatching { Simulations.examProfile(data, c).board?.trim() }.getOrNull().orEmpty() }
        }.orEmpty()
        val raw = HashMap<Long, Pair<Int, Int>>() // tópico -> (banca, todas)
        for ((board, list) in leaves.groupBy(::boardOf)) {
            for (chunk in list.chunked(400)) {
                val body = buildJsonObject {
                    if (board.isNotBlank()) put("board", board)
                    put("items", buildJsonArray {
                        chunk.forEach { topic ->
                            add(buildJsonObject {
                                put("id", topic.id.toString())
                                put("subjectName", subjects[topic.subjectId]?.name.orEmpty())
                                put("topicPath", buildJsonArray { pathOf(topic).forEach { add(JsonPrimitive(it)) } })
                            })
                        }
                    })
                }.toString()
                val response = runCatching {
                    httpRequest("POST", "${WebConfig.SUPABASE_URL}$PATH", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json", "Content-Type" to "application/json"), body)
                }.getOrNull()
                if (response == null || !response.ok) return
                val rows = runCatching { snapshotJson.parseToJsonElement(response.body).jsonObject["incidence"] as? JsonArray }.getOrNull().orEmpty()
                rows.forEach { row ->
                    val o = row as? JsonObject ?: return@forEach
                    val id = (o["id"] as? JsonPrimitive)?.content?.toLongOrNull() ?: return@forEach
                    raw[id] = ((o["board"] as? JsonPrimitive)?.intOrNull ?: 0) to ((o["all"] as? JsonPrimitive)?.intOrNull ?: 0)
                }
            }
        }
        // Uma régua só por matéria (como no app): a banca quando ela tem questões em 3+ tópicos; senão, todas.
        val subjectOf = leaves.associate { it.id to it.subjectId }
        val result = raw.entries.groupBy { subjectOf[it.key] }.flatMap { (_, list) ->
            val byBoard = list.count { it.value.first > 0 } >= 3
            list.map { (id, v) -> id to if (byBoard) v.first else v.second }
        }.toMap()
        val hot = br.com.estudario.domain.planner.StudyPlanBlueprint.incidenceLevels(
            leaves.groupBy { it.subjectId }.values.map { list -> list.mapNotNull { topic -> result[topic.id]?.let { topic.id to it } } },
        ).first
        hotCache = hot
        runCatching {
            localStorage.setItem(HOT, hot.joinToString(","))
            localStorage.setItem(KEY, buildJsonObject { result.forEach { (k, v) -> put(k.toString(), v) } }.toString())
            localStorage.setItem(AT, kotlin.js.Date.now().toString())
        }
        cache = result
    }
}
