package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NotebookPageMapTest {
    private val summary = """
        ## Balanço e resultado: revisão essencial

        **BP = posição em uma data; DRE = desempenho de um período.** A equação é ${'$'}${'$'}A = P + PL${'$'}${'$'}. O regime de competência apropria receitas e despesas pelos fatos econômicos.

        ### Composição do BP
        - **Realizável a longo prazo:** direitos após o exercício seguinte, indicados no art. 179, II, da lei societária.
        - **Passivo circulante:** obrigações exigíveis em até doze meses depois da data do balanço.

        ### DRE
        **Receita líquida = receita bruta menos deduções pertinentes.** **Lucro bruto = receita líquida menos o custo das vendas do período.**
    """.trimIndent()

    private val theory = listOf(
        """
        ### Remédios constitucionais
        | Instrumento | Protege |
        | --- | --- |
        | Habeas corpus | Liberdade de locomoção |
        | Habeas data | Dados pessoais em registros |

        ### Como cai na prova
        - Aviso prévio de reunião não significa pedido de autorização.
        - A ação popular é proposta por cidadão; pessoa jurídica não tem legitimidade.
        """.trimIndent(),
    )

    @Test fun summaryBecomesPartsWithTitles() {
        val map = NotebookPageMap.build(summary, theory, emptyList(), emptyList(), emptyList())
        assertEquals(3, map.blocks.size)
        assertNull(map.blocks[0].title)
        assertEquals("Composição do BP", map.blocks[1].title)
        val all = map.blocks.flatMap { it.lines }
        assertTrue(all.none { "$$" in it }, all.joinToString(" | "))
        assertTrue(all.any { "art. 179, II" in it }, all.joinToString(" | "))
        assertTrue(all.none { "Receita líquida" in it && "Lucro bruto" in it }, all.joinToString(" | "))
    }

    @Test fun examAndTablesComeFromTheTheory() {
        val map = NotebookPageMap.build(summary, theory, emptyList(), emptyList(), emptyList())
        assertEquals(2, map.exam.size)
        assertEquals(1, map.tables.size)
        assertEquals("Remédios constitucionais", map.tables[0].title)
    }

    @Test fun allTrapsAndNoRepeatedCards() {
        val traps = (1..6).map { "Pegadinha número $it sobre um assunto diferente de prova, com detalhe $it." }
        val recall = listOf(
            "Aviso prévio de reunião é pedido de autorização?" to "Não: aviso prévio de reunião não significa pedido de autorização.",
            "O que protege o habeas data?" to "Dados pessoais em registros públicos.",
        )
        val map = NotebookPageMap.build(summary, theory, emptyList(), traps, recall)
        assertEquals(6, map.traps.size)
        assertEquals(2, map.cards.size)
    }
}
