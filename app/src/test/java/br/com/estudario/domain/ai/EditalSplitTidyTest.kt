package br.com.estudario.domain.ai

import br.com.estudario.data.ai.AiTopicProposal
import org.junit.Assert.assertEquals
import org.junit.Test

class EditalSplitTidyTest {
    private fun t(name: String, vararg children: AiTopicProposal, page: Int = 1) =
        AiTopicProposal(name, 0, children.mapIndexed { i, c -> c.copy(position = i) }, listOf(page))

    private fun draft(topic: AiTopicProposal) = AiSyllabusDraftTopic.fromProposal(topic, "ai-subject-x")

    @Test fun `leaf stays leaf`() {
        val d = draft(t("Significação contextual de palavras e expressões"))
        assertEquals("Significação contextual de palavras e expressões", d.name)
        assertEquals(0, d.children.size)
    }

    @Test fun `real split keeps parent literal and children`() {
        val d = draft(t("Linguagens: Java, Python", t("Java"), t("Python")))
        assertEquals("Linguagens: Java, Python", d.name)
        assertEquals(listOf("Java", "Python"), d.children.map { it.name })
    }

    @Test fun `single child repeating parent collapses into a leaf`() {
        val d = draft(t("Articulação textual", t("Articulação textual.", page = 3)))
        assertEquals("Articulação textual", d.name)
        assertEquals(0, d.children.size)
        assertEquals(listOf(1, 3), d.sourcePages)
    }

    @Test fun `single child with its own name stays`() {
        val d = draft(t("Lei 8.112/1990", t("Regime disciplinar")))
        assertEquals(listOf("Regime disciplinar"), d.children.map { it.name })
    }

    @Test fun `child taken from the parent terms is kept`() {
        val d = draft(t("DevOps: Git; JSON Web Tokens (JWT)", t("Git"), t("JSON Web Tokens (JWT)")))
        assertEquals(2, d.children.size)
    }

    @Test fun `child that only adds a prefix to the parent collapses`() {
        val d = draft(t("Articulação textual", t("Noções de articulação textual")))
        assertEquals(0, d.children.size)
    }

    @Test fun `echo child among siblings is removed and its children lifted`() {
        val d = draft(t("Redes", t("Redes", t("TCP"), t("UDP")), t("Wi-Fi")))
        assertEquals(listOf("TCP", "UDP", "Wi-Fi"), d.children.map { it.name })
        assertEquals(listOf(0, 1, 2), d.children.map { it.position })
    }
}
