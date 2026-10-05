package br.com.estudario.ui.ai

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import br.com.estudario.BuildConfig
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.account.GoogleDriveBackupService
import br.com.estudario.data.remote.SupabaseGoogleCredential
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File

/**
 * A única entrada do app: a conta Google do celular.
 *
 * Um toque abre a tela padrão do Android com as contas do aparelho; com a conta escolhida, o app
 * entra na conta Estudário (plano, uso da IA e editais) e preenche o perfil (nome, e-mail e foto).
 * O backup no Drive usa essa mesma conta: na primeira vez o Google só pede a permissão da pasta
 * privada do app, sem outro login.
 */
object GoogleAccountSignIn {
    val available: Boolean get() = BuildConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank()

    /** Entra e devolve o e-mail da conta. Erros vêm com mensagem pronta para a tela. */
    suspend fun signIn(context: Context): Result<String> = runCatching {
        val app = context.applicationContext as EstudarioApplication
        val google = credential(context)
        try {
            app.supabaseAuthRepository.signInWithGoogle(SupabaseGoogleCredential(google.idToken))
        } catch (error: Exception) {
            throw IllegalStateException("Não foi possível entrar agora. Confira a internet e tente de novo.", error)
        }
        val email = google.id
        val prefs = app.preferences
        prefs.setUserEmail(email)
        google.displayName?.takeIf { it.isNotBlank() && prefs.userName.first().isBlank() }?.let { prefs.setUserName(it) }
        if (prefs.userPhotoPath.first() == null) google.profilePictureUri?.let { savePhoto(app, it.toString()) }
        email
    }

    /** Sai da conta neste aparelho. O estudo salvo aqui e os backups no Drive continuam onde estão. */
    suspend fun signOut(context: Context) {
        val app = context.applicationContext as EstudarioApplication
        runCatching { app.supabaseAuthRepository.signOut() }
        app.preferences.setUserEmail("")
        runCatching { CredentialManager.create(context).clearCredentialState(androidx.credentials.ClearCredentialStateRequest()) }
    }

    /**
     * Mantém a pessoa conectada. A sessão do servidor vale cerca de uma hora; quando está para
     * vencer, o app a renova em segundo plano com a chave de renovação guardada cifrada no aparelho,
     * sem tela nenhuma. Só sem essa chave (primeiro uso depois da atualização, ou chave recusada) o
     * app pede ao Google um novo comprovante da conta, o que pode abrir a janela do Google por cima
     * do app; por isso isso acontece no máximo a cada 12 horas, contadas mesmo com o app fechado.
     * Quem saiu da conta de propósito (e-mail apagado) não é reconectado.
     */
    suspend fun renewIfNeeded(context: Context, marginSeconds: Long = 600) {
        if (!available) return
        val app = context.applicationContext as EstudarioApplication
        if (app.preferences.userEmail.first().isBlank()) return
        val repo = app.supabaseAuthRepository
        val expiresAt = repo.sessionExpiresAt()
        val now = System.currentTimeMillis() / 1_000
        if (repo.accessToken() != null && expiresAt != null && expiresAt - now > marginSeconds) return
        if (repo.refreshSession()) return
        val prefs = app.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE)
        if (now - prefs.getLong(LAST_SILENT_ATTEMPT, 0L) < SILENT_RETRY_SECONDS) return
        prefs.edit().putLong(LAST_SILENT_ATTEMPT, now).apply()
        runCatching {
            val google = silentCredential(context)
            repo.signInWithGoogle(SupabaseGoogleCredential(google.idToken))
        }
    }

    private suspend fun silentCredential(context: Context): GoogleIdTokenCredential {
        val option = com.google.android.libraries.identity.googleid.GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(true)
            .setAutoSelectEnabled(true)
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val credential = CredentialManager.create(context).getCredential(context, request).credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            return GoogleIdTokenCredential.createFrom(credential.data)
        }
        throw IllegalStateException("Sem credencial silenciosa.")
    }

    /** Só o ID token (usado onde a sessão é tratada por fora). */
    suspend fun idToken(context: Context): Result<String> = runCatching { credential(context).idToken }

    private suspend fun credential(context: Context): GoogleIdTokenCredential {
        val option = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val credential = try {
            CredentialManager.create(context).getCredential(context, request).credential
        } catch (_: GetCredentialCancellationException) {
            throw IllegalStateException(CANCELLED)
        } catch (_: NoCredentialException) {
            throw IllegalStateException("Nenhuma conta Google neste celular. Adicione uma nas configurações do Android.")
        } catch (error: Exception) {
            throw IllegalStateException("Não foi possível abrir o login do Google agora.", error)
        }
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            return GoogleIdTokenCredential.createFrom(credential.data)
        }
        throw IllegalStateException("O Google não devolveu a conta. Tente de novo.")
    }

    private suspend fun savePhoto(app: EstudarioApplication, url: String) {
        val target = File(app.filesDir, "profile_${System.currentTimeMillis()}.jpg")
        val saved = withContext(Dispatchers.IO) { runCatching { GoogleDriveBackupService().downloadPhoto(url, target) }.getOrDefault(false) }
        if (saved) app.preferences.setUserPhotoPath(target.absolutePath)
    }

    const val CANCELLED = "Login cancelado."
    private const val SILENT_RETRY_SECONDS = 12 * 60 * 60L
    private const val AUTH_PREFS = "estudario_auth"
    private const val LAST_SILENT_ATTEMPT = "last_silent_google_attempt"
}
