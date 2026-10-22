package br.com.estudario.ui.assistant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.clipRect
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
    /** Mostrando o que tem nas mãos (vitrine do armário): item para a frente, luva aberta. */
    PRESENT,
}

/**
 * O visual do Folha no app inteiro: as peças escolhidas no armário (FolhaWardrobe) e o contexto do
 * dia. O app mantém em dia (FolhaLookSync); todos os Folhas leem daqui a cada quadro.
 */
object FolhaLook {
    var cover by mutableStateOf("indigo")
    var ribbon by mutableStateOf("menta")
    var hat by mutableStateOf("capelo")
    var eyes by mutableStateOf("nenhum")
    var neck by mutableStateOf("nenhum")
    var hand by mutableStateOf("nenhum")
    var aura by mutableStateOf("nenhum")
    var gloves by mutableStateOf("branca")
    var pet by mutableStateOf("nenhum")
    var back by mutableStateOf("nada")
    var pages by mutableStateOf("lisa")
    var ink by mutableStateOf("marinho")
    var cheeks by mutableStateOf("rosa")
    /** Dias seguidos: medalha no capelo a partir de 7 (bronze), 30 (prata) e 100 (ouro). */
    var streak by mutableIntStateOf(0)
    /** A sequência pode cair hoje (fim do dia, meta não batida): parado, ele fica preocupadinho. */
    var atRisk by mutableStateOf(false)
    /** A amizade com o Folha (FolhaBond): libera gestos novos ao toque. */
    var bond by mutableIntStateOf(0)
    /** Primeiro nome da pessoa, para ele chamar pelo nome. */
    var name by mutableStateOf("")
    /** Falas de amigo (FolhaMemory.bondTapLines), sorteadas junto com as de sempre ao tocar nele. */
    var bondLines by mutableStateOf<List<String>>(emptyList())
    /** Só para testes: força a hora do dia (manhã tem café, noite tem touca). */
    var hour by mutableStateOf<Int?>(null)

    /** Peça atual de cada lugar, pela chave do desenho ("cover", "hat"...). */
    fun pieces(): Map<String, String> = mapOf("cover" to cover, "ribbon" to ribbon, "hat" to hat, "eyes" to eyes, "neck" to neck, "hand" to hand, "aura" to aura, "back" to back, "gloves" to gloves, "pet" to pet, "pages" to pages, "ink" to ink, "cheeks" to cheeks)

    fun set(key: String, id: String) {
        when (key) {
            "cover" -> cover = id
            "ribbon" -> ribbon = id
            "hat" -> hat = id
            "eyes" -> eyes = id
            "neck" -> neck = id
            "hand" -> hand = id
            "aura" -> aura = id
            "gloves" -> gloves = id
            "pet" -> pet = id
            "back" -> back = id
            "pages" -> pages = id
            "ink" -> ink = id
            "cheeks" -> cheeks = id
        }
    }
}

/**
 * Vitrine do armário: veste peças só neste Folha ([pieces]: "hat" to "coroa"...). [plain] tira medalha,
 * café e touca; [noScene] tira só café e touca.
 */
data class FolhaStyle(val pieces: Map<String, String> = emptyMap(), val plain: Boolean = false, val noScene: Boolean = false)

/** Capa do livro: degradê (claro, meio, escuro), sombra de baixo, cor dos braços, metal das cantoneiras e textura. */
private class CoverStyle(val c: List<Color>, val under: Color, val arm: List<Color>, val silver: Boolean = false, val pattern: String = "", val stitch: Color? = null)

private fun rgb(v: Long) = Color(v)
private val COVERS = mapOf(
    "indigo" to CoverStyle(listOf(rgb(0xFF5247E0), rgb(0xFF3A2FC4), rgb(0xFF2A209E)), rgb(0xFF1A1370), listOf(rgb(0xFF3326CE), rgb(0xFF6158F0))),
    "esmeralda" to CoverStyle(listOf(rgb(0xFF36C897), rgb(0xFF1F9E75), rgb(0xFF147A5A)), rgb(0xFF0B4F3A), listOf(rgb(0xFF178A63), rgb(0xFF4FD3A2))),
    "rubi" to CoverStyle(listOf(rgb(0xFFE8566D), rgb(0xFFC42F4B), rgb(0xFF961D37)), rgb(0xFF5E0F22), listOf(rgb(0xFFB3263F), rgb(0xFFEE6B80))),
    "ametista" to CoverStyle(listOf(rgb(0xFFAA68EE), rgb(0xFF8746D3), rgb(0xFF662DAB)), rgb(0xFF3E1670), listOf(rgb(0xFF7438C4), rgb(0xFFB07CF0))),
    "bolinhas" to CoverStyle(listOf(rgb(0xFFFF8FA3), rgb(0xFFF06A85), rgb(0xFFD14D6B)), rgb(0xFF7A1F35), listOf(rgb(0xFFD9506D), rgb(0xFFFF9DB0)), silver = true, pattern = "dots"),
    "couro" to CoverStyle(listOf(rgb(0xFFBC7D4E), rgb(0xFF965A31), rgb(0xFF6D3D1F)), rgb(0xFF43240E), listOf(rgb(0xFF82491F), rgb(0xFFC08453)), pattern = "leather", stitch = Color(0xBFFFE2B4)),
    "jeans" to CoverStyle(listOf(rgb(0xFF5B86C4), rgb(0xFF3F6AA8), rgb(0xFF2C4F84)), rgb(0xFF1B3358), listOf(rgb(0xFF355E99), rgb(0xFF6E98D4)), silver = true, pattern = "denim", stitch = Color(0xE6F2A64A)),
    "xadrez" to CoverStyle(listOf(rgb(0xFFD2463C), rgb(0xFFB0322B), rgb(0xFF86231E)), rgb(0xFF4F1410), listOf(rgb(0xFFA12C26), rgb(0xFFDE6A5F)), pattern = "plaid"),
    "grafite" to CoverStyle(listOf(rgb(0xFF61667C), rgb(0xFF43475C), rgb(0xFF2D3042)), rgb(0xFF1A1C28), listOf(rgb(0xFF3A3E52), rgb(0xFF6B7088)), silver = true),
    "noite" to CoverStyle(listOf(rgb(0xFF36388A), rgb(0xFF212059), rgb(0xFF121138)), rgb(0xFF07061E), listOf(rgb(0xFF26246A), rgb(0xFF4B4AA8)), silver = true, pattern = "stars"),
    "holografica" to CoverStyle(listOf(rgb(0xFFC9B6FF), rgb(0xFF9FD8FF), rgb(0xFFFFC2E2)), rgb(0xFF5B4C9A), listOf(rgb(0xFF8E7CE0), rgb(0xFFC7B8FF)), silver = true, pattern = "holo"),
    "ouro" to CoverStyle(listOf(rgb(0xFFFFDD84), rgb(0xFFEBB13A), rgb(0xFFBD8214)), rgb(0xFF7A4F05), listOf(rgb(0xFFC98A12), rgb(0xFFFFD978)), silver = true, pattern = "shine"),
    "obsidiana" to CoverStyle(listOf(rgb(0xFF34343F), rgb(0xFF1B1B24), rgb(0xFF0C0C12)), rgb(0xFF000000), listOf(rgb(0xFF22222C), rgb(0xFF4A4A5C)), pattern = "foil"),
    "tweed" to CoverStyle(listOf(rgb(0xFF9A8A78), rgb(0xFF7A6B5B), rgb(0xFF5A4D40)), rgb(0xFF2E241B), listOf(rgb(0xFF6A5B4C), rgb(0xFFA4937F)), silver = true, pattern = "tweed", stitch = Color(0xB3E8D6BA)),
    "real" to CoverStyle(listOf(rgb(0xFF8A4FD6), rgb(0xFF6430A8), rgb(0xFF45187C)), rgb(0xFF22093F), listOf(rgb(0xFF5A2A9C), rgb(0xFF9A6BE0)), pattern = "fleur"),
    "diamante" to CoverStyle(listOf(rgb(0xFFEAF6FF), rgb(0xFFB9DDF7), rgb(0xFF86BCE6)), rgb(0xFF4F7FA6), listOf(rgb(0xFF6FA9D9), rgb(0xFFBFE3FF)), silver = true, pattern = "facets"),
)

/** Luvas: preenchimento e punho ("arcoiris" tem degradê; "boxe" muda o formato da mão). */
private val GLOVES = mapOf(
    "branca" to listOf(Color.White, rgb(0xFFECE8F6)), "menta" to listOf(rgb(0xFFC9F7E4), rgb(0xFF7EE0B8)), "coral" to listOf(rgb(0xFFFFD3C9), rgb(0xFFFF8C75)),
    "preta" to listOf(rgb(0xFF3A3A46), rgb(0xFF24242E)), "dourada" to listOf(rgb(0xFFFFE9A6), rgb(0xFFF5B83D)),
    "arcoiris" to listOf(Color.White, rgb(0xFFB995F5)), "boxe" to listOf(rgb(0xFFE5373F), Color.White),
)

private class Look(
    val cover: CoverStyle, val ribbon: List<Color>, val hat: String, val eyes: String, val neck: String, val hand: String, val aura: String,
    val gloves: List<Color>, val streak: Int, val atRisk: Boolean, val morning: Boolean, val night: Boolean,
    val gloveKind: String = "branca", val pet: String = "nenhum", val back: String = "nada",
    val pages: String = "lisa", val ink: Color = Color(0xFF26215C), val cheek: Color? = Color(0xFFF4A6C0),
)

/** O próprio Folha: tinta dos olhos, sobrancelhas e boca, e cor das bochechas. */
private val INKS = mapOf(
    "marinho" to Color(0xFF26215C), "azul" to Color(0xFF1F4FD8), "vermelha" to Color(0xFFC42F4B), "verde" to Color(0xFF0F8A5F), "grafite" to Color(0xFF3A3A44),
    "roxa" to Color(0xFF6D3CE0), "cafe" to Color(0xFF6B3E1E), "ameixa" to Color(0xFF7A1F6A), "floresta" to Color(0xFF14523A), "dourada" to Color(0xFFA8700A), "nanquim" to Color(0xFF0B0B10),
)
private val CHEEKS = mapOf("rosa" to Color(0xFFF4A6C0), "pessego" to Color(0xFFFFB98A), "lilas" to Color(0xFFC9A6F4), "coral" to Color(0xFFFF8C75), "nenhuma" to null)

private fun resolveLook(o: FolhaStyle?): Look {
    val h = FolhaLook.hour ?: java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val plain = o?.plain == true
    val scene = !plain && o?.noScene != true
    val cur = FolhaLook.pieces()
    fun a(k: String) = o?.pieces?.get(k) ?: cur.getValue(k)
    return Look(
        cover = COVERS[a("cover")] ?: COVERS.getValue("indigo"),
        ribbon = ribbonColors(a("ribbon")),
        hat = a("hat"), eyes = a("eyes"), neck = a("neck"), hand = a("hand"), aura = a("aura"),
        gloves = GLOVES[a("gloves")] ?: GLOVES.getValue("branca"),
        streak = if (plain) 0 else FolhaLook.streak,
        atRisk = !plain && FolhaLook.atRisk,
        morning = scene && h in 5..10,
        night = scene && (h >= 22 || h < 5),
        gloveKind = a("gloves"), pet = a("pet"), back = a("back"),
        pages = a("pages"), ink = INKS[a("ink")] ?: INKS.getValue("marinho"), cheek = if (a("cheeks") in CHEEKS) CHEEKS[a("cheeks")] else CHEEKS.getValue("rosa"),
    )
}

