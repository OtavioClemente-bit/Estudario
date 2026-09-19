package br.com.estudario.data.local

import br.com.estudario.domain.PriorityEvidence
import br.com.estudario.domain.PriorityEvidenceType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PriorityEvidenceCodecTest {
    @Test
    fun codecRoundTripsEvidenceAndIgnoresMalformedJson() {
        val input = listOf(PriorityEvidence(PriorityEvidenceType.OFFICIAL_WEIGHT, "Peso 2", 2.0))
        assertEquals(input, PriorityEvidenceCodec.decode(PriorityEvidenceCodec.encode(input)))
        assertTrue(PriorityEvidenceCodec.decode("{broken").isEmpty())
        assertTrue(PriorityEvidenceCodec.decode("[]").isEmpty())
    }

    @Test
    fun missingEvidenceValueRemainsNull() {
        val decoded = PriorityEvidenceCodec.decode("[{\"type\":\"BOARD_HISTORY\",\"description\":\"Provas anteriores\"}]")
        assertEquals(listOf(PriorityEvidence(PriorityEvidenceType.BOARD_HISTORY, "Provas anteriores")), decoded)
    }
}
