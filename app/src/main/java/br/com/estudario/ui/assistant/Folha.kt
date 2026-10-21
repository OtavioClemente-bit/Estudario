package br.com.estudario.ui.assistant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Como o Folha está: muda olhos, boca, sobrancelhas, braços e o que acontece em volta dele.
 * O Folha 2.0 é o mesmo do site (web/src/jsMain/resources/folha-live.js): mesma grade, mesmas poses.
 */
enum class FolhaMood {
    /** Esperando: respira, pisca, olha em volta e, de vez em quando, faz alguma coisa sozinho. */
    IDLE,
    /** Gerando: lê as próprias páginas, escreve e folheia com as mãos. */
    THINKING,
    /** Conversando: gesticula e a boca acompanha as letras da fala. */
    TALKING,
    /** Pronto: olhos em meia-lua, punhos para o alto, pulinhos e confete. */
    HAPPY,
    /** Deu errado: sobrancelha que sobe por dentro, pálpebra caída, beicinho e lágrima. */
    SAD,
    /** Chegando ou saindo: mão aberta lá no alto, acenando. */
    WAVE,
    /** Mostrando algo do lado. */
    POINT,
    /** Piscadela cúmplice. */
    WINK,
    /** Susto bom: olhos arregalados, boca de "Oh!", mãos nas bochechas. */
    SURPRISED,
    /** Orgulhoso: mãos na cintura, sorriso de lado, brilhinhos. */
    PROUD,
    /** Estudando concentrado: escreve com lápis, língua no canto da boca. */
    FOCUS,
    /** Cochilando: olhos fechados, "zZ" subindo. */
    SLEEPY,
    /** Tonto (muitos toques seguidos): olhos em espiral, estrelinhas girando. */
    DIZZY,
    /** "Ufa!" (acertou logo depois de errar): passa a mão na testa, solta o ar e relaxa num sorriso. */
    RELIEVED,
    /** Em chamas (muitos acertos seguidos): olhos de estrela, pulos altos, labaredas. */
    ECSTATIC,
}

/**
 * O visual do Folha no app inteiro: o que a pessoa escolheu no armário (FolhaWardrobe) e o contexto
 * do dia. O app mantém em dia (FolhaLookSync); todos os Folhas leem daqui a cada quadro.
 */
object FolhaLook {
    var cover by mutableStateOf("indigo")
    var ribbon by mutableStateOf("menta")
    var acc by mutableStateOf("nenhum")
    /** Dias seguidos: medalha no capelo a partir de 7 (bronze), 30 (prata) e 100 (ouro). */
    var streak by mutableIntStateOf(0)
    /** A sequência pode cair hoje (fim do dia, meta não batida): parado, ele fica preocupadinho. */
    var atRisk by mutableStateOf(false)
    /** Só para testes: força a hora do dia (manhã tem café, noite tem touca). */
    var hour by mutableStateOf<Int?>(null)
}

/** Vitrine do armário: veste itens só neste Folha; [plain] tira medalha, café e touca. */
data class FolhaStyle(val cover: String? = null, val ribbon: String? = null, val acc: String? = null, val plain: Boolean = false)

/** Capa do livro: degradê (claro, meio, escuro), sombra de baixo e o metal das cantoneiras. */
private class CoverStyle(val c: List<Color>, val under: Color, val silver: Boolean = false, val stitch: Boolean = false, val stars: Boolean = false, val shine: Boolean = false)

private val COVERS = mapOf(
    "indigo" to CoverStyle(listOf(Color(0xFF5247E0), Color(0xFF3A2FC4), Color(0xFF2A209E)), Color(0xFF1A1370)),
    "esmeralda" to CoverStyle(listOf(Color(0xFF36C897), Color(0xFF1F9E75), Color(0xFF147A5A)), Color(0xFF0B4F3A)),
    "rubi" to CoverStyle(listOf(Color(0xFFE8566D), Color(0xFFC42F4B), Color(0xFF961D37)), Color(0xFF5E0F22)),
    "ametista" to CoverStyle(listOf(Color(0xFFAA68EE), Color(0xFF8746D3), Color(0xFF662DAB)), Color(0xFF3E1670)),
    "couro" to CoverStyle(listOf(Color(0xFFBC7D4E), Color(0xFF965A31), Color(0xFF6D3D1F)), Color(0xFF43240E), stitch = true),
    "grafite" to CoverStyle(listOf(Color(0xFF61667C), Color(0xFF43475C), Color(0xFF2D3042)), Color(0xFF1A1C28), silver = true),
    "noite" to CoverStyle(listOf(Color(0xFF36388A), Color(0xFF212059), Color(0xFF121138)), Color(0xFF07061E), silver = true, stars = true),
    "ouro" to CoverStyle(listOf(Color(0xFFFFDD84), Color(0xFFEBB13A), Color(0xFFBD8214)), Color(0xFF7A4F05), silver = true, shine = true),
)

private class Look(val cover: CoverStyle, val ribbon: List<Color>, val acc: String, val streak: Int, val atRisk: Boolean, val morning: Boolean, val night: Boolean)

private fun resolveLook(o: FolhaStyle?): Look {
    val h = FolhaLook.hour ?: java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val plain = o?.plain == true
    return Look(
        cover = COVERS[o?.cover ?: FolhaLook.cover] ?: COVERS.getValue("indigo"),
        ribbon = ribbonColors(o?.ribbon ?: FolhaLook.ribbon),
        acc = o?.acc ?: FolhaLook.acc,
        streak = if (plain) 0 else FolhaLook.streak,
        atRisk = !plain && FolhaLook.atRisk,
        morning = !plain && h in 5..10,
        night = !plain && (h >= 22 || h < 5),
    )
}

private val Ink = Color(0xFF26215C)
private val PageLine = Color(0xFFD8D4E6)
private val Cap = Color(0xFF231C6B)
private val CapTop = Color(0xFF2F2789)
private val Arm = Color(0xFF3326CE)
private val ArmLight = Color(0xFF6158F0)
private val Gold = Color(0xFFF5B83D)
private val Mint = Color(0xFF7EE0B8)
private val MintDark = Color(0xFF3FBF8F)
private val Cheek = Color(0xFFF4A6C0)
private val Spark = Color(0xFFFFE07A)
private val Paper = Color(0xFFFFFDF8)
private val PaperMid = Color(0xFFF7F4EE)
private val PaperEdge = Color(0xFFE9E4DA)
private val PaperEdgeDark = Color(0xFFCFC8BC)
private val Gutter = Color(0xFF3A3170)

private fun ribbonColors(id: String): List<Color> = when (id) {
    "ceu" -> listOf(Color(0xFF3D8BEA), Color(0xFF6FB0FF), Color(0xFFD6EAFF))
    "coral" -> listOf(Color(0xFFE2604A), Color(0xFFFF8C75), Color(0xFFFFD6CC))
    "lilas" -> listOf(Color(0xFF9466DD), Color(0xFFB995F5), Color(0xFFECDFFF))
    "dourada" -> listOf(Color(0xFFC98A12), Gold, Color(0xFFFFEBB5))
    else -> listOf(MintDark, Mint, Color(0xFFC6F7E2))
}

/** Abaixo disso o desenho simplifica (some o detalhe miúdo, os traços engrossam). */
private val SMALL = 72.dp

/**
 * O Folha, a cara do Assistente Estudário: o livro da marca, vivo, de capelo de formatura.
 *
 * Cada humor é uma pose; ele vai de uma para a outra por molas (com antecipação e um pouquinho de
 * passagem do ponto). Pisca em intervalos irregulares, olha em volta, faz manias quando parado,
 * cochila se fica muito tempo parado, e fica tonto com muitos toques seguidos. Um toque faz ele
 * pular e piscar um olho, e chama [onClick] (a ação do lugar onde ele está, como gerar).
 * [say]: o texto que ele está falando (a boca segue as letras); [gulp]: 0 a 1, uma folha chegando.
 */
@Composable
fun Folha(
    size: Dp,
    modifier: Modifier = Modifier,
    mood: FolhaMood = FolhaMood.IDLE,
    onClick: (() -> Unit)? = null,
    /** 0 a 1: uma folha acabou de chegar até ele (a cena de processamento avisa); ele "engole" e brilha. */
    gulp: Float = 0f,
    say: String? = null,
    sayMillis: Long = 0L,
    /** Vitrine do armário: veste itens só neste Folha. */
    style: FolhaStyle? = null,
    /** Chega pulando (cai de leve do alto e amassa ao pousar). */
    enter: Boolean = true,
    /** Saindo: acena e sobe sumindo (quem fecha a tela espera [FOLHA_LEAVE_MS]). */
    leaving: Boolean = false,
) {
    val sim = remember { FolhaSim().also { it.enterPending = enter } }
    var frame by remember { mutableLongStateOf(0L) }
    sim.small = size < SMALL
    sim.setMood(mood)
    sim.setLeaving(leaving)
    LaunchedEffect(say) { sim.setSay(say, sayMillis) }
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) withFrameNanos { now ->
            val dt = if (last == 0L) 0f else ((now - last) / 1e9f).coerceAtMost(0.05f)
            last = now
            sim.step(now / 1_000_000L, dt)
            frame = now
        }
    }
    val clickable = if (onClick != null) Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
    ) {
        sim.poke()
        onClick()
    } else Modifier

    Canvas(
        modifier
            .size(size)
            .semantics { contentDescription = "Folha, do Estudário" }
            .then(clickable),
    ) {
        frame.let { }   // redesenha a cada quadro
        val f = sim.snapshot(gulp, resolveLook(style))
        if (f.fade >= 0.999f && f.rise == 0f) drawFolha(f)
        else translate(0f, -f.rise * this.size.height) {
            drawContext.canvas.saveLayer(Rect(-this.size.width, -this.size.height, this.size.width * 2, this.size.height * 2), androidx.compose.ui.graphics.Paint().apply { alpha = f.fade })
            drawFolha(f)
            drawContext.canvas.restore()
        }
    }
}

/** Quanto dura a saída do Folha (tchau + sumir subindo). */
const val FOLHA_LEAVE_MS = 760L

/**
 * O Folha que fala ao aparecer e depois fica ouvindo. A boca segue as letras de [text], no tempo de
 * ler (mais ou menos) em voz alta.
 */
@Composable
fun FolhaTalking(size: Dp, text: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    val total = (text.length * 45L).coerceIn(900L, 3200L)
    var talking by remember(text) { mutableStateOf(true) }
    LaunchedEffect(text) { delay(total); talking = false }
    Folha(size, modifier, if (talking) FolhaMood.TALKING else FolhaMood.IDLE, onClick, say = if (talking) text else null, sayMillis = total)
}

/**
 * O Folha como imagem, para o ícone grande das notificações (fora do Compose). O ícone pequeno da
 * barra continua monocromático, como o Android exige; este aparece colorido ao lado do texto.
 */
fun folhaBitmap(sizePx: Int, mood: FolhaMood = FolhaMood.HAPPY, style: FolhaStyle = FolhaStyle(plain = true)): android.graphics.Bitmap {
    val image = androidx.compose.ui.graphics.ImageBitmap(sizePx, sizePx)
    val canvas = androidx.compose.ui.graphics.Canvas(image)
    androidx.compose.ui.graphics.drawscope.CanvasDrawScope().draw(
        androidx.compose.ui.unit.Density(1f),
        androidx.compose.ui.unit.LayoutDirection.Ltr,
        canvas,
        Size(sizePx.toFloat(), sizePx.toFloat()),
    ) {
        // Um quadro "bonito": olhos abertos, borla no meio do balanço; cabe a mão para o alto.
        val m = if (mood == FolhaMood.THINKING) FolhaMood.IDLE else mood
        val sim = FolhaSim().apply { small = sizePx < 160; setMood(m); settle(m) }
        val fit = 0.9f
        translate(size.width * (1 - fit) / 2f, size.height * (1 - fit) / 2f + size.width * 0.02f) {
            scale(fit, fit, pivot = Offset.Zero) { drawFolha(sim.snapshot(0f, resolveLook(style))) }
        }
    }
    return image.asAndroidBitmap()
}

// ===================================================================== pose e molas

private fun clamp(v: Float, lo: Float, hi: Float) = v.coerceIn(lo, hi)
private fun lerp(a: Float, b: Float, f: Float) = a + (b - a) * f
private fun sm(x: Float): Float { val c = clamp(x, 0f, 1f); return c * c * (3 - 2 * c) }
private const val PIf = PI.toFloat()

private enum class Hand { MITT, OPEN, FIST, POINT, PEN }

