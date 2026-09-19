package br.com.estudario.data.transfer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import br.com.estudario.data.local.QuestionSourceType
import br.com.estudario.data.local.ContentOriginType

class EstudoPackageParserTest {
    @Test fun versionTwoPreservesHierarchyWithoutAttributingDefaultsToAuthorialQuestions() {
        val plan = EstudoPackageParser.parse(validV2)
        assertEquals(2, plan.version)
        assertEquals("Concurso Teste", plan.competitionName)
        assertEquals(1, plan.subjects.size)
        assertEquals("Tópico", plan.subjects.single().topics.single().title)
        val child = plan.subjects.single().topics.single().children.single()
        assertEquals("Subtópico", child.title)
        assertEquals(1, child.theories.size)
        assertTrue(child.theories.single().markdown.contains("## Capítulo 1"))
        assertNull(child.questions.single().board)
        assertNull(child.questions.single().year)
    }

    @Test fun realQuestionWithProvenanceCanInheritPackageDefaults() {
        val sourced = validV2.replace(
            "\"id\":\"q1\"",
            "\"id\":\"q1\",\"questionSourceType\":\"REAL\",\"sourceId\":\"Q-1\",\"sourceUrl\":\"https://example.org/prova\"",
        )
        val question = EstudoPackageParser.parse(sourced)
            .subjects.single().topics.single().children.single().questions.single()

        assertEquals("Banca X", question.board)
        assertEquals(2026, question.year)
    }

    @Test fun rejectsQuestionWithoutExactlyOneCorrectOption() {
        val invalid = validV2.replace("\"correta\":true", "\"correta\":false")
        val error = runCatching { EstudoPackageParser.parse(invalid) }.exceptionOrNull()
        assertTrue(error is EstudoPackageException)
        assertTrue(error?.message?.contains("exatamente uma") == true)
    }

    @Test fun versionOneRemainsCompatible() {
        val plan = EstudoPackageParser.parse("""{"version":1,"packageId":"legacy","concurso":"C","materia":"M","topico":"T","questoes":[]}""")
        assertEquals(1, plan.version)
        assertEquals("T", plan.subjects.single().topics.single().title)
    }

    @Test fun flatVersionTwoReadsAllLearningLayers() {
        val plan = EstudoPackageParser.parse("""{
          "version":2,"packageId":"flat-test","competition":"C","subject":"M","topic":"T",
          "summary":"Resumo completo","quickReview":"Revisão rápida",
          "tips":["Bizu"],"traps":["Pegadinha"],"activeRecall":["O que é?"],
          "questions":[],"errorConcepts":[{"id":"e1","title":"Confusão","summary":"Correção"}]
        }""")
        val topic = plan.subjects.single().topics.single()
        assertEquals(2, topic.summaries.size)
        assertEquals(3, topic.snippets.size)
        assertEquals(1, topic.errorConcepts.size)
    }

    @Test fun questionWithoutExplicitOriginIsAlwaysAuthorial() {
        val plan = EstudoPackageParser.parse(validV2)
        assertEquals(QuestionSourceType.AUTHORIAL, plan.subjects.single().topics.single().children.single().questions.single().sourceType)
    }

    @Test fun readsExplicitDidacticAndAdaptedOrigins() {
        val changed = validV2
            .replace("\"titulo\":\"Subtópico\"", "\"titulo\":\"Subtópico\",\"contentOriginType\":\"DIDACTIC_SUBDIVISION\"")
            .replace("\"id\":\"q1\"", "\"id\":\"q1\",\"questionSourceType\":\"REAL_ADAPTED\",\"sourceId\":\"Q919874\"")
        val child = EstudoPackageParser.parse(changed).subjects.single().topics.single().children.single()
        assertEquals(ContentOriginType.DIDACTIC_SUBDIVISION, child.originType)
        assertEquals(QuestionSourceType.REAL_ADAPTED, child.questions.single().sourceType)
        assertEquals("Q919874", child.questions.single().sourceId)
    }

    private val validV2 = """
        {
          "version":2,
          "packageId":"teste-v2",
          "concurso":{"nome":"Concurso Teste","principal":true},
          "padroesQuestao":{"banca":"Banca X","ano":2026},
          "materias":[{
            "id":"materia","nome":"Matéria","ordem":0,
            "topicos":[{
              "id":"topico","titulo":"Tópico","ordem":0,
              "subtopicos":[{
                "id":"subtopico","titulo":"Subtópico","ordem":0,
                "teorias":[{"id":"teoria","titulo":"Livro","capitulos":[{"id":"cap-1","titulo":"Capítulo 1","markdown":"Texto longo."}]}],
                "resumos":[{"id":"resumo","titulo":"R","markdown":"# Conteúdo"}],
                "questoes":[{
                  "id":"q1","enunciado":"Pergunta?","explicacao":"Porque sim.",
                  "alternativas":[
                    {"chave":"A","texto":"Certa","correta":true},
                    {"chave":"B","texto":"Errada","correta":false}
                  ]
                }]
              }]
            }]
          }]
        }
    """.trimIndent()
}
