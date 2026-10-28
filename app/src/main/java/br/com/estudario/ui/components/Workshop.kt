package br.com.estudario.ui.components

import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * A oficina do Folha (a mesma do site, workshop.js): a espera de toda geração mostra o trabalho
 * acontecendo, no traço da turma, em cima de uma mesa de caderno pontilhado.
 *
 *  PLAN      os blocos das matérias caem nos dias da semana e se acomodam; a semana cheia comemora
 *  MATERIAL  o caderno se escreve: o lápis corre as linhas, o marca-texto grifa, vem um flashcard,
 *            cai uma dica colada e a página vira
 *  QUESTIONS as questões chegam como cartas, o Folha confere as alternativas, a certa acende, o
 *            carimbo bate e a carta vai para a pilha
 *  EDITAL    a lupa varre o PDF linha por linha e as matérias saltam para a lista
 *
 * Tudo sai de um relógio só (tempo em segundos); cada cena é uma função pura do tempo, então não
 * há estado para desencontrar nem animação para vazar.
 */
enum class WorkshopKind { PLAN, MATERIAL, QUESTIONS, EDITAL }

private val INK = Color(0xFF26234A)
private val PAPER = Color(0xFFFFFEFA)
private val RULE = Color(0xFFE7E2F5)
private val LINE = Color(0xFF8F8AB8)
private val SOFT_LINE = Color(0xFFC9C3E3)
private val HIGHLIGHT = Color(0x99FFD23F)
private val MARGIN = Color(0xFFF4A6C0)
private val GREEN = Color(0xFF1FA97F)
private val SUBJECTS = listOf(Color(0xFF5B4BF5), Color(0xFF22C59A), Color(0xFFFF5C4D), Color(0xFFFFB421), Color(0xFF2EA8F5), Color(0xFFF472B6))

/** Medidas da cena em "pontos de projeto" (320 x 182), escaladas para a largura disponível. */
private const val W = 320f
private const val H = 182f

@Composable
private fun rememberSeconds(): Float {
    val t by produceState(0f) {
        val start = withInfiniteAnimationFrameNanos { it }
        while (true) withInfiniteAnimationFrameNanos { now -> value = (now - start) / 1_000_000_000f }
    }
    return t
}

private fun clamp01(v: Float) = v.coerceIn(0f, 1f)
private fun easeOut(p: Float) = 1f - (1f - clamp01(p)).let { it * it * it }
private fun backOut(p: Float): Float { val x = clamp01(p) - 1f; return 1f + 2.4f * x * x * x + 1.4f * x * x }

@Composable
fun WorkshopScene(kind: WorkshopKind, modifier: Modifier = Modifier) {
    val t = rememberSeconds()
    BoxWithConstraints(modifier.fillMaxWidth().widthIn(max = 460.dp).aspectRatio(W / H)) {
        val k = maxWidth.value / W
        fun u(v: Float): Dp = (v * k).dp
        // A mesa: papel pontilhado com degrau, como os cartões do app.
        Box(
            Modifier.fillMaxSize().padding(top = u(4f))
                .clip(RoundedCornerShape(u(18f)))
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(u(18f))),
        ) {
            val dot = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
            Canvas(Modifier.fillMaxSize()) {
                val gap = 11f * k * density
                var y = gap / 2
                while (y < size.height) { var x = gap / 2; while (x < size.width) { drawCircle(dot, 1.1f * density, Offset(x, y)); x += gap }; y += gap }
            }
        }
        val mood = when (kind) {
            WorkshopKind.MATERIAL -> if ((t % MATERIAL_CYCLE) in 7.2f..8.5f) FolhaMood.HAPPY else FolhaMood.THINKING
            WorkshopKind.QUESTIONS -> if ((t % QUESTION_CYCLE) in 1.4f..2.1f) FolhaMood.HAPPY else FolhaMood.TALKING
            WorkshopKind.PLAN -> if ((t % PLAN_CYCLE) > PLAN_BLOCKS * PLAN_STEP) FolhaMood.HAPPY else FolhaMood.THINKING
            WorkshopKind.EDITAL -> if (floor(t / EDITAL_STEP).toInt() % 9 == 8) FolhaMood.HAPPY else FolhaMood.THINKING
        }
        when (kind) {
            WorkshopKind.MATERIAL -> MaterialScene(t, k)
            WorkshopKind.QUESTIONS -> QuestionsScene(t, k)
            WorkshopKind.PLAN -> PlanScene(t, k)
            WorkshopKind.EDITAL -> EditalScene(t, k)
        }
        Folha(u(84f), Modifier.offset(u(6f), u(H - 86f)), mood = mood)
    }
}

