package br.com.estudario.ui.widget

import android.content.Context
import android.content.Intent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import br.com.estudario.EstudarioApplication
import br.com.estudario.MainActivity
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.ui.planner.minutesLabelPtBr
import java.time.LocalDate

class EstudarioWidget : GlanceAppWidget() {
    
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
            val nextTask = ofToday.firstOrNull { it.status == PlanTaskStatus.EM_ANDAMENTO }
                ?: ofToday.firstOrNull()
                ?: pending.firstOrNull()
            
            if (nextTask != null) {
                nextSubject = nextTask.subjectNameSnapshot
                nextTopic = nextTask.topicNameSnapshot ?: "Missão"
                timeLabel = minutesLabelPtBr(nextTask.plannedMinutes)
                val xpAmount = nextTask.plannedMinutes + (nextTask.plannedQuestions * 2)
                xpLabel = "+$xpAmount XP"
                hasTask = true
                taskIdToStart = nextTask.id
                topicIdToStart = nextTask.topicId ?: 0L
            }
        }

        // o Folha: humor e fala a partir do resumo do dia que o app guardou (FolhaWidget)
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val day = FolhaWidget.read(context)
        val folha = runCatching { FolhaWidget.image(context, FolhaWidget.mood(day, hour), 300) }.getOrNull()
        val fala = FolhaWidget.line(day, hour)

        provideContent {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (hasTask) {
                    putExtra("START_FOCUS_TASK_ID", taskIdToStart)
                    putExtra("START_FOCUS_TOPIC_ID", topicIdToStart)
                    putExtra("START_FOCUS_TITLE", "$nextSubject - $nextTopic")
                }
            }
            val white = ColorProvider(android.graphics.Color.WHITE)
            val mute = ColorProvider(android.graphics.Color.parseColor("#B8B3E6"))
            val gold = ColorProvider(android.graphics.Color.parseColor("#FFB421"))

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(24.dp)
                    .background(ColorProvider(android.graphics.Color.parseColor("#1D1670")))
                    .padding(12.dp)
                    .clickable(actionStartActivity(intent)),
            ) {
                // o Folha e a fala dele
                Row(verticalAlignment = Alignment.CenterVertically, modifier = GlanceModifier.fillMaxWidth()) {
                    if (folha != null) Image(ImageProvider(folha), contentDescription = "Folha", modifier = GlanceModifier.size(64.dp))
                    Column(modifier = GlanceModifier.defaultWeight().padding(start = 6.dp)) {
                        Text(
                            text = if (day == null) "ESTUDÁRIO" else if (day.streak > 0) "${day.streak} ${if (day.streak == 1) "DIA" else "DIAS"} SEGUIDOS" else "SEQUÊNCIA",
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = gold),
                        )
                        Text(text = fala, maxLines = 2, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = white))
                    }
                }
                Spacer(GlanceModifier.height(8.dp))
                // a meta do dia
                if (day != null && day.goal > 0) {
                    androidx.glance.appwidget.LinearProgressIndicator(
                        progress = day.progress, modifier = GlanceModifier.fillMaxWidth().height(6.dp),
                        color = gold, backgroundColor = ColorProvider(android.graphics.Color.parseColor("#3A3390")),
                    )
                    Spacer(GlanceModifier.height(6.dp))
                }
                // a próxima missão
                Text(text = "PRÓXIMA MISSÃO", style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = mute))
                Text(text = nextSubject, maxLines = 1, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = white))
                Text(text = nextTopic, maxLines = 2, style = TextStyle(fontSize = 12.sp, color = mute))
                if (hasTask) Row(verticalAlignment = Alignment.CenterVertically, modifier = GlanceModifier.padding(top = 4.dp)) {
                    Text(text = timeLabel, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, color = white), modifier = GlanceModifier.padding(end = 10.dp))
                    Text(text = xpLabel, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = gold))
                }
            }
        }
    }
}
