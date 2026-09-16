package br.com.meuconcurso.data.transfer

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val MAX_IMPORT_BYTES = 32 * 1024 * 1024

suspend fun ContentResolver.readIncomingFile(uri: Uri, hintedMimeType: String? = null): IncomingFilePayload = withContext(Dispatchers.IO) {
    val name = query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) cursor.getString(0) else null
    }
    val bytes = openInputStream(uri)?.use { input ->
        val buffer = ByteArray(8192)
        val output = java.io.ByteArrayOutputStream()
        while (true) {
            val count = input.read(buffer)
            if (count < 0) break
            if (output.size() + count > MAX_IMPORT_BYTES) throw FileReadException("Este arquivo é grande demais para ser importado.")
            output.write(buffer, 0, count)
        }
        output.toByteArray()
    } ?: throw FileReadException("Não foi possível abrir o arquivo.")
    IncomingFilePayload(name, getType(uri) ?: hintedMimeType, bytes.toString(Charsets.UTF_8))
}
