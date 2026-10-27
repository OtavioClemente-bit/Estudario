package br.com.estudario.ui.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.animation.core.spring
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.BadgeProgress
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.StreakEngine
import br.com.estudario.domain.StreakSummary
import br.com.estudario.ui.components.ActivityHeatmap
import br.com.estudario.ui.components.HeatTone
import br.com.estudario.ui.components.ProfileAvatar
import br.com.estudario.ui.theme.EstudarioTheme

/**
 * O Perfil como tela de jogo, igual ao do site: cartão do jogador (liga, anel do nível, barra de XP
 * em segmentos e o HUD), missões de hoje, trilha de títulos, sala de troféus, combo de dias e
 * atributos. Os painéis são escuros nos dois temas, como a tela de um console; por dentro rodam no
 * tema escuro do app, para que barras, mapa e medalhas usem as cores certas sobre o fundo escuro.
 */

internal object Game {
    val bg = Color(0xFF12143A)
    val bg2 = Color(0xFF1C1B52)
    val ink = Color.White
    val muted = Color(0xFFA9ABD9)
    val line = Color.White.copy(alpha = 0.10f)
    val track = Color.White.copy(alpha = 0.08f)
    val mint = Color(0xFF34D399)
    val flame = Color(0xFFFF8A3D)
    val violet = Color(0xFF8B7CFF)
    val gold = Color(0xFFFFC94A)
}

/** A liga sai da faixa do nível ([levelTier]): bronze até o 9, prata, ouro, safira, rubi e esmeralda no 50. */
internal data class League(val name: String, val c1: Color, val c2: Color)

internal fun leagueOf(level: Int): League = when (levelTier(level)) {
    1 -> League("Bronze", Color(0xFFCD7F32), Color(0xFFF3B27A))
    2 -> League("Prata", Color(0xFF9AA6BD), Color(0xFFEEF2FA))
    3 -> League("Ouro", Color(0xFFF5B82E), Color(0xFFFFE08A))
    4 -> League("Safira", Color(0xFF3B82F6), Color(0xFF93C5FD))
    5 -> League("Rubi", Color(0xFFE5484D), Color(0xFFFF9AA2))
    else -> League("Esmeralda", Color(0xFF10B981), Color(0xFF6EE7B7))
}

private val AttrColors = listOf(Color(0xFF8B7CFF), Color(0xFF34D399), Color(0xFF60A5FA), Color(0xFFFFC94A), Color(0xFFF472B6), Color(0xFF22D3EE))

internal fun formatXp(xp: Int): String = "%,d".format(xp).replace(',', '.')

private val Kicker = 1.6.sp

// ---------------------------------------------------------------- moldura

/** Painel de console: fundo escuro fixo, borda fina e um título em caixa alta com a cor do destaque. */
@Composable
internal fun GamePanel(
    title: String,
    accent: Color,
    trailing: String? = null,
    onTrailing: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(24.dp)
    EstudarioTheme(darkTheme = true) {
        Column(
            Modifier.fillMaxWidth().clip(shape)
                .background(Brush.verticalGradient(listOf(Game.bg2, Game.bg)))
                .border(1.dp, Game.line, shape)
                .padding(18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).rotate(45f).background(accent))
                Spacer(Modifier.width(10.dp))
                Text(title.uppercase(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Game.ink, letterSpacing = Kicker, modifier = Modifier.weight(1f))
                if (trailing != null) Row(
                    Modifier.clip(RoundedCornerShape(50)).then(if (onTrailing != null) Modifier.clickable(onClick = onTrailing) else Modifier).padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(trailing, style = MaterialTheme.typography.labelMedium, color = accent, fontWeight = FontWeight.Bold)
                    if (onTrailing != null) Icon(Icons.Outlined.ChevronRight, null, Modifier.size(18.dp), tint = accent)
                }
            }
            Spacer(Modifier.height(14.dp))
            content()
        }
    }
}

// ---------------------------------------------------------------- cartão do jogador

