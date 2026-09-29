package br.com.estudario.data.ai

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.data.remote.AiAccessTokenProvider
import java.nio.charset.StandardCharsets
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Jobs de texto da IA do Estudário: conteúdo de um tópico e plano de estudo. Mesma função e mesmo
 * login do edital; a diferença é que não há PDF, o app manda só os dados do tópico ou do plano.
 */
data class AiTextJobView(
    val jobId: String,
    val status: AiJobStatus,
    val proposal: JsonObject?,
    val errorCode: String?,
)

class AiTextJobClient(
    private val publishableKey: String,
    private val accessTokenProvider: AiAccessTokenProvider,
    private val transport: AiHttpTransport,
    private val httpTimeoutMillis: Long = 30_000,
) {
    suspend fun create(feature: AiFeature, idempotencyKey: String, input: JsonObject): AiTextJobView {
        val body = buildJsonObject {
            put("feature", feature.name)
            put("input", input)
        }.toString().toByteArray(StandardCharsets.UTF_8)
        val response = execute(
            AiHttpRequest("POST", JOBS_PATH, headers(idempotencyKey) + ("Content-Type" to JSON), body),
            setOf(200, 201),
        )
        val json = parse(response.body)
        return AiTextJobView(json.string("jobId"), json.status(), null, null)
    }

    suspend fun get(jobId: String): AiTextJobView {
        val response = execute(AiHttpRequest("GET", "$JOBS_PATH/${jobId.urlSafe()}", headers()), setOf(200))
        val json = parse(response.body)
        return AiTextJobView(
            jobId = json.string("jobId"),
            status = json.status(),
            proposal = (json["proposal"] as? JsonObject),
            errorCode = json["errorCode"]?.jsonPrimitive?.contentOrNull,
        )
    }

    /**
     * Espera o job terminar. O conteúdo com pesquisa na web leva alguns minutos; o intervalo cresce
     * até 10 s para não martelar o servidor. Estourar o tempo não perde nada: o job continua e pode
     * ser retomado pelo mesmo id.
     */
    suspend fun await(jobId: String, timeoutMillis: Long = 12 * 60_000L, onPoll: (AiTextJobView) -> Unit = {}): AiTextJobView {
        val deadline = System.currentTimeMillis() + timeoutMillis
        var wait = 2_000L
        while (true) {
            val job = get(jobId)
            onPoll(job)
            if (job.status in TERMINAL) return job
            if (System.currentTimeMillis() + wait > deadline) throw AiProcessTimeoutException(jobId)
            delay(wait)
            wait = (wait * 3 / 2).coerceAtMost(10_000L)
        }
    }

    private suspend fun execute(request: AiHttpRequest, accepted: Set<Int>): AiHttpResponse {
        val response = try {
            withTimeout(httpTimeoutMillis) { transport.execute(request) }
        } catch (_: TimeoutCancellationException) {
            throw AiApiException("HTTP_TIMEOUT", 504)
        } catch (error: CancellationException) {
            throw error
        } catch (_: Throwable) {
            throw AiApiException("NETWORK_UNAVAILABLE", 503)
        }
        if (response.status !in accepted) {
            val code = runCatching { parse(response.body)["error"]?.jsonObject?.get("code")?.jsonPrimitive?.contentOrNull }.getOrNull()
            throw AiApiException(code ?: "AI_API_ERROR", response.status)
        }
        return response
    }

    private fun headers(idempotencyKey: String? = null): Map<String, String> {
        val token = accessTokenProvider.accessToken() ?: throw AiAuthenticationRequiredException()
        return buildMap {
            put("Authorization", "Bearer $token")
            if (publishableKey.isNotBlank()) put("apikey", publishableKey)
            put("Accept", JSON)
            idempotencyKey?.let { put("Idempotency-Key", it) }
        }
    }

    private fun parse(body: String): JsonObject = runCatching { json.parseToJsonElement(body).jsonObject }
        .getOrElse { throw AiApiException("INVALID_RESPONSE", 502) }

    private fun JsonObject.string(name: String): String = this[name]?.jsonPrimitive?.contentOrNull?.takeIf(String::isNotBlank)
        ?: throw AiApiException("INVALID_RESPONSE", 502)

    private fun JsonObject.status(): AiJobStatus = runCatching { AiJobStatus.valueOf(string("status").uppercase(Locale.US)) }
        .getOrElse { throw AiApiException("INVALID_RESPONSE", 502) }

    private fun String.urlSafe(): String = java.net.URLEncoder.encode(this, StandardCharsets.UTF_8).replace("+", "%20")

    private companion object {
        const val JOBS_PATH = "/functions/v1/ai-syllabus-jobs"
        const val JSON = "application/json; charset=utf-8"
        val TERMINAL = setOf(AiJobStatus.SUCCEEDED, AiJobStatus.FAILED, AiJobStatus.EXPIRED, AiJobStatus.CANCELLED)
        val json = Json { ignoreUnknownKeys = true }
    }
}

