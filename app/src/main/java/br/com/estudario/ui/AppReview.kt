package br.com.estudario.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.google.android.play.core.review.ReviewManagerFactory

/**
 * A avaliação na Google Play ("Está gostando do Estudário?"), a janelinha oficial do Google, dentro
 * do app. Só num momento bom (fechando a comemoração de uma sequência de 3 dias ou mais), nunca nos
 * primeiros dias de uso e no máximo uma vez a cada 60 dias. O próprio Google ainda decide se mostra.
 */
object AppReview {
    private const val PREFS = "estudario_review"
    private const val MIN_STREAK = 3
    private const val FIRST_DAYS_MS = 3L * 86_400_000
    private const val GAP_MS = 60L * 86_400_000

    private fun Context.activity(): Activity? {
        var current: Context? = this
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return null
    }

    fun maybeAsk(context: Context, streakDays: Int) {
        if (streakDays < MIN_STREAK) return
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()
        val firstSeen = prefs.getLong("first_seen", 0L).takeIf { it > 0 } ?: now.also { prefs.edit().putLong("first_seen", it).apply() }
        if (now - firstSeen < FIRST_DAYS_MS || now - prefs.getLong("last_asked", 0L) < GAP_MS) return
        val activity = context.activity() ?: return
        prefs.edit().putLong("last_asked", now).apply()
        val manager = ReviewManagerFactory.create(activity)
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) manager.launchReviewFlow(activity, request.result)
        }
    }

    /**
     * O dia do plano concluído: o Folha pergunta antes se a pessoa está curtindo (ReviewInvite). Mesmas
     * regras da sequência (nada nos 3 primeiros dias, uma vez a cada 60 dias) e, depois de "pode
     * melhorar", não pergunta mais.
     */
    fun shouldInvite(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()
        val firstSeen = prefs.getLong("first_seen", 0L).takeIf { it > 0 } ?: now.also { prefs.edit().putLong("first_seen", it).apply() }
        return !prefs.getBoolean("said_improve", false) && now - firstSeen >= FIRST_DAYS_MS && now - prefs.getLong("last_asked", 0L) >= GAP_MS
    }

    /** O convite apareceu (conta para o intervalo de 60 dias, qualquer que seja a resposta). */
    fun markInvited(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putLong("last_asked", System.currentTimeMillis()).apply()
    }

    /** "Pode melhorar": nunca mais o convite (a pessoa já disse o que pensa). */
    fun markImprove(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean("said_improve", true).apply()
    }

    /** "Estou amando": a janelinha oficial do Google (que ainda decide se mostra). */
    fun launchPlayReview(context: Context) {
        val activity = context.activity() ?: return
        val manager = ReviewManagerFactory.create(activity)
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) manager.launchReviewFlow(activity, request.result)
        }
    }

    /** Marca o primeiro uso (para não pedir avaliação logo de cara). */
    fun markFirstUse(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getLong("first_seen", 0L) == 0L) prefs.edit().putLong("first_seen", System.currentTimeMillis()).apply()
    }
}
