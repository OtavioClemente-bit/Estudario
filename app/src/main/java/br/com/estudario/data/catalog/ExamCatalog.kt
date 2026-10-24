package br.com.estudario.data.catalog

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.Normalizer
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * Um edital pronto do catálogo (tabela `exam_catalog`), conferido e publicado a partir de
 * `conteudo/editais`. Os tópicos têm o mesmo texto dos apelidos da biblioteca, então o material de
 * cada um sai pronto do banco, sem IA.
 */
@Serializable
data class ExamCatalogEntry(
    val id: String,
    @SerialName("short_name") val shortName: String,
    val agency: String,
    val role: String,
    val board: String? = null,
    val year: Int? = null,
    @SerialName("edital_ref") val editalRef: String? = null,
    @SerialName("source_url") val sourceUrl: String? = null,
    val notice: String? = null,
    @SerialName("search_norm") val searchNorm: String = "",
    @SerialName("subject_count") val subjectCount: Int = 0,
    @SerialName("topic_count") val topicCount: Int = 0,
    @SerialName("ready_topic_count") val readyTopicCount: Int = 0,
) {
    /** "PMMG · Soldado (CFSd QPPM)". */
    val title: String get() = "$shortName · $role"

    /** "Polícia Militar de Minas Gerais · IDECAN · 2025". */
    val details: String get() = listOfNotNull(agency.takeIf { !it.equals(shortName, true) }, board, year?.toString()).joinToString(" · ")

    /** Parte do edital que já tem matéria pronta, de 0 a 100. */
    val readyPercent: Int get() = if (topicCount == 0) 0 else (readyTopicCount * 100 / topicCount).coerceIn(0, 100)
}

@Serializable
/** [questions]: questões da matéria na prova real (quadro do edital); null quando o edital não informa. */
data class ExamCatalogSubject(
    val name: String,
    val topics: List<String>,
    val questions: Int? = null,
    /** Peso de cada questão da matéria na nota (1 quando o edital não diferencia). */
    val weight: Double? = null,
    /** Quando o edital só dá o total do bloco: a frase que explica a divisão. */
    val blockNote: String? = null,
) {
    val share: br.com.estudario.domain.ExamWeights.Share? get() = questions?.let { br.com.estudario.domain.ExamWeights.Share(it, weight ?: 1.0, blockNote) }
}

@Serializable
private data class ExamCatalogSubjectsRow(val subjects: List<ExamCatalogSubject>)

/**
 * Prova oficial pronta de um edital (tabela `exam_simulation`): a prova inteira, na ordem e na
 * proporção da prova real, com gabarito e explicação. Vira o primeiro simulado do concurso, sem IA.
 */
@Serializable
data class OfficialSimulation(
    val id: String,
    @SerialName("catalog_id") val catalogId: String,
    val board: String? = null,
    /** CERTO_ERRADO, MULTIPLA_ESCOLHA ou MISTO. */
    val format: String,
    val structure: String? = null,
    @SerialName("item_count") val itemCount: Int,
    val questions: List<OfficialQuestion>,
)

@Serializable
data class OfficialQuestion(
    val n: Int,
    val subject: String,
    val topic: String? = null,
    val format: String,
    val difficulty: String? = null,
    val statement: String,
    val options: List<OfficialOption>,
    val explanation: String,
    val trap: String? = null,
)

@Serializable
data class OfficialOption(val key: String, val text: String, val correct: Boolean)

/**
 * Busca no catálogo como as pessoas digitam: "pm mg", "policia federal", "agente pf", "trt
 * analista". Cada palavra digitada precisa aparecer (no começo de alguma palavra) na sigla, no
 * órgão, no cargo, na banca ou nos sinônimos do edital. Siglas comuns são expandidas.
 */
