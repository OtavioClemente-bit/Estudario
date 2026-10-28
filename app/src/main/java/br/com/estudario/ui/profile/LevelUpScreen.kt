package br.com.estudario.ui.profile

import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.runtime.setValue
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
    val promo = event.newTitle || LevelCrest.isPromotion(level)
    val floor = ProgressEngine.xpForLevel(level)
    val need = (ProgressEngine.xpForLevel(level + 1) - floor).coerceAtLeast(1)
    val into = (event.totalXp - floor).coerceIn(0, need)
    val unlocked = remember(level) { FolhaWardrobe.items.filter { it.level == level } }
    val nextTitle = ProgressEngine.nextTitle(level)
    val (c1, c2, c3) = Triple(hex(rank.light), hex(rank.mid), hex(rank.dark))
    GameScene(
        c1, c2, c3, sound = "cena_nivel", stamp = if (promo) "PROMOVIDO!" else null,
        from = "NÍVEL $old", to = "NÍVEL $level", word = "SUBIU DE NÍVEL!",
        sub = if (promo) "Nova patente: ${event.title} · ${rank.metal}" else "${event.title} · patente ${rank.metal} · ${LevelCrest.stars(level)}★",
        loot = buildList {
            if (promo) add(GameLoot(Glyph.Medal, "NOVO TÍTULO", "${event.title} · patente ${rank.metal}", gold = true))
            unlocked.take(2).forEach { add(GameLoot(Glyph.Hanger, "LIBEROU NO ARMÁRIO", "${it.name} · ${it.rarity.label}")) }
            if (nextTitle != null) {
                val (at, name) = nextTitle
                add(GameLoot(Glyph.Flag, "PRÓXIMA PATENTE", "$name no nível $at · ${if (at - level == 1) "falta 1 nível" else "faltam ${at - level} níveis"}"))
            } else add(GameLoot(Glyph.Trophy, "PATENTE MÁXIMA", "Você é ${ProgressEngine.levelTitle(level)}. Agora é colecionar estrelas."))
        },
        line = levelLine(level, event.title, promo), primary = "Continuar", onPrimary = onClose, onClose = onClose,
        // barra do nível novo
        middle = { t ->
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
        },
        before = { s -> LevelCrestImage(old, s) },
        after = { s -> LevelCrestImage(level, s) },
    )
}

/** Um prêmio da cena de jogo: desenho, rótulo e texto; [gold] destaca o principal. */
internal data class GameLoot(val glyph: Glyph, val kicker: String, val text: String, val gold: Boolean = false)

/**
 * A cena de jogo (nível, sequência e emblema, iguais às do site): o "antes" treme e estoura num
 * clarão, o "depois" cai com ondas, faíscas, tremida, som e vibração; o letreiro entra letra por
 * letra, depois o [middle] (barra, dias), os prêmios, o Folha e o botão. Tocar na tela pula para o fim.
 */
