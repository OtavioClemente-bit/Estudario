package br.com.estudario.ui.profile

import br.com.estudario.ui.theme.screenPadding
import androidx.compose.foundation.layout.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.outlined.Close
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
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
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.LinearProgressIndicator
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.Icon
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
    // tocar num emblema abre a medalha em destaque
    var aberto by remember { mutableStateOf<String?>(null) }

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
        aberto?.let { id -> current.badges.firstOrNull { it.badge.id == id }?.let { MedalDetailDialog(it, current.badges, onPick = { aberto = it }, onClose = { aberto = null }) } }
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
                items(badges.size) { index -> BadgeRow(badges[index]) { aberto = badges[index].badge.id } }
            }
        }
    }
}

@Composable
private fun BadgeRow(row: BadgeProgress, onOpen: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable(onClickLabel = "Ver a medalha", onClick = onOpen)) {
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

private fun tierLabel(tier: Int) = when (tier) { 1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda" }

/**
 * A medalha em destaque (a mesma do site): grande, balançando como moeda na mão, com o brilho passando;
 * arrastar gira, tocar dá um rodopio (de costas aparece o verso liso). Embaixo, o nível, o que pede, o
 * progresso e a escada da categoria (dá para tocar nos outros degraus).
 */
@Composable
private fun MedalDetailDialog(item: BadgeProgress, all: List<BadgeProgress>, onPick: (String) -> Unit, onClose: () -> Unit) {
    val b = item.badge
    val ladder = all.filter { it.badge.category == b.category }.sortedBy { it.badge.tier }
    val scope = rememberCoroutineScope()
    val spin = remember(b.id) { Animatable(0f) }
    val wave = rememberInfiniteTransition(label = "medalha")
    val sway by wave.animateFloat(-24f, 24f, infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "balanço")
    val shine by wave.animateFloat(-0.6f, 1.6f, infiniteRepeatable(tween(3400, easing = LinearEasing)), label = "brilho")
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(Modifier.fillMaxSize().clickable(remember { MutableInteractionSource() }, indication = null, onClick = onClose), contentAlignment = Alignment.Center) {
            Column(
                Modifier.padding(16.dp).widthIn(max = 440.dp).fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .clickable(remember { MutableInteractionSource() }, indication = null) { }
                    .verticalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(Modifier.fillMaxWidth()) { IconButton(onClick = onClose, Modifier.align(Alignment.TopEnd)) { Icon(Icons.Outlined.Close, "Fechar") } }
                // a medalha: balanço + giro do dedo
                val angle = (if (item.earned) sway else 0f) + spin.value
                val norm = ((angle % 360f) + 360f) % 360f
                val back = norm in 90f..270f
                Box(
                    Modifier.size(220.dp)
                        .pointerInput(b.id) {
                            detectHorizontalDragGestures(
                                onDragEnd = { scope.launch { spin.animateTo(kotlin.math.round(spin.value / 360f) * 360f, spring(0.55f, 120f)) } },
                            ) { _, dx -> scope.launch { spin.snapTo(spin.value + dx * 0.6f) } }
                        }
                        .clickable(remember { MutableInteractionSource() }, indication = null) {
                            scope.launch { spin.animateTo(spin.value + 360f, tween(900, easing = FastOutSlowInEasing)) }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (item.earned) MedalGlow(b.tier)
                    Box(Modifier.graphicsLayer { rotationY = angle; cameraDistance = 14f * density }, contentAlignment = Alignment.Center) {
                        MedalArt(if (back) b.copy(id = "verso") else b, item.earned, 190.dp)
                        if (item.earned && !back) MedalShine(shine, b.tier, Modifier.size(190.dp))
                    }
                }
                Text(
                    "${tierLabel(b.tier).uppercase()} · ${b.category.label.uppercase()}",
                    Modifier.clip(RoundedCornerShape(50)).background(tierColor(b.tier).copy(alpha = 0.16f)).padding(horizontal = 12.dp, vertical = 5.dp),
                    color = tierColor(b.tier), fontWeight = FontWeight.Black, fontSize = 11.sp, letterSpacing = 1.5.sp,
                )
                Text(b.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                Text(b.requirement, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                if (item.earned) Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Outlined.CheckCircle, null, Modifier.size(18.dp), tint = Color(0xFF1FA65C))
                    Text("Conquistado", fontWeight = FontWeight.Bold, color = Color(0xFF1FA65C))
                } else Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    LinearProgressIndicator({ item.percent }, Modifier.fillMaxWidth().clip(RoundedCornerShape(50)))
                    Text(item.hint ?: "${item.current} de ${item.target} ${b.unit} · faltam ${item.remaining}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (ladder.size > 1) {
                    Spacer(Modifier.height(4.dp))
                    Text("A ESCADA DE ${b.category.label.uppercase()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
                    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ladder.forEach { step ->
                            val on = step.badge.id == b.id
                            Column(
                                Modifier.clip(RoundedCornerShape(14.dp))
                                    .then(if (on) Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)) else Modifier)
                                    .clickable { onPick(step.badge.id) }.padding(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                MedalArt(step.badge, step.earned, 48.dp)
                                Text("${step.badge.target} ${step.badge.unit}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (step.earned || on) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

/** A luz atrás da medalha conquistada: brilho na cor do nível e raios girando devagar. */
@Composable
private fun MedalGlow(tier: Int) {
    val c = tierColor(tier)
    val turn by rememberInfiniteTransition(label = "raios").animateFloat(0f, 360f, infiniteRepeatable(tween(40_000, easing = LinearEasing)), label = "giro")
    Canvas(Modifier.fillMaxSize()) {
        val ctr = center; val r = size.minDimension / 2
        drawCircle(Brush.radialGradient(listOf(c.copy(alpha = 0.42f), c.copy(alpha = 0f)), ctr, r), r, ctr)
        rotate(turn, ctr) {
            for (i in 0 until 16) {
                val a = Math.toRadians(i * 22.5).toFloat(); val w = 0.07f
                val p = Path().apply {
                    moveTo(ctr.x, ctr.y)
                    lineTo(ctr.x + kotlin.math.cos(a - w) * r, ctr.y + kotlin.math.sin(a - w) * r)
                    lineTo(ctr.x + kotlin.math.cos(a + w) * r, ctr.y + kotlin.math.sin(a + w) * r); close()
                }
                drawPath(p, Brush.radialGradient(listOf(c.copy(alpha = 0.32f), c.copy(alpha = 0f)), ctr, r))
            }
        }
    }
}

/** O brilho passando na diagonal, só dentro da moeda. */
@Composable
private fun MedalShine(u: Float, tier: Int, modifier: Modifier) {
    Canvas(modifier) {
        val r = size.minDimension / 2f * when { tier >= 5 -> 0.5f; tier >= 3 -> 0.6f; else -> 0.76f }
        clipPath(Path().apply { addOval(Rect(center, r)) }) {
            val x = center.x - r * 1.6f + u * r * 3.2f
            drawRect(Brush.linearGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.55f), Color.Transparent), start = Offset(x - r * 0.35f, center.y - r), end = Offset(x + r * 0.35f, center.y + r)))
        }
    }
}
