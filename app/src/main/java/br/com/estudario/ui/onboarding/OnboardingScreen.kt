package br.com.estudario.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.ui.components.EstudarioWordmark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A primeira impressão do Estudário: três páginas, cada uma com uma cena animada feita das peças
 * reais do app (o edital virando matérias, o material da IA, a evolução até a prova). Testando com
 * gente de verdade, ninguém lia páginas longas; aqui o desenho conta a história e o texto é uma
 * frase. Aparece uma única vez, na primeira instalação. Quem conhece o app pula em um toque.
 */
private data class OnboardingPage(val eyebrow: String, val title: String, val body: String)

private val pages = listOf(
    OnboardingPage("Seu edital", "Seu edital vira\num plano de estudo", "Envie o PDF e o Estudário organiza as matérias e monta o caminho até a prova."),
    OnboardingPage("Estudário IA", "Teoria, flashcards\ne questões para você", "Cada tópico vira material completo, com questões no estilo da banca e fonte conferida."),
    OnboardingPage("Sua evolução", "Estude todo dia e\nveja a aprovação chegar", "Revisões no momento certo, sequência de dias e o seu domínio crescendo a cada sessão."),
)

private val Indigo = Color(0xFF4F46E5)
private val Violet = Color(0xFF9F5BF5)
private val Mint = Color(0xFF34D399)
private val Amber = Color(0xFFFBBF24)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == pages.lastIndex

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val stageHeight = maxHeight * 0.56f
            // Fundo da cena: o gradiente da marca, com bolhas de luz que flutuam devagar.
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(stageHeight)
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(Brush.linearGradient(listOf(Indigo, Violet))),
            ) { FloatingGlow() }

            Column(Modifier.fillMaxSize()) {
                HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { index ->
                    val active = pagerState.currentPage == index
                    Column(Modifier.fillMaxSize()) {
                        Box(
                            Modifier.fillMaxWidth().height(stageHeight).statusBarsPadding().clearAndSetSemantics { },
                            contentAlignment = Alignment.Center,
                        ) {
                            when (index) {
                                0 -> EditalScene(active)
                                1 -> MaterialScene(active)
                                else -> ProgressScene(active)
                            }
                        }
                        Column(Modifier.fillMaxWidth().padding(horizontal = 28.dp).padding(top = 28.dp)) {
                            if (index == 0) {
                                EstudarioWordmark()
                                Spacer(Modifier.height(14.dp))
                            } else {
                                Text(
                                    pages[index].eyebrow.uppercase(),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp,
                                )
                                Spacer(Modifier.height(10.dp))
                            }
                            Text(pages[index].title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(Modifier.height(12.dp))
                            Text(pages[index].body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Row(
                    Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PageIndicator(count = pages.size, current = pagerState.currentPage)
                    Spacer(Modifier.weight(1f))
                    if (!isLast) {
                        TextButton(onClick = onFinish) { Text("Pular", style = MaterialTheme.typography.labelLarge) }
                        Spacer(Modifier.width(4.dp))
                    }
                    Button(
                        onClick = { if (isLast) onFinish() else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.height(54.dp),
                    ) {
                        Text(if (isLast) "Começar a estudar" else "Próximo", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingGlow() {
    val t = rememberInfiniteTransition(label = "glow")
    val drift by t.animateFloat(0f, 1f, infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Reverse), label = "drift")
    Canvas(Modifier.fillMaxSize()) {
        drawCircle(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.22f), Color.Transparent), center = center.copy(x = size.width * (0.85f - 0.1f * drift), y = size.height * 0.18f), radius = size.width * 0.45f), radius = size.width * 0.45f, center = center.copy(x = size.width * (0.85f - 0.1f * drift), y = size.height * 0.18f))
        drawCircle(Brush.radialGradient(listOf(Mint.copy(alpha = 0.25f), Color.Transparent), center = center.copy(x = size.width * (0.1f + 0.12f * drift), y = size.height * 0.85f), radius = size.width * 0.4f), radius = size.width * 0.4f, center = center.copy(x = size.width * (0.1f + 0.12f * drift), y = size.height * 0.85f))
        // Pontinhos de luz, como poeira de estrela.
        listOf(0.12f to 0.3f, 0.3f to 0.12f, 0.72f to 0.62f, 0.9f to 0.42f, 0.55f to 0.08f, 0.2f to 0.68f).forEachIndexed { i, (x, y) ->
            val pulse = 0.4f + 0.6f * ((drift + i * 0.17f) % 1f)
            drawCircle(Color.White.copy(alpha = 0.55f * pulse), radius = (2 + i % 3).dp.toPx(), center = center.copy(x = size.width * x, y = size.height * y))
        }
    }
}

/** Sobe e desce devagar, como um objeto flutuando. [phase] desencontra os vizinhos. */
@Composable
private fun Modifier.floating(phase: Int, amplitude: Dp = 6.dp): Modifier {
    val t = rememberInfiniteTransition(label = "float$phase")
    val y by t.animateFloat(-1f, 1f, infiniteRepeatable(tween(2400 + phase * 300, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "y")
    return this.offset(y = amplitude * y)
}

/** Entra na cena (escala e opacidade) quando a página fica ativa, um elemento depois do outro. */
@Composable
private fun Entrance(active: Boolean, order: Int, content: @Composable () -> Unit) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(active) {
        if (active) { delay(120L + order * 140L); shown = true } else shown = false
    }
    AnimatedVisibility(shown, enter = fadeIn(tween(380)) + scaleIn(tween(420, easing = FastOutSlowInEasing), initialScale = 0.82f)) { content() }
}

@Composable
private fun SceneCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier.shadow(18.dp, RoundedCornerShape(22.dp), ambientColor = Color(0x55312E81), spotColor = Color(0x55312E81)),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
    ) { content() }
}

@Composable
private fun TextLines(count: Int, color: Color = Color(0xFFE5E7EB), widths: List<Float> = listOf(1f, 0.85f, 0.7f, 0.9f)) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(count) { i -> Box(Modifier.fillMaxWidth(widths[i % widths.size]).height(7.dp).clip(RoundedCornerShape(50)).background(color)) }
    }
}

// ---------------------------------------------------------------- cena 1: o edital vira matérias

@Composable
private fun EditalScene(active: Boolean) {
    Box(Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 20.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.align(Alignment.CenterStart).offset(x = 6.dp, y = (-18).dp)) {
            Entrance(active, 0) {
                SceneCard(Modifier.width(132.dp).rotate(-6f).floating(0)) {
                    Column(Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(26.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFFEE2E2)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.Description, null, Modifier.size(16.dp), tint = Color(0xFFDC2626))
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("edital.pdf", style = MaterialTheme.typography.labelMedium, color = Color(0xFF374151), fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(12.dp))
                        TextLines(6)
                    }
                }
            }
        }
        Box(Modifier.align(Alignment.CenterEnd).offset(y = 14.dp)) {
            Entrance(active, 2) {
                SceneCard(Modifier.width(184.dp).floating(1)) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Suas matérias", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Color(0xFF111827))
                        SubjectBar("Português", 0.72f, Indigo, active, 0)
                        SubjectBar("Direito Const.", 0.45f, Color(0xFF0EA5E9), active, 1)
                        SubjectBar("Raciocínio", 0.6f, Color(0xFFF59E0B), active, 2)
                        SubjectBar("Informática", 0.3f, Color(0xFF10B981), active, 3)
                    }
                }
            }
        }
        // Brilho entre os dois cartões: o momento em que o Estudário organiza.
        Box(Modifier.align(Alignment.Center).offset(x = (-6).dp, y = (-104).dp)) {
            Entrance(active, 1) { MagicBadge() }
        }
    }
}

