package br.com.estudario.ui.assistant

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/** Como o Folha está: muda olhos, boca, sobrancelhas e o que acontece em volta dele. */
enum class FolhaMood {
    /** Esperando: flutua, pisca e olha em volta. */
    IDLE,
    /** Gerando: lê as próprias páginas, as linhas se escrevem e estrelinhas giram em volta. */
    THINKING,
    /** Conversando com a pessoa: a boca abre e fecha e ele balança de leve. */
    TALKING,
    /** Pronto: sorriso aberto, olhos de alegria e pulinhos. */
    HAPPY,
    /** Deu errado: sobrancelhas tristes, fita caída. */
    SAD,
}

private val Ink = Color(0xFF26215C)
private val PageLine = Color(0xFFD8D4E6)
private val Cap = Color(0xFF231C6B)
private val CapTop = Color(0xFF2F2789)
private val Gold = Color(0xFFF5B83D)
private val Mint = Color(0xFF7EE0B8)
private val MintDark = Color(0xFF3FBF8F)
private val Cheek = Color(0xFFF4A6C0)
private val Spark = Color(0xFFFFE07A)

/**
 * O Folha, a cara do Assistente Estudário: o livro da marca, vivo, de capelo de formatura.
 *
 * Ele nunca fica parado de verdade: respira, pisca em intervalos irregulares (às vezes duas
 * vezes seguidas), olha em volta, e a borla do capelo e a fita balançam com o movimento. Um toque
 * faz ele pular e piscar um olho, e chama [onClick] (a ação do lugar onde ele está, como gerar).
 */
@Composable
fun Folha(
    size: Dp,
    modifier: Modifier = Modifier,
    mood: FolhaMood = FolhaMood.IDLE,
    onClick: (() -> Unit)? = null,
    /** 0 a 1: uma folha acabou de chegar até ele (a cena de processamento avisa); ele "engole" e brilha. */
    gulp: Float = 0f,
) {
    val time by produceState(0f) {
        val start = withFrameNanos { it }
        while (true) withFrameNanos { value = (it - start) / 1_000_000_000f }
    }
    var blink by remember { mutableFloatStateOf(0f) }
    var look by remember { mutableStateOf(Offset.Zero) }
    var wink by remember { mutableStateOf(false) }
    val bounce = remember { Animatable(0f) }
    val lookX = remember { Animatable(0f) }
    val lookY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Piscadas: intervalo irregular, às vezes dupla, como gente.
    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(1800, 4600))
            repeat(if (Random.nextInt(5) == 0) 2 else 1) {
                val anim = Animatable(0f)
                anim.animateTo(1f, tween(70)) { blink = value }
                anim.animateTo(0f, tween(110)) { blink = value }
                delay(90)
            }
        }
    }
    // Olhar: parado, ele olha em volta; pensando, ele lê da esquerda para a direita.
    LaunchedEffect(mood) {
        while (true) {
            when (mood) {
                FolhaMood.THINKING -> {
                    launch { lookY.animateTo(0.6f, tween(300)) }
                    lookX.animateTo(-1f, tween(260))
                    lookX.animateTo(1f, tween(900))
                }
                FolhaMood.IDLE -> {
                    delay(Random.nextLong(900, 2600))
                    launch { lookY.animateTo(Random.nextFloat() * 1.2f - 0.6f, tween(380)) }
                    lookX.animateTo(Random.nextFloat() * 2f - 1f, tween(380))
                }
                else -> {
                    launch { lookY.animateTo(0f, tween(300)) }
                    lookX.animateTo(0f, tween(300))
                    delay(800)
                }
            }
            look = Offset(lookX.value, lookY.value)
        }
    }

    val clickable = if (onClick != null) Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
    ) {
        scope.launch {
            wink = true
            bounce.snapTo(0f)
            bounce.animateTo(1f, tween(110))
            bounce.animateTo(0f, spring(dampingRatio = 0.32f, stiffness = Spring.StiffnessMedium))
            delay(250)
            wink = false
        }
        onClick()
    } else Modifier

    Canvas(
        modifier
            .size(size)
            .semantics { contentDescription = "Folha, o Assistente Estudário" }
            .then(clickable),
    ) {
        drawFolha(
            t = time,
            mood = mood,
            blink = if (mood == FolhaMood.HAPPY) 0f else blink,
            look = Offset(lookX.value, lookY.value),
            wink = wink,
            bounce = maxOf(bounce.value, gulp * 0.45f),
            gulp = gulp,
        )
    }
}

