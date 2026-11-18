package br.com.estudario.web.data

import br.com.estudario.domain.DailyGoal
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.StreakEngine
import br.com.estudario.domain.StreakSummary
import br.com.estudario.domain.planner.PlanTaskType
import kotlinx.browser.localStorage
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull

/** Preferências deste navegador (no app ficam em Ajustes). */
object Prefs {
    private fun read(key: String): String? = runCatching { localStorage.getItem(key) }.getOrNull()
    private fun write(key: String, value: String) { runCatching { localStorage.setItem(key, value) } }

    /**
     * Meta diária de questões (a sequência conta o dia com essa meta, uma tarefa do plano ou uma revisão).
     * Vai na foto da conta (chave "userPrefs", a mesma do app, SyncedPrefs.kt) com o histórico das
     * mudanças: com metas diferentes, o mesmo dia fechava aqui e não no celular, que gastava o
     * marca-página num dia estudado. Vale a meta mudada por último, em qualquer aparelho.
     */
    var dailyGoal: Int
        get() = goal().questions
        set(value) {
            val next = value.coerceIn(DailyGoal.MIN, DailyGoal.MAX)
            val old = goal()
            val today = Queries.todayEpoch()
            val history = (old.history.ifEmpty { listOf(0L to old.questions) }.filter { it.first != today } + (today to next)).sortedBy { it.first }
            val at = kotlin.js.Date.now().toLong()
            write("estudario.goal", next.toString())
            write("estudario.goal.updatedAt", at.toString())
            Store.update { it.withObject(SYNCED_KEY, synced(it).withGoal(next, history.joinToString(",") { (d, q) -> "$d:$q" }, at)) }
        }

    /** A meta com o histórico: a da conta, se mudou depois da deste navegador. */
    fun goal(): DailyGoal {
        val cloud = Store.data.obj(SYNCED_KEY)
        val cloudAt = cloud?.long("goalUpdatedAt") ?: 0L
        val localAt = read("estudario.goal.updatedAt")?.toLongOrNull() ?: 0L
        if (cloud != null && cloud.long("dailyGoal") != null && cloudAt >= localAt) {
            val history = (cloud["goalHistory"] as? JsonPrimitive)?.contentOrNull.orEmpty().split(',').mapNotNull { entry ->
                val parts = entry.split(':')
                val day = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
                val q = parts.getOrNull(1)?.toIntOrNull() ?: return@mapNotNull null
                day to q
            }.sortedBy { it.first }
            return DailyGoal(cloud.long("dailyGoal")!!.toInt().coerceIn(DailyGoal.MIN, DailyGoal.MAX), history)
        }
        return DailyGoal(read("estudario.goal")?.toIntOrNull()?.coerceIn(DailyGoal.MIN, DailyGoal.MAX) ?: DailyGoal.DEFAULT.questions)
    }

    /** Chave da foto com a meta e as peças vistas do armário (a mesma do app). */
    const val SYNCED_KEY = "userPrefs"

    fun synced(data: Snapshot): JsonObject = data.obj(SYNCED_KEY) ?: JsonObject(emptyMap())

    /**
     * Uma lista do que já foi visto ou comemorado em qualquer aparelho da conta ("wardrobeSeen",
     * "celebratedPieces", "celebratedBadges"; as mesmas do app). Só cresce.
     */
    fun syncedSet(name: String): Set<String> = (synced(Store.data)[name] as? kotlinx.serialization.json.JsonArray)
        ?.mapNotNull { (it as? JsonPrimitive)?.contentOrNull?.takeIf(String::isNotBlank) }?.toSet().orEmpty()

    /** Soma [values] à lista da conta (o app recebe na próxima sincronização). */
    fun addSynced(name: String, values: Set<String>) {
        if (syncedSet(name).containsAll(values)) return
        // A mudança é refeita por cima da versão da conta quando outro aparelho publicou antes
        // (Store): por isso soma ao que está lá em vez de gravar a lista daqui.
        Store.update { data -> withSynced(data, name, values) }
    }

