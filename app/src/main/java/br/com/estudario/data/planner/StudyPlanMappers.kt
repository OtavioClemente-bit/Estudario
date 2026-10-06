package br.com.estudario.data.planner

import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.local.planner.StudyTaskExecutionEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlannerTask
import br.com.estudario.domain.planner.TaskExecution
import kotlinx.datetime.LocalDate
import br.com.estudario.time.*
import kotlinx.datetime.TimeZone

internal fun PlanTaskEntity.toDomain() = PlannerTask(
    id = id,
    planId = planId,
    subjectId = subjectId,
    topicId = topicId,
    date = LocalDate.fromEpochDays(scheduledEpochDay),
    type = type,
    plannedMinutes = plannedMinutes,
    plannedQuestions = plannedQuestions,
    priority = priority,
    status = status,
    locked = locked,
    origin = origin,
    replannedFromTaskId = replannedFromTaskId,
    sequence = sequence,
)

internal fun StudyTaskExecutionEntity.toDomain(zoneId: TimeZone = TimeZone.currentSystemDefault()) = TaskExecution(
    id = id,
    taskId = taskId,
    subjectId = subjectId,
    topicId = topicId,
    date = epochMillisToDate(completedAt, zoneId),
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
    sequence = sequence,
)

internal fun PlanTaskEntity.withStatus(status: PlanTaskStatus, revision: Long) = copy(
    status = status,
    updatedRevision = revision,
    updatedAt = System.currentTimeMillis(),
)
