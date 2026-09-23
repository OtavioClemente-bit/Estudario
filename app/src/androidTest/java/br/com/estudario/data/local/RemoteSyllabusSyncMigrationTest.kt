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
    private val databaseName = "migration-v14-v15-remote-syllabus-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName!!,
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrateFourteenToFifteenPreservesLocalIdsAndAddsRemoteSyncStorage() {
        helper.createDatabase(databaseName, 14).apply {
            execSQL("INSERT INTO competitions (id, name, isPrimary, createdAt, externalId) VALUES (41, 'Concurso local', 1, 10, 'local-competition-41')")
            execSQL("INSERT INTO subjects (id, competitionId, name, position, externalId) VALUES (51, 41, 'Direito', 0, 'subject-51')")
            execSQL(
                "INSERT INTO topics (id, subjectId, parentTopicId, title, description, position, status, notes, priority, externalId, contentOriginType) " +
                    "VALUES (61, 51, NULL, 'Constitucional', 'nota local', 0, 'ESTUDADO', 'minhas anotações', 'ALTA', 'topic-61', 'EDITAL')",
            )
            close()
        }

        helper.runMigrationsAndValidate(databaseName, 15, true, AppDatabase.MIGRATION_14_15).apply {
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
            query("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = 'remote_syllabus_sync'").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(1, cursor.getInt(0))
            }
            query("PRAGMA table_info(remote_syllabus_sync)").use { cursor ->
                val columns = buildSet {
                    while (cursor.moveToNext()) add(cursor.getString(1))
                }
                assertTrue(columns.containsAll(setOf("operation", "localSyllabusId", "remoteSyllabusId", "jobId", "payloadHash", "state", "attemptCount", "nextAttemptAt", "lastError", "createdAt", "updatedAt")))
            }
            close()
        }
    }
}
