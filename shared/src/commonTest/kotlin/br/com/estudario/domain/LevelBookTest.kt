package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LevelBookTest {
    @Test
    fun todosOsNiveisSaemComSvgLimpo() {
        for (level in 1..120) for (mode in LevelBook.Mode.entries) {
            val svg = LevelBook.svg(level, mode)
            assertTrue(svg.startsWith("<svg") && svg.endsWith("</svg>"), "nível $level $mode")
            assertFalse("NaN" in svg || "Infinity" in svg || Regex("""\d\.\d+E""").containsMatchIn(svg), "nível $level $mode")
            assertTrue(">$level</text>" in svg, "o número $level aparece")
        }
    }

    @Test
    fun aSubidaPassaPeloNumeroNovo() {
        val fim = LevelBook.levelUpSvg(29, 30, 1.0)
        assertTrue(">30</text>" in fim)
        val meio = LevelBook.levelUpSvg(29, 30, 0.3)
        assertTrue(">29</text>" in meio)
        assertEquals(2600, LevelBook.levelUpMillis(29, 30))
        assertEquals(1800, LevelBook.levelUpMillis(30, 31))
    }

    @Test
    fun numerosComDuasCasas() {
        assertEquals("12.5", LevelBook.n(12.5))
        assertEquals("-0.3", LevelBook.n(-0.3))
        assertEquals("0", LevelBook.n(0.0001))
        assertEquals("3.05", LevelBook.n(3.05))
    }

    @Test
    fun cadaTituloTemUmaCapa() {
        assertEquals(11, LevelBook.tiers.size)
        LevelBook.tiers.forEach { assertEquals(ProgressEngine.levelTitle(it.from), it.title) }
    }
}
