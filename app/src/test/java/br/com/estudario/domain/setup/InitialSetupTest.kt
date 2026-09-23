package br.com.estudario.domain.setup

import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.StudyProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InitialSetupTest {
    @Test
    fun `snapshot round trips accents separators and optional values`() {
        val source = InitialSetupSnapshot(
            status = InitialSetupStatus.IN_PROGRESS,
            step = InitialSetupStep.SUBJECT_DIFFICULTY,
            competitionId = 42,
            competitionName = "TRF | Área cível",
            role = "Analista / Técnico",
            examDate = null,
            syllabusMethod = SyllabusMethod.MANUAL,
            manualSubjects = listOf("Direito Constitucional", "Português ~ redação"),
            manualTopics = mapOf("Direito Constitucional" to listOf("Controle de constitucionalidade", "Direitos fundamentais")),
            studyProfile = StudyProfile.APROFUNDANDO,
            availabilityMinutes = listOf(90, 0, 120, 120, 60, 30, 0),
            sessionMinutes = 45,
            planPreference = "Priorizar questões e revisão",
            subjectDifficulties = mapOf(
                "materia|1" to PersonalDifficulty.HARD,
                "direito~civil" to PersonalDifficulty.EASY,
            ),
        )

        assertEquals(source, InitialSetupSnapshotCodec.decode(InitialSetupSnapshotCodec.encode(source)))
    }

    @Test
    fun `legacy snapshot keeps availability and starts with no subject difficulty choices`() {
        val legacy = listOf(
            "1", "IN_PROGRESS", "AVAILABILITY", "42", "Policia", "Analista", "", "MANUAL",
            "Portugues", "", "DO_ZERO", "120,75,90,0,60,30,0", "50", "AUTOMATIC", "", "plano-1",
        ).joinToString("|")

        val decoded = InitialSetupSnapshotCodec.decode(legacy)

        assertTrue(decoded.subjectDifficulties.isEmpty())
        assertEquals(listOf(120, 75, 90, 0, 60, 30, 0), decoded.availabilityMinutes)
        assertEquals(InitialSetupStep.AVAILABILITY, decoded.step)
    }

    @Test
    fun `corrupt snapshot falls back to safe defaults`() {
        val decoded = InitialSetupSnapshotCodec.decode("not-a-valid-snapshot")
        assertEquals(InitialSetupStatus.NOT_STARTED, decoded.status)
        assertEquals(InitialSetupStep.INTRO, decoded.step)
        assertEquals(7, decoded.availabilityMinutes.size)
    }

    @Test
    fun `state machine only advances one intentional step`() {
        assertTrue(InitialSetupTransitions.canAdvance(InitialSetupStep.INTRO, InitialSetupStep.COMPETITION))
        assertFalse(InitialSetupTransitions.canAdvance(InitialSetupStep.INTRO, InitialSetupStep.PROFILE))
        assertTrue(InitialSetupTransitions.canAdvance(InitialSetupStep.SYLLABUS_REVIEW, InitialSetupStep.SUBJECT_PRIORITY))
        assertEquals(InitialSetupStep.SUBJECT_DIFFICULTY, InitialSetupTransitions.previous(InitialSetupStep.AVAILABILITY))
        assertTrue(InitialSetupTransitions.canAdvance(InitialSetupStep.SUBJECT_DIFFICULTY, InitialSetupStep.AVAILABILITY))
        assertTrue(InitialSetupTransitions.canAdvance(InitialSetupStep.AVAILABILITY, InitialSetupStep.PROFILE))
        assertEquals(InitialSetupStep.AVAILABILITY, InitialSetupTransitions.previous(InitialSetupStep.PROFILE))
        assertEquals(null, InitialSetupTransitions.previous(InitialSetupStep.INTRO))
    }

    @Test
    fun `subject difficulty selection follows the current stable subject ids`() {
        val snapshot = InitialSetupSnapshot(
            subjectDifficulties = mapOf("subject-1" to PersonalDifficulty.HARD, "removed" to PersonalDifficulty.EASY),
        )

        assertEquals(
            mapOf("subject-1" to PersonalDifficulty.HARD),
            snapshot.subjectDifficultiesFor(setOf("subject-1", "subject-2")),
        )
    }

    @Test
    fun `new subjects have no preselected difficulty`() {
        assertTrue(InitialSetupSnapshot().subjectDifficultiesFor(setOf("1", "2")).isEmpty())
    }

    @Test
    fun `difficulty step allows neutral defaults for unmodified subjects`() {
        val snapshot = InitialSetupSnapshot(
            step = InitialSetupStep.SUBJECT_DIFFICULTY,
            subjectDifficulties = mapOf("1" to PersonalDifficulty.EASY),
        )
        assertTrue(InitialSetupTransitions.canAdvance(snapshot, InitialSetupStep.AVAILABILITY, setOf("1", "2")))
        assertTrue(InitialSetupTransitions.canAdvance(snapshot, InitialSetupStep.AVAILABILITY, emptySet()))
        assertEquals(PersonalDifficulty.DEFAULT, snapshot.dimensionsFor("2", ExamPriority.HIGH).personalDifficulty)
    }

    @Test
    fun `empty syllabus cannot advance from review`() {
        val snapshot = InitialSetupSnapshot(step = InitialSetupStep.SYLLABUS_REVIEW)
        assertFalse(InitialSetupTransitions.canAdvance(snapshot, InitialSetupStep.SUBJECT_PRIORITY, emptySet()))
        assertTrue(InitialSetupTransitions.canAdvance(snapshot, InitialSetupStep.SUBJECT_PRIORITY, setOf("1")))
    }

    @Test
    fun `normalization preserves optional exam date while cleaning drafts`() {
        val normalized = InitialSetupSnapshot(
            competitionName = "  Polícia  ",
            manualSubjects = listOf("Português", " Português ", ""),
            availabilityMinutes = listOf(90),
            sessionMinutes = 999,
        ).normalized()
        assertEquals("Polícia", normalized.competitionName)
        assertEquals(listOf("Português"), normalized.manualSubjects)
        assertEquals(7, normalized.availabilityMinutes.size)
        assertEquals(180, normalized.sessionMinutes)
    }

    @Test
    fun `existing workspace bypasses setup while an empty workspace does not`() {
        assertTrue(InitialSetupWorkspace.hasExistingData(1, 0))
        assertTrue(InitialSetupWorkspace.hasExistingData(0, 1))
        assertFalse(InitialSetupWorkspace.hasExistingData(0, 0))
    }
}