private val Ink = Color(0xFF26215C)
private val PageLine = Color(0xFFD8D4E6)
private val Cap = Color(0xFF231C6B)
private val CapTop = Color(0xFF2F2789)
private val Arm = Color(0xFF3326CE)
internal val ArmDefault = Color(0xFF3326CE)
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
    "preta" -> listOf(Color(0xFF111118), Color(0xFF2F2F3A), Color(0xFF77778A))
    "arcoiris" -> listOf(Color(0xFFE8566D), Color(0xFFF5B83D), Color(0xFF36C897), Color(0xFF6FB0FF))
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
    /** A tela já tem um balão de conversa com ele: a fala do toque vai para lá (sem balão em cima dele). */
    onQuip: ((String) -> Unit)? = null,
) {
    val sim = remember { FolhaSim().also { it.enterPending = enter } }
    var frame by remember { mutableLongStateOf(0L) }
    sim.small = size < SMALL
    // grande e tocável: fala, pensa, sente carinho e cumprimenta quando você volta
    sim.chatty = onClick != null && size >= 80.dp && style?.noScene != true
    if (sim.chatty) {
        val owner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        androidx.compose.runtime.DisposableEffect(owner) {
            var pausedAt = 0L
            val obs = androidx.lifecycle.LifecycleEventObserver { _, e ->
                if (e == androidx.lifecycle.Lifecycle.Event.ON_PAUSE) pausedAt = System.currentTimeMillis()
                if (e == androidx.lifecycle.Lifecycle.Event.ON_RESUME && pausedAt > 0 && System.currentTimeMillis() - pausedAt > 60_000) sim.greet()
            }
            owner.lifecycle.addObserver(obs)
            onDispose { owner.lifecycle.removeObserver(obs) }
        }
    }
    sim.setMood(mood)
    sim.setLeaving(leaving)
    LaunchedEffect(say) { sim.setSay(say, sayMillis) }
    // Economia de bateria: com o modo economia do aparelho ligado, no máximo 30 quadros por segundo.
    val context = androidx.compose.ui.platform.LocalContext.current
    val powerSave = remember { runCatching { (context.getSystemService(android.content.Context.POWER_SERVICE) as android.os.PowerManager).isPowerSaveMode }.getOrDefault(false) }
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) withFrameNanos { now ->
            // calmo e parado (respirando, piscando, falando): 30 quadros; cochilando ou pequeno: 20; em movimento: todos
            val cap = sim.fpsCap(powerSave)
            if (cap > 0 && last != 0L && now - last < 1_000_000_000L / cap - 4_000_000L) return@withFrameNanos
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

    val pet = if (sim.chatty) Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dx -> sim.pet(dx, this.size.width.toFloat(), if (change.position.x > this.size.width / 2f) 1f else -1f) }
    } else Modifier
    // a caixa leva o modifier de quem chamou; o balão de fala se ancora nela
    androidx.compose.foundation.layout.Box(modifier) {
    val q = sim.quip
    if (onQuip != null) LaunchedEffect(q) { if (q != null) onQuip(q) }
    if (q != null && onQuip == null) androidx.compose.ui.window.Popup(alignment = androidx.compose.ui.Alignment.TopCenter, offset = androidx.compose.ui.unit.IntOffset(0, -with(androidx.compose.ui.platform.LocalDensity.current) { (size * 0.62f).roundToPx() })) {
        androidx.compose.foundation.layout.Box(
            androidx.compose.ui.Modifier.widthIn(max = maxOf(180.dp, size * 1.5f)).shadow(6.dp, androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                .background(Color.White, androidx.compose.foundation.shape.RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 8.dp),
        ) { androidx.compose.material3.Text(q, color = Ink, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, fontSize = 13.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center) }
    }
    Canvas(
        Modifier
            .size(size)
            .semantics { contentDescription = "Folha, do Estudário" }
            .then(clickable)
            .then(pet),
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
}

/** Quanto dura a saída do Folha (tchau + sumir subindo). */
const val FOLHA_LEAVE_MS = 760L

/**
 * O Folha que fala ao aparecer e depois fica ouvindo. A boca segue as letras de [text], no tempo de
 * ler (mais ou menos) em voz alta.
 */
@Composable
fun FolhaTalking(size: Dp, text: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, onQuip: ((String) -> Unit)? = null, rest: FolhaMood = FolhaMood.IDLE) {
    val total = (text.length * 45L).coerceIn(900L, 3200L)
    var talking by remember(text) { mutableStateOf(true) }
    LaunchedEffect(text) { delay(total); talking = false }
    Folha(size, modifier, if (talking) FolhaMood.TALKING else rest, onClick, say = if (talking) text else null, sayMillis = total, onQuip = onQuip)
}

/**
 * O Folha como imagem, para o ícone grande das notificações (fora do Compose). O ícone pequeno da
 * barra continua monocromático, como o Android exige; este aparece colorido ao lado do texto.
 */
fun folhaBitmap(sizePx: Int, mood: FolhaMood = FolhaMood.HAPPY, style: FolhaStyle = FolhaStyle(plain = true), zoom: Triple<Float, Float, Float>? = null): android.graphics.Bitmap {
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
        val look = resolveLook(style)
        val sim = FolhaSim().apply { small = zoom == null && sizePx < 160; holding = look.hand != "nenhum"; setMood(m); settle(m) }
        if (zoom != null) {
            // de perto: o ponto (x, y) da grade 100 x 100 fica no centro, aumentado z vezes
            val (z, fx, fy) = zoom; val k = size.minDimension / 100f
            translate(size.width / 2f - fx * k * z, size.height / 2f - fy * k * z) { scale(z, z, pivot = Offset.Zero) { drawFolha(sim.snapshot(0f, look)) } }
            return@draw
        }
        val pet = look.pet != "nenhum" && sizePx >= 160
        val wide = look.back != "nada" || look.aura == "holofote" || look.aura == "fogos"
        val fit = when { pet || wide -> 0.76f; m == FolhaMood.PRESENT -> 0.78f; else -> 0.9f }
        translate(size.width * (1 - fit) / 2f + (if (pet) size.width * 0.07f else 0f), size.height * (1 - fit) / 2f + size.width * (when { pet -> 0.01f; m == FolhaMood.PRESENT -> 0.05f; else -> 0.02f })) {
            scale(fit, fit, pivot = Offset.Zero) { drawFolha(sim.snapshot(0f, look)) }
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
private fun pose(mood: FolhaMood, t: Float, lookX: Float, lookY: Float, fid: Fidget?, fu: Float, tm: Float = 9f, worried: Boolean = false, holding: Boolean = false): PoseValues {
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
            "read" -> {   // lê a própria página: os olhos correm a linha
                p.lookY = lerp(p.lookY, 0.85f, e); p.lookX = e * sin(fu * 14f) * 0.8f; p.up = 0.3f * e; p.browY = 0.4f * e
                p.mW = lerp(5f, 3.4f, e); p.mCurve = lerp(3.4f, 1.4f, e); p.tilt += 1.5f * e
            }
            "whistle" -> {   // assobia olhando para cima, balançando no ritmo
                p.mW = lerp(5f, 1.8f, e); p.mCurve = lerp(3.4f, 0f, e); p.mOpen = 0.55f * e; p.lookX = 0.5f * e; p.lookY = -0.6f * e; p.browY = -0.8f * e
                p.tilt += sin(fu * 18f) * 2.2f * e; p.hop = -abs(sin(fu * 9f)) * 1.2f * e
            }
            "peek" -> {   // inclina para o lado e dá um tchauzinho
                val side = fid.side; val wv = sin(t * 13f) * e
                p.tilt = side * 7f * e; p.hop = -1.2f * e; p.low = 0.4f * e; p.mW = 5.6f; p.mCurve = 4.4f; p.mOpen = 0.4f * e; p.cheek = lerp(0.45f, 0.65f, e); p.lookX = side * 0.8f * e; p.lookY = -0.2f * e
                if (side > 0f) { p.hR0 = lerp(p.hR0, 100f, e); p.hR1 = lerp(p.hR1, 32f, e) + wv * 1.4f; if (e > 0.3f) p.shapeR = Hand.OPEN; p.rotR = (-10f + 24f * sin(t * 13f)) * e; p.bendR = -5f * e }
                else { p.hL0 = lerp(p.hL0, 0f, e); p.hL1 = lerp(p.hL1, 32f, e) + wv * 1.4f; if (e > 0.3f) p.shapeL = Hand.OPEN; p.rotL = (10f - 24f * sin(t * 13f)) * e; p.bendL = -5f * e }
            }
            "hatflip" -> {   // joga o chapéu para cima e pega (o chapéu voa no desenho)
                val k = sin(PIf * clamp(fu, 0f, 1f))
                p.lookY = -k; p.lookX = 0f; p.mOpen = 0.5f * k; p.mW = lerp(5f, 3.4f, k); p.mCurve = lerp(3.4f, 2f, k); p.browY = -1.6f * k
                p.hR0 = lerp(p.hR0, 88f, k); p.hR1 = lerp(p.hR1, 42f, k); if (k > 0.3f) p.shapeR = Hand.OPEN; p.rotR = -30f * k; p.bendR = -4f * k
            }
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
        FolhaMood.PRESENT -> {
            // mostrando o que tem nas mãos: a esquerda levanta o item para a frente, a direita abre a luva
            val b = sin(t * 3f)
            p.hop = -1f + sin(t * 2f) * 0.6f; p.tilt = -2f + b * 1.2f
            p.hL0 = 4f; p.hL1 = 45f + b * 1.2f; p.shapeL = Hand.FIST; p.rotL = -10f; p.bendL = -4f
            p.hR0 = 96f; p.hR1 = 46f - b * 1.2f; p.shapeR = Hand.OPEN; p.rotR = -20f + b * 12f; p.bendR = -4f
            p.low = 0.4f; p.browY = -1.6f; p.mW = 5.8f; p.mCurve = 4.6f; p.mOpen = 0.5f; p.cheek = 0.7f; p.lookX = -0.5f; p.lookY = -0.3f
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
    // segurando um item: a mão esquerda parada sobe um pouco para a frente, fechada, para o item aparecer
    if (holding && p.hL0 < 10f && p.hL1 > 73f) { p.hL0 = 11f; p.hL1 = 71f + (p.hL1 - 78f) * 0.5f; p.rotL = -12f; p.bendL = 4f; if (p.shapeL == Hand.MITT) p.shapeL = Hand.FIST }
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
    private var prevShown: FolhaMood? = null
    var worried = false
    var holding = false
    /** Entrada: na primeira pose, começa do alto e cai (a mola e o amasso de aterrissagem fazem o resto). */
    var enterPending = false
    private var leaveAt = -1L
    private var burstAt = -1L
    // vida: reação ao toque, carinho, balão de pensamento e frase do balão
    private var react: String? = null; private var reactAt = 0L; private var lastReact: String? = null
    private var petUntil = 0L; private var petSide = 1f; private var petW = 0f; private var petAcc = 0f; private var wasPet = false
    private var thought: String? = null; private var thoughtAt = 0L; private var nextThought = 0L; private var lastThought: String? = null
    private var quipAt = -100_000L; private var quipUntil = 0L; private var lastDt = 0f; private var bornAt = 0L
    /** A frase do balão agora (a tela mostra em cima dele); nula sem frase. */
    var quip by androidx.compose.runtime.mutableStateOf<String?>(null)
        private set
    /** Folha grande e tocável: fala ao ser tocado e pensa quando fica à toa. */
    var chatty = false

    /** Quadros por segundo de que ele precisa agora (0 = todos). */
    fun fpsCap(powerSave: Boolean): Int {
        val busy = leaveAt >= 0 || nowMs < dizzyUntil || bounceAt >= 0 || fid != null || speaking || !inited || react != null || petW > 0.02f || nowMs < petUntil || thought != null
        val moving = abs(v[0]) + abs(v[3]) + abs(v[5]) > 14f || abs(sqV) > 0.5f
        if (busy || moving) return if (powerSave) 30 else 0
        return when (shown) {
            FolhaMood.SLEEPY -> 20
            FolhaMood.IDLE, FolhaMood.FOCUS, FolhaMood.SAD, FolhaMood.TALKING, FolhaMood.PROUD -> if (small) 20 else 30
            else -> if (powerSave) 30 else 0
        }
    }

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

    /** Toque: amassa e reage de um jeito diferente a cada vez; se for grande, solta uma frase. */
    fun poke() {
        bounceAt = nowMs; idleSince = nowMs
        val all = reactionsNow()
        // o gesto mais novo da amizade aparece mais
        var k = if (all.size > REACTIONS.size && Random.nextInt(100) < 35) all.last().first else all.random().first
        if (k == lastReact) k = all[(all.indexOfFirst { it.first == k } + 1) % all.size].first
        react = k; reactAt = nowMs; lastReact = k
        if (k == "wink") { wink = true; winkUntil = nowMs + 710 }
        say(tapLine())
        taps.removeAll { nowMs - it > 1400 }; taps.add(nowMs)
        if (taps.size >= 4) { dizzyUntil = nowMs + 2200; taps.clear() }
        burstAt = nowMs
    }

    /** Carinho: arrastar o dedo de um lado para o outro em cima dele. [dx] em pixels; [w] a largura dele. */
    fun pet(dx: Float, w: Float, side: Float) {
        petAcc = petAcc * 0.97f + abs(dx)
        if (petAcc > w * 1.5f) { petUntil = nowMs + 700; petSide = side; idleSince = nowMs }
    }

    /** Voltou para o app depois de um tempo: dá um pulo e cumprimenta. */
    fun greet() { react = "hop"; reactAt = nowMs; say(BACK_LINES.random()) }

    /** Uma frase no balão, com a boca acompanhando (só nos grandes e no máximo uma a cada 3,5 s). */
    private fun say(text: String) {
        if (!chatty || nowMs - quipAt < 3500) return
        val total = (text.length * 45L).coerceIn(900L, 2600L)
        setSay(text, total); quipAt = nowMs; quipUntil = nowMs + total + 400; quip = text
    }

    /** Para imagens paradas: pula as molas direto para a pose. */
    fun settle(m: FolhaMood) {
        t = 0.35f; lookX = 0.3f; lookY = -0.2f; moodAt = -9000L
        val p = pose(m, t, lookX, lookY, null, 0f, holding = holding)
        p.values().copyInto(x); shapeL = p.shapeL; shapeR = p.shapeR; shown = m; inited = true
    }

    fun step(now: Long, dt: Float) {
        if (!inited) { nowMs = now; idleSince = now; bornAt = now; nextBlink = now + 1800; nextFid = now + 6000 + Random.nextLong(5000) }
        nowMs = now; t += dt; lastDt = dt
        var m = mood
        if (leaveAt >= 0) m = FolhaMood.WAVE
        else if (now < dizzyUntil) m = FolhaMood.DIZZY
        else if (mood == FolhaMood.IDLE && now - idleSince > 40_000) m = FolhaMood.SLEEPY
        // falando a frase do balão, se ele estiver tranquilo
        if (now < quipUntil && m in QUIP_MOODS) m = FolhaMood.TALKING
        if (quip != null && now > quipUntil + 1400) quip = null
        if (shown == FolhaMood.SLEEPY && m != FolhaMood.SLEEPY) bounceAt = now        // acorda num susto
        if (m != shown && inited) { sqV += 9f; v[3] += 40f; v[5] += 40f }              // antecipação
        if (m != shown || !inited) { moodAt = now; if (inited) prevShown = shown }
        shown = m
        if (wink && now > winkUntil) wink = false
        updateBlink(now); updateLook(now, m)
        val f = fidget(now, m)
        val p = pose(m, t, lookX, lookY, f, if (f != null) (now - f.start).toFloat() / f.dur else 0f, (now - moodAt) / 1000f, worried, holding)
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
            val kinds = listOf("cap" to 1700L, "yawn" to 2300L, "glance" to 1500L, "bounce" to 1100L, "read" to 2600L, "whistle" to 2400L, "peek" to 1800L, "hatflip" to 1500L)
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

    /**
     * A vida por cima da pose: carinho, reação ao toque, chapéu voando, assobio e o balão de pensamento
     * quando ele fica parado à toa. Mexe no quadro [p] e devolve os extras do desenho.
     */
    private fun life(p: PoseValues): Life? {
        if (small) return null
        val now = nowMs
        fun env(u: Float) = sin(PIf * clamp(u, 0f, 1f)).pow(0.6f)
        fun mix(get: () -> Float, set: (Float) -> Unit, v: Float, w: Float) = set(lerp(get(), v, w))
        var turn = 1f; var hatUp = 0f; var hatSpin = 0f; var whistle = 0f; var hearts = 0f; var th: String? = null; var thU = 0f
        var armsFront = false; var starEyes = false; var sparkle = 0f; var goldHearts = false
        val petting = now < petUntil
        petW += ((if (petting) 1f else 0f) - petW) * minOf(1f, lastDt * (if (petting) 9f else 5f))
        if (!petting) petAcc *= 0.9f
        if (petW > 0.01f) {
            val w = petW
            p.low = lerp(p.low, 0.78f, w); p.up = lerp(p.up, 0.1f, w); p.cheek = lerp(p.cheek, 1f, w); p.tilt = lerp(p.tilt, petSide * 6f + sin(t * 9f) * 1.2f, w)
            p.mW = lerp(p.mW, 5.8f, w); p.mCurve = lerp(p.mCurve, 4.8f, w); p.mOpen = lerp(p.mOpen, 0.3f, w); p.hop = lerp(p.hop, -0.8f + sin(t * 9f) * 0.4f, w); p.browY = lerp(p.browY, -1.2f, w)
            hearts = w
            if (petting) wasPet = true else if (wasPet && w < 0.5f) { wasPet = false; say(PET_LINES.random()) }
        }
        react?.let { k ->
            val dur = reactionsNow().firstOrNull { it.first == k }?.second ?: 900L
            val u = (now - reactAt).toFloat() / dur
            if (u >= 1f) react = null else {
                val w = env(u)
                when (k) {
                    "giggle" -> { p.low = lerp(p.low, 0.8f, w); p.up = lerp(p.up, 0.1f, w); p.cheek = lerp(p.cheek, 1f, w); p.mOpen = lerp(p.mOpen, 0.85f, w); p.mW = lerp(p.mW, 6f, w); p.mCurve = lerp(p.mCurve, 5f, w); p.tilt += sin(u * 42f) * 4f * w; p.hop += -abs(sin(u * 21f)) * 1.5f * w }
                    "spin" -> { turn = cos(2f * PIf * (1f - (1f - u).pow(3))); p.hop += -sin(PIf * u) * 6f; p.mOpen = lerp(p.mOpen, 0.6f, w); p.pupil = lerp(p.pupil, 1.3f, w) }
                    "hop" -> {
                        p.hop += -sin(PIf * u) * 13f; hatUp = maxOf(0f, sin(PIf * clamp((u - 0.08f) / 0.92f, 0f, 1f))) * 9f
                        p.low = lerp(p.low, 0.6f, w); p.mOpen = lerp(p.mOpen, 1f, w); p.mW = lerp(p.mW, 6.2f, w); p.mCurve = lerp(p.mCurve, 5f, w); p.pupil = lerp(p.pupil, 1.3f, w)
                        p.hL0 = lerp(p.hL0, 2f, w); p.hL1 = lerp(p.hL1, 42f, w); p.hR0 = lerp(p.hR0, 98f, w); p.hR1 = lerp(p.hR1, 42f, w); if (w > 0.4f) { p.shapeL = Hand.FIST; p.shapeR = Hand.FIST }
                    }
                    "dance" -> {
                        // dancinha: pulinhos no ritmo, balança de um lado para o outro, braços alternando para o alto
                        val beat = sin(u * PIf * 6f); val up = beat > 0f
                        p.hop += -abs(beat) * 5f * w; p.tilt += beat * 7f * w; whistle = w
                        p.low = lerp(p.low, 0.6f, w); p.mOpen = lerp(p.mOpen, 0.5f, w); p.mW = lerp(p.mW, 5.8f, w); p.mCurve = lerp(p.mCurve, 4.8f, w); p.cheek = lerp(p.cheek, 0.9f, w)
                        p.hL0 = lerp(p.hL0, if (up) 4f else 10f, w); p.hL1 = lerp(p.hL1, if (up) 40f else 70f, w); p.hR0 = lerp(p.hR0, if (up) 90f else 96f, w); p.hR1 = lerp(p.hR1, if (up) 70f else 40f, w)
                        if (w > 0.4f) { p.shapeL = if (up) Hand.FIST else Hand.MITT; p.shapeR = if (up) Hand.MITT else Hand.FIST }
                    }
                    "hug" -> {
                        // abraço: os braços cruzam na frente do livro, olhinhos fechados, bochecha vermelha e corações
                        p.hL0 = lerp(p.hL0, 58f, w); p.hL1 = lerp(p.hL1, 70f, w); p.hR0 = lerp(p.hR0, 42f, w); p.hR1 = lerp(p.hR1, 68f, w)
                        p.rotL = lerp(p.rotL, -40f, w); p.rotR = lerp(p.rotR, 40f, w); p.bendL = lerp(p.bendL, 8f, w); p.bendR = lerp(p.bendR, 8f, w)
                        if (w > 0.3f) { p.shapeL = Hand.OPEN; p.shapeR = Hand.OPEN; armsFront = true }
                        p.low = lerp(p.low, 0.8f, w); p.up = lerp(p.up, 0.1f, w); p.cheek = lerp(p.cheek, 1f, w); p.mW = lerp(p.mW, 5.4f, w); p.mCurve = lerp(p.mCurve, 4.6f, w); p.mOpen = lerp(p.mOpen, 0.15f, w)
                        p.tilt += sin(u * PIf * 4f) * 2.5f * w; hearts = maxOf(hearts, w)
                    }
                    "stars" -> { starEyes = w > 0.25f; sparkle = w; p.hop += -sin(PIf * u) * 8f; p.mOpen = lerp(p.mOpen, 1f, w); p.mW = lerp(p.mW, 6.4f, w); p.mCurve = lerp(p.mCurve, 5.2f, w); p.cheek = lerp(p.cheek, 0.9f, w) }
                    "love" -> { p.low = lerp(p.low, 0.78f, w); p.cheek = lerp(p.cheek, 1f, w); p.mW = lerp(p.mW, 5.4f, w); p.mCurve = lerp(p.mCurve, 4.6f, w); p.mOpen = lerp(p.mOpen, 0.2f, w); hearts = maxOf(hearts, w) }
                    "wink" -> { p.low = lerp(p.low, 0.5f, w); p.mCurve = lerp(p.mCurve, 4.4f, w); p.cheek = lerp(p.cheek, 0.8f, w) }
                }
            }
        }
        // Lendários: ao chegar na tela, corações dourados em volta dele
        if (FolhaLook.bond >= 6 && now - bornAt < 3000) { hearts = maxOf(hearts, sin(PIf * (now - bornAt) / 3000f)); goldHearts = true }
        fid?.let { fd ->
            val u = clamp((now - fd.start).toFloat() / fd.dur, 0f, 1f)
            if (fd.kind == "hatflip") { hatUp = 20f * sin(PIf * u); hatSpin = 360f * u }
            if (fd.kind == "whistle") whistle = env(u)
        }
        // balão de pensamento: de vez em quando, parado e à toa, ele sonha acordado com alguma coisa
        val calm = shown == FolhaMood.IDLE && fid == null && react == null && !petting && now > quipUntil
        if (nextThought == 0L) nextThought = now + 9000 + Random.nextLong(12000)
        if (thought == null && chatty && calm && now > nextThought) {
            var k = THOUGHTS.random(); if (k == lastThought) k = THOUGHTS[(THOUGHTS.indexOf(k) + 1) % THOUGHTS.size]
            thought = k; thoughtAt = now; lastThought = k
        }
        thought?.let { k ->
            val u = (now - thoughtAt) / 3600f
            if (u >= 1f || (shown != FolhaMood.IDLE && u < 0.88f)) { thought = null; nextThought = now + 16000 + Random.nextLong(14000) }
            else {
                val w = env(u)
                p.lookX = lerp(p.lookX, 0.75f, w); p.lookY = lerp(p.lookY, -0.9f, w); p.mCurve = lerp(p.mCurve, 3.8f, w); p.mW = lerp(p.mW, 4.4f, w); p.browY = lerp(p.browY, -0.8f, w)
                th = k; thU = u
            }
        }
        return Life(turn, hatUp, hatSpin, whistle, hearts, th, thU, armsFront, starEyes, sparkle, goldHearts)
    }

    fun snapshot(gulp: Float, look: Look): FolhaFrame {
        val p = PoseValues().also { it.set(x); it.shapeL = shapeL; it.shapeR = shapeR }
        val lifeNow = life(p)
        worried = look.atRisk
        holding = look.hand != "nenhum"
        var fade = 1f; var rise = 0f
        if (leaveAt >= 0) { val u = clamp((nowMs - leaveAt - 480L) / 280f, 0f, 1f); fade = 1f - u; rise = u * u * 0.3f }
        return FolhaFrame(
            look = look, tm = (nowMs - moodAt) / 1000f, fade = fade, rise = rise, prevMood = prevShown,
            burst = if (burstAt < 0) 0f else clamp(1f - (nowMs - burstAt) / 650f, 0f, 1f),
            t = t, mood = if (lifeNow?.starEyes == true) FolhaMood.ECSTATIC else shown, small = small, p = p, vy = v[0], blink = blink,
            wink = if (wink || shown == FolhaMood.WINK) 1f else 0f, popL = popL, popR = popR,
            squash = maxOf(bounce(), gulp * 0.45f) + clamp(sq * 0.1f, -0.2f, 0.25f), gulp = gulp,
            speechOpen = if (speaking) mouthOpen else null, speechRound = mouthRound, life = lifeNow,
        )
    }
}

/** Humores em que ele para para falar a frase do balão. */
private val QUIP_MOODS = setOf(FolhaMood.IDLE, FolhaMood.WAVE, FolhaMood.HAPPY, FolhaMood.PROUD, FolhaMood.POINT)

private class FolhaFrame(
    val look: Look, val tm: Float, val fade: Float, val rise: Float, val burst: Float, val prevMood: FolhaMood? = null,
    val t: Float, val mood: FolhaMood, val small: Boolean, val p: PoseValues, val vy: Float, val blink: Float, val wink: Float,
    val popL: Float, val popR: Float, val squash: Float, val gulp: Float, val speechOpen: Float?, val speechRound: Float,
    val life: Life? = null,
)

/** O que a vida põe por cima do desenho: giro, chapéu voando, notas do assobio, corações e o balão de pensamento. */
private class Life(
    val turn: Float = 1f, val hatUp: Float = 0f, val hatSpin: Float = 0f, val whistle: Float = 0f, val hearts: Float = 0f,
    val thought: String? = null, val thoughtU: Float = 0f,
    val armsFront: Boolean = false, val starEyes: Boolean = false, val sparkle: Float = 0f, val goldHearts: Boolean = false,
)

/** Reações ao toque (cada toque, uma diferente) e quanto duram, em ms. */
private val REACTIONS = listOf("giggle" to 900L, "spin" to 750L, "hop" to 950L, "love" to 1300L, "wink" to 800L)
/** Gestos que a amizade libera (FolhaBond.kt): dancinha (Parceiros), abraço (Amigos), olhos de estrela (Inseparáveis). */
private val BOND_REACTIONS = listOf(Triple(2, "dance", 1700L), Triple(3, "hug", 1500L), Triple(5, "stars", 1300L))
private fun reactionsNow(): List<Pair<String, Long>> = REACTIONS + BOND_REACTIONS.filter { FolhaLook.bond >= it.first }.map { it.second to it.third }
/** O que ele imagina no balão de pensamento. */
private val THOUGHTS = listOf("bulb", "heart", "star", "book", "coffee", "note", "question", "trophy", "pizza")
/** Frases: tocar nele, fazer carinho e voltar para o app. Curtas, no jeito dele (as mesmas do site). */
private val TAP_LINES = listOf(
    "Ei! Isso faz cócegas!", "Hehe, de novo!", "Tô aqui, firme e forte!", "Bora mais um tópico?", "Você tá indo bem, viu?",
    "Psiu… revisar amanhã fixa o dobro.", "Já bebeu água hoje?", "Cada questão é um passo pra posse!", "Eu acredito em você. Muito!",
    "Ai, minhas páginas!", "Folha, ao seu dispor!", "Um tópico de cada vez, combinado?", "Errar aqui é acertar na prova.",
    "Tô torcendo por você!", "Quer ver um giro? Toca de novo!", "Uma pausa curtinha também é estudo.", "Hoje a gente vence o edital um pouquinho.",
)
private val MORNING_LINES = listOf("Bom dia! Já tomou café?", "Cedinho assim? Orgulho!", "Manhã é a melhor hora pra memória.")
private val NIGHT_LINES = listOf("Tá tarde, hein? Mais um e cama!", "Estudo da noite também conta.", "Não esquece de dormir, tá? A memória agradece.")
private val PET_LINES = listOf("Ahh, que carinho bom!", "Hehe, faz de novo!", "Você é demais!", "Fiquei vermelhinho…", "Agora eu estudo até de cabeça pra baixo!")
internal val BACK_LINES = listOf("Voltou! Senti sua falta.", "Ei, que bom te ver!", "Bora continuar de onde paramos?", "Eu guardei a página pra você!")
private fun tapLine(): String {
    val h = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val list = if (Random.nextInt(4) == 0) (if (h in 5..10) MORNING_LINES else if (h >= 22 || h < 5) NIGHT_LINES else TAP_LINES)
        else if (FolhaLook.bondLines.isNotEmpty() && Random.nextInt(100) < 35) FolhaLook.bondLines else TAP_LINES
    return list.random()
}

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
        drawFloorAura(t, f.look.aura, small)
        drawPet(t, f.look.pet, mood, small, f.burst)
        translate(0f, p.hop) {
            scale(squashX / sqrt(stretch), squashY * stretch, pivot = Offset(50f, 88f)) {
                rotate(p.tilt, pivot = Offset(50f, 80f)) {
                // girando no lugar (reação ao toque): o corpo achata e vira, como em 3D
                val turn = f.life?.turn ?: 1f
                scale(if (abs(turn) < 0.04f) (if (turn < 0f) -0.04f else 0.04f) else turn, 1f, pivot = Offset(50f, 0f)) {
                    val look = f.look
                    drawAura(t, look.aura, small, f.burst, behind = true)
                    drawBack(t, look.back, mood, small, p.hop)
                    // na frente do livro: as duas mãos folheando ou no susto; a do lápis no foco; a da testa no "ufa"
                    var front = when (mood) { FolhaMood.THINKING, FolhaMood.SURPRISED, FolhaMood.PRESENT -> "LR"; FolhaMood.FOCUS, FolhaMood.RELIEVED -> "R"; else -> "" }
                    // segurando um item: a mão esquerda fica na frente do livro, para o item aparecer
                    if (look.hand != "nenhum" && 'L' !in front) front = "L$front"
                    if (f.life?.armsFront == true) front = "LR"
                    drawArms(f, "LR".filter { it !in front })
                    drawBook(t, mood, small, look)
                    drawPageLines(t, mood, small, look.ink)
                    if (!small) drawDogEar(t, mood)
                    if (mood == FolhaMood.THINKING) drawTurningPage(t)
                    drawFace(f)
                    drawEyewear(p, small, look.eyes, t)
                    drawNeckwear(t, small, look.neck)
                    // à noite, parado ou cochilando, a touca entra no lugar do capelo (outro chapéu escolhido fica)
                    val nightcap = look.night && look.hat == "capelo" && (mood == FolhaMood.IDLE || mood == FolhaMood.SLEEPY)
                    // no pulo de comemoração o chapéu sai um pouco da cabeça e volta (atrasado em relação ao corpo)
                    val fly = if (mood == FolhaMood.HAPPY || mood == FolhaMood.ECSTATIC) maxOf(0f, sin(t * 5.2f - 0.9f)).pow(2) else 0f
                    translate(0f, -fly * (if (mood == FolhaMood.ECSTATIC) 6f else 4f) - (f.life?.hatUp ?: 0f)) {
                        rotate(p.capRot + fly * 8f * sin(t * 2.6f) + (f.life?.hatSpin ?: 0f), pivot = Offset(50f, 38f)) {
                            if (nightcap) drawNightcap(t, p.tilt, f.vy) else drawHat(f, look, p.tilt)
                        }
                    }
                    if (front.isNotEmpty()) drawArms(f, front)
                    if (mood == FolhaMood.RELIEVED && !small) drawRelief(f.tm)
                    if (look.atRisk && mood == FolhaMood.IDLE && !small) drawSweat(t)
                }
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
        drawAura(t, look.aura, small, f.burst, behind = false)
        if (look.aura == "holofote") drawSpotLamps(t)
        drawBurst(f.burst)
        f.life?.let { drawLife(t, it, small) }
    }
}

// ---- vida: notas do assobio, corações do carinho e o balão de pensamento
private fun DrawScope.drawLife(t: Float, l: Life, small: Boolean) {
    if (l.whistle > 0.05f) for (i in 0 until 3) {
        val ph = (t * 0.7f + i / 3f) % 1f; val a = sin(PIf * ph) * l.whistle; val x = 62f + ph * 22f + sin(t * 3f + i) * 2f; val y = 74f - ph * 30f
        rotate(sin(t * 2f + i) * 0.3f * 180f / PIf, pivot = Offset(x, y)) { scale(1.8f, 1.8f, pivot = Offset(x, y)) {
            val c = Color(0xFF3A2FC4).copy(alpha = a)
            rotate(-0.45f * 180f / PIf, pivot = Offset(x, y)) { oval(c, x - 1.6f, y - 1.15f, 3.2f, 2.3f) }
            line(c, x + 1.35f, y - 0.3f, x + 1.35f, y - 5.6f, 0.5f)
            drawPath(Path().apply { moveTo(x + 1.35f, y - 5.6f); cubicTo(x + 3.4f, y - 4.8f, x + 3.8f, y - 3.2f, x + 2.8f, y - 2f) }, c, style = stroke(0.55f))
        } }
    }
    if (l.hearts > 0.05f) for (i in 0 until if (small) 2 else 4) {
        val ph = (t * 0.6f + i / 4f) % 1f; val a = sin(PIf * ph) * l.hearts; val side = if (i % 2 == 1) 1f else -1f
        val x = 50f + side * (24f + i * 4f) + sin(t * 2f + i) * 2f; val y = 44f - ph * 34f; val r = 2.4f + ph * 1.4f
        drawPath(heart(x, y, r), vg(if (l.goldHearts) GOLD_STOPS.map { it.copy(alpha = a) } else listOf(Color(0xFFFF8FA3).copy(alpha = a), Color(0xFFE0475E).copy(alpha = a)), y - r, y + r))
    }
    if (l.sparkle > 0.05f) for (i in 0 until 8) {
        val a = i * PIf / 4f + t; val ph = (t * 1.5f + i / 8f) % 1f; val r = 34f + ph * 14f
        star4(50f + cos(a) * r, 58f + sin(a) * r * 0.75f, 3f * sin(PIf * ph) * l.sparkle, (if (i % 2 == 1) Color.White else Gold).copy(alpha = l.sparkle))
    }
    val th = l.thought ?: return
    val u = l.thoughtU
    val pop = when { u < 0.12f -> { val x = u / 0.12f; val c = 1.9f; 1f + (c + 1f) * (x - 1f).pow(3) + c * (x - 1f).pow(2) }; u > 0.88f -> 1f - (u - 0.88f) / 0.12f; else -> 1f }
    if (pop <= 0f) return
    val a = clamp(pop * 1.4f, 0f, 1f); val cx = 84f; val cy = sin(t * 2f) * 0.8f; val sc = (0.4f + 0.6f * pop) * 1.4f
    for ((x, y, r) in listOf(Triple(70f, 22f, 1.4f), Triple(75f, 14.5f, 2.3f))) { drawCircle(Color(0x2E26215C).copy(alpha = 0.18f * a), r + 0.7f, Offset(x, y)); drawCircle(Color.White.copy(alpha = a), r, Offset(x, y)) }
    translate(cx, cy) { scale(sc, sc, pivot = Offset.Zero) {
        val puffs = listOf(Triple(-6f, 1.5f, 5f), Triple(0f, -2.5f, 6.6f), Triple(6f, 1f, 5.2f), Triple(0f, 3.6f, 5.4f), Triple(-3f, -1f, 5f), Triple(3.4f, -1.4f, 5f))
        for ((x, y, r) in puffs) drawCircle(Color(0xFF26215C).copy(alpha = 0.22f * a), r + 0.9f, Offset(x, y + 0.6f))
        for ((x, y, r) in puffs) drawCircle(Color.White.copy(alpha = a), r, Offset(x, y))
        drawThoughtIcon(th, t, a)
    } }
}

private fun DrawScope.drawThoughtIcon(kind: String, t: Float, a: Float) {
    fun c(v: Long) = Color(v).copy(alpha = a)
    when (kind) {
        "bulb" -> {
            drawCircle(Brush.radialGradient(listOf(c(0xE6FFE07A), Color(0x00FFE07A)), center = Offset(0f, -1.4f), radius = 6f), 6f, Offset(0f, -1.4f))
            drawCircle(c(0xFFFFD84A), 3f, Offset(0f, -1.6f)); drawRect(c(0xFF8E92A6), Offset(-1.5f, 1.2f), Size(3f, 2.2f)); line(c(0xFF6B7088), -1.5f, 2.3f, 1.5f, 2.3f, 0.35f)
            drawCircle(Color.White.copy(alpha = 0.8f * a), 0.8f, Offset(-1f, -2.6f))
        }
        "heart" -> drawPath(heart(0f, 0f, 3.4f), vg(listOf(c(0xFFFF8FA3), c(0xFFE0475E)), -4f, 3f))
        "star" -> drawPath(star5(0f, 0.3f, 4.2f), vg(GOLD_STOPS.map { it.copy(alpha = a) }, -4f, 4f))
        "book" -> {
            drawPath(Path().apply { moveTo(0f, -1.6f); quadraticTo(-2.4f, -3f, -4.6f, -2.2f); lineTo(-4.6f, 2.6f); quadraticTo(-2.4f, 1.8f, 0f, 3f); quadraticTo(2.4f, 1.8f, 4.6f, 2.6f); lineTo(4.6f, -2.2f); quadraticTo(2.4f, -3f, 0f, -1.6f); close() }, c(0xFF5247E0))
            line(Color.White.copy(alpha = a), 0f, -1.4f, 0f, 2.8f, 0.4f); line(Color.White.copy(alpha = 0.7f * a), -3.6f, -0.6f, -1f, -0.2f, 0.3f); line(Color.White.copy(alpha = 0.7f * a), 1f, -0.2f, 3.6f, -0.6f, 0.3f)
        }
        "coffee" -> {
            val mug = Path().apply { moveTo(-3f, -1.2f); lineTo(2.4f, -1.2f); lineTo(2f, 3f); lineTo(-2.6f, 3f); close() }
            drawPath(mug, Color.White.copy(alpha = a)); drawPath(mug, c(0xFF26215C), style = Stroke(0.45f))
            drawArc(c(0xFF26215C), -90f, 180f, false, Offset(1.6f, -0.4f), Size(2.4f, 2.4f), style = Stroke(0.45f)); drawRect(c(0xFF8A5A2B), Offset(-2.7f, -1f), Size(4.8f, 0.9f))
            for (i in 0 until 2) { val x = -1f + i * 1.8f; val k = sin(t * 3f + i); drawPath(Path().apply { moveTo(x, -2f); quadraticTo(x + k, -3.4f, x, -4.6f) }, c(0xB38C82AA), style = stroke(0.4f)) }
        }
        "note" -> {
            val col = c(0xFF3A2FC4)
            rotate(-0.45f * 180f / PIf, pivot = Offset(-1.6f, 2.2f)) { oval(col, -3.1f, 1.1f, 3f, 2.2f) }; rotate(-0.45f * 180f / PIf, pivot = Offset(2.4f, 1.4f)) { oval(col, 0.9f, 0.3f, 3f, 2.2f) }
            line(col, -0.3f, 2f, -0.3f, -3.4f, 0.5f); line(col, 3.7f, 1.2f, 3.7f, -4.2f, 0.5f)
            drawPath(Path().apply { moveTo(-0.3f, -3.4f); lineTo(3.7f, -4.2f); lineTo(3.7f, -3.2f); lineTo(-0.3f, -2.4f); close() }, col)
        }
        "question" -> {
            val q = Path().apply { moveTo(-2.2f, -1.6f); cubicTo(-2.2f, -4.6f, 2.6f, -4.6f, 2.4f, -1.6f); cubicTo(2.3f, 0f, 0f, 0.2f, 0f, 1.6f) }
            drawPath(q, c(0xFF5247E0), style = stroke(1.3f)); drawCircle(c(0xFF5247E0), 0.8f, Offset(0f, 3.6f))
        }
        "trophy" -> {
            val gold = vg(GOLD_STOPS.map { it.copy(alpha = a) }, -3.4f, 3f)
            drawPath(Path().apply { moveTo(-2.8f, -3f); lineTo(2.8f, -3f); quadraticTo(2.6f, 1f, 0f, 1.4f); quadraticTo(-2.6f, 1f, -2.8f, -3f); close() }, gold)
            drawRect(gold, Offset(-0.6f, 1.2f), Size(1.2f, 1.4f)); drawRect(gold, Offset(-2f, 2.5f), Size(4f, 1f))
            drawArc(c(0xFFF5B83D), 90f, 180f, false, Offset(-4f, -2.8f), Size(2.4f, 2.4f), style = Stroke(0.5f)); drawArc(c(0xFFF5B83D), -90f, 180f, false, Offset(1.6f, -2.8f), Size(2.4f, 2.4f), style = Stroke(0.5f))
        }
        "pizza" -> {
            drawPath(Path().apply { moveTo(0f, 3.6f); lineTo(-3.6f, -2.6f); quadraticTo(0f, -4.2f, 3.6f, -2.6f); close() }, c(0xFFF7C35A))
            drawPath(Path().apply { moveTo(-3.6f, -2.6f); quadraticTo(0f, -4.4f, 3.6f, -2.6f); lineTo(3.2f, -1.8f); quadraticTo(0f, -3.2f, -3.2f, -1.8f); close() }, c(0xFFC98A4B))
            for ((x, y) in listOf(-1f to -1f, 1.2f to -0.6f, 0f to 1.4f)) drawCircle(c(0xFFE0475E), 0.8f, Offset(x, y))
        }
    }
}

private val COVER_STARS = listOf(
    floatArrayOf(9.8f, 50f, 0.35f), floatArrayOf(10.6f, 63f, 0.25f), floatArrayOf(9.6f, 76f, 0.4f), floatArrayOf(90.2f, 55f, 0.3f), floatArrayOf(89.6f, 70f, 0.4f),
    floatArrayOf(90.4f, 82f, 0.25f), floatArrayOf(24f, 88.6f, 0.3f), floatArrayOf(40f, 90.4f, 0.35f), floatArrayOf(62f, 90.2f, 0.3f), floatArrayOf(77f, 88.4f, 0.35f),
)
// Efeitos no chão (fora do corpo: não pulam com ele). Holofotes: dois canhões de luz nos cantos de cima
// varrendo o palco, com a poça de luz no chão; runas: um círculo mágico girando debaixo dos pés.
private fun DrawScope.drawFloorAura(t: Float, kind: String, small: Boolean) {
    when (kind) {
        "holofote" -> for (side in listOf(-1f, 1f)) {
            val sx = 50f + side * 56f; val sy = -6f; val tx = 50f + sin(t * 0.8f + (if (side > 0f) 2.2f else 0f)) * 24f; val ty = 95f
            val ang = atan2(ty - sy, tx - sx) * 180f / PIf; val len = sqrt((tx - sx) * (tx - sx) + (ty - sy) * (ty - sy)); val w = len * 0.17f
            translate(sx, sy) {
                rotate(ang, pivot = Offset.Zero) {
                    val cone = Path().apply { moveTo(0f, -2f); lineTo(len, -w); quadraticTo(len + 3f, 0f, len, w); lineTo(0f, 2f); close() }
                    drawPath(cone, Brush.horizontalGradient(listOf(Color(0x8CFFE28C), Color(0x33FFE28C), Color(0x0FFFE28C)), startX = 0f, endX = len))
                    val core = Path().apply { moveTo(0f, -0.8f); lineTo(len, -w * 0.35f); lineTo(len, w * 0.35f); lineTo(0f, 0.8f); close() }
                    drawPath(core, Color(0x2EFFF8DC))
                }
            }
            scale(1f, 0.26f, pivot = Offset(tx, ty)) { drawCircle(Brush.radialGradient(listOf(Color(0xA6FFECAA), Color(0x00FFECAA)), center = Offset(tx, ty), radius = 20f), 20f, Offset(tx, ty)) }
            if (!small) for (i in 0 until 5) {
                val u = (t * 0.07f + i * 0.21f) % 1f
                val px = sx + (tx - sx) * (0.25f + u * 0.7f) + sin(t + i * 3f) * 2f; val py = sy + (ty - sy) * (0.25f + u * 0.7f) + cos(t * 0.7f + i) * 2f
                drawCircle(Color(0xFFFFF6D6).copy(alpha = 0.7f * sin(PIf * u)), 0.55f, Offset(px, py))
            }
        }
        "runas" -> {
            // o círculo mágico em perspectiva: anéis, hexagrama e runas girando, pulsando de leve
            val pulse = 0.75f + 0.25f * sin(t * 2.4f); val rot = t * 0.35f; val K = 0.22f; val cx = 50f; val cy = 94f
            fun at(r: Float, a: Float) = Offset(cx + cos(a) * r, cy + sin(a) * r * K)
            scale(1f, K, pivot = Offset(cx, cy)) {
                drawCircle(Brush.radialGradient(0f to Color(0x007EE0B8), 0.72f to Mint.copy(alpha = 0.28f * pulse), 1f to Color(0x009563FF), center = Offset(cx, cy), radius = 56f), 56f, Offset(cx, cy))
            }
            fun ring(r: Float, c: Color, w: Float) = drawOval(c, Offset(cx - r, cy - r * K), Size(2 * r, 2 * r * K), style = Stroke(w))
            ring(47f, Mint.copy(alpha = 0.9f * pulse), if (small) 1.4f else 0.9f); ring(42f, Color(0xFFB995F5).copy(alpha = 0.8f * pulse), if (small) 1f else 0.55f); ring(29f, Mint.copy(alpha = 0.6f * pulse), 0.5f)
            for (k in 0 until 2) {
                val p = Path(); for (i in 0 until 3) { val o = at(42f, rot + k * PIf / 3f + i * 2f * PIf / 3f); if (i == 0) p.moveTo(o.x, o.y) else p.lineTo(o.x, o.y) }
                p.close(); drawPath(p, Color(0xFFB995F5).copy(alpha = 0.7f * pulse), style = stroke(0.5f))
            }
            if (!small) {
                val runes = listOf(floatArrayOf(0f, -2f, 0f, 2f, -1.4f, -0.6f, 1.4f, 0.6f), floatArrayOf(-1.4f, -2f, 1.4f, -2f, 0f, -2f, 0f, 2f), floatArrayOf(-1.2f, 2f, 0f, -2f, 0f, -2f, 1.2f, 2f), floatArrayOf(-1.4f, -1f, 1.4f, -1f, -1.4f, 1f, 1.4f, 1f))
                for (i in 0 until 16) {
                    val o = at(44.5f, -rot * 0.7f + i * PIf / 8f); val r = runes[i % 4]; val c = Color(0xFFC8FFE8).copy(alpha = 0.85f * pulse)
                    line(c, o.x + r[0] * 0.7f, o.y + r[1] * 0.32f, o.x + r[2] * 0.7f, o.y + r[3] * 0.32f, 0.5f); line(c, o.x + r[4] * 0.7f, o.y + r[5] * 0.32f, o.x + r[6] * 0.7f, o.y + r[7] * 0.32f, 0.5f)
                }
            }
        }
    }
}

// Lâmpadas dos holofotes, na frente de tudo nos cantos de cima.
private fun DrawScope.drawSpotLamps(t: Float) {
    for (side in listOf(-1f, 1f)) {
        val sx = 50f + side * 56f; val sy = -6f; val tx = 50f + sin(t * 0.8f + (if (side > 0f) 2.2f else 0f)) * 24f
        translate(sx, sy) {
            rotate(atan2(95f - sy, tx - sx) * 180f / PIf, pivot = Offset.Zero) {
                drawPath(Path().apply { moveTo(-6f, -2.6f); lineTo(1f, -3.4f); lineTo(1f, 3.4f); lineTo(-6f, 2.6f); close() }, vg(listOf(Color(0xFF5A5F75), Color(0xFF2E3142), Color(0xFF1B1D29)), -3.4f, 3.4f))
                drawRect(Color(0xFF3C4054), Offset(1f, -3.8f), Size(1.4f, 7.6f))
                drawOval(Brush.radialGradient(listOf(Color.White, Color(0xE6FFECAA), Color(0x00FFE28C)), center = Offset(2.6f, 0f), radius = 4f), Offset(1f, -3.6f), Size(3.2f, 7.2f))
                line(Color.White.copy(alpha = 0.35f), -5f, -2f, 0.5f, -2.8f, 0.4f)
            }
        }
    }
}

// Costas: atrás do livro, presas nos ombros. Asas de fada que batem (rápido na festa), mochila-foguete
// com chama que acende quando ele pula, manto real esvoaçante com gola de arminho e barra dourada.
private fun DrawScope.drawBack(t: Float, kind: String, mood: FolhaMood, small: Boolean, hop: Float) {
    if (kind == "nada") return
    val party = mood == FolhaMood.HAPPY || mood == FolhaMood.ECSTATIC; val air = clamp((-hop - 1.5f) / 4f, 0f, 1f)
    fun mirrored(side: Float, block: DrawScope.() -> Unit) = if (side > 0f) scale(-1f, 1f, pivot = Offset(50f, 0f)) { block() } else block()
    when (kind) {
        "asas" -> {
            val f = sin(t * (if (party) 13f else 3f)); val amp = if (party) 1f else 0.35f + air * 0.6f
            for (side in listOf(-1f, 1f)) mirrored(side) {
                rotate(-4f - f * 9f * amp, pivot = Offset(24f, 54f)) {
                    scale(0.84f + 0.16f * f * amp, 1f, pivot = Offset(24f, 54f)) {
                        val up = Path().apply { moveTo(24f, 48f); cubicTo(18f, 24f, 2f, 2f, -8f, 6f); cubicTo(-17f, 10f, -16f, 30f, -10f, 40f); cubicTo(-4f, 50f, 8f, 55f, 24f, 56f); close() }
                        val lo = Path().apply { moveTo(23f, 57f); cubicTo(6f, 56f, -12f, 62f, -13f, 73f); cubicTo(-14f, 83f, 2f, 82f, 12f, 74f); cubicTo(18f, 69f, 22f, 64f, 24f, 60f); close() }
                        for ((p, y0, y1) in listOf(Triple(up, 4f, 56f), Triple(lo, 57f, 82f))) {
                            drawPath(p, Brush.linearGradient(listOf(Color(0xEBFFC4EE), Color(0xDBCBB2FF), Color(0xD1A0E4FF)), start = Offset(-12f, y0), end = Offset(24f, y1)))
                            drawPath(p, Color.White.copy(alpha = 0.95f), style = stroke(if (small) 1.3f else 0.7f))
                        }
                        if (!small) {
                            val v = Path().apply { moveTo(23f, 51f); quadraticTo(12f, 30f, -5f, 10f); moveTo(23f, 53f); quadraticTo(4f, 40f, -12f, 30f); moveTo(23f, 59f); quadraticTo(4f, 64f, -9f, 76f) }
                            drawPath(v, Color.White.copy(alpha = 0.75f), style = stroke(0.45f))
                            drawPath(Path().apply { moveTo(-13f, 14f); quadraticTo(-11f, 7f, -5f, 7f) }, Color.White.copy(alpha = 0.8f), style = stroke(0.9f))
                            val sxs = floatArrayOf(-13f, -4f, -10f, 6f); val sys = floatArrayOf(24f, 5f, 78f, 16f)
                            for (i in 0 until 4) { val ph = (t * 0.9f + i * 0.25f + (if (side > 0f) 0.5f else 0f)) % 1f; star4(sxs[i], sys[i], 1.9f * sin(PIf * ph), Color.White.copy(alpha = sin(PIf * ph))) }
                            for (i in 0 until 4) { val ph = (t * 0.5f + i / 4f + (if (side > 0f) 0.12f else 0f)) % 1f; drawCircle(Color(0xFFFFE2FA).copy(alpha = 0.9f * (1f - ph)), 0.7f * (1f - ph) + 0.2f, Offset(-6f + i * 3f + sin(t * 3f + i) * 2f, 78f + ph * 18f)) }
                        }
                    }
                }
            }
        }
        "foguete" -> {
            val thrust = if (party) 1f else air
            for (side in listOf(-1f, 1f)) mirrored(side) {
                val fl = 5f + thrust * 15f + sin(t * 31f + side) * (0.6f + thrust * 1.4f); val fx = 3.6f; val fy = 84.5f
                fun flame(w: Float, len: Float, stops: List<Color>) = drawPath(Path().apply {
                    moveTo(fx - w, fy); cubicTo(fx - w * 1.1f, fy + len * 0.45f, fx - w * 0.25f, fy + len * 0.8f, fx, fy + len)
                    cubicTo(fx + w * 0.25f, fy + len * 0.8f, fx + w * 1.1f, fy + len * 0.45f, fx + w, fy); close()
                }, vg(stops, fy, fy + len))
                if (thrust > 0.05f) { flame(3.4f, fl, listOf(Color(0xFFFFE07A), Color(0xFFFF9F3A), Color(0x00F2552C))); flame(1.8f, fl * 0.6f, listOf(Color.White, Color(0xFFFFF1BF), Color(0x00FFE07A))) }
                else { flame(2.2f, fl, listOf(Color(0xFFE6F4FF), Color(0xFF7FC4FF), Color(0x00508CFF))); flame(1f, fl * 0.5f, listOf(Color.White, Color(0x00C8E8FF))) }
                if (thrust > 0.3f && !small) for (i in 0 until 4) { val ph = (t * 1.6f + i / 4f) % 1f; drawCircle(Color(0xFFDCDCEB).copy(alpha = 0.45f * (1f - ph) * thrust), 1.4f + ph * 3.4f, Offset(fx - 2f - ph * 6f + sin(i * 3f) * 2f, fy + fl * 0.7f + ph * 8f)) }
                drawPath(Path().apply { moveTo(0.4f, 79.5f); lineTo(6.8f, 79.5f); lineTo(8f, 85f); lineTo(-0.8f, 85f); close() }, hg(listOf(Color(0xFF2C2F40), Color(0xFF6B7088), Color(0xFF2C2F40)), -0.8f, 8f))
                val fin = Path().apply { moveTo(-3f, 64f); lineTo(-8f, 77f); lineTo(-8f, 80f); lineTo(-3f, 76f); close() }
                drawPath(fin, vg(listOf(Color(0xFFF05A6E), Color(0xFFC42F4B)), 64f, 80f)); drawPath(fin, Color(0x803C0A19), style = stroke(0.4f))
                val tank = Path().apply { moveTo(-3.4f, 53f); cubicTo(-3.4f, 45f, 10.6f, 45f, 10.6f, 53f); lineTo(10.6f, 77f); quadraticTo(10.6f, 80.5f, 3.6f, 80.5f); quadraticTo(-3.4f, 80.5f, -3.4f, 77f); close() }
                drawPath(tank, hg(listOf(Color(0xFF7C8398), Color(0xFFE8ECF4), Color(0xFFC3C9D6), Color(0xFF6E7588)), -3.4f, 10.6f))
                clipPath(tank) {
                    val red = hg(listOf(Color(0xFFB3263F), Color(0xFFF05A6E), Color(0xFFC42F4B), Color(0xFF7A1428)), -3.4f, 10.6f)
                    drawRect(red, Offset(-4f, 44f), Size(16f, 6.5f)); drawRect(red, Offset(-4f, 60f), Size(16f, 3.2f))
                }
                drawPath(tank, Color(0x8C1E2030), style = stroke(if (small) 1f else 0.5f))
                if (!small) {
                    for (y in listOf(55f, 70f)) for (x in listOf(-1.2f, 8.4f)) drawCircle(Color(0x993C4054), 0.35f, Offset(x, y))
                    line(Color.White.copy(alpha = 0.7f), 0.6f, 52f, 0.6f, 77f, 0.7f)
                    val blink = (t * 1.3f + (if (side > 0f) 0.5f else 0f)) % 1f < 0.5f
                    drawCircle(if (blink) Mint else Color(0xFF2E6B57), 0.8f, Offset(3.6f, 66.5f))
                }
            }
        }
        "capa-real" -> {
            val wave = sin(t * 1.7f); val fl = air * 6f + (if (party) 3f else 0f)
            val L = -6f - fl + wave * 1.2f; val R = 106f + fl - wave * 1.2f
            val hem = (1..6).map { i -> floatArrayOf(L + (R - L) * (i - 0.5f) / 6f, 102f + sin(t * 2.3f + i * 1.3f) * 0.9f, L + (R - L) * i / 6f, 99f + sin(t * 2f + i) * 0.4f) }
            val cape = Path().apply { moveTo(16f, 44f); cubicTo(6f, 58f, L + 3f, 80f, L, 99f); hem.forEach { quadraticTo(it[0], it[1], it[2], it[3]) }; cubicTo(R - 3f, 80f, 94f, 58f, 84f, 44f); close() }
            drawPath(cape, hg(listOf(Color(0xFF6E0726), Color(0xFFC2183F), Color(0xFFE2364F), Color(0xFFC2183F), Color(0xFF6E0726)), L, R))
            if (!small) clipPath(cape) {
                for (i in 0 until 7) { val x = L + 6f + (R - L - 12f) * i / 6f + sin(t * 1.7f + i) * 1.2f; drawRect(hg(listOf(Color(0x003C0014), Color(0x473C0014), Color(0x003C0014)), x - 3f, x + 3f), Offset(x - 3f, 44f), Size(6f, 60f)) }
            }
            val hemP = Path().apply { moveTo(L, 99f); hem.forEach { quadraticTo(it[0], it[1], it[2], it[3]) } }
            drawPath(hemP, Color(0xFF7A4F05), style = stroke(if (small) 2.6f else 2.2f)); drawPath(hemP, Gold, style = stroke(if (small) 1.8f else 1.4f))
            if (!small) drawPath(hemP, Color(0xCCFFF1BF), style = stroke(0.4f))
            for (side in listOf(-1f, 1f)) {
                val cx = 50f + side * 36f
                rotate(side * -0.12f * 180f / PIf, pivot = Offset(cx, 43f)) {
                    drawOval(vg(listOf(Color.White, Color(0xFFECE8F6)), 38f, 48f), Offset(cx - 11f, 38f), Size(22f, 10f))
                    drawOval(Color(0x998C82AA), Offset(cx - 11f, 38f), Size(22f, 10f), style = Stroke(0.5f))
                    if (!small) for ((x, y) in listOf(-6f to -1.5f, -1f to -2.6f, 4.5f to -1.2f, 8f to 1f, -8.5f to 1.5f)) {
                        drawPath(Path().apply { moveTo(cx + x, 43f + y - 1f); quadraticTo(cx + x + 0.7f, 43f + y + 0.6f, cx + x, 43f + y + 1.3f); quadraticTo(cx + x - 0.7f, 43f + y + 0.6f, cx + x, 43f + y - 1f); close() }, Color(0xFF1B1B24))
                    }
                }
            }
        }
    }
}

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
    if (!small) clipPath(cover) { drawCoverPattern(t, c, cover) }
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
    if (!small) drawPagePattern(look.pages, left, right, look.ink, t)
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

private fun DrawScope.drawPageLines(t: Float, mood: FolhaMood, small: Boolean, ink: Color) {
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
    val ink = f.look.ink; val cheekC = f.look.cheek
    val p = f.p; val t = f.t; val mood = f.mood; val small = f.small; val blink = f.blink
    val eyeL = Offset(31f + p.lookX * 1.6f, 61f + p.lookY * 1.4f); val eyeR = Offset(69f + p.lookX * 1.6f, 61f + p.lookY * 1.4f)
    if (cheekC != null) { oval(cheekC.copy(alpha = p.cheek), 19f, 67f, 9f, 5f); oval(cheekC.copy(alpha = p.cheek), 72f, 67f, 9f, 5f) }

    fun eye(c: Offset, side: Float, extraLid: Float) {
        val x = c.x; val y = c.y
        if (mood == FolhaMood.DIZZY) {   // espiral girando
            val sp = Path(); val a0 = t * 7f * side
            for (i in 0..40) { val a = a0 + i * 0.42f; val rr = 0.25f + i * 0.12f; val px = x + cos(a) * rr; val py = y + sin(a) * rr * 1.15f; if (i == 0) sp.moveTo(px, py) else sp.lineTo(px, py) }
            drawPath(sp, ink, style = stroke(if (small) 1.6f else 1.2f)); return
        }
        if (mood == FolhaMood.ECSTATIC && blink < 0.5f) {   // olhos de estrela, pulsando
            val st = star5(x, y, (if (small) 6.4f else 5.8f) * (1f + 0.1f * sin(t * 9f)))
            drawPath(st, Brush.verticalGradient(listOf(Color(0xFFFFF1BF), Gold, Color(0xFFE39A1E)), startY = y - 6f, endY = y + 5f))
            drawPath(st, ink, style = stroke(if (small) 1.2f else 0.8f))
            if (!small) drawCircle(Color.White.copy(alpha = 0.9f), 0.9f, Offset(x - 1.2f, y - 1.6f))
            return
        }
        val rx = 4.4f * p.eyeScale * (if (small) 1.18f else 1f); val ry = 5.8f * p.eyeScale * (if (small) 1.1f else 1f)
        val up = clamp(maxOf(p.up, blink, extraLid), 0f, 1f); val low = clamp(p.low * (1 - blink), 0f, 1f)
        if (small && low > 0.45f && blink < 0.5f) {   // pequeno e feliz: o arco clássico (^^) lê melhor
            drawPath(Path().apply { moveTo(x - 4.5f, y + 1.5f); quadraticTo(x, y - 5f, x + 4.5f, y + 1.5f) }, ink, style = stroke(2.8f)); return
        }
        if (up > 0.96f) {   // fechado: só o traço
            drawPath(Path().apply { moveTo(x - 4.4f, y + 0.4f); quadraticTo(x, y + 2.4f, x + 4.4f, y + 0.4f) }, ink, style = stroke(if (small) 2.2f else 1.8f)); return
        }
        drawOval(ink, Offset(x - rx, y - ry), Size(rx * 2, ry * 2))
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
                drawPath(Path().apply { moveTo(xa, ya); quadraticTo(x, mid, xb, yb) }, ink, style = stroke(1.2f))
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
        drawPath(Path().apply { moveTo(outer.x, outer.y); quadraticTo(c.x, by - 1.4f + p.browIn * 0.6f, inner.x, inner.y) }, ink.copy(alpha = 0.9f * browA), style = stroke(if (small) 2.2f else 1.7f))
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
        oval(ink, mx - r, my - r * 1.1f, r * 2, r * 2.2f)
        for (i in 0 until 4) { val a = i * (PIf / 2) + 0.5f; drawCircle(Spark.copy(alpha = f.gulp), 1.8f * f.gulp * 0.9f, Offset(50f + cos(a) * (22f + 14f * (1 - f.gulp)), 58f + sin(a) * (16f + 10f * (1 - f.gulp)))) }
        return
    }
    if (mood == FolhaMood.SURPRISED) { oval(ink, mx - 2.6f, my - 3f, 5.2f, 6.4f); oval(Cheek, mx - 1.6f, my + 0.9f, 3.2f, 1.8f); return }
    if (mood == FolhaMood.DIZZY) {
        val wv = Path(); for (i in 0..12) { val px = mx - 5f + i * (10f / 12f); val py = my + sin(i * 1.4f + t * 8f) * 0.9f; if (i == 0) wv.moveTo(px, py) else wv.lineTo(px, py) }
        drawPath(wv, ink, style = stroke(if (small) 2.4f else 1.8f)); return
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
        drawPath(m, ink, style = stroke(if (small) 2.8f else 2.3f))
    } else {
        val top = corner + c * 0.15f - open * 0.6f; val depth = maxOf(1.4f, c * 1.1f) + open * 5.4f
        val m = Path().apply { moveTo(mx - w, corner); quadraticTo(mx, top, mx + w, corner); cubicTo(mx + w * 0.95f, corner + depth * 1.25f, mx - w * 0.95f, corner + depth * 1.25f, mx - w, corner); close() }
        drawPath(m, ink)
        clipPath(m) { oval(Cheek, mx - w * 0.55f, corner + depth - 1.6f * open - 0.6f, w * 1.1f, 3.4f * open + 1f) }
    }
}

private fun DrawScope.drawCap(t: Float, tilt: Float, vy: Float, gold: Boolean = false) {
    val top = if (gold) Color(0xFFFFE08A) else CapTop; val mid = if (gold) Color(0xFFE9AE34) else Cap
    val low = if (gold) Color(0xFFA86F0C) else Color(0xFF15104A); val edgeC = if (gold) Color(0xFF7A4F05) else Color(0xFF120D3F)
    val tas = if (gold) Color.White else Gold; val tasD = if (gold) Color(0xFFD9D4EE) else Color(0xFFD89422)
    drawPath(Path().apply { moveTo(38f, 32f); lineTo(62f, 32f); lineTo(60.5f, 42.5f); quadraticTo(50f, 46.5f, 39.5f, 42.5f); close() },
        Brush.verticalGradient(listOf(mid, low), startY = 32f, endY = 46f))
    drawPath(Path().apply { moveTo(23f, 29.5f); lineTo(50f, 38f); lineTo(77f, 29.5f); lineTo(77f, 31.3f); lineTo(50f, 39.8f); lineTo(23f, 31.3f); close() }, edgeC)
    val board = Path().apply { moveTo(50f, 21f); lineTo(77f, 29.5f); lineTo(50f, 38f); lineTo(23f, 29.5f); close() }
    drawPath(board, Brush.linearGradient(listOf(top, mid), start = Offset(30f, 22f), end = Offset(70f, 38f)))
    drawPath(board, Color.White.copy(alpha = 0.22f), style = stroke(0.5f))
    line(Color.White.copy(alpha = 0.18f), 33f, 27.5f, 50f, 22.5f, 1.2f)
    // borla: balança com o corpo e reage aos pulos (atrasada)
    val swing = sin(t * 2.6f) * 4f - tilt * 0.8f + clamp(vy * 0.08f, -5f, 5f)
    val bx = 50f; val by = 29.5f; val cx = 74f; val cy = 30.5f
    line(tas, bx, by, cx, cy, 1.3f)
    val ex = cx + swing * 0.5f; val ey = cy + 12f - maxOf(0f, -vy) * 0.03f
    drawPath(Path().apply { moveTo(cx, cy); quadraticTo(cx + swing * 0.2f, cy + 6f, ex, ey) }, tas, style = stroke(1.3f))
    drawPath(Path().apply { moveTo(ex - 1.4f, ey); lineTo(ex + 1.4f, ey); lineTo(ex + 2.4f + swing * 0.15f, ey + 6.5f); lineTo(ex - 2.4f + swing * 0.15f, ey + 6.5f); close() },
        Brush.verticalGradient(listOf(tas, tasD), startY = ey, endY = ey + 6.5f))
    drawCircle(tas, 1.8f, Offset(bx, by)); drawCircle(if (gold) Color.White else Color(0xFFFFE3A0), 0.7f, Offset(bx - 0.5f, by - 0.5f))
}

private fun DrawScope.drawRibbon(t: Float, mood: FolhaMood, colors: List<Color>) {
    val dark = colors[0]; val base = colors[1]; val light = colors[2]
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
    if (colors.size == 4) {
        drawPath(r, Brush.verticalGradient(colors, startY = lieTop, endY = tipY))
        drawPath(r, Brush.horizontalGradient(listOf(Color(0x1F000000), Color(0x40FFFFFF), Color(0x14000000)), startX = x0, endX = x0 + w + 0.6f + sway))
    } else drawPath(r, Brush.horizontalGradient(listOf(dark, base, light, base), startX = x0, endX = x0 + w + 0.6f + sway))
    drawRect(Brush.verticalGradient(listOf(Color(0x730E0A45), Color(0x000E0A45)), startY = 88f, endY = 91.5f), Offset(x0, 88f), Size(w + 0.3f, 3.5f))
    line(Color.White.copy(alpha = 0.55f), x0 + 0.5f, edge - 0.4f, x0 + w, edge - 0.4f, 0.5f, false)
    drawRect(Brush.verticalGradient(listOf(dark.copy(alpha = 0.55f), dark.copy(alpha = 0f)), startY = edge, endY = edge + 1.8f), Offset(x0 + 0.3f, edge), Size(w, 1.8f))
}

// ---- companheiro: um bichinho (ou plantinha) no chão, ao lado esquerdo do Folha
// Fica no chão (não pula junto); comemora junto, cochila junto e pula quando tocam no Folha.
private val PARTY_MOODS = setOf(FolhaMood.HAPPY, FolhaMood.ECSTATIC, FolhaMood.PROUD, FolhaMood.WAVE, FolhaMood.PRESENT)

private fun DrawScope.drawPet(t: Float, kind: String, mood: FolhaMood, small: Boolean, burst: Float) {
    if (kind == "nenhum" || small) return   // pequeno demais, só atrapalharia
    val party = mood in PARTY_MOODS; val sleepy = mood == FolhaMood.SLEEPY; val sad = mood == FolhaMood.SAD
    val hop = when { party -> -abs(sin(t * 5.2f + 0.8f)) * 3f; burst > 0f -> -sin(PIf * (1f - burst)) * 4f; sad -> 0.3f; else -> -abs(sin(t * 1.3f)) * 0.4f }
    var tearAt: Offset? = null
    val blink = sleepy || (t % 3.7f) < 0.13f
    val x = -9f; val base = 95f; val k = 1.45f
    scale(1f, 0.22f, pivot = Offset(x, base)) { drawCircle(Ink.copy(alpha = 0.2f), (7f + hop * 0.3f) * k, Offset(x, base)) }
    fun DrawScope.eye(ex: Float, ey: Float, r: Float = 0.75f) {
        if (sad && !sleepy) {   // olhinho triste: pupila menor, sobrancelha subindo por dentro e uma lágrima no primeiro olho
            val inner = if (ex < 0f) 1f else -1f
            drawCircle(Ink, r * 0.8f, Offset(ex, ey + 0.2f)); drawCircle(Color.White, r * 0.3f, Offset(ex - r * 0.25f, ey - 0.1f))
            line(Ink, ex - inner * r * 1.4f, ey - r * 1.5f, ex + inner * r * 1.1f, ey - r * 2.4f, 0.45f)
            if (tearAt == null) tearAt = Offset(ex, ey + r)
            return
        }
        if (blink) { drawPath(Path().apply { moveTo(ex - r * 1.2f, ey); quadraticTo(ex, ey + r * 0.9f, ex + r * 1.2f, ey) }, Ink, style = stroke(0.5f)); return }
        drawCircle(Ink, r, Offset(ex, ey)); drawCircle(Color.White, r * 0.4f, Offset(ex - r * 0.3f, ey - r * 0.35f))
    }
    fun DrawScope.smile(mx: Float, my: Float, w: Float = 1.1f) = drawPath(Path().apply { moveTo(mx - w, my); quadraticTo(mx, my + w * (if (party) 1.2f else 0.7f), mx + w, my) }, Ink, style = stroke(0.45f))
    translate(x, base + hop * k) {
        scale(k, k, pivot = Offset.Zero) {
            when (kind) {
                "planta" -> {
                    // vaso de barro com folhas que balançam (e uma flor quando ele comemora)
                    val sway = sin(t * 1.4f) * 0.15f
                    for ((a, len, c) in listOf(Triple(-0.55f, 9f, Color(0xFF2E9E72)), Triple(0.05f, 11f, MintDark), Triple(0.6f, 8.5f, Color(0xFF2E9E72)))) {
                        translate(0f, -8f) {
                            rotate((a + sway) * 180f / PIf, pivot = Offset.Zero) {
                                line(Color(0xFF2E9E72), 0f, 0f, 0f, -len * 0.7f, 0.6f)
                                oval(c, -2f, -len * 0.62f - len * 0.4f, 4f, len * 0.8f)
                                line(Color.White.copy(alpha = 0.35f), 0f, -len * 0.3f, 0f, -len * 0.9f, 0.3f)
                            }
                        }
                    }
                    if (party) { val fx = sin(t * 1.4f) * 1.2f; for (i in 0 until 5) { val a = i * 2f * PIf / 5f + t; drawCircle(Color(0xFFFF8FA3), 1.1f, Offset(fx + cos(a) * 1.3f, -19f + sin(a) * 1.3f)) }; drawCircle(Gold, 0.8f, Offset(fx, -19f)) }
                    drawPath(Path().apply { moveTo(-5f, -7f); lineTo(5f, -7f); lineTo(4f, 0f); lineTo(-4f, 0f); close() }, hg(listOf(Color(0xFFE8895A), Color(0xFFD9774B), Color(0xFFB85A33)), -5f, 5f))
                    drawPath(roundRect(-5.8f, -8.6f, 11.6f, 2.2f, 0.8f), Color(0xFFC4643A))
                    eye(-1.8f, -4.2f, 0.6f); eye(1.8f, -4.2f, 0.6f); smile(0f, -2.6f, 0.9f)
                    drawCircle(Cheek.copy(alpha = 0.6f), 0.7f, Offset(-3f, -3f)); drawCircle(Cheek.copy(alpha = 0.6f), 0.7f, Offset(3f, -3f))
                }
                "patinho" -> rotate(sin(t * 2f) * 0.06f * 180f / PIf, pivot = Offset.Zero) {
                    // patinho de borracha, balançando como na água
                    drawOval(vg(listOf(Color(0xFFFFE27A), Color(0xFFFFD84A), Color(0xFFF0B400)), -9f, 0f), Offset(-6.2f, -8.7f), Size(12.4f, 8.6f))
                    drawPath(Path().apply { moveTo(-5.4f, -6f); lineTo(-7.6f, -9f); lineTo(-4.2f, -7.6f); close() }, Color(0xFFF0B400))
                    drawPath(Path().apply { moveTo(-2.6f, -5.4f); quadraticTo(0.6f, -3f, 2.4f, -5f) }, Color(0xFFE0A000), style = stroke(0.6f))
                    drawCircle(Color(0xFFFFD84A), 3.5f, Offset(2.6f, -10.2f))
                    oval(Color(0xFFFF8A2B), 4f, -10.6f, 4f, 2f)
                    eye(3.6f, -11.1f, 0.7f); drawCircle(Cheek.copy(alpha = 0.7f), 0.8f, Offset(2.2f, -9.2f))
                    oval(Color.White.copy(alpha = 0.6f), -0.2f, -13f, 2.4f, 1.2f)
                }
                "gatinho" -> {
                    // gatinho sentado, rabo abanando e bigode
                    val tw = sin(t * 2.2f) * 2f
                    drawPath(Path().apply { moveTo(4f, -2f); quadraticTo(10f, -3f + tw * 0.3f, 8.5f + tw * 0.4f, -10f + tw) }, Color(0xFFD9803A), style = stroke(1.8f))
                    drawOval(vg(listOf(Color(0xFFF7B36E), Color(0xFFE58F45)), -9f, 0f), Offset(-5f, -9.4f), Size(10f, 9.6f))
                    oval(Color(0xFFFFE7CF), -2.6f, -6.6f, 5.2f, 6f)
                    for (s in listOf(-1f, 1f)) {
                        drawPath(Path().apply { moveTo(s * 3.8f, -14.4f); lineTo(s * 2.6f, -18.4f); lineTo(s * 0.6f, -15.4f); close() }, Color(0xFFE58F45))
                        drawPath(Path().apply { moveTo(s * 3f, -14.8f); lineTo(s * 2.5f, -17f); lineTo(s * 1.3f, -15.3f); close() }, Color(0xFFFFB3C7))
                    }
                    drawCircle(Color(0xFFF7B36E), 4.5f, Offset(0f, -12f))
                    line(Color(0x99C86E28), -1f, -16f, -1f, -14.6f, 0.5f); line(Color(0x99C86E28), 1f, -16f, 1f, -14.6f, 0.5f)
                    eye(-1.7f, -12.4f, 0.8f); eye(1.7f, -12.4f, 0.8f)
                    drawPath(Path().apply { moveTo(-0.5f, -10.9f); lineTo(0.5f, -10.9f); lineTo(0f, -10.3f); close() }, Color(0xFFFF8FA3))
                    drawPath(Path().apply { moveTo(-1f, -9.8f); quadraticTo(-0.5f, -9.2f, 0f, -9.9f); quadraticTo(0.5f, -9.2f, 1f, -9.8f) }, Ink, style = stroke(0.4f))
                    for (s in listOf(-1f, 1f)) { line(Color(0x8026215C), s * 1.6f, -10.4f, s * 4.8f, -10.9f, 0.25f); line(Color(0x8026215C), s * 1.6f, -10f, s * 4.6f, -9.6f, 0.25f) }
                    drawCircle(Cheek.copy(alpha = 0.6f), 0.8f, Offset(-2.8f, -10.6f)); drawCircle(Cheek.copy(alpha = 0.6f), 0.8f, Offset(2.8f, -10.6f))
                    for (s in listOf(-1f, 1f)) oval(Color(0xFFFFF4E6), s * 2f - 1.5f, -1.5f, 3f, 1.8f)
                }
                "coruja" -> {
                    // corujinha estudiosa, de capelo, batendo as asas quando comemora
                    val flap = if (party) sin(t * 12f) * 0.5f else 0f
                    for (s in listOf(-1f, 1f)) translate(s * 4.6f, -8f) { rotate(s * (0.3f + flap) * 180f / PIf, pivot = Offset.Zero) { oval(Color(0xFF7A5230), -1.8f, -2.5f, 3.6f, 8f) } }
                    drawOval(vg(listOf(Color(0xFFB9834C), Color(0xFF8C5E33)), -14f, 0f), Offset(-5.4f, -14f), Size(10.8f, 14f))
                    oval(Color(0xFFF1DDB3), -3.6f, -9f, 7.2f, 8.8f)
                    for (i in 0 until 3) drawPath(Path().apply { moveTo(-1.2f, -6f + i * 1.6f); lineTo(0f, -5.2f + i * 1.6f); lineTo(1.2f, -6f + i * 1.6f) }, Color(0x998C5E33), style = stroke(0.35f))
                    for (s in listOf(-1f, 1f)) drawPath(Path().apply { moveTo(s * 4.4f, -11.4f); lineTo(s * 4.4f, -14.6f); lineTo(s * 2.4f, -12.6f); close() }, Color(0xFF8C5E33))
                    for (s in listOf(-1f, 1f)) { drawCircle(Color.White, 2.3f, Offset(s * 2.2f, -10f)); drawCircle(Color(0xFFC98A4B), 2.3f, Offset(s * 2.2f, -10f), style = Stroke(0.4f)); eye(s * 2.2f, -10f, 1.1f) }
                    drawPath(Path().apply { moveTo(-0.8f, -8.6f); lineTo(0.8f, -8.6f); lineTo(0f, -7.2f); close() }, Color(0xFFFF8A2B))
                    drawPath(Path().apply { moveTo(0f, -17.2f); lineTo(5f, -15.4f); lineTo(0f, -13.6f); lineTo(-5f, -15.4f); close() }, Cap)
                    line(Gold, 0f, -15.4f, 4f, -15f, 0.4f); line(Gold, 4f, -15f, 4.2f + sin(t * 2.6f) * 0.4f, -12.6f, 0.45f)
                }
                "robo" -> {
                    // robozinho: antena piscando, rosto de tela e esteiras
                    drawPath(roundRect(-5.2f, -2.8f, 10.4f, 2.8f, 1.4f), Color(0xFF2D3042))
                    for (i in -1..1) drawCircle(Color(0xFF8E92A6), 0.7f, Offset(i * 3f, -1.4f))
                    drawPath(roundRect(-4.2f, -9.4f, 8.4f, 6.8f, 1.6f), vg(listOf(Color(0xFF5A4FE6), Color(0xFF3A2FC4)), -9.4f, -2.6f))
                    drawPath(heart(0f, -6.2f, 1.3f), Color(0xFFFF8FA3))
                    drawPath(roundRect(-5.2f, -17.6f, 10.4f, 8f, 2.2f), vg(listOf(Color(0xFFE3E6F0), Color(0xFFB7B4D0)), -17.6f, -9.6f))
                    drawPath(roundRect(-4f, -16.4f, 8f, 5.6f, 1.4f), Color(0xFF1E1B2E))
                    for (s in listOf(-1f, 1f)) { if (blink) line(Mint, s * 1.8f - 0.8f, -13.4f, s * 1.8f + 0.8f, -13.4f, 0.5f) else drawPath(roundRect(s * 1.8f - 0.7f, -14.6f, 1.4f, 2.2f, 0.6f), Mint) }
                    drawPath(Path().apply { moveTo(-1f, -12f); quadraticTo(0f, -11.2f, 1f, -12f) }, Mint, style = stroke(0.4f))
                    line(Color(0xFF8E92A6), 0f, -17.6f, 0f, -20.2f, 0.5f)
                    drawCircle(if (sin(t * 4f) > 0f) Color(0xFFFF5C4D) else Mint, 0.9f, Offset(0f, -20.6f))
                    line(Color.White.copy(alpha = 0.6f), -4.4f, -16.6f, -2.6f, -17f, 0.4f)
                }
                "coelho" -> {
                    // coelhinho: orelhas compridas que balançam, nariz que mexe e pompom de rabo
                    val ear = sin(t * 2.2f) * 0.12f; val twitch = abs(sin(t * 7f)) * 0.4f
                    for (s in listOf(-1f, 1f)) translate(s * 1.6f, -14f) {
                        rotate((s * 0.18f + ear * s + (if (party) sin(t * 9f) * 0.15f else 0f) + (if (sad) s * 0.9f else 0f)) * 180f / PIf, pivot = Offset.Zero) {
                            oval(Color(0xFFF6F2FF), -1.7f, -10.4f, 3.4f, 10.8f); drawOval(Color(0x99968CBE), Offset(-1.7f, -10.4f), Size(3.4f, 10.8f), style = Stroke(0.3f))
                            oval(Color(0xFFFFC2D4), -0.8f, -9.2f, 1.6f, 8.4f)
                        }
                    }
                    drawCircle(Color.White, 1.8f, Offset(-4.6f, -3f))
                    drawOval(vg(listOf(Color.White, Color(0xFFE6E1F5)), -9f, 0f), Offset(-5f, -9.2f), Size(10f, 9.2f)); drawOval(Color(0x80968CBE), Offset(-5f, -9.2f), Size(10f, 9.2f), style = Stroke(0.3f))
                    drawOval(vg(listOf(Color.White, Color(0xFFECE8F8)), -15.6f, -7.6f), Offset(-4.4f, -15.6f), Size(8.8f, 8f)); drawOval(Color(0x80968CBE), Offset(-4.4f, -15.6f), Size(8.8f, 8f), style = Stroke(0.3f))
                    eye(-1.6f, -12f, 0.75f); eye(1.6f, -12f, 0.75f)
                    drawPath(Path().apply { moveTo(-0.5f, -10.4f - twitch * 0.3f); lineTo(0.5f, -10.4f - twitch * 0.3f); lineTo(0f, -9.8f); close() }, Color(0xFFFF8FA3))
                    drawPath(Path().apply { moveTo(-0.9f, -9.2f); quadraticTo(-0.45f, -8.7f, 0f, -9.4f); quadraticTo(0.45f, -8.7f, 0.9f, -9.2f) }, Ink, style = stroke(0.35f))
                    drawCircle(Cheek.copy(alpha = 0.6f), 0.8f, Offset(-2.8f, -10.4f)); drawCircle(Cheek.copy(alpha = 0.6f), 0.8f, Offset(2.8f, -10.4f))
                    for (s in listOf(-1f, 1f)) oval(Color(0xFFF6F2FF), s * 2.1f - 1.6f, -1.4f, 3.2f, 1.8f)
                }
                "dragao" -> {
                    // dragãozinho: asas batendo, rabo com espinho e baforada de fogo quando comemora
                    val flap = sin(t * (if (party) 10f else 3f)) * 0.35f
                    for (s in listOf(-1f, 1f)) translate(-1f + s * 1.5f, -9f) {
                        rotate((-0.6f * s + flap * s) * 180f / PIf, pivot = Offset.Zero) {
                            drawPath(Path().apply { moveTo(0f, 0f); quadraticTo(s * -6f, -5f, s * -7f, 1f); quadraticTo(s * -4f, 0f, s * -3.5f, 2.4f); quadraticTo(s * -2f, 1f, 0f, 2f); close() }, Color(0xFF9563FF))
                        }
                    }
                    val tw = sin(t * 2f) * 0.8f
                    drawPath(Path().apply { moveTo(-3.5f, -2f); quadraticTo(-9f, -1f, -10.5f, -5f + tw) }, Color(0xFF1F9E75), style = stroke(1.6f))
                    drawPath(Path().apply { moveTo(-11.6f, -5.6f + tw); lineTo(-9.6f, -6.4f + tw); lineTo(-10.6f, -3.8f + tw); close() }, Color(0xFF9563FF))
                    drawOval(vg(listOf(Color(0xFF4FD3A2), Color(0xFF1F9E75)), -10f, 0f), Offset(-4.8f, -10.2f), Size(9.6f, 10f))
                    oval(Color(0xFFD9FBEE), -2f, -7.2f, 5.2f, 6.4f)
                    drawCircle(Color(0xFF36C897), 4f, Offset(2.2f, -12f))
                    oval(Color(0xFF36C897), 3f, -12.9f, 4.8f, 3.4f)
                    drawCircle(Ink, 0.3f, Offset(6.6f, -11.6f)); drawCircle(Ink, 0.3f, Offset(6.2f, -10.6f))
                    for (hx in listOf(0.4f, 2.8f)) drawPath(Path().apply { moveTo(hx, -15.2f); lineTo(hx + 0.6f, -17.6f); lineTo(hx + 1.4f, -15.2f); close() }, Gold)
                    eye(3.2f, -13f, 0.8f); drawCircle(Cheek.copy(alpha = 0.6f), 0.8f, Offset(1.2f, -11f))
                    if (party) {
                        val kk = 0.6f + 0.4f * abs(sin(t * 8f))
                        drawPath(Path().apply { moveTo(7.6f, -11.6f); quadraticTo(10f + 2f * kk, -14f, 12f + 2.5f * kk, -11.2f); quadraticTo(10f + 2f * kk, -9f, 7.6f, -10.6f); close() },
                            hg(listOf(Color(0xFFFFE07A), Color(0xFFFF9F3A), Color(0xFFF2552C)), 7.6f, 13f))
                    }
                }
            }
        }
    }
    tearAt?.let { tp ->   // a lágrima escorre (posição do olho no desenho do bichinho, já na escala dele)
        val ph = (t % 1.8f) / 1.8f; val a = if (ph < 0.15f) ph / 0.15f else 1f - maxOf(0f, ph - 0.7f) / 0.3f
        val tx = x + tp.x * k; val ty = base + hop * k + (tp.y + 1f + ph * 4f) * k
        drawPath(Path().apply { moveTo(tx, ty - 1.2f * k); quadraticTo(tx + 0.9f * k, ty + 0.3f * k, tx, ty + 0.7f * k); quadraticTo(tx - 0.9f * k, ty + 0.3f * k, tx, ty - 1.2f * k) }, Color(0xFF7EC8FF).copy(alpha = 0.9f * a))
    }
    if (sleepy) {   // "z" desenhado como traço (sem fonte)
        val zx = x + 8f; val zy = base - 26f + sin(t * 1.4f) * 1.5f; val s = 1.6f
        drawPath(Path().apply { moveTo(zx - s, zy - s); lineTo(zx + s, zy - s); lineTo(zx - s, zy + s); lineTo(zx + s, zy + s) }, Ink.copy(alpha = 0.6f + 0.4f * sin(t * 2f)), style = stroke(0.6f))
    }
}

// ---- armário: chapéus, óculos, peito, mão e aura (o mesmo desenho do site, folha-live.js)
// Tudo é desenhado dentro do corpo do Folha (pula, inclina, amassa e estica junto); chapéu gira com o
// capelo, óculos seguem o olhar, o item da mão segue a mão esquerda.

private val MUG_MOODS = setOf(FolhaMood.IDLE, FolhaMood.TALKING, FolhaMood.THINKING, FolhaMood.FOCUS, FolhaMood.WAVE, FolhaMood.POINT, FolhaMood.PROUD, FolhaMood.WINK)
private val MOON_MOODS = setOf(FolhaMood.IDLE, FolhaMood.SLEEPY, FolhaMood.FOCUS, FolhaMood.THINKING, FolhaMood.WINK)
private val GOLD3 = listOf(Color(0xFFFFF1BF), Gold, Color(0xFFC98A12))

private fun heart(x: Float, y: Float, r: Float) = Path().apply {
    moveTo(x, y + r * 0.9f)
    cubicTo(x - r * 1.5f, y - r * 0.1f, x - r * 0.9f, y - r * 1.25f, x, y - r * 0.45f)
    cubicTo(x + r * 0.9f, y - r * 1.25f, x + r * 1.5f, y - r * 0.1f, x, y + r * 0.9f); close()
}
private fun roundRect(x: Float, y: Float, w: Float, h: Float, r: Float) = Path().apply {
    addRoundRect(androidx.compose.ui.geometry.RoundRect(x, y, x + w, y + h, androidx.compose.ui.geometry.CornerRadius(r)))
}
private fun vg(colors: List<Color>, y0: Float, y1: Float) = Brush.verticalGradient(colors, startY = y0, endY = y1)
private fun hg(colors: List<Color>, x0: Float, x1: Float) = Brush.horizontalGradient(colors, startX = x0, endX = x1)

/** Padrão do papel (pautado, quadriculado, pontilhado, partitura), bem leve, por baixo do rosto. */
/** Papéis coloridos: o claro e a borda um pouco mais forte. */
private val PAPERS = mapOf(
    "creme" to (Color(0x99FFF0CD) to Color(0xB3F0D7A5)), "rosa" to (Color(0x99FFDAE6) to Color(0xB3FABED2)), "menta" to (Color(0x99D2F7E6) to Color(0xB3AAE6CD)),
    "ceu" to (Color(0xA6D6EAFF) to Color(0xBFAFD2FA)), "lavanda" to (Color(0x9EE8DEFF) to Color(0xB8CDBCFA)), "kraft" to (Color(0x9ED6B080) to Color(0xB8BE915F)),
)

/** O papel das páginas: pauta, quadriculado, pontilhado e papéis coloridos, sem rabisco na cara dele. */
private fun DrawScope.drawPagePattern(kind: String, left: Path, right: Path, ink: Color, t: Float) {
    if (kind == "lisa") return
    fun penPath(block: Path.() -> Unit, c: Color, w: Float) = drawPath(Path().apply(block), c, style = stroke(w))
    for (page in listOf(left, right)) clipPath(page) {
        when (kind) {
            "pautada" -> {
                var y = 44f; while (y < 88f) { line(Color(0x805A96F0), 10f, y, 90f, y + 1f, 0.28f, false); y += 3.6f }
                line(Color(0x8CE84669), 16f, 38f, 16f, 90f, 0.35f, false); line(Color(0x8CE84669), 84f, 38f, 84f, 90f, 0.35f, false)
            }
            "quadriculada" -> {
                var y = 39f; while (y < 90f) { line(Color(0x665A96F0), 10f, y, 90f, y, 0.22f, false); y += 3.2f }
                var x = 11.6f; while (x < 90f) { line(Color(0x665A96F0), x, 36f, x, 90f, 0.22f, false); x += 3.2f }
            }
            "pontilhada" -> { var y = 42f; while (y < 88f) { var x = 13f; while (x < 88f) { drawCircle(Color(0x73464678), 0.3f, Offset(x, y)); x += 3.2f }; y += 3.2f } }
            "partitura" -> for (y0 in listOf(43f, 79f)) for (i in 0 until 5) line(Color(0x5C322864), 10f, y0 + i * 1.3f, 90f, y0 + i * 1.3f + 0.6f, 0.22f, false)
            "creme", "rosa", "menta", "ceu", "lavanda", "kraft" -> {
                // papel colorido, liso, com a fibra bem de leve
                val (c1, c2) = PAPERS.getValue(kind)
                drawPath(page, Brush.horizontalGradient(listOf(c2, c1, c1, c2), startX = 10f, endX = 90f))
                if (kind == "kraft") for (i in 0 until 26) line(Color(0x29784F28), 12f + (i * 37) % 76, 40f + (i * 23) % 46, 14f + (i * 37) % 76, 40.4f + (i * 23) % 46, 0.25f)
            }
            "pergaminho" -> {
                drawPath(page, Color(0x6BE2BE78))
                drawPath(page, Brush.radialGradient(0f to Color(0x00965A1E), 0.39f to Color(0x00965A1E), 1f to Color(0x6B965A1E), center = Offset(50f, 64f), radius = 46f))
                for (i in 0 until 18) drawCircle(Color(0x1F8C5A28), 0.4f + (i % 3) * 0.3f, Offset(14f + (i * 37) % 72, 42f + (i * 23) % 44))
            }
            "aquarela" -> for ((x, y, r, c) in listOf(Quad(16f, 46f, 9f, Color(0xFFFF9DB8)), Quad(84f, 48f, 9f, Color(0xFF9FD3FF)), Quad(18f, 82f, 8f, Color(0xFFB9F2CF)), Quad(82f, 82f, 8f, Color(0xFFFFE36B)), Quad(50f, 42f, 7f, Color(0xFFC9B6FF)))) {
                drawCircle(Brush.radialGradient(0f to c.copy(alpha = 0.32f), 0.75f to c.copy(alpha = 0.42f), 0.92f to c.copy(alpha = 0.6f), 1f to c.copy(alpha = 0f), center = Offset(x, y), radius = r), r, Offset(x, y))
            }
            "ouro" -> {
                val goldLine = Color(0xCCC98A12)
                penPath({ moveTo(13f, 42.8f); cubicTo(23f, 39.6f, 36f, 39.2f, 47.6f, 45.6f); lineTo(47.6f, 85.4f); cubicTo(36f, 80.2f, 23f, 79.8f, 13f, 82.4f); close() }, goldLine, 0.45f)
                penPath({ moveTo(87f, 42.8f); cubicTo(77f, 39.6f, 64f, 39.2f, 52.4f, 45.6f); lineTo(52.4f, 85.4f); cubicTo(64f, 80.2f, 77f, 79.8f, 87f, 82.4f); close() }, goldLine, 0.45f)
                for ((x, y, sx, sy) in listOf(Quad(15f, 45f, 1f, 1f), Quad(85f, 45f, -1f, 1f), Quad(15f, 80f, 1f, -1f), Quad(85f, 80f, -1f, -1f))) {
                    penPath({ moveTo(x, y + sy * 5f); quadraticTo(x, y, x + sx * 5f, y); moveTo(x + sx * 1.6f, y + sy * 1.6f); quadraticTo(x + sx * 4f, y + sy * 0.6f, x + sx * 3.4f, y + sy * 3.2f); quadraticTo(x + sx * 2.2f, y + sy * 4.4f, x + sx * 1.6f, y + sy * 1.6f) }, Color(0xE6C98A12), 0.4f)
                    drawCircle(Gold, 0.6f, Offset(x + sx * 2.4f, y + sy * 2.4f))
                }
                val sh = (t * 0.25f) % 1f; val x = 110f * sh
                drawPath(page, Brush.linearGradient(listOf(Color(0x00FFECAA), Color(0x66FFECAA), Color(0x00FFECAA)), start = Offset(x - 8f, 30f), end = Offset(x + 8f, 92f)))
            }
        }
    }
}

private data class Quad<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

/** Textura da capa (só aparece nas bordas e no pé do livro, onde a capa fica à mostra). */
private fun DrawScope.drawCoverPattern(t: Float, c: CoverStyle, cover: Path) {
    when (c.pattern) {
        "leather" -> for (g in GRAIN) drawCircle(Color(0xFF281204).copy(alpha = g[3] * 1.3f), g[2] * 1.3f, Offset(if (g[0] < 50f) 8.5f + (g[0] - 15f) * 0.1f else 91.5f - (85f - g[0]) * 0.1f, g[1] + 6f))
        "denim" -> { var x = -50f; while (x < 100f) { line(Color.White.copy(alpha = 0.07f), x, 40f, x + 54f, 95f, 0.25f, false); x += 0.9f } }
        "plaid" -> {
            var x = 4f; while (x < 100f) { line(Color(0x47140A0A), x, 40f, x, 95f, 1.6f, false); line(Color(0x59FFE6A0), x + 2.5f, 40f, x + 2.5f, 95f, 0.3f, false); x += 6f }
            var y = 42f; while (y < 95f) { line(Color(0x47140A0A), 0f, y, 100f, y, 1.6f, false); line(Color(0x59FFE6A0), 0f, y + 2.5f, 100f, y + 2.5f, 0.3f, false); y += 6f }
        }
        "dots" -> { var y = 44f; while (y < 94f) { var x = if (y % 6.4f < 3.2f) 8.6f else 10.2f; while (x < 92f) { drawCircle(Color.White.copy(alpha = 0.75f), 0.55f, Offset(x, y)); x += 3.2f }; y += 3.2f } }
        "holo" -> { val h = (t * 40f) % 360f; drawPath(cover, Brush.linearGradient((0..4).map { Color.hsl((h + it * 72f) % 360f, 0.9f, 0.72f, 0.55f) }, start = Offset(0f, 40f), end = Offset(100f, 95f))) }
        "tweed" -> {
            // espinha de peixe: zigue-zague miúdo em duas tonalidades
            var y = 40f
            while (y < 96f) { var x = 6f; while (x < 96f) { val k = ((x + y) / 3.2f) % 2f < 1f; line(if (k) Color(0x29FFF0DC) else Color(0x381E140A), x, y, x + 1.6f, y + (if (k) 1.2f else -1.2f), 0.35f, false); x += 3.2f }; y += 1.6f }
        }
        "fleur" -> {
            // veludo com losangos dourados bordados
            var y = 44f
            while (y < 94f) { var x = if (y % 8f < 4f) 9f else 11f; while (x < 92f) { drawPath(Path().apply { moveTo(x, y - 0.9f); lineTo(x + 0.7f, y); lineTo(x, y + 0.9f); lineTo(x - 0.7f, y); close() }, Color(0x8CF5C451)); x += 4f }; y += 4f }
            drawPath(cover, Brush.linearGradient(listOf(Color(0x1AFFFFFF), Color(0x00FFFFFF), Color(0x1F000000)), start = Offset(8f, 40f), end = Offset(92f, 95f)))
        }
        "foil" -> { var x = -50f; while (x < 100f) { line(Color(0x73F5B83D), x, 40f, x + 54f, 95f, 0.25f, false); x += 7f } }
        "facets" -> { var x = -50f; while (x < 100f) { line(Color.White.copy(alpha = 0.35f), x, 40f, x + 30f, 95f, 0.3f, false); line(Color(0x405082B4), x + 30f, 40f, x, 95f, 0.3f, false); x += 5f } }
        else -> { var x = -50f; while (x < 100f) { line(Color.White.copy(alpha = 0.045f), x, 40f, x + 54f, 95f, 0.3f, false); line(Color.Black.copy(alpha = 0.06f), x + 54f, 40f, x, 95f, 0.3f, false); x += 1.5f } }
    }
    c.stitch?.let { sc ->   // costura pontilhada rente à borda
        val st = Path().apply { moveTo(10.2f, 45.8f); lineTo(10.2f, 87.4f); cubicTo(22f, 85.3f, 38f, 86.2f, 50f, 91.4f); cubicTo(62f, 86.2f, 78f, 85.3f, 89.8f, 87.4f); lineTo(89.8f, 45.8f) }
        drawPath(st, sc, style = Stroke(0.35f, cap = StrokeCap.Round, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(1.1f, 0.9f))))
    }
    if (c.pattern == "stars") for (st in COVER_STARS) drawCircle(Color(0xFFFFF8D6).copy(alpha = 0.55f + 0.45f * abs(sin(t * 1.3f + st[0]))), st[2], Offset(st[0], st[1]))
    if (c.pattern == "shine" || c.pattern == "facets" || c.pattern == "foil") {   // um brilho passando de tempos em tempos
        val sx = -30f + ((t * 0.32f) % 1f) * 170f; val a = if (c.pattern == "shine") 0.55f else 0.4f
        drawPath(cover, Brush.linearGradient(listOf(Color.White.copy(alpha = 0f), Color.White.copy(alpha = a), Color.White.copy(alpha = 0f)), start = Offset(sx, 40f), end = Offset(sx + 18f, 95f)))
    }
}

// ---- chapéus (no lugar do capelo; dentro do giro do capelo)
/** Sombra de contato: o chapéu assenta na cabeça (no alto das páginas) em vez de flutuar. */
private fun DrawScope.hatShadow(w: Float) {
    drawOval(Brush.radialGradient(listOf(Color(0x471A1350), Color(0x001A1350)), center = Offset(50f, 45.6f), radius = w), Offset(50f - w, 45.6f - 2.4f), Size(w * 2, 4.8f))
}

private fun DrawScope.drawBone() {
    // boné de frente, aba curva para a frente, gomos costurados, botão e o escudo bordado
    hatShadow(20f)
    val crown = Path().apply { moveTo(32.5f, 44.5f); cubicTo(31.5f, 28.5f, 40f, 21.5f, 50f, 21.5f); cubicTo(60f, 21.5f, 68.5f, 28.5f, 67.5f, 44.5f); quadraticTo(50f, 41f, 32.5f, 44.5f); close() }
    drawPath(crown, vg(listOf(Color(0xFF6A5FF0), Color(0xFF4237D6), Color(0xFF2A209E)), 21f, 45f))
    clipPath(crown) {
        drawCircle(Brush.radialGradient(listOf(Color(0x59FFFFFF), Color(0x00FFFFFF)), center = Offset(42f, 27f), radius = 13f), 13f, Offset(42f, 27f))
        for ((x0, cx) in listOf(41f to 38f, 59f to 62f)) drawPath(Path().apply { moveTo(50f, 21.8f); quadraticTo(cx, 30f, x0 - (if (x0 < 50f) 2f else -2f), 43.5f) }, Color(0x59120D3F), style = stroke(0.5f))
        line(Color(0x59120D3F), 50f, 21.8f, 50f, 28.6f, 0.5f)
    }
    drawPath(crown, Color(0x80120D3F), style = stroke(0.5f))
    // escudo bordado: um livrinho aberto
    drawPath(roundRect(44f, 28.6f, 12f, 8.6f, 2.4f), Paper); drawPath(roundRect(44f, 28.6f, 12f, 8.6f, 2.4f), Color(0x40120D3F), style = stroke(0.35f))
    drawPath(Path().apply { moveTo(46f, 31.4f); quadraticTo(48f, 30.4f, 50f, 31.6f); quadraticTo(52f, 30.4f, 54f, 31.4f); lineTo(54f, 35.2f); quadraticTo(52f, 34.2f, 50f, 35.4f); quadraticTo(48f, 34.2f, 46f, 35.2f); close() }, Color(0xFF3A2FC4))
    line(Paper, 50f, 31.8f, 50f, 35f, 0.35f)
    // aba curva, com costura e borda clara
    val visor = Path().apply { moveTo(30.5f, 43.2f); quadraticTo(50f, 39.4f, 69.5f, 43.2f); quadraticTo(72.4f, 47.6f, 66.4f, 48.8f); quadraticTo(50f, 45.4f, 33.6f, 48.8f); quadraticTo(27.6f, 47.6f, 30.5f, 43.2f); close() }
    drawPath(visor, vg(listOf(Color(0xFF3A2FC4), Color(0xFF1A1370)), 40f, 49f))
    drawPath(Path().apply { moveTo(31.4f, 43.6f); quadraticTo(50f, 40f, 68.6f, 43.6f) }, Color.White.copy(alpha = 0.35f), style = stroke(0.5f))
    drawPath(Path().apply { moveTo(33.4f, 47.2f); quadraticTo(50f, 44.2f, 66.6f, 47.2f) }, Color.White.copy(alpha = 0.4f),
        style = Stroke(0.3f, cap = StrokeCap.Round, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.9f, 0.7f))))
    drawCircle(Mint, 1.6f, Offset(50f, 21.8f)); drawCircle(Color.White.copy(alpha = 0.7f), 0.55f, Offset(49.5f, 21.3f))
}

private fun DrawScope.drawBoina() {
    // boina de artista, de lado, com a faixa abraçando a cabeça e dobras macias
    hatShadow(18f)
    drawPath(Path().apply { moveTo(34.6f, 41.6f); quadraticTo(50f, 39f, 65.4f, 41.6f); lineTo(65f, 45.4f); quadraticTo(50f, 42.6f, 35f, 45.4f); close() }, vg(listOf(Color(0xFF8E1A2E), Color(0xFF6B1222)), 40f, 46f))
    translate(47.5f, 34.6f) {
        rotate(-0.12f * 180f / PIf, pivot = Offset.Zero) {
            val top = Path().apply { moveTo(-21f, 2f); cubicTo(-23f, -6f, -10f, -11f, 1f, -10.4f); cubicTo(14f, -10f, 24f, -5f, 21.6f, 1.6f); cubicTo(20f, 6.6f, 8f, 8.4f, 0f, 8f); cubicTo(-10f, 7.6f, -20f, 6.4f, -21f, 2f); close() }
            drawPath(top, Brush.radialGradient(listOf(Color(0xFFF27387), Color(0xFFD23A55), Color(0xFF9C1F36)), center = Offset(-7f, -4f), radius = 23f))
            drawPath(Path().apply { moveTo(-16f, 3.4f); quadraticTo(0f, 8.6f, 17f, 3f) }, Color(0x59640A1E), style = stroke(0.6f))
            drawPath(Path().apply { moveTo(-10f, -4.6f); quadraticTo(-2f, -6.6f, 8f, -5.4f) }, Color.White.copy(alpha = 0.25f), style = stroke(0.6f))
        }
    }
    drawPath(Path().apply { moveTo(43.6f, 25.6f); quadraticTo(43f, 23.6f, 44.4f, 22.8f) }, Color(0xFF7A1428), style = stroke(1.2f))
}

private fun DrawScope.drawExplorador() {
    // chapéu de explorador: copa redonda, aba larga com costura, fita marrom e o broche
    hatShadow(24f)
    drawOval(vg(listOf(Color(0xFFE2C68C), Color(0xFFB8955A), Color(0xFF94743F)), 37f, 48.4f), Offset(24f, 37.2f), Size(52f, 11.2f))
    drawArc(Color(0x735A3C19), 9f, 162f, false, Offset(26.4f, 38.2f), Size(47.2f, 9.2f),
        style = Stroke(0.35f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(1f, 0.8f))))
    val dome = Path().apply { moveTo(35f, 42f); cubicTo(34f, 26.5f, 42f, 21.5f, 50f, 21.5f); cubicTo(58f, 21.5f, 66f, 26.5f, 65f, 42f); quadraticTo(50f, 44.4f, 35f, 42f); close() }
    drawPath(dome, vg(listOf(Color(0xFFEFDCAE), Color(0xFFCFAE6F), Color(0xFFA8854B)), 21f, 43f))
    clipPath(dome) {
        drawCircle(Brush.radialGradient(listOf(Color(0x59FFFFFF), Color(0x00FFFFFF)), center = Offset(43f, 27f), radius = 12f), 12f, Offset(43f, 27f))
        drawPath(Path().apply { moveTo(50f, 22f); quadraticTo(48.6f, 30f, 50f, 37.5f) }, Color(0x596E5028), style = stroke(0.6f))
    }
    drawPath(Path().apply { moveTo(35.3f, 37.4f); quadraticTo(50f, 39.6f, 64.7f, 37.4f); lineTo(64.9f, 41.4f); quadraticTo(50f, 43.8f, 35.1f, 41.4f); close() }, vg(listOf(Color(0xFF7A5530), Color(0xFF5A3D20)), 37f, 42f))
    drawCircle(Gold, 1.6f, Offset(59.2f, 40f)); drawPath(star5(59.2f, 40f, 1f), Color(0xFF7A4F05))
}

private fun DrawScope.drawFones(t: Float) {
    // headset: arco acolchoado, conchas grandes nas bordas do livro, luz menta e microfone até a boca
    val arc = Path().apply { moveTo(11f, 52f); cubicTo(9.5f, 14.5f, 90.5f, 14.5f, 89f, 52f) }
    drawPath(arc, Color(0xFF1E1B2E), style = stroke(4.4f)); drawPath(arc, Color(0xFF3A3550), style = stroke(2.6f))
    val pad = Path().apply { moveTo(36f, 25.4f); quadraticTo(50f, 20.6f, 64f, 25.4f) }
    drawPath(pad, Color(0xFF2A209E), style = stroke(5.2f)); drawPath(pad, Color(0xFF4B3FD8), style = stroke(3.6f))
    line(Color.White.copy(alpha = 0.35f), 40f, 23.8f, 49f, 21.8f, 0.9f)
    for (side in listOf(-1f, 1f)) {
        val x = if (side < 0) 3.6f else 86.4f
        drawPath(roundRect(x, 46.5f, 10f, 18.5f, 4.6f), hg(if (side < 0) listOf(Color(0xFF2A209E), Color(0xFF4B3FD8)) else listOf(Color(0xFF4B3FD8), Color(0xFF2A209E)), x, x + 10f))
        drawPath(roundRect(if (side < 0) x + 6.6f else x - 0.4f, 47.6f, 3.8f, 16.3f, 1.9f), Color(0xFF24242E))
        drawOval(Mint, Offset(x + 5f - 2.6f, 55.8f - 4.6f), Size(5.2f, 9.2f), style = Stroke(0.9f))
        drawCircle(Mint.copy(alpha = 0.6f + 0.4f * sin(t * 3f + side)), 0.8f, Offset(x + 5f, 55.8f))
        val hx = x + (if (side < 0) 1.8f else 7.6f); line(Color.White.copy(alpha = 0.35f), hx, 48.6f, hx, 54f, 0.8f)
    }
    // microfone saindo da concha direita
    drawPath(Path().apply { moveTo(88f, 62.5f); cubicTo(84f, 74f, 72f, 77.5f, 63.5f, 76.2f) }, Color(0xFF1E1B2E), style = stroke(1.4f))
    rotate(-0.15f * 180f / PIf, pivot = Offset(62.4f, 76f)) { oval(Color(0xFF3A3550), 60f, 74.3f, 4.8f, 3.4f) }
    drawCircle(Color.White.copy(alpha = 0.45f), 0.5f, Offset(61.8f, 75.4f))
}

private fun DrawScope.drawCartola() {
    // cartola de gala, levemente inclinada, com faixa dourada e brilho de cetim
    hatShadow(19f)
    rotate(-0.07f * 180f / PIf, pivot = Offset(50f, 43f)) {
        drawOval(vg(listOf(Color(0xFF45405E), Color(0xFF14121F)), 38f, 47f), Offset(30.5f, 38.4f), Size(39f, 8.4f))
        val body = Path().apply { moveTo(37.8f, 42f); lineTo(39f, 13.5f); quadraticTo(50f, 11.6f, 61f, 13.5f); lineTo(62.2f, 42f); quadraticTo(50f, 44.6f, 37.8f, 42f); close() }
        drawPath(body, hg(listOf(Color(0xFF1A1726), Color(0xFF423C60), Color(0xFF2A2640), Color(0xFF15121F)), 38f, 62f))
        drawPath(Path().apply { moveTo(38.3f, 35f); quadraticTo(50f, 36.4f, 61.7f, 35f); lineTo(62f, 39.4f); quadraticTo(50f, 41.2f, 38f, 39.4f); close() }, vg(GOLD3, 35f, 40f))
        drawOval(vg(listOf(Color(0xFF4A4466), Color(0xFF2A2640)), 11f, 16f), Offset(39f, 11f), Size(22f, 4.8f))
        line(Color.White.copy(alpha = 0.28f), 42.6f, 16f, 41.8f, 33.6f, 1.1f)
        line(Color.White.copy(alpha = 0.12f), 45f, 16f, 44.4f, 33.6f, 0.6f)
    }
}

private fun DrawScope.drawCoroa(t: Float) {
    // coroa de ouro com veludo por dentro, pérolas nas pontas e pedras
    hatShadow(19f)
    drawPath(Path().apply { moveTo(35f, 41f); quadraticTo(50f, 22f, 65f, 41f); close() }, vg(listOf(Color(0xFFC42F4B), Color(0xFF7A1428)), 24f, 41f))
    val c = Path().apply {
        moveTo(33.4f, 45f); lineTo(32.6f, 28.5f); lineTo(39.6f, 34.6f); lineTo(42.6f, 23.6f); lineTo(46.8f, 32.6f); lineTo(50f, 18.6f); lineTo(53.2f, 32.6f); lineTo(57.4f, 23.6f)
        lineTo(60.4f, 34.6f); lineTo(67.4f, 28.5f); lineTo(66.6f, 45f); quadraticTo(50f, 41.8f, 33.4f, 45f); close()
    }
    drawPath(c, vg(listOf(Color(0xFFFFF1BF), Color(0xFFF5C451), Color(0xFFD99A1B), Color(0xFFB57A0C)), 18f, 45f)); drawPath(c, Color(0x8C784600), style = stroke(0.4f))
    drawPath(Path().apply { moveTo(33.2f, 39.4f); quadraticTo(50f, 36.6f, 66.8f, 39.4f); lineTo(66.7f, 41.6f); quadraticTo(50f, 38.8f, 33.3f, 41.6f); close() }, Color(0x73A0640A))
    for ((x, y) in listOf(32.6f to 28.5f, 42.6f to 23.6f, 50f to 18.6f, 57.4f to 23.6f, 67.4f to 28.5f)) { drawCircle(Color(0xFFFFFDF4), 1.35f, Offset(x, y)); drawCircle(Color(0x99C8AA78), 0.5f, Offset(x + 0.4f, y + 0.4f)) }
    for ((x, col, r) in listOf(Triple(50f, Color(0xFFE0475E), 2f), Triple(42f, Color(0xFF3D8BEA), 1.4f), Triple(58f, Color(0xFF36C897), 1.4f))) {
        oval(col, x - r, 42.4f - r * 1.2f, r * 2, r * 2.4f); drawCircle(Color.White.copy(alpha = 0.7f), r * 0.35f, Offset(x - r * 0.35f, 42.4f - r * 0.45f))
    }
    val k = maxOf(0f, sin(t * 1.4f)).pow(8); if (k > 0.02f) star4(46.5f, 27f, 3.4f * k, Color.White.copy(alpha = k))
}

private fun DrawScope.drawMago(t: Float, tilt: Float, vy: Float) {
    // chapéu de mago de veludo: aba larga, ponta que tomba e balança, lua e estrelas bordadas
    hatShadow(21f)
    val sway = sin(t * 1.8f) * 1.6f - tilt * 0.5f + clamp(vy * 0.05f, -3f, 3f)
    drawOval(vg(listOf(Color(0xFF5A3DC4), Color(0xFF2A1A78), Color(0xFF1C1155)), 38f, 47f), Offset(27.5f, 38.2f), Size(45f, 8.8f))
    val tipX = 63f + sway; val tipY = 12f + abs(sway) * 0.3f
    val cone = Path().apply {
        moveTo(33.4f, 42f); cubicTo(38.6f, 32f, 43f, 18f, 51f, 10.6f); quadraticTo(57f, 6.4f, tipX + 2f, tipY - 1.6f)
        quadraticTo(tipX - 1f, tipY + 2.4f, 57.6f, 16f); cubicTo(58.4f, 26f, 61.6f, 34f, 66.6f, 42f); quadraticTo(50f, 38.8f, 33.4f, 42f); close()
    }
    drawPath(cone, hg(listOf(Color(0xFF7B5CE6), Color(0xFF5A3DC4), Color(0xFF3A2788)), 34f, 66f))
    clipPath(cone) {
        drawCircle(Brush.radialGradient(listOf(Color(0x47FFFFFF), Color(0x00FFFFFF)), center = Offset(43f, 26f), radius = 14f), 14f, Offset(43f, 26f))
        drawCircle(vg(GOLD3, 27.8f, 34.2f), 3.2f, Offset(45.6f, 31f)); drawCircle(Color(0xFF5A3DC4), 2.7f, Offset(47f, 30f))
        for ((x, y, r) in listOf(Triple(54f, 25f, 1.7f), Triple(50.4f, 37.2f, 1.2f), Triple(55.8f, 34f, 1f), Triple(52f, 17f, 1.1f))) drawPath(star5(x, y, r), vg(GOLD3, y - r, y + r))
    }
    drawPath(Path().apply { moveTo(34.4f, 40.4f); quadraticTo(50f, 37.4f, 65.6f, 40.4f); lineTo(66.2f, 42.8f); quadraticTo(50f, 39.8f, 33.8f, 42.8f); close() }, vg(GOLD3, 37.4f, 42.8f))
    drawCircle(Color(0xFFB995F5), 1.1f, Offset(50f, 40.2f)); drawCircle(Color.White.copy(alpha = 0.7f), 0.4f, Offset(49.7f, 39.9f))
    val k = 0.6f + 0.4f * sin(t * 3f); star4(tipX + 2.2f, tipY - 1.6f, 2.4f * k, Color(0xFFFFF0B4).copy(alpha = 0.95f * k))
}

private fun DrawScope.drawAureola(t: Float) {
    // auréola de luz: anel dourado que flutua e brilha, com faíscas girando
    val y = 22.5f + sin(t * 2f) * 1.2f
    drawOval(Color(0x66FFD66E), Offset(50f - 14.5f, y - 4.4f), Size(29f, 8.8f), style = Stroke(4f))
    drawOval(vg(listOf(Color(0xFFFFF6D6), Color(0xFFFFD877), Color(0xFFE9A82B)), y - 3.4f, y + 3.4f), Offset(36.5f, y - 3.4f), Size(27f, 6.8f), style = Stroke(1.6f))
    drawArc(Color.White.copy(alpha = 0.9f), 189f, 99f, false, Offset(36.5f, y - 3.4f), Size(27f, 6.8f), style = stroke(0.5f))
    for (i in 0 until 3) { val a = t * 1.4f + i * 2f * PIf / 3f; val k = 0.5f + 0.5f * sin(t * 4f + i); star4(50f + cos(a) * 13.5f, y + sin(a) * 3.4f, 1.6f * k, Color.White.copy(alpha = 0.9f * k)) }
}

/** O chapéu escolhido (no frame girado do capelo). */
private fun DrawScope.drawHat(f: FolhaFrame, look: Look, tilt: Float) {
    val t = f.t; val small = f.small
    when (look.hat) {
        "bone" -> drawBone()
        "boina" -> drawBoina()
        "explorador" -> drawExplorador()
        "fones" -> drawFones(t)
        "cartola" -> drawCartola()
        "coroa" -> drawCoroa(t)
        "mago" -> drawMago(t, tilt, f.vy)
        "aureola" -> drawAureola(t)
        else -> {
            drawCap(t, tilt, f.vy, gold = look.hat == "capelo-ouro")
            if (look.hat == "louros") drawLaurels(small)
            if (look.streak >= 7 && !small) drawStreakMedal(t, look.streak, tilt)
        }
    }
}

// ---- óculos (seguem o rosto, um pouco menos que os olhos)
private fun DrawScope.drawEyewear(p: PoseValues, small: Boolean, kind: String, t: Float) {
    if (kind == "nenhum") return
    val dx = p.lookX * 0.7f; val dy = p.lookY * 0.5f; val w = if (small) 1.5f else 0.95f
    val eyes = listOf(Offset(31f + dx, 61f + dy), Offset(69f + dx, 61f + dy))
    fun bridge(color: Color, lw: Float, y0: Float = 60f, y1: Float = 56.8f) = drawPath(Path().apply { moveTo(38f + dx, y0 + dy); quadraticTo(50f + dx, y1 + dy, 62f + dx, y0 + dy) }, color, style = stroke(lw))
    fun arms(color: Color, lw: Float) { line(color, 24f + dx, 59.5f + dy, 18.5f, 58f, lw); line(color, 76f + dx, 59.5f + dy, 81.5f, 58f, lw) }
    fun glare(c: Offset, rx: Float, ry: Float) { if (!small) drawArc(Color.White.copy(alpha = 0.8f), 194f, 61f, false, Offset(c.x - rx, c.y - ry), Size(rx * 2, ry * 2), style = stroke(0.55f)) }
    when (kind) {
        "leitura" -> {
            // armação redonda de tartaruga, lente levemente azulada e plaquetas no nariz
            for (c in eyes) {
                oval(Color(0x29C8E6FF), c.x - 7.2f, c.y - 7f, 14.4f, 14f)
                drawOval(vg(listOf(Color(0xFFC98A4B), Color(0xFF8A5A2B), Color(0xFFB87A3F)), c.y - 7f, c.y + 7f), Offset(c.x - 7.2f, c.y - 7f), Size(14.4f, 14f), style = Stroke(if (small) 1.8f else 1.4f))
                drawArc(Color(0xB3FFE2B4), 198f, 72f, false, Offset(c.x - 6.5f, c.y - 6.3f), Size(13f, 12.6f), style = stroke(0.35f))
                glare(c, 5.6f, 5.4f)
            }
            val frame = Color(0xFF8A5A2B)
            bridge(frame, if (small) 1.6f else 1.2f, 59.6f, 57f); arms(frame, if (small) 1.6f else 1.1f)
            if (!small) for (x in listOf(44.6f, 55.4f)) drawCircle(Color.White.copy(alpha = 0.6f), 0.6f, Offset(x + dx, 63.4f + dy))
        }
        "escuros" -> {
            // óculos escuros de armação grossa: lente em degradê com reflexo; as sobrancelhas continuam falando
            val lw = if (small) 2f else 1.5f; val frame = Color(0xFF14111F)
            for (c in eyes) {
                val x = c.x; val y = c.y
                val l = Path().apply {
                    moveTo(x - 8f, y - 5.6f); lineTo(x + 8f, y - 5.6f); quadraticTo(x + 8.8f, y - 5.6f, x + 8.5f, y - 4.4f); lineTo(x + 7.2f, y + 3.6f)
                    quadraticTo(x + 6.7f, y + 6f, x + 4.4f, y + 6f); lineTo(x - 4.4f, y + 6f); quadraticTo(x - 6.7f, y + 6f, x - 7.2f, y + 3.6f); lineTo(x - 8.5f, y - 4.4f); quadraticTo(x - 8.8f, y - 5.6f, x - 8f, y - 5.6f); close()
                }
                drawPath(l, vg(listOf(Color(0xFF463F72), Color(0xFF221D3A), Color(0xFF120F1F)), y - 6f, y + 6f))
                if (!small) clipPath(l) { line(Color.White.copy(alpha = 0.28f), x - 7f, y + 7f, x + 1f, y - 7f, 3f); line(Color.White.copy(alpha = 0.18f), x - 1f, y + 7f, x + 4f, y - 7f, 1f) }
                drawPath(l, frame, style = stroke(lw)); line(frame, x - 8.2f, y - 5.2f, x + 8.2f, y - 5.2f, lw * 1.5f)
            }
            bridge(frame, lw * 1.2f, 56.4f, 55.2f); arms(frame, lw)
        }
        "nerd" -> {
            // armação grossa de acetato com brilho e um esparadrapo no meio
            val lw = if (small) 2.2f else 1.8f; val frame = Color(0xFF1E1B2E)
            for (c in eyes) {
                val r = roundRect(c.x - 7.4f, c.y - 5.8f, 14.8f, 12f, 3.2f)
                drawPath(r, Color(0x24C8E6FF)); drawPath(r, frame, style = stroke(lw))
                if (!small) drawPath(Path().apply { moveTo(c.x - 6f, c.y - 5.2f); quadraticTo(c.x, c.y - 6.2f, c.x + 6f, c.y - 5.2f) }, Color.White.copy(alpha = 0.4f), style = stroke(0.4f))
                glare(c, 5.2f, 4.4f)
            }
            bridge(frame, lw, 59.2f, 58.6f); arms(frame, lw)
            if (!small) { drawPath(roundRect(48f + dx, 57.4f + dy, 4f, 3f, 0.5f), Paper); drawPath(roundRect(48f + dx, 57.4f + dy, 4f, 3f, 0.5f), Color(0x2E000000), style = Stroke(0.2f)) }
        }
        "estrela" -> {
            // óculos de festa em estrela: armação rosa em degradê, lente cintilante e brilhinhos
            val lw = if (small) 1.8f else 1.3f
            for (c in eyes) {
                val s = star5(c.x, c.y, 8.8f); drawPath(s, Color(0x40FFB4DC))
                drawPath(s, vg(listOf(Color(0xFFFF8FC4), Color(0xFFF0559A), Color(0xFFC2357A)), c.y - 9f, c.y + 9f), style = stroke(lw))
                if (!small) for (i in 0 until 4) { val a = t * 0.8f + i * 1.6f; drawCircle(Color.White.copy(alpha = 0.5f + 0.5f * sin(t * 4f + i)), 0.35f, Offset(c.x + cos(a) * 4f, c.y + sin(a) * 3.4f)) }
            }
            bridge(Color(0xFFF0559A), lw, 59f, 57.5f); arms(Color(0xFFF0559A), lw)
            if (!small) { val k = 0.5f + 0.5f * sin(t * 3f); star4(eyes[1].x + 7f, eyes[1].y - 7.5f, 2f * k, Color.White.copy(alpha = 0.9f * k)); star4(eyes[0].x - 7f, eyes[0].y + 5f, 1.4f * (1 - k), Color.White.copy(alpha = 0.9f * (1 - k))) }
        }
        "coracao" -> {
            // óculos de coração: armação vermelha em degradê com lente rosada
            val lw = if (small) 1.8f else 1.3f
            for (c in eyes) {
                val hp = heart(c.x, c.y + 0.6f, 7f); drawPath(hp, Color(0x4DFF5A82))
                drawPath(hp, vg(listOf(Color(0xFFFF7A95), Color(0xFFD63A5E), Color(0xFFA8233A)), c.y - 7f, c.y + 7f), style = stroke(lw))
                if (!small) drawPath(Path().apply { moveTo(c.x - 4.4f, c.y - 1.4f); quadraticTo(c.x - 4f, c.y - 4f, c.x - 1.6f, c.y - 3.4f) }, Color.White.copy(alpha = 0.75f), style = stroke(0.5f))
            }
            bridge(Color(0xFFD63A5E), lw, 58.4f, 56.6f); arms(Color(0xFFD63A5E), lw)
        }
        "monoculo" -> {
            // monóculo de ouro com aro chanfrado e corrente de elos
            val c = eyes[1]
            oval(Color(0x24DCF0FF), c.x - 7.3f, c.y - 7.3f, 14.6f, 14.6f)
            drawOval(vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), c.y - 7.3f, c.y + 7.3f), Offset(c.x - 7.3f, c.y - 7.3f), Size(14.6f, 14.6f), style = Stroke(if (small) 2f else 1.6f))
            drawOval(Color(0x66784600), Offset(c.x - 6.4f, c.y - 6.4f), Size(12.8f, 12.8f), style = Stroke(0.3f))
            glare(c, 5.8f, 5.8f)
            if (!small) for (i in 0 until 9) {
                val u = i / 8f; val cx = c.x + 5.4f + u * 8.6f + sin(u * PIf) * 1.4f; val cy = c.y + 5.2f + u * 8.4f + sin(u * PIf) * 2.6f
                rotate(u * 2f * 180f / PIf, pivot = Offset(cx, cy)) { drawOval(Gold, Offset(cx - 0.55f, cy - 0.35f), Size(1.1f, 0.7f), style = Stroke(0.25f)) }
            }
        }
    }
}

// ---- peito (embaixo da boca, no pé das páginas)
private fun DrawScope.drawNeckwear(t: Float, small: Boolean, kind: String) {
    when (kind) {
        "borboleta" -> {
            val x = 50f; val y = 83.4f; val red = Color(0xFFE0475E); val dk = Color(0xFFA8233A)
            for (side in listOf(-1f, 1f)) {
                val p = Path().apply { moveTo(x, y); quadraticTo(x + side * 3f, y - 3.6f, x + side * 6f, y - 2.6f); quadraticTo(x + side * 6.8f, y, x + side * 6f, y + 2.6f); quadraticTo(x + side * 3f, y + 3.6f, x, y); close() }
                drawPath(p, hg(if (side < 0) listOf(red, dk) else listOf(dk, red), if (side < 0) x - 6f else x, if (side < 0) x else x + 6f))
                if (!small) drawPath(Path().apply { moveTo(x + side * 1.5f, y - 0.6f); quadraticTo(x + side * 3.6f, y - 1.2f, x + side * 5f, y - 0.4f) }, Color.White.copy(alpha = 0.35f), style = stroke(0.35f))
            }
            oval(dk, x - 1.6f, y - 1.9f, 3.2f, 3.8f); drawCircle(Color.White.copy(alpha = 0.35f), 0.45f, Offset(x - 0.5f, y - 0.7f))
        }
        "gravata" -> scale(1.3f, 1.25f, pivot = Offset(50f, 80f)) {
            // gravata listrada de verdade: nó, lâmina larga com ponta e sombra de um lado
            val sw = sin(t * 1.9f) * 0.8f
            val blade = Path().apply { moveTo(48.2f, 83.4f); lineTo(51.8f, 83.4f); lineTo(54.6f + sw, 94f); lineTo(50f + sw, 98.6f); lineTo(45.4f + sw, 94f); close() }
            drawPath(blade, hg(listOf(Color(0xFF5A4FE6), Color(0xFF3A2FC4), Color(0xFF2A209E)), 45f, 55f))
            clipPath(blade) {
                for (i in 0 until 7) { val yy = 83f + i * 2.4f; line(Gold.copy(alpha = 0.9f), 42f, yy + 4f, 58f, yy - 2f, 0.8f, false); line(Color.White.copy(alpha = 0.18f), 42f, yy + 5.2f, 58f, yy - 0.8f, 0.4f, false) }
                drawRect(Color(0x2E0A0628), Offset(50f + sw * 0.5f, 82f), Size(6f, 18f))
            }
            drawPath(blade, Color(0x73120D3F), style = stroke(0.35f))
            val knot = Path().apply { moveTo(47f, 79.8f); lineTo(53f, 79.8f); lineTo(51.8f, 83.8f); lineTo(48.2f, 83.8f); close() }
            drawPath(knot, vg(listOf(Color(0xFF4B3FD8), Color(0xFF2A209E)), 79.8f, 83.8f)); drawPath(knot, Color(0x73120D3F), style = stroke(0.35f))
            line(Color.White.copy(alpha = 0.35f), 47.8f, 80.6f, 52.2f, 80.6f, 0.5f)
        }
        "cachecol" -> {
            // cachecol de tricô: faixa com listras e textura, nó do lado e duas pontas com franja
            val sw = sin(t * 1.7f) * 0.8f
            fun DrawScope.stripes(path: Path, x0: Float, x1: Float) = clipPath(path) {
                drawRect(Color(0xFFE0475E), Offset(x0, 70f), Size(x1 - x0, 32f))
                var x = x0; while (x < x1) { drawRect(Color(0xFFFFF4EC), Offset(x, 70f), Size(2f, 32f)); x += 5f }
                var y = 76f; while (y < 100f) { line(Color(0x1F781428), x0, y, x1, y, 0.25f, false); y += 1.1f }
                drawRect(Color(0x243C0014), Offset(x0, 84f), Size(x1 - x0, 20f))
            }
            val band = Path().apply { moveTo(26f, 79.4f); quadraticTo(50f, 84.4f, 74f, 79.4f); quadraticTo(75.6f, 82.4f, 74.2f, 85.4f); quadraticTo(50f, 90f, 25.8f, 85.4f); quadraticTo(24.4f, 82.4f, 26f, 79.4f); close() }
            stripes(band, 22f, 80f); drawPath(band, Color(0x668C1E32), style = stroke(0.35f))
            for ((dx0, ang) in listOf(0f to 0f, 2.6f to 0.16f)) {
                val x = 60f + dx0
                val end = Path().apply { moveTo(x - 2.4f, 85.6f); lineTo(x + 2.6f, 85f); lineTo(x + 4.6f + sw + ang * 10f, 95.8f); lineTo(x - 0.6f + sw + ang * 10f, 96.4f); close() }
                stripes(end, x - 4f, x + 10f); drawPath(end, Color(0x668C1E32), style = stroke(0.35f))
                for (i in 0 until 5) line(Color(0xFFC42F4B), x - 0.2f + sw + ang * 10f + i * 1.05f, 96.2f, x - 0.4f + sw + ang * 10f + i * 1.05f, 98.4f, 0.45f)
            }
            val knot = ellipsePath(61.2f, 84.6f, 3.4f, 2.8f); stripes(knot, 57f, 65f); drawPath(knot, Color(0x738C1E32), style = stroke(0.35f))
        }
        "medalha" -> {
            // medalha de campeão: fita listrada em V, aro, estrela em relevo e brilho
            val sw = sin(t * 2f) * 0.6f; val mx = 50f + sw; val my = 90f
            for (s in listOf(-1f, 1f)) {
                val p = Path().apply { moveTo(50f + s * 7.6f, 77.2f); lineTo(50f + s * 4.4f, 77.2f); lineTo(mx + s * 0.6f, 86.6f); lineTo(mx + s * 3.2f, 86f); close() }
                drawPath(p, if (s < 0) Color(0xFF3D8BEA) else Color(0xFFE0475E))
                clipPath(p) { line(Color.White.copy(alpha = 0.55f), 50f + s * 6f, 76f, mx + s * 1.9f, 87f, 0.7f) }
            }
            drawCircle(Color(0xFF9C6A12), 5.2f, Offset(mx, my + 0.5f))
            drawCircle(vg(listOf(Color(0xFFFFF1BF), Color(0xFFF5C451), Color(0xFFC98A12)), my - 5f, my + 5f), 5f, Offset(mx, my))
            drawCircle(Color(0x99965F0A), 3.7f, Offset(mx, my), style = Stroke(0.4f))
            drawPath(star5(mx, my + 0.2f, 2.6f), vg(listOf(Color(0xFFC98A12), Color(0xFFF5C451)), my - 2.6f, my + 2.6f))
            clipPath(Path().apply { addOval(Rect(mx - 5f, my - 5f, mx + 5f, my + 5f)) }) { val k = (t * 0.4f) % 1f; line(Color.White.copy(alpha = 0.55f), mx - 7f + k * 14f, my - 6f, mx - 9f + k * 14f, my + 6f, 1.4f) }
        }
        "broche" -> {
            // broche de estrela em metal chanfrado: estrela de fora escura, de dentro clara
            val r = if (small) 4.2f else 3.6f; val x = 23.5f; val y = 79.6f
            drawPath(star5(x, y + 0.3f, r), vg(listOf(Color(0xFFC98A12), Color(0xFF8A5A0C)), y - r, y + r))
            drawPath(star5(x, y, r * 0.86f), vg(listOf(Color(0xFFFFF6D6), Color(0xFFF5C451), Color(0xFFD99A1B)), y - r, y + r))
            drawPath(star5(x - 0.4f, y - 0.4f, r * 0.35f), Color.White.copy(alpha = 0.45f))
            if (!small) { val k = maxOf(0f, sin(t * 1.7f)).pow(6); if (k > 0.02f) star4(x + 2f, y - 2f, 2.4f * k, Color.White.copy(alpha = 0.95f * k)) }
        }
        "flor" -> {
            // flor na lapela: pétalas em degradê com nervura, miolo de pontinhos e uma folha
            val x = 76.5f; val y = 79.6f
            drawPath(Path().apply { moveTo(x - 1f, y + 2.4f); quadraticTo(x - 6.4f, y + 3.4f, x - 6.4f, y + 7.2f); quadraticTo(x - 2.4f, y + 6.4f, x - 1f, y + 2.4f); close() }, vg(listOf(Color(0xFF5FD3A8), Color(0xFF2E9E72)), y + 2f, y + 7f))
            line(Color.White.copy(alpha = 0.4f), x - 1.4f, y + 3f, x - 5.4f, y + 6.2f, 0.25f)
            translate(x, y) {
                rotate(sin(t * 1.5f) * 0.15f * 180f / PIf, pivot = Offset.Zero) {
                    for (i in 0 until 6) rotate(i * 60f, pivot = Offset.Zero) {
                        drawPath(Path().apply { moveTo(0f, 0f); cubicTo(1.6f, -1.6f, 4f, -1.2f, 4.2f, 0f); cubicTo(4f, 1.2f, 1.6f, 1.6f, 0f, 0f); close() }, hg(listOf(Color(0xFFFF6F8E), Color(0xFFFFB3C7), Color(0xFFFFE3EA)), 0f, 4.2f))
                        line(Color(0x59C83C5A), 0.6f, 0f, 3.4f, 0f, 0.18f)
                    }
                    drawCircle(Gold, 1.4f, Offset.Zero)
                    for (i in 0 until 6) { val a = i * PIf / 3f + 0.5f; drawCircle(Color(0xFFC98A12), 0.22f, Offset(cos(a) * 0.8f, sin(a) * 0.8f)) }
                    drawCircle(Color.White.copy(alpha = 0.6f), 0.35f, Offset(-0.4f, -0.5f))
                }
            }
        }
        "roseta" -> {
            // roseta de campeão: duas camadas de pregas, caudas em V e a estrela no centro
            val x = 24.5f; val y = 78.4f
            for ((s, c) in listOf(-1f to Color(0xFF3A2FC4), 1f to Color(0xFF2A209E))) {
                drawPath(Path().apply { moveTo(x + s * 0.4f, y + 2f); lineTo(x + s * 3.4f, y + 10.6f); lineTo(x + s * 2f, y + 9.4f); lineTo(x + s * 1f, y + 11f); lineTo(x - s * 0.8f, y + 2.6f); close() }, c)
                line(Color.White.copy(alpha = 0.35f), x + s * 0.6f, y + 3f, x + s * 2.6f, y + 9.6f, 0.3f)
            }
            for ((rr, n, cols) in listOf(Triple(4.4f, 16, Color(0xFF3A2FC4) to Color(0xFF2A209E)), Triple(3.2f, 12, Color(0xFFF5C451) to Color(0xFFE9AE34)))) {
                for (i in 0 until n) {
                    val a = i * 2f * PIf / n; val px = x + cos(a) * rr * 0.72f; val py = y + sin(a) * rr * 0.72f
                    rotate(a * 180f / PIf, pivot = Offset(px, py)) { oval(if (i % 2 == 1) cols.first else cols.second, px - rr * 0.42f, py - rr * 0.28f, rr * 0.84f, rr * 0.56f) }
                }
            }
            drawCircle(vg(listOf(Color(0xFFFFFDF4), Color(0xFFFFE3A0)), y - 2.2f, y + 2.2f), 2.2f, Offset(x, y))
            drawPath(star5(x, y + 0.1f, 1.5f), Color(0xFF2A209E))
        }
    }
}

// ---- item na mão esquerda (desenhado antes da mão, que fica por cima segurando)
// Reage ao humor: a varinha solta faíscas e o troféu brilha quando ele comemora; o balão escapa quando
// ele fica triste e volta descendo depois.
private fun DrawScope.drawHeld(kind: String, h: Offset, angRad: Float, t: Float, small: Boolean, mood: FolhaMood = FolhaMood.IDLE, tm: Float = 9f, prevMood: FolhaMood? = null) {
    val tiltA = clamp(angRad, -0.6f, 0.6f) * 0.5f; val sc = if (small) 1.45f else 1.8f
    val party = mood in PARTY_MOODS
    translate(h.x, h.y) {
        rotate(tiltA * 180f / PIf, pivot = Offset.Zero) {
            scale(sc, sc, pivot = Offset.Zero) {
                when (kind) {
                    "lapis" -> drawPencilAt(2.5f, 8f, -3f, -9f, if (small) 1.4f else 1.15f)
                    "marcatexto" -> {
                        rotate(-0.35f * 180f / PIf, pivot = Offset.Zero) {
                            drawPath(roundRect(-2.2f, -9.6f, 4.4f, 14.6f, 1.6f), hg(listOf(Color(0xFFFFF59A), Color(0xFFF7E436), Color(0xFFD9C21F)), -2.2f, 2.2f))
                            drawPath(roundRect(-1.2f, -6f, 2.4f, 7.4f, 1f), Color.White.copy(alpha = 0.45f))
                            drawPath(roundRect(-2.5f, -13.2f, 5f, 4.6f, 1.6f), vg(listOf(Color(0xFF5FD3A8), Color(0xFF2E9E72)), -13.2f, -8.6f))
                            drawPath(roundRect(1.6f, -12.6f, 1.2f, 5.6f, 0.5f), Color(0xFF1F7A5A))
                            drawPath(Path().apply { moveTo(-1.5f, 5f); lineTo(1.5f, 5f); lineTo(0.9f, 7.6f); lineTo(-1.5f, 6.8f); close() }, Color(0xFFE3CF1A))
                            line(Color.White.copy(alpha = 0.6f), -1.5f, -8.8f, -1.5f, 3.8f, 0.4f)
                        }
                        if (party) { val k = (t * 0.8f) % 1f; line(Color(0xFFFFE94D).copy(alpha = 0.5f * (1 - k)), 4f, 6f, 4f + k * 8f, 6f - k * 2f, 2.2f) }
                    }
                    "livro" -> rotate(-0.14f * 180f / PIf, pivot = Offset.Zero) {
                        drawPath(Path().apply { moveTo(3.6f, -10.6f); lineTo(5f, -9.6f); lineTo(5f, 0.2f); lineTo(3.6f, -0.6f); close() }, Color(0xFFF4EFE4))
                        for (i in 1 until 4) line(Color(0x5996826A), 3.8f + i * 0.3f, -10f + i * 0.25f, 3.8f + i * 0.3f, -0.4f + i * 0.2f, 0.12f)
                        drawPath(Path().apply { moveTo(-4.6f, -11f); lineTo(3.6f, -11f); lineTo(5f, -10f); lineTo(-3.2f, -10f); close() }, Color(0xFFF4EFE4))
                        drawPath(roundRect(-4.8f, -11f, 8.6f, 10.6f, 0.9f), vg(listOf(Color(0xFFEE6B80), Color(0xFFC42F4B), Color(0xFF961D37)), -11f, -0.4f))
                        drawRect(Color(0x2E000000), Offset(-4.8f, -11f), Size(1.4f, 10.6f))
                        drawPath(roundRect(-2.6f, -8.6f, 5f, 3f, 0.5f), Color(0xF2FFEBB4))
                        line(Color(0xFFC98A12), -2f, -7.6f, 1.8f, -7.6f, 0.3f); line(Color(0xFFC98A12), -2f, -6.6f, 1f, -6.6f, 0.3f)
                        line(Color(0xFFF5C451), -3.6f, -2.4f, 3f, -2.4f, 0.35f)
                        val rw = sin(t * 2f) * 0.3f
                        drawPath(Path().apply { moveTo(0.4f, -0.4f); lineTo(1.6f, -0.4f); lineTo(1.6f, 2.6f + rw); lineTo(1f, 2f + rw); lineTo(0.4f, 2.6f + rw); close() }, Mint)
                    }
                    "balao" -> {
                        // balão de látex: brilho, nó e barbante cacheado; escapa no triste e volta descendo depois
                        var lift = 0f; var alpha = 1f
                        if (mood == FolhaMood.SAD) { val u = clamp(tm / 2.2f, 0f, 1f); lift = -u * u * 70f; alpha = 1f - clamp((u - 0.7f) / 0.3f, 0f, 1f) }
                        else if (prevMood == FolhaMood.SAD && tm < 1.2f) { val u = 1f - sm(tm / 1.2f); lift = -u * 60f }
                        val bx = -5f + sin(t * 1.3f) * 2.2f; val by = -25f + sin(t * 1.7f) * 1.4f + lift
                        if (alpha > 0.01f) {
                            val str = Path().apply {
                                if (lift > -1f) { moveTo(0f, -1f); for (i in 1..10) { val u = i / 10f; lineTo(bx * u + sin(u * 12f + t * 2f) * 0.7f * (1 - u), -1f + (by + 8f) * u) } }
                                else { moveTo(bx, by + 7f); quadraticTo(bx + 1.5f, by + 11f, bx - 0.5f, by + 15f) }
                            }
                            drawPath(str, Color(0x993C326E).copy(alpha = 0.6f * alpha), style = stroke(0.35f))
                            rotate(sin(t * 1.3f) * 0.12f * 180f / PIf, pivot = Offset(bx, by)) {
                                drawOval(Brush.radialGradient(listOf(Color(0xFFFFB1C0), Color(0xFFF0476A), Color(0xFFB81F3E)), center = Offset(bx - 2f, by - 2.6f), radius = 7.4f), Offset(bx - 5.4f, by - 6.4f), Size(10.8f, 12.8f), alpha = alpha)
                            }
                            drawPath(Path().apply { moveTo(bx - 1f, by + 7.2f); lineTo(bx + 1f, by + 7.2f); lineTo(bx, by + 6f); close() }, Color(0xFFB81F3E).copy(alpha = alpha))
                            rotate(-0.5f * 180f / PIf, pivot = Offset(bx - 2.2f, by - 2.8f)) { oval(Color.White.copy(alpha = 0.6f * alpha), bx - 3.4f, by - 4.9f, 2.4f, 4.2f) }
                            drawCircle(Color.White.copy(alpha = 0.5f * alpha), 0.5f, Offset(bx + 2.4f, by + 2.6f))
                        }
                    }
                    "varinha" -> {
                        // varinha: haste preta com anéis dourados, estrela com halo e rastro de faíscas
                        line(Color(0xFF1E1B2E), 2.2f, 6.4f, -4.4f, -10.8f, 1.4f); line(Color.White, -3.6f, -8.8f, -4.4f, -10.8f, 1.4f)
                        for (u in listOf(0.05f, 0.16f)) line(Gold, 2.2f - 6.6f * u - 0.8f, 6.4f - 17.2f * u + 0.3f, 2.2f - 6.6f * u + 0.8f, 6.4f - 17.2f * u - 0.3f, 0.6f)
                        val sx = -4.8f; val sy = -12.8f; val r = 3.2f + 0.4f * sin(t * 6f)
                        drawCircle(Brush.radialGradient(listOf(Color(0xB3FFE68C), Color(0x00FFE68C)), center = Offset(sx, sy), radius = r * 2.4f), r * 2.4f, Offset(sx, sy))
                        rotate(t * 1.5f * 180f / PIf, pivot = Offset(sx, sy)) { drawPath(star5(sx, sy, r), vg(GOLD3, sy - r, sy + r)); drawPath(star5(sx - 0.3f, sy - 0.3f, r * 0.4f), Color.White.copy(alpha = 0.55f)) }
                        val n = if (party) 10 else 4
                        for (i in 0 until n) {
                            val ph = (t * (if (party) 1.6f else 0.9f) + i / n.toFloat()) % 1f; val a = i * 1.7f + t; val reach = if (party) 14f else 7f
                            star4(sx + cos(a) * (3f + ph * reach), sy + sin(a) * (3f + ph * reach) - ph * 3f, (if (party) 1.8f else 1.3f) * (1 - ph), (if (i % 3 == 0) Mint else Color(0xFFFFF0B4)).copy(alpha = 1 - ph))
                        }
                    }
                    "trofeu" -> {
                        // troféu: taça com alças curvas, nó no pé, base de madeira com plaquinha e estrela; brilha na comemoração
                        for (s in listOf(-1f, 1f)) { val hd = Path().apply { moveTo(s * 4.4f, -12.6f); cubicTo(s * 8f, -12.6f, s * 7.4f, -7.6f, s * 3.2f, -7.4f) }; drawPath(hd, Color(0xFFB57A0C), style = stroke(1.2f)); drawPath(hd, Color(0xFFF5C451), style = stroke(0.5f)) }
                        val cup = Path().apply { moveTo(-4.8f, -14f); lineTo(4.8f, -14f); cubicTo(4.8f, -8f, 2.6f, -6f, 0.9f, -5.8f); lineTo(0.9f, -4.2f); lineTo(-0.9f, -4.2f); lineTo(-0.9f, -5.8f); cubicTo(-2.6f, -6f, -4.8f, -8f, -4.8f, -14f); close() }
                        drawPath(cup, hg(listOf(Color(0xFFC98A12), Color(0xFFFFF1BF), Color(0xFFF5C451), Color(0xFFB57A0C)), -4.8f, 4.8f))
                        oval(Color(0xFF9C6A12), -4.8f, -15f, 9.6f, 2f)
                        drawCircle(Color(0xFFE9AE34), 1f, Offset(0f, -4.4f))
                        drawPath(roundRect(-3.4f, -3.6f, 6.8f, 2.8f, 0.6f), vg(listOf(Color(0xFF8A5A2B), Color(0xFF5A3D20)), -3.6f, -0.8f))
                        drawPath(roundRect(-1.8f, -3f, 3.6f, 1.4f, 0.3f), Color(0xFFF5C451))
                        drawPath(star5(0f, -10.4f, 2f), Color(0xCCA0640A))
                        if (party) { val k = abs(sin(t * 3f)); star4(-3f, -12.4f, 2.6f * k, Color.White.copy(alpha = k)); star4(5.5f, -15.5f, 1.6f * (1 - k), Color(0xFFFFF0B4).copy(alpha = 1 - k)) }
                    }
                    "pasta" -> {
                        // pasta executiva de couro: alça na mão, fecho dourado, costura e cantos reforçados
                        drawPath(Path().apply { moveTo(-2.6f, 0.6f); quadraticTo(-2.6f, -2f, 0f, -2f); quadraticTo(2.6f, -2f, 2.6f, 0.6f) }, Color(0xFF3A2410), style = stroke(1.3f))
                        drawPath(roundRect(-7f, 0.6f, 14f, 10f, 1.4f), vg(listOf(Color(0xFF8A5A2B), Color(0xFF6B4320), Color(0xFF4E2F15)), 0.6f, 10.6f))
                        drawPath(roundRect(-7f, 0.6f, 14f, 10f, 1.4f), Color(0xFF2E1A08), style = Stroke(0.4f))
                        drawPath(roundRect(-7f, 0.6f, 14f, 3.6f, 1.2f), vg(listOf(Color(0xFF9C6A35), Color(0xFF7A4E26)), 0.6f, 4.2f))
                        drawPath(roundRect(-6.2f, 1.4f, 12.4f, 8.4f, 1f), Color(0x8CFFE2B4), style = Stroke(0.2f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.6f, 0.5f))))
                        for (x in listOf(-4f, 4f)) drawPath(roundRect(x - 1f, 3.2f, 2f, 1.8f, 0.4f), vg(GOLD3, 3.2f, 5f))
                        for ((x, y) in listOf(-6.4f to 1.2f, 6.4f to 1.2f, -6.4f to 10f, 6.4f to 10f)) drawCircle(Color(0xFFE9AE34), 0.5f, Offset(x, y))
                        line(Color.White.copy(alpha = 0.25f), -5.6f, 5.6f, 2f, 5.6f, 0.4f)
                    }
                    "lupa" -> {
                        // lupa de detetive: aro de latão chanfrado, vidro azulado com reflexos e cabo de madeira
                        line(Color(0xFF5A3D20), 1.4f, 5.4f, -2.2f, -4.2f, 2.2f); line(Color(0xFF8A5A2B), 1.2f, 5f, -2f, -4f, 1.2f)
                        line(Color(0xFFC98A12), -1.6f, -3.4f, -2.6f, -5.4f, 2.4f)
                        val lx = -4.8f; val ly = -10.2f
                        drawCircle(Brush.radialGradient(listOf(Color(0x8CE6F5FF), Color(0x5996C8F0)), center = Offset(lx - 1.4f, ly - 1.6f), radius = 4.6f), 4.6f, Offset(lx, ly))
                        drawCircle(vg(listOf(Color(0xFFFFF1BF), Color(0xFFC98A12), Color(0xFF7A4F05)), ly - 4.6f, ly + 4.6f), 4.6f, Offset(lx, ly), style = Stroke(1.4f))
                        drawArc(Color.White.copy(alpha = 0.9f), 198f, 63f, false, Offset(lx - 3.4f, ly - 3.4f), Size(6.8f, 6.8f), style = stroke(0.6f))
                        drawArc(Color.White.copy(alpha = 0.9f), 288f, 18f, false, Offset(lx - 3.4f, ly - 3.4f), Size(6.8f, 6.8f), style = stroke(0.6f))
                    }
                }
            }
        }
    }
}

