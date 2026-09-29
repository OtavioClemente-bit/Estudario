package br.com.estudario.ui.ai

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import br.com.estudario.BuildConfig

/**
 * Pede ao Android a conta Google da pessoa (a tela padrão do sistema, com as contas do celular) e
 * devolve o ID token que o Supabase troca por uma sessão da conta Estudário.
 */
object GoogleAccountSignIn {
    suspend fun idToken(context: Context): Result<String> = runCatching {
        val option = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val credential = try {
            CredentialManager.create(context).getCredential(context, request).credential
        } catch (_: GetCredentialCancellationException) {
            throw IllegalStateException("Login cancelado.")
        } catch (_: NoCredentialException) {
            throw IllegalStateException("Nenhuma conta Google neste celular. Adicione uma nas configurações do Android.")
        } catch (error: Exception) {
            throw IllegalStateException("Não foi possível abrir o login do Google agora.", error)
        }
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            GoogleIdTokenCredential.createFrom(credential.data).idToken
        } else {
            throw IllegalStateException("O Google não devolveu a conta. Tente de novo.")
        }
    }
}
