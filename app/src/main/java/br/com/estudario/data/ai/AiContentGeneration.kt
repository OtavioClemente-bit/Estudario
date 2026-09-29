package br.com.estudario.data.ai

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.prompt.ContentBlock
import br.com.estudario.data.prompt.ContentPromptOptions
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.data.prompt.QuestionDifficulty
import br.com.estudario.data.remote.SupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseClientConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

/** Andamento de uma geração de conteúdo pela IA do Estudário, para a tela acompanhar. */
sealed interface AiContentProgress {
    data object Sending : AiContentProgress
    data object Processing : AiContentProgress
    /** [estudo] é um pacote .estudo pronto para a pré-visualização de importação do app. */
    data class Done(val estudo: String) : AiContentProgress
    data class Failed(val message: String) : AiContentProgress
}

/**
 * Conteúdo de um tópico gerado no servidor (ai-syllabus-jobs, recurso CONTENT_GENERATION).
 *
 * O resultado vira um pacote .estudo com os mesmos IDs que o pedido para outras IAs usaria, então a
 * importação cai no tópico certo e passa pela mesma pré-visualização antes de salvar.
 */
class AiContentGenerator(
    private val config: SupabaseClientConfig,
    private val authRepository: SupabaseAuthRepository,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransport(config.projectUrl),
    private val pollIntervalMillis: Long = 4_000,
    private val maxWaitMillis: Long = 8 * 60_000,
) {
    fun generate(
        competition: CompetitionEntity,
        subject: SubjectEntity,
        topics: List<TopicEntity>,
        target: TopicEntity,
        options: ContentPromptOptions,
    ): Flow<AiContentProgress> = flow {
        emit(AiContentProgress.Sending)
        if (!config.isConfigured) return@flow emit(AiContentProgress.Failed("A IA do Estudário não está disponível nesta versão do app."))
        val token = authRepository.accessToken()
            ?: return@flow emit(AiContentProgress.Failed("Entre na sua conta para usar a IA do Estudário."))
        val body = JSONObject()
            .put("feature", "CONTENT_GENERATION")
            .put("input", AiContentRequest.input(competition, subject, topics, target, options))
            .toString()
        val created = try {
            withTimeout(REQUEST_TIMEOUT_MILLIS) {
                transport.execute(request("POST", PATH, token, body, idempotencyKey = UUID.randomUUID().toString()))
            }
        } catch (error: CancellationException) {
            if (error is kotlinx.coroutines.TimeoutCancellationException) return@flow emit(AiContentProgress.Failed(NETWORK)) else throw error
        } catch (_: Throwable) {
            return@flow emit(AiContentProgress.Failed(NETWORK))
        }
        if (created.status !in 200..299) return@flow emit(AiContentProgress.Failed(errorMessage(created)))
        val jobId = runCatching { JSONObject(created.body).getString("jobId") }.getOrNull()
            ?: return@flow emit(AiContentProgress.Failed(NETWORK))

        emit(AiContentProgress.Processing)
        val deadline = System.currentTimeMillis() + maxWaitMillis
        while (System.currentTimeMillis() < deadline) {
            delay(pollIntervalMillis)
            val response = runCatching {
                withTimeout(REQUEST_TIMEOUT_MILLIS) { transport.execute(request("GET", "$PATH/$jobId", token)) }
            }.getOrElse { error ->
                if (error is CancellationException && error !is kotlinx.coroutines.TimeoutCancellationException) throw error
                null
            } ?: continue
            if (response.status !in 200..299) continue
            val job = runCatching { JSONObject(response.body) }.getOrNull() ?: continue
            when (job.optString("status")) {
                "SUCCEEDED" -> {
                    val proposal = job.optJSONObject("proposal")
                        ?: return@flow emit(AiContentProgress.Failed("A IA terminou, mas o conteúdo não chegou. Tente de novo."))
                    return@flow emit(AiContentProgress.Done(AiContentEstudo.build(competition, subject, topics, target, proposal)))
                }
                "FAILED", "EXPIRED", "CANCELLED" -> return@flow emit(AiContentProgress.Failed(
                    "Não consegui gerar um material confiável desta vez. Sua cota não foi usada; tente de novo ou use outra IA.",
                ))
            }
        }
        emit(AiContentProgress.Failed("A geração está demorando mais que o normal. Ela continua no servidor; tente abrir de novo em alguns minutos."))
    }

    private fun request(method: String, path: String, token: String, body: String? = null, idempotencyKey: String? = null) = AiHttpRequest(
        method = method,
        path = path,
        headers = buildMap {
            put("Authorization", "Bearer $token")
            put("apikey", config.publishableKey)
            put("Accept", "application/json")
            if (idempotencyKey != null) put("Idempotency-Key", idempotencyKey)
            if (body != null) put("Content-Type", "application/json; charset=utf-8")
        },
        body = body?.toByteArray(Charsets.UTF_8),
    )

    private fun errorMessage(response: AiHttpResponse): String {
        val code = runCatching { JSONObject(response.body).getJSONObject("error").getString("code") }.getOrNull()
        return when (code) {
            "QUOTA_EXHAUSTED", "AI_QUOTA_EXHAUSTED" -> "Você usou todas as gerações de conteúdo do seu plano neste mês. Veja em Perfil > Planos e uso."
            "QUOTA_RESERVED" -> "Já tem uma geração em andamento. Espere ela terminar."
            "QUESTION_LIMIT_EXCEEDED" -> "Essa quantidade de questões passa do limite do seu plano."
            "AI_RATE_LIMIT_EXCEEDED" -> "Muitas tentativas em pouco tempo. Tente de novo em alguns minutos."
            "AUTH_REQUIRED", "AUTH_INVALID" -> "Entre na sua conta para usar a IA do Estudário."
            "AI_ACCESS_DENIED", "BETA_ACCESS_REQUIRED" -> "Sua conta ainda não tem acesso à IA do Estudário."
            else -> NETWORK
        }
    }

    private companion object {
        const val PATH = "/functions/v1/ai-syllabus-jobs"
        const val REQUEST_TIMEOUT_MILLIS = 30_000L
        const val NETWORK = "Não foi possível falar com a IA do Estudário agora. Confira a internet e tente de novo."
    }
}

