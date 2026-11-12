package br.com.estudario.domain.planner

/**
 * Cópias velhas de tarefas: um aparelho reorganizou o dia (a tarefa virou REPROGRAMADA e nasceu uma
 * cópia), enquanto o outro concluía a original. Na sincronização a original volta como concluída e a
 * cópia também fica, e a pessoa via a mesma teoria duas vezes, uma delas pedindo para estudar de novo.
 *
 * A cópia de uma tarefa só nasce de tarefa planejada ou em andamento; se alguma tarefa acima na
 * corrente (replannedFrom) está concluída ou foi pulada, a cópia ainda planejada (não começada) é velha e sai.
 */
object StaleCopies {
    data class Row(val id: String, val replannedFrom: String?, val status: String)

    private val finished = setOf("CONCLUIDA", "NAO_REALIZADA")
    private val open = setOf("PLANEJADA", "NAO_REALIZADA")

    fun ids(rows: List<Row>): Set<String> {
        val byId = rows.associateBy { it.id }
        val out = hashSetOf<String>()
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