@Composable
internal fun PlayerCard(
    profile: UserProfile,
    progress: ProgressEngine.ProgressSummary,
    streak: StreakSummary?,
    mission: Pair<Int, Int>?,
    onEditName: () -> Unit,
    onChangePhoto: () -> Unit,
    onBadges: () -> Unit,
) {
    val league = leagueOf(progress.level)
    val shape = RoundedCornerShape(28.dp)
    val loop = rememberInfiniteTransition(label = "cartao")
    val shine by loop.animateFloat(-0.6f, 1.6f, infiniteRepeatable(tween(5_200, easing = LinearEasing)), label = "cartao-brilho")
    EstudarioTheme(darkTheme = true) {
        Column(
            Modifier.fillMaxWidth().clip(shape)
                .background(Brush.linearGradient(listOf(Game.bg2, Game.bg, Color(0xFF0B0C26))))
                .drawBehind {
                    // Grade fina de holograma e o brilho da liga no canto.
                    val step = 22.dp.toPx()
                    var x = 0f
                    while (x < size.width) { drawLine(Color.White.copy(alpha = 0.035f), Offset(x, 0f), Offset(x, size.height)); x += step }
                    var y = 0f
                    while (y < size.height) { drawLine(Color.White.copy(alpha = 0.035f), Offset(0f, y), Offset(size.width, y)); y += step }
                    drawCircle(Brush.radialGradient(listOf(league.c1.copy(alpha = 0.38f), Color.Transparent), Offset(size.width, 0f), size.width * 0.7f), size.width * 0.7f, Offset(size.width, 0f))
                }
                .drawWithContent {
                    drawContent()
                    // Faixa de luz que atravessa o cartão de tempos em tempos.
                    val cx = size.width * shine
                    drawRect(Brush.linearGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.07f), Color.Transparent), Offset(cx - 120f, 0f), Offset(cx + 120f, size.height)))
                }
                .border(1.5.dp, Brush.linearGradient(listOf(league.c2.copy(alpha = 0.7f), Game.line, league.c1.copy(alpha = 0.5f))), shape)
                .padding(18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("CARTÃO DO JOGADOR", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Game.muted, letterSpacing = Kicker, modifier = Modifier.weight(1f))
                Row(
                    Modifier.clip(RoundedCornerShape(50)).background(league.c1.copy(alpha = 0.18f)).border(1.dp, league.c1, RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Rounded.EmojiEvents, null, Modifier.size(14.dp), tint = league.c2)
                    Spacer(Modifier.width(4.dp))
                    Text("LIGA ${league.name.uppercase()}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = league.c2, letterSpacing = 1.sp)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                PlayerAvatar(profile, progress, league, onChangePhoto)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(progress.levelTitle.uppercase(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = league.c2, letterSpacing = Kicker)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            profile.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Game.ink,
                            maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false),
                        )
                        Box(Modifier.size(32.dp).clip(CircleShape).clickable(onClick = onEditName), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Edit, "Editar nome", Modifier.size(16.dp), tint = Game.muted)
                        }
                    }
                    Text(
                        if (profile.signedIn) profile.email else "Sem conta: o progresso fica só neste celular",
                        style = MaterialTheme.typography.bodySmall, color = Game.muted, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            XpBar(progress, league)
            if (streak != null) {
                Spacer(Modifier.height(16.dp))
                Hud(progress, streak, mission, onBadges)
            }
        }
    }
}

/** O avatar dentro do anel do nível, com um anel tracejado girando por fora e a placa "NV n". */
@Composable
private fun PlayerAvatar(profile: UserProfile, progress: ProgressEngine.ProgressSummary, league: League, onChangePhoto: () -> Unit) {
    val fraction by animateFloatAsState(progress.levelProgress, tween(1_000, easing = FastOutSlowInEasing), label = "anel")
    val spin by rememberInfiniteTransition(label = "anel-giro").animateFloat(0f, 360f, infiniteRepeatable(tween(18_000, easing = LinearEasing)), label = "giro")
    Box(Modifier.size(116.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(116.dp)) {
            val dash = 3.dp.toPx()
            rotate(spin) {
                drawCircle(league.c2.copy(alpha = 0.45f), size.minDimension / 2 - 1.dp.toPx(), style = Stroke(1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash * 2))))
            }
            val stroke = 6.dp.toPx()
            val inset = 8.dp.toPx() + stroke / 2
            val arc = Size(size.width - inset * 2, size.height - inset * 2)
            drawArc(Game.track, 0f, 360f, false, Offset(inset, inset), arc, style = Stroke(stroke))
            drawArc(
                Brush.sweepGradient(listOf(league.c1, league.c2, league.c1)),
                90f, 360f * fraction.coerceAtLeast(0.02f), false, Offset(inset, inset), arc, style = Stroke(stroke, cap = StrokeCap.Round),
            )
        }
        Box(Modifier.clip(CircleShape).clickable(onClick = onChangePhoto)) { ProfileAvatar(profile.photoPath, profile.initials, 84.dp) }
        Box(
            Modifier.align(Alignment.BottomCenter).offset(y = 2.dp).clip(RoundedCornerShape(8.dp))
                .background(Brush.horizontalGradient(listOf(league.c1, league.c2)))
                .border(2.dp, Game.bg, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 2.dp),
        ) { Text("NV ${progress.level}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Game.bg) }
        Box(
            Modifier.align(Alignment.TopEnd).offset(x = (-4).dp, y = 6.dp).size(28.dp).clip(CircleShape).background(Game.bg2)
                .border(1.5.dp, league.c2, CircleShape).clickable(onClick = onChangePhoto),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Rounded.PhotoCamera, "Trocar imagem", Modifier.size(15.dp), tint = league.c2) }
    }
}

