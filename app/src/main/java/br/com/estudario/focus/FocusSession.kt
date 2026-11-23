package br.com.estudario.focus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.FocusSessionEntity
import br.com.estudario.data.local.FocusSessionOrigin
import br.com.estudario.data.preferences.FocusSessionPrefs
import br.com.estudario.domain.FocusRules
import br.com.estudario.domain.FocusRunning
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

    suspend fun start(
        context: Context,
        title: String,
        subjectIds: Set<Long>,
        origin: FocusSessionOrigin,
        topicId: Long?,
        taskId: String?,
    ): Long {
        val app = context.applicationContext as EstudarioApplication
        val atual = app.preferences.focusSession.first()
        if (atual.active) {
            // A mesma coisa de novo (mesmo tópico e tarefa): segue a que está correndo, sem zerar o cronômetro.
            if (atual.topicId == topicId && atual.taskId == taskId && atual.origin == origin) {
                showFor(context, atual)
                return atual.startedAt
            }
            // Outra coisa: a sessão aberta é encerrada e salva antes. Nunca duas sessões ao mesmo tempo.
            stop(context)
        }
        val startedAt = System.currentTimeMillis()
        val wantsDnd = app.preferences.focusDoNotDisturb.first()
        val previousFilter = if (wantsDnd) FocusMode.turnOnDnd(context) else FocusSessionPrefs.FILTER_UNKNOWN
        val resolvedSubjectIds = when {
            subjectIds.isNotEmpty() -> subjectIds
            topicId != null -> app.database.dao().topic(topicId)?.subjectId?.let(::setOf).orEmpty()
            taskId != null -> app.database.plannerDao().task(taskId)?.subjectId?.let(::setOf).orEmpty()
            else -> emptySet()
        }
        // A meta da sessão segue a regra do site (FocusRules): a tarefa do plano manda; foco livre usa o
        // aviso escolhido; num tópico, quem calcula é o plano do dia (AppViewModel.focusPlannedMinutes).
        val taskMinutes = taskId?.let { app.database.plannerDao().task(it)?.plannedMinutes }
        val planned = if (taskMinutes == null && origin != FocusSessionOrigin.LIVRE) null
            else FocusRules.plannedMinutes(taskMinutes, app.preferences.focusPrefs.first())
        app.preferences.startFocusSession(startedAt, title, resolvedSubjectIds, origin, topicId, taskId, previousFilter, planned)
        syncSoon(context)
        FocusMode.showOngoing(context, title, startedAt, dndOn = previousFilter != FocusSessionPrefs.FILTER_UNKNOWN)
        FocusMode.scheduleSafetyNet(context)
        // O widget de Foco acompanha o que acontece no app.
        br.com.estudario.ui.widget.WidgetUpdater.focus(context)
        return startedAt
    }

    /**
     * Encerra a sessão: devolve o Não Perturbe, tira a notificação e registra o tempo medido.
     * Sessão presa a uma tarefa do plano não vira sessão de estudo aqui, quem registra o tempo
     * dela é a conclusão da tarefa, e contar duas vezes inflaria o histórico.
     */
    /**
     * [studiedMinutes] é o tempo que a pessoa disse ter estudado (pergunta do cronômetro esquecido);
     * sem ele, vale a regra de FocusRunning.unattendedMillis: tudo, ou só o confiável se o cronômetro
     * passou do ponto da pergunta sem resposta. [endAt] encerra num momento anterior (a sessão nova
     * começou no outro aparelho); [publishEnd] = false não avisa a conta (a sessão nova já está lá).
     */
    suspend fun stop(context: Context, studiedMinutes: Int? = null, endAt: Long? = null, publishEnd: Boolean = true): Int {
        val app = context.applicationContext as EstudarioApplication
        val session = app.preferences.focusSession.first()
        if (!session.active) return 0
        val running = session.toRunning() ?: return 0
        val completedAt = (endAt ?: System.currentTimeMillis()).coerceAtLeast(session.startedAt)
        // Tempo estudado de verdade: pausas não contam, e cronômetro esquecido não vira horas.
        val studiedMillis = studiedMinutes?.let { (it * 60_000L).coerceIn(0L, running.elapsedMillis(completedAt)) }
            ?: running.unattendedMillis(completedAt)
        val studiedSeconds = studiedMillis / 1_000L
        val minutes = (studiedSeconds / 60L).toInt()
        val history = FocusSessionEntity(
            id = session.sessionId.ifBlank { "legacy-${session.startedAt}" },
            title = session.title.ifBlank { "Sessão de estudo" },
            startedAt = session.startedAt,
            // Tempo cortado (esquecido aberto): o estudo foi no começo, então a linha termina lá.
            completedAt = if (studiedMillis < running.elapsedMillis(completedAt)) session.startedAt + studiedMillis else completedAt,
            durationSeconds = studiedSeconds.coerceAtLeast(0L),
            subjectIdsText = session.subjectIds.filter { it > 0L }.sorted().joinToString(","),
            origin = session.origin,
            topicId = session.topicId,
            taskId = session.taskId,
        )
        // Menos de 1 minuto não entra no histórico (igual no site). O id da linha é o da sessão em
        // andamento, o mesmo que o site usaria: encerrada nos dois, a foto fica com uma linha só.
        val counts = FocusRules.counts(studiedSeconds)
        val ended = running.end(completedAt).takeIf { publishEnd }
        FocusSessionStopCoordinator(
            save = { if (counts) app.focusSessionRepository.recordOnce(it) },
            clearActive = {
                app.preferences.clearFocusSession(minutes, session.taskId, ended)
                FocusMode.restoreDnd(context, session.previousFilter)
                FocusMode.clearOngoing(context)
                FocusMode.cancelSafetyNet(context)
            },
        ).finish(history)
        br.com.estudario.ui.widget.WidgetUpdater.focus(context)
        syncSoon(context)
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
        // Perguntou "Você ainda está estudando?" e ninguém respondeu: encerra salvando só o tempo confiável.
        if (session.toRunning()?.expired(System.currentTimeMillis()) == true) {
            val saved = stop(context)
            FocusMode.notifyAutoClosed(context, minutes, saved)
        } else {
            showFor(context, session)
            FocusMode.scheduleSafetyNet(context)
        }
    }

    /** "Sim, ainda estou estudando": a pergunta só volta mais tarde (e o outro aparelho fica sabendo). */
    suspend fun confirmStillStudying(context: Context) {
        val app = context.applicationContext as EstudarioApplication
        app.preferences.confirmFocusSession(System.currentTimeMillis())
        FocusMode.scheduleSafetyNet(context)
        syncSoon(context)
    }

    /** Pausa o cronômetro (o Não Perturbe continua como está). */
    suspend fun pause(context: Context) {
        val app = context.applicationContext as EstudarioApplication
        app.preferences.pauseFocusSession(System.currentTimeMillis())
        showFor(context, app.preferences.focusSession.first())
        syncSoon(context)
    }

    /** Desconta um tempo parado do foco e atualiza a notificação. */
    suspend fun discount(context: Context, millis: Long) {
        val app = context.applicationContext as EstudarioApplication
        app.preferences.discountFocusSession(millis)
        showFor(context, app.preferences.focusSession.first())
        syncSoon(context)
    }

    suspend fun resume(context: Context) {
        val app = context.applicationContext as EstudarioApplication
        app.preferences.resumeFocusSession(System.currentTimeMillis())
        showFor(context, app.preferences.focusSession.first())
        syncSoon(context)
    }

    /**
     * A sessão que veio da conta (o site ou outro celular). Começada lá, aparece aqui com o mesmo
     * cronômetro (sem ligar o Não Perturbe, que só quem começou liga); pausada ou retomada lá, fica
     * igual aqui; encerrada lá, some daqui sem salvar de novo (a linha do histórico vem na foto).
     */
    suspend fun applyRemote(context: Context, remote: FocusRunning?) {
        if (remote == null) return
        val app = context.applicationContext as EstudarioApplication
        val local = app.preferences.focusSession.first()
        val savedIds = app.database.dao().focusSessionsOnce().mapTo(hashSetOf()) { it.id }
        val lastEnded = app.preferences.focusEnded.first()?.sessionId
        val now = System.currentTimeMillis()
        when (val decision = FocusRunning.reconcile(local.toRunning(), remote, now, { it in savedIds }, lastEnded)) {
            FocusRunning.Remote.Keep -> Unit
            is FocusRunning.Remote.Adopt -> {
                val same = local.active && local.sessionId == decision.running.sessionId
                app.preferences.adoptFocusSession(decision.running, sameSession = same)
                if (!same) FocusMode.scheduleSafetyNet(context)
                showFor(context, app.preferences.focusSession.first())
            }
            is FocusRunning.Remote.Replace -> {
                // Começaram outra sessão no outro aparelho: a daqui é salva até ali e a nova passa a valer.
                stop(context, endAt = decision.endAt, publishEnd = false)
                app.preferences.adoptFocusSession(decision.running, sameSession = false)
                FocusMode.scheduleSafetyNet(context)
                showFor(context, app.preferences.focusSession.first())
            }
            FocusRunning.Remote.EndedElsewhere -> {
                val mine = local.toRunning() ?: return
                app.preferences.dropFocusSession(if (remote.sessionId == mine.sessionId) remote else mine.end(now))
                FocusMode.restoreDnd(context, local.previousFilter)
                FocusMode.clearOngoing(context)
                FocusMode.cancelSafetyNet(context)
                br.com.estudario.ui.widget.WidgetUpdater.focus(context)
            }
        }
    }

    /** O outro aparelho fica sabendo logo (começar, pausar, continuar, encerrar). */
    private fun syncSoon(context: Context) {
        runCatching { br.com.estudario.data.sync.CloudSyncWorker.syncSoon(context.applicationContext, delaySeconds = 2) }
    }

    /** Notificação coerente com o estado: cronômetro com base descontando as pausas, ou "pausado". */
    private fun showFor(context: Context, session: FocusSessionPrefs) {
        br.com.estudario.ui.widget.WidgetUpdater.focus(context)
        if (!session.active) return
        val now = System.currentTimeMillis()
        FocusMode.showOngoing(
            context,
            session.title,
            now - session.elapsedMillis(now),
            dndOn = session.previousFilter != FocusSessionPrefs.FILTER_UNKNOWN,
            paused = session.paused,
        )
    }
}

/** O botão "Encerrar" da notificação, funciona mesmo com o app fechado. */
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