object ExamCatalogSearch {
    private val expansions = mapOf(
        "pm" to "policia militar",
        "pf" to "policia federal",
        "prf" to "policia rodoviaria federal",
        "pc" to "policia civil",
        "cbm" to "corpo de bombeiros militar",
        "bm" to "bombeiro militar",
        "mg" to "minas gerais",
        "trt" to "tribunal regional do trabalho",
        "trf" to "tribunal regional federal",
        "tj" to "tribunal de justica",
        "inss" to "instituto nacional do seguro social",
        "eb" to "exercito brasileiro",
    )
    private val ignored = setOf("de", "da", "do", "das", "dos", "e", "a", "o", "para", "concurso", "edital", "prova")

    fun normalize(value: String): String = Normalizer.normalize(value.lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "")
        .replace(Regex("[^a-z0-9]+"), " ")
        .trim()

    private fun words(value: String) = normalize(value).split(' ').filter { it.isNotBlank() }

    fun search(entries: List<ExamCatalogEntry>, query: String, limit: Int = 30): List<ExamCatalogEntry> {
        val terms = words(query).filter { it !in ignored }
        if (terms.isEmpty()) return emptyList()
        return entries.mapNotNull { entry ->
            val haystack = words(listOf(entry.shortName, entry.agency, entry.role, entry.board.orEmpty(), entry.year?.toString().orEmpty(), entry.searchNorm).joinToString(" "))
            val score = score(terms, haystack, entry) ?: return@mapNotNull null
            entry to score
        }
            .sortedWith(compareByDescending<Pair<ExamCatalogEntry, Int>> { it.second }.thenByDescending { it.first.year ?: 0 }.thenBy { it.first.role })
            .take(limit)
            .map { it.first }
    }

    /** null quando alguma palavra digitada não aparece; senão, quanto maior, mais parecido. */
    private fun score(terms: List<String>, haystack: List<String>, entry: ExamCatalogEntry): Int? {
        var total = 0
        for (term in terms) {
            val direct = haystack.any { it.startsWith(term) }
            val expanded = expansions[term]?.split(' ')?.filter { it !in ignored }?.all { part -> haystack.any { it.startsWith(part) } } == true
            if (!direct && !expanded) return null
            total += when {
                haystack.any { it == term } -> 3
                direct -> 2
                else -> 1
            }
        }
        val shortName = normalize(entry.shortName)
        val query = terms.joinToString(" ")
        if (shortName == query || shortName.replace(" ", "") == query.replace(" ", "")) total += 10
        else if (shortName.startsWith(query)) total += 5
        return total
    }
}

/**
 * Lê o catálogo direto do Supabase (leitura pública, só editais publicados). A lista fica em memória
 * e num arquivo do app: abre na hora, funciona sem internet com a última cópia e é atualizada em
 * segundo plano quando passa de algumas horas. As matérias de cada edital só descem quando a pessoa
 * escolhe um.
 */
