package br.com.estudario.domain.ai

import br.com.estudario.data.ai.*
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.prompt.ContentPromptOptions
import br.com.estudario.data.transfer.EstudoPackageCodec
import br.com.estudario.data.transfer.EstudoPackageParser
import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.*
import org.junit.Test

class AiLongTopicRegressionTest {
    private val literal = listOf(File("supabase/functions/_shared/fixtures/v1/long-topic-name.txt"), File("../supabase/functions/_shared/fixtures/v1/long-topic-name.txt"))
        .first { it.isFile }.readText().trimEnd()
    private val children = listOf(
        "principais técnicas de pré-processamento de dados estruturados e não estruturados",
        "conceitos de modelos preditivos (supervisionados) e descritivos (não supervisionados)",
        "avaliação de modelos (sobreajuste, métricas de classificação e regressão, análise ROC)",
        "ferramentas de desenvolvimento de aplicações de aprendizado de máquina (scikit-learn, keras e pytorch)",
    ).mapIndexed { index, name -> AiTopicProposal(name, index, emptyList(), listOf(2)) }
    private fun proposal(name: String = literal) = AiSyllabusProposal(1, "syllabus-v1", "model", "Edital", listOf(
        AiSubjectProposal("Tecnologia da Informação", 0, AiPriority.NORMAL, listOf(AiTopicProposal(name, 0, children, listOf(2))), listOf(2)),
    ), emptyList(), emptyList())

    @Test fun literalParentSurvivesDtoDraftMapperAndEstudoRoundTrip() {
        assertEquals(430, literal.length)
        assertEquals(446, literal.toByteArray(Charsets.UTF_8).size)
        val decoded = EstudarioContractJson.decodeProposal(EstudarioContractJson.json.encodeToString(proposal()))
        assertEquals(literal, decoded.subjects.single().topics.single().name)
        val draft = AiSyllabusDraft.fromProposal(1, "TRT 3 REGIAO", decoded)
        AiSyllabusProposalValidator.validateDraft(draft)
        val parsed = EstudoPackageParser.parse(AiSyllabusToEstudoMapper.toOfficialPackage(draft))
        val restored = EstudoPackageParser.parse(EstudoPackageCodec.encode(parsed))
        val parent = restored.subjects.single().topics.single()
        assertEquals(literal, parent.title)
        assertEquals(children.map { it.name }, parent.children.map { it.title })
        assertTrue(parent.theories.isEmpty())
    }

    @Test fun longTopicKeepsSafetyChecksAndShortSubjectLimit() {
        for (invalid in listOf("", " \n ", "bad\u0000text", "bad\u0085text", "x".repeat(4_001))) {
            assertThrows(ContractValidationException::class.java) { EstudarioContractJson.decodeProposal(EstudarioContractJson.json.encodeToString(proposal(invalid))) }
            assertThrows(AiSyllabusDraftValidationException::class.java) { AiSyllabusProposalValidator.validateDraft(AiSyllabusDraft.fromProposal(1, "TRT", proposal(invalid))) }
        }
        AiSyllabusProposalValidator.validateDraft(AiSyllabusDraft.fromProposal(1, "TRT", proposal("x".repeat(4_000))))
        val tooLongSubject = proposal().copy(subjects = listOf(proposal().subjects.single().copy(name = "x".repeat(201))))
        assertThrows(AiSyllabusDraftValidationException::class.java) { AiSyllabusProposalValidator.validateDraft(AiSyllabusDraft.fromProposal(1, "TRT", tooLongSubject)) }
    }

    @Test fun contentRequestsKeepFullParentAndChildScopeWithoutEightHundredCharacterCut() {
        val competition = CompetitionEntity(id = 1, name = "TRT")
        val subject = SubjectEntity(id = 2, competitionId = 1, name = "TI")
        for (name in listOf(literal, "x".repeat(4_000))) {
            val parent = TopicEntity(id = 3, subjectId = 2, title = name)
            val child = TopicEntity(id = 4, subjectId = 2, title = children.first().name, parentTopicId = 3)
            val topics = listOf(parent, child)
            val input = AiContentRequest.input(competition, subject, topics, child, ContentPromptOptions())
            assertEquals(name, input.getJSONArray("topicPath").getString(0))
            assertEquals(child.title, input.getJSONArray("topicPath").getString(1))
            val alternate = TopicContentAiInput.build(competition, null, subject, child, topics)
            assertEquals(name, alternate.getValue("topicPath").jsonArray.first().jsonPrimitive.content)
        }
    }

    @Test fun warningsAndSourceTitlesUseTheirOwnLimitsAtDtoAndDraftBoundaries() {
        val warning = AiWarning(AiWarningCode.AMBIGUOUS_STRUCTURE, AiWarningSeverity.WARNING, "m".repeat(8_000), listOf(2), "a".repeat(8_000))
        val valid = proposal().copy(documentTitle = "d".repeat(4_000), warnings = listOf(warning), ambiguities = listOf(warning.ambiguity!!))
        val decoded = EstudarioContractJson.decodeProposal(EstudarioContractJson.json.encodeToString(valid))
        AiSyllabusProposalValidator.validateDraft(AiSyllabusDraft.fromProposal(1, "TRT", decoded))
        for (invalid in listOf("", "bad\u0000text", "x".repeat(8_001))) {
            for (value in listOf(valid.copy(warnings = listOf(warning.copy(message = invalid))), valid.copy(warnings = listOf(warning.copy(ambiguity = invalid))))) {
                assertThrows(ContractValidationException::class.java) { EstudarioContractJson.decodeProposal(EstudarioContractJson.json.encodeToString(value)) }
                assertThrows(AiSyllabusDraftValidationException::class.java) { AiSyllabusProposalValidator.validateDraft(AiSyllabusDraft.fromProposal(1, "TRT", value)) }
            }
        }
    }
}
