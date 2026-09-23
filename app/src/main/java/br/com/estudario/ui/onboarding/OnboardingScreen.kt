package br.com.estudario.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import br.com.estudario.ui.components.EstudarioArt
import br.com.estudario.ui.components.EstudarioWordmark
import br.com.estudario.ui.components.rememberArtProgress
import br.com.estudario.ui.theme.EstudarioMotion
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.theme.EstudarioTheme
import kotlinx.coroutines.launch

/**
 * A primeira impressão do Estudário: cinco páginas apresentam o produto pela experiência que ele
 * oferece, do edital à evolução, sem recorrer a uma lista genérica de recursos.
 *
 * Aparece uma única vez, na primeira instalação. Quem conhece o app pula em um toque.
 */
private data class OnboardingPage(
    val eyebrow: String?,
    val title: String,
    val body: String,
    val art: OnboardingArt,
)

/**
 * As cinco páginas contam uma história em sequência. Primeiro, o conteúdo se transforma em um
 * caminho de estudo. Depois, esse caminho ganha estrutura, ritmo, apoio da IA e indicadores de
 * evolução. Cada texto complementa a ilustração correspondente, sem repetir sua mensagem.
 */
private fun onboardingPages(): List<OnboardingPage> = listOf(
    OnboardingPage(
        eyebrow = null,
        title = "Transforme seu edital em um\ncaminho de estudo.",
        body = "Organize sua preparação a partir de um edital completo ou apenas das matérias essenciais. Acompanhe sua evolução com clareza até o dia da prova.",
        art = OnboardingArt.WELCOME,
    ),
    OnboardingPage(
        eyebrow = "Conteúdo",
        title = "Comece pelo edital ou pelas\nmatérias essenciais.",
        body = "Importe o edital completo ou selecione apenas as matérias que fazem parte da sua preparação. Tópicos e subtópicos organizam o conteúdo, enquanto o conhecimento prévio já fica registrado desde o início.",
        art = OnboardingArt.SYLLABUS,
    ),
    OnboardingPage(
        eyebrow = "Plano",
        title = "Um plano claro para cada\nsessão de estudo.",
        body = "O plano define a matéria, o tópico, a atividade e a duração de cada sessão. Assim, você pode começar a estudar sem perder tempo reorganizando a rotina.",
        art = OnboardingArt.PLAN,
    ),
    OnboardingPage(
        eyebrow = "Sua IA",
        title = "A IA que você já usa,\nintegrada ao seu plano.",
        body = "O Estudário prepara a solicitação com seu conteúdo, suas prioridades e seu ritmo. Você escolhe a ferramenta de IA, e o resultado retorna organizado no seu plano.",
        art = OnboardingArt.COMPANION,
    ),
    OnboardingPage(
        eyebrow = "Evolução",
        title = "Uma evolução que pode\nser comprovada.",
        body = "Cobertura, domínio, desempenho e revisões formam uma visão objetiva do seu avanço. Você entende o que já consolidou e onde deve concentrar seus próximos estudos.",
        art = OnboardingArt.PROGRESS,
    ),
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = remember { onboardingPages() }
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == pages.lastIndex

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { index ->
                // A ilustração só assenta quando a página é a que está sendo vista: entrar numa
                // página deve parecer que o Estudário montou aquilo ali, na frente da pessoa.
                OnboardingPageContent(
                    page = pages[index],
                    showWordmark = index == 0,
                    active = pagerState.currentPage == index,
                    index = index,
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = EstudarioSpacing.screenGutter, vertical = EstudarioSpacing.large),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PageIndicator(count = pages.size, current = pagerState.currentPage)
                Spacer(Modifier.weight(1f))
                if (!isLast) {
                    TextButton(onClick = onFinish) {
                        Text("Pular", style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(Modifier.width(EstudarioSpacing.tight))
                }
                Button(
                    onClick = {
                        if (isLast) onFinish() else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Text(if (isLast) "Começar" else "Próximo", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

/**
 * A página como unidade editorial.
 *
 * Ilustração, título, texto e navegação dividem a mesma margem e a mesma coluna, a arte não é um
 * banner solto acima do conteúdo, ela é o primeiro parágrafo da página. A proporção 16:10 mantém
 * a imagem por volta de um quarto da altura da tela, em vez de esmagar o texto.
 *
 * O bloco inteiro fica um pouco acima do meio geométrico (pesos 1 e 1,25): é onde o olho espera
 * encontrar o centro de uma composição, e é o que separa um layout desenhado de um centralizado.
 */
@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    showWordmark: Boolean,
    active: Boolean,
    index: Int,
    /**
     * Força o estado da ilustração. Existe para os previews do Android Studio, que renderizam um
     * único quadro: sem isto, a prévia mostraria a arte no meio do assentamento, ou vazia.
     */
    artProgress: Float? = null,
) {
    val animated = rememberArtProgress(key = index, play = active)
    val progress = artProgress ?: animated
    // Em tela baixa ou com fonte grande, título + texto + arte não cabiam e o fim do texto era
    // cortado. Agora a página rola quando precisa (heightIn(min) mantém a composição centralizada
    // quando sobra espaço) e a arte nunca passa de ~36% da altura disponível.
    BoxWithConstraints(Modifier.fillMaxSize()) {
    val artMaxHeight = maxHeight * 0.36f
    val compactSpacing = maxHeight < 560.dp
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .heightIn(min = maxHeight)
            .padding(horizontal = EstudarioSpacing.screenGutter),
    ) {
        Spacer(Modifier.weight(1f))

        // A ilustração explica; o texto conclui. Por isso ela vem antes, e é decorativa para o
        // leitor de tela, quem usa TalkBack recebe a mesma informação pelo título e pelo corpo.
        Box(Modifier.fillMaxWidth().clearAndSetSemantics { }, contentAlignment = Alignment.Center) {
            OnboardingArtSlot(page.art, Modifier.widthIn(max = artMaxHeight * EstudarioArt.ASPECT), progress = progress)
        }

        Spacer(Modifier.height(if (compactSpacing) EstudarioSpacing.large else EstudarioSpacing.section))

        if (showWordmark) {
            EstudarioWordmark()
            Spacer(Modifier.height(EstudarioSpacing.comfortable))
        } else if (page.eyebrow != null) {
            Text(
                page.eyebrow.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(EstudarioSpacing.small))
        }

        Text(
            page.title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(EstudarioSpacing.small))
        Text(
            page.body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.weight(1.25f))
    }
    }
}

/**
 * O indicador é o mesmo motivo da marca: barras, não bolinhas. A página atual é a barra larga e
 * cheia; as outras são traços curtos e discretos.
 */
@Composable
private fun PageIndicator(count: Int, current: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.hairline), verticalAlignment = Alignment.CenterVertically) {
        repeat(count) { index ->
            val active = index == current
            val width by animateDpAsState(if (active) 22.dp else 8.dp, EstudarioMotion.quick(), label = "indicator-width")
            val color by animateColorAsState(
                if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                EstudarioMotion.quick(),
                label = "indicator-color",
            )
            Box(
                Modifier
                    .width(width)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color),
            )
        }
    }
}

// ---------------------------------------------------------------- previews

@Preview(name = "Onboarding, abertura", showBackground = true, heightDp = 760)
@Composable
private fun OnboardingFirstPagePreview() {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            OnboardingPageContent(onboardingPages().first(), showWordmark = true, active = true, index = 0, artProgress = 1f)
        }
    }
}

