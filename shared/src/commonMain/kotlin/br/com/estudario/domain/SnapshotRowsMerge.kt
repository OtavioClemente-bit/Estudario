package br.com.estudario.domain

/**
 * A junção de três vias das tabelas da foto da conta, a mesma no app (SnapshotMerge, org.json) e no site
 * (SiteMerge, kotlinx.serialization). Cada linha é comparada com a base (a última versão que os dois lados
 * tinham em comum):
 *
 * - mudou só de um lado: fica a mudança;
 * - foi criada de um lado: entra;
 * - foi apagada de um lado e o outro não mexeu: sai;
 * - mudou dos dois lados: [resolve] (tarefa do plano fica com o estado mais avançado, o resto com a edição
 *   mais recente).
 *
 * Sem base (primeira sincronização, ou o site com edições guardadas sem internet), nada é apagado: as duas
 * versões se somam.
 */
object SnapshotRowsMerge {
    /** Decisão final (concluída, não feita, reagendada, cancelada) vence estado aberto. */
    val taskStatusRank = mapOf(
        "CONCLUIDA" to 7, "NAO_REALIZADA" to 6, "REPROGRAMADA" to 5, "CANCELADA" to 4,
        "EM_ANDAMENTO" to 3, "PAUSADA" to 2, "PLANEJADA" to 1,
    )

    /** Campos de horário, na ordem em que valem para saber qual edição é a mais recente. */
    val stampFields = listOf("updatedAt", "completedAt", "answeredAt", "createdAt")

    /** Tabelas sem "id": a chave é a combinação destes campos. Outras sem id usam a linha inteira. */
    val compositeKeys = mapOf(
        "planSubjects" to listOf("planId", "subjectId"),
        "studyAvailability" to listOf("planId", "day"),
        "studyDayOverrides" to listOf("planId", "day"),
        "studyPlanRevisions" to listOf("planId", "revision"),
        "errorConceptEntries" to listOf("conceptId", "errorEntryId"),
        "questionTags" to listOf("questionId", "tagId"),
    )

    /** Qual das duas versões da mesma linha fica, quando as duas mudaram. No empate, a local. */
    fun <T> winner(table: String, local: T, remote: T, status: (T) -> String?, stamp: (T) -> Long): T {
        if (table == "planTasks") {
            val lr = taskStatusRank[status(local)] ?: 0
            val rr = taskStatusRank[status(remote)] ?: 0
            if (lr != rr) return if (lr > rr) local else remote
        }
        return if (stamp(remote) > stamp(local)) remote else local
    }

    fun <T : Any> rows(
        base: List<T>?,
        local: List<T>,
        remote: List<T>,
        noBase: Boolean,
        key: (T) -> String,
        canonical: (T) -> String,
        resolve: (T, T) -> T,
    ): List<T> {
        val b = index(base.orEmpty(), key)
        val l = index(local, key)
        val r = index(remote, key)
        val order = LinkedHashSet<String>().apply { addAll(r.keys); addAll(l.keys) }
        val out = ArrayList<T>(order.size)
        fun same(x: T, y: T) = canonical(x) == canonical(y)
        for (k in order) {
            val lv = l[k]
            val rv = r[k]
            val bv = b[k]
            val chosen: T? = when {
                lv == null && rv == null -> null
                bv == null || noBase -> when {
                    lv != null && rv != null -> if (same(lv, rv)) lv else resolve(lv, rv)
                    else -> lv ?: rv
                }
                lv == null -> if (rv == null || same(rv, bv)) null else rv // apagada aqui; lá ninguém mexeu
                rv == null -> if (same(lv, bv)) null else lv // apagada lá; aqui ninguém mexeu
                same(lv, bv) -> rv
                same(rv, bv) -> lv
                same(lv, rv) -> lv
                else -> resolve(lv, rv)
            }
            if (chosen != null) out += chosen
        }
        return out
    }

    private fun <T> index(rows: List<T>, key: (T) -> String): LinkedHashMap<String, T> {
        val map = LinkedHashMap<String, T>()
        rows.forEach { map[key(it)] = it }
        return map
    }
}
