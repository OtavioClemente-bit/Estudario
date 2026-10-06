package br.com.estudario.math

import kotlin.math.roundToInt
import kotlin.math.roundToLong

/** Mesmo resultado de java.lang.Math.round(double): empate sobe, NaN vira 0. */
fun javaRound(value: Double): Long = if (value.isNaN()) 0L else value.roundToLong()

/** Mesmo resultado de java.lang.Math.round(float). */
fun javaRound(value: Float): Int = if (value.isNaN()) 0 else value.roundToInt()
