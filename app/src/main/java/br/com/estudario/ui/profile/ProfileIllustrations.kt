package br.com.estudario.ui.profile

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.ui.theme.EstudarioTheme
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.brand.turmaMotion
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

// Ilustrações vetoriais na grade 100 × 100. Os contornos e ornamentos foram compostos
// individualmente com curvas; as hachuras seguem as dobras, sem ruído ou textura artificial.
private val RankInk = Color(0xFF302544)
private val RankPaper = Color(0xFFFFFAEA)
private val RankPurple = Color(0xFF7551CB)

private data class RankColors(val metal: Color, val shade: Color, val light: Color, val enamel: Color)

private fun rankColors(tier: Int) = when (tier) {
    1 -> RankColors(Color(0xFFDA9B65), Color(0xFFA96341), Color(0xFFFFD3A0), Color(0xFF695094))
    2 -> RankColors(Color(0xFFC9D5DD), Color(0xFF889AAE), Color(0xFFF4F7FA), Color(0xFF66509B))
    3 -> RankColors(Color(0xFFF4C65E), Color(0xFFD09436), Color(0xFFFFE8A1), Color(0xFF604493))
    4 -> RankColors(Color(0xFF9AC7EF), Color(0xFF588BC5), Color(0xFFDEEEFF), Color(0xFF344F91))
    5 -> RankColors(Color(0xFFF1B061), Color(0xFFC77B40), Color(0xFFFFE1A0), Color(0xFF873A61))
    else -> RankColors(Color(0xFFF0CC6B), Color(0xFFB58A39), Color(0xFFFFEDB4), Color(0xFF266F68))
}

