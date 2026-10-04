package br.com.estudario.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Gráfico dentro do material de estudo. A IA escreve um bloco
 *
 * ```grafico
 * {"tipo": "pizza", "titulo": "...", "itens": [{"rotulo": "A", "valor": 40}]}
 * ```
 *
 * e o app desenha. Tipos: pizza, barras, linha (séries de pontos) e funcao (curvas de expressões
 * em x, para matemática). Bloco que não dá para ler vira nada: melhor sumir do que mostrar lixo.
 */
sealed interface StudyChart {
    val title: String?
    val caption: String?

    data class Item(val label: String, val value: Double)
    data class Series(val name: String?, val points: List<Pair<Double, Double>>)
    data class Curve(val name: String?, val expression: Expression)

    data class Pie(override val title: String?, override val caption: String?, val items: List<Item>, val unit: String?) : StudyChart
    data class Bars(override val title: String?, override val caption: String?, val items: List<Item>, val unit: String?) : StudyChart
    data class Lines(override val title: String?, override val caption: String?, val series: List<Series>, val xLabel: String?, val yLabel: String?) : StudyChart
    data class Function(
        override val title: String?,
        override val caption: String?,
        val curves: List<Curve>,
        val xMin: Double,
        val xMax: Double,
        val marks: List<Pair<String?, Pair<Double, Double>>>,
    ) : StudyChart

    /** Figura de geometria ou de física (triângulo com os lados, vetores...). Ver [GeometryFigure]. */
    data class Geometry(override val title: String?, override val caption: String?, val figure: GeometryFigure) : StudyChart

    companion object {
        const val FENCE = "grafico"
        private val json = Json { isLenient = true; ignoreUnknownKeys = true }

        fun parse(source: String): StudyChart? = runCatching {
            val root = json.parseToJsonElement(source.trim()) as? JsonObject ?: return null
            val type = root.text("tipo", "type")?.lowercase()?.let(::fold) ?: return null
            val title = root.text("titulo", "title")
            val caption = root.text("legenda", "fonte", "caption")
            when (type) {
                "pizza", "pie", "setores" -> items(root)?.let { Pie(title, caption, it, root.text("unidade", "unit")) }
                "barras", "barra", "bar", "bars", "colunas" -> items(root)?.let { Bars(title, caption, it, root.text("unidade", "unit")) }
                "linha", "linhas", "line" -> {
                    val series = (root["series"] as? JsonArray)?.mapNotNull { element ->
                        val obj = element as? JsonObject ?: return@mapNotNull null
                        val points = (obj["pontos"] ?: obj["points"]).pairs()
                        if (points.size < 2) null else Series(obj.text("nome", "name"), points)
                    }.orEmpty().take(4)
                    if (series.isEmpty()) null else Lines(title, caption, series, root.text("eixoX", "x"), root.text("eixoY", "y"))
                }
                "funcao", "function", "grafico" -> {
                    val curves = (root["funcoes"] ?: root["functions"]).let { it as? JsonArray }?.mapNotNull { element ->
                        val obj = element as? JsonObject
                        val expr = obj?.text("expr", "expressao") ?: (element as? JsonPrimitive)?.content
                        expr?.let { Expression.parse(it) }?.let { Curve(obj?.text("nome", "name") ?: expr, it) }
                    }.orEmpty().take(4)
                    val xMin = root.number("xmin") ?: -5.0
                    val xMax = root.number("xmax") ?: 5.0
                    val marks = (root["pontos"] as? JsonArray)?.mapNotNull { element ->
                        val obj = element as? JsonObject ?: return@mapNotNull null
                        val x = obj.number("x") ?: return@mapNotNull null
                        val y = obj.number("y") ?: return@mapNotNull null
                        obj.text("rotulo", "label") to (x to y)
                    }.orEmpty().take(8)
                    if (curves.isEmpty() || xMax <= xMin) null else Function(title, caption, curves, xMin, xMax, marks)
                }
                "geometria", "figura", "geometry", "fisica" -> GeometryFigure.parse(root, title, caption)?.let { Geometry(title, caption, it) }
                else -> null
            }
        }.getOrNull()

        private fun items(root: JsonObject): List<Item>? = (root["itens"] ?: root["items"] ?: root["dados"]).let { it as? JsonArray }?.mapNotNull { element ->
            val obj = element as? JsonObject ?: return@mapNotNull null
            val label = obj.text("rotulo", "label", "nome") ?: return@mapNotNull null
            val value = obj.number("valor") ?: obj.number("value") ?: return@mapNotNull null
            if (value.isNaN() || value < 0) null else Item(label, value)
        }?.take(12)?.takeIf { list -> list.size >= 2 && list.sumOf { it.value } > 0 }

        private fun JsonObject.text(vararg keys: String): String? = keys.firstNotNullOfOrNull { key ->
            (this[key] as? JsonPrimitive)?.takeIf { it.isString }?.content?.trim()?.takeIf(String::isNotEmpty)
        }

        private fun JsonObject.number(key: String): Double? = (this[key] as? JsonPrimitive)?.let { it.doubleOrNull ?: it.content.replace(',', '.').toDoubleOrNull() }

        private fun JsonElement?.pairs(): List<Pair<Double, Double>> = (this as? JsonArray)?.mapNotNull { point ->
            when (point) {
                is JsonArray -> {
                    val x = (point.getOrNull(0) as? JsonPrimitive)?.doubleOrNull
                    val y = (point.getOrNull(1) as? JsonPrimitive)?.doubleOrNull
                    if (x != null && y != null) x to y else null
                }
                is JsonObject -> point.number("x")?.let { x -> point.number("y")?.let { y -> x to y } }
                else -> null
            }
        }.orEmpty().sortedBy { it.first }

        private fun fold(value: String) = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD).replace(Regex("\\p{M}+"), "")
    }
}

