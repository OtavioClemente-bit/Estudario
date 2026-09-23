package br.com.estudario.data.transfer.planner

import br.com.estudario.data.local.planner.AvailabilityMode
import br.com.estudario.domain.planner.PlanOrigin
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.domain.planner.StudyProfile
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

class StudyPlanCodec {
    fun decode(text: String): StudyPlanFileV1 {
        val root = try { JSONObject(text) } catch (_: Exception) {
            throw StudyPlanValidationException("O arquivo .plano não contém JSON válido.")
        }
        val format = root.optString("format")
        // Arquivos gerados antes da troca de nome continuam valendo: o app escreve o formato novo
        // e aceita os dois na leitura.
        if (format !in ACCEPTED && !(format.isEmpty() && root.has("planId") && root.has("tarefas"))) {
            throw StudyPlanValidationException("Formato inválido. Esperado: $FORMAT.")
        }
        val version = root.optInt("version", if (format.isEmpty()) VERSION else -1)
        if (version != VERSION) throw StudyPlanValidationException("Versão .plano não suportada: $version. Este aplicativo aceita a versão 1.")
        val competition = root.requireObject("concurso")
        val configuration = root.requireObject("configuracao")
        val plan = StudyPlanFileV1(
            planId = root.requireText("planId", "planId"),
            competition = PlanCompetitionDto(competition.requireText("externalId", "concurso.externalId"), competition.requireText("nome", "concurso.nome")),
            name = root.requireText("nome", "nome"),
            objective = root.requireText("objetivo", "objetivo"),
            active = root.optBoolean("active", false),
            masterPlan = root.optBoolean("masterPlan", false),
            startDate = date(root.requireText("dataInicio", "dataInicio"), "dataInicio"),
            examDate = root.optionalText("dataProva")?.let { date(it, "dataProva") },
            baseRevision = if (root.has("baseRevision") && !root.isNull("baseRevision")) root.getLong("baseRevision") else null,
            configuration = PlanConfigurationDto(
                mode = enum(configuration.requireText("modo", "configuracao.modo"), "configuracao.modo"),
                days = configuration.array("dias").objects().mapIndexed { index, day ->
                    PlanDayDto(day.requireInt("dia", "configuracao.dias[$index].dia"), day.requireInt("minutos", "configuracao.dias[$index].minutos"), day.optBoolean("indisponivel", false))
                },
                weeklyQuestions = configuration.optInt("questoesSemanais", 0),
                monthlyDiscursives = configuration.optInt("discursivasMensais", 0),
                blockMinutes = if (configuration.has("blocoMinutos")) configuration.requireInt("blocoMinutos", "configuracao.blocoMinutos") else 50,
                profile = if (configuration.has("perfil")) enum(configuration.requireText("perfil", "configuracao.perfil"), "configuracao.perfil") else StudyProfile.DO_ZERO,
            ),
            subjects = root.array("prioridades").objects().mapIndexed { index, item ->
                PlanSubjectDto(
                    externalId = item.requireText("externalId", "prioridades[$index].externalId"),
                    name = item.requireText("nome", "prioridades[$index].nome"),
                    priority = enum(item.requireText("prioridade", "prioridades[$index].prioridade"), "prioridades[$index].prioridade"),
                    maintenanceMinutes = item.optInt("manutencaoMinutos", 0),
                    paused = item.optBoolean("pausada", false),
                    position = item.optInt("posicao", index),
                )
            },
            annualPhases = root.array("fasesAnuais").objects().mapIndexed { index, item -> parseAnnual(item, index) },
            monthlyPlans = root.array("planosMensais").objects().mapIndexed { index, item -> parseMonth(item, index) },
            weeklyPlans = root.array("planosSemanais").objects().mapIndexed { index, item -> parseWeek(item, index) },
            tasks = root.array("tarefas").objects().mapIndexed { index, item -> parseTask(item, index) },
            metadata = root.optJSONObject("metadata")?.let { metadata -> metadata.keys().asSequence().associateWith { metadata.optString(it) } }.orEmpty(),
        )
        val normalized = normalizeIdentifiers(plan)
        StudyPlanValidation.validate(normalized)
        return normalized
    }

