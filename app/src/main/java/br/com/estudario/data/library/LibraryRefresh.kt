package br.com.estudario.data.library

import android.content.Context
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiContentRequest
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import br.com.estudario.data.local.QuestionOptionEntity
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.data.local.TopicSnippetEntity
import br.com.estudario.data.transfer.normalizedQuestionHash
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

/**
 * As matérias que vieram da biblioteca se atualizam sozinhas (o mesmo no site): uma vez por dia o app
 * pergunta à função library-refresh, com a marca da versão de cada tópico. Quando a equipe corrigiu
 * a matéria ou o recorte da banca, o texto é trocado NO LUGAR (mesmos registros): a teoria, o resumo,
 * os cartões, as dicas e as questões que a pessoa já tem, corrigidas. Respostas, erros, revisões e
 * favoritos continuam; nada é apagado. Matéria que veio da IA (capítulos que não são os da
 * biblioteca) fica como está e não é perguntada de novo.
 */
object LibraryRefresh {
    private const val PATH = "/functions/v1/library-refresh"
    private const val PREFS = "biblioteca_atualizacao"
    private const val DAY = "dia"
    /** Marca de quem não é da biblioteca: não pergunta mais (gerar de novo limpa a marca). */
    private const val NOT_LIBRARY = "ia"

    /** O que mudou num tópico, para o aviso do Folha. */
    data class Applied(val topicId: Long, val title: String, val parts: List<String>)

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /**
     * A marca guardada vem com a "impressão" da teoria daquele momento ("marca|impressão"): se a teoria
     * mudou por fora (o tópico foi gerado de novo), a marca deixa de valer e a matéria passa pela
     * adoção outra vez (a nova pode ter vindo da IA).
     */
    private fun stampOf(context: Context, topicId: Long, theory: String?): String? {
        val saved = prefs(context).getString("t$topicId", null) ?: return null
        val stamp = saved.substringBefore('|')
        val print = saved.substringAfter('|', "")
        return if (print.isEmpty() || print == fingerprint(theory)) stamp else null
    }
    private fun setStamp(context: Context, topicId: Long, stamp: String?, theory: String?) =
        prefs(context).edit().apply { if (stamp == null) remove("t$topicId") else putString("t$topicId", "$stamp|${fingerprint(theory)}") }.apply()
    private fun fingerprint(theory: String?): String = theory?.hashCode()?.toUInt()?.toString(36) ?: "-"

    private class Candidate(
        val topicId: Long, val title: String, val prefix: String, val item: JSONObject, val questionIds: List<Long>, val theory: String?,
    )

