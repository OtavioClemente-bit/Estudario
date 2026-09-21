package br.com.estudario.ui.screens.home

import java.time.LocalDate

/**
 * Modelos de apresentação da Home — nada de `PlannerTaskUi`, `StreakSummary` ou ViewModel aqui.
 * O [br.com.estudario.ui.screens.HomeScreen] traduz o estado real do app para estes tipos simples;
 * os composables desta pasta só recebem estado pronto e nunca tocam em domínio, Room ou regra de
 * negócio. É isso que torna as previews possíveis sem instanciar um `AppViewModel` — e o que
 * garante que a Home não vire um segundo lugar onde as regras do planejador são calculadas.
 */
data class StudyTaskUi(
    val id: String,
    val topicId: Long?,
    val subjectName: String,
    val topicName: String,
    val activityLabel: String,
    val durationLabel: String,
    val ctaLabel: String,
    val scheduledForToday: Boolean,
)

/** O concurso ativo — contexto do objetivo, nunca protagonista da tela. */
data class ActiveContestUi(
    val name: String,
    val objective: String?,
)

/** O painel "Agora" — a seção de maior hierarquia da tela. Um estado por vez, nunca combinados. */
sealed interface CurrentStudyUiState {
    /** Há uma missão para agora — em andamento ou ainda não começada. */
    data class Ready(val task: StudyTaskUi, val progressFraction: Float?) : CurrentStudyUiState
    /** Nenhum plano ativo: primeira coisa que a pessoa precisa resolver, não um aviso qualquer. */
    data object NoPlan : CurrentStudyUiState
    /** Plano ativo, mas nada agendado para hoje (dia de descanso ou intervalo entre fases). */
    data object NoTaskToday : CurrentStudyUiState
    /** Todas as missões de hoje concluídas. */
    data class DayComplete(val missionsToday: Int, val minutesToday: Int) : CurrentStudyUiState
    data object Loading : CurrentStudyUiState
}

/** Uma matéria dentro da barra de cobertura do edital. */
data class SubjectCoverageUi(
    val name: String,
    val studiedTopics: Int,
    val totalTopics: Int,
    val position: Int = 0,
) {
    val percent: Int get() = if (totalTopics == 0) 0 else studiedTopics * 100 / totalTopics
    val fraction: Float get() = if (totalTopics == 0) 0f else (studiedTopics.toFloat() / totalTopics).coerceIn(0f, 1f)
}

/**
 * A cobertura do edital inteiro. [masteryPercent] é nulo quando ainda não há base suficiente de
 * questões e revisões para falar em domínio — e aí a Home simplesmente não fala, em vez de mostrar
 * um número que não se sustenta.
 */
data class SyllabusCoverageUi(
    val percent: Int,
    val studiedTopics: Int,
    val totalTopics: Int,
    val subjects: List<SubjectCoverageUi>,
    val masteryPercent: Int?,
)

/** A leitura de ritmo já resolvida pelo domínio, pronta para virar texto. */
sealed interface PaceUi {
    data class Comfortable(val forecast: LocalDate, val daysBeforeExam: Long) : PaceUi
    data class Tight(val forecast: LocalDate, val daysBeforeExam: Long) : PaceUi
    data class Behind(val forecast: LocalDate, val daysAfterExam: Long) : PaceUi
    data class NoExamDate(val forecast: LocalDate) : PaceUi
    data object Complete : PaceUi
    data object Unknown : PaceUi
}

/**
 * Desempenho recente. Só existe quando há questões suficientes respondidas: [previousAccuracy] é
 * nulo enquanto não houver uma janela anterior para comparar, e aí nenhuma variação é exibida.
 */
data class PerformanceUi(
    val recentAccuracy: Int,
    val previousAccuracy: Int?,
    val answeredRecently: Int,
) {
    val delta: Int? get() = previousAccuracy?.let { recentAccuracy - it }
}

/** Constância e evolução dentro do Estudário — nível é progresso no app, não medida de capacidade. */
data class StandingUi(
    val streakDays: Int,
    val bestStreakDays: Int,
    val level: Int,
    val levelTitle: String,
    val totalXp: Int,
    val levelFraction: Float,
    val xpIntoLevel: Int = 0,
    val xpForNextLevel: Int = 100,
)

/** As próximas atividades do dia, em versão discreta — a agenda completa vive na aba Plano. */
data class NextUpUi(
    val items: List<StudyTaskUi>,
    val remainingToday: Int,
)
