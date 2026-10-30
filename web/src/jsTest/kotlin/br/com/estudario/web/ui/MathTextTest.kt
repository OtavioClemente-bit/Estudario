package br.com.estudario.web.ui

import kotlin.test.Test
import kotlin.test.assertEquals

class MathTextTest {
    @Test
    fun formulaDeLogicaViraSimbolos() {
        assertEquals("¬p", MathText.plain("""\neg p"""))
        assertEquals("p ∧ q", MathText.plain("""p \land q"""))
        assertEquals("p ∨ q", MathText.plain("""p \lor q"""))
        assertEquals("p ⊻ q", MathText.plain("""p \underline{\vee} q"""))
        assertEquals("p ↔ q", MathText.plain("""p \leftrightarrow q"""))
        assertEquals("p ≡ p", MathText.plain("""p \equiv p"""))
    }

    @Test
    fun comandoCurtoNaoComeOComprido() {
        assertEquals("( x ≤ 2 )", MathText.plain("""\left( x \le 2 \right)"""))
        assertEquals("p → q", MathText.plain("""p \rightarrow q"""))
        assertEquals("(a)/(b) ≠ 1", MathText.plain("""\frac{a}{b} \neq 1"""))
    }

    @Test
    fun tituloDaTabelaSemMarcacao() {
        assertEquals("Tabela: Critério · Outorga (por serviços, funcional, técnica)", TheoryPins.titleFor("| Critério | **Outorga** (por serviços, funcional, técnica) |\n| --- | --- |"))
        assertEquals("Tabela: p · q · ¬p", TheoryPins.titleFor("| \$\$p\$\$ | \$\$q\$\$ | \$\$\\neg p\$\$ |\n| --- | --- | --- |"))
    }
}
