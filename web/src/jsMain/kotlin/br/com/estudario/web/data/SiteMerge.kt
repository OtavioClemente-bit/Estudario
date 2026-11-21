package br.com.estudario.web.data

import br.com.estudario.domain.SnapshotRowsMerge
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull

/**
 * A junção do app (SnapshotMerge.kt) do lado do site, com as mesmas regras por tabela (domain/SnapshotRowsMerge).
 *
 * Serve para o que o site guardou sem internet e só consegue enviar depois que outro aparelho já publicou.
 * Antes o site perguntava qual versão manter: "manter a minha" apagava o que o celular tinha concluído, e
 * "usar a da conta" jogava fora o que foi feito aqui. Agora as duas se juntam e nada concluído se perde.
 */
object SiteMerge {
    /** Campos de "userPrefs" e o horário que diz qual lado mudou por último. */
    private val stampOf = mapOf(
        "dailyGoal" to "goalUpdatedAt", "goalHistory" to "goalUpdatedAt",
        "paperPattern" to "paperUpdatedAt", "paperLight" to "paperUpdatedAt",
        "userName" to "userNameUpdatedAt",
    )

    fun merge(base: JsonObject?, local: JsonObject, remote: JsonObject): JsonObject {
        val keys = LinkedHashSet<String>().apply { addAll(local.keys); addAll(remote.keys) }
        val out = LinkedHashMap<String, JsonElement>()
        for (key in keys) {
            val l = local[key]
            val r = remote[key]
            val b = base?.get(key)
            out[key] = when {
                l is JsonArray && r is JsonArray -> JsonArray(
                    SnapshotRowsMerge.rows(b as? JsonArray, l, r, base == null, { keyOf(key, it) }, ::canonical) { x, y -> resolve(key, x, y) },
                )
                l == null -> r!!
                r == null -> l
                b != null && canonical(l) == canonical(b) -> r
                b != null && canonical(r) == canonical(b) -> l
                l is JsonObject && r is JsonObject -> mergeObject(b as? JsonObject, l, r)
                else -> l
            }
        }
        return JsonObject(out)
    }

    /** Objeto solto (userPrefs, folhaLook): campo a campo; no conflito, o lado que mudou aquele campo por último. */
    private fun mergeObject(base: JsonObject?, local: JsonObject, remote: JsonObject): JsonObject {
        val keys = LinkedHashSet<String>().apply { addAll(remote.keys); addAll(local.keys) }
        val out = LinkedHashMap<String, JsonElement>()
        fun stamp(o: JsonObject, field: String): Long {
            val named = stampOf[field]?.let { (o[it] as? JsonPrimitive)?.longOrNull }
            return named ?: o.entries.filter { it.key.endsWith("At") }.maxOfOrNull { (it.value as? JsonPrimitive)?.longOrNull ?: 0L } ?: 0L
        }
        for (k in keys) {
            val l = local[k]
            val r = remote[k]
            val b = base?.get(k)
            out[k] = when {
                l == null -> r!!
                r == null -> l
                b != null && canonical(l) == canonical(b) -> r
                b != null && canonical(r) == canonical(b) -> l
                canonical(l) == canonical(r) -> l
                // Listas do que já foi visto ou comemorado: só crescem.
                l is JsonArray && r is JsonArray -> JsonArray((l + r).distinctBy(::canonical))
                // O próprio horário: o maior.
                k.endsWith("At") && l is JsonPrimitive && r is JsonPrimitive -> if ((r.longOrNull ?: 0L) > (l.longOrNull ?: 0L)) r else l
                else -> if (stamp(remote, k) > stamp(local, k)) r else l
            }
        }
        return JsonObject(out)
    }

    private fun resolve(table: String, local: JsonElement, remote: JsonElement): JsonElement {
        if (local !is JsonObject || remote !is JsonObject) return local
        val winner = SnapshotRowsMerge.winner(table, local, remote, { (it["status"] as? JsonPrimitive)?.contentOrNull }, ::stamp)
        if (table == "studyPlans") {
            val revision = maxOf((local["revision"] as? JsonPrimitive)?.longOrNull ?: 0L, (remote["revision"] as? JsonPrimitive)?.longOrNull ?: 0L)
            return JsonObject(winner + ("revision" to JsonPrimitive(revision)))
        }
        return winner
    }

    private fun stamp(row: JsonObject): Long =
        SnapshotRowsMerge.stampFields.firstNotNullOfOrNull { (row[it] as? JsonPrimitive)?.longOrNull } ?: Long.MIN_VALUE

    private fun keyOf(table: String, row: JsonElement): String {
        if (row !is JsonObject) return canonical(row)
        row["id"]?.takeIf { it !is JsonNull }?.let { return "id:" + ((it as? JsonPrimitive)?.contentOrNull ?: it.toString()) }
        SnapshotRowsMerge.compositeKeys[table]?.let { fields -> if (fields.all { it in row }) return fields.joinToString("|") { f -> (row[f] as? JsonPrimitive)?.contentOrNull ?: row[f].toString() } }
        return canonical(row)
    }

    /** Texto da linha com as chaves em ordem (e 5.0 igual a 5), para comparar sem depender da ordem dos campos. */
    internal fun canonical(value: JsonElement?): String = when (value) {
        null, JsonNull -> "null"
        is JsonObject -> value.keys.sorted().joinToString(",", "{", "}") { "\"$it\":" + canonical(value[it]) }
        is JsonArray -> value.joinToString(",", "[", "]") { canonical(it) }
        is JsonPrimitive -> if (value.isString) "\"" + value.content + "\"" else value.content.toDoubleOrNull()?.let { d ->
            if (d == kotlin.math.floor(d) && kotlin.math.abs(d) < 9.0e15) d.toLong().toString() else d.toString()
        } ?: value.content
    }
}
