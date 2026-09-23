package br.com.estudario.domain.setup

import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.StudyDimensions
import br.com.estudario.domain.planner.StudyProfile
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/** Estado persistente do assistente de primeira configuração. Os nomes são dados de formato: não renomear sem migração. */
enum class InitialSetupStatus { NOT_STARTED, IN_PROGRESS, DEFERRED, COMPLETED }

/**
 * Escala antiga de dificuldade, em três níveis.
 *
 * Mantida só para ler snapshots gravados antes do Smart Planner e para telas que ainda mostram
 * três opções. O assistente usa [PersonalDifficulty], de cinco níveis.
 */
enum class SubjectDifficulty { EASY, MEDIUM, HARD }

/**
 * Passos do assistente.
 *
 * A ordem segue a conversa, não o banco: primeiro o concurso e o edital, depois **como a prova
 * distribui a atenção** (prioridade), depois **como a pessoa se distribui** (dificuldade e
 * conhecimento), depois a rotina, e só então o resumo antes de montar. Perguntas relacionadas
 * moram no mesmo passo, dificuldade e conhecimento prévio são uma tela só, e disponibilidade,
 * tamanho de sessão e preferência de variedade também.
 */
enum class InitialSetupStep {
    INTRO,
    COMPETITION,
    EXAM_DATE,
    SYLLABUS_METHOD,
    SYLLABUS_REVIEW,
    /** "Como esta prova distribui a atenção?", prioridade por matéria, já preenchida e editável. */
    SUBJECT_PRIORITY,
    /** "E para você?", dificuldade e conhecimento prévio na mesma tela, com padrões neutros. */
    SUBJECT_DIFFICULTY,
    AVAILABILITY,
    PROFILE,
    /** "Seu perfil de estudo", o resumo que aparece imediatamente antes de gerar. */
    PLAN_SUMMARY,
    PLAN_METHOD,
    PLAN_REVIEW,
    READY,
}

enum class SyllabusMethod { DIRECT_AI, IMPORT_ESTUDO, MANUAL, CHATGPT }

enum class PlanCreationMethod { AUTOMATIC, EXTERNAL_AI }

/**
 * Preferência de alternância entre matérias.
 *
 * Só existe porque muda comportamento real: alimenta `interleaveSubjects` e o teto diário por
 * matéria no [br.com.estudario.domain.planner.PlannerPolicy]. Se um dia deixar de mudar algo, a
 * pergunta sai do assistente.
 */
enum class SubjectVariety(val label: String, val description: String) {
    MORE_VARIETY("Mais variedade", "Prefiro alternar bastante entre matérias no mesmo dia."),
    BALANCED("Equilibrado", "Um pouco de alternância, sem picar demais o dia."),
    MORE_CONTINUITY("Mais continuidade", "Prefiro ficar mais tempo na mesma matéria."),
    ;

    /** Teto de um dia que uma mesma matéria pode ocupar. 100 = sem teto. */
    val dailySubjectSharePercent: Int
        get() = when (this) {
            MORE_VARIETY -> 40
            BALANCED -> 60
            MORE_CONTINUITY -> 100
        }

    val interleave: Boolean get() = this != MORE_CONTINUITY
}

