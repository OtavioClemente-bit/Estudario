package br.com.estudario.domain.setup

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
            step = InitialSetupStep.AVAILABILITY,
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
        )

        assertEquals(source, InitialSetupSnapshotCodec.decode(InitialSetupSnapshotCodec.encode(source)))
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
        assertEquals(InitialSetupStep.PROFILE, InitialSetupTransitions.previous(InitialSetupStep.AVAILABILITY))
        assertEquals(null, InitialSetupTransitions.previous(InitialSetupStep.INTRO))
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
