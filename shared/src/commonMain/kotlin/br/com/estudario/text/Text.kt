package br.com.estudario.text

/** Decomposição canônica (NFD), como java.text.Normalizer.normalize(value, Form.NFD). */
expect fun normalizeNfd(value: String): String

/** Remove acentos: NFD seguido da retirada das marcas combinantes. */
fun stripAccents(value: String): String = normalizeNfd(value).replace(Regex("\\p{M}+"), "")

/** Mesmo resultado de java.net.URLEncoder.encode(value, "UTF-8"). */
fun formUrlEncode(value: String): String = buildString {
    for (byte in value.encodeToByteArray()) {
        val b = byte.toInt() and 0xFF
        val c = b.toChar()
        when {
            c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9' || c == '.' || c == '-' || c == '*' || c == '_' -> append(c)
            c == ' ' -> append('+')
            else -> append('%').append(HEX_UPPER[b shr 4]).append(HEX_UPPER[b and 0xF])
        }
    }
}

/** Mesmo resultado de java.net.URLDecoder.decode(value, "UTF-8"). */
fun formUrlDecode(value: String): String {
    val out = ByteArray(value.length * 3)
    var size = 0
    var i = 0
    val chunk = StringBuilder()
    fun flushChunk() {
        if (chunk.isEmpty()) return
        chunk.toString().encodeToByteArray().forEach { out[size++] = it }
        chunk.clear()
    }
    while (i < value.length) {
        val c = value[i]
        when (c) {
            '+' -> { chunk.append(' '); i++ }
            '%' -> {
                require(i + 2 < value.length) { "Sequência % incompleta" }
                flushChunk()
                out[size++] = value.substring(i + 1, i + 3).toInt(16).toByte()
                i += 3
            }
            else -> { chunk.append(c); i++ }
        }
    }
    flushChunk()
    return out.copyOf(size).decodeToString()
}

private const val HEX_UPPER = "0123456789ABCDEF"
private const val HEX_LOWER = "0123456789abcdef"

fun ByteArray.toHex(): String = buildString(size * 2) {
    for (byte in this@toHex) {
        val b = byte.toInt() and 0xFF
        append(HEX_LOWER[b shr 4]).append(HEX_LOWER[b and 0xF])
    }
}
