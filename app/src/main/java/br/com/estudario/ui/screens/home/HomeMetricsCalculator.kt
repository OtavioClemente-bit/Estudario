package br.com.estudario.ui.screens.home

import br.com.estudario.data.local.ErrorStatus
import br.com.estudario.data.local.ErrorWithQuestion
import br.com.estudario.data.local.QuestionAttemptEntity
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.ReviewScheduleEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.ComputedReviewStatus
import br.com.estudario.domain.MasteryCalculator
import br.com.estudario.domain.MasteryInput
import br.com.estudario.domain.ReviewPolicy

/** Métricas calculadas da Home, sem dependência de Compose ou de ViewModel. */
internal data class HomeMetrics(
    val coverage: Int = 0,
    val startedTopics: Int = 0,
    val totalTopics: Int = 0,
    val mastery: Int = 0,
    val subjects: List<SubjectCoverageUi> = emptyList(),
    val pendingReviews: Int = 0,
    val pendingErrors: Int = 0,
    val errorsDueToday: Int = 0,
    val answeredCount: Int = 0,
    val weakTopicId: Long? = null,
    val weakTopicTitle: String = "",
    val weakTopicMastery: Int = 0,
)

/**
 * Converte o estado real do edital para o resumo usado na Home.
 *
 * A lista de matérias é completa e segue a ordem declarada no edital. Qualquer compactação visual
 * deve acontecer no componente, nunca nesta fronteira de dados.
 */
internal fun calculateHomeMetrics(
    competitionId: Long,
    subjects: List<SubjectEntity>,
    topics: List<TopicEntity>,
    questions: List<QuestionWithOptions>,
    attempts: List<QuestionAttemptEntity>,
    errors: List<ErrorWithQuestion>,
    reviews: List<ReviewScheduleEntity>,
): HomeMetrics {
    val now = System.currentTimeMillis()
    val competitionSubjects = subjects
        .filter { it.competitionId == competitionId }
        .sortedWith(compareBy<SubjectEntity> { it.position }.thenBy { it.name })
    val subjectIds = competitionSubjects.mapTo(hashSetOf()) { it.id }
    val competitionTopics = topics.filter { it.subjectId in subjectIds }
    val questionsByTopic = questions.groupBy { it.question.topicId }
    val attemptsByQuestion = attempts.groupBy { it.questionId }
    val errorsByQuestion = errors.groupBy { it.entry.questionId }
    val reviewsByTopic = reviews.groupBy { it.topicId }

    var masterySum = 0
    var weakTopic: TopicEntity? = null
    var weakMastery = Int.MAX_VALUE
    competitionTopics.forEach { topic ->
        val topicQuestions = questionsByTopic[topic.id].orEmpty()
        val topicAttempts = topicQuestions.flatMap { attemptsByQuestion[it.question.id].orEmpty() }
        val recent = topicAttempts.sortedByDescending { it.answeredAt }.take(20)
        val topicErrors = topicQuestions.flatMap { errorsByQuestion[it.question.id].orEmpty() }
        val topicReviews = reviewsByTopic[topic.id].orEmpty()
        val percent = MasteryCalculator.percent(
            MasteryInput(
                topic.status,
                topicQuestions.sumOf { it.question.answerCount },
                topicQuestions.sumOf { it.question.correctCount },
                recent.count { !it.correct },
                topicReviews.count { it.completedAt != null },
                recent.size,
                recent.count { it.correct },
                topicErrors.count { it.entry.status == ErrorStatus.RECORRENTE },
                topicReviews.count { ReviewPolicy.status(it, now) == ComputedReviewStatus.ATRASADA },
            ),
        )
        masterySum += percent
        if (topic.status != TopicStatus.NAO_ESTUDADO && percent < weakMastery) {
            weakMastery = percent
            weakTopic = topic
        }
    }

    val topicsBySubject = competitionTopics.groupBy { it.subjectId }
    val subjectCoverage = competitionSubjects.mapNotNull { subject ->
        val subjectTopics = topicsBySubject[subject.id].orEmpty()
        if (subjectTopics.isEmpty()) return@mapNotNull null
        SubjectCoverageUi(
            name = subject.name,
            studiedTopics = subjectTopics.count { it.status != TopicStatus.NAO_ESTUDADO },
            totalTopics = subjectTopics.size,
            position = subject.position,
        )
    }

    val started = competitionTopics.count { it.status != TopicStatus.NAO_ESTUDADO }
    return HomeMetrics(
        coverage = if (competitionTopics.isEmpty()) 0 else started * 100 / competitionTopics.size,
        startedTopics = started,
        totalTopics = competitionTopics.size,
        mastery = if (competitionTopics.isEmpty()) 0 else masterySum / competitionTopics.size,
        subjects = subjectCoverage,
        pendingReviews = reviews.count { ReviewPolicy.status(it, now) in setOf(ComputedReviewStatus.DISPONIVEL, ComputedReviewStatus.ATRASADA) },
        pendingErrors = errors.count { it.entry.pending },
        errorsDueToday = errors.count { item -> item.entry.nextRetryAt?.let { it <= now } == true },
        answeredCount = attempts.size,
        weakTopicId = weakTopic?.id,
        weakTopicTitle = weakTopic?.title.orEmpty(),
        weakTopicMastery = if (weakTopic == null) 0 else weakMastery,
    )
}
