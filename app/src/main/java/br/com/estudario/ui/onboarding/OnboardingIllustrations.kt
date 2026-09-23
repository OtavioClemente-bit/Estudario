package br.com.estudario.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.components.EstudarioArt
import br.com.estudario.ui.components.EstudarioArtPalette
import br.com.estudario.ui.components.EstudarioGlyph
import br.com.estudario.ui.components.artDotField
import br.com.estudario.ui.components.rememberArtPalette
import br.com.estudario.ui.components.rememberArtProgress

/**
 * As ilustrações do Estudário.
 *
 * ## O que isto é, e o que não é
 *
 * A arte final do primeiro acesso é um **render 3D profissional**, feito fora do app, nada em
 * Compose Canvas chega perto disso, e tentar simular 3D com formas vetoriais achatadas só produz
 * uma versão pior. Este arquivo não tenta mais fazer esse trabalho.
 *
 * O que ele faz: um **espaço de espera** honesto (o glifo da marca, discreto, sobre um painel
 * suave) enquanto a arte final não chega, e o mecanismo que troca um pelo outro sem tocar em
 * código. A direção completa, conceito, composição, e os prompts para gerar as cinco cenas, está
 * em `docs/arte-onboarding.md`.
 *
 * As cenas continuam representando os mesmos cinco momentos do produto (abertura, conteúdo,
 * plano, sua IA, evolução), mas isso agora vive só na direção de arte e na cópia da tela, não
 * numa ilustração gerada aqui.
 *
 * ## Por que não há texto nas imagens
 *
 * Todo o texto mora na UI. Isso mantém tradução, escala tipográfica e leitor de tela funcionando,
 * e permite trocar a arte sem retrabalhar conteúdo.
 *
 * ## Como trocar pela arte final
 *
 * Cada cena tem um nome de asset definitivo em [OnboardingArt]. Basta colocar um drawable com esse
 * nome em `res/drawable/` e ele assume no lugar do placeholder, **sem tocar em código**, e entra
 * com a mesma transição suave (assentamento de [EstudarioArt.SETTLE_MS]) que o placeholder já usa,
 * não estático. Não existe flag, build variant ou import para mudar: o slot procura o recurso em
 * tempo de execução e usa o que encontrar. Ver `docs/arte-onboarding.md` para especificação e
 * prompts de geração.
 */
enum class OnboardingArt(val assetName: String) {
    /** O que você precisa estudar virando um caminho controlável. */
    WELCOME("art_onboarding_welcome"),

    /** O conteúdo, de um edital completo às matérias que você escolheu, como estrutura viva. */
    SYLLABUS("art_onboarding_syllabus"),

    /** O conteúdo distribuído ao longo do tempo. */
    PLAN("art_onboarding_plan"),

    /** Sua própria IA, do seu jeito, voltando a fazer parte do plano. */
    COMPANION("art_onboarding_ai"),

    /** O caminho sendo preenchido. */
    PROGRESS("art_onboarding_progress"),

    /** Estado vazio: nenhum concurso ainda. */
    EMPTY_NO_EXAM("art_empty_no_exam"),
}

/**
 * O slot de ilustração.
 *
 * Procura o drawable definitivo; se não existir, mostra o espaço de espera. Os dois entram com a
 * mesma transição, trocar um pelo outro nunca é um salto visual.
 */
@Composable
fun OnboardingArtSlot(
    art: OnboardingArt,
    modifier: Modifier = Modifier,
    progress: Float = rememberArtProgress(art),
) {
    val context = LocalContext.current
    val assetId = remember(art, context) {
        runCatching {
            @Suppress("DiscouragedApi")
            context.resources.getIdentifier(art.assetName, "drawable", context.packageName)
        }.getOrDefault(0)
    }
    val palette = rememberArtPalette()

    Box(modifier.fillMaxWidth().aspectRatio(EstudarioArt.ASPECT)) {
        if (assetId != 0) {
            Image(
                painter = painterResource(assetId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(progress)
                    .scale(0.94f + 0.06f * progress),
                contentScale = ContentScale.Fit,
            )
        } else {
            OnboardingPlaceholderArt(palette, progress)
        }
    }
}

/**
 * O espaço de espera: só o glifo da marca sobre um painel suave, com a mesma grade de pontos usada
 * em outras telas do app. De propósito, ele não tenta contar a história de cada cena, isso é
 * trabalho da arte final (ver `docs/arte-onboarding.md`), não de uma forma vetorial tentando imitar
 * 3D. Um placeholder honesto vale mais que uma ilustração ruim.
 */
@Composable
private fun OnboardingPlaceholderArt(palette: EstudarioArtPalette, progress: Float) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            artDotField(palette, alpha = 0.06f)
            val insetX = size.width * 0.05f
            val insetY = size.height * 0.08f
            drawRoundRect(
                color = palette.brand.copy(alpha = if (palette.dark) 0.12f else 0.055f),
                topLeft = Offset(insetX, insetY),
                size = Size(size.width - insetX * 2f, size.height - insetY * 2f),
                cornerRadius = CornerRadius(size.height * 0.14f, size.height * 0.14f),
            )
        }
        EstudarioGlyph(
            size = 40.dp,
            modifier = Modifier
                .alpha(progress)
                .scale(0.85f + 0.15f * progress),
        )
    }
}

// ------------------------------------------------------------------ nomes públicos estáveis

@Composable
fun OnboardingWelcomeArt(modifier: Modifier = Modifier) =
    OnboardingArtSlot(OnboardingArt.WELCOME, modifier)

@Composable
fun OnboardingEditalArt(modifier: Modifier = Modifier) =
    OnboardingArtSlot(OnboardingArt.SYLLABUS, modifier)

@Composable
fun OnboardingPlanArt(modifier: Modifier = Modifier) =
    OnboardingArtSlot(OnboardingArt.PLAN, modifier)

@Composable
fun OnboardingCompanionArt(modifier: Modifier = Modifier) =
    OnboardingArtSlot(OnboardingArt.COMPANION, modifier)

@Composable
fun OnboardingProgressArt(modifier: Modifier = Modifier) =
    OnboardingArtSlot(OnboardingArt.PROGRESS, modifier)

@Composable
fun EmptyNoExamArt(modifier: Modifier = Modifier) =
    OnboardingArtSlot(OnboardingArt.EMPTY_NO_EXAM, modifier, progress = 1f)
