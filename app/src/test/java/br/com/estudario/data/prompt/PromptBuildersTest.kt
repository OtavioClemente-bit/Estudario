package br.com.estudario.data.prompt

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.ContentOriginType
import br.com.estudario.data.local.Priority
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.transfer.EstudoPackageParser
import br.com.estudario.data.transfer.IncomingFileFormat
import br.com.estudario.data.transfer.IncomingText
import br.com.estudario.data.transfer.planner.StudyPlanCodec
import br.com.estudario.domain.planner.PlanPriority
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class PromptBuildersTest {
    private val competition = CompetitionEntity(id = 1, name = "TRT-3 — Analista de TI", externalId = "concurso-trt3")
    private val subject = SubjectEntity(id = 10, competitionId = 1, name = "Segurança da Informação", position = 2, externalId = null)
    private val parent = TopicEntity(id = 100, subjectId = 10, title = "Criptografia", position = 1, priority = Priority.ALTA, externalId = "seg-cripto")
    private val child = TopicEntity(id = 101, subjectId = 10, parentTopicId = 100, title = "Hash \"SHA\" e MAC", position = 0, contentOriginType = ContentOriginType.DIDACTIC_SUBDIVISION)
    private val other = TopicEntity(id = 102, subjectId = 10, title = "Controle de acesso", position = 2)

    @Test fun editalPromptRequestsEvidenceBasedPriorityAssessment() {
        val prompt = EditalPromptBuilder.build(EditalPromptOptions(competitionName = "TRT-3"))
        val officialQuestions = prompt.indexOf("quantidade oficial de questões")
        val officialWeight = prompt.indexOf("peso oficial")
        val officialScore = prompt.indexOf("pontuação oficial")
        val elimination = prompt.indexOf("critério eliminatório")
        val distribution = prompt.indexOf("distribuição oficial")
        val history = prompt.indexOf("histórico fornecido")
        assertTrue(officialQuestions >= 0)
        assertTrue(officialQuestions < officialWeight)
        assertTrue(officialWeight < officialScore)
        assertTrue(officialScore < elimination)
        assertTrue(elimination < distribution)
        assertTrue(distribution < history)
        assertTrue(prompt.contains("priorityAssessment"))
        assertTrue(prompt.contains("score inteiro de 0 a 100"))
        assertTrue(prompt.contains("confidence entre 0.0 e 1.0"))
        assertTrue(prompt.contains("Não invente estatísticas"))
    }

    @Test fun contentPromptPreservesPriorityContextWithoutRecalculatingIt() {
        val prompt = ContentPromptBuilder.build(
            competition,
            subject,
            listOf(parent.copy(assessedPriorityScore = 75, hasAssessedPriority = true)),
            setOf(parent.id),
            ContentPromptOptions(),
        )
        assertTrue(prompt.contains("priorityAssessment"))
        assertTrue(prompt.contains("preserve"))
        assertTrue(prompt.contains("não recalcul"))
    }

    @Test fun `content skeleton is a valid estudo package that points to existing topics`() {
        val prompt = ContentPromptBuilder.build(competition, subject, listOf(parent, child, other), setOf(child.id), ContentPromptOptions(style = QuestionStyle.TRUE_FALSE, board = "FCC"))
        val json = IncomingText.clean(prompt.substringAfter("ESTRUTURA:"))
        assertEquals(IncomingFileFormat.ESTUDO, IncomingFileFormat.detect(json))
        val plan = EstudoPackageParser.parse(json)
        assertEquals("concurso-trt3", plan.competitionId)
        val parsedSubject = plan.subjects.single()
        assertEquals("materia-10", parsedSubject.id)
        assertEquals(2, parsedSubject.position)
        val parsedParent = parsedSubject.topics.single()
        assertEquals("seg-cripto", parsedParent.id)
        assertEquals(Priority.ALTA, parsedParent.priority)
        assertTrue(parsedParent.questions.isEmpty())
        val parsedChild = parsedParent.children.single()
        assertEquals("topico-101", parsedChild.id)
        assertEquals("Hash \"SHA\" e MAC", parsedChild.title)
        assertEquals(ContentOriginType.DIDACTIC_SUBDIVISION, parsedChild.originType)
        assertEquals(listOf("C", "E"), parsedChild.questions.single().options.map { it.key })
        assertTrue(parsedChild.theories.isNotEmpty() && parsedChild.summaries.size == 2)
        assertFalse(prompt.contains("Controle de acesso"))
    }

    @Test fun `unselected blocks are left out of the skeleton`() {
        val prompt = ContentPromptBuilder.build(competition, subject, listOf(parent, child, other), setOf(other.id, parent.id), ContentPromptOptions(blocks = setOf(ContentBlock.QUESTIONS)))
        val plan = EstudoPackageParser.parse(IncomingText.clean(prompt.substringAfter("ESTRUTURA:")))
        val topics = plan.subjects.single().topics
        assertEquals(listOf("seg-cripto", "topico-102"), topics.map { it.id })
        assertTrue(topics.all { it.theories.isEmpty() && it.summaries.isEmpty() && it.questions.size == 2 })
        assertFalse(prompt.contains("\"summary\""))
    }

    @Test fun `one mixed question has one skeleton example`() {
        val prompt = ContentPromptBuilder.build(
            competition,
            subject,
            listOf(parent),
            setOf(parent.id),
            ContentPromptOptions(blocks = setOf(ContentBlock.QUESTIONS), questionCount = 1),
        )

        val plan = EstudoPackageParser.parse(IncomingText.clean(prompt.substringAfter("ESTRUTURA:")))
        assertEquals(1, plan.subjects.single().topics.single().questions.size)
    }

    @Test fun `plan prompt carries the exact ids and availability`() {
        val subjects = listOf(PlanSubjectInfo("materia-10", "Segurança", listOf(PlanTopicInfo("topico-101", "Hash", true)), answered = 20, accuracyPercent = 55))
        val prompt = PlanPromptBuilder.build("concurso-trt3", "TRT-3", subjects, PlanPromptOptions(startDate = LocalDate.of(2026, 9, 21), dayMinutes = listOf(60, 60, 60, 60, 60, 0, 0), priorities = mapOf("materia-10" to PlanPriority.CRITICAL)))
        assertTrue(prompt.contains("\"externalId\": \"concurso-trt3\""))
        assertTrue(prompt.contains("{ \"dia\": 6, \"minutos\": 0, \"indisponivel\": true }"))
        assertTrue(prompt.contains("topico-101 → Hash [ok]"))
        assertTrue(prompt.contains("\"topicoExternalId\": \"topico-101\""))
        assertTrue(prompt.contains("\"topicoNome\": \"Hash\""))
        assertTrue(prompt.contains("55% de acerto"))
        assertTrue(prompt.contains("\"prioridade\": \"CRITICAL\""))
    }

    @Test fun `knowledge content prompt requires official web research and records consulted sources`() {
        val prompt = ContentPromptBuilder.build(
            competition,
            subject,
            listOf(parent),
            setOf(parent.id),
            ContentPromptOptions(source = MaterialSource.AI_KNOWLEDGE),
        )

        assertTrue(prompt.contains("Pesquise na internet"))
        assertTrue(prompt.contains("fontes oficiais"))
        assertTrue(prompt.contains("fontes complementares confiáveis"))
        assertTrue(prompt.contains("identifique-as como complementares"))
        assertTrue(prompt.contains("Não deixe uma matéria inteira sem conteúdo"))
        assertTrue(prompt.contains("Se você não puder navegar na internet, não gere o arquivo .estudo"))
        assertTrue(prompt.contains("não gere JSON de importação"))
        assertTrue(prompt.contains("omita apenas as afirmações específicas sem suporte confiável"))
        assertTrue(prompt.contains("Norma ou regra vigente só pode ser afirmada com fonte oficial atualizada"))
        assertTrue(prompt.contains("Fontes consultadas"))
        assertTrue(prompt.contains("Fontes oficiais/primárias"))
        assertTrue(prompt.contains("Fontes complementares"))
        assertTrue(prompt.contains("URL exata"))
        assertTrue(prompt.contains("Se você não puder navegar na internet, não gere o arquivo .estudo"))
        assertTrue(prompt.contains("não gere JSON de importação"))
        assertFalse(prompt.contains("Anexe o material para eu continuar"))
    }

    @Test fun `knowledge mode refuses to create unverifiable material without web research`() {
        val prompt = ContentPromptBuilder.build(
            competition,
            subject,
            listOf(parent),
            setOf(parent.id),
            ContentPromptOptions(source = MaterialSource.AI_KNOWLEDGE),
        )

        assertTrue(prompt.contains("Se você não puder navegar na internet, não gere o arquivo .estudo"))
        assertTrue(prompt.contains("não gere JSON de importação"))
        assertFalse(prompt.contains("Entregue o arquivo assim mesmo"))
        assertFalse(prompt.contains("A ÚNICA razão para não gerar o arquivo"))
    }

    @Test fun `content prompt forbids unsupported content from missing attached evidence`() {
        val prompt = ContentPromptBuilder.build(
            competition,
            subject,
            listOf(parent),
            setOf(parent.id),
            ContentPromptOptions(source = MaterialSource.ATTACHED),
        )

        assertTrue(prompt.contains("páginas, artigos ou seções"))
        assertTrue(prompt.contains("Não invente nem complete lacunas"))
        assertTrue(prompt.contains("fontes complementares confiáveis"))
        assertTrue(prompt.contains("omita apenas as afirmações específicas sem suporte confiável"))
        assertFalse(prompt.contains("Anexe o material para eu continuar"))
    }

    @Test fun `question generation searches actual bank exams and verifies answer keys first`() {
        val prompt = ContentPromptBuilder.build(
            competition,
            subject,
            listOf(parent),
            setOf(parent.id),
            ContentPromptOptions(blocks = setOf(ContentBlock.QUESTIONS), questionCount = 4, board = "FCC"),
        )

        assertTrue(prompt.contains("Pesquise na internet primeiro por questões reais de provas anteriores"))
        assertTrue(prompt.contains("caderno oficial da prova"))
        assertTrue(prompt.contains("gabarito oficial definitivo"))
        assertTrue(prompt.contains("questionSourceType \"REAL\""))
        assertTrue(prompt.contains("questionSourceType \"REAL_ADAPTED\""))
        assertTrue(prompt.contains("sourceId e sourceUrl"))
        assertTrue(prompt.contains("Se não localizar questões reais reutilizáveis, complete a quantidade com questões autorais"))
        assertTrue(prompt.contains("não invente banca, órgão, ano, prova, questão, gabarito ou URL"))
        assertTrue(prompt.contains("direito de reprodução"))
    }

    @Test fun `question difficulty uses a consistent rubric for easy medium and hard`() {
        val prompt = ContentPromptBuilder.build(
            competition,
            subject,
            listOf(parent),
            setOf(parent.id),
            ContentPromptOptions(blocks = setOf(ContentBlock.QUESTIONS), difficulty = QuestionDifficulty.MIXED),
        )

        assertTrue(prompt.contains("FACIL: cobrança direta"))
        assertTrue(prompt.contains("MEDIA: aplicação de regra"))
        assertTrue(prompt.contains("DIFICIL: combinação de conceitos"))
        assertTrue(prompt.contains("A classificação de dificuldade é estimada"))
    }

    @Test fun `question schema provides per-question provenance fields without fake defaults`() {
        val prompt = ContentPromptBuilder.build(
            competition,
            subject,
            listOf(parent),
            setOf(parent.id),
            ContentPromptOptions(blocks = setOf(ContentBlock.QUESTIONS)),
        )

        assertTrue(prompt.contains("\"banca\": \"\", \"orgao\": \"\", \"ano\": 0, \"origem\": \"\""))
        assertTrue(prompt.contains("\"questionSourceType\": \"AUTHORIAL\", \"sourceId\": null, \"sourceUrl\": null"))
        assertFalse(prompt.contains("\"orgao\": \"ORGAO\""))
        assertFalse(prompt.contains("\"banca\": \"BANCA\""))
    }

    @Test fun `edital prompt forbids filling missing syllabus items from memory`() {
        val prompt = EditalPromptBuilder.build(EditalPromptOptions(source = EditalSource.ATTACH_PDF))

        assertTrue(prompt.contains("Não acrescente matérias ou tópicos com base no seu conhecimento geral"))
        assertTrue(prompt.contains("Se a fonte oficial consultada não comprovar um item, não o inclua"))
        assertTrue(prompt.contains("não invente nem complete lacunas"))
    }

    @Test fun `edital prompt treats attachment as optional and recommends official pdf`() {
        val prompt = EditalPromptBuilder.build(EditalPromptOptions(source = EditalSource.ATTACH_PDF))

        assertTrue(prompt.contains("opcional"))
        assertTrue(prompt.contains("recomendado"))
        assertTrue(prompt.contains("pesquise fontes oficiais"))
        assertTrue(prompt.contains("Banca e ano são opcionais"))
        assertTrue(prompt.contains("não recuse o edital inteiro"))
        assertFalse(prompt.contains("deve estar ANEXADO"))
    }

    @Test fun `edital without attachment searches official source and keeps json fallback`() {
        val prompt = EditalPromptBuilder.build(
            EditalPromptOptions(competitionName = "TRT-3", role = "Analista de TI"),
        )

        assertTrue(prompt.contains("MODO SEM ANEXO"))
        assertTrue(prompt.contains("Pesquise na internet o edital oficial"))
        assertTrue(prompt.contains("Não recuse apenas porque não há PDF anexado"))
        assertTrue(prompt.contains("Se não conseguir criar um arquivo para download, responda com JSON puro"))
        assertFalse(prompt.contains("Só não gere o arquivo se o edital não tiver chegado"))
        assertFalse(prompt.contains("Não gere o arquivo se o material de entrada obrigatório não chegou"))
    }

    @Test fun `edital prompt prioritizes the attachment when one was selected`() {
        val prompt = EditalPromptBuilder.build(EditalPromptOptions(source = EditalSource.ATTACH_PDF, attachmentProvided = true))

        assertTrue(prompt.contains("O PDF oficial foi ANEXADO"))
        assertTrue(prompt.contains("Priorize o anexo"))
        assertFalse(prompt.contains("é opcional, mas recomendado"))
    }

    @Test fun `plan prompt cannot add subjects or topics absent from app data`() {
        val prompt = PlanPromptBuilder.build(
            "concurso-trt3",
            "TRT-3",
            listOf(PlanSubjectInfo("materia-10", "Segurança", listOf(PlanTopicInfo("topico-101", "Hash", false)), 0, null)),
            PlanPromptOptions(),
        )

        assertTrue(prompt.contains("Não crie matérias, tópicos, IDs ou dados factuais"))
        assertTrue(prompt.contains("use somente as matérias e os tópicos listados"))
    }

    @Test fun `plan prompt with no app subjects cannot fall back to template placeholders`() {
        val prompt = PlanPromptBuilder.build("concurso-trt3", "TRT-3", emptyList(), PlanPromptOptions())

        assertTrue(prompt.contains("não gere arquivo .plano"))
        assertFalse(prompt.contains("Crie um arquivo .plano"))
        assertFalse(prompt.contains("ID-DA-MATERIA"))
        assertFalse(prompt.contains("\"Matéria\""))
    }

    @Test fun `plan prompt without topic data cannot invent topic names`() {
        val prompt = PlanPromptBuilder.build(
            "concurso-trt3",
            "TRT-3",
            listOf(PlanSubjectInfo("materia-10", "Segurança", emptyList(), 0, null)),
            PlanPromptOptions(includeTopics = false),
        )

        assertTrue(prompt.contains("Os tópicos não foram fornecidos pelo app"))
        assertTrue(prompt.contains("topicoNome vazio"))
        assertTrue(prompt.contains("\"topicoNome\": \"\""))
        assertFalse(prompt.contains("\"topicoNome\": \"...\""))
        assertFalse(prompt.contains("descreva o assunto em topicoNome"))
    }

    @Test fun `plans with simple ids generated by AI are accepted`() {
        val raw = """
            {"planId":"plano-x","concurso":{"externalId":"c","nome":"C"},"nome":"P","objetivo":"O","dataInicio":"2026-09-21","dataProva":null,
             "configuracao":{"modo":"SIMPLE","dias":[{"dia":1,"minutos":60,"indisponivel":false}],"questoesSemanais":10,"discursivasMensais":0},
             "prioridades":[],"fasesAnuais":[],"planosMensais":[{"id":"mes-01","mes":"2026-09","foco":"f"}],"planosSemanais":[],
             "tarefas":[{"id":"tarefa-001","data":"2026-09-21","tipo":"THEORY","minutos":60,"prioridade":"HIGH","dependencias":[]},
                        {"id":"tarefa-002","data":"2026-09-22","tipo":"QUESTIONS","minutos":30,"prioridade":"HIGH","dependencias":["tarefa-001"]}]}
        """.trimIndent()
        assertEquals(IncomingFileFormat.PLANO, IncomingFileFormat.detect(raw))
        val plan = StudyPlanCodec().decode(raw)
        assertEquals(plan.tasks[0].id, plan.tasks[1].dependencies.single())
        assertEquals(plan, StudyPlanCodec().decode(raw))
    }

    @Test fun `edital prompt asks for the attachment instead of pasted text`() {
        val attach = EditalPromptBuilder.build(EditalPromptOptions(competitionName = "TRT-3", role = "Analista TI", board = "FCC", year = "2026"))
        assertTrue(attach.contains("PDF oficial é opcional, mas recomendado"))
        assertTrue(attach.contains("pesquise fontes oficiais"))
        assertFalse(attach.contains("Anexe o edital para eu continuar"))
        assertFalse(attach.contains("TEXTO DO EDITAL:"))
        assertTrue(attach.contains("\"id\": \"concurso-trt-3-analista-ti-2026\""))
        val paste = EditalPromptBuilder.build(EditalPromptOptions(source = EditalSource.PASTE_TEXT))
        assertTrue(paste.endsWith("[cole aqui o conteúdo programático]"))
    }
}
