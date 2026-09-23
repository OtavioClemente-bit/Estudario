package br.com.estudario.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween

/**
 * O movimento do Estudário. Três regras, e nenhuma exceção decorativa:
 *
 * 1. **Movimento serve para explicar mudança de estado**, nunca para enfeitar a entrada da tela.
 * 2. **Progresso anima; conteúdo não salta.** Uma barra que avança de 62% para 68% conta uma
 *    história; um card que entra deslizando só atrasa a leitura.
 * 3. **Curto.** Nada acima de [Emphasized] (400ms), e isso só para progresso longo.
 *
 * As durações são nomeadas pelo papel, não pelo número, para que ajustar o ritmo do app inteiro
 * seja mudar um valor aqui.
 */
object EstudarioMotion {
    /** Realce imediato, pressionar, marcar, alternar. */
    const val Instant = 120

    /** Transição padrão entre estados de um mesmo componente. */
    const val Quick = 220

    /** Troca de conteúdo dentro de uma seção (o painel "Agora" mudando de missão). */
    const val Standard = 300

    /** Progresso contando, barras de cobertura, anel de meta, XP subindo. */
    const val Emphasized = 400

    /** Entrada/saída suave, sem energia: o padrão do app. */
    val Standard_Easing: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    /** Para valores que "assentam", progresso chegando ao destino. */
    val Settle: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)

    fun <T> quick(): FiniteAnimationSpec<T> = tween(Quick, easing = Standard_Easing)
    fun <T> standard(): FiniteAnimationSpec<T> = tween(Standard, easing = Standard_Easing)
    fun <T> progress(): FiniteAnimationSpec<T> = tween(Emphasized, easing = Settle)
}
