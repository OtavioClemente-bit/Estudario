package br.com.estudario.ui.planner

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.data.local.planner.StudyAvailabilityEntity
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Surface

// ---------------------------------------------------------------- prioridade

/** Cor de cada prioridade: vermelho chama, cinza descansa. Igual nos dois temas. */
internal fun priorityColor(priority: PlanPriority): Color = when (priority) {
    PlanPriority.CRITICAL -> Color(0xFFE5484D)
    PlanPriority.HIGH -> Color(0xFFF2852B)
    PlanPriority.MEDIUM -> Color(0xFF3E8BFF)
    PlanPriority.LOW -> Color(0xFF8A94A6)
}

internal fun priorityName(priority: PlanPriority): String = when (priority) {
    PlanPriority.CRITICAL -> "Crítica"
    PlanPriority.HIGH -> "Alta"
    PlanPriority.MEDIUM -> "Média"
    PlanPriority.LOW -> "Baixa"
}

private fun priorityHint(priority: PlanPriority): String = when (priority) {
    PlanPriority.CRITICAL -> "Entra quase todo dia e recebe a maior fatia do tempo"
    PlanPriority.HIGH -> "Aparece com frequência, logo atrás das críticas"
    PlanPriority.MEDIUM -> "Ritmo normal, intercalada com as outras"
    PlanPriority.LOW -> "Manutenção: só o suficiente para não esquecer"
}

/** Etiqueta colorida da prioridade, com bolinha; toque abre o seletor. */
@Composable
internal fun PriorityPill(priority: PlanPriority, enabled: Boolean, onClick: () -> Unit) {
    val color = if (enabled) priorityColor(priority) else MaterialTheme.colorScheme.outline
    Row(
        Modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.16f))
            .border(1.dp, color.copy(alpha = 0.55f), RoundedCornerShape(50))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(6.dp))
        Text(priorityName(priority), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
    }
}

/** Escolha da prioridade: quatro cartões coloridos com o que cada nível faz no cronograma. */
@Composable
internal fun PriorityPickerDialog(subjectName: String, current: PlanPriority, onDismiss: () -> Unit, onPick: (PlanPriority) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 6.dp, modifier = Modifier.widthIn(max = 420.dp)) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Prioridade", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(subjectName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                PlanPriority.entries.forEach { level ->
                    val color = priorityColor(level)
                    val selected = level == current
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
                            .background(if (selected) color.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceContainerHigh)
                            .border(if (selected) 2.dp else 1.dp, if (selected) color else Color.Transparent, RoundedCornerShape(18.dp))
                            .clickable { onPick(level); onDismiss() }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Barrinhas de intensidade: crítica cheia, baixa com uma só.
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.Bottom, modifier = Modifier.height(22.dp)) {
                            val filled = 4 - level.ordinal
                            repeat(4) { i ->
                                Box(Modifier.width(5.dp).fillMaxHeight(0.4f + i * 0.2f).clip(RoundedCornerShape(2.dp)).background(if (i < filled) color else color.copy(alpha = 0.2f)))
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(priorityName(level), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
                            Text(priorityHint(level), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (selected) Box(Modifier.size(26.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Check, null, Modifier.size(16.dp), tint = Color.White)
                        }
                    }
                }
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) { Text("Fechar") }
            }
        }
    }
}

// ---------------------------------------------------------------- horas por dia

private const val STEP = 30
private const val MAX_MINUTES = 600

private fun hoursLabel(minutes: Int): String = when {
    minutes <= 0 -> "Folga"
    minutes % 60 == 0 -> "${minutes / 60}h"
    minutes < 60 -> "${minutes}min"
    else -> "${minutes / 60}h${"%02d".format(minutes % 60)}"
}

/**
 * Horas de estudo de cada dia da semana. Cada dia tem − e + de 30 em 30 minutos, a barra mostra o
 * peso do dia e um toque no nome alterna entre folga e o último valor. Atalhos copiam o dia útil.
 */
