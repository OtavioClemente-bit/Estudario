package br.com.estudario.ui.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.EstudarioApplication

@Composable
fun AiAccessPanel(showTitle: Boolean = true) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val accessViewModel: AiAccessViewModel = viewModel(factory = AiAccessViewModel.Factory(app.aiAccessRepository))
    val state by accessViewModel.state.collectAsState()
    LaunchedEffect(accessViewModel) { accessViewModel.refresh() }
    AiAccessSummary(state, showTitle)
}

@Composable
fun AiAccessSummary(state: AiAccessUiState, showTitle: Boolean = true) {
    // Sem conta (ou sem internet) as três linhas diriam a mesma coisa: uma frase basta.
    val unavailable = state.items.isNotEmpty() && state.items.values.all { !it.canUse && it.quotaCopy.isEmpty() }
    Column(Modifier.fillMaxWidth().padding(vertical = if (showTitle) 12.dp else 0.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (showTitle) Text("IA do Estudário · Beta", style = MaterialTheme.typography.titleMedium)
        if (unavailable) {
            Text("Entre na sua conta e confira a internet para usar a IA do Estudário.", style = MaterialTheme.typography.bodySmall)
            return@Column
        }
        AiFeature.entries.forEach { feature ->
            val display = state.items[feature] ?: return@forEach
            val label = when (feature) {
                AiFeature.SYLLABUS_GENERATION -> "Edital"
                AiFeature.PLAN_GENERATION -> "Plano"
                AiFeature.CONTENT_GENERATION -> "Conteúdo"
            }
            Text("$label: ${display.availabilityCopy}")
            if (display.quotaCopy.isNotEmpty()) Text(display.quotaCopy, style = MaterialTheme.typography.bodySmall)
        }
        if (state.localFallbackAvailable) Text("Importar .estudo ou montar manualmente", style = MaterialTheme.typography.bodySmall)
    }
}
