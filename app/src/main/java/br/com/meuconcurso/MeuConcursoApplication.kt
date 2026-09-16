package br.com.meuconcurso

import android.app.Application
import br.com.meuconcurso.data.StudyRepository
import br.com.meuconcurso.data.local.AppDatabase
import br.com.meuconcurso.data.preferences.AppPreferences
import br.com.meuconcurso.data.planner.StudyExecutionService
import br.com.meuconcurso.data.planner.StudyPlanApplicationService
import br.com.meuconcurso.data.planner.StudyPlanRepository
import br.com.meuconcurso.data.transfer.IncomingFileCoordinator
import br.com.meuconcurso.data.transfer.planner.StudyPlanTransferService
import br.com.meuconcurso.domain.planner.StudyPlannerEngine
import br.com.meuconcurso.notifications.StudyNotificationCoordinator
import kotlinx.coroutines.*

class MeuConcursoApplication : Application() {
    lateinit var database: AppDatabase
        private set
    lateinit var repository: StudyRepository
        private set
    lateinit var preferences: AppPreferences
        private set
    lateinit var planRepository: StudyPlanRepository
        private set
    lateinit var planService: StudyPlanApplicationService
        private set
    lateinit var executionService: StudyExecutionService
        private set
    lateinit var planTransferService: StudyPlanTransferService
        private set
    val incomingFiles = IncomingFileCoordinator()

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.create(this)
        repository = StudyRepository(database)
        planRepository = StudyPlanRepository(database)
        planService = StudyPlanApplicationService(database, StudyPlannerEngine())
        executionService = StudyExecutionService(database, planService)
        planTransferService = StudyPlanTransferService(database)
        preferences = AppPreferences(this)
        StudyNotificationCoordinator.createChannels(this)
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch { StudyNotificationCoordinator.refresh(this@MeuConcursoApplication, preferences) }
    }
}
