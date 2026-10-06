package br.com.estudario.web.data

import br.com.estudario.text.Sha256
import br.com.estudario.text.stripAccents
import br.com.estudario.text.toHex
import br.com.estudario.web.Turnstile
import br.com.estudario.web.WebConfig
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.put
import kotlin.js.Date
import kotlin.random.Random

/**
 * Material de um tópico gerado pela IA do Estudário, pelo app web. O pedido é o mesmo do app
 * (AiContentGenerator, recurso CONTENT_GENERATION em ai-syllabus-jobs), com a verificação anti-robô
 * e a identificação do navegador que o servidor exige do site. O resultado é gravado com as regras
 * do importador do app para pacotes da IA (EstudoPackageService): mesmos ids externos, então uma
 * nova geração no celular ou no site substitui a anterior do mesmo tópico.
 */
object AiContent {
    data class Options(
        val blocks: Set<String> = setOf("THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS"),
        val depth: String = "BOOK",
        val questionCount: Int = 10,
        val style: String = "MIXED",
        val difficulty: String = "HARD",
        val board: String = "",
    )

    sealed interface Progress {
        data object Checking : Progress
        data object Sending : Progress
        data object Processing : Progress
        data class Done(val proposal: JsonObject) : Progress
        data class Failed(val message: String) : Progress
    }

    private const val PATH = "/functions/v1/ai-syllabus-jobs"
    private const val NETWORK = "Não foi possível falar com o Estudário agora. Confira a internet e tente de novo."

    /** Identificação deste navegador para o saldo grátis (hash do id local, nunca dados pessoais). */
    private fun browserHash(): String = Sha256.digest("estudario-browser:${Cloud.deviceId}".encodeToByteArray()).toHex()

    private fun idempotencyKey(): String = Random.nextBytes(16).toHex()

    private fun topicPath(data: Snapshot, topic: Topic): List<String> {
        val byId = data.topics.associateBy { it.id }
        val path = ArrayDeque<String>()
        var current: Topic? = topic
        val seen = HashSet<Long>()
        while (current != null && seen.add(current.id)) {
            path.addFirst(current.title.take(300))
            current = current.parentTopicId?.let { byId[it] }
        }
        return path.toList().takeLast(6)
    }

