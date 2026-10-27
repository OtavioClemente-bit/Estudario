package br.com.estudario.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Test

/** O padrão do markdown em linha do caderno: um padrão inválido fechava o app ao abrir a página. */
class StudyNotebookInlineTest {
    @Test
    fun achaNegritoCodigoEItalico() {
        val found = INLINE_TOKEN.findAll("A **tese** usa `x++` e *talvez* __isso__.").map { m -> m.groupValues.drop(1).first { it.isNotEmpty() } }.toList()
        assertEquals(listOf("tese", "x++", "talvez", "isso"), found)
    }

    @Test
    fun naoConfundeOperadoresComItalico() {
        assertEquals(emptyList<String>(), INLINE_TOKEN.findAll("a * b = c e 2*3").map { it.value }.toList())
    }
}
