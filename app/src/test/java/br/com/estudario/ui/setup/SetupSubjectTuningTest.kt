package br.com.estudario.ui.setup

import br.com.estudario.data.catalog.ExamCatalogEntry
import br.com.estudario.domain.catalog.ExamCategory
import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SetupSubjectTuningTest {
    @Test
    fun `barrinhas vao do clarinho ao forte`() {
        val levels = (0 until 5).map { SubjectTuning.intensity(it, 5) }
        assertEquals(levels.sorted(), levels)
        assertTrue(levels.first() < 0.4f)
        assertEquals(1f, levels.last(), 0.0001f)
    }

    @Test
    fun `cada medidor tem explicacao curta e o Folha comenta com o nome da materia`() {
        TuningAxis.entries.forEach { axis ->
            assertTrue(axis.help.isNotBlank() && axis.help.length < 200)
            (0 until 5).forEach { level -> assertTrue(SubjectTuning.reaction(axis, level, "Português").contains("Português")) }
        }
    }

    @Test
    fun `previa do plano muda com o peso e com o que ja sabe`() {
        val base = SubjectTuning.planPreview(ExamPriority.MEDIUM, PersonalDifficulty.NORMAL, InitialKnowledge.NONE)
        assertTrue(base.startsWith("No plano:"))
        assertTrue(base != SubjectTuning.planPreview(ExamPriority.VERY_HIGH, PersonalDifficulty.NORMAL, InitialKnowledge.NONE))
        assertTrue(base != SubjectTuning.planPreview(ExamPriority.MEDIUM, PersonalDifficulty.NORMAL, InitialKnowledge.STRONG))
    }

    private val entries = listOf(
        ExamCatalogEntry("1", "TRT-3", "Tribunal Regional do Trabalho da 3ª Região", "Técnico", "FCC", 2026),
        ExamCatalogEntry("2", "TRT-3", "Tribunal Regional do Trabalho da 3ª Região", "Analista", "FCC", 2026),
        ExamCatalogEntry("3", "CBMMG", "Corpo de Bombeiros Militar de Minas Gerais", "Soldado", "IDECAN", 2025),
        ExamCatalogEntry("4", "INSS", "Instituto Nacional do Seguro Social", "Técnico", "Cebraspe", 2022),
    )

    @Test
    fun `botao conta editais e nao cargos`() {
        assertEquals(2, countExams(entries, ExamPickerFilter(), 2026, searching = false))
        assertEquals(3, countExams(entries, ExamPickerFilter(recentOnly = false), 2026, searching = false))
        assertEquals(1, countExams(entries, ExamPickerFilter(category = ExamCategory.SECURITY), 2026, searching = false))
        // Na busca o período não vale.
        assertEquals(3, countExams(entries, ExamPickerFilter(), 2026, searching = true))
        assertTrue(examFilterQuip(0).isNotBlank() && examFilterQuip(null).isNotBlank())
        ExamCategory.entries.forEach { it.glyph() }
    }
}
