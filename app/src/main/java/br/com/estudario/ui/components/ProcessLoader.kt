package br.com.estudario.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.theme.estudarioColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

/**
 * A espera "trabalhando" do Estudário: o livro abre no centro e o material chega até ele. Folhas
 * de PDF, listas, questões e marcações partem da borda, fazem uma curva e são absorvidas pelo
 * livro, com um brilho a cada chegada. É o mesmo livro da marca ([drawEstudarioBook]), só que
 * grande e acompanhado do que ele está recebendo.
 *
 * Usado onde a espera é longa e tem etapas (a IA lendo um edital) e, em versão compacta, em todo
 * diálogo de carregamento do app. Com animações desligadas no sistema, a cena fica parada e o texto
 * das etapas continua avançando.
 */
@Composable
fun EstudarioProcessScene(size: Dp, modifier: Modifier = Modifier, sheets: Int = 5) {
    val reduced = rememberReducedMotion()
    val time by produceState(initialValue = 1.35f, reduced) {
        if (reduced) return@produceState
        // Relógio "infinito" do Compose: testes e o modo sem animação conseguem pausá-lo.
        val start = withInfiniteAnimationFrameNanos { it }
        while (true) withInfiniteAnimationFrameNanos { value = 1.35f + (it - start) / 1_000_000_000f }
    }
    val palette = sceneColors()
    // O livro abre uma vez, ao entrar: a capa gira de lado até ficar de frente.
    val opening = remember { Animatable(if (reduced) 1f else 0f) }
    LaunchedEffect(Unit) { opening.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) }

    Box(modifier.size(size).semantics { contentDescription = "Processando" }, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) { drawScene(time, palette, sheets, reduced) }
        // O Folha no meio, lendo e escrevendo, enquanto as folhas chegam voando até ele.
        br.com.estudario.ui.assistant.Folha(
            size = size * 0.56f,
            mood = br.com.estudario.ui.assistant.FolhaMood.THINKING,
            modifier = Modifier.graphicsLayer {
                val o = opening.value
                rotationY = (1f - o) * 88f
                cameraDistance = 14f * density
                alpha = 0.2f + 0.8f * o
                val breath = if (reduced) 1f else 1f + 0.025f * sin(time * 2.2f)
                scaleX = (0.82f + 0.18f * o) * breath
                scaleY = (0.82f + 0.18f * o) * breath
            },
        )
    }
}

/**
 * Tela inteira de processamento: a cena, a etapa atual em destaque, a lista de etapas com o que já
 * passou marcado, a barra de progresso e o tempo decorrido.
 *
 * As etapas avançam pelo tempo ([stageMillis] cada) e a última fica ativa até o trabalho terminar,
 * a barra nunca chega a 100% sozinha, ela só enche quando a tela sai. Assim o texto nunca promete
 * um fim que ainda não aconteceu.
 */
