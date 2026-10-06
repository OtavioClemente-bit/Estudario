package br.com.estudario.web.data

import br.com.estudario.domain.simulation.SimulationMode
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Ciclo do simulado no site: planta, questões guardadas fora do banco, entrega e correção. */
class SimulationsTest {
    private fun edital(): Pair<Snapshot, Long> = Catalog.apply(
        Snapshot.empty(),
        CatalogEntry("x", "PMXX", "Polícia Militar", "Soldado"),
        listOf(CatalogSubject("Português", listOf("Crase", "Pontuação", "Concordância")), CatalogSubject("Direito", listOf("Direitos fundamentais", "Administração pública"))),
    )

    private fun proposal(n: Int) = snapshotJson.parseToJsonElement(
        """{"questions":[${(1..n).joinToString(",") { """{"itemRef":"i1","statement":"Questão $it","difficulty":"MEDIA","explanation":"porque","trap":"pegadinha","options":[{"key":"A","text":"certa","correct":true},{"key":"B","text":"errada","correct":false}]}""" }}],
           "detectedBoard":{"name":"Cebraspe","sourceUrl":"https://exemplo.br"}}""",
    ).jsonObject

    @Test
    fun diagnosticCycleFeedsErrorsAndBank() {
        val (data, competitionId) = edital()
        val (created, id) = Simulations.create(data, competitionId, SimulationMode.DIAGNOSTIC, 20)
        val parts = Simulations.decodeParts(Simulations.row(created, id)?.str("partsJson"))
        assertTrue(parts.isNotEmpty())
        assertEquals("GENERATING", created.simulations.single().status)

        val stored = Simulations.storeQuestions(created, id, 0, parts[0], proposal(4))
        // Regravar a mesma parte não duplica.
        val again = Simulations.storeQuestions(stored, id, 0, parts[0], proposal(4))
        assertEquals(4, again.questions.count { it.simulationId == id })
        assertTrue(Queries.visibleQuestions(again).none { it.simulationId == id }, "fora do banco até a entrega")
        assertEquals("Cebraspe", Simulations.examProfile(again, competitionId).board)

        val started = Simulations.start(again, id)
        assertEquals("IN_PROGRESS", started.simulations.single().status)
        val qs = started.questions.filter { it.simulationId == id }
        val answers = mapOf(qs[0].id to "A", qs[1].id to "A", qs[2].id to "B")
        val finished = Simulations.submit(started, id, answers, 600)
        val sim = finished.simulations.single()
        assertEquals("FINISHED", sim.status)
        assertEquals(2, sim.correctCount)
        assertEquals(50, sim.scorePercent)
        assertEquals(1, finished.errors.size)
        assertEquals(4, Queries.visibleQuestions(finished).count { it.simulationId == id }, "entregue: questões entram no banco")
        assertTrue(Simulations.rematchSources(finished, competitionId).isNotEmpty())
        assertEquals(2, Simulations.subjectScores(finished, id).sumOf { it.correct })
    }
}
