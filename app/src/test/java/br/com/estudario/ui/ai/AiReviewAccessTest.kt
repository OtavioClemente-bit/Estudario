package br.com.estudario.ui.ai

import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpResponse
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.remote.DriveAccessTokenSource
import br.com.estudario.data.remote.SupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseClientConfig
import br.com.estudario.data.remote.SupabaseGoogleCredential
import br.com.estudario.data.remote.SupabaseSession
import br.com.estudario.data.remote.SupabaseSessionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import java.io.IOException
import java.net.SocketTimeoutException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AiReviewAccessTest {
    @Test
    fun accessBoundaryDistinguishesUnauthenticatedBetaDeniedQuotaAndReady() {
        assertEquals(AiReviewAccessKind.UNAUTHENTICATED, AiReviewAccessResult(false, false, "UNAUTHENTICATED").toUiState().kind)
        assertEquals(AiReviewAccessKind.DENIED, AiReviewAccessResult(true, false, "BETA_ACCESS_REQUIRED").toUiState().kind)
        assertEquals("QUOTA_EXHAUSTED", AiReviewAccessResult(true, false, "QUOTA_EXHAUSTED").toUiState().reasonCode)
        assertEquals(AiReviewAccessKind.READY, AiReviewAccessResult(true, true).toUiState().kind)
    }

    @Test
    fun authenticatedBetaAccessPreservesFeatureQuotaAndResetFromServerContract() = runTest {
        val transport = RecordingTransport(accessJson(beta = true, featureEnabled = true, remaining = 2))
        val result = repository("supabase-jwt", transport).loadAccess()

        val access = available(result).access
        assertTrue(access.authenticated)
        assertTrue(access.betaAccess)
        assertTrue(access.featureEnabled)
        assertEquals(AiFeature.SYLLABUS_GENERATION, access.feature)
        assertEquals(2, access.quota?.remaining)
        assertEquals("2026-09-01T00:00:00Z", access.quota?.periodStart)
        assertEquals("Bearer supabase-jwt", transport.requests.single().headers["Authorization"])
        assertEquals("GET", transport.requests.single().method)
        assertTrue(transport.requests.single().path.contains("feature=SYLLABUS_GENERATION"))
    }

    @Test
    fun betaDeniedFeatureDisabledAndQuotaExhaustedAreReturnedWithoutLocalPolicy() = runTest {
        val betaDenied = repository("jwt", RecordingTransport(accessJson(beta = false, featureEnabled = true, remaining = 2))).loadAccess()
        val featureDisabled = repository("jwt", RecordingTransport(accessJson(beta = true, featureEnabled = false, remaining = 2))).loadAccess()
        val quotaExhausted = repository("jwt", RecordingTransport(accessJson(beta = true, featureEnabled = true, remaining = 0))).loadAccess()

        assertEquals("BETA_ACCESS_REQUIRED", available(betaDenied).access.reasonCode)
        assertEquals("FEATURE_DISABLED", available(featureDisabled).access.reasonCode)
        assertEquals("QUOTA_EXHAUSTED", available(quotaExhausted).access.reasonCode)
        assertFalse(available(betaDenied).access.canUse)
        assertFalse(available(featureDisabled).access.canUse)
        assertEquals(0, available(quotaExhausted).access.quota?.remaining)
    }

    @Test
    fun absentJwtDoesNotMakeARequestAndDoesNotReadDriveToken() = runTest {
        val transport = RecordingTransport(accessJson(beta = true, featureEnabled = true, remaining = 1))
        val auth = FakeAuthRepository(token = null)
        val drive = DriveAccessTokenSource { null }
        val repository = DefaultAiAccessRepository(
            config = config(),
            authRepository = auth,
            transport = transport,
        )

        val result = repository.loadAccess()

        assertEquals(AiAccessFailureCode.AUTH_REQUIRED, failed(result).failure.code)
        assertNull(drive.accessToken())
        assertTrue(transport.requests.isEmpty())
    }

    @Test
    fun closedConfigurationFailsBeforeAnyRequestEvenWithJwt() = runTest {
        val transport = RecordingTransport(accessJson(beta = true, featureEnabled = true, remaining = 1))
        val result = DefaultAiAccessRepository(
            config = SupabaseClientConfig.from(projectUrl = "", publishableKey = ""),
            authRepository = FakeAuthRepository("supabase-jwt"),
            transport = transport,
        ).loadAccess()

        assertEquals(AiAccessFailureCode.CONFIGURATION_CLOSED, failed(result).failure.code)
        assertTrue(transport.requests.isEmpty())
    }

    @Test
    fun expiredJwtIsClassifiedAsAuthenticationFailureWithoutLeakingResponseBody() = runTest {
        val transport = RecordingTransport(AiHttpResponse(401, "secret-provider-detail"))
        val result = repository("expired-jwt", transport).loadAccess()

        val failure = failed(result).failure
        assertEquals(AiAccessFailureCode.AUTH_EXPIRED, failure.code)
        assertNull(failure.safeMessage)
    }

    @Test
    fun invalidHttpContractIsSafeAndNeverAcceptedAsAccess() = runTest {
        val malformed = repository("jwt", RecordingTransport(AiHttpResponse(200, "{\"canUse\":true}"))).loadAccess()
        val serverFailure = repository("jwt", RecordingTransport(AiHttpResponse(503, "provider-secret"))).loadAccess()

        assertEquals(AiAccessFailureCode.INVALID_RESPONSE, failed(malformed).failure.code)
        assertEquals(AiAccessFailureCode.HTTP_UNAVAILABLE, failed(serverFailure).failure.code)
        assertNull(failed(serverFailure).failure.safeMessage)
    }

    @Test
    fun timeoutAndOfflineAreReportedAsSafeTransportFailures() = runTest {
        val timeout = repository("jwt", RecordingTransportFailure(SocketTimeoutException("timeout"))).loadAccess()
        val offline = repository("jwt", RecordingTransportFailure(IOException("offline"))).loadAccess()

        assertEquals(AiAccessFailureCode.TIMEOUT, failed(timeout).failure.code)
        assertEquals(AiAccessFailureCode.OFFLINE, failed(offline).failure.code)
    }

    private fun available(result: AiAccessLoadResult): AiAccessLoadResult.Available {
        assertTrue("Expected server access contract, got $result", result is AiAccessLoadResult.Available)
        return result as AiAccessLoadResult.Available
    }

    private fun failed(result: AiAccessLoadResult): AiAccessLoadResult.Failed {
        assertTrue("Expected safe access failure, got $result", result is AiAccessLoadResult.Failed)
        return result as AiAccessLoadResult.Failed
    }

    private fun repository(token: String?, transport: AiHttpTransport): AiAccessRepository =
        DefaultAiAccessRepository(
            config = config(),
            authRepository = FakeAuthRepository(token),
            transport = transport,
        )

    private fun config() = SupabaseClientConfig.from(
        projectUrl = "https://project.supabase.co",
        publishableKey = "sb_publishable_1234567890123456",
    )

    private fun accessJson(beta: Boolean, featureEnabled: Boolean, remaining: Int): String = """
        {
          "authenticated":true,
          "betaAccess":$beta,
          "feature":"SYLLABUS_GENERATION",
          "featureEnabled":$featureEnabled,
          "quota":{"feature":"SYLLABUS_GENERATION","limit":3,"successfulCount":${3 - remaining},"reservedCount":0,"remaining":$remaining,"periodStart":"2026-09-01T00:00:00Z"},
          "canUse":${beta && featureEnabled && remaining > 0},
          "reasonCode":${if (beta && featureEnabled && remaining > 0) "null" else "\"${if (!beta) "BETA_ACCESS_REQUIRED" else if (!featureEnabled) "FEATURE_DISABLED" else "QUOTA_EXHAUSTED"}\""}
        }
    """.trimIndent()

    private class RecordingTransport(private val response: AiHttpResponse) : AiHttpTransport {
        val requests = mutableListOf<AiHttpRequest>()

        constructor(body: String) : this(AiHttpResponse(200, body))

        override suspend fun execute(request: AiHttpRequest): AiHttpResponse {
            requests += request
            return response
        }
    }

    private class RecordingTransportFailure(private val failure: Throwable) : AiHttpTransport {
        override suspend fun execute(request: AiHttpRequest): AiHttpResponse = throw failure
    }

    private class FakeAuthRepository(token: String?) : SupabaseAuthRepository {
        private val state = MutableStateFlow<SupabaseSessionState>(
            SupabaseSessionState.Ready(token?.let { SupabaseSession(it) }),
        )

        override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession = error("not used")
        override suspend fun sendEmailOtp(email: String) = error("not used")
        override suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession = error("not used")
        override suspend fun signOut() = error("not used")
        override fun observeSession(): Flow<SupabaseSessionState> = state
        override fun accessToken(): String? = (state.value as SupabaseSessionState.Ready).session?.accessToken
    }
}
