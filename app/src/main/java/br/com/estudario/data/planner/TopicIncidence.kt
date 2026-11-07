package br.com.estudario.data.planner

import android.content.Context
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiContentRequest
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

/**
 * Quanto cada tópico do edital cai nas provas de verdade (questões oficiais na biblioteca), na banca do
 * concurso; sem banca ou sem questão dela, de todas as bancas. O servidor conta (library-incidence); aqui
 * fica guardado por tópico e o montador do plano usa (PlannerSnapshotBuilder.Topic.incidence): o que mais
 * cai ganha mais treino e o que quase não cai vai para o fim de cada matéria. Atualiza uma vez por dia.
 */
object TopicIncidence {
    private const val PREFS = "topic_incidence"
    private const val KEY = "counts"
    private const val AT = "fetched_at"
    private const val PATH = "/functions/v1/library-incidence"
    private const val DAY_MS = 86_400_000L

    private const val HOT = "hot"

    @Volatile private var cache: Map<Long, Int>? = null
    @Volatile private var hotCache: Set<Long>? = null

    /** Os tópicos que mais caem dentro de cada matéria (a mesma régua do plano): ganham a etiqueta "Cai muito". */
    fun hot(context: Context): Set<Long> = hotCache ?: run {
        val ids = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(HOT, "").orEmpty().split(",").mapNotNullTo(hashSetOf()) { it.toLongOrNull() }
        hotCache = ids
        ids
    }

    /** Questões de provas por tópico do aparelho (só os que têm matéria na biblioteca). */
    fun counts(context: Context): Map<Long, Int> = cache ?: run {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, null)
        val parsed = runCatching {
            val o = JSONObject(raw ?: "{}")
            o.keys().asSequence().mapNotNull { k -> k.toLongOrNull()?.let { it to o.getInt(k) } }.toMap()
        }.getOrDefault(emptyMap())
        cache = parsed
        parsed
    }

    /**
     * Uma régua só por matéria: a banca do concurso quando ela tem questões em 3+ tópicos da matéria; senão,
     * todas as bancas para todos os tópicos dela. Misturar as duas réguas na mesma matéria punha um tópico
     * com 80 questões no geral acima de um com 12 da banca (e o da banca ia para o fim).
     */
    internal fun chooseBasis(raw: Map<Long, Pair<Int, Int>>, subjectOf: Map<Long, Long>): Map<Long, Int> =
        raw.entries.groupBy { subjectOf[it.key] }.flatMap { (_, list) ->
            val byBoard = list.count { it.value.first > 0 } >= 3
            list.map { (id, v) -> id to if (byBoard) v.first else v.second }
        }.toMap()

    suspend fun refresh(app: EstudarioApplication, force: Boolean = false) {
        val prefs = app.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (!force && System.currentTimeMillis() - prefs.getLong(AT, 0) < DAY_MS) return
        val token = app.supabaseAuthRepository.accessToken() ?: return
        val config = app.supabaseClientConfig
        if (!config.isConfigured) return
        val dao = app.database.dao()
        val topics = dao.topicsOnce()
        val parents = topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val leaves = topics.filter { it.id !in parents }
        if (leaves.isEmpty()) return
        val subjects = dao.subjectsOnce().associateBy { it.id }
        val boardOf = app.database.simulationDao().examProfilesOnce().associate { it.competitionId to it.board }
        val transport = UrlConnectionAiHttpTransport(config.projectUrl)
        val raw = HashMap<Long, Pair<Int, Int>>() // tópico -> (banca, todas)
        try {
            // Um pedido por banca (cada concurso pode ter a sua).
            leaves.groupBy { t -> subjects[t.subjectId]?.competitionId?.let(boardOf::get).orEmpty() }.forEach { (board, list) ->
                for (chunk in list.chunked(400)) {
                    val items = JSONArray()
                    chunk.forEach { topic ->
                        items.put(JSONObject()
                            .put("id", topic.id.toString())
                            .put("subjectName", subjects[topic.subjectId]?.name.orEmpty())
                            .put("topicPath", JSONArray(AiContentRequest.pathOf(topic, topics))))
                    }
                    val body = JSONObject().put("items", items).apply { if (board.isNotBlank()) put("board", board) }
                    val response = withTimeout(20_000) {
                        transport.execute(AiHttpRequest(
                            method = "POST",
                            path = PATH,
                            headers = mapOf("Authorization" to "Bearer $token", "apikey" to config.publishableKey, "Accept" to "application/json", "Content-Type" to "application/json; charset=utf-8"),
                            body = body.toString().toByteArray(Charsets.UTF_8),
                        ))
                    }
                    if (response.status != 200) return
                    val rows = JSONObject(response.body).optJSONArray("incidence") ?: JSONArray()
                    for (i in 0 until rows.length()) {
                        val row = rows.optJSONObject(i) ?: continue
                        val id = row.optString("id").toLongOrNull() ?: continue
                        raw[id] = row.optInt("board") to row.optInt("all")
                    }
                }
            }
        } catch (error: CancellationException) {
            if (error !is kotlinx.coroutines.TimeoutCancellationException) throw error
            return
        } catch (_: Throwable) {
            return
        }
        val result = chooseBasis(raw, leaves.associate { it.id to it.subjectId })
        val json = JSONObject().apply { result.forEach { (k, v) -> put(k.toString(), v) } }
        val hot = br.com.estudario.domain.planner.StudyPlanBlueprint.incidenceLevels(
            leaves.groupBy { it.subjectId }.values.map { list -> list.mapNotNull { topic -> result[topic.id]?.let { topic.id to it } } },
        ).first
        prefs.edit().putString(KEY, json.toString()).putString(HOT, hot.joinToString(",")).putLong(AT, System.currentTimeMillis()).apply()
        cache = result
        hotCache = hot
    }
}
