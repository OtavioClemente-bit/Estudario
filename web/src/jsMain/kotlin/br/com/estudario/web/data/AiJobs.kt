package br.com.estudario.web.data

import br.com.estudario.text.Sha256
import br.com.estudario.text.toHex
import br.com.estudario.web.Turnstile
import br.com.estudario.web.WebConfig
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import kotlin.js.Date

class AiJobException(val code: String, message: String) : Exception(message)

/** Pedidos de IA do site em ai-syllabus-jobs (criar e acompanhar um job), com as travas do app web. */
object AiJobs {
    private const val PATH = "/functions/v1/ai-syllabus-jobs"

    fun browserHash(): String = Sha256.digest("estudario-browser:${Cloud.deviceId}".encodeToByteArray()).toHex()

    /** Cria (ou reaproveita, pela mesma chave) um job; devolve o id. */
    suspend fun create(feature: String, idempotencyKey: String, input: JsonObject): String {
        val token = Auth.accessToken() ?: throw AiJobException("AUTH_REQUIRED", "Entre na sua conta para gerar com o Estudário.")
        val human = Turnstile.token()
        val body = buildJsonObject { put("feature", feature); put("input", input) }.toString()
        val response = httpRequest(
            "POST", "${WebConfig.SUPABASE_URL}$PATH",
            mapOf(
                "Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json",
                "Content-Type" to "application/json", "Idempotency-Key" to idempotencyKey, "x-estudario-client" to "web",
                "x-estudario-device" to browserHash(), "x-turnstile-token" to human,
            ),
            body,
        )
        if (!response.ok) {
            val code = runCatching { (snapshotJson.parseToJsonElement(response.body).jsonObject["error"] as JsonObject).str("code") }.getOrNull() ?: if (response.status == 0) "NETWORK" else "REMOTE"
            throw AiJobException(code, message(code))
        }
        return (snapshotJson.parseToJsonElement(response.body).jsonObject["jobId"] as JsonPrimitive).content
    }

    data class Status(val status: String, val proposal: JsonObject?, val errorCode: String?)

    suspend fun status(jobId: String): Status? {
        val token = Auth.accessToken() ?: throw AiJobException("AUTH_REQUIRED", "Sua sessão expirou. Entre de novo.")
        val response = httpRequest("GET", "${WebConfig.SUPABASE_URL}$PATH/$jobId", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json"))
        if (!response.ok) return null
        val job = runCatching { snapshotJson.parseToJsonElement(response.body).jsonObject }.getOrNull() ?: return null
        return Status(job.str("status") ?: "", job["proposal"] as? JsonObject, job.str("errorCode") ?: (job["error"] as? JsonObject)?.str("code"))
    }

    /** Espera o job terminar (ou o tempo acabar: aí continua no servidor e pode ser retomado). */
    suspend fun await(jobId: String, timeoutMillis: Long): Status? {
        val deadline = Date.now() + timeoutMillis
        while (Date.now() < deadline) {
            val current = status(jobId)
            if (current != null && current.status in setOf("SUCCEEDED", "FAILED", "EXPIRED", "CANCELLED")) return current
            delay(4_000)
        }
        return null
    }

    fun message(code: String): String = when (code) {
        "NOT_AN_EDITAL" -> br.com.estudario.domain.ai.EditalGuard.MESSAGE
        "QUOTA_EXHAUSTED", "AI_QUOTA_EXHAUSTED" -> "Você usou todas as gerações deste recurso no seu plano neste mês."
        "QUOTA_RESERVED" -> "Já tem uma geração em andamento. Espere ela terminar."
        "QUESTION_LIMIT_EXCEEDED" -> "Essa quantidade de questões passa do limite do seu plano."
        "DEVICE_QUOTA_EXHAUSTED" -> "O saldo grátis já foi usado neste navegador ou nesta rede."
        "AI_RATE_LIMIT_EXCEEDED" -> "Muitas tentativas em pouco tempo. Tente de novo em alguns minutos."
        "TURNSTILE_FAILED", "TURNSTILE_UNAVAILABLE" -> "A verificação de segurança não passou. Recarregue a página e tente de novo."
        "AUTH_REQUIRED", "AUTH_INVALID" -> "Entre na sua conta para gerar com o Estudário."
        "AI_ACCESS_DENIED", "BETA_ACCESS_REQUIRED" -> "Sua conta ainda não tem acesso à geração pelo Estudário."
        "NETWORK" -> "Não foi possível falar com o Estudário agora. Confira a internet."
        else -> "Não deu para gerar agora. Tente de novo."
    }
}