/** Barra de XP em segmentos, com brilho correndo, e o que falta para o próximo nível. */
@Composable
private fun XpBar(progress: ProgressEngine.ProgressSummary, league: League) {
    val fraction by animateFloatAsState(progress.levelProgress, tween(1_000, easing = FastOutSlowInEasing), label = "xp")
    val run by rememberInfiniteTransition(label = "xp-brilho").animateFloat(-0.2f, 1.2f, infiniteRepeatable(tween(2_400, easing = LinearEasing)), label = "xp-x")
    val segments = 12
    Row(verticalAlignment = Alignment.Bottom) {
        Text(formatXp(progress.xpIntoLevel), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Game.ink)
        Text(" / ${formatXp(progress.xpForNextLevel)} XP", style = MaterialTheme.typography.labelMedium, color = Game.muted, modifier = Modifier.padding(bottom = 2.dp))
        Spacer(Modifier.weight(1f))
        if (progress.xpToday > 0) Text(
            "+${progress.xpToday} hoje", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Game.mint,
            modifier = Modifier.clip(RoundedCornerShape(50)).background(Game.mint.copy(alpha = 0.14f)).padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
    Spacer(Modifier.height(8.dp))
    Canvas(Modifier.fillMaxWidth().height(16.dp)) {
        val gap = 3.dp.toPx()
        val w = (size.width - gap * (segments - 1)) / segments
        val filled = fraction * segments
        val radius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx())
        repeat(segments) { i ->
            val left = i * (w + gap)
            drawRoundRect(Game.track, Offset(left, 0f), Size(w, size.height), radius)
            val part = (filled - i).coerceIn(0f, 1f)
            if (part > 0f) drawRoundRect(
                Brush.horizontalGradient(listOf(league.c1, league.c2), 0f, size.width),
                Offset(left, 0f), Size(w * part, size.height), radius,
            )
        }
        val cx = size.width * fraction * run
        if (fraction > 0f) drawRect(
            Brush.horizontalGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.55f), Color.Transparent), cx - 40f, cx + 40f),
            Offset(0f, 0f), Size(size.width * fraction, size.height),
        )
    }
    Spacer(Modifier.height(6.dp))
    val faltam = (progress.xpForNextLevel - progress.xpIntoLevel).coerceAtLeast(0)
    Text("Faltam ${formatXp(faltam)} XP para o nível ${progress.level + 1}", style = MaterialTheme.typography.labelSmall, color = Game.muted)
}

/** Os quatro números do HUD: combo, XP, troféus e meta do dia. */
@Composable
private fun Hud(progress: ProgressEngine.ProgressSummary, streak: StreakSummary, mission: Pair<Int, Int>?, onBadges: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HudStat(Modifier.weight(1f), "${streak.current}", if (streak.current == 1) "dia de combo" else "dias de combo", "recorde ${streak.best}", Game.flame) {
                StreakFlame(lit = streak.current > 0, size = 30.dp, glow = false)
            }
            HudStat(Modifier.weight(1f), formatXp(progress.totalXp), "XP total", "+${progress.xpThisWeek} semana", Game.violet) {
                Icon(Icons.Rounded.Bolt, null, Modifier.size(24.dp), tint = Game.violet)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HudStat(Modifier.weight(1f).gameClick(onBadges), "${progress.earnedBadges.size}", "troféus", "de ${progress.badges.size}", Game.gold) {
                Icon(Icons.Rounded.EmojiEvents, null, Modifier.size(24.dp), tint = Game.gold)
            }
            if (mission != null) {
                val (done, total) = mission
                HudStat(
                    Modifier.weight(1f), "$done/$total", "missão do dia", if (done >= total) "concluída" else "do plano", Game.mint,
                ) { GoalDial(done.toFloat() / total, done >= total) }
            } else {
                val goal = streak.goal.questions.coerceAtLeast(1)
                HudStat(
                    Modifier.weight(1f), "${streak.todayQuestions}/$goal", "questões hoje", if (streak.todayQuestions >= goal) "meta batida" else "meta do dia", Game.mint,
                ) { GoalDial((streak.todayQuestions.toFloat() / goal).coerceAtMost(1f), streak.todayQuestions >= goal) }
            }
        }
    }
}