/** Entrada do conteúdo de um tópico, no formato validado pelo servidor. */
object TopicContentAiInput {
    fun build(competition: CompetitionEntity, role: String?, subject: SubjectEntity, topic: TopicEntity, allTopics: List<TopicEntity>): JsonObject {
        val path = mutableListOf(topic.title)
        var current = topic
        while (true) {
            current = allTopics.firstOrNull { it.id == current.parentTopicId } ?: break
            path.add(0, current.title)
        }
        return buildJsonObject {
            put("competitionName", competition.name.take(300))
            role?.takeIf(String::isNotBlank)?.let { put("role", it.take(300)) }
            put("subjectName", subject.name.take(300))
            put("topicPath", buildJsonArray { path.takeLast(6).forEach { add(JsonPrimitive(it.take(800))) } })
            topic.scopeCovers?.takeIf(String::isNotBlank)?.let { put("scopeCovers", it.take(800)) }
            topic.scopeExcludes?.takeIf(String::isNotBlank)?.let { put("scopeExcludes", it.take(800)) }
        }
    }
}

/**
 * Converte o conteúdo gerado pelo servidor num pacote .estudo (version 2) com os mesmos ids e a
 * mesma estrutura do prompt externo. Assim ele passa pelo importador de sempre, com prévia e
 * confirmação, em vez de ganhar um caminho de gravação próprio.
 */
object TopicContentEstudoMapper {
    fun toEstudo(
        competition: CompetitionEntity,
        subject: SubjectEntity,
        topics: List<TopicEntity>,
        target: TopicEntity,
        proposal: JsonObject,
    ): String {
        val chain = generateSequence(target) { child -> topics.firstOrNull { it.id == child.parentTopicId } }.toList().reversed()
        val topicId = PromptIds.topic(target)
        val packageId = "conteudo-${PromptIds.slug(PromptIds.subject(subject))}-${PromptIds.slug(topicId)}-ia-${System.currentTimeMillis()}"

        fun node(index: Int): JsonObject {
            val topic = chain[index]
            val isTarget = index == chain.lastIndex
            return buildJsonObject {
                put("id", PromptIds.topic(topic))
                put("titulo", topic.title)
                put("ordem", topic.position)
                put("prioridade", topic.priority.name)
                put("contentOriginType", topic.contentOriginType.name)
                if (isTarget) addContent(topicId, proposal)
                put("subtopicos", if (isTarget) JsonArray(emptyList()) else buildJsonArray { add(node(index + 1)) })
            }
        }

        return buildJsonObject {
            put("version", 2)
            put("packageId", packageId)
            put("concurso", buildJsonObject {
                put("id", PromptIds.competition(competition))
                put("nome", competition.name)
            })
            put("materias", buildJsonArray {
                add(buildJsonObject {
                    put("id", PromptIds.subject(subject))
                    put("nome", subject.name)
                    put("ordem", subject.position)
                    put("topicos", buildJsonArray { add(node(0)) })
                })
            })
        }.toString()
    }

