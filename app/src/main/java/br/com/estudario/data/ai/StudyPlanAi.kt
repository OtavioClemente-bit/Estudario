package br.com.estudario.data.ai

import br.com.estudario.data.local.planner.AvailabilityMode
import br.com.estudario.data.prompt.PlanPromptBuilder
import br.com.estudario.data.prompt.PlanPromptOptions
import br.com.estudario.data.prompt.PlanSubjectInfo
import br.com.estudario.data.transfer.planner.AnnualPhaseDto
import br.com.estudario.data.transfer.planner.PlanCompetitionDto
import br.com.estudario.data.transfer.planner.PlanConfigurationDto
import br.com.estudario.data.transfer.planner.PlanDayDto
import br.com.estudario.data.transfer.planner.PlanSubjectDto
import br.com.estudario.data.transfer.planner.PlanTaskDto
import br.com.estudario.data.transfer.planner.StudyPlanCodec
import br.com.estudario.data.transfer.planner.StudyPlanFileV1
import br.com.estudario.domain.planner.PlanOrigin
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.domain.planner.StudyProfile
import java.time.LocalDate
import java.util.UUID
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Plano de estudo pela IA do Estudário.
 *
 * Para gastar poucos tokens, a IA recebe refs curtas (s1, t12) em vez dos ids do app e devolve
 * tarefas compactas (dia, ref, tipo, minutos, questões). O [Context] guarda tudo o que foi enviado:
 * refs, datas e configuração. Com ele, o resultado vira um .plano idêntico ao de uma IA externa,
 * validado pelo mesmo codificador e importado pelo mesmo caminho, mesmo se o job terminar outro dia.
 */
object StudyPlanAi {
    private const val MAX_DAYS = 120L
    private val json = Json { ignoreUnknownKeys = true }

    data class Prepared(val input: JsonObject, val context: String)

    fun prepare(
        competitionExternalId: String,
        competitionName: String,
        subjects: List<PlanSubjectInfo>,
        o: PlanPromptOptions,
    ): Prepared {
        val end = minOf(PlanPromptBuilder.endDate(o), o.startDate.plusDays(MAX_DAYS - 1))
        var topicCounter = 0
        val refs = buildJsonObject {
            subjects.forEachIndexed { index, subject ->
                val subjectRef = "s${index + 1}"
                put(subjectRef, buildJsonObject {
                    put("id", subject.id)
                    put("name", subject.name)
                    put("priority", (o.priorities[subject.id] ?: PlanPriority.MEDIUM).name)
                })
                subject.topics.forEach { topic ->
                    topicCounter += 1
                    put("t$topicCounter", buildJsonObject {
                        put("id", topic.id)
                        put("title", topic.title)
                        put("subject", subjectRef)
                    })
                }
            }
        }
        topicCounter = 0
        val input = buildJsonObject {
            put("competitionName", competitionName.take(300))
            put("startDate", o.startDate.toString())
            put("endDate", end.toString())
            o.examDate?.let { put("examDate", it.toString()) }
            put("dayMinutes", buildJsonArray { o.dayMinutes.forEach { add(kotlinx.serialization.json.JsonPrimitive(it.coerceIn(0, 16 * 60))) } })
            put("blockMinutes", o.blockMinutes.coerceIn(15, 180))
            put("weeklyQuestions", o.weeklyQuestions.coerceIn(0, 2000))
            put("profile", o.studyProfile.name)
            o.planPreference.trim().takeIf(String::isNotBlank)?.let { put("preference", it.take(800)) }
            put("subjects", buildJsonArray {
                subjects.forEachIndexed { index, subject ->
                    add(buildJsonObject {
                        put("ref", "s${index + 1}")
                        put("name", subject.name.take(300))
                        put("priority", (o.priorities[subject.id] ?: PlanPriority.MEDIUM).name)
                        put("topics", buildJsonArray {
                            subject.topics.forEach { topic ->
                                topicCounter += 1
                                add(buildJsonObject {
                                    put("ref", "t$topicCounter")
                                    put("title", topic.title.take(800))
                                    put("studied", topic.studied)
                                })
                            }
                        })
                    })
                }
            })
        }
        val context = buildJsonObject {
            put("competitionId", competitionExternalId)
            put("competitionName", competitionName)
            put("planName", o.planName.trim().ifBlank { "Meu plano de estudos" })
            put("objective", o.objective.text)
            put("start", o.startDate.toString())
            o.examDate?.let { put("exam", it.toString()) }
            put("dayMinutes", buildJsonArray { o.dayMinutes.forEach { add(kotlinx.serialization.json.JsonPrimitive(it)) } })
            put("block", o.blockMinutes)
            put("weeklyQuestions", o.weeklyQuestions)
            put("monthlyDiscursives", o.monthlyDiscursives)
            put("profile", o.studyProfile.name)
            put("subjectOrder", buildJsonArray { subjects.indices.forEach { add(kotlinx.serialization.json.JsonPrimitive("s${it + 1}")) } })
            put("refs", refs)
        }
        return Prepared(input, context.toString())
    }