@Composable
private fun HudStat(modifier: Modifier, value: String, label: String, sub: String, accent: Color, icon: @Composable () -> Unit) {
    Row(
        modifier.clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha = 0.05f)).border(1.dp, Game.line, RoundedCornerShape(16.dp)).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(accent.copy(alpha = 0.14f)), contentAlignment = Alignment.Center) { icon() }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Game.ink, maxLines = 1)
            Text(label, style = MaterialTheme.typography.labelSmall, color = Game.ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(sub, style = MaterialTheme.typography.labelSmall, color = accent, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun GoalDial(fraction: Float, done: Boolean) {
    Box(contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(26.dp)) {
            val stroke = 3.5.dp.toPx()
            val inset = stroke / 2
            val arc = Size(size.width - stroke, size.height - stroke)
            drawArc(Game.track, 0f, 360f, false, Offset(inset, inset), arc, style = Stroke(stroke))
            drawArc(Game.mint, -90f, 360f * fraction, false, Offset(inset, inset), arc, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        if (done) Icon(Icons.Rounded.Check, null, Modifier.size(14.dp), tint = Game.mint)
    }
}

// ---------------------------------------------------------------- toque de jogo

/** Toque de botão de controle: afunda ao apertar e volta com mola. */
@Composable
internal fun Modifier.gameClick(onClick: () -> Unit): Modifier {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.94f else 1f, spring(dampingRatio = 0.42f, stiffness = 700f), label = "toque")
    return this.graphicsLayer { scaleX = scale; scaleY = scale }.clickable(source, LocalIndication.current, onClick = onClick)
}

// ---------------------------------------------------------------- missão do dia

/**
 * A missão do dia é o dia do plano (as tarefas de hoje, a mesma da tela inicial). [mission] vem como
 * feitas/total, ou nulo quando hoje não há tarefa. Embaixo, as questões do dia, o combo e o próximo troféu.
 */
@Composable
internal fun MissionsPanel(progress: ProgressEngine.ProgressSummary, streak: StreakSummary, mission: Pair<Int, Int>?, onOpenPlan: () -> Unit, onBadges: () -> Unit) {
    val goal = streak.goal.questions.coerceAtLeast(1)
    GamePanel("Missão do dia", Game.mint, trailing = mission?.let { "${it.first} de ${it.second}" }) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (mission != null) {
                val (done, total) = mission
                Quest(
                    Icons.Rounded.Flag, Game.mint, "Termine a missão do dia", done.toFloat() / total, done >= total,
                    if (done >= total) "Todas as tarefas de hoje feitas" else "$done de $total ${if (total == 1) "tarefa" else "tarefas"} do plano · faltam ${total - done}",
                    onClick = onOpenPlan,
                )
            } else Quest(
                Icons.Rounded.CalendarMonth, Game.mint, "Dia livre no plano", 0f, false,
                "Sem tarefas hoje: as questões e as revisões contam", onClick = onOpenPlan,
            )
            Quest(
                Icons.Rounded.Quiz, Game.violet, "Resolva $goal ${if (goal == 1) "questão" else "questões"}",
                (streak.todayQuestions.toFloat() / goal).coerceAtMost(1f), streak.todayQuestions >= goal,
                "${streak.todayQuestions} de $goal hoje",
            )
            Quest(
                Icons.Rounded.LocalFireDepartment, Game.flame,
                if (streak.current > 0) "Mantenha o combo de ${streak.current} ${if (streak.current == 1) "dia" else "dias"}" else "Comece um combo de dias",
                if (streak.todayDone) 1f else streak.todayProgress, streak.todayDone,
                if (streak.todayDone) "Hoje já conta no combo" else "1 tarefa do plano, 1 revisão ou $goal questões salvam o dia",
            )
            progress.nextBadges.firstOrNull()?.let { next ->
                Quest(
                    Icons.Rounded.EmojiEvents, Game.gold, "Desbloqueie: ${next.badge.name}", next.percent, false,
                    next.hint ?: "faltam ${next.remaining} ${next.badge.unit}", onClick = onBadges,
                )
            }
        }
    }
}

