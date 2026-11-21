package br.com.estudario.domain

import br.com.estudario.domain.planner.StaleCopies
import br.com.estudario.domain.planner.StaleCopies.Row
import kotlin.test.Test
import kotlin.test.assertEquals

class StaleCopiesTest {
    @Test fun `copia de tarefa concluida em outro aparelho sai e a corrente normal fica`() {
        val rows = listOf(
            Row("a", null, "CONCLUIDA"), Row("b", "a", "PLANEJADA"), // concluída no site, copiada no celular
            Row("c", null, "CONCLUIDA"), Row("d", "c", "REPROGRAMADA"), Row("e", "d", "PLANEJADA"), // cópia da cópia
            Row("f", null, "CONCLUIDA"), Row("g", "f", "NAO_REALIZADA"),
            Row("x", null, "REPROGRAMADA"), Row("y", "x", "PLANEJADA"), // reorganização normal
            Row("p", null, "CONCLUIDA"), Row("q", "p", "EM_ANDAMENTO"), // já começada: não mexe
        )
        assertEquals(setOf("b", "e", "g"), StaleCopies.ids(rows))
    }

    /** Celular e site reorganizaram a mesma tarefa para o mesmo dia: fica uma cópia só (a de menor id). */
    @Test
    fun duasCopiasDaMesmaTarefaNoMesmoDiaViramUma() {
        val rows = listOf(
            Row("t", null, "REPROGRAMADA", "100|THEORY"),
            Row("c-site", "t", "PLANEJADA", "106|THEORY"), Row("c-cel", "t", "PLANEJADA", "106|THEORY"),
            Row("parte2", "t", "PLANEJADA", "107|THEORY"), // outro dia: parte da mesma teoria, fica
        )
        assertEquals(setOf("c-site"), StaleCopies.ids(rows))
    }
}
