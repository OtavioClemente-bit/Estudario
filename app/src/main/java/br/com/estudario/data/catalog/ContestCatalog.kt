package br.com.estudario.data.catalog

import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.remote.SupabaseClientConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.Normalizer
import java.util.UUID

/** Um concurso pronto do catálogo, já aprovado. */
data class CatalogContest(
    val id: String,
    val name: String,
    val role: String,
    val board: String?,
    val agency: String?,
    val year: Int,
    val editalUrl: String?,
    val subjectCount: Int,
    val topicCount: Int,
) {
    /** Edital de ano anterior ao atual: pode já existir um mais novo. */
    fun possiblyOutdated(currentYear: Int): Boolean = year < currentYear
}

/** Envio esperando aprovação, com o pacote para conferir antes de aprovar. */
data class PendingContest(val contest: CatalogContest, val submittedAt: String, val subjects: List<String>)

class ContestCatalogException(val code: String) : IllegalStateException(code)

/**
 * Conversa com o catálogo no Supabase. Buscar e baixar são públicos; enviar pede conta; a fila
 * de aprovação só responde para administradores (as regras moram no banco, não aqui).
 */
class ContestCatalogRepository(
    private val config: SupabaseClientConfig,
    private val accessToken: () -> String?,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransport(config.projectUrl),
) {
    suspend fun search(query: String): List<CatalogContest> {
        if (query.trim().length < 2) return emptyList()
        val array = JSONArray(rpc("search_contest_catalog", JSONObject().put("p_query", query.trim())))
        return (0 until array.length()).map { contest(array.getJSONObject(it)) }
    }

    /** O pacote .estudo do concurso, pronto para a tela de revisão de importação. */
    suspend fun estudo(id: String): String {
        val body = request("GET", "/rest/v1/contest_catalog?id=eq.$id&select=estudo", null)
        val rows = JSONArray(body)
        if (rows.length() == 0) throw ContestCatalogException("NOT_FOUND")
        return rows.getJSONObject(0).getJSONObject("estudo").toString()
    }

    suspend fun submit(
        name: String, role: String, board: String?, agency: String?, year: Int, editalUrl: String?,
        estudo: JSONObject, subjectCount: Int, topicCount: Int,
    ): String {
        if (accessToken() == null) throw ContestCatalogException("AUTH_REQUIRED")
        return rpc(
            "submit_contest_catalog",
            JSONObject()
                .put("p_name", name).put("p_role", role).put("p_board", board ?: "").put("p_agency", agency ?: "")
                .put("p_year", year).put("p_edital_url", editalUrl ?: "").put("p_estudo", estudo)
                .put("p_subject_count", subjectCount).put("p_topic_count", topicCount),
        ).trim('"')
    }

    suspend fun isAdmin(): Boolean = accessToken() != null &&
        runCatching { rpc("is_app_admin", JSONObject()).trim() == "true" }.getOrDefault(false)

    suspend fun pending(): List<PendingContest> {
        val array = JSONArray(rpc("list_pending_contest_catalog", JSONObject()))
        return (0 until array.length()).map { index ->
            val row = array.getJSONObject(index)
            PendingContest(contest(row), row.optString("submitted_at"), CatalogEstudo.subjectNames(row.optJSONObject("estudo")))
        }
    }

    suspend fun review(id: String, approve: Boolean, note: String? = null) {
        rpc("review_contest_catalog", JSONObject().put("p_id", id).put("p_approve", approve).put("p_note", note ?: JSONObject.NULL))
    }

    private fun contest(row: JSONObject) = CatalogContest(
        id = row.getString("id"),
        name = row.getString("name"),
        role = row.getString("role"),
        board = row.optString("board").takeIf { it.isNotBlank() && it != "null" },
        agency = row.optString("agency").takeIf { it.isNotBlank() && it != "null" },
        year = row.getInt("year"),
        editalUrl = row.optString("edital_url").takeIf { it.isNotBlank() && it != "null" },
        subjectCount = row.optInt("subject_count"),
        topicCount = row.optInt("topic_count"),
    )

    private suspend fun rpc(name: String, body: JSONObject): String = request("POST", "/rest/v1/rpc/$name", body)

    private suspend fun request(method: String, path: String, body: JSONObject?): String = withContext(Dispatchers.IO) {
        if (!config.isConfigured) throw ContestCatalogException("UNAVAILABLE")
        val headers = buildMap {
            put("apikey", config.publishableKey)
            put("Authorization", "Bearer ${accessToken() ?: config.publishableKey}")
            put("Accept", "application/json")
            if (body != null) put("Content-Type", "application/json")
        }
        val response = runCatching { transport.execute(AiHttpRequest(method, path, headers, body?.toString()?.toByteArray())) }
            .getOrElse { throw ContestCatalogException("NETWORK") }
        if (response.status !in 200..299) {
            val code = runCatching { JSONObject(response.body).optString("message") }.getOrNull()?.takeIf { it.isNotBlank() } ?: "HTTP_${response.status}"
            throw ContestCatalogException(code)
        }
        response.body
    }
}

