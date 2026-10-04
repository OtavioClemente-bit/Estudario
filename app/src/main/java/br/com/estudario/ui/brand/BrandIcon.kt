package br.com.estudario.ui.brand

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

/**
 * Substitui o `Icon` do Material no app inteiro. Os símbolos com significado (livro, calendário,
 * cronômetro, troféu, chama…) viram os desenhos do Estudário ([Glyph]); os de controle (voltar,
 * fechar, setas, mais opções) continuam traços simples, porque ali o ícone é ferramenta, não
 * personagem.
 *
 * O `tint` decide o estado: cinza explícito (texto secundário, desligado) deixa o desenho em cinza;
 * qualquer outra cor mantém as cores próprias do desenho.
 */
@Composable
fun Icon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
) {
    val glyph = glyphFor(imageVector.name)
    if (glyph == null) {
        androidx.compose.material3.Icon(imageVector, contentDescription, modifier, if (tint.isSpecified) tint else LocalContentColor.current)
        return
    }
    val scheme = androidx.compose.material3.MaterialTheme.colorScheme
    // Estrela, coração e marcador "vazios" são o estado desligado: cinza, para o toque ter resposta visível.
    val empty = imageVector.name.endsWith("Border")
    val muted = empty || tint.isSpecified && (
        tint.alpha < 0.6f ||
            listOf(scheme.outline, scheme.outlineVariant).any { it.copy(alpha = 1f) == tint.copy(alpha = 1f) }
        )
    val semantics = if (contentDescription != null) {
        Modifier.semantics { this.contentDescription = contentDescription; role = Role.Image }
    } else Modifier
    Canvas(modifier.then(semantics).size(24.dp)) { drawGlyph(glyph, muted) }
}

@Composable
fun Icon(painter: Painter, contentDescription: String?, modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) =
    androidx.compose.material3.Icon(painter, contentDescription, modifier, tint)

@Composable
fun Icon(bitmap: ImageBitmap, contentDescription: String?, modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) =
    androidx.compose.material3.Icon(bitmap, contentDescription, modifier, tint)

private fun isGrey(color: Color): Boolean {
    val hi = max(color.red, max(color.green, color.blue))
    val lo = min(color.red, min(color.green, color.blue))
    val saturation = if (hi == 0f) 0f else (hi - lo) / hi
    val l = color.luminance()
    return saturation < 0.18f && l > 0.12f && l < 0.8f
}

/** `ImageVector.name` do Material vem como "Outlined.CheckCircle" ou "AutoMirrored.Rounded.MenuBook". */
internal fun glyphFor(vectorName: String): Glyph? = when (vectorName.substringAfterLast('.')) {
    "Home" -> Glyph.House
    "LibraryBooks" -> Glyph.Books
    "MenuBook", "AutoStories", "Article", "Toc" -> Glyph.OpenBook
    "CalendarMonth", "Event", "EventAvailable", "EventRepeat", "EventBusy" -> Glyph.Calendar
    "Timer", "Schedule", "HourglassTop", "PendingActions" -> Glyph.Timer
    "Quiz", "FactCheck", "Checklist", "Assignment", "Rule" -> Glyph.Clipboard
    "Bookmarks" -> Glyph.Notebook
    "Bookmark", "BookmarkBorder" -> Glyph.Bookmark
    "School" -> Glyph.Cap
    "AutoAwesome" -> Glyph.Spark
    "Psychology" -> Glyph.Brain
    "CheckCircle", "TaskAlt" -> Glyph.Check
    "Flag", "OutlinedFlag" -> Glyph.Flag
    "Lock" -> Glyph.Lock
    "Star", "StarBorder" -> Glyph.Star
    "EmojiEvents" -> Glyph.Trophy
    "WorkspacePremium" -> Glyph.Medal
    "Lightbulb" -> Glyph.Bulb
    "WarningAmber", "Warning", "ErrorOutline", "ReportProblem" -> Glyph.Warning
    "LocalFireDepartment" -> Glyph.Fire
    "Bolt", "OfflineBolt", "Speed" -> Glyph.Bolt
    "Favorite", "FavoriteBorder" -> Glyph.Heart
    "Notifications", "NotificationsActive" -> Glyph.Bell
    "QueryStats", "Insights", "PieChart", "Timeline", "TrendingUp", "TrendingDown" -> Glyph.Chart
    "Autorenew", "Replay", "History", "Restore", "EventRepeatOutlined" -> Glyph.Cycle
    "GpsFixed", "CenterFocusStrong", "Explore" -> Glyph.Target
    "FolderOpen", "Inbox", "Archive", "Unarchive" -> Glyph.Folder
    "Settings", "Tune" -> Glyph.Gear
    "HelpOutline", "Info" -> Glyph.Help
    "Person", "AccountCircle" -> Glyph.Person
    "Shield", "VerifiedUser" -> Glyph.Shield
    "Style" -> Glyph.Cards
    "Shuffle" -> Glyph.Dice
    "PlayCircle", "PlayCircleOutline", "OndemandVideo" -> Glyph.Play
    "CloudSync", "CloudDownload", "CloudDone", "CloudUpload", "CloudOff" -> Glyph.Cloud
    "Description", "UploadFile", "FileOpen", "PictureAsPdf", "Summarize" -> Glyph.Doc
    "EditNote", "BorderColor" -> Glyph.Pencil
    "LightMode" -> Glyph.Sun
    "DarkMode", "Bedtime" -> Glyph.Moon
    "PhoneAndroid", "Devices" -> Glyph.Phone
    "FiberNew" -> Glyph.Gem
    "EmojiEventsOutlined" -> Glyph.Trophy
    else -> null
}
