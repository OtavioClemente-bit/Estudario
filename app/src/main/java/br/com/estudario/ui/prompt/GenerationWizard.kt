package br.com.estudario.ui.prompt

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.tour.TutorialVideo
import br.com.estudario.ui.tour.TutorialVideoDialog
import kotlinx.coroutines.launch

/**
 * Uma parte do pedido. [error] nulo = parte completa; com texto, o motivo aparece na própria parte
 * e no rodapé, e o botão de gerar espera.
 */
class WizardStep(
    val title: String,
    val hint: String? = null,
    val error: String? = null,
    /** O título amigável da parte (a pergunta do Folha); sem ele, vale o título. */
    val question: String = title,
    /** Resumo curto da escolha, mostrado no cabeçalho da parte. */
    val answer: String? = null,
    val content: @Composable ColumnScope.() -> Unit,
)

/** Item do resumo do pedido no rodapé. Tocar nele rola até a parte [step]. */
data class WizardSummaryItem(val label: String, val value: String, val step: Int)

/** Geração dentro do app, pela IA do Estudário. */
class ServerGenerationOption(
    val description: String,
    val enabled: Boolean,
    val disabledReason: String? = null,
    val onGenerate: () -> Unit,
)

/**
 * O pedido de geração numa tela só: as partes numeradas (o que gerar, profundidade, questões…)
 * ficam todas à vista e se ajustam em qualquer ordem, e o rodapé fixo mostra o resumo do pedido e o
 * botão de gerar. Antes era um assistente de conversa, uma pergunta por vez, com revisão no fim:
 * para mudar um detalhe era preciso voltar etapa por etapa.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenerationWizard(
    title: String,
    subtitle: String,
    steps: List<WizardStep>,
    summary: List<WizardSummaryItem>,
    @Suppress("UNUSED_PARAMETER") prompt: String,
    onDismiss: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onImportText: (String) -> Unit,
    @Suppress("UNUSED_PARAMETER") onPickFile: () -> Unit,
    @Suppress("UNUSED_PARAMETER") returnFileLabel: String,
    @Suppress("UNUSED_PARAMETER") attachment: PromptAttachment? = null,
    server: ServerGenerationOption? = null,
    tutorial: TutorialVideo? = null,
    externalWarning: String? = null,
) {
    var showTutorial by remember { mutableStateOf(false) }
    var showHow by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val tops = remember { mutableStateMapOf<Int, Int>() }
    val firstInvalid = steps.indexOfFirst { it.error != null }
    val ready = firstInvalid < 0 && server?.enabled != false
    val scheme = MaterialTheme.colorScheme
    fun goTo(i: Int) { scope.launch { scroll.animateScrollTo(((tops[i] ?: 0) - 12).coerceAtLeast(0)) } }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        BackHandler(onBack = onDismiss)
        // O estúdio (como no site): painéis escuros de console, cada parte com a cor e o desenho dela,
        // e no rodapé o pedido resumido numa frase (sem repetir as escolhas) e o botão grande.
        br.com.estudario.ui.studio.StudioBackdrop(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) {
                Row(Modifier.fillMaxWidth().padding(start = 18.dp, end = 8.dp, top = 12.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        br.com.estudario.ui.studio.GxKicker("Estúdio do Estudário")
                        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = br.com.estudario.ui.studio.Gx.mute, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    if (tutorial != null) IconButton(onClick = { showTutorial = true }) { Icon(Icons.Outlined.HelpOutline, "Ver vídeo de ajuda", tint = Color.White) }
                    Box(
                        Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.06f)).clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center,
                    ) { Icon(Icons.Outlined.Close, "Fechar", tint = Color.White) }
                }
                HorizontalDivider(color = br.com.estudario.ui.studio.Gx.line)

                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(scroll).padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    steps.forEachIndexed { i, step ->
                        Box(Modifier.onGloballyPositioned { tops[i] = it.positionInParent().y.toInt() }) {
                            br.com.estudario.ui.studio.GxSection(stepGlyph(step.title), step.question, step.hint, StepColors[i % StepColors.size], error = step.error) { step.content(this) }
                        }
                    }
                    if (externalWarning != null) Notice(externalWarning, scheme.errorContainer, scheme.onErrorContainer)
                    Spacer(Modifier.height(4.dp))
                }

                Column(
                    Modifier.fillMaxWidth().background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color.Transparent, br.com.estudario.ui.studio.Gx.bg)))
                        .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    val recipe = summary.map { it.value }.filter { it.isNotBlank() }.joinToString(" · ")
                    if (recipe.isNotBlank()) Column(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.Black.copy(alpha = 0.28f))
                            .border(1.dp, br.com.estudario.ui.studio.Gx.violet.copy(alpha = 0.3f), RoundedCornerShape(14.dp)).padding(horizontal = 12.dp, vertical = 9.dp),
                    ) {
                        br.com.estudario.ui.studio.GxKicker("Seu pacote")
                        Text(recipe, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFFE9E5FF), maxLines = 3, overflow = TextOverflow.Ellipsis)
                    }
                    val reason = steps.getOrNull(firstInvalid)?.error ?: server?.disabledReason?.takeIf { !server.enabled }
                    if (reason != null) Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickable(enabled = firstInvalid >= 0) { goTo(firstInvalid) }.background(Color(0x33FF5A7A)).padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(reason, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFC2CD))
                        if (firstInvalid >= 0) Text("Ajustar", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    if (server != null) {
                        br.com.estudario.ui.studio.GxLaunch("Gerar com o Estudário", enabled = ready, onClick = server.onGenerate)
                        TextButton(onClick = { showHow = !showHow }, modifier = Modifier.align(Alignment.CenterHorizontally), contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)) {
                            Icon(Icons.Outlined.Info, null, Modifier.size(16.dp), tint = br.com.estudario.ui.studio.Gx.mute); Spacer(Modifier.width(4.dp))
                            Text(if (showHow) "Fechar" else "Como o Estudário gera e quanto usa do saldo", style = MaterialTheme.typography.labelMedium, color = br.com.estudario.ui.studio.Gx.mute)
                        }
                        AnimatedVisibility(showHow) { Text(server.description, style = MaterialTheme.typography.bodySmall, color = br.com.estudario.ui.studio.Gx.mute) }
                    }
                }
            }
        }
    }
    if (showTutorial && tutorial != null) TutorialVideoDialog(tutorial) { showTutorial = false }
}

/** A cor de cada parte do pedido, na ordem (a mesma paleta do site). */
private val StepColors = listOf(
    br.com.estudario.ui.studio.Gx.violet, br.com.estudario.ui.studio.Gx.cyan, br.com.estudario.ui.studio.Gx.mint,
    br.com.estudario.ui.studio.Gx.gold, br.com.estudario.ui.studio.Gx.orange, br.com.estudario.ui.studio.Gx.pink,
)

