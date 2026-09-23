package br.com.estudario.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Ritmo vertical do Estudário: uma escala única de espaçamento para toda a interface, em vez de
 * cada componente inventar seu próprio padding. Nomes por função (não por valor), para que a
 * escala possa evoluir sem forçar renomear os usos.
 */
object EstudarioSpacing {
    /** Respiro mínimo entre elementos muito próximos (ex.: ícone e rótulo). */
    val hairline: Dp = 4.dp

    /** Espaço interno de elementos compactos (chips, badges). */
    val tight: Dp = 8.dp

    /** Espaço entre um título e seu apoio imediato. */
    val small: Dp = 12.dp

    /** A unidade de referência do app, margem de tela e gap padrão entre blocos relacionados. */
    val medium: Dp = 16.dp

    /** Espaço interno de painéis e cards. */
    val comfortable: Dp = 20.dp

    /** Separação entre seções dentro do mesmo bloco visual. */
    val large: Dp = 24.dp

    /** Separação entre seções independentes da tela (o "parágrafo" do layout). */
    val section: Dp = 32.dp

    /** Respiro generoso, usado com moderação, só onde o espaço vazio comunica algo. */
    val expansive: Dp = 40.dp

    /**
     * Margem horizontal padrão de tela, proporcional ao aparelho (12dp em tela bem estreita, 20dp
     * no celular comum, 28dp em tablet). Vem de [LocalEstudarioLayout], por isso só existe dentro de
     * um composable.
     */
    val screenGutter: Dp
        @Composable @ReadOnlyComposable get() = LocalEstudarioLayout.current.screenGutter
}

/**
 * Padding padrão do conteúdo de uma tela (listas e colunas roláveis): margem lateral proporcional
 * ao aparelho e respiro vertical fixo. Use no lugar de `PaddingValues(20.dp)`.
 */
@Composable
@ReadOnlyComposable
fun screenPadding(vertical: Dp = 20.dp): PaddingValues =
    PaddingValues(horizontal = LocalEstudarioLayout.current.screenGutter, vertical = vertical)
