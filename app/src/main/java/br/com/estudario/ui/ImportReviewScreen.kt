package br.com.estudario.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.data.transfer.ImportMode
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors

/**
 * Revisão de um pacote .estudo antes de salvar: o que chega, em que tópico, e os avisos que
 * importam. Tela cheia, com a ação principal sempre à vista no rodapé.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImportReviewScreen(state: TransferState.Preview, onConfirm: (ImportMode?, Boolean) -> Unit, onCancel: () -> Unit) {
    val preview = state.value
    val duplicate = preview.packageAlreadyImported || preview.duplicateCount > 0
    var markStudied by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onCancel, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        BackHandler(onBack = onCancel)
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onCancel) { Icon(Icons.Outlined.Close, "Cancelar") }
                    Text("Revisar antes de salvar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                }
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Resumo do pacote
                    Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text("MATERIAL PARA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(preview.competition, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text(
                                "${preview.subjects.size} matéria(s) · ${preview.topicCount} tópico(s)" + if (preview.subtopicCount > 0) " · ${preview.subtopicCount} subtópico(s)" else "",
                                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                Stat(Icons.Outlined.AutoStories, preview.theoryCount, "teorias", Modifier.weight(1f))
                                Stat(Icons.Outlined.Summarize, preview.summaryCount, "resumos", Modifier.weight(1f))
                                Stat(Icons.Outlined.Quiz, preview.questionCount, "questões", Modifier.weight(1f))
                                Stat(Icons.Outlined.Style, preview.snippetCount, "revisão", Modifier.weight(1f))
                            }
                        }
                    }

                    if (duplicate) Notice(Icons.Outlined.Info, MaterialTheme.colorScheme.primary, "Você já importou este material. Atualize para trocar pelo novo mantendo seu histórico (respostas, marcações e revisões), ou crie uma cópia separada.")
                    if (preview.downgradedQuestions > 0) Notice(
                        Icons.Outlined.WarningAmber, estudarioColors().attention,
                        "${preview.downgradedQuestions} questão(ões) vieram como de prova real sem dizer de onde. Elas entram como autorais, sem banca nem ano.",
                    )
                    if (preview.sourceCount > 0) Notice(Icons.Outlined.Link, estudarioColors().completed, "Feito a partir de ${preview.sourceCount} fonte(s) consultada(s). Cada uma fica salva no tópico, com o link, para você conferir quando quiser.")

                    Text("O que entra", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    preview.subjects.forEach { subject ->
                        Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(subject.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                val withContent = subject.topics.filter { it.theoryCount + it.summaryCount + it.questionCount + it.snippetCount > 0 }
                                if (withContent.isEmpty()) {
                                    Text("${subject.topicCount} tópico(s) na estrutura do edital, sem material novo.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                withContent.take(12).forEach { topic ->
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(start = (topic.depth * 12).dp)) {
                                        Icon(Icons.Outlined.CheckCircle, null, Modifier.size(18.dp).padding(top = 2.dp), tint = estudarioColors().completed)
                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(topic.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                if (topic.theoryCount > 0) Pill("${topic.theoryCount} teoria")
                                                if (topic.summaryCount > 0) Pill("${topic.summaryCount} resumo(s)")
                                                if (topic.questionCount > 0) Pill("${topic.questionCount} questões")
                                                if (topic.snippetCount > 0) Pill("${topic.snippetCount} de revisão")
                                            }
                                        }
                                    }
                                }
                                if (withContent.size > 12) Text("+ ${withContent.size - 12} tópico(s)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                    Spacer(Modifier.heightIn(min = 8.dp))
                }

                // Rodapé fixo: a decisão sempre à vista.
                Surface(tonalElevation = 3.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!duplicate) Row(
                            Modifier.fillMaxWidth().clickable { markStudied = !markStudied },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(checked = markStudied, onCheckedChange = { markStudied = it })
                            Column {
                                Text("Já estudei este conteúdo", style = MaterialTheme.typography.bodyMedium)
                                Text("Marca os tópicos como estudados e agenda as revisões.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        if (duplicate) {
                            Button(onClick = { onConfirm(ImportMode.UPDATE, false) }, Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Atualizar e manter meu histórico") }
                            OutlinedButton(onClick = { onConfirm(ImportMode.COPY, false) }, Modifier.fillMaxWidth()) { Text("Criar uma cópia separada") }
                        } else {
                            Button(onClick = { onConfirm(null, markStudied) }, Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Salvar no Estudário") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Stat(icon: ImageVector, value: Int, label: String, modifier: Modifier = Modifier) {
    Surface(shape = EstudarioShapes.row, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f), modifier = modifier) {
        Column(Modifier.padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
            Text("$value", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}

@Composable
private fun Notice(icon: ImageVector, tint: Color, text: String) {
    Surface(shape = EstudarioShapes.row, color = tint.copy(alpha = 0.12f), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, null, tint = tint)
            Text(text, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun Pill(text: String) {
    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
        Box(Modifier.padding(horizontal = 10.dp, vertical = 3.dp)) {
            Text(text, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}