data class InitialSetupSnapshot(
    val version: Int = InitialSetupSnapshotCodec.CURRENT_VERSION,
    val status: InitialSetupStatus = InitialSetupStatus.NOT_STARTED,
    val step: InitialSetupStep = InitialSetupStep.INTRO,
    val competitionId: Long? = null,
    val competitionName: String = "",
    val role: String = "",
    /** ISO-8601 date. A null date is a supported, useful state, it must not block the plan. */
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
    /**
     * Eixo 2, quanto a matéria custa para esta pessoa. Todas começam em
     * [PersonalDifficulty.NORMAL]; a pessoa mexe só no que precisa, e por isso a ausência de uma
     * chave é uma resposta válida, não um campo pendente.
     */
    val subjectDifficulties: Map<String, PersonalDifficulty> = emptyMap(),
    /** Eixo 3, quanto a pessoa já sabia da matéria antes de começar. */
    val subjectKnowledge: Map<String, InitialKnowledge> = emptyMap(),
    /**
     * Eixo 1, ajustes manuais da prioridade da prova. Vazio significa "aceito o que o edital
     * disse"; só entra aqui a matéria cuja prioridade a pessoa mudou à mão.
     */
    val subjectPriorities: Map<String, ExamPriority> = emptyMap(),
    val variety: SubjectVariety = SubjectVariety.BALANCED,
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
        subjectDifficulties = subjectDifficulties.mapKeys { it.key.trim() }.filterKeys(String::isNotBlank),
        subjectKnowledge = subjectKnowledge.mapKeys { it.key.trim() }.filterKeys(String::isNotBlank),
        subjectPriorities = subjectPriorities.mapKeys { it.key.trim() }.filterKeys(String::isNotBlank),
    )

    fun subjectDifficultiesFor(subjectIds: Set<String>): Map<String, PersonalDifficulty> =
        subjectDifficulties.filterKeys { it.isNotBlank() && it in subjectIds }

    /**
     * Os três eixos de uma matéria, com os padrões neutros no lugar do que não foi respondido.
     *
     * É esta função que torna o assistente curto: ninguém precisa classificar 120 tópicos nem
     * responder sobre todas as matérias para o plano existir.
     */
    fun dimensionsFor(subjectId: String, examPriority: ExamPriority): StudyDimensions = StudyDimensions(
        examPriority = subjectPriorities[subjectId] ?: examPriority,
        personalDifficulty = subjectDifficulties[subjectId] ?: PersonalDifficulty.DEFAULT,
        initialKnowledge = subjectKnowledge[subjectId] ?: InitialKnowledge.DEFAULT,
    )

    /**
     * Se todas as matérias têm dificuldade respondida.
     *
     * Deixou de ser condição para avançar, o assistente não exige mais resposta matéria por
     * matéria, mas continua útil para telas que queiram mostrar o quanto já foi revisado.
     */
    fun hasAllSubjectDifficulties(subjectIds: Set<String>): Boolean =
        subjectIds.isNotEmpty() && subjectIds.all { it.isNotBlank() && it in subjectDifficulties }

    /** Quantas matérias a pessoa ajustou de fato, o assistente mostra isso em vez de exigir tudo. */
    fun tunedSubjectCount(subjectIds: Set<String>): Int =
        subjectIds.count { it in subjectDifficulties || it in subjectKnowledge || it in subjectPriorities }

    val weeklyMinutes: Int get() = availabilityMinutes.sum()

    companion object {
        fun defaultAvailability() = listOf(120, 120, 120, 120, 120, 120, 0)
    }
}

/**
 * Codec deliberadamente pequeno e tolerante. DataStore guarda apenas uma string; campos de texto
 * são URL-encoded para que nomes longos, acentos e separadores não quebrem o snapshot.
 *
 * Campos novos entram sempre no fim: um snapshot antigo devolve string vazia nos índices que não
 * existiam e cai no padrão, sem migração.
 */
object InitialSetupSnapshotCodec {
    const val CURRENT_VERSION = 3
    private const val FIELD_SEPARATOR = "|"
    private const val LIST_SEPARATOR = "~"

