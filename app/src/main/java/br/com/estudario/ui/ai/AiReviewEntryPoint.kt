package br.com.estudario.ui.ai

import kotlinx.coroutines.flow.first
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
import br.com.estudario.ui.brand.Button
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
    onReopen: (() -> Unit)? = null,
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
        key = "ai-review-${target.id}-${target.entryId}",
        factory = remember(target, onLoginRequested) {
            AiReviewViewModelFactory(
                application,
                target.id,
                target.title,
                onLoginRequested?.let(::AiReviewLoginLauncher) ?: localLoginLauncher,
                target,
            )
        },
    )
    val state by actualReviewViewModel.state.collectAsState()
    val preferences by actualReviewViewModel.preferences.collectAsState()
    // Voltar do sistema fecha a tela da IA e devolve ao passo de onde ela foi aberta.
    // Fechar durante a análise não perde nada: ela segue em segundo plano e o aviso do topo reabre a revisão.
    val closeOrBackground: () -> Unit = {
        if ((state.content is AiReviewContent.Processing || state.content is AiReviewContent.Submitting) && onReopen != null) {
            val id = "edital:${target.id}"
            BackgroundAiTasks.start(id, target.title, "Edital", null, open = onReopen) {
                val done = actualReviewViewModel.state.first { it.content is AiReviewContent.Review || it.content is AiReviewContent.Failure }.content
                if (done is AiReviewContent.Failure) throw IllegalStateException(done.message)
                ""
            }
            BackgroundAiTasks.sendToBackground(id)
        }
        onClose()
    }
    androidx.activity.compose.BackHandler(enabled = !loginOpen) { closeOrBackground() }
    LaunchedEffect(target.sourceUri, target.sourceName) {
        if (reviewViewModel != null) {
            target.preferences?.let(actualReviewViewModel::updatePreferences)
            target.sourceUri?.let { actualReviewViewModel.provideSource(it, target.sourceName) }
        }
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
        onPickSource = { sourceError = null; sourcePicker.launch(onPdfResult) },
        onDraftChange = actualReviewViewModel::changeDraft,
        onApply = actualReviewViewModel::apply,
        onConfirmReplacement = actualReviewViewModel::confirmReplacement,
        onCancelReplacement = actualReviewViewModel::cancelReplacement,
        onRetry = actualReviewViewModel::retry,
        onFallback = closeOrBackground,
        onClose = closeOrBackground,
        onLocalApplied = onLocalApplied,
        onSyncAck = onSyncAck,
        preferences = preferences,
        onPreferencesChange = actualReviewViewModel::updatePreferences,
        onConfirmGeneration = actualReviewViewModel::confirmGeneration,
        onEditInformation = actualReviewViewModel::editInformation,
    )
    if (loginOpen) {
        AccountLoginDialog(
            onDismiss = { loginOpen = false },
            onSignedIn = {
                loginOpen = false
                loginContinuation?.invoke()
                loginContinuation = null
            },
        )
    }
}
