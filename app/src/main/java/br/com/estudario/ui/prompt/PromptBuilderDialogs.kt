package br.com.estudario.ui.prompt

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
        WizardStep("Concurso e cargo", "Crie um novo concurso ou complete um que já está no app. Banca e ano ajudam a localizar o edital correto.", competitionError) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = target == null, onClick = { targetId = null }, label = { Text("Novo concurso") })
                competitions.forEach { competition -> FilterChip(selected = competition.id == targetId, onClick = { targetId = competition.id }, label = { Text(competition.name) }) }
            }
            if (target == null) {
                OutlinedTextField(options.competitionName, { options = options.copy(competitionName = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Nome do concurso *") }, placeholder = { Text("Ex.: TRT 3ª Região") })
            }
            OutlinedTextField(options.role, { options = options.copy(role = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Cargo ou área *") }, placeholder = { Text("Ex.: Analista Judiciário, Área Administrativa") })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(options.board, { options = options.copy(board = it) }, Modifier.weight(1f), singleLine = true, label = { Text("Banca") })
                OutlinedTextField(options.year, { value -> options = options.copy(year = value.filter(Char::isDigit).take(4)) }, Modifier.width(110.dp), singleLine = true, label = { Text("Ano") })
            }
        },
        WizardStep("O que extrair", "Defina a parte do edital que interessa e como os tópicos devem ficar.") {
            OptionSection("Abrangência") { ChoiceChips(EditalScope.entries, options.scope, { it.label }) { options = options.copy(scope = it) } }
            OptionSection("Nível de detalhe", "Dividir itens longos cria subtópicos didáticos, marcados como subdivisão de estudo.") {
                ChoiceChips(EditalDetail.entries, options.detail, { it.label }) { options = options.copy(detail = it) }
            }
            ToggleRow("Descrição curta em cada tópico", "Uma frase com o escopo do assunto", options.includeDescriptions) { options = options.copy(includeDescriptions = it) }
            ToggleRow("Prioridade pelo peso na prova", "Matérias com mais questões ficam como prioridade alta", options.priorityByWeight) { options = options.copy(priorityByWeight = it) }
            if (target == null) ToggleRow("Tornar concurso principal", null, options.makePrimary) { options = options.copy(makePrimary = it) }
        },
        WizardStep("Edital oficial", "Com o PDF do edital, a IA segue exatamente as matérias e os tópicos publicados.") {
            ChoiceChips(EditalSource.entries, options.source, { it.label }) { options = options.copy(source = it) }
            if (options.source == EditalSource.ATTACH_PDF) {
                AttachmentPicker(attachment, "Escolher PDF do edital", { attach.launch(attachmentTypes) }, { attachment = null })
                if (attachment == null) Text("Sem o arquivo, a IA precisa pesquisar fontes oficiais, o que é menos preciso.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text("O pedido termina com o espaço “TEXTO DO EDITAL”: cole o conteúdo programático logo depois, no app de IA.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
    )
    val summary = listOf(
        WizardSummaryItem("Concurso", effective.competitionName.ifBlank { "Não informado" }, 0),
        WizardSummaryItem("Cargo", listOf(effective.role, effective.board, effective.year).filter(String::isNotBlank).joinToString(" · ").ifBlank { "Não informado" }, 0),
        WizardSummaryItem("Extração", "${options.scope.label} · ${options.detail.label}", 1),
        WizardSummaryItem("Edital", if (options.source == EditalSource.PASTE_TEXT) "Texto colado na IA" else attachment?.name ?: "Sem PDF anexado", 2),
    )
    val server = ServerGenerationOption(
        description = "Lê o PDF oficial, extrai matérias e tópicos e mostra tudo para você revisar antes de salvar. Usa 1 geração de edital do seu plano.",
        enabled = target != null && options.source == EditalSource.ATTACH_PDF,
        disabledReason = when {
            target == null -> "Disponível para concursos já criados no app. Crie o concurso primeiro ou use outra IA."
            options.source != EditalSource.ATTACH_PDF -> "A IA do Estudário trabalha com o PDF oficial do edital."
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
        title = "Gerar edital com IA",
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

    if (subject == null || competition == null) { LaunchedEffect(Unit) { onDismiss() }; return }
    val selectedIds = selected.toSet()
    val prompt = remember(options, selectedIds, topics, subject, competition) {
        if (selectedIds.isEmpty()) "" else ContentPromptBuilder.build(competition, subject, topics, selectedIds, options)
    }

    val maxQuestions = GenerationLimits.maxQuestions()
    val wholeSubject = !singleTopicMode && ordered.isNotEmpty() && selectedIds.size == ordered.size
    val singleTopic = topics.firstOrNull { it.id in selectedIds }.takeIf { selectedIds.size == 1 }

    val steps = buildList {
        if (!singleTopicMode) add(WizardStep(
            "Abrangência",
            "Gerar um tópico por vez produz material mais profundo e fácil de conferir. Tópicos com ✓ já têm conteúdo.",
            when {
                ordered.isEmpty() -> "Esta matéria ainda não tem tópicos. Adicione tópicos ou importe o edital primeiro."
                selectedIds.isEmpty() -> "Selecione ao menos um tópico."
                else -> null
            },
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !wholeSubject,
                    onClick = { selected.clear(); selected.addAll(ordered.map { it.first }.filter { it.id !in withContent }.take(1).map { it.id }) },
                    label = { Text("Tópico específico") },
                )
                FilterChip(
                    selected = wholeSubject,
                    enabled = ordered.isNotEmpty(),
                    onClick = { selected.clear(); selected.addAll(ordered.map { it.first.id }) },
                    label = { Text("Matéria completa (${ordered.size} tópicos)") },
                )
            }
            if (!wholeSubject) ElevatedCard {
                Column(Modifier.padding(vertical = 4.dp)) {
                    ordered.forEach { (topic, depth) ->
                        val checked = topic.id in selectedIds
                        Row(
                            Modifier.fillMaxWidth().clickable { if (checked) selected.remove(topic.id) else selected.add(topic.id) }.padding(start = (8 + depth * 18).dp, end = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(checked, { if (it) selected.add(topic.id) else selected.remove(topic.id) })
                            Text(topic.title, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                            if (topic.id in withContent) Text("✓", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            if (selectedIds.size > 1) Text("${selectedIds.size} tópicos selecionados. $MULTI_TOPIC_WARNING", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        })
        add(WizardStep(
            "O que gerar",
            "Escolha os materiais que quer receber. Comece pelo material completo e desmarque o que não precisa.",
            "Escolha pelo menos um tipo de material.".takeIf { options.blocks.isEmpty() },
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = { options = options.copy(blocks = ContentBlock.entries.toSet()) }, label = { Text("Material completo") })
                AssistChip(onClick = { options = options.copy(blocks = setOf(ContentBlock.QUESTIONS)) }, label = { Text("Só questões") })
            }
            MultiChoiceChips(ContentBlock.entries, options.blocks, { it.label }) { options = options.copy(blocks = it) }
            if (ContentBlock.THEORY in options.blocks) OptionSection("Profundidade da teoria") {
                ChoiceChips(TheoryDepth.entries, options.depth, { it.label }) { options = options.copy(depth = it) }
            }
        })
        if (ContentBlock.QUESTIONS in options.blocks) add(WizardStep(
            "Questões",
            "Quantidade, formato e nível das questões de treino.",
            "Defina a quantidade de questões.".takeIf { options.questionCount !in GenerationLimits.MIN_QUESTIONS..maxQuestions },
        ) {
            QuestionCountPicker(options.questionCount, maxQuestions, perTopic = selectedIds.size > 1) { options = options.copy(questionCount = it) }
            OptionSection("Formato") { ChoiceChips(QuestionStyle.entries, options.style, { it.label }) { options = options.copy(style = it) } }
            OptionSection("Dificuldade") { ChoiceChips(QuestionDifficulty.entries, options.difficulty, { it.label }) { options = options.copy(difficulty = it) } }
            OutlinedTextField(options.board, { options = options.copy(board = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Banca") }, supportingText = { Text("Orienta o estilo das questões") })
        })
        // Órgão e esfera decidem QUAL lei se aplica. É o dado que a IA não deduz com segurança,
        // e errar isso faz a pessoa estudar o estatuto de outro ente do começo ao fim.
        add(WizardStep("Contexto e fontes", "Órgão e esfera definem qual estatuto e qual legislação valem para o seu concurso.") {
            OutlinedTextField(options.agency, { options = options.copy(agency = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Órgão do concurso") }, placeholder = { Text("Ex.: TRT 3ª Região") })
            OptionSection("Esfera") { ChoiceChips(LegalSphere.entries, options.sphere, { it.label }) { options = options.copy(sphere = it) } }
            OptionSection("Fonte do conteúdo") {
                ChoiceChips(MaterialSource.entries, options.source, { it.label }) { options = options.copy(source = it) }
                if (options.source == MaterialSource.ATTACHED) AttachmentPicker(attachment, "Anexar lei, apostila ou PDF", { attach.launch(attachmentTypes) }, { attachment = null })
            }
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
            "Contexto",
            listOf(
                options.agency.ifBlank { "Órgão não informado" },
                options.sphere.label,
                if (options.source == MaterialSource.ATTACHED) attachment?.name ?: "Sem anexo" else options.source.label,
            ).joinToString(" · "),
            stepOf("Contexto e fontes"),
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
        tutorial = TutorialVideo.CONTENT,
        externalWarning = MULTI_TOPIC_WARNING.takeIf { selectedIds.size > 1 },
    )
}

/** Quantidade de questões limitada ao plano atual (grátis: até [GenerationLimits.FREE_MAX_QUESTIONS]). */
@Composable
private fun QuestionCountPicker(value: Int, max: Int, perTopic: Boolean, onChange: (Int) -> Unit) {
    val current = value.coerceIn(GenerationLimits.MIN_QUESTIONS, max)
    LaunchedEffect(value, max) { if (value != current) onChange(current) }
    Text("$current questões${if (perTopic) " por tópico" else ""}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    Slider(current.toFloat(), { onChange(it.roundToInt()) }, valueRange = GenerationLimits.MIN_QUESTIONS.toFloat()..max.toFloat(), steps = (max - GenerationLimits.MIN_QUESTIONS - 1).coerceAtLeast(0))
    Text("Seu plano permite até $max questões por geração.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    val maxQuestions = GenerationLimits.maxQuestions()

    val steps = listOf(
        WizardStep(
            "Quantidade e formato",
            "Quantas questões você quer resolver e em qual formato.",
            "Defina a quantidade de questões.".takeIf { options.questionCount !in GenerationLimits.MIN_QUESTIONS..maxQuestions },
        ) {
            QuestionCountPicker(options.questionCount, maxQuestions, perTopic = false) { options = options.copy(questionCount = it) }
            OptionSection("Formato") { ChoiceChips(QuestionStyle.entries, options.style, { it.label }) { options = options.copy(style = it) } }
        },
        WizardStep("Nível e banca", "A banca orienta o estilo dos enunciados e das alternativas.") {
            OptionSection("Dificuldade") { ChoiceChips(QuestionDifficulty.entries, options.difficulty, { it.label }) { options = options.copy(difficulty = it) } }
            OutlinedTextField(options.board, { options = options.copy(board = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Banca") })
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
    )
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
            "Qual é o foco deste plano?",
            when {
                competition == null -> "Crie ou importe um concurso no Edital antes de gerar o plano."
                competitionSubjects.isEmpty() -> "Este concurso ainda não tem matérias. Importe o edital primeiro."
                options.planName.isBlank() -> "Dê um nome ao plano."
                else -> null
            },
        ) {
            if (competitions.size > 1) OptionSection("Concurso") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    competitions.forEach { item -> FilterChip(selected = item.id == competitionId, onClick = { competitionId = item.id; priorities.clear() }, label = { Text(item.name) }) }
                }
            }
            OutlinedTextField(options.planName, { options = options.copy(planName = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Nome do plano *") })
            ChoiceChips(PlanObjective.entries, options.objective, { it.label }) { options = options.copy(objective = it) }
            Text(options.objective.text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        WizardStep("Momento atual", "Em que ponto da preparação você está? Isso define a proporção entre teoria, questões e revisão.") {
            ChoiceChips(StudyProfile.entries, options.studyProfile, { it.label }) { options = options.copy(studyProfile = it) }
            Text(options.studyProfile.summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OptionSection("Método") {
                ChoiceChips(PlanMethod.entries, options.method, { it.label }) { options = options.copy(method = it) }
                Text(options.method.text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        WizardStep(
            "Datas",
            "Horizontes curtos geram tarefas mais detalhadas. Ao terminar, é só gerar o próximo bloco.",
            "A data da prova não pode ser anterior ao início do plano.".takeIf { !datesValid },
        ) {
            FlowRow(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { pickExamDate = true }) { Text(options.examDate?.let { "Prova: ${it.format(dateFormat)}" } ?: "Definir data da prova") }
                if (options.examDate != null) TextButton(onClick = { options = options.copy(examDate = null) }) { Text("Sem data") }
            }
            OptionSection("Tarefas detalhadas para") {
                ChoiceChips(listOf(2, 4, 8, 12), options.horizonWeeks, { "$it semanas" }) { options = options.copy(horizonWeeks = it) }
            }
            if (datesValid) {
                val end = PlanPromptBuilder.endDate(effective)
                Text("De ${options.startDate.format(dateFormat)} até ${end.format(dateFormat)}.", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            }
        },
        WizardStep(
            "Disponibilidade",
            "Tempo líquido por dia, já descontando pausas. Deixe em zero os dias de folga.",
            "Defina pelo menos um dia com tempo de estudo.".takeIf { dayMinutes.none { it > 0 } },
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(60, 120, 180, 240).forEachIndexed { index, minutes ->
                    FilterChip(selected = dailyPreset == index, onClick = { dailyPreset = index; for (day in 0..4) dayMinutes[day] = minutes }, label = { Text("${minutes / 60}h de segunda a sexta") })
                }
            }
            listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom").forEachIndexed { index, label ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(label, Modifier.width(40.dp), fontWeight = FontWeight.SemiBold)
                    Slider(dayMinutes[index].toFloat(), { dayMinutes[index] = (it / 15f).toInt() * 15; dailyPreset = -1 }, Modifier.weight(1f), valueRange = 0f..480f, steps = 31)
                    Text(if (dayMinutes[index] == 0) "folga" else minutesText(dayMinutes[index]), Modifier.widthIn(min = 64.dp), style = MaterialTheme.typography.bodySmall, maxLines = 1)
                }
            }
            Text("Total: ${minutesText(dayMinutes.sum())} por semana", fontWeight = FontWeight.SemiBold)
            OptionSection("Duração de cada bloco de estudo") {
                ChoiceChips(listOf(25, 50, 90), options.blockMinutes, { minutesText(it) }) { options = options.copy(blockMinutes = it) }
            }
        },
        WizardStep("Metas", "Quanto de prática você quer por semana.") {
            Text("${options.weeklyQuestions} questões por semana", fontWeight = FontWeight.SemiBold)
            Slider(options.weeklyQuestions.toFloat(), { options = options.copy(weeklyQuestions = (it / 25f).toInt() * 25) }, valueRange = 0f..500f, steps = 19)
            Text(if (options.monthlyDiscursives == 0) "Sem discursivas" else "${options.monthlyDiscursives} discursiva(s) por mês", fontWeight = FontWeight.SemiBold)
            Slider(options.monthlyDiscursives.toFloat(), { options = options.copy(monthlyDiscursives = it.toInt()) }, valueRange = 0f..8f, steps = 7)
            ToggleRow("Incluir simulados", "Um simulado a cada 2 a 4 semanas", options.includeSimulations) { options = options.copy(includeSimulations = it) }
        },
        WizardStep("Prioridades", "Matérias críticas recebem mais tempo; as de prioridade baixa ficam só em manutenção.") {
            subjectInfos.forEach { info ->
                Column(Modifier.fillMaxWidth()) {
                    Text(info.name + (info.accuracyPercent?.let { " • $it% de acerto" } ?: ""), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    ChoiceChips(listOf(PlanPriority.CRITICAL, PlanPriority.HIGH, PlanPriority.MEDIUM, PlanPriority.LOW), priorities[info.id] ?: PlanPriority.MEDIUM, { priorityLabel(it) }) { priorities[info.id] = it }
                }
            }
            OutlinedTextField(options.planPreference, { options = options.copy(planPreference = it.take(300)) }, Modifier.fillMaxWidth(), minLines = 2, label = { Text("Observações (opcional)") }, placeholder = { Text("Ex.: priorizar Português; revisar aos domingos") })
            ToggleRow("Enviar tópicos do edital", "Permite tarefas por tópico, na ordem certa (${subjectInfos.sumOf { it.topics.size }} tópicos)", options.includeTopics) { options = options.copy(includeTopics = it) }
            ToggleRow("Enviar meu desempenho", "Acertos por matéria, para reforçar as mais fracas", options.includePerformance) { options = options.copy(includePerformance = it) }
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
        WizardSummaryItem("Disponibilidade", "${minutesText(dayMinutes.sum())} por semana · blocos de ${minutesText(options.blockMinutes)}", 3),
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
        title = "Plano de estudos com IA",
        subtitle = competition?.name ?: "Crie um concurso no Edital primeiro",
        steps = steps,
        summary = summary,
        prompt = prompt,
        onDismiss = onDismiss,
        onImportText = { onDismiss(); viewModel.openIncomingText(it) },
        onPickFile = { onDismiss(); onPickFile() },
        returnFileLabel = "Abrir arquivo .plano",
    )
}

private fun priorityLabel(value: PlanPriority) = when (value) {
    PlanPriority.CRITICAL -> "Crítica"
    PlanPriority.HIGH -> "Alta"
    PlanPriority.MEDIUM -> "Média"
    PlanPriority.LOW -> "Baixa"
}

private fun minutesText(minutes: Int): String = when {
    minutes < 60 -> "$minutes min"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h${"%02d".format(minutes % 60)}"
}
