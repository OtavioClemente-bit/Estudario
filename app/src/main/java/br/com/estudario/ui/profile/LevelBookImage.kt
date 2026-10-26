package br.com.estudario.ui.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.LevelBook
import com.caverock.androidsvg.SVG
import kotlinx.coroutines.launch

/**
 * O livro de Folhas do nível (o mesmo desenho do site: LevelBook, no módulo comum, gera o SVG e o
 * AndroidSVG desenha). Até 40 dp mostra só a medalha; até 84 dp, a medalha simples com o número
 * maior. Tocando, ele abre e o Folha traz uma folha; [animateFrom] faz a subida de nível de verdade
 * (a tela de comemoração usa).
 */
@Composable
internal fun LevelBookImage(level: Int, size: Dp, modifier: Modifier = Modifier, animateFrom: Int? = null) {
    val mode = when { size <= 40.dp -> LevelBook.Mode.TINY; size <= 84.dp -> LevelBook.Mode.SMALL; else -> LevelBook.Mode.NORMAL }
    // O parado é lido uma vez por nível e tamanho; na animação, cada quadro é um SVG novo.
    val still = remember(level, mode) { runCatching { SVG.getFromString(LevelBook.svg(level, mode)) }.getOrNull() }
    var playing by remember(level) { mutableStateOf<Pair<Int, Int>?>(null) }
    val progress = remember(level) { Animatable(0f) }
    val scope = rememberCoroutineScope()
    suspend fun play(from: Int, to: Int) {
        playing = from to to
        progress.snapTo(0f)
        progress.animateTo(1f, tween(LevelBook.levelUpMillis(from, to).toInt(), easing = LinearEasing))
        playing = null
    }
    if (animateFrom != null && animateFrom != level) LaunchedEffect(level, animateFrom) { kotlinx.coroutines.delay(350); play(animateFrom, level) }
    val title = LevelBook.tiers[LevelBook.tierOf(level).coerceAtLeast(0)].title
    val tap = if (mode == LevelBook.Mode.TINY) Modifier else Modifier.clickable(remember { MutableInteractionSource() }, indication = null) {
        if (playing == null) scope.launch { play(level, level) }
    }
    Canvas(modifier.size(size).semantics { contentDescription = "Nível $level, $title" }.then(tap)) {
        val run = playing
        val svg = if (run == null) still else runCatching { SVG.getFromString(LevelBook.levelUpSvg(run.first, run.second, progress.value.toDouble(), mode)) }.getOrNull()
        svg ?: return@Canvas
        svg.documentWidth = this.size.width
        svg.documentHeight = this.size.height
        svg.renderToCanvas(drawContext.canvas.nativeCanvas)
    }
}
