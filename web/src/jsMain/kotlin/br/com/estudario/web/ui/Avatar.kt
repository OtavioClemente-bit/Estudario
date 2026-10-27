package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Store
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import org.w3c.files.get

/** Os personagens do Estudário para a imagem do perfil (os mesmos do app: PresetAvatar). */
private data class PresetAvatar(val key: String, val label: String, val top: String, val bottom: String, val glyph: String?)

private val PRESETS = listOf(
    PresetAvatar("folha", "Folha", "#6C5CFF", "#3326CE", null),
    PresetAvatar("capelo", "Formatura", "#34D399", "#0E8F63", "school"),
    PresetAvatar("lampada", "Ideia", "#FFC857", "#E08A00", "lightbulb"),
    PresetAvatar("alvo", "Foco", "#FF8A7A", "#D9443A", "gps_fixed"),
    PresetAvatar("trofeu", "Aprovação", "#5CC8FF", "#1C7FC4", "emoji_events"),
)

/**
 * A imagem escolhida para o perfil, guardada neste navegador: "avatar:chave" (um personagem),
 * "photo:data:…" (foto enviada, reduzida para 256 px) ou nada (a foto da conta Google, ou as iniciais).
 */
object AvatarChoice {
    private const val KEY = "estudario.avatar"
    var value by mutableStateOf(runCatching { localStorage.getItem(KEY) }.getOrNull())
        private set
    var picking by mutableStateOf(false)
    fun set(v: String?) {
        value = v
        runCatching { if (v == null) localStorage.removeItem(KEY) else localStorage.setItem(KEY, v) }
            .onFailure { Toast.show("Não consegui guardar a imagem neste navegador.") }
    }
}

/** O conteúdo do avatar (vai dentro do círculo de quem chama): personagem, foto, Google ou iniciais. */
@Composable
fun AvatarContent() {
    val session = Store.session
    val v = AvatarChoice.value
    val preset = v?.takeIf { it.startsWith("avatar:") }?.removePrefix("avatar:")?.let { k -> PRESETS.firstOrNull { it.key == k } }
    when {
        preset != null -> PresetArt(preset)
        v?.startsWith("photo:") == true -> Img(src = v.removePrefix("photo:"), alt = "")
        session?.avatarUrl != null -> Img(src = session.avatarUrl, alt = "")
        else -> Text(initials(session?.name ?: session?.email))
    }
}

@Composable
private fun PresetArt(p: PresetAvatar) {
    Span({ classes("av-preset"); attr("style", "background:linear-gradient(180deg,${p.top},${p.bottom})") }) {
        if (p.glyph == null) Folha("happy", 40, live = false) else Icon(p.glyph, filled = true)
    }
}

/** A janela de escolher a imagem: os personagens, uma foto sua ou a da conta. */
@Composable
fun AvatarPickerHost() {
    if (!AvatarChoice.picking) return
    val close = { AvatarChoice.picking = false }
    Modal(onDismiss = close, extraClass = "gx-sheetmodal av-modal") {
        Div({ classes("gx-top") }) {
            Div({ classes("gx-top-id") }) {
                Span({ classes("gx-kicker") }) { Text("SUA IMAGEM") }
                H2 { Text("Quem é você no Estudário?") }
                P({ classes("gx-topic") }) { Text("Escolha um dos nossos personagens ou use uma foto sua.") }
            }
            Button({ classes("gx-x"); attr("type", "button"); attr("aria-label", "Fechar"); onClick { close() } }) { Icon("close", plain = true) }
        }
        Div({ classes("gx-controls", "solo") }) {
            Div({ classes("av-grid") }) {
                PRESETS.forEach { p ->
                    val on = AvatarChoice.value == "avatar:${p.key}"
                    Button({ classes(*listOfNotNull("av-pick", if (on) "on" else null).toTypedArray()); attr("type", "button"); onClick { AvatarChoice.set("avatar:${p.key}"); close() } }) {
                        Span({ classes("av-circle") }) { PresetArt(p) }
                        B { Text(p.label) }
                    }
                }
                Store.session?.avatarUrl?.let { url ->
                    val on = AvatarChoice.value == null
                    Button({ classes(*listOfNotNull("av-pick", if (on) "on" else null).toTypedArray()); attr("type", "button"); onClick { AvatarChoice.set(null); close() } }) {
                        Span({ classes("av-circle") }) { Img(src = url, alt = "") }
                        B { Text("Foto da conta") }
                    }
                }
            }
            Label(attrs = { classes("gx-launch", "av-upload") }) {
                Span({ classes("gx-launch-ic") }) { Icon("person", filled = true) }
                Span { Text("Usar uma foto minha") }
                Input(InputType.File) {
                    attr("accept", "image/*"); attr("style", "display:none")
                    onChange { e ->
                        val file = (e.target as HTMLInputElement).files?.get(0) ?: return@onChange
                        val reader = FileReader()
                        reader.onload = {
                            val img = document.createElement("img") as HTMLImageElement
                            img.onload = {
                                // Corta no centro e reduz para 256 px: cabe no navegador sem pesar.
                                val side = minOf(img.naturalWidth, img.naturalHeight)
                                val canvas = document.createElement("canvas") as HTMLCanvasElement
                                canvas.width = 256; canvas.height = 256
                                val ctx = canvas.getContext("2d").asDynamic()
                                ctx.drawImage(img, (img.naturalWidth - side) / 2.0, (img.naturalHeight - side) / 2.0, side.toDouble(), side.toDouble(), 0.0, 0.0, 256.0, 256.0)
                                AvatarChoice.set("photo:" + canvas.toDataURL("image/jpeg", 0.85))
                                close()
                                null
                            }
                            img.src = reader.result as String
                            null
                        }
                        reader.readAsDataURL(file)
                    }
                }
            }
            if (AvatarChoice.value != null) Button({ classes("gx-ghost"); attr("type", "button"); onClick { AvatarChoice.set(null); close() } }) { Text(if (Store.session?.avatarUrl != null) "Voltar para a foto da conta" else "Remover imagem") }
            P({ classes("gx-note") }) { Text("A imagem fica guardada neste navegador.") }
        }
    }
}