/**
 * Expressão em x para o gráfico de função: + - * / ^, parênteses, multiplicação implícita (2x,
 * 3(x+1)), sen/sin, cos, tg/tan, ln, log (base 10), raiz/sqrt, abs, exp, pi e e.
 */
class Expression private constructor(private val root: Node) {
    fun at(x: Double): Double = root.eval(x)

    private sealed interface Node { fun eval(x: Double): Double }
    private class Num(val v: Double) : Node { override fun eval(x: Double) = v }
    private object X : Node { override fun eval(x: Double) = x }
    private class Unary(val f: (Double) -> Double, val a: Node) : Node { override fun eval(x: Double) = f(a.eval(x)) }
    private class Binary(val op: Char, val a: Node, val b: Node) : Node {
        override fun eval(x: Double): Double {
            val l = a.eval(x); val r = b.eval(x)
            return when (op) { '+' -> l + r; '-' -> l - r; '*' -> l * r; '/' -> l / r; else -> l.pow(r) }
        }
    }

    companion object {
        private val functions: Map<String, (Double) -> Double> = mapOf(
            "sen" to Math::sin, "sin" to Math::sin, "cos" to Math::cos, "tg" to Math::tan, "tan" to Math::tan,
            "ln" to Math::log, "log" to Math::log10, "raiz" to Math::sqrt, "sqrt" to Math::sqrt, "abs" to Math::abs, "exp" to Math::exp,
        )

        fun parse(text: String): Expression? = runCatching {
            val source = text.replace(" ", "").replace(',', '.').replace('·', '*').replace('×', '*').replace('−', '-').lowercase()
                .removePrefix("y=").removePrefix("f(x)=")
            val parser = Parser(source)
            val node = parser.sum()
            if (!parser.done()) null else Expression(node).takeIf { e -> (-3..3).any { e.at(it.toDouble()).isFinite() } }
        }.getOrNull()

        private class Parser(val s: String) {
            var i = 0
            fun done() = i >= s.length
            private fun peek() = s.getOrNull(i)

            fun sum(): Node {
                var node = product()
                while (peek() == '+' || peek() == '-') { val op = s[i++]; node = Binary(op, node, product()) }
                return node
            }

            fun product(): Node {
                var node = unary()
                while (true) {
                    val c = peek() ?: break
                    node = when {
                        c == '*' || c == '/' -> { i++; Binary(c, node, unary()) }
                        // Multiplicação implícita: 2x, 3(x+1), x sen(x).
                        c == '(' || c == 'x' || c.isLetter() || c.isDigit() -> Binary('*', node, unary())
                        else -> break
                    }
                }
                return node
            }

            fun unary(): Node = if (peek() == '-') { i++; Unary({ -it }, unary()) } else if (peek() == '+') { i++; unary() } else power()

            fun power(): Node {
                val base = atom()
                return if (peek() == '^') { i++; Binary('^', base, unary()) } else base
            }

            fun atom(): Node {
                val c = peek() ?: error("fim")
                if (c == '(') { i++; val inner = sum(); require(peek() == ')'); i++; return inner }
                if (c.isDigit() || c == '.') {
                    val start = i
                    while (peek()?.let { it.isDigit() || it == '.' } == true) i++
                    return Num(s.substring(start, i).toDouble())
                }
                if (c.isLetter()) {
                    val name = functions.keys.sortedByDescending { it.length }.firstOrNull { s.startsWith(it, i) }
                    if (name != null && s.getOrNull(i + name.length) == '(') { i += name.length; return Unary(functions.getValue(name), atom()) }
                    if (s.startsWith("pi", i)) { i += 2; return Num(PI) }
                    if (c == 'x') { i++; return X }
                    if (c == 'e') { i++; return Num(Math.E) }
                }
                error("símbolo inesperado em $i")
            }
        }
    }
}

