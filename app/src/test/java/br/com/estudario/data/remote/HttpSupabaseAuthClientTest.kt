package br.com.estudario.data.remote

import kotlinx.coroutines.test.runTest
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class HttpSupabaseAuthClientTest {
    private val config = SupabaseClientConfig.from(
        "https://project.example.invalid",
        "sb_publishable_test_key_1234567890",
    )

    @Test fun otpSendUsesAuthRouteAndClientHeaders() = runTest {
        val transport = RecordingTransport(AuthHttpResponse(200, "{}"))
        HttpSupabaseAuthClient(config, transport).sendEmailOtp("person@example.com")

        val request = transport.requests.single()
        assertEquals("POST", request.method)
        assertEquals("/auth/v1/otp", request.path)
        assertEquals(config.publishableKey, request.headers["apikey"])
        assertEquals("application/json", request.headers["Content-Type"])
        assertFalse(request.headers.containsKey("Authorization"))
        assertEquals("person@example.com", JSONObject(String(request.body!!)).getString("email"))
    }

    @Test fun otpVerificationMapsSupabaseSession() = runTest {
        val transport = RecordingTransport(AuthHttpResponse(200, sessionBody("otp-jwt")))
        val session = HttpSupabaseAuthClient(config, transport).verifyEmailOtp("person@example.com", "123456")

        val request = transport.requests.single()
        assertEquals("POST", request.method)
        assertEquals("/auth/v1/verify", request.path)
        assertFalse(request.headers.containsKey("Authorization"))
        val body = JSONObject(String(request.body!!))
        assertEquals("person@example.com", body.getString("email"))
        assertEquals("123456", body.getString("token"))
        assertEquals("email", body.getString("type"))
        assertEquals(SupabaseSession("otp-jwt", "user-1", 2_000_000_000), session)
    }

    @Test fun googleIdTokenExchangeUsesProviderAndReturnsOnlySupabaseToken() = runTest {
        val transport = RecordingTransport(AuthHttpResponse(200, sessionBody("supabase-jwt")))
        val session = HttpSupabaseAuthClient(config, transport)
            .signInWithGoogle(SupabaseGoogleCredential("google-id-token"))

        val request = transport.requests.single()
        assertEquals("POST", request.method)
        assertEquals("/auth/v1/token?grant_type=id_token", request.path)
        assertEquals(config.publishableKey, request.headers["apikey"])
        assertFalse(request.headers.containsKey("Authorization"))
        val body = JSONObject(String(request.body!!))
        assertEquals("google", body.getString("provider"))
        assertEquals("google-id-token", body.getString("id_token"))
        assertEquals("supabase-jwt", session.accessToken)
    }

    @Test fun localSignOutUsesCurrentSupabaseBearer() = runTest {
        val transport = RecordingTransport(AuthHttpResponse(204))
        HttpSupabaseAuthClient(config, transport).signOut("supabase-jwt")

        val request = transport.requests.single()
        assertEquals("POST", request.method)
        assertEquals("/auth/v1/logout?scope=local", request.path)
        assertEquals("Bearer supabase-jwt", request.headers["Authorization"])
        assertEquals(config.publishableKey, request.headers["apikey"])
    }

    @Test fun closedConfigurationMakesNoRequest() = runTest {
        val transport = RecordingTransport(AuthHttpResponse(200, "{}"))
        val failure = runCatching {
            HttpSupabaseAuthClient(SupabaseClientConfig.from("", ""), transport)
                .sendEmailOtp("person@example.com")
        }.exceptionOrNull()

        assertTrue(failure is SupabaseConfigurationException)
        assertTrue(failure!!.message!!.contains("configuration"))
        assertTrue(transport.requests.isEmpty())
    }

    @Test fun unsuccessfulAndMalformedResponsesExposeOnlySafeTypedErrors() = runTest {
        val rawError = "sensitive-provider-error"
        val failed = RecordingTransport(AuthHttpResponse(400, "{\"error\":\"$rawError\"}"))
        val client = HttpSupabaseAuthClient(config, failed)
        val httpFailure = runCatching { client.sendEmailOtp("person@example.com") }.exceptionOrNull()
        assertTrue(httpFailure is SupabaseAuthException)
        assertFalse(httpFailure.toString().contains(rawError))

        val malformed = RecordingTransport(AuthHttpResponse(200, "{\"access_token\":\"token\"}"))
        val parseFailure = runCatching {
            HttpSupabaseAuthClient(config, malformed).verifyEmailOtp("person@example.com", "123456")
        }.exceptionOrNull()
        assertTrue(parseFailure is SupabaseAuthException)
        assertFalse(parseFailure.toString().contains("token"))
    }

    @Test fun expiresInMapsUsingClockWhenAbsoluteExpiryMissing() = runTest {
        val transport = RecordingTransport(AuthHttpResponse(200,
            "{\"access_token\":\"supabase-jwt\",\"expires_in\":3600,\"user\":{\"id\":\"user-1\"}}"))
        val session = HttpSupabaseAuthClient(config, transport, clockSeconds = { 1_000 })
            .verifyEmailOtp("person@example.com", "123456")
        assertEquals(4_600L, session.expiresAtEpochSeconds)
    }

    private fun sessionBody(token: String) =
        "{\"access_token\":\"$token\",\"token_type\":\"bearer\",\"expires_in\":3600," +
            "\"expires_at\":2000000000,\"user\":{\"id\":\"user-1\"}}"

    private class RecordingTransport(private val response: AuthHttpResponse) : AuthHttpTransport {
        val requests = mutableListOf<AuthHttpRequest>()
        override suspend fun execute(request: AuthHttpRequest): AuthHttpResponse {
            requests += request
            return response
        }
    }
}
