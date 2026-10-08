package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.svg.Circle
import org.jetbrains.compose.web.svg.Line
import org.jetbrains.compose.web.svg.Path
import org.jetbrains.compose.web.svg.Polyline
import org.jetbrains.compose.web.svg.Rect
import org.jetbrains.compose.web.svg.Svg
import org.jetbrains.compose.web.svg.SvgText
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin

/**
 * Gráfico dentro do material (mesmo formato do app, ui/components/StudyChart.kt): um bloco
 *
 * ```grafico            (ou ~~~grafico)
 * {"tipo": "barras", "titulo": "...", "itens": [{"rotulo": "A", "valor": 5}]}
 * ```
 *
 * O site desenha pizza, barras e linha em SVG. Os tipos que só o app desenha (funcao, geometria) e
 * blocos que não dá para ler somem: melhor não mostrar nada do que mostrar o JSON cru.
 */
sealed interface StudyChart {
    val title: String?
    val caption: String?

    data class Item(val label: String, val value: Double)
    data class Series(val name: String?, val points: List<Pair<Double, Double>>)

    data class Pie(override val title: String?, override val caption: String?, val items: List<Item>, val unit: String?) : StudyChart
    data class Bars(override val title: String?, override val caption: String?, val items: List<Item>, val unit: String?) : StudyChart
    data class Lines(override val title: String?, override val caption: String?, val series: List<Series>, val xLabel: String?, val yLabel: String?) : StudyChart

    companion object {
        private val json = Json { isLenient = true; ignoreUnknownKeys = true }

        /** Nomes aceitos depois da cerca (```grafico, ~~~barras...), os mesmos do app. */
        val fences = setOf("grafico", "gráfico", "geometria", "figura", "funcao", "função", "pizza", "barras", "linha", "chart")

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
                        val points = ((obj["pontos"] ?: obj["points"]) as? JsonArray)?.mapNotNull { p ->
                            when (p) {
                                is JsonArray -> if (p.size >= 2) p[0].num()?.let { x -> p[1].num()?.let { y -> x to y } } else null
                                is JsonObject -> p["x"]?.num()?.let { x -> p["y"]?.num()?.let { y -> x to y } }
                                else -> null
                            }
                        }.orEmpty().sortedBy { it.first }
                        if (points.size < 2) null else Series(obj.text("nome", "name"), points)
                    }.orEmpty().take(4)
                    if (series.isEmpty()) null else Lines(title, caption, series, root.text("eixoX", "x"), root.text("eixoY", "y"))
                }
                else -> null
            }
        }.getOrNull()

        private fun items(root: JsonObject): List<Item>? = (root["itens"] ?: root["items"] ?: root["dados"]).let { it as? JsonArray }?.mapNotNull { element ->
            val obj = element as? JsonObject ?: return@mapNotNull null
            val label = obj.text("rotulo", "label", "nome") ?: return@mapNotNull null
            val value = obj["valor"]?.num() ?: obj["value"]?.num() ?: return@mapNotNull null
            if (value.isNaN() || value < 0) null else Item(label, value)
        }?.take(12)?.takeIf { list -> list.size >= 2 && list.sumOf { it.value } > 0 }

        private fun JsonObject.text(vararg keys: String): String? = keys.firstNotNullOfOrNull { key ->
            (this[key] as? JsonPrimitive)?.takeIf { it.isString }?.content?.trim()?.takeIf(String::isNotEmpty)
        }

        private fun kotlinx.serialization.json.JsonElement.num(): Double? =
            (this as? JsonPrimitive)?.let { it.doubleOrNull ?: it.content.replace(',', '.').toDoubleOrNull() }

        private fun fold(s: String) = s.replace("ç", "c").replace("ã", "a").replace("á", "a").replace("é", "e").replace("ó", "o")
    }
}

