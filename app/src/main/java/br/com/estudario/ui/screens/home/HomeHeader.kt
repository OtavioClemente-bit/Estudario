package br.com.estudario.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.onboarding.EmptyNoExamArt
import br.com.estudario.ui.theme.EstudarioSpacing
import java.time.LocalTime

/**
 * O topo da Home: quem está estudando e para qual concurso.
 *
 * A saudação é uma linha de contexto, não um "Olá 👋" ocupando meia tela, a marca, a busca e o
 * perfil já moram na barra de identidade do app, então aqui sobra espaço para o que importa: o
 * concurso ativo, que é o que dá sentido a todo o resto da tela.
 */
@Composable
fun HomeHeader(
    firstName: String,
    contest: ActiveContestUi?,
    onOpenContest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            "${greeting()}, $firstName",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (contest != null) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onOpenContest),
            ) {
                Text(
                    contest.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                )
                if (!contest.objective.isNullOrBlank()) {
                    Text(
                        contest.objective,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

/**
 * Sem concurso cadastrado não existe painel para mostrar, e um dashboard zerado seria pior que
 * nada. Esta tela faz uma pergunta e oferece o primeiro passo.
 */
@Composable
fun HomeNoContestState(onAddContest: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Spacer(Modifier.height(EstudarioSpacing.medium))
        Column(Modifier.fillMaxWidth().clearAndSetSemantics { }) { EmptyNoExamArt() }
        Spacer(Modifier.height(EstudarioSpacing.section))
        Text(
            "Qual é o seu próximo concurso?",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(EstudarioSpacing.small))
        Text(
            "Adicionar seu edital é o primeiro passo para o Estudário montar sua jornada: as matérias viram tópicos, os tópicos viram plano, e o plano vira o que estudar hoje.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(EstudarioSpacing.large))
        Button(
            onClick = onAddContest,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text("Adicionar concurso", style = MaterialTheme.typography.labelLarge)
        }
    }
}

/**
 * Uma linha só, no fim de uma seção: leva para onde o assunto continua. Evita transformar a Home em
 * menu, quem quer o detalhe toca, quem não quer nem percebe.
 */
@Composable
fun HomeSectionLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(vertical = EstudarioSpacing.hairline),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
    }
}

private fun greeting(now: LocalTime = LocalTime.now()): String = when (now.hour) {
    in 0..4 -> "Boa madrugada"
    in 5..11 -> "Bom dia"
    in 12..17 -> "Boa tarde"
    else -> "Boa noite"
}
