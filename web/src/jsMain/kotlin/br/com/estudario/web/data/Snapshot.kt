package br.com.estudario.web.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull

internal val snapshotJson = Json { ignoreUnknownKeys = true; coerceInputValues = true; explicitNulls = true }

/**
 * A foto de estudo carregada no navegador. O JSON original é a fonte da verdade: as leituras
 * decodificam só os campos que as telas usam, e as edições trocam objetos inteiros por id,
 * preservando todos os outros campos para o app Android receber exatamente o que mandou.
 */
class Snapshot private constructor(val root: JsonObject, parent: Snapshot? = null, reuse: Map<String, Array<Any?>> = emptyMap()) {
    /**
     * Cada edição gera uma foto nova. Antes ela começava sem nada decodificado: responder uma questão
     * fazia todas as telas decodificarem de novo as 9 mil tarefas do plano, as questões, as tentativas...
     * e o site travava uns segundos no Confirmar e no Finalizar. Agora a foto nova herda da anterior
     * o que não mudou: as tabelas inteiras que são o mesmo objeto e, dentro da tabela editada, cada
     * item que continuou igual. Só o item novo ou trocado é decodificado de novo.
     */
    private val decoded = HashMap<String, Array<Any?>>()
    private val cache = HashMap<String, List<Any>>()

    /** O JSON de cada tabela já escrito em texto: o envio junta os pedaços e só reescreve o que mudou. */
    private val encoded = HashMap<String, String>()

