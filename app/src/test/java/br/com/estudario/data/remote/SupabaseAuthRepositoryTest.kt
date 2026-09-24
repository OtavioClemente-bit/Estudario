package br.com.estudario.data.remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SupabaseAuthRepositoryTest {
    @Test
    fun missingSessionIsUnauthenticatedAndHasNoAiToken() = runTest {
        val repository = repository()

        assertNull(repository.observeSession().first())
        assertNull(repository.accessToken())
    }

    @Test
    fun googleSignInStoresOnlyTheSupabaseSessionForAi() = runTest {
        val client = FakeSupabaseAuthClient(
            session = SupabaseSession(accessToken = "supabase-jwt-token", userId = "user-1"),
        )
        val repository = repository(client)

        repository.signInWithGoogle(SupabaseGoogleCredential("google-id-token"))

        assertEquals("supabase-jwt-token", repository.accessToken())
        assertEquals("supabase-jwt-token", SupabaseAiTokenProvider(repository).accessToken())
        assertNotEquals("google-drive-access-token", SupabaseAiTokenProvider(repository).accessToken())
        assertEquals("google-id-token", client.googleCredentials.single().idToken)
    }

    @Test
    fun emailOtpVerificationPersistsTheSupabaseSession() = runTest {
        val client = FakeSupabaseAuthClient(
            session = SupabaseSession(accessToken = "otp-supabase-jwt", userId = "user-2"),
        )
        val repository = repository(client)

        repository.sendEmailOtp("person@example.com")
        repository.verifyEmailOtp("person@example.com", "123456")

        assertEquals(listOf("person@example.com"), client.otpEmails)
        assertEquals(listOf("person@example.com" to "123456"), client.verifiedOtps)
        assertEquals("otp-supabase-jwt", repository.accessToken())
    }

    @Test
    fun signOutClearsTheLocalSupabaseSession() = runTest {
        val repository = repository(
            FakeSupabaseAuthClient(
                session = SupabaseSession(accessToken = "supabase-jwt-token"),
            ),
        )
        repository.signInWithGoogle(SupabaseGoogleCredential("google-id-token"))

        repository.signOut()

        assertNull(repository.observeSession().first())
        assertNull(repository.accessToken())
    }

    @Test
    fun signOutClearsLocalStateEvenWhenRemoteSignOutFails() = runTest {
        val client = FakeSupabaseAuthClient(
            session = SupabaseSession(accessToken = "supabase-jwt-token"),
            signOutFailure = IllegalStateException("network unavailable"),
        )
        val repository = repository(client)
        repository.signInWithGoogle(SupabaseGoogleCredential("google-id-token"))

        runCatching { repository.signOut() }

        assertNull(repository.observeSession().first())
        assertNull(repository.accessToken())
    }

    private fun repository(client: SupabaseAuthClient = FakeSupabaseAuthClient()): SupabaseAuthRepository =
        DefaultSupabaseAuthRepository(client, FakeSupabaseSessionStore())
}

private class FakeSupabaseSessionStore(initial: SupabaseSession? = null) : SupabaseSessionStore {
    private val state = MutableStateFlow(initial)

    override fun observe(): StateFlow<SupabaseSession?> = state

    override fun current(): SupabaseSession? = state.value

    override suspend fun save(session: SupabaseSession) {
        state.value = session
    }

    override suspend fun clear() {
        state.value = null
    }
}

private class FakeSupabaseAuthClient(
    private val session: SupabaseSession? = null,
    private val signOutFailure: Throwable? = null,
) : SupabaseAuthClient {
    val googleCredentials = mutableListOf<SupabaseGoogleCredential>()
    val otpEmails = mutableListOf<String>()
    val verifiedOtps = mutableListOf<Pair<String, String>>()

    override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession =
        session.also { googleCredentials += credential } ?: error("fake session not configured")

    override suspend fun sendEmailOtp(email: String) {
        otpEmails += email
    }

    override suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession =
        session.also { verifiedOtps += email to token } ?: error("fake session not configured")

    override suspend fun signOut() {
        signOutFailure?.let { throw it }
    }
}