class ExamCatalogRepository(
    private val baseUrl: String,
    private val apiKey: String,
    private val cacheFile: File?,
    private val clock: () -> Long = System::currentTimeMillis,
    private val http: suspend (String) -> String = { url -> defaultGet(url, apiKey) },
) {
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
    private val mutex = Mutex()
    @Volatile private var memory: List<ExamCatalogEntry>? = null
    @Volatile private var fetchedAt: Long = 0

    val isConfigured: Boolean get() = baseUrl.isNotBlank() && apiKey.isNotBlank()

    /** Última lista conhecida (memória ou arquivo), sem rede. */
    suspend fun cached(): List<ExamCatalogEntry>? = withContext(Dispatchers.IO) {
        memory ?: readCache()?.also { memory = it }
    }

    /**
     * Lista do catálogo. Com [force] ou cópia velha, busca no servidor; se a rede falhar, devolve a
     * última cópia e só lança erro quando não há nenhuma.
     */
    suspend fun entries(force: Boolean = false): List<ExamCatalogEntry> = mutex.withLock {
        val current = memory ?: withContext(Dispatchers.IO) { readCache() }?.also { memory = it }
        val fresh = current != null && clock() - fetchedAt < MAX_AGE_MILLIS
        if (current != null && fresh && !force) return current
        try {
            val fields = "id,short_name,agency,role,board,year,edital_ref,source_url,notice,search_norm,subject_count,topic_count,ready_topic_count"
            // O servidor devolve no máximo 1.000 linhas por consulta: busca em páginas até acabar.
            val list = mutableListOf<ExamCatalogEntry>()
            while (true) {
                val body = http("${baseUrl.trimEnd('/')}/rest/v1/exam_catalog?select=$fields&status=eq.PUBLISHED&order=short_name.asc,role.asc,id.asc&limit=$PAGE_SIZE&offset=${list.size}")
                val page = json.decodeFromString(ListSerializer(ExamCatalogEntry.serializer()), body)
                list += page
                if (page.size < PAGE_SIZE) break
            }
            memory = list
            fetchedAt = clock()
            withContext(Dispatchers.IO) { writeCache(json.encodeToString(ListSerializer(ExamCatalogEntry.serializer()), list)) }
            list
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            current ?: throw ExamCatalogException("Não consegui carregar a lista de editais agora. Confira a internet e tente de novo.", error)
        }
    }

    /** Matérias e tópicos de um edital, na ordem do edital. */
    suspend fun subjects(id: String): List<ExamCatalogSubject> {
        val encoded = URLEncoder.encode(id, StandardCharsets.UTF_8.name())
        val body = try {
            http("${baseUrl.trimEnd('/')}/rest/v1/exam_catalog?select=subjects&id=eq.$encoded&status=eq.PUBLISHED&limit=1")
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            throw ExamCatalogException("Não consegui baixar as matérias deste edital. Confira a internet e tente de novo.", error)
        }
        val rows = runCatching { json.decodeFromString(ListSerializer(ExamCatalogSubjectsRow.serializer()), body) }
            .getOrElse { throw ExamCatalogException("O edital veio num formato inesperado. Tente de novo em instantes.", it) }
        val subjects = rows.firstOrNull()?.subjects.orEmpty()
            .map { subject -> subject.copy(name = subject.name.trim(), topics = subject.topics.map(String::trim).filter(String::isNotBlank)) }
            .filter { it.name.isNotBlank() && it.topics.isNotEmpty() }
        if (subjects.isEmpty()) throw ExamCatalogException("Este edital não está mais disponível no catálogo. Escolha outro ou anexe o PDF.")
        return subjects
    }

    /**
     * A prova oficial do edital (o simulado nº 1), ou null quando o edital não tem prova pronta. A
     * prova só é lida com conta ([accessToken]): sem login, o servidor não entrega.
     */
    suspend fun officialSimulation(catalogId: String, accessToken: String?): OfficialSimulation? {
        if (accessToken == null) return null
        val encoded = URLEncoder.encode(catalogId, StandardCharsets.UTF_8.name())
        val body = try {
            defaultGet("${baseUrl.trimEnd('/')}/rest/v1/exam_simulation?select=id,catalog_id,board,format,structure,item_count,questions&catalog_id=eq.$encoded&status=eq.PUBLISHED&order=number.asc&limit=1", apiKey, accessToken)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            throw ExamCatalogException("Não consegui baixar a prova oficial agora. Confira a internet e tente de novo.", error)
        }
        return runCatching { json.decodeFromString(ListSerializer(OfficialSimulation.serializer()), body) }
            .getOrElse { throw ExamCatalogException("A prova oficial veio num formato inesperado. Tente de novo em instantes.", it) }
            .firstOrNull()
            ?.takeIf { it.questions.isNotEmpty() }
    }

    /**
     * Edital do catálogo de onde o concurso veio: o id gravado na escolha ([linked]) ou, para
     * concursos montados antes disso, o edital com o mesmo nome ("PF 2025" ou "PF 2025 · Agente").
     */
    suspend fun catalogIdFor(competitionName: String, linked: String?): String? {
        linked?.let { return it }
        val list = runCatching { entries() }.getOrNull() ?: cached() ?: return null
        val name = ExamCatalogSearch.normalize(competitionName)
        return list.firstOrNull { ExamCatalogSearch.normalize(it.title) == name }?.id
            ?: list.filter { ExamCatalogSearch.normalize(it.shortName) == name }.singleOrNull()?.id
    }

    private fun readCache(): List<ExamCatalogEntry>? = runCatching {
        val file = cacheFile?.takeIf { it.exists() } ?: return null
        json.decodeFromString(ListSerializer(ExamCatalogEntry.serializer()), file.readText())
    }.getOrNull()

    private fun writeCache(body: String) {
        val file = cacheFile ?: return
        runCatching {
            file.parentFile?.mkdirs()
            val temp = File(file.parentFile, "${file.name}.tmp")
            temp.writeText(body)
            if (!temp.renameTo(file)) { file.delete(); temp.renameTo(file) }
        }
    }

    companion object {
        const val MAX_AGE_MILLIS = 6L * 60 * 60 * 1000
        private const val PAGE_SIZE = 1000
        private const val TIMEOUT_MILLIS = 15_000

        private suspend fun defaultGet(url: String, apiKey: String, bearer: String = apiKey): String = withContext(Dispatchers.IO) {
            val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = TIMEOUT_MILLIS
                readTimeout = TIMEOUT_MILLIS
                setRequestProperty("apikey", apiKey)
                setRequestProperty("Authorization", "Bearer $bearer")
                setRequestProperty("Accept", "application/json")
            }
            try {
                val code = connection.responseCode
                val stream = if (code in 200..299) connection.inputStream else connection.errorStream
                val text = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
                if (code !in 200..299) throw ExamCatalogException("HTTP $code")
                text
            } finally {
                connection.disconnect()
            }
        }
    }
}

