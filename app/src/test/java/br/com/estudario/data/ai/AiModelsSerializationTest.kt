package br.com.estudario.data.ai

import java.io.File
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

    @Test
    fun decodesNewQuotaUsageAndResetWhileAcceptingLegacyMissingReset() {
        val updated = accessWithQuotaJson.replace(
            "\"reservedCount\": 1,",
            "\"reservedCount\": 1, \"used\": 1, \"resetAt\": \"2026-09-24T03:00:00Z\",",
        )
        val quota = EstudarioContractJson.decodeAccess(updated).quota!!
        assertEquals(1, quota.used)
        assertEquals("2026-09-24T03:00:00Z", quota.resetAt)
        assertEquals(null, EstudarioContractJson.decodeAccess(accessWithQuotaJson).quota?.resetAt)
    }

    @Test
    fun rejectsQuotaUsageThatDisagreesWithCounts() {
        val invalid = accessWithQuotaJson.replace(
            "\"reservedCount\": 1,",
            "\"reservedCount\": 1, \"used\": 0,",
        )
        assertThrows(ContractValidationException::class.java) { EstudarioContractJson.decodeAccess(invalid) }
    }

    @Test
    fun rejectsBlankJobIdsAndMalformedJobTimestamps() {
        val blankId = reservedJobJson.replace("\"jobId\": \"job-1\"", "\"jobId\": \" \"")
        val malformedTimestamp = reservedJobJson.replace("2026-09-23T12:00:00Z", "not-a-timestamp")

        assertThrows(ContractValidationException::class.java) {
            EstudarioContractJson.decodeJob(blankId)
        }
        assertThrows(ContractValidationException::class.java) {
            EstudarioContractJson.decodeJob(malformedTimestamp)
        }
    }

    @Test
    fun rejectsBlankAccessReasonCodes() {
        val invalid = accessJson.replace("\"reasonCode\": \"QUOTA_EXHAUSTED\"", "\"reasonCode\": \" \"")

        assertThrows(ContractValidationException::class.java) {
            EstudarioContractJson.decodeAccess(invalid)
        }
    }
}

private val validProposalJson: String
    get() = fixture("ai-syllabus-proposal.json")

private val reservedJobJson: String
    get() = fixture("job-reserved.json")

private val accessJson: String
    get() = fixture("access-quota-null.json")

private val accessWithQuotaJson: String
    get() = fixture("access-quota-reserved.json")

private fun fixture(name: String): String {
    val file = listOf(
        File("supabase/functions/_shared/fixtures/v1/$name"),
        File("../supabase/functions/_shared/fixtures/v1/$name"),
        File("../../supabase/functions/_shared/fixtures/v1/$name"),
    ).firstOrNull { it.isFile }
    check(file != null) { "Missing versioned fixture: $name" }
    return file.readText()
}
