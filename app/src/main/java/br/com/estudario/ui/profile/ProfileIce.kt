package br.com.estudario.ui.profile

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale

/** Gelo facetado ao redor da chama perdida: a personagem continua visível dentro dele. */
internal fun DrawScope.drawStreakIce(front: Boolean) {
    scale(size.minDimension / 100f, pivot = Offset.Zero) {
        val edge = Color(0xFF5998BE)
        val light = Color(0xFFECFAFF)
        val blue = Color(0xFFADDDF1)
        if (!front) {
            val shell = Path().apply {
                moveTo(29f, 14f); lineTo(68f, 12f); lineTo(86f, 28f)
                lineTo(89f, 70f); lineTo(74f, 90f); lineTo(27f, 92f)
                lineTo(12f, 73f); lineTo(14f, 33f); close()
            }
            drawPath(shell, light, style = Stroke(6f, join = StrokeJoin.Round))
            drawPath(shell, blue.copy(alpha = 0.55f))
            drawPath(shell, edge, style = Stroke(1.7f, join = StrokeJoin.Round))
            val topFacet = Path().apply {
                moveTo(29f, 14f); lineTo(68f, 12f); lineTo(86f, 28f)
                lineTo(63f, 25f); lineTo(34f, 29f); lineTo(14f, 33f); close()
            }
            drawPath(topFacet, light.copy(alpha = 0.85f))
            val rightFacet = Path().apply {
                moveTo(86f, 28f); lineTo(89f, 70f); lineTo(74f, 90f)
                lineTo(72f, 72f); lineTo(77f, 37f); close()
            }
            drawPath(rightFacet, Color(0xFF73B9D9).copy(alpha = 0.35f))
            val bottomFacet = Path().apply {
                moveTo(12f, 73f); lineTo(27f, 92f); lineTo(74f, 90f)
                lineTo(72f, 80f); lineTo(30f, 82f); close()
            }
            drawPath(bottomFacet, Color(0xFF8EC9E4).copy(alpha = 0.7f))
        } else {
            // Reflexos quebrados nas bordas, deixando o rosto livre no centro.
            val reflections = Path().apply {
                moveTo(24f, 32f); lineTo(32f, 22f); lineTo(44f, 21f)
                moveTo(19f, 43f); lineTo(18f, 64f)
                moveTo(78f, 40f); lineTo(80f, 51f)
                moveTo(33f, 86f); lineTo(48f, 86f)
            }
            drawPath(reflections, light, style = Stroke(2.7f, cap = StrokeCap.Round, join = StrokeJoin.Round))
            val cracks = Path().apply {
                moveTo(15f, 57f); lineTo(24f, 61f); lineTo(20f, 69f)
                moveTo(24f, 61f); lineTo(29f, 59f)
                moveTo(83f, 62f); lineTo(76f, 68f); lineTo(79f, 76f)
                moveTo(76f, 68f); lineTo(70f, 69f)
                moveTo(53f, 89f); lineTo(50f, 81f); lineTo(55f, 77f)
            }
            drawPath(cracks, edge.copy(alpha = 0.7f), style = Stroke(0.9f, cap = StrokeCap.Round, join = StrokeJoin.Round))
            val crystalLeft = Path().apply {
                moveTo(15f, 75f); lineTo(9f, 64f); lineTo(5f, 77f)
                lineTo(10f, 87f); lineTo(26f, 91f); close()
            }
            val crystalRight = Path().apply {
                moveTo(76f, 89f); lineTo(86f, 78f); lineTo(96f, 83f)
                lineTo(91f, 94f); close()
            }
            for (crystal in listOf(crystalLeft, crystalRight)) {
                drawPath(crystal, blue)
                drawPath(crystal, edge, style = Stroke(1.2f, join = StrokeJoin.Round))
            }
            drawPath(Path().apply {
                moveTo(9f, 69f); lineTo(11f, 79f); lineTo(21f, 87f)
                moveTo(87f, 83f); lineTo(84f, 88f); lineTo(90f, 91f)
            }, light, style = Stroke(1.4f, cap = StrokeCap.Round))
            val snow = Path().apply {
                moveTo(80f, 14f); lineTo(80f, 23f); moveTo(76f, 18.5f); lineTo(84f, 18.5f)
                moveTo(19f, 20f); lineTo(19f, 26f); moveTo(16f, 23f); lineTo(22f, 23f)
            }
            drawPath(snow, edge, style = Stroke(1.2f, cap = StrokeCap.Round))
        }
    }
}
