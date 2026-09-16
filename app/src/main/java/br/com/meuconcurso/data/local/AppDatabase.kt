package br.com.meuconcurso.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.meuconcurso.data.local.planner.*

@Database(
    entities = [
        CompetitionEntity::class, SubjectEntity::class, TopicEntity::class, SummaryEntity::class,
        QuestionEntity::class, QuestionOptionEntity::class, QuestionAttemptEntity::class,
        ErrorNotebookEntryEntity::class, ReviewScheduleEntity::class, ReviewHistoryEntity::class,
        StudyQueueEntity::class, StudySessionEntity::class, UserNoteEntity::class, TagEntity::class,
        QuestionTagCrossRef::class, TheoryDocumentEntity::class, TheoryMarkEntity::class,
        TopicSnippetEntity::class, ErrorConceptEntity::class, ErrorConceptEntryCrossRef::class,
        ReviewSessionEntity::class, QueueEventEntity::class, QuestionSessionEntity::class,
        ImportPackageEntity::class,
        StudyPlanEntity::class, StudyPlanRevisionEntity::class, StudyAvailabilityEntity::class,
        StudyDayOverrideEntity::class, PlanSubjectEntity::class, AnnualPhaseEntity::class,
        AnnualPhaseSubjectEntity::class, AnnualPhaseTopicEntity::class, MonthlyPlanEntity::class,
        MonthlyPlanSubjectEntity::class, MonthlyPlanTopicEntity::class, WeeklyPlanEntity::class,
        PlanTaskEntity::class, PlanTaskDependencyEntity::class, StudyTaskExecutionEntity::class,
    ],
    version = 5,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): AppDao
    abstract fun plannerDao(): PlannerDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `theory_documents` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `topicId` INTEGER NOT NULL, `title` TEXT NOT NULL, `markdown` TEXT NOT NULL, `externalId` TEXT, `lastReadBlock` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_theory_documents_topicId` ON `theory_documents` (`topicId`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_theory_documents_externalId` ON `theory_documents` (`externalId`)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `theory_marks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `theoryId` INTEGER NOT NULL, `blockIndex` INTEGER NOT NULL, `quote` TEXT NOT NULL, `note` TEXT NOT NULL, `color` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`theoryId`) REFERENCES `theory_documents`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_theory_marks_theoryId` ON `theory_marks` (`theoryId`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_theory_marks_theoryId_blockIndex` ON `theory_marks` (`theoryId`, `blockIndex`)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `summaries` ADD COLUMN `kind` TEXT NOT NULL DEFAULT 'COMPLETO'")
                db.execSQL("ALTER TABLE `error_notebook` ADD COLUMN `selectedAnswer` TEXT")
                db.execSQL("ALTER TABLE `error_notebook` ADD COLUMN `correctAnswer` TEXT")
                db.execSQL("ALTER TABLE `error_notebook` ADD COLUMN `status` TEXT NOT NULL DEFAULT 'NOVO'")
                db.execSQL("ALTER TABLE `review_schedule` ADD COLUMN `ignoredAt` INTEGER")
                db.execSQL("ALTER TABLE `review_schedule` ADD COLUMN `perceivedDifficulty` TEXT")
                db.execSQL("ALTER TABLE `review_schedule` ADD COLUMN `questionCorrect` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `review_schedule` ADD COLUMN `questionTotal` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `study_queue` ADD COLUMN `enqueuedAt` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `study_queue` ADD COLUMN `postponements` INTEGER NOT NULL DEFAULT 0")

                db.execSQL("CREATE TABLE IF NOT EXISTS `topic_snippets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `topicId` INTEGER NOT NULL, `kind` TEXT NOT NULL, `text` TEXT NOT NULL, `isFavorite` INTEGER NOT NULL, `externalId` TEXT, `position` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_topic_snippets_topicId` ON `topic_snippets` (`topicId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_topic_snippets_kind` ON `topic_snippets` (`kind`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_topic_snippets_externalId` ON `topic_snippets` (`externalId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_topic_snippets_isFavorite` ON `topic_snippets` (`isFavorite`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `error_concepts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `topicId` INTEGER NOT NULL, `title` TEXT NOT NULL, `summary` TEXT NOT NULL, `isFavorite` INTEGER NOT NULL, `externalId` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_error_concepts_topicId` ON `error_concepts` (`topicId`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_error_concepts_externalId` ON `error_concepts` (`externalId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_error_concepts_isFavorite` ON `error_concepts` (`isFavorite`)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `error_concept_entries` (`conceptId` INTEGER NOT NULL, `errorEntryId` INTEGER NOT NULL, PRIMARY KEY(`conceptId`, `errorEntryId`), FOREIGN KEY(`conceptId`) REFERENCES `error_concepts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`errorEntryId`) REFERENCES `error_notebook`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_error_concept_entries_conceptId` ON `error_concept_entries` (`conceptId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_error_concept_entries_errorEntryId` ON `error_concept_entries` (`errorEntryId`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `review_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `reviewId` INTEGER NOT NULL, `topicId` INTEGER NOT NULL, `startedAt` INTEGER NOT NULL, `completedAt` INTEGER NOT NULL, `recalled` INTEGER NOT NULL, `forgotten` INTEGER NOT NULL, `questionCorrect` INTEGER NOT NULL, `questionTotal` INTEGER NOT NULL, `perceivedDifficulty` TEXT NOT NULL, FOREIGN KEY(`reviewId`) REFERENCES `review_schedule`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_review_sessions_reviewId` ON `review_sessions` (`reviewId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_review_sessions_topicId` ON `review_sessions` (`topicId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_review_sessions_completedAt` ON `review_sessions` (`completedAt`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `queue_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `topicId` INTEGER NOT NULL, `type` TEXT NOT NULL, `occurredAt` INTEGER NOT NULL, `reason` TEXT NOT NULL, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_queue_events_topicId` ON `queue_events` (`topicId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_queue_events_occurredAt` ON `queue_events` (`occurredAt`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `question_sessions` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `startedAt` INTEGER NOT NULL, `completedAt` INTEGER NOT NULL, `durationSeconds` INTEGER NOT NULL, `questionCount` INTEGER NOT NULL, `correctCount` INTEGER NOT NULL, `subjectIdsText` TEXT NOT NULL, `topicIdsText` TEXT NOT NULL, PRIMARY KEY(`id`))")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_question_sessions_startedAt` ON `question_sessions` (`startedAt`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_question_sessions_completedAt` ON `question_sessions` (`completedAt`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_question_sessions_type` ON `question_sessions` (`type`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `import_packages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageId` TEXT NOT NULL, `schemaVersion` INTEGER NOT NULL, `importedAt` INTEGER NOT NULL, `contentHash` TEXT NOT NULL)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_import_packages_packageId` ON `import_packages` (`packageId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_import_packages_importedAt` ON `import_packages` (`importedAt`)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `competitions` ADD COLUMN `externalId` TEXT")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_competitions_externalId` ON `competitions` (`externalId`)")
                db.execSQL("ALTER TABLE `subjects` ADD COLUMN `externalId` TEXT")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_subjects_externalId` ON `subjects` (`externalId`)")
                db.execSQL("ALTER TABLE `topics` ADD COLUMN `externalId` TEXT")
                db.execSQL("ALTER TABLE `topics` ADD COLUMN `contentOriginType` TEXT NOT NULL DEFAULT 'EDITAL'")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_topics_externalId` ON `topics` (`externalId`)")

                db.execSQL("ALTER TABLE `questions` ADD COLUMN `questionSourceType` TEXT NOT NULL DEFAULT 'AUTHORIAL'")
                db.execSQL("ALTER TABLE `questions` ADD COLUMN `sourceId` TEXT")
                db.execSQL("ALTER TABLE `questions` ADD COLUMN `sourceUrl` TEXT")
                db.execSQL("ALTER TABLE `questions` ADD COLUMN `normalizedHash` TEXT")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_sourceId` ON `questions` (`sourceId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_normalizedHash` ON `questions` (`normalizedHash`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_questionSourceType` ON `questions` (`questionSourceType`)")

                db.execSQL("ALTER TABLE `error_concepts` ADD COLUMN `errorCount` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `error_concepts` ADD COLUMN `correctAfterErrorCount` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `error_concepts` ADD COLUMN `lastErrorAt` INTEGER")
                db.execSQL("ALTER TABLE `error_concepts` ADD COLUMN `lastReviewedAt` INTEGER")
                db.execSQL("ALTER TABLE `error_concepts` ADD COLUMN `priority` TEXT NOT NULL DEFAULT 'NORMAL'")
                db.execSQL("ALTER TABLE `error_concepts` ADD COLUMN `mastered` INTEGER NOT NULL DEFAULT 0")

                db.execSQL("ALTER TABLE `study_sessions` ADD COLUMN `competitionId` INTEGER")
                db.execSQL("ALTER TABLE `study_sessions` ADD COLUMN `subjectId` INTEGER")
                db.execSQL("ALTER TABLE `study_sessions` ADD COLUMN `durationSeconds` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `study_sessions` ADD COLUMN `questionCount` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `study_sessions` ADD COLUMN `correctCount` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `study_sessions` ADD COLUMN `wrongCount` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `study_sessions` ADD COLUMN `notes` TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE `study_sessions` ADD COLUMN `sourcePackageId` TEXT")

                db.execSQL("DROP INDEX IF EXISTS `index_import_packages_packageId`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_import_packages_packageId` ON `import_packages` (`packageId`)")
                db.execSQL("ALTER TABLE `import_packages` ADD COLUMN `fileName` TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE `import_packages` ADD COLUMN `createdCount` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `import_packages` ADD COLUMN `updatedCount` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `import_packages` ADD COLUMN `ignoredCount` INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `study_plans` (`id` TEXT NOT NULL, `competitionId` INTEGER NOT NULL, `name` TEXT NOT NULL, `objective` TEXT NOT NULL, `startEpochDay` INTEGER NOT NULL, `examEpochDay` INTEGER, `active` INTEGER NOT NULL, `masterPlan` INTEGER NOT NULL, `archived` INTEGER NOT NULL, `revision` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`competitionId`) REFERENCES `competitions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_plans_competitionId` ON `study_plans` (`competitionId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_plans_competitionId_active` ON `study_plans` (`competitionId`, `active`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_plans_competitionId_masterPlan` ON `study_plans` (`competitionId`, `masterPlan`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_plans_archived` ON `study_plans` (`archived`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `study_plan_revisions` (`planId` TEXT NOT NULL, `revision` INTEGER NOT NULL, `baseRevision` INTEGER NOT NULL, `reason` TEXT NOT NULL, `proposalId` TEXT, `summary` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`planId`, `revision`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_plan_revisions_planId` ON `study_plan_revisions` (`planId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_plan_revisions_createdAt` ON `study_plan_revisions` (`createdAt`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `study_availability` (`planId` TEXT NOT NULL, `dayOfWeek` INTEGER NOT NULL, `availableMinutes` INTEGER NOT NULL, `unavailable` INTEGER NOT NULL, `mode` TEXT NOT NULL, PRIMARY KEY(`planId`, `dayOfWeek`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_availability_planId` ON `study_availability` (`planId`)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `study_day_overrides` (`planId` TEXT NOT NULL, `epochDay` INTEGER NOT NULL, `availableMinutes` INTEGER, `unavailable` INTEGER NOT NULL, `locked` INTEGER NOT NULL, PRIMARY KEY(`planId`, `epochDay`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_day_overrides_planId` ON `study_day_overrides` (`planId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_day_overrides_epochDay` ON `study_day_overrides` (`epochDay`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `plan_subjects` (`planId` TEXT NOT NULL, `subjectId` INTEGER NOT NULL, `subjectNameSnapshot` TEXT NOT NULL, `priority` TEXT NOT NULL, `paused` INTEGER NOT NULL, `minimumMaintenanceMinutes` INTEGER NOT NULL, `weightOverride` INTEGER, `position` INTEGER NOT NULL, PRIMARY KEY(`planId`, `subjectId`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_plan_subjects_planId` ON `plan_subjects` (`planId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_plan_subjects_subjectId` ON `plan_subjects` (`subjectId`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `annual_phases` (`id` TEXT NOT NULL, `planId` TEXT NOT NULL, `position` INTEGER NOT NULL, `name` TEXT NOT NULL, `objective` TEXT NOT NULL, `completionCriteria` TEXT NOT NULL, `startEpochDay` INTEGER NOT NULL, `endEpochDay` INTEGER NOT NULL, `targetMinutes` INTEGER NOT NULL, `targetQuestions` INTEGER NOT NULL, `targetDiscursives` INTEGER NOT NULL, `targetPercent` INTEGER NOT NULL, `validFromRevision` INTEGER NOT NULL, `validUntilRevision` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_annual_phases_planId` ON `annual_phases` (`planId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_annual_phases_planId_startEpochDay_endEpochDay` ON `annual_phases` (`planId`, `startEpochDay`, `endEpochDay`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_annual_phases_planId_validUntilRevision` ON `annual_phases` (`planId`, `validUntilRevision`)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `annual_phase_subjects` (`phaseId` TEXT NOT NULL, `subjectId` INTEGER NOT NULL, PRIMARY KEY(`phaseId`, `subjectId`), FOREIGN KEY(`phaseId`) REFERENCES `annual_phases`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_annual_phase_subjects_phaseId` ON `annual_phase_subjects` (`phaseId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_annual_phase_subjects_subjectId` ON `annual_phase_subjects` (`subjectId`)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `annual_phase_topics` (`phaseId` TEXT NOT NULL, `topicId` INTEGER NOT NULL, PRIMARY KEY(`phaseId`, `topicId`), FOREIGN KEY(`phaseId`) REFERENCES `annual_phases`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_annual_phase_topics_phaseId` ON `annual_phase_topics` (`phaseId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_annual_phase_topics_topicId` ON `annual_phase_topics` (`topicId`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `monthly_plans` (`id` TEXT NOT NULL, `planId` TEXT NOT NULL, `yearMonth` TEXT NOT NULL, `focus` TEXT NOT NULL, `targetMinutes` INTEGER NOT NULL, `targetQuestions` INTEGER NOT NULL, `targetDiscursives` INTEGER NOT NULL, `targetPercent` INTEGER NOT NULL, `validFromRevision` INTEGER NOT NULL, `validUntilRevision` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_monthly_plans_planId` ON `monthly_plans` (`planId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_monthly_plans_planId_yearMonth` ON `monthly_plans` (`planId`, `yearMonth`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_monthly_plans_planId_validUntilRevision` ON `monthly_plans` (`planId`, `validUntilRevision`)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `monthly_plan_subjects` (`monthlyPlanId` TEXT NOT NULL, `subjectId` INTEGER NOT NULL, `maintenance` INTEGER NOT NULL, PRIMARY KEY(`monthlyPlanId`, `subjectId`), FOREIGN KEY(`monthlyPlanId`) REFERENCES `monthly_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_monthly_plan_subjects_monthlyPlanId` ON `monthly_plan_subjects` (`monthlyPlanId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_monthly_plan_subjects_subjectId` ON `monthly_plan_subjects` (`subjectId`)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `monthly_plan_topics` (`monthlyPlanId` TEXT NOT NULL, `topicId` INTEGER NOT NULL, PRIMARY KEY(`monthlyPlanId`, `topicId`), FOREIGN KEY(`monthlyPlanId`) REFERENCES `monthly_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_monthly_plan_topics_monthlyPlanId` ON `monthly_plan_topics` (`monthlyPlanId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_monthly_plan_topics_topicId` ON `monthly_plan_topics` (`topicId`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `weekly_plans` (`id` TEXT NOT NULL, `planId` TEXT NOT NULL, `weekStartEpochDay` INTEGER NOT NULL, `objective` TEXT NOT NULL, `targetMinutes` INTEGER NOT NULL, `targetQuestions` INTEGER NOT NULL, `targetDiscursives` INTEGER NOT NULL, `validFromRevision` INTEGER NOT NULL, `validUntilRevision` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_weekly_plans_planId` ON `weekly_plans` (`planId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_weekly_plans_planId_weekStartEpochDay` ON `weekly_plans` (`planId`, `weekStartEpochDay`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_weekly_plans_planId_validUntilRevision` ON `weekly_plans` (`planId`, `validUntilRevision`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `plan_tasks` (`id` TEXT NOT NULL, `planId` TEXT NOT NULL, `competitionId` INTEGER NOT NULL, `annualPhaseId` TEXT, `monthlyPlanId` TEXT, `weeklyPlanId` TEXT, `subjectId` INTEGER, `topicId` INTEGER, `subjectNameSnapshot` TEXT NOT NULL, `topicNameSnapshot` TEXT, `scheduledEpochDay` INTEGER NOT NULL, `type` TEXT NOT NULL, `plannedMinutes` INTEGER NOT NULL, `plannedQuestions` INTEGER NOT NULL, `priority` TEXT NOT NULL, `status` TEXT NOT NULL, `origin` TEXT NOT NULL, `notes` TEXT NOT NULL, `locked` INTEGER NOT NULL, `progressNote` TEXT NOT NULL, `replannedFromTaskId` TEXT, `createdRevision` INTEGER NOT NULL, `updatedRevision` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`competitionId`) REFERENCES `competitions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL, FOREIGN KEY(`annualPhaseId`) REFERENCES `annual_phases`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL, FOREIGN KEY(`monthlyPlanId`) REFERENCES `monthly_plans`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL, FOREIGN KEY(`weeklyPlanId`) REFERENCES `weekly_plans`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL)")
                listOf("planId", "competitionId", "subjectId", "topicId", "annualPhaseId", "monthlyPlanId", "weeklyPlanId").forEach { column -> db.execSQL("CREATE INDEX IF NOT EXISTS `index_plan_tasks_$column` ON `plan_tasks` (`$column`)") }
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_plan_tasks_planId_scheduledEpochDay` ON `plan_tasks` (`planId`, `scheduledEpochDay`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_plan_tasks_planId_status` ON `plan_tasks` (`planId`, `status`)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `plan_task_dependencies` (`taskId` TEXT NOT NULL, `dependsOnTaskId` TEXT NOT NULL, PRIMARY KEY(`taskId`, `dependsOnTaskId`), FOREIGN KEY(`taskId`) REFERENCES `plan_tasks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`dependsOnTaskId`) REFERENCES `plan_tasks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_plan_task_dependencies_taskId` ON `plan_task_dependencies` (`taskId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_plan_task_dependencies_dependsOnTaskId` ON `plan_task_dependencies` (`dependsOnTaskId`)")

                db.execSQL("CREATE TABLE IF NOT EXISTS `study_task_executions` (`id` TEXT NOT NULL, `planId` TEXT NOT NULL, `taskId` TEXT, `competitionId` INTEGER NOT NULL, `subjectId` INTEGER, `topicId` INTEGER, `startedAt` INTEGER NOT NULL, `completedAt` INTEGER NOT NULL, `actualMinutes` INTEGER NOT NULL, `questionsDone` INTEGER NOT NULL, `correctAnswers` INTEGER NOT NULL, `notes` TEXT NOT NULL, `perceivedDifficulty` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`taskId`) REFERENCES `plan_tasks`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL, FOREIGN KEY(`competitionId`) REFERENCES `competitions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL, FOREIGN KEY(`topicId`) REFERENCES `topics`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL)")
                listOf("planId", "taskId", "competitionId", "subjectId", "topicId", "completedAt").forEach { column -> db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_task_executions_$column` ON `study_task_executions` (`$column`)") }

                db.execSQL("CREATE TRIGGER IF NOT EXISTS `study_plans_activate_one_insert` AFTER INSERT ON `study_plans` WHEN NEW.active = 1 BEGIN UPDATE `study_plans` SET active = 0 WHERE competitionId = NEW.competitionId AND id <> NEW.id; END")
                db.execSQL("CREATE TRIGGER IF NOT EXISTS `study_plans_activate_one_update` AFTER UPDATE OF active ON `study_plans` WHEN NEW.active = 1 BEGIN UPDATE `study_plans` SET active = 0 WHERE competitionId = NEW.competitionId AND id <> NEW.id; END")
                db.execSQL("CREATE TRIGGER IF NOT EXISTS `study_plans_master_one_insert` AFTER INSERT ON `study_plans` WHEN NEW.masterPlan = 1 BEGIN UPDATE `study_plans` SET masterPlan = 0 WHERE competitionId = NEW.competitionId AND id <> NEW.id; END")
                db.execSQL("CREATE TRIGGER IF NOT EXISTS `study_plans_master_one_update` AFTER UPDATE OF masterPlan ON `study_plans` WHEN NEW.masterPlan = 1 BEGIN UPDATE `study_plans` SET masterPlan = 0 WHERE competitionId = NEW.competitionId AND id <> NEW.id; END")
                db.execSQL("CREATE TRIGGER IF NOT EXISTS `study_plans_archive_guard_insert` AFTER INSERT ON `study_plans` WHEN NEW.archived = 1 BEGIN UPDATE `study_plans` SET active = 0, masterPlan = 0 WHERE id = NEW.id; END")
                db.execSQL("CREATE TRIGGER IF NOT EXISTS `study_plans_archive_guard_update` AFTER UPDATE OF archived ON `study_plans` WHEN NEW.archived = 1 BEGIN UPDATE `study_plans` SET active = 0, masterPlan = 0 WHERE id = NEW.id; END")
                db.execSQL("CREATE TRIGGER IF NOT EXISTS `plan_task_dependencies_same_plan` BEFORE INSERT ON `plan_task_dependencies` WHEN (SELECT planId FROM plan_tasks WHERE id = NEW.taskId) <> (SELECT planId FROM plan_tasks WHERE id = NEW.dependsOnTaskId) OR NEW.taskId = NEW.dependsOnTaskId BEGIN SELECT RAISE(ABORT, 'Task dependencies must belong to the same plan and cannot reference themselves.'); END")
            }
        }

        fun create(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "meu-concurso.db",
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5).build()
    }
}
