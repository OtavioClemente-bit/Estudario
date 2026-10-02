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

    @Test
    fun dinheiroEscritoComoFormulaViraTexto() {
        val text = StudyMarkdownNormalizer.normalize("O lucro máximo é \$\$R\\\$\\,1.050,00\$\$, obtido com 25 unidades.")
        assertEquals("O lucro máximo é R$ 1.050,00, obtido com 25 unidades.", text)
    }

    @Test
    fun formulaViraTextoSimplesNasPrevias() {
        assertEquals("Uma função é dada por f(x)=-2(x+1)(x-4). Qual?", plainFormulaText("Uma função é dada por \$\$f(x)=-2(x+1)(x-4)\$\$. Qual?"))
        assertEquals("y_v=-Δ/4a e Δ ≤ 0", plainFormulaText("\$\$y_v=-\\frac{\\Delta}{4a}\$\$ e \$\$\\Delta \\le 0\$\$"))
        assertEquals("Texto sem fórmula", plainFormulaText("Texto sem fórmula"))
    }

    @Test
    fun capituloQueRepeteOTituloMostraUmaVezSo() {
        val blocks = studyBlocks("## 1. Fundamentos\n\n## 1. Fundamentos\n\nTexto.\n\n## 2. Outro\n\nMais.")
        assertEquals(listOf("## 1. Fundamentos", "Texto.", "## 2. Outro", "Mais."), blocks)
    }
}
