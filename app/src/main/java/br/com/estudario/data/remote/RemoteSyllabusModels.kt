package br.com.estudario.data.remote

import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.CURRENT_AI_SCHEMA_VERSION
import br.com.estudario.data.ai.ContractValidationException
import br.com.estudario.data.ai.EstudarioContractJson
import br.com.estudario.data.ai.requireContractInstant
import br.com.estudario.data.ai.requireContractText
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonObject

enum class PrivateSyllabusVisibility { PRIVATE }
enum class PrivateSyllabusSource { AI_GENERATED, IMPORTED, MANUAL }
enum class PrivateSyllabusStatus { ACTIVE, ARCHIVED, DELETED }
enum class RemoteSyllabusSyncState { PENDING, SYNCED, FAILED }

@Serializable
data class PrivateSyllabusTopic(
    val remoteTopicId: String,
    val externalId: String,
    val parentRemoteTopicId: String?,
    val name: String,
    val position: Int,
    val packageVersion: String,
    val schemaVersion: Int,
    val metadata: JsonObject,
    val children: List<PrivateSyllabusTopic>,
)

@Serializable
data class PrivateSyllabusSubject(
    val remoteSubjectId: String,
    val externalId: String,
    val name: String,
    val position: Int,
    val suggestedPriority: AiPriority,
    val packageVersion: String,
    val schemaVersion: Int,
    val metadata: JsonObject,
    val topics: List<PrivateSyllabusTopic>,
)

@Serializable
data class PrivateSyllabus(
    val remoteSyllabusId: String,
    val title: String,
    val position: Int,
    val visibility: PrivateSyllabusVisibility,
    val source: PrivateSyllabusSource,
    val sourceJobId: String?,
    val sourceHash: String?,
    val schemaVersion: Int,
    val status: PrivateSyllabusStatus,
    val metadata: JsonObject,
    val subjects: List<PrivateSyllabusSubject>,
)

@Serializable
data class RemoteSyllabusSyncAcknowledgement(
    val remoteSyllabusId: String?,
    val jobId: String?,
    val payloadHash: String,
    val state: RemoteSyllabusSyncState,
    val attemptCount: Int,
    val nextAttemptAt: String?,
    val safeError: String?,
    val createdAt: String,
    val updatedAt: String,
    val attemptToken: String?,
)

object RemoteSyllabusContractJson {
    fun decodePrivateSyllabus(raw: String): PrivateSyllabus = EstudarioContractJson.json.decodeFromString<PrivateSyllabus>(raw).also { it.validate() }

    fun decodeSyncAcknowledgement(raw: String): RemoteSyllabusSyncAcknowledgement = EstudarioContractJson.json.decodeFromString<RemoteSyllabusSyncAcknowledgement>(raw).also { it.validate() }
}

private fun PrivateSyllabus.validate() {
    requireText(remoteSyllabusId, "privateSyllabus.remoteSyllabusId")
    requireText(title, "privateSyllabus.title")
    requirePosition(position, "privateSyllabus.position")
    requireSchema(schemaVersion, "privateSyllabus.schemaVersion")
    sourceJobId?.let { requireContractText(it, "privateSyllabus.sourceJobId") }
    if (subjects.isEmpty()) throw ContractValidationException("privateSyllabus.subjects: must not be empty")
    requireUniquePositions(subjects.map { it.position }, "privateSyllabus.subjects")
    if (subjects.map { it.externalId }.toSet().size != subjects.size) {
        throw ContractValidationException("privateSyllabus.subjects.externalId: duplicate")
    }
    if (subjects.map { it.remoteSubjectId }.toSet().size != subjects.size) {
        throw ContractValidationException("privateSyllabus.subjects.remoteSubjectId: duplicate")
    }
    val remoteTopicIds = subjects.flatMap { subject -> subject.topics.flatMap { it.allRemoteTopicIds() } }
    if (remoteTopicIds.toSet().size != remoteTopicIds.size) {
        throw ContractValidationException("privateSyllabus.subjects.remoteTopicId: duplicate")
    }
    subjects.forEachIndexed { index, subject -> subject.validate("privateSyllabus.subjects[$index]") }
    if (sourceHash != null && !SHA256.matches(sourceHash)) throw ContractValidationException("privateSyllabus.sourceHash: invalid SHA-256")
}

