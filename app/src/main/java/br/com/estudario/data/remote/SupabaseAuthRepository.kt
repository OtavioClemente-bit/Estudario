package br.com.estudario.data.remote

import br.com.estudario.BuildConfig
import java.net.URI
import java.util.Base64
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.withLock

@JvmInline
value class SupabaseGoogleCredential(val idToken: String) {
    init {
        require(idToken.isNotBlank()) { "Google credential must not be blank." }
    }
}

class SupabaseConfigurationException(message: String) : IllegalStateException(message)

class SupabaseClientConfig private constructor(
    val projectUrl: String,
    val publishableKey: String,
) {
    val isConfigured: Boolean
        get() = projectUrl.isNotBlank() && publishableKey.isNotBlank()

    companion object {
        fun from(projectUrl: String, publishableKey: String): SupabaseClientConfig {
            val normalizedUrl = projectUrl.trim()
            val normalizedKey = publishableKey.trim()
            if (normalizedUrl.isEmpty() && normalizedKey.isEmpty()) {
                return SupabaseClientConfig("", "")
            }
            if (normalizedUrl.isEmpty() || normalizedKey.isEmpty()) {
                throw SupabaseConfigurationException(
                    "Supabase client configuration is incomplete; the configuration gate is closed.",
                )
            }
            SupabaseClientConfigValidator.validate(normalizedUrl, normalizedKey)
            return SupabaseClientConfig(normalizedUrl, normalizedKey)
        }

        fun fromBuildConfig(): SupabaseClientConfig =
            from(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_PUBLISHABLE_KEY)
    }
}

object SupabaseClientConfigValidator {
    private val publishableKeyPattern = Regex("sb_publishable_[A-Za-z0-9_-]{16,}")
    private val jwtSegmentPattern = Regex("[A-Za-z0-9_-]+")

    fun validate(projectUrl: String, publishableKey: String) {
        val url = runCatching { URI(projectUrl) }.getOrNull()
        if (url == null || url.scheme != "https" || url.host.isNullOrBlank() || url.userInfo != null || url.query != null || url.fragment != null) {
            throw SupabaseConfigurationException("Supabase project URL must be an HTTPS URL without embedded credentials.")
        }
        rejectServerOnlyValue(publishableKey)
        rejectServerOnlyValue(projectUrl)
        if (!isClientSafePublishableKey(publishableKey)) {
            throw SupabaseConfigurationException(
                "Supabase publishable key must match the client-safe publishable or legacy anon shape.",
            )
        }
    }

    private fun isClientSafePublishableKey(value: String): Boolean {
        if (publishableKeyPattern.matches(value)) return true

        val segments = value.split('.')
        if (segments.size != 3 || segments.any { !jwtSegmentPattern.matches(it) }) return false

        val payload = runCatching {
            Base64.getUrlDecoder().decode(segments[1]).toString(Charsets.UTF_8)
        }.getOrNull() ?: return false
        return jwtClaim(payload, "role") == "anon" && !jwtClaim(payload, "ref").isNullOrBlank()
    }

    private fun rejectServerOnlyValue(value: String) {
        val normalized = value.lowercase()
        val marker = listOf("service_role", "service-role", "servicerole", "service role", "openai", "sk-", "sb_secret_")
            .firstOrNull(normalized::contains)
        if (marker != null || jwtRole(value) == "service_role") {
            throw SupabaseConfigurationException(
                "Server-only service-role/OpenAI credentials are not allowed in Android configuration.",
            )
        }
    }

    private fun jwtRole(value: String): String? {
        val payload = value.split('.').getOrNull(1) ?: return null
        val decoded = runCatching { Base64.getUrlDecoder().decode(payload).toString(Charsets.UTF_8) }.getOrNull() ?: return null
        return jwtClaim(decoded, "role")
    }

