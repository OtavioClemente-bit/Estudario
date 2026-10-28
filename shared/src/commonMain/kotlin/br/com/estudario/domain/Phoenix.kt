package br.com.estudario.domain

/**
 * A fênix da sequência, com o contorno de caneta da turma do Estudário: uma ave de verdade, de peito
 * erguido, cabeça com bico curvo e olhar firme, crista de três plumas, asas abertas em leque de penas
 * longas e cauda de cinco plumas com "olhos" dourados na ponta. Acordada ela é fogo (carmim, laranja e
 * ouro, com chamas nas pontas); dormindo é cinza, de olhos fechados e asas caídas em volta do corpo.
 *
 * Sai em partes para o site e o app baterem as asas: [Part.WING_L] gira em torno de [WING_L_PIVOT] e
 * [Part.WING_R] em torno de [WING_R_PIVOT] (viewBox 200 x 200).
 */
object Phoenix {
    enum class Part { TAIL, WING_L, WING_R, BODY }

    val WING_L_PIVOT = 86f to 100f
    val WING_R_PIVOT = 114f to 100f

    private const val INK = "#302544"

    private data class Palette(
        val bodyTop: String, val bodyMid: String, val bodyLow: String,
        val wingRoot: String, val wingMid: String, val wingTip: String,
        val covertA: String, val covertB: String,
        val tailA: String, val tailB: String, val eyeRing: String, val eyeCore: String,
        val beak: String, val chest: String,
    )

    private val fire = Palette(
        "#FFE27A", "#FF9A1F", "#E8401C",
        "#FFD24A", "#FF7A1A", "#C8261A",
        "#FFF1B8", "#FFB43A",
        "#FF8A1F", "#C8261A", "#FFE27A", "#B0124A",
        "#FFC23A", "#FFF4C8",
    )
    private val ash = Palette(
        "#B3AFCB", "#8F8AAE", "#5E5A7C",
        "#9C98B8", "#77738F", "#4E4A68",
        "#C9C5DD", "#8F8AAE",
        "#7F7A9C", "#4E4A68", "#B3AFCB", "#4E4A68",
        "#9C98B8", "#D6D3E6",
    )

    // ---- a asa esquerda (a direita é o espelho): do ombro sobe pelo bordo até a ponta lá em cima e
    // volta pelas penas longas, uma a uma, até o flanco
    private const val WING = "M86 96 C76 82 66 70 58 58 C46 46 30 36 12 28 C18 40 22 46 30 52 C20 50 14 52 8 56 C18 62 26 64 34 66 C24 68 16 72 12 78 C24 80 32 80 40 80 C32 86 26 92 24 100 C36 98 46 96 52 94 C48 102 46 110 48 118 C58 112 66 108 72 106 C74 114 78 120 86 124 Z"
    // penas menores (coberteiras), mais claras, perto do ombro
    private const val COVERTS = "M86 100 C78 88 68 78 60 68 C54 76 56 84 62 88 C56 94 58 100 64 102 C62 108 66 112 72 112 C76 116 80 118 86 118 Z"
    // as hastes das penas longas
    private const val SHAFTS = "M58 60 L18 34 M56 64 L14 56 M58 70 L18 78 M62 80 L30 98 M68 90 L52 114"
    // (sem chamas soltas nas pontas: pareciam luas soltas)
    @Suppress("unused") private const val WING_FLAMES = "M12 28 C6 20 8 12 14 6 C14 14 18 18 22 20 C18 22 14 24 12 28 Z M8 56 C0 52 -2 44 2 38 C4 46 8 48 12 50 Z M12 78 C4 78 0 72 0 66 C4 72 8 74 14 74 Z"

