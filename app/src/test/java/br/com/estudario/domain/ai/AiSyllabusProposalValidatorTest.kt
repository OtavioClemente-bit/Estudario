package br.com.estudario.domain.ai

import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.ai.AiWarning
import br.com.estudario.data.ai.AiWarningCode
import br.com.estudario.data.ai.AiWarningSeverity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiSyllabusProposalValidatorTest {
    @Test fun `rejects blank and control-character names`() {
        val draft = draft(subjects = listOf(subject(name = "\u0000")))

        val error = runCatching { AiSyllabusProposalValidator.validateDraft(draft) }.exceptionOrNull()

        assertTrue(error is AiSyllabusDraftValidationException)
        assertTrue(error?.message?.contains("name") == true)
    }

    @Test fun `rejects duplicate external ids even when names differ`() {
        val draft = draft(subjects = listOf(subject(name = "Direito", position = 0, externalId = "same"), subject(name = "Administração", position = 1, externalId = "same")))

        val error = runCatching { AiSyllabusProposalValidator.validateDraft(draft) }.exceptionOrNull()

        assertTrue(error is AiSyllabusDraftValidationException)
        assertTrue(error?.message?.contains("externalId") == true)
    }

    @Test fun `rejects duplicate sibling names and positions`() {
        val draft = draft(subjects = listOf(subject(name = "Direito", position = 0), subject(name = " direito ", position = 1)))

        val error = runCatching { AiSyllabusProposalValidator.validateDraft(draft) }.exceptionOrNull()

        assertTrue(error is AiSyllabusDraftValidationException)
        assertTrue(error?.message?.contains("sibling") == true)
    }

    @Test fun `rejects duplicate topic external ids globally`() {
        val duplicated = subject().copy(
            topics = listOf(
                topic(name = "Constituição", externalId = "topic-same"),
                topic(name = "Administração", position = 1, externalId = "topic-same"),
            ),
        )

        val error = runCatching { AiSyllabusProposalValidator.validateDraft(draft(subjects = listOf(duplicated))) }.exceptionOrNull()

        assertTrue(error is AiSyllabusDraftValidationException)
        assertTrue(error?.message?.contains("duplicate externalId") == true)
    }

    @Test fun `rejects cyclic trees and depth overflow`() {
        val cyclicChildren = mutableListOf<AiSyllabusDraftTopic>()
        val cyclic = AiSyllabusDraftTopic(name = "Ciclo", position = 0, externalId = "cycle", children = cyclicChildren, sourcePages = listOf(1))
        cyclicChildren += cyclic
        val cycleError = runCatching { AiSyllabusProposalValidator.validateDraft(draft(topic = cyclic)) }.exceptionOrNull()
        assertTrue(cycleError is AiSyllabusDraftValidationException)
        assertTrue("message=${cycleError?.message}", cycleError?.message?.lowercase()?.contains("cycle") == true)

        var deep = AiSyllabusDraftTopic(name = "Nível", position = 0, externalId = "deep-0", sourcePages = listOf(1))
        repeat(AiSyllabusProposalValidator.MAX_TOPIC_DEPTH) { depth ->
            deep = AiSyllabusDraftTopic(name = "Nível", position = 0, externalId = "deep-${depth + 1}", children = listOf(deep), sourcePages = listOf(1))
        }
        val depthError = runCatching { AiSyllabusProposalValidator.validateDraft(draft(topic = deep)) }.exceptionOrNull()
        assertTrue(depthError is AiSyllabusDraftValidationException)
        assertTrue(depthError?.message?.contains("depth") == true)
    }

    @Test fun `retains warnings and rejects source model mismatch`() {
        val warning = AiWarning(AiWarningCode.DOCUMENT_MISMATCH, AiWarningSeverity.WARNING, "Outro edital", listOf(18, 19), "capa")
        val proposal = proposal(warnings = listOf(warning))
        val draft = AiSyllabusDraft.fromProposal(7L, "Alvo", proposal, sourceVersion = "pdf-v1", sourceModelVersion = "gpt-other")

        val error = runCatching { AiSyllabusProposalValidator.validateDraft(draft) }.exceptionOrNull()

        assertTrue(error is AiSyllabusDraftValidationException)
        assertTrue(error?.message?.contains("modelVersion") == true)
        assertEquals(listOf(warning), draft.warnings)
    }

    @Test fun `rejects prompt and schema mismatches at the draft boundary`() {
        val proposal = proposal()
        val promptError = runCatching {
            AiSyllabusProposalValidator.validateDraft(
                AiSyllabusDraft.fromProposal(7L, "Alvo", proposal, sourcePromptVersion = "prompt-outdated"),
            )
        }.exceptionOrNull()
        val schemaError = runCatching {
            AiSyllabusProposalValidator.validateDraft(
                AiSyllabusDraft.fromProposal(7L, "Alvo", proposal, sourceSchemaVersion = 99),
            )
        }.exceptionOrNull()

        assertTrue(promptError is AiSyllabusDraftValidationException)
        assertTrue(promptError?.message?.contains("sourcePromptVersion") == true)
        assertTrue(schemaError is AiSyllabusDraftValidationException)
        assertTrue(schemaError?.message?.contains("sourceSchemaVersion") == true)
    }

    @Test fun `bindToTarget makes selected id and title authoritative`() {
        val draft = AiSyllabusDraft.fromProposal(7L, "Nome antigo", proposal(documentTitle = "Nome detectado"), importedFileName = "Nome detectado.estudo")
            .copy(titleOverride = "Nome do modelo")

        val bound = AiSyllabusProposalValidator.bindToTarget(draft, targetSyllabusId = 99L, targetTitle = "Edital selecionado")

        assertEquals(99L, bound.targetSyllabusId)
        assertEquals("Edital selecionado", bound.targetTitle)
        assertEquals(null, bound.titleOverride)
        assertEquals("Nome detectado", bound.proposal.documentTitle)
        assertEquals("Nome detectado.estudo", bound.importedFileName)
    }

    private fun draft(
        subjects: List<AiSyllabusDraftSubject> = listOf(subject()),
        topic: AiSyllabusDraftTopic? = null,
    ): AiSyllabusDraft {
        val proposal = proposal()
        return AiSyllabusDraft.fromProposal(7L, "Alvo", proposal).copy(
            subjects = if (topic == null) subjects else listOf(subject().copy(topics = listOf(topic))),
        )
    }

    private fun subject(name: String = "Direito", position: Int = 0, externalId: String = "subject-$position") =
        AiSyllabusDraftSubject(name, position, AiPriority.NORMAL, listOf(topic()), externalId, listOf(1))

    private fun topic(name: String = "Constituição", position: Int = 0, externalId: String = "topic-$position", children: List<AiSyllabusDraftTopic> = emptyList()) =
        AiSyllabusDraftTopic(name, position, externalId, children, listOf(1))

    private fun proposal(
        documentTitle: String = "Edital detectado",
        subjects: List<AiSubjectProposal> = listOf(AiSubjectProposal("Direito", 0, AiPriority.NORMAL, listOf(AiTopicProposal("Constituição", 0, emptyList(), listOf(1))), listOf(1))),
        warnings: List<AiWarning> = emptyList(),
    ) = AiSyllabusProposal(1, "syllabus-v1", "gpt-6-luna", documentTitle, subjects, warnings, emptyList())
}