class ExamCatalogException(message: String, cause: Throwable? = null) : Exception(message, cause)

/** Concurso local → edital do catálogo de onde ele foi montado (para achar a prova oficial). */
object CatalogLinks {
    private fun prefs(context: android.content.Context) =
        context.applicationContext.getSharedPreferences("catalog_links", android.content.Context.MODE_PRIVATE)

    fun save(context: android.content.Context, competitionId: Long, catalogId: String) =
        prefs(context).edit().putString(competitionId.toString(), catalogId).apply()

    fun get(context: android.content.Context, competitionId: Long): String? = prefs(context).getString(competitionId.toString(), null)
}

/** Uma instância por processo: a lista em memória vale para todas as telas. */
object ExamCatalogProvider {
    @Volatile private var instance: ExamCatalogRepository? = null

    fun get(context: android.content.Context): ExamCatalogRepository = instance ?: synchronized(this) {
        instance ?: ExamCatalogRepository(
            baseUrl = br.com.estudario.BuildConfig.SUPABASE_URL,
            apiKey = br.com.estudario.BuildConfig.SUPABASE_PUBLISHABLE_KEY,
            cacheFile = File(context.applicationContext.filesDir, "catalog/exam_catalog.json"),
        ).also { instance = it }
    }
}

/**
 * A banca do edital de um concurso: a do perfil da prova ou, sem ela, a do catálogo de onde o
 * concurso veio. "" quando não se sabe. É a banca que vai no pedido da matéria quando a pessoa ainda
 * não tem questões naquela matéria (sem banca, a matéria vinha sem "Como a banca cobra").
 */
suspend fun editalBoardOf(context: android.content.Context, competitionId: Long, competitionName: String): String {
    val app = context.applicationContext as? br.com.estudario.EstudarioApplication ?: return ""
    app.database.simulationDao().examProfile(competitionId)?.board?.trim()?.takeIf(String::isNotBlank)?.let { return it }
    return runCatching {
        val catalog = ExamCatalogProvider.get(context)
        if (!catalog.isConfigured) return@runCatching ""
        val id = catalog.catalogIdFor(competitionName, CatalogLinks.get(context, competitionId)) ?: return@runCatching ""
        catalog.entries().firstOrNull { it.id == id }?.board?.trim().orEmpty()
    }.getOrDefault("")
}