    /** [data] com [values] somados à lista [name] da conta (união: nada some). */
    fun withSynced(data: Snapshot, name: String, values: Set<String>): Snapshot {
        val current = (synced(data)[name] as? kotlinx.serialization.json.JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.contentOrNull }.orEmpty()
        val list = kotlinx.serialization.json.JsonArray((current + values).filter { it.isNotBlank() }.distinct().sorted().map { JsonPrimitive(it) })
        return data.withObject(SYNCED_KEY, JsonObject(synced(data) + (name to list)))
    }

    private fun syncedSetIn(data: Snapshot, name: String): Set<String> = (synced(data)[name] as? kotlinx.serialization.json.JsonArray)
        ?.mapNotNull { (it as? JsonPrimitive)?.contentOrNull?.takeIf(String::isNotBlank) }?.toSet().orEmpty()

    /**
     * As animações já vistas em qualquer aparelho da conta (chaves de [br.com.estudario.domain.Celebrated],
     * as mesmas do app), contando as listas antigas de emblemas e peças.
     */
    fun celebratedIn(data: Snapshot): Set<String> = br.com.estudario.domain.Celebrated.all(
        syncedSetIn(data, br.com.estudario.domain.Celebrated.SYNCED),
        syncedSetIn(data, br.com.estudario.domain.Celebrated.LEGACY_BADGES),
        syncedSetIn(data, br.com.estudario.domain.Celebrated.LEGACY_PIECES),
    )

    fun wasCelebrated(key: String): Boolean = key in celebratedIn(Store.data)

    /** Grava que a animação apareceu (o app recebe na próxima sincronização e não repete). */
    fun markCelebrated(keys: Collection<String>) {
        if (Store.demo || keys.isEmpty()) return
        addSynced(br.com.estudario.domain.Celebrated.SYNCED, keys.toSet())
    }

    /** Grava campos soltos na foto da conta (ex.: o fundo do caderno), sem mexer no resto. */
    fun setSyncedFields(fields: Map<String, kotlinx.serialization.json.JsonElement>) {
        Store.update { data -> data.withObject(SYNCED_KEY, JsonObject(synced(data) + fields)) }
    }

    /** Limite do nome escolhido (o mesmo do app, NameDialog.kt). */
    const val NAME_MAX = 30

    /** O nome como fica guardado: sem espaços sobrando e dentro do limite. */
    fun cleanName(raw: String): String = raw.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.joinToString(" ").take(NAME_MAX)

    /**
     * Como a pessoa quer ser chamada: o nome escolhido no app ou no site ("userName" na foto da conta,
     * o mesmo de SyncedPrefs.kt do app) ou, sem escolha, o da conta Google.
     */
    fun userName(data: Snapshot = Store.data): String? =
        (synced(data)["userName"] as? JsonPrimitive)?.contentOrNull?.trim()?.takeIf { it.isNotBlank() }
            ?: Store.session?.name?.takeIf { it.isNotBlank() }

    /** Grava o nome escolhido na foto da conta (o app adota na próxima sincronização). */
    fun setUserName(raw: String) {
        val name = cleanName(raw)
        if (name.isEmpty()) return
        setSyncedFields(mapOf("userName" to JsonPrimitive(name), "userNameUpdatedAt" to JsonPrimitive(kotlin.js.Date.now().toLong())))
    }

    private fun JsonObject.long(key: String): Long? = (this[key] as? JsonPrimitive)?.longOrNull
    private fun JsonObject.withGoal(questions: Int, history: String, at: Long) =
        JsonObject(this + mapOf("dailyGoal" to JsonPrimitive(questions), "goalHistory" to JsonPrimitive(history), "goalUpdatedAt" to JsonPrimitive(at)))

    /** Mostrar a explicação logo após responder (como no app). */
    var explanationRightAway: Boolean
        get() = read("estudario.explain") != "0"
        set(value) { write("estudario.explain", if (value) "1" else "0"); AccountSettings.changed(br.com.estudario.domain.SyncedSettings.EXPLAIN_RIGHT_AWAY, br.com.estudario.domain.SyncedSettings.flag(value)) }

