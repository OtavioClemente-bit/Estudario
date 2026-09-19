package br.com.estudario.data.transfer.planner

import java.util.UUID

class StudyPlanValidationException(message: String) : IllegalArgumentException(message)

internal object StudyPlanValidation {
    fun validate(plan: StudyPlanFileV1) {
        uuid(plan.planId, "planId")
        requireText(plan.competition.externalId, "concurso.externalId")
        requireText(plan.competition.name, "concurso.nome")
        requireText(plan.name, "nome")
        requireText(plan.objective, "objetivo")
        if (plan.examDate != null && plan.examDate < plan.startDate) fail("dataProva", "não pode ser anterior à data de início")
        if (plan.configuration.days.map { it.day }.distinct().size != plan.configuration.days.size) fail("configuracao.dias", "dias duplicados")
        plan.configuration.days.forEachIndexed { index, day ->
            if (day.day !in 1..7) fail("configuracao.dias[$index].dia", "deve estar entre 1 e 7")
            nonNegative(day.minutes, "configuracao.dias[$index].minutos")
        }
        nonNegative(plan.configuration.weeklyQuestions, "configuracao.questoesSemanais")
        nonNegative(plan.configuration.monthlyDiscursives, "configuracao.discursivasMensais")
        plan.subjects.forEachIndexed { index, subject ->
            requireText(subject.externalId, "prioridades[$index].externalId")
            nonNegative(subject.maintenanceMinutes, "prioridades[$index].manutencaoMinutos")
        }
        val ids = linkedSetOf<String>()
        fun register(id: String, path: String) {
            uuid(id, path)
            if (!ids.add(id)) fail(path, "UUID duplicado")
        }
        plan.annualPhases.forEachIndexed { index, phase ->
            register(phase.id, "fasesAnuais[$index].id")
            if (phase.endDate < phase.startDate) fail("fasesAnuais[$index].fim", "não pode ser anterior ao início")
            nonNegative(phase.targetMinutes, "fasesAnuais[$index].metaMinutos")
        }
        plan.monthlyPlans.forEachIndexed { index, month -> register(month.id, "planosMensais[$index].id") }
        plan.weeklyPlans.forEachIndexed { index, week -> register(week.id, "planosSemanais[$index].id") }
        plan.tasks.forEachIndexed { index, task ->
            register(task.id, "tarefas[$index].id")
            nonNegative(task.minutes, "tarefas[$index].minutos")
            nonNegative(task.questions, "tarefas[$index].questoes")
            if (task.id in task.dependencies) fail("tarefas[$index].dependencias", "uma tarefa não pode depender de si mesma")
        }
        val taskIds = plan.tasks.mapTo(hashSetOf()) { it.id }
        plan.tasks.forEachIndexed { index, task ->
            task.dependencies.forEach { dependency ->
                if (dependency !in taskIds) fail("tarefas[$index].dependencias", "tarefa inexistente: $dependency")
            }
        }
        detectCycles(plan.tasks)
    }

    private fun detectCycles(tasks: List<PlanTaskDto>) {
        val dependencies = tasks.associate { it.id to it.dependencies }
        val visiting = hashSetOf<String>()
        val visited = hashSetOf<String>()
        fun visit(id: String) {
            if (id in visited) return
            if (!visiting.add(id)) fail("tarefas.dependencias", "ciclo detectado")
            dependencies[id].orEmpty().forEach(::visit)
            visiting.remove(id)
            visited.add(id)
        }
        dependencies.keys.forEach(::visit)
    }

    private fun uuid(value: String, path: String) {
        try { UUID.fromString(value) } catch (_: Exception) { fail(path, "UUID inválido") }
    }
    private fun requireText(value: String, path: String) { if (value.isBlank()) fail(path, "campo obrigatório vazio") }
    private fun nonNegative(value: Int, path: String) { if (value < 0) fail(path, "não pode ser negativo") }
    private fun fail(path: String, message: String): Nothing = throw StudyPlanValidationException("$path: $message.")
}
