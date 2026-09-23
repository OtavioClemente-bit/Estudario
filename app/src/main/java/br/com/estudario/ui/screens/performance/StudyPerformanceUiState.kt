package br.com.estudario.ui.screens.performance

import br.com.estudario.domain.performance.StudyPerformancePeriod
import br.com.estudario.domain.performance.StudyPerformanceResult

data class StudyPerformanceUiState(
    val selectedPeriod: StudyPerformancePeriod,
    val result: StudyPerformanceResult,
)
