package br.com.estudario.web.data

import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.domain.ErrorRetryLadder
import br.com.estudario.domain.ReviewIntervals
import br.com.estudario.time.plusDays
import br.com.estudario.time.startOfDay
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlin.js.Date
import kotlin.random.Random

/**
 * As mesmas operações do app Android (StudyRepository e StudyExecutionService), escritas sobre a
 * foto JSON. Cada uma devolve uma nova foto; quem chama passa para [Store.update]. Os campos e
 * regras seguem o código do app linha a linha, para os dois lados gravarem a mesma coisa.
 */
object Actions {
    private fun now(): Long = Date.now().toLong()

    private fun uuid(): String {
        val bytes = Random.nextBytes(16)
        bytes[6] = ((bytes[6].toInt() and 0x0f) or 0x40).toByte()
        bytes[8] = ((bytes[8].toInt() and 0x3f) or 0x80).toByte()
        val hex = bytes.joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
        return "${hex.substring(0, 8)}-${hex.substring(8, 12)}-${hex.substring(12, 16)}-${hex.substring(16, 20)}-${hex.substring(20)}"
    }

    private fun JsonObject.long(key: String): Long? = (this[key] as? JsonPrimitive)?.longOrNull
    private fun JsonObject.int(key: String): Int? = (this[key] as? JsonPrimitive)?.intOrNull
    private fun JsonObject.bool(key: String): Boolean? = (this[key] as? JsonPrimitive)?.booleanOrNull
    private fun JsonObject.idIs(id: Long) = long("id") == id
    private fun JsonObject.idIs(id: String) = str("id") == id

    private fun startOfDayInDays(days: Long): Long =
        Queries.todayDate().plusDays(days).startOfDay(Queries.zone).toEpochMilliseconds()

    // ------------------------------------------------------------------ plano

    /** StudyExecutionService.complete: registra a execução e conclui (ou deixa em andamento) a tarefa. */
    fun completeTask(data: Snapshot, taskId: String, minutes: Int, questions: Int = 0, correct: Int = 0, notes: String = "", fromQuiz: Boolean = false): Snapshot {
        val task = data.tasks.firstOrNull { it.id == taskId } ?: return data
        if (task.status in setOf("CONCLUIDA", "NAO_REALIZADA", "PAUSADA")) return data
        val plan = data.plans.firstOrNull { it.id == task.planId } ?: return data
        val completedAt = now()
        val startedAt = completedAt - minutes.coerceAtLeast(0) * 60_000L
        val nextRevision = plan.revision + 1
        val nextStatus = if (!fromQuiz && minutes < task.minutes) "EM_ANDAMENTO" else "CONCLUIDA"
        val reason = when {
            fromQuiz -> "QUESTION_BATTERY_COMPLETED"
            nextStatus == "CONCLUIDA" -> "TASK_COMPLETED"
            else -> "TASK_PARTIAL"
        }
        val execution = jsonOf(
            "id" to uuid(), "planId" to task.planId, "taskId" to task.id, "competitionId" to task.competitionId,
            "subjectId" to task.subjectId, "topicId" to task.topicId, "startedAt" to startedAt, "completedAt" to completedAt,
            "minutes" to minutes.coerceAtLeast(0), "questions" to questions.coerceAtLeast(0), "correct" to correct.coerceIn(0, questions.coerceAtLeast(0)),
            "notes" to notes.trim(), "difficulty" to "NORMAL", "createdAt" to completedAt,
        )
        return data
            .edit(Keys.TASKS) { if (it.idIs(taskId)) it.with("status" to JsonPrimitive(nextStatus), "updatedRevision" to JsonPrimitive(nextRevision), "updatedAt" to JsonPrimitive(completedAt)) else it }
            .edit(Keys.PLANS) { if (it.idIs(plan.id)) it.with("revision" to JsonPrimitive(nextRevision), "updatedAt" to JsonPrimitive(completedAt)) else it }
            .appendAll(
                mapOf(
                    Keys.EXECUTIONS to listOf(execution),
                    Keys.PLAN_REVISIONS to listOf(jsonOf("planId" to plan.id, "revision" to nextRevision, "base" to plan.revision, "reason" to reason, "proposalId" to null, "summary" to task.id, "createdAt" to completedAt)),
                ),
            )
    }