// ---- aura: em volta do Folha; [behind] desenha a parte de trás
private fun DrawScope.drawAura(t: Float, kind: String, small: Boolean, burst: Float, behind: Boolean) {
    val boost = if (burst > 0f) 1f + burst * 1.5f else 1f
    when (kind) {
        "brilhos" -> if (!behind) {
            // brilhos de quatro pontas com halo, em dourado e branco, acendendo e apagando
            val spots = listOf(10f to 40f, 90f to 36f, 6f to 70f, 95f to 66f, 22f to 20f, 80f to 16f)
            for (i in 0 until if (small) 3 else 6) {
                val ph = (t * 0.8f + i * 0.19f) % 1f; val a = sin(PIf * ph); val (x, y) = spots[i]; val r = (if (small) 4.6f else 4.2f) * a * boost
                if (!small && r > 0.01f) drawCircle(Brush.radialGradient(listOf((if (i % 2 == 1) Color.White else Gold).copy(alpha = 0.45f * a), Gold.copy(alpha = 0f)), center = Offset(x, y), radius = r * 1.6f), r * 1.6f, Offset(x, y))
                star4(x, y, r, if (i % 2 == 1) Color.White.copy(alpha = a) else Gold.copy(alpha = a))
            }
        }
        "coracoes" -> if (!behind) for (i in 0 until if (small) 2 else 4) {
            val ph = (t * 0.35f + i / 4f) % 1f; val side = if (i % 2 == 1) 1f else -1f
            val x = 50f + side * (40f + sin(ph * 6f + i) * 3f); val y = 80f - ph * 60f; val r = 3.4f * boost * (0.7f + ph * 0.5f); val a = sin(PIf * ph) * 0.95f
            rotate(sin(t * 2f + i) * 0.25f * 180f / PIf, pivot = Offset(x, y)) {
                drawPath(heart(x, y, r), vg(listOf(Color(0xFFFF8FA3).copy(alpha = a), Color(0xFFE0475E).copy(alpha = a)), y - r, y + r))
                rotate(-0.6f * 180f / PIf, pivot = Offset(x - r * 0.4f, y - r * 0.35f)) { oval(Color.White.copy(alpha = 0.7f * a), x - r * 0.65f, y - r * 0.51f, r * 0.5f, r * 0.32f) }
            }
        }
        "notas" -> if (!behind) for (i in 0 until if (small) 2 else 4) {
            // notas musicais (colcheia e semicolcheia ligadas) subindo e balançando
            val ph = (t * 0.32f + i / 4f) % 1f; val side = if (i % 2 == 1) 1f else -1f
            val x = 50f + side * (38f + sin(ph * 5f + i) * 4f); val y = 76f - ph * 56f; val a = sin(PIf * ph)
            val c = (if (i % 2 == 1) Color(0xFF3A2FC4) else Color(0xFF9563FF)).copy(alpha = a)
            translate(x, y) {
                rotate(sin(t * 2f + i) * 0.25f * 180f / PIf, pivot = Offset.Zero) {
                    scale(1.6f * boost, 1.6f * boost, pivot = Offset.Zero) {
                        rotate(-0.45f * 180f / PIf, pivot = Offset.Zero) { oval(c, -1.7f, -1.25f, 3.4f, 2.5f) }
                        line(c, 1.45f, -0.3f, 1.45f, -6.2f, 0.55f)
                        if (i % 2 == 1) {
                            rotate(-0.45f * 180f / PIf, pivot = Offset(4.6f, -1f)) { oval(c, 2.9f, -2.25f, 3.4f, 2.5f) }
                            line(c, 6.05f, -1.3f, 6.05f, -7.2f, 0.55f)
                            drawPath(Path().apply { moveTo(1.45f, -6.2f); lineTo(6.05f, -7.2f); lineTo(6.05f, -6.1f); lineTo(1.45f, -5.1f); close() }, c)
                        } else drawPath(Path().apply { moveTo(1.45f, -6.2f); cubicTo(3.6f, -5.4f, 4.2f, -3.6f, 3f, -2.2f) }, c, style = stroke(0.6f))
                        rotate(-0.45f * 180f / PIf, pivot = Offset(-0.6f, -0.4f)) { oval(Color.White.copy(alpha = 0.6f * a), -1.1f, -0.7f, 1f, 0.6f) }
                    }
                }
            }
        }
        "orbita" -> for (i in 0 until 3) {
            // três estrelas em órbita: passam por trás e pela frente do Folha
            val a = t * 1.6f + i * 2f * PIf / 3f; val front = sin(a) > 0f; if (front == behind) continue
            val x = 50f + cos(a) * 50f; val y = 80f + sin(a) * 17f; val r = (if (front) 3.8f else 2.6f) * boost
            if (front) drawPath(star5(x, y, r), vg(GOLD3, y - r, y + r)) else drawPath(star5(x, y, r), Gold.copy(alpha = 0.55f))
        }
        "chamas" -> if (behind) for (i in 0 until if (small) 2 else 4) {
            val side = if (i % 2 == 1) 1f else -1f; val row = i / 2; val x = 50f + side * (40f + row * 7f); val base = 94f - row * 12f
            val hgt = (13f + 3f * sin(t * 9f + i * 1.7f)) * boost; val wd = 4.2f
            val p = Path().apply {
                moveTo(x - wd, base); cubicTo(x - wd * 1.1f, base - hgt * 0.5f, x - wd * 0.2f + sin(t * 7f + i) * 0.8f, base - hgt * 0.7f, x + sin(t * 6f + i) * 1.2f, base - hgt)
                cubicTo(x + wd * 0.6f, base - hgt * 0.6f, x + wd * 1.1f, base - hgt * 0.45f, x + wd, base); quadraticTo(x, base + 1.8f, x - wd, base)
            }
            drawPath(p, vg(listOf(Color(0xFFFFE07A), Color(0xFFFF9F3A), Color(0xFFF2552C)), base - hgt, base))
        }
        "confete" -> if (!behind) {
            // confete de verdade: papeizinhos que giram em 3D (achatam e viram), bolinhas e serpentinas
            val colors = listOf(Spark, Mint, Cheek, Color(0xFF6FB0FF), Color(0xFFB995F5))
            for (i in 0 until if (small) 6 else 14) {
                val ph = (t * 0.26f + i * 0.071f) % 1f; val x = 6f + ((i * 37) % 88) + sin(t * 1.6f + i) * 3.5f; val y = -4f + ph * 104f
                if (y > 40f && y < 92f && x > 12f && x < 88f) continue   // não cai na frente do rosto
                val c = colors[i % 5].copy(alpha = 0.95f); val flip = cos(t * 5f + i * 1.3f)
                rotate((t * 2f + i) * 180f / PIf, pivot = Offset(x, y)) {
                    when (i % 3) {
                        0 -> drawCircle(c, 1f * boost, Offset(x, y))
                        1 -> scale(flip, 1f, pivot = Offset(x, y)) { drawRect(c, Offset(x - 2f * boost, y - 1f), Size(4f * boost, 2f)); drawRect(Color.White.copy(alpha = 0.35f), Offset(x - 2f * boost, y - 1f), Size(4f * boost, 0.6f)) }
                        else -> drawPath(Path().apply { moveTo(x - 3f, y); cubicTo(x - 1.5f, y - 1.6f, x - 0.5f, y + 1.6f, x + 1f, y); cubicTo(x + 2f, y - 1.4f, x + 2.6f, y + 0.8f, x + 3.4f, y) }, c, style = stroke(0.7f * boost))
                    }
                }
            }
        }
        "runas" -> {
            // runas mágicas: um anel de símbolos que brilham, gira devagar e passa por trás e pela frente (por baixo do rosto)
            val runes = listOf(floatArrayOf(0f, -2f, 0f, 2f, -1.4f, -0.6f, 1.4f, 0.6f), floatArrayOf(-1.4f, -2f, 1.4f, -2f, 0f, -2f, 0f, 2f), floatArrayOf(-1.2f, 2f, 0f, -2f, 0f, -2f, 1.2f, 2f),
                floatArrayOf(-1.4f, -1f, 1.4f, -1f, -1.4f, 1f, 1.4f, 1f), floatArrayOf(0f, -2f, 0f, 2f, 0f, 0f, 1.4f, -1.4f), floatArrayOf(-1.4f, 2f, -1.4f, -2f, -1.4f, 0f, 1.4f, 0f))
            for (i in 0 until 10) {
                val a = t * 0.6f + i * 2f * PIf / 10f; val front = sin(a) > 0f; if (front == behind) continue
                val x = 50f + cos(a) * 52f; val y = 82f + sin(a) * 15f + sin(t * 1.3f) * 0.8f; val k = (if (front) 1f else 0.7f) * boost; val al = (if (front) 0.95f else 0.45f) * (0.7f + 0.3f * sin(t * 3f + i))
                drawCircle(Brush.radialGradient(listOf(Mint.copy(alpha = 0.55f * al), Color(0x009563FF)), center = Offset(x, y), radius = 4.6f * k), 4.6f * k, Offset(x, y))
                val r = runes[i % runes.size]; val c = Color(0xFF28966E).copy(alpha = al)
                line(c, x + r[0] * k, y + r[1] * k, x + r[2] * k, y + r[3] * k, 0.6f * k); line(c, x + r[4] * k, y + r[5] * k, x + r[6] * k, y + r[7] * k, 0.6f * k)
            }
            // faíscas de luz subindo do círculo do chão
            if (!behind) for (i in 0 until if (small) 4 else 9) {
                val ph = (t * 0.45f + i * 0.113f) % 1f; val side = if (i % 2 == 1) 1f else -1f
                val x = 50f + side * (20f + (i * 7) % 24) + sin(t * 2f + i) * 1.2f; val y = 94f - ph * 40f; val a = sin(PIf * ph)
                drawCircle((if (i % 3 != 0) Mint else Color(0xFFB995F5)).copy(alpha = 0.85f * a), (if (small) 1.3f else 0.9f) * (1f - ph * 0.4f), Offset(x, y))
            }
        }
        "fogos" -> if (!behind) {
            // fogos de artifício de verdade: o foguete sobe com rastro de faíscas, estoura em dois anéis de cores
            // (com clarão no centro) e a purpurina cai piscando. Seis pontos em volta, longe do rosto.
            val cols = listOf(Color(0xFFFFD84A) to Color.White, Color(0xFFFF8FA3) to Color(0xFFFFD84A), Mint to Color.White, Color(0xFF6FB0FF) to Color(0xFFB995F5), Color(0xFFB995F5) to Color(0xFFFF8FA3))
            val spots = listOf(10f to 20f, 90f to 14f, 22f to -2f, 80f to -6f, -4f to 46f, 104f to 40f)
            for (i in 0 until if (small) 3 else 6) {
                val cyc = t * 0.36f + i * 0.37f; val ph = cyc % 1f; val (c1, c2) = cols[(i + cyc.toInt()) % 5]; val (cx, cy) = spots[i]
                if (ph < 0.2f) {
                    val u = ph / 0.2f; val y = cy + 34f * (1f - u * (2f - u))
                    for (j in 0 until 4) drawCircle(c1.copy(alpha = 0.8f - j * 0.2f), 0.6f - j * 0.08f, Offset(cx + sin(t * 40f + j * 2f) * 0.6f, y + 1.6f + j * 2f))
                    drawCircle(Color.White, 0.95f, Offset(cx, y)); continue
                }
                val u = (ph - 0.2f) / 0.8f; val e = 1f - (1f - u).pow(3); val rr = (4f + e * 13f) * boost * (if (small) 0.8f else 1f); val al = clamp(1.3f - u * 1.3f, 0f, 1f); val fall = u * u * 6f
                if (u < 0.14f) { val fl = 1f - u / 0.14f; drawCircle(Brush.radialGradient(listOf(Color.White.copy(alpha = fl), c1.copy(alpha = 0f)), center = Offset(cx, cy), radius = 7f), 7f, Offset(cx, cy)) }
                for (j in 0 until 14) {
                    val a = j * 2f * PIf / 14f + i; val px = cx + cos(a) * rr; val py = cy + sin(a) * rr + fall
                    line(c1.copy(alpha = al * 0.5f), cx + cos(a) * rr * 0.62f, cy + sin(a) * rr * 0.62f + fall * 0.6f, px, py, 0.85f)
                    drawCircle((if (j % 2 == 1) c2 else c1).copy(alpha = al), 0.95f, Offset(px, py))
                    if (!small && u > 0.35f && (j + (t * 10f).toInt()) % 3 == 0) star4(px + cos(a) * 1.5f, py + 2.5f + u * 4f, 1.3f, Color.White.copy(alpha = al))
                }
                for (j in 0 until 8) { val a = j * PIf / 4f + i + 0.4f; val r2 = rr * 0.42f; drawCircle(c2.copy(alpha = al), 0.7f, Offset(cx + cos(a) * r2, cy + sin(a) * r2 + fall * 0.5f)) }
            }
        }
        "galaxia" -> for (i in 0 until 18) {
            // anel de estrelinhas e poeira cósmica em volta (metade passa por trás)
            val a = t * 0.7f + i * 2f * PIf / 18f; val front = sin(a) > 0f; if (front == behind) continue
            val x = 50f + cos(a) * 54f; val y = 80f + sin(a) * 17f
            val c = when (i % 3) { 0 -> Color.White; 1 -> Color(0xFFB995F5); else -> Color(0xFF6FB0FF) }
            drawCircle(c.copy(alpha = if (front) 0.95f else 0.5f), (if (front) 1.3f else 0.8f) * boost, Offset(x, y))
            if (i % 6 == 0) star4(x, y, (if (front) 3.4f else 2.2f) * boost, c.copy(alpha = if (front) 0.9f else 0.5f))
        }
    }
}

