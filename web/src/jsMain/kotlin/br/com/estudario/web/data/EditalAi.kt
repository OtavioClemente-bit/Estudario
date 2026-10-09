package br.com.estudario.web.data

import br.com.estudario.web.Turnstile
import br.com.estudario.web.WebConfig
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.await
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array
import org.w3c.files.File
import kotlin.js.Date
import kotlin.js.Promise
import kotlin.js.json
import kotlin.random.Random

/**
 * "Montar edital com IA" no site: o texto do PDF é lido no navegador (pdf.js), igual ao app lê no
 * celular, e vai junto do PDF para a IA do Estudário (SYLLABUS_GENERATION). A resposta vira
 * matérias e tópicos do concurso.
 */
object EditalAi {
    private const val PDFJS = "https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.min.js"
    private const val WORKER = "https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.worker.min.js"
    private const val PATH = "/functions/v1/ai-syllabus-jobs"
    const val MAX_BYTES = 50 * 1024 * 1024

    data class Options(val competitionName: String, val role: String, val board: String, val year: String)

    sealed interface Step {
        data object Reading : Step
        data object Uploading : Step
        data object Processing : Step
    }

    private suspend fun ensurePdfJs(): dynamic {
        val existing = window.asDynamic().pdfjsLib
        if (existing != null && existing != undefined) return existing
        val loaded = CompletableDeferred<Unit>()
        val script = document.createElement("script")
        script.setAttribute("src", PDFJS)
        script.addEventListener("load", { loaded.complete(Unit) })
        script.addEventListener("error", { loaded.completeExceptionally(IllegalStateException("Não deu para carregar o leitor de PDF.")) })
        document.head?.appendChild(script)
        withTimeout(30_000) { loaded.await() }
        val lib = window.asDynamic().pdfjsLib
        lib.GlobalWorkerOptions.workerSrc = WORKER
        return lib
    }

    private suspend fun sha256(buffer: ArrayBuffer): String {
        val digest = (window.asDynamic().crypto.subtle.digest("SHA-256", buffer) as Promise<ArrayBuffer>).await()
        val bytes = Uint8Array(digest)
        return (0 until bytes.length).joinToString("") { (bytes.asDynamic()[it] as Int).toString(16).padStart(2, '0') }
    }

    data class PdfText(val text: String, val totalPages: Int)

    private suspend fun readText(buffer: ArrayBuffer): PdfText {
        val lib = ensurePdfJs()
        val pdf = (lib.getDocument(json("data" to Uint8Array(buffer.slice(0)))).promise as Promise<dynamic>).await()
        val total = pdf.numPages as Int
        val out = StringBuilder()
        for (page in 1..total) {
            val content = ((pdf.getPage(page) as Promise<dynamic>).await().getTextContent() as Promise<dynamic>).await()
            val items = content.items as Array<dynamic>
            var lastY: Double? = null
            items.forEach { item ->
                val y = (item.transform as Array<Double>)[5]
                if (lastY != null && kotlin.math.abs(y - lastY!!) > 2) out.append('\n') else if (out.isNotEmpty()) out.append(' ')
                out.append(item.str as String)
                lastY = y
            }
            out.append("\n\n")
            if (out.length > 440_000) break
        }
        return PdfText(out.toString().replace(Regex("[ \\t]+"), " ").trim().take(440_000), total)
    }

    private suspend fun headers(key: String): Map<String, String> {
        val token = Auth.accessToken() ?: throw AiJobException("AUTH_REQUIRED", AiJobs.message("AUTH_REQUIRED"))
        return mapOf(
            "Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json", "Content-Type" to "application/json",
            "Idempotency-Key" to key, "x-estudario-client" to "web", "x-estudario-device" to AiJobs.browserHash(), "x-turnstile-token" to Turnstile.token(),
        )
    }

    private fun fail(response: HttpResponse): Nothing {
        val code = runCatching { (snapshotJson.parseToJsonElement(response.body).jsonObject["error"] as JsonObject).str("code") }.getOrNull() ?: if (response.status == 0) "NETWORK" else "REMOTE"
        throw AiJobException(code, when (code) {
            "SOURCE_TOO_LARGE", "PDF_TOO_LARGE" -> "O PDF é grande demais (máximo de 50 MB)."
            "SOURCE_INVALID", "PDF_INVALID" -> "Não consegui abrir este PDF. Confira se é o edital em PDF de texto."
            else -> AiJobs.message(code)
        })
    }

    private const val BUCKET = "ai-syllabus-sources"

