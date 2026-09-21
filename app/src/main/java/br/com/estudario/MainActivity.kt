package br.com.estudario

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.IntentCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.EstudarioApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Mantém a splash na tela só até sabermos se é a primeira abertura (mostra o tour)
        // ou uma abertura normal (vai direto para a navegação principal).
        splashScreen.setKeepOnScreenCondition { viewModel.hasCompletedOnboarding.value == null || viewModel.seenTours.value == null }
        setContent { EstudarioApp(viewModel) }
        // Ao girar a tela a Activity é recriada com o mesmo intent: não importa o arquivo duas vezes.
        if (savedInstanceState == null) {
            handleIncomingFile(intent)
            handleNotification(intent)
            handleWidgetAction(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingFile(intent)
        handleNotification(intent)
        handleWidgetAction(intent)
    }

    private fun handleWidgetAction(intent: Intent?) {
        val taskId = intent?.getStringExtra("START_FOCUS_TASK_ID")
        val topicId = intent?.getLongExtra("START_FOCUS_TOPIC_ID", 0L)
        val title = intent?.getStringExtra("START_FOCUS_TITLE")
        
        if (!taskId.isNullOrBlank() && topicId != null && topicId > 0L && !title.isNullOrBlank()) {
            viewModel.startFocus(title = title, topicId = topicId, taskId = taskId)
        }
    }

    /**
     * Aceita três caminhos:
     * - "Abrir com" (ACTION_VIEW) a partir do gerenciador de arquivos, Drive, WhatsApp etc.;
     * - "Compartilhar" um arquivo (ACTION_SEND + EXTRA_STREAM), ex.: o .estudo baixado no app da IA;
     * - "Compartilhar" o texto da resposta (ACTION_SEND + EXTRA_TEXT), ex.: o JSON copiado da conversa.
     */
    private fun handleIncomingFile(intent: Intent?) {
        intent ?: return
        val uri: Uri? = when (intent.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> IntentCompat.getParcelableExtra(intent, Intent.EXTRA_STREAM, Uri::class.java)
            else -> null
        }
        val sharedText = if (intent.action == Intent.ACTION_SEND) intent.getStringExtra(Intent.EXTRA_TEXT) else null
        if (uri == null && sharedText.isNullOrBlank()) return
        viewModel.beginIncomingFile()
        lifecycleScope.launch {
            val raw = if (uri != null) {
                runCatching {
                    withContext(Dispatchers.IO) { contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) } }
                }.getOrNull()
            } else sharedText
            if (raw.isNullOrBlank()) viewModel.reportIncomingFileError("Não foi possível ler o arquivo. Tente baixá-lo novamente e abrir pelo gerenciador de arquivos, ou use o botão Importar dentro do app.")
            else viewModel.openIncomingText(raw)
        }
    }

    private fun handleNotification(intent: Intent?) {
        intent?.getStringExtra("notification_destination")?.let(viewModel::openFromNotification)
    }
}
