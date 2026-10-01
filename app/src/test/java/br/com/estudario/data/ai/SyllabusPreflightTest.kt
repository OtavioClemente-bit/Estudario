package br.com.estudario.data.ai

import org.junit.Assert.*
import org.junit.Test

class SyllabusPreflightTest {
    private val pmmg = """
        EDITAL DRH/CRS Nº 10/2024
        Polícia Militar de Minas Gerais
        Curso de Formação de Soldados - CFSd 2025
        Concurso Público para admissão ao curso de formação de soldados.
        ANEXO: CONTEÚDO PROGRAMÁTICO
        1. Língua Portuguesa: interpretação e compreensão de textos.
        2. Matemática: operações e funções do primeiro grau.
        3. Direito Constitucional: direitos e garantias fundamentais.
    """.trimIndent()

    private fun inspect(text: String, competition: String = "PMMG", role: String = "Soldado") =
        SyllabusPreflight.inspect(listOf(text), AiSyllabusPreferences(competitionName = competition, role = role))

    @Test fun `aliases accents short names and plural roles are compatible`() {
        listOf("PMMG", "PM MG", "Policia Militar MG", "Polícia Militar de Minas Gerais", "Policia Militar", "PM").forEach { competition ->
            listOf("Soldado", "CFSd", "Formação de Soldados", "Curso de Formação de Soldados").forEach { role ->
                assertEquals("$competition / $role", SyllabusPreflightKind.VALID, inspect(pmmg, competition, role).kind)
            }
        }
    }

    @Test fun `clearly divergent context warns without rejecting authoritative PDF`() {
        val result = inspect(pmmg, "TRT 3 REGIAO TI")
        assertEquals(SyllabusPreflightKind.VALID_WITH_WARNING, result.kind)
        assertTrue(result.canGenerate)
        assertTrue(result.warnings.any { it.contains("TRT 3 REGIAO TI") })
        assertTrue(result.documentIdentification!!.contains("Polícia Militar"))
    }

    @Test fun `sharing a generic institution word does not conceal a mismatch`() {
        assertEquals(SyllabusPreflightKind.VALID_WITH_WARNING, inspect(pmmg, "Polícia Federal", "Delegado").kind)
    }

    @Test fun `an official programme need not contain edital keyword`() {
        val result = inspect(pmmg.replace("EDITAL DRH/CRS Nº 10/2024", "Programa de matérias para ingresso"))
        assertTrue(result.canGenerate)
        assertEquals(SyllabusPreflightKind.VALID, result.kind)
    }

    @Test fun `clearly unrelated bill is blocked`() {
        val result = inspect("Boleto bancário. Linha digitável. Beneficiário Banco. Comprovante de pagamento. " .repeat(10))
        assertEquals(SyllabusPreflightKind.NOT_AN_EDITAL, result.kind)
        assertFalse(result.canGenerate)
    }

    @Test fun `contest schedule without programme is blocked`() {
        val result = inspect("Edital de concurso público. Cronograma de inscrições. Calendário de divulgação de resultados e convocação. ".repeat(8))
        assertEquals(SyllabusPreflightKind.CONTENT_NOT_FOUND, result.kind)
        assertFalse(result.canGenerate)
    }

    @Test fun `scanned empty unknown and mixed files stay uncertain`() {
        listOf(emptyList(), listOf(""), listOf("Documento desconhecido sem identificação clara. ".repeat(6)), listOf("Concurso público: cronograma de inscrições. ".repeat(10), ""), listOf("Boleto bancário, linha digitável e pagamento. ".repeat(10), "")).forEach { pages ->
            val result = SyllabusPreflight.inspect(pages, AiSyllabusPreferences("PMMG", "Soldado"))
            assertEquals(SyllabusPreflightKind.CANNOT_VALIDATE, result.kind)
            assertTrue(result.canGenerate)
        }
    }
}