@Composable
fun EstudarioProcessView(
    title: String,
    stages: List<String>,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    stageMillis: Long = 5_500L,
    sceneSize: Dp = 260.dp,
    /**
     * Etapa real, quando quem chama sabe em que ponto está (ex.: leitura do PDF página a página).
     * Igual a [stages].size marca tudo como feito. Sem ela, as etapas andam pelo tempo.
     */
    stageIndex: Int? = null,
    /** Progresso real de 0 a 1; sem ele, a barra segue a curva por tempo. */
    progressValue: Float? = null,
    /** Texto da etapa atual no lugar do nome dela (ex.: "Lendo a página 23 de 88"). */
    stageDetail: String? = null,
    /** Texto quando tudo terminou. */
    doneLabel: String = "Pronto",
    footer: (@Composable () -> Unit)? = null,
) {
    val elapsed = rememberElapsedMillis()
    val current = stageIndex?.coerceIn(0, stages.size)
        ?: if (stages.isEmpty()) 0 else min((elapsed / stageMillis).toInt(), stages.lastIndex)
    val completed = stageIndex != null && stageIndex >= stages.size
    // Curva assintótica: anda rápido no começo e desacelera, sem nunca "acabar" por conta própria.
    val expected = (stageMillis * max(stages.size, 1)).toFloat()
    val target = progressValue?.coerceIn(0.03f, 1f)
        ?: (0.94f * (1f - exp(-elapsed / (expected * 0.55f)))).coerceIn(0.03f, 0.94f)
    val progress by animateFloatAsState(target, tween(600), label = "process-progress")

    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        EstudarioProcessScene(sceneSize)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            if (eyebrow != null) Text(
                eyebrow.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            if (stages.isNotEmpty()) AnimatedContent(
                targetState = when {
                    completed -> doneLabel
                    stageDetail != null -> stageDetail
                    else -> stages[current]
                },
                transitionSpec = {
                    (slideInVertically { it / 2 } + fadeIn(tween(350))) togetherWith (slideOutVertically { -it / 2 } + fadeOut(tween(250)))
                },
                label = "process-stage",
            ) { stage ->
                Text(
                    if (completed) stage else "$stage…",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
        ProcessProgressBar(progress, Modifier.fillMaxWidth(0.72f))
        Text(
            "${if (stages.isEmpty() || completed) "" else "Etapa ${current + 1} de ${stages.size} · "}${formatElapsed(elapsed)}",
            style = MaterialTheme.typography.bodySmall.copy(fontFeatureSettings = "tnum"),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (stages.size > 1) StageChecklist(stages, current, Modifier.widthIn(max = 420.dp).fillMaxWidth())
        footer?.invoke()
    }
}

/**
 * Diálogo de espera, na mesma linguagem da tela de processamento, mas compacto. Substitui o
 * diálogo com o livro pequeno ao lado do texto: toda espera do app ganha a cena inteira.
 */
@Composable
fun EstudarioProcessDialog(title: String, message: String? = null, steps: List<String> = emptyList()) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 6.dp,
            shadowElevation = 16.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.widthIn(max = 380.dp),
        ) {
            val elapsed = rememberElapsedMillis()
            val stepNow = if (steps.isEmpty()) 0 else min((elapsed / 2_400L).toInt(), steps.lastIndex)
            Column(
                Modifier.padding(horizontal = 26.dp, vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                EstudarioProcessScene(176.dp, sheets = 4)
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                val line = steps.getOrNull(stepNow)?.let { "$it…" } ?: message
                if (line != null) AnimatedContent(
                    targetState = line,
                    transitionSpec = { (slideInVertically { it / 2 } + fadeIn()) togetherWith (slideOutVertically { -it / 2 } + fadeOut()) },
                    label = "dialog-step",
                ) { text ->
                    Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                }
                ProcessProgressBar(null, Modifier.fillMaxWidth(0.7f).padding(top = 6.dp))
            }
        }
    }
}

// ------------------------------------------------------------------------------------ partes

@Composable
private fun ProcessProgressBar(progress: Float?, modifier: Modifier = Modifier) {
    val track = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    val fill = Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, estudarioColors().completed))
    val sweep by rememberInfiniteTransition(label = "bar").animateFloat(
        0f, 1f, infiniteRepeatable(tween(1_400, easing = LinearEasing), RepeatMode.Restart), label = "bar-sweep",
    )
    Canvas(modifier.height(6.dp)) {
        val r = CornerRadius(size.height / 2, size.height / 2)
        drawRoundRect(track, cornerRadius = r)
        if (progress != null) {
            drawRoundRect(fill, size = Size(size.width * progress, size.height), cornerRadius = r)
            // Um brilho corre por dentro da parte cheia: parece vivo mesmo quando a etapa demora.
            val x = size.width * progress * sweep
            drawRoundRect(
                Brush.horizontalGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.55f), Color.Transparent), x - 60f, x + 60f),
                size = Size(size.width * progress, size.height),
                cornerRadius = r,
            )
        } else {
            // Sem progresso conhecido: um trecho cheio corre de ponta a ponta.
            val w = size.width * 0.34f
            val x = (size.width + w) * sweep - w
            val start = max(0f, x)
            val end = min(size.width, x + w)
            if (end > start) drawRoundRect(fill, topLeft = Offset(start, 0f), size = Size(end - start, size.height), cornerRadius = r)
        }
    }
}