    /** StudyExecutionService.skip */
    fun skipTask(data: Snapshot, taskId: String, reason: String): Snapshot {
        val task = data.tasks.firstOrNull { it.id == taskId } ?: return data
        if (task.locked) return data
        val plan = data.plans.firstOrNull { it.id == task.planId } ?: return data
        val at = now()
        val nextRevision = plan.revision + 1
        return data
            .edit(Keys.TASKS) { if (it.idIs(taskId)) it.with("notes" to JsonPrimitive(reason.trim()), "status" to JsonPrimitive("NAO_REALIZADA"), "updatedRevision" to JsonPrimitive(nextRevision), "updatedAt" to JsonPrimitive(at)) else it }
            .edit(Keys.PLANS) { if (it.idIs(plan.id)) it.with("revision" to JsonPrimitive(nextRevision), "updatedAt" to JsonPrimitive(at)) else it }
            .append(Keys.PLAN_REVISIONS, jsonOf("planId" to plan.id, "revision" to nextRevision, "base" to plan.revision, "reason" to "TASK_SKIPPED", "proposalId" to null, "summary" to task.id, "createdAt" to at))
    }

    // ------------------------------------------------------------------ tópicos

    /** StudyRepository.completeStudy: conclui o tópico, registra a 1ª sessão e agenda as revisões. */
    fun completeStudy(data: Snapshot, topicId: Long, startedAt: Long = now()): Snapshot {
        val topic = data.topics.firstOrNull { it.id == topicId } ?: return data
        if (topic.status in setOf("ESTUDADO", "REVISANDO", "DOMINADO")) return data
        val at = now()
        var next = data
        val firstCompletion = data.sessions.none { it.topicId == topicId }
        if (firstCompletion) {
            val subject = data.subjects.firstOrNull { it.id == topic.subjectId }
            val questionIds = data.questions.filter { it.topicId == topicId }.mapTo(hashSetOf()) { it.id }
            val attempts = data.attempts.filter { it.answeredAt in startedAt..at && it.questionId in questionIds }
            next = next.append(
                Keys.SESSIONS,
                jsonOf(
                    "id" to data.nextId(Keys.SESSIONS), "topicId" to topicId, "startedAt" to startedAt, "completedAt" to at,
                    "competitionId" to subject?.competitionId, "subjectId" to subject?.id, "durationSeconds" to ((at - startedAt) / 1000).coerceAtLeast(0),
                    "questionCount" to attempts.size, "correctCount" to attempts.count { it.correct }, "wrongCount" to attempts.count { !it.correct },
                    "notes" to "", "sourcePackageId" to null,
                ),
            )
        }
        next = next.edit(Keys.TOPICS) {
            if (it.idIs(topicId)) it.with("status" to JsonPrimitive("ESTUDADO"), "firstStudiedAt" to JsonPrimitive(it.long("firstStudiedAt") ?: at), "lastStudiedAt" to JsonPrimitive(at)) else it
        }
        val hasPending = data.reviews.any { it.topicId == topicId && it.completedAt == null && it.ignoredAt == null }
        if (!hasPending) {
            var id = next.nextId(Keys.REVIEWS)
            val reviews = ReviewIntervals.days.mapIndexed { index, days ->
                jsonOf("id" to id++, "topicId" to topicId, "stage" to index + 1, "dueAt" to startOfDayInDays(days), "completedAt" to null, "ignoredAt" to null, "perceivedDifficulty" to null, "questionCorrect" to 0, "questionTotal" to 0)
            }
            next = next.appendAll(mapOf(Keys.REVIEWS to reviews))
        }
        return next.edit("queue") { if (it.long("topicId") == topicId) null else it }
    }

    /** StudyRepository.unmarkStudied */
    fun unmarkStudied(data: Snapshot, topicId: Long): Snapshot {
        val hasHistory = data.sessions.any { it.topicId == topicId } || data.reviews.any { it.topicId == topicId && it.completedAt != null }
        return data
            .edit(Keys.REVIEWS) { if (it.long("topicId") == topicId && it.long("completedAt") == null) null else it }
            .edit(Keys.TOPICS) {
                if (!it.idIs(topicId)) it
                else it.with(
                    "status" to JsonPrimitive("NAO_ESTUDADO"),
                    "firstStudiedAt" to (if (hasHistory) it["firstStudiedAt"] else null).toJsonElement(),
                    "lastStudiedAt" to (if (hasHistory) it["lastStudiedAt"] else null).toJsonElement(),
                )
            }
    }

    // ------------------------------------------------------------------ revisões

