package br.com.estudario.domain

/**
 * A fila das revisões espaçadas com teto por dia. Sem teto, quem sumia três dias voltava para 15 ou
 * 20 revisões de uma vez e largava tudo. Com ele, cada dia recebe no máximo [capFor] revisões; as que
 * passam vão para os dias seguintes, primeiro as mais atrasadas e, entre elas, as de etapa mais cedo
 * (a memória mais frágil). A curva atrasa um ou dois dias em vez de virar uma pilha.
 *
 * É só a conta do dia em que cada revisão aparece: a data marcada na revisão não muda.
 */
object ReviewQueue {
    data class Item(val id: Long, val dueDay: Long, val stage: Int)

    /** Minutos que uma revisão costuma levar (a conta de "uns N minutos" das telas). */
    const val MINUTES_EACH = 4

    /** Teto do dia: um quarto do tempo de estudo do dia, nunca menos que 4 (sem plano, 10). */
    fun capFor(dayLimitMinutes: Int?): Int =
        if (dayLimitMinutes == null || dayLimitMinutes <= 0) 10 else maxOf(4, dayLimitMinutes / 4 / MINUTES_EACH)

    /**
     * A 1ª revisão de um tópico recém-estudado (vence hoje ou venceu ontem) fura a fila: é a memória
     * mais frágil, e esperar atrás das atrasadas fazia o D+1 virar D+5. Antes, com atrasadas acima do
     * teto, o tópico estudado ontem não aparecia hoje.
     */
    private fun fresh(item: Item, today: Long) = item.stage <= 1 && item.dueDay in (today - 1)..today

    /** Em que dia cada revisão aparece, de [today] em diante. [cap] dá o teto de cada dia. */
    fun spread(items: List<Item>, today: Long, cap: (Long) -> Int): Map<Long, Long> {
        val result = HashMap<Long, Long>(items.size)
        val waiting = items.sortedWith(compareBy({ !fresh(it, today) }, { it.dueDay }, { it.stage })).toMutableList()
        var day = today
        // Um ano de folga basta: com o teto mínimo de 4, mais de 1.400 revisões cabem.
        while (waiting.isNotEmpty() && day < today + 366) {
            val limit = cap(day)
            var taken = 0
            val it = waiting.iterator()
            while (it.hasNext() && taken < limit) {
                val item = it.next()
                if (item.dueDay > day) break
                result[item.id] = day; it.remove(); taken++
            }
            day++
        }
        waiting.forEach { result[it.id] = maxOf(it.dueDay, today) }
        return result
    }
}
