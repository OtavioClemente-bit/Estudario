package br.com.estudario.domain

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FolhaAreaTest {
    @AfterTest fun reset() { FolhaWardrobe.homeArea = null; FolhaWardrobe.owned = emptySet() }

    @Test fun `area do edital pelo orgao e pelo cargo`() {
        assertEquals(FolhaArea.TI, FolhaArea.of("TRT 3", "Tribunal Regional do Trabalho", "Analista Judiciário - Tecnologia da Informação"))
        assertEquals(FolhaArea.TRIBUNAIS, FolhaArea.of("TRT 3", "Tribunal Regional do Trabalho", "Técnico Judiciário"))
        assertEquals(FolhaArea.POLICIA, FolhaArea.of("PMMG", "Polícia Militar de Minas Gerais", "Soldado"))
        assertEquals(FolhaArea.MILITAR, FolhaArea.of("EsPCEx", "Exército Brasileiro", "Cadete"))
        assertEquals(FolhaArea.MILITAR, FolhaArea.of("CBMMG", "Corpo de Bombeiros Militar", "Soldado"))
        assertEquals(FolhaArea.SAUDE, FolhaArea.of("EBSERH", "Empresa Brasileira de Serviços Hospitalares", "Enfermeiro"))
        assertEquals(FolhaArea.CONTROLE, FolhaArea.of("TCU", "Tribunal de Contas da União", "Auditor"))
        assertEquals(FolhaArea.FISCAL, FolhaArea.of("SEFAZ", "Secretaria da Fazenda", "Auditor Fiscal"))
        assertEquals(FolhaArea.SAUDE, FolhaArea.of("Prefeitura", "Prefeitura de Contagem", "Técnico de Enfermagem"))
        assertEquals(FolhaArea.ADMINISTRATIVO, FolhaArea.of("INSS", "Instituto Nacional do Seguro Social", "Técnico do Seguro Social"))
    }

    @Test fun `peca da area do edital sai antes e as das outras areas tambem saem, mais devagar`() {
        val jaleco = FolhaWardrobe.find(FolhaSlot.CAPA, "pijama-cirurgico")!!
        val dias = jaleco.days!!
        FolhaWardrobe.homeArea = FolhaArea.SAUDE
        assertTrue(FolhaWardrobe.unlocked(jaleco, ProgressEngine.ProgressSummary(studyDays = dias)))
        FolhaWardrobe.homeArea = FolhaArea.MILITAR
        assertFalse(FolhaWardrobe.unlocked(jaleco, ProgressEngine.ProgressSummary(studyDays = dias)))
        assertTrue(FolhaWardrobe.unlocked(jaleco, ProgressEngine.ProgressSummary(studyDays = dias * FolhaWardrobe.OTHER_AREA_FACTOR)))
    }

    @Test fun `o que ja saiu fica mesmo trocando de edital`() {
        FolhaWardrobe.homeArea = FolhaArea.SAUDE
        val summary = ProgressEngine.ProgressSummary(studyDays = 40)
        val keep = FolhaWardrobe.toKeep(summary)
        assertTrue("capa:pijama-cirurgico".replace("capa", FolhaSlot.CAPA.key) in keep)
        FolhaWardrobe.owned = keep
        FolhaWardrobe.homeArea = FolhaArea.TI
        val pijama = FolhaWardrobe.find(FolhaSlot.CAPA, "pijama-cirurgico")!!
        assertTrue(FolhaWardrobe.unlocked(pijama, ProgressEngine.ProgressSummary(studyDays = 0)))
    }

    @Test fun `armario com cerca de 300 pecas, sem repetir lugar e id, e coleções apontando para pecas que existem`() {
        val items = FolhaWardrobe.items
        assertTrue(items.size >= 290, "só ${items.size} peças")
        assertEquals(items.size, items.map { it.slot to it.id }.toSet().size, "peça repetida no mesmo lugar")
        FolhaWardrobe.collections.forEach { c ->
            assertEquals(c.pieces.size, FolhaWardrobe.piecesOf(c).size, "coleção ${c.id} com peça que não existe")
            assertTrue(FolhaWardrobe.rewardOf(c) != null, "coleção ${c.id} sem recompensa")
        }
        FolhaArea.entries.forEach { a -> assertTrue(items.count { it.area == a } >= 8, "área ${a.label} com poucas peças") }
    }
}
