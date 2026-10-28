package br.com.estudario.domain

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * O brasão do nível (o mesmo desenho no app e no site): cada título é uma patente com o seu metal
 * (bronze, prata, ouro, esmeralda...), as estrelas contam os níveis dentro do título e o brasão ganha
 * peças conforme sobe: joia, asas, coroa, louros e raios. O SVG sai pronto, sem fontes nem imagens.
 */
object LevelCrest {
    /** Uma patente: o primeiro nível dela, o título e as três cores do metal (claro, médio, escuro). */
    data class Rank(val from: Int, val title: String, val metal: String, val light: String, val mid: String, val dark: String)

    val ranks = listOf(
        Rank(1, ProgressEngine.levelTitle(1), "Ferro lilás", "#D9D3FF", "#8D82E8", "#4B3FB0"),
        Rank(5, ProgressEngine.levelTitle(5), "Bronze", "#FFD3A3", "#C9783A", "#7A3E16"),
        Rank(10, ProgressEngine.levelTitle(10), "Prata", "#FFFFFF", "#AEB6C8", "#5E667A"),
        Rank(15, ProgressEngine.levelTitle(15), "Ouro", "#FFF0B3", "#F2B300", "#9A6200"),
        Rank(20, ProgressEngine.levelTitle(20), "Esmeralda", "#B8FBDD", "#22B07A", "#0B6045"),
        Rank(25, ProgressEngine.levelTitle(25), "Safira", "#C2DEFF", "#3D7BF2", "#173C99"),
        Rank(30, ProgressEngine.levelTitle(30), "Ametista", "#EBCBFF", "#9B4BE0", "#53208A"),
        Rank(35, ProgressEngine.levelTitle(35), "Rubi", "#FFC2D0", "#E5375F", "#8A1631"),
        Rank(40, ProgressEngine.levelTitle(40), "Platina", "#FFFFFF", "#BFE0EC", "#5D8494"),
        Rank(45, ProgressEngine.levelTitle(45), "Diamante", "#E2FFFF", "#3FD8F0", "#0E7C9A"),
        Rank(50, ProgressEngine.levelTitle(50), "Lendário", "#FFE9A8", "#FF7A59", "#7A1FB0"),
    )

    fun rankIndex(level: Int): Int = ranks.indexOfLast { level >= it.from }.coerceAtLeast(0)
    fun rank(level: Int): Rank = ranks[rankIndex(level)]

    /** Estrelas do brasão: quantos níveis a pessoa já subiu dentro do título (até 5). */
    fun stars(level: Int): Int = (level - rank(level).from + 1).coerceIn(1, 5)

    /** Se este nível abre um título novo (a patente muda). */
    fun isPromotion(level: Int): Boolean = level > 1 && ranks.any { it.from == level }

