package br.com.estudario.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.ui.draw.drawBehind
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.draw.alpha
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.estudarioColors
import java.time.LocalTime

/** Uma atividade do dia na lista da missão. */
data class MissionItemUi(
    val title: String,
    val subtitle: String,
    val done: Boolean,
    val current: Boolean = false,
    val kind: br.com.estudario.domain.planner.PlanTaskType? = null,
    val typeLabel: String = "",
    val minutes: Int = 0,
    val subject: String? = null,
    val inProgress: Boolean = false,
    /** Abre esta tarefa (teoria, questões ou revisão). */
    val onOpen: (() -> Unit)? = null,
)

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
        // Sequência e nível ficam no perfil: o cartão fala só do concurso.
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
 * A missão do dia: o dia do plano como uma trilha de passos numerados, igual ao site. O próximo
 * passo fica aberto, grande, com o tipo, o tempo e o botão de começar (ou de gerar o material); os
 * feitos ficam marcados e os seguintes esperam a vez. Quem nunca usou sabe por onde começar.
 */
@Composable
fun DailyMissionCard(
    mission: DailyMissionUi,
    onOpenPlan: () -> Unit,
    modifier: Modifier = Modifier,
    onStartCurrent: (() -> Unit)? = null,
    onGenerate: (() -> Unit)? = null,
) {
    val g = br.com.estudario.ui.profile.Game
    val done = mission.doneCount
    val total = mission.items.size
    val complete = total > 0 && done == total
    // O próximo passo é o primeiro ainda não feito, na ordem do dia; tocar num passo abre ele.
    val nowIndex = mission.items.indexOfFirst { !it.done }
    var openIndex by androidx.compose.runtime.saveable.rememberSaveable(mission.items.size) { androidx.compose.runtime.mutableStateOf<Int?>(null) }
    val expanded = openIndex ?: nowIndex
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    run {
        Column(
            modifier.fillMaxWidth().clip(shape)
                .background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(g.bg2, g.bg)))
                .border(1.dp, g.line, shape)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Cabeçalho: o anel com feitos/total e a frase do que falta.
            Row(verticalAlignment = Alignment.CenterVertically) {
                val ring by androidx.compose.animation.core.animateFloatAsState(if (total == 0) 0f else done.toFloat() / total, androidx.compose.animation.core.tween(900), label = "missao-anel")
                Box(Modifier.size(74.dp), contentAlignment = Alignment.Center) {
                    androidx.compose.foundation.Canvas(Modifier.size(74.dp)) {
                        val stroke = 7.dp.toPx()
                        val inset = stroke / 2
                        val arc = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke)
                        drawArc(g.track, 0f, 360f, false, androidx.compose.ui.geometry.Offset(inset, inset), arc, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke))
                        drawArc(g.mint, -90f, 360f * ring, false, androidx.compose.ui.geometry.Offset(inset, inset), arc, style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$done/$total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = g.ink)
                        Text("PASSOS", style = MaterialTheme.typography.labelSmall, fontSize = androidx.compose.ui.unit.TextUnit(9f, androidx.compose.ui.unit.TextUnitType.Sp), color = g.muted)
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text("MISSÃO DO DIA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = g.muted, letterSpacing = androidx.compose.ui.unit.TextUnit(1.6f, androidx.compose.ui.unit.TextUnitType.Sp))
                    Text(
                        when {
                            complete -> "Missão cumprida! Você fechou o plano de hoje."
                            done == 0 -> "Hoje são $total ${if (total == 1) "passo" else "passos"}. Comece pelo primeiro."
                            total - done == 1 -> "Falta 1 passo para fechar o dia."
                            else -> "Faltam ${total - done} passos para fechar o dia."
                        },
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = if (complete) g.mint else g.ink,
                    )
                    Text(
                        if (complete) "Amanhã tem mais." else "${minutesText((mission.plannedMinutes - mission.doneMinutes).coerceAtLeast(0))} de estudo pela frente",
                        style = MaterialTheme.typography.bodySmall, color = g.muted,
                    )
                }
            }
            // Os passos, ligados por um fio.
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val shown = mission.items.take(6)
                shown.forEachIndexed { index, item -> MissionStep(index, item, index == nowIndex, index == expanded, { openIndex = index }, item.onOpen ?: onStartCurrent, if (index == nowIndex) onGenerate else null) }
            }
            if (total > 6) Text("+ ${total - 6} no plano de hoje", style = MaterialTheme.typography.labelMedium, color = g.readable(g.violet))
            Row(Modifier.fillMaxWidth().clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp)).clickable(onClick = onOpenPlan).padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.CheckCircle, null, Modifier.size(18.dp), tint = g.muted)
                Spacer(Modifier.width(8.dp))
                Text("Ver o plano inteiro", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = g.ink, modifier = Modifier.weight(1f))
                Text("›", style = MaterialTheme.typography.titleMedium, color = g.muted)
            }
        }
    }
}

