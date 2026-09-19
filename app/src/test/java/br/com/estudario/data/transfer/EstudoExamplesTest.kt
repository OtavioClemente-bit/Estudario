package br.com.estudario.data.transfer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class EstudoExamplesTest {
    @Test fun completeSyllabusExampleIsValid() {
        val plan = EstudoPackageParser.parse(example("edital-completo-v2.estudo"))
        assertEquals(2, plan.version)
        assertEquals(3, plan.subjects.size)
        assertTrue(plan.subjects.any { subject -> subject.topics.any { it.children.isNotEmpty() } })
    }

    @Test fun topicContentExampleContainsBookSummaryAndQuestions() {
        val plan = EstudoPackageParser.parse(example("conteudo-controle-acesso-v2.estudo"))
        val target = plan.subjects.single().topics.single().children.single()
        assertEquals(1, target.theories.size)
        assertTrue(target.theories.single().markdown.contains("## 1. Fundamentos"))
        assertEquals(1, target.summaries.size)
        assertEquals(2, target.questions.size)
    }

    @Test fun flatV2ExampleContainsEveryLearningLayer() {
        val topic = EstudoPackageParser.parse(example("conteudo-hash-v2-completo.estudo")).subjects.single().topics.single()
        assertEquals(1, topic.theories.size)
        assertEquals(2, topic.summaries.size)
        assertEquals(6, topic.snippets.size)
        assertEquals(1, topic.questions.size)
        assertEquals(1, topic.errorConcepts.size)
    }

    private fun example(name: String): String {
        val candidates = listOf(File("examples/$name"), File("../examples/$name"))
        return candidates.firstOrNull { it.isFile }?.readText() ?: error("Exemplo não encontrado: $name")
    }
}