/** Desenho do Folha numa grade de 100 x 100. */
private fun DrawScope.drawFolha(t: Float, mood: FolhaMood, blink: Float, look: Offset, wink: Boolean, bounce: Float, gulp: Float = 0f) {
    val k = size.minDimension / 100f
    val hop = when (mood) {
        FolhaMood.HAPPY -> -abs(sin(t * 5.2f)) * 5f
        else -> sin(t * 2.1f) * 1.4f
    }
    val tilt = when (mood) {
        FolhaMood.TALKING -> sin(t * 3.4f) * 3f
        FolhaMood.THINKING -> sin(t * 1.3f) * 2f
        FolhaMood.SAD -> -4f
        else -> sin(t * 0.9f) * 1.2f
    }
    // Ao tocar: amassa e estica, como borracha.
    val squashX = 1f + 0.10f * bounce
    val squashY = 1f - 0.12f * bounce

    scale(k, k, pivot = Offset.Zero) {
        // Sombra no chão: menor quando ele sobe.
        val lift = (-hop).coerceAtLeast(0f)
        scale(1f, 0.18f, pivot = Offset(50f, 95f)) {
            drawCircle(
                Brush.radialGradient(listOf(Ink.copy(alpha = 0.28f), Color.Transparent), center = Offset(50f, 95f), radius = 36f - lift * 1.6f),
                radius = 36f - lift * 1.6f, center = Offset(50f, 95f),
            )
            // Núcleo mais escuro e justo: o peso do livro no chão; some quando ele sobe.
            drawCircle(
                Brush.radialGradient(listOf(Ink.copy(alpha = 0.30f * (1f - lift / 6f).coerceIn(0.3f, 1f)), Color.Transparent), center = Offset(50f, 95f), radius = 26f),
                radius = 26f, center = Offset(50f, 95f),
            )
        }
        if (mood == FolhaMood.THINKING) drawSparkles(t, behind = true)

        translate(0f, hop) {
            scale(squashX, squashY, pivot = Offset(50f, 88f)) {
                rotate(tilt, pivot = Offset(50f, 80f)) {
                    drawArms(t, mood)
                    drawBook(t, mood)
                    drawPageLines(t, mood)
                    drawFace(t, mood, blink, look, wink, gulp)
                    drawCap(t, tilt)
                }
            }
        }
        if (mood == FolhaMood.THINKING) drawSparkles(t, behind = false)
        if (mood == FolhaMood.HAPPY) drawCelebration(t)
    }
}

/**
 * Uma página aberta. As duas se encontram na lombada (x = 50), sem fresta: a página sobe
 * saindo da costura, faz a barriga e cai um pouco na borda, como papel de verdade.
 * [side] -1 é a esquerda, 1 a direita; [drop] e [spread] descem e abrem a borda (para o bloco).
 */
private fun page(side: Float, drop: Float = 0f, spread: Float = 0f) = Path().apply {
    fun x(v: Float) = 50f + side * (v + spread * (v / 38f))
    moveTo(x(0f), 44f + drop * 0.2f)
    cubicTo(x(10f), 37.6f, x(26f), 36.4f, x(38f), 40.4f + drop)
    lineTo(x(38f), 84.4f + drop)
    cubicTo(x(26f), 80.6f + drop, x(10f), 81.4f + drop, x(0f), 87f + drop)
    close()
}

/** A beirada do bloco de páginas: a espessura entre a página de cima e a capa. */
private fun pageBlock(side: Float, depth: Float) = Path().apply {
    val outer = 50f + side * 38f
    val outerLow = 50f + side * (38f + depth * 0.45f)
    moveTo(outer, 40.4f)
    lineTo(outer, 84.4f)
    cubicTo(50f + side * 26f, 80.6f, 50f + side * 10f, 81.4f, 50f, 87f)
    lineTo(50f, 87f + depth)
    cubicTo(50f + side * 10f, 81.4f + depth, 50f + side * 26.5f, 80.6f + depth, outerLow, 84.4f + depth)
    lineTo(outerLow, 41.6f + depth * 0.3f)
    close()
}

private val Paper = Color(0xFFFFFDF8)
private val PaperMid = Color(0xFFF7F4EE)
private val PaperEdge = Color(0xFFE9E4DA)
private val PaperEdgeDark = Color(0xFFCFC8BC)
private val Gutter = Color(0xFF3A3170)

