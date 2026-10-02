package br.com.estudario.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Figura de geometria ou de física: pontos com nome, segmentos com rótulo ("a = 3"), polígonos,
 * ângulos (reto vira o quadradinho), círculos e vetores com seta. Escala igual nos dois eixos,
 * para o triângulo 3-4-5 sair com a cara de 3-4-5.
 *
 * ```grafico
 * {"tipo":"geometria","titulo":"Teorema de Pitágoras",
 *  "pontos":[{"nome":"A","x":0,"y":0},{"nome":"B","x":4,"y":0},{"nome":"C","x":0,"y":3}],
 *  "poligonos":[["A","B","C"]],
 *  "segmentos":[{"de":"A","ate":"B","rotulo":"b = 4"},{"de":"A","ate":"C","rotulo":"c = 3"},{"de":"B","ate":"C","rotulo":"a = 5"}],
 *  "angulos":[{"vertice":"A","de":"B","ate":"C","reto":true}]}
 * ```
 */
data class GeometryFigure(
    val title: String?,
    val caption: String?,
    val points: Map<String, Pair<Double, Double>>,
    val showPointNames: Boolean,
    val polygons: List<List<String>>,
    val segments: List<Segment>,
    val vectors: List<Segment>,
    val angles: List<Angle>,
    val circles: List<Circle>,
) {
    data class Segment(val from: String, val to: String, val label: String?, val dashed: Boolean)
    data class Angle(val vertex: String, val from: String, val to: String, val label: String?, val right: Boolean)
    data class Circle(val center: String, val radius: Double, val label: String?)

    companion object {
        fun parse(root: JsonObject, title: String?, caption: String?): GeometryFigure? {
            val points = (root["pontos"] as? JsonArray)?.mapNotNull { element ->
                val obj = element as? JsonObject ?: return@mapNotNull null
                val name = obj.str("nome", "name") ?: return@mapNotNull null
                val x = obj.num("x") ?: return@mapNotNull null
                val y = obj.num("y") ?: return@mapNotNull null
                name to (x to y)
            }?.take(16)?.toMap().orEmpty()
            if (points.isEmpty()) return null
            fun segments(key: String) = (root[key] as? JsonArray)?.mapNotNull { element ->
                val obj = element as? JsonObject ?: return@mapNotNull null
                val from = obj.str("de", "from")?.takeIf { it in points } ?: return@mapNotNull null
                val to = obj.str("ate", "até", "to")?.takeIf { it in points } ?: return@mapNotNull null
                Segment(from, to, obj.str("rotulo", "label"), (obj["tracejado"] as? JsonPrimitive)?.booleanOrNull == true)
            }?.take(24).orEmpty()
            val polygons = (root["poligonos"] as? JsonArray)?.mapNotNull { element ->
                (element as? JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.content?.takeIf { name -> name in points } }?.takeIf { it.size >= 3 }
            }?.take(6).orEmpty()
            val angles = (root["angulos"] as? JsonArray)?.mapNotNull { element ->
                val obj = element as? JsonObject ?: return@mapNotNull null
                val vertex = obj.str("vertice", "vértice")?.takeIf { it in points } ?: return@mapNotNull null
                val from = obj.str("de")?.takeIf { it in points } ?: return@mapNotNull null
                val to = obj.str("ate", "até")?.takeIf { it in points } ?: return@mapNotNull null
                Angle(vertex, from, to, obj.str("rotulo", "label"), (obj["reto"] as? JsonPrimitive)?.booleanOrNull == true)
            }?.take(8).orEmpty()
            val circles = (root["circulos"] as? JsonArray)?.mapNotNull { element ->
                val obj = element as? JsonObject ?: return@mapNotNull null
                val center = obj.str("centro")?.takeIf { it in points } ?: return@mapNotNull null
                val radius = obj.num("raio")?.takeIf { it > 0 } ?: return@mapNotNull null
                Circle(center, radius, obj.str("rotulo", "label"))
            }?.take(4).orEmpty()
            val showNames = (root["nomesDosPontos"] as? JsonPrimitive)?.booleanOrNull ?: true
            val figure = GeometryFigure(title, caption, points, showNames, polygons, segments("segmentos"), segments("vetores"), angles, circles)
            return figure.takeIf { it.polygons.isNotEmpty() || it.segments.isNotEmpty() || it.vectors.isNotEmpty() || it.circles.isNotEmpty() }
        }

        private fun JsonObject.str(vararg keys: String) = keys.firstNotNullOfOrNull { (this[it] as? JsonPrimitive)?.content?.trim()?.takeIf(String::isNotEmpty) }
        private fun JsonObject.num(key: String) = (this[key] as? JsonPrimitive)?.let { it.doubleOrNull ?: it.content.replace(',', '.').toDoubleOrNull() }
    }
}

