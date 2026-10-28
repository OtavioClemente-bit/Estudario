package br.com.estudario.ui.profile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.domain.FolhaWardrobe
import br.com.estudario.domain.LevelCrest
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.EstudarioSom
import br.com.estudario.ui.brand.Glyph
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Subiu de nível, com cara de jogo (a mesma cena do site): o brasão antigo treme e estoura num clarão,
 * o novo cai com onda de choque, faíscas, tremida e o som de nível, o letreiro entra letra por letra e,
 * depois, a barra do nível novo, os prêmios (título novo, peças do armário, próxima patente) e o Folha.
 * Tocar na tela pula para o fim.
 */
@Composable
fun LevelUpScreen(event: AppViewModel.LevelUp, onClose: () -> Unit) {
    val level = event.level
    val old = (level - 1).coerceAtLeast(1)
    val rank = LevelCrest.rank(level)
    val c1 = hex(rank.light); val c2 = hex(rank.mid); val c3 = hex(rank.dark)
    val promo = event.newTitle || LevelCrest.isPromotion(level)
    val floor = ProgressEngine.xpForLevel(level)
    val need = (ProgressEngine.xpForLevel(level + 1) - floor).coerceAtLeast(1)
    val into = (event.totalXp - floor).coerceIn(0, need)
    val unlocked = remember(level) { FolhaWardrobe.items.filter { it.level == level } }
    val nextTitle = ProgressEngine.nextTitle(level)

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val reduced = remember { runCatching { android.provider.Settings.Global.getFloat(context.contentResolver, android.provider.Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f }.getOrDefault(false) }
    val clock = remember { Animatable(if (reduced) END else 0f) }
    LaunchedEffect(Unit) {
        if (reduced) return@LaunchedEffect
        clock.animateTo(IMPACT, tween((IMPACT * 1000).toInt(), easing = LinearEasing))
        EstudarioSom.play(context, "cena_nivel")
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        clock.animateTo(END, tween(((END - IMPACT) * 1000).toInt(), easing = LinearEasing))
    }
    val t = clock.value
    val loop = rememberInfiniteTransition(label = "lvx")
    val spin by loop.animateFloat(0f, 360f, infiniteRepeatable(tween(26_000, easing = LinearEasing)), label = "spin")
    val bob by loop.animateFloat(0f, 1f, infiniteRepeatable(tween(1600), RepeatMode.Reverse), label = "bob")
    val sparks = remember { List(30) { Triple(Random.nextInt(130, 240).toFloat(), Random.nextInt(4, 9).toFloat(), Random.nextInt(0, 140) / 1000f) } }
    val motes = remember { List(20) { listOf(Random.nextFloat(), Random.nextFloat(), Random.nextInt(3, 8).toFloat(), Random.nextInt(4, 9).toFloat()) } }

    BackHandler(onBack = onClose)
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        BoxWithConstraints(
            Modifier.fillMaxSize()
                .background(Brush.radialGradient(listOf(lerp(Color(0xFF0B0A24), c2, 0.42f), Color(0xFF0B0A24), Color(0xFF050414)), radius = 1600f, center = Offset.Unspecified))
                .clickable(remember { MutableInteractionSource() }, indication = null) { if (t < END) scope.launch { clock.snapTo(END) } },
        ) {
            // fundo: raios girando e pontinhos subindo
            val raysA = seg(t, IMPACT, 0.6f)
            Canvas(Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height * 0.27f)
                if (raysA > 0f) rotate(spin, center) {
                    for (i in 0 until 18) {
                        val a0 = (i * 20.0) * PI / 180; val a1 = (i * 20.0 + 7) * PI / 180
                        val r = size.maxDimension
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(center.x, center.y)
                            lineTo(center.x + (r * cos(a0)).toFloat(), center.y + (r * sin(a0)).toFloat())
                            lineTo(center.x + (r * cos(a1)).toFloat(), center.y + (r * sin(a1)).toFloat())
                            close()
                        }
                        drawPath(path, Brush.radialGradient(listOf(c1.copy(alpha = 0.16f * raysA), Color.Transparent), center, size.minDimension * 0.75f))
                    }
                }
                motes.forEach { (x, phase, s, dur) ->
                    val p = ((spin / 360f * 26f) / dur + phase) % 1f
                    drawCircle(c1.copy(alpha = (if (p < 0.15f) p / 0.15f else 1f - p) * 0.7f), s, Offset(x * size.width, size.height * (1.05f - p * 1.1f)))
                }
            }
            val quake = if (t in IMPACT..IMPACT + 0.42f) { val q = (t - IMPACT) / 0.42f; (sin(q * 40f) * (1f - q) * 7f) } else 0f
            val stageSize = if (maxHeight < 700.dp) 190.dp else 230.dp
            val narrow = maxWidth < 380.dp
            Column(
                Modifier.fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()).graphicsLayer { translationX = quake.dp.toPx(); translationY = (quake * 0.6f).dp.toPx() }
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            ) {
                // palco
                Box(Modifier.size(stageSize + 30.dp), contentAlignment = Alignment.Center) {
                    // halo
                    val haloA = seg(t, IMPACT, 0.5f)
                    if (haloA > 0f) Box(Modifier.size(stageSize).graphicsLayer { alpha = haloA * (0.75f + 0.25f * bob); scaleX = 0.95f + 0.1f * bob; scaleY = scaleX }
                        .background(Brush.radialGradient(listOf(c2.copy(alpha = 0.7f), Color.Transparent)), CircleShape))
                    // ondas e faíscas
                    Canvas(Modifier.size(stageSize)) {
                        val ctr = Offset(size.width / 2, size.height / 2)
                        for (i in 0 until 3) {
                            val p = seg(t, IMPACT + 0.03f + i * 0.14f, 0.9f)
                            if (p in 0.001f..0.999f) drawCircle(c1.copy(alpha = 0.95f * (1f - p)), size.minDimension * 0.42f * (0.35f + 1.95f * easeOut(p)), ctr, style = Stroke((3f * (1f - p) + 1f).dp.toPx()))
                        }
                        sparks.forEachIndexed { i, (r, s, dl) ->
                            val p = seg(t, IMPACT + 0.01f + dl, 0.85f)
                            if (p in 0.001f..0.999f) {
                                val a = (i * 360f / sparks.size) * PI.toFloat() / 180f
                                val d = (20f + (r - 20f) * easeOut(p)).dp.toPx()
                                val len = (s * 2.4f * (1.4f - p)).dp.toPx()
                                val dir = Offset(cos(a), sin(a))
                                drawLine(lerp(Color.White, c1, p).copy(alpha = 1f - p), ctr + dir * d, ctr + dir * (d + len), s.dp.toPx() * 0.6f, cap = StrokeCap.Round)
                            }
                        }
                    }
                    // brasão antigo: entra, treme e estoura
                    if (t < IMPACT + 0.25f) {
                        val inP = CubicBezierEasing(0.3f, 1.6f, 0.5f, 1f).transform(seg(t, 0.1f, 0.45f))
                        val shake = if (t in 0.75f..1.05f) sin((t - 0.75f) * 70f) * 6f else 0f
                        val pop = seg(t, IMPACT - 0.02f, 0.27f)
                        LevelCrestImage(old, stageSize, Modifier.graphicsLayer {
                            alpha = inP.coerceIn(0f, 1f) * (1f - pop); scaleX = (0.7f + 0.3f * inP) * (1f + 0.35f * pop); scaleY = scaleX
                            translationX = shake.dp.toPx(); rotationZ = shake * 0.5f
                        })
                    }
                    // brasão novo: cai com tudo e flutua
                    val slam = seg(t, IMPACT, 0.62f)
                    if (slam > 0f) {
                        val e = CubicBezierEasing(0.2f, 1.5f, 0.45f, 1f).transform(slam)
                        LevelCrestImage(level, stageSize, Modifier.graphicsLayer {
                            alpha = (slam * 2f).coerceAtMost(1f); val sc = 2.6f + (1f - 2.6f) * e; scaleX = sc; scaleY = sc
                            rotationZ = -14f * (1f - e); translationY = if (slam >= 1f) (-8f * bob).dp.toPx() else 0f
                        })
                    }
                    // clarão
                    val flash = seg(t, IMPACT - 0.03f, 0.55f)
                    if (flash in 0.001f..0.999f) Box(Modifier.size(stageSize * 1.3f).graphicsLayer { alpha = if (flash < 0.25f) flash / 0.25f else (1f - flash) / 0.75f; scaleX = 0.3f + 1.3f * flash; scaleY = scaleX }
                        .background(Brush.radialGradient(listOf(Color.White, c1.copy(alpha = 0.8f), Color.Transparent)), CircleShape))
                    // carimbo de promoção
                    if (promo) {
                        val st = seg(t, 1.6f, 0.45f)
                        if (st > 0f) Text(
                            "PROMOVIDO!",
                            Modifier.align(Alignment.TopEnd).padding(top = 6.dp).rotate(12f)
                                .graphicsLayer { val e = CubicBezierEasing(0.2f, 1.6f, 0.4f, 1f).transform(st); alpha = (st * 1.5f).coerceAtMost(1f); scaleX = 2.6f + (1f - 2.6f) * e; scaleY = scaleX }
                                .clip(RoundedCornerShape(10.dp)).background(Brush.verticalGradient(listOf(Color(0xFFFFF3B8), Color(0xFFFFC22E), Color(0xFFE08A00))))
                                .border(2.dp, Color.White, RoundedCornerShape(10.dp)).padding(horizontal = 12.dp, vertical = 5.dp),
                            color = Color(0xFF2B1600), fontWeight = FontWeight.Black, fontSize = 15.sp, letterSpacing = 1.sp,
                        )
                    }
                }
                // letreiro
                Appear(t, 0.2f) {
                    Row(
                        Modifier.clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.07f)).border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(50)).padding(horizontal = 14.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("NÍVEL $old", color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.6.sp)
                        Text("▶", color = c1, fontSize = 9.sp)
                        Text("NÍVEL $level", color = c1, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.6.sp)
                    }
                }
                Row(horizontalArrangement = Arrangement.Center) {
                    val word = "SUBIU DE NÍVEL!"
                    val big = if (narrow) 32.sp else 40.sp
                    word.forEachIndexed { i, ch ->
                        val p = seg(t, 1.35f + i * 0.035f, 0.5f)
                        val e = CubicBezierEasing(0.2f, 1.7f, 0.45f, 1f).transform(p)
                        Text(
                            if (ch == ' ') " " else ch.toString(),
                            Modifier.graphicsLayer { alpha = (p * 1.4f).coerceAtMost(1f); translationY = (36f * (1f - e)).dp.toPx(); scaleX = 0.3f + 0.7f * e; scaleY = scaleX; rotationZ = -12f * (1f - e) },
                            style = TextStyle(brush = Brush.verticalGradient(listOf(Color.White, c1, c2)), fontSize = big, fontWeight = FontWeight.Black,
                                shadow = androidx.compose.ui.graphics.Shadow(c3, Offset(0f, 6f), 2f)),
                        )
                    }
                }
                Appear(t, 1.75f) {
                    Text(
                        if (promo) "Nova patente: ${event.title} · ${rank.metal}" else "${event.title} · patente ${rank.metal} · ${LevelCrest.stars(level)}★",
                        color = Color.White.copy(alpha = 0.82f), fontSize = 15.sp, textAlign = TextAlign.Center,
                    )
                }
                // barra do nível novo
                Appear(t, 1.9f) {
                    Column(Modifier.widthIn(max = 420.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Nível $level", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                            Text("faltam ${need - into} XP para o nível ${level + 1}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        }
                        val fill = easeOut(seg(t, 2.0f, 0.9f)) * (into.toFloat() / need).coerceAtLeast(0.02f)
                        Box(Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.1f)).border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(50))) {
                            Box(Modifier.fillMaxHeight().fillMaxWidth(fill).clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(c3, c2, c1))))
                        }
                    }
                }
                // prêmios
                val loot = buildList {
                    if (promo) add(Triple(Glyph.Medal, "NOVO TÍTULO", "${event.title} · patente ${rank.metal}"))
                    unlocked.take(2).forEach { add(Triple(Glyph.Hanger, "LIBEROU NO ARMÁRIO", "${it.name} · ${it.rarity.label}")) }
                    if (nextTitle != null) {
                        val (at, name) = nextTitle
                        add(Triple(Glyph.Flag, "PRÓXIMA PATENTE", "$name no nível $at · ${if (at - level == 1) "falta 1 nível" else "faltam ${at - level} níveis"}"))
                    } else add(Triple(Glyph.Trophy, "PATENTE MÁXIMA", "Você é ${ProgressEngine.levelTitle(level)}. Agora é colecionar estrelas."))
                }
                Column(Modifier.widthIn(max = 420.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    loot.forEachIndexed { i, (glyph, kicker, text) ->
                        val p = seg(t, 2.2f + i * 0.15f, 0.45f)
                        val e = CubicBezierEasing(0.2f, 1.4f, 0.45f, 1f).transform(p)
                        val gold = kicker == "NOVO TÍTULO"
                        Row(
                            Modifier.fillMaxWidth().graphicsLayer { alpha = p.coerceAtMost(1f); translationY = (18f * (1f - e)).dp.toPx(); scaleX = 0.94f + 0.06f * e; scaleY = scaleX }
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (gold) Brush.verticalGradient(listOf(Color(0x3DFFC22E), Color(0x14FFC22E))) else Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.09f), Color.White.copy(alpha = 0.04f))))
                                .border(1.dp, if (gold) Color(0x8CFFD66E) else Color.White.copy(alpha = 0.14f), RoundedCornerShape(16.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Box(Modifier.size(38.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.08f)), contentAlignment = Alignment.Center) { BrandIcon(glyph, size = 28.dp) }
                            Column {
                                Text(kicker, color = if (gold) Color(0xFFFFD66E) else c1, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp)
                                Text(text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, lineHeight = 18.sp)
                            }
                        }
                    }
                }
                // o Folha
                Appear(t, 2.7f) {
                    Row(Modifier.widthIn(max = 420.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Folha(56.dp, mood = FolhaMood.PROUD)
                        Text(
                            levelLine(level, event.title, promo),
                            Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(horizontal = 14.dp, vertical = 10.dp),
                            color = Color(0xFF1A1440), fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 19.sp,
                        )
                    }
                }
                Appear(t, 2.9f) {
                    Box(
                        Modifier.widthIn(min = 210.dp).clip(RoundedCornerShape(16.dp))
                            .background(Brush.verticalGradient(listOf(Color(0xFFFFF3B8), Color(0xFFFFC22E), Color(0xFFE89A00))))
                            .border(1.dp, Color(0xFF8A5600).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clickable(onClick = onClose).padding(horizontal = 34.dp, vertical = 15.dp),
                        contentAlignment = Alignment.Center,
                    ) { Text("Continuar", color = Color(0xFF2B1600), fontWeight = FontWeight.Black, fontSize = 16.sp) }
                }
            }
        }
    }
}