@Composable
private fun SubjectBar(name: String, target: Float, color: Color, active: Boolean, order: Int) {
    var go by remember { mutableStateOf(false) }
    LaunchedEffect(active) { if (active) { delay(600L + order * 160L); go = true } else go = false }
    val fill by animateFloatAsState(if (go) target else 0f, tween(900, easing = FastOutSlowInEasing), label = "fill")
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(color))
            Spacer(Modifier.width(6.dp))
            Text(name, style = MaterialTheme.typography.labelMedium, color = Color(0xFF374151))
        }
        Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)).background(Color(0xFFF1F5F9))) {
            Box(Modifier.fillMaxWidth(fill).fillMaxHeight().clip(RoundedCornerShape(50)).background(color))
        }
    }
}

@Composable
private fun MagicBadge() {
    val t = rememberInfiniteTransition(label = "magic")
    val spin by t.animateFloat(0f, 360f, infiniteRepeatable(tween(6000, easing = LinearEasing)), label = "spin")
    Box(
        Modifier.size(54.dp).shadow(14.dp, CircleShape).clip(CircleShape).background(Brush.sweepGradient(listOf(Amber, Color(0xFFF472B6), Violet, Amber))).graphicsLayer { rotationZ = spin },
        contentAlignment = Alignment.Center,
    ) {
        Box(Modifier.size(46.dp).clip(CircleShape).background(Color.White).graphicsLayer { rotationZ = -spin }, contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.AutoAwesome, null, Modifier.size(24.dp), tint = Indigo)
        }
    }
}

