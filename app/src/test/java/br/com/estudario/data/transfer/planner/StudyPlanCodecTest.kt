package br.com.estudario.data.transfer.planner

import br.com.estudario.domain.planner.StudyProfile
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
    fun `legacy version one plan defaults the study method`() {
        val decoded = codec.decode(validPlan())

        assertEquals(50, decoded.configuration.blockMinutes)
        assertEquals(StudyProfile.DO_ZERO, decoded.configuration.profile)
    }

    @Test
    fun `study method is preserved in version one round trip`() {
        val plan = codec.decode(validPlan()).copy(
            configuration = codec.decode(validPlan()).configuration.copy(
                blockMinutes = 90,
                profile = StudyProfile.RETA_FINAL,
            ),
        )

        val encoded = codec.encode(plan)
        val roundTrip = codec.decode(encoded)

        assertTrue(encoded.contains("\"blocoMinutos\": 90"))
        assertTrue(encoded.contains("\"perfil\": \"RETA_FINAL\""))
        assertEquals(90, roundTrip.configuration.blockMinutes)
        assertEquals(StudyProfile.RETA_FINAL, roundTrip.configuration.profile)
    }

    @Test
    fun `unknown study profile and out of range blocks are rejected`() {
        val unknownProfile = validPlan().replace(
            "\"discursivasMensais\":2",
            "\"discursivasMensais\":2,\"perfil\":\"DESCONHECIDO\"",
        )
        val tooShort = validPlan().replace(
            "\"discursivasMensais\":2",
            "\"discursivasMensais\":2,\"blocoMinutos\":14",
        )
        val tooLong = validPlan().replace(
            "\"discursivasMensais\":2",
            "\"discursivasMensais\":2,\"blocoMinutos\":181",
        )

        assertTrue(runCatching { codec.decode(unknownProfile) }.exceptionOrNull() is StudyPlanValidationException)
        assertTrue(runCatching { codec.decode(tooShort) }.exceptionOrNull() is StudyPlanValidationException)
        assertTrue(runCatching { codec.decode(tooLong) }.exceptionOrNull() is StudyPlanValidationException)
    }

    @Test
    fun `future version is rejected explicitly`() {
        val error = runCatching {
            codec.decode("""{"format":"estudario-plano","version":2}""")
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
          "format":"estudario-plano",
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
