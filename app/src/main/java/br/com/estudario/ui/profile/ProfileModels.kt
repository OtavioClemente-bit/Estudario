package br.com.estudario.ui.profile

import br.com.estudario.domain.DailyGoal
import br.com.estudario.domain.StreakSummary

/**
 * Perfil local da pessoa. Quando o login com Google for ligado, ele apenas preenche estes mesmos
 * campos — nada mais no app precisa saber de onde o nome e a foto vieram.
 */
data class UserProfile(
    val name: String = "",
    val email: String = "",
    val photoPath: String? = null,
    val dailyGoal: DailyGoal = DailyGoal.DEFAULT,
) {
    val signedIn: Boolean get() = email.isNotBlank()
    val displayName: String get() = name.ifBlank { "Concurseiro" }
    val firstName: String get() = displayName.trim().substringBefore(' ')
    val initials: String
        get() = displayName.trim().split(" ").filter { it.isNotBlank() }
            .let { parts -> if (parts.size >= 2) "${parts.first().first()}${parts.last().first()}" else parts.firstOrNull()?.take(2).orEmpty() }
            .uppercase()
}

/** Evento de comemoração: a pessoa fechou a meta do dia e a sequência subiu. */
data class StreakCelebration(val summary: StreakSummary, val reason: String, val xpToday: Int = 0)
