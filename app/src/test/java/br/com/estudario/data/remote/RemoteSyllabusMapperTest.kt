package br.com.estudario.data.remote

import br.com.estudario.data.ai.AiPriority
import br.com.estudario.data.ai.AiSyllabusProposal
import br.com.estudario.data.ai.AiSubjectProposal
import br.com.estudario.data.ai.AiTopicProposal
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.domain.ai.AiSyllabusDraft
import br.com.estudario.domain.ai.AiSyllabusToEstudoMapper
import java.security.MessageDigest
import org.json.JSONObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class RemoteSyllabusMapperTest {
    @Test
    fun officialPackageRoundTripPreservesIdentityOrderParentsPrioritiesMetadataVersionsAndHash() {
        val draft = AiSyllabusDraft.fromProposal(
            targetSyllabusId = 41L,
            targetTitle = "Edital oficial",
            proposal = AiSyllabusProposal(
                schemaVersion = 1,
                promptVersion = "syllabus-v1",
                modelVersion = "gpt-6-luna",
                documentTitle = "Documento",
                subjects = listOf(
                    AiSubjectProposal(
                        name = "Constitucional",
                        position = 2,
                        suggestedPriority = AiPriority.HIGH,
                        topics = listOf(
                            AiTopicProposal("Direitos", 4, listOf(AiTopicProposal("Remédios", 1, emptyList(), emptyList())), emptyList()),
                        ),
                        sourcePages = listOf(42),
                    ),
                ),
                warnings = emptyList(),
                ambiguities = emptyList(),
            ),
        )
        val packageJson = JSONObject(AiSyllabusToEstudoMapper.toOfficialPackage(draft)).apply {
            getJSONArray("materias").getJSONObject(0).getJSONArray("topicos").getJSONObject(0)
                .put("prioridade", "BAIXA")
                .put("metadata", JSONObject().put("officialPriority", "original-metadata").put("marker", "topic"))
        }.toString()
        val payloadHash = sha256(packageJson)
        val remote = RemoteSyllabusMapper.fromLocal(
            CompetitionEntity(id = 41L, name = "Edital oficial", externalId = "competition-official"),
            packageJson,
            payloadHash,
        )
        val restored = RemoteSyllabusMapper.toOfficialPackage(remote)
        val restoredRemote = RemoteSyllabusMapper.fromLocal(
            CompetitionEntity(id = 41L, name = "Edital oficial", externalId = "competition-official", remoteSyllabusId = remote.remoteSyllabusId),
            restored,
            sha256(restored),
        )

        assertEquals(remote.remoteSyllabusId, restoredRemote.remoteSyllabusId)
        assertEquals(remote.subjects.map { it.externalId }, restoredRemote.subjects.map { it.externalId })
        assertEquals(remote.subjects.map { it.position }, restoredRemote.subjects.map { it.position })
        assertEquals(remote.subjects.single().suggestedPriority, restoredRemote.subjects.single().suggestedPriority)
        assertEquals(remote.subjects.single().topics.single().externalId, restoredRemote.subjects.single().topics.single().externalId)
        assertEquals(remote.subjects.single().topics.single().children.single().parentRemoteTopicId, restoredRemote.subjects.single().topics.single().children.single().parentRemoteTopicId)
        assertEquals(remote.subjects.single().metadata, restoredRemote.subjects.single().metadata)
        assertEquals(remote.subjects.single().topics.single().metadata, restoredRemote.subjects.single().topics.single().metadata)
        assertEquals(JsonPrimitive("original-metadata"), remote.subjects.single().topics.single().metadata["officialPriority"])
        assertEquals(JsonPrimitive("BAIXA"), remote.subjects.single().topics.single().metadata["__estudario_official_priority"])
        assertEquals("estudo-v2", remote.subjects.single().packageVersion)
        assertEquals(1, remote.schemaVersion)
        assertEquals(payloadHash, remote.metadata["payloadHash"]?.jsonPrimitive?.content)
        assertEquals(payloadHash, sha256(restored))
        assertNotEquals("Constitucional", remote.subjects.single().externalId)
    }

    @Test
    fun restoredTreeUsesExternalIdsAndParentsInsteadOfNamesOrReplacementIds() {
        val remote = PrivateSyllabus(
            remoteSyllabusId = "remote-fixed",
            title = "Edital",
            position = 0,
            visibility = PrivateSyllabusVisibility.PRIVATE,
            source = PrivateSyllabusSource.IMPORTED,
            sourceJobId = null,
            sourceHash = null,
            schemaVersion = 1,
            status = PrivateSyllabusStatus.ACTIVE,
            metadata = kotlinx.serialization.json.buildJsonObject { put("payloadHash", JsonPrimitive("a".repeat(64))) },
            subjects = listOf(
                PrivateSyllabusSubject("remote-subject", "subject-stable", "Nome", 0, AiPriority.NORMAL, "estudo-v2", 1, kotlinx.serialization.json.buildJsonObject { put("marker", JsonPrimitive("kept")) }, listOf(
                    PrivateSyllabusTopic("remote-topic", "topic-stable", null, "Nome do tópico", 0, "estudo-v2", 1, kotlinx.serialization.json.buildJsonObject { put("marker", JsonPrimitive("kept")) }, emptyList()),
                )),
            ),
        )
        val restored = RemoteSyllabusMapper.toOfficialPackage(remote)
        val root = JSONObject(restored)
        assertEquals("subject-stable", root.getJSONArray("materias").getJSONObject(0).getString("id"))
        assertEquals("topic-stable", root.getJSONArray("materias").getJSONObject(0).getJSONArray("topicos").getJSONObject(0).getString("id"))
        assertEquals("remote-fixed", root.getJSONObject("metadata").getString("remoteSyllabusId"))
    }

    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray())
        .joinToString("") { "%02x".format(it) }
}
