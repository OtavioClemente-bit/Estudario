package br.com.estudario.data.catalog

import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.InitialSetupSnapshotCodec
import br.com.estudario.domain.setup.SyllabusMethod
import java.io.File
import java.nio.file.Files
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class ExamCatalogTest {
    private val pmmg = ExamCatalogEntry(
        id = "pmmg-cfsd-qppm-2025", shortName = "PMMG", agency = "Polícia Militar de Minas Gerais",
        role = "Soldado (CFSd QPPM)", board = "PMMG (CRS)", year = 2025,
        searchNorm = "pmmg | policia militar de minas gerais | soldado | pm mg | policia militar mg",
        subjectCount = 5, topicCount = 54, readyTopicCount = 53,
    )
    private val pf = ExamCatalogEntry(
        id = "pf-agente-2025", shortName = "Polícia Federal", agency = "Polícia Federal",
        role = "Agente de Polícia Federal", board = "Cebraspe", year = 2025, searchNorm = "pf | agente pf",
    )
    private val prf = ExamCatalogEntry(
        id = "prf-policial-rodoviario-federal-2021", shortName = "PRF", agency = "Polícia Rodoviária Federal",
        role = "Policial Rodoviário Federal", board = "Cebraspe", year = 2021, searchNorm = "prf | policia rodoviaria",
    )
    private val trt = ExamCatalogEntry(
        id = "trt3-2022--analista-judiciaria", shortName = "TRT 3ª Região", agency = "Tribunal Regional do Trabalho da 3ª Região (MG)",
        role = "Analista Judiciário, Área Judiciária", board = "FUMARC", year = 2022, searchNorm = "trt | trt mg | analista judiciario",
    )
    private val all = listOf(pf, pmmg, prf, trt)

    @Test fun `acha pelo jeito que as pessoas digitam`() {
        assertEquals(pmmg, ExamCatalogSearch.search(all, "pmmg").first())
        assertEquals(pmmg, ExamCatalogSearch.search(all, "PM MG").first())
        assertEquals(pmmg, ExamCatalogSearch.search(all, "polícia militar mg").first())
        assertEquals(pmmg, ExamCatalogSearch.search(all, "soldado").first())
        assertEquals(pf, ExamCatalogSearch.search(all, "policia federal").first())
        assertEquals(pf, ExamCatalogSearch.search(all, "agente pf").first())
        assertEquals(prf, ExamCatalogSearch.search(all, "prf").first())
        assertEquals(trt, ExamCatalogSearch.search(all, "trt analista").first())
        assertEquals(trt, ExamCatalogSearch.search(all, "tribunal regional do trabalho").first())
    }

    @Test fun `todas as palavras precisam bater e busca vazia nao lista nada`() {
        assertTrue(ExamCatalogSearch.search(all, "pmmg analista").isEmpty())
        assertTrue(ExamCatalogSearch.search(all, "receita federal").isEmpty())
        assertTrue(ExamCatalogSearch.search(all, "   ").isEmpty())
        assertTrue(ExamCatalogSearch.search(all, "concurso edital").isEmpty())
    }

    @Test fun `sigla exata vem antes de quem so menciona a palavra`() {
        val results = ExamCatalogSearch.search(all, "federal")
        assertEquals(setOf(pf, prf), results.toSet())
    }

    @Test fun `percentual pronto e detalhes`() {
        assertEquals(98, pmmg.readyPercent)
        assertEquals("Polícia Militar de Minas Gerais · PMMG (CRS) · 2025", pmmg.details)
        assertEquals("Cebraspe · 2025", pf.details)
    }

    @Test fun `lista vem da rede, fica em arquivo e serve sem internet`() = runBlocking<Unit> {
        val dir = Files.createTempDirectory("catalog").toFile()
        val cache = File(dir, "exam_catalog.json")
        val body = """[{"id":"pmmg-cfsd-qppm-2025","short_name":"PMMG","agency":"Polícia Militar de Minas Gerais","role":"Soldado","board":null,"year":2025,"search_norm":"pmmg","subject_count":5,"topic_count":54,"ready_topic_count":50,"extra":1}]"""
        val online = ExamCatalogRepository("https://x.supabase.co", "key", cache, http = { url ->
            assertTrue(url.contains("/rest/v1/exam_catalog?select="))
            assertTrue(url.contains("status=eq.PUBLISHED"))
            body
        })
        assertEquals("PMMG", online.entries().single().shortName)
        assertTrue(cache.exists())

        val offline = ExamCatalogRepository("https://x.supabase.co", "key", cache, http = { error("sem internet") })
        assertEquals("PMMG", offline.entries(force = true).single().shortName)

        val empty = ExamCatalogRepository("https://x.supabase.co", "key", File(dir, "nada.json"), http = { error("sem internet") })
        try {
            empty.entries()
            fail("sem cópia e sem rede deveria avisar")
        } catch (error: ExamCatalogException) {
            assertTrue(error.message!!.contains("internet"))
        }
        dir.deleteRecursively()
    }

    @Test fun `materias do edital vem limpas e edital sumido avisa`() = runBlocking<Unit> {
        val repo = ExamCatalogRepository("https://x.supabase.co", "key", null, http = { url ->
            if (url.contains("id=eq.pmmg")) """[{"subjects":[{"name":" Português ","topics":["Crase."," ",""]},{"name":"Vazia","topics":[]}]}]""" else "[]"
        })
        val subjects = repo.subjects("pmmg-cfsd-qppm-2025")
        assertEquals(listOf(ExamCatalogSubject("Português", listOf("Crase."))), subjects)
        try {
            repo.subjects("outro")
            fail("edital inexistente deveria avisar")
        } catch (error: ExamCatalogException) {
            assertTrue(error.message!!.contains("não está mais disponível"))
        }
    }

    @Test fun `snapshot guarda o edital do catalogo e le os antigos sem ele`() {
        val snapshot = InitialSetupSnapshot(competitionName = "PMMG", role = "Soldado", syllabusMethod = SyllabusMethod.CATALOG, catalogExamId = "pmmg-cfsd-qppm-2025")
        val decoded = InitialSetupSnapshotCodec.decode(InitialSetupSnapshotCodec.encode(snapshot))
        assertEquals("pmmg-cfsd-qppm-2025", decoded.catalogExamId)
        assertEquals(SyllabusMethod.CATALOG, decoded.syllabusMethod)

        val old = InitialSetupSnapshotCodec.encode(InitialSetupSnapshot(competitionName = "X")).substringBeforeLast('|')
        assertNull(InitialSetupSnapshotCodec.decode(old).catalogExamId)
        assertEquals("X", InitialSetupSnapshotCodec.decode(old).competitionName)
    }
}
