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
class Snapshot private constructor(val root: JsonObject) {
    private val cache = HashMap<String, List<Any>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> list(key: String, serializer: KSerializer<T>): List<T> = cache.getOrPut(key) {
        array(key).mapNotNull { element -> runCatching { snapshotJson.decodeFromJsonElement(serializer, element) }.getOrNull() }
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
    fun encode(): String = snapshotJson.encodeToString(JsonObject.serializer(), root)

    // ----- edição -----

    /** Nova foto com os itens de [key] transformados; os que [transform] devolve null são removidos. */
    fun edit(key: String, transform: (JsonObject) -> JsonObject?): Snapshot {
        val updated = JsonArray(array(key).mapNotNull { (it as? JsonObject)?.let(transform) })
        return Snapshot(JsonObject(root + (key to updated)))
    }

    fun append(key: String, item: JsonObject): Snapshot =
        Snapshot(JsonObject(root + (key to JsonArray(array(key) + item))))

    fun appendAll(changes: Map<String, List<JsonObject>>): Snapshot {
        val next = root.toMutableMap()
        changes.forEach { (key, items) -> next[key] = JsonArray(array(key) + items) }
        return Snapshot(JsonObject(next))
    }

    /** Próximo id numérico livre de uma tabela (mesma regra do autoincremento do Room). */
    fun nextId(key: String): Long = (array(key).maxOfOrNull { ((it as? JsonObject)?.get("id") as? JsonPrimitive)?.longOrNull ?: 0L } ?: 0L) + 1

    companion object {
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
