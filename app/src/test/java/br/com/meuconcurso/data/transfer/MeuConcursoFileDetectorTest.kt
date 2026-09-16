package br.com.meuconcurso.data.transfer

import org.junit.Assert.*
import org.junit.Test

class MeuConcursoFileDetectorTest {
    private val detector = MeuConcursoFileDetector()
    @Test fun detectsPlanWithGenericMime() = assertEquals(FileDetectionResult.Match(MeuConcursoFileFormat.PLANO), detector.detect(IncomingFilePayload("Plano.plano", "application/octet-stream", """{"format":"meu-concurso-plano","version":1}""")))
    @Test fun rejectsExtensionMismatch() = assertTrue(detector.detect(IncomingFilePayload("Plano.plano", "application/json", """{"format":"meu-concurso-backup","version":5}""")) is FileDetectionResult.ExtensionMismatch)
    @Test fun detectsSimpleEstudo() = assertEquals(FileDetectionResult.Match(MeuConcursoFileFormat.ESTUDO), detector.detect(IncomingFilePayload("x.estudo", "text/plain", """{"version":2,"packageId":"p","competition":"C"}""")))
    @Test fun rejectsInvalidJson() = assertEquals(FileDetectionResult.InvalidJson, detector.detect(IncomingFilePayload("x.plano", null, "{")))
}