/** Cerca que abre um gráfico: ```grafico ou ~~~grafico (com ou sem espaço antes do nome). */
internal fun chartFenceName(trimmed: String): String? {
    val fence = when { trimmed.startsWith("```") -> "```"; trimmed.startsWith("~~~") -> "~~~"; else -> return null }
    val name = trimmed.removePrefix(fence).trim().substringBefore(' ').substringBefore('{').lowercase()
    return name.takeIf { it in StudyChart.fences }
}

private val palette = listOf("var(--primary)", "#0E9F8E", "#E07A1F", "#C2417B", "#5B8DEF", "#8A6FDF", "#3D9A50", "#B5651D")

private fun fmt(v: Double): String {
    val r = kotlin.math.round(v * 100) / 100
    val s = if (r == kotlin.math.floor(r)) r.toLong().toString() else r.toString()
    return s.replace('.', ',')
}

/** Passo "redondo" do eixo (1, 2, 5 × 10ⁿ) para uns 4 traços até o máximo. */
private fun niceMax(max: Double): Pair<Double, Double> {
    if (max <= 0) return 1.0 to 0.25
    val raw = max / 4
    val mag = 10.0.pow(floor(log10(raw)))
    val step = listOf(1.0, 2.0, 2.5, 5.0, 10.0).map { it * mag }.first { it >= raw }
    return ceil(max / step) * step to step
}

@Composable
fun ChartView(chart: StudyChart) {
    Div({ classes("study-chart") }) {
        chart.title?.let { Div({ classes("study-chart-title") }) { Text(it) } }
        when (chart) {
            is StudyChart.Bars -> BarsSvg(chart)
            is StudyChart.Pie -> PieSvg(chart)
            is StudyChart.Lines -> LinesSvg(chart)
        }
        chart.caption?.let { Div({ classes("study-chart-caption") }) { Text(it) } }
    }
}

@Composable
private fun BarsSvg(chart: StudyChart.Bars) {
    val w = 560.0; val h = 300.0; val left = 44.0; val right = 12.0; val top = 16.0; val bottom = 46.0
    val (max, step) = niceMax(chart.items.maxOf { it.value })
    val plotW = w - left - right; val plotH = h - top - bottom
    val slot = plotW / chart.items.size
    val barW = (slot * 0.6).coerceAtMost(70.0)
    Svg(viewBox = "0 0 $w $h", attrs = { classes("study-chart-svg"); attr("role", "img"); attr("aria-label", chart.title ?: "gráfico de barras") }) {
        var t = 0.0
        while (t <= max + 1e-9) {
            val y = top + plotH - plotH * t / max
            Line(left, y, w - right, y, { classes("grid") })
            SvgText(fmt(t), left - 6, y + 4, { classes("tick"); attr("text-anchor", "end") })
            t += step
        }
        chart.items.forEachIndexed { i, item ->
            val bh = plotH * item.value / max
            val x = left + slot * i + (slot - barW) / 2
            Rect(x, top + plotH - bh, barW, bh, { attr("fill", palette[i % palette.size]); attr("rx", "3") })
            SvgText(fmt(item.value), x + barW / 2, top + plotH - bh - 5, { classes("value"); attr("text-anchor", "middle") })
            SvgText(item.label.take(16), x + barW / 2, h - bottom + 18, { classes("tick"); attr("text-anchor", "middle") })
        }
        Line(left, top + plotH, w - right, top + plotH, { classes("axis") })
        chart.unit?.let { SvgText(it, left, h - 6, { classes("unit") }) }
    }
}

