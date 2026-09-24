package br.com.estudario.data.ai

import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.io.File
import java.nio.file.Files
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PdfSourceReaderTest {
    @Test
    fun readsPdfBytesAndComputesSha256BeforeUpload() {
        val bytes = "%PDF-test".toByteArray()
        val reader = PdfSourceReader(
            provider = PdfSourceProvider {
                PdfSourceInput("application/pdf", "edital.pdf", ByteArrayInputStream(bytes))
            },
        )

        val source = reader.read("content://edital")

        assertArrayEquals(bytes, source.bytes)
        assertEquals("edital.pdf", source.fileName)
        assertEquals("application/pdf", source.mimeType)
        assertEquals(sha256(bytes), source.sha256)
    }

    @Test
    fun rejectsNonPdfMimeBeforeNetworkCalls() {
        val reader = PdfSourceReader(
            provider = PdfSourceProvider {
                PdfSourceInput("text/plain", "edital.pdf", ByteArrayInputStream(ByteArray(4)))
            },
        )

        assertThrows(PdfSourcePreflightException::class.java) {
            reader.read("content://edital")
        }
    }

    @Test
    fun rejectsBytesAboveAndroidPreflightLimit() {
        val reader = PdfSourceReader(
            provider = PdfSourceProvider {
                PdfSourceInput("application/pdf", "edital.pdf", ByteArrayInputStream(ByteArray(5)))
            },
            maxBytes = 4,
        )

        assertThrows(PdfSourcePreflightException::class.java) {
            reader.read("content://edital")
        }
    }

    @Test
    fun persistsPdfSnapshotPrivatelyAndReadsTheSameFingerprint() {
        val bytes = "%PDF-durable".toByteArray()
        val source = PdfSource("content://edital", "edital.pdf", "application/pdf", bytes, sha256(bytes))
        val directory = Files.createTempDirectory("ai-source-test").toFile()
        try {
            val store = FilePdfSourceSnapshotStore(directory)
            val path = store.save(source)
            val restored = store.read(path, source.fileName)

            assertEquals(source.mimeType, restored.mimeType)
            assertEquals(source.sha256, restored.sha256)
            assertArrayEquals(source.bytes, restored.bytes)
        } finally {
            directory.deleteRecursively()
        }
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }
}
