package br.com.estudario.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Linguagem de forma do Estudário. Nem tudo tem o mesmo raio, o painel "Agora" é a forma mais
 * arredondada da tela (é o que deve chamar atenção primeiro); componentes compactos e chips usam
 * um raio bem menor, quase reto, para não competir por atenção.
 *
 * Tipado como [CornerBasedShape] (não a interface [androidx.compose.ui.graphics.Shape] mais genérica)
 * de propósito: é o tipo que o construtor de [Shapes] do Material 3 exige, e as formas daqui alimentam
 * [EstudarioMaterialShapes] diretamente.
 */
object EstudarioShapes {
    /** O painel principal ("Agora"), a forma mais distinta da Home. */
    val spotlight: CornerBasedShape = RoundedCornerShape(28.dp)

    /** Painéis e cards de conteúdo padrão. */
    val panel: CornerBasedShape = RoundedCornerShape(18.dp)

    /** Linhas e itens dentro de um painel (nó da trilha, item de lista). */
    val row: CornerBasedShape = RoundedCornerShape(14.dp)

    /** Elementos compactos, chips, badges, marcadores de matéria. */
    val compact: CornerBasedShape = RoundedCornerShape(10.dp)

    /** Forma totalmente arredondada, pílulas e indicadores circulares. */
    val pill: CornerBasedShape = RoundedCornerShape(50)
}

/** Shapes do Material 3 derivadas da mesma escala, para os poucos componentes que os exigem diretamente. */
val EstudarioMaterialShapes: Shapes = Shapes(
    extraSmall = EstudarioShapes.compact,
    small = EstudarioShapes.row,
    medium = EstudarioShapes.panel,
    large = EstudarioShapes.spotlight,
    extraLarge = RoundedCornerShape(32.dp),
)