/** Entrada do pedido, no formato que o servidor valida (text-job-input.ts). */
internal object AiContentRequest {
    fun input(competition: CompetitionEntity, subject: SubjectEntity, topics: List<TopicEntity>, target: TopicEntity, options: ContentPromptOptions): JSONObject {
        val blocks = options.blocks.ifEmpty { ContentBlock.entries.toSet() }
        return JSONObject()
            .put("competitionName", competition.name.take(300))
            .put("board", options.board.trim().takeIf(String::isNotBlank)?.take(80) ?: JSONObject.NULL)
            // O órgão decide qual estatuto vale; sem resposta da pessoa, vale o próprio concurso.
            .put("agency", options.agency.trim().ifBlank { competition.name }.take(160))
            .put("subjectName", subject.name.take(300))
            .put("topicPath", JSONArray(pathOf(target, topics).map { it.take(800) }))
            .put("options", JSONObject()
                .put("blocks", JSONArray(ContentBlock.entries.filter { it in blocks }.map { it.name }))
                .put("depth", when (options.depth.name) { "ESSENTIAL" -> "ESSENTIAL"; "BOOK" -> "BOOK"; else -> "DEEP" })
                .put("questionCount", if (ContentBlock.QUESTIONS in blocks) options.questionCount else 0)
                .put("questionStyle", options.style.name)
                .put("difficulty", when (options.difficulty) {
                    QuestionDifficulty.EASY -> "EASY"
                    QuestionDifficulty.MEDIUM -> "MEDIUM"
                    QuestionDifficulty.HARD -> "HARD"
                    QuestionDifficulty.MIXED -> "MIXED"
                }))
    }

    /** Da raiz até o tópico (no máximo 6 níveis, como o servidor aceita). */
    fun pathOf(target: TopicEntity, topics: List<TopicEntity>): List<String> {
        val path = ArrayDeque<String>()
        var current: TopicEntity? = target
        val seen = HashSet<Long>()
        while (current != null && seen.add(current.id)) {
            path.addFirst(current.title)
            current = topics.firstOrNull { it.id == current!!.parentTopicId }
        }
        return path.toList().takeLast(6)
    }
}

/** Converte o resultado validado do servidor num pacote .estudo (versão 2, hierárquico). */
internal object AiContentEstudo {
    fun build(competition: CompetitionEntity, subject: SubjectEntity, topics: List<TopicEntity>, target: TopicEntity, proposal: JSONObject): String {
        val chain = generateSequence(target) { topic -> topics.firstOrNull { it.id == topic.parentTopicId } }.toList().reversed()
        val targetId = PromptIds.topic(target)
        var node: JSONObject? = null
        for (topic in chain.reversed()) {
            val json = JSONObject()
                .put("id", PromptIds.topic(topic))
                .put("titulo", topic.title)
                .put("ordem", topic.position)
                .put("prioridade", topic.priority.name)
                .put("contentOriginType", topic.contentOriginType.name)
                .put("subtopicos", JSONArray().apply { node?.let(::put) })
            if (topic.id == target.id) fillContent(json, targetId, proposal)
            node = json
        }
        return JSONObject()
            .put("version", 2)
            .put("packageId", "ia-estudario-${PromptIds.slug(targetId)}-${UUID.randomUUID().toString().take(8)}")
            .put("concurso", JSONObject().put("id", PromptIds.competition(competition)).put("nome", competition.name))
            .put("materias", JSONArray().put(
                JSONObject()
                    .put("id", PromptIds.subject(subject))
                    .put("nome", subject.name)
                    .put("ordem", subject.position)
                    .put("topicos", JSONArray().put(node)),
            ))
            .put("warnings", proposal.optJSONArray("warnings") ?: JSONArray())
            .toString()
    }

