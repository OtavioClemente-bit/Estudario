package br.com.estudario.web.data

import br.com.estudario.web.WebConfig
import kotlinx.browser.localStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.js.Date

@Serializable
data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    /** Expiração do access token, em segundos desde 1970. */
    val expiresAt: Long,
    val userId: String,
    val email: String? = null,
    val name: String? = null,
    val avatarUrl: String? = null,
)

class AuthException(message: String) : Exception(message)

/**
 * Login com o Supabase direto pela API (sem SDK): Google pelo token do "Fazer login com o Google"
 * ou código por e-mail. A sessão fica no localStorage deste navegador e é renovada sozinha.
 */
object Auth {
    private const val STORAGE_KEY = "estudario.session"
    private val json = Json { ignoreUnknownKeys = true }

    var session: AuthSession? = load()
        private set

    private fun load(): AuthSession? = runCatching {
        localStorage.getItem(STORAGE_KEY)?.let { json.decodeFromString(AuthSession.serializer(), it) }
    }.getOrNull()

    private fun save(value: AuthSession?) {
        session = value
        if (value == null) localStorage.removeItem(STORAGE_KEY)
        else localStorage.setItem(STORAGE_KEY, json.encodeToString(AuthSession.serializer(), value))
    }

    private val baseHeaders get() = mapOf("apikey" to WebConfig.SUPABASE_KEY, "Content-Type" to "application/json")

    suspend fun signInWithGoogle(idToken: String) {
        val body = buildJsonObject { put("provider", "google"); put("id_token", idToken) }.toString()
        save(parse(httpRequest("POST", "${WebConfig.SUPABASE_URL}/auth/v1/token?grant_type=id_token", baseHeaders, body)))
    }

    suspend fun sendEmailCode(email: String) {
        val body = buildJsonObject { put("email", email.trim()); put("create_user", true) }.toString()
        val response = httpRequest("POST", "${WebConfig.SUPABASE_URL}/auth/v1/otp", baseHeaders, body)
        if (!response.ok) throw AuthException(errorMessage(response, "Não deu para enviar o código. Confira o e-mail e tente de novo."))
    }

    suspend fun verifyEmailCode(email: String, code: String) {
        val body = buildJsonObject { put("type", "email"); put("email", email.trim()); put("token", code.trim()) }.toString()
        save(parse(httpRequest("POST", "${WebConfig.SUPABASE_URL}/auth/v1/verify", baseHeaders, body)))
    }

    /** Token válido por pelo menos mais um minuto, renovando se preciso; null se a sessão acabou. */
    suspend fun accessToken(): String? {
        val current = session ?: return null
        val now = (Date.now() / 1000).toLong()
        if (current.expiresAt - now > 60) return current.accessToken
        val body = buildJsonObject { put("refresh_token", current.refreshToken) }.toString()
        val response = httpRequest("POST", "${WebConfig.SUPABASE_URL}/auth/v1/token?grant_type=refresh_token", baseHeaders, body)
        if (response.status in 400..499) { save(null); return null }
        if (!response.ok) return current.accessToken
        save(parse(response))
        return session?.accessToken
    }

    /**
     * Volta do link de login do e-mail: o Supabase devolve a sessão no endereço
     * (#access_token=...&refresh_token=...). Guarda a sessão e limpa o endereço.
     */
    suspend fun consumeRedirect(): Boolean {
        val hash = kotlinx.browser.window.location.hash.removePrefix("#")
        if ("access_token=" !in hash) return false
        val params = hash.split('&').mapNotNull { part -> part.split('=', limit = 2).takeIf { it.size == 2 }?.let { it[0] to js("decodeURIComponent")(it[1]) as String } }.toMap()
        kotlinx.browser.window.history.replaceState(null, "", kotlinx.browser.window.location.pathname + "#/inicio")
        val access = params["access_token"] ?: return false
        val user = httpRequest("GET", "${WebConfig.SUPABASE_URL}/auth/v1/user", baseHeaders + ("Authorization" to "Bearer $access"))
        if (!user.ok) return false
        val wrapped = buildJsonObject {
            put("access_token", access)
            put("refresh_token", params["refresh_token"] ?: "")
            params["expires_at"]?.toLongOrNull()?.let { put("expires_at", it) }
            params["expires_in"]?.toLongOrNull()?.let { put("expires_in", it) }
            put("user", json.parseToJsonElement(user.body))
        }.toString()
        save(parse(HttpResponse(200, wrapped)))
        return true
    }

    suspend fun signOut() {
        val token = session?.accessToken
        save(null)
        if (token != null) httpRequest("POST", "${WebConfig.SUPABASE_URL}/auth/v1/logout", baseHeaders + ("Authorization" to "Bearer $token"))
    }

    private fun parse(response: HttpResponse): AuthSession {
        if (!response.ok) throw AuthException(errorMessage(response, "Não deu para entrar. Tente de novo."))
        val root = json.parseToJsonElement(response.body).jsonObject
        val user = root["user"]?.jsonObject ?: throw AuthException("Resposta de login inválida.")
        val meta = user["user_metadata"] as? JsonObject
        val expiresIn = root["expires_in"]?.jsonPrimitive?.longOrNull ?: 3600
        return AuthSession(
            accessToken = root.str("access_token") ?: throw AuthException("Resposta de login inválida."),
            refreshToken = root.str("refresh_token") ?: "",
            expiresAt = root["expires_at"]?.jsonPrimitive?.longOrNull ?: ((Date.now() / 1000).toLong() + expiresIn),
            userId = user.str("id") ?: throw AuthException("Resposta de login inválida."),
            email = user.str("email"),
            name = meta?.str("full_name") ?: meta?.str("name"),
            avatarUrl = meta?.str("avatar_url") ?: meta?.str("picture"),
        )
    }

    private fun errorMessage(response: HttpResponse, fallback: String): String {
        if (response.status == 0) return "Sem conexão com a internet."
        val root = runCatching { json.parseToJsonElement(response.body).jsonObject }.getOrNull() ?: return fallback
        val code = root.str("error_code") ?: root.str("code")
        return when (code) {
            "otp_expired" -> "O código expirou ou está errado. Peça um novo."
            "over_email_send_rate_limit" -> "Muitos códigos em pouco tempo. Espere um minuto e tente de novo."
            else -> fallback
        }
    }
}

internal fun JsonObject.str(key: String): String? = (this[key] as? kotlinx.serialization.json.JsonPrimitive)?.takeIf { it.isString }?.content