// ---------------------------------------------------------------- cena 2: o material da IA

@Composable
private fun MaterialScene(active: Boolean) {
    Box(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)) {
        Box(Modifier.align(Alignment.TopStart).offset(x = 4.dp, y = 8.dp)) {
            Entrance(active, 0) {
                SceneCard(Modifier.width(150.dp).rotate(-4f).floating(2)) {
                    Column(Modifier.padding(14.dp)) {
                        Chip(Icons.Outlined.MenuBook, "Teoria", Indigo)
                        Spacer(Modifier.height(10.dp))
                        Text("1. Lei de Ohm", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        Spacer(Modifier.height(8.dp))
                        TextLines(3)
                        Spacer(Modifier.height(8.dp))
                        Box(Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFEEF2FF)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Text("U = R · I", style = MaterialTheme.typography.labelLarge, color = Indigo, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
        Box(Modifier.align(Alignment.TopEnd).offset(x = (-2).dp, y = 30.dp)) {
            Entrance(active, 1) { FlipCard(active) }
        }
        Box(Modifier.align(Alignment.BottomCenter).offset(y = (-6).dp)) {
            Entrance(active, 2) { QuestionCard(active) }
        }
    }
}

@Composable
private fun Chip(icon: ImageVector, label: String, color: Color) {
    Row(Modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.12f)).padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(13.dp), tint = color)
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
    }
}

/** Um flashcard que vira sozinho: pergunta, depois a resposta. */
@Composable
private fun FlipCard(active: Boolean) {
    val t = rememberInfiniteTransition(label = "flip")
    val turn by t.animateFloat(0f, 1f, infiniteRepeatable(tween(4200, easing = LinearEasing)), label = "turn")
    // Parado de frente, gira, parado de verso, gira de volta.
    val angle = when {
        !active -> 0f
        turn < 0.35f -> 0f
        turn < 0.5f -> (turn - 0.35f) / 0.15f * 180f
        turn < 0.85f -> 180f
        else -> 180f + (turn - 0.85f) / 0.15f * 180f
    }
    val back = angle % 360f in 90f..270f
    Box(Modifier.floating(3).graphicsLayer { rotationY = angle; cameraDistance = 14f * density }) {
        SceneCard(Modifier.width(132.dp).height(116.dp)) {
            Column(
                Modifier.fillMaxSize().graphicsLayer { rotationY = if (back) 180f else 0f }.background(if (back) Color(0xFFECFDF5) else Color(0xFFF5F3FF)).padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Chip(Icons.Outlined.Style, if (back) "Resposta" else "Flashcard", if (back) Color(0xFF059669) else Violet)
                Spacer(Modifier.height(10.dp))
                Text(
                    if (back) "30 dias" else "Prazo para tomar posse?",
                    style = if (back) MaterialTheme.typography.titleLarge else MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF111827),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }
    }
}

/** Uma questão em que a alternativa certa acende em verde, com o check entrando. */
@Composable
private fun QuestionCard(active: Boolean) {
    var answered by remember { mutableStateOf(false) }
    LaunchedEffect(active) {
        answered = false
        while (active) { delay(1400); answered = true; delay(2600); answered = false }
    }
    SceneCard(Modifier.width(250.dp).floating(4, amplitude = 4.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Chip(Icons.Outlined.Check, "Questão · nível banca", Color(0xFFDB2777))
            }
            TextLines(2, widths = listOf(1f, 0.6f))
            listOf("A" to false, "B" to true, "C" to false).forEach { (key, correct) ->
                val highlight = answered && correct
                val bg by animateColorAsState(if (highlight) Color(0xFFD1FAE5) else Color(0xFFF8FAFC), tween(350), label = "bg$key")
                val border by animateColorAsState(if (highlight) Color(0xFF10B981) else Color(0xFFE5E7EB), tween(350), label = "bd$key")
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(border).padding(1.5.dp).clip(RoundedCornerShape(9.dp)).background(bg).padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("$key)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF374151))
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.weight(1f)) { TextLines(1, color = if (highlight) Color(0xFFA7F3D0) else Color(0xFFE5E7EB), widths = listOf(if (correct) 0.8f else 0.6f)) }
                    AnimatedVisibility(highlight, enter = scaleIn(tween(300)) + fadeIn()) {
                        Box(Modifier.size(20.dp).clip(CircleShape).background(Color(0xFF10B981)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Check, null, Modifier.size(14.dp), tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- cena 3: a evolução

@Composable
private fun ProgressScene(active: Boolean) {
    Box(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp), contentAlignment = Alignment.Center) {
        Entrance(active, 0) { MasteryRing(active) }
        Box(Modifier.align(Alignment.TopEnd).offset(x = (-4).dp, y = 18.dp)) {
            Entrance(active, 1) {
                SceneCard(Modifier.floating(5)) {
                    Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocalFireDepartment, null, Modifier.size(26.dp), tint = Color(0xFFF97316))
                        Spacer(Modifier.width(6.dp))
                        Column {
                            Text("12 dias", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color(0xFF111827))
                            Text("de sequência", style = MaterialTheme.typography.labelSmall, color = Color(0xFF6B7280))
                        }
                    }
                }
            }
        }
        Box(Modifier.align(Alignment.BottomStart).offset(x = 4.dp, y = (-14).dp)) {
            Entrance(active, 2) { WeekCard(active) }
        }
    }
}

@Composable
private fun MasteryRing(active: Boolean) {
    var go by remember { mutableStateOf(false) }
    LaunchedEffect(active) { if (active) { delay(350); go = true } else go = false }
    val value by animateFloatAsState(if (go) 0.78f else 0f, tween(1600, easing = FastOutSlowInEasing), label = "ring")
    Box(Modifier.size(170.dp).floating(6, amplitude = 4.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.size(170.dp).shadow(22.dp, CircleShape).clip(CircleShape).background(Color.White))
        Canvas(Modifier.size(140.dp)) {
            val stroke = 14.dp.toPx()
            drawArc(Color(0xFFEEF2FF), 0f, 360f, false, style = Stroke(stroke))
            // O degradê circular começa às 3 horas; girando o desenho, ele começa no topo junto com o arco.
            rotate(-90f) { drawArc(Brush.sweepGradient(0f to Indigo, 0.4f to Violet, 0.78f to Mint, 0.97f to Mint, 1f to Indigo), 0f, 360f * value, false, style = Stroke(stroke, cap = StrokeCap.Round)) }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${(value * 100).toInt()}%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color(0xFF111827))
            Text("domínio do edital", style = MaterialTheme.typography.labelSmall, color = Color(0xFF6B7280))
        }
    }
}

@Composable
private fun WeekCard(active: Boolean) {
    var lit by remember { mutableStateOf(0) }
    LaunchedEffect(active) {
        lit = 0
        if (active) repeat(6) { delay(220); lit = it + 1 }
    }
    SceneCard(Modifier.floating(7)) {
        Column(Modifier.padding(12.dp)) {
            Text("Esta semana", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF374151))
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                listOf("S", "T", "Q", "Q", "S", "S", "D").forEachIndexed { i, day ->
                    val on = i < lit
                    val bg by animateColorAsState(if (on) Mint else Color(0xFFF1F5F9), tween(250), label = "day$i")
                    Box(Modifier.size(22.dp).clip(CircleShape).background(bg), contentAlignment = Alignment.Center) {
                        if (on) Icon(Icons.Outlined.Check, null, Modifier.size(13.dp), tint = Color.White)
                        else Text(day, style = MaterialTheme.typography.labelSmall, color = Color(0xFF9CA3AF))
                    }
                }
            }
        }
    }
}

/** Barras, não bolinhas: o mesmo motivo da marca. A página atual é a barra larga e cheia. */
@Composable
private fun PageIndicator(count: Int, current: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(count) { index ->
            val active = index == current
            val width by animateDpAsState(if (active) 26.dp else 8.dp, tween(260), label = "indicator-width")
            val color by animateColorAsState(if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant, tween(260), label = "indicator-color")
            Box(Modifier.width(width).height(8.dp).clip(RoundedCornerShape(50)).background(color))
        }
    }
}