/** Toque: um estouro rápido de brilhos. */
private fun DrawScope.drawBurst(burst: Float) {
    if (burst <= 0f) return
    val u = 1f - burst
    for (i in 0 until 8) { val a = i * PIf / 4f; val r = 30f + u * 26f; star4(50f + cos(a) * r, 62f + sin(a) * r * 0.7f, 3f * burst, Spark.copy(alpha = burst)) }
}

/** Lápis da ponta (x0, y0) até a borracha (x1, y1). */
private fun DrawScope.drawPencilAt(x0: Float, y0: Float, x1: Float, y1: Float, r: Float) {
    // lápis sextavado: três faces de amarelo, ponteira de metal com frisos, borracha e ponta de grafite
    val len = hypot(x1 - x0, y1 - y0)
    translate(x0, y0) {
        rotate(atan2(y1 - y0, x1 - x0) * 180f / PIf, pivot = Offset.Zero) {
            drawPath(Path().apply { moveTo(0.2f, 0f); lineTo(3.4f, -r); lineTo(3.4f, r); close() }, hg(listOf(Color(0xFFE9C08A), Color(0xFFF7DDB4)), 0f, 3.4f))
            drawPath(Path().apply { moveTo(0f, 0f); lineTo(1.3f, -r * 0.42f); lineTo(1.3f, r * 0.42f); close() }, Color(0xFF3A3550))
            val bodyL = len - 7f
            drawRect(Color(0xFFFFD45A), Offset(3.4f, -r), Size(bodyL, r * 0.68f))
            drawRect(Gold, Offset(3.4f, -r * 0.32f), Size(bodyL, r * 0.64f))
            drawRect(Color(0xFFD9921A), Offset(3.4f, r * 0.32f), Size(bodyL, r * 0.68f))
            line(Color.White.copy(alpha = 0.55f), 3.6f, -r * 0.7f, 3.4f + bodyL, -r * 0.7f, 0.25f, false)
            val fx = 3.4f + bodyL
            drawRect(vg(listOf(Color(0xFFF2F3F8), Color(0xFFB7B9C9), Color(0xFF8E92A6)), -r, r), Offset(fx, -r * 1.05f), Size(2f, r * 2.1f))
            for (k in listOf(0.5f, 1f, 1.5f)) line(Color(0x8C5A5A6E), fx + k, -r * 1.05f, fx + k, r * 1.05f, 0.18f, false)
            drawPath(Path().apply { moveTo(fx + 2f, -r); lineTo(fx + 3.6f, -r); quadraticTo(fx + 4.6f, -r, fx + 4.6f, 0f); quadraticTo(fx + 4.6f, r, fx + 3.6f, r); lineTo(fx + 2f, r); close() }, vg(listOf(Color(0xFFFFB3C7), Color(0xFFF07A99)), -r, r))
        }
    }
}

