package br.com.estudario.ui.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Timer
import br.com.estudario.ui.brand.Card
import androidx.compose.material3.CardDefaults
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.FocusSessionEntity
import br.com.estudario.data.local.FocusSessionOrigin
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.theme.screenPadding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private enum class FocusPeriod(val label: String, val days: Long?) { WEEK("7 dias", 7), MONTH("30 dias", 30), ALL("Tudo", null) }

/**
 * Histórico do Modo foco: quanto e o que foi estudado. Filtra por período, matéria e origem, mostra
 * o tempo por matéria e cada sessão com o tópico ou a tarefa que estava sendo estudada.
 */
@Composable
fun FocusHistoryScreen(viewModel: AppViewModel) {
    val history by viewModel.focusSessions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val activeSession by viewModel.focusSession.collectAsState()
    val namesById = remember(subjects) { subjects.associate { it.id to it.name } }
    val topicTitleById = remember(topics) { topics.associate { it.id to it.title } }
    var period by rememberSaveable { mutableStateOf(FocusPeriod.WEEK) }
    var subjectFilter by rememberSaveable { mutableStateOf<Long?>(null) }
    var originFilter by rememberSaveable { mutableStateOf<FocusSessionOrigin?>(null) }

    val zone = ZoneId.systemDefault()
    val filtered = remember(history, period, subjectFilter, originFilter) {
        val since = period.days?.let { LocalDate.now().minusDays(it - 1).atStartOfDay(zone).toInstant().toEpochMilli() } ?: Long.MIN_VALUE
        history.filter { session ->
            session.completedAt >= since &&
                (subjectFilter == null || subjectFilter in session.subjectIds()) &&
                (originFilter == null || session.origin == originFilter)
        }
    }
    val totalMinutes = filtered.sumOf { it.durationSeconds.coerceAtLeast(0L) } / 60L
    val activeDays = filtered.map { Instant.ofEpochMilli(it.completedAt).atZone(zone).toLocalDate() }.distinct().size
    val average = if (filtered.isEmpty()) 0L else totalMinutes / filtered.size
    val perSubject = remember(filtered, namesById) {
        filtered.flatMap { session ->
            val ids = session.subjectIds()
            // Sessão com várias matérias divide o tempo entre elas; sem matéria, vai para "Livre".
            if (ids.isEmpty()) listOf("Sessão livre" to session.durationSeconds)
            else ids.map { (namesById[it] ?: "Matéria removida") to session.durationSeconds / ids.size }
        }.groupBy({ it.first }, { it.second }).mapValues { it.value.sum() / 60L }.entries.sortedByDescending { it.value }
    }
    val subjectsInHistory = remember(history, namesById) { history.flatMap { it.subjectIds() }.distinct().mapNotNull { id -> namesById[id]?.let { id to it } } }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = screenPadding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("Seu histórico de foco", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                Text("Quanto você estudou com o cronômetro ligado e o que estava estudando em cada sessão.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (activeSession.active) item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Sessão em andamento", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(activeSession.title.ifBlank { "Sessão de foco" }, style = MaterialTheme.typography.bodyMedium)
                        Text("Ela entra no histórico quando for encerrada.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FocusPeriod.entries.forEach { value -> FilterChip(period == value, { period = value }, label = { Text(value.label) }) }
                    Spacer(Modifier.width(8.dp))
                    listOf(null to "Todas as origens", FocusSessionOrigin.PLANO to "Plano", FocusSessionOrigin.MATERIA to "Matéria", FocusSessionOrigin.LIVRE to "Livre").forEach { (origin, label) ->
                        FilterChip(originFilter == origin, { originFilter = origin }, label = { Text(label) })
                    }
                }
                if (subjectsInHistory.isNotEmpty()) Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(subjectFilter == null, { subjectFilter = null }, label = { Text("Todas as matérias") })
                    subjectsInHistory.forEach { (id, name) ->
                        FilterChip(subjectFilter == id, { subjectFilter = if (subjectFilter == id) null else id }, label = { Text(name, maxLines = 1, overflow = TextOverflow.Ellipsis) })
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Stat("Tempo de foco", formatLongMinutes(totalMinutes))
                    Stat("Sessões", filtered.size.toString())
                    Stat("Média", formatLongMinutes(average))
                    Stat("Dias", activeDays.toString())
                }
            }
        }
        if (perSubject.isNotEmpty()) item {
            Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surfaceContainerLow, shape = MaterialTheme.shapes.large) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Tempo por matéria", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    val max = perSubject.maxOf { it.value }.coerceAtLeast(1L)
                    perSubject.take(8).forEach { (name, minutes) ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row {
                                Text(name, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(formatLongMinutes(minutes), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                            }
                            Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                                Box(Modifier.fillMaxWidth(minutes.toFloat() / max).fillMaxHeight().clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.primary))
                            }
                        }
                    }
                }
            }
        }
        if (filtered.isEmpty()) {
            item {
                Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surfaceContainerLow, shape = MaterialTheme.shapes.large) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(if (history.isEmpty()) "Ainda sem sessões encerradas" else "Nada neste filtro", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            if (history.isEmpty()) "Ligue o Modo foco antes de estudar e encerre a sessão ao terminar. A duração e o que você estudou aparecem aqui."
                            else "Mude o período, a matéria ou a origem para ver outras sessões.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        } else {
            item { Text("Sessões", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(filtered, key = { it.id }) { session ->
                Column {
                    FocusHistoryRow(session, namesById)
                    // O que estava sendo estudado: o tópico aberto quando o foco começou.
                    session.topicId?.let(topicTitleById::get)?.let { topicTitle ->
                        Text("Tópico: $topicTitle", Modifier.padding(start = 50.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

private fun FocusSessionEntity.subjectIds(): List<Long> = subjectIdsText.split(',').mapNotNull { it.trim().toLongOrNull() }

@Composable
private fun Stat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}
