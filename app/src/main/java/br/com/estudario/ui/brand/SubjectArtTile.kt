package br.com.estudario.ui.brand

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** O personagem da turma do Folha para o desenho escolhido em SubjectArt (mesmos nomes do site). */
fun subjectArtGlyph(key: String): Glyph = when (key) {
    "art-idioma" -> Glyph.Idioma
    "art-pena" -> Glyph.Pena
    "art-calculadora" -> Glyph.Calculadora
    "art-laptop" -> Glyph.Laptop
    "art-colunas" -> Glyph.Colunas
    "art-martelo" -> Glyph.Martelo
    "art-moedas" -> Glyph.Moedas
    "art-maleta" -> Glyph.Maleta
    "art-pergaminho" -> Glyph.Pergaminho
    "art-balanca" -> Glyph.Balanca
    "art-globo" -> Glyph.Globo
    "art-kitmedico" -> Glyph.KitMedico
    "art-frasco" -> Glyph.Frasco
    "art-barco" -> Glyph.Barco
    "art-lupa" -> Glyph.Lupa
    "art-recibo" -> Glyph.Recibo
    "art-urna" -> Glyph.Urna
    "art-cofrinho" -> Glyph.Cofrinho
    "trending_up" -> Glyph.Chart
    "folder_open" -> Glyph.Folder
    "school" -> Glyph.Cap
    "shield" -> Glyph.Shield
    else -> Glyph.Books
}

/**
 * O selo de uma matéria ou de um concurso: o personagem do assunto (desenhado no traço da turma do
 * Folha) sobre um fundo suave na cor dele. Substitui a letra/sigla solta, que parecia genérica.
 */
@Composable
fun SubjectArtTile(key: String, color: Color, size: Dp = 40.dp, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(size * 0.32f)
    Box(
        modifier
            .size(size)
            .clip(shape)
            .background(Brush.linearGradient(listOf(color.copy(alpha = 0.22f), color.copy(alpha = 0.08f))))
            .border(1.5.dp, color.copy(alpha = 0.30f), shape),
        contentAlignment = Alignment.Center,
    ) {
        BrandIcon(subjectArtGlyph(key), size = size * 0.86f)
    }
}
