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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
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
 * Os botões do Estudário (22/11, versão simples a pedido do dono: a folha de caderno poluía a tela).
 * Cantos arredondados, cor sólida da marca para a ação principal, contorno fino para as comuns,
 * vermelho só no perigo. Nada de faixa, carimbo, brilho ou animação de ícone: o botão não pode
 * disputar atenção com o estudo. Button, OutlinedButton, FilledTonalButton, TextButton e IconButton
 * daqui têm a mesma assinatura dos do Material; `kind` escolhe a cor da ação.
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
private val Green = Color(0xFF1F9D68)
private val Sun = Color(0xFFFFC94A)
private val Red = Color(0xFFD93F3F)
private val Ink = Color(0xFF26234A)

/**
 * As cores de um botão: fundo, texto e contorno. Sem enfeite: cor sólida para a ação principal,
 * contorno fino para as comuns.
 */
data class NotePalette(val face: Color, val ink: Color, val border: Color = Color.Transparent)

@Composable
private fun palette(kind: NoteKind, enabled: Boolean): NotePalette {
    val s = MaterialTheme.colorScheme
    val dark = isCadernoNight()
    if (!enabled) return NotePalette(s.onSurface.copy(alpha = 0.08f), s.onSurface.copy(alpha = 0.38f))
    val indigo = if (dark) IndigoNight else Indigo
    val line = s.onSurface.copy(alpha = 0.18f)
    return when (kind) {
        NoteKind.Confirm, NoteKind.Complete -> NotePalette(Green, Color.White)
        NoteKind.Subscribe -> NotePalette(Sun, Ink)
        NoteKind.Danger -> NotePalette(Color.Transparent, Red, Red.copy(alpha = 0.5f))
        NoteKind.Paper, NoteKind.Settings, NoteKind.Help, NoteKind.Notify -> NotePalette(Color.Transparent, s.onSurface, line)
        else -> NotePalette(indigo, Color.White)
    }
}

/** A tinta de caneta (a sombra dura dos recortes de cartão ainda usa). */
@Composable
fun cutInk(): Color = if (isCadernoNight()) PenDark else PenLight

val BrandButtonShape = RoundedCornerShape(12.dp)

private class NoteMetrics(val minHeight: Dp, val hPad: Dp, val fontSize: Float)

private fun metrics(size: NoteSize) = when (size) {
    NoteSize.Small -> NoteMetrics(40.dp, 14.dp, 14f)
    else -> NoteMetrics(48.dp, 20.dp, 15f)
}

/** Botão com texto. Uma linha só: encolhe um pouco antes de cortar com reticências. */
@Composable
fun NoteButton(
    kind: NoteKind,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @Suppress("UNUSED_PARAMETER") glyph: Glyph? = kind.glyph,
    size: NoteSize = NoteSize.Normal,
    enabled: Boolean = true,
    contentDescription: String? = null,
) {
    val m = metrics(size)
    NoteSurface(
        kind = kind, size = size, enabled = enabled, glyph = null, onClick = onClick,
        modifier = if (size == NoteSize.Block) modifier.fillMaxWidth() else modifier,
        contentDescription = contentDescription, interactionSource = null, rail = null,
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
    MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = m.fontSize.sp, letterSpacing = 0.1.sp)

/** O miolo comum: cantos arredondados, cor da ação, leve afundar no toque. */
@Composable
internal fun NoteSurface(
    kind: NoteKind,
    size: NoteSize,
    enabled: Boolean,
    @Suppress("UNUSED_PARAMETER") glyph: Glyph?,
    onClick: () -> Unit,
    modifier: Modifier,
    contentDescription: String?,
    interactionSource: MutableInteractionSource?,
    @Suppress("UNUSED_PARAMETER") rail: (@Composable () -> Unit)?,
    paletteOverride: NotePalette? = null,
    contentPadding: PaddingValues? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val m = metrics(size)
    val pal = if (enabled) paletteOverride ?: palette(kind, true) else palette(kind, false)
    val reduce = rememberReducedMotion()
    val src = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    val press by animateFloatAsState(if (pressed && enabled && !reduce) 1f else 0f, tween(90), label = "btn-press")
    val shade = if (pal.face == Color.Transparent) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f * press)
    else Color.Black.copy(alpha = 0.10f * press)
    Row(
        modifier
            .defaultMinSize(minWidth = 64.dp, minHeight = m.minHeight)
            .graphicsLayer { scaleX = 1f - 0.02f * press; scaleY = scaleX }
            .then(if (contentDescription != null) Modifier.semantics { this.contentDescription = contentDescription } else Modifier)
            .clip(BrandButtonShape)
            .background(pal.face)
            .background(shade)
            .then(if (pal.border != Color.Transparent) Modifier.border(1.dp, pal.border, BrandButtonShape) else Modifier)
            .clickable(interactionSource = src, indication = null, enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(contentPadding ?: PaddingValues(horizontal = m.hPad, vertical = 8.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides pal.ink) {
            ProvideTextStyle(noteTextStyle(m).copy(color = pal.ink)) { content() }
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

/** Botão só de ícone (fechar, voltar, menu): só o ícone, com um círculo leve no toque. 48 dp de toque. */
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
    val s = MaterialTheme.colorScheme
    val src = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    val tint = when {
        !enabled -> s.onSurface.copy(alpha = 0.38f)
        kind == NoteKind.Danger -> Red
        else -> LocalContentColor.current
    }
    Box(
        modifier
            .requiredSizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .clip(CircleShape)
            .background(if (pressed && enabled) s.onSurface.copy(alpha = 0.08f) else Color.Transparent)
            .clickable(src, indication = null, enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides tint) { content() }
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

@Preview(name = "Botões, claro", showBackground = true, backgroundColor = 0xFFF7F3E8, heightDp = 1300)
@Composable
private fun NoteButtonsLightPreview() { br.com.estudario.ui.theme.EstudarioTheme(darkTheme = false) { NoteGallery() } }

@Preview(name = "Botões, escuro", showBackground = true, backgroundColor = 0xFF14122B, heightDp = 1300)
@Composable
private fun NoteButtonsDarkPreview() { br.com.estudario.ui.theme.EstudarioTheme(darkTheme = true) { NoteGallery() } }