@Composable
internal fun GameScene(
    c1: Color, c2: Color, c3: Color, sound: String?, stamp: String?,
    from: String, to: String, word: String, sub: String, loot: List<GameLoot>, line: String,
    primary: String, onPrimary: () -> Unit, onClose: () -> Unit,
    middle: (@Composable (Float) -> Unit)? = null,
    before: @Composable (androidx.compose.ui.unit.Dp) -> Unit,
    after: @Composable (androidx.compose.ui.unit.Dp) -> Unit,
    /** Sons antes do impacto: (segundo da cena, nome). */
    cues: List<Pair<Float, String>> = emptyList(),
    /** Segundos antes da linha do tempo começar (a forja do emblema roda primeiro). */
    delay: Float = 0f,
    /** Algo desenhado no fundo do palco (a forja), com o tamanho do palco. */
    backdrop: (@Composable (androidx.compose.ui.unit.Dp, Float) -> Unit)? = null,
    /** Altura do palco em relação à largura, e quanto o impacto sobe ou desce nele (fração da altura). */
    stageTall: Float = 1f, fxShift: Float = 0f,
    /** Peças a mais no palco que seguem o relógio (redemoinho, o Folha). */
    stageExtra: (@Composable BoxScope.(Float, androidx.compose.ui.unit.Dp) -> Unit)? = null,
    /** O Folha da fala: o humor, e se ele aparece ali (na sequência ele fica do lado da fênix, no palco). */
    folhaMood: FolhaMood = FolhaMood.PROUD, folhaInLine: Boolean = true,
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val reduced = remember { runCatching { android.provider.Settings.Global.getFloat(context.contentResolver, android.provider.Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f }.getOrDefault(false) }
    val clock = remember { Animatable(if (reduced) END + delay else 0f) }
    LaunchedEffect(Unit) {
        if (reduced) return@LaunchedEffect
        var at = 0f
        for ((cueAt, name) in cues.sortedBy { it.first }.filter { it.first < IMPACT }) {
            clock.animateTo(delay + cueAt, tween(((delay + cueAt - at) * 1000).toInt(), easing = LinearEasing)); at = delay + cueAt
            EstudarioSom.play(context, name)
        }
        clock.animateTo(delay + IMPACT, tween(((delay + IMPACT - at) * 1000).toInt(), easing = LinearEasing))
        if (sound != null) EstudarioSom.play(context, sound)
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        clock.animateTo(delay + END, tween(((END - IMPACT) * 1000).toInt(), easing = LinearEasing))
    }
    val t = clock.value - delay
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
                .clickable(remember { MutableInteractionSource() }, indication = null) { if (t < END) scope.launch { clock.snapTo(END + delay) } },
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
                // palco (com a forja no fundo, quando houver; o impacto pode subir até a medalha)
                Box(Modifier.width(stageSize + 30.dp).height((stageSize + 30.dp) * stageTall), contentAlignment = Alignment.Center) {
                backdrop?.invoke(stageSize, t)
                Box(Modifier.size(stageSize + 30.dp).offset(y = (stageSize + 30.dp) * stageTall * fxShift), contentAlignment = Alignment.Center) {
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
                        Box(Modifier.graphicsLayer {
                            alpha = inP.coerceIn(0f, 1f) * (1f - pop); scaleX = (0.7f + 0.3f * inP) * (1f + 0.35f * pop); scaleY = scaleX
                            translationX = shake.dp.toPx(); rotationZ = shake * 0.5f
                        }) { before(stageSize) }
                    }
                    // brasão novo: cai com tudo e flutua
                    val slam = seg(t, IMPACT, 0.62f)
                    if (slam > 0f) {
                        val e = CubicBezierEasing(0.2f, 1.5f, 0.45f, 1f).transform(slam)
                        Box(Modifier.graphicsLayer {
                            alpha = (slam * 2f).coerceAtMost(1f); val sc = 2.6f + (1f - 2.6f) * e; scaleX = sc; scaleY = sc
                            rotationZ = -14f * (1f - e); translationY = if (slam >= 1f) (-8f * bob).dp.toPx() else 0f
                        }) { after(stageSize) }
                    }
                    // clarão
                    val flash = seg(t, IMPACT - 0.03f, 0.55f)
                    if (flash in 0.001f..0.999f) Box(Modifier.size(stageSize * 1.3f).graphicsLayer { alpha = if (flash < 0.25f) flash / 0.25f else (1f - flash) / 0.75f; scaleX = 0.3f + 1.3f * flash; scaleY = scaleX }
                        .background(Brush.radialGradient(listOf(Color.White, c1.copy(alpha = 0.8f), Color.Transparent)), CircleShape))
                    // carimbo de promoção
                    if (stamp != null) {
                        val st = seg(t, 1.6f, 0.45f)
                        if (st > 0f) Text(
                            stamp,
                            Modifier.align(Alignment.TopEnd).padding(top = 6.dp).rotate(12f)
                                .graphicsLayer { val e = CubicBezierEasing(0.2f, 1.6f, 0.4f, 1f).transform(st); alpha = (st * 1.5f).coerceAtMost(1f); scaleX = 2.6f + (1f - 2.6f) * e; scaleY = scaleX }
                                .clip(RoundedCornerShape(10.dp)).background(Brush.verticalGradient(listOf(Color(0xFFFFF3B8), Color(0xFFFFC22E), Color(0xFFE08A00))))
                                .border(2.dp, Color.White, RoundedCornerShape(10.dp)).padding(horizontal = 12.dp, vertical = 5.dp),
                            color = Color(0xFF2B1600), fontWeight = FontWeight.Black, fontSize = 15.sp, letterSpacing = 1.sp,
                        )
                    }
                    stageExtra?.invoke(this, t, stageSize)
                }
                }
                // letreiro
                Appear(t, 0.2f) {
                    Row(
                        Modifier.clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.07f)).border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(50)).padding(horizontal = 14.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(from, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.6.sp)
                        Text("▶", color = c1, fontSize = 9.sp)
                        Text(to, color = c1, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.6.sp)
                    }
                }
                Row(horizontalArrangement = Arrangement.Center) {
                    val big = if (narrow || word.length > 13) 32.sp else 40.sp
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
                    Text(sub, color = Color.White.copy(alpha = 0.82f), fontSize = 15.sp, textAlign = TextAlign.Center)
                }
                if (middle != null) Appear(t, 1.9f) { middle(t) }
                // prêmios
                Column(Modifier.widthIn(max = 420.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    loot.forEachIndexed { i, (glyph, kicker, text, gold) ->
                        val p = seg(t, 2.2f + i * 0.15f, 0.45f)
                        val e = CubicBezierEasing(0.2f, 1.4f, 0.45f, 1f).transform(p)
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
                        if (folhaInLine) Folha(56.dp, mood = folhaMood)
                        Text(
                            line,
                            Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(horizontal = 14.dp, vertical = 10.dp),
                            textAlign = if (folhaInLine) TextAlign.Start else TextAlign.Center,
                            color = Color(0xFF1A1440), fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 19.sp,
                        )
                    }
                }
                Appear(t, 2.9f) {
                    Box(
                        Modifier.widthIn(min = 210.dp).clip(RoundedCornerShape(16.dp))
                            .background(Brush.verticalGradient(listOf(Color(0xFFFFF3B8), Color(0xFFFFC22E), Color(0xFFE89A00))))
                            .border(1.dp, Color(0xFF8A5600).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clickable(onClick = onPrimary).padding(horizontal = 34.dp, vertical = 15.dp),
                        contentAlignment = Alignment.Center,
                    ) { Text(primary, color = Color(0xFF2B1600), fontWeight = FontWeight.Black, fontSize = 16.sp) }
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

// ---------------------------------------------------------------- a fênix

/**
 * A fênix da sequência pintada com fogo em tempo real (phoenix.js, o mesmo do site, numa WebView
 * transparente): dorme como carvão com veias de brasa e, quando [wake] vira verdade (o impacto da cena),
 * o app a acende — ela explode em fogo e abre as asas. A WebView é maior que a fênix para o brilho caber.
 */
@android.annotation.SuppressLint("SetJavaScriptEnabled")
@Composable
private fun FenixScene(size: androidx.compose.ui.unit.Dp, wake: Boolean) {
    val px = size.value.toInt()
    val html = remember(px) {
        """<!doctype html><html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>html,body{margin:0;padding:0;background:transparent;overflow:hidden}estudario-fenix{display:block;width:${px}px;height:${px}px;margin:${px / 2}px auto 0}</style></head>
<body><script src="phoenix.js"></script><estudario-fenix state="sleep"></estudario-fenix></body></html>"""
    }
    val lit = remember { booleanArrayOf(false) }
    androidx.compose.ui.viewinterop.AndroidView(
        factory = { ctx ->
            android.webkit.WebView(ctx).apply {
                setBackgroundColor(0)
                settings.javaScriptEnabled = true
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
                overScrollMode = android.view.View.OVER_SCROLL_NEVER
                importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO
                loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null)
            }
        },
        update = { view -> if (wake && !lit[0]) { lit[0] = true; view.evaluateJavascript("document.querySelector('estudario-fenix').ignite()", null) } },
        onRelease = { it.destroy() },
        // a fênix pinta além da própria caixa (35% dos lados, 45% em cima, para o fogo subir)
        modifier = Modifier.requiredSize(size * 1.8f).offset(y = -(size * 0.15f)),
    )
}

/** O "antes" da fênix: o "z" subindo enquanto ela dorme. */
@Composable
private fun SleepZ(size: androidx.compose.ui.unit.Dp) {
    val z by rememberInfiniteTransition(label = "z").animateFloat(0f, 1f, infiniteRepeatable(tween(1800, easing = LinearEasing)), label = "z")
    Box(Modifier.size(size)) {
        Box(Modifier.align(Alignment.TopEnd).padding(end = size * 0.12f, top = size * 0.14f)) {
            listOf(0f, 0.33f, 0.66f).forEachIndexed { i, off ->
                val p = (z + off) % 1f
                Text("z", Modifier.offset(x = (i * 12).dp + (10f * p).dp, y = (-i * 16).dp - (14f * p).dp).graphicsLayer { alpha = if (p < 0.3f) p / 0.3f else 1f - p },
                    color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Black, fontSize = (16 + i * 6).sp)
            }
        }
    }
}


/** O redemoinho: um pontinho preto que gira cada vez mais rápido e cresce até o clarão. */
@Composable
private fun BoxScope.Vortex(t: Float, size: androidx.compose.ui.unit.Dp) {
    val p = seg(t, 0.2f, 0.82f)
    if (t >= IMPACT + 0.1f) return
    Canvas(Modifier.size(size).align(Alignment.Center)) {
        val scale = 0.035f + (1.5f - 0.035f) * (p * p)
        val r = this.size.minDimension * 0.42f * scale
        val alpha = if (p > 0.92f) ((1f - p) / 0.08f).coerceIn(0f, 1f) else 1f
        rotate(1700f * p * p) {
            for (i in 0 until 12) drawArc(if (i % 2 == 0) Color(0xFF0B0618).copy(alpha = alpha) else Color(0xFF3B1E55).copy(alpha = alpha), i * 30f, 22f, true, topLeft = center - Offset(r, r), size = androidx.compose.ui.geometry.Size(r * 2, r * 2))
        }
        drawCircle(Color(0xFF05030C).copy(alpha = alpha), r * 0.34f, center)
    }
}

/** Os dias da semana: chama acesa nos dias estudados, apagada nos outros; hoje maior, escrito HOJE. */
@Composable
private fun GameWeekRow(week: List<Pair<String, Boolean>>, t: Float, c1: Color, c2: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Bottom) {
        week.forEachIndexed { i, (letter, done) ->
            val p = seg(t, 1.9f + i * 0.09f, 0.42f)
            val e = CubicBezierEasing(0.2f, 1.6f, 0.45f, 1f).transform(p)
            val today = i == week.lastIndex
            Column(
                Modifier.width(if (today) 52.dp else 40.dp)
                    .graphicsLayer { alpha = p.coerceAtMost(1f); translationY = (16f * (1f - e) - if (today && done) 4f else 0f).dp.toPx(); scaleX = 0.6f + 0.4f * e; scaleY = scaleX }
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (done) Brush.verticalGradient(listOf(c1.copy(alpha = 0.4f), c2.copy(alpha = 0.3f))) else Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.06f), Color.White.copy(alpha = 0.06f))))
                    .border(if (today) 2.dp else 1.dp, if (done) c1.copy(alpha = if (today) 1f else 0.7f) else Color.White.copy(alpha = if (today) 0.4f else 0.12f), RoundedCornerShape(14.dp))
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(if (today) "HOJE" else letter, color = if (today) c1 else if (done) Color.White else Color.White.copy(alpha = 0.55f), fontSize = 10.sp, fontWeight = FontWeight.Black)
                Box(Modifier.graphicsLayer { alpha = if (done) 1f else 0.45f }) { StreakFlame(lit = done, size = if (today) 28.dp else 20.dp, glow = false) }
            }
        }
    }
}

