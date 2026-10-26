package br.com.estudario.web.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.time.plusDays

class ForecastWorkerTest {
    private fun data() = Snapshot.parse("""{
      "topics":[{"id":1,"subjectId":1,"title":"Tópico de teste"}],
      "questions":[{"id":10,"topicId":1,"statement":"Texto de teste", "explanation":"Texto longo dispensável"}],
      "attempts":[{"id":20,"questionId":10,"selectedKey":"A","correct":true,"answeredAt":0}],
      "notes":[{"id":1,"text":"Anotação dispensável"}],
      "theories":[{"id":1,"topicId":1,"markdown":"Texto dispensável"}]
    }""")

    private fun key(data: Snapshot, day: String = "2026-10-25", zone: String = "America/Sao_Paulo",
        free: Boolean = false, excluded: Set<Long> = emptySet(), plan: String = "p") =
        ForecastKey(data, plan, day, zone, free, excluded)

    @Test fun unrelatedEditsReuseTheSameForecast() {
        val original = data()
        val changed = original.withObject("folhaLook", kotlinx.serialization.json.JsonObject(mapOf("hat" to JsonPrimitive("novo"))))
        assertTrue(key(original).matches(key(changed)))
    }

    @Test fun studyEditsInvalidateTheForecast() {
        val original = data()
        val changed = original.edit(Keys.TOPICS) { it.with("status" to JsonPrimitive("ESTUDADO")) }
        assertFalse(key(original).matches(key(changed)))
    }

    @Test fun dayZonePlanAndFreeRulesInvalidateTheForecast() {
        val data = data()
        val original = key(data)
        assertFalse(original.matches(key(data, day = "2026-10-26")))
        assertFalse(original.matches(key(data, zone = "UTC")))
        assertFalse(original.matches(key(data, plan = "outro")))
        assertFalse(original.matches(key(data, free = true)))
        assertFalse(original.matches(key(data, excluded = setOf(1))))
    }

    @Test fun workerPayloadPreservesThePlannerQuestionAndAnswerInputs() {
        val request = key(data(), free = true, excluded = setOf(7)).request()
        val decoded = Json.decodeFromString<ForecastRequest>(Json.encodeToString(request))
        val snapshot = Snapshot.parse(decoded.snapshot)
        assertEquals(listOf(10L), snapshot.questions.map { it.id })
        assertEquals(listOf(1L), snapshot.questions.map { it.topicId })
        assertEquals(listOf(true), snapshot.attempts.map { it.correct })
        assertEquals(listOf(10L), snapshot.attempts.map { it.questionId })
        assertTrue(decoded.freeRules)
        assertEquals(setOf(7L), decoded.excludedTopicIds)
        assertFalse("Texto longo dispensável" in decoded.snapshot)
        assertFalse("Anotação dispensável" in decoded.snapshot)
        assertFalse("Texto dispensável" in decoded.snapshot)
    }

    @Test fun workerPayloadProducesTheSameProjectionAsTheFullSnapshot() {
        val (catalog, competition) = Catalog.apply(Snapshot.empty(),
            CatalogEntry(id = "perf", shortName = "Teste", agency = "Teste", role = "Teste"),
            listOf(CatalogSubject("Matéria de teste", listOf("Tópico um", "Tópico dois", "Tópico três"))))
        val (planned, planId) = Planning.create(catalog, Planning.NewPlan(
            competitionId = competition, name = "Teste", objective = "Teste", startDate = Queries.todayDate(),
            examDate = Queries.todayDate().plusDays(90), weeklyMinutes = listOf(120, 120, 120, 120, 120, 180, 0),
            subjects = catalog.subjects.map { Planning.SubjectChoice(it.id, it.name) },
            method = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO)))
        val plan = planned.plans.single { it.id == planId }
        val expected = Forecast.compute(planned, plan, freeRules = false, excludedTopicIds = emptySet())
        val request = key(planned, plan = planId).request()
        val transferred = Snapshot.parse(request.snapshot)
        val actual = Forecast.compute(transferred, transferred.plans.single(), request.freeRules, request.excludedTopicIds)
        assertTrue(expected != null)
        assertEquals(expected, actual)
        assertEquals(expected, Json.decodeFromString<Forecast.Result>(Json.encodeToString(expected)))
    }
}
