package br.com.estudario.web.data

import br.com.estudario.domain.simulation.BlueprintTopic
import br.com.estudario.domain.simulation.BoardStyle
import br.com.estudario.domain.simulation.SimulationBlueprint
import br.com.estudario.domain.simulation.SimulationMode
import br.com.estudario.domain.simulation.SimulationReadiness
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.put
import kotlin.js.Date
import kotlin.random.Random

/** Uma parte da geração: até 30 questões, um job no servidor (mesmo formato do app, em partsJson). */
data class SimPart(
    val key: String,
    val input: JsonObject,
    val refs: Map<String, Long>,
    val jobId: String? = null,
    val status: String = "PENDING",
    val error: String? = null,
)

data class SubjectScore(val subjectName: String, val total: Int, val correct: Int) {
    val percent: Int get() = if (total == 0) 0 else correct * 100 / total
}

/**
 * Simulados no site, com as regras do SimulationService do app: planta pelo SimulationBlueprint
 * (módulo comum), geração em partes (no máximo duas ao mesmo tempo), questões guardadas fora do
 * banco até a entrega, e a correção que alimenta caderno de erros e estatísticas.
 */
object Simulations {
    private val scope: CoroutineScope = MainScope()
    private val running = HashSet<Long>()

    private fun now() = Date.now().toLong()
    private fun JsonObject.long(key: String): Long? = (this[key] as? JsonPrimitive)?.longOrNull
    private fun JsonObject.int(key: String): Int? = (this[key] as? JsonPrimitive)?.intOrNull
    private fun JsonElement?.text(): String = (this as? JsonPrimitive)?.contentOrNull.orEmpty()

    fun row(data: Snapshot, id: Long): JsonObject? = data.array(Keys.SIMULATIONS).firstOrNull { (it as? JsonObject)?.long("id") == id } as? JsonObject

    // ---------------------------------------------------------------- prontidão

    fun leafTopics(data: Snapshot, competitionId: Long): List<BlueprintTopic> {
        val subjects = data.subjects.filter { it.competitionId == competitionId }.sortedBy { it.position }
        val subjectIds = subjects.mapTo(hashSetOf()) { it.id }
        val topicRows = data.array(Keys.TOPICS).mapNotNull { it as? JsonObject }.filter { (it.long("subjectId") ?: -1) in subjectIds }
        val topics = data.topics.filter { it.subjectId in subjectIds }
        val parents = topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val withTheory = data.theories.mapTo(hashSetOf()) { it.topicId }
        val byId = topics.associateBy { it.id }
        val order = subjects.withIndex().associate { it.value.id to it.index }
        fun path(topic: Topic): List<String> {
            val out = ArrayDeque<String>()
            var current: Topic? = topic
            val seen = HashSet<Long>()
            while (current != null && seen.add(current.id)) { out.addFirst(current.title); current = current.parentTopicId?.let { byId[it] } }
            return out.toList()
        }
        return topics.filter { it.id !in parents }.sortedWith(compareBy({ order[it.subjectId] ?: 0 }, { it.position })).map { topic ->
            val raw = topicRows.firstOrNull { it.long("id") == topic.id }
            BlueprintTopic(
                topicId = topic.id,
                subjectId = topic.subjectId,
                subjectName = subjects.first { it.id == topic.subjectId }.name,
                path = path(topic),
                weight = raw?.int("assessedPriorityScore") ?: 50,
                studied = topic.status != "NAO_ESTUDADO",
                hasContent = topic.id in withTheory,
                scope = raw?.str("scopeCovers"),
            )
        }
    }

    fun readiness(data: Snapshot, competitionId: Long): SimulationReadiness {
        val leaves = leafTopics(data, competitionId)
        val days = (data.attempts.map { it.answeredAt } + data.sessions.map { it.completedAt }).mapTo(hashSetOf()) { Queries.dateOf(it) }
        val finished = data.simulations.filter { it.competitionId == competitionId && it.status == "FINISHED" }
        return SimulationReadiness(
            leafTopics = leaves.size,
            studiedTopics = leaves.count { it.studied },
            coveredTopics = leaves.count { it.studied || it.hasContent },
            studyDays = days.size,
            wrongInFinished = finished.sumOf { it.questionCount - it.correctCount },
        )
    }

    data class ExamProfile(val board: String?, val style: String, val targetPercent: Int)

