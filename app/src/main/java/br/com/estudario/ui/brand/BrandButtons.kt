package br.com.estudario.ui.brand

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.ui.components.rememberReducedMotion
import kotlinx.coroutines.launch

/*
 * O botão "folha de caderno" do Estudário (aprovado pelo dono em 22/11, o mesmo do site): todo botão
 * é um pedaço de folha de fichário. Por trás uma folha de baixo deslocada e girada; na frente a folha
 * com a faixa do fichário à esquerda (com o ícone da turma ou dois furos), a linha de margem, a pauta
 * fraca e o reflexo no topo. No toque a folha afunda, o brilho atravessa, o canto dobra, o carimbo
 * se espalha e o ícone faz a animação da sua ação. Cada AÇÃO tem a sua cor e o seu ícone (NoteKind).
 *
 * Button, OutlinedButton, FilledTonalButton, TextButton e IconButton daqui têm a mesma assinatura dos
 * do Material, então o app inteiro desenha a folha; `kind` escolhe a identidade da ação.
 */

/** A animação própria do ícone de cada ação, tocada no clique. */
enum class NoteMotion { None, Tilt, Draw, Pulse, Flip, Spin, Turn, Stamp, Shine, Rise, Fly }

/** As ações do app. Cada uma tem cor, ícone da turma e animação. */
enum class NoteKind(val glyph: Glyph?, val motion: NoteMotion) {
    /** Estudar teoria, começar tópico. */
    Study(Glyph.OpenBook, NoteMotion.Tilt),
    /** Confirmar resposta. */
    Confirm(Glyph.Check, NoteMotion.Draw),
    /** Treinar questões, nova sessão. */
    Practice(Glyph.Target, NoteMotion.Pulse),
    /** Revisar, flashcards. */
    Review(Glyph.Cards, NoteMotion.Flip),
    /** Simulado. */
    Simulado(Glyph.Timer, NoteMotion.Spin),
    /** Foco. */
    Focus(Glyph.Timer, NoteMotion.Turn),
    /** Concluir tarefa. */
    Complete(Glyph.Medal, NoteMotion.Stamp),
    /** Assinar, planos pagos. */
    Subscribe(Glyph.Gem, NoteMotion.Shine),
    /** Enviar edital em PDF. */
    Upload(Glyph.Doc, NoteMotion.Rise),
    /** Próxima, continuar. */
    Next(Glyph.Forward, NoteMotion.Fly),
    /** Ação principal comum (salvar, entrar): índigo com furos. */
    Primary(null, NoteMotion.None),
    /** Ação comum (cancelar, voltar, fechar): papel com furos. */
    Paper(null, NoteMotion.None),
    /** Ajustes. */
    Settings(Glyph.Gear, NoteMotion.Spin),
    /** Ajuda. */
    Help(Glyph.Help, NoteMotion.Tilt),
    /** Notificações, lembretes. */
    Notify(Glyph.Bell, NoteMotion.Tilt),
    /** Apagar, sair da conta. */
    Danger(Glyph.Warning, NoteMotion.Pulse),
}

enum class NoteSize { Normal, Small, Block }

private val PenLight = Color(0xFF26234A)
private val PenDark = Color(0xFF05040F)
private val Indigo = Color(0xFF5B4FF0)
private val IndigoNight = Color(0xFF7A6EFF)
private val Sun = Color(0xFFFFC94A)
private val Red = Color(0xFFE5484D)
private val Ink = Color(0xFF26234A)
private val PaperLight = Color(0xFFFFFDF6)
private val PaperNight = Color(0xFFEDEBFF)

/** As cores de uma variante: face, margem, texto e os tons derivados. */
data class NotePalette(val face: Color, val margin: Color, val ink: Color) {
    val strip: Color get() = lerp(face, Color.Black, 0.09f)
    val under: Color get() = lerp(face, Color.Black, 0.34f)
    val rule: Color get() = if (face.luminance() < 0.45f) Color.White.copy(alpha = 0.16f) else Ink.copy(alpha = 0.14f)
}