// ------------------------------------------------------------------ matéria: o caderno se escreve

private const val MATERIAL_CYCLE = 9.6f
private val LINE_WIDTHS = listOf(0.92f, 0.74f, 0.88f, 0.96f, 0.7f, 0.84f, 0.5f)

@Composable
private fun MaterialScene(time: Float, k: Float) {
    val t = time % MATERIAL_CYCLE
    fun u(v: Float): Dp = (v * k).dp
    val left = 98f; val top = 16f; val pageW = 104f; val pageH = 148f
    val lineX = left + 8f; val lineMax = pageW - 16f
    // Onde a ponta do lápis está agora (fim da linha que está sendo escrita), calculado antes do desenho.
    var tip = Offset(lineX, top + 11f)
    var writing = false
    run {
        if (t < 0.7f) { tip = Offset(lineX + 62f * clamp01(t / 0.7f), top + 11f); writing = true }
        LINE_WIDTHS.forEachIndexed { i, w ->
            val start = 0.8f + i * 0.62f
            val p = clamp01((t - start) / 0.55f)
            if (p > 0f) { tip = Offset(lineX + lineMax * w * p, top + 26f + i * 14f); if (p < 1f) writing = true }
        }
        val rx = left + pageW + 20f
        listOf(0.86f, 0.7f, 0.78f).forEachIndexed { i, w ->
            val p = clamp01((t - (5.7f + i * 0.45f)) / 0.4f)
            if (p > 0f) { tip = Offset(rx + 70f * w * p, top + 54f + i * 14f); if (p < 1f) writing = true }
        }
    }
    Canvas(Modifier.fillMaxSize()) {
        val s = k * density
        fun r(x: Float, y: Float, w: Float, h: Float, c: Color, rad: Float = 3f) = drawRoundRect(c, Offset(x * s, y * s), Size(w * s, h * s), CornerRadius(rad * s))
        // Duas páginas.
        listOf(left, left + pageW).forEachIndexed { i, x ->
            r(x, top, pageW, pageH, PAPER, 8f)
            var ly = top + 22f
            while (ly < top + pageH - 6f) { drawLine(RULE, Offset((x + 2) * s, ly * s), Offset((x + pageW - 2) * s, ly * s), 0.8f * s); ly += 14f }
            if (i == 1) drawLine(MARGIN.copy(alpha = .7f), Offset((x + 14) * s, (top + 2) * s), Offset((x + 14) * s, (top + pageH - 2) * s), 1.2f * s)
            drawRoundRect(INK, Offset(x * s, top * s), Size(pageW * s, pageH * s), CornerRadius(8f * s), style = Stroke(2.2f * s))
        }
        // Título.
        val titleP = clamp01(t / 0.7f)
        r(lineX, top + 8f, 62f * titleP, 6f, INK)
        // Linhas da esquerda, com grifo em duas.
        LINE_WIDTHS.forEachIndexed { i, w ->
            val start = 0.8f + i * 0.62f
            val p = clamp01((t - start) / 0.55f)
            val y = top + 24f + i * 14f
            if (p > 0f) {
                if ((i == 1 || i == 4) && t > start + 0.6f) r(lineX - 2f, y - 4f, (lineMax * w + 4f) * clamp01((t - start - 0.6f) / 0.4f), 11f, HIGHLIGHT)
                r(lineX, y, lineMax * w * p, 4f, LINE, 2f)
            }
        }
        // Página da direita: flashcard, três linhas e a dica colada.
        val rx = left + pageW + 20f
        val cardP = backOut((t - 5.2f) / 0.45f)
        if (t > 5.2f) withTransform({ scale(cardP, cardP, Offset((rx + 36) * s, (top + 24) * s)) }) {
            r(rx - 4f, top + 8f, 80f, 30f, Color(0xFFECE9FF), 8f)
            drawRoundRect(INK, Offset((rx - 4f) * s, (top + 8f) * s), Size(80f * s, 30f * s), CornerRadius(8f * s), style = Stroke(2f * s))
            listOf(0.8f, 0.6f, 0.7f).forEachIndexed { i, w -> r(rx + 3f, top + 14f + i * 7f, 64f * w, 3.5f, Color(0xFF5B4BF5).copy(alpha = .75f), 2f) }
        }
        listOf(0.86f, 0.7f, 0.78f).forEachIndexed { i, w ->
            val start = 5.7f + i * 0.45f
            val p = clamp01((t - start) / 0.4f)
            val y = top + 52f + i * 14f
            if (p > 0f) r(rx, y, 70f * w * p, 4f, LINE, 2f)
        }
        if (t > 7.2f) {
            val p = backOut((t - 7.2f) / 0.5f)
            val y = top + 106f - (1f - p) * 90f
            withTransform({ rotate(4f + (1f - p) * -30f, Offset((rx + 48) * s, (y + 16) * s)) }) {
                r(rx + 26f, y, 46f, 32f, Color(0xFFFFE36E), 3f)
                listOf(0.8f, 0.6f, 0.5f).forEachIndexed { i, w -> r(rx + 31f, y + 7f + i * 7f, 34f * w, 3f, Color(0xFFB98A00).copy(alpha = .6f), 2f) }
            }
        }
        // A página vira: a folha da direita gira sobre a lombada.
        if (t > 8.6f) {
            val p = clamp01((t - 8.6f) / 0.8f)
            val fw = pageW * (1f - p)
            r(left + pageW, top, max(fw, 1f), pageH, PAPER, 8f)
            drawRoundRect(INK.copy(alpha = 1f - p * .5f), Offset((left + pageW) * s, top * s), Size(max(fw, 1f) * s, pageH * s), CornerRadius(8f * s), style = Stroke(2f * s))
        }
    }
    // O lápis da turma, com a ponta na ponta da linha (desenhado virado para escrever para a frente).
    val wobble = if (writing) sin(time * 38f) * 2.5f else 0f
    BrandIcon(
        Glyph.Pencil,
        Modifier.offset(u(tip.x - 24f), u(tip.y - 24f + wobble * .4f)).graphicsLayer { scaleX = -1f; rotationZ = -6f + wobble },
        size = u(26f),
    )
}

