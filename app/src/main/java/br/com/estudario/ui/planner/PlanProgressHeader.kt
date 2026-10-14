package br.com.estudario.ui.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.ui.theme.EstudarioShapes
import java.time.LocalDate
import java.time.temporal.ChronoUnit

// O cabeçalho do plano mora em PlanHeroCard.kt (PlanProgressHeader).

/** "amanhã", "segunda, 5/10": como uma pessoa falaria a data. */
internal fun dayLabelPtBr(date: java.time.LocalDate, today: java.time.LocalDate): String {
    if (date == today.plusDays(1)) return "amanhã"
    val dia = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale("pt", "BR")).substringBefore("-")
    return "$dia, ${date.dayOfMonth}/${date.monthValue}"
}
