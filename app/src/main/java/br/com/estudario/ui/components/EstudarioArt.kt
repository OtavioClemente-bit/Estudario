package br.com.estudario.ui.components

import android.provider.Settings
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import br.com.estudario.ui.theme.EstudarioMotion
import br.com.estudario.ui.theme.estudarioColors

/**
 * A linguagem visual do Estudário.
 *
 * O app tem um símbolo: **a barra**. Ela já está no glifo da marca, uma pilha de barras onde as de
 * baixo estão preenchidas e a de cima não, o edital virando conhecimento de baixo para cima. Este
 * arquivo não inventa um segundo motivo concorrente; ele **estende** aquele.
 *
 * O vocabulário inteiro tem três palavras, e não mais que três:
 *
 * - **Módulo** ([artModule]), a barra da marca, agora extrudada. Um item do edital: uma matéria,
 *   um tópico, uma tarefa. Vista de frente ela é o glifo; vista com profundidade ela é um objeto
 *   que pode ser movido, empilhado e encaixado.
 * - **Trilha** ([artTrack]), o percurso em que os módulos se encaixam. É a promessa do produto:
 *   o edital deixa de ser uma pilha e passa a ter ordem no tempo.
 * - **Nó** ([artNode]), um ponto decidido na trilha: onde você está, ou onde quer chegar.
 *
 * Nasceu para desenhar as cinco ilustrações de tela cheia do primeiro acesso, hoje isso é trabalho
 * de um render 3D gerado fora do app (ver `docs/arte-onboarding.md`); Compose achatado nunca ia
 * parecer profissional o suficiente para essa função, por mais elaborado que ficasse. O vocabulário
 * continua valendo para **elementos funcionais pequenos**: barra de cobertura, indicador de
 * progresso, o painel de espera do onboarding (`OnboardingPlaceholderArt`) enquanto a arte final não
 * chega. É isso que faz esses elementos parecerem uma família em vez de vários desenhos soltos:
 * **mesma perspectiva, mesma luz, mesma espessura, mesma geometria**.
 *
 * ## A perspectiva
 *
 * Isometria muito suave, não 3D. Cada módulo é uma laje: uma face frontal, uma face extrudada para
 * cima e para a direita (que recua em direção ao fundo), uma aresta iluminada no alto e uma sombra
 * curta embaixo. A luz vem sempre do alto à esquerda, em todas as cenas, sem exceção, é a regra
 * que mais rápido denuncia uma ilustração fora da família quando quebrada.
 *
 * ## As cores
 *
 * Nada de gradiente e nada de cor fora do tema. A profundidade não vem de escurecer para o preto
 * (que some no modo escuro) e sim de **misturar com a cor da superfície**: a face extrudada sempre
 * recua em direção ao fundo, e isso funciona igual no claro e no escuro.
 */
data class EstudarioArtPalette(
    /** Traço e sombra. */
    val ink: Color,
    /** A cor da marca: o que está decidido, o que está vivo. */
    val brand: Color,
    /** O que já foi dominado. */
    val done: Color,
    /** O que ainda não foi tocado. */
    val pending: Color,
    /** Atenção, usada com parcimônia, nunca como terceira cor decorativa. */
    val accent: Color,
    /** O fundo. É contra ela que a profundidade é construída. */
    val surface: Color,
    val dark: Boolean,
) {
    /** A face que recua: sempre em direção ao fundo, nunca em direção ao preto. */
    fun receding(color: Color): Color = lerp(color, surface, 0.45f)

    /** A aresta que recebe luz. Discreta, se aparecer sozinha, está forte demais. */
    fun lit(color: Color): Color = lerp(color, Color.White, if (dark) 0.34f else 0.28f)

    /** A sombra projetada. Curta e macia: os objetos estão pousados, não flutuando. */
    val shadow: Color get() = ink.copy(alpha = if (dark) 0.34f else 0.10f)
}

@Composable
fun rememberArtPalette(): EstudarioArtPalette {
    val dark = isSystemInDarkTheme()
    val extended = estudarioColors()
    return EstudarioArtPalette(
        ink = MaterialTheme.colorScheme.onSurface,
        brand = MaterialTheme.colorScheme.primary,
        done = extended.completed,
        pending = extended.upcoming,
        accent = extended.attention,
        surface = MaterialTheme.colorScheme.surface,
        dark = dark,
    )
}

