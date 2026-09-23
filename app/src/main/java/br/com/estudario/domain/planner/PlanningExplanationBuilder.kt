package br.com.estudario.domain.planner

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

/**
 * As frases do plano.
 *
 * Sem IA: cada texto nasce de um estado que o motor já calculou. É um catálogo de templates
 * combinados por [PlannerReasonCode], o que mantém a experiência offline, determinística e
 * traduzível, e faz o assistente responder à pessoa com o que ele realmente decidiu, não com uma
 * frase genérica de encorajamento.
 *
 * Tom: direto e adulto. Nada de comemoração, exclamação ou emoji.
 */
object PlanningExplanationBuilder {

    private val DATE = DateTimeFormatter.ofPattern("dd/MM")

    /**
     * Por que esta matéria aparece com esta frequência. Uma frase, com os motivos que realmente
     * pesaram, é o equivalente à explicação que uma IA daria.
     */
    fun explain(subjectName: String, need: StudyNeed, limit: Int = 3): String {
        val parts = need.topReasons(limit).mapNotNull { phraseFor(it) }
        if (parts.isEmpty()) return "$subjectName entra no rodízio normal do seu edital."
        return "$subjectName ${verbFor(need)} " + join(parts) + "."
    }

    /** A mesma explicação em itens, para a tela "como montamos isto". */
    fun explainAsItems(subjectName: String, need: StudyNeed, limit: Int = 3): List<String> =
        need.topReasons(limit).mapNotNull { reason ->
            phraseFor(reason)?.let { "$subjectName: $it" }
        }

    private fun verbFor(need: StudyNeed): String = when {
        need.score >= 0.70 -> "aparece com mais frequência porque"
        need.score >= 0.45 -> "tem espaço garantido no plano porque"
        else -> "aparece com frequência menor porque"
    }

    private fun phraseFor(reason: PlannerReason): String? = when (reason.code) {
        PlannerReasonCode.HIGH_EXAM_PRIORITY -> "tem prioridade alta na prova"
        PlannerReasonCode.LOW_EXAM_PRIORITY -> "tem peso menor neste edital"
        PlannerReasonCode.HIGH_PERSONAL_DIFFICULTY -> "você marcou dificuldade alta"
        PlannerReasonCode.DIFFICULTY_RELAXED_BY_EVIDENCE ->
            "seu desempenho já mostra mais facilidade do que você esperava"
        PlannerReasonCode.DIFFICULTY_RAISED_BY_EVIDENCE ->
            "os resultados estão vindo abaixo do que você esperava"
        PlannerReasonCode.LOW_RECENT_ACCURACY ->
            "seu acerto recente ficou em ${percent(reason.observed)}"
        PlannerReasonCode.HIGH_RECENT_ACCURACY ->
            "seu acerto recente está em ${percent(reason.observed)}"
        PlannerReasonCode.INSUFFICIENT_SAMPLE -> null
        PlannerReasonCode.CONTENT_NOT_STARTED -> "o conteúdo ainda não foi iniciado"
        PlannerReasonCode.CONTENT_REMAINING -> "ainda há bastante conteúdo pela frente"
        PlannerReasonCode.CONTENT_COVERED -> "o conteúdo já está coberto"
        PlannerReasonCode.STRONG_PRIOR_KNOWLEDGE -> "você já chegou com base nessa matéria"
        PlannerReasonCode.REVISION_DUE -> "há revisão marcada para agora"
        PlannerReasonCode.REVISION_OVERDUE -> "há revisão atrasada"
        PlannerReasonCode.LONG_TIME_WITHOUT_CONTACT ->
            "faz ${reason.observed?.roundToInt() ?: 0} dias sem contato"
        PlannerReasonCode.TASKS_MISSED -> "algumas tarefas ficaram para trás"
        PlannerReasonCode.EXAM_APPROACHING -> "a prova está próxima"
        PlannerReasonCode.MAINTENANCE_ONLY -> "ela entra apenas em manutenção"
    }

