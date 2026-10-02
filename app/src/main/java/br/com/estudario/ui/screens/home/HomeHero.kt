package br.com.estudario.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import java.time.LocalTime

/** Uma atividade do dia na lista da missão. */
data class MissionItemUi(val title: String, val subtitle: String, val done: Boolean, val current: Boolean = false)

/** A missão do dia inteira: o que o plano pede hoje e quanto já foi feito. */
data class DailyMissionUi(val items: List<MissionItemUi>, val plannedMinutes: Int, val doneMinutes: Int) {
    val doneCount get() = items.count { it.done }
}

/**
 * O topo da Home: a pessoa, o concurso, quanto falta para a prova e o quanto do edital já foi.
 * É a primeira impressão do app, então concentra a pergunta "estou no caminho?" num cartão só.
 */
@Composable
fun HomeHero(
    firstName: String,
    contest: ActiveContestUi,
    daysToExam: Long?,
    coverage: SyllabusCoverageUi,
    standing: StandingUi,
    onOpenContest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val on = MaterialTheme.colorScheme.onPrimaryContainer
    Surface(shape = EstudarioShapes.spotlight, color = MaterialTheme.colorScheme.primaryContainer, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.clickable(onClick = onOpenContest).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("${greetingNow()}, $firstName", style = MaterialTheme.typography.bodyMedium, color = on)
                    Text(contest.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = on, maxLines = 3, overflow = TextOverflow.Ellipsis)
                }
                if (daysToExam != null) Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 12.dp)) {
                    Text("$daysToExam", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(if (daysToExam == 1L) "dia para a prova" else "dias para a prova", style = MaterialTheme.typography.labelSmall, color = on)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row {
                    Text("Edital coberto", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, color = on)
                    Text("${coverage.percent}% · ${coverage.studiedTopics}/${coverage.totalTopics} tópicos", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = on)
                }
                Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))) {
                    Box(Modifier.fillMaxWidth((coverage.percent / 100f).coerceIn(0.015f, 1f)).fillMaxHeight().clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Outlined.LocalFireDepartment, null, Modifier.size(18.dp), tint = estudarioColors().attention)
                    Text(if (standing.streakDays == 0) "Comece a sequência hoje" else "${standing.streakDays} ${if (standing.streakDays == 1) "dia" else "dias"} seguidos", style = MaterialTheme.typography.labelLarge, color = on)
                }
                Text("Nível ${standing.level}", style = MaterialTheme.typography.labelLarge, color = on)
                coverage.masteryPercent?.let { Text("Domínio $it%", style = MaterialTheme.typography.labelLarge, color = on) }
            }
        }
    }
}

/**
 * A missão do dia: o dia inteiro do plano numa linha só. O que já foi feito fica riscado, o que
 * está em andamento aparece destacado como "Agora" e o resto vem em seguida, na ordem. Substitui
 * a lista "Depois", que repetia as mesmas tarefas logo abaixo.
 */
@Composable
fun DailyMissionCard(mission: DailyMissionUi, onOpenPlan: () -> Unit, modifier: Modifier = Modifier) {
    val done = mission.doneCount
    val total = mission.items.size
    val complete = total > 0 && done == total
    val accent = if (complete) estudarioColors().completed else MaterialTheme.colorScheme.primary
    Surface(onClick = onOpenPlan, shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Missão do dia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        if (complete) "Tudo feito. Você cumpriu o plano de hoje."
                        else "${minutesText(mission.doneMinutes)} de ${minutesText(mission.plannedMinutes)} · faltam ${total - done}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text("$done/$total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = accent)
            }
            // Uma fatia por tarefa: dá para ver de relance quantas faltam.
            Row(Modifier.fillMaxWidth().height(6.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                mission.items.forEach { item ->
                    Box(
                        Modifier.weight(1f).fillMaxHeight().clip(CircleShape).background(
                            when {
                                item.done -> accent
                                item.current -> accent.copy(alpha = 0.45f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                        ),
                    )
                }
            }
            Column {
                val shown = mission.items.take(6)
                shown.forEachIndexed { index, item -> MissionRow(item, isFirst = index == 0, isLast = index == shown.lastIndex) }
            }
            if (total > 6) Text("+ ${total - 6} no plano de hoje", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun MissionRow(item: MissionItemUi, isFirst: Boolean, isLast: Boolean) {
    val primary = MaterialTheme.colorScheme.primary
    val line = MaterialTheme.colorScheme.outlineVariant
    Row(Modifier.fillMaxWidth().height(androidx.compose.foundation.layout.IntrinsicSize.Min)) {
        // Linha do dia: o marcador de cada tarefa e o fio que liga uma à outra.
        Column(Modifier.width(24.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.width(2.dp).height(8.dp).background(if (isFirst) androidx.compose.ui.graphics.Color.Transparent else line))
            when {
                item.done -> Icon(Icons.Outlined.CheckCircle, null, Modifier.size(20.dp), tint = estudarioColors().completed)
                item.current -> Box(Modifier.size(20.dp).clip(CircleShape).background(primary.copy(alpha = 0.18f)), contentAlignment = Alignment.Center) {
                    Box(Modifier.size(9.dp).clip(CircleShape).background(primary))
                }
                else -> Icon(Icons.Outlined.RadioButtonUnchecked, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.outline)
            }
            Box(Modifier.width(2.dp).weight(1f).background(if (isLast) androidx.compose.ui.graphics.Color.Transparent else line))
        }
        Column(Modifier.weight(1f).padding(start = 10.dp, top = 6.dp, bottom = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    item.title,
                    Modifier.weight(1f, fill = false),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (item.current) FontWeight.SemiBold else FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (item.done) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (item.done) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                )
                if (item.current) Surface(shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp), color = primary.copy(alpha = 0.14f)) {
                    Text("Agora", Modifier.padding(horizontal = 7.dp, vertical = 1.dp), style = MaterialTheme.typography.labelSmall, color = primary, fontWeight = FontWeight.Bold)
                }
            }
            Text(item.subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

private fun minutesText(minutes: Int): String = when {
    minutes < 60 -> "$minutes min"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h${"%02d".format(minutes % 60)}"
}

private fun greetingNow(now: LocalTime = LocalTime.now()): String = when (now.hour) {
    in 0..4 -> "Boa madrugada"
    in 5..11 -> "Bom dia"
    in 12..17 -> "Boa tarde"
    else -> "Boa noite"
}
