package br.com.estudario.data.sync

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SnapshotMergeTest {
    @Test
    fun latestDifficultAttemptKeepsStagePendingAcrossSync() {
        fun review(completed: Long?, due: Long, updated: Long) = JSONObject()
            .put("id", 1).put("topicId", 10).put("stage", 1).put("dueAt", due)
            .put("completedAt", completed ?: JSONObject.NULL).put("intervalDays", 1).put("updatedAt", updated)
        fun state(row: JSONObject) = JSONObject().put("reviews", JSONArray(listOf(row)))
        val base = state(review(null, 100, 0))
        val latest = state(review(null, 300, 250))
        val older = state(review(200, 100, 200))

        for ((local, remote) in listOf(latest to older, older to latest)) {
            val merged = SnapshotMerge.merge(base, local, remote).getJSONArray("reviews").getJSONObject(0)
            assertTrue(merged.isNull("completedAt"))
            assertEquals(300L, merged.getLong("dueAt"))
            assertEquals(1L, merged.getLong("intervalDays"))
        }
    }

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

    /**
     * Linhas novas dos dois lados com o mesmo id viram uma só. Por isso o site numera o que cria
     * longe dos ids do celular (Snapshot.ID_GAP no app web).
     */
    @Test
    fun materiaCriadaNosDoisLadosComMesmoIdPerdeUma() {
        fun theory(id: Long, text: String) = JSONObject().put("id", id).put("topicId", 1).put("markdown", text).put("updatedAt", 5)
        val base = JSONObject().put("theories", JSONArray())
        val phone = JSONObject().put("theories", JSONArray(listOf(theory(1, "do celular"))))
        val site = JSONObject().put("theories", JSONArray(listOf(theory(1, "do site"))))

        val merged = SnapshotMerge.merge(base, phone, site)

        assertEquals(1, merged.getJSONArray("theories").length())
    }

    /** Visual do Folha trocado no app e no site: fica o trocado por último, nos dois sentidos. */
    @Test
    fun visualDoFolhaFicaOTrocadoPorUltimo() {
        fun look(hat: String, at: Long) = JSONObject().put("hat", hat).put("updatedAt", at)
        val base = JSONObject().put("folhaLook", look("capelo", 1))
        val phone = JSONObject().put("folhaLook", look("coroa", 10))
        val site = JSONObject().put("folhaLook", look("mago", 20))

        assertEquals("mago", SnapshotMerge.merge(base, phone, site).getJSONObject("folhaLook").getString("hat"))
        assertEquals("mago", SnapshotMerge.merge(base, site, phone).getJSONObject("folhaLook").getString("hat"))
        // só um lado tem visual: ele fica
        assertEquals("coroa", SnapshotMerge.merge(null, phone, JSONObject()).getJSONObject("folhaLook").getString("hat"))
    }

    @Test
    fun metaMudadaPorUltimoValeEOQueFoiVistoNosDoisSeSoma() {
        fun prefs(goal: Int, at: Long, vararg seen: String) = JSONObject().put("userPrefs", JSONObject()
            .put("dailyGoal", goal).put("goalHistory", "0:$goal").put("goalUpdatedAt", at)
            .put("wardrobeSeen", JSONArray(seen.toList())).put("celebratedPieces", JSONArray()).put("celebratedBadges", JSONArray(listOf("b1"))))

        val merged = SnapshotMerge.merge(null, prefs(20, 10, "CHAPEU:a"), prefs(30, 99, "OCULOS:b")).getJSONObject("userPrefs")

        assertEquals(30, merged.getInt("dailyGoal"))
        assertEquals(listOf("CHAPEU:a", "OCULOS:b"), (0 until merged.getJSONArray("wardrobeSeen").length()).map { merged.getJSONArray("wardrobeSeen").getString(it) })
        assertEquals(1, merged.getJSONArray("celebratedBadges").length())
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