/**
 * Pacote .estudo só com a estrutura (matérias e tópicos) de um concurso do aparelho, para o
 * catálogo. Os IDs levam um prefixo próprio do envio, para quem baixar ganhar um concurso novo e
 * independente, que pode editar à vontade.
 */
object CatalogEstudo {
    fun build(competition: CompetitionEntity, subjects: List<SubjectEntity>, topics: List<TopicEntity>, title: String): JSONObject {
        val prefix = "cat-${UUID.randomUUID().toString().take(8)}"
        fun topicJson(topic: TopicEntity): JSONObject = JSONObject()
            .put("id", "$prefix-t${topic.id}")
            .put("titulo", topic.title)
            .put("ordem", topic.position)
            .put("prioridade", topic.priority.name)
            .put("contentOriginType", topic.contentOriginType.name)
            .put("subtopicos", JSONArray(topics.filter { it.parentTopicId == topic.id }.sortedBy { it.position }.map(::topicJson)))
        val materias = subjects.filter { it.competitionId == competition.id }.sortedBy { it.position }.map { subject ->
            JSONObject()
                .put("id", "$prefix-m${subject.id}")
                .put("nome", subject.name)
                .put("ordem", subject.position)
                .put("topicos", JSONArray(topics.filter { it.subjectId == subject.id && it.parentTopicId == null }.sortedBy { it.position }.map(::topicJson)))
        }
        return JSONObject()
            .put("version", 2)
            .put("packageId", prefix)
            .put("concurso", JSONObject().put("id", prefix).put("nome", title))
            .put("materias", JSONArray(materias))
            .put("warnings", JSONArray())
    }

    fun subjectNames(estudo: JSONObject?): List<String> {
        val materias = estudo?.optJSONArray("materias") ?: return emptyList()
        return (0 until materias.length()).map { materias.getJSONObject(it).optString("nome") }
    }
}

/**
 * Compara o concurso do catálogo com o PDF que a pessoa anexou, no próprio celular e sem IA:
 * procura cada matéria no texto do edital e confere o ano.
 */
object EditalComparison {
    data class Result(val found: List<String>, val missing: List<String>, val yearInPdf: Boolean) {
        val matches get() = missing.isEmpty() && yearInPdf
    }

    fun compare(subjects: List<String>, year: Int, pdfText: String): Result {
        val text = normalize(pdfText)
        val (found, missing) = subjects.partition { subject -> keyWords(subject).let { words -> words.isNotEmpty() && words.all(text::contains) } }
        return Result(found, missing, text.contains(year.toString()))
    }

    private fun normalize(value: String): String =
        Normalizer.normalize(value, Normalizer.Form.NFD).replace(Regex("\\p{M}+"), "").lowercase().replace(Regex("\\s+"), " ")

    /** As palavras que identificam a matéria, sem "de", "e", "noções"... */
    private fun keyWords(subject: String): List<String> = normalize(subject)
        .split(Regex("[^a-z0-9]+"))
        .filter { it.length > 3 && it !in STOP }

    private val STOP = setOf("nocoes", "conhecimentos", "basicos", "gerais", "especificos", "sobre", "para", "atualidades")
}