    // ---- o corpo: peito erguido, pescoço e cabeça
    private const val TORSO = "M100 76 C116 78 126 94 124 116 C122 136 112 150 100 158 C88 150 78 136 76 116 C74 94 84 78 100 76 Z"
    private const val CHEST = "M100 92 C110 94 116 104 114 118 C112 132 106 142 100 146 C94 142 88 132 86 118 C84 104 90 94 100 92 Z"
    private const val CHEST_SCALES = "M90 108 Q95 112 100 108 Q105 112 110 108 M88 120 Q94 124 100 120 Q106 124 112 120 M91 132 Q95.5 136 100 132 Q104.5 136 109 132"
    private const val HEAD = "M100 40 C113 40 120 50 119 61 C118 72 110 80 100 80 C90 80 82 72 81 61 C80 50 87 40 100 40 Z"
    // a crista: três plumas que sobem da cabeça, com a ponta em gota
    private const val CREST = "M96 44 C90 34 84 26 80 16 C78 10 84 6 88 12 C92 20 96 32 99 43 Z M100 42 C99 30 99 18 100 6 C101 0 107 0 107 6 C106 18 104 30 102 42 Z M104 44 C108 32 112 20 116 12 C120 6 126 10 124 16 C120 26 114 34 106 45 Z"
    private const val BEAK = "M93 64 Q100 60 107 64 Q104 74 100 80 Q96 74 93 64 Z"
    private const val BEAK_LINE = "M95 66 Q100 69 105 66"

    // ---- a cauda: cinco plumas longas caindo, com "olho" na ponta
    private const val TAIL = "M94 150 C90 172 94 188 100 199 C106 188 110 172 106 150 Z " +
        "M94 148 C82 164 68 178 52 192 C64 196 80 188 90 176 C94 168 97 158 99 152 Z " +
        "M106 148 C118 164 132 178 148 192 C136 196 120 188 110 176 C106 168 103 158 101 152 Z " +
        "M92 146 C74 154 54 160 32 166 C42 180 62 178 78 168 C86 162 92 156 96 150 Z " +
        "M108 146 C126 154 146 160 168 166 C158 180 138 178 122 168 C114 162 108 156 104 150 Z"
    private val TAIL_EYES = listOf(100f to 189f, 60f to 186f, 140f to 186f, 40f to 170f, 160f to 170f)

