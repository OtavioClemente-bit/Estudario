package br.com.estudario.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.theme.estudarioColors
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

/**
 * A marca do Estudário dentro da interface: **o livro aberto**, o mesmo do ícone na tela inicial
 * do celular, com o que a pessoa faz nele desenhado nas páginas. Na esquerda, o conteúdo (linhas
 * de texto); na direita, o que já foi dominado (o certo); no meio, a fita verde que marca onde ela
 * parou. Nada aqui é enfeite: é o app inteiro em um símbolo.
 *
 * O mesmo desenho é a animação de espera do app ([EstudarioBookLoader]): a página da direita vira
 * para a esquerda levando o próprio desenho junto, e a volta encaixa exatamente no começo, por isso
 * o loop não tem salto.
 */
@Composable
fun EstudarioGlyph(size: Dp = 28.dp, modifier: Modifier = Modifier) {
    val colors = bookColors()
    Canvas(modifier.size(size)) {
        drawEstudarioBook(colors, flip = null, fitTop = BookShape.TOP_OUT)
    }
}

/**
 * O carregamento do Estudário: o livro folheando. Substitui o spinner genérico em toda espera do
 * app (diálogos, telas carregando, o botão do Google conectando).
 *
 * Com animações desligadas no sistema, o livro aparece parado, o texto ao lado continua dizendo
 * que algo está acontecendo.
 */
@Composable
fun EstudarioBookLoader(size: Dp = 48.dp, modifier: Modifier = Modifier) {
    val colors = bookColors()
    val reduced = rememberReducedMotion()
    val flip = if (reduced) {
        null
    } else {
        val transition = rememberInfiniteTransition(label = "book-loader")
        val t by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                keyframes {
                    durationMillis = BookShape.CYCLE_MS
                    0f at 0
                    // Uma pausa curta com o livro aberto, a virada, e o assentamento.
                    0f at BookShape.HOLD_MS using FastOutSlowInEasing
                    1f at BookShape.CYCLE_MS - BookShape.SETTLE_MS
                },
                repeatMode = RepeatMode.Restart,
            ),
            label = "page-turn",
        )
        t
    }
    Canvas(modifier.size(size).progressSemantics()) {
        // Espaço acima do livro para a página levantar sem ser cortada.
        drawEstudarioBook(colors, flip = flip, fitTop = BookShape.TOP_OUT - BookShape.LIFT)
    }
}

/**
 * Assinatura tipográfica: o livro e o nome, com o rastreamento aberto que o app usa como "voz"
 * (o mesmo estilo dos rótulos AGORA / HOJE / SEU PROGRESSO).
 */
@Composable
fun EstudarioWordmark(
    modifier: Modifier = Modifier,
    tagline: String? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
            EstudarioGlyph(size = 30.dp)
            Text(
                "ESTUDÁRIO",
                style = MaterialTheme.typography.titleLarge,
                color = color,
            )
        }
        if (tagline != null) {
            Text(
                tagline,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
            )
        }
    }
}

// ------------------------------------------------------------------------------------ desenho

/** Cores do livro. A tinta das páginas é a cor da superfície: os traços parecem vazados. */
internal data class BookColors(val page: Color, val ink: Color, val ribbon: Color)

@Composable
private fun bookColors() = BookColors(
    page = MaterialTheme.colorScheme.primary,
    ink = MaterialTheme.colorScheme.surface,
    ribbon = estudarioColors().completed,
)

/**
 * A geometria do livro, na mesma grade de 108 unidades do ícone adaptativo do Android
 * (`res/drawable/ic_launcher_foreground.xml`). Mudar um número aqui sem mudar lá descasa a marca
 * do app da marca do celular.
 */
internal object BookShape {
    const val OUT_L = 16f
    const val OUT_R = 92f
    const val SPINE_L = 51f
    const val SPINE_R = 57f
    const val HINGE = 54f
    const val TOP_OUT = 28f
    const val TOP_IN = 34f
    const val BOT_OUT = 76f
    const val BOT_IN = 82f
    const val RIBBON_END = 91f

    /** Quanto a borda externa da página sobe no meio da virada. */
    const val LIFT = 7f

    const val CYCLE_MS = 1_250
    const val HOLD_MS = 260
    const val SETTLE_MS = 170

    private const val SLOPE = (TOP_IN - TOP_OUT) / (SPINE_L - OUT_L)

    /** Y de uma linha paralela à borda de cima da página esquerda. */
    fun onLeft(x: Float, base: Float) = Offset(x, base + SLOPE * (x - OUT_L))

    /** Y de uma linha paralela à borda de cima da página direita. */
    fun onRight(x: Float, base: Float) = Offset(x, base + SLOPE * (OUT_R - x))

    val leftPage = listOf(Offset(OUT_L, TOP_OUT), Offset(SPINE_L, TOP_IN), Offset(SPINE_L, BOT_IN), Offset(OUT_L, BOT_OUT))
    val rightPage = listOf(Offset(SPINE_R, TOP_IN), Offset(OUT_R, TOP_OUT), Offset(OUT_R, BOT_OUT), Offset(SPINE_R, BOT_IN))

