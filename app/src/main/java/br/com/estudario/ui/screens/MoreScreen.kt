package br.com.estudario.ui.screens

import br.com.estudario.ui.theme.screenPadding
import br.com.estudario.BuildConfig
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.HorizontalDivider
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import br.com.estudario.focus.FocusMode
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.ScreenTitle
import br.com.estudario.ui.prompt.BigValueSlider
import br.com.estudario.ui.prompt.ChoiceCard
import br.com.estudario.ui.theme.EstudarioShapes

/**
 * Ajustes: as preferências do estudo em grupos claros (aparência, questões, revisões, foco) e os
 * atalhos para o que mora em telas próprias (notificações, agenda, assistente).
 */
@Composable
fun MoreScreen(
    viewModel: AppViewModel,
    onOpenSetup: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onAgenda: () -> Unit = {},
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val timer by viewModel.questionTimer.collectAsState()
    val defaultCount by viewModel.defaultQuestionCount.collectAsState()
    val showExplanation by viewModel.showExplanation.collectAsState()
    val reviewIntervals by viewModel.reviewIntervals.collectAsState()
    val focusDnd by viewModel.focusDoNotDisturb.collectAsState()
    val focusScreenOn by viewModel.focusKeepScreenOn.collectAsState()
    val context = LocalContext.current
    var dndGranted by remember { mutableStateOf(FocusMode.hasDndAccess(context)) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> if (event == Lifecycle.Event.ON_RESUME) dndGranted = FocusMode.hasDndAccess(context) }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LazyColumn(
        Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = screenPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { ScreenTitle("Ajustes", "Deixe o Estudário do seu jeito") }

        item {
            Group("Aparência") {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(Triple("SYSTEM", "Sistema", Icons.Outlined.PhoneAndroid), Triple("LIGHT", "Claro", Icons.Outlined.LightMode), Triple("DARK", "Escuro", Icons.Outlined.DarkMode)).forEach { (key, label, icon) ->
                        val selected = themeMode == key
                        Surface(
                            onClick = { viewModel.setThemeMode(key) },
                            shape = EstudarioShapes.row,
                            color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier.weight(1f),
                        ) {
                            Column(Modifier.padding(vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(icon, null, tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(label, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
        }

        item {
            Group("Questões") {
                ToggleRow(Icons.Outlined.Timer, "Cronômetro nas questões", "Mostra e registra o tempo de cada sessão.", timer, viewModel::setQuestionTimer)
                HorizontalDivider()
                ToggleRow(Icons.Outlined.Visibility, "Explicação logo após responder", "Desligue para fazer tudo e ver as explicações só no final.", showExplanation, viewModel::setShowExplanation)
                HorizontalDivider()
                BigValueSlider(defaultCount.coerceIn(5, 50), 5..50, 5, { "$it" }, { viewModel.setDefaultQuestionCount(it) }, caption = "questões por sessão, por padrão", quickValues = listOf(10, 20, 30))
            }
        }

        item {
            Group("Revisões espaçadas") {
                Text("Depois que você estuda um tópico, o app agenda revisões nestes intervalos.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                ChoiceCard("Padrão · D+1, D+7, D+30", reviewIntervals == listOf(1L, 7L, 30L), { viewModel.setReviewIntervals(listOf(1L, 7L, 30L)) }, description = "Equilíbrio entre lembrar e ter tempo para matéria nova.", icon = Icons.Outlined.Replay)
                ChoiceCard("Intensivo · D+1, 3, 7, 14, 30", reviewIntervals == listOf(1L, 3L, 7L, 14L, 30L), { viewModel.setReviewIntervals(listOf(1L, 3L, 7L, 14L, 30L)) }, description = "Mais revisões, para reta final ou matéria difícil.", icon = Icons.Outlined.Speed)
            }
        }

        item {
            Group("Modo foco") {
                ToggleRow(Icons.Outlined.DoNotDisturbOn, "Não perturbe durante o estudo", "Silencia o celular ao começar e devolve tudo ao normal ao encerrar.", focusDnd, viewModel::setFocusDoNotDisturb)
                if (focusDnd && !dndGranted) {
                    Text("Falta autorizar o Não Perturbe nas configurações do Android (uma vez só).", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    OutlinedButton(onClick = { context.startActivity(FocusMode.dndSettingsIntent()) }, Modifier.fillMaxWidth()) { Text("Conceder acesso") }
                }
                HorizontalDivider()
                ToggleRow(Icons.Outlined.Lightbulb, "Manter a tela ligada", "Enquanto a tela do modo foco estiver aberta.", focusScreenOn, viewModel::setFocusKeepScreenOn)
            }
        }

        item {
            Group("Mais") {
                LinkRow(Icons.Outlined.Notifications, "Notificações", "Lembrete diário e pendências", onNotifications)
                HorizontalDivider()
                LinkRow(Icons.Outlined.CalendarMonth, "Agenda do celular", "Salvar o plano no calendário", onAgenda)
                HorizontalDivider()
                LinkRow(Icons.Outlined.AutoAwesome, "Configurar meus estudos", "Revisar concurso, edital, rotina e plano", onOpenSetup)
            }
        }

        item {
            Text(
                "Estudário ${BuildConfig.VERSION_NAME} · as preferências ficam salvas neste aparelho.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}

@Composable
private fun Group(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
        Surface(shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { content() }
        }
    }
}

@Composable
private fun ToggleRow(icon: ImageVector, title: String, description: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconTile(icon)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.width(8.dp))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun LinkRow(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = androidx.compose.ui.graphics.Color.Transparent) {
        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconTile(icon)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Ícone em bloco de cor suave: o mesmo padrão das folhas de ação, para o app inteiro falar igual. */
@Composable
private fun IconTile(icon: ImageVector) {
    Icon(icon, null, Modifier.size(38.dp), tint = MaterialTheme.colorScheme.primary)
}