    /**
     * Pergunta e aplica (uma vez por dia; [force] ignora o dia). Devolve o que mudou, para o aviso.
     * Sem conta ou sem internet: nada (tenta de novo na próxima vez).
     */
    suspend fun runDaily(app: EstudarioApplication, force: Boolean = false): List<Applied> {
        val today = LocalDate.now().toString()
        if (!force && prefs(app).getString(DAY, null) == today) return emptyList()
        val token = app.supabaseAuthRepository.accessToken() ?: return emptyList()
        val config = app.supabaseClientConfig
        if (!config.isConfigured) return emptyList()
        val dao = app.database.dao()
        val topics = dao.topicsOnce()
        val subjects = dao.subjectsOnce().associateBy { it.id }
        val competitions = dao.competitionsOnce().associateBy { it.id }
        val theories = dao.theoriesOnce().groupBy { it.topicId }
        val summaries = dao.summariesOnce().groupBy { it.topicId }
        val snippets = dao.snippetsOnce().groupBy { it.topicId }
        val questions = dao.questionsOnce().groupBy { it.question.topicId }
        val boards = HashMap<Long, String>()
        val candidates = topics.mapNotNull { topic ->
            val prefix = (topic.externalId ?: "topico-${topic.id}") + "-"
            // O id externo pode vir com o pacote na frente ("pacote:topico-x-teoria").
            fun owns(externalId: String?, suffix: String) = externalId != null && (externalId == "$prefix$suffix" || externalId.endsWith(":$prefix$suffix"))
            val theory = theories[topic.id].orEmpty().firstOrNull { owns(it.externalId, "teoria") }
            val stamp = stampOf(app, topic.id, theory?.markdown)
            if (stamp == NOT_LIBRARY) return@mapNotNull null
            val own = questions[topic.id].orEmpty().filter { !it.question.isHidden && it.question.externalId?.let { e -> e.startsWith("${prefix}q-") || e.contains(":${prefix}q-") } == true }
            if (theory == null && own.isEmpty()) return@mapNotNull null
            val sums = summaries[topic.id].orEmpty()
            val snips = snippets[topic.id].orEmpty().filter { it.externalId?.contains(prefix) == true }
            val blocks = JSONArray().apply {
                if (theory != null) put("THEORY")
                if (sums.any { owns(it.externalId, "summary") }) put("SUMMARY")
                if (sums.any { owns(it.externalId, "quick") }) put("QUICK_REVIEW")
                if (snips.any { it.kind == SnippetKind.BIZU || it.kind == SnippetKind.PEGADINHA }) put("TIPS_TRAPS")
                if (snips.any { it.kind == SnippetKind.RECUPERACAO }) put("ACTIVE_RECALL")
                if (own.isNotEmpty()) put("QUESTIONS")
            }
            val subject = subjects[topic.subjectId]
            val competition = subject?.let { competitions[it.competitionId] }
            val board = competition?.let { c -> boards.getOrPut(c.id) { runCatching { br.com.estudario.data.catalog.editalBoardOf(app, c.id, c.name) }.getOrDefault("") } }.orEmpty()
            val chapters = theory?.markdown?.lineSequence()?.filter { it.startsWith("## ") }?.map { it.removePrefix("## ").trim() }?.take(12)?.toList().orEmpty()
            val item = JSONObject()
                .put("id", topic.id.toString())
                .put("subjectName", subject?.name.orEmpty())
                .put("topicPath", JSONArray(AiContentRequest.pathOf(topic, topics)))
                .put("board", board.takeIf { it.isNotBlank() }?.take(80) ?: JSONObject.NULL)
                .put("stamp", stamp ?: JSONObject.NULL)
                .put("blocks", blocks)
                .put("chapterTitles", JSONArray(chapters.map { it.take(200) }))
                .put("questions", JSONArray(own.take(400).map { q -> q.question.statement.trim().let { if (it.length <= 300) it else it.take(150) + it.takeLast(150) } }))
            Candidate(topic.id, topic.title, prefix, item, own.take(400).map { it.question.id }, theory?.markdown)
        }
        if (candidates.isEmpty()) { prefs(app).edit().putString(DAY, today).apply(); return emptyList() }
        val transport = UrlConnectionAiHttpTransport(config.projectUrl)
        val applied = mutableListOf<Applied>()
        var allAnswered = true
        try {
            for (chunk in candidates.chunked(100)) {
                val byId = chunk.associateBy { it.topicId.toString() }
                val response = withTimeout(40_000) {
                    transport.execute(AiHttpRequest(
                        method = "POST",
                        path = PATH,
                        headers = mapOf("Authorization" to "Bearer $token", "apikey" to config.publishableKey, "Accept" to "application/json", "Content-Type" to "application/json; charset=utf-8"),
                        body = JSONObject().put("items", JSONArray(chunk.map { it.item })).toString().toByteArray(Charsets.UTF_8),
                    ))
                }
                if (response.status != 200) { allAnswered = false; break }
                val results = JSONObject(response.body).optJSONArray("items") ?: JSONArray()
                for (i in 0 until results.length()) {
                    val result = results.optJSONObject(i) ?: continue
                    val c = byId[result.optString("id")] ?: continue
                    when (result.optString("status")) {
                        "same" -> result.optString("stamp").takeIf { it.isNotBlank() }?.let { setStamp(app, c.topicId, it, c.theory) }
                        "skip" -> setStamp(app, c.topicId, NOT_LIBRARY, c.theory)
                        "later" -> allAnswered = false
                        "update" -> {
                            val parts = apply(app, c, result)
                            if (parts.isNotEmpty()) applied += Applied(c.topicId, c.title, parts)
                        }
                    }
                }
            }
        } catch (error: CancellationException) {
            if (error !is kotlinx.coroutines.TimeoutCancellationException) throw error
            allAnswered = false
        } catch (_: Throwable) {
            allAnswered = false
        }
        // Só marca o dia quando tudo foi respondido (o que ficou para depois vem na próxima abertura).
        if (allAnswered) prefs(app).edit().putString(DAY, today).apply()
        return applied
    }

    private fun JSONObject.text(key: String): String? = optString(key, "").trim().takeIf { it.isNotBlank() && it != "null" }