    /**
     * IAs raramente produzem UUIDs válidos para dezenas de tarefas ("tarefa-01", UUIDs com letras
     * fora do intervalo a até f...). IDs que não são UUID viram UUIDs determinísticos derivados do texto original,
     * sempre no escopo do plano, o mesmo arquivo gera os mesmos IDs, e dependências continuam
     * apontando para as tarefas certas. Duplicados continuam duplicados e são rejeitados.
     */
    private fun normalizeIdentifiers(plan: StudyPlanFileV1): StudyPlanFileV1 {
        val planId = asUuid(plan.planId, "plan")
        fun child(id: String) = asUuid(id, "$planId:")
        return plan.copy(
            planId = planId,
            annualPhases = plan.annualPhases.map { it.copy(id = child(it.id)) },
            monthlyPlans = plan.monthlyPlans.map { it.copy(id = child(it.id)) },
            weeklyPlans = plan.weeklyPlans.map { it.copy(id = child(it.id)) },
            tasks = plan.tasks.map { task -> task.copy(id = child(task.id), dependencies = task.dependencies.map(::child)) },
        )
    }

    private fun asUuid(value: String, namespace: String): String {
        val parsed = runCatching { java.util.UUID.fromString(value) }.getOrNull()
        if (parsed != null && parsed.toString().equals(value, ignoreCase = true)) return value
        return java.util.UUID.nameUUIDFromBytes("$namespace$value".toByteArray(Charsets.UTF_8)).toString()
    }

    fun encode(plan: StudyPlanFileV1): String {
        StudyPlanValidation.validate(plan)
        return JSONObject()
            .put("format", FORMAT).put("version", VERSION).put("planId", plan.planId)
            .put("concurso", JSONObject().put("externalId", plan.competition.externalId).put("nome", plan.competition.name))
            .put("nome", plan.name).put("objetivo", plan.objective).put("active", plan.active).put("masterPlan", plan.masterPlan)
            .put("dataInicio", plan.startDate.toString()).put("dataProva", plan.examDate?.toString() ?: JSONObject.NULL)
            .put("baseRevision", plan.baseRevision ?: JSONObject.NULL)
            .put("configuracao", JSONObject().put("modo", plan.configuration.mode.name)
                .put("dias", JSONArray(plan.configuration.days.sortedBy { it.day }.map { JSONObject().put("dia", it.day).put("minutos", it.minutes).put("indisponivel", it.unavailable) }))
                .put("questoesSemanais", plan.configuration.weeklyQuestions).put("discursivasMensais", plan.configuration.monthlyDiscursives)
                .put("blocoMinutos", plan.configuration.blockMinutes).put("perfil", plan.configuration.profile.name))
            .put("prioridades", JSONArray(plan.subjects.sortedBy { it.position }.map { subjectJson(it) }))
            .put("fasesAnuais", JSONArray(plan.annualPhases.map { annualJson(it) }))
            .put("planosMensais", JSONArray(plan.monthlyPlans.map { monthJson(it) }))
            .put("planosSemanais", JSONArray(plan.weeklyPlans.map { weekJson(it) }))
            .put("tarefas", JSONArray(plan.tasks.sortedWith(compareBy<PlanTaskDto>({ it.date }, { it.id })).map { taskJson(it) }))
            .put("metadata", JSONObject(plan.metadata.toSortedMap()))
            .toString(2)
    }

    private fun parseAnnual(item: JSONObject, index: Int) = AnnualPhaseDto(
        item.requireText("id", "fasesAnuais[$index].id"), item.requireText("nome", "fasesAnuais[$index].nome"), item.optString("objetivo"), item.optString("criterioConclusao"),
        date(item.requireText("inicio", "fasesAnuais[$index].inicio"), "fasesAnuais[$index].inicio"), date(item.requireText("fim", "fasesAnuais[$index].fim"), "fasesAnuais[$index].fim"),
        item.optInt("metaMinutos", 0), item.optInt("metaQuestoes", 0), item.optInt("metaDiscursivas", 0), item.optInt("metaPercentual", 0),
    )
    private fun parseMonth(item: JSONObject, index: Int) = MonthlyPlanDto(item.requireText("id", "planosMensais[$index].id"), item.requireText("mes", "planosMensais[$index].mes"), item.optString("foco"), item.optInt("metaMinutos", 0), item.optInt("metaQuestoes", 0), item.optInt("metaDiscursivas", 0), item.optInt("metaPercentual", 0))
    private fun parseWeek(item: JSONObject, index: Int) = WeeklyPlanDto(item.requireText("id", "planosSemanais[$index].id"), date(item.requireText("inicio", "planosSemanais[$index].inicio"), "planosSemanais[$index].inicio"), item.optString("objetivo"), item.optInt("metaMinutos", 0), item.optInt("metaQuestoes", 0), item.optInt("metaDiscursivas", 0))
    private fun parseTask(item: JSONObject, index: Int) = PlanTaskDto(
        id = item.requireText("id", "tarefas[$index].id"), subjectExternalId = item.optionalText("materiaExternalId"), topicExternalId = item.optionalText("topicoExternalId"),
        subjectName = item.optionalText("materiaNome"), topicName = item.optionalText("topicoNome"), date = date(item.requireText("data", "tarefas[$index].data"), "tarefas[$index].data"),
        type = enum(item.requireText("tipo", "tarefas[$index].tipo"), "tarefas[$index].tipo"), minutes = item.requireInt("minutos", "tarefas[$index].minutos"), questions = item.optInt("questoes", 0),
        priority = enum(item.requireText("prioridade", "tarefas[$index].prioridade"), "tarefas[$index].prioridade"), status = enum(item.optString("status", PlanTaskStatus.PLANEJADA.name), "tarefas[$index].status"),
        origin = enum(item.optString("origem", PlanOrigin.IMPORTED.name), "tarefas[$index].origem"), locked = item.optBoolean("locked", false), notes = item.optString("observacoes"),
        dependencies = item.array("dependencias").strings(),
    )

