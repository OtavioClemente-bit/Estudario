package br.com.estudario.domain.planner

import kotlinx.datetime.LocalDate

/**
 * O que fazer com o resultado do motor antes de gravar, igual no app e no site.
 *
 * O motor remarca toda tarefa que não está presa (vira REPROGRAMADA) e cria outra com id novo, mesmo
 * quando a nova é idêntica à antiga (mesmo dia, matéria, tópico, tipo e tamanho). Com dois aparelhos
 * reorganizando o plano várias vezes por dia, cada reorganização gerava uma revisão nova e uma corrente
 * de cópias da mesma tarefa (caso real de 23/11: 14 revisões num dia, o mesmo tópico remarcado ~10
 * vezes), e cada aparelho ficava com ids diferentes para a mesma tarefa.
 *
 * [stabilize] devolve a mesma proposta sem essas trocas inúteis: a tarefa que continuaria igual fica
 * como está (mesmo id, sem transição). Com isso reorganizar duas vezes sem nada mudar não grava nada
 * ([isNoop]) e os dois aparelhos chegam aos mesmos ids.
 */
object PlanStability {
    private data class Slot(val subjectId: Long?, val topicId: Long?, val type: PlanTaskType, val date: LocalDate, val minutes: Int, val questions: Int)

    private fun PlannerTask.slot() = Slot(subjectId, topicId, type, date, plannedMinutes, plannedQuestions)

    fun stabilize(proposal: PlanningProposal, current: List<PlannerTask>): PlanningProposal {
        val byId = current.associateBy { it.id }
        // Só a tarefa planejada (não começada), solta e que o motor ia apenas remarcar pode continuar no lugar.
        val reusable = proposal.transitions.filter { it.from == PlanTaskStatus.PLANEJADA && it.to == PlanTaskStatus.REPROGRAMADA }
            .mapNotNull { byId[it.taskId] }
            .filter { !it.locked && it.status == PlanTaskStatus.PLANEJADA }
        if (reusable.isEmpty() || proposal.newTasks.isEmpty()) return proposal
        val pool = reusable.groupByTo(LinkedHashMap()) { it.slot() }
            .mapValues { (_, list) -> list.sortedWith(compareBy({ it.sequence }, { it.id })).toMutableList() }
        val kept = linkedSetOf<String>()
        val newTasks = ArrayList<PlannerTask>(proposal.newTasks.size)
        for (task in proposal.newTasks) {
            val candidates = pool[task.slot()]
            if (candidates.isNullOrEmpty() || task.origin != PlanOrigin.ENGINE) { newTasks += task; continue }
            // A própria origem primeiro (a cópia volta a ser a original); senão qualquer uma igual.
            val match = candidates.firstOrNull { it.id == task.replannedFromTaskId } ?: candidates.first()
            candidates.remove(match)
            kept += match.id
        }
        if (kept.isEmpty()) return proposal
        return proposal.copy(
            preservedTaskIds = proposal.preservedTaskIds + kept,
            transitions = proposal.transitions.filterNot { it.taskId in kept },
            // Cópia nova que apontava para uma tarefa que ficou viva não é mais cópia dela.
            newTasks = newTasks.map { if (it.replannedFromTaskId in kept) it.copy(replannedFromTaskId = null) else it },
        )
    }

    /** Nada muda: não grava revisão nem toca nas tarefas. */
    fun isNoop(proposal: PlanningProposal): Boolean = proposal.transitions.isEmpty() && proposal.newTasks.isEmpty()

    /**
     * A proposta esvaziaria o plano: havia estudo marcado de hoje em diante e depois dela não sobraria
     * nenhum. Acontece quando o motor roda com os dados incompletos (antes de carregar ou no meio de uma
     * sincronização) e no log real aparecia "0 tarefa(s) planejada(s)" seguido de outra com 83. Não grava.
     * Plano sem matéria ativa (tudo pausado) pode ficar vazio de verdade, e o REBUILD esvazia de propósito
     * (logo depois vem o DAY_MISSED que monta o plano de novo).
     */
    fun wouldEmptyPlan(proposal: PlanningProposal, current: List<PlannerTask>, today: LocalDate, hasActiveSubjects: Boolean, reason: ReplanReason): Boolean {
        if (!hasActiveSubjects || reason == ReplanReason.REBUILD) return false
        val live = setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
        val ahead = current.filter { it.date >= today && it.status in live }
        if (ahead.isEmpty()) return false
        val retired = proposal.transitions.mapTo(hashSetOf()) { it.taskId }
        val remaining = ahead.count { it.id !in retired }
        return remaining == 0 && proposal.newTasks.none { it.date >= today }
    }
}