@Composable
private fun PieSvg(chart: StudyChart.Pie) {
    val total = chart.items.sumOf { it.value }
    val size = 220.0; val c = size / 2; val r = size / 2 - 6
    Div({ classes("study-chart-pie") }) {
        Svg(viewBox = "0 0 $size $size", attrs = { classes("study-chart-svg", "pie"); attr("role", "img"); attr("aria-label", chart.title ?: "gráfico de setores") }) {
            var angle = -PI / 2
            chart.items.forEachIndexed { i, item ->
                val sweep = 2 * PI * item.value / total
                val color = palette[i % palette.size]
                if (sweep >= 2 * PI - 1e-9) {
                    Circle(c, c, r, { attr("fill", color) })
                } else if (sweep > 0) {
                    val x1 = c + r * cos(angle); val y1 = c + r * sin(angle)
                    val x2 = c + r * cos(angle + sweep); val y2 = c + r * sin(angle + sweep)
                    val large = if (sweep > PI) 1 else 0
                    Path("M $c $c L $x1 $y1 A $r $r 0 $large 1 $x2 $y2 Z", { attr("fill", color); attr("stroke", "var(--paper)"); attr("stroke-width", "2") })
                }
                angle += sweep
            }
        }
        Div({ classes("study-chart-legend") }) {
            chart.items.forEachIndexed { i, item ->
                Div({ classes("row") }) {
                    Span({ classes("sw"); attr("style", "background:${palette[i % palette.size]}") })
                    Span { Text("${item.label}: ${fmt(item.value)}${chart.unit?.let { " $it" } ?: ""} (${fmt(100 * item.value / total)}%)") }
                }
            }
        }
    }
}

@Composable
private fun LinesSvg(chart: StudyChart.Lines) {
    val w = 560.0; val h = 300.0; val left = 44.0; val right = 14.0; val top = 16.0; val bottom = 46.0
    val all = chart.series.flatMap { it.points }
    val xMin = all.minOf { it.first }; val xMax = all.maxOf { it.first }.let { if (it == xMin) xMin + 1 else it }
    val yLow = all.minOf { it.second }.coerceAtMost(0.0)
    val (yMax, step) = niceMax(all.maxOf { it.second } - yLow).let { (m, s) -> (m + yLow) to s }
    val plotW = w - left - right; val plotH = h - top - bottom
    fun px(x: Double) = left + plotW * (x - xMin) / (xMax - xMin)
    fun py(y: Double) = top + plotH - plotH * (y - yLow) / (yMax - yLow)
    Svg(viewBox = "0 0 $w $h", attrs = { classes("study-chart-svg"); attr("role", "img"); attr("aria-label", chart.title ?: "gráfico de linhas") }) {
        var t = yLow
        while (t <= yMax + 1e-9) {
            Line(left, py(t), w - right, py(t), { classes("grid") })
            SvgText(fmt(t), left - 6, py(t) + 4, { classes("tick"); attr("text-anchor", "end") })
            t += step
        }
        all.map { it.first }.distinct().take(12).forEach { x ->
            SvgText(fmt(x), px(x), h - bottom + 18, { classes("tick"); attr("text-anchor", "middle") })
        }
        Line(left, top + plotH, w - right, top + plotH, { classes("axis") })
        chart.series.forEachIndexed { i, s ->
            val color = palette[i % palette.size]
            Polyline(*s.points.flatMap { listOf(px(it.first), py(it.second)) }.toTypedArray(), attrs = {
                attr("fill", "none"); attr("stroke", color); attr("stroke-width", "3"); attr("stroke-linejoin", "round")
            })
            s.points.forEach { (x, y) ->
                Circle(px(x), py(y), 4, { attr("fill", color) })
                SvgText(fmt(y), px(x), py(y) - 9, { classes("value"); attr("text-anchor", "middle") })
            }
        }
        val axes = listOfNotNull(chart.xLabel?.let { "x: $it" }, chart.yLabel?.let { "y: $it" }).joinToString("   ")
        if (axes.isNotEmpty()) SvgText(axes, left, h - 6, { classes("unit") })
    }
    if (chart.series.size > 1 || chart.series.first().name != null) Div({ classes("study-chart-legend", "inline") }) {
        chart.series.forEachIndexed { i, s ->
            Div({ classes("row") }) {
                Span({ classes("sw"); attr("style", "background:${palette[i % palette.size]}") })
                Span { Text(s.name ?: "série ${i + 1}") }
            }
        }
    }
}
