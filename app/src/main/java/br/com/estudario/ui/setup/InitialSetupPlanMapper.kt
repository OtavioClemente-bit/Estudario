package br.com.estudario.ui.setup

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.QuestionAttemptEntity
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.data.prompt.PlanSubjectInfo
import br.com.estudario.data.prompt.PlanTopicInfo
import br.com.estudario.data.prompt.PromptIds
import br.com.estudario.domain.PriorityAssessment
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.PriorityResolver
import br.com.estudario.domain.PriorityState
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.SubjectDifficulty
import br.com.estudario.domain.setup.effectivePriority
import br.com.estudario.domain.setup.toPlanPriority

data class InitialSetupPlanData(
    val promptSubjects: List<PlanSubjectInfo>,
    val officialPrioritiesBySubjectId: Map<Long, PlanPriority>,
    val planningPrioritiesBySubjectId: Map<Long, PlanPriority>,
    val planningPrioritiesByExternalId: Map<String, PlanPriority>,
)

/** Builds both plan paths from the same selected syllabus, evidence and priority snapshot. */
object InitialSetupPlanMapper {
    fun map(
        competition: CompetitionEntity?,
        subjects: List<SubjectEntity>,
        topics: List<TopicEntity>,
        questions: List<QuestionWithOptions>,
        attempts: List<QuestionAttemptEntity>,
        difficulties: Map<String, SubjectDifficulty>,
    ): InitialSetupPlanData {
        val topicById = topics.associateBy { it.id }
        val questionById = questions.associate { it.question.id to it.question }
        val attemptsBySubject = attempts.mapNotNull { attempt ->
            val question = questionById[attempt.questionId] ?: return@mapNotNull null
            val topic = topicById[question.topicId] ?: return@mapNotNull null
            topic.subjectId to attempt
        }.groupBy({ it.first }, { it.second })
        val officialCompetitionPriority = competition?.let(::effectiveOfficialPriority)
        val officialPriorities = LinkedHashMap<Long, PlanPriority>()
        val planningPriorities = LinkedHashMap<Long, PlanPriority>()
        val prioritiesByExternalId = LinkedHashMap<String, PlanPriority>()

        val promptSubjects = subjects.map { subject ->
            val externalId = PromptIds.subject(subject)
            val official = effectiveOfficialPriority(subject, officialCompetitionPriority)
            val difficulty = difficulties[subject.id.toString()] ?: SubjectDifficulty.MEDIUM
            val planned = effectivePriority(official, difficulty)
            officialPriorities[subject.id] = official
            planningPriorities[subject.id] = planned
            prioritiesByExternalId[externalId] = planned

            val subjectTopics = topics.filter { it.subjectId == subject.id }.sortedWith(compareBy<TopicEntity> { it.position }.thenBy { it.id })
            val subjectAttempts = attemptsBySubject[subject.id].orEmpty()
            val answered = subjectAttempts.size
            PlanSubjectInfo(
                id = externalId,
                name = subject.name,
                topics = subjectTopics.map { topic ->
                    PlanTopicInfo(PromptIds.topic(topic), topic.title, topic.status != TopicStatus.NAO_ESTUDADO)
                },
                answered = answered,
                accuracyPercent = if (answered == 0) null else subjectAttempts.count { it.correct } * 100 / answered,
            )
        }
        return InitialSetupPlanData(promptSubjects, officialPriorities, planningPriorities, prioritiesByExternalId)
    }

    fun map(
        competition: CompetitionEntity?,
        subjects: List<SubjectEntity>,
        topics: List<TopicEntity>,
        questions: List<QuestionWithOptions>,
        attempts: List<QuestionAttemptEntity>,
        snapshot: InitialSetupSnapshot,
    ): InitialSetupPlanData = map(competition, subjects, topics, questions, attempts, snapshot.subjectDifficulties)

    private fun effectiveOfficialPriority(subject: SubjectEntity, parent: PriorityLevel?): PlanPriority =
        priorityState(
            subject.hasAssessedPriority,
            subject.assessedPriorityScore,
            subject.assessedPrioritySource,
            subject.assessedPriorityConfidence,
            subject.assessedPriorityRationale,
            subject.userPriorityOverride,
            parent,
        )

    private fun effectiveOfficialPriority(competition: CompetitionEntity): PriorityLevel =
        PriorityResolver.effectivePriority(
            PriorityState(
                hasAssessedPriority = competition.hasAssessedPriority,
                assessment = PriorityAssessment(
                    score = competition.assessedPriorityScore,
                    source = competition.assessedPrioritySource,
                    confidence = competition.assessedPriorityConfidence,
                    rationale = competition.assessedPriorityRationale,
                    evidence = emptyList(),
                ),
                userPriorityOverride = competition.userPriorityOverride,
            ),
            parent = null,
        )

    private fun priorityState(
        hasAssessedPriority: Boolean,
        score: Int,
        source: br.com.estudario.domain.PrioritySource,
        confidence: Float,
        rationale: String?,
        override: PriorityLevel?,
        parent: PriorityLevel?,
    ): PlanPriority = PriorityResolver.effectivePriority(
        PriorityState(
            hasAssessedPriority,
            PriorityAssessment(score, source, confidence, rationale, emptyList()),
            override,
        ),
        parent,
    ).toPlanPriority()
}