@Composable
private fun Quest(icon: ImageVector, accent: Color, title: String, fraction: Float, complete: Boolean, sub: String, onClick: (() -> Unit)? = null) {
    val shown by animateFloatAsState(fraction, tween(800), label = "missao")
    val pop by animateFloatAsState(if (complete) 1f else 0.85f, spring(dampingRatio = 0.35f, stiffness = 400f), label = "missao-pop")
    Row(
        Modifier.fillMaxWidth()
            .then(if (onClick != null) Modifier.gameClick(onClick) else Modifier)
            .clip(RoundedCornerShape(16.dp))
            .background(if (complete) accent.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.04f))
            .border(1.dp, if (complete) accent.copy(alpha = 0.5f) else Game.line, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(38.dp).graphicsLayer { scaleX = pop; scaleY = pop }.clip(RoundedCornerShape(12.dp))
                .background(if (complete) accent else accent.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(if (complete) Icons.Rounded.Check else icon, null, Modifier.size(22.dp), tint = if (complete) Game.bg else accent)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Game.ink, modifier = Modifier.weight(1f))
                if (complete) Text(
                    "COMPLETA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Game.bg, letterSpacing = 1.sp,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(accent).padding(horizontal = 6.dp, vertical = 1.dp),
                ) else Text("${(fraction * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = accent)
            }
            Spacer(Modifier.height(6.dp))
            Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)).background(Game.track)) {
                Box(Modifier.fillMaxWidth(shown.coerceIn(0f, 1f)).fillMaxHeight().clip(RoundedCornerShape(50)).background(accent))
            }
            Spacer(Modifier.height(4.dp))
            Text(sub, style = MaterialTheme.typography.labelSmall, color = Game.muted, letterSpacing = 0.2.sp)
        }
    }
}

// ---------------------------------------------------------------- trilha de títulos

