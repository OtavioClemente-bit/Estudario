package br.com.estudario.domain

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sin

/**
 * O livro de Folhas: o desenho do nível, em SVG, igual no app (AndroidSVG) e no site (no próprio
 * navegador). Cada nível é uma folha a mais, então o livro engorda; a cada título a capa ganha
 * acabamento (do caderno pardo à capa de ouro com o ipê) e o número fica numa medalha que muda de
 * metal (bronze, prata, ouro, ouro radiante).
 *
 * Tamanhos: normal (Perfil, 88 px ou mais), [Mode.SMALL] (cartão, galeria: medalha simples e número
 * maior) e [Mode.TINY] (avatar, Início, abaixo de 40 px: só a medalha).
 *
 * Sem SMIL nem paint-order: o AndroidSVG não anima nem conhece paint-order, então o brilho que pulsa
 * vem de [Frame.pulse] e o contorno do número é um segundo texto por baixo.
 */
object LevelBook {
    enum class Mode { NORMAL, SMALL, TINY }

    data class Tier(val from: Int, val title: String, val name: String)

    /** Os títulos são os do ProgressEngine; cada um ganha uma capa. */
    val tiers: List<Tier> = listOf(
        Tier(1, "Iniciante", "Caderno pardo"),
        Tier(5, "Estudante", "Caderno espiral"),
        Tier(10, "Concurseiro", "Brochura"),
        Tier(15, "Disciplinado", "Capa dura com fita"),
        Tier(20, "Estrategista", "Cantoneiras"),
        Tier(25, "Focado na vaga", "Duas fitas"),
        Tier(30, "Veterano", "Couro e dourado"),
        Tier(35, "Especialista", "Brasão gravado"),
        Tier(40, "Aprovável", "Fecho dourado"),
        Tier(45, "Quase servidor", "Gema no brasão"),
        Tier(50, "Lenda do edital", "Capa de ouro"),
    )

    fun tierOf(level: Int): Int = tiers.count { level >= it.from } - 1

    /** Um quadro do desenho: aberto (0 a 1), o número mostrado e o deslocamento dele ao girar, o brilho que passa. */
    data class Frame(
        val open: Double = 0.0,
        val number: Int? = null,
        val flip: Double = 0.0,
        val shine: Double? = null,
        /** 0 a 1, para o brilho de fundo pulsar sem SMIL. */
        val pulse: Double = 0.5,
    )

    private class Cover(val a: String, val b: String, val e: String, val leather: Boolean = false, val gold: Boolean = false)
    private val covers = listOf(
        Cover("#D8B48A", "#A97A4E", "#7E5634"),
        Cover("#8CC4F7", "#4D8FD4", "#2F5F96"),
        Cover("#7D6CF5", "#4232C9", "#2A1F8F"),
        Cover("#3DAA85", "#1E7457", "#124D39"),
        Cover("#3E73C9", "#1F4791", "#132E61"),
        Cover("#E0663A", "#A9361A", "#6E2210"),
        Cover("#9A2C50", "#5E1530", "#3A0B1D", leather = true),
        Cover("#2C3A7A", "#121A45", "#0A0F2B", leather = true),
        Cover("#1F6B42", "#0B3A22", "#062414", leather = true),
        Cover("#5B1A9E", "#2A0750", "#1A0333", leather = true),
        Cover("#FFE07A", "#D99A06", "#8A5A00", gold = true),
    )

    private class Medal(
        val a: String, val m: String, val b: String, val ink: String,
        val ribbon: Pair<String, String>, val laurel: Pair<String, String>? = null,
        val star: Boolean = false, val gems: Boolean = false, val glow: Boolean = false,
    )
    private fun medalOf(level: Int) = when {
        level >= 50 -> Medal("#FFF8D6", "#FFC93C", "#A86F00", "#5A3700", "#E5484D" to "#A61E24", "#FFD957" to "#7A4E00", star = true, gems = true, glow = true)
        level >= 30 -> Medal("#FFF4C2", "#F2B620", "#93620A", "#5A3700", "#5B4BE8" to "#3427A8", "#F5C542" to "#6E4700", star = true)
        level >= 15 -> Medal("#FFFFFF", "#CBD3E0", "#6E788B", "#2B3447", "#2FBF8A" to "#18805A", "#7FD3A8" to "#1E6B4C")
        else -> Medal("#FBDDBD", "#CD8A55", "#7E4519", "#4A230A", "#FFB421" to "#C77E00")
    }

    private var uid = 0

    /** O SVG inteiro (viewBox 0 0 120 120). */
    fun svg(level: Int, mode: Mode = Mode.NORMAL, frame: Frame = Frame()): String =
        "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 120 120\">" + body(level, mode, frame) + "</svg>"

