package br.com.estudario.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object EstudarioSpacing {
    val hairline: Dp = 4.dp
    val tight: Dp = 8.dp
    val small: Dp = 12.dp
    val medium: Dp = 16.dp
    val comfortable: Dp = 20.dp
    val large: Dp = 24.dp
    val section: Dp = 32.dp
    val expansive: Dp = 40.dp
    val screenGutter: Dp = 20.dp
}

object EstudarioShapes {
    val spotlight: CornerBasedShape = RoundedCornerShape(28.dp)
    val panel: CornerBasedShape = RoundedCornerShape(18.dp)
    val row: CornerBasedShape = RoundedCornerShape(14.dp)
    val compact: CornerBasedShape = RoundedCornerShape(10.dp)
    val pill: CornerBasedShape = RoundedCornerShape(50)
}

object EstudarioMotion {
    private val easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    fun <T> quick(): FiniteAnimationSpec<T> = tween(220, easing = easing)
    fun <T> standard(): FiniteAnimationSpec<T> = tween(300, easing = easing)
    fun <T> progress(): FiniteAnimationSpec<T> = tween(400, easing = easing)
}