private fun DrawScope.drawLaurels(small: Boolean) {
    // coroa de louros: dois ramos de folhas com nervura, que se cruzam na frente com um laço vermelho
    for (side in listOf(-1f, 1f)) {
        val p0 = Offset(50f + side * 2f, 45.6f); val p1 = Offset(50f + side * 17.5f, 46.5f); val p2 = Offset(50f + side * 17f, 30f)
        fun at(u: Float) = Offset((1 - u) * (1 - u) * p0.x + 2 * (1 - u) * u * p1.x + u * u * p2.x, (1 - u) * (1 - u) * p0.y + 2 * (1 - u) * u * p1.y + u * u * p2.y)
        drawPath(Path().apply { moveTo(p0.x, p0.y); quadraticTo(p1.x, p1.y, p2.x, p2.y) }, Color(0xFF9C6A12), style = stroke(if (small) 1f else 0.7f))
        val n = if (small) 5 else 8
        for (i in 1..n) {
            val u = i / n.toFloat(); val a = at(u); val b = at(minOf(1f, u + 0.02f)); val tang = atan2(b.y - a.y, b.x - a.x); val sz = 1f - u * 0.3f
            for (k in if (i == n) listOf(0f) else listOf(-1f, 1f)) {
                translate(a.x, a.y) {
                    rotate((tang + k * 0.7f) * 180f / PIf, pivot = Offset.Zero) {
                        scale(sz, sz, pivot = Offset.Zero) {
                            val leaf = Path().apply { moveTo(0f, 0f); quadraticTo(1.8f, -1.3f, 4f, 0f); quadraticTo(1.8f, 1.3f, 0f, 0f); close() }
                            drawPath(leaf, vg(listOf(Color(0xFFFFE9A6), Gold, Color(0xFFC98A12)), -1.3f, 1.3f))
                            if (!small) { line(Color(0x99965F0A), 0.4f, 0f, 3.4f, 0f, 0.18f); drawPath(leaf, Color(0x59784600), style = stroke(0.18f)) }
                        }
                    }
                }
            }
        }
    }
    for (s in listOf(-1f, 1f)) drawPath(Path().apply { moveTo(50f, 45.6f); quadraticTo(50f + s * 2.4f, 43.4f, 50f + s * 3.6f, 44.4f); quadraticTo(50f + s * 3.2f, 46.6f, 50f, 45.6f); close() }, Color(0xFFD23A55))
    drawCircle(Color(0xFFA8233A), 0.9f, Offset(50f, 45.6f))
}

