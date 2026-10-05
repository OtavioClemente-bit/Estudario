package br.com.estudario.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Renovação da sessão sem a janela do Google: a chave fica cifrada e fora da sessão pública. */
class SupabaseRefreshSessionTest {
    @Test
    fun googleSignInKeepsTheRefreshTokenAndRefreshRenewsSilently() = runTest {
        val client = RefreshingClient()
        val refresh = MemoryRefreshStore()
        val repository = DefaultSupabaseAuthRepository(client, MemorySessionStore(), clockSeconds = { 1_000L }, refreshStore = refresh)

        repository.signInWithGoogle(SupabaseGoogleCredential("google-id-token"))
        assertEquals("refresh-1", refresh.value)

        assertTrue(repository.refreshSession())
        assertEquals("refresh-1", client.usedRefreshTokens.single())
        assertEquals("access-2", repository.accessToken())
        // O servidor troca a chave a cada renovação; a nova substitui a anterior.
        assertEquals("refresh-2", refresh.value)
    }

    @Test
    fun rejectedRefreshTokenIsForgottenButNetworkFailureIsKept() = runTest {
        val refresh = MemoryRefreshStore("old")
        val rejected = DefaultSupabaseAuthRepository(
            RefreshingClient(failure = SupabaseAuthException(SupabaseAuthException.Code.REQUEST_FAILED, 400)),
            MemorySessionStore(), refreshStore = refresh,
        )
        assertFalse(rejected.refreshSession())
        assertNull(refresh.value)

        val kept = MemoryRefreshStore("old")
        val offline = DefaultSupabaseAuthRepository(
            RefreshingClient(failure = SupabaseAuthException(SupabaseAuthException.Code.NETWORK_ERROR)),
            MemorySessionStore(), refreshStore = kept,
        )
        assertFalse(offline.refreshSession())
        assertEquals("old", kept.value)
    }

    @Test
    fun signOutForgetsTheRefreshToken() = runTest {
        val refresh = MemoryRefreshStore("refresh-1")
        val repository = DefaultSupabaseAuthRepository(RefreshingClient(), MemorySessionStore(), refreshStore = refresh)

        repository.signOut()

        assertNull(refresh.value)
        assertFalse(repository.refreshSession())
    }

    @Test
    fun storedRefreshTokenIsEncryptedAndUnreadableCipherTextIsDropped() = runTest {
        val dataStore = MemoryPreferencesDataStore()
        val store = DataStoreSupabaseRefreshTokenStore(dataStore, ReversingCipher())

        store.save("refresh-secret")
        val raw = dataStore.data.value.asMap().values.single() as String
        assertFalse(raw.contains("refresh-secret"))
        assertEquals("refresh-secret", store.read())

        val broken = DataStoreSupabaseRefreshTokenStore(dataStore, object : TokenCipher {
            override fun encrypt(plain: String) = plain
            override fun decrypt(encoded: String): String? = null
        })
        assertNull(broken.read())
        assertTrue(dataStore.data.value.asMap().isEmpty())
    }
}

private class RefreshingClient(private val failure: Exception? = null) : SupabaseAuthClient {
    val usedRefreshTokens = mutableListOf<String>()

    override suspend fun signInWithGoogle(credential: SupabaseGoogleCredential) = signInWithGoogleTokens(credential).session

    override suspend fun signInWithGoogleTokens(credential: SupabaseGoogleCredential) =
        SupabaseTokens(SupabaseSession("access-1", "user-1", 5_000L), "refresh-1")

    override suspend fun refreshSession(refreshToken: String): SupabaseTokens {
        failure?.let { throw it }
        usedRefreshTokens += refreshToken
        return SupabaseTokens(SupabaseSession("access-2", "user-1", 9_000L), "refresh-2")
    }

    override suspend fun sendEmailOtp(email: String) = Unit
    override suspend fun verifyEmailOtp(email: String, token: String) = SupabaseSession("otp", "user-1", 5_000L)
    override suspend fun signOut(accessToken: String) = Unit
}

private class MemorySessionStore : SupabaseSessionStore {
    private val state = MutableStateFlow<SupabaseSessionState>(SupabaseSessionState.Ready(null))
    override fun observe(): StateFlow<SupabaseSessionState> = state
    override fun current(): SupabaseSessionState = state.value
    override suspend fun save(session: SupabaseSession) { state.value = SupabaseSessionState.Ready(session) }
    override suspend fun clear() { state.value = SupabaseSessionState.Ready(null) }
}

private class MemoryRefreshStore(var value: String? = null) : SupabaseRefreshTokenStore {
    override suspend fun read() = value
    override suspend fun save(refreshToken: String) { value = refreshToken }
    override suspend fun clear() { value = null }
}

/** Cifra de teste: inverte o texto (o Keystore real só existe no aparelho). */
private class ReversingCipher : TokenCipher {
    override fun encrypt(plain: String) = "enc:" + plain.reversed()
    override fun decrypt(encoded: String) = encoded.removePrefix("enc:").reversed()
}

private class MemoryPreferencesDataStore : DataStore<Preferences> {
    override val data = MutableStateFlow(emptyPreferences())
    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences =
        transform(data.value).also { data.value = it }
}
