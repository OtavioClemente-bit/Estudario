package br.com.estudario.data.transfer.planner

import org.json.JSONArray
import org.json.JSONObject

data class ContextSubject(val name: String, val priority: String, val plannedMinutes: Int, val actualMinutes: Int)
data class ContextWeakTopic(val name: String, val accuracyPercent: Int, val sampleSize: Int)
data class ContextTask(val subject: String, val topic: String?, val type: String, val minutes: Int, val date: String)
data class PlanContext(
    val competition: String,
    val objective: String,
    val annualPhase: String?,
    val currentMonth: String,
    val currentWeek: String,
    val weeklyCapacityMinutes: Int,
    val plannedMinutes: Int,
    val actualMinutes: Int,
    val questions: Int,
    val correct: Int,
    val adherencePercent: Int,
    val capacityDeficitMinutes: Int,
    val subjects: List<ContextSubject>,
    val weakTopics: List<ContextWeakTopic>,
    val missedTasks: List<ContextTask>,
    val futureTasks: List<ContextTask>,
    val alerts: List<String>,
)

object PlanContextExporter {
    fun exportJson(context: PlanContext): String = JSONObject()
        .put("format", "estudario-plan-context")
        .put("version", 1)
        .put("competition", context.competition)
        .put("objective", context.objective)
        .put("annualPhase", context.annualPhase ?: JSONObject.NULL)
        .put("currentMonth", context.currentMonth)
        .put("currentWeek", context.currentWeek)
        .put("weeklyCapacityMinutes", context.weeklyCapacityMinutes)
        .put("progress", JSONObject()
            .put("plannedMinutes", context.plannedMinutes)
            .put("actualMinutes", context.actualMinutes)
            .put("adherencePercent", context.adherencePercent)
            .put("questions", context.questions)
            .put("correct", context.correct)
            .put("accuracyPercent", if (context.questions == 0) JSONObject.NULL else context.correct * 100 / context.questions))
        .put("capacityDeficitMinutes", context.capacityDeficitMinutes)
        .put("subjects", JSONArray(context.subjects.map { JSONObject().put("name", it.name).put("priority", it.priority).put("plannedMinutes", it.plannedMinutes).put("actualMinutes", it.actualMinutes) }))
        .put("weakTopics", JSONArray(context.weakTopics.map { JSONObject().put("name", it.name).put("accuracyPercent", it.accuracyPercent).put("sampleSize", it.sampleSize) }))
        .put("missedTasks", JSONArray(context.missedTasks.map(::taskJson)))
        .put("futureTasks", JSONArray(context.futureTasks.map(::taskJson)))
        .put("alerts", JSONArray(context.alerts))
        .toString()

    fun exportText(context: PlanContext): String = buildString {
        appendLine("Concurso: ${context.competition}")
        appendLine("Objetivo: ${context.objective}")
        context.annualPhase?.let { appendLine("Fase anual: $it") }
        appendLine("Mês: ${context.currentMonth} | Semana: ${context.currentWeek}")
        appendLine("Capacidade: ${context.weeklyCapacityMinutes} min/semana")
        appendLine("Planejado: ${context.plannedMinutes} min | Realizado: ${context.actualMinutes} min | Aderência: ${context.adherencePercent}%")
        appendLine("Questões: ${context.questions} | Acertos: ${context.correct}")
        appendLine("Déficit: ${context.capacityDeficitMinutes} min")
        if (context.weakTopics.isNotEmpty()) appendLine("Tópicos fracos: " + context.weakTopics.joinToString { "${it.name} ${it.accuracyPercent}% (${it.sampleSize})" })
        if (context.missedTasks.isNotEmpty()) appendLine("Não realizadas: " + context.missedTasks.joinToString { "${it.subject}/${it.topic.orEmpty()} ${it.minutes} min" })
        if (context.futureTasks.isNotEmpty()) appendLine("Próximas: " + context.futureTasks.joinToString { "${it.date} ${it.subject}/${it.topic.orEmpty()} ${it.type} ${it.minutes} min" })
        if (context.alerts.isNotEmpty()) appendLine("Alertas: " + context.alerts.joinToString())
    }.trim()

    private fun taskJson(task: ContextTask) = JSONObject()
        .put("subject", task.subject)
        .put("topic", task.topic ?: JSONObject.NULL)
        .put("type", task.type)
        .put("minutes", task.minutes)
        .put("date", task.date)
}
