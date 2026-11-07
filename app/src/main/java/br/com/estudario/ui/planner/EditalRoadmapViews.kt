package br.com.estudario.ui.planner

import kotlinx.datetime.toJavaMonth
import kotlinx.datetime.toJavaLocalDate
import br.com.estudario.time.daysBetween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WarningAmber
import br.com.estudario.ui.brand.FilledTonalButton
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.planner.EditalRoadmapResult
import br.com.estudario.domain.planner.PlanPhaseKind
import br.com.estudario.domain.planner.RoadmapMonth
import br.com.estudario.domain.planner.RoadmapVerdict
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

private val ptBr = Locale("pt", "BR")
private val shortDate = DateTimeFormatter.ofPattern("d 'de' MMMM", ptBr)

private fun LocalDate.label(): String = format(shortDate) + if (year != LocalDate.now().year) " de $year" else ""

/** Cor de cada fase, a mesma na faixa e nos meses. */
@Composable
private fun PlanPhaseKind.tone(): Color = when (this) {
    PlanPhaseKind.BASE -> MaterialTheme.colorScheme.primary
    PlanPhaseKind.APROFUNDAMENTO -> Color(0xFF2EA56F)
    PlanPhaseKind.RETA_FINAL -> Color(0xFFE08A00)
}

private fun PlanPhaseKind.goal(): String = when (this) {
    PlanPhaseKind.BASE -> "Ver o edital inteiro, com questões logo depois da teoria"
    PlanPhaseKind.APROFUNDAMENTO -> "Mais questões e reforço dos pontos fracos"
    PlanPhaseKind.RETA_FINAL -> "Sem teoria nova: revisão, questões e simulados"
}

/**
 * O caminho até a prova: o selo "cabe ou não cabe", as fases e o edital mês a mês. Entra no topo
 * da Visão geral do plano.
 */
fun LazyListScope.editalRoadmap(
    roadmap: EditalRoadmapResult,
    onEditAvailability: () -> Unit,
    onShiftEnd: (Long) -> Unit,
    planId: String = "",
) {
    item(key = "roadmap_verdict") { VerdictCard(roadmap, onEditAvailability, onShiftEnd, planId) }
    item(key = "roadmap_phases") { PhaseStrip(roadmap) }
    item(key = "roadmap_title") {
        Column(Modifier.padding(top = 4.dp)) {
            Text("Seu edital mês a mês", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                "Quando cada parte do edital é vista pela primeira vez. Se a rotina mudar, o mapa se refaz.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    items(roadmap.months, key = { "roadmap_${it.month}" }) { month ->
        MonthRow(month, isLast = month == roadmap.months.last(), isFirst = month == roadmap.months.first())
    }
}

/** "1h40" / "45 min": o tempo do dia do jeito que a pessoa fala. */
private fun dayLength(minutes: Int): String = when {
    minutes < 60 -> "$minutes min"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h${(minutes % 60).toString().padStart(2, '0')}"
}

@Composable
private fun VerdictCard(roadmap: EditalRoadmapResult, onEditAvailability: () -> Unit, onShiftEnd: (Long) -> Unit, planId: String) {
    // Quando não cabe, a pessoa decide: aumentar as horas para ver tudo, ou focar no que mais cai (o
    // plano já deixa de fora o de menor peso). A escolha fica guardada no aparelho, por plano.
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = androidx.compose.runtime.remember { context.getSharedPreferences("plan_roadmap", android.content.Context.MODE_PRIVATE) }
    var focus by androidx.compose.runtime.remember(planId) { androidx.compose.runtime.mutableStateOf(planId.isNotEmpty() && prefs.getBoolean("focus_$planId", false)) }
    fun chooseFocus(value: Boolean) { focus = value; if (planId.isNotEmpty()) prefs.edit().putBoolean("focus_$planId", value).apply() }
    val notFit = roadmap.verdict == RoadmapVerdict.DOES_NOT_FIT
    val focused = notFit && focus
    val (tone, icon, title) = when {
        focused -> Triple(Color(0xFF2F5BD3), Icons.Outlined.CheckCircle, "Foco no que mais cai")
        roadmap.verdict == RoadmapVerdict.COMFORTABLE -> Triple(Color(0xFF2EA56F), Icons.Outlined.CheckCircle, "Seu edital cabe com folga")
        roadmap.verdict == RoadmapVerdict.TIGHT -> Triple(Color(0xFFE08A00), Icons.Outlined.WarningAmber, "Cabe, mas no limite")
        else -> Triple(MaterialTheme.colorScheme.error, Icons.Outlined.ErrorOutline, "O edital inteiro não cabe nas suas horas")
    }
    val daily = roadmap.weeklyCapacityMinutes / 7
    val extra = roadmap.extraMinutesPerDayToFit
    val leftOut = "${roadmap.topicsLeftOut} ${if (roadmap.topicsLeftOut == 1) "tópico" else "tópicos"}"
    val body = when {
        focused -> "Você cobre ${roadmap.weightCoveredPercent}% do peso da prova. Os $leftOut de menor peso" +
            (roadmap.atRiskSubjects.takeIf { it.isNotEmpty() }?.let { " (de ${it.take(2).joinToString(" e ")})" } ?: "") +
            " ficam para o fim, se sobrar tempo."
        roadmap.verdict == RoadmapVerdict.COMFORTABLE ->
            "Você vê o último tópico em ${roadmap.coverageDate!!.toJavaLocalDate().label()} e ainda sobram ${roadmap.slackDays} dias antes da reta final."
        roadmap.verdict == RoadmapVerdict.TIGHT ->
            "O último tópico entra em ${roadmap.coverageDate!!.toJavaLocalDate().label()}, colado na reta final. Qualquer atraso aperta a revisão."
        else -> buildString {
            append("Com ${dayLength(daily)} por dia, ficam $leftOut de fora")
            if (roadmap.atRiskSubjects.isNotEmpty()) append(", principalmente de ${roadmap.atRiskSubjects.take(2).joinToString(" e ")}")
            append(". Escolha como seguir:")
        }
    }
    Surface(shape = RoundedCornerShape(24.dp), color = tone.copy(alpha = 0.12f), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) { Icon(icon, null, tint = tone, modifier = Modifier.size(36.dp)) }
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
            Text(body, style = MaterialTheme.typography.bodyMedium)
            if (notFit && !focused) {
                // As duas saídas, com a conta de cada uma.
                FitChoice(
                    "Ver o edital inteiro",
                    if (extra > 0) "Mais $extra min por dia: de ${dayLength(daily)} para ${dayLength(daily + extra)}." else "Aumente as horas da semana.",
                    Icons.Outlined.Schedule, tone, onEditAvailability,
                )
                FitChoice(
                    "Focar no que mais cai",
                    "Fica com ${dayLength(daily)} por dia e cobre ${roadmap.weightCoveredPercent}% do peso da prova. Sai só o de menor peso.",
                    Icons.Outlined.CheckCircle, Color(0xFF2F5BD3),
                ) { chooseFocus(true) }
            }
            if (focused) {
                LinearProgressIndicator(progress = { roadmap.weightCoveredPercent / 100f }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = tone)
            }
            // Números que sustentam o selo: a pessoa vê a conta, não só a conclusão.
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Stat("${roadmap.studiedTopics}/${roadmap.totalTopics}", "tópicos vistos", Modifier.weight(1f))
                Stat(dayLength(daily), "por dia", Modifier.weight(1f))
                Stat("${daysBetween(roadmap.start, roadmap.end)}", if (roadmap.hasExamDate) "dias até a prova" else "dias de plano", Modifier.weight(1f))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.Event, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    if (roadmap.hasExamDate) "Prova em ${roadmap.end.toJavaLocalDate().label()}. O fim do plano é a prova."
                    else "Prazo do plano: ${roadmap.end.toJavaLocalDate().label()}. Sem data de prova, você escolhe até quando.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!roadmap.hasExamDate) {
                    OutlinedButton(onClick = { onShiftEnd(-30) }, modifier = Modifier.weight(1f)) { Text("− 30 dias") }
                    FilledTonalButton(onClick = { onShiftEnd(30) }, modifier = Modifier.weight(1f)) { Text("+ 30 dias") }
                }
                if (focused) {
                    OutlinedButton(onClick = { chooseFocus(false) }, modifier = Modifier.weight(1f)) { Text("Quero ver tudo") }
                } else if (!notFit && (roadmap.verdict != RoadmapVerdict.COMFORTABLE || roadmap.hasExamDate)) {
                    OutlinedButton(onClick = onEditAvailability, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Outlined.Schedule, null, Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("Horas")
                    }
                }
            }
        }
    }
}

