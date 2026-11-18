package br.com.estudario.ui.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import br.com.estudario.EstudarioApplication
import br.com.estudario.MainActivity
import br.com.estudario.R
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.brand.drawGlyph
import br.com.estudario.ui.planner.minutesLabelPtBr
import java.time.LocalDate
import java.util.Locale
import java.time.format.TextStyle as DayStyle

/*
 * Os widgets do plano, no mesmo desenho do app: papel (creme no tema claro, azul-marinho no escuro),
 * contorno de caneta e sombra de adesivo, os personagens da turma e letras que crescem com o widget.
 * - Plano do dia: a sequência de hoje; tocar num passo abre ali mesmo "Abrir" e "Focar".
 * - Semana: os próximos 7 dias; tocar num dia mostra os estudos dele no próprio widget.
 * - Contagem: quantos dias faltam para a prova e quanto do edital já foi visto.
 *
 * Regra do Android para widgets: cada linha ou coluna aceita no máximo 10 filhos (o resto some).
 * Por isso o espaço entre itens é padding, não Spacer, e listas longas ficam em colunas próprias.
 */

/** Cor com versão clara e escura (o widget segue o tema do celular, como o app). */
private fun dn(day: Long, night: Long) = androidx.glance.color.ColorProvider(day = Color(day), night = Color(night))

internal object WidgetLook {
    /** Texto principal: tinta de caneta no claro, branco no escuro. */
    val white = dn(0xFF14113A, 0xFFFFFFFF)
    /** Tinta fixa, para texto sobre as bolinhas coloridas. */
    val ink = ColorProvider(Color(0xFF14113A))
    val mute = dn(0xFF5E5A8C, 0xFFB4B6E2)
    val faint = dn(0xFF8783AE, 0xFF8A8EC4)
    val gold = dn(0xFFB86E00, 0xFFFFC94A)
    val mint = dn(0xFF0F8F63, 0xFF34D399)
    val coral = dn(0xFFC93D2E, 0xFFFF8A80)
    val track = dn(0xFFE6E1F7, 0xFF2A3B63)
    val blue = dn(0xFF3157C9, 0xFF8FB0FF)
    // Cores dos tipos de estudo (as das bolinhas e traços; iguais nos dois temas).
    val theory = ColorProvider(Color(0xFFA78BFF))
    val questions = ColorProvider(Color(0xFF34D399))
    val review = ColorProvider(Color(0xFFFFA36B))
    val sim = ColorProvider(Color(0xFF6CC4FF))
    val doneDot = ColorProvider(Color(0xFF34D399))

    val bg = ImageProvider(R.drawable.widget_bg)
    val card = ImageProvider(R.drawable.widget_card)
    val cardHi = ImageProvider(R.drawable.widget_card_hi)
    val daySel = ImageProvider(R.drawable.widget_day_sel)
    val button = ImageProvider(R.drawable.widget_button)
    val ghost = ImageProvider(R.drawable.widget_button_ghost)

    fun kindColor(type: PlanTaskType) = when (type) {
        PlanTaskType.THEORY -> theory
        PlanTaskType.QUESTIONS -> questions
        PlanTaskType.REVIEW -> review
        PlanTaskType.SIMULATION -> sim
        else -> ColorProvider(Color(0xFFFFC94A))
    }

    fun kindLabel(type: PlanTaskType) = when (type) {
        PlanTaskType.THEORY -> "Teoria"
        PlanTaskType.QUESTIONS -> "Questões"
        PlanTaskType.REVIEW -> "Revisão"
        PlanTaskType.SIMULATION -> "Simulado"
        else -> "Estudo"
    }

    fun kindGlyph(type: PlanTaskType) = when (type) {
        PlanTaskType.THEORY -> Glyph.OpenBook
        PlanTaskType.QUESTIONS -> Glyph.Clipboard
        PlanTaskType.REVIEW -> Glyph.Cycle
        PlanTaskType.SIMULATION -> Glyph.Timer
        else -> Glyph.Pencil
    }

