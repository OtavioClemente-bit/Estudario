package br.com.estudario.ui.setup

import br.com.estudario.data.catalog.ExamCatalogEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SetupExamPickerTest {
    private fun entry(id: String, short: String, role: String, board: String? = "FCC", year: Int? = 2025, ready: Int = 0) =
        ExamCatalogEntry(id, short, "Órgão $short", role, board, year, topicCount = 100, readyTopicCount = ready)

    @Test
    fun `cargos do mesmo edital viram um grupo so`() {
        val groups = groupExams(listOf(entry("1", "TRT-3", "Técnico"), entry("2", "PMMG", "Soldado"), entry("3", "TRT-3", "Analista")))
        assertEquals(listOf("TRT-3", "PMMG"), groups.map { it.shortName })
        assertEquals(2, groups.first().entries.size)
        assertFalse(groups.first().single)
        assertTrue(groups[1].single)
    }

    @Test
    fun `ano ou banca diferente e outro edital`() {
        val groups = groupExams(listOf(entry("1", "TRT-3", "Técnico", year = 2025), entry("2", "TRT-3", "Técnico", year = 2019), entry("3", "TRT-3", "Técnico", board = "FGV")))
        assertEquals(3, groups.size)
    }

    @Test
    fun `lista inicial mostra so recentes e os mais novos primeiro`() {
        val entries = listOf(entry("1", "A", "x", year = 2025, ready = 10), entry("2", "B", "x", year = 2026), entry("3", "C", "x", year = 2020), entry("4", "D", "x", year = 2025, ready = 90))
        assertEquals(listOf("B", "D", "A"), browseExams(entries, ExamPickerFilter(), 2026).map { it.shortName })
        assertEquals(4, browseExams(entries, ExamPickerFilter(recentOnly = false), 2026).size)
    }

    @Test
    fun `filtro de banca vale na busca e conta como ativo`() {
        val filter = ExamPickerFilter(board = "FGV")
        val results = listOf(entry("1", "A", "x"), entry("2", "B", "x", board = "FGV"))
        assertEquals(listOf("B"), searchExams(results, filter).map { it.shortName })
        assertEquals(1, filter.activeCount)
        assertEquals(0, ExamPickerFilter().activeCount)
    }
}
