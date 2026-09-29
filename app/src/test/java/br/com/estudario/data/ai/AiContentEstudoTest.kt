package br.com.estudario.data.ai

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.prompt.ContentBlock
import br.com.estudario.data.prompt.ContentPromptOptions
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.data.prompt.QuestionDifficulty
import br.com.estudario.data.prompt.QuestionStyle
import br.com.estudario.data.transfer.EstudoPackageParser
import br.com.estudario.data.transfer.allTopics
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AiContentEstudoTest {
    private val competition = CompetitionEntity(id = 1, name = "TRT-3")
    private val subject = SubjectEntity(id = 2, competitionId = 1, name = "Português")
    private val parent = TopicEntity(id = 3, subjectId = 2, title = "Sintaxe")
    private val target = TopicEntity(id = 4, subjectId = 2, title = "Crase", parentTopicId = 3)
    private val topics = listOf(parent, target)

    private val proposal = JSONObject(
        """
        {"schemaVersion":2,"promptVersion":"topic-content-v2","modelVersion":"m",
         "scope":{"covers":"Uso da crase","excludes":"Regência"},
         "theoryTitle":"Crase",
         "chapters":[{"title":"1. Fundamentos","markdown":"Texto"},{"title":"2. Casos","markdown":"Mais"}],
         "summary":"Resumo","quickReview":"Rápida","tips":["Dica"],"traps":[],"activeRecall":[],
         "errorConcepts":[{"key":"e1","title":"Masculino","summary":"Não ocorre"}],
         "questions":[{"statement":"Julgue.","format":"TRUE_FALSE","difficulty":"MEDIA",
           "options":[{"key":"C","text":"Certo","correct":true},{"key":"E","text":"Errado","correct":false}],
           "explanation":"Porque sim.","section":"1. Fundamentos","errorConceptKey":"e1",
           "sourceType":"AUTHORIAL","board":null,"agency":null,"year":null,"sourceUrl":null}],
         "sources":[{"kind":"OFICIAL","title":"Gramática","publisher":"X","reference":"","url":"https://example.org","accessedAt":"2026-09-29"}],
         "warnings":[]}
        """.trimIndent(),
    )

    @Test
    fun resultadoDoServidorViraPacoteQueOImportadorAceitaNoTopicoCerto() {
        val plan = EstudoPackageParser.parse(AiContentEstudo.build(competition, subject, topics, target, proposal))
        assertEquals(PromptIds.competition(competition), plan.competitionId)
        val imported = plan.allTopics().single { it.id == PromptIds.topic(target) }
        assertEquals(1, imported.theories.size)
        assertTrue(imported.theories.single().markdown.contains("## 2. Casos"))
        assertEquals(2, imported.summaries.size)
        val question = imported.questions.single()
        assertEquals(listOf("C", "E"), question.options.map { it.key })
        assertNull(question.board)
        assertEquals(1, imported.sources.size)
        // O tópico pai vem só como estrutura, sem conteúdo.
        assertTrue(plan.allTopics().single { it.id == PromptIds.topic(parent) }.questions.isEmpty())
    }

    @Test
    fun pedidoLevaCaminhoDoTopicoEAsEscolhasDaPessoa() {
        val options = ContentPromptOptions(blocks = setOf(ContentBlock.QUESTIONS), questionCount = 15, difficulty = QuestionDifficulty.HARD, style = QuestionStyle.TRUE_FALSE)
        val input = AiContentRequest.input(competition, subject, topics, target, options)
        assertEquals(listOf("Sintaxe", "Crase"), (0 until input.getJSONArray("topicPath").length()).map { input.getJSONArray("topicPath").getString(it) })
        assertEquals("TRT-3", input.getString("agency"))
        val sent = input.getJSONObject("options")
        assertEquals(15, sent.getInt("questionCount"))
        assertEquals("HARD", sent.getString("difficulty"))
        assertEquals("TRUE_FALSE", sent.getString("questionStyle"))
        assertEquals(listOf("QUESTIONS"), (0 until sent.getJSONArray("blocks").length()).map { sent.getJSONArray("blocks").getString(it) })
    }
}
