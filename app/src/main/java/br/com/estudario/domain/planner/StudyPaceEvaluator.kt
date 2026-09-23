package br.com.estudario.domain.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * A leitura do ritmo: a previsão de cobertura do edital comparada com a data da prova.
 *
 * Fica no domínio de propósito. A Home só escolhe como apresentar o resultado, a decisão de o que
 * significa "ritmo adequado" é regra do produto, precisa ser testável e vale igual em qualquer tela
 * que venha a mostrar isso (Home, Plano, notificação).
 *
 * A margem de folga é intencionalmente generosa: terminar o edital no dia da prova não é terminar a
 * tempo, porque revisão e simulado ainda precisam caber. [COMFORTABLE_MARGIN_DAYS] é o mínimo de
 * folga para o ritmo ser chamado de adequado.
 */
object StudyPaceEvaluator {

    const val COMFORTABLE_MARGIN_DAYS = 14L

    sealed interface Pace {
        /** Sem previsão possível, plano sem capacidade semanal declarada, ou sem tarefas restantes. */
        data object Unknown : Pace

        /** Há previsão, mas nenhuma data de prova cadastrada: mostra só a data estimada. */
        data class NoExamDate(val forecast: LocalDate) : Pace

        /** O edital fecha com folga confortável antes da prova. */
        data class Comfortable(val forecast: LocalDate, val daysBeforeExam: Long) : Pace

        /** O edital fecha antes da prova, mas em cima da hora. */
        data class Tight(val forecast: LocalDate, val daysBeforeExam: Long) : Pace

        /** No ritmo atual, sobra conteúdo depois da prova. */
        data class Behind(val forecast: LocalDate, val daysAfterExam: Long) : Pace

        /** Todo o conteúdo previsto já foi coberto. */
        data object Complete : Pace
    }

    /**
     * [forecast] é a data estimada de cobertura vinda de [StudyPlanForecastCalculator]; [examDate] é
     * a data da prova cadastrada no plano, quando existir. [hasRemainingWork] separa "não dá para
     * prever" de "não falta nada", quem chama só precisa saber se ainda há tarefa em aberto, sem
     * repetir aqui a conta de minutos que o planejador já faz.
     */
    fun evaluate(
        today: LocalDate,
        forecast: LocalDate?,
        examDate: LocalDate?,
        hasRemainingWork: Boolean,
    ): Pace {
        if (!hasRemainingWork) return Pace.Complete
        if (forecast == null) return Pace.Unknown

        val effectiveForecast = if (forecast.isBefore(today)) today else forecast
        if (examDate == null) return Pace.NoExamDate(effectiveForecast)

        val days = ChronoUnit.DAYS.between(effectiveForecast, examDate)
        return when {
            days < 0 -> Pace.Behind(effectiveForecast, -days)
            days >= COMFORTABLE_MARGIN_DAYS -> Pace.Comfortable(effectiveForecast, days)
            else -> Pace.Tight(effectiveForecast, days)
        }
    }
}
