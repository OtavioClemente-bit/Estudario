package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PhoenixTest {
    @Test fun svg_fecha_e_tem_as_partes() {
        for (awake in listOf(true, false)) {
            val svg = Phoenix.svg(awake, "t")
            assertTrue(svg.startsWith("<svg") && svg.endsWith("</svg>"))
            listOf("px-tail", "px-wing-l", "px-wing-r", "px-body").forEach { assertTrue(it in svg) }
        }
        val wings = Phoenix.svg(true, "w", setOf(Phoenix.Part.WING_L))
        assertTrue("px-wing-l" in wings)
        assertFalse("px-body" in wings)
    }
}
