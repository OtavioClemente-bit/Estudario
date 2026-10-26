package br.com.estudario.web.data

import kotlinx.browser.document
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Serializable
internal data class ForecastRequest(
    val snapshot: String,
    val planId: String,
    val freeRules: Boolean,
    val excludedTopicIds: Set<Long>,
)

/** O worker importa o mesmo motor do app; não acessa rede, sessão ou armazenamento da conta. */
internal fun installForecastWorker(): Boolean {
    if (!(js("typeof WorkerGlobalScope !== 'undefined' && self instanceof WorkerGlobalScope") as Boolean)) return false
    val worker: dynamic = js("self")
    worker.onmessage = { event: dynamic ->
        try {
            val request = Json.decodeFromString<ForecastRequest>(event.data as String)
            val data = Snapshot.parse(request.snapshot)
            val plan = data.plans.firstOrNull { it.id == request.planId } ?: error("Plano indisponível")
            val result = Forecast.compute(data, plan, request.freeRules, request.excludedTopicIds)
            worker.postMessage(kotlin.js.json("ok" to true, "result" to Json.encodeToString<Forecast.Result?>(result)))
        } catch (_: Throwable) {
            worker.postMessage(kotlin.js.json("ok" to false))
        }
    }
    return true
}

// Sem teorias, respostas, anotações ou outras tabelas que a previsão não lê. Os campos grandes das
// questões também ficam fora; somente a relação questão/tópico é necessária para o motor.
internal val forecastTables = listOf(Keys.PLANS, Keys.PLAN_SUBJECTS, Keys.TOPICS, Keys.QUESTIONS,
    Keys.ATTEMPTS, Keys.REVIEWS, Keys.TASKS, Keys.EXECUTIONS, Keys.AVAILABILITY, "studyDayOverrides")

internal class ForecastKey(
    val data: Snapshot,
    val planId: String,
    val day: String,
    val zone: String,
    val freeRules: Boolean,
    val excludedTopicIds: Set<Long>,
) {
    fun matches(other: ForecastKey): Boolean = planId == other.planId && day == other.day &&
        zone == other.zone && freeRules == other.freeRules && excludedTopicIds == other.excludedTopicIds &&
        forecastTables.all { data.root[it] === other.data.root[it] }

    fun request(): ForecastRequest {
        val root = data.root.filterKeys { it in forecastTables }.toMutableMap()
        fun trim(key: String, fields: Set<String>, requiredText: String) {
            root[key] = kotlinx.serialization.json.JsonArray(data.array(key).mapNotNull { row ->
                (row as? JsonObject)?.let { JsonObject(it.filterKeys { field -> field in fields } + (requiredText to JsonPrimitive(""))) }
            })
        }
        trim(Keys.QUESTIONS, setOf("id", "topicId"), "statement")
        trim(Keys.ATTEMPTS, setOf("id", "questionId", "correct", "answeredAt"), "selectedKey")
        return ForecastRequest(JsonObject(root).toString(), planId, freeRules, excludedTopicIds)
    }
}

data class ForecastLoad(val loading: Boolean = true, val result: Forecast.Result? = null, val failed: Boolean = false)

/** Compartilha o cálculo entre Início/cabeçalho/visão geral, inclusive ao trocar de tela. */
object ForecastService {
    private val scope = MainScope()
    private var key: ForecastKey? = null
    private var job: Deferred<ForecastLoad>? = null

    private fun key(data: Snapshot, plan: StudyPlan) = ForecastKey(data, plan.id,
        Queries.todayDate().toString(), Queries.zone.id, LibraryGaps.free(), LibraryGaps.excluded())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun cached(data: Snapshot, plan: StudyPlan): ForecastLoad? =
        job?.takeIf { key?.matches(key(data, plan)) == true && it.isCompleted && !it.isCancelled }
            ?.getCompleted()?.takeUnless { it.failed }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    suspend fun load(data: Snapshot, plan: StudyPlan): ForecastLoad {
        val wanted = key(data, plan)
        val existing = job
        if (key?.matches(wanted) == true && existing != null && !existing.isCancelled &&
            (!existing.isCompleted || !existing.getCompleted().failed)) return existing.await()
        // A foto mudou enquanto calculava: encerra o worker antigo; seu resultado não substitui o novo.
        existing?.cancel()
        key = wanted
        return scope.async {
            try {
                ForecastLoad(loading = false, result = runWorker(wanted.request()))
            } catch (_: kotlinx.coroutines.TimeoutCancellationException) {
                ForecastLoad(loading = false, failed = true)
            } catch (cancelled: kotlinx.coroutines.CancellationException) {
                throw cancelled
            } catch (_: Throwable) {
                ForecastLoad(loading = false, failed = true)
            }
        }.also { job = it }.await()
    }

    private suspend fun runWorker(request: ForecastRequest): Forecast.Result? = withTimeout(90_000) {
        suspendCancellableCoroutine { continuation ->
            // Usa a versão do bundle que abriu esta aba, inclusive depois de uma nova publicação.
            val query = document.querySelector("script[src*='estudario.js']")?.getAttribute("src")
                ?.substringAfter('?', "").orEmpty()
            val url = "forecast-worker.js" + if (query.isEmpty()) "" else "?$query"
            val worker: dynamic = js("new Worker(url)")
            continuation.invokeOnCancellation { worker.terminate() }
            worker.onmessage = { event: dynamic ->
                worker.terminate()
                if (continuation.isActive) {
                    try {
                        check(event.data.ok == true)
                        continuation.resume(Json.decodeFromString<Forecast.Result?>(event.data.result as String))
                    } catch (error: Throwable) { continuation.resumeWithException(error) }
                }
            }
            worker.onerror = { event: dynamic ->
                event.preventDefault()
                worker.terminate()
                if (continuation.isActive) continuation.resumeWithException(IllegalStateException("Previsão indisponível"))
            }
            worker.postMessage(Json.encodeToString(request))
        }
    }
}