@Composable
internal fun GeometryCanvas(figure: GeometryFigure) {
    val colors = MaterialTheme.colorScheme
    val measurer = rememberTextMeasurer()
    val ink = colors.onSurface
    val accent = colors.primary
    val vectorTone = Color(0xFFE5484D)
    val labelStyle = TextStyle(color = ink, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic)
    val nameStyle = TextStyle(color = colors.onSurfaceVariant, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    // Proporção da figura: larga fica baixa, alta fica alta, dentro de um limite que caiba na tela.
    val xs = figure.points.values.map { it.first } + figure.circles.flatMap { c -> figure.points.getValue(c.center).first.let { listOf(it - c.radius, it + c.radius) } }
    val ys = figure.points.values.map { it.second } + figure.circles.flatMap { c -> figure.points.getValue(c.center).second.let { listOf(it - c.radius, it + c.radius) } }
    val spanX = (xs.max() - xs.min()).coerceAtLeast(1e-6)
    val spanY = (ys.max() - ys.min()).coerceAtLeast(1e-6)
    val ratio = (spanX / spanY).toFloat().coerceIn(0.8f, 2.2f)
    Canvas(Modifier.fillMaxWidth().aspectRatio(ratio)) {
        val pad = 34.dp.toPx()
        val scale = min((size.width - 2 * pad) / spanX, (size.height - 2 * pad) / spanY).toFloat()
        val offsetX = (size.width - spanX.toFloat() * scale) / 2
        val offsetY = (size.height - spanY.toFloat() * scale) / 2
        fun at(name: String): Offset = figure.points.getValue(name).let { (x, y) ->
            Offset(offsetX + ((x - xs.min()) * scale).toFloat(), size.height - offsetY - ((y - ys.min()) * scale).toFloat())
        }
        val centroid = figure.points.keys.map(::at).let { list -> Offset(list.sumOf { it.x.toDouble() }.toFloat() / list.size, list.sumOf { it.y.toDouble() }.toFloat() / list.size) }

        figure.polygons.forEach { polygon ->
            val path = Path().apply {
                polygon.forEachIndexed { i, name -> at(name).let { if (i == 0) moveTo(it.x, it.y) else lineTo(it.x, it.y) } }
                close()
            }
            drawPath(path, accent.copy(alpha = 0.08f))
            drawPath(path, accent, style = Stroke(2.5.dp.toPx()))
        }
        figure.circles.forEach { circle ->
            val center = at(circle.center)
            drawCircle(accent, (circle.radius * scale).toFloat(), center, style = Stroke(2.5.dp.toPx()))
            circle.label?.let { label ->
                val edge = center + Offset((circle.radius * scale).toFloat(), 0f)
                drawLine(ink, center, edge, 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f)))
                drawLabelNear(measurer, label, (center + edge) / 2f, Offset(0f, -1f), labelStyle)
            }
        }
        figure.segments.forEach { segment ->
            val a = at(segment.from); val b = at(segment.to)
            drawLine(ink, a, b, 2.dp.toPx(), pathEffect = if (segment.dashed) PathEffect.dashPathEffect(floatArrayOf(10f, 8f)) else null)
            segment.label?.let { drawLabelNear(measurer, it, (a + b) / 2f, outward((a + b) / 2f, a, b, centroid), labelStyle) }
        }
        figure.angles.forEach { angle -> drawAngle(at(angle.vertex), at(angle.from), at(angle.to), angle, measurer, labelStyle, accent) }
        figure.vectors.forEach { vector ->
            val a = at(vector.from); val b = at(vector.to)
            drawArrow(a, b, vectorTone)
            vector.label?.let { drawLabelNear(measurer, it, (a + b) / 2f, outward((a + b) / 2f, a, b, centroid), labelStyle.copy(color = vectorTone)) }
        }
        // Sem nomes (figura de física), os pontos são só apoio do desenho: nem bolinha aparece.
        if (figure.showPointNames) figure.points.keys.forEach { name ->
            val p = at(name)
            drawCircle(ink, 3.5.dp.toPx(), p)
            run {
                val away = (p - centroid).let { d -> val len = hypot(d.x, d.y); if (len < 1f) Offset(-1f, -1f) else d / len }
                drawLabelNear(measurer, name, p, away, nameStyle)
            }
        }
    }
}

