package br.com.estudario.ui.planner

import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val portugueseFullDateFormatter =
    DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))

fun PlanTaskType.displayNamePtBr(): String = when (this) {
    PlanTaskType.THEORY -> "Teoria"
    PlanTaskType.QUESTIONS -> "Questões"
    PlanTaskType.REVIEW -> "Revisão"
    PlanTaskType.ACTIVE_RECALL -> "Recordação ativa"
    PlanTaskType.FLASHCARDS -> "Flashcards"
    PlanTaskType.SIMULATION -> "Simulado"
    PlanTaskType.DISCURSIVE -> "Discursiva"
}

/**
 * O nome da tarefa como uma ordem clara. Com tópico, é o tópico; sem tópico (rodízio da matéria),
 * diz o que fazer em vez de "Sessão de estudos": "Questões de Português · 20 questões".
 */
fun br.com.estudario.data.local.planner.PlanTaskEntity.taskTitlePtBr(): String {
    topicNameSnapshot?.takeIf { it.isNotBlank() }?.let { return it }
    val subject = subjectNameSnapshot.ifBlank { "todas as matérias" }
    val base = when (type) {
        PlanTaskType.THEORY -> "Teoria de $subject"
        PlanTaskType.QUESTIONS -> "Questões de $subject"
        PlanTaskType.REVIEW -> "Revisão de $subject"
        PlanTaskType.ACTIVE_RECALL -> "Recordação ativa de $subject"
        PlanTaskType.FLASHCARDS -> "Flashcards de $subject"
        PlanTaskType.SIMULATION -> "Simulado de $subject"
        PlanTaskType.DISCURSIVE -> "Discursiva de $subject"
    }
    return if (plannedQuestions > 0 && type == PlanTaskType.QUESTIONS) "$base · $plannedQuestions questões" else base
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

fun forecastDateLabelPtBr(date: LocalDate): String = date.format(portugueseFullDateFormatter)
