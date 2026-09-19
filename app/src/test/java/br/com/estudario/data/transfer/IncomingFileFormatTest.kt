package br.com.estudario.data.transfer

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
        // Nome novo do app, e o antigo continua valendo para arquivos já gerados.
        assertEquals(IncomingFileFormat.ESTUDO, IncomingFileFormat.detect("""{"format":"estudario-estudo","version":2}"""))
        assertEquals(IncomingFileFormat.PLANO, IncomingFileFormat.detect("""{"format":"estudario-plano","version":1}"""))
        assertEquals(IncomingFileFormat.BACKUP, IncomingFileFormat.detect("""{"format":"estudario-backup","version":5}"""))
    }

    @Test fun `unknown or malformed input is not guessed`() {
        assertNull(IncomingFileFormat.detect("{}"))
        assertNull(IncomingFileFormat.detect("not-json"))
    }

    @Test fun `detects estudo v2 without format field`() {
        assertEquals(IncomingFileFormat.ESTUDO, IncomingFileFormat.detect("""{"version":2,"packageId":"edital-x","concurso":{"id":"c","nome":"C"},"materias":[]}"""))
    }

    @Test fun `accepts AI answers wrapped in markdown fences or extra text`() {
        val fenced = "```json\n{\"format\":\"meu-concurso-plano\",\"version\":1}\n```"
        assertEquals(IncomingFileFormat.PLANO, IncomingFileFormat.detect(fenced))
        assertEquals(IncomingFileFormat.ESTUDO, IncomingFileFormat.detect("Aqui está o arquivo:\n{\"version\":2,\"packageId\":\"p\",\"materias\":[]}\nBons estudos!"))
        assertEquals("{\"a\":1}", IncomingText.clean("\uFEFF  {\"a\":1}  "))
    }
}
