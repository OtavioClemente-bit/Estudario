package br.com.estudario.data.remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class SupabaseAuthRepositoryTest {
    @Test
    fun publicSupabaseSessionDoesNotExposeRefreshToken() {
        assertFalse(SupabaseSession::class.java.declaredFields.any { it.name == "refreshToken" })
    }

    @Test
    fun missingSessionIsUnauthenticatedAndHasNoAiToken() = runTest {
        val repository = repository()

        assertEquals(SupabaseSessionState.Ready(null), repository.observeSession().first())
        assertNull(repository.accessToken())
    }

    @Test
    fun loadingSessionIsNotReportedAsUnauthenticated() = runTest {
        val repository = repository(initialState = SupabaseSessionState.Loading)

        assertEquals(SupabaseSessionState.Loading, repository.observeSession().first())
        assertNull(repository.accessToken())
    }

    @Test
    fun googleSignInStoresOnlyTheSupabaseSessionForAi() = runTest {
        val client = FakeSupabaseAuthClient(
            session = SupabaseSession(accessToken = "supabase-jwt-token", userId = "user-1"),
        )
        val repository = repository(client)
        val driveTokenSource = DriveAccessTokenSource { "google-drive-access-token" }

        repository.signInWithGoogle(SupabaseGoogleCredential("google-id-token"))

        assertEquals("supabase-jwt-token", repository.accessToken())
        assertEquals("supabase-jwt-token", SupabaseAiTokenProvider(repository).accessToken())
        assertEquals("google-drive-access-token", driveTokenSource.accessToken())
        assertNotEquals(driveTokenSource.accessToken(), SupabaseAiTokenProvider(repository).accessToken())
        assertEquals("google-id-token", client.googleCredentials.single().idToken)
    }

    @Test
    fun concreteGoogleDriveTokenAdapterCannotProvideTheDriveTokenToAi() = runTest {
        val client = FakeSupabaseAuthClient(
            session = SupabaseSession(accessToken = "supabase-jwt-token", userId = "user-1"),
        )
        val repository = repository(client)
        val driveTokenSource = GoogleDriveAccessTokenSource { "google-drive-access-token" }

        repository.signInWithGoogle(SupabaseGoogleCredential("google-id-token"))

        assertEquals("google-drive-access-token", driveTokenSource.accessToken())
        assertEquals("supabase-jwt-token", SupabaseAiTokenProvider(repository).accessToken())
        assertNotEquals(driveTokenSource.accessToken(), SupabaseAiTokenProvider(repository).accessToken())
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

        assertEquals(SupabaseSessionState.Ready(null), repository.observeSession().first())
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

        val result = runCatching { repository.signOut() }

        assertTrue(result.isFailure)
        assertSame(client.signOutFailure, result.exceptionOrNull())
        assertEquals(SupabaseSessionState.Ready(null), repository.observeSession().first())
        assertNull(repository.accessToken())
    }

    private fun repository(
        client: SupabaseAuthClient = FakeSupabaseAuthClient(),
        initialState: SupabaseSessionState = SupabaseSessionState.Ready(null),
    ): SupabaseAuthRepository =
        DefaultSupabaseAuthRepository(client, FakeSupabaseSessionStore(initialState))
}

private class FakeSupabaseSessionStore(initial: SupabaseSessionState) : SupabaseSessionStore {
    private val state = MutableStateFlow(initial)

    override fun observe(): StateFlow<SupabaseSessionState> = state

    override fun current(): SupabaseSessionState = state.value

    override suspend fun save(session: SupabaseSession) {
        state.value = SupabaseSessionState.Ready(session)
    }

    override suspend fun clear() {
        state.value = SupabaseSessionState.Ready(null)
    }
}

private class FakeSupabaseAuthClient(
    private val session: SupabaseSession? = null,
    val signOutFailure: Throwable? = null,
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
