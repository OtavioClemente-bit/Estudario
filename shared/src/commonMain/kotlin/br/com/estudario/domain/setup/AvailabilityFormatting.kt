package br.com.estudario.domain.setup

import kotlin.math.roundToInt

fun formatAvailabilityMinutes(minutes: Int): String {
    val value = minutes.coerceAtLeast(0)
    if (value == 0) return "Folga"
    val hours = value / 60
    val remainingMinutes = value % 60
    return when {
        hours == 0 -> "$remainingMinutes min"
        remainingMinutes == 0 -> "$hours h"
        else -> "$hours h $remainingMinutes min"
    }
}

fun snapAvailabilityMinutes(rawMinutes: Float): Int =
    ((rawMinutes / 15f).roundToInt() * 15).coerceIn(0, 1_440)
