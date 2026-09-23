package br.com.estudario.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Test

class QueuePresentationTest {
    @Test
    fun firstUnpausedRowIsNextEvenWhenItIsNotTheFirstRow() {
        assertEquals("Pausado", queueRowLabel(paused = true, completed = false, isNext = false))
        assertEquals("Próximo estudo", queueRowLabel(paused = false, completed = false, isNext = true))
    }

    @Test
    fun pausedRowsKeepPausedLabelWhenAnotherRowIsNext() {
        assertEquals("Pausado", queueRowLabel(paused = true, completed = false, isNext = false))
    }

    @Test
    fun completedStaleRowsCannotBeLabeledAsNext() {
        assertEquals("Concluído", queueRowLabel(paused = false, completed = true, isNext = false))
        assertEquals("Concluído", queueRowLabel(paused = false, completed = true, isNext = true))
    }

    @Test
    fun otherAvailableRowsStayInQueue() {
        assertEquals("Na fila", queueRowLabel(paused = false, completed = false, isNext = false))
    }
}
