package br.com.estudario.domain.ai

import br.com.estudario.data.ai.AiWarning
import br.com.estudario.data.local.ContentOriginType
import br.com.estudario.data.local.Priority
import br.com.estudario.data.transfer.EstudoPackageCodec
import br.com.estudario.data.transfer.EstudoPackageParser
import br.com.estudario.data.transfer.PackagePlan
import br.com.estudario.data.transfer.PackageWarning
import br.com.estudario.data.transfer.SubjectPlan
import br.com.estudario.data.transfer.TopicPlan
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

object AiSyllabusToEstudoMapper {
    fun toOfficialPackage(draft: AiSyllabusDraft): String {
        val checked = AiSyllabusProposalValidator.validateDraft(draft)
        val packageId = packageId(checked)
        val plan = PackagePlan(
            version = 2,
            packageId = packageId,
            competitionId = checked.targetSyllabusId.toString(),
            competitionName = checked.targetTitle.trim(),
            primary = false,
            subjects = checked.subjects.sortedBy { it.position }.map(::subjectPlan),
            schemaVersion = OFFICIAL_ESTUDO_SCHEMA_VERSION,
            packageVersion = OFFICIAL_ESTUDO_PACKAGE_VERSION,
            metadata = metadata(checked),
            warnings = checked.warnings.map(::warningPlan),
        )
        val encoded = EstudoPackageCodec.encode(plan)

        // This is the existing .estudo boundary: generated output must be consumable by the same parser
        // used by EstudoPackageService, without introducing an AI-specific import format.
        EstudoPackageParser.parse(encoded)
        return encoded
    }

    private fun subjectPlan(subject: AiSyllabusDraftSubject): SubjectPlan = SubjectPlan(
        id = subject.externalId,
        name = subject.name.trim(),
        position = subject.position,
        topics = subject.topics.sortedBy { it.position }.map { topicPlan(it, null) },
        externalId = subject.externalId,
        priority = subject.suggestedPriority.toOfficialPriority(),
        sourcePages = subject.sourcePages,
    )

    private fun topicPlan(topic: AiSyllabusDraftTopic, parentExternalId: String?): TopicPlan = TopicPlan(
        id = topic.externalId,
        title = topic.name.trim(),
        description = "",
        notes = "",
        position = topic.position,
        priority = Priority.NORMAL,
        originType = ContentOriginType.EDITAL,
        theories = emptyList(),
        summaries = emptyList(),
        snippets = emptyList(),
        questions = emptyList(),
        errorConcepts = emptyList(),
        children = topic.children.sortedBy { it.position }.map { topicPlan(it, topic.externalId) },
        externalId = topic.externalId,
        parentExternalId = parentExternalId,
        sourcePages = topic.sourcePages,
    )

    private fun metadata(draft: AiSyllabusDraft): JSONObject = JSONObject()
        .put("packageVersion", OFFICIAL_ESTUDO_PACKAGE_VERSION)
        .put("schemaVersion", draft.proposal.schemaVersion)
        .put("promptVersion", draft.proposal.promptVersion)
        .put("modelVersion", draft.proposal.modelVersion)
        .put("sourceVersion", draft.sourceVersion)
        .put("sourceFileName", draft.importedFileName ?: JSONObject.NULL)
        .put("sourceHash", draft.sourceHash ?: JSONObject.NULL)
        .put("targetSyllabusId", draft.targetSyllabusId)
        .put("documentTitle", draft.proposal.documentTitle)
        .put("ambiguities", JSONArray(draft.ambiguities))
        .put("warnings", JSONArray(draft.warnings.map(::warningJson)))

    private fun warningJson(warning: AiWarning): JSONObject = JSONObject()
        .put("code", warning.code.name)
        .put("severity", warning.severity.name)
        .put("message", warning.message)
        .put("sourcePages", JSONArray(warning.sourcePages))
        .put("ambiguity", warning.ambiguity ?: JSONObject.NULL)

    private fun warningPlan(warning: AiWarning): PackageWarning = PackageWarning(
        code = warning.code.name,
        severity = warning.severity.name,
        message = warning.message,
        sourcePages = warning.sourcePages,
        ambiguity = warning.ambiguity,
    )

    private fun packageId(draft: AiSyllabusDraft): String {
        val fingerprint = buildString {
            append(draft.targetSyllabusId).append('|')
            draft.subjects.sortedBy { it.position }.forEach { subject ->
                append(subject.externalId).append(':').append(subject.position).append('|')
                subject.topics.sortedBy { it.position }.forEach { topic -> appendTopics(this, topic) }
            }
        }
        val digest = MessageDigest.getInstance("SHA-256").digest(fingerprint.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }.take(24)
        return "ai-syllabus-${draft.targetSyllabusId}-$digest"
    }

    private fun appendTopics(builder: StringBuilder, topic: AiSyllabusDraftTopic) {
        builder.append(topic.externalId).append(':').append(topic.position).append('|')
        topic.children.sortedBy { it.position }.forEach { appendTopics(builder, it) }
    }
}

fun toOfficialPackage(draft: AiSyllabusDraft): String = AiSyllabusToEstudoMapper.toOfficialPackage(draft)

private fun br.com.estudario.data.ai.AiPriority.toOfficialPriority(): Priority = when (this) {
    br.com.estudario.data.ai.AiPriority.LOW -> Priority.BAIXA
    br.com.estudario.data.ai.AiPriority.NORMAL -> Priority.NORMAL
    br.com.estudario.data.ai.AiPriority.HIGH -> Priority.ALTA
}
