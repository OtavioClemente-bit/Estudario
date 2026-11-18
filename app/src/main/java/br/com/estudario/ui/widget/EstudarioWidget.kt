package br.com.estudario.ui.widget

import android.content.Context
import android.content.Intent
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import br.com.estudario.EstudarioApplication
import br.com.estudario.MainActivity
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.planner.minutesLabelPtBr
import java.time.LocalDate

private val Pokes = intPreferencesKey("cutucadas")

/** Tocar no Folha: ele troca de cara e diz outra coisa (o widget não pode ter animação contínua). */
class PokeFolhaAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        updateAppWidgetState(context, glanceId) { it[Pokes] = (it[Pokes] ?: 0) + 1 }
        EstudarioWidget().update(context, glanceId)
    }
}

/** Próxima missão: o Folha com a fala do dia, a sequência, a meta e o próximo estudo. */
class EstudarioWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as EstudarioApplication
        val plan = app.planRepository.plansOnce().firstOrNull { it.active && !it.archived }

        var nextSubject = "Tudo feito!"
        var nextTopic = "Você não tem missões pendentes."
        var timeLabel = ""
        var xpLabel = ""
        var hasTask = false
        var taskIdToStart = ""
        var topicIdToStart = 0L

        if (plan != null) {
            val todayEpoch = LocalDate.now().toEpochDay()
            val tasks = app.planRepository.tasksOnce(plan.id)
            val pending = tasks.filter { it.scheduledEpochDay <= todayEpoch && (it.status == PlanTaskStatus.PLANEJADA || it.status == PlanTaskStatus.EM_ANDAMENTO) }
            // Igual ao "Agora" do Início: a de hoje primeiro (já começada antes); atrasada só se hoje
            // não tiver nada pendente.
            val ofToday = pending.filter { it.scheduledEpochDay == todayEpoch }
            val nextTask = ofToday.firstOrNull { it.status == PlanTaskStatus.EM_ANDAMENTO } ?: ofToday.firstOrNull() ?: pending.firstOrNull()
            if (nextTask != null) {
                nextSubject = nextTask.subjectNameSnapshot
                nextTopic = nextTask.topicNameSnapshot ?: "Missão"
                timeLabel = minutesLabelPtBr(nextTask.plannedMinutes)
                xpLabel = "+${nextTask.plannedMinutes + nextTask.plannedQuestions * 2} XP"
                hasTask = true
                taskIdToStart = nextTask.id
                topicIdToStart = nextTask.topicId ?: 0L
            } else {
                // Hoje está em dia: mostra o próximo estudo do plano em vez de um "tudo feito" sem rumo.
                tasks.filter { it.scheduledEpochDay > todayEpoch && (it.status == PlanTaskStatus.PLANEJADA || it.status == PlanTaskStatus.EM_ANDAMENTO) }
                    .minWithOrNull(compareBy({ it.scheduledEpochDay }, { it.sequence }))
                    ?.let { upcoming ->
                        val weekday = LocalDate.ofEpochDay(upcoming.scheduledEpochDay).dayOfWeek
                            .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale("pt", "BR"))
                        nextSubject = "Hoje está em dia!"
                        nextTopic = "Próximo, $weekday: ${upcoming.topicNameSnapshot ?: upcoming.subjectNameSnapshot}"
                    }
            }
        }

        // o Folha: humor e fala a partir do resumo do dia que o app guardou (FolhaWidget); ao ser
        // tocado ele alterna entre algumas caras e falas
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val day = FolhaWidget.read(context)
        val baseMood = FolhaWidget.mood(day, hour)
        val pokeMoods = listOf(baseMood, FolhaMood.HAPPY, FolhaMood.SURPRISED, FolhaMood.PROUD, FolhaMood.WAVE)
        val faces = pokeMoods.map { runCatching { FolhaWidget.image(context, it, 360) }.getOrNull() }
        val name = day?.name?.takeIf { it.isNotBlank() }
        val lines = listOf(
            FolhaWidget.line(day, hour),
            "Ei${name?.let { ", $it" } ?: ""}! Tô aqui de olho no seu plano.",
            "Opa! Uma página por vez, combinado?",
            if ((day?.streak ?: 0) > 1) "${day!!.streak} dias seguidos! Não para agora." else "Bora acender a sequência hoje?",
            "Eu seguro o marca-texto, você estuda!",
        )

        provideContent {
            val poke = (currentState(Pokes) ?: 0) % lines.size
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (hasTask) {
                    putExtra("START_FOCUS_TASK_ID", taskIdToStart)
                    putExtra("START_FOCUS_TOPIC_ID", topicIdToStart)
                    putExtra("START_FOCUS_TITLE", "$nextSubject - $nextTopic")
                }
            }
            WidgetFrame(actionStartActivity(intent), center = true) {
                // o Folha (toque nele: outra cara, outra fala) e o balão
                Row(verticalAlignment = Alignment.CenterVertically, modifier = GlanceModifier.fillMaxWidth()) {
                    faces[poke]?.let { Image(ImageProvider(it), contentDescription = "Folha: toque para ele falar", modifier = GlanceModifier.size(84.dp).clickable(actionRunCallback<PokeFolhaAction>())) }
                    Column(modifier = GlanceModifier.defaultWeight().padding(start = 8.dp)) {
                        Text(
                            text = if (day == null) "ESTUDÁRIO" else if (day.streak > 0) "🔥 ${day.streak} ${if (day.streak == 1) "DIA" else "DIAS"} SEGUIDOS" else "SEQUÊNCIA",
                            style = WidgetLook.eyebrow(),
                        )
                        Box(GlanceModifier.fillMaxWidth().padding(top = 4.dp).background(WidgetLook.card).padding(start = 10.dp, top = 7.dp, end = 13.dp, bottom = 10.dp)) {
                            Text(text = lines[poke], maxLines = 3, style = TextStyle(fontSize = fs(14f), fontWeight = FontWeight.Bold, color = WidgetLook.white))
                        }
                    }
                }
                // a meta do dia
                if (day != null && day.goal > 0) Box(GlanceModifier.padding(top = 10.dp)) { Progress(day.progress, WidgetLook.gold) }
                // a próxima missão
                Text(text = "PRÓXIMA MISSÃO", style = WidgetLook.eyebrow(WidgetLook.faint), modifier = GlanceModifier.padding(top = 10.dp))
                Text(text = nextSubject, maxLines = 1, style = WidgetLook.title(17))
                Text(text = nextTopic, maxLines = 2, style = WidgetLook.body(size = 13))
                if (hasTask) Row(verticalAlignment = Alignment.CenterVertically, modifier = GlanceModifier.padding(top = 4.dp)) {
                    Text(text = timeLabel, style = TextStyle(fontSize = fs(13f), fontWeight = FontWeight.Bold, color = WidgetLook.white), modifier = GlanceModifier.padding(end = 10.dp))
                    Text(text = xpLabel, style = TextStyle(fontSize = fs(13f), fontWeight = FontWeight.Bold, color = WidgetLook.gold))
                }
            }
        }
    }
}
