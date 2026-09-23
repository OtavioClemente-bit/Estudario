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
import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.toExamPriority
import br.com.estudario.domain.planner.toPlanPriority
import br.com.estudario.domain.setup.InitialSetupSnapshot

data class InitialSetupPlanData(
    val promptSubjects: List<PlanSubjectInfo>,
    /** Importância na prova, vinda do edital/análise. É a única coisa que decide peso de rodízio. */
    val officialPrioritiesBySubjectId: Map<Long, PlanPriority>,
    /**
     * Mantido por compatibilidade com as telas existentes, e agora **idêntico** a
     * [officialPrioritiesBySubjectId].
     *
     * Antes este mapa carregava a prioridade já elevada pela dificuldade declarada, o que fundia
     * dois conceitos independentes num número só. A dificuldade agora viaja em
     * [difficultiesBySubjectId] e é o motor de necessidade que decide o que fazer com ela.
     */
    val planningPrioritiesBySubjectId: Map<Long, PlanPriority>,
    val planningPrioritiesByExternalId: Map<String, PlanPriority>,
    /** Quanto cada matéria custa para esta pessoa. Eixo separado, nunca somado à prioridade. */
    val difficultiesBySubjectId: Map<Long, PersonalDifficulty> = emptyMap(),
    /** Quanto a pessoa já sabia de cada matéria ao começar. Terceiro eixo, também separado. */
    val knowledgeBySubjectId: Map<Long, InitialKnowledge> = emptyMap(),
)

/** Builds both plan paths from the same selected syllabus, evidence and priority snapshot. */
object InitialSetupPlanMapper {
    fun map(
        competition: CompetitionEntity?,
        subjects: List<SubjectEntity>,
        topics: List<TopicEntity>,
        questions: List<QuestionWithOptions>,
        attempts: List<QuestionAttemptEntity>,
        difficulties: Map<String, PersonalDifficulty>,
        knowledge: Map<String, InitialKnowledge> = emptyMap(),
        /** Ajustes manuais de prioridade feitos no assistente; vazio significa "aceito o edital". */
        priorityOverrides: Map<String, ExamPriority> = emptyMap(),
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
        val personalDifficulties = LinkedHashMap<Long, PersonalDifficulty>()
        val priorKnowledge = LinkedHashMap<Long, InitialKnowledge>()

        val promptSubjects = subjects.map { subject ->
            val externalId = PromptIds.subject(subject)
            val key = subject.id.toString()
            // A prioridade é a da prova e segue sendo só isso, vinda do edital, ou corrigida à mão
            // pela pessoa no assistente. A dificuldade e o conhecimento vão por outros campos.
            val official = priorityOverrides[key]?.toPlanPriority()
                ?: effectiveOfficialPriority(subject, officialCompetitionPriority)
            officialPriorities[subject.id] = official
            planningPriorities[subject.id] = official
            prioritiesByExternalId[externalId] = official
            personalDifficulties[subject.id] = difficulties[key] ?: PersonalDifficulty.DEFAULT
            priorKnowledge[subject.id] = knowledge[key] ?: InitialKnowledge.DEFAULT

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
        return InitialSetupPlanData(
            promptSubjects = promptSubjects,
            officialPrioritiesBySubjectId = officialPriorities,
            planningPrioritiesBySubjectId = planningPriorities,
            planningPrioritiesByExternalId = prioritiesByExternalId,
            difficultiesBySubjectId = personalDifficulties,
            knowledgeBySubjectId = priorKnowledge,
        )
    }

    fun map(
        competition: CompetitionEntity?,
        subjects: List<SubjectEntity>,
        topics: List<TopicEntity>,
        questions: List<QuestionWithOptions>,
        attempts: List<QuestionAttemptEntity>,
        snapshot: InitialSetupSnapshot,
    ): InitialSetupPlanData = map(
        competition = competition,
        subjects = subjects,
        topics = topics,
        questions = questions,
        attempts = attempts,
        difficulties = snapshot.subjectDifficulties,
        knowledge = snapshot.subjectKnowledge,
        priorityOverrides = snapshot.subjectPriorities,
    )

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
        // Passa pela escala de cinco faixas antes de virar bucket: é o caminho sem perda.
    ).toExamPriority().toPlanPriority()
}
