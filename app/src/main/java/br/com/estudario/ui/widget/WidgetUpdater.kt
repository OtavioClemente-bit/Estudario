package br.com.estudario.ui.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object WidgetUpdater {
    @OptIn(DelicateCoroutinesApi::class)
    fun update(context: Context) {
        GlobalScope.launch {
            // Todos os widgets do app: cada um falhando sozinho não impede os outros.
            runCatching { EstudarioWidget().updateAll(context) }
            runCatching { DayPlanWidget().updateAll(context) }
            runCatching { WeekWidget().updateAll(context) }
            runCatching { CountdownWidget().updateAll(context) }
            runCatching { ReviewsWidget().updateAll(context) }
            runCatching { QuestionWidget().updateAll(context) }
            runCatching { FocusWidget().updateAll(context) }
            runCatching { StreakWidget().updateAll(context) }
        }
    }

    /** Só o widget de Foco (começar, pausar, retomar, encerrar). */
    @OptIn(DelicateCoroutinesApi::class)
    fun focus(context: Context) {
        GlobalScope.launch { runCatching { FocusWidget().updateAll(context.applicationContext) } }
    }
}
