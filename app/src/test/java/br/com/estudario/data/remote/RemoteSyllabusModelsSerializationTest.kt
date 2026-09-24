package br.com.estudario.data.remote

import br.com.estudario.data.ai.ContractValidationException
import java.io.File
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RemoteSyllabusModelsSerializationTest {
    @Test
    fun decodesVersionedFixtureWithNestedParentLinksAndStableIds() {
        val syllabus = RemoteSyllabusContractJson.decodePrivateSyllabus(privateSyllabusJson)
        val acknowledgement = RemoteSyllabusContractJson.decodeSyncAcknowledgement(syncAcknowledgementJson)

        assertEquals("subject-direito-constitucional", syllabus.subjects.single().externalId)
        assertEquals("topic-direitos-fundamentais", syllabus.subjects.single().topics.single().externalId)
        assertEquals("remote-topic-1", syllabus.subjects.single().topics.single().children.single().parentRemoteTopicId)
        assertEquals(null, syllabus.subjects.single().topics.single().parentRemoteTopicId)
        assertEquals(null, acknowledgement.remoteSyllabusId)
        assertEquals(null, acknowledgement.safeError)
    }

    @Test
    fun rejectsRootTopicWithAParentLink() {
        val invalid = privateSyllabusJson.replace("\"parentRemoteTopicId\": null", "\"parentRemoteTopicId\": \"wrong-parent\"")

        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodePrivateSyllabus(invalid)
        }
    }

    @Test
    fun rejectsRemoteUnknownFieldsAndUnsupportedVersions() {
        val unknown = privateSyllabusJson.replace("\"status\": \"ACTIVE\"", "\"status\": \"ACTIVE\", \"unexpected\": true")
        val unsupported = privateSyllabusJson.replaceFirst("\"schemaVersion\": 1", "\"schemaVersion\": 2")

        assertThrows(SerializationException::class.java) {
            RemoteSyllabusContractJson.decodePrivateSyllabus(unknown)
        }
        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodePrivateSyllabus(unsupported)
        }
    }

    @Test
    fun rejectsDuplicateRemoteSubjectAndTopicIds() {
        val root = Json.parseToJsonElement(privateSyllabusJson).jsonObject
        val subjects = root["subjects"]!!.jsonArray
        val originalSubject = subjects.single().jsonObject
        val duplicateSubject = originalSubject.copyWith(
            "externalId" to JsonPrimitive("subject-outro"),
            "position" to JsonPrimitive(1),
        )
        val duplicateSubjectJson = root.copyWith("subjects" to JsonArray(listOf(originalSubject, duplicateSubject))).toString()

        val rootTopic = originalSubject["topics"]!!.jsonArray.single().jsonObject
        val child = rootTopic["children"]!!.jsonArray.single().jsonObject
        val duplicateTopic = child.copyWith("remoteTopicId" to rootTopic["remoteTopicId"]!!)
        val duplicateTopicJson = root.withSubject(
            originalSubject.withTopics(
                rootTopic.copyWith("children" to JsonArray(listOf(child, duplicateTopic))),
            ),
        ).toString()

        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodePrivateSyllabus(duplicateSubjectJson)
        }
        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodePrivateSyllabus(duplicateTopicJson)
        }
    }

    @Test
    fun rejectsDuplicateSiblingPositions() {
        val root = Json.parseToJsonElement(privateSyllabusJson).jsonObject
        val subject = root["subjects"]!!.jsonArray.single().jsonObject
        val topic = subject["topics"]!!.jsonArray.single().jsonObject
        val child = topic["children"]!!.jsonArray.single().jsonObject
        val sibling = child.copyWith(
            "remoteTopicId" to JsonPrimitive("remote-topic-3"),
            "externalId" to JsonPrimitive("topic-extra"),
        )
        val invalid = root.withSubject(
            subject.withTopics(topic.copyWith("children" to JsonArray(listOf(child, sibling)))),
        ).toString()

        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodePrivateSyllabus(invalid)
        }
    }

    @Test
    fun rejectsBlankIdsAndMalformedSyncTimestamps() {
        val blankSourceJobId = privateSyllabusJson.replace("\"sourceJobId\": \"job-1\"", "\"sourceJobId\": \" \"")
        val blankSyncJobId = syncAcknowledgementJson.replace("\"jobId\": \"job-1\"", "\"jobId\": \" \"")
        val malformedTimestamp = syncAcknowledgementJson.replace("2026-09-23T12:00:00Z", "not-a-timestamp")

        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodePrivateSyllabus(blankSourceJobId)
        }
        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodeSyncAcknowledgement(blankSyncJobId)
        }
        assertThrows(ContractValidationException::class.java) {
            RemoteSyllabusContractJson.decodeSyncAcknowledgement(malformedTimestamp)
        }
    }
}

private val privateSyllabusJson: String
    get() = fixture("private-syllabus.json")

private val syncAcknowledgementJson: String
    get() = fixture("sync-pending.json")

private fun fixture(name: String): String {
    val file = listOf(
        File("supabase/functions/_shared/fixtures/v1/$name"),
        File("../supabase/functions/_shared/fixtures/v1/$name"),
        File("../../supabase/functions/_shared/fixtures/v1/$name"),
    ).firstOrNull { it.isFile }
    check(file != null) { "Missing versioned fixture: $name" }
    return file.readText()
}

private fun JsonObject.copyWith(vararg overrides: Pair<String, JsonElement>): JsonObject = buildJsonObject {
    forEach { (key, value) -> put(key, value) }
    overrides.forEach { (key, value) -> put(key, value) }
}

private fun JsonObject.withTopics(topic: JsonObject): JsonObject = copyWith("topics" to JsonArray(listOf(topic)))

private fun JsonObject.withSubject(subject: JsonObject): JsonObject = copyWith("subjects" to JsonArray(listOf(subject)))
