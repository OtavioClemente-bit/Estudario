package br.com.estudario.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.theme.EstudarioTheme
import java.time.LocalDate

/**
 * Previews da Home sem ViewModel — dados falsos batendo nos mesmos modelos de apresentação que
 * [br.com.estudario.ui.screens.HomeScreen] monta a partir do estado real. Cobrem os estados que
 * mais mudam a tela: rotina normal, dia concluído, sem plano, sem concurso, plano atrasado, edital
 * quase fechado, Dark Mode e fonte ampliada.
 */
@Composable
private fun HomePreviewScaffold(
    current: CurrentStudyUiState,
    coverage: SyllabusCoverageUi,
    pace: PaceUi,
    performance: PerformanceUi?,
    standing: StandingUi,
    nextUp: NextUpUi = NextUpUi(emptyList(), 0),
    contest: ActiveContestUi? = ActiveContestUi("TRT 3ª Região", "Analista Judiciário — Tecnologia da Informação"),
) {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = EstudarioSpacing.small, bottom = EstudarioSpacing.expansive),
            ) {
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        HomeHeader(firstName = "Otávio", contest = contest, onOpenContest = {})
                    }
                }
                item { Spacer(Modifier.height(EstudarioSpacing.comfortable)) }
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        JourneySnapshot(coverage = coverage, standing = standing)
                    }
                }
                item { Spacer(Modifier.height(EstudarioSpacing.medium)) }
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        CurrentStudySection(state = current, onPrimaryAction = {}, practiceAvailable = true)
                    }
                }
                if (nextUp.items.isNotEmpty()) {
                    item { Spacer(Modifier.height(EstudarioSpacing.medium)) }
                    item {
                        Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                            NextUpStrip(nextUp, onOpenPlan = {})
                        }
                    }
                }
                item { Spacer(Modifier.height(EstudarioSpacing.section)) }
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        SyllabusCoverage(coverage = coverage, onOpenSyllabus = {})
                    }
                }
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        HomeDivider(Modifier.padding(vertical = EstudarioSpacing.large))
                    }
                }
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        PaceForecast(pace = pace, onOpenPlan = {})
                    }
                }
                item { Spacer(Modifier.height(EstudarioSpacing.large)) }
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        PerformanceAndStanding(performance, standing, onOpenStatistics = {}, onOpenProfile = {})
                    }
                }
                item { Spacer(Modifier.height(EstudarioSpacing.large)) }
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        LevelRow(standing = standing, onOpenProfile = {})
                    }
                }
                item { Spacer(Modifier.height(EstudarioSpacing.large)) }
                item {
                    Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                        HomeAttention(
                            pendingReviews = 3,
                            pendingErrors = 2,
                            weakTopicName = "Controle de constitucionalidade",
                            weakTopicMastery = 38,
                            onOpenReviews = {},
                            onOpenErrors = {},
                            onOpenWeakTopic = {},
                        )
                    }
                }
            }
        }
    }
}

private fun fakeTask(subject: String, topic: String, activity: String, minutes: Int, cta: String) = StudyTaskUi(
    id = "$subject-$topic",
    topicId = 1L,
    subjectName = subject,
    topicName = topic,
    activityLabel = activity,
    durationLabel = "$minutes min",
    ctaLabel = cta,
    scheduledForToday = true,
)

private val fakeCoverage = SyllabusCoverageUi(
    percent = 68,
    studiedTopics = 142,
    totalTopics = 208,
    subjects = listOf(
        SubjectCoverageUi("Direito Constitucional", 46, 62),
        SubjectCoverageUi("LÍNGUA PORTUGUESA (NÍVEL MÉDIO E SUPERIOR)", 28, 44),
        SubjectCoverageUi("Redes de Computadores", 22, 38),
        SubjectCoverageUi("ANALISTA JUDICIÁRIO – ÁREA DE APOIO ESPECIALIZADO – TECNOLOGIA DA INFORMAÇÃO", 30, 34),
        SubjectCoverageUi("Direito Administrativo", 16, 30),
    ),
    masteryPercent = 54,
)

private val fakeStanding = StandingUi(
    streakDays = 12,
    bestStreakDays = 21,
    level = 14,
    levelTitle = "Disciplinado",
    totalXp = 1840,
    levelFraction = 0.62f,
    xpIntoLevel = 112,
    xpForNextLevel = 200,
)

private val fakePerformance = PerformanceUi(recentAccuracy = 78, previousAccuracy = 72, answeredRecently = 50)

private val fakeNextUp = NextUpUi(
    items = listOf(
        fakeTask("Redes de Computadores", "Protocolos de roteamento", "Questões", 30, "Começar"),
        fakeTask("Português", "Concordância verbal", "Revisão", 20, "Começar"),
    ),
    remainingToday = 3,
)

