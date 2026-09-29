package br.com.estudario.data.ai

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.prompt.PlanPromptOptions
import br.com.estudario.data.prompt.PlanSubjectInfo
import br.com.estudario.data.prompt.PlanTopicInfo
import br.com.estudario.data.transfer.EstudoPackageParser
import br.com.estudario.data.transfer.allTopics
import br.com.estudario.data.transfer.planner.StudyPlanCodec
import br.com.estudario.domain.planner.PlanOrigin
import br.com.estudario.domain.planner.PlanPriority
import java.time.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiTextJobMappersTest {
    private val competition = CompetitionEntity(id = 1, name = "TRT 3")
    private val subject = SubjectEntity(id = 10, competitionId = 1, name = "Noções de Direito")
    private val parent = TopicEntity(id = 100, subjectId = 10, title = "Direito Constitucional")
    private val topic = TopicEntity(id = 101, subjectId = 10, parentTopicId = 100, title = "Dos princípios fundamentais", position = 1)

    private val proposal = Json.parseToJsonElement(
        """
        {
          "schemaVersion": 1, "promptVersion": "topic-content-v1", "modelVersion": "m",
          "scope": {"covers": "Arts. 1º a 4º", "excludes": "Direitos fundamentais"},
          "theoryTitle": "Princípios fundamentais",
          "chapters": [{"title": "1. Fundamentos", "markdown": "Texto 1"}, {"title": "2. Aplicação", "markdown": "Texto 2"}],
          "summary": "# Resumo", "quickReview": "# Revisão", "tips": ["Bizu"], "traps": ["Pegadinha"], "activeRecall": ["Pergunta?"],
          "errorConcepts": [{"key": "e1", "title": "Fundamento x objetivo", "summary": "Explicação"}],
          "questions": [
            {"statement": "Enunciado", "format": "MULTIPLE_CHOICE", "difficulty": "MEDIA",
             "options": [{"key":"A","text":"a","correct":false},{"key":"B","text":"b","correct":true},{"key":"C","text":"c","correct":false},{"key":"D","text":"d","correct":false},{"key":"E","text":"e","correct":false}],
             "explanation": "Porque sim (CF, art. 1º).", "section": "1. Fundamentos", "errorConceptKey": "e1",
             "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null}
          ],
          "sources": [{"kind": "OFICIAL", "title": "Constituição", "publisher": "Planalto", "reference": "Art. 1º", "url": "https://www.planalto.gov.br", "accessedAt": "2026-09-28"}],
          "warnings": []
        }
        """.trimIndent(),
    ).jsonObject

    @Test fun `topic content becomes an estudo package the official parser accepts in the right topic`() {
        val estudo = TopicContentEstudoMapper.toEstudo(competition, subject, listOf(parent, topic), topic, proposal)
        val plan = EstudoPackageParser.parse(estudo)
        val target = plan.allTopics().single { it.externalId == "topico-101" }
        assertEquals(1, target.theories.size)
        assertEquals(1, target.questions.size)
        assertEquals(1, target.errorConcepts.size)
        assertEquals(1, target.sources.size)
        // O pai entra só como estrutura, sem conteúdo novo.
        val ancestor = plan.allTopics().single { it.externalId == "topico-100" }
        assertTrue(ancestor.theories.isEmpty() && ancestor.questions.isEmpty())
    }

    @Test fun `plan context maps compact AI tasks back to real ids in a valid plano`() {
        val subjects = listOf(
            PlanSubjectInfo("materia-10", "Português", listOf(PlanTopicInfo("topico-1", "Interpretação", false)), 0, null),
            PlanSubjectInfo("materia-11", "Direito", listOf(PlanTopicInfo("topico-2", "Princípios", true)), 0, null),
        )
        val options = PlanPromptOptions(
            startDate = LocalDate.of(2026, 9, 28),
            dayMinutes = listOf(120, 120, 120, 120, 120, 60, 0),
            priorities = mapOf("materia-10" to PlanPriority.HIGH),
        )
        val prepared = StudyPlanAi.prepare("concurso-1", "TRT 3", subjects, options)
        val sent = prepared.input["subjects"]!!.jsonArray
        assertEquals("s1", sent[0].jsonObject["ref"]!!.jsonPrimitive.content)
        assertEquals("t2", sent[1].jsonObject["topics"]!!.jsonArray[0].jsonObject["ref"]!!.jsonPrimitive.content)

        val aiPlan = Json.parseToJsonElement(
            """
            {"schemaVersion":1,"promptVersion":"study-plan-v1","modelVersion":"m","summary":"Estratégia.",
             "phases":[{"name":"Base","objective":"Fundamentos","fromDay":0,"toDay":6}],
             "tasks":[{"d":0,"s":"s1","t":"t1","k":"THEORY","m":50,"q":0},
                      {"d":1,"s":"s2","t":"t2","k":"QUESTIONS","m":50,"q":15},
                      {"d":5,"s":null,"t":null,"k":"SIMULATION","m":60,"q":40}],
             "warnings":[]}
            """.trimIndent(),
        ).jsonObject
        val plano = StudyPlanAi.toPlano(prepared.context, aiPlan)
        val decoded = StudyPlanCodec().decode(plano)
        assertEquals(3, decoded.tasks.size)
        assertEquals("topico-1", decoded.tasks.first { it.date == LocalDate.of(2026, 9, 28) }.topicExternalId)
        assertEquals("materia-11", decoded.tasks.first { it.date == LocalDate.of(2026, 9, 29) }.subjectExternalId)
        assertTrue(decoded.tasks.all { it.origin == PlanOrigin.IMPORTED })
        assertEquals(PlanPriority.HIGH, decoded.subjects.first { it.externalId == "materia-10" }.priority)
        assertEquals("Estratégia.", decoded.metadata["premissas"])
    }
}
