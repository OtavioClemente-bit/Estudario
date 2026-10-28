package br.com.estudario.domain

/**
 * A fênix da sequência, no traço da turma do Estudário (a chama de rosto fofo, contorno de caneta),
 * com asas, cauda de penas de fogo, crista e bico. Dormindo ela fica cinza de cinza, de olhos
 * fechados; acordada, pega fogo. Sai em partes para o site e o app baterem as asas: [Part.WING_L] gira
 * em torno de [WING_L_PIVOT] e [Part.WING_R] em torno de [WING_R_PIVOT] (viewBox 200 x 200).
 */
object Phoenix {
    enum class Part { TAIL, WING_L, WING_R, BODY }

    val WING_L_PIVOT = 88f to 112f
    val WING_R_PIVOT = 112f to 112f

    private const val INK = "#302544"

    private data class Palette(val f0: String, val f1: String, val f2: String, val in0: String, val in1: String, val w0: String, val w1: String)

    private val fire = Palette("#FF4D2E", "#FF9A1F", "#FFD84A", "#FFB13B", "#FFF4B8", "#FF6A2B", "#FFC93C")
    private val ash = Palette("#4E4A68", "#6E6A8A", "#9C98B8", "#77738F", "#B3AFCB", "#4A4664", "#7F7A9C")

    /** Asa esquerda (a direita é o espelho): penas em chama, da raiz no ombro até a ponta lá em cima. */
    private const val WING = "M90 122 C76 124 58 118 46 106 C36 96 28 80 26 60 C36 70 42 74 50 76 C46 64 46 52 52 38 C58 54 62 62 68 68 C70 58 74 52 80 48 C80 66 84 84 92 100 Z"
    private const val WING_IN = "M88 118 C78 118 64 114 56 104 C48 96 44 86 44 76 C52 84 58 86 64 88 C62 80 62 72 66 64 C70 74 74 80 80 84 C82 92 86 100 90 108 Z"
    /** O corpo: a chama da turma, com a ponta virando crista. */
    private const val BODY = "M100 34 C108 52 132 68 136 100 C140 132 122 158 100 158 C78 158 60 132 64 100 C66 84 74 74 82 66 C84 78 88 84 94 88 C90 72 92 52 100 34 Z"
    private const val BODY_IN = "M100 92 C106 102 120 110 120 128 C120 142 110 150 100 150 C90 150 80 142 80 128 C80 116 88 110 92 106 C94 112 96 114 98 116 C96 108 97 100 100 92 Z"
    private const val CREST = "M95 50 C88 44 86 36 89 26 C93 34 97 38 100 41 Z M105 50 C112 44 114 36 111 26 C107 34 103 38 100 41 Z"
    private const val TAIL = "M88 150 C80 166 70 178 56 186 C72 186 82 182 90 174 C88 184 90 192 96 198 C100 188 102 180 100 170 C104 180 110 186 122 190 C116 178 116 168 112 150 Z"

    /** O SVG inteiro (ou só as [parts] pedidas), acordada ou dormindo. [id] separa os degradês na página. */
    fun svg(awake: Boolean, id: String = "px", parts: Set<Part> = Part.entries.toSet()): String {
        val p = if (awake) fire else ash
        val sb = StringBuilder()
        sb.append("""<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200" width="200" height="200">""")
        sb.append("<defs>")
        sb.append("""<linearGradient id="$id-f" x1="0" y1="1" x2="0" y2="0"><stop offset="0" stop-color="${p.f0}"/><stop offset=".55" stop-color="${p.f1}"/><stop offset="1" stop-color="${p.f2}"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-i" x1="0" y1="1" x2="0" y2="0"><stop offset="0" stop-color="${p.in0}"/><stop offset="1" stop-color="${p.in1}"/></linearGradient>""")
        sb.append("""<linearGradient id="$id-w" x1="1" y1="1" x2="0" y2="0"><stop offset="0" stop-color="${p.w0}"/><stop offset="1" stop-color="${p.w1}"/></linearGradient>""")
        sb.append("</defs>")
        if (Part.TAIL in parts) {
            sb.append("""<g class="px-tail"><path d="$TAIL" fill="url(#$id-w)" stroke="$INK" stroke-width="3" stroke-linejoin="round"/>""")
            sb.append("""<path d="M92 156 C88 166 84 172 78 178 M100 158 C100 168 98 176 96 184 M108 156 C110 166 112 172 116 178" fill="none" stroke="$INK" stroke-width="1.6" stroke-linecap="round" opacity=".45"/></g>""")
        }
        if (Part.WING_L in parts) sb.append(wing(id, mirror = false))
        if (Part.WING_R in parts) sb.append(wing(id, mirror = true))
        if (Part.BODY in parts) {
            sb.append("""<g class="px-body">""")
            sb.append("""<path d="$CREST" fill="url(#$id-f)" stroke="$INK" stroke-width="2.6" stroke-linejoin="round"/>""")
            sb.append("""<path d="$BODY" fill="url(#$id-f)" stroke="$INK" stroke-width="3.2" stroke-linejoin="round"/>""")
            sb.append("""<path d="$BODY_IN" fill="url(#$id-i)" opacity=".9"/>""")
            // rosto: olhos (abertos com brilho, ou fechados dormindo), bochechas e bico
            if (awake) {
                sb.append("""<ellipse cx="88" cy="108" rx="5.5" ry="7" fill="$INK"/><ellipse cx="112" cy="108" rx="5.5" ry="7" fill="$INK"/>""")
                sb.append("""<circle cx="90" cy="105" r="2" fill="#FFFFFF"/><circle cx="114" cy="105" r="2" fill="#FFFFFF"/>""")
                sb.append("""<ellipse cx="78" cy="120" rx="6" ry="3.5" fill="#FF6B8A" opacity=".55"/><ellipse cx="122" cy="120" rx="6" ry="3.5" fill="#FF6B8A" opacity=".55"/>""")
            } else {
                sb.append("""<path d="M82 108 Q88 113 94 108 M106 108 Q112 113 118 108" fill="none" stroke="$INK" stroke-width="2.6" stroke-linecap="round"/>""")
                sb.append("""<ellipse cx="78" cy="120" rx="6" ry="3.5" fill="#B3AFCB" opacity=".5"/><ellipse cx="122" cy="120" rx="6" ry="3.5" fill="#B3AFCB" opacity=".5"/>""")
            }
            sb.append("""<path d="M95 116 L105 116 L100 124 Z" fill="${if (awake) "#FFB421" else "#9C98B8"}" stroke="$INK" stroke-width="2" stroke-linejoin="round"/>""")
            sb.append("</g>")
        }
        sb.append("</svg>")
        return sb.toString()
    }

    private fun wing(id: String, mirror: Boolean): String {
        val cls = if (mirror) "px-wing-r" else "px-wing-l"
        val tf = if (mirror) """ transform="translate(200 0) scale(-1 1)"""" else ""
        return """<g class="$cls"><g$tf><path d="$WING" fill="url(#$id-w)" stroke="$INK" stroke-width="3" stroke-linejoin="round"/><path d="$WING_IN" fill="url(#$id-i)" opacity=".55"/>""" +
            """<path d="M86 112 C72 106 58 96 50 84 M88 116 C74 114 62 108 54 100" fill="none" stroke="$INK" stroke-width="1.6" stroke-linecap="round" opacity=".4"/></g></g>"""
    }
}
