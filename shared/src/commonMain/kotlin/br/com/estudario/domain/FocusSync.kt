package br.com.estudario.domain

/**
 * O modo foco é um só no app e no site: as mesmas regras, as mesmas preferências e a mesma sessão em
 * andamento. As duas plataformas guardam isto dentro de "userPrefs" da foto da conta (SyncedPrefs.kt
 * no app, Prefs em web/.../data/Progress.kt no site), como campos simples:
 *
 * - "focusMinutes", "focusAlert", "focusClockHidden", "focusPrefsUpdatedAt": o aviso de tempo e a
 *   contagem escondida; vale o mudado por último;
 * - "focusRunning": a sessão em andamento (ou a marca de que ela acabou), para o outro aparelho
 *   mostrar o mesmo cronômetro, pausar, continuar ou encerrar. Vale a mexida por último.
 *
 * A sessão encerrada vira uma linha de "focusSessions" com o MESMO id da sessão em andamento: se os
 * dois aparelhos chegarem a salvar, a junção da foto (por id) deixa uma linha só.
 */
object FocusRules {
    const val DEFAULT_MINUTES = 50
    const val MIN_MINUTES = 5
    const val MAX_MINUTES = 180
    const val STEP_MINUTES = 5
    val PRESETS = listOf(25, 50, 90)

    /** Teto de uma sessão: passou disso, foi esquecida aberta e é encerrada sozinha. */
    const val MAX_SESSION_MINUTES = 240

    /** Menos que isso não entra no histórico (nem no app nem no site). */
    const val MIN_SAVED_SECONDS = 60L

    const val DEFAULT_TITLE = "Sessão de estudo"

    const val KEY_RUNNING = "focusRunning"
    const val KEY_MINUTES = "focusMinutes"
    const val KEY_ALERT = "focusAlert"
    const val KEY_CLOCK_HIDDEN = "focusClockHidden"
    const val KEY_PREFS_AT = "focusPrefsUpdatedAt"

    fun clampMinutes(minutes: Int): Int = minutes.coerceIn(MIN_MINUTES, MAX_MINUTES)

    /** A origem gravada no histórico (os nomes de FocusSessionOrigin do app). */
    fun origin(taskId: String?, topicId: Long?): String = when {
        !taskId.isNullOrBlank() -> "PLANO"
        topicId != null && topicId > 0L -> "MATERIA"
        else -> "LIVRE"
    }

    fun counts(studiedSeconds: Long): Boolean = studiedSeconds >= MIN_SAVED_SECONDS

    /**
     * A meta (aviso) da sessão: a tarefa do plano manda; sem tarefa, o aviso escolhido nas
     * preferências, se estiver ligado.
     */
    fun plannedMinutes(taskMinutes: Int?, prefs: FocusPrefs): Int? =
        taskMinutes?.takeIf { it > 0 } ?: if (prefs.alertOn) clampMinutes(prefs.minutes) else null
}

/** Preferências do foco, as mesmas nos dois aparelhos. */
data class FocusPrefs(
    val minutes: Int = FocusRules.DEFAULT_MINUTES,
    val alertOn: Boolean = true,
    val clockHidden: Boolean = false,
    val updatedAt: Long = 0L,
) {
    fun fields(): Map<String, Any?> = mapOf(
        FocusRules.KEY_MINUTES to FocusRules.clampMinutes(minutes),
        FocusRules.KEY_ALERT to alertOn,
        FocusRules.KEY_CLOCK_HIDDEN to clockHidden,
        FocusRules.KEY_PREFS_AT to updatedAt,
    )

    companion object {
        /** Lê de "userPrefs"; null se a conta ainda não tem preferência de foco. */
        fun from(get: (String) -> Any?): FocusPrefs? {
            val at = (get(FocusRules.KEY_PREFS_AT) as? Number)?.toLong() ?: return null
            return FocusPrefs(
                minutes = FocusRules.clampMinutes((get(FocusRules.KEY_MINUTES) as? Number)?.toInt() ?: FocusRules.DEFAULT_MINUTES),
                alertOn = get(FocusRules.KEY_ALERT) as? Boolean ?: true,
                clockHidden = get(FocusRules.KEY_CLOCK_HIDDEN) as? Boolean ?: false,
                updatedAt = at,
            )
        }

        fun newer(a: FocusPrefs?, b: FocusPrefs?): FocusPrefs? = when {
            a == null -> b
            b == null -> a
            b.updatedAt > a.updatedAt -> b
            else -> a
        }
    }
}

/**
 * A sessão em andamento, igual nos dois aparelhos. [endedAt] > 0 é a marca de que ela acabou (o
 * outro aparelho para de mostrá-la sem salvar de novo). [updatedAt] é a última mexida (começar,
 * pausar, continuar, descontar, encerrar): entre dois aparelhos, a mais nova vale.
 */
