package br.com.estudario.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteSyllabusSyncMigrationTest {
    private val databaseName = "migration-v14-v18-remote-syllabus-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName!!,
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrateFourteenToEighteenPreservesProgressHierarchyAndRemoteSyncStorage() {
        helper.createDatabase(databaseName, 14).apply {
            execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt, externalId) VALUES (41, 'Concurso local', 1, 10, 'local-competition-41')")
            execSQL("INSERT INTO subjects (id, competitionId, name, position, externalId) VALUES (51, 41, 'Direito', 0, 'subject-51')")
            execSQL(
                "INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, notes, priority, externalId, contentOriginType) " +
                    "VALUES (61, 51, NULL, 'Constitucional', 'nota local', 0, 'ESTUDADO', 'minhas anotações', 'ALTA', 'topic-61', 'EDITAL')",
            )
            execSQL(
                "INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, notes, priority, externalId, contentOriginType) " +
                    "VALUES (62, 51, 61, 'Controle de constitucionalidade', '', 0, 'EM_ESTUDO', '', 'NORMAL', 'topic-62', 'EDITAL')",
            )
            execSQL(
                "INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, notes, priority, externalId, contentOriginType) " +
                    "VALUES (63, 51, 62, 'Ações de controle', '', 0, 'NAO_ESTUDADO', '', 'NORMAL', 'topic-63', 'EDITAL')",
            )
            execSQL("INSERT INTO review_schedule (id, topicId, stage, dueAt, completedAt, ignoredAt, perceivedDifficulty, questionCorrect, questionTotal) VALUES (71, 63, 2, 1000, NULL, NULL, 'DIFICIL', 3, 5)")
            execSQL("INSERT INTO study_queue (id, topicId, position, paused, enqueuedAt, postponements) VALUES (81, 63, 4, 1, 900, 2)")
            execSQL(
                "INSERT INTO study_sessions (id, topicId, startedAt, completedAt, competitionId, subjectId, durationSeconds, questionCount, correctCount, wrongCount, notes, sourcePackageId) " +
                    "VALUES (91, 63, 100, 700, 41, 51, 600, 8, 6, 2, 'progresso local', NULL)",
            )
            close()
        }

        helper.runMigrationsAndValidate(databaseName, 15, true, AppDatabase.MIGRATION_14_15).apply {
            execSQL("INSERT INTO remote_syllabus_sync (id, operation, localSyllabusId, remoteSyllabusId, jobId, payloadHash, state, attemptCount, nextAttemptAt, lastError, createdAt, updatedAt) VALUES (101, 'UPSERT', 41, 'remote-41', 'job-old', 'same-payload', 'SYNCED', 1, 0, NULL, 10, 20)")
            execSQL("INSERT INTO remote_syllabus_sync (id, operation, localSyllabusId, remoteSyllabusId, jobId, payloadHash, state, attemptCount, nextAttemptAt, lastError, createdAt, updatedAt) VALUES (102, 'UPSERT', 41, 'remote-41', 'job-new', 'same-payload', 'FAILED', 2, 500, 'NETWORK_ERROR', 11, 30)")
            close()
        }

        helper.runMigrationsAndValidate(databaseName, 16, true, AppDatabase.MIGRATION_15_16).apply {
            close()
        }

        helper.runMigrationsAndValidate(databaseName, 17, true, AppDatabase.MIGRATION_16_17).apply {
            query("SELECT id, name, externalId, remoteSyllabusId FROM competitions WHERE id = 41").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(41L, cursor.getLong(0))
                assertEquals("Concurso local", cursor.getString(1))
                assertEquals("local-competition-41", cursor.getString(2))
                assertTrue(cursor.isNull(3))
            }
            query("SELECT id, competitionId, externalId FROM subjects WHERE id = 51").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(51L, cursor.getLong(0))
                assertEquals(41L, cursor.getLong(1))
                assertEquals("subject-51", cursor.getString(2))
            }
            query("SELECT id, subjectId, parentTopicId, externalId FROM topics WHERE id = 61").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(61L, cursor.getLong(0))
                assertEquals(51L, cursor.getLong(1))
                assertTrue(cursor.isNull(2))
                assertEquals("topic-61", cursor.getString(3))
            }
            query("SELECT parentTopicId FROM topics WHERE id = 63").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(62L, cursor.getLong(0))
            }
            query("SELECT topicId, stage, dueAt, questionCorrect, questionTotal FROM review_schedule WHERE id = 71").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(63L, cursor.getLong(0))
                assertEquals(2, cursor.getInt(1))
                assertEquals(1000L, cursor.getLong(2))
                assertEquals(3, cursor.getInt(3))
                assertEquals(5, cursor.getInt(4))
            }
            query("SELECT topicId, position, paused, postponements FROM study_queue WHERE id = 81").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(63L, cursor.getLong(0))
                assertEquals(4, cursor.getInt(1))
                assertEquals(1, cursor.getInt(2))
                assertEquals(2, cursor.getInt(3))
            }
            query("SELECT id, topicId, competitionId, subjectId, durationSeconds, questionCount, correctCount, wrongCount, notes FROM study_sessions WHERE id = 91").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(91L, cursor.getLong(0))
                assertEquals(63L, cursor.getLong(1))
                assertEquals(41L, cursor.getLong(2))
                assertEquals(51L, cursor.getLong(3))
                assertEquals(600L, cursor.getLong(4))
                assertEquals(8, cursor.getInt(5))
                assertEquals(6, cursor.getInt(6))
                assertEquals(2, cursor.getInt(7))
                assertEquals("progresso local", cursor.getString(8))
            }
            query("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = 'remote_syllabus_sync'").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(1, cursor.getInt(0))
            }
            query("PRAGMA table_info(remote_syllabus_sync)").use { cursor ->
                val columns = buildSet {
                    while (cursor.moveToNext()) add(cursor.getString(1))
                }
                assertTrue(columns.containsAll(setOf("operation", "localSyllabusId", "remoteSyllabusId", "jobId", "payloadHash", "state", "attemptCount", "attemptToken", "nextAttemptAt", "lastError", "createdAt", "updatedAt")))
            }
            query("SELECT COUNT(*) FROM remote_syllabus_sync WHERE localSyllabusId = 41 AND operation = 'UPSERT' AND payloadHash = 'same-payload'").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(1, cursor.getInt(0))
            }
            query("SELECT state, jobId, lastError FROM remote_syllabus_sync WHERE localSyllabusId = 41 AND payloadHash = 'same-payload'").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("FAILED", cursor.getString(0))
                assertEquals("job-new", cursor.getString(1))
                assertEquals("NETWORK_ERROR", cursor.getString(2))
            }
            query("SELECT attemptToken FROM remote_syllabus_sync WHERE localSyllabusId = 41 AND payloadHash = 'same-payload'").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("", cursor.getString(0))
            }
            close()
        }

        helper.runMigrationsAndValidate(databaseName, 18, true, AppDatabase.MIGRATION_17_18).apply {
            query("PRAGMA table_info(remote_syllabus_sync)").use { cursor ->
                val columns = buildSet {
                    while (cursor.moveToNext()) add(cursor.getString(1))
                }
                assertTrue(columns.contains("payloadJson"))
            }
            query("SELECT payloadJson FROM remote_syllabus_sync WHERE localSyllabusId = 41 AND operation = 'UPSERT'").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("", cursor.getString(0))
            }
            close()
        }
    }
}
