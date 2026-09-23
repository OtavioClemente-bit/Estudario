package br.com.estudario.data.transfer

import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.planner.*
import br.com.estudario.domain.planner.*
import org.json.JSONArray
import org.json.JSONObject

internal object PlannerBackupCodec {
    suspend fun write(db: AppDatabase, root: JSONObject) {
        val d = db.plannerDao()
        root.put("studyPlans", rows(d.plansOnce()) { p -> j("id",p.id,"competitionId",p.competitionId,"name",p.name,"objective",p.objective,"start",p.startEpochDay,"exam",p.examEpochDay,"active",p.active,"master",p.masterPlan,"archived",p.archived,"revision",p.revision,"createdAt",p.createdAt,"updatedAt",p.updatedAt,"profile",p.profile,"block",p.blockMinutes,"weeklyQuestions",p.weeklyQuestionsTarget,"topicQuestions",p.questionsPerTopic,"simulations",p.simulationsPerMonth,"discursives",p.discursivesPerMonth,"interleave",p.interleaveSubjects,"dailyShare",p.dailySubjectSharePercent) })
        root.put("studyPlanRevisions", rows(d.revisionsOnce()) { r -> j("planId",r.planId,"revision",r.revision,"base",r.baseRevision,"reason",r.reason,"proposalId",r.proposalId,"summary",r.summary,"createdAt",r.createdAt) })
        root.put("studyAvailability", rows(d.availabilityOnce()) { a -> j("planId",a.planId,"day",a.dayOfWeek,"minutes",a.availableMinutes,"unavailable",a.unavailable,"mode",a.mode.name) })
        root.put("studyDayOverrides", rows(d.dayOverridesOnce()) { a -> j("planId",a.planId,"day",a.epochDay,"minutes",a.availableMinutes,"unavailable",a.unavailable,"locked",a.locked) })
        // Os três eixos viajam no backup. Sem "personalDifficulty" e "initialKnowledge", restaurar
        // um backup devolveria o plano com todas as matérias em normal/nunca estudei, ou seja,
        // apagaria em silêncio o que a pessoa respondeu no assistente.
        root.put("planSubjects", rows(d.subjectsOnce()) { s -> j("planId",s.planId,"subjectId",s.subjectId,"name",s.subjectNameSnapshot,"priority",s.priority.name,"paused",s.paused,"maintenance",s.minimumMaintenanceMinutes,"weight",s.weightOverride,"position",s.position,"personalDifficulty",s.personalDifficulty.name,"initialKnowledge",s.initialKnowledge.name) })
        root.put("annualPhases", rows(d.annualPhasesOnce()) { p -> j("id",p.id,"planId",p.planId,"position",p.position,"name",p.name,"objective",p.objective,"criteria",p.completionCriteria,"start",p.startEpochDay,"end",p.endEpochDay,"minutes",p.targetMinutes,"questions",p.targetQuestions,"discursives",p.targetDiscursives,"percent",p.targetPercent,"fromRevision",p.validFromRevision,"untilRevision",p.validUntilRevision) })
        root.put("monthlyPlans", rows(d.monthlyPlansOnce()) { p -> j("id",p.id,"planId",p.planId,"yearMonth",p.yearMonth,"focus",p.focus,"minutes",p.targetMinutes,"questions",p.targetQuestions,"discursives",p.targetDiscursives,"percent",p.targetPercent,"fromRevision",p.validFromRevision,"untilRevision",p.validUntilRevision) })
        root.put("weeklyPlans", rows(d.weeklyPlansOnce()) { p -> j("id",p.id,"planId",p.planId,"weekStart",p.weekStartEpochDay,"objective",p.objective,"minutes",p.targetMinutes,"questions",p.targetQuestions,"discursives",p.targetDiscursives,"fromRevision",p.validFromRevision,"untilRevision",p.validUntilRevision) })
        root.put("annualPhaseSubjects", rows(d.annualPhaseSubjectsOnce()) { j("phaseId",it.phaseId,"subjectId",it.subjectId) })
        root.put("annualPhaseTopics", rows(d.annualPhaseTopicsOnce()) { j("phaseId",it.phaseId,"topicId",it.topicId) })
        root.put("monthlyPlanSubjects", rows(d.monthlyPlanSubjectsOnce()) { j("monthlyPlanId",it.monthlyPlanId,"subjectId",it.subjectId,"maintenance",it.maintenance) })
        root.put("monthlyPlanTopics", rows(d.monthlyPlanTopicsOnce()) { j("monthlyPlanId",it.monthlyPlanId,"topicId",it.topicId) })
        root.put("planTasks", rows(d.tasksOnce()) { t -> j("id",t.id,"planId",t.planId,"competitionId",t.competitionId,"annualPhaseId",t.annualPhaseId,"monthlyPlanId",t.monthlyPlanId,"weeklyPlanId",t.weeklyPlanId,"subjectId",t.subjectId,"topicId",t.topicId,"subjectName",t.subjectNameSnapshot,"topicName",t.topicNameSnapshot,"day",t.scheduledEpochDay,"type",t.type.name,"minutes",t.plannedMinutes,"questions",t.plannedQuestions,"priority",t.priority.name,"status",t.status.name,"origin",t.origin.name,"notes",t.notes,"locked",t.locked,"progressNote",t.progressNote,"replannedFrom",t.replannedFromTaskId,"createdRevision",t.createdRevision,"updatedRevision",t.updatedRevision,"createdAt",t.createdAt,"updatedAt",t.updatedAt) })
        root.put("planTaskDependencies", rows(d.dependenciesOnce()) { j("taskId",it.taskId,"dependsOn",it.dependsOnTaskId) })
        root.put("studyTaskExecutions", rows(d.executionsOnce()) { e -> j("id",e.id,"planId",e.planId,"taskId",e.taskId,"competitionId",e.competitionId,"subjectId",e.subjectId,"topicId",e.topicId,"startedAt",e.startedAt,"completedAt",e.completedAt,"minutes",e.actualMinutes,"questions",e.questionsDone,"correct",e.correctAnswers,"notes",e.notes,"difficulty",e.perceivedDifficulty.name,"createdAt",e.createdAt) })
    }

