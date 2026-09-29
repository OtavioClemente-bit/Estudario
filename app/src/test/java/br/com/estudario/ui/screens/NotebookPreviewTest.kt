package br.com.estudario.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookPreviewTest {
    @Test
    fun previaTiraSintaxeDoMarkdownEMantemOConteudo() {
        assertEquals("Juros compostos Crescem **rápido**".replace("**", ""), plainPreview("## Juros compostos\n\nCrescem **rápido**"))
        assertEquals("Regime · Fórmula · Simples · J = Cit", plainPreview("| Regime | Fórmula |\n|---|---|\n| Simples | \$\$J = Cit\$\$ |"))
        assertEquals("Atenção à banca", plainPreview("> Atenção à banca"))
    }
}
