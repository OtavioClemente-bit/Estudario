package br.com.estudario.ui.profile

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/** O que fazer assim que a autorização do Google voltar. */
enum class GoogleAction { SIGN_IN, BACKUP, RESTORE }

private const val DRIVE_APPDATA = "https://www.googleapis.com/auth/drive.appdata"

/**
 * Autorização do Google em um passo só: pede perfil, e-mail e a pasta privada do app no Drive.
 * Na primeira vez o Google mostra a tela de consentimento; depois disso o token volta sem
 * interromper a pessoa, o que deixa o backup e a restauração com um toque.
 */
object GoogleAuthorization {
    private val scopes = listOf(
        Scope("https://www.googleapis.com/auth/userinfo.profile"),
        Scope("https://www.googleapis.com/auth/userinfo.email"),
        Scope(DRIVE_APPDATA),
    )

    suspend fun authorize(context: Context): AuthorizationResult =
        Identity.getAuthorizationClient(context)
            .authorize(AuthorizationRequest.builder().setRequestedScopes(scopes).build())
            .await()

    fun fromIntent(context: Context, intent: Intent): AuthorizationResult =
        Identity.getAuthorizationClient(context).getAuthorizationResultFromIntent(intent)

    fun describe(error: Throwable): String = when {
        error is ApiException && error.statusCode == 10 ->
            "O app ainda não está registrado no Google Cloud com esta assinatura. Confira o cliente OAuth Android (pacote br.com.estudario e a impressão SHA-1) e tente de novo."
        error is ApiException && (error.statusCode == 16 || error.statusCode == 12501) -> "Login cancelado."
        error is ApiException -> "O Google recusou o login (código ${error.statusCode})."
        else -> error.message ?: "Não foi possível falar com o Google."
    }
}

/**
 * Devolve uma função que inicia a autorização. [onToken] recebe o token de acesso, já pronto para
 * ler o perfil e falar com o Drive.
 */
@Composable
fun rememberGoogleAuthorizer(onToken: (String) -> Unit, onError: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        val data = result.data
        if (data == null) {
            onError("Login cancelado.")
        } else {
            runCatching { GoogleAuthorization.fromIntent(context, data).accessToken }
                .onSuccess { token -> if (token.isNullOrBlank()) onError("O Google não devolveu a autorização.") else onToken(token) }
                .onFailure { onError(GoogleAuthorization.describe(it)) }
        }
    }
    return {
        scope.launch {
            runCatching { GoogleAuthorization.authorize(context) }
                .onSuccess { result ->
                    val pending = result.pendingIntent
                    when {
                        result.hasResolution() && pending != null -> launcher.launch(IntentSenderRequest.Builder(pending.intentSender).build())
                        !result.accessToken.isNullOrBlank() -> onToken(result.accessToken!!)
                        else -> onError("O Google não devolveu a autorização.")
                    }
                }
                .onFailure { onError(GoogleAuthorization.describe(it)) }
        }
    }
}
