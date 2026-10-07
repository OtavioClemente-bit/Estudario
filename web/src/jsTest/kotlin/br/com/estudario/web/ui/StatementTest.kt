package br.com.estudario.web.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StatementTest {
    private val sample = "TEXTO-BASE 7 — Prioridades ambientais (Percepção ambiental; Agência Brasil (17/09/2026); adaptação didática de fonte real: https://agenciabrasil.ebc.com.br/geral/noticia). Uma pesquisa de 2026 ouviu 1.800 brasileiros sobre clima e prioridades públicas. A amostra não permite atribuir a mesma opinião a cada brasileiro.\n\n1. Que relação o conectivo 'Ao mesmo tempo' estabelece entre as informações?"

    @Test
    fun separaTextoBaseComandoEFonte() {
        val parts = splitStatement(sample)
        assertEquals("TEXTO-BASE 7", parts.label)
        assertEquals("Prioridades ambientais", parts.title)
        assertTrue(parts.base.startsWith("Uma pesquisa de 2026"), parts.base)
        assertTrue("http" !in parts.base)
        assertEquals("Que relação o conectivo 'Ao mesmo tempo' estabelece entre as informações?", parts.command)
        assertTrue(parts.source!!.contains("agenciabrasil.ebc.com.br"))
    }

    @Test
    fun enunciadoSimplesFicaComoEsta() {
        val parts = splitStatement("Julgue o item: a Constituição prevê o habeas data.")
        assertNull(parts.label)
        assertNull(parts.source)
    }
}
