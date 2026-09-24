package br.com.estudario.domain.ai

import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.ai.AiWarning
import br.com.estudario.data.ai.AiWarningCode
import br.com.estudario.data.ai.AiWarningSeverity
import br.com.estudario.data.transfer.EstudoPackageParser
import br.com.estudario.data.transfer.EstudoPackageCodec
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiSyllabusToEstudoMapperTest {
    @Test fun `maps reviewed tree through official estudo parser preserving order parents and stable ids`() {
        val proposal = proposal()
        val draft = AiSyllabusDraft.fromProposal(42L, "Edital alvo", proposal, importedFileName = "arquivo-com-outro-nome.pdf")
        val first = AiSyllabusToEstudoMapper.toOfficialPackage(draft)
        val second = AiSyllabusToEstudoMapper.toOfficialPackage(draft)

        assertEquals(first, second)
        val parsed = EstudoPackageParser.parse(first)
        assertEquals("Edital alvo", parsed.competitionName)
        assertEquals(2, parsed.version)
        assertEquals(listOf(0, 1), parsed.subjects.sortedBy { it.position }.map { it.position })
        val root = JSONObject(first)
        val subjects = root.getJSONArray("materias")
        val firstSubject = subjects.getJSONObject(0)
        assertEquals("Matéria A", firstSubject.getString("nome"))
        assertEquals("Filho A", firstSubject.getJSONArray("topicos").getJSONObject(0).getJSONArray("subtopicos").getJSONObject(0).getString("titulo"))
        assertNotEquals("arquivo-com-outro-nome.pdf", root.getJSONObject("concurso").getString("nome"))
    }

    @Test fun `selected target id prevents imported file and model title from creating a second edital`() {
        val draft = AiSyllabusDraft.fromProposal(7L, "Edital já selecionado", proposal(documentTitle = "Edital do arquivo"), importedFileName = "edital-do-arquivo.estudo")

        val json = JSONObject(AiSyllabusToEstudoMapper.toOfficialPackage(draft))
        val competition = json.getJSONObject("concurso")

        assertEquals("7", competition.getString("id"))
        assertEquals("Edital já selecionado", competition.getString("nome"))
        assertEquals(2, json.getJSONArray("materias").length())
    }

    @Test fun `retains warning pages and package schema model and source versions`() {
        val warning = AiWarning(AiWarningCode.UNREADABLE_PAGES, AiWarningSeverity.WARNING, "Página ilegível", listOf(8, 9), "anexo")
        val draft = AiSyllabusDraft.fromProposal(7L, "Alvo", proposal(warnings = listOf(warning)), importedFileName = "fonte.pdf", sourceVersion = "pdf-source-v2")

        val root = JSONObject(AiSyllabusToEstudoMapper.toOfficialPackage(draft))
        val metadata = root.getJSONObject("metadata")
        val metadataWarning = metadata.getJSONArray("warnings").getJSONObject(0)

        assertEquals(2, root.getInt("version"))
        assertEquals(1, metadata.getInt("schemaVersion"))
        assertEquals("gpt-6-luna", metadata.getString("modelVersion"))
        assertEquals("pdf-source-v2", metadata.getString("sourceVersion"))
        assertEquals("fonte.pdf", metadata.getString("sourceFileName"))
        assertEquals(listOf(8, 9), (0 until metadataWarning.getJSONArray("sourcePages").length()).map { metadataWarning.getJSONArray("sourcePages").getInt(it) })
        assertTrue(metadata.getJSONArray("warnings").length() == 1)
    }

    @Test fun `official boundary round trip preserves package metadata hierarchy ids and source pages`() {
        val warning = AiWarning(AiWarningCode.UNREADABLE_PAGES, AiWarningSeverity.WARNING, "Página ilegível", listOf(8, 9), "anexo")
        val draft = AiSyllabusDraft.fromProposal(
            42L,
            "Edital alvo",
            proposal(warnings = listOf(warning)),
            importedFileName = "fonte.pdf",
            sourceVersion = "pdf-source-v2",
            sourceHash = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef",
        )
        val first = AiSyllabusToEstudoMapper.toOfficialPackage(draft)
        val parsed = EstudoPackageParser.parse(first)

        assertEquals(1, parsed.schemaVersion)
        assertEquals("estudo-v2", parsed.packageVersion)
        assertEquals(JSONObject(first).getJSONObject("metadata").toString(), parsed.metadata?.toString())
        assertEquals(listOf(warning.sourcePages), parsed.warnings.map { it.sourcePages })
        assertEquals(draft.subjects.sortedBy { it.position }.map { it.externalId }, parsed.subjects.sortedBy { it.position }.map { it.externalId })
        val expectedSubject = draft.subjects.single { it.position == 0 }
        val expectedRootTopic = expectedSubject.topics.single()
        val rootTopic = parsed.subjects.single { it.externalId == expectedSubject.externalId }.topics.single()
        assertEquals(expectedRootTopic.externalId, rootTopic.externalId)
        assertEquals(null, rootTopic.parentExternalId)
        assertEquals(listOf(2), rootTopic.sourcePages)
        assertEquals(expectedRootTopic.children.single().externalId, rootTopic.children.single().externalId)
        assertEquals(expectedRootTopic.externalId, rootTopic.children.single().parentExternalId)
        assertEquals(listOf(4), rootTopic.children.single().sourcePages)

        val restored = EstudoPackageParser.parse(EstudoPackageCodec.encode(parsed))
        assertEquals(parsed.schemaVersion, restored.schemaVersion)
        assertEquals(parsed.packageVersion, restored.packageVersion)
        assertEquals(parsed.metadata?.toString(), restored.metadata?.toString())
        assertEquals(parsed.warnings, restored.warnings)
        assertEquals(parsed.subjects.map { it.externalId }, restored.subjects.map { it.externalId })
        assertEquals(parsed.subjects.flatMap { flattenTopics(it.topics) }, restored.subjects.flatMap { flattenTopics(it.topics) })
    }

    @Test fun `selected target title wins over title override in official package`() {
        val draft = AiSyllabusDraft.fromProposal(7L, "Edital selecionado", proposal(documentTitle = "Edital do arquivo"))
            .copy(titleOverride = "Nome do modelo")

        val bound = AiSyllabusProposalValidator.bindToTarget(draft, 7L, "Edital selecionado")

        assertEquals("Edital selecionado", JSONObject(AiSyllabusToEstudoMapper.toOfficialPackage(bound)).getJSONObject("concurso").getString("nome"))
    }

    private fun flattenTopics(topics: List<br.com.estudario.data.transfer.TopicPlan>): List<String> = topics.flatMap { topic ->
        listOf("${topic.externalId}|${topic.parentExternalId}|${topic.sourcePages}") + flattenTopics(topic.children)
    }

    private fun proposal(documentTitle: String = "Edital detectado", warnings: List<AiWarning> = emptyList()) = AiSyllabusProposal(
        schemaVersion = 1,
        promptVersion = "syllabus-v1",
        modelVersion = "gpt-6-luna",
        documentTitle = documentTitle,
        subjects = listOf(
            AiSubjectProposal("Matéria B", 1, AiPriority.HIGH, listOf(AiTopicProposal("Tópico B", 2, emptyList(), listOf(3))), listOf(3)),
            AiSubjectProposal("Matéria A", 0, AiPriority.NORMAL, listOf(AiTopicProposal("Tópico A", 0, listOf(AiTopicProposal("Filho A", 0, emptyList(), listOf(4))), listOf(2))), listOf(2)),
        ),
        warnings = warnings,
        ambiguities = emptyList(),
    )
}
