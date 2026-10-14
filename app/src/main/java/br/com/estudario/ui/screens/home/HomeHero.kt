package br.com.estudario.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.ui.draw.drawBehind
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Spacer
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
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.Surface
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
    // O cartão do concurso com a cara do Estudário: a tinta índigo do Folha, a pauta do caderno e a
    // margem vermelha ao fundo, o nome do concurso sempre inteiro (a letra diminui antes de cortar),
    // os dias até a prova num ingresso dourado e o edital coberto num anel.
    val ink = androidx.compose.ui.graphics.Color(0xFF2E2799)
    val inkDeep = androidx.compose.ui.graphics.Color(0xFF1B1464)
    val gold = androidx.compose.ui.graphics.Color(0xFFF5B83D)
    val white = androidx.compose.ui.graphics.Color.White
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
    val nameStyle = when {
        contest.name.length > 90 -> MaterialTheme.typography.titleMedium
        contest.name.length > 50 -> MaterialTheme.typography.titleLarge
        else -> MaterialTheme.typography.headlineSmall
    }
    Column(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(androidx.compose.ui.graphics.Brush.linearGradient(listOf(ink, inkDeep)))
            .drawBehind {
                // Pauta do caderno, bem leve, e a linha vermelha da margem.
                val step = 26.dp.toPx()
                var y = step
                while (y < size.height) { drawLine(white.copy(alpha = 0.06f), androidx.compose.ui.geometry.Offset(0f, y), androidx.compose.ui.geometry.Offset(size.width, y), 1.dp.toPx()); y += step }
                val margin = 14.dp.toPx()
                drawLine(androidx.compose.ui.graphics.Color(0xFFE7A9A3).copy(alpha = 0.45f), androidx.compose.ui.geometry.Offset(margin, 0f), androidx.compose.ui.geometry.Offset(margin, size.height), 1.5.dp.toPx())
            }
            .clickable(onClick = onOpenContest)
            .padding(start = 28.dp, end = 20.dp, top = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("${greetingNow()}, $firstName", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = white.copy(alpha = 0.8f))
            if (daysToExam != null) {
                // O ingresso: dias até a prova, com o recorte das bordas.
                Row(
                    Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp)).background(gold).padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("$daysToExam", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = inkDeep)
                    Spacer(Modifier.width(6.dp))
                    Text(if (daysToExam == 1L) "dia para\na prova" else "dias para\na prova", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = inkDeep, lineHeight = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp))
                }
            }
        }
        Text(contest.name, style = nameStyle, fontWeight = FontWeight.Black, color = white)
        if (!contest.objective.isNullOrBlank() && contest.objective != contest.name) {
            Text(contest.objective, style = MaterialTheme.typography.bodyMedium, color = white.copy(alpha = 0.75f))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoverageRing(coverage.percent, gold, white)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("do edital coberto", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = white)
                Text("${coverage.studiedTopics} de ${coverage.totalTopics} tópicos estudados", style = MaterialTheme.typography.bodySmall, color = white.copy(alpha = 0.75f))
                coverage.masteryPercent?.let { Text("Domínio de $it% nas questões", style = MaterialTheme.typography.bodySmall, color = white.copy(alpha = 0.75f)) }
            }
        }
        @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
        androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HeroChip(br.com.estudario.ui.brand.Glyph.Fire, if (standing.streakDays == 0) "Comece a sequência hoje" else "${standing.streakDays} ${if (standing.streakDays == 1) "dia" else "dias"} seguidos")
            HeroChip(br.com.estudario.ui.brand.Glyph.Medal, "Nível ${standing.level} · ${standing.levelTitle}")
        }
    }
}

/** O anel do edital coberto: trilha clara, arco dourado que cresce animado e a porcentagem no meio. */
@Composable
private fun CoverageRing(percent: Int, gold: androidx.compose.ui.graphics.Color, white: androidx.compose.ui.graphics.Color) {
    val shown by androidx.compose.animation.core.animateFloatAsState((percent / 100f).coerceIn(0f, 1f), androidx.compose.animation.core.tween(1100), label = "ring")
    Box(Modifier.size(76.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(Modifier.size(76.dp)) {
            val stroke = 9.dp.toPx()
            val inset = stroke / 2
            val arcSize = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke)
            drawArc(white.copy(alpha = 0.16f), 0f, 360f, false, androidx.compose.ui.geometry.Offset(inset, inset), arcSize, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke))
            drawArc(gold, -90f, 360f * shown, false, androidx.compose.ui.geometry.Offset(inset, inset), arcSize, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round))
        }
        Text("$percent%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = white)
    }
}

@Composable
private fun HeroChip(glyph: br.com.estudario.ui.brand.Glyph, text: String) {
    Row(
        Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(50)).background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.14f)).padding(start = 6.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        br.com.estudario.ui.brand.BrandIcon(glyph, size = 24.dp)
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = androidx.compose.ui.graphics.Color.White)
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
