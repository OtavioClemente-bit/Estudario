package br.com.estudario.data.sync

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

/**
 * Guarda de lançamento (scripts/guardas.sh) da sincronização: celular e site estudam sem rede, cada um com
 * a sua cópia, e sincronizam em ordens diferentes, inclusive com um envio velho que chega por último.
 *
 * O servidor é o da conta (sync_commit): só aceita o envio feito sobre a revisão atual; quem chega atrasado
 * baixa a versão da conta e junta (SnapshotMerge, com a base = a última versão que o aparelho sincronizou).
 *
 * Depois de todos sincronizarem, confere: nenhuma conclusão perdida; nada concluído ou reprogramado volta a
 * ficar planejado; uma cópia viva só por tarefa reprogramada e dia; nenhum dia de estudo (execuções, questões
 * respondidas, sessões de foco) some; a meta diária fica com a mudada por último; os aparelhos terminam iguais.
 */
class TwoDeviceSyncGuardTest {
    private class Server { var revision = 0L; var snapshot = JSONObject() }

    private class Device(val name: String, var local: JSONObject, var base: JSONObject?, var knownRevision: Long)

    /** O que cada aparelho fez, para conferir no fim. */
    private class Truth {
        val completed = hashSetOf<String>()
        val rescheduled = hashSetOf<String>()
        val executions = hashSetOf<String>()
        val attempts = hashSetOf<Long>()
        val focus = hashSetOf<Long>()
        var goal = 20; var goalAt = 0L
    }

    private fun copy(o: JSONObject) = JSONObject(o.toString())

    private fun rows(o: JSONObject, key: String): List<JSONObject> {
        val a = o.optJSONArray(key) ?: return emptyList()
        return (0 until a.length()).map { a.getJSONObject(it) }
    }

    private fun append(o: JSONObject, key: String, row: JSONObject) {
        if (!o.has(key)) o.put(key, JSONArray())
        o.getJSONArray(key).put(row)
    }

    private fun task(id: String, day: Long, type: String, topic: Long, status: String = "PLANEJADA", from: String? = null, at: Long = 0) =
        JSONObject().put("id", id).put("planId", "p").put("day", day).put("type", type).put("topicId", topic).put("status", status)
            .put("replannedFrom", from ?: JSONObject.NULL).put("updatedAt", at)

    private fun initial(): JSONObject {
        val o = JSONObject().put("format", "estudario-backup").put("version", 8)
        o.put("planTasks", JSONArray((0 until 30).map { i -> task("t$i", 100L + i / 3, if (i % 2 == 0) "THEORY" else "QUESTIONS", 1000L + i / 2) }))
        o.put("studyPlans", JSONArray().put(JSONObject().put("id", "p").put("revision", 1).put("updatedAt", 0)))
        o.put("userPrefs", JSONObject().put("dailyGoal", 20).put("goalUpdatedAt", 0))
        o.put("studyTaskExecutions", JSONArray()); o.put("attempts", JSONArray()); o.put("focusSessions", JSONArray())
        return o
    }

    /** sync_commit: aceita só sobre a revisão atual; senão o aparelho baixa, junta e envia de novo. */
    private fun sync(server: Server, d: Device) {
        val outgoing = if (d.knownRevision == server.revision) d.local else SnapshotMerge.merge(d.base, d.local, copy(server.snapshot))
        server.snapshot = copy(outgoing)
        server.revision++
        d.local = copy(outgoing)
        d.base = copy(outgoing)
        d.knownRevision = server.revision
    }

    private fun edit(rnd: Random, d: Device, clock: Long, truth: Truth, ids: () -> Long) {
        val tasks = rows(d.local, "planTasks")
        val open = tasks.filter { it.getString("status") == "PLANEJADA" }
        when (rnd.nextInt(5)) {
            0, 1 -> open.randomOrNull(rnd)?.let { t ->
                // Concluir: a tarefa, o tempo registrado e as questões respondidas.
                t.put("status", "CONCLUIDA").put("updatedAt", clock)
                val e = "e-${d.name}-${ids()}"
                append(d.local, "studyTaskExecutions", JSONObject().put("id", e).put("taskId", t.getString("id")).put("completedAt", clock))
                val a = ids()
                append(d.local, "attempts", JSONObject().put("id", a).put("questionId", a).put("correct", true).put("answeredAt", clock))
                truth.completed += t.getString("id"); truth.executions += e; truth.attempts += a
            }
            2 -> open.randomOrNull(rnd)?.let { t ->
                // Reorganizar: a tarefa vira reprogramada e nasce a cópia num dia adiante (id próprio deste aparelho).
                t.put("status", "REPROGRAMADA").put("updatedAt", clock)
                val copyId = "c-${d.name}-${ids()}"
                append(d.local, "planTasks", task(copyId, t.getLong("day") + 1 + rnd.nextInt(2), t.getString("type"), t.getLong("topicId"), from = t.getString("id"), at = clock))
                truth.rescheduled += t.getString("id")
            }
            3 -> {
                val f = ids()
                append(d.local, "focusSessions", JSONObject().put("id", f).put("startedAt", clock - 1500).put("completedAt", clock).put("durationSeconds", 1500))
                truth.focus += f
            }
            else -> {
                val goal = 10 + rnd.nextInt(50)
                d.local.put("userPrefs", JSONObject().put("dailyGoal", goal).put("goalUpdatedAt", clock).put("goalHistory", ""))
                if (clock > truth.goalAt) { truth.goal = goal; truth.goalAt = clock }
            }
        }
    }

