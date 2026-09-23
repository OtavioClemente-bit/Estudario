package br.com.estudario.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {
    private val databaseName = "migration-v2-v3-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName!!,
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrateTwoToThreePreservesExistingContentAndCreatesV2Tables() {
        helper.createDatabase(databaseName, 2).apply {
            execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt) VALUES (1, 'Concurso', 1, 1)")
            execSQL("INSERT INTO subjects (id, competitionId, name, position) VALUES (1, 1, 'Matéria', 0)")
            execSQL("INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, firstStudiedAt, lastStudiedAt, lastReviewedAt, notes, priority) VALUES (1, 1, NULL, 'Tópico', '', 0, 'ESTUDADO', NULL, NULL, NULL, '', 'NORMAL')")
            execSQL("INSERT INTO summaries (id, topicId, title, markdown, isFavorite, ownNotes, externalId, createdAt, updatedAt) VALUES (1, 1, 'Resumo', '# Texto', 1, 'minha nota', 'r1', 1, 1)")
            close()
        }
        helper.runMigrationsAndValidate(databaseName, 3, true, AppDatabase.MIGRATION_2_3).apply {
            query("SELECT title, kind, isFavorite, ownNotes FROM summaries WHERE id = 1").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Resumo", cursor.getString(0))
                assertEquals("COMPLETO", cursor.getString(1))
                assertEquals(1, cursor.getInt(2))
                assertEquals("minha nota", cursor.getString(3))
            }
            query("SELECT COUNT(*) FROM topic_snippets").use { cursor -> cursor.moveToFirst(); assertEquals(0, cursor.getInt(0)) }
            query("SELECT COUNT(*) FROM question_sessions").use { cursor -> cursor.moveToFirst(); assertEquals(0, cursor.getInt(0)) }
            close()
        }
    }

    @Test
    fun migrateThreeToFourPreservesProgressAndAddsStableMetadata() {
        val name = "migration-v3-v4-test"
        helper.createDatabase(name, 3).apply {
            execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt) VALUES (1, 'Concurso', 1, 10)")
            execSQL("INSERT INTO subjects (id, competitionId, name, position) VALUES (1, 1, 'TI', 0)")
            execSQL("INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, firstStudiedAt, lastStudiedAt, lastReviewedAt, notes, priority) VALUES (1, 1, NULL, 'Integridade', '', 0, 'ESTUDADO', 11, 12, NULL, 'nota pessoal', 'ALTA')")
            close()
        }
        helper.runMigrationsAndValidate(name, 4, true, AppDatabase.MIGRATION_3_4).apply {
            query("SELECT title, status, lastStudiedAt, notes, contentOriginType, externalId FROM topics WHERE id = 1").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Integridade", cursor.getString(0))
                assertEquals("ESTUDADO", cursor.getString(1))
                assertEquals(12, cursor.getLong(2))
                assertEquals("nota pessoal", cursor.getString(3))
                assertEquals("EDITAL", cursor.getString(4))
                assertEquals(null, cursor.getString(5))
            }
            close()
        }
    }

    @Test
    fun migrateFourToFivePreservesExistingDataAndCreatesPlannerTables() {
        val name = "migration-v4-v5-test"
        helper.createDatabase(name, 4).apply {
            execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt, externalId) VALUES (1, 'Concurso', 1, 10, 'competition-1')")
            execSQL("INSERT INTO subjects (id, competitionId, name, position, externalId) VALUES (1, 1, 'TI', 0, 'subject-1')")
            execSQL("INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, firstStudiedAt, lastStudiedAt, lastReviewedAt, notes, priority, externalId, contentOriginType) VALUES (1, 1, NULL, 'Integridade', '', 0, 'ESTUDADO', 11, 12, NULL, 'nota pessoal', 'ALTA', 'topic-1', 'EDITAL')")
            close()
        }

        helper.runMigrationsAndValidate(name, 5, true, AppDatabase.MIGRATION_4_5).apply {
            query("SELECT name, externalId FROM competitions WHERE id = 1").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Concurso", cursor.getString(0))
                assertEquals("competition-1", cursor.getString(1))
            }
            query("SELECT title, status, notes FROM topics WHERE id = 1").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Integridade", cursor.getString(0))
                assertEquals("ESTUDADO", cursor.getString(1))
                assertEquals("nota pessoal", cursor.getString(2))
            }
            query("SELECT COUNT(*) FROM study_plans").use { cursor -> cursor.moveToFirst(); assertEquals(0, cursor.getInt(0)) }
            query("SELECT COUNT(*) FROM plan_tasks").use { cursor -> cursor.moveToFirst(); assertEquals(0, cursor.getInt(0)) }
            query("SELECT COUNT(*) FROM study_task_executions").use { cursor -> cursor.moveToFirst(); assertEquals(0, cursor.getInt(0)) }
            close()
        }
    }

    @Test
    fun migrateFiveToSixKeepsPlansAndAddsMethodDefaults() {
        val name = "migration-v5-v6-test"
        helper.createDatabase(name, 5).apply {
            execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt, externalId) VALUES (1, 'Concurso', 1, 10, 'competition-1')")
            execSQL(
                "INSERT INTO study_plans (id, competitionId, name, objective, startEpochDay, examEpochDay, active, masterPlan, archived, revision, createdAt, updatedAt) " +
                    "VALUES ('plan-1', 1, 'Plano', 'Objetivo', 20000, NULL, 1, 0, 0, 3, 10, 11)",
            )
            close()
        }

        helper.runMigrationsAndValidate(name, 6, true, AppDatabase.MIGRATION_5_6).apply {
            query("SELECT name, revision, profile, blockMinutes, weeklyQuestionsTarget, questionsPerTopic, simulationsPerMonth, discursivesPerMonth, interleaveSubjects FROM study_plans WHERE id = 'plan-1'").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Plano", cursor.getString(0))
                assertEquals(3, cursor.getLong(1))
                assertEquals("DO_ZERO", cursor.getString(2))
                assertEquals(50, cursor.getInt(3))
                assertEquals(100, cursor.getInt(4))
                assertEquals(15, cursor.getInt(5))
                assertEquals(2, cursor.getInt(6))
                assertEquals(0, cursor.getInt(7))
                assertEquals(1, cursor.getInt(8))
            }
            close()
        }
    }

    @Test
    fun migrateTenToElevenKeepsErrorNotebookAndAddsRetryLadder() {
        val name = "migration-v10-v11-test"
        helper.createDatabase(name, 10).apply {
            execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt, externalId) VALUES (1, 'Concurso', 1, 10, 'competition-1')")
            execSQL("INSERT INTO subjects (id, competitionId, name, position, externalId) VALUES (1, 1, 'Matéria', 0, 's1')")
            execSQL(
                "INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, firstStudiedAt, lastStudiedAt, lastReviewedAt, notes, priority, externalId, contentOriginType, scopeCovers, scopeExcludes) " +
                    "VALUES (1, 1, NULL, 'Tópico', '', 0, 'ESTUDADO', NULL, NULL, NULL, '', 'NORMAL', 't1', 'EDITAL', NULL, NULL)",
            )
            execSQL(
                "INSERT INTO questions (id, topicId, externalId, board, agency, year, difficulty, source, statement, explanation, notes, tagsText, importedAt, answerCount, correctCount, errorCount, lastAnswer, lastAnsweredAt, isFavorite, questionSourceType, sourceId, sourceUrl, normalizedHash, reviewAnchor, errorConceptExternalId) " +
                    "VALUES (1, 1, 'q1', NULL, NULL, NULL, NULL, NULL, 'Enunciado', '', '', '', 10, 1, 0, 1, 'B', 20, 0, 'AUTHORIAL', NULL, NULL, NULL, NULL, NULL)",
            )
            execSQL(
                "INSERT INTO error_notebook (id, questionId, errorCount, retryCorrectCount, firstErrorAt, lastErrorAt, lastReviewedAt, comment, concept, pending, selectedAnswer, correctAnswer, status) " +
                    "VALUES (1, 1, 2, 0, 10, 20, NULL, 'minha nota', 'conceito', 1, 'B', 'A', 'RECORRENTE')",
            )
            close()
        }

        helper.runMigrationsAndValidate(name, 11, true, AppDatabase.MIGRATION_10_11).apply {
            query("SELECT errorCount, comment, status, retryStreak, nextRetryAt FROM error_notebook WHERE id = 1").use { cursor ->
                cursor.moveToFirst()
                assertEquals(2, cursor.getInt(0))
                assertEquals("minha nota", cursor.getString(1))
                assertEquals("RECORRENTE", cursor.getString(2))
                assertEquals(0, cursor.getInt(3))
                // Erro ainda em aberto entra na escada contando a partir do último erro.
                assertEquals(20L + 3L * 86_400_000L, cursor.getLong(4))
            }
            close()
        }
    }

    @Test
    fun migrateElevenToTwelvePreservesStudyDataAndMapsLegacyPriority() {
        val name = "migration-v11-v12-priority-test"
        helper.createDatabase(name, 11).apply {
            execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt, externalId) VALUES (1, 'Concurso', 1, 10, 'competition-1')")
            execSQL("INSERT INTO subjects (id, competitionId, name, position, externalId) VALUES (1, 1, 'TI', 0, 'subject-1')")
            execSQL(
                "INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, firstStudiedAt, lastStudiedAt, lastReviewedAt, notes, priority, externalId, contentOriginType, scopeCovers, scopeExcludes) " +
                    "VALUES (1, 1, NULL, 'Tópico', '', 0, 'ESTUDADO', 11, 12, NULL, 'nota', 'ALTA', 'topic-1', 'EDITAL', NULL, NULL)",
            )
            close()
        }

        helper.runMigrationsAndValidate(name, 12, true, AppDatabase.MIGRATION_11_12).apply {
            query("SELECT name, externalId FROM competitions WHERE id = 1").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Concurso", cursor.getString(0))
                assertEquals("competition-1", cursor.getString(1))
            }
            query("SELECT title, status, lastStudiedAt, notes, assessedPriorityScore, hasAssessedPriority, userPriorityOverride FROM topics WHERE id = 1").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Tópico", cursor.getString(0))
                assertEquals("ESTUDADO", cursor.getString(1))
                assertEquals(12, cursor.getLong(2))
                assertEquals("nota", cursor.getString(3))
                assertEquals(70, cursor.getInt(4))
                assertEquals(1, cursor.getInt(5))
                assertEquals(null, cursor.getString(6))
            }
            close()
        }
    }

    @Test
    fun migrateThirteenToFourteenSeparatesLegacyFocusRows() {
        val name = "migration-v13-v14-focus-test"
        helper.createDatabase(name, 13).apply {
            execSQL(
                "INSERT INTO study_sessions " +
                    "(id, topicId, startedAt, completedAt, competitionId, subjectId, durationSeconds, " +
                    "questionCount, correctCount, wrongCount, notes, sourcePackageId) " +
                    "VALUES (7, 0, 1000, 91000, NULL, NULL, 90, 0, 0, 0, 'Modo foco', NULL)",
            )
            execSQL(
                "INSERT INTO study_sessions " +
                    "(id, topicId, startedAt, completedAt, competitionId, subjectId, durationSeconds, " +
                    "questionCount, correctCount, wrongCount, notes, sourcePackageId) " +
                    "VALUES (8, 42, 2000, 122000, 3, 4, 120, 0, 0, 0, 'Modo foco', NULL)",
            )
            execSQL(
                "INSERT INTO study_sessions " +
                    "(id, topicId, startedAt, completedAt, competitionId, subjectId, durationSeconds, " +
                    "questionCount, correctCount, wrongCount, notes, sourcePackageId) " +
                    "VALUES (9, 42, 3000, 6000, 3, 4, 3, 1, 1, 0, 'Estudo concluído', NULL)",
            )
            close()
        }

        helper.runMigrationsAndValidate(name, 14, true, AppDatabase.MIGRATION_13_14).apply {
            query("SELECT id, title, startedAt, completedAt, durationSeconds, subjectIdsText, origin, topicId FROM focus_sessions ORDER BY id").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("legacy-7", cursor.getString(0))
                assertEquals("Sessão de foco", cursor.getString(1))
                assertEquals(1000L, cursor.getLong(2))
                assertEquals(91000L, cursor.getLong(3))
                assertEquals(90L, cursor.getLong(4))
                assertEquals("", cursor.getString(5))
                assertEquals("LIVRE", cursor.getString(6))
                assertTrue(cursor.isNull(7))

                assertTrue(cursor.moveToNext())
                assertEquals("legacy-8", cursor.getString(0))
                assertEquals("4", cursor.getString(5))
                assertEquals("MATERIA", cursor.getString(6))
                assertEquals(42L, cursor.getLong(7))
                assertFalse(cursor.moveToNext())
            }
            query("SELECT COUNT(*) FROM study_sessions WHERE notes = 'Modo foco'").use { cursor ->
                cursor.moveToFirst()
                assertEquals(0, cursor.getInt(0))
            }
            query("SELECT COUNT(*) FROM study_sessions WHERE id = 9 AND notes = 'Estudo concluído'").use { cursor ->
                cursor.moveToFirst()
                assertEquals(1, cursor.getInt(0))
            }
            close()
        }
    }
}