    /** O relógio no placar das questões (Ajustes). */
    var hudTimer: Boolean
        get() = read("estudario.hud.tempo") != "0"
        set(value) { write("estudario.hud.tempo", if (value) "1" else "0"); AccountSettings.changed(br.com.estudario.domain.SyncedSettings.HUD_TIMER, br.com.estudario.domain.SyncedSettings.flag(value)) }

    /** As certas e erradas no placar das questões (Ajustes). */
    var hudScore: Boolean
        get() = read("estudario.hud.placar") != "0"
        set(value) { write("estudario.hud.placar", if (value) "1" else "0"); AccountSettings.changed(br.com.estudario.domain.SyncedSettings.HUD_SCORE, br.com.estudario.domain.SyncedSettings.flag(value)) }

    /**
     * Aviso de tempo e contagem escondida do modo foco, os mesmos do app (campos de "userPrefs",
     * domain/FocusSync.kt): vale o mudado por último em qualquer aparelho. Sem nada na conta ainda,
     * valem os antigos deste navegador.
     */
    fun focusPrefs(data: Snapshot = Store.data): br.com.estudario.domain.FocusPrefs =
        br.com.estudario.domain.FocusPrefs.from(getter(synced(data))) ?: br.com.estudario.domain.FocusPrefs(
            minutes = br.com.estudario.domain.FocusRules.clampMinutes(read("estudario.focus")?.toIntOrNull() ?: br.com.estudario.domain.FocusRules.DEFAULT_MINUTES),
            clockHidden = read("estudario.focusbar.hidden") == "1",
        )

    fun setFocusPrefs(value: br.com.estudario.domain.FocusPrefs) {
        if (Store.demo) return
        setSyncedFields(fieldsJson(value.copy(updatedAt = kotlin.js.Date.now().toLong()).fields()))
    }

    var focusMinutes: Int
        get() = focusPrefs().minutes
        set(value) = setFocusPrefs(focusPrefs().copy(minutes = value, alertOn = true))

    /** Lê um bloco JSON da foto no formato que as regras de shared/ entendem. */
    fun getter(o: JsonObject?): (String) -> Any? = { key ->
        when (val v = o?.get(key)) {
            is JsonPrimitive -> when {
                v.isString -> v.content
                v.content == "true" -> true
                v.content == "false" -> false
                else -> v.longOrNull ?: v.content.toDoubleOrNull()
            }
            is JsonObject -> v
            else -> null
        }
    }

    fun fieldsJson(fields: Map<String, Any?>): Map<String, kotlinx.serialization.json.JsonElement> = fields.mapValues { (_, v) ->
        when (v) {
            null -> kotlinx.serialization.json.JsonNull
            is String -> JsonPrimitive(v)
            is Boolean -> JsonPrimitive(v)
            is Int -> JsonPrimitive(v)
            is Long -> JsonPrimitive(v)
            is Number -> JsonPrimitive(v.toDouble())
            else -> JsonPrimitive(v.toString())
        }
    }
}

data class ProgressView(
    val streak: StreakSummary,
    val progress: ProgressEngine.ProgressSummary,
)

/** XP, nível, sequência e emblemas: os mesmos ProgressEngine/StreakEngine do app, calculados uma vez por foto. */
object Progress {
    private var lastData: Snapshot? = null
    private var lastGoal: DailyGoal? = null
    private var last: ProgressView? = null

