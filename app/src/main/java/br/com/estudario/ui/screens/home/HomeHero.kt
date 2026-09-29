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
data class MissionItemUi(val title: String, val subtitle: String, val done: Boolean)

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
 * A missão do dia como checklist: cada atividade do plano de hoje, marcada quando feita, com o
 * progresso em cima. Mostra de relance o que falta, em vez de a pessoa ter de abrir o Plano.
 */
@Composable
fun DailyMissionCard(mission: DailyMissionUi, onOpenPlan: () -> Unit, modifier: Modifier = Modifier) {
    val done = mission.doneCount
    val total = mission.items.size
    Surface(onClick = onOpenPlan, shape = EstudarioShapes.panel, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Missão do dia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        if (done == total) "Tudo feito. Você cumpriu o plano de hoje." else "Faltam ${total - done} de $total · ${minutesText(mission.doneMinutes)} de ${minutesText(mission.plannedMinutes)}",
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text("$done/$total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = if (done == total) estudarioColors().completed else MaterialTheme.colorScheme.primary)
            }
            Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant)) {
                Box(Modifier.fillMaxWidth((if (total == 0) 0f else done.toFloat() / total).coerceIn(0.015f, 1f)).fillMaxHeight().clip(CircleShape).background(if (done == total) estudarioColors().completed else MaterialTheme.colorScheme.primary))
            }
            mission.items.take(5).forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(
                        if (item.done) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked, null, Modifier.size(20.dp),
                        tint = if (item.done) estudarioColors().completed else MaterialTheme.colorScheme.outline,
                    )
                    Column(Modifier.weight(1f)) {
                        Text(item.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis, color = if (item.done) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface)
                        Text(item.subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
            if (total > 5) Text("+ ${total - 5} no plano de hoje", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
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