fun notePalette(kind: NoteKind, dark: Boolean, enabled: Boolean = true): NotePalette {
    if (!enabled) {
        return if (dark) NotePalette(Color(0xFF2E2B57), Color(0xFF55517F), Color(0xFFB4B1D2))
        else NotePalette(Color(0xFFECE9DE), Color(0xFFC9C5B8), Color(0xFF5E5B72))
    }
    val indigo = if (dark) IndigoNight else Indigo
    val paper = if (dark) PaperNight else PaperLight
    return when (kind) {
        NoteKind.Study, NoteKind.Simulado, NoteKind.Primary -> NotePalette(indigo, Sun, Color.White)
        NoteKind.Confirm -> NotePalette(Color(0xFF2FB57A), Sun, Color.White)
        NoteKind.Practice -> NotePalette(Color(0xFFFF7A5C), Ink, Ink)
        NoteKind.Review -> NotePalette(Color(0xFFB9A7FF), Ink, Ink)
        NoteKind.Focus, NoteKind.Subscribe -> NotePalette(Sun, Indigo, Ink)
        NoteKind.Danger -> NotePalette(paper, Red, Color(0xFFC6402F))
        else -> NotePalette(paper, Red, Ink)
    }
}

/** A tinta de caneta do contorno (e a sombra dura dos recortes de cartão, que também a usam). */
@Composable
fun cutInk(): Color = if (isCadernoNight()) PenDark else PenLight

/** Mantido para quem desenha coisas com o mesmo canto (cartões). */
val BrandButtonShape = RoundedCornerShape(topStart = 6.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 6.dp)

private class NoteMetrics(val minHeight: Dp, val rail: Dp, val endPad: Dp, val fontSize: Float, val glyph: Dp, val holes: Int)

private fun metrics(size: NoteSize) = when (size) {
    NoteSize.Small -> NoteMetrics(42.dp, 30.dp, 22.dp, 13.5f, 22.dp, 1)
    else -> NoteMetrics(54.dp, 44.dp, 30.dp, 15.5f, 27.dp, 2)
}

private val BackX = 6.dp
private val BackY = 7.dp

/**
 * O botão folha de caderno, com texto. `glyph` troca o ícone da ação (sem ícone: os furos do
 * fichário). Texto em uma linha só: encolhe um pouco antes de cortar com reticências.
 */
@Composable
fun NoteButton(
    kind: NoteKind,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glyph: Glyph? = kind.glyph,
    size: NoteSize = NoteSize.Normal,
    enabled: Boolean = true,
    contentDescription: String? = null,
) {
    val m = metrics(size)
    NoteSurface(
        kind = kind, size = size, enabled = enabled, glyph = glyph,
        onClick = onClick,
        modifier = if (size == NoteSize.Block) modifier.fillMaxWidth() else modifier,
        contentDescription = contentDescription,
        interactionSource = null,
        rail = null,
    ) {
        BasicText(
            text,
            style = noteTextStyle(m).copy(color = LocalContentColor.current),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            autoSize = TextAutoSize.StepBased(minFontSize = (m.fontSize - 3f).sp, maxFontSize = m.fontSize.sp, stepSize = 0.5.sp),
        )
    }
}

@Composable
private fun noteTextStyle(m: NoteMetrics): TextStyle =
    MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = m.fontSize.sp, letterSpacing = 0.1.sp)