// ------------------------------------------------------------------ questões: cartas corrigidas

private const val QUESTION_CYCLE = 2.8f

@Composable
private fun QuestionsScene(time: Float, k: Float) {
    val cycle = floor(time / QUESTION_CYCLE).toInt()
    val t = time % QUESTION_CYCLE
    fun u(v: Float): Dp = (v * k).dp
    val right = (cycle * 3 + 1) % 4
    val cx = 176f; val cy = 18f; val cw = 128f; val ch = 124f
    Canvas(Modifier.fillMaxSize()) {
        val s = k * density
        fun card(x: Float, y: Float, w: Float, h: Float) {
            drawRoundRect(INK, Offset(x * s, (y + 3f) * s), Size(w * s, h * s), CornerRadius(10f * s))
            drawRoundRect(PAPER, Offset(x * s, y * s), Size(w * s, h * s), CornerRadius(10f * s))
            drawRoundRect(INK, Offset(x * s, y * s), Size(w * s, h * s), CornerRadius(10f * s), style = Stroke(2f * s))
        }
        // A pilha das prontas.
        repeat(min(cycle, 4)) { i ->
            withTransform({ rotate(-8f + i * 5f, Offset((122) * s, (150) * s)) }) {
                card(96f + i * 2f, 128f - i * 4f, 58f, 38f)
                drawRoundRect(SOFT_LINE, Offset((102f + i * 2f) * s, (136f - i * 4f) * s), Size(36f * s, 3f * s), CornerRadius(2f * s))
                drawRoundRect(Color(0xFF9BE3C6), Offset((102f + i * 2f) * s, (148f - i * 4f) * s), Size(28f * s, 3f * s), CornerRadius(2f * s))
            }
        }
        // A carta da vez: entra, é conferida e voa para a pilha.
        val inP = easeOut(t / 0.45f)
        val outP = clamp01((t - 2.1f) / 0.5f)
        val dx = (1f - inP) * 200f + outP * -120f
        val dy = outP * 70f
        val sc = 1f - outP * 0.55f
        if (outP < 1f) withTransform({
            translate(dx * s, dy * s)
            scale(sc, sc, Offset((cx + cw / 2) * s, (cy + ch / 2) * s))
            rotate(-1f + (1f - inP) * 12f - outP * 10f, Offset((cx + cw / 2) * s, (cy + ch / 2) * s))
        }) {
            card(cx, cy, cw, ch)
            drawRoundRect(LINE, Offset((cx + 10) * s, (cy + 10) * s), Size(104f * s, 4f * s), CornerRadius(2f * s))
            drawRoundRect(LINE, Offset((cx + 10) * s, (cy + 19) * s), Size(78f * s, 4f * s), CornerRadius(2f * s))
            val scan = if (t in 0.5f..1.38f) ((t - 0.5f) / 0.22f).toInt() else -1
            repeat(4) { i ->
                val oy = cy + 32f + i * 21f
                val ok = t >= 1.4f && i == right
                val fill = if (ok) Color(0xFFD9F7EA) else PAPER
                val edge = when { ok -> GREEN; i == scan -> Color(0xFF9D93FF); else -> Color(0xFFDDD6F0) }
                val nudge = if (i == scan) 2f else 0f
                drawRoundRect(fill, Offset((cx + 8 + nudge) * s, oy * s), Size((cw - 16) * s, 16f * s), CornerRadius(6f * s))
                drawRoundRect(edge, Offset((cx + 8 + nudge) * s, oy * s), Size((cw - 16) * s, 16f * s), CornerRadius(6f * s), style = Stroke(1.6f * s))
                drawCircle(if (ok) GREEN else Color(0xFFECE9FF), 5.5f * s, Offset((cx + 18 + nudge) * s, (oy + 8) * s))
                drawRoundRect(SOFT_LINE, Offset((cx + 28 + nudge) * s, (oy + 6) * s), Size((50f + (i * 17 % 30)) * s, 3.5f * s), CornerRadius(2f * s))
            }
        }
    }
    // O carimbo da turma bate na carta certa.
    if (t in 1.4f..2.15f) {
        val p = clamp01((t - 1.4f) / 0.25f)
        val sc = 2.2f - 1.2f * backOut(p)
        BrandIcon(Glyph.Check, Modifier.offset(u(cx + cw - 22f), u(cy - 12f)).graphicsLayer { scaleX = sc; scaleY = sc; rotationZ = 8f; alpha = clamp01(p * 3f) }, size = u(34f))
    }
}

