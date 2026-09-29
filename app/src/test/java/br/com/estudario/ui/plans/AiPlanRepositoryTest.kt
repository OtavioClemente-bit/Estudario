package br.com.estudario.ui.plans

import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpResponse
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.remote.SupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseClientConfig
import br.com.estudario.data.remote.SupabaseGoogleCredential
import br.com.estudario.data.remote.SupabaseSession
import br.com.estudario.data.remote.SupabaseSessionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiPlanRepositoryTest {
    private val summaryJson = """
        {
          "planTier":"FREE","planRenewsAt":null,"betaAccess":true,
          "plans":[{"planTier":"PRO","limits":[{"feature":"QUESTION_BATCH","periodKind":"MONTHLY","quotaLimit":100,"maxPerRequest":30,"maxPerTopicMonth":120}]}],
          "usage":[{"feature":"CONTENT_GENERATION","periodKind":"MONTHLY","limit":10,"used":3,"remaining":7,"periodStart":"2026-09-01","resetAt":"2026-10-01T03:00:00.000Z"}],
          "futureField":1
        }
    """.trimIndent()

    @Test fun loadsSummaryWithBearerTokenAndIgnoresNewFields() = runTest {
        val transport = RecordingTransport(AiHttpResponse(200, summaryJson))
        val result = repository("jwt", transport).load()
        assertTrue(result is AiPlanLoadResult.Available)
        val summary = (result as AiPlanLoadResult.Available).summary
        assertEquals("FREE", summary.planTier)
        assertEquals(7, summary.usage.single().remaining)
        assertEquals(30, summary.plans.single().limits.single().maxPerRequest)
        assertEquals("GET", transport.requests.single().method)
        assertEquals("Bearer jwt", transport.requests.single().headers["Authorization"])
    }

    @Test fun signedOutDoesNotCallServer() = runTest {
        val transport = RecordingTransport(AiHttpResponse(200, summaryJson))
        assertEquals(AiPlanLoadResult.SignedOut, repository(null, transport).load())
        assertTrue(transport.requests.isEmpty())
    }

    @Test fun serverErrorIsUnavailable() = runTest {
        assertEquals(AiPlanLoadResult.Unavailable, repository("jwt", RecordingTransport(AiHttpResponse(503, "{}"))).load())
    }

    @Test fun adInterestNeverReportsReward() = runTest {
        val transport = RecordingTransport(AiHttpResponse(200, """{"recorded":true,"rewardGranted":false,"tapsToday":1}"""))
        val result = repository("jwt", transport).registerAdInterest()
        assertTrue(result!!.recorded)
        assertFalse(result.rewardGranted)
        assertEquals("POST", transport.requests.single().method)
        assertTrue(transport.requests.single().path.endsWith("/ai-plan/ad-interest"))
    }

    private fun repository(token: String?, transport: AiHttpTransport) = HttpAiPlanRepository(
        config = SupabaseClientConfig.from("https://project.supabase.co", "sb_publishable_1234567890123456"),
        authRepository = FakeAuthRepository(token),
        transport = transport,
    )

    private class RecordingTransport(private val response: AiHttpResponse) : AiHttpTransport {
        val requests = mutableListOf<AiHttpRequest>()
        override suspend fun execute(request: AiHttpRequest): AiHttpResponse {
            requests += request
            return response
        }
    }

    private class FakeAuthRepository(token: String?) : SupabaseAuthRepository {
        private val state = MutableStateFlow<SupabaseSessionState>(SupabaseSessionState.Ready(token?.let { SupabaseSession(it) }))
        override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession = error("not used")
        override suspend fun sendEmailOtp(email: String) = error("not used")
        override suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession = error("not used")
        override suspend fun signOut() = error("not used")
        override fun observeSession(): Flow<SupabaseSessionState> = state
        override fun accessToken(): String? = (state.value as SupabaseSessionState.Ready).session?.accessToken
    }
}