/** Tudo o que vai por mola de uma pose para outra (unidades da grade 100 x 100). */
private class PoseValues {
    var hop = 0f; var tilt = 0f
    var hL0 = 4f; var hL1 = 78f; var hR0 = 96f; var hR1 = 78f
    var rotL = 0f; var rotR = 0f; var bendL = 2f; var bendR = 2f
    var up = 0f; var low = 0f; var droop = 0f; var eyeScale = 1f; var pupil = 1f; var lookX = 0f; var lookY = 0f
    var browY = 0f; var browIn = 0f; var browR = 0f
    var mW = 5f; var mCurve = 3.4f; var mOpen = 0f; var cheek = 0.45f; var capRot = 0f; var tongue = 0f
    var shapeL = Hand.MITT; var shapeR = Hand.MITT

    fun values() = floatArrayOf(hop, tilt, hL0, hL1, hR0, hR1, rotL, rotR, bendL, bendR, up, low, droop, eyeScale, pupil, lookX, lookY, browY, browIn, browR, mW, mCurve, mOpen, cheek, capRot, tongue)
    fun set(v: FloatArray) {
        hop = v[0]; tilt = v[1]; hL0 = v[2]; hL1 = v[3]; hR0 = v[4]; hR1 = v[5]; rotL = v[6]; rotR = v[7]; bendL = v[8]; bendR = v[9]
        up = v[10]; low = v[11]; droop = v[12]; eyeScale = v[13]; pupil = v[14]; lookX = v[15]; lookY = v[16]; browY = v[17]; browIn = v[18]; browR = v[19]
        mW = v[20]; mCurve = v[21]; mOpen = v[22]; cheek = v[23]; capRot = v[24]; tongue = v[25]
    }
    companion object {
        const val N = 26
        // índices que vão rápido (corpo e olhar) e devagar (braços)
        val FAST = setOf(0, 1, 15, 16)
        val SLOW = setOf(2, 3, 4, 5, 6, 7, 8, 9)
    }
}

private class Fidget(val kind: String, val start: Long, val dur: Long, val side: Float)

private const val TURN_SHARE = 0.32f
private const val TURN_CYCLE = 2.4f

private class Turn(val cycle: Float, val raw: Float, val p: Float, val c: Float, val lift: Float, val edgeX: Float, val up: Float, val turning: Boolean)
private fun turnState(t: Float): Turn {
    val cycle = (t % TURN_CYCLE) / TURN_CYCLE
    val raw = clamp(cycle / TURN_SHARE, 0f, 1f)
    val p = raw * raw * (3 - 2 * raw)
    val c = cos(PIf * p); val lift = sin(PIf * p)
    return Turn(cycle, raw, p, c, lift, 50f + 38f * c, lift * 6f, raw > 0f && raw < 1f)
}

/** Pose alvo do humor no instante [t]. */
/** [tm]: segundos desde que o humor começou (cenas com começo, meio e fim); [worried]: sequência em risco. */
private fun pose(mood: FolhaMood, t: Float, lookX: Float, lookY: Float, fid: Fidget?, fu: Float, tm: Float = 9f, worried: Boolean = false): PoseValues {
    val p = PoseValues()
    p.hop = sin(t * 2.1f) * 1.4f; p.tilt = sin(t * 0.9f) * 1.2f
    p.hL1 = 78f + sin(t * 2.1f) * 1.2f; p.hR1 = 78f - sin(t * 2.1f) * 1.2f
    p.lookX = lookX; p.lookY = lookY
    fun env(u: Float) = sin(PIf * clamp(u, 0f, 1f)).pow(0.7f)
    if (mood == FolhaMood.IDLE && worried) { p.browIn = 0.6f; p.browY = 0.3f; p.mW = 3.8f; p.mCurve = 0.6f; p.cheek = 0.35f }
    if (mood == FolhaMood.IDLE && fid != null) {
        val e = env(fu)
        when (fid.kind) {
            "cap" -> {   // ajeita o capelo
                p.hR0 = lerp(p.hR0, 74f, e); p.hR1 = lerp(p.hR1, 34f, e); if (e > 0.3f) p.shapeR = Hand.OPEN; p.rotR = -40f * e; p.bendR = lerp(2f, -6f, e)
                p.capRot = 7f * sin(PIf * clamp((fu - 0.3f) / 0.5f, 0f, 1f)); p.lookX = 0.7f * e; p.lookY = -0.6f * e; p.browR = 1.2f * e
            }
            "yawn" -> {  // espreguiça e boceja
                p.hL0 = lerp(p.hL0, 8f, e); p.hL1 = lerp(p.hL1, 40f, e); p.hR0 = lerp(p.hR0, 92f, e); p.hR1 = lerp(p.hR1, 40f, e)
                if (e > 0.3f) { p.shapeL = Hand.FIST; p.shapeR = Hand.FIST }; p.bendL = -5f * e; p.bendR = -5f * e; p.hop = -1.6f * e
                p.up = 0.95f * clamp((fu - 0.2f) / 0.3f, 0f, 1f) * (1 - clamp((fu - 0.8f) / 0.15f, 0f, 1f))
                p.mOpen = 1.1f * e; p.mW = lerp(5f, 3.2f, e); p.mCurve = lerp(3.4f, 0.4f, e); p.browY = -1.6f * e
            }
            "glance" -> { p.lookX = fid.side * e; p.lookY = -0.1f; p.up = 0.25f * e; p.browR = if (fid.side > 0) 1.4f * e else 0f; p.browY = -0.4f * e; p.mW = lerp(5f, 3.6f, e); p.mCurve = lerp(3.4f, 1f, e) }
            "bounce" -> {
                p.hop = -3.2f * abs(sin(PIf * fu * 2)); p.low = 0.45f * e; p.mOpen = 0.6f * e; p.mW = 5.6f; p.mCurve = 4.4f; p.cheek = lerp(0.45f, 0.7f, e)
                p.hL0 = lerp(p.hL0, 4f, e); p.hL1 = lerp(p.hL1, 58f, e); p.hR0 = lerp(p.hR0, 96f, e); p.hR1 = lerp(p.hR1, 58f, e)
            }
        }
    }
    when (mood) {
        FolhaMood.HAPPY -> {
            val pump = abs(sin(t * 5.2f)) * 4f
            p.hop = -abs(sin(t * 5.2f)) * 5f; p.tilt = sin(t * 2.6f) * 2f
            p.hL0 = 2f; p.hL1 = 46f - pump; p.hR0 = 98f; p.hR1 = 46f - pump; p.shapeL = Hand.FIST; p.shapeR = Hand.FIST; p.rotL = -18f; p.rotR = 18f; p.bendL = -4f; p.bendR = -4f
            p.low = 0.64f; p.pupil = 1.25f; p.browY = -2.2f; p.mW = 6.4f; p.mCurve = 5f; p.mOpen = 1f; p.cheek = 0.75f
        }
        FolhaMood.WAVE -> {
            val wv = sin(t * 13f)
            p.hR0 = 100f + wv * 2f; p.hR1 = 30f + wv * 1.2f; p.shapeR = Hand.OPEN; p.rotR = -10f + wv * 26f; p.bendR = -5f
            p.low = 0.32f; p.browY = -1.4f; p.mW = 5.6f; p.mCurve = 4.4f; p.mOpen = 0.55f; p.cheek = 0.6f; p.tilt = -1.5f + wv * 0.6f
        }
        FolhaMood.POINT -> {
            p.hR0 = 106f; p.hR1 = 60f + sin(t * 4f) * 1.2f; p.shapeR = Hand.POINT; p.rotR = -6f; p.bendR = -2f; p.tilt = -2f
            p.lookX = 1f; p.lookY = 0f; p.browY = -1.2f; p.browR = 1.2f; p.mW = 5f; p.mCurve = 4f; p.mOpen = 0.25f
        }
        FolhaMood.TALKING -> {
            val wv = sin(t * 7f) * 5f; val beat = sin(t * 3.4f)
            p.tilt = beat * 3f; p.hL0 = 3f; p.hL1 = 76f; p.hR0 = 97f + wv * 0.3f; p.hR1 = 52f + wv; p.shapeR = Hand.OPEN; p.rotR = beat * 18f; p.bendR = -3f
            p.browY = -0.8f * maxOf(0f, sin(t * 2.3f)); p.mW = 4.6f; p.mCurve = 2.4f
        }
        FolhaMood.THINKING -> {
            p.tilt = sin(t * 1.3f) * 2f; p.hL0 = 4f; p.hL1 = 76f + sin(t * 1.5f); p.hR0 = 93f; p.hR1 = 70f + sin(t * 2.2f) * 1.5f; p.shapeR = Hand.FIST; p.bendR = 3f
            p.up = 0.15f; p.browR = 1.8f; p.browY = -0.4f; p.mW = 3.4f; p.mCurve = 1.2f; p.mOpen = 0f
            // folheando: a mão direita pega o canto da página e empurra; a esquerda recebe do outro lado
            val tt = turnState(t)
            val grab = if (tt.cycle > 0.86f) sm((tt.cycle - 0.86f) / 0.14f) else if (tt.turning) 1f - sm((tt.p - 0.5f) / 0.22f) else 0f
            if (grab > 0.001f) {
                val ex = if (tt.turning) tt.edgeX else 88f; val ey = 80f - (if (tt.turning) tt.up * 0.8f else 0f)
                p.hR0 = lerp(p.hR0, ex + 1.5f, grab); p.hR1 = lerp(p.hR1, ey, grab); p.shapeR = Hand.OPEN; p.rotR = lerp(0f, 150f, grab); p.bendR = lerp(3f, -6f, grab)
            }
            val catchK = if (tt.turning) sm((tt.p - 0.62f) / 0.2f) else 0f
            if (catchK > 0.001f) { p.hL0 = lerp(p.hL0, tt.edgeX - 1.5f, catchK); p.hL1 = lerp(p.hL1, 80f - tt.up * 0.8f, catchK); p.shapeL = Hand.OPEN; p.rotL = lerp(0f, -150f, catchK); p.bendL = -6f }
            if (tt.turning) { p.lookX = clamp((tt.edgeX - 50f) / 30f, -1f, 1f); p.lookY = 0.4f; p.mOpen = 0.3f * tt.lift; p.mW = 3f + tt.lift; p.up = 0f; p.browR = 0.6f }
        }
        FolhaMood.SURPRISED -> {
            p.hop = -2f; p.hL0 = 18f; p.hL1 = 72f; p.hR0 = 82f; p.hR1 = 72f; p.shapeL = Hand.OPEN; p.shapeR = Hand.OPEN; p.rotL = 60f; p.rotR = -60f; p.bendL = -4f; p.bendR = -4f
            p.eyeScale = 1.22f; p.pupil = 1.45f; p.browY = -3.6f; p.mOpen = 0.9f; p.mW = 3.2f; p.mCurve = 0f; p.cheek = 0.55f
        }
        FolhaMood.PROUD -> {
            p.hop = -1.2f + sin(t * 2f) * 0.5f; p.tilt = -2f + sin(t * 0.9f) * 0.6f
            p.hL0 = 9f; p.hL1 = 74f; p.hR0 = 91f; p.hR1 = 74f; p.shapeL = Hand.FIST; p.shapeR = Hand.FIST; p.rotL = 150f; p.rotR = -150f; p.bendL = 6f; p.bendR = 6f
            p.up = 0.38f; p.browY = -1.8f; p.browR = 0.8f; p.mW = 5.6f; p.mCurve = 4.4f; p.cheek = 0.65f; p.lookX = -0.2f; p.lookY = -0.4f
        }
        FolhaMood.FOCUS -> {
            val scr = sin(t * 9f); val sc2 = cos(t * 4.6f)
            p.tilt = 1.5f; p.hop = 0.6f + sin(t * 1.6f) * 0.4f
            p.hR0 = 66f + scr * 4.5f + sin(t * 0.9f) * 3f; p.hR1 = 76f + sc2 * 0.9f; p.shapeR = Hand.PEN; p.rotR = 0f; p.bendR = -4f
            p.up = 0.28f; p.lookX = 0.55f + scr * 0.15f; p.lookY = 0.85f; p.browIn = -0.55f; p.browY = 0.8f
            p.mW = 2.8f; p.mCurve = 0.4f; p.tongue = 1f
        }
        FolhaMood.SLEEPY -> {
            p.hop = 1.4f + sin(t * 1.1f) * 1.2f; p.tilt = 3.5f + sin(t * 0.55f) * 1.2f
            p.hL0 = 7f; p.hL1 = 84f; p.hR0 = 93f; p.hR1 = 84f; p.up = 1f; p.browY = 1f; p.mOpen = 0.18f + 0.12f * (0.5f + 0.5f * sin(t * 1.1f)); p.mW = 2.4f; p.mCurve = 0f; p.cheek = 0.35f
        }
        FolhaMood.DIZZY -> {
            p.tilt = sin(t * 6f) * 7f; p.hop = sin(t * 3f) * 1.2f; p.hL0 = 5f + sin(t * 6f) * 3f; p.hL1 = 72f; p.hR0 = 95f + sin(t * 6f + 1f) * 3f; p.hR1 = 72f
            p.mW = 4.4f; p.mCurve = 0f; p.mOpen = 0f; p.browY = -1f; p.cheek = 0.6f
        }
        FolhaMood.SAD -> {
            p.hop = 1.6f + sin(t * 1.4f) * 0.6f; p.tilt = sin(t * 0.8f) * 0.8f
            p.hL0 = 6f; p.hL1 = 85f; p.hR0 = 94f; p.hR1 = 85f; p.rotL = 30f; p.rotR = -30f; p.bendL = 4f; p.bendR = 4f
            p.up = 0.42f; p.droop = 1f; p.pupil = 0.9f; p.lookY = 0.9f
            p.browIn = 1f; p.browY = 0.6f; p.mW = 4.6f; p.mCurve = -3.4f; p.cheek = 0.3f
        }
        FolhaMood.RELIEVED -> {
            // "Ufa!": de olhos fechados, passa a mão na testa e solta o ar; depois relaxa num sorriso
            val w = 1f - sm((tm - 2.1f) / 0.6f); val sw = 0.5f + 0.5f * sin(tm * 4.4f - 1.2f)
            p.hop = lerp(-0.6f, 1.4f, w) + sin(t * 2f) * 0.4f; p.tilt = lerp(-1.5f, 2.5f, w)
            p.hR0 = lerp(96f, lerp(80f, 60f, sw), w); p.hR1 = lerp(76f, 50f, w); p.shapeR = if (w > 0.4f) Hand.OPEN else Hand.MITT; p.rotR = -95f * w; p.bendR = lerp(2f, -7f, w)
            p.hL0 = lerp(5f, 8f, w); p.hL1 = lerp(76f, 82f, w)
            p.up = w; p.low = lerp(0.5f, 0f, w); p.browIn = 0.45f * w; p.browY = lerp(-1.2f, -0.4f, w)
            p.mW = lerp(5.4f, 2.3f, w); p.mCurve = lerp(4.2f, 0f, w); p.mOpen = lerp(0.3f, 0.45f, w); p.cheek = lerp(0.65f, 0.55f, w)
        }
        FolhaMood.ECSTATIC -> {
            // em chamas: olhos de estrela, pulos altos e socos no ar alternados
            val a = sin(t * 6.2f)
            p.hop = -abs(a) * 7f; p.tilt = sin(t * 3.1f) * 3f
            p.hL0 = 0f; p.hL1 = 40f - maxOf(0f, a) * 7f; p.hR0 = 100f; p.hR1 = 40f - maxOf(0f, -a) * 7f
            p.shapeL = Hand.FIST; p.shapeR = Hand.FIST; p.rotL = -24f; p.rotR = 24f; p.bendL = -6f; p.bendR = -6f
            p.pupil = 1.3f; p.browY = -3f; p.mW = 6.8f; p.mCurve = 5.4f; p.mOpen = 1.15f; p.cheek = 0.85f
        }
        else -> {}
    }
    return p
}

