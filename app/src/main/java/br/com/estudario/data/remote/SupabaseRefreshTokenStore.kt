package br.com.estudario.data.remote

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlinx.coroutines.flow.first

/**
 * Guarda a chave de renovação da sessão (refresh token) para o app renovar o login sozinho, sem a
 * janela do Google a cada abertura. Ela fica fora da sessão pública ([SupabaseSession]) e só é
 * gravada cifrada: [TokenCipher] usa uma chave do Android Keystore que não sai do aparelho.
 */
interface SupabaseRefreshTokenStore {
    suspend fun read(): String?

    suspend fun save(refreshToken: String)

    suspend fun clear()
}

/** Cifra e decifra texto curto. A implementação de produção usa o Android Keystore. */
interface TokenCipher {
    fun encrypt(plain: String): String

    /** Null quando o texto não pode mais ser decifrado (chave apagada ou dado corrompido). */
    fun decrypt(encoded: String): String?
}

private val Context.supabaseRefreshDataStore by preferencesDataStore(name = "supabase_auth_refresh")

class DataStoreSupabaseRefreshTokenStore(
    private val dataStore: DataStore<Preferences>,
    private val cipher: TokenCipher,
) : SupabaseRefreshTokenStore {
    constructor(context: Context) : this(context.supabaseRefreshDataStore, KeystoreTokenCipher())

    private val key = stringPreferencesKey("refresh_token_encrypted")

    override suspend fun read(): String? {
        val stored = dataStore.data.first()[key] ?: return null
        val plain = runCatching { cipher.decrypt(stored) }.getOrNull()
        if (plain.isNullOrBlank()) clear()
        return plain?.takeIf(String::isNotBlank)
    }

    override suspend fun save(refreshToken: String) {
        require(refreshToken.isNotBlank()) { "Refresh token must not be blank." }
        val encrypted = cipher.encrypt(refreshToken)
        dataStore.edit { it[key] = encrypted }
    }

    override suspend fun clear() {
        dataStore.edit { it.remove(key) }
    }
}

/** AES-GCM com chave gerada e presa no Android Keystore. */
class KeystoreTokenCipher(private val alias: String = "estudario_supabase_refresh") : TokenCipher {
    private fun key(): SecretKey {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (store.getEntry(alias, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(
            KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build(),
        )
        return generator.generateKey()
    }

    override fun encrypt(plain: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, key()) }
        val sealed = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
        val encoder = Base64.getEncoder()
        return encoder.encodeToString(cipher.iv) + ":" + encoder.encodeToString(sealed)
    }

    override fun decrypt(encoded: String): String? {
        val parts = encoded.split(':')
        if (parts.size != 2) return null
        val decoder = Base64.getDecoder()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, decoder.decode(parts[0])))
        return cipher.doFinal(decoder.decode(parts[1])).toString(Charsets.UTF_8)
    }

    private companion object {
        const val TRANSFORMATION = "AES/GCM/NoPadding"
    }
}
