package br.com.estudario.domain.setup

import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.StudyProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Testes do estado do assistente de planejamento.
 *
 * Dois pontos merecem teste de verdade: o codec, porque ele lê snapshots gravados pela versão
 * anterior do app e um erro aqui apaga a configuração de alguém em silêncio; e as transições,
 * porque a regra mudou. Os passos dos três eixos deixaram de travar o avanço.
 */
class InitialSetupWizardTest {

    private fun snapshot() = InitialSetupSnapshot(
        status = InitialSetupStatus.IN_PROGRESS,
        step = InitialSetupStep.SUBJECT_DIFFICULTY,
        competitionId = 7L,
        competitionName = "TRT-3: Técnico Judiciário",
        role = "Área: Tecnologia da Informação",
        examDate = "2026-12-20",
        syllabusMethod = SyllabusMethod.IMPORT_ESTUDO,
        studyProfile = StudyProfile.APROFUNDANDO,
        availabilityMinutes = listOf(120, 120, 60, 120, 90, 240, 0),
        sessionMinutes = 45,
        subjectDifficulties = mapOf("1" to PersonalDifficulty.VERY_HARD, "2" to PersonalDifficulty.EASY),
        subjectKnowledge = mapOf("1" to InitialKnowledge.NONE, "2" to InitialKnowledge.SOLID),
        subjectPriorities = mapOf("2" to ExamPriority.VERY_HIGH),
        variety = SubjectVariety.MORE_CONTINUITY,
    )

    // --- Codec ------------------------------------------------------------------------------

    @Test
    fun `o snapshot sobrevive a uma ida e volta pelo codec`() {
        val original = snapshot().normalized()
        val restored = InitialSetupSnapshotCodec.decode(InitialSetupSnapshotCodec.encode(original))
        assertEquals(original.copy(version = InitialSetupSnapshotCodec.CURRENT_VERSION), restored)
    }

    @Test
    fun `os tres eixos voltam separados`() {
        val restored = InitialSetupSnapshotCodec.decode(InitialSetupSnapshotCodec.encode(snapshot()))
        assertEquals(PersonalDifficulty.VERY_HARD, restored.subjectDifficulties["1"])
        assertEquals(InitialKnowledge.SOLID, restored.subjectKnowledge["2"])
        assertEquals(ExamPriority.VERY_HIGH, restored.subjectPriorities["2"])
        assertEquals(SubjectVariety.MORE_CONTINUITY, restored.variety)
    }

    @Test
    fun `um snapshot da versao anterior continua legivel`() {
        // Formato v2: sem conhecimento, sem prioridades e com a dificuldade em três níveis.
        val legacy = listOf(
            "2",
            InitialSetupStatus.IN_PROGRESS.name,
            InitialSetupStep.SUBJECT_DIFFICULTY.name,
            "7",
            "TRT-3",
            "TI",
            "2026-12-20",
            SyllabusMethod.MANUAL.name,
            "",
            "",
            StudyProfile.DO_ZERO.name,
            "120,120,120,120,120,120,0",
            "50",
            PlanCreationMethod.AUTOMATIC.name,
            "",
            "",
            "1=EASY~2=MEDIUM~3=HARD",
        ).joinToString("|")

        val restored = InitialSetupSnapshotCodec.decode(legacy)

        assertEquals("A escala antiga precisa virar a nova sem perder a resposta", PersonalDifficulty.EASY, restored.subjectDifficulties["1"])
        assertEquals("MEDIUM é NORMAL na escala de cinco", PersonalDifficulty.NORMAL, restored.subjectDifficulties["2"])
        assertEquals(PersonalDifficulty.HARD, restored.subjectDifficulties["3"])
        assertTrue("campos novos entram vazios, não quebrados", restored.subjectKnowledge.isEmpty())
        assertTrue(restored.subjectPriorities.isEmpty())
        assertEquals(SubjectVariety.BALANCED, restored.variety)
        assertEquals(7L, restored.competitionId)
    }