/** Fala: a boca segue a letra atual (vogal abre, m/b/p fecha, pausa fecha). */
private fun mouthFor(ch: Char?): Pair<Float, Float> {
    val c = ch?.lowercaseChar() ?: return 0f to 0f
    return when {
        c in "aáàâã" -> 1f to 0f
        c in "oóôõ" -> 0.8f to 0.8f
        c in "uú" -> 0.45f to 1f
        c in "eéêiíy" -> 0.55f to 0f
        c in "mbp" -> 0f to 0f
        c.isLetter() -> 0.28f to 0.1f
        c == ' ' -> 0.12f to 0f
        else -> 0f to 0f
    }
}

/** O estado vivo do Folha: molas, piscadas, olhar, manias, toques e fala. */
private class FolhaSim {
    var small = false
    private var mood = FolhaMood.IDLE
    private var shown = FolhaMood.IDLE
    private var t = 0f
    private var nowMs = 0L
    private val x = FloatArray(PoseValues.N)
    private val v = FloatArray(PoseValues.N)
    private var inited = false
    private var shapeL = Hand.MITT; private var shapeR = Hand.MITT
    private var popL = 0f; private var popR = 0f
    private var sq = 0f; private var sqV = 0f
    private var prevVy = 0f
    private var blink = 0f; private val blinkPlan = ArrayList<Long>(); private var nextBlink = 0L
    private var lookX = 0f; private var lookY = 0f; private var lookFx = 0f; private var lookFy = 0f; private var lookTx = 0f; private var lookTy = 0f
    private var lookFrom = 0L; private var lookDur = 1L; private var lookNext = 0L
    private var bounceAt = -1L; private var wink = false; private var winkUntil = 0L
    private val taps = ArrayList<Long>(); private var dizzyUntil = 0L
    private var idleSince = 0L
    private var fid: Fidget? = null; private var nextFid = 0L; private var lastFid: String? = null
    private var sayText: String? = null; private var sayStart = 0L; private var sayTimes = LongArray(0); private var sayEnd = 0L
    private var mouthOpen = 0f; private var mouthRound = 0f; private var speaking = false
    private var moodAt = 0L
    var worried = false
    /** Entrada: na primeira pose, começa do alto e cai (a mola e o amasso de aterrissagem fazem o resto). */
    var enterPending = false
    private var leaveAt = -1L

    fun setLeaving(on: Boolean) { if (on && leaveAt < 0) leaveAt = nowMs else if (!on) leaveAt = -1L }

    fun setMood(m: FolhaMood) { if (m != mood) { mood = m; idleSince = nowMs } }

    fun setSay(text: String?, totalMs: Long) {
        sayText = text; sayStart = nowMs
        if (text == null) return
        // mesmo ritmo do site, esticado para durar o tempo pedido
        val raw = LongArray(text.length); var at = 0L
        text.forEachIndexed { i, ch -> raw[i] = at; at += when (ch) { '.', '!', '?', ':' -> 220L; ',' -> 110L; ' ' -> 18L; else -> 16L } }
        val k = if (totalMs > 0 && at > 0) totalMs.toFloat() / at else 1f
        sayTimes = LongArray(text.length) { (raw[it] * k).toLong() }; sayEnd = (at * k).toLong()
    }

    fun poke() {
        bounceAt = nowMs; wink = true; winkUntil = nowMs + 710; idleSince = nowMs
        taps.removeAll { nowMs - it > 1400 }; taps.add(nowMs)
        if (taps.size >= 4) { dizzyUntil = nowMs + 2200; taps.clear() }
    }

    /** Para imagens paradas: pula as molas direto para a pose. */
    fun settle(m: FolhaMood) {
        t = 0.35f; lookX = 0.3f; lookY = -0.2f; moodAt = -9000L
        val p = pose(m, t, lookX, lookY, null, 0f)
        p.values().copyInto(x); shapeL = p.shapeL; shapeR = p.shapeR; shown = m; inited = true
    }

    fun step(now: Long, dt: Float) {
        if (!inited) { nowMs = now; idleSince = now; nextBlink = now + 1800; nextFid = now + 6000 + Random.nextLong(5000) }
        nowMs = now; t += dt
        var m = mood
        if (leaveAt >= 0) m = FolhaMood.WAVE
        else if (now < dizzyUntil) m = FolhaMood.DIZZY
        else if (mood == FolhaMood.IDLE && now - idleSince > 40_000) m = FolhaMood.SLEEPY
        if (shown == FolhaMood.SLEEPY && m != FolhaMood.SLEEPY) bounceAt = now        // acorda num susto
        if (m != shown && inited) { sqV += 9f; v[3] += 40f; v[5] += 40f }              // antecipação
        if (m != shown || !inited) moodAt = now
        shown = m
        if (wink && now > winkUntil) wink = false
        updateBlink(now); updateLook(now, m)
        val f = fidget(now, m)
        val p = pose(m, t, lookX, lookY, f, if (f != null) (now - f.start).toFloat() / f.dur else 0f, (now - moodAt) / 1000f, worried)
        val target = p.values()
        if (!inited) {
            target.copyInto(x); shapeL = p.shapeL; shapeR = p.shapeR; inited = true
            if (enterPending) { enterPending = false; x[0] = -16f; x[3] -= 10f; x[5] -= 10f }
        }
        if (p.shapeL != shapeL) { shapeL = p.shapeL; popL = 1f }
        if (p.shapeR != shapeR) { shapeR = p.shapeR; popR = 1f }
        popL = maxOf(0f, popL - dt * 4f); popR = maxOf(0f, popR - dt * 4f)
        if (dt > 0f) {
            val steps = ceil(dt / (1f / 120f)).toInt().coerceAtLeast(1); val h = dt / steps
            val handK = when (m) { FolhaMood.THINKING -> 700f; FolhaMood.FOCUS -> 600f; else -> 120f }
            repeat(steps) {
                for (i in 0 until PoseValues.N) {
                    val k = if (i in PoseValues.FAST) 420f else if (i in PoseValues.SLOW) handK else 200f
                    val a = k * (target[i] - x[i]) - 2f * 0.62f * sqrt(k) * v[i]
                    v[i] += a * h; x[i] += v[i] * h
                }
                val a = 260f * (0f - sq) - 2f * 0.62f * sqrt(260f) * sqV
                sqV += a * h; sq += sqV * h
            }
            val vy = v[0]
            if (prevVy > 18f && vy < prevVy * 0.4f && x[0] > -1f) sqV += prevVy * 0.12f    // amasso de aterrissagem
            prevVy = vy
        }
        // fala
        speaking = sayText != null && m == FolhaMood.TALKING
        if (speaking) {
            val el = now - sayStart; val text = sayText!!
            var ch: Char? = null
            if (el < sayEnd && text.isNotEmpty()) { var i = sayTimes.indexOfLast { it <= el }; if (i < 0) i = 0; ch = text[i] }
            val (o, r) = mouthFor(ch)
            val k = if (dt > 0f) 1f - exp(-dt * 28f) else 1f
            mouthOpen += (o - mouthOpen) * k; mouthRound += (r - mouthRound) * k
        }
    }

    private fun updateBlink(now: Long) {
        if (now >= nextBlink && blinkPlan.isEmpty()) {
            var at = now
            repeat(if (Random.nextInt(5) == 0) 2 else 1) { blinkPlan.add(at); at += 270 }
            nextBlink = at + 1800 + Random.nextLong(2800)
        }
        blink = 0f
        blinkPlan.removeAll { start ->
            val d = now - start
            when {
                d < 0 -> false
                d < 70 -> { blink = maxOf(blink, d / 70f); false }
                d < 180 -> { blink = maxOf(blink, 1f - (d - 70) / 110f); false }
                else -> true
            }
        }
    }

    private fun updateLook(now: Long, m: FolhaMood) {
        val pr = clamp((now - lookFrom).toFloat() / lookDur, 0f, 1f); val e = 1 - (1 - pr) * (1 - pr)
        lookX = lookFx + (lookTx - lookFx) * e; lookY = lookFy + (lookTy - lookFy) * e
        if (pr < 1f || now < lookNext) return
        lookFx = lookX; lookFy = lookY; lookFrom = now
        when (m) {
            FolhaMood.THINKING -> { val toLeft = lookX > 0; lookTx = if (toLeft) -1f else 1f; lookTy = 0.6f; lookDur = if (toLeft) 260 else 900; lookNext = now + lookDur }
            FolhaMood.IDLE -> { lookTx = Random.nextFloat() * 2 - 1; lookTy = Random.nextFloat() * 1.2f - 0.6f; lookDur = 380; lookNext = now + 380 + 900 + Random.nextLong(1700) }
            FolhaMood.SAD -> { lookTx = (Random.nextFloat() - 0.5f) * 0.6f; lookTy = 0.9f; lookDur = 700; lookNext = now + 1800 }
            else -> { lookTx = 0f; lookTy = 0f; lookDur = 300; lookNext = now + 1100 }
        }
    }

    private fun fidget(now: Long, m: FolhaMood): Fidget? {
        if (m != FolhaMood.IDLE) { fid = null; nextFid = now + 5000 + Random.nextLong(4000); return null }
        if (fid == null && now > nextFid) {
            val kinds = listOf("cap" to 1700L, "yawn" to 2300L, "glance" to 1500L, "bounce" to 1100L)
            var k = kinds[Random.nextInt(kinds.size)]
            if (k.first == lastFid) k = kinds[(kinds.indexOf(k) + 1) % kinds.size]
            fid = Fidget(k.first, now, k.second, if (Random.nextBoolean()) -1f else 1f); lastFid = k.first
        }
        val f = fid ?: return null
        if (now - f.start >= f.dur) { fid = null; nextFid = now + 7000 + Random.nextLong(6000); return null }
        return f
    }

    private fun bounce(): Float {
        if (bounceAt < 0) return 0f
        val d = nowMs - bounceAt
        if (d < 110) return d / 110f
        val s = (d - 110) / 1000f
        if (s > 1.4f) { bounceAt = -1; return 0f }
        return exp(-s * 5.5f) * cos(s * 17f)
    }

