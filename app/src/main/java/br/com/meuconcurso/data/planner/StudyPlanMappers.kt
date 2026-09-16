package br.com.meuconcurso.data.planner

import br.com.meuconcurso.data.local.planner.PlanTaskEntity
import br.com.meuconcurso.data.local.planner.StudyTaskExecutionEntity
import br.com.meuconcurso.domain.planner.PlanTaskStatus
import br.com.meuconcurso.domain.planner.PlannerTask
import br.com.meuconcurso.domain.planner.TaskExecution
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

internal fun PlanTaskEntity.toDomain() = PlannerTask(
    id = id,
    planId = planId,
    subjectId = subjectId,
    topicId = topicId,
    date = LocalDate.ofEpochDay(scheduledEpochDay),
    type = type,
    plannedMinutes = plannedMinutes,
    plannedQuestions = plannedQuestions,
    priority = priority,
    status = status,
    locked = locked,
    origin = origin,
    replannedFromTaskId = replannedFromTaskId,
)

internal fun StudyTaskExecutionEntity.toDomain(zoneId: ZoneId = ZoneId.systemDefault()) = TaskExecution(
    id = id,
    taskId = taskId,
    subjectId = subjectId,
    topicId = topicId,
    date = Instant.ofEpochMilli(completedAt).atZone(zoneId).toLocalDate(),
    minutes = actualMinutes,
    questions = questionsDone,
    correct = correctAnswers,
)

internal fun PlannerTask.toEntity(
    competitionId: Long,
    revision: Long,
    subjectName: String,
    topicName: String?,
    previous: PlanTaskEntity? = null,
) = PlanTaskEntity(
    id = id,
    planId = planId,
    competitionId = competitionId,
    annualPhaseId = previous?.annualPhaseId,
    monthlyPlanId = previous?.monthlyPlanId,
    weeklyPlanId = previous?.weeklyPlanId,
    subjectId = subjectId,
    topicId = topicId,
    subjectNameSnapshot = subjectName,
    topicNameSnapshot = topicName,
    scheduledEpochDay = date.toEpochDay(),
    type = type,
    plannedMinutes = plannedMinutes,
    plannedQuestions = plannedQuestions,
    priority = priority,
    status = status,
    origin = origin,
    locked = locked,
    replannedFromTaskId = replannedFromTaskId,
    createdRevision = previous?.createdRevision ?: revision,
    updatedRevision = revision,
    createdAt = previous?.createdAt ?: System.currentTimeMillis(),
)

internal fun PlanTaskEntity.withStatus(status: PlanTaskStatus, revision: Long) = copy(
    status = status,
    updatedRevision = revision,
    updatedAt = System.currentTimeMillis(),
)
