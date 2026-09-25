package br.com.estudario.data.remote

import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.put

data class AuthHttpRequest(
    val method: String,
    val path: String,
    val headers: Map<String, String>,
    val body: ByteArray,
)

data class AuthHttpResponse(val status: Int, val body: String = "")

fun interface AuthHttpTransport {
    suspend fun execute(request: AuthHttpRequest): AuthHttpResponse
}

class UrlConnectionAuthHttpTransport(private val baseUrl: String) : AuthHttpTransport {
    override suspend fun execute(request: AuthHttpRequest): AuthHttpResponse = withContext(Dispatchers.IO) {
        val connection = (URL("${baseUrl.trimEnd('/')}${request.path}").openConnection() as HttpURLConnection).apply {
            requestMethod = request.method
            connectTimeout = 15_000
            readTimeout = 30_000
            doOutput = true
            setFixedLengthStreamingMode(request.body.size)
            request.headers.forEach { (name, value) -> setRequestProperty(name, value) }
        }
        try {
            connection.outputStream.use { it.write(request.body) }
            val status = connection.responseCode
            val body = (if (status >= 400) connection.errorStream else connection.inputStream)
                ?.use { it.readBytes().toString(Charsets.UTF_8) }.orEmpty()
            AuthHttpResponse(status, body)
        } finally {
            connection.disconnect()
        }
    }
}

class SupabaseAuthException(
    val code: Code,
    val status: Int? = null,
) : IllegalStateException("Supabase Auth ${code.name.lowercase()}.") {
    enum class Code { REQUEST_FAILED, INVALID_RESPONSE, NETWORK_ERROR }
}

/** Supabase Auth REST adapter. Request/response contents and provider errors never enter exceptions. */
class HttpSupabaseAuthClient(
    private val config: SupabaseClientConfig,
    private val transport: AuthHttpTransport = UrlConnectionAuthHttpTransport(config.projectUrl),
    private val clockSeconds: () -> Long = { System.currentTimeMillis() / 1_000 },
) : SupabaseAuthClient {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun sendEmailOtp(email: String) {
        execute("/auth/v1/otp", buildJsonObject { put("email", email) })
    }

    override suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession =
        session(execute("/auth/v1/verify", buildJsonObject {
            put("email", email)
            put("token", token)
            put("type", "email")
        }))

    override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession =
        session(execute("/auth/v1/token?grant_type=id_token", buildJsonObject {
            put("provider", "google")
            put("id_token", credential.idToken)
        }))

    override suspend fun signOut(accessToken: String) {
        execute("/auth/v1/logout?scope=local", buildJsonObject { }, accessToken)
    }

    private suspend fun execute(path: String, body: JsonObject, accessToken: String? = null): AuthHttpResponse {
        if (!config.isConfigured) {
            throw SupabaseConfigurationException(
                "Supabase Auth configuration gate is closed; provide client-safe values before device testing.",
            )
        }
        val headers = mutableMapOf(
            "apikey" to config.publishableKey,
            "Content-Type" to "application/json",
            "Accept" to "application/json",
        )
        if (accessToken != null) headers["Authorization"] = "Bearer $accessToken"
        val request = AuthHttpRequest("POST", path, headers, json.encodeToString(JsonObject.serializer(), body).toByteArray())
        val response = try {
            transport.execute(request)
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            throw SupabaseAuthException(SupabaseAuthException.Code.NETWORK_ERROR)
        }
        if (response.status !in 200..299) {
            throw SupabaseAuthException(SupabaseAuthException.Code.REQUEST_FAILED, response.status)
        }
        return response
    }

    private fun session(response: AuthHttpResponse): SupabaseSession {
        val payload = try {
            json.parseToJsonElement(response.body) as? JsonObject
        } catch (_: Exception) {
            null
        } ?: throw invalidResponse()
        val accessToken = payload.string("access_token") ?: throw invalidResponse()
        val userId = (payload["user"] as? JsonObject)?.string("id") ?: throw invalidResponse()
        val expiry = payload.long("expires_at")
            ?: payload.long("expires_in")?.takeIf { it > 0 }?.let { clockSeconds() + it }
            ?: throw invalidResponse()
        if (expiry <= clockSeconds()) throw invalidResponse()
        return SupabaseSession(accessToken, userId, expiry)
    }

    private fun JsonObject.string(name: String): String? =
        runCatching { get(name)?.jsonPrimitive?.contentOrNull?.takeIf(String::isNotBlank) }.getOrNull()

    private fun JsonObject.long(name: String): Long? =
        runCatching { get(name)?.jsonPrimitive?.longOrNull }.getOrNull()

    private fun invalidResponse() = SupabaseAuthException(SupabaseAuthException.Code.INVALID_RESPONSE)
}