    @Test
    fun `lixo no snapshot nao derruba o assistente`() {
        assertEquals(InitialSetupSnapshot(), InitialSetupSnapshotCodec.decode(null))
        assertEquals(InitialSetupSnapshot(), InitialSetupSnapshotCodec.decode(""))

        // Texto sem sentido não lança: cada campo irreconhecível cai no próprio padrão, e o que
        // importa é que o assistente reabra num estado navegável em vez de quebrar.
        val garbage = InitialSetupSnapshotCodec.decode("isto|nao|e|um|snapshot|qualquer")
        assertEquals(InitialSetupStatus.NOT_STARTED, garbage.status)
        assertEquals(InitialSetupStep.INTRO, garbage.step)
        assertEquals(StudyProfile.DO_ZERO, garbage.studyProfile)
        assertEquals(SubjectVariety.BALANCED, garbage.variety)
        assertTrue(garbage.subjectDifficulties.isEmpty())
        assertTrue(garbage.subjectKnowledge.isEmpty())
        assertTrue(garbage.subjectPriorities.isEmpty())
        assertEquals(7, garbage.availabilityMinutes.size)
    }

    @Test
    fun `nomes com acento e separadores sobrevivem`() {
        val tricky = snapshot().copy(
            competitionName = "Concurso | Nível ~ Médio = 2026",
            manualSubjects = listOf("Raciocínio Lógico", "Direito Administrativo"),
            manualTopics = mapOf("Raciocínio Lógico" to listOf("Proposições", "Tabelas-verdade")),
        )
        val restored = InitialSetupSnapshotCodec.decode(InitialSetupSnapshotCodec.encode(tricky))
        assertEquals(tricky.competitionName, restored.competitionName)
        assertEquals(tricky.manualSubjects, restored.manualSubjects)
        assertEquals(tricky.manualTopics, restored.manualTopics)
    }

    // --- Os três eixos ----------------------------------------------------------------------

    @Test
    fun `materia sem resposta recebe os padroes neutros`() {
        val empty = InitialSetupSnapshot()
        val dimensions = empty.dimensionsFor("42", ExamPriority.HIGH)
        assertEquals("a prioridade do edital é respeitada", ExamPriority.HIGH, dimensions.examPriority)
        assertEquals(PersonalDifficulty.NORMAL, dimensions.personalDifficulty)
        assertEquals(InitialKnowledge.NONE, dimensions.initialKnowledge)
    }

    @Test
    fun `o ajuste manual de prioridade vence o edital`() {
        val tuned = InitialSetupSnapshot(subjectPriorities = mapOf("42" to ExamPriority.VERY_LOW))
        assertEquals(ExamPriority.VERY_LOW, tuned.dimensionsFor("42", ExamPriority.VERY_HIGH).examPriority)
    }

    @Test
    fun `responder dificuldade nao mexe na prioridade`() {
        val base = InitialSetupSnapshot()
        val harder = base.copy(subjectDifficulties = mapOf("42" to PersonalDifficulty.VERY_HARD))
        assertEquals(
            "a prioridade da prova precisa continuar exatamente a mesma",
            base.dimensionsFor("42", ExamPriority.MEDIUM).examPriority,
            harder.dimensionsFor("42", ExamPriority.MEDIUM).examPriority,
        )
        assertNotEquals(
            base.dimensionsFor("42", ExamPriority.MEDIUM).personalDifficulty,
            harder.dimensionsFor("42", ExamPriority.MEDIUM).personalDifficulty,
        )
    }

    @Test
    fun `a contagem de ajustes reflete o que a pessoa tocou`() {
        val ids = setOf("1", "2", "3")
        assertEquals(0, InitialSetupSnapshot().tunedSubjectCount(ids))
        assertEquals(
            2,
            InitialSetupSnapshot(
                subjectDifficulties = mapOf("1" to PersonalDifficulty.HARD),
                subjectKnowledge = mapOf("2" to InitialKnowledge.SOLID),
                // Matéria fora do edital atual não conta.
                subjectPriorities = mapOf("9" to ExamPriority.HIGH),
            ).tunedSubjectCount(ids),
        )
    }

    // --- Transições -------------------------------------------------------------------------

