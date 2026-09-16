package br.com.meuconcurso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.ui.AppViewModel
import br.com.meuconcurso.ui.components.EmptyState
import br.com.meuconcurso.domain.ReviewPolicy
import br.com.meuconcurso.domain.ComputedReviewStatus
import java.text.DateFormat
import java.util.Date

@Composable
fun ReviewsScreen(viewModel: AppViewModel, onBack: () -> Unit, onStartReview: (Long) -> Unit, onOpenTopic: (Long) -> Unit) {
    val reviews by viewModel.reviews.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val pending = reviews.filter { it.completedAt == null && it.ignoredAt == null }.sortedWith(compareBy({ ReviewPolicy.status(it).ordinal }, { it.dueAt }))
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Column { Text("Revisões", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("Agenda D+1, D+7 e D+30") }
            }
        }
        if (pending.isEmpty()) item { EmptyState("Revisões em dia", "Marque um tópico como estudado para criar a agenda automaticamente.") }
        items(pending, key = { it.id }) { review ->
            val topic = topics.firstOrNull { it.id == review.topicId }
            val status = ReviewPolicy.status(review)
            ElevatedCard(onClick = { onStartReview(review.id) }) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(topic?.title ?: "Tópico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("${when (status) { ComputedReviewStatus.ATRASADA -> "Atrasada"; ComputedReviewStatus.DISPONIVEL -> "Disponível"; else -> "Agendada" }} • ${DateFormat.getDateInstance().format(Date(review.dueAt))}")
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        FilledTonalButton(onClick = { onStartReview(review.id) }) { Text("Revisar") }
                        TextButton(onClick = { onOpenTopic(review.topicId) }) { Text("Ver tópico") }
                    }
                }
            }
        }
    }
}
