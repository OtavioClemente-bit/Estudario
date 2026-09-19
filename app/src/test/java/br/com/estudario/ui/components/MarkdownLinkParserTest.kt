package br.com.estudario.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownLinkParserTest {
    @Test
    fun `markdown source link keeps its label and destination`() {
        val tokens = parseInlineLinks("Fonte: [Edital oficial](https://gov.br/edital)")

        assertEquals(
            listOf(
                InlineMarkdownToken.Text("Fonte: "),
                InlineMarkdownToken.Link("Edital oficial", "https://gov.br/edital"),
            ),
            tokens,
        )
    }

    @Test
    fun `bare web urls become links without trailing punctuation`() {
        val tokens = parseInlineLinks("Referência: https://example.org/prova.pdf.")

        assertEquals(
            listOf(
                InlineMarkdownToken.Text("Referência: "),
                InlineMarkdownToken.Link("https://example.org/prova.pdf", "https://example.org/prova.pdf"),
                InlineMarkdownToken.Text("."),
            ),
            tokens,
        )
    }

    @Test
    fun `non web or malformed links remain plain text`() {
        val tokens = parseInlineLinks("[arquivo](javascript:alert(1)) e https://")

        assertTrue(tokens.all { it is InlineMarkdownToken.Text })
        assertFalse(tokens.any { it is InlineMarkdownToken.Link })
    }

    @Test
    fun `multiple source links are kept separately`() {
        val tokens = parseInlineLinks("[Edital](https://gov.br/edital) e https://banca.org/prova")

        assertEquals(
            listOf(
                InlineMarkdownToken.Link("Edital", "https://gov.br/edital"),
                InlineMarkdownToken.Text(" e "),
                InlineMarkdownToken.Link("https://banca.org/prova", "https://banca.org/prova"),
            ),
            tokens,
        )
    }

    @Test
    fun `only http links with a host and without embedded credentials are safe`() {
        assertTrue(isSafeWebUrl("https://gov.br/edital"))
        assertTrue(isSafeWebUrl("http://banca.org/prova"))
        assertFalse(isSafeWebUrl("ftp://banca.org/prova"))
        assertFalse(isSafeWebUrl("https://usuario:senha@example.org/prova"))
    }
}