@Composable
private fun StageChecklist(stages: List<String>, current: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(Modifier.padding(horizontal = 18.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            stages.forEachIndexed { index, stage ->
                val done = index < current
                val active = index == current
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.size(22.dp), contentAlignment = Alignment.Center) {
                        when {
                            done -> Box(
                                Modifier.size(22.dp).clip(CircleShape).background(estudarioColors().completed),
                                contentAlignment = Alignment.Center,
                            ) { Icon(Icons.Rounded.Check, null, Modifier.size(15.dp), tint = estudarioColors().onCompleted) }
                            active -> CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.4.dp, color = MaterialTheme.colorScheme.primary)
                            else -> Box(Modifier.size(12.dp).border(1.6.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape))
                        }
                    }
                    Text(
                        stage,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                        color = when {
                            active -> MaterialTheme.colorScheme.onSurface
                            done -> MaterialTheme.colorScheme.onSurfaceVariant
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                        },
                    )
                }
            }
        }
    }
}

/** O livro da marca folheando sem parar, com a mesma virada do [EstudarioBookLoader]. */
@Composable
private fun BookFlipping(size: Dp, reduced: Boolean, modifier: Modifier = Modifier) {
    val colors = BookColors(
        page = MaterialTheme.colorScheme.primary,
        ink = MaterialTheme.colorScheme.surface,
        ribbon = estudarioColors().completed,
    )
    val flip = if (reduced) null else {
        val t by rememberInfiniteTransition(label = "scene-book").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                keyframes {
                    durationMillis = 1_700
                    0f at 0
                    0f at 520 using FastOutSlowInEasing
                    1f at 1_500
                },
                repeatMode = RepeatMode.Restart,
            ),
            label = "scene-page",
        )
        t
    }
    Canvas(modifier.size(size).aspectRatio(1f)) {
        drawEstudarioBook(colors, flip = flip, fitTop = BookShape.TOP_OUT - BookShape.LIFT)
    }
}

private data class SceneColors(
    val glow: Color,
    val ring: Color,
    val ringSoft: Color,
    val paper: Color,
    val paperEdge: Color,
    val line: Color,
    val pdf: Color,
    val accent: Color,
    val success: Color,
    val attention: Color,
    val spark: Color,
)

@Composable
private fun sceneColors(): SceneColors {
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.surface.luminance() < 0.5f
    return SceneColors(
        glow = scheme.primary,
        ring = scheme.primary.copy(alpha = 0.55f),
        ringSoft = scheme.primary.copy(alpha = 0.16f),
        paper = if (dark) Color(0xFFEDEBF7) else Color.White,
        paperEdge = if (dark) Color(0x33000000) else Color(0x1F1B1464),
        line = Color(0xFFB8B4CE),
        pdf = Color(0xFFE5484D),
        accent = scheme.primary,
        success = estudarioColors().completed,
        attention = Color(0xFFF2B33D),
        spark = if (dark) Color(0xFFFFF4D6) else scheme.primary,
    )
}

/** Tipos de material que chegam ao livro. A ordem é só visual. */
private enum class Sheet { PDF, LIST, QUESTION, HIGHLIGHT, OUTLINE }

private const val FLIGHT_SECONDS = 3.6f

