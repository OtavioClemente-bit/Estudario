package br.com.meuconcurso.ui.planner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.meuconcurso.MeuConcursoApplication
import br.com.meuconcurso.data.local.planner.StudyAvailabilityEntity
import br.com.meuconcurso.data.planner.*
import br.com.meuconcurso.data.transfer.planner.PlanImportMode
import br.com.meuconcurso.data.transfer.planner.*
import br.com.meuconcurso.domain.planner.PlanPriority
import br.com.meuconcurso.domain.planner.ReplanReason
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneId

class StudyPlanViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MeuConcursoApplication
    private val repository = app.planRepository
    private val service = app.planService
    private val executions = app.executionService
    private val transferService = app.planTransferService

    private val selected = MutableStateFlow(PlanSection.TODAY)
    private val _state = MutableStateFlow(ActivePlanUiState(loading = true))
    val state = _state.asStateFlow()
    private val _transfer = MutableStateFlow<PlanTransferUiState>(PlanTransferUiState.Idle)
    val transfer = _transfer.asStateFlow()
    val competitions = app.repository.competitions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val subjects = app.repository.subjects.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            combine(repository.plans, competitions, selected) { plans, contests, section -> Triple(plans, contests, section) }
                .collectLatest { (plans, contests, section) ->
                    val primary = contests.firstOrNull { it.isPrimary }?.id
                    val plan = plans.firstOrNull { it.active && !it.archived && (primary == null || it.competitionId == primary) }
                        ?: plans.firstOrNull { it.active && !it.archived }
                    _state.value = if (plan == null) ActivePlanUiState(selectedSection = section, allPlans = plans)
                    else load(plan.id, plans, section)
                }
        }
    }

    private suspend fun load(planId: String, plans: List<br.com.meuconcurso.data.local.planner.StudyPlanEntity>, section: PlanSection): ActivePlanUiState {
        val plan = repository.plan(planId) ?: return ActivePlanUiState(allPlans = plans, selectedSection = section)
        val availability = repository.availabilityOnce(planId)
        val mapped = StudyPlanUiMapper.map(
            plan = plan,
            tasks = repository.tasksOnce(planId),
            executions = repository.executionsOnce(planId),
            today = LocalDate.now(),
            allPlans = plans,
            availability = availability,
            planSubjects = repository.subjectsOnce(planId),
            annual = repository.annualOnce(planId),
            monthly = repository.monthlyOnce(planId),
            weekly = repository.weeklyOnce(planId),
            dayOverrides = repository.dayOverridesOnce(planId),
            section = section,
        )
        val weeklyCapacity = availability.sumOf { if (it.unavailable) 0 else it.availableMinutes }
        val remaining = mapped.tasks.filter { it.entity.status in setOf(br.com.meuconcurso.domain.planner.PlanTaskStatus.PLANEJADA, br.com.meuconcurso.domain.planner.PlanTaskStatus.EM_ANDAMENTO) }.sumOf { (it.entity.plannedMinutes - it.actualMinutes).coerceAtLeast(0) }
        val forecast = br.com.meuconcurso.domain.planner.StudyPlanForecastCalculator.forecast(LocalDate.now(), remaining, weeklyCapacity, 0)
        val master = plans.firstOrNull { it.competitionId == plan.competitionId && it.masterPlan && !it.archived }
        val alerts = if (master == null) emptyList() else {
            val masterSubjects = repository.subjectsOnce(master.id).filter { it.priority == PlanPriority.CRITICAL }.map { br.com.meuconcurso.domain.planner.MasterSubject(it.subjectId, it.subjectNameSnapshot, it.priority) }
            val last = repository.allExecutionsOnce().filter { it.competitionId == plan.competitionId && it.subjectId != null }.groupBy { it.subjectId!! }.mapValues { (_, values) -> values.maxOf { Instant.ofEpochMilli(it.completedAt).atZone(ZoneId.systemDefault()).toLocalDate() } }
            br.com.meuconcurso.domain.planner.MasterPlanMonitor.evaluate(LocalDate.now(), 7, masterSubjects, last)
        }
        val planSubjectIds = mapped.planSubjects.mapTo(hashSetOf()) { it.subjectId }
        val topics = app.database.dao().topicsOnce().filter { it.subjectId in planSubjectIds }.associateBy { it.id }
        val attemptsByQuestion = app.database.dao().attemptsOnce().groupBy { it.questionId }
        val weakTopics = app.database.dao().questionsOnce().groupBy { it.question.topicId }.mapNotNull { (topicId, questions) ->
            val attempts = questions.flatMap { attemptsByQuestion[it.question.id].orEmpty() }
            val accuracy = if (attempts.isEmpty()) 100 else attempts.count { it.correct } * 100 / attempts.size
            if (attempts.size >= 10 && accuracy < 65) ContextWeakTopic(topics[topicId]?.title ?: return@mapNotNull null, accuracy, attempts.size) else null
        }.sortedBy { it.accuracyPercent }
        return mapped.copy(forecastDate = forecast.estimatedDate, masterAlerts = alerts, weakTopics = weakTopics)
    }

    fun selectSection(section: PlanSection) { selected.value = section }

    fun createPlan(input: CreatePlanInput, generate: Boolean = true) = launch {
        val id = service.createPlan(input.copy(active = true))
        service.activate(id)
        if (generate) service.replan(id, ReplanReason.INITIAL)
    }
    fun activate(id: String) = launch { service.activate(id) }
    fun markMaster(id: String) = launch { service.markMaster(id) }
    fun archive(id: String) = launch { service.archive(id) }
    fun restore(id: String) = launch { service.restore(id) }
    fun duplicate(id: String, name: String) = launch { service.duplicate(id, name) }
    fun generate() = _state.value.activePlan?.id?.let { id -> launch { service.replan(id, ReplanReason.INITIAL) } }
    fun start(taskId: String) = launch { executions.start(taskId) }
    fun complete(taskId: String, input: CompleteTaskInput) = launch { executions.complete(taskId, input); service.replan(_state.value.activePlan!!.id, ReplanReason.TASK_COMPLETED) }
    fun skip(taskId: String, reason: String) = launch { executions.skip(taskId, reason); service.replan(_state.value.activePlan!!.id, ReplanReason.TASK_SKIPPED) }
    fun reprogram(taskId: String, date: LocalDate?) = launch { executions.reprogram(taskId, date) }
    fun toggleTaskLock(taskId: String, locked: Boolean) = launch { service.setTaskLocked(taskId, locked) }
    fun toggleDayLock(date: LocalDate, locked: Boolean) = _state.value.activePlan?.id?.let { id -> launch { service.setDayLocked(id, date, locked) } }
    fun updateAvailability(values: List<StudyAvailabilityEntity>) = _state.value.activePlan?.id?.let { id -> launch { service.updateAvailability(id, values) } }
    fun updateSubject(subjectId: Long, priority: PlanPriority, paused: Boolean) = _state.value.activePlan?.id?.let { id -> launch { service.updateSubject(id, subjectId, priority, paused) } }

    fun inspectPlan(raw: String) = launch {
        _transfer.value = PlanTransferUiState.Loading
        _transfer.value = PlanTransferUiState.Preview(raw, transferService.preview(raw))
    }
    fun importPlan(raw: String, mode: PlanImportMode, active: Boolean, master: Boolean) = launch {
        val result = transferService.import(raw, mode, active, master)
        _transfer.value = PlanTransferUiState.Success("Plano importado: ${result.tasksInserted} tarefa(s) nova(s), ${result.tasksProtected} protegida(s).")
    }
    fun clearTransfer() { _transfer.value = PlanTransferUiState.Idle }
    suspend fun exportPlan(id: String): String = transferService.exportPlan(id)
    suspend fun exportActivePlan(): String = exportPlan(_state.value.activePlan?.id ?: error("Nenhum plano ativo."))
    fun exportContextJson(): String = PlanContextExporter.exportJson(context())
    fun exportContextText(): String = PlanContextExporter.exportText(context())

    private fun context(): PlanContext {
        val current = _state.value
        val plan = current.activePlan ?: error("Nenhum plano ativo.")
        val rows = current.tasks
        val bySubject = rows.groupBy { it.entity.subjectNameSnapshot }.map { (name, tasks) -> ContextSubject(name, tasks.first().entity.priority.name, tasks.sumOf { it.entity.plannedMinutes }, tasks.sumOf { it.actualMinutes }) }
        val future = rows.filter { it.entity.scheduledEpochDay >= current.today.toEpochDay() }.take(30).map { ContextTask(it.entity.subjectNameSnapshot, it.entity.topicNameSnapshot, it.entity.type.name, it.entity.plannedMinutes, LocalDate.ofEpochDay(it.entity.scheduledEpochDay).toString()) }
        val missed = rows.filter { it.entity.status == br.com.meuconcurso.domain.planner.PlanTaskStatus.NAO_REALIZADA }.map { ContextTask(it.entity.subjectNameSnapshot, it.entity.topicNameSnapshot, it.entity.type.name, it.entity.plannedMinutes, LocalDate.ofEpochDay(it.entity.scheduledEpochDay).toString()) }
        val weeklyCapacity = current.availability.sumOf { if (it.unavailable) 0 else it.availableMinutes }
        val planned = current.weekTasks.sumOf { it.entity.plannedMinutes }; val actual = current.weekTasks.sumOf { it.actualMinutes }
        return PlanContext(
            competition = competitions.value.firstOrNull { it.id == plan.competitionId }?.name ?: "Concurso",
            objective = plan.objective,
            annualPhase = current.annualPhases.firstOrNull { current.today.toEpochDay() in it.startEpochDay..it.endEpochDay }?.name,
            currentMonth = "%04d-%02d".format(current.today.year, current.today.monthValue),
            currentWeek = "${current.weekStart}/${current.weekStart.plusDays(6)}",
            weeklyCapacityMinutes = weeklyCapacity,
            plannedMinutes = planned,
            actualMinutes = actual,
            questions = current.weekTasks.sumOf { it.questionsDone },
            correct = current.weekTasks.sumOf { it.correctAnswers },
            adherencePercent = if (planned == 0) 0 else (actual * 100 / planned).coerceAtMost(100),
            capacityDeficitMinutes = current.deficitMinutes,
            subjects = bySubject,
            weakTopics = current.weakTopics,
            missedTasks = missed,
            futureTasks = future,
            alerts = current.masterAlerts.map { "${it.subjectName} sem estudo há ${it.inactiveDays} dias." },
        )
    }

    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        runCatching { block() }.onFailure { error ->
            _state.update { it.copy(message = error.message ?: "Não foi possível concluir a ação.") }
            if (_transfer.value is PlanTransferUiState.Loading) _transfer.value = PlanTransferUiState.Error(error.message ?: "Arquivo inválido.")
        }
    }
    fun consumeMessage() { _state.update { it.copy(message = null) } }

    companion object {
        fun defaultAvailability(planId: String = "pending", minutes: Int = 120) = (1..7).map { day ->
            StudyAvailabilityEntity(planId, day, if (day == 7) 0 else minutes, unavailable = day == 7)
        }
        fun subjectInputs(subjects: List<br.com.meuconcurso.data.local.SubjectEntity>) = subjects.mapIndexed { index, subject ->
            PlanSubjectInput(subject.id, subject.name, PlanPriority.MEDIUM, 0, false, index)
        }
    }
}
