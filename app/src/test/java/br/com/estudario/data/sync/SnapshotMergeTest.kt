package br.com.estudario.data.sync

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SnapshotMergeTest {
    private fun task(id: String, status: String, updatedAt: Long = 0) =
        JSONObject().put("id", id).put("planId", "p").put("status", status).put("updatedAt", updatedAt)

    private fun snapshot(vararg tasks: JSONObject, executions: List<JSONObject> = emptyList()) =
        JSONObject().put("format", "estudario-backup").put("version", 8)
            .put("planTasks", JSONArray(tasks.toList()))
            .put("studyTaskExecutions", JSONArray(executions))

    private fun JSONObject.task(id: String): JSONObject? {
        val rows = getJSONArray("planTasks")
        return (0 until rows.length()).map { rows.getJSONObject(it) }.firstOrNull { it.getString("id") == id }
    }

    @Test
    fun cadaLadoConcluiUmaTarefaENenhumaSePerde() {
        val base = snapshot(task("a", "PLANEJADA"), task("b", "PLANEJADA"))
        val phone = snapshot(task("a", "CONCLUIDA", 10), task("b", "PLANEJADA"), executions = listOf(JSONObject().put("id", "e1").put("taskId", "a")))
        val site = snapshot(task("a", "PLANEJADA"), task("b", "CONCLUIDA", 20), executions = listOf(JSONObject().put("id", "e2").put("taskId", "b")))

        val merged = SnapshotMerge.merge(base, phone, site)

        assertEquals("CONCLUIDA", merged.task("a")!!.getString("status"))
        assertEquals("CONCLUIDA", merged.task("b")!!.getString("status"))
        assertEquals(2, merged.getJSONArray("studyTaskExecutions").length())
    }

    @Test
    fun concluidaVenceReprogramadaQuandoOsDoisMexeram() {
        val base = snapshot(task("a", "PLANEJADA"))
        val phone = snapshot(task("a", "CONCLUIDA", 10))
        val site = snapshot(task("a", "REPROGRAMADA", 99))
        assertEquals("CONCLUIDA", SnapshotMerge.merge(base, phone, site).task("a")!!.getString("status"))
        assertEquals("CONCLUIDA", SnapshotMerge.merge(base, site, phone).task("a")!!.getString("status"))
    }

    @Test
    fun apagadaDeUmLadoSemMudancaDoOutroSai() {
        val base = snapshot(task("a", "PLANEJADA"), task("b", "PLANEJADA"))
        val phone = snapshot(task("b", "PLANEJADA"))
        val site = snapshot(task("a", "PLANEJADA"), task("b", "PLANEJADA"), task("c", "PLANEJADA"))
        val merged = SnapshotMerge.merge(base, phone, site)
        assertNull(merged.task("a"))
        assertEquals("PLANEJADA", merged.task("c")!!.getString("status"))
    }

    @Test
    fun ordemDosCamposNaoContaComoMudanca() {
        val base = snapshot(task("a", "PLANEJADA", 5))
        val reordered = JSONObject().put("updatedAt", 5).put("status", "PLANEJADA").put("planId", "p").put("id", "a")
        val phone = snapshot(task("a", "EM_ANDAMENTO", 7))
        val site = snapshot(reordered)
        assertEquals("EM_ANDAMENTO", SnapshotMerge.merge(base, phone, site).task("a")!!.getString("status"))
    }

    @Test
    fun semBaseNadaEApagado() {
        val phone = snapshot(task("a", "PLANEJADA"))
        val site = snapshot(task("b", "PLANEJADA"))
        val merged = SnapshotMerge.merge(null, phone, site)
        assertEquals(2, merged.getJSONArray("planTasks").length())
    }

    @Test
    fun revisaoDoPlanoFicaComAMaior() {
        val plan = { rev: Long, at: Long -> JSONObject().put("id", "p").put("revision", rev).put("updatedAt", at) }
        val base = JSONObject().put("studyPlans", JSONArray().put(plan(3, 0)))
        val phone = JSONObject().put("studyPlans", JSONArray().put(plan(5, 10)))
        val site = JSONObject().put("studyPlans", JSONArray().put(plan(4, 20)))
        val merged = SnapshotMerge.merge(base, phone, site).getJSONArray("studyPlans").getJSONObject(0)
        assertEquals(5L, merged.getLong("revision"))
        assertEquals(20L, merged.getLong("updatedAt"))
    }
}
