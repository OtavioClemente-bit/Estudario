package br.com.estudario.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** O aviso de andamento vira o de "pronto" no mesmo lugar, e some ao cancelar. */
@RunWith(AndroidJUnit4::class)
class AiGenerationNotificationsTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val manager get() = context.getSystemService(NotificationManager::class.java)

    @Before fun grant() {
        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("pm grant ${context.packageName} android.permission.POST_NOTIFICATIONS").close()
        Thread.sleep(500)
        AiGenerationNotifications.createChannels(context)
    }

    @Test fun andamentoViraProntoEDepoisSome() {
        AiGenerationNotifications.showRunning(context, "content:42", "Lei de Ohm", "Material")
        Thread.sleep(300)
        val running = manager.activeNotifications.single { it.notification.extras.getString("android.title") == "Gerando material" }
        assertTrue("fica fixo enquanto gera", running.isOngoing)

        AiGenerationNotifications.showReady(context, "content:42", "Lei de Ohm", "Material")
        Thread.sleep(300)
        val ready = manager.activeNotifications.filter { it.id == running.id }
        assertEquals(1, ready.size)
        assertEquals("Material pronto ✨", ready.single().notification.extras.getString("android.title"))
        assertEquals("Revisar e salvar", ready.single().notification.actions.single().title.toString())

        AiGenerationNotifications.cancel(context, "content:42")
        Thread.sleep(300)
        assertTrue(manager.activeNotifications.none { it.id == running.id })
    }
}
