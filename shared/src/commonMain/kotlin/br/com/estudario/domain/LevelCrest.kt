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

    private const val GOLD_EDGE = "#8A5200"

    /**
     * O brasão em SVG (viewBox 240 x 240). [id] separa os degradês quando há mais de um brasão na
     * mesma página; [small] tira os detalhes finos (para tamanhos de ícone).
     * Escudo de metal com borda chanfrada e miolo escuro, o número no meio e, embaixo, a faixa com as
     * cinco casas de estrela do título (as acesas são os níveis já subidos nele, as outras ficam vazias).
     */
    fun svg(level: Int, id: String = "lc", small: Boolean = false): String {
        val tier = rankIndex(level)
        val r = ranks[tier]
        val sb = StringBuilder()
        sb.append("""<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 240 240" width="240" height="240">""")
        sb.append("<defs>")
        sb.append("""<linearGradient id="$id-m" x1="0" y1="0" x2="0.35" y2="1"><stop offset="0" stop-color="${r.light}"/><stop offset=".5" stop-color="${r.mid}"/><stop offset="1" stop-color="${r.dark}"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-b" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#FFFFFF" stop-opacity=".85"/><stop offset=".45" stop-color="#FFFFFF" stop-opacity=".15"/><stop offset="1" stop-color="${r.dark}" stop-opacity=".9"/></linearGradient>""")
        sb.append("""<radialGradient id="$id-p" cx=".5" cy=".35" r=".75"><stop offset="0" stop-color="#3D3399"/><stop offset="1" stop-color="#120E36"/></radialGradient>""")
        sb.append("""<linearGradient id="$id-g" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#FFF6C4"/><stop offset=".45" stop-color="#FFC933"/><stop offset="1" stop-color="#C77A00"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-r" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${r.mid}"/><stop offset="1" stop-color="${r.dark}"/></linearGradient>""")
        sb.append("""<radialGradient id="$id-h" cx=".5" cy=".5" r=".5"><stop offset="0" stop-color="${r.mid}" stop-opacity=".5"/><stop offset="1" stop-color="${r.mid}" stop-opacity="0"/></radialGradient>""")
        sb.append("</defs>")

        // brilho de fundo
        sb.append("""<circle cx="120" cy="120" r="114" fill="url(#$id-h)"/>""")
        // raios (Aprovável em diante)
        if (tier >= 8) {
            for (i in 0 until 12) {
                val a = i * 30.0
                sb.append("""<path d="M120 118 L${px(120.0, 118.0, 116.0, a - 4)} L${px(120.0, 118.0, 116.0, a + 4)} Z" fill="${r.light}" opacity=".2"/>""")
            }
        }
        // louros (Especialista em diante): folhas num arco à esquerda, espelhadas à direita
        if (tier >= 7 && !small) {
            for (i in 0 until 7) {
                val deg = 105.0 + i * 16
                val x = n1(120 + 98 * cos(deg * PI / 180))
                val y = n1(122 + 98 * sin(deg * PI / 180))
                val rot = (deg + 40).roundToInt()
                sb.append(leaf(x, y, rot, id))
                sb.append(leaf(n1(240 - x), y, -rot, id))
            }
        }
        // asas (Concurseiro em diante): três penas de cada lado, atrás do escudo
        if (tier >= 2) {
            val wing = "M66 70 C44 58 22 54 4 60 C18 72 36 82 58 90 C36 88 18 92 6 102 C24 110 42 112 58 110 C42 114 28 124 20 138 C38 138 52 132 62 126 Z"
            sb.append("""<g fill="url(#$id-m)" stroke="${r.dark}" stroke-width="3" stroke-linejoin="round"><path d="$wing"/><path d="$wing" transform="translate(240 0) scale(-1 1)"/></g>""")
            if (!small) sb.append("""<g stroke="${r.dark}" stroke-width="2" stroke-linecap="round" opacity=".5" fill="none"><path d="M60 82 Q36 72 16 64 M58 104 Q36 100 16 102 M60 122 Q44 126 30 134"/><path d="M180 82 Q204 72 224 64 M182 104 Q204 100 224 102 M180 122 Q196 126 210 134"/></g>""")
        }
        // escudo: borda de metal, chanfro (claro em cima, escuro embaixo) e o miolo escuro
        sb.append("""<path d="M120 24 L192 52 L192 122 Q192 180 120 212 Q48 180 48 122 L48 52 Z" fill="url(#$id-m)" stroke="${r.dark}" stroke-width="4" stroke-linejoin="round"/>""")
        sb.append("""<path d="M120 32 L185 57 L185 122 Q185 175 120 204 Q55 175 55 122 L55 57 Z" fill="none" stroke="url(#$id-b)" stroke-width="3"/>""")
        sb.append("""<path d="M120 44 L174 65 L174 120 Q174 165 120 190 Q66 165 66 120 L66 65 Z" fill="url(#$id-p)" stroke="${r.dark}" stroke-width="3"/>""")
        if (!small) {
            sb.append("""<path d="M70 68 L120 48 L148 59 L76 138 L70 120 Z" fill="#FFFFFF" opacity=".08"/>""")
            for (x in listOf(60, 180)) sb.append("""<circle cx="$x" cy="62" r="4" fill="url(#$id-g)" stroke="${r.dark}" stroke-width="1.5"/>""")
        }
        // topo: coroa de Estrategista em diante; antes, uma joia lapidada num engaste de ouro
        if (tier >= 4) {
            sb.append("""<path d="M86 44 L82 14 L101 28 L120 6 L139 28 L158 14 L154 44 Z" fill="url(#$id-g)" stroke="$GOLD_EDGE" stroke-width="2.5" stroke-linejoin="round"/>""")
            sb.append("""<path d="M84 36 L156 36 L154 46 L86 46 Z" fill="#E09A12" stroke="$GOLD_EDGE" stroke-width="2.5" stroke-linejoin="round"/>""")
            for ((x, y) in listOf(82 to 14, 120 to 6, 158 to 14)) sb.append("""<circle cx="$x" cy="$y" r="4.5" fill="url(#$id-g)" stroke="$GOLD_EDGE" stroke-width="2"/>""")
            sb.append("""<path d="M120 33 L126 41 L120 49 L114 41 Z" fill="${r.light}" stroke="${r.dark}" stroke-width="2"/>""")
            for (x in listOf(98, 142)) sb.append("""<circle cx="$x" cy="41" r="3" fill="${r.mid}" stroke="${r.dark}" stroke-width="1.5"/>""")
        } else if (tier >= 1) {
            sb.append("""<path d="M100 34 Q120 24 140 34 L136 44 Q120 37 104 44 Z" fill="url(#$id-g)" stroke="$GOLD_EDGE" stroke-width="2.2" stroke-linejoin="round"/>""")
            sb.append("""<path d="M100 18 L110 7 L130 7 L140 18 L120 44 Z" fill="${r.mid}"/>""")
            sb.append("""<path d="M110 7 L130 7 L125 18 L115 18 Z" fill="${r.light}"/>""")
            sb.append("""<path d="M100 18 L115 18 L120 44 Z" fill="${r.light}" opacity=".55"/>""")
            sb.append("""<path d="M125 18 L140 18 L120 44 Z" fill="${r.dark}" opacity=".7"/>""")
            sb.append("""<path d="M100 18 L110 7 L130 7 L140 18 L120 44 Z" fill="none" stroke="${r.dark}" stroke-width="2.5" stroke-linejoin="round"/>""")
            if (!small) sb.append("""<path d="M113 10 L116 12 M104 17 L139 17" stroke="#FFFFFF" stroke-width="1.6" stroke-linecap="round" opacity=".7"/>""")
        }
        // número: contorno embaixo e branco em cima (dois textos: o AndroidSVG não entende paint-order)
        val digits = level.toString()
        val fs = when (digits.length) { 1 -> 74; 2 -> 62; else -> 48 }
        val font = """x="120" y="${(114 + fs * 0.36).roundToInt()}" text-anchor="middle" font-family="Nunito, Arial, Helvetica, sans-serif" font-weight="900" font-size="$fs""""
        sb.append("""<text $font fill="${r.dark}" stroke="${r.dark}" stroke-width="9" stroke-linejoin="round">$digits</text>""")
        sb.append("""<text $font fill="#FFFFFF">$digits</text>""")
        // faixa: pontas dobradas atrás e a fita curva com as cinco casas de estrela
        sb.append("""<path d="M46 184 L18 186 L30 198 L18 212 L50 210 Z" fill="${r.dark}" stroke="${r.dark}" stroke-width="3" stroke-linejoin="round"/>""")
        sb.append("""<path d="M194 184 L222 186 L210 198 L222 212 L190 210 Z" fill="${r.dark}" stroke="${r.dark}" stroke-width="3" stroke-linejoin="round"/>""")
        sb.append("""<path d="M40 180 Q120 194 200 180 L200 208 Q120 222 40 208 Z" fill="url(#$id-r)" stroke="${r.dark}" stroke-width="3" stroke-linejoin="round"/>""")
        if (!small) sb.append("""<path d="M44 184 Q120 197 196 184" fill="none" stroke="${r.light}" stroke-opacity=".6" stroke-width="2"/>""")
        val lit = stars(level)
        for (i in 0 until 5) {
            val x = 72.0 + i * 24
            val u = (x - 120) / 80
            val cy = n1(194 + 7 * (1 - u * u) + 0.5)
            if (i < lit) {
                sb.append("""<path d="${star(x, cy, 11.0)}" fill="url(#$id-g)" stroke="#7A4600" stroke-width="1.6" stroke-linejoin="round"/>""")
                if (!small) sb.append("""<path d="${star(x, cy - 1.5, 4.2)}" fill="#FFFBE0" opacity=".75"/>""")
            } else {
                sb.append("""<path d="${star(x, cy, 10.0)}" fill="#000000" fill-opacity=".35" stroke="${r.light}" stroke-opacity=".35" stroke-width="1.4" stroke-linejoin="round"/>""")
            }
        }
        sb.append("</svg>")
        return sb.toString()
    }

    /** Uma folha de louro (em pé, girada [rot] graus em volta do centro). */
    private fun leaf(x: Double, y: Double, rot: Int, id: String): String =
        """<path d="M$x ${n1(y - 14)} Q${n1(x + 7)} $y $x ${n1(y + 14)} Q${n1(x - 7)} $y $x ${n1(y - 14)} Z" transform="rotate($rot $x $y)" fill="url(#$id-g)" stroke="$GOLD_EDGE" stroke-width="1.6"/>"""

    private fun n1(v: Double): Double = (v * 10).roundToInt() / 10.0

    private fun px(cx: Double, cy: Double, radius: Double, deg: Double): String {
        val a = deg * PI / 180
        return "${n1(cx + radius * cos(a))} ${n1(cy + radius * sin(a))}"
    }

    private fun star(cx: Double, cy: Double, r: Double): String {
        val pts = (0 until 10).map { i ->
            val rr = if (i % 2 == 0) r else r * 0.48
            val a = (-90 + i * 36) * PI / 180
            "${n1(cx + rr * cos(a))} ${n1(cy + rr * sin(a))}"
        }
        return "M" + pts.joinToString(" L") + " Z"
    }
}
