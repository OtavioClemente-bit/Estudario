package br.com.estudario.web.data

import br.com.estudario.web.WebConfig
import kotlinx.browser.localStorage
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.put
import kotlin.js.Date

/**
 * As matérias que vieram da biblioteca se atualizam sozinhas (o mesmo no app): uma vez por dia o site
 * pergunta à função library-refresh, com a marca da versão de cada tópico. Quando a equipe corrigiu
 * a matéria ou o recorte da banca, o texto é trocado NO LUGAR (mesmos ids): a teoria, o resumo, os
 * cartões, as dicas e as questões que a pessoa já tem, corrigidas. Respostas, erros, revisões e
 * favoritos continuam; nada é apagado. Matéria que veio da IA (capítulos que não são os da
 * biblioteca) fica como está e não é perguntada de novo.
 */
object LibraryRefresh {
    private const val PATH = "/functions/v1/library-refresh"
    private const val DAY_KEY = "estudario.biblioteca.atualizada"
    /** Marca de quem não é da biblioteca: não pergunta mais (gerar de novo limpa a marca). */
    const val NOT_LIBRARY = "ia"

    /** O que mudou num tópico, para o aviso do Folha. */
    data class Applied(val topicId: Long, val title: String, val parts: List<String>)

    private fun today(): String = Date().toDateString()
    private fun JsonObject.long(key: String): Long? = (this[key] as? JsonPrimitive)?.longOrNull
    private fun JsonElement?.text(): String? = (this as? JsonPrimitive)?.contentOrNull?.trim()?.takeIf { it.isNotBlank() && it != "null" }
    private fun JsonObject.arr(key: String): List<JsonElement> = (this[key] as? JsonArray).orEmpty()

    /** Os tópicos com matéria gerada (teoria ou questões do pacote do tópico) e o que cada um tem. */
    private class Candidate(val topic: Topic, val prefix: String, val blocks: List<String>, val chapterTitles: List<String>, val questionIds: List<Long>, val statements: List<String>)

    private fun rows(data: Snapshot, key: String) = data.array(key).mapNotNull { it as? JsonObject }

    private fun candidates(data: Snapshot): List<Candidate> {
        val topicRows = rows(data, Keys.TOPICS).associateBy { it.long("id") }
        // Linhas cruas: os modelos do site não guardam o externalId de teoria e resumo.
        val theories = rows(data, Keys.THEORIES).groupBy { it.long("topicId") }
        val summaries = rows(data, Keys.SUMMARIES).groupBy { it.long("topicId") }
        val snippets = rows(data, Keys.SNIPPETS).groupBy { it.long("topicId") }
        val questions = rows(data, Keys.QUESTIONS).groupBy { it.long("topicId") }
        return data.topics.mapNotNull { topic ->
            val row = topicRows[topic.id] ?: return@mapNotNull null
            if (row.str("libraryStamp") == NOT_LIBRARY) return@mapNotNull null
            val prefix = (row.str("externalId") ?: "topico-${topic.id}") + "-"
            val owned = { r: JsonObject -> r.str("externalId")?.startsWith(prefix) == true }
            val theory = theories[topic.id].orEmpty().firstOrNull { it.str("externalId") == "${prefix}teoria" }
            val own = questions[topic.id].orEmpty().filter { (it["hidden"] as? JsonPrimitive)?.contentOrNull != "true" && it.str("externalId")?.startsWith("${prefix}q-") == true }
            if (theory == null && own.isEmpty()) return@mapNotNull null
            val sums = summaries[topic.id].orEmpty()
            val snips = snippets[topic.id].orEmpty().filter(owned)
            val blocks = buildList {
                if (theory != null) add("THEORY")
                if (sums.any { it.str("externalId") == "${prefix}summary" }) add("SUMMARY")
                if (sums.any { it.str("externalId") == "${prefix}quick" }) add("QUICK_REVIEW")
                if (snips.any { it.str("kind") == "BIZU" || it.str("kind") == "PEGADINHA" }) add("TIPS_TRAPS")
                if (snips.any { it.str("kind") == "RECUPERACAO" }) add("ACTIVE_RECALL")
                if (own.isNotEmpty()) add("QUESTIONS")
            }
            val chapters = theory?.str("markdown")?.lineSequence()?.filter { it.startsWith("## ") }?.map { it.removePrefix("## ").trim() }?.toList().orEmpty()
            val statements = own.map { (it.str("statement") ?: "").trim().let { s -> if (s.length <= 300) s else s.take(150) + s.takeLast(150) } }
            Candidate(topic, prefix, blocks, chapters, own.mapNotNull { it.long("id") }, statements)
        }
    }

    private suspend fun boardOf(data: Snapshot, topic: Topic): String? {
        val competition = data.subjects.firstOrNull { it.id == topic.subjectId }?.let { s -> data.competitions.firstOrNull { it.id == s.competitionId } } ?: return null
        return Simulations.examProfile(data, competition.id).board?.trim()?.ifBlank { null } ?: Catalog.boardFor(competition.name)
    }