    fun examProfile(data: Snapshot, competitionId: Long): ExamProfile {
        val row = data.array("examProfiles").firstOrNull { (it as? JsonObject)?.long("competitionId") == competitionId } as? JsonObject
        return ExamProfile(row?.str("board"), row?.str("style") ?: "FIVE_OPTIONS", row?.int("targetPercent") ?: 70)
    }

    fun saveExamProfile(data: Snapshot, competitionId: Long, board: String?, targetPercent: Int): Snapshot {
        val cleaned = board?.trim()?.ifBlank { null }
        val row = jsonOf(
            "competitionId" to competitionId, "board" to cleaned, "boardSource" to if (cleaned == null) null else "USER", "boardUrl" to null,
            "boardPending" to false, "style" to BoardStyle.styleFor(cleaned), "targetPercent" to targetPercent.coerceIn(30, 100), "updatedAt" to now(),
        )
        return data.edit("examProfiles") { if (it.long("competitionId") == competitionId) null else it }.append("examProfiles", row)
    }

    // ---------------------------------------------------------------- criação

    private fun encodeParts(parts: List<SimPart>): String = JsonArray(parts.map { part ->
        jsonOf(
            "key" to part.key, "input" to part.input, "refs" to JsonObject(part.refs.mapValues { JsonPrimitive(it.value) }),
            "jobId" to part.jobId, "status" to part.status, "error" to part.error,
        )
    }).toString()

    fun decodeParts(text: String?): List<SimPart> = runCatching {
        snapshotJson.parseToJsonElement(text ?: "[]").jsonArray.map { element ->
            val obj = element.jsonObject
            SimPart(
                key = obj["key"].text(),
                input = obj["input"] as? JsonObject ?: JsonObject(emptyMap()),
                refs = (obj["refs"] as? JsonObject).orEmpty().mapValues { (it.value as? JsonPrimitive)?.longOrNull ?: 0L },
                jobId = obj["jobId"].text().ifBlank { null },
                status = obj["status"].text().ifBlank { "PENDING" },
                error = obj["error"].text().ifBlank { null },
            )
        }
    }.getOrDefault(emptyList())

    private fun weakSpots(data: Snapshot, topicIds: Set<Long>): List<String> {
        val concepts = data.errorConcepts.filter { it.topicId in topicIds }.associateBy { it.id }
        val counts = data.errorConceptEntries.groupingBy { it.conceptId }.eachCount()
        return counts.entries.filter { it.key in concepts }.sortedByDescending { it.value }.take(8).map { entry ->
            val concept = concepts.getValue(entry.key)
            listOf(concept.title, concept.summary).filter(String::isNotBlank).joinToString(": ").replace(Regex("\\s+"), " ").take(240)
        }
    }

    /** Questões erradas nos simulados entregues e as abertas no caderno de erros (Revanche). */
    fun rematchSources(data: Snapshot, competitionId: Long): List<Question> {
        val wrong = LinkedHashSet<Long>()
        data.simulations.filter { it.competitionId == competitionId && it.status == "FINISHED" }.sortedByDescending { it.finishedAt ?: 0 }.forEach { simulation ->
            val answers = answersOf(row(data, simulation.id))
            data.questions.filter { it.simulationId == simulation.id }.forEach { q -> if (answers[q.id] != q.options.firstOrNull { it.correct }?.key) wrong += q.id }
        }
        data.errors.filter { it.status == "NOVO" || it.status == "RECORRENTE" }.forEach { wrong += it.questionId }
        val leafIds = leafTopics(data, competitionId).mapTo(hashSetOf()) { it.topicId }
        val byId = data.questions.associateBy { it.id }
        return wrong.mapNotNull { byId[it] }.filter { it.topicId in leafIds && it.options.size in 2..5 && it.options.count { o -> o.correct } == 1 }
    }

