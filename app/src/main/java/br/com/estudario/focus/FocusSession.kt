package br.com.estudario.focus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.preferences.FocusSessionPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Regras da sessão de foco, num lugar só, sem depender da tela estar aberta: o botão da
 * notificação, a rede de segurança e a abertura do app chamam exatamente estas funções.
 */
object FocusSessionManager {

    /** Minutos abaixo disso não viram registro de estudo — foi um toque sem querer. */
    private const val MIN_MINUTES_TO_RECORD = 1

    suspend fun start(context: Context, title: String, topicId: Long? = null, taskId: String? = null): Long {
        val app = context.applicationContext as EstudarioApplication
        // Já existe sessão aberta: respeitar a que está correndo em vez de zerar o cronômetro dela.
        val atual = app.preferences.focusSession.first()
        if (atual.active) {
            FocusMode.showOngoing(context, atual.title, atual.startedAt, dndOn = atual.previousFilter != FocusSessionPrefs.FILTER_UNKNOWN)
            return atual.startedAt
        }
        val startedAt = System.currentTimeMillis()
        val wantsDnd = app.preferences.focusDoNotDisturb.first()
        val previousFilter = if (wantsDnd) FocusMode.turnOnDnd(context) else FocusSessionPrefs.FILTER_UNKNOWN
        app.preferences.startFocusSession(startedAt, title, topicId, taskId, previousFilter)
        FocusMode.showOngoing(context, title, startedAt, dndOn = previousFilter != FocusSessionPrefs.FILTER_UNKNOWN)
        FocusMode.scheduleSafetyNet(context)
        return startedAt
    }

    /**
     * Encerra a sessão: devolve o Não Perturbe, tira a notificação e registra o tempo medido.
     * Sessão presa a uma tarefa do plano não vira sessão de estudo aqui — quem registra o tempo
     * dela é a conclusão da tarefa, e contar duas vezes inflaria o histórico.
     */
    suspend fun stop(context: Context, record: Boolean = true): Int {
        val app = context.applicationContext as EstudarioApplication
        val session = app.preferences.focusSession.first()
        if (!session.active) return 0
        val completedAt = System.currentTimeMillis()
        val minutes = session.elapsedMinutes(completedAt)
        FocusMode.restoreDnd(context, session.previousFilter)
        FocusMode.clearOngoing(context)
        FocusMode.cancelSafetyNet(context)
        app.preferences.clearFocusSession(minutes, session.taskId)
        if (record && session.taskId == null && minutes >= MIN_MINUTES_TO_RECORD) {
            app.repository.logStudySession(session.topicId, session.startedAt, completedAt, notes = "Modo foco")
        }
        return minutes
    }

    /**
     * Chamado quando o app abre e pela rede de segurança. Sessão vencida é encerrada e avisada;
     * sessão viva só tem a notificação recolocada, caso a pessoa a tenha dispensado.
     */
    suspend fun reconcile(context: Context) {
        val app = context.applicationContext as EstudarioApplication
        val session = app.preferences.focusSession.first()
        if (!session.active) return
        val minutes = session.elapsedMinutes()
        if (minutes >= FocusMode.MAX_MINUTES) {
            stop(context)
            FocusMode.notifyAutoClosed(context, minutes)
        } else {
            FocusMode.showOngoing(context, session.title, session.startedAt, dndOn = session.previousFilter != FocusSessionPrefs.FILTER_UNKNOWN)
            FocusMode.scheduleSafetyNet(context)
        }
    }
}

/** O botão "Encerrar" da notificação — funciona mesmo com o app fechado. */
class FocusActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != FocusMode.ACTION_STOP) return
        val pending = goAsync()
        val appContext = context.applicationContext
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            try {
                FocusSessionManager.stop(appContext)
            } finally {
                pending.finish()
            }
        }
    }
}

/** Fecha sozinha a sessão que ficou aberta tempo demais, com o app fechado ou não. */
class FocusSafetyNetWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        FocusSessionManager.reconcile(applicationContext)
        return Result.success()
    }
}
