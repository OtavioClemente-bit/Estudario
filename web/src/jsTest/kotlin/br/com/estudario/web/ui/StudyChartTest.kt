package br.com.estudario.web.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StudyChartTest {
    @Test
    fun cercaComCrasesOuTils() {
        assertEquals("grafico", chartFenceName("~~~grafico"))
        assertEquals("grafico", chartFenceName("```grafico"))
        assertEquals("grafico", chartFenceName("~~~grafico {\"tipo\":\"barras\"} ~~~"))
        assertNull(chartFenceName("```kotlin"))
        assertNull(chartFenceName("texto comum"))
    }

    @Test
    fun barrasELinha() {
        val bars = StudyChart.parse("{\"tipo\":\"barras\",\"titulo\":\"T\",\"itens\":[{\"rotulo\":\"A\",\"valor\":5},{\"rotulo\":\"B\",\"valor\":10}]}")
        assertTrue(bars is StudyChart.Bars && bars.items.size == 2 && bars.title == "T")
        val line = StudyChart.parse("{\"tipo\":\"linha\",\"series\":[{\"nome\":\"u\",\"pontos\":[[1,12],[2,15],[3,14]]}]}")
        assertTrue(line is StudyChart.Lines && line.series.first().points.size == 3)
    }

    @Test
    fun blocoQuebradoOuTipoDoAppSome() {
        assertNull(StudyChart.parse("{quebrado"))
        assertNull(StudyChart.parse("{\"tipo\":\"funcao\",\"funcoes\":[\"x^2\"]}"))
    }
}
