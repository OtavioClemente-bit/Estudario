package br.com.estudario.data.ai

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.test.core.app.ApplicationProvider
import br.com.estudario.data.remote.AiAccessTokenProvider
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiSyllabusPreparationDeviceTest {
    @Test
    fun actualPdfIsExtractedAndCheckedWithoutRemoteCallsOrRequestPersistence() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        EditalPdfText.init(context)
        val pdf = PdfDocument()
        val page = pdf.startPage(PdfDocument.PageInfo.Builder(600, 800, 1).create())
        val lines = listOf(
            "EDITAL DRH CRS 10/2024",
            "POLICIA MILITAR DE MINAS GERAIS",
            "Curso de Formacao de Soldados CFSd 2025",
            "Concurso publico para admissao ao curso de formacao.",
            "CONTEUDO PROGRAMATICO",
            "1. Portugues: compreensao de textos, gramatica e sintaxe.",
            "2. Matematica: operacoes, equacoes e funcoes.",
            "3. Direito Constitucional: direitos e garantias fundamentais.",
        )
        lines.forEachIndexed { index, line -> page.canvas.drawText(line, 20f, 40f + index * 25f, Paint().apply { textSize = 14f }) }
        pdf.finishPage(page)
        val output = ByteArrayOutputStream()
        pdf.writeTo(output)
        pdf.close()
        val directory = File(context.cacheDir, "syllabus-preflight-${System.nanoTime()}")
        try {
            var savedRequests = 0
            val repository = DefaultAiSyllabusRepository(
                api = object : AiApiClient {
                    override suspend fun createOrGetJob(idempotencyKey: String, source: AiSourceMetadata, sourceReady: Boolean): AiCreateJob = error("preflight cannot reserve quota")
                    override suspend fun uploadSource(target: AiUploadTarget, source: PdfSource) = error("preflight cannot upload")
                    override suspend fun processJob(jobId: String): AiJobStatus = error("preflight cannot process")
                    override suspend fun getJob(jobId: String, timeoutMillis: Long?): AiJob = error("preflight cannot contact jobs")
                    override suspend fun awaitJob(jobId: String, policy: AiPollingPolicy, sleeper: suspend (Long) -> Unit, clockMillis: () -> Long): AiJob = error("preflight cannot poll")
                },
                sourceReader = PdfSourceReader(PdfSourceProvider { PdfSourceInput("application/pdf", "pmmg.pdf", ByteArrayInputStream(output.toByteArray())) }),
                requestStore = object : AiJobRequestStore {
                    override suspend fun save(value: PersistedAiJobRequest) { savedRequests++ }
                    override suspend fun get(requestId: String): PersistedAiJobRequest? = null
                    override suspend fun list(): List<PersistedAiJobRequest> = emptyList()
                },
                accessTokenProvider = AiAccessTokenProvider { error("preflight cannot authenticate") },
                sourceSnapshots = FilePdfSourceSnapshotStore(directory),
            )
            val prepared = repository.prepare("content://local/pmmg", "pmmg.pdf")
            assertTrue(prepared.pages.single().contains("MILITAR"))
            assertTrue(File(directory, prepared.snapshotPath).exists())
            assertEquals(SyllabusPreflightKind.VALID, prepared.preflight(AiSyllabusPreferences("PMMG", "Soldado")).kind)
            assertEquals(SyllabusPreflightKind.VALID_WITH_WARNING, prepared.preflight(AiSyllabusPreferences("TRT 3 REGIAO TI", "Soldado")).kind)
            assertEquals(0, savedRequests)
        } finally {
            directory.deleteRecursively()
        }
    }
}
