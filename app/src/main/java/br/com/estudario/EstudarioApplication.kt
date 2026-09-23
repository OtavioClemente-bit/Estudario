package br.com.estudario

import android.app.Application
import br.com.estudario.data.StudyRepository
import br.com.estudario.data.FocusSessionRepository
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.preferences.AppPreferences
import br.com.estudario.data.planner.StudyExecutionService
import br.com.estudario.data.planner.StudyPlanApplicationService
import br.com.estudario.data.planner.StudyPlanRepository
import br.com.estudario.data.transfer.IncomingFileCoordinator
import br.com.estudario.data.transfer.planner.StudyPlanTransferService
import br.com.estudario.domain.planner.StudyPlannerEngine
import br.com.estudario.focus.FocusSessionManager
import br.com.estudario.notifications.StudyNotificationCoordinator
import kotlinx.coroutines.*

class EstudarioApplication : Application() {
    lateinit var database: AppDatabase
        private set
    lateinit var repository: StudyRepository
        private set
    lateinit var focusSessionRepository: FocusSessionRepository
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
        focusSessionRepository = FocusSessionRepository(database.dao())
        preferences = AppPreferences(this)
        planRepository = StudyPlanRepository(database)
        val calendarSyncService = br.com.estudario.data.planner.CalendarSyncService(this)
        planService = StudyPlanApplicationService(database, StudyPlannerEngine(), calendarSyncService)
        executionService = StudyExecutionService(database, planService)
        planTransferService = StudyPlanTransferService(database)
        StudyNotificationCoordinator.createChannels(this)
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            StudyNotificationCoordinator.refresh(this@EstudarioApplication, preferences)
            // Sessão de foco que ficou aberta (app fechado, aparelho reiniciado) é resolvida aqui:
            // ou volta com a notificação, ou é encerrada e o Não Perturbe é devolvido.
            FocusSessionManager.reconcile(this@EstudarioApplication)
        }
    }
}
