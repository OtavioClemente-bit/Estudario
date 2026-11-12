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

    @Test fun `peca da area do edital segue a escada normal e a de outra area pede bem mais`() {
        val pijama = FolhaWardrobe.find(FolhaSlot.CAPA, "pijama-cirurgico")!!
        FolhaWardrobe.homeArea = FolhaArea.SAUDE
        val goal = FolhaWardrobe.goalOf(pijama)!!
        fun with(n: Int) = when (goal.kind) {
            FolhaWardrobe.GoalKind.DIAS -> ProgressEngine.ProgressSummary(studyDays = n)
            FolhaWardrobe.GoalKind.QUESTOES -> ProgressEngine.ProgressSummary(totalQuestions = n)
            FolhaWardrobe.GoalKind.TOPICOS -> ProgressEngine.ProgressSummary(topicsStudied = n)
        }
        assertFalse(FolhaWardrobe.unlocked(pijama, with(goal.target - 1)))
        assertTrue(FolhaWardrobe.unlocked(pijama, with(goal.target)))
        FolhaWardrobe.homeArea = FolhaArea.MILITAR
        assertFalse(FolhaWardrobe.unlocked(pijama, with(goal.target)))
        assertTrue(FolhaWardrobe.unlocked(pijama, with(goal.target * FolhaWardrobe.OTHER_AREA_FACTOR)))
    }

    @Test fun `quem comeca nao ganha quase nada e as primeiras pecas sao da propria area`() {
        FolhaWardrobe.homeArea = FolhaArea.TI
        val iniciante = ProgressEngine.ProgressSummary(studyDays = 4, totalQuestions = 80, topicsStudied = 3)
        val ganhou = FolhaWardrobe.items.filter { it.days != null && FolhaWardrobe.unlocked(it, iniciante) }
        assertTrue(ganhou.size <= 6, "iniciante ganhou ${ganhou.size} peças: ${ganhou.map { it.id }}")
        assertTrue(ganhou.none { it.area != null && it.area != FolhaArea.TI }, "peça de outra área cedo demais: ${ganhou.map { it.id }}")
    }

    @Test fun `o que ja saiu fica mesmo trocando de edital`() {
        FolhaWardrobe.homeArea = FolhaArea.SAUDE
        val summary = ProgressEngine.ProgressSummary(studyDays = 400, totalQuestions = 20000, topicsStudied = 500)
        val keep = FolhaWardrobe.toKeep(summary)
        assertTrue("${FolhaSlot.CAPA.key}:pijama-cirurgico" in keep)
        FolhaWardrobe.owned = keep
        FolhaWardrobe.homeArea = FolhaArea.TI
        val pijama = FolhaWardrobe.find(FolhaSlot.CAPA, "pijama-cirurgico")!!
        assertTrue(FolhaWardrobe.unlocked(pijama, ProgressEngine.ProgressSummary()))
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
