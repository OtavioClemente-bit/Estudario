package br.com.estudario.domain

/** Avisos antes de concluir um tópico; nenhum deles impede a escolha da pessoa. */
enum class TopicCompletionWarning { NONE, OUTSIDE_ACTIVE_PLAN, UNDER_PLANNED_TIME }

object TopicCompletionPolicy {
    fun warning(plannedMinutes: Int?, actualMinutes: Int): TopicCompletionWarning {
        require(actualMinutes >= 0) { "O tempo realizado não pode ser negativo." }
        require(plannedMinutes == null || plannedMinutes >= 0) { "O tempo planejado não pode ser negativo." }
        return when {
            plannedMinutes == null -> TopicCompletionWarning.OUTSIDE_ACTIVE_PLAN
            actualMinutes < plannedMinutes -> TopicCompletionWarning.UNDER_PLANNED_TIME
            else -> TopicCompletionWarning.NONE
        }
    }
}