// ---------------------------------------------------------------- sequência do dia

/** Os marcos da sequência (dias seguidos) que a cena mostra como próximo objetivo (os mesmos do site). */
private val STREAK_MARKS = listOf(3, 7, 14, 21, 30, 45, 60, 90, 120, 180, 270, 365)
private val FIRE1 = Color(0xFFFFE08A)
private val FIRE2 = Color(0xFFFF8A3D)
private val FIRE3 = Color(0xFFB23A0E)

private fun weekOf(summary: br.com.estudario.domain.StreakSummary): List<Pair<String, Boolean>> {
    val letters = listOf("S", "T", "Q", "Q", "S", "S", "D")
    return summary.week.filter { !it.future }.takeLast(7).map { letters[it.date.dayOfWeek.ordinal] to it.done }
}

/**
 * Dia cumprido, com cara de jogo (a mesma cena do site): a fênix dorme cinza; um pontinho preto gira
 * cada vez mais rápido, vira redemoinho e, no clarão, ela acorda pegando fogo (o Folha leva um susto).
 * Os dias da semana acendem com a chama, hoje em destaque, e entram os prêmios.
 */
@Composable
fun StreakCelebrationScreen(celebration: StreakCelebration, onClose: () -> Unit) {
    val summary = celebration.summary
    val days = summary.current.coerceAtLeast(1)
    val best = maxOf(summary.best, days)
    val record = days > 1 && days >= best
    val next = STREAK_MARKS.firstOrNull { it > days }
    val week = weekOf(summary)
    GameScene(
        FIRE1, FIRE2, FIRE3, sound = "combo", stamp = if (record) "RECORDE!" else null,
        from = if (days <= 1) "DORMINDO" else "DIA ${days - 1}", to = if (days == 1) "ACORDOU" else "DIA $days", word = "SEQUÊNCIA!",
        sub = if (days == 1) "1 dia · a fênix acordou" else "$days dias seguidos estudando",
        loot = buildList {
            if (celebration.xpToday > 0) add(GameLoot(Glyph.Bolt, "XP DE HOJE", "+${celebration.xpToday} XP no dia", gold = true))
            if (next != null) add(GameLoot(Glyph.Flag, "PRÓXIMO MARCO", "$next dias · ${if (next - days == 1) "falta 1 dia" else "faltam ${next - days} dias"}"))
            add(if (record) GameLoot(Glyph.Trophy, "NOVO RECORDE", "Sua maior sequência: $days dias") else GameLoot(Glyph.Trophy, "SEU RECORDE", "$best dias · faltam ${best - days} para bater"))
        },
        line = when {
            days <= 1 -> "Que susto! A fênix acordou. Volte amanhã para ela não dormir de novo."
            record -> "Recorde! A fênix nunca esteve tão acesa. Amanhã tem mais."
            else -> "Ela acordou com tudo! Faltam ${best - days} para o seu recorde de $best."
        },
        primary = "Bora continuar", onPrimary = onClose, onClose = onClose,
        middle = { t -> GameWeekRow(week, t, FIRE1, FIRE2) },
        before = { s -> SleepZ(s) },
        after = { },
        backdrop = { s, t -> FenixScene(s * 1.3f, wake = t >= IMPACT) },
        cues = listOf(0.25f to "tonto"),
        folhaInLine = false,
        stageExtra = { t, s ->
            Vortex(t, s)
            // o Folha do lado, assustado com a fênix pegando fogo: aparece no clarão e recua um pouco
            val r = seg(t, 1.12f, 0.7f)
            if (r > 0f) {
                val e = CubicBezierEasing(0.2f, 1.2f, 0.4f, 1f).transform(r)
                Box(Modifier.align(Alignment.BottomStart).offset(x = (-30).dp, y = (-20).dp).graphicsLayer { alpha = (r * 3f).coerceAtMost(1f); translationX = (16f - 24f * e).dp.toPx(); rotationZ = 4f - 10f * e }) {
                    Folha(64.dp, mood = FolhaMood.SURPRISED)
                }
            }
            StreakCounter(days, t)
        },
    )
}

