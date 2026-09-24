package br.com.estudario.ui.ai

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.estudario.EstudarioApplication
import br.com.estudario.ui.setup.AiPdfUriPermission
import br.com.estudario.ui.setup.ContentResolverAiPdfUriPermission
import br.com.estudario.ui.setup.InitialSetupAiPdfSource
import br.com.estudario.ui.prompt.attachmentFor
import kotlinx.coroutines.launch

/** Boundary around the Android document picker so the result callback remains testable. */
fun interface AiReviewPdfPicker {
    fun launch(onResult: (Uri?) -> Unit)
}

@Composable
private fun rememberAiReviewPdfPicker(): AiReviewPdfPicker {
    val callback = remember { mutableStateOf<(Uri?) -> Unit>({}) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        callback.value(uri)
    }
    return remember(launcher) {
        AiReviewPdfPicker { onResult ->
            callback.value = onResult
            launcher.launch(InitialSetupAiPdfSource.PICKER_MIME_TYPES)
        }
    }
}

@Composable
fun AiReviewEntryPoint(
    target: AiReviewTarget,
    onClose: () -> Unit,
    onLoginRequested: ((onReturned: () -> Unit) -> Unit)? = null,
    onLocalApplied: () -> Unit = {},
    onSyncAck: () -> Unit = {},
    reviewViewModel: AiReviewViewModel? = null,
    pdfPicker: AiReviewPdfPicker? = null,
    pdfPermission: AiPdfUriPermission? = null,
) {
    val context = LocalContext.current
    val application = context.applicationContext as EstudarioApplication
    val scope = rememberCoroutineScope()
    val authRepository = application.supabaseAuthRepository
    var loginOpen by rememberSaveable { mutableStateOf(false) }
    var otpSent by rememberSaveable { mutableStateOf(false) }
    var loginEmail by rememberSaveable { mutableStateOf("") }
    var loginCode by rememberSaveable { mutableStateOf("") }
    var loginBusy by rememberSaveable { mutableStateOf(false) }
    var loginError by rememberSaveable { mutableStateOf<String?>(null) }
    var sourceError by rememberSaveable { mutableStateOf<String?>(null) }
    var loginContinuation by remember { mutableStateOf<(() -> Unit)?>(null) }
    val localLoginLauncher = remember {
        AiReviewLoginLauncher { onReturned ->
            loginContinuation = onReturned
            loginOpen = true
            otpSent = false
            loginError = null
        }
    }
    val actualReviewViewModel: AiReviewViewModel = reviewViewModel ?: viewModel(
        key = "ai-review-${target.id}",
        factory = remember(target.id, target.title, onLoginRequested) {
            AiReviewViewModelFactory(
                application,
                target.id,
                target.title,
                onLoginRequested?.let(::AiReviewLoginLauncher) ?: localLoginLauncher,
            )
        },
    )
    val state by actualReviewViewModel.state.collectAsState()
    LaunchedEffect(target.sourceUri, target.sourceName) {
        target.sourceUri?.let { actualReviewViewModel.provideSource(it, target.sourceName) }
    }
    val sourcePicker = pdfPicker ?: rememberAiReviewPdfPicker()
    val sourcePermission = pdfPermission ?: remember(context) { ContentResolverAiPdfUriPermission(context.contentResolver) }
    val onPdfResult: (Uri?) -> Unit = { uri ->
        if (uri != null) {
            runCatching {
                val persistedUri = InitialSetupAiPdfSource.persist(uri, sourcePermission)
                val attachment = context.attachmentFor(persistedUri)
                actualReviewViewModel.start(attachment.uri.toString(), attachment.name)
            }.onFailure {
                sourceError = "Não foi possível manter acesso ao PDF selecionado."
            }
        }
    }
    AiReviewScreen(
        state = state,
        sourceError = sourceError,
        onLogin = actualReviewViewModel::requestLogin,
        onPickSource = { sourcePicker.launch(onPdfResult) },
        onDraftChange = actualReviewViewModel::changeDraft,
        onApply = actualReviewViewModel::apply,
        onConfirmReplacement = actualReviewViewModel::confirmReplacement,
        onCancelReplacement = actualReviewViewModel::cancelReplacement,
        onRetry = actualReviewViewModel::retry,
        onFallback = onClose,
        onClose = onClose,
        onLocalApplied = onLocalApplied,
        onSyncAck = onSyncAck,
    )
    if (loginOpen) {
        AlertDialog(
            onDismissRequest = { if (!loginBusy) loginOpen = false },
            title = { Text("Entrar na conta Estudário") },
            text = {
                Column {
                    Text("O acesso da IA usa somente a sessão Supabase da sua conta.")
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = loginEmail,
                        onValueChange = { loginEmail = it },
                        enabled = !otpSent && !loginBusy,
                        label = { Text("E-mail") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (otpSent) {
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = loginCode,
                            onValueChange = { loginCode = it },
                            enabled = !loginBusy,
                            label = { Text("Código recebido") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    loginError?.let { message ->
                        Spacer(Modifier.height(8.dp))
                        Text(message)
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = !loginBusy && if (otpSent) loginCode.isNotBlank() else loginEmail.isNotBlank(),
                    onClick = {
                        scope.launch {
                            loginBusy = true
                            loginError = null
                            runCatching {
                                if (otpSent) {
                                    authRepository.verifyEmailOtp(loginEmail, loginCode)
                                    loginOpen = false
                                    loginContinuation?.invoke()
                                    loginContinuation = null
                                } else {
                                    authRepository.sendEmailOtp(loginEmail)
                                    otpSent = true
                                }
                            }.onFailure {
                                loginError = "Não foi possível concluir o login nesta configuração."
                            }
                            loginBusy = false
                        }
                    },
                ) { Text(if (otpSent) "Verificar código" else "Enviar código") }
            },
            dismissButton = {
                TextButton(enabled = !loginBusy, onClick = { loginOpen = false }) { Text("Cancelar") }
            },
        )
    }
}
