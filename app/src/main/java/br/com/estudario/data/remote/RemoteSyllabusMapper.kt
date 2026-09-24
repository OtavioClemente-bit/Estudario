package br.com.estudario.data.remote

import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.ContentOriginType
import br.com.estudario.data.local.Priority
import br.com.estudario.data.transfer.EstudoPackageCodec
import br.com.estudario.data.transfer.EstudoPackageParser
import br.com.estudario.data.transfer.PackagePlan
import br.com.estudario.data.transfer.SubjectPlan
import br.com.estudario.data.transfer.TopicPlan
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.UUID

/** Bridges the canonical `.estudo` snapshot and the normalized private-library tree. */
object RemoteSyllabusMapper {
    private const val TOPIC_PRIORITY_METADATA_KEY = "__estudario_official_priority"

    fun fromLocal(competition: CompetitionEntity, packageJson: String, payloadHash: String): PrivateSyllabus {
        val plan = EstudoPackageParser.parse(packageJson)
        val stableIdentity = JSONObject(plan.metadata?.toString() ?: "{}").optString("stableIdentity").takeIf { it.isNotBlank() && it != "null" }
            ?: "competition:${competition.externalId ?: plan.competitionId}"
        val remoteSyllabusId = competition.remoteSyllabusId ?: stableId("syllabus", stableIdentity)
        val rootMetadata = JSONObject(plan.metadata?.toString() ?: "{}")
            .put("stableIdentity", stableIdentity)
            .put("payloadHash", payloadHash)
            .put("remoteSyllabusId", remoteSyllabusId)
            .put("localSyllabusId", competition.id)
            .put("localSyllabusExternalId", competition.externalId ?: plan.competitionId)
            .put("packageId", plan.packageId)
            .put("canonicalPayload", packageJson)
        val subjects = plan.subjects.sortedBy { it.position }.map { subject ->
            val subjectId = stableId("subject:$remoteSyllabusId", subject.externalId)
            PrivateSyllabusSubject(
                remoteSubjectId = subjectId,
                externalId = subject.externalId,
                name = subject.name,
                position = subject.position,
                suggestedPriority = subject.priority.toAiPriority(),
                packageVersion = plan.packageVersion,
                schemaVersion = plan.schemaVersion,
                metadata = jsonMetadata(subject.metadata),
                topics = subject.topics.sortedBy { it.position }.map { topic -> topicFromPlan(topic, subjectId, null, plan) },
            )
        }
        return PrivateSyllabus(
            remoteSyllabusId = remoteSyllabusId,
            title = competition.name,
            position = 0,
            visibility = PrivateSyllabusVisibility.PRIVATE,
            source = sourceFromMetadata(rootMetadata),
            sourceJobId = rootMetadata.optString("sourceJobId").takeIf { it.isNotBlank() && it != "null" },
            sourceHash = rootMetadata.optString("sourceHash").takeIf { it.matches(Regex("[0-9a-fA-F]{64}")) }?.lowercase(),
            schemaVersion = plan.schemaVersion,
            status = PrivateSyllabusStatus.ACTIVE,
            metadata = jsonObject(rootMetadata),
            subjects = subjects,
        )
    }

    fun toOfficialPackage(remote: PrivateSyllabus): String {
        val rootMetadata = JSONObject(remote.metadata.toString())
            .put("remoteSyllabusId", remote.remoteSyllabusId)
        rootMetadata.optString("canonicalPayload").takeIf { it.isNotBlank() && it != "null" }?.let { canonical ->
            return canonical.also { EstudoPackageParser.parse(it) }
        }
        val packageId = rootMetadata.optString("packageId").takeIf { it.isNotBlank() && it != "null" }
            ?: "private-syllabus-${remote.remoteSyllabusId}"
        val competitionId = rootMetadata.optString("localSyllabusExternalId").takeIf { it.isNotBlank() && it != "null" }
            ?: remote.remoteSyllabusId
        val packageVersion = remote.subjects.firstOrNull()?.packageVersion ?: rootMetadata.optString("packageVersion", "estudo-v2")
        val subjects = remote.subjects.sortedBy { it.position }.map { subject ->
            SubjectPlan(
                id = subject.externalId,
                name = subject.name,
                position = subject.position,
                topics = subject.topics.sortedBy { it.position }.map { topic -> topicToPlan(topic, null) },
                externalId = subject.externalId,
                priority = subject.suggestedPriority.toPriority(),
                sourcePages = emptyList(),
                metadata = metadataOrNull(subject.metadata),
            )
        }
        return EstudoPackageCodec.encode(
            PackagePlan(
                version = 2,
                packageId = packageId,
                competitionId = competitionId,
                competitionName = remote.title,
                primary = false,
                subjects = subjects,
                schemaVersion = remote.schemaVersion,
                packageVersion = packageVersion,
                metadata = rootMetadata,
            ),
        ).also { EstudoPackageParser.parse(it) }
    }