/**
 * O contador da sequência (o mesmo do site): o número de ontem sobe e sai, o de hoje entra rolando de
 * baixo com um pulo e acende em fogo, com "dias de sequência" embaixo. Fica na base da fênix.
 */
@Composable
private fun BoxScope.StreakCounter(days: Int, t: Float) {
    val out = seg(t, 1.02f, 0.4f)
    val inP = seg(t, 1.1f, 0.62f)
    val e = CubicBezierEasing(0.2f, 1.6f, 0.4f, 1f).transform(inP)
    val fire = TextStyle(
        brush = Brush.verticalGradient(listOf(Color(0xFFFFFBE0), Color(0xFFFFD24A), Color(0xFFFF7A1A), Color(0xFFC8261A))),
        fontSize = 72.sp, fontWeight = FontWeight.Black, shadow = androidx.compose.ui.graphics.Shadow(Color(0xFF7A1A08), Offset(0f, 8f), 6f),
    )
    Column(Modifier.align(Alignment.BottomCenter).offset(y = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.height(84.dp).widthIn(min = 120.dp).clip(RoundedCornerShape(0.dp)), contentAlignment = Alignment.Center) {
            if (out < 1f) Text("${(days - 1).coerceAtLeast(0)}", Modifier.graphicsLayer { translationY = (-90f * out).dp.toPx(); alpha = 1f - out },
                style = fire.copy(brush = Brush.verticalGradient(listOf(Color(0xFFB3AFCB), Color(0xFF6E6A8A)))))
            if (inP > 0f) Text("$days", Modifier.graphicsLayer { translationY = (90f * (1f - e)).dp.toPx(); alpha = inP.coerceAtMost(1f); scaleX = 0.7f + 0.3f * e; scaleY = scaleX }, style = fire)
        }
        Text(if (days == 1) "DIA DE SEQUÊNCIA" else "DIAS DE SEQUÊNCIA", Modifier.graphicsLayer { alpha = seg(t, 1.4f, 0.4f) },
            color = Color(0xFFFFD9A0), fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 2.6.sp)
    }
}

