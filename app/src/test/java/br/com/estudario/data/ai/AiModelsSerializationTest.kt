package br.com.estudario.data.ai

import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class AiModelsSerializationTest {
    @Test
    fun decodesNestedProposalAndTypedWarning() {
        val proposal = EstudarioContractJson.decodeProposal(validProposalJson)

        assertEquals("Remédios constitucionais", proposal.subjects.single().topics.single().children.single().name)
        assertEquals(listOf(44), proposal.warnings.single().sourcePages)
        assertEquals("A seção pode pertencer a duas matérias.", proposal.warnings.single().ambiguity)
    }

    @Test
    fun rejectsUnsupportedSchemaVersion() {
        val json = validProposalJson.replace("\"schemaVersion\": 1", "\"schemaVersion\": 2")

        assertThrows(ContractValidationException::class.java) {
            EstudarioContractJson.decodeProposal(json)
        }
    }

    @Test
    fun rejectsUnknownFieldsAndDefinitiveLocalIds() {
        val unknown = validProposalJson.replace(
            "\"ambiguities\": [\"A seção pode pertencer a duas matérias.\"]",
            "\"ambiguities\": [\"A seção pode pertencer a duas matérias.\"],\"unexpected\":true",
        )
        val localId = validProposalJson.replace(
            "\"name\": \"Direito Constitucional\"",
            "\"name\": \"Direito Constitucional\",\"id\":42",
        )

        assertThrows(SerializationException::class.java) { EstudarioContractJson.decodeProposal(unknown) }
        assertThrows(SerializationException::class.java) { EstudarioContractJson.decodeProposal(localId) }
    }

    @Test
    fun rejectsMissingRequiredFields() {
        val missingTitle = validProposalJson.replace("\"documentTitle\": \"Edital TRT-3\",", "")

        assertThrows(SerializationException::class.java) { EstudarioContractJson.decodeProposal(missingTitle) }
    }

    @Test
    fun rejectsMalformedTopicAndWarningStructures() {
        val invalidTopic = validProposalJson.replaceFirst("\"position\": 0,", "\"position\": -1,")
        val invalidWarning = validProposalJson.replace("\"sourcePages\": [44]", "\"sourcePages\": [0]")

        assertThrows(ContractValidationException::class.java) { EstudarioContractJson.decodeProposal(invalidTopic) }
        assertThrows(ContractValidationException::class.java) { EstudarioContractJson.decodeProposal(invalidWarning) }
    }

    @Test
    fun preservesNullableJobAndAccessFields() {
        val job = EstudarioContractJson.decodeJob(reservedJobJson)
        val access = EstudarioContractJson.decodeAccess(accessJson)
        val quotaAccess = EstudarioContractJson.decodeAccess(accessWithQuotaJson)

        assertEquals(null, job.proposal)
        assertEquals(null, access.quota)
        assertEquals(1, quotaAccess.quota?.reservedCount)
        assertEquals(null, job.providerExecutionStartedAt)
    }
}

private val validProposalJson = """
    {
      "schemaVersion": 1,
      "promptVersion": "syllabus-v1",
      "modelVersion": "gpt-6-luna",
      "documentTitle": "Edital TRT-3",
      "subjects": [{
        "name": "Direito Constitucional",
        "position": 0,
        "suggestedPriority": "NORMAL",
        "topics": [{
          "name": "Direitos fundamentais",
          "position": 0,
          "children": [{"name":"Remédios constitucionais","position":0,"children":[],"sourcePages":[43]}],
          "sourcePages": [42, 43]
        }],
        "sourcePages": [42, 43]
      }],
      "warnings": [{
        "code": "AMBIGUOUS_STRUCTURE",
        "severity": "WARNING",
        "message": "O texto não deixa claro se este item é uma matéria ou um tópico.",
        "sourcePages": [44],
        "ambiguity": "A seção pode pertencer a duas matérias."
      }],
      "ambiguities": ["A seção pode pertencer a duas matérias."]
    }
""".trimIndent()

private val reservedJobJson = """
    {
      "jobId":"job-1","feature":"SYLLABUS_GENERATION","status":"RESERVED",
      "schemaVersion":null,"promptVersion":null,"modelVersion":null,"proposal":null,"warnings":[],
      "errorCode":null,"errorMessage":null,"createdAt":"2026-09-23T12:00:00Z","updatedAt":"2026-09-23T12:00:00Z",
      "finishedAt":null,"providerExecutionStartedAt":null
    }
""".trimIndent()

private val accessJson = """
    {
      "authenticated":true,"betaAccess":true,"feature":"SYLLABUS_GENERATION","featureEnabled":true,
      "quota":null,"canUse":false,"reasonCode":"QUOTA_EXHAUSTED"
    }
""".trimIndent()

private val accessWithQuotaJson = """
    {
      "authenticated":true,"betaAccess":true,"feature":"SYLLABUS_GENERATION","featureEnabled":true,
      "quota":{"feature":"SYLLABUS_GENERATION","limit":1,"successfulCount":0,"reservedCount":1,"remaining":0,"periodStart":"2026-09-23"},
      "canUse":false,"reasonCode":"QUOTA_RESERVED"
    }
""".trimIndent()