    suspend fun generate(data: Snapshot, topicId: Long, options: Options, onProgress: (Progress) -> Unit): Progress {
        val topic = data.topics.firstOrNull { it.id == topicId } ?: return Progress.Failed("Tópico não encontrado.")
        val subject = data.subjects.firstOrNull { it.id == topic.subjectId } ?: return Progress.Failed("Matéria não encontrada.")
        val competition = data.competitions.firstOrNull { it.id == subject.competitionId } ?: return Progress.Failed("Concurso não encontrado.")
        val token = Auth.accessToken() ?: return Progress.Failed("Entre na sua conta para gerar com o Estudário.")
        onProgress(Progress.Checking)
        val human = try { Turnstile.token() } catch (error: Throwable) { return Progress.Failed(error.message ?: "A verificação de segurança falhou.") }
        onProgress(Progress.Sending)
        val blocks = options.blocks
        val body = buildJsonObject {
            put("feature", "CONTENT_GENERATION")
            put("input", buildJsonObject {
                put("competitionName", competition.name.take(300))
                if (options.board.isNotBlank()) put("board", options.board.trim().take(80)) else put("board", JsonNull)
                put("agency", competition.name.take(160))
                put("subjectName", subject.name.take(300))
                put("topicPath", buildJsonArray { topicPath(data, topic).forEach { add(JsonPrimitive(it)) } })
                put("options", buildJsonObject {
                    put("blocks", buildJsonArray { listOf("THEORY", "SUMMARY", "QUICK_REVIEW", "TIPS_TRAPS", "ACTIVE_RECALL", "QUESTIONS", "ERROR_CONCEPTS").filter { it in blocks }.forEach { add(JsonPrimitive(it)) } })
                    put("depth", options.depth)
                    put("questionCount", if ("QUESTIONS" in blocks) options.questionCount else 0)
                    put("questionStyle", options.style)
                    put("difficulty", options.difficulty)
                })
            })
        }.toString()
        val headers = mapOf(
            "Authorization" to "Bearer $token",
            "apikey" to WebConfig.SUPABASE_KEY,
            "Accept" to "application/json",
            "Content-Type" to "application/json",
            "Idempotency-Key" to idempotencyKey(),
            "x-estudario-client" to "web",
            "x-estudario-device" to browserHash(),
            "x-turnstile-token" to human,
        )
        val created = httpRequest("POST", "${WebConfig.SUPABASE_URL}$PATH", headers, body)
        if (!created.ok) return Progress.Failed(errorMessage(created))
        val jobId = runCatching { (snapshotJson.parseToJsonElement(created.body).jsonObject["jobId"] as JsonPrimitive).content }.getOrNull()
            ?: return Progress.Failed(NETWORK)
        onProgress(Progress.Processing)
        val deadline = Date.now() + 8 * 60_000
        while (Date.now() < deadline) {
            delay(4_000)
            val current = Auth.accessToken() ?: return Progress.Failed("Sua sessão expirou. Entre de novo.")
            val response = httpRequest("GET", "${WebConfig.SUPABASE_URL}$PATH/$jobId", mapOf("Authorization" to "Bearer $current", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json"))
            if (!response.ok) continue
            val job = runCatching { snapshotJson.parseToJsonElement(response.body).jsonObject }.getOrNull() ?: continue
            when (job.str("status")) {
                "SUCCEEDED" -> {
                    val proposal = job["proposal"] as? JsonObject ?: return Progress.Failed("O material ficou pronto, mas não chegou aqui. Tente de novo.")
                    return Progress.Done(proposal)
                }
                "FAILED", "EXPIRED", "CANCELLED" -> return Progress.Failed("Não consegui gerar um material confiável desta vez. Sua cota não foi usada; tente de novo.")
            }
        }
        return Progress.Failed("A geração está demorando mais que o normal. Ela continua no servidor; tente de novo em alguns minutos.")
    }

    private fun errorMessage(response: HttpResponse): String {
        if (response.status == 0) return NETWORK
        val code = runCatching { (snapshotJson.parseToJsonElement(response.body).jsonObject["error"] as JsonObject).str("code") }.getOrNull()
        return when (code) {
            "QUOTA_EXHAUSTED", "AI_QUOTA_EXHAUSTED" -> "Você usou todas as gerações de conteúdo do seu plano neste mês."
            "QUOTA_RESERVED" -> "Já tem uma geração em andamento. Espere ela terminar."
            "QUESTION_LIMIT_EXCEEDED" -> "Essa quantidade de questões passa do limite do seu plano."
            "DEVICE_QUOTA_EXHAUSTED" -> "O saldo grátis de gerações já foi usado neste navegador ou nesta rede."
            "AI_RATE_LIMIT_EXCEEDED" -> "Muitas tentativas em pouco tempo. Tente de novo em alguns minutos."
            "TURNSTILE_FAILED", "TURNSTILE_UNAVAILABLE" -> "A verificação de segurança não passou. Recarregue a página e tente de novo."
            "AUTH_REQUIRED", "AUTH_INVALID" -> "Entre na sua conta para gerar com o Estudário."
            "AI_ACCESS_DENIED", "BETA_ACCESS_REQUIRED" -> "Sua conta ainda não tem acesso à geração pelo Estudário."
            else -> NETWORK
        }
    }

    // ------------------------------------------------------------------ gravação

    private fun JsonObject.long(key: String): Long? = (this[key] as? JsonPrimitive)?.longOrNull
    private fun JsonObject.int(key: String): Int? = (this[key] as? JsonPrimitive)?.intOrNull
    private fun JsonObject.bool(key: String): Boolean? = (this[key] as? JsonPrimitive)?.booleanOrNull
    private fun JsonElement?.text(): String? = (this as? JsonPrimitive)?.contentOrNull?.trim()?.takeIf { it.isNotBlank() && it != "null" }
    private fun JsonObject.arr(key: String): List<JsonElement> = (this[key] as? JsonArray).orEmpty()

    /** normalizedQuestionHash do app: sem acentos, só letras e números, SHA-256. */
    private fun questionHash(statement: String): String =
        Sha256.digest(stripAccents(statement.lowercase()).replace(Regex("[^a-z0-9]+"), " ").trim().encodeToByteArray()).toHex()

    /** Grava o resultado no tópico, como a importação de um pacote "ia-estudario-..." no app. */
    fun apply(data: Snapshot, topicId: Long, proposal: JsonObject): Snapshot {
        val topicRow = data.array(Keys.TOPICS).firstOrNull { (it as? JsonObject)?.long("id") == topicId } as? JsonObject ?: return data
        val t = topicRow.str("externalId") ?: "topico-$topicId"
        val prefix = "$t-"
        val at = Date.now().toLong()
        val packageId = "ia-estudario-${t.take(60)}-${Random.nextBytes(4).toHex()}"
        var next = data

        // Nova geração substitui a anterior do mesmo tópico; questão já respondida ou favorita só some das listas.
        val hasTheory = proposal.arr("chapters").isNotEmpty()
        if (hasTheory) {
            fun ownedByPrevious(row: JsonObject) = row.long("topicId") == topicId && row.str("externalId")?.startsWith(prefix) == true
            next = next.edit(Keys.THEORIES) { if (ownedByPrevious(it)) null else it }
                .edit(Keys.SUMMARIES) { if (ownedByPrevious(it)) null else it }
                .edit(Keys.SNIPPETS) { if (ownedByPrevious(it)) null else it }
                .edit(Keys.QUESTIONS) { row ->
                    if (!ownedByPrevious(row)) row
                    else if ((row.int("answerCount") ?: 0) > 0 || row.bool("favorite") == true) row.with("hidden" to JsonPrimitive(true))
                    else null
                }
        }
        val scope = proposal["scope"] as? JsonObject
        next = next.edit(Keys.TOPICS) { row ->
            if (row.long("id") != topicId) row
            else row.with(
                "externalId" to JsonPrimitive(t),
                "scopeCovers" to (scope?.get("covers").text()?.let { JsonPrimitive(it) } ?: row["scopeCovers"] ?: JsonNull),
                "scopeExcludes" to (scope?.get("excludes").text()?.let { JsonPrimitive(it) } ?: row["scopeExcludes"] ?: JsonNull),
            )
        }

        val theories = mutableListOf<JsonObject>()
        if (hasTheory) {
            val title = proposal["theoryTitle"].text() ?: "Teoria"
            val markdown = proposal.arr("chapters").mapIndexed { index, element ->
                val chapter = element as JsonObject
                val chapterTitle = chapter["title"].text() ?: "Capítulo ${index + 1}"
                val body = chapter["markdown"].text().orEmpty()
                val firstLine = body.trimStart().lineSequence().firstOrNull().orEmpty()
                val repeats = firstLine.startsWith("#") && firstLine.trimStart('#').trim().trim('*').trim().equals(chapterTitle.trim(), true)
                if (repeats) body.trimStart() else "## $chapterTitle\n\n$body"
            }.joinToString("\n\n")
            theories += jsonOf(
                "id" to next.nextId(Keys.THEORIES), "topicId" to topicId, "title" to title,
                "markdown" to if (markdown.startsWith("# ")) markdown else "# $title\n\n$markdown",
                "externalId" to "$t-teoria", "lastReadBlock" to 0, "createdAt" to at, "updatedAt" to at,
            )
        }

        val summaries = mutableListOf<JsonObject>()
        var summaryId = next.nextId(Keys.SUMMARIES)
        proposal["summary"].text()?.let { text ->
            summaries += jsonOf("id" to summaryId++, "topicId" to topicId, "title" to "Resumo completo", "markdown" to text, "favorite" to false, "ownNotes" to "", "externalId" to "$t-summary", "createdAt" to at, "updatedAt" to at, "kind" to "COMPLETO")
        }
        val cards = proposal.arr("flashcards").mapNotNull { it as? JsonObject }
        val quick = if (cards.isNotEmpty()) cards.joinToString("\n\n") { "### ${it["front"].text().orEmpty()}\n\n${it["back"].text().orEmpty()}" } else proposal["quickReview"].text()
        quick?.let { text ->
            summaries += jsonOf("id" to summaryId++, "topicId" to topicId, "title" to "Revisão rápida", "markdown" to text, "favorite" to false, "ownNotes" to "", "externalId" to "$t-quick", "createdAt" to at, "updatedAt" to at, "kind" to "RAPIDO")
        }

        val snippets = mutableListOf<JsonObject>()
        var snippetId = next.nextId(Keys.SNIPPETS)
        fun addSnippets(key: String, kind: String) {
            proposal.arr(key).forEachIndexed { index, element ->
                val obj = element as? JsonObject
                val text = obj?.let { it["text"].text() ?: it["question"].text() } ?: element.text() ?: return@forEachIndexed
                snippets += jsonOf(
                    "id" to snippetId++, "topicId" to topicId, "kind" to kind, "text" to text, "favorite" to false,
                    "externalId" to "$t-${kind.lowercase()}-$index", "position" to index, "createdAt" to at, "updatedAt" to at, "answer" to obj?.get("answer").text(),
                )
            }
        }
        addSnippets("tips", "BIZU")
        addSnippets("traps", "PEGADINHA")
        addSnippets("activeRecall", "RECUPERACAO")

        val concepts = mutableListOf<JsonObject>()
        var conceptId = next.nextId(Keys.ERROR_CONCEPTS)
        val existingConcepts = next.errorConcepts.mapNotNullTo(hashSetOf()) { it.externalId }
        proposal.arr("errorConcepts").mapNotNull { it as? JsonObject }.forEach { concept ->
            val external = "$t-erro-${concept["key"].text()}"
            if (external in existingConcepts) return@forEach
            concepts += jsonOf(
                "id" to conceptId++, "topicId" to topicId, "title" to (concept["title"].text() ?: return@forEach), "summary" to concept["summary"].text().orEmpty(),
                "favorite" to false, "externalId" to external, "createdAt" to at, "updatedAt" to at, "errorCount" to 0, "correctAfterErrorCount" to 0,
                "lastErrorAt" to null, "lastReviewedAt" to null, "priority" to "NORMAL", "mastered" to false,
            )
        }

        val questions = mutableListOf<JsonObject>()
        var questionId = next.nextId(Keys.QUESTIONS)
        var optionId = (next.questions.flatMap { it.options }.maxOfOrNull { it.id } ?: 0L) + 1
        val knownHashes = next.array(Keys.QUESTIONS).mapNotNullTo(hashSetOf()) { (it as? JsonObject)?.str("normalizedHash") }
        proposal.arr("questions").mapNotNull { it as? JsonObject }.forEachIndexed { index, question ->
            val statement = question["statement"].text() ?: return@forEachIndexed
            val hash = questionHash(statement)
            if (!knownHashes.add(hash)) return@forEachIndexed
            val declaredReal = question["sourceType"].text() == "REAL"
            val url = question["sourceUrl"].text()
            // Diz-se de prova real sem apontar origem: vira autoral e perde banca/órgão/ano (mesma regra do app).
            val real = declaredReal && url != null
            val options = question.arr("options").mapNotNull { it as? JsonObject }.mapIndexed { position, option ->
                jsonOf("id" to optionId++, "key" to (option["key"].text()?.uppercase() ?: ('A' + position).toString()), "text" to option["text"].text().orEmpty(), "correct" to (option.bool("correct") ?: false), "position" to position)
            }
            if (options.size < 2 || options.count { it.bool("correct") == true } != 1) return@forEachIndexed
            val difficulty = question["difficulty"].text()?.takeIf { it in setOf("FACIL", "MEDIA", "DIFICIL") }
            val conceptKey = question["errorConceptKey"].text()
            questions += jsonOf(
                "id" to questionId++, "topicId" to topicId, "externalId" to "$t-q-${index + 1}-${Random.nextBytes(3).toHex()}",
                "board" to if (real) question["board"].text() else null, "agency" to if (real) question["agency"].text() else null,
                "year" to if (real) question.int("year") else null, "difficulty" to difficulty, "source" to null, "statement" to statement,
                "explanation" to question["explanation"].text().orEmpty(), "notes" to "", "tags" to "", "importedAt" to at,
                "answerCount" to 0, "correctCount" to 0, "errorCount" to 0, "lastAnswer" to null, "lastAnsweredAt" to null, "favorite" to false,
                "questionSourceType" to if (real) "REAL" else "AUTHORIAL", "sourceId" to null, "sourceUrl" to if (real) url else null,
                "normalizedHash" to hash, "reviewAnchor" to question["section"].text()?.takeIf { it != "Questões" },
                "errorConceptExternalId" to conceptKey?.let { "$t-erro-$it" }, "hidden" to false, "simulationId" to null,
                "options" to JsonArray(options),
            )
        }

        var sourceId = next.nextId("contentSources")
        val sources = proposal.arr("sources").mapNotNull { it as? JsonObject }.mapIndexedNotNull { index, source ->
            val title = source["title"].text() ?: return@mapIndexedNotNull null
            jsonOf(
                "id" to sourceId++, "topicId" to topicId, "packageId" to packageId,
                "kind" to if (source["kind"].text()?.uppercase()?.startsWith("OFICIAL") == true) "OFICIAL" else "COMPLEMENTAR",
                "title" to title, "publisher" to source["publisher"].text().orEmpty(), "reference" to source["reference"].text().orEmpty(),
                "url" to source["url"].text(), "accessedAt" to source["accessedAt"].text().orEmpty(), "externalId" to "$t-fonte-${index + 1}", "createdAt" to at,
            )
        }

        val created = theories.size + summaries.size + snippets.size + questions.size + concepts.size
        return next.appendAll(
            mapOf(
                Keys.THEORIES to theories,
                Keys.SUMMARIES to summaries,
                Keys.SNIPPETS to snippets,
                Keys.QUESTIONS to questions,
                Keys.ERROR_CONCEPTS to concepts,
                "contentSources" to sources,
                "importPackages" to listOf(
                    jsonOf(
                        "id" to next.nextId("importPackages"), "packageId" to packageId, "schemaVersion" to 2, "importedAt" to at,
                        "contentHash" to Sha256.digest(proposal.toString().encodeToByteArray()).toHex(), "fileName" to "", "createdCount" to created,
                        "updatedCount" to 0, "ignoredCount" to 0,
                    ),
                ),
            ),
        )
    }
}