    fun snapshot(gulp: Float, look: Look): FolhaFrame {
        val p = PoseValues().also { it.set(x); it.shapeL = shapeL; it.shapeR = shapeR }
        worried = look.atRisk
        var fade = 1f; var rise = 0f
        if (leaveAt >= 0) { val u = clamp((nowMs - leaveAt - 480L) / 280f, 0f, 1f); fade = 1f - u; rise = u * u * 0.3f }
        return FolhaFrame(
            look = look, tm = (nowMs - moodAt) / 1000f, fade = fade, rise = rise,
            t = t, mood = shown, small = small, p = p, vy = v[0], blink = blink,
            wink = if (wink || shown == FolhaMood.WINK) 1f else 0f, popL = popL, popR = popR,
            squash = maxOf(bounce(), gulp * 0.45f) + clamp(sq * 0.1f, -0.2f, 0.25f), gulp = gulp,
            speechOpen = if (speaking) mouthOpen else null, speechRound = mouthRound,
        )
    }
}

private class FolhaFrame(
    val look: Look, val tm: Float, val fade: Float, val rise: Float,
    val t: Float, val mood: FolhaMood, val small: Boolean, val p: PoseValues, val vy: Float, val blink: Float, val wink: Float,
    val popL: Float, val popR: Float, val squash: Float, val gulp: Float, val speechOpen: Float?, val speechRound: Float,
)

// ===================================================================== desenho (grade 100 x 100)

private fun stroke(w: Float) = Stroke(w, cap = StrokeCap.Round, join = StrokeJoin.Round)
private fun DrawScope.line(color: Color, x0: Float, y0: Float, x1: Float, y1: Float, w: Float, round: Boolean = true) =
    drawLine(color, Offset(x0, y0), Offset(x1, y1), w, if (round) StrokeCap.Round else StrokeCap.Butt)
private fun DrawScope.oval(color: Color, x: Float, y: Float, w: Float, h: Float) = drawOval(color, Offset(x, y), Size(maxOf(w, 0.01f), maxOf(h, 0.01f)))
private fun ellipsePath(cx: Float, cy: Float, rx: Float, ry: Float) = Path().apply { addOval(Rect(cx - rx, cy - ry, cx + rx, cy + ry)) }

private fun page(side: Float, drop: Float = 0f) = Path().apply {
    fun x(v: Float) = 50f + side * v
    moveTo(x(0f), 44f + drop * 0.2f)
    cubicTo(x(10f), 37.6f, x(26f), 36.4f, x(38f), 40.4f + drop)
    lineTo(x(38f), 84.4f + drop)
    cubicTo(x(26f), 80.6f + drop, x(10f), 81.4f + drop, x(0f), 87f + drop)
    close()
}
private fun pageBlock(side: Float, depth: Float) = Path().apply {
    val outer = 50f + side * 38f; val outerLow = 50f + side * (38f + depth * 0.45f)
    moveTo(outer, 40.4f); lineTo(outer, 84.4f)
    cubicTo(50f + side * 26f, 80.6f, 50f + side * 10f, 81.4f, 50f, 87f)
    lineTo(50f, 87f + depth)
    cubicTo(50f + side * 10f, 81.4f + depth, 50f + side * 26.5f, 80.6f + depth, outerLow, 84.4f + depth)
    lineTo(outerLow, 41.6f + depth * 0.3f)
    close()
}
private fun coverPath() = Path().apply {
    moveTo(8.5f, 43.5f); cubicTo(22f, 40.5f, 38f, 41.5f, 50f, 47.5f); cubicTo(62f, 41.5f, 78f, 40.5f, 91.5f, 43.5f)
    lineTo(91.5f, 89f); cubicTo(78f, 86.4f, 62f, 87.4f, 50f, 93f); cubicTo(38f, 87.4f, 22f, 86.4f, 8.5f, 89f); close()
}

private fun DrawScope.drawFolha(f: FolhaFrame) {
    val k = size.minDimension / 100f
    val p = f.p; val t = f.t; val mood = f.mood; val small = f.small
    val squashX = 1f + 0.10f * f.squash; val squashY = 1f - 0.12f * f.squash
    val stretch = 1f + clamp(abs(f.vy) * 0.006f, 0f, 0.07f)
    scale(k, k, pivot = Offset.Zero) {
        // sombra: menor quando ele sobe
        val lift = maxOf(-p.hop, 0f)
        scale(1f, 0.18f, pivot = Offset(50f, 95f)) {
            val r1 = 36f - lift * 1.6f
            drawCircle(Brush.radialGradient(listOf(Ink.copy(alpha = 0.28f), Color.Transparent), center = Offset(50f, 95f), radius = r1), r1, Offset(50f, 95f))
            drawCircle(Brush.radialGradient(listOf(Ink.copy(alpha = 0.30f * clamp(1f - lift / 6f, 0.3f, 1f)), Color.Transparent), center = Offset(50f, 95f), radius = 26f), 26f, Offset(50f, 95f))
        }
        translate(0f, p.hop) {
            scale(squashX / sqrt(stretch), squashY * stretch, pivot = Offset(50f, 88f)) {
                rotate(p.tilt, pivot = Offset(50f, 80f)) {
                    val look = f.look
                    // na frente do livro: as duas mãos folheando ou no susto; a do lápis no foco; a da testa no "ufa"
                    val front = when (mood) { FolhaMood.THINKING, FolhaMood.SURPRISED -> "LR"; FolhaMood.FOCUS, FolhaMood.RELIEVED -> "R"; else -> "" }
                    drawArms(f, "LR".filter { it !in front })
                    drawBook(t, mood, small, look)
                    drawPageLines(t, mood, small)
                    if (!small) drawDogEar(t, mood)
                    if (mood == FolhaMood.THINKING) drawTurningPage(t)
                    drawFace(f)
                    if (look.acc == "oculos") drawGlasses(p, small)
                    if (look.acc == "gravata") drawBowTie(small)
                    val nightcap = look.night && (mood == FolhaMood.IDLE || mood == FolhaMood.SLEEPY)
                    rotate(p.capRot, pivot = Offset(50f, 38f)) {
                        if (nightcap) drawNightcap(t, p.tilt, f.vy)
                        else {
                            drawCap(t, p.tilt, f.vy)
                            if (look.acc == "lapis") drawPencilAt(25f, 44.5f, 41.5f, 36f, if (small) 1.5f else 1.15f)   // lápis atrás da "orelha"
                            if (look.acc == "louros") drawLaurels(small)
                            if (look.acc == "estrela") drawStarPin(t, small)
                            if (look.streak >= 7 && !small) drawStreakMedal(t, look.streak, p.tilt)
                        }
                    }
                    if (front.isNotEmpty()) drawArms(f, front)
                    if (mood == FolhaMood.RELIEVED && !small) drawRelief(f.tm)
                    if (look.atRisk && mood == FolhaMood.IDLE && !small) drawSweat(t)
                }
            }
        }
        val look = f.look
        if (!small && look.morning && mood in MUG_MOODS) drawMug(t)
        if (!small && look.night && mood in MOON_MOODS) drawMoon(t)
        when (mood) {
            FolhaMood.ECSTATIC -> { drawFlames(t, small); drawCelebration(t, small) }
            FolhaMood.HAPPY -> drawCelebration(t, small)
            FolhaMood.PROUD -> drawSparkles(t, small)
            FolhaMood.SLEEPY -> drawZzz(t, small)
            FolhaMood.DIZZY -> drawDizzyStars(t)
            FolhaMood.FOCUS -> if (!small) drawFocusMarks(t)
            else -> {}
        }
    }
}

private val COVER_STARS = listOf(
    floatArrayOf(9.8f, 50f, 0.35f), floatArrayOf(10.6f, 63f, 0.25f), floatArrayOf(9.6f, 76f, 0.4f), floatArrayOf(90.2f, 55f, 0.3f), floatArrayOf(89.6f, 70f, 0.4f),
    floatArrayOf(90.4f, 82f, 0.25f), floatArrayOf(24f, 88.6f, 0.3f), floatArrayOf(40f, 90.4f, 0.35f), floatArrayOf(62f, 90.2f, 0.3f), floatArrayOf(77f, 88.4f, 0.35f),
)
private val GOLD_STOPS = listOf(Color(0xFFFFE3A0), Gold, Color(0xFFC98A12))
private val SILVER_STOPS = listOf(Color.White, Color(0xFFCDD0DC), Color(0xFF8E92A6))

private fun DrawScope.drawCorners(silver: Boolean = false) {
    fun corner(x: Float, y: Float, sx: Float, sy: Float) {
        val p = Path().apply {
            moveTo(x, y + sy * 6.5f); lineTo(x, y + sy * 1.2f); quadraticTo(x, y, x + sx * 1.2f, y); lineTo(x + sx * 6.5f, y)
            lineTo(x + sx * 6.5f, y + sy * 1.9f); lineTo(x + sx * 1.9f, y + sy * 1.9f); lineTo(x + sx * 1.9f, y + sy * 6.5f); close()
        }
        drawPath(p, Brush.verticalGradient(if (silver) SILVER_STOPS else GOLD_STOPS, startY = minOf(y, y + sy * 6.5f), endY = maxOf(y, y + sy * 6.5f)))
        drawPath(p, Color(0x59784600), style = Stroke(0.3f))
        drawCircle(Color.White.copy(alpha = 0.75f), 0.45f, Offset(x + sx * 0.95f, y + sy * 0.95f))
    }
    corner(8.5f, 43.5f, 1f, 1f); corner(91.5f, 43.5f, -1f, 1f); corner(8.5f, 89f, 1f, -1f); corner(91.5f, 89f, -1f, -1f)
}

/** Orelha na página da direita (canto de baixo dobrado): cara de livro estudado. */
private fun DrawScope.drawDogEar(t: Float, mood: FolhaMood) {
    val k = if (mood == FolhaMood.THINKING) 0.7f + 0.3f * abs(sin(t * 1.2f)) else 1f
    val x = 87.7f; val y = 84.4f; val d = 5f * k
    drawPath(Path().apply { moveTo(x - d, y + 0.1f); lineTo(x + 0.2f, y - d - 0.1f); lineTo(x + 0.2f, y + 0.1f); close() }, PaperEdge)
    val fold = Path().apply { moveTo(x - d, y + 0.1f); lineTo(x + 0.2f, y - d - 0.1f); lineTo(x - d * 0.8f, y - d * 0.78f); close() }
    translate(-0.4f, 0.4f) { drawPath(fold, Ink.copy(alpha = 0.12f)) }
    drawPath(fold, Brush.horizontalGradient(listOf(Color.White, Color(0xFFECE6DA)), startX = x - d, endX = x))
    drawPath(fold, Gutter.copy(alpha = 0.18f), style = Stroke(0.25f))
}

private val GRAIN: List<FloatArray> = run {
    var s = 7L
    fun rnd(): Float { s = (s * 1103515245L + 12345L) and 0x7fffffffL; return s / 0x7fffffff.toFloat() }
    List(80) { i ->
        val side = if (i % 2 == 0) -1f else 1f
        floatArrayOf(50f + side * (3f + rnd() * 32f), 44f + rnd() * 36f, 0.15f + rnd() * 0.22f, 0.05f + rnd() * 0.08f, if (floor(rnd() * 4f) == 0f) 1f else 0f)
    }
}