    private fun fillContent(topic: JSONObject, id: String, proposal: JSONObject) {
        val chapters = proposal.optJSONArray("chapters") ?: JSONArray()
        if (chapters.length() > 0) {
            val capitulos = JSONArray()
            for (index in 0 until chapters.length()) {
                val chapter = chapters.getJSONObject(index)
                capitulos.put(JSONObject().put("id", "$id-cap-${index + 1}").put("titulo", chapter.optString("title")).put("markdown", chapter.optString("markdown")))
            }
            topic.put("teorias", JSONArray().put(
                JSONObject().put("id", "$id-teoria").put("titulo", proposal.optString("theoryTitle").ifBlank { "Teoria" }).put("capitulos", capitulos),
            ))
        }
        proposal.optString("summary").takeIf(String::isNotBlank)?.let { topic.put("summary", it) }
        proposal.optString("quickReview").takeIf(String::isNotBlank)?.let { topic.put("quickReview", it) }
        listOf("tips", "traps", "activeRecall").forEach { key ->
            proposal.optJSONArray(key)?.takeIf { it.length() > 0 }?.let { topic.put(key, it) }
        }
        val concepts = proposal.optJSONArray("errorConcepts") ?: JSONArray()
        if (concepts.length() > 0) {
            val converted = JSONArray()
            for (index in 0 until concepts.length()) {
                val concept = concepts.getJSONObject(index)
                converted.put(JSONObject().put("id", "$id-erro-${concept.optString("key")}").put("title", concept.optString("title")).put("summary", concept.optString("summary")))
            }
            topic.put("errorConcepts", converted)
        }
        val questions = proposal.optJSONArray("questions") ?: JSONArray()
        if (questions.length() > 0) {
            val converted = JSONArray()
            for (index in 0 until questions.length()) {
                val question = questions.getJSONObject(index)
                val real = question.optString("sourceType") == "REAL"
                val options = question.getJSONArray("options")
                val alternatives = JSONArray()
                for (optionIndex in 0 until options.length()) {
                    val option = options.getJSONObject(optionIndex)
                    alternatives.put(JSONObject().put("chave", option.optString("key")).put("texto", option.optString("text")).put("correta", option.optBoolean("correct")))
                }
                converted.put(JSONObject()
                    .put("id", "$id-q-${index + 1}-${UUID.randomUUID().toString().take(6)}")
                    .put("enunciado", question.optString("statement"))
                    .put("dificuldade", question.optString("difficulty"))
                    .put("alternativas", alternatives)
                    .put("explicacao", question.optString("explanation"))
                    .put("secao", question.optString("section").takeIf { it.isNotBlank() && it != "Questões" } ?: JSONObject.NULL)
                    .put("conceitoErro", question.optString("errorConceptKey").takeIf { it.isNotBlank() && it != "null" }?.let { "$id-erro-$it" } ?: JSONObject.NULL)
                    .put("questionSourceType", if (real) "REAL" else "AUTHORIAL")
                    .put("sourceUrl", if (real) question.opt("sourceUrl") else JSONObject.NULL)
                    .put("banca", if (real) question.opt("board") else JSONObject.NULL)
                    .put("orgao", if (real) question.opt("agency") else JSONObject.NULL)
                    .put("ano", if (real) question.opt("year") else JSONObject.NULL))
            }
            topic.put("questoes", converted)
        }
        proposal.optJSONObject("scope")?.let { scope ->
            topic.put("escopo", JSONObject().put("cobre", scope.optString("covers")).put("naoCobre", scope.optString("excludes")))
        }
        val sources = proposal.optJSONArray("sources") ?: JSONArray()
        val fontes = JSONArray()
        for (index in 0 until sources.length()) {
            val source = sources.getJSONObject(index)
            fontes.put(JSONObject()
                .put("id", "$id-fonte-${index + 1}")
                .put("tipo", source.optString("kind"))
                .put("titulo", source.optString("title"))
                .put("publicador", source.optString("publisher"))
                .put("referencia", source.optString("reference"))
                .put("url", source.optString("url"))
                .put("acessadoEm", source.optString("accessedAt")))
        }
        topic.put("fontes", fontes)
    }
}