@Composable
internal fun TitlesTrail(level: Int) {
    val league = leagueOf(level)
    val stops = listOf(1) + (5..50 step 5).toList()
    val current = stops.last { it <= level }
    val pulse by rememberInfiniteTransition(label = "trilha").animateFloat(0.55f, 1f, infiniteRepeatable(tween(1_100), RepeatMode.Reverse), label = "pulso")
    val scroll = rememberScrollState()
    val nodeWidth = 96.dp
    val px = with(LocalDensity.current) { nodeWidth.toPx() }
    LaunchedEffect(current) { scroll.scrollTo(((stops.indexOf(current) - 1).coerceAtLeast(0) * px).toInt()) }
    val next = ProgressEngine.nextTitle(level)
    GamePanel("Trilha de títulos", league.c2, trailing = "NV $level") {
        Row(Modifier.fillMaxWidth().horizontalScroll(scroll)) {
            stops.forEachIndexed { i, stop ->
                val reached = stop <= level
                val now = stop == current
                Column(Modifier.width(nodeWidth), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.fillMaxWidth().height(52.dp), contentAlignment = Alignment.Center) {
                        // Trilho entre as fases: aceso até onde a pessoa chegou.
                        Row(Modifier.fillMaxWidth().height(4.dp)) {
                            Box(Modifier.weight(1f).fillMaxHeight().background(if (i == 0) Color.Transparent else if (reached) league.c1 else Game.track))
                            Box(Modifier.weight(1f).fillMaxHeight().background(if (i == stops.lastIndex) Color.Transparent else if (stops[i + 1] <= level) league.c1 else Game.track))
                        }
                        Box(
                            Modifier.size(if (now) 42.dp else 34.dp).rotate(45f).clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        now -> Brush.linearGradient(listOf(league.c1, league.c2))
                                        reached -> Brush.linearGradient(listOf(league.c1.copy(alpha = 0.75f), league.c1.copy(alpha = 0.55f)))
                                        else -> Brush.linearGradient(listOf(Color.White.copy(alpha = 0.10f), Color.White.copy(alpha = 0.06f)))
                                    },
                                )
                                .border(if (now) 2.dp else 1.dp, if (now) Color.White.copy(alpha = pulse) else if (reached) league.c2.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.28f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(Modifier.rotate(-45f), contentAlignment = Alignment.Center) {
                                if (reached) Text("$stop", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Game.bg)
                                else Icon(Icons.Rounded.Lock, null, Modifier.size(15.dp), tint = Color(0xFFD6D2FA))
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        ProgressEngine.levelTitle(stop), style = MaterialTheme.typography.bodySmall, fontSize = 12.sp, letterSpacing = 0.sp, lineHeight = 14.sp,
                        fontWeight = if (now) FontWeight.Black else FontWeight.Medium,
                        color = if (now) league.c2 else if (reached) Game.ink else Color(0xFFCFCBF5),
                        textAlign = TextAlign.Center, maxLines = 2, modifier = Modifier.padding(horizontal = 4.dp),
                    )
                    if (!reached) Text("nível $stop", style = MaterialTheme.typography.labelSmall, color = Game.muted, fontSize = 10.sp, letterSpacing = 0.sp)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            if (next != null) "Próximo título: ${next.second}, no nível ${next.first}" else "Você chegou ao último título da trilha.",
            style = MaterialTheme.typography.bodySmall, color = Game.muted,
        )
    }
}

// ---------------------------------------------------------------- troféus

@Composable
internal fun TrophyRoom(progress: ProgressEngine.ProgressSummary, onBadges: () -> Unit) {
    val earned = progress.earnedBadges
    val total = progress.badges.size.coerceAtLeast(1)
    val locked = progress.nextBadges.take(3)
    GamePanel("Sala de troféus", Game.gold, trailing = "Ver todos", onTrailing = onBadges) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("${earned.size}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Game.gold)
            Text(" de ${progress.badges.size} troféus", style = MaterialTheme.typography.bodyMedium, color = Game.muted, modifier = Modifier.padding(bottom = 4.dp))
        }
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)).background(Game.track)) {
            Box(Modifier.fillMaxWidth(earned.size.toFloat() / total).fillMaxHeight().clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Game.gold))))
        }
        Spacer(Modifier.height(14.dp))
        // Na vitrine, os três últimos ganhos; embaixo, os três mais perto de sair, trancados.
        if (earned.isNotEmpty()) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            earned.takeLast(3).reversed().forEach { row -> TrophySlot(Modifier.weight(1f), row, true, onBadges) }
            repeat(3 - earned.takeLast(3).size) { Spacer(Modifier.weight(1f)) }
        }
        if (earned.isNotEmpty() && locked.isNotEmpty()) Spacer(Modifier.height(8.dp))
        if (locked.isNotEmpty()) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            locked.forEach { row -> TrophySlot(Modifier.weight(1f), row, false, onBadges) }
            repeat(3 - locked.size) { Spacer(Modifier.weight(1f)) }
        }
        if (earned.isEmpty()) {
            Spacer(Modifier.height(10.dp))
            Text("Os primeiros troféus saem rápido: resolva questões e feche a meta do dia.", style = MaterialTheme.typography.bodySmall, color = Game.muted)
        }
    }
}