private fun DrawScope.drawScene(time: Float, c: SceneColors, count: Int, reduced: Boolean) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = min(size.width, size.height) / 2

    // Halo que respira atrás de tudo.
    val pulse = 0.85f + 0.15f * sin(time * 1.6f)
    drawCircle(
        Brush.radialGradient(
            listOf(c.glow.copy(alpha = 0.26f * pulse), c.glow.copy(alpha = 0.08f), Color.Transparent),
            center = center,
            radius = radius * 0.98f,
        ),
        radius = radius,
        center = center,
    )

    // Anéis: um tracejado girando e um contínuo bem suave, com pontos orbitando.
    val ringR = radius * 0.78f
    drawCircle(c.ringSoft, ringR * 1.12f, center, style = Stroke(1.2.dp.toPx()))
    rotate(time * 22f, center) {
        drawCircle(
            c.ring,
            ringR,
            center,
            style = Stroke(
                width = 1.6.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 9.dp.toPx())),
            ),
        )
    }
    // Arco de "varredura" dando a volta, como um radar lendo o material.
    rotate(-time * 70f, center) {
        drawArc(
            Brush.sweepGradient(listOf(Color.Transparent, c.accent.copy(alpha = 0.55f)), center),
            startAngle = 0f,
            sweepAngle = 80f,
            useCenter = false,
            topLeft = Offset(center.x - ringR * 0.9f, center.y - ringR * 0.9f),
            size = Size(ringR * 1.8f, ringR * 1.8f),
            style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round),
        )
    }
    repeat(3) { i ->
        val speed = 0.55f + i * 0.23f
        val a = time * speed + i * 2.1f
        val r = ringR * (1.12f - i * 0.06f)
        drawCircle(
            if (i == 1) c.success else c.accent,
            radius = (3.2f - i * 0.5f).dp.toPx(),
            center = center + Offset(cos(a) * r, sin(a) * r),
        )
    }

    // Faíscas paradas no lugar, piscando em tempos diferentes.
    repeat(9) { i ->
        val a = i * 2.39996f + 0.4f
        val r = ringR * (0.62f + (i % 4) * 0.14f)
        val twinkle = ((sin(time * (1.3f + i * 0.21f) + i) + 1f) / 2f).pow(3f)
        if (twinkle > 0.05f) drawSparkle(center + Offset(cos(a) * r, sin(a) * r), (3.5f + 3f * twinkle).dp.toPx(), c.spark.copy(alpha = twinkle * 0.9f))
    }

    if (reduced) {
        // Parado: algumas folhas em volta do livro, sem movimento.
        listOf(Sheet.PDF to -2.3f, Sheet.QUESTION to -0.5f, Sheet.LIST to 2.4f).forEach { (kind, a) ->
            val p = center + Offset(cos(a) * ringR * 0.95f, sin(a) * ringR * 0.95f)
            drawSheet(kind, p, radius * 0.25f, a * 8f, 1f, c)
        }
        return
    }

    // O material voando para dentro do livro.
    for (i in 0 until count) {
        val offset = i * FLIGHT_SECONDS / count
        val phaseTime = time + offset
        val cycle = (phaseTime / FLIGHT_SECONDS).toInt()
        val p = (phaseTime % FLIGHT_SECONDS) / FLIGHT_SECONDS
        val seed = hash(cycle * 31 + i * 7)
        val kind = Sheet.entries[(seed % Sheet.entries.size + Sheet.entries.size) % Sheet.entries.size]
        val startAngle = (i * (2 * PI / count) + (seed % 100) / 100f * 0.9f).toFloat()
        val curl = if (seed % 2 == 0) 1.1f else -1.1f
        val eased = easeInCubic(p)
        val r = radius * (0.9f - 0.88f * eased)
        val a = startAngle + curl * eased
        val pos = center + Offset(cos(a) * r, sin(a) * r)
        val alpha = when {
            p < 0.12f -> p / 0.12f
            p > 0.82f -> ((1f - p) / 0.18f).coerceAtLeast(0f)
            else -> 1f
        }
        val scale = 1f - 0.72f * p.pow(1.6f)
        val tilt = sin(time * 2f + i) * 14f + (1f - p) * curl * 18f
        // Um rastro curto de pontinhos mostra a trajetória.
        if (p in 0.18f..0.85f) repeat(3) { k ->
            val back = (eased - 0.05f * (k + 1)).coerceAtLeast(0f)
            val rb = radius * (0.9f - 0.88f * back)
            val ab = startAngle + curl * back
            drawCircle(c.accent.copy(alpha = alpha * (0.35f - k * 0.1f)), (2.4f - k * 0.5f).dp.toPx(), center + Offset(cos(ab) * rb, sin(ab) * rb))
        }
        drawSheet(kind, pos, radius * 0.27f * scale, tilt, alpha, c)
        // Chegada: um anel que abre e some a partir do livro.
        if (p > 0.84f) {
            val b = (p - 0.84f) / 0.16f
            drawCircle(
                c.success.copy(alpha = (1f - b) * 0.55f),
                radius = radius * (0.2f + 0.3f * b),
                center = center,
                style = Stroke((3f * (1f - b) + 0.5f).dp.toPx()),
            )
            drawSparkle(center + Offset(cos(a) * radius * 0.26f, sin(a) * radius * 0.26f), (7f * (1f - b)).dp.toPx(), c.spark.copy(alpha = 1f - b))
        }
    }
}

