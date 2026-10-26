package br.com.estudario.ui.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.StreakSummary
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.brand.drawGlyph
import br.com.estudario.ui.brand.turmaMotion
import kotlin.math.PI
import kotlin.math.sin

/**
 * O que dá vida ao Perfil: o livro do nível, o avatar no anel de progresso,
 * o fogo da sequência e os quatro números do topo (os mesmos do site).
 */

/** A faixa do selo pelo nível: bronze até o 9, prata do 10, ouro do 20, safira, rubi e esmeralda no 50. */
internal fun levelTier(level: Int): Int = (level / 10 + 1).coerceIn(1, 6)

/** O avatar dentro do anel do nível: o arco sai de baixo (do número do nível) e dá a volta até ele. */
@Composable
internal fun LevelAvatar(progress: ProgressEngine.ProgressSummary?, size: Dp, content: @Composable () -> Unit) {
    val fraction by animateFloatAsState(progress?.levelProgress ?: 0f, tween(900, easing = FastOutSlowInEasing), label = "anel-nivel")
    val primary = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.surfaceContainerHighest
    val tierColor = progress?.let { tierPalette(levelTier(it.level)).light } ?: primary
    Box(Modifier.size(size + 22.dp), contentAlignment = Alignment.Center) {
        if (progress != null) RaysGlow(primary)
        Canvas(Modifier.size(size + 14.dp)) {
            val stroke = 5.dp.toPx()
            val inset = stroke / 2
            val arcSize = androidx.compose.ui.geometry.Size(this.size.width - stroke, this.size.height - stroke)
            drawArc(track, 0f, 360f, false, Offset(inset, inset), arcSize, style = Stroke(stroke))
            drawArc(
                Brush.sweepGradient(listOf(Color(0xFFFFB421), primary, Color(0xFF22C59A), tierColor, Color(0xFFFFB421))),
                90f, 360f * fraction, false, Offset(inset, inset), arcSize, style = Stroke(stroke, cap = StrokeCap.Round),
            )
        }
        content()
        // A medalha do livro de Folhas, com o número do nível (bronze, prata, ouro, ouro radiante).
        if (progress != null) LevelBookImage(progress.level, 40.dp, Modifier.align(Alignment.BottomCenter).offset(y = 8.dp))
    }
}

/** A chama da turma do Folha, nas cores originais: respira pela base e solta pequenas brasas. */
@Composable
internal fun StreakFlame(lit: Boolean, size: Dp, glow: Boolean = true, frozen: Boolean = false) {
    // Sem sequência não há animação ativa: só o personagem em repouso.
    val phase = if (lit) {
        val loop = rememberInfiniteTransition(label = "chama-viva")
        val time by loop.animateFloat(0f, (2 * PI).toFloat(), infiniteRepeatable(tween(4_800, easing = LinearEasing)), label = "chama-ciclo")
        time
    } else 0f
    Canvas(Modifier.size(size).turmaMotion(
        Glyph.Fire, directTouch = true,
        onClickLabel = if (frozen) "Animar a chama congelada" else "Animar a chama da sequência",
        animation = if (frozen) "wiggle" else null,
    )) {
        val side = this.size.minDimension
        if (frozen) drawStreakIce(front = false)
        val breath = if (lit) sin(phase * 4f) else 0f
        if (lit && glow) {
            val halo = Offset(center.x, side * 0.6f)
            drawCircle(Brush.radialGradient(listOf(Color(0xFFFFAE42).copy(alpha = 0.20f + breath * 0.025f), Color.Transparent), halo, side * 0.49f), side * 0.49f, halo)
            drawOval(Color(0xFFF28435).copy(alpha = 0.12f), Offset(side * 0.25f, side * 0.85f), Size(side * 0.5f, side * 0.07f))
        }
        val anchor = Offset(center.x, side * 0.88f)
        rotate(if (lit) sin(phase * 2f) * 2.6f else 0f, anchor) {
            scale(if (frozen) 0.72f else 0.93f - breath * 0.015f, if (frozen) 0.72f else 0.93f + breath * 0.035f, if (frozen) center else anchor) {
                // Sem a tinta SrcIn, que apagava o amarelo, o rosto e o contorno da marca.
                drawGlyph(Glyph.Fire, muted = !lit)
            }
        }
        if (frozen) drawStreakIce(front = true)
        if (lit && glow) repeat(if (size >= 64.dp) 4 else 2) { i ->
            val travel = (phase / (2 * PI).toFloat() * 2f + i * 0.27f) % 1f
            val alpha = sin(travel * PI).toFloat() * 0.75f
            val x = side * (if (i % 2 == 0) 0.16f else 0.84f) + sin(phase * 2f + i) * side * 0.035f
            val y = side * (0.63f - travel * 0.48f)
            drawCircle(if (i % 2 == 0) Color(0xFFFFB421) else Color(0xFFFF7847), side * (0.018f - travel * 0.008f), Offset(x, y), alpha)
        }
    }
}

/** Os quatro números do topo, como no site: sequência, XP, emblemas e a meta do dia. */
@Composable
internal fun ProfileKpis(
    progress: ProgressEngine.ProgressSummary?,
    streak: StreakSummary?,
    onBadges: () -> Unit,
) {
    if (progress == null || streak == null) return
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Kpi(
                Modifier.weight(1f), "${streak.current}", if (streak.current == 1) "dia seguido" else "dias seguidos",
                "Recorde: ${streak.best}",
            ) { StreakFlame(lit = streak.current > 0, size = 40.dp) }
            Kpi(
                Modifier.weight(1f), "%,d".format(progress.totalXp).replace(',', '.'), "XP no total",
                if (progress.xpToday > 0) "+${progress.xpToday} hoje" else "${progress.xpThisWeek} XP na semana",
                good = progress.xpToday > 0,
            ) { KpiIcon(Icons.Rounded.Bolt, Color(0xFF8B7CFF)) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Kpi(
                Modifier.weight(1f).clip(RoundedCornerShape(20.dp)).clickable(onClick = onBadges),
                "${progress.earnedBadges.size}", "de ${progress.badges.size} emblemas",
                progress.nextBadges.firstOrNull()?.let { "Próximo: ${it.badge.name}" } ?: "Coleção completa",
            ) { ProfileEmblemsIcon() }
            val goal = streak.goal.questions.coerceAtLeast(1)
            val done = streak.todayQuestions
            Kpi(
                Modifier.weight(1f), "$done", if (streak.todayDone) "meta batida!" else "de $goal questões",
                if (streak.todayDone) "Dia salvo na sequência" else "Meta do dia · ${(streak.todayProgress * 100).toInt()}%",
                good = streak.todayDone,
            ) { ProfileDailyGoalIcon(streak.todayProgress, streak.todayDone) }
        }
    }
}

@Composable
private fun Kpi(modifier: Modifier, value: String, label: String, sub: String, good: Boolean = false, icon: @Composable () -> Unit) {
    Row(
        modifier.clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        icon()
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, maxLines = 1)
            }
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
            Text(
                sub, style = MaterialTheme.typography.labelSmall, maxLines = 1,
                color = if (good) Color(0xFF22C59A) else MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun KpiIcon(icon: ImageVector, color: Color) {
    Box(Modifier.size(40.dp).clip(CircleShape).background(color.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) {
        Icon(icon, null, Modifier.size(24.dp), tint = color)
    }
}
