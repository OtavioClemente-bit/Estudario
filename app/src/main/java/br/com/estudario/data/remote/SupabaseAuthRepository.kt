package br.com.estudario.data.remote

import br.com.estudario.BuildConfig
import java.net.URI
import java.util.Base64
import kotlinx.coroutines.flow.Flow

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
    fun validate(projectUrl: String, publishableKey: String) {
        val url = runCatching { URI(projectUrl) }.getOrNull()
        if (url == null || url.scheme != "https" || url.host.isNullOrBlank() || url.userInfo != null || url.query != null || url.fragment != null) {
            throw SupabaseConfigurationException("Supabase project URL must be an HTTPS URL without embedded credentials.")
        }
        rejectServerOnlyValue(publishableKey)
        rejectServerOnlyValue(projectUrl)
        if (publishableKey.any(Char::isWhitespace)) {
            throw SupabaseConfigurationException("Supabase publishable key must not contain whitespace.")
        }
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
        val role = Regex("\\\"role\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"")
            .find(decoded)
            ?.groupValues
            ?.getOrNull(1)
        return role?.lowercase()
    }
}

interface SupabaseAuthClient {
    suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession

    suspend fun sendEmailOtp(email: String)

    suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession

    suspend fun signOut()
}

interface SupabaseAuthRepository {
    suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession

    suspend fun sendEmailOtp(email: String)

    suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession

    suspend fun signOut()

    fun observeSession(): Flow<SupabaseSession?>

    fun accessToken(): String?
}

class DefaultSupabaseAuthRepository(
    private val client: SupabaseAuthClient,
    private val sessionStore: SupabaseSessionStore,
) : SupabaseAuthRepository {
    override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession {
        val session = client.signInWithGoogle(credential)
        sessionStore.save(session)
        return session
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
            client.signOut()
        } catch (error: Throwable) {
            failure = error
        } finally {
            sessionStore.clear()
        }
        failure?.let { throw it }
    }

    override fun observeSession(): Flow<SupabaseSession?> = sessionStore.observe()

    override fun accessToken(): String? = sessionStore.current()?.accessToken
}

fun interface AiAccessTokenProvider {
    fun accessToken(): String?
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

    override suspend fun signOut(): Unit = unavailable()
}
