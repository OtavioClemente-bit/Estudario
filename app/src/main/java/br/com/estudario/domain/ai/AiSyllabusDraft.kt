package br.com.estudario.domain.ai

import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.ai.AiWarning
import java.security.MessageDigest
import java.text.Normalizer

const val OFFICIAL_ESTUDO_PACKAGE_VERSION: String = "estudo-v2"
const val OFFICIAL_ESTUDO_SCHEMA_VERSION: Int = 1

/** Editable, local-only representation of a reviewed AI proposal. */
data class AiSyllabusDraft(
    val targetSyllabusId: Long,
    val proposal: AiSyllabusProposal,
    val targetTitle: String = "",
    val importedFileName: String? = null,
    val titleOverride: String? = null,
    val subjects: List<AiSyllabusDraftSubject> = proposal.subjects.map { AiSyllabusDraftSubject.fromProposal(it) },
    val warnings: List<AiWarning> = proposal.warnings,
    val ambiguities: List<String> = proposal.ambiguities,
    val sourceVersion: String = "pdf-source-v1",
    val sourcePromptVersion: String = proposal.promptVersion,
    val sourceModelVersion: String = proposal.modelVersion,
    val sourceSchemaVersion: Int = proposal.schemaVersion,
    val sourceHash: String? = null,
) {
    val effectiveTitle: String
        get() = titleOverride?.trim()?.takeIf { it.isNotEmpty() } ?: targetTitle.trim()

    companion object {
        fun fromProposal(
            targetSyllabusId: Long,
            targetTitle: String,
            proposal: AiSyllabusProposal,
            importedFileName: String? = null,
            titleOverride: String? = null,
            sourceVersion: String = "pdf-source-v1",
            sourcePromptVersion: String = proposal.promptVersion,
            sourceModelVersion: String = proposal.modelVersion,
            sourceSchemaVersion: Int = proposal.schemaVersion,
            sourceHash: String? = null,
        ): AiSyllabusDraft = AiSyllabusDraft(
            targetSyllabusId = targetSyllabusId,
            proposal = proposal,
            targetTitle = targetTitle,
            importedFileName = importedFileName,
            titleOverride = titleOverride,
            subjects = proposal.subjects.map { AiSyllabusDraftSubject.fromProposal(it) },
            warnings = proposal.warnings,
            ambiguities = proposal.ambiguities,
            sourceVersion = sourceVersion,
            sourcePromptVersion = sourcePromptVersion,
            sourceModelVersion = sourceModelVersion,
            sourceSchemaVersion = sourceSchemaVersion,
            sourceHash = sourceHash,
        )
    }
}

data class AiSyllabusDraftSubject(
    val name: String,
    val position: Int,
    val suggestedPriority: AiPriority,
    val topics: List<AiSyllabusDraftTopic>,
    val externalId: String,
    val sourcePages: List<Int>,
) {
    companion object {
        fun fromProposal(proposal: AiSubjectProposal): AiSyllabusDraftSubject {
            val externalId = AiSyllabusExternalIds.subject(proposal.name)
            return AiSyllabusDraftSubject(
                name = proposal.name,
                position = proposal.position,
                suggestedPriority = proposal.suggestedPriority,
                topics = proposal.topics.map { AiSyllabusDraftTopic.fromProposal(it, externalId) },
                externalId = externalId,
                sourcePages = proposal.sourcePages,
            )
        }
    }
}

data class AiSyllabusDraftTopic(
    val name: String,
    val position: Int,
    val externalId: String,
    val children: List<AiSyllabusDraftTopic> = emptyList(),
    val sourcePages: List<Int> = emptyList(),
) {
    companion object {
        fun fromProposal(proposal: AiTopicProposal, parentExternalId: String): AiSyllabusDraftTopic {
            val externalId = AiSyllabusExternalIds.topic(parentExternalId, proposal.name)
            return AiSyllabusDraftTopic(
                name = proposal.name,
                position = proposal.position,
                externalId = externalId,
                children = proposal.children.map { fromProposal(it, externalId) },
                sourcePages = proposal.sourcePages,
            )
        }
    }
}

internal object AiSyllabusExternalIds {
    fun subject(name: String): String = "ai-subject-${digest(canonical(name))}"

    fun topic(parentExternalId: String, name: String): String = "ai-topic-${digest("$parentExternalId|${canonical(name)}")}"

    private fun canonical(value: String): String = Normalizer.normalize(value.trim().lowercase(), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "")
        .replace(Regex("[^a-z0-9]+"), " ")
        .trim()

    private fun digest(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }
        .take(24)
}
