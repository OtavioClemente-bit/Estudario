package br.com.estudario.web.data

import kotlinx.serialization.Serializable

// Visões tipadas da foto sincronizada (o backup do app, formato "estudario-backup" v8). Os nomes
// dos campos são os mesmos que o BackupService do Android escreve; campos que o site não usa
// continuam no JSON original e voltam intactos quando a foto é enviada de novo.

@Serializable
data class Competition(val id: Long, val name: String, val primary: Boolean = false, val createdAt: Long = 0)

@Serializable
data class Subject(val id: Long, val competitionId: Long, val name: String, val position: Int = 0)

@Serializable
data class Topic(
    val id: Long,
    val subjectId: Long,
    val parentTopicId: Long? = null,
    val title: String,
    val description: String = "",
    val position: Int = 0,
    val status: String = "NAO_ESTUDADO",
    val priority: String = "NORMAL",
    val firstStudiedAt: Long? = null,
    val lastStudiedAt: Long? = null,
    val lastReviewedAt: Long? = null,
    val notes: String = "",
    val contentOriginType: String = "EDITAL",
)

@Serializable
data class Summary(val id: Long, val topicId: Long, val title: String, val markdown: String, val favorite: Boolean = false, val kind: String = "COMPLETO", val updatedAt: Long = 0)

@Serializable
data class Theory(val id: Long, val topicId: Long, val title: String, val markdown: String, val lastReadBlock: Int = 0, val updatedAt: Long = 0)

@Serializable
data class Snippet(val id: Long, val topicId: Long, val kind: String, val text: String, val answer: String? = null, val position: Int = 0)

@Serializable
data class QuestionOption(val id: Long, val key: String, val text: String, val correct: Boolean, val position: Int = 0)

@Serializable
data class Question(
    val id: Long,
    val topicId: Long,
    val statement: String,
    val explanation: String = "",
    val board: String? = null,
    val agency: String? = null,
    val year: Int? = null,
    val difficulty: String? = null,
    val answerCount: Int = 0,
    val correctCount: Int = 0,
    val errorCount: Int = 0,
    val lastAnswer: String? = null,
    val lastAnsweredAt: Long? = null,
    val favorite: Boolean = false,
    val hidden: Boolean = false,
    val simulationId: Long? = null,
    val reviewAnchor: String? = null,
    val tags: String = "",
    val errorConceptExternalId: String? = null,
    val options: List<QuestionOption> = emptyList(),
)

@Serializable
data class Attempt(val id: Long, val questionId: Long, val selectedKey: String, val correct: Boolean, val answeredAt: Long, val sessionId: String? = null)

@Serializable
data class ErrorEntry(
    val id: Long,
    val questionId: Long,
    val errorCount: Int = 1,
    val retryCorrectCount: Int = 0,
    val firstErrorAt: Long = 0,
    val lastErrorAt: Long = 0,
    val lastReviewedAt: Long? = null,
    val comment: String = "",
    val concept: String = "",
    val pending: Boolean = true,
    val selectedAnswer: String? = null,
    val correctAnswer: String? = null,
    val status: String = "NOVO",
    val retryStreak: Int = 0,
    val nextRetryAt: Long? = null,
)

@Serializable
data class Review(
    val id: Long,
    val topicId: Long,
    val stage: Int,
    val dueAt: Long,
    val completedAt: Long? = null,
    val ignoredAt: Long? = null,
    val perceivedDifficulty: String? = null,
    val questionCorrect: Int = 0,
    val questionTotal: Int = 0,
)

@Serializable
data class StudyPlan(
    val id: String,
    val competitionId: Long,
    val name: String,
    val objective: String = "",
    val start: Long,
    val exam: Long? = null,
    val active: Boolean = false,
    val master: Boolean = false,
    val archived: Boolean = false,
    val revision: Long = 0,
    val profile: String? = null,
    val block: Int? = null,
)

@Serializable
data class PlanTask(
    val id: String,
    val planId: String,
    val competitionId: Long,
    val subjectId: Long? = null,
    val topicId: Long? = null,
    val subjectName: String = "",
    val topicName: String? = null,
    val day: Long,
    val type: String,
    val minutes: Int,
    val questions: Int = 0,
    val priority: String = "MEDIUM",
    val status: String,
    val origin: String = "AUTOMATIC",
    val notes: String = "",
    val locked: Boolean = false,
    val progressNote: String = "",
    val updatedAt: Long = 0,
)

