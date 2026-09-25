package br.com.estudario.data.remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
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
            session = SupabaseSession(accessToken = "supabase-jwt-token", userId = "user-1", expiresAtEpochSeconds = 2_000_000_000),
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
            session = SupabaseSession(accessToken = "supabase-jwt-token", userId = "user-1", expiresAtEpochSeconds = 2_000_000_000),
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
            session = SupabaseSession(accessToken = "otp-supabase-jwt", userId = "user-2", expiresAtEpochSeconds = 2_000_000_000),
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
        val client = FakeSupabaseAuthClient(
            session = SupabaseSession(accessToken = "supabase-jwt-token", expiresAtEpochSeconds = 2_000_000_000),
        )
        val repository = repository(client)
        repository.signInWithGoogle(SupabaseGoogleCredential("google-id-token"))

        repository.signOut()

        assertEquals(listOf("supabase-jwt-token"), client.signOutTokens)
        assertEquals(SupabaseSessionState.Ready(null), repository.observeSession().first())
        assertNull(repository.accessToken())
    }

    @Test
    fun signOutClearsLocalStateEvenWhenRemoteSignOutFails() = runTest {
        val client = FakeSupabaseAuthClient(
            session = SupabaseSession(accessToken = "supabase-jwt-token", expiresAtEpochSeconds = 2_000_000_000),
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

    @Test
    fun expiredOrUnknownExpiryNeverProvidesAiToken() = runTest {
        val expired = repository(initialState = SupabaseSessionState.Ready(
            SupabaseSession("expired-jwt", expiresAtEpochSeconds = 1),
        ))
        val unknown = repository(initialState = SupabaseSessionState.Ready(SupabaseSession("unknown-jwt")))

        assertNull(SupabaseAiTokenProvider(expired).accessToken())
        assertNull(SupabaseAiTokenProvider(unknown).accessToken())
    }

    @Test
    fun observedReadySessionBecomesUnauthenticatedAtExpiry() = runTest {
        var nowSeconds = 1_000L
        val store = FakeSupabaseSessionStore(SupabaseSessionState.Ready(
            SupabaseSession("supabase-jwt", expiresAtEpochSeconds = 1_002),
        ))
        val repository = DefaultSupabaseAuthRepository(
            FakeSupabaseAuthClient(), store, clockSeconds = { nowSeconds },
        )
        val states = mutableListOf<SupabaseSessionState>()
        val observer = launch { repository.observeSession().take(2).toList(states) }
        runCurrent()
        assertEquals(
            SupabaseSessionState.Ready(SupabaseSession("supabase-jwt", expiresAtEpochSeconds = 1_002)),
            states.single(),
        )

        advanceTimeBy(2_000)
        nowSeconds = 1_002L
        runCurrent()

        assertEquals(SupabaseSessionState.Ready(null), states.last())
        assertNull(repository.accessToken())
        observer.join()
    }

    @Test
    fun veryLargeFutureExpiryStaysObservedReadyWhileTokenIsValid() = runTest {
        val session = SupabaseSession("supabase-jwt", expiresAtEpochSeconds = Long.MAX_VALUE)
        val repository = DefaultSupabaseAuthRepository(
            FakeSupabaseAuthClient(),
            FakeSupabaseSessionStore(SupabaseSessionState.Ready(session)),
            clockSeconds = { 1_000L },
        )
        val states = mutableListOf<SupabaseSessionState>()
        val observer = backgroundScope.launch { repository.observeSession().collect { states += it } }

        runCurrent()

        assertEquals(listOf(SupabaseSessionState.Ready(session)), states)
        assertEquals("supabase-jwt", repository.accessToken())
        observer.cancel()
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
    val signOutTokens = mutableListOf<String>()

    override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential): SupabaseSession =
        session.also { googleCredentials += credential } ?: error("fake session not configured")

    override suspend fun sendEmailOtp(email: String) {
        otpEmails += email
    }

    override suspend fun verifyEmailOtp(email: String, token: String): SupabaseSession =
        session.also { verifiedOtps += email to token } ?: error("fake session not configured")

    override suspend fun signOut(accessToken: String) {
        signOutTokens += accessToken
        signOutFailure?.let { throw it }
    }
}