@Composable
internal fun AvailabilityEditorDialog(current: List<StudyAvailabilityEntity>, onDismiss: () -> Unit, onConfirm: (List<StudyAvailabilityEntity>) -> Unit) {
    val initial = (1..7).map { day -> current.firstOrNull { it.dayOfWeek == day }?.let { if (it.unavailable) 0 else it.availableMinutes } ?: 0 }
    val minutes = remember(current) { mutableStateListOf(*initial.toTypedArray()) }
    val names = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")
    val primary = MaterialTheme.colorScheme.primary
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 6.dp, modifier = Modifier.widthIn(max = 440.dp)) {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 22.dp)) {
                Text("Horas de estudo por dia", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Conte só o tempo de estudo de verdade. O plano é refeito com essas horas.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(14.dp))
                Column(Modifier.weight(1f, fill = false).heightIn(max = 460.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    names.forEachIndexed { index, name ->
                        val value = minutes[index]
                        val fill by animateFloatAsState((value.toFloat() / MAX_MINUTES).coerceIn(0f, 1f), label = "day-$index")
                        val bg by animateColorAsState(if (value > 0) primary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surfaceContainerHigh, label = "day-bg-$index")
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(bg).padding(start = 14.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(5.dp))
                                Box(Modifier.fillMaxWidth(0.92f).height(6.dp).clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.surfaceContainerHighest)) {
                                    Box(Modifier.fillMaxWidth(fill).height(6.dp).clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(primary, Color(0xFF22C59A)))))
                                }
                            }
                            StepButton(Icons.Rounded.Remove, "Menos 30 minutos em $name", enabled = value > 0) { minutes[index] = (value - STEP).coerceAtLeast(0) }
                            Text(
                                hoursLabel(value),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = if (value > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.widthIn(min = 62.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            )
                            StepButton(Icons.Rounded.Add, "Mais 30 minutos em $name", enabled = value < MAX_MINUTES) { minutes[index] = (value + STEP).coerceAtMost(MAX_MINUTES) }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickAction("Igualar dias úteis") { val base = minutes[0]; (1..4).forEach { minutes[it] = base } }
                    QuickAction("Fim de semana livre") { minutes[5] = 0; minutes[6] = 0 }
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Total na semana", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Text(minutesLabel(minutes.sum()), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = { onConfirm(minutes.mapIndexed { index, value -> StudyAvailabilityEntity("pending", index + 1, value, value == 0) }); onDismiss() },
                    enabled = minutes.any { it > 0 },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Salvar e refazer o plano") }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
            }
        }
    }
}

@Composable
private fun StepButton(icon: androidx.compose.ui.graphics.vector.ImageVector, description: String, enabled: Boolean, onClick: () -> Unit) {
    val color = MaterialTheme.colorScheme.primary
    Box(
        Modifier.padding(horizontal = 4.dp).size(38.dp).clip(CircleShape)
            .background(if (enabled) color.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceContainerHighest)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, description, Modifier.size(20.dp), tint = if (enabled) color else MaterialTheme.colorScheme.outline) }
}

@Composable
private fun QuickAction(label: String, onClick: () -> Unit) {
    Text(
        label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.clip(RoundedCornerShape(50)).border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(50))
            .clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 7.dp),
    )
}

// ---------------------------------------------------------------- reprogramar

/** Para onde levar uma tarefa atrasada: três caminhos claros, sem jargão. */
@Composable
internal fun ReprogramDialog(taskTitle: String, onDismiss: () -> Unit, onPick: (java.time.LocalDate?) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 6.dp, modifier = Modifier.widthIn(max = 420.dp)) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Reprogramar", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(taskTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("O que você já fez dela continua registrado.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                ReprogramOption("Deixar o plano encaixar", "Vai para o primeiro espaço livre do seu cronograma", Color(0xFF7C5CFF)) { onPick(null); onDismiss() }
                ReprogramOption("Amanhã", "Entra no plano de amanhã", Color(0xFF22C59A)) { onPick(java.time.LocalDate.now().plusDays(1)); onDismiss() }
                ReprogramOption("Hoje", "Fazer ainda hoje, junto com o que já estava", Color(0xFFF2852B)) { onPick(java.time.LocalDate.now()); onDismiss() }
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) { Text("Cancelar") }
            }
        }
    }
}

@Composable
private fun ReprogramOption(title: String, detail: String, color: Color, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable(onClick = onClick).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
