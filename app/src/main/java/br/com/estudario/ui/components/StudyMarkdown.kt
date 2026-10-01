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
 * Texto de estudo em Markdown completo: títulos, listas numeradas, itálico, código, citações,
 * tabelas e fórmulas LaTeX (`$$...$$`, na linha ou em bloco). As cores seguem o tema do app.
 */
@Composable
fun StudyMarkdown(markdown: String, modifier: Modifier = Modifier, textSizeSp: Float? = null, onLongPress: (() -> Unit)? = null, onTap: (() -> Unit)? = null) {
    // Gráficos (```grafico) são desenhados pelo app; o resto segue no Markdown.
    val parts = remember(markdown) { splitCharts(markdown) }
    if (parts.size == 1 && parts[0].second == null) {
        MarkdownTextView(parts[0].first, modifier, textSizeSp, onLongPress, onTap)
        return
    }
    androidx.compose.foundation.layout.Column(modifier) {
        parts.forEach { (text, chart) ->
            if (chart != null) StudyChartView(chart) else MarkdownTextView(text, androidx.compose.ui.Modifier.fillMaxWidth(), textSizeSp, onLongPress, onTap)
        }
    }
}

@Composable
private fun MarkdownTextView(markdown: String, modifier: Modifier, textSizeSp: Float?, onLongPress: (() -> Unit)?, onTap: (() -> Unit)?) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val size = textSizeSp ?: MaterialTheme.typography.bodyLarge.fontSize.value
    val density = LocalDensity.current.density
    val markwon = remember(colors, size, density) { studyMarkwon(context, colors, size, density) }
    val text = remember(markdown) { StudyMarkdownNormalizer.normalize(markdown) }
    AndroidView(
        modifier = modifier,
        factory = { viewContext ->
            TextView(viewContext).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setLineSpacing(0f, 1.3f)
                includeFontPadding = false
            }
        },
        update = { view ->
            view.setTextColor(colors.onSurface.toArgb())
            view.setLinkTextColor(colors.primary.toArgb())
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
            markwon.setMarkdown(view, text)
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

private fun studyMarkwon(context: Context, colors: ColorScheme, textSizeSp: Float, density: Float): Markwon {
    fun dp(value: Int) = (value * density).roundToInt()
    val onSurface = colors.onSurface.toArgb()
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
                    .headingTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
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

    fun normalize(markdown: String): String = markdown
        .replace("\r\n", "\n")
        .replace(displayBrackets) { "\n$$\n${it.groupValues[1].trim()}\n$$\n" }
        .replace(inlineParens) { "$$${it.groupValues[1].trim()}$$" }
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
    return blocks
}