object EstudarioArt {

    /** Proporção de toda ilustração do app. Editorial, larga, sem esmagar o texto abaixo. */
    const val ASPECT: Float = 16f / 10f

    /**
     * Duração do assentamento das ilustrações.
     *
     * É a única exceção documentada ao teto de [EstudarioMotion.Emphasized]: uma ilustração conta
     * uma transformação (a pilha virando caminho) e precisa ser legível. Roda uma vez por página,
     * nunca em resposta a toque, e é totalmente suprimida com redução de movimento ligada.
     */
    const val SETTLE_MS: Int = 640

    /** Extrusão da laje, como fração da altura do módulo. Constante em todas as cenas. */
    const val DEPTH_RATIO: Float = 0.34f

    /** Espessura da trilha, como fração da altura da tela de desenho. */
    const val TRACK_RATIO: Float = 0.018f

    /**
     * A curva da jornada.
     *
     * É literalmente a mesma função na abertura e na evolução, a trilha que a pessoa vê na
     * primeira tela é a trilha que ela vê se preenchendo na última. Essa repetição é o que
     * transforma quatro telas em uma história só.
     *
     * Muda apenas o trecho de largura que ela ocupa: na abertura divide a cena com a nuvem de
     * conteúdo desordenado, na evolução é a cena inteira.
     *
     * @param u posição no percurso, de 0 (início) a 1 (chegada).
     */
    fun journey(
        u: Float,
        width: Float,
        height: Float,
        startX: Float = 0.44f,
        endX: Float = 0.86f,
    ): Offset {
        val t = u.coerceIn(0f, 1f)
        // Suavização em S: sai devagar, ganha inclinação no meio, assenta na chegada.
        val eased = t * t * (3f - 2f * t)
        return Offset(
            x = width * (startX + (endX - startX) * t),
            y = height * (0.78f - 0.54f * eased),
        )
    }
}

/**
 * Um módulo: a barra da marca com profundidade.
 *
 * @param ghost desenha só o contorno, o item que existe no edital mas ainda não foi tocado.
 */
fun DrawScope.artModule(
    topLeft: Offset,
    size: Size,
    color: Color,
    palette: EstudarioArtPalette,
    ghost: Boolean = false,
    alpha: Float = 1f,
) {
    if (alpha <= 0.01f || size.width <= 0f || size.height <= 0f) return
    val radius = CornerRadius(size.height / 2f, size.height / 2f)

    if (ghost) {
        drawRoundRect(
            color = color.copy(alpha = 0.30f * alpha),
            topLeft = topLeft,
            size = size,
            cornerRadius = radius,
            style = Stroke(width = size.height * 0.16f),
        )
        return
    }

    val depth = size.height * EstudarioArt.DEPTH_RATIO

    // Sombra em duas camadas: uma larga e macia (contato geral com a superfície) e uma mais justa
    // e um pouco mais forte logo sob o objeto (peso). É o que separa "laje colada no fundo" de
    // "laje pousada", deslocadas para baixo e para a direita, já que a luz vem do alto à esquerda.
    val farShadowAlpha = (if (palette.dark) 0.22f else 0.065f) * alpha
    val nearShadowAlpha = (if (palette.dark) 0.36f else 0.13f) * alpha
    drawRoundRect(
        color = palette.ink.copy(alpha = farShadowAlpha),
        topLeft = Offset(topLeft.x + depth * 0.62f, topLeft.y + depth * 1.05f),
        size = size,
        cornerRadius = radius,
    )
    drawRoundRect(
        color = palette.ink.copy(alpha = nearShadowAlpha),
        topLeft = Offset(topLeft.x + depth * 0.40f, topLeft.y + depth * 0.70f),
        size = Size(size.width * 0.94f, size.height * 0.94f),
        cornerRadius = radius,
    )
    // Face extrudada: sobe e vai para a direita, recuando em direção ao fundo.
    drawRoundRect(
        color = palette.receding(color).copy(alpha = alpha),
        topLeft = Offset(topLeft.x + depth, topLeft.y - depth),
        size = size,
        cornerRadius = radius,
    )
    // Face frontal.
    drawRoundRect(
        color = color.copy(alpha = alpha),
        topLeft = topLeft,
        size = size,
        cornerRadius = radius,
    )
    // Aresta iluminada: um fio no alto da face frontal. É o que dá matéria ao objeto.
    val insetX = size.height * 0.38f
    if (size.width > insetX * 2f) {
        drawRoundRect(
            color = palette.lit(color).copy(alpha = 0.55f * alpha),
            topLeft = Offset(topLeft.x + insetX, topLeft.y + size.height * 0.20f),
            size = Size(size.width - insetX * 2f, size.height * 0.15f),
            cornerRadius = CornerRadius(size.height * 0.08f, size.height * 0.08f),
        )
    }
}