/** O desenho da turma de cada parte, pelo título. */
private fun stepGlyph(title: String): br.com.estudario.ui.brand.Glyph = when {
    title.startsWith("O que gerar") -> br.com.estudario.ui.brand.Glyph.Books
    title.startsWith("Abrang") -> br.com.estudario.ui.brand.Glyph.Target
    title.startsWith("Quest") || title.startsWith("Quantidade") -> br.com.estudario.ui.brand.Glyph.Clipboard
    title.startsWith("Nível") -> br.com.estudario.ui.brand.Glyph.Colunas
    title.startsWith("Edital") || title.startsWith("Fonte") -> br.com.estudario.ui.brand.Glyph.Doc
    title.startsWith("Rotina") || title.startsWith("Tempo") || title.startsWith("Prazo") -> br.com.estudario.ui.brand.Glyph.Calendar
    else -> br.com.estudario.ui.brand.Glyph.Pencil
}

/** Uma parte do pedido: número (vira ✓ quando completa), a pergunta, a dica e as escolhas. */
@Suppress("unused")
@Composable
private fun Section(number: Int, step: WizardStep, modifier: Modifier) {
    val scheme = MaterialTheme.colorScheme
    val done = step.error == null
    Surface(
        shape = EstudarioShapes.panel,
        color = scheme.surfaceContainerLow,
        border = if (done) null else BorderStroke(1.5.dp, scheme.error.copy(alpha = 0.6f)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    Modifier.size(28.dp).clip(CircleShape).background(if (done) scheme.primary else scheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    if (done) Icon(Icons.Outlined.Check, null, Modifier.size(16.dp), tint = scheme.onPrimary)
                    else Text("$number", fontWeight = FontWeight.Bold, color = scheme.onSurfaceVariant, fontSize = 13.sp)
                }
                Column(Modifier.weight(1f)) {
                    Text(step.title.uppercase(), fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = scheme.primary)
                    Text(step.question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
            step.hint?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant) }
            step.content(this)
            step.error?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = scheme.error, fontWeight = FontWeight.SemiBold) }
        }
    }
}

@Composable
private fun Notice(text: String, bg: Color, fg: Color) {
    Surface(shape = RoundedCornerShape(12.dp), color = bg, modifier = Modifier.fillMaxWidth()) {
        Text(text, Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall, color = fg)
    }
}
