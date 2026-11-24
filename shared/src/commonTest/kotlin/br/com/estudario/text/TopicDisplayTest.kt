package br.com.estudario.text

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TopicDisplayTest {
    @Test
    fun cleansEditalFragments() {
        assertEquals("Relações de subordinação entre orações e entre termos da oração", TopicDisplay.clean("relações de subordinação entre orações e entre termos da oração;"))
        assertEquals("Uso da crase", TopicDisplay.clean("  e uso da crase. "))
        assertEquals("Concordância verbal e nominal", TopicDisplay.clean("concordância   verbal\n e nominal ,"))
        assertEquals("Pontuação", TopicDisplay.clean("- pontuação;"))
        assertEquals("Lei 8.112/1990", TopicDisplay.clean("Lei 8.112/1990."))
    }

    @Test
    fun keepsIntentionalCaseAndOddInput() {
        assertEquals("eSocial", TopicDisplay.clean("eSocial;"))
        assertEquals("Ética", TopicDisplay.clean("ética"))
        assertEquals(";", TopicDisplay.clean(";"))
        assertEquals("E", TopicDisplay.clean("e"))
    }

    @Test
    fun depthFromIndent() {
        assertEquals(0, TopicDisplay.depthOf("Crase"))
        assertEquals(1, TopicDisplay.depthOf("  Crase"))
        assertEquals(2, TopicDisplay.depthOf("    Crase"))
    }

    @Test
    fun weightLabel() {
        assertEquals("15 questões · 25% da nota", TopicDisplay.weightLabel(15, "15 de 60 questões na prova (25% da nota)"))
        assertEquals("1 questão", TopicDisplay.weightLabel(1, null))
        assertEquals("30% da nota", TopicDisplay.weightLabel(null, "bloco (30% da nota)"))
        assertEquals("Fora da prova objetiva", TopicDisplay.weightLabel(0, "não cai na prova objetiva"))
        assertNull(TopicDisplay.weightLabel(null, null))
    }
}