    /**
     * Pergunta e aplica (uma vez por dia; [force] ignora o dia). Devolve o que mudou, para o aviso.
     * Sem conta, sem internet ou na demonstração: nada.
     */
    suspend fun runDaily(force: Boolean = false): List<Applied> {
        if (Store.demo) return emptyList()
        if (!force && runCatching { localStorage.getItem(DAY_KEY) }.getOrNull() == today()) return emptyList()
        val token = Auth.accessToken() ?: return emptyList()
        val data = Store.data
        val all = candidates(data)
        if (all.isEmpty()) { runCatching { localStorage.setItem(DAY_KEY, today()) }; return emptyList() }
        val topicRows = data.array(Keys.TOPICS).mapNotNull { it as? JsonObject }.associateBy { it.long("id") }
        val subjects = data.subjects.associateBy { it.id }
        val applied = mutableListOf<Applied>()
        var allAnswered = true
        for (chunk in all.chunked(100)) {
            val byId = chunk.associateBy { it.topic.id.toString() }
            val body = buildJsonObject {
                put("items", buildJsonArray {
                    chunk.forEach { c ->
                        add(buildJsonObject {
                            put("id", c.topic.id.toString())
                            put("subjectName", subjects[c.topic.subjectId]?.name.orEmpty())
                            put("topicPath", buildJsonArray { AiContent.topicPath(data, c.topic).forEach { add(JsonPrimitive(it)) } })
                            boardOf(data, c.topic)?.let { put("board", it.take(80)) } ?: put("board", JsonNull)
                            topicRows[c.topic.id]?.str("libraryStamp")?.let { put("stamp", it) } ?: put("stamp", JsonNull)
                            put("blocks", buildJsonArray { c.blocks.forEach { add(JsonPrimitive(it)) } })
                            put("chapterTitles", buildJsonArray { c.chapterTitles.take(12).forEach { add(JsonPrimitive(it.take(200))) } })
                            put("questions", buildJsonArray { c.statements.take(400).forEach { add(JsonPrimitive(it)) } })
                        })
                    }
                })
            }.toString()
            val response = runCatching {
                httpRequest("POST", "${WebConfig.SUPABASE_URL}$PATH", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json", "Content-Type" to "application/json"), body)
            }.getOrNull()
            if (response == null || !response.ok) { allAnswered = false; break }
            val results = runCatching { snapshotJson.parseToJsonElement(response.body).jsonObject["items"] as? JsonArray }.getOrNull().orEmpty().mapNotNull { it as? JsonObject }
            for (result in results) {
                val c = byId[result.str("id")] ?: continue
                when (result.str("status")) {
                    "same" -> result.str("stamp")?.let { stamp -> if (topicRows[c.topic.id]?.str("libraryStamp") != stamp) Store.update { setStamp(it, c.topic.id, stamp) } }
                    "skip" -> Store.update { setStamp(it, c.topic.id, NOT_LIBRARY) }
                    "later" -> allAnswered = false
                    "update" -> {
                        var parts = emptyList<String>()
                        Store.update { snapshot -> apply(snapshot, c, result).also { parts = it.second }.first }
                        if (parts.isNotEmpty()) applied += Applied(c.topic.id, c.topic.title, parts)
                    }
                }
            }
        }
        // Só marca o dia quando tudo foi respondido (o que ficou para depois vem na próxima abertura).
        if (allAnswered) runCatching { localStorage.setItem(DAY_KEY, today()) }
        return applied
    }

    private fun setStamp(data: Snapshot, topicId: Long, stamp: String): Snapshot =
        data.edit(Keys.TOPICS) { if (it.long("id") == topicId) it.with("libraryStamp" to JsonPrimitive(stamp)) else it }

    /** Troca o texto no lugar (mesmos ids). Devolve a foto nova e o que mudou ("teoria revisada", "3 questões corrigidas"…). */
    private fun apply(data: Snapshot, c: Candidate, result: JsonObject): Pair<Snapshot, List<String>> {
        val content = result["content"] as? JsonObject ?: return data to emptyList()
        val stamp = result.str("stamp") ?: return data to emptyList()
        val at = Date.now().toLong()
        val p = c.prefix
        val topicId = c.topic.id
        val parts = mutableListOf<String>()
        var next = data

        // Teoria: o mesmo registro, com o texto novo (os grifos ficam presos à teoria).
        if (content.arr("chapters").isNotEmpty()) {
            val (title, markdown) = AiContent.theoryOf(content)
            var changed = false
            next = next.edit(Keys.THEORIES) { row ->
                if (row.long("topicId") != topicId || row.str("externalId") != "${p}teoria" || row.str("markdown") == markdown) row
                else { changed = true; row.with("title" to JsonPrimitive(title), "markdown" to JsonPrimitive(markdown), "updatedAt" to JsonPrimitive(at)) }
            }
            if (changed) parts += "teoria revisada"
        }
        // Resumo e revisão rápida (cartões): o mesmo registro.
        val summary = content["summary"].text()
        val cards = content.arr("flashcards").mapNotNull { it as? JsonObject }
        val quick = if (cards.isNotEmpty()) cards.joinToString("\n\n") { "### ${it["front"].text().orEmpty()}\n\n${it["back"].text().orEmpty()}" } else content["quickReview"].text()
        var summaryChanged = false
        var cardsChanged = false
        next = next.edit(Keys.SUMMARIES) { row ->
            if (row.long("topicId") != topicId) row
            else when (row.str("externalId")) {
                "${p}summary" -> if (summary == null || row.str("markdown") == summary) row else { summaryChanged = true; row.with("markdown" to JsonPrimitive(summary), "updatedAt" to JsonPrimitive(at)) }
                "${p}quick" -> if (quick == null || row.str("markdown") == quick) row else { cardsChanged = true; row.with("markdown" to JsonPrimitive(quick), "updatedAt" to JsonPrimitive(at)) }
                else -> row
            }
        }
        if (summaryChanged) parts += "resumo atualizado"
        if (cardsChanged) parts += "cartões atualizados"
        // Dicas, pegadinhas e memorização: a lista do tópico é trocada quando o texto mudou.
        val snippetSets = listOf("tips" to "BIZU", "traps" to "PEGADINHA", "activeRecall" to "RECUPERACAO")
        var snippetsChanged = false
        val newSnippets = mutableListOf<JsonObject>()
        var snippetId = next.nextId(Keys.SNIPPETS)
        for ((key, kind) in snippetSets) {
            val items = content.arr(key)
            if (items.isEmpty()) continue
            val texts = items.map { e -> (e as? JsonObject)?.let { it["text"].text() ?: it["question"].text() } ?: e.text() }
            val current = rows(next, Keys.SNIPPETS).filter { it.long("topicId") == topicId && it.str("kind") == kind && it.str("externalId")?.startsWith(p) == true }.sortedBy { it.long("position") ?: 0 }
            if (current.map { it.str("text") } == texts.filterNotNull() || current.isEmpty()) continue
            snippetsChanged = true
            val favorites = current.filter { (it["favorite"] as? JsonPrimitive)?.contentOrNull == "true" }.mapNotNullTo(hashSetOf()) { it.str("text") }
            next = next.edit(Keys.SNIPPETS) { row -> if (row.long("topicId") == topicId && row.str("kind") == kind && row.str("externalId")?.startsWith(p) == true) null else row }
            items.forEachIndexed { index, element ->
                val obj = element as? JsonObject
                val text = texts[index] ?: return@forEachIndexed
                newSnippets += jsonOf(
                    "id" to snippetId++, "topicId" to topicId, "kind" to kind, "text" to text, "favorite" to (text in favorites),
                    "externalId" to "$p${kind.lowercase()}-$index", "position" to index, "createdAt" to at, "updatedAt" to at, "answer" to obj?.get("answer").text(),
                )
            }
        }
        if (snippetsChanged) { next = next.appendAll(mapOf(Keys.SNIPPETS to newSnippets)); parts += "dicas e cartões de memória atualizados" }
        // Questões que a pessoa já tem: corrigidas no lugar (mesmo id, mesmas respostas e erros).
        var fixed = 0
        var optionId = (rows(next, Keys.QUESTIONS).flatMap { (it["options"] as? JsonArray).orEmpty() }.mapNotNull { (it as? JsonObject)?.long("id") }.maxOrNull() ?: 0L) + Snapshot.ID_GAP
        val updates = result.arr("questions").mapNotNull { it as? JsonObject }.mapNotNull { q ->
            val index = (q["index"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull() ?: return@mapNotNull null
            val id = c.questionIds.getOrNull(index) ?: return@mapNotNull null
            val parts3 = AiContent.questionParts(q["question"] as? JsonObject ?: return@mapNotNull null) ?: return@mapNotNull null
            id to parts3
        }.toMap()
        next = next.edit(Keys.QUESTIONS) { row ->
            val id = row.long("id") ?: return@edit row
            val (statement, explanation, options) = updates[id] ?: return@edit row
            val oldOptions = (row["options"] as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }.sortedBy { it.long("position") ?: 0 }
            val same = row.str("statement") == statement && row.str("explanation") == explanation &&
                oldOptions.map { Triple(it.str("key"), it.str("text"), (it["correct"] as? JsonPrimitive)?.contentOrNull == "true") } == options.map { Triple(it.first, it.second, it.third) }
            if (same) return@edit row
            fixed++
            val newOptions = options.mapIndexed { position, (key, text, correct) ->
                jsonOf("id" to (oldOptions.getOrNull(position)?.long("id") ?: optionId++), "key" to key, "text" to text, "correct" to correct, "position" to position)
            }
            row.with(
                "statement" to JsonPrimitive(statement), "explanation" to JsonPrimitive(explanation),
                "normalizedHash" to JsonPrimitive(AiContent.hashOfStatement(statement)), "options" to JsonArray(newOptions),
            )
        }
        if (fixed > 0) parts += if (fixed == 1) "1 questão corrigida" else "$fixed questões corrigidas"
        next = setStamp(next, topicId, stamp)
        return next to parts
    }
}
