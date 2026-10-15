package br.com.estudario.data.catalog

import br.com.estudario.data.local.AppDao
import br.com.estudario.data.local.PriorityEvidenceCodec
import br.com.estudario.domain.ExamWeights
import br.com.estudario.domain.PrioritySource
import kotlinx.coroutines.CancellationException

/**
 * Peso real da prova (questões por matéria no quadro do edital) gravado como prioridade avaliada da
 * matéria. Nunca mexe em prioridade que a pessoa trocou nem em avaliação de outra fonte (IA, histórico).
 */
object ExamWeightsApplier {
    suspend fun apply(dao: AppDao, competitionId: Long, subjects: List<ExamCatalogSubject>): Boolean {
        val weights = ExamWeights.assessAll(subjects.map { it.share })
        if (weights.isEmpty()) return false
        val byName = subjects.mapIndexed { index, subject -> subject.name.lowercase() to weights[index] }.toMap()
        var changed = false
        for (subject in dao.subjectsFor(competitionId)) {
            val weight = byName[subject.name.lowercase()] ?: continue
            if (subject.userPriorityOverride != null) continue
            if (subject.hasAssessedPriority && subject.assessedPrioritySource != PrioritySource.OFFICIAL_EXAM_STRUCTURE) continue
            dao.updateSubject(
                subject.copy(
                    assessedPriorityScore = weight.score,
                    assessedPrioritySource = weight.source,
                    assessedPriorityConfidence = weight.confidence,
                    assessedPriorityRationale = weight.rationale,
                    assessedPriorityEvidenceJson = PriorityEvidenceCodec.encode(weight.evidence),
                    hasAssessedPriority = true,
                ),
            )
            changed = true
        }
        return changed
    }

    /**
     * Quem escolheu um edital do catálogo antes de ele ter os pesos: acha o edital de mesmo nome e
     * mesmas matérias e grava os pesos. Falha de rede só adia para a próxima abertura.
     */
    suspend fun backfill(dao: AppDao, catalog: ExamCatalogRepository) {
        try {
            val pending = dao.competitionsOnce().filter { competition ->
                val own = dao.subjectsFor(competition.id)
                own.isNotEmpty() && own.none { it.hasAssessedPriority }
            }
            if (pending.isEmpty()) return
            val entries = catalog.entries()
            for (competition in pending) {
                val names = dao.subjectsFor(competition.id).mapTo(hashSetOf()) { it.name.lowercase() }
                val candidates = entries.filter { it.shortName.equals(competition.name, true) || "${it.shortName} · ${it.role}".equals(competition.name, true) }.take(12)
                for (entry in candidates) {
                    val subjects = runCatching { catalog.subjects(entry.id) }.getOrNull() ?: continue
                    if (subjects.any { it.questions == null } || subjects.mapTo(hashSetOf()) { it.name.lowercase() } != names) continue
                    apply(dao, competition.id, subjects)
                    break
                }
            }
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
        }
    }
}
