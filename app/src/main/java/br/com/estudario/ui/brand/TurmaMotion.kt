package br.com.estudario.ui.brand

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import android.provider.Settings
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sin
import kotlinx.coroutines.launch

/**
 * A turma do Folha se mexe quando a pessoa toca no botão, item ou cartão que tem o personagem - o
 * mesmo movimento do site ao passar o mouse (chama tremula, troféu comemora, sino toca...).
 *
 * A raiz do app ([turmaTouchRoot]) ouve os toques sem consumi-los; cada personagem guarda onde está na
 * tela, e o toque anima o personagem mais próximo na mesma linha. Quem desligou as animações do
 * sistema (escala de animação 0) vê os personagens parados.
 */
internal object TurmaPress {
    private val slots = HashMap<Any, Rect>()
    var event by mutableStateOf<Pair<Any, Int>?>(null)
        private set
    private var serial = 0

    fun place(key: Any, rect: Rect) { slots[key] = rect }
    fun remove(key: Any) { slots.remove(key) }

    /** [band]: meia altura extra da linha; [reach]: quanto à direita do personagem o toque ainda conta. */
    fun press(p: Offset, band: Float, reach: Float) {
        var best: Any? = null
        var bestScore = Float.MAX_VALUE
        for ((key, r) in slots) {
            if (r.width <= 0f) continue
            val dy = abs(p.y - r.center.y)
            if (dy > max(r.height / 2f + band, band * 1.6f)) continue
            if (p.x < r.left - band || p.x > r.right + reach) continue
            val dx = if (p.x in r.left..r.right) 0f else minOf(abs(p.x - r.left), abs(p.x - r.right))
            val score = dy * 3f + dx
            if (score < bestScore) { bestScore = score; best = key }
        }
        if (best != null) event = best to ++serial
    }
}

/** Na raiz do app: ouve os toques (sem consumir) e avisa o personagem mais próximo. */
fun Modifier.turmaTouchRoot(): Modifier = composed {
    val density = LocalDensity.current
    val band = with(density) { 22.dp.toPx() }
    val reach = with(density) { 340.dp.toPx() }
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val e = awaitPointerEvent(PointerEventPass.Initial)
                if (e.type == PointerEventType.Press) e.changes.firstOrNull()?.let { TurmaPress.press(it.position, band, reach) }
            }
        }
    }
}

/** Liga o personagem ao toque. [directTouch] anima o próprio desenho, sem acionar o cartão pai. */
internal fun Modifier.turmaMotion(
    glyph: Glyph,
    enabled: Boolean = true,
    directTouch: Boolean = false,
    onClickLabel: String? = null,
    animation: String? = null,
): Modifier = composed {
    if (!enabled) return@composed this
    val key = remember { Any() }
    val view = LocalView.current
    fun motionOn() =
        runCatching { Settings.Global.getFloat(view.context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) }.getOrDefault(1f) > 0f
    val anim = animation ?: remember(glyph) { Turma.art(glyph)?.anim ?: "hop" }
    val t = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val duration = if (anim == "spin" || anim == "flip" || anim == "roll") 760 else 640
    if (!directTouch) {
        DisposableEffect(key) { onDispose { TurmaPress.remove(key) } }
        LaunchedEffect(key, anim) {
            var last: Int? = null
            snapshotFlow { TurmaPress.event }.collect { ev ->
                if (ev == null || ev.first !== key || ev.second == last) return@collect
                last = ev.second
                if (!motionOn()) return@collect
                t.snapTo(0f)
                t.animateTo(1f, tween(duration, easing = LinearEasing))
            }
        }
    }
    val touch = if (directTouch) Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() }, indication = null,
        role = Role.Button, onClickLabel = onClickLabel,
    ) {
        if (motionOn()) scope.launch {
            t.snapTo(0f)
            t.animateTo(1f, tween(duration, easing = LinearEasing))
        }
    } else Modifier
    this
        .then(touch)
        .then(if (directTouch) Modifier.semantics { contentDescription = onClickLabel ?: glyph.name } else Modifier)
        .onGloballyPositioned { if (!directTouch) TurmaPress.place(key, it.boundsInWindow()) }
        .graphicsLayer {
            val m = motionOf(anim, t.value)
            val side = size.minDimension
            translationX = m.dx * side
            translationY = m.dy * side
            rotationZ = m.rot
            scaleX = m.sx
            scaleY = m.sy
            transformOrigin = when (anim) {
                "spin", "pulse", "roll", "flip" -> TransformOrigin(0.5f, 0.5f)
                "ring", "sway" -> TransformOrigin(0.5f, 0.08f)
                else -> TransformOrigin(0.5f, 0.92f)
            }
        }
}

private class Motion(val dx: Float = 0f, val dy: Float = 0f, val rot: Float = 0f, val sx: Float = 1f, val sy: Float = 1f)

/** Os mesmos movimentos do turma.js do site, como funções do tempo (0 → 1). */
private fun motionOf(anim: String, t: Float): Motion {
    if (t >= 1f) return Motion()
    val fade = 1f - t
    fun s(k: Float, phase: Float = 0f) = sin((k * PI * t + phase).toFloat())
    fun backOut(x: Float): Float { val c = 1.7f; val y = x - 1f; return 1f + (c + 1f) * y * y * y + c * y * y }
    return when (anim) {
        "wiggle" -> Motion(rot = 11f * s(4f) * fade)
        "spin" -> Motion(rot = 360f * backOut(t), sx = 1f + 0.08f * s(1f), sy = 1f + 0.08f * s(1f))
        "flip" -> Motion(rot = 360f * backOut(t), sx = 1f - 0.08f * s(1f), sy = 1f - 0.08f * s(1f))
        "flicker" -> Motion(sx = 1f - 0.06f * s(6f) * fade, sy = 1f + 0.12f * s(6f) * fade, rot = 3f * s(4f, 1f) * fade)
        "cheer" -> Motion(dy = -0.16f * s(1f), rot = 9f * s(4f) * fade)
        "pulse" -> { val k = 1f + 0.16f * abs(s(3f)) * fade; Motion(sx = k, sy = k) }
        "zap" -> Motion(dx = 0.04f * s(10f) * fade, dy = -0.02f * s(8f) * fade, rot = 7f * s(10f) * fade, sx = 1f + 0.06f * s(1f), sy = 1f + 0.06f * s(1f))
        "sway" -> Motion(rot = 10f * s(3f) * fade)
        "float" -> Motion(dy = -0.10f * s(1f))
        "write" -> Motion(dx = 0.05f * s(6f) * fade, dy = 0.04f * s(6f, 1f) * fade, rot = -5f * s(3f) * fade)
        "ring" -> Motion(rot = 16f * s(6f) * fade)
        "roll" -> Motion(rot = 360f * backOut(t), dy = -0.07f * s(1f))
        "peek" -> Motion(dy = -0.11f * s(1f), rot = -5f * s(2f) * fade)
        else -> { // hop: pulo com amassadinho na volta
            val up = if (t < 0.6f) sin(PI.toFloat() * t / 0.6f) else 0f
            val squash = if (t >= 0.6f) sin(PI.toFloat() * (t - 0.6f) / 0.4f) else 0f
            Motion(dy = -0.16f * up, sx = 1f + 0.07f * squash, sy = 1f - 0.08f * squash)
        }
    }
}
