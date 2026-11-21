package br.com.estudario.web.data

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Guarda de lançamento (scripts/guardas.sh), lado do site: o celular publica enquanto o site estudou sem
 * internet (a cópia guardada, que não está na fila de edições). Ao voltar a rede, o site junta as duas
 * (SiteMerge, sem base) e também a foto lida de novo (Snapshot.parse, que cancela a cópia velha).
 *
 * Confere: nenhuma conclusão perdida; nada concluído volta a planejado; nenhum dia de estudo (questões
 * respondidas, tempo registrado, sessões de foco) some ou dobra; a meta diária fica com a mudada por último;
 * as listas do que já foi comemorado só crescem; a cópia de tarefa reorganizada de um lado e concluída do
 * outro não fica viva.
 */
class TwoDeviceSyncGuardTest {
    private fun obj(vararg f: Pair<String, Any?>) = jsonOf(*f)
    private fun rows(o: JsonObject, key: String) = (o[key] as? JsonArray)?.map { it as JsonObject }.orEmpty()
    private fun JsonObject.s(k: String) = (this[k] as? JsonPrimitive)?.contentOrNull
    private fun JsonObject.l(k: String) = (this[k] as? JsonPrimitive)?.longOrNull
    private fun JsonObject.put(key: String, rows: List<JsonObject>) = JsonObject(this + (key to JsonArray(rows)))

    private fun task(id: String, day: Long, status: String = "PLANEJADA", from: String? = null, at: Long = 0) =
        obj("id" to id, "planId" to "p", "day" to day, "type" to "THEORY", "topicId" to day, "status" to status, "replannedFrom" to from, "updatedAt" to at)

    private fun initial() = obj(
        "format" to "estudario-backup", "version" to 8,
        "planTasks" to JsonArray((0 until 20).map { task("t$it", 100L + it) }),
        "studyTaskExecutions" to JsonArray(emptyList()), "attempts" to JsonArray(emptyList()), "focusSessions" to JsonArray(emptyList()),
        "userPrefs" to obj("dailyGoal" to 20, "goalUpdatedAt" to 0, "celebratedBadges" to JsonArray(emptyList())),
    )

    private class Truth {
        val done = hashSetOf<String>(); val exec = hashSetOf<String>(); val attempts = hashSetOf<Long>(); val focus = hashSetOf<Long>()
        val badges = hashSetOf<String>(); var goal = 20; var goalAt = 0L
    }

    private fun edit(rnd: Random, side: String, s: JsonObject, clock: Long, t: Truth, id: () -> Long): JsonObject {
        val tasks = rows(s, "planTasks")
        val open = tasks.filter { it.s("status") == "PLANEJADA" }
        return when (rnd.nextInt(5)) {
            0, 1 -> open.randomOrNull(rnd)?.let { task ->
                val e = "e-$side-${id()}"; val a = id()
                t.done += task.s("id")!!; t.exec += e; t.attempts += a
                s.put("planTasks", tasks.map { if (it.s("id") == task.s("id")) it.with("status" to JsonPrimitive("CONCLUIDA"), "updatedAt" to JsonPrimitive(clock)) else it })
                    .put("studyTaskExecutions", rows(s, "studyTaskExecutions") + obj("id" to e, "taskId" to task.s("id"), "completedAt" to clock))
                    .put("attempts", rows(s, "attempts") + obj("id" to a, "questionId" to a, "correct" to true, "answeredAt" to clock))
            } ?: s
            2 -> open.randomOrNull(rnd)?.let { task ->
                val copy = task("c-$side-${id()}", task.l("day")!! + 1, from = task.s("id"), at = clock)
                s.put("planTasks", tasks.map { if (it.s("id") == task.s("id")) it.with("status" to JsonPrimitive("REPROGRAMADA"), "updatedAt" to JsonPrimitive(clock)) else it } + copy)
            } ?: s
            3 -> {
                val f = id(); t.focus += f
                val badge = "b$side${rnd.nextInt(3)}"; t.badges += badge
                val prefs = s["userPrefs"] as JsonObject
                val seen = ((prefs["celebratedBadges"] as? JsonArray).orEmpty() + JsonPrimitive(badge)).distinct()
                s.put("focusSessions", rows(s, "focusSessions") + obj("id" to f, "startedAt" to clock - 1500, "completedAt" to clock))
                    .let { JsonObject(it + ("userPrefs" to prefs.with("celebratedBadges" to JsonArray(seen)))) }
            }
            else -> {
                val goal = 10 + rnd.nextInt(50)
                if (clock > t.goalAt) { t.goal = goal; t.goalAt = clock }
                val prefs = s["userPrefs"] as JsonObject
                JsonObject(s + ("userPrefs" to prefs.with("dailyGoal" to JsonPrimitive(goal), "goalUpdatedAt" to JsonPrimitive(clock))))
            }
        }
    }

