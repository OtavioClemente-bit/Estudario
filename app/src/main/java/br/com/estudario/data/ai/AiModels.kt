package br.com.estudario.data.ai

import java.time.Instant
import java.time.format.DateTimeParseException
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
    val used: Int = successfulCount + reservedCount,
    val resetAt: String? = null,
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
        job.validate()
    }

    fun decodeAccess(raw: String): AiAccess = json.decodeFromString<AiAccess>(raw).also { access ->
        access.validate()
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

private fun AiAccess.validate() {
    quota?.validate("access.quota")
    reasonCode?.let { requireContractText(it, "access.reasonCode") }
}

private fun AiJob.validate() {
    requireContractText(jobId, "job.jobId")
    if (schemaVersion != null && schemaVersion != CURRENT_AI_SCHEMA_VERSION) {
        throw ContractValidationException("job.schemaVersion: unsupported schema version $schemaVersion")
    }
    promptVersion?.let { requireContractText(it, "job.promptVersion") }
    modelVersion?.let { requireContractText(it, "job.modelVersion") }
    proposal?.validate()
    warnings.validateWarnings("job.warnings")
    errorCode?.let { requireContractText(it, "job.errorCode") }
    errorMessage?.let { requireContractText(it, "job.errorMessage") }
    requireContractInstant(createdAt, "job.createdAt")
    requireContractInstant(updatedAt, "job.updatedAt")
    finishedAt?.let { requireContractInstant(it, "job.finishedAt") }
    providerExecutionStartedAt?.let { requireContractInstant(it, "job.providerExecutionStartedAt") }
    if (status == AiJobStatus.SUCCEEDED) {
        val completedProposal = proposal
            ?: throw ContractValidationException("job.proposal: required for SUCCEEDED jobs")
        if (schemaVersion != CURRENT_AI_SCHEMA_VERSION) {
            throw ContractValidationException("job.schemaVersion: required for SUCCEEDED jobs")
        }
        if (promptVersion != completedProposal.promptVersion) {
            throw ContractValidationException("job.promptVersion: does not match proposal")
        }
        if (modelVersion != completedProposal.modelVersion) {
            throw ContractValidationException("job.modelVersion: does not match proposal")
        }
        if (finishedAt == null) {
            throw ContractValidationException("job.finishedAt: required for SUCCEEDED jobs")
        }
    }
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
    requireContractText(periodStart, "$path.periodStart")
    if (used != successfulCount + reservedCount) throw ContractValidationException("$path.used: does not match counts")
    resetAt?.let { requireContractInstant(it, "$path.resetAt") }
}

private fun requireText(value: String, path: String) {
    if (value.isBlank()) throw ContractValidationException("$path: must be non-empty")
}

internal fun requireContractText(value: String, path: String) {
    if (value.isBlank()) throw ContractValidationException("$path: must be non-empty")
}

internal fun requireContractInstant(value: String, path: String) {
    requireContractText(value, path)
    if (!ISO_INSTANT.matches(value)) throw ContractValidationException("$path: must be an ISO-8601 date-time")
    try {
        Instant.parse(value)
    } catch (_: DateTimeParseException) {
        throw ContractValidationException("$path: must be an ISO-8601 date-time")
    }
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

private val ISO_INSTANT = Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d{1,9})?(?:Z|[+-]\d{2}:\d{2})""")