/**
 * O Folha salva a sequência com o marca-página (a mesma cena do site): a fênix dorme cinza, ele chega na
 * ponta dos pés com o marca-página, solta uma faísca e ela acorda em fogo. Uma vez por dia salvo.
 */
@Composable
fun StreakSaveScene(days: Int, left: Int, dayLabel: String, week: List<Pair<String, Int>> = emptyList(), onClose: () -> Unit) {
    val days7 = week.ifEmpty { listOf("T" to 1, "Q" to 1, "Q" to 1, "S" to 1, "S" to 1, "D" to 2, "S" to 0) }.map { it.first to (it.second != 0) }
    GameScene(
        FIRE1, FIRE2, FIRE3, sound = "combo", stamp = "SALVA!",
        from = "QUASE APAGOU", to = "ACESA", word = "SEQUÊNCIA SALVA!",
        sub = "$days ${if (days == 1) "dia" else "dias"}" + if (dayLabel.isNotBlank()) " · o Folha marcou $dayLabel" else " de sequência",
        loot = listOf(
            GameLoot(Glyph.Bookmark, "MARCA-PÁGINA USADO", if (dayLabel.isNotBlank()) "No dia sem estudo: $dayLabel" else "No dia sem estudo", gold = true),
            GameLoot(Glyph.Notebook, "SOBRAM", if (left > 0) "$left ${if (left == 1) "marca-página guardado" else "marca-páginas guardados"}" else "Nenhum. Estude 7 dias seguidos para ganhar outro"),
            GameLoot(Glyph.Fire, "SEQUÊNCIA", "$days ${if (days == 1) "dia" else "dias"} e contando"),
        ),
        line = "Shhh… ela quase apagou, mas eu acendi de novo pra você. Bora não deixar dormir hoje?",
        primary = "Bora continuar", onPrimary = onClose, onClose = onClose,
        middle = { t -> GameWeekRow(days7, t, FIRE1, FIRE2) },
        before = { s -> SleepZ(s) },
        after = { },
        backdrop = { s, t -> FenixScene(s * 1.3f, wake = t >= IMPACT) },
        cues = listOf(0.8f to "toque"),
        folhaInLine = false,
        stageExtra = { t, s ->
            StreakCounter(days, t)
            // o Folha chega na ponta dos pés com o marca-página…
            val walk = seg(t, 0.05f, 0.9f)
            Box(Modifier.align(Alignment.BottomStart).offset(x = (-20).dp).graphicsLayer {
                alpha = (walk * 5f).coerceAtMost(1f); translationX = (-120f * (1f - walk)).dp.toPx(); translationY = (-6f * kotlin.math.abs(kotlin.math.sin(walk * PI.toFloat() * 4f))).dp.toPx()
            }) {
                Folha(52.dp, mood = FolhaMood.WINK)
                Box(Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = (-6).dp).rotate(if (t > 0.7f) -20f else 18f)) { BrandIcon(Glyph.Bookmark, size = 26.dp) }
            }
            // …e solta a faísca que acende a fênix
            val sp = seg(t, 0.78f, 0.32f)
            if (sp in 0.001f..0.999f) Box(
                Modifier.align(Alignment.Center).offset(x = (-80f * (1f - sp)).dp, y = (80f * (1f - sp)).dp).size((10f + 8f * sp).dp).clip(CircleShape)
                    .background(Brush.radialGradient(listOf(Color.White, Color(0xFFFFC22E), Color.Transparent))),
            )
        },
    )
}

