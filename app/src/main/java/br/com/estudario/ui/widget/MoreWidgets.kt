package br.com.estudario.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import br.com.estudario.EstudarioApplication
import br.com.estudario.R
import br.com.estudario.data.local.FocusSessionOrigin
import br.com.estudario.ui.brand.Glyph
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/*
 * Mais widgets, no mesmo desenho do app (PlanWidgets): cada um com um papel só.
 * - Revisões de hoje: o que vence hoje (e o que atrasou); tocar abre a revisão.
 * - Questão do dia: respondida no próprio widget; depois dá para pedir outra.
 * - Foco: começar, pausar e retomar o cronômetro sem abrir o app.
 * - Constância: o mapa das últimas semanas de estudo e a sequência.
 * Mesma regra dos outros: no máximo 10 filhos por linha ou coluna.
 */

// --------------------------------------------------------------------------- Revisões de hoje

class ReviewsWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as EstudarioApplication
        val zone = ZoneId.systemDefault()
        val start = LocalDate.now().atStartOfDay(zone).toInstant().toEpochMilli()
        val end = LocalDate.now().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val dao = app.database.dao()
        val all = dao.reviewsOnce()
        val pending = all.filter { it.completedAt == null && it.ignoredAt == null && it.dueAt < end }
        val doneToday = all.count { (it.completedAt ?: 0L) >= start }
        val late = pending.count { it.dueAt < start }
        val topics = dao.topicsOnce().associateBy { it.id }
        val rows = pending.sortedBy { it.dueAt }.take(3)
        provideContent {
            WidgetFrame(null, center = rows.isEmpty()) {
                Header(
                    Glyph.Cycle, "REVISÕES DE HOJE",
                    when {
                        pending.isEmpty() && doneToday > 0 -> "Tudo revisado!"
                        pending.isEmpty() -> "Nada para revisar"
                        pending.size == 1 -> "1 revisão"
                        else -> "${pending.size} revisões"
                    },
                    eyebrowColor = WidgetLook.coral, action = openApp(context, "reviews", "reviews-head"),
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("$doneToday feitas", style = TextStyle(fontSize = fs(14f), fontWeight = FontWeight.Bold, color = WidgetLook.mint))
                        if (late > 0) Text("$late atrasada${if (late == 1) "" else "s"}", style = WidgetLook.body(WidgetLook.coral, 12))
                    }
                }
                Column(GlanceModifier.fillMaxWidth().padding(top = 12.dp)) {
                    if (rows.isEmpty()) Row(GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        FolhaFace(context, if (doneToday > 0) br.com.estudario.ui.assistant.FolhaMood.PROUD else br.com.estudario.ui.assistant.FolhaMood.HAPPY, 60)
                        Text(
                            if (doneToday > 0) "Memória em dia! A próxima rodada aparece aqui quando vencer." else "Quando um tópico estudado precisar de revisão, ele aparece aqui.",
                            maxLines = 3, style = WidgetLook.body(WidgetLook.white, 14), modifier = GlanceModifier.defaultWeight().padding(start = 12.dp),
                        )
                    }
                    rows.forEach { review ->
                        val isLate = review.dueAt < start
                        Box(GlanceModifier.fillMaxWidth().padding(bottom = 6.dp)) {
                            Row(
                                GlanceModifier.fillMaxWidth().background(WidgetLook.card).padding(start = 10.dp, top = 8.dp, end = 13.dp, bottom = 10.dp)
                                    .clickable(openApp(context, "review-session/${review.id}", "review-${review.id}")),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(GlanceModifier.size(30.dp).cornerRadius(15.dp).background(if (isLate) ColorProvider(Color(0xFFFF8A80)) else WidgetLook.review), contentAlignment = Alignment.Center) {
                                    Text("R${review.stage}", style = TextStyle(fontSize = fs(11f), fontWeight = FontWeight.Bold, color = WidgetLook.ink, textAlign = TextAlign.Center))
                                }
                                Column(GlanceModifier.defaultWeight().padding(start = 10.dp)) {
                                    Text(topics[review.topicId]?.title?.substringBefore(" (")?.trimEnd('.') ?: "Revisão", maxLines = 1, style = TextStyle(fontSize = fs(15f), fontWeight = FontWeight.Bold, color = WidgetLook.white))
                                    Text(if (isLate) "atrasada · toque para revisar" else "${review.stage}ª revisão · hoje", style = WidgetLook.body(if (isLate) WidgetLook.coral else WidgetLook.mute, 13))
                                }
                            }
                        }
                    }
                    if (pending.size > rows.size) Text("+ ${pending.size - rows.size} na lista", style = WidgetLook.body(WidgetLook.faint, 12), modifier = GlanceModifier.clickable(openApp(context, "reviews", "reviews-more")))
                }
            }
        }
    }
}

class ReviewsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ReviewsWidget()
}

// ---------------------------------------------------------------------------- Questão do dia

/** A questão do dia fica guardada (uma por dia) com a resposta dada no widget. */
internal object DailyQuestion {
    private const val PREFS = "widget_questao_do_dia"
    fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /** A resposta de hoje, observada pelo widget para redesenhar assim que a pessoa toca. */
    val answered = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)

    /** Tira a marcação do texto (negrito, títulos, código): o widget só mostra texto simples. */
    fun plain(text: String) = text.replace(Regex("[*_`#>]+"), "").replace(Regex("\\s+"), " ").trim()

    /**
     * Só algumas candidatas vêm do banco (curtas, de tópicos estudados e menos dominadas primeiro): ler
     * o banco inteiro, com milhares de questões, fazia o widget ficar carregando para sempre.
     */
    suspend fun pick(context: Context, skip: Boolean = false): br.com.estudario.data.local.QuestionWithOptions? {
        val app = context.applicationContext as EstudarioApplication
        val today = LocalDate.now().toEpochDay()
        val p = prefs(context)
        val dao = app.database.dao()
        if (!skip && p.getLong("day", -1) == today) dao.question(p.getLong("id", -1))?.let { return it }
        val pool = dao.widgetQuestionCandidates().filter { q ->
            q.options.size in 2..5 && q.options.any { it.isCorrect } && q.options.all { plain(it.text).length <= 180 }
        }
        if (pool.isEmpty()) return null
        val turn = if (p.getLong("day", -1) == today) p.getInt("turn", 0) + (if (skip) 1 else 0) else 0
        val chosen = pool[(((today * 2654435761L) + turn * 7919L) % pool.size).toInt().let { if (it < 0) it + pool.size else it }]
        p.edit().putLong("day", today).putLong("id", chosen.question.id).putInt("turn", turn).remove("answer").apply()
        return chosen
    }
}

private val AnswerKey = ActionParameters.Key<String>("resposta")

/** Resposta tocada no widget: grava como uma resposta normal (conta no Desempenho e no caderno de erros). */
class AnswerDailyQuestion : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val key = parameters[AnswerKey] ?: return
        val p = DailyQuestion.prefs(context)
        if (p.getString("answer", null) != null) return
        val app = context.applicationContext as EstudarioApplication
        val question = app.database.dao().question(p.getLong("id", -1)) ?: return
        runCatching { app.repository.answer(question, key, sessionId = "widget-${LocalDate.now()}") }
        p.edit().putString("answer", key).apply()
        DailyQuestion.answered.value = key
        runCatching { StreakWidget().updateAll(context) }
    }
}

/** Respondeu e quer mais: outra questão no lugar, sem abrir o app. */
class NextDailyQuestion : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        DailyQuestion.pick(context, skip = true)
        DailyQuestion.answered.value = null
        QuestionWidget().update(context, glanceId)
    }
}

class QuestionWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val question = DailyQuestion.pick(context)
        DailyQuestion.answered.value = DailyQuestion.prefs(context).getString("answer", null)
        val trueFalse = question?.options?.map { it.key }?.toSet() == setOf("C", "E")
        provideContent {
            val answer by DailyQuestion.answered.collectAsState()
            if (question == null) { EmptyState(context, "QUESTÃO DO DIA", "Salve questões no Estudário e uma delas aparece aqui todo dia."); return@provideContent }
            val correct = question.options.firstOrNull { it.isCorrect }?.key
            WidgetFrame(null) {
                Header(Glyph.Bulb, "QUESTÃO DO DIA", if (answer == null) "Sua vez!" else if (answer == correct) "Acertou!" else "Quase! Veja a certa", eyebrowColor = WidgetLook.blue) {
                    if (answer != null) Text(if (answer == correct) "✓" else "✕", style = TextStyle(fontSize = fs(24f), fontWeight = FontWeight.Bold, color = if (answer == correct) WidgetLook.mint else WidgetLook.coral))
                }
                Text(
                    DailyQuestion.plain(question.question.statement), maxLines = if (answer == null) 5 else 3,
                    style = TextStyle(fontSize = fs(14f), color = WidgetLook.white),
                    modifier = GlanceModifier.padding(top = 10.dp, bottom = 8.dp),
                )
                Column(GlanceModifier.fillMaxWidth()) {
                    // Respondida: ficam só a certa e a marcada, para caber a explicação e o "outra questão".
                    question.options.sortedBy { it.position }.filter { answer == null || it.key == correct || it.key == answer }.forEach { option ->
                        val bg = when {
                            answer == null -> R.drawable.widget_option
                            option.key == correct -> R.drawable.widget_option_ok
                            option.key == answer -> R.drawable.widget_option_bad
                            else -> R.drawable.widget_option
                        }
                        val label = if (trueFalse) (if (option.key == "C") "Certo" else "Errado") else DailyQuestion.plain(option.text)
                        val base = GlanceModifier.fillMaxWidth().background(ImageProvider(bg)).padding(start = 10.dp, top = 7.dp, end = 12.dp, bottom = 9.dp)
                        Box(GlanceModifier.fillMaxWidth().padding(bottom = 5.dp)) {
                            Row(
                                if (answer == null) base.clickable(actionRunCallback<AnswerDailyQuestion>(actionParametersOf(AnswerKey to option.key))) else base,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (!trueFalse) Text(option.key, style = TextStyle(fontSize = fs(15f), fontWeight = FontWeight.Bold, color = WidgetLook.gold), modifier = GlanceModifier.padding(end = 8.dp))
                                Text(label, maxLines = if (answer == null) 2 else 4, style = TextStyle(fontSize = fs(13f), color = WidgetLook.white), modifier = GlanceModifier.defaultWeight())
                            }
                        }
                    }
                    if (answer != null) Row(GlanceModifier.fillMaxWidth().padding(top = 4.dp)) {
                        StickerButton("Explicação", openApp(context, "topic/${question.question.topicId}", "qod-${question.question.id}"), primary = false, modifier = GlanceModifier.defaultWeight().padding(end = 6.dp))
                        StickerButton("Outra questão", actionRunCallback<NextDailyQuestion>(), primary = true, modifier = GlanceModifier.defaultWeight())
                    }
                }
            }
        }
    }
}

class QuestionWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = QuestionWidget()
}

// ------------------------------------------------------------------------------------- Foco

internal val FocusTaskKey = ActionParameters.Key<String>("tarefa")

/** Começa o foco (numa tarefa do plano, ou livre), sem abrir o app. */
class StartFocusAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val app = context.applicationContext as EstudarioApplication
        val task = parameters[FocusTaskKey]?.let { app.database.plannerDao().task(it) }
        runCatching {
            br.com.estudario.focus.FocusSessionManager.start(
                context,
                task?.let { "${it.subjectNameSnapshot} - ${it.topicNameSnapshot ?: "Estudo"}" } ?: "Sessão de estudo",
                emptySet(),
                if (task != null) FocusSessionOrigin.PLANO else FocusSessionOrigin.LIVRE,
                task?.topicId, task?.id,
            )
        }.onFailure { android.util.Log.w("FocusWidget", "não deu para começar o foco", it) }
        FocusWidget().updateAll(context)
    }
}

class ToggleFocusAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val app = context.applicationContext as EstudarioApplication
        val session = app.preferences.focusSession.first()
        runCatching {
            if (session.pausedAt > 0L) br.com.estudario.focus.FocusSessionManager.resume(context)
            else br.com.estudario.focus.FocusSessionManager.pause(context)
        }
        FocusWidget().updateAll(context)
    }
}

class FocusWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as EstudarioApplication
        val initial = app.preferences.focusSession.first()
        val today = LocalDate.now().toEpochDay()
        val next = activePlanTasks(context)?.second.orEmpty()
            .filter { it.scheduledEpochDay <= today && it.isOpen() }
            .sortedWith(compareBy({ it.scheduledEpochDay != today }, { it.sequence }))
            .firstOrNull()
        provideContent {
            // Lido aqui dentro: começar, pausar ou retomar (pelo widget ou pelo app) redesenha na hora.
            val session by app.preferences.focusSession.collectAsState(initial)
            val now = System.currentTimeMillis()
            val minutes = if (!session.active) 0L else ((if (session.pausedAt > 0) session.pausedAt else now) - session.startedAt - session.pausedMillis).coerceAtLeast(0) / 60_000
            val since = if (session.active) Instant.ofEpochMilli(session.startedAt).atZone(ZoneId.systemDefault()).toLocalTime().let { "%02d:%02d".format(it.hour, it.minute) } else ""
            WidgetFrame(null, center = true) {
                Header(Glyph.Timer, "FOCO", if (session.active) (if (minutes >= 60) "${minutes / 60}h${"%02d".format(minutes % 60)}" else "$minutes min") else "Pronto para focar?", eyebrowColor = WidgetLook.mint) {
                    if (session.active) Text(if (session.pausedAt > 0) "● pausado" else "● contando", style = TextStyle(fontSize = fs(12f), fontWeight = FontWeight.Bold, color = if (session.pausedAt > 0) WidgetLook.gold else WidgetLook.mint))
                }
                Text(
                    if (session.active) "${session.title.ifBlank { "Sessão de estudo" }} · desde $since"
                    else next?.let { "Próximo: ${it.shortTitle()}" } ?: "Uma sessão livre, no seu ritmo.",
                    maxLines = 2, style = WidgetLook.body(size = 14), modifier = GlanceModifier.padding(top = 6.dp),
                )
                Row(GlanceModifier.fillMaxWidth().padding(top = 12.dp)) {
                    if (session.active) {
                        StickerButton(if (session.pausedAt > 0) "Retomar" else "Pausar", actionRunCallback<ToggleFocusAction>(), primary = true, modifier = GlanceModifier.defaultWeight().padding(end = 6.dp))
                        StickerButton("Encerrar", openApp(context, "focus", "focus-open"), primary = false, modifier = GlanceModifier.defaultWeight())
                    } else {
                        StickerButton(if (next != null) "Focar no próximo" else "Começar", actionRunCallback<StartFocusAction>(if (next != null) actionParametersOf(FocusTaskKey to next.id) else actionParametersOf()), primary = true, modifier = GlanceModifier.defaultWeight().padding(end = if (next != null) 6.dp else 0.dp))
                        if (next != null) StickerButton("Livre", actionRunCallback<StartFocusAction>(), primary = false, modifier = GlanceModifier.defaultWeight())
                    }
                }
            }
        }
    }
}

class FocusWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FocusWidget()
}

// -------------------------------------------------------------------------------- Constância

class StreakWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as EstudarioApplication
        val zone = ZoneId.systemDefault()
        fun dayOf(millis: Long) = Instant.ofEpochMilli(millis).atZone(zone).toLocalDate().toEpochDay()
        val today = LocalDate.now()
        val todayEpoch = today.toEpochDay()
        val weeks = 15
        // Começa numa segunda-feira, para cada coluna ser uma semana.
        val firstDay = today.minusDays((today.dayOfWeek.value - 1).toLong()).minusWeeks((weeks - 1).toLong()).toEpochDay()
        val since = LocalDate.ofEpochDay(firstDay).atStartOfDay(zone).toInstant().toEpochMilli()
        // Atividade do dia: questões respondidas + minutos de estudo registrados (5 min ≈ 1 questão).
        val score = HashMap<Long, Int>()
        app.database.dao().attemptsSince(since).forEach { score.merge(dayOf(it.answeredAt), 1, Int::plus) }
        app.database.plannerDao().executionsOnce().filter { it.completedAt >= since }.forEach { score.merge(dayOf(it.completedAt), (it.actualMinutes / 5).coerceAtLeast(1), Int::plus) }
        var cursor = if ((score[todayEpoch] ?: 0) > 0) todayEpoch else todayEpoch - 1
        var counted = 0
        while ((score[cursor] ?: 0) > 0) { counted++; cursor-- }
        // A sequência oficial é a do app (a mesma da Contagem e do Início); a contada aqui só se não houver.
        val streak = FolhaWidget.read(context)?.streak ?: counted
        val activeDays = (firstDay..todayEpoch).count { (score[it] ?: 0) > 0 }
        val levels = listOf(0x00000000L, 0xFFB7EBD5L, 0xFF6FD8AEL, 0xFF34B884L, 0xFF138A5EL).map { ColorProvider(Color(it)) }
        val empty = androidx.glance.color.ColorProvider(day = Color(0xFFE9E5F5), night = Color(0xFF223358))
        fun level(v: Int) = when { v <= 0 -> 0; v < 5 -> 1; v < 15 -> 2; v < 30 -> 3; else -> 4 }
        provideContent {
            WidgetFrame(openApp(context, "statistics", "streak"), center = true) {
                Header(Glyph.Fire, "CONSTÂNCIA", if (streak > 0) "$streak ${if (streak == 1) "dia seguido" else "dias seguidos"}" else "Comece hoje") {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("$activeDays dias", style = TextStyle(fontSize = fs(15f), fontWeight = FontWeight.Bold, color = WidgetLook.white))
                        Text("em $weeks semanas", style = WidgetLook.body(WidgetLook.faint, 12))
                    }
                }
                // O mapa: 7 linhas (seg a dom) × 15 semanas; cada linha dividida em dois blocos (limite de 10).
                Column(GlanceModifier.fillMaxWidth().padding(top = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    (0 until 7).forEach { weekday ->
                        Row {
                            listOf(0 until 8, 8 until weeks).forEach { range ->
                                Row {
                                    range.forEach { w ->
                                        val day = firstDay + w * 7L + weekday
                                        val v = score[day] ?: 0
                                        val color = when { day > todayEpoch -> ColorProvider(Color.Transparent); v <= 0 -> empty; else -> levels[level(v)] }
                                        Box(GlanceModifier.padding(end = 3.dp, bottom = 3.dp)) {
                                            Box(GlanceModifier.size(13.dp).cornerRadius(4.dp).background(color)) {}
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class StreakWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StreakWidget()
}