    fun of(data: Snapshot): ProgressView {
        val goal = Prefs.goal()
        last?.let { if (lastData === data && lastGoal == goal) return it }
        // A mesma montagem do app (ProgressInputs, no shared): mesmas linhas da foto, mesmo resultado.
        val result = br.com.estudario.domain.ProgressInputs.evaluate(sources(data), goal, Queries.todayDate(), Queries.zone)
        val streak = result.streak
        val progress = result.progress
        // Armário por área: a área do edital principal e as peças que a conta já ganhou. As que acabaram
        // de sair por dias estudados vão para a lista da conta (fora deste cálculo, para não refazer a foto
        // no meio dele), e ficam mesmo se o edital mudar.
        br.com.estudario.domain.FolhaWardrobe.homeArea = homeArea(data)
        br.com.estudario.domain.FolhaWardrobe.owned = (Prefs.synced(data)["wardrobeEarned"] as? kotlinx.serialization.json.JsonArray)
            ?.mapNotNull { (it as? JsonPrimitive)?.contentOrNull }?.toSet().orEmpty()
        val keep = br.com.estudario.domain.FolhaWardrobe.toKeep(progress)
        if (keep.isNotEmpty() && !Store.demo && Store.session != null) kotlinx.browser.window.setTimeout({ Prefs.addSynced("wardrobeEarned", keep) }, 0)
        return ProgressView(streak, progress).also { lastData = data; lastGoal = goal; last = it }
    }

    /** As linhas da foto no formato comum (ProgressInputs), as mesmas que o app passa. */
    fun sources(data: Snapshot): br.com.estudario.domain.ProgressInputs.Sources {
        val primary = Queries.primaryCompetition(data)
        return br.com.estudario.domain.ProgressInputs.Sources(
            attempts = data.attempts.map { br.com.estudario.domain.ProgressInputs.Attempt(it.answeredAt, it.correct) },
            reviews = data.reviewHistory.map { br.com.estudario.domain.ProgressInputs.Review(it.reviewedAt) },
            sessions = data.sessions.map { br.com.estudario.domain.ProgressInputs.Session(it.id, it.topicId, it.startedAt, it.completedAt) },
            focus = data.focusSessions.map { br.com.estudario.domain.ProgressInputs.Focus(it.startedAt, it.completedAt, it.durationSeconds, it.origin == "LIVRE") },
            executions = data.executions.map { br.com.estudario.domain.ProgressInputs.Execution(it.taskId, it.completedAt, it.minutes, it.questions, it.correct) },
            questionRuns = data.questionSessions.map { br.com.estudario.domain.ProgressInputs.QuestionRun(it.startedAt, it.completedAt, it.questionCount) },
            taskTypes = data.tasks.mapNotNull { t -> runCatching { PlanTaskType.valueOf(t.type) }.getOrNull()?.let { t.id to it } }.toMap(),
            topics = data.topics.map { br.com.estudario.domain.ProgressInputs.TopicRow(it.id, it.subjectId, it.parentTopicId, Queries.isStudied(it)) },
            primarySubjectIds = data.subjects.filter { primary != null && it.competitionId == primary.id }.mapTo(hashSetOf()) { it.id },
            marks = data.theoryMarks.size + data.notes.size,
            errorsFixed = data.errors.count { it.retryCorrectCount > 0 },
            flashDecks = (Prefs.synced(data)["flashDecks"] as? kotlinx.serialization.json.JsonArray)?.size ?: 0,
            retiredPlanXp = (Prefs.synced(data)["retiredPlanXp"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull() ?: 0,
        )
    }

    /** A área do edital principal (o nome do concurso e o cargo dos planos dele). */
    fun homeArea(data: Snapshot): br.com.estudario.domain.FolhaArea? {
        val c = Queries.primaryCompetition(data) ?: return null
        val role = data.plans.filter { it.competitionId == c.id }.joinToString(" ") { it.objective + " " + it.name }
        return br.com.estudario.domain.FolhaArea.of(c.name, c.name, "${c.name} $role")
    }

    /** XP que uma tarefa do plano rende (mostrado na tarefa, como no app). */
    fun taskXp(task: PlanTask): Int {
        val type = runCatching { PlanTaskType.valueOf(task.type) }.getOrNull() ?: return 0
        return ProgressEngine.previewPlanTask(type, task.minutes, task.questions).base
    }

}
