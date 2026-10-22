package br.com.estudario.ui.widget

import android.content.Context
import br.com.estudario.data.FolhaLookStore
import br.com.estudario.domain.FolhaBond
import br.com.estudario.domain.FolhaSlot
import br.com.estudario.domain.FolhaWardrobe
import br.com.estudario.domain.StreakSummary
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.assistant.FolhaStyle
import br.com.estudario.ui.assistant.folhaBitmap

/**
 * O Folha no widget: o app guarda aqui o resumo do dia (sequência, meta, amizade, dias sem estudar)
 * sempre que o progresso muda, e o widget lê sem abrir o banco. O humor e a fala dele saem desse
 * resumo e da hora (de manhã acena, à noite cochila se já fechou o dia, some e ele fica com saudade).
 */
object FolhaWidget {
    private fun prefs(context: Context) = context.getSharedPreferences("folha_widget", Context.MODE_PRIVATE)

    data class State(
        val streak: Int, val best: Int, val todayDone: Boolean, val progress: Float, val todayQuestions: Int, val goal: Int,
        val daysAway: Int?, val bond: Int, val name: String,
    )

    /** Guarda o resumo e atualiza o widget (só quando algo mudou, para não redesenhar à toa). */
    fun save(context: Context, s: StreakSummary?, bond: Int) {
        if (s == null) return
        val away = FolhaBond.daysAway(s.calendar, br.com.estudario.time.today())
        val name = br.com.estudario.ui.assistant.FolhaLook.name
        val key = listOf(s.current, s.best, s.todayDone, (s.todayProgress * 100).toInt(), s.todayQuestions, s.goal.questions, away, bond, name).joinToString("|")
        val p = prefs(context)
        if (p.getString("key", null) == key) return
        p.edit().putString("key", key).putInt("streak", s.current).putInt("best", s.best).putBoolean("todayDone", s.todayDone)
            .putFloat("progress", s.todayProgress).putInt("todayQuestions", s.todayQuestions).putInt("goal", s.goal.questions)
            .putInt("daysAway", away ?: -1).putInt("bond", bond).putString("name", name)
            .putLong("day", java.time.LocalDate.now().toEpochDay()).apply()
        WidgetUpdater.update(context)
    }

    fun read(context: Context): State? {
        val p = prefs(context)
        if (!p.contains("key")) return null
        // o resumo é de quando o app foi aberto por último: a sequência só vale se for de hoje, ou de
        // ontem com o dia fechado; mais velho que isso, ela já caiu
        val savedDay = p.getLong("day", 0L); val today = java.time.LocalDate.now().toEpochDay()
        val savedDone = p.getBoolean("todayDone", false)
        val streak = when { savedDay == today -> p.getInt("streak", 0); savedDay == today - 1 && savedDone -> p.getInt("streak", 0); else -> 0 }
        return State(
            streak, p.getInt("best", 0), savedDone && savedDay == today, if (savedDay == today) p.getFloat("progress", 0f) else 0f, if (savedDay == today) p.getInt("todayQuestions", 0) else 0,
            p.getInt("goal", 0), p.getInt("daysAway", -1).takeIf { it >= 0 }?.plus((today - savedDay).toInt().coerceAtLeast(0)), p.getInt("bond", 0), p.getString("name", "").orEmpty(),
        )
    }

    /** O humor do Folha no widget agora. */
    fun mood(s: State?, hour: Int): FolhaMood = when {
        s == null -> FolhaMood.WAVE
        (s.daysAway ?: 0) >= 2 && !s.todayDone -> FolhaMood.SAD
        s.todayDone && (hour >= 22 || hour < 5) -> FolhaMood.SLEEPY
        s.todayDone -> FolhaMood.PROUD
        s.streak >= 2 && hour >= 18 -> FolhaMood.SURPRISED
        hour in 5..10 -> FolhaMood.WAVE
        else -> FolhaMood.HAPPY
    }

    /** O que ele diz no widget agora (curto, cabe em duas linhas). */
    fun line(s: State?, hour: Int): String {
        val nome = s?.name?.takeIf { it.isNotBlank() }
        return when {
            s == null -> "Oi! Abre o app que eu te mostro o dia."
            (s.daysAway ?: 0) >= 2 && !s.todayDone -> "Faz ${s.daysAway} dias… senti sua falta!"
            s.todayDone && (hour >= 22 || hour < 5) -> "Dia fechado. Boa noite${nome?.let { ", $it" } ?: ""}!"
            s.todayDone -> "Meta batida! Tô orgulhoso${nome?.let { ", $it" } ?: ""}."
            s.streak >= 2 && hour >= 18 -> "Ainda dá tempo de manter os ${s.streak} dias!"
            hour in 5..10 -> "Bom dia${nome?.let { ", $it" } ?: ""}! Bora começar?"
            s.streak == 0 -> "Bora acender a sequência hoje?"
            else -> "Bora estudar? Eu seguro a página."
        }
    }

    /** O Folha com o visual escolhido no armário, no humor de agora. */
    fun image(context: Context, mood: FolhaMood, sizePx: Int): android.graphics.Bitmap {
        val pieces = FolhaSlot.entries.associate { slot -> slot.key to (FolhaLookStore.read(context, slot) ?: FolhaWardrobe.default(slot).id) }
        return folhaBitmap(sizePx, mood, FolhaStyle(pieces = pieces, noScene = true))
    }
}