    /** O rótulo pequeno em caixa alta que abre cada widget. */
    @Composable fun eyebrow(color: ColorProvider = gold) = TextStyle(fontSize = fs(12f), fontWeight = FontWeight.Bold, color = color)
    @Composable fun title(size: Int = 19) = TextStyle(fontSize = fs(size.toFloat()), fontWeight = FontWeight.Bold, color = white)
    @Composable fun body(color: ColorProvider = mute, size: Int = 14) = TextStyle(fontSize = fs(size.toFloat()), color = color)
}

/**
 * Tamanho de letra que cresce com o widget: no tamanho padrão já é legível e, se a pessoa aumentar o
 * widget, as letras acompanham (até 35% maiores), em vez de sobrar espaço vazio.
 */
@Composable
internal fun fs(base: Float): TextUnit {
    val w = LocalSize.current.width.value
    val k = if (w <= 0f) 1f else (w / 290f).coerceIn(1f, 1.35f)
    return (base * k).sp
}

/** Abre o app numa rota (a mesma das notificações) ou só abre o app. */
internal fun openApp(context: Context, route: String?, requestKey: String): Action {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        // Dados diferentes por item: sem isso o Android reaproveita o mesmo atalho para todos os passos.
        data = android.net.Uri.parse("estudario://widget/$requestKey")
        if (route != null) putExtra("notification_destination", route)
    }
    return actionStartActivity(intent)
}

internal fun PlanTaskEntity.isOpen() = status == PlanTaskStatus.PLANEJADA || status == PlanTaskStatus.EM_ANDAMENTO
internal fun PlanTaskEntity.counts() = status != PlanTaskStatus.REPROGRAMADA
internal fun PlanTaskEntity.shortTitle() = (topicNameSnapshot ?: subjectNameSnapshot).substringBefore(" (").trim().trimEnd('.')
internal fun PlanTaskEntity.route() = when {
    type == PlanTaskType.SIMULATION -> "simulations"
    topicId != null -> "topic/$topicId/task/$id"
    else -> "plan"
}
internal fun PlanTaskEntity.amount() =
    if (type == PlanTaskType.QUESTIONS && plannedQuestions > 0) "$plannedQuestions questões" else minutesLabelPtBr(plannedMinutes)

internal suspend fun activePlanTasks(context: Context): Pair<br.com.estudario.data.local.planner.StudyPlanEntity, List<PlanTaskEntity>>? {
    val app = context.applicationContext as EstudarioApplication
    val plan = app.planRepository.plansOnce().firstOrNull { it.active && !it.archived } ?: return null
    return plan to app.planRepository.tasksOnce(plan.id)
}

/** Personagem da turma (o mesmo desenho do app) como imagem, para o widget. */
internal fun glyphBitmap(glyph: Glyph, px: Int = 96): android.graphics.Bitmap {
    val image = ImageBitmap(px, px)
    val canvas = androidx.compose.ui.graphics.Canvas(image)
    CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, canvas, Size(px.toFloat(), px.toFloat())) { drawGlyph(glyph) }
    return image.asAndroidBitmap()
}

@Composable
internal fun GlyphIcon(glyph: Glyph, size: Int = 26) {
    val bitmap = runCatching { glyphBitmap(glyph, size * 4) }.getOrNull() ?: return
    Image(ImageProvider(bitmap), contentDescription = null, modifier = GlanceModifier.size(size.dp))
}

/** A moldura de todos os widgets: o papel recortado com contorno e sombra, e o respiro das bordas. */
@Composable
internal fun WidgetFrame(action: Action?, center: Boolean = false, content: @Composable () -> Unit) {
    // A sombra ocupa 4 dp à direita e embaixo: o respiro compensa para o conteúdo ficar no meio do papel.
    val base = GlanceModifier.fillMaxSize().background(WidgetLook.bg).padding(start = 16.dp, top = 14.dp, end = 20.dp, bottom = 18.dp)
    Column(if (action != null) base.clickable(action) else base, verticalAlignment = if (center) Alignment.CenterVertically else Alignment.Top) { content() }
}

