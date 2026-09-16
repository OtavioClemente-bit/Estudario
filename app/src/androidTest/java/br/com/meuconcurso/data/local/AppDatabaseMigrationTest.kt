package br.com.meuconcurso.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
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
}