/** O miolo comum: folha de baixo, folha, faixa com ícone, movimento, carimbo e háptico. */
@Composable
internal fun NoteSurface(
    kind: NoteKind,
    size: NoteSize,
    enabled: Boolean,
    glyph: Glyph?,
    onClick: () -> Unit,
    modifier: Modifier,
    contentDescription: String?,
    interactionSource: MutableInteractionSource?,
    rail: (@Composable () -> Unit)?,
    paletteOverride: NotePalette? = null,
    contentPadding: PaddingValues? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val m = metrics(size)
    val dark = isCadernoNight()
    val pal = if (enabled) paletteOverride ?: notePalette(kind, dark) else notePalette(kind, dark, false)
    val pen = (if (dark) PenDark else PenLight).copy(alpha = if (enabled) 1f else 0.5f)
    val back = (if (dark) Color(0xFF3A3470) else PaperLight).copy(alpha = if (enabled) 1f else 0.6f)
    val hole = MaterialTheme.colorScheme.background
    val reduce = rememberReducedMotion()
    val src = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    val active = pressed && enabled
    val press by animateFloatAsState(
        if (active) 1f else 0f,
        if (reduce) snap() else spring(dampingRatio = 0.5f, stiffness = 700f),
        label = "note-press",
    )
    // Movimento leve (roda em celular fraco): a folha afunda, o carimbo abre e o ícone faz a sua animação. Sem brilho nem canto dobrado.
    val fold = 0f
    val sweep = remember { Animatable(-1f) }
    val stamp = remember { Animatable(-1f) }
    val icon = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val click: () -> Unit = {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        if (!reduce) {
            scope.launch { stamp.snapTo(0f); stamp.animateTo(1f, tween(500, easing = FastOutSlowInEasing)); stamp.snapTo(-1f) }
            scope.launch { icon.snapTo(0f); icon.animateTo(1f, tween(650, easing = FastOutSlowInEasing)); icon.snapTo(0f) }
        }
        onClick()
    }
    val showRail = glyph != null || rail != null
    Box(
        modifier
            .defaultMinSize(minWidth = 64.dp, minHeight = m.minHeight)
            .then(if (contentDescription != null) Modifier.semantics { this.contentDescription = contentDescription } else Modifier)
            .clickable(interactionSource = src, indication = null, enabled = enabled, role = Role.Button, onClick = click)
            .drawBehind {
                drawNote(
                    pal = pal, pen = pen, back = back, hole = hole, rail = m.rail.toPx(), holes = if (showRail) 0 else m.holes,
                    press = press, fold = fold, sweep = sweep.value, stamp = stamp.value, ruled = true,
                )
            },
        propagateMinConstraints = true,
    ) {
        // a faixa do fichário: o ícone da ação (ou os furos, desenhados acima)
        if (showRail) {
            Box(Modifier.matchParentSize().padding(end = BackX, bottom = BackY).graphicsLayer { translationX = 3.dp.toPx() * press; translationY = 4.dp.toPx() * press }) {
                Box(Modifier.width(m.rail).fillMaxHeight().align(Alignment.CenterStart), contentAlignment = Alignment.Center) {
                    if (rail != null) rail() else if (glyph != null) {
                        Canvas(Modifier.size(m.glyph).noteIconMotion(kind.motion) { icon.value }) { drawGlyph(glyph, muted = !enabled) }
                    }
                }
            }
        }
        Row(
            Modifier
                .padding(end = BackX, bottom = BackY)
                .graphicsLayer { translationX = 3.dp.toPx() * press; translationY = 4.dp.toPx() * press }
                .padding(contentPadding ?: PaddingValues(start = m.rail + 10.dp, end = m.endPad, top = 6.dp, bottom = 6.dp)),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(LocalContentColor provides pal.ink, LocalNoteRail provides showRail) {
                ProvideTextStyle(noteTextStyle(m).copy(color = pal.ink)) { content() }
            }
        }
    }
}

/** A animação própria de cada ícone, de 0 (parado) a 1 (fim), indo e voltando. */
private fun Modifier.noteIconMotion(motion: NoteMotion, t: () -> Float): Modifier = graphicsLayer {
    val p = t()
    val wave = kotlin.math.sin(p * Math.PI).toFloat()
    when (motion) {
        NoteMotion.Tilt -> rotationZ = -18f * wave
        NoteMotion.Pulse -> { scaleX = 1f + 0.22f * wave; scaleY = scaleX }
        NoteMotion.Flip -> rotationY = 180f * wave
        NoteMotion.Spin -> rotationZ = 40f * wave
        NoteMotion.Turn -> rotationZ = 180f * p
        NoteMotion.Stamp -> { scaleX = 1f + 0.35f * wave; scaleY = scaleX; rotationZ = -10f * wave }
        NoteMotion.Shine -> { scaleX = 1f + 0.15f * wave; scaleY = scaleX; alpha = 1f - 0.35f * wave * (1f - p) }
        NoteMotion.Rise -> translationY = -6.dp.toPx() * wave
        NoteMotion.Fly -> { translationX = 8.dp.toPx() * wave; alpha = 1f - 0.5f * wave }
        NoteMotion.Draw -> { scaleX = if (p > 0f) 0.4f + 0.6f * p else 1f; transformOrigin = TransformOrigin(0f, 0.5f) }
        NoteMotion.None -> Unit
    }
}

