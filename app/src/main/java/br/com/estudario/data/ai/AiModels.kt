package br.com.estudario.data.ai

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

const val CURRENT_AI_SCHEMA_VERSION: Int = 1

enum class AiFeature { SYLLABUS_GENERATION, PLAN_GENERATION, CONTENT_GENERATION }
enum class AiJobStatus { RESERVED, PROCESSING, SUCCEEDED, FAILED, EXPIRED, CANCELLED }
enum class AiPriority { LOW, NORMAL, HIGH }
enum class AiWarningSeverity { INFO, WARNING, ERROR }
enum class AiWarningCode {
    UNREADABLE_PAGES,
    PARTIAL_TEXT_EXTRACTION,
    AMBIGUOUS_STRUCTURE,
    INCOMPLETE_STRUCTURE,
    POSSIBLE_DUPLICATE_SECTION,
    DOCUMENT_MISMATCH,
    TRUNCATED_SOURCE,
}

@Serializable
data class AiWarning(
    val code: AiWarningCode,
    val severity: AiWarningSeverity,
    val message: String,
    val sourcePages: List<Int>,
    val ambiguity: String?,
)

@Serializable
data class AiTopicProposal(
    val name: String,
    val position: Int,
    val children: List<AiTopicProposal>,
    val sourcePages: List<Int>,
)

@Serializable
data class AiSubjectProposal(
    val name: String,
    val position: Int,
    val suggestedPriority: AiPriority,
    val topics: List<AiTopicProposal>,
    val sourcePages: List<Int>,
)

@Serializable
data class AiSyllabusProposal(
    val schemaVersion: Int,
    val promptVersion: String,
    val modelVersion: String,
    val documentTitle: String,
    val subjects: List<AiSubjectProposal>,
    val warnings: List<AiWarning>,
    val ambiguities: List<String>,
)

@Serializable
data class AiQuota(
    val feature: AiFeature,
    val limit: Int,
    val successfulCount: Int,
    val reservedCount: Int,
    val remaining: Int,
    val periodStart: String,
)

@Serializable
data class AiAccess(
    val authenticated: Boolean,
    val betaAccess: Boolean,
    val feature: AiFeature,
    val featureEnabled: Boolean,
    val quota: AiQuota?,
    val canUse: Boolean,
    val reasonCode: String?,
)

@Serializable
data class AiJob(
    val jobId: String,
    val feature: AiFeature,
    val status: AiJobStatus,
    val schemaVersion: Int?,
    val promptVersion: String?,
    val modelVersion: String?,
    val proposal: AiSyllabusProposal?,
    val warnings: List<AiWarning>,
    val errorCode: String?,
    val errorMessage: String?,
    val createdAt: String,
    val updatedAt: String,
    val finishedAt: String?,
    val providerExecutionStartedAt: String?,
)

class ContractValidationException(message: String) : IllegalArgumentException(message)

object EstudarioContractJson {
    val json: Json = Json {
        ignoreUnknownKeys = false
        explicitNulls = true
        encodeDefaults = true
        isLenient = false
        coerceInputValues = false
    }

    fun decodeProposal(raw: String): AiSyllabusProposal = json.decodeFromString<AiSyllabusProposal>(raw).also { proposal ->
        proposal.validate()
    }

    fun decodeJob(raw: String): AiJob = json.decodeFromString<AiJob>(raw).also { job ->
        if (job.schemaVersion != null && job.schemaVersion != CURRENT_AI_SCHEMA_VERSION) {
            throw ContractValidationException("job.schemaVersion: unsupported schema version ${job.schemaVersion}")
        }
        job.proposal?.validate()
        job.warnings.validateWarnings("job.warnings")
    }

    fun decodeAccess(raw: String): AiAccess = json.decodeFromString<AiAccess>(raw).also { access ->
        access.quota?.validate("access.quota")
    }
}

private fun AiSyllabusProposal.validate() {
    if (schemaVersion != CURRENT_AI_SCHEMA_VERSION) {
        throw ContractValidationException("proposal.schemaVersion: unsupported schema version $schemaVersion")
    }
    requireText(promptVersion, "proposal.promptVersion")
    requireText(modelVersion, "proposal.modelVersion")
    requireText(documentTitle, "proposal.documentTitle")
    if (subjects.isEmpty()) throw ContractValidationException("proposal.subjects: must not be empty")
    requireUniquePositions(subjects.map { it.position }, "proposal.subjects")
    subjects.forEachIndexed { index, subject -> subject.validate("proposal.subjects[$index]") }
    warnings.validateWarnings("proposal.warnings")
    ambiguities.forEachIndexed { index, ambiguity -> requireText(ambiguity, "proposal.ambiguities[$index]") }
    if (ambiguities.size != ambiguities.toSet().size) throw ContractValidationException("proposal.ambiguities: duplicate entries")
}

private fun AiSubjectProposal.validate(path: String) {
    requireText(name, "$path.name")
    requirePosition(position, "$path.position")
    if (topics.isEmpty()) throw ContractValidationException("$path.topics: must not be empty")
    requireUniquePositions(topics.map { it.position }, "$path.topics")
    topics.forEachIndexed { index, topic -> topic.validate("$path.topics[$index]") }
    requirePages(sourcePages, "$path.sourcePages")
}

private fun AiTopicProposal.validate(path: String) {
    requireText(name, "$path.name")
    requirePosition(position, "$path.position")
    requireUniquePositions(children.map { it.position }, "$path.children")
    children.forEachIndexed { index, child -> child.validate("$path.children[$index]") }
    requirePages(sourcePages, "$path.sourcePages")
}

private fun List<AiWarning>.validateWarnings(path: String) {
    forEachIndexed { index, warning ->
        requireText(warning.message, "$path[$index].message")
        requirePages(warning.sourcePages, "$path[$index].sourcePages")
        warning.ambiguity?.let { requireText(it, "$path[$index].ambiguity") }
    }
}

private fun AiQuota.validate(path: String) {
    if (limit < 1 || successfulCount < 0 || reservedCount < 0 || remaining < 0 || successfulCount + reservedCount + remaining != limit) {
        throw ContractValidationException("$path: invalid quota counts")
    }
}

private fun requireText(value: String, path: String) {
    if (value.isBlank()) throw ContractValidationException("$path: must be non-empty")
}

private fun requirePosition(value: Int, path: String) {
    if (value < 0) throw ContractValidationException("$path: must be >= 0")
}

private fun requirePages(value: List<Int>, path: String) {
    if (value.isEmpty() || value.any { it < 1 } || value.size != value.toSet().size) {
        throw ContractValidationException("$path: must contain unique positive pages")
    }
}

private fun requireUniquePositions(values: List<Int>, path: String) {
    if (values.any { it < 0 } || values.size != values.toSet().size) {
        throw ContractValidationException("$path: positions must be unique and >= 0")
    }
}
