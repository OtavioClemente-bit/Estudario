package br.com.estudario.domain.planner

/**
 * "Encaixar nos próximos dias" para o que o motor não reorganiza: tarefa de plano trazido da IA (o motor
 * não mexe nelas), tarefa sem matéria ou presa num dia travado que já passou. Antes o botão só
 * replanejava, essas tarefas continuavam no dia antigo e o aviso de atraso voltava a cada abertura do app.
 *
 * Cada atrasada (na ordem recebida) vai para o primeiro dia, de hoje em diante, em que ainda cabe no
 * tempo do dia; se nenhum dia da janela comporta, vai para o dia com estudo menos carregado. Mesma
 * conta no app e no site.
 */
object OverdueFit {
    data class Item(val id: String, val minutes: Int)

    /** Dia da semana ISO (1 = segunda ... 7 = domingo) de um dia em epochDay. */
    fun isoDayOfWeek(epochDay: Long): Int = (((epochDay + 3) % 7 + 7) % 7).toInt() + 1

    /**
     * @param capacity minutos de estudo do dia (0 = sem estudo).
     * @param used minutos já ocupados por dia (tarefas vigentes de hoje em diante).
     * @return id da tarefa -> novo dia.
     */
    fun distribute(late: List<Item>, today: Long, capacity: (Long) -> Int, used: Map<Long, Int>, horizonDays: Int = 14): Map<String, Long> {
        val days = (0 until horizonDays.coerceAtLeast(1)).map { today + it }
        val studyDays = days.filter { capacity(it) > 0 }.ifEmpty { listOf(today) }
        val load = HashMap(used)
        val out = LinkedHashMap<String, Long>()
        for (item in late) {
            val minutes = item.minutes.coerceAtLeast(0)
            val day = studyDays.firstOrNull { (load[it] ?: 0) + minutes <= capacity(it) }
                ?: studyDays.minByOrNull { (load[it] ?: 0) - capacity(it) }!!
            out[item.id] = day
            load[day] = (load[day] ?: 0) + minutes
        }
        return out
    }
}
