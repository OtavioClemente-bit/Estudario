package br.com.estudario.ui.plans

import br.com.estudario.data.ai.AiAdInterestResult
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.ai.AiPlanSummary
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import br.com.estudario.data.ai.aiPlanJson
import br.com.estudario.data.remote.SupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseClientConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout

sealed interface AiPlanLoadResult {
    data class Available(val summary: AiPlanSummary) : AiPlanLoadResult
    data object SignedOut : AiPlanLoadResult
    data object Unavailable : AiPlanLoadResult
}

interface AiPlanRepository {
    suspend fun load(): AiPlanLoadResult
    suspend fun registerAdInterest(): AiAdInterestResult?
}

/** Cliente de leitura da função ai-plan. Nunca altera plano nem cota. */
class HttpAiPlanRepository(
    private val config: SupabaseClientConfig,
    private val authRepository: SupabaseAuthRepository,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransport(config.projectUrl),
) : AiPlanRepository {
    override suspend fun load(): AiPlanLoadResult {
        if (!config.isConfigured) return AiPlanLoadResult.Unavailable
        val token = authRepository.accessToken() ?: return AiPlanLoadResult.SignedOut
        return try {
            val response = withTimeout(TIMEOUT_MILLIS) { transport.execute(request("GET", PATH, token)) }
            when (response.status) {
                200 -> AiPlanLoadResult.Available(aiPlanJson.decodeFromString(AiPlanSummary.serializer(), response.body))
                401 -> AiPlanLoadResult.SignedOut
                else -> AiPlanLoadResult.Unavailable
            }
        } catch (error: CancellationException) {
            if (error is kotlinx.coroutines.TimeoutCancellationException) AiPlanLoadResult.Unavailable else throw error
        } catch (_: Throwable) {
            AiPlanLoadResult.Unavailable
        }
    }

    override suspend fun registerAdInterest(): AiAdInterestResult? {
        if (!config.isConfigured) return null
        val token = authRepository.accessToken() ?: return null
        return try {
            val response = withTimeout(TIMEOUT_MILLIS) { transport.execute(request("POST", "$PATH/ad-interest", token, body = "{}")) }
            if (response.status == 200) aiPlanJson.decodeFromString(AiAdInterestResult.serializer(), response.body) else null
        } catch (error: CancellationException) {
            if (error is kotlinx.coroutines.TimeoutCancellationException) null else throw error
        } catch (_: Throwable) {
            null
        }
    }

    private fun request(method: String, path: String, token: String, body: String? = null) = AiHttpRequest(
        method = method,
        path = path,
        headers = buildMap {
            put("Authorization", "Bearer $token")
            put("apikey", config.publishableKey)
            put("Accept", "application/json")
            if (body != null) put("Content-Type", "application/json; charset=utf-8")
        },
        body = body?.toByteArray(Charsets.UTF_8),
    )

    private companion object {
        const val PATH = "/functions/v1/ai-plan"
        const val TIMEOUT_MILLIS = 20_000L
    }
}