    /** Página esquerda: um título e três linhas de texto, o conteúdo. */
    val leftDrawing: List<Pair<List<Offset>, Float>> = listOf(
        listOf(onLeft(22f, 42.5f), onLeft(38f, 42.5f)) to 3.6f,
        listOf(onLeft(22f, 51.5f), onLeft(45f, 51.5f)) to 2.6f,
        listOf(onLeft(22f, 59f), onLeft(45f, 59f)) to 2.6f,
        listOf(onLeft(22f, 66.5f), onLeft(36f, 66.5f)) to 2.6f,
    )

    /** Página direita: o certo e uma linha, o que já foi dominado. */
    val rightDrawing: List<Pair<List<Offset>, Float>> = listOf(
        listOf(Offset(67.5f, 53.5f), Offset(73f, 59f), Offset(84.5f, 45f)) to 3.8f,
        listOf(onRight(66f, 68.5f), onRight(86f, 68.5f)) to 2.6f,
    )

    val ribbon = listOf(
        onRight(60f, TOP_OUT - 0.6f),
        onRight(64.5f, TOP_OUT - 0.6f),
        Offset(64.5f, RIBBON_END),
        Offset(62.25f, RIBBON_END - 3f),
        Offset(60f, RIBBON_END),
    )
}

/**
 * Desenha o livro ocupando a largura disponível, centralizado.
 *
 * [flip] é o andamento da virada de página (0 = aberto, 1 = a página já passou para a esquerda);
 * `null` desenha o livro parado. A página que vira é a da direita, com o desenho dela; depois do
 * meio da virada aparece o verso, que é exatamente a página esquerda, então o quadro final é
 * idêntico ao inicial.
 */
internal fun DrawScope.drawEstudarioBook(colors: BookColors, flip: Float?, fitTop: Float) {
    val boxWidth = BookShape.OUT_R - BookShape.OUT_L
    val boxHeight = BookShape.RIBBON_END - fitTop
    val scale = minOf(size.width / boxWidth, size.height / boxHeight)
    val dx = (size.width - boxWidth * scale) / 2f - BookShape.OUT_L * scale
    val dy = (size.height - boxHeight * scale) / 2f - fitTop * scale

    withTransform({
        translate(dx, dy)
        scale(scale, scale, pivot = Offset.Zero)
    }) {
        quad(BookShape.leftPage, colors.page)
        quad(BookShape.rightPage, colors.page)
        BookShape.leftDrawing.forEach { (points, width) -> strokeLine(points, width, colors.ink) }
        BookShape.rightDrawing.forEach { (points, width) -> strokeLine(points, width, colors.ink) }

        if (flip != null && flip > 0f && flip < 1f) {
            val angle = PI * flip
            val c = cos(angle).toFloat()
            val s = sin(angle).toFloat()
            val front = c >= 0f
            // A página escurece quando fica de lado para a luz, é isso que dá volume à virada.
            val shaded = lerp(Color.Black, colors.page, 0.80f + 0.20f * abs(c))
            val source = if (front) BookShape.rightPage else BookShape.leftPage
            quad(source.map { flipPoint(it, c, s, front) }, shaded)
            val inkOnPage = lerp(shaded, colors.ink, abs(c).pow(0.6f))
            val drawing = if (front) BookShape.rightDrawing else BookShape.leftDrawing
            drawing.forEach { (points, width) ->
                strokeLine(points.map { flipPoint(it, c, s, front) }, width * max(0.35f, abs(c)), inkOnPage)
            }
        }

        quad(BookShape.ribbon, colors.ribbon)
    }
}

/**
 * Leva um ponto da página para a posição dele durante a virada: comprime na horizontal em torno da
 * lombada (o cosseno do ângulo) e levanta a borda externa (o seno), mais quanto mais longe da lombada.
 */
private fun flipPoint(p: Offset, c: Float, s: Float, fromRight: Boolean): Offset {
    val u = if (fromRight) {
        (p.x - BookShape.HINGE) / (BookShape.OUT_R - BookShape.HINGE)
    } else {
        (BookShape.HINGE - p.x) / (BookShape.HINGE - BookShape.OUT_L)
    }
    val x = BookShape.HINGE + (p.x - BookShape.HINGE) * (if (fromRight) c else -c)
    return Offset(x, p.y - BookShape.LIFT * s * u)
}

private fun DrawScope.quad(points: List<Offset>, color: Color) {
    val path = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) lineTo(points[i].x, points[i].y)
        close()
    }
    drawPath(path, color)
}

private fun DrawScope.strokeLine(points: List<Offset>, width: Float, color: Color) {
    val path = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) lineTo(points[i].x, points[i].y)
    }
    drawPath(path, color, style = Stroke(width = width, cap = StrokeCap.Round, join = StrokeJoin.Round))
}
