package br.com.estudario.focus

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.estudario.MainActivity
import br.com.estudario.R
import br.com.estudario.data.preferences.FocusSessionPrefs
import java.util.concurrent.TimeUnit

/**
 * Peças de sistema do modo foco: o Não Perturbe do Android, a notificação fixa com o botão de
 * encerrar e a rede de segurança que fecha uma sessão esquecida.
 *
 * Duas decisões importantes aqui:
 * - o filtro usado é o PRIORITY, não o silêncio total, para que as exceções que a pessoa já
 *   configurou (favoritos, quem liga duas vezes) continuem passando;
 * - o filtro anterior é guardado antes de mexer, e devolvido ao encerrar, para nunca deixar o
 *   aparelho mudo por engano.
 */
object FocusMode {
    const val CHANNEL = "focus_mode"
    const val ACTION_STOP = "br.com.estudario.action.FOCUS_STOP"
    const val NOTIFICATION_ID = 3010
    private const val CLOSED_NOTIFICATION_ID = 3011
    private const val SAFETY_WORK = "focus_safety_net"

    /** Teto de uma sessão: passou disso, foi esquecida aberta e o app encerra sozinho. */
    const val MAX_MINUTES = 240

    fun hasDndAccess(context: Context): Boolean =
        context.getSystemService(NotificationManager::class.java)?.isNotificationPolicyAccessGranted == true

    /** Tela do sistema onde a pessoa concede o acesso ao Não Perturbe (concessão única). */
    fun dndSettingsIntent(): Intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    /** Liga o Não Perturbe e devolve o filtro que estava valendo (ou FILTER_UNKNOWN se não mexeu). */
    fun turnOnDnd(context: Context): Int {
        val manager = context.getSystemService(NotificationManager::class.java) ?: return FocusSessionPrefs.FILTER_UNKNOWN
        if (!manager.isNotificationPolicyAccessGranted) return FocusSessionPrefs.FILTER_UNKNOWN
        return runCatching {
            val previous = manager.currentInterruptionFilter
            manager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
            previous
        }.getOrDefault(FocusSessionPrefs.FILTER_UNKNOWN)
    }

    /** Devolve o filtro anterior. Se não sabemos qual era, não mexe — nunca "chuta" o normal. */
    fun restoreDnd(context: Context, previousFilter: Int) {
        if (previousFilter == FocusSessionPrefs.FILTER_UNKNOWN) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (!manager.isNotificationPolicyAccessGranted) return
        runCatching { manager.setInterruptionFilter(previousFilter) }
    }

    fun showOngoing(context: Context, title: String, startedAt: Long, dndOn: Boolean) {
        if (!canPost(context)) return
        val open = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("notification_destination", "focus")
        }
        val openIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, open, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val stopIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID + 1,
            Intent(context, FocusActionReceiver::class.java).setAction(ACTION_STOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = NotificationCompat.Builder(context, CHANNEL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Modo foco ativo")
            .setContentText(if (dndOn) "$title • Não perturbe ligado" else title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(if (dndOn) "$title\nO Não Perturbe volta ao normal assim que você encerrar." else title))
            .setWhen(startedAt)
            .setUsesChronometer(true)
            .setOngoing(true)
            .setSilent(true)
            .setShowWhen(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openIntent)
            .addAction(0, "Encerrar", stopIntent)
            .build()
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    fun clearOngoing(context: Context) = NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)

    /** Avisa que uma sessão esquecida foi encerrada sozinha — a pessoa precisa saber. */
    fun notifyAutoClosed(context: Context, minutes: Int) {
        if (!canPost(context)) return
        val notification = NotificationCompat.Builder(context, CHANNEL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Modo foco encerrado sozinho")
            .setContentText("A sessão ficou aberta por $minutes minutos. O Não Perturbe voltou ao normal.")
            .setAutoCancel(true)
            .setSilent(true)
            .build()
        NotificationManagerCompat.from(context).notify(CLOSED_NOTIFICATION_ID, notification)
    }

    /**
     * Rede de segurança: se o app for fechado, morrer ou a pessoa esquecer, este trabalho encerra a
     * sessão e devolve o Não Perturbe mesmo sem ninguém abrir o app.
     */
    fun scheduleSafetyNet(context: Context) {
        val request = OneTimeWorkRequestBuilder<FocusSafetyNetWorker>()
            .setInitialDelay(MAX_MINUTES.toLong(), TimeUnit.MINUTES)
            .addTag(SAFETY_WORK)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(SAFETY_WORK, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancelSafetyNet(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(SAFETY_WORK)
    }

    private fun canPost(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
}
