package br.com.estudario.data.local.planner

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.estudario.domain.planner.PlanTaskType
import kotlinx.coroutines.flow.Flow

/** Projeção enxuta: o XP só precisa do tipo de cada tarefa, não da tarefa inteira. */
data class PlanTaskTypeRow(val id: String, val type: PlanTaskType)

@Dao
interface PlannerDao {
    @Query("SELECT * FROM study_plans ORDER BY archived, active DESC, masterPlan DESC, updatedAt DESC") fun plans(): Flow<List<StudyPlanEntity>>
    @Query("SELECT * FROM study_plans ORDER BY updatedAt DESC") suspend fun plansOnce(): List<StudyPlanEntity>
    @Query("SELECT * FROM study_plans WHERE id = :id") suspend fun plan(id: String): StudyPlanEntity?
    @Query("SELECT * FROM study_plans WHERE competitionId = :competitionId AND active = 1 AND archived = 0 LIMIT 1") fun activePlan(competitionId: Long): Flow<StudyPlanEntity?>
    @Query("SELECT * FROM study_plans WHERE competitionId = :competitionId AND active = 1 AND archived = 0 LIMIT 1") suspend fun activePlanOnce(competitionId: Long): StudyPlanEntity?
    @Query("SELECT * FROM study_plans WHERE competitionId = :competitionId AND masterPlan = 1 AND archived = 0 LIMIT 1") suspend fun masterPlanOnce(competitionId: Long): StudyPlanEntity?
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertPlan(value: StudyPlanEntity)
    @Update suspend fun updatePlan(value: StudyPlanEntity)
    @Query("UPDATE study_plans SET revision = revision + 1, updatedAt = :now WHERE id = :planId AND revision = :baseRevision") suspend fun claimRevision(planId: String, baseRevision: Long, now: Long): Int
    @Query("UPDATE study_plans SET active = CASE WHEN id = :planId THEN 1 ELSE 0 END WHERE competitionId = :competitionId AND archived = 0") suspend fun activateOnly(competitionId: Long, planId: String)
    @Query("UPDATE study_plans SET masterPlan = CASE WHEN id = :planId THEN 1 ELSE 0 END WHERE competitionId = :competitionId AND archived = 0") suspend fun markOnlyMaster(competitionId: Long, planId: String)
    @Query("UPDATE study_plans SET archived = 1, active = 0, masterPlan = 0, updatedAt = :now WHERE id = :planId") suspend fun archive(planId: String, now: Long)
    @Query("UPDATE study_plans SET archived = 0, active = 0, masterPlan = 0, updatedAt = :now WHERE id = :planId") suspend fun restore(planId: String, now: Long)
    /** Apaga o plano. As tarefas, execuções, fases e metas caem junto por CASCADE. */
    @Query("DELETE FROM study_plans WHERE id = :planId") suspend fun deletePlan(planId: String)
    @Query("SELECT COUNT(*) FROM study_task_executions WHERE planId = :planId") suspend fun executionCountFor(planId: String): Int

