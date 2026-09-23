package br.com.estudario.ui.screens

internal fun queueRowLabel(paused: Boolean, completed: Boolean, isNext: Boolean): String = when {
    paused -> "Pausado"
    completed -> "Concluído"
    isNext -> "Próximo estudo"
    else -> "Na fila"
}