/** Separa o Markdown em trechos de texto e blocos ```grafico, na ordem. */
/**
 * Nomes de bloco que viram figura. O pedido manda `grafico`, mas o modelo às vezes usa o próprio
 * tipo (`geometria`, `funcao`...) e aí o app mostrava o código em vez do desenho.
 */
private val chartFences = setOf(StudyChart.FENCE, "gráfico", "geometria", "figura", "funcao", "função", "pizza", "barras", "linha", "chart")

private fun isChartFence(trimmed: String): Boolean =
    trimmed.startsWith("```") && trimmed.removePrefix("```").trim().lowercase() in chartFences

internal fun splitCharts(markdown: String): List<Pair<String, StudyChart?>> {
    if (markdown.lines().none { isChartFence(it.trim()) }) return listOf(markdown to null)
    val parts = mutableListOf<Pair<String, StudyChart?>>()
    val text = StringBuilder()
    val chart = StringBuilder()
    var inChart = false
    markdown.lines().forEach { line ->
        val trimmed = line.trim()
        when {
            !inChart && isChartFence(trimmed) -> {
                if (text.isNotBlank()) parts += text.toString().trim('\n') to null
                text.clear(); inChart = true
            }
            inChart && trimmed.startsWith("```") -> {
                StudyChart.parse(chart.toString())?.let { parts += "" to it }
                chart.clear(); inChart = false
            }
            inChart -> chart.append(line).append('\n')
            else -> text.append(line).append('\n')
        }
    }
    if (text.isNotBlank()) parts += text.toString().trim('\n') to null
    return parts
}