    /** StudyRepository.completeReview */
    fun completeReview(data: Snapshot, reviewId: Long, difficulty: ReviewDifficulty, correct: Int = 0, total: Int = 0, startedAt: Long = now()): Snapshot {
        val review = data.reviews.firstOrNull { it.id == reviewId } ?: return data
        val at = now()
        val ofTopic = data.reviews.filter { it.topicId == review.topicId }
        var next = data.edit(Keys.REVIEWS) {
            if (it.idIs(reviewId)) it.with("completedAt" to JsonPrimitive(at), "perceivedDifficulty" to JsonPrimitive(difficulty.name), "questionCorrect" to JsonPrimitive(correct), "questionTotal" to JsonPrimitive(total)) else it
        }
        val upcoming = ofTopic.filter { it.stage > review.stage && it.completedAt == null && it.ignoredAt == null }.minByOrNull { it.stage }
        if (upcoming != null) {
            val remaining = (upcoming.dueAt - at).coerceAtLeast(86_400_000L)
            val adjusted = when (difficulty) {
                ReviewDifficulty.DIFICIL -> at + 86_400_000L
                ReviewDifficulty.NORMAL -> upcoming.dueAt
                ReviewDifficulty.FACIL -> at + (remaining * 1.35).toLong()
            }
            if (adjusted != upcoming.dueAt) next = next.edit(Keys.REVIEWS) { if (it.idIs(upcoming.id)) it.with("dueAt" to JsonPrimitive(adjusted)) else it }
        } else {
            val ordered = ofTopic.sortedBy { it.stage }
            val nextStage = (ordered.lastOrNull()?.stage ?: review.stage) + 1
            val previousInterval = if (ordered.size >= 2) (ordered[ordered.lastIndex].dueAt - ordered[ordered.lastIndex - 1].dueAt) / 86_400_000L else ReviewIntervals.days.last()
            val days = ReviewIntervals.adjustedIntervalDays(previousInterval, difficulty, correct, total)
            next = next.append(Keys.REVIEWS, jsonOf("id" to next.nextId(Keys.REVIEWS), "topicId" to review.topicId, "stage" to nextStage, "dueAt" to startOfDayInDays(days), "completedAt" to null, "ignoredAt" to null, "perceivedDifficulty" to null, "questionCorrect" to 0, "questionTotal" to 0))
        }
        next = next.append(Keys.REVIEW_HISTORY, jsonOf("id" to next.nextId(Keys.REVIEW_HISTORY), "topicId" to review.topicId, "reviewedAt" to at))
        next = next.append(
            Keys.REVIEW_SESSIONS,
            jsonOf("id" to next.nextId(Keys.REVIEW_SESSIONS), "reviewId" to review.id, "topicId" to review.topicId, "startedAt" to startedAt, "completedAt" to at, "recalled" to 0, "forgotten" to 0, "questionCorrect" to correct, "questionTotal" to total, "difficulty" to difficulty.name),
        )
        return next.edit(Keys.TOPICS) { if (it.idIs(review.topicId)) it.with("status" to JsonPrimitive("REVISANDO"), "lastReviewedAt" to JsonPrimitive(at)) else it }
    }

    /** StudyRepository.ignoreReview */
    fun ignoreReview(data: Snapshot, reviewId: Long): Snapshot =
        data.edit(Keys.REVIEWS) { if (it.idIs(reviewId)) it.with("ignoredAt" to JsonPrimitive(now())) else it }

    // ------------------------------------------------------------------ questões

