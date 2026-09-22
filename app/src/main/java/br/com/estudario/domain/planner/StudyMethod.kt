package br.com.estudario.domain.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * O método do plano sem IA.
 *
 * A ideia é que o app não sorteie tarefas: ele aplica, sempre do mesmo jeito, o que funciona em
 * preparação para concurso — teoria com consolidação em questões, revisão espaçada, simulado
 * periódico, prioridade para matéria de peso e para o que a pessoa erra mais, e o tempo dividido em
 * blocos do tamanho que ela aguenta. É engessado de propósito: dá para conferir por que cada
 * tarefa entrou.
 */
enum class StudyProfile(val label: String, val summary: String) {
    DO_ZERO(
        "Começando do zero",
        "Ainda não vi a maior parte do edital. Prioriza teoria, com questões logo depois para fixar.",
    ),
    APROFUNDANDO(
        "Já vi a teoria",
        "Conheço o conteúdo e preciso treinar. Equilibra questões, revisão e a teoria que falta.",
    ),
    RETA_FINAL(
        "Reta final",
        "A prova está perto. Sem teoria nova: questões, revisão do que já estudei e simulados.",
    ),
}

/** Divisão do tempo entre teoria, questões e revisão. Sempre soma 100. */
data class StudyMix(val theoryPercent: Int, val questionsPercent: Int, val reviewPercent: Int) {
    init { require(theoryPercent + questionsPercent + reviewPercent == 100) { "A divisão do tempo precisa somar 100%." } }

    fun describe() = "$theoryPercent% teoria • $questionsPercent% questões • $reviewPercent% revisão"

    companion object {
        val BASE = StudyMix(55, 30, 15)
        val APROFUNDAMENTO = StudyMix(30, 45, 25)
        val RETA_FINAL = StudyMix(0, 55, 45)
    }
}

enum class PlanPhaseKind(val label: String) {
    BASE("Base"),
    APROFUNDAMENTO("Aprofundamento"),
    RETA_FINAL("Reta final"),
}

data class StudyPhase(
    val kind: PlanPhaseKind,
    val start: LocalDate,
    val end: LocalDate,
    val mix: StudyMix,
    val objective: String,
    val criteria: String,
) {
    val days: Int get() = (ChronoUnit.DAYS.between(start, end).toInt() + 1).coerceAtLeast(1)
    fun contains(date: LocalDate) = !date.isBefore(start) && !date.isAfter(end)
}

/**
 * Configuração escolhida no assistente. Fica gravada no plano para que replanejar continue
 * seguindo as mesmas regras.
 */
data class StudyMethodConfig(
    val profile: StudyProfile = StudyProfile.DO_ZERO,
    /** Tamanho do bloco de estudo. Toda tarefa é um múltiplo disso. */
    val blockMinutes: Int = 50,
    val weeklyQuestionsTarget: Int = 100,
    val questionsPerTopic: Int = 15,
    val simulationsPerMonth: Int = 2,
    val discursivesPerMonth: Int = 0,
    val includeFlashcards: Boolean = true,
    /** Misturar matérias no mesmo dia em vez de emendar horas da mesma. */
    val interleaveSubjects: Boolean = true,
) {
    init {
        require(blockMinutes in 15..180) { "O bloco precisa ter entre 15 e 180 minutos." }
        require(weeklyQuestionsTarget >= 0)
        require(questionsPerTopic >= 0)
        require(simulationsPerMonth in 0..8)
        require(discursivesPerMonth in 0..12)
    }

    /** Minutos por questão usados para dimensionar as tarefas de questões. */
    val minutesPerQuestion: Int get() = 2

    companion object {
        val BLOCK_OPTIONS = listOf(25, 30, 45, 50, 60, 90)
        fun forProfile(profile: StudyProfile) = when (profile) {
            StudyProfile.DO_ZERO -> StudyMethodConfig(profile, weeklyQuestionsTarget = 80, simulationsPerMonth = 1)
            StudyProfile.APROFUNDANDO -> StudyMethodConfig(profile, weeklyQuestionsTarget = 150, simulationsPerMonth = 2)
            StudyProfile.RETA_FINAL -> StudyMethodConfig(profile, weeklyQuestionsTarget = 250, simulationsPerMonth = 4)
        }
    }
}

object StudyMethod {
    /** Reta final nunca menor que isso, nem maior que isso — mesmo em preparação longa. */
    private const val MIN_FINAL_DAYS = 21
    private const val MAX_FINAL_DAYS = 60
    private const val MIN_DEEP_DAYS = 21

