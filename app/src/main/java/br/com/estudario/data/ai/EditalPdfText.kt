package br.com.estudario.data.ai

import android.content.Context
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper

/**
 * Lê o texto de cada página do PDF no próprio celular e escolhe o que vai para a IA.
 *
 * Roda no aparelho porque o servidor tem limite de CPU por chamada e um edital de centenas de
 * páginas não caberia lá. O resultado fica em memória pelo hash do arquivo: as duas chamadas do
 * mesmo pedido (criar e prender o PDF) mandam exatamente o mesmo texto.
 */
object EditalPdfText {
    @Volatile private var initialized = false
    private val cache = object : LinkedHashMap<String, AiSourceText?>(4, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, AiSourceText?>?) = size > 3
    }

    fun init(context: Context) {
        if (initialized) return
        runCatching { PDFBoxResourceLoader.init(context.applicationContext) }
        initialized = true
    }

    /** null quando não dá para ler o texto (PDF só de imagem, protegido): aí vai o PDF, como antes. */
    fun of(bytes: ByteArray, sha256: String): AiSourceText? = synchronized(cache) {
        if (cache.containsKey(sha256)) return cache[sha256]
        val result = runCatching { EditalSectionFinder.select(pages(bytes)) }.getOrNull()
            ?.takeIf { it.text.count(Char::isLetter) > 500 }
        cache[sha256] = result
        result
    }

    private fun pages(bytes: ByteArray): List<String> = PDDocument.load(bytes).use { document ->
        val stripper = PDFTextStripper()
        (1..document.numberOfPages).map { page ->
            stripper.startPage = page
            stripper.endPage = page
            stripper.getText(document)
        }
    }
}