    suspend fun restore(db: AppDatabase, root: JSONObject) {
        val d = db.plannerDao()
        root.a("studyPlans").forEach { x -> d.insertPlan(StudyPlanEntity(x.s("id"),x.l("competitionId"),x.s("name"),x.s("objective"),x.l("start"),x.nl("exam"),x.b("active"),x.b("master"),x.b("archived"),x.l("revision"),x.l("createdAt"),x.l("updatedAt"),x.ns("profile") ?: "DO_ZERO",x.ni("block") ?: 50,x.ni("weeklyQuestions") ?: 100,x.ni("topicQuestions") ?: 15,x.ni("simulations") ?: 2,x.ni("discursives") ?: 0,x.optBoolean("interleave", true),x.ni("dailyShare") ?: 60)) }
        root.a("studyPlanRevisions").forEach { x -> d.insertRevision(StudyPlanRevisionEntity(x.s("planId"),x.l("revision"),x.l("base"),x.s("reason"),x.ns("proposalId"),x.s("summary"),x.l("createdAt"))) }
        d.upsertAvailability(root.a("studyAvailability").map { x -> StudyAvailabilityEntity(x.s("planId"),x.i("day"),x.i("minutes"),x.b("unavailable"),AvailabilityMode.valueOf(x.s("mode"))) })
        root.a("studyDayOverrides").forEach { x -> d.upsertDayOverride(StudyDayOverrideEntity(x.s("planId"),x.l("day"),x.ni("minutes"),x.b("unavailable"),x.b("locked"))) }
        // Backup antigo não tem os dois eixos pessoais: a ausência cai no padrão neutro em vez de
        // explodir, que é o mesmo que um plano criado antes do Smart Planner já faz.
        d.upsertPlanSubjects(root.a("planSubjects").map { x -> PlanSubjectEntity(x.s("planId"),x.l("subjectId"),x.s("name"),PlanPriority.valueOf(x.s("priority")),x.b("paused"),x.i("maintenance"),x.ni("weight"),x.i("position"),x.personalDifficulty("personalDifficulty"),x.initialKnowledge("initialKnowledge")) })
        d.insertAnnualPhases(root.a("annualPhases").map { x -> AnnualPhaseEntity(x.s("id"),x.s("planId"),x.i("position"),x.s("name"),x.s("objective"),x.s("criteria"),x.l("start"),x.l("end"),x.i("minutes"),x.i("questions"),x.i("discursives"),x.i("percent"),x.l("fromRevision"),x.nl("untilRevision")) })
        d.insertMonthlyPlans(root.a("monthlyPlans").map { x -> MonthlyPlanEntity(x.s("id"),x.s("planId"),x.s("yearMonth"),x.s("focus"),x.i("minutes"),x.i("questions"),x.i("discursives"),x.i("percent"),x.l("fromRevision"),x.nl("untilRevision")) })
        d.insertWeeklyPlans(root.a("weeklyPlans").map { x -> WeeklyPlanEntity(x.s("id"),x.s("planId"),x.l("weekStart"),x.s("objective"),x.i("minutes"),x.i("questions"),x.i("discursives"),x.l("fromRevision"),x.nl("untilRevision")) })
        d.insertAnnualPhaseSubjects(root.a("annualPhaseSubjects").map { AnnualPhaseSubjectEntity(it.s("phaseId"),it.l("subjectId")) })
        d.insertAnnualPhaseTopics(root.a("annualPhaseTopics").map { AnnualPhaseTopicEntity(it.s("phaseId"),it.l("topicId")) })
        d.insertMonthlyPlanSubjects(root.a("monthlyPlanSubjects").map { MonthlyPlanSubjectEntity(it.s("monthlyPlanId"),it.l("subjectId"),it.b("maintenance")) })
        d.insertMonthlyPlanTopics(root.a("monthlyPlanTopics").map { MonthlyPlanTopicEntity(it.s("monthlyPlanId"),it.l("topicId")) })
        d.insertTasks(root.a("planTasks").map { x -> PlanTaskEntity(x.s("id"),x.s("planId"),x.l("competitionId"),x.ns("annualPhaseId"),x.ns("monthlyPlanId"),x.ns("weeklyPlanId"),x.nl("subjectId"),x.nl("topicId"),x.s("subjectName"),x.ns("topicName"),x.l("day"),PlanTaskType.valueOf(x.s("type")),x.i("minutes"),x.i("questions"),PlanPriority.valueOf(x.s("priority")),PlanTaskStatus.valueOf(x.s("status")),PlanOrigin.valueOf(x.s("origin")),x.s("notes"),x.b("locked"),x.s("progressNote"),x.ns("replannedFrom"),x.l("createdRevision"),x.l("updatedRevision"),x.l("createdAt"),x.l("updatedAt")) })
        d.insertDependencies(root.a("planTaskDependencies").map { PlanTaskDependencyEntity(it.s("taskId"),it.s("dependsOn")) })
        root.a("studyTaskExecutions").forEach { x -> d.insertExecution(StudyTaskExecutionEntity(x.s("id"),x.s("planId"),x.ns("taskId"),x.l("competitionId"),x.nl("subjectId"),x.nl("topicId"),x.l("startedAt"),x.l("completedAt"),x.i("minutes"),x.i("questions"),x.i("correct"),x.s("notes"),PerceivedDifficulty.valueOf(x.s("difficulty")),x.l("createdAt"))) }
    }