    @Test
    fun `a ordem da conversa vai do edital ao resumo`() {
        val expected = listOf(
            InitialSetupStep.INTRO to InitialSetupStep.COMPETITION,
            InitialSetupStep.COMPETITION to InitialSetupStep.EXAM_DATE,
            InitialSetupStep.EXAM_DATE to InitialSetupStep.SYLLABUS_METHOD,
            InitialSetupStep.SYLLABUS_METHOD to InitialSetupStep.SYLLABUS_REVIEW,
            InitialSetupStep.SYLLABUS_REVIEW to InitialSetupStep.SUBJECT_PRIORITY,
            InitialSetupStep.SUBJECT_PRIORITY to InitialSetupStep.SUBJECT_DIFFICULTY,
            InitialSetupStep.SUBJECT_DIFFICULTY to InitialSetupStep.AVAILABILITY,
            InitialSetupStep.AVAILABILITY to InitialSetupStep.PROFILE,
            InitialSetupStep.PROFILE to InitialSetupStep.PLAN_SUMMARY,
            InitialSetupStep.PLAN_SUMMARY to InitialSetupStep.PLAN_METHOD,
            InitialSetupStep.PLAN_METHOD to InitialSetupStep.PLAN_REVIEW,
            InitialSetupStep.PLAN_REVIEW to InitialSetupStep.READY,
        )
        expected.forEach { (from, to) ->
            assertTrue("$from deveria avançar para $to", InitialSetupTransitions.canAdvance(from, to))
            assertEquals(from, InitialSetupTransitions.previous(to))
        }
    }

    @Test
    fun `os passos dos tres eixos nao travam mais o avanco`() {
        val ids = setOf("1", "2", "3")
        val untouched = InitialSetupSnapshot(step = InitialSetupStep.SUBJECT_DIFFICULTY)
        assertTrue(
            "obrigar a responder matéria por matéria era o formulário que o assistente substituiu",
            InitialSetupTransitions.canAdvance(untouched, InitialSetupStep.AVAILABILITY, ids),
        )
        val priority = InitialSetupSnapshot(step = InitialSetupStep.SUBJECT_PRIORITY)
        assertTrue(InitialSetupTransitions.canAdvance(priority, InitialSetupStep.SUBJECT_DIFFICULTY, ids))
    }

    @Test
    fun `o edital e a disponibilidade continuam obrigatorios`() {
        val semEdital = InitialSetupSnapshot(step = InitialSetupStep.SYLLABUS_REVIEW)
        assertFalse(InitialSetupTransitions.canAdvance(semEdital, InitialSetupStep.SUBJECT_PRIORITY, emptySet()))
        assertTrue(InitialSetupTransitions.canAdvance(semEdital, InitialSetupStep.SUBJECT_PRIORITY, setOf("1")))

        val semTempo = InitialSetupSnapshot(
            step = InitialSetupStep.AVAILABILITY,
            availabilityMinutes = List(7) { 0 },
        )
        assertFalse(
            "um plano sem nenhum dia disponível não é um plano",
            InitialSetupTransitions.canAdvance(semTempo, InitialSetupStep.PROFILE, setOf("1")),
        )
    }

    @Test
    fun `nenhum passo pula etapas`() {
        assertFalse(InitialSetupTransitions.canAdvance(InitialSetupStep.SYLLABUS_REVIEW, InitialSetupStep.PLAN_SUMMARY))
        assertFalse(InitialSetupTransitions.canAdvance(InitialSetupStep.INTRO, InitialSetupStep.READY))
        assertFalse(InitialSetupTransitions.canAdvance(InitialSetupStep.AVAILABILITY, InitialSetupStep.SUBJECT_DIFFICULTY))
    }

    // --- Variedade --------------------------------------------------------------------------

    @Test
    fun `cada resposta de variedade produz um comportamento diferente`() {
        val tetos = SubjectVariety.entries.map { it.dailySubjectSharePercent }
        assertEquals(
            "três opções precisam de três comportamentos, senão a pergunta é decorativa",
            SubjectVariety.entries.size,
            tetos.distinct().size,
        )
        assertTrue(tetos.all { it in 20..100 })
        assertTrue(SubjectVariety.MORE_VARIETY.dailySubjectSharePercent < SubjectVariety.BALANCED.dailySubjectSharePercent)
        assertTrue(SubjectVariety.BALANCED.dailySubjectSharePercent < SubjectVariety.MORE_CONTINUITY.dailySubjectSharePercent)
        assertFalse(SubjectVariety.MORE_CONTINUITY.interleave)
    }

    @Test
    fun `a disponibilidade normalizada tem sempre sete dias`() {
        val curto = InitialSetupSnapshot(availabilityMinutes = listOf(60, 60)).normalized()
        assertEquals(7, curto.availabilityMinutes.size)
        assertEquals(120, curto.weeklyMinutes)

        val absurdo = InitialSetupSnapshot(availabilityMinutes = List(7) { 5_000 }).normalized()
        assertTrue(absurdo.availabilityMinutes.all { it <= 1_440 })
    }
}