    /** StudyRepository.answer: tentativa, contadores, caderno de erros e conceitos. Devolve (foto, acertou). */
    fun answer(data: Snapshot, questionId: Long, selectedKey: String, sessionId: String? = null): Pair<Snapshot, Boolean> {
        val question = data.questions.firstOrNull { it.id == questionId } ?: return data to false
        val correct = question.options.firstOrNull { it.key == selectedKey }?.correct == true
        val correctKey = question.options.firstOrNull { it.correct }?.key
        val at = now()
        var next = data.append(Keys.ATTEMPTS, jsonOf("id" to data.nextId(Keys.ATTEMPTS), "questionId" to questionId, "selectedKey" to selectedKey, "correct" to correct, "answeredAt" to at, "sessionId" to sessionId))
        next = next.edit(Keys.QUESTIONS) {
            if (!it.idIs(questionId)) it
            else it.with(
                "answerCount" to JsonPrimitive((it.int("answerCount") ?: 0) + 1),
                "correctCount" to JsonPrimitive((it.int("correctCount") ?: 0) + if (correct) 1 else 0),
                "errorCount" to JsonPrimitive((it.int("errorCount") ?: 0) + if (correct) 0 else 1),
                "lastAnswer" to JsonPrimitive(selectedKey),
                "lastAnsweredAt" to JsonPrimitive(at),
            )
        }
        val existing = data.errors.firstOrNull { it.questionId == questionId }
        if (!correct) {
            val streak = ErrorRetryLadder.nextStreak(existing?.retryStreak ?: 0, correct = false)
            val retryAt = ErrorRetryLadder.nextRetryAt(streak, at)
            val errorId: Long
            if (existing == null) {
                errorId = next.nextId(Keys.ERRORS)
                next = next.append(
                    Keys.ERRORS,
                    jsonOf(
                        "id" to errorId, "questionId" to questionId, "errorCount" to 1, "retryCorrectCount" to 0, "firstErrorAt" to at, "lastErrorAt" to at,
                        "lastReviewedAt" to null, "comment" to "", "concept" to "", "pending" to true, "selectedAnswer" to selectedKey, "correctAnswer" to correctKey,
                        "status" to "NOVO", "retryStreak" to streak, "nextRetryAt" to retryAt,
                    ),
                )
            } else {
                errorId = existing.id
                val recurrent = existing.lastReviewedAt != null || existing.retryCorrectCount > 0 || existing.status == "CORRIGIDO"
                next = next.edit(Keys.ERRORS) {
                    if (!it.idIs(existing.id)) it
                    else it.with(
                        "errorCount" to JsonPrimitive(existing.errorCount + 1), "lastErrorAt" to JsonPrimitive(at), "pending" to JsonPrimitive(true),
                        "selectedAnswer" to JsonPrimitive(selectedKey), "correctAnswer" to correctKey.toJsonElement(),
                        "status" to JsonPrimitive(if (recurrent) "RECORRENTE" else existing.status), "retryStreak" to JsonPrimitive(streak), "nextRetryAt" to retryAt.toJsonElement(),
                    )
                }
            }
            val declared = question.errorConceptExternalId?.takeIf { it.isNotBlank() }?.let { ext -> data.errorConcepts.firstOrNull { it.externalId == ext } }
            val conceptName = declared?.title
                ?: question.tags.split(',').firstOrNull()?.trim()?.takeUnless { it.isBlank() }
                ?: data.topics.firstOrNull { it.id == question.topicId }?.title ?: "Conceito da questão"
            val concept = declared ?: data.errorConcepts.firstOrNull { it.topicId == question.topicId && it.title == conceptName }
            val conceptId: Long
            if (concept == null) {
                conceptId = next.nextId(Keys.ERROR_CONCEPTS)
                next = next.append(
                    Keys.ERROR_CONCEPTS,
                    jsonOf(
                        "id" to conceptId, "topicId" to question.topicId, "title" to conceptName, "summary" to "", "favorite" to false, "externalId" to null,
                        "createdAt" to at, "updatedAt" to at, "errorCount" to 1, "correctAfterErrorCount" to 0, "lastErrorAt" to at, "lastReviewedAt" to null,
                        "priority" to "ALTA", "mastered" to false,
                    ),
                )
            } else {
                conceptId = concept.id
                next = next.edit(Keys.ERROR_CONCEPTS) {
                    if (!it.idIs(concept.id)) it
                    else it.with(
                        "errorCount" to JsonPrimitive(concept.errorCount + 1), "lastErrorAt" to JsonPrimitive(at),
                        "priority" to JsonPrimitive(if (concept.errorCount >= 1) "ALTA" else "NORMAL"), "mastered" to JsonPrimitive(false), "updatedAt" to JsonPrimitive(at),
                    )
                }
            }
            if (next.errorConceptEntries.none { it.conceptId == conceptId && it.errorEntryId == errorId }) {
                next = next.append(Keys.ERROR_CONCEPT_ENTRIES, jsonOf("conceptId" to conceptId, "errorEntryId" to errorId))
            }
        } else if (existing != null) {
            val streak = ErrorRetryLadder.nextStreak(existing.retryStreak, correct = true)
            next = next.edit(Keys.ERRORS) {
                if (!it.idIs(existing.id)) it
                else it.with(
                    "retryCorrectCount" to JsonPrimitive(existing.retryCorrectCount + 1), "lastReviewedAt" to JsonPrimitive(at), "pending" to JsonPrimitive(false),
                    "status" to JsonPrimitive("CORRIGIDO"), "retryStreak" to JsonPrimitive(streak), "nextRetryAt" to ErrorRetryLadder.nextRetryAt(streak, at).toJsonElement(),
                )
            }
            val conceptIds = data.errorConceptEntries.filter { it.errorEntryId == existing.id }.mapTo(hashSetOf()) { it.conceptId }
            next = next.edit(Keys.ERROR_CONCEPTS) {
                val id = it.long("id")
                if (id == null || id !in conceptIds) it
                else {
                    val after = (it.int("correctAfterErrorCount") ?: 0) + 1
                    it.with("correctAfterErrorCount" to JsonPrimitive(after), "lastReviewedAt" to JsonPrimitive(at), "mastered" to JsonPrimitive(after >= (it.int("errorCount") ?: 0)), "updatedAt" to JsonPrimitive(at))
                }
            }
        }
        return next to correct
    }