/** Direção perpendicular ao segmento, para o lado de fora da figura. */
private fun outward(mid: Offset, a: Offset, b: Offset, centroid: Offset): Offset {
    val dx = b.x - a.x; val dy = b.y - a.y
    val len = hypot(dx, dy).coerceAtLeast(1f)
    var normal = Offset(-dy / len, dx / len)
    if ((mid + normal - centroid).getDistance() < (mid - centroid).getDistance()) normal = -normal
    return normal
}

private fun DrawScope.drawLabelNear(measurer: TextMeasurer, text: String, anchor: Offset, direction: Offset, style: TextStyle) {
    val layout = measurer.measure(text, style, softWrap = false, maxLines = 1)
    val gap = 12.dp.toPx()
    val center = anchor + direction * (gap + max(layout.size.width, layout.size.height) / 2f)
    val topLeft = Offset(
        (center.x - layout.size.width / 2f).coerceIn(0f, (size.width - layout.size.width).coerceAtLeast(0f)),
        (center.y - layout.size.height / 2f).coerceIn(0f, (size.height - layout.size.height).coerceAtLeast(0f)),
    )
    drawText(layout, topLeft = topLeft)
}

private fun DrawScope.drawArrow(a: Offset, b: Offset, color: Color) {
    drawLine(color, a, b, 3.dp.toPx())
    val angle = atan2(b.y - a.y, b.x - a.x)
    val head = 12.dp.toPx()
    val path = Path().apply {
        moveTo(b.x, b.y)
        lineTo(b.x - head * cos(angle - 0.45f), b.y - head * sin(angle - 0.45f))
        lineTo(b.x - head * cos(angle + 0.45f), b.y - head * sin(angle + 0.45f))
        close()
    }
    drawPath(path, color)
}

private fun DrawScope.drawAngle(v: Offset, p: Offset, q: Offset, angle: GeometryFigure.Angle, measurer: TextMeasurer, style: TextStyle, color: Color) {
    val u1 = (p - v).let { it / it.getDistance().coerceAtLeast(1f) }
    val u2 = (q - v).let { it / it.getDistance().coerceAtLeast(1f) }
    val r = 18.dp.toPx()
    if (angle.right) {
        // O quadradinho do ângulo reto.
        val path = Path().apply {
            moveTo(v.x + u1.x * r, v.y + u1.y * r)
            lineTo(v.x + (u1.x + u2.x) * r, v.y + (u1.y + u2.y) * r)
            lineTo(v.x + u2.x * r, v.y + u2.y * r)
        }
        drawPath(path, color, style = Stroke(2.dp.toPx()))
    } else {
        val start = Math.toDegrees(atan2(u1.y, u1.x).toDouble()).toFloat()
        var sweep = Math.toDegrees(atan2(u2.y, u2.x).toDouble()).toFloat() - start
        if (sweep > 180f) sweep -= 360f
        if (sweep < -180f) sweep += 360f
        drawArc(color, start, sweep, useCenter = false, topLeft = v - Offset(r, r), size = Size(2 * r, 2 * r), style = Stroke(2.dp.toPx()))
    }
    angle.label?.let { label ->
        val bisector = (u1 + u2).let { d -> val len = d.getDistance(); if (len < 1e-3f) Offset(0f, -1f) else d / len }
        drawLabelNear(measurer, label, v + bisector * r, bisector, style.copy(color = color, fontStyle = FontStyle.Normal))
    }
}
