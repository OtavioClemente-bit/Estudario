package br.com.estudario.data.ai

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import br.com.estudario.EstudarioApplication
import br.com.estudario.ui.ai.BackgroundAiTasks
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Gerações de matéria e de questões aceitas pelo servidor e ainda não entregues.
 *
 * A geração acontece no servidor; o app só acompanha. Se o Android fecha o app em segundo plano
 * (comum em tablet e celular com pouca memória, às vezes só por minimizar), o acompanhamento morria
 * junto e a geração "sumia". Agora o id do pedido fica guardado aqui: ao abrir o app de novo, ou pelo
 * trabalho em segundo plano ([PendingContentWorker]), o acompanhamento recomeça do mesmo pedido, sem
 * gastar cota de novo, e o resultado chega como sempre (aviso no topo ou notificação "pronto").
 * Sai daqui quando a pessoa abre o resultado ou descarta o aviso.
 */
object PendingContentJobs {
    data class Job(
        val taskId: String, val jobId: String, val title: String, val kind: String,
        val competitionId: Long, val subjectId: Long, val topicId: Long, val createdAt: Long,
    )

    private const val PREFS = "ai_content_jobs"
    private const val KEY = "jobs"
    /** O servidor guarda o resultado por bem mais que isso; depois de um dia o aviso já perdeu o sentido. */
    private const val MAX_AGE_MILLIS = 24 * 60 * 60_000L

    private fun prefs(context: Context) = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    @Synchronized
    fun list(context: Context): List<Job> = runCatching {
        val array = JSONArray(prefs(context).getString(KEY, "[]"))
        (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            Job(o.getString("taskId"), o.getString("jobId"), o.getString("title"), o.getString("kind"),
                o.getLong("competitionId"), o.getLong("subjectId"), o.getLong("topicId"), o.getLong("createdAt"))
        }
    }.getOrDefault(emptyList()).filter { System.currentTimeMillis() - it.createdAt < MAX_AGE_MILLIS }

    @Synchronized
    private fun write(context: Context, jobs: List<Job>) {
        val array = JSONArray()
        jobs.forEach { j ->
            array.put(JSONObject().put("taskId", j.taskId).put("jobId", j.jobId).put("title", j.title).put("kind", j.kind)
                .put("competitionId", j.competitionId).put("subjectId", j.subjectId).put("topicId", j.topicId).put("createdAt", j.createdAt))
        }
        prefs(context).edit().putString(KEY, array.toString()).commit()
    }

    @Synchronized
    fun save(context: Context, job: Job) {
        write(context, list(context).filterNot { it.taskId == job.taskId } + job)
        PendingContentWorker.enqueue(context)
    }

    @Synchronized
    fun remove(context: Context, taskId: String) {
        val all = list(context)
        if (all.any { it.taskId == taskId }) write(context, all.filterNot { it.taskId == taskId })
    }

    /**
     * Recomeça o acompanhamento de cada pedido guardado que não está rodando neste processo. Devolve
     * quantos foram retomados. Chamado ao abrir o app e pelo [PendingContentWorker].
     */
    suspend fun resumeAll(app: EstudarioApplication): Int {
        var resumed = 0
        list(app).forEach { job ->
            if (BackgroundAiTasks.find(job.taskId) != null) return@forEach
            val dao = app.database.dao()
            val competition = dao.competitionById(job.competitionId)
            val subject = dao.subjectsFor(job.competitionId).firstOrNull { it.id == job.subjectId }
            val target = dao.topic(job.topicId)
            if (competition == null || subject == null || target == null) { remove(app, job.taskId); return@forEach }
            val topics = dao.topicsFor(subject.id)
            resumed++
            BackgroundAiTasks.start(job.taskId, job.title, job.kind, competition.id) {
                var result: String? = null
                app.aiContentGenerator.resume(job.jobId, competition, subject, topics, target).collect { value ->
                    when (value) {
                        is AiContentProgress.Done -> result = value.estudo
                        is AiContentProgress.Failed -> throw IllegalStateException(value.message)
                        else -> Unit
                    }
                }
                result ?: throw IllegalStateException("A geração terminou sem resultado. Tente de novo.")
            }
            // Retomado sem a tela aberta: o aviso do topo (ou a notificação) acompanha.
            BackgroundAiTasks.sendToBackground(job.taskId)
        }
        return resumed
    }
}

/**
 * Acompanha pelo WorkManager as gerações guardadas quando o app não está aberto: o Android pode ter
 * fechado o app, mas o WorkManager volta a rodar e, quando a matéria fica pronta, a notificação
 * "pronto" aparece. Ao abrir o app, o resultado já espera no aviso do topo.
 */
class PendingContentWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        val app = applicationContext as EstudarioApplication
        if (PendingContentJobs.list(app).isEmpty()) return Result.success()
        PendingContentJobs.resumeAll(app)
        // Espera os acompanhamentos deste processo terminarem (o limite do WorkManager é 10 min).
        val until = System.currentTimeMillis() + 9 * 60_000L
        while (System.currentTimeMillis() < until) {
            val ids = PendingContentJobs.list(app).map { it.taskId }.toSet()
            if (BackgroundAiTasks.tasks.value.none { it.id in ids && it.status == BackgroundAiTasks.Status.Running }) break
            delay(3_000)
        }
        return Result.success()
    }

    companion object {
        private const val NAME = "ai-content-pending"

        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<PendingContentWorker>()
                // Dá tempo de o acompanhamento normal (com o app aberto) terminar sozinho primeiro.
                .setInitialDelay(45, TimeUnit.SECONDS)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setBackoffCriteria(BackoffPolicy.LINEAR, 30, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(NAME, ExistingWorkPolicy.KEEP, request)
        }
    }
}
