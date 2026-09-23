package br.com.estudario.ui.profile

import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.BadgeCategory
import br.com.estudario.domain.BadgeProgress
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.LoadingScreen

internal fun categoryIcon(category: BadgeCategory): ImageVector = when (category) {
    BadgeCategory.PLANO -> Icons.Outlined.CalendarMonth
    BadgeCategory.SEQUENCIA -> Icons.Outlined.LocalFireDepartment
    BadgeCategory.METAS -> Icons.Outlined.CheckCircle
    BadgeCategory.QUESTOES -> Icons.Outlined.Quiz
    BadgeCategory.PRECISAO -> Icons.Outlined.GpsFixed
    BadgeCategory.TOPICOS -> Icons.AutoMirrored.Outlined.MenuBook
    BadgeCategory.EDITAL -> Icons.Outlined.Checklist
    BadgeCategory.REVISOES -> Icons.Outlined.EventRepeat
    BadgeCategory.SIMULADOS -> Icons.Outlined.Timer
    BadgeCategory.DISCURSIVAS -> Icons.Outlined.EditNote
    BadgeCategory.MARATONA -> Icons.Outlined.DirectionsRun
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgesScreen(viewModel: AppViewModel, onBack: () -> Unit, showInternalTopBar: Boolean = true) {
    val progress by viewModel.progress.collectAsState()
    var categoria by remember { mutableStateOf<BadgeCategory?>(null) }

    Scaffold(
        topBar = {
            if (showInternalTopBar) TopAppBar(
                title = { Text("Emblemas") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Voltar") } },
            )
        },
    ) { padding ->
        val current = progress
        if (current == null) {
            LoadingScreen("Conferindo suas conquistas", modifier = Modifier.padding(padding))
            return@Scaffold
        }
        val conquistados = current.badges.count { it.earned }
        val visiveis = current.badges.filter { categoria == null || it.badge.category == categoria }
        val porCategoria = visiveis.groupBy { it.badge.category }

        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = screenPadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("$conquistados de ${current.badges.size} emblemas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        LinearProgressIndicator(
                            { conquistados.toFloat() / current.badges.size.coerceAtLeast(1) },
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(50)),
                        )
                        val proximo = current.nextBadges.firstOrNull()
                        Text(
                            if (proximo == null) "Você conquistou tudo. Sério."
                            else "Mais perto: ${proximo.badge.name}, faltam ${proximo.remaining} ${proximo.badge.unit}.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            item {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    FilterChip(categoria == null, { categoria = null }, { Text("Todos") })
                    BadgeCategory.entries.forEach { item ->
                        FilterChip(categoria == item, { categoria = item }, { Text(item.label) })
                    }
                }
            }
            porCategoria.forEach { (category, badges) ->
                item {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(categoryIcon(category), null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(category.label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("${badges.count { it.earned }}/${badges.size}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                items(badges.size) { index -> BadgeRow(badges[index]) }
            }
        }
    }
}

@Composable
private fun BadgeRow(row: BadgeProgress) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            BadgeMedal(row.badge, row.earned)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        row.badge.name,
                        fontWeight = FontWeight.SemiBold,
                        color = if (row.earned) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (row.earned) Icon(Icons.Outlined.CheckCircle, "Conquistado", Modifier.size(15.dp), tint = tierColor(row.badge.tier))
                }
                Text(row.badge.requirement, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (!row.earned) {
                    LinearProgressIndicator({ row.percent }, Modifier.fillMaxWidth().clip(RoundedCornerShape(50)))
                    Text(
                        row.hint ?: "${row.current} de ${row.target} ${row.badge.unit} • faltam ${row.remaining}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