@Serializable
data class TaskExecution(
    val id: String,
    val planId: String,
    val taskId: String? = null,
    val competitionId: Long,
    val subjectId: Long? = null,
    val topicId: Long? = null,
    val startedAt: Long,
    val completedAt: Long,
    val minutes: Int,
    val questions: Int = 0,
    val correct: Int = 0,
    val notes: String = "",
    val difficulty: String = "NORMAL",
    val createdAt: Long = 0,
)

@Serializable
data class Availability(val planId: String, val day: Int, val minutes: Int, val unavailable: Boolean = false, val mode: String = "FIXED")

@Serializable
data class PlanSubject(
    val planId: String,
    val subjectId: Long,
    val name: String,
    val priority: String = "MEDIUM",
    val paused: Boolean = false,
    val position: Int = 0,
)

@Serializable
data class StudySession(
    val id: Long,
    val topicId: Long,
    val startedAt: Long,
    val completedAt: Long,
    val competitionId: Long? = null,
    val subjectId: Long? = null,
    val durationSeconds: Long = 0,
    val questionCount: Int = 0,
    val correctCount: Int = 0,
)

@Serializable
data class FocusSession(val id: String, val title: String = "", val startedAt: Long, val completedAt: Long, val durationSeconds: Long = 0, val origin: String = "LIVRE", val topicId: Long? = null, val taskId: String? = null)

@Serializable
data class QuestionSession(
    val id: String,
    val type: String,
    val startedAt: Long,
    val completedAt: Long,
    val durationSeconds: Long = 0,
    val questionCount: Int = 0,
    val correctCount: Int = 0,
)

@Serializable
data class Simulation(
    val id: Long,
    val competitionId: Long,
    val mode: String,
    val title: String,
    val status: String,
    val plannedQuestions: Int = 0,
    val timeLimitMinutes: Int = 0,
    val createdAt: Long = 0,
    val finishedAt: Long? = null,
    val answeredCount: Int = 0,
    val correctCount: Int = 0,
    val questionCount: Int = 0,
    val scorePercent: Int? = null,
)

@Serializable
data class ReviewHistory(val id: Long, val topicId: Long, val reviewedAt: Long)

/** Chaves de array da foto, em um só lugar. */
object Keys {
    const val COMPETITIONS = "competitions"
    const val SUBJECTS = "subjects"
    const val TOPICS = "topics"
    const val SUMMARIES = "summaries"
    const val THEORIES = "theories"
    const val SNIPPETS = "snippets"
    const val QUESTIONS = "questions"
    const val ATTEMPTS = "attempts"
    const val ERRORS = "errors"
    const val REVIEWS = "reviews"
    const val REVIEW_HISTORY = "reviewHistory"
    const val SESSIONS = "sessions"
    const val FOCUS_SESSIONS = "focusSessions"
    const val QUESTION_SESSIONS = "questionSessions"
    const val PLANS = "studyPlans"
    const val TASKS = "planTasks"
    const val EXECUTIONS = "studyTaskExecutions"
    const val AVAILABILITY = "studyAvailability"
    const val PLAN_SUBJECTS = "planSubjects"
    const val SIMULATIONS = "simulations"
    const val ERROR_CONCEPTS = "errorConcepts"
    const val ERROR_CONCEPT_ENTRIES = "errorConceptEntries"
    const val REVIEW_SESSIONS = "reviewSessions"
    const val PLAN_REVISIONS = "studyPlanRevisions"
}

@Serializable
data class ErrorConcept(
    val id: Long,
    val topicId: Long,
    val title: String,
    val summary: String = "",
    val externalId: String? = null,
    val errorCount: Int = 0,
    val correctAfterErrorCount: Int = 0,
    val lastErrorAt: Long? = null,
    val lastReviewedAt: Long? = null,
    val priority: String = "NORMAL",
    val mastered: Boolean = false,
)

@Serializable
data class ErrorConceptEntry(val conceptId: Long, val errorEntryId: Long)
