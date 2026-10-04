package br.com.estudario.ui.components

import android.content.Context
import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.latex.JLatexMathTheme
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tables.TableTheme
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import kotlin.math.roundToInt

/**
 * Texto curto (alternativa, cartão, dica) que só passa pelo leitor de Markdown quando tem
 * fórmula ou destaque; sem isso fica um Text comum, mais leve e com o estilo do lugar.
 */
@Composable
fun StudyInlineText(
    text: String,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge,
    color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
    fontWeight: androidx.compose.ui.text.font.FontWeight? = null,
    onTap: (() -> Unit)? = null,
) {
    val rich = remember(text) { "\$\$" in text || "\\(" in text || "**" in text }
    val ink = if (color == androidx.compose.ui.graphics.Color.Unspecified) androidx.compose.material3.LocalContentColor.current else color
    if (rich) StudyMarkdown(text, modifier, textSizeSp = style.fontSize.value, onTap = onTap, textColor = ink)
    else androidx.compose.material3.Text(text, modifier, color = ink, style = style, fontWeight = fontWeight)
}

/**
 * Texto de estudo em Markdown completo: títulos, listas numeradas, itálico, código, citações,
 * tabelas e fórmulas LaTeX (`$$...$$`, na linha ou em bloco). As cores seguem o tema do app.
 */
@Composable
fun StudyMarkdown(markdown: String, modifier: Modifier = Modifier, textSizeSp: Float? = null, onLongPress: (() -> Unit)? = null, onTap: (() -> Unit)? = null, textColor: androidx.compose.ui.graphics.Color? = null, centered: Boolean = false) {
    // Gráficos (```grafico) são desenhados pelo app; o resto segue no Markdown.
    val parts = remember(markdown) { splitCharts(markdown) }
    if (parts.size == 1 && parts[0].second == null) {
        MarkdownTextView(parts[0].first, modifier, textSizeSp, onLongPress, onTap, textColor, centered)
        return
    }
    androidx.compose.foundation.layout.Column(modifier) {
        parts.forEach { (text, chart) ->
            if (chart != null) StudyChartView(chart) else MarkdownTextView(text, androidx.compose.ui.Modifier.fillMaxWidth(), textSizeSp, onLongPress, onTap, textColor, centered)
        }
    }
}

@Composable
private fun MarkdownTextView(markdown: String, modifier: Modifier, textSizeSp: Float?, onLongPress: (() -> Unit)?, onTap: (() -> Unit)?, textColor: androidx.compose.ui.graphics.Color? = null, centered: Boolean = false) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val size = textSizeSp ?: MaterialTheme.typography.bodyLarge.fontSize.value
    val density = LocalDensity.current.density
    val ink = textColor ?: colors.onSurface
    val markwon = remember(colors, size, density, ink) { studyMarkwon(context, colors, size, density, ink) }
    val text = remember(markdown) { StudyMarkdownNormalizer.normalize(markdown) }
    AndroidView(
        modifier = modifier,
        factory = { viewContext ->
            TextView(viewContext).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setLineSpacing(0f, 1.3f)
                includeFontPadding = false
                // A mesma letra do resto do app; sem isso o Android usa a fonte do sistema aqui dentro.
                androidx.core.content.res.ResourcesCompat.getFont(viewContext, br.com.estudario.R.font.nunito_family)?.let { typeface = it }
            }
        },
        update = { view ->
            // Centralizado (cartões): lista e tabela continuam à esquerda, centralizadas ficam ilegíveis.
            val center = centered && !hasBlockLayout(markdown)
            view.gravity = if (center) android.view.Gravity.CENTER_HORIZONTAL else android.view.Gravity.START
            view.textAlignment = if (center) android.view.View.TEXT_ALIGNMENT_CENTER else android.view.View.TEXT_ALIGNMENT_VIEW_START
            view.setTextColor(ink.toArgb())
            view.setLinkTextColor(colors.primary.toArgb())
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
            markwon.setMarkdown(view, text)
            centerInlineFormulas(view, markwon.configuration().theme())
            // Segurar o trecho é o gesto de marcar: o TextView recebe o toque, então o aviso vem dele.
            if (onLongPress != null) {
                view.isLongClickable = true
                view.setOnLongClickListener { v -> v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS); onLongPress(); true }
            } else {
                view.setOnLongClickListener(null)
            }
            // Toque simples seleciona o trecho (na leitura da teoria). Link continua abrindo normalmente.
            if (onTap != null) view.setOnClickListener { if (view.selectionStart == -1 || view.selectionStart == view.selectionEnd) onTap() } else view.setOnClickListener(null)
        },
    )
}

