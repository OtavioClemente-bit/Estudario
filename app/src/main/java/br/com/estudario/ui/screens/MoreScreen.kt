package br.com.estudario.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.ProfileAvatar
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.tour.TourKey
import br.com.estudario.ui.tour.tourTarget

@Composable
fun MoreScreen(
    viewModel: AppViewModel,
    onReviews: () -> Unit,
    onStatistics: () -> Unit,
    onNotifications: () -> Unit,
    onErrors: () -> Unit,
    onProfile: () -> Unit,
    onSources: () -> Unit,
    onFocus: () -> Unit,
    onHelp: () -> Unit = {},
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val timer by viewModel.questionTimer.collectAsState()
    val defaultCount by viewModel.defaultQuestionCount.collectAsState()
    val showExplanation by viewModel.showExplanation.collectAsState()
    val reviewIntervals by viewModel.reviewIntervals.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val focus by viewModel.focusSession.collectAsState()

    val tourStep by viewModel.tourStep.collectAsState()
    val listState = rememberLazyListState()
    // Índices dos itens abaixo, para rolar até o item que o guia está explicando.
    LaunchedEffect(tourStep?.key) {
        val index = when (tourStep?.key) {
            TourKey.MORE_REVIEWS -> 3
            TourKey.MORE_STATS -> 4
            TourKey.MORE_ERRORS -> 5
            else -> null
        }
        if (index != null) listState.animateScrollToItem(index)
    }
    fun target(key: TourKey) = Modifier.tourTarget(key, tourStep?.key) { viewModel.reportTourTargetBounds(key, it) }

    LazyColumn(Modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { ScreenTitle("Mais", "Ferramentas e configurações") { IconButton(onClick = onHelp) { Icon(Icons.Outlined.HelpOutline, "Ajuda desta seção") } } }
        item {
            // Perfil no topo: nome, foto, sequência e, dentro dele, backup e conta.
            ElevatedCard(Modifier.fillMaxWidth().clickable(onClick = onProfile)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    ProfileAvatar(profile.photoPath, profile.initials, 52.dp)
                    Column(Modifier.weight(1f)) {
                        Text(profile.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            if (profile.signedIn) profile.email else "Conta local • toque para editar, ver sequência e fazer backup",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        streak?.let { Text("${it.current} dia(s) de sequência • melhor: ${it.best}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary) }
                    }
                    Icon(Icons.Outlined.ChevronRight, null)
                }
            }
        }
        item { SectionTitle("Estudo") }
        item {
            // Sessão livre: serve para estudar no livro, no caderno ou em videoaula com o
            // Não Perturbe ligado e o tempo sendo medido.
            MoreItem(
                Icons.Outlined.Timer,
                "Modo foco",
                if (focus.active) "Sessão em andamento • toque para ver o cronômetro" else "Sessão cronometrada com o Não Perturbe ligado",
                onClick = { if (!focus.active) viewModel.startFocus("Sessão livre"); onFocus() },
            )
        }
        item { MoreItem(Icons.Outlined.EventRepeat, "Revisões espaçadas", "D+1, D+7, D+30 e, depois disso, sempre", target(TourKey.MORE_REVIEWS), onReviews) }
        item { MoreItem(Icons.Outlined.QueryStats, "Desempenho", "Acertos por matéria, pontos fortes e o que pede revisão", target(TourKey.MORE_STATS), onStatistics) }
        item { MoreItem(Icons.Outlined.ErrorOutline, "Caderno de erros", "Questões erradas e os conceitos que causam o erro", target(TourKey.MORE_ERRORS), onErrors) }
        item { MoreItem(Icons.Outlined.FactCheck, "Histórico e fontes", "De onde veio cada conteúdo que a IA gerou, com o link para conferir", onClick = onSources) }
        item { MoreItem(Icons.Outlined.NotificationsActive, "Notificações de estudo", "Horário, pendências, revisões e teste de aviso", onClick = onNotifications) }
        item { SectionTitle("Preferências de estudo") }
        item {
            ElevatedCard {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Aparência", fontWeight = FontWeight.Bold)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("SYSTEM" to "Sistema", "LIGHT" to "Claro", "DARK" to "Escuro").forEach { (key, label) -> FilterChip(selected = themeMode == key, onClick = { viewModel.setThemeMode(key) }, label = { Text(label) }) }
                    }
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text("Cronômetro nas questões"); Text("Registra a duração da sessão", style = MaterialTheme.typography.bodySmall) }; Switch(timer, viewModel::setQuestionTimer) }
                    Row(verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text("Mostrar explicação"); Text("Após confirmar a resposta", style = MaterialTheme.typography.bodySmall) }; Switch(showExplanation, viewModel::setShowExplanation) }
                    Text("Quantidade padrão: $defaultCount", fontWeight = FontWeight.SemiBold)
                    Slider(defaultCount.toFloat().coerceIn(5f, 50f), { viewModel.setDefaultQuestionCount(it.toInt()) }, valueRange = 5f..50f, steps = 8)
                    Text("Ciclo de revisão", fontWeight = FontWeight.SemiBold)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(listOf(1L, 7L, 30L) to "D+1, D+7, D+30", listOf(1L, 3L, 7L, 14L, 30L) to "Intensivo").forEach { (value, label) -> FilterChip(selected = reviewIntervals == value, onClick = { viewModel.setReviewIntervals(value) }, label = { Text(label) }) }
                    }
                }
            }
        }
        item { Text("Estudário 2.2.0 • Local-first • Estudo offline; conta Google e backup no Drive são opcionais", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 12.dp)) }
    }
}

@Composable private fun SectionTitle(text: String) { Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }

@Composable private fun MoreItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    ElevatedCard(modifier.fillMaxWidth().clickable(onClick = onClick)) {
        ListItem(headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) }, supportingContent = { Text(subtitle) }, leadingContent = { Icon(icon, null) }, trailingContent = { Icon(Icons.Outlined.ChevronRight, null) })
    }
}
