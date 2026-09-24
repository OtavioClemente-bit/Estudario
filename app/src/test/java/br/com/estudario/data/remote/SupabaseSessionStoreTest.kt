package br.com.estudario.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SupabaseSessionStoreTest {
    @Test
    fun coldStartReportsLoadingBeforePersistedDataIsReadable() {
        val dataStore = BlockingPreferencesDataStore()
        val scope = testScope()
        try {
            val store = DataStoreSupabaseSessionStore(dataStore, scope)

            assertEquals(SupabaseSessionState.Loading, store.current())

            dataStore.release.complete(Unit)
            assertEquals(SupabaseSessionState.Ready(null), awaitReady(store))
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun persistsReloadsAndClearsWithoutPersistingRefreshToken() {
        val dataStore = InMemoryPreferencesDataStore()
        val scope = testScope()
        try {
            runBlocking { dataStore.seedRefreshToken("legacy-refresh-token") }
            val store = DataStoreSupabaseSessionStore(dataStore, scope)
            val session = SupabaseSession(
                accessToken = "supabase-jwt-token",
                userId = "user-1",
                expiresAtEpochSeconds = 1_800_000_000,
            )

            assertEquals(SupabaseSessionState.Ready(null), awaitReady(store))
            assertNull(dataStore.refreshToken())
            runBlocking { store.save(session) }
            assertEquals("supabase-jwt-token", dataStore.accessToken())
            assertNull(dataStore.refreshToken())

            val reloaded = DataStoreSupabaseSessionStore(dataStore, scope)
            val restored = awaitReady(reloaded).session
            assertEquals(session, restored)

            runBlocking { store.clear() }

            assertEquals(SupabaseSessionState.Ready(null), awaitReady(reloaded) { it.session == null })
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun readFailureIsExposedAndNotCollapsedIntoUnauthenticated() {
        val scope = testScope()
        try {
            val store = DataStoreSupabaseSessionStore(FailingPreferencesDataStore(), scope)

            val state = awaitState(store) { it is SupabaseSessionState.ReadError }

            assertTrue(state is SupabaseSessionState.ReadError)
            assertNotEquals(SupabaseSessionState.Ready(null), state)
        } finally {
            scope.cancel()
        }
    }

    private fun testScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private fun awaitReady(
        store: SupabaseSessionStore,
        predicate: (SupabaseSessionState.Ready) -> Boolean = { true },
    ): SupabaseSessionState.Ready = runBlocking {
        withTimeout(2_000) {
            store.observe().first { it is SupabaseSessionState.Ready && predicate(it) } as SupabaseSessionState.Ready
        }
    }

    private fun awaitState(
        store: SupabaseSessionStore,
        predicate: (SupabaseSessionState) -> Boolean,
    ): SupabaseSessionState = runBlocking {
        withTimeout(2_000) { store.observe().first(predicate) }
    }
}

private class InMemoryPreferencesDataStore : DataStore<Preferences> {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val preferences = MutableStateFlow<Preferences>(emptyPreferences())

    override val data: Flow<Preferences> = preferences

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        val updated = transform(preferences.value)
        preferences.value = updated
        return updated
    }

    suspend fun seedRefreshToken(value: String) {
        updateData { current ->
            current.toMutablePreferences().apply { this[refreshTokenKey] = value }
        }
    }

    fun accessToken(): String? = preferences.value[accessTokenKey]

    fun refreshToken(): String? = preferences.value[refreshTokenKey]
}

private class BlockingPreferencesDataStore : DataStore<Preferences> {
    val release = CompletableDeferred<Unit>()

    override val data: Flow<Preferences> = flow {
        release.await()
        emit(emptyPreferences())
    }

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        release.await()
        return transform(emptyPreferences())
    }
}

private class FailingPreferencesDataStore : DataStore<Preferences> {
    override val data: Flow<Preferences> = flow {
        error("corrupt Supabase session data")
    }

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences =
        error("write not expected")
}