private fun DrawScope.drawBook(t: Float, mood: FolhaMood) {
    // Capa dura com espessura: a borda escura embaixo é o papelão visto de lado.
    val cover = Path().apply {
        moveTo(8.5f, 43.5f); cubicTo(22f, 40.5f, 38f, 41.5f, 50f, 47.5f); cubicTo(62f, 41.5f, 78f, 40.5f, 91.5f, 43.5f)
        lineTo(91.5f, 89f); cubicTo(78f, 86.4f, 62f, 87.4f, 50f, 93f); cubicTo(38f, 87.4f, 22f, 86.4f, 8.5f, 89f); close()
    }
    translate(0f, 1.7f) { drawPath(cover, Color(0xFF1A1370)) }
    drawPath(cover, Brush.verticalGradient(listOf(Color(0xFF5247E0), Color(0xFF3A2FC4), Color(0xFF2A209E)), startY = 41f, endY = 93f))
    // Brilho de tecido da capa, vindo da luz de cima à esquerda.
    drawPath(cover, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.14f), Color.Transparent), start = Offset(8f, 44f), end = Offset(40f, 70f)))

    // A fita sai de baixo do bloco de folhas e dobra sobre a borda da capa.
    drawRibbon(t, mood)

    // Bloco de folhas: a beirada de papel com as folhas riscadas finas, não degraus.
    for (side in floatArrayOf(-1f, 1f)) {
        val block = pageBlock(side, 3.6f)
        drawPath(block, Brush.verticalGradient(listOf(PaperEdge, PaperEdgeDark), startY = 80f, endY = 91f))
        for (i in 1..4) {
            val d = i * 0.72f
            val p = Path().apply {
                moveTo(50f + side * (38f + d * 0.45f), 84.4f + d)
                cubicTo(50f + side * 26.2f, 80.6f + d, 50f + side * 10f, 81.4f + d, 50f, 87f + d)
            }
            drawPath(p, Color(0xFF8C8270).copy(alpha = 0.22f), style = Stroke(0.28f))
        }
        // Beirada lateral: mesmas linhas finas, na vertical.
        for (i in 1..3) {
            val x = 50f + side * (38f + i * 0.42f)
            drawLine(Color(0xFF8C8270).copy(alpha = 0.18f), Offset(x, 41f + i * 0.3f), Offset(x, 84.4f + i * 0.9f), 0.25f)
        }
    }

    // Páginas de cima: papel levemente quente, mais claro onde a luz bate.
    val left = page(-1f)
    val right = page(1f)
    drawPath(left, Brush.horizontalGradient(listOf(PaperMid, Paper, Paper, PaperMid), startX = 12f, endX = 50f))
    drawPath(right, Brush.horizontalGradient(listOf(PaperMid, Paper, Paper, Color(0xFFF1EDE6)), startX = 50f, endX = 88f))
    // A curva da página entrando na costura: sombra macia que só escurece colada ao meio.
    val gutterStops = arrayOf(
        0f to Color.Transparent,
        0.55f to Gutter.copy(alpha = 0.035f),
        0.85f to Gutter.copy(alpha = 0.10f),
        1f to Gutter.copy(alpha = 0.20f),
    )
    drawPath(left, Brush.horizontalGradient(*gutterStops, startX = 39f, endX = 50f))
    drawPath(right, Brush.horizontalGradient(*gutterStops, startX = 61f, endX = 50f))
    // Leve queda de luz nas bordas externas, onde a página desce.
    drawPath(left, Brush.horizontalGradient(listOf(Gutter.copy(alpha = 0.06f), Color.Transparent), startX = 12f, endX = 17f))
    drawPath(right, Brush.horizontalGradient(listOf(Color.Transparent, Gutter.copy(alpha = 0.08f)), startX = 83f, endX = 88f))
    // A costura em si: um fio, quase nada.
    drawLine(Gutter.copy(alpha = 0.16f), Offset(50f, 44.3f), Offset(50f, 86.8f), 0.35f)
    // Fio de luz na borda de cima de cada página, onde o papel faz a barriga.
    for (side in floatArrayOf(-1f, 1f)) {
        val rim = Path().apply {
            moveTo(50f + side * 3f, 41.9f)
            cubicTo(50f + side * 12f, 37.9f, 50f + side * 26f, 36.9f, 50f + side * 36.5f, 40.2f)
        }
        drawPath(rim, Color.White.copy(alpha = 0.9f), style = Stroke(0.5f, cap = StrokeCap.Round))
    }
}