private fun DrawScope.inkShape(path: Path, fill: Color, width: Float = 1.6f) {
    drawPath(path, fill)
    drawPath(path, RankInk, style = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.pen(path: Path, color: Color = RankInk, width: Float = 0.85f) {
    drawPath(path, color, style = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

/** O nível: o livro de Folhas (LevelBookImage), que engorda a cada nível e troca de capa a cada título. */
@Composable
internal fun LevelSeal(level: Int, size: Dp = 116.dp) {
    LevelBookImage(level, size)
}

private fun bookmarkSway(detail: Int, phase: Float, tap: Float): Float =
    sin(phase * 2f) * (0.22f + detail * 0.055f) + sin(tap * 4 * PI).toFloat() * (1f - tap) * (1.8f + detail * 0.18f)

// Apenas curvas constantes entram no cache; folhas animadas continuam transitórias.
private val bookPaths = HashMap<String, Path>()
private fun bookPath(svg: String): Path = bookPaths.getOrPut(svg) { PathParser().parsePathString(svg).toPath() }
private fun DrawScope.bookShape(svg: String, color: Color, width: Float = 1.6f) = inkShape(bookPath(svg), color, width)
private fun DrawScope.bookPen(svg: String, color: Color = RankInk, width: Float = 0.85f, alpha: Float = 1f) =
    pen(bookPath(svg), color.copy(alpha = color.alpha * alpha), width)
private fun DrawScope.bookFill(svg: String, color: Color, alpha: Float = 1f) = drawPath(bookPath(svg), color, alpha = alpha)

private fun DrawScope.rankDrawing(level: Int, c: RankColors, phase: Float, tap: Float) {
    val detail = (level / 5).coerceIn(0, 10)
    val active = tap > 0f && tap < 1f
    val lift = sin(tap * PI).toFloat()
    val paper = Color(0xFFF4E6C8)
    val ink = Color(0xFFAC997D)
    bookFill("M10 88C27 82 73 82 91 87C95 92 71 96 50 95C28 96 6 92 10 88Z", RankInk, 0.12f)
    val growth = 0.92f + detail * 0.006f
    scale(growth, growth, Offset(50f, 54f)) {
        val cover = "M7 30Q27 22 49 35Q72 21 92 28L95 79Q73 77 54 86Q50 90 46 86Q28 79 5 83Z"
        bookPen(cover, RankPaper, 5.5f)
        bookShape(cover, c.shade, 1.65f)
        bookShape("M7 30Q28 22 49 35Q71 22 92 28L93 74Q72 72 54 81Q50 85 46 81Q28 75 6 78Z", c.metal, 1.05f)
        bookFill("M7 74Q28 71 48 81L50 86Q28 78 6 81Z", c.shade)
        bookPen("M11 79Q30 76 46 82M56 82Q74 76 90 77", c.light, 0.75f)
        bookShape("M10 29Q29 23 49 36Q70 22 90 27L91 74Q71 72 51 84Q29 74 9 78Z", paper, 1.05f)
        bookPen("M13 73Q29 70 46 79M13 76Q29 73 45 81M56 79Q74 71 88 72M57 82Q74 74 88 75", ink, 0.55f)
        bookShape("M11 26C24 20 37 24 49 33L50 77C36 68 23 67 10 72Z", Color(0xFFFFF4D9), 1.25f)
        bookShape("M49 33C62 22 77 19 90 24L91 70C77 65 63 69 50 77Z", RankPaper, 1.25f)
        bookFill("M43 29L49 33L50 77L44 73Q47 54 43 29Z", Color(0xFFE7D6AF), 0.65f)
        bookFill("M49 33L55 29Q53 53 55 74L50 77Z", Color(0xFFE7D6AF), 0.4f)
        bookPen("M14 29Q28 25 40 31M60 29Q76 23 86 26", Color.White, 0.8f, 0.8f)
        if (detail >= 1) {
            bookPen("M24 64C27 57 27 48 24 41", c.shade, 0.85f)
            bookShape("M25 50C18 49 16 44 18 40C24 39 28 44 25 50Z", c.light, 0.75f)
            bookPen("M19 42L25 49", c.shade, 0.55f)
            littleSpark(76f, 47f, 4.4f, c.light)
        }
        if (detail >= 2) {
            bookShape("M26 58C25 51 29 47 33 47C36 52 32 57 26 58Z", c.light, 0.75f)
            bookPen("M32 49L27 56", c.shade, 0.55f)
            drawCircle(c.shade, 8f, Offset(76f, 47f), style = Stroke(0.7f))
            drawCircle(c.enamel, 1.15f, Offset(76f, 47f))
            val curl = sin(phase * 2f) * (0.25f + detail * 0.04f) + lift * 2f
            val fold = Path().apply {
                moveTo(80f - curl, 23f); quadraticTo(85f, 22f, 90f, 24f)
                lineTo(90f, 34f + curl); quadraticTo(87f - curl, 27f, 80f - curl, 23f); close()
            }
            inkShape(fold, paper, 0.75f)
            bookPen("M86 26L88 29", Color.White, 0.65f)
        }
        if (detail >= 3) {
            bookPen("M16 34L16 31Q23 28 30 32M70 31Q79 26 85 29L85 32M16 65L16 69Q25 68 32 72M70 72Q79 67 85 68L85 64", c.shade, 0.65f)
            bookPen("M76 36L76 38M76 56L76 58M65 47L67 47M85 47L87 47", c.shade, 0.7f)
            bookPen("M15 76Q29 73 41 79M60 79Q76 72 88 73", c.light, 0.6f)
        }
        if (detail >= 4) {
            val corners = "M7 66L6 79Q13 77 21 79M92 62L94 76Q87 75 79 78"
            bookPen(corners, RankInk, 2.7f); bookPen(corners, c.light, 1.25f)
            bookShape("M91 43L96 42L97 50L91 52Z", Color(0xFFBCA4DF), 0.75f)
        }
        if (detail >= 5) {
            bookShape("M25 63C19 63 17 60 18 57C23 55 27 59 25 63Z", c.enamel, 0.65f)
            bookPen("M19 58L24 62", c.light, 0.55f)
            bookPen("M48 67L52 69M48 72L52 74M48 77L52 79", c.shade, 0.7f)
        }
        if (detail >= 6) {
            bookShape("M80 60C73 58 71 68 79 69C75 66 76 62 80 60Z", c.metal, 0.65f)
            drawCircle(c.enamel, 0.7f, Offset(70f, 61f))
            drawCircle(c.enamel, 0.7f, Offset(84f, 65f))
        }
        if (detail >= 7) {
            bookPen("M13 78Q29 76 42 82M59 82Q76 75 89 76", c.shade, 0.6f)
            bookPen("M14 80Q29 78 41 84M60 84Q76 77 89 78", c.light, 0.6f)
        }
        if (detail >= 8) {
            bookPen("M8 43L8 34Q15 31 22 33M91 40L91 32Q85 29 78 32", c.light, 0.9f)
            bookPen("M12 39Q14 34 18 35M89 37Q85 32 82 34", c.enamel, 0.6f)
            bookShape("M91 55L96 54L97 62L91 64Z", Color(0xFFD69778), 0.7f)
        }
        if (detail >= 9) rotate(sin(phase * 2f) * 2.5f + sin(tap * 5 * PI).toFloat() * (1f - tap) * 8f, Offset(83f, 75f)) {
            val cord = "M83 75C93 76 94 83 89 87Q87 89 88 91"
            bookPen(cord, RankInk, 1.9f); bookPen(cord, c.light, 0.95f)
            bookPen("M84 76C91 78 92 83 88 87", c.shade, 0.45f)
            drawCircle(c.metal, 1.65f, Offset(88f, 90f))
            drawCircle(RankInk, 1.65f, Offset(88f, 90f), style = Stroke(0.7f))
            val threads = "M86 92Q84 95 84 98M87 92Q86 95 86 98M88 92L88 99M89 92Q90 95 90 98M90 92Q92 95 92 97"
            bookPen(threads, RankInk, 1.35f); bookPen(threads, c.light, 0.65f)
            bookPen("M85.5 92L90.5 92", c.shade, 0.8f)
        }
        if (detail >= 10) {
            bookShape("M19 30L22 27L25 30L22 32Z", c.light, 0.5f)
            bookShape("M77 28L80 25L83 27L80 30Z", c.light, 0.5f)
        }
        if (level >= 100) bookPen("M15 81Q29 79 40 85M61 85Q76 78 89 79", c.light, 0.65f)
        if (level >= 150) {
            bookPen("M15 35L15 39M18 34L18 37M83 34L83 37M86 33L86 37", c.light, 0.65f)
            drawCircle(c.light, 10f, Offset(76f, 47f), style = Stroke(0.5f))
        }
        if (level >= 200) bookPen("M14 35L14 39M17 34L17 38M84 33L84 37M87 32L87 36", c.light, 0.7f)
        if (active) {
            val turn = (tap * (1 + detail / 4)) % 1f
            val span = 40f * kotlin.math.cos(turn * PI).toFloat()
            val up = sin(turn * PI).toFloat() * 11f
            val shadow = Path().apply {
                moveTo(50f, 34f); lineTo(50f + span * 0.9f, 27f)
                lineTo(50f + span * 0.9f, 72f); lineTo(50f, 78f); close()
            }
            drawPath(shadow, RankInk, alpha = sin(turn * PI).toFloat() * 0.13f)
            val sheet = Path().apply {
                moveTo(50f, 33f)
                cubicTo(50f + span * 0.38f, 26f - up, 50f + span, 20f - up, 50f + span, 25f)
                lineTo(50f + span, 70f)
                cubicTo(50f + span * 0.8f, 66f - up * 0.25f, 50f + span * 0.4f, 70f, 50f, 77f); close()
            }
            inkShape(sheet, if (turn < 0.5f) RankPaper else Color(0xFFF5E7CB), 0.85f)
            pen(Path().apply { moveTo(50f, 34f); quadraticTo(50f + span * 0.2f, 51f - up * 0.25f, 50f, 75f) }, Color(0xFFD6C19B), 0.55f)
        }
    }
    rotate(bookmarkSway(detail, phase, tap), Offset(50f, 20f)) {
        bookFill("M39 20Q51 17 65 20L65 66L52 61L39 68Z", RankInk, 0.15f)
        val ribbon = "M37 19Q49 15 63 18L63 64L50 58L37 66Z"
        bookPen(ribbon, RankPaper, 3.8f); bookShape(ribbon, c.enamel, 1.55f)
        bookFill("M37 19L40 18L40 61L37 66Z", c.shade, 0.35f)
        bookPen("M41 22Q50 19 59 21", if (level < 5) Color(0xFFC2B4A2) else c.light, 0.7f, 0.7f)
        if (detail >= 3) bookPen("M40 27L40 55M60 26L60 55", c.light, 0.55f, 0.45f)
        if (detail >= 4) {
            bookShape("M37 19Q49 15 63 18L63 23Q50 20 37 24Z", c.metal, 0.8f)
            bookPen("M42 20Q50 18 59 19", c.light, 0.65f)
        }
        if (detail >= 7) bookPen("M45 56L50 53L55 56", c.light, 0.6f)
        if (detail >= 10) littleSpark(50f, 20f, 1.8f, c.light)
        if (level >= 100) {
            drawCircle(c.enamel, 0.65f, Offset(42f, 21f)); drawCircle(c.enamel, 0.65f, Offset(59f, 20f))
        }
    }
}

private fun DrawScope.bookLights(detail: Int, c: RankColors, phase: Float, tap: Float) {
    if (detail >= 6) repeat((detail - 5).coerceAtMost(2)) { i ->
        val travel = (phase / (2 * PI).toFloat() + i * 0.46f) % 1f
        littleSpark(if (i == 0) 19f else 83f, 26f - travel * 9f, 1.1f + detail * 0.035f, c.light, sin(travel * PI).toFloat() * 0.55f)
    }
    if (tap > 0f && tap < 1f && detail >= 2) littleSpark(84f + tap * 3f, 27f - tap * 8f, 1.7f, c.light, sin(tap * PI).toFloat() * 0.8f)
}

private fun DrawScope.littleSpark(x: Float, y: Float, r: Float, color: Color, alpha: Float = 1f) {
    val sparkle = Path().apply {
        moveTo(x, y - r); quadraticTo(x + r * 0.2f, y - r * 0.2f, x + r, y)
        quadraticTo(x + r * 0.2f, y + r * 0.2f, x, y + r)
        quadraticTo(x - r * 0.2f, y + r * 0.2f, x - r, y)
        quadraticTo(x - r * 0.2f, y - r * 0.2f, x, y - r); close()
    }
    drawPath(sparkle, color, alpha = alpha)
    drawPath(sparkle, RankInk, alpha = alpha, style = Stroke(0.8f, join = StrokeJoin.Round))
}

/** Medalha da coleção: fitas dobradas, livro aberto e borda gravada. */
@Composable
internal fun ProfileEmblemsIcon() {
    Canvas(Modifier.size(40.dp).turmaMotion(Glyph.Medal, directTouch = true, onClickLabel = "Animar o emblema de conquista")) {
        scale(this.size.minDimension / 100f, pivot = Offset.Zero) {
            val ribbon = Path().apply {
                moveTo(31f, 55f); lineTo(49f, 62f); lineTo(40f, 93f); lineTo(30f, 84f); lineTo(18f, 87f); close()
                moveTo(53f, 62f); lineTo(71f, 55f); lineTo(84f, 87f); lineTo(71f, 84f); lineTo(62f, 94f); close()
            }
            drawPath(ribbon, Color.White, style = Stroke(10f, join = StrokeJoin.Round))
            inkShape(ribbon, RankPurple, 3.5f)
            pen(Path().apply { moveTo(32f, 69f); lineTo(28f, 80f); moveTo(68f, 69f); lineTo(73f, 80f) }, Color(0xFFCAB6FF), 2f)
            val medal = Path().apply {
                moveTo(50f, 8f); cubicTo(57f, 6f, 61f, 13f, 67f, 14f)
                cubicTo(75f, 14f, 76f, 22f, 80f, 27f); cubicTo(86f, 33f, 81f, 40f, 83f, 46f)
                cubicTo(83f, 53f, 75f, 56f, 72f, 62f); cubicTo(67f, 69f, 59f, 65f, 53f, 69f)
                cubicTo(46f, 73f, 40f, 67f, 34f, 67f); cubicTo(26f, 68f, 25f, 59f, 20f, 55f)
                cubicTo(14f, 51f, 18f, 43f, 16f, 37f); cubicTo(14f, 29f, 22f, 26f, 24f, 20f)
                cubicTo(27f, 13f, 35f, 15f, 40f, 10f); quadraticTo(45f, 6f, 50f, 8f); close()
            }
            drawPath(medal, Color.White, style = Stroke(10f, join = StrokeJoin.Round))
            inkShape(medal, Color(0xFFF4C65E), 3.6f)
            drawCircle(Color(0xFFFFE8A1), 23f, Offset(50f, 39f))
            drawCircle(RankInk, 23f, Offset(50f, 39f), style = Stroke(1.6f))
            val book = Path().apply {
                moveTo(50f, 31f); quadraticTo(39f, 24f, 31f, 29f); lineTo(31f, 48f)
                quadraticTo(41f, 44f, 50f, 51f); quadraticTo(59f, 44f, 69f, 48f)
                lineTo(69f, 29f); quadraticTo(60f, 25f, 50f, 31f); close()
            }
            inkShape(book, RankPaper, 2.8f)
            pen(Path().apply {
                moveTo(50f, 32f); lineTo(50f, 49f)
                moveTo(36f, 34f); quadraticTo(41f, 33f, 45f, 36f)
                moveTo(36f, 40f); quadraticTo(41f, 39f, 45f, 42f)
                moveTo(56f, 36f); quadraticTo(61f, 33f, 65f, 34f)
            }, RankInk, 1.6f)
            pen(Path().apply { moveTo(25f, 33f); lineTo(22f, 34f); moveTo(27f, 50f); lineTo(24f, 52f); moveTo(74f, 32f); lineTo(78f, 30f); moveTo(65f, 60f); lineTo(67f, 63f) }, Color(0xFFBA7B32), 1.7f)
            littleSpark(85f, 13f, 6f, Color(0xFFFFD869))
        }
    }
}

/** Alvo ilustrado com flecha emplumada e anel ligado ao progresso real do dia. */
@Composable
internal fun ProfileDailyGoalIcon(fraction: Float, done: Boolean) {
    val progress by animateFloatAsState(fraction.coerceIn(0f, 1f), tween(700, easing = FastOutSlowInEasing), label = "alvo-do-dia")
    val track = MaterialTheme.colorScheme.outlineVariant
    val accent = if (done) Color(0xFF22B58A) else Color(0xFF8060D7)
    Canvas(Modifier.size(40.dp).turmaMotion(Glyph.Target, directTouch = true, onClickLabel = "Animar a meta do dia")) {
        scale(this.size.minDimension / 100f, pivot = Offset.Zero) {
            drawArc(track, 0f, 360f, false, Offset(3f, 12f), Size(80f, 80f), style = Stroke(4f, cap = StrokeCap.Round))
            drawArc(accent, -90f, progress * 360f, false, Offset(3f, 12f), Size(80f, 80f), style = Stroke(4f, cap = StrokeCap.Round))
            val target = Path().apply {
                moveTo(44f, 20f); cubicTo(62f, 18f, 76f, 33f, 75f, 52f)
                cubicTo(76f, 70f, 63f, 84f, 44f, 84f); cubicTo(26f, 85f, 12f, 70f, 13f, 53f)
                cubicTo(12f, 35f, 26f, 20f, 44f, 20f); close()
            }
            drawPath(target, Color.White, style = Stroke(7f))
            inkShape(target, RankPaper, 3.2f)
            drawCircle(if (done) Color(0xFF93E1BC) else Color(0xFFEF987D), 24f, Offset(44f, 52f))
            drawCircle(RankInk, 24f, Offset(44f, 52f), style = Stroke(1.8f))
            drawCircle(RankPaper, 16f, Offset(44f, 52f))
            drawCircle(RankInk, 16f, Offset(44f, 52f), style = Stroke(1.6f))
            drawCircle(if (done) Color(0xFF2CAE83) else Color(0xFFE86B52), 8.5f, Offset(44f, 52f))
            drawCircle(RankInk, 8.5f, Offset(44f, 52f), style = Stroke(1.6f))
            pen(Path().apply { moveTo(23f, 38f); quadraticTo(26f, 31f, 32f, 29f) }, Color.White, 2.1f)
            // A haste tem contorno de tinta e preenchimento de madeira.
            pen(Path().apply { moveTo(45f, 51f); lineTo(87f, 9f) }, RankInk, 6f)
            pen(Path().apply { moveTo(46f, 50f); lineTo(87f, 9f) }, Color(0xFFF7CD78), 2.6f)
            val head = Path().apply { moveTo(42f, 54f); lineTo(47f, 38f); lineTo(57f, 48f); close() }
            inkShape(head, Color(0xFFF7CD78), 2f)
            val feather = Path().apply { moveTo(73f, 23f); lineTo(72f, 11f); lineTo(88f, 2f); lineTo(87f, 15f); lineTo(99f, 14f); lineTo(89f, 29f); lineTo(78f, 28f); close() }
            inkShape(feather, RankPurple, 2.5f)
            pen(Path().apply { moveTo(77f, 22f); lineTo(87f, 12f); moveTo(80f, 18f); lineTo(79f, 13f); moveTo(83f, 23f); lineTo(89f, 23f) }, Color(0xFFD7C5FF), 1.5f)
            if (done) {
                drawCircle(Color.White, 15f, Offset(78f, 77f))
                drawCircle(Color(0xFF22B58A), 12f, Offset(78f, 77f))
                drawCircle(RankInk, 12f, Offset(78f, 77f), style = Stroke(2f))
                pen(Path().apply { moveTo(72f, 77f); lineTo(77f, 82f); lineTo(85f, 72f) }, Color.White, 3f)
            }
        }
    }
}

/** Prancha de revisão no aparelho, acessível apenas pela entrada de debug da Activity. */
@Composable
internal fun ProfileArtShowcase(dark: Boolean) {
    EstudarioTheme(darkTheme = dark) {
        Column(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Cada conquista deixa sua marca", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
            Text("Cada nível, uma folha a mais. Cada título, uma capa nova.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            for (row in listOf(listOf(1, 10), listOf(20, 30), listOf(40, 50))) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    for (level in row) Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LevelSeal(level, 136.dp)
                        Text("Nível $level", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                ProfileEmblemsIcon()
                ProfileDailyGoalIcon(0f, false)
                ProfileDailyGoalIcon(0.4f, false)
                ProfileDailyGoalIcon(1f, true)
            }
            Text("Emblemas · Meta do dia em 0%, 40% e 100%", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