/**
 * A trilha: o percurso que dá ordem aos módulos.
 *
 * Desenhada como uma curva suave entre pontos, sempre com as mesmas pontas arredondadas. A
 * espessura é a mesma em todas as cenas, trilha fina numa tela e grossa noutra quebraria a
 * família tão rápido quanto trocar a paleta.
 */
fun DrawScope.artTrack(
    points: List<Offset>,
    color: Color,
    thickness: Float,
    alpha: Float = 1f,
    /** Tracejada: o vocabulário de "fora do seu controle direto", usado só no leque de "sua IA". */
    dashed: Boolean = false,
) {
    if (points.size < 2 || alpha <= 0.01f) return
    val path = Path().apply {
        moveTo(points.first().x, points.first().y)
        points.zipWithNext { from, to ->
            // Conector cúbico com controles no meio do caminho: curva macia, sem cotovelo.
            val midX = (from.x + to.x) / 2f
            cubicTo(midX, from.y, midX, to.y, to.x, to.y)
        }
    }
    drawPath(
        path = path,
        color = color.copy(alpha = alpha),
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            pathEffect = if (dashed) {
                PathEffect.dashPathEffect(floatArrayOf(thickness * 1.1f, thickness * 3.4f))
            } else {
                null
            },
        ),
    )
}

/**
 * Um raio reto, não curvo.
 *
 * [artTrack] desenha uma curva em S pensada para jornadas longas e diagonais; num conector curto e
 * majoritariamente vertical essa mesma curva produz um gancho feio perto da ponta (a tangente nos
 * dois extremos é sempre horizontal). O raio existe só para conectores curtos, como o pequeno leque
 * de "sua IA": às vezes tracejado, para marcar o que ainda é externo ao plano.
 */
fun DrawScope.artSpoke(
    from: Offset,
    to: Offset,
    color: Color,
    thickness: Float,
    alpha: Float = 1f,
    dashed: Boolean = false,
) {
    if (alpha <= 0.01f) return
    drawLine(
        color = color.copy(alpha = alpha),
        start = from,
        end = to,
        strokeWidth = thickness,
        cap = StrokeCap.Round,
        pathEffect = if (dashed) {
            PathEffect.dashPathEffect(floatArrayOf(thickness * 1.3f, thickness * 4.2f))
        } else {
            null
        },
    )
}

/**
 * Textura de fundo: uma grade esparsa de pontos, como papel de prancheta técnica.
 *
 * Sempre atrás de tudo, sempre com a mesma densidade, é o que separa uma tela "vazia" de uma
 * tela "desenhada com intenção", sem competir com os módulos. Baixíssimo contraste de propósito.
 */
fun DrawScope.artDotField(palette: EstudarioArtPalette, alpha: Float = 0.05f) {
    val cols = 15
    val rows = 10
    val radius = size.height * 0.0028f
    for (col in 1 until cols) {
        for (row in 1 until rows) {
            drawCircle(
                color = palette.ink.copy(alpha = alpha),
                radius = radius,
                center = Offset(size.width * col / cols, size.height * row / rows),
            )
        }
    }
}

/**
 * Marcações curtas de régua, perpendiculares a um eixo, o mesmo ritmo usado no plano, reaproveitado
 * onde marcar um eixo com pontos discretos ajuda a leitura (progresso, cronograma).
 */
