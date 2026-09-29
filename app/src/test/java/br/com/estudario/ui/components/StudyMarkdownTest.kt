package br.com.estudario.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StudyMarkdownTest {
    @Test
    fun tabelaFormulaECodigoFicamInteirosMesmoComLinhaEmBranco() {
        val markdown = """
            # Juros

            | Regime | Fórmula |
            |---|---|
            | Simples | ${'$'}${'$'}J = C \cdot i \cdot t${'$'}${'$'} |

            ${'$'}${'$'}
            M = C(1 + i)^t

            \text{com } t \text{ em meses}
            ${'$'}${'$'}

            ```
            linha 1

            linha 2
            ```

            Fim.
        """.trimIndent()
        val blocks = studyBlocks(markdown)
        assertEquals(5, blocks.size)
        assertTrue(blocks[1].lines().size == 3)
        assertTrue(blocks[2].startsWith("$$") && blocks[2].endsWith("$$"))
        assertTrue(blocks[3].contains("linha 1") && blocks[3].contains("linha 2"))
    }

    @Test
    fun formulasEscritasComBarrasViramCifraoDuploSemMexerEmDinheiro() {
        val text = StudyMarkdownNormalizer.normalize("Custa R$ 10 e \\(a^2\\) vale.\n\\[x = 1\\]")
        assertTrue(text.contains("R$ 10"))
        assertTrue(text.contains("$\$a^2$$"))
        assertTrue(text.contains("\n$$\nx = 1\n$$\n"))
    }
}