@Composable
private fun MissionStep(index: Int, item: MissionItemUi, isNow: Boolean, isOpen: Boolean, onSelect: () -> Unit, onStart: (() -> Unit)?, onGenerate: (() -> Unit)?) {
    val g = br.com.estudario.ui.profile.Game
    val color = when (item.kind) {
        br.com.estudario.domain.planner.PlanTaskType.THEORY -> androidx.compose.ui.graphics.Color(0xFFA78BFF)
        br.com.estudario.domain.planner.PlanTaskType.QUESTIONS, br.com.estudario.domain.planner.PlanTaskType.SIMULATION -> androidx.compose.ui.graphics.Color(0xFF2FD39A)
        br.com.estudario.domain.planner.PlanTaskType.REVIEW -> androidx.compose.ui.graphics.Color(0xFF22D3EE)
        br.com.estudario.domain.planner.PlanTaskType.FLASHCARDS, br.com.estudario.domain.planner.PlanTaskType.ACTIVE_RECALL -> androidx.compose.ui.graphics.Color(0xFFFFC94D)
        else -> androidx.compose.ui.graphics.Color(0xFFFF8A3D)
    }
    val pulse by androidx.compose.animation.core.rememberInfiniteTransition(label = "passo").animateFloat(
        0.35f, 1f, androidx.compose.animation.core.infiniteRepeatable(androidx.compose.animation.core.tween(1_100), androidx.compose.animation.core.RepeatMode.Reverse), label = "passo-pulso",
    )
    Row(verticalAlignment = Alignment.Top) {
        // O número do passo: ✓ quando feito, aceso e pulsando quando é a vez.
        Box(
            Modifier.padding(top = 8.dp).size(34.dp).clip(CircleShape)
                .background(if (item.done) g.mint else if (isNow) color else g.bg2)
                .border(2.dp, if (isNow) androidx.compose.ui.graphics.Color.White.copy(alpha = pulse) else if (item.done) g.mint else br.com.estudario.ui.profile.Game.tint(0.18f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (item.done) Text("✓", fontWeight = FontWeight.Black, color = androidx.compose.ui.graphics.Color(0xFF14113A))
            else Text("${index + 1}", fontWeight = FontWeight.Black, color = if (isNow) g.bg else g.muted)
        }
        Spacer(Modifier.width(12.dp))
        val cardShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        Column(
            Modifier.weight(1f).clip(cardShape)
                .background(if (isOpen) androidx.compose.ui.graphics.Brush.linearGradient(listOf(color.copy(alpha = 0.22f), br.com.estudario.ui.profile.Game.tint(0.03f))) else androidx.compose.ui.graphics.Brush.linearGradient(listOf(br.com.estudario.ui.profile.Game.tint(0.035f), br.com.estudario.ui.profile.Game.tint(0.035f))))
                .border(1.5.dp, if (isOpen) color else br.com.estudario.ui.profile.Game.tint(0.07f), cardShape)
                .clickable(onClick = onSelect)
                .padding(if (isOpen) 14.dp else 11.dp)
                .then(if (item.done && !isOpen) Modifier.alpha(0.55f) else Modifier),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.typeLabel.ifBlank { "Tarefa" }, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = g.readable(color))
                if (item.minutes > 0) Text("  ·  ${minutesText(item.minutes)}", style = MaterialTheme.typography.labelMedium, color = g.muted)
                Spacer(Modifier.weight(1f))
                if (isNow) Text(
                    "PRÓXIMO PASSO",
                    Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp)).background(color).padding(horizontal = 7.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = androidx.compose.ui.graphics.Color(0xFF14113A),
                    fontSize = androidx.compose.ui.unit.TextUnit(9.5f, androidx.compose.ui.unit.TextUnitType.Sp),
                )
            }
            Text(
                item.title,
                style = if (isOpen) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold, color = g.ink, maxLines = if (isOpen) 3 else 2, overflow = TextOverflow.Ellipsis,
                textDecoration = if (item.done && !isOpen) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
            )
            item.subject?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = g.muted, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            if (isOpen && onStart != null) {
                Spacer(Modifier.height(6.dp))
                val generate = onGenerate
                br.com.estudario.ui.brand.Button(
                    onClick = { if (generate != null) generate() else onStart() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF6C5CFF), contentColor = androidx.compose.ui.graphics.Color.White),
                ) {
                    Text(
                        when {
                            generate != null -> "Gerar o material deste passo"
                            item.done -> "Rever este passo"
                            item.inProgress -> "Continuar de onde parei"
                            item.kind == br.com.estudario.domain.planner.PlanTaskType.QUESTIONS -> "Resolver as questões"
                            item.kind == br.com.estudario.domain.planner.PlanTaskType.REVIEW -> "Fazer a revisão"
                            item.kind == br.com.estudario.domain.planner.PlanTaskType.THEORY -> "Estudar a teoria"
                            else -> "Começar este passo"
                        },
                        fontWeight = FontWeight.Black,
                    )
                }
            }
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
