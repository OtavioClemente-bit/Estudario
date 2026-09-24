package br.com.estudario.ui.ai

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.estudario.ui.prompt.attachmentFor

@Composable
fun AiReviewEntryPoint(target: AiReviewTarget, onClose: () -> Unit) {
    val context = LocalContext.current
    val application = context.applicationContext as android.app.Application
    val reviewViewModel: AiReviewViewModel = viewModel(
        key = "ai-review-${target.id}",
        factory = remember(target.id, target.title) { AiReviewViewModelFactory(application, target.id, target.title) },
    )
    val state by reviewViewModel.state.collectAsState()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            val attachment = context.attachmentFor(it)
            reviewViewModel.start(attachment.uri.toString(), attachment.name)
        }
    }
    AiReviewScreen(
        state = state,
        onLogin = reviewViewModel::onLoginReturned,
        onPickSource = { picker.launch(arrayOf("application/pdf")) },
        onDraftChange = reviewViewModel::changeDraft,
        onApply = reviewViewModel::apply,
        onConfirmReplacement = reviewViewModel::confirmReplacement,
        onCancelReplacement = reviewViewModel::cancelReplacement,
        onRetry = reviewViewModel::retry,
        onFallback = onClose,
        onClose = onClose,
    )
}
