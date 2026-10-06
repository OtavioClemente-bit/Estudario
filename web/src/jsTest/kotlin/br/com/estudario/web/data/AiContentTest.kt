package br.com.estudario.web.data

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Gravação do material da IA no site, com as regras do importador do app. */
class AiContentTest {
    private fun proposal(version: String) = snapshotJson.parseToJsonElement(
        """
        {"theoryTitle":"Crase $version","chapters":[{"title":"Regra geral","markdown":"A crase é a fusão. $version"}],
         "summary":"Resumo $version","flashcards":[{"front":"O que é crase?","back":"Fusão de a + a."}],
         "tips":["Troque por ao"],"traps":["Antes de verbo não há crase"],"activeRecall":[{"question":"Quando usar?","answer":"Fusão"}],
         "errorConcepts":[{"key":"e1","title":"Crase antes de masculino","summary":"Não ocorre"}],
         "questions":[
           {"statement":"Questão A $version","format":"MULTIPLE_CHOICE","difficulty":"MEDIA","options":[{"key":"A","text":"x","correct":true},{"key":"B","text":"y","correct":false}],"explanation":"porque","section":"Regra geral","errorConceptKey":"e1","sourceType":"REAL","board":"FGV","agency":"TJ","year":2024,"sourceUrl":null},
           {"statement":"Questão B $version","format":"TRUE_FALSE","difficulty":"FACIL","options":[{"key":"C","text":"Certo","correct":false},{"key":"E","text":"Errado","correct":true}],"explanation":"porque","section":"Questões","errorConceptKey":null,"sourceType":"AUTHORIAL","board":null,"agency":null,"year":null,"sourceUrl":null}
         ],
         "sources":[{"kind":"OFICIAL","title":"Gramática","publisher":"X","reference":"cap. 1","url":"https://exemplo.br","accessedAt":"2026-10-06"}],
         "scope":{"covers":"Crase","excludes":"Regência"}}
        """.trimIndent(),
    ).jsonObject

    private fun withTopic(): Pair<Snapshot, Long> {
        val (data, _) = Catalog.apply(Snapshot.empty(), CatalogEntry("x", "TJXX", "Tribunal", "Analista"), listOf(CatalogSubject("Português", listOf("Crase"))))
        return data to data.topics.single().id
    }

    @Test
    fun generatedMaterialIsSavedLikeTheAppImport() {
        val (data, topicId) = withTopic()
        val saved = AiContent.apply(data, topicId, proposal("v1"))
        val theory = saved.theories.single()
        assertTrue(theory.markdown.startsWith("# Crase v1"))
        assertTrue("## Regra geral" in theory.markdown)
        assertEquals(setOf("COMPLETO", "RAPIDO"), saved.summaries.map { it.kind }.toSet())
        assertEquals(listOf("BIZU", "PEGADINHA", "RECUPERACAO"), saved.snippets.map { it.kind })
        assertEquals(2, saved.questions.size)
        // "REAL" sem link vira autoral e perde banca/órgão/ano.
        val first = saved.questions.first { it.statement.startsWith("Questão A") }
        assertEquals(null, first.board)
        assertEquals("topico-$topicId-erro-e1", first.errorConceptExternalId)
        assertEquals("Regra geral", first.reviewAnchor)
        assertEquals(1, saved.errorConcepts.size)
        assertEquals("Crase", saved.topics.single().let { (saved.array(Keys.TOPICS).single() as JsonObject).str("scopeCovers") })
        // A foto continua no formato do backup.
        assertEquals(2, Snapshot.parse(saved.encode()).questions.size)
    }

    @Test
    fun regeneratingReplacesPreviousMaterialButKeepsAnsweredQuestionsHidden() {
        val (data, topicId) = withTopic()
        val first = AiContent.apply(data, topicId, proposal("v1"))
        val answeredId = first.questions.first().id
        val (answered, _) = Actions.answer(first, answeredId, "A")
        val second = AiContent.apply(answered, topicId, proposal("v2"))
        assertEquals(1, second.theories.size)
        assertTrue(second.theories.single().markdown.contains("v2"))
        assertEquals(2, second.summaries.size)
        val old = second.questions.single { it.id == answeredId }
        assertTrue(old.hidden, "questão respondida fica oculta, não some")
        assertEquals(2, second.questions.count { !it.hidden })
        assertEquals(1, second.attempts.size)
    }
}
