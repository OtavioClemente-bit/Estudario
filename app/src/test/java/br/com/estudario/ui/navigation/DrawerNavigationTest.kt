package br.com.estudario.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DrawerNavigationTest {
    @Test
    fun `meus concursos e uma entrada so e abre os concursos`() {
        var opened = false
        val sections = estudarioDrawerSections(
            onSyllabus = { opened = true },
            onPlan = {},
            onTrain = {},
            onReviews = {},
            onErrors = {},
            onFocus = {},
            onStatistics = {},
            onBadges = {},
            onSources = {},
            onSettings = {},
            onNotifications = {},
            onSyncCalendar = {},
            onHelp = {},
        )
        // Uma entrada só: os editais salvos na conta ficam dentro de Meus concursos, não no menu.
        val entry = sections.flatMap { it.entries }.single { it.label == "Meus concursos" }

        entry.onClick()

        assertEquals("syllabus", entry.route)
        assertTrue(opened)
        assertTrue(sections.flatMap { it.entries }.none { it.route == "my-syllabi" })
    }

    @Test
    fun `historico do foco tem destino proprio no menu lateral`() {
        var opened = false
        val sections = estudarioDrawerSections(
            onSyllabus = {},
            onPlan = {},
            onTrain = {},
            onReviews = {},
            onErrors = {},
            onFocus = {},
            onStatistics = {},
            onBadges = {},
            onSources = {},
            onSettings = {},
            onNotifications = {},
            onSyncCalendar = {},
            onHelp = {},
            onFocusHistory = { opened = true },
        )
        val entry = sections.flatMap { it.entries }.single { it.label == "Histórico do foco" }

        entry.onClick()

        assertEquals("focus-history", entry.route)
        assertTrue(opened)
    }
}
