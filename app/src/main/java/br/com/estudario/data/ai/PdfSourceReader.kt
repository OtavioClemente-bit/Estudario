package br.com.estudario.data.ai

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.security.MessageDigest
import java.util.Locale

data class PdfSourceInput(
    val mimeType: String?,
    val fileName: String?,
    val stream: InputStream,
)

fun interface PdfSourceProvider {
    fun open(uri: String): PdfSourceInput
}

data class PdfSource(
    val uri: String,
    val fileName: String,
    val mimeType: String,
    val bytes: ByteArray,
    val sha256: String,
)

class PdfSourcePreflightException(
    val code: Code,
    message: String,
) : IllegalArgumentException(message) {
    enum class Code { MIME_UNSUPPORTED, TOO_LARGE, EMPTY, UNREADABLE }
}

class PdfSourceReader(
    private val provider: PdfSourceProvider,
    private val maxBytes: Long = DEFAULT_MAX_BYTES,
) {
    init {
        require(maxBytes > 0) { "PDF byte limit must be positive." }
    }

    fun read(uri: String, requestedFileName: String? = null): PdfSource {
        val input = try {
            provider.open(uri)
        } catch (error: PdfSourcePreflightException) {
            throw error
        } catch (error: Throwable) {
            throw PdfSourcePreflightException(
                PdfSourcePreflightException.Code.UNREADABLE,
                "PDF source could not be opened.",
            )
        }

        val mimeType = input.mimeType
            ?.substringBefore(';')
            ?.trim()
            ?.lowercase(Locale.US)
        if (mimeType != PDF_MIME) {
            input.stream.close()
            throw PdfSourcePreflightException(
                PdfSourcePreflightException.Code.MIME_UNSUPPORTED,
                "Only application/pdf sources are accepted.",
            )
        }

        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = ByteArrayOutputStream()
        try {
            input.stream.use { stream ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var total = 0L
                while (true) {
                    val read = stream.read(buffer)
                    if (read < 0) break
                    if (read == 0) continue
                    total += read
                    if (total > maxBytes) {
                        throw PdfSourcePreflightException(
                            PdfSourcePreflightException.Code.TOO_LARGE,
                            "PDF source exceeds the Android preflight byte limit.",
                        )
                    }
                    digest.update(buffer, 0, read)
                    bytes.write(buffer, 0, read)
                }
            }
        } catch (error: PdfSourcePreflightException) {
            throw error
        } catch (error: Throwable) {
            throw PdfSourcePreflightException(
                PdfSourcePreflightException.Code.UNREADABLE,
                "PDF source could not be read.",
            )
        }

        val result = bytes.toByteArray()
        if (result.isEmpty()) {
            throw PdfSourcePreflightException(
                PdfSourcePreflightException.Code.EMPTY,
                "PDF source must not be empty.",
            )
        }
        return PdfSource(
            uri = uri,
            fileName = requestedFileName?.trim()?.takeIf(String::isNotEmpty)
                ?: input.fileName?.trim()?.takeIf(String::isNotEmpty)
                ?: "source.pdf",
            mimeType = PDF_MIME,
            bytes = result,
            sha256 = digest.digest().toHex(),
        )
    }

    companion object {
        const val DEFAULT_MAX_BYTES: Long = 50L * 1024L * 1024L
        const val PDF_MIME: String = "application/pdf"

        fun fromContentResolver(
            resolver: ContentResolver,
            maxBytes: Long = DEFAULT_MAX_BYTES,
        ): PdfSourceReader = PdfSourceReader(
            provider = PdfSourceProvider { rawUri ->
                val uri = Uri.parse(rawUri)
                val stream = resolver.openInputStream(uri)
                    ?: throw PdfSourcePreflightException(
                        PdfSourcePreflightException.Code.UNREADABLE,
                        "PDF source could not be opened.",
                    )
                PdfSourceInput(
                    mimeType = resolver.getType(uri),
                    fileName = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                        ?.use { cursor ->
                            if (cursor.moveToFirst()) cursor.getString(0) else null
                        },
                    stream = stream,
                )
            },
            maxBytes = maxBytes,
        )
    }
}

private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
