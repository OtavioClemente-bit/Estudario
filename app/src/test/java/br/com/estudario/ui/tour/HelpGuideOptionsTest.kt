package br.com.estudario.ui.tour

import org.junit.Assert.assertEquals
import org.junit.Test

class HelpGuideOptionsTest {
    @Test
    fun exposes_edital_and_material_guides_in_that_order() {
        assertEquals(
            listOf(HelpGuide.EDITAL, HelpGuide.MATERIAL),
            helpGuideOptions(),
        )
    }
}