    /**
     * O brasão em SVG (viewBox 240 x 240). [id] separa os degradês quando há mais de um brasão na
     * mesma página; [small] tira os detalhes finos (para tamanhos de ícone).
     */
    fun svg(level: Int, id: String = "lc", small: Boolean = false): String {
        val tier = rankIndex(level)
        val r = ranks[tier]
        val sb = StringBuilder()
        sb.append("""<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 240 240" width="240" height="240">""")
        sb.append("<defs>")
        sb.append("""<linearGradient id="$id-m" x1="0" y1="0" x2="0.35" y2="1"><stop offset="0" stop-color="${r.light}"/><stop offset=".48" stop-color="${r.mid}"/><stop offset="1" stop-color="${r.dark}"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-p" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#2A2470"/><stop offset="1" stop-color="#0F0C2E"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-g" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#FFF3B8"/><stop offset=".5" stop-color="#FFC22E"/><stop offset="1" stop-color="#B86E00"/></linearGradient>""")
        sb.append("""<radialGradient id="$id-h" cx=".5" cy=".5" r=".5"><stop offset="0" stop-color="${r.mid}" stop-opacity=".55"/><stop offset="1" stop-color="${r.mid}" stop-opacity="0"/></radialGradient>""")
        sb.append("</defs>")

        // brilho de fundo
        sb.append("""<circle cx="120" cy="124" r="112" fill="url(#$id-h)"/>""")
        // raios (Aprovável em diante)
        if (tier >= 8) {
            for (i in 0 until 12) {
                val a = i * 30.0
                sb.append("""<path d="M120 124 L${px(120, 124, 116, a - 4)} L${px(120, 124, 116, a + 4)} Z" fill="${r.light}" opacity=".22"/>""")
            }
        }
        // asas (Concurseiro em diante)
        if (tier >= 2) {
            val wing = "M80 100 L14 72 L30 106 L6 114 L36 138 L20 160 L82 150 Z"
            sb.append("""<g fill="url(#$id-m)" stroke="${r.dark}" stroke-width="3" stroke-linejoin="round"><path d="$wing"/><path d="$wing" transform="translate(240 0) scale(-1 1)"/></g>""")
            if (!small) sb.append("""<g stroke="${r.dark}" stroke-width="2" opacity=".55" fill="none"><path d="M74 108 L34 104 M74 124 L32 132 M76 140 L42 154"/><path d="M166 108 L206 104 M166 124 L208 132 M164 140 L198 154"/></g>""")
        }
        // louros (Especialista em diante)
        if (tier >= 7 && !small) {
            // folhas num arco do lado esquerdo (de baixo para cima), espelhadas à direita
            for (i in 0 until 6) {
                val a = 100.0 + i * 18.0
                val x = (120 + 100 * cos(a * PI / 180)).roundToInt()
                val y = (130 + 100 * sin(a * PI / 180)).roundToInt()
                val rot = (a + 35).roundToInt()
                sb.append("""<ellipse cx="$x" cy="$y" rx="5" ry="13" transform="rotate($rot $x $y)" fill="url(#$id-g)" stroke="#8A5600" stroke-width="1.5"/>""")
                sb.append("""<ellipse cx="${240 - x}" cy="$y" rx="5" ry="13" transform="rotate(${-rot} ${240 - x} $y)" fill="url(#$id-g)" stroke="#8A5600" stroke-width="1.5"/>""")
            }
        }
        // escudo
        sb.append("""<path d="M120 30 L188 58 L188 128 Q188 182 120 212 Q52 182 52 128 L52 58 Z" fill="url(#$id-m)" stroke="${r.dark}" stroke-width="4" stroke-linejoin="round"/>""")
        sb.append("""<path d="M120 46 L174 68 L174 126 Q174 168 120 194 Q66 168 66 126 L66 68 Z" fill="url(#$id-p)" stroke="${r.light}" stroke-opacity=".55" stroke-width="2"/>""")
        if (!small) sb.append("""<path d="M66 68 L120 46 L146 57 L72 146 L66 126 Z" fill="#FFFFFF" opacity=".09"/>""")
        // topo: joia de Estudante a Disciplinado; coroa de Estrategista em diante
        if (tier >= 4) {
            sb.append("""<path d="M86 40 L92 12 L108 30 L120 4 L132 30 L148 12 L154 40 Z" fill="url(#$id-g)" stroke="#8A5600" stroke-width="2.5" stroke-linejoin="round"/>""")
            sb.append("""<circle cx="120" cy="30" r="5" fill="${r.light}" stroke="${r.dark}" stroke-width="2"/>""")
        } else if (tier >= 1) {
            sb.append("""<path d="M120 14 L132 32 L120 48 L108 32 Z" fill="${r.light}" stroke="${r.dark}" stroke-width="3" stroke-linejoin="round"/>""")
        }
        // número
        val digits = level.toString()
        val fs = when (digits.length) { 1 -> 70; 2 -> 60; else -> 46 }
        sb.append("""<text x="120" y="${132 + fs / 3}" text-anchor="middle" font-family="Nunito, Arial, Helvetica, sans-serif" font-weight="900" font-size="$fs" fill="#FFFFFF" stroke="${r.dark}" stroke-width="7" stroke-linejoin="round" paint-order="stroke">$digits</text>""")
        // estrelas
        val n = stars(level)
        val gap = 17
        val start = 120 - (n - 1) * gap / 2.0
        for (i in 0 until n) sb.append("""<path d="${star(start + i * gap, 176.0, 7.5)}" fill="url(#$id-g)" stroke="#8A5600" stroke-width="1.2" stroke-linejoin="round"/>""")
        sb.append("</svg>")
        return sb.toString()
    }

    private fun px(cx: Int, cy: Int, radius: Int, deg: Double): String {
        val a = deg * PI / 180
        return "${(cx + radius * cos(a)).roundToInt()} ${(cy + radius * sin(a)).roundToInt()}"
    }

    private fun star(cx: Double, cy: Double, r: Double): String {
        val pts = (0 until 10).map { i ->
            val rr = if (i % 2 == 0) r else r * 0.45
            val a = (-90 + i * 36) * PI / 180
            "${((cx + rr * cos(a)) * 10).roundToInt() / 10.0} ${((cy + rr * sin(a)) * 10).roundToInt() / 10.0}"
        }
        return "M" + pts.joinToString(" L") + " Z"
    }
}
