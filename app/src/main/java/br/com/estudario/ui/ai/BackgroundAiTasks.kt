package br.com.estudario.ui.ai

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import br.com.estudario.notifications.AiGenerationNotifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Gerações da IA que continuam quando a janela é fechada.
 *
 * A pessoa pode fechar a tela de "gerando" de propósito ou sem querer, ou minimizar o app: o
 * trabalho segue no escopo do app, um aviso no topo mostra que ainda está gerando e, quando
 * termina, o resultado espera pronto para revisar. Com o app minimizado, as notificações fazem o
 * mesmo papel (ver [AiGenerationNotifications]). Nada se perde por um toque errado.
 */
object BackgroundAiTasks {
    sealed interface Status {
        data object Running : Status
        /** [result] é o texto (.estudo ou .plano) que segue para a prévia de importação. */
        data class Ready(val result: String, val competitionId: Long?) : Status
        data class Failed(val message: String) : Status
    }

    data class Task(val id: String, val title: String, val kind: String, val status: Status, val inForeground: Boolean, val open: (() -> Unit)? = null)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val jobs = mutableMapOf<String, Job>()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()
    private var appContext: Context? = null

    /** Telas do app abertas agora; zero = app minimizado ou fechado. */
    private var startedActivities = 0
    private val appVisible get() = startedActivities > 0

    fun init(context: Context) {
        appContext = context.applicationContext
        (context.applicationContext as? Application)?.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityStarted(activity: Activity) {
                startedActivities++
                // Voltou para o app: o aviso de andamento sai, a própria tela mostra o progresso.
                if (startedActivities == 1) runningTasks().forEach { AiGenerationNotifications.cancel(activity, it.id) }
            }

            override fun onActivityStopped(activity: Activity) {
                startedActivities = (startedActivities - 1).coerceAtLeast(0)
                // Minimizou com geração em andamento: o aviso fixo mostra que ela continua.
                if (startedActivities == 0) runningTasks().forEach { AiGenerationNotifications.showRunning(activity, it.id, it.title, it.kind) }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })
    }

    private fun runningTasks() = _tasks.value.filter { it.status == Status.Running }

    /**
     * Começa (ou reaproveita, se já estiver rodando com o mesmo [id]) uma geração. [work] devolve o
     * texto pronto ou lança erro com uma mensagem legível.
     */
    fun start(id: String, title: String, kind: String, competitionId: Long?, open: (() -> Unit)? = null, work: suspend () -> String) {
        if (jobs[id]?.isActive == true) return
        upsert(Task(id, title, kind, Status.Running, inForeground = true, open = open))
        jobs[id] = scope.launch {
            val status = runCatching { work() }.fold(
                onSuccess = { Status.Ready(it, competitionId) },
                onFailure = { Status.Failed(it.message ?: "Não foi possível gerar agora. Tente de novo.") },
            )
            val current = find(id)
            // Terminou com o app minimizado: o resultado vai para o aviso do topo, que a pessoa vê ao voltar.
            val unseen = current?.inForeground == false || !appVisible
            upsert((current ?: Task(id, title, kind, status, false)).copy(status = status, inForeground = current?.inForeground == true && appVisible))
            if (unseen) notifyDone(id, title, kind, status) else appContext?.let { AiGenerationNotifications.cancel(it, id) }
        }
    }

    fun find(id: String): Task? = _tasks.value.firstOrNull { it.id == id }

    /** A janela foi fechada: a geração continua, e o aviso global passa a acompanhá-la. */
    fun sendToBackground(id: String) = _tasks.update { list -> list.map { if (it.id == id) it.copy(inForeground = false) else it } }

    fun bringToForeground(id: String) = _tasks.update { list -> list.map { if (it.id == id) it.copy(inForeground = true) else it } }

    /** O resultado foi entregue (aberto na prévia) ou a pessoa descartou o aviso. */
    fun dismiss(id: String) {
        jobs.remove(id)?.let { if (it.isActive) it.cancel() }
        _tasks.update { list -> list.filterNot { it.id == id } }
        appContext?.let { AiGenerationNotifications.cancel(it, id) }
    }

    private fun upsert(task: Task) = _tasks.update { list -> list.filterNot { it.id == task.id } + task }

    private fun notifyDone(id: String, title: String, kind: String, status: Status) {
        val context = appContext ?: return
        when (status) {
            is Status.Ready -> AiGenerationNotifications.showReady(context, id, title, kind)
            is Status.Failed -> AiGenerationNotifications.showFailed(context, id, title, kind, status.message)
            Status.Running -> Unit
        }
    }
}
