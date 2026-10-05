package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.BadgeCatalog
import br.com.estudario.ui.profile.BadgeArt
import br.com.estudario.ui.theme.EstudarioTheme

/** Vitrine de debug: a tela de geração como aparece ao gerar material ou ler um edital. */
@Composable
fun LoaderShowcase() = EstudarioTheme {
    br.com.estudario.ui.brand.Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { Box(Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
        EstudarioProcessView(
            title = "Gerando seu material",
            eyebrow = "Crase",
            stages = listOf("Lendo o tópico", "Montando a teoria", "Escrevendo as questões", "Revisando"),
            stageMillis = 3_000L,
            footer = { br.com.estudario.ui.prompt.ContentCommitmentsCard() },
        )
    } }
}

/** Vitrine de debug: todas as medalhas, conquistadas, para conferir o desenho. */
@Composable
fun BadgeShowcase() = EstudarioTheme {
    LazyVerticalGrid(
        GridCells.Fixed(4),
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(BadgeCatalog.all) { badge ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BadgeArt(badge, earned = true, size = 76.dp)
                Text(badge.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center, modifier = Modifier.width(88.dp))
            }
        }
    }
}

/** Vitrine de debug: a comemoração de subida de nível. */
@Composable
fun LevelUpShowcase() = EstudarioTheme {
    br.com.estudario.ui.profile.LevelUpScreen(br.com.estudario.ui.AppViewModel.LevelUp(10, "Concurseiro", newTitle = true, totalXp = 4_620), onClose = {})
}

/** Vitrine de debug: o aviso de tempo planejado do modo foco. */
@Composable
fun FocusTimeUpShowcase() = EstudarioTheme {
    br.com.estudario.ui.brand.Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        br.com.estudario.ui.focus.FocusTimeUpDialog(45, "Crase", onContinue = {}, onFinish = {})
    }
}
