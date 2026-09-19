package br.com.estudario.domain

import br.com.estudario.domain.planner.PlanTaskType
import java.time.LocalDate

/**
 * XP e emblemas.
 *
 * Duas decisões seguram o equilíbrio:
 *
 * 1. **A atividade do plano é a maior fonte.** Fazer a tarefa que o cronograma mandou rende mais do
 *    que responder questão solta, porque é o comportamento que leva à aprovação. Simulado e
 *    discursiva rendem muito por serem caros de fazer.
 * 2. **Nada é acumulável sem limite no mesmo dia.** Questão fora do plano tem teto diário, então
 *    não dá para moer o banco de questões e pular de nível — e o XP nunca é "gasto", só cresce,
 *    por isso ele é recalculado do zero a partir do histórico e não fica guardado em lugar nenhum.
 *    Isso também significa que restaurar um backup devolve o nível certo.
 */
object ProgressEngine {

    // ---------------------------------------------------------------- XP

    private const val QUESTION_XP = 1
    private const val CORRECT_BONUS_XP = 1
    private const val DAILY_QUESTION_CAP = 120
    private const val REVIEW_XP = 12
    private const val TOPIC_STUDIED_XP = 25
    private const val GOAL_XP = 30
    private const val STREAK_BONUS_PER_DAY = 3
    private const val STREAK_BONUS_CAP_DAYS = 10
    private const val MINUTES_PER_XP = 5

    /** Base de cada tipo de tarefa do plano, antes do tempo e dos acertos. */
    fun baseXp(type: PlanTaskType): Int = when (type) {
        PlanTaskType.THEORY -> 12
        PlanTaskType.QUESTIONS -> 12
        PlanTaskType.REVIEW -> 18
        PlanTaskType.ACTIVE_RECALL -> 16
        PlanTaskType.FLASHCARDS -> 10
        PlanTaskType.SIMULATION -> 45
        PlanTaskType.DISCURSIVE -> 40
    }

    fun planTaskXp(type: PlanTaskType, minutes: Int, correct: Int): Int =
        baseXp(type) + (minutes / MINUTES_PER_XP).coerceAtMost(40) + correct.coerceAtMost(80)

    // ---------------------------------------------------------------- recompensa anunciada

    /**
     * O que a pessoa vê ANTES de fazer: [base] é garantido pelo simples fato de concluir,
     * [bonus] é o teto do que os acertos ainda podem somar. Os números vêm das mesmas constantes
     * que pagam de verdade, então o anunciado é exatamente o que cai.
     */
    data class XpReward(val base: Int, val bonus: Int = 0, val bonusLabel: String? = null) {
        val max: Int get() = base + bonus
        val hasBonus: Boolean get() = bonus > 0
    }

    fun previewPlanTask(type: PlanTaskType, plannedMinutes: Int, plannedQuestions: Int): XpReward = XpReward(
        base = baseXp(type) + (plannedMinutes / MINUTES_PER_XP).coerceAtMost(40),
        bonus = plannedQuestions.coerceAtMost(80),
        bonusLabel = if (plannedQuestions > 0) "+1 por acerto" else null,
    )

    /** XP já conquistado por uma tarefa concluída. */
    fun earnedPlanTask(type: PlanTaskType, actualMinutes: Int, correct: Int): Int = planTaskXp(type, actualMinutes, correct)

    fun previewQuestions(count: Int): XpReward = XpReward(
        base = count.coerceAtMost(DAILY_QUESTION_CAP) * QUESTION_XP,
        bonus = count.coerceAtMost(DAILY_QUESTION_CAP) * CORRECT_BONUS_XP,
        bonusLabel = "+1 por acerto",
    )

    fun previewReview(): XpReward = XpReward(REVIEW_XP)

    fun previewTopicStudied(): XpReward = XpReward(TOPIC_STUDIED_XP)

    fun previewDailyGoal(currentStreak: Int): XpReward = XpReward(
        base = GOAL_XP,
        bonus = (currentStreak + 1).coerceAtMost(STREAK_BONUS_CAP_DAYS) * STREAK_BONUS_PER_DAY,
        bonusLabel = "bônus de sequência",
    )

    data class RewardRow(val label: String, val detail: String, val reward: XpReward)

    /** O quadro de recompensas mostrado no perfil. */
    fun rewardTable(currentStreak: Int): List<RewardRow> = listOf(
        RewardRow("Simulado do plano", "Cerca de 2h30 de prova cronometrada", previewPlanTask(PlanTaskType.SIMULATION, 150, 75)),
        RewardRow("Discursiva do plano", "Uma folha escrita à mão", previewPlanTask(PlanTaskType.DISCURSIVE, 90, 0)),
        RewardRow("Meta do dia", "Fechar a meta mantém a sequência viva", previewDailyGoal(currentStreak)),
        RewardRow("Tarefa de questões", "Bloco de 40 min com 20 questões", previewPlanTask(PlanTaskType.QUESTIONS, 40, 20)),
        RewardRow("Tópico estudado", "Marcar um tópico como concluído", previewTopicStudied()),
        RewardRow("Tarefa de revisão", "Bloco de revisão do plano", previewPlanTask(PlanTaskType.REVIEW, 25, 0)),
        RewardRow("Tarefa de teoria", "Bloco de 50 min de teoria", previewPlanTask(PlanTaskType.THEORY, 50, 0)),
        RewardRow("Revisão espaçada", "Fechar uma revisão fora do plano", previewReview()),
        RewardRow("Questão avulsa", "Fora do plano, até $DAILY_QUESTION_CAP por dia", previewQuestions(1)),
    )

