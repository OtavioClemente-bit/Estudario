package br.com.estudario.domain.setup

import br.com.estudario.domain.planner.StudyProfile
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/** Estado persistente do assistente de primeira configuração. Os nomes são dados de formato: não renomear sem migração. */
enum class InitialSetupStatus { NOT_STARTED, IN_PROGRESS, DEFERRED, COMPLETED }

enum class InitialSetupStep {
    INTRO,
    COMPETITION,
    EXAM_DATE,
    SYLLABUS_METHOD,
    SYLLABUS_REVIEW,
    PROFILE,
    AVAILABILITY,
    PLAN_METHOD,
    PLAN_REVIEW,
    READY,
}

enum class SyllabusMethod { DIRECT_AI, IMPORT_ESTUDO, MANUAL, CHATGPT }

enum class PlanCreationMethod { AUTOMATIC, EXTERNAL_AI }

data class InitialSetupSnapshot(
    val version: Int = 1,
    val status: InitialSetupStatus = InitialSetupStatus.NOT_STARTED,
    val step: InitialSetupStep = InitialSetupStep.INTRO,
    val competitionId: Long? = null,
    val competitionName: String = "",
    val role: String = "",
    /** ISO-8601 date. A null date is a supported, useful state — it must not block the plan. */
    val examDate: String? = null,
    val syllabusMethod: SyllabusMethod? = null,
    val manualSubjects: List<String> = emptyList(),
    val manualTopics: Map<String, List<String>> = emptyMap(),
    val studyProfile: StudyProfile = StudyProfile.DO_ZERO,
    /** Monday through Sunday, in minutes. Zero means unavailable. */
    val availabilityMinutes: List<Int> = defaultAvailability(),
    val sessionMinutes: Int = 50,
    val planMethod: PlanCreationMethod = PlanCreationMethod.AUTOMATIC,
    val planPreference: String = "",
    val lastValidPlanId: String? = null,
) {
    fun normalized(): InitialSetupSnapshot = copy(
        competitionName = competitionName.trim(),
        role = role.trim(),
        examDate = examDate?.trim()?.takeIf(String::isNotBlank),
        manualSubjects = manualSubjects.map(String::trim).filter(String::isNotBlank).distinct(),
        manualTopics = manualTopics.mapKeys { it.key.trim() }.mapValues { (_, topics) -> topics.map(String::trim).filter(String::isNotBlank).distinct() },
        availabilityMinutes = availabilityMinutes.take(7).map { it.coerceIn(0, 1_440) }.let { values ->
            values + List((7 - values.size).coerceAtLeast(0)) { 0 }
        },
        sessionMinutes = sessionMinutes.coerceIn(15, 180),
        planPreference = planPreference.trim(),
    )

    companion object {
        fun defaultAvailability() = listOf(120, 120, 120, 120, 120, 120, 0)
    }
}

/**
 * Codec deliberadamente pequeno e tolerante. DataStore guarda apenas uma string; campos de texto
 * são URL-encoded para que nomes longos, acentos e separadores não quebrem o snapshot.
 */
object InitialSetupSnapshotCodec {
    private const val FIELD_SEPARATOR = "|"
    private const val LIST_SEPARATOR = "~"

    fun encode(value: InitialSetupSnapshot): String {
        val snapshot = value.normalized()
        return listOf(
            snapshot.version.toString(),
            snapshot.status.name,
            snapshot.step.name,
            snapshot.competitionId?.toString().orEmpty(),
            encodeText(snapshot.competitionName),
            encodeText(snapshot.role),
            encodeText(snapshot.examDate.orEmpty()),
            snapshot.syllabusMethod?.name.orEmpty(),
            snapshot.manualSubjects.joinToString(LIST_SEPARATOR, transform = ::encodeText),
            snapshot.manualTopics.entries.joinToString(LIST_SEPARATOR) { (subject, topics) ->
                "${encodeText(subject)}=${topics.joinToString(",", transform = ::encodeText)}"
            },
            snapshot.studyProfile.name,
            snapshot.availabilityMinutes.joinToString(","),
            snapshot.sessionMinutes.toString(),
            snapshot.planMethod.name,
            encodeText(snapshot.planPreference),
            encodeText(snapshot.lastValidPlanId.orEmpty()),
        ).joinToString(FIELD_SEPARATOR)
    }

