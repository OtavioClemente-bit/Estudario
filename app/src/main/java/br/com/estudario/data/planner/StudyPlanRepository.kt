package br.com.estudario.data.planner

import br.com.estudario.data.local.AppDatabase

class StudyPlanRepository(private val db: AppDatabase) {
    private val dao = db.plannerDao()

    val plans = dao.plans()
    fun activePlan(competitionId: Long) = dao.activePlan(competitionId)
    fun tasks(planId: String) = dao.tasksFor(planId)
    fun executions(planId: String) = dao.executionsFor(planId)

    suspend fun plan(planId: String) = dao.plan(planId)
    suspend fun task(taskId: String) = dao.task(taskId)
    suspend fun plansOnce() = dao.plansOnce()
    suspend fun tasksOnce(planId: String) = dao.tasksForOnce(planId)
    suspend fun executionsOnce(planId: String) = dao.executionsForOnce(planId)
    suspend fun availabilityOnce(planId: String) = dao.availabilityFor(planId)
    suspend fun subjectsOnce(planId: String) = dao.subjectsFor(planId)
    suspend fun annualOnce(planId: String) = dao.currentAnnualPhases(planId)
    suspend fun monthlyOnce(planId: String) = dao.currentMonthlyPlans(planId)
    suspend fun weeklyOnce(planId: String) = dao.currentWeeklyPlans(planId)
    suspend fun dayOverridesOnce(planId: String) = dao.dayOverridesFor(planId)
    suspend fun allExecutionsOnce() = dao.executionsOnce()
    suspend fun latestRevisionOnce(planId: String) = dao.latestRevision(planId)
    suspend fun executionCountOnce(planId: String) = dao.executionCountFor(planId)
}