    // ---------------------------------------------------------------- níveis

    /** XP acumulado para estar no nível [level]. Nível 1 começa em zero. */
    fun xpForLevel(level: Int): Int = if (level <= 1) 0 else 50 * (level - 1) * level

    fun levelFor(totalXp: Int): Int {
        var level = 1
        while (xpForLevel(level + 1) <= totalXp && level < 200) level++
        return level
    }

    fun levelTitle(level: Int): String = when {
        level >= 50 -> "Lenda do edital"
        level >= 40 -> "Aprovável"
        level >= 30 -> "Veterano"
        level >= 20 -> "Estrategista"
        level >= 15 -> "Disciplinado"
        level >= 10 -> "Concurseiro"
        level >= 5 -> "Estudante"
        else -> "Iniciante"
    }

    // ---------------------------------------------------------------- cálculo

    data class PlanWork(val date: LocalDate, val type: PlanTaskType, val minutes: Int, val correct: Int)

    data class ProgressInput(
        val today: LocalDate,
        val days: List<DailyActivity>,
        val goal: DailyGoal,
        val planWork: List<PlanWork>,
        val bestStreak: Int,
        val currentStreak: Int,
        val totalQuestions: Int,
        val correctQuestions: Int,
        val reviewsCompleted: Int,
        val topicsStudied: Int,
        val topicsTotal: Int,
    )

    data class XpSource(val label: String, val xp: Int)

    data class ProgressSummary(
        val totalXp: Int = 0,
        val level: Int = 1,
        val levelTitle: String = "Iniciante",
        val xpIntoLevel: Int = 0,
        val xpForNextLevel: Int = 100,
        val xpToday: Int = 0,
        val xpThisWeek: Int = 0,
        val sources: List<XpSource> = emptyList(),
        val badges: List<BadgeProgress> = emptyList(),
    ) {
        val levelProgress: Float get() = if (xpForNextLevel <= 0) 1f else (xpIntoLevel.toFloat() / xpForNextLevel).coerceIn(0f, 1f)
        val earnedBadges: List<BadgeProgress> get() = badges.filter { it.earned }
        val nextBadges: List<BadgeProgress> get() = badges.filterNot { it.earned }.sortedByDescending { it.percent }
    }

    fun evaluate(input: ProgressInput): ProgressSummary {
        val byDate = input.days.associateBy { it.date }
        val goalDays = input.days.filter { StreakEngine.isDone(it, input.goal) }.map { it.date }.toSortedSet()
        val planByDate = input.planWork.groupBy { it.date }

        var planXp = 0
        var questionXp = 0
        var reviewXp = 0
        var topicXp = 0
        var goalXp = 0
        var xpToday = 0
        var xpThisWeek = 0
        val weekStart = input.today.minusDays((input.today.dayOfWeek.value - 1).toLong())

        val dates = (byDate.keys + planByDate.keys).toSortedSet()
        dates.forEach { date ->
            val activity = byDate[date]
            var dayXp = 0

            planByDate[date].orEmpty().forEach { work ->
                val xp = planTaskXp(work.type, work.minutes, work.correct)
                planXp += xp
                dayXp += xp
            }
            if (activity != null) {
                // Questão fora do plano: com teto diário, para o nível não virar farm de banco.
                val counted = activity.questions.coerceAtMost(DAILY_QUESTION_CAP)
                val correct = activity.correct.coerceAtMost(counted)
                val xp = counted * QUESTION_XP + correct * CORRECT_BONUS_XP
                questionXp += xp
                dayXp += xp

                reviewXp += activity.reviews * REVIEW_XP
                dayXp += activity.reviews * REVIEW_XP
                topicXp += activity.studySessions * TOPIC_STUDIED_XP
                dayXp += activity.studySessions * TOPIC_STUDIED_XP
            }
            if (date in goalDays) {
                // Bônus cresce com a sequência viva naquele dia, com teto — sequência longa vale,
                // mas não vira uma bola de neve que ofusca o estudo em si.
                var run = 0
                var cursor = date
                while (cursor in goalDays && run < STREAK_BONUS_CAP_DAYS) { run++; cursor = cursor.minusDays(1) }
                val xp = GOAL_XP + run * STREAK_BONUS_PER_DAY
                goalXp += xp
                dayXp += xp
            }

            if (date == input.today) xpToday = dayXp
            if (!date.isBefore(weekStart) && !date.isAfter(input.today)) xpThisWeek += dayXp
        }

        val totalXp = planXp + questionXp + reviewXp + topicXp + goalXp
        val level = levelFor(totalXp)
        val floor = xpForLevel(level)
        val ceiling = xpForLevel(level + 1)

        return ProgressSummary(
            totalXp = totalXp,
            level = level,
            levelTitle = levelTitle(level),
            xpIntoLevel = totalXp - floor,
            xpForNextLevel = (ceiling - floor).coerceAtLeast(1),
            xpToday = xpToday,
            xpThisWeek = xpThisWeek,
            sources = listOf(
                XpSource("Tarefas do plano", planXp),
                XpSource("Metas diárias e sequência", goalXp),
                XpSource("Questões", questionXp),
                XpSource("Revisões", reviewXp),
                XpSource("Tópicos estudados", topicXp),
            ).filter { it.xp > 0 }.sortedByDescending { it.xp },
            badges = BadgeCatalog.evaluate(input, planByDate),
        )
    }
}
