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
            val nextTask = pending.firstOrNull()
            
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

        provideContent {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                if (hasTask) {
                    putExtra("START_FOCUS_TASK_ID", taskIdToStart)
                    putExtra("START_FOCUS_TOPIC_ID", topicIdToStart)
                    putExtra("START_FOCUS_TITLE", "$nextSubject - $nextTopic")
                }
            }

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ImageProvider(android.R.color.white)) // Fundo branco simples
                    .padding(16.dp)
                    .clickable(actionStartActivity(intent))
            ) {
                Column(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PRÓXIMA MISSÃO",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(android.graphics.Color.parseColor("#4F46E5"))
                        ),
                        modifier = GlanceModifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = nextSubject,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        modifier = GlanceModifier.padding(bottom = 4.dp)
                    )
                    
                    Text(
                        text = nextTopic,
                        style = TextStyle(fontSize = 14.sp),
                        modifier = GlanceModifier.padding(bottom = 12.dp)
                    )
                    
                    if (hasTask) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = GlanceModifier.fillMaxWidth()
                        ) {
                            Text(
                                text = timeLabel,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
                                modifier = GlanceModifier.padding(end = 12.dp)
                            )
                            Text(
                                text = xpLabel,
                                style = TextStyle(
                                    fontSize = 12.sp, 
                                    fontWeight = FontWeight.Bold,
                                    color = ColorProvider(android.graphics.Color.parseColor("#FFC94D"))
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
