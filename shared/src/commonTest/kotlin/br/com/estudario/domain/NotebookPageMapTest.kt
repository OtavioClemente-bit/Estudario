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
        assertEquals(2, map.examCount)
        assertEquals(1, map.tables.size)
        assertEquals("Remédios constitucionais", map.tables[0].title)
    }

    @Test fun formulasBecomeReadable() {
        assertEquals("C(n,p)=n!/(p!(n-p)!)", Formula.readable("""C_{n,p}=\frac{n!}{p!(n-p)!}"""))
        assertEquals("x=x_0+v·t", Formula.readable("""x=x_0+v\cdot t"""))
        assertEquals("√(a^2+b^2)", Formula.readable("""\sqrt{a^{2}+b^{2}}"""))
        val map = NotebookPageMap.build(summary, theory, emptyList(), emptyList(), emptyList())
        assertTrue("A = P + PL" in map.formulas, map.formulas.toString())
    }

    @Test fun cutSentencesAreJoinedBack() {
        val joined = StudyNotebook.joinFragments(listOf("Restrições: zero não inicia número;", "não adjacentes vão por lacunas;", "Outra frase completa aqui."))
        assertEquals(2, joined.size, joined.toString())
        assertTrue(joined[0].endsWith("lacunas;"), joined.toString())
    }

    @Test fun summaryWithoutTitlesFollowsTheChapters() {
        val chapters = listOf(
            "## 1. Nacionalidade\n\nBrasileiro nato, naturalizado, perda da nacionalidade e cargos privativos de nato.",
            "## 2. Direitos políticos\n\nVoto, alistamento, plebiscito, referendo e elegibilidade.",
        )
        val plain = "Brasileiro nato é quem nasce no Brasil, salvo filhos de estrangeiros a serviço do seu país. " +
            "O voto é obrigatório para maiores de dezoito anos e facultativo para analfabetos e idosos. " +
            "A perda da nacionalidade do naturalizado exige cancelamento judicial por atividade nociva. " +
            "O plebiscito consulta o eleitor antes do ato, e o referendo confirma o ato depois de feito."
        val map = NotebookPageMap.build(plain, chapters, emptyList(), emptyList(), emptyList())
        assertEquals(listOf("Nacionalidade", "Direitos políticos"), map.blocks.map { it.title })
        assertEquals(2, map.blocks[0].lines.size)
    }

    @Test fun allTrapsAndNoRepeatedCards() {
        val traps = (1..6).map { "Pegadinha número $it sobre um assunto diferente de prova, com detalhe $it." }
        val recall = listOf(
            "Aviso prévio de reunião é pedido de autorização?" to "Não: aviso prévio de reunião não significa pedido de autorização.",
            "Quanto tempo dura o mandato de senador?" to "Oito anos, com renovação alternada de um e dois terços.",
        )
        val map = NotebookPageMap.build(summary, theory, emptyList(), traps, recall)
        assertEquals(6, map.traps.size)
        // o cartão que repete o "como cai" sai; o que traz algo novo fica
        assertEquals(listOf("Quanto tempo dura o mandato de senador?"), map.cards.map { it.first })
    }
}
