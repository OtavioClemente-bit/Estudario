package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.data.local.planner.AvailabilityMode
import br.com.estudario.data.local.planner.StudyAvailabilityEntity
import br.com.estudario.data.planner.CreatePlanInput
import br.com.estudario.data.planner.PlanSubjectInput
import br.com.estudario.domain.planner.*
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private val PASSOS = listOf("Objetivo", "Concurso", "Momento", "Tempo", "Peso das matérias", "Metas", "Prévia")
private val DIAS = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")

/**
 * Assistente do plano sem IA. Cada passo é uma pergunta que muda o cronograma de verdade, e o
 * último passo mostra a prévia calculada com as mesmas regras que vão gerar as tarefas — dá para
 * voltar e ajustar antes de criar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanWizardScreen(
    competitions: List<CompetitionEntity>,
    subjects: List<SubjectEntity>,
    topics: List<TopicEntity>,
    onCancel: () -> Unit,
    onCreate: (CreatePlanInput) -> Unit,
) {
    var step by remember { mutableIntStateOf(0) }
    var competitionId by remember { mutableLongStateOf(competitions.firstOrNull { it.isPrimary }?.id ?: competitions.firstOrNull()?.id ?: 0) }
    var name by remember { mutableStateOf("Meu plano de estudos") }
    var objective by remember { mutableStateOf("Concluir o edital com revisões e questões") }
    var profile by remember { mutableStateOf(StudyProfile.DO_ZERO) }
    var examDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dailyMinutes by remember { mutableFloatStateOf(120f) }
    var advanced by remember { mutableStateOf(false) }
    val advancedMinutes = remember { mutableStateListOf(120, 120, 120, 120, 120, 180, 0) }
    var blockMinutes by remember { mutableIntStateOf(50) }
    var interleave by remember { mutableStateOf(true) }
    val weights = remember { mutableStateMapOf<Long, Int>() }
    var weeklyQuestions by remember { mutableIntStateOf(80) }
    var questionsPerTopic by remember { mutableIntStateOf(15) }
    var simulations by remember { mutableIntStateOf(1) }
    var discursives by remember { mutableIntStateOf(0) }

    val selectedSubjects = subjects.filter { it.competitionId == competitionId }.sortedBy { it.position }
    val subjectIds = selectedSubjects.mapTo(hashSetOf()) { it.id }
    val selectedTopics = topics.filter { it.subjectId in subjectIds }

    // Trocar o perfil traz metas que combinam com ele; depois disso a pessoa pode ajustar à vontade.
    LaunchedEffect(profile) {
        val preset = StudyMethodConfig.forProfile(profile)
        weeklyQuestions = preset.weeklyQuestionsTarget
        simulations = preset.simulationsPerMonth
    }

    val availability = remember(advanced, dailyMinutes, advancedMinutes.toList()) {
        if (advanced) advancedMinutes.mapIndexed { index, value -> StudyAvailabilityEntity("pending", index + 1, value, value == 0, AvailabilityMode.ADVANCED) }
        else StudyPlanViewModel.defaultAvailability(minutes = dailyMinutes.toInt()).map { it.copy(mode = AvailabilityMode.SIMPLE) }
    }
    val weeklyCapacity = availability.sumOf { if (it.unavailable) 0 else it.availableMinutes }
    val config = StudyMethodConfig(
        profile = profile,
        blockMinutes = blockMinutes,
        weeklyQuestionsTarget = weeklyQuestions,
        questionsPerTopic = questionsPerTopic,
        simulationsPerMonth = simulations,
        discursivesPerMonth = discursives,
        interleaveSubjects = interleave,
    )
    fun weightOf(subject: SubjectEntity) = weights[subject.id] ?: 3

    val preview = remember(config, selectedSubjects, selectedTopics, weeklyCapacity, examDate, weights.toMap()) {
        buildPreview(config, selectedSubjects, selectedTopics, availability, weeklyCapacity, examDate) { weights[it] ?: 3 }
    }

    val podeAvancar = when (step) {
        0 -> name.isNotBlank() && objective.isNotBlank()
        1 -> competitionId > 0 && selectedSubjects.isNotEmpty()
        3 -> weeklyCapacity > 0
        else -> true
    }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = examDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli())
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    examDate = pickerState.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate() }
                    showDatePicker = false
                }) { Text("Usar esta data") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } },
        ) { DatePicker(pickerState) }
    }

    Dialog(onDismissRequest = onCancel, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    Column {
                        TopAppBar(
                            title = { Column { Text("Criar plano sem IA", style = MaterialTheme.typography.titleMedium); Text("${step + 1} de ${PASSOS.size} • ${PASSOS[step]}", style = MaterialTheme.typography.bodySmall) } },
                            navigationIcon = { IconButton(onClick = onCancel) { Icon(Icons.Outlined.Close, "Fechar") } },
                        )
                        LinearProgressIndicator({ (step + 1) / PASSOS.size.toFloat() }, Modifier.fillMaxWidth())
                    }
                },
                bottomBar = {
                    Surface(tonalElevation = 3.dp) {
                        Row(
                            Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            OutlinedButton(onClick = { if (step > 0) step-- else onCancel() }, modifier = Modifier.weight(1f)) {
                                Text(if (step > 0) "Voltar" else "Cancelar")
                            }
                            Button(
                                onClick = {
                                    if (step < PASSOS.lastIndex) step++ else {
                                        onCreate(
                                            CreatePlanInput(
                                                competitionId = competitionId,
                                                name = name,
                                                objective = objective,
                                                startDate = LocalDate.now(),
                                                examDate = examDate,
                                                availability = availability,
                                                subjects = selectedSubjects.mapIndexed { index, subject ->
                                                    PlanSubjectInput(subject.id, subject.name, priorityFor(weightOf(subject)), 0, false, index)
                                                        .let { it.copy(weight = weightOf(subject)) }
                                                },
                                                active = true,
                                                method = config,
                                            ),
                                        )
                                        onCancel()
                                    }
                                },
                                enabled = podeAvancar && (step < PASSOS.lastIndex || selectedSubjects.isNotEmpty()),
                                modifier = Modifier.weight(1f),
                            ) { Text(if (step == PASSOS.lastIndex) "Criar e gerar" else "Continuar") }
                        }
                    }
                },
            ) { padding ->
                Column(
                    Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    when (step) {
                        0 -> {
                            Explicacao("Este é o plano montado pelo próprio app, sem IA. Ele segue sempre as mesmas regras — teoria com questões logo depois, revisão espaçada, rodízio de matérias por peso e simulado periódico — e na última tela você vê exatamente o que vai sair.")
                            OutlinedTextField(name, { name = it }, label = { Text("Nome do plano") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(objective, { objective = it }, label = { Text("Objetivo") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                        }
                        1 -> {
                            SecaoTitulo("Para qual concurso?")
                            competitions.forEach { item ->
                                FilterChip(competitionId == item.id, { competitionId = item.id }, { Text(item.name) })
                            }
                            if (selectedSubjects.isEmpty()) Aviso("Este concurso ainda não tem matérias. Monte o edital primeiro — é a única parte que realmente pede IA (ou digitação manual).")
                            else Text("${selectedSubjects.size} matéria(s) • ${selectedTopics.size} tópico(s) • ${selectedTopics.count { it.status == TopicStatus.NAO_ESTUDADO }} ainda não estudado(s)", style = MaterialTheme.typography.bodyMedium)
                        }
                        2 -> {
                            SecaoTitulo("Em que ponto você está?")
                            StudyProfile.entries.forEach { item ->
                                Escolha(item.label, item.summary, profile == item) { profile = item }
                            }
                            HorizontalDivider()
                            SecaoTitulo("Data da prova")
                            Text("Com a data, o plano se divide em Base, Aprofundamento e Reta final, e a divisão do tempo muda sozinha conforme a prova chega.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { showDatePicker = true }) { Text(examDate?.let { "%02d/%02d/%d".format(it.dayOfMonth, it.monthValue, it.year) } ?: "Escolher data") }
                                if (examDate != null) TextButton(onClick = { examDate = null }) { Text("Ainda não sei") }
                            }
                        }
                        3 -> {
                            SecaoTitulo("Quanto tempo você tem de verdade?")
                            Text("Conte só o tempo limpo de estudo, sem deslocamento e sem pausa. Plano que assume tempo que não existe atrasa na primeira semana.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(!advanced, { advanced = false }, { Text("Mesmo tempo todo dia") })
                                FilterChip(advanced, { advanced = true }, { Text("Dia a dia") })
                            }
                            if (!advanced) {
                                Text("${dailyMinutes.toInt()} minutos por dia, de segunda a sábado", fontWeight = FontWeight.Bold)
                                Slider(dailyMinutes, { dailyMinutes = it }, valueRange = 30f..360f, steps = 10)
                            } else {
                                DIAS.forEachIndexed { index, label ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("$label: ${advancedMinutes[index]} min", Modifier.width(110.dp), style = MaterialTheme.typography.bodySmall)
                                        Slider(advancedMinutes[index].toFloat(), { advancedMinutes[index] = it.toInt() }, Modifier.weight(1f), valueRange = 0f..480f, steps = 15)
                                    }
                                }
                            }
                            Text("Capacidade semanal: ${minutesLabel(weeklyCapacity)}", fontWeight = FontWeight.SemiBold)
                            HorizontalDivider()
                            SecaoTitulo("Tamanho do bloco")
                            Text("Toda tarefa sai como múltiplo desse bloco. Blocos menores rendem mais quando a concentração é curta.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                StudyMethodConfig.BLOCK_OPTIONS.forEach { option ->
                                    FilterChip(blockMinutes == option, { blockMinutes = option }, { Text("$option min") })
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text("Misturar matérias no mesmo dia")
                                    Text("Evita emendar horas da mesma matéria; ajuda a fixar melhor.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Switch(interleave, { interleave = it })
                            }
                        }
                        4 -> {
                            SecaoTitulo("Quanto cada matéria vale para você")
                            Text("Use o número de questões que a banca costuma cobrar. Peso 5 recebe cerca de cinco vezes o tempo de peso 1, e todas aparecem já na primeira semana.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            selectedSubjects.forEach { subject ->
                                val peso = weightOf(subject)
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(subject.name, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                        Text(pesoLabel(peso), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        (1..5).forEach { valor ->
                                            FilterChip(peso == valor, { weights[subject.id] = valor }, { Text("$valor") })
                                        }
                                    }
                                }
                            }
                        }
                        5 -> {
                            SecaoTitulo("Metas")
                            NumeroSlider("Questões por semana", weeklyQuestions, 0..500, 25) { weeklyQuestions = it }
                            NumeroSlider("Questões depois de cada tópico", questionsPerTopic, 0..40, 5) { questionsPerTopic = it }
                            NumeroSlider("Simulados por mês", simulations, 0..8, 1) { simulations = it }
                            NumeroSlider("Discursivas por mês", discursives, 0..12, 1) { discursives = it }
                            Explicacao("A meta de questões é fechada por baterias semanais quando as questões dos tópicos não dão conta sozinhas. Simulado e discursiva caem no seu dia mais livre.")
                        }
                        else -> PreviaDoPlano(preview, name, selectedSubjects, weeklyCapacity)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- prévia

internal data class WizardPreview(
    val phases: List<StudyPhase>,
    val currentPhase: StudyPhase,
    val notes: List<String>,
    val minutesBySubject: Map<Long, Int>,
    val minutesByType: Map<PlanTaskType, Int>,
    val weeklyCapacity: Int,
    val horizonCapacity: Int,
    val plannedMinutes: Int,
    val topicsInHorizon: Int,
    val remainingTopics: Int,
    val weeksToFinishSyllabus: Int?,
)

private fun buildPreview(
    config: StudyMethodConfig,
    subjects: List<SubjectEntity>,
    topics: List<TopicEntity>,
    availability: List<StudyAvailabilityEntity>,
    weeklyCapacity: Int,
    examDate: LocalDate?,
    weightOf: (Long) -> Int,
): WizardPreview {
    val today = LocalDate.now()
    val heaviest = availability.filterNot { it.unavailable }.maxByOrNull { it.availableMinutes }
        ?.let { DayOfWeek.of(it.dayOfWeek) } ?: DayOfWeek.SATURDAY
    val blueprintSubjects = subjects.mapIndexed { index, subject ->
        val weight = weightOf(subject.id)
        BlueprintSubject(subject.id, subject.name, priorityFor(weight), weight, index)
    }
    val blueprintTopics = topics.map { topic ->
        BlueprintTopic(
            topicId = topic.id,
            subjectId = topic.subjectId,
            title = topic.title,
            position = topic.position,
            depth = if (topic.parentTopicId == null) 0 else 1,
            studied = topic.status != TopicStatus.NAO_ESTUDADO,
        )
    }
    val result = StudyPlanBlueprint.build(
        BlueprintInput(
            today = today,
            planStart = today,
            examDate = examDate,
            config = config,
            subjects = blueprintSubjects,
            topics = blueprintTopics,
            weeklyCapacityMinutes = weeklyCapacity,
            heaviestDay = heaviest,
        ),
    )
    val remaining = blueprintTopics.count { !it.studied }
    val minutesPerTopic = config.blockMinutes + config.questionsPerTopic * config.minutesPerQuestion
    val theoryShare = result.currentPhase.mix.theoryPercent + result.currentPhase.mix.questionsPercent
    val weeklyForSyllabus = weeklyCapacity * theoryShare / 100
    return WizardPreview(
        phases = result.phases,
        currentPhase = result.currentPhase,
        notes = result.notes,
        minutesBySubject = result.demands.groupBy { it.subjectId }.mapValues { (_, list) -> list.sumOf { it.minutes } },
        minutesByType = result.demands.groupBy { it.type }.mapValues { (_, list) -> list.sumOf { it.minutes } },
        weeklyCapacity = weeklyCapacity,
        horizonCapacity = result.horizonCapacityMinutes,
        plannedMinutes = result.plannedMinutes,
        topicsInHorizon = result.demands.count { it.type == PlanTaskType.THEORY },
        remainingTopics = remaining,
        weeksToFinishSyllabus = if (weeklyForSyllabus <= 0 || remaining == 0) null else (remaining * minutesPerTopic + weeklyForSyllabus - 1) / weeklyForSyllabus,
    )
}

@Composable
private fun PreviaDoPlano(preview: WizardPreview, name: String, subjects: List<SubjectEntity>, weeklyCapacity: Int) {
    Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    if (subjects.isEmpty()) {
        Aviso("Sem matérias no edital não há o que planejar. Volte ao passo do concurso.")
        return
    }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Primeiras 4 semanas", fontWeight = FontWeight.Bold)
            Text("${minutesLabel(weeklyCapacity)} por semana • ${preview.topicsInHorizon} tópico(s) novo(s) no período")
            LinearProgressIndicator(
                { if (preview.horizonCapacity == 0) 0f else (preview.plannedMinutes.toFloat() / preview.horizonCapacity).coerceIn(0f, 1f) },
                Modifier.fillMaxWidth(),
            )
            Text(
                if (preview.plannedMinutes > preview.horizonCapacity) "A demanda passa da sua disponibilidade — o que não couber é remarcado automaticamente."
                else "${minutesLabel(preview.plannedMinutes)} planejados de ${minutesLabel(preview.horizonCapacity)} disponíveis.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Como o tempo é dividido", fontWeight = FontWeight.Bold)
            Text(preview.currentPhase.mix.describe(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            preview.minutesByType.entries.sortedByDescending { it.value }.forEach { (type, minutes) ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(type.label(), style = MaterialTheme.typography.bodySmall)
                    Text(minutesLabel(minutes), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Tempo por matéria (4 semanas)", fontWeight = FontWeight.Bold)
            val total = preview.minutesBySubject.values.sum().coerceAtLeast(1)
            subjects.forEach { subject ->
                val minutes = preview.minutesBySubject[subject.id] ?: 0
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(subject.name, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        Text("${minutesLabel(minutes)} • ${minutes * 100 / total}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                    LinearProgressIndicator({ minutes.toFloat() / total }, Modifier.fillMaxWidth())
                }
            }
        }
    }
    if (preview.phases.size > 1) {
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Fases até a prova", fontWeight = FontWeight.Bold)
                preview.phases.forEach { phase ->
                    Column {
                        Text("${phase.kind.label} • ${phase.days} dias", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                        Text("%02d/%02d a %02d/%02d • ${phase.mix.describe()}".format(phase.start.dayOfMonth, phase.start.monthValue, phase.end.dayOfMonth, phase.end.monthValue), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(phase.objective, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
    preview.weeksToFinishSyllabus?.let { weeks ->
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Previsão do edital", fontWeight = FontWeight.Bold)
                Text("${preview.remainingTopics} tópico(s) a ver, cerca de $weeks semana(s) nesse ritmo.")
                Text("Termina por volta de %02d/%02d/%d.".format(LocalDate.now().plusWeeks(weeks.toLong()).dayOfMonth, LocalDate.now().plusWeeks(weeks.toLong()).monthValue, LocalDate.now().plusWeeks(weeks.toLong()).year), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Por que este plano", fontWeight = FontWeight.Bold)
            preview.notes.forEach { note ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("•", color = MaterialTheme.colorScheme.primary)
                    Text(note, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

// ---------------------------------------------------------------- peças

internal fun priorityFor(weight: Int) = when (weight.coerceIn(1, 5)) {
    5 -> PlanPriority.CRITICAL
    4 -> PlanPriority.HIGH
    3 -> PlanPriority.MEDIUM
    else -> PlanPriority.LOW
}

private fun pesoLabel(weight: Int) = when (weight) {
    5 -> "Decisiva"; 4 -> "Muito importante"; 3 -> "Importante"; 2 -> "Complementar"; else -> "Leve"
}

@Composable private fun SecaoTitulo(text: String) { Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }

@Composable private fun Explicacao(text: String) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium) {
        Text(text, Modifier.padding(14.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable private fun Aviso(text: String) {
    Surface(color = MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.medium) {
        Text(text, Modifier.padding(14.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
    }
}

@Composable private fun Escolha(title: String, body: String, selected: Boolean, onClick: () -> Unit) {
    OutlinedCard(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
    ) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RadioButton(selected, onClick)
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable private fun NumeroSlider(label: String, value: Int, range: IntRange, step: Int, onChange: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("$value", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value.toFloat(),
            { onChange((it / step).toInt() * step) },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = ((range.last - range.first) / step - 1).coerceAtLeast(0),
        )
    }
}
