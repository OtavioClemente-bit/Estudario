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
import androidx.compose.runtime.staticCompositionLocalOf
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

internal val LocalStudyLinksEnabled = staticCompositionLocalOf { true }

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
fun StudyMarkdown(
    markdown: String,
    modifier: Modifier = Modifier,
    textSizeSp: Float? = null,
    onLongPress: (() -> Unit)? = null,
    onTap: (() -> Unit)? = null,
    textColor: androidx.compose.ui.graphics.Color? = null,
    centered: Boolean = false,
    highlights: List<String> = emptyList(),
    highlightColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color(0x73FFC857),
    onSelectText: ((quote: String, withNote: Boolean) -> Unit)? = null,
    /** Cor de cada trecho grifado (ARGB); os que não estão aqui usam highlightColor. */
    highlightColors: Map<String, Int> = emptyMap(),
    /** Grifar já escolhendo a cor (0 cai muito, 1 entendi, 2 revisar): o menu da seleção mostra as três. */
    onSelectColor: ((quote: String, color: Int) -> Unit)? = null,
    notedHighlights: Set<String> = emptySet(),
    onHighlightTap: ((quote: String) -> Unit)? = null,
) {
    // Gráficos (```grafico) são desenhados pelo app; o resto segue no Markdown.
    val parts = remember(markdown) { splitCharts(centerLoneFormulas(markdown)) }
    if (parts.size == 1 && parts[0].second == null) {
        MarkdownTextView(parts[0].first, modifier, textSizeSp, onLongPress, onTap, textColor, centered, highlights, highlightColor, onSelectText, notedHighlights, onHighlightTap, highlightColors, onSelectColor)
        return
    }
    androidx.compose.foundation.layout.Column(modifier) {
        parts.forEach { (text, chart) ->
            if (chart != null) StudyChartView(chart) else MarkdownTextView(text, androidx.compose.ui.Modifier.fillMaxWidth(), textSizeSp, onLongPress, onTap, textColor, centered, highlights, highlightColor, onSelectText, notedHighlights, onHighlightTap, highlightColors, onSelectColor)
        }
    }
}

/** Leitura com fonte de livro (serifada, entrelinha maior): ligada pela teoria quando a pessoa escolhe "Livro". */
val LocalReaderBook = androidx.compose.runtime.staticCompositionLocalOf { false }

