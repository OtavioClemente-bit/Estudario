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

    /** O motor grava NAO_REALIZADA na tarefa de um dia que passou e cria a continuação dela: a continuação fica. */
    @Test
    fun continuacaoDeTarefaAtrasadaNaoEhCopiaVelha() {
        val rows = listOf(
            Row("seg", null, "NAO_REALIZADA", "100|THEORY"), Row("ter", "seg", "PLANEJADA", "101|THEORY"),
            Row("x", null, "NAO_REALIZADA"), Row("y", "x", "REPROGRAMADA"), Row("z", "y", "PLANEJADA"),
        )
        assertEquals(emptySet(), StaleCopies.ids(rows))
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