private fun DrawScope.drawBook(t: Float, mood: FolhaMood, small: Boolean, look: Look) {
    val cover = coverPath(); val c = look.cover
    // contorno macio: claro por fora (fundo escuro) e tinta por dentro (fundo claro)
    drawPath(cover, Color.White.copy(alpha = 0.28f), style = stroke(if (small) 3f else 2f))
    drawPath(cover, c.under.copy(alpha = 0.85f), style = stroke(if (small) 1.6f else 1f))
    translate(0f, 1.7f) { drawPath(cover, c.under) }
    drawPath(cover, Brush.verticalGradient(c.c, startY = 41f, endY = 93f))
    drawPath(cover, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.14f), Color.Transparent), start = Offset(8f, 44f), end = Offset(40f, 70f)))
    if (!small) clipPath(cover) {
        if (c.stitch) {   // couro: granulado e costura pontilhada rente à borda
            for (g in GRAIN) drawCircle(Color(0xFF281204).copy(alpha = g[3] * 1.3f), g[2] * 1.3f, Offset(if (g[0] < 50f) 8.5f + (g[0] - 15f) * 0.1f else 91.5f - (85f - g[0]) * 0.1f, g[1] + 6f))
            val st = Path().apply { moveTo(10.2f, 45.8f); lineTo(10.2f, 87.4f); cubicTo(22f, 85.3f, 38f, 86.2f, 50f, 91.4f); cubicTo(62f, 86.2f, 78f, 85.3f, 89.8f, 87.4f); lineTo(89.8f, 45.8f) }
            drawPath(st, Color(0xBFFFE2B4), style = Stroke(0.35f, cap = StrokeCap.Round, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(1.1f, 0.9f))))
        } else {
            var x = -50f
            while (x < 100f) { line(Color.White.copy(alpha = 0.045f), x, 40f, x + 54f, 95f, 0.3f, false); line(Color.Black.copy(alpha = 0.06f), x + 54f, 40f, x, 95f, 0.3f, false); x += 1.5f }
        }
        if (c.stars) for (st in COVER_STARS) drawCircle(Color(0xFFFFF8D6).copy(alpha = 0.55f + 0.45f * abs(sin(t * 1.3f + st[0]))), st[2], Offset(st[0], st[1]))
        if (c.shine) {   // ouro: um brilho passando de tempos em tempos
            val sx = -30f + ((t * 0.32f) % 1f) * 170f
            drawPath(cover, Brush.linearGradient(listOf(Color.White.copy(alpha = 0f), Color.White.copy(alpha = 0.55f), Color.White.copy(alpha = 0f)), start = Offset(sx, 40f), end = Offset(sx + 18f, 95f)))
        }
    }
    for (side in listOf(-1f, 1f)) {
        val x0 = 50f + side * 41.5f
        drawPath(Path().apply { moveTo(x0, 43.5f); lineTo(x0 + side * 1.6f, 44.4f); lineTo(x0 + side * 1.6f, 88.2f); lineTo(x0, 89f); close() },
            Brush.verticalGradient(listOf(c.c[2], c.under), startY = 44f, endY = 89f))
    }
    if (!small) drawCorners(c.silver)
    drawRibbon(t, mood, look.ribbon)
    for (side in listOf(-1f, 1f)) {
        drawPath(pageBlock(side, 3.6f), Brush.verticalGradient(listOf(PaperEdge, PaperEdgeDark), startY = 80f, endY = 91f))
        if (!small) {
            for (i in 1..6) {
                val d = i * 0.52f
                drawPath(Path().apply { moveTo(50f + side * (38f + d * 0.45f), 84.4f + d); cubicTo(50f + side * 26.2f, 80.6f + d, 50f + side * 10f, 81.4f + d, 50f, 87f + d) },
                    Color(0x388C8270), style = stroke(0.28f))
            }
            for (i in 1..5) { val x = 50f + side * (38f + i * 0.3f); line(Color(0x2E8C8270), x, 41f + i * 0.3f, x, 84.4f + i * 0.9f, 0.25f, false) }
        }
    }
    val left = page(-1f); val right = page(1f)
    drawPath(left, Brush.horizontalGradient(listOf(PaperMid, Paper, Paper, PaperMid), startX = 12f, endX = 50f))
    drawPath(right, Brush.horizontalGradient(listOf(PaperMid, Paper, Paper, Color(0xFFF1EDE6)), startX = 50f, endX = 88f))
    val gutter = arrayOf(0f to Gutter.copy(alpha = 0f), 0.55f to Gutter.copy(alpha = 0.035f), 0.85f to Gutter.copy(alpha = 0.10f), 1f to Gutter.copy(alpha = 0.20f))
    drawPath(left, Brush.horizontalGradient(*gutter, startX = 39f, endX = 50f))
    drawPath(right, Brush.horizontalGradient(*gutter, startX = 61f, endX = 50f))
    if (!small) for (g in GRAIN) {
        val c = Color(0xFF9A8F7A).copy(alpha = g[3])
        if (g[4] > 0f) line(c, g[0], g[1], g[0] + 1.4f, g[1] + 0.3f, 0.18f) else drawCircle(c, g[2], Offset(g[0], g[1]))
    }
    drawPath(left, Brush.horizontalGradient(listOf(Gutter.copy(alpha = 0.06f), Gutter.copy(alpha = 0f)), startX = 12f, endX = 17f))
    drawPath(right, Brush.horizontalGradient(listOf(Gutter.copy(alpha = 0f), Gutter.copy(alpha = 0.08f)), startX = 83f, endX = 88f))
    line(Gutter.copy(alpha = 0.16f), 50f, 44.3f, 50f, 86.8f, 0.35f, false)
    if (!small) { oval(Color(0xFFC0392B), 48.6f, 43.2f, 2.8f, 1.6f); line(Color(0xD9FFE6AA), 48.9f, 43.9f, 51.1f, 43.9f, 0.35f, false) }
    for (side in listOf(-1f, 1f)) {
        drawPath(Path().apply { moveTo(50f + side * 3f, 41.9f); cubicTo(50f + side * 12f, 37.9f, 50f + side * 26f, 36.9f, 50f + side * 36.5f, 40.2f) },
            Color.White.copy(alpha = 0.9f), style = stroke(0.5f))
    }
}

private fun DrawScope.drawTurningPage(t: Float) {
    val tt = turnState(t)
    if (!tt.turning) return
    val w = tt.edgeX - 50f; val up = tt.up; val lift = tt.lift; val c = tt.c; val edgeX = tt.edgeX
    val dir = if (c >= 0f) 1f else -1f
    // o canto de baixo (onde a mão segura) vai adiantado: a folha enrola em diagonal
    val lead = lift * 7f * (if (c >= 0f) -1f else 1f) * 0.6f; val curl = lift * 3.2f
    val sheet = Path().apply {
        moveTo(50f, 44f)
        cubicTo(50f + w * 0.28f, 37.6f - up * 0.7f, 50f + w * 0.7f, 36.4f - up, edgeX - lead * 0.4f, 40.4f - up * 0.8f)
        quadraticTo(edgeX + dir * 2.2f * lift - lead * 0.2f, 62f - up, edgeX + lead, 84.4f - up * 0.8f - curl)
        cubicTo(50f + w * 0.7f + lead * 0.5f, 80.6f - up - curl * 0.5f, 50f + w * 0.28f, 81.4f - up * 0.5f, 50f, 87f)
        close()
    }
    translate(dir * 2.5f * lift, 2.2f * lift) { drawPath(sheet, Gutter.copy(alpha = 0.13f * lift)) }
    val front = c >= 0f; val facing = abs(c); val base = if (front) Paper else Color(0xFFF2EEE6)
    if (abs(w) > 0.6f) {
        drawPath(sheet, Brush.horizontalGradient(listOf(lerpColor(base, Gutter, 0.16f + 0.1f * lift), lerpColor(base, Gutter, 0.05f * (1 - facing)), lerpColor(base, Color.White, 0.5f * facing)), startX = 50f, endX = edgeX))
        if (front) for ((y, len) in listOf(45f to 0.5f, 49f to 0.7f, 79.5f to 0.45f)) {
            val yy = y - up * 0.85f
            line(PageLine.copy(alpha = facing), 50f + w * 0.88f, yy, 50f + w * (0.88f - len), yy + 1.5f * (len - 0.4f), 1.6f * (0.5f + 0.5f * facing))
        }
    }
    line(Color.White.copy(alpha = 0.8f), edgeX - lead * 0.4f, 40.6f - up * 0.8f, edgeX + dir * 1.6f * lift, 62f - up, 0.45f)
    drawPath(sheet, Gutter.copy(alpha = 0.18f), style = stroke(0.25f))
    if (lift > 0.15f) {   // dobrinha do canto enrolado (o verso aparecendo)
        val cx = edgeX + lead; val cy = 84.4f - up * 0.8f - curl; val kk = lift * 4f
        val cu = Path().apply { moveTo(cx, cy); lineTo(cx - dir * kk * 0.9f, cy + kk * 0.15f); quadraticTo(cx - dir * kk * 0.3f, cy - kk * 0.5f, cx - dir * kk * 0.15f, cy - kk * 1.1f); close() }
        drawPath(cu, Color(0xFFEFEAE0)); drawPath(cu, Gutter.copy(alpha = 0.2f), style = stroke(0.25f))
    }
}

private fun lerpColor(a: Color, b: Color, f: Float) = Color(lerp(a.red, b.red, f), lerp(a.green, b.green, f), lerp(a.blue, b.blue, f), lerp(a.alpha, b.alpha, f))

private fun DrawScope.drawPageLines(t: Float, mood: FolhaMood, small: Boolean) {
    val writing = mood == FolhaMood.THINKING
    val cycle = (t % TURN_CYCLE) / TURN_CYCLE
    val lw = if (small) 2.4f else 1.6f
    fun ln(x0: Float, y0: Float, len: Float, slope: Float, index: Int, mirror: Boolean) {
        val pr = if (!writing) 1f else clamp((cycle - TURN_SHARE) / (1 - TURN_SHARE) * 6f - index, 0f, 1f)
        if (pr <= 0f) return
        val x1 = if (mirror) x0 - len * pr else x0 + len * pr
        line(PageLine, x0, y0, x1, y0 + slope * len * pr, lw)
    }
    if (!small) {
        // anotações: marca-texto numa linha, um visto e uma estrelinha (depois que ele escreve)
        val hl = if (!writing) 1f else clamp((cycle - TURN_SHARE) / (1 - TURN_SHARE) * 6f - 1.5f, 0f, 1f)
        if (hl > 0f) line(Color(0x8CFFE36B), 16.6f, 49.4f, 16.6f + 24f * hl, 49.4f + 2.9f * hl, 3.2f)
        if (hl >= 1f) {
            drawPath(Path().apply { moveTo(78.6f, 74.4f); lineTo(79.8f, 75.8f); lineTo(82.4f, 72.6f) }, MintDark, style = stroke(0.9f))
            star4(21f, 54.6f, 1.6f, Gold.copy(alpha = 0.85f))
        }
    }
    ln(17f, 45f, 20f, 0.1f, 0, false); ln(17f, 49f, 26f, 0.12f, 1, false)
    ln(83f, 45f, 20f, 0.1f, 2, true); ln(83f, 49f, 26f, 0.12f, 3, true)
    if (!small) { ln(17f, 79.5f, 18f, -0.03f, 4, false); ln(83f, 79.5f, 18f, -0.03f, 5, true) }
}

