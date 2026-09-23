package br.com.estudario.ui.setup

import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.planner.BlueprintTopic
import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.FeasibilityReport
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanFeasibilityAnalyzer
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanningExplanationBuilder
import br.com.estudario.domain.planner.PlannerWeights
import br.com.estudario.domain.planner.StudyDimensions
import br.com.estudario.domain.planner.StudyEvidence
import br.com.estudario.domain.planner.StudyMethod
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyNeed
import br.com.estudario.domain.planner.StudyNeedCalculator
import br.com.estudario.domain.planner.WorkloadEstimator
import br.com.estudario.domain.planner.toExamPriority
import br.com.estudario.domain.setup.InitialSetupSnapshot
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * O que o assistente sabe sobre a pessoa antes de montar o plano.
 *
 * Isto é cálculo puro, deliberadamente fora do Compose: as telas do assistente só desenham o que
 * este arquivo devolve. É o que permite testar a devolutiva do assistente sem instrumentar
 * interface, e o que garante que a frase mostrada é a mesma decisão que o motor vai tomar, e não
 * um texto escrito à parte que pode divergir do cálculo.
 */
data class SubjectPreview(
    val subjectId: Long,
    val name: String,
    val dimensions: StudyDimensions,
    val need: StudyNeed,
    val topicCount: Int,
) {
    /** A frase que o assistente devolve quando a pessoa classifica esta matéria. */
    val feedback: String get() = PlanningExplanationBuilder.wizardFeedback(name, dimensions)

    /** A explicação de por que ela aparece com a frequência que aparece. */
    val explanation: String get() = PlanningExplanationBuilder.explain(name, need)
}

data class SetupPlannerPreview(
    val subjects: List<SubjectPreview>,
    val feasibility: FeasibilityReport,
    val weeklyMinutes: Int,
    val sessionMinutes: Int,
    val examDate: LocalDate?,
    val topicCount: Int,
) {
    /** Maior prioridade na prova. Empate desfeito por id, para o resumo não dançar entre aberturas. */
    val topPriority: SubjectPreview?
        get() = subjects.maxWithOrNull(
            compareBy<SubjectPreview> { it.dimensions.examPriority.normalized }.thenByDescending { it.subjectId },
        )

    /** Maior dificuldade declarada. */
    val hardest: SubjectPreview?
        get() = subjects
            .filter { it.dimensions.personalDifficulty >= PersonalDifficulty.HARD }
            .maxWithOrNull(
                compareBy<SubjectPreview> { it.dimensions.personalDifficulty.normalized }.thenByDescending { it.subjectId },
            )

    /** As matérias que mais vão aparecer, na ordem da necessidade. */
    fun mostDemanding(limit: Int = 3): List<SubjectPreview> =
        subjects.sortedWith(compareByDescending<SubjectPreview> { it.need.score }.thenBy { it.subjectId }).take(limit)

    /** O "como montamos isso": uma linha por matéria que realmente puxou o plano. */
    fun highlights(limit: Int = 3): List<String> = mostDemanding(limit).map { it.explanation }

    val feasibilityMessage: String get() = PlanningExplanationBuilder.feasibility(feasibility)
}

object SetupPlannerPreviewFactory {

    /**
     * Os estágios do cálculo, na ordem em que acontecem de verdade.
     *
     * A tela de processamento marca cada um quando ele termina. Se um dia o cálculo deixar de ter
     * uma destas etapas, a etapa sai desta lista, a tela não mostra progresso que não existe.
     */
    val PREVIEW_STAGES = listOf(
        "Analisando sua disponibilidade",
        "Pesando as matérias do edital",
        "Estimando o conteúdo restante",
        "Verificando o tempo até a prova",
    )