data class FocusRunning(
    val sessionId: String,
    val startedAt: Long,
    val title: String = FocusRules.DEFAULT_TITLE,
    val subjectIds: List<Long> = emptyList(),
    val origin: String = "LIVRE",
    val topicId: Long? = null,
    val taskId: String? = null,
    val plannedMinutes: Int? = null,
    /** Momento em que foi pausada (0 = contando). */
    val pausedAt: Long = 0L,
    /** Soma das pausas já encerradas. */
    val pausedMillis: Long = 0L,
    val updatedAt: Long = startedAt,
    val endedAt: Long = 0L,
) {
    val active: Boolean get() = startedAt > 0L && endedAt == 0L && sessionId.isNotBlank()
    val paused: Boolean get() = active && pausedAt > 0L

    fun elapsedMillis(now: Long): Long =
        if (startedAt <= 0L) 0L else ((if (pausedAt > 0L) pausedAt else if (endedAt > 0L) endedAt else now) - startedAt - pausedMillis).coerceAtLeast(0L)

    fun elapsedMinutes(now: Long): Int = (elapsedMillis(now) / 60_000L).toInt()

    /** Esquecida aberta além do teto. */
    fun expired(now: Long): Boolean = active && elapsedMinutes(now) >= FocusRules.MAX_SESSION_MINUTES

    fun pause(at: Long): FocusRunning = if (!active || paused) this else copy(pausedAt = at, updatedAt = at)
    fun resume(at: Long): FocusRunning = if (!paused) this else copy(pausedMillis = pausedMillis + (at - pausedAt).coerceAtLeast(0L), pausedAt = 0L, updatedAt = at)
    fun discount(millis: Long, at: Long): FocusRunning = if (!active || millis <= 0L) this else copy(pausedMillis = pausedMillis + millis.coerceAtMost(elapsedMillis(at)), updatedAt = at)
    fun end(at: Long): FocusRunning = copy(endedAt = at, updatedAt = at)

    fun fields(): Map<String, Any?> = mapOf(
        "sessionId" to sessionId,
        "startedAt" to startedAt,
        "title" to title,
        "subjectIds" to subjectIds.filter { it > 0L }.sorted().joinToString(","),
        "origin" to origin,
        "topicId" to topicId,
        "taskId" to taskId,
        "plannedMinutes" to plannedMinutes,
        "pausedAt" to pausedAt,
        "pausedMillis" to pausedMillis,
        "updatedAt" to updatedAt,
        "endedAt" to endedAt,
    )

    /** O que fazer aqui com a sessão que veio da conta. */
    sealed interface Remote {
        /** Nada muda neste aparelho. */
        data object Keep : Remote
        /** Mostrar (ou atualizar) a sessão da conta aqui. */
        data class Adopt(val running: FocusRunning) : Remote
        /** A sessão daqui foi encerrada no outro aparelho: some sem salvar de novo. */
        data object EndedElsewhere : Remote
    }

    companion object {
        fun from(get: (String) -> Any?): FocusRunning? {
            val id = (get("sessionId") as? String)?.takeIf { it.isNotBlank() } ?: return null
            val started = (get("startedAt") as? Number)?.toLong()?.takeIf { it > 0L } ?: return null
            return FocusRunning(
                sessionId = id,
                startedAt = started,
                title = (get("title") as? String)?.takeIf { it.isNotBlank() } ?: FocusRules.DEFAULT_TITLE,
                subjectIds = (get("subjectIds") as? String).orEmpty().split(',').mapNotNull { it.trim().toLongOrNull() }.filter { it > 0L },
                origin = (get("origin") as? String)?.takeIf { it in setOf("LIVRE", "MATERIA", "PLANO") } ?: "LIVRE",
                topicId = (get("topicId") as? Number)?.toLong()?.takeIf { it > 0L },
                taskId = (get("taskId") as? String)?.takeIf { it.isNotBlank() },
                plannedMinutes = (get("plannedMinutes") as? Number)?.toInt()?.takeIf { it > 0 },
                pausedAt = (get("pausedAt") as? Number)?.toLong() ?: 0L,
                pausedMillis = (get("pausedMillis") as? Number)?.toLong() ?: 0L,
                updatedAt = (get("updatedAt") as? Number)?.toLong() ?: started,
                endedAt = (get("endedAt") as? Number)?.toLong() ?: 0L,
            )
        }

        /** Entre duas fotos: vale a mexida mais nova; empate, a marca de encerrada. */
        fun newer(a: FocusRunning?, b: FocusRunning?): FocusRunning? = when {
            a == null -> b
            b == null -> a
            b.updatedAt > a.updatedAt -> b
            b.updatedAt == a.updatedAt && b.endedAt > 0L && a.endedAt == 0L -> b
            else -> a
        }

        /**
         * [local] é a sessão deste aparelho (ou null), [remote] a da conta, [saved] diz se uma sessão
         * já está no histórico (encerrada em algum lugar), [lastEndedHere] o id da última encerrada
         * aqui (não ressuscita).
         */
        fun reconcile(local: FocusRunning?, remote: FocusRunning?, now: Long, saved: (String) -> Boolean, lastEndedHere: String? = null): Remote {
            if (remote == null) return Remote.Keep
            val mine = local?.takeIf { it.active }
            if (mine != null && remote.sessionId == mine.sessionId) {
                if (remote.updatedAt <= mine.updatedAt) return Remote.Keep
                return if (remote.active) Remote.Adopt(remote) else Remote.EndedElsewhere
            }
            if (mine != null) {
                // Duas sessões diferentes: a daqui foi salva em outro lugar, então some; senão fica.
                return if (saved(mine.sessionId)) Remote.EndedElsewhere else Remote.Keep
            }
            if (!remote.active || remote.expired(now) || saved(remote.sessionId) || remote.sessionId == lastEndedHere) return Remote.Keep
            return Remote.Adopt(remote)
        }
    }
}