@Preview(name = "Home — rotina normal", showBackground = true, heightDp = 1180)
@Composable
private fun HomeNormalPreview() {
    HomePreviewScaffold(
        current = CurrentStudyUiState.Ready(
            fakeTask("Banco de Dados", "Normalização — 3ª Forma Normal", "Teoria", 25, "Continuar estudo"),
            progressFraction = 0.4f,
        ),
        coverage = fakeCoverage,
        pace = PaceUi.Comfortable(LocalDate.now().plusMonths(2), 24),
        performance = fakePerformance,
        standing = fakeStanding,
        nextUp = fakeNextUp,
    )
}

@Preview(name = "Home — dia concluído", showBackground = true, heightDp = 1180)
@Composable
private fun HomeDayCompletePreview() {
    HomePreviewScaffold(
        current = CurrentStudyUiState.DayComplete(missionsToday = 4, minutesToday = 145),
        coverage = fakeCoverage,
        pace = PaceUi.Comfortable(LocalDate.now().plusMonths(2), 31),
        performance = fakePerformance,
        standing = fakeStanding,
    )
}

@Preview(name = "Home — plano atrasado", showBackground = true, heightDp = 1180)
@Composable
private fun HomeBehindPreview() {
    HomePreviewScaffold(
        current = CurrentStudyUiState.Ready(
            fakeTask("Segurança da Informação", "Criptografia assimétrica", "Questões", 40, "Começar estudo"),
            progressFraction = null,
        ),
        coverage = fakeCoverage.copy(percent = 41, studiedTopics = 86),
        pace = PaceUi.Behind(LocalDate.now().plusMonths(5), 37),
        performance = fakePerformance.copy(recentAccuracy = 61, previousAccuracy = 69),
        standing = fakeStanding.copy(streakDays = 2, bestStreakDays = 21),
        nextUp = fakeNextUp,
    )
}

@Preview(name = "Home — sem plano", showBackground = true, heightDp = 1180)
@Composable
private fun HomeNoPlanPreview() {
    HomePreviewScaffold(
        current = CurrentStudyUiState.NoPlan,
        coverage = fakeCoverage.copy(percent = 12, studiedTopics = 25, masteryPercent = null),
        pace = PaceUi.Unknown,
        performance = null,
        standing = fakeStanding.copy(streakDays = 0, bestStreakDays = 0, level = 2, levelTitle = "Iniciante", totalXp = 120, levelFraction = 0.2f, xpIntoLevel = 20, xpForNextLevel = 100),
        contest = ActiveContestUi("TRT 3ª Região", null),
    )
}

@Preview(name = "Home — edital quase fechado", showBackground = true, heightDp = 1180)
@Composable
private fun HomeAlmostDonePreview() {
    HomePreviewScaffold(
        current = CurrentStudyUiState.NoTaskToday,
        coverage = fakeCoverage.copy(percent = 96, studiedTopics = 200, masteryPercent = 81),
        pace = PaceUi.Complete,
        performance = fakePerformance.copy(recentAccuracy = 84, previousAccuracy = 80),
        standing = fakeStanding.copy(streakDays = 47, bestStreakDays = 47, level = 22, levelTitle = "Estrategista"),
    )
}

@Preview(name = "Home — Dark Mode", showBackground = true, heightDp = 1180, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeDarkPreview() {
    HomePreviewScaffold(
        current = CurrentStudyUiState.Ready(
            fakeTask("Banco de Dados", "Normalização — 3ª Forma Normal", "Teoria", 25, "Continuar estudo"),
            progressFraction = 0.4f,
        ),
        coverage = fakeCoverage,
        pace = PaceUi.Tight(LocalDate.now().plusMonths(3), 6),
        performance = fakePerformance,
        standing = fakeStanding,
        nextUp = fakeNextUp,
    )
}

@Preview(name = "Home — fonte ampliada", showBackground = true, heightDp = 1400, fontScale = 1.6f)
@Composable
private fun HomeLargeFontPreview() {
    HomePreviewScaffold(
        current = CurrentStudyUiState.Ready(
            fakeTask("Banco de Dados", "Normalização — 3ª Forma Normal", "Teoria", 25, "Continuar estudo"),
            progressFraction = 0.4f,
        ),
        coverage = fakeCoverage,
        pace = PaceUi.NoExamDate(LocalDate.now().plusMonths(4)),
        performance = fakePerformance,
        standing = fakeStanding,
    )
}

@Preview(name = "Home — sem concurso", showBackground = true, heightDp = 700)
@Composable
private fun HomeNoContestPreview() {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                HomeNoContestState(onAddContest = {})
            }
        }
    }
}

@Preview(name = "Home — sem concurso (escuro)", showBackground = true, heightDp = 700, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeNoContestDarkPreview() {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(Modifier.padding(horizontal = EstudarioSpacing.screenGutter)) {
                HomeNoContestState(onAddContest = {})
            }
        }
    }
}
