package br.com.estudario.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import br.com.estudario.data.ai.ContractValidationException

class RemoteSyllabusModelsSerializationTest {
    @Test
    fun preservesStableExternalIdsAndNullableSyncAcknowledgmentFields() {
        val syllabus = RemoteSyllabusContractJson.decodePrivateSyllabus(privateSyllabusJson)
        val acknowledgement = RemoteSyllabusContractJson.decodeSyncAcknowledgement(syncAcknowledgementJson)

        assertEquals("subject-direito-constitucional", syllabus.subjects.single().externalId)
        assertEquals("topic-direitos-fundamentais", syllabus.subjects.single().topics.single().externalId)
        assertEquals(null, syllabus.subjects.single().topics.single().parentRemoteTopicId)
        assertEquals(null, acknowledgement.remoteSyllabusId)
        assertEquals(null, acknowledgement.safeError)
    }

    @Test
    fun rejectsRootTopicWithAParentLink() {
        val invalid = privateSyllabusJson.replace("\"parentRemoteTopicId\":null", "\"parentRemoteTopicId\":\"wrong-parent\"")

        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodePrivateSyllabus(invalid)
        }
    }
}

private val privateSyllabusJson = """
    {
      "remoteSyllabusId":"remote-syllabus-1","title":"Edital TRT-3","position":0,"visibility":"PRIVATE",
      "source":"AI_GENERATED","sourceJobId":"job-1","sourceHash":"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
      "schemaVersion":1,"status":"ACTIVE","metadata":{"packageVersion":"estudo-v2"},
      "subjects":[{
        "remoteSubjectId":"remote-subject-1","externalId":"subject-direito-constitucional","name":"Direito Constitucional",
        "position":0,"suggestedPriority":"NORMAL","packageVersion":"estudo-v2","schemaVersion":1,"metadata":{},
        "topics":[{
          "remoteTopicId":"remote-topic-1","externalId":"topic-direitos-fundamentais","parentRemoteTopicId":null,
          "name":"Direitos fundamentais","position":0,"packageVersion":"estudo-v2","schemaVersion":1,"metadata":{},"children":[]
        }]
      }]
    }
""".trimIndent()

private val syncAcknowledgementJson = """
    {
      "remoteSyllabusId":null,"jobId":"job-1","payloadHash":"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
      "state":"PENDING","attemptCount":0,"nextAttemptAt":null,"safeError":null,
      "createdAt":"2026-09-23T12:00:00Z","updatedAt":"2026-09-23T12:00:00Z","attemptToken":null
    }
""".trimIndent()
