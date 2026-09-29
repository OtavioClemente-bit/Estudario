package br.com.estudario.ui.ai

import android.content.Context
import br.com.estudario.notifications.StudyNotificationCoordinator
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
 * A pessoa pode fechar a tela de "gerando" de propósito ou sem querer: o trabalho segue no escopo do
 * app, um aviso no topo mostra que ainda está gerando e, quando termina, o resultado espera pronto
 * para revisar. Nada se perde por um toque errado.
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

    fun init(context: Context) { appContext = context.applicationContext }

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
            upsert(find(id)?.copy(status = status) ?: Task(id, title, kind, status, false))
            if (find(id)?.inForeground == false) notifyDone(title, kind, status)
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
    }

    private fun upsert(task: Task) = _tasks.update { list -> list.filterNot { it.id == task.id } + task }

    private fun notifyDone(title: String, kind: String, status: Status) {
        val context = appContext ?: return
        val (head, body) = when (status) {
            is Status.Ready -> "$kind pronto" to "\"$title\" está pronto. Abra o Estudário para revisar e salvar."
            is Status.Failed -> "Não deu para gerar $kind".lowercase().replaceFirstChar { it.uppercase() } to status.message
            Status.Running -> return
        }
        runCatching { StudyNotificationCoordinator.post(context, "pending_study", ("ai:" + title).hashCode(), head, body) }
    }
}