    /** Só o conteúdo (sem a tag svg), para quem já tem o elemento. */
    fun body(level: Int, mode: Mode = Mode.NORMAL, frame: Frame = Frame()): String {
        val t = tierOf(level).coerceAtLeast(0)
        val c = covers[t]
        val md = medalOf(level)
        val id = "lb" + (uid++ % 100_000)
        val open = frame.open
        val pages = min(level, 60)
        val d = 4 + pages * 0.2
        val dx = d * 0.92
        val dy = d * 0.42
        val w = 58.0
        val h = 74.0
        val board = 1.8
        val x = 60 - (w + dx) / 2 - 1
        val y = 16 - dy / 2 + 4
        val r = if (t <= 1) 3.5 else 5.5
        val x2 = x + w
        val y2 = y + h
        val leatherish = c.leather || c.gold
        val s = StringBuilder()
        s.append("<defs>")
        s.lin("${id}cv", "0", "0", "1", "1", "0" to c.a, ".55" to c.b, "1" to c.e)
        s.lin("${id}back", "0", "0", "1", "1", "0" to c.b, "1" to c.e)
        s.lin("${id}spine", "0", "0", "1", "0", "0" to c.e, ".45" to c.b, ".7" to c.a, "1" to c.e)
        s.lin("${id}fore", "0", "0", "1", "0", "0" to "#FFFDF6", ".55" to "#F4EBD5", "1" to "#D9C79F")
        s.lin("${id}foot", "0", "0", "0", "1", "0" to "#F8F1DE", "1" to "#D6C398")
        s.lin("${id}gild", "0", "0", "1", "1", "0" to "#FFF4B8", ".4" to "#F5BE2A", ".75" to "#D89A0A", "1" to "#9C6A00")
        s.lin("${id}gold", "0", "0", "1", "1", "0" to "#FFF8D6", ".3" to "#FFD957", ".65" to "#E3A400", "1" to "#93620A")
        s.lin("${id}silver", "0", "0", "1", "1", "0" to "#FFFFFF", ".45" to "#D3D9E4", "1" to "#7A8497")
        s.append("<linearGradient id=\"${id}gloss\" x1=\"0\" y1=\"0\" x2=\"0\" y2=\"1\"><stop offset=\"0\" stop-color=\"#ffffff\" stop-opacity=\".38\"/><stop offset=\".4\" stop-color=\"#ffffff\" stop-opacity=\"0\"/></linearGradient>")
        s.append("<linearGradient id=\"${id}sh\" x1=\"0\" y1=\"0\" x2=\"1\" y2=\"0\"><stop offset=\"0\" stop-color=\"#ffffff\" stop-opacity=\"0\"/><stop offset=\".5\" stop-color=\"#ffffff\" stop-opacity=\".85\"/><stop offset=\"1\" stop-color=\"#ffffff\" stop-opacity=\"0\"/></linearGradient>")
        s.append("<radialGradient id=\"${id}aura\"><stop offset=\"0\" stop-color=\"#FFD43B\" stop-opacity=\".6\"/><stop offset=\"1\" stop-color=\"#FFD43B\" stop-opacity=\"0\"/></radialGradient>")
        s.append("<radialGradient id=\"${id}mdField\" cx=\".38\" cy=\".3\" r=\".85\"><stop offset=\"0\" stop-color=\"${md.a}\"/><stop offset=\".5\" stop-color=\"${md.m}\"/><stop offset=\"1\" stop-color=\"${md.b}\"/></radialGradient>")
        s.lin("${id}mdRim", "0", "0", "1", "1", "0" to md.a, ".35" to md.m, ".7" to md.b, "1" to md.m)
        s.append("<pattern id=\"${id}grain\" width=\"5\" height=\"5\" patternUnits=\"userSpaceOnUse\"><circle cx=\"1\" cy=\"1\" r=\".5\" fill=\"#000000\" opacity=\".18\"/><circle cx=\"3.6\" cy=\"3\" r=\".42\" fill=\"#ffffff\" opacity=\".09\"/><circle cx=\"2.2\" cy=\"4.4\" r=\".36\" fill=\"#000000\" opacity=\".13\"/></pattern>")
        s.append("<pattern id=\"${id}kraft\" width=\"5\" height=\"5\" patternUnits=\"userSpaceOnUse\"><path d=\"M0 2 l5 1\" stroke=\"#7E5634\" stroke-width=\".35\" opacity=\".25\"/><circle cx=\"3\" cy=\"4\" r=\".4\" fill=\"#7E5634\" opacity=\".3\"/></pattern>")
        s.append("<clipPath id=\"${id}clip\"><rect x=\"${n(x)}\" y=\"${n(y)}\" width=\"${n(w)}\" height=\"${n(h)}\" rx=\"${n(r)}\"/></clipPath>")
        s.append("</defs>")

        val mx = x + 12 + (w - 17) / 2 + 1
        val my = y2 - 9
        val number = frame.number ?: level
        if (mode == Mode.TINY) {
            s.append("<g transform=\"translate(60 54) scale(2.55) translate(${n(-mx)} ${n(-my)})\">")
            medal(s, id, md, mx, my, number, frame, small = true)
            s.append("</g>")
            return s.toString()
        }
        val pulse = 0.7 + 0.3 * frame.pulse
        if (t >= 9) s.append("<circle cx=\"60\" cy=\"56\" r=\"56\" fill=\"url(#${id}aura)\" opacity=\"${n(pulse)}\"/>")
        if (t >= 10) for (i in 0 until 8) {
            val a = i * PI / 4 + frame.pulse * PI / 8
            s.append("<path d=\"M60 56 L${n(60 + cos(a) * 60)} ${n(56 + sin(a) * 60)}\" stroke=\"#FFD43B\" stroke-width=\"5\" opacity=\".12\" stroke-linecap=\"round\"/>")
        }
        s.append("<ellipse cx=\"${n(60 + dx / 2)}\" cy=\"${n(y2 + dy + 6)}\" rx=\"${n(w / 2 + dx / 2 + 6)}\" ry=\"5.5\" fill=\"#1B1464\" opacity=\".12\"/>")
        s.append("<ellipse cx=\"${n(60 + dx / 2)}\" cy=\"${n(y2 + dy + 3)}\" rx=\"${n(w / 2 + dx / 2)}\" ry=\"2.4\" fill=\"#1B1464\" opacity=\".18\"/>")
        // contracapa: a placa de trás
        s.append("<rect x=\"${n(x + dx)}\" y=\"${n(y + dy)}\" width=\"${n(w)}\" height=\"${n(h)}\" rx=\"${n(r)}\" fill=\"url(#${id}back)\" stroke=\"${c.e}\" stroke-width=\"1\"/>")
        // o miolo: as folhas entre as placas, 2,2 para dentro das capas
        val ins = 2.2
        val gild = t >= 6
        val fx1 = x2 - board
        val fy1 = y + ins
        val fx2 = x2 + dx - ins - .6
        val fy2 = y2 - ins
        s.append("<path d=\"M${n(fx1)} ${n(fy1)} L${n(fx2)} ${n(fy1 + dy)} Q${n(fx2 - 1.4)} ${n((fy1 + fy2) / 2 + dy)} ${n(fx2)} ${n(fy2 + dy)} L${n(fx1)} ${n(fy2)} Z\" fill=\"url(#${id}${if (gild) "gild" else "fore"})\"/>")
        s.append("<path d=\"M${n(x + ins + 6)} ${n(y2 - board)} L${n(x2 - ins)} ${n(y2 - board)} L${n(x2 + dx - ins - .6)} ${n(y2 + dy - ins)} L${n(x + dx + ins + 6)} ${n(y2 + dy - ins)} Z\" fill=\"url(#${id}${if (gild) "gild" else "foot"})\"/>")
        val lines = min(pages + 2, 26)
        for (i in 1 until lines) {
            val k = i.toDouble() / lines
            val op = if (gild) .55 else .35 + k * .5
            val col = if (gild) "#A77100" else "#BFA97A"
            val sw = if (i % 4 == 0) .7 else .38
            val lx = fx1 + (fx2 - fx1) * k
            val ly = fy1 + dy * k
            s.append("<path d=\"M${n(lx)} ${n(ly + .6)} Q${n(lx - 1.4 * k)} ${n((ly + fy2 + dy * k) / 2)} ${n(lx)} ${n(fy2 + dy * k - .6)}\" fill=\"none\" stroke=\"$col\" stroke-width=\"${n(sw)}\" opacity=\"${n(op)}\"/>")
            val bx = x + ins + 6 + dx * k
            val by = y2 - board + (dy - ins + board) * k
            s.append("<line x1=\"${n(bx)}\" y1=\"${n(by)}\" x2=\"${n(bx + w - ins * 2 - 6)}\" y2=\"${n(by)}\" stroke=\"$col\" stroke-width=\"${n(sw * .9)}\" opacity=\"${n(op * .9)}\"/>")
        }
        s.append("<path d=\"M${n(fx1)} ${n(fy1)} L${n(fx1 + 2.4)} ${n(fy1 + 1)} L${n(fx1 + 2.4)} ${n(fy2 + 1)} L${n(fx1)} ${n(fy2)} Z\" fill=\"#000000\" opacity=\".12\"/>")
        // a primeira folha, que aparece quando a capa abre
        if (open > 0.02) {
            val px = x + 1.5
            val pw = w - board - 1.5
            val ph = h - ins * 2
            s.append("<rect x=\"${n(px)}\" y=\"${n(y + ins)}\" width=\"${n(pw)}\" height=\"${n(ph)}\" rx=\"2\" fill=\"#FFFDF6\"/>")
            var ly = y + ins + 9.5
            while (ly < y2 - ins - 2) { s.append("<line x1=\"${n(px + 1)}\" y1=\"${n(ly)}\" x2=\"${n(px + pw - 1)}\" y2=\"${n(ly)}\" stroke=\"#C9D6F2\" stroke-width=\".45\"/>"); ly += 4 }
            s.append("<line x1=\"${n(x + 14)}\" y1=\"${n(y + ins)}\" x2=\"${n(x + 14)}\" y2=\"${n(y2 - ins)}\" stroke=\"#F4A3A8\" stroke-width=\".6\"/>")
            s.append("<g transform=\"translate(${n(x + w / 2 + 6)} ${n(y + h / 2)}) scale(.7)\" opacity=\".75\"><rect x=\"-8\" y=\"-10\" width=\"16\" height=\"20\" rx=\"3\" fill=\"none\" stroke=\"#5B4BE8\" stroke-width=\"1.3\"/><circle cx=\"-3\" cy=\"-3\" r=\"1.3\" fill=\"#5B4BE8\"/><circle cx=\"3\" cy=\"-3\" r=\"1.3\" fill=\"#5B4BE8\"/><path d=\"M-3 2 q3 3 6 0\" stroke=\"#5B4BE8\" stroke-width=\"1.2\" fill=\"none\" stroke-linecap=\"round\"/></g>")
            s.append("<rect x=\"${n(px)}\" y=\"${n(y + ins)}\" width=\"8\" height=\"${n(ph)}\" fill=\"#000000\" opacity=\"${n(.18 * open)}\"/>")
        }
        // capa da frente: gira pela lombada ao abrir
        val sx = 1 - open * 0.66
        s.append("<g transform=\"translate(${n(x)} 0) scale(${n(sx)} 1) translate(${n(-x)} 0)\">")
        s.append("<rect x=\"${n(x + .9)}\" y=\"${n(y + .8)}\" width=\"${n(w)}\" height=\"${n(h)}\" rx=\"${n(r)}\" fill=\"${c.e}\"/>")
        s.append("<rect x=\"${n(x)}\" y=\"${n(y)}\" width=\"${n(w)}\" height=\"${n(h)}\" rx=\"${n(r)}\" fill=\"url(#${id}cv)\"/>")
        s.append("<g clip-path=\"url(#${id}clip)\">")
        if (t == 0) s.append("<rect x=\"${n(x)}\" y=\"${n(y)}\" width=\"${n(w)}\" height=\"${n(h)}\" fill=\"url(#${id}kraft)\"/>")
        if (c.leather) s.append("<rect x=\"${n(x)}\" y=\"${n(y)}\" width=\"${n(w)}\" height=\"${n(h)}\" fill=\"url(#${id}grain)\"/>")
        if (c.gold) for (i in 0 until 8) s.append("<path d=\"M${n(x - 14 + i * 11)} ${n(y)} l20 ${n(h)}\" stroke=\"#FFF6C9\" stroke-width=\"2\" opacity=\".2\"/>")
        when (t) {
            0 -> s.append("<rect x=\"${n(x + 14)}\" y=\"${n(y + 10)}\" width=\"${n(w - 22)}\" height=\"15\" rx=\"2\" fill=\"#F7ECDA\" stroke=\"#A97A4E\" stroke-width=\".6\"/><line x1=\"${n(x + 18)}\" y1=\"${n(y + 16)}\" x2=\"${n(x2 - 12)}\" y2=\"${n(y + 16)}\" stroke=\"#C9A27A\" stroke-width=\".6\"/><line x1=\"${n(x + 18)}\" y1=\"${n(y + 20)}\" x2=\"${n(x2 - 18)}\" y2=\"${n(y + 20)}\" stroke=\"#C9A27A\" stroke-width=\".6\"/>")
            1 -> s.append("<rect x=\"${n(x + 14)}\" y=\"${n(y + 10)}\" width=\"${n(w - 22)}\" height=\"12\" rx=\"6\" fill=\"#ffffff\" opacity=\".85\"/><line x1=\"${n(x + 19)}\" y1=\"${n(y + 16)}\" x2=\"${n(x2 - 13)}\" y2=\"${n(y + 16)}\" stroke=\"#7FA8D8\" stroke-width=\".8\"/>")
            2 -> s.append("<rect x=\"${n(x)}\" y=\"${n(y + 12)}\" width=\"${n(w)}\" height=\"10\" fill=\"#ffffff\" opacity=\".18\"/><rect x=\"${n(x)}\" y=\"${n(y + 24)}\" width=\"${n(w)}\" height=\"1.4\" fill=\"#ffffff\" opacity=\".25\"/>")
        }
        if (t in 3..5) s.append("<rect x=\"${n(x + 13)}\" y=\"${n(y + 6)}\" width=\"${n(w - 19)}\" height=\"${n(h - 12)}\" rx=\"3.5\" fill=\"#000000\" opacity=\".08\"/><rect x=\"${n(x + 13.5)}\" y=\"${n(y + 6.5)}\" width=\"${n(w - 20)}\" height=\"${n(h - 13)}\" rx=\"3.5\" fill=\"none\" stroke=\"#ffffff\" stroke-opacity=\".3\" stroke-width=\".8\"/>")
        if (t == 5) s.append("<path d=\"M${n(x)} ${n(y + h * .66)} L${n(x2)} ${n(y + h * .48)} L${n(x2)} ${n(y + h * .56)} L${n(x)} ${n(y + h * .74)} Z\" fill=\"#ffffff\" opacity=\".14\"/>")
        if (leatherish) {
            s.append("<rect x=\"${n(x + 12)}\" y=\"${n(y + 5)}\" width=\"${n(w - 17)}\" height=\"${n(h - 10)}\" rx=\"4\" fill=\"#000000\" opacity=\"${if (c.gold) ".05" else ".14"}\"/>")
            s.append("<rect x=\"${n(x + 13)}\" y=\"${n(y + 6)}\" width=\"${n(w - 19)}\" height=\"${n(h - 12)}\" rx=\"3.5\" fill=\"none\" stroke=\"url(#${id}gold)\" stroke-width=\"1.2\"/>")
            s.append("<rect x=\"${n(x + 15.5)}\" y=\"${n(y + 8.5)}\" width=\"${n(w - 24)}\" height=\"${n(h - 17)}\" rx=\"2.5\" fill=\"none\" stroke=\"url(#${id}gold)\" stroke-width=\".5\" stroke-dasharray=\"1.4 1.1\" opacity=\".9\"/>")
            for ((cx0, cy0, sxx, syy) in listOf(Q(x + 13, y + 6, 1.0, 1.0), Q(x2 - 6, y + 6, -1.0, 1.0), Q(x + 13, y2 - 6, 1.0, -1.0), Q(x2 - 6, y2 - 6, -1.0, -1.0))) {
                s.append("<path d=\"M${n(cx0 + sxx * 2.5)} ${n(cy0 + syy * 7.5)} q0 ${n(syy * -5)} ${n(sxx * 5)} ${n(syy * -5)}\" fill=\"none\" stroke=\"url(#${id}gold)\" stroke-width=\".9\"/>")
            }
        }
        if (c.leather) s.append("<rect x=\"${n(x + 2.6)}\" y=\"${n(y + 2.6)}\" width=\"${n(w - 5.2)}\" height=\"${n(h - 5.2)}\" rx=\"${n(r - 1.5)}\" fill=\"none\" stroke=\"#FFE7A3\" stroke-opacity=\".55\" stroke-width=\".5\" stroke-dasharray=\"1.6 1.2\"/>")
        s.append("<rect x=\"${n(x)}\" y=\"${n(y)}\" width=\"${n(w)}\" height=\"${n(h)}\" fill=\"url(#${id}gloss)\"/>")
        frame.shine?.let { u -> s.append("<rect x=\"${n(x - 40 + u * (w + 80))}\" y=\"${n(y - 20)}\" width=\"18\" height=\"${n(h + 40)}\" fill=\"url(#${id}sh)\" transform=\"rotate(20 60 60)\"/>") }
        s.append("</g>")
        s.append("<rect x=\"${n(x + .6)}\" y=\"${n(y + .6)}\" width=\"${n(w - 1.2)}\" height=\"${n(h - 1.2)}\" rx=\"${n(r)}\" fill=\"none\" stroke=\"#ffffff\" stroke-opacity=\".32\" stroke-width=\".9\"/>")
        s.append("<rect x=\"${n(x)}\" y=\"${n(y)}\" width=\"${n(w)}\" height=\"${n(h)}\" rx=\"${n(r)}\" fill=\"none\" stroke=\"${c.e}\" stroke-width=\"1.2\"/>")
        // lombada com dobradiça, cabeceado e nervuras
        if (t != 1) {
            s.append("<path d=\"M${n(x)} ${n(y + r)} Q${n(x)} ${n(y)} ${n(x + r)} ${n(y)} L${n(x + 10)} ${n(y)} L${n(x + 10)} ${n(y2)} L${n(x + r)} ${n(y2)} Q${n(x)} ${n(y2)} ${n(x)} ${n(y2 - r)} Z\" fill=\"url(#${id}spine)\"/>")
            s.append("<line x1=\"${n(x + 10.6)}\" y1=\"${n(y + 1)}\" x2=\"${n(x + 10.6)}\" y2=\"${n(y2 - 1)}\" stroke=\"#000000\" stroke-opacity=\".25\" stroke-width=\".9\"/>")
            s.append("<line x1=\"${n(x + 11.6)}\" y1=\"${n(y + 1)}\" x2=\"${n(x + 11.6)}\" y2=\"${n(y2 - 1)}\" stroke=\"#ffffff\" stroke-opacity=\".22\" stroke-width=\".7\"/>")
        }
        if (t >= 3) {
            for (yy in listOf(y + .9, y2 - 2.6)) s.append("<rect x=\"${n(x + 1.6)}\" y=\"${n(yy)}\" width=\"7\" height=\"1.7\" rx=\".8\" fill=\"#E5484D\"/><rect x=\"${n(x + 1.6)}\" y=\"${n(yy + .85)}\" width=\"7\" height=\".55\" fill=\"#FFF1C2\"/>")
            for (k in listOf(.2, .4, .6, .8)) s.append("<rect x=\"${n(x + .6)}\" y=\"${n(y + h * k - 1.6)}\" width=\"9\" height=\"3.2\" rx=\"1.5\" fill=\"${if (t >= 6) "url(#${id}gold)" else c.a}\" opacity=\"${if (t >= 6) "1" else ".6"}\"/><rect x=\"${n(x + .6)}\" y=\"${n(y + h * k + .6)}\" width=\"9\" height=\".7\" fill=\"#000000\" opacity=\".2\"/>")
        }
        if (t == 1) for (i in 0 until 11) {
            val cy = y + 5 + i * 6.4
            s.append("<circle cx=\"${n(x + 5)}\" cy=\"${n(cy + 1)}\" r=\"1.15\" fill=\"#3A4A66\"/><path d=\"M${n(x + 5)} ${n(cy + 1)} C${n(x - 2.5)} ${n(cy + 1)} ${n(x - 2.5)} ${n(cy - 3)} ${n(x + 1)} ${n(cy - 3.2)}\" fill=\"none\" stroke=\"url(#${id}silver)\" stroke-width=\"1.6\" stroke-linecap=\"round\"/>")
        }
        if (t >= 4) {
            val met = if (t >= 6) "url(#${id}gold)" else "url(#${id}silver)"
            for ((cx0, cy0, syy) in listOf(Triple(x2, y, 1.0), Triple(x2, y2, -1.0))) {
                s.append("<path d=\"M${n(cx0 - 15)} ${n(cy0)} L${n(cx0 - r)} ${n(cy0)} Q${n(cx0)} ${n(cy0)} ${n(cx0)} ${n(cy0 + syy * r)} L${n(cx0)} ${n(cy0 + syy * 15)} Z\" fill=\"$met\" stroke=\"#000000\" stroke-opacity=\".25\" stroke-width=\".5\"/>")
                s.append("<path d=\"M${n(cx0 - 13)} ${n(cy0 + syy * 1.2)} L${n(cx0 - 1.2)} ${n(cy0 + syy * 13)}\" stroke=\"#ffffff\" stroke-opacity=\".45\" stroke-width=\".6\"/>")
                s.append("<circle cx=\"${n(cx0 - 4.6)}\" cy=\"${n(cy0 + syy * 4.6)}\" r=\"1.15\" fill=\"#ffffff\" fill-opacity=\".85\" stroke=\"#000000\" stroke-opacity=\".33\" stroke-width=\".4\"/>")
            }
        }
        val cx = x + 12 + (w - 17) / 2 + 1
        val cy = y + h * 0.36
        if (t in 7..9) {
            s.append("<path d=\"M${n(cx)} ${n(cy - 14)} l10 3.6 v7.6 q0 8.6 -10 13 q-10 -4.4 -10 -13 v-7.6 Z\" fill=\"${c.e}\" fill-opacity=\".45\" stroke=\"url(#${id}gold)\" stroke-width=\"1.5\"/>")
            s.append("<path d=\"M${n(cx)} ${n(cy - 10)} l6.4 2.4 v5 q0 5.6 -6.4 8.6 q-6.4 -3 -6.4 -8.6 v-5 Z\" fill=\"none\" stroke=\"url(#${id}gold)\" stroke-width=\".5\" opacity=\".8\"/>")
            laurel(s, cx, cy + 1, 15.0, "url(#${id}gold)", "#6E4700", 5)
            if (t == 9) {
                s.append("<g opacity=\"${n(0.78 + 0.22 * frame.pulse)}\"><path d=\"M${n(cx)} ${n(cy - 8)} l5.6 5.4 l-5.6 8.6 l-5.6 -8.6 Z\" fill=\"#9F7AEA\"/><path d=\"M${n(cx)} ${n(cy - 8)} l5.6 5.4 h-11.2 Z\" fill=\"#E9D8FD\"/><path d=\"M${n(cx)} ${n(cy + 6)} l-5.6 -8.6 h3.6 Z\" fill=\"#6B21A8\"/><path d=\"M${n(cx - 2)} ${n(cy - 5)} l1.6 -1.4\" stroke=\"#ffffff\" stroke-width=\".8\" stroke-linecap=\"round\"/></g>")
            } else star(s, cx, cy - 1, 4.4, "url(#${id}gold)", "#6E4700")
        }
        if (t >= 10) {
            s.append("<circle cx=\"${n(cx)}\" cy=\"${n(cy - 4)}\" r=\"15\" fill=\"none\" stroke=\"#9C6A00\" stroke-width=\".6\" opacity=\".6\"/>")
            s.append("<path d=\"M${n(cx)} ${n(cy + 11)} q-1 -8 0 -14 M${n(cx)} ${n(cy + 1)} q-5.6 -3.6 -8.4 -8.6 M${n(cx)} ${n(cy - 1)} q5.6 -3.6 8.6 -8\" stroke=\"#7A4E00\" stroke-width=\"1.7\" fill=\"none\" stroke-linecap=\"round\"/>")
            val flowers = listOf(-8.0 to -9.0, 9.0 to -9.0, 0.0 to -14.0, -4.4 to -17.0, 4.6 to -17.0, -11.6 to -3.0, 11.6 to -3.0, 0.0 to -20.0, -7.6 to -15.0, 7.8 to -15.0)
            flowers.forEachIndexed { i, (ax, ay) ->
                s.append("<g transform=\"translate(${n(cx + ax)} ${n(cy + ay)}) rotate(${i * 23})\">")
                for (a in listOf(0, 72, 144, 216, 288)) s.append("<ellipse rx=\"2.2\" ry=\"1.4\" cx=\"1.8\" transform=\"rotate($a)\" fill=\"#FFF6C9\" stroke=\"#B57A00\" stroke-width=\".35\"/>")
                s.append("<circle r=\".8\" fill=\"#B36B00\"/></g>")
            }
        }
        if (t >= 8) s.append("<path d=\"M${n(x2 - 3)} ${n(y + h * .5 - 7)} h8 q3 0 3 3 v8 q0 3 -3 3 h-8 Z\" fill=\"url(#${id}gold)\" stroke=\"#6E4700\" stroke-width=\".6\"/><rect x=\"${n(x2 + 1)}\" y=\"${n(y + h * .5 - 3.4)}\" width=\"4\" height=\"6.8\" rx=\"1.4\" fill=\"#6E4700\" opacity=\".5\"/><circle cx=\"${n(x2 + 3)}\" cy=\"${n(y + h * .5)}\" r=\"1.2\" fill=\"#FFF3B0\"/>")
        s.append("</g>")
        // fitas marcadoras
        val fitaY = y2 + dy - ins - 1
        fun ribbon(rx: Double, len: Int, c1: String, c2: String) {
            s.append("<path d=\"M${n(rx)} ${n(fitaY)} v$len l3 -3.2 l3 3.2 v-$len Z\" fill=\"$c1\"/><path d=\"M${n(rx + 3)} ${n(fitaY)} v${n(len - 3.2)} l3 3.2 v-$len Z\" fill=\"$c2\" opacity=\".9\"/><line x1=\"${n(rx + .6)}\" y1=\"${n(fitaY + 1)}\" x2=\"${n(rx + .6)}\" y2=\"${n(fitaY + len - 1)}\" stroke=\"#ffffff\" stroke-opacity=\".35\" stroke-width=\".5\"/>")
        }
        if (t >= 3) ribbon(x + w - 20, 13, "#E5484D", "#B9282E")
        if (t >= 5) ribbon(x + w - 30, 10, "#FFB421", "#D98C00")
        medal(s, id, md, mx, my, number, frame, small = mode == Mode.SMALL)
        return s.toString()
    }

    // A medalha: roseta plissada, fitas com dobra, louros, borda em relevo, anel gravado, número em
    // alto-relevo; estrela no ouro, pedrinhas no ouro radiante. Pequena: sem louros nem pontinhos e
    // o número maior, escuro, com contorno branco (para ler em 30 px).
    private fun medal(s: StringBuilder, id: String, md: Medal, mx: Double, my: Double, number: Int, frame: Frame, small: Boolean) {
        val r = if (small) 16.0 else 13.5
        s.append("<path d=\"M${n(mx - 7)} ${n(my + 4)} L${n(mx - 13)} ${n(my + 25)} L${n(mx - 8.6)} ${n(my + 22)} L${n(mx - 5.4)} ${n(my + 26.4)} L${n(mx - .5)} ${n(my + 6)} Z\" fill=\"${md.ribbon.second}\"/>")
        s.append("<path d=\"M${n(mx - 7)} ${n(my + 4)} L${n(mx - 10.4)} ${n(my + 16)} L${n(mx - 4)} ${n(my + 6)} Z\" fill=\"#000000\" opacity=\".18\"/>")
        s.append("<path d=\"M${n(mx + 7)} ${n(my + 4)} L${n(mx + 13)} ${n(my + 25)} L${n(mx + 8.6)} ${n(my + 22)} L${n(mx + 5.4)} ${n(my + 26.4)} L${n(mx + .5)} ${n(my + 6)} Z\" fill=\"${md.ribbon.first}\"/>")
        s.append("<path d=\"M${n(mx + 8.4)} ${n(my + 8)} L${n(mx + 12)} ${n(my + 22.6)}\" stroke=\"#ffffff\" stroke-opacity=\".35\" stroke-width=\".7\"/>")
        if (md.glow) s.append("<circle cx=\"${n(mx)}\" cy=\"${n(my)}\" r=\"${n(r + 6 + 6 * frame.pulse)}\" fill=\"url(#${id}aura)\"/>")
        val petals = 20
        val ro = r + 5.2
        val ri = r + .6
        for (i in 0 until petals) {
            val a0 = i.toDouble() / petals * PI * 2
            val a1 = (i + 1).toDouble() / petals * PI * 2
            val am = (a0 + a1) / 2
            s.append("<path d=\"M${n(mx + cos(a0) * ri)} ${n(my + sin(a0) * ri)} L${n(mx + cos(am) * ro)} ${n(my + sin(am) * ro)} L${n(mx + cos(a1) * ri)} ${n(my + sin(a1) * ri)} Z\" fill=\"${if (i % 2 == 1) md.ribbon.first else md.ribbon.second}\"/>")
        }
        s.append("<circle cx=\"${n(mx)}\" cy=\"${n(my)}\" r=\"${n(ri + .2)}\" fill=\"none\" stroke=\"#000000\" stroke-opacity=\".18\" stroke-width=\".8\"/>")
        if (md.laurel != null && !small) laurel(s, mx, my + 1, r + 2.6, md.laurel.first, md.laurel.second, 6)
        s.append("<circle cx=\"${n(mx + .5)}\" cy=\"${n(my + 1.3)}\" r=\"${n(r)}\" fill=\"#000000\" opacity=\".25\"/>")
        s.append("<circle cx=\"${n(mx)}\" cy=\"${n(my)}\" r=\"${n(r)}\" fill=\"url(#${id}mdRim)\" stroke=\"${md.b}\" stroke-width=\".7\"/>")
        if (!small) {
            val teeth = StringBuilder()
            for (i in 0 until 36) {
                val a = i / 36.0 * PI * 2
                val rr = if (i % 2 == 1) r - .3 else r - 1.3
                teeth.append(if (i == 0) "M" else "L").append(n(mx + cos(a) * rr)).append(' ').append(n(my + sin(a) * rr)).append(' ')
            }
            s.append("<path d=\"${teeth}Z\" fill=\"none\" stroke=\"${md.b}\" stroke-width=\".45\" opacity=\".7\"/>")
        }
        s.append("<circle cx=\"${n(mx)}\" cy=\"${n(my)}\" r=\"${n(r - 2.4)}\" fill=\"${md.b}\"/>")
        s.append("<circle cx=\"${n(mx)}\" cy=\"${n(my)}\" r=\"${n(r - 2.9)}\" fill=\"url(#${id}mdField)\"/>")
        if (!small) for (i in 0 until 26) {
            val a = i / 26.0 * PI * 2
            s.append("<circle cx=\"${n(mx + cos(a) * (r - 4.6))}\" cy=\"${n(my + sin(a) * (r - 4.6))}\" r=\".38\" fill=\"${md.b}\" opacity=\".7\"/>")
        }
        s.append("<path d=\"M${n(mx - 7.6)} ${n(my - 5.4)} A9 9 0 0 1 ${n(mx + 5)} ${n(my - 8.4)}\" stroke=\"#ffffff\" stroke-opacity=\".75\" stroke-width=\"1.5\" fill=\"none\" stroke-linecap=\"round\"/>")
        s.append("<circle cx=\"${n(mx - 7.2)}\" cy=\"${n(my - 2.4)}\" r=\".7\" fill=\"#ffffff\" opacity=\".8\"/>")
        if (md.star) star(s, mx, my - r - 2.6, 3.6, "url(#${id}gold)", "#6E4700")
        if (md.gems) for ((ga, gc) in listOf(PI * .78 to "#E5484D", PI * .22 to "#2FBF8A")) {
            val gx = mx + cos(ga) * (r - 1)
            val gy = my + sin(ga) * (r - 1)
            s.append("<circle cx=\"${n(gx)}\" cy=\"${n(gy)}\" r=\"1.5\" fill=\"$gc\" stroke=\"#ffffff\" stroke-width=\".4\"/><circle cx=\"${n(gx - .5)}\" cy=\"${n(gy - .5)}\" r=\".45\" fill=\"#ffffff\"/>")
        }
        // o número: contorno (texto de baixo) e preenchimento, sem paint-order (o AndroidSVG não tem)
        val fs = if (small) (if (number > 99) 12.0 else if (number > 9) 17.0 else 19.0) else (if (number > 99) 9.5 else if (number > 9) 12.5 else 14.0)
        val ty = my + fs * .36 + frame.flip
        fun text(dx: Double, dy: Double, fill: String, extra: String = "") =
            s.append("<text x=\"${n(mx + dx)}\" y=\"${n(ty + dy)}\" text-anchor=\"middle\" font-family=\"Nunito, sans-serif\" font-weight=\"900\" font-size=\"${n(fs)}\" letter-spacing=\"-.5\" fill=\"$fill\" $extra>$number</text>")
        s.append("<clipPath id=\"${id}mc\"><circle cx=\"${n(mx)}\" cy=\"${n(my)}\" r=\"${n(r - 2.9)}\"/></clipPath><g clip-path=\"url(#${id}mc)\">")
        if (small) {
            text(0.0, 0.0, "#ffffff", "stroke=\"#ffffff\" stroke-width=\"2.6\" stroke-linejoin=\"round\"")
            text(0.0, 0.0, md.ink)
        } else {
            text(.5, .8, md.b, "opacity=\".9\"")
            text(-.35, -.45, "#ffffff", "opacity=\".7\"")
            text(0.0, 0.0, "#FFF7DA", "stroke=\"#FFF7DA\" stroke-width=\"1.1\" stroke-linejoin=\"round\"")
            text(0.0, 0.0, md.ink)
        }
        s.append("</g>")
    }

    private fun laurel(s: StringBuilder, cx: Double, cy: Double, r: Double, fill: String, edge: String, count: Int) {
        for (side in listOf(-1, 1)) {
            s.append("<path d=\"M${n(cx + side * 3)} ${n(cy + r + 2)} A${n(r)} ${n(r)} 0 0 ${if (side < 0) 1 else 0} ${n(cx + side * r * .82)} ${n(cy - r * .55)}\" fill=\"none\" stroke=\"$edge\" stroke-width=\".9\"/>")
            for (i in 0 until count) {
                val a = PI / 2 + side * (0.35 + i * (2.05 / count))
                val px = cx + cos(a) * r
                val py = cy + sin(a) * r
                val rot = a * 180 / PI + (if (side < 0) -55 else 55) + 90
                s.append("<ellipse cx=\"${n(px)}\" cy=\"${n(py)}\" rx=\"3.1\" ry=\"1.35\" transform=\"rotate(${n(rot)} ${n(px)} ${n(py)})\" fill=\"$fill\" stroke=\"$edge\" stroke-width=\".45\"/>")
            }
        }
    }

    private fun star(s: StringBuilder, cx: Double, cy: Double, r: Double, fill: String, edge: String) {
        val d = StringBuilder()
        for (i in 0 until 10) {
            val a = -PI / 2 + i * PI / 5
            val rr = if (i % 2 == 1) r * .45 else r
            d.append(if (i == 0) "M" else "L").append(n(cx + cos(a) * rr)).append(' ').append(n(cy + sin(a) * rr)).append(' ')
        }
        s.append("<path d=\"${d}Z\" fill=\"$fill\" stroke=\"$edge\" stroke-width=\".6\" stroke-linejoin=\"round\"/>")
    }

    // ------------------------------------------------------------------ subir de nível

    /** Quanto dura a subida (ms): mais longa quando troca de capa. */
    fun levelUpMillis(from: Int, to: Int): Long = if (tierOf(from) != tierOf(to)) 2600 else 1800

    /**
     * Um quadro da subida de nível em [u] (0 a 1): o livro abre, a Folha voa e entra, fecha mais
     * grosso, a medalha gira o número e dá um pulinho, saem faíscas e uma onda. Troca de capa: o
     * brilho passa pela capa nova.
     */
    fun levelUpSvg(from: Int, to: Int, u: Double, mode: Mode = Mode.NORMAL): String {
        val tierChange = tierOf(to) != tierOf(from)
        fun ease(v: Double) = 1 - (1 - v).pow(3)
        val open = when { u < .22 -> ease(u / .22); u < .52 -> 1.0; u < .68 -> 1 - ease((u - .52) / .16); else -> 0.0 }
        var num = from
        var flip = 0.0
        if (u >= .66 && u < .86) { val k = (u - .66) / .2; flip = if (k < .5) -k * 2 * 14 else (1 - k) * 2 * 14; num = if (k < .5) from else to }
        else if (u >= .86) num = to
        val level = if (u >= .52) to else from
        val pop = if (u > .82) { val k = min(1.0, (u - .82) / .18); 1 + 2.4 * (k - 1).pow(3) + 1.4 * (k - 1).pow(2) } else 1.0
        val scale = 1 + (pop - 1) * .06
        val s = StringBuilder("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 120 120\">")
        s.append("<g transform=\"translate(60 60) scale(${n(scale)}) translate(-60 -60)\">")
        s.append(body(level, mode, Frame(open = open, number = num, flip = flip, shine = if (tierChange && u > .66) (u - .66) / .34 else null, pulse = (sin(u * PI * 4) + 1) / 2)))
        s.append("</g>")
        if (u > .1 && u < .55) {
            val k = ease((u - .1) / .45)
            // rastro dourado atrás da Folha
            for (i in 0 until 8) {
                val kk = (k - i * .035).coerceAtLeast(0.0)
                s.append("<circle cx=\"${n(108 - kk * 50)}\" cy=\"${n(6 + kk * 50 - sin(kk * PI) * 14)}\" r=\"${n(1.8 - i * .18)}\" fill=\"#FFB421\" opacity=\"${n(.8 - i * .1)}\"/>")
            }
            flyingFolha(s, 108 - k * 50, 6 + k * 50 - sin(k * PI) * 14, (1 - k) * 35 - 8, 1.15 - k * .55)
        }
        if (u > .7) {
            val k = (u - .7) / .3
            val count = if (tierChange) 22 else 12
            for (i in 0 until count) {
                val a = i.toDouble() / count * PI * 2 + (i % 3) * .07
                val rr = 18 + (26 + (i * 37 % 26)) * ease(k)
                val sc = (.8 + (i * 53 % 80) / 100.0) * (1.3 - k * .5)
                s.append("<path d=\"M0 -3.4 Q.6 -.6 3.4 0 Q.6 .6 0 3.4 Q-.6 .6 -3.4 0 Q-.6 -.6 0 -3.4 Z\" fill=\"${if (i % 3 == 0) "#FFFFFF" else "#FFC93C"}\" opacity=\"${n(1 - k)}\" transform=\"translate(${n(60 + cos(a) * rr)} ${n(62 + sin(a) * rr)}) scale(${n(sc)})\"/>")
            }
            s.append("<circle cx=\"60\" cy=\"62\" r=\"${n(10 + k * 46)}\" fill=\"none\" stroke=\"#FFC93C\" stroke-width=\"${n(2.5 * (1 - k))}\" opacity=\"${n(1 - k)}\"/>")
        }
        s.append("</svg>")
        return s.toString()
    }

    /** A Folha que voa e entra no livro: folha de papel com orelha dobrada, linhas e carinha. */
    private fun flyingFolha(s: StringBuilder, px: Double, py: Double, rot: Double, sc: Double) {
        s.append("<g transform=\"translate(${n(px)} ${n(py)}) rotate(${n(rot)}) scale(${n(sc)})\">")
        s.append("<ellipse cx=\"2\" cy=\"16\" rx=\"9\" ry=\"2.2\" fill=\"#1B1464\" opacity=\".15\"/>")
        s.append("<path d=\"M-10 -13 h14 l6 6 v20 q0 2 -2 2 h-18 q-2 0 -2 -2 v-24 q0 -2 2 -2 Z\" fill=\"#ffffff\" stroke=\"#1B1464\" stroke-width=\"1.8\" stroke-linejoin=\"round\"/>")
        s.append("<path d=\"M4 -13 v4 q0 2 2 2 h4 Z\" fill=\"#E9E4FF\" stroke=\"#1B1464\" stroke-width=\"1.4\" stroke-linejoin=\"round\"/>")
        s.append("<path d=\"M-7 3 h12 M-7 7 h12 M-7 11 h8\" stroke=\"#C9C2F5\" stroke-width=\"1.1\" stroke-linecap=\"round\"/>")
        s.append("<ellipse cx=\"-4\" cy=\"-4\" rx=\"1.6\" ry=\"2\" fill=\"#1B1464\"/><ellipse cx=\"3\" cy=\"-4\" rx=\"1.6\" ry=\"2\" fill=\"#1B1464\"/>")
        s.append("<circle cx=\"-3.4\" cy=\"-4.8\" r=\".5\" fill=\"#ffffff\"/><circle cx=\"3.6\" cy=\"-4.8\" r=\".5\" fill=\"#ffffff\"/>")
        s.append("<path d=\"M-3 0 q2.5 2.6 5 0\" stroke=\"#1B1464\" stroke-width=\"1.5\" fill=\"none\" stroke-linecap=\"round\"/>")
        s.append("<circle cx=\"-7\" cy=\"-1\" r=\"1.3\" fill=\"#FF8FA3\" opacity=\".7\"/><circle cx=\"6\" cy=\"-1\" r=\"1.3\" fill=\"#FF8FA3\" opacity=\".7\"/>")
        s.append("</g>")
    }

    // ------------------------------------------------------------------ apoio

    private data class Q(val a: Double, val b: Double, val c: Double, val d: Double)

    private fun StringBuilder.lin(id: String, x1: String, y1: String, x2: String, y2: String, vararg stops: Pair<String, String>) {
        append("<linearGradient id=\"$id\" x1=\"$x1\" y1=\"$y1\" x2=\"$x2\" y2=\"$y2\">")
        stops.forEach { (o, c) -> append("<stop offset=\"$o\" stop-color=\"$c\"/>") }
        append("</linearGradient>")
    }

    /** Número com até duas casas, sem notação científica (igual no JVM e no JS). */
    internal fun n(v: Double): String {
        val r = (v * 100).roundToLong()
        val neg = r < 0
        val a = abs(r)
        val int = a / 100
        val frac = (a % 100).toInt()
        val f = when { frac == 0 -> ""; frac % 10 == 0 -> "." + (frac / 10); else -> "." + frac.toString().padStart(2, '0') }
        return (if (neg && (int != 0L || frac != 0)) "-" else "") + int + f
    }
}