    fun toPlano(contextText: String, proposal: JsonObject): String {
        val context = json.parseToJsonElement(contextText).jsonObject
        val refs = context["refs"]!!.jsonObject
        val start = LocalDate.parse(context.text("start"))
        val planId = UUID.nameUUIDFromBytes("${context.text("competitionId")}|${context.text("planName")}|$start|ia-estudario".toByteArray()).toString()
        fun child(value: String) = UUID.nameUUIDFromBytes("$planId:$value".toByteArray()).toString()
        val dayMinutes = context["dayMinutes"]!!.jsonArray.map { it.jsonPrimitive.int }

        val subjects = context["subjectOrder"]!!.jsonArray.mapIndexed { index, element ->
            val ref = refs[element.jsonPrimitive.content]!!.jsonObject
            val priority = PlanPriority.valueOf(ref.text("priority"))
            PlanSubjectDto(
                externalId = ref.text("id"),
                name = ref.text("name"),
                priority = priority,
                maintenanceMinutes = when (priority) { PlanPriority.CRITICAL -> 90; PlanPriority.HIGH -> 60; PlanPriority.MEDIUM -> 45; PlanPriority.LOW -> 30 },
                paused = false,
                position = index,
            )
        }
        val phases = proposal["phases"]?.jsonArray.orEmpty().mapIndexed { index, element ->
            val phase = element.jsonObject
            AnnualPhaseDto(
                id = child("fase-${index + 1}"),
                name = phase.text("name"),
                objective = phase.text("objective"),
                completionCriteria = "",
                startDate = start.plusDays(phase["fromDay"]!!.jsonPrimitive.int.toLong()),
                endDate = start.plusDays(phase["toDay"]!!.jsonPrimitive.int.toLong()),
                targetMinutes = 0, targetQuestions = 0, targetDiscursives = 0, targetPercent = 0,
            )
        }
        val tasks = proposal["tasks"]?.jsonArray.orEmpty().mapIndexed { index, element ->
            val task = element.jsonObject
            val subjectRef = task["s"]?.jsonPrimitive?.contentOrNull?.let { refs[it]?.jsonObject }
            val topicRef = task["t"]?.jsonPrimitive?.contentOrNull?.let { refs[it]?.jsonObject }
            val type = PlanTaskType.valueOf(task.text("k"))
            PlanTaskDto(
                id = child("tarefa-${index + 1}"),
                subjectExternalId = subjectRef?.text("id"),
                topicExternalId = topicRef?.text("id"),
                subjectName = subjectRef?.text("name") ?: "Simulado",
                topicName = topicRef?.text("title"),
                date = start.plusDays(task["d"]!!.jsonPrimitive.int.toLong()),
                type = type,
                minutes = task["m"]!!.jsonPrimitive.int,
                questions = task["q"]?.jsonPrimitive?.intOrNull ?: 0,
                priority = subjectRef?.let { PlanPriority.valueOf(it.text("priority")) } ?: PlanPriority.HIGH,
                status = PlanTaskStatus.PLANEJADA,
                origin = PlanOrigin.IMPORTED,
                locked = false,
                notes = "",
                dependencies = emptyList(),
            )
        }
        val plan = StudyPlanFileV1(
            planId = planId,
            competition = PlanCompetitionDto(context.text("competitionId"), context.text("competitionName")),
            name = context.text("planName"),
            objective = context.text("objective"),
            active = false,
            masterPlan = false,
            startDate = start,
            examDate = context["exam"]?.jsonPrimitive?.contentOrNull?.let(LocalDate::parse),
            baseRevision = null,
            configuration = PlanConfigurationDto(
                mode = if (dayMinutes.distinct().size <= 1) AvailabilityMode.SIMPLE else AvailabilityMode.ADVANCED,
                days = dayMinutes.mapIndexed { index, minutes -> PlanDayDto(index + 1, minutes, minutes == 0) },
                weeklyQuestions = context["weeklyQuestions"]!!.jsonPrimitive.int,
                monthlyDiscursives = context["monthlyDiscursives"]!!.jsonPrimitive.int,
                blockMinutes = context["block"]!!.jsonPrimitive.int,
                profile = StudyProfile.valueOf(context.text("profile")),
            ),
            subjects = subjects,
            annualPhases = phases,
            monthlyPlans = emptyList(),
            weeklyPlans = emptyList(),
            tasks = tasks,
            // A estratégia explicada pela IA viaja com o plano e aparece nas premissas.
            metadata = mapOf("premissas" to proposal.text("summary"), "origem" to "IA do Estudário"),
        )
        return StudyPlanCodec().encode(plan)
    }

    private fun JsonObject.text(name: String): String = this[name]?.jsonPrimitive?.contentOrNull.orEmpty()
    private fun JsonArray?.orEmpty(): List<kotlinx.serialization.json.JsonElement> = this ?: emptyList()
}