    private fun kotlinx.serialization.json.JsonObjectBuilder.addContent(id: String, p: JsonObject) {
        val chapters = p.array("chapters")
        put("teorias", buildJsonArray {
            add(buildJsonObject {
                put("id", "$id-teoria-ia")
                put("titulo", p.text("theoryTitle"))
                put("capitulos", buildJsonArray {
                    chapters.forEachIndexed { index, chapter ->
                        add(buildJsonObject {
                            put("id", "$id-ia-cap-${(index + 1).toString().padStart(2, '0')}")
                            put("titulo", chapter.text("title"))
                            put("markdown", chapter.text("markdown"))
                        })
                    }
                })
            })
        })
        put("summary", p.text("summary"))
        put("quickReview", p.text("quickReview"))
        put("tips", p.strings("tips"))
        put("traps", p.strings("traps"))
        // Objetos { question, answer }: o importador lê a pergunta e a resposta.
        put("activeRecall", p["activeRecall"] ?: JsonArray(emptyList()))
        put("errorConcepts", buildJsonArray {
            p.array("errorConcepts").forEach { concept ->
                add(buildJsonObject {
                    put("id", "$id-ia-erro-${concept.text("key")}")
                    put("title", concept.text("title"))
                    put("summary", concept.text("summary"))
                })
            }
        })
        put("questoes", buildJsonArray {
            p.array("questions").forEachIndexed { index, question ->
                val real = question.text("sourceType") == "REAL"
                add(buildJsonObject {
                    put("id", "$id-ia-q-${(index + 1).toString().padStart(2, '0')}")
                    put("questionSourceType", if (real) "REAL" else "AUTHORIAL")
                    put("banca", question.nullableText("board") ?: "")
                    put("orgao", question.nullableText("agency") ?: "")
                    put("ano", question["year"]?.jsonPrimitive?.intOrNull ?: 0)
                    put("origem", if (real) "Prova" else "")
                    put("sourceId", JsonNull)
                    put("sourceUrl", question.nullableText("sourceUrl")?.let(::JsonPrimitive) ?: JsonNull)
                    put("enunciado", question.text("statement"))
                    put("dificuldade", question.text("difficulty"))
                    put("tags", JsonArray(emptyList()))
                    put("secao", question.text("section"))
                    put("conceitoErro", "$id-ia-erro-${question.text("errorConceptKey")}")
                    put("alternativas", buildJsonArray {
                        question.array("options").forEach { option ->
                            add(buildJsonObject {
                                put("chave", option.text("key"))
                                put("texto", option.text("text"))
                                put("correta", option["correct"]?.jsonPrimitive?.contentOrNull == "true")
                            })
                        }
                    })
                    put("explicacao", question.text("explanation"))
                })
            }
        })
        val scope = p["scope"]?.jsonObject
        put("escopo", buildJsonObject {
            put("cobre", scope?.text("covers") ?: "")
            put("naoCobre", scope?.nullableText("excludes") ?: "")
        })
        put("fontes", buildJsonArray {
            p.array("sources").forEachIndexed { index, source ->
                add(buildJsonObject {
                    put("id", "$id-ia-fonte-${index + 1}")
                    put("tipo", source.text("kind"))
                    put("titulo", source.text("title"))
                    put("publicador", source.text("publisher"))
                    put("referencia", source.nullableText("reference") ?: "")
                    put("url", source.text("url"))
                    put("acessadoEm", source.text("accessedAt"))
                })
            }
        })
    }

    private fun JsonObject.array(name: String): List<JsonObject> = (this[name] as? JsonArray)?.map { it.jsonObject }.orEmpty()
    private fun JsonObject.text(name: String): String = this[name]?.jsonPrimitive?.contentOrNull.orEmpty()
    private fun JsonObject.nullableText(name: String): String? = (this[name] as? JsonPrimitive)?.contentOrNull?.takeIf(String::isNotBlank)
    private fun JsonObject.strings(name: String): JsonElement = JsonArray((this[name] as? JsonArray)?.map { JsonPrimitive(it.jsonPrimitive.content) }.orEmpty())
}