    private fun topicFromPlan(topic: TopicPlan, subjectId: String, parentRemoteTopicId: String?, plan: PackagePlan): PrivateSyllabusTopic {
        val remoteTopicId = stableId("topic:$subjectId", topic.externalId)
        return PrivateSyllabusTopic(
            remoteTopicId = remoteTopicId,
            externalId = topic.externalId,
            parentRemoteTopicId = parentRemoteTopicId,
            name = topic.title,
            position = topic.position,
            packageVersion = plan.packageVersion,
            schemaVersion = plan.schemaVersion,
            metadata = topicMetadata(topic),
            children = topic.children.sortedBy { it.position }.map { child -> topicFromPlan(child, subjectId, remoteTopicId, plan) },
        )
    }

    private fun topicToPlan(topic: PrivateSyllabusTopic, parentExternalId: String?): TopicPlan = TopicPlan(
        id = topic.externalId,
        title = topic.name,
        description = "",
        notes = "",
        position = topic.position,
        priority = topic.metadata[TOPIC_PRIORITY_METADATA_KEY]?.toString()?.trim('"')?.let { value ->
            runCatching { Priority.valueOf(value) }.getOrNull()
        } ?: Priority.NORMAL,
        originType = ContentOriginType.EDITAL,
        theories = emptyList(),
        summaries = emptyList(),
        snippets = emptyList(),
        questions = emptyList(),
        errorConcepts = emptyList(),
        children = topic.children.sortedBy { it.position }.map { child -> topicToPlan(child, topic.externalId) },
        externalId = topic.externalId,
        parentExternalId = parentExternalId,
        metadata = metadataOrNull(topic.metadata),
    )

    private fun stableId(namespace: String, value: String): String = UUID.nameUUIDFromBytes(
        "$namespace:$value".toByteArray(StandardCharsets.UTF_8),
    ).toString()

    private fun jsonObject(value: JSONObject): JsonObject = Json.parseToJsonElement(value.toString()).jsonObject

    private fun jsonMetadata(value: JSONObject?): JsonObject = value?.let(::jsonObject) ?: buildJsonObject { }

    private fun topicMetadata(topic: TopicPlan): JsonObject = JSONObject(topic.metadata?.toString() ?: "{}").apply {
        put(TOPIC_PRIORITY_METADATA_KEY, topic.priority.name)
    }.let(::jsonObject)

    private fun metadataOrNull(value: JsonObject): JSONObject? = value
        .filterKeys { it != TOPIC_PRIORITY_METADATA_KEY }
        .takeIf { it.isNotEmpty() }
        ?.let { JSONObject(JsonObject(it).toString()) }

    private fun sourceFromMetadata(metadata: JSONObject): PrivateSyllabusSource = when (metadata.optString("source").uppercase()) {
        "IMPORTED" -> PrivateSyllabusSource.IMPORTED
        "MANUAL" -> PrivateSyllabusSource.MANUAL
        else -> PrivateSyllabusSource.AI_GENERATED
    }
}

private fun Priority.toAiPriority(): AiPriority = when (this) {
    Priority.BAIXA -> AiPriority.LOW
    Priority.NORMAL -> AiPriority.NORMAL
    Priority.ALTA -> AiPriority.HIGH
}

private fun AiPriority.toPriority(): Priority = when (this) {
    AiPriority.LOW -> Priority.BAIXA
    AiPriority.NORMAL -> Priority.NORMAL
    AiPriority.HIGH -> Priority.ALTA
}