    fun decode(raw: String?): InitialSetupSnapshot {
        if (raw.isNullOrBlank()) return InitialSetupSnapshot()
        return runCatching {
            val fields = raw.split(FIELD_SEPARATOR)
            fun field(index: Int) = fields.getOrNull(index).orEmpty()
            InitialSetupSnapshot(
                version = field(0).toIntOrNull() ?: 1,
                status = enumOrDefault(field(1), InitialSetupStatus.NOT_STARTED),
                step = enumOrDefault(field(2), InitialSetupStep.INTRO),
                competitionId = field(3).toLongOrNull(),
                competitionName = decodeText(field(4)),
                role = decodeText(field(5)),
                examDate = decodeText(field(6)).takeIf(String::isNotBlank),
                syllabusMethod = field(7).takeIf(String::isNotBlank)?.let { enumOrDefault(it, SyllabusMethod.MANUAL) },
                manualSubjects = field(8).split(LIST_SEPARATOR).filter(String::isNotBlank).map(::decodeText),
                manualTopics = field(9).split(LIST_SEPARATOR).filter(String::isNotBlank).mapNotNull { entry ->
                    val separator = entry.indexOf('=')
                    if (separator <= 0) null else decodeText(entry.substring(0, separator)) to entry.substring(separator + 1).split(",").filter(String::isNotBlank).map(::decodeText)
                }.toMap(),
                studyProfile = enumOrDefault(field(10), StudyProfile.DO_ZERO),
                availabilityMinutes = field(11).split(",").mapNotNull(String::toIntOrNull).ifEmpty { InitialSetupSnapshot.defaultAvailability() },
                sessionMinutes = field(12).toIntOrNull() ?: 50,
                planMethod = enumOrDefault(field(13), PlanCreationMethod.AUTOMATIC),
                planPreference = decodeText(field(14)),
                lastValidPlanId = decodeText(field(15)).takeIf(String::isNotBlank),
            ).normalized()
        }.getOrDefault(InitialSetupSnapshot())
    }

    private fun encodeText(value: String) = URLEncoder.encode(value, StandardCharsets.UTF_8.name())
    private fun decodeText(value: String) = URLDecoder.decode(value, StandardCharsets.UTF_8.name())

    private inline fun <reified T : Enum<T>> enumOrDefault(value: String, fallback: T): T =
        runCatching { enumValueOf<T>(value) }.getOrDefault(fallback)
}

/** State-machine guard used by UI and tests; persistence is handled separately. */
object InitialSetupTransitions {
    private val forward = mapOf(
        InitialSetupStep.INTRO to InitialSetupStep.COMPETITION,
        InitialSetupStep.COMPETITION to InitialSetupStep.EXAM_DATE,
        InitialSetupStep.EXAM_DATE to InitialSetupStep.SYLLABUS_METHOD,
        InitialSetupStep.SYLLABUS_METHOD to InitialSetupStep.SYLLABUS_REVIEW,
        InitialSetupStep.SYLLABUS_REVIEW to InitialSetupStep.PROFILE,
        InitialSetupStep.PROFILE to InitialSetupStep.AVAILABILITY,
        InitialSetupStep.AVAILABILITY to InitialSetupStep.PLAN_METHOD,
        InitialSetupStep.PLAN_METHOD to InitialSetupStep.PLAN_REVIEW,
        InitialSetupStep.PLAN_REVIEW to InitialSetupStep.READY,
    )

    fun canAdvance(from: InitialSetupStep, to: InitialSetupStep): Boolean =
        forward[from] == to

    fun previous(step: InitialSetupStep): InitialSetupStep? =
        forward.entries.firstOrNull { it.value == step }?.key
}

object InitialSetupWorkspace {
    /** Any restored contest or plan is evidence that the person already has a workspace. */
    fun hasExistingData(competitionCount: Int, planCount: Int): Boolean = competitionCount > 0 || planCount > 0
}
