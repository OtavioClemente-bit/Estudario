package br.com.estudario.data.ai

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.security.MessageDigest

/**
 * Lê um edital real no aparelho. O PDF não vai no repositório: o teste só roda quando ele foi
 * copiado para a pasta do app (adb push ... /sdcard/Android/data/br.com.estudario/files/edital-teste.pdf).
 */
@RunWith(AndroidJUnit4::class)
class EditalPdfTextDeviceTest {
    @Test
    fun recortaOConteudoProgramaticoDeUmEditalReal() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(context.getExternalFilesDir(null), "edital-teste.pdf")
        assumeTrue("PDF de teste ausente", file.exists())
        EditalPdfText.init(context)
        val bytes = file.readBytes()
        val started = System.currentTimeMillis()
        val result = EditalPdfText.of(bytes, MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) })!!
        val millis = System.currentTimeMillis() - started
        android.util.Log.i("EditalPdfTextTest", "pages=${result.pages} of ${result.totalPages} focused=${result.focused} chars=${result.text.length} ms=$millis")
        assertEquals("1, 51-54", result.pages)
        assertTrue(result.focused)
        assertTrue(result.text.contains("PORTUGUESA"))
    }
}