    /** O SVG inteiro (ou só as [parts] pedidas), acordada ou dormindo. [id] separa os degradês na página. */
    fun svg(awake: Boolean, id: String = "px", parts: Set<Part> = Part.entries.toSet()): String {
        val p = if (awake) fire else ash
        val sb = StringBuilder()
        sb.append("""<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200" width="200" height="200">""")
        sb.append("<defs>")
        sb.append("""<linearGradient id="$id-b" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${p.bodyTop}"/><stop offset=".5" stop-color="${p.bodyMid}"/><stop offset="1" stop-color="${p.bodyLow}"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-w" x1="1" y1="1" x2="0" y2="0"><stop offset="0" stop-color="${p.wingRoot}"/><stop offset=".5" stop-color="${p.wingMid}"/><stop offset="1" stop-color="${p.wingTip}"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-c" x1="1" y1="1" x2="0" y2="0"><stop offset="0" stop-color="${p.covertA}"/><stop offset="1" stop-color="${p.covertB}"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-t" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${p.tailA}"/><stop offset="1" stop-color="${p.tailB}"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-fl" x1="0" y1="1" x2="0" y2="0"><stop offset="0" stop-color="#FF5A1F"/><stop offset=".6" stop-color="#FFB421"/><stop offset="1" stop-color="#FFF2A8"/></linearGradient>""")
        sb.append("</defs>")
        if (Part.TAIL in parts) {
            sb.append("""<g class="px-tail"><path d="$TAIL" fill="url(#$id-t)" stroke="$INK" stroke-width="2.6" stroke-linejoin="round"/>""")
            TAIL_EYES.forEach { (x, y) ->
                sb.append("""<circle cx="$x" cy="$y" r="5.5" fill="${p.eyeRing}" stroke="$INK" stroke-width="1.6"/><circle cx="$x" cy="$y" r="2.4" fill="${p.eyeCore}"/>""")
            }
            sb.append("</g>")
        }
        if (Part.WING_L in parts) sb.append(wing(id, p, awake, mirror = false))
        if (Part.WING_R in parts) sb.append(wing(id, p, awake, mirror = true))
        if (Part.BODY in parts) {
            sb.append("""<g class="px-body">""")
            sb.append("""<path d="$CREST" fill="url(#$id-w)" stroke="$INK" stroke-width="2.4" stroke-linejoin="round"/>""")
            if (awake) sb.append("""<g class="px-flame"><path d="M80 16 C74 8 76 0 82 -4 C82 4 86 8 90 10 Z M100 6 C96 -2 98 -10 104 -14 C104 -6 108 -2 110 0 Z M124 16 C130 8 128 0 122 -4 C122 4 118 8 114 10 Z" fill="url(#$id-fl)"/></g>""")
            sb.append("""<path d="$TORSO" fill="url(#$id-b)" stroke="$INK" stroke-width="3" stroke-linejoin="round"/>""")
            sb.append("""<path d="$CHEST" fill="${p.chest}" opacity=".55"/>""")
            sb.append("""<path d="$CHEST_SCALES" fill="none" stroke="$INK" stroke-width="1.4" stroke-linecap="round" opacity=".35"/>""")
            sb.append("""<path d="$HEAD" fill="url(#$id-b)" stroke="$INK" stroke-width="3" stroke-linejoin="round"/>""")
            if (awake) {
                // olhos de ave: amêndoa com brilho e a sobrancelha marcada (olhar firme, mas fofo)
                sb.append("""<ellipse cx="90.5" cy="57" rx="4.6" ry="5.6" fill="$INK"/><ellipse cx="109.5" cy="57" rx="4.6" ry="5.6" fill="$INK"/><path d="M85 55 L82 52 M115 55 L118 52" stroke="$INK" stroke-width="2" stroke-linecap="round"/>""")
                sb.append("""<circle cx="92" cy="55" r="1.9" fill="#FFFFFF"/><circle cx="111" cy="55" r="1.9" fill="#FFFFFF"/>""")
                sb.append("""<ellipse cx="86" cy="66" rx="4.5" ry="2.6" fill="#FF6B8A" opacity=".5"/><ellipse cx="114" cy="66" rx="4.5" ry="2.6" fill="#FF6B8A" opacity=".5"/>""")
            } else {
                sb.append("""<path d="M85 57 Q90.5 61 96 57 M104 57 Q109.5 61 115 57" fill="none" stroke="$INK" stroke-width="2.4" stroke-linecap="round"/>""")
            }
            sb.append("""<path d="$BEAK" fill="${p.beak}" stroke="$INK" stroke-width="2" stroke-linejoin="round"/><path d="$BEAK_LINE" fill="none" stroke="$INK" stroke-width="1.2" stroke-linecap="round" opacity=".6"/>""")
            sb.append("</g>")
        }
        sb.append("</svg>")
        return sb.toString()
    }

    private fun wing(id: String, p: Palette, awake: Boolean, mirror: Boolean): String {
        val cls = if (mirror) "px-wing-r" else "px-wing-l"
        val (px, py) = WING_L_PIVOT
        // dormindo, as asas caem e se fecham em volta do corpo
        val fold = if (awake) "" else """ transform="translate($px $py) rotate(-104) scale(.72) translate(-$px -$py)""""
        val flip = if (mirror) """ transform="translate(200 0) scale(-1 1)"""" else ""
        val sb = StringBuilder()
        sb.append("""<g class="$cls"><g$flip><g$fold>""")
        sb.append("""<path d="$WING" fill="url(#$id-w)" stroke="$INK" stroke-width="2.8" stroke-linejoin="round"/>""")
        sb.append("""<path d="$SHAFTS" fill="none" stroke="$INK" stroke-width="1.4" stroke-linecap="round" opacity=".35"/>""")
        sb.append("""<path d="$COVERTS" fill="url(#$id-c)" stroke="$INK" stroke-width="2" stroke-linejoin="round"/>""")
        sb.append("</g></g></g>")
        return sb.toString()
    }
}
