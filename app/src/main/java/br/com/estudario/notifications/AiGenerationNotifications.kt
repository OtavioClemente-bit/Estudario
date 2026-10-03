package br.com.estudario.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import br.com.estudario.MainActivity
import br.com.estudario.R

/**
 * Avisos das gerações da IA quando o app não está na tela.
 *
 * Enquanto gera com o app minimizado, um aviso discreto e fixo ("Gerando o material de X") mostra
 * que o trabalho continua. Quando termina, ele vira um aviso com som ("Material pronto") e o botão
 * "Revisar e salvar", que abre o app direto no resultado. Com o app aberto não aparece nada: a
 * própria tela já mostra o andamento.
 */
object AiGenerationNotifications {
    /** Silencioso: o andamento não deve tocar nem vibrar. */
    private const val CHANNEL_PROGRESS = "ai_generation_progress"

    /** Com som: o resultado é o que a pessoa está esperando. */
    private const val CHANNEL_DONE = "ai_generation_done"

    /** Destino do toque no aviso: abre o app com o aviso "Revisar" no topo. */
    const val DESTINATION = "home"

    fun createChannels(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        manager.createNotificationChannels(listOf(
            NotificationChannel(CHANNEL_PROGRESS, "Gerações em andamento", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Mostra que o Estudário continua gerando seu material com o app minimizado."
                setShowBadge(false)
            },
            NotificationChannel(CHANNEL_DONE, "Gerações prontas", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Avisa quando o material, as questões ou o edital ficam prontos para revisar."
            },
        ))
    }

    private fun idFor(taskId: String) = ("ai-task:$taskId").hashCode()

    private fun canPost(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    private fun openApp(context: Context, taskId: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("notification_destination", DESTINATION)
        }
        return PendingIntent.getActivity(context, idFor(taskId), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    /** "Gerando o material de Lei de Ohm", fixo e sem som, com a barra de progresso animada. */
    fun showRunning(context: Context, taskId: String, title: String, kind: String) {
        if (!canPost(context)) return
        val notification = NotificationCompat.Builder(context, CHANNEL_PROGRESS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Gerando ${kind.lowercase()}")
            .setLargeIcon(runCatching { br.com.estudario.ui.assistant.folhaBitmap(256, br.com.estudario.ui.assistant.FolhaMood.THINKING) }.getOrNull())
            .setContentText(title)
            .setSubText("Assistente Estudário")
            .setProgress(0, 0, true)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setContentIntent(openApp(context, taskId))
            .build()
        runCatching { NotificationManagerCompat.from(context).notify(idFor(taskId), notification) }
    }

    /** "Material pronto", com som, texto completo e o botão que leva à revisão. */
    fun showReady(context: Context, taskId: String, title: String, kind: String) {
        if (!canPost(context)) return
        val open = openApp(context, taskId)
        val body = "\"$title\" está pronto. Toque para revisar e salvar no seu edital."
        val notification = NotificationCompat.Builder(context, CHANNEL_DONE)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("$kind pronto ✨")
            .setLargeIcon(runCatching { br.com.estudario.ui.assistant.folhaBitmap(256, br.com.estudario.ui.assistant.FolhaMood.HAPPY) }.getOrNull())
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setSubText("Assistente Estudário")
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(open)
            .addAction(0, "Revisar e salvar", open)
            .build()
        runCatching { NotificationManagerCompat.from(context).notify(idFor(taskId), notification) }
    }

    fun showFailed(context: Context, taskId: String, title: String, kind: String, message: String) {
        if (!canPost(context)) return
        val body = "$message Nada foi descontado do seu saldo."
        val notification = NotificationCompat.Builder(context, CHANNEL_DONE)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Não deu para gerar ${kind.lowercase()}")
            .setLargeIcon(runCatching { br.com.estudario.ui.assistant.folhaBitmap(256, br.com.estudario.ui.assistant.FolhaMood.SAD) }.getOrNull())
            .setContentText("$title: $body")
            .setStyle(NotificationCompat.BigTextStyle().bigText("$title\n$body"))
            .setSubText("Assistente Estudário")
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ERROR)
            .setContentIntent(openApp(context, taskId))
            .build()
        runCatching { NotificationManagerCompat.from(context).notify(idFor(taskId), notification) }
    }

    fun cancel(context: Context, taskId: String) {
        runCatching { NotificationManagerCompat.from(context).cancel(idFor(taskId)) }
    }
}
