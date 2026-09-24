package br.com.estudario.domain.ai

import br.com.estudario.data.ai.CURRENT_AI_SCHEMA_VERSION
import br.com.estudario.data.ai.AiWarning
import java.util.Collections
import java.util.IdentityHashMap

class AiSyllabusDraftValidationException(message: String) : IllegalArgumentException(message)

object AiSyllabusProposalValidator {
    const val MAX_TOPIC_DEPTH: Int = 32
    private const val MAX_NAME_LENGTH: Int = 200

    fun validateDraft(draft: AiSyllabusDraft): AiSyllabusDraft {
        if (draft.targetSyllabusId <= 0) fail("targetSyllabusId must be positive")
        requireName(draft.targetTitle, "targetTitle")
        draft.titleOverride?.let { requireName(it, "titleOverride") }
        requireName(draft.sourceVersion, "sourceVersion")
        if (draft.proposal.schemaVersion != CURRENT_AI_SCHEMA_VERSION) fail("unsupported proposal.schemaVersion")
        requireName(draft.proposal.promptVersion, "proposal.promptVersion")
        requireName(draft.proposal.modelVersion, "proposal.modelVersion")
        requireName(draft.proposal.documentTitle, "proposal.documentTitle")
        if (draft.sourceSchemaVersion != draft.proposal.schemaVersion) {
            fail("sourceSchemaVersion does not match proposal.schemaVersion")
        }
        if (draft.sourcePromptVersion != draft.proposal.promptVersion) {
            fail("sourcePromptVersion does not match proposal.promptVersion")
        }
        if (draft.sourceModelVersion != draft.proposal.modelVersion) {
            fail("sourceModelVersion does not match proposal.modelVersion")
        }
        draft.sourceHash?.let { if (!SHA256.matches(it)) fail("sourceHash must be a SHA-256") }
        if (!draft.warnings.containsAll(draft.proposal.warnings)) fail("warnings must retain the proposal warnings")
        if (!draft.ambiguities.containsAll(draft.proposal.ambiguities)) fail("ambiguities must retain the proposal ambiguities")
        validateWarnings(draft.warnings)
        if (draft.ambiguities.any(String::isBlank) || draft.ambiguities.size != draft.ambiguities.toSet().size) {
            fail("ambiguities must be unique and non-empty")
        }
        if (draft.subjects.isEmpty()) fail("subjects must not be empty")

        validateSiblings(draft.subjects, "subjects") { it.name to it.position }
        val subjectIds = HashSet<String>()
        val topicIds = HashSet<String>()
        draft.subjects.forEachIndexed { subjectIndex, subject ->
            val subjectPath = "subjects[$subjectIndex]"
            requireName(subject.name, "$subjectPath.name")
            requireExternalId(subject.externalId, "$subjectPath.externalId")
            if (!subjectIds.add(subject.externalId)) fail("$subjectPath.externalId: duplicate externalId ${subject.externalId}")
            requirePosition(subject.position, "$subjectPath.position")
            requireOptionalPages(subject.sourcePages, "$subjectPath.sourcePages")
            validateTopics(subject.topics, subjectPath, topicIds)
        }
        return draft
    }

    fun bindToTarget(draft: AiSyllabusDraft, targetSyllabusId: Long, targetTitle: String): AiSyllabusDraft {
        if (targetSyllabusId <= 0) fail("targetSyllabusId must be positive")
        requireName(targetTitle, "targetTitle")
        return draft.copy(targetSyllabusId = targetSyllabusId, targetTitle = targetTitle.trim(), titleOverride = null)
    }

    private fun validateTopics(topics: List<AiSyllabusDraftTopic>, path: String, allTopicIds: MutableSet<String>) {
        validateSiblings(topics, "$path.topics") { it.name to it.position }
        topics.forEachIndexed { index, topic ->
            validateTopic(topic, "$path.topics[$index]", 1, allTopicIds, Collections.newSetFromMap(IdentityHashMap()))
        }
    }

    private fun validateTopic(
        topic: AiSyllabusDraftTopic,
        path: String,
        depth: Int,
        allTopicIds: MutableSet<String>,
        visiting: MutableSet<AiSyllabusDraftTopic>,
    ) {
        if (!visiting.add(topic)) fail("$path: cycle detected")
        if (depth > MAX_TOPIC_DEPTH) fail("$path: depth exceeds $MAX_TOPIC_DEPTH")
        requireName(topic.name, "$path.name")
        requireExternalId(topic.externalId, "$path.externalId")
        if (!allTopicIds.add(topic.externalId)) fail("$path.externalId: duplicate externalId ${topic.externalId}")
        requirePosition(topic.position, "$path.position")
        requireOptionalPages(topic.sourcePages, "$path.sourcePages")
        validateSiblings(topic.children, "$path.children") { it.name to it.position }
        topic.children.forEachIndexed { index, child ->
            validateTopic(child, "$path.children[$index]", depth + 1, allTopicIds, visiting)
        }
        visiting.remove(topic)
    }

    private fun <T> validateSiblings(items: List<T>, path: String, key: (T) -> Pair<String, Int>) {
        val names = HashSet<String>()
        val positions = HashSet<Int>()
        items.forEachIndexed { index, item ->
            val (name, position) = key(item)
            val canonicalName = name.trim().lowercase()
            if (!names.add(canonicalName)) fail("$path[$index]: duplicate sibling name")
            if (!positions.add(position)) fail("$path[$index]: duplicate sibling position")
        }
    }

    private fun requireName(value: String, path: String) {
        if (value.isBlank() || value.length > MAX_NAME_LENGTH || value.any { it.isISOControl() }) fail("$path: invalid name")
    }

    private fun requireExternalId(value: String, path: String) {
        if (value.isBlank() || value.length > MAX_NAME_LENGTH || value.any { it.isWhitespace() || it.isISOControl() }) fail("$path: invalid externalId")
    }

    private fun requirePosition(value: Int, path: String) {
        if (value < 0) fail("$path: position must be >= 0")
    }

    private fun requirePages(value: List<Int>, path: String) {
        if (value.isEmpty() || value.any { it < 1 } || value.size != value.toSet().size) fail("$path: invalid source pages")
    }

    private fun requireOptionalPages(value: List<Int>, path: String) {
        if (value.isNotEmpty() && (value.any { it < 1 } || value.size != value.toSet().size)) fail("$path: invalid source pages")
    }

    private fun validateWarnings(warnings: List<AiWarning>) {
        warnings.forEachIndexed { index, warning ->
            requireName(warning.message, "warnings[$index].message")
            requirePages(warning.sourcePages, "warnings[$index].sourcePages")
            warning.ambiguity?.let { requireName(it, "warnings[$index].ambiguity") }
        }
    }

    private fun fail(message: String): Nothing = throw AiSyllabusDraftValidationException(message)

    private val SHA256 = Regex("[0-9a-fA-F]{64}")
}

fun validateDraft(draft: AiSyllabusDraft): AiSyllabusDraft = AiSyllabusProposalValidator.validateDraft(draft)

fun bindToTarget(draft: AiSyllabusDraft, targetSyllabusId: Long, targetTitle: String): AiSyllabusDraft =
    AiSyllabusProposalValidator.bindToTarget(draft, targetSyllabusId, targetTitle)
