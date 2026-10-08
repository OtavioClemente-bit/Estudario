package br.com.estudario.text

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SourcesSectionTest {
    @Test
    fun `secao de fontes no fim sai com os subtitulos`() {
        val markdown = """
            ## Crase
            Texto da teoria.

            ### Fontes consultadas
            #### Fontes oficiais/primárias
            - Lei X
            #### Fontes complementares
            - Livro Y
        """.trimIndent()
        val out = SourcesSection.strip(markdown)
        assertEquals("## Crase\nTexto da teoria.", out)
    }

    @Test
    fun `rotulo em negrito e a lista abaixo saem, o resto fica`() {
        val markdown = "Antes.\n\n**Fontes:**\n- A\n- B\n\nDepois."
        val out = SourcesSection.strip(markdown)
        assertTrue(out.contains("Antes."))
        assertTrue(out.contains("Depois."))
        assertFalse(out.contains("- A"))
    }

    @Test
    fun `conteudo que so comeca com fonte nao e cortado`() {
        val markdown = "## Fontes de energia\n**Fontes de energia renováveis:** solar e eólica."
        assertEquals(markdown, SourcesSection.strip(markdown))
    }

    @Test
    fun `secao seguinte depois das fontes volta a aparecer`() {
        val markdown = "## Fontes\n- A\n## Exercícios\nTexto."
        assertEquals("## Exercícios\nTexto.", SourcesSection.strip(markdown))
    }

    @Test
    fun `citacao so com links no meio do texto sai`() {
        val text = "O prazo é de cinco anos ([planalto.gov.br](https://www.planalto.gov.br/l8112.htm?utm_source=openai)). Depois."
        assertEquals("O prazo é de cinco anos. Depois.", SourcesSection.stripInline(text))
        assertEquals("Veja isto.", SourcesSection.stripInline("Veja isto ([A](https://a.gov.br); [B](https://b.gov.br))."))
    }

    @Test
    fun `parenteses com texto ou link no meio da frase ficam`() {
        val text = "Use a regra (ver também a Súmula 134) e o [portal](https://x.gov.br) (oficial)."
        assertEquals(text, SourcesSection.stripInline(text))
    }
}