private fun pageShape(w: Float, h: Float, small: Float, big: Float, fold: Float): Path {
    val p = Path().apply {
        addRoundRect(
            RoundRect(0f, 0f, w, h, topLeftCornerRadius = CornerRadius(small), topRightCornerRadius = CornerRadius(big),
                bottomRightCornerRadius = CornerRadius(big), bottomLeftCornerRadius = CornerRadius(small)),
        )
    }
    if (fold <= 0.5f) return p
    val cut = Path().apply { moveTo(w - fold, h + 1f); lineTo(w + 1f, h + 1f); lineTo(w + 1f, h - fold); close() }
    return Path().apply { op(p, cut, PathOperation.Difference) }
}

private fun DrawScope.drawNote(
    pal: NotePalette, pen: Color, back: Color, hole: Color, rail: Float, holes: Int,
    press: Float, fold: Float, sweep: Float, stamp: Float, ruled: Boolean,
    small: Float = 6.dp.toPx(), big: Float = 20.dp.toPx(),
) {
    val w = size.width - BackX.toPx()
    val h = size.height - BackY.toPx()
    if (w <= 0f || h <= 0f) return
    val stroke = 2.5.dp.toPx()
    val bigR = minOf(big, h / 2f)
    val smallR = minOf(small, h / 2f)
    // 1. a folha de baixo
    val base = pageShape(w, h, smallR, bigR, 0f)
    val bx = (5f + (1f - 5f) * press).dp.toPx()
    val by = (6f + (1f - 6f) * press).dp.toPx()
    val br = 2f + (0.8f - 2f) * press
    translate(bx, by) {
        rotate(br, pivot = Offset(w / 2f, h / 2f)) {
            drawPath(base, back)
            drawPath(base, pen, style = Stroke(stroke, join = StrokeJoin.Round))
        }
    }
    // 2. a folha
    val dx = 3.dp.toPx() * press
    val dy = 4.dp.toPx() * press
    val f = 20.dp.toPx() * fold
    translate(dx, dy) {
        val page = pageShape(w, h, smallR, bigR, f)
        clipPath(page) {
            drawRect(pal.face)
            if (rail > 0f) drawRect(pal.strip, size = Size(rail, h))
            if (ruled) {
                val step = 12.dp.toPx()
                var y = step
                val line = 1.dp.toPx()
                while (y < h) { drawRect(pal.rule, Offset(rail, y), Size(w - rail, line)); y += step }
            }
            drawRect(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.24f), Color.Transparent), 0f, h * 0.5f), size = Size(w, h * 0.5f))
            if (sweep >= 0f) {
                val band = w * 0.28f
                val x = -band + (w + band * 2f) * sweep
                drawPath(Path().apply { moveTo(x, 0f); lineTo(x + band * 0.55f, 0f); lineTo(x + band * 0.15f, h); lineTo(x - band * 0.4f, h); close() }, Color.White.copy(alpha = 0.5f))
            }
            if (stamp >= 0f) {
                val r = maxOf(w, h) * (0.15f + 0.6f * stamp)
                drawCircle(pal.margin.copy(alpha = 0.55f * (1f - stamp)), r, Offset(w / 2f, h / 2f), style = Stroke(4.dp.toPx() * (1f - stamp) + 1f))
            }
            if (rail > 0f) drawRect(pal.margin, Offset(rail - 1.5.dp.toPx(), 0f), Size(3.dp.toPx(), h))
        }
        drawPath(page, pen, style = Stroke(stroke, join = StrokeJoin.Round))
        if (f > 0.5f) {
            val flap = Path().apply { moveTo(w - f, h); lineTo(w, h - f); lineTo(w - f, h - f); close() }
            drawPath(flap, pal.under)
            drawPath(flap, pen, style = Stroke(stroke * 0.8f, join = StrokeJoin.Round))
        }
        // os furos do fichário, quando não há ícone da ação
        if (holes > 0 && rail > 0f) {
            val r = minOf(5.5.dp.toPx(), h / 6f)
            val cx = rail / 2f - 1.dp.toPx()
            val ys = if (holes == 1) listOf(h / 2f) else listOf(h * 0.3f, h * 0.7f)
            for (cy in ys) {
                drawCircle(hole, r, Offset(cx, cy))
                clipRect(cx - r, cy - r, cx + r, cy) { drawCircle(Color.Black.copy(alpha = 0.18f), r, Offset(cx, cy + 1.5.dp.toPx())) }
                drawCircle(pen, r, Offset(cx, cy), style = Stroke(stroke))
            }
        }
    }
}

