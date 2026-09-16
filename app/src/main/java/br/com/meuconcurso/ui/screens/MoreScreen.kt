package br.com.meuconcurso.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.ui.AppViewModel
import br.com.meuconcurso.ui.components.ConfirmDialog
import br.com.meuconcurso.ui.components.ScreenTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Composable
fun MoreScreen(viewModel: AppViewModel, onSearch: () -> Unit, onReviews: () -> Unit, onQueue: () -> Unit, onStatistics: () -> Unit, onQuestionBank: () -> Unit, onImportGuide: () -> Unit, onNotifications: () -> Unit, onErrors: () -> Unit, onPlan: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val themeMode by viewModel.themeMode.collectAsState()
    val timer by viewModel.questionTimer.collectAsState()
    val defaultCount by viewModel.defaultQuestionCount.collectAsState()
    val showExplanation by viewModel.showExplanation.collectAsState()
    val reviewIntervals by viewModel.reviewIntervals.collectAsState()
    var pendingBackup by remember { mutableStateOf<String?>(null) }
    var restoreRaw by remember { mutableStateOf<String?>(null) }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { scope.launch { readText(context, it)?.let(viewModel::inspectEstudo) } }
    }
    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { scope.launch { restoreRaw = readText(context, it) } }
    }
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        val data = pendingBackup
        if (uri != null && data != null) scope.launch(Dispatchers.IO) { context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(data) } }
    }
    if (restoreRaw != null) ConfirmDialog("Restaurar backup?", "Os dados locais atuais serão substituídos pelo conteúdo deste backup. Essa ação não pode ser desfeita.", "Restaurar", onDismiss = { restoreRaw = null }) {
        viewModel.restoreBackup(restoreRaw!!); restoreRaw = null
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { ScreenTitle("Mais", "Ferramentas e configurações") }
        item { SectionTitle("Estudo") }
        item { MoreItem(Icons.Outlined.Search, "Pesquisa global", "Busque em todo o conteúdo offline", onSearch) }
        item { MoreItem(Icons.Outlined.EventRepeat, "Revisões espaçadas", "Agenda D+1, D+7 e D+30", onReviews) }
        item { MoreItem(Icons.Outlined.Reorder, "Fila de estudos", "Reorganize, pause e conclua blocos", onQueue) }
        item { MoreItem(Icons.Outlined.QueryStats, "Desempenho", "Pontos fortes e assuntos que pedem revisão", onStatistics) }
        item { MoreItem(Icons.Outlined.Quiz, "Banco de questões", "Busque, filtre, favorite e monte uma sessão", onQuestionBank) }
        item { MoreItem(Icons.Outlined.ErrorOutline, "Caderno de erros", "Revise erros e conceitos vinculados", onErrors) }
        item { MoreItem(Icons.Outlined.CalendarMonth, "Planos de estudo", "Criar, importar, exportar e gerenciar planos", onPlan) }
        item { MoreItem(Icons.Outlined.NotificationsActive, "Notificações de estudo", "Horário, pendências, revisões e teste de aviso", onNotifications) }
        item { SectionTitle("Conteúdo e segurança") }
        item { MoreItem(Icons.Outlined.AutoAwesome, "Gerar estudo com IA", "Use GPT, Claude, Gemini ou outra IA com prompt pronto", onImportGuide) }
        item { MoreItem(Icons.Outlined.FileOpen, "Importar pacote .estudo", "Edital, livros, resumos e questões") { importLauncher.launch(arrayOf("application/json", "application/octet-stream", "text/plain")) } }
        item { MoreItem(Icons.Outlined.CloudDownload, "Exportar backup completo", "Salve uma cópia versionada no local que escolher") {
            scope.launch { pendingBackup = viewModel.createBackup(); exportLauncher.launch("meu-concurso-backup-${LocalDate.now()}.json") }
        } }
        item { MoreItem(Icons.Outlined.Restore, "Restaurar backup", "Substitui os dados após confirmação") { restoreLauncher.launch(arrayOf("application/json", "application/octet-stream")) } }
        item { SectionTitle("Preferências de estudo") }
        item {
            ElevatedCard {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Aparência", fontWeight = FontWeight.Bold)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("SYSTEM" to "Sistema", "LIGHT" to "Claro", "DARK" to "Escuro").forEach { (key, label) -> FilterChip(selected = themeMode == key, onClick = { viewModel.setThemeMode(key) }, label = { Text(label) }) }
                    }
                    HorizontalDivider()
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text("Cronômetro nas questões"); Text("Registra a duração da sessão", style = MaterialTheme.typography.bodySmall) }; Switch(timer, viewModel::setQuestionTimer) }
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text("Mostrar explicação"); Text("Após confirmar a resposta", style = MaterialTheme.typography.bodySmall) }; Switch(showExplanation, viewModel::setShowExplanation) }
                    Text("Quantidade padrão: $defaultCount", fontWeight = FontWeight.SemiBold)
                    Slider(defaultCount.toFloat().coerceIn(5f, 50f), { viewModel.setDefaultQuestionCount(it.toInt()) }, valueRange = 5f..50f, steps = 8)
                    Text("Ciclo de revisão", fontWeight = FontWeight.SemiBold)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(listOf(1L, 7L, 30L) to "D+1, D+7, D+30", listOf(1L, 3L, 7L, 14L, 30L) to "Intensivo").forEach { (value, label) -> FilterChip(selected = reviewIntervals == value, onClick = { viewModel.setReviewIntervals(value) }, label = { Text(label) }) }
                    }
                }
            }
        }
        item { Text("Meu Concurso 2.1.1 • Local-first • Sem login e sem internet", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 12.dp)) }
    }
}

@Composable private fun SectionTitle(text: String) { Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }

@Composable private fun MoreItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        ListItem(headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) }, supportingContent = { Text(subtitle) }, leadingContent = { Icon(icon, null) }, trailingContent = { Icon(Icons.Outlined.ChevronRight, null) })
    }
}

private suspend fun readText(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
    context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
}
