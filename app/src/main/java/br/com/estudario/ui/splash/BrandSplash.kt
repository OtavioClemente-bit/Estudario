package br.com.estudario.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** A cor exata da splash do sistema: a abertura começa nela, sem salto. */
private val SplashIndigo = Color(0xFF3326CE)
private val DeepIndigo = Color(0xFF1E1A78)
private val Violet = Color(0xFF8B5CF6)
private val Mint = Color(0xFF7EE0B8)
private val Ink = Color(0xFF3326CE)

private val Smooth = CubicBezierEasing(0.2f, 0f, 0f, 1f)

/**
 * A abertura do app. Continua de onde a splash do sistema para (mesmo índigo, livro no centro) e
 * conta a marca em ~2 segundos: o livro se abre, o conteúdo se escreve na página da esquerda, o
 * certo se desenha na da direita e a fita cai marcando a página. Depois entram o nome, a frase e
 * a barra de carregamento. Só sai quando o tempo mínimo passou e o app está pronto ([ready]).
 */
@Composable
fun BrandSplash(ready: Boolean, onFinished: () -> Unit) {
    // Já começa meio aberto e com o brilho aceso: o primeiro quadro tem a marca, nunca um azul vazio.
    // 1 = escondido embaixo da tela, 0 = no lugar.
    val rise = remember { Animatable(1f) }
    val hello = remember { Animatable(0f) }
    val glow = remember { Animatable(0.7f) }
    val title = remember { Animatable(0f) }
    val tagline = remember { Animatable(0f) }
    val loader = remember { Animatable(0f) }
    val exit = remember { Animatable(0f) }
    val isReady by rememberUpdatedState(ready)
    val finish by rememberUpdatedState(onFinished)

    // Ícones claros na barra de status sobre o índigo; ao sair, volta ao que o tema pedia.
    val view = androidx.compose.ui.platform.LocalView.current
    androidx.compose.runtime.DisposableEffect(Unit) {
        val window = (view.context as? android.app.Activity)?.window
        val controller = window?.let { androidx.core.view.WindowCompat.getInsetsController(it, view) }
        val before = controller?.isAppearanceLightStatusBars
        val beforeNav = controller?.isAppearanceLightNavigationBars
        controller?.isAppearanceLightStatusBars = false
        controller?.isAppearanceLightNavigationBars = false
        onDispose {
            before?.let { controller.isAppearanceLightStatusBars = it }
            beforeNav?.let { controller.isAppearanceLightNavigationBars = it }
        }
    }

    LaunchedEffect(Unit) {
        // O Folha sobe de baixo da tela, quica e dá tchau. Tudo em menos de um segundo:
        // abertura é cumprimento, não espera.
        launch { glow.animateTo(1f, tween(400, easing = Smooth)) }
        launch { rise.animateTo(0f, spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow)) }
        launch { delay(260); hello.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium)) }
        launch { delay(180); title.animateTo(1f, tween(420, easing = Smooth)) }
        launch { delay(380); tagline.animateTo(1f, tween(380, easing = Smooth)) }
        delay(820)
        // Se o app ainda estiver carregando, a barra aparece; normalmente nem dá tempo.
        if (!isReady) launch { loader.animateTo(1f, tween(250, easing = Smooth)) }
        while (!isReady) delay(30)
        exit.animateTo(1f, tween(260, easing = Smooth))
        finish()
    }

    val infinite = rememberInfiniteTransition(label = "splash")
    val sweep by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(1100, easing = LinearEasing)), label = "sweep")
    val breathe by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(2400, easing = Smooth), RepeatMode.Reverse), label = "breathe")

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = 1f - exit.value; scaleX = 1f + 0.06f * exit.value; scaleY = scaleX }
            .background(SplashIndigo)
            .drawBehind {
                // O degradê nasce do índigo da splash do sistema, então a troca não aparece.
                val g = glow.value
                drawRect(Brush.verticalGradient(listOf(DeepIndigo.copy(alpha = 0.55f * g), Color.Transparent, Violet.copy(alpha = 0.55f * g))))
                drawCircle(
                    Brush.radialGradient(listOf(Color(0xFF8E85FF).copy(alpha = 0.45f * g), Color.Transparent), center = Offset(size.width / 2, size.height * 0.40f), radius = size.width * (0.55f + 0.05f * breathe)),
                    radius = size.width * (0.55f + 0.05f * breathe), center = Offset(size.width / 2, size.height * 0.40f),
                )
                drawCircle(
                    Brush.radialGradient(listOf(Mint.copy(alpha = 0.16f * g), Color.Transparent), center = Offset(size.width * 0.85f, size.height * 0.85f), radius = size.width * 0.6f),
                    radius = size.width * 0.6f, center = Offset(size.width * 0.85f, size.height * 0.85f),
                )
            },
    ) {
        val folhaSize = (maxWidth * 0.48f).coerceAtMost(230.dp)
        val travel = maxHeight
        Column(
            Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(Modifier.weight(0.9f))
            Box(contentAlignment = Alignment.TopEnd) {
                br.com.estudario.ui.assistant.Folha(
                    folhaSize,
                    Modifier.graphicsLayer { translationY = rise.value * travel.toPx() },
                    mood = br.com.estudario.ui.assistant.FolhaMood.WAVE,
                )
                Text(
                    "Oi!",
                    color = SplashIndigo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .graphicsLayer { scaleX = hello.value; scaleY = hello.value; alpha = hello.value.coerceIn(0f, 1f) }
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                )
            }
            Spacer(Modifier.height(28.dp))
            Wordmark(title.value)
            Spacer(Modifier.height(14.dp))
            Tagline(tagline.value)
            Spacer(Modifier.weight(1f))
            LoaderBar(loader.value, sweep)
            Spacer(Modifier.height(18.dp))
            Text(
                "Preparando seus estudos",
                color = Color.White.copy(alpha = 0.55f * loader.value),
                fontSize = 12.sp,
                letterSpacing = 1.2.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.navigationBarsPadding().height(44.dp))
        }
    }
}