    private fun jwtClaim(payload: String, name: String): String? =
        Regex("\\\"$name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"")
            .find(payload)
            ?.groupValues
            ?.getOrNull(1)
            ?.lowercase()
}

/** Sessão nova mais a chave de renovação, que não faz parte da sessão pública. */
class SupabaseTokens(val session: SupabaseSession, val refreshToken: String?) {
    override fun toString(): String = "SupabaseTokens(session=<redacted>, refreshToken=<redacted>)"
}

interface SupabaseAuthClient {
    suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession

    /** O mesmo login, devolvendo também a chave de renovação quando o servidor manda. */
    suspend fun signInWithGoogleTokens(credential: SupabaseGoogleCredential): SupabaseTokens =
        SupabaseTokens(signInWithGoogle(credential), null)

    /** Troca a chave de renovação por uma sessão nova (e uma chave nova, que substitui a anterior). */
    suspend fun refreshSession(refreshToken: String): SupabaseTokens =
        throw UnsupportedOperationException("Renovação não disponível neste cliente.")

    suspend fun sendEmailOtp(email: String)

    suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession

    suspend fun signOut(accessToken: String)
}

interface SupabaseAuthRepository {
    suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession

    suspend fun sendEmailOtp(email: String)

    suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession

    suspend fun signOut()

    fun observeSession(): Flow<SupabaseSessionState>

    /** Returns a JWT only for a ready authenticated session; loading, empty, and read-error states return null. */
    fun accessToken(): String?

    /** Returns the user ID for the same ready, unexpired session that supplies [accessToken]. */
    fun currentUserId(): String? = null

    /** When the stored session expires (epoch seconds), or null with no session. Used to renew early. */
    fun sessionExpiresAt(): Long? = null

