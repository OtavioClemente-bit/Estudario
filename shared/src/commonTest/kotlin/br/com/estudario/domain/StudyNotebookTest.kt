package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StudyNotebookTest {
    private val day = 86_400_000L
    private val now = 1_800_000_000_000L

    @Test
    fun resumoEmBlocoViraFrasesCurtas() {
        val crase = "A crase é a fusão de duas vogais idênticas (a + a), indicada na escrita pelo acento grave (à). Ocorre quando a preposição a, exigida por um termo regente, encontra o artigo definido feminino a/as. Para decidir, verifica-se primeiro a regência do termo anterior. O teste do masculino confirma a análise."
        val lines = StudyNotebook.summaryLines(crase)
        assertEquals(4, lines.size)
        assertTrue(lines.all { it.length <= 220 })
        assertTrue(lines.first().startsWith("A crase"))
    }

    @Test
    fun tituloEMarcacaoSaem() {
        val lines = StudyNotebook.summaryLines("## Atos\n\n- **Presunção** de legitimidade vale até prova em contrário.\n- Autoexecutoriedade não está em todos os atos.")
        assertEquals(2, lines.size)
        assertTrue(lines.none { it.startsWith("#") || it.startsWith("-") })
    }

    @Test
    fun folheadaPegaOQueEsfriaEDaPessoaPrimeiro() {
        val cold = StudyNotebook.Topic(1, 1, "Crase", "ESTUDADO", now - 40 * day,
            summary = "A crase é a fusão de duas vogais idênticas. Ocorre com a preposição exigida pelo termo regente e o artigo feminino. O teste do masculino confirma.",
            traps = listOf("Antes de palavra masculina não há crase: a pé, a cavalo."),
            recall = listOf("Quando ocorre crase?" to "Quando a preposição a encontra o artigo feminino a.", "x?" to "curta"),
            marks = listOf(StudyNotebook.Mark("Antes de verbo não há crase.", "cai muito", now - 30 * day)),
            wrongQuestions = listOf(77L))
        val fresh = StudyNotebook.Topic(2, 1, "Regência", "REVISANDO", now - 2 * day, tips = listOf("Comece pela regência do verbo, nunca pelo gênero."))
        val pages = StudyNotebook.folheada(listOf(fresh, cold), now, daySeed = 20261026)
        assertEquals(5, pages.size)
        assertTrue(pages.last() is StudyNotebook.Page.Question)
        assertTrue(pages.any { it is StudyNotebook.Page.MarkPage })
        // o cartão de resposta "curta" não passa no mínimo
        assertTrue(pages.filterIsInstance<StudyNotebook.Page.Recall>().none { it.answer == "curta" })
        // com só 2 tópicos estudados, o que esfria pode dar mais de duas páginas
        assertTrue(pages.count { it.topicId == 1L } >= 3)
    }

    @Test
    fun semEstudoNaoTemFolheada() {
        assertTrue(StudyNotebook.folheada(listOf(StudyNotebook.Topic(1, 1, "X", "NAO_ESTUDADO", null)), now, 1).isEmpty())
    }

    @Test
    fun firmeza() {
        assertEquals(StudyNotebook.Firmness.COLD, StudyNotebook.firmness(StudyNotebook.Topic(1, 1, "a", "ESTUDADO", now - 40 * day), now))
        assertEquals(StudyNotebook.Firmness.FIRM, StudyNotebook.firmness(StudyNotebook.Topic(1, 1, "a", "REVISANDO", now - 3 * day), now))
        assertEquals(StudyNotebook.Firmness.MID, StudyNotebook.firmness(StudyNotebook.Topic(1, 1, "a", "ESTUDADO", now - 20 * day), now))
    }
}
