package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import kotlinx.browser.document
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.Element
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

/**
 * Gráficos e figuras do material (o mesmo bloco ```grafico que o app desenha, ver StudyChart.kt e
 * StudyGeometry.kt no Android): pizza, barras, linha, funcao e geometria, em SVG. Os rótulos entram
 * como textContent, nunca como HTML. Bloco que não dá para ler vira nada.
 */
object WebChart {
    /** Nomes de bloco que viram figura (o modelo às vezes usa o próprio tipo no lugar de "grafico"). */
    val fences = setOf("grafico", "gráfico", "geometria", "figura", "funcao", "função", "pizza", "barras", "linha", "chart")

    private val json = Json { isLenient = true; ignoreUnknownKeys = true }

    sealed interface Chart { val title: String?; val caption: String? }
    data class Item(val label: String, val value: Double)
    data class Pie(override val title: String?, override val caption: String?, val items: List<Item>) : Chart
    data class Bars(override val title: String?, override val caption: String?, val items: List<Item>, val unit: String?) : Chart
    data class Series(val name: String?, val points: List<Pair<Double, Double>>)
    data class Lines(override val title: String?, override val caption: String?, val series: List<Series>, val xLabel: String?, val yLabel: String?) : Chart
    data class Curve(val name: String, val expr: Expr)
    data class Function(override val title: String?, override val caption: String?, val curves: List<Curve>, val xMin: Double, val xMax: Double, val marks: List<Pair<String?, Pair<Double, Double>>>) : Chart
    data class Segment(val from: String, val to: String, val label: String?, val dashed: Boolean)
    data class Angle(val vertex: String, val from: String, val to: String, val label: String?, val right: Boolean)
    data class Circle(val center: String, val radius: Double, val label: String?)
    data class Geometry(
        override val title: String?, override val caption: String?,
        val points: Map<String, Pair<Double, Double>>, val showNames: Boolean, val polygons: List<List<String>>,
        val segments: List<Segment>, val vectors: List<Segment>, val angles: List<Angle>, val circles: List<Circle>,
    ) : Chart

    fun parse(source: String): Chart? = runCatching {
        val root = json.parseToJsonElement(source.trim()) as? JsonObject ?: return null
        val type = fold(root.text("tipo", "type")?.lowercase() ?: return null)
        val title = root.text("titulo", "title")
        val caption = root.text("legenda", "fonte", "caption")
        when (type) {
            "pizza", "pie", "setores" -> items(root, false)?.let { Pie(title, caption, it) }
            "barras", "barra", "bar", "bars", "colunas" -> items(root, true)?.let { Bars(title, caption, it, root.text("unidade", "unit")) }
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
                    val text = obj?.text("expr", "expressao") ?: (element as? JsonPrimitive)?.content
                    text?.let { Expr.parse(it) }?.let { Curve(obj?.text("nome", "name") ?: text, it) }
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
            "geometria", "figura", "geometry", "fisica" -> geometry(root, title, caption)
            else -> null
        }
    }.getOrNull()