@Composable
private fun MarkdownTextView(
    markdown: String,
    modifier: Modifier,
    textSizeSp: Float?,
    onLongPress: (() -> Unit)?,
    onTap: (() -> Unit)?,
    textColor: androidx.compose.ui.graphics.Color? = null,
    centered: Boolean = false,
    highlights: List<String> = emptyList(),
    highlightColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    onSelectText: ((String, Boolean) -> Unit)? = null,
    notedHighlights: Set<String> = emptySet(),
    onHighlightTap: ((String) -> Unit)? = null,
    highlightColors: Map<String, Int> = emptyMap(),
    onSelectColor: ((String, Int) -> Unit)? = null,
) {
    val selectable = onSelectText != null
    val linksEnabled = LocalStudyLinksEnabled.current
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val size = textSizeSp ?: MaterialTheme.typography.bodyLarge.fontSize.value
    val density = LocalDensity.current.density
    val ink = textColor ?: colors.onSurface
    val markwon = remember(colors, size, density, ink) { studyMarkwon(context, colors, size, density, ink) }
    val text = remember(markdown) { StudyMarkdownNormalizer.normalize(markdown) }
    val book = LocalReaderBook.current
    AndroidView(
        modifier = modifier,
        factory = { viewContext ->
            TextView(viewContext).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setLineSpacing(0f, 1.3f)
                includeFontPadding = false
                // A mesma letra do resto do app; sem isso o Android usa a fonte do sistema aqui dentro.
                androidx.core.content.res.ResourcesCompat.getFont(viewContext, br.com.estudario.R.font.nunito_family)?.let { typeface = it }
                // Leitura: segurar e arrastar seleciona só o trecho (uma palavra, meia frase, um item da lista).
                if (selectable) setTextIsSelectable(true)
            }
        },
        update = { view ->
            // Centralizado (cartões): lista e tabela continuam à esquerda, centralizadas ficam ilegíveis.
            val center = centered && !hasBlockLayout(markdown)
            view.gravity = if (center) android.view.Gravity.CENTER_HORIZONTAL else android.view.Gravity.START
            view.textAlignment = if (center) android.view.View.TEXT_ALIGNMENT_CENTER else android.view.View.TEXT_ALIGNMENT_VIEW_START
            if (book) { view.typeface = android.graphics.Typeface.create("serif", android.graphics.Typeface.NORMAL); view.setLineSpacing(0f, 1.42f) }
            else { androidx.core.content.res.ResourcesCompat.getFont(view.context, br.com.estudario.R.font.nunito_family)?.let { view.typeface = it }; view.setLineSpacing(0f, 1.3f) }
            view.setTextColor(ink.toArgb())
            view.setLinkTextColor(colors.primary.toArgb())
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
            // Só redesenha o texto quando ele (ou os grifos) mudam: assim uma seleção em andamento não some.
            val stamp = listOf(text, size, ink.toArgb(), book, colors.primary.toArgb(), highlights, highlightColor.toArgb(), notedHighlights, highlightColors, linksEnabled)
            if (view.tag != stamp) {
                view.tag = stamp
                markwon.setMarkdown(view, text)
                centerInlineFormulas(view, markwon.configuration().theme())
                paintedRanges[view] = paintHighlights(view, highlights, highlightColor.toArgb(), notedHighlights, highlightColors)
                if (!linksEnabled) (view.text as? android.text.Spannable)?.let { content ->
                    content.getSpans(0, content.length, android.text.style.ClickableSpan::class.java).forEach { content.removeSpan(it) }
                }
            }
            view.linksClickable = linksEnabled
            view.movementMethod = if (linksEnabled) LinkMovementMethod.getInstance() else null
            // Guarda onde o dedo tocou: no clique, um trecho grifado abre a nota dele.
            if (onHighlightTap != null) view.setOnTouchListener { v, e ->
                if (e.action == android.view.MotionEvent.ACTION_DOWN) touchAt[v as TextView] = floatArrayOf(e.x, e.y)
                false
            } else view.setOnTouchListener(null)
            if (onSelectText != null) view.customSelectionActionModeCallback = highlightActions(view, onSelectText, onSelectColor)
            // Segurar o trecho é o gesto de marcar: o TextView recebe o toque, então o aviso vem dele.
            if (onLongPress != null && !selectable) {
                view.isLongClickable = true
                view.setOnLongClickListener { v -> v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS); onLongPress(); true }
            } else {
                view.setOnLongClickListener(null)
            }
            // Toque simples seleciona o trecho (na leitura da teoria). Link continua abrindo normalmente.
            if (onTap != null || onHighlightTap != null) view.setOnClickListener {
                if (view.selectionStart != -1 && view.selectionStart != view.selectionEnd) return@setOnClickListener
                val hit = if (onHighlightTap == null) null else touchAt[view]?.let { (x, y) ->
                    val offset = view.getOffsetForPosition(x, y)
                    paintedRanges[view].orEmpty().firstOrNull { offset in it.first }?.second
                }
                if (hit != null) onHighlightTap?.invoke(hit) else onTap?.invoke()
            } else view.setOnClickListener(null)
        },
    )
}

private fun studyMarkwon(context: Context, colors: ColorScheme, textSizeSp: Float, density: Float, ink: androidx.compose.ui.graphics.Color): Markwon {
    fun dp(value: Int) = (value * density).roundToInt()
    val onSurface = ink.toArgb()
    val night = androidx.core.graphics.ColorUtils.calculateLuminance(colors.background.toArgb()) < 0.5
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
                    // Código no meio do texto com cor própria, como no site: roxo no claro, lilás no escuro.
                    .codeTextColor(if (night) 0xFFC9C2FF.toInt() else 0xFF4A3BD8.toInt())
                    .codeBackgroundColor(if (night) 0x24ABA2FF else 0x1A5B4BF5)
                    .codeBlockTextColor(colors.onSurface.toArgb())
                    .codeBlockBackgroundColor(colors.surfaceVariant.toArgb())
                    .codeBlockMargin(dp(12))
                    .thematicBreakColor(colors.outlineVariant.toArgb())
            }
        })
        .build()
}

private val paintedRanges = java.util.WeakHashMap<TextView, List<Pair<IntRange, String>>>()
private val touchAt = java.util.WeakHashMap<TextView, FloatArray>()

/**
 * Pinta de marca-texto, no próprio texto, cada trecho grifado (achado mesmo com espaços e quebras
 * diferentes); os que têm nota ganham também um sublinhado. Devolve onde cada trecho ficou.
 */