    /** Sessão de questões concluída (o histórico de treinos do app). */
    fun saveQuestionSession(data: Snapshot, type: String, startedAt: Long, questionCount: Int, correctCount: Int, subjectIds: Set<Long>, topicIds: Set<Long>, id: String = uuid()): Snapshot {
        val at = now()
        return data.append(
            Keys.QUESTION_SESSIONS,
            jsonOf(
                "id" to id, "type" to type, "startedAt" to startedAt, "completedAt" to at, "durationSeconds" to ((at - startedAt) / 1000).coerceAtLeast(0),
                "questionCount" to questionCount, "correctCount" to correctCount, "subjectIds" to subjectIds.joinToString(","), "topicIds" to topicIds.joinToString(","),
            ),
        )
    }

    fun toggleFavorite(data: Snapshot, questionId: Long): Snapshot =
        data.edit(Keys.QUESTIONS) { if (it.idIs(questionId)) it.with("favorite" to JsonPrimitive(!(it.bool("favorite") ?: false))) else it }

    // ------------------------------------------------------------------ caderno de estudo

    /** StudyRepository.saveNote: nova anotação (id 0) ou edição. */
    fun saveNote(data: Snapshot, id: Long, topicId: Long?, text: String): Snapshot =
        if (id == 0L) data.append("notes", jsonOf("id" to data.nextId("notes"), "topicId" to topicId, "questionId" to null, "text" to text.trim(), "createdAt" to now()))
        else data.edit("notes") { if (it.idIs(id)) it.with("topicId" to topicId.toJsonElement(), "text" to JsonPrimitive(text.trim())) else it }

    fun deleteNote(data: Snapshot, id: Long): Snapshot = data.edit("notes") { if (it.idIs(id)) null else it }

    fun toggleSnippetFavorite(data: Snapshot, id: Long): Snapshot =
        data.edit(Keys.SNIPPETS) { if (it.idIs(id)) it.with("favorite" to JsonPrimitive(!(it.bool("favorite") ?: false)), "updatedAt" to JsonPrimitive(now())) else it }

    fun toggleSummaryFavorite(data: Snapshot, id: Long): Snapshot =
        data.edit(Keys.SUMMARIES) { if (it.idIs(id)) it.with("favorite" to JsonPrimitive(!(it.bool("favorite") ?: false)), "updatedAt" to JsonPrimitive(now())) else it }

    fun deleteTheoryMark(data: Snapshot, id: Long): Snapshot = data.edit("theoryMarks") { if (it.idIs(id)) null else it }

    /** Até onde a teoria foi lida (lastReadBlock do app): só avança, e vai junto para o celular. */
    fun setLastReadBlock(data: Snapshot, theoryId: Long, block: Int): Snapshot =
        data.edit(Keys.THEORIES) {
            if (it.idIs(theoryId) && block > (it.int("lastReadBlock") ?: -1)) it.with("lastReadBlock" to JsonPrimitive(block), "updatedAt" to JsonPrimitive(now())) else it
        }

    /** SavedFlashcards do app: um cartão do baralho guardado no Caderno (ou tirado de lá). */
    fun toggleSavedFlashcard(data: Snapshot, summaryId: Long, topicId: Long, front: String, back: String): Snapshot {
        val externalId = savedCardId(summaryId, front, back)
        val existing = data.snippets.firstOrNull { it.externalId == externalId }
        if (existing != null) return data.edit(Keys.SNIPPETS) { if (it.idIs(existing.id)) null else it }
        return data.append(
            Keys.SNIPPETS,
            jsonOf(
                "id" to data.nextId(Keys.SNIPPETS), "topicId" to topicId, "kind" to "RECUPERACAO", "text" to front, "answer" to back.ifBlank { null },
                "position" to 0, "favorite" to true, "externalId" to externalId, "createdAt" to now(), "updatedAt" to now(),
            ),
        )
    }

    fun savedCardId(summaryId: Long, front: String, back: String): String =
        "flashcard:$summaryId:" + br.com.estudario.text.Sha256.digest("$front\u0000$back".encodeToByteArray()).let { bytes -> bytes.joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') } }.take(24)

    fun newSessionId(): String = uuid()
}
