package br.com.estudario.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import br.com.estudario.ui.theme.EstudarioTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

/** Desenha um texto de estudo completo e salva a imagem, para conferência visual do leitor. */
class StudyMarkdownRenderTest {
    @get:Rule val rule = createComposeRule()

    private val sample = """
        # 2. Juros compostos

        No regime **composto**, os juros de cada período entram no capital do período seguinte. Veja o *contraste* com o simples:

        | Regime | Montante | Cresce |
        |---|---|---|
        | Simples | ${'$'}${'$'}M = C(1 + i\,t)${'$'}${'$'} | Linear |
        | Composto | ${'$'}${'$'}M = C(1 + i)^t${'$'}${'$'} | Exponencial |

        Fórmula geral:

        ${'$'}${'$'}
        M = C \cdot (1 + i)^{t} \qquad i = \frac{r}{100}
        ${'$'}${'$'}

        1. Converta a taxa para decimal.
        2. Use o **mesmo período** para taxa e tempo.
        3. Arredonde só no final.

        > Pegadinha: a banca troca taxa mensal por anual no enunciado.

        Código de exemplo: `M = C * (1 + i) ** t`
    """.trimIndent()

    @Test
    fun rendersTablesFormulasAndLists() {
        rule.setContent {
            EstudarioTheme(darkTheme = true) {
                Column(Modifier.testTag("page").background(MaterialTheme.colorScheme.background).padding(16.dp)) {
                    studyBlocks(sample).forEach { block -> StudyMarkdown(block, Modifier.fillMaxWidth().padding(vertical = 6.dp)) }
                }
            }
        }
        rule.waitForIdle()
        Thread.sleep(1500) // JLatexMath desenha as fórmulas em segundo plano.
        rule.waitForIdle()
        val bitmap = rule.onNodeWithTag("page").captureToImage().asAndroidBitmap()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        File(context.filesDir, "study-markdown.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