/** Linhas de texto discretas nas páginas; pensando, elas vão sendo escritas. */
private fun DrawScope.drawPageLines(t: Float, mood: FolhaMood) {
    val writing = mood == FolhaMood.THINKING
    val cycle = (t % 2.4f) / 2.4f
    fun line(x0: Float, y0: Float, len: Float, slope: Float, index: Int, mirror: Boolean) {
        val p = if (!writing) 1f else ((cycle * 6f - index).coerceIn(0f, 1f))
        if (p <= 0f) return
        val x1 = if (mirror) x0 - len * p else x0 + len * p
        drawLine(PageLine, Offset(x0, y0), Offset(x1, y0 + slope * len * p), 1.6f, StrokeCap.Round)
    }
    line(17f, 45f, 20f, 0.1f, 0, false)
    line(17f, 49f, 26f, 0.12f, 1, false)
    line(83f, 45f, 20f, 0.1f, 2, true)
    line(83f, 49f, 26f, 0.12f, 3, true)
    line(17f, 79.5f, 18f, -0.03f, 4, false)
    line(83f, 79.5f, 18f, -0.03f, 5, true)
}

private fun DrawScope.drawFace(t: Float, mood: FolhaMood, blink: Float, look: Offset, wink: Boolean, gulp: Float = 0f) {
    val eyeL = Offset(31f + look.x * 1.6f, 61f + look.y * 1.4f)
    val eyeR = Offset(69f + look.x * 1.6f, 61f + look.y * 1.4f)

    // Bochechas.
    drawOval(Cheek.copy(alpha = if (mood == FolhaMood.HAPPY) 0.7f else 0.45f), topLeft = Offset(19f, 67f), size = Size(9f, 5f))
    drawOval(Cheek.copy(alpha = if (mood == FolhaMood.HAPPY) 0.7f else 0.45f), topLeft = Offset(72f, 67f), size = Size(9f, 5f))

    fun eye(c: Offset, closed: Float, isWink: Boolean) {
        if (mood == FolhaMood.HAPPY || isWink) {
            // Olho de alegria: um arco "^".
            val p = Path().apply { moveTo(c.x - 4.5f, c.y + 1.5f); quadraticTo(c.x, c.y - 5f, c.x + 4.5f, c.y + 1.5f) }
            drawPath(p, Ink, style = Stroke(2.6f, cap = StrokeCap.Round))
            return
        }
        val ry = 5.8f * (1f - closed).coerceAtLeast(0.08f)
        drawOval(Ink, topLeft = Offset(c.x - 4.4f, c.y - ry), size = Size(8.8f, ry * 2))
        if (closed < 0.6f) {
            drawCircle(Color.White, 1.7f, Offset(c.x - 1.4f + look.x * 0.6f, c.y - 2.2f))
            drawCircle(Color.White.copy(alpha = 0.7f), 0.8f, Offset(c.x + 1.6f, c.y + 1.8f))
        }
    }
    eye(eyeL, blink, false)
    eye(eyeR, blink, wink)

    // Sobrancelhas: pensando, uma sobe; triste, as duas caem para fora.
    when (mood) {
        FolhaMood.THINKING -> {
            drawLine(Ink, Offset(26f, 51.5f), Offset(35f, 50f), 1.8f, StrokeCap.Round)
            drawLine(Ink, Offset(65f, 49f), Offset(74f, 50.5f + sin(t * 2f)), 1.8f, StrokeCap.Round)
        }
        FolhaMood.SAD -> {
            drawLine(Ink, Offset(26f, 51f), Offset(35f, 53f), 1.8f, StrokeCap.Round)
            drawLine(Ink, Offset(65f, 53f), Offset(74f, 51f), 1.8f, StrokeCap.Round)
        }
        else -> Unit
    }

    // Boca.
    val m = Offset(50f, 73f)
    if (gulp > 0.08f) {
        // "Nham": boca redonda recebendo a folha, com brilho saindo das páginas.
        val r = 1.5f + 3.2f * gulp
        drawOval(Ink, topLeft = Offset(m.x - r, m.y - r * 1.1f), size = Size(r * 2, r * 2.2f))
        for (i in 0 until 4) {
            val a = i * (PI.toFloat() / 2f) + 0.5f
            star(Offset(50f + cos(a) * (22f + 14f * (1f - gulp)), 58f + sin(a) * (16f + 10f * (1f - gulp))), 1.8f * gulp, Spark.copy(alpha = gulp))
        }
        return
    }
    when (mood) {
        FolhaMood.TALKING -> {
            // Fala com ritmo de sílabas, não um abre e fecha mecânico.
            val open = (0.5f + 0.5f * sin(t * 13f)) * (0.55f + 0.45f * abs(sin(t * 3.1f)))
            val h = 1.2f + 5f * open
            drawOval(Ink, topLeft = Offset(m.x - 4.2f, m.y - h / 2), size = Size(8.4f, h))
            if (h > 3f) drawOval(Cheek, topLeft = Offset(m.x - 2.4f, m.y + h / 2 - 2.2f), size = Size(4.8f, 2f))
        }
        FolhaMood.HAPPY -> {
            val p = Path().apply { moveTo(m.x - 6f, m.y - 1.5f); quadraticTo(m.x, m.y + 9f, m.x + 6f, m.y - 1.5f); close() }
            drawPath(p, Ink)
            drawOval(Cheek, topLeft = Offset(m.x - 3f, m.y + 2f), size = Size(6f, 2.6f))
        }
        FolhaMood.SAD -> {
            val p = Path().apply { moveTo(m.x - 4.5f, m.y + 2.5f); quadraticTo(m.x, m.y - 2.5f, m.x + 4.5f, m.y + 2.5f) }
            drawPath(p, Ink, style = Stroke(2.2f, cap = StrokeCap.Round))
        }
        FolhaMood.THINKING -> {
            // Boquinha de lado, concentrado.
            val p = Path().apply { moveTo(m.x - 3f, m.y + 0.5f); quadraticTo(m.x + 1f, m.y + 2.5f, m.x + 4f, m.y - 0.5f) }
            drawPath(p, Ink, style = Stroke(2.2f, cap = StrokeCap.Round))
        }
        FolhaMood.IDLE -> {
            val p = Path().apply { moveTo(m.x - 5f, m.y - 1f); quadraticTo(m.x, m.y + 5f, m.x + 5f, m.y - 1f) }
            drawPath(p, Ink, style = Stroke(2.3f, cap = StrokeCap.Round))
        }
    }
}