private fun paintHighlights(view: TextView, quotes: List<String>, color: Int, noted: Set<String>, colors: Map<String, Int> = emptyMap()): List<Pair<IntRange, String>> {
    if (quotes.isEmpty()) return emptyList()
    val spannable = view.text as? android.text.Spannable ?: return emptyList()
    val plain = spannable.toString()
    return quotes.mapNotNull { quote ->
        val range = findQuote(plain, quote) ?: return@mapNotNull null
        spannable.setSpan(android.text.style.BackgroundColorSpan(colors[quote] ?: color), range.first, range.last + 1, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (quote in noted) spannable.setSpan(android.text.style.UnderlineSpan(), range.first, range.last + 1, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        range to quote
    }
}

/** Onde o trecho está no texto já desenhado; aceita diferença de espaços, quebras e marcadores de lista. */
internal fun findQuote(plain: String, quote: String): IntRange? {
    val q = quote.trim()
    if (q.isEmpty()) return null
    plain.indexOf(q).takeIf { it >= 0 }?.let { return it until it + q.length }
    val words = q.split(Regex("""[\s•·\u200B]+""")).filter { it.isNotBlank() }
    if (words.isEmpty()) return null
    val pattern = words.joinToString("""[\s•·\u200B]+""") { Regex.escape(it) }
    return runCatching { Regex(pattern).find(plain)?.range }.getOrNull()
}

/** O menu da seleção ganha "Grifar" e "Grifar com nota" na frente (Copiar e o resto continuam). */
private fun highlightActions(view: TextView, onSelect: (String, Boolean) -> Unit, onColor: ((String, Int) -> Unit)? = null) = object : android.view.ActionMode.Callback {
    override fun onCreateActionMode(mode: android.view.ActionMode, menu: android.view.Menu): Boolean {
        if (onColor != null) {
            // Grifar já na cor: amarelo cai muito, verde entendi, rosa revisar.
            menu.add(android.view.Menu.NONE, MENU_COLOR_BASE + 0, 0, "Cai muito").setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS)
            menu.add(android.view.Menu.NONE, MENU_COLOR_BASE + 1, 1, "Entendi").setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM)
            menu.add(android.view.Menu.NONE, MENU_COLOR_BASE + 2, 2, "Revisar").setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM)
        } else menu.add(android.view.Menu.NONE, MENU_HIGHLIGHT, 0, "Grifar").setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS)
        menu.add(android.view.Menu.NONE, MENU_HIGHLIGHT_NOTE, 3, "Grifar com nota").setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return true
    }
    override fun onPrepareActionMode(mode: android.view.ActionMode, menu: android.view.Menu) = false
    override fun onActionItemClicked(mode: android.view.ActionMode, item: android.view.MenuItem): Boolean {
        val color = (item.itemId - MENU_COLOR_BASE).takeIf { it in 0..2 }
        if (item.itemId != MENU_HIGHLIGHT && item.itemId != MENU_HIGHLIGHT_NOTE && color == null) return false
        val start = minOf(view.selectionStart, view.selectionEnd).coerceAtLeast(0)
        val end = maxOf(view.selectionStart, view.selectionEnd).coerceAtMost(view.text.length)
        val quote = if (end > start) view.text.subSequence(start, end).toString().trim() else ""
        mode.finish()
        if (quote.isNotBlank()) {
            if (color != null && onColor != null) onColor(quote, color) else onSelect(quote, item.itemId == MENU_HIGHLIGHT_NOTE)
        }
        return true
    }
    override fun onDestroyActionMode(mode: android.view.ActionMode) {}
}

private const val MENU_HIGHLIGHT = 0x6772_01
private const val MENU_HIGHLIGHT_NOTE = 0x6772_02
private const val MENU_COLOR_BASE = 0x6772_10

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
    // As fontes ficam só na aba Fontes, não no meio da teoria.
    StudyMarkdownNormalizer.normalize(br.com.estudario.text.SourcesSection.strip(markdown)).lines().forEach { line ->
        val trimmed = line.trim()
        if (trimmed.startsWith("```")) inFence = !inFence
        else if (!inFence && trimmed == "$$") inMath = !inMath
        if (trimmed.isEmpty() && !inFence && !inMath) flush() else current.append(line).append('\n')
    }
    flush()
    // Citações da busca entre parênteses no meio da frase também saem (as fontes ficam na aba Fontes).
    // Bloco a bloco, para a contagem de blocos (marcas e progresso de leitura) não mudar.
    for (i in blocks.indices) blocks[i] = br.com.estudario.text.SourcesSection.stripInline(blocks[i]).ifBlank { blocks[i] }
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

/**
 * Fórmula sozinha numa linha (`$$x^2$$` no meio do texto) vira fórmula de bloco, centralizada e
 * com respiro em volta, como num livro. Fórmula no meio da frase continua na linha.
 */
private const val D = "$"
private val LoneFormula = Regex("""^\s*\${D}\${D}([^${D}]+)\${D}\${D}\s*${D}""")

internal fun centerLoneFormulas(markdown: String): String {
    if (!markdown.contains("$D$D")) return markdown
    var inFence = false
    return markdown.lines().joinToString("\n") { line ->
        if (line.trimStart().startsWith("```")) inFence = !inFence
        val m = if (inFence) null else LoneFormula.matchEntire(line)
        if (m == null) line else "\n$D$D\n" + m.groupValues[1].trim() + "\n$D$D\n"
    }
}
