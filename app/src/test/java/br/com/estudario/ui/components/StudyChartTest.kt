package br.com.estudario.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StudyChartTest {
    @Test
    fun pizzaComItens() {
        val chart = StudyChart.parse("""{"tipo": "pizza", "titulo": "Receita", "itens": [{"rotulo": "ICMS", "valor": 60}, {"rotulo": "ISS", "valor": 40}]}""")
        assertTrue(chart is StudyChart.Pie)
        assertEquals(2, (chart as StudyChart.Pie).items.size)
        assertEquals("Receita", chart.title)
    }

    @Test
    fun barrasAceitaVirgulaDecimalEmTexto() {
        val chart = StudyChart.parse("""{"tipo": "barras", "itens": [{"rotulo": "A", "valor": "1,5"}, {"rotulo": "B", "valor": 2}]}""") as StudyChart.Bars
        assertEquals(1.5, chart.items[0].value, 1e-9)
    }

    @Test
    fun funcaoComPontos() {
        val chart = StudyChart.parse("""{"tipo": "função", "funcoes": [{"expr": "x^2 - 4", "nome": "f(x)"}], "xmin": -3, "xmax": 3, "pontos": [{"x": 2, "y": 0, "rotulo": "raiz"}]}""")
        assertTrue(chart is StudyChart.Function)
        val f = chart as StudyChart.Function
        assertEquals(0.0, f.curves[0].expression.at(2.0), 1e-9)
        assertEquals(1, f.marks.size)
    }

    @Test
    fun linhaPrecisaDeDoisPontos() {
        assertNull(StudyChart.parse("""{"tipo": "linha", "series": [{"nome": "s", "pontos": [[1, 2]]}]}"""))
        val ok = StudyChart.parse("""{"tipo": "linha", "series": [{"nome": "PIB", "pontos": [[2020, 1], [2021, 3]]}]}""")
        assertTrue(ok is StudyChart.Lines)
    }

    @Test
    fun blocoInvalidoSome() {
        assertNull(StudyChart.parse("isso não é json"))
        assertNull(StudyChart.parse("""{"tipo": "pizza", "itens": [{"rotulo": "só um", "valor": 1}]}"""))
        assertNull(StudyChart.parse("""{"tipo": "desconhecido"}"""))
    }

    @Test
    fun expressoes() {
        fun f(text: String, x: Double) = Expression.parse(text)!!.at(x)
        assertEquals(7.0, f("2x + 1", 3.0), 1e-9)
        assertEquals(-9.0, f("-x^2", 3.0), 1e-9)
        assertEquals(8.0, f("2^3", 0.0), 1e-9)
        assertEquals(12.0, f("3(x+1)", 3.0), 1e-9)
        assertEquals(1.0, f("sen(pi/2)", 0.0), 1e-9)
        assertEquals(2.0, f("log(100)", 0.0), 1e-9)
        assertEquals(3.0, f("raiz(9)", 0.0), 1e-9)
        assertEquals(5.0, f("y = x + 2", 3.0), 1e-9)
        assertEquals(1100.0, f("1000*(1 + 0,1)^x", 1.0), 1e-9)
        assertNull(Expression.parse("2 +"))
        assertNull(Expression.parse("foo(x)"))
    }

    @Test
    fun separaTextoEGrafico() {
        val md = "Antes\n\n```grafico\n{\"tipo\":\"pizza\",\"itens\":[{\"rotulo\":\"a\",\"valor\":1},{\"rotulo\":\"b\",\"valor\":1}]}\n```\n\nDepois"
        val parts = splitCharts(md)
        assertEquals(3, parts.size)
        assertEquals("Antes", parts[0].first)
        assertTrue(parts[1].second is StudyChart.Pie)
        assertEquals("Depois", parts[2].first)
    }

    @Test
    fun cercaComTilsTambemViraGrafico() {
        val parts = splitCharts("Antes\n\n~~~grafico\n{\"tipo\":\"barras\",\"itens\":[{\"rotulo\":\"A\",\"valor\":5},{\"rotulo\":\"B\",\"valor\":10}]}\n~~~\n\nDepois")
        assertEquals(3, parts.size)
        assertTrue(parts[1].second is StudyChart.Bars)
        assertEquals("Depois", parts[2].first)
    }

    @Test
    fun graficoQuebradoNaoAparece() {
        val parts = splitCharts("Texto\n```grafico\n{quebrado\n```")
        assertEquals(listOf("Texto"), parts.map { it.first })
    }

    @Test
    fun marcasDoEixo() {
        assertEquals(listOf(0.0, 2.0, 4.0, 6.0, 8.0, 10.0), niceTicks(0.0, 10.0))
    }

    @Test
    fun geometriaTrianguloRetangulo() {
        val chart = StudyChart.parse("""{"tipo":"geometria","titulo":"Pitágoras","pontos":[{"nome":"A","x":0,"y":0},{"nome":"B","x":4,"y":0},{"nome":"C","x":0,"y":3}],"poligonos":[["A","B","C"]],"segmentos":[{"de":"B","ate":"C","rotulo":"a = 5"}],"angulos":[{"vertice":"A","de":"B","ate":"C","reto":true}]}""")
        assertTrue(chart is StudyChart.Geometry)
        val figure = (chart as StudyChart.Geometry).figure
        assertEquals(3, figure.points.size)
        assertEquals(1, figure.polygons.size)
        assertTrue(figure.angles.single().right)
    }

    @Test
    fun geometriaIgnoraPontoInexistente() {
        val chart = StudyChart.parse("""{"tipo":"figura","pontos":[{"nome":"A","x":0,"y":0}],"segmentos":[{"de":"A","ate":"Z"}]}""")
        assertNull(chart)
    }

    @Test
    fun blocoMarcadoComOTipoTambemViraFigura() {
        val figura = """{"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0},{"nome":"B","x":8,"y":0},{"nome":"C","x":8,"y":5}],"poligonos":[["A","B","C"]]}"""
        val parts = splitCharts("Antes.\n\n```geometria\n$figura\n```\n\nDepois.")
        assertEquals(3, parts.size)
        assertTrue(parts[1].second != null)
    }
}