/** Cabeçalho comum: personagem da turma, o rótulo e o título; à direita, um número ou aviso. */
@Composable
internal fun Header(glyph: Glyph, eyebrow: String, title: String, eyebrowColor: ColorProvider = WidgetLook.gold, action: Action? = null, trailing: @Composable () -> Unit = {}) {
    val base = GlanceModifier.fillMaxWidth()
    Row(if (action != null) base.clickable(action) else base, verticalAlignment = Alignment.CenterVertically) {
        GlyphIcon(glyph, 34)
        Column(GlanceModifier.defaultWeight().padding(start = 10.dp)) {
            Text(eyebrow, style = WidgetLook.eyebrow(eyebrowColor))
            Text(title, maxLines = 1, style = WidgetLook.title())
        }
        trailing()
    }
}

/** O Folha, como no app, para dar rosto aos avisos de vazio e de dia livre. */
@Composable
internal fun FolhaFace(context: Context, mood: br.com.estudario.ui.assistant.FolhaMood, size: Int = 72) {
    val bitmap = runCatching { FolhaWidget.image(context, mood, size * 4) }.getOrNull() ?: return
    Image(ImageProvider(bitmap), contentDescription = "Folha", modifier = GlanceModifier.size(size.dp))
}

@Composable
internal fun EmptyState(context: Context, eyebrow: String, line: String) {
    WidgetFrame(openApp(context, "plan", "empty-$eyebrow"), center = true) {
        Row(GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            FolhaFace(context, br.com.estudario.ui.assistant.FolhaMood.HAPPY)
            Column(GlanceModifier.defaultWeight().padding(start = 12.dp)) {
                Text(eyebrow, style = WidgetLook.eyebrow())
                Text(line, maxLines = 4, style = WidgetLook.title(16), modifier = GlanceModifier.padding(top = 2.dp))
            }
        }
    }
}

/** Uma barra de progresso, com o trilho da marca. */
@Composable
internal fun Progress(value: Float, color: ColorProvider = WidgetLook.questions) {
    LinearProgressIndicator(progress = value.coerceIn(0f, 1f), modifier = GlanceModifier.fillMaxWidth().height(8.dp).cornerRadius(4.dp), color = color, backgroundColor = WidgetLook.track)
}

/** Botão de adesivo (dourado ou papel), do jeito dos botões do app. */
@Composable
internal fun StickerButton(label: String, action: Action, primary: Boolean = true, modifier: GlanceModifier = GlanceModifier) {
    Box(
        modifier.background(if (primary) WidgetLook.button else WidgetLook.ghost).padding(start = 8.dp, end = 11.dp, top = 8.dp, bottom = 11.dp).clickable(action),
        contentAlignment = Alignment.Center,
    ) { Text(label, maxLines = 1, style = TextStyle(fontSize = fs(14f), fontWeight = FontWeight.Bold, color = if (primary) WidgetLook.ink else WidgetLook.white, textAlign = TextAlign.Center)) }
}

// ------------------------------------------------------------------------------- Plano do dia

private val OpenStep = stringPreferencesKey("passo_aberto")
private val StepParam = ActionParameters.Key<String>("passo")

/** Tocar num passo abre (ou fecha) as ações dele no próprio widget. */
class ToggleStepAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val step = parameters[StepParam] ?: return
        updateAppWidgetState(context, glanceId) { prefs ->
            if (prefs[OpenStep] == step) prefs[OpenStep] = "-" else prefs[OpenStep] = step
        }
        DayPlanWidget().update(context, glanceId)
    }
}

class DayPlanWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val today = LocalDate.now().toEpochDay()
        val loaded = activePlanTasks(context)
        val tasks = loaded?.second.orEmpty().filter { it.scheduledEpochDay == today && it.counts() }.sortedBy { it.sequence }
        val late = loaded?.second.orEmpty().count { it.scheduledEpochDay < today && it.isOpen() }
        val done = tasks.count { it.status == PlanTaskStatus.CONCLUIDA }
        val nextId = tasks.firstOrNull { it.isOpen() }?.id
        val left = tasks.filter { it.isOpen() }.sumOf { it.plannedMinutes }
        // Dia livre: o próximo dia com estudo, para o widget nunca ficar sem rumo.
        val upcoming = loaded?.second.orEmpty().filter { it.scheduledEpochDay > today && it.isOpen() }.sortedWith(compareBy({ it.scheduledEpochDay }, { it.sequence })).firstOrNull()
        provideContent {
            if (loaded == null) { EmptyState(context, "PLANO DE HOJE", "Monte seu plano no Estudário e a sequência de hoje aparece aqui."); return@provideContent }
            // Passo aberto: o que a pessoa tocou; sem toque, o próximo da vez.
            val opened = currentState(OpenStep).let { if (it == null) nextId else it.takeIf { s -> s != "-" } }
            WidgetFrame(null, center = tasks.isEmpty()) {
                Header(
                    Glyph.Calendar, "PLANO DE HOJE",
                    when {
                        tasks.isEmpty() -> "Dia livre no plano"
                        done == tasks.size -> "Missão cumprida!"
                        else -> "$done de ${tasks.size} feitos"
                    },
                    action = openApp(context, "plan", "day-head"),
                ) {
                    if (left > 0 || late > 0) Column(horizontalAlignment = Alignment.End) {
                        if (left > 0) Text(minutesLabelPtBr(left), style = TextStyle(fontSize = fs(16f), fontWeight = FontWeight.Bold, color = WidgetLook.white))
                        Text(if (late > 0) "$late atrasad${if (late == 1) "o" else "os"}" else "pela frente", style = WidgetLook.body(if (late > 0) WidgetLook.coral else WidgetLook.faint, 12))
                    }
                }
                if (tasks.isNotEmpty()) Box(GlanceModifier.padding(top = 10.dp, bottom = 10.dp)) { Progress(done.toFloat() / tasks.size) }
                Column(GlanceModifier.fillMaxWidth()) {
                    tasks.take(5).forEachIndexed { index, task -> StepRow(context, index, task, task.id == nextId, task.id == opened) }
                    if (tasks.size > 5) Text("+ ${tasks.size - 5} no plano de hoje", style = WidgetLook.body(WidgetLook.faint, 12), modifier = GlanceModifier.clickable(openApp(context, "plan", "day-more")))
                    if (tasks.isEmpty()) Row(GlanceModifier.fillMaxWidth().padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        FolhaFace(context, br.com.estudario.ui.assistant.FolhaMood.HAPPY, 68)
                        Column(GlanceModifier.defaultWeight().padding(start = 12.dp)) {
                            Text("Descanso merecido. Se quiser, uma revisão rápida mantém tudo fresco.", maxLines = 3, style = WidgetLook.body(WidgetLook.white, 15))
                            if (upcoming != null) Text(
                                "Próximo: ${LocalDate.ofEpochDay(upcoming.scheduledEpochDay).dayOfWeek.getDisplayName(DayStyle.SHORT, Locale("pt", "BR")).take(3).uppercase()} · ${upcoming.shortTitle()}",
                                maxLines = 2, style = TextStyle(fontSize = fs(13f), fontWeight = FontWeight.Bold, color = WidgetLook.gold),
                                modifier = GlanceModifier.padding(top = 6.dp).clickable(openApp(context, upcoming.route(), "day-next")),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepRow(context: Context, index: Int, task: PlanTaskEntity, isNext: Boolean, isOpen: Boolean) {
    val isDone = task.status == PlanTaskStatus.CONCLUIDA
    Box(GlanceModifier.fillMaxWidth().padding(bottom = 6.dp)) {
        Column(GlanceModifier.fillMaxWidth().background(if (isNext) WidgetLook.cardHi else WidgetLook.card).padding(start = 10.dp, top = 8.dp, end = 13.dp, bottom = 10.dp)) {
            Row(
                GlanceModifier.fillMaxWidth().clickable(actionRunCallback<ToggleStepAction>(actionParametersOf(StepParam to task.id))),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    GlanceModifier.size(28.dp).cornerRadius(14.dp).background(if (isDone) WidgetLook.doneDot else WidgetLook.kindColor(task.type)),
                    contentAlignment = Alignment.Center,
                ) { Text(if (isDone) "✓" else "${index + 1}", style = TextStyle(fontSize = fs(13f), fontWeight = FontWeight.Bold, color = WidgetLook.ink, textAlign = TextAlign.Center)) }
                Column(GlanceModifier.defaultWeight().padding(start = 10.dp)) {
                    Text(
                        task.shortTitle(), maxLines = if (isOpen) 2 else 1,
                        style = TextStyle(fontSize = fs(15f), fontWeight = FontWeight.Bold, color = if (isDone) WidgetLook.faint else WidgetLook.white, textDecoration = if (isDone) TextDecoration.LineThrough else null),
                    )
                    Text("${WidgetLook.kindLabel(task.type)} · ${task.amount()}", maxLines = 1, style = WidgetLook.body(size = 13))
                }
                if (isNext) Text("AGORA", style = TextStyle(fontSize = fs(11f), fontWeight = FontWeight.Bold, color = WidgetLook.gold))
            }
            // Aberto: as ações do passo, sem sair do widget até a pessoa escolher.
            if (isOpen && !isDone) Row(GlanceModifier.fillMaxWidth().padding(top = 8.dp)) {
                StickerButton("Abrir", openApp(context, task.route(), "day-${task.id}"), primary = true, modifier = GlanceModifier.defaultWeight().padding(end = 6.dp))
                StickerButton("Focar", actionRunCallback<StartFocusAction>(actionParametersOf(FocusTaskKey to task.id)), primary = false, modifier = GlanceModifier.defaultWeight())
            }
        }
    }
}

class DayPlanWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DayPlanWidget()
}

// ----------------------------------------------------------------------------------- Semana

private val SelectedDay = intPreferencesKey("dia_escolhido")
private val DayParam = ActionParameters.Key<Int>("dia")

/** Tocar num dia mostra os estudos dele no próprio widget (sem abrir o app). */
class SelectDayAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val day = parameters[DayParam] ?: return
        updateAppWidgetState(context, glanceId) { it[SelectedDay] = day }
        WeekWidget().update(context, glanceId)
    }
}

class WeekWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val today = LocalDate.now()
        val loaded = activePlanTasks(context)
        val byDay = loaded?.second.orEmpty().filter { it.counts() }.groupBy { it.scheduledEpochDay }
        val days = (0L until 7L).map { today.plusDays(it) }
        val pt = Locale("pt", "BR")
        val week = days.sumOf { d -> byDay[d.toEpochDay()].orEmpty().sumOf { it.plannedMinutes } }
        provideContent {
            if (loaded == null) { EmptyState(context, "SEMANA DE ESTUDOS", "Monte seu plano e cada dia da semana aparece aqui."); return@provideContent }
            val selected = (currentState(SelectedDay) ?: 0).coerceIn(0, 6)
            WidgetFrame(null) {
                Header(Glyph.Calendar, "SEMANA DE ESTUDOS", today.month.getDisplayName(DayStyle.FULL, pt).replaceFirstChar { it.uppercase() }, action = openApp(context, "plan", "week-head")) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(minutesLabelPtBr(week), style = TextStyle(fontSize = fs(16f), fontWeight = FontWeight.Bold, color = WidgetLook.white))
                        Text("nos 7 dias", style = WidgetLook.body(WidgetLook.faint, 12))
                    }
                }
                // Uma coluna por dia; o escolhido em azul. Tocar escolhe o dia.
                Row(GlanceModifier.fillMaxWidth().padding(top = 12.dp, bottom = 10.dp)) {
                    days.forEachIndexed { i, d ->
                        val list = byDay[d.toEpochDay()].orEmpty().sortedBy { it.sequence }
                        val allDone = list.isNotEmpty() && list.all { it.status == PlanTaskStatus.CONCLUIDA }
                        Box(GlanceModifier.defaultWeight().padding(end = if (i < days.lastIndex) 4.dp else 0.dp)) {
                            Column(
                                GlanceModifier.fillMaxWidth()
                                    .background(if (i == selected) WidgetLook.daySel else if (i == 0) WidgetLook.cardHi else WidgetLook.card)
                                    .padding(top = 6.dp, bottom = 9.dp, end = 2.dp)
                                    .clickable(actionRunCallback<SelectDayAction>(actionParametersOf(DayParam to i))),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(if (i == 0) "HOJE" else d.dayOfWeek.getDisplayName(DayStyle.SHORT, pt).take(3).uppercase(pt), style = TextStyle(fontSize = fs(10f), fontWeight = FontWeight.Bold, color = if (i == selected) WidgetLook.ink else WidgetLook.faint, textAlign = TextAlign.Center))
                                Text("${d.dayOfMonth}", style = TextStyle(fontSize = fs(18f), fontWeight = FontWeight.Bold, color = if (i == selected) WidgetLook.ink else WidgetLook.white, textAlign = TextAlign.Center))
                                Column(GlanceModifier.padding(top = 3.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    when {
                                        list.isEmpty() -> Text("livre", style = TextStyle(fontSize = fs(9f), color = if (i == selected) WidgetLook.ink else WidgetLook.faint, textAlign = TextAlign.Center))
                                        allDone -> Text("✓", style = TextStyle(fontSize = fs(14f), fontWeight = FontWeight.Bold, color = WidgetLook.mint, textAlign = TextAlign.Center))
                                        else -> list.take(4).forEach { t ->
                                            Box(GlanceModifier.padding(bottom = 3.dp)) {
                                                Box(GlanceModifier.width(20.dp).height(5.dp).cornerRadius(3.dp).background(if (t.status == PlanTaskStatus.CONCLUIDA) WidgetLook.track else WidgetLook.kindColor(t.type))) {}
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Os estudos do dia escolhido; tocar num deles abre o estudo.
                val day = days[selected]
                val list = byDay[day.toEpochDay()].orEmpty().sortedBy { it.sequence }
                val label = when (selected) { 0 -> "HOJE"; 1 -> "AMANHÃ"; else -> day.dayOfWeek.getDisplayName(DayStyle.FULL, pt).uppercase(pt) } + " · ${day.dayOfMonth}/${day.monthValue}"
                Text(label, style = WidgetLook.eyebrow(WidgetLook.blue))
                Column(GlanceModifier.fillMaxWidth()) {
                    if (list.isEmpty()) Text("Dia livre. Descanso também faz parte do plano.", maxLines = 2, style = WidgetLook.body(size = 14), modifier = GlanceModifier.padding(top = 6.dp))
                    list.take(4).forEach { t ->
                        val tDone = t.status == PlanTaskStatus.CONCLUIDA
                        Row(GlanceModifier.fillMaxWidth().padding(top = 7.dp).clickable(openApp(context, t.route(), "week-task-${t.id}")), verticalAlignment = Alignment.CenterVertically) {
                            Box(GlanceModifier.size(12.dp).cornerRadius(6.dp).background(if (tDone) WidgetLook.doneDot else WidgetLook.kindColor(t.type))) {}
                            Text(t.shortTitle(), maxLines = 1, style = TextStyle(fontSize = fs(14f), color = if (tDone) WidgetLook.faint else WidgetLook.white, textDecoration = if (tDone) TextDecoration.LineThrough else null), modifier = GlanceModifier.defaultWeight().padding(start = 10.dp))
                            Text(t.amount(), style = WidgetLook.body(WidgetLook.faint, 12))
                        }
                    }
                    if (list.size > 4) Text("+ ${list.size - 4} neste dia", style = WidgetLook.body(WidgetLook.faint, 12), modifier = GlanceModifier.padding(top = 6.dp).clickable(openApp(context, "plan", "week-more")))
                }
            }
        }
    }
}

class WeekWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeekWidget()
}

// --------------------------------------------------------------------------------- Contagem

class CountdownWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as EstudarioApplication
        val plan = app.planRepository.plansOnce().firstOrNull { it.active && !it.archived }
        val today = LocalDate.now().toEpochDay()
        val daysLeft = plan?.examEpochDay?.let { it - today }?.takeIf { it >= 0 }
        val dao = app.database.dao()
        val competition = plan?.let { p -> dao.competitionsOnce().firstOrNull { it.id == p.competitionId } }
            ?: dao.competitionsOnce().firstOrNull { it.isPrimary }
        val percent = competition?.let { c ->
            val subjects = dao.subjectsFor(c.id).mapTo(hashSetOf()) { it.id }
            val topics = dao.topicsOnce().filter { it.subjectId in subjects }
            val parents = topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
            val leaves = topics.filter { it.id !in parents }
            if (leaves.isEmpty()) null else leaves.count { it.status != br.com.estudario.data.local.TopicStatus.NAO_ESTUDADO } * 100 / leaves.size
        }
        val day = FolhaWidget.read(context)
        provideContent {
            WidgetFrame(openApp(context, null, "countdown"), center = true) {
                Column(GlanceModifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    GlyphIcon(Glyph.Target, 36)
                    Text((competition?.name ?: "Sua prova").uppercase().take(24), maxLines = 1, style = TextStyle(fontSize = fs(12f), fontWeight = FontWeight.Bold, color = WidgetLook.gold, textAlign = TextAlign.Center), modifier = GlanceModifier.padding(top = 4.dp))
                    Text(
                        when { daysLeft == null -> "?"; daysLeft == 0L -> "HOJE"; else -> "$daysLeft" },
                        style = TextStyle(fontSize = fs(44f), fontWeight = FontWeight.Bold, color = WidgetLook.white, textAlign = TextAlign.Center),
                    )
                    Text(
                        when { daysLeft == null -> "cadastre a data da prova"; daysLeft == 0L -> "é o dia da prova"; daysLeft == 1L -> "dia para a prova"; else -> "dias para a prova" },
                        maxLines = 1, style = TextStyle(fontSize = fs(13f), color = WidgetLook.mute, textAlign = TextAlign.Center),
                    )
                    if (percent != null) {
                        Box(GlanceModifier.padding(top = 10.dp, bottom = 4.dp)) { Progress(percent / 100f) }
                        Text("$percent% do edital visto", style = TextStyle(fontSize = fs(12f), fontWeight = FontWeight.Bold, color = WidgetLook.white, textAlign = TextAlign.Center))
                    }
                    if (day != null && day.streak > 0) Text("🔥 ${day.streak} ${if (day.streak == 1) "dia seguido" else "dias seguidos"}", style = TextStyle(fontSize = fs(12f), color = WidgetLook.gold, textAlign = TextAlign.Center), modifier = GlanceModifier.padding(top = 2.dp))
                }
            }
        }
    }
}

class CountdownWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CountdownWidget()
}
