package br.com.estudario.text

import kotlin.test.Test
import kotlin.test.assertEquals

class QuestionKeyTest {
    // Valores tirados do questionKey do servidor: se mudar um lado, o simulado volta a repetir questão.
    @Test
    fun sameKeyAsServer() {
        assertEquals("12db636d", QuestionKey.of("Julgue o item: A crase é OBRIGATÓRIA antes de 2ª palavra?"))
        assertEquals("bb782469", QuestionKey.of("I. um\n\nII. dois"))
        assertEquals("bb782469", QuestionKey.of("I. um II. dois"))
    }
}
