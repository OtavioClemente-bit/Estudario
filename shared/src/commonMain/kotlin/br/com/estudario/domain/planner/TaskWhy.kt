package br.com.estudario.domain.planner

/**
 * "Por que esta tarefa está aqui?" em uma linha, igual no app e no site. A pessoa confia mais no plano
 * quando entende o motivo de cada coisa: próximo tópico na ordem, retomada por queda de acertos,
 * revisão misturada da matéria, simulado de acompanhamento ou revisão espaçada.
 *
 * [retake]: o tópico já teve teoria concluída antes desta tarefa (é a "teoria de retomada" do motor).
 */
object TaskWhy {
    fun text(type: PlanTaskType, hasTopic: Boolean, retake: Boolean, subjectName: String): String? {
        val subject = subjectName.ifBlank { "a matéria" }
        return when (type) {
            PlanTaskType.THEORY -> if (retake) "Retomada: seus acertos recentes neste tópico caíram, então volta uma teoria curta antes de mais questões."
            else "Próximo tópico de $subject, na ordem do edital e no ritmo que cabe até a prova."
            PlanTaskType.QUESTIONS -> if (!hasTopic) "Revisão da matéria: questões misturadas do que você já estudou de $subject, para não esquecer." else null
            PlanTaskType.SIMULATION -> "Simulado do plano: mostra onde você está e ajusta o foco das próximas semanas."
            PlanTaskType.REVIEW, PlanTaskType.ACTIVE_RECALL, PlanTaskType.FLASHCARDS -> "Revisão espaçada: é a hora certa para fixar antes de esquecer."
            PlanTaskType.DISCURSIVE -> "Treino de discursiva: a prova cobra escrita, e ela melhora com prática regular."
        }
    }
}