/** Uma folha de material: papel com o desenho do que ela é (PDF, lista, questão, marcação, esquema). */
private fun DrawScope.drawSheet(kind: Sheet, at: Offset, h: Float, tiltDeg: Float, alpha: Float, c: SceneColors) {
    if (alpha <= 0.01f || h <= 1f) return
    val w = h * 0.78f
    translate(at.x - w / 2, at.y - h / 2) {
        rotate(tiltDeg, Offset(w / 2, h / 2)) {
            val corner = CornerRadius(h * 0.09f)
            // Sombra suave deslocada e o papel.
            drawRoundRect(Color.Black.copy(alpha = 0.12f * alpha), topLeft = Offset(h * 0.03f, h * 0.05f), size = Size(w, h), cornerRadius = corner)
            val fold = h * 0.2f
            val paper = Path().apply {
                moveTo(0f, corner.x)
                quadraticTo(0f, 0f, corner.x, 0f)
                lineTo(w - fold, 0f)
                lineTo(w, fold)
                lineTo(w, h - corner.x)
                quadraticTo(w, h, w - corner.x, h)
                lineTo(corner.x, h)
                quadraticTo(0f, h, 0f, h - corner.x)
                close()
            }
            drawPath(paper, c.paper.copy(alpha = alpha))
            drawPath(paper, c.paperEdge.copy(alpha = c.paperEdge.alpha * alpha), style = Stroke(h * 0.012f))
            // Orelha dobrada no canto.
            drawPath(
                Path().apply { moveTo(w - fold, 0f); lineTo(w - fold, fold); lineTo(w, fold); close() },
                c.line.copy(alpha = 0.55f * alpha),
            )
            val lineW = h * 0.045f
            fun bar(y: Float, from: Float, to: Float, color: Color = c.line) =
                drawLine(color.copy(alpha = color.alpha * alpha), Offset(w * from, h * y), Offset(w * to, h * y), lineW, StrokeCap.Round)
            when (kind) {
                Sheet.PDF -> {
                    drawRoundRect(c.pdf.copy(alpha = alpha), Offset(w * 0.12f, h * 0.14f), Size(w * 0.44f, h * 0.15f), CornerRadius(h * 0.03f))
                    // "PDF" em traços dentro da etiqueta, legível mesmo pequeno.
                    val y0 = h * 0.175f; val y1 = h * 0.255f; val ink = Color.White.copy(alpha = alpha); val s = h * 0.022f
                    val x = w * 0.18f
                    drawLine(ink, Offset(x, y0), Offset(x, y1), s, StrokeCap.Round)
                    drawLine(ink, Offset(x, y0), Offset(x + w * 0.06f, y0), s, StrokeCap.Round)
                    drawLine(ink, Offset(x, (y0 + y1) / 2), Offset(x + w * 0.06f, (y0 + y1) / 2), s, StrokeCap.Round)
                    drawLine(ink, Offset(x + w * 0.06f, y0), Offset(x + w * 0.06f, (y0 + y1) / 2), s, StrokeCap.Round)
                    val x2 = w * 0.3f
                    drawLine(ink, Offset(x2, y0), Offset(x2, y1), s, StrokeCap.Round)
                    drawLine(ink, Offset(x2, y0), Offset(x2 + w * 0.05f, (y0 + y1) / 2), s, StrokeCap.Round)
                    drawLine(ink, Offset(x2 + w * 0.05f, (y0 + y1) / 2), Offset(x2, y1), s, StrokeCap.Round)
                    val x3 = w * 0.42f
                    drawLine(ink, Offset(x3, y0), Offset(x3, y1), s, StrokeCap.Round)
                    drawLine(ink, Offset(x3, y0), Offset(x3 + w * 0.07f, y0), s, StrokeCap.Round)
                    drawLine(ink, Offset(x3, (y0 + y1) / 2), Offset(x3 + w * 0.05f, (y0 + y1) / 2), s, StrokeCap.Round)
                    bar(0.44f, 0.14f, 0.86f); bar(0.56f, 0.14f, 0.78f); bar(0.68f, 0.14f, 0.86f); bar(0.8f, 0.14f, 0.6f)
                }
                Sheet.LIST -> {
                    bar(0.2f, 0.14f, 0.6f, c.accent)
                    listOf(0.38f, 0.52f, 0.66f, 0.8f).forEach { y ->
                        drawCircle(c.accent.copy(alpha = alpha), h * 0.03f, Offset(w * 0.18f, h * y))
                        bar(y, 0.3f, if (y > 0.7f) 0.66f else 0.84f)
                    }
                }
                Sheet.QUESTION -> {
                    bar(0.18f, 0.14f, 0.86f); bar(0.28f, 0.14f, 0.6f)
                    listOf(0.46f, 0.6f, 0.74f).forEachIndexed { index, y ->
                        val o = Offset(w * 0.2f, h * y)
                        if (index == 1) drawCircle(c.success.copy(alpha = alpha), h * 0.045f, o)
                        else drawCircle(c.line.copy(alpha = alpha), h * 0.042f, o, style = Stroke(h * 0.016f))
                        bar(y, 0.34f, if (index == 2) 0.62f else 0.82f)
                    }
                }
                Sheet.HIGHLIGHT -> {
                    drawRoundRect(c.attention.copy(alpha = 0.55f * alpha), Offset(w * 0.1f, h * 0.36f), Size(w * 0.72f, h * 0.1f), CornerRadius(h * 0.02f))
                    bar(0.2f, 0.14f, 0.8f); bar(0.41f, 0.14f, 0.78f); bar(0.6f, 0.14f, 0.86f); bar(0.74f, 0.14f, 0.7f)
                }
                Sheet.OUTLINE -> {
                    bar(0.2f, 0.14f, 0.7f, c.accent)
                    bar(0.36f, 0.14f, 0.62f); bar(0.5f, 0.3f, 0.84f); bar(0.64f, 0.3f, 0.76f); bar(0.8f, 0.14f, 0.58f)
                    drawLine(c.line.copy(alpha = 0.6f * alpha), Offset(w * 0.2f, h * 0.42f), Offset(w * 0.2f, h * 0.66f), h * 0.014f)
                }
            }
        }
    }
}