    private fun subjectJson(it: PlanSubjectDto) = JSONObject().put("externalId", it.externalId).put("nome", it.name).put("prioridade", it.priority.name).put("manutencaoMinutos", it.maintenanceMinutes).put("pausada", it.paused).put("posicao", it.position)
    private fun annualJson(it: AnnualPhaseDto) = JSONObject().put("id", it.id).put("nome", it.name).put("objetivo", it.objective).put("criterioConclusao", it.completionCriteria).put("inicio", it.startDate.toString()).put("fim", it.endDate.toString()).put("metaMinutos", it.targetMinutes).put("metaQuestoes", it.targetQuestions).put("metaDiscursivas", it.targetDiscursives).put("metaPercentual", it.targetPercent)
    private fun monthJson(it: MonthlyPlanDto) = JSONObject().put("id", it.id).put("mes", it.yearMonth).put("foco", it.focus).put("metaMinutos", it.targetMinutes).put("metaQuestoes", it.targetQuestions).put("metaDiscursivas", it.targetDiscursives).put("metaPercentual", it.targetPercent)
    private fun weekJson(it: WeeklyPlanDto) = JSONObject().put("id", it.id).put("inicio", it.weekStart.toString()).put("objetivo", it.objective).put("metaMinutos", it.targetMinutes).put("metaQuestoes", it.targetQuestions).put("metaDiscursivas", it.targetDiscursives)
    private fun taskJson(it: PlanTaskDto) = JSONObject().put("id", it.id).putNullable("materiaExternalId", it.subjectExternalId).putNullable("topicoExternalId", it.topicExternalId).putNullable("materiaNome", it.subjectName).putNullable("topicoNome", it.topicName).put("data", it.date.toString()).put("tipo", it.type.name).put("minutos", it.minutes).put("questoes", it.questions).put("prioridade", it.priority.name).put("status", it.status.name).put("origem", it.origin.name).put("locked", it.locked).put("observacoes", it.notes).put("dependencias", JSONArray(it.dependencies))

    private fun date(value: String, path: String): LocalDate = try { LocalDate.parse(value) } catch (_: Exception) { throw StudyPlanValidationException("$path: data inválida.") }
    private inline fun <reified T : Enum<T>> enum(value: String, path: String): T = try { enumValueOf<T>(value) } catch (_: Exception) { throw StudyPlanValidationException("$path: valor desconhecido: $value.") }

    companion object {
        const val FORMAT = "estudario-plano"
        const val LEGACY_FORMAT = "estudario-plano"
        const val VERSION = 1
        val ACCEPTED = setOf(FORMAT, LEGACY_FORMAT, "plano")
    }
}

private fun JSONObject.requireObject(key: String): JSONObject = optJSONObject(key) ?: throw StudyPlanValidationException("$key: objeto obrigatório ausente.")
private fun JSONObject.requireText(key: String, path: String): String = optionalText(key) ?: throw StudyPlanValidationException("$path: campo obrigatório ausente ou vazio.")
private fun JSONObject.optionalText(key: String): String? = if (!has(key) || isNull(key)) null else optString(key).trim().takeIf { it.isNotEmpty() }
private fun JSONObject.requireInt(key: String, path: String): Int = if (has(key) && !isNull(key)) try { getInt(key) } catch (_: Exception) { throw StudyPlanValidationException("$path: inteiro inválido.") } else throw StudyPlanValidationException("$path: campo obrigatório ausente.")
private fun JSONObject.array(key: String): JSONArray = optJSONArray(key) ?: JSONArray()
private fun JSONArray.objects(): List<JSONObject> = (0 until length()).map { index -> optJSONObject(index) ?: throw StudyPlanValidationException("Item $index deve ser um objeto.") }
private fun JSONArray.strings(): List<String> = (0 until length()).map { getString(it) }
private fun JSONObject.putNullable(key: String, value: String?): JSONObject = put(key, value ?: JSONObject.NULL)
