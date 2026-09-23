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
fun AppMark(size: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier.size(size).clip(CircleShape).background(AppMarkBackground),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            // O desenho do ícone tem margem de segurança; ampliar um pouco faz o livro preencher o círculo.
            modifier = Modifier.size(size * 1.3f),
        )
    }
}

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
