package br.com.estudario.ui.planner

import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType

fun PlanTaskType.displayNamePtBr(): String = when (this) {
    PlanTaskType.THEORY -> "Teoria"
    PlanTaskType.QUESTIONS -> "Questões"
    PlanTaskType.REVIEW -> "Revisão"
    PlanTaskType.ACTIVE_RECALL -> "Recordação ativa"
    PlanTaskType.FLASHCARDS -> "Flashcards"
    PlanTaskType.SIMULATION -> "Simulado"
    PlanTaskType.DISCURSIVE -> "Discursiva"
}

fun PlanTaskStatus.displayNamePtBr(): String = when (this) {
    PlanTaskStatus.PLANEJADA -> "Planejada"
    PlanTaskStatus.EM_ANDAMENTO -> "Em andamento"
    PlanTaskStatus.CONCLUIDA -> "Concluída"
    PlanTaskStatus.REPROGRAMADA -> "Reprogramada"
    PlanTaskStatus.NAO_REALIZADA -> "Não realizada"
    PlanTaskStatus.PAUSADA -> "Pausada"
}

fun PlanPriority.displayNamePtBr(): String = when (this) {
    PlanPriority.CRITICAL -> "Muito alta"
    PlanPriority.HIGH -> "Alta"
    PlanPriority.MEDIUM -> "Média"
    PlanPriority.LOW -> "Baixa"
}

fun completionActionPtBr(status: PlanTaskStatus): String = when (status) {
    PlanTaskStatus.PLANEJADA -> "Começar"
    PlanTaskStatus.EM_ANDAMENTO -> "Continuar"
    PlanTaskStatus.CONCLUIDA -> "Concluída"
    PlanTaskStatus.REPROGRAMADA -> "Continuar"
    PlanTaskStatus.NAO_REALIZADA -> "Reprogramar"
    PlanTaskStatus.PAUSADA -> "Retomar"
}

fun minutesLabelPtBr(value: Int): String {
    val minutes = value.coerceAtLeast(0)
    val hours = minutes / 60
    val remainder = minutes % 60
    return when {
        hours == 0 -> "$minutes min"
        remainder == 0 -> "${hours}h"
        else -> "${hours}h ${remainder}min"
    }
}