    /** Monta a planta e grava o simulado; devolve (foto, id). A geração começa com [generate]. */
    fun create(data: Snapshot, competitionId: Long, mode: SimulationMode, size: Int): Pair<Snapshot, Long> {
        val competition = data.competitions.firstOrNull { it.id == competitionId } ?: error("Concurso não encontrado.")
        val profile = examProfile(data, competitionId)
        val leaves = leafTopics(data, competitionId)
        val weak = weakSpots(data, leaves.mapTo(hashSetOf()) { it.topicId })
        val base = buildJsonObject {
            put("competitionName", competition.name.take(300))
            profile.board?.let { put("board", it.take(80)) }
            put("mode", mode.name)
            put("style", profile.style)
            put("detectBoard", profile.board == null)
            put("weakSpots", JsonArray(weak.map { JsonPrimitive(it) }))
        }
        val parts: List<SimPart> = if (mode == SimulationMode.REMATCH) {
            val sources = rematchSources(data, competitionId).take(SimulationBlueprint.MAX_PER_PART)
            if (sources.isEmpty()) emptyList() else listOf(
                SimPart(
                    key = "",
                    refs = sources.mapIndexed { index, q -> "r${index + 1}" to q.topicId }.toMap(),
                    input = buildJsonObject {
                        base.forEach { (k, v) -> put(k, v) }
                        put("partIndex", 0); put("partCount", 1); put("items", JsonArray(emptyList()))
                        put("rematch", buildJsonArray {
                            sources.forEach { q ->
                                add(buildJsonObject {
                                    put("statement", q.statement.take(3_000))
                                    put("options", buildJsonArray { q.options.sortedBy { it.position }.forEach { o -> add(buildJsonObject { put("key", o.key); put("text", o.text.take(1_500)); put("correct", o.correct) }) } })
                                })
                            }
                        })
                    },
                ),
            )
        } else {
            val blueprint = SimulationBlueprint.build(mode, leaves, size)
            val itemTopics = blueprint.parts.flatMap { it.items }.mapTo(hashSetOf()) { it.topicId }
            val avoid = data.questions.filter { it.topicId in itemTopics }.take(40).map { it.statement.replace(Regex("\\s+"), " ").take(280) }.filter { it.length >= 15 }
            blueprint.parts.mapIndexed { index, part ->
                SimPart(
                    key = "",
                    refs = part.items.mapIndexed { i, item -> "i${i + 1}" to item.topicId }.toMap(),
                    input = buildJsonObject {
                        base.forEach { (k, v) -> put(k, v) }
                        put("partIndex", index); put("partCount", blueprint.parts.size)
                        put("items", buildJsonArray {
                            part.items.forEach { item ->
                                add(buildJsonObject {
                                    put("subjectName", item.subjectName.take(300))
                                    put("topicPath", JsonArray(item.path.takeLast(6).map { JsonPrimitive(it.take(800)) }))
                                    put("count", item.count)
                                    put("difficulty", "MISTA")
                                    item.scope?.takeIf(String::isNotBlank)?.let { put("scope", it.take(800)) }
                                })
                            }
                        })
                        put("avoid", JsonArray(avoid.map { JsonPrimitive(it) }))
                    },
                )
            }
        }
        require(parts.isNotEmpty()) { "Ainda não há tópicos suficientes para montar este simulado." }
        val planned = parts.sumOf { p -> (p.input["items"] as? JsonArray).orEmpty().sumOf { (it.jsonObject["count"] as? JsonPrimitive)?.intOrNull ?: 0 } + (p.input["rematch"] as? JsonArray).orEmpty().size }
        val id = data.nextId(Keys.SIMULATIONS)
        val keyed = parts.mapIndexed { index, part -> part.copy(key = "sim-web-$id-${Random.nextBytes(8).joinToString("") { (it.toInt() and 0xff).toString(16) }}-p$index") }
        val title = when (mode) {
            SimulationMode.DIAGNOSTIC -> "Diagnóstico"
            SimulationMode.STUDIED -> "Simulado do que estudei"
            SimulationMode.FULL -> "Prova completa"
            SimulationMode.REMATCH -> "Revanche"
        }
        val at = now()
        val row = jsonOf(
            "id" to id, "competitionId" to competitionId, "mode" to mode.name, "title" to "$title · $planned questões", "board" to profile.board,
            "style" to profile.style, "plannedQuestions" to planned, "timeLimitMinutes" to BoardStyle.minutesFor(profile.style, planned), "status" to "GENERATING",
            "partsJson" to encodeParts(keyed), "answersJson" to "{}", "flaggedJson" to "[]", "weakSpotsJson" to JsonArray(weak.map { JsonPrimitive(it) }).toString(),
            "sourceSimulationId" to null, "createdAt" to at, "startedAt" to null, "finishedAt" to null, "elapsedSeconds" to 0, "answeredCount" to 0,
            "correctCount" to 0, "questionCount" to 0, "scorePercent" to null,
        )
        return data.append(Keys.SIMULATIONS, row) to id
    }

    // ---------------------------------------------------------------- geração

