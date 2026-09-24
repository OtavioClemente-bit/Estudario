package br.com.estudario.ui.ai

import br.com.estudario.data.ai.AiAccess
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.ai.EstudarioContractJson
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import br.com.estudario.data.remote.SupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseClientConfig
import br.com.estudario.data.remote.DriveAccessTokenSource
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

enum class AiAccessFailureCode {
    CONFIGURATION_CLOSED,
    AUTH_REQUIRED,
    AUTH_EXPIRED,
    INVALID_RESPONSE,
    HTTP_INVALID,
    HTTP_UNAVAILABLE,
    TIMEOUT,
    OFFLINE,
}

data class AiAccessFailure(
    val code: AiAccessFailureCode,
    val status: Int? = null,
    /** Deliberately null: remote response bodies and transport messages are not UI errors. */
    val safeMessage: String? = null,
)

sealed interface AiAccessLoadResult {
    data class Available(val access: AiAccess) : AiAccessLoadResult
    data class Failed(val failure: AiAccessFailure) : AiAccessLoadResult
}

/** Read-only client for the existing Supabase Edge Function contract. It is not the jobs client. */
interface AiAccessApiClient {
    suspend fun loadAccess(feature: AiFeature = AiFeature.SYLLABUS_GENERATION): AiAccess
}

class AiAccessApiException(
    val code: AiAccessFailureCode,
    val status: Int? = null,
) : IllegalStateException(code.name)

class HttpAiAccessApiClient(
    private val config: SupabaseClientConfig,
    private val authRepository: SupabaseAuthRepository,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransport(config.projectUrl),
    private val httpTimeoutMillis: Long = DEFAULT_HTTP_TIMEOUT_MILLIS,
    /** Injection-only seam for proving this boundary never reads a Google Drive credential. */
    @Suppress("UNUSED_PARAMETER")
    private val driveAccessTokenSource: DriveAccessTokenSource? = null,
) : AiAccessApiClient {
    init {
        require(httpTimeoutMillis > 0)
    }

    override suspend fun loadAccess(feature: AiFeature): AiAccess {
        if (!config.isConfigured) throw AiAccessApiException(AiAccessFailureCode.CONFIGURATION_CLOSED)
        val jwt = authRepository.accessToken()
            ?: throw AiAccessApiException(AiAccessFailureCode.AUTH_REQUIRED)
        val response = execute(
            AiHttpRequest(
                method = "GET",
                path = "/functions/v1/ai-access?feature=${feature.name}",
                headers = mapOf(
                    "Authorization" to "Bearer $jwt",
                    "apikey" to config.publishableKey,
                    "Accept" to JSON_CONTENT_TYPE,
                ),
            ),
        )
        if (response.status != 200) {
            val code = when {
                response.status == 401 -> AiAccessFailureCode.AUTH_EXPIRED
                response.status >= 500 -> AiAccessFailureCode.HTTP_UNAVAILABLE
                else -> AiAccessFailureCode.HTTP_INVALID
            }
            throw AiAccessApiException(code, response.status)
        }
        return runCatching { EstudarioContractJson.decodeAccess(response.body) }
            .getOrElse { throw AiAccessApiException(AiAccessFailureCode.INVALID_RESPONSE, 502) }
    }

    private suspend fun execute(request: AiHttpRequest): br.com.estudario.data.ai.AiHttpResponse {
        return try {
            withTimeout(httpTimeoutMillis) { transport.execute(request) }
        } catch (_: TimeoutCancellationException) {
            throw AiAccessApiException(AiAccessFailureCode.TIMEOUT, 504)
        } catch (_: SocketTimeoutException) {
            throw AiAccessApiException(AiAccessFailureCode.TIMEOUT, 504)
        } catch (error: CancellationException) {
            throw error
        } catch (_: IOException) {
            throw AiAccessApiException(AiAccessFailureCode.OFFLINE, 503)
        } catch (_: AiAccessApiException) {
            throw AiAccessApiException(AiAccessFailureCode.OFFLINE, 503)
        } catch (_: Throwable) {
            throw AiAccessApiException(AiAccessFailureCode.OFFLINE, 503)
        }
    }

    private companion object {
        const val DEFAULT_HTTP_TIMEOUT_MILLIS = 30_000L
        const val JSON_CONTENT_TYPE = "application/json; charset=utf-8"
    }
}

interface AiAccessRepository {
    suspend fun loadAccess(feature: AiFeature = AiFeature.SYLLABUS_GENERATION): AiAccessLoadResult
}

/** Shared application instance for Task 13 and the future quota surface; it never mutates quota. */
class DefaultAiAccessRepository(
    private val apiClient: AiAccessApiClient,
) : AiAccessRepository {
    constructor(
        config: SupabaseClientConfig,
        authRepository: SupabaseAuthRepository,
        transport: AiHttpTransport = UrlConnectionAiHttpTransport(config.projectUrl),
        httpTimeoutMillis: Long = 30_000L,
        driveAccessTokenSource: DriveAccessTokenSource? = null,
    ) : this(
        HttpAiAccessApiClient(
            config = config,
            authRepository = authRepository,
            transport = transport,
            httpTimeoutMillis = httpTimeoutMillis,
            driveAccessTokenSource = driveAccessTokenSource,
        ),
    )

    override suspend fun loadAccess(feature: AiFeature): AiAccessLoadResult = try {
        AiAccessLoadResult.Available(apiClient.loadAccess(feature))
    } catch (error: AiAccessApiException) {
        AiAccessLoadResult.Failed(AiAccessFailure(error.code, error.status))
    } catch (error: CancellationException) {
        throw error
    } catch (_: Throwable) {
        AiAccessLoadResult.Failed(AiAccessFailure(AiAccessFailureCode.OFFLINE, 503))
    }
}

data class AiReviewAccessResult(
    val authenticated: Boolean,
    val canUse: Boolean,
    val reasonCode: String? = null,
    val access: AiAccess? = null,
) {
    fun toUiState(): AiReviewAccessState = when {
        !authenticated -> AiReviewAccessState.UNAUTHENTICATED
        canUse -> AiReviewAccessState(AiReviewAccessKind.READY, access = access)
        else -> AiReviewAccessState.denied(reasonCode, access)
    }
}

interface AiReviewAccessGateway {
    suspend fun check(): AiReviewAccessResult
}

fun interface AiReviewLoginLauncher {
    fun launch(onReturned: () -> Unit)
}

class DefaultAiReviewAccessGateway(
    private val repository: AiAccessRepository,
) : AiReviewAccessGateway {
    override suspend fun check(): AiReviewAccessResult = when (val result = repository.loadAccess()) {
        is AiAccessLoadResult.Available -> result.access.toReviewResult()
        is AiAccessLoadResult.Failed -> result.failure.toReviewResult()
    }

    private fun AiAccess.toReviewResult() = AiReviewAccessResult(
        authenticated = authenticated,
        canUse = canUse,
        reasonCode = reasonCode,
        access = this,
    )

    private fun AiAccessFailure.toReviewResult(): AiReviewAccessResult {
        val unauthenticated = code == AiAccessFailureCode.AUTH_REQUIRED || code == AiAccessFailureCode.AUTH_EXPIRED
        return AiReviewAccessResult(
            authenticated = !unauthenticated,
            canUse = false,
            reasonCode = when (code) {
                AiAccessFailureCode.AUTH_REQUIRED, AiAccessFailureCode.AUTH_EXPIRED -> "UNAUTHENTICATED"
                else -> code.name
            },
        )
    }
}
