package br.com.estudario.ui.ai

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.TopicContentAiInput
import br.com.estudario.data.ai.TopicContentEstudoMapper
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity

private val ContentCopy = AiGenerationCopy(
    screenTitle = "Estudário",
    heroTitle = "Material completo deste tópico",
    heroText = "O Estudário pesquisa em fontes oficiais, confere a versão vigente das leis e prepara teoria, resumo e questões comentadas só do que este item do edital pede.",
    benefits = listOf(
        Icons.Outlined.MenuBook to "Teoria em capítulos, resumo e revisão rápida",
        Icons.Outlined.Quiz to "10 questões comentadas, do fácil ao difícil",
        Icons.Outlined.FactCheck to "Fontes oficiais listadas para você conferir",
    ),
    generateLabel = "Gerar com o Estudário",
    processingTitle = "Preparando seu material",
    stages = listOf(
        "Delimitando o recorte do edital",
        "Pesquisando em fontes oficiais",
        "Conferindo leis e versões vigentes",
        "Conferindo a legislação vigente",
        "Redigindo a teoria",
        "Montando resumo e revisão rápida",
        "Criando as questões comentadas",
        "Conferindo tudo antes de entregar",
    ),
    stageMillis = 22_000L,
    durationHint = "Cada fonte é conferida, por isso leva de 2 a 4 minutos.",
    fallbackLabel = "",
)

/**
 * Gera o material de um tópico pela IA do Estudário (1 por dia) e entrega o pacote à importação
 * padrão do app, que mostra a prévia e pede confirmação antes de gravar.
 */
@Composable
fun TopicAiContentScreen(
    competition: CompetitionEntity,
    subject: SubjectEntity,
    topic: TopicEntity,
    allTopics: List<TopicEntity>,
    onImport: (estudo: String) -> Unit,
    onFallback: () -> Unit,
    onClose: () -> Unit,
) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val jobs: AiTextJobViewModel = viewModel(
        key = "ai-content-${topic.id}",
        factory = AiTextJobViewModel.Factory(app, AiFeature.CONTENT_GENERATION, "content:${topic.id}"),
    )
    val state by jobs.state.collectAsState()
    // Pronto rápido (a matéria da biblioteca sai em ~3 s): antes de abrir, a animação passa pelas etapas que
    // faltavam, em vez de cortar no meio. Só quando a pessoa viu a geração começar nesta tela.
    var finale by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<Int?>(null) }
    var sawGenerating by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    if (state == AiTextJobState.Generating) sawGenerating = true
    LaunchedEffect(state) {
        val done = state as? AiTextJobState.Done ?: return@LaunchedEffect
        if (sawGenerating) {
            for (i in 2 until ContentCopy.stages.size) { finale = i; kotlinx.coroutines.delay(700) }
            finale = ContentCopy.stages.size; kotlinx.coroutines.delay(400)
        }
        val estudo = runCatching {
            TopicContentEstudoMapper.toEstudo(competition, subject, allTopics, topic, done.proposal)
        }.getOrNull() ?: return@LaunchedEffect
        jobs.consumeResult()
        onImport(estudo)
    }
    AiGenerationScreen(
        target = topic.title,
        copy = ContentCopy,
        state = if (finale != null) AiTextJobState.Generating else state,
        stageIndex = finale,
        onGenerate = { jobs.start(TopicContentAiInput.build(competition, null, subject, topic, allTopics)) },
        onRefresh = jobs::refresh,
        onFallback = onFallback,
        onClose = onClose,
    )
}
