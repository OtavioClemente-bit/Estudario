package br.com.meuconcurso

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.meuconcurso.ui.AppViewModel
import br.com.meuconcurso.ui.MeuConcursoApp
import br.com.meuconcurso.data.transfer.IncomingFileFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val uri = intent?.data?.takeIf { intent.action == Intent.ACTION_VIEW } ?: return
        lifecycleScope.launch {
            runCatching { withContext(Dispatchers.IO) { contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: error("Não foi possível abrir o arquivo.") } }
                .onSuccess { raw ->
                    when (IncomingFileFormat.detect(raw)) {
                        IncomingFileFormat.ESTUDO -> viewModel.inspectEstudo(raw)
                        IncomingFileFormat.PLANO -> (application as MeuConcursoApplication).incomingFiles.publishPlan(raw)
                        IncomingFileFormat.BACKUP -> viewModel.reportIncomingFileError("Use Mais > Restaurar backup para confirmar a substituição dos dados locais.")
                        null -> viewModel.reportIncomingFileError("Arquivo não reconhecido. Use um .estudo, .plano ou backup válido.")
                    }
                }
        }
    }

    private fun handleNotification(intent: Intent?) {
        intent?.getStringExtra("notification_destination")?.let(viewModel::openFromNotification)
    }
}