/** O capelo, com a borla dourada balançando ao contrário do movimento do corpo. */
private fun DrawScope.drawCap(t: Float, tilt: Float) {
    // Base que veste a lombada.
    val base = Path().apply {
        moveTo(38f, 32f); lineTo(62f, 32f); lineTo(60.5f, 42.5f)
        quadraticTo(50f, 46.5f, 39.5f, 42.5f); close()
    }
    drawPath(base, Brush.verticalGradient(listOf(Cap, Color(0xFF15104A)), startY = 32f, endY = 46f))
    // Tábua em perspectiva.
    val board = Path().apply { moveTo(50f, 21f); lineTo(77f, 29.5f); lineTo(50f, 38f); lineTo(23f, 29.5f); close() }
    drawPath(Path().apply { moveTo(23f, 29.5f); lineTo(50f, 38f); lineTo(77f, 29.5f); lineTo(77f, 31.3f); lineTo(50f, 39.8f); lineTo(23f, 31.3f); close() }, Color(0xFF120D3F))
    drawPath(board, Brush.linearGradient(listOf(CapTop, Cap), start = Offset(30f, 22f), end = Offset(70f, 38f)))
    // Brilho na tábua.
    drawLine(Color.White.copy(alpha = 0.18f), Offset(33f, 27.5f), Offset(50f, 22.5f), 1.2f, StrokeCap.Round)
    // Borla: cordão até a ponta e o pingente balançando.
    val swing = sin(t * 2.6f) * 4f - tilt * 0.8f
    val button = Offset(50f, 29.5f)
    val corner = Offset(74f, 30.5f)
    drawLine(Gold, button, corner, 1.3f, StrokeCap.Round)
    val end = Offset(corner.x + swing * 0.5f, corner.y + 12f)
    val cord = Path().apply { moveTo(corner.x, corner.y); quadraticTo(corner.x + swing * 0.2f, corner.y + 6f, end.x, end.y) }
    drawPath(cord, Gold, style = Stroke(1.3f, cap = StrokeCap.Round))
    val tassel = Path().apply {
        moveTo(end.x - 1.4f, end.y); lineTo(end.x + 1.4f, end.y)
        lineTo(end.x + 2.4f + swing * 0.15f, end.y + 6.5f); lineTo(end.x - 2.4f + swing * 0.15f, end.y + 6.5f); close()
    }
    drawPath(tassel, Brush.verticalGradient(listOf(Gold, Color(0xFFD89422)), startY = end.y, endY = end.y + 6.5f))
    drawCircle(Gold, 1.8f, button)
    drawCircle(Color(0xFFFFE3A0), 0.7f, Offset(button.x - 0.5f, button.y - 0.5f))
}

