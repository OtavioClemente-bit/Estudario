package br.com.estudario.web

import br.com.estudario.web.data.Store
import br.com.estudario.web.ui.App
import org.jetbrains.compose.web.renderComposable

fun main() {
    Theme.apply()
    if (kotlinx.browser.window.location.hash.startsWith("#demo") || kotlinx.browser.window.location.search.contains("demo")) Store.startDemo() else Store.start()
    renderComposable(rootElementId = "root") { App() }
}