    /**
     * Monta a prévia a partir do que o assistente já coletou.
     *
     * Note que nada aqui exige que a pessoa tenha respondido tudo: matéria sem resposta usa os
     * padrões neutros de [InitialSetupSnapshot.dimensionsFor]. O assistente pode mostrar um resumo
     * útil mesmo para quem só clicou "continuar" em tudo.
     */
    fun build(
        snapshot: InitialSetupSnapshot,
        subjects: List<SubjectEntity>,
        topics: List<TopicEntity>,
        officialPriorities: Map<Long, PlanPriority>,
        today: LocalDate = LocalDate.now(),
        /**
         * Avisa a cada etapa **real** concluída, para o assistente mostrar o cálculo acontecendo.
         * Os estágios são os de [PREVIEW_STAGES], nesta ordem.
         */
        onStage: (Int) -> Unit = {},
    ): SetupPlannerPreview {
        val examDate = snapshot.examDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        val daysUntilExam = examDate
            ?.let { ChronoUnit.DAYS.between(today, it).toInt() }
            ?.takeIf { it >= 0 }
        val config = StudyMethodConfig.forProfile(snapshot.studyProfile).copy(
            blockMinutes = snapshot.sessionMinutes.coerceIn(15, 180),
            interleaveSubjects = snapshot.variety.interleave,
            dailySubjectSharePercent = snapshot.variety.dailySubjectSharePercent,
        )
        val phase = StudyMethod.phaseAt(
            StudyMethod.phases(today, examDate, snapshot.studyProfile),
            today,
        )
        val weights = PlannerWeights.forPhase(phase.kind)
        onStage(1) // disponibilidade e fase lidas

        val topicsBySubject = topics.groupBy { it.subjectId }
        val previews = subjects.map { subject ->
            val key = subject.id.toString()
            val official = (officialPriorities[subject.id] ?: PlanPriority.MEDIUM).toExamPriority()
            val dimensions = snapshot.dimensionsFor(key, official)
            val subjectTopics = topicsBySubject[subject.id].orEmpty()
            val evidence = StudyEvidence(
                coveredTopics = subjectTopics.count { it.status != TopicStatus.NAO_ESTUDADO },
                totalTopics = subjectTopics.size,
            )
            SubjectPreview(
                subjectId = subject.id,
                name = subject.name,
                dimensions = dimensions,
                need = StudyNeedCalculator.evaluate(
                    subjectId = subject.id,
                    dimensions = dimensions,
                    evidence = evidence,
                    weights = weights,
                    daysUntilExam = daysUntilExam,
                ),
                topicCount = subjectTopics.size,
            )
        }

        onStage(2) // necessidade por matéria calculada

        val pending = topics
            .filter { it.status == TopicStatus.NAO_ESTUDADO }
            .map { topic ->
                BlueprintTopic(
                    topicId = topic.id,
                    subjectId = topic.subjectId,
                    title = topic.title,
                    position = topic.position,
                    studied = false,
                )
            }
        val needsByTopic = pending.associate { topic ->
            topic.topicId to (previews.firstOrNull { it.subjectId == topic.subjectId }?.need
                ?: StudyNeedCalculator.evaluate(topic.subjectId, weights = weights, daysUntilExam = daysUntilExam))
        }
        val workload = WorkloadEstimator.estimate(
            pendingTopics = pending,
            config = config,
            needs = needsByTopic,
        )
        onStage(3) // carga restante estimada

        val report = PlanFeasibilityAnalyzer.analyze(
            today = today,
            examDate = examDate,
            planStart = today,
            weeklyCapacityMinutes = snapshot.weeklyMinutes,
            workload = workload,
            topicCount = topics.size,
            needsBySubject = previews.associate { it.subjectId to it.need },
        )
        onStage(4) // viabilidade verificada

        return SetupPlannerPreview(
            subjects = previews,
            feasibility = report,
            weeklyMinutes = snapshot.weeklyMinutes,
            sessionMinutes = config.blockMinutes,
            examDate = examDate,
            topicCount = topics.size,
        )
    }

    /**
     * Rótulo de prioridade sugerido para uma matéria que ainda não foi ajustada à mão.
     *
     * O assistente mostra a prioridade **já preenchida**, a pessoa corrige o que discordar, em vez
     * de preencher do zero. É a diferença entre "responda 8 perguntas" e "confira o que eu entendi".
     */
    fun suggestedPriority(officialPriorities: Map<Long, PlanPriority>, subjectId: Long): ExamPriority =
        (officialPriorities[subjectId] ?: PlanPriority.MEDIUM).toExamPriority()
}
