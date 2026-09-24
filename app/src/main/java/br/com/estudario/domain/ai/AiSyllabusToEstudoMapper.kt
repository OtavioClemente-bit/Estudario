package br.com.estudario.domain.ai

import br.com.estudario.data.ai.AiWarning
import br.com.estudario.data.transfer.EstudoPackageParser
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

object AiSyllabusToEstudoMapper {
    fun toOfficialPackage(draft: AiSyllabusDraft): String {
        val checked = AiSyllabusProposalValidator.validateDraft(draft)
        val packageId = packageId(checked)
        val root = JSONObject()
            .put("format", "estudario-estudo")
            .put("version", 2)
            .put("schemaVersion", OFFICIAL_ESTUDO_SCHEMA_VERSION)
            .put("packageVersion", OFFICIAL_ESTUDO_PACKAGE_VERSION)
            .put("packageId", packageId)
            .put(
                "concurso",
                JSONObject()
                    .put("id", checked.targetSyllabusId.toString())
                    .put("nome", checked.effectiveTitle)
                    .put("principal", false),
            )
            .put("materias", JSONArray(checked.subjects.sortedBy { it.position }.map(::subjectJson)))
            .put("warnings", JSONArray(checked.warnings.map(::warningJson)))
            .put("metadata", metadata(checked))
            .toString(2)

        // This is the existing .estudo boundary: generated output must be consumable by the same parser
        // used by EstudoPackageService, without introducing an AI-specific import format.
        EstudoPackageParser.parse(root)
        return root
    }

    private fun subjectJson(subject: AiSyllabusDraftSubject): JSONObject = JSONObject()
        .put("id", subject.externalId)
        .put("externalId", subject.externalId)
        .put("nome", subject.name.trim())
        .put("ordem", subject.position)
        .put("prioridade", subject.suggestedPriority.officialName())
        .put("sourcePages", JSONArray(subject.sourcePages))
        .put("topicos", JSONArray(subject.topics.sortedBy { it.position }.map { topicJson(it, null) }))

    private fun topicJson(topic: AiSyllabusDraftTopic, parentExternalId: String?): JSONObject = JSONObject()
        .put("id", topic.externalId)
        .put("externalId", topic.externalId)
        .put("titulo", topic.name.trim())
        .put("ordem", topic.position)
        .put("prioridade", "NORMAL")
        .put("parentExternalId", parentExternalId ?: JSONObject.NULL)
        .put("sourcePages", JSONArray(topic.sourcePages))
        .put("subtopicos", JSONArray(topic.children.sortedBy { it.position }.map { topicJson(it, topic.externalId) }))

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

private fun br.com.estudario.data.ai.AiPriority.officialName(): String = when (this) {
    br.com.estudario.data.ai.AiPriority.LOW -> "BAIXA"
    br.com.estudario.data.ai.AiPriority.NORMAL -> "NORMAL"
    br.com.estudario.data.ai.AiPriority.HIGH -> "ALTA"
}
