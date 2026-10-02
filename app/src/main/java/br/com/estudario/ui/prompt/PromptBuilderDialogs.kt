package br.com.estudario.ui.prompt

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.fillMaxSize
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiContentProgress
import kotlinx.coroutines.flow.Flow
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.AlertDialog
import br.com.estudario.ui.theme.estudarioColors
import br.com.estudario.ui.theme.EstudarioShapes
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.ai.AiSyllabusPreferences
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.data.prompt.ContentBlock
import br.com.estudario.data.prompt.ContentPromptBuilder
import br.com.estudario.data.prompt.ContentPromptOptions
import br.com.estudario.data.prompt.EditalDetail
import br.com.estudario.data.prompt.EditalPromptBuilder
import br.com.estudario.data.prompt.EditalPromptOptions
import br.com.estudario.data.prompt.EditalScope
import br.com.estudario.data.prompt.EditalSource
import br.com.estudario.data.prompt.ExistingQuestionReference
import br.com.estudario.data.prompt.GenerationLimits
import br.com.estudario.data.prompt.MaterialSource
import br.com.estudario.data.prompt.PlanMethod
import br.com.estudario.data.prompt.PlanObjective
import br.com.estudario.data.prompt.PlanPromptBuilder
import br.com.estudario.data.prompt.PlanPromptOptions
import br.com.estudario.data.prompt.PlanSubjectInfo
import br.com.estudario.data.prompt.PlanTopicInfo
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.data.prompt.QuestionDifficulty
import br.com.estudario.data.prompt.LegalSphere
import br.com.estudario.data.prompt.QuestionStyle
import br.com.estudario.data.prompt.TheoryDepth
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.tour.TutorialVideo
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.math.roundToInt

private val attachmentTypes = arrayOf("application/pdf", "image/*", "text/plain")
private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private const val MULTI_TOPIC_WARNING = "Para preservar a profundidade e facilitar a conferência das fontes, recomendamos gerar um tópico por vez. Solicitações com vários tópicos podem produzir respostas incompletas ou afirmações sem respaldo. Revise o conteúdo antes de estudar."

