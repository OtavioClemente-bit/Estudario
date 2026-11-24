package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import java.io.File

/**
 * Editais reais do catálogo (exam_catalog.subjects, copiados em 23/11/2026 para src/jvmTest/resources/editais)
 * montados como o app monta um plano novo a partir do catálogo: uma matéria por disciplina, os tópicos na
 * ordem do edital, nenhuma questão salva ainda e nada estudado.
 */
object CatalogEditalFixtures {
    data class Subject(val name: String, val examQuestions: Int, val topics: List<String>)

    fun load(id: String): List<Subject> {
        val file = listOf(File("src/jvmTest/resources/editais/$id.txt"), File("shared/src/jvmTest/resources/editais/$id.txt")).first { it.exists() }
        val out = mutableListOf<Subject>()
        file.readLines().forEach { line ->
            when {
                line.startsWith("## ") -> line.removePrefix("## ").split("|").let { out += Subject(it[0], it.getOrNull(1)?.toIntOrNull() ?: 0, emptyList()) }
                line.startsWith("- ") -> out[out.lastIndex] = out.last().copy(topics = out.last().topics + line.removePrefix("- "))
            }
        }
        return out
    }

    /** O peso que o app dá pela quantidade de questões da matéria na prova (ExamWeightsApplier, aproximado). */
    fun priorityOf(questions: Int): PlanPriority = when {
        questions >= 15 -> PlanPriority.CRITICAL
        questions >= 10 -> PlanPriority.HIGH
        questions >= 5 -> PlanPriority.MEDIUM
        questions > 0 -> PlanPriority.LOW
        else -> PlanPriority.MEDIUM
    }

    fun input(
        subjects: List<Subject>,
        today: LocalDate,
        dailyMinutes: Int,
        examInDays: Int?,
        profile: StudyProfile = StudyProfile.DO_ZERO,
        free: Boolean = false,
        studyDays: Int = 7,
    ): PlannerSnapshotBuilder.Input {
        val planSubjects = subjects.mapIndexed { i, s ->
            PlannerSnapshotBuilder.PlanSubject(i + 1L, s.name, priorityOf(s.examQuestions), false, 0, null, i, PersonalDifficulty.NORMAL, InitialKnowledge.NONE)
        }
        val topics = subjects.flatMapIndexed { i, s ->
            s.topics.mapIndexed { p, title -> PlannerSnapshotBuilder.Topic(1000L * (i + 1) + p, i + 1L, null, title, p, false, null) }
        }
        return PlannerSnapshotBuilder.Input(
            plan = PlannerSnapshotBuilder.Plan("plan-cat", 0, today.toEpochDay(), examInDays?.let { today.plusDays(it.toLong()).toEpochDay() }, StudyMethodConfig.forProfile(profile)),
            planSubjects = planSubjects,
            topics = topics,
            questionsByTopic = emptyMap(),
            attempts = emptyList(),
            pendingReviews = emptyList(),
            tasks = emptyList(),
            executions = emptyList(),
            availability = (1..7).map { PlannerSnapshotBuilder.Availability(it, if (it <= studyDays) dailyMinutes else 0, it > studyDays) },
            dayOverrides = emptyList(),
            today = today,
            zone = TimeZone.UTC,
            freeRules = free,
        )
    }
}
