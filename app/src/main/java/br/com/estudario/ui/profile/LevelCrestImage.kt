package br.com.estudario.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.LevelCrest
import com.caverock.androidsvg.SVG

/**
 * O brasão do nível (o mesmo desenho do site: LevelCrest, no módulo comum, gera o SVG e o AndroidSVG
 * desenha): a patente do título, as estrelas e o número. Até 48 dp sai sem os detalhes finos.
 */
@Composable
internal fun LevelCrestImage(level: Int, size: Dp, modifier: Modifier = Modifier) {
    val small = size <= 48.dp
    val svg = remember(level, small) { runCatching { SVG.getFromString(LevelCrest.svg(level, "lc$level", small)) }.getOrNull() }
    Canvas(modifier.size(size).semantics { contentDescription = "Nível $level, ${LevelCrest.rank(level).title}" }) {
        svg ?: return@Canvas
        svg.documentWidth = this.size.width
        svg.documentHeight = this.size.height
        svg.renderToCanvas(drawContext.canvas.nativeCanvas)
    }
}