    init {
        if (parent != null) {
            parent.decoded.forEach { (key, items) -> if (root[key] === parent.root[key]) decoded[key] = items }
            parent.cache.forEach { (key, items) -> if (root[key] === parent.root[key]) cache[key] = items }
            parent.encoded.forEach { (key, text) -> if (root[key] === parent.root[key]) encoded[key] = text }
        }
        decoded.putAll(reuse)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> list(key: String, serializer: KSerializer<T>): List<T> = cache.getOrPut(key) {
        val elements = array(key)
        val items = decoded[key]?.takeIf { it.size == elements.size } ?: arrayOfNulls<Any?>(elements.size).also { it.fill(PENDING) }
        for (i in items.indices) {
            if (items[i] === PENDING) items[i] = runCatching { snapshotJson.decodeFromJsonElement(serializer, elements[i]) }.getOrNull()
        }
        decoded[key] = items
        items.filterNotNull().filter { it !== PENDING }
    } as List<T>

    fun array(key: String): JsonArray = root[key] as? JsonArray ?: JsonArray(emptyList())

    val competitions get() = list(Keys.COMPETITIONS, Competition.serializer())
    val subjects get() = list(Keys.SUBJECTS, Subject.serializer())
    val topics get() = list(Keys.TOPICS, Topic.serializer())
    val summaries get() = list(Keys.SUMMARIES, Summary.serializer())
    val theories get() = list(Keys.THEORIES, Theory.serializer())
    val snippets get() = list(Keys.SNIPPETS, Snippet.serializer())
    val questions get() = list(Keys.QUESTIONS, Question.serializer())
    val attempts get() = list(Keys.ATTEMPTS, Attempt.serializer())
    val errors get() = list(Keys.ERRORS, ErrorEntry.serializer())
    val reviews get() = list(Keys.REVIEWS, Review.serializer())
    val reviewHistory get() = list(Keys.REVIEW_HISTORY, ReviewHistory.serializer())
    val sessions get() = list(Keys.SESSIONS, StudySession.serializer())
    val focusSessions get() = list(Keys.FOCUS_SESSIONS, FocusSession.serializer())
    val questionSessions get() = list(Keys.QUESTION_SESSIONS, QuestionSession.serializer())
    val plans get() = list(Keys.PLANS, StudyPlan.serializer())
    val tasks get() = list(Keys.TASKS, PlanTask.serializer())
    val executions get() = list(Keys.EXECUTIONS, TaskExecution.serializer())
    val availability get() = list(Keys.AVAILABILITY, Availability.serializer())
    val planSubjects get() = list(Keys.PLAN_SUBJECTS, PlanSubject.serializer())
    val simulations get() = list(Keys.SIMULATIONS, Simulation.serializer())
    val errorConcepts get() = list(Keys.ERROR_CONCEPTS, ErrorConcept.serializer())
    val errorConceptEntries get() = list(Keys.ERROR_CONCEPT_ENTRIES, ErrorConceptEntry.serializer())
    val notes get() = list("notes", UserNote.serializer())
    val theoryMarks get() = list("theoryMarks", TheoryMark.serializer())
    val contentSources get() = list("contentSources", ContentSource.serializer())
    val importPackages get() = list("importPackages", ImportPackage.serializer())

    val isEmpty: Boolean get() = competitions.isEmpty() && plans.isEmpty()

    /** Texto canônico (compacto, sem horário de exportação): o mesmo que o app calcula o hash. */
    fun encode(): String = buildString {
        // O mesmo texto de antes (JSON compacto, campos na mesma ordem), montado tabela por tabela.
        append('{')
        var first = true
        root.forEach { (key, value) ->
            if (!first) append(',')
            first = false
            append(snapshotJson.encodeToString(JsonPrimitive.serializer(), JsonPrimitive(key)))
            append(':')
            append(encoded.getOrPut(key) { snapshotJson.encodeToString(JsonElement.serializer(), value) })
        }
        append('}')
    }

    // ----- edição -----

    /** Nova foto com os itens de [key] transformados; os que [transform] devolve null são removidos. */
    fun edit(key: String, transform: (JsonObject) -> JsonObject?): Snapshot {
        val before = array(key)
        val old = decoded[key]?.takeIf { it.size == before.size }
        val kept = ArrayList<JsonElement>(before.size)
        val items = ArrayList<Any?>(before.size)
        before.forEachIndexed { i, element ->
            val next = (element as? JsonObject)?.let(transform) ?: return@forEachIndexed
            kept += next
            // o item que continuou o mesmo objeto aproveita o que já estava decodificado
            items += if (old != null && next === element) old[i] else PENDING
        }
        return Snapshot(JsonObject(root + (key to JsonArray(kept))), this, mapOf(key to items.toTypedArray()))
    }

    /** Nova foto com o objeto [key] trocado (campos que não são tabela, como o visual do Folha). */
    fun withObject(key: String, value: JsonObject): Snapshot = Snapshot(JsonObject(root + (key to value)), this)

    fun obj(key: String): JsonObject? = root[key] as? JsonObject

    fun append(key: String, item: JsonObject): Snapshot {
        val before = array(key)
        val reuse = decoded[key]?.takeIf { it.size == before.size }?.let { mapOf(key to (it.toList() + PENDING).toTypedArray()) } ?: emptyMap()
        return Snapshot(JsonObject(root + (key to JsonArray(before + item))), this, reuse)
    }

    fun appendAll(changes: Map<String, List<JsonObject>>): Snapshot {
        val next = root.toMutableMap()
        val reuse = HashMap<String, Array<Any?>>()
        changes.forEach { (key, items) ->
            val before = array(key)
            next[key] = JsonArray(before + items)
            decoded[key]?.takeIf { it.size == before.size }?.let { reuse[key] = (it.toList() + List(items.size) { PENDING }).toTypedArray() }
        }
        return Snapshot(JsonObject(next), this, reuse)
    }

    /**
     * Próximo id numérico de uma tabela, [ID_GAP] acima do maior. O celular numera o que cria com
     * maior + 1; se o site usasse o mesmo número, o que os dois criassem ao mesmo tempo (matéria
     * gerada aqui, questão respondida lá) teria o mesmo id e, ao juntar, um dos dois sumia.
     */
    fun nextId(key: String): Long = (array(key).maxOfOrNull { ((it as? JsonObject)?.get("id") as? JsonPrimitive)?.longOrNull ?: 0L } ?: 0L) + ID_GAP

    companion object {
        /** Marca de item ainda não decodificado. */
        private val PENDING = Any()

        /** Folga entre os ids do celular e os do site (ver [nextId]). */
        const val ID_GAP = 100_000L

        fun parse(text: String): Snapshot {
            val root = snapshotJson.parseToJsonElement(text).jsonObject
            val format = root.str("format")
            require(format == null || format == "estudario-backup" || format == "backup") { "Formato de dados desconhecido." }
            return Snapshot(JsonObject(root - "exportedAt"))
        }

        fun empty(): Snapshot = Snapshot(
            JsonObject(mapOf("format" to JsonPrimitive("estudario-backup"), "version" to JsonPrimitive(8))),
        )
    }
}

/** Cópia de um objeto JSON com campos trocados. */
fun JsonObject.with(vararg fields: Pair<String, JsonElement>): JsonObject = JsonObject(this + fields)

fun jsonOf(vararg fields: Pair<String, Any?>): JsonObject = JsonObject(fields.associate { (key, value) -> key to value.toJsonElement() })

fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> kotlinx.serialization.json.JsonNull
    is JsonElement -> this
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    else -> JsonPrimitive(toString())
}
