package br.com.estudario.data.sync

import org.json.JSONArray
import org.json.JSONObject

/**
 * Junta as duas versões quando o celular e o site mudaram ao mesmo tempo, em vez de uma apagar a
 * outra. Compara cada linha (tarefa, questão respondida, revisão...) com a última versão que os dois
 * tinham em comum (a base):
 *
 * - mudou só de um lado: fica a mudança;
 * - foi criada de um lado: entra;
 * - foi apagada de um lado e o outro não mexeu: sai;
 * - mudou dos dois lados: tarefa do plano fica com o estado mais avançado (concluída vence planejada),
 *   o resto com a edição mais recente (updatedAt/completedAt), e no empate a deste aparelho.
 *
 * Sem base (primeira sincronização deste aparelho), nada é apagado: as duas versões se somam.
 */
object SnapshotMerge {
    /** Tabelas sem "id" e o estado de cada tarefa: as regras são as do site também (domain/SnapshotRowsMerge). */
    private val compositeKeys = br.com.estudario.domain.SnapshotRowsMerge.compositeKeys

    fun merge(base: JSONObject?, local: JSONObject, remote: JSONObject): JSONObject {
        val out = JSONObject()
        val keys = (local.keys().asSequence() + remote.keys().asSequence()).toCollection(LinkedHashSet())
        for (key in keys) {
            val l = local.opt(key)
            val r = remote.opt(key)
            val b = base?.opt(key)
            when {
                l is JSONArray && r is JSONArray -> out.put(key, mergeArray(key, b as? JSONArray, l, r, base == null))
                // Visual do Folha: vale o trocado por último (em qualquer aparelho).
                key == br.com.estudario.data.FolhaLookStore.SNAPSHOT_KEY -> out.put(key, br.com.estudario.data.FolhaLookStore.newer(l as? JSONObject, r as? JSONObject))
                // Meta diária e peças vistas: a meta mudada por último, as peças vistas nos dois.
                key == SyncedPrefs.SNAPSHOT_KEY -> out.put(key, SyncedPrefs.merge(l as? JSONObject, r as? JSONObject))
                l == null -> out.put(key, r)
                r == null -> out.put(key, l)
                // Campo simples (formato, versão): fica o que mudou em relação à base; senão, o daqui.
                b != null && same(l, b) -> out.put(key, r)
                else -> out.put(key, l)
            }
        }
        return withoutStaleCopies(out)
    }

    /**
     * Cópia de tarefa criada num aparelho ao reorganizar o dia enquanto o outro concluía a original:
     * fica cancelada (StaleCopies), para a mesma teoria não voltar pedindo estudo nem aparecer duas vezes.
     */
    fun withoutStaleCopies(snapshot: JSONObject): JSONObject {
        val tasks = snapshot.optJSONArray("planTasks") ?: return snapshot
        val rows = (0 until tasks.length()).mapNotNull { i ->
            val o = tasks.optJSONObject(i) ?: return@mapNotNull null
            val id = o.optString("id").takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            br.com.estudario.domain.planner.StaleCopies.Row(id, o.optString("replannedFrom").takeIf { !o.isNull("replannedFrom") && it.isNotEmpty() }, o.optString("status"), "${o.opt("day")}|${o.optString("type")}")
        }
        val stale = br.com.estudario.domain.planner.StaleCopies.ids(rows)
        if (stale.isEmpty()) return snapshot
        val at = System.currentTimeMillis()
        for (i in 0 until tasks.length()) {
            val o = tasks.optJSONObject(i) ?: continue
            if (o.optString("id") in stale) o.put("status", "CANCELADA").put("updatedAt", at)
        }
        return snapshot
    }

    private fun mergeArray(table: String, base: JSONArray?, local: JSONArray, remote: JSONArray, noBase: Boolean): JSONArray =
        JSONArray(br.com.estudario.domain.SnapshotRowsMerge.rows(items(base), items(local), items(remote), noBase, { keyOf(table, it) }, ::canonical) { l, r -> resolve(table, l, r) })

    private fun items(array: JSONArray?): List<Any> = if (array == null) emptyList() else (0 until array.length()).mapNotNull { array.opt(it) }

    /** As duas versões mudaram a mesma linha. */
    private fun resolve(table: String, local: Any, remote: Any): Any {
        if (local !is JSONObject || remote !is JSONObject) return local
        // Tarefa: decisão final (concluída, não feita, reagendada, cancelada) vence estado aberto; o resto, a mais recente.
        val winner = br.com.estudario.domain.SnapshotRowsMerge.winner(table, local, remote, { it.optString("status") }, ::stamp)
        if (table == "studyPlans") {
            // A revisão do plano só anda para a frente: a maior das duas, para o próximo envio valer.
            val revision = maxOf(local.optLong("revision"), remote.optLong("revision"))
            return JSONObject(winner.toString()).put("revision", revision)
        }
        return winner
    }

    private fun stamp(row: JSONObject): Long =
        br.com.estudario.domain.SnapshotRowsMerge.stampFields.firstNotNullOfOrNull { field ->
            row.optLong(field, Long.MIN_VALUE).takeIf { it != Long.MIN_VALUE }
        } ?: Long.MIN_VALUE

    private fun keyOf(table: String, row: Any): String {
        if (row !is JSONObject) return canonical(row)
        if (row.has("id") && !row.isNull("id")) return "id:" + row.get("id").toString()
        compositeKeys[table]?.let { fields -> if (fields.all { row.has(it) }) return fields.joinToString("|") { row.opt(it).toString() } }
        return canonical(row)
    }

    private fun same(a: Any, b: Any): Boolean = canonical(a) == canonical(b)

    /** Texto da linha com as chaves em ordem, para comparar sem depender da ordem dos campos. */
    private fun canonical(value: Any?): String = when (value) {
        is JSONObject -> value.keys().asSequence().sorted().joinToString(",", "{", "}") { "\"$it\":" + canonical(value.opt(it)) }
        is JSONArray -> (0 until value.length()).joinToString(",", "[", "]") { canonical(value.opt(it)) }
        null, JSONObject.NULL -> "null"
        is String -> JSONObject.quote(value)
        is Number -> {
            val d = value.toDouble()
            if (d == Math.floor(d) && !d.isInfinite() && kotlin.math.abs(d) < 9.0e15) d.toLong().toString() else d.toString()
        }
        else -> value.toString()
    }
}
