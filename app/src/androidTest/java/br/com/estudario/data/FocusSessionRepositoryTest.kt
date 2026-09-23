package br.com.estudario.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.FocusSessionEntity
import br.com.estudario.data.local.FocusSessionOrigin
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FocusSessionRepositoryTest {
    @Test
    fun recordOnceIgnoresDuplicateSessionIds() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            val repository = FocusSessionRepository(database.dao())
            val session = FocusSessionEntity(
                id = "session-1",
                title = "Estudo livre",
                startedAt = 1_000L,
                completedAt = 61_000L,
                durationSeconds = 60L,
                origin = FocusSessionOrigin.LIVRE,
            )

            assertTrue(repository.recordOnce(session))
            assertFalse(repository.recordOnce(session.copy(durationSeconds = 120L)))
            assertEquals(listOf(session), database.dao().focusSessionsOnce())
        } finally {
            database.close()
        }
    }
}