private fun studyMarkwon(context: Context, colors: ColorScheme, textSizeSp: Float, density: Float, ink: androidx.compose.ui.graphics.Color): Markwon {
    fun dp(value: Int) = (value * density).roundToInt()
    val onSurface = ink.toArgb()
    return Markwon.builder(context)
        .usePlugin(MarkwonInlineParserPlugin.create())
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(
            TablePlugin.create(
                TableTheme.Builder()
                    .tableBorderColor(colors.outlineVariant.toArgb())
                    .tableBorderWidth(dp(1))
                    .tableCellPadding(dp(8))
                    .tableHeaderRowBackgroundColor(colors.primaryContainer.toArgb())
                    .tableEvenRowBackgroundColor(colors.surfaceVariant.copy(alpha = 0.45f).toArgb())
                    .tableOddRowBackgroundColor(colors.surface.toArgb())
                    .build(),
            ),
        )
        .usePlugin(
            JLatexMathPlugin.create(textSizeSp * density * 1.15f) { builder ->
                builder.inlinesEnabled(true)
                builder.theme()
                    .textColor(onSurface)
                    .blockHorizontalAlignment(ru.noties.jlatexmath.JLatexMathDrawable.ALIGN_CENTER)
                    .blockPadding(JLatexMathTheme.Padding.symmetric(dp(8), dp(4)))
            },
        )
        .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder
                    .headingBreakHeight(0)
                    .headingTextSizeMultipliers(floatArrayOf(1.45f, 1.28f, 1.14f, 1.05f, 1f, 1f))
                    .headingTypeface(androidx.core.content.res.ResourcesCompat.getFont(context, br.com.estudario.R.font.nunito_800) ?: Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
                    .linkColor(colors.primary.toArgb())
                    .blockQuoteColor(colors.primary.toArgb())
                    .blockQuoteWidth(dp(4))
                    .blockMargin(dp(20))
                    .listItemColor(colors.primary.toArgb())
                    .bulletWidth(dp(6))
                    .codeTextColor(colors.onSurface.toArgb())
                    .codeBackgroundColor(colors.surfaceVariant.toArgb())
                    .codeBlockTextColor(colors.onSurface.toArgb())
                    .codeBlockBackgroundColor(colors.surfaceVariant.toArgb())
                    .codeBlockMargin(dp(12))
                    .thematicBreakColor(colors.outlineVariant.toArgb())
            }
        })
        .build()
}

/**
 * Ajusta o que as IAs costumam escrever para o que o leitor entende: `\( \)` e `\[ \]` viram
 * `$$`, e blocos `$$` ganham linhas próprias. `$` sozinho fica como está, porque também é
 * cifrão ("R$ 100").
 */
object StudyMarkdownNormalizer {
    private val displayBrackets = Regex("""\\\[(.+?)\\]""", RegexOption.DOT_MATCHES_ALL)
    private val inlineParens = Regex("""\\\((.+?)\\\)""", RegexOption.DOT_MATCHES_ALL)
    private val moneyFormula = Regex("""\$\$\s*(?:\\text\{)?R\\\$\}?\s*(?:\\[,;! ])?\s*([\d.,]+)\s*\$\$""")

    fun normalize(markdown: String): String = markdown
        .replace("\r\n", "\n")
        .replace(displayBrackets) { "\n$$\n${it.groupValues[1].trim()}\n$$\n" }
        .replace(inlineParens) { "$$${it.groupValues[1].trim()}$$" }
        // Valor em reais escrito como fórmula ("$$R\$\,1.050,00$$") vira texto: o LaTeX não desenha o cifrão.
        .replace(moneyFormula) { "R$ ${it.groupValues[1]}" }
}

/**
 * Divide o texto em blocos para o leitor (marcar, retomar a leitura), sem quebrar tabela, lista
 * de código ou fórmula em bloco no meio: a linha em branco só separa blocos fora deles.
 */
fun studyBlocks(markdown: String): List<String> {
    val blocks = mutableListOf<String>()
    val current = StringBuilder()
    var inFence = false
    var inMath = false
    fun flush() {
        val block = current.toString().trim('\n')
        if (block.isNotBlank()) blocks += block
        current.clear()
    }
    StudyMarkdownNormalizer.normalize(markdown).lines().forEach { line ->
        val trimmed = line.trim()
        if (trimmed.startsWith("```")) inFence = !inFence
        else if (!inFence && trimmed == "$$") inMath = !inMath
        if (trimmed.isEmpty() && !inFence && !inMath) flush() else current.append(line).append('\n')
    }
    flush()
    // Capítulo cujo texto já abre com o próprio título ("## 1. X" seguido de "## 1. X"): mostra uma vez só.
    return blocks.filterIndexed { i, block -> i == 0 || headingText(block)?.let { it != headingText(blocks[i - 1]) } ?: true }
}

private fun headingText(block: String): String? = block.trim().takeIf { it.startsWith("#") && '\n' !in it }
    ?.trimStart('#')?.trim()?.trim('*')?.trim()?.lowercase()

/**
 * A fórmula na linha vinha centralizada na altura da linha inteira, que inclui o espaço extra
 * entre linhas embaixo do texto, e ficava abaixo das letras. Troca o desenho dela por um que
 * centraliza no meio das letras da própria linha.
 */
