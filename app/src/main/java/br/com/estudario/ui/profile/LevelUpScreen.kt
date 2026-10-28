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
    c1: Color, c2: Color, c3: Color, sound: String, stamp: String?,
    from: String, to: String, word: String, sub: String, loot: List<GameLoot>, line: String,
    primary: String, onPrimary: () -> Unit, onClose: () -> Unit,
    middle: (@Composable (Float) -> Unit)? = null,
    before: @Composable (androidx.compose.ui.unit.Dp) -> Unit,
    after: @Composable (androidx.compose.ui.unit.Dp) -> Unit,
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val reduced = remember { runCatching { android.provider.Settings.Global.getFloat(context.contentResolver, android.provider.Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f }.getOrDefault(false) }
    val clock = remember { Animatable(if (reduced) END else 0f) }
    LaunchedEffect(Unit) {
        if (reduced) return@LaunchedEffect
        clock.animateTo(IMPACT, tween((IMPACT * 1000).toInt(), easing = LinearEasing))
        EstudarioSom.play(context, sound)
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
                        Folha(56.dp, mood = FolhaMood.PROUD)
                        Text(
                            line,
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

// ---------------------------------------------------------------- sequência do dia

/** Os marcos da sequência (dias seguidos) que a cena mostra como próximo objetivo (os mesmos do site). */
private val STREAK_MARKS = listOf(3, 7, 14, 21, 30, 45, 60, 90, 120, 180, 270, 365)

/**
 * Dia cumprido, com cara de jogo (a mesma cena do site): a chama apagada com a conta de ontem treme e
 * estoura, a acesa cai com a moeda do dia, os dias da semana acendem um por um e entram os prêmios
 * (XP de hoje, próximo marco, recorde). Recorde novo ganha o carimbo. Uma vez por dia, ao fechar a meta.
 */
@Composable
fun StreakCelebrationScreen(celebration: StreakCelebration, onClose: () -> Unit) {
    val summary = celebration.summary
    val days = summary.current.coerceAtLeast(1)
    val best = maxOf(summary.best, days)
    val record = days > 1 && days >= best
    val next = STREAK_MARKS.firstOrNull { it > days }
    val letters = listOf("S", "T", "Q", "Q", "S", "S", "D")
    val week = summary.week.filter { !it.future }.takeLast(7).map { letters[it.date.dayOfWeek.ordinal] to it.done }
    val c1 = Color(0xFFFFE08A); val c2 = Color(0xFFFF8A3D)
    GameScene(
        c1, c2, Color(0xFFB23A0E), sound = "cena_sequencia", stamp = if (record) "RECORDE!" else null,
        from = if (days <= 1) "COMEÇOU" else "DIA ${days - 1}", to = "DIA $days", word = "SEQUÊNCIA!",
        sub = if (days == 1) "1 dia · a chama acendeu" else "$days dias seguidos estudando",
        loot = buildList {
            if (celebration.xpToday > 0) add(GameLoot(Glyph.Bolt, "XP DE HOJE", "+${celebration.xpToday} XP no dia", gold = true))
            if (next != null) add(GameLoot(Glyph.Flag, "PRÓXIMO MARCO", "$next dias · ${if (next - days == 1) "falta 1 dia" else "faltam ${next - days} dias"}"))
            add(if (record) GameLoot(Glyph.Trophy, "NOVO RECORDE", "Sua maior sequência: $days dias") else GameLoot(Glyph.Trophy, "SEU RECORDE", "$best dias · faltam ${best - days} para bater"))
        },
        line = when {
            days <= 1 -> "A chama acendeu! Volte amanhã para ela não apagar."
            record -> "Recorde! Ninguém te para. Amanhã tem mais."
            else -> "Mais um dia aceso. Faltam ${best - days} para o seu recorde de $best."
        },
        primary = "Bora continuar", onPrimary = onClose, onClose = onClose,
        // os dias da semana acendendo um por um
        middle = { t ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                week.forEachIndexed { i, (letter, done) ->
                    val p = seg(t, 1.9f + i * 0.09f, 0.42f)
                    val e = CubicBezierEasing(0.2f, 1.6f, 0.45f, 1f).transform(p)
                    val today = i == week.lastIndex
                    Column(
                        Modifier.width(40.dp).graphicsLayer { alpha = p.coerceAtMost(1f); translationY = (16f * (1f - e) - if (today && done) 3f else 0f).dp.toPx(); scaleX = 0.6f + 0.4f * e; scaleY = scaleX }
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (done) Brush.verticalGradient(listOf(c1.copy(alpha = 0.4f), c2.copy(alpha = 0.3f))) else Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.06f), Color.White.copy(alpha = 0.06f))))
                            .border(if (today && done) 2.dp else 1.dp, if (done) c1.copy(alpha = if (today) 1f else 0.7f) else Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(letter, color = if (done) Color.White else Color.White.copy(alpha = 0.55f), fontSize = 11.sp, fontWeight = FontWeight.Black)
                        Text(if (done) "✓" else "·", color = if (done) Color.White else Color.White.copy(alpha = 0.55f), fontSize = 16.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        },
        before = { s -> FlameCoin(days - 1, lit = false, size = s) },
        after = { s -> FlameCoin(days, lit = true, size = s) },
    )
}

/** A chama da sequência com a moeda do número no canto (apagada e cinza no "antes"). */
@Composable
private fun FlameCoin(n: Int, lit: Boolean, size: androidx.compose.ui.unit.Dp) {
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        StreakFlame(lit = lit, size = size * 0.9f, glow = lit)
        val coin = size * 0.32f
        Box(
            Modifier.align(Alignment.BottomEnd).padding(end = size * 0.06f, bottom = size * 0.06f).size(coin).clip(CircleShape)
                .background(if (lit) Brush.verticalGradient(listOf(Color(0xFFFFF3B8), Color(0xFFFFC22E), Color(0xFFFF8A3D))) else Brush.verticalGradient(listOf(Color(0xFFE6E3F5), Color(0xFFA9A5C6))))
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center,
        ) { Text("$n", color = if (lit) Color(0xFF4A1800) else Color(0xFF2A2440), fontWeight = FontWeight.Black, fontSize = (coin.value * 0.45f).sp) }
    }
}

// ---------------------------------------------------------------- emblema novo

private fun faixaLabel(tier: Int) = when (tier) {
    1 -> "Bronze"; 2 -> "Prata"; 3 -> "Ouro"; 4 -> "Safira"; 5 -> "Rubi"; else -> "Esmeralda"
}

/**
 * Emblema novo, com cara de jogo (a mesma cena do site): a silhueta escura do emblema (a arte de
 * drawable-nodpi/emblema_<id>) treme e estoura, ele cai colorido com a faixa carimbada, e entram o que
 * a pessoa fez, a faixa e a categoria. Quando cai mais de um de uma vez, cada um ganha a sua cena.
 */
@Composable
fun BadgeCelebrationScreen(badges: List<br.com.estudario.domain.Badge>, onClose: () -> Unit) {
    if (badges.isEmpty()) return
    var index by remember(badges) { androidx.compose.runtime.mutableIntStateOf(0) }
    val badge = badges.getOrNull(index) ?: return
    val last = index >= badges.lastIndex
    val tier = faixaLabel(badge.tier)
    val rank = LevelCrest.ranks.first { it.metal == tier }
    androidx.compose.runtime.key(index) {
        GameScene(
            hex(rank.light), hex(rank.mid), hex(rank.dark), sound = "cena_emblema", stamp = tier.uppercase() + "!",
            from = "BLOQUEADO", to = "DESBLOQUEADO", word = "CONQUISTA!",
            sub = "${badge.name} · emblema $tier" + if (badges.size > 1) "  (${index + 1}/${badges.size})" else "",
            loot = listOf(
                GameLoot(Glyph.Trophy, "O QUE VOCÊ FEZ", badge.requirement, gold = true),
                GameLoot(Glyph.Medal, "FAIXA", "$tier · ${badge.tier} de 6"),
                GameLoot(Glyph.Star, "CATEGORIA", badge.category.label),
            ),
            line = when (badge.tier) {
                in 1..2 -> "Primeiro de muitos! Cada emblema é prova de que você está indo."
                in 3..4 -> "Esse é pesado! Pouca gente chega aqui."
                else -> "Lendário! Isso aqui é de quem estuda de verdade."
            },
            primary = if (last) "Continuar" else "Próximo emblema", onPrimary = { if (last) onClose() else index++ }, onClose = onClose,
            before = { s -> BadgeSilhouette(badge, s) },
            after = { s -> MedalArt(badge, earned = true, size = s) },
        )
    }
}

/** A silhueta escura do emblema, com "?" (o "antes" da cena). */
@Composable
private fun BadgeSilhouette(badge: br.com.estudario.domain.Badge, size: androidx.compose.ui.unit.Dp) {
    val ctx = LocalContext.current
    val res = remember(badge.id) { ctx.resources.getIdentifier("emblema_" + badge.id.replace('-', '_'), "drawable", ctx.packageName) }
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        if (res != 0) androidx.compose.foundation.Image(
            androidx.compose.ui.res.painterResource(res), contentDescription = null, modifier = Modifier.size(size), alpha = 0.75f,
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black),
        ) else MedalArt(badge, earned = false, size = size)
        Text("?", color = Color.White.copy(alpha = 0.55f), fontSize = (size.value * 0.28f).sp, fontWeight = FontWeight.Black)
    }
}