private fun DrawScope.drawFace(f: FolhaFrame) {
    val p = f.p; val t = f.t; val mood = f.mood; val small = f.small; val blink = f.blink
    val eyeL = Offset(31f + p.lookX * 1.6f, 61f + p.lookY * 1.4f); val eyeR = Offset(69f + p.lookX * 1.6f, 61f + p.lookY * 1.4f)
    oval(Cheek.copy(alpha = p.cheek), 19f, 67f, 9f, 5f); oval(Cheek.copy(alpha = p.cheek), 72f, 67f, 9f, 5f)

    fun eye(c: Offset, side: Float, extraLid: Float) {
        val x = c.x; val y = c.y
        if (mood == FolhaMood.DIZZY) {   // espiral girando
            val sp = Path(); val a0 = t * 7f * side
            for (i in 0..40) { val a = a0 + i * 0.42f; val rr = 0.25f + i * 0.12f; val px = x + cos(a) * rr; val py = y + sin(a) * rr * 1.15f; if (i == 0) sp.moveTo(px, py) else sp.lineTo(px, py) }
            drawPath(sp, Ink, style = stroke(if (small) 1.6f else 1.2f)); return
        }
        if (mood == FolhaMood.ECSTATIC && blink < 0.5f) {   // olhos de estrela, pulsando
            val st = star5(x, y, (if (small) 6.4f else 5.8f) * (1f + 0.1f * sin(t * 9f)))
            drawPath(st, Brush.verticalGradient(listOf(Color(0xFFFFF1BF), Gold, Color(0xFFE39A1E)), startY = y - 6f, endY = y + 5f))
            drawPath(st, Ink, style = stroke(if (small) 1.2f else 0.8f))
            if (!small) drawCircle(Color.White.copy(alpha = 0.9f), 0.9f, Offset(x - 1.2f, y - 1.6f))
            return
        }
        val rx = 4.4f * p.eyeScale * (if (small) 1.18f else 1f); val ry = 5.8f * p.eyeScale * (if (small) 1.1f else 1f)
        val up = clamp(maxOf(p.up, blink, extraLid), 0f, 1f); val low = clamp(p.low * (1 - blink), 0f, 1f)
        if (small && low > 0.45f && blink < 0.5f) {   // pequeno e feliz: o arco clássico (^^) lê melhor
            drawPath(Path().apply { moveTo(x - 4.5f, y + 1.5f); quadraticTo(x, y - 5f, x + 4.5f, y + 1.5f) }, Ink, style = stroke(2.8f)); return
        }
        if (up > 0.96f) {   // fechado: só o traço
            drawPath(Path().apply { moveTo(x - 4.4f, y + 0.4f); quadraticTo(x, y + 2.4f, x + 4.4f, y + 0.4f) }, Ink, style = stroke(if (small) 2.2f else 1.8f)); return
        }
        drawOval(Ink, Offset(x - rx, y - ry), Size(rx * 2, ry * 2))
        if (up < 0.85f && low < 0.9f) {
            drawCircle(Color.White, 1.7f * p.pupil, Offset(x - 1.4f + p.lookX * 0.6f, y - 2.2f))
            drawCircle(Color.White.copy(alpha = 0.7f), 0.8f * p.pupil, Offset(x + 1.6f, y + 1.8f))
        }
        // pálpebras da cor do papel, fechando como cortina; a borda é um traço de tinta
        clipPath(ellipsePath(x, y, rx + 0.6f, ry + 0.6f)) {
            if (up > 0.01f) {
                val yl = y - ry - 0.8f + (2 * ry + 0.8f) * up; val dr = p.droop * 2.6f
                val yo = yl + dr; val yi = yl - dr * 0.4f
                val xa = x - rx - 1f; val xb = x + rx + 1f
                val ya = if (side < 0) yo else yi; val yb = if (side < 0) yi else yo
                val mid = (ya + yb) / 2f + 1.4f * (1 - up)
                drawPath(Path().apply { moveTo(xa, y - ry - 2f); lineTo(xb, y - ry - 2f); lineTo(xb, yb); quadraticTo(x, mid, xa, ya); close() }, Paper)
                drawPath(Path().apply { moveTo(xa, ya); quadraticTo(x, mid, xb, yb) }, Ink, style = stroke(1.2f))
            }
            if (low > 0.01f) {
                val yl = y + ry + 0.8f - (2 * ry + 0.8f) * low
                drawPath(Path().apply { moveTo(x - rx - 1f, y + ry + 2f); lineTo(x + rx + 1f, y + ry + 2f); lineTo(x + rx + 1f, yl + 2.4f * low); quadraticTo(x, yl - 3.2f * low, x - rx - 1f, yl + 2.4f * low); close() }, Paper)
            }
        }
    }
    eye(eyeL, -1f, 0f); eye(eyeR, 1f, f.wink)

    // sobrancelhas: pequeno, só aparecem quando mudam a expressão (neutras, só embolam o rosto)
    val browA = if (small) clamp((abs(p.browY) + p.browIn * 2f + p.browR) / 1.2f - 0.3f, 0f, 1f) else 1f
    if (browA > 0.05f) for ((c, side) in listOf(eyeL to -1f, eyeR to 1f)) {
        val by = c.y - 9.8f + p.browY - (if (side > 0) p.browR else 0f) + (if (blink > 0.5f) 0.6f else 0f)
        val inner = Offset(c.x - side * 4.2f, by - p.browIn * 2.2f + (1 - p.browIn) * 0.4f); val outer = Offset(c.x + side * 4.4f, by + p.browIn * 1.2f)
        drawPath(Path().apply { moveTo(outer.x, outer.y); quadraticTo(c.x, by - 1.4f + p.browIn * 0.6f, inner.x, inner.y) }, Ink.copy(alpha = 0.9f * browA), style = stroke(if (small) 2.2f else 1.7f))
    }
    // lágrima (triste)
    if (mood == FolhaMood.SAD && !small) {
        val tp = (t % 3.2f) / 3.2f
        if (tp < 0.8f) {
            val ty = 64.5f + tp * 9f; val ta = if (tp < 0.1f) tp / 0.1f else 1f - maxOf(0f, tp - 0.6f) / 0.2f
            drawPath(Path().apply { moveTo(36.2f, ty - 2.2f); quadraticTo(38.2f, ty + 0.4f, 36.2f, ty + 1.2f); quadraticTo(34.2f, ty + 0.4f, 36.2f, ty - 2.2f) }, Color(0xFF7EC8FF).copy(alpha = 0.85f * ta))
            drawCircle(Color.White.copy(alpha = 0.8f * ta), 0.35f, Offset(35.8f, ty - 0.2f))
        }
    }
    // boca
    val mx = 50f; val my = 73f
    if (f.gulp > 0.08f) {
        val r = 1.5f + 3.2f * f.gulp
        oval(Ink, mx - r, my - r * 1.1f, r * 2, r * 2.2f)
        for (i in 0 until 4) { val a = i * (PIf / 2) + 0.5f; drawCircle(Spark.copy(alpha = f.gulp), 1.8f * f.gulp * 0.9f, Offset(50f + cos(a) * (22f + 14f * (1 - f.gulp)), 58f + sin(a) * (16f + 10f * (1 - f.gulp)))) }
        return
    }
    if (mood == FolhaMood.SURPRISED) { oval(Ink, mx - 2.6f, my - 3f, 5.2f, 6.4f); oval(Cheek, mx - 1.6f, my + 0.9f, 3.2f, 1.8f); return }
    if (mood == FolhaMood.DIZZY) {
        val wv = Path(); for (i in 0..12) { val px = mx - 5f + i * (10f / 12f); val py = my + sin(i * 1.4f + t * 8f) * 0.9f; if (i == 0) wv.moveTo(px, py) else wv.lineTo(px, py) }
        drawPath(wv, Ink, style = stroke(if (small) 2.4f else 1.8f)); return
    }
    var open = p.mOpen; var w = p.mW; var c = p.mCurve
    if (mood == FolhaMood.TALKING) {
        val so = f.speechOpen
        if (so != null) { open = so; w = 4.6f - 1.7f * f.speechRound; c = 2.4f * (1 - f.speechRound) }
        else { open = (0.5f + 0.5f * sin(t * 13f)) * (0.55f + 0.45f * abs(sin(t * 3.1f))); w = 4.2f + 0.8f * open }
    }
    if (mood == FolhaMood.SAD) c += sin(t * 9f) * 0.35f
    val corner = my - c * 0.3f
    if (open < 0.12f) {
        val m = Path().apply { moveTo(mx - w, corner); quadraticTo(mx, corner + c * 1.6f, mx + w, corner) }
        if (p.tongue > 0.5f) oval(Cheek, mx + w - 2.4f, corner - 0.2f, 2.6f, 2.2f)   // língua no canto (concentrado)
        drawPath(m, Ink, style = stroke(if (small) 2.8f else 2.3f))
    } else {
        val top = corner + c * 0.15f - open * 0.6f; val depth = maxOf(1.4f, c * 1.1f) + open * 5.4f
        val m = Path().apply { moveTo(mx - w, corner); quadraticTo(mx, top, mx + w, corner); cubicTo(mx + w * 0.95f, corner + depth * 1.25f, mx - w * 0.95f, corner + depth * 1.25f, mx - w, corner); close() }
        drawPath(m, Ink)
        clipPath(m) { oval(Cheek, mx - w * 0.55f, corner + depth - 1.6f * open - 0.6f, w * 1.1f, 3.4f * open + 1f) }
    }
}

private fun DrawScope.drawCap(t: Float, tilt: Float, vy: Float) {
    drawPath(Path().apply { moveTo(38f, 32f); lineTo(62f, 32f); lineTo(60.5f, 42.5f); quadraticTo(50f, 46.5f, 39.5f, 42.5f); close() },
        Brush.verticalGradient(listOf(Cap, Color(0xFF15104A)), startY = 32f, endY = 46f))
    drawPath(Path().apply { moveTo(23f, 29.5f); lineTo(50f, 38f); lineTo(77f, 29.5f); lineTo(77f, 31.3f); lineTo(50f, 39.8f); lineTo(23f, 31.3f); close() }, Color(0xFF120D3F))
    val board = Path().apply { moveTo(50f, 21f); lineTo(77f, 29.5f); lineTo(50f, 38f); lineTo(23f, 29.5f); close() }
    drawPath(board, Brush.linearGradient(listOf(CapTop, Cap), start = Offset(30f, 22f), end = Offset(70f, 38f)))
    drawPath(board, Color.White.copy(alpha = 0.22f), style = stroke(0.5f))
    line(Color.White.copy(alpha = 0.18f), 33f, 27.5f, 50f, 22.5f, 1.2f)
    // borla: balança com o corpo e reage aos pulos (atrasada)
    val swing = sin(t * 2.6f) * 4f - tilt * 0.8f + clamp(vy * 0.08f, -5f, 5f)
    val bx = 50f; val by = 29.5f; val cx = 74f; val cy = 30.5f
    line(Gold, bx, by, cx, cy, 1.3f)
    val ex = cx + swing * 0.5f; val ey = cy + 12f - maxOf(0f, -vy) * 0.03f
    drawPath(Path().apply { moveTo(cx, cy); quadraticTo(cx + swing * 0.2f, cy + 6f, ex, ey) }, Gold, style = stroke(1.3f))
    drawPath(Path().apply { moveTo(ex - 1.4f, ey); lineTo(ex + 1.4f, ey); lineTo(ex + 2.4f + swing * 0.15f, ey + 6.5f); lineTo(ex - 2.4f + swing * 0.15f, ey + 6.5f); close() },
        Brush.verticalGradient(listOf(Gold, Color(0xFFD89422)), startY = ey, endY = ey + 6.5f))
    drawCircle(Gold, 1.8f, Offset(bx, by)); drawCircle(Color(0xFFFFE3A0), 0.7f, Offset(bx - 0.5f, by - 0.5f))
}

private fun DrawScope.drawRibbon(t: Float, mood: FolhaMood, colors: List<Color>) {
    val (dark, base, light) = colors
    val sad = mood == FolhaMood.SAD; val sway = if (sad) 0f else sin(t * 1.9f + 0.6f) * 1.3f
    val w = 3.4f; val x0 = 51.4f; val lieTop = 86f; val edge = 94.6f; val hang = if (sad) 3.2f else 5.4f
    val tipY = edge + hang; val tipX = x0 + 0.5f + sway
    val r = Path().apply {
        moveTo(x0, lieTop); lineTo(x0 + w, lieTop); lineTo(x0 + w + 0.3f, edge)
        quadraticTo(x0 + w + 0.3f + sway * 0.3f, edge + hang * 0.5f, tipX + w, tipY)
        lineTo(tipX + w / 2f, tipY - 1.6f); lineTo(tipX, tipY)
        quadraticTo(x0 + 0.3f + sway * 0.3f, edge + hang * 0.5f, x0 + 0.3f, edge)
        close()
    }
    translate(0.7f, 0.5f) { drawPath(r, Color(0x590E0A45)) }
    drawPath(r, Brush.horizontalGradient(listOf(dark, base, light, base), startX = x0, endX = x0 + w + 0.6f + sway))
    drawRect(Brush.verticalGradient(listOf(Color(0x730E0A45), Color(0x000E0A45)), startY = 88f, endY = 91.5f), Offset(x0, 88f), Size(w + 0.3f, 3.5f))
    line(Color.White.copy(alpha = 0.55f), x0 + 0.5f, edge - 0.4f, x0 + w, edge - 0.4f, 0.5f, false)
    drawRect(Brush.verticalGradient(listOf(dark.copy(alpha = 0.55f), dark.copy(alpha = 0f)), startY = edge, endY = edge + 1.8f), Offset(x0 + 0.3f, edge), Size(w, 1.8f))
}

// ---- acessórios do armário e cenário do dia

private val MUG_MOODS = setOf(FolhaMood.IDLE, FolhaMood.TALKING, FolhaMood.THINKING, FolhaMood.FOCUS, FolhaMood.WAVE, FolhaMood.POINT, FolhaMood.PROUD, FolhaMood.WINK)
private val MOON_MOODS = setOf(FolhaMood.IDLE, FolhaMood.SLEEPY, FolhaMood.FOCUS, FolhaMood.THINKING, FolhaMood.WINK)

/** Lápis da ponta (x0, y0) até a borracha (x1, y1). */
private fun DrawScope.drawPencilAt(x0: Float, y0: Float, x1: Float, y1: Float, r: Float) {
    val len = hypot(x1 - x0, y1 - y0)
    translate(x0, y0) {
        rotate(atan2(y1 - y0, x1 - x0) * 180f / PIf, pivot = Offset.Zero) {
            drawPath(Path().apply { moveTo(0f, 0f); lineTo(1.2f, -r * 0.45f); lineTo(1.2f, r * 0.45f); close() }, Ink)
            drawPath(Path().apply { moveTo(1.1f, -r * 0.42f); lineTo(3.2f, -r); lineTo(3.2f, r); lineTo(1.1f, r * 0.42f); close() }, Color(0xFFF0D3A8))
            drawRect(Gold, Offset(3.2f, -r), Size(len - 6.4f, r * 2))
            drawRect(Color(0xFFE39A1E), Offset(3.2f, r * 0.15f), Size(len - 6.4f, r * 0.85f))
            drawRect(Color(0xFFC8CAD6), Offset(len - 3.2f, -r), Size(1.5f, r * 2))
            oval(Color(0xFFF59AB8), len - 2.4f, -r, 2.8f, r * 2)
        }
    }
}

