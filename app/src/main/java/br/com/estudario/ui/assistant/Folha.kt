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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
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
    // coleções de carreira: verde-oliva com camuflado, jaleco branco e o Vade Mecum de couro vinho
    "oliva" to CoverStyle(listOf(rgb(0xFF86935A), rgb(0xFF66733D), rgb(0xFF48542A)), rgb(0xFF262E12), listOf(rgb(0xFF5C6934), rgb(0xFF929F62)), silver = true, pattern = "camo", stitch = Color(0xB3D6CC96)),
    "jaleco" to CoverStyle(listOf(rgb(0xFFFFFFFF), rgb(0xFFEEF2F8), rgb(0xFFD3DBE7)), rgb(0xFF8D99AD), listOf(rgb(0xFFB8C4D6), rgb(0xFFE9EEF6)), silver = true, pattern = "jaleco", stitch = Color(0xB3468CC8)),
    // farda azul-marinho da polícia, lousa verde do professor e o terno chumbo de risca de giz do controle
    "farda" to CoverStyle(listOf(rgb(0xFF3A4A8C), rgb(0xFF22306A), rgb(0xFF141C46)), rgb(0xFF0A0F2A), listOf(rgb(0xFF1C2858), rgb(0xFF3A4A8C)), pattern = "farda", stitch = Color(0x99E9AE34)),
    "lousa" to CoverStyle(listOf(rgb(0xFF3F7A5C), rgb(0xFF2D5E45), rgb(0xFF1F4532)), rgb(0xFF10271B), listOf(rgb(0xFF2A5641), rgb(0xFF4E8A6A)), silver = true, pattern = "lousa"),
    "chumbo" to CoverStyle(listOf(rgb(0xFF555A66), rgb(0xFF3C404B), rgb(0xFF272A33)), rgb(0xFF14161C), listOf(rgb(0xFF33363F), rgb(0xFF626775)), silver = true, pattern = "risca"),
    "vademecum" to CoverStyle(listOf(rgb(0xFFB23A45), rgb(0xFF8C1F2C), rgb(0xFF62121C)), rgb(0xFF3A0910), listOf(rgb(0xFF7E1A26), rgb(0xFFBE4A55)), pattern = "codigo"),
    // armário 2.0: carreiras (saúde, militar, polícia, jurídico, fiscal, bancos, legislativo, TI) e estampas
    "pijama-cirurgico" to CoverStyle(listOf(rgb(0xFF6FD0C4), rgb(0xFF3FA69B), rgb(0xFF2A8078)), rgb(0xFF134640), listOf(rgb(0xFF2E8A82), rgb(0xFF74D6CA)), silver = true, pattern = "pijama", stitch = Color(0x99E1FFFA)),
    "deserto" to CoverStyle(listOf(rgb(0xFFE2CDA4), rgb(0xFFC9AD7C), rgb(0xFFA3875A)), rgb(0xFF5A4628), listOf(rgb(0xFFA88C5E), rgb(0xFFE0CBA0)), silver = true, pattern = "deserto", stitch = Color(0x99FFF0D2)),
    "colete" to CoverStyle(listOf(rgb(0xFF34405E), rgb(0xFF1F2740), rgb(0xFF121726)), rgb(0xFF06080E), listOf(rgb(0xFF1C2338), rgb(0xFF3E4A6A)), silver = true, pattern = "colete"),
    "processo" to CoverStyle(listOf(rgb(0xFFF2DCA8), rgb(0xFFDFC184), rgb(0xFFC4A262)), rgb(0xFF7A5E2C), listOf(rgb(0xFFBE9C5C), rgb(0xFFF0D8A2)), silver = true, pattern = "processo"),
    "nota-fiscal" to CoverStyle(listOf(rgb(0xFFFFFFFF), rgb(0xFFF5F2EA), rgb(0xFFE2DDCF)), rgb(0xFF9A937F), listOf(rgb(0xFFB9B3A2), rgb(0xFFF2EEE4)), silver = true, pattern = "nota"),
    "bancario" to CoverStyle(listOf(rgb(0xFF3368C4), rgb(0xFF1F4796), rgb(0xFF14306C)), rgb(0xFF0A1A40), listOf(rgb(0xFF1A3E86), rgb(0xFF4678D0)), pattern = "bancario"),
    "plenario" to CoverStyle(listOf(rgb(0xFF479866), rgb(0xFF2C6E45), rgb(0xFF1C4E2F)), rgb(0xFF0D2A18), listOf(rgb(0xFF245C3A), rgb(0xFF55A672)), pattern = "plenario"),
    "espiral" to CoverStyle(listOf(rgb(0xFFFF946F), rgb(0xFFF06A48), rgb(0xFFC94E32)), rgb(0xFF6E2614), listOf(rgb(0xFFD8583A), rgb(0xFFFFA283)), silver = true, pattern = "espiral"),
    "circuito" to CoverStyle(listOf(rgb(0xFF33A862), rgb(0xFF1E7E46), rgb(0xFF135A30)), rgb(0xFF08301A), listOf(rgb(0xFF1A6E3C), rgb(0xFF45B872)), pattern = "circuito"),
    "pasta-arquivo" to CoverStyle(listOf(rgb(0xFF86B8E8), rgb(0xFF5E96D2), rgb(0xFF4074B0)), rgb(0xFF1F4270), listOf(rgb(0xFF4A80BE), rgb(0xFF94C2EE)), silver = true, pattern = "pasta"),
    "girassol" to CoverStyle(listOf(rgb(0xFF78C0F2), rgb(0xFF4C98DA), rgb(0xFF3076B8)), rgb(0xFF163E6A), listOf(rgb(0xFF3A82C6), rgb(0xFF86C8F4)), pattern = "girassol"),
    "oceano" to CoverStyle(listOf(rgb(0xFF46BCE6), rgb(0xFF1F8EC6), rgb(0xFF11649A)), rgb(0xFF073A5C), listOf(rgb(0xFF187CB2), rgb(0xFF58C6EC)), silver = true, pattern = "ondas"),
    "melancia" to CoverStyle(listOf(rgb(0xFF62C870), rgb(0xFF3A9E4A), rgb(0xFF257A33)), rgb(0xFF0F4219), listOf(rgb(0xFF2F8A3E), rgb(0xFF70D27E)), silver = true, pattern = "melancia"),
    "cosmos" to CoverStyle(listOf(rgb(0xFF43237E), rgb(0xFF22104E), rgb(0xFF0B0626)), rgb(0xFF000010), listOf(rgb(0xFF2A1460), rgb(0xFF5A36A0)), silver = true, pattern = "cosmos"),
)

/** Luvas: preenchimento e punho ("arcoiris" tem degradê; "boxe" muda o formato da mão). */
private val GLOVES = mapOf(
    "branca" to listOf(Color.White, rgb(0xFFECE8F6)), "menta" to listOf(rgb(0xFFC9F7E4), rgb(0xFF7EE0B8)), "coral" to listOf(rgb(0xFFFFD3C9), rgb(0xFFFF8C75)),
    "preta" to listOf(rgb(0xFF3A3A46), rgb(0xFF24242E)), "dourada" to listOf(rgb(0xFFFFE9A6), rgb(0xFFF5B83D)),
    "arcoiris" to listOf(Color.White, rgb(0xFFB995F5)), "boxe" to listOf(rgb(0xFFE5373F), Color.White),
    // armário 2.0 (os detalhes de cada uma ficam em drawGloveDetail)
    "procedimento" to listOf(rgb(0xFFA8D2F8), rgb(0xFF6FA6E6)), "taticas" to listOf(rgb(0xFF4A4E40), rgb(0xFF2A2D22)), "operacional" to listOf(rgb(0xFF2E3A50), rgb(0xFF161C2A)),
    "gala" to listOf(Color.White, rgb(0xFFF2EEFF)), "couro-marrom" to listOf(rgb(0xFFA8693C), rgb(0xFF6D3D1F)), "chumbo" to listOf(rgb(0xFF5E636F), rgb(0xFF3C404B)),
    "marinho" to listOf(rgb(0xFF2E3C7C), rgb(0xFF1A2350)), "plenario" to listOf(rgb(0xFF3E8A5A), rgb(0xFF1C4E2F)), "giz" to listOf(Color.White, rgb(0xFFE8E4DA)),
    "rgb" to listOf(rgb(0xFFFF5A7A), rgb(0xFF6FB0FF)), "areia" to listOf(rgb(0xFFE8D6AC), rgb(0xFFC4A878)),
)

private class Look(
    val cover: CoverStyle, val ribbon: List<Color>, val hat: String, val eyes: String, val neck: String, val hand: String, val aura: String,
    val gloves: List<Color>, val streak: Int, val atRisk: Boolean, val morning: Boolean, val night: Boolean,
    val gloveKind: String = "branca", val pet: String = "nenhum", val back: String = "nada",
    val pages: String = "lisa", val ink: Color = Color(0xFF26215C), val cheek: Color? = Color(0xFFF4A6C0),
    val ribbonPat: String = "",
)

/** O próprio Folha: tinta dos olhos, sobrancelhas e boca, e cor das bochechas. */
private val INKS = mapOf(
    "marinho" to Color(0xFF26215C), "azul" to Color(0xFF1F4FD8), "vermelha" to Color(0xFFC42F4B), "verde" to Color(0xFF0F8A5F), "grafite" to Color(0xFF3A3A44),
    "roxa" to Color(0xFF6D3CE0), "cafe" to Color(0xFF6B3E1E), "ameixa" to Color(0xFF7A1F6A), "floresta" to Color(0xFF14523A), "dourada" to Color(0xFFA8700A), "nanquim" to Color(0xFF0B0B10),
    "terminal" to Color(0xFF139A44), "rosa-choque" to Color(0xFFE0177A), "laranja" to Color(0xFFE0661A), "turquesa" to Color(0xFF0E9AA0),
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
        ribbon = ribbonColors(a("ribbon")), ribbonPat = ribbonPattern(a("ribbon")),
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
    "verde-cirurgico" -> listOf(Color(0xFF2E8A82), Color(0xFF5FC4B8), Color(0xFFCDF2EC))
    "camuflada" -> listOf(Color(0xFF48542A), Color(0xFF86935A), Color(0xFFC9CFA0))
    "zebrada" -> listOf(Color(0xFF2A2410), Color(0xFFFFD21F), Color(0xFFFFF3A6))
    "vinho" -> listOf(Color(0xFF5E1220), Color(0xFF8C1F2C), Color(0xFFD97A88))
    "cedula" -> listOf(Color(0xFF2E7A4A), Color(0xFF5BAE76), Color(0xFFCDEBD6))
    "relatorio" -> listOf(Color(0xFF1E4592), Color(0xFF3A6FD0), Color(0xFFCFE0FF))
    "ouro-velho" -> listOf(Color(0xFF7A5814), Color(0xFFB08A2C), Color(0xFFE8D08A))
    "senado" -> listOf(Color(0xFF123E8C), Color(0xFF1F5BC4), Color(0xFFBFD6FF))
    "giz" -> listOf(Color(0xFFFF9DB8), Color(0xFFFFE36B), Color(0xFF9FE8C4), Color(0xFF9FD3FF))
    "cabo-rede" -> listOf(Color(0xFF1F5FC4), Color(0xFF3D8BEA), Color(0xFFBFDCFF))
    "clipes" -> listOf(Color(0xFF7E8296), Color(0xFFCDD0DC), Color.White)
    else -> listOf(MintDark, Mint, Color(0xFFC6F7E2))
}
/** Fitas com estampa ou formato próprio (o resto é só degradê). */
private fun ribbonPattern(id: String): String = when (id) {
    "camuflada" -> "camo"; "zebrada" -> "zebra"; "cedula" -> "cedula"; "ouro-velho" -> "brilho"; "giz" -> "giz"; "cabo-rede" -> "cabo"; "clipes" -> "clipes"
    else -> ""
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
        // o toque de página; tonto, os passarinhos de desenho animado
        val tonto = sim.poke()
        br.com.estudario.ui.brand.EstudarioSom.play(context, if (tonto) "tonto" else "toque", 0.7f)
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
            // camada própria: o Folha redesenha sozinho a cada quadro, sem regravar a tela inteira
            .graphicsLayer()
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
        val wide = look.back != "nada" || look.aura == "holofote" || look.aura == "fogos" || look.aura == "supernova"
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
private const val DEG = 180f / PIf

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
    // no susto, segurando um item: a mão do item se encolhe junto ao peito (sem soltar) e só a outra vai ao rosto
    if (holding && mood == FolhaMood.SURPRISED) { p.hL0 = 10f; p.hL1 = 68f; p.shapeL = Hand.FIST; p.rotL = -20f; p.bendL = 6f }
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
    /** Toque: reage e, no quarto toque rápido, fica tonto (devolve true quando ficou tonto agora). */
    fun poke(): Boolean {
        bounceAt = nowMs; idleSince = nowMs
        val all = reactionsNow()
        // o gesto mais novo da amizade aparece mais
        var k = if (all.size > REACTIONS.size && Random.nextInt(100) < 35) all.last().first else all.random().first
        if (k == lastReact) k = all[(all.indexOfFirst { it.first == k } + 1) % all.size].first
        react = k; reactAt = nowMs; lastReact = k
        if (k == "wink") { wink = true; winkUntil = nowMs + 710 }
        say(tapLine())
        taps.removeAll { nowMs - it > 1400 }; taps.add(nowMs)
        val tonto = taps.size >= 4
        if (tonto) { dizzyUntil = nowMs + 2200; taps.clear() }
        burstAt = nowMs
        return tonto
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
// as falas de toque, carinho e volta: o mesmo banco do site (FolhaFalas)
private val TAP_LINES = br.com.estudario.domain.FolhaFalas.tap
private val MORNING_LINES = br.com.estudario.domain.FolhaFalas.tapMorning
private val NIGHT_LINES = br.com.estudario.domain.FolhaFalas.tapNight
private val PET_LINES = br.com.estudario.domain.FolhaFalas.pet
internal val BACK_LINES = br.com.estudario.domain.FolhaFalas.back
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
        // o companheiro na frente de tudo o que fica atrás do Folha (mochila, manto, toga...)
        drawPet(t, f.look.pet, mood, small, f.burst)
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
        "colunas" -> {
            // o piso do fórum: dois degraus de mármore em perspectiva debaixo dos pés, com um reflexo correndo
            val steps = listOf(Triple(floatArrayOf(56f, 5f, 96.4f), Color(0xFFD9D4E8), Color(0xFFB9B2D0)), Triple(floatArrayOf(50f, 3.8f, 94.6f), Color(0xFFF7F5FC), Color(0xFFDCD7EA)))
            for ((q, c0, c1) in steps) {
                val rx = q[0]; val ry = q[1]; val y = q[2]
                drawOval(vg(listOf(c0, c1), y - ry, y + ry), Offset(50f - rx, y - ry), Size(2f * rx, 2f * ry)); drawOval(Color(0x59463C78), Offset(50f - rx, y - ry), Size(2f * rx, 2f * ry), style = Stroke(if (small) 0.8f else 0.4f))
            }
            if (!small) {
                val top = Path().apply { addOval(Rect(0f, 90.8f, 100f, 98.4f)) }; val sx = -10f + ((t * 14f) % 120f)
                clipPath(top) { drawRect(hg(listOf(Color.White.copy(alpha = 0f), Color.White.copy(alpha = 0.7f), Color.White.copy(alpha = 0f)), sx - 8f, sx + 8f), Offset(sx - 8f, 90f), Size(16f, 9f)) }
            }
        }
        "chuva-moedas" -> {
            // pilhas de moedas no chão dos dois lados (com moedas soltas e brilho correndo pelas bordas)
            fun stack(cx: Float, n: Int) {
                for (kk in 0 until n) {
                    val y = 95f - kk * 1.45f; val dx = sin(kk * 2.3f + cx) * 0.4f
                    drawRect(Color(0xFFA86A08), Offset(cx + dx - 3.4f, y), Size(6.8f, 1.2f)); drawArc(Color(0xFFA86A08), 0f, 180f, true, Offset(cx + dx - 3.4f, y + 0.3f), Size(6.8f, 1.8f))
                    val o = Offset(cx + dx - 3.4f, y - 0.9f); val sz = Size(6.8f, 1.8f)
                    drawOval(hg(listOf(Color(0xFFC98A12), Color(0xFFFFE59A), Gold), cx - 3.4f, cx + 3.4f), o, sz); drawOval(Color(0xB3965A05), o, sz, style = Stroke(0.25f))
                }
            }
            for ((cx, n) in listOf(5f to 5, 12f to 3, 88f to 3, 95f to 6)) stack(cx, if (small) minOf(n, 3) else n)
            for ((cx, cy) in listOf(19f to 96.4f, 81f to 96.6f)) { val o = Offset(cx - 2.6f, cy - 0.75f); val sz = Size(5.2f, 1.5f); drawOval(hg(listOf(Color(0xFFC98A12), Color(0xFFFFE59A), Gold), cx - 2.6f, cx + 2.6f), o, sz); drawOval(Color(0xB3965A05), o, sz, style = Stroke(0.25f)) }
            if (!small) {
                val gx = floatArrayOf(5f, 12f, 88f, 95f); val gy = floatArrayOf(88.6f, 91.6f, 91.6f, 87.2f)
                for (i in 0 until 4) { val ph = (t * 0.6f + i / 4f) % 1f; val a = sin(PIf * ph); star4(gx[i] + 2f, gy[i], 1.5f * a, Color.White.copy(alpha = a)) }
            }
        }
        "outono" -> {
            // folhas caídas no chão, deitadas (achatadas), dos dois lados
            val cs = listOf(Color(0xFFFF8A5C) to Color(0xFFE0472A), Color(0xFFFFC266) to Color(0xFFF08A24), Color(0xFFFFE27A) to Color(0xFFE0A82A), Color(0xFFD9965A) to Color(0xFF9A5A2A), Color(0xFFFF7A88) to Color(0xFFC8324A))
            for (q in listOf(floatArrayOf(4f, 0.4f, 0f), floatArrayOf(11f, 2.1f, 1f), floatArrayOf(19f, -0.8f, 3f), floatArrayOf(80f, 1.4f, 2f), floatArrayOf(88f, -1.9f, 4f), floatArrayOf(96f, 0.6f, 1f))) {
                val (c0, c1) = cs[q[2].toInt()]
                scale(1f, 0.45f, pivot = Offset(0f, 95.6f)) { drawAuraLeaf(q[0], 95.6f, if (small) 1.8f else 1.4f, q[1], 1f, c0, c1, 1f) }
            }
        }
        "neve" -> {
            // camada de neve no chão com dois montinhos dos lados e brilhinhos de gelo
            drawOval(vg(listOf(Color.White, Color(0xFFDCE6F8)), 92.4f, 98.8f), Offset(-6f, 92.4f), Size(112f, 6.4f)); drawOval(Color(0x666F8FE0), Offset(-6f, 92.4f), Size(112f, 6.4f), style = Stroke(if (small) 0.8f else 0.4f))
            for ((cx, rx) in listOf(8f to 11f, 92f to 10f)) {
                val p = Path().apply { moveTo(cx - rx, 96f); cubicTo(cx - rx * 0.6f, 90.6f, cx + rx * 0.5f, 90f, cx + rx, 96f); close() }
                drawPath(p, vg(listOf(Color.White, Color(0xFFE2EAF8)), 90.6f, 96f)); drawPath(p, Color(0x736F8FE0), style = stroke(if (small) 0.8f else 0.4f))
            }
            if (!small) {
                val gx = floatArrayOf(5f, 12f, 30f, 70f, 90f); val gy = floatArrayOf(93f, 94f, 96.4f, 96.6f, 93.4f)
                for (i in 0 until 5) { val ph = (t * 0.5f + i / 5f) % 1f; val a = sin(PIf * ph); star4(gx[i], gy[i], 1.3f * a, Color(0xFFAAD2FF).copy(alpha = a)) }
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

// Costas: atrás do livro, presas nos ombros. Mochila de estudos com chaveiro que balança, mochila-foguete
// com chama que acende quando ele pula, manto real esvoaçante com gola de arminho e barra dourada.
private fun DrawScope.drawBack(t: Float, kind: String, mood: FolhaMood, small: Boolean, hop: Float) {
    if (kind == "nada") return
    val party = mood == FolhaMood.HAPPY || mood == FolhaMood.ECSTATIC; val air = clamp((-hop - 1.5f) / 4f, 0f, 1f)
    fun mirrored(side: Float, block: DrawScope.() -> Unit) = if (side > 0f) scale(-1f, 1f, pivot = Offset(50f, 0f)) { block() } else block()
    when (kind) {
        "mochila" -> {
            // mochila de estudos pendurada no ombro esquerdo (igual ao site): lona índigo, bolso com zíper, régua e
            // lápis saindo por cima, etiqueta menta com o "E" e um chaveiro de estrela que balança (mais na festa)
            val sw = sin(t * (if (party) 7f else 1.6f)) * (if (party) 0.28f else 0.1f) + air * 0.2f
            val gold3 = listOf(Color(0xFFFFF1BF), Gold, Color(0xFFC98A12))
            rotate((-0.08f + sin(t * 1.2f) * 0.02f - air * 0.06f) * DEG, pivot = Offset(2f, 60f)) {
                translate(-1f, 44f) { rotate(-0.22f * DEG, pivot = Offset.Zero) {
                    val rg = roundRect(-1.6f, -13f, 3.2f, 15f, 0.4f)
                    drawPath(rg, vg(listOf(Color(0xFFFFE58A), Color(0xFFF5C842)), -13f, 2f)); drawPath(rg, Color(0x73785008), style = stroke(0.3f))
                    if (!small) for (i in 0 until 6) line(Color(0x99785008), -1.6f, -11f + i * 2f, -0.4f - (i % 2) * 0.6f, -11f + i * 2f, 0.25f)
                } }
                drawPencilAt(8.5f, 46f, 12.5f, 32f, 1.25f)
                val body = Path().apply { moveTo(-9f, 50f); quadraticTo(-9.6f, 43f, -2f, 42.4f); lineTo(10f, 42.4f); quadraticTo(15.6f, 43f, 15.4f, 50f); lineTo(15f, 80f); quadraticTo(15f, 84f, 10.6f, 84f); lineTo(-4.6f, 84f); quadraticTo(-9f, 84f, -9f, 80f); close() }
                drawPath(body, hg(listOf(Color(0xFF1D1670), Color(0xFF3A2FC4), Color(0xFF2A209E), Color(0xFF16105A)), -9f, 15.4f))
                if (!small) clipPath(body) { for (i in 0 until 14) line(Color.White.copy(alpha = 0.05f), -9f + i * 2f, 42f, -9f + i * 2f - 6f, 84f, 0.5f) }
                drawPath(body, Color(0x990A0828), style = stroke(if (small) 1f else 0.5f))
                val flap = Path().apply { moveTo(-8.4f, 50f); quadraticTo(-8.6f, 44f, -2f, 43.6f); lineTo(10f, 43.6f); quadraticTo(14.8f, 44f, 14.8f, 50f); quadraticTo(3f, 56f, -8.4f, 50f); close() }
                drawPath(flap, vg(listOf(Color(0xFF5247E0), Color(0xFF3A2FC4)), 43.6f, 54f))
                if (!small) drawPath(Path().apply { moveTo(-7f, 49.4f); quadraticTo(3f, 54.2f, 13.4f, 49.4f) }, Color.White.copy(alpha = 0.45f), style = Stroke(0.3f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.9f, 0.7f))))
                val bk = roundRect(1.4f, 51.2f, 3.6f, 3.2f, 0.6f); drawPath(bk, hg(gold3, 1.4f, 5f)); drawPath(bk, Color(0x995A3C05), style = stroke(0.3f))
                val pk = roundRect(-6.4f, 63f, 18.8f, 16f, 3f); drawPath(pk, vg(listOf(Color(0xFF3A2FC4), Color(0xFF2A209E)), 63f, 79f)); drawPath(pk, Color(0x800A0828), style = stroke(0.35f))
                line(Color(0xFFC9CCE0), -4.6f, 66.2f, 10.6f, 66.2f, 0.55f)
                if (!small) { for (i in 0 until 15) line(Color(0xB31E195A), -4.4f + i, 65.8f, -4.4f + i, 66.6f, 0.2f); drawPath(roundRect(8.6f, 66f, 1.4f, 3f, 0.5f), hg(gold3, 8.6f, 10f)) }
                drawPath(roundRect(-0.6f, 70.6f, 6.4f, 4.4f, 1f), Mint)
                if (!small) {
                    // o "E" da etiqueta, em traços
                    val ink = Color(0xFF1D1670)
                    line(ink, 1.8f, 71.6f, 1.8f, 74f, 0.55f); line(ink, 1.8f, 71.6f, 3.6f, 71.6f, 0.55f); line(ink, 1.8f, 72.8f, 3.2f, 72.8f, 0.55f); line(ink, 1.8f, 74f, 3.6f, 74f, 0.55f)
                    rotate(sw * DEG, pivot = Offset(9.3f, 69f)) {
                        line(Color(0xFFB9BCD0), 9.3f, 69f, 9.3f, 73f, 0.35f)
                        drawCircle(Color(0xFFD7D9E8), 0.8f, Offset(9.3f, 73.6f), style = stroke(0.35f))
                        val st = star5(9.3f, 76.8f, 2.4f); drawPath(st, hg(gold3, 6.9f, 11.7f)); drawPath(st, Color(0x8C785008), style = stroke(0.25f))
                    }
                    line(Color.White.copy(alpha = 0.35f), -7.6f, 52f, -7.8f, 80f, 0.6f)
                }
            }
            val sp = Path().apply { moveTo(-2f, 43f); cubicTo(2f, 36f, 10f, 35f, 16f, 40f) }
            drawPath(sp, Color(0xFF16105A), style = stroke(if (small) 3.4f else 2.8f)); drawPath(sp, Color(0xFF2A209E), style = stroke(if (small) 2.2f else 1.8f))
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
        "paraquedas" -> {
            // mochila de paraquedista: as bolsas verde-oliva nas laterais e o velame aberto lá em cima, em gomos
            // verdes e amarelos, balançando; no pulo e na festa ele infla e sobe um pouco
            val lift = (if (party) 3f else 0f) + air * 4f; val sway = sin(t * 1.1f) * 0.045f; val cy = 6f - lift
            rotate(sway * DEG, pivot = Offset(50f, 60f)) {
                if (!small) for (side in listOf(-1f, 1f)) for (k in 0..3) line(Color(0x8C3C4028), 50f + side * (46f - k * 10f), cy + 4f + k * 0.6f, 50f + side * 46f, 50f, 0.3f)
                val w = 48f + (if (party) 2f else 0f); val h = 17f + lift * 0.6f; val n = 8
                val dome = Path().apply {
                    moveTo(50f - w, cy + 4f); cubicTo(50f - w, cy - h * 1.25f, 50f + w, cy - h * 1.25f, 50f + w, cy + 4f)
                    for (i in n downTo 1) { val x0 = 50f - w + (i.toFloat() / n) * 2f * w; val x1 = 50f - w + ((i - 1).toFloat() / n) * 2f * w; quadraticTo((x0 + x1) / 2f, cy + 1.2f, x1, cy + 4f) }
                    close()
                }
                clipPath(dome) {
                    // gomos em leque a partir do topo (cobrem o velame inteiro, até as abas)
                    val ax = 50f; val ay = cy - h * 0.94f
                    for (i in 0 until n) {
                        val a0 = PIf - (i.toFloat() / n) * PIf; val a1 = PIf - ((i + 1).toFloat() / n) * PIf
                        drawPath(Path().apply { moveTo(ax, ay); lineTo(ax + cos(a0) * 140f, ay + sin(a0) * 60f + 2f); lineTo(ax + cos(a1) * 140f, ay + sin(a1) * 60f + 2f); close() }, if (i % 2 == 1) Color(0xFFFFD23F) else Color(0xFF1E9A48))
                    }
                    drawPath(dome, hg(listOf(Color(0x38000000), Color(0x2EFFFFFF), Color(0x47000000)), 50f - w, 50f + w))
                }
                drawPath(dome, Color(0x73143C1E), style = stroke(if (small) 0.9f else 0.4f))
                drawCircle(Color(0xFF1F3F9A), 2.4f, Offset(50f, cy - h * 0.94f)); drawPath(star5(50f, cy - h * 0.94f, 1.3f), Color.White)
            }
            // as bolsas nas laterais: lona com bolso, alças e fivela
            for (side in listOf(-1f, 1f)) mirrored(side) {
                val bag = roundRect(-5.6f, 49f, 15f, 30f, 4f)
                drawPath(bag, hg(listOf(Color(0xFF46512A), Color(0xFF7E8B4E), Color(0xFF5C6934)), -5.6f, 9.4f)); drawPath(bag, Color(0x991E240E), style = stroke(if (small) 1f else 0.45f))
                val pk = roundRect(-4.4f, 63f, 8.4f, 11f, 2f); drawPath(pk, Color(0xFF66733D)); drawPath(pk, Color(0x801E240E), style = stroke(0.35f))
                drawRect(Color(0xFF3E4A20), Offset(-4.4f, 63f), Size(8.4f, 2.6f))
                drawPath(roundRect(-1.4f, 64.6f, 2.8f, 2f, 0.4f), Color(0xFFC9D0DC), style = stroke(0.45f))
                if (!small) {
                    line(Color.White.copy(alpha = 0.2f), -3.8f, 52f, -3.8f, 76f, 0.6f)
                    drawPath(roundRect(-4.6f, 50f, 13f, 28f, 3.2f), Color(0x99D6CC96), style = Stroke(0.25f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.8f, 0.6f))))
                }
            }
        }
        "toga" -> {
            // toga de magistrado: cetim preto com pregas que balançam, brilho que corre, a murça de veludo sobre
            // os ombros com filete dourado e o cordão com borla pendurado
            val wave = sin(t * 1.5f); val fl = air * 5f + (if (party) 2.5f else 0f)
            val l = -4f - fl + wave; val r = 104f + fl - wave
            val hem = (1..8).map { i -> floatArrayOf(l + (r - l) * (i - 0.5f) / 8f, 103.4f + sin(t * 2f + i) * 0.7f, l + (r - l) * i / 8f, 101f + sin(t * 1.8f + i * 1.4f) * 0.3f) }
            val robe = Path().apply { moveTo(15f, 44f); cubicTo(6f, 60f, l + 2f, 82f, l, 101f); hem.forEach { quadraticTo(it[0], it[1], it[2], it[3]) }; cubicTo(r - 2f, 82f, 94f, 60f, 85f, 44f); close() }
            drawPath(robe, hg(listOf(Color(0xFF050508), Color(0xFF1E1E2A), Color(0xFF2E2E3E), Color(0xFF1E1E2A), Color(0xFF050508)), l, r))
            if (!small) clipPath(robe) {
                for (i in 0 until 9) { val x = l + 5f + (r - l - 10f) * i / 8f + sin(t * 1.5f + i) * 1f; drawRect(hg(listOf(Color(0x00FFFFFF), Color(0x17FFFFFF), Color(0x00FFFFFF)), x - 2.6f, x + 2.6f), Offset(x - 2.6f, 44f), Size(5.2f, 62f)) }
                val sx = -40f + ((t * 0.3f) % 1f) * 190f
                drawRect(Brush.linearGradient(listOf(Color(0x00FFFFFF), Color(0x2EBEC8FF), Color(0x00FFFFFF)), start = Offset(sx, 40f), end = Offset(sx + 16f, 104f)), Offset(-20f, 40f), Size(140f, 70f))
            }
            drawPath(robe, Color(0x99000000), style = stroke(if (small) 1f else 0.5f))
            for (side in listOf(-1f, 1f)) {
                val cx = 50f + side * 37f
                rotate(side * -0.14f * DEG, pivot = Offset(cx, 44.6f)) {
                    translate(cx, 44.6f) {
                        val m = Path().apply { moveTo(-11f, -2.6f); quadraticTo(0f, -6f, 11f, -2.6f); quadraticTo(12.4f, 3f, 7f, 6.4f); quadraticTo(0f, 8.4f, -7f, 6.4f); quadraticTo(-12.4f, 3f, -11f, -2.6f); close() }
                        drawPath(m, vg(listOf(Color(0xFF3A3A4C), Color(0xFF14141C)), -6f, 8f)); drawPath(m, Color(0xFFC98A12), style = stroke(if (small) 0.9f else 0.55f))
                        if (!small) drawPath(Path().apply { moveTo(-8f, -1.8f); quadraticTo(-1f, -4.4f, 6f, -2.4f) }, Color.White.copy(alpha = 0.22f), style = stroke(0.6f))
                    }
                }
            }
            if (!small) {
                val sw = sin(t * 2f) * 1.2f
                val cord = Path().apply { moveTo(84f, 48.6f); quadraticTo(95f, 52f, 95.6f + sw * 0.5f, 62f) }
                drawPath(cord, Color(0xFF9C6A12), style = stroke(0.9f)); drawPath(cord, Gold, style = stroke(0.45f))
                drawCircle(Color(0xFFE9AE34), 1.1f, Offset(95.6f + sw * 0.5f, 62.4f))
                drawPath(Path().apply { moveTo(94.6f + sw * 0.5f, 63f); lineTo(96.6f + sw * 0.5f, 63f); lineTo(97.6f + sw, 68.6f); lineTo(93.6f + sw, 68.6f); close() }, vg(listOf(Color(0xFFFFE08A), Color(0xFFC98A12)), 63f, 68.6f))
                for (i in 0 until 4) line(Color(0x99965F0A), 94.2f + sw + i, 64.4f, 94.2f + sw + i, 68.4f, 0.2f)
            }
        }
        "asas-fenix" -> {
            // asas de fênix (Pro, iguais ao site): asas erguidas em V, feitas de penas de fogo. Cada pena nasce
            // vermelha no ombro, passa pelo laranja e acaba numa ponta amarela que tremula como chama; um
            // miolo claro brilha por dentro, chamas pequenas se soltam das pontas e brasas sobem devagar.
            val busy = party || air > 0.1f
            val flap = sin(t * (if (busy) 7f else 1.8f)) * (if (busy) 10f else 3f) - air * 6f
            if (!small) drawCircle(Brush.radialGradient(listOf(Color(0x59FF7A1A), Color(0x26FF4D1A), Color(0x00FF4D1A)), center = Offset(50f, 50f), radius = 62f), 62f, Offset(50f, 50f))
            fun DrawScope.fireFeather(len0: Float, w: Float, bend0: Float, ph: Float, core: Boolean) {
                val len = len0 * (1f + 0.04f * sin(t * 6f + ph))
                val bend = bend0 + sin(t * 9f + ph) * 1.1f
                val p = Path().apply {
                    moveTo(0f, -w * 0.3f)
                    cubicTo(-len * 0.3f, -w * 1.05f, -len * 0.62f, -w * 0.5f + bend * 0.5f, -len * 0.8f, -w * 0.32f + bend * 0.8f)
                    // a ponta é uma língua de fogo: afina, ondula e volta
                    quadraticTo(-len * 0.9f, -w * 0.5f + bend, -len, bend)
                    quadraticTo(-len * 0.88f, w * 0.15f + bend, -len * 0.8f, w * 0.45f + bend * 0.8f)
                    cubicTo(-len * 0.62f, w * 0.75f + bend * 0.5f, -len * 0.3f, w * 1.0f, 0f, w * 0.3f); close()
                }
                drawPath(p, hg(listOf(Color(0xFFFFE07A), Color(0xFFFF9F1A), Color(0xFFF2552C), Color(0xFFB3261E), Color(0xFF8C1A14)), -len, 0f))
                if (core && !small) {
                    val c = Path().apply { moveTo(-len * 0.12f, 0f); quadraticTo(-len * 0.5f, -w * 0.32f + bend * 0.3f, -len * 0.86f, bend * 0.9f); quadraticTo(-len * 0.5f, w * 0.32f + bend * 0.3f, -len * 0.12f, 0f); close() }
                    drawPath(c, hg(listOf(Color(0xCCFFFBE6), Color(0x66FFE07A), Color(0x00FFE07A)), -len * 0.86f, -len * 0.12f))
                }
                drawPath(p, Color(0xB38C2410), style = stroke(if (small) 0.9f else 0.45f))
                if (core && !small) for (k in 0 until 2) {
                    // chamas pequenas que se soltam da ponta e somem
                    val q = (t * 1.6f + ph * 0.3f + k * 0.5f) % 1f; val x = -len - 1f - q * 9f; val y = bend + sin(t * 7f + ph + k) * 1.5f - q * 1.5f; val r = 2.2f * (1f - q)
                    drawPath(Path().apply { moveTo(x + r, y - r * 0.6f); quadraticTo(x - r * 1.6f, y, x + r, y + r * 0.6f); quadraticTo(x + r * 0.2f, y, x + r, y - r * 0.6f); close() },
                        Color(if (k == 0) 0xFFFFC23D else 0xFFFF7A1A).copy(alpha = 0.85f * (1f - q)))
                }
            }
            for (side in listOf(-1f, 1f)) mirrored(side) {
                rotate(flap, pivot = Offset(20f, 56f)) {
                    translate(20f, 56f) {
                        for (i in 4 downTo 0) { val len = 56f - i * 5f
                            rotate(62f - i * 15f, pivot = Offset.Zero) { fireFeather(len, 6.6f, len * 0.12f, i * 1.3f + side, true) } }
                        for (i in 3 downTo 0) { val len = 24f - i * 1.5f
                            rotate(58f - i * 18f, pivot = Offset.Zero) { fireFeather(len, 7f, len * 0.08f, i * 2f + side, false) } }
                    }
                }
            }
            if (!small) for (i in 0 until 7) {
                val ph = (t * 0.4f + i / 7f) % 1f; val sx = if (i % 2 == 1) 1f else -1f
                drawCircle(Color(if (i % 3 == 0) 0xFFFFE07A else 0xFFFF9F3A).copy(alpha = 0.8f * sin(PIf * ph)), 0.55f + 0.35f * (1f - ph), Offset(50f + sx * (24f + i * 4f) + sin(t * 2.4f + i) * 2f, 44f - ph * 40f))
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
        "socorro" -> {
            // mochila de primeiros socorros no ombro esquerdo (igual ao site): lona vermelha com o zíper prateado em
            // volta, a cruz branca grande, a alça de mão em cima e o puxador do zíper que balança (mais na festa)
            val sw = sin(t * (if (party) 7f else 1.6f)) * (if (party) 0.28f else 0.1f) + air * 0.2f
            rotate((-0.08f + sin(t * 1.2f) * 0.02f - air * 0.06f) * DEG, pivot = Offset(2f, 60f)) {
                val hd = Path().apply { moveTo(-3f, 43f); quadraticTo(2f, 36.4f, 7f, 43f) }
                drawPath(hd, Color(0xFF5A0A14), style = stroke(if (small) 2.4f else 2f)); drawPath(hd, Color(0xFFB3182A), style = stroke(if (small) 1.4f else 1.1f))
                val body = roundRect(-10f, 42.4f, 26f, 41.6f, 5f)
                drawPath(body, hg(listOf(Color(0xFF7A0F1E), Color(0xFFE0303F), Color(0xFFC42032), Color(0xFF6E0B18)), -10f, 16f))
                if (!small) clipPath(body) { for (i in 0 until 14) line(Color.White.copy(alpha = 0.05f), -10f + i * 2f, 42f, -16f + i * 2f, 84f, 0.5f) }
                drawPath(body, Color(0xA632050A), style = stroke(if (small) 1f else 0.5f))
                val zp = roundRect(-8f, 44.6f, 22f, 37.2f, 3.6f); drawPath(zp, Color(0xFFC9CCE0), style = stroke(0.55f))
                if (!small) drawPath(zp, Color(0xCC3C0A14), style = Stroke(0.5f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.25f, 0.55f))))
                // a cruz branca (fica na parte que aparece, à esquerda do livro)
                val cx = -1f; val cy = 62f
                val cr = Path().apply { addPath(roundRect(cx - 2.2f, cy - 6.4f, 4.4f, 12.8f, 0.8f)); addPath(roundRect(cx - 6.4f, cy - 2.2f, 12.8f, 4.4f, 0.8f)) }
                translate(0.4f, 0.5f) { drawPath(cr, Color(0x593C050F)) }
                drawPath(cr, vg(listOf(Color.White, Color(0xFFF1E6EA)), cy - 6.4f, cy + 6.4f))
                if (!small) {
                    line(Color.White.copy(alpha = 0.4f), -8.8f, 48f, -9f, 80f, 0.6f)
                    rotate(sw * DEG, pivot = Offset(-8f, 50f)) {
                        line(Color(0xFFB9BCD0), -8f, 50f, -8f, 53f, 0.4f)
                        drawPath(roundRect(-8.9f, 53f, 1.8f, 3.2f, 0.6f), hg(listOf(Color(0xFF8E95A6), Color(0xFFF3F6FA), Color(0xFF9AA3B4)), -8.9f, -7.1f))
                    }
                }
            }
            val sp = Path().apply { moveTo(-2f, 43f); cubicTo(2f, 36f, 10f, 35f, 16f, 40f) }
            drawPath(sp, Color(0xFF5A0A14), style = stroke(if (small) 3.4f else 2.8f)); drawPath(sp, Color(0xFFB3182A), style = stroke(if (small) 2.2f else 1.8f))
        }
        "mochila-tatica" -> mirrored(1f) {
            // mochila tática no ombro direito: lona preta com as faixas de fixação costuradas, duas tiras com fivela
            // de engate, a alça de mão e um patch verde-oliva com a estrela; balança de leve
            rotate((-0.07f + sin(t * 1.1f + 1f) * 0.02f - air * 0.06f) * DEG, pivot = Offset(2f, 60f)) {
                val hd = Path().apply { moveTo(-4f, 42.6f); quadraticTo(1f, 36.4f, 6f, 42.6f) }
                drawPath(hd, Color(0xFF0A0B0E), style = stroke(if (small) 2.4f else 2f)); drawPath(hd, Color(0xFF3A3F4C), style = stroke(if (small) 1.4f else 1.1f))
                val body = roundRect(-11f, 42f, 27f, 43f, 4.4f)
                drawPath(body, hg(listOf(Color(0xFF0E1014), Color(0xFF343844), Color(0xFF22252D), Color(0xFF0A0B0E)), -11f, 16f))
                drawPath(body, Color(0xBF000000), style = stroke(if (small) 1f else 0.5f))
                for (y in listOf(52f, 58f, 64f, 70f)) {
                    drawRect(Color(0xFF14161C), Offset(-10f, y), Size(19f, 2.4f))
                    if (!small) { var x = -9f; while (x < 9f) { line(Color(0x73969BAA), x, y + 0.2f, x, y + 2.2f, 0.25f); x += 3.2f }; line(Color.White.copy(alpha = 0.08f), -10f, y + 0.2f, 9f, y + 0.2f, 0.3f) }
                }
                for (y in listOf(46.6f, 77f)) {
                    drawRect(Color(0xFF1B1D24), Offset(-11f, y), Size(21f, 1.8f))
                    val bk = roundRect(-6.4f, y - 0.6f, 4.2f, 3f, 0.6f); drawPath(bk, hg(listOf(Color(0xFF3A3E4A), Color(0xFF6B7088), Color(0xFF3A3E4A)), -6.4f, -2.2f)); drawPath(bk, Color(0x99000000), style = stroke(0.25f))
                    line(Color(0xFF1B1D24), -4.3f, y - 0.2f, -4.3f, y + 2.2f, 0.5f)
                }
                val pt = roundRect(-8.4f, 55.2f, 9.4f, 7.2f, 1.2f); drawPath(pt, vg(listOf(Color(0xFF7E8B4E), Color(0xFF5C6934)), 55.2f, 62.4f)); drawPath(pt, Color(0x99141905), style = stroke(0.3f))
                if (!small) drawPath(roundRect(-7.8f, 55.8f, 8.2f, 6f, 0.9f), Color(0xB3E6E1B4), style = Stroke(0.2f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.6f, 0.45f))))
                drawPath(star5(-3.7f, 59f, 2f), Color(0xFF2B3214))
                if (!small) line(Color.White.copy(alpha = 0.18f), -9.8f, 46f, -9.8f, 82f, 0.6f)
                val sp = Path().apply { moveTo(-2f, 43f); cubicTo(2f, 36f, 10f, 35f, 16f, 40f) }
                drawPath(sp, Color(0xFF0A0B0E), style = stroke(if (small) 3.4f else 2.8f)); drawPath(sp, Color(0xFF2E323C), style = stroke(if (small) 2.2f else 1.8f))
            }
        }
        "arquivo" -> {
            // arquivo de pastas atrás do livro: o gaveteiro de aço cinza-azulado aparece por cima e pelos lados, com
            // a gaveta de cima aberta e as pastas coloridas espetadas (que sobem e descem), o porta-etiqueta, o
            // puxador e os pezinhos embaixo
            val top = 18f; val cab = roundRect(5f, top, 90f, 74f, 3f)
            drawPath(cab, hg(listOf(Color(0xFF5E6A80), Color(0xFFA9B4C8), Color(0xFF8A96AC), Color(0xFF55607A)), 5f, 95f))
            drawPath(cab, Color(0x99141928), style = stroke(if (small) 1f else 0.5f))
            for (y in listOf(42f, 66f)) line(Color(0x8C1E2337), 5f, y, 95f, y, 0.5f, false)
            drawRect(Color(0xFF2E3346), Offset(8f, 92f), Size(6f, 2.4f)); drawRect(Color(0xFF2E3346), Offset(86f, 92f), Size(6f, 2.4f))
            val cols = listOf(0xFFFF6F8E, 0xFFFFD23F, 0xFF6FB0FF, 0xFF7EE0B8, 0xFFB995F5, 0xFFFF9F3A, 0xFF5247E0).map { Color(it) }
            for (i in 0 until 7) {
                val x = 21.8f + i * 9.4f; val y = top - 3f + (i % 2) * 1.4f + sin(t * 1.6f + i * 1.3f) * (if (party) 1.6f else 0.6f) - air * 1.5f
                val f = Path().apply { moveTo(x - 4f, y + 8f); lineTo(x - 4f, y + 1f); lineTo(x - 1f, y + 1f); lineTo(x, y - 0.8f); lineTo(x + 3f, y - 0.8f); lineTo(x + 3.6f, y + 1f); lineTo(x + 4f, y + 1f); lineTo(x + 4f, y + 8f); close() }
                drawPath(f, cols[i]); drawRect(Color.White.copy(alpha = 0.28f), Offset(x - 4f, y + 1f), Size(8f, 1.2f)); drawPath(f, Color(0x731E1E3C), style = stroke(0.3f))
                if (!small) drawRect(Color.White, Offset(x + 0.4f, y - 0.2f), Size(2.2f, 0.8f))
            }
            val dr = roundRect(12f, top + 2f, 76f, 22f, 2f)
            drawPath(dr, vg(listOf(Color(0xFFC3CCDC), Color(0xFF8E9AB0), Color(0xFF6E7A92)), top + 2f, top + 24f)); drawPath(dr, Color(0x8C141928), style = stroke(0.45f))
            line(Color.White.copy(alpha = 0.55f), 14f, top + 3.2f, 86f, top + 3.2f, 0.5f)
            val lb = roundRect(42f, top + 5.4f, 16f, 6f, 0.8f); drawPath(lb, hg(listOf(Color(0xFF8E95A6), Color(0xFFF3F6FA), Color(0xFF9AA3B4)), 42f, 58f)); drawPath(lb, Color(0x801E2332), style = stroke(0.3f))
            drawRect(Color(0xFFFFFDF4), Offset(43.2f, top + 6.4f), Size(13.6f, 4f))
            if (!small) for ((yy, w) in listOf(top + 7.6f to 9f, top + 9.2f to 6f)) line(Color(0x8C3C3C6E), 44.4f, yy, 44.4f + w, yy, 0.4f)
            val hb = roundRect(41f, top + 14f, 18f, 3.2f, 1.6f); drawPath(hb, hg(listOf(Color(0xFF5E6578), Color(0xFFE8ECF4), Color(0xFF7C8398)), 41f, 59f)); drawPath(hb, Color(0x99141928), style = stroke(0.3f))
        }
        "malote" -> {
            // malote do banco: bolsa de lona cinza-azulada pendurada no ombro direito pela alça de couro, com o
            // reforço de couro embaixo, o zíper e o cadeado de latão que balança
            val sw = sin(t * (if (party) 6f else 1.4f)) * (if (party) 0.12f else 0.05f) + air * 0.08f
            val strap = Path().apply { moveTo(78f, 44f); cubicTo(86f, 36f, 96f, 40f, 100f, 57f) }
            drawPath(strap, Color(0xFF3A2418), style = stroke(if (small) 3.2f else 2.6f)); drawPath(strap, Color(0xFF7A5236), style = stroke(if (small) 2f else 1.6f))
            rotate(sw * DEG, pivot = Offset(100f, 57f)) {
                val bag = Path().apply { moveTo(85f, 58f); lineTo(113f, 58f); quadraticTo(115f, 58f, 114.6f, 61f); lineTo(112.6f, 84f); quadraticTo(112.4f, 86f, 110f, 86f); lineTo(88f, 86f); quadraticTo(85.6f, 86f, 85.4f, 84f); lineTo(83.4f, 61f); quadraticTo(83f, 58f, 85f, 58f); close() }
                drawPath(bag, hg(listOf(Color(0xFF4A5670), Color(0xFF8C9AB4), Color(0xFF6E7C98), Color(0xFF3E4860)), 83f, 115f))
                clipPath(bag) {
                    if (!small) { for (i in 0 until 17) line(Color.White.copy(alpha = 0.06f), 83f + i * 2f, 58f, 83f + i * 2f, 86f, 0.4f); var y = 59f; while (y < 86f) { line(Color.Black.copy(alpha = 0.05f), 83f, y, 115f, y, 0.3f); y += 1.6f } }
                    drawRect(vg(listOf(Color(0xFF7A5236), Color(0xFF4A2E1C)), 80.6f, 86f), Offset(80f, 80.6f), Size(40f, 6f))
                }
                drawPath(bag, Color(0x99141928), style = stroke(if (small) 1f else 0.5f))
                line(Color(0xFFC9CCE0), 85f, 61.4f, 113f, 61.4f, 0.6f)
                if (!small) for (i in 0 until 14) line(Color(0xB3282D3C), 85.6f + i * 2f, 61f, 85.6f + i * 2f, 61.8f, 0.25f)
                translate(98.5f, 62f) { rotate(sin(t * 2.2f) * 0.12f * DEG, pivot = Offset.Zero) {
                    val sh = Path().apply { moveTo(-1.6f, 3f); lineTo(-1.6f, 1.4f); arcTo(Rect(-1.6f, -0.2f, 1.6f, 3f), 180f, 180f, false); lineTo(1.6f, 3f) }
                    drawPath(sh, Color(0xFF7C8398), style = stroke(0.75f)); drawPath(sh, Color(0xFFE3E6F0), style = stroke(0.3f))
                    val lk = roundRect(-2.6f, 2.6f, 5.2f, 4.8f, 1f); drawPath(lk, vg(listOf(Color(0xFFFFE58A), Color(0xFFE9AE34), Color(0xFF9C6A12)), 2.6f, 7.4f)); drawPath(lk, Color(0xB35A3C05), style = stroke(0.3f))
                    drawCircle(Color(0xFF4A2E06), 0.55f, Offset(0f, 4.4f)); line(Color(0xFF4A2E06), 0f, 4.6f, 0f, 6f, 0.45f); line(Color.White.copy(alpha = 0.55f), -1.8f, 3.4f, -1.8f, 6.6f, 0.3f)
                } }
            }
        }
        "cofre" -> {
            // cofre de ferro nas costas (lendário): a caixa pesada aparece por cima e pelo lado direito do livro, com
            // chanfro, rebites e filete dourado na porta; a roda de segredo cromada gira para lá e para cá, o volante
            // de três braços gira devagar, e na festa moedas de ouro pulam lá de dentro
            val X = 58f; val Y = 12f; val W = 48f; val H = 50f
            rotate((0.07f + sin(t * 1.1f) * 0.012f - air * 0.03f) * DEG, pivot = Offset(82f, 40f)) {
                if (party && !small) for (i in 0 until 3) {
                    val ph = (t * 0.8f + i / 3f) % 1f; val cx = X + 14f + i * 10f + ph * (i - 1) * 6f; val cy = Y + 2f - sin(PIf * ph) * 13f; val rx = 0.4f + 2f * abs(cos(t * 6f + i))
                    val coin = ellipsePath(cx, cy, rx, 2.2f); drawPath(coin, vg(GOLD3, cy - 2.2f, cy + 2.2f)); drawPath(coin, Color(0xB3785008), style = Stroke(0.3f))
                }
                drawPath(roundRect(X + 1.2f, Y + 1.4f, W, H, 3f), Color(0x4D0A0C14))
                val box = roundRect(X, Y, W, H, 3f)
                drawPath(box, hg(listOf(Color(0xFF1C1F26), Color(0xFF4A505E), Color(0xFF6A7182), Color(0xFF3A3F4A), Color(0xFF16181E)), X, X + W))
                clipPath(box) { drawRect(vg(listOf(Color(0xFF9AA2B4), Color(0xFF4A505E)), Y, Y + 3.4f), Offset(X, Y), Size(W, 3.4f)) }
                drawPath(box, Color(0xBF05060A), style = stroke(if (small) 1.1f else 0.6f))
                val door = roundRect(X + 4f, Y + 5f, W - 8f, H - 9f, 2f)
                drawPath(door, vg(listOf(Color(0xFF5A6172), Color(0xFF3A3F4C), Color(0xFF2A2E38)), Y + 5f, Y + H - 4f))
                drawPath(door, Color(0xFFC98A12), style = stroke(if (small) 0.8f else 0.5f)); if (!small) drawPath(door, Color(0x59FFF1BF), style = stroke(0.2f))
                for ((rx, ry) in listOf(X + 2f to Y + 2f, X + W - 2f to Y + 2f, X + 2f to Y + H - 2f, X + W - 2f to Y + H - 2f, X + W / 2f to Y + 2f, X + 2f to Y + H / 2f, X + W - 2f to Y + H / 2f)) {
                    drawCircle(Color(0xFF16181E), 0.8f, Offset(rx, ry)); drawCircle(Color(0xFFA9B0C0), 0.4f, Offset(rx - 0.2f, ry - 0.2f))
                }
                // roda de segredo
                val dx = X + 20f; val dy = Y + 17f; val r = 6.2f; val spin = sin(t * 0.9f) * 1.6f + (if (party) t * 3f else 0f)
                drawCircle(Color(0x6605060A), r + 0.4f, Offset(dx + 0.5f, dy + 0.6f))
                drawCircle(vg(listOf(Color.White, Color(0xFFC3C9D6), Color(0xFF6E7588)), dy - r, dy + r), r, Offset(dx, dy))
                drawCircle(Color(0xFF1E2028), r - 1.1f, Offset(dx, dy))
                translate(dx, dy) { rotate(spin * DEG, pivot = Offset.Zero) {
                    for (i in 0 until 24) { val a = i * PIf / 12f; val r0 = if (i % 6 != 0) r - 1.9f else r - 2.6f; line(Color(0xFFE3E6F0), cos(a) * r0, sin(a) * r0, cos(a) * (r - 1.3f), sin(a) * (r - 1.3f), if (i % 6 != 0) 0.22f else 0.4f) }
                    drawCircle(vg(listOf(Color(0xFFF3F6FA), Color(0xFF9AA3B4), Color(0xFF5E6578)), -2.6f, 2.6f), 2.6f, Offset.Zero)
                    line(Color(0xFF3C4054), 0f, -2.2f, 0f, -0.6f, 0.5f); drawCircle(Color.White.copy(alpha = 0.7f), 0.6f, Offset(-0.9f, -0.9f))
                } }
                val pt = Path().apply { moveTo(dx - 1f, dy - r - 1.3f); lineTo(dx + 1f, dy - r - 1.3f); lineTo(dx, dy - r + 0.4f); close() }
                drawPath(pt, Gold); drawPath(pt, Color(0xB36E4600), style = stroke(0.2f))
                // volante de três braços
                val wx = X + W - 11f; val wy = Y + 30f; val wa = t * 0.6f + (if (party) t * 2f else 0f)
                for (i in 0 until 3) {
                    val a = wa + i * 2f * PIf / 3f; val ex = wx + cos(a) * 5f; val ey = wy + sin(a) * 5f
                    line(Color(0xFF2A2E38), wx, wy, ex, ey, 1.3f); line(Color(0xFFC3C9D6), wx, wy, ex, ey, 0.7f); drawCircle(vg(listOf(Color.White, Color(0xFF8E95A6)), ey - 1.1f, ey + 1.1f), 1.1f, Offset(ex, ey))
                }
                drawCircle(vg(listOf(Color(0xFFF3F6FA), Color(0xFF6E7588)), wy - 1.7f, wy + 1.7f), 1.7f, Offset(wx, wy))
                if (!small) {
                    val k = maxOf(0f, sin(t * 1.2f)).pow(8); if (k > 0.02f) star4(dx + 4f, dy - 4.4f, 3f * k, Color.White.copy(alpha = k))
                    val k2 = maxOf(0f, sin(t * 1.2f + 2.5f)).pow(8); if (k2 > 0.02f) star4(X + W - 3f, Y + 3f, 2.6f * k2, Gold.copy(alpha = k2))
                }
            }
        }
        "cupulas" -> {
            // Congresso atrás do Folha (lendário, igual ao site): tudo fica acima do livro. As duas torres gêmeas no
            // centro, bem altas, com as janelinhas que acendem e a passarela; a cúpula do Senado (convexa) à esquerda
            // e a tigela da Câmara (côncava) à direita, saindo para fora das laterais; a laje branca com o espelho
            // d'água e um brilho de fim de tarde redondo e suave atrás
            if (!small) drawCircle(Brush.radialGradient(0f to Color(0x80FFD68C), 0.6f to Color(0x2E96BEFF), 1f to Color(0x0096BEFF), center = Offset(50f, 18f), radius = 38f), 38f, Offset(50f, 18f))
            val con = listOf(Color.White, Color(0xFFE6E9F2), Color(0xFFB9C0D2)); val ed = Color(0x99323C5F); val ew = if (small) 0.9f else 0.45f
            for (x in listOf(42.8f, 51.6f)) {
                drawRect(hg(listOf(Color(0xFFC9CFDD), Color.White, Color(0xFFAEB6C8)), x, x + 5.6f), Offset(x, -16f), Size(5.6f, 56f)); drawRect(ed, Offset(x, -16f), Size(5.6f, 56f), style = Stroke(ew))
                if (!small) for (k in 0 until 17) { val lit = sin(t * 0.7f + k * 1.7f + x) > 0.6f; drawRect(if (lit) Color(0xF2FFD66E) else Color(0x80465A8C), Offset(x + 0.9f, -14.4f + k * 3f), Size(3.8f, 1.2f)) }
            }
            drawRect(vg(con, -3f, 2f), Offset(48.4f, -3f), Size(3.2f, 5f)); drawRect(ed, Offset(48.4f, -3f), Size(3.2f, 5f), style = Stroke(ew))
            val dm = Path().apply { moveTo(-17f, 38f); cubicTo(-17f, 19f, 19f, 19f, 19f, 38f); close() }
            drawPath(dm, vg(con, 24f, 38f)); drawPath(dm, ed, style = stroke(ew))
            if (!small) drawPath(Path().apply { moveTo(-12f, 35f); cubicTo(-11f, 28f, -3f, 25.2f, 3f, 25.6f) }, Color.White.copy(alpha = 0.95f), style = stroke(0.8f))
            val ped = Path().apply { moveTo(96f, 38f); lineTo(98f, 32f); lineTo(104f, 32f); lineTo(106f, 38f); close() }
            drawPath(ped, vg(con, 32f, 38f)); drawPath(ped, ed, style = stroke(ew))
            val bw = Path().apply { moveTo(83f, 21f); quadraticTo(101f, 19f, 119f, 21f); cubicTo(117f, 34f, 85f, 34f, 83f, 21f); close() }
            drawPath(bw, vg(con, 21f, 32f)); drawPath(bw, ed, style = stroke(ew))
            val rim = ellipsePath(101f, 20.8f, 17.6f, 2f); drawPath(rim, vg(listOf(Color(0xFF9AA3B8), Color(0xFFD7DCE8)), 18.8f, 22.8f)); drawPath(rim, ed, style = Stroke(ew))
            val slab = roundRect(-20f, 38f, 140f, 4f, 0.8f); drawPath(slab, vg(con, 38f, 42f)); drawPath(slab, ed, style = stroke(ew))
            if (!small) {
                for (i in 0 until 6) { val x = -20f + ((t * 4f + i * 23f) % 140f); line(Color(0xCC6EAAF0), x, 40f, x + 3f, 40f, 0.35f) }
                for ((i, x) in listOf(0 to 45.6f, 1 to 54.4f)) { val k = maxOf(0f, sin(t * 1.1f + i * 2f)).pow(8); if (k > 0.02f) star4(x, -16.5f, 2.6f * k, Color.White.copy(alpha = k)) }
            }
        }
        "mochila-escolar" -> mirrored(1f) {
            // mochila escolar colorida no ombro direito: corpo turquesa, tampa rosa com bolinhas, bolso amarelo com
            // zíper e três chaveirinhos pendurados (coração, pompom e estrelinha) que balançam, mais na festa
            val sw = sin(t * (if (party) 7f else 1.6f)) * (if (party) 0.3f else 0.12f) + air * 0.2f
            rotate((-0.08f + sin(t * 1.2f + 2f) * 0.02f - air * 0.06f) * DEG, pivot = Offset(2f, 60f)) {
                val body = Path().apply { moveTo(-9f, 50f); quadraticTo(-9.6f, 43f, -2f, 42.4f); lineTo(10f, 42.4f); quadraticTo(15.6f, 43f, 15.4f, 50f); lineTo(15f, 80f); quadraticTo(15f, 84f, 10.6f, 84f); lineTo(-4.6f, 84f); quadraticTo(-9f, 84f, -9f, 80f); close() }
                drawPath(body, hg(listOf(Color(0xFF16807F), Color(0xFF4FD6D0), Color(0xFF2BB5B0), Color(0xFF11706F)), -9f, 15.4f))
                drawPath(body, Color(0x99053232), style = stroke(if (small) 1f else 0.5f))
                val flap = Path().apply { moveTo(-8.4f, 50f); quadraticTo(-8.6f, 44f, -2f, 43.6f); lineTo(10f, 43.6f); quadraticTo(14.8f, 44f, 14.8f, 50f); quadraticTo(3f, 56f, -8.4f, 50f); close() }
                drawPath(flap, vg(listOf(Color(0xFFFF9EC4), Color(0xFFE85A92)), 43.6f, 54f))
                if (!small) clipPath(flap) { for ((x, y) in listOf(-6f to 46.4f, -2.4f to 48.8f, -5f to 50.2f, 1.4f to 45.8f, 5f to 48.6f, 8.6f to 46f, 11.6f to 49f)) drawCircle(Color.White.copy(alpha = 0.75f), 0.6f, Offset(x, y)) }
                drawPath(flap, Color(0x80781440), style = stroke(0.35f))
                val pk = roundRect(-6.4f, 63f, 18.8f, 16f, 3f); drawPath(pk, vg(listOf(Color(0xFFFFE45C), Color(0xFFF5B83D)), 63f, 79f)); drawPath(pk, Color(0x8078500A), style = stroke(0.35f))
                line(Color(0xFFC9CCE0), -4.6f, 66.2f, 10.6f, 66.2f, 0.55f)
                if (!small) {
                    line(Color.White.copy(alpha = 0.35f), -7.6f, 52f, -7.8f, 80f, 0.6f)
                    fun DrawScope.charm(x: Float, i: Int, draw: DrawScope.() -> Unit) = translate(x, 66.4f) { rotate((sw * (1f + i * 0.25f) + (i - 1) * 0.08f) * DEG, pivot = Offset.Zero) { line(Color(0xFFB9BCD0), 0f, 0f, 0f, 2.6f, 0.3f); draw() } }
                    charm(-5.4f, 0) { drawPath(heart(0f, 4f, 1.6f), vg(listOf(Color(0xFFFF8FA3), Color(0xFFE0475E)), 2.6f, 5.6f)) }
                    charm(-2.8f, 1) {
                        for (k in 0 until 10) { val a = k * PIf / 5f; drawCircle(Color(0xFF9A6CF0), 0.7f, Offset(cos(a) * 1.3f, 4.6f + sin(a) * 1.3f)) }
                        drawCircle(vg(listOf(Color(0xFFD9C6FF), Color(0xFF8A63E0)), 3f, 6.2f), 1.4f, Offset(0f, 4.6f))
                    }
                    charm(-0.2f, 2) { val st = star5(0f, 4.4f, 1.8f); drawPath(st, vg(GOLD3, 2.6f, 6.2f)); drawPath(st, Color(0x8C785008), style = stroke(0.2f)) }
                }
                val sp = Path().apply { moveTo(-2f, 43f); cubicTo(2f, 36f, 10f, 35f, 16f, 40f) }
                drawPath(sp, Color(0xFFA8235E), style = stroke(if (small) 3.4f else 2.8f)); drawPath(sp, Color(0xFFFF6FA8), style = stroke(if (small) 2.2f else 1.8f))
            }
        }
        "pastas" -> {
            // torre de pastas (lendário): uma pilha alta de pastas coloridas atrás do ombro esquerdo, cada uma com a
            // aba, a etiqueta na lombada e as folhas aparecendo; a torre balança como gelatina (mais no pulo e na
            // festa) e lá no topo fica a pasta dourada com a estrela, que brilha
            val n = if (small) 7 else 10; val amp = (if (party) 2.2f else 1f) + air * 2f
            val cols = listOf(0xFFFF8FA3 to 0xFFE0475E, 0xFFFFE58A to 0xFFF5B83D, 0xFF8EC5FF to 0xFF3F7FDC, 0xFF9FF0C8 to 0xFF3FBF8F, 0xFFCDB6FF to 0xFF8A63E0, 0xFFFFC28A to 0xFFF08A2E)
            for (i in 0 until n) {
                val k = i / (n - 1f); val wob = sin(t * 1.5f - i * 0.35f) * amp
                val x = 24f + sin(i * 2.3f) * 1.6f + wob * k * k * 3f; val y = 44f - i * (if (small) 7f else 5.2f) - 2.6f; val a = sin(i * 1.7f) * 0.05f + wob * k * 0.035f
                val top = i == n - 1
                val c1 = if (top) Color(0xFFFFF1BF) else Color(cols[i % cols.size].first); val c2 = if (top) Color(0xFFC98A12) else Color(cols[i % cols.size].second)
                translate(x, y) { rotate(a * DEG, pivot = Offset.Zero) {
                    val tab = roundRect(6f, -3.6f, 5f, 1.8f, 0.5f); drawPath(tab, c1); drawPath(tab, Color(0x66191E3C), style = stroke(0.25f))
                    drawRect(Color(0xFFFFFDF4), Offset(-11.4f, -3.1f), Size(16f, 1.2f))
                    val f = roundRect(-12.5f, -2.4f, 25f, 4.8f, 0.9f); drawPath(f, vg(listOf(c1, c2), -2.4f, 2.4f)); drawPath(f, Color(0x731E193C), style = stroke(if (small) 0.7f else 0.3f))
                    if (!small) {
                        drawRect(Color.White.copy(alpha = 0.85f), Offset(-10.8f, -1.2f), Size(6f, 2.4f))
                        line(Color(0x803C3C6E), -10f, -0.3f, -6.4f, -0.3f, 0.3f); line(Color(0x803C3C6E), -10f, 0.6f, -7.4f, 0.6f, 0.3f); line(Color.White.copy(alpha = 0.35f), -3f, -1.7f, 10.6f, -1.7f, 0.4f)
                    }
                    if (top) {
                        val st = star5(3f, 0f, 1.9f); drawPath(st, Color(0xFFFFFDF4)); drawPath(st, Color(0x99785008), style = stroke(0.2f))
                        if (!small) { val kk = maxOf(0f, sin(t * 1.4f)).pow(6); if (kk > 0.02f) star4(9f, -4.4f, 2.8f * kk, Color.White.copy(alpha = kk)) }
                    }
                } }
            }
        }
        "borboleta" -> {
            // asas de borboleta: duas asas de cada lado (a de cima grande, a de baixo menor) presas nos ombros, em
            // degradê amarelo, laranja, rosa e roxo, com borda escura de pintinhas brancas, nervuras e um "olho"
            // azul; batem devagar (mais rápido no pulo e na festa)
            val busy = party || air > 0.1f; val beat = 0.72f + 0.28f * cos(t * (if (busy) 6f else 1.7f))
            for (side in listOf(-1f, 1f)) mirrored(side) {
                translate(24f, 52f) { scale(beat, 1f, pivot = Offset.Zero) {
                    val up = Path().apply { moveTo(0f, -1f); cubicTo(-6f, -24f, -34f, -38f, -44f, -26f); cubicTo(-50f, -14f, -34f, -2f, 0f, 2f); close() }
                    val lo = Path().apply { moveTo(0f, 2f); cubicTo(-30f, 2f, -40f, 22f, -30f, 32f); cubicTo(-20f, 39f, -6f, 24f, 0f, 4f); close() }
                    fun wing(r: Float) = Brush.radialGradient(0f to Color(0xFFFFE680), 0.35f to Color(0xFFFF9F3A), 0.7f to Color(0xFFF0477A), 1f to Color(0xFF7B3FD0), center = Offset.Zero, radius = r)
                    drawPath(lo, wing(40f)); drawPath(up, wing(50f))
                    if (!small) {
                        for ((x, y) in listOf(-40f to -24f, -30f to -30f, -42f to -14f, -30f to -4f)) line(Color(0x59461440), 0f, 0f, x, y, 0.4f)
                        for ((x, y) in listOf(-28f to 26f, -34f to 14f, -14f to 30f)) line(Color(0x59461440), 0f, 3f, x, y, 0.4f)
                    }
                    drawCircle(Color(0xFF2A1840), 3.4f, Offset(-30f, -18f)); drawCircle(Color(0xFF3FA8FF), 2.4f, Offset(-30f, -18f)); drawCircle(Color.White, 0.9f, Offset(-30.6f, -18.6f))
                    drawPath(up, Color(0xFF2A1840), style = stroke(if (small) 2f else 1.5f)); drawPath(lo, Color(0xFF2A1840), style = stroke(if (small) 2f else 1.5f))
                    if (!small) for ((x, y) in listOf(-43f to -26f, -39.5f to -30.5f, -45.6f to -18f, -38f to -7.4f, -33.6f to 29.6f, -37.4f to 21f, -24f to 35f)) drawCircle(Color.White, 0.55f, Offset(x, y))
                } }
            }
        }
        "heroi" -> {
            // capa de herói (lendário): vermelha, comprida e esvoaçante, puxada pelo vento para o lado, com o forro
            // dourado aparecendo na barra, a gola alta atrás dos ombros e os dois fechos de estrela; no pulo e na
            // festa ela se abre mais e aparecem as riscas de vento
            val fl = air * 8f + (if (party) 4f else 0f); val wave = sin(t * 1.6f); val wind = 4f + sin(t * 0.9f) * 2.5f + fl * 0.8f
            val L = -8f - fl + wave * 1.6f; val R = 108f + fl - wave * 1.6f
            val hem = (1..7).map { i -> val u0 = (i - 0.5f) / 7f; val u1 = i / 7f; floatArrayOf(L + (R - L) * u0 + wind * u0, 104f + sin(t * 2.6f + i * 1.2f) * 2.4f + fl * 0.3f, L + (R - L) * u1 + wind * u1, 100f + sin(t * 2.2f + i * 0.9f) * 1.2f) }
            fun capeP(dy: Float) = Path().apply { moveTo(16f, 44f); cubicTo(6f, 60f, L + 2f, 82f, L, 100f + dy); hem.forEach { quadraticTo(it[0], it[1] + dy, it[2], it[3] + dy) }; cubicTo(R + wind - 2f, 82f, 95f, 60f, 84f, 44f); close() }
            val lining = capeP(2.2f)
            drawPath(lining, hg(listOf(Color(0xFFB57A0C), Color(0xFFFFD23F), Color(0xFFF5B83D), Color(0xFF9C6A12)), L, R + wind)); drawPath(lining, Color(0x805A3200), style = stroke(if (small) 0.9f else 0.4f))
            val cape = capeP(0f)
            drawPath(cape, hg(listOf(Color(0xFF7A0A1A), Color(0xFFD61F2E), Color(0xFFFF4B4B), Color(0xFFD61F2E), Color(0xFF7A0A1A)), L, R + wind))
            if (!small) clipPath(cape) {
                for (i in 0 until 7) { val x = L + 6f + (R - L - 12f) * i / 6f + wind * (i / 6f) + sin(t * 1.6f + i) * 1.4f; drawRect(hg(listOf(Color(0x0046000A), Color(0x4D46000A), Color(0x0046000A)), x - 3f, x + 3f), Offset(x - 3f, 44f), Size(6f, 64f)) }
                val sx = -40f + ((t * 0.28f) % 1f) * 190f
                drawRect(Brush.linearGradient(listOf(Color(0x00FFFFFF), Color(0x38FFD2D2), Color(0x00FFFFFF)), start = Offset(sx, 40f), end = Offset(sx + 16f, 104f)), Offset(-20f, 40f), Size(150f, 70f))
            }
            drawPath(cape, Color(0x993C000A), style = stroke(if (small) 1f else 0.5f))
            for (side in listOf(-1f, 1f)) mirrored(side) {
                val col = Path().apply { moveTo(11f, 45f); lineTo(4f, 29f - fl * 0.2f); quadraticTo(15f, 33f, 23f, 43f); close() }
                drawPath(col, vg(listOf(Color(0xFFFFE08A), Color(0xFFE9A21A)), 29f, 45f))
                drawPath(Path().apply { moveTo(11f, 45f); lineTo(4f, 29f - fl * 0.2f); quadraticTo(7f, 38f, 16f, 44.6f); close() }, vg(listOf(Color(0xFFFF4B4B), Color(0xFFA8142A)), 29f, 45f))
                drawPath(col, Color(0x8C3C000A), style = stroke(if (small) 0.9f else 0.4f))
                drawCircle(Color(0x593C1E00), 2.4f, Offset(14.3f, 41.6f)); drawCircle(vg(GOLD3, 39f, 43.4f), 2.2f, Offset(14f, 41.2f)); drawPath(star5(14f, 41.3f, 1.3f), Color(0xFFFFFDF4))
            }
            if ((party || air > 0.1f) && !small) for (i in 0 until 4) {
                val ph = (t * 1.8f + i / 4f) % 1f; val y = 58f + i * 9f; val c = Color.White.copy(alpha = 0.55f * sin(PIf * ph))
                line(c, -6f - ph * 14f, y, -1f - ph * 14f, y, 0.6f); line(c, 106f + ph * 14f, y + 4f, 111f + ph * 14f, y + 4f, 0.6f)
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
    drawRibbon(t, mood, look.ribbon, look.ribbonPat)
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

private fun DrawScope.drawRibbon(t: Float, mood: FolhaMood, colors: List<Color>, pat: String = "") {
    val dark = colors[0]; val base = colors[1]; val light = colors[2]
    val sad = mood == FolhaMood.SAD; val sway = if (sad) 0f else sin(t * 1.9f + 0.6f) * 1.3f
    val w = 3.4f; val x0 = 51.4f; val lieTop = 86f; val edge = 94.6f; val hang = if (sad) 3.2f else 5.4f
    val tipY = edge + hang; val tipX = x0 + 0.5f + sway
    if (pat == "cabo" || pat == "clipes") { drawRibbonObject(pat, colors, x0 + w / 2f, lieTop, edge, tipX + w / 2f, tipY, sway); return }
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
    if (pat.isNotEmpty()) clipPath(r) { drawRibbonPattern(t, pat, x0, lieTop, tipY, w, sway) }
    drawRect(Brush.verticalGradient(listOf(Color(0x730E0A45), Color(0x000E0A45)), startY = 88f, endY = 91.5f), Offset(x0, 88f), Size(w + 0.3f, 3.5f))
    line(Color.White.copy(alpha = 0.55f), x0 + 0.5f, edge - 0.4f, x0 + w, edge - 0.4f, 0.5f, false)
    drawRect(Brush.verticalGradient(listOf(dark.copy(alpha = 0.55f), dark.copy(alpha = 0f)), startY = edge, endY = edge + 1.8f), Offset(x0 + 0.3f, edge), Size(w, 1.8f))
}

/** Estampa por cima da fita (já recortada no formato dela). */
private fun DrawScope.drawRibbonPattern(t: Float, pat: String, x0: Float, y0: Float, y1: Float, w: Float, sway: Float) {
    when (pat) {
        "camo" -> for (q in listOf(floatArrayOf(0.8f, 88f, 1.3f, 0f), floatArrayOf(2.6f, 91f, 1.1f, 1f), floatArrayOf(1.2f, 94f, 1.2f, 0f), floatArrayOf(2.8f, 96.6f, 1f, 2f), floatArrayOf(1.4f, 98.6f, 0.9f, 1f))) {
            // manchas de camuflado em três tons
            val c = listOf(Color(0x99262E12), Color(0x8C6E5630), Color(0x8CB2BA78))[q[3].toInt()]
            val x = x0 + q[0] + sway * (q[1] - y0) / (y1 - y0)
            rotate(0.5f * 180f / PIf, pivot = Offset(x, q[1])) { oval(c, x - q[2] * 1.2f, q[1] - q[2] * 0.8f, q[2] * 2.4f, q[2] * 1.6f) }
        }
        "zebra" -> {
            // fita zebrada de isolamento: listras pretas em diagonal
            var y = y0 - 4f
            while (y < y1 + 2f) { drawPath(Path().apply { moveTo(x0 - 1f, y); lineTo(x0 + w + 3f, y + 3f); lineTo(x0 + w + 3f, y + 4f); lineTo(x0 - 1f, y + 1f); close() }, Color(0xE618140A)); y += 2.2f }
        }
        "cedula" -> {
            // verde-cédula: guilhoché (ondinhas finas) e o filete claro no meio, como nota de dinheiro
            for (k in 0 until 3) {
                val p = Path(); var y = y0; var first = true
                while (y <= y1) { val x = x0 + 0.7f + k + sin(y * 2.2f + k) * 0.35f + sway * (y - y0) / (y1 - y0); if (first) p.moveTo(x, y) else p.lineTo(x, y); first = false; y += 0.5f }
                drawPath(p, Color(0x73E6FFEB), style = stroke(0.18f))
            }
            line(Color.White.copy(alpha = 0.35f), x0 + w / 2f, y0, x0 + w / 2f + sway, y1, 0.3f, false)
        }
        "brilho" -> {
            // ouro velho: um brilho fosco que desce pela fita de vez em quando
            val sy = y0 - 6f + ((t * 0.3f) % 1f) * 22f
            drawRect(Brush.verticalGradient(listOf(Color(0x00FFF0BE), Color(0x8CFFF0BE), Color(0x00FFF0BE)), startY = sy, endY = sy + 4f), Offset(x0 - 2f, sy), Size(w + 6f, 4f))
        }
        "giz" -> {
            // giz colorido: pó de giz pontilhado e riscos claros
            for (i in 0 until 16) drawCircle(Color.White.copy(alpha = 0.55f), 0.18f + (i % 3) * 0.08f, Offset(x0 + 0.4f + (i * 1.7f) % w, y0 + 1f + (i * 2.9f) % (y1 - y0 - 2f)))
            for (y in listOf(89f, 93.5f, 97f)) line(Color.White.copy(alpha = 0.4f), x0 + 0.6f, y, x0 + w - 0.4f, y - 0.5f, 0.25f)
        }
    }
}

/** Fitas que viram objeto: o cabo de rede (com plugue na ponta) e a corrente de clipes. */
private fun DrawScope.drawRibbonObject(pat: String, cs: List<Color>, xa: Float, ya: Float, yb: Float, xt: Float, yt: Float, sway: Float) {
    if (pat == "cabo") {
        val path = Path().apply { moveTo(xa, ya); lineTo(xa + 0.15f, yb); quadraticTo(xa + 0.15f + sway * 0.3f, yb + (yt - yb) * 0.5f, xt, yt - 2.6f) }
        translate(0.6f, 0.5f) { drawPath(path, Color(0x590E0A45), style = stroke(1.9f)) }
        drawPath(path, Color(0xFF123C80), style = stroke(1.9f)); drawPath(path, cs[1], style = stroke(1.4f)); drawPath(path, cs[2].copy(alpha = 0.7f), style = stroke(0.35f))
        // plugue RJ45: corpo transparente com os contatos dourados e a trava
        translate(xt, yt - 1.6f) {
            rotate(sway * 0.06f * 180f / PIf, pivot = Offset.Zero) {
                val pl = roundRect(-1.4f, -1.4f, 2.8f, 3.2f, 0.5f)
                drawPath(pl, vg(listOf(Color(0xF2F0F8FF), Color(0xF2BED2EB)), -1.4f, 1.8f)); drawPath(pl, Color(0xFF5A6E90), style = stroke(0.3f))
                for (i in 0 until 4) line(Gold, -0.9f + i * 0.6f, 0.6f, -0.9f + i * 0.6f, 1.6f, 0.25f, false)
                drawPath(Path().apply { moveTo(-0.6f, -1.4f); lineTo(-0.4f, -2.2f); lineTo(0.4f, -2.2f); lineTo(0.6f, -1.4f) }, Color(0xFF5A6E90), style = stroke(0.3f))
            }
        }
        return
    }
    // corrente de clipes: um clipe atrás do outro ao longo da fita, girando de leve
    val pts = listOf(Offset(xa, ya + 1.2f), Offset(xa + 0.1f, ya + 4f), Offset(xa + 0.15f, yb - 0.6f), Offset(xa + 0.15f + sway * 0.25f, yb + (yt - yb) * 0.45f), Offset(xt, yt - 1.6f))
    pts.forEachIndexed { i, p ->
        translate(p.x, p.y) {
            rotate(((if (i % 2 == 1) 0.18f else -0.18f) + sway * 0.04f) * 180f / PIf, pivot = Offset.Zero) {
                val c = Path().apply {
                    moveTo(-0.5f, 1.2f); lineTo(-0.5f, -1f); arcTo(Rect(-0.5f, -1.5f, 0.5f, -0.5f), 180f, 180f, false); lineTo(0.5f, 1.4f)
                    arcTo(Rect(-0.4f, 0.95f, 0.5f, 1.85f), 0f, 180f, false); lineTo(-0.4f, -0.6f); arcTo(Rect(-0.4f, -1f, 0.4f, -0.2f), 180f, 180f, false); lineTo(0.4f, 0.8f)
                }
                drawPath(c, Color(0x8C0E0A45), style = stroke(0.55f)); drawPath(c, if (i % 3 == 1) Color(0xFFF06A85) else cs[1], style = stroke(0.35f)); drawPath(c, Color.White.copy(alpha = 0.7f), style = stroke(0.1f))
            }
        }
    }
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
    val x = if (sad) -3f else -6f; val base = 95f; val k = 1.45f   // triste: chega pertinho para consolar
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
        // olhando para o Folha (à direita e um pouco para cima)
        drawCircle(Ink, r, Offset(ex + r * 0.28f, ey - r * 0.15f)); drawCircle(Color.White, r * 0.4f, Offset(ex - r * 0.02f, ey - r * 0.5f))
    }
    fun DrawScope.smile(mx: Float, my: Float, w: Float = 1.1f) = drawPath(Path().apply { moveTo(mx - w, my); quadraticTo(mx, my + w * (if (party) 1.2f else 0.7f), mx + w, my) }, Ink, style = stroke(0.45f))
    // elipses pelo centro (cor, gradiente, contorno) e o contorno de tinta suave dos bichinhos novos
    fun DrawScope.ell(c: Color, cx: Float, cy: Float, rx: Float, ry: Float) = drawOval(c, Offset(cx - rx, cy - ry), Size(2f * rx, 2f * ry))
    fun DrawScope.ellB(b: Brush, cx: Float, cy: Float, rx: Float, ry: Float) = drawOval(b, Offset(cx - rx, cy - ry), Size(2f * rx, 2f * ry))
    fun DrawScope.ellLine(c: Color, cx: Float, cy: Float, rx: Float, ry: Float, w: Float) = drawOval(c, Offset(cx - rx, cy - ry), Size(2f * rx, 2f * ry), style = stroke(w))
    fun ellPath(cx: Float, cy: Float, rx: Float, ry: Float) = Path().apply { addOval(Rect(cx - rx, cy - ry, cx + rx, cy + ry)) }
    val Out = Color(0x5926215C)
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
                    for (s in listOf(-1f, 1f)) { if (blink) line(Mint, s * 1.8f - 0.8f, -13.4f, s * 1.8f + 0.8f, -13.4f, 0.5f) else if (party) drawPath(heart(s * 1.8f + 0.3f, -13.4f, 1f), Color(0xFFFF8FA3)) else drawPath(roundRect(s * 1.8f - 0.4f, -14.8f, 1.4f, 2.2f, 0.6f), Mint) }
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
                    // dragãozinho bebê, virado para o Folha: corpo gordinho com a barriga de placas, espinhos dourados
                    // nas costas, asas de morcego com nervuras que batem (rápido na festa), rabo enrolado com a ponta de
                    // espada, focinho com narinas, chifrinhos e o dentinho no sorriso; na festa ele solta fogo
                    val flap = sin(t * (if (party) 10f else 2.6f)) * (if (party) 0.45f else 0.22f); val sw = sin(t * 1.8f) * 0.6f; val ol = Color(0x8C0E563E)
                    for (s in listOf(-1f, 1f)) translate(-1.2f + s * 0.6f, -9.6f) {
                        rotate((-0.35f - s * 0.25f + flap * (if (s < 0f) 1f else 0.7f)) * DEG, pivot = Offset.Zero) {
                            scale(if (s > 0f) 0.82f else 1f, if (s > 0f) 0.82f else 1f, pivot = Offset.Zero) {
                                val w = Path().apply { moveTo(0f, 0f); quadraticTo(-4f, -7.5f, -9.4f, -6.6f); quadraticTo(-8.4f, -4.2f, -9f, -2.4f); quadraticTo(-7f, -2.8f, -6.6f, -0.6f); quadraticTo(-4.6f, -1.4f, -3.8f, 1.4f); quadraticTo(-2f, 0.6f, 0f, 1.6f); close() }
                                drawPath(w, vg(if (s < 0f) listOf(Color(0xFFB995F5), Color(0xFF7B4FE0)) else listOf(Color(0xFF9E7AE8), Color(0xFF6A3FCC)), -7f, 2f)); drawPath(w, Color(0x8C3C1E82), style = stroke(0.35f))
                                for ((x, y) in listOf(-9.4f to -6.6f, -9f to -2.4f, -3.8f to 1.4f)) line(Color(0xB3462396), 0f, 0f, x * 0.92f, y * 0.92f, 0.35f)
                            }
                        }
                    }
                    val tail = Path().apply { moveTo(-3f, -2.2f); cubicTo(-9f, -1f, -11.4f, -4.6f, -9.6f + sw * 0.3f, -8.2f); quadraticTo(-8.6f, -9.8f, -7.4f + sw * 0.4f, -8.8f) }
                    drawPath(tail, Color(0xFF1A8A66), style = stroke(2.4f)); drawPath(tail, Color(0xFF3CC795), style = stroke(1.3f))
                    val tx = -7.4f + sw * 0.4f; val ty = -8.8f
                    drawPath(Path().apply { moveTo(tx - 0.6f, ty + 0.4f); lineTo(tx + 1.4f, ty - 2.2f); lineTo(tx + 1.6f, ty + 0.9f); close() }, Color(0xFF9563FF))
                    for (fx in listOf(-2.2f, 2.4f)) { oval(Color(0xFF1F9E75), fx - 2f, -1.7f, 4f, 2.2f); for (c in listOf(-0.8f, 0f, 0.8f)) drawCircle(Color(0xFFF6FFF9), 0.28f, Offset(fx + 0.8f + c, -0.2f)) }
                    val body = Path().apply { moveTo(0f, -11f); cubicTo(4.6f, -11f, 5.6f, -4.6f, 4.6f, -1.6f); quadraticTo(3.4f, 0.2f, 0f, 0.2f); quadraticTo(-3.6f, 0.2f, -4.8f, -1.8f); cubicTo(-5.6f, -5f, -4.4f, -11f, 0f, -11f); close() }
                    drawPath(body, Brush.radialGradient(0f to Color(0xFF6FE6B8), 0.6f to Color(0xFF36C897), 1f to Color(0xFF1A8A66), center = Offset(-1.4f, -7.4f), radius = 7f)); drawPath(body, ol, style = stroke(0.35f))
                    val belly = Path().apply { addOval(Rect(1.2f - 2.7f, -4.4f - 3.7f, 1.2f + 2.7f, -4.4f + 3.7f)) }
                    drawPath(belly, vg(listOf(Color(0xFFFFF6D6), Color(0xFFF3E3A8)), -8f, -0.8f))
                    clipPath(belly) { for (i in 0 until 4) drawPath(Path().apply { moveTo(-1.6f, -6.8f + i * 1.6f); quadraticTo(1.2f, -5.8f + i * 1.6f, 4f, -6.8f + i * 1.6f) }, Color(0x8CBE963C), style = stroke(0.28f)) }
                    for (q in listOf(floatArrayOf(-4.2f, -8.6f, -1.9f), floatArrayOf(-4.9f, -6f, -1.75f), floatArrayOf(-5f, -3.4f, -1.6f))) translate(q[0], q[1]) {
                        rotate(q[2] * DEG, pivot = Offset.Zero) { drawPath(Path().apply { moveTo(-0.9f, 0f); lineTo(0f, -1.8f); lineTo(0.9f, 0f); close() }, Color(0xFFF5B83D)) }
                    }
                    val hx = 2.6f; val hy = -14f
                    for ((x, a) in listOf((hx - 2.2f) to -0.5f, (hx + 0.6f) to -0.15f)) translate(x, hy - 3.6f) {
                        rotate(a * DEG, pivot = Offset.Zero) { drawPath(Path().apply { moveTo(-0.8f, 0.6f); quadraticTo(-0.7f, -1.6f, 0.6f, -2.8f); quadraticTo(0.4f, -1f, 0.9f, 0.6f); close() }, vg(listOf(Color(0xFFFFE59A), Color(0xFFE9A82B)), -2.8f, 0.6f)) }
                    }
                    drawPath(Path().apply { moveTo(hx - 3.6f, hy - 0.6f); lineTo(hx - 6f, hy - 2.2f); lineTo(hx - 3.8f, hy + 1.2f); close() }, Color(0xFF9563FF))
                    val head = Path().apply { addOval(Rect(hx - 4.4f, hy - 4f, hx + 4.4f, hy + 4f)) }
                    drawPath(head, Brush.radialGradient(0f to Color(0xFF7AEBC0), 0.65f to Color(0xFF3CC795), 1f to Color(0xFF1F9E75), center = Offset(hx - 1.4f, hy - 1.8f), radius = 5f)); drawPath(head, ol, style = stroke(0.35f))
                    val snout = Path().apply { addOval(Rect(hx + 1f, hy - 0.7f, hx + 6.2f, hy + 3.1f)) }
                    drawPath(snout, vg(listOf(Color(0xFF6FE6B8), Color(0xFF36C897)), hy - 0.7f, hy + 3.1f)); drawPath(snout, ol, style = stroke(0.3f))
                    drawCircle(Color(0xFF0E563E), 0.32f, Offset(hx + 4.8f, hy + 0.5f)); drawCircle(Color(0xFF0E563E), 0.32f, Offset(hx + 5.4f, hy + 1.3f))
                    drawPath(Path().apply { moveTo(hx + 1.4f, hy + 2.2f); quadraticTo(hx + 3f, hy + (if (party) 3.6f else 3f), hx + 4.8f, hy + 2.4f) }, Ink, style = stroke(0.4f))
                    drawPath(Path().apply { moveTo(hx + 3.2f, hy + 2.9f); lineTo(hx + 3.7f, hy + 3.9f); lineTo(hx + 4.1f, hy + 2.8f); close() }, Color.White)
                    eye(hx + 0.6f, hy - 0.8f, 1f); drawCircle(Cheek.copy(alpha = 0.7f), 0.9f, Offset(hx + 1.2f, hy + 1.6f))
                    if (party) {
                        val kk = 0.6f + 0.4f * abs(sin(t * 8f)); val x0 = hx + 6f; val y0 = hy + 1.2f
                        drawPath(Path().apply { moveTo(x0, y0 - 0.6f); quadraticTo(x0 + 2.4f + 2f * kk, y0 - 3f, x0 + 4.6f + 2.5f * kk, y0); quadraticTo(x0 + 2.4f + 2f * kk, y0 + 2.2f, x0, y0 + 0.6f); close() },
                            hg(listOf(Color(0xFFFFE07A), Color(0xFFFF9F3A), Color(0xFFF2552C)), x0, x0 + 6f))
                    }
                }
                "ursinho" -> rotate(sin(t * 1.5f) * 0.05f * DEG, pivot = Offset.Zero) {
                    // ursinho de pelúcia (saúde): orelhinhas redondas, focinho claro, coração na barriga e um curativo na testa
                    for (s in listOf(-1f, 1f)) rotate(s * 0.3f * DEG, pivot = Offset(s * 4.3f, -3f)) { ell(Color(0xFFA8703F), s * 4.3f, -3f, 1.5f, 2.4f) }
                    ellB(vg(listOf(Color(0xFFD39B62), Color(0xFFA8703F)), -9.4f, 0f), 0f, -4.6f, 5.2f, 4.8f); ellLine(Out, 0f, -4.6f, 5.2f, 4.8f, 0.35f)
                    ell(Color(0xFFF3DDBF), 0f, -3.9f, 2.9f, 3f); drawPath(heart(0f, -4f, 1.1f), Color(0xFFFF8FA3))
                    for (s in listOf(-1f, 1f)) { ell(Color(0xFFC08650), s * 2.3f, -0.6f, 1.7f, 1f); ell(Color(0xFFF3DDBF), s * 2.3f, -0.5f, 0.9f, 0.55f) }
                    for (s in listOf(-1f, 1f)) { ell(Color(0xFFB57B45), s * 3.5f, -15.3f, 1.9f, 1.9f); ellLine(Out, s * 3.5f, -15.3f, 1.9f, 1.9f, 0.3f); ell(Color(0xFFF3C3A8), s * 3.5f, -15.2f, 1f, 1f) }
                    ellB(vg(listOf(Color(0xFFDEA76D), Color(0xFFB57B45)), -15.9f, -7.3f), 0f, -11.6f, 4.7f, 4.3f); ellLine(Out, 0f, -11.6f, 4.7f, 4.3f, 0.35f)
                    ell(Color(0xFFF3DDBF), 0f, -9.9f, 2.2f, 1.6f); ell(Ink, 0f, -10.7f, 0.85f, 0.6f); drawCircle(Color.White.copy(alpha = 0.7f), 0.25f, Offset(-0.3f, -10.9f))
                    eye(-1.8f, -12.4f, 0.75f); eye(1.8f, -12.4f, 0.75f); smile(0f, -9.4f, 0.8f)
                    drawCircle(Cheek.copy(alpha = 0.6f), 0.8f, Offset(-3.1f, -10.4f)); drawCircle(Cheek.copy(alpha = 0.6f), 0.8f, Offset(3.1f, -10.4f))
                    // o curativo: fita bege com a almofadinha no meio e os furinhos
                    translate(-1.6f, -14.5f) { rotate(0.45f * DEG, pivot = Offset.Zero) {
                        val ba = roundRect(-2.3f, -0.75f, 4.6f, 1.5f, 0.75f); drawPath(ba, Color(0xFFFFD9B8)); drawPath(ba, Color(0x99AA6E46), style = stroke(0.25f))
                        drawPath(roundRect(-0.7f, -0.6f, 1.4f, 1.2f, 0.3f), Color(0xFFFFF4EA))
                        for (dx in listOf(-1.6f, 1.6f)) for (dy in listOf(-0.25f, 0.25f)) drawCircle(Color(0x8CAA6E46), 0.13f, Offset(dx, dy))
                    } }
                }
                "pastor" -> {
                    // pastor-alemão (militar): orelhas em pé, focinho de máscara escura, sela preta nas costas, coleira
                    // verde-oliva com a plaquinha dourada e o rabo peludo que abana (rápido na festa)
                    val wag = sin(t * (if (party) 9f else 2f)) * (if (party) 0.5f else 0.2f)
                    translate(-4.2f, -2.6f) { rotate((-0.4f + wag) * DEG, pivot = Offset.Zero) { val tl = Path().apply { moveTo(0f, 0f); quadraticTo(-4f, -0.8f, -4.8f, -6f) }; drawPath(tl, Color(0xFF3A2A22), style = stroke(2.4f)); drawPath(tl, Color(0xFF6B4A33), style = stroke(1f)) } }
                    val body = ellPath(-0.4f, -4.8f, 5.2f, 4.9f)
                    drawPath(body, vg(listOf(Color(0xFFD9A15C), Color(0xFFB07434)), -9.7f, 0f))
                    clipPath(body) { ellB(vg(listOf(Color(0xFF4A3A30), Color(0xFF2A1E18)), -10.4f, -2.8f), -3.8f, -6.6f, 4.2f, 3.8f) }
                    drawPath(body, Out, style = stroke(0.35f))
                    ell(Color(0xFFF0CF9C), 1.8f, -3.8f, 2.3f, 3f)
                    for (fx in listOf(-1.2f, 2.8f)) ell(Color(0xFFC98A45), fx, -0.6f, 1.6f, 1f)
                    for (e in listOf(floatArrayOf(-2.6f, -14.4f, -2.8f, -19.8f, 0.4f, -15.8f), floatArrayOf(1.8f, -15.8f, 4f, -20f, 4.6f, -14.4f))) {
                        val ear = Path().apply { moveTo(e[0], e[1]); lineTo(e[2], e[3]); lineTo(e[4], e[5]); close() }
                        drawPath(ear, Color(0xFF3A2A22)); drawPath(ear, Out, style = stroke(0.3f))
                        drawPath(Path().apply { moveTo(e[0] * 0.7f + e[2] * 0.3f + 0.3f, e[1] * 0.7f + e[3] * 0.3f + 0.6f); lineTo(e[2] * 0.85f + (e[0] + e[4]) * 0.075f, e[3] * 0.85f + (e[1] + e[5]) * 0.075f); lineTo(e[4] * 0.7f + e[2] * 0.3f - 0.3f, e[5] * 0.7f + e[3] * 0.3f + 0.6f); close() }, Color(0xFF9A6A45))
                    }
                    val head = ellPath(1f, -12.2f, 4.3f, 4f)
                    drawPath(head, vg(listOf(Color(0xFFDDA764), Color(0xFFB87A38)), -16.2f, -8.2f))
                    clipPath(head) { ell(Color(0xFF3A2A22), 0.4f, -16f, 5f, 2.4f) }
                    drawPath(head, Out, style = stroke(0.35f))
                    ellB(vg(listOf(Color(0xFF4A3A30), Color(0xFF2A1E18)), -12.3f, -8.9f), 4.7f, -10.6f, 2.6f, 1.7f); ellLine(Out, 4.7f, -10.6f, 2.6f, 1.7f, 0.3f)
                    ell(Color(0xFF120E0C), 6.9f, -11.1f, 0.8f, 0.65f); drawCircle(Color.White.copy(alpha = 0.6f), 0.22f, Offset(6.6f, -11.4f))
                    drawCircle(Color(0xFFECC07F), 0.5f, Offset(-0.1f, -14.2f)); drawCircle(Color(0xFFECC07F), 0.5f, Offset(2.7f, -14.2f))
                    eye(-0.1f, -12.7f, 0.75f); eye(2.7f, -12.7f, 0.75f)
                    drawPath(Path().apply { moveTo(3.4f, -9.4f); quadraticTo(4.6f, -8.6f, 6f, -9.4f) }, Color(0xFFF0CF9C), style = stroke(0.4f))
                    if (party) { ell(Color(0xFFFF7F96), 4.9f, -8.4f, 0.75f, 1.05f); line(Color(0x99C83C5A), 4.9f, -8.9f, 4.9f, -7.8f, 0.2f) }
                    line(Color(0xFF5B6B2F), -3f, -8.3f, 4f, -8.7f, 1.2f)
                    ellB(vg(GOLD3, -8f, -6.4f), 1.2f, -7.2f, 0.85f, 0.85f); ellLine(Color(0xFFA86A08), 1.2f, -7.2f, 0.85f, 0.85f, 0.25f)
                    drawCircle(Cheek.copy(alpha = 0.55f), 0.7f, Offset(-1.7f, -10.8f))
                }
                "k9" -> {
                    // cão farejador (polícia): orelhas caídas que balançam, focinho que fareja, rabo em pé de ponta branca
                    // e o colete azul-marinho com a faixa refletiva e "K9" em amarelo
                    val sniff = abs(sin(t * 6f)) * 0.35f; val ear = sin(t * 2.1f) * 0.12f; val tw = sin(t * 3f) * 0.8f
                    drawPath(Path().apply { moveTo(-4.2f, -3.2f); quadraticTo(-7f, -4.6f, -6.6f + tw * 0.3f, -9f + tw * 0.2f) }, Color(0xFFA86530), style = stroke(1.6f)); drawCircle(Color(0xFFFFF4E6), 0.9f, Offset(-6.6f + tw * 0.3f, -9f + tw * 0.2f))
                    val body = ellPath(-0.2f, -4.6f, 5.2f, 4.7f); drawPath(body, vg(listOf(Color(0xFFD08A48), Color(0xFFA86530)), -9.3f, 0f))
                    clipPath(body) {
                        drawPath(roundRect(-5.6f, -8.8f, 9.2f, 6.6f, 1.2f), vg(listOf(Color(0xFF33438A), Color(0xFF1A2350)), -8.8f, -2.2f))
                        line(Color(0xFFC9D2E8), -5.6f, -3.4f, 3.6f, -3.4f, 0.55f); line(Color.White.copy(alpha = 0.8f), -5.6f, -3.55f, 3.6f, -3.55f, 0.15f)
                    }
                    drawPath(body, Out, style = stroke(0.35f))
                    for ((w, c) in listOf(0.9f to Color(0x800E0A45), 0.42f to Color(0xFFFFD23F))) {   // "K9" a traço
                        line(c, -2.6f, -7.4f, -2.6f, -4.6f, w); line(c, -2.6f, -5.9f, -1.2f, -7.4f, w); line(c, -2.1f, -6.3f, -1.1f, -4.6f, w)
                        drawCircle(c, 0.75f, Offset(0.4f, -6.6f), style = Stroke(w)); line(c, 1.15f, -6.6f, 0.9f, -4.6f, w)
                    }
                    for (fx in listOf(-1.6f, 2.6f)) ell(Color(0xFFFFF4E6), fx, -0.6f, 1.6f, 1f)
                    ellB(vg(listOf(Color(0xFFD69350), Color(0xFFAE6A33)), -16.2f, -8.6f), 1f, -12.4f, 4f, 3.8f); ellLine(Out, 1f, -12.4f, 4f, 3.8f, 0.35f)
                    ell(Color(0xFFFFF4E6), 1.2f, -13.9f, 0.8f, 2f)
                    ell(Color(0xFFFFF4E6), 3.9f, -10.6f, 2.5f, 1.7f); ellLine(Out, 3.9f, -10.6f, 2.5f, 1.7f, 0.25f)
                    ell(Ink, 6.1f + sniff * 0.3f, -11.2f, 0.9f, 0.7f); drawCircle(Color.White.copy(alpha = 0.6f), 0.22f, Offset(5.8f + sniff * 0.3f, -11.5f))
                    for ((ex, a) in listOf(-2.6f to 0.3f + ear, 4.4f to -0.3f - ear)) rotate(a * DEG, pivot = Offset(ex, -11.4f)) { ellB(vg(listOf(Color(0xFF8A5025), Color(0xFF6A3A18)), -14.6f, -8.2f), ex, -11.4f, 1.6f, 3.2f); ellLine(Out, ex, -11.4f, 1.6f, 3.2f, 0.3f) }
                    eye(-0.2f, -12.9f, 0.75f); eye(2.4f, -12.9f, 0.75f)
                    drawPath(Path().apply { moveTo(3f, -9.6f); quadraticTo(4.2f, -8.8f, 5.4f, -9.6f) }, Ink, style = stroke(0.38f))
                    drawCircle(Cheek.copy(alpha = 0.55f), 0.7f, Offset(1.6f, -10.4f))
                }
                "tartaruga" -> {
                    // tartaruga sábia (tribunais): casco de domo com placas hexagonais e borda creme, pescoço que espicha
                    // devagar e os óculos redondos de leitura
                    val nk = sin(t * 0.9f) * 0.5f + (if (party) 0.8f else 0f)
                    for (fx in listOf(-3.8f, 3.2f)) { ell(Color(0xFF7FBF6A), fx, -0.9f, 1.6f, 1.2f); ellLine(Out, fx, -0.9f, 1.6f, 1.2f, 0.25f) }
                    drawPath(Path().apply { moveTo(-5.8f, -2.8f); lineTo(-8f, -1.6f); lineTo(-5.6f, -1.4f); close() }, Color(0xFF7FBF6A))
                    val neck = Path().apply { moveTo(3.4f, -3f); quadraticTo(5.6f, -3.6f, 6.4f + nk, -6.6f) }
                    drawPath(neck, Color(0xFF6FAE5C), style = stroke(2.9f)); drawPath(neck, Color(0xFF8FCC78), style = stroke(2.1f))
                    val hx = 6.8f + nk; val hy = -7.6f
                    ellB(vg(listOf(Color(0xFFA8DC92), Color(0xFF78B862)), hy - 2.4f, hy + 2.4f), hx, hy, 2.7f, 2.4f); ellLine(Out, hx, hy, 2.7f, 2.4f, 0.3f)
                    val shell = Path().apply { moveTo(-6.4f, -2.2f); cubicTo(-6.2f, -10.6f, 5.8f, -10.6f, 6f, -2.2f); close() }
                    drawPath(shell, vg(listOf(Color(0xFF86C563), Color(0xFF4E8A3E)), -8.6f, -2.2f))
                    clipPath(shell) {
                        for (q in listOf(floatArrayOf(-0.2f, -5.6f, 2.1f), floatArrayOf(-4.4f, -4f, 1.8f), floatArrayOf(4f, -4f, 1.8f), floatArrayOf(-2.4f, -8.6f, 1.5f), floatArrayOf(2.2f, -8.6f, 1.5f))) {
                            val p = Path(); for (i in 0 until 6) { val a = i * PIf / 3f; val px = q[0] + cos(a) * q[2]; val py = q[1] + sin(a) * q[2] * 0.8f; if (i == 0) p.moveTo(px, py) else p.lineTo(px, py) }; p.close()
                            drawPath(p, Color(0x73AADC78)); drawPath(p, Color(0x8C28501E), style = stroke(0.35f))
                        }
                    }
                    drawPath(shell, Out, style = stroke(0.35f))
                    val rim = roundRect(-6.9f, -2.9f, 13.4f, 1.5f, 0.75f); drawPath(rim, vg(listOf(Color(0xFFE8D49A), Color(0xFFC9B27A)), -2.9f, -1.4f)); drawPath(rim, Out, style = stroke(0.25f))
                    rotate(-0.3f * DEG, pivot = Offset(-2.8f, -7.8f)) { ell(Color.White.copy(alpha = 0.35f), -2.8f, -7.8f, 1.6f, 0.7f) }
                    eye(hx - 0.7f, hy - 0.4f, 0.55f); eye(hx + 1.1f, hy - 0.4f, 0.55f)
                    for (ex in listOf(hx - 0.7f, hx + 1.1f)) { drawCircle(Color(0x40C8E6FF), 0.95f, Offset(ex, hy - 0.4f)); drawCircle(Ink, 0.95f, Offset(ex, hy - 0.4f), style = Stroke(0.28f)) }
                    line(Ink, hx - 0.05f, hy - 0.5f, hx + 0.45f, hy - 0.5f, 0.25f); line(Ink, hx - 1.65f, hy - 0.5f, hx - 2.6f, hy - 1f, 0.25f)
                    smile(hx + 0.4f, hy + 1.1f, 0.6f); drawCircle(Cheek.copy(alpha = 0.6f), 0.55f, Offset(hx + 1.9f, hy + 0.7f))
                }
                "leaozinho" -> {
                    // leãozinho (fiscal): juba fofa de tufos (estufa na festa), focinho branquinho, rabo com pompom
                    val tw = sin(t * 2f) * 1.6f; val fluff = if (party) abs(sin(t * 8f)) * 0.4f else 0f
                    drawPath(Path().apply { moveTo(4f, -1.8f); quadraticTo(9.4f, -2f, 8.6f + tw * 0.3f, -8f + tw * 0.6f) }, Color(0xFFE0A040), style = stroke(1.3f))
                    drawCircle(Color(0xFFB8541E), 1.4f, Offset(8.6f + tw * 0.3f, -8.4f + tw * 0.6f))
                    ellB(vg(listOf(Color(0xFFFFD27A), Color(0xFFE9A93E)), -9.3f, 0f), 0f, -4.6f, 5f, 4.7f); ellLine(Out, 0f, -4.6f, 5f, 4.7f, 0.35f)
                    ell(Color(0xFFFFF0C9), 0f, -3.7f, 2.6f, 2.9f)
                    for (s in listOf(-1f, 1f)) { ell(Color(0xFFFFE6A8), s * 2f, -0.6f, 1.5f, 0.95f); ellLine(Out, s * 2f, -0.6f, 1.5f, 0.95f, 0.25f) }
                    ell(Color(0xFFB8541E), 0f, -12f, 6.6f + fluff, 6.2f + fluff)
                    for (i in 0 until 12) { val a = i * 2f * PIf / 12f + sin(t * 1.6f) * 0.04f; drawCircle(if (i % 2 == 1) Color(0xFFE8812E) else Color(0xFFD26A22), 2.2f + fluff * 0.5f, Offset(cos(a) * (4.5f + fluff), -12f + sin(a) * (4.3f + fluff))) }
                    for (s in listOf(-1f, 1f)) { drawCircle(Color(0xFFF2B850), 1.3f, Offset(s * 3.2f, -15.4f)); drawCircle(Color(0xFFFFB3A0), 0.6f, Offset(s * 3.2f, -15.3f)) }
                    ellB(vg(listOf(Color(0xFFFFDA88), Color(0xFFF0B24A)), -15.6f, -8.4f), 0f, -12f, 3.8f, 3.6f); ellLine(Out, 0f, -12f, 3.8f, 3.6f, 0.3f)
                    for (s in listOf(-1f, 1f)) ell(Color(0xFFFFF6E0), s * 0.9f, -10.3f, 1.2f, 0.95f)
                    drawPath(Path().apply { moveTo(-0.75f, -11.4f); lineTo(0.75f, -11.4f); lineTo(0f, -10.6f); close() }, Color(0xFF7A3E1A))
                    eye(-1.6f, -12.7f, 0.75f); eye(1.6f, -12.7f, 0.75f)
                    drawPath(Path().apply { moveTo(-0.9f, -9.6f); quadraticTo(-0.45f, -9f, 0f, -9.7f); quadraticTo(0.45f, -9f, 0.9f, -9.6f) }, Ink, style = stroke(0.35f))
                    drawCircle(Cheek.copy(alpha = 0.6f), 0.75f, Offset(-2.6f, -10.9f)); drawCircle(Cheek.copy(alpha = 0.6f), 0.75f, Offset(2.6f, -10.9f))
                }
                "castor" -> {
                    // castor contador (controle): rabo de remo quadriculado que bate, dentões, óculos redondos e o lápis
                    // atrás da orelha
                    val slap = if (party) abs(sin(t * 8f)) * 0.35f else sin(t * 1.6f) * 0.06f
                    translate(-3.6f, -1.4f) { rotate((-0.25f - slap) * DEG, pivot = Offset.Zero) {
                        val tl = ellPath(-3.6f, 0f, 3.8f, 1.7f); drawPath(tl, vg(listOf(Color(0xFF6E5040), Color(0xFF4A3328)), -1.7f, 1.7f))
                        clipPath(tl) { for (i in -4..4) { line(Color(0x73190F0A), -7.4f + i * 1.2f, -2f, -5.4f + i * 1.2f, 2f, 0.22f); line(Color(0x73190F0A), -5.4f + i * 1.2f, -2f, -7.4f + i * 1.2f, 2f, 0.22f) } }
                        drawPath(tl, Out, style = stroke(0.3f))
                    } }
                    ellB(vg(listOf(Color(0xFFB07A4E), Color(0xFF8A5A36)), -9.6f, 0f), 0f, -4.8f, 5f, 4.8f); ellLine(Out, 0f, -4.8f, 5f, 4.8f, 0.35f)
                    ell(Color(0xFFD9B48C), 0f, -4f, 2.8f, 3.2f)
                    for (s in listOf(-1f, 1f)) ell(Color(0xFF5E4030), s * 2.2f, -0.6f, 1.6f, 0.9f)
                    translate(3.4f, -15.6f) { rotate(-0.75f * DEG, pivot = Offset.Zero) {   // o lápis atrás da orelha direita
                        drawRect(Color(0xFFFF8FA3), Offset(-4.4f, -0.45f), Size(0.8f, 0.9f)); drawRect(Color(0xFFC9CCD6), Offset(-3.6f, -0.45f), Size(0.6f, 0.9f))
                        drawRect(vg(listOf(Color(0xFFFFE27A), Gold), -0.45f, 0.45f), Offset(-3f, -0.45f), Size(5f, 0.9f))
                        drawPath(Path().apply { moveTo(2f, -0.45f); lineTo(3.4f, 0f); lineTo(2f, 0.45f); close() }, Color(0xFFF3D3A5)); drawCircle(Ink, 0.2f, Offset(3.2f, 0f))
                    } }
                    for (s in listOf(-1f, 1f)) { drawCircle(Color(0xFF8A5A36), 1.25f, Offset(s * 3.4f, -15f)); drawCircle(Color(0xFFC99872), 0.55f, Offset(s * 3.4f, -15f)) }
                    ellB(vg(listOf(Color(0xFFBE8A5C), Color(0xFF94643E)), -15.8f, -7.8f), 0f, -11.8f, 4.4f, 4f); ellLine(Out, 0f, -11.8f, 4.4f, 4f, 0.35f)
                    ell(Color(0xFFE1C29C), 0f, -10f, 2.3f, 1.6f); ell(Color(0xFF3A2418), 0f, -10.9f, 0.9f, 0.6f)
                    for (s in listOf(-1f, 1f)) { val th = roundRect(if (s > 0f) 0.05f else -0.95f, -9.3f, 0.9f, 1.5f, 0.25f); drawPath(th, Color.White); drawPath(th, Color(0x7326215C), style = stroke(0.2f)) }
                    eye(-1.7f, -12.6f, 0.7f); eye(1.7f, -12.6f, 0.7f)
                    for (s in listOf(-1f, 1f)) { drawCircle(Ink, 1.25f, Offset(s * 1.7f, -12.6f), style = Stroke(0.32f)); line(Ink, s * 2.95f, -12.8f, s * 4.2f, -13.3f, 0.28f) }
                    line(Ink, -0.45f, -12.8f, 0.45f, -12.8f, 0.3f)
                    drawCircle(Cheek.copy(alpha = 0.6f), 0.7f, Offset(-3f, -10.6f)); drawCircle(Cheek.copy(alpha = 0.6f), 0.7f, Offset(3f, -10.6f))
                }
                "porquinho" -> rotate(sin(t * 1.7f) * 0.04f * DEG, pivot = Offset.Zero) {
                    // porquinho-cofre de louça (bancos): fenda de moeda nas costas, focinho de disco, rabinho enrolado,
                    // uma florzinha pintada no lado e o brilho de cerâmica; balança de leve
                    val pink = Color(0xFFE77E9C); val ol = Color(0x73AA3C5A)
                    for (fx in listOf(-3.6f, -1.2f, 2f, 4.2f)) drawPath(roundRect(fx - 0.9f, -2.4f, 1.8f, 2.4f, 0.6f), pink)
                    drawPath(Path().apply { moveTo(-6.2f, -6.4f); cubicTo(-8.6f, -7.8f, -8.8f, -4.6f, -7.4f, -5.2f); cubicTo(-6.6f, -5.6f, -7.2f, -7f, -8f, -6.6f) }, pink, style = stroke(0.6f))
                    drawPath(Path().apply { moveTo(-0.6f, -9.9f); lineTo(0.4f, -12.4f); lineTo(1.8f, -10.3f); close() }, pink)
                    ellB(vg(listOf(Color(0xFFFFC2D1), Color(0xFFF79AB4), pink), -10.9f, -1.1f), 0f, -6f, 6.6f, 4.9f); ellLine(ol, 0f, -6f, 6.6f, 4.9f, 0.35f)
                    val ear = Path().apply { moveTo(1.6f, -9.8f); lineTo(3f, -12.8f); lineTo(4.2f, -9.9f); close() }; drawPath(ear, Color(0xFFF28AA8)); drawPath(ear, Color(0x66AA3C5A), style = stroke(0.25f))
                    drawPath(roundRect(-3.4f, -10.9f, 3.4f, 0.9f, 0.45f), Color(0xFF5A2338)); line(Color.White.copy(alpha = 0.6f), -3f, -9.8f, -0.4f, -9.8f, 0.25f)
                    ellB(vg(listOf(Color(0xFFFFB3C6), Color(0xFFF08AA6)), -7.7f, -3.9f), 6.4f, -5.8f, 1.5f, 1.9f); ellLine(ol, 6.4f, -5.8f, 1.5f, 1.9f, 0.3f)
                    ell(Color(0xFFB84A6A), 6.5f, -6.5f, 0.3f, 0.45f); ell(Color(0xFFB84A6A), 6.5f, -5.1f, 0.3f, 0.45f)
                    eye(3.6f, -7.4f, 0.75f); smile(4.2f, -4.8f, 0.7f); drawCircle(Color(0x8CFF7896), 0.9f, Offset(3.4f, -5.6f))
                    for (i in 0 until 5) { val a = i * 2f * PIf / 5f; drawCircle(Color.White.copy(alpha = 0.85f), 0.55f, Offset(-2.4f + cos(a) * 0.75f, -4.8f + sin(a) * 0.75f)) }
                    drawCircle(Gold, 0.45f, Offset(-2.4f, -4.8f))
                    rotate(-0.2f * DEG, pivot = Offset(-2.4f, -8.4f)) { ell(Color.White.copy(alpha = 0.55f), -2.4f, -8.4f, 2f, 0.75f) }
                    drawCircle(Color.White.copy(alpha = 0.8f), 0.35f, Offset(0.2f, -9.3f))
                }
                "pombo" -> {
                    // pombo-correio (legislativo): cinza com pescoço furta-cor, cabeça que vai e vem (bem de pombo), asa de
                    // listras e a cartinha com selo de coração no bico
                    val bob = if (party) sin(t * 10f) * 0.8f else (if ((t * 1.2f) % 1f < 0.5f) 0.8f else 0f); val flap = if (party) sin(t * 14f) * 0.5f else 0f
                    for (fx in listOf(-1f, 1.6f)) { line(Color(0xFFF08A5D), fx, -2.2f, fx, -0.3f, 0.5f); line(Color(0xFFF08A5D), fx - 0.7f, 0f, fx + 0.9f, 0f, 0.45f) }
                    val tail = Path().apply { moveTo(-4f, -5.2f); lineTo(-8.4f, -3.8f); lineTo(-7.8f, -2f); lineTo(-3.6f, -3.2f); close() }; drawPath(tail, Color(0xFF6E7490)); drawPath(tail, Out, style = stroke(0.25f))
                    ellB(vg(listOf(Color(0xFFC9CCDC), Color(0xFF8E93AE)), -8.6f, -1.4f), -0.4f, -5f, 5f, 3.6f); ellLine(Out, -0.4f, -5f, 5f, 3.6f, 0.35f)
                    translate(-1.6f, -6.4f) { rotate((-0.15f - flap) * DEG, pivot = Offset.Zero) {
                        rotate(0.15f * DEG, pivot = Offset(-1f, 0.6f)) { ellB(vg(listOf(Color(0xFFB3B7CC), Color(0xFF7A809E)), -1.4f, 2.6f), -1f, 0.6f, 3.6f, 2f); ellLine(Out, -1f, 0.6f, 3.6f, 2f, 0.25f) }
                        line(Color(0xFF4E536E), -2.6f, 0.4f, -0.6f, 1.4f, 0.4f); line(Color(0xFF4E536E), -3.6f, 1.2f, -1.6f, 2.2f, 0.4f)
                    } }
                    ellB(hg(listOf(Color(0xFF5FB88C), Color(0xFF8A6FC8)), 0.6f, 5f), 2.8f + bob * 0.5f, -8.4f, 2.2f, 2.4f)
                    ellB(vg(listOf(Color(0xFFC2C6D8), Color(0xFF8E93AE)), -13.5f, -8.5f), 3.6f + bob, -11f, 2.6f, 2.5f); ellLine(Out, 3.6f + bob, -11f, 2.6f, 2.5f, 0.3f)
                    drawPath(Path().apply { moveTo(5.8f + bob, -11.5f); lineTo(7.7f + bob, -10.9f); lineTo(5.8f + bob, -10.3f); close() }, Color(0xFFE8A07A)); drawCircle(Color(0xFFF4F2FA), 0.4f, Offset(5.9f + bob, -11.5f))
                    translate(7.4f + bob, -10.8f) { rotate((0.15f + sin(t * 2f) * 0.05f) * DEG, pivot = Offset.Zero) {   // a cartinha
                        val env = roundRect(-0.4f, -1.4f, 4.4f, 2.9f, 0.35f); drawPath(env, vg(listOf(Color.White, Color(0xFFECEAF6)), -1.4f, 1.5f)); drawPath(env, Color(0x9926215C), style = stroke(0.28f))
                        drawPath(Path().apply { moveTo(-0.4f, -1.4f); lineTo(1.8f, 0.2f); lineTo(4f, -1.4f) }, Color(0x7326215C), style = stroke(0.25f))
                        drawPath(heart(1.8f, 0.3f, 0.6f), Color(0xFFFF5C6E))
                    } }
                    drawCircle(Color(0xFFFFB07A), 1.05f, Offset(3.9f + bob, -11.6f)); eye(3.9f + bob, -11.6f, 0.7f); drawCircle(Cheek.copy(alpha = 0.6f), 0.6f, Offset(4.6f + bob, -10.1f))
                }
                "minhoca" -> {
                    // minhoca leitora (educação): sai do buraquinho de uma maçã vermelha, de gomos rosados que balançam,
                    // com óculos redondos de quem leu muito
                    val sw = sin(t * 1.8f) * (if (party) 1.6f else 0.8f)
                    val ap = Path().apply { moveTo(0f, -8.8f); cubicTo(3f, -11.2f, 6.4f, -9.4f, 6f, -5.2f); cubicTo(5.8f, -1.4f, 3f, 0.4f, 0f, -0.6f); cubicTo(-3f, 0.4f, -5.8f, -1.4f, -6f, -5.2f); cubicTo(-6.4f, -9.4f, -3f, -11.2f, 0f, -8.8f); close() }
                    drawPath(ap, vg(listOf(Color(0xFFFF6B6B), Color(0xFFE0384A), Color(0xFFB82A3A)), -10.4f, 0f)); drawPath(ap, Color(0x806E1423), style = stroke(0.35f))
                    line(Color(0xFF7A4A2A), 0f, -9f, 0.4f, -11.4f, 0.6f)
                    rotate(-0.5f * DEG, pivot = Offset(1.9f, -11.3f)) { ellB(vg(listOf(Color(0xFF5FD3A0), Color(0xFF2E9E72)), -12f, -10.6f), 1.9f, -11.3f, 1.6f, 0.7f) }
                    line(Color(0x99145A3C), 0.6f, -10.7f, 3.1f, -11.9f, 0.2f)
                    rotate(0.3f * DEG, pivot = Offset(-3.4f, -6.4f)) { ell(Color.White.copy(alpha = 0.5f), -3.4f, -6.4f, 0.9f, 1.8f) }
                    ell(Color(0xFF5A1A22), 2.6f, -6.4f, 1.4f, 1f)
                    fun pt(u: Float) = Offset(2.6f + 2.4f * u + sw * u * u, -6.8f - 6.2f * u)
                    for (i in 0..5) { val p = pt(i / 5f * 0.9f); drawCircle(Color(0x73AA3C5A), 1.45f, p); drawCircle(if (i % 2 == 1) Color(0xFFFF9EB5) else Color(0xFFF47E9E), 1.2f, p) }
                    val hp = pt(1.15f); val hx = hp.x; val hy = hp.y
                    ellB(vg(listOf(Color(0xFFFFB8CA), Color(0xFFF48AA6)), hy - 1.9f, hy + 1.9f), hx, hy, 2.1f, 1.9f); ellLine(Color(0x73AA3C5A), hx, hy, 2.1f, 1.9f, 0.3f)
                    eye(hx - 0.75f, hy - 0.2f, 0.48f); eye(hx + 0.75f, hy - 0.2f, 0.48f)
                    for (s in listOf(-1f, 1f)) drawCircle(Ink, 0.8f, Offset(hx + s * 0.75f, hy - 0.2f), style = Stroke(0.26f))
                    line(Ink, hx - 0.05f, hy - 0.3f, hx + 0.05f, hy - 0.3f, 0.26f)
                    smile(hx, hy + 0.9f, 0.5f); drawCircle(Cheek.copy(alpha = 0.7f), 0.45f, Offset(hx - 1.5f, hy + 0.6f)); drawCircle(Cheek.copy(alpha = 0.7f), 0.45f, Offset(hx + 1.5f, hy + 0.6f))
                }
                "pixel" -> {
                    // gatinho de pixel (TI): o gatinho em blocos 8-bit, com contorno de bloco, realce em cima, olhos de um
                    // pixel que piscam e o rabo que troca de quadro (como animação de jogo antigo)
                    val px = listOf(".d.....d..", ".dd...dd..", ".ooooooo..", "ooeoooeoo.", "ocoopooco.", ".ooooooo..", "..ooooo...", ".oooowoo..", ".ooowwwoo.", ".ooowwwoo.", ".ww...ww..")
                    val c = 1.35f; val x0 = -6.6f; val y0 = -14.85f; val fr = (t * (if (party) 6f else 1.5f)).toInt() % 2
                    val cells = mutableListOf<Triple<Int, Int, Char>>()
                    px.forEachIndexed { r, row -> row.forEachIndexed { q, ch -> if (ch != '.') cells.add(Triple(q, r, ch)) } }
                    cells.add(Triple(9, 8, 'd')); cells.add(Triple(9, 7, 'd')); cells.add(if (fr == 1) Triple(10, 6, 'd') else Triple(9, 6, 'd'))
                    for ((q, r) in cells) drawRect(Color(0x8C26215C), Offset(x0 + q * c - 0.35f, y0 + r * c - 0.35f), Size(c + 0.7f, c + 0.7f))
                    for ((q, r, ch) in cells) {
                        val cx = x0 + q * c; val cy = y0 + r * c
                        if (ch == 'e') {
                            if (blink) { drawRect(Color(0xFFF7A44A), Offset(cx, cy), Size(c + 0.02f, c + 0.02f)); drawRect(Ink, Offset(cx, cy + c * 0.55f), Size(c, c * 0.35f)) }
                            else { drawRect(Ink, Offset(cx, cy), Size(c + 0.02f, c + 0.02f)); if (!sad) drawRect(Color.White, Offset(cx + 0.15f, cy + 0.15f), Size(0.45f, 0.45f)) else if (tearAt == null) tearAt = Offset(cx + c / 2f, cy + c) }
                            continue
                        }
                        val col = when (ch) { 'd' -> Color(0xFFD9772B); 'w' -> Color(0xFFFFF1DD); 'p' -> Color(0xFFFF8FA3); 'c' -> Color(0xFFFFB3C7); else -> Color(0xFFF7A44A) }
                        drawRect(col, Offset(cx, cy), Size(c + 0.02f, c + 0.02f))
                        if (ch == 'o' && (r == 0 || px[r - 1][q] == '.')) drawRect(Color.White.copy(alpha = 0.35f), Offset(cx, cy), Size(c, 0.35f))
                    }
                }
                "peixinho" -> {
                    // peixinho dourado no aquário redondo (administrativo): água com marolinha, pedrinhas e alga; o peixe
                    // nada de um lado para o outro (vira na ponta) e solta bolhas; vidro com brilho e borda de cima
                    val rr = 6.6f; val cy = -7f; val sp = if (party) 1.8f else 0.9f; val wy = -9.4f
                    val bowl = Path().apply { addOval(Rect(-rr, cy - rr, rr, cy + rr)) }
                    drawPath(bowl, Color(0x59DCF0FF))
                    val fx = sin(t * sp) * 2.6f; val dir = if (cos(t * sp) >= 0f) 1f else -1f; val fy = -5.4f + sin(t * 2.3f) * 0.5f; val wig = sin(t * 8f) * 0.4f
                    clipPath(bowl) {
                        val wv = Path().apply { moveTo(-8f, wy); var xx = -8f; while (xx <= 8f) { lineTo(xx, wy + sin(xx * 1.2f + t * 3f) * 0.3f); xx += 0.5f }; lineTo(8f, 2f); lineTo(-8f, 2f); close() }
                        drawPath(wv, vg(listOf(Color(0xCC82CDFF), Color(0xE63C8CE6)), wy, -0.4f))
                        line(Color.White.copy(alpha = 0.6f), -5f, wy + 0.2f, 5f, wy + 0.2f, 0.3f)
                        for ((ppx, pr, pc) in listOf(Triple(-3.6f, 1.1f, Color(0xFFF5C842)), Triple(-1.6f, 0.9f, Color(0xFFB995F5)), Triple(0.4f, 1.2f, Color(0xFFFF8FA3)), Triple(2.6f, 1f, Color(0xFFF5C842)), Triple(4.2f, 0.9f, Mint))) drawCircle(pc, pr, Offset(ppx, -0.8f))
                        drawPath(Path().apply { moveTo(-3.4f, -1.2f); quadraticTo(-4.4f + sin(t * 2f) * 0.6f, -3.6f, -3f + sin(t * 2.2f) * 0.8f, -6f) }, Color(0xFF2E9E72), style = stroke(0.8f))
                        translate(fx, fy) { scale(dir, 1f, pivot = Offset.Zero) {
                            drawPath(Path().apply { moveTo(-1.5f, 0f); lineTo(-3.1f, -1.2f + wig); lineTo(-3.1f, 1.2f + wig); close() }, Color(0xFFFF8A2B))
                            ellB(hg(listOf(Color(0xFFFF7A2B), Color(0xFFFFB347)), -2f, 2f), 0f, 0f, 2f, 1.4f); ellLine(Color(0x73A03C0A), 0f, 0f, 2f, 1.4f, 0.22f)
                            rotate(-0.2f * DEG, pivot = Offset(-0.2f, -1.3f)) { ell(Color(0xFFFF8A2B), -0.2f, -1.3f, 0.9f, 0.4f) }
                            if (sad) drawCircle(Ink, 0.45f, Offset(0.9f, -0.3f)) else eye(0.9f, -0.3f, 0.5f)
                        } }
                        val nb = if (party) 5 else 3
                        for (i in 0 until nb) { val ph = (t * 0.6f + i.toFloat() / nb) % 1f; drawCircle(Color.White.copy(alpha = 0.9f * (1f - ph * 0.6f)), 0.35f + ph * 0.25f, Offset(fx + dir * 2.2f + sin(t * 3f + i) * 0.4f, fy - 0.6f - ph * (fy - wy)), style = Stroke(0.22f)) }
                    }
                    drawPath(bowl, Color(0xE678AADC), style = stroke(0.5f))
                    ell(Color.White.copy(alpha = 0.35f), 0f, cy - rr + 0.5f, 3.6f, 0.9f); ellLine(Color(0xFF8FB8E6), 0f, cy - rr + 0.5f, 3.6f, 0.9f, 0.7f)
                    drawArc(Color.White.copy(alpha = 0.85f), 180f * 1.08f, 180f * 0.3f, false, Offset(-(rr - 1.2f), cy - (rr - 1.2f)), Size(2f * (rr - 1.2f), 2f * (rr - 1.2f)), style = stroke(0.6f))
                    drawCircle(Color.White.copy(alpha = 0.8f), 0.35f, Offset(-4.4f, -2.6f))
                }
                "cachorrinho" -> {
                    // cachorrinho branquinho: orelhas caídas marrons, mancha no olho, linguinha de fora, coleira vermelha e
                    // o rabo que não para (abana mais rápido na festa)
                    val wag = sin(t * (if (party) 14f else 4f)) * (if (party) 0.6f else 0.35f); val ear = sin(t * 2.4f) * 0.1f
                    translate(-4.2f, -3.4f) { rotate((-0.6f + wag) * DEG, pivot = Offset.Zero) { val tl = Path().apply { moveTo(0f, 0f); quadraticTo(-1.8f, -2f, -1.1f, -4.4f) }; drawPath(tl, Color(0x99967859), style = stroke(1.9f)); drawPath(tl, Color.White, style = stroke(1.3f)); drawCircle(Color(0xFFC98A50), 0.75f, Offset(-1.1f, -4.4f)) } }
                    val body = ellPath(0f, -4.6f, 5f, 4.7f); drawPath(body, vg(listOf(Color.White, Color(0xFFEDE3D6)), -9.3f, 0f))
                    clipPath(body) { ell(Color(0xFFD9A066), -3f, -5.8f, 1.8f, 1.4f) }; drawPath(body, Out, style = stroke(0.35f))
                    for (s in listOf(-1f, 1f)) { ell(Color.White, s * 2f, -0.6f, 1.5f, 0.9f); ellLine(Out, s * 2f, -0.6f, 1.5f, 0.9f, 0.25f) }
                    line(Color(0xFFFF5C6E), -3.2f, -8.6f, 3.2f, -8.6f, 1f); drawCircle(Gold, 0.6f, Offset(0.2f, -7.8f))
                    val head = ellPath(0f, -12f, 4.4f, 4f); drawPath(head, vg(listOf(Color.White, Color(0xFFEFE6DA)), -16f, -8f)); drawPath(head, Out, style = stroke(0.35f))
                    clipPath(head) { ell(Color(0xFFE0AD78), 1.9f, -12.8f, 1.6f, 1.5f) }
                    for (s in listOf(-1f, 1f)) rotate(-s * (0.35f + ear) * DEG, pivot = Offset(s * 4.1f, -11.6f)) { ellB(vg(listOf(Color(0xFFC98A50), Color(0xFFA8703F)), -14.7f, -8.5f), s * 4.1f, -11.6f, 1.5f, 3.1f); ellLine(Out, s * 4.1f, -11.6f, 1.5f, 3.1f, 0.3f) }
                    ell(Color(0xFFFFF8F0), 0f, -10.2f, 2f, 1.4f); ell(Ink, 0f, -11f, 0.8f, 0.6f); drawCircle(Color.White.copy(alpha = 0.7f), 0.2f, Offset(-0.25f, -11.2f))
                    eye(-1.7f, -12.7f, 0.8f); eye(1.7f, -12.7f, 0.8f)
                    drawPath(Path().apply { moveTo(-1f, -9.8f); quadraticTo(-0.5f, -9.2f, 0f, -9.9f); quadraticTo(0.5f, -9.2f, 1f, -9.8f) }, Ink, style = stroke(0.38f))
                    if (!sad) { ell(Color(0xFFFF8FA3), 0.35f, -8.9f, 0.65f, 0.85f); line(Color(0x80C83C5A), 0.35f, -9.3f, 0.35f, -8.4f, 0.18f) }
                    drawCircle(Cheek.copy(alpha = 0.6f), 0.75f, Offset(-2.8f, -10.6f)); drawCircle(Cheek.copy(alpha = 0.6f), 0.75f, Offset(2.8f, -10.6f))
                }
                "pinguim" -> rotate(sin(t * 2.4f) * 0.08f * DEG, pivot = Offset.Zero) {
                    // pinguinzinho: corpo de ovo azul-escuro, barriga branca, bico e pés laranja, cachecol vermelho de
                    // listras e nadadeiras que batem; bamboleia de um lado para o outro
                    val flap = if (party) sin(t * 14f) * 0.6f else sin(t * 2.4f) * 0.12f; val dark = Color(0xFF2B2E4A)
                    for (s in listOf(-1f, 1f)) ell(Color(0xFFFF9F3A), s * 1.8f, -0.4f, 1.7f, 0.8f)
                    for (s in listOf(-1f, 1f)) translate(s * 4.5f, -8f) { rotate(s * (0.35f + flap) * DEG, pivot = Offset.Zero) { ell(dark, 0f, 2.6f, 1.2f, 3.2f) } }
                    ellB(vg(listOf(Color(0xFF4A4F78), dark, Color(0xFF1E2036)), -15.8f, -0.2f), 0f, -8f, 5.2f, 7.8f); ellLine(Out, 0f, -8f, 5.2f, 7.8f, 0.35f)
                    ellB(vg(listOf(Color.White, Color(0xFFE8ECF6)), -10.8f, -0.4f), 0f, -5.6f, 3.7f, 5.2f); ell(Color.White, 0f, -11f, 3.4f, 2.8f)
                    eye(-1.4f, -11.8f, 0.75f); eye(1.4f, -11.8f, 0.75f)
                    drawPath(Path().apply { moveTo(-0.9f, -10.5f); lineTo(0.9f, -10.5f); lineTo(0f, -9.3f); close() }, Color(0xFFFF9F3A))
                    drawCircle(Cheek.copy(alpha = 0.7f), 0.7f, Offset(-2.4f, -10.2f)); drawCircle(Cheek.copy(alpha = 0.7f), 0.7f, Offset(2.4f, -10.2f))
                    drawPath(roundRect(-4.7f, -8.9f, 9.4f, 1.7f, 0.85f), vg(listOf(Color(0xFFFF7A88), Color(0xFFE0475E)), -8.9f, -7.2f))
                    translate(2.2f, -7.8f) { rotate((-0.15f + sin(t * 2f) * 0.08f) * DEG, pivot = Offset.Zero) { drawPath(roundRect(-0.8f, 0f, 1.7f, 3.4f, 0.5f), Color(0xFFE0475E)); line(Color.White.copy(alpha = 0.8f), -0.8f, 1.6f, 0.9f, 1.6f, 0.35f) } }
                    for (lx in listOf(-2.6f, 0f, 2.6f)) line(Color.White.copy(alpha = 0.75f), lx, -8.9f, lx, -7.2f, 0.35f)
                    rotate(-0.3f * DEG, pivot = Offset(-2f, -13.8f)) { ell(Color.White.copy(alpha = 0.35f), -2f, -13.8f, 1.4f, 0.6f) }
                }
                "abelha" -> {
                    // abelhinha voando: paira no ar balançando, asas transparentes batendo rápido, listras, ferrãozinho,
                    // anteninhas e um rastro pontilhado de voo; com sono ela pousa no chão e fecha as asas
                    val fl = if (sleepy) 0f else 1f; val bx = sin(t * 1.1f) * 1.4f * fl; val by = if (sleepy) -3.4f else -10f + sin(t * 3.2f) * 1.2f
                    if (!sleepy) for (kk in 1..6) drawCircle(Ink.copy(alpha = 0.28f * (1f - kk / 7f)), 0.35f, Offset(bx - 4.6f - kk * 1.5f, by + 0.6f + sin(t * 3.2f - kk * 0.9f) * 1.6f))
                    drawPath(Path().apply { moveTo(bx - 4.4f, by - 0.4f); lineTo(bx - 6f, by + 0.2f); lineTo(bx - 4.4f, by + 0.8f); close() }, Ink)
                    val body = ellPath(bx, by, 4.6f, 3.3f); drawPath(body, vg(listOf(Color(0xFFFFE27A), Color(0xFFFFC93C), Color(0xFFF0A800)), by - 3.3f, by + 3.3f))
                    clipPath(body) { for (sx in listOf(-2.8f, -0.6f, 1.6f)) drawRect(Ink.copy(alpha = 0.92f), Offset(bx + sx, by - 4f), Size(1.1f, 8f)) }
                    drawPath(body, Out, style = stroke(0.35f)); rotate(-0.1f * DEG, pivot = Offset(bx - 1f, by - 2f)) { ell(Color.White.copy(alpha = 0.45f), bx - 1f, by - 2f, 1.6f, 0.5f) }
                    val wsc = if (sleepy) 0.5f else 0.45f + 0.55f * abs(sin(t * 28f))
                    for ((wx, a) in listOf(-1.2f to -0.45f, 0.6f to 0.25f)) translate(bx + wx, by - 2.6f) { rotate(a * DEG, pivot = Offset.Zero) { scale(1f, wsc, pivot = Offset.Zero) {
                        ell(Color(0xB3E1F2FF), 0f, -2.4f, 1.8f, 2.7f); ellLine(Color(0xBF6E96D2), 0f, -2.4f, 1.8f, 2.7f, 0.3f); line(Color(0x806E96D2), 0f, 0f, 0f, -3.8f, 0.2f)
                    } } }
                    for ((ax, tx) in listOf(3.6f to 3.4f, 4.8f to 5.6f + sin(t * 3f) * 0.3f)) { drawPath(Path().apply { moveTo(bx + ax, by - 2.6f); quadraticTo(bx + ax, by - 4.6f, bx + tx, by - 5.4f) }, Ink, style = stroke(0.3f)); drawCircle(Ink, 0.4f, Offset(bx + tx, by - 5.4f)) }
                    ellB(vg(listOf(Color(0xFFFFE89A), Color(0xFFFFCC4A)), by - 3f, by + 1.8f), bx + 4.2f, by - 0.6f, 2.5f, 2.4f); ellLine(Out, bx + 4.2f, by - 0.6f, 2.5f, 2.4f, 0.3f)
                    eye(bx + 3.5f, by - 1f, 0.55f); eye(bx + 5.2f, by - 1f, 0.55f); smile(bx + 4.4f, by + 0.4f, 0.55f)
                    drawCircle(Cheek.copy(alpha = 0.7f), 0.5f, Offset(bx + 2.8f, by + 0.3f)); drawCircle(Cheek.copy(alpha = 0.7f), 0.5f, Offset(bx + 6f, by + 0.2f))
                }
                "polvo" -> {
                    // polvinho: cabeça de domo lilás com pintinhas e olhos grandes, seis tentáculos que ondulam com
                    // ventosas; na festa os tentáculos das pontas acenam
                    for (i in 0 until 6) {
                        val tbx = -4f + i * 1.6f; val sd = if (i < 3) -1f else 1f; val wave = if ((i == 0 || i == 5) && party) -abs(sin(t * 8f + i)) * 3.4f else 0f
                        val ex = tbx + cos(t * 2.4f + i) * 0.9f + sd; val ey = -0.5f + wave
                        val tp = Path().apply { moveTo(tbx, -4.6f); quadraticTo(tbx + sin(t * 2.4f + i) * 1.4f + sd * 0.6f, -2.2f + wave * 0.5f, ex, ey) }
                        drawPath(tp, Color(0xFF8E57D6), style = stroke(2f)); drawPath(tp, Color(0xFFB983F0), style = stroke(1.3f))
                        drawCircle(Color(0xFFB983F0), 0.75f, Offset(ex + sd * 0.3f, ey - 0.2f)); drawCircle(Color(0xFFF3D6FF), 0.25f, Offset((tbx + ex) / 2f, (-4.6f + ey) / 2f + 0.6f))
                    }
                    ell(Color(0xFFA46CE2), 0f, -4.6f, 5f, 1.6f)
                    ellB(Brush.radialGradient(0f to Color(0xFFE9C4FF), 0.6f to Color(0xFFB983F0), 1f to Color(0xFF8E57D6), center = Offset(-1.6f, -11.4f), radius = 6.4f), 0f, -9f, 5.2f, 5.6f); ellLine(Color(0x80502890), 0f, -9f, 5.2f, 5.6f, 0.35f)
                    for (q in listOf(floatArrayOf(-2.8f, -12.2f, 0.8f), floatArrayOf(2.9f, -12.6f, 0.55f), floatArrayOf(-0.5f, -14f, 0.5f), floatArrayOf(3.6f, -10.4f, 0.4f))) drawCircle(Color(0xFFD9A8FF), q[2], Offset(q[0], q[1]))
                    for (s in listOf(-1f, 1f)) { drawCircle(Color.White, 1.45f, Offset(s * 1.8f, -8.6f)); eye(s * 1.8f, -8.6f, 0.95f) }
                    smile(0f, -6.4f, 0.8f); drawCircle(Cheek.copy(alpha = 0.75f), 0.75f, Offset(-3.4f, -7f)); drawCircle(Cheek.copy(alpha = 0.75f), 0.75f, Offset(3.4f, -7f))
                    rotate(-0.4f * DEG, pivot = Offset(-2.2f, -12.8f)) { ell(Color.White.copy(alpha = 0.45f), -2.2f, -12.8f, 1.4f, 0.7f) }
                }
            }
            // ---- reage ao Folha (à direita dele): o carinho de tempos em tempos, o consolo e a comemoração de cada um
            val ci = (t % 8.5f) / 8.5f; val check = if (!party && !sleepy && !sad && ci > 0.8f) sin(PIf * (ci - 0.8f) / 0.2f) else 0f
            val headY = when (kind) {
                "planta" -> -12f; "patinho" -> -14f; "robo" -> -22f; "coelho" -> -18f
                "ursinho", "k9" -> -18f; "pastor" -> -20f; "tartaruga" -> -12f; "leaozinho" -> -19f; "porquinho" -> -13f
                "pombo", "polvo", "peixinho" -> -15f; "pixel", "pinguim", "abelha" -> -16f
                else -> -17f
            }
            val bub = if (sad) 0.85f + 0.15f * sin(t * 3f) else check
            if (bub > 0.02f) {
                val bx = 6.5f; val by = headY - 5f - (if (sad) sin(t * 2f) * 0.6f else (1f - bub) * 2f)
                oval(Color.White.copy(alpha = bub), bx - 3.6f, by - 3f, 7.2f, 6f)
                drawOval(Color(0x401D1670).copy(alpha = 0.25f * bub), Offset(bx - 3.6f, by - 3f), Size(7.2f, 6f), style = stroke(0.3f))
                drawCircle(Color.White.copy(alpha = bub), 0.8f, Offset(bx - 2.6f, by + 3.2f)); drawCircle(Color.White.copy(alpha = bub), 0.45f, Offset(bx - 3.8f, by + 4.4f))
                drawPath(heart(bx, by + 0.3f, 1.5f * (1f + 0.12f * sin(t * 6f))), Color(0xFFFF6B86).copy(alpha = bub))
            }
            if (party) when (kind) {
                "planta" -> for (i in 0 until 5) {
                    val ph = (t * 0.7f + i / 5f) % 1f; val px = 2f + ph * 18f; val py = -19f - sin(PIf * ph) * 7f + ph * 6f
                    rotate((t * 3f + i) * DEG, pivot = Offset(px, py)) { oval(Color(0xFFFF8FA3).copy(alpha = 1f - ph), px - 1f, py - 0.55f, 2f, 1.1f) }
                }
                "patinho" -> { val q = (t * 3f) % 1f; for (a in listOf(-0.5f, 0f, 0.5f)) line(Color(0xFFFF8A2B).copy(alpha = 1f - q), 8.6f + cos(a) * (1f + q * 2f), -9.8f + sin(a) * (1f + q * 2f), 8.6f + cos(a) * (2f + q * 2.5f), -9.8f + sin(a) * (2f + q * 2.5f), 0.4f) }
                "gatinho" -> {
                    val w = sin(t * 8f) * 0.6f
                    drawCircle(Color(0xFFF7B36E), 1.7f, Offset(4.6f, -6.6f + w)); drawCircle(Color(0xFFFFB3C7), 0.7f, Offset(4.6f, -6.3f + w))
                    for (d in listOf(-0.9f, 0f, 0.9f)) drawCircle(Color(0xFFFFB3C7), 0.32f, Offset(4.6f + d, -7.6f + w))
                    val hp = (t * 0.8f) % 1f; drawPath(heart(9f + sin(t * 2f), -20f - hp * 4f, 1.2f), Color(0xFFFF6B86).copy(alpha = sin(PIf * hp)))
                }
                "coruja" -> for (i in 0 until 2) {
                    val ph = (t * 0.6f + i / 2f) % 1f; val nx = 5f + ph * 10f; val ny = -14f - ph * 8f; val c = Ink.copy(alpha = sin(PIf * ph))
                    drawCircle(c, 0.8f, Offset(nx, ny)); line(c, nx + 0.7f, ny, nx + 0.7f, ny - 3f, 0.35f); line(c, nx + 0.7f, ny - 3f, nx + 2f, ny - 2.4f, 0.35f)
                }
                "robo" -> for (i in 0 until 4) { val ph = (t * 1.4f + i / 4f) % 1f; drawCircle(Mint.copy(alpha = 1f - ph), 0.45f, Offset(ph * 12f, -20.6f - sin(PIf * ph) * 5f)) }
                "coelho" -> for (i in 0 until 3) { val ph = (t * 0.9f + i / 3f) % 1f; star4(-4f + i * 5f, -24f - ph * 4f, 1.3f * sin(PIf * ph), Color(0xFFFFD66E).copy(alpha = sin(PIf * ph))) }
                "dragao" -> { val ph = (t * 0.9f) % 1f; drawOval(Color(0xFFAAA5BE).copy(alpha = (1f - ph) * 0.9f), Offset(13f + ph * 6f - (1.2f + ph * 2f), -11.5f - ph * 3f - (0.8f + ph * 1.4f)), Size(2f * (1.2f + ph * 2f), 2f * (0.8f + ph * 1.4f)), style = Stroke(0.5f)) }
                // comemorações dos bichinhos novos
                "ursinho" -> for (i in 0 until 3) {   // cruzinhas de saúde subindo
                    val ph = (t * 0.7f + i / 3f) % 1f; val a = sin(PIf * ph); val cx = 4f + i * 4.5f; val cy = -17f - ph * 7f
                    for ((w, c) in listOf(1.5f to Color.White.copy(alpha = a), 0.8f to Color(0xFFF05260).copy(alpha = a))) { line(c, cx - 1.1f, cy, cx + 1.1f, cy, w); line(c, cx, cy - 1.1f, cx, cy + 1.1f, w) }
                }
                "pastor", "leaozinho" -> {   // latido / rugidinho
                    val q = (t * 2.6f) % 1f; val isP = kind == "pastor"; val ox = if (isP) 8.6f else 5.6f; val oy = if (isP) -11f else -10.4f
                    val c = (if (isP) Color(0xFF3A2A22) else Color(0xFFD26A22)).copy(alpha = 1f - q)
                    for (a in listOf(-0.5f, 0f, 0.5f)) line(c, ox + cos(a) * (1f + q * 2f), oy + sin(a) * (1f + q * 2f), ox + cos(a) * (2f + q * 2.5f), oy + sin(a) * (2f + q * 2.5f), 0.45f)
                }
                "k9" -> {   // fareja: baforadinhas saindo do focinho e o brilho do distintivo
                    for (i in 0 until 3) { val ph = (t * 1.2f + i / 3f) % 1f; drawCircle(Color(0xFFAAA5BE).copy(alpha = (1f - ph) * 0.7f), 0.4f + ph * 0.8f, Offset(7.4f + ph * 5f, -11.2f - sin(PIf * ph) * 1.5f)) }
                    star4(3.2f, -7.6f, 1.3f * abs(sin(t * 4f)), Color(0xF2FFECA0))
                }
                "tartaruga" -> {   // a lâmpada da ideia acende sobre a cabeça
                    val a = 0.6f + 0.4f * sin(t * 6f); val lbx = 9f; val lby = -15.5f
                    drawCircle(Brush.radialGradient(listOf(Spark.copy(alpha = 0.6f * a), Spark.copy(alpha = 0f)), center = Offset(lbx, lby), radius = 4f), 4f, Offset(lbx, lby))
                    drawCircle(Spark, 1.6f, Offset(lbx, lby)); drawCircle(Color.White.copy(alpha = 0.8f), 0.5f, Offset(lbx - 0.5f, lby - 0.5f))
                    drawPath(roundRect(lbx - 0.8f, lby + 1.2f, 1.6f, 1.1f, 0.3f), Color(0xFF9A9EB4))
                    for (i in 0 until 5) { val an = -PIf * 0.9f + i * PIf * 0.2f; line(Gold.copy(alpha = a), lbx + cos(an) * 2.2f, lby + sin(an) * 2.2f, lbx + cos(an) * 3.2f, lby + sin(an) * 3.2f, 0.35f) }
                }
                "castor" -> for (i in 0 until 3) {   // contas: "+" e "=" subindo
                    val ph = (t * 0.6f + i / 3f) % 1f; val a = sin(PIf * ph); val x2 = 5f + i * 4f; val y2 = -17f - ph * 6f
                    val c = (if (i % 2 == 1) Color(0xFF2E9E72) else Color(0xFF3A2FC4)).copy(alpha = a)
                    if (i % 2 == 1) { line(c, x2 - 1f, y2, x2 + 1f, y2, 0.5f); line(c, x2, y2 - 1f, x2, y2 + 1f, 0.5f) } else { line(c, x2 - 1f, y2 - 0.45f, x2 + 1f, y2 - 0.45f, 0.5f); line(c, x2 - 1f, y2 + 0.45f, x2 + 1f, y2 + 0.45f, 0.5f) }
                }
                "porquinho" -> {   // a moeda cai girando dentro da fenda e brilha
                    val ph = (t * 0.9f) % 1f
                    if (ph < 0.6f) { val y2 = -22f + ph / 0.6f * 10.6f; scale(maxOf(0.2f, abs(cos(t * 9f))), 1f, pivot = Offset(-1.7f, y2)) { drawCircle(Color(0xFFB57A0C), 1.7f, Offset(-1.7f, y2)); drawCircle(Color(0xFFFFD877), 1.3f, Offset(-1.7f, y2)) } }
                    else { val u = sin(PIf * (ph - 0.6f) / 0.4f); star4(-1.7f, -12f, 2.2f * u, Gold.copy(alpha = u)) }
                }
                "pombo" -> for (i in 0 until 2) { val ph = (t * 0.7f + i / 2f) % 1f; drawPath(heart(10f + ph * 4f + sin(t * 3f + i), -13f - ph * 8f, 0.9f), Color(0xFFFF6B86).copy(alpha = sin(PIf * ph))) }
                "minhoca" -> {   // o livrinho aberto flutuando com brilho
                    val lby = -20f + sin(t * 2f)
                    translate(9f, lby) { rotate(-0.1f * DEG, pivot = Offset.Zero) { for (s in listOf(-1f, 1f)) {
                        val pg = Path().apply { moveTo(0f, 0.8f); quadraticTo(s * 1.4f, 0f, s * 2.8f, 0.6f); lineTo(s * 2.8f, -1.6f); quadraticTo(s * 1.4f, -2.2f, 0f, -1.4f); close() }
                        drawPath(pg, Color.White); drawPath(pg, Color(0x9926215C), style = stroke(0.25f))
                        for (kk in 0 until 2) line(Color(0xB36F8FE0), s * 0.6f, -0.9f + kk * 0.7f, s * 2.2f, -0.7f + kk * 0.7f, 0.18f)
                    } } }
                    star4(12.2f, lby - 2.4f, 1.2f * abs(sin(t * 4f)), Gold.copy(alpha = 0.9f))
                }
                "pixel" -> for (i in 0 until 2) {   // coraçõezinhos de pixel
                    val ph = (t * 0.6f + i / 2f) % 1f; val a = sin(PIf * ph); val hx = 6f + i * 5f; val hy = -19f - ph * 6f
                    listOf(".x.x.", "xxxxx", ".xxx.", "..x..").forEachIndexed { r, row -> row.forEachIndexed { q, ch -> if (ch == 'x') drawRect(Color(0xFFFF5C78).copy(alpha = a), Offset(hx + q * 0.7f, hy + r * 0.7f), Size(0.72f, 0.72f)) } }
                }
                "peixinho" -> for (i in 0 until 4) { val ph = (t * 0.8f + i / 4f) % 1f; drawCircle(Color(0xFF6FB0FF).copy(alpha = 1f - ph), 0.4f + ph * 0.5f, Offset(-1.5f + i + sin(t * 3f + i) * 0.8f, -13.6f - ph * 8f), style = Stroke(0.28f)) }
                "cachorrinho" -> {   // um ossinho girando no ar
                    val ph = (t * 0.5f) % 1f; val a = sin(PIf * ph); val oc = Color(0xFF967859).copy(alpha = 0.8f * a)
                    translate(9f, -18f - ph * 5f) { rotate(t * 3f * DEG, pivot = Offset.Zero) {
                        drawRect(Color.White.copy(alpha = a), Offset(-1.6f, -0.45f), Size(3.2f, 0.9f)); drawRect(oc, Offset(-1.6f, -0.45f), Size(3.2f, 0.9f), style = Stroke(0.25f))
                        for ((ox, oy) in listOf(-1.7f to -0.55f, -1.7f to 0.55f, 1.7f to -0.55f, 1.7f to 0.55f)) { drawCircle(Color.White.copy(alpha = a), 0.6f, Offset(ox, oy)); drawCircle(oc, 0.6f, Offset(ox, oy), style = Stroke(0.25f)) }
                    } }
                }
                "pinguim" -> for (i in 0 until 3) { val ph = (t * 0.8f + i / 3f) % 1f; star4(5f + i * 4f, -17f - ph * 5f, 1.4f * sin(PIf * ph), Color(0xFF96D2FF).copy(alpha = sin(PIf * ph))) }
                "abelha" -> for (i in 0 until 3) { val ph = (t * 0.7f + i / 3f) % 1f; drawPath(heart(7f + i * 3.5f, -16f - ph * 6f, 0.9f), Color(0xFFFFC93C).copy(alpha = sin(PIf * ph))) }
                "polvo" -> for (i in 0 until 4) { val ph = (t * 0.7f + i / 4f) % 1f; drawCircle(Color(0xFF6FB0FF).copy(alpha = 1f - ph), 0.45f + ph * 0.4f, Offset(4f + i * 2f + sin(t * 2f + i) * 0.6f, -14f - ph * 8f), style = Stroke(0.28f)) }
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

/**
 * Papéis do armário 2.0 (um lado por vez, já recortado na página; side -1 = esquerda). Tudo bem leve,
 * concentrado no alto e no pé da página, para não brigar com o rosto no meio.
 */
private val PAGES2 = setOf("receituario", "mapa", "boletim", "timbrado", "livro-caixa", "planilha", "extrato", "diario-oficial", "caligrafia", "terminal", "formulario")

private fun dashed(w: Float, on: Float, off: Float) = Stroke(w, cap = StrokeCap.Butt, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(on, off)))

private fun DrawScope.drawPagePattern2(kind: String, page: Path, side: Float, t: Float) {
    fun X(v: Float) = 50f + side * v   // v = distância da lombada (0 no meio do livro, 38 na borda)
    val lo = minOf(X(4f), X(35f)); val hi = maxOf(X(4f), X(35f))
    fun pen(c: Color, w: Float, block: Path.() -> Unit) = drawPath(Path().apply(block), c, style = stroke(w))
    when (kind) {
        "receituario" -> {
            // receituário: faixa verde-água no alto, o "Rx" na esquerda e a linha da assinatura com carimbo na direita
            drawRect(Color(0x383CAAA0), Offset(10f, 36f), Size(80f, 8.6f)); line(Color(0x8C288C82), lo, 44.6f, hi, 44.6f, 0.3f, false)
            if (side < 0f) {
                coverText("Rx", X(30f), 48.6f, 3.4f, 0xBF1E786E.toInt())
                var y = 78f; while (y < 86f) { line(Color(0x595A96C8), X(34f), y, X(6f), y + 0.4f, 0.22f, false); y += 2.6f }
            } else {
                line(Color(0x803C3C5A), X(8f), 80f, X(30f), 79.4f, 0.25f, false)
                pen(Color(0x801F4FD8), 0.3f) { moveTo(X(10f), 79.4f); cubicTo(X(13f), 76.6f, X(15f), 81f, X(18f), 78.2f); cubicTo(X(20f), 76.8f, X(22f), 79.6f, X(26f), 78f) }
                drawCircle(Color(0x66288C82), 2.2f, Offset(X(28f), 74.6f), style = Stroke(0.35f)); line(Color(0x66288C82), X(26.6f), 74.6f, X(29.4f), 74.6f, 0.3f, false)
            }
        }
        "mapa" -> {
            // mapa de campanha: papel envelhecido, curvas de nível, rota tracejada e o X do objetivo; rosa dos ventos na direita
            drawPath(page, Color(0x52E4C88C))
            for ((cx, cy) in listOf(X(26f) to 48f, X(12f) to 80f)) { var r = 2f; while (r < 9f) { rotate(0.3f * 180f / PIf, pivot = Offset(cx, cy)) { drawOval(Color(0x52788C46), Offset(cx - r * 1.3f, cy - r * 0.8f), Size(r * 2.6f, r * 1.6f), style = Stroke(0.22f)) }; r += 2f } }
            drawPath(Path().apply { moveTo(X(34f), 84f); cubicTo(X(26f), 76f, X(36f), 70f, X(30f), 62f) }, Color(0x80C8323C), style = dashed(0.35f, 1f, 0.8f))
            if (side < 0f) { line(Color(0xB3C8323C), X(31.2f), 60.8f, X(28.8f), 63.2f, 0.45f); line(Color(0xB3C8323C), X(28.8f), 60.8f, X(31.2f), 63.2f, 0.45f) }
            else {
                val cx = X(30f); val cy = 47.4f
                for ((a, l) in listOf(0f to 3f, PIf / 2f to 2.2f, PIf to 2.2f, -PIf / 2f to 2.2f)) {
                    drawPath(Path().apply { moveTo(cx, cy); lineTo(cx + cos(a - 0.3f) * 0.8f, cy + sin(a - 0.3f) * 0.8f); lineTo(cx + cos(a - PIf / 2f) * l, cy + sin(a - PIf / 2f) * l); lineTo(cx + cos(a + 0.3f) * 0.8f, cy + sin(a + 0.3f) * 0.8f); close() }, if (a == 0f) Color(0x99C8323C) else Color(0x805A4628))
                }
                coverText("N", cx, cy - 4f, 1.6f, 0x995A4628.toInt())
            }
        }
        "boletim" -> {
            // boletim de ocorrência: cabeçalho cinza com brasão, campos em caixinhas e linhas de relato no pé
            drawRect(Color(0x2E465A82), Offset(10f, 36f), Size(80f, 9f)); line(Color(0x8C32466E), lo, 45f, hi, 45f, 0.35f, false)
            if (side < 0f) drawPath(Path().apply { moveTo(X(31f), 40.6f); lineTo(X(27f), 40.6f); lineTo(X(27f), 43f); quadraticTo(X(29f), 44.8f, X(31f), 43f); close() }, Color(0x7332466E))
            else coverText("B.O.", X(28f), 42.4f, 2.2f, 0x9932466E.toInt(), family = android.graphics.Typeface.SANS_SERIF)
            for ((a, b) in listOf(34f to 22f, 21f to 6f)) drawRect(Color(0x5932466E), Offset(minOf(X(a), X(b)), 47f), Size(abs(X(a) - X(b)), 3f), style = Stroke(0.22f))
            var y = 78f; while (y < 87f) { line(Color(0x4D32466E), X(34f), y, X(5f), y + 0.5f, 0.2f, false); y += 2.4f }
        }
        "timbrado" -> {
            // papel timbrado: emblema e faixa dupla no alto, rodapé com filete fino
            line(Color(0x8C1F4FD8), lo, 44.2f, hi, 44.2f, 0.5f, false); line(Color(0xB3F5B83D), lo, 45.2f, hi, 45.2f, 0.25f, false)
            if (side < 0f) {
                drawCircle(Brush.radialGradient(listOf(Color(0x995A82F0), Color(0x991F4FD8)), center = Offset(X(30f), 40.6f), radius = 2.4f), 2f, Offset(X(30f), 41f))
                drawCircle(Color.White.copy(alpha = 0.8f), 0.8f, Offset(X(30f), 41f))
                for (y in listOf(40.2f, 41.8f)) line(Color(0x661F4FD8), X(26.6f), y, X(18f), y, 0.4f, false)
            }
            line(Color(0x591F4FD8), X(34f), 81.6f, X(6f), 82.4f, 0.22f, false)
            line(Color(0x401F4FD8), X(26f), 83.4f, X(14f), 83.6f, 0.3f, false)
        }
        "livro-caixa" -> {
            // livro-caixa: papel esverdeado, pauta azul e colunas vermelhas de débito e crédito
            drawPath(page, Color(0x66D2ECD6))
            var y = 44f; while (y < 88f) { line(Color(0x665A96C8), 10f, y, 90f, y + 0.5f, 0.2f, false); y += 2.8f }
            for (v in listOf(6f, 11f, 33f)) { line(Color(0x80DC4650), X(v), 38f, X(v), 90f, 0.22f, false); line(Color(0x80DC4650), X(v + 0.6f), 38f, X(v + 0.6f), 90f, 0.22f, false) }
            if (side > 0f) for ((yy, w) in listOf(47f to 3f, 52.6f to 2.2f, 80.6f to 3.4f)) line(Color(0x66283250), X(10f), yy, X(10f - w), yy, 0.35f, false)
        }
        "planilha" -> {
            // planilha: grade cinza, cabeçalho de colunas e linhas em cinza e uma célula selecionada em verde
            var y = 38f; while (y < 90f) { line(Color(0x4D788296), 10f, y, 90f, y, 0.18f, false); y += 3f }
            var v = 2f; while (v < 39f) { line(Color(0x4D788296), X(v), 36f, X(v), 90f, 0.18f, false); v += 6f }
            drawRect(Color(0x3396A0B4), Offset(10f, 36f), Size(80f, 5f))
            if (side < 0f) drawRect(Color(0x3396A0B4), Offset(10f, 41f), Size(4f, 50f))
            else { drawRect(Color(0xCC1E9650), Offset(X(26f), 77f), Size(6f, 3f), style = Stroke(0.4f)); drawRect(Color(0xD91E9650), Offset(X(32f) - 0.5f, 79.5f), Size(1f, 1f)) }
        }
        "extrato" -> {
            // extrato bancário: faixas cinza alternadas, descrição à esquerda e valores à direita (um vermelho)
            drawRect(Color(0x2E1F4FD8), Offset(10f, 36f), Size(80f, 7.6f))
            var y = 45f; var i = 0
            while (y < 88f) {
                if (i % 2 == 0) drawRect(Color(0x1A788296), Offset(10f, y - 1.2f), Size(80f, 3f))
                if (i < 3 || y > 76f) {
                    line(Color(0x593C3C50), X(if (side < 0f) 34f else 18f), y + 0.2f, X(if (side < 0f) 24f else 12f), y + 0.2f, 0.4f, false)
                    if (side > 0f) line(if (i % 3 == 1) Color(0x8CC8323C) else Color(0x801E8250), X(33f), y + 0.2f, X(28f), y + 0.2f, 0.45f, false)
                }
                y += 3f; i++
            }
        }
        "diario-oficial" -> {
            // diário oficial: cabeçalho com filete grosso e fino, brasão, e colunas de texto miúdo no pé
            line(Color(0x8C14141E), lo, 44.4f, hi, 44.4f, 0.6f, false); line(Color(0x6614141E), lo, 45.4f, hi, 45.4f, 0.2f, false)
            if (side < 0f) {
                drawCircle(Color(0x73286E3C), 1.8f, Offset(X(30f), 41.4f)); drawCircle(Color(0x99F5C451), 0.9f, Offset(X(30f), 41.4f))
                for (y in listOf(40.6f, 42.2f)) line(Color(0x6614141E), X(27f), y, X(10f), y, 0.6f, false)
            } else coverText("DIÁRIO OFICIAL", X(20f), 41.6f, 1.7f, 0x8C14141E.toInt())
            for ((a, b) in listOf(34f to 21f, 19f to 6f)) { var y = 74f; while (y < 88f) { line(Color(0x4728283C), X(a), y, X(b + ((y * 7f) % 3f)), y, 0.35f, false); y += 1.4f } }
            line(Color(0x4D28283C), X(20f), 72f, X(20f), 88f, 0.15f, false)
        }
        "caligrafia" -> {
            // caderno de caligrafia: grupos de quatro linhas (a do meio tracejada) e guias inclinadas
            for (y0 in listOf(42f, 76f)) {
                for ((dy, c, dash) in listOf(Triple(0f, Color(0x66E84669), false), Triple(2.4f, Color(0x735A96F0), true), Triple(4.8f, Color(0x8C5A96F0), false), Triple(7.2f, Color(0x66E84669), false))) {
                    val p = Path().apply { moveTo(10f, y0 + dy); lineTo(90f, y0 + dy + 0.8f) }
                    drawPath(p, c, style = if (dash) dashed(0.22f, 0.8f, 0.6f) else Stroke(0.22f))
                }
                var x = 12f; while (x < 90f) { line(Color(0x335A96F0), x, y0 + 7.6f, x + 2.4f, y0 - 0.2f, 0.18f, false); x += 4f }
            }
            if (side < 0f) pen(Color(0x801F4FD8), 0.3f) { moveTo(X(32f), 81f); cubicTo(X(33f), 78f, X(30f), 77.6f, X(30.4f), 80.6f); cubicTo(X(30f), 81.4f, X(29f), 80f, X(28.4f), 77.2f); lineTo(X(27.6f), 81f) }
        }
        "terminal" -> {
            // terminal: fundo escuro (mais claro no meio, onde fica o rosto), linhas de comando verdes e o cursor piscando
            drawPath(page, Brush.radialGradient(0f to Color(0x8C284032), 0.14f to Color(0x8C284032), 1f to Color(0xE00A120E), center = Offset(50f, 64f), radius = 44f))
            if (side < 0f) for ((c, x) in listOf(Color(0xFFFF5F57) to 31f, Color(0xFFFEBC2E) to 29.4f, Color(0xFF28C840) to 27.8f)) drawCircle(c, 0.55f, Offset(X(x), 42.6f))
            val gr = Color(0xB350F082)
            val rows = if (side < 0f) listOf(Triple(47f, 33f, 26f), Triple(49.6f, 33f, 18f), Triple(78.6f, 33f, 22f), Triple(81.2f, 33f, 28f)) else listOf(Triple(47f, 6f, 20f), Triple(78.6f, 6f, 26f), Triple(81.2f, 6f, 14f))
            for ((y, a, b) in rows) {
                line(gr, X(a), y, X(b), y, 0.55f, false)
                if (side < 0f) coverText(">", X(34.6f), y, 1.8f, 0xB350F082.toInt(), family = android.graphics.Typeface.MONOSPACE)
            }
            if (side > 0f && (t % 1.1f) < 0.6f) drawRect(gr, Offset(X(16f) - 0.5f, 80.2f), Size(1.2f, 1.8f))
        }
        "formulario" -> {
            // formulário: campos com rótulo e linha, caixinhas de marcar (uma marcada) no pé
            drawRect(Color(0x247882A0), Offset(10f, 36f), Size(80f, 8.4f))
            for (y in listOf(47.4f, 50.6f)) { line(Color(0x663C4664), X(34f), y - 0.8f, X(30f), y - 0.8f, 0.35f, false); line(Color(0x593C4664), X(29f), y, X(6f), y, 0.2f, false) }
            for ((i, y) in listOf(0 to 78f, 1 to 81.4f, 2 to 84.8f)) {
                val bx = minOf(X(33f), X(31f))
                drawRect(Color(0x803C4664), Offset(bx, y - 1f), Size(2f, 2f), style = Stroke(0.25f))
                line(Color(0x4D3C4664), X(29.4f), y, X(if (side < 0f) 18f else 20f), y, 0.35f, false)
                if (i == (if (side < 0f) 0 else 1)) pen(Color(0xCC1F4FD8), 0.35f) { moveTo(bx + 0.4f, y); lineTo(bx + 0.9f, y + 0.6f); lineTo(bx + 1.7f, y - 0.8f) }
            }
        }
    }
}

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
            in PAGES2 -> drawPagePattern2(kind, page, if (page === left) -1f else 1f, t)
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

/**
 * Capas do armário 2.0. A capa só aparece nas lombadas (x 8,5 a 12 e 88 a 91,5) e numa faixinha no pé,
 * então o desenho de cada uma se concentra ali.
 */
private val COVER_PAT2 = setOf("pijama", "deserto", "colete", "processo", "nota", "bancario", "plenario", "espiral", "circuito", "pasta", "girassol", "ondas", "melancia", "cosmos")

/** Manchas arredondadas de camuflado, sempre no mesmo lugar. */
private fun DrawScope.blotches(seed: Int, cols: List<Color>, n: Int) {
    for (i in 0 until n) {
        val x = 6f + ((i * seed) % 88); val y = 41f + ((i * 29) % 54); val r = 2f + ((i * 7) % 5) * 0.5f; val a = i * 1.3f
        val p = Path().apply {
            moveTo(x + r, y)
            for (k in 1..6) { val ang = k * PIf / 3f; val rr = r * (0.7f + 0.3f * sin(a + k * 2.1f)); quadraticTo(x + cos(ang - PIf / 6f) * rr * 1.25f, y + sin(ang - PIf / 6f) * rr * 0.9f, x + cos(ang) * rr, y + sin(ang) * rr * 0.75f) }
            close()
        }
        drawPath(p, cols[i % cols.size])
    }
}

private fun DrawScope.coverText(s: String, x: Float, y: Float, size: Float, color: Int, bold: Boolean = true, family: android.graphics.Typeface = android.graphics.Typeface.SERIF) = drawIntoCanvas { c ->
    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply { this.color = color; textSize = size; textAlign = android.graphics.Paint.Align.CENTER; typeface = android.graphics.Typeface.create(family, if (bold) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL) }
    c.nativeCanvas.drawText(s, x, y + size * 0.35f, paint)
}

private fun DrawScope.drawCoverPattern2(t: Float, pat: String, cover: Path) {
    val spines = listOf(10.2f, 89.8f)
    when (pat) {
        "pijama" -> {
            // pijama cirúrgico: trama de algodão, bolso com caneta na lombada esquerda e crachá na direita
            var x = -50f; while (x < 100f) { line(Color.White.copy(alpha = 0.05f), x, 40f, x + 54f, 95f, 0.25f, false); x += 1.2f }
            val pk = roundRect(8.9f, 62f, 2.8f, 6.4f, 0.6f)
            drawPath(pk, Color(0x59E1FFFA)); drawPath(pk, Color(0xE6144E4A), style = stroke(0.4f)); line(Color(0x8CE1FFFA), 9f, 62.7f, 11.6f, 62.7f, 0.2f, false)
            drawRect(vg(listOf(Color(0xFF5B8CF0), Color(0xFF2F5FC4)), 58.6f, 63f), Offset(10.3f, 58.6f), Size(0.9f, 4f)); line(Color(0xFFC9D2E8), 11.3f, 59f, 11.3f, 61.6f, 0.25f, false)
            val bd = roundRect(88.6f, 52f, 2.6f, 3.8f, 0.4f); drawPath(bd, Color.White); drawPath(bd, Color(0x99144E4A), style = stroke(0.25f))
            drawRect(Color(0xFF2F8FD8), Offset(88.6f, 52f), Size(2.6f, 0.9f)); line(Color(0x8C28283C), 89.1f, 53.9f, 90.7f, 53.9f, 0.25f, false); line(Color(0x6628283C), 89.1f, 54.9f, 90.2f, 54.9f, 0.25f, false)
            line(Color(0xE6C8CDDC), 89.9f, 49.6f, 89.9f, 52f, 0.25f, false)
        }
        "deserto" -> {
            // camuflado do deserto: manchas areia, terra e café, com pontinhos de areia
            blotches(43, listOf(Color(0x808C683C), Color(0x8CF0E2C0), Color(0x80B28C5C)), 34)
            for (i in 0 until 30) drawCircle(Color(0x595A4224), 0.22f, Offset(7f + ((i * 31) % 86), 42f + ((i * 17) % 52)))
        }
        "colete" -> {
            // colete tático: tecido grosso, fitas de fixação na lombada, bolso com aba e a faixa "POLÍCIA" refletiva
            var x = 6f; while (x < 96f) { line(Color.White.copy(alpha = 0.04f), x, 40f, x, 95f, 0.3f, false); x += 1.6f }
            for (sx in spines) { var y = 62f; while (y < 88f) { drawRect(Color.Black.copy(alpha = 0.35f), Offset(sx - 1.7f, y), Size(3.4f, 1.3f)); line(Color.White.copy(alpha = 0.14f), sx - 1.7f, y + 0.15f, sx + 1.7f, y + 0.15f, 0.2f, false); y += 3.4f } }
            for (sx in spines) {
                val fl = roundRect(sx - 1.5f, 44.6f, 3f, 1.8f, 0.4f); drawPath(fl, Color(0x990A0E18)); drawPath(fl, Color.White.copy(alpha = 0.15f), style = stroke(0.2f))
                drawPath(roundRect(sx - 1.7f, 53.6f, 3.4f, 3.6f, 0.3f), vg(listOf(Color(0xFFF4F4F0), Color(0xFFC8CAD0)), 53.6f, 57.2f))
                for (i in 0 until 4) line(Color(0xFF1A2236), sx - 1.2f + i * 0.8f, 54.6f, sx - 0.8f + i * 0.8f, 54.6f, 0.5f, false)
                line(Color(0x991A2236), sx - 1.2f, 56.2f, sx + 1.2f, 56.2f, 0.3f, false)
            }
        }
        "processo" -> {
            // capa de processo: cartolina com fibras, etiqueta de autuação na lombada esquerda e grampos na direita
            for (i in 0 until 30) line(Color(0x2E8C6428), 7f + ((i * 37) % 86), 42f + ((i * 23) % 50), 8.4f + ((i * 37) % 86), 42.3f + ((i * 23) % 50), 0.25f)
            val lb = roundRect(8.9f, 49f, 2.9f, 10f, 0.3f); drawPath(lb, Color.White); drawPath(lb, Color(0x80785A28), style = stroke(0.25f))
            drawRect(Color(0xFFD8434E), Offset(8.9f, 49f), Size(2.9f, 1.2f))
            for (y in listOf(51.6f, 53.2f, 54.8f, 56.4f)) line(Color(0x733C3C50), 9.4f, y, if (y == 56.4f) 10.4f else 11.3f, y, 0.25f, false)
            for (y in listOf(48f, 82f)) {
                val gr = roundRect(88.4f, y, 2.8f, 3f, 0.5f); drawPath(gr, vg(listOf(Color.White, Color(0xFFB8BCC8), Color(0xFF8E92A6)), y, y + 3f)); drawPath(gr, Color(0x99464650), style = stroke(0.25f))
                line(Color(0xB346465A), 89.1f, y + 1.5f, 90.5f, y + 1.5f, 0.35f); drawCircle(Color.White.copy(alpha = 0.9f), 0.25f, Offset(88.9f, y + 0.6f))
            }
            drawCircle(Color(0x40D8434E), 1.3f, Offset(10.3f, 76f)); drawCircle(Color(0x8CD8434E), 1.3f, Offset(10.3f, 76f), style = Stroke(0.25f))
        }
        "nota" -> {
            // nota fiscal: cabeçalho azul, linhas de campos, picote na esquerda e código de barras na direita
            for (sx in spines) { drawRect(Color(0xB3326ED2), Offset(sx - 1.8f, 44f), Size(3.6f, 5f)); line(Color(0x991E4696), sx - 1.2f, 46.5f, sx + 1f, 46.5f, 0.4f, false) }
            var y = 51f; while (y < 88f) { for (sx in spines) line(Color(0x73464664), sx - 1.7f, y, sx + 1.7f, y, 0.3f, false); y += 2.4f }
            y = 50f; while (y < 89f) { drawCircle(Color(0x73787364), 0.2f, Offset(11.5f, y)); y += 1.2f }
            val bars = floatArrayOf(0.5f, 0.2f, 0.3f, 0.6f, 0.2f, 0.4f, 0.2f, 0.5f, 0.3f, 0.2f, 0.6f, 0.2f, 0.3f, 0.4f, 0.2f, 0.5f, 0.2f, 0.3f, 0.6f, 0.2f, 0.4f, 0.3f)
            drawRect(Color.White, Offset(88.3f, 65.4f), Size(3.1f, 19.4f))
            var by = 66f; bars.forEachIndexed { i, b -> if (i % 2 == 0) drawRect(Color(0xFF1A1A22), Offset(88.7f, by), Size(2.3f, b)); by += b + 0.25f }
        }
        "bancario" -> {
            // azul de agência: risca finíssima, filete dourado rente às páginas e a moeda-emblema nas lombadas
            var x = 6f; while (x < 96f) { line(Color(0x24C8DCFF), x, 40f, x, 95f, 0.15f, false); x += 1.6f }
            line(Color(0xE6F5C451), 11.4f, 45f, 11.4f, 88f, 0.35f, false); line(Color(0xE6F5C451), 88.6f, 45f, 88.6f, 88f, 0.35f, false)
            for (cx in listOf(9.8f, 90.2f)) {
                drawCircle(Brush.radialGradient(listOf(Color(0xFFFFF0B8), Color(0xFFC98A12)), center = Offset(cx - 0.4f, 56.6f), radius = 1.7f), 1.4f, Offset(cx, 57f))
                drawCircle(Color(0x99784600), 1.4f, Offset(cx, 57f), style = Stroke(0.2f)); drawCircle(Color(0xB3965F0A), 0.85f, Offset(cx, 57f), style = Stroke(0.2f))
                line(Color(0xD9965F0A), cx, 56.4f, cx, 57.6f, 0.3f, false)
            }
        }
        "plenario" -> {
            // verde do plenário: couro de poltrona com grão, filetes dourados e a fileira de tachas
            for (g in GRAIN) drawCircle(Color(0xFF001408).copy(alpha = g[3] * 1.2f), g[2] * 1.2f, Offset(if (g[0] < 50f) 8.5f + (g[0] - 15f) * 0.1f else 91.5f - (85f - g[0]) * 0.1f, g[1] + 6f))
            for (cx in listOf(9.6f, 90.4f)) for (y in listOf(47f, 49f, 82f, 84f)) line(Color(0xD9F5C451), cx - 1.2f, y, cx + 1.2f, y, 0.35f, false)
            for (cx in listOf(9.6f, 90.4f)) { var y = 53f; while (y < 80f) { drawCircle(Brush.radialGradient(listOf(Color(0xFFFFF0B8), Color(0xFFB47A10)), center = Offset(cx - 0.2f, y - 0.2f), radius = 0.65f), 0.55f, Offset(cx, y)); y += 3.2f } }
        }
        "espiral" -> {
            // caderno de espiral: furos na capa e a espiral de arame prateado na lombada esquerda
            var x = -50f; while (x < 100f) { line(Color.White.copy(alpha = 0.05f), x, 40f, x + 54f, 95f, 0.3f, false); x += 1.5f }
            var y = 46f
            while (y < 88f) {
                drawCircle(Color(0xBF3C140A), 0.45f, Offset(11f, y + 0.3f))
                rotate(-0.25f * 180f / PIf, pivot = Offset(9.4f, y)) {
                    drawArc(Color(0xCC505464), 18f, 144f, false, Offset(7.5f, y - 0.6f), Size(3.8f, 1.2f), style = stroke(0.4f))
                    drawArc(Color(0xFF6C7084), 189f, 162f, false, Offset(7.5f, y - 0.6f), Size(3.8f, 1.2f), style = stroke(0.6f))
                    drawArc(Color(0xFFE8EAF2), 189f, 162f, false, Offset(7.5f, y - 0.6f), Size(3.8f, 1.2f), style = stroke(0.3f))
                }
                y += 2.4f
            }
        }
        "circuito" -> {
            // placa de circuito: trilhas douradas com ilhas de solda e LEDs piscando
            val traces = listOf(
                listOf(9f to 45f, 9f to 53f, 11f to 55f, 11f to 63f), listOf(10.6f to 45f, 10.6f to 49f), listOf(9.2f to 66f, 9.2f to 74f, 11f to 76f, 11f to 86f),
                listOf(91f to 48f, 91f to 58f, 89f to 60f, 89f to 68f), listOf(89.4f to 72f, 89.4f to 80f, 91f to 82f, 91f to 88f), listOf(16f to 87.4f, 26f to 87f, 30f to 88f), listOf(84f to 87.4f, 74f to 87f, 70f to 88f),
            )
            for (pts in traces) {
                drawPath(Path().apply { moveTo(pts[0].first, pts[0].second); for (q in pts.drop(1)) lineTo(q.first, q.second) }, Color(0xF2FFCE5A), style = stroke(0.6f))
                for (q in listOf(pts.first(), pts.last())) { drawCircle(Color(0xFFF0C45A), 0.5f, Offset(q.first, q.second)); drawCircle(Color(0xFF135A30), 0.2f, Offset(q.first, q.second)) }
            }
            for (q in listOf(Quad(10.4f, 69f, Color(0xFFFF4D5E), 0f), Quad(89.6f, 63f, Color(0xFF5CFF8A), 1.7f), Quad(90f, 84.6f, Color(0xFF6FB0FF), 3.1f), Quad(10f, 82f, Color(0xFFFFD84A), 4.4f))) {
                val (x, y, c, ph) = q; val on = 0.35f + 0.65f * (if (sin(t * 3f + ph) > 0f) 1f else 0.3f)
                drawCircle(Brush.radialGradient(listOf(c.copy(alpha = 0.6f * on), c.copy(alpha = 0f)), center = Offset(x, y), radius = 2.6f), 2.6f, Offset(x, y))
                drawCircle(c.copy(alpha = on), 0.95f, Offset(x, y)); drawCircle(Color.White.copy(alpha = 0.8f), 0.2f, Offset(x - 0.2f, y - 0.2f))
            }
        }
        "pasta" -> {
            // pasta-arquivo: cartolina com fibra e o elástico preto passando pelas lombadas
            for (i in 0 until 34) drawCircle(Color.White.copy(alpha = 0.12f), 0.25f + (i % 3) * 0.15f, Offset(7f + ((i * 37) % 86), 42f + ((i * 23) % 50)))
            for (y in listOf(58f, 76f)) for ((a, b) in listOf(8f to 12.2f, 87.8f to 92f)) {
                drawRect(vg(listOf(Color(0xFF3A3A46), Color(0xFF15151C), Color(0xFF2A2A34)), y - 0.6f, y + 0.6f), Offset(a, y - 0.6f), Size(b - a, 1.2f))
                line(Color.White.copy(alpha = 0.3f), a, y - 0.4f, b, y - 0.4f, 0.15f, false)
            }
            val tab = roundRect(88.6f, 46f, 2.4f, 6f, 0.5f); drawPath(tab, Color.White.copy(alpha = 0.85f)); drawPath(tab, Color(0x80284678), style = stroke(0.25f))
            line(Color(0x99284678), 89.2f, 48f, 90.4f, 48f, 0.25f, false); line(Color(0x73284678), 89.2f, 49.4f, 90f, 49.4f, 0.25f, false)
        }
        "girassol" -> {
            // girassóis grandes, cortados pela borda como estampa (lombadas e faixa do pé)
            for (q in listOf(floatArrayOf(9.6f, 49f, 2.3f, 0f), floatArrayOf(11f, 62f, 2f, 1.4f), floatArrayOf(9.4f, 75f, 2.3f, 2.1f), floatArrayOf(10.8f, 87f, 2f, 3f), floatArrayOf(90.4f, 54f, 2.3f, 2.6f), floatArrayOf(89f, 67f, 2f, 3.8f), floatArrayOf(90.6f, 80f, 2.3f, 4.4f), floatArrayOf(22f, 90.6f, 2.2f, 5f), floatArrayOf(36f, 92.2f, 2f, 5.6f), floatArrayOf(64f, 92.2f, 2f, 6.2f), floatArrayOf(78f, 90.6f, 2.2f, 0.7f))) {
                val x = q[0]; val y = q[1]; val s = q[2]; val sw = sin(t * 1.4f + q[3]) * 0.1f
                for (k in 0 until 12) {
                    val a = k * PIf / 6f + sw; val px = x + cos(a) * 0.95f * s; val py = y + sin(a) * 0.95f * s
                    rotate(a * 180f / PIf, pivot = Offset(px, py)) {
                        drawOval(if (k % 2 == 1) Color(0xFFFFE04A) else Color(0xFFFFC21F), Offset(px - 0.6f * s, py - 0.27f * s), Size(1.2f * s, 0.54f * s))
                        drawOval(Color(0xB3BE6E00), Offset(px - 0.6f * s, py - 0.27f * s), Size(1.2f * s, 0.54f * s), style = Stroke(0.18f))
                    }
                }
                drawCircle(Brush.radialGradient(listOf(Color(0xFF8A4E1C), Color(0xFF3E1E06)), center = Offset(x - 0.15f * s, y - 0.15f * s), radius = 0.6f * s), 0.48f * s, Offset(x, y))
                for ((dx, dy) in listOf(-0.18f to -0.12f, 0.14f to 0.06f, -0.03f to 0.2f)) drawCircle(Color(0x99FFC878), 0.06f * s, Offset(x + dx * s, y + dy * s))
            }
        }
        "ondas" -> {
            // ondas do mar: cristas brancas bem marcadas, em arcos, correndo devagar, e espuma no pé
            var y = 45f; var i = 0
            while (y < 96f) {
                val ph = (t * 0.8f + i * 0.5f) % 1f
                val p = Path().apply { moveTo(2f, y); var x = 2f - ph * 4f + (i % 2) * 2f; while (x < 98f) { quadraticTo(x + 2f, y - 1.8f, x + 4f, y); x += 4f } }
                drawPath(p, Color.White.copy(alpha = 0.8f), style = stroke(0.55f))
                translate(0f, 0.9f) { drawPath(p, Color(0x590A3C6E), style = stroke(0.4f)) }
                y += 3.4f; i++
            }
            var fx = 14f; while (fx < 88f) { drawCircle(Color.White.copy(alpha = 0.85f), 0.5f, Offset(fx, 90.4f - abs(fx - 50f) * 0.05f + sin(fx + t * 2f) * 0.2f)); fx += 3f }
        }
        "melancia" -> {
            // melancia: casca verde-escura listrada e, na faixa do pé e nas lombadas, a fatia vermelha com sementes
            var x = 5f
            while (x < 96f) {
                val p = Path(); var y = 40f
                while (y <= 95f) { val xx = x + if ((y / 1.2f) % 2f < 1f) -0.5f else 0.5f; if (y == 40f) p.moveTo(xx, y) else p.lineTo(xx, y); y += 1.2f }
                drawPath(p, Color(0xBF0E3C16), style = stroke(1.3f)); x += 3.4f
            }
            // fatia: polpa vermelha seguindo a borda de baixo, com a linha branca da casca embaixo
            val sl = Path().apply {
                moveTo(8f, 84.6f); cubicTo(22f, 82f, 38f, 83f, 50f, 88.6f); cubicTo(62f, 83f, 78f, 82f, 92f, 84.6f)
                lineTo(92f, 87.6f); cubicTo(78f, 85f, 62f, 86f, 50f, 91.6f); cubicTo(38f, 86f, 22f, 85f, 8f, 87.6f); close()
            }
            drawPath(sl, vg(listOf(Color(0xFFFF6A78), Color(0xFFE8324A)), 83f, 91f))
            drawPath(Path().apply { moveTo(8f, 87.8f); cubicTo(22f, 85.2f, 38f, 86.2f, 50f, 91.8f); cubicTo(62f, 86.2f, 78f, 85.2f, 92f, 87.8f) }, Color(0xFFF2FBE4), style = stroke(0.6f))
            for ((a, side) in listOf(10.4f to 1f, 89.6f to -1f)) {
                drawRect(Color(0xFFF2FBE4), Offset(if (side > 0f) a - 0.5f else a, 44f), Size(0.5f, 42f))
                val rx0 = if (side > 0f) a else a - 2f
                drawRect(hg(if (side > 0f) listOf(Color(0xFFFF6A78), Color(0xFFE8324A)) else listOf(Color(0xFFE8324A), Color(0xFFFF6A78)), rx0, rx0 + 2f), Offset(rx0, 44f), Size(2f, 42f))
                var y = 48f; while (y < 84f) { val sx = a + side * 1f; rotate(side * 0.4f * 180f / PIf, pivot = Offset(sx, y)) { oval(Color(0xFF1A1210), sx - 0.28f, y - 0.5f, 0.56f, 1f) }; y += 3.6f }
            }
            for (sx in listOf(16f, 26f, 36f, 64f, 74f, 84f)) { val sy = 85.6f + if (abs(sx - 50f) < 20f) 1.6f else 0.4f; rotate(0.3f * 180f / PIf, pivot = Offset(sx, sy)) { oval(Color(0xFF1A1210), sx - 0.5f, sy - 0.28f, 1f, 0.56f) } }
        }
        "cosmos" -> {
            // cosmos: nebulosa rosa/roxa clara bem visível nas lombadas e no pé, estrelas grandes cintilando,
            // um planeta com anel e, às vezes, uma estrela cadente
            for (q in listOf(Quad(10f, 54f, 9f, Color(0xFFFF96E1)), Quad(10f, 78f, 8f, Color(0xFFC896FF)), Quad(90f, 60f, 9f, Color(0xFFC896FF)), Quad(90f, 82f, 8f, Color(0xFFFF96E1)), Quad(30f, 92f, 12f, Color(0xFFBE8CFF)), Quad(70f, 92f, 12f, Color(0xFFFF96E1)))) {
                drawPath(cover, Brush.radialGradient(0f to q.d.copy(alpha = 0.85f), 0.5f to q.d.copy(alpha = 0.4f), 1f to q.d.copy(alpha = 0f), center = Offset(q.a, q.b), radius = q.c))
            }
            for (st in listOf(floatArrayOf(9.4f, 47f, 0.5f), floatArrayOf(11f, 52f, 0.35f), floatArrayOf(9.2f, 63f, 0.65f), floatArrayOf(10.8f, 70f, 0.4f), floatArrayOf(9.8f, 82f, 0.55f), floatArrayOf(90.6f, 49f, 0.65f), floatArrayOf(89.2f, 57f, 0.4f), floatArrayOf(90.8f, 70f, 0.55f), floatArrayOf(89.4f, 77f, 0.4f), floatArrayOf(90.4f, 86f, 0.5f), floatArrayOf(20f, 89.4f, 0.55f), floatArrayOf(36f, 90.8f, 0.45f), floatArrayOf(64f, 90.8f, 0.55f), floatArrayOf(80f, 89.4f, 0.45f))) {
                val x = st[0]; val y = st[1]; val r = st[2]; val tw = 0.5f + 0.5f * abs(sin(t * 1.7f + x * 0.7f + y))
                val sc = Color(0xFFFFFCEB)
                drawCircle(sc.copy(alpha = tw), r, Offset(x, y))
                if (r > 0.45f) { line(sc.copy(alpha = tw * 0.85f), x - r * 2.8f, y, x + r * 2.8f, y, 0.2f); line(sc.copy(alpha = tw * 0.85f), x, y - r * 2.8f, x, y + r * 2.8f, 0.2f) }
            }
            drawCircle(Brush.radialGradient(listOf(Color(0xFFFFD3A0), Color(0xFFC2603A)), center = Offset(9.6f, 83.4f), radius = 1.5f), 1.2f, Offset(10f, 84f))
            rotate(-0.3f * 180f / PIf, pivot = Offset(10f, 84f)) { drawOval(Color(0xD9FFE2AA), Offset(8f, 83.5f), Size(4f, 1f), style = Stroke(0.25f)) }
            val k = (t * 0.22f) % 1f
            if (k < 0.18f) { val u = k / 0.18f; val x = 87f + u * 4f; val y = 46f + u * 10f; line(Color.White.copy(alpha = 0.9f * (1f - u)), x - 1.6f, y - 4f, x, y, 0.25f); drawCircle(Color.White, 0.3f, Offset(x, y)) }
            val sx = -30f + ((t * 0.25f) % 1f) * 170f
            drawPath(cover, Brush.linearGradient(listOf(Color.White.copy(alpha = 0f), Color(0x40DCC8FF), Color.White.copy(alpha = 0f)), start = Offset(sx, 40f), end = Offset(sx + 18f, 95f)))
        }
    }
}

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
        "camo" -> {
            // camuflado: manchas arredondadas em três tons (escuro, terra e claro), sempre no mesmo lugar
            val cols = listOf(Color(0x8C262E12), Color(0x806E5630), Color(0x73B2BA78))
            for (i in 0 until 34) {
                val x = 6f + ((i * 41) % 88); val y = 41f + ((i * 29) % 54); val r = 2.2f + ((i * 7) % 5) * 0.55f; val a = i * 0.9f
                val p = Path().apply {
                    moveTo(x + r, y)
                    for (k in 1..6) { val ang = k * PIf / 3f; val rr = r * (0.7f + 0.3f * sin(a + k * 2.1f)); quadraticTo(x + cos(ang - PIf / 6f) * rr * 1.25f, y + sin(ang - PIf / 6f) * rr * 0.9f, x + cos(ang) * rr, y + sin(ang) * rr * 0.75f) }
                    close()
                }
                drawPath(p, cols[i % 3])
            }
        }
        "jaleco" -> {
            // jaleco: tecido liso com trama fininha, cruz de saúde nas lombadas e botões na barra
            var x = -50f; while (x < 100f) { line(Color(0x0D788CAA), x, 40f, x + 54f, 95f, 0.25f, false); x += 1.2f }
            for (cx in listOf(10f, 90f)) { drawRect(Color(0xFFE0475E), Offset(cx - 0.45f, 49f), Size(0.9f, 3.2f)); drawRect(Color(0xFFE0475E), Offset(cx - 1.6f, 50.15f), Size(3.2f, 0.9f)) }
            for (cx in listOf(30f, 70f)) { val cy = 90.2f - abs(cx - 50f) * 0.06f; drawCircle(Color(0x8C788CAA), 0.85f, Offset(cx, cy)); drawCircle(Color.White, 0.6f, Offset(cx - 0.15f, cy - 0.2f)) }
        }
        "farda" -> {
            // farda: tecido rip-stop (quadriculado miúdo), o brasão dourado nas lombadas e a faixa refletiva na barra
            var x = 6f; while (x < 96f) { line(Color.White.copy(alpha = 0.05f), x, 40f, x, 95f, 0.25f, false); x += 2.2f }
            var y = 41f; while (y < 95f) { line(Color.White.copy(alpha = 0.05f), 6f, y, 94f, y, 0.25f, false); y += 2.2f }
            for (cx in listOf(10f, 90f)) {
                drawPath(Path().apply { moveTo(cx - 1.6f, 48.4f); lineTo(cx + 1.6f, 48.4f); lineTo(cx + 1.6f, 51f); quadraticTo(cx, 53.6f, cx - 1.6f, 51f); close() }, Color(0xFFE9AE34))
                drawCircle(Color(0xFF22306A), 0.6f, Offset(cx, 50.4f))
            }
            val band = Path().apply { moveTo(10f, 81.6f); cubicTo(22f, 79.5f, 38f, 80.4f, 50f, 85.6f); cubicTo(62f, 80.4f, 78f, 79.5f, 90f, 81.6f); lineTo(90f, 83.6f); cubicTo(78f, 81.5f, 62f, 82.4f, 50f, 87.6f); cubicTo(38f, 82.4f, 22f, 81.5f, 10f, 83.6f); close() }
            drawPath(band, hg(listOf(Color(0xFFB8BCC8), Color(0xFFF2F4FA), Color(0xFF9EA3B2), Color(0xFFF2F4FA), Color(0xFFB8BCC8)), 10f, 90f))
        }
        "lousa" -> {
            // lousa: verde fosco com pó de giz, um triângulo e uma conta meio apagados e a moldura de madeira nas lombadas
            for (i in 0 until 40) drawCircle(Color.White.copy(alpha = 0.06f), 0.4f + (i % 3) * 0.3f, Offset(8f + ((i * 37) % 84), 42f + ((i * 23) % 50)))
            val ch = Color.White.copy(alpha = 0.32f)
            drawPath(Path().apply { moveTo(14f, 86f); lineTo(14f, 77f); lineTo(24f, 86f); close() }, ch, style = stroke(0.4f))
            line(ch, 14f, 84.4f, 15.6f, 84.4f, 0.3f, false); line(ch, 15.6f, 84.4f, 15.6f, 86f, 0.3f, false)
            for (q in listOf(floatArrayOf(77f, 78f, 80f, 82f), floatArrayOf(77f, 82f, 80f, 78f), floatArrayOf(82f, 79.4f, 84.4f, 79.4f), floatArrayOf(82f, 80.8f, 84.4f, 80.8f), floatArrayOf(86f, 78f, 87.6f, 77.4f), floatArrayOf(87.6f, 77.4f, 87.6f, 82f))) line(ch, q[0], q[1], q[2], q[3], 0.4f, false)
            drawPath(Path().apply { moveTo(70f, 86f); cubicTo(74f, 84f, 80f, 87f, 86f, 85f) }, Color.White.copy(alpha = 0.12f), style = stroke(1.6f))
            for (cx in listOf(9.2f, 90.8f)) { drawRect(Color(0xBF966437), Offset(cx - 1.2f, 43f), Size(2.4f, 46f)); line(Color(0x59FFDCAA), cx - 0.6f, 43f, cx - 0.6f, 89f, 0.3f, false) }
        }
        "risca" -> {
            // terno de risca de giz, com os botões nas lombadas
            var x = 6f; while (x < 96f) { line(Color(0x38E6E8F0), x, 40f, x, 95f, 0.22f, false); x += 2.6f }
            for (cx in listOf(10f, 90f)) for (cy in listOf(62f, 70f)) drawCircle(Color(0xB3C8CDDC), 0.7f, Offset(cx, cy))
        }
        "codigo" -> {
            // Vade Mecum: couro com grão, filetes dourados nas lombadas e o parágrafo em relevo
            for (g in GRAIN) drawCircle(Color(0xFF1E0006).copy(alpha = g[3] * 1.2f), g[2] * 1.2f, Offset(if (g[0] < 50f) 8.5f + (g[0] - 15f) * 0.1f else 91.5f - (85f - g[0]) * 0.1f, g[1] + 6f))
            for (cx in listOf(9.2f, 90.8f)) for (y in listOf(47f, 49f, 80f, 82f)) line(Color(0xD9F5C451), cx - 1.4f, y, cx + 1.4f, y, 0.4f, false)
            drawIntoCanvas { c ->
                val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply { color = 0xE6F5C451.toInt(); textSize = 3f; textAlign = android.graphics.Paint.Align.CENTER; typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD) }
                for (cx in listOf(9.2f, 90.8f)) c.nativeCanvas.drawText("§", cx, 65.6f, paint)
            }
        }
        in COVER_PAT2 -> drawCoverPattern2(t, c.pattern, cover)
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
    // boné em três quartos, assentado no alto do livro: copa baixa de seis gomos (o botão puxado para a
    // direita), aba curta para a frente e à esquerda, fora do rosto, e o escudo bordado no gomo da frente
    hatShadow(19f)
    val ink = Color(0xFF120D3F)
    val crown = Path().apply { moveTo(34.5f, 44.6f); cubicTo(33.6f, 31f, 42f, 24.2f, 52.4f, 24.2f); cubicTo(62f, 24.2f, 67.6f, 31.4f, 66.4f, 44.2f); quadraticTo(50.5f, 46.6f, 34.5f, 44.6f); close() }
    drawPath(crown, vg(listOf(Color(0xFF6E63F2), Color(0xFF4338D8), Color(0xFF2A209E)), 24f, 46f))
    clipPath(crown) {
        drawRect(hg(listOf(ink.copy(alpha = 0f), ink.copy(alpha = 0.38f)), 52f, 68f), Offset(50f, 20f), Size(20f, 28f))
        drawCircle(Brush.radialGradient(listOf(Color(0x52FFFFFF), Color(0x00FFFFFF)), center = Offset(42f, 30f), radius = 12f), 12f, Offset(42f, 30f))
        for ((cx, cy, x1, y1) in listOf(Quad(40f, 30f, 37.4f, 45f), Quad(49.4f, 34f, 47.6f, 46.4f), Quad(60f, 32f, 59.6f, 45.6f))) {
            val p = Path().apply { moveTo(54.2f, 24.6f); quadraticTo(cx, cy, x1, y1) }
            drawPath(p, ink.copy(alpha = 0.32f), style = stroke(0.45f)); drawPath(p, Color.White.copy(alpha = 0.12f), style = stroke(0.2f))
        }
        drawPath(Path().apply { moveTo(34.3f, 42.4f); quadraticTo(50.5f, 44.6f, 66.6f, 42f); lineTo(66.4f, 44.2f); quadraticTo(50.5f, 46.6f, 34.5f, 44.6f); close() }, ink.copy(alpha = 0.35f))
    }
    drawPath(crown, ink.copy(alpha = 0.55f), style = stroke(0.45f))
    // escudo bordado no gomo da frente: o livrinho aberto
    translate(46.2f, 35.4f) { rotate(-0.06f * 180f / PIf, pivot = Offset.Zero) { scale(0.82f, 0.86f, pivot = Offset.Zero) {
        drawPath(roundRect(-5.6f, -4f, 11.2f, 8f, 2.2f), Paper); drawPath(roundRect(-5.6f, -4f, 11.2f, 8f, 2.2f), ink.copy(alpha = 0.25f), style = stroke(0.35f))
        drawPath(Path().apply { moveTo(-3.8f, -1.6f); quadraticTo(-1.9f, -2.6f, 0f, -1.4f); quadraticTo(1.9f, -2.6f, 3.8f, -1.6f); lineTo(3.8f, 2f); quadraticTo(1.9f, 1f, 0f, 2.2f); quadraticTo(-1.9f, 1f, -3.8f, 2f); close() }, Color(0xFF3A2FC4))
        line(Paper, 0f, -1.2f, 0f, 1.9f, 0.35f)
    } } }
    // aba: sai da frente da copa para a esquerda, vista de cima, com a costura e o avesso escuro na ponta
    translate(0f, -0.9f) {
        val under = Path().apply { moveTo(56.6f, 45.4f); cubicTo(50f, 50.4f, 38f, 51.4f, 29.6f, 50.2f); cubicTo(25.6f, 49.6f, 24f, 48.4f, 24.4f, 47f); cubicTo(25f, 48.6f, 26.6f, 49.2f, 29.6f, 49.4f); cubicTo(38f, 50.6f, 50f, 49.6f, 56.6f, 45.4f); close() }
        drawPath(under, Color(0xFF140E52))
        val visor = Path().apply { moveTo(35f, 42.6f); cubicTo(40f, 45.4f, 49f, 46.4f, 56.6f, 45.4f); cubicTo(50f, 49.6f, 38f, 50.6f, 29.6f, 49.4f); cubicTo(24f, 48.6f, 23.2f, 45.4f, 26.8f, 43.8f); cubicTo(29.4f, 42.8f, 32.4f, 42.4f, 35f, 42.6f); close() }
        drawPath(visor, Brush.linearGradient(listOf(Color(0xFF4B40DC), Color(0xFF2A209E)), start = Offset(30f, 43f), end = Offset(46f, 50f)))
        drawPath(visor, ink.copy(alpha = 0.5f), style = stroke(0.35f))
        drawPath(Path().apply { moveTo(28.4f, 46.2f); cubicTo(36f, 48.6f, 46f, 48.4f, 53.4f, 46.2f) }, Color.White.copy(alpha = 0.45f),
            style = Stroke(0.28f, cap = StrokeCap.Round, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.8f, 0.6f))))
        line(Color.White.copy(alpha = 0.35f), 28.6f, 44.4f, 34f, 43.2f, 0.5f)
    }
    // botão menta no topo
    drawCircle(Color(0xFF3FAF86), 1.6f, Offset(54.2f, 24.6f)); drawCircle(Mint, 1.3f, Offset(54f, 24.3f)); drawCircle(Color.White.copy(alpha = 0.75f), 0.45f, Offset(53.6f, 23.9f))
}

private fun DrawScope.drawBoinaMilitar(t: Float) {
    // boina militar verde-oliva: caída para a direita, debrum de couro preto e o distintivo dourado na frente
    hatShadow(18f)
    drawPath(Path().apply { moveTo(34.6f, 41.4f); quadraticTo(50f, 38.8f, 65.4f, 41.4f); lineTo(65f, 45.2f); quadraticTo(50f, 42.4f, 35f, 45.2f); close() }, vg(listOf(Color(0xFF2A2A22), Color(0xFF121210)), 39f, 46f))
    line(Color.White.copy(alpha = 0.18f), 37f, 42.4f, 63f, 42.4f, 0.35f)
    translate(53f, 37.6f) {
        rotate(0.13f * DEG, pivot = Offset.Zero) {
            scale(0.94f, 0.72f, pivot = Offset.Zero) {
                val top = Path().apply { moveTo(-21.6f, 1.8f); cubicTo(-24f, -6f, -12f, -10.6f, 0f, -10.2f); cubicTo(13f, -9.8f, 25.4f, -4.6f, 23.4f, 3.4f); cubicTo(22f, 8f, 9f, 8.6f, 0f, 8f); cubicTo(-10f, 7.4f, -20.4f, 6f, -21.6f, 1.8f); close() }
                drawPath(top, Brush.radialGradient(listOf(Color(0xFF97A465), Color(0xFF6B7840), Color(0xFF46512A)), center = Offset(-8f, -4f), radius = 24f))
                // feltro: pontinhos de textura e a dobra caída do lado
                clipPath(top) { for (i in 0 until 40) drawCircle(Color(0x291E240E), 0.3f, Offset(-20f + ((i * 37) % 44), -9f + ((i * 23) % 17))) }
                drawPath(Path().apply { moveTo(-14f, 4.2f); quadraticTo(4f, 8.4f, 21f, 2.4f) }, Color(0x661E240E), style = stroke(0.7f))
                drawPath(Path().apply { moveTo(-12f, -4.6f); quadraticTo(-3f, -7f, 9f, -6f) }, Color.White.copy(alpha = 0.2f), style = stroke(0.6f))
            }
        }
    }
    // distintivo: escudo dourado com ramos e a estrela, um brilho de vez em quando
    val bx = 41.6f; val by = 37.2f
    for (s in listOf(-1f, 1f)) {
        drawPath(Path().apply { moveTo(bx + s * 0.8f, by + 3.2f); quadraticTo(bx + s * 4.4f, by + 2f, bx + s * 4f, by - 2.6f) }, Color(0xFFB57A0C), style = stroke(0.9f))
        for (k in 0 until 3) { val cx = bx + s * (2.4f + k * 0.6f); val cy = by + 2.2f - k * 1.7f; rotate(s * (0.9f + k * 0.3f) * DEG, pivot = Offset(cx, cy)) { oval(Color(0xFFF5C451), cx - 0.9f, cy - 0.45f, 1.8f, 0.9f) } }
    }
    val sh = Path().apply { moveTo(bx - 2.6f, by - 3f); lineTo(bx + 2.6f, by - 3f); lineTo(bx + 2.6f, by + 0.6f); quadraticTo(bx + 2.4f, by + 2.8f, bx, by + 3.8f); quadraticTo(bx - 2.4f, by + 2.8f, bx - 2.6f, by + 0.6f); close() }
    drawPath(sh, vg(listOf(Color(0xFFFFF1BF), Color(0xFFF5C451), Color(0xFFB57A0C)), by - 3f, by + 3.8f)); drawPath(sh, Color(0x996E4600), style = stroke(0.3f))
    drawPath(star5(bx, by + 0.2f, 1.7f), Color(0xFF2E7D3A))
    val k = maxOf(0f, sin(t * 1.2f)).pow(10); if (k > 0.02f) star4(bx + 2f, by - 2.6f, 2.6f * k, Color.White.copy(alpha = k))
}

private fun DrawScope.drawQuepe(t: Float) {
    // quepe da polícia: copa azul-marinho larga, faixa preta com cordão dourado, pala de verniz e o brasão alado
    hatShadow(18f)
    drawPath(Path().apply { moveTo(33f, 40f); cubicTo(28f, 30f, 34f, 24.5f, 50f, 24.5f); cubicTo(66f, 24.5f, 72f, 30f, 67f, 40f); close() }, vg(listOf(Color(0xFF3A4A8C), Color(0xFF22306A), Color(0xFF141C46)), 24.5f, 40f))
    drawOval(vg(listOf(Color(0xFF4C5DA6), Color(0xFF2A3878)), 23.6f, 30.8f), Offset(32.4f, 23.6f), Size(35.2f, 7.2f))
    line(Color.White.copy(alpha = 0.22f), 38f, 26.4f, 56f, 25.4f, 0.6f)
    drawPath(Path().apply { moveTo(34.4f, 38.6f); quadraticTo(50f, 36.2f, 65.6f, 38.6f); lineTo(65.2f, 44.4f); quadraticTo(50f, 41.8f, 34.8f, 44.4f); close() }, vg(listOf(Color(0xFF1A1A22), Color(0xFF060608)), 37f, 44.4f))
    val cord = Path().apply { moveTo(36f, 42.6f); quadraticTo(50f, 40.6f, 64f, 42.6f) }
    drawPath(cord, Color(0xFFB57A0C), style = stroke(0.9f)); drawPath(cord, Color(0xFFFFD877), style = stroke(0.45f))
    for (x in listOf(36f, 64f)) drawCircle(Color(0xFFE9AE34), 0.9f, Offset(x, 42.6f))
    drawPath(Path().apply { moveTo(35f, 44.2f); quadraticTo(50f, 41.6f, 65f, 44.2f); quadraticTo(60f, 49.4f, 50f, 49.6f); quadraticTo(40f, 49.4f, 35f, 44.2f); close() }, vg(listOf(Color(0xFF2C2C36), Color(0xFF0B0B10)), 42f, 49.6f))
    drawPath(Path().apply { moveTo(39f, 45.4f); quadraticTo(50f, 44f, 60f, 45.4f) }, Color.White.copy(alpha = 0.35f), style = stroke(0.5f))
    val bx = 50f; val by = 32.6f
    for (sd in listOf(-1f, 1f)) drawPath(Path().apply { moveTo(bx + sd * 2.4f, by - 0.6f); quadraticTo(bx + sd * 6.4f, by - 2.6f, bx + sd * 7.6f, by - 0.4f); quadraticTo(bx + sd * 5f, by + 0.6f, bx + sd * 2.4f, by + 1.4f); close() }, vg(GOLD3, by - 2.6f, by + 1.4f))
    val sh = Path().apply { moveTo(bx - 2.6f, by - 3.2f); lineTo(bx + 2.6f, by - 3.2f); lineTo(bx + 2.6f, by + 0.6f); quadraticTo(bx + 2.4f, by + 2.8f, bx, by + 3.8f); quadraticTo(bx - 2.4f, by + 2.8f, bx - 2.6f, by + 0.6f); close() }
    drawPath(sh, vg(listOf(Color(0xFFFFF1BF), Color(0xFFF5C451), Color(0xFFB57A0C)), by - 3.2f, by + 3.8f)); drawPath(sh, Color(0x996E4600), style = stroke(0.3f))
    drawPath(star5(bx, by + 0.1f, 1.6f), Color(0xFF22306A))
    val k = maxOf(0f, sin(t * 1.2f + 0.8f)).pow(10); if (k > 0.02f) star4(bx + 2.4f, by - 2.8f, 2.6f * k, Color.White.copy(alpha = k))
}

private fun DrawScope.drawViseira(t: Float) {
    // viseira de contador: faixa escura, aba larga de acetato verde translúcido e um lápis preso do lado
    hatShadow(14f)
    drawPath(Path().apply { moveTo(34.6f, 37.4f); quadraticTo(50f, 34.4f, 65.4f, 37.4f); lineTo(65f, 43.4f); quadraticTo(50f, 40.4f, 35f, 43.4f); close() }, vg(listOf(Color(0xFF3A3F4E), Color(0xFF1E2230)), 35f, 43.4f))
    line(Color.White.copy(alpha = 0.2f), 37f, 40.2f, 63f, 40.2f, 0.35f)
    drawPencilAt(63.5f, 41.5f, 69f, 33f, 0.9f)
    val brim = Path().apply { moveTo(33.2f, 42.2f); quadraticTo(50f, 39.2f, 66.8f, 42.2f); quadraticTo(64.4f, 47.6f, 50f, 48f); quadraticTo(35.6f, 47.6f, 33.2f, 42.2f); close() }
    drawPath(brim, vg(listOf(Color(0xD146BE78), Color(0x9E1E8C50)), 40f, 48f)); drawPath(brim, Color(0xB30A4628), style = stroke(0.45f))
    drawPath(Path().apply { moveTo(38f, 43.4f); quadraticTo(50f, 41.6f, 62f, 43.4f) }, Color(0x99DCFFE6), style = stroke(0.5f))
    val k = maxOf(0f, sin(t * 0.9f)).pow(12); if (k > 0.02f) star4(40f, 45f, 2f * k, Color.White.copy(alpha = k))
}

private fun DrawScope.drawTouca(t: Float) {
    // touca de centro cirúrgico: tecido azul-turquesa estampado, barra dobrada e o laço amarrado do lado
    hatShadow(18f)
    val dome = Path().apply { moveTo(33.6f, 44f); cubicTo(31.6f, 30f, 40f, 21.6f, 50f, 21.6f); cubicTo(60f, 21.6f, 68.4f, 30f, 66.4f, 44f); quadraticTo(50f, 40.6f, 33.6f, 44f); close() }
    drawPath(dome, vg(listOf(Color(0xFF6FD6D0), Color(0xFF3FB3AD), Color(0xFF2A8C88)), 21.6f, 44f))
    clipPath(dome) {
        // estampa: cruzinhas e coraçõezinhos
        for (i in 0 until 16) {
            val x = 36f + ((i * 13) % 28); val y = 25f + ((i * 7) % 15)
            if (i % 2 == 1) { drawRect(Color.White.copy(alpha = 0.75f), Offset(x - 0.25f, y - 0.8f), Size(0.5f, 1.6f)); drawRect(Color.White.copy(alpha = 0.75f), Offset(x - 0.8f, y - 0.25f), Size(1.6f, 0.5f)) }
            else drawPath(heart(x, y, 0.9f), Color(0xD9FFAABE))
        }
        drawRect(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.35f), Color.White.copy(alpha = 0f)), center = Offset(43f, 27f), radius = 13f), Offset(30f, 18f), Size(40f, 28f))
        for (x in listOf(41f, 50f, 59f)) drawPath(Path().apply { moveTo(x, 38.6f); quadraticTo(x - 0.8f, 33f, x + 0.4f, 28f) }, Color(0x47145A58), style = stroke(0.5f))
    }
    val band = Path().apply { moveTo(33.4f, 39.6f); quadraticTo(50f, 36.8f, 66.6f, 39.6f); lineTo(66.4f, 44.4f); quadraticTo(50f, 41.4f, 33.6f, 44.4f); close() }
    drawPath(band, vg(listOf(Color(0xFF56C8C2), Color(0xFF2E9C97)), 37f, 44.4f)); drawPath(band, Color(0x66145A58), style = stroke(0.3f))
    drawPath(Path().apply { moveTo(34.4f, 43.2f); quadraticTo(50f, 40.4f, 65.6f, 43.2f) }, Color.White.copy(alpha = 0.55f), style = Stroke(0.25f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.8f, 0.6f))))
    // laço do lado direito, com as pontas balançando
    val sw = sin(t * 2.2f) * 0.8f
    for ((dx, len) in listOf(0f to 9.6f, 1.6f to 8f)) {
        val tl = Path().apply { moveTo(66f + dx * 0.4f, 41.6f); quadraticTo(69f + dx + sw * 0.5f, 45f, 68.6f + dx + sw, 41.6f + len) }
        drawPath(tl, Color(0xFF2E9C97), style = stroke(1.5f)); drawPath(tl, Color.White.copy(alpha = 0.3f), style = stroke(0.35f))
    }
    for (s in listOf(-1f, 1f)) rotate(s * 0.5f * DEG, pivot = Offset(66.4f + s * 2f, 40.6f)) { oval(Color(0xFF3FB3AD), 66.4f + s * 2f - 2.2f, 39.3f, 4.4f, 2.6f) }
    drawCircle(Color(0xFF2A8C88), 1f, Offset(66.4f, 40.8f))
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

// ---- chapéus do armário 2.0
/** Cor com transparência (atalho dos chapéus e óculos novos). */
private fun hatC(hex: Long, a: Float) = Color(hex).copy(alpha = a)
private val HatDash = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.8f, 0.6f))
/** Elipse girada preenchida (manchas, pétalas, folhas). */
private fun DrawScope.hatEll(color: Color, x: Float, y: Float, rx: Float, ry: Float, a: Float = 0f) {
    rotate(a * DEG, pivot = Offset(x, y)) { oval(color, x - rx, y - ry, rx * 2, ry * 2) }
}
/** Ponto numa curva quadrática (para enfileirar flores e rebites ao longo de um arco). */
private fun hatQ(u: Float, x0: Float, y0: Float, cx: Float, cy: Float, x1: Float, y1: Float): Offset {
    val v = 1 - u; return Offset(v * v * x0 + 2 * v * u * cx + u * u * x1, v * v * y0 + 2 * v * u * cy + u * u * y1)
}
private fun DrawScope.hatGloss(x: Float, y: Float, r: Float, a: Float) {
    drawRect(Brush.radialGradient(listOf(Color.White.copy(alpha = a), Color.White.copy(alpha = 0f)), center = Offset(x, y), radius = r), Offset(x - r, y - r), Size(2 * r, 2 * r))
}
private fun DrawScope.hatSpark(x: Float, y: Float, r: Float, k: Float) { if (k > 0.02f) star4(x, y, r * k, Color.White.copy(alpha = k)) }

private fun DrawScope.drawEspelhoFrontal(t: Float) {
    // espelho frontal de médico: faixa elástica clara na testa, articulação e o espelho redondo com o furinho
    // no meio; um reflexo corre pelo espelho e às vezes ele cintila
    hatShadow(16f)
    val band = Path().apply { moveTo(33.4f, 39.6f); quadraticTo(50f, 36.6f, 66.6f, 39.6f); lineTo(66.4f, 44.2f); quadraticTo(50f, 41.2f, 33.6f, 44.2f); close() }
    drawPath(band, vg(listOf(Color(0xFFF4F2FA), Color(0xFFC9C6D8)), 37f, 44.2f)); drawPath(band, hatC(0xFF1E1B2E, 0.35f), style = stroke(0.35f))
    drawPath(Path().apply { moveTo(34.4f, 42.8f); quadraticTo(50f, 39.8f, 65.6f, 42.8f) }, hatC(0xFF5A5678, 0.45f), style = Stroke(0.25f, pathEffect = HatDash))
    line(Color(0xFF3A3550), 50f, 39.4f, 50f, 36f, 1.6f); line(Color(0xFF8E8AA6), 50f, 39.4f, 50f, 36f, 0.6f)
    drawCircle(Color(0xFF5A5670), 1.5f, Offset(50f, 39.2f)); drawCircle(Color(0xFFC9C6D8), 0.6f, Offset(49.6f, 38.8f))
    val mx = 50f; val my = 29.4f; val r = 7.4f
    drawCircle(Color(0xFF2A2640), r + 1.1f, Offset(mx, my))
    drawCircle(Brush.radialGradient(0f to Color.White, 0.45f to Color(0xFFDDE6F2), 1f to Color(0xFF8A97AE), center = Offset(mx - 2.4f, my - 2.6f), radius = r + 2f), r, Offset(mx, my))
    drawCircle(Color.White.copy(alpha = 0.55f), r - 1.4f, Offset(mx, my), style = Stroke(0.4f))
    clipPath(ellipsePath(mx, my, r, r)) { val sx = mx - 12f + ((t * 0.3f) % 1f) * 24f; line(Color.White.copy(alpha = 0.6f), sx - 3f, my + 8f, sx + 3f, my - 8f, 2f) }
    drawCircle(Color(0xFF2A2640), 1.2f, Offset(mx, my)); drawCircle(Color.White.copy(alpha = 0.5f), 0.35f, Offset(mx - 0.4f, my - 0.4f))
    hatSpark(mx - 3.4f, my - 3.6f, 3f, maxOf(0f, sin(t * 1.3f)).pow(8))
}

private fun DrawScope.drawCapacete(t: Float) {
    // capacete militar camuflado: casco verde com manchas, a rede por cima, aba do casco e a jugular com fivela
    hatShadow(20f)
    val dome = Path().apply { moveTo(31.6f, 44.4f); cubicTo(30.4f, 28f, 39.4f, 20.6f, 50f, 20.6f); cubicTo(60.6f, 20.6f, 69.6f, 28f, 68.4f, 44.4f); quadraticTo(50f, 41.6f, 31.6f, 44.4f); close() }
    drawPath(dome, vg(listOf(Color(0xFF8A9A5B), Color(0xFF5F6E38), Color(0xFF3E4A22)), 20.6f, 44.4f))
    clipPath(dome) {
        val dk = Color(0xFF3B4420); val br = Color(0xFF7A5A34); val sa = Color(0xFFC2B78A)
        hatEll(dk, 38f, 30f, 5f, 2.6f, 0.4f); hatEll(br, 55f, 25.6f, 6f, 2.4f, -0.3f); hatEll(dk, 61.4f, 36f, 4.6f, 2.8f, 0.6f); hatEll(br, 44f, 38f, 6f, 2f, 0.1f)
        hatEll(sa, 50f, 31.6f, 3.4f, 2f, -0.8f); hatEll(sa, 35f, 40f, 3f, 1.6f, 0.3f); hatEll(sa, 64f, 28.6f, 2.6f, 1.6f, 0.9f); hatEll(dk, 43.6f, 24.4f, 2.6f, 1.4f, 0.2f)
        for (i in -4 until 10) { line(hatC(0xFF1E220E, 0.38f), 20f + i * 5, 46f, 34f + i * 5, 18f, 0.3f); line(hatC(0xFF1E220E, 0.38f), 34f + i * 5, 46f, 20f + i * 5, 18f, 0.3f) }
        hatGloss(42f, 27f, 13f, 0.3f)
    }
    drawPath(dome, hatC(0xFF1E220E, 0.55f), style = stroke(0.45f))
    val rim = Path().apply { moveTo(30.4f, 43.2f); quadraticTo(50f, 39.6f, 69.6f, 43.2f); quadraticTo(70.6f, 45.4f, 69f, 46.2f); quadraticTo(50f, 42.8f, 31f, 46.2f); quadraticTo(29.4f, 45.4f, 30.4f, 43.2f); close() }
    drawPath(rim, vg(listOf(Color(0xFF6E7E44), Color(0xFF38431E)), 41f, 46.2f)); drawPath(rim, hatC(0xFF1E220E, 0.5f), style = stroke(0.3f))
    line(Color.White.copy(alpha = 0.22f), 36f, 42.6f, 46f, 41.4f, 0.4f)
    for (s in listOf(-1f, 1f)) {
        val x0 = 50f + s * 18.4f
        drawPath(Path().apply { moveTo(x0, 45f); quadraticTo(x0 + s * 1.2f, 49f, x0 + s * 0.4f, 52.6f) }, Color(0xFF4A3A22), style = stroke(1.1f))
        drawRect(Color(0xFFC9C6B0), Offset(x0 + s * 0.6f - 0.9f, 48.6f), Size(1.8f, 1.4f))
    }
}

private fun DrawScope.drawBoneTatico(t: Float) {
    // boné tático da polícia: copa azul-marinho quase preta em gomos, aba reta com costura e o patch de velcro
    // com o escudo prateado e a estrela
    hatShadow(18f)
    val crown = Path().apply { moveTo(34f, 43.6f); cubicTo(32.6f, 29.6f, 40.6f, 23.6f, 50f, 23.6f); cubicTo(59.4f, 23.6f, 67.4f, 29.6f, 66f, 43.6f); quadraticTo(50f, 41f, 34f, 43.6f); close() }
    drawPath(crown, vg(listOf(Color(0xFF3A4466), Color(0xFF1C2340), Color(0xFF0E1226)), 23.6f, 43.6f))
    clipPath(crown) {
        hatGloss(42f, 29f, 12f, 0.25f)
        for ((cx, x1) in listOf(42f to 38.6f, 50f to 50f, 58f to 61.4f)) { val p = Path().apply { moveTo(50f, 24f); quadraticTo(cx, 32f, x1, 43f) }; drawPath(p, Color.Black.copy(alpha = 0.4f), style = stroke(0.4f)); drawPath(p, Color.White.copy(alpha = 0.08f), style = stroke(0.2f)) }
    }
    drawPath(crown, hatC(0xFF050814, 0.6f), style = stroke(0.45f))
    drawCircle(Color(0xFF0E1226), 1.3f, Offset(50f, 24.2f)); drawCircle(Color.White.copy(alpha = 0.3f), 0.4f, Offset(49.6f, 23.8f))
    drawPath(roundRect(44.2f, 29.8f, 11.6f, 8.8f, 1.6f), Color(0xFF2B3352)); drawPath(roundRect(44.2f, 29.8f, 11.6f, 8.8f, 1.6f), Color.Black.copy(alpha = 0.4f), style = stroke(0.3f))
    drawPath(roundRect(44.8f, 30.4f, 10.4f, 7.6f, 1.2f), Color.White.copy(alpha = 0.35f), style = Stroke(0.25f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.6f, 0.5f))))
    val bx = 50f; val by = 34f
    val sh = Path().apply { moveTo(bx - 2.8f, by - 3f); lineTo(bx + 2.8f, by - 3f); lineTo(bx + 2.8f, by + 0.4f); quadraticTo(bx + 2.6f, by + 2.6f, bx, by + 3.6f); quadraticTo(bx - 2.6f, by + 2.6f, bx - 2.8f, by + 0.4f); close() }
    drawPath(sh, vg(listOf(Color.White, Color(0xFFC9CED8), Color(0xFF7E8498)), by - 3f, by + 3.6f)); drawPath(sh, hatC(0xFF141828, 0.6f), style = stroke(0.3f))
    drawPath(star5(bx, by + 0.1f, 1.7f), Color(0xFF1C2340))
    val visor = Path().apply { moveTo(34.4f, 42.4f); quadraticTo(50f, 39.8f, 65.6f, 42.4f); quadraticTo(62f, 49.4f, 50f, 49.8f); quadraticTo(38f, 49.4f, 34.4f, 42.4f); close() }
    drawPath(visor, vg(listOf(Color(0xFF252C4A), Color(0xFF0A0D1C)), 41f, 49.8f)); drawPath(visor, Color.Black.copy(alpha = 0.5f), style = stroke(0.35f))
    drawPath(Path().apply { moveTo(37.4f, 44.4f); quadraticTo(50f, 46.6f, 62.6f, 44.4f) }, Color.White.copy(alpha = 0.3f), style = Stroke(0.25f, pathEffect = HatDash))
    line(Color.White.copy(alpha = 0.25f), 39f, 43.2f, 46f, 42.2f, 0.5f)
    hatSpark(bx + 2.6f, by - 2.6f, 2.6f, maxOf(0f, sin(t * 1.2f + 2f)).pow(10))
}

private fun DrawScope.drawBarrete(t: Float) {
    // barrete de jurista: copa preta macia, quase quadrada, com três cristas em pé e a borla no alto que balança
    hatShadow(18f)
    val body = Path().apply { moveTo(34.6f, 44f); lineTo(34f, 31f); quadraticTo(34f, 25.4f, 40f, 25f); quadraticTo(50f, 23.4f, 60f, 25f); quadraticTo(66f, 25.4f, 66f, 31f); lineTo(65.4f, 44f); quadraticTo(50f, 41.6f, 34.6f, 44f); close() }
    drawPath(body, hg(listOf(Color(0xFF14121C), Color(0xFF3A3650), Color(0xFF24212F), Color(0xFF0E0D14)), 34f, 66f))
    clipPath(body) {
        for (x in listOf(42f, 50f, 58f)) { line(Color.Black.copy(alpha = 0.45f), x, 26f, x + 0.3f, 42f, 0.8f); line(Color.White.copy(alpha = 0.12f), x + 0.7f, 26f, x + 1f, 42f, 0.4f) }
        hatGloss(41f, 30f, 10f, 0.18f)
    }
    drawPath(body, Color.Black.copy(alpha = 0.6f), style = stroke(0.4f))
    for (x in listOf(40.6f, 50f, 59.4f)) {
        val fin = Path().apply { moveTo(x - 3f, 26.2f); quadraticTo(x - 0.6f, 18.8f, x, 18.6f); quadraticTo(x + 0.6f, 18.8f, x + 3f, 26.2f); close() }
        drawPath(fin, hg(listOf(Color(0xFF2A2640), Color(0xFF4A4466), Color(0xFF14121C)), x - 3f, x + 3f)); drawPath(fin, Color.Black.copy(alpha = 0.55f), style = stroke(0.3f))
        line(Color.White.copy(alpha = 0.2f), x - 0.6f, 20f, x - 1.6f, 25.2f, 0.35f)
    }
    drawPath(Path().apply { moveTo(34.5f, 40.6f); quadraticTo(50f, 38.2f, 65.5f, 40.6f); lineTo(65.4f, 44f); quadraticTo(50f, 41.6f, 34.6f, 44f); close() }, vg(listOf(Color(0xFF2A2640), Color(0xFF0E0D14)), 39f, 44f))
    // borla: botão no alto, cordão e franjas balançando
    val sw = sin(t * 2f) * 0.9f
    drawPath(Path().apply { moveTo(50f, 19f); quadraticTo(54f + sw * 0.4f, 18.6f, 55.6f + sw, 22.6f) }, Color(0xFFB57A0C), style = stroke(0.7f))
    drawCircle(Color(0xFF1A1726), 1.8f, Offset(50f, 18.6f)); drawCircle(Color.White.copy(alpha = 0.35f), 0.5f, Offset(49.5f, 18f))
    val tx = 55.6f + sw; val ty = 22.6f
    drawPath(Path().apply { moveTo(tx - 0.8f, ty); lineTo(tx + 0.8f, ty); lineTo(tx + 2.2f + sw * 0.3f, ty + 6.4f); lineTo(tx - 2f + sw * 0.3f, ty + 6.4f); close() }, vg(listOf(Color(0xFF2A2640), Color(0xFF0E0D14)), ty, ty + 6.4f))
    for (i in 0 until 5) line(Color.White.copy(alpha = 0.18f), tx - 0.4f + i * 0.2f, ty + 1f, tx - 1.6f + i * 0.85f + sw * 0.3f, ty + 6.2f, 0.2f)
    drawCircle(Gold, 1f, Offset(tx, ty + 0.2f)); drawCircle(Color.White.copy(alpha = 0.6f), 0.3f, Offset(tx - 0.3f, ty))
}

private fun DrawScope.drawFedora() {
    // fedora de auditor: aba virada (atrás sobe, frente desce), copa cinza com vinco e os dois apertos na frente,
    // fita preta com o lacinho do lado e uma peninha
    hatShadow(22f)
    val brim = Path().apply { moveTo(25.4f, 42.4f); quadraticTo(50f, 35.6f, 74.6f, 42.4f); quadraticTo(77f, 45.6f, 72f, 46.6f); quadraticTo(50f, 50.2f, 28f, 46.6f); quadraticTo(23f, 45.6f, 25.4f, 42.4f); close() }
    drawPath(brim, vg(listOf(Color(0xFF8E92A6), Color(0xFF5C6074), Color(0xFF3E4152)), 37f, 50f)); drawPath(brim, hatC(0xFF141622, 0.5f), style = stroke(0.35f))
    line(Color.White.copy(alpha = 0.3f), 29f, 45.6f, 40f, 47.6f, 0.4f)
    val crown = Path().apply { moveTo(35.6f, 42f); cubicTo(34.4f, 32f, 36.4f, 24.6f, 42f, 23.6f); quadraticTo(46f, 25.6f, 50f, 24.8f); quadraticTo(54f, 25.6f, 58f, 23.6f); cubicTo(63.6f, 24.6f, 65.6f, 32f, 64.4f, 42f); quadraticTo(50f, 44.4f, 35.6f, 42f); close() }
    drawPath(crown, hg(listOf(Color(0xFF6E7286), Color(0xFFA6AABC), Color(0xFF7E8296), Color(0xFF565A6C)), 35f, 65f))
    clipPath(crown) {
        drawPath(Path().apply { moveTo(50f, 25.2f); quadraticTo(48.8f, 30f, 50f, 35f) }, hatC(0xFF1E202E, 0.5f), style = stroke(0.9f))
        drawPath(Path().apply { moveTo(51f, 25.4f); quadraticTo(50f, 30f, 51.2f, 34.6f) }, Color.White.copy(alpha = 0.3f), style = stroke(0.4f))
        hatEll(hatC(0xFF1E202E, 0.2f), 43.6f, 31f, 1.6f, 3.8f, 0.2f); hatEll(hatC(0xFF1E202E, 0.2f), 56.4f, 31f, 1.6f, 3.8f, -0.2f)
        hatGloss(41f, 29f, 9f, 0.25f)
    }
    drawPath(crown, hatC(0xFF141622, 0.5f), style = stroke(0.4f))
    drawPath(Path().apply { moveTo(35.2f, 37f); quadraticTo(50f, 39.4f, 64.8f, 37f); lineTo(65f, 41.6f); quadraticTo(50f, 44f, 35f, 41.6f); close() }, vg(listOf(Color(0xFF2E2C3A), Color(0xFF0E0D14)), 37f, 44f))
    line(Color.White.copy(alpha = 0.18f), 37f, 38.2f, 63f, 38.2f, 0.3f)
    hatEll(Color(0xFF1A1824), 38.4f, 39.6f, 1.6f, 1.1f, 0.3f); hatEll(Color(0xFF1A1824), 39.8f, 40.4f, 1.2f, 0.8f, -0.4f)
    drawPath(Path().apply { moveTo(61.6f, 39f); quadraticTo(63f, 32f, 67.4f, 29.6f); quadraticTo(66.4f, 35f, 61.6f, 39f); close() }, vg(listOf(Color(0xFF6FB0FF), Color(0xFF3D8BEA)), 29.6f, 39f))
    line(Color.White.copy(alpha = 0.6f), 62f, 38.4f, 66.6f, 30.6f, 0.3f)
}

private fun DrawScope.drawCoco(t: Float) {
    // chapéu-coco de banqueiro: cúpula preta bem redonda e lustrosa, fita de gorgorão vinho e aba curta
    // enroladinha nos lados
    hatShadow(19f)
    val brim = Path().apply { moveTo(30.4f, 42.6f); quadraticTo(50f, 38.4f, 69.6f, 42.6f); quadraticTo(71.6f, 41.4f, 71.4f, 43.4f); quadraticTo(69.4f, 46.6f, 50f, 47.2f); quadraticTo(30.6f, 46.6f, 28.6f, 43.4f); quadraticTo(28.4f, 41.4f, 30.4f, 42.6f); close() }
    drawPath(brim, vg(listOf(Color(0xFF3A3650), Color(0xFF121018)), 40f, 47.2f)); drawPath(brim, Color.Black.copy(alpha = 0.5f), style = stroke(0.3f))
    val dome = Path().apply { moveTo(35.4f, 42.2f); cubicTo(34.4f, 27f, 41f, 21.4f, 50f, 21.4f); cubicTo(59f, 21.4f, 65.6f, 27f, 64.6f, 42.2f); quadraticTo(50f, 44.4f, 35.4f, 42.2f); close() }
    drawPath(dome, Brush.radialGradient(0f to Color(0xFF5A5470), 0.5f to Color(0xFF24212F), 1f to Color(0xFF0E0D14), center = Offset(44f, 28f), radius = 20f))
    drawPath(Path().apply { moveTo(39.6f, 33f); quadraticTo(40f, 25.6f, 46.6f, 23.6f) }, Color.White.copy(alpha = 0.4f), style = stroke(0.9f))
    drawPath(Path().apply { moveTo(35.5f, 37.4f); quadraticTo(50f, 39.6f, 64.5f, 37.4f); lineTo(64.6f, 41.6f); quadraticTo(50f, 43.8f, 35.4f, 41.6f); close() }, vg(listOf(Color(0xFF7A2232), Color(0xFF4A1220)), 37.4f, 43.8f))
    for (i in 0 until 12) line(Color.Black.copy(alpha = 0.18f), 37f + i * 2.4f, 38.6f, 37f + i * 2.4f, 42.4f, 0.25f)
    hatEll(Color(0xFF5A1626), 61f, 39.8f, 1.8f, 1.2f, 0.4f); hatEll(Color(0xFF5A1626), 62.6f, 40.4f, 1.4f, 0.9f, -0.5f); drawCircle(Color(0xFF3A0C16), 0.7f, Offset(61.8f, 40f))
    hatSpark(44f, 26f, 2.6f, maxOf(0f, sin(t * 1.1f + 1f)).pow(10))
}

private fun DrawScope.drawChapeuLapis(t: Float) {
    // chapéu de lápis: borracha rosa na base, ponteira de metal com frisos, corpo amarelo sextavado afinando em
    // cone, a madeira apontada e a ponta de grafite lá no alto
    hatShadow(17f)
    val er = Path().apply { moveTo(34.4f, 39.4f); quadraticTo(50f, 37f, 65.6f, 39.4f); lineTo(65.4f, 44.6f); quadraticTo(50f, 42.2f, 34.6f, 44.6f); close() }
    drawPath(er, vg(listOf(Color(0xFFFFB3C8), Color(0xFFF27A9C), Color(0xFFD95A80)), 37.4f, 44.6f)); drawPath(er, hatC(0xFF781E3C, 0.35f), style = stroke(0.3f))
    line(Color.White.copy(alpha = 0.4f), 37f, 40.8f, 46f, 39.8f, 0.5f)
    val fer = Path().apply { moveTo(35.4f, 35.2f); quadraticTo(50f, 33f, 64.6f, 35.2f); lineTo(65.6f, 39.6f); quadraticTo(50f, 37.2f, 34.4f, 39.6f); close() }
    drawPath(fer, hg(listOf(Color(0xFF9AA0B4), Color(0xFFEEF0F6), Color(0xFFB4B8C8), Color(0xFF7E8498)), 34f, 66f)); drawPath(fer, hatC(0xFF282C3C, 0.4f), style = stroke(0.3f))
    for (y in listOf(36.6f, 38f)) drawPath(Path().apply { moveTo(35f, y); quadraticTo(50f, y - 2.2f, 65f, y) }, hatC(0xFF282C3C, 0.35f), style = stroke(0.3f))
    val cone = Path().apply { moveTo(35.4f, 35.4f); lineTo(44.4f, 19.8f); quadraticTo(50f, 19f, 55.6f, 19.8f); lineTo(64.6f, 35.4f); quadraticTo(50f, 33.2f, 35.4f, 35.4f); close() }
    drawPath(cone, hg(listOf(Color(0xFFE9A20F), Color(0xFFFFD84A), Color(0xFFFFE98A), Color(0xFFF5B82A), Color(0xFFC98A12)), 35f, 65f))
    line(hatC(0xFF965A00, 0.3f), 45.4f, 34f, 48f, 19.6f, 0.45f); line(hatC(0xFF965A00, 0.3f), 54.6f, 34f, 52f, 19.6f, 0.45f)
    line(Color.White.copy(alpha = 0.45f), 41.6f, 32f, 46.4f, 21.4f, 0.6f)
    drawPath(cone, hatC(0xFF965A00, 0.45f), style = stroke(0.35f))
    val wood = Path().apply { moveTo(44.4f, 20f); quadraticTo(50f, 19.2f, 55.6f, 20f); lineTo(51.7f, 12.4f); lineTo(48.3f, 12.4f); close() }
    drawPath(wood, vg(listOf(Color(0xFFF6D9A8), Color(0xFFE0B47A)), 12f, 20f))
    drawPath(Path().apply { moveTo(44.4f, 20f); for (i in 0 until 4) quadraticTo(45.8f + i * 2.8f, 18.2f, 47.2f + i * 2.8f, 19.6f) }, hatC(0xFF965A00, 0.5f), style = stroke(0.35f))
    drawPath(wood, hatC(0xFF78501E, 0.45f), style = stroke(0.3f))
    drawPath(Path().apply { moveTo(48.3f, 12.6f); lineTo(51.7f, 12.6f); lineTo(50f, 8.4f); close() }, vg(listOf(Color(0xFF6A6488), Color(0xFF2A2640)), 8.4f, 12.6f))
    line(Color.White.copy(alpha = 0.5f), 49.4f, 11.6f, 49.9f, 9.4f, 0.3f)
    hatSpark(50f, 8.6f, 2.4f, maxOf(0f, sin(t * 1.3f)).pow(8))
}

private fun DrawScope.drawHeadset(t: Float) {
    // headset de suporte: arco fino por cima, um fone só (na esquerda) com a luz vermelha de "em atendimento",
    // o apoio do outro lado e o microfone de haste com espuma até a boca
    val arc = Path().apply { moveTo(13f, 50f); cubicTo(12f, 19f, 88f, 19f, 87f, 50f) }
    drawPath(arc, Color(0xFF1E1B2E), style = stroke(2.2f)); drawPath(arc, Color(0xFF55507A), style = stroke(1f))
    val pad = Path().apply { moveTo(40f, 27.4f); quadraticTo(50f, 24.2f, 60f, 27.4f) }
    drawPath(pad, Color(0xFF1E1B2E), style = stroke(3.4f)); drawPath(pad, Color(0xFF3A3550), style = stroke(2.4f)); line(Color.White.copy(alpha = 0.3f), 43f, 25.8f, 49f, 24.8f, 0.6f)
    drawPath(roundRect(84.6f, 47.4f, 4.6f, 8f, 2f), vg(listOf(Color(0xFF4A4466), Color(0xFF1E1B2E)), 47.4f, 55.4f))
    drawCircle(Brush.radialGradient(listOf(Color(0xFF6A64A0), Color(0xFF24212F)), center = Offset(11.4f, 52.6f), radius = 6.4f), 6f, Offset(12.6f, 54f))
    drawCircle(Color(0xFF1E1B2E), 4.4f, Offset(12.6f, 54f), style = Stroke(1f))
    drawCircle(Mint, 3f, Offset(12.6f, 54f), style = Stroke(0.5f))
    line(Color.White.copy(alpha = 0.35f), 9f, 51f, 10.6f, 49.4f, 0.7f)
    val on = 0.5f + 0.5f * sin(t * 4f)
    drawCircle(hatC(0xFFFF5A6E, 0.25f * on), 1.6f, Offset(12.6f, 54f)); drawCircle(hatC(0xFFFF5A6E, 0.6f + 0.4f * on), 0.8f, Offset(12.6f, 54f))
    val boom = Path().apply { moveTo(15.4f, 58f); cubicTo(18f, 70f, 28f, 77.4f, 39.6f, 77.4f) }
    drawPath(boom, Color(0xFF1E1B2E), style = stroke(1.3f)); drawPath(boom, Color.White.copy(alpha = 0.18f), style = stroke(0.35f))
    hatEll(Color(0xFF2A2640), 42f, 77.4f, 2.8f, 2.1f)
    for (i in 0 until 6) drawCircle(Color.White.copy(alpha = 0.18f), 0.25f, Offset(40.6f + (i % 3) * 1.3f, 76.6f + (i / 3) * 1.4f))
}

private fun DrawScope.drawClipe(t: Float) {
    // clipe gigante preso no alto do livro: arame de metal com as voltas do clipe, a ponta passando pela frente
    // da página e um brilho que corre pelo metal
    translate(58f, 46f) { rotate((0.16f + sin(t * 1.4f) * 0.02f) * DEG, pivot = Offset.Zero) {
        val p = Path().apply {
            moveTo(1.6f, 3f); lineTo(1.6f, -18f); arcTo(Rect(-1.6f, -19.6f, 1.6f, -16.4f), 0f, -180f, false); lineTo(-1.6f, 4f)
            arcTo(Rect(0.6f - 2.2f, 4f - 2.2f, 0.6f + 2.2f, 4f + 2.2f), 180f, -180f, false); lineTo(2.8f, -22f)
            arcTo(Rect(-0.4f - 3.2f, -22f - 3.2f, -0.4f + 3.2f, -22f + 3.2f), 0f, -180f, false); lineTo(-3.6f, 1f)
        }
        drawPath(p, Color(0xFF4E5468), style = stroke(1.9f)); drawPath(p, Color(0xFFB4BACA), style = stroke(1.3f)); drawPath(p, Color.White.copy(alpha = 0.75f), style = stroke(0.4f))
        hatSpark(-1.6f, -24.4f, 3f, maxOf(0f, sin(t * 1.5f)).pow(8))
        val u = (t * 0.35f) % 1f; drawCircle(Color.White.copy(alpha = 0.8f * sin(PIf * u)), 0.6f, Offset(2.8f, -2f - u * 18f))
    } }
}

private fun DrawScope.drawGorro(t: Float, tilt: Float, vy: Float) {
    // gorro de lã: copa vermelha de tricô com listras brancas, barra dobrada canelada e o pompom que balança
    hatShadow(19f)
    val sw = sin(t * 1.8f) * 1f - tilt * 0.4f + clamp(vy * 0.04f, -2f, 2f)
    val dome = Path().apply { moveTo(33.4f, 41f); cubicTo(32f, 27f, 40f, 20f, 50f + sw * 0.3f, 19.6f); cubicTo(60f, 20f, 68f, 27f, 66.6f, 41f); close() }
    drawPath(dome, Color(0xFFE0475E))
    clipPath(dome) {
        for (y in listOf(24.4f, 31.4f)) drawRect(Paper, Offset(28f, y), Size(44f, 3.2f))
        for (r in 0 until 9) for (c in 0 until 12) {
            val x = 33f + c * 3f + (r % 2) * 1.5f; val y = 21f + r * 2.4f
            drawPath(Path().apply { moveTo(x - 0.8f, y - 0.5f); lineTo(x, y + 0.5f); lineTo(x + 0.8f, y - 0.5f) }, hatC(0xFF500A1E, 0.18f), style = stroke(0.25f))
        }
        drawRect(vg(listOf(Color.White.copy(alpha = 0.18f), hatC(0xFF3C0014, 0.25f)), 20f, 41f), Offset(28f, 18f), Size(44f, 24f))
    }
    drawPath(dome, hatC(0xFF5A0A1E, 0.45f), style = stroke(0.4f))
    val cuff = Path().apply { moveTo(32.4f, 38.4f); quadraticTo(50f, 35.4f, 67.6f, 38.4f); lineTo(67.2f, 45f); quadraticTo(50f, 42f, 32.8f, 45f); close() }
    drawPath(cuff, vg(listOf(Color(0xFFF26A80), Color(0xFFB8304A)), 36f, 45f))
    clipPath(cuff) { var x = 33.4f; while (x < 67f) { line(hatC(0xFF5A0A1E, 0.3f), x, 34f, x, 46f, 0.45f); x += 2.2f } }
    drawPath(cuff, hatC(0xFF5A0A1E, 0.45f), style = stroke(0.35f))
    val px = 50f + sw; val py = 17.4f
    for (i in 0 until 8) { val a = i * PIf / 4f + 0.3f; drawCircle(Color(0xFFEFE6EC), 1.6f, Offset(px + cos(a) * 3.2f, py + sin(a) * 3.2f)) }
    drawCircle(Brush.radialGradient(listOf(Color.White, Color(0xFFE6DCE4)), center = Offset(px - 1.2f, py - 1.4f), radius = 4.4f), 3.6f, Offset(px, py))
}

private fun DrawScope.drawOrelhasGato(t: Float) {
    // tiara de orelhinhas de gato: arco fino preto e duas orelhas com o miolo rosa; a da direita mexe de vez em quando
    val tw = maxOf(0f, sin(t * 1.1f)).pow(16) * 0.3f
    val band = Path().apply { moveTo(30.6f, 47f); quadraticTo(50f, 33.4f, 69.4f, 47f) }
    drawPath(band, Color(0xFF1E1B2E), style = stroke(2f)); drawPath(band, Color(0xFF3A3550), style = stroke(0.9f))
    for (s in listOf(-1f, 1f)) translate(50f + s * 10.4f, 40.8f) { rotate((s * 0.3f + (if (s > 0) tw else 0f)) * DEG, pivot = Offset.Zero) {
        val ear = Path().apply { moveTo(-5.2f, 1.4f); quadraticTo(-4.4f, -5.4f, -0.6f, -9.4f); quadraticTo(0.3f, -10.2f, 1.2f, -9f); quadraticTo(4.6f, -4.6f, 5.2f, 1.4f); quadraticTo(0f, 2.4f, -5.2f, 1.4f); close() }
        drawPath(ear, vg(listOf(Color(0xFF4A4466), Color(0xFF1E1B2E)), -10f, 2f)); drawPath(ear, Color(0xFF14111F), style = stroke(0.4f))
        drawPath(Path().apply { moveTo(-3f, 0.6f); quadraticTo(-2.4f, -4.4f, 0f, -7f); quadraticTo(2.6f, -3.8f, 3f, 0.6f); quadraticTo(0f, 1.2f, -3f, 0.6f); close() }, vg(listOf(Color(0xFFFFB3C8), Color(0xFFF27A9C)), -7f, 1f))
        line(Color.White.copy(alpha = 0.3f), -3.6f, -1f, -1.2f, -7.4f, 0.4f)
    } }
}

private fun DrawScope.drawTiaraFlores(t: Float) {
    // tiara de flores: ramo verde em arco no alto do livro, folhinhas e florzinhas de cinco pétalas coloridas
    val vine = Path().apply { moveTo(31f, 45f); quadraticTo(50f, 34.6f, 69f, 45f) }
    drawPath(vine, Color(0xFF2E7D3A), style = stroke(1.3f)); drawPath(vine, Color(0xFF7ACB7E), style = stroke(0.4f))
    for (i in 0 until 8) { val q = hatQ(0.06f + i * 0.125f, 31f, 45f, 50f, 34.6f, 69f, 45f); val odd = i % 2 == 1; hatEll(if (odd) Color(0xFF4FAF5E) else Color(0xFF3E8E4E), q.x + (if (odd) 1.2f else -1.2f), q.y - 0.8f, 1.8f, 0.8f, if (odd) -0.7f else 0.7f) }
    val cols = listOf(0xFFFF8FA3, 0xFFFFD84A, 0xFFB995F5, 0xFF6FB0FF, 0xFFFF9F5A, 0xFFFF8FC4, 0xFF7EE0B8)
    cols.forEachIndexed { i, c ->
        val q = hatQ(0.04f + i * 0.153f, 31f, 45f, 50f, 34.6f, 69f, 45f); val r = if (i == 3) 3f else 2.4f; val cy = q.y - 0.6f + sin(t * 2f + i) * 0.35f
        for (k in 0 until 5) { val a = k * 2f * PIf / 5f + i; hatEll(Color(c), q.x + cos(a) * r * 0.6f, cy + sin(a) * r * 0.6f, r * 0.55f, r * 0.4f, a) }
        drawCircle(Color.Black.copy(alpha = 0.12f), r * 0.4f, Offset(q.x + 0.2f, cy + 0.3f)); drawCircle(if (i == 1) Color(0xFFF5A623) else Color(0xFFFFE07A), r * 0.36f, Offset(q.x, cy))
        drawCircle(Color.White.copy(alpha = 0.7f), r * 0.13f, Offset(q.x - r * 0.12f, cy - r * 0.14f))
    }
}

private fun DrawScope.drawBandana(t: Float) {
    // bandana amarrada: tecido vermelho de bolinhas brancas cobrindo o alto, nó do lado direito e as pontas
    // soltas balançando
    hatShadow(18f)
    val cap = Path().apply { moveTo(33.4f, 44.4f); cubicTo(32.4f, 30f, 40.4f, 23f, 50f, 23f); cubicTo(59.6f, 23f, 67.6f, 30f, 66.6f, 44.4f); quadraticTo(50f, 40.6f, 33.4f, 44.4f); close() }
    drawPath(cap, vg(listOf(Color(0xFFF0566E), Color(0xFFD23A55), Color(0xFF9C1F36)), 23f, 44f))
    clipPath(cap) {
        for (r in 0 until 5) for (c in 0 until 8) drawCircle(Color.White.copy(alpha = 0.92f), 0.85f, Offset(34f + c * 4.6f + (r % 2) * 2.3f, 25.6f + r * 4.2f))
        for ((x0, x1) in listOf(40f to 44f, 58f to 55f)) drawPath(Path().apply { moveTo(x0, 25f); quadraticTo((x0 + x1) / 2f + 1.6f, 33f, x1, 42f) }, hatC(0xFF5A0A1E, 0.3f), style = stroke(0.6f))
        hatGloss(42f, 28f, 11f, 0.3f)
    }
    drawPath(cap, hatC(0xFF5A0A1E, 0.5f), style = stroke(0.4f))
    drawPath(Path().apply { moveTo(34.4f, 42.6f); quadraticTo(50f, 39.2f, 65.6f, 42.6f) }, Color.White.copy(alpha = 0.55f), style = Stroke(0.25f, pathEffect = HatDash))
    val sw = sin(t * 2.4f) * 1.1f
    for ((i, la) in listOf(8.4f to 0.5f, 7f to 1.1f).withIndex()) {
        val (len, ang) = la; val d = i.toFloat()
        val ex = 66.4f + sin(ang) * len + sw * (1 - d * 0.4f); val ey = 41f + cos(ang) * len
        val tl = Path().apply { moveTo(65.6f, 39.6f); quadraticTo(69f + sw * 0.4f, 41f + d, ex + 1.2f, ey - 1f); lineTo(ex - 1f, ey + 0.6f); quadraticTo(67f + sw * 0.3f, 43.4f, 65.8f, 42.4f); close() }
        drawPath(tl, vg(listOf(Color(0xFFE0475E), Color(0xFF9C1F36)), 39f, ey + 1f)); drawPath(tl, hatC(0xFF5A0A1E, 0.45f), style = stroke(0.3f))
        drawCircle(Color.White.copy(alpha = 0.9f), 0.6f, Offset((65.8f + ex) / 2f + 0.6f, (41f + ey) / 2f))
    }
    hatEll(Color(0xFFC23250), 66.4f, 41f, 2.4f, 2f, 0.3f); drawCircle(Color.White.copy(alpha = 0.9f), 0.6f, Offset(66f, 40.4f)); line(hatC(0xFF5A0A1E, 0.4f), 65.2f, 42.2f, 67.6f, 40f, 0.35f)
}

private fun DrawScope.drawChef(t: Float) {
    // chapéu de chef: faixa branca pregueada e a copa alta e fofa, feita de gomos estufados (com um leve respirar)
    hatShadow(18f)
    val br = 1f + sin(t * 1.6f) * 0.015f
    scale(br, 2f - br, pivot = Offset(50f, 38f)) {
        val top = Path().apply {
            for ((x, y, r) in listOf(Triple(40.4f, 27f, 7.4f), Triple(50f, 21.6f, 8.6f), Triple(59.6f, 27f, 7.4f), Triple(44.4f, 32.4f, 7f), Triple(55.6f, 32.4f, 7f))) addOval(Rect(x - r, y - r, x + r, y + r))
            addRect(Rect(36f, 28f, 64f, 39f))
        }
        drawPath(top, hatC(0xFF3C385A, 0.4f), style = stroke(0.9f))
        drawPath(top, vg(listOf(Color.White, Color(0xFFF4F2FA), Color(0xFFD9D5E8)), 13f, 40f))
        for (q in listOf(floatArrayOf(45f, 18f, 44f, 24f, 46.6f, 29f), floatArrayOf(55f, 18f, 56f, 24f, 53.4f, 29f), floatArrayOf(50f, 28f, 49.4f, 32f, 50f, 37f)))
            drawPath(Path().apply { moveTo(q[0], q[1]); quadraticTo(q[2], q[3], q[4], q[5]) }, hatC(0xFF8C86AA, 0.4f), style = stroke(0.5f))
        hatGloss(46f, 19f, 6f, 0.8f)
    }
    val band = Path().apply { moveTo(35f, 37.2f); quadraticTo(50f, 34.8f, 65f, 37.2f); lineTo(65f, 44.4f); quadraticTo(50f, 41.8f, 35f, 44.4f); close() }
    drawPath(band, vg(listOf(Color.White, Color(0xFFE4E0F2)), 36f, 44.4f)); drawPath(band, hatC(0xFF3C385A, 0.35f), style = stroke(0.35f))
    var x = 38f; while (x < 64f) { val o = (x - 50f) * (x - 50f) * 0.008f; line(hatC(0xFF7872A0, 0.25f), x, 37.6f - o, x, 43.4f - o, 0.4f); x += 3f }
}

private fun DrawScope.drawPraia(t: Float) {
    // chapéu de praia de palha: aba bem larga ondulando, trama trançada, fita coral com laço e as pontas ao vento
    hatShadow(26f)
    val wv = sin(t * 1.4f) * 0.6f
    fun weave(from: Float, to: Float) { var x = from; while (x < to) { line(hatC(0xFF8C6428, 0.22f), x, 20f, x + 12f, 52f, 0.35f); line(hatC(0xFF8C6428, 0.22f), x + 12f, 20f, x, 52f, 0.35f); x += 1.8f } }
    val brim = Path().apply { moveTo(20f, 43.6f - wv); quadraticTo(22f, 38.6f, 50f, 38.4f); quadraticTo(78f, 38.6f, 80f, 43.6f + wv); quadraticTo(78f, 49.4f, 50f, 49.6f); quadraticTo(22f, 49.4f, 20f, 43.6f - wv); close() }
    drawPath(brim, vg(listOf(Color(0xFFF6E2A8), Color(0xFFE0C27A), Color(0xFFB8975A)), 38f, 49.6f))
    clipPath(brim) { weave(10f, 90f) }
    drawPath(brim, hatC(0xFF78501E, 0.5f), style = stroke(0.4f))
    val dome = Path().apply { moveTo(37f, 42.6f); cubicTo(36.2f, 29f, 42f, 24.4f, 50f, 24.4f); cubicTo(58f, 24.4f, 63.8f, 29f, 63f, 42.6f); quadraticTo(50f, 44.6f, 37f, 42.6f); close() }
    drawPath(dome, vg(listOf(Color(0xFFF8E8B8), Color(0xFFE2C47E), Color(0xFFB89656)), 24.4f, 43f))
    clipPath(dome) { weave(26f, 66f); hatGloss(44f, 29f, 10f, 0.35f) }
    drawPath(dome, hatC(0xFF78501E, 0.45f), style = stroke(0.35f))
    drawPath(Path().apply { moveTo(36.8f, 37.6f); quadraticTo(50f, 39.8f, 63.2f, 37.6f); lineTo(63.1f, 41.4f); quadraticTo(50f, 43.6f, 36.9f, 41.4f); close() }, vg(listOf(Color(0xFFFF8FA3), Color(0xFFF0566E)), 37.6f, 43.6f))
    drawPath(Path().apply { moveTo(37f, 39.6f); quadraticTo(50f, 41.8f, 63f, 39.6f) }, Color.White.copy(alpha = 0.75f), style = stroke(0.45f))
    val sw = sin(t * 2.6f) * 1.2f
    for ((ex, ey) in listOf(31f + sw to 47.6f, 34.4f + sw * 0.7f to 49.4f)) { val tl = Path().apply { moveTo(38.4f, 40.4f); quadraticTo(35f, 42f, ex, ey) }; drawPath(tl, Color(0xFFF0566E), style = stroke(1.3f)); drawPath(tl, Color.White.copy(alpha = 0.4f), style = stroke(0.3f)) }
    for (s in listOf(-1f, 1f)) hatEll(Color(0xFFFF7A90), 38.4f + s * 1.8f, 39.8f, 2f, 1.2f, s * 0.4f)
    drawCircle(Color(0xFFD63A5E), 0.9f, Offset(38.4f, 40f))
}

private fun DrawScope.drawPirata(t: Float) {
    // tricórnio de pirata: copa preta, aba dobrada para cima com as pontas, debrum dourado e a caveirinha fofa
    hatShadow(22f)
    drawPath(Path().apply { moveTo(37f, 40f); cubicTo(37f, 28f, 43f, 24f, 50f, 24f); cubicTo(57f, 24f, 63f, 28f, 63f, 40f); close() }, vg(listOf(Color(0xFF3A3650), Color(0xFF121018)), 24f, 40f))
    val brim = Path().apply { moveTo(24.4f, 30.6f); quadraticTo(36f, 36.4f, 50f, 31.4f); quadraticTo(64f, 36.4f, 75.6f, 30.6f); quadraticTo(72f, 41.4f, 66f, 44.6f); quadraticTo(50f, 41.6f, 34f, 44.6f); quadraticTo(28f, 41.4f, 24.4f, 30.6f); close() }
    drawPath(brim, vg(listOf(Color(0xFF3A3650), Color(0xFF1A1726), Color(0xFF0E0D14)), 30f, 45f)); drawPath(brim, Color.Black.copy(alpha = 0.55f), style = stroke(0.4f))
    val trim = Path().apply { moveTo(24.4f, 30.6f); quadraticTo(36f, 36.4f, 50f, 31.4f); quadraticTo(64f, 36.4f, 75.6f, 30.6f) }
    drawPath(trim, Color(0xFFB57A0C), style = stroke(1.2f)); drawPath(trim, Color(0xFFFFD877), style = stroke(0.5f))
    for (x in listOf(24.4f, 75.6f)) drawCircle(Gold, 0.9f, Offset(x, 30.6f))
    val sx = 50f; val sy = 37.4f
    for (s in listOf(-1f, 1f)) {
        line(Color(0xFFE8E2D4), sx - 4.4f * s, sy - 3.4f, sx + 4.4f * s, sy + 3.4f, 1.1f)
        for (e in listOf(-1f, 1f)) { drawCircle(Color(0xFFFFFDF4), 0.8f, Offset(sx + e * 4.6f * s - 0.4f * s * e, sy + e * 3.6f - 0.5f)); drawCircle(Color(0xFFFFFDF4), 0.8f, Offset(sx + e * 4.6f * s + 0.4f * s * e, sy + e * 3.6f + 0.5f)) }
    }
    val sk = Path().apply { arcTo(Rect(sx - 3.4f, sy - 4f, sx + 3.4f, sy + 2.8f), 147.6f, 244.8f, true); lineTo(sx + 2f, sy + 3f); lineTo(sx - 2f, sy + 3f); close() }
    drawPath(sk, vg(listOf(Color.White, Color(0xFFE4DFD0)), sy - 4f, sy + 3f))
    drawCircle(Color(0xFF14111F), 1f, Offset(sx - 1.3f, sy - 0.6f)); drawCircle(Color(0xFF14111F), 1f, Offset(sx + 1.3f, sy - 0.6f))
    drawCircle(Color.White.copy(alpha = 0.8f), 0.3f, Offset(sx - 1.6f, sy - 0.9f)); drawCircle(Color.White.copy(alpha = 0.8f), 0.3f, Offset(sx + 1f, sy - 0.9f))
    drawCircle(Cheek.copy(alpha = 0.7f), 0.6f, Offset(sx - 2.4f, sy + 0.8f)); drawCircle(Cheek.copy(alpha = 0.7f), 0.6f, Offset(sx + 2.4f, sy + 0.8f))
    for (x in listOf(-1f, 0f, 1f)) line(hatC(0xFF14111F, 0.5f), sx + x * 0.8f, sy + 1.8f, sx + x * 0.8f, sy + 2.8f, 0.25f)
    hatSpark(66f, 34.4f, 2.6f, maxOf(0f, sin(t * 1.2f + 0.4f)).pow(10))
}

private fun DrawScope.drawViking(t: Float) {
    // capacete viking: dois chifres de marfim curvados para os lados, casco de metal polido, faixa de bronze em
    // cruz com rebites
    hatShadow(19f)
    for (s in listOf(-1f, 1f)) {
        val h = Path().apply { moveTo(50f + s * 13.4f, 33.4f); cubicTo(50f + s * 20f, 33f, 50f + s * 24.6f, 28f, 50f + s * 24.6f, 18.6f); cubicTo(50f + s * 22.6f, 25f, 50f + s * 19f, 28.6f, 50f + s * 14.6f, 38.4f); close() }
        drawPath(h, vg(listOf(Color(0xFFFFFDF4), Color(0xFFEDE0C0), Color(0xFFC9B48A)), 18f, 38f)); drawPath(h, hatC(0xFF6E501E, 0.5f), style = stroke(0.35f))
        for (i in 0 until 3) line(hatC(0xFF8C6428, 0.35f), 50f + s * (16.4f + i * 2.2f), 37f - i * 1.6f, 50f + s * (17.4f + i * 2.4f), 32.8f - i * 2f, 0.35f)
        line(Color.White.copy(alpha = 0.7f), 50f + s * 16f, 33.8f, 50f + s * 22.6f, 24f, 0.4f)
    }
    val dome = Path().apply { moveTo(33.4f, 43.6f); cubicTo(32.4f, 28.6f, 40.6f, 22f, 50f, 22f); cubicTo(59.4f, 22f, 67.6f, 28.6f, 66.6f, 43.6f); quadraticTo(50f, 40.6f, 33.4f, 43.6f); close() }
    drawPath(dome, hg(listOf(Color(0xFF7E8498), Color(0xFFD8DCE6), Color(0xFFA4AABB), Color(0xFF6A7084)), 33f, 67f))
    clipPath(dome) { hatGloss(43f, 27f, 10f, 0.5f); drawRect(hg(listOf(Color(0xFFB57A0C), Color(0xFFF5C451), Color(0xFFB57A0C)), 47.8f, 52.2f), Offset(47.8f, 20f), Size(4.4f, 24f)) }
    drawPath(dome, hatC(0xFF282C3C, 0.5f), style = stroke(0.4f))
    val rim = Path().apply { moveTo(33f, 40.2f); quadraticTo(50f, 37.2f, 67f, 40.2f); lineTo(66.7f, 44.2f); quadraticTo(50f, 41.2f, 33.3f, 44.2f); close() }
    drawPath(rim, vg(listOf(Color(0xFFF5C451), Color(0xFFD99A1B), Color(0xFF9C6A12)), 37.6f, 44.2f)); drawPath(rim, hatC(0xFF6E4600, 0.55f), style = stroke(0.3f))
    for (i in 0 until 7) { val q = hatQ(0.08f + i * 0.14f, 33.2f, 42.2f, 50f, 39.2f, 66.8f, 42.2f); drawCircle(Color(0xFF9C6A12), 0.65f, q); drawCircle(Color(0xFFFFF1BF), 0.3f, Offset(q.x - 0.2f, q.y - 0.2f)) }
    for (y in listOf(26f, 31f, 36f)) { drawCircle(Color(0xFF9C6A12), 0.6f, Offset(50f, y)); drawCircle(Color(0xFFFFF1BF), 0.25f, Offset(49.8f, y - 0.2f)) }
    hatSpark(41.6f, 27.4f, 2.8f, maxOf(0f, sin(t * 1.3f + 2f)).pow(10))
}

private fun DrawScope.drawAstronauta(t: Float) {
    // capacete de astronauta (lendário): bolha de vidro em volta do alto do livro, com reflexo curvo, estrelinhas
    // cintilando no vidro, e a gola branca com as luzinhas piscando
    val bub = Path().apply { moveTo(12.6f, 47f); cubicTo(9f, 4f, 91f, 4f, 87.4f, 47f); close() }
    drawPath(bub, Brush.radialGradient(listOf(hatC(0xFFDCF0FF, 0.08f), hatC(0xFF78B4F0, 0.26f)), center = Offset(44f, 26f), radius = 42f))
    drawPath(bub, hatC(0xFF283C6E, 0.35f), style = stroke(1f)); drawPath(bub, hatC(0xFFDCF0FF, 0.85f), style = stroke(0.5f))
    drawPath(Path().apply { moveTo(19.4f, 38f); cubicTo(18.4f, 25f, 27f, 17f, 38f, 14.6f) }, Color.White.copy(alpha = 0.55f), style = stroke(2.2f))
    drawPath(Path().apply { moveTo(24f, 38.6f); cubicTo(23.6f, 31f, 26.6f, 26f, 30.4f, 23.2f) }, Color.White.copy(alpha = 0.35f), style = stroke(0.7f))
    line(Color.White.copy(alpha = 0.45f), 77f, 22f, 80.4f, 27f, 0.9f)
    listOf(28f to 30f, 72f to 34f, 64f to 20f, 36f to 22f, 81f to 40f).forEachIndexed { i, (x, y) -> val k = 0.5f + 0.5f * sin(t * 2.6f + i * 1.7f); star4(x, y, 1.4f * k, Color.White.copy(alpha = 0.85f * k)) }
    val u = (t * 0.2f) % 1f; drawCircle(Color.White.copy(alpha = 0.7f * sin(PIf * u)), 0.9f, hatQ(u, 14f, 40f, 50f, 0f, 86f, 40f))
    val col = Path().apply { moveTo(11f, 45f); quadraticTo(50f, 39.4f, 89f, 45f); lineTo(89f, 50f); quadraticTo(50f, 44.4f, 11f, 50f); close() }
    drawPath(col, vg(listOf(Color.White, Color(0xFFDCD8EA), Color(0xFFA8A4BE)), 41f, 50f)); drawPath(col, hatC(0xFF282446, 0.45f), style = stroke(0.4f))
    drawPath(Path().apply { moveTo(11.4f, 45.6f); quadraticTo(50f, 40f, 88.6f, 45.6f) }, Gold, style = stroke(0.6f))
    for ((x, c, ph) in listOf(Triple(70f, 0xFFFF5A6EL, 0f), Triple(74.6f, 0xFF7EE0B8L, 1.4f), Triple(79.2f, 0xFF6FB0FFL, 2.8f))) {
        val k = 0.5f + 0.5f * sin(t * 4f + ph); val y = 46.6f - (x - 50f) * (x - 50f) * 0.0004f + 0.6f
        drawCircle(hatC(c, 0.3f * k), 1.4f, Offset(x, y)); drawCircle(hatC(c, 0.6f + 0.4f * k), 0.65f, Offset(x, y))
    }
}

/** O chapéu escolhido (no frame girado do capelo). */
private fun DrawScope.drawHat(f: FolhaFrame, look: Look, tilt: Float) {
    val t = f.t; val small = f.small
    when (look.hat) {
        "bone" -> drawBone()
        "boina" -> drawBoina()
        "boina-militar" -> drawBoinaMilitar(t)
        "touca" -> drawTouca(t)
        "quepe" -> drawQuepe(t)
        "viseira" -> drawViseira(t)
        "explorador" -> drawExplorador()
        "fones" -> drawFones(t)
        "cartola" -> drawCartola()
        "coroa" -> drawCoroa(t)
        "mago" -> drawMago(t, tilt, f.vy)
        "aureola" -> drawAureola(t)
        "espelho-frontal" -> drawEspelhoFrontal(t)
        "capacete" -> drawCapacete(t)
        "bone-tatico" -> drawBoneTatico(t)
        "barrete" -> drawBarrete(t)
        "fedora" -> drawFedora()
        "coco" -> drawCoco(t)
        "chapeu-lapis" -> drawChapeuLapis(t)
        "headset" -> drawHeadset(t)
        "clipe" -> drawClipe(t)
        "gorro" -> drawGorro(t, tilt, f.vy)
        "orelhas-gato" -> drawOrelhasGato(t)
        "tiara-flores" -> drawTiaraFlores(t)
        "bandana" -> drawBandana(t)
        "chef" -> drawChef(t)
        "praia" -> drawPraia(t)
        "pirata" -> drawPirata(t)
        "viking" -> drawViking(t)
        "astronauta" -> drawAstronauta(t)
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
        "aviador" -> {
            // óculos de aviador: lentes em gota, espelhadas (verde no alto, dourado embaixo), aro fino e ponte dupla
            val lw = if (small) 1.4f else 0.85f; val frame = Color(0xFFB8902E)
            for ((c, s) in listOf(eyes[0] to -1f, eyes[1] to 1f)) {
                val x = c.x; val y = c.y
                val p = Path().apply { moveTo(x - s * 6.8f, y - 5.2f); quadraticTo(x, y - 6.6f, x + s * 7.8f, y - 4.8f); cubicTo(x + s * 9f, y + 1f, x + s * 6f, y + 7.2f, x + s * 1.2f, y + 7.2f); cubicTo(x - s * 4.6f, y + 7.2f, x - s * 7.6f, y + 1f, x - s * 6.8f, y - 5.2f); close() }
                drawPath(p, vg(listOf(Color(0xFF2F5A4A), Color(0xFF3E4A3A), Color(0xFF8A6A2A), Color(0xFFD9A441)), y - 6f, y + 7.2f))
                if (!small) clipPath(p) { val k = (t * 0.25f) % 1f; line(Color.White.copy(alpha = 0.32f), x - 9f + k * 6f, y + 8f, x - 1f + k * 6f, y - 8f, 2.6f); line(Color.White.copy(alpha = 0.2f), x - 3f + k * 6f, y + 8f, x + 2f + k * 6f, y - 8f, 0.9f) }
                drawPath(p, vg(listOf(Color(0xFFFFF1BF), Color(0xFFC9A040), Color(0xFF8A6A22)), y - 6f, y + 7f), style = stroke(lw))
            }
            bridge(frame, lw, 56.6f, 55.8f); bridge(frame, lw * 0.8f, 59.6f, 59.2f); arms(frame, lw)
        }
        "meia-lua" -> {
            // óculos meia-lua de juiz: só a metade de baixo, mais embaixo no rosto; os olhos espiam por cima
            val lw = if (small) 1.4f else 0.8f; val yo = 3.2f; val frame = Color(0xFFC98A12)
            for (c in eyes) {
                val x = c.x; val y = c.y
                val p = Path().apply { moveTo(x - 6.6f, y + yo - 0.6f); lineTo(x + 6.6f, y + yo - 0.6f); cubicTo(x + 6.6f, y + yo + 4.6f, x + 3.6f, y + yo + 6f, x, y + yo + 6f); cubicTo(x - 3.6f, y + yo + 6f, x - 6.6f, y + yo + 4.6f, x - 6.6f, y + yo - 0.6f); close() }
                drawPath(p, Color(0x33D2EBFF))
                drawPath(p, vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), y + yo, y + yo + 6f), style = stroke(lw))
                if (!small) drawArc(Color.White.copy(alpha = 0.8f), 189f, 72f, false, Offset(x - 5f, y + yo + 1.2f), Size(6.8f, 3.6f), style = stroke(0.45f))
            }
            bridge(frame, lw, 63.4f, 62.2f)
            line(frame, eyes[0].x - 6.6f, eyes[0].y + yo - 0.4f, 18.5f, 58.6f, lw * 0.9f); line(frame, eyes[1].x + 6.6f, eyes[1].y + yo - 0.4f, 81.5f, 58.6f, lw * 0.9f)
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
        "protecao" -> {
            // óculos de proteção: uma lente só, transparente e envolvente, borda de borracha azul, respiros e elástico
            val lw = if (small) 1.8f else 1.2f; val strap = Color(0xFF2E6FB8)
            line(strap, 20f + dx, 59f + dy, 16f, 58f, if (small) 2.6f else 2.2f); line(strap, 80f + dx, 59f + dy, 84f, 58f, if (small) 2.6f else 2.2f)
            translate(dx, dy) {
                val p = Path().apply { moveTo(20.4f, 55.4f); quadraticTo(50f, 51.4f, 79.6f, 55.4f); quadraticTo(82f, 62f, 78f, 67.6f); quadraticTo(70f, 70.6f, 60f, 68f); quadraticTo(55f, 63.6f, 50f, 63.4f); quadraticTo(45f, 63.6f, 40f, 68f); quadraticTo(30f, 70.6f, 22f, 67.6f); quadraticTo(18f, 62f, 20.4f, 55.4f); close() }
                drawPath(p, hatC(0xFFC8F0FF, 0.24f))
                if (!small) clipPath(p) { line(Color.White.copy(alpha = 0.4f), 24f, 70f, 34f, 52f, 2.4f); line(Color.White.copy(alpha = 0.25f), 62f, 70f, 70f, 52f, 1.2f) }
                drawPath(p, Color(0xFF5BA8E6), style = stroke(lw)); drawPath(p, Color.White.copy(alpha = 0.45f), style = stroke(0.3f))
                if (!small) for (x in listOf(28f, 32f, 68f, 72f)) line(strap, x, 55.2f, x + 0.4f, 56.8f, 0.6f)
            }
        }
        "tatico" -> {
            // óculos táticos: viseira envolvente espelhada (azul que vira laranja), barra preta no alto e reflexo correndo
            val lw = if (small) 1.8f else 1.2f; val frame = Color(0xFF14111F)
            translate(dx, dy) {
                val p = Path().apply { moveTo(19f, 56.4f); quadraticTo(50f, 52f, 81f, 56.4f); quadraticTo(82f, 61.4f, 78.6f, 65.4f); quadraticTo(68f, 69.4f, 58f, 65.6f); quadraticTo(54f, 61.6f, 50f, 61.4f); quadraticTo(46f, 61.6f, 42f, 65.6f); quadraticTo(32f, 69.4f, 21.4f, 65.4f); quadraticTo(18f, 61.4f, 19f, 56.4f); close() }
                drawPath(p, vg(listOf(Color(0xFF1E2A5E), Color(0xFF3A5AC0), Color(0xFFE07A3A), Color(0xFFF5C451)), 53f, 68f))
                if (!small) clipPath(p) { val k = (t * 0.25f) % 1f; line(Color.White.copy(alpha = 0.35f), 14f + k * 70f, 70f, 22f + k * 70f, 50f, 3f); line(Color.White.copy(alpha = 0.2f), 20f + k * 70f, 70f, 25f + k * 70f, 50f, 1f) }
                drawPath(p, frame, style = stroke(lw))
                drawPath(Path().apply { moveTo(19.4f, 56.6f); quadraticTo(50f, 52.2f, 80.6f, 56.6f) }, frame, style = stroke(lw * 1.8f))
            }
            arms(frame, lw * 1.2f)
        }
        "pincene" -> {
            // pincenê: duas lentes ovais presas no nariz pela mola, sem hastes, com a correntinha dourada pendurada
            val lw = if (small) 1.5f else 0.8f
            for (c in eyes) {
                oval(hatC(0xFFD2EBFF, 0.18f), c.x - 6.2f, c.y + 0.6f - 5.4f, 12.4f, 10.8f)
                drawOval(vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), c.y - 5f, c.y + 6f), Offset(c.x - 6.2f, c.y + 0.6f - 5.4f), Size(12.4f, 10.8f), style = Stroke(lw))
                glare(Offset(c.x, c.y + 0.6f), 4.8f, 4.2f)
            }
            drawPath(Path().apply { moveTo(37.2f + dx, 59.6f + dy); cubicTo(42f + dx, 54.4f + dy, 45f + dx, 58.6f + dy, 50f + dx, 56.2f + dy); cubicTo(55f + dx, 58.6f + dy, 58f + dx, 54.4f + dy, 62.8f + dx, 59.6f + dy) }, Color(0xFFC98A12), style = stroke(lw))
            if (!small) {
                for (x in listOf(45f, 55f)) hatEll(Color.White.copy(alpha = 0.7f), x + dx, 61.4f + dy, 0.8f, 1.2f)
                val sw = sin(t * 1.6f) * 0.8f; val l = eyes[0]
                for (i in 0..12) {
                    val u = i / 12f; val cx = l.x - 6f + (18f - l.x + 6f) * u - sin(u * PIf) * 1.6f + sw * sin(u * PIf); val cy = l.y + 2f + (84f - l.y - 2f) * u + sin(u * PIf) * 3f
                    rotate(u * 3f * DEG, pivot = Offset(cx, cy)) { drawOval(Gold, Offset(cx - 0.55f, cy - 0.35f), Size(1.1f, 0.7f), style = Stroke(0.28f)) }
                }
            }
        }
        "lupa-dupla" -> {
            // óculos de lupa: lentes grossas e redondas de aumento, armação preta pesada, anéis de vidro que deixam o
            // olhar maior e a lupinha extra levantada no canto
            val lw = if (small) 2.2f else 1.8f; val frame = Color(0xFF1E1B2E)
            for (c in eyes) {
                drawCircle(Brush.radialGradient(0f to hatC(0xFFE6F5FF, 0.05f), 0.7f to hatC(0xFFC8E6FF, 0.18f), 1f to hatC(0xFF78AAE6, 0.5f), center = c, radius = 8.2f), 8.2f, c)
                if (!small) for (r in listOf(6.6f, 4.8f)) drawArc(Color.White.copy(alpha = 0.4f), 162f, 126f, false, Offset(c.x - r, c.y - r), Size(r * 2, r * 2), style = stroke(0.35f))
                drawCircle(frame, 8.2f, c, style = Stroke(lw))
                drawArc(Color.White.copy(alpha = 0.35f), 198f, 72f, false, Offset(c.x - 8.2f, c.y - 8.2f), Size(16.4f, 16.4f), style = stroke(0.4f))
                glare(c, 6.4f, 6.4f)
            }
            bridge(frame, lw, 59.4f, 57.4f); arms(frame, lw)
            if (!small) {
                val c = eyes[1]; val hx = c.x + 6.4f; val hy = c.y - 7f; val lc = Offset(hx + 2f, hy - 2.2f)
                line(Color(0xFF3A3550), c.x + 5.4f, c.y - 5.4f, hx, hy, 0.7f)
                drawCircle(hatC(0xFFC8E6FF, 0.35f), 2.6f, lc); drawCircle(frame, 2.6f, lc, style = Stroke(0.9f))
                drawArc(Color.White.copy(alpha = 0.8f), 198f, 72f, false, Offset(lc.x - 1.8f, lc.y - 1.8f), Size(3.6f, 3.6f), style = stroke(0.35f))
            }
        }
        "auditor" -> {
            // óculos de auditor: retangulares de armação fina de metal grafite, barrinha reforçada em cima
            val lw = if (small) 1.5f else 0.75f; val frame = Color(0xFF4A5068)
            for (c in eyes) {
                val r = roundRect(c.x - 7.6f, c.y - 4.4f, 15.2f, 9f, 1.2f)
                drawPath(r, hatC(0xFFD2E6FA, 0.16f)); drawPath(r, frame, style = stroke(lw))
                line(Color(0xFF2A2E40), c.x - 7.4f, c.y - 4.2f, c.x + 7.4f, c.y - 4.2f, lw * 1.8f)
                glare(c, 5.6f, 3.4f)
            }
            bridge(frame, lw, 57.6f, 56.6f); arms(frame, lw)
            if (!small) for (x in listOf(44.8f, 55.2f)) drawCircle(Color.White.copy(alpha = 0.6f), 0.5f, Offset(x + dx, 62.6f + dy))
        }
        "dourados" -> {
            // armação dourada de banqueiro: aro largo de ouro polido, lente levemente âmbar e um brilho que passa
            val lw = if (small) 2f else 1.4f
            for (c in eyes) {
                val r = roundRect(c.x - 7.4f, c.y - 6f, 14.8f, 12f, 4.8f)
                drawPath(r, hatC(0xFFFFDC96, 0.18f))
                drawPath(r, Color(0xFF9C6A12), style = stroke(lw + 0.5f)); drawPath(r, vg(listOf(Color(0xFFFFF1BF), Color(0xFFF5C451), Color(0xFFB57A0C)), c.y - 6f, c.y + 6f), style = stroke(lw))
                glare(c, 5.4f, 4.4f)
            }
            bridge(Color(0xFFD99A1B), lw, 58.6f, 57f); arms(Color(0xFFD99A1B), lw)
            if (!small) hatSpark(eyes[1].x + 6.4f, eyes[1].y - 5.6f, 2.6f, maxOf(0f, sin(t * 1.4f)).pow(6))
        }
        "redondos" -> {
            // óculos redondos de tartaruga: aros grossos bem redondos, pintinhas de casco e ponte em buraco de fechadura
            val lw = if (small) 2.4f else 1.9f; val shell = Color(0xFF7A4A22)
            for (c in eyes) {
                drawCircle(hatC(0xFFFFEBC8, 0.14f), 7.4f, c)
                drawCircle(shell, 7.4f, c, style = Stroke(lw))
                if (!small) {
                    drawCircle(Color(0xFFC98A4B), 7.4f, c, style = Stroke(lw * 0.6f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(1.2f, 1.6f))))
                    drawCircle(Color(0xFF3E2410), 7.4f, c, style = Stroke(lw * 0.6f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.8f, 2.6f), 1.4f)))
                }
                glare(c, 5.8f, 5.8f)
            }
            drawPath(Path().apply { moveTo(38.4f + dx, 59.6f + dy); quadraticTo(41f + dx, 57.2f + dy, 44.6f + dx, 58.2f + dy); quadraticTo(50f + dx, 54.2f + dy, 55.4f + dx, 58.2f + dy); quadraticTo(59f + dx, 57.2f + dy, 61.6f + dx, 59.6f + dy) }, shell, style = stroke(lw * 0.8f))
            arms(shell, lw * 0.7f)
        }
        "gatinho" -> {
            // óculos gatinho: cantos de fora puxados para cima em ponta, armação vinho-rosa e strass nas pontas
            val lw = if (small) 1.9f else 1.4f
            for ((c, o) in listOf(eyes[0] to -1f, eyes[1] to 1f)) {
                val x = c.x; val y = c.y
                val p = Path().apply { moveTo(x - o * 6.6f, y - 3.4f); quadraticTo(x, y - 5.4f, x + o * 7.4f, y - 5.2f); lineTo(x + o * 10f, y - 8.4f); quadraticTo(x + o * 9.6f, y + 1f, x + o * 5f, y + 4.8f); quadraticTo(x, y + 7f, x - o * 5f, y + 4.4f); quadraticTo(x - o * 7.6f, y + 1.6f, x - o * 6.6f, y - 3.4f); close() }
                drawPath(p, hatC(0xFFFFBEDC, 0.2f))
                drawPath(p, vg(listOf(Color(0xFFFF7AB0), Color(0xFFD63A7A), Color(0xFF9C1F56)), y - 8f, y + 6f), style = stroke(lw))
                glare(c, 5f, 4.2f)
                if (!small) { drawCircle(Color.White, 0.6f, Offset(x + o * 8.4f, y - 6.4f)); val k = 0.5f + 0.5f * sin(t * 3f + o); star4(x + o * 8.4f, y - 6.4f, 1.8f * k, Color.White.copy(alpha = 0.9f * k)) }
            }
            bridge(Color(0xFFD63A7A), lw, 58.4f, 57f); arms(Color(0xFFD63A7A), lw)
        }
        "vr" -> {
            // óculos de realidade virtual: caixa escura cobrindo os olhos, faixa até os lados, tela da frente com brilho
            // de cores mudando e o sensor piscando
            val sw = if (small) 3f else 2.6f
            line(Color(0xFF2A2640), 18f + dx, 60f + dy, 12f, 58.6f, sw); line(Color(0xFF2A2640), 82f + dx, 60f + dy, 88f, 58.6f, sw)
            translate(dx, dy) {
                val box = roundRect(18f, 52f, 64f, 18.6f, 6f)
                drawPath(box, vg(listOf(Color(0xFF5A5480), Color(0xFF2A2640), Color(0xFF14111F)), 52f, 70.6f)); drawPath(box, Color(0xFF0E0D14), style = stroke(0.6f))
                val sc = roundRect(21.4f, 54.6f, 57.2f, 13.4f, 4.4f); val ph = (t * 0.2f) % 1f
                drawPath(sc, Brush.linearGradient(0f to Color(0xFF5247E0), 0.35f to Color(0xFF36C8C0), 0.65f to Color(0xFFB995F5), 1f to Color(0xFF5247E0), start = Offset(21f + ph * 40f - 40f, 0f), end = Offset(79f + ph * 40f, 0f)))
                drawPath(sc, vg(listOf(Color.White.copy(alpha = 0.35f), Color.White.copy(alpha = 0f), Color.Black.copy(alpha = 0.25f)), 54.6f, 68f))
                if (!small) { line(Color.White.copy(alpha = 0.6f), 25f, 56.6f, 36f, 56.2f, 0.6f); val k = 0.5f + 0.5f * sin(t * 4f); drawCircle(Mint.copy(alpha = 0.4f + 0.6f * k), 0.7f, Offset(74.6f, 57.6f)); drawCircle(Color(0xFF14111F), 0.9f, Offset(50f, 54f)) }
            }
        }
        "neon" -> {
            // óculos neon: armação que brilha e troca de cor devagar (rosa, laranja, verde, azul...)
            val lw = if (small) 1.9f else 1.3f; val hue = (t * 40f) % 360f
            val col = Color.hsl(hue, 1f, 0.62f); val glow = col.copy(alpha = 0.35f)
            for (c in eyes) {
                val r = roundRect(c.x - 7.6f, c.y - 5.8f, 15.2f, 11.8f, 4.2f)
                drawPath(r, Color.hsl(hue, 1f, 0.7f, 0.14f))
                drawPath(r, glow, style = stroke(lw * 3f)); drawPath(r, col, style = stroke(lw)); drawPath(r, Color.White.copy(alpha = 0.6f), style = stroke(lw * 0.3f))
                glare(c, 5.4f, 4.2f)
            }
            bridge(glow, lw * 3f, 58.6f, 57.4f); bridge(col, lw, 58.6f, 57.4f); arms(col, lw)
        }
        "3d" -> {
            // óculos 3D de papelão: armação branca de papel com a dobrinha, lente vermelha na esquerda e azul na direita
            line(PaperEdge, 20f + dx, 59f + dy, 17f, 58f, if (small) 2.4f else 2f); line(PaperEdge, 80f + dx, 59f + dy, 83f, 58f, if (small) 2.4f else 2f)
            translate(dx, dy) {
                val fr = roundRect(19.4f, 54f, 61.2f, 14.4f, 1.6f); drawPath(fr, Paper); drawPath(fr, hatC(0xFF50463C, 0.35f), style = stroke(0.4f))
                drawPath(roundRect(22.6f, 56f, 18.8f, 10.4f, 1f), hatC(0xFFE6283C, 0.72f))
                drawPath(roundRect(58.6f, 56f, 18.8f, 10.4f, 1f), hatC(0xFF28B4E6, 0.72f))
                if (!small) {
                    line(Color.White.copy(alpha = 0.5f), 24f, 64.6f, 28.6f, 57.4f, 1f); line(Color.White.copy(alpha = 0.5f), 60f, 64.6f, 64.6f, 57.4f, 1f)
                    drawPath(Path().apply { moveTo(45f, 68.4f); quadraticTo(50f, 62.6f, 55f, 68.4f); close() }, Paper); line(hatC(0xFF50463C, 0.25f), 50f, 54.4f, 50f, 61f, 0.3f)
                }
            }
        }
        "mergulho" -> {
            // máscara de mergulho: borda de silicone turquesa em volta dos dois olhos com o bolsinho do nariz, vidro
            // com reflexo, alça larga e o snorkel laranja do lado, soltando bolhas
            val lw = if (small) 2.6f else 2.2f
            line(Color(0xFF1A8296), 19f + dx, 61f + dy, 14f, 59f, lw * 1.3f); line(Color(0xFF1A8296), 81f + dx, 61f + dy, 86f, 59f, lw * 1.3f)
            if (!small) {
                val sn = Path().apply { moveTo(80f + dx, 70f + dy); quadraticTo(89f, 66f, 88.4f, 50f); lineTo(88f, 30f); quadraticTo(88f, 26f, 85f, 25f) }
                drawPath(sn, Color(0xFF9C4A10), style = stroke(3f)); drawPath(sn, Color(0xFFFF8A2B), style = stroke(2.2f)); drawPath(sn, Color.White.copy(alpha = 0.4f), style = stroke(0.5f))
                drawPath(roundRect(82.6f, 22.6f, 4.6f, 3.6f, 1.2f), Color(0xFF1A8296))
                for (i in 0 until 3) { val ph = (t * 0.6f + i / 3f) % 1f; drawCircle(hatC(0xFF96DCFF, 1f - ph), 0.6f + ph * 0.8f, Offset(83f + sin(ph * 9f + i) * 1.2f, 22f - ph * 10f), style = Stroke(0.35f)) }
            }
            translate(dx, dy) {
                val p = Path().apply { moveTo(20f, 56f); quadraticTo(50f, 50.6f, 80f, 56f); quadraticTo(83f, 64f, 77f, 69.4f); quadraticTo(66f, 72f, 58f, 68.6f); quadraticTo(54f, 72f, 50f, 72.6f); quadraticTo(46f, 72f, 42f, 68.6f); quadraticTo(34f, 72f, 23f, 69.4f); quadraticTo(17f, 64f, 20f, 56f); close() }
                drawPath(p, hatC(0xFFAAEBFF, 0.26f))
                if (!small) clipPath(p) { line(Color.White.copy(alpha = 0.45f), 26f, 72f, 36f, 52f, 2.6f); line(Color.White.copy(alpha = 0.25f), 34f, 72f, 41f, 52f, 0.9f) }
                drawPath(p, Color(0xFF1A8296), style = stroke(lw * 1.3f)); drawPath(p, Color(0xFF36C8C0), style = stroke(lw * 0.8f)); drawPath(p, Color.White.copy(alpha = 0.4f), style = stroke(0.3f))
            }
        }
    }
}

// ---- peito (embaixo da boca, no pé das páginas)
// As peças do peito no tamanho certo: os broches e medalhas ficam presos sempre no mesmo lugar (lado
// esquerdo, abaixo do olho) e maiores, para lerem de longe; gravatas e laços no meio, logo abaixo da boca.
private val NECK_ZOOM = mapOf("borboleta" to floatArrayOf(50f, 83.4f, 1.35f), "medalha" to floatArrayOf(25.6f, 73.2f, 1.4f), "roseta" to floatArrayOf(24.5f, 78.4f, 1.45f),
    "medalha-militar" to floatArrayOf(25f, 71.6f, 1.4f), "balanca" to floatArrayOf(24.8f, 72.4f, 1.5f), "broche" to floatArrayOf(23.5f, 79.6f, 1.55f), "flor" to floatArrayOf(76.5f, 79.6f, 1.45f), "caneta" to floatArrayOf(26f, 70f, 1.3f),
    "cruz-saude" to floatArrayOf(24.5f, 77f, 1.4f), "divisas" to floatArrayOf(24.5f, 75.5f, 1.35f), "brasao" to floatArrayOf(24.5f, 76f, 1.4f), "leao" to floatArrayOf(24.5f, 77f, 1.4f),
    "selo" to floatArrayOf(24.5f, 75.5f, 1.35f), "parlamentar" to floatArrayOf(24.5f, 77f, 1.4f), "estrelinha" to floatArrayOf(24.5f, 77.5f, 1.35f), "postits" to floatArrayOf(24.5f, 76f, 1.3f), "sininho" to floatArrayOf(50f, 83f, 1.3f))

private fun DrawScope.drawNeckwear(t: Float, small: Boolean, kind: String) {
    val z = NECK_ZOOM[kind]
    if (z == null) drawNeckwearRaw(t, small, kind) else scale(z[2], z[2], pivot = Offset(z[0], z[1])) { drawNeckwearRaw(t, small, kind) }
}

private fun DrawScope.drawNeckwearRaw(t: Float, small: Boolean, kind: String) {
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
            // medalha de ouro presa no peito, do lado esquerdo: presilha dourada, fita listrada (azul, branco e
            // vermelho) com a ponta em V, e o disco pendurado que balança de leve, com louros, estrela e brilho
            val px = 25.6f; val py = 73.2f; val sw = sin(t * 2.1f) * 0.07f
            val rib = Path().apply { moveTo(px - 3.4f, py); lineTo(px + 3.4f, py); lineTo(px + 3.4f, py + 5.4f); lineTo(px, py + 6.8f); lineTo(px - 3.4f, py + 5.4f); close() }
            clipPath(rib) {
                for ((x0, w, c) in listOf(Triple(-3.4f, 2.4f, Color(0xFF2F6FD0)), Triple(-1f, 2f, Color.White), Triple(1f, 2.4f, Color(0xFFD63A55)))) drawRect(c, Offset(px + x0, py), Size(w, 7f))
                drawRect(Color.Black.copy(alpha = 0.12f), Offset(px + 1.6f, py), Size(2f, 7f)); line(Color.White.copy(alpha = 0.4f), px - 3f, py + 1f, px - 3f, py + 5.2f, 0.35f)
            }
            drawPath(rib, Color(0x5914143C), style = stroke(0.3f))
            drawPath(roundRect(px - 3.9f, py - 1.1f, 7.8f, 1.6f, 0.6f), vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), py - 1.1f, py + 0.5f))
            translate(px, py + 6.4f) {
                rotate(sw * DEG, pivot = Offset.Zero) {
                    drawCircle(Color(0xFFB57A0C), 0.9f, Offset(0f, 0.6f), style = Stroke(0.5f))
                    val my = 5.6f; val r = 4.5f
                    drawCircle(Color(0x593C1E00), r + 0.2f, Offset(0.3f, my + 0.5f))
                    drawCircle(vg(listOf(Color(0xFFFFF4CC), Color(0xFFF5C451), Color(0xFFC98A12), Color(0xFF9C6A12)), my - r, my + r), r, Offset(0f, my))
                    drawCircle(vg(listOf(Color(0xFFE9AE34), Color(0xFFFFE08A)), my - r, my + r), r - 0.9f, Offset(0f, my))
                    // louros em volta
                    for (i in 0 until 10) {
                        if (i == 0 || i == 5) continue
                        val a = PIf / 2f + i * PIf / 5f; val sd = if (i < 5) 1f else -1f; val cx = cos(a) * 2.75f; val cy = my + sin(a) * 2.75f
                        rotate((a + sd * 0.9f) * DEG, pivot = Offset(cx, cy)) { oval(Color(0xBF965F0A), cx - 0.75f, cy - 0.35f, 1.5f, 0.7f) }
                    }
                    val st = star5(0f, my + 0.1f, 1.9f); drawPath(st, vg(listOf(Color(0xFFFFF6D6), Color(0xFFE9AE34)), my - 1.8f, my + 1.8f)); drawPath(st, Color(0x99965F0A), style = stroke(0.2f))
                    clipPath(Path().apply { addOval(Rect(-r, my - r, r, my + r)) }) { val k = (t * 0.35f) % 1.6f; line(Color.White.copy(alpha = 0.6f), -6f + k * 9f, my - 6f, -8f + k * 9f, my + 6f, 1.3f) }
                }
            }
        }
        "distintivo" -> {
            // distintivo da polícia preso no peito: estrela de seis pontas em ouro chanfrado, as bolinhas nas pontas,
            // o anel com pontinhos gravados e o centro azul com a estrela; um brilho passa de tempos em tempos
            val r = 6f
            fun star6(rr0: Float, dx: Float = 0f, dy: Float = 0f) = Path().apply { for (i in 0 until 12) { val a = -PIf / 2f + i * PIf / 6f; val rr = if (i % 2 == 1) rr0 * 0.56f else rr0; if (i == 0) moveTo(dx + cos(a) * rr, dy + sin(a) * rr) else lineTo(dx + cos(a) * rr, dy + sin(a) * rr) }; close() }
            translate(25f, 76.5f) {
                rotate(sin(t * 1.6f) * 0.03f * DEG, pivot = Offset.Zero) {
                    drawPath(star6(r, 0.45f, 0.6f), Color(0x59281900))
                    drawPath(star6(r), vg(listOf(Color(0xFFFFF4CC), Color(0xFFF5C451), Color(0xFFB57A0C)), -r, r))
                    for (i in 0 until 6) { val a = -PIf / 2f + i * PIf / 3f; val b = a + PIf / 6f; drawPath(Path().apply { moveTo(0f, 0f); lineTo(cos(a) * r, sin(a) * r); lineTo(cos(b) * r * 0.56f, sin(b) * r * 0.56f); close() }, Color(0x47965F0A)) }
                    drawPath(star6(r), Color(0xA66E4600), style = stroke(0.3f))
                    for (i in 0 until 6) { val a = -PIf / 2f + i * PIf / 3f; drawCircle(Color(0xFFB57A0C), 0.75f, Offset(cos(a) * r, sin(a) * r)); drawCircle(Color(0xFFFFE9A8), 0.5f, Offset(cos(a) * r - 0.12f, sin(a) * r - 0.12f)) }
                    drawCircle(Color(0xFFB57A0C), 3.3f, Offset.Zero); drawCircle(Color(0xFFF5C451), 3f, Offset.Zero)
                    for (i in 0 until 16) { val a = i * PIf / 8f; drawCircle(Color(0xBF784B05), 0.18f, Offset(cos(a) * 2.45f, sin(a) * 2.45f)) }
                    drawCircle(Color(0xFF1A2660), 1.95f, Offset.Zero); drawPath(star5(0f, 0.1f, 1.25f), vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34)), -1.2f, 1.2f))
                    clipPath(star6(r)) { val k = (t * 0.3f) % 1.8f; line(Color.White.copy(alpha = 0.7f), -8f + k * 12f, -8f, -11f + k * 12f, 8f, 1.6f) }
                }
            }
        }
        "cracha" -> {
            // crachá de professor: o cordão menta vem dos dois ombros e se encontra na presilha; o cartão pende no
            // meio do peito, com a faixa índigo, a foto do Folha, as linhas do nome e o código de barras
            val cx = 50f; val cy = 83f
            for (p in listOf(Path().apply { moveTo(14f, 66f); quadraticTo(26f, 86f, cx - 0.8f, cy) }, Path().apply { moveTo(86f, 66f); quadraticTo(74f, 86f, cx + 0.8f, cy) })) {
                drawPath(p, Color(0x330A3C28), style = stroke(if (small) 2.8f else 2.3f)); drawPath(p, Color(0xFF1E8A5A), style = stroke(if (small) 2.2f else 1.7f)); drawPath(p, Color(0xFF5FE3B5), style = stroke(if (small) 0.9f else 0.6f))
            }
            translate(cx, cy) {
                rotate(sin(t * 1.7f) * 0.05f * DEG, pivot = Offset.Zero) {
                    scale(1.25f, 1.25f, pivot = Offset.Zero) {
                        drawPath(roundRect(-1.1f, -0.8f, 2.2f, 2.8f, 0.5f), hg(listOf(Color(0xFF8E92A6), Color(0xFFE3E6F0), Color(0xFF8E92A6)), -1.1f, 1.1f))
                        drawPath(roundRect(-4.3f, 2.1f, 9.2f, 11.6f, 1.2f), Color(0x4014143C))
                        val card = roundRect(-4.6f, 1.6f, 9.2f, 11.6f, 1.2f)
                        drawPath(card, Color.White); drawPath(card, Color(0x66282850), style = stroke(0.25f))
                        clipPath(card) { drawRect(vg(listOf(Color(0xFF3A2FC4), Color(0xFF2A209E)), 1.6f, 4.6f), Offset(-5f, 1.6f), Size(10f, 3f)) }
                        line(Color.White.copy(alpha = 0.85f), -3.4f, 3.1f, 0.6f, 3.1f, 0.45f); drawCircle(Color(0xFF5FE3B5), 0.55f, Offset(3.2f, 3.1f))
                        val ph = roundRect(-3.6f, 5.4f, 3.8f, 4.4f, 0.6f); drawPath(ph, Color(0xFFFFF6E0)); drawPath(ph, Color(0x4D282850), style = stroke(0.2f))
                        drawRect(Color(0xFF2A209E), Offset(-3.4f, 5.6f), Size(3.4f, 1.1f))
                        drawCircle(Color(0xFF1D1670), 0.32f, Offset(-2.4f, 7.7f)); drawCircle(Color(0xFF1D1670), 0.32f, Offset(-0.9f, 7.7f))
                        drawPath(Path().apply { moveTo(-2.4f, 8.7f); quadraticTo(-1.65f, 9.3f, -0.9f, 8.7f) }, Color(0xFF1D1670), style = stroke(0.22f))
                        for ((y, w) in listOf(6f to 3.4f, 7.3f to 2.4f, 8.6f to 2.9f)) line(Color(0x803C3C64), 1f, y, 1f + w, y, 0.4f)
                        for (i in 0 until 12) drawRect(Color(0xFF2A209E), Offset(-3.4f + i * 0.6f, 10.6f), Size(if (i % 3 != 0) 0.25f else 0.4f, 1.6f))
                    }
                }
            }
        }
        "caneta" -> {
            // caneta-tinteiro de laca preta com clipe dourado e uma lapiseira menta, num bolsinho de couro
            val px = 26f; val py = 70f
            translate(px - 1.2f, py) {
                rotate(-0.06f * DEG, pivot = Offset.Zero) {
                    drawPath(roundRect(-1.3f, -6f, 2.6f, 14f, 1.2f), hg(listOf(Color(0xFF0E0E14), Color(0xFF3A3A48), Color(0xFF0E0E14)), -1.3f, 1.3f))
                    drawRect(hg(GOLD3, -1.35f, 1.35f), Offset(-1.35f, -0.6f), Size(2.7f, 0.9f))
                    drawPath(roundRect(0.7f, -5.6f, 0.8f, 7f, 0.4f), vg(GOLD3, -5.6f, 1.4f)); drawCircle(Color(0xFFE9AE34), 0.55f, Offset(1.1f, 1.4f))
                    drawCircle(Color(0xFFE9AE34), 0.6f, Offset(0f, -6f))
                    line(Color.White.copy(alpha = 0.4f), -0.6f, -5f, -0.6f, 7f, 0.3f)
                }
            }
            translate(px + 1.8f, py + 1f) {
                rotate(0.08f * DEG, pivot = Offset.Zero) {
                    drawPath(roundRect(-0.9f, -4.6f, 1.8f, 12f, 0.8f), hg(listOf(Color(0xFF2E9E72), Mint, Color(0xFF2E9E72)), -0.9f, 0.9f))
                    drawRect(Color(0xFFC9CCE0), Offset(0.4f, -4.2f), Size(0.5f, 5f))
                }
            }
            val pk = Path().apply { moveTo(px - 4.6f, py + 3f); lineTo(px + 4.6f, py + 3f); lineTo(px + 4.2f, py + 11f); quadraticTo(px, py + 12.6f, px - 4.2f, py + 11f); close() }
            drawPath(pk, vg(listOf(Color(0xFF5A3A26), Color(0xFF3A2418)), py + 3f, py + 12.6f)); drawPath(pk, Color(0x99140A05), style = stroke(0.3f))
            drawPath(Path().apply { moveTo(px - 3.8f, py + 4f); lineTo(px + 3.8f, py + 4f) }, Color(0x99F0C896), style = Stroke(0.22f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.6f, 0.5f))))
            line(Color.White.copy(alpha = 0.15f), px - 3.6f, py + 5f, px - 3.4f, py + 10f, 0.4f)
        }
        "medalha-militar" -> {
            // medalha de honra: barretas no alto (verde e amarelo; azul e branco), a fita verde com a faixa
            // amarela dobrada em pentágono e a estrela de cinco pontas com o centro esmaltado de azul
            val px = 25f; val py = 71.6f; val sw = sin(t * 1.8f) * 0.05f
            for ((x, c) in listOf((px - 5f) to listOf(Color(0xFF1E8A3A), Color(0xFFFFD23F)), (px + 0.2f) to listOf(Color(0xFF1F4FB0), Color.White))) {
                val w = 4.8f; val h = 2.2f
                drawRect(c[0], Offset(x, py), Size(w, h)); drawRect(c[1], Offset(x + w * 0.36f, py), Size(w * 0.28f, h))
                drawRect(Color.Black.copy(alpha = 0.15f), Offset(x, py + h * 0.6f), Size(w, h * 0.4f)); drawRect(Color(0x80141E0A), Offset(x, py), Size(w, h), style = Stroke(0.25f))
            }
            translate(px, py + 3f) {
                rotate(sw * DEG, pivot = Offset.Zero) {
                    val rib = Path().apply { moveTo(-2.8f, 0f); lineTo(2.8f, 0f); lineTo(2.8f, 3.2f); lineTo(0f, 4.6f); lineTo(-2.8f, 3.2f); close() }
                    clipPath(rib) { drawRect(Color(0xFF1E8A3A), Offset(-3f, 0f), Size(6f, 5f)); drawRect(Color(0xFFFFD23F), Offset(-0.8f, 0f), Size(1.6f, 5f)); drawRect(Color.Black.copy(alpha = 0.14f), Offset(0.8f, 0f), Size(2f, 5f)) }
                    drawPath(rib, Color(0x80141E0A), style = stroke(0.25f))
                    val sy = 8.6f; val r = 4.2f
                    drawPath(star5(0.3f, sy + 0.4f, r), Color(0x59281900))
                    val st = star5(0f, sy, r); drawPath(st, vg(listOf(Color(0xFFFFF4CC), Color(0xFFF5C451), Color(0xFFB57A0C)), sy - r, sy + r)); drawPath(st, Color(0x8C6E4600), style = stroke(0.25f))
                    for (i in 0 until 5) { val a = -PIf / 2f + i * 2f * PIf / 5f; line(Color(0x73965F0A), 0f, sy, cos(a) * r * 0.85f, sy + sin(a) * r * 0.85f, 0.2f) }
                    drawCircle(Color(0xFFB57A0C), 1.75f, Offset(0f, sy)); drawCircle(Color(0xFF1F4FB0), 1.45f, Offset(0f, sy)); drawPath(star5(0f, sy + 0.05f, 0.75f), Color.White)
                    val k = maxOf(0f, sin(t * 1.3f + 1f)).pow(8); if (k > 0.02f) star4(2.6f, sy - 3f, 2.2f * k, Color.White.copy(alpha = k))
                }
            }
        }
        "estetoscopio" -> {
            // estetoscópio pendurado nos ombros: o tubo desce em U por baixo da boca e as duas pontas caem pelos lados,
            // de um as olivas (as hastes de metal com as pontinhas pretas), do outro o diafragma prateado balançando
            val sw = sin(t * 2f) * 0.6f; val w = if (small) 2.6f else 2.1f
            fun tube(p: Path) { drawPath(p, Color(0x400A1E32), style = stroke(w + 0.9f)); drawPath(p, Color(0xFF1F4A66), style = stroke(w)); drawPath(p, Color(0xFF3B86AE), style = stroke(w * 0.42f)) }
            tube(Path().apply { moveTo(14f, 66f); cubicTo(19f, 93f, 81f, 93f, 86f, 66f) })
            tube(Path().apply { moveTo(14f, 66f); cubicTo(10.6f, 71f, 11f + sw * 0.3f, 77f, 13f + sw * 0.4f, 80.6f) })
            val ex = 13f + sw * 0.4f; val ey = 80.6f
            for (s in listOf(-1f, 1f)) {
                val arm = Path().apply { moveTo(ex, ey); quadraticTo(ex + s * 2.8f, ey + 1.6f, ex + s * 2.6f, ey + 5.4f) }
                drawPath(arm, Color(0xFF7C8598), style = stroke(1.1f)); drawPath(arm, Color(0xFFDDE3EC), style = stroke(0.45f))
                drawCircle(Color(0xFF1E1B2E), 1f, Offset(ex + s * 2.6f, ey + 5.8f)); drawCircle(Color.White.copy(alpha = 0.4f), 0.35f, Offset(ex + s * 2.6f - 0.3f, ey + 5.5f))
            }
            drawCircle(Color(0xFFC9D0DC), 1.1f, Offset(ex, ey)); drawCircle(Color.White, 0.4f, Offset(ex - 0.3f, ey - 0.3f))
            tube(Path().apply { moveTo(86f, 66f); cubicTo(89.4f, 71f, 89f + sw, 77f, 87f + sw, 80.4f) })
            val dx = 87f + sw; val dy = 84.6f; val r = 3.6f
            line(Color(0xFF9AA3B4), dx, 80.2f, dx, 81.4f, 1.3f)
            drawCircle(Color(0x4D141E32), r + 0.3f, Offset(dx + 0.4f, dy + 0.5f))
            drawCircle(vg(listOf(Color(0xFFF3F6FA), Color(0xFFAEB6C4), Color(0xFF6E7588)), dy - r, dy + r), r, Offset(dx, dy))
            drawCircle(vg(listOf(Color(0xFFC9D0DA), Color(0xFFF7F9FC)), dy - r, dy + r), r * 0.7f, Offset(dx, dy))
            drawCircle(Color(0x805A6478), r * 0.7f, Offset(dx, dy), style = Stroke(0.3f))
            drawArc(Color.White.copy(alpha = 0.95f), 198f, 81f, false, Offset(dx - r, dy - r), Size(2f * r, 2f * r), style = Stroke(0.5f))
        }
        "balanca" -> {
            // broche da balança da Justiça, em ouro: coluna, travessão que oscila devagar e os dois pratos
            val x = 24.8f; val y = 72.4f; val tilt = sin(t * 1.2f) * 0.1f
            fun gold(y0: Float, y1: Float) = vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), y0, y1)
            drawPath(Path().apply { moveTo(x - 2.6f, y + 10.2f); lineTo(x + 2.6f, y + 10.2f); lineTo(x + 1.4f, y + 8.8f); lineTo(x - 1.4f, y + 8.8f); close() }, gold(y + 8.8f, y + 10.2f))
            line(Color(0xFFC98A12), x, y + 0.6f, x, y + 9f, 0.8f); line(Color.White.copy(alpha = 0.5f), x - 0.2f, y + 1.4f, x - 0.2f, y + 8.4f, 0.2f)
            drawCircle(Color(0xFFE9AE34), 0.9f, Offset(x, y)); drawCircle(Color.White.copy(alpha = 0.7f), 0.3f, Offset(x - 0.3f, y - 0.3f))
            translate(x, y + 1.6f) {
                rotate(tilt * DEG, pivot = Offset.Zero) {
                    line(Color(0xFFC98A12), -5f, 0f, 5f, 0f, 0.7f)
                    for (sd in listOf(-1f, 1f)) translate(sd * 4.8f, 0f) {
                        rotate(-tilt * DEG, pivot = Offset.Zero) {
                            line(Color(0xCC965F0A), 0f, 0f, -1.8f, 4.2f, 0.2f); line(Color(0xCC965F0A), 0f, 0f, 1.8f, 4.2f, 0.2f)
                            drawPath(Path().apply { moveTo(-2.2f, 4.2f); lineTo(2.2f, 4.2f); quadraticTo(1.8f, 5.8f, 0f, 5.8f); quadraticTo(-1.8f, 5.8f, -2.2f, 4.2f); close() }, gold(4.2f, 5.8f))
                        }
                    }
                }
            }
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
        "cruz-saude" -> {
            // broche redondo da saúde (igual ao site): aro dourado, fundo branco com filete verde e a cruz vermelha
            // em relevo; de tempos em tempos um brilho atravessa o broche
            val x = 24.5f; val y = 77f; val r = 4.6f
            drawCircle(Color(0x4D281414), r + 0.2f, Offset(x + 0.35f, y + 0.5f))
            drawCircle(vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), y - r, y + r), r, Offset(x, y))
            drawCircle(vg(listOf(Color.White, Color(0xFFE3E9F0)), y - r, y + r), r - 0.8f, Offset(x, y))
            drawCircle(Color(0xFF2E9E5A), r - 1.35f, Offset(x, y), style = Stroke(0.45f))
            val cr = Path().apply { addPath(roundRect(x - 0.95f, y - 2.5f, 1.9f, 5f, 0.35f)); addPath(roundRect(x - 2.5f, y - 0.95f, 5f, 1.9f, 0.35f)) }
            translate(0.25f, 0.3f) { drawPath(cr, Color(0x59780A14)) }
            drawPath(cr, vg(listOf(Color(0xFFFF6B6B), Color(0xFFE02A3A), Color(0xFFA8142A)), y - 2.5f, y + 2.5f))
            line(Color.White.copy(alpha = 0.6f), x - 0.35f, y - 2f, x - 0.35f, y - 1.2f, 0.3f)
            clipPath(ellipsePath(x, y, r, r)) { val k = (t * 0.33f) % 1.7f; line(Color.White.copy(alpha = 0.55f), x - 6f + k * 9f, y - 6f, x - 8f + k * 9f, y + 6f, 1.2f) }
        }
        "divisas" -> translate(24.5f, 75.5f) { rotate(sin(t * 1.6f) * 0.03f * DEG, pivot = Offset.Zero) {
            // divisas de sargento: escudinho verde-oliva com aro dourado e três "V" dourados empilhados
            val sh = Path().apply { moveTo(-4.4f, -4.6f); lineTo(4.4f, -4.6f); lineTo(4.4f, 2f); quadraticTo(4f, 5.6f, 0f, 7.4f); quadraticTo(-4f, 5.6f, -4.4f, 2f); close() }
            translate(0.4f, 0.5f) { drawPath(sh, Color(0x59141905)) }
            drawPath(sh, vg(listOf(Color(0xFF7E8B4E), Color(0xFF5C6934), Color(0xFF3E4A20)), -4.6f, 7.4f))
            drawPath(sh, Color(0xFFC98A12), style = stroke(if (small) 1f else 0.7f)); drawPath(sh, Color(0xB3FFF1BF), style = stroke(0.25f))
            for (i in 0 until 3) {
                val yy = -2.6f + i * 2.5f; val v = Path().apply { moveTo(-3f, yy); lineTo(0f, yy + 2.2f); lineTo(3f, yy) }
                drawPath(v, Color(0x8C3C2800), style = stroke(1.5f)); drawPath(v, Color(0xFFE9AE34), style = stroke(1.05f)); drawPath(v, Color(0xCCFFF6D6), style = stroke(0.3f))
            }
            val k = maxOf(0f, sin(t * 1.4f + 2f)).pow(8); if (k > 0.02f) star4(3f, -3.4f, 2f * k, Color.White.copy(alpha = k))
        } }
        "plaqueta" -> {
            // plaqueta de identificação militar: correntinha de bolinhas vinda dos ombros e as duas plaquetas de
            // metal penduradas no peito, com furo, borda em relevo e linhas gravadas; balançam de leve
            val cx = 50f; val cy = 83.5f
            for ((x0, c1x) in listOf(14f to 26f, 86f to 74f)) {
                val ex = cx + (if (x0 < 50f) -0.6f else 0.6f)
                if (small) { val p = Path().apply { moveTo(x0, 66f); quadraticTo(c1x, 86f, ex, cy) }; drawPath(p, Color(0xFF8E92A6), style = stroke(1.3f)); drawPath(p, Color(0xFFE3E6F0), style = stroke(0.5f)) }
                else for (i in 0..26) {
                    val u = i / 26f; val a = (1 - u) * (1 - u); val b = 2 * u * (1 - u); val c = u * u; val px = a * x0 + b * c1x + c * ex; val py = a * 66f + b * 86f + c * cy
                    drawCircle(Color(0xFF7C8398), 0.62f, Offset(px, py)); drawCircle(Color(0xFFE8ECF4), 0.32f, Offset(px - 0.15f, py - 0.15f))
                }
            }
            translate(cx, cy) { rotate(sin(t * 1.7f) * 0.07f * DEG, pivot = Offset.Zero) { scale(1.2f, 1.2f, pivot = Offset.Zero) {
                fun DrawScope.tag(dx: Float, dy: Float, ang: Float, back: Boolean) = translate(dx, dy) { rotate(ang * DEG, pivot = Offset.Zero) {
                    val p = roundRect(-3.2f, 1.2f, 6.4f, 9.4f, 2.2f)
                    translate(0.35f, 0.45f) { drawPath(p, Color(0x4D141928)) }
                    drawPath(p, hg(if (back) listOf(Color(0xFF6E7588), Color(0xFFC3C9D6), Color(0xFF8E95A6)) else listOf(Color(0xFF8E95A6), Color(0xFFF3F6FA), Color(0xFFC3C9D6), Color(0xFF7C8398)), -3.2f, 3.2f))
                    drawPath(p, Color(0x99282D3C), style = stroke(0.3f))
                    drawPath(roundRect(-2.5f, 1.9f, 5f, 8f, 1.6f), Color.White.copy(alpha = 0.55f), style = stroke(0.25f))
                    drawCircle(Color(0xFF3C4054), 0.55f, Offset(0f, 2.8f)); drawCircle(Color.White.copy(alpha = 0.6f), 0.2f, Offset(-0.15f, 2.65f))
                    if (!back) for ((yy, w) in listOf(5f to 3.4f, 6.4f to 2.6f, 7.8f to 3f)) { line(Color(0x993C4054), -w / 2f, yy, w / 2f, yy, 0.4f); line(Color.White.copy(alpha = 0.6f), -w / 2f, yy + 0.35f, w / 2f, yy + 0.35f, 0.2f) }
                } }
                drawCircle(Color(0xFF9AA3B4), 0.8f, Offset(0f, 0.6f), style = Stroke(0.4f))
                tag(1.2f, 0.4f, 0.2f, true); tag(0f, 0f, 0f, false)
            } } }
        }
        "brasao" -> translate(24.5f, 76f) { rotate(sin(t * 1.5f) * 0.03f * DEG, pivot = Offset.Zero) {
            // brasão da corporação: escudo de ouro com o campo azul, a faixa dourada de cima com três pontinhos,
            // a estrela de cinco pontas no meio e um brilho que passa de tempos em tempos
            fun shield(k: Float) = Path().apply { moveTo(-4.6f * k, -4.6f * k); quadraticTo(0f, -3.4f * k, 4.6f * k, -4.6f * k); lineTo(4.6f * k, 0.6f * k); quadraticTo(4.3f * k, 4.8f * k, 0f, 6.8f * k); quadraticTo(-4.3f * k, 4.8f * k, -4.6f * k, 0.6f * k); close() }
            translate(0.4f, 0.5f) { drawPath(shield(1f), Color(0x59141432)) }
            drawPath(shield(1f), vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), -4.6f, 6.8f))
            drawPath(shield(0.8f), vg(listOf(Color(0xFF3D63C8), Color(0xFF1F3F9A), Color(0xFF132A6E)), -4f, 6f))
            clipPath(shield(0.8f)) { drawRect(vg(listOf(Color(0xFFFFE08A), Color(0xFFC98A12)), -4f, -1.8f), Offset(-5f, -5f), Size(10f, 3.2f)) }
            for (dx in listOf(-1.8f, 0f, 1.8f)) drawCircle(Color(0xFF1F3F9A), 0.35f, Offset(dx, -2.7f))
            val st = star5(0f, 1.6f, 2.4f); drawPath(st, vg(listOf(Color(0xFFFFF6D6), Color(0xFFE9AE34)), -0.8f, 4f)); drawPath(st, Color(0x996E4600), style = stroke(0.2f))
            drawPath(shield(1f), Color(0xA66E4600), style = stroke(if (small) 0.8f else 0.35f))
            clipPath(shield(1f)) { val k = (t * 0.3f) % 1.8f; line(Color.White.copy(alpha = 0.6f), -8f + k * 12f, -7f, -11f + k * 12f, 8f, 1.4f) }
        } }
        "jabo" -> translate(50f, 81.2f) { rotate(sin(t * 1.6f) * 0.04f * DEG, pivot = Offset.Zero) { scale(1.2f, 1.2f, pivot = Offset.Zero) {
            // jabô de renda: três babados brancos em cascata presos num nozinho logo abaixo da boca, com a barra
            // recortada em ondinhas, furinhos de renda e uma sombra suave; balança de leve
            for (i in 2 downTo 0) {
                val y0 = 0.6f + i * 3.3f; val w0 = 1.8f + i * 0.7f; val w = 4.4f + i * 1.3f; val h = 4.6f; val n = 5
                val p = Path().apply {
                    moveTo(-w0, y0); lineTo(w0, y0); quadraticTo(w * 0.9f, y0 + h * 0.4f, w, y0 + h)
                    for (j in n downTo 1) { val xa = -w + (j.toFloat() / n) * 2f * w; val xb = -w + ((j - 1).toFloat() / n) * 2f * w; quadraticTo((xa + xb) / 2f, y0 + h + 1.6f, xb, y0 + h) }
                    quadraticTo(-w * 0.9f, y0 + h * 0.4f, -w0, y0); close()
                }
                translate(0.3f, 0.5f) { drawPath(p, Color(0x2928285A)) }
                drawPath(p, vg(listOf(Color.White, Color(0xFFF1F2F9), Color(0xFFD9DCEA)), y0, y0 + h + 1.6f))
                drawPath(p, Color(0x735A5A8C), style = stroke(0.25f))
                if (!small) {
                    for (j in 0 until n) drawCircle(Color(0x8C8C8CBE), 0.32f, Offset(-w + (j + 0.5f) / n * 2f * w, y0 + h + 0.3f))
                    for (dx in listOf(-0.45f, 0.45f)) line(Color(0x599696C8), dx * w, y0 + 1f, dx * w * 1.6f, y0 + h - 0.3f, 0.2f)
                }
            }
            val kn = ellipsePath(0f, 0.4f, 1.9f, 1.3f); drawPath(kn, vg(listOf(Color.White, Color(0xFFD9DCEA)), -0.9f, 1.7f)); drawPath(kn, Color(0x805A5A8C), style = Stroke(0.25f))
            drawCircle(Color.White.copy(alpha = 0.9f), 0.4f, Offset(-0.6f, 0f))
        } } }
        "leao" -> {
            // broche do leão (Receita): juba de pétalas douradas em volta, carinha de ouro claro com orelhas,
            // olhos, focinho e bigodinho gravados; de vez em quando um brilho pisca na juba
            val x = 24.5f; val y = 77f
            drawCircle(Color(0x593C1E00), 5.2f, Offset(x + 0.35f, y + 0.5f))
            for (i in 0 until 14) {
                val a = i * 2f * PIf / 14f + 0.1f; val ex = x + cos(a) * 3.6f; val ey = y + sin(a) * 3.6f
                rotate(a * DEG, pivot = Offset(ex, ey)) { drawOval(vg(if (i % 2 == 1) listOf(Color(0xFFF5C451), Color(0xFFB57A0C)) else listOf(Color(0xFFE9AE34), Color(0xFF8A5A0C)), ey - 1.15f, ey + 1.15f), Offset(ex - 1.9f, ey - 1.15f), Size(3.8f, 2.3f)) }
            }
            for (s in listOf(-1f, 1f)) { drawCircle(Color(0xFFC98A12), 1.05f, Offset(x + s * 2.2f, y - 2.3f)); drawCircle(Color(0xFFFFE08A), 0.55f, Offset(x + s * 2.2f, y - 2.2f)) }
            drawCircle(vg(listOf(Color(0xFFFFF4CC), Color(0xFFF5C451), Color(0xFFD99A1B)), y - 3f, y + 3f), 3f, Offset(x, y))
            for (s in listOf(-1f, 1f)) drawCircle(Color(0xFF5A3A06), 0.42f, Offset(x + s * 1.1f, y - 0.6f))
            drawCircle(Color(0xFFFFF6D6), 0.85f, Offset(x - 0.55f, y + 1.2f)); drawCircle(Color(0xFFFFF6D6), 0.85f, Offset(x + 0.55f, y + 1.2f))
            drawPath(Path().apply { moveTo(x - 0.75f, y + 0.2f); lineTo(x + 0.75f, y + 0.2f); lineTo(x, y + 1f); close() }, Color(0xFF7A4A08))
            drawPath(Path().apply { moveTo(x - 0.9f, y + 1.9f); quadraticTo(x, y + 2.5f, x + 0.9f, y + 1.9f) }, Color(0xFF7A4A08), style = stroke(0.25f))
            if (!small) {
                for (s in listOf(-1f, 1f)) for (dy in listOf(-0.2f, 0.4f)) line(Color(0x997A4A08), x + s * 1.4f, y + 1.2f + dy, x + s * 2.6f, y + 0.9f + dy * 1.8f, 0.15f)
                val k = maxOf(0f, sin(t * 1.5f + 0.7f)).pow(6); if (k > 0.02f) star4(x + 3.2f, y - 3.6f, 2.2f * k, Color.White.copy(alpha = 0.95f * k))
            }
        }
        "selo" -> {
            // selo de conformidade: duas fitas verdes com a ponta em V, o selo serrilhado verde com anel de
            // pontinhos dourados e o "check" branco no meio; um brilho passa de vez em quando
            val x = 24.5f; val y = 75.5f; val r = 5f
            for ((s, c) in listOf(-1f to Color(0xFF1E8A4A), 1f to Color(0xFF16703B))) {
                drawPath(Path().apply { moveTo(x + s * 0.6f, y + 2f); lineTo(x + s * 3.6f, y + 10.4f); lineTo(x + s * 2.2f, y + 9.4f); lineTo(x + s * 1.2f, y + 11f); lineTo(x - s * 0.9f, y + 2.8f); close() }, c)
                line(Color.White.copy(alpha = 0.35f), x + s * 0.8f, y + 3f, x + s * 2.8f, y + 9.4f, 0.3f)
            }
            val burst = Path().apply { for (i in 0 until 40) { val a = i * PIf / 20f; val rr = if (i % 2 == 1) r * 0.88f else r; if (i == 0) moveTo(x + cos(a) * rr, y + sin(a) * rr) else lineTo(x + cos(a) * rr, y + sin(a) * rr) }; close() }
            translate(0.35f, 0.5f) { drawPath(burst, Color(0x590A2814)) }
            drawPath(burst, vg(listOf(Color(0xFF6FE0A6), Color(0xFF22A35E), Color(0xFF13703E)), y - r, y + r)); drawPath(burst, Color(0x8C0A3C1E), style = stroke(0.25f))
            drawCircle(vg(listOf(Color(0xFF2FB56C), Color(0xFF178049)), y - 3.4f, y + 3.4f), 3.4f, Offset(x, y))
            if (!small) for (i in 0 until 18) { val a = i * PIf / 9f; drawCircle(Color(0xFFF5C451), 0.2f, Offset(x + cos(a) * 2.9f, y + sin(a) * 2.9f)) }
            val ck = Path().apply { moveTo(x - 1.6f, y + 0.1f); lineTo(x - 0.4f, y + 1.3f); lineTo(x + 1.8f, y - 1.2f) }
            drawPath(ck, Color(0x990A3C1E), style = stroke(1.3f)); drawPath(ck, Color.White, style = stroke(0.85f))
            clipPath(burst) { val k = (t * 0.32f) % 1.8f; line(Color.White.copy(alpha = 0.55f), x - 7f + k * 12f, y - 7f, x - 10f + k * 12f, y + 7f, 1.3f) }
        }
        "seda" -> scale(1.3f, 1.25f, pivot = Offset(50f, 80f)) {
            // gravata de seda azul: lâmina lisa em cetim (o miolo mais claro), um reflexo que corre de cima a
            // baixo, a trama de pontinhos e o nó com a dobrinha
            val sw = sin(t * 1.9f) * 0.8f
            val blade = Path().apply { moveTo(48.2f, 83.4f); lineTo(51.8f, 83.4f); lineTo(54.6f + sw, 94f); lineTo(50f + sw, 98.6f); lineTo(45.4f + sw, 94f); close() }
            drawPath(blade, hg(listOf(Color(0xFF1A4A9E), Color(0xFF4F8FEA), Color(0xFF9CCBFF), Color(0xFF3F7FDC), Color(0xFF163E8A)), 45f, 55f))
            clipPath(blade) {
                if (!small) for (i in 0 until 6) for (j in 0 until 3) drawCircle(Color.White.copy(alpha = 0.16f), 0.28f, Offset(47.6f + j * 2.4f + (i % 2) * 1.2f + sw * (i / 6f), 85f + i * 2.2f))
                val k = (t * 0.45f) % 1.6f; val yy = 78f + k * 16f; line(Color.White.copy(alpha = 0.55f), 42f, yy + 3f, 58f, yy - 3f, 1.4f, false)
                drawRect(Color(0x33050F32), Offset(50.6f + sw * 0.5f, 82f), Size(6f, 18f))
            }
            drawPath(blade, Color(0x800A1E50), style = stroke(0.35f))
            val knot = Path().apply { moveTo(47f, 79.8f); lineTo(53f, 79.8f); lineTo(51.8f, 83.8f); lineTo(48.2f, 83.8f); close() }
            drawPath(knot, vg(listOf(Color(0xFF7FB4F7), Color(0xFF1F55B0)), 79.8f, 83.8f)); drawPath(knot, Color(0x800A1E50), style = stroke(0.35f))
            line(Color.White.copy(alpha = 0.5f), 47.8f, 80.6f, 52.2f, 80.6f, 0.5f); line(Color(0x590A1E50), 49f, 82.6f, 51f, 82.6f, 0.3f)
        }
        "parlamentar" -> {
            // broche parlamentar: oval de ouro com o esmalte branco, a faixa verde e amarela atravessando na
            // diagonal e o centro azul com a estrelinha; um brilho corre o broche
            val x = 24.5f; val y = 77f; val rx = 5f; val ry = 3.9f
            translate(0.35f, 0.5f) { drawPath(ellipsePath(x, y, rx + 0.2f, ry + 0.2f), Color(0x593C1E00)) }
            drawPath(ellipsePath(x, y, rx, ry), vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), y - ry, y + ry))
            val inner = ellipsePath(x, y, rx - 0.9f, ry - 0.9f)
            drawPath(inner, vg(listOf(Color.White, Color(0xFFECEFF5)), y - ry, y + ry))
            clipPath(inner) { rotate(-0.55f * DEG, pivot = Offset(x, y)) {
                drawRect(Color(0xFF1E9A48), Offset(x - 7f, y - 1.7f), Size(14f, 3.4f)); drawRect(Color(0xFFFFD23F), Offset(x - 7f, y - 0.6f), Size(14f, 1.2f))
                drawRect(Color.Black.copy(alpha = 0.12f), Offset(x - 7f, y + 0.9f), Size(14f, 0.8f))
            } }
            drawCircle(Color(0xFF9C6A12), 1.35f, Offset(x, y)); drawCircle(Color(0xFF1F4FB0), 1.1f, Offset(x, y)); drawPath(star5(x, y + 0.05f, 0.6f), Color.White)
            drawPath(ellipsePath(x, y, rx, ry), Color(0x996E4600), style = stroke(if (small) 0.7f else 0.3f))
            clipPath(ellipsePath(x, y, rx, ry)) { val k = (t * 0.3f) % 1.8f; line(Color.White.copy(alpha = 0.6f), x - 8f + k * 12f, y - 6f, x - 11f + k * 12f, y + 6f, 1.3f) }
        }
        "estrelinha" -> {
            // estrelinha de boa nota: adesivo de estrela dourada com a borda branca do recorte, carinha sorridente
            // e bochechas; ela balança de leve e pisca um brilho
            val x = 24.5f; val y = 77.5f; val r = 5f
            translate(x, y) { rotate(sin(t * 1.4f) * 0.08f * DEG, pivot = Offset.Zero) {
                translate(0.35f, 0.5f) { drawPath(star5(0f, 0f, r + 0.7f), Color(0x4D3C1E00)) }
                drawPath(star5(0f, 0f, r + 0.7f), Color.White); drawPath(star5(0f, 0f, r + 0.7f), Color(0x73968CB4), style = stroke(0.25f))
                drawPath(star5(0f, 0f, r), vg(listOf(Color(0xFFFFF4B8), Color(0xFFFFD23F), Color(0xFFE9A21A)), -r, r))
                drawPath(star5(-0.8f, -1f, r * 0.32f), Color.White.copy(alpha = 0.45f))
                for (s in listOf(-1f, 1f)) { drawCircle(Color(0xFF5A3A06), 0.45f, Offset(s * 1.1f, 0.1f)); drawCircle(Color(0x8CFF8296), 0.6f, Offset(s * 2f, 1.2f)) }
                drawPath(Path().apply { moveTo(-0.9f, 1.1f); quadraticTo(0f, 2.1f, 0.9f, 1.1f) }, Color(0xFF5A3A06), style = stroke(0.3f))
            } }
            val k = maxOf(0f, sin(t * 1.8f)).pow(6); if (k > 0.02f) star4(x + 4f, y - 4f, 2.2f * k, Color.White.copy(alpha = 0.95f * k))
        }
        "pendrive" -> {
            // pendrive no cordão: cordão azul vindo dos dois ombros até a argola; o pendrive de plástico índigo com
            // a faixa menta, o conector de metal com os furinhos e a luzinha que pisca
            val cx = 50f; val cy = 83f
            for (p in listOf(Path().apply { moveTo(14f, 66f); quadraticTo(26f, 86f, cx - 0.8f, cy) }, Path().apply { moveTo(86f, 66f); quadraticTo(74f, 86f, cx + 0.8f, cy) })) {
                drawPath(p, Color(0x330A143C), style = stroke(if (small) 2.8f else 2.3f)); drawPath(p, Color(0xFF1F4FB0), style = stroke(if (small) 2.2f else 1.7f)); drawPath(p, Color(0xFF6FB0FF), style = stroke(if (small) 0.9f else 0.6f))
            }
            translate(cx, cy) { rotate(sin(t * 1.8f) * 0.07f * DEG, pivot = Offset.Zero) { scale(1.25f, 1.25f, pivot = Offset.Zero) {
                drawCircle(Color(0xFFB9BFCC), 0.9f, Offset(0f, 0.4f), style = Stroke(0.45f))
                val body = roundRect(-2.3f, 1.3f, 4.6f, 8.4f, 1.2f)
                translate(0.3f, 0.4f) { drawPath(body, Color(0x4014143C)) }
                drawPath(body, hg(listOf(Color(0xFF2A209E), Color(0xFF5247E0), Color(0xFF3A2FC4), Color(0xFF1D1670)), -2.3f, 2.3f))
                clipPath(body) { drawRect(Mint, Offset(-2.3f, 5f), Size(4.6f, 1.3f)) }
                drawPath(body, Color(0x990A0828), style = stroke(0.25f))
                drawCircle(Color(0xFFD7D9E8), 0.45f, Offset(0f, 2.3f))
                line(Color.White.copy(alpha = 0.35f), -1.5f, 2.6f, -1.5f, 8.8f, 0.35f)
                drawCircle(if ((t * 1.2f) % 1f < 0.5f) Mint else Color(0xFF2E6B57), 0.4f, Offset(1.1f, 8.3f))
                drawRect(hg(listOf(Color(0xFF8E95A6), Color(0xFFF3F6FA), Color(0xFF9AA3B4)), -1.6f, 1.6f), Offset(-1.6f, 9.7f), Size(3.2f, 3.4f))
                drawRect(Color(0x8C282D3C), Offset(-1.6f, 9.7f), Size(3.2f, 3.4f), style = Stroke(0.2f))
                drawRect(Color(0xFF3C4054), Offset(-1f, 10.6f), Size(0.7f, 0.7f)); drawRect(Color(0xFF3C4054), Offset(0.3f, 10.6f), Size(0.7f, 0.7f))
            } } }
        }
        "postits" -> {
            // post-its colados no peito: amarelo, rosa e menta, cada um com a faixa de cola, os rabisquinhos e a
            // pontinha levantada; o de baixo está meio solto e balança
            fun DrawScope.note(x: Float, y: Float, ang: Float, c1: Color, c2: Color, ink: Color) = translate(x, y) { rotate(ang * DEG, pivot = Offset.Zero) {
                drawRect(Color(0x33281E3C), Offset(-2.6f, 0.5f), Size(6f, 6f))
                val p = Path().apply { moveTo(-3f, 0f); lineTo(3f, 0f); lineTo(3f, 4.6f); lineTo(1.6f, 6f); lineTo(-3f, 6f); close() }
                drawPath(p, vg(listOf(c1, c2), 0f, 6f))
                drawRect(Color.Black.copy(alpha = 0.07f), Offset(-3f, 0f), Size(6f, 1.3f))
                val curl = Path().apply { moveTo(3f, 4.6f); lineTo(1.6f, 6f); lineTo(1.9f, 4.9f); close() }
                drawPath(curl, c1); drawPath(curl, Color.Black.copy(alpha = 0.15f), style = stroke(0.15f))
                drawPath(p, Color(0x4D3C2814), style = stroke(0.2f))
                if (!small) for ((yy, w) in listOf(2.3f to 4.2f, 3.5f to 3.2f, 4.7f to 2.4f)) line(ink, -2.2f, yy, -2.2f + w, yy, 0.3f)
            } }
            note(21f, 72.5f, -0.18f, Color(0xFFFFF3A0), Color(0xFFFFD84A), Color(0x8C785A0A))
            note(27.6f, 74.6f, 0.14f, Color(0xFFFFD1E3), Color(0xFFFF8FB8), Color(0x80962850))
            note(22.6f, 79.4f, -0.06f + sin(t * 2.2f) * 0.14f, Color(0xFFD2FBE6), Mint, Color(0x8C146446))
        }
        "perolas" -> {
            // colar de pérolas: o fio desce dos ombros em U por baixo da boca, cada pérola com o reflexo rosado e o
            // pontinho de luz; a do meio é maior, presa num fecho dourado, e um brilho corre o fio devagar
            fun bz(u: Float): Offset { val a = (1 - u).pow(3); val b = 3 * u * (1 - u).pow(2); val c = 3 * u * u * (1 - u); val d = u.pow(3); return Offset(a * 16f + b * 20f + c * 80f + d * 84f, a * 66f + b * 92f + c * 92f + d * 66f) }
            val n = if (small) 13 else 21; val run = (t * 0.25f) % 1.4f; val r = if (small) 1.6f else 1.25f
            fun DrawScope.pearl(p: Offset, rr: Float, k: Float) {
                drawCircle(Color(0x38281E46), rr, p + Offset(0.3f, 0.4f))
                drawCircle(Brush.radialGradient(0f to Color.White, 0.55f to Color(0xFFF3ECF4), 1f to Color(0xFFC9BCD6), center = p + Offset(-rr * 0.35f, -rr * 0.4f), radius = rr * 1.3f), rr, p)
                drawCircle(Color.White.copy(alpha = 0.7f + 0.3f * k), rr * (0.28f + 0.15f * k), p + Offset(-rr * 0.35f, -rr * 0.4f))
            }
            for (i in 0..n) { val u = i.toFloat() / n; pearl(bz(u), r, maxOf(0f, 1f - abs(u - run) * 8f)) }
            val m = bz(0.5f)
            drawCircle(vg(GOLD3, m.y + 0.6f, m.y + 2.2f), 0.8f, Offset(m.x, m.y + 1.4f))
            pearl(Offset(m.x, m.y + 3.6f), if (small) 2.4f else 2f, maxOf(0f, 1f - abs(0.5f - run) * 8f))
        }
        "lenco" -> {
            // lenço de seda amarrado no pescoço: faixa laranja e coral com bolinhas, o nó na frente e as duas
            // pontas soltas que esvoaçam devagar
            val sw = sin(t * 1.7f) * 0.9f; val sw2 = sin(t * 1.7f + 1f) * 0.9f
            fun DrawScope.silk(p: Path, x0: Float, x1: Float) {
                drawPath(p, hg(listOf(Color(0xFFFF6F5C), Color(0xFFFFA94D), Color(0xFFFF6F5C)), x0, x1))
                clipPath(p) {
                    if (!small) for (i in 0 until 30) drawCircle(Color(0xBFFFFAEB), 0.42f, Offset(x0 + ((i * 2.7f) % (x1 - x0)), 78f + ((i * 3.1f) % 20f)))
                    drawRect(Color(0x2678140A), Offset(x0, 85.5f), Size(x1 - x0, 20f))
                }
                drawPath(p, Color(0x738C2814), style = stroke(0.3f))
            }
            silk(Path().apply { moveTo(28f, 79.6f); quadraticTo(50f, 84.2f, 72f, 79.6f); quadraticTo(73.2f, 81.6f, 72.2f, 83.4f); quadraticTo(50f, 88f, 27.8f, 83.4f); quadraticTo(26.8f, 81.6f, 28f, 79.6f); close() }, 26f, 74f)
            silk(Path().apply { moveTo(48.6f, 85.4f); quadraticTo(45f + sw, 89f, 42.4f + sw, 94.6f); quadraticTo(45.4f + sw, 93.6f, 47.4f + sw * 0.5f, 94.8f); quadraticTo(48.4f, 90f, 50.4f, 86f); close() }, 40f, 52f)
            silk(Path().apply { moveTo(51.4f, 85.4f); quadraticTo(55f + sw2, 89f, 58f + sw2, 93.8f); quadraticTo(55f + sw2, 93.2f, 53f + sw2 * 0.5f, 94.4f); quadraticTo(51.6f, 90f, 49.6f, 86f); close() }, 48f, 60f)
            silk(ellipsePath(50f, 85.2f, 2.4f, 2f), 47.6f, 52.4f)
            line(Color.White.copy(alpha = 0.45f), 48.8f, 84.4f, 50.6f, 84.2f, 0.35f)
        }
        "sininho" -> {
            // sininho dourado num lacinho vermelho: o sino balança de um lado para o outro com o badalo atrasado, e
            // quando chega na ponta aparecem um brilho e as ondinhas do "plim"
            val x = 50f; val y = 82.6f; val ph = sin(t * 3f); val ang = ph * 0.3f; val red = Color(0xFFE0475E); val dk = Color(0xFFA8233A)
            for (s in listOf(-1f, 1f)) drawPath(Path().apply { moveTo(x, y); quadraticTo(x + s * 1.8f, y - 2.1f, x + s * 3.4f, y - 1.5f); quadraticTo(x + s * 3.9f, y, x + s * 3.4f, y + 1.5f); quadraticTo(x + s * 1.8f, y + 2.1f, x, y); close() },
                hg(if (s < 0) listOf(red, dk) else listOf(dk, red), if (s < 0) x - 3.6f else x, if (s < 0) x else x + 3.6f))
            drawCircle(dk, 0.9f, Offset(x, y))
            translate(x, y + 0.8f) { rotate(ang * DEG, pivot = Offset.Zero) {
                drawCircle(Color(0xFFC98A12), 0.6f, Offset(0f, 0.6f), style = Stroke(0.35f))
                drawCircle(Color(0xFF9C6A12), 0.85f, Offset(-ang * 2f, 6.8f))
                val bell = Path().apply { moveTo(-2.9f, 5.6f); quadraticTo(-2.6f, 1.4f, 0f, 1.3f); quadraticTo(2.6f, 1.4f, 2.9f, 5.6f); lineTo(3.5f, 6.4f); lineTo(-3.5f, 6.4f); close() }
                drawPath(bell, vg(listOf(Color(0xFFFFF4CC), Color(0xFFF5C451), Color(0xFFC98A12)), 1.3f, 6.4f)); drawPath(bell, Color(0x996E4600), style = stroke(if (small) 0.6f else 0.3f))
                line(Color(0xB3965F0A), -3.1f, 5.6f, 3.1f, 5.6f, 0.3f)
                line(Color.White.copy(alpha = 0.7f), -1.4f, 2.6f, -1.9f, 4.8f, 0.45f)
            } }
            val k = minOf(1f, maxOf(0f, abs(ph) - 0.85f) / 0.15f)
            if (k > 0f && !small) {
                val s = if (ph > 0f) -1f else 1f
                for (j in 0 until 2) line(Gold.copy(alpha = k), x + s * (4.6f + j * 1.4f), y + 4f - j * 0.8f, x + s * (5.6f + j * 1.4f), y + 3f - j * 0.8f, 0.35f)
                star4(x + s * 3.4f, y + 2.4f, 1.6f * k, Color.White.copy(alpha = k))
            }
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
                    "radio" -> {
                        // rádio da viatura: corpo preto emborrachado, antena, alto-falante, visor verde com sinal e a luz
                        // vermelha de transmitindo (pisca mais rápido na festa, com as ondinhas saindo da antena)
                        rotate((-0.12f + (if (party) sin(t * 6f) * 0.08f else 0f)) * DEG, pivot = Offset.Zero) {
                            line(Color(0xFF2A2C38), 1.6f, -9f, 1.8f, -17f, 0.9f); drawCircle(Color(0xFF2A2C38), 0.75f, Offset(1.8f, -17.2f))
                            drawPath(roundRect(-2.4f, -12f, 1.8f, 2.2f, 0.5f), Color(0xFF3C4054))
                            val body = roundRect(-3.2f, -10f, 6.4f, 14f, 1.4f)
                            drawPath(body, hg(listOf(Color(0xFF1C1E28), Color(0xFF3C4054), Color(0xFF1C1E28)), -3.2f, 3.2f)); drawPath(body, Color.Black.copy(alpha = 0.6f), style = stroke(0.3f))
                            for (i in 0 until 4) line(Color(0xB38C91AA), -2f, -7.6f + i * 1.1f, 2f, -7.6f + i * 1.1f, 0.45f)
                            drawPath(roundRect(-2.2f, -2.6f, 4.4f, 2.4f, 0.4f), Mint)
                            for (i in 0 until 4) drawRect(Color(0xFF1D4A3A), Offset(-1.6f + i * 0.9f, -0.6f - (i + 1) * 0.35f), Size(0.6f, (i + 1) * 0.35f))
                            val on = (t * (if (party) 4f else 1.2f)) % 1f < 0.5f
                            if (on && !small) drawCircle(Color(0x4DFF5C4D), 1.4f, Offset(-1.8f, -9f))
                            drawCircle(if (on) Color(0xFFFF5C4D) else Color(0xFF6B1E1A), 0.6f, Offset(-1.8f, -9f))
                            line(Color.White.copy(alpha = 0.3f), -2.6f, -9.2f, -2.6f, 3f, 0.35f)
                            if (party && !small) for (i in 0 until 3) {
                                val ph = (t * 1.5f + i / 3f) % 1f; val r = 1.5f + ph * 5f
                                drawArc(Mint.copy(alpha = 0.8f * (1f - ph)), -153f, 108f, false, Offset(1.8f - r, -17.2f - r), Size(2f * r, 2f * r), style = Stroke(0.4f))
                            }
                        }
                    }
                    "maca" -> translate(0f, -5f) {
                        // maçã do professor: vermelha brilhante, cabinho e folha menta; na festa solta estrelinhas
                        rotate(sin(t * 1.3f) * 0.05f * DEG, pivot = Offset.Zero) {
                            val ap = Path().apply { moveTo(0f, -4.2f); cubicTo(2.6f, -6.4f, 6.6f, -4.6f, 6f, 0.6f); cubicTo(5.6f, 4.8f, 2.4f, 6.6f, 0f, 5.2f); cubicTo(-2.4f, 6.6f, -5.6f, 4.8f, -6f, 0.6f); cubicTo(-6.6f, -4.6f, -2.6f, -6.4f, 0f, -4.2f); close() }
                            drawPath(ap, Brush.radialGradient(0f to Color(0xFFFF7A7A), 0.55f to Color(0xFFE0303F), 1f to Color(0xFF9C1424), center = Offset(-2f, -2f), radius = 7.5f))
                            drawPath(ap, Color(0x805A0A14), style = stroke(0.3f))
                            line(Color(0xFF6B4320), 0f, -4f, 0.6f, -7.4f, 0.7f)
                            drawPath(Path().apply { moveTo(0.6f, -6.4f); quadraticTo(3.4f, -9.2f, 5.2f, -7f); quadraticTo(3f, -5.2f, 0.6f, -6.4f); close() }, vg(listOf(Mint, Color(0xFF2E9E72)), -9f, -5f))
                            rotate(-0.4f * DEG, pivot = Offset(-2.8f, -1.6f)) { oval(Color.White.copy(alpha = 0.55f), -3.9f, -3.4f, 2.2f, 3.6f) }
                            if (party) for (i in 0 until 3) { val ph = (t * 0.9f + i / 3f) % 1f; star4(-6f + i * 6f, -9f - ph * 4f, 1.4f * sin(PIf * ph), Color.White.copy(alpha = sin(PIf * ph))) }
                        }
                    }
                    "calculadora" -> rotate(-0.1f * DEG, pivot = Offset.Zero) {
                        // calculadora de mesa: visor verde com os dígitos mudando e as teclas (na festa ele aperta sem parar)
                        val body = roundRect(-4.4f, -12f, 8.8f, 13.6f, 1.4f)
                        drawPath(body, vg(listOf(Color(0xFF4A5068), Color(0xFF2C3042)), -12f, 1.6f)); drawPath(body, Color.Black.copy(alpha = 0.5f), style = stroke(0.3f))
                        drawPath(roundRect(-3.4f, -10.8f, 6.8f, 3f, 0.5f), Color(0xFFC8E6C0))
                        val n = (t * (if (party) 6f else 1.5f)).toInt()
                        for (i in 0 until 4) drawRect(if ((n + i * 3) % 5 != 0) Color(0xFF2A3A2A) else Color(0x402A3A2A), Offset(-2.6f + i * 1.5f, -10f), Size(1f, 1.5f))
                        for (r in 0 until 4) for (c in 0 until 4) {
                            val press = party && (((t * 6f).toInt() + r * 4 + c) % 7 == 0)
                            drawPath(roundRect(-3.5f + c * 1.85f, -6.6f + r * 1.85f, 1.45f, 1.35f, 0.35f), when { press -> Mint; c == 3 -> Color(0xFFF5B83D); r == 3 && c == 0 -> Color(0xFFFF8FA3); else -> Color(0xFFD7DAE6) })
                        }
                        line(Color.White.copy(alpha = 0.3f), -3.8f, -11.4f, 3.6f, -11.4f, 0.35f)
                    }
                    "bandeira" -> {
                        // bandeira do Brasil num mastro com ponta dourada: tremula para fora (longe do rosto); na festa,
                        // ele agita e o pano tremula mais rápido
                        val wav = if (party) 1f else 0.55f; val sp = if (party) 7.5f else 4.2f
                        rotate((if (party) sin(t * 5f) * 0.16f else sin(t * 1.4f) * 0.04f) * DEG, pivot = Offset.Zero) {
                            line(Color(0xFF6B4320), 1.4f, 5f, -1.2f, -21.6f, 1.1f); line(Color.White.copy(alpha = 0.35f), 1.1f, 3.6f, -1.3f, -20.4f, 0.3f)
                            drawCircle(Color(0xFFB57A0C), 1.15f, Offset(-1.3f, -22.6f)); drawCircle(Color(0xFFFFE08A), 0.7f, Offset(-1.5f, -22.9f))
                            val x0 = -1.4f; val y0 = -21.4f; val fw = 12.4f; val fh = 8.6f; val n = 14
                            fun off(u: Float) = sin(t * sp - u * 5.2f) * 0.9f * wav * u + u * 1.2f   // o pano cai um tico na ponta
                            fun pt(u: Float, v: Float) = Offset(x0 - u * fw, y0 + v * fh + off(u))
                            val flag = Path().apply {
                                pt(0f, 0f).let { moveTo(it.x, it.y) }
                                for (i in 1..n) pt(i.toFloat() / n, 0f).let { lineTo(it.x, it.y) }
                                for (i in n downTo 0) pt(i.toFloat() / n, 1f).let { lineTo(it.x, it.y) }
                                close()
                            }
                            clipPath(flag) {
                                drawRect(Color(0xFF1E9A48), Offset(x0 - fw - 2f, y0 - 3f), Size(fw + 3f, fh + 8f))
                                val rh = listOf(pt(0.08f, 0.5f), pt(0.5f, 0.1f), pt(0.92f, 0.5f), pt(0.5f, 0.9f))
                                drawPath(Path().apply { moveTo(rh[0].x, rh[0].y); for (q in rh.drop(1)) lineTo(q.x, q.y); close() }, Color(0xFFFFD23F))
                                val c = pt(0.5f, 0.5f); drawCircle(Color(0xFF1F3F9A), 2.5f, c)
                                clipPath(Path().apply { addOval(Rect(c.x - 2.5f, c.y - 2.5f, c.x + 2.5f, c.y + 2.5f)) }) {
                                    drawArc(Color.White, 212.4f, 115.2f, false, Offset(c.x - 4.4f, c.y - 0.4f), Size(10f, 10f), style = stroke(0.55f))
                                    for ((ox, oy) in listOf(-1.2f to 0.9f, 0.4f to 1.4f, 1.3f to 0.6f, -0.4f to 0.5f, 0.9f to 1.9f)) drawCircle(Color.White, 0.17f, Offset(c.x + ox, c.y + oy))
                                }
                                // dobras do pano: faixas de luz e sombra que andam com a onda
                                for (i in 0 until n) {
                                    val u = (i + 0.5f) / n; val sh = cos(t * sp - u * 5.2f) * wav; val q = pt(u, 0f)
                                    drawRect(if (sh > 0f) Color.White.copy(alpha = 0.16f * sh) else Color.Black.copy(alpha = -0.16f * sh), Offset(q.x - fw / n / 2f - 0.05f, q.y - 1f), Size(fw / n + 0.1f, fh + 2f))
                                }
                            }
                            drawPath(flag, Color(0x590A3C1E), style = stroke(0.25f))
                        }
                    }
                    "prancheta" -> {
                        // prancheta de plantão: placa de madeira, presilha de metal, a ficha com a cruz, linhas e o
                        // traçado do batimento que vai se desenhando
                        rotate(-0.18f * DEG, pivot = Offset.Zero) {
                            val board = roundRect(-7.4f, -15.6f, 10.6f, 14.2f, 1.1f)
                            drawPath(board, vg(listOf(Color(0xFFC08453), Color(0xFF965A31), Color(0xFF6D3D1F)), -15.6f, -1.4f)); drawPath(board, Color(0x803C1E0A), style = stroke(0.3f))
                            drawRect(Color(0xFFFFFDF8), Offset(-6.4f, -13.4f), Size(8.6f, 11.2f))
                            drawRect(Color(0xFFE0475E), Offset(-5.6f, -12.4f), Size(0.8f, 2.4f)); drawRect(Color(0xFFE0475E), Offset(-6.4f, -11.6f), Size(2.4f, 0.8f))
                            for (y in listOf(-7.6f, -6.2f, -4.8f, -3.4f)) line(Color(0x8C788CAA), -5.6f, y, if (y == -3.4f) -1f else 1.4f, y, 0.3f)
                            val k = if (party) (t * 0.9f) % 1f else 1f
                            val ecg = Path().apply { moveTo(-3.6f, -10.8f); for ((x, y) in listOf(-2.4f to -10.8f, -1.8f to -12.4f, -1.1f to -9.4f, -0.5f to -11f, 0.1f to -10.8f, 1.6f to -10.8f)) if ((x + 3.6f) / 5.2f <= k) lineTo(x, y) }
                            drawPath(ecg, Color(0xFFE0475E), style = stroke(0.35f))
                            drawPath(roundRect(-4.2f, -16.6f, 4.6f, 2.4f, 0.6f), vg(listOf(Color(0xFFF3F6FA), Color(0xFFAEB6C4), Color(0xFF6E7588)), -16.6f, -14.2f))
                            drawCircle(Color(0xFF6E7588), 0.45f, Offset(-1.9f, -16.1f))
                        }
                    }
                    "martelo" -> {
                        // martelo de juiz: cabo torneado e a cabeça em barril com anéis dourados; na comemoração ele bate
                        val knock = if (party) -abs(sin(t * 6f)) * 0.55f else 0f
                        rotate(knock * DEG, pivot = Offset.Zero) {
                            line(Color(0xFF5A3D20), 1.6f, 5.2f, -2.4f, -8f, 1.5f); line(Color(0xFF9C6A35), 1.4f, 4.6f, -2.2f, -7.4f, 0.7f)
                            drawCircle(Color(0xFF7A4E26), 0.95f, Offset(1.8f, 5.6f))
                            translate(-3f, -9.8f) {
                                rotate((-1.27f + PIf / 2f) * DEG, pivot = Offset.Zero) {
                                    drawPath(roundRect(-5f, -1.9f, 10f, 3.8f, 1.4f), vg(listOf(Color(0xFFC08453), Color(0xFF8A5A2B), Color(0xFF5A3D20)), -1.9f, 1.9f))
                                    for (x in listOf(-3.6f, 3.6f)) drawRect(vg(listOf(Color(0xFFFFF1BF), Color(0xFFE9AE34), Color(0xFF9C6A12)), -2f, 2f), Offset(x - 0.45f, -2f), Size(0.9f, 4f))
                                    for (x in listOf(-5f, 5f)) oval(Color(0xFF6D3D1F), x - 0.6f, -1.9f, 1.2f, 3.8f)
                                    line(Color.White.copy(alpha = 0.4f), -2.6f, -1.1f, 2.6f, -1.1f, 0.4f)
                                }
                            }
                        }
                        if (party && abs(sin(t * 6f)) > 0.96f) star4(-6f, -12f, 2.4f, Color(0xF2FFF0B4))
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
                    "termometro" -> {
                        // termômetro digital: ponta de metal, corpo branco, visor verde com a leitura e o botão azul;
                        // na festa o visor pisca e ele apita (estrelinha na ponta)
                        rotate(-0.32f * DEG, pivot = Offset.Zero) {
                            drawPath(roundRect(-0.7f, -18.4f, 1.4f, 5.2f, 0.7f), hg(listOf(Color(0xFF8E96A8), Color(0xFFF3F6FA), Color(0xFF8E96A8)), -0.7f, 0.7f))
                            val body = roundRect(-1.9f, -14.4f, 3.8f, 18f, 1.9f)
                            drawPath(body, hg(listOf(Color(0xFFD5DCE8), Color.White, Color(0xFFC2CADB)), -1.9f, 1.9f)); drawPath(body, Ink.copy(alpha = 0.45f), style = stroke(0.3f))
                            drawPath(roundRect(-1.9f, -1.2f, 3.8f, 4.8f, 1.9f), hg(listOf(Color(0xFF2E6FC0), Color(0xFF6FB0FF), Color(0xFF2E6FC0)), -1.9f, 1.9f))
                            drawCircle(Color(0xFF1F4F94), 0.75f, Offset(0f, 1f)); drawCircle(Color.White.copy(alpha = 0.5f), 0.3f, Offset(-0.25f, 0.75f))
                            val lcd = roundRect(-1.2f, -11f, 2.4f, 7f, 0.4f)
                            drawPath(lcd, Color(0xFFC8E6C0)); drawPath(lcd, Color(0xFF1E3C28).copy(alpha = 0.45f), style = stroke(0.2f))
                            if (!(party && (t * 3f) % 1f < 0.3f)) {
                                // dígitos de visor (37,5) em pé, como quadradinhos de segmento
                                for (y in listOf(-10.2f, -8.2f, -5.8f)) { drawRect(Color(0xFF2A3A2A), Offset(-0.75f, y), Size(1.5f, 1.5f)); drawRect(Color(0xFFC8E6C0), Offset(-0.35f, y + 0.35f), Size(0.7f, 0.8f)) }
                                drawCircle(Color(0xFF2A3A2A), 0.22f, Offset(0.55f, -6.4f))
                            }
                            line(Color.White.copy(alpha = 0.8f), -1.1f, -13.4f, -1.1f, -1.8f, 0.35f)
                        }
                        if (party) { val k = abs(sin(t * 5f)); star4(-6.6f, -17f, 2f * k, Color(0xFFFFF0B4).copy(alpha = k)) }
                    }
                    "cantil" -> rotate(sin(t * 1.4f) * 0.05f * DEG, pivot = Offset.Zero) {
                        // cantil militar: corpo de lona verde-oliva com costura, faixa da alça, gargalo e tampa de metal
                        // presa na correntinha; balança de leve (na festa espirra uma gotinha da tampa)
                        val strap = Path().apply { moveTo(1.4f, 2f); quadraticTo(3.4f, -8f, -0.4f, -12.4f) }
                        drawPath(strap, Color(0xFF4A5622), style = stroke(1.2f)); drawPath(strap, Color.White.copy(alpha = 0.18f), style = stroke(0.3f))
                        drawOval(vg(listOf(Color(0xFFA4B45E), Color(0xFF6E7D35), Color(0xFF4A5622)), -12.6f, -0.6f), Offset(-8.2f, -12.6f), Size(10.4f, 12f))
                        drawOval(Color(0xFF1E240C).copy(alpha = 0.6f), Offset(-8.2f, -12.6f), Size(10.4f, 12f), style = Stroke(0.35f))
                        drawOval(Color(0xFFEBF0BE).copy(alpha = 0.6f), Offset(-7.3f, -11.7f), Size(8.6f, 10.2f), style = Stroke(0.22f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.6f, 0.5f))))
                        drawPath(roundRect(-8.1f, -7.4f, 10.2f, 1.6f, 0.4f), Color(0xFF3E4818))
                        drawPath(roundRect(-3.9f, -7.8f, 1.8f, 2.4f, 0.3f), Color(0xFFC2CADB), style = stroke(0.4f))
                        rotate(-0.4f * DEG, pivot = Offset(-5.2f, -9.6f)) { oval(Color.White.copy(alpha = 0.3f), -6.3f, -11.6f, 2.2f, 4f) }
                        drawPath(roundRect(-4.4f, -14.2f, 2.8f, 2.4f, 0.4f), hg(listOf(Color(0xFF8E96A8), Color(0xFFE4E8F0), Color(0xFF8E96A8)), -4.4f, -1.6f))
                        val cap = roundRect(-4.8f, -16.2f, 3.6f, 2.4f, 0.7f)
                        drawPath(cap, vg(listOf(Color(0xFF6E7588), Color(0xFF3C4054)), -16.2f, -13.8f)); drawPath(cap, Color.Black.copy(alpha = 0.5f), style = stroke(0.25f))
                        drawPath(Path().apply { moveTo(-1.4f, -15f); quadraticTo(0f, -13.6f, -1.2f, -12f) }, Color(0xFFC2CADB), style = Stroke(0.3f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.35f, 0.35f))))
                        if (party && !small) { val ph = (t * 1.2f) % 1f; drawCircle(Color(0xFF6FB0FF).copy(alpha = 1f - ph), 0.7f * (1f - ph * 0.5f), Offset(-3f - ph * 4f, -17f - sin(PIf * ph) * 4f)) }
                    }
                    "binoculo" -> rotate((-0.15f + (if (party) sin(t * 3f) * 0.15f else 0f)) * DEG, pivot = Offset.Zero) {
                        // binóculo militar: dois canhões verde-escuros com estrias de borracha, ponte com a roda de foco,
                        // lentes azuis com o reflexo passeando; na festa ele "procura" balançando de um lado para o outro
                        for (cx in listOf(-5.6f, -0.6f)) {
                            val bar = roundRect(cx - 2.2f, -13f, 4.4f, 11f, 1.4f)
                            drawPath(bar, hg(listOf(Color(0xFF26301C), Color(0xFF5C6B42), Color(0xFF26301C)), cx - 2.2f, cx + 2.2f)); drawPath(bar, Color(0xFF0A0F05).copy(alpha = 0.6f), style = stroke(0.3f))
                            for (y in listOf(-10.4f, -9.4f, -8.4f, -7.4f)) line(Color.Black.copy(alpha = 0.35f), cx - 2f, y, cx + 2f, y, 0.25f)
                            drawPath(roundRect(cx - 1.8f, -2.8f, 3.6f, 2.8f, 0.9f), Color(0xFF15190F))
                            oval(Color(0xFF15190F), cx - 2.2f, -13.95f, 4.4f, 1.9f)
                            drawOval(vg(listOf(Color(0xFFBFE3FF), Color(0xFF3E8BE0)), -13.7f, -12.5f), Offset(cx - 1.6f, -13.72f), Size(3.2f, 1.24f))
                            drawCircle(Color.White.copy(alpha = 0.9f), 0.3f, Offset(cx - 0.6f + sin(t * 1.2f) * 0.6f, -13.25f))
                            line(Color.White.copy(alpha = 0.22f), cx - 1.5f, -11.8f, cx - 1.5f, -3.4f, 0.4f)
                        }
                        drawPath(roundRect(-3.9f, -10.4f, 1.6f, 5.2f, 0.5f), Color(0xFF3A4628))
                        drawPath(roundRect(-4f, -12.6f, 1.8f, 1.8f, 0.6f), Color(0xFF1C1E28))
                        for (x in listOf(-3.6f, -3.1f, -2.6f)) line(Color(0xFFAAAFC8).copy(alpha = 0.7f), x, -12.4f, x, -11f, 0.15f)
                    }
                    "apito" -> {
                        // apito de polícia: câmara redonda e bocal de metal cromado, furinho, argola e o cordão trançado
                        // vermelho e azul caindo da mão; na festa ele apita (ondinhas saindo do bocal)
                        val cord = Path().apply { moveTo(-7.1f, -6.2f); cubicTo(-4f, -1f, 2.4f, -2f, 1.4f, 4.6f) }
                        drawPath(cord, Color(0xFFC42F4B), style = stroke(0.9f)); drawPath(cord, Color(0xFF2E6FC0), style = Stroke(0.9f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.7f, 0.7f))))
                        translate(-5f, -9f) {
                            rotate((-0.5f + (if (party) sin(t * 14f) * 0.06f else 0f)) * DEG, pivot = Offset.Zero) {
                                val mouth = roundRect(0.6f, -1.6f, 6.4f, 2.6f, 0.6f)
                                drawPath(mouth, vg(listOf(Color(0xFFF3F6FA), Color(0xFFAEB6C4), Color(0xFF6E7588)), -1.6f, 1f)); drawPath(mouth, Ink.copy(alpha = 0.5f), style = stroke(0.25f))
                                drawCircle(Brush.radialGradient(0f to Color.White, 0.5f to Color(0xFFC2CADB), 1f to Color(0xFF6E7588), center = Offset(-1f, 0f), radius = 3.6f), 3.4f, Offset(0f, 1f))
                                drawCircle(Ink.copy(alpha = 0.5f), 3.4f, Offset(0f, 1f), style = Stroke(0.25f))
                                drawPath(roundRect(0.8f, -1.4f, 1.8f, 0.9f, 0.3f), Color(0xFF3C4054))
                                drawCircle(Color(0xFF8E96A8), 0.9f, Offset(-3.2f, 1.4f), style = Stroke(0.45f))
                                line(Color.White.copy(alpha = 0.75f), 1.6f, -1f, 6.4f, -1f, 0.3f); drawCircle(Color.White.copy(alpha = 0.7f), 0.6f, Offset(-1.2f, -0.4f))
                                if (party && !small) for (i in 0 until 3) { val ph = (t * 2f + i / 3f) % 1f; line(Mint.copy(alpha = 1f - ph), 7.6f + ph * 3f, -0.3f + (i - 1) * 1.4f * (1f + ph), 8.8f + ph * 3f, -0.3f + (i - 1) * 1.9f * (1f + ph), 0.4f) }
                            }
                        }
                    }
                    "lanterna" -> {
                        // lanterna tática: corpo preto recartilhado, botão vermelho, cabeça mais larga com a lente acesa e o
                        // facho de luz suave que varre devagar; na festa ela pisca
                        val lit = !(party && (t * 4f) % 1f < 0.25f)
                        rotate((-0.5f + sin(t * 0.9f) * 0.12f) * DEG, pivot = Offset.Zero) {
                            if (lit && !small) drawPath(Path().apply { moveTo(-2f, -13f); lineTo(-6f, -28f); quadraticTo(0f, -30f, 6f, -28f); lineTo(2f, -13f); close() }, vg(listOf(Color(0x8CFFF0AA), Color(0x00FFF0AA)), -13f, -29f))
                            drawPath(roundRect(-1.5f, -9f, 3f, 13f, 0.8f), hg(listOf(Color(0xFF14161E), Color(0xFF4A5068), Color(0xFF14161E)), -1.5f, 1.5f))
                            for (i in 0 until 5) line(Color(0xFF969BB4).copy(alpha = 0.45f), -1.3f, -3f + i * 1.1f, 1.3f, -3f + i * 1.1f, 0.25f)
                            val head = Path().apply { moveTo(-1.5f, -8.6f); lineTo(-2.4f, -11.6f); lineTo(-2.4f, -13.2f); lineTo(2.4f, -13.2f); lineTo(2.4f, -11.6f); lineTo(1.5f, -8.6f); close() }
                            drawPath(head, hg(listOf(Color(0xFF1C1E28), Color(0xFF5C6280), Color(0xFF1C1E28)), -2.4f, 2.4f)); drawPath(head, Color.Black.copy(alpha = 0.6f), style = stroke(0.25f))
                            oval(if (lit) Color(0xFFFFF3B0) else Color(0xFF8A845A), -2.2f, -13.8f, 4.4f, 1.2f)
                            drawPath(roundRect(-0.6f, -6.6f, 1.2f, 1.6f, 0.4f), Color(0xFFE0303F))
                            line(Color.White.copy(alpha = 0.3f), -1f, -8f, -1f, 3.4f, 0.3f)
                        }
                    }
                    "autos" -> rotate(-0.12f * DEG, pivot = Offset.Zero) {
                        // autos do processo: calhamaço de folhas com a capa parda, etiqueta com o número, barbante
                        // cruzado com laço; na festa ganha o carimbo verde de "ok"
                        for (i in 3 downTo 1) { val pg = roundRect(-7.6f + i * 0.35f, -14.2f + i * 0.45f, 11f, 13.6f, 0.5f); drawPath(pg, if (i % 2 == 1) Color(0xFFF4EFE4) else Color(0xFFFFFDF8)); drawPath(pg, Color(0xFF968264).copy(alpha = 0.5f), style = stroke(0.15f)) }
                        val cov = roundRect(-7.6f, -14.2f, 11f, 13.6f, 0.6f)
                        drawPath(cov, vg(listOf(Color(0xFFE8C48A), Color(0xFFD3A869), Color(0xFFB88A4C)), -14.2f, -0.6f)); drawPath(cov, Color(0xFF5A3C14).copy(alpha = 0.55f), style = stroke(0.3f))
                        val lbl = roundRect(-6.4f, -13f, 3.8f, 3.8f, 0.4f)
                        drawPath(lbl, Color.White); drawPath(lbl, Color(0xFF5A3C14).copy(alpha = 0.4f), style = stroke(0.15f))
                        val txt = Color(0xFF3C3C5A).copy(alpha = 0.6f)
                        line(Color(0xFFC42F4B), -6f, -12.2f, -3f, -12.2f, 0.35f); line(txt, -6f, -11.1f, -3.2f, -11.1f, 0.25f); line(txt, -6f, -10.1f, -3.8f, -10.1f, 0.25f)
                        // barbante cruzado e o laço
                        val tw = Color(0xFFA0743E)
                        line(tw, -2.1f, -14.2f, -2.1f, -0.6f, 0.45f); line(tw, -7.6f, -6f, 3.4f, -6f, 0.45f)
                        for (s in listOf(-1f, 1f)) rotate(s * 0.4f * DEG, pivot = Offset(-2.1f + s * 1.2f, -6.5f)) { drawOval(tw, Offset(-3.3f + s * 1.2f, -7.2f), Size(2.4f, 1.4f), style = Stroke(0.4f)) }
                        line(tw, -2.1f, -6f, -3f, -4.2f, 0.4f); line(tw, -2.1f, -6f, -1f, -4.4f, 0.4f); drawCircle(Color(0xFF8A5A2B), 0.5f, Offset(-2.1f, -6f))
                        line(Color.White.copy(alpha = 0.35f), -6.8f, -1.6f, 2.6f, -1.6f, 0.35f)
                        if (party) {
                            val k = clamp((t * 0.8f) % 1f * 3f, 0f, 1f); val gc = Color(0xFF2E9E72).copy(alpha = k)
                            drawPath(roundRect(-1f, -4.4f, 3.6f, 2.6f, 0.5f), gc, style = stroke(0.35f))
                            drawPath(Path().apply { moveTo(-0.2f, -3.1f); lineTo(0.6f, -2.4f); lineTo(1.9f, -3.9f) }, gc, style = stroke(0.4f))
                        }
                    }
                    "leitor" -> rotate(-0.2f * DEG, pivot = Offset.Zero) {
                        // leitor de código de barras (pistola): cabo emborrachado na mão, gatilho, cabeça amarela com a
                        // janela; o laser vermelho pisca e varre (na festa ele "bipa" sem parar)
                        val on = (t * (if (party) 5f else 1.4f)) % 1f < 0.6f
                        if (on && !small) {
                            drawPath(Path().apply { moveTo(-8.6f, -10.8f); lineTo(-16f, -14.6f); lineTo(-16f, -5.4f); close() }, hg(listOf(Color(0x66FF3C3C), Color(0x0FFF3C3C)), -8.6f, -16f))
                            val jy = sin(t * 9f) * 0.6f; line(Color(0xE6FF2828), -16f, -14.2f + jy, -16f, -5.8f + jy, 0.35f)
                        }
                        val hd = Path().apply { moveTo(-1.8f, -7.2f); lineTo(1.6f, -7.2f); lineTo(2.4f, 3.2f); quadraticTo(0.8f, 4.2f, -0.6f, 3.4f); close() }
                        drawPath(hd, hg(listOf(Color(0xFF1C1E28), Color(0xFF4A5068), Color(0xFF2A2C38)), -1.8f, 2.4f)); drawPath(hd, Color.Black.copy(alpha = 0.5f), style = stroke(0.25f))
                        for (y in listOf(-3f, -1.6f, -0.2f, 1.2f)) line(Color(0xFF969BB4).copy(alpha = 0.35f), -0.6f, y, 1.4f, y, 0.25f)
                        drawPath(roundRect(-3f, -7f, 1.3f, 2.8f, 0.6f), Color(0xFF2A2C38))
                        val head = roundRect(-8f, -12.6f, 10.4f, 5.6f, 1.8f)
                        drawPath(head, vg(listOf(Color(0xFFFFE45C), Color(0xFFF5B83D), Color(0xFFC98A12)), -12.6f, -7f)); drawPath(head, Color(0xFF5A3C0A).copy(alpha = 0.6f), style = stroke(0.3f))
                        drawPath(roundRect(-8.4f, -12.2f, 2f, 4.8f, 0.7f), Color(0xFF2A2C38))
                        drawPath(roundRect(-8.7f, -11.6f, 0.7f, 3.6f, 0.2f), if (on) Color(0xFFFF5C4D) else Color(0xFF6B1E1A))
                        line(Color.White.copy(alpha = 0.55f), -5.6f, -11.8f, 1f, -11.8f, 0.4f)
                        drawCircle(if (on) Mint else Color(0xFF2E5A4A), 0.4f, Offset(0.4f, -9.6f))
                    }
                    "planilha" -> rotate(-0.12f * DEG, pivot = Offset.Zero) {
                        // planilha no tablet: moldura escura, tela branca com cabeçalho verde, grade de células e um
                        // gráfico de barras que respira (na festa as barras dançam)
                        val tab = roundRect(-9f, -15.2f, 12f, 15.6f, 1.6f)
                        drawPath(tab, vg(listOf(Color(0xFF4A5068), Color(0xFF2C3042)), -15.2f, 0.4f)); drawPath(tab, Color.Black.copy(alpha = 0.5f), style = stroke(0.3f))
                        drawRect(Color.White, Offset(-8.1f, -14.2f), Size(10.2f, 13.6f))
                        drawRect(Color(0xFF2E9E72), Offset(-8.1f, -14.2f), Size(10.2f, 1.5f))
                        drawRect(Mint.copy(alpha = 0.45f), Offset(-5.5f, -11.3f), Size(2.6f, 1.3f))
                        val gl = Color(0xFF788CAA).copy(alpha = 0.6f)
                        for (x in listOf(-5.5f, -2.9f, -0.3f)) line(gl, x, -14.2f, x, -7.4f, 0.15f)
                        for (y in listOf(-12.7f, -11.3f, -10f, -8.7f, -7.4f)) line(gl, -8.1f, y, 2.1f, y, 0.15f)
                        for (x in listOf(-7.6f, -5f, -2.4f, 0.2f)) line(Color.White.copy(alpha = 0.85f), x, -13.45f, x + 1.4f, -13.45f, 0.3f)
                        val hs = listOf(2.4f, 3.6f, 2.8f, 4.6f); val cs = listOf(Color(0xFF6FB0FF), Mint, Color(0xFFF5B83D), Color(0xFFFF8FA3))
                        for (i in 0 until 4) { val hh = hs[i] * (0.85f + 0.15f * sin(t * (if (party) 6f else 1.5f) + i * 1.3f)); drawPath(roundRect(-7.2f + i * 2.3f, -1.6f - hh, 1.5f, hh, 0.3f), cs[i]) }
                        line(Color(0xFF3C3C5A).copy(alpha = 0.6f), -7.6f, -1.5f, 1.8f, -1.5f, 0.2f)
                        drawCircle(Color(0xFF6E7588), 0.25f, Offset(-3f, -14.7f))
                        drawPath(Path().apply { moveTo(-8.1f, -8f); lineTo(-3f, -14.2f); lineTo(-1.4f, -14.2f); lineTo(-8.1f, -6f); close() }, Color.White.copy(alpha = 0.18f))
                    }
                    "maquininha" -> rotate(-0.14f * DEG, pivot = Offset.Zero) {
                        // maquininha de cartão: corpo verde-água arredondado, visor com o "aprovado", teclado numérico com
                        // as teclas coloridas e o papelzinho saindo por cima (na festa sai comprovante sem parar)
                        val pl = if (party) 2f + ((t * 0.8f) % 1f) * 6f else 2.6f + sin(t * 1.2f) * 0.3f
                        val pap = roundRect(-5.2f, -15.4f - pl, 6.4f, pl + 1f, 0.2f)
                        drawPath(pap, Color.White); drawPath(pap, Color(0xFF7882A0).copy(alpha = 0.5f), style = stroke(0.15f))
                        for (i in 0 until 4) { val y = -15.4f - pl + 0.9f + i * 0.9f; if (y < -15.6f) line(Color(0xFF5A6482).copy(alpha = 0.55f), -4.4f, y, if (i % 2 == 1) -1.4f else -0.2f, y, 0.2f) }
                        val body = roundRect(-6.4f, -16f, 8.8f, 17.6f, 1.8f)
                        drawPath(body, vg(listOf(Color(0xFF5FD3C0), Color(0xFF2EA897), Color(0xFF1F7A6E)), -16f, 1.6f)); drawPath(body, Color(0xFF0A3C32).copy(alpha = 0.6f), style = stroke(0.3f))
                        line(Color(0xFF16564D), -5.4f, -15.3f, 1.4f, -15.3f, 0.6f)
                        drawPath(roundRect(-5.2f, -13.8f, 6.4f, 4f, 0.6f), Color(0xFF1C2A30))
                        drawPath(Path().apply { moveTo(-3.4f, -11.9f); lineTo(-2.6f, -11.1f); lineTo(-1.2f, -12.8f) }, Mint, style = stroke(0.4f))
                        line(Mint.copy(alpha = 0.7f), -0.6f, -12.4f, 0.6f, -12.4f, 0.25f); line(Mint.copy(alpha = 0.7f), -0.6f, -11.4f, 0.2f, -11.4f, 0.25f)
                        val last = listOf(Color(0xFFFF5C4D), Color(0xFFF5C451), Color(0xFF3FBF8F))
                        for (r in 0 until 5) for (c in 0 until 3) drawPath(roundRect(-5f + c * 2.15f, -8.6f + r * 1.75f, 1.75f, 1.3f, 0.4f), if (r == 4) last[c] else Color(0xFFEEF2F6))
                        line(Color.White.copy(alpha = 0.4f), -5.6f, -14.6f, -5.6f, -1f, 0.35f)
                    }
                    "cofrinho" -> translate(-3.4f, -7f) {
                        // cofrinho de porquinho: rosa com brilho, focinho com narinas, orelha, olhinho, bochecha,
                        // perninhas e o rabo em caracol; uma moeda dourada cai na fenda (na festa caem mais rápido)
                        rotate(sin(t * 1.6f) * 0.04f * DEG, pivot = Offset.Zero) {
                            val ph = (t / (if (party) 0.8f else 2.6f)) % 1f
                            if (ph < 0.5f) { val cy = -12f + ph / 0.5f * 7.6f; drawCircle(Color(0xFFC98A12), 1.4f, Offset(0.7f, cy)); drawCircle(Color(0xFFF5C451), 1.05f, Offset(0.7f, cy)); line(Color(0xFFC98A12), 0.7f, cy - 0.5f, 0.7f, cy + 0.5f, 0.25f) }
                            for (x in listOf(-3.2f, -1f, 1.6f, 3.6f)) drawPath(roundRect(x - 0.8f, 3f, 1.6f, 2.4f, 0.6f), Color(0xFFE0708C))
                            drawPath(Path().apply { moveTo(5.6f, 0f); cubicTo(7.8f, -1.4f, 7.6f, 1.8f, 6.4f, 1f); quadraticTo(6f, 0.2f, 7f, -0.4f) }, Color(0xFFE0708C), style = stroke(0.5f))
                            drawOval(Brush.radialGradient(0f to Color(0xFFFFD0DC), 0.55f to Color(0xFFFF9FB6), 1f to Color(0xFFE0708C), center = Offset(-2f, -2f), radius = 6.6f), Offset(-6f, -4.8f), Size(12f, 9.6f))
                            val pk = Color(0xFF8C2846).copy(alpha = 0.5f)
                            drawOval(pk, Offset(-6f, -4.8f), Size(12f, 9.6f), style = Stroke(0.3f))
                            val ear = Path().apply { moveTo(-3.8f, -3.4f); lineTo(-2.6f, -6.6f); lineTo(-1.2f, -4.2f); close() }
                            drawPath(ear, Color(0xFFFF8FA8)); drawPath(ear, pk, style = stroke(0.25f))
                            oval(Color(0xFFFF8FA8), -7.5f, -1.3f, 3f, 3.8f); drawOval(pk, Offset(-7.5f, -1.3f), Size(3f, 3.8f), style = Stroke(0.25f))
                            drawCircle(Color(0xFFB84466), 0.32f, Offset(-6.2f, 0f)); drawCircle(Color(0xFFB84466), 0.32f, Offset(-6.2f, 1.2f))
                            drawCircle(Ink, 0.55f, Offset(-3.6f, -1.2f)); drawCircle(Color.White, 0.2f, Offset(-3.75f, -1.4f))
                            drawCircle(Color(0xFFFF6E96).copy(alpha = 0.45f), 0.8f, Offset(-3.2f, 0.8f))
                            drawPath(roundRect(-0.6f, -4.9f, 2.6f, 0.7f, 0.35f), Color(0xFF8C2A48))
                            rotate(-0.3f * DEG, pivot = Offset(-1.2f, -2.6f)) { oval(Color.White.copy(alpha = 0.55f), -2.8f, -3.4f, 3.2f, 1.6f) }
                        }
                    }
                    "microfone" -> {
                        // microfone da tribuna: base redonda preta, haste "pescoço de ganso" com anéis, cápsula com grade
                        // e o anel vermelho de "no ar" (na festa as ondas de som saem da cápsula)
                        drawOval(vg(listOf(Color(0xFF4A5068), Color(0xFF1C1E28)), 1.6f, 4.4f), Offset(-3.4f, 1.6f), Size(8f, 2.8f))
                        oval(Color(0xFF5C6280), -2.6f, 1.6f, 6.4f, 1.8f)
                        drawCircle(Color(0xFF3FBF8F), 0.4f, Offset(2.6f, 2.8f))
                        val nk = Path().apply { moveTo(0.6f, 2.4f); cubicTo(1.4f, -4f, -4f, -6f, -4.6f, -12.4f) }
                        drawPath(nk, Color(0xFF2A2C38), style = stroke(1.3f)); drawPath(nk, Color(0xFF6E7588), style = Stroke(1.3f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.4f, 0.6f))))
                        translate(-4.8f, -13f) {
                            rotate(-0.35f * DEG, pivot = Offset.Zero) {
                                val live = (t * 0.7f) % 1f < 0.85f
                                drawPath(roundRect(-1.4f, 0.2f, 2.8f, 1.1f, 0.3f), if (live) Color(0xFFFF5C4D) else Color(0xFF6B1E1A))
                                if (live && !small) drawRect(Color(0x40FF5C4D), Offset(-1.9f, -0.2f), Size(3.8f, 1.9f))
                                val cap = roundRect(-1.7f, -5.4f, 3.4f, 5.8f, 1.7f)
                                drawPath(cap, hg(listOf(Color(0xFF1C1E28), Color(0xFF5C6280), Color(0xFF1C1E28)), -1.7f, 1.7f)); drawPath(cap, Color.Black.copy(alpha = 0.6f), style = stroke(0.25f))
                                for (y in listOf(-4.2f, -3.2f, -2.2f, -1.2f)) line(Color(0xFFAAAFC8).copy(alpha = 0.55f), -1.2f, y, 1.2f, y, 0.2f)
                                line(Color.White.copy(alpha = 0.35f), -1f, -4.4f, -1f, -0.6f, 0.3f)
                                if (party && !small) for (i in 0 until 3) {
                                    val ph = (t * 1.5f + i / 3f) % 1f; val r = 2.5f + ph * 5f
                                    drawArc(Mint.copy(alpha = 0.85f * (1f - ph)), -153f, 54f, false, Offset(-r, -3f - r), Size(2f * r, 2f * r), style = Stroke(0.4f))
                                }
                            }
                        }
                    }
                    "projeto" -> rotate(-0.12f * DEG, pivot = Offset.Zero) {
                        // projeto de lei: pergaminho aberto entre dois rolos de madeira, título e linhas de texto e o
                        // selo de lacre vermelho com as fitinhas balançando
                        drawRect(vg(listOf(Color(0xFFFFF4D6), Color(0xFFF2DFAE), Color(0xFFE2C786)), -15.2f, -2.8f), Offset(-7.6f, -15.2f), Size(9.6f, 12.4f))
                        line(Color(0xFF7A4E26), -6.2f, -13.4f, 0.6f, -13.4f, 0.55f)
                        val ends = listOf(0.8f, 0.2f, 0.9f, -0.6f, -2.6f)
                        for (i in 0 until 5) line(Color(0xFF785028).copy(alpha = 0.5f), -6.4f, -11.6f + i * 1.2f, ends[i], -11.6f + i * 1.2f, 0.25f)
                        for (y in listOf(-16.4f, -3.6f)) {
                            val rl = roundRect(-8.4f, y, 11.2f, 2f, 1f)
                            drawPath(rl, vg(listOf(Color(0xFFFFF4D6), Color(0xFFD9B878), Color(0xFFA07A40)), y, y + 2f)); drawPath(rl, Color(0xFF6E461E).copy(alpha = 0.5f), style = stroke(0.2f))
                            for (x in listOf(-8.9f, 3.3f)) drawCircle(Color(0xFF6D3D1F), 0.8f, Offset(x, y + 1f))
                        }
                        val sw = sin(t * 2f) * 0.5f
                        for (s in listOf(-1f, 1f)) drawPath(Path().apply { moveTo(-1.6f + s * 0.4f, -4.2f); lineTo(-1.6f + s * 1.4f + sw, -0.4f); lineTo(-1.6f + s * 0.6f + sw, -0.9f); lineTo(-1.6f + s * 0.2f + sw * 0.6f, -0.2f); close() }, Color(0xFFC42F4B))
                        drawCircle(Brush.radialGradient(listOf(Color(0xFFFF8A8A), Color(0xFFA8162E)), center = Offset(-2.1f, -5.1f), radius = 1.9f), 1.7f, Offset(-1.6f, -4.6f))
                        drawPath(star5(-1.6f, -4.6f, 0.9f), Color(0x99780A1E))
                    }
                    "regua" -> rotate((-0.42f + (if (party) sin(t * 5f) * 0.2f else sin(t * 1.2f) * 0.03f)) * DEG, pivot = Offset.Zero) {
                        // régua escolar amarela: marcações de milímetro e centímetro, os números e o furinho; balança de
                        // leve (na festa vira batuta)
                        val rl = roundRect(-1.9f, -20.4f, 3.8f, 23f, 0.6f)
                        drawPath(rl, hg(listOf(Color(0xFFFFE45C), Color(0xFFFFD23F), Color(0xFFE0B020)), -1.9f, 1.9f)); drawPath(rl, Color(0xFF8C640A).copy(alpha = 0.6f), style = stroke(0.3f))
                        val tk = Color(0xFF5A3C0A).copy(alpha = 0.8f)
                        for (i in 0..40) {
                            val y = 1.6f - i * 0.5f; val cm = i % 10 == 0; val half = i % 5 == 0
                            if (small && !half) continue
                            line(tk, -1.9f, y, -1.9f + (if (cm) 1.6f else if (half) 1.1f else 0.6f), y, if (cm) 0.2f else 0.12f, round = false)
                            if (cm) drawRect(Color(0xFF5A3C0A).copy(alpha = 0.75f), Offset(0f, y - 0.35f), Size(0.5f, 0.7f))
                        }
                        drawCircle(Color(0xFF8C640A).copy(alpha = 0.7f), 0.6f, Offset(0f, -19.4f), style = Stroke(0.25f))
                        line(Color.White.copy(alpha = 0.6f), 1.3f, -19.6f, 1.3f, 2f, 0.35f)
                    }
                    "globo" -> {
                        // globo terrestre: pé de madeira na mão, meridiano dourado em meia-lua, esfera azul com os
                        // continentes verdes girando devagar e o brilho
                        val cx = -3.6f; val cy = -11.4f; val gr = 5.4f
                        drawOval(vg(listOf(Color(0xFFC08453), Color(0xFF6D3D1F)), 2.5f, 4.7f), Offset(-3f, 2.5f), Size(6.8f, 2.2f))
                        line(Color(0xFF7A4E26), 0.4f, 3f, -1.07f, -5.4f, 0.9f)
                        clipPath(Path().apply { addOval(Rect(cx - gr, cy - gr, cx + gr, cy + gr)) }) {
                            drawRect(Brush.radialGradient(0f to Color(0xFF9ED8FF), 0.6f to Color(0xFF3E8BE0), 1f to Color(0xFF1F4F94), center = Offset(cx - 1.8f, cy - 2f), radius = gr), Offset(cx - gr, cy - gr), Size(2 * gr, 2 * gr))
                            val rot = (t * 1.1f) % 14f
                            val lands = listOf(floatArrayOf(-3f, -2.4f, 2.2f, 1.6f), floatArrayOf(1f, 0.8f, 1.6f, 2.6f), floatArrayOf(4.4f, -1.6f, 1.4f, 1.1f), floatArrayOf(-1.4f, 3.4f, 1.8f, 1f), floatArrayOf(8f, 1.6f, 2.4f, 1.8f), floatArrayOf(10.6f, -2.6f, 1.6f, 1.2f))
                            for (k in listOf(-14f, 0f)) for ((x, y, rx, ry) in lands) {
                                val px = cx + x + rot + k; val py = cy + y
                                rotate(0.3f * DEG, pivot = Offset(px, py)) { oval(Color(0xFF4CC38A), px - rx, py - ry, 2 * rx, 2 * ry) }
                            }
                            drawRect(Brush.radialGradient(0f to Color(0x000A1E50), 0.4f to Color(0x000A1E50), 1f to Color(0x660A1E50), center = Offset(cx - 1.8f, cy - 2f), radius = gr), Offset(cx - gr, cy - gr), Size(2 * gr, 2 * gr))
                        }
                        drawCircle(Color(0xFF14285A).copy(alpha = 0.55f), gr, Offset(cx, cy), style = Stroke(0.3f))
                        rotate(-0.5f * DEG, pivot = Offset(cx - 1.8f, cy - 2.4f)) { oval(Color.White.copy(alpha = 0.45f), cx - 3f, cy - 4.2f, 2.4f, 3.6f) }
                        drawArc(vg(GOLD3, cy - 6.5f, cy + 6.5f), -90f - 0.4f * DEG, 180f, false, Offset(cx - 6.5f, cy - 6.5f), Size(13f, 13f), style = stroke(0.9f))
                        drawCircle(Color(0xFFC98A12), 0.55f, Offset(cx - 6.5f * sin(0.4f), cy - 6.5f * cos(0.4f))); drawCircle(Color(0xFFC98A12), 0.55f, Offset(cx + 6.5f * sin(0.4f), cy + 6.5f * cos(0.4f)))
                    }
                    "notebook" -> rotate(-0.1f * DEG, pivot = Offset.Zero) {
                        // notebookzinho aberto: tampa prateada, tela escura com linhas de código coloridas e o cursor
                        // piscando, teclado em perspectiva com touchpad; na festa o código rola sozinho
                        val lid = roundRect(-9.6f, -13f, 12.6f, 12.2f, 1f)
                        drawPath(lid, vg(listOf(Color(0xFFD5DCE8), Color(0xFF8E96A8)), -13f, -0.8f)); drawPath(lid, Ink.copy(alpha = 0.5f), style = stroke(0.3f))
                        drawRect(Color(0xFF1E2140), Offset(-8.8f, -12.2f), Size(11f, 10.6f))
                        clipPath(Path().apply { addRect(Rect(-8.8f, -12.2f, 2.2f, -1.6f)) }) {
                            val sc = if (party) (t * 3f) % 1f else 0f
                            val code = listOf(Triple(0f, 4f, Color(0xFFFF8FA3)), Triple(1.2f, 5.4f, Mint), Triple(1.2f, 3.4f, Color(0xFFF5B83D)), Triple(2.4f, 4.6f, Color(0xFF6FB0FF)), Triple(1.2f, 4f, Mint), Triple(0f, 2.6f, Color(0xFFB995F5)), Triple(0f, 3.6f, Color(0xFFFF8FA3)))
                            code.forEachIndexed { i, (ind, len, c) -> val y = -10.9f + i * 1.4f - sc * 1.4f; line(c, -7.8f + ind, y, -7.8f + ind + len, y, 0.5f) }
                            if ((t * 2f) % 1f < 0.5f) drawRect(Color.White, Offset(-4.8f, -3.6f - sc * 1.4f), Size(0.5f, 1f))
                        }
                        drawCircle(Color(0xFF3C4054), 0.25f, Offset(-3.3f, -12.6f))
                        val base = Path().apply { moveTo(-10f, -0.9f); lineTo(3.4f, -0.9f); lineTo(4.8f, 1.6f); lineTo(-11.4f, 1.6f); close() }
                        drawPath(base, vg(listOf(Color(0xFFE4E8F0), Color(0xFFAEB6C4)), -0.9f, 1.6f)); drawPath(base, Ink.copy(alpha = 0.5f), style = stroke(0.25f))
                        for (i in 0 until 9) drawRect(Color(0xFF3C4054).copy(alpha = 0.55f), Offset(-9.4f + i * 1.4f, -0.5f), Size(1f, 0.45f))
                        drawPath(roundRect(-5f, 0.35f, 3f, 0.9f, 0.2f), Color(0xFF6E7588).copy(alpha = 0.5f))
                    }
                    "caneca" -> rotate(-0.06f * DEG, pivot = Offset.Zero) {
                        // caneca de café: cerâmica roxa com coraçãozinho, alça na mão, café escuro por cima e a fumacinha
                        // subindo em ondas (na festa sai mais fumaça)
                        drawOval(Color(0xFF3F35B5), Offset(-3f, -8.8f), Size(4.8f, 5.6f), style = Stroke(1.2f))
                        val mug = Path().apply { moveTo(-9f, -11f); lineTo(-1.2f, -11f); lineTo(-1.6f, -1.2f); quadraticTo(-1.7f, 0.2f, -3f, 0.2f); lineTo(-7.2f, 0.2f); quadraticTo(-8.5f, 0.2f, -8.6f, -1.2f); close() }
                        drawPath(mug, hg(listOf(Color(0xFF3F35B5), Color(0xFF8A80F5), Color(0xFF5247E0), Color(0xFF3F35B5)), -9f, -1.2f)); drawPath(mug, Color(0xFF1E145A).copy(alpha = 0.55f), style = stroke(0.3f))
                        oval(Color(0xFFECEAF8), -9f, -11.9f, 7.8f, 1.8f)
                        oval(Color(0xFF6B3E1F), -8.4f, -11.5f, 6.6f, 1.2f)
                        drawPath(heart(-5.1f, -5.6f, 1.6f), Color(0xFFFF8FA3))
                        line(Color.White.copy(alpha = 0.4f), -7.6f, -9.6f, -7.4f, -2f, 0.45f)
                        val n = if (party) 3 else 2
                        for (i in 0 until n) {
                            val ph = (t * 0.5f + i / n.toFloat()) % 1f; val bx = -6.6f + i * (3f / maxOf(1, n - 1)); val y0 = -12f - ph * 5f
                            val smk = Path().apply { for (j in 0..6) { val x = bx + sin(j * 0.9f + t * 3f + i) * 0.8f; val y = y0 - j * 0.9f; if (j > 0) lineTo(x, y) else moveTo(x, y) } }
                            drawPath(smk, Color(0xFFA096C8).copy(alpha = 0.7f * sin(PIf * ph)), style = stroke(0.6f))
                        }
                    }
                    "carimbo" -> {
                        // carimbo: pegador de madeira torneado (a mão segura no pescoço), base de madeira com etiqueta e
                        // a borracha vermelha de tinta; na festa ele carimba no ar
                        val pr = if (party) maxOf(0f, sin(t * 6f)) * 1.6f else 0f
                        translate(-1f, pr) {
                            drawCircle(Brush.radialGradient(0f to Color(0xFFF0A070), 0.6f to Color(0xFFB5582A), 1f to Color(0xFF7A3410), center = Offset(-0.7f, -5.4f), radius = 2.4f), 2.2f, Offset(0f, -4.6f))
                            drawPath(Path().apply { moveTo(-0.9f, -3f); lineTo(0.9f, -3f); lineTo(1.3f, 1f); lineTo(-1.3f, 1f); close() }, hg(listOf(Color(0xFF7A3410), Color(0xFFC9774A), Color(0xFF7A3410)), -1.3f, 1.3f))
                            drawPath(roundRect(-2f, 0.6f, 4f, 1f, 0.4f), vg(GOLD3, 0.6f, 1.6f))
                            val blk = roundRect(-4.6f, 1.5f, 9.2f, 3.2f, 0.7f)
                            drawPath(blk, vg(listOf(Color(0xFFC08453), Color(0xFF8A5A2B)), 1.5f, 4.7f)); drawPath(blk, Color(0xFF3C1E0A).copy(alpha = 0.55f), style = stroke(0.3f))
                            drawPath(roundRect(-2.4f, 2.2f, 4.8f, 1.6f, 0.3f), Color(0xFFFFFDF8)); line(Color(0xFFC42F4B), -1.8f, 3f, 1.8f, 3f, 0.3f)
                            drawPath(roundRect(-4.2f, 4.6f, 8.4f, 1.1f, 0.3f), Color(0xFFE0303F))
                            drawCircle(Color.White.copy(alpha = 0.5f), 0.6f, Offset(-0.8f, -5.4f))
                        }
                        if (party && pr > 1.4f) drawPath(roundRect(-5f, 7.4f, 8f, 1.4f, 0.4f), Color(0xFFE0303F).copy(alpha = 0.7f), style = stroke(0.35f))
                    }
                    "grampeador" -> {
                        // grampeador: base preta comprida, braço vermelho com a ponta cromada e a dobradiça atrás; na festa
                        // ele grampeia sem parar (o braço abre e fecha)
                        val open = if (party) abs(sin(t * 5f)) * 0.28f else 0.04f + sin(t * 1.3f) * 0.02f
                        translate(2.4f, -3f) {
                            rotate(-0.25f * DEG, pivot = Offset.Zero) {
                                drawPath(roundRect(-13f, 0.4f, 14.4f, 2.2f, 1f), vg(listOf(Color(0xFF3C4054), Color(0xFF1C1E28)), 0.4f, 2.6f))
                                drawPath(roundRect(-12.6f, 0.1f, 2.6f, 0.6f, 0.2f), Color(0xFFC2CADB))
                                translate(0.4f, 0.2f) {
                                    rotate(open * DEG, pivot = Offset.Zero) {
                                        val arm = roundRect(-13.6f, -3.4f, 14.4f, 3.4f, 1.4f)
                                        drawPath(arm, vg(listOf(Color(0xFFFF7A7A), Color(0xFFE0303F), Color(0xFF9C1424)), -3.4f, 0f)); drawPath(arm, Color(0xFF5A0A14).copy(alpha = 0.55f), style = stroke(0.3f))
                                        val nose = roundRect(-13.8f, -3.6f, 3.6f, 3.8f, 1.3f)
                                        drawPath(nose, hg(listOf(Color(0xFF8E96A8), Color(0xFFF3F6FA), Color(0xFFAEB6C4)), -13.8f, -10.2f)); drawPath(nose, Ink.copy(alpha = 0.4f), style = stroke(0.2f))
                                        line(Color.White.copy(alpha = 0.55f), -9.4f, -2.8f, -1f, -2.8f, 0.4f)
                                    }
                                }
                                drawCircle(Color(0xFF2A2C38), 1.3f, Offset(0.4f, 0.2f)); drawCircle(Color(0xFF8E96A8), 0.45f, Offset(0.4f, 0.2f))
                                if (party && open < 0.03f) star4(-13f, 0.4f, 1.8f, Color(0xF2FFF0B4))
                            }
                        }
                    }
                    "guarda-chuva" -> rotate((-0.22f + (if (party) sin(t * 4f) * 0.25f else sin(t * 1.3f) * 0.06f)) * DEG, pivot = Offset.Zero) {
                        // guarda-chuva colorido aberto: gomos de arco-íris com a borda ondulada, pontinha em cima, haste e
                        // o cabo em gancho na mão; balança de leve (na festa gira de um lado para o outro)
                        drawPath(Path().apply { moveTo(0f, -4f); lineTo(0f, 2f); cubicTo(0f, 4.4f, 2.8f, 4.4f, 2.8f, 2.2f) }, Color(0xFF6B4320), style = stroke(1.2f))
                        line(Color(0xFF4A5068), 0f, -4f, 0f, -24f, 0.6f)
                        val ay = -24.5f; val cols = listOf(Color(0xFFFF6B6B), Color(0xFFFFD84A), Mint, Color(0xFF6FB0FF), Color(0xFFB995F5), Color(0xFFFF8FA3))
                        for (i in 0 until 6) {
                            val x0 = -9f + i * 3f; val x1 = x0 + 3f
                            val gp = Path().apply { moveTo(0f, ay); quadraticTo(x0, ay + 0.5f, x0, -17.5f); quadraticTo((x0 + x1) / 2f, -19.3f, x1, -17.5f); quadraticTo(x1, ay + 0.5f, 0f, ay); close() }
                            drawPath(gp, cols[i]); drawPath(gp, Ink.copy(alpha = 0.4f), style = stroke(0.25f))
                        }
                        rotate(-0.4f * DEG, pivot = Offset(-4f, -21.6f)) { oval(Color.White.copy(alpha = 0.3f), -6.6f, -22.6f, 5.2f, 2f) }
                        for (i in 0..6) drawCircle(Color(0xFF4A5068), 0.3f, Offset(-9f + i * 3f, -17.5f))
                        line(Color(0xFF4A5068), 0f, -24.5f, 0f, -26.2f, 0.6f); drawCircle(Color(0xFF4A5068), 0.45f, Offset(0f, -26.3f))
                    }
                    "sorvete" -> rotate((-0.12f + sin(t * 1.4f) * 0.04f) * DEG, pivot = Offset.Zero) {
                        // sorvete de casquinha: casquinha de biscoito quadriculada, bola de morango embaixo e de menta em
                        // cima, granulado colorido e uma gotinha escorrendo
                        val cone = Path().apply { moveTo(-4.2f, -7.4f); lineTo(4.2f, -7.4f); lineTo(0.3f, 3.4f); lineTo(-0.3f, 3.4f); close() }
                        drawPath(cone, vg(listOf(Color(0xFFF2C27A), Color(0xFFD9984A), Color(0xFFB5752E)), -7.4f, 3.4f))
                        clipPath(cone) { val wc = Color(0xFF965A1E).copy(alpha = 0.55f); for (i in -6..6) { line(wc, i * 1.6f - 6f, -8f, i * 1.6f + 6f, 4f, 0.25f); line(wc, i * 1.6f + 6f, -8f, i * 1.6f - 6f, 4f, 0.25f) } }
                        drawPath(cone, Color(0xFF784614).copy(alpha = 0.6f), style = stroke(0.3f))
                        val dy = ((t * 0.4f) % 1f) * 2.2f
                        drawPath(Path().apply { moveTo(1.8f, -7f); lineTo(2.8f, -7f); lineTo(2.6f, -5.6f + dy); quadraticTo(2.2f, -4.6f + dy, 1.9f, -5.6f + dy); close() }, Color(0xFFFF8FA8))
                        val r1 = Brush.radialGradient(0f to Color(0xFFFFD6E0), 0.6f to Color(0xFFFF8FA8), 1f to Color(0xFFE0607E), center = Offset(-1.6f, -10f), radius = 5f)
                        drawOval(r1, Offset(-4.6f, -12f), Size(9.2f, 6.4f))
                        for (i in 0 until 5) drawCircle(r1, 0.9f, Offset(-3.6f + i * 1.8f, -7f))
                        drawOval(Brush.radialGradient(0f to Color(0xFFE0FFF4), 0.6f to Mint, 1f to Color(0xFF3FBF8F), center = Offset(-1.8f, -14f), radius = 4.2f), Offset(-4.2f, -16.1f), Size(7.6f, 6.6f))
                        val spr = listOf(floatArrayOf(-2f, -13.6f, 0.6f), floatArrayOf(0.8f, -14.6f, -0.4f), floatArrayOf(1.8f, -12.4f, 1.2f), floatArrayOf(-1.2f, -11.6f, -0.9f), floatArrayOf(-2.6f, -9.2f, 0.3f), floatArrayOf(1.6f, -9.6f, -0.6f), floatArrayOf(-0.4f, -15.4f, 1f))
                        val sprC = listOf(Color(0xFFFF5C8A), Color(0xFFFFD84A), Color(0xFF6FB0FF), Color(0xFFB995F5), Color(0xFFFFD84A), Color(0xFF6FB0FF), Color(0xFFFF5C8A))
                        spr.forEachIndexed { i, (x, y, a) -> line(sprC[i], x - cos(a) * 0.45f, y - sin(a) * 0.45f, x + cos(a) * 0.45f, y + sin(a) * 0.45f, 0.35f) }
                        rotate(-0.5f * DEG, pivot = Offset(-1.8f, -14.2f)) { oval(Color.White.copy(alpha = 0.6f), -2.8f, -14.8f, 2f, 1.2f) }
                    }
                    "pipoca" -> rotate(-0.08f * DEG, pivot = Offset.Zero) {
                        // pipoca de cinema: balde listrado vermelho e branco, pipocas fofas transbordando e uma pipoca
                        // pulando de vez em quando (na festa pulam várias)
                        fun kernel(x: Float, y: Float, r: Float) {
                            for ((dx, dy) in listOf(-0.55f to 0.2f, 0.55f to 0.2f, 0f to -0.45f)) { val o = Offset(x + dx * r, y + dy * r); drawCircle(Color(0xFFFFF6D8), r * 0.72f, o); drawCircle(Color(0xFFC89646).copy(alpha = 0.6f), r * 0.72f, o, style = Stroke(0.18f)) }
                            drawCircle(Color(0xFFF5C451), r * 0.22f, Offset(x + r * 0.3f, y + r * 0.2f))
                        }
                        for ((x, y) in listOf(-7f to -9.8f, -4.8f to -10.6f, -2.4f to -10.2f, 0f to -10.8f, 2.2f to -9.8f, -6f to -11.8f, -3.4f to -12.4f, -0.8f to -12.2f, 1.4f to -11.8f, -2f to -13.8f)) kernel(x, y, 1.3f)
                        val bk = Path().apply { moveTo(-8f, -9f); lineTo(3f, -9f); lineTo(1.6f, 1.6f); lineTo(-6.6f, 1.6f); close() }
                        clipPath(bk) {
                            drawRect(Color.White, Offset(-9f, -10f), Size(13f, 12f))
                            for (i in 0 until 7 step 2) drawPath(Path().apply { moveTo(-8f + i * 11f / 7f, -9f); lineTo(-8f + (i + 1) * 11f / 7f, -9f); lineTo(-6.6f + (i + 1) * 8.2f / 7f, 1.6f); lineTo(-6.6f + i * 8.2f / 7f, 1.6f); close() }, Color(0xFFE0303F))
                            drawRect(hg(listOf(Color(0x2E000000), Color.Transparent, Color.Transparent, Color(0x33000000)), -8f, 3f), Offset(-9f, -10f), Size(13f, 12f))
                        }
                        drawPath(bk, Color(0xFF6E141E).copy(alpha = 0.55f), style = stroke(0.3f))
                        val rim = roundRect(-8.4f, -9.6f, 11.8f, 1.3f, 0.6f)
                        drawPath(rim, Color.White); drawPath(rim, Color(0xFF6E141E).copy(alpha = 0.5f), style = stroke(0.25f))
                        val n = if (party) 4 else 1
                        for (i in 0 until n) { val ph = (t * (if (party) 1.1f else 0.45f) + i / n.toFloat()) % 1f; if (ph < 0.7f) { val u = ph / 0.7f; kernel(-3f + (i - 1.5f) * 2.4f + u * (if (i % 2 == 1) 2f else -2f), -12f - sin(PIf * u) * 8f, 1.1f) } }
                    }
                    "violao" -> {
                        // violãozinho: corpo de madeira em oito com boca escura e roseta, cavalete, braço com trastes,
                        // cabeça com tarraxas e as cordas (na festa ele toca: as cordas vibram e saem notinhas)
                        translate(-2f, -2f) {
                            rotate(-0.5f * DEG, pivot = Offset.Zero) {
                                drawPath(Path().apply { moveTo(-1.2f, -23.6f); lineTo(1.2f, -23.6f); lineTo(0.9f, -19.8f); lineTo(-0.9f, -19.8f); close() }, Color(0xFF3A2410))
                                for ((x, y) in listOf(-1.5f to -22.8f, 1.5f to -22.8f, -1.4f to -21f, 1.4f to -21f)) drawCircle(Gold, 0.45f, Offset(x, y))
                                val ol = Color(0xFF5A280A).copy(alpha = 0.7f)
                                drawOval(ol, Offset(-4.6f, -6.8f), Size(9.2f, 8.4f), style = Stroke(0.6f)); drawOval(ol, Offset(-3.5f, -11.7f), Size(7f, 6.6f), style = Stroke(0.6f))
                                val wood = Brush.radialGradient(0f to Color(0xFFF7C984), 0.6f to Color(0xFFD98A3A), 1f to Color(0xFFA85A1E), center = Offset(-1.6f, -7f), radius = 8f)
                                drawOval(wood, Offset(-4.6f, -6.8f), Size(9.2f, 8.4f)); drawOval(wood, Offset(-3.5f, -11.7f), Size(7f, 6.6f))
                                drawCircle(Gold, 1.9f, Offset(0f, -6.2f), style = Stroke(0.3f))
                                drawCircle(Color(0xFF2A1608), 1.5f, Offset(0f, -6.2f))
                                drawPath(roundRect(-1.8f, -2.2f, 3.6f, 0.9f, 0.3f), Color(0xFF3A2410))
                                drawPath(roundRect(-0.8f, -20f, 1.6f, 12.1f, 0.3f), vg(listOf(Color(0xFF6D3D1F), Color(0xFF4E2F15)), -20f, -7.9f))
                                var fy = -18.4f
                                while (fy < -8.5f) { line(Color(0xFFD7DAE6), -0.8f, fy, 0.8f, fy, 0.2f, round = false); fy += 1.6f }
                                val vib = if (party) sin(t * 40f) * 0.15f else 0f
                                for (i in 0 until 4) { val x = -0.45f + i * 0.3f; line(Color.White.copy(alpha = 0.85f), x, -19.8f, x + vib * (if (i % 2 == 1) 1f else -1f), -1.8f, 0.12f) }
                                rotate(0.3f * DEG, pivot = Offset(-2.4f, -3.6f)) { oval(Color.White.copy(alpha = 0.3f), -3.3f, -5.4f, 1.8f, 3.6f) }
                            }
                        }
                        if (party && !small) for (i in 0 until 2) {
                            val ph = (t * 0.8f + i / 2f) % 1f; val x = -9f - i * 3f - ph * 2f; val y = -8f - ph * 9f; val nc = Color(0xFF5247E0).copy(alpha = sin(PIf * ph))
                            drawCircle(nc, 0.8f, Offset(x, y)); line(nc, x + 0.7f, y, x + 0.7f, y - 2.6f, 0.3f)
                        }
                    }
                    "pincel" -> {
                        // pincel de pintor: cabo de madeira vermelho laqueado, virola prateada, cerdas e a ponta molhada de
                        // tinta (a cor muda devagar) com a gota brilhando; na festa ele pinta no ar e respinga
                        val paint = listOf(Color(0xFF5247E0), Color(0xFFFF5C8A), Color(0xFF2E9E72), Color(0xFFF5B83D))[(t * 0.25f).toInt().mod(4)]
                        rotate((-0.4f + (if (party) sin(t * 5f) * 0.25f else 0f)) * DEG, pivot = Offset.Zero) {
                            val hd = Path().apply { moveTo(-0.9f, -9f); lineTo(0.9f, -9f); lineTo(0.6f, 5f); quadraticTo(0f, 6.2f, -0.6f, 5f); close() }
                            drawPath(hd, hg(listOf(Color(0xFF9C1424), Color(0xFFF05A5A), Color(0xFF9C1424)), -0.9f, 0.9f)); drawPath(hd, Color(0xFF5A0A14).copy(alpha = 0.5f), style = stroke(0.25f))
                            drawPath(roundRect(-1.3f, -12.6f, 2.6f, 3.8f, 0.4f), hg(listOf(Color(0xFF8E96A8), Color(0xFFF3F6FA), Color(0xFF8E96A8)), -1.3f, 1.3f))
                            for (y in listOf(-11.2f, -10.2f)) line(Color(0xFF50556E).copy(alpha = 0.6f), -1.3f, y, 1.3f, y, 0.2f)
                            val br = Path().apply { moveTo(-1.3f, -12.4f); cubicTo(-1.8f, -15f, -0.8f, -17.6f, 0f, -18.6f); cubicTo(0.8f, -17.6f, 1.8f, -15f, 1.3f, -12.4f); close() }
                            drawPath(br, vg(listOf(Color(0xFFE8C48A), Color(0xFFC9A060)), -18.6f, -12.4f))
                            clipPath(br) { drawRect(paint, Offset(-2f, -19f), Size(4f, 3.8f)) }
                            drawPath(br, Color(0xFF5A3C14).copy(alpha = 0.45f), style = stroke(0.2f))
                            drawCircle(paint, 0.6f + 0.15f * sin(t * 3f), Offset(0f, -18.4f)); drawCircle(Color.White.copy(alpha = 0.6f), 0.2f, Offset(-0.15f, -18.6f))
                            line(Color.White.copy(alpha = 0.45f), -0.35f, -8f, -0.25f, 3.6f, 0.3f)
                            if (party && !small) for (i in 0 until 3) { val ph = (t * 1.3f + i / 3f) % 1f; drawCircle(paint, maxOf(0.01f, 0.5f * (1f - ph)), Offset(-2f - ph * 5f + i, -18f - ph * 3f + i * 1.5f)) }
                        }
                    }
                }
            }
        }
    }
}

// ---- peças das auras novas: folha de bordo (outono), cifrão, moeda de ouro que gira e letras a traço
private val AURA_MAPLE = floatArrayOf(0f, -3f, 0.7f, -1.6f, 1.9f, -2.2f, 1.5f, -0.6f, 2.8f, -0.2f, 1.6f, 0.6f, 1.8f, 1.6f, 0.4f, 1.2f, 0f, 1.6f, -0.4f, 1.2f, -1.8f, 1.6f, -1.6f, 0.6f, -2.8f, -0.2f, -1.5f, -0.6f, -1.9f, -2.2f, -0.7f, -1.6f)
private fun auraMaple() = Path().apply { var i = 0; while (i < AURA_MAPLE.size) { if (i == 0) moveTo(AURA_MAPLE[i], AURA_MAPLE[i + 1]) else lineTo(AURA_MAPLE[i], AURA_MAPLE[i + 1]); i += 2 }; close() }
private fun DrawScope.drawAuraLeaf(x: Float, y: Float, s: Float, rot: Float, flip: Float, c0: Color, c1: Color, al: Float) {
    translate(x, y) { rotate(rot * DEG, pivot = Offset.Zero) { scale(s * maxOf(0.2f, abs(flip)), s, pivot = Offset.Zero) {
        val p = auraMaple(); drawPath(p, vg(listOf(c0, c1), -3f, 1.6f), alpha = al); drawPath(p, Color(0xFF6E2D0F).copy(alpha = 0.5f * al), style = stroke(0.25f))
        for ((x1, y1) in listOf(0f to -2.4f, 1.9f to -0.5f, -1.9f to -0.5f)) line(Color(0xFF6E2D0F).copy(alpha = 0.45f * al), 0f, 1.2f, x1, y1, 0.2f)
        line(c1.copy(alpha = al), 0f, 1.2f, 0f, 3f, 0.35f)
    } } }
}
private fun auraDollar() = Path().apply {
    moveTo(0.95f, -1.05f); quadraticTo(0.65f, -1.6f, 0f, -1.6f); quadraticTo(-0.9f, -1.6f, -0.9f, -0.8f); quadraticTo(-0.9f, -0.05f, 0f, 0f)
    quadraticTo(0.95f, 0.05f, 0.95f, 0.8f); quadraticTo(0.95f, 1.6f, 0f, 1.6f); quadraticTo(-0.7f, 1.6f, -1f, 1.05f); moveTo(0f, -2.2f); lineTo(0f, 2.2f)
}
// moeda de ouro girando: achata (cos do giro) e mostra a borda grossa do lado de trás
private fun DrawScope.drawAuraCoin(x: Float, y: Float, r: Float, spin: Float, al: Float) {
    if (al <= 0.01f) return
    val sq = cos(spin); val w = maxOf(0.12f, abs(sq)); val th = r * 0.22f * (1f - w) * (if (sq > 0f) 1f else -1f)
    drawOval(Color(0xFF9A6508).copy(alpha = al), Offset(x + th - r * w, y - r), Size(2f * r * w, 2f * r))
    drawOval(Brush.linearGradient(listOf(Color(0xFFFFF1BF), Color(0xFFFFD24A), Color(0xFFC98A12)), start = Offset(x - r, y - r), end = Offset(x + r, y + r)), Offset(x - r * w, y - r), Size(2f * r * w, 2f * r), alpha = al)
    drawOval(Color(0xFFA86A08).copy(alpha = al), Offset(x - r * w, y - r), Size(2f * r * w, 2f * r), style = stroke(maxOf(0.2f, r * 0.12f)))
    if (w > 0.4f) {
        drawOval(Color(0xFFA86A08).copy(alpha = 0.7f * al), Offset(x - r * 0.7f * w, y - r * 0.7f), Size(1.4f * r * w, 1.4f * r), style = stroke(r * 0.08f))
        translate(x, y) { scale(w * r / 3.6f, r / 3.6f, pivot = Offset.Zero) { drawPath(auraDollar(), Color(0xFF965A05).copy(alpha = 0.85f * al), style = stroke(0.6f)) } }
    }
    rotate(-0.6f * DEG, pivot = Offset(x - r * 0.35f * w, y - r * 0.4f)) { oval(Color.White.copy(alpha = 0.7f * al), x - r * 0.65f * w, y - r * 0.56f, r * 0.6f * w, r * 0.32f) }
}
// letras a traço (cada letra: linhas quebradas numa caixa de -1..1 por -1.4..1.4); o "A" já vem com o til do NÃO
private val AURA_GLYPH = mapOf(
    'S' to listOf(floatArrayOf(0.9f, -1.4f, -0.9f, -1.4f, -0.9f, 0f, 0.9f, 0f, 0.9f, 1.4f, -0.9f, 1.4f)), 'I' to listOf(floatArrayOf(0f, -1.4f, 0f, 1.4f)),
    'M' to listOf(floatArrayOf(-1f, 1.4f, -1f, -1.4f, 0f, 0.2f, 1f, -1.4f, 1f, 1.4f)), 'N' to listOf(floatArrayOf(-1f, 1.4f, -1f, -1.4f, 1f, 1.4f, 1f, -1.4f)),
    'A' to listOf(floatArrayOf(-1f, 1.4f, 0f, -1.4f, 1f, 1.4f), floatArrayOf(-0.5f, 0.4f, 0.5f, 0.4f), floatArrayOf(-0.8f, -2.1f, -0.3f, -2.5f, 0.3f, -2.1f, 0.8f, -2.5f)),
    'O' to listOf(floatArrayOf(-1f, -1.4f, 1f, -1.4f, 1f, 1.4f, -1f, 1.4f, -1f, -1.4f)),
)
private fun DrawScope.auraWord(word: String, cx: Float, cy: Float, s: Float, color: Color, w: Float) {
    word.forEachIndexed { j, ch ->
        val ox = cx + (j - (word.length - 1) / 2f) * 2.9f * s
        for (pl in AURA_GLYPH[ch] ?: emptyList()) { val p = Path(); var i = 0; while (i < pl.size) { val px = ox + pl[i] * s; val py = cy + pl[i + 1] * s; if (i == 0) p.moveTo(px, py) else p.lineTo(px, py); i += 2 }; drawPath(p, color, style = stroke(w)) }
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
        "avioes" -> {
            // aviõezinhos de papel (Essencial, iguais ao site): três aviões dobrados de folha pautada voando em
            // volta do Folha. Asa de cima com pauta, margem e um adesivo de estrela; quilha e asa de baixo na
            // sombra; o avião inclina nas curvas (achata, como se girasse), deixa uma fita que afina com
            // brilhinhos e projeta a sombra no chão. Passam por trás (menores) e pela frente.
            val blue = Color(0xFF2F5BD3); val navy = Color(0xFF26215C)
            fun pos(a: Float) = Offset(50f + cos(a) * 50f, 54f + sin(a) * 20f + sin(2f * a) * 6f)
            for (i in 0 until 3) {
                val a = t * 1.25f + i * 2f * PIf / 3f; val front = sin(a) > 0f; if (front == behind) continue
                val p = pos(a); val ahead = pos(a + 0.04f)
                val heading = atan2(ahead.y - p.y, ahead.x - p.x) * DEG
                val s = (if (front) 9f else 6.4f) * boost; val al = if (front) 1f else 0.72f
                val roll = 0.55f + 0.45f * abs(sin(a))
                if (!small) {
                    // a fita do rastro, afinando e sumindo, com dois brilhinhos
                    var prev = p
                    for (k in 1..14) {
                        val q = pos(a - k * 0.05f); val f = 1f - k / 15f
                        line(blue.copy(alpha = 0.42f * al * f), prev.x, prev.y, q.x, q.y, 1.7f * f + 0.15f)
                        prev = q
                    }
                    for (k in listOf(5, 10)) { val q = pos(a - k * 0.05f); star4(q.x, q.y, (0.9f + 0.5f * sin(t * 7f + i + k)) * al, Color.White.copy(alpha = 0.9f * al)) }
                    if (front) drawOval(navy.copy(alpha = 0.12f * (p.y / 95f)), Offset(p.x - s * 0.7f, 93.6f), Size(s * 1.4f, s * 0.28f))
                }
                rotate(heading, pivot = p) { scale(1f, roll, pivot = p) {
                    fun P(vararg v: Float) = Path().apply { moveTo(p.x + v[0] * s, p.y + v[1] * s); var j = 2; while (j < v.size) { lineTo(p.x + v[j] * s, p.y + v[j + 1] * s); j += 2 }; close() }
                    val bottom = P(1f, 0f, -0.55f, 0.12f, -0.9f, 0.5f)
                    val keel = P(1f, 0f, -0.45f, -0.04f, -0.72f, 0.24f)
                    val top = P(1f, 0f, -0.82f, -0.64f, -0.45f, -0.04f)
                    val w = if (small) 0.8f else 0.42f
                    drawPath(bottom, vg(listOf(Color(0xFFC9D7FF), Color(0xFFA9BEF5)), p.y, p.y + s * 0.5f), alpha = al); drawPath(bottom, navy.copy(alpha = al), style = stroke(w))
                    drawPath(keel, vg(listOf(Color(0xFF8FA8EE), Color(0xFF6F8FE0)), p.y - s * 0.05f, p.y + s * 0.25f), alpha = al); drawPath(keel, navy.copy(alpha = al), style = stroke(w))
                    drawPath(top, vg(listOf(Color.White, Color(0xFFF4F7FF)), p.y - s * 0.64f, p.y), alpha = al)
                    if (!small) clipPath(top) {
                        for (k in 0 until 4) line(Color(0x8C6F8FE0).copy(alpha = 0.6f * al), p.x - s, p.y - s * (0.1f + k * 0.14f), p.x + s, p.y - s * (0.1f + k * 0.14f), 0.28f)
                        line(Color(0xFFF2A3A3).copy(alpha = 0.85f * al), p.x - s * 0.62f, p.y - s, p.x - s * 0.62f, p.y + s, 0.32f)
                        drawPath(star5(p.x - s * 0.3f, p.y - s * 0.3f, s * 0.13f), Color(0xFFFFD23F).copy(alpha = al))
                    }
                    drawPath(top, navy.copy(alpha = al), style = stroke(w))
                    if (!small) line(Color.White.copy(alpha = 0.9f * al), p.x + s * 0.92f, p.y - s * 0.01f, p.x - s * 0.4f, p.y - s * 0.05f, 0.5f)
                } }
            }
        }
        "supernova" -> {
            // a supernova do Pro: raios de luz girando e um halo que pulsa atrás; seis páginas de ouro em órbita
            // (passam por trás e pela frente) e uma coroa de três estrelas piscando por cima
            if (behind) {
                val pulse = 1f + 0.06f * sin(t * 2.4f)
                drawCircle(Brush.radialGradient(listOf(Color(0x80FFE07A), Color(0x33FFB421), Color(0x00FFB421)), center = Offset(50f, 56f), radius = 54f * pulse * boost), 54f * pulse * boost, Offset(50f, 56f))
                if (!small) rotate(t * 12f, pivot = Offset(50f, 56f)) {
                    for (i in 0 until 12) {
                        val a = i * 2f * PIf / 12f; val r = 74f
                        drawPath(Path().apply { moveTo(50f, 56f); lineTo(50f + cos(a - 0.06f) * r, 56f + sin(a - 0.06f) * r); lineTo(50f + cos(a + 0.06f) * r, 56f + sin(a + 0.06f) * r); close() },
                            Brush.radialGradient(listOf(Color(0x33FFE07A).copy(alpha = 0.16f + 0.08f * sin(t * 2f + i)), Color(0x00FFE07A)), center = Offset(50f, 56f), radius = r))
                    }
                }
            }
            for (i in 0 until 6) {
                val a = t * 1.1f + i * 2f * PIf / 6f; val front = sin(a) > 0f; if (front == behind) continue
                val x = 50f + cos(a) * 52f; val y = 62f + sin(a) * 16f; val s = (if (front) 1f else 0.7f) * boost
                rotate(cos(a) * 25f, pivot = Offset(x, y)) {
                    val pg = roundRect(x - 2.6f * s, y - 3.4f * s, 5.2f * s, 6.8f * s, 0.6f * s)
                    drawPath(pg, if (front) vg(listOf(Color(0xFFFFFDF2), Color(0xFFFFE9B0)), y - 3.4f * s, y + 3.4f * s) else SolidColor(Color(0x99FFE9B0)))
                    drawPath(pg, Gold.copy(alpha = if (front) 1f else 0.6f), style = stroke(0.45f))
                    if (front && !small) { line(Color(0x99C98A12), x - 1.6f * s, y - 1f * s, x + 1.6f * s, y - 1f * s, 0.3f); line(Color(0x99C98A12), x - 1.6f * s, y + 0.6f * s, x + 1.2f * s, y + 0.6f * s, 0.3f) }
                }
                if (front && !small) star4(x - cos(a + 0.35f) * 5f, y - sin(a + 0.35f) * 2f, 1.2f, Color(0xCCFFE07A))
            }
            if (!behind) for (i in 0 until 3) {
                val (x, y) = listOf(36f to 15f, 50f to 8f, 64f to 15f)[i]
                val r = (if (i == 1) 3.6f else 2.6f) * (0.65f + 0.35f * sin(t * 3f + i * 2f)) * boost
                drawCircle(Brush.radialGradient(listOf(Color(0x99FFE07A), Color(0x00FFE07A)), center = Offset(x, y), radius = r * 2.2f), r * 2.2f, Offset(x, y))
                star4(x, y, r, if (i == 1) Color.White else Gold)
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
        "giroflex" -> {
            // giroflex: atrás dele, a luz vermelha e a azul se revezando, com os fachos varrendo a partir dos cantos de cima
            val redOn = (t * 2.2f) % 1f < 0.5f; val flash = sin(PIf * ((t * 4.4f) % 1f))
            if (behind) for ((sd, col) in listOf(-1f to Color(0xFFFF3C46), 1f to Color(0xFF3C78FF))) {
                val on = if (sd < 0f) redOn else !redOn; val a = (if (on) 0.72f else 0.14f) * (0.6f + 0.4f * flash) * boost; val x = 50f + sd * 44f
                drawCircle(Brush.radialGradient(listOf(col.copy(alpha = a.coerceIn(0f, 1f)), col.copy(alpha = 0f)), center = Offset(x, 58f), radius = 40f), 40f, Offset(x, 58f))
                translate(50f + sd * 56f, -6f) {
                    rotate((PIf / 2f - sd * (0.45f + 0.35f * sin(t * 3f + (if (sd > 0f) PIf else 0f)))) * DEG, pivot = Offset.Zero) {
                        drawPath(Path().apply { moveTo(0f, 0f); lineTo(90f, -12f); lineTo(90f, 12f); close() }, Brush.horizontalGradient(listOf(col.copy(alpha = ((if (on) 0.62f else 0.14f) * boost).coerceIn(0f, 1f)), col.copy(alpha = 0f)), startX = 0f, endX = 90f))
                    }
                }
            }
            else if (!small) for (i in 0 until 4) {
                val ph = (t * 0.6f + i / 4f) % 1f; val sd = if (i % 2 == 1) 1f else -1f
                star4(50f + sd * (40f + i * 2f), 30f + i * 12f, 1.8f * sin(PIf * ph), (if (sd < 0f) Color(0xFFFF7882) else Color(0xFF78AAFF)).copy(alpha = sin(PIf * ph)))
            }
        }
        "abc" -> if (!behind) {
            // letras e contas de giz subindo devagar dos lados (nunca na frente do rosto), com pozinho de giz
            val glyphs = listOf(
                listOf(floatArrayOf(-1.2f, 1.6f, 0f, -1.6f), floatArrayOf(0f, -1.6f, 1.2f, 1.6f), floatArrayOf(-0.6f, 0.4f, 0.6f, 0.4f)),
                listOf(floatArrayOf(-1.2f, 0f, 1.2f, 0f), floatArrayOf(0f, -1.2f, 0f, 1.2f)),
                listOf(floatArrayOf(-1.2f, -0.5f, 1.2f, -0.5f), floatArrayOf(-1.2f, 0.5f, 1.2f, 0.5f)),
                listOf(floatArrayOf(-0.5f, -1f, 0f, -1.6f), floatArrayOf(0f, -1.6f, 0f, 1.6f)),
                listOf(floatArrayOf(-1f, -1f, 1f, 1f), floatArrayOf(-1f, 1f, 1f, -1f)),
                listOf(floatArrayOf(-1.4f, 0.2f, -0.6f, 1.6f), floatArrayOf(-0.6f, 1.6f, 0.2f, -1.6f), floatArrayOf(0.2f, -1.6f, 1.6f, -1.6f)),
                listOf(floatArrayOf(-1.4f, -1.2f, 1.4f, -1.2f), floatArrayOf(-0.6f, -1.2f, -0.8f, 1.6f), floatArrayOf(0.6f, -1.2f, 0.8f, 1.6f)),
            )
            val cols = listOf(Color.White, Color(0xFFBEF5DC), Color(0xFFFFECA0), Color(0xFFFFC8D7))
            for (i in 0 until if (small) 5 else 10) {
                val ph = (t * 0.22f + i * 0.1f) % 1f; val sd = if (i % 2 == 1) 1f else -1f
                val x = 50f + sd * (36f + ((i * 7) % 14)) + sin(t * 1.2f + i) * 2f; val y = 96f - ph * 92f
                if (y > 40f && y < 92f && x > 14f && x < 86f) continue
                val a = sin(PIf * ph); val sc = (1.2f + (i % 3) * 0.25f) * boost
                translate(x, y) {
                    rotate(sin(t + i) * 0.3f * DEG, pivot = Offset.Zero) {
                        scale(sc, sc, pivot = Offset.Zero) {
                            for ((w, col) in listOf(1.1f to Color(0xFF1D1670).copy(alpha = 0.3f * a), 0.5f to cols[i % 4].copy(alpha = 0.95f * a))) for (q in glyphs[i % glyphs.size]) line(col, q[0], q[1], q[2], q[3], w)
                        }
                    }
                }
                if (!small) drawCircle(Color.White.copy(alpha = 0.4f * a), 0.4f, Offset(x + 1.6f, y + 2.2f))
            }
        }
        "graficos" -> if (!behind) {
            // gráficos de controle flutuando dos lados (a última barra sobe e desce) e moedas girando para cima
            for (q in listOf(floatArrayOf(-4f, 60f, 0f), floatArrayOf(98f, 20f, 0.5f))) {
                val cx = q[0]; val y = q[1] + sin(t * 1.2f + q[2] * 6f) * 1.4f; val grow = 0.5f + 0.5f * sin(t * 1.6f + q[2] * 6f)
                val card = roundRect(cx - 6f, y - 6f, 12f, 10f, 1.6f); drawPath(card, Color.White.copy(alpha = 0.92f)); drawPath(card, Color(0x591D1670), style = stroke(0.3f))
                listOf(0.35f, 0.55f, 0.75f, 1f).forEachIndexed { i, hh -> val bh = 7f * hh * (if (i == 3) 0.7f + 0.3f * grow else 1f); drawRect(if (i == 3) Color(0xFF2E9E72) else Color(0xFF7B8CFF), Offset(cx - 4.4f + i * 2.4f, y + 2.6f - bh), Size(1.6f, bh)) }
                drawPath(Path().apply { moveTo(cx - 4.6f, y + 0.6f); lineTo(cx - 1.6f, y - 1.4f); lineTo(cx + 0.6f, y - 0.6f); lineTo(cx + 4f, y - 4.2f) }, Color(0xFFF5B83D), style = stroke(0.5f))
            }
            if (!small) for (i in 0 until 6) {
                val ph = (t * 0.3f + i / 6f) % 1f; val sd = if (i % 2 == 1) 1f else -1f
                val x = 50f + sd * (40f + ((i * 5) % 10)); val y = 94f - ph * 60f; val a = sin(PIf * ph); val sq = cos(t * 4f + i)
                scale(maxOf(0.15f, abs(sq)), 1f, pivot = Offset(x, y)) { drawCircle(Color(0xFFB57A0C).copy(alpha = a), 1.9f * boost, Offset(x, y)); drawCircle(Color(0xFFFFD877).copy(alpha = a), 1.5f * boost, Offset(x, y)) }
                if (abs(sq) > 0.6f) line(Color(0xFFB57A0C).copy(alpha = a), x, y - 0.9f * boost, x, y + 0.9f * boost, 0.35f)
            }
        }
        "batimentos" -> {
            // batimentos: atrás dele, a linha do monitor cardíaco com o pulso de luz correndo; na frente,
            // coraçõezinhos que pulsam no ritmo e cruzinhas subindo
            val beat = (t * 1.25f) % 1f
            val thump = exp(-((beat - 0.05f) * (beat - 0.05f)) / 0.002f) + 0.6f * exp(-((beat - 0.22f) * (beat - 0.22f)) / 0.002f)
            if (behind) {
                val y0 = 66f
                fun ecg(x: Float): Float {
                    val u = ((x + 20f) % 40f) / 40f
                    return when { u < 0.38f -> 0f; u < 0.43f -> -(u - 0.38f) * 40f; u < 0.5f -> -2f + (u - 0.43f) * 200f; u < 0.56f -> 12f - (u - 0.5f) * 320f; u < 0.62f -> -7.2f + (u - 0.56f) * 120f; u < 0.72f -> sin((u - 0.62f) * PIf / 0.1f) * 2f; else -> 0f }
                }
                val p = Path(); var x = -16f
                while (x <= 116f) { val y = y0 - ecg(x) * 0.9f; if (x == -16f) p.moveTo(x, y) else p.lineTo(x, y); x += 0.5f }
                drawPath(p, Color(0x403FBF8F), style = stroke(if (small) 2.6f else 2f)); drawPath(p, Mint.copy(alpha = 0.85f), style = stroke(if (small) 1.2f else 0.7f))
                val hx = -16f + ((t * 52f) % 132f); val hy = y0 - ecg(hx) * 0.9f
                drawCircle(Brush.radialGradient(listOf(Color(0xF2DCFFF0), Color(0x007EE0B8)), center = Offset(hx, hy), radius = 7f), 7f, Offset(hx, hy))
                drawCircle(Color.White, 0.9f, Offset(hx, hy))
            } else {
                for (q in listOf(floatArrayOf(8f, 46f, 3f, 0f), floatArrayOf(92f, 40f, 3.4f, 0.5f), floatArrayOf(12f, 86f, 2.4f, 0.3f), floatArrayOf(90f, 84f, 2.6f, 0.8f))) {
                    val r = q[2] * (1f + 0.28f * thump * boost) * (if (small) 1.2f else 1f); val x = q[0]; val y = q[1] + sin(t * 1.4f + q[3] * 6f) * 1.2f
                    drawPath(heart(x, y, r), vg(listOf(Color(0xFFFF8FA3), Color(0xFFE0475E)), y - r, y + r))
                    rotate(-0.6f * DEG, pivot = Offset(x - r * 0.4f, y - r * 0.35f)) { oval(Color.White.copy(alpha = 0.7f), x - r * 0.65f, y - r * 0.51f, r * 0.5f, r * 0.32f) }
                }
                if (!small) for (i in 0 until 5) {
                    val ph = (t * 0.3f + i / 5f) % 1f; val side = if (i % 2 == 1) 1f else -1f; val x = 50f + side * (42f + ((i * 5) % 9)); val y = 92f - ph * 70f; val a = sin(PIf * ph); val sc = 1.2f * boost
                    drawRect(Color.White.copy(alpha = 0.9f * a), Offset(x - 0.45f * sc, y - 1.5f * sc), Size(0.9f * sc, 3f * sc)); drawRect(Color.White.copy(alpha = 0.9f * a), Offset(x - 1.5f * sc, y - 0.45f * sc), Size(3f * sc, 0.9f * sc))
                    drawRect(MintDark.copy(alpha = 0.9f * a), Offset(x - 0.25f * sc, y - 1.2f * sc), Size(0.5f * sc, 2.4f * sc)); drawRect(MintDark.copy(alpha = 0.9f * a), Offset(x - 1.2f * sc, y - 0.25f * sc), Size(2.4f * sc, 0.5f * sc))
                }
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
        "patente" -> {
            // estrelas da patente (militar): cinco estrelas douradas em arco sobre a cabeça, com sombra, contorno,
            // realce e um brilho que passa de uma para a outra; atrás, um leque suave de luz dourada
            val n = if (small) 3 else 5
            if (behind) drawCircle(Brush.radialGradient(listOf(Spark.copy(alpha = 0.4f), Spark.copy(alpha = 0f)), center = Offset(50f, 10f), radius = 46f), 46f, Offset(50f, 10f))
            else for (i in 0 until n) {
                val u = i / (n - 1f); val a = PIf * (1.15f + u * 0.7f); val x = 50f + cos(a) * 44f; val y = 30f + sin(a) * 30f
                val tw = 0.5f + 0.5f * sin(t * 3f - i * 0.9f); val r = (if (i == (n - 1) / 2) 4.6f else 3.5f) * (0.92f + 0.08f * tw) * boost * (if (small) 1.2f else 1f)
                drawCircle(Brush.radialGradient(listOf(Spark.copy(alpha = 0.4f * tw), Spark.copy(alpha = 0f)), center = Offset(x, y), radius = r * 2f), r * 2f, Offset(x, y))
                drawPath(star5(x + 0.5f, y + 0.7f, r), Color(0x400E0A45))
                val st = star5(x, y, r); drawPath(st, vg(GOLD3, y - r, y + r)); drawPath(st, Color(0xFFA86A08), style = stroke(if (small) 0.8f else 0.45f))
                drawPath(star5(x - r * 0.1f, y - r * 0.15f, r * 0.42f), Color.White.copy(alpha = 0.4f))
                if (tw > 0.85f && !small) star4(x - r * 0.45f, y - r * 0.55f, 1.8f * (tw - 0.85f) / 0.15f, Color.White.copy(alpha = 0.95f))
            }
        }
        "colunas" -> {
            // colunas do fórum (tribunais): atrás dele, duas colunas gregas de mármore (base em degraus, fuste
            // canelado, capitel com volutas) sustentando o frontão; um reflexo de luz desce devagar pelo mármore
            if (behind) {
                val ol = Color(0x80463C78); val ws = if (small) 0.8f else 0.4f
                fun marble(x0: Float, x1: Float) = hg(listOf(Color(0xFFC9C3DC), Color(0xFFF7F5FC), Color(0xFFE4E0F0), Color(0xFFB9B2D0)), x0, x1)
                val ped = Path().apply { moveTo(-6f, 21.6f); lineTo(50f, 3f); lineTo(106f, 21.6f); close() }
                drawPath(ped, vg(listOf(Color(0xFFF7F5FC), Color(0xFFD9D4E8)), 3f, 21.6f)); drawPath(ped, ol, style = stroke(if (small) 0.8f else 0.45f))
                if (!small) drawPath(Path().apply { moveTo(2f, 20f); lineTo(50f, 5.6f); lineTo(98f, 20f); close() }, Color(0x4D463C78), style = stroke(0.35f))
                val arch = roundRect(-6f, 21.4f, 112f, 4.6f, 0.6f); drawPath(arch, vg(listOf(Color(0xFFEEEBF6), Color(0xFFC9C3DC)), 21.4f, 26f)); drawPath(arch, ol, style = stroke(ws))
                for (cx in listOf(3f, 97f)) {
                    val w = 9f; val top = 26f; val bot = 93f
                    val b1 = roundRect(cx - w / 2f - 2f, bot - 3f, w + 4f, 3f, 0.6f); drawPath(b1, marble(cx - w / 2f - 2f, cx + w / 2f + 2f)); drawPath(b1, ol, style = stroke(0.4f))
                    val b2 = roundRect(cx - w / 2f - 1f, bot - 5f, w + 2f, 2f, 0.5f); drawPath(b2, marble(cx - w / 2f - 1f, cx + w / 2f + 1f)); drawPath(b2, ol, style = stroke(0.4f))
                    val sh = Path().apply { addRect(Rect(cx - w / 2f, top + 4f, cx + w / 2f, bot - 5f)) }; drawPath(sh, marble(cx - w / 2f, cx + w / 2f))
                    if (!small) {
                        for (kk in 1..3) line(Color(0x73786EA0), cx - w / 2f + kk * w / 4f, top + 5f, cx - w / 2f + kk * w / 4f, bot - 6f, 0.45f)
                        val ph = (t * 0.18f + (if (cx > 50f) 0.5f else 0f)) % 1f; val sy = top + 4f + ph * (bot - top - 9f)
                        clipPath(sh) { drawRect(vg(listOf(Color.White.copy(alpha = 0f), Color.White.copy(alpha = 0.75f), Color.White.copy(alpha = 0f)), sy - 6f, sy + 6f), Offset(cx - w / 2f, sy - 6f), Size(w, 12f)) }
                    }
                    drawPath(sh, ol, style = stroke(0.4f))
                    val ab = roundRect(cx - w / 2f - 2.4f, top, w + 4.8f, 1.8f, 0.4f); drawPath(ab, marble(cx - w / 2f - 2.4f, cx + w / 2f + 2.4f)); drawPath(ab, ol, style = stroke(0.4f))
                    val ec = roundRect(cx - w / 2f - 1f, top + 1.8f, w + 2f, 2.2f, 0.6f); drawPath(ec, marble(cx - w / 2f - 1f, cx + w / 2f + 1f)); drawPath(ec, ol, style = stroke(0.4f))
                    for (s in listOf(-1f, 1f)) {   // volutas
                        val vx = cx + s * (w / 2f + 0.6f); val vy = top + 3.2f
                        drawCircle(Color(0xFFF1EEF8), 1.5f, Offset(vx, vy)); drawCircle(ol, 1.5f, Offset(vx, vy), style = stroke(0.4f))
                        drawArc(ol, 0f, 288f, false, Offset(vx - 0.8f, vy - 0.8f), Size(1.6f, 1.6f), style = stroke(0.4f)); drawCircle(Color(0x99463C78), 0.3f, Offset(vx, vy))
                    }
                }
            } else if (!small) for (i in 0 until 4) {   // brilhinhos nos capitéis
                val ph = (t * 0.5f + i / 4f) % 1f; val a = sin(PIf * ph)
                star4(if (i < 2) 3f + (if (i == 1) 4f else -4f) else 97f + (if (i == 3) 4f else -4f), 28f + (i % 2) * 2f, 1.6f * a, Color.White.copy(alpha = a))
            }
        }
        "cifroes" -> if (!behind) for (i in 0 until if (small) 3 else 7) {
            // cifrões (fiscal): "R$" verdes de contorno escuro subindo dos lados (nunca na frente do rosto),
            // balançando, com realce claro e brilhinho dourado
            val ph = (t * 0.24f + i / 7f) % 1f; val sd = if (i % 2 == 1) 1f else -1f
            val x = 50f + sd * (38f + ((i * 11) % 12)) + sin(t * 1.3f + i) * 2f; val y = 96f - ph * 92f
            if (y > 40f && y < 92f && x > 14f && x < 86f) continue
            val a = sin(PIf * ph); val sc = (1.3f + (i % 3) * 0.3f) * boost * (if (small) 1.3f else 1f)
            val rP = Path().apply { moveTo(-2.7f, 1.6f); lineTo(-2.7f, -1.6f); lineTo(-1.5f, -1.6f); quadraticTo(-0.3f, -1.6f, -0.3f, -0.75f); quadraticTo(-0.3f, 0.1f, -1.5f, 0.1f); lineTo(-2.7f, 0.1f); moveTo(-1.6f, 0.1f); lineTo(-0.3f, 1.6f) }
            val dP = auraDollar()
            translate(x, y) { rotate(sin(t * 1.5f + i) * 0.25f * DEG, pivot = Offset.Zero) { scale(sc, sc, pivot = Offset.Zero) {
                for ((w, c) in listOf(1.4f to Color(0xFF0E462D).copy(alpha = 0.55f * a), 0.75f to MintDark.copy(alpha = a), 0.25f to Color(0xFFD2FFEB).copy(alpha = 0.85f * a))) {
                    drawPath(rP, c, style = stroke(w)); translate(1.35f, 0f) { drawPath(dP, c, style = stroke(w)) }
                }
            } } }
            if (!small && i % 2 == 0) star4(x + 3.6f * sc, y - 2.4f * sc, 1.2f * a * abs(sin(t * 3f + i)), Gold.copy(alpha = a))
        }
        "chuva-moedas" -> {
            // chuva de moedas (bancos, LENDÁRIA): atrás, um clarão dourado que pulsa e moedas pequenas caindo ao
            // longe; na frente, moedas de ouro caindo dos lados girando (achatam e mostram a borda), com o cifrão
            // gravado, reflexo, um estalo de brilho quando batem nas pilhas do chão e purpurina dourada piscando
            if (behind) {
                val pulse = 0.75f + 0.25f * sin(t * 2.2f)
                drawCircle(Brush.radialGradient(0f to Spark.copy(alpha = (0.42f * pulse * boost).coerceIn(0f, 1f)), 0.6f to Color(0xFFFFBE3C).copy(alpha = 0.16f * pulse), 1f to Color(0x00FFBE3C), center = Offset(50f, 52f), radius = 62f), 62f, Offset(50f, 52f))
                if (!small) for (i in 0 until 9) { val ph = (t * 0.17f + i / 9f) % 1f; drawAuraCoin(4f + ((i * 29) % 92), -10f + ph * 104f, 1.5f, t * 4f + i, 0.5f * sin(PIf * ph)) }
            } else {
                for (i in 0 until if (small) 5 else 12) {
                    val ph = (t * 0.3f + i * 0.083f) % 1f; val sd = if (i % 2 == 1) 1f else -1f; val x = 50f + sd * (36f + ((i * 13) % 18)) + sin(t + i) * 1.5f; val y = -8f + ph * 94f
                    drawAuraCoin(x, y, (2.2f + (i % 3) * 0.45f) * boost * (if (small) 1.2f else 1f), t * 5f + i * 1.7f, if (ph > 0.93f) (1f - ph) / 0.07f else minOf(1f, ph * 8f))
                    if (ph > 0.9f && !small) { val u = (ph - 0.9f) / 0.1f; for (j in 0 until 4) { val an = -PIf * 0.15f - j * PIf * 0.23f; drawCircle(Gold.copy(alpha = 1f - u), 0.45f, Offset(x + cos(an) * (1f + u * 4f), 87f + sin(an) * (1f + u * 3f))) } }
                }
                if (!small) {
                    val sx = floatArrayOf(6f, 94f, 16f, 84f, 2f, 98f, 24f, 76f); val sy = floatArrayOf(30f, 26f, 58f, 52f, 80f, 74f, 12f, 8f)
                    for (i in 0 until 8) { val ph = (t * 0.7f + i * 0.13f) % 1f; val a = sin(PIf * ph); star4(sx[i], sy[i], 1.6f * a, if (i % 2 == 1) Color.White.copy(alpha = a) else Gold.copy(alpha = a)) }
                }
            }
        }
        "votos" -> if (!behind) for (i in 0 until if (small) 4 else 8) {
            // votos voando (legislativo): cartõezinhos de "SIM" (verde) e "NÃO" (vermelho) subindo dos lados, girando
            // no ar (viram e mostram o verso com o visto ou o xis), com faixa colorida e a palavra a traço
            val ph = (t * 0.2f + i / 8f) % 1f; val sd = if (i % 2 == 1) 1f else -1f
            val x = 50f + sd * (39f + ((i * 9) % 12)) + sin(t * 1.1f + i) * 3f; val y = 96f - ph * 94f
            if (y > 40f && y < 92f && x > 14f && x < 86f) continue
            val a = sin(PIf * ph); val yes = i % 3 != 1; val flip = cos(t * 2.2f + i * 1.4f); val sc = boost * (if (small) 1.4f else 1.1f)
            val ca = (if (yes) Color(0xFF2E9E72) else Color(0xFFE0475E)).copy(alpha = a)
            translate(x, y) { rotate(sin(t * 1.7f + i) * 0.35f * DEG, pivot = Offset.Zero) { scale(sc * maxOf(0.12f, abs(flip)), sc, pivot = Offset.Zero) {
                val card = roundRect(-4f, -2.6f, 8f, 5.2f, 0.9f)
                translate(0.4f, 0.5f) { drawPath(card, Color(0xFF0E0A45).copy(alpha = 0.2f * a)) }
                drawPath(card, vg(listOf(Color.White, Color(0xFFECEAF6)), -2.6f, 2.6f), alpha = a)
                clipPath(card) { drawRect(ca, Offset(-4f, -2.6f), Size(1.3f, 5.2f)) }
                drawPath(card, Color(0xFF26215C).copy(alpha = 0.5f * a), style = stroke(0.3f))
                if (flip > 0f) auraWord(if (yes) "SIM" else "NAO", 0.65f, 0.15f, 0.5f, ca, 0.5f)
                else if (yes) drawPath(Path().apply { moveTo(-0.8f, 0f); lineTo(0.4f, 1.2f); lineTo(2.4f, -1.4f) }, ca, style = stroke(0.8f))
                else { line(ca, -0.6f, -1.3f, 1.8f, 1.3f, 0.8f); line(ca, 1.8f, -1.3f, -0.6f, 1.3f, 0.8f) }
            } } }
        }
        "papeis" -> for (i in 0 until if (small) 4 else 8) {
            // papéis voando (administrativo): folhas de ofício que atravessam a cena ao vento, tremulando (viram e
            // dobram), com pauta, margem, clipe ou carimbo; as da frente passam por cima, as outras por trás
            val front = i % 2 == 0; if (front == behind) continue
            val ph = (t * 0.09f + i / 8f) % 1f; val x = -12f + ph * 124f
            val y = (if (front) 2f + ((i * 13) % 26) else 24f + ((i * 29) % 56)) + sin(t * 1.4f + i * 2f) * 4f + sin(ph * PIf * 2f) * 5f
            val al = minOf(1f, ph * 8f, (1f - ph) * 8f); val flut = cos(t * 3f + i * 1.9f); val k = (if (front) 1f else 0.75f) * boost * (if (small) 1.3f else 1f)
            translate(x, y) { rotate((sin(t * 1.2f + i) * 0.5f + ph * 1.2f) * DEG, pivot = Offset.Zero) { scale(k * maxOf(0.15f, abs(flut)), k * (0.85f + 0.15f * sin(t * 4f + i)), pivot = Offset.Zero) {
                val sh = Path().apply { moveTo(-3f, -4f); lineTo(1.8f, -4f); lineTo(3f, -2.8f); lineTo(3f, 4f); lineTo(-3f, 4f); close() }
                translate(0.4f, 0.5f) { drawPath(sh, Color(0xFF0E0A45).copy(alpha = 0.15f * al)) }
                if (flut > 0f) drawPath(sh, vg(listOf(Color.White, Color(0xFFEEECF7)), -4f, 4f), alpha = al) else drawPath(sh, Color(0xFFE2DFF0).copy(alpha = al))
                if (flut > 0f && !small) {
                    for (j in 0 until 5) line(Color(0xFF6F8FE0).copy(alpha = 0.55f * al), -2.2f, -2.2f + j * 1.3f, if (j > 0) 2.3f else 0.9f, -2.2f + j * 1.3f, 0.25f)
                    line(Color(0xFFF2A3A3).copy(alpha = 0.85f * al), -1.9f, -3.7f, -1.9f, 3.7f, 0.25f)
                    if (i % 4 == 2) { drawCircle(Color(0xFFE0475E).copy(alpha = 0.8f * al), 1.1f, Offset(1.2f, 2.2f), style = Stroke(0.3f)); line(Color(0xFFE0475E).copy(alpha = 0.8f * al), 0.5f, 2.2f, 1.9f, 2.2f, 0.3f) }
                }
                drawPath(sh, Color(0xFF26215C).copy(alpha = 0.45f * al), style = stroke(0.3f))
                val cn = Path().apply { moveTo(1.8f, -4f); lineTo(1.8f, -2.8f); lineTo(3f, -2.8f); close() }; drawPath(cn, Color(0xFFD8D4E6).copy(alpha = al)); drawPath(cn, Color(0xFF26215C).copy(alpha = 0.35f * al), style = stroke(0.2f))
                if (i % 4 == 0) drawPath(Path().apply { moveTo(-1.6f, -2.4f); lineTo(-1.6f, -4.6f); quadraticTo(-1.6f, -5.2f, -1f, -5.2f); quadraticTo(-0.4f, -5.2f, -0.4f, -4.6f); lineTo(-0.4f, -2.8f) }, Color(0xFF8E92A6).copy(alpha = al), style = stroke(0.35f))
            } } }
        }
        "bolhas" -> for (i in 0 until if (small) 5 else 11) {
            // bolhas de sabão: sobem balançando com reflexo furta-cor na borda, janelinha de brilho, e estouram no
            // alto num anel de gotinhas; as da frente ficam dos lados, as de trás sobem por toda a cena
            val front = i % 3 != 0; if (front == behind) continue
            val ph = (t * 0.15f + i * 0.091f) % 1f; val sd = if (i % 2 == 1) 1f else -1f
            val x = (if (front) 50f + sd * (37f + ((i * 7) % 15)) else 12f + ((i * 31) % 76)) + sin(t * 1.3f + i) * 3f; val y = 98f - ph * 100f
            if (front && y > 40f && y < 92f && x > 14f && x < 86f) continue
            val r = (2f + (i % 4) * 0.8f) * boost * (if (front) 1f else 0.75f) * (if (small) 1.2f else 1f)
            if (ph > 0.9f) { val u = (ph - 0.9f) / 0.1f; for (j in 0 until 6) { val an = j * PIf / 3f; drawCircle(Color(0xFF96D2FF).copy(alpha = 1f - u), 0.35f, Offset(x + cos(an) * r * (1f + u), y + sin(an) * r * (1f + u))) }; continue }
            val wob = 1f + 0.06f * sin(t * 5f + i)
            scale(wob, 2f - wob, pivot = Offset(x, y)) {
                drawCircle(Brush.radialGradient(0f to Color.White.copy(alpha = 0.05f), 0.7f to Color(0xFFB4DCFF).copy(alpha = 0.12f), 0.9f to Color(0xFFFFAAE6).copy(alpha = 0.4f), 1f to Color(0xFF8CDCFF).copy(alpha = 0.6f), center = Offset(x, y), radius = r), r, Offset(x, y))
                drawCircle(Brush.horizontalGradient(listOf(Color(0xD9FF8CC8), Color(0xD978C8FF), Color(0xD996F0BE)), startX = x - r, endX = x + r), r, Offset(x, y), style = Stroke(if (small) 0.6f else 0.35f))
                drawArc(Color.White.copy(alpha = 0.9f), 198f, 63f, false, Offset(x - r * 0.68f, y - r * 0.68f), Size(r * 1.36f, r * 1.36f), style = stroke(0.5f))
                drawCircle(Color.White.copy(alpha = 0.85f), r * 0.12f, Offset(x + r * 0.42f, y - r * 0.42f))
            }
        }
        "outono" -> {
            // folhas de outono: folhas de bordo (laranja, vermelha, amarela e marrom) descendo em zigue-zague,
            // girando e virando; as da frente caem dos lados, as de trás por toda a cena
            val cs = listOf(Color(0xFFFF8A5C) to Color(0xFFE0472A), Color(0xFFFFC266) to Color(0xFFF08A24), Color(0xFFFFE27A) to Color(0xFFE0A82A), Color(0xFFD9965A) to Color(0xFF9A5A2A), Color(0xFFFF7A88) to Color(0xFFC8324A))
            for (i in 0 until if (small) 5 else 12) {
                val front = i % 2 == 0; if (front == behind) continue
                val ph = (t * 0.11f + i * 0.083f) % 1f; val sd = if (i % 4 < 2) -1f else 1f
                val x = (if (front) 50f + sd * (40f + ((i * 7) % 12)) else 10f + ((i * 37) % 80)) + sin(t * 1.3f + i * 1.7f) * 6f; val y = -6f + ph * 100f
                if (front && y > 40f && y < 92f && x > 14f && x < 86f) continue
                val (c0, c1) = cs[i % 5]
                drawAuraLeaf(x, y, (if (front) 1.5f else 1.1f) * boost * (if (small) 1.3f else 1f), sin(t * 1.1f + i) * 0.9f + i, cos(t * 2f + i * 1.3f), c0, c1, minOf(1f, ph * 10f, (1f - ph) * 10f))
            }
        }
        "neve" -> for (i in 0 until if (small) 6 else 16) {
            // neve: flocos de seis pontas com raminhos e bolinhas macias, caindo devagar e balançando (os da frente
            // dos lados, os de trás por toda a cena); contorno azulado para aparecer no fundo claro
            val front = i % 2 == 0; if (front == behind) continue
            val ph = (t * 0.1f + i * 0.0625f) % 1f; val sd = if (i % 4 < 2) -1f else 1f
            val x = (if (front) 50f + sd * (39f + ((i * 5) % 13)) else 4f + ((i * 41) % 92)) + sin(t * 0.9f + i * 1.7f) * 3f; val y = -6f + ph * 102f
            if (front && y > 40f && y < 92f && x > 14f && x < 86f) continue
            val al = minOf(1f, ph * 10f, (1f - ph) * 10f) * (if (front) 1f else 0.8f)
            if (i % 3 == 0) {
                val s = (if (front) 1.3f else 0.9f) * boost * (if (small) 1.3f else 1f)
                translate(x, y) { rotate((t * 0.5f + i) * DEG, pivot = Offset.Zero) { scale(s, s, pivot = Offset.Zero) {
                    for ((w, c) in listOf(0.95f to Color(0xFF6F8FE0).copy(alpha = 0.45f * al), 0.45f to Color.White.copy(alpha = al))) for (kk in 0 until 6) {
                        val an = kk * PIf / 3f; val ca = cos(an); val sa = sin(an)
                        fun pp(px: Float, py: Float) = Offset(px * ca - py * sa, px * sa + py * ca)
                        val o = pp(0f, -2.4f); val m = pp(0f, -1.4f); val b1 = pp(0.7f, -2f); val b2 = pp(-0.7f, -2f)
                        line(c, 0f, 0f, o.x, o.y, w); line(c, m.x, m.y, b1.x, b1.y, w); line(c, m.x, m.y, b2.x, b2.y, w)
                    }
                } } }
            } else {
                val r = (0.7f + (i % 3) * 0.35f) * boost * (if (front) 1f else 0.8f) * (if (small) 1.3f else 1f)
                drawCircle(Brush.radialGradient(0f to Color.White.copy(alpha = al), 0.7f to Color(0xFFF0F6FF).copy(alpha = 0.9f * al), 1f to Color(0xFF96B4EB).copy(alpha = 0.6f * al), center = Offset(x, y), radius = r), r, Offset(x, y))
            }
        }
        "arcoiris" -> {
            // arco-íris atrás do Folha: sete faixas em arco com um brilho que corre por elas; na frente, nuvenzinhas
            // fofas nas duas pontas e brilhinhos piscando em cima do arco
            val cx = 50f; val cy = 96f; val rxO = 54f; val ryO = 92f; val bw = 3.2f
            val cols = listOf(Color(0xFFFF5C6E), Color(0xFFFF9F3A), Color(0xFFFFD84A), Color(0xFF3FBF8F), Color(0xFF6FB0FF), Color(0xFF5A4FE6), Color(0xFFB995F5))
            if (behind) {
                val ga = 0.9f * (0.9f + 0.1f * sin(t * 1.5f))
                cols.forEachIndexed { kk, c -> val o = kk * bw + bw / 2f; drawArc(c.copy(alpha = ga), 180f, 180f, false, Offset(cx - (rxO - o), cy - (ryO - o)), Size(2f * (rxO - o), 2f * (ryO - o)), style = Stroke(bw + 0.25f)) }
                if (!small) {
                    val a = 180f + ((t * 0.22f) % 1f) * 180f; val o = 3.5f * bw
                    drawArc(Color.White.copy(alpha = 0.35f * ga), a - 5.7f, 11.4f, false, Offset(cx - (rxO - o), cy - (ryO - o)), Size(2f * (rxO - o), 2f * (ryO - o)), style = Stroke(7f * bw))
                    drawArc(Color.White.copy(alpha = 0.6f * ga), 180f, 180f, false, Offset(cx - rxO, cy - ryO), Size(2f * rxO, 2f * ryO), style = Stroke(0.4f))
                }
            } else {
                val mid = rxO - 3.5f * bw
                val puffs = listOf(floatArrayOf(-4.4f, 0.6f, 2.8f), floatArrayOf(-1.4f, -1.6f, 3.6f), floatArrayOf(2.2f, -0.8f, 3.2f), floatArrayOf(4.8f, 0.8f, 2.4f), floatArrayOf(0f, 1.2f, 3.2f))
                for ((sx, ph) in listOf(cx - mid to 0f, cx + mid to 1.6f)) {
                    val by = 93f + sin(t * 1.4f + ph) * 0.8f; val kk = if (small) 1.2f else 1f
                    for (p in puffs) drawCircle(Color(0x736F8FE0), (p[2] + 0.4f) * kk, Offset(sx + p[0] * kk, by + p[1] * kk))
                    for (p in puffs) drawCircle(Brush.radialGradient(listOf(Color.White, Color(0xFFE4ECFA)), center = Offset(sx + p[0] * kk, by + (p[1] - p[2] * 0.5f) * kk), radius = p[2] * kk), p[2] * kk, Offset(sx + p[0] * kk, by + p[1] * kk))
                }
                for (i in 0 until if (small) 2 else 5) {
                    val ph = (t * 0.5f + i / 5f) % 1f; val a = sin(PIf * ph); val an = PIf + (0.12f + i * 0.19f) * PIf
                    star4(cx + cos(an) * (rxO + 3f), cy + sin(an) * (ryO + 3f), 2f * a * boost, if (i % 2 == 1) Color.White.copy(alpha = a) else Gold.copy(alpha = a))
                }
            }
        }
        "codigo" -> {
            // chuva de código (TI, LENDÁRIA): atrás dele, colunas de símbolos verdes caindo (a cabeça de cada coluna
            // brilha quase branca, o rastro vai apagando e os símbolos trocam sozinhos) sobre um brilho verde; na
            // frente, "</>" e "{ }" flutuando dos lados
            val gs = listOf(
                listOf(floatArrayOf(0f, -1.2f, 0f, 1.2f)), listOf(floatArrayOf(-0.7f, -1.2f, 0.7f, -1.2f, 0.7f, 1.2f, -0.7f, 1.2f, -0.7f, -1.2f)),
                listOf(floatArrayOf(0.7f, -1.2f, -0.7f, 0f, 0.7f, 1.2f)), listOf(floatArrayOf(-0.7f, -1.2f, 0.7f, 0f, -0.7f, 1.2f)), listOf(floatArrayOf(0.6f, -1.2f, -0.6f, 1.2f)),
                listOf(floatArrayOf(-0.7f, -0.45f, 0.7f, -0.45f), floatArrayOf(-0.7f, 0.45f, 0.7f, 0.45f)), listOf(floatArrayOf(0.5f, -1.2f, 0f, -1.2f, 0f, -0.2f, -0.5f, 0f, 0f, 0.2f, 0f, 1.2f, 0.5f, 1.2f)),
                listOf(floatArrayOf(-0.7f, -1.2f, 0.7f, -1.2f, -0.2f, 1.2f)), listOf(floatArrayOf(-0.4f, -1.2f, 0f, -1.2f, 0f, 1.2f), floatArrayOf(-0.6f, 1.2f, 0.6f, 1.2f)),
            )
            fun drawG(gl: List<FloatArray>, x: Float, y: Float, s: Float, c: Color, w: Float, mx: Float = 1f) {
                for (pl in gl) { val p = Path(); var i = 0; while (i < pl.size) { val px = x + pl[i] * s * mx; val py = y + pl[i + 1] * s; if (i == 0) p.moveTo(px, py) else p.lineTo(px, py); i += 2 }; drawPath(p, c, style = stroke(w)) }
            }
            if (behind) {
                // brilho verde forte em volta (pulsa), como o clarão da chuva de moedas
                val pulse = 0.8f + 0.2f * sin(t * 2.2f)
                drawCircle(Brush.radialGradient(0f to Color(0xFF3CE68C).copy(alpha = (0.4f * pulse * boost).coerceIn(0f, 1f)), 0.6f to Color(0xFF1EC86E).copy(alpha = 0.18f * pulse), 1f to Color(0x001EC86E), center = Offset(50f, 52f), radius = 66f), 66f, Offset(50f, 52f))
                // colunas: mais densas nas laterais e por cima do livro; cada símbolo com contorno escuro para aparecer
                val xs = if (small) listOf(-4f, 4f, 12f, 50f, 88f, 96f, 104f) else listOf(-8f, -2f, 4f, 10f, 16f, 26f, 38f, 50f, 62f, 74f, 84f, 90f, 96f, 102f, 108f)
                val s = if (small) 1.9f else 1.5f; val len = if (small) 7 else 12
                xs.forEachIndexed { c, x ->
                    val sp = 0.24f + ((c * 7) % 5) * 0.05f; val head = ((t * sp + c * 0.37f) % 1f) * 160f - 28f
                    for (j in 0 until len) {
                        val y = head - j * 4.6f; if (y < -8f || y > 98f) continue
                        val a = if (j == 0) 1f else maxOf(0.12f, 1f - j.toFloat() / len); val gi = (c * 7 + j * 3 + floor(t * (if (j == 0) 8f else 2f) + c).toInt()) % gs.size
                        if (j == 0) drawCircle(Brush.radialGradient(listOf(Color(0xD9AAFFCD), Color(0x008CFFBE)), center = Offset(x, y), radius = 4.4f), 4.4f, Offset(x, y))
                        drawG(gs[gi], x, y, s, Color(0xFF052D19).copy(alpha = 0.55f * a), if (small) 1.6f else 1.2f)
                        drawG(gs[gi], x, y, s, if (j == 0) Color(0xFFF2FFF7) else if (j < 3) Color(0xFF78FFAF).copy(alpha = a) else Color(0xFF28E178).copy(alpha = a), if (small) 0.9f else 0.65f)
                    }
                }
            } else if (!small) for (i in 0 until 4) {
                val ph = (t * 0.2f + i / 4f) % 1f; val sd = if (i % 2 == 1) 1f else -1f; val x = 50f + sd * (42f + ((i * 5) % 7)); val y = 92f - ph * 80f; val a = sin(PIf * ph); val k = 1.1f * boost
                val parts = if (i % 2 == 0) listOf(Triple(gs[2], -2.3f, 1f), Triple(gs[4], 0f, 1f), Triple(gs[3], 2.3f, 1f)) else listOf(Triple(gs[6], -1.6f, 1f), Triple(gs[6], 1.6f, -1f))
                for ((w, c) in listOf(1.2f to Color(0xFF0A3C23).copy(alpha = 0.5f * a), 0.55f to Color(0xFF5AEB9B).copy(alpha = a))) for ((gl, dx, mx) in parts) drawG(gl, x + dx * k, y, k, c, w, mx)
            }
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
    // luvas RGB: o degradê vai girando de cor devagar
    val hue = (System.currentTimeMillis() % 12000L) / 1000f * 30f
    val fill: Brush = when (kind) {
        "arcoiris" -> Brush.linearGradient(listOf(Color(0xFFFF8FA3), Color(0xFFFFD84A), Color(0xFF7EE0B8), Color(0xFF6FB0FF), Color(0xFFB995F5)), start = Offset(-1f, -3f), end = Offset(5f, 3f))
        "rgb" -> Brush.linearGradient((0..2).map { Color.hsl((hue + it * 60f) % 360f, 0.95f, 0.62f) }, start = Offset(-1f, -3f), end = Offset(5f, 3f))
        "procedimento", "couro-marrom" -> Brush.verticalGradient(listOf(lerpColor(glove[0], Color.White, 0.35f), glove[0]), startY = -3f, endY = 3f)
        else -> androidx.compose.ui.graphics.SolidColor(glove[0])
    }
    val cuffC = if (kind == "rgb") Color.hsl((hue + 180f) % 360f, 0.8f, 0.45f) else glove[1]
    drawOval(cuffC, Offset(-2.8f, -2.5f), Size(3.2f, 5f)); drawOval(Arm, Offset(-2.8f, -2.5f), Size(3.2f, 5f), style = Stroke(ow))
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
    if (!small) drawGloveDetail(shape, kind, Arm)
}

/** Detalhes das luvas especiais, por cima da mão. */
private fun DrawScope.drawGloveDetail(shape: Hand, kind: String, out: Color) {
    val closed = shape != Hand.OPEN && shape != Hand.POINT
    when (kind) {
        "taticas", "operacional" -> {
            // protetor rígido no nó dos dedos (de borracha, com dois gomos) e o velcro no punho
            if (shape != Hand.OPEN) {
                val kp = roundRect(2.6f, -2.2f, 1.6f, 4.4f, 0.8f)
                drawPath(kp, vg(listOf(Color(0xFF5A5E66), Color(0xFF24262C)), -2.2f, 2.2f)); drawPath(kp, out.copy(alpha = 0.8f), style = stroke(0.3f))
                line(Color.White.copy(alpha = 0.25f), 3.4f, -1.6f, 3.4f, 1.6f, 0.25f); line(Color.Black.copy(alpha = 0.4f), 2.7f, 0f, 4.1f, 0f, 0.25f, false)
            }
            line(Color.Black.copy(alpha = 0.35f), -2f, -1.6f, -2f, 1.6f, 0.5f, false)
        }
        "procedimento" -> {
            // látex de procedimento: brilho comprido e a borda enrolada do punho
            if (closed) drawPath(Path().apply { moveTo(0.4f, -2.2f); quadraticTo(2.6f, -2.8f, 4f, -0.8f) }, Color.White.copy(alpha = 0.75f), style = stroke(0.35f))
            line(Color.White.copy(alpha = 0.6f), -2.5f, -1.8f, -2.5f, 1.8f, 0.4f)
        }
        "gala" -> {
            // luva de gala: três pespontos no dorso e o botãozinho de pérola no punho
            if (closed) for (y in listOf(-0.9f, 0f, 0.9f)) line(Color(0x80968CBE), 0.6f, y * 1.1f, 2.4f, y, 0.2f)
            drawCircle(Color(0xFFE8E2F4), 0.55f, Offset(-1.9f, 1.5f)); drawCircle(Color.White, 0.22f, Offset(-2.05f, 1.35f))
        }
        "couro-marrom" -> if (closed) {
            // luva de couro: costura pontilhada clara contornando o dorso
            drawArc(Color(0xCCFFE2B4), -2.2f * 180f / PIf, 4.4f * 180f / PIf, false, Offset(1.7f - 2.3f, -2.1f), Size(4.6f, 4.2f),
                style = Stroke(0.18f, cap = StrokeCap.Round, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(0.45f, 0.35f))))
        }
        "giz" -> {
            // mãos de giz: pó branco grudado e uma nuvenzinha soltando
            for (q in listOf(floatArrayOf(2.6f, -1.4f, 0.35f), floatArrayOf(3.6f, 0.6f, 0.28f), floatArrayOf(1.2f, 1.6f, 0.3f), floatArrayOf(4.2f, -0.4f, 0.22f), floatArrayOf(0.4f, -1.8f, 0.25f))) drawCircle(Color(0x8CC8C4BA), q[2], Offset(q[0], q[1]))
            val k = (System.currentTimeMillis() % 1667L) / 1667f
            drawCircle(Color.White.copy(alpha = 0.5f * (1f - k)), 0.5f + k * 1.2f, Offset(4.6f + k * 1.5f, -1.5f - k * 2f))
        }
    }
}