/**
 * A fita verde de marcar página, de cetim. Ela nasce presa na costura (por baixo do bloco de
 * folhas), deita sobre a capa, dobra na quina e cai pendurada com peso; só a ponta balança.
 * Triste, ela pende mais curta e parada.
 */
private fun DrawScope.drawRibbon(t: Float, mood: FolhaMood) {
    val sad = mood == FolhaMood.SAD
    val sway = if (sad) 0f else sin(t * 1.9f + 0.6f) * 1.3f
    val w = 3.4f
    val x0 = 51.4f
    // Trecho deitado na capa, do bloco até a quina (y ~94.7, contando a espessura).
    val lieTop = 86f
    val edge = 94.6f
    val hang = if (sad) 3.2f else 5.4f
    val tipY = edge + hang
    val tipX = x0 + 0.5f + sway
    val ribbon = Path().apply {
        moveTo(x0, lieTop); lineTo(x0 + w, lieTop)
        lineTo(x0 + w + 0.3f, edge)
        quadraticTo(x0 + w + 0.3f + sway * 0.3f, edge + hang * 0.5f, tipX + w, tipY)
        lineTo(tipX + w / 2f, tipY - 1.6f)
        lineTo(tipX, tipY)
        quadraticTo(x0 + 0.3f + sway * 0.3f, edge + hang * 0.5f, x0 + 0.3f, edge)
        close()
    }
    // Sombra da fita na capa (só no trecho deitado) e no ar logo atrás dela.
    translate(0.7f, 0.5f) { drawPath(ribbon, Color(0xFF0E0A45).copy(alpha = 0.35f)) }
    drawPath(ribbon, Brush.horizontalGradient(listOf(MintDark, Mint, Color(0xFFC6F7E2), Mint), startX = x0, endX = x0 + w + 0.6f + sway))
    // Onde ela entra por baixo das folhas: escurece, porque ali não chega luz.
    drawRect(
        Brush.verticalGradient(listOf(Color(0xFF0E0A45).copy(alpha = 0.45f), Color.Transparent), startY = 88f, endY = 91.5f),
        topLeft = Offset(x0, 88f), size = Size(w + 0.3f, 3.5f),
    )
    // A dobra na quina da capa: uma faixa de luz e logo abaixo a sombra da curva.
    drawLine(Color.White.copy(alpha = 0.55f), Offset(x0 + 0.5f, edge - 0.4f), Offset(x0 + w, edge - 0.4f), 0.5f)
    drawRect(
        Brush.verticalGradient(listOf(Color(0xFF1F7A5A).copy(alpha = 0.55f), Color.Transparent), startY = edge, endY = edge + 1.8f),
        topLeft = Offset(x0 + 0.3f, edge), size = Size(w, 1.8f),
    )
}

/** Estrelinhas girando em volta enquanto ele gera; as de trás passam por trás do livro. */
private fun DrawScope.drawSparkles(t: Float, behind: Boolean) {
    for (i in 0 until 4) {
        val a = t * 1.4f + i * (PI.toFloat() / 2f)
        val depth = sin(a)
        if ((depth < 0f) != behind) continue
        val c = Offset(50f + cos(a) * 46f, 60f + depth * 9f - 4f)
        val r = (2.4f + 1.6f * (0.5f + 0.5f * sin(t * 6f + i))) * (if (behind) 0.7f else 1f)
        star(c, r, if (i % 2 == 0) Spark else Mint)
    }
}

/** Pronto: confetes de estrela subindo dos lados. */
private fun DrawScope.drawCelebration(t: Float) {
    for (i in 0 until 5) {
        val p = ((t * 0.7f + i * 0.21f) % 1f)
        val side = if (i % 2 == 0) -1f else 1f
        val c = Offset(50f + side * (30f + i * 3f), 70f - p * 48f)
        star(c, 2.6f * (1f - p), listOf(Spark, Mint, Cheek)[i % 3].copy(alpha = 1f - p))
    }
}