    fun encode(value: InitialSetupSnapshot): String {
        val snapshot = value.normalized()
        return listOf(
            CURRENT_VERSION.toString(),
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
            encodeMap(snapshot.subjectDifficulties) { it.name },
            // --- versão 3 -----------------------------------------------------------------
            encodeMap(snapshot.subjectKnowledge) { it.name },
            encodeMap(snapshot.subjectPriorities) { it.name },
            snapshot.variety.name,
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
                subjectDifficulties = decodeMap(field(16), ::decodePersonalDifficulty),
                subjectKnowledge = decodeMap(field(17)) { enumOrDefault(it, InitialKnowledge.NONE) },
                subjectPriorities = decodeMap(field(18)) { enumOrDefault(it, ExamPriority.MEDIUM) },
                variety = enumOrDefault(field(19), SubjectVariety.BALANCED),
            ).normalized()
        }.getOrDefault(InitialSetupSnapshot())
    }

    /**
     * Lê tanto a escala nova de cinco níveis quanto a antiga de três.
     *
     * Um snapshot gravado antes do Smart Planner traz `MEDIUM`, que não existe em
     * [PersonalDifficulty], vira `NORMAL`, que é a mesma resposta na escala nova.
     */
    private fun decodePersonalDifficulty(raw: String): PersonalDifficulty =
        runCatching { PersonalDifficulty.valueOf(raw) }.getOrElse {
            when (runCatching { SubjectDifficulty.valueOf(raw) }.getOrNull()) {
                SubjectDifficulty.EASY -> PersonalDifficulty.EASY
                SubjectDifficulty.HARD -> PersonalDifficulty.HARD
                else -> PersonalDifficulty.NORMAL
            }
        }

    private fun <T> encodeMap(values: Map<String, T>, name: (T) -> String): String =
        values.entries.joinToString(LIST_SEPARATOR) { (id, value) -> "${encodeText(id)}=${name(value)}" }

    private fun <T> decodeMap(raw: String, parse: (String) -> T): Map<String, T> =
        raw.split(LIST_SEPARATOR).mapNotNull { entry ->
            val separator = entry.lastIndexOf('=')
            if (separator <= 0) null else {
                val id = decodeText(entry.substring(0, separator)).trim()
                if (id.isBlank()) null else id to parse(entry.substring(separator + 1))
            }
        }.toMap()

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
        // O edital entra, e a conversa passa imediatamente a "como a prova distribui a atenção".
        InitialSetupStep.SYLLABUS_REVIEW to InitialSetupStep.SUBJECT_PRIORITY,
        InitialSetupStep.SUBJECT_PRIORITY to InitialSetupStep.SUBJECT_DIFFICULTY,
        InitialSetupStep.SUBJECT_DIFFICULTY to InitialSetupStep.AVAILABILITY,
        InitialSetupStep.AVAILABILITY to InitialSetupStep.PROFILE,
        InitialSetupStep.PROFILE to InitialSetupStep.PLAN_SUMMARY,
        InitialSetupStep.PLAN_SUMMARY to InitialSetupStep.PLAN_METHOD,
        InitialSetupStep.PLAN_METHOD to InitialSetupStep.PLAN_REVIEW,
        InitialSetupStep.PLAN_REVIEW to InitialSetupStep.READY,
    )

    fun canAdvance(from: InitialSetupStep, to: InitialSetupStep): Boolean =
        forward[from] == to

    /**
     * Só o edital é obrigatório.
     *
     * Os passos dos três eixos deixaram de travar o avanço: todas as matérias já começam com um
     * valor neutro válido, e obrigar alguém a responder matéria por matéria era exatamente o
     * formulário que o assistente veio substituir.
     */
    fun canAdvance(snapshot: InitialSetupSnapshot, to: InitialSetupStep, subjectIds: Set<String>): Boolean =
        canAdvance(snapshot.step, to) && when (snapshot.step) {
            InitialSetupStep.SYLLABUS_REVIEW -> subjectIds.isNotEmpty()
            InitialSetupStep.AVAILABILITY -> snapshot.availabilityMinutes.any { it > 0 }
            else -> true
        }

    fun previous(step: InitialSetupStep): InitialSetupStep? =
        forward.entries.firstOrNull { it.value == step }?.key
}

object InitialSetupWorkspace {
    /** Any restored contest or plan is evidence that the person already has a workspace. */
    fun hasExistingData(competitionCount: Int, planCount: Int): Boolean = competitionCount > 0 || planCount > 0
}