    private fun check(label: String, server: Server, devices: List<Device>, truth: Truth) {
        val final = server.snapshot
        val tasks = rows(final, "planTasks").associateBy { it.getString("id") }
        truth.completed.forEach { id -> assertEquals("$label: conclusão perdida de $id", "CONCLUIDA", tasks[id]?.getString("status")) }
        truth.rescheduled.forEach { id -> assertTrue("$label: $id reprogramada voltou para ${tasks[id]?.getString("status")}", tasks[id]?.getString("status") in setOf("REPROGRAMADA", "CONCLUIDA")) }
        // Nada concluído ou reprogramado volta a planejado: o que é planejado no fim nunca foi tocado ou é cópia.
        tasks.values.filter { it.getString("status") == "PLANEJADA" }.forEach { t ->
            assertTrue("$label: ${t.getString("id")} ressuscitou", t.getString("id") !in truth.completed && t.getString("id") !in truth.rescheduled)
        }
        // Cópia viva: no máximo uma por tarefa de origem, dia e tipo.
        tasks.values.filter { it.getString("status") == "PLANEJADA" && !it.isNull("replannedFrom") }
            .groupBy { "${it.getString("replannedFrom")}|${it.getLong("day")}|${it.getString("type")}" }
            .forEach { (k, l) -> assertEquals("$label: cópias duplicadas de $k: ${l.map { it.getString("id") }}", 1, l.size) }
        assertEquals("$label: execução perdida", truth.executions, rows(final, "studyTaskExecutions").map { it.getString("id") }.toSet())
        assertEquals("$label: questão respondida perdida ou dobrada", truth.attempts.size, rows(final, "attempts").size)
        assertEquals("$label: questão respondida perdida", truth.attempts, rows(final, "attempts").map { it.getLong("id") }.toSet())
        assertEquals("$label: sessão de foco perdida (dia de estudo some da sequência)", truth.focus, rows(final, "focusSessions").map { it.getLong("id") }.toSet())
        assertEquals("$label: meta diária não é a mudada por último", truth.goal, final.getJSONObject("userPrefs").getInt("dailyGoal"))
        devices.forEach { d ->
            val mine = rows(d.local, "planTasks").associate { it.getString("id") to it.getString("status") }
            assertEquals("$label: ${d.name} terminou diferente da conta", tasks.mapValues { it.value.getString("status") }, mine)
        }
    }

    @Test
    fun doisAparelhosOfflineSincronizandoEmOrdensDiferentes() {
        repeat(300) { seed ->
            val rnd = Random(seed)
            val server = Server().apply { snapshot = initial(); revision = 1 }
            val devices = listOf(Device("cel", copy(server.snapshot), copy(server.snapshot), 1), Device("site", copy(server.snapshot), copy(server.snapshot), 1))
            val truth = Truth()
            var clock = 1_000L
            var next = 1L
            // Envio velho: uma cópia do celular guardada antes de uma rodada, que chega depois de tudo.
            var stale: Device? = null
            repeat(12) { round ->
                devices.forEach { d -> repeat(rnd.nextInt(4)) { edit(rnd, d, clock++, truth) { next++ } } }
                if (round == 3) devices[0].let { stale = Device("cel-velho", copy(it.local), it.base?.let(::copy), it.knownRevision) }
                devices.shuffled(rnd).filter { rnd.nextDouble() < 0.7 }.forEach { sync(server, it) }
            }
            // O envio velho chega por último (a conta já andou): ele junta sobre a versão atual.
            stale?.let { sync(server, it) }
            // Todos sincronizam até ficar em dia.
            repeat(2) { devices.forEach { sync(server, it) } }
            check("semente $seed", server, devices, truth)
        }
    }

    /** O aparelho que nunca sincronizou (sem base) e traz um plano velho não apaga nem desfaz nada da conta. */
    @Test
    fun aparelhoSemBaseNaoDesfazOQueAContaTem() {
        val server = Server().apply { snapshot = initial(); revision = 5 }
        val old = copy(server.snapshot)
        val first = rows(server.snapshot, "planTasks").first()
        first.put("status", "CONCLUIDA").put("updatedAt", 50)
        append(server.snapshot, "studyTaskExecutions", JSONObject().put("id", "e1").put("taskId", first.getString("id")))
        val device = Device("novo", old, null, 0)
        sync(server, device)
        assertEquals("CONCLUIDA", rows(server.snapshot, "planTasks").first { it.getString("id") == first.getString("id") }.getString("status"))
        assertEquals(1, rows(server.snapshot, "studyTaskExecutions").size)
    }
}