private fun DrawScope.star(c: Offset, r: Float, color: Color) {
    val s = Path().apply {
        moveTo(c.x, c.y - r * 2f)
        quadraticTo(c.x, c.y, c.x + r * 2f, c.y)
        quadraticTo(c.x, c.y, c.x, c.y + r * 2f)
        quadraticTo(c.x, c.y, c.x - r * 2f, c.y)
        quadraticTo(c.x, c.y, c.x, c.y - r * 2f)
        close()
    }
    drawPath(s, color)
}

/**
 * O Folha que fala ao aparecer (pelo tempo de ler [text] em voz alta, mais ou menos) e depois
 * fica ouvindo. Para os cabeçalhos em balão, onde a pergunta é a "fala" dele.
 */
@Composable
fun FolhaTalking(size: Dp, text: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    var talking by remember(text) { mutableStateOf(true) }
    LaunchedEffect(text) {
        delay((text.length * 45L).coerceIn(900L, 3200L))
        talking = false
    }
    Folha(size, modifier, if (talking) FolhaMood.TALKING else FolhaMood.IDLE, onClick)
}

/**
 * Bracinhos saindo das bordas das páginas: acenam quando ele fala, sobem quando comemora,
 * caem quando está triste e, pensando, um deles fica no "queixo".
 */
private fun DrawScope.drawArms(t: Float, mood: FolhaMood) {
    fun arm(shoulder: Offset, hand: Offset) {
        val elbow = Offset((shoulder.x + hand.x) / 2f + (if (hand.x < 50f) -2f else 2f), (shoulder.y + hand.y) / 2f + 2f)
        val p = Path().apply { moveTo(shoulder.x, shoulder.y); quadraticTo(elbow.x, elbow.y, hand.x, hand.y) }
        drawPath(p, Color(0xFF3326CE), style = Stroke(3.2f, cap = StrokeCap.Round))
        drawCircle(Color.White, 3.3f, hand)
        drawCircle(Color(0xFF3326CE), 3.3f, hand, style = Stroke(1.4f))
    }
    val ls = Offset(12f, 66f)
    val rs = Offset(88f, 66f)
    when (mood) {
        FolhaMood.TALKING -> {
            val wave = sin(t * 7f) * 5f
            arm(ls, Offset(3f, 76f))
            arm(rs, Offset(97f + wave * 0.3f, 52f + wave))
        }
        FolhaMood.HAPPY -> {
            val pump = abs(sin(t * 5.2f)) * 4f
            arm(ls, Offset(2f, 48f - pump))
            arm(rs, Offset(98f, 48f - pump))
        }
        FolhaMood.SAD -> {
            arm(ls, Offset(6f, 84f))
            arm(rs, Offset(94f, 84f))
        }
        FolhaMood.THINKING -> {
            arm(ls, Offset(4f, 76f + sin(t * 1.5f)))
            arm(rs, Offset(93f, 70f + sin(t * 2.2f) * 1.5f))
        }
        FolhaMood.IDLE -> {
            arm(ls, Offset(4f, 78f + sin(t * 2.1f) * 1.2f))
            arm(rs, Offset(96f, 78f - sin(t * 2.1f) * 1.2f))
        }
    }
}

/**
 * O Folha como imagem, para o ícone grande das notificações (fora do Compose). O ícone pequeno da
 * barra continua monocromático, como o Android exige; este aparece colorido ao lado do texto.
 */
fun folhaBitmap(sizePx: Int, mood: FolhaMood = FolhaMood.HAPPY): android.graphics.Bitmap {
    val image = androidx.compose.ui.graphics.ImageBitmap(sizePx, sizePx)
    val canvas = androidx.compose.ui.graphics.Canvas(image)
    androidx.compose.ui.graphics.drawscope.CanvasDrawScope().draw(
        androidx.compose.ui.unit.Density(1f),
        androidx.compose.ui.unit.LayoutDirection.Ltr,
        canvas,
        Size(sizePx.toFloat(), sizePx.toFloat()),
    ) {
        // Um quadro "bonito": olhos abertos, borla no meio do balanço.
        drawFolha(t = 0.35f, mood = mood, blink = 0f, look = Offset(0.3f, -0.2f), wink = false, bounce = 0f)
    }
    return image.asAndroidBitmap()
}
