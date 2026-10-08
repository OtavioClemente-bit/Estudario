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
    /** Tabelas sem "id": a chave é a combinação destes campos. Outras sem id usam a linha inteira. */
    private val compositeKeys = mapOf(
        "planSubjects" to listOf("planId", "subjectId"),
        "studyAvailability" to listOf("planId", "day"),
        "studyDayOverrides" to listOf("planId", "day"),
        "errorConceptEntries" to listOf("conceptId", "errorEntryId"),
        "questionTags" to listOf("questionId", "tagId"),
    )

    private val taskStatusRank = mapOf(
        "CONCLUIDA" to 7, "NAO_REALIZADA" to 6, "EM_ANDAMENTO" to 5, "PAUSADA" to 4,
        "REPROGRAMADA" to 3, "CANCELADA" to 2, "PLANEJADA" to 1,
    )

    fun merge(base: JSONObject?, local: JSONObject, remote: JSONObject): JSONObject {
        val out = JSONObject()
        val keys = (local.keys().asSequence() + remote.keys().asSequence()).toCollection(LinkedHashSet())
        for (key in keys) {
            val l = local.opt(key)
            val r = remote.opt(key)
            val b = base?.opt(key)
            when {
                l is JSONArray && r is JSONArray -> out.put(key, mergeArray(key, b as? JSONArray, l, r, base == null))
                l == null -> out.put(key, r)
                r == null -> out.put(key, l)
                // Campo simples (formato, versão): fica o que mudou em relação à base; senão, o daqui.
                b != null && same(l, b) -> out.put(key, r)
                else -> out.put(key, l)
            }
        }
        return out
    }

    private fun mergeArray(table: String, base: JSONArray?, local: JSONArray, remote: JSONArray, noBase: Boolean): JSONArray {
        val b = index(table, base)
        val l = index(table, local)
        val r = index(table, remote)
        val order = LinkedHashSet<String>().apply { addAll(r.keys); addAll(l.keys) }
        val out = JSONArray()
        for (key in order) {
            val lv = l[key]
            val rv = r[key]
            val bv = b[key]
            val chosen: Any? = when {
                lv == null && rv == null -> null
                bv == null || noBase -> when {
                    lv != null && rv != null -> if (same(lv, rv)) lv else resolve(table, lv, rv)
                    else -> lv ?: rv
                }
                lv == null -> if (rv == null || same(rv, bv)) null else rv // apagada aqui; lá ninguém mexeu
                rv == null -> if (same(lv, bv)) null else lv // apagada lá; aqui ninguém mexeu
                same(lv, bv) -> rv
                same(rv, bv) -> lv
                same(lv, rv) -> lv
                else -> resolve(table, lv, rv)
            }
            if (chosen != null) out.put(chosen)
        }
        return out
    }

    /** As duas versões mudaram a mesma linha. */
    private fun resolve(table: String, local: Any, remote: Any): Any {
        if (local !is JSONObject || remote !is JSONObject) return local
        if (table == "planTasks") {
            val lr = taskStatusRank[local.optString("status")] ?: 0
            val rr = taskStatusRank[remote.optString("status")] ?: 0
            if (lr != rr) return if (lr > rr) local else remote
        }
        val winner = if (stamp(remote) > stamp(local)) remote else local
        if (table == "studyPlans") {
            // A revisão do plano só anda para a frente: a maior das duas, para o próximo envio valer.
            val revision = maxOf(local.optLong("revision"), remote.optLong("revision"))
            return JSONObject(winner.toString()).put("revision", revision)
        }
        return winner
    }

    private fun stamp(row: JSONObject): Long =
        listOf("updatedAt", "completedAt", "answeredAt", "createdAt").firstNotNullOfOrNull { field ->
            row.optLong(field, Long.MIN_VALUE).takeIf { it != Long.MIN_VALUE }
        } ?: Long.MIN_VALUE

    private fun index(table: String, array: JSONArray?): LinkedHashMap<String, Any> {
        val map = LinkedHashMap<String, Any>()
        if (array == null) return map
        for (i in 0 until array.length()) {
            val row = array.opt(i) ?: continue
            map[keyOf(table, row)] = row
        }
        return map
    }

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