// ------------------------------------------------------------------ plano: a semana se monta

private const val PLAN_BLOCKS = 18
private const val PLAN_STEP = 0.32f
private const val PLAN_CYCLE = PLAN_BLOCKS * PLAN_STEP + 1.7f
private val PLAN_ORDER = listOf(3, 0, 5, 1, 6, 2, 4)

@Composable
private fun PlanScene(time: Float, k: Float) {
    val t = time % PLAN_CYCLE
    val today = (java.time.LocalDate.now().dayOfWeek.value + 6) % 7
    val letters = listOf("S", "T", "Q", "Q", "S", "S", "D")
    val colX = { i: Int -> 98f + i * 30.5f }
    val colW = 26f; val colTop = 30f; val colBottom = 170f
    Canvas(Modifier.fillMaxSize()) {
        val s = k * density
        val full = t > PLAN_BLOCKS * PLAN_STEP
        val pulse = if (full) sin(clamp01((t - PLAN_BLOCKS * PLAN_STEP) / 0.6f) * PI.toFloat()) * 0.05f else 0f
        val fade = clamp01((t - PLAN_BLOCKS * PLAN_STEP - 1.1f) / 0.5f)
        repeat(7) { i ->
            val isToday = i == today
            val x = colX(i)
            drawRoundRect(if (isToday) Color(0xFFFFF3C4) else Color(0xFFECE9FF).copy(alpha = .7f), Offset(x * s, colTop * s), Size(colW * s, (colBottom - colTop) * s), CornerRadius(7f * s))
            drawRoundRect(if (isToday) Color(0xFFFFB421) else Color(0xFFCFC5F0), Offset(x * s, colTop * s), Size(colW * s, (colBottom - colTop) * s), CornerRadius(7f * s), style = Stroke(1.4f * s))
        }
        val stack = FloatArray(7)
        repeat(PLAN_BLOCKS) { b ->
            val col = PLAN_ORDER[b % 7]
            val h = 12f + (b * 37 % 5) * 4f
            val bottom = colBottom - 3f - stack[col]
            stack[col] += h + 3f
            val start = b * PLAN_STEP
            if (t < start) return@repeat
            val p = clamp01((t - start) / 0.3f)
            val fall = (1f - backOut(p)) * -(bottom - colTop + 30f)
            val x = colX(col) + 2.5f
            val y = bottom - h + fall
            val color = SUBJECTS[b % SUBJECTS.size].copy(alpha = 1f - fade)
            withTransform({ scale(1f + pulse, 1f + pulse, Offset((x + 10) * s, (y + h / 2) * s)) }) {
                drawRoundRect(INK.copy(alpha = 1f - fade), Offset(x * s, (y + 2f) * s), Size(21f * s, h * s), CornerRadius(5f * s))
                drawRoundRect(color, Offset(x * s, y * s), Size(21f * s, h * s), CornerRadius(5f * s))
                drawRoundRect(Color.White.copy(alpha = .5f * (1f - fade)), Offset((x + 4) * s, (y + 3) * s), Size(10f * s, 2.5f * s), CornerRadius(2f * s))
            }
        }
    }
    Row(Modifier.offset(x = (98f * k).dp, y = (12f * k).dp), horizontalArrangement = Arrangement.spacedBy(((30.5f - 26f) * k).dp)) {
        letters.forEachIndexed { i, l ->
            Text(l, Modifier.width((26f * k).dp), fontSize = (9f * k).sp, fontWeight = FontWeight.Black, color = if (i == today) Color(0xFFC47F00) else MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

// ------------------------------------------------------------------ edital: a lupa encontra as matérias

private const val EDITAL_STEP = 0.32f
private val EDITAL_NAMES = listOf("Português", "Dir. Constitucional", "Dir. Administrativo", "Raciocínio Lógico", "Informática", "Legislação", "Atualidades", "Dir. do Trabalho", "Contabilidade", "Ética")
private const val EDITAL_LINES = 13

@Composable
private fun EditalScene(time: Float, k: Float) {
    fun u(v: Float): Dp = (v * k).dp
    val step = floor(time / EDITAL_STEP).toInt()
    val line = step % EDITAL_LINES
    val found = step / 3
    val docX = 98f; val docY = 12f; val docW = 92f; val docH = 158f
    val lineY = { i: Int -> docY + 18f + i * 10.5f }
    Canvas(Modifier.fillMaxSize()) {
        val s = k * density
        drawRoundRect(INK, Offset(docX * s, (docY + 3f) * s), Size(docW * s, docH * s), CornerRadius(10f * s))
        drawRoundRect(PAPER, Offset(docX * s, docY * s), Size(docW * s, docH * s), CornerRadius(10f * s))
        drawRoundRect(INK, Offset(docX * s, docY * s), Size(docW * s, docH * s), CornerRadius(10f * s), style = Stroke(2f * s))
        drawRoundRect(Color(0xFFFF5C4D), Offset((docX + docW - 26f) * s, (docY + 5f) * s), Size(20f * s, 9f * s), CornerRadius(3f * s))
        repeat(EDITAL_LINES) { i ->
            val w = 50f + (i * 29 % 30)
            drawRoundRect(if (i == line) Color(0xFF9D93FF) else SOFT_LINE, Offset((docX + 8f) * s, lineY(i) * s), Size(w * s, 4f * s), CornerRadius(2f * s))
        }
    }
    // As matérias encontradas: as últimas cinco, a mais nova entrando com um pulo.
    Column(Modifier.offset(u(198f), u(12f)), verticalArrangement = Arrangement.spacedBy(u(5f))) {
        val shown = (max(0, found - 4)..found).toList()
        shown.forEach { n ->
            val newest = n == found
            val p = if (newest) backOut(((time % (EDITAL_STEP * 3)) / (EDITAL_STEP * 1.6f))) else 1f
            Surface(
                shape = RoundedCornerShape(u(8f)),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, INK),
                modifier = Modifier.width(u(112f)).graphicsLayer { scaleX = p; scaleY = p; alpha = clamp01(p) },
            ) {
                Row(Modifier.padding(horizontal = u(6f), vertical = u(4f)), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(u(7f)).clip(RoundedCornerShape(2.dp)).background(SUBJECTS[n % SUBJECTS.size]))
                    Text(EDITAL_NAMES[n % EDITAL_NAMES.size], Modifier.padding(start = u(5f)), fontSize = (8.5f * k).sp, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
    BrandIcon(Glyph.Lupa, Modifier.offset(u(docX + 30f + sin(line * 1.3f) * 22f), u(lineY(line) - 16f)), size = u(32f))
}

// ------------------------------------------------------------------ a lista de tarefas do Folha

/**
 * As etapas como lista de tarefas em papel pautado com margem: a atual com o lápis tremendo, as
 * feitas riscadas e com o check verde, as próximas esperando.
 */
@Composable
fun WorkshopChecklist(stages: List<String>, current: Int, modifier: Modifier = Modifier) {
    val t = rememberSeconds()
    val green = GREEN
    Surface(
        modifier,
        shape = RoundedCornerShape(6.dp, 6.dp, 18.dp, 6.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Box(Modifier.drawBehind {
            drawLine(MARGIN.copy(alpha = .75f), Offset(36.dp.toPx(), 0f), Offset(36.dp.toPx(), size.height), 1.6.dp.toPx())
        }) {
            Column(Modifier.padding(start = 10.dp, end = 14.dp, top = 8.dp, bottom = 8.dp)) {
                stages.forEachIndexed { i, stage ->
                    val done = i < current
                    val now = i == current
                    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(22.dp), contentAlignment = Alignment.Center) {
                            when {
                                done -> Box(Modifier.size(20.dp).clip(RoundedCornerShape(6.dp)).background(green), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Rounded.Check, null, Modifier.size(15.dp), tint = Color.White)
                                }
                                now -> BrandIcon(Glyph.Pencil, Modifier.graphicsLayer { rotationZ = sin(t * 14f) * 10f; translationY = sin(t * 14f) * 1.5f }, size = 24.dp)
                                else -> Box(Modifier.size(18.dp).border(1.6.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(5.dp)))
                            }
                        }
                        Text(
                            stage,
                            Modifier.padding(start = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = FontStyle.Italic,
                            fontWeight = if (now) FontWeight.Bold else FontWeight.SemiBold,
                            textDecoration = if (done) TextDecoration.LineThrough else null,
                            color = when {
                                now -> MaterialTheme.colorScheme.onSurface
                                done -> MaterialTheme.colorScheme.onSurfaceVariant
                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .6f)
                            },
                            maxLines = 2,
                        )
                    }
                }
            }
        }
    }
}

/** O título com o marca-texto amarelo atrás, que anda junto com o progresso. */
@Composable
fun HighlightedTitle(text: String, progress: Float, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier.drawBehind {
            drawRoundRect(HIGHLIGHT, Offset(0f, size.height * 0.56f), Size(size.width * clamp01(progress), size.height * 0.36f), CornerRadius(3.dp.toPx()))
        },
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Black,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
    )
}
