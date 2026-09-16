package br.com.meuconcurso

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.meuconcurso.ui.AppViewModel
import br.com.meuconcurso.ui.MeuConcursoApp
import kotlinx.coroutines.launch
import androidx.core.content.IntentCompat
import android.net.Uri

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MeuConcursoApp(viewModel) }
        handleIncomingFile(intent)
        handleNotification(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingFile(intent)
        handleNotification(intent)
    }

    private fun handleIncomingFile(intent: Intent?) {
        val uri: Uri = when (intent?.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> IntentCompat.getParcelableExtra(intent, Intent.EXTRA_STREAM, Uri::class.java)
            else -> null
        } ?: return
        lifecycleScope.launch {
            (application as MeuConcursoApplication).incomingFiles.open(contentResolver, uri, intent?.type)
        }
    }

    private fun handleNotification(intent: Intent?) {
        intent?.getStringExtra("notification_destination")?.let(viewModel::openFromNotification)
    }
}