    /** Cancela um pedido que não chegou a usar a IA: o servidor devolve a cota reservada. */
    private suspend fun releaseQuota(jobId: String) {
        val token = Auth.accessToken() ?: return
        runCatching { httpRequest("POST", "${WebConfig.SUPABASE_URL}/functions/v1/ai-syllabus-cancel/$jobId", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY)) }
    }

    /** Gera a proposta de edital; devolve o JSON da proposta. */
    suspend fun generate(file: File, options: Options, onStep: (Step) -> Unit): JsonObject {
        if (file.size.toInt() > MAX_BYTES) throw AiJobException("SOURCE_TOO_LARGE", "O PDF é grande demais (máximo de 50 MB).")
        onStep(Step.Reading)
        val buffer = (file.asDynamic().arrayBuffer() as Promise<ArrayBuffer>).await()
        val hash = sha256(buffer)
        val text = runCatching { readText(buffer) }.getOrNull()
        // Barreira sem IA: PDF sem cara de edital (boletim, apostila) não chega a gastar cota.
        if (text != null && !br.com.estudario.domain.ai.EditalGuard.check(text.text).ok) throw AiJobException("NOT_AN_EDITAL", br.com.estudario.domain.ai.EditalGuard.MESSAGE)
        val key = "web-edital-" + Random.nextBytes(12).joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
        val source = buildJsonObject {
            put("fileName", file.name.take(200)); put("mimeType", "application/pdf"); put("sourceHash", hash); put("sourceBytes", buffer.byteLength)
        }
        fun body(ready: Boolean, objectPath: String?) = buildJsonObject {
            put("feature", "SYLLABUS_GENERATION")
            put("source", if (!ready) source else JsonObject(source + mapOf("objectPath" to JsonPrimitive(objectPath), "ready" to JsonPrimitive(true))))
            if (text != null && text.text.length > 200) put("sourceText", buildJsonObject {
                put("text", text.text); put("pages", "1-${text.totalPages}"); put("totalPages", text.totalPages); put("focused", false)
            })
            put("options", buildJsonObject {
                put("competitionName", options.competitionName.trim()); put("role", options.role.trim()); put("board", options.board.trim()); put("year", options.year.trim())
                put("scope", "FULL"); put("detail", "DIDACTIC"); put("includeDescriptions", false)
            })
        }.toString()

        onStep(Step.Uploading)
        val created = httpRequest("POST", "${WebConfig.SUPABASE_URL}$PATH", headers(key), body(false, null))
        if (!created.ok) fail(created)
        val createdJson = snapshotJson.parseToJsonElement(created.body).jsonObject
        val jobId = createdJson.str("jobId") ?: fail(created)
        val uploadPath = createdJson.str("uploadPath") ?: fail(created)
        // Se o PDF não chegar ao servidor, o pedido é cancelado: a cota reservada volta para a pessoa.
        if ((createdJson["sourceBound"] as? JsonPrimitive)?.contentOrNull != "true") try {
            // O caminho do arquivo vem sem o bucket; sem ele o Storage recusava todo envio.
            val target = createdJson.str("uploadUrl") ?: "${WebConfig.SUPABASE_URL}/storage/v1/object/$BUCKET/${uploadPath.split('/').joinToString("/") { js("encodeURIComponent")(it) as String }}"
            val token = Auth.accessToken() ?: throw AiJobException("AUTH_REQUIRED", AiJobs.message("AUTH_REQUIRED"))
            val upload = window.fetch(
                target,
                org.w3c.fetch.RequestInit(
                    method = "POST",
                    headers = json("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Content-Type" to "application/pdf", "x-upsert" to "true", "x-source-sha256" to hash),
                    body = Int8Array(buffer),
                ),
            ).await()
            if (upload.status.toInt() !in 200..299) throw AiJobException("UPLOAD", "Não deu para enviar o PDF. Tente de novo.")
            val bound = httpRequest("POST", "${WebConfig.SUPABASE_URL}$PATH", headers(key), body(true, uploadPath))
            if (!bound.ok) fail(bound)
        } catch (error: Throwable) {
            releaseQuota(jobId)
            throw error
        }
        onStep(Step.Processing)
        val token = Auth.accessToken() ?: throw AiJobException("AUTH_REQUIRED", AiJobs.message("AUTH_REQUIRED"))
        httpRequest("POST", "${WebConfig.SUPABASE_URL}$PATH/$jobId/process", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY))
        val deadline = Date.now() + 15 * 60_000
        while (Date.now() < deadline) {
            delay(5_000)
            val status = AiJobs.status(jobId) ?: continue
            when (status.status) {
                "SUCCEEDED" -> return status.proposal ?: throw AiJobException("EMPTY", "O edital ficou pronto, mas não chegou aqui. Tente de novo.")
                "FAILED", "EXPIRED", "CANCELLED" -> throw AiJobException(status.errorCode ?: "FAILED", if (status.errorCode == "NOT_AN_EDITAL") "O Estudário leu o PDF e não achou conteúdo programático de concurso. Sua cota foi devolvida." else "Não consegui montar um edital confiável a partir deste PDF. Sua cota não foi usada.")
            }
        }
        throw AiJobException("TIMEOUT", "Está demorando mais que o normal. A montagem continua no servidor; tente de novo em alguns minutos.")
    }

    // ---------------------------------------------------------------- aplicar

    data class TopicNode(val name: String, val children: List<TopicNode>)
    data class SubjectNode(val name: String, val topics: List<TopicNode>)

    private fun JsonElement?.text(): String = (this as? JsonPrimitive)?.contentOrNull?.trim().orEmpty()

    fun parse(proposal: JsonObject): List<SubjectNode> {
        fun topic(element: JsonElement): TopicNode? {
            val obj = element as? JsonObject ?: return null
            val name = obj["name"].text().ifBlank { obj["title"].text() }
            if (name.isBlank()) return null
            return TopicNode(name, (obj["children"] as? JsonArray).orEmpty().sortedBy { ((it as? JsonObject)?.get("position") as? JsonPrimitive)?.intOrNull ?: 0 }.mapNotNull(::topic))
        }
        return (proposal["subjects"] as? JsonArray).orEmpty().mapNotNull { element ->
            val obj = element as? JsonObject ?: return@mapNotNull null
            val name = obj["name"].text()
            if (name.isBlank()) null
            else SubjectNode(name, (obj["topics"] as? JsonArray).orEmpty().sortedBy { ((it as? JsonObject)?.get("position") as? JsonPrimitive)?.intOrNull ?: 0 }.mapNotNull(::topic))
        }.filter { it.topics.isNotEmpty() }
    }

    /** Grava o edital proposto num concurso (novo ou completando o de mesmo nome). Devolve (foto, id do concurso). */
    fun apply(data: Snapshot, competitionName: String, subjects: List<SubjectNode>): Pair<Snapshot, Long> {
        require(subjects.isNotEmpty()) { "A IA não encontrou matérias neste PDF." }
        val at = Date.now().toLong()
        val name = competitionName.trim().ifBlank { "Meu concurso" }.take(120)
        var next = data
        val existing = data.competitions.firstOrNull { it.name.equals(name, true) }
        val competitionId = existing?.id ?: next.nextId(Keys.COMPETITIONS).also { id ->
            next = next.edit(Keys.COMPETITIONS) { it.with("primary" to JsonPrimitive(false)) }
                .append(Keys.COMPETITIONS, jsonOf("id" to id, "name" to name, "primary" to true, "createdAt" to at, "externalId" to null))
        }
        var subjectId = next.nextId(Keys.SUBJECTS)
        var topicId = next.nextId(Keys.TOPICS)
        val newSubjects = mutableListOf<JsonObject>()
        val newTopics = mutableListOf<JsonObject>()
        val current = next.subjects.filter { it.competitionId == competitionId }
        fun addTopics(sid: Long, nodes: List<TopicNode>, parent: Long?, origin: String) {
            nodes.forEachIndexed { index, node ->
                val id = topicId++
                newTopics += jsonOf(
                    "id" to id, "subjectId" to sid, "parentTopicId" to parent, "title" to node.name.take(500), "description" to "", "position" to index,
                    "status" to "NAO_ESTUDADO", "firstStudiedAt" to null, "lastStudiedAt" to null, "lastReviewedAt" to null, "notes" to "", "priority" to "NORMAL",
                    "externalId" to null, "contentOriginType" to origin, "scopeCovers" to null, "scopeExcludes" to null,
                )
                addTopics(sid, node.children, id, "DIDACTIC_SUBDIVISION")
            }
        }
        subjects.forEachIndexed { index, subject ->
            val sid = current.firstOrNull { it.name.equals(subject.name, true) }?.id
                ?: subjectId++.also { newSubjects += jsonOf("id" to it, "competitionId" to competitionId, "name" to subject.name.take(300), "position" to current.size + index, "externalId" to null) }
            val known = next.topics.filter { it.subjectId == sid }.mapTo(hashSetOf()) { it.title.lowercase() }
            addTopics(sid, subject.topics.filter { it.name.lowercase() !in known }, null, "EDITAL")
        }
        next = next.edit(Keys.COMPETITIONS) { it.with("primary" to JsonPrimitive((it["id"] as? JsonPrimitive)?.content == competitionId.toString())) }
        return next.appendAll(mapOf(Keys.SUBJECTS to newSubjects, Keys.TOPICS to newTopics)) to competitionId
    }
}

private fun JsonArray?.orEmpty(): List<JsonElement> = this ?: emptyList()
