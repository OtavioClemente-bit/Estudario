package br.com.estudario.data.ai

import java.io.ByteArrayInputStream
import java.security.MessageDigest
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

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }
}
