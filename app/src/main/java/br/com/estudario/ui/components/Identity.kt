package br.com.estudario.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/** Índigo do fundo do ícone do app (`ic_launcher_background`), usado onde a marca aparece dentro da interface. */
val AppMarkBackground = Color(0xFF3326CE)

/**
 * A marca do app: o livro aberto com as páginas desenhadas e a fita verde, o mesmo desenho do ícone
 * na tela inicial do celular.
 * Aparece na comemoração da sequência e no perfil.
 */
@Composable
fun AppMark(size: Dp, modifier: Modifier = Modifier) = AppIconMark(size, modifier)

/**
 * Foto do perfil, com as iniciais como reserva. A imagem fica guardada dentro do app (o URI da
 * galeria não sobrevive ao reinício), então é carregada do arquivo fora da thread principal.
 */
@Composable
fun ProfileAvatar(
    photoPath: String?,
    initials: String,
    size: Dp,
    modifier: Modifier = Modifier,
    ring: Boolean = false,
) {
    PresetAvatar.of(photoPath)?.let { preset ->
        val ringModifier = if (!ring) Modifier else Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape).padding(3.dp)
        Box(modifier.size(size).then(ringModifier), contentAlignment = Alignment.Center) { PresetAvatarArt(preset, size) }
        return
    }
    val bitmap by produceState<ImageBitmap?>(null, photoPath) {
        value = photoPath?.let { path ->
            withContext(Dispatchers.IO) {
                runCatching { BitmapFactory.decodeFile(path)?.asImageBitmap() }.getOrNull()
            }
        }
    }
    val shape = CircleShape
    val ringModifier = if (!ring) Modifier else Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape).padding(3.dp)
    Box(
        modifier.size(size).then(ringModifier),
        contentAlignment = Alignment.Center,
    ) {
        val current = bitmap
        if (current != null) {
            Image(current, null, Modifier.fillMaxSize().clip(shape), contentScale = ContentScale.Crop)
        } else {
            Box(
                Modifier.fillMaxSize().clip(shape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    initials.ifBlank { "?" },
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size.value / 2.6f).sp,
                )
            }
        }
    }
}

/**
 * Avatares do Estudário para quem não quer usar foto. Ficam guardados como `avatar:<chave>` no
 * mesmo lugar da foto, então o resto do app (menu, topo, perfil) mostra sem saber a diferença.
 */
enum class PresetAvatar(val key: String, val label: String, val top: Color, val bottom: Color, val glyph: br.com.estudario.ui.brand.Glyph?) {
    FOLHA("folha", "Folha", Color(0xFF6C5CFF), Color(0xFF3326CE), null),
    CAPELO("capelo", "Formatura", Color(0xFF34D399), Color(0xFF0E8F63), br.com.estudario.ui.brand.Glyph.Cap),
    LAMPADA("lampada", "Ideia", Color(0xFFFFC857), Color(0xFFE08A00), br.com.estudario.ui.brand.Glyph.Bulb),
    ALVO("alvo", "Foco", Color(0xFFFF8A7A), Color(0xFFD9443A), br.com.estudario.ui.brand.Glyph.Target),
    TROFEU("trofeu", "Aprovação", Color(0xFF5CC8FF), Color(0xFF1C7FC4), br.com.estudario.ui.brand.Glyph.Trophy),
    CHAMA("chama", "Chama", Color(0xFFFFB347), Color(0xFFE8590C), br.com.estudario.ui.brand.Glyph.Fire),
    CEREBRO("cerebro", "Cérebro", Color(0xFFF9A8D4), Color(0xFFC2416C), br.com.estudario.ui.brand.Glyph.Brain),
    LIVROS("livros", "Livros", Color(0xFF5EEAD4), Color(0xFF0F766E), br.com.estudario.ui.brand.Glyph.Books),
    ESTRELA("estrela", "Estrela", Color(0xFFFDE68A), Color(0xFFD97706), br.com.estudario.ui.brand.Glyph.Star),
    DIAMANTE("diamante", "Diamante", Color(0xFFA5F3FC), Color(0xFF0E7490), br.com.estudario.ui.brand.Glyph.Gem),
    MEDALHA("medalha", "Medalha", Color(0xFFFCD34D), Color(0xFFB45309), br.com.estudario.ui.brand.Glyph.Medal),
    ESCUDO("escudo", "Escudo", Color(0xFF93C5FD), Color(0xFF1D4ED8), br.com.estudario.ui.brand.Glyph.Shield),
    CAFE("cafe", "Café", Color(0xFFD6A77A), Color(0xFF7C4A21), br.com.estudario.ui.brand.Glyph.Coffee),
    LUA("lua", "Coruja da noite", Color(0xFFA5B4FC), Color(0xFF3730A3), br.com.estudario.ui.brand.Glyph.Moon),
    CORACAO("coracao", "Coração", Color(0xFFFDA4AF), Color(0xFFBE123C), br.com.estudario.ui.brand.Glyph.Heart),
    DADO("dado", "Sorte", Color(0xFFC4B5FD), Color(0xFF6D28D9), br.com.estudario.ui.brand.Glyph.Dice);

    val path: String get() = "avatar:$key"

    companion object {
        fun of(path: String?): PresetAvatar? = path?.takeIf { it.startsWith("avatar:") }?.removePrefix("avatar:")?.let { k -> entries.firstOrNull { it.key == k } }
    }
}

@Composable
fun PresetAvatarArt(avatar: PresetAvatar, size: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier.size(size).clip(CircleShape)
            .background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(avatar.top, avatar.bottom))),
        contentAlignment = Alignment.Center,
    ) {
        if (avatar.glyph == null) br.com.estudario.ui.assistant.Folha(size * 0.86f)
        else br.com.estudario.ui.brand.BrandIcon(avatar.glyph, size = size * 0.62f)
    }
}