    @Test
    fun siteComCopiaSemInternetJuntaComOQueOCelularPublicou() {
        repeat(200) { seed ->
            val rnd = Random(seed)
            val truth = Truth()
            var phone = initial()
            var site = initial()
            var clock = 1_000L
            var next = 1L
            repeat(15) {
                repeat(rnd.nextInt(3)) { phone = edit(rnd, "cel", phone, clock++, truth) { next++ } }
                repeat(rnd.nextInt(3)) { site = edit(rnd, "site", site, clock++, truth) { next++ } }
            }
            // Ordem 1: o site junta sobre o que o celular publicou. Ordem 2: o celular é que chega por último.
            for ((label, merged) in listOf("site por último" to SiteMerge.merge(null, site, phone), "celular por último" to SiteMerge.merge(null, phone, site))) {
                val parsed = Snapshot.parse(snapshotJson.encodeToString(JsonObject.serializer(), merged)).root
                val tasks = rows(parsed, "planTasks").associateBy { it.s("id")!! }
                val where = "semente $seed, $label"
                truth.done.forEach { assertEquals("CONCLUIDA", tasks[it]?.s("status"), "$where: conclusão perdida de $it") }
                // Cópia viva de tarefa concluída do outro lado: tem de sair (cancelada).
                tasks.values.filter { it.s("status") == "PLANEJADA" && it["replannedFrom"] !is JsonNull && it["replannedFrom"] != null }.forEach { c ->
                    assertTrue(c.s("replannedFrom") !in truth.done, "$where: cópia ${c.s("id")} de tarefa concluída continua viva")
                }
                assertEquals(truth.exec, rows(parsed, "studyTaskExecutions").map { it.s("id")!! }.toSet(), "$where: tempo registrado perdido")
                assertEquals(truth.attempts.size, rows(parsed, "attempts").size, "$where: questão respondida perdida ou dobrada")
                assertEquals(truth.focus, rows(parsed, "focusSessions").map { it.l("id")!! }.toSet(), "$where: sessão de foco perdida")
                val prefs = parsed["userPrefs"] as JsonObject
                assertEquals(truth.goal.toLong(), prefs.l("dailyGoal"), "$where: meta diária não é a mudada por último")
                assertEquals(truth.badges, (prefs["celebratedBadges"] as JsonArray).map { (it as JsonPrimitive).content }.toSet(), "$where: comemoração vista some")
            }
        }
    }

    /** Com base, o que foi apagado de um lado e não mexido do outro sai; o criado entra. */
    @Test
    fun comBaseApagadoDeUmLadoSai() {
        val base = initial()
        val phone = base.put("planTasks", rows(base, "planTasks").drop(1))
        val site = base.put("planTasks", rows(base, "planTasks") + task("novo", 300))
        val merged = SiteMerge.merge(base, site, phone)
        val ids = rows(merged, "planTasks").map { it.s("id") }
        assertTrue("t0" !in ids)
        assertTrue("novo" in ids)
    }

    private fun JsonArray?.orEmpty(): List<JsonElement> = this ?: emptyList()
}