private fun PrivateSyllabusSubject.validate(path: String) {
    requireText(remoteSubjectId, "$path.remoteSubjectId")
    requireText(externalId, "$path.externalId")
    requireText(name, "$path.name")
    requirePosition(position, "$path.position")
    requireText(packageVersion, "$path.packageVersion")
    requireSchema(schemaVersion, "$path.schemaVersion")
    requireUniquePositions(topics.map { it.position }, "$path.topics")
    val ids = topics.flatMap { it.allExternalIds() }
    if (ids.toSet().size != ids.size) throw ContractValidationException("$path.topics.externalId: duplicate")
    topics.forEachIndexed { index, topic -> topic.validate("$path.topics[$index]") }
}

private fun PrivateSyllabusTopic.validate(path: String, expectedParentRemoteTopicId: String? = null) {
    requireText(remoteTopicId, "$path.remoteTopicId")
    requireText(externalId, "$path.externalId")
    if (parentRemoteTopicId != expectedParentRemoteTopicId) {
        throw ContractValidationException("$path.parentRemoteTopicId: must point to the containing topic")
    }
    requireText(name, "$path.name")
    requirePosition(position, "$path.position")
    requireText(packageVersion, "$path.packageVersion")
    requireSchema(schemaVersion, "$path.schemaVersion")
    requireUniquePositions(children.map { it.position }, "$path.children")
    children.forEachIndexed { index, child -> child.validate("$path.children[$index]", remoteTopicId) }
}

private fun PrivateSyllabusTopic.allExternalIds(): List<String> = listOf(externalId) + children.flatMap { it.allExternalIds() }

private fun PrivateSyllabusTopic.allRemoteTopicIds(): List<String> = listOf(remoteTopicId) + children.flatMap { it.allRemoteTopicIds() }

private fun RemoteSyllabusSyncAcknowledgement.validate() {
    remoteSyllabusId?.let { requireContractText(it, "syncAcknowledgement.remoteSyllabusId") }
    jobId?.let { requireContractText(it, "syncAcknowledgement.jobId") }
    if (payloadHash.length != 64 || !SHA256.matches(payloadHash)) throw ContractValidationException("syncAcknowledgement.payloadHash: invalid SHA-256")
    if (attemptCount < 0) throw ContractValidationException("syncAcknowledgement.attemptCount: must be >= 0")
    nextAttemptAt?.let { requireContractInstant(it, "syncAcknowledgement.nextAttemptAt") }
    safeError?.let { requireContractText(it, "syncAcknowledgement.safeError") }
    requireContractInstant(createdAt, "syncAcknowledgement.createdAt")
    requireContractInstant(updatedAt, "syncAcknowledgement.updatedAt")
    attemptToken?.let { requireContractText(it, "syncAcknowledgement.attemptToken") }
}

private fun requireText(value: String, path: String) {
    if (value.isBlank()) throw ContractValidationException("$path: must be non-empty")
}

private fun requirePosition(value: Int, path: String) {
    if (value < 0) throw ContractValidationException("$path: must be >= 0")
}

private fun requireUniquePositions(values: List<Int>, path: String) {
    if (values.any { it < 0 } || values.size != values.toSet().size) throw ContractValidationException("$path: positions must be unique and >= 0")
}

private fun requireSchema(value: Int, path: String) {
    if (value != CURRENT_AI_SCHEMA_VERSION) throw ContractValidationException("$path: unsupported schema version $value")
}

private val SHA256 = Regex("[0-9a-f]{64}")