/** Brilho de quatro pontas. */
private fun DrawScope.drawSparkle(at: Offset, r: Float, color: Color) {
    if (r <= 0.5f || color.alpha <= 0.01f) return
    val k = r * 0.28f
    val path = Path().apply {
        moveTo(at.x, at.y - r)
        quadraticTo(at.x + k * 0.3f, at.y - k * 0.3f, at.x + r, at.y)
        quadraticTo(at.x + k * 0.3f, at.y + k * 0.3f, at.x, at.y + r)
        quadraticTo(at.x - k * 0.3f, at.y + k * 0.3f, at.x - r, at.y)
        quadraticTo(at.x - k * 0.3f, at.y - k * 0.3f, at.x, at.y - r)
        close()
    }
    drawPath(path, color)
}

private fun easeInCubic(t: Float) = t * t * (0.35f + 0.65f * t)

private fun hash(value: Int): Int {
    var x = value * 0x45d9f3b
    x = (x xor (x ushr 16)) * 0x45d9f3b
    return (x xor (x ushr 16)) and 0x7fffffff
}

/** Tempo desde que a espera apareceu, em passos de 250 ms (não recompõe a cada quadro). */
@Composable
private fun rememberElapsedMillis(): Long {
    val elapsed by produceState(0L) {
        val start = withInfiniteAnimationFrameNanos { it }
        while (true) withInfiniteAnimationFrameNanos { now ->
            val bucket = (now - start) / 1_000_000L / 250L * 250L
            if (bucket != value) value = bucket
        }
    }
    return elapsed
}

private fun formatElapsed(millis: Long): String {
    val seconds = millis / 1000
    return "%d:%02d".format(seconds / 60, seconds % 60)
}
