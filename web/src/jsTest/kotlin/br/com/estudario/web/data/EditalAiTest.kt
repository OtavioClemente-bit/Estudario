package br.com.estudario.web.data

import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EditalAiTest {
    @Test
    fun proposalBecomesSubjectsAndNestedTopicsInOrder() {
        val proposal = snapshotJson.parseToJsonElement(
            """{"documentTitle":"Edital TJXX","subjects":[
                 {"name":"Português","position":0,"topics":[{"name":"Crase","position":1,"children":[]},{"name":"Interpretação","position":0,"children":[]}]},
                 {"name":"Informática","position":1,"topics":[{"name":"Linguagens: Java e Python","position":0,"children":[{"name":"Java","position":0,"children":[]},{"name":"Python","position":1,"children":[]}]}]},
                 {"name":"Vazia","position":2,"topics":[]}]}""",
        ).jsonObject
        val nodes = EditalAi.parse(proposal)
        assertEquals(listOf("Português", "Informática"), nodes.map { it.name })
        assertEquals(listOf("Interpretação", "Crase"), nodes[0].topics.map { it.name })
        val (data, competitionId) = EditalAi.apply(Snapshot.empty(), "TJXX", nodes)
        assertEquals(2, data.subjects.count { it.competitionId == competitionId })
        val java = data.topics.single { it.title == "Java" }
        val parent = data.topics.single { it.title == "Linguagens: Java e Python" }
        assertEquals(parent.id, java.parentTopicId)
        assertEquals("DIDACTIC_SUBDIVISION", java.contentOriginType)
        assertTrue(data.competitions.single().primary)
        // Leaf topics for the plan: the parent groups, the children are studied.
        assertEquals(4, Queries.leafTopics(data).size)
    }
}
