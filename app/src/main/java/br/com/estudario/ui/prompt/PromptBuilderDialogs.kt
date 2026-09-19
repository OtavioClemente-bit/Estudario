package br.com.estudario.ui.prompt

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.tour.TutorialVideo
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val attachmentTypes = arrayOf("application/pdf", "image/*", "text/plain")
private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private const val MULTI_TOPIC_WARNING = "Para preservar a profundidade e facilitar a conferência das fontes, recomendamos gerar um tópico por vez. Solicitações com vários tópicos podem produzir respostas incompletas ou afirmações sem respaldo. Revise o conteúdo antes de estudar."

// ---------------------------------------------------------------------------------------------
// Edital
// ---------------------------------------------------------------------------------------------

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

    PromptBuilderDialog(
        title = "Montar edital com IA",
        subtitle = "Matérias, tópicos e subtópicos a partir do edital",
        steps = listOf(
            "Escolha as opções abaixo. Banca, ano e anexo são opcionais; o PDF oficial é recomendado quando você tiver acesso a ele.",
            "Toque em Enviar para a IA e escolha o ChatGPT, Gemini ou outro app. Se não anexar o PDF agora, a IA pesquisará fontes oficiais quando possível.",
            "Baixe o arquivo .estudo gerado e abra com o Estudário (ou copie a resposta e toque em Colar resposta).",
        ),
        prompt = prompt,
        onDismiss = onDismiss,
        onImportText = { onDismiss(); viewModel.openIncomingText(it) },
        onPickFile = { onDismiss(); onPickFile() },
        attachment = attachment.takeIf { options.source == EditalSource.ATTACH_PDF },
        tutorial = TutorialVideo.EDITAL,
    ) {
        OptionSection("Concurso", "Crie um novo ou complete um concurso que já existe no app. Banca e ano são opcionais.") {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = target == null, onClick = { targetId = null }, label = { Text("Novo concurso") })
                competitions.forEach { competition -> FilterChip(selected = competition.id == targetId, onClick = { targetId = competition.id }, label = { Text(competition.name) }) }
            }
            if (target == null) {
                OutlinedTextField(options.competitionName, { options = options.copy(competitionName = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Nome do concurso (ex.: TRT-3)") })
            }
            OutlinedTextField(options.role, { options = options.copy(role = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Cargo ou área (opcional)") })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(options.board, { options = options.copy(board = it) }, Modifier.weight(1f), singleLine = true, label = { Text("Banca (opcional)") })
                OutlinedTextField(options.year, { value -> options = options.copy(year = value.filter(Char::isDigit).take(4)) }, Modifier.width(110.dp), singleLine = true, label = { Text("Ano (opcional)") })
            }
        }
        OptionSection("Como a IA vai receber o edital") {
            ChoiceChips(EditalSource.entries, options.source, { it.label }) { options = options.copy(source = it) }
            if (options.source == EditalSource.ATTACH_PDF) {
                AttachmentPicker(attachment, "Anexar PDF do edital (opcional)", { attach.launch(attachmentTypes) }, { attachment = null })
                if (attachment == null) Text("Recomendado: com o edital oficial anexado, a IA consegue seguir exatamente as matérias e os tópicos. Sem o arquivo, ela deve pesquisar fontes oficiais.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            else Text("O prompt termina com o espaço “TEXTO DO EDITAL”: cole o conteúdo programático logo depois, no app de IA.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        OptionSection("O que incluir") {
            ChoiceChips(EditalScope.entries, options.scope, { it.label }) { options = options.copy(scope = it) }
        }
        OptionSection("Nível de detalhe", "Dividir itens longos cria subtópicos didáticos, marcados como subdivisão de estudo.") {
            ChoiceChips(EditalDetail.entries, options.detail, { it.label }) { options = options.copy(detail = it) }
        }
        OptionSection("Extras") {
            ToggleRow("Descrição curta em cada tópico", "Uma frase com o escopo do assunto", options.includeDescriptions) { options = options.copy(includeDescriptions = it) }
            ToggleRow("Prioridade pelo peso na prova", "Matérias com mais questões ficam como prioridade alta", options.priorityByWeight) { options = options.copy(priorityByWeight = it) }
            if (target == null) ToggleRow("Tornar concurso principal", null, options.makePrimary) { options = options.copy(makePrimary = it) }
        }
    }
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

    PromptBuilderDialog(
        title = if (singleTopicMode) "Conteúdo do tópico com IA" else "Gerar conteúdo por tópico",
        subtitle = if (singleTopicMode) topics.firstOrNull { it.id in selectedIds }?.let { ContentPromptBuilder.pathOf(it, topics) } ?: subject.name else subject.name,
        steps = listOf(
            if (singleTopicMode) "Escolha o que a IA deve gerar para este tópico." else "Recomendado: escolha o tópico que vai estudar agora e o que a IA deve gerar.",
            "Toque em Enviar para a IA e escolha o app. Nomes e IDs já vão certinhos no pedido.",
            "Baixe o .estudo gerado e abra com o Estudário, ou copie a resposta e toque em Colar resposta.",
        ),
        prompt = prompt,
        onDismiss = onDismiss,
        onImportText = { onDismiss(); viewModel.openIncomingText(it) },
        onPickFile = { onDismiss(); onPickFile() },
        attachment = attachment.takeIf { options.source == MaterialSource.ATTACHED },
        shareEnabled = selectedIds.isNotEmpty(),
        disabledReason = "Marque pelo menos um tópico.",
        tutorial = TutorialVideo.CONTENT,
        shareWarning = MULTI_TOPIC_WARNING.takeIf { selectedIds.size > 1 },
    ) {
        if (!singleTopicMode) OptionSection("Tópicos", "Selecione o tópico que vai estudar agora. Para obter uma resposta mais completa e verificar as fontes, recomendamos gerar um tópico por vez. Tópicos com ✓ já têm conteúdo.") {
            if (ordered.isEmpty()) Text("Esta matéria ainda não tem tópicos. Adicione tópicos ou importe o edital primeiro.", color = MaterialTheme.colorScheme.error)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = { selected.clear(); selected.addAll(ordered.map { it.first }.filter { it.id !in withContent }.take(1).map { it.id }) }, label = { Text("Próximo sem conteúdo") })
                AssistChip(onClick = { selected.clear() }, label = { Text("Limpar") })
            }
            ElevatedCard {
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
        }
        OptionSection("O que gerar") {
            MultiChoiceChips(ContentBlock.entries, options.blocks, { it.label }) { options = options.copy(blocks = it) }
        }
        if (ContentBlock.THEORY in options.blocks) OptionSection("Profundidade da teoria") {
            ChoiceChips(TheoryDepth.entries, options.depth, { it.label }) { options = options.copy(depth = it) }
        }
        if (ContentBlock.QUESTIONS in options.blocks) OptionSection("Questões") {
            Text("${options.questionCount} questões${if (selectedIds.size > 1) " por tópico" else ""}", fontWeight = FontWeight.SemiBold)
            Slider(options.questionCount.toFloat(), { options = options.copy(questionCount = it.toInt()) }, valueRange = 5f..30f, steps = 4)
            ChoiceChips(QuestionStyle.entries, options.style, { it.label }) { options = options.copy(style = it) }
            // Órgão e esfera decidem QUAL lei se aplica. É o dado que a IA não deduz com segurança,
            // e errar isso faz a pessoa estudar o estatuto de outro ente do começo ao fim.
            OutlinedTextField(
                options.agency,
                { options = options.copy(agency = it) },
                Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Órgão do concurso (opcional)") },
                supportingText = { Text("Define qual estatuto e qual legislação se aplicam") },
            )
            ChoiceChips(LegalSphere.entries, options.sphere, { it.label }) { options = options.copy(sphere = it) }
            ChoiceChips(QuestionDifficulty.entries, options.difficulty, { it.label }) { options = options.copy(difficulty = it) }
            OutlinedTextField(options.board, { options = options.copy(board = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Banca (estilo das questões)") })
        }
        OptionSection("Fonte do conteúdo") {
            ChoiceChips(MaterialSource.entries, options.source, { it.label }) { options = options.copy(source = it) }
            if (options.source == MaterialSource.ATTACHED) AttachmentPicker(attachment, "Anexar lei, apostila ou PDF", { attach.launch(attachmentTypes) }, { attachment = null })
        }
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
    val prompt = remember(effective, subjectInfos, competition) {
        if (competition == null) "" else PlanPromptBuilder.build(PromptIds.competition(competition), competition.name, subjectInfos, effective)
    }

    if (pickExamDate) {
        val state = rememberDatePickerState(initialSelectedDateMillis = options.examDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli())
        DatePickerDialog(
            onDismissRequest = { pickExamDate = false },
            confirmButton = { TextButton(onClick = { options = options.copy(examDate = state.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate() }); pickExamDate = false }) { Text("OK") } },
            dismissButton = { TextButton(onClick = { pickExamDate = false }) { Text("Cancelar") } },
        ) { DatePicker(state) }
    }

    PromptBuilderDialog(
        title = "Plano de estudos com IA",
        subtitle = competition?.name ?: "Crie um concurso no Edital primeiro",
        steps = listOf(
            "Ajuste seus dias, horários, prioridades e metas.",
            "Toque em Enviar para a IA. O pedido já leva suas matérias, tópicos e desempenho.",
            "Baixe o arquivo .plano gerado e abra com o Estudário, ou copie a resposta e toque em Colar resposta.",
        ),
        prompt = prompt,
        onDismiss = onDismiss,
        onImportText = { onDismiss(); viewModel.openIncomingText(it) },
        onPickFile = { onDismiss(); onPickFile() },
        shareEnabled = competition != null && competitionSubjects.isNotEmpty() && dayMinutes.any { it > 0 },
        disabledReason = when {
            competition == null -> "Crie ou importe um concurso no Edital antes de gerar o plano."
            competitionSubjects.isEmpty() -> "Este concurso ainda não tem matérias. Importe o edital primeiro."
            else -> "Defina pelo menos um dia com tempo de estudo."
        },
    ) {
        if (competitions.size > 1) OptionSection("Concurso") {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                competitions.forEach { item -> FilterChip(selected = item.id == competitionId, onClick = { competitionId = item.id; priorities.clear() }, label = { Text(item.name) }) }
            }
        }
        OptionSection("Objetivo") {
            OutlinedTextField(options.planName, { options = options.copy(planName = it) }, Modifier.fillMaxWidth(), singleLine = true, label = { Text("Nome do plano") })
            ChoiceChips(PlanObjective.entries, options.objective, { it.label }) { options = options.copy(objective = it) }
            Text(options.objective.text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        OptionSection("Datas") {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { pickExamDate = true }) { Text(options.examDate?.let { "Prova: ${it.format(dateFormat)}" } ?: "Definir data da prova") }
                if (options.examDate != null) TextButton(onClick = { options = options.copy(examDate = null) }) { Text("Sem data") }
            }
            Text("Tarefas detalhadas para:", style = MaterialTheme.typography.bodyMedium)
            ChoiceChips(listOf(2, 4, 8, 12), options.horizonWeeks, { "$it semanas" }) { options = options.copy(horizonWeeks = it) }
            val end = PlanPromptBuilder.endDate(effective)
            Text("De ${options.startDate.format(dateFormat)} até ${end.format(dateFormat)}. Horizontes curtos cabem melhor numa resposta; depois é só gerar o próximo bloco.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (options.examDate != null && options.examDate!!.isBefore(LocalDate.now())) Text("A data da prova está no passado.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        OptionSection("Tempo líquido por dia", "Já descontando pausas. Deixe em 0 os dias de folga.") {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(60, 120, 180, 240).forEachIndexed { index, minutes ->
                    FilterChip(selected = dailyPreset == index, onClick = { dailyPreset = index; for (day in 0..4) dayMinutes[day] = minutes }, label = { Text("${minutes / 60}h seg–sex") })
                }
            }
            listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom").forEachIndexed { index, label ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(label, Modifier.width(40.dp), fontWeight = FontWeight.SemiBold)
                    Slider(dayMinutes[index].toFloat(), { dayMinutes[index] = (it / 15f).toInt() * 15; dailyPreset = -1 }, Modifier.weight(1f), valueRange = 0f..480f, steps = 31)
                    Text(if (dayMinutes[index] == 0) "folga" else minutesText(dayMinutes[index]), Modifier.width(64.dp), style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("Total: ${minutesText(dayMinutes.sum())} por semana", fontWeight = FontWeight.SemiBold)
        }
        OptionSection("Metas") {
            Text("${options.weeklyQuestions} questões por semana")
            Slider(options.weeklyQuestions.toFloat(), { options = options.copy(weeklyQuestions = (it / 25f).toInt() * 25) }, valueRange = 0f..500f, steps = 19)
            Text(if (options.monthlyDiscursives == 0) "Sem discursivas" else "${options.monthlyDiscursives} discursiva(s) por mês")
            Slider(options.monthlyDiscursives.toFloat(), { options = options.copy(monthlyDiscursives = it.toInt()) }, valueRange = 0f..8f, steps = 7)
            ToggleRow("Incluir simulados", "Um simulado a cada 2 a 4 semanas", options.includeSimulations) { options = options.copy(includeSimulations = it) }
        }
        OptionSection("Método") {
            ChoiceChips(PlanMethod.entries, options.method, { it.label }) { options = options.copy(method = it) }
            Text(options.method.text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (subjectInfos.isNotEmpty()) OptionSection("Prioridade de cada matéria", "Crítica recebe mais tempo; baixa só manutenção.") {
            subjectInfos.forEach { info ->
                Column(Modifier.fillMaxWidth()) {
                    Text(info.name + (info.accuracyPercent?.let { " • $it% de acerto" } ?: ""), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    ChoiceChips(listOf(PlanPriority.CRITICAL, PlanPriority.HIGH, PlanPriority.MEDIUM, PlanPriority.LOW), priorities[info.id] ?: PlanPriority.MEDIUM, { priorityLabel(it) }) { priorities[info.id] = it }
                }
            }
        }
        OptionSection("O que mandar para a IA") {
            ToggleRow("Tópicos do edital", "Permite tarefas por tópico, na ordem certa (${subjectInfos.sumOf { it.topics.size }} tópicos)", options.includeTopics) { options = options.copy(includeTopics = it) }
            ToggleRow("Meu desempenho", "Acertos por matéria, para reforçar as mais fracas", options.includePerformance) { options = options.copy(includePerformance = it) }
        }
    }
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
