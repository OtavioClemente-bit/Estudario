package br.com.estudario.web

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.document
import kotlinx.browser.localStorage

/** Tema claro, escuro ou do sistema, como em Ajustes > Aparência no app. */
object Theme {
    private const val KEY = "estudario.theme"
    var mode by mutableStateOf(runCatching { localStorage.getItem(KEY) }.getOrNull() ?: "SYSTEM")
        private set

    fun set(value: String) {
        mode = value
        runCatching { localStorage.setItem(KEY, value) }
        // Vai para a conta: o app usa o mesmo tema (e o tema escolhido no app chega aqui).
        br.com.estudario.web.data.AccountSettings.changed(br.com.estudario.domain.SyncedSettings.THEME, value)
        apply()
    }

    fun apply() {
        val root = document.documentElement ?: return
        when (mode) {
            "LIGHT" -> root.setAttribute("data-theme", "light")
            "DARK" -> root.setAttribute("data-theme", "dark")
            else -> root.removeAttribute("data-theme")
        }
    }
}