    private fun updatePart(simulationId: Long, index: Int, change: (SimPart) -> SimPart) {
        Store.update { data ->
            data.edit(Keys.SIMULATIONS) { sim ->
                if (sim.long("id") != simulationId) sim
                else {
                    val parts = decodeParts(sim.str("partsJson")).toMutableList()
                    if (index in parts.indices) parts[index] = change(parts[index])
                    sim.with("partsJson" to JsonPrimitive(encodeParts(parts)))
                }
            }
        }
    }

    fun isRunning(id: Long) = id in running

    /** Gera (ou retoma) as partes pendentes, duas por vez. */
    fun generate(simulationId: Long) {
        if (!running.add(simulationId)) return
        scope.launch {
            try {
                val parts = decodeParts(row(Store.data, simulationId)?.str("partsJson"))
                val slots = Semaphore(2)
                parts.withIndex().filter { it.value.status != "DONE" }.map { (index, part) ->
                    async { slots.withPermit { runPart(simulationId, index, part) } }
                }.awaitAll()
                finish(simulationId)
            } finally {
                running.remove(simulationId)
            }
        }
    }

    private suspend fun runPart(simulationId: Long, index: Int, start: SimPart) {
        var part = start
        try {
            if (part.jobId == null) {
                val jobId = AiJobs.create("SIMULATION_GENERATION", part.key, part.input)
                part = part.copy(jobId = jobId, status = "RUNNING", error = null)
                updatePart(simulationId, index) { part }
            }
            val job = AiJobs.await(part.jobId!!, 20 * 60_000L)
            when {
                job == null -> updatePart(simulationId, index) { it.copy(status = "RUNNING") }
                job.status == "SUCCEEDED" && job.proposal != null -> {
                    Store.update { storeQuestions(it, simulationId, index, part, job.proposal) }
                    updatePart(simulationId, index) { it.copy(status = "DONE", error = null) }
                }
                else -> updatePart(simulationId, index) { it.copy(status = "FAILED", error = "Não consegui gerar esta parte com qualidade. A cota dela foi devolvida; tente de novo.") }
            }
        } catch (error: AiJobException) {
            updatePart(simulationId, index) { it.copy(status = "FAILED", jobId = null, error = error.message) }
        } catch (error: Throwable) {
            updatePart(simulationId, index) { it.copy(status = "FAILED", error = error.message ?: "Falha ao gerar esta parte.") }
        }
    }

    private fun finish(simulationId: Long) {
        val sim = row(Store.data, simulationId) ?: return
        val parts = decodeParts(sim.str("partsJson"))
        if (parts.any { it.status == "RUNNING" || it.status == "PENDING" }) return
        val stored = Store.data.questions.count { it.simulationId == simulationId }
        val status = when { parts.all { it.status == "DONE" } -> "READY"; stored > 0 -> "PARTIAL"; else -> "FAILED" }
        Store.update { data -> data.edit(Keys.SIMULATIONS) { if (it.long("id") == simulationId) it.with("status" to JsonPrimitive(status), "questionCount" to JsonPrimitive(stored)) else it } }
    }

    fun retryFailed(simulationId: Long) {
        Store.update { data ->
            data.edit(Keys.SIMULATIONS) { sim ->
                if (sim.long("id") != simulationId) sim
                else sim.with(
                    "status" to JsonPrimitive("GENERATING"),
                    "partsJson" to JsonPrimitive(encodeParts(decodeParts(sim.str("partsJson")).map {
                        if (it.status == "FAILED") it.copy(status = "PENDING", error = null, jobId = null, key = "sim-web-$simulationId-${Random.nextBytes(6).joinToString("") { b -> (b.toInt() and 0xff).toString(16) }}-retry") else it
                    })),
                )
            }
        }
        generate(simulationId)
    }