private fun star5(x: Float, y: Float, r: Float, sy: Float = 1f) = Path().apply {
    for (i in 0 until 10) { val a = -PIf / 2 + i * PIf / 5; val rr = if (i % 2 == 1) r * 0.45f else r; val px = x + cos(a) * rr; val py = y + sin(a) * rr * sy; if (i == 0) moveTo(px, py) else lineTo(px, py) }
    close()
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
    if ('L' in which) drawArm(Offset(12f, 66f), Offset(p.hL0, p.hL1), p.bendL, -1f, p.shapeL, p.rotL, f.popL, f.small, f.look, f.t, f)
    if ('R' in which) drawArm(Offset(88f, 66f), Offset(p.hR0, p.hR1), p.bendR, 1f, p.shapeR, p.rotR, f.popR, f.small, f.look, f.t)
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
private fun DrawScope.drawArm(sh: Offset, h: Offset, bend: Float, side: Float, shape: Hand, rot: Float, pop: Float, small: Boolean, look: Look, t: Float, frame: FolhaFrame? = null) {
    val armC = look.cover.arm[0]; val armL = look.cover.arm[1]
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
    drawPath(Path().apply { moveTo(l1[0].x, l1[0].y); l1.forEach { lineTo(it.x, it.y) }; r1.asReversed().forEach { lineTo(it.x, it.y) }; close() }, armC)
    val edge = if (side < 0) r1 else l1
    drawPath(Path().apply { moveTo(edge[1].x, edge[1].y); for (i in 2 until n - 1) lineTo(edge[i].x, edge[i].y) }, armL.copy(alpha = 0.7f), style = stroke(0.6f))
    if (shape == Hand.PEN) drawPencil(h.x - 2.4f, h.y + 3.2f, small)
    // mão: na direção do fim do braço, mais o giro do humor; "pop" quando troca de forma
    val ang = atan2(h.y - cy, h.x - cx) * 180f / PIf + rot
    // o item da mão esquerda vai junto com a mão (some quando a mão abre para pegar a folha ou no susto)
    if (side < 0 && look.hand != "nenhum" && (shape == Hand.MITT || shape == Hand.FIST)) drawHeld(look.hand, h, ang * PIf / 180f - PIf, t, small, frame?.mood ?: FolhaMood.IDLE, frame?.tm ?: 9f, frame?.prevMood)
    val sc = (if (small) 1.35f else 1.25f) * (1f + 0.28f * pop)
    translate(h.x, h.y) {
        rotate(ang, pivot = Offset.Zero) {
            scale(sc, if (side < 0) -sc else sc, pivot = Offset.Zero) { drawHand(shape, small, armC, look.gloves, look.gloveKind) }
        }
    }
}

private fun DrawScope.drawHand(shape: Hand, small: Boolean, Arm: Color = br.com.estudario.ui.assistant.ArmDefault, glove: List<Color> = listOf(Color.White, Color(0xFFECE8F6)), kind: String = "branca") {
    val ow = if (small) 1.0f else 0.6f
    if (kind == "boxe") {   // luva de boxe: a mão vira a luva, em qualquer gesto
        val dark = Color(0xFF8E1A22)
        drawPath(roundRect(-2.8f, -2.7f, 2.6f, 5.4f, 1f), Color.White); drawPath(roundRect(-2.8f, -2.7f, 2.6f, 5.4f, 1f), dark, style = Stroke(ow))
        line(Color(0xFFE5373F), -1.5f, -2.6f, -1.5f, 2.6f, 0.5f, false)
        val red = Brush.radialGradient(listOf(Color(0xFFFF7A80), Color(0xFFC3212B)), center = Offset(1.6f, -1.4f), radius = 4.4f)
        val th = Path().apply { addOval(Rect(1.2f - 1.7f, -3f - 1.3f, 1.2f + 1.7f, -3f + 1.3f)) }
        val gl = ellipsePath(2.6f, 0.1f, 4f, 3.6f)
        for (p in listOf(gl, th)) { drawPath(p, red); drawPath(p, dark, style = Stroke(ow)) }
        rotate(-0.4f * 180f / PIf, pivot = Offset(1.6f, -1.5f)) { oval(Color.White.copy(alpha = 0.55f), 0.2f, -2.3f, 2.8f, 1.6f) }
        return
    }
    val fill: Brush = if (kind == "arcoiris") Brush.linearGradient(listOf(Color(0xFFFF8FA3), Color(0xFFFFD84A), Color(0xFF7EE0B8), Color(0xFF6FB0FF), Color(0xFFB995F5)), start = Offset(-1f, -3f), end = Offset(5f, 3f))
        else androidx.compose.ui.graphics.SolidColor(glove[0])
    drawOval(glove[1], Offset(-2.8f, -2.5f), Size(3.2f, 5f)); drawOval(Arm, Offset(-2.8f, -2.5f), Size(3.2f, 5f), style = Stroke(ow))
    line(Gold, -1.2f, -2.2f, -1.2f, 2.2f, 0.7f, false)
    fun outline(path: Path) { drawPath(path, fill); drawPath(path, Arm, style = Stroke(ow, join = StrokeJoin.Round)) }
    fun finger(x: Float, y: Float, a: Float, len: Float, r: Float) {
        val e = Offset(x + cos(a) * len, y + sin(a) * len)
        drawLine(Arm, Offset(x, y), e, r * 2 + ow * 2, StrokeCap.Round); drawLine(fill, Offset(x, y), e, r * 2, StrokeCap.Round)
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
    // brilho de luva (dá volume e deixa a cor da luva aparecer)
    if (!small && shape != Hand.OPEN) rotate(-0.4f * 180f / PIf, pivot = Offset(1f, -1.1f)) { oval(Color.White.copy(alpha = 0.45f), -0.2f, -1.8f, 2.4f, 1.4f) }
}
