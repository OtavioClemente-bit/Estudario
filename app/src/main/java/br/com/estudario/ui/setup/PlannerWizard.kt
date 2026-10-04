package br.com.estudario.ui.setup

import br.com.estudario.ui.theme.estudarioLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.FilterChip
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.components.EstudarioBookLoader
import br.com.estudario.ui.theme.EstudarioMotion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * A casca do assistente de planejamento.
 *
 * A diferença entre um formulário e um assistente não é a quantidade de telas, é o fato de o
 * sistema **responder**. Aqui isso acontece de três formas, todas ligadas ao cálculo real:
 *
 * 1. [WizardPage] dá voz ao Estudário: a pergunta em destaque e, logo abaixo, uma fala curta
 *    explicando por que ela está sendo feita.
 * 2. [WizardFeedback] devolve, logo depois de cada resposta, o que aquela resposta mudou no plano.
 *    O texto vem de [br.com.estudario.domain.planner.PlanningExplanationBuilder], derivado das
 *    regras, não é uma frase de encorajamento escrita à parte.
 * 3. [WizardProcessing] mostra o cálculo acontecendo, com os estágios reais do motor.
 *
 * Nada aqui simula conversa com IA e nada aqui espera de mentira: ver [WIZARD_MIN_TRANSITION_MS].
 */

/**
 * Piso de duração das transições de processamento.
 *
 * O cálculo do Estudário é rápido, normalmente termina em poucos milissegundos. Sem um piso, a
 * tela de "montando sua estratégia" apareceria e sumiria como um flash, o que lê como bug. Com
 * 400ms, a transição é legível e a pessoa entende que algo foi decidido ali.
 *
 * O que este valor **não** é: uma espera fabricada. Se o cálculo levar mais que isso, a tela dura o
 * tempo do cálculo. Nunca existe delay depois que o resultado está pronto além deste piso visual.
 */
internal const val WIZARD_MIN_TRANSITION_MS = 400L

/** Quanto o Folha "pensa" antes de mostrar como entendeu a preparação: o bastante para ser visto. */
internal const val WIZARD_THINKING_MS = 3600L

sealed interface WizardComputation<out T> {
    data object Running : WizardComputation<Nothing>
    data class Ready<T>(val value: T) : WizardComputation<T>
}

/**
 * Roda um cálculo fora da thread principal e o expõe à tela.
 *
 * O resultado só aparece quando existe de verdade; o piso visual não inventa tempo, apenas impede
 * que a transição pisque.
 */
@Composable
internal fun <T> produceWizardResult(
    vararg keys: Any?,
    minMillis: Long = WIZARD_MIN_TRANSITION_MS,
    compute: suspend () -> T,
): WizardComputation<T> {
    var state by remember(*keys) { mutableStateOf<WizardComputation<T>>(WizardComputation.Running) }
    LaunchedEffect(*keys) {
        state = WizardComputation.Running
        val startedAt = System.currentTimeMillis()
        val value = withContext(Dispatchers.Default) { compute() }
        val elapsed = System.currentTimeMillis() - startedAt
        if (elapsed < minMillis) delay(minMillis - elapsed)
        state = WizardComputation.Ready(value)
    }
    return state
}

/**
 * Uma pergunta do assistente.
 *
 * [aside] é a fala do Estudário, o motivo da pergunta, em uma linha. Ela é o que transforma
 * "Dificuldade por matéria" em "Não precisa pensar demais. Isso pode ser ajustado depois."
 */
@Composable
internal fun WizardPage(
    eyebrow: String,
    question: String,
    aside: String,
    icon: ImageVector,
    showScrollIndicator: Boolean = false,
    bottom: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Mesma caixa da SetupPage: altura cheia para o weight(1f) do scroll ter contra o que medir, e
    // o rodapé respeitando a barra de navegação.
    Column(Modifier.fillMaxSize().padding(horizontal = estudarioLayout().screenGutter)) {
        SetupScrollContainer(Modifier.weight(1f), showScrollIndicator = showScrollIndicator) {
            SetupHeader(eyebrow, question, icon, aside, italicDescription = true)
            content()
        }
        SetupBottomBar(bottom)
    }
}

/** A fala do assistente: menor que a pergunta, em itálico, sem aspas decorativas na tela. */
@Composable
internal fun WizardAside(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.bodyLarge,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/**
 * A devolutiva depois de uma resposta.
 *
 * Aparece e some com o conteúdo, sem ocupar espaço quando não há nada a dizer. O texto vem das
 * regras: se o motor não tem uma conclusão para aquela combinação, nada é mostrado, melhor o
 * silêncio que um elogio genérico.
 */
@Composable
internal fun WizardFeedback(text: String?, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = !text.isNullOrBlank(),
        enter = fadeIn(EstudarioMotion.quick()),
        exit = fadeOut(EstudarioMotion.quick()),
        modifier = modifier,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().testTag("wizard_feedback"),
        ) {
            Row(
                Modifier.fillMaxWidth().padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
            ) {
                br.com.estudario.ui.assistant.Folha(30.dp, mood = br.com.estudario.ui.assistant.FolhaMood.TALKING)
                Text(
                    text.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}

/**
 * A tela de processamento.
 *
 * [stages] são os estágios reais do motor, e [completed] avança conforme eles terminam. Não existe
 * porcentagem inventada: ou o estágio terminou, ou não terminou.
 */
@Composable
internal fun WizardProcessing(
    title: String,
    stages: List<String>,
    completed: Int,
    modifier: Modifier = Modifier,
) {
    // O cálculo é instantâneo; as etapas são marcadas uma a uma, no ritmo de quem está pensando,
    // para a pessoa ver o Folha trabalhando nas respostas dela. Nunca passa do que já terminou.
    var shown by remember { mutableStateOf(0) }
    LaunchedEffect(stages.size) {
        while (shown < stages.size) { delay(WIZARD_THINKING_MS / (stages.size + 1)); shown++ }
    }
    val visibleDone = minOf(shown, completed)
    Column(
        modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 24.dp).testTag("wizard_processing"),
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        br.com.estudario.ui.assistant.Folha(190.dp, mood = br.com.estudario.ui.assistant.FolhaMood.THINKING)
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Text(
            stages.getOrNull(visibleDone)?.let { "$it…" } ?: "Pronto!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        androidx.compose.material3.LinearProgressIndicator(
            progress = { visibleDone / stages.size.coerceAtLeast(1).toFloat() },
            modifier = Modifier.fillMaxWidth(0.7f).padding(vertical = 4.dp),
        )
        stages.forEachIndexed { index, stage ->
            val done = index < visibleDone
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (done) "✓" else "·",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (done) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline,
                )
                Text(
                    stage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (done) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Seletor compacto de escala.
 *
 * Cinco opções numa linha rolável, sob o nome da matéria. É o que evita abrir uma página por
 * matéria: a pessoa percorre a lista e toca só no que quer mudar, porque tudo já vem com um valor
 * válido selecionado.
 */
@Composable
internal fun <T> WizardScale(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    testTagPrefix: String,
    onSelect: (T) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selected,
                onClick = { onSelect(option) },
                label = { Text(label(option)) },
                modifier = Modifier.testTag("${testTagPrefix}_${(option as? Enum<*>)?.name ?: option.toString()}"),
            )
        }
    }
}

/** Cabeçalho de uma matéria dentro de uma lista de ajuste. */
@Composable
internal fun WizardSubjectHeader(name: String, detail: String?) {
    Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    if (!detail.isNullOrBlank()) {
        Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
