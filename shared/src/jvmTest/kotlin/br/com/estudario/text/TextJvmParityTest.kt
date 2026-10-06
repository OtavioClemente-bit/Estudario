package br.com.estudario.text

import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/** Garante que as versões comuns dão exatamente o mesmo resultado das APIs Java que substituem. */
class TextJvmParityTest {
    private val samples = listOf(
        "", "a", "abc", "Direito Constitucional", "Língua Portuguesa: crase, acentuação & coesão",
        "100% + 50 = ?/#[]@!\$'()*,;~", "emoji 📚 e 中文", "x".repeat(55), "y".repeat(56), "z".repeat(64), "w".repeat(1000),
    )

    @Test
    fun sha256MatchesMessageDigest() {
        val random = Random(42)
        val inputs = samples.map { it.encodeToByteArray() } + (0 until 200).map { random.nextBytes(random.nextInt(0, 300)) }
        inputs.forEach { bytes ->
            assertContentEquals(MessageDigest.getInstance("SHA-256").digest(bytes), Sha256.digest(bytes))
        }
    }

    @Test
    fun formUrlEncodingMatchesJava() {
        samples.forEach { value ->
            val encoded = URLEncoder.encode(value, "UTF-8")
            assertEquals(encoded, formUrlEncode(value))
            assertEquals(URLDecoder.decode(encoded, "UTF-8"), formUrlDecode(encoded))
        }
    }

    @Test
    fun hexMatchesFormat() {
        val bytes = Random(7).nextBytes(64)
        assertEquals(bytes.joinToString("") { "%02x".format(it) }, bytes.toHex())
    }
}
