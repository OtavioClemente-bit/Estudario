package br.com.estudario.web.data

import br.com.estudario.web.WebConfig
import kotlinx.browser.localStorage
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put

/**
 * Tópicos que ficam fora do plano do Grátis (o mesmo LibraryGaps do app): os que ainda não têm matéria
 * pronta na biblioteca, porque o Grátis não usa IA. O servidor responde quais estão prontos
 * (library-ready); os outros ficam guardados neste navegador e o montador do plano deixa de fora.
 * Quando a matéria é publicada, o tópico volta no próximo replanejamento. Nos pagos, a lista é vazia.
 */
object LibraryGaps {
    private const val KEY = "estudario.lacunas"
    private const val FREE = "estudario.gratis"
    private const val PATH = "/functions/v1/library-ready"

    /** Fora do plano: os sem matéria pronta (Grátis) e os que saíram do edital na atualização do catálogo. */
    fun excluded(): Set<Long> =
        runCatching { localStorage.getItem(KEY) }.getOrNull()?.split(",")?.mapNotNullTo(hashSetOf()) { it.toLongOrNull() }.orEmpty() +
            EditalUpdate.outOfEdital(Store.data)

    private fun save(ids: Set<Long>) { runCatching { localStorage.setItem(KEY, ids.joinToString(",")) } }

    /** A conta estava no Grátis na última consulta: o plano segue as regras dele (PlannerSnapshotBuilder.Input.freeRules). */
    fun free(): Boolean = runCatching { localStorage.getItem(FREE) }.getOrNull() == "1"

    /** [free]: a conta está no Grátis (null quando não deu para saber: fica a lista de antes). */
    suspend fun refresh(data: Snapshot, free: Boolean?) {
        if (free == null) return
        runCatching { localStorage.setItem(FREE, if (free) "1" else "0") }
        if (!free) { save(emptySet()); return }
        val token = Auth.accessToken() ?: return
        val withTheory = data.theories.mapTo(hashSetOf()) { it.topicId }
        val parents = data.topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val byId = data.topics.associateBy { it.id }
        val subjects = data.subjects.associateBy { it.id }
        val candidates = data.topics.filter { it.id !in withTheory && it.id !in parents && it.status == "NAO_ESTUDADO" }
        if (candidates.isEmpty()) { save(emptySet()); return }
        fun pathOf(topic: Topic): List<String> {
            val path = ArrayDeque<String>()
            var current: Topic? = topic
            val seen = HashSet<Long>()
            while (current != null && seen.add(current.id)) { path.addFirst(current.title.take(300)); current = current.parentTopicId?.let { byId[it] } }
            return path.toList().takeLast(6)
        }
        val ready = hashSetOf<Long>()
        for (chunk in candidates.chunked(400)) {
            val body = buildJsonObject {
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
            (runCatching { snapshotJson.parseToJsonElement(response.body).jsonObject["ready"] as? JsonArray }.getOrNull()).orEmpty()
                .mapNotNullTo(ready) { (it as? JsonPrimitive)?.content?.toLongOrNull() }
        }
        save(candidates.map { it.id }.filterTo(hashSetOf()) { it !in ready })
    }
}