/** "ESTUDÁRIO" letra a letra: cada uma sobe e aparece um pouco depois da anterior. */
@Composable
private fun Wordmark(progress: Float) {
    val word = "ESTUDÁRIO"
    Row(horizontalArrangement = Arrangement.Center) {
        word.forEachIndexed { i, ch ->
            val local = ((progress * (word.length + 4) - i) / 4f).coerceIn(0f, 1f)
            val eased = Smooth.transform(local)
            Text(
                ch.toString(),
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                modifier = Modifier.graphicsLayer {
                    alpha = eased
                    translationY = (1f - eased) * 18.dp.toPx()
                },
            )
        }
    }
}

/** A frase de efeito entre dois traços que se estendem a partir do centro. */
@Composable
private fun Tagline(progress: Float) {
    val eased = Smooth.transform(progress)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(28.dp * eased).height(1.5.dp).background(Brush.horizontalGradient(listOf(Color.Transparent, Mint))))
            Text(
                "  DO EDITAL À APROVAÇÃO  ",
                color = Mint.copy(alpha = eased),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.4.sp,
            )
            Box(Modifier.width(28.dp * eased).height(1.5.dp).background(Brush.horizontalGradient(listOf(Mint, Color.Transparent))))
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "Cada dia de estudo te deixa mais perto da posse.",
            color = Color.White.copy(alpha = 0.82f * eased),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer { translationY = (1f - eased) * 10.dp.toPx() },
        )
    }
}

/** Trilho fino com um brilho que corre da esquerda para a direita, em vez de um círculo girando. */
@Composable
private fun LoaderBar(appear: Float, sweep: Float) {
    Box(
        Modifier
            .graphicsLayer { alpha = appear; scaleX = 0.6f + 0.4f * appear }
            .width(132.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.16f))
            .drawBehind {
                val w = size.width * 0.42f
                val x = -w + (size.width + w) * sweep
                drawRect(
                    Brush.horizontalGradient(listOf(Color.Transparent, Mint, Color.White, Color.Transparent), startX = x, endX = x + w),
                    topLeft = Offset(x, 0f), size = androidx.compose.ui.geometry.Size(w, size.height),
                )
            },
    )
}

/**
 * O livro do ícone, em versão "de verdade", na grade de 108 do ícone adaptativo: páginas curvas
 * com sombreado, o bloco de folhas embaixo de cada página, a dobra escura no meio, a sombra no
 * chão e a fita com volume. [open] abre as páginas a partir da lombada.
 */