@Composable
private fun TrophySlot(modifier: Modifier, row: BadgeProgress, earned: Boolean, onClick: () -> Unit) {
    Column(
        modifier.gameClick(onClick).clip(RoundedCornerShape(16.dp))
            .background(if (earned) Brush.verticalGradient(listOf(Game.gold.copy(alpha = 0.16f), Color.Transparent)) else Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.04f), Color.White.copy(alpha = 0.04f))))
            .border(1.dp, if (earned) Game.gold.copy(alpha = 0.4f) else Game.line, RoundedCornerShape(16.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            BadgeMedal(row.badge, earned = earned, size = 48.dp)
            if (!earned) Box(Modifier.align(Alignment.BottomEnd).size(18.dp).clip(CircleShape).background(Game.bg).border(1.dp, Game.line, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Lock, null, Modifier.size(10.dp), tint = Game.muted)
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(row.badge.name, style = MaterialTheme.typography.labelSmall, letterSpacing = 0.sp, color = if (earned) Game.ink else Game.muted, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
        if (!earned) Text("${(row.percent * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Game.gold)
    }
}

// ---------------------------------------------------------------- combo

@Composable
internal fun ComboPanel(streak: StreakSummary) {
    GamePanel("Combo de dias", Game.flame, trailing = "recorde ${streak.best}") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StreakFlame(lit = streak.current > 0, size = 76.dp, frozen = streak.current == 0 && streak.best > 0)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("${streak.current}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = if (streak.current > 0) Game.flame else Game.ink)
                Text(if (streak.current == 1) "dia de combo" else "dias de combo", style = MaterialTheme.typography.bodyMedium, color = Game.muted)
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            streak.week.forEach { day -> WeekDot(if (day.protected) day.copy(done = true) else day, Color(0xFFFF7A2F)) }
        }
        Spacer(Modifier.height(14.dp))
        // Escudos: a folga que o Folha guarda a cada 7 dias seguidos e que salva o combo num dia sem estudo.
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.04f)).border(1.dp, Game.line, RoundedCornerShape(14.dp)).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(StreakEngine.MAX_FREEZES) { i ->
                Icon(Icons.Rounded.Shield, null, Modifier.size(24.dp), tint = if (i < streak.freezes) Color(0xFF60A5FA) else Game.track)
            }
            Spacer(Modifier.width(10.dp))
            Text(
                when {
                    streak.freezes > 0 && streak.daysToNextFreeze > 0 -> "${streak.freezes} ${if (streak.freezes == 1) "escudo guardado" else "escudos guardados"}. Outro em ${streak.daysToNextFreeze} ${if (streak.daysToNextFreeze == 1) "dia" else "dias"}."
                    streak.freezes > 0 -> "${streak.freezes} escudos guardados, o máximo."
                    else -> "Mais ${streak.daysToNextFreeze} ${if (streak.daysToNextFreeze == 1) "dia seguido" else "dias seguidos"} e você ganha um escudo: num dia sem estudo, ele salva o combo."
                },
                style = MaterialTheme.typography.bodySmall, color = Game.muted, modifier = Modifier.weight(1f),
            )
        }
        if (streak.week.any { it.protected }) {
            Spacer(Modifier.height(6.dp))
            Text("Um escudo salvou um dia desta semana.", style = MaterialTheme.typography.labelSmall, color = Color(0xFF60A5FA))
        }
        Spacer(Modifier.height(14.dp))
        ActivityHeatmap(streak.calendar, streak.goal, contentPadding = PaddingValues(0.dp), tone = HeatTone.Fire)
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ComboStat("${streak.activeDays}", "dias jogados")
            ComboStat(formatXp(streak.totalQuestions), "questões")
            ComboStat("${streak.totalMinutes / 60}h${"%02d".format(streak.totalMinutes % 60)}", "de estudo")
        }
    }
}

@Composable
private fun ComboStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Game.ink)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Game.muted)
    }
}

// ---------------------------------------------------------------- atributos

/** De onde veio o XP, como barras de atributo de RPG. */
@Composable
internal fun AttributesPanel(progress: ProgressEngine.ProgressSummary) {
    if (progress.sources.isEmpty()) return
    val max = progress.sources.maxOf { it.xp }.coerceAtLeast(1)
    val percents = wholePercents(progress.sources.map { it.xp })
    GamePanel("Atributos", Game.violet, trailing = "+${formatXp(progress.xpThisWeek)} XP na semana") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            progress.sources.forEachIndexed { i, source ->
                val color = AttrColors[i % AttrColors.size]
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(source.label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = Game.ink, modifier = Modifier.weight(1f))
                        Text("${formatXp(source.xp)} XP", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = color)
                        Text("  ${percents[i]}%", style = MaterialTheme.typography.labelSmall, color = Game.muted)
                    }
                    Spacer(Modifier.height(5.dp))
                    SegmentBar(source.xp.toFloat() / max, color)
                }
            }
        }
    }
}

@Composable
private fun SegmentBar(fraction: Float, color: Color, segments: Int = 20, height: Dp = 8.dp) {
    val shown by animateFloatAsState(fraction, tween(900, easing = FastOutSlowInEasing), label = "atributo")
    Canvas(Modifier.fillMaxWidth().height(height)) {
        val gap = 2.dp.toPx()
        val w = (size.width - gap * (segments - 1)) / segments
        val filled = shown * segments
        repeat(segments) { i ->
            val on = (filled - i).coerceIn(0f, 1f)
            drawRect(Game.track, Offset(i * (w + gap), 0f), Size(w, size.height))
            if (on > 0f) drawRect(color, Offset(i * (w + gap), 0f), Size(w * on, size.height))
        }
    }
}
