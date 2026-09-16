package br.com.meuconcurso.data.transfer.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StudyPlanCodecTest {
    private val codec = StudyPlanCodec()

    @Test
    fun `valid version one round trips with stable identifiers`() {
        val decoded = codec.decode(validPlan())
        val roundTrip = codec.decode(codec.encode(decoded))

        assertEquals("11111111-1111-1111-1111-111111111111", roundTrip.planId)
        assertEquals("competition-1", roundTrip.competition.externalId)
        assertEquals(1_320, roundTrip.configuration.days.sumOf { it.minutes })
        assertEquals(decoded, roundTrip)
    }

    @Test
    fun `future version is rejected explicitly`() {
        val error = runCatching {
            codec.decode("""{"format":"meu-concurso-plano","version":2}""")
        }.exceptionOrNull()

        assertEquals("Versão .plano não suportada: 2. Este aplicativo aceita a versão 1.", error?.message)
    }

    @Test
    fun `invalid json and wrong format are never accepted silently`() {
        assertTrue(runCatching { codec.decode("not-json") }.exceptionOrNull() is StudyPlanValidationException)
        assertTrue(runCatching { codec.decode("""{"format":"outro","version":1}""") }.exceptionOrNull() is StudyPlanValidationException)
    }

    @Test
    fun `duplicate task ids are rejected with their path`() {
        val duplicated = validPlan().replace(
            "\"tarefas\":[]",
            "\"tarefas\": [${task("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")},${task("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")} ]",
        )

        val error = runCatching { codec.decode(duplicated) }.exceptionOrNull()

        assertTrue(error?.message.orEmpty().contains("tarefas[1].id"))
    }

    @Test
    fun `cyclic dependencies are rejected`() {
        val first = task("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")
        val second = task("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        val cyclic = validPlan().replace("\"tarefas\":[]", "\"tarefas\":[$first,$second]")

        val error = runCatching { codec.decode(cyclic) }.exceptionOrNull()

        assertTrue(error?.message.orEmpty().contains("ciclo"))
    }

    private fun validPlan() = """
        {
          "format":"meu-concurso-plano",
          "version":1,
          "planId":"11111111-1111-1111-1111-111111111111",
          "concurso":{"externalId":"competition-1","nome":"Concurso"},
          "nome":"Plano principal",
          "objetivo":"Aprovação",
          "active":false,
          "masterPlan":true,
          "dataInicio":"2026-09-15",
          "dataProva":null,
          "configuracao":{
            "modo":"ADVANCED",
            "dias":[
              {"dia":1,"minutos":210,"indisponivel":false},
              {"dia":2,"minutos":210,"indisponivel":false},
              {"dia":3,"minutos":210,"indisponivel":false},
              {"dia":4,"minutos":210,"indisponivel":false},
              {"dia":5,"minutos":120,"indisponivel":false},
              {"dia":6,"minutos":240,"indisponivel":false},
              {"dia":7,"minutos":120,"indisponivel":false}
            ],
            "questoesSemanais":120,
            "discursivasMensais":2
          },
          "prioridades":[],
          "fasesAnuais":[],
          "planosMensais":[],
          "planosSemanais":[],
          "tarefas":[],
          "metadata":{"createdBy":"test"}
        }
    """.trimIndent()

    private fun task(id: String, dependency: String? = null) = """
        {
          "id":"$id",
          "materiaExternalId":"subject-1",
          "topicoExternalId":"topic-1",
          "data":"2026-09-15",
          "tipo":"THEORY",
          "minutos":60,
          "questoes":0,
          "prioridade":"CRITICAL",
          "status":"PLANEJADA",
          "origem":"IMPORTED",
          "locked":false,
          "dependencias":[${dependency?.let { "\"$it\"" }.orEmpty()}]
        }
    """.trimIndent()
}