private fun DrawScope.drawRealisticBook(open: Float, lines: Float, check: Float, ribbon: Float, breathe: Float) {
    val k = size.width / 108f
    val float = -1.6f * breathe
    scale(k, k, pivot = Offset.Zero) {
        translate(0f, float) {
            // Sombra no chão: achata quando o livro "sobe" na respiração.
            val shadowW = 40f * open + 6f
            scale(1f, 0.16f, pivot = Offset(54f, 92f - float)) {
                drawCircle(
                    Brush.radialGradient(listOf(Color(0xFF120E50).copy(alpha = 0.55f), Color(0xFF120E50).copy(alpha = 0.18f), Color.Transparent), center = Offset(54f, 92f - float), radius = shadowW),
                    radius = shadowW, center = Offset(54f, 92f - float),
                )
            }

            val e = open.coerceIn(0f, 1f)
            // Cada página gira na lombada: o X é comprimido em direção ao centro enquanto abre.
            fun lx(x: Float) = 54f - (54f - x) * e
            fun rx(x: Float) = 54f + (x - 54f) * e

            // Bloco de folhas (espessura), em camadas sob cada página.
            for (layer in 3 downTo 1) {
                val dy = layer * 1.5f
                val tone = Color(0xFFD9D6FF).copy(alpha = 0.55f + 0.12f * (3 - layer))
                drawPath(leftPage(::lx, dy, -layer * 0.9f), tone)
                drawPath(rightPage(::rx, dy, layer * 0.9f), tone)
            }

            // Páginas com sombreado: mais escuras perto da dobra, claras na borda.
            drawPath(leftPage(::lx, 0f, 0f), Brush.horizontalGradient(listOf(Color.White, Color(0xFFF4F3FF), Color(0xFFDCD9FA)), startX = lx(16f), endX = 54f))
            drawPath(rightPage(::rx, 0f, 0f), Brush.horizontalGradient(listOf(Color(0xFFDCD9FA), Color(0xFFF4F3FF), Color.White), startX = 54f, endX = rx(92f)))
            // Dobra central.
            drawRect(
                Brush.horizontalGradient(listOf(Color.Transparent, Color(0x553326CE), Color.Transparent), startX = 49f, endX = 59f),
                topLeft = Offset(49f, 33f), size = androidx.compose.ui.geometry.Size(10f, 50f),
            )

            if (e > 0.85f) {
                // Linhas do conteúdo, escritas uma depois da outra.
                val segs = listOf(
                    Triple(Offset(22f, 43.5f), Offset(38f, 46.3f), 3.6f),
                    Triple(Offset(22f, 52.5f), Offset(45f, 56.5f), 2.6f),
                    Triple(Offset(22f, 60f), Offset(45f, 64f), 2.6f),
                    Triple(Offset(22f, 67.5f), Offset(36f, 69.9f), 2.6f),
                )
                segs.forEachIndexed { i, (a, b, w) ->
                    val p = (lines * segs.size - i).coerceIn(0f, 1f)
                    if (p > 0f) drawLine(Ink, Offset(lx(a.x), a.y), Offset(lx(a.x + (b.x - a.x) * p), a.y + (b.y - a.y) * p), w, StrokeCap.Round)
                }
                // O certo, desenhado como a mão faria.
                if (check > 0f) {
                    val path = Path().apply { moveTo(67.5f, 53.5f); lineTo(73f, 59f); lineTo(84.5f, 45f) }
                    val measure = PathMeasure().apply { setPath(path, false) }
                    val partial = Path()
                    measure.getSegment(0f, measure.length * check, partial, true)
                    drawPath(partial, Ink, style = Stroke(3.8f, cap = StrokeCap.Round, join = StrokeJoin.Round))
                }
                val under = (check * 1.4f - 0.4f).coerceIn(0f, 1f)
                if (under > 0f) drawLine(Ink.copy(alpha = 0.85f), Offset(66f, 73f), Offset(66f + 20f * under, 73f - 3.4f * under), 2.6f, StrokeCap.Round)
            }

            // A fita: cai do topo e balança até parar, com um lado mais escuro para dar volume.
            if (ribbon > 0f) {
                val len = 58f * ribbon
                val top = 32.9f
                val bottom = top + len
                val tail = Path().apply {
                    moveTo(60f, top); lineTo(64.5f, top - 0.8f); lineTo(64.5f, bottom)
                    lineTo(62.25f, bottom - 3f); lineTo(60f, bottom); close()
                }
                drawPath(tail, Brush.horizontalGradient(listOf(Color(0xFF4FC79A), Mint, Color(0xFFA8F0D2)), startX = 60f, endX = 64.5f))
                drawLine(Color(0x33000000), Offset(64.5f, top), Offset(64.5f, bottom), 0.6f)
            }
        }
    }
}

private fun leftPage(x: (Float) -> Float, dy: Float, dx: Float) = Path().apply {
    moveTo(x(16f) + dx, 28f + dy)
    quadraticTo(x(34f) + dx, 27.5f + dy, x(51f) + dx, 34f + dy)
    lineTo(x(51f) + dx, 82f + dy)
    quadraticTo(x(34f) + dx, 75.5f + dy, x(16f) + dx, 76f + dy)
    close()
}

private fun rightPage(x: (Float) -> Float, dy: Float, dx: Float) = Path().apply {
    moveTo(x(57f) + dx, 34f + dy)
    quadraticTo(x(74f) + dx, 27.5f + dy, x(92f) + dx, 28f + dy)
    lineTo(x(92f) + dx, 76f + dy)
    quadraticTo(x(74f) + dx, 75.5f + dy, x(57f) + dx, 82f + dy)
    close()
}

/**
 * O livro da abertura em tamanho livre, para outras telas da marca (entrada com a conta): monta-se
 * uma vez (abre, escreve, marca o certo, a fita cai) e depois fica flutuando de leve.
 */
@Composable
fun AnimatedBrandBook(size: androidx.compose.ui.unit.Dp, modifier: Modifier = Modifier) {
    val open = remember { Animatable(0.2f) }
    val lines = remember { Animatable(0f) }
    val check = remember { Animatable(0f) }
    val ribbon = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        open.animateTo(1f, tween(520, easing = Smooth))
        launch { lines.animateTo(1f, tween(560, easing = LinearEasing)) }
        launch { delay(260); check.animateTo(1f, tween(420, easing = Smooth)) }
        launch { delay(380); ribbon.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMediumLow)) }
    }
    val infinite = rememberInfiniteTransition(label = "brand-book")
    val breathe by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(2400, easing = Smooth), RepeatMode.Reverse), label = "breathe")
    Canvas(modifier.size(size)) { drawRealisticBook(open.value, lines.value, check.value, ribbon.value, breathe) }
}