    internal fun storeQuestions(data: Snapshot, simulationId: Long, partIndex: Int, part: SimPart, proposal: JsonObject): Snapshot {
        val sim = row(data, simulationId) ?: return data
        val weak = runCatching { snapshotJson.parseToJsonElement(sim.str("weakSpotsJson") ?: "[]").jsonArray.map { it.text() } }.getOrDefault(emptyList())
        val existing = data.questions.filter { it.simulationId == simulationId }.mapTo(hashSetOf()) { q -> (data.array(Keys.QUESTIONS).firstOrNull { (it as? JsonObject)?.long("id") == q.id } as? JsonObject)?.str("externalId") }
        var questionId = data.nextId(Keys.QUESTIONS)
        var optionId = (data.questions.flatMap { it.options }.maxOfOrNull { it.id } ?: 0L) + 1
        val at = now()
        val rows = (proposal["questions"] as? JsonArray).orEmpty().mapIndexedNotNull { index, element ->
            val question = element as? JsonObject ?: return@mapIndexedNotNull null
            val externalId = "sim-$simulationId-p$partIndex-q${index + 1}"
            if (externalId in existing) return@mapIndexedNotNull null
            val topicId = part.refs[question["itemRef"].text()] ?: part.refs.values.firstOrNull() ?: return@mapIndexedNotNull null
            val trap = question["trap"].text()
            val weakText = (question["targetsWeakSpot"] as? JsonPrimitive)?.intOrNull?.let { weak.getOrNull(it) }?.takeIf(String::isNotBlank)
            val explanation = buildString {
                append(question["explanation"].text())
                if (trap.isNotBlank()) append("\n\n> **Armadilha:** ").append(trap)
                if (weakText != null) append("\n\n> **Feita para você:** esta questão explora uma confusão que já apareceu no seu caderno de erros (").append(weakText.substringBefore(':')).append(").")
            }
            val options = (question["options"] as? JsonArray).orEmpty().mapIndexed { position, option ->
                val value = option.jsonObject
                jsonOf("id" to optionId++, "key" to value["key"].text(), "text" to value["text"].text(), "correct" to ((value["correct"] as? JsonPrimitive)?.booleanOrNull == true), "position" to position)
            }
            jsonOf(
                "id" to questionId++, "topicId" to topicId, "externalId" to externalId, "board" to sim.str("board"), "agency" to null, "year" to null,
                "difficulty" to question["difficulty"].text().takeIf { it in setOf("FACIL", "MEDIA", "DIFICIL") }, "source" to "Simulado Estudário",
                "statement" to question["statement"].text(), "explanation" to explanation, "notes" to "", "tags" to listOfNotNull("simulado", if (weakText != null) "ponto-fraco" else null).joinToString(","),
                "importedAt" to at, "answerCount" to 0, "correctCount" to 0, "errorCount" to 0, "lastAnswer" to null, "lastAnsweredAt" to null, "favorite" to false,
                "questionSourceType" to "AUTHORIAL", "sourceId" to null, "sourceUrl" to null, "normalizedHash" to null, "reviewAnchor" to null,
                "errorConceptExternalId" to null, "hidden" to false, "simulationId" to simulationId, "options" to JsonArray(options),
            )
        }
        var next = data.appendAll(mapOf(Keys.QUESTIONS to rows))
        // Banca achada pela IA, com fonte: fica pendente até a pessoa confirmar.
        val detected = proposal["detectedBoard"] as? JsonObject
        val name = detected?.get("name").text().ifBlank { null }
        val competitionId = sim.long("competitionId") ?: 0
        if (name != null && examProfile(next, competitionId).board == null) {
            next = next.edit("examProfiles") { if (it.long("competitionId") == competitionId) null else it }.append(
                "examProfiles",
                jsonOf("competitionId" to competitionId, "board" to name, "boardSource" to "WEB", "boardUrl" to detected?.get("sourceUrl").text().ifBlank { null }, "boardPending" to true, "style" to BoardStyle.styleFor(name), "targetPercent" to examProfile(next, competitionId).targetPercent, "updatedAt" to at),
            )
        }
        return next
    }

    // ---------------------------------------------------------------- prova

    fun answersOf(row: JsonObject?): Map<Long, String> = runCatching {
        snapshotJson.parseToJsonElement(row?.str("answersJson") ?: "{}").jsonObject.mapNotNull { (k, v) -> k.toLongOrNull()?.let { it to v.text() } }.toMap()
    }.getOrDefault(emptyMap())

    fun flaggedOf(row: JsonObject?): Set<Long> = runCatching {
        snapshotJson.parseToJsonElement(row?.str("flaggedJson") ?: "[]").jsonArray.mapNotNullTo(hashSetOf()) { (it as? JsonPrimitive)?.longOrNull }
    }.getOrDefault(emptySet())

