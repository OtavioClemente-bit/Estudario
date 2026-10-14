package br.com.estudario.ui.brand

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject

/**
 * Os ícones do Estudário: a turma do Folha. Cada ícone é um personagem com o rosto do mascote (olhos
 * ovais com brilho, bochecha, boquinha) num adesivo de caderno (recorte branco, traço de caneta, aba de
 * profundidade embaixo). O desenho vem de design/turma/turma.js, gerado em resources/turma.json por
 * scripts/icones/gerar.ts — o mesmo arquivo gera o turma.js do site, então app e site são idênticos.
 *
 * As cores são fixas e não mudam com o tema; sem cor (desligado, bloqueado, aba fora de foco) o
 * personagem fica em cinza. Abaixo de 36 dp o rosto se simplifica, para não virar borrão.
 */
enum class Glyph {
    House, Books, OpenBook, Calendar, Timer, Clipboard, Notebook, Bookmark, Cap, Check, Flag,
    Lock, Star, Trophy, Medal, Bulb, Warning, Oops, Fire, Bolt, Heart, Bell, Chart, Cycle, Target, Folder,
    Gear, Help, Person, Shield, Cards, Dice, Play, Cloud, Doc, Pencil, Sun, Moon, Phone, Gem, Brain, Trap,
}

@Composable
fun BrandIcon(glyph: Glyph, modifier: Modifier = Modifier, size: Dp = 28.dp, muted: Boolean = false) {
    Canvas(modifier.size(size)) { drawGlyph(glyph, muted) }
}

/** Desenha o personagem ocupando a área atual, centralizado. */
fun DrawScope.drawGlyph(glyph: Glyph, muted: Boolean = false) {
    val art = Turma.art(glyph) ?: return
    val (vx, vy, vw, _) = Turma.viewBox
    val side = size.minDimension
    val k = side / vw
    val detail = side >= 36.dp.toPx()
    translate((size.width - side) / 2f, (size.height - side) / 2f) {
        scale(k, k, pivot = androidx.compose.ui.geometry.Offset.Zero) {
            translate(-vx, -vy) {
                for (p in art.body) drawPrim(p, muted)
                for (p in if (detail) art.face else art.faceSmall) drawPrim(p, muted)
            }
        }
    }
}

private fun DrawScope.drawPrim(p: Turma.Prim, muted: Boolean) {
    translate(0f, p.dy) {
        p.fill?.let { drawPath(p.path, Turma.tone(it, muted), alpha = p.alpha) }
        p.stroke?.let {
            drawPath(p.path, Turma.tone(it, muted), alpha = p.alpha, style = Stroke(p.width, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

internal object Turma {
    class Prim(val path: Path, val fill: Color?, val stroke: Color?, val width: Float, val alpha: Float, val dy: Float)
    class Art(val anim: String, val body: List<Prim>, val face: List<Prim>, val faceSmall: List<Prim>)

    var viewBox = floatArrayOf(-8f, -10f, 116f, 116f)
        private set
    private val arts: Map<String, Art> by lazy { load() }

    fun art(glyph: Glyph): Art? = arts[glyph.name]

    /** Cinza do estado desligado, mantendo a luz e a sombra do desenho. */
    fun tone(color: Color, muted: Boolean): Color {
        if (!muted) return color
        val v = 0.55f + color.luminance() * 0.42f
        return Color(v, v, (v * 1.02f).coerceAtMost(1f), color.alpha)
    }

    private fun load(): Map<String, Art> {
        val text = Turma::class.java.classLoader?.getResourceAsStream("turma.json")?.bufferedReader()?.use { it.readText() }
            ?: return emptyMap()
        val root = JSONObject(text)
        root.optJSONArray("viewBox")?.let { vb -> viewBox = FloatArray(4) { vb.getDouble(it).toFloat() } }
        val icons = root.getJSONObject("icons")
        return icons.keys().asSequence().associateWith { name ->
            val o = icons.getJSONObject(name)
            Art(o.optString("anim"), prims(o.getJSONArray("body")), prims(o.getJSONArray("face")), prims(o.getJSONArray("faceSmall")))
        }
    }

    private fun prims(a: JSONArray): List<Prim> = List(a.length()) { i ->
        val o = a.getJSONObject(i)
        Prim(
            path = PathParser().parsePathString(o.getString("d")).toPath(),
            fill = o.optString("f").takeIf { it.isNotEmpty() && it != "null" }?.let(::color),
            stroke = o.optString("s").takeIf { it.isNotEmpty() }?.let(::color),
            width = o.optDouble("w", 5.0).toFloat(),
            alpha = o.optDouble("o", 1.0).toFloat(),
            dy = o.optDouble("dy", 0.0).toFloat(),
        )
    }

    /** "#RRGGBB" ou "rgba(r,g,b,a)". */
    private fun color(s: String): Color {
        if (s.startsWith("#")) return Color(android.graphics.Color.parseColor(s))
        val n = s.substringAfter("(").substringBefore(")").split(",").map { it.trim().toFloat() }
        return Color(n[0] / 255f, n[1] / 255f, n[2] / 255f, n.getOrElse(3) { 1f })
    }
}
