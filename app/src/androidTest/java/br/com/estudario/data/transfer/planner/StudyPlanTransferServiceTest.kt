package br.com.estudario.data.transfer.planner

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.data.local.planner.StudyTaskExecutionEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudyPlanTransferServiceTest {
    private lateinit var db: AppDatabase
    private lateinit var service: StudyPlanTransferService

    @Before fun setup() = runBlocking {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java).allowMainThreadQueries().build()
        val competition = db.dao().insertCompetition(CompetitionEntity(name = "Concurso", externalId = "competition-1"))
        val subject = db.dao().insertSubject(SubjectEntity(competitionId = competition, name = "TI", externalId = "subject-1"))
        db.dao().insertTopic(TopicEntity(subjectId = subject, title = "Segurança", externalId = "topic-1"))
        service = StudyPlanTransferService(db)
    }

    @After fun close() = db.close()

    @Test fun createResolvesStableEditalIdsAndNeverImportsExecution() = runBlocking {
        val result = service.import(planJson(), PlanImportMode.CREATE)
        val task = db.plannerDao().tasksForOnce(result.planId).single()

        assertTrue(task.subjectId != null)
        assertTrue(task.topicId != null)
        assertEquals(PlanTaskStatus.PLANEJADA, task.status)
        assertTrue(db.plannerDao().executionsForOnce(result.planId).isEmpty())
    }

    @Test fun importedStudyMethodIsSavedForFutureReplanning() = runBlocking {
        val result = service.import(planJson(blockMinutes = 90, profile = "RETA_FINAL"), PlanImportMode.CREATE)
        val plan = db.plannerDao().plan(result.planId)!!

        assertEquals(90, plan.blockMinutes)
        assertEquals("RETA_FINAL", plan.profile)
        assertEquals(90, plan.methodConfig().blockMinutes)
        assertEquals(StudyProfile.RETA_FINAL, plan.methodConfig().profile)
    }

    @Test fun mergePreservesCompletedLocalTask() = runBlocking {
        val result = service.import(planJson(), PlanImportMode.CREATE)
        val task = db.plannerDao().tasksForOnce(result.planId).single()
        db.plannerDao().updateTask(task.copy(status = PlanTaskStatus.CONCLUIDA))

        service.import(planJson(minutes = 120, planId = result.planId), PlanImportMode.MERGE)

        val preserved = db.plannerDao().task(task.id)!!
        assertEquals(PlanTaskStatus.CONCLUIDA, preserved.status)
        assertEquals(60, preserved.plannedMinutes)
    }

    @Test fun mergeKeepsPartialExecutionAndCreatesOnlyRemainingDemand() = runBlocking {
        val result = service.import(planJson(), PlanImportMode.CREATE)
        val task = db.plannerDao().tasksForOnce(result.planId).single()
        db.plannerDao().updateTask(task.copy(status = PlanTaskStatus.EM_ANDAMENTO))
        db.plannerDao().insertExecution(StudyTaskExecutionEntity("execution", result.planId, task.id, task.competitionId, task.subjectId, task.topicId, 1, 2, 25))

        service.import(planJson(planId = result.planId), PlanImportMode.MERGE)

        assertEquals(25, db.plannerDao().executionsForOnce(result.planId).single().actualMinutes)
        assertEquals(PlanTaskStatus.REPROGRAMADA, db.plannerDao().task(task.id)?.status)
        assertEquals(35, db.plannerDao().tasksForOnce(result.planId).single { it.replannedFromTaskId == task.id }.plannedMinutes)
    }

    private fun planJson(minutes: Int = 60, planId: String = "11111111-1111-1111-1111-111111111111", blockMinutes: Int? = null, profile: String? = null) = """
        {"format":"estudario-plano","version":1,"planId":"$planId",
        "concurso":{"externalId":"competition-1","nome":"Concurso"},"nome":"Plano","objetivo":"Aprovação",
        "active":false,"masterPlan":false,"dataInicio":"2026-09-15","dataProva":null,
        "configuracao":{"modo":"ADVANCED","dias":[{"dia":1,"minutos":120,"indisponivel":false}],"questoesSemanais":10,"discursivasMensais":0${blockMinutes?.let { ",\"blocoMinutos\":$it" }.orEmpty()}${profile?.let { ",\"perfil\":\"$it\"" }.orEmpty()}},
        "prioridades":[{"externalId":"subject-1","nome":"TI","prioridade":"CRITICAL","manutencaoMinutos":30,"pausada":false,"posicao":0}],
        "fasesAnuais":[],"planosMensais":[],"planosSemanais":[],
        "tarefas":[{"id":"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa","materiaExternalId":"subject-1","topicoExternalId":"topic-1","data":"2026-09-16","tipo":"THEORY","minutos":$minutes,"questoes":0,"prioridade":"CRITICAL","status":"PLANEJADA","origem":"IMPORTED","locked":false,"dependencias":[]}],"metadata":{}}
    """.trimIndent()
}