@Preview(name = "Onboarding, cobertura", showBackground = true, heightDp = 760)
@Composable
private fun OnboardingMiddlePagePreview() {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            OnboardingPageContent(onboardingPages()[1], showWordmark = false, active = true, index = 1, artProgress = 1f)
        }
    }
}

@Preview(name = "Onboarding, sua IA", showBackground = true, heightDp = 760)
@Composable
private fun OnboardingCompanionPagePreview() {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            OnboardingPageContent(onboardingPages()[3], showWordmark = false, active = true, index = 3, artProgress = 1f)
        }
    }
}

@Preview(name = "Onboarding, última página", showBackground = true, heightDp = 760)
@Composable
private fun OnboardingLastPagePreview() {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            OnboardingPageContent(onboardingPages().last(), showWordmark = false, active = true, index = 4, artProgress = 1f)
        }
    }
}

@Preview(name = "Onboarding, direção (escuro)", showBackground = true, heightDp = 760, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnboardingDarkPagePreview() {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            OnboardingPageContent(onboardingPages()[2], showWordmark = false, active = true, index = 2, artProgress = 1f)
        }
    }
}

@Preview(name = "Onboarding, indicador", showBackground = true, widthDp = 220, heightDp = 60)
@Composable
private fun OnboardingIndicatorPreview() {
    EstudarioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(Modifier.padding(EstudarioSpacing.medium)) { PageIndicator(count = 5, current = 1) }
        }
    }
}
