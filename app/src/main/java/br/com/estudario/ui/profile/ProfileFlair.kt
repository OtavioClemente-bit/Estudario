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
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.StreakSummary
import br.com.estudario.ui.brand.Icon

/**
 * O que dá vida ao Perfil: o selo do nível com os raios das medalhas, o avatar no anel do nível,
 * o fogo da sequência e os quatro números do topo (os mesmos do site).
 */

/** A faixa do selo pelo nível: bronze até o 9, prata do 10, ouro do 20, safira, rubi e esmeralda no 50. */
internal fun levelTier(level: Int): Int = (level / 10 + 1).coerceIn(1, 6)

/** Selo do nível: hexágono no metal da faixa, com brilho e raios girando em volta, como as medalhas. */
@Composable
internal fun LevelSeal(level: Int, size: Dp = 112.dp) {
    val palette = tierPalette(levelTier(level))
    val shine by rememberInfiniteTransition(label = "selo").animateFloat(
        -0.6f, 1.6f, infiniteRepeatable(tween(3_200, easing = LinearEasing)), label = "selo-brilho",
    )
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        RaysGlow(palette.dark)
        Canvas(Modifier.size(size * 0.68f).graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }) {
            val r = this.size.minDimension / 2
            val c = center
            fun hex(radius: Float) = Path().apply {
                repeat(6) { i ->
                    val a = Math.toRadians(-90.0 + i * 60.0)
                    val x = c.x + radius * kotlin.math.cos(a).toFloat()
                    val y = c.y + radius * kotlin.math.sin(a).toFloat()
                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }
            drawPath(hex(r), palette.rim)
            drawPath(hex(r * 0.9f), Brush.linearGradient(listOf(palette.light, palette.dark), Offset(0f, 0f), Offset(this.size.width, this.size.height)))
            drawPath(hex(r * 0.72f), Brush.linearGradient(listOf(Color(0xFF6A4CF0), Color(0xFF2B1C8F)), Offset(0f, 0f), Offset(this.size.width, this.size.height)))
            drawPath(hex(r * 0.72f), palette.light.copy(alpha = 0.8f), style = Stroke(1.5.dp.toPx()))
            // o reflexo passando na diagonal, só dentro do selo
            val x = shine * this.size.width
            drawPath(
                hex(r),
                Brush.linearGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.5f), Color.Transparent), Offset(x - r * 0.5f, 0f), Offset(x + r * 0.5f, this.size.height)),
                blendMode = BlendMode.SrcAtop,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("NÍVEL", fontSize = (size.value * 0.085f).sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.85f), letterSpacing = 1.sp)
            Text("$level", fontSize = (size.value * 0.26f).sp, lineHeight = (size.value * 0.27f).sp, fontWeight = FontWeight.Black, color = Color.White)
        }
    }
}

/** O avatar dentro do anel do nível: o arco cheio é o quanto já andou para o próximo. */
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
                Brush.sweepGradient(listOf(primary, Color(0xFF22C59A), tierColor, primary)),
                -90f, 360f * fraction, false, Offset(inset, inset), arcSize, style = Stroke(stroke, cap = StrokeCap.Round),
            )
        }
        content()
        if (progress != null) Text(
            "${progress.level}",
            Modifier.align(Alignment.BottomCenter).offset(y = 2.dp).clip(RoundedCornerShape(50))
                .background(Brush.horizontalGradient(listOf(Color(0xFF6A4CF0), Color(0xFF22C59A))))
                .padding(horizontal = 10.dp, vertical = 2.dp),
            color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp,
        )
    }
}

/** O fogo da sequência: chama em degradê que tremula; apagada (cinza) sem sequência. */
@Composable
internal fun StreakFlame(lit: Boolean, size: Dp, glow: Boolean = true) {
    val wave = rememberInfiniteTransition(label = "fogo")
    val flicker by wave.animateFloat(0.93f, 1.07f, infiniteRepeatable(tween(520, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "fogo-altura")
    val sway by wave.animateFloat(-4f, 4f, infiniteRepeatable(tween(760, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "fogo-balanço")
    val gray = MaterialTheme.colorScheme.outline
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        if (lit && glow) Canvas(Modifier.fillMaxSize()) {
            drawCircle(Brush.radialGradient(listOf(Color(0xFFFF8A3D).copy(alpha = 0.45f * flicker), Color.Transparent), center, this.size.minDimension / 2))
        }
        Icon(
            Icons.Rounded.LocalFireDepartment, null,
            Modifier.size(size * 0.78f)
                .graphicsLayer {
                    if (lit) { scaleY = flicker; scaleX = 2f - flicker * 0.98f; rotationZ = sway }
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f)
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .drawWithContent {
                    drawContent()
                    if (lit) drawRect(Brush.verticalGradient(listOf(Color(0xFFFFD25A), Color(0xFFFF7A2F), Color(0xFFE5392B))), blendMode = BlendMode.SrcIn)
                },
            tint = if (lit) Color.White else gray,
        )
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
            ) { KpiIcon(Icons.Rounded.MilitaryTech, Color(0xFFF5B83D)) }
            val goal = streak.goal.questions.coerceAtLeast(1)
            val done = streak.todayQuestions
            Kpi(
                Modifier.weight(1f), "$done", if (streak.todayDone) "meta batida!" else "de $goal questões",
                if (streak.todayDone) "Dia salvo na sequência" else "Meta do dia",
                good = streak.todayDone,
            ) { GoalRing((done.toFloat() / goal).coerceIn(0f, 1f), streak.todayDone) }
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

@Composable
private fun GoalRing(fraction: Float, done: Boolean) {
    val track = MaterialTheme.colorScheme.surfaceContainerHighest
    val color = if (done) Color(0xFF22C59A) else MaterialTheme.colorScheme.primary
    Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize().padding(3.dp)) {
            val stroke = Stroke(4.dp.toPx(), cap = StrokeCap.Round)
            drawArc(track, 0f, 360f, false, style = stroke)
            drawArc(color, -90f, 360f * fraction, false, style = stroke)
        }
        Text("${(fraction * 100).toInt()}%", fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