/** Uma das saídas quando o edital não cabe: um botão grande com a conta embaixo. */
@Composable
private fun FitChoice(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tone: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, tone.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, null, tint = tone, modifier = Modifier.size(24.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, tint = tone, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun Stat(value: String, label: String, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), modifier = modifier) {
        Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** As fases lado a lado, do tamanho do tempo de cada uma, com o objetivo de cada fase. */
@Composable
private fun PhaseStrip(roadmap: EditalRoadmapResult) {
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("As fases da preparação", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Row(Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(7.dp)), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                roadmap.phases.forEach { phase ->
                    Box(Modifier.weight(phase.days.toFloat()).fillMaxHeight().background(phase.kind.tone()))
                }
            }
            roadmap.phases.forEach { phase ->
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.padding(top = 5.dp).size(10.dp).clip(CircleShape).background(phase.kind.tone()))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "${phase.kind.label} · ${phase.start.toJavaLocalDate().label()} a ${phase.end.toJavaLocalDate().label()}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(phase.kind.goal(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MonthRow(month: RoadmapMonth, isFirst: Boolean, isLast: Boolean) {
    val tone = month.phases.lastOrNull()?.tone() ?: MaterialTheme.colorScheme.primary
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        // Linha do tempo: bolinha do mês e o fio que liga os meses.
        Column(Modifier.width(26.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.width(2.dp).height(10.dp).background(if (isFirst) Color.Transparent else MaterialTheme.colorScheme.outlineVariant))
            Box(Modifier.size(14.dp).clip(CircleShape).background(tone))
            Box(Modifier.width(2.dp).weight(1f).background(if (isLast) Color.Transparent else MaterialTheme.colorScheme.outlineVariant))
        }
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.weight(1f).padding(start = 8.dp, bottom = 4.dp),
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        month.month.month.toJavaMonth().getDisplayName(TextStyle.FULL, ptBr).replaceFirstChar(Char::uppercase) +
                            if (month.month.year != LocalDate.now().year) " ${month.month.year}" else "",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    month.phases.forEach { kind ->
                        Surface(shape = RoundedCornerShape(8.dp), color = kind.tone().copy(alpha = 0.15f), modifier = Modifier.padding(start = 4.dp)) {
                            Text(kind.label, Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = kind.tone(), fontWeight = FontWeight.Bold)
                        }
                    }
                }
                if (month.reviewOnly) {
                    Text("Revisão, questões e simulados", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        month.slices.forEach { slice ->
                            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                                Text(
                                    "${slice.name} · ${slice.newTopics}",
                                    Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                )
                            }
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LinearProgressIndicator(
                        progress = { month.coveragePercent / 100f },
                        modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = tone,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    )
                    Text("${month.coveragePercent}% do edital", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
