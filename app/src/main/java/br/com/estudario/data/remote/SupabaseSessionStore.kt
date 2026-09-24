package br.com.estudario.data.remote

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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

interface SupabaseSessionStore {
    fun observe(): StateFlow<SupabaseSession?>

    fun current(): SupabaseSession?

    suspend fun save(session: SupabaseSession)

    suspend fun clear()
}

private val Context.supabaseSessionDataStore by preferencesDataStore(name = "supabase_auth_session")

class DataStoreSupabaseSessionStore(
    context: Context,
    scope: CoroutineScope,
) : SupabaseSessionStore {
    private val dataStore = context.supabaseSessionDataStore
    private val state = MutableStateFlow<SupabaseSession?>(null)

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val userIdKey = stringPreferencesKey("user_id")
    private val expiresAtKey = longPreferencesKey("expires_at_epoch_seconds")

    init {
        scope.launch {
            dataStore.data
                .catch { emit(emptyPreferences()) }
                .map(::decode)
                .collect { state.value = it }
        }
    }

    override fun observe(): StateFlow<SupabaseSession?> = state

    override fun current(): SupabaseSession? = state.value

    override suspend fun save(session: SupabaseSession) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = session.accessToken
            putOrRemove(preferences, refreshTokenKey, session.refreshToken)
            putOrRemove(preferences, userIdKey, session.userId)
            session.expiresAtEpochSeconds?.let { preferences[expiresAtKey] = it }
                ?: preferences.remove(expiresAtKey)
        }
        state.value = session
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
        state.value = null
    }

    private fun decode(preferences: Preferences): SupabaseSession? {
        val accessToken = preferences[accessTokenKey]?.takeIf(String::isNotBlank) ?: return null
        return SupabaseSession(
            accessToken = accessToken,
            refreshToken = preferences[refreshTokenKey],
            userId = preferences[userIdKey],
            expiresAtEpochSeconds = preferences[expiresAtKey],
        )
    }

    private fun putOrRemove(preferences: MutablePreferences, key: Preferences.Key<String>, value: String?) {
        if (value == null) preferences.remove(key) else preferences[key] = value
    }
}