    /**
     * A devolutiva imediata do assistente, logo depois de a pessoa classificar uma matéria.
     *
     * É aqui que o wizard deixa de parecer formulário: a resposta vem das regras reais, cruzando
     * os três eixos, e por isso ela muda conforme a combinação, como uma conversa mudaria.
     */
    fun wizardFeedback(subjectName: String, dimensions: StudyDimensions): String {
        val high = dimensions.examPriority >= ExamPriority.HIGH
        val hard = dimensions.personalDifficulty >= PersonalDifficulty.HARD
        val known = dimensions.initialKnowledge >= InitialKnowledge.SOLID
        return when {
            high && hard && !known ->
                "$subjectName pesa muito na prova e é onde você tem mais dificuldade. Vai receber mais prática e revisões mais próximas."
            high && hard && known ->
                "$subjectName é decisiva e trabalhosa, mas você já tem base. Frequência alta, com foco em questões em vez de teoria."
            high && !hard && known ->
                "Alta importância e boa base. Vamos manter $subjectName presente sem gastar tempo com o que você já domina."
            high && !hard ->
                "$subjectName vale muito ponto e não costuma te travar. Espaço garantido, com ritmo de questões desde o começo."
            hard && !high ->
                "$subjectName não é a de maior peso, mas exige esforço. Vai aparecer em blocos menores e mais vezes."
            known ->
                "Você já tem base em $subjectName. O plano começa por consolidação, não por teoria."
            else ->
                "$subjectName entra no rodízio padrão. Dá para ajustar depois, com os dados das suas questões."
        }
    }

    /** Resumo honesto da viabilidade, com a conta aberta. */
    fun feasibility(report: FeasibilityReport): String = when (report.verdict) {
        FeasibilityVerdict.NO_EXAM_DATE ->
            "Sem data de prova definida, o plano segue por cobertura do edital. " +
                (report.projectedCoverageDate?.let { "No ritmo atual, o conteúdo fecha em ${it.format(DATE)}." } ?: "")
        FeasibilityVerdict.COMFORTABLE ->
            "Seu tempo está confortável. Com ${hours(report.weeklyCapacityMinutes)} por semana há espaço para " +
                "terminar o conteúdo até ${report.targetCoverageDate?.format(DATE)} e ainda reservar " +
                "${report.consolidationDays} dias de consolidação antes da prova."
        FeasibilityVerdict.TIGHT ->
            "Sua agenda está apertada para este edital. Com ${hours(report.weeklyCapacityMinutes)} por semana o " +
                "conteúdo fecha em ${report.projectedCoverageDate?.format(DATE)}, depois da meta de " +
                "${report.targetCoverageDate?.format(DATE)}, sobra menos tempo de revisão final."
        FeasibilityVerdict.INFEASIBLE ->
            "Com ${hours(report.weeklyCapacityMinutes)} por semana, a projeção ultrapassa a data da prova. " +
                "Dá para priorizar o que mais vale ou aumentar a carga semanal."
    }

    /** As opções que a pessoa escolhe quando o tempo não fecha. O motor calcula; ela decide. */
    fun describeOption(option: FeasibilityOption): String = when (option) {
        is FeasibilityOption.IncreaseWeeklyLoad ->
            "Mais ${hours(option.extraWeeklyMinutes)} por semana: conclusão em " +
                "${option.projectedCoverageDate.format(DATE)}" +
                if (option.daysSaved > 0) ", ${option.daysSaved} dias antes." else "."
        is FeasibilityOption.PrioritizeByWeight ->
            "Manter a carga atual e priorizar o conteúdo de maior peso: cabe cerca de " +
                "${(option.coveredShare * 100).roundToInt()}% do edital, e o restante fica marcado como descoberto."
        is FeasibilityOption.ShortenConsolidation ->
            "Reduzir a consolidação para ${option.newConsolidationDays} dias, movendo a meta de conteúdo para " +
                "${option.newTargetCoverageDate.format(DATE)}."
    }

    /** Linha de resumo do perfil, para a tela anterior à geração. */
    fun profileSummary(
        weeklyMinutes: Int,
        sessionMinutes: Int,
        examDate: LocalDate?,
        today: LocalDate,
        topPrioritySubject: String?,
        hardestSubject: String?,
    ): List<Pair<String, String>> = buildList {
        add("Disponibilidade" to "${hours(weeklyMinutes)} / semana")
        add("Sessões" to "$sessionMinutes min")
        if (examDate != null) {
            val weeks = java.time.temporal.ChronoUnit.WEEKS.between(today, examDate).coerceAtLeast(0)
            add("Prova" to "$weeks semanas")
        } else {
            add("Prova" to "sem data definida")
        }
        topPrioritySubject?.let { add("Maior prioridade" to it) }
        hardestSubject?.let { add("Maior dificuldade" to it) }
    }

    private fun hours(minutes: Int): String {
        if (minutes <= 0) return "0h"
        val h = minutes / 60
        val m = minutes % 60
        return when {
            h == 0 -> "${m}min"
            m == 0 -> "${h}h"
            else -> "${h}h${m}"
        }
    }

    private fun percent(value: Double?): String =
        value?.let { "${(it * 100).roundToInt()}%" } ?: "um valor baixo"

    private fun join(parts: List<String>): String = when (parts.size) {
        1 -> parts.first()
        2 -> "${parts[0]} e ${parts[1]}"
        else -> parts.dropLast(1).joinToString(", ") + " e " + parts.last()
    }
}
