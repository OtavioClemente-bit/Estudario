package br.com.estudario.ui.ai

import kotlinx.coroutines.flow.first
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
    screenTitle = "Estudário",
    heroTitle = "Seu plano sob medida",
    heroText = "O Estudário distribui suas matérias nos dias e horários que você escolheu, com revisões e simulados no momento certo.",
    benefits = listOf(
        Icons.Outlined.CalendarMonth to "Tarefas dia a dia, dentro do seu tempo",
        Icons.Outlined.Timeline to "Fases, revisões espaçadas e simulados",
        Icons.Outlined.Rule to "Só as suas matérias e tópicos, nada inventado",
    ),
    generateLabel = "Montar com o Estudário",
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
    fallbackLabel = "",
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
    onBackground: () -> Unit = onClose,
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
        onClose = {
            // Fechar no meio da geração não perde o plano: o job segue no servidor e no ViewModel
            // da Activity, e o aviso global entrega o .plano quando ficar pronto.
            if (state is AiTextJobState.Generating) {
                BackgroundAiTasks.start("plan:$competitionExternalId", competitionName, "Plano", null) {
                    val finished = jobs.state.first { it is AiTextJobState.Done || it is AiTextJobState.Failed }
                    if (finished is AiTextJobState.Failed) throw IllegalStateException(finished.message)
                    val done = finished as AiTextJobState.Done
                    val plano = StudyPlanAi.toPlano(done.context ?: throw IllegalStateException("Resposta sem contexto."), done.proposal)
                    jobs.consumeResult()
                    plano
                }
                BackgroundAiTasks.sendToBackground("plan:$competitionExternalId")
                onBackground()
            } else onClose()
        },
    )
}