    /**
     * Divide o tempo até a prova em fases. Sem data de prova existe uma fase só, do perfil
     * escolhido — não dá para prometer reta final sem saber quando a prova é.
     */
    fun phases(start: LocalDate, exam: LocalDate?, profile: StudyProfile): List<StudyPhase> {
        if (profile == StudyProfile.RETA_FINAL) {
            return listOf(
                StudyPhase(
                    PlanPhaseKind.RETA_FINAL,
                    start,
                    exam ?: start.plusDays(89),
                    StudyMix.RETA_FINAL,
                    "Chegar na prova com o edital revisado e ritmo de questões alto.",
                    "Simulados em dia e revisão fechada nas matérias de maior peso.",
                ),
            )
        }
        val mixInicial = if (profile == StudyProfile.DO_ZERO) StudyMix.BASE else StudyMix.APROFUNDAMENTO
        if (exam == null || !exam.isAfter(start)) {
            return listOf(
                StudyPhase(
                    if (profile == StudyProfile.DO_ZERO) PlanPhaseKind.BASE else PlanPhaseKind.APROFUNDAMENTO,
                    start,
                    start.plusDays(179),
                    mixInicial,
                    "Cobrir o edital com consolidação em questões e revisão espaçada.",
                    "Todos os tópicos vistos ao menos uma vez, com revisões em dia.",
                ),
            )
        }
        val total = ChronoUnit.DAYS.between(start, exam).toInt()
        // Preparação muito curta vira reta final direto: não adianta programar base de teoria.
        if (total <= MIN_FINAL_DAYS + 7) {
            return listOf(
                StudyPhase(PlanPhaseKind.RETA_FINAL, start, exam, StudyMix.RETA_FINAL, "Revisar e treinar até a prova.", "Simulados e revisão em dia."),
            )
        }
        val finalDays = (total * 20 / 100).coerceIn(MIN_FINAL_DAYS, MAX_FINAL_DAYS)
        val restante = total - finalDays
        val deepDays = (restante * 40 / 100).coerceAtLeast(MIN_DEEP_DAYS).coerceAtMost(restante - 7)
        val baseDays = restante - deepDays
        val baseEnd = start.plusDays((baseDays - 1).toLong())
        val deepEnd = baseEnd.plusDays(deepDays.toLong())
        return listOf(
            StudyPhase(
                PlanPhaseKind.BASE, start, baseEnd, mixInicial,
                "Ver o edital inteiro, cada tópico com questões logo depois da teoria.",
                "Edital coberto e revisões D+1/D+7 acontecendo no dia.",
            ),
            StudyPhase(
                PlanPhaseKind.APROFUNDAMENTO, baseEnd.plusDays(1), deepEnd, StudyMix.APROFUNDAMENTO,
                "Trocar volume de teoria por volume de questões e fechar os pontos fracos.",
                "Acerto acima de 70% nas matérias de maior peso.",
            ),
            StudyPhase(
                PlanPhaseKind.RETA_FINAL, deepEnd.plusDays(1), exam, StudyMix.RETA_FINAL,
                "Sem teoria nova: revisão, questões e simulados cronometrados.",
                "Edital revisado e simulados semanais feitos.",
            ),
        )
    }

    fun phaseAt(phases: List<StudyPhase>, date: LocalDate): StudyPhase =
        phases.firstOrNull { it.contains(date) } ?: phases.lastOrNull()
            ?: StudyPhase(PlanPhaseKind.BASE, date, date.plusDays(179), StudyMix.BASE, "", "")

    /**
     * Peso da matéria (1 a 5) → fatia do tempo. Uma matéria peso 5 recebe cerca de cinco vezes o
     * tempo de uma peso 1, que é como as bancas costumam distribuir as questões.
     */
    fun weightOf(priority: PlanPriority, override: Int?): Int = override?.coerceIn(1, 5) ?: when (priority) {
        PlanPriority.CRITICAL -> 5
        PlanPriority.HIGH -> 4
        PlanPriority.MEDIUM -> 3
        PlanPriority.LOW -> 2
    }

    /** Arredonda para o bloco mais próximo, nunca abaixo de um bloco. */
    fun toBlocks(minutes: Int, blockMinutes: Int): Int {
        if (minutes <= blockMinutes) return blockMinutes
        val blocks = (minutes + blockMinutes / 2) / blockMinutes
        return blocks.coerceAtLeast(1) * blockMinutes
    }
}