    private fun geometry(root: JsonObject, title: String?, caption: String?): Geometry? {
        val points = (root["pontos"] as? JsonArray)?.mapNotNull { element ->
            val obj = element as? JsonObject ?: return@mapNotNull null
            val name = obj.text("nome", "name") ?: return@mapNotNull null
            val x = obj.number("x") ?: return@mapNotNull null
            val y = obj.number("y") ?: return@mapNotNull null
            name to (x to y)
        }?.take(16)?.toMap().orEmpty()
        if (points.isEmpty()) return null
        fun segments(key: String) = (root[key] as? JsonArray)?.mapNotNull { element ->
            val obj = element as? JsonObject ?: return@mapNotNull null
            val from = obj.text("de", "from")?.takeIf { it in points } ?: return@mapNotNull null
            val to = obj.text("ate", "até", "to")?.takeIf { it in points } ?: return@mapNotNull null
            Segment(from, to, obj.text("rotulo", "label"), (obj["tracejado"] as? JsonPrimitive)?.booleanOrNull == true)
        }?.take(24).orEmpty()
        val polygons = (root["poligonos"] as? JsonArray)?.mapNotNull { element ->
            (element as? JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.content?.takeIf { name -> name in points } }?.takeIf { it.size >= 3 }
        }?.take(6).orEmpty()
        val angles = (root["angulos"] as? JsonArray)?.mapNotNull { element ->
            val obj = element as? JsonObject ?: return@mapNotNull null
            val vertex = obj.text("vertice", "vértice")?.takeIf { it in points } ?: return@mapNotNull null
            val from = obj.text("de")?.takeIf { it in points } ?: return@mapNotNull null
            val to = obj.text("ate", "até")?.takeIf { it in points } ?: return@mapNotNull null
            Angle(vertex, from, to, obj.text("rotulo", "label"), (obj["reto"] as? JsonPrimitive)?.booleanOrNull == true)
        }?.take(8).orEmpty()
        val circles = (root["circulos"] as? JsonArray)?.mapNotNull { element ->
            val obj = element as? JsonObject ?: return@mapNotNull null
            val center = obj.text("centro")?.takeIf { it in points } ?: return@mapNotNull null
            val radius = obj.number("raio")?.takeIf { it > 0 } ?: return@mapNotNull null
            Circle(center, radius, obj.text("rotulo", "label"))
        }?.take(4).orEmpty()
        val showNames = (root["nomesDosPontos"] as? JsonPrimitive)?.booleanOrNull ?: true
        val figure = Geometry(title, caption, points, showNames, polygons, segments("segmentos"), segments("vetores"), angles, circles)
        return figure.takeIf { it.polygons.isNotEmpty() || it.segments.isNotEmpty() || it.vectors.isNotEmpty() || it.circles.isNotEmpty() }
    }

    private fun items(root: JsonObject, allowNegative: Boolean): List<Item>? = (root["itens"] ?: root["items"] ?: root["dados"]).let { it as? JsonArray }?.mapNotNull { element ->
        val obj = element as? JsonObject ?: return@mapNotNull null
        val label = obj.text("rotulo", "label", "nome") ?: return@mapNotNull null
        val value = obj.number("valor") ?: obj.number("value") ?: return@mapNotNull null
        if (value.isNaN() || (value < 0 && !allowNegative)) null else Item(label, value)
    }?.take(12)?.takeIf { list -> list.size >= 2 && list.sumOf { abs(it.value) } > 0 }

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

    private fun fold(value: String): String = (value.asDynamic().normalize("NFD") as String).replace(Regex("[\\u0300-\\u036f]+"), "")

    /** Expressão em x (mesma gramática do app): + - * / ^, parênteses, 2x, sen, cos, tg, ln, log, raiz, abs, exp, pi, e. */
    class Expr private constructor(private val root: Node) {
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
                "sen" to ::sin, "sin" to ::sin, "cos" to ::cos, "tg" to { v -> kotlin.math.tan(v) }, "tan" to { v -> kotlin.math.tan(v) },
                "ln" to { v -> kotlin.math.ln(v) }, "log" to { v -> log10(v) }, "raiz" to { v -> kotlin.math.sqrt(v) }, "sqrt" to { v -> kotlin.math.sqrt(v) },
                "abs" to { v -> abs(v) }, "exp" to { v -> kotlin.math.exp(v) },
            )