    /** Troca o texto no lugar. Devolve o que mudou ("teoria revisada", "3 questões corrigidas"…). */
    private suspend fun apply(app: EstudarioApplication, c: Candidate, result: JSONObject): List<String> {
        val content = result.optJSONObject("content") ?: return emptyList()
        val stamp = result.text("stamp") ?: return emptyList()
        val dao = app.database.dao()
        val now = System.currentTimeMillis()
        val p = c.prefix
        fun owns(externalId: String?, suffix: String) = externalId != null && (externalId == "$p$suffix" || externalId.endsWith(":$p$suffix"))
        val parts = mutableListOf<String>()

        // Teoria: o mesmo registro, com o texto novo (o mesmo formato da importação: capítulos com "## ").
        val chapters = content.optJSONArray("chapters") ?: JSONArray()
        var theoryNow = c.theory
        if (chapters.length() > 0) {
            // O mesmo formato da importação (EstudoPackageService.parseTheories), para comparar texto com texto.
            val title = content.text("theoryTitle") ?: "Teoria"
            val joined = (0 until chapters.length()).joinToString("\n\n") { i ->
                val chapter = chapters.getJSONObject(i)
                val chapterTitle = chapter.text("title") ?: "Capítulo ${i + 1}"
                val body = chapter.optString("markdown").trim()
                val first = body.trimStart().lineSequence().firstOrNull().orEmpty()
                if (first.startsWith("#") && first.trimStart('#').trim().trim('*').trim().equals(chapterTitle.trim(), true)) body.trimStart() else "## $chapterTitle\n\n$body"
            }
            val markdown = if (joined.startsWith("# ")) joined else "# $title\n\n$joined"
            val theory = dao.theoriesOnce().firstOrNull { it.topicId == c.topicId && owns(it.externalId, "teoria") }
            if (theory != null && theory.markdown != markdown) {
                dao.updateTheory(theory.copy(title = title, markdown = markdown, updatedAt = now))
                theoryNow = markdown
                parts += "teoria revisada"
            }
        }
        // Resumo e revisão rápida (cartões).
        val summary = content.text("summary")
        val cards = content.optJSONArray("flashcards") ?: JSONArray()
        val quick = if (cards.length() > 0) (0 until cards.length()).joinToString("\n\n") { i -> val card = cards.getJSONObject(i); "### ${card.optString("front").trim()}\n\n${card.optString("back").trim()}" } else content.text("quickReview")
        dao.summariesOnce().filter { it.topicId == c.topicId }.forEach { row ->
            when {
                owns(row.externalId, "summary") && row.kind == SummaryKind.COMPLETO && summary != null && row.markdown != summary -> { dao.updateSummary(row.copy(markdown = summary, updatedAt = now)); parts += "resumo atualizado" }
                owns(row.externalId, "quick") && quick != null && row.markdown != quick -> { dao.updateSummary(row.copy(markdown = quick, updatedAt = now)); parts += "cartões atualizados" }
            }
        }
        // Dicas, pegadinhas e memorização: a lista é trocada quando o texto mudou (favoritos ficam pelo texto).
        var snippetsChanged = false
        for ((key, kind) in listOf("tips" to SnippetKind.BIZU, "traps" to SnippetKind.PEGADINHA, "activeRecall" to SnippetKind.RECUPERACAO)) {
            val items = content.optJSONArray(key) ?: continue
            if (items.length() == 0) continue
            val texts = (0 until items.length()).map { i -> items.optJSONObject(i)?.let { it.text("text") ?: it.text("question") } ?: items.optString(i).trim() }
            val current = dao.snippetsOnce().filter { it.topicId == c.topicId && it.kind == kind && it.externalId?.contains(p) == true }.sortedBy { it.position }
            if (current.isEmpty() || current.map { it.text } == texts) continue
            snippetsChanged = true
            val favorites = current.filter { it.isFavorite }.mapTo(hashSetOf()) { it.text }
            current.forEach { dao.deleteSnippet(it) }
            texts.forEachIndexed { i, text ->
                if (text.isBlank()) return@forEachIndexed
                dao.insertSnippet(TopicSnippetEntity(topicId = c.topicId, kind = kind, text = text, isFavorite = text in favorites, externalId = "$p${kind.name.lowercase()}-$i", position = i, answer = items.optJSONObject(i)?.text("answer")))
            }
        }
        if (snippetsChanged) parts += "dicas e cartões de memória atualizados"
        // Questões que a pessoa já tem: corrigidas no lugar (mesmo id, mesmas respostas e erros).
        val byId = dao.questionsOnce().associateBy { it.question.id }
        var fixed = 0
        val updates = result.optJSONArray("questions") ?: JSONArray()
        for (i in 0 until updates.length()) {
            val u = updates.optJSONObject(i) ?: continue
            val id = c.questionIds.getOrNull(u.optInt("index", -1)) ?: continue
            val current = byId[id] ?: continue
            val q = u.optJSONObject("question") ?: continue
            val statement = q.text("statement") ?: continue
            val options = q.optJSONArray("options") ?: continue
            val newOptions = (0 until options.length()).mapNotNull { k -> options.optJSONObject(k) }.mapIndexed { position, o ->
                QuestionOptionEntity(questionId = id, key = o.text("key")?.uppercase() ?: ('A' + position).toString(), text = o.optString("text").trim(), isCorrect = o.optBoolean("correct"), position = position)
            }
            if (newOptions.size < 2 || newOptions.count { it.isCorrect } != 1) continue
            val explanation = q.optString("explanation").trim()
            val old = current.options.sortedBy { it.position }.map { Triple(it.key, it.text, it.isCorrect) }
            if (current.question.statement == statement && current.question.explanation == explanation && old == newOptions.map { Triple(it.key, it.text, it.isCorrect) }) continue
            dao.updateQuestion(current.question.copy(statement = statement, explanation = explanation, normalizedHash = normalizedQuestionHash(statement)))
            dao.deleteOptionsFor(id)
            dao.insertOptions(newOptions)
            fixed++
        }
        if (fixed > 0) parts += if (fixed == 1) "1 questão corrigida" else "$fixed questões corrigidas"
        setStamp(app, c.topicId, stamp, theoryNow)
        return parts
    }
}
