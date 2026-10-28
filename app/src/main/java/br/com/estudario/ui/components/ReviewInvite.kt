package br.com.estudario.ui.components

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.ui.AppReview
import kotlinx.coroutines.launch

/**
 * "Está curtindo estudar com o Estudário?", com o Folha, no dia do plano concluído. Quem está amando
 * vai para a avaliação oficial da Google Play; quem acha que pode melhorar conta o quê (vai para a
 * equipe, nos reportes) e não é mandado para a loja. Regras de quando aparece: AppReview.shouldInvite.
 */
@Composable
fun ReviewInvite(onDone: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var step by remember { mutableStateOf("ask") }
    var text by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDone) {
        Column(
            Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp)).verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when (step) {
                "ask" -> {
                    br.com.estudario.ui.assistant.Folha(110.dp, mood = br.com.estudario.ui.assistant.FolhaMood.PROUD)
                    Text("Dia do plano concluído!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                    Text(
                        "Mandou bem hoje. Me conta uma coisa: está curtindo estudar com o Estudário?",
                        style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
                    )
                    br.com.estudario.ui.brand.Button(
                        onClick = { AppReview.launchPlayReview(context); onDone() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) { Text("Estou amando!", fontWeight = FontWeight.Bold) }
                    br.com.estudario.ui.brand.OutlinedButton(onClick = { step = "improve" }, modifier = Modifier.fillMaxWidth()) { Text("Pode melhorar") }
                    TextButton(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("Agora não") }
                }
                "improve" -> {
                    br.com.estudario.ui.assistant.Folha(96.dp, mood = br.com.estudario.ui.assistant.FolhaMood.THINKING)
                    Text("O que eu posso melhorar?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                    Text(
                        "A equipe lê cada mensagem. Pode falar de tudo: matérias, questões, plano, o que faltou.",
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
                    )
                    OutlinedTextField(text, { text = it.take(1000) }, Modifier.fillMaxWidth().height(140.dp), placeholder = { Text("Sua sugestão") })
                    br.com.estudario.ui.brand.Button(
                        onClick = {
                            AppReview.markImprove(context)
                            val app = context.applicationContext as br.com.estudario.EstudarioApplication
                            scope.launch {
                                app.contentReportClient.send(
                                    br.com.estudario.data.remote.ContentReport(
                                        kind = br.com.estudario.data.remote.ReportKind.OTHER,
                                        reason = br.com.estudario.data.remote.ReportReason.OTHER,
                                        excerpt = "Sugestão pelo convite de avaliação",
                                        comment = text,
                                    ),
                                )
                            }
                            step = "thanks"
                        },
                        enabled = text.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) { Text("Enviar para a equipe", fontWeight = FontWeight.Bold) }
                    TextButton(onClick = { AppReview.markImprove(context); onDone() }, modifier = Modifier.fillMaxWidth()) { Text("Prefiro não dizer") }
                }
                else -> {
                    br.com.estudario.ui.assistant.Folha(110.dp, mood = br.com.estudario.ui.assistant.FolhaMood.ECSTATIC)
                    Text("Valeu demais!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                    Text("Sua sugestão chegou na equipe. Bora para o próximo dia!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    br.com.estudario.ui.brand.Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("Fechar") }
                }
            }
        }
    }
}