private fun DrawScope.drawGlasses(p: PoseValues, small: Boolean) {
    // óculos redondos de leitura: acompanham o rosto (menos que os olhos)
    val dx = p.lookX * 0.7f; val dy = p.lookY * 0.5f; val w = if (small) 1.5f else 0.95f; val frame = Color(0xFFB47A1C)
    for (cx in listOf(31f, 69f)) {
        val x = cx + dx; val y = 61f + dy
        oval(Color.White.copy(alpha = 0.10f), x - 7f, y - 6.8f, 14f, 13.6f)
        drawOval(frame, Offset(x - 7f, y - 6.8f), Size(14f, 13.6f), style = Stroke(w))
        if (!small) drawArc(Color.White.copy(alpha = 0.8f), 194f, 61f, false, Offset(x - 5.6f, y - 5.4f), Size(11.2f, 10.8f), style = stroke(0.55f))
    }
    drawPath(Path().apply { moveTo(38f + dx, 60f + dy); quadraticTo(50f + dx, 56.6f + dy, 62f + dx, 60f + dy) }, frame, style = stroke(w))
    line(frame, 24f + dx, 59.5f + dy, 18.5f, 58f, w); line(frame, 76f + dx, 59.5f + dy, 81.5f, 58f, w)
}

private fun DrawScope.drawBowTie(small: Boolean) {
    val x = 50f; val y = 83.4f; val red = Color(0xFFE0475E); val dk = Color(0xFFA8233A)
    for (side in listOf(-1f, 1f)) {
        val p = Path().apply { moveTo(x, y); quadraticTo(x + side * 3f, y - 3.6f, x + side * 6f, y - 2.6f); quadraticTo(x + side * 6.8f, y, x + side * 6f, y + 2.6f); quadraticTo(x + side * 3f, y + 3.6f, x, y); close() }
        drawPath(p, Brush.horizontalGradient(if (side < 0) listOf(red, dk) else listOf(dk, red), startX = if (side < 0) x - 6f else x, endX = if (side < 0) x else x + 6f))
        if (!small) drawPath(Path().apply { moveTo(x + side * 1.5f, y - 0.6f); quadraticTo(x + side * 3.6f, y - 1.2f, x + side * 5f, y - 0.4f) }, Color.White.copy(alpha = 0.35f), style = stroke(0.35f))
    }
    oval(dk, x - 1.6f, y - 1.9f, 3.2f, 3.8f)
    drawCircle(Color.White.copy(alpha = 0.35f), 0.45f, Offset(x - 0.5f, y - 0.7f))
}

private fun DrawScope.drawLaurels(small: Boolean) {
    // coroa de louros dourada: dois ramos abraçando a base do capelo
    val n = if (small) 4 else 6
    for (side in listOf(-1f, 1f)) {
        val p0 = Offset(50f + side * 3f, 45f); val p1 = Offset(50f + side * 15f, 45.5f); val p2 = Offset(50f + side * 16.5f, 33f)
        fun at(u: Float) = Offset((1 - u) * (1 - u) * p0.x + 2 * (1 - u) * u * p1.x + u * u * p2.x, (1 - u) * (1 - u) * p0.y + 2 * (1 - u) * u * p1.y + u * u * p2.y)
        drawPath(Path().apply { moveTo(p0.x, p0.y); quadraticTo(p1.x, p1.y, p2.x, p2.y) }, Color(0xFFB07A12), style = stroke(if (small) 0.9f else 0.55f))
        for (i in 1..n) {
            val u = i / n.toFloat(); val a = at(u); val b = at(minOf(1f, u + 0.02f)); val tang = atan2(b.y - a.y, b.x - a.x)
            for (k in if (i == n) listOf(0f) else listOf(-1f, 1f)) {
                translate(a.x, a.y) {
                    rotate((tang + k * 0.75f) * 180f / PIf, pivot = Offset.Zero) {
                        drawOval(Brush.horizontalGradient(listOf(Color(0xFFC98A12), Gold, Color(0xFFFFE3A0)), startX = 0f, endX = 3.2f), Offset(-0.4f, -0.85f), Size(3.8f, 1.7f))
                        if (!small) drawOval(Color(0x59784600), Offset(-0.4f, -0.85f), Size(3.8f, 1.7f), style = Stroke(0.22f))
                    }
                }
            }
        }
    }
}

private fun star5(x: Float, y: Float, r: Float, sy: Float = 1f) = Path().apply {
    for (i in 0 until 10) { val a = -PIf / 2 + i * PIf / 5; val rr = if (i % 2 == 1) r * 0.45f else r; val px = x + cos(a) * rr; val py = y + sin(a) * rr * sy; if (i == 0) moveTo(px, py) else lineTo(px, py) }
    close()
}

private fun DrawScope.drawStarPin(t: Float, small: Boolean) {
    val s = star5(37f, 28.4f, if (small) 4f else 3.2f, 0.62f)
    drawPath(s, Brush.verticalGradient(listOf(Color(0xFFFFF1BF), Gold, Color(0xFFC98A12)), startY = 25f, endY = 31f))
    drawPath(s, Color(0x73784600), style = Stroke(0.3f))
    if (!small) { val k = maxOf(0f, sin(t * 1.7f)).pow(6); if (k > 0.02f) star4(38.6f, 26.8f, 2.2f * k, Color.White.copy(alpha = 0.95f * k)) }
}

private fun DrawScope.drawStreakMedal(t: Float, days: Int, tilt: Float) {
    // medalha da sequência pendurada na aba do capelo: bronze (7+), prata (30+), ouro (100+)
    val m = when { days >= 100 -> listOf(Color(0xFFFFF1BF), Gold, Color(0xFFB97E12)); days >= 30 -> SILVER_STOPS; else -> listOf(Color(0xFFF6C79A), Color(0xFFD08B4E), Color(0xFF94562A)) }
    val sw = sin(t * 2.2f + 1f) * 1.2f - tilt * 0.4f; val x = 25.5f; val y = 31f; val mx = x + sw * 0.4f; val my = y + 8f
    drawPath(Path().apply { moveTo(x - 1.2f, y); lineTo(x + 1.2f, y); lineTo(mx + 0.9f, my - 2f); lineTo(mx - 0.9f, my - 2f); close() }, Color(0xFFE0475E))
    line(Color.White.copy(alpha = 0.6f), x, y + 0.2f, mx, my - 2.2f, 0.35f)
    drawCircle(m[2], 2.9f, Offset(mx, my + 0.3f))
    drawCircle(Brush.verticalGradient(m, startY = my - 2.6f, endY = my + 2.6f), 2.6f, Offset(mx, my))
    drawPath(Path().apply { moveTo(mx, my - 1.6f); quadraticTo(mx + 1.5f, my, mx, my + 1.4f); quadraticTo(mx - 1.5f, my, mx, my - 1.6f) }, m[2].copy(alpha = 0.9f))
}

private fun DrawScope.drawNightcap(t: Float, tilt: Float, vy: Float) {
    // touca de dormir listrada (à noite, parado ou cochilando), com pompom que balança
    val sw = sin(t * 1.6f) * 1.6f - tilt * 0.6f + clamp(vy * 0.05f, -3f, 3f); val tx = 79f + sw; val ty = 35f + abs(sw) * 0.3f
    val cone = Path().apply {
        moveTo(36.5f, 41f); quadraticTo(38f, 23f, 55f, 21.5f); quadraticTo(70f + sw * 0.4f, 21f, tx, ty)
        quadraticTo(68f + sw * 0.3f, 28f, 62.5f, 31f); quadraticTo(63f, 36f, 63.5f, 41f); close()
    }
    clipPath(cone) {
        drawRect(Color(0xFF4B3FD8), Offset(20f, 10f), Size(70f, 40f))
        for (i in -6 until 12) { val x = 20f + i * 6f; drawPath(Path().apply { moveTo(x, 50f); lineTo(x + 3f, 50f); lineTo(x + 23f, 10f); lineTo(x + 20f, 10f); close() }, Color(0xFFDCD7FA)) }
        drawRect(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.18f), Color(0x400E0A45)), startY = 20f, endY = 42f), Offset(20f, 10f), Size(70f, 40f))
    }
    drawPath(cone, Color(0x590E0A45), style = stroke(0.4f))
    val rim = Path().apply { moveTo(35f, 41.5f); quadraticTo(50f, 37f, 65f, 41.5f); quadraticTo(66.5f, 44f, 64.5f, 45.4f); quadraticTo(50f, 41.4f, 35.5f, 45.4f); quadraticTo(33.5f, 44f, 35f, 41.5f) }
    drawPath(rim, Brush.verticalGradient(listOf(Color.White, Color(0xFFE4E0F2)), startY = 38f, endY = 46f))
    drawPath(rim, Color(0x330E0A45), style = stroke(0.3f))
    drawCircle(Brush.radialGradient(listOf(Color.White, Color(0xFFD9D4EE)), center = Offset(tx - 1f, ty), radius = 3.4f), 3f, Offset(tx, ty + 1f))
}

private fun DrawScope.drawRelief(tm: Float) {
    // "ufa": nuvenzinha de ar saindo da boca e gota de suor voando da testa
    val a = clamp((tm - 0.5f) / 1.4f, 0f, 1f)
    if (a > 0f && a < 1f) for (i in 0 until 3) {
        val u = clamp(a * 1.4f - i * 0.18f, 0f, 1f); if (u <= 0f || u >= 1f) continue
        val r = 1.4f + u * 2.4f - i * 0.3f; val cx = 44f - u * 16f - i * 2.5f; val cy = 75f + u * 4f + i
        drawCircle(Color(0xFFAAA5D7).copy(alpha = 0.35f * (1 - u)), r, Offset(cx, cy + 0.4f))
        drawCircle(Color.White.copy(alpha = 0.85f * (1 - u)), r, Offset(cx, cy))
    }
    val d = clamp((tm - 0.9f) / 0.8f, 0f, 1f)
    if (d > 0f && d < 1f) {
        val x = 74f + d * 12f; val y = 50f - sin(PIf * d) * 7f + d * 6f
        drawPath(Path().apply { moveTo(x, y - 2f); quadraticTo(x + 1.6f, y + 0.4f, x, y + 1.1f); quadraticTo(x - 1.6f, y + 0.4f, x, y - 2f) }, Color(0xFF7EC8FF).copy(alpha = 1f - d * 0.6f))
    }
}

private fun DrawScope.drawSweat(t: Float) {
    // sequência em risco: uma gotinha de suor de vez em quando
    val ph = (t % 4.5f) / 4.5f; if (ph > 0.55f) return
    val a = if (ph < 0.1f) ph / 0.1f else 1f - maxOf(0f, ph - 0.4f) / 0.15f; val y = 51f + ph * 6f
    drawPath(Path().apply { moveTo(80f, y - 2.2f); quadraticTo(81.8f, y + 0.4f, 80f, y + 1.2f); quadraticTo(78.2f, y + 0.4f, 80f, y - 2.2f) }, Color(0xFF7EC8FF).copy(alpha = 0.9f * a))
    drawCircle(Color.White.copy(alpha = 0.8f * a), 0.35f, Offset(79.6f, y - 0.1f))
}

private fun DrawScope.drawMug(t: Float) {
    // de manhã: a caneca de café do lado do livro, fumegando
    val x = 99f; val y = 85.5f; val w = 8.4f; val h = 9.2f
    scale(1f, 0.22f, pivot = Offset(x + w / 2f, 95f)) { drawCircle(Ink.copy(alpha = 0.22f), 6f, Offset(x + w / 2f, 95f)) }
    drawArc(Color(0xFFE9E5F5), -90f, 180f, false, Offset(x + w + 0.2f - 2.6f, y + 4.4f - 2.8f), Size(5.2f, 5.6f), style = stroke(1.5f))
    val body = Path().apply {
        moveTo(x, y); lineTo(x + w, y); lineTo(x + w - 0.3f, y + h - 1.6f); quadraticTo(x + w - 0.5f, y + h, x + w - 2f, y + h)
        lineTo(x + 2f, y + h); quadraticTo(x + 0.5f, y + h, x + 0.3f, y + h - 1.6f); close()
    }
    drawPath(body, Brush.horizontalGradient(listOf(Color.White, Color(0xFFF2EFFA), Color(0xFFD9D4EE)), startX = x, endX = x + w))
    clipPath(body) { drawRect(Color(0xFF3A2FC4), Offset(x, y + 3.6f), Size(w, 2.4f)) }
    oval(Color(0xFF6B4226), x + 0.5f, y - 0.7f, w - 1f, 1.8f); oval(Color(0x59FFE6C8), x + 1.6f, y - 0.5f, 2.4f, 0.6f)
    drawPath(body, Ink.copy(alpha = 0.25f), style = Stroke(0.3f))
    for (i in 0 until 2) {
        val ph = (t * 0.45f + i * 0.5f) % 1f; val a = sin(PIf * ph) * 0.55f; val sx = x + 2.6f + i * 3f
        val st = Path().apply { moveTo(sx, y - 1.5f - ph * 3f); cubicTo(sx - 1.5f, y - 3f - ph * 4f, sx + 1.5f, y - 5f - ph * 4f, sx, y - 7f - ph * 5f) }
        drawPath(st, Color.White.copy(alpha = a), style = stroke(0.8f)); drawPath(st, Color(0xFF786EAA).copy(alpha = a * 0.35f), style = stroke(0.3f))
    }
}

