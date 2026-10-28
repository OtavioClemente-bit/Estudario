package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LevelCrestTest {
    @Test fun patentes_seguem_os_titulos() {
        LevelCrest.ranks.forEach { assertEquals(ProgressEngine.levelTitle(it.from), it.title) }
        for (level in 1..120) assertEquals(ProgressEngine.levelTitle(level), LevelCrest.rank(level).title)
    }

    @Test fun estrelas_e_promocao() {
        assertEquals(1, LevelCrest.stars(1)); assertEquals(4, LevelCrest.stars(4)); assertEquals(1, LevelCrest.stars(5)); assertEquals(5, LevelCrest.stars(9))
        assertEquals(5, LevelCrest.stars(80))
        assertTrue(LevelCrest.isPromotion(10)); assertFalse(LevelCrest.isPromotion(11)); assertFalse(LevelCrest.isPromotion(1))
    }

    @Test fun svg_fecha_e_mostra_o_numero() {
        for (level in 1..120) {
            val svg = LevelCrest.svg(level, "t$level")
            assertTrue(svg.startsWith("<svg") && svg.endsWith("</svg>"))
            assertTrue(">$level</text>" in svg)
            assertFalse("NaN" in svg)
        }
    }
}
