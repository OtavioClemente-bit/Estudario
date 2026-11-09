package br.com.estudario.text

import kotlin.test.Test
import kotlin.test.assertEquals

class StatementLayoutTest {
    @Test fun separaComandoColadoNoUltimoItem() {
        val s = "Manuela separa:\n\nI. Tipo de controle;\n\nII. Propriedades;\n\nIV. Domínios de segurança. Associe os conjuntos, na ordem: a; b."
        assertEquals("Manuela separa:\n\nI. Tipo de controle;\n\nII. Propriedades;\n\nIV. Domínios de segurança.\n\nAssocie os conjuntos, na ordem: a; b.", StatementLayout.separateCommand(s))
    }
    @Test fun naoMexeSemItensOuJaSeparado() {
        assertEquals("Texto simples. Assinale a correta.", StatementLayout.separateCommand("Texto simples. Assinale a correta."))
        val ok = "I. A.\n\nII. B.\n\nAssinale a correta."
        assertEquals(ok, StatementLayout.separateCommand(ok))
    }
}