private fun DrawScope.drawMoon(t: Float) {
    // à noite: lua minguante e duas estrelinhas piscando
    val moon = Path().apply { addOval(Rect(6f, 8f, 16f, 18f)) }
    val bite = Path().apply { addOval(Rect(9f, 6.6f, 18.2f, 15.8f)) }
    drawPath(Path.combine(androidx.compose.ui.graphics.PathOperation.Difference, moon, bite), Brush.verticalGradient(listOf(Color(0xFFFFF4C8), Color(0xFFFFD877)), startY = 8f, endY = 18f))
    star4(21f, 7f, 1.3f + 0.5f * sin(t * 2.1f), Color(0xE6FFE696))
    star4(4f, 22f, 1f + 0.4f * sin(t * 1.7f + 2f), Color(0xCCFFE696))
}

private fun DrawScope.drawFlames(t: Float, small: Boolean) {
    // em chamas: labaredas tremulando dos dois lados
    for (i in 0 until if (small) 2 else 4) {
        val side = if (i % 2 == 1) 1f else -1f; val row = i / 2
        val x = 50f + side * (44f + row * 6f); val base = 92f - row * 14f; val hgt = 9f + 2.5f * sin(t * 9f + i * 1.7f) + (if (small) 3f else 0f); val wd = 3.4f + (if (small) 1f else 0f)
        val p = Path().apply {
            moveTo(x - wd, base); cubicTo(x - wd * 1.1f, base - hgt * 0.5f, x - wd * 0.2f + sin(t * 7f + i) * 0.8f, base - hgt * 0.7f, x + sin(t * 6f + i) * 1.2f, base - hgt)
            cubicTo(x + wd * 0.6f, base - hgt * 0.6f, x + wd * 1.1f, base - hgt * 0.45f, x + wd, base); quadraticTo(x, base + 1.8f, x - wd, base)
        }
        drawPath(p, Brush.verticalGradient(listOf(Color(0xFFFFE07A), Color(0xFFFF9F3A), Color(0xFFF2552C)), startY = base - hgt, endY = base))
        oval(Color(0xD9FFF8C8), x - wd * 0.45f, base - hgt * 0.28f - hgt * 0.26f, wd * 0.9f, hgt * 0.52f)
    }
}

private fun DrawScope.star4(x: Float, y: Float, r: Float, color: Color) {
    drawPath(Path().apply { moveTo(x, y - r); quadraticTo(x, y, x + r, y); quadraticTo(x, y, x, y + r); quadraticTo(x, y, x - r, y); quadraticTo(x, y, x, y - r) }, color)
}
private fun DrawScope.drawSparkles(t: Float, small: Boolean) {
    val spots = listOf(14f to 30f, 86f to 26f, 8f to 52f, 93f to 50f)
    for (i in 0 until if (small) 2 else 4) {
        val ph = (t * 0.9f + i * 0.27f) % 1f; val a = sin(PIf * ph)
        star4(spots[i].first, spots[i].second, (if (small) 3.4f else 2.6f) * a, Gold.copy(alpha = 0.95f * a))
    }
}
/** "zZ" subindo, desenhado como traço (sem fonte). */
private fun DrawScope.drawZzz(t: Float, small: Boolean) {
    for (i in 0 until if (small) 2 else 3) {
        val ph = (t * 0.35f + i / 3f) % 1f; val a = sin(PIf * ph); val s = 1.6f + ph * 2.2f
        val x = 76f + ph * 14f + sin(ph * 6f) * 2f; val y = 34f - ph * 22f
        drawPath(Path().apply { moveTo(x - s, y - s); lineTo(x + s, y - s); lineTo(x - s, y + s); lineTo(x + s, y + s) }, Ink.copy(alpha = a), style = stroke(if (small) 1.4f else 0.9f))
    }
}
private fun DrawScope.drawDizzyStars(t: Float) {
    for (i in 0 until 4) { val a = t * 3.2f + i * PIf / 2f; star4(50f + cos(a) * 22f, 22f + sin(a) * 5f, 2.4f, if (i % 2 == 1) Spark else Mint) }
}
private fun DrawScope.drawFocusMarks(t: Float) {
    // rabisco aparecendo debaixo do lápis
    val n = floor(((t * 0.45f) % 1f) * 24f).toInt()
    val p = Path(); for (i in 0..n) { val x = 60f + i * 0.95f; val y = 81.5f + sin(i * 1.7f) * 0.7f; if (i == 0) p.moveTo(x, y) else p.lineTo(x, y) }
    drawPath(p, Ink.copy(alpha = 0.55f), style = stroke(0.6f))
}
private fun DrawScope.drawCelebration(t: Float, small: Boolean) {
    val colors = listOf(Spark, Mint, Cheek)
    for (i in 0 until if (small) 3 else 6) {
        val ph = (t * 0.7f + i * 0.17f) % 1f; val side = if (i % 2 == 0) -1f else 1f
        val x = 50f + side * (30f + i * 3f); val y = 70f - ph * 50f; val rr = 2.6f * (1 - ph) * 0.9f
        val col = colors[i % 3].copy(alpha = 1f - ph)
        if (i % 3 == 2) rotate((t * 4f + i) * 180f / PIf, pivot = Offset(x, y)) { drawRect(col, Offset(x - rr, y - rr * 0.5f), Size(rr * 2, rr)) }
        else drawCircle(col, rr, Offset(x, y))
    }
}

// ---- braços e mãos
private fun DrawScope.drawArms(f: FolhaFrame, which: String) {
    val p = f.p
    if ('L' in which) drawArm(Offset(12f, 66f), Offset(p.hL0, p.hL1), p.bendL, -1f, p.shapeL, p.rotL, f.popL, f.small)
    if ('R' in which) drawArm(Offset(88f, 66f), Offset(p.hR0, p.hR1), p.bendR, 1f, p.shapeR, p.rotR, f.popR, f.small)
}

private fun DrawScope.drawPencil(x: Float, y: Float, small: Boolean) {
    rotate(0.5f * 180f / PIf, pivot = Offset(x, y)) {
        val l = 12f; val r = if (small) 1.3f else 1.05f
        drawRect(Gold, Offset(x - r, y - l * 0.75f), Size(r * 2, l * 0.75f))
        drawRect(Color(0xFFE39A1E), Offset(x + r * 0.2f, y - l * 0.75f), Size(r * 0.8f, l * 0.75f))
        drawRect(Color(0xFFC8CAD6), Offset(x - r, y - l * 0.75f - 1.4f), Size(r * 2, 1.4f))
        oval(Color(0xFFF59AB8), x - r, y - l * 0.75f - 3.3f, r * 2, 2.2f)
        drawPath(Path().apply { moveTo(x - r, y); lineTo(x + r, y); lineTo(x, y + 3.1f); close() }, Color(0xFFF0D3A8))
        drawPath(Path().apply { moveTo(x - 0.45f, y + 2.1f); lineTo(x + 0.45f, y + 2.1f); lineTo(x, y + 3.2f); close() }, Ink)
    }
}

/** Braço de borracha que afina, com "cotovelo" ([bend]) e brilho em cima; mão de luva na ponta. */
private fun DrawScope.drawArm(sh: Offset, h: Offset, bend: Float, side: Float, shape: Hand, rot: Float, pop: Float, small: Boolean) {
    val mx = (sh.x + h.x) / 2f; val my = (sh.y + h.y) / 2f; val dx = h.x - sh.x; val dy = h.y - sh.y
    val len = hypot(dx, dy).takeIf { it > 0f } ?: 1f
    val nx = -dy / len; val ny = dx / len
    val cx = mx + nx * bend * side + side * 1.5f; val cy = my + ny * bend * side + 2f
    val n = 14; val w0 = if (small) 2.3f else 1.9f; val w1 = if (small) 1.7f else 1.3f
    val l1 = ArrayList<Offset>(); val r1 = ArrayList<Offset>()
    for (i in 0..n) {
        val u = i / n.toFloat(); val a = (1 - u) * (1 - u); val b = 2 * (1 - u) * u; val c = u * u
        val x = a * sh.x + b * cx + c * h.x; val y = a * sh.y + b * cy + c * h.y
        val tx = 2 * (1 - u) * (cx - sh.x) + 2 * u * (h.x - cx); val ty = 2 * (1 - u) * (cy - sh.y) + 2 * u * (h.y - cy)
        val tl = hypot(tx, ty).takeIf { it > 0f } ?: 1f; val w = lerp(w0, w1, u)
        l1.add(Offset(x - ty / tl * w, y + tx / tl * w)); r1.add(Offset(x + ty / tl * w, y - tx / tl * w))
    }
    drawPath(Path().apply { moveTo(l1[0].x, l1[0].y); l1.forEach { lineTo(it.x, it.y) }; r1.asReversed().forEach { lineTo(it.x, it.y) }; close() }, Arm)
    val edge = if (side < 0) r1 else l1
    drawPath(Path().apply { moveTo(edge[1].x, edge[1].y); for (i in 2 until n - 1) lineTo(edge[i].x, edge[i].y) }, ArmLight.copy(alpha = 0.7f), style = stroke(0.6f))
    if (shape == Hand.PEN) drawPencil(h.x - 2.4f, h.y + 3.2f, small)
    // mão: na direção do fim do braço, mais o giro do humor; "pop" quando troca de forma
    val ang = atan2(h.y - cy, h.x - cx) * 180f / PIf + rot
    val sc = (if (small) 1.12f else 1f) * (1f + 0.28f * pop)
    translate(h.x, h.y) {
        rotate(ang, pivot = Offset.Zero) {
            scale(sc, if (side < 0) -sc else sc, pivot = Offset.Zero) { drawHand(shape, small) }
        }
    }
}

private fun DrawScope.drawHand(shape: Hand, small: Boolean) {
    val ow = if (small) 1.0f else 0.6f
    drawOval(Color(0xFFECE8F6), Offset(-2.8f, -2.5f), Size(3.2f, 5f)); drawOval(Arm, Offset(-2.8f, -2.5f), Size(3.2f, 5f), style = Stroke(ow))
    line(Gold, -1.2f, -2.2f, -1.2f, 2.2f, 0.7f, false)
    fun outline(path: Path) { drawPath(path, Color.White); drawPath(path, Arm, style = Stroke(ow, join = StrokeJoin.Round)) }
    fun finger(x: Float, y: Float, a: Float, len: Float, r: Float) {
        val e = Offset(x + cos(a) * len, y + sin(a) * len)
        drawLine(Arm, Offset(x, y), e, r * 2 + ow * 2, StrokeCap.Round); drawLine(Color.White, Offset(x, y), e, r * 2, StrokeCap.Round)
    }
    when (shape) {
        Hand.OPEN -> {
            listOf(-0.95f, -0.35f, 0.25f, 0.85f).forEachIndexed { i, a -> finger(2.2f, 0f, a * 0.8f, 2.6f + if (i == 1 || i == 2) 0.5f else 0f, 0.95f) }
            finger(0.9f, -1.8f, -1.9f, 1.9f, 0.95f)
            outline(ellipsePath(1.6f, 0f, 2.6f, 2.5f))
            if (!small) line(Arm.copy(alpha = 0.25f), 2.6f, -0.8f, 3.1f, 0.8f, 0.35f)
        }
        Hand.FIST, Hand.PEN -> {
            outline(Path().apply { addOval(Rect(1.2f - 1.5f, -2.3f - 1.0f, 1.2f + 1.5f, -2.3f + 1.0f)) })
            outline(ellipsePath(1.7f, 0f, 3.1f, 2.9f))
            if (!small) drawPath(Path().apply { moveTo(3.4f, -1.1f); quadraticTo(4.4f, 0f, 3.4f, 1.1f) }, Arm.copy(alpha = 0.3f), style = stroke(0.4f))
        }
        Hand.POINT -> {
            finger(3.2f, -0.6f, 0f, 4.2f, 0.95f)
            outline(Path().apply { addOval(Rect(1.4f - 1.4f, -1.9f - 0.9f, 1.4f + 1.4f, -1.9f + 0.9f)) })
            outline(ellipsePath(1.6f, 0.4f, 2.8f, 2.5f))
        }
        Hand.MITT -> {   // luva relaxada: a bolinha de sempre, com polegar
            outline(Path().apply { addOval(Rect(1.0f - 1.2f, -2.6f - 1.6f, 1.0f + 1.2f, -2.6f + 1.6f)) })
            outline(ellipsePath(1.7f, 0f, 3.1f, 3.0f))
            if (!small) drawPath(Path().apply { moveTo(3.4f, -1.2f); quadraticTo(4.6f, 0f, 3.6f, 1.4f) }, Arm.copy(alpha = 0.3f), style = stroke(0.4f))
        }
    }
}
