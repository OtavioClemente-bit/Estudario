package br.com.meuconcurso.data.transfer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class IncomingFileFormatTest {
    @Test fun `detects estudo including legacy schema marker`() {
        assertEquals(IncomingFileFormat.ESTUDO, IncomingFileFormat.detect("""{"format":"meu-concurso-estudo","version":2}"""))
        assertEquals(IncomingFileFormat.ESTUDO, IncomingFileFormat.detect("""{"schemaVersion":1,"competition":{}}"""))
    }

    @Test fun `detects plano and backup by root format`() {
        assertEquals(IncomingFileFormat.PLANO, IncomingFileFormat.detect("""{"format":"meu-concurso-plano","version":1}"""))
        assertEquals(IncomingFileFormat.BACKUP, IncomingFileFormat.detect("""{"format":"meu-concurso-backup","version":5}"""))
    }

    @Test fun `unknown or malformed input is not guessed`() {
        assertNull(IncomingFileFormat.detect("{}"))
        assertNull(IncomingFileFormat.detect("not-json"))
    }
}
