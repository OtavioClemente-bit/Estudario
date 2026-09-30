package br.com.estudario.data.simulation

import android.content.Context
import androidx.room.withTransaction
import br.com.estudario.data.StudyRepository
import br.com.estudario.data.ai.AiApiException
import br.com.estudario.data.ai.AiAuthenticationRequiredException
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.AiJobStatus
import br.com.estudario.data.ai.AiProcessTimeoutException
import br.com.estudario.data.ai.AiTextJobClient
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.Difficulty
import br.com.estudario.data.local.ErrorStatus
import br.com.estudario.data.local.ExamProfileEntity
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionOptionEntity
import br.com.estudario.data.local.QuestionSessionEntity
import br.com.estudario.data.local.QuestionSessionType
import br.com.estudario.data.local.QuestionSourceType
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.SimulationEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.simulation.BlueprintTopic
import br.com.estudario.domain.simulation.BoardStyle
import br.com.estudario.domain.simulation.SimulationBlueprint
import br.com.estudario.domain.simulation.SimulationMode
import br.com.estudario.domain.simulation.SimulationReadiness
import br.com.estudario.domain.simulation.SimulationUnlock
import java.time.Instant
import java.time.ZoneId
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.json.JSONArray
import org.json.JSONObject

/** Uma parte da geração: até 30 questões, um job no servidor. */
data class SimulationPart(
    val key: String,
    val input: JsonObject,
    /** ref do item (i1, r1...) → tópico onde a questão vai morar. */
    val refs: Map<String, Long>,
    val jobId: String? = null,
    /** PENDING, RUNNING, DONE ou FAILED. */
    val status: String = "PENDING",
    val error: String? = null,
)

/** Resultado por matéria, para o gráfico do resultado. */
data class SubjectScore(val subjectName: String, val total: Int, val correct: Int) {
    val percent: Int get() = if (total == 0) 0 else correct * 100 / total
}

class SimulationException(message: String) : IllegalStateException(message)

/**
 * O simulado de ponta a ponta: planta, geração em partes paralelas (que sobrevive ao app fechar),
 * questões guardadas fora do banco até a entrega, e a correção que alimenta caderno de erros,
 * revisões e estatísticas como qualquer questão respondida.
 */
