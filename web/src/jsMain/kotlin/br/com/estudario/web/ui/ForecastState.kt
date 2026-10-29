package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.ForecastLoad
import br.com.estudario.web.data.ForecastService
import br.com.estudario.web.data.LibraryGaps
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.StudyPlan

@Composable
internal fun rememberForecast(data: Snapshot, plan: StudyPlan?): ForecastLoad {
    val day = Queries.todayDate()
    val zone = Queries.zone.id
    val free = LibraryGaps.free()
    val excluded = LibraryGaps.excluded(data)
    var state by remember(data, plan, day, zone, free, excluded) {
        mutableStateOf(if (plan == null) ForecastLoad(loading = false) else ForecastService.cached(data, plan) ?: ForecastLoad())
    }
    LaunchedEffect(data, plan, day, zone, free, excluded) {
        if (plan != null) state = ForecastService.load(data, plan)
    }
    return state
}
