package br.com.estudario.data.remote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class SupabaseSession(
    val accessToken: String,
    val refreshToken: String? = null,
    val userId: String? = null,
    val expiresAtEpochSeconds: Long? = null,
) {
    init {
        require(accessToken.isNotBlank()) { "Supabase session access token must not be blank." }
    }
}

/** Loading and read failures are distinct from a successfully hydrated empty session. */
sealed interface SupabaseSessionState {
    data object Loading : SupabaseSessionState

    data class Ready(val session: SupabaseSession?) : SupabaseSessionState

    data class ReadError(val cause: Throwable) : SupabaseSessionState
}

interface SupabaseSessionStore {
    fun observe(): StateFlow<SupabaseSessionState>

    fun current(): SupabaseSessionState

    suspend fun save(session: SupabaseSession)

    suspend fun clear()
}

private val Context.supabaseSessionDataStore by preferencesDataStore(name = "supabase_auth_session")

/**
 * Persists only the Supabase access token, user id, and expiry. Refresh tokens stay memory-only
 * and any legacy refresh-token preference is removed on the next save.
 */
class DataStoreSupabaseSessionStore(
    private val dataStore: DataStore<Preferences>,
    scope: CoroutineScope,
) : SupabaseSessionStore {
    constructor(context: Context, scope: CoroutineScope) : this(context.supabaseSessionDataStore, scope)

    private val state = MutableStateFlow<SupabaseSessionState>(SupabaseSessionState.Loading)

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val legacyRefreshTokenKey = stringPreferencesKey("refresh_token")
    private val userIdKey = stringPreferencesKey("user_id")
    private val expiresAtKey = longPreferencesKey("expires_at_epoch_seconds")

    init {
        scope.launch {
            try {
                // Migrate any value written by the previous implementation before exposing state.
                dataStore.edit { preferences -> preferences.remove(legacyRefreshTokenKey) }
                dataStore.data
                    .map(::decode)
                    .collect { state.value = SupabaseSessionState.Ready(it) }
            } catch (error: Throwable) {
                if (error is CancellationException) throw error
                state.value = SupabaseSessionState.ReadError(error)
            }
        }
    }

    override fun observe(): StateFlow<SupabaseSessionState> = state

    override fun current(): SupabaseSessionState = state.value

    override suspend fun save(session: SupabaseSession) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = session.accessToken
            // Refresh tokens are intentionally memory-only in this task. Remove any value written
            // by an older implementation so the account-session boundary remains fail-closed.
            preferences.remove(legacyRefreshTokenKey)
            putOrRemove(preferences, userIdKey, session.userId)
            session.expiresAtEpochSeconds?.let { preferences[expiresAtKey] = it }
                ?: preferences.remove(expiresAtKey)
        }
        state.value = SupabaseSessionState.Ready(session)
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
        state.value = SupabaseSessionState.Ready(null)
    }

    private fun decode(preferences: Preferences): SupabaseSession? {
        val accessToken = preferences[accessTokenKey]?.takeIf(String::isNotBlank) ?: return null
        return SupabaseSession(
            accessToken = accessToken,
            userId = preferences[userIdKey],
            expiresAtEpochSeconds = preferences[expiresAtKey],
        )
    }

    private fun putOrRemove(preferences: MutablePreferences, key: Preferences.Key<String>, value: String?) {
        if (value == null) preferences.remove(key) else preferences[key] = value
    }
}