/** De qual ação é um botão do Material antigo, pelas cores que ele pediu. */
@Composable
private fun inferKind(colors: ButtonColors, fallback: NoteKind): NoteKind {
    val s = MaterialTheme.colorScheme
    return when {
        colors.containerColor == s.error || colors.contentColor == s.error -> NoteKind.Danger
        colors.containerColor == s.tertiary || colors.containerColor == s.tertiaryContainer -> NoteKind.Review
        colors.containerColor == s.secondary -> NoteKind.Practice
        else -> fallback
    }
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @Suppress("UNUSED_PARAMETER") shape: Shape = BrandButtonShape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    @Suppress("UNUSED_PARAMETER") elevation: ButtonElevation? = null,
    @Suppress("UNUSED_PARAMETER") border: BorderStroke? = null,
    @Suppress("UNUSED_PARAMETER") contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    kind: NoteKind? = null,
    glyph: Glyph? = null,
    size: NoteSize = NoteSize.Normal,
    content: @Composable RowScope.() -> Unit,
) {
    val k = kind ?: inferKind(colors, NoteKind.Primary)
    NoteSurface(k, size, enabled, glyph ?: k.glyph, onClick, modifier, null, interactionSource, null, content = content)
}

@Composable
fun FilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @Suppress("UNUSED_PARAMETER") shape: Shape = BrandButtonShape,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    @Suppress("UNUSED_PARAMETER") elevation: ButtonElevation? = null,
    @Suppress("UNUSED_PARAMETER") border: BorderStroke? = null,
    @Suppress("UNUSED_PARAMETER") contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    kind: NoteKind? = null,
    glyph: Glyph? = null,
    size: NoteSize = NoteSize.Normal,
    content: @Composable RowScope.() -> Unit,
) {
    val k = kind ?: inferKind(colors, NoteKind.Paper)
    NoteSurface(k, size, enabled, glyph ?: k.glyph, onClick, modifier, null, interactionSource, null, content = content)
}

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @Suppress("UNUSED_PARAMETER") shape: Shape = BrandButtonShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    @Suppress("UNUSED_PARAMETER") elevation: ButtonElevation? = null,
    @Suppress("UNUSED_PARAMETER") border: BorderStroke? = null,
    @Suppress("UNUSED_PARAMETER") contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    kind: NoteKind? = null,
    glyph: Glyph? = null,
    size: NoteSize = NoteSize.Normal,
    content: @Composable RowScope.() -> Unit,
) {
    val k = kind ?: inferKind(colors, NoteKind.Paper)
    NoteSurface(k, size, enabled, glyph ?: k.glyph, onClick, modifier, null, interactionSource, null, content = content)
}

