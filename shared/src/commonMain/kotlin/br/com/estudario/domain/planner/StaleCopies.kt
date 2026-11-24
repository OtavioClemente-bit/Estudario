package br.com.estudario.domain.planner

/**
 * Cópias velhas de tarefas: um aparelho reorganizou o dia (a tarefa virou REPROGRAMADA e nasceu uma
 * cópia), enquanto o outro concluía a original. Na sincronização a original volta como concluída e a
 * cópia também fica, e a pessoa via a mesma teoria duas vezes, uma delas pedindo para estudar de novo.
 *
 * A cópia de uma tarefa só nasce de tarefa planejada ou em andamento; se alguma tarefa acima na
 * corrente (replannedFrom) está concluída, a cópia ainda planejada (não começada) é velha e sai.
 */
object StaleCopies {
    /** [slot]: dia e tipo da tarefa ("106|THEORY"); null quando quem chama não sabe (aí só vale a regra da corrente). */
    data class Row(val id: String, val replannedFrom: String?, val status: String, val slot: String? = null)

    /**
     * Só a concluída encerra a corrente. A NAO_REALIZADA não: é o que o motor grava na tarefa de um dia que
     * passou, junto com a cópia dela nos próximos dias (a continuação de verdade). Antes ela contava como fim
     * e toda cópia de estudo atrasado era cancelada a cada sincronização (e a cada abertura do site); o
     * motor marcava de novo com outro id na reorganização seguinte e o plano mudava "do nada" ao longo do dia.
     */
    private val finished = setOf("CONCLUIDA")
    private val open = setOf("PLANEJADA", "NAO_REALIZADA")

    fun ids(rows: List<Row>): Set<String> {
        val byId = rows.associateBy { it.id }
        val out = hashSetOf<String>()
        // Os dois aparelhos reorganizaram a mesma tarefa para o mesmo dia, cada um com a sua cópia: na
        // sincronização ficavam as duas vivas (a mesma teoria duas vezes no dia). Fica uma (o menor id, igual
        // nos dois aparelhos). Partes de uma teoria dividida caem em dias diferentes e não entram aqui.
        rows.filter { it.status == "PLANEJADA" && it.replannedFrom != null && it.slot != null }
            .groupBy { it.replannedFrom + "|" + it.slot }
            .values.forEach { same -> if (same.size > 1) out += same.map { it.id }.sorted().drop(1) }
        for (row in rows) {
            if (row.status !in open || row.replannedFrom == null) continue
            var from = row.replannedFrom
            val seen = hashSetOf(row.id)
            while (from != null && seen.add(from)) {
                val source = byId[from] ?: break
                if (source.status in finished) { out += row.id; break }
                from = source.replannedFrom
            }
        }
        return out
    }
}