            fun parse(text: String): Expr? = runCatching {
                val source = text.replace(" ", "").replace(',', '.').replace('·', '*').replace('×', '*').replace('−', '-').lowercase()
                    .removePrefix("y=").removePrefix("f(x)=")
                val parser = Parser(source)
                val node = parser.sum()
                if (!parser.done()) null else Expr(node).takeIf { e -> (-3..3).any { e.at(it.toDouble()).isFinite() } }
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
                        if (c == 'e') { i++; return Num(kotlin.math.E) }
                    }
                    error("símbolo inesperado em $i")
                }
            }
        }
    }

    // ---------- Desenho em SVG ----------

    private const val NS = "http://www.w3.org/2000/svg"
    private val tones = listOf("var(--primary)", "#2EA56F", "#F0A202", "#E5484D", "#3FA7D6", "#9B5DE5", "#8D6E63", "#00897B", "#D81B60", "#7CB342", "#5C6BC0", "#FF7043")
    private const val VECTOR = "#E5484D"

    private class Svg(width: Double, height: Double) {
        val root: Element = document.createElementNS(NS, "svg").apply {
            setAttribute("viewBox", "0 0 ${fmt(width)} ${fmt(height)}")
            setAttribute("class", "chart-svg")
            setAttribute("role", "img")
        }
        fun el(name: String, vararg attrs: Pair<String, Any>, text: String? = null): Element {
            val e = document.createElementNS(NS, name)
            attrs.forEach { (k, v) -> e.setAttribute(k, if (v is Double) fmt(v) else v.toString()) }
            if (text != null) e.textContent = text
            root.appendChild(e)
            return e
        }
        fun line(x1: Double, y1: Double, x2: Double, y2: Double, stroke: String, width: Double, dash: String? = null) =
            el("line", "x1" to x1, "y1" to y1, "x2" to x2, "y2" to y2, "style" to "stroke:$stroke;stroke-width:${fmt(width)}${dash?.let { ";stroke-dasharray:$it" } ?: ""}")
        fun text(x: Double, y: Double, value: String, size: Double = 11.0, fill: String = "var(--muted)", anchor: String = "start", weight: String = "400", italic: Boolean = false) =
            el("text", "x" to x, "y" to y, "style" to "fill:$fill;font-size:${fmt(size)}px;font-weight:$weight;text-anchor:$anchor;dominant-baseline:middle${if (italic) ";font-style:italic" else ""}", text = value)
    }

    private fun fmt(v: Double): String = ((v * 100).toLong() / 100.0).toString()

    /** Largura aproximada do texto em SVG (não dá para medir antes de desenhar). */
    private fun textWidth(text: String, size: Double) = text.length * size * 0.56

    fun render(chart: Chart, width: Double = 420.0): Element? = runCatching {
        when (chart) {
            is Pie -> pie(chart, width)
            is Bars -> bars(chart, width)
            is Lines -> lines(chart, width)
            is Function -> function(chart, width)
            is Geometry -> geometry(chart, width)
        }
    }.getOrNull()

    private fun pie(chart: Pie, width: Double): Element {
        val total = chart.items.sumOf { it.value }
        val rowH = 22.0
        // Tela estreita (celular): a legenda vai para baixo da pizza, com a largura toda.
        val stacked = width < 400
        val pieBox = 150.0
        val height = if (stacked) pieBox + 10 + chart.items.size * rowH else max(pieBox, chart.items.size * rowH + 10)
        val svg = Svg(width, height)
        val cx = if (stacked) width / 2 else 75.0
        val cy = if (stacked) pieBox / 2 else height / 2
        val r = 68.0
        val legendX = if (stacked) 6.0 else 168.0
        var start = -PI / 2
        chart.items.forEachIndexed { index, item ->
            val sweep = item.value / total * 2 * PI
            val tone = tones[index % tones.size]
            if (chart.items.size == 1 || sweep >= 2 * PI - 1e-6) {
                svg.el("circle", "cx" to cx, "cy" to cy, "r" to r, "style" to "fill:$tone")
            } else {
                val x1 = cx + r * cos(start); val y1 = cy + r * sin(start)
                val x2 = cx + r * cos(start + sweep); val y2 = cy + r * sin(start + sweep)
                val large = if (sweep > PI) 1 else 0
                svg.el("path", "d" to "M ${fmt(cx)} ${fmt(cy)} L ${fmt(x1)} ${fmt(y1)} A ${fmt(r)} ${fmt(r)} 0 $large 1 ${fmt(x2)} ${fmt(y2)} Z", "style" to "fill:$tone;stroke:var(--card);stroke-width:2")
            }
            start += sweep
        }
        val top = if (stacked) pieBox + 10 + rowH / 2 else cy - chart.items.size * rowH / 2 + rowH / 2
        chart.items.forEachIndexed { index, item ->
            val y = top + index * rowH
            svg.el("circle", "cx" to legendX, "cy" to y, "r" to 5.0, "style" to "fill:${tones[index % tones.size]}")
            svg.text(legendX + 12, y, clip(item.label, width - legendX - 12 - 52, 12.0), 12.0, "var(--ink)")
            svg.text(width - 6, y, "${number(item.value / total * 100)}%", 12.0, "var(--ink)", "end", "800")
        }
        return svg.root
    }

    private fun bars(chart: Bars, width: Double): Element {
        val row = 32.0
        val svg = Svg(width, chart.items.size * row + 8)
        val labelWidth = (chart.items.maxOf { textWidth(it.label, 11.5) } + 10).coerceIn(width * 0.22, width * 0.46)
        val maxPositive = chart.items.maxOf { it.value }.coerceAtLeast(0.0)
        val maxNegative = (-chart.items.minOf { it.value }).coerceAtLeast(0.0)
        val span = (maxPositive + maxNegative).takeIf { it > 0 } ?: 1.0
        val valueTexts = chart.items.map { number(it.value) + (chart.unit?.let { u -> if (u == "%") u else " $u" } ?: "") }
        val widest = valueTexts.maxOf { textWidth(it, 11.5) }
        val gap = 6.0
        val room = width - labelWidth - (if (maxNegative > 0) 2 else 1) * (widest + gap)
        val zeroX = labelWidth + (if (maxNegative > 0) widest + gap else 0.0) + room * (maxNegative / span)
        if (maxNegative > 0) svg.line(zeroX, 0.0, zeroX, chart.items.size * row, "var(--faint)", 1.0)
        chart.items.forEachIndexed { index, item ->
            val top = index * row + 4
            val mid = top + (row - 8) / 2
            svg.text(0.0, mid, clip(item.label, labelWidth - 8, 11.5), 11.5, "var(--muted)")
            val w = max(3.0, room * (abs(item.value) / span))
            val left = if (item.value < 0) zeroX - w else zeroX
            svg.el("rect", "x" to left, "y" to top + 3, "width" to w, "height" to row - 14, "rx" to 6.0, "style" to "fill:${tones[index % tones.size]}")
            if (item.value < 0) svg.text(left - gap, mid, valueTexts[index], 11.5, "var(--ink)", "end", "800")
            else svg.text(zeroX + w + gap, mid, valueTexts[index], 11.5, "var(--ink)", "start", "800")
        }
        return svg.root
    }

    private fun clip(text: String, room: Double, size: Double): String {
        val fit = (room / (size * 0.56)).toInt()
        return if (text.length <= fit || fit < 4) text else text.take(fit - 1) + "…"
    }

    private class Plot(val svg: Svg, val toX: (Double) -> Double, val toY: (Double) -> Double, val clip: String)

    private var clipCount = 0

    private fun plot(width: Double, height: Double, xMinRaw: Double, xMaxRaw: Double, yMinRaw: Double, yMaxRaw: Double, xLabel: String?, yLabel: String?, axesAtZero: Boolean): Plot {
        val svg = Svg(width, height)
        val (xMin, xMax) = if (xMaxRaw > xMinRaw) xMinRaw to xMaxRaw else (xMinRaw - 1) to (xMinRaw + 1)
        val pad = (yMaxRaw - yMinRaw).takeIf { it > 0 }?.let { it * 0.08 } ?: 1.0
        val yMin = yMinRaw - pad; val yMax = yMaxRaw + pad
        val left = 40.0; val bottom = height - 22; val top = 8.0; val right = width - 8
        val toX = { x: Double -> left + (x - xMin) / (xMax - xMin) * (right - left) }
        val toY = { y: Double -> bottom - (y - yMin) / (yMax - yMin) * (bottom - top) }
        ticks(yMin, yMax).forEach { y ->
            svg.line(left, toY(y), right, toY(y), "var(--soft-2)", 1.0, "4 4")
            svg.text(left - 4, toY(y), number(y), 10.5, "var(--faint)", "end")
        }
        ticks(xMin, xMax).forEach { x ->
            svg.line(toX(x), top, toX(x), bottom, "var(--soft-2)", 1.0, "4 4")
            svg.text(toX(x), bottom + 11, number(x), 10.5, "var(--faint)", "middle")
        }
        val axisY = if (axesAtZero && 0.0 in yMin..yMax) toY(0.0) else bottom
        val axisX = if (axesAtZero && 0.0 in xMin..xMax) toX(0.0) else left
        svg.line(left, axisY, right, axisY, "var(--muted)", 1.5)
        svg.line(axisX, top, axisX, bottom, "var(--muted)", 1.5)
        xLabel?.let { svg.text(right, axisY - 10, it, 11.0, "var(--muted)", "end") }
        yLabel?.let { svg.text(axisX + 5, top + 6, it, 11.0, "var(--muted)") }
        // As curvas ficam dentro da área do gráfico (uma reta íngreme não passa por cima do título).
        val clip = "chart-clip-${++clipCount}"
        val defs = svg.el("defs")
        val clipPath = document.createElementNS(NS, "clipPath").apply { setAttribute("id", clip) }
        clipPath.appendChild(document.createElementNS(NS, "rect").apply {
            setAttribute("x", fmt(left)); setAttribute("y", fmt(top)); setAttribute("width", fmt(right - left)); setAttribute("height", fmt(bottom - top))
        })
        defs.appendChild(clipPath)
        return Plot(svg, toX, toY, clip)
    }

    private fun lines(chart: Lines, width: Double): Element {
        val all = chart.series.flatMap { it.points }
        val p = plot(width, width * 0.66, all.minOf { it.first }, all.maxOf { it.first }, all.minOf { it.second }, all.maxOf { it.second }, chart.xLabel, chart.yLabel, false)
        chart.series.forEachIndexed { index, series ->
            val tone = tones[index % tones.size]
            val d = series.points.mapIndexed { i, (x, y) -> "${if (i == 0) "M" else "L"} ${fmt(p.toX(x))} ${fmt(p.toY(y))}" }.joinToString(" ")
            p.svg.el("path", "d" to d, "clip-path" to "url(#${p.clip})", "style" to "fill:none;stroke:$tone;stroke-width:3;stroke-linejoin:round")
            series.points.forEach { (x, y) -> p.svg.el("circle", "cx" to p.toX(x), "cy" to p.toY(y), "r" to 3.5, "style" to "fill:$tone") }
        }
        return withLegend(p.svg.root, if (chart.series.size > 1 || chart.series.first().name != null) chart.series.mapIndexed { i, s -> (s.name ?: "Série ${i + 1}") to tones[i % tones.size] } else emptyList())
    }

    private fun function(chart: Function, width: Double): Element {
        val samples = chart.curves.map { curve -> (0..240).map { step -> val x = chart.xMin + (chart.xMax - chart.xMin) * step / 240.0; x to curve.expr.at(x) } }
        val ys = samples.flatten().map { it.second }.filter { it.isFinite() }.sorted()
        val lo = ys.getOrElse((ys.size * 0.05).toInt()) { -1.0 }
        val hi = ys.getOrElse((ys.size * 0.95).toInt().coerceAtMost(ys.lastIndex)) { 1.0 }
        val marksY = chart.marks.map { it.second.second }
        val yMin = min(min(lo, 0.0), marksY.minOrNull() ?: lo)
        val yMax = max(max(hi, 0.0), marksY.maxOrNull() ?: hi)
        val p = plot(width, width * 0.8, chart.xMin, chart.xMax, yMin, yMax, "x", "y", true)
        samples.forEachIndexed { index, points ->
            val d = StringBuilder()
            var pen = false
            points.forEach { (x, y) ->
                val visible = y.isFinite() && y >= yMin - (yMax - yMin) && y <= yMax + (yMax - yMin)
                if (!visible) { pen = false; return@forEach }
                d.append(if (pen) " L " else " M ").append(fmt(p.toX(x))).append(' ').append(fmt(p.toY(y)))
                pen = true
            }
            p.svg.el("path", "d" to d.toString().trim(), "clip-path" to "url(#${p.clip})", "style" to "fill:none;stroke:${tones[index % tones.size]};stroke-width:3;stroke-linejoin:round")
        }
        chart.marks.forEach { (label, point) ->
            val x = p.toX(point.first); val y = p.toY(point.second)
            p.svg.el("circle", "cx" to x, "cy" to y, "r" to 6.0, "style" to "fill:var(--card)")
            p.svg.el("circle", "cx" to x, "cy" to y, "r" to 4.0, "style" to "fill:var(--ink)")
            label?.let {
                val w = textWidth(it, 11.0)
                val lx = if (x + 8 + w > width - 8) x - 8 - w else x + 8
                val ly = max(8.0, y - 14)
                p.svg.el("rect", "x" to lx - 3, "y" to ly - 8, "width" to w + 6, "height" to 16.0, "rx" to 4.0, "style" to "fill:var(--soft-2)")
                p.svg.text(lx, ly, it, 11.0, "var(--ink)", "start", "800")
            }
        }
        return withLegend(p.svg.root, chart.curves.mapIndexed { i, c -> c.name to tones[i % tones.size] })
    }

    private fun withLegend(svg: Element, entries: List<Pair<String, String>>): Element {
        if (entries.isEmpty()) return svg
        val wrap = document.createElement("div")
        wrap.appendChild(svg)
        val legend = document.createElement("div")
        legend.setAttribute("class", "chart-legend")
        entries.forEach { (label, tone) ->
            val item = document.createElement("span")
            val swatch = document.createElement("i")
            swatch.setAttribute("style", "background:$tone")
            item.appendChild(swatch)
            item.appendChild(document.createTextNode(label))
            legend.appendChild(item)
        }
        wrap.appendChild(legend)
        return wrap
    }

    private fun geometry(fig: Geometry, width: Double): Element {
        val xs = fig.points.values.map { it.first } + fig.circles.flatMap { c -> fig.points.getValue(c.center).first.let { listOf(it - c.radius, it + c.radius) } }
        val ys = fig.points.values.map { it.second } + fig.circles.flatMap { c -> fig.points.getValue(c.center).second.let { listOf(it - c.radius, it + c.radius) } }
        val spanX = (xs.max() - xs.min()).coerceAtLeast(1e-6)
        val spanY = (ys.max() - ys.min()).coerceAtLeast(1e-6)
        val ratio = (spanX / spanY).coerceIn(0.8, 2.2)
        val height = width / ratio
        val svg = Svg(width, height)
        val pad = 34.0
        val scale = min((width - 2 * pad) / spanX, (height - 2 * pad) / spanY)
        val offsetX = (width - spanX * scale) / 2
        val offsetY = (height - spanY * scale) / 2
        fun at(name: String): Pair<Double, Double> = fig.points.getValue(name).let { (x, y) -> (offsetX + (x - xs.min()) * scale) to (height - offsetY - (y - ys.min()) * scale) }
        val all = fig.points.keys.map(::at)
        val centroid = all.sumOf { it.first } / all.size to all.sumOf { it.second } / all.size
        fun label(text: String, ax: Double, ay: Double, dx: Double, dy: Double, fill: String, italic: Boolean, weight: String) {
            val w = textWidth(text, 13.0)
            val reach = 12 + max(w, 13.0) / 2
            val cx = (ax + dx * reach).coerceIn(w / 2, width - w / 2)
            val cy = (ay + dy * reach).coerceIn(8.0, height - 8)
            svg.text(cx, cy, text, 13.0, fill, "middle", weight, italic)
        }
        fun outward(ax: Double, ay: Double, bx: Double, by: Double): Pair<Double, Double> {
            val dx = bx - ax; val dy = by - ay
            val len = hypot(dx, dy).coerceAtLeast(1.0)
            var nx = -dy / len; var ny = dx / len
            val mx = (ax + bx) / 2; val my = (ay + by) / 2
            if (hypot(mx + nx - centroid.first, my + ny - centroid.second) < hypot(mx - centroid.first, my - centroid.second)) { nx = -nx; ny = -ny }
            return nx to ny
        }
        fig.polygons.forEach { polygon ->
            val d = polygon.mapIndexed { i, name -> at(name).let { "${if (i == 0) "M" else "L"} ${fmt(it.first)} ${fmt(it.second)}" } }.joinToString(" ") + " Z"
            svg.el("path", "d" to d, "style" to "fill:color-mix(in srgb, var(--primary) 8%, transparent);stroke:var(--primary);stroke-width:2.5;stroke-linejoin:round")
        }
        fig.circles.forEach { circle ->
            val (cx, cy) = at(circle.center)
            val r = circle.radius * scale
            svg.el("circle", "cx" to cx, "cy" to cy, "r" to r, "style" to "fill:none;stroke:var(--primary);stroke-width:2.5")
            circle.label?.let {
                svg.line(cx, cy, cx + r, cy, "var(--ink)", 1.5, "8 6")
                label(it, cx + r / 2, cy, 0.0, -1.0, "var(--ink)", true, "600")
            }
        }
        fig.segments.forEach { s ->
            val (ax, ay) = at(s.from); val (bx, by) = at(s.to)
            svg.line(ax, ay, bx, by, "var(--ink)", 2.0, if (s.dashed) "10 8" else null)
            s.label?.let { val (nx, ny) = outward(ax, ay, bx, by); label(it, (ax + bx) / 2, (ay + by) / 2, nx, ny, "var(--ink)", true, "600") }
        }
        fig.angles.forEach { a ->
            val (vx, vy) = at(a.vertex); val (px, py) = at(a.from); val (qx, qy) = at(a.to)
            val l1 = hypot(px - vx, py - vy).coerceAtLeast(1.0); val l2 = hypot(qx - vx, qy - vy).coerceAtLeast(1.0)
            val u1 = (px - vx) / l1 to (py - vy) / l1; val u2 = (qx - vx) / l2 to (qy - vy) / l2
            val r = 18.0
            if (a.right) {
                svg.el("path", "d" to "M ${fmt(vx + u1.first * r)} ${fmt(vy + u1.second * r)} L ${fmt(vx + (u1.first + u2.first) * r)} ${fmt(vy + (u1.second + u2.second) * r)} L ${fmt(vx + u2.first * r)} ${fmt(vy + u2.second * r)}", "style" to "fill:none;stroke:var(--primary);stroke-width:2")
            } else {
                var sweep = atan2(u2.second, u2.first) - atan2(u1.second, u1.first)
                if (sweep > PI) sweep -= 2 * PI
                if (sweep < -PI) sweep += 2 * PI
                val sweepFlag = if (sweep > 0) 1 else 0
                svg.el("path", "d" to "M ${fmt(vx + u1.first * r)} ${fmt(vy + u1.second * r)} A ${fmt(r)} ${fmt(r)} 0 0 $sweepFlag ${fmt(vx + u2.first * r)} ${fmt(vy + u2.second * r)}", "style" to "fill:none;stroke:var(--primary);stroke-width:2")
            }
            a.label?.let {
                val bx = u1.first + u2.first; val by = u1.second + u2.second
                val len = hypot(bx, by)
                val (dx, dy) = if (len < 1e-3) 0.0 to -1.0 else bx / len to by / len
                label(it, vx + dx * r, vy + dy * r, dx, dy, "var(--primary)", false, "600")
            }
        }
        fig.vectors.forEach { v ->
            val (ax, ay) = at(v.from); val (bx, by) = at(v.to)
            svg.line(ax, ay, bx, by, VECTOR, 3.0)
            val angle = atan2(by - ay, bx - ax); val head = 12.0
            svg.el("path", "d" to "M ${fmt(bx)} ${fmt(by)} L ${fmt(bx - head * cos(angle - 0.45))} ${fmt(by - head * sin(angle - 0.45))} L ${fmt(bx - head * cos(angle + 0.45))} ${fmt(by - head * sin(angle + 0.45))} Z", "style" to "fill:$VECTOR")
            v.label?.let { val (nx, ny) = outward(ax, ay, bx, by); label(it, (ax + bx) / 2, (ay + by) / 2, nx, ny, VECTOR, true, "600") }
        }
        if (fig.showNames) fig.points.keys.forEach { name ->
            val (x, y) = at(name)
            svg.el("circle", "cx" to x, "cy" to y, "r" to 3.5, "style" to "fill:var(--ink)")
            val dx = x - centroid.first; val dy = y - centroid.second
            val len = hypot(dx, dy)
            val (ux, uy) = if (len < 1) -0.7 to -0.7 else dx / len to dy / len
            label(name, x, y, ux, uy, "var(--muted)", false, "800")
        }
        return svg.root
    }

    private fun ticks(min: Double, max: Double, target: Int = 5): List<Double> {
        val span = max - min
        if (span <= 0 || !span.isFinite()) return emptyList()
        val raw = span / target
        val magnitude = 10.0.pow(floor(log10(raw)))
        val step = listOf(1.0, 2.0, 2.5, 5.0, 10.0).map { it * magnitude }.first { it >= raw }
        val first = ceil(min / step) * step
        return generateSequence(first) { it + step }.takeWhile { it <= max + step * 1e-9 }.map { if (abs(it) < step * 1e-9) 0.0 else it }.toList()
    }

    /** Número no jeito brasileiro: 1.250 e 0,25. */
    fun number(value: Double): String {
        val negative = value < 0
        val v = abs(value)
        val text = if (v == floor(v) && v < 1e12) {
            v.toLong().toString().reversed().chunked(3).joinToString(".").reversed()
        } else {
            val fixed = (v.asDynamic().toFixed(2) as String).trimEnd('0').trimEnd('.')
            val (int, dec) = fixed.split('.').let { it[0] to it.getOrNull(1) }
            int.reversed().chunked(3).joinToString(".").reversed() + (dec?.let { ",$it" } ?: "")
        }
        return if (negative) "−$text" else text
    }
}

/** Um bloco ```grafico do material: título, desenho e legenda. Bloco ilegível não aparece. */
@Composable
fun ChartBlock(source: String) {
    val chart = WebChart.parse(source) ?: return
    key(source) {
        Div({ classes("chart-card") }) {
            chart.title?.let { org.jetbrains.compose.web.dom.Div({ classes("chart-title") }) { org.jetbrains.compose.web.dom.Text(it) } }
            Div({
                classes("chart-body")
                ref { element ->
                    WebChart.render(chart, (element.clientWidth.toDouble()).takeIf { it > 0 }?.coerceIn(280.0, 560.0) ?: 420.0)?.let { element.appendChild(it) }
                    onDispose { while (element.firstChild != null) element.removeChild(element.firstChild!!) }
                }
            })
            chart.caption?.let { org.jetbrains.compose.web.dom.Div({ classes("chart-caption") }) { org.jetbrains.compose.web.dom.Text(it) } }
        }
    }
}