private fun centerInlineFormulas(view: TextView, theme: MarkwonTheme) {
    val spannable = view.text as? android.text.Spannable ?: return
    spannable.getSpans(0, spannable.length, io.noties.markwon.image.AsyncDrawableSpan::class.java)
        // Só as fórmulas na linha (a classe da biblioteca não é pública); as de bloco seguem centralizadas.
        .filter { it.javaClass.simpleName == "JLatexInlineAsyncDrawableSpan" }
        .forEach { old ->
        val start = spannable.getSpanStart(old)
        val end = spannable.getSpanEnd(old)
        val flags = spannable.getSpanFlags(old)
        spannable.removeSpan(old)
        spannable.setSpan(TextCenteredFormulaSpan(theme, old.getDrawable()), start, end, flags)
    }
}

private class TextCenteredFormulaSpan(theme: MarkwonTheme, private val formula: io.noties.markwon.image.AsyncDrawable) :
    io.noties.markwon.image.AsyncDrawableSpan(theme, formula, ALIGN_CENTER, false) {
    override fun getSize(paint: android.graphics.Paint, text: CharSequence?, start: Int, end: Int, fm: android.graphics.Paint.FontMetricsInt?): Int {
        if (!formula.hasResult()) return super.getSize(paint, text, start, end, fm)
        val bounds = formula.bounds
        if (fm != null) {
            val letters = paint.fontMetricsInt
            val center = (letters.ascent + letters.descent) / 2
            val half = bounds.height() / 2
            fm.ascent = minOf(letters.ascent, center - half)
            fm.descent = maxOf(letters.descent, center + half)
            fm.top = fm.ascent
            fm.bottom = fm.descent
        }
        return bounds.right
    }

    override fun draw(canvas: android.graphics.Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: android.graphics.Paint) {
        if (!formula.hasResult()) return super.draw(canvas, text, start, end, x, top, y, bottom, paint)
        val letters = paint.fontMetrics
        val center = y + (letters.ascent + letters.descent) / 2f
        val bounds = formula.bounds
        canvas.save()
        canvas.translate(x, center - bounds.height() / 2f - bounds.top)
        formula.draw(canvas)
        canvas.restore()
    }
}

/** Texto com lista, tabela ou citação, que não fica bom centralizado. */
internal fun hasBlockLayout(markdown: String): Boolean = markdown.lines().any { line ->
    val t = line.trimStart()
    t.startsWith("- ") || t.startsWith("* ") || t.startsWith("|") || t.startsWith("> ") || Regex("""^\d+[.)] """).containsMatchIn(t)
}

private val latexSymbols = mapOf(
    """\cdot""" to "·", """\times""" to "×", """\div""" to "÷", """\leq""" to "≤", """\le""" to "≤", """\geq""" to "≥", """\ge""" to "≥",
    """\neq""" to "≠", """\ne""" to "≠", """\approx""" to "≈", """\infty""" to "∞", """\pm""" to "±", """\Delta""" to "Δ", """\pi""" to "π",
    """\alpha""" to "α", """\beta""" to "β", """\theta""" to "θ", """\rho""" to "ρ", """\mu""" to "μ", """\Omega""" to "Ω", """\cup""" to "∪",
    """\cap""" to "∩", """\in""" to "∈", """\to""" to "→", """\Rightarrow""" to "⇒", """\%""" to "%", """\$""" to "$", """\,""" to " ", """\;""" to " ",
)

/**
 * Fórmula LaTeX como texto simples, para prévias de uma ou poucas linhas (lista de questões,
 * Caderno): "$$\frac{a}{b} \le x^2$$" vira "a/b ≤ x^2", sem os cifrões nem as barras.
 */
fun plainFormulaText(text: String): String {
    if (!text.contains("\$\$") && !text.contains('\\')) return text
    var out = StudyMarkdownNormalizer.normalize(text).replace("\$\$", "")
    out = out.replace(Regex("""\\frac\{([^{}]*)\}\{([^{}]*)\}"""), "($1)/($2)").replace(Regex("""\(([^()\s+\-]+)\)/"""), "$1/").replace(Regex("""/\(([^()\s+\-]+)\)"""), "/$1")
    out = out.replace(Regex("""\\sqrt\{([^{}]*)\}"""), "√($1)")
    out = out.replace(Regex("""\\(?:text|mathrm|mathbf|operatorname)\{([^{}]*)\}"""), "$1")
    out = out.replace(Regex("""\\mathbb\{R\}"""), "ℝ").replace(Regex("""\\mathbb\{N\}"""), "ℕ").replace(Regex("""\\mathbb\{Z\}"""), "ℤ")
    latexSymbols.entries.sortedByDescending { it.key.length }.forEach { (latex, symbol) -> out = out.replace(latex, symbol) }
    return out.replace(Regex("""\\left|\\right"""), "").replace(Regex("""\\[a-zA-Z]+"""), "").replace("{", "").replace("}", "").replace(Regex("""[ \t]+"""), " ").trim()
}
