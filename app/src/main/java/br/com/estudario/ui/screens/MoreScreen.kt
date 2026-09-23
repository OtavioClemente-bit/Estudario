package br.com.estudario.ui.screens

import br.com.estudario.ui.theme.screenPadding
import br.com.estudario.BuildConfig
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.ScreenTitle

/**
 * Ajustes é uma tela de preferências de estudo. Os destinos de acompanhamento e notificações
 * continuam no Drawer; repetir esses atalhos aqui transformava a tela em um segundo menu lateral.
 */
@Composable
fun MoreScreen(
    viewModel: AppViewModel,
    onOpenSetup: () -> Unit = {},
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val timer by viewModel.questionTimer.collectAsState()
    val defaultCount by viewModel.defaultQuestionCount.collectAsState()
    val showExplanation by viewModel.showExplanation.collectAsState()
    val reviewIntervals by viewModel.reviewIntervals.collectAsState()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = screenPadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { ScreenTitle("Ajustes", "Preferências de estudo") }
        item {
            ElevatedCard {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Aparência", style = MaterialTheme.typography.titleMedium)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("SYSTEM" to "Sistema", "LIGHT" to "Claro", "DARK" to "Escuro").forEach { (key, label) ->
                            FilterChip(
                                selected = themeMode == key,
                                onClick = { viewModel.setThemeMode(key) },
                                label = { Text(label) },
                            )
                        }
                    }
                    HorizontalDivider()
                    PreferenceSwitch(
                        title = "Cronômetro nas questões",
                        description = "Registra a duração da sessão",
                        checked = timer,
                        onCheckedChange = viewModel::setQuestionTimer,
                    )
                    PreferenceSwitch(
                        title = "Mostrar explicação",
                        description = "Após confirmar a resposta",
                        checked = showExplanation,
                        onCheckedChange = viewModel::setShowExplanation,
                    )
                    Text("Quantidade padrão: $defaultCount", style = MaterialTheme.typography.titleSmall)
                    Slider(
                        value = defaultCount.toFloat().coerceIn(5f, 50f),
                        onValueChange = { viewModel.setDefaultQuestionCount(it.toInt()) },
                        valueRange = 5f..50f,
                        steps = 8,
                    )
                    Text("Ciclo de revisão", style = MaterialTheme.typography.titleSmall)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            listOf(1L, 7L, 30L) to "D+1, D+7, D+30",
                            listOf(1L, 3L, 7L, 14L, 30L) to "Intensivo",
                        ).forEach { (value, label) ->
                            FilterChip(
                                selected = reviewIntervals == value,
                                onClick = { viewModel.setReviewIntervals(value) },
                                label = { Text(label) },
                            )
                        }
                    }
                }
            }
        }
        item {
            ElevatedCard(onClick = onOpenSetup) {
                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Assistente de configuração", style = MaterialTheme.typography.titleMedium)
                        Text("Revisar concurso, edital, rotina e plano", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Preferências locais", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "Essas escolhas ficam no aparelho e personalizam suas sessões de estudo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        item {
            Spacer(Modifier.height(4.dp))
            Text(
                "Estudário ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PreferenceSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row {
        Column(Modifier.weight(1f)) {
            Text(title)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