    private fun <T> rows(values: List<T>, transform: (T) -> JSONObject) = JSONArray(values.map(transform))
    private fun j(vararg values: Any?): JSONObject = JSONObject().also { o -> values.asList().chunked(2).forEach { (k,v) -> o.put(k as String, v ?: JSONObject.NULL) } }
    private fun JSONObject.a(key: String) = (optJSONArray(key) ?: JSONArray()).let { a -> (0 until a.length()).map { a.getJSONObject(it) } }
    private fun JSONObject.s(k:String)=getString(k); private fun JSONObject.ns(k:String)=if(!has(k)||isNull(k)) null else getString(k)
    private fun JSONObject.i(k:String)=getInt(k); private fun JSONObject.ni(k:String)=if(!has(k)||isNull(k)) null else getInt(k)
    private fun JSONObject.l(k:String)=getLong(k); private fun JSONObject.nl(k:String)=if(!has(k)||isNull(k)) null else getLong(k)
    private fun JSONObject.b(k:String)=getBoolean(k)

    // Enum ausente ou irreconhecível cai no padrão: um backup antigo, ou corrompido, nunca deve
    // derrubar a restauração inteira por causa de um campo novo.
    private fun JSONObject.personalDifficulty(k: String): PersonalDifficulty =
        ns(k)?.let { raw -> runCatching { PersonalDifficulty.valueOf(raw) }.getOrNull() } ?: PersonalDifficulty.NORMAL
    private fun JSONObject.initialKnowledge(k: String): InitialKnowledge =
        ns(k)?.let { raw -> runCatching { InitialKnowledge.valueOf(raw) }.getOrNull() } ?: InitialKnowledge.NONE
}