fun DrawScope.artTicks(points: List<Offset>, length: Float, palette: EstudarioArtPalette, alpha: Float = 0.45f) {
    points.forEach { point ->
        drawLine(
            color = palette.pending.copy(alpha = alpha),
            start = point,
            end = Offset(point.x, point.y + length),
            strokeWidth = size.height * 0.009f,
            cap = StrokeCap.Round,
        )
    }
}

/**
 * Um nó: um ponto decidido na trilha.
 *
 * @param halo anéis concêntricos que marcam o destino ou o ponto vivo. Usado uma vez por cena, no
 *   máximo, é o elemento de maior peso da composição e perde força se repetido.
 */
fun DrawScope.artNode(
    center: Offset,
    radius: Float,
    color: Color,
    palette: EstudarioArtPalette,
    halo: Boolean = false,
    filled: Boolean = true,
    alpha: Float = 1f,
) {
    if (alpha <= 0.01f) return
    if (halo) {
        drawCircle(color.copy(alpha = 0.10f * alpha), radius * 2.6f, center)
        drawCircle(color.copy(alpha = 0.18f * alpha), radius * 1.8f, center, style = Stroke(width = radius * 0.30f))
    }
    if (filled) {
        drawCircle(palette.shadow.copy(alpha = palette.shadow.alpha * alpha), radius, center.copy(y = center.y + radius * 0.34f))
        drawCircle(color.copy(alpha = alpha), radius, center)
        drawCircle(palette.lit(color).copy(alpha = 0.45f * alpha), radius * 0.42f, Offset(center.x - radius * 0.28f, center.y - radius * 0.30f))
    } else {
        drawCircle(color.copy(alpha = 0.45f * alpha), radius, center, style = Stroke(width = radius * 0.42f))
    }
}

/**
 * O progresso de assentamento de uma ilustração, de 0 a 1.
 *
 * Roda uma vez quando a cena aparece. Com redução de movimento ativada no sistema, devolve 1
 * imediatamente: a arte nasce montada, sem animação nenhuma.
 */
@Composable
fun rememberArtProgress(key: Any? = Unit, play: Boolean = true): Float {
    if (rememberReducedMotion()) return 1f
    var started by remember(key) { mutableStateOf(false) }
    LaunchedEffect(key, play) { if (play) started = true }
    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(EstudarioArt.SETTLE_MS, easing = EstudarioMotion.Settle),
        label = "art-settle",
    )
    return progress
}

/**
 * Se o sistema pede movimento reduzido.
 *
 * Lê a escala de duração de animação do Android, zero significa que a pessoa desligou animações
 * nas opções de acessibilidade ou de desenvolvedor. Falhar a leitura nunca bloqueia a tela.
 */
@Composable
fun rememberReducedMotion(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        runCatching {
            Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
        }.getOrDefault(false)
    }
}

/** Interpolação de posição, escrita à mão para não depender de sobrecarga de `lerp` importada. */
internal fun lerpOffset(from: Offset, to: Offset, t: Float): Offset {
    val f = t.coerceIn(0f, 1f)
    return Offset(from.x + (to.x - from.x) * f, from.y + (to.y - from.y) * f)
}

/**
 * Entrada escalonada: cada elemento tem sua janela dentro do progresso total da cena.
 *
 * Com [window] = 0,6 e cinco elementos, o primeiro anda de 0 a 0,6 e o último de 0,4 a 1,0, eles
 * se sobrepõem bastante, que é o que faz o conjunto parecer assentar junto em vez de entrar em
 * fila indiana.
 */
internal fun stagger(progress: Float, index: Int, count: Int, window: Float = 0.6f): Float {
    if (count <= 1) return progress.coerceIn(0f, 1f)
    val step = (1f - window) / (count - 1)
    val start = index * step
    return ((progress - start) / window).coerceIn(0f, 1f)
}

/** Suavização padrão das entradas: sai rápido, assenta devagar. */
internal fun settle(t: Float): Float {
    val x = t.coerceIn(0f, 1f)
    return 1f - (1f - x) * (1f - x) * (1f - x)
}
