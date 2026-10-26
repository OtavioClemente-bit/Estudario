package br.com.estudario.domain.planner

import kotlin.random.Random

/** Uma questão salva, do jeito que a bateria do plano precisa enxergar. */
data class PlanQuestionCandidate(
    val id: Long,
    val topicId: Long,
    val subjectId: Long?,
    val answered: Boolean,
    /** Está no caderno de erros, ainda para refazer. */
    val pendingError: Boolean,
)

/**
 * As questões da bateria do plano (a Missão do Folha). No grátis a matéria vem com poucas questões, e
 * sortear entre todas fazia a pessoa refazer as mesmas que já acertou. A bateria nunca repete acerto:
 *
 * 1. as ainda não respondidas do tópico da tarefa;
 * 2. as do caderno de erros (do tópico primeiro, depois da matéria);
 * 3. as não respondidas de outros tópicos da mesma matéria que a pessoa já começou a estudar.
 *
 * Sem tópico (tarefa da matéria inteira), vale a matéria nos tópicos já começados. Lista vazia quer
 * dizer que não sobrou nada novo nem errado: a tela avisa em vez de repetir.
 */
object PlanQuestionPool {
    fun pick(
        candidates: List<PlanQuestionCandidate>,
        topicId: Long?,
        subjectId: Long?,
        studiedTopicIds: Set<Long>,
        count: Int,
        random: Random = Random.Default,
    ): List<Long> {
        val inTopic = { c: PlanQuestionCandidate -> topicId != null && c.topicId == topicId }
        val inSubject = { c: PlanQuestionCandidate -> subjectId != null && c.subjectId == subjectId && c.topicId in studiedTopicIds }
        val steps = listOf(
            candidates.filter { inTopic(it) && !it.answered }.shuffled(random),
            candidates.filter { inTopic(it) && it.pendingError }.shuffled(random),
            candidates.filter { !inTopic(it) && inSubject(it) && it.pendingError }.shuffled(random),
            candidates.filter { !inTopic(it) && inSubject(it) && !it.answered }.shuffled(random),
        )
        val out = LinkedHashSet<Long>()
        for (step in steps) for (c in step) { if (out.size >= count) return out.toList(); out += c.id }
        return out.toList()
    }
}
