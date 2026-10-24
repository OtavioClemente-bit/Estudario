package br.com.estudario.data.planner

import android.content.Context
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiContentRequest
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.ui.plans.AiPlanLoadResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

/**
 * Tópicos que ficam fora do plano do Grátis: os que ainda não têm matéria pronta na biblioteca (o
 * Grátis não usa IA). O servidor responde quais estão prontos (library-ready); os outros ficam
 * guardados aqui e o montador do plano deixa de fora (PlannerSnapshotBuilder.excludedTopicIds).
 * Quando a matéria é publicada, a próxima consulta tira o tópico daqui e ele entra no replanejamento.
 * Nos planos pagos a lista fica vazia: lá a IA escreve na hora o que falta.
 */
object LibraryGaps {
    private const val PREFS = "biblioteca_lacunas"
    private const val KEY = "fora_do_plano"
    private const val FREE = "gratis"
    private const val PATH = "/functions/v1/library-ready"

    fun excluded(context: Context): Set<Long> =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getStringSet(KEY, emptySet()).orEmpty().mapNotNullTo(hashSetOf()) { it.toLongOrNull() }

    private fun save(context: Context, ids: Set<Long>) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putStringSet(KEY, ids.mapTo(hashSetOf()) { it.toString() }).apply()
    }

    /** A conta estava no Grátis na última consulta: o plano segue as regras dele (PlannerSnapshotBuilder.Input.freeRules). */
    fun free(context: Context): Boolean = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(FREE, false)

    /** Pergunta ao servidor o que está pronto. Sem conta ou sem resposta, fica a lista de antes. */
    suspend fun refresh(app: EstudarioApplication): Set<Long> {
        val summary = (app.aiPlanRepository.load() as? AiPlanLoadResult.Available)?.summary ?: return excluded(app)
        app.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(FREE, summary.planTier == "FREE").apply()
        if (summary.planTier != "FREE") { save(app, emptySet()); return emptySet() }
        val token = app.supabaseAuthRepository.accessToken() ?: return excluded(app)
        val config = app.supabaseClientConfig
        if (!config.isConfigured) return excluded(app)
        val dao = app.database.dao()
        val topics = dao.topicsOnce()
        val subjects = dao.subjectsOnce().associateBy { it.id }
        // Já tem matéria no aparelho, já foi estudado ou é grupo de subtópicos: não precisa perguntar.
        val withTheory = dao.theoriesOnce().mapTo(hashSetOf()) { it.topicId }
        val parents = topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val candidates = topics.filter { it.id !in withTheory && it.id !in parents && it.status == TopicStatus.NAO_ESTUDADO }
        if (candidates.isEmpty()) { save(app, emptySet()); return emptySet() }
        val transport = UrlConnectionAiHttpTransport(config.projectUrl)
        val ready = hashSetOf<Long>()
        try {
            for (chunk in candidates.chunked(400)) {
                val items = JSONArray()
                chunk.forEach { topic ->
                    items.put(JSONObject()
                        .put("id", topic.id.toString())
                        .put("subjectName", subjects[topic.subjectId]?.name.orEmpty())
                        .put("topicPath", JSONArray(AiContentRequest.pathOf(topic, topics))))
                }
                val response = withTimeout(20_000) {
                    transport.execute(AiHttpRequest(
                        method = "POST",
                        path = PATH,
                        headers = mapOf("Authorization" to "Bearer $token", "apikey" to config.publishableKey, "Accept" to "application/json", "Content-Type" to "application/json; charset=utf-8"),
                        body = JSONObject().put("items", items).toString().toByteArray(Charsets.UTF_8),
                    ))
                }
                if (response.status != 200) return excluded(app)
                val list = JSONObject(response.body).optJSONArray("ready") ?: JSONArray()
                for (i in 0 until list.length()) list.optString(i).toLongOrNull()?.let(ready::add)
            }
        } catch (error: CancellationException) {
            if (error !is kotlinx.coroutines.TimeoutCancellationException) throw error
            return excluded(app)
        } catch (_: Throwable) {
            return excluded(app)
        }
        val gaps = candidates.map { it.id }.filterTo(hashSetOf()) { it !in ready }
        save(app, gaps)
        return gaps
    }
}