class SimulationService(
    private val context: Context,
    private val db: AppDatabase,
    private val repository: StudyRepository,
    private val client: AiTextJobClient,
) {
    private val dao = db.simulationDao()
    private val appDao = db.dao()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val running = mutableMapOf<Long, Job>()
    private val partsLock = Mutex()

    val simulations = dao.simulations()
    val examProfiles = dao.examProfiles()

    // ---------------------------------------------------------------------------------------- prontidão

    suspend fun leafTopics(competitionId: Long): List<BlueprintTopic> {
        val subjects = appDao.subjectsOnce().filter { it.competitionId == competitionId }
        val subjectIds = subjects.map { it.id }.toSet()
        val topics = appDao.topicsOnce().filter { it.subjectId in subjectIds }
        val parents = topics.mapNotNull { it.parentTopicId }.toSet()
        val withTheory = appDao.theoriesOnce().map { it.topicId }.toSet()
        val subjectOrder = subjects.withIndex().associate { it.value.id to it.index }
        return topics.filter { it.id !in parents }
            .sortedWith(compareBy({ subjectOrder[it.subjectId] ?: 0 }, { it.position }))
            .map { topic ->
                BlueprintTopic(
                    topicId = topic.id,
                    subjectId = topic.subjectId,
                    subjectName = subjects.first { it.id == topic.subjectId }.name,
                    path = pathOf(topic, topics),
                    weight = topic.assessedPriorityScore,
                    studied = topic.status != TopicStatus.NAO_ESTUDADO,
                    hasContent = topic.id in withTheory,
                    scope = topic.scopeCovers,
                )
            }
    }

    suspend fun readiness(competitionId: Long): SimulationReadiness {
        val leaves = leafTopics(competitionId)
        val zone = ZoneId.of("America/Sao_Paulo")
        val days = (appDao.attemptsOnce().map { it.answeredAt } + appDao.sessionsOnce().map { it.completedAt })
            .map { Instant.ofEpochMilli(it).atZone(zone).toLocalDate() }
            .toSet()
        val finished = dao.simulationsOnce().filter { it.competitionId == competitionId && it.status == "FINISHED" }
        return SimulationReadiness(
            leafTopics = leaves.size,
            studiedTopics = leaves.count { it.studied },
            coveredTopics = leaves.count { it.studied || it.hasContent },
            studyDays = days.size,
            wrongInFinished = finished.sumOf { it.questionCount - it.correctCount },
        )
    }

    suspend fun examProfile(competitionId: Long): ExamProfileEntity = dao.examProfile(competitionId) ?: ExamProfileEntity(competitionId)

    suspend fun saveBoard(competitionId: Long, board: String?, source: String = "USER") {
        val current = examProfile(competitionId)
        dao.saveExamProfile(
            current.copy(
                board = board?.trim()?.ifBlank { null },
                boardSource = if (board.isNullOrBlank()) null else source,
                boardUrl = if (source == "USER") null else current.boardUrl,
                boardPending = false,
                style = BoardStyle.styleFor(board),
                updatedAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun confirmDetectedBoard(competitionId: Long) {
        val current = examProfile(competitionId)
        dao.saveExamProfile(current.copy(boardPending = false, updatedAt = System.currentTimeMillis()))
    }

    suspend fun saveTarget(competitionId: Long, percent: Int) {
        val current = examProfile(competitionId)
        dao.saveExamProfile(current.copy(targetPercent = percent.coerceIn(30, 100), updatedAt = System.currentTimeMillis()))
    }

    // ---------------------------------------------------------------------------------------- criação

    /** Monta a planta, grava o simulado e começa a gerar. Devolve o id local. */
    suspend fun create(competitionId: Long, mode: SimulationMode, size: Int): Long {
        val competition = appDao.competitionsOnce().firstOrNull { it.id == competitionId }
            ?: throw SimulationException("Concurso não encontrado.")
        val profile = examProfile(competitionId)
        val board = profile.board
        val style = profile.style
        val weakSpots = weakSpots(competitionId)
        val base = buildJsonObject {
            put("competitionName", competition.name.take(300))
            board?.let { put("board", it.take(80)) }
            put("mode", mode.name)
            put("style", style)
            put("detectBoard", board == null)
            put("weakSpots", JsonArray(weakSpots.map { JsonPrimitive(it) }))
        }
        val parts = if (mode == SimulationMode.REMATCH) rematchParts(competitionId, base) else blueprintParts(competitionId, mode, size, base)
        if (parts.isEmpty()) throw SimulationException("Ainda não há tópicos suficientes para montar este simulado.")
        val planned = parts.sumOf { questionCountOf(it.input) }
        val title = when (mode) {
            SimulationMode.DIAGNOSTIC -> "Diagnóstico"
            SimulationMode.STUDIED -> "Simulado do que estudei"
            SimulationMode.FULL -> "Prova completa"
            SimulationMode.REMATCH -> "Revanche"
        }
        val id = dao.insertSimulation(
            SimulationEntity(
                competitionId = competitionId,
                mode = mode.name,
                title = "$title · $planned questões",
                board = board,
                style = style,
                plannedQuestions = planned,
                timeLimitMinutes = BoardStyle.minutesFor(style, planned),
                partsJson = encodeParts(parts),
                weakSpotsJson = JSONArray(weakSpots).toString(),
            ),
        )
        // Chave única por simulado: a mesma parte reenviada não reserva cota duas vezes.
        val keyed = parts.mapIndexed { index, part -> part.copy(key = "sim-$id-${UUID.randomUUID()}-p$index") }
        dao.updateSimulation(dao.simulation(id)!!.copy(partsJson = encodeParts(keyed)))
        resume(id)
        return id
    }

    private fun questionCountOf(input: JsonObject): Int =
        (input["items"] as? JsonArray).orEmpty().sumOf { it.jsonObject["count"]?.jsonPrimitive?.intOrNull ?: 0 } +
            (input["rematch"] as? JsonArray).orEmpty().size

    private suspend fun blueprintParts(competitionId: Long, mode: SimulationMode, size: Int, base: JsonObject): List<SimulationPart> {
        val topics = leafTopics(competitionId)
        val blueprint = SimulationBlueprint.build(mode, topics, size)
        val avoid = avoidList(blueprint.parts.flatMap { it.items }.map { it.topicId }.toSet())
        return blueprint.parts.mapIndexed { index, part ->
            val refs = part.items.mapIndexed { itemIndex, item -> "i${itemIndex + 1}" to item.topicId }.toMap()
            val input = buildJsonObject {
                base.forEach { (key, value) -> put(key, value) }
                put("partIndex", index)
                put("partCount", blueprint.parts.size)
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
            }
            SimulationPart(key = "", input = input, refs = refs)
        }
    }

    /** Revanche: as questões erradas nos simulados entregues e as do caderno de erros ainda abertas. */
    private suspend fun rematchParts(competitionId: Long, base: JsonObject): List<SimulationPart> {
        val sources = rematchSources(competitionId).take(SimulationBlueprint.MAX_PER_PART)
        if (sources.isEmpty()) return emptyList()
        val refs = sources.mapIndexed { index, question -> "r${index + 1}" to question.question.topicId }.toMap()
        val input = buildJsonObject {
            base.forEach { (key, value) -> put(key, value) }
            put("partIndex", 0)
            put("partCount", 1)
            put("items", JsonArray(emptyList()))
            put("rematch", buildJsonArray {
                sources.forEach { source ->
                    add(buildJsonObject {
                        put("statement", source.question.statement.take(3_000))
                        put("options", buildJsonArray {
                            source.options.sortedBy { it.position }.forEach { option ->
                                add(buildJsonObject {
                                    put("key", option.key)
                                    put("text", option.text.take(1_500))
                                    put("correct", option.isCorrect)
                                })
                            }
                        })
                    })
                }
            })
        }
        return listOf(SimulationPart(key = "", input = input, refs = refs))
    }

    suspend fun rematchSources(competitionId: Long): List<QuestionWithOptions> {
        val finished = dao.simulationsOnce().filter { it.competitionId == competitionId && it.status == "FINISHED" }
        val wrongIds = LinkedHashSet<Long>()
        for (simulation in finished.sortedByDescending { it.finishedAt ?: 0 }) {
            val answers = decodeAnswers(simulation.answersJson)
            dao.questionsFor(simulation.id).forEach { row ->
                val correct = row.options.firstOrNull { it.isCorrect }?.key
                if (answers[row.question.id] != correct) wrongIds += row.question.id
            }
        }
        val topicIds = leafTopics(competitionId).map { it.topicId }.toSet()
        appDao.errorsOnce().filter { it.status == ErrorStatus.NOVO || it.status == ErrorStatus.RECORRENTE }.forEach { wrongIds += it.questionId }
        val all = appDao.questionsPage(5_000, 0).associateBy { it.question.id }
        return wrongIds.mapNotNull { all[it] }.filter { it.question.topicId in topicIds && it.options.size in 2..5 && it.options.count { option -> option.isCorrect } == 1 }
    }

    private suspend fun avoidList(topicIds: Set<Long>): List<String> =
        appDao.questionsPage(2_000, 0)
            .filter { it.question.topicId in topicIds }
            .take(40)
            .map { it.question.statement.replace(Regex("\\s+"), " ").take(280) }
            .filter { it.length >= 15 }

    /** Os conceitos em que a pessoa mais erra, para a IA montar distratores em cima deles. */
    private suspend fun weakSpots(competitionId: Long): List<String> {
        val topicIds = leafTopics(competitionId).map { it.topicId }.toSet()
        val concepts = appDao.errorConceptsOnce().filter { it.topicId in topicIds }.associateBy { it.id }
        val counts = appDao.errorConceptEntriesOnce().groupingBy { it.conceptId }.eachCount()
        return counts.entries.filter { it.key in concepts }.sortedByDescending { it.value }.take(8).map { entry ->
            val concept = concepts.getValue(entry.key)
            listOf(concept.title, concept.summary).filter(String::isNotBlank).joinToString(": ").replace(Regex("\\s+"), " ").take(240)
        }
    }

    // ---------------------------------------------------------------------------------------- geração

    /** Retoma a geração de todos os simulados pendentes (ex.: o app foi fechado no meio). */
    fun resumeAll() = scope.launch { dao.generating().forEach { resume(it.id) } }

    fun resume(simulationId: Long) {
        synchronized(running) {
            if (running[simulationId]?.isActive == true) return
            running[simulationId] = scope.launch { generate(simulationId) }
        }
    }

    /** Refaz só as partes que falharam. */
    fun retryFailed(simulationId: Long) = scope.launch {
        partsLock.withLock {
            val simulation = dao.simulation(simulationId) ?: return@launch
            val parts = decodeParts(simulation.partsJson).map {
                if (it.status == "FAILED") it.copy(status = "PENDING", error = null, jobId = null, key = "sim-$simulationId-${UUID.randomUUID()}-retry") else it
            }
            dao.updateSimulation(simulation.copy(status = "GENERATING", partsJson = encodeParts(parts)))
        }
        resume(simulationId)
    }

    private suspend fun generate(simulationId: Long) {
        val simulation = dao.simulation(simulationId) ?: return
        val parts = decodeParts(simulation.partsJson)
        coroutineScope {
            parts.withIndex().filter { it.value.status != "DONE" }.map { (index, part) ->
                async { runPart(simulationId, index, part) }
            }.awaitAll()
        }
        finishGeneration(simulationId)
    }

    private suspend fun runPart(simulationId: Long, index: Int, start: SimulationPart) {
        var part = start
        try {
            if (part.jobId == null) {
                val created = client.create(AiFeature.SIMULATION_GENERATION, part.key, part.input)
                part = part.copy(jobId = created.jobId, status = "RUNNING")
                updatePart(simulationId, index) { part }
            }
            val job = client.await(part.jobId!!, timeoutMillis = 20 * 60_000L)
            when (job.status) {
                AiJobStatus.SUCCEEDED -> {
                    val proposal = job.proposal ?: throw SimulationException("A parte ficou pronta, mas não chegou ao aparelho.")
                    storeQuestions(simulationId, index, part, proposal)
                    updatePart(simulationId, index) { it.copy(status = "DONE", error = null) }
                }
                else -> updatePart(simulationId, index) {
                    it.copy(status = "FAILED", error = "Não consegui gerar esta parte com qualidade. A cota dela foi devolvida; tente de novo.")
                }
            }
        } catch (_: AiProcessTimeoutException) {
            // Continua no servidor: fica RUNNING e é retomada na próxima abertura.
            updatePart(simulationId, index) { it.copy(status = "RUNNING") }
        } catch (_: AiAuthenticationRequiredException) {
            updatePart(simulationId, index) { it.copy(status = "FAILED", error = "Entre na sua conta para gerar o simulado.") }
        } catch (error: AiApiException) {
            updatePart(simulationId, index) { it.copy(status = "FAILED", jobId = if (part.jobId == null) null else it.jobId, error = apiMessage(error.code)) }
        } catch (error: kotlinx.coroutines.CancellationException) {
            throw error
        } catch (error: Throwable) {
            updatePart(simulationId, index) { it.copy(status = "FAILED", error = error.message ?: "Falha ao gerar esta parte.") }
        }
    }

    private suspend fun updatePart(simulationId: Long, index: Int, change: (SimulationPart) -> SimulationPart) = partsLock.withLock {
        val simulation = dao.simulation(simulationId) ?: return@withLock
        val parts = decodeParts(simulation.partsJson).toMutableList()
        if (index !in parts.indices) return@withLock
        parts[index] = change(parts[index])
        dao.updateSimulation(simulation.copy(partsJson = encodeParts(parts)))
    }

    private suspend fun finishGeneration(simulationId: Long) {
        val simulation = dao.simulation(simulationId) ?: return
        val parts = decodeParts(simulation.partsJson)
        if (parts.any { it.status == "RUNNING" || it.status == "PENDING" }) return
        val stored = dao.questionsFor(simulationId).size
        val status = when {
            parts.all { it.status == "DONE" } -> "READY"
            stored > 0 -> "PARTIAL"
            else -> "FAILED"
        }
        dao.updateSimulation(simulation.copy(status = status, questionCount = stored))
        val (title, body) = when (status) {
            "READY" -> "Seu simulado está pronto" to "${simulation.title}: ${stored} questões inéditas esperando por você."
            "PARTIAL" -> "Simulado quase pronto" to "$stored questões prontas. Você pode começar ou tentar gerar o resto."
            else -> "Não deu para gerar o simulado" to "Nenhuma cota foi usada nas partes que falharam. Tente de novo."
        }
        runCatching { br.com.estudario.notifications.StudyNotificationCoordinator.post(context, "pending_study", ("sim:$simulationId").hashCode(), title, body) }
    }

    private suspend fun storeQuestions(simulationId: Long, partIndex: Int, part: SimulationPart, proposal: JsonObject) {
        val simulation = dao.simulation(simulationId) ?: return
        val weakSpots = runCatching { JSONArray(simulation.weakSpotsJson) }.getOrNull()
        db.withTransaction {
            // Idempotente: parte regravada (retomada depois de fechar o app) não duplica questões.
            val existing = dao.questionsFor(simulationId).mapNotNull { it.question.externalId }.toSet()
            (proposal["questions"] as? JsonArray).orEmpty().forEachIndexed { index, element ->
                val question = element.jsonObject
                val externalId = "sim-$simulationId-p$partIndex-q${index + 1}"
                if (externalId in existing) return@forEachIndexed
                val topicId = part.refs[question.text("itemRef")] ?: part.refs.values.firstOrNull() ?: return@forEachIndexed
                val trap = question.text("trap")
                val weakIndex = question["targetsWeakSpot"]?.jsonPrimitive?.intOrNull
                val weak = weakIndex?.let { weakSpots?.optString(it) }?.takeIf(String::isNotBlank)
                val explanation = buildString {
                    append(question.text("explanation"))
                    if (trap.isNotBlank()) append("\n\n> **Armadilha:** ").append(trap)
                    if (weak != null) append("\n\n> **Feita para você:** esta questão explora uma confusão que já apareceu no seu caderno de erros (").append(weak.substringBefore(':')).append(").")
                }
                val id = appDao.insertQuestion(
                    QuestionEntity(
                        topicId = topicId,
                        externalId = externalId,
                        board = simulation.board,
                        difficulty = runCatching { Difficulty.valueOf(question.text("difficulty")) }.getOrNull(),
                        source = "Simulado Estudário",
                        statement = question.text("statement"),
                        explanation = explanation,
                        tagsText = listOfNotNull("simulado", if (weak != null) "ponto-fraco" else null).joinToString(","),
                        questionSourceType = QuestionSourceType.AUTHORIAL,
                        simulationId = simulationId,
                    ),
                )
                appDao.insertOptions((question["options"] as? JsonArray).orEmpty().mapIndexed { position, option ->
                    val value = option.jsonObject
                    QuestionOptionEntity(
                        questionId = id,
                        key = value.text("key"),
                        text = value.text("text"),
                        isCorrect = value["correct"]?.jsonPrimitive?.booleanOrNull == true,
                        position = position,
                    )
                })
            }
            // Banca achada pela IA, com fonte: fica pendente até a pessoa confirmar.
            val detected = proposal["detectedBoard"] as? JsonObject
            val name = detected?.get("name")?.jsonPrimitive?.contentOrNull
            if (name != null) {
                val profile = examProfile(simulation.competitionId)
                if (profile.board == null) {
                    dao.saveExamProfile(
                        profile.copy(
                            board = name,
                            boardSource = "WEB",
                            boardUrl = detected["sourceUrl"]?.jsonPrimitive?.contentOrNull,
                            boardPending = true,
                            style = BoardStyle.styleFor(name),
                            updatedAt = System.currentTimeMillis(),
                        ),
                    )
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------- prova

    suspend fun start(simulationId: Long) {
        val simulation = dao.simulation(simulationId) ?: return
        if (simulation.startedAt == null || simulation.status in setOf("READY", "PARTIAL")) {
            // PARTIAL só começa com o que já existe: o que não veio não entra na nota.
            val count = dao.questionsFor(simulationId).size
            dao.updateSimulation(
                simulation.copy(
                    status = "IN_PROGRESS",
                    startedAt = simulation.startedAt ?: System.currentTimeMillis(),
                    questionCount = count,
                    timeLimitMinutes = if (count != simulation.plannedQuestions) BoardStyle.minutesFor(simulation.style, count) else simulation.timeLimitMinutes,
                ),
            )
        }
    }

    suspend fun saveProgress(simulationId: Long, answers: Map<Long, String>, flagged: Set<Long>, elapsedSeconds: Long) {
        val simulation = dao.simulation(simulationId) ?: return
        if (simulation.status == "FINISHED") return
        dao.updateSimulation(
            simulation.copy(
                answersJson = JSONObject(answers.mapKeys { it.key.toString() }).toString(),
                flaggedJson = JSONArray(flagged.toList()).toString(),
                elapsedSeconds = elapsedSeconds,
                answeredCount = answers.size,
            ),
        )
    }

    /** Entrega: cada resposta é registrada como uma questão respondida (erros vão para o caderno). */
    suspend fun submit(simulationId: Long, answers: Map<Long, String>, elapsedSeconds: Long) {
        val simulation = dao.simulation(simulationId) ?: return
        if (simulation.status == "FINISHED") return
        val questions = dao.questionsFor(simulationId)
        val sessionId = "simulado-$simulationId"
        var correct = 0
        for (question in questions) {
            val selected = answers[question.question.id] ?: continue
            if (repository.answer(question, selected, sessionId)) correct++
        }
        val topics = questions.map { it.question.topicId }.toSet()
        val subjectIds = appDao.topicsOnce().filter { it.id in topics }.map { it.subjectId }.toSet()
        val now = System.currentTimeMillis()
        appDao.insertQuestionSession(
            QuestionSessionEntity(
                id = sessionId,
                type = QuestionSessionType.SIMULATION,
                startedAt = simulation.startedAt ?: now,
                completedAt = now,
                durationSeconds = elapsedSeconds,
                questionCount = questions.size,
                correctCount = correct,
                subjectIdsText = subjectIds.joinToString(","),
                topicIdsText = topics.joinToString(","),
            ),
        )
        dao.updateSimulation(
            simulation.copy(
                status = "FINISHED",
                answersJson = JSONObject(answers.mapKeys { it.key.toString() }).toString(),
                finishedAt = now,
                elapsedSeconds = elapsedSeconds,
                answeredCount = answers.size,
                correctCount = correct,
                questionCount = questions.size,
                scorePercent = if (questions.isEmpty()) 0 else correct * 100 / questions.size,
            ),
        )
    }

    suspend fun delete(simulationId: Long) {
        synchronized(running) { running.remove(simulationId)?.cancel() }
        db.withTransaction {
            val simulation = dao.simulation(simulationId)
            // Entregue: as questões já são da pessoa e ficam no banco; só o registro da prova sai.
            if (simulation?.status != "FINISHED") dao.deleteQuestionsFor(simulationId)
            dao.deleteSimulation(simulationId)
        }
    }

    suspend fun subjectScores(simulationId: Long): List<SubjectScore> {
        val simulation = dao.simulation(simulationId) ?: return emptyList()
        val answers = decodeAnswers(simulation.answersJson)
        val topics = appDao.topicsOnce().associateBy { it.id }
        val subjects = appDao.subjectsOnce().associateBy { it.id }
        return dao.questionsFor(simulationId).groupBy { subjects[topics[it.question.topicId]?.subjectId]?.name ?: "Outros" }
            .map { (name, rows) ->
                SubjectScore(name, rows.size, rows.count { row -> answers[row.question.id] == row.options.firstOrNull { it.isCorrect }?.key })
            }
            .sortedBy { it.percent }
    }

    fun questions(simulationId: Long) = dao.questionsForFlow(simulationId)
    fun simulation(simulationId: Long) = dao.simulationFlow(simulationId)

    // ---------------------------------------------------------------------------------------- utilidades

    private fun pathOf(topic: TopicEntity, all: List<TopicEntity>): List<String> {
        val path = mutableListOf(topic.title)
        var current = topic
        while (true) {
            current = all.firstOrNull { it.id == current.parentTopicId } ?: break
            path.add(0, current.title)
        }
        return path.takeLast(6)
    }

    private fun apiMessage(code: String): String = when (code) {
        "QUOTA_EXHAUSTED", "AI_QUOTA_EXHAUSTED", "QUOTA_RESERVED" -> "Os simulados do seu plano acabaram neste mês. Veja em Perfil > Planos e uso."
        "DEVICE_QUOTA_EXHAUSTED" -> "O simulado grátis deste aparelho já foi usado. Os planos pagos liberam mais."
        "AI_ACCESS_DENIED", "BETA_ACCESS_REQUIRED" -> "Sua conta ainda não tem acesso à geração pelo Estudário."
        "AUTH_REQUIRED", "AUTH_INVALID" -> "Entre na sua conta para gerar o simulado."
        "AI_RATE_LIMIT_EXCEEDED" -> "Muitas tentativas em pouco tempo. Tente de novo em alguns minutos."
        "INVALID_REQUEST" -> "O edital deste concurso tem algo que o gerador não aceitou. Tente com menos questões."
        else -> "Não foi possível falar com o Estudário agora. Confira a internet e tente de novo."
    }

    companion object {
        fun decodeAnswers(json: String): Map<Long, String> = runCatching {
            val value = JSONObject(json)
            value.keys().asSequence().mapNotNull { key -> key.toLongOrNull()?.let { it to value.getString(key) } }.toMap()
        }.getOrDefault(emptyMap())

        fun decodeFlagged(json: String): Set<Long> = runCatching {
            val value = JSONArray(json)
            (0 until value.length()).map { value.getLong(it) }.toSet()
        }.getOrDefault(emptySet())

        fun decodeParts(json: String): List<SimulationPart> = runCatching {
            val array = JSONArray(json)
            (0 until array.length()).map { index ->
                val item = array.getJSONObject(index)
                val refs = item.getJSONObject("refs")
                SimulationPart(
                    key = item.getString("key"),
                    input = kotlinx.serialization.json.Json.parseToJsonElement(item.getString("input")).jsonObject,
                    refs = refs.keys().asSequence().associateWith { refs.getLong(it) },
                    jobId = item.optString("jobId").ifBlank { null },
                    status = item.optString("status", "PENDING"),
                    error = item.optString("error").ifBlank { null },
                )
            }
        }.getOrDefault(emptyList())

        fun encodeParts(parts: List<SimulationPart>): String = JSONArray(parts.map { part ->
            JSONObject()
                .put("key", part.key)
                .put("input", part.input.toString())
                .put("refs", JSONObject(part.refs))
                .put("jobId", part.jobId ?: "")
                .put("status", part.status)
                .put("error", part.error ?: "")
        }).toString()

        private fun JsonObject.text(name: String): String = this[name]?.jsonPrimitive?.contentOrNull.orEmpty()
    }
}

private fun JsonArray?.orEmpty(): List<kotlinx.serialization.json.JsonElement> = this ?: emptyList()