    fun start(data: Snapshot, simulationId: Long): Snapshot = data.edit(Keys.SIMULATIONS) { sim ->
        if (sim.long("id") != simulationId) sim
        else {
            val count = data.questions.count { it.simulationId == simulationId }
            val style = sim.str("style") ?: "FIVE_OPTIONS"
            sim.with(
                "status" to JsonPrimitive("IN_PROGRESS"),
                "startedAt" to JsonPrimitive(sim.long("startedAt") ?: now()),
                "questionCount" to JsonPrimitive(count),
                "timeLimitMinutes" to JsonPrimitive(if (count != (sim.int("plannedQuestions") ?: count)) BoardStyle.minutesFor(style, count) else sim.int("timeLimitMinutes") ?: BoardStyle.minutesFor(style, count)),
            )
        }
    }

    fun saveProgress(data: Snapshot, simulationId: Long, answers: Map<Long, String>, flagged: Set<Long>, elapsedSeconds: Long): Snapshot = data.edit(Keys.SIMULATIONS) { sim ->
        if (sim.long("id") != simulationId || sim.str("status") == "FINISHED") sim
        else sim.with(
            "answersJson" to JsonPrimitive(JsonObject(answers.entries.associate { it.key.toString() to JsonPrimitive(it.value) }).toString()),
            "flaggedJson" to JsonPrimitive(JsonArray(flagged.map { JsonPrimitive(it) }).toString()),
            "elapsedSeconds" to JsonPrimitive(elapsedSeconds),
            "answeredCount" to JsonPrimitive(answers.size),
        )
    }

    /** Entrega: cada resposta vira uma questão respondida (erros vão para o caderno), como no app. */
    fun submit(data: Snapshot, simulationId: Long, answers: Map<Long, String>, elapsedSeconds: Long): Snapshot {
        val sim = row(data, simulationId) ?: return data
        if (sim.str("status") == "FINISHED") return data
        val questions = data.questions.filter { it.simulationId == simulationId }
        val sessionId = "simulado-$simulationId"
        var next = data
        var correct = 0
        questions.forEach { q ->
            val selected = answers[q.id] ?: return@forEach
            val (after, ok) = Actions.answer(next, q.id, selected, sessionId)
            next = after
            if (ok) correct++
        }
        val topicIds = questions.mapTo(hashSetOf()) { it.topicId }
        val subjectIds = next.topics.filter { it.id in topicIds }.mapTo(hashSetOf()) { it.subjectId }
        val at = now()
        next = next.append(
            Keys.QUESTION_SESSIONS,
            jsonOf("id" to sessionId, "type" to "SIMULATION", "startedAt" to (sim.long("startedAt") ?: at), "completedAt" to at, "durationSeconds" to elapsedSeconds, "questionCount" to questions.size, "correctCount" to correct, "subjectIds" to subjectIds.joinToString(","), "topicIds" to topicIds.joinToString(",")),
        )
        return next.edit(Keys.SIMULATIONS) {
            if (it.long("id") != simulationId) it
            else it.with(
                "status" to JsonPrimitive("FINISHED"),
                "answersJson" to JsonPrimitive(JsonObject(answers.entries.associate { e -> e.key.toString() to JsonPrimitive(e.value) }).toString()),
                "finishedAt" to JsonPrimitive(at), "elapsedSeconds" to JsonPrimitive(elapsedSeconds), "answeredCount" to JsonPrimitive(answers.size),
                "correctCount" to JsonPrimitive(correct), "questionCount" to JsonPrimitive(questions.size),
                "scorePercent" to JsonPrimitive(if (questions.isEmpty()) 0 else correct * 100 / questions.size),
            )
        }
    }

    fun delete(data: Snapshot, simulationId: Long): Snapshot {
        val finished = row(data, simulationId)?.str("status") == "FINISHED"
        return data
            .edit(Keys.QUESTIONS) { if (!finished && it.long("simulationId") == simulationId) null else it }
            .edit(Keys.SIMULATIONS) { if (it.long("id") == simulationId) null else it }
    }

    fun subjectScores(data: Snapshot, simulationId: Long): List<SubjectScore> {
        val answers = answersOf(row(data, simulationId))
        val topics = data.topics.associateBy { it.id }
        val subjects = data.subjects.associateBy { it.id }
        return data.questions.filter { it.simulationId == simulationId }.groupBy { topics[it.topicId]?.subjectId }.map { (subjectId, list) ->
            SubjectScore(subjects[subjectId]?.name ?: "Outros", list.size, list.count { q -> answers[q.id] == q.options.firstOrNull { it.correct }?.key })
        }.sortedByDescending { it.total }
    }
}

private fun JsonArray?.orEmpty(): List<JsonElement> = this ?: emptyList()
private fun JsonObject?.orEmpty(): Map<String, JsonElement> = this ?: emptyMap()