// ---------------------------------------------------------------- emblema novo

private fun faixaLabel(tier: Int) = when (tier) {
    1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda"
}

/** Quando a medalha sai da forja na cena do Folha (folha-live.js, cena "emblema"). */
private const val FORGE_REVEAL = 4.35f

/**
 * Emblema novo: a forja do Folha (a mesma cena do site: ele cunha a medalha em três marteladas e ela
 * sobe com a arte de drawable-nodpi/emblema_<id>). Quando ela sai, onda de choque, faíscas e a faixa
 * carimbada; depois o letreiro e os prêmios. Quando cai mais de um de uma vez, cada um ganha a sua cena.
 */
@Composable
fun BadgeCelebrationScreen(badges: List<br.com.estudario.domain.Badge>, onClose: () -> Unit) {
    if (badges.isEmpty()) return
    var index by remember(badges) { androidx.compose.runtime.mutableIntStateOf(0) }
    val badge = badges.getOrNull(index) ?: return
    val last = index >= badges.lastIndex
    val tier = faixaLabel(badge.tier)
    val rank = LevelCrest.ranks.first { it.metal == tier }
    val ctx = LocalContext.current
    // a arte ilustrada do emblema vai para a cena como data URL (a WebView não lê res/drawable)
    val arte = remember(badge.id) {
        val res = ctx.resources.getIdentifier("emblema_" + badge.id.replace('-', '_'), "drawable", ctx.packageName)
        if (res == 0) null else runCatching {
            "data:image/webp;base64," + android.util.Base64.encodeToString(ctx.resources.openRawResource(res).use { it.readBytes() }, android.util.Base64.NO_WRAP)
        }.getOrNull()
    }
    val screenW = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp.dp
    androidx.compose.runtime.key(index) {
        GameScene(
            hex(rank.light), hex(rank.mid), hex(rank.dark), sound = null, stamp = tier.uppercase() + "!",
            from = "BLOQUEADO", to = "DESBLOQUEADO", word = "CONQUISTA!",
            sub = "${badge.name} · emblema $tier" + if (badges.size > 1) "  (${index + 1}/${badges.size})" else "",
            loot = listOf(
                GameLoot(Glyph.Trophy, "O QUE VOCÊ FEZ", badge.requirement, gold = true),
                GameLoot(Glyph.Medal, "FAIXA", "$tier · ${badge.tier} de 6"),
                GameLoot(Glyph.Star, "CATEGORIA", badge.category.label),
            ),
            line = when (badge.tier) {
                in 1..2 -> "Saiu quentinha da forja! Cada emblema é prova de que você está indo."
                in 3..4 -> "Essa deu trabalho de martelar! Pouca gente chega aqui."
                else -> "Lendário! Até a bigorna tremeu. Isso aqui é de quem estuda de verdade."
            },
            primary = if (last) "Continuar" else "Próximo emblema", onPrimary = { if (last) onClose() else index++ }, onClose = onClose,
            before = { }, after = { },
            delay = FORGE_REVEAL - IMPACT, stageTall = 1.1f, fxShift = -0.25f,
            backdrop = { s, _ ->
                val w = s + 30.dp
                val bleed = w * 0.34f
                FolhaScene(
                    listOf("tipo" to "emblema", "tier" to badge.tier.toString(), "categoria" to badge.id.substringBefore("-"), "faixa" to tier) + listOfNotNull(arte?.let { "arte" to it }),
                    Modifier.requiredSize(screenW, w * 1.1f + bleed * 2), stageWidthDp = w.value.toInt(), bleedDp = bleed.value.toInt(),
                )
            },
        )
    }
}
