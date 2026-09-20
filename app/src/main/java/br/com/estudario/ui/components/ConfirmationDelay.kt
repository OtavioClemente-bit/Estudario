package br.com.estudario.ui.components

internal object ConfirmationDelay {
    fun remainingSeconds(elapsedMillis: Long, delayMillis: Long): Int {
        if (delayMillis <= 0L) return 0
        return ((delayMillis - elapsedMillis + 999L) / 1_000L).coerceAtLeast(0L).toInt()
    }

    fun isReady(elapsedMillis: Long, delayMillis: Long): Boolean = elapsedMillis >= delayMillis
}