private const val IMPACT = 1.05f
private const val END = 3.4f

/** Quanto da etapa que começa em [start] e dura [dur] segundos já passou (0 a 1). */
private fun seg(t: Float, start: Float, dur: Float): Float = ((t - start) / dur).coerceIn(0f, 1f)
private fun easeOut(x: Float): Float = 1f - (1f - x) * (1f - x) * (1f - x)

/** Entra subindo um pouco, a partir do segundo [at] da cena. */
@Composable
private fun Appear(t: Float, at: Float, content: @Composable () -> Unit) {
    val p = easeOut(seg(t, at, 0.4f))
    Box(Modifier.graphicsLayer { alpha = p; translationY = (14f * (1f - p)).dp.toPx() }) { content() }
}

private fun hex(s: String): Color = Color(android.graphics.Color.parseColor(s))

/** A fala do Folha na subida de nível (a mesma do site). */
private fun levelLine(level: Int, title: String, promo: Boolean): String = when {
    promo -> "Promoção! Agora você é $title. Isso é estudo de verdade aparecendo."
    level % 3 == 0 -> "Nível $level no bolso. Bora pro ${level + 1}?"
    level % 3 == 1 -> "Mais um degrau rumo à vaga. Cada XP é questão feita e revisão cumprida."
    else -> "Subiu! Quem estuda todo dia chega lá. Você está chegando."
}