    @Insert suspend fun insertRevision(value: StudyPlanRevisionEntity)
    @Query("SELECT * FROM study_plan_revisions WHERE planId = :planId ORDER BY revision") suspend fun revisionsFor(planId: String): List<StudyPlanRevisionEntity>
    @Query("SELECT * FROM study_plan_revisions WHERE planId = :planId ORDER BY revision DESC LIMIT 1") suspend fun latestRevision(planId: String): StudyPlanRevisionEntity?
    @Query("SELECT * FROM study_plan_revisions ORDER BY planId, revision") suspend fun revisionsOnce(): List<StudyPlanRevisionEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAvailability(values: List<StudyAvailabilityEntity>)
    @Query("SELECT * FROM study_availability WHERE planId = :planId ORDER BY dayOfWeek") suspend fun availabilityFor(planId: String): List<StudyAvailabilityEntity>
    @Query("SELECT * FROM study_availability ORDER BY planId, dayOfWeek") suspend fun availabilityOnce(): List<StudyAvailabilityEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertDayOverride(value: StudyDayOverrideEntity)
    @Query("SELECT * FROM study_day_overrides WHERE planId = :planId") suspend fun dayOverridesFor(planId: String): List<StudyDayOverrideEntity>
    @Query("SELECT * FROM study_day_overrides ORDER BY planId, epochDay") suspend fun dayOverridesOnce(): List<StudyDayOverrideEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPlanSubjects(values: List<PlanSubjectEntity>)
    @Query("SELECT * FROM plan_subjects WHERE planId = :planId ORDER BY position") suspend fun subjectsFor(planId: String): List<PlanSubjectEntity>
    @Query("SELECT * FROM plan_subjects ORDER BY planId, position") suspend fun subjectsOnce(): List<PlanSubjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAnnualPhases(values: List<AnnualPhaseEntity>)
    @Query("SELECT * FROM annual_phases WHERE planId = :planId AND validUntilRevision IS NULL ORDER BY position") suspend fun currentAnnualPhases(planId: String): List<AnnualPhaseEntity>
    @Query("SELECT * FROM annual_phases ORDER BY planId, validFromRevision, position") suspend fun annualPhasesOnce(): List<AnnualPhaseEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertMonthlyPlans(values: List<MonthlyPlanEntity>)
    @Query("SELECT * FROM monthly_plans WHERE planId = :planId AND validUntilRevision IS NULL ORDER BY yearMonth") suspend fun currentMonthlyPlans(planId: String): List<MonthlyPlanEntity>
    @Query("SELECT * FROM monthly_plans ORDER BY planId, validFromRevision, yearMonth") suspend fun monthlyPlansOnce(): List<MonthlyPlanEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertWeeklyPlans(values: List<WeeklyPlanEntity>)
    @Query("SELECT * FROM weekly_plans WHERE planId = :planId AND validUntilRevision IS NULL ORDER BY weekStartEpochDay") suspend fun currentWeeklyPlans(planId: String): List<WeeklyPlanEntity>
    @Query("SELECT * FROM weekly_plans ORDER BY planId, validFromRevision, weekStartEpochDay") suspend fun weeklyPlansOnce(): List<WeeklyPlanEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertTasks(values: List<PlanTaskEntity>)
    @Update suspend fun updateTask(value: PlanTaskEntity)
    @Query("SELECT * FROM plan_tasks WHERE id = :id") suspend fun task(id: String): PlanTaskEntity?
    @Query("SELECT * FROM plan_tasks WHERE planId = :planId ORDER BY scheduledEpochDay, createdAt") fun tasksFor(planId: String): Flow<List<PlanTaskEntity>>
    @Query("SELECT * FROM plan_tasks ORDER BY planId, scheduledEpochDay, createdAt") fun tasks(): Flow<List<PlanTaskEntity>>
    @Query("SELECT * FROM plan_tasks WHERE planId = :planId ORDER BY scheduledEpochDay, createdAt") suspend fun tasksForOnce(planId: String): List<PlanTaskEntity>
    @Query("SELECT * FROM plan_tasks ORDER BY planId, scheduledEpochDay, createdAt") suspend fun tasksOnce(): List<PlanTaskEntity>
    /** Todas as tarefas, de todos os planos: o XP precisa saber o tipo de cada execução. */
    @Query("SELECT id, type FROM plan_tasks") fun taskTypes(): Flow<List<PlanTaskTypeRow>>
    @Query("SELECT * FROM plan_tasks WHERE planId = :planId AND scheduledEpochDay BETWEEN :start AND :end ORDER BY scheduledEpochDay, createdAt") suspend fun tasksInRange(planId: String, start: Long, end: Long): List<PlanTaskEntity>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertDependencies(values: List<PlanTaskDependencyEntity>)
    @Query("SELECT * FROM plan_task_dependencies WHERE taskId IN (SELECT id FROM plan_tasks WHERE planId = :planId)") suspend fun dependenciesFor(planId: String): List<PlanTaskDependencyEntity>
    @Query("SELECT * FROM plan_task_dependencies ORDER BY taskId, dependsOnTaskId") suspend fun dependenciesOnce(): List<PlanTaskDependencyEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertExecution(value: StudyTaskExecutionEntity)
    @Query("SELECT * FROM study_task_executions WHERE planId = :planId ORDER BY completedAt") fun executionsFor(planId: String): Flow<List<StudyTaskExecutionEntity>>
    @Query("SELECT * FROM study_task_executions WHERE planId = :planId ORDER BY completedAt") suspend fun executionsForOnce(planId: String): List<StudyTaskExecutionEntity>
    @Query("SELECT * FROM study_task_executions ORDER BY planId, completedAt") suspend fun executionsOnce(): List<StudyTaskExecutionEntity>
    /** Todas as execuções, de todos os planos: alimenta a sequência de estudos e o mapa de frequência. */
    @Query("SELECT * FROM study_task_executions ORDER BY completedAt") fun executions(): Flow<List<StudyTaskExecutionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAnnualPhaseSubjects(values: List<AnnualPhaseSubjectEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAnnualPhaseTopics(values: List<AnnualPhaseTopicEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertMonthlyPlanSubjects(values: List<MonthlyPlanSubjectEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertMonthlyPlanTopics(values: List<MonthlyPlanTopicEntity>)
    @Query("SELECT * FROM annual_phase_subjects") suspend fun annualPhaseSubjectsOnce(): List<AnnualPhaseSubjectEntity>
    @Query("SELECT * FROM annual_phase_topics") suspend fun annualPhaseTopicsOnce(): List<AnnualPhaseTopicEntity>
    @Query("SELECT * FROM monthly_plan_subjects") suspend fun monthlyPlanSubjectsOnce(): List<MonthlyPlanSubjectEntity>
    @Query("SELECT * FROM monthly_plan_topics") suspend fun monthlyPlanTopicsOnce(): List<MonthlyPlanTopicEntity>
}
