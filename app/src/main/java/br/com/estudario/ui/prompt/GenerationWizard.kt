package br.com.estudario.ui.prompt

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
        Surface(Modifier.fillMaxSize(), color = scheme.background) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) {
                // cabeçalho: o que está sendo pedido e para onde
                Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 10.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Folha(44.dp, enter = false)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    if (tutorial != null) IconButton(onClick = { showTutorial = true }) { Icon(Icons.Outlined.HelpOutline, "Ver vídeo de ajuda") }
                    IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar") }
                }
                HorizontalDivider()

                // as partes do pedido, todas à vista
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(scroll).padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    steps.forEachIndexed { i, step ->
                        Section(i + 1, step, Modifier.onGloballyPositioned { tops[i] = it.positionInParent().y.toInt() })
                    }
                    if (externalWarning != null) Notice(externalWarning, scheme.errorContainer, scheme.onErrorContainer)
                    Spacer(Modifier.height(4.dp))
                }

                // rodapé fixo: o resumo do pedido e o botão
                Surface(color = scheme.surfaceContainer, shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp), tonalElevation = 3.dp) {
                    Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            summary.forEach { item ->
                                Surface(onClick = { goTo(item.step) }, shape = RoundedCornerShape(12.dp), color = scheme.surface, border = BorderStroke(1.dp, scheme.outlineVariant)) {
                                    Column(Modifier.widthIn(max = 220.dp).padding(horizontal = 12.dp, vertical = 7.dp)) {
                                        Text(item.label.uppercase(), fontSize = 9.5.sp, letterSpacing = 0.8.sp, fontWeight = FontWeight.Bold, color = scheme.onSurfaceVariant)
                                        Text(item.value, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                            }
                        }
                        val reason = steps.getOrNull(firstInvalid)?.error ?: server?.disabledReason?.takeIf { !server.enabled }
                        if (reason != null) Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickable(enabled = firstInvalid >= 0) { goTo(firstInvalid) }.background(scheme.errorContainer).padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(reason, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = scheme.onErrorContainer)
                            if (firstInvalid >= 0) Text("Ajustar", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = scheme.onErrorContainer)
                        }
                        if (server != null) {
                            Button(onClick = server.onGenerate, enabled = ready, modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp)) {
                                Folha(26.dp, enter = false); Spacer(Modifier.width(8.dp)); Text("Gerar com o Estudário", fontWeight = FontWeight.Bold)
                            }
                            TextButton(onClick = { showHow = !showHow }, modifier = Modifier.align(Alignment.CenterHorizontally), contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)) {
                                Icon(Icons.Outlined.Info, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text(if (showHow) "Fechar" else "Como o Estudário gera e quanto usa do saldo", style = MaterialTheme.typography.labelMedium)
                            }
                            AnimatedVisibility(showHow) { Text(server.description, style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant) }
                        }
                    }
                }
            }
        }
    }
    if (showTutorial && tutorial != null) TutorialVideoDialog(tutorial) { showTutorial = false }
}

/** Uma parte do pedido: número (vira ✓ quando completa), a pergunta, a dica e as escolhas. */
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