    /**
     * Renova a sessão em segundo plano com a chave de renovação guardada, sem abrir nenhuma tela.
     * Devolve false quando não há chave ou o servidor recusou (aí é preciso entrar de novo).
     */
    suspend fun refreshSession(): Boolean = false
}

class DefaultSupabaseAuthRepository(
    private val client: SupabaseAuthClient,
    private val sessionStore: SupabaseSessionStore,
    private val clockSeconds: () -> Long = { System.currentTimeMillis() / 1_000 },
    private val refreshStore: SupabaseRefreshTokenStore? = null,
) : SupabaseAuthRepository {
    // O servidor troca a chave a cada renovação; duas renovações ao mesmo tempo usariam a mesma
    // chave e a segunda seria recusada.
    private val refreshLock = kotlinx.coroutines.sync.Mutex()

    override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession {
        val tokens = client.signInWithGoogleTokens(credential)
        sessionStore.save(tokens.session)
        rememberRefreshToken(tokens.refreshToken)
        return tokens.session
    }

    override suspend fun refreshSession(): Boolean {
        val store = refreshStore ?: return false
        return refreshLock.withLock {
            val token = runCatching { store.read() }.getOrNull() ?: return@withLock false
            try {
                val tokens = client.refreshSession(token)
                sessionStore.save(tokens.session)
                rememberRefreshToken(tokens.refreshToken)
                true
            } catch (error: kotlinx.coroutines.CancellationException) {
                throw error
            } catch (error: SupabaseAuthException) {
                // Chave recusada (vencida ou revogada): apaga para não insistir. Falha de rede
                // mantém a chave para a próxima tentativa.
                if (error.code == SupabaseAuthException.Code.REQUEST_FAILED && (error.status ?: 0) in 400..499) {
                    runCatching { store.clear() }
                }
                false
            } catch (_: Exception) {
                false
            }
        }
    }

    private suspend fun rememberRefreshToken(refreshToken: String?) {
        val store = refreshStore ?: return
        runCatching { if (refreshToken.isNullOrBlank()) store.clear() else store.save(refreshToken) }
    }

    override suspend fun sendEmailOtp(email: String) {
        require(email.isNotBlank()) { "Email must not be blank." }
        client.sendEmailOtp(email.trim())
    }

    override suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession {
        require(email.isNotBlank()) { "Email must not be blank." }
        require(token.isNotBlank()) { "Email OTP must not be blank." }
        val session = client.verifyEmailOtp(email.trim(), token.trim())
        sessionStore.save(session)
        return session
    }

    override suspend fun signOut() {
        var failure: Throwable? = null
        try {
            accessToken()?.let { client.signOut(it) }
        } catch (error: Throwable) {
            failure = error
        } finally {
            sessionStore.clear()
            refreshStore?.let { runCatching { it.clear() } }
        }
        failure?.let { throw it }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeSession(): Flow<SupabaseSessionState> = sessionStore.observe().flatMapLatest { state ->
        val session = (state as? SupabaseSessionState.Ready)?.session
        val expiry = session?.expiresAtEpochSeconds
        if (session == null) {
            flowOf(state)
        } else if (expiry == null || expiry <= clockSeconds()) {
            flowOf(SupabaseSessionState.Ready(null))
        } else {
            flow {
                emit(state)
                while (true) {
                    val now = clockSeconds()
                    if (now >= expiry) break
                    // Bound each wait before converting seconds to milliseconds. Recheck the
                    // clock so even an unusually large expiry eventually becomes unauthenticated.
                    val maxCheckSeconds = 86_400L
                    val waitSeconds = if (now <= Long.MAX_VALUE - maxCheckSeconds &&
                        expiry > now + maxCheckSeconds
                    ) maxCheckSeconds else expiry - now
                    delay(waitSeconds * 1_000)
                }
                emit(SupabaseSessionState.Ready(null))
            }
        }
    }

    override fun sessionExpiresAt(): Long? =
        (sessionStore.current() as? SupabaseSessionState.Ready)?.session?.expiresAtEpochSeconds

    override fun accessToken(): String? {
        val session = (sessionStore.current() as? SupabaseSessionState.Ready)?.session ?: return null
        val expiry = session.expiresAtEpochSeconds ?: return null
        return session.accessToken.takeIf { expiry > clockSeconds() }
    }

    override fun currentUserId(): String? {
        val session = (sessionStore.current() as? SupabaseSessionState.Ready)?.session ?: return null
        val expiry = session.expiresAtEpochSeconds ?: return null
        return session.userId?.takeIf { it.isNotBlank() && expiry > clockSeconds() }
    }
}

fun interface AiAccessTokenProvider {
    fun accessToken(): String?
}

/**
 * Adapter boundary for the existing Google Drive token owner. Supabase AI callers never receive
 * this source; it exists so integrations can keep Drive credentials separate from Supabase JWTs.
 */
fun interface DriveAccessTokenSource {
    fun accessToken(): String?
}

/**
 * Minimal adapter for the existing Google Drive authorization path. It owns only the Drive token
 * supplier and is never passed to the Supabase AI token provider.
 */
class GoogleDriveAccessTokenSource(
    private val accessTokenProvider: () -> String?,
) : DriveAccessTokenSource {
    override fun accessToken(): String? = accessTokenProvider()
}

class SupabaseAiTokenProvider(
    private val repository: SupabaseAuthRepository,
) : AiAccessTokenProvider {
    override fun accessToken(): String? = repository.accessToken()
}

class UnavailableSupabaseAuthClient(
    private val config: SupabaseClientConfig,
) : SupabaseAuthClient {
    private fun unavailable(): Nothing = throw SupabaseConfigurationException(
        if (config.isConfigured) {
            "Supabase Auth transport is not installed in this configuration."
        } else {
            "Supabase Auth configuration gate is closed; provide client-safe values before device testing."
        },
    )

    override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession = unavailable()

    override suspend fun sendEmailOtp(email: String): Unit = unavailable()

    override suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession = unavailable()

    override suspend fun signOut(accessToken: String): Unit = unavailable()
}