// ---------------------------------------------------------------------------------------------
// Edital
// ---------------------------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditalPromptBuilderDialog(viewModel: AppViewModel, selectedCompetitionId: Long?, onDismiss: () -> Unit, onPickFile: () -> Unit) {
    val context = LocalContext.current
    val competitions by viewModel.competitions.collectAsState()
    var targetId by remember { mutableStateOf(selectedCompetitionId?.takeIf { id -> competitions.any { it.id == id } }) }
    val target = competitions.firstOrNull { it.id == targetId }
    var options by remember { mutableStateOf(EditalPromptOptions()) }
    var attachment by remember { mutableStateOf<PromptAttachment?>(null) }
    val attach = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> uri?.let { attachment = context.attachmentFor(it) } }
    val effective = (if (target != null) options.copy(competitionName = target.name, existingCompetitionId = PromptIds.competition(target), makePrimary = target.isPrimary) else options)
        .copy(attachmentProvided = attachment != null)
    val prompt = remember(effective) { EditalPromptBuilder.build(effective) }
    val competitionError = when {
        effective.competitionName.isBlank() -> "Informe o nome do concurso."
        effective.role.isBlank() -> "Informe o cargo ou a área."
        else -> null
    }

    val steps = listOf(
        WizardStep(
            "Concurso e cargo",
            "Crie um novo concurso ou complete um que já está no app. Banca e ano ajudam a localizar o edital correto.",
            competitionError,
            question = "Vamos trazer seu edital. De qual concurso e cargo estamos falando?",
            answer = listOf(effective.competitionName, effective.role, effective.board, effective.year).filter(String::isNotBlank).joinToString(" · "),
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = target == null, onClick = { targetId = null }, label = { Text("Novo concurso") })
                competitions.forEach { competition -> FilterChip(selected = competition.id == targetId, onClick = { targetId = competition.id }, label = { Text(competition.name) }) }
            }
            if (target == null) {
                OutlinedTextField(options.competitionName, { options = options.copy(competitionName = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Nome do concurso *") }, placeholder = { Text("Ex.: Polícia Federal") })
            }
            OutlinedTextField(options.role, { options = options.copy(role = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Cargo ou área *") }, placeholder = { Text("Ex.: Analista Judiciário, Área Administrativa") })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(options.board, { options = options.copy(board = it) }, Modifier.weight(1f), singleLine = true, label = { Text("Banca") })
                OutlinedTextField(options.year, { value -> options = options.copy(year = value.filter(Char::isDigit).take(4)) }, Modifier.width(110.dp), singleLine = true, label = { Text("Ano") })
            }
        },
        WizardStep(
            "O que extrair",
            "Defina a parte do edital que interessa e como os tópicos devem ficar.",
            question = "Que parte do edital você quer no app?",
            answer = "${options.scope.label} · ${options.detail.label}",
        ) {
            OptionSection("Abrangência") { ChoiceChips(EditalScope.entries, options.scope, { it.label }) { options = options.copy(scope = it) } }
            OptionSection("Nível de detalhe", "Dividir itens longos cria subtópicos didáticos, marcados como subdivisão de estudo.") {
                ChoiceChips(EditalDetail.entries, options.detail, { it.label }) { options = options.copy(detail = it) }
            }
            ToggleRow("Descrição curta em cada tópico", "Uma frase com o escopo do assunto", options.includeDescriptions) { options = options.copy(includeDescriptions = it) }
            ToggleRow("Prioridade pelo peso na prova", "Matérias com mais questões ficam como prioridade alta", options.priorityByWeight) { options = options.copy(priorityByWeight = it) }
            if (target == null) ToggleRow("Tornar concurso principal", null, options.makePrimary) { options = options.copy(makePrimary = it) }
        },
        WizardStep(
            "Edital oficial",
            "Com o PDF do edital, o Estudário segue exatamente as matérias e os tópicos publicados.",
            question = "Você tem o PDF oficial do edital?",
            answer = if (options.source == EditalSource.PASTE_TEXT) "Vou colar o texto" else attachment?.name ?: "Sem PDF por enquanto",
        ) {
            ChoiceChips(EditalSource.entries, options.source, { it.label }) { options = options.copy(source = it) }
            if (options.source == EditalSource.ATTACH_PDF) {
                AttachmentPicker(attachment, "Escolher PDF do edital", { attach.launch(attachmentTypes) }, { attachment = null })
                if (attachment == null) Text("Sem o arquivo, as matérias podem não bater com o edital publicado.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text("O pedido termina com o espaço “TEXTO DO EDITAL”: cole o conteúdo programático logo depois, na sua IA.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
    )
    val summary = listOf(
        WizardSummaryItem("Concurso", effective.competitionName.ifBlank { "Não informado" }, 0),
        WizardSummaryItem("Cargo", listOf(effective.role, effective.board, effective.year).filter(String::isNotBlank).joinToString(" · ").ifBlank { "Não informado" }, 0),
        WizardSummaryItem("Extração", "${options.scope.label} · ${options.detail.label}", 1),
        WizardSummaryItem("Edital", if (options.source == EditalSource.PASTE_TEXT) "Texto colado na sua IA" else attachment?.name ?: "Sem PDF anexado", 2),
    )
    val server = ServerGenerationOption(
        description = "Lê o PDF oficial, extrai matérias e tópicos e mostra tudo para você revisar antes de salvar. Usa 1 geração de edital do seu plano.",
        enabled = target != null && options.source == EditalSource.ATTACH_PDF,
        disabledReason = when {
            target == null -> "Disponível para concursos já criados no app. Crie o concurso primeiro, na aba Concursos."
            options.source != EditalSource.ATTACH_PDF -> "Para gerar pelo Estudário, anexe o PDF oficial do edital."
            else -> null
        },
        onGenerate = {
            target?.let { competition ->
                onDismiss()
                viewModel.openAiReview(
                    competition.id,
                    competition.name,
                    attachment?.uri?.toString(),
                    attachment?.name,
                    AiSyllabusPreferences(
                        competitionName = effective.competitionName,
                        role = effective.role,
                        board = effective.board,
                        year = effective.year,
                        scope = options.scope.name,
                        detail = options.detail.name,
                        includeDescriptions = options.includeDescriptions,
                    ),
                )
            }
        },
    )

    GenerationWizard(
        title = "Montar matérias pelo edital",
        subtitle = "Matérias, tópicos e subtópicos a partir do edital oficial",
        steps = steps,
        summary = summary,
        prompt = prompt,
        onDismiss = onDismiss,
        onImportText = { onDismiss(); viewModel.openIncomingText(it, targetId) },
        onPickFile = { onDismiss(); onPickFile() },
        returnFileLabel = "Abrir arquivo .estudo",
        attachment = attachment.takeIf { options.source == EditalSource.ATTACH_PDF },
        server = server,
        tutorial = TutorialVideo.EDITAL,
    )
}

// ---------------------------------------------------------------------------------------------
// Conteúdo: matéria inteira (vários tópicos) ou um tópico
// ---------------------------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContentPromptBuilderDialog(viewModel: AppViewModel, subjectId: Long, initialTopicIds: Set<Long>?, onDismiss: () -> Unit, onPickFile: () -> Unit) {
    val context = LocalContext.current
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val allTopics by viewModel.topics.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val subject = subjects.firstOrNull { it.id == subjectId }
    val competition = competitions.firstOrNull { it.id == subject?.competitionId }
    val topics = allTopics.filter { it.subjectId == subjectId }
    val ordered = remember(topics) { orderedTree(topics) }
    val withContent = remember(theories, summaries, questions) {
        buildSet { theories.forEach { add(it.topicId) }; summaries.forEach { add(it.topicId) }; questions.forEach { add(it.question.topicId) } }
    }
    val singleTopicMode = initialTopicIds != null && initialTopicIds.size == 1
    val selected = remember {
        mutableStateListOf<Long>().apply {
            addAll(initialTopicIds ?: ordered.map { it.first }.filter { it.id !in withContent }.take(1).map { it.id })
        }
    }
    val subjectBoard = remember(questions, topics) {
        val ids = topics.map { it.id }.toSet()
        questions.filter { it.question.topicId in ids }.mapNotNull { it.question.board }.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key.orEmpty()
    }
    var options by remember(subjectBoard) { mutableStateOf(ContentPromptOptions(board = subjectBoard)) }
    var attachment by remember { mutableStateOf<PromptAttachment?>(null) }
    val attach = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> uri?.let { attachment = context.attachmentFor(it) } }
    var serverTarget by remember { mutableStateOf<TopicEntity?>(null) }
    // Sem conta, a IA do Estudário não tem de onde descontar a cota: entra primeiro, gera em seguida.
    var loginFor by remember { mutableStateOf<TopicEntity?>(null) }
    val application = context.applicationContext as EstudarioApplication

    if (subject == null || competition == null) { LaunchedEffect(Unit) { onDismiss() }; return }
    val selectedIds = selected.toSet()
    val prompt = remember(options, selectedIds, topics, subject, competition) {
        // O órgão decide qual estatuto/lei vale; em vez de perguntar, usa o próprio concurso.
        if (selectedIds.isEmpty()) "" else ContentPromptBuilder.build(competition, subject, topics, selectedIds, options.copy(agency = options.agency.ifBlank { competition.name }))
    }

    val wholeSubject = !singleTopicMode && ordered.isNotEmpty() && selectedIds.size == ordered.size
    val singleTopic = topics.firstOrNull { it.id in selectedIds }.takeIf { selectedIds.size == 1 }

    val questionLimit = rememberQuestionBatchLimit()
    val steps = buildList {
        if (!singleTopicMode) add(WizardStep(
            "Abrangência",
            "Um tópico por vez gera material mais profundo e fácil de conferir. Os que têm ✓ já têm conteúdo.",
            when {
                ordered.isEmpty() -> "Esta matéria ainda não tem tópicos. Adicione tópicos ou importe o edital primeiro."
                selectedIds.isEmpty() -> "Selecione ao menos um tópico."
                else -> null
            },
            question = "Vamos montar seu material de ${subject.name}. O que você quer estudar?",
            answer = when {
                wholeSubject -> "A matéria completa (${ordered.size} tópicos)"
                singleTopic != null -> singleTopic.title
                else -> "${selectedIds.size} tópicos"
            },
        ) {
            ChoiceCard(
                "Um tópico específico", !wholeSubject,
                { selected.clear(); selected.addAll(ordered.map { it.first }.filter { it.id !in withContent }.take(1).map { it.id }) },
                description = "Recomendado · mais profundidade", icon = Icons.Outlined.CenterFocusStrong,
            )
            ChoiceCard(
                "A matéria completa", wholeSubject,
                { selected.clear(); selected.addAll(ordered.map { it.first.id }) },
                description = "${ordered.size} tópicos de uma vez", icon = Icons.Outlined.LibraryBooks,
            )
            if (!wholeSubject) Column {
                ordered.forEach { (topic, depth) ->
                    val checked = topic.id in selectedIds
                    Row(
                        Modifier.fillMaxWidth().clickable { if (checked) selected.remove(topic.id) else selected.add(topic.id) }.padding(start = (depth * 18).dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(checked, { if (it) selected.add(topic.id) else selected.remove(topic.id) })
                        Text(topic.title, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        if (topic.id in withContent) Text("✓", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    }
                }
            }
            if (selectedIds.size > 1) Text("${selectedIds.size} tópicos selecionados. $MULTI_TOPIC_WARNING", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        })
        add(WizardStep(
            "O que gerar",
            "Marque tudo o que quer receber. Dá para começar pelo pacote completo e desmarcar o resto.",
            "Escolha pelo menos um tipo de material.".takeIf { options.blocks.isEmpty() },
            question = "O que você quer que eu prepare?",
            answer = (if (options.blocks == ContentBlock.entries.toSet()) "Material completo" else options.blocks.sortedBy { it.ordinal }.joinToString(", ") { it.label }) +
                if (ContentBlock.THEORY in options.blocks) " · teoria ${options.depth.label.lowercase()}" else "",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(options.blocks == ContentBlock.entries.toSet(), { options = options.copy(blocks = ContentBlock.entries.toSet()) }, label = { Text("Pacote completo") })
                FilterChip(options.blocks == setOf(ContentBlock.QUESTIONS), { options = options.copy(blocks = setOf(ContentBlock.QUESTIONS)) }, label = { Text("Só questões") })
            }
            MultiChoiceCards(ContentBlock.entries, options.blocks, { it.label }, { options = options.copy(blocks = it) }, description = ::blockDescription, icon = ::blockIcon)
            if (ContentBlock.THEORY in options.blocks) OptionSection("Profundidade da teoria") {
                ChoiceCards(TheoryDepth.entries, options.depth, { it.label }, { options = options.copy(depth = it) }, description = ::depthDescription)
            }
            MaterialBaseSection(
                useOwn = options.source == MaterialSource.ATTACHED,
                attachment = attachment,
                onUseOwnChange = { use -> options = options.copy(source = if (use) MaterialSource.ATTACHED else MaterialSource.AI_KNOWLEDGE) },
                onPick = { attach.launch(attachmentTypes) },
                onClear = { attachment = null },
            )
        })
        if (ContentBlock.QUESTIONS in options.blocks) add(WizardStep(
            "Questões",
            "Arraste para escolher. O limite depende do seu plano.",
            "Defina a quantidade de questões.".takeIf { options.questionCount !in GenerationLimits.MIN_QUESTIONS..questionLimit.max },
            question = "Quantas questões e em que nível?",
            answer = "${options.questionCount} questões · ${options.difficulty.label} · ${options.style.label}" + options.board.takeIf(String::isNotBlank)?.let { " · $it" }.orEmpty(),
        ) {
            QuestionCountSelector(options.questionCount, questionLimit, perTopic = selectedIds.size > 1) { options = options.copy(questionCount = it) }
            OptionSection("Dificuldade") { DifficultySelector(options.difficulty) { options = options.copy(difficulty = it) } }
            OptionSection("Formato") { ChoiceCards(QuestionStyle.entries, options.style, ::styleTitle, { options = options.copy(style = it) }, description = ::styleDescription) }
            BoardField(options.board) { options = options.copy(board = it) }
        })
    }
    fun stepOf(title: String) = steps.indexOfFirst { it.title == title }.coerceAtLeast(0)
    val summary = buildList {
        add(WizardSummaryItem(
            if (selectedIds.size > 1) "Tópicos" else "Tópico",
            when {
                wholeSubject -> "Matéria completa (${ordered.size} tópicos)"
                singleTopic != null -> singleTopic.title
                else -> "${selectedIds.size} tópicos"
            },
            if (singleTopicMode) stepOf("O que gerar") else stepOf("Abrangência"),
        ))
        add(WizardSummaryItem("Materiais", options.blocks.sortedBy { it.ordinal }.joinToString(", ") { it.label }.ifBlank { "Nenhum" }, stepOf("O que gerar")))
        if (ContentBlock.QUESTIONS in options.blocks) add(WizardSummaryItem(
            "Questões",
            "${options.questionCount}${if (selectedIds.size > 1) " por tópico" else ""} · ${options.style.label} · ${options.difficulty.label}" +
                options.board.takeIf(String::isNotBlank)?.let { " · $it" }.orEmpty(),
            stepOf("Questões"),
        ))
        add(WizardSummaryItem(
            "Base",
            if (options.source == MaterialSource.ATTACHED) attachment?.name ?: "Material seu (ainda não anexado)" else "Pesquisa em fontes oficiais",
            stepOf("O que gerar"),
        ))
    }

    GenerationWizard(
        title = if (singleTopicMode) "Gerar material do tópico" else "Gerar material da matéria",
        subtitle = if (singleTopicMode) singleTopic?.let { ContentPromptBuilder.pathOf(it, topics) } ?: subject.name else subject.name,
        steps = steps,
        summary = summary,
        prompt = prompt,
        onDismiss = onDismiss,
        onImportText = { onDismiss(); viewModel.openIncomingText(it, competition.id) },
        onPickFile = { onDismiss(); onPickFile() },
        returnFileLabel = "Abrir arquivo .estudo",
        attachment = attachment.takeIf { options.source == MaterialSource.ATTACHED },
        server = ServerGenerationOption(
            description = "Pesquisa em fontes oficiais, confere a versão vigente das leis, escreve o material do tópico e mostra tudo, com as fontes, para você revisar antes de salvar. Usa 1 geração de conteúdo do seu plano.",
            enabled = singleTopic != null && options.source != MaterialSource.ATTACHED,
            disabledReason = when {
                singleTopic == null -> "O Estudário gera um tópico por vez. Escolha um tópico."
                options.source == MaterialSource.ATTACHED -> "Para trabalhar em cima do seu material, envie para a sua IA favorita (o anexo vai junto)."
                else -> null
            },
            onGenerate = { if (application.supabaseAuthRepository.accessToken() == null) loginFor = singleTopic else serverTarget = singleTopic },
        ),
        tutorial = TutorialVideo.CONTENT,
        externalWarning = MULTI_TOPIC_WARNING.takeIf { selectedIds.size > 1 },
    )
    loginFor?.let { pending ->
        br.com.estudario.ui.ai.AccountLoginDialog(
            onDismiss = { loginFor = null },
            onSignedIn = { loginFor = null; serverTarget = pending },
        )
    }
    serverTarget?.let { target ->
        // A geração roda no escopo do app: fechar a janela não cancela, só manda para segundo plano.
        val taskId = "content:${target.id}"
        LaunchedEffect(taskId) {
            br.com.estudario.ui.ai.BackgroundAiTasks.start(taskId, target.title, "Material", competition.id) {
                var result: String? = null
                application.aiContentGenerator.generate(competition, subject, topics, target, options).collect { value ->
                    when (value) {
                        is AiContentProgress.Done -> result = value.estudo
                        is AiContentProgress.Failed -> throw IllegalStateException(value.message)
                        else -> Unit
                    }
                }
                result ?: throw IllegalStateException("A geração terminou sem resultado. Tente de novo.")
            }
        }
        ServerContentGenerationDialog(
            topicTitle = target.title,
            taskId = taskId,
            onDone = { estudo -> serverTarget = null; br.com.estudario.ui.ai.BackgroundAiTasks.dismiss(taskId); onDismiss(); viewModel.openIncomingText(estudo, competition.id) },
            onBackground = { br.com.estudario.ui.ai.BackgroundAiTasks.sendToBackground(taskId); serverTarget = null; onDismiss() },
            onClose = { br.com.estudario.ui.ai.BackgroundAiTasks.dismiss(taskId); serverTarget = null },
        )
    }
}

/**
 * Etapas mostradas enquanto o Estudário escreve o material do tópico. São as etapas reais do
 * servidor: busca na web com prioridade para fontes oficiais, conferência da redação vigente das
 * normas e nada afirmado sem fonte (ver prompts/text-jobs-v1.ts).
 */
private val ContentGenerationStages = listOf(
    "Lendo o que o edital pede neste tópico",
    "Pesquisando em fontes oficiais",
    "Conferindo leis e versões vigentes",
    "Escrevendo a teoria, passo a passo",
    "Montando resumo e flashcards",
    "Criando questões no estilo da banca",
    "Revisando cada fonte citada",
)

/** O que o Estudário garante em todo material, mostrado enquanto ele trabalha. */
private val ContentCommitments = listOf(
    "Feito para o seu concurso" to "Escrito para o cargo e a banca informados, no nível em que o assunto é cobrado na prova.",
    "Base em fontes oficiais" to "Legislação, órgãos públicos e bancas vêm primeiro. Cada fonte consultada fica listada no material.",
    "Revisão de fatos" to "Uma segunda verificação confere artigos, prazos, números, contas e gabaritos antes da entrega.",
    "Você decide o que salvar" to "Nada entra no seu caderno sem a sua revisão. Se a geração não for concluída, ela volta para o seu saldo.",
)

/**
 * Tela de processamento da geração no servidor, com a cena animada do Estudário (a mesma do
 * edital). Ao terminar, entrega o pacote para a revisão de importação; se falhar, explica e volta.
 */
@Composable
private fun ServerContentGenerationDialog(topicTitle: String, taskId: String, onDone: (String) -> Unit, onBackground: () -> Unit, onClose: () -> Unit) {
    val tasks by br.com.estudario.ui.ai.BackgroundAiTasks.tasks.collectAsState()
    val status = tasks.firstOrNull { it.id == taskId }?.status
    LaunchedEffect(status) {
        (status as? br.com.estudario.ui.ai.BackgroundAiTasks.Status.Ready)?.let { onDone(it.result) }
    }
    val failed = status as? br.com.estudario.ui.ai.BackgroundAiTasks.Status.Failed
    androidx.compose.ui.window.Dialog(
        // Voltar ou fechar durante a geração não perde nada: ela segue em segundo plano.
        onDismissRequest = { if (failed != null) onClose() else onBackground() },
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true, dismissOnClickOutside = false),
    ) {
        androidx.compose.material3.Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (failed == null) {
                    br.com.estudario.ui.components.EstudarioProcessView(
                        title = "Preparando seu material",
                        eyebrow = topicTitle,
                        stages = ContentGenerationStages,
                        stageMillis = 14_000L,
                        footer = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ContentCommitmentsCard()
                                Text("Leva em média de 2 a 4 minutos, porque as fontes são consultadas e conferidas. Você pode continuar usando o app: avisaremos assim que o material estiver pronto para revisão.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                androidx.compose.material3.OutlinedButton(onClick = onBackground) { Text("Continuar em segundo plano") }
                                TextButton(onClick = onClose) { Text("Cancelar geração", color = MaterialTheme.colorScheme.error) }
                            }
                        },
                    )
                } else {
                    Spacer(Modifier.height(48.dp))
                    Icon(Icons.Outlined.AutoAwesome, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                    Text("Não deu certo desta vez", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                    Text(failed.message, textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    androidx.compose.material3.Button(onClick = onClose, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
                }
            }
        }
    }
}

@Composable
fun AdditionalQuestionPromptBuilderDialog(
    viewModel: AppViewModel,
    subjectId: Long,
    topicId: Long,
    onDismiss: () -> Unit,
    onPickFile: () -> Unit,
) {
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val allTopics by viewModel.topics.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val subject = subjects.firstOrNull { it.id == subjectId }
    val competition = competitions.firstOrNull { it.id == subject?.competitionId }
    val topic = allTopics.firstOrNull { it.id == topicId && it.subjectId == subjectId }
    val subjectTopics = allTopics.filter { it.subjectId == subjectId }
    val subjectTopicIds = remember(subjectTopics) { subjectTopics.map { it.id }.toSet() }
    val existingQuestions = remember(questions, subjectTopicIds) {
        questions.asSequence()
            .filter { it.question.topicId in subjectTopicIds }
            .map { question ->
                ExistingQuestionReference(
                    statement = question.question.statement,
                    options = question.options.sortedBy { it.position }.map { it.text },
                )
            }
            .toList()
    }
    val subjectBoard = remember(questions, subjectTopicIds) {
        questions.asSequence()
            .filter { it.question.topicId in subjectTopicIds }
            .mapNotNull { it.question.board?.takeIf(String::isNotBlank) }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
            .orEmpty()
    }
    var options by remember(subjectBoard) {
        mutableStateOf(
            ContentPromptOptions(
                blocks = setOf(ContentBlock.QUESTIONS),
                questionCount = 10,
                difficulty = QuestionDifficulty.MEDIUM,
                board = subjectBoard,
            ),
        )
    }
    val batchId = remember(topicId) { UUID.randomUUID().toString() }

    if (subject == null || competition == null || topic == null) {
        LaunchedEffect(Unit) { onDismiss() }
        return
    }

    val prompt = remember(options, competition, subject, subjectTopics, topic, existingQuestions, batchId) {
        ContentPromptBuilder.build(
            competition = competition,
            subject = subject,
            topics = subjectTopics,
            targetTopicIds = setOf(topic.id),
            o = options.copy(blocks = setOf(ContentBlock.QUESTIONS)),
            existingQuestions = existingQuestions,
            additionalQuestionBatchId = batchId,
        )
    }

    val questionLimit = rememberQuestionBatchLimit()
    val steps = listOf(
        WizardStep(
            "Quantidade",
            "Arraste para escolher. O limite depende do seu plano.",
            "Defina a quantidade de questões.".takeIf { options.questionCount !in GenerationLimits.MIN_QUESTIONS..questionLimit.max },
            question = "Quantas questões novas você quer resolver?",
            answer = "${options.questionCount} questões · ${options.style.label}",
        ) {
            QuestionCountSelector(options.questionCount, questionLimit, perTopic = false) { options = options.copy(questionCount = it) }
            OptionSection("Formato") { ChoiceCards(QuestionStyle.entries, options.style, ::styleTitle, { options = options.copy(style = it) }, description = ::styleDescription) }
        },
        WizardStep(
            "Nível e banca",
            "A banca orienta o estilo dos enunciados e das alternativas.",
            question = "Em que nível e no estilo de qual banca?",
            answer = listOf(options.difficulty.label, options.board).filter(String::isNotBlank).joinToString(" · "),
        ) {
            DifficultySelector(options.difficulty) { options = options.copy(difficulty = it) }
            BoardField(options.board) { options = options.copy(board = it) }
            Text(
                if (existingQuestions.isEmpty()) "Ainda não há questões cadastradas nesta matéria para comparar."
                else "${existingQuestions.size} questão(ões) já cadastradas vão junto só para evitar repetição, sem gabaritos nem desempenho.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
    )
    val summary = listOf(
        WizardSummaryItem("Questões", "${options.questionCount} · ${options.style.label}", 0),
        WizardSummaryItem("Nível", listOf(options.difficulty.label, options.board).filter(String::isNotBlank).joinToString(" · "), 1),
    )

    // Mesma IA do material, pedindo só o bloco de questões: o lote soma às questões do tópico.
    val context = LocalContext.current
    val application = context.applicationContext as EstudarioApplication
    var generating by remember { mutableStateOf(false) }
    var askLogin by remember { mutableStateOf(false) }
    GenerationWizard(
        title = "Gerar novas questões",
        subtitle = ContentPromptBuilder.pathOf(topic, subjectTopics),
        steps = steps,
        summary = summary,
        prompt = prompt,
        onDismiss = onDismiss,
        onImportText = { onDismiss(); viewModel.openIncomingText(it) },
        onPickFile = { onDismiss(); onPickFile() },
        returnFileLabel = "Abrir arquivo .estudo",
        server = ServerGenerationOption(
            description = "Escreve ${options.questionCount} questões novas no estilo da banca, com explicação de cada alternativa, e mostra para você revisar antes de salvar. Usa 1 geração de conteúdo do seu plano.",
            enabled = true,
            disabledReason = null,
            onGenerate = { if (application.supabaseAuthRepository.accessToken() == null) askLogin = true else generating = true },
        ),
    )
    if (askLogin) {
        br.com.estudario.ui.ai.AccountLoginDialog(
            onDismiss = { askLogin = false },
            onSignedIn = { askLogin = false; generating = true },
        )
    }
    if (generating) {
        val taskId = "questions:${topic.id}"
        val questionOptions = options.copy(blocks = setOf(ContentBlock.QUESTIONS))
        LaunchedEffect(taskId) {
            br.com.estudario.ui.ai.BackgroundAiTasks.start(taskId, topic.title, "Questões", competition.id) {
                var result: String? = null
                // Só as do próprio tópico: são as que o lote novo não pode repetir.
                val avoid = questions.filter { it.question.topicId == topic.id && !it.question.isHidden }.map { it.question.statement }
                application.aiContentGenerator.generate(competition, subject, subjectTopics, topic, questionOptions, avoid).collect { value ->
                    when (value) {
                        is AiContentProgress.Done -> result = value.estudo
                        is AiContentProgress.Failed -> throw IllegalStateException(value.message)
                        else -> Unit
                    }
                }
                result ?: throw IllegalStateException("A geração terminou sem resultado. Tente de novo.")
            }
        }
        ServerContentGenerationDialog(
            topicTitle = topic.title,
            taskId = taskId,
            onDone = { estudo -> generating = false; br.com.estudario.ui.ai.BackgroundAiTasks.dismiss(taskId); onDismiss(); viewModel.openIncomingText(estudo, competition.id) },
            onBackground = { br.com.estudario.ui.ai.BackgroundAiTasks.sendToBackground(taskId); generating = false; onDismiss() },
            onClose = { br.com.estudario.ui.ai.BackgroundAiTasks.dismiss(taskId); generating = false },
        )
    }
}

private fun orderedTree(topics: List<TopicEntity>): List<Pair<TopicEntity, Int>> {
    val result = mutableListOf<Pair<TopicEntity, Int>>()
    fun visit(parent: Long?, depth: Int) {
        topics.filter { it.parentTopicId == parent }.sortedWith(compareBy({ it.position }, { it.title })).forEach { topic ->
            result += topic to depth
            visit(topic.id, depth + 1)
        }
    }
    visit(null, 0)
    // Tópicos órfãos (pai removido) não podem sumir da lista.
    topics.filter { topic -> result.none { it.first.id == topic.id } }.forEach { result += it to 0 }
    return result
}

// ---------------------------------------------------------------------------------------------
// Plano de estudos
// ---------------------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlanPromptBuilderDialog(viewModel: AppViewModel, onDismiss: () -> Unit, onPickFile: () -> Unit) {
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val questions by viewModel.questions.collectAsState()
    var competitionId by remember { mutableStateOf((competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull())?.id) }
    val competition: CompetitionEntity? = competitions.firstOrNull { it.id == competitionId }
    LaunchedEffect(competitionId) { competitionId?.let(viewModel::ensureExternalIds) }

    var options by remember { mutableStateOf(PlanPromptOptions()) }
    val dayMinutes = remember { mutableStateListOf(*options.dayMinutes.toTypedArray()) }
    val priorities = remember { mutableStateMapOf<String, PlanPriority>() }
    var pickExamDate by remember { mutableStateOf(false) }
    var dailyPreset by remember { mutableIntStateOf(-1) }
    var studioPlan by remember { mutableStateOf(false) }

    val competitionSubjects: List<SubjectEntity> = subjects.filter { it.competitionId == competitionId }.sortedBy { it.position }
    val subjectInfos = remember(competitionSubjects, topics, questions) {
        val topicsBySubject = topics.groupBy { it.subjectId }
        val subjectByTopic = topics.associate { it.id to it.subjectId }
        val stats = questions.groupBy { subjectByTopic[it.question.topicId] }
        competitionSubjects.map { subject ->
            val subjectQuestions = stats[subject.id].orEmpty()
            val answered = subjectQuestions.sumOf { it.question.answerCount }
            val correct = subjectQuestions.sumOf { it.question.correctCount }
            PlanSubjectInfo(
                id = PromptIds.subject(subject),
                name = subject.name,
                topics = orderedTree(topicsBySubject[subject.id].orEmpty()).map { (topic, _) -> PlanTopicInfo(PromptIds.topic(topic), topic.title, topic.status != TopicStatus.NAO_ESTUDADO) },
                answered = answered,
                accuracyPercent = if (answered == 0) null else correct * 100 / answered,
            )
        }
    }
    val effective = options.copy(dayMinutes = dayMinutes.toList(), priorities = priorities.toMap())
    val datesValid = effective.examDate == null || !effective.examDate.isBefore(effective.startDate)
    val prompt = remember(effective, subjectInfos, competition) {
        if (competition == null || !datesValid) "" else PlanPromptBuilder.build(PromptIds.competition(competition), competition.name, subjectInfos, effective)
    }

    if (pickExamDate) {
        val state = rememberDatePickerState(initialSelectedDateMillis = options.examDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli())
        DatePickerDialog(
            onDismissRequest = { pickExamDate = false },
            confirmButton = { TextButton(onClick = { options = options.copy(examDate = state.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate() }); pickExamDate = false }) { Text("OK") } },
            dismissButton = { TextButton(onClick = { pickExamDate = false }) { Text("Cancelar") } },
        ) { DatePicker(state) }
    }

    val steps = listOf(
        WizardStep(
            "Objetivo",
            "Isso define o ritmo e o equilíbrio entre teoria, questões e revisão.",
            when {
                competition == null -> "Crie ou importe um concurso na aba Concursos antes de gerar o plano."
                competitionSubjects.isEmpty() -> "Este concurso ainda não tem matérias. Importe o edital primeiro."
                options.planName.isBlank() -> "Dê um nome ao plano."
                else -> null
            },
            question = "Olá! Vamos montar seu plano${competition?.let { " para ${it.name}" } ?: ""}. Qual é o seu objetivo agora?",
            answer = options.objective.label,
        ) {
            if (competitions.size > 1) OptionSection("Concurso") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    competitions.forEach { item -> FilterChip(selected = item.id == competitionId, onClick = { competitionId = item.id; priorities.clear() }, label = { Text(item.name) }) }
                }
            }
            ChoiceCards(PlanObjective.entries, options.objective, { it.label }, { options = options.copy(objective = it) }, description = { it.text }, icon = ::objectiveIcon)
            OutlinedTextField(options.planName, { options = options.copy(planName = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Nome do plano") })
        },
        WizardStep(
            "Momento atual",
            "Assim eu sei quanto de teoria nova entra no plano.",
            question = "Em que ponto da preparação você está?",
            answer = "${options.studyProfile.label} · ${options.method.label}",
        ) {
            ChoiceCards(StudyProfile.entries, options.studyProfile, { it.label }, { options = options.copy(studyProfile = it) }, description = { it.summary }, icon = ::profileIcon)
            OptionSection("Como prefere estudar?") {
                ChoiceCards(PlanMethod.entries, options.method, { it.label }, { options = options.copy(method = it) }, description = { it.text })
            }
        },
        WizardStep(
            "Datas",
            "Horizontes curtos geram tarefas mais detalhadas. Ao terminar, é só gerar o próximo bloco.",
            "A data da prova não pode ser anterior ao início do plano.".takeIf { !datesValid },
            question = "Quando é a prova e para quantas semanas eu detalho as tarefas?",
            answer = (options.examDate?.let { "Prova em ${it.format(dateFormat)}" } ?: "Sem data de prova") + " · ${options.horizonWeeks} semanas",
        ) {
            FlowRow(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { pickExamDate = true }) { Text(options.examDate?.let { "Prova: ${it.format(dateFormat)}" } ?: "Definir data da prova") }
                if (options.examDate != null) TextButton(onClick = { options = options.copy(examDate = null) }) { Text("Ainda sem data") }
            }
            BigValueSlider(
                value = options.horizonWeeks,
                range = 1..12,
                step = 1,
                format = { if (it == 1) "1 semana" else "$it semanas" },
                onChange = { options = options.copy(horizonWeeks = it) },
                caption = if (datesValid) "De ${options.startDate.format(dateFormat)} até ${PlanPromptBuilder.endDate(effective).format(dateFormat)}" else null,
                quickValues = listOf(2, 4, 8, 12),
            )
        },
        WizardStep(
            "Disponibilidade",
            "Arraste cada barra para marcar o tempo líquido do dia, já sem pausas. Zero é folga.",
            "Defina pelo menos um dia com tempo de estudo.".takeIf { dayMinutes.none { it > 0 } },
            question = "Quanto tempo você tem por dia?",
            answer = "${durationText(dayMinutes.sum())} por semana · blocos de ${durationText(options.blockMinutes)}",
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(60, 120, 180, 240).forEachIndexed { index, minutes ->
                    FilterChip(selected = dailyPreset == index, onClick = { dailyPreset = index; for (day in 0..4) dayMinutes[day] = minutes }, label = { Text("${minutes / 60}h seg a sex") })
                }
            }
            WeekHoursPicker(dayMinutes, onChange = { day, minutes -> dayMinutes[day] = minutes; dailyPreset = -1 })
            OptionSection("Cada bloco de estudo dura") {
                BigValueSlider(
                    value = options.blockMinutes,
                    range = 25..120,
                    step = 5,
                    format = ::durationText,
                    onChange = { options = options.copy(blockMinutes = it) },
                    quickValues = listOf(25, 50, 90),
                )
            }
        },
        WizardStep(
            "Metas",
            "Questões são o que mais fixa o conteúdo. Arraste para ajustar.",
            question = "Quantas questões por semana você quer fazer?",
            answer = "${options.weeklyQuestions} questões por semana" + if (options.includeSimulations) " · com simulados" else "",
        ) {
            BigValueSlider(
                value = options.weeklyQuestions,
                range = 0..500,
                step = 25,
                format = { "$it" },
                onChange = { options = options.copy(weeklyQuestions = it) },
                caption = "questões por semana",
                quickValues = listOf(50, 100, 200, 300),
            )
            ToggleRow("Incluir simulados", "Um simulado a cada 2 a 4 semanas", options.includeSimulations) { options = options.copy(includeSimulations = it) }
            ToggleRow("Treinar discursivas", if (options.monthlyDiscursives == 0) "Sem discursivas" else "${options.monthlyDiscursives} por mês", options.monthlyDiscursives > 0) { options = options.copy(monthlyDiscursives = if (it) 2 else 0) }
            if (options.monthlyDiscursives > 0) Slider(options.monthlyDiscursives.toFloat(), { options = options.copy(monthlyDiscursives = it.roundToInt().coerceAtLeast(1)) }, valueRange = 1f..8f, steps = 6)
        },
        WizardStep(
            "Prioridades",
            "Críticas recebem mais tempo; as de prioridade baixa ficam só em manutenção.",
            question = "Quais matérias precisam de mais atenção?",
            answer = listOf(PlanPriority.CRITICAL, PlanPriority.HIGH, PlanPriority.MEDIUM, PlanPriority.LOW)
                .mapNotNull { priority -> subjectInfos.count { (priorities[it.id] ?: PlanPriority.MEDIUM) == priority }.takeIf { it > 0 }?.let { "$it ${priorityLabel(priority).lowercase()}" } }
                .joinToString(" · ")
                .ifBlank { "Sem matérias" },
        ) {
            subjectInfos.forEach { info ->
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(info.name + (info.accuracyPercent?.let { " · $it% de acerto" } ?: ""), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    PrioritySelector(priorities[info.id] ?: PlanPriority.MEDIUM) { priorities[info.id] = it }
                }
            }
            OutlinedTextField(options.planPreference, { options = options.copy(planPreference = it.take(300)) }, Modifier.fillMaxWidth(), minLines = 2, label = { Text("Algo mais que eu deva saber? (opcional)") }, placeholder = { Text("Ex.: priorizar Português; revisar aos domingos") })
            ToggleRow("Usar os tópicos do edital", "Tarefas por tópico, na ordem certa (${subjectInfos.sumOf { it.topics.size }} tópicos)", options.includeTopics) { options = options.copy(includeTopics = it) }
            ToggleRow("Usar meu desempenho", "Reforça as matérias com menos acertos", options.includePerformance) { options = options.copy(includePerformance = it) }
        },
    )
    val priorityCounts = subjectInfos.groupingBy { priorities[it.id] ?: PlanPriority.MEDIUM }.eachCount()
    val summary = listOf(
        WizardSummaryItem("Plano", listOf(options.planName.ifBlank { "Sem nome" }, options.objective.label).joinToString(" · "), 0),
        WizardSummaryItem("Momento e método", "${options.studyProfile.label} · ${options.method.label}", 1),
        WizardSummaryItem(
            "Período",
            if (datesValid) "${options.startDate.format(dateFormat)} a ${PlanPromptBuilder.endDate(effective).format(dateFormat)}" +
                (options.examDate?.let { " · prova em ${it.format(dateFormat)}" } ?: "")
            else "Datas inválidas",
            2,
        ),
        WizardSummaryItem("Disponibilidade", "${durationText(dayMinutes.sum())} por semana · blocos de ${durationText(options.blockMinutes)}", 3),
        WizardSummaryItem("Metas", "${options.weeklyQuestions} questões por semana" + if (options.includeSimulations) " · com simulados" else "", 4),
        WizardSummaryItem(
            "Prioridades",
            listOf(PlanPriority.CRITICAL, PlanPriority.HIGH, PlanPriority.MEDIUM, PlanPriority.LOW)
                .mapNotNull { priority -> priorityCounts[priority]?.let { "$it ${priorityLabel(priority).lowercase()}" } }
                .joinToString(" · ")
                .ifBlank { "Nenhuma matéria" },
            5,
        ),
    )

    GenerationWizard(
        title = "Plano de estudos com o assistente",
        subtitle = competition?.name ?: "Crie um concurso na aba Concursos primeiro",
        steps = steps,
        summary = summary,
        prompt = prompt,
        onDismiss = onDismiss,
        onImportText = { onDismiss(); viewModel.openIncomingText(it) },
        onPickFile = { onDismiss(); onPickFile() },
        returnFileLabel = "Abrir arquivo .plano",
        server = ServerGenerationOption(
            description = "Monta o plano com as respostas desta conversa e mostra tudo para você conferir antes de salvar. Usa 1 geração de plano do seu plano.",
            enabled = competition != null && competitionSubjects.isNotEmpty() && datesValid && dayMinutes.any { it > 0 },
            disabledReason = "Responda às etapas acima para liberar.".takeIf { competition == null || competitionSubjects.isEmpty() || !datesValid || dayMinutes.none { it > 0 } },
            onGenerate = { studioPlan = true },
        ),
    )
    // IA do Estudário: usa exatamente as respostas do assistente e entrega um .plano pelo mesmo
    // caminho de importação (prévia e confirmação).
    if (studioPlan && competition != null) {
        br.com.estudario.ui.ai.StudyPlanAiScreen(
            competitionExternalId = PromptIds.competition(competition),
            competitionName = competition.name,
            prepare = { runCatching { br.com.estudario.data.ai.StudyPlanAi.prepare(PromptIds.competition(competition), competition.name, subjectInfos, effective) }.getOrNull() },
            onPlano = { plano -> studioPlan = false; onDismiss(); viewModel.openIncomingText(plano) },
            onFallback = { studioPlan = false },
            onClose = { studioPlan = false },
            onBackground = { studioPlan = false; onDismiss() },
        )
    }
}

private fun priorityLabel(value: PlanPriority) = when (value) {
    PlanPriority.CRITICAL -> "Crítica"
    PlanPriority.HIGH -> "Alta"
    PlanPriority.MEDIUM -> "Média"
    PlanPriority.LOW -> "Baixa"
}

private fun blockDescription(block: ContentBlock): String = when (block) {
    ContentBlock.THEORY -> "Explicação completa, em capítulos"
    ContentBlock.SUMMARY -> "O essencial em poucas páginas"
    ContentBlock.QUICK_REVIEW -> "Baralho para estudar por repetição"
    ContentBlock.TIPS_TRAPS -> "Dicas e pegadinhas de banca"
    ContentBlock.ACTIVE_RECALL -> "Perguntas para testar a memória"
    ContentBlock.QUESTIONS -> "Questões de treino com gabarito comentado"
    ContentBlock.ERROR_CONCEPTS -> "Onde a maioria erra e por quê"
}

private fun blockIcon(block: ContentBlock): ImageVector = when (block) {
    ContentBlock.THEORY -> Icons.Outlined.MenuBook
    ContentBlock.SUMMARY -> Icons.Outlined.Summarize
    ContentBlock.QUICK_REVIEW -> Icons.Outlined.Bolt
    ContentBlock.TIPS_TRAPS -> Icons.Outlined.Lightbulb
    ContentBlock.ACTIVE_RECALL -> Icons.Outlined.Psychology
    ContentBlock.QUESTIONS -> Icons.Outlined.Quiz
    ContentBlock.ERROR_CONCEPTS -> Icons.Outlined.ReportProblem
}

private fun styleTitle(style: QuestionStyle): String = when (style) {
    QuestionStyle.MIXED -> "Misturado"
    QuestionStyle.FIVE_OPTIONS -> "Múltipla escolha (A a E)"
    QuestionStyle.FOUR_OPTIONS -> "Múltipla escolha (A a D)"
    QuestionStyle.TRUE_FALSE -> "Certo ou Errado (C/E)"
}

private fun styleDescription(style: QuestionStyle): String = when (style) {
    QuestionStyle.MIXED -> "Um pouco de múltipla escolha e um pouco de Certo ou Errado"
    QuestionStyle.FIVE_OPTIONS -> "Cinco alternativas, uma correta. O formato mais comum (FGV, FCC, Vunesp)"
    QuestionStyle.FOUR_OPTIONS -> "Quatro alternativas, uma correta"
    QuestionStyle.TRUE_FALSE -> "Você julga se a afirmação está certa ou errada. É o estilo do Cebraspe"
}

/** Banca opcional: em branco, a IA segue o estilo das provas anteriores deste concurso. */
@Composable
private fun BoardField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value, onChange, Modifier.fillMaxWidth(), singleLine = true,
        label = { Text("Banca (opcional)") },
        placeholder = { Text("Ex.: Cebraspe, FGV, FCC") },
        supportingText = { Text("Em branco, as questões seguem o estilo das provas anteriores deste concurso.") },
    )
}

/** Opção de a IA trabalhar em cima de um material da pessoa (lei, apostila, PDF). */
@Composable
private fun MaterialBaseSection(useOwn: Boolean, attachment: PromptAttachment?, onUseOwnChange: (Boolean) -> Unit, onPick: () -> Unit, onClear: () -> Unit) {
    ToggleRow(
        "Usar um material meu como base",
        if (useOwn) "O material que você anexar é a base" else "Sem anexo, o Estudário pesquisa em fontes oficiais",
        useOwn,
        onUseOwnChange,
    )
    if (useOwn) AttachmentPicker(attachment, "Anexar lei, apostila ou PDF", onPick, onClear)
}

private fun depthDescription(depth: TheoryDepth): String = when (depth) {
    TheoryDepth.ESSENTIAL -> "Direto ao ponto"
    TheoryDepth.DEEP -> "Com exemplos e exceções"
    TheoryDepth.BOOK -> "O mais completo possível"
}

private fun objectiveIcon(objective: PlanObjective): ImageVector = when (objective) {
    PlanObjective.APPROVAL -> Icons.Outlined.EmojiEvents
    PlanObjective.COVER_SYLLABUS -> Icons.Outlined.Checklist
    PlanObjective.QUESTIONS -> Icons.Outlined.Quiz
    PlanObjective.FINAL_REVIEW -> Icons.Outlined.Flag
}

private fun profileIcon(profile: StudyProfile): ImageVector = when (profile) {
    StudyProfile.DO_ZERO -> Icons.Outlined.School
    StudyProfile.RETA_FINAL -> Icons.Outlined.Timer
    else -> Icons.Outlined.TrendingUp
}

/** Prioridade da matéria em quatro botões lado a lado, com a cor de cada nível. */
@Composable
private fun PrioritySelector(selected: PlanPriority, onSelect: (PlanPriority) -> Unit) {
    val colors = estudarioColors()
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(PlanPriority.CRITICAL, PlanPriority.HIGH, PlanPriority.MEDIUM, PlanPriority.LOW).forEach { priority ->
            val tone = when (priority) {
                PlanPriority.CRITICAL -> Color(0xFFC0392B)
                PlanPriority.HIGH -> colors.attention
                PlanPriority.MEDIUM -> colors.current
                PlanPriority.LOW -> colors.upcoming
            }
            val isSelected = priority == selected
            Surface(
                onClick = { onSelect(priority) },
                modifier = Modifier.weight(1f),
                shape = EstudarioShapes.pill,
                color = if (isSelected) tone else MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, if (isSelected) tone else MaterialTheme.colorScheme.outlineVariant),
            ) {
                Text(
                    priorityLabel(priority),
                    Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

/**
 * O compromisso do Estudário com o material, à vista enquanto ele trabalha: é o que faz a pessoa
 * confiar no que vai estudar.
 */
@Composable
private fun ContentCommitmentsCard() {
    androidx.compose.material3.Surface(
        shape = br.com.estudario.ui.theme.EstudarioShapes.panel,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.VerifiedUser, null, Modifier.size(20.dp), tint = br.com.estudario.ui.theme.estudarioColors().completed)
                Spacer(Modifier.width(8.dp))
                Text("Como preparamos seu material", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            ContentCommitments.forEach { (head, line) ->
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Outlined.CheckCircle, null, Modifier.size(18.dp).padding(top = 1.dp), tint = br.com.estudario.ui.theme.estudarioColors().completed)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(head, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        Text(line, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
