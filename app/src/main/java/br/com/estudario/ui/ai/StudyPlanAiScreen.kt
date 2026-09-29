package br.com.estudario.ui.ai

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Rule
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.StudyPlanAi

private val PlanCopy = AiGenerationCopy(
    screenTitle = "IA do Estudário",
    heroTitle = "Seu plano montado pela IA",
    heroText = "A IA distribui as suas matérias e tópicos nos dias e horas que você informou, com revisões e simulados na hora certa.",
    benefits = listOf(
        Icons.Outlined.CalendarMonth to "Tarefas dia a dia, dentro do seu tempo",
        Icons.Outlined.Timeline to "Fases, revisões espaçadas e simulados",
        Icons.Outlined.Rule to "Só as suas matérias e tópicos, nada inventado",
    ),
    generateLabel = "Montar plano com a IA",
    processingTitle = "Montando seu plano",
    stages = listOf(
        "Lendo suas matérias e prioridades",
        "Calculando o tempo de cada semana",
        "Definindo as fases do estudo",
        "Distribuindo teoria e questões",
        "Encaixando revisões e simulados",
        "Conferindo o limite de cada dia",
    ),
    stageMillis = 12_000L,
    durationHint = "Costuma levar de 1 a 2 minutos.",
    fallbackLabel = "Prefiro usar outra IA (ChatGPT, Gemini…)",
)

/**
 * Plano de estudo pela IA do Estudário (1 por dia). O resultado sai como .plano e segue o mesmo
 * caminho de uma IA externa: prévia, validação de cobertura e confirmação antes de aplicar.
 */
@Composable
fun StudyPlanAiScreen(
    competitionExternalId: String,
    competitionName: String,
    prepare: () -> StudyPlanAi.Prepared?,
    onPlano: (String) -> Unit,
    onFallback: () -> Unit,
    onClose: () -> Unit,
) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val jobs: AiTextJobViewModel = viewModel(
        key = "ai-plan-$competitionExternalId",
        factory = AiTextJobViewModel.Factory(app, AiFeature.PLAN_GENERATION, "plan:$competitionExternalId"),
    )
    val state by jobs.state.collectAsState()
    LaunchedEffect(state) {
        val done = state as? AiTextJobState.Done ?: return@LaunchedEffect
        val context = done.context ?: return@LaunchedEffect
        val plano = runCatching { StudyPlanAi.toPlano(context, done.proposal) }.getOrNull() ?: return@LaunchedEffect
        jobs.consumeResult()
        onPlano(plano)
    }
    AiGenerationScreen(
        target = competitionName,
        copy = PlanCopy,
        state = state,
        onGenerate = { prepare()?.let { jobs.start(it.input, it.context) } },
        onRefresh = jobs::refresh,
        onFallback = onFallback,
        onClose = onClose,
    )
}
