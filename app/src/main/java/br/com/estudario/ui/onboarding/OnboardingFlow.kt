package br.com.estudario.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.theme.EstudarioSpacing

@Composable
fun OnboardingFlow(viewModel: AppViewModel, onDone: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(EstudarioSpacing.screenGutter),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Bem-vindo ao Estudário", style = MaterialTheme.typography.headlineMedium)
        Text("Organize seus editais e mantenha seu ritmo de estudo.", modifier = Modifier.padding(top = EstudarioSpacing.small))
        Button(onClick = onDone, modifier = Modifier.padding(top = EstudarioSpacing.large)) { Text("Começar") }
    }
}
