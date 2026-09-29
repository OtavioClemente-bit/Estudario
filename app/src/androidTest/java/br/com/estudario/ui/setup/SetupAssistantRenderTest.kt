package br.com.estudario.ui.setup

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import br.com.estudario.ui.prompt.BigValueSlider
import br.com.estudario.ui.prompt.WeekHoursPicker
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

/** Desenha uma etapa da configuração no estilo de assistente e salva a imagem para conferência. */
class SetupAssistantRenderTest {
    @get:Rule val rule = createComposeRule()

    @Test
    fun rendersAssistantStep() {
        rule.setContent {
            EstudarioTheme(darkTheme = true) {
                Box(Modifier.testTag("page").width(400.dp).height(860.dp).background(MaterialTheme.colorScheme.background)) {
                    SetupPage(
                        eyebrow = "Seu ritmo",
                        title = "Quanto tempo cabe na sua semana?",
                        description = "Arraste cada barra para marcar o tempo líquido do dia, já sem pausas. Zero é folga.",
                        icon = Icons.Outlined.Schedule,
                        bottom = { SetupPrimaryButton("Continuar", {}) },
                    ) {
                        WeekHoursPicker(listOf(120, 120, 180, 120, 90, 240, 0), { _, _ -> }, maxMinutes = 720)
                        BigValueSlider(50, 15..120, 5, { "$it min" }, {}, caption = "Tamanho-base de cada tarefa", quickValues = listOf(25, 45, 50, 60, 90))
                    }
                }
            }
        }
        rule.waitForIdle()
        val bitmap = rule.onNodeWithTag("page").captureToImage().asAndroidBitmap()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        File(context.filesDir, "setup-assistant.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