/** O botão de texto: a folha pequena (um furo só, faixa estreita). */
@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @Suppress("UNUSED_PARAMETER") shape: Shape = BrandButtonShape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    @Suppress("UNUSED_PARAMETER") elevation: ButtonElevation? = null,
    @Suppress("UNUSED_PARAMETER") border: BorderStroke? = null,
    @Suppress("UNUSED_PARAMETER") contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource? = null,
    kind: NoteKind? = null,
    glyph: Glyph? = null,
    size: NoteSize = NoteSize.Small,
    content: @Composable RowScope.() -> Unit,
) {
    val k = kind ?: inferKind(colors, NoteKind.Paper)
    NoteSurface(k, size, enabled, glyph ?: k.glyph, onClick, modifier, null, interactionSource, null, content = content)
}

/**
 * O botão só de ícone (fechar, voltar, menu): quadradinho de papel com contorno de caneta e a folha
 * de baixo, mesmo movimento, sem pauta. Toque de 48 dp no mínimo.
 */
@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @Suppress("UNUSED_PARAMETER") colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    kind: NoteKind = NoteKind.Paper,
    content: @Composable () -> Unit,
) {
    val dark = isCadernoNight()
    val pal = notePalette(kind, dark, enabled)
    val pen = (if (dark) PenDark else PenLight).copy(alpha = if (enabled) 1f else 0.5f)
    val back = (if (dark) Color(0xFF3A3470) else PaperLight).copy(alpha = if (enabled) 1f else 0.6f)
    val reduce = rememberReducedMotion()
    val src = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    val press by animateFloatAsState(if (pressed && enabled) 1f else 0f, if (reduce) snap() else spring(0.5f, 700f), label = "note-icon-press")
    val stamp = remember { Animatable(-1f) }
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    Box(
        modifier
            // Mesmo quem pediu um botão menor ganha a folha inteira e o toque de 48 dp.
            .requiredSizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .clickable(src, indication = null, enabled = enabled, role = Role.Button) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                if (!reduce) scope.launch { stamp.snapTo(0f); stamp.animateTo(1f, tween(450)); stamp.snapTo(-1f) }
                onClick()
            }
            .padding(3.dp)
            .drawBehind {
                val r = 11.dp.toPx()
                drawNote(pal, pen, back, Color.Transparent, rail = 0f, holes = 0, press = press, fold = 0f, sweep = -1f,
                    stamp = stamp.value, ruled = false, small = r, big = r)
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier.padding(end = BackX, bottom = BackY).graphicsLayer { translationX = 3.dp.toPx() * press; translationY = 4.dp.toPx() * press },
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(LocalContentColor provides pal.ink) { content() }
        }
    }
}

// ---------------------------------------------------------------- previews

@Composable
private fun NoteGallery() {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        NoteKind.entries.forEach { k -> NoteButton(k, k.name, {}, size = NoteSize.Block) }
        NoteButton(NoteKind.Confirm, "Confirmar (desligado)", {}, size = NoteSize.Block, enabled = false)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NoteButton(NoteKind.Paper, "Cancelar", {}, size = NoteSize.Small)
            NoteButton(NoteKind.Next, "Próxima", {}, size = NoteSize.Small)
            IconButton({}) { BrandIcon(Glyph.Cross, size = 24.dp) }
        }
    }
}

@Preview(name = "Botões folha, claro", showBackground = true, backgroundColor = 0xFFF7F3E8, heightDp = 1300)
@Composable
private fun NoteButtonsLightPreview() { br.com.estudario.ui.theme.EstudarioTheme(darkTheme = false) { NoteGallery() } }

@Preview(name = "Botões folha, escuro", showBackground = true, backgroundColor = 0xFF14122B, heightDp = 1300)
@Composable
private fun NoteButtonsDarkPreview() { br.com.estudario.ui.theme.EstudarioTheme(darkTheme = true) { NoteGallery() } }

/**
 * Verdadeiro dentro de um botão folha que já mostra o desenho da ação na faixa: os ícones de dentro
 * (Icon/BrandIcon) não desenham nada, para não aparecer o mesmo desenho duas vezes.
 */
val LocalNoteRail = androidx.compose.runtime.staticCompositionLocalOf { false }
