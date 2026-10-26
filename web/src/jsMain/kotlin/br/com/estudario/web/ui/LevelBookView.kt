package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import br.com.estudario.domain.LevelBook
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLElement

/**
 * O livro de Folhas do nível (o mesmo desenho do app, LevelBook no módulo comum). Até 40 px mostra
 * só a medalha; até 84 px, a medalha simples com o número maior. Tocando, ele abre e o Folha traz
 * uma folha; [animateFrom] faz a subida de nível de verdade (a tela de comemoração usa).
 */
@Composable
internal fun LevelBookView(level: Int, size: Int, animateFrom: Int? = null, extraStyle: String = "") {
    val mode = when { size <= 40 -> LevelBook.Mode.TINY; size <= 84 -> LevelBook.Mode.SMALL; else -> LevelBook.Mode.NORMAL }
    androidx.compose.runtime.key(level, size, animateFrom) {
        Div({
            classes("level-book")
            attr("role", "img")
            attr("aria-label", "Nível $level, ${LevelBook.tiers[LevelBook.tierOf(level).coerceAtLeast(0)].title}")
            attr("style", "width:${size}px;height:${size}px;$extraStyle")
            ref { el ->
                el.innerHTML = LevelBook.svg(level, mode)
                var running = false
                fun play(from: Int, to: Int) {
                    if (running || window.matchMedia("(prefers-reduced-motion: reduce)").matches) { el.innerHTML = LevelBook.svg(to, mode); return }
                    running = true
                    val total = LevelBook.levelUpMillis(from, to).toDouble()
                    val start = window.performance.now()
                    fun frame(now: Double) {
                        if (!el.isConnected) return
                        val u = ((now - start) / total).coerceIn(0.0, 1.0)
                        el.innerHTML = LevelBook.levelUpSvg(from, to, u, mode)
                        if (u < 1) window.requestAnimationFrame(::frame) else { running = false; el.innerHTML = LevelBook.svg(to, mode) }
                    }
                    window.requestAnimationFrame(::frame)
                }
                // tocar: ele abre e o Folha traz uma folha (o número fica)
                if (mode != LevelBook.Mode.TINY) el.addEventListener("click", { play(level, level) })
                if (animateFrom != null && animateFrom != level) window.setTimeout({ play(animateFrom, level) }, 350)
                onDispose { }
            }
        })
    }
}

@Suppress("unused")
private val HTMLElement.isConnected: Boolean get() = asDynamic().isConnected as Boolean
