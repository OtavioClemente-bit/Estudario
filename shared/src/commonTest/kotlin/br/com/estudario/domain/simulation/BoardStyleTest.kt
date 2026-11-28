package br.com.estudario.domain.simulation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BoardStyleTest {
    @Test fun normalizesAliasesLikeTheServer() {
        assertEquals("cebraspe", BoardStyle.normalize("CESPE/UnB"))
        assertEquals("cebraspe", BoardStyle.normalize("Cebraspe"))
        assertEquals("fcc", BoardStyle.normalize("Fundação Carlos Chagas"))
        assertEquals("fgv", BoardStyle.normalize("Fundação Getúlio Vargas"))
        assertEquals("aocp", BoardStyle.normalize("Instituto AOCP"))
        assertEquals("ibfc", BoardStyle.normalize("Instituto Brasileiro de Formação e Capacitação"))
        assertTrue(BoardStyle.sameBoard("cespe", "CEBRASPE"))
        assertFalse(BoardStyle.sameBoard("FCC", "FGV"))
        assertFalse(BoardStyle.sameBoard(null, null))
    }

    @Test fun styleByBoard() {
        assertEquals("TRUE_FALSE", BoardStyle.styleFor("CEBRASPE"))
        assertEquals("TRUE_FALSE", BoardStyle.styleFor("Cebraspe/CESPE"))
        assertEquals("TRUE_FALSE", BoardStyle.styleFor("Quadrix"))
        assertEquals("TRUE_FALSE", BoardStyle.styleFor("Iades"))
        assertEquals("FOUR_OPTIONS", BoardStyle.styleFor("FUMARC"))
        assertEquals("FIVE_OPTIONS", BoardStyle.styleFor("FCC"))
        assertEquals("FIVE_OPTIONS", BoardStyle.styleFor("Banca Qualquer"))
        assertEquals("FIVE_OPTIONS", BoardStyle.styleFor(null))
    }

    @Test fun wrongCancelsRightOnlyForCertoErrado() {
        assertTrue(BoardStyle.wrongCancelsRight("Cebraspe"))
        assertTrue(BoardStyle.wrongCancelsRight("CESPE"))
        assertTrue(BoardStyle.wrongCancelsRight("Quadrix"))
        assertTrue(BoardStyle.wrongCancelsRight("IADES"))
        assertFalse(BoardStyle.wrongCancelsRight("FCC"))
        assertFalse(BoardStyle.wrongCancelsRight("Fumarc"))
        assertFalse(BoardStyle.wrongCancelsRight(null))
    }

    @Test fun scoring() {
        assertEquals(4, BoardStyle.netScore(7, 3))
        assertEquals(0, BoardStyle.netScore(2, 5))
        // 10 itens: 7 certas, 3 erradas → líquida 4 → 40% na Cebraspe; 70% na FCC.
        assertEquals(40, BoardStyle.scorePercent("Cebraspe", 7, 3, 10))
        assertEquals(70, BoardStyle.scorePercent("FCC", 7, 3, 10))
        // Em branco não tira ponto: 6 certas, 0 erradas, 4 em branco.
        assertEquals(60, BoardStyle.scorePercent("Cebraspe", 6, 0, 10))
        assertEquals(0, BoardStyle.scorePercent("Cebraspe", 1, 9, 10))
        assertEquals(0, BoardStyle.scorePercent("FCC", 0, 0, 0))
    }

    @Test fun affinityOrdersOwnBoardThenSameFormat() {
        assertEquals(0, BoardStyle.affinity("FCC", "Fundação Carlos Chagas"))
        assertEquals(1, BoardStyle.affinity("FCC", "FGV"))
        assertEquals(2, BoardStyle.affinity("FCC", "Cebraspe"))
        assertEquals(1, BoardStyle.affinity("Cebraspe", "Quadrix"))
        assertEquals(2, BoardStyle.affinity("Cebraspe", null))
        assertEquals(0, BoardStyle.affinity(null, "FGV"))
    }

    @Test fun displayNameAndKnownBoards() {
        assertEquals("Cebraspe", BoardStyle.displayName("cespe"))
        assertEquals("Banca X", BoardStyle.displayName(" Banca X "))
        assertEquals(null, BoardStyle.displayName("  "))
        listOf("FCC", "FGV", "Cebraspe", "Vunesp", "Fumarc", "Cesgranrio", "IBFC", "Instituto AOCP", "Quadrix", "Iades", "Idecan", "Consulplan", "Fundatec", "IBADE", "Selecon", "Instituto Access")
            .forEach { assertTrue(it in BoardStyle.KNOWN_BOARDS, it) }
    }
}
