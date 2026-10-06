package br.com.estudario.data.transfer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.estudario.data.StudyRepository
import br.com.estudario.data.local.AppDatabase
import java.io.File
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Gera o conteúdo da demonstração do app web (web/src/jsMain/resources/demo.json) a partir da
 * mesma demonstração do app, para os dois mostrarem o mesmo material. Rodar com
 * `connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=...ExportDemoSnapshotTest`
 * e copiar o arquivo com `adb pull`.
 */
@RunWith(AndroidJUnit4::class)
class ExportDemoSnapshotTest {
    @Test fun exportDemo() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        try {
            StudyRepository(database).loadDemoData()
            val json = JSONObject(BackupService(database).export()).apply { remove("exportedAt") }
            assertTrue(json.getJSONArray("topics").length() > 0)
            File(context.getExternalFilesDir(null), "demo.json").writeText(json.toString(2))
        } finally {
            database.close()
        }
    }
}
