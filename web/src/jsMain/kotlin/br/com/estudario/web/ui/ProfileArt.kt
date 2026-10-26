package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.TagElement

/** Os mesmos desenhos e gestos do perfil Android, renderizados por profile-art.js. */
@Composable
internal fun ProfileArt(
    kind: String,
    size: Int = 40,
    level: Int = 1,
    lit: Boolean = false,
    frozen: Boolean = false,
    fraction: Float = 0f,
    done: Boolean = false,
) {
    TagElement<org.w3c.dom.HTMLElement>(
        elementBuilder = ElementBuilder.createBuilder("estudario-profile-art"),
        applyAttrs = {
            attr("kind", kind)
            attr("level", level.coerceIn(1, 200).toString())
            attr("lit", lit.toString())
            attr("frozen", frozen.toString())
            attr("fraction", fraction.coerceIn(0f, 1f).toString())
            attr("done", done.toString())
            attr("style", "--profile-art-size:${size}px;--profile-art-track:var(--line-strong)")
        },
        content = null,
    )
}