private fun palette(primary: Color, secondary: Color, tertiary: Color) = listOf(
    primary, Color(0xFF2EA56F), Color(0xFFF0A202), Color(0xFFE5484D), tertiary, Color(0xFF3FA7D6), secondary, Color(0xFF9B5DE5),
    Color(0xFF8D6E63), Color(0xFF00897B), Color(0xFFD81B60), Color(0xFF7CB342),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudyChartView(chart: StudyChart, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val tones = palette(colors.primary, colors.secondary, colors.tertiary)
    val measurer = rememberTextMeasurer()
    val labelStyle = TextStyle(color = colors.onSurfaceVariant, fontSize = 11.sp)
    Surface(modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(18.dp), color = colors.surfaceContainerLow) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            chart.title?.let { Text(it, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold) }
            when (chart) {
                is StudyChart.Pie -> PieChart(chart, tones)
                is StudyChart.Bars -> Canvas(Modifier.fillMaxWidth().height((chart.items.size * 34 + 8).dp)) { drawBars(chart, tones, measurer, labelStyle, colors.onSurface) }
                is StudyChart.Lines -> {
                    Canvas(Modifier.fillMaxWidth().aspectRatio(1.5f)) {
                        val all = chart.series.flatMap { it.points }
                        drawPlot(all.minOf { it.first }, all.maxOf { it.first }, all.minOf { it.second }, all.maxOf { it.second }, measurer, labelStyle, colors.outlineVariant, colors.onSurfaceVariant, chart.xLabel, chart.yLabel) { toX, toY ->
                            chart.series.forEachIndexed { index, series ->
                                val path = Path()
                                series.points.forEachIndexed { i, (x, y) -> if (i == 0) path.moveTo(toX(x), toY(y)) else path.lineTo(toX(x), toY(y)) }
                                drawPath(path, tones[index % tones.size], style = Stroke(3.dp.toPx()))
                                series.points.forEach { (x, y) -> drawCircle(tones[index % tones.size], 3.5.dp.toPx(), Offset(toX(x), toY(y))) }
                            }
                        }
                    }
                    if (chart.series.size > 1 || chart.series.first().name != null) Legend(chart.series.mapIndexed { i, s -> (s.name ?: "Série ${i + 1}") to tones[i % tones.size] })
                }
                is StudyChart.Function -> {
                    Canvas(Modifier.fillMaxWidth().aspectRatio(1.25f)) {
                        val samples = chart.curves.map { curve ->
                            (0..240).map { step -> val x = chart.xMin + (chart.xMax - chart.xMin) * step / 240.0; x to curve.expression.at(x) }
                        }
                        val ys = samples.flatten().map { it.second }.filter { it.isFinite() }.sorted()
                        // Corta extremos (assíntotas) para a curva caber: usa 5% a 95% dos valores.
                        val lo = ys.getOrElse((ys.size * 0.05).toInt()) { -1.0 }
                        val hi = ys.getOrElse((ys.size * 0.95).toInt().coerceAtMost(ys.lastIndex)) { 1.0 }
                        val marksY = chart.marks.map { it.second.second }
                        val yMin = min(min(lo, 0.0), marksY.minOrNull() ?: lo)
                        val yMax = max(max(hi, 0.0), marksY.maxOrNull() ?: hi)
                        drawPlot(chart.xMin, chart.xMax, yMin, yMax, measurer, labelStyle, colors.outlineVariant, colors.onSurfaceVariant, "x", "y", axesAtZero = true) { toX, toY ->
                            samples.forEachIndexed { index, points ->
                                val path = Path()
                                var pen = false
                                points.forEach { (x, y) ->
                                    val visible = y.isFinite() && y >= yMin - (yMax - yMin) && y <= yMax + (yMax - yMin)
                                    if (!visible) { pen = false; return@forEach }
                                    if (pen) path.lineTo(toX(x), toY(y)) else path.moveTo(toX(x), toY(y))
                                    pen = true
                                }
                                drawPath(path, tones[index % tones.size], style = Stroke(3.dp.toPx()))
                            }
                            chart.marks.forEach { (label, point) ->
                                val at = Offset(toX(point.first), toY(point.second))
                                drawCircle(colors.surface, 6.dp.toPx(), at)
                                drawCircle(colors.onSurface, 4.dp.toPx(), at)
                                label?.let {
                                    val text = measurer.measure(it, labelStyle.copy(color = colors.onSurface, fontWeight = FontWeight.Bold), softWrap = false, maxLines = 1)
                                    // Perto da borda direita o rótulo vai para a esquerda do ponto, sem quebrar letra por letra.
                                    val x = if (at.x + 8.dp.toPx() + text.size.width > size.width) at.x - 8.dp.toPx() - text.size.width else at.x + 8.dp.toPx()
                                    // Fundo atrás do rótulo: a curva passa por perto do ponto e cobria o texto ("V(2, -4)").
                                    val topLeft = Offset(x.coerceAtLeast(0f), (at.y - text.size.height - 6.dp.toPx()).coerceAtLeast(0f))
                                    val pad = 3.dp.toPx()
                                    drawRoundRect(colors.surfaceContainerHigh, topLeft - Offset(pad, pad / 2), Size(text.size.width + 2 * pad, text.size.height + pad), androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
                                    drawText(text, topLeft = topLeft)
                                }
                            }
                        }
                    }
                    Legend(chart.curves.mapIndexed { i, c -> (c.name ?: "f${i + 1}") to tones[i % tones.size] })
                }
                is StudyChart.Geometry -> GeometryCanvas(chart.figure)
            }
            chart.caption?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PieChart(chart: StudyChart.Pie, tones: List<Color>) {
    val total = chart.items.sumOf { it.value }
    val surface = MaterialTheme.colorScheme.surfaceContainerLow
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Canvas(Modifier.size(140.dp)) {
            var start = -90f
            chart.items.forEachIndexed { index, item ->
                val sweep = (item.value / total * 360).toFloat()
                drawArc(tones[index % tones.size], start, sweep, useCenter = true, size = Size(size.width, size.height))
                drawArc(surface, start, sweep, useCenter = true, style = Stroke(2.dp.toPx()), size = Size(size.width, size.height))
                start += sweep
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            chart.items.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).clip(CircleShape).background(tones[index % tones.size]))
                    Text(
                        "  ${item.label}",
                        Modifier.weight(1f),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        "${formatNumber(item.value / total * 100)}%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Legend(entries: List<Pair<String, Color>>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        entries.forEach { (label, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.width(14.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(color))
                Text("  $label", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

private fun DrawScope.drawBars(chart: StudyChart.Bars, tones: List<Color>, measurer: TextMeasurer, style: TextStyle, valueColor: Color) {
    val labelWidth = size.width * 0.34f
    val maxValue = chart.items.maxOf { it.value }.takeIf { it > 0 } ?: 1.0
    val row = 34.dp.toPx()
    chart.items.forEachIndexed { index, item ->
        val top = index * row + 4.dp.toPx()
        val label = measurer.measure(item.label, style, maxLines = 2, softWrap = true, constraints = androidx.compose.ui.unit.Constraints(maxWidth = (labelWidth - 8.dp.toPx()).toInt()))
        drawText(label, topLeft = Offset(0f, top + (row - 8.dp.toPx() - label.size.height) / 2))
        val valueText = formatNumber(item.value) + (chart.unit?.let { if (it == "%") it else " $it" } ?: "")
        val value = measurer.measure(valueText, style.copy(color = valueColor, fontWeight = FontWeight.Bold))
        val room = size.width - labelWidth - value.size.width - 8.dp.toPx()
        val width = (room * (item.value / maxValue)).toFloat().coerceAtLeast(3.dp.toPx())
        drawRoundRect(tones[index % tones.size], Offset(labelWidth, top + 3.dp.toPx()), Size(width, row - 14.dp.toPx()), androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()))
        drawText(value, topLeft = Offset(labelWidth + width + 6.dp.toPx(), top + (row - 8.dp.toPx() - value.size.height) / 2))
    }
}

/** Eixos, grade e marcas com números "redondos"; [content] desenha em coordenadas do gráfico. */
private fun DrawScope.drawPlot(
    xMinRaw: Double, xMaxRaw: Double, yMinRaw: Double, yMaxRaw: Double,
    measurer: TextMeasurer, style: TextStyle, grid: Color, axis: Color,
    xLabel: String?, yLabel: String?, axesAtZero: Boolean = false,
    content: DrawScope.(toX: (Double) -> Float, toY: (Double) -> Float) -> Unit,
) {
    val (xMin, xMax) = if (xMaxRaw > xMinRaw) xMinRaw to xMaxRaw else (xMinRaw - 1) to (xMinRaw + 1)
    val pad = (yMaxRaw - yMinRaw).takeIf { it > 0 }?.let { it * 0.08 } ?: 1.0
    val yMin = yMinRaw - pad; val yMax = yMaxRaw + pad
    val left = 34.dp.toPx(); val bottom = size.height - 22.dp.toPx(); val top = 8.dp.toPx(); val right = size.width - 8.dp.toPx()
    val toX = { x: Double -> (left + (x - xMin) / (xMax - xMin) * (right - left)).toFloat() }
    val toY = { y: Double -> (bottom - (y - yMin) / (yMax - yMin) * (bottom - top)).toFloat() }
    val dashed = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
    niceTicks(yMin, yMax).forEach { y ->
        drawLine(grid, Offset(left, toY(y)), Offset(right, toY(y)), 1.dp.toPx(), pathEffect = dashed)
        val t = measurer.measure(formatNumber(y), style)
        drawText(t, topLeft = Offset(left - t.size.width - 4.dp.toPx(), toY(y) - t.size.height / 2))
    }
    niceTicks(xMin, xMax).forEach { x ->
        drawLine(grid, Offset(toX(x), top), Offset(toX(x), bottom), 1.dp.toPx(), pathEffect = dashed)
        val t = measurer.measure(formatNumber(x), style)
        drawText(t, topLeft = Offset(toX(x) - t.size.width / 2, bottom + 4.dp.toPx()))
    }
    val axisY = if (axesAtZero && 0.0 in yMin..yMax) toY(0.0) else bottom
    val axisX = if (axesAtZero && 0.0 in xMin..xMax) toX(0.0) else left
    drawLine(axis, Offset(left, axisY), Offset(right, axisY), 1.5.dp.toPx())
    drawLine(axis, Offset(axisX, top), Offset(axisX, bottom), 1.5.dp.toPx())
    xLabel?.let { drawText(measurer, it, Offset(right - measurer.measure(it, style).size.width, axisY - 16.dp.toPx()), style = style) }
    yLabel?.let { drawText(measurer, it, Offset(axisX + 4.dp.toPx(), top), style = style) }
    content(toX, toY)
}

internal fun niceTicks(min: Double, max: Double, target: Int = 5): List<Double> {
    val span = max - min
    if (span <= 0 || !span.isFinite()) return emptyList()
    val raw = span / target
    val magnitude = 10.0.pow(floor(log10(raw)))
    val step = listOf(1.0, 2.0, 2.5, 5.0, 10.0).map { it * magnitude }.first { it >= raw }
    val first = ceil(min / step) * step
    return generateSequence(first) { it + step }.takeWhile { it <= max + step * 1e-9 }.map { if (abs(it) < step * 1e-9) 0.0 else it }.toList()
}

internal fun formatNumber(value: Double): String {
    if (value == floor(value) && abs(value) < 1e9) return "%,d".format(java.util.Locale("pt", "BR"), value.toLong())
    val text = "%.2f".format(java.util.Locale("pt", "BR"), value).trimEnd('0').trimEnd(',')
    return text
}
