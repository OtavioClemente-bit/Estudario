package br.com.estudario.ui.components

import java.net.URI
import java.util.Locale

internal sealed interface InlineMarkdownToken {
    data class Text(val value: String) : InlineMarkdownToken
    data class Link(val label: String, val url: String) : InlineMarkdownToken
}

private val inlineLinkPattern = Regex("\\[([^\\]]+)]\\((https?://[^\\s)]+)\\)|https?://[^\\s<>()]+", RegexOption.IGNORE_CASE)
private val trailingUrlPunctuation = setOf('.', ',', ';', ':', '!', '?')

internal fun parseInlineLinks(text: String): List<InlineMarkdownToken> {
    val tokens = mutableListOf<InlineMarkdownToken>()

    fun appendText(value: String) {
        if (value.isEmpty()) return
        val previous = tokens.lastOrNull()
        if (previous is InlineMarkdownToken.Text) {
            tokens[tokens.lastIndex] = InlineMarkdownToken.Text(previous.value + value)
        } else {
            tokens += InlineMarkdownToken.Text(value)
        }
    }

    var cursor = 0
    inlineLinkPattern.findAll(text).forEach { match ->
        appendText(text.substring(cursor, match.range.first))
        val markdownLabel = match.groups[1]?.value
        val candidate = match.groups[2]?.value ?: match.value.trimEnd(*trailingUrlPunctuation.toCharArray())
        val url = candidate.takeIf(::isSafeWebUrl)
        if (url == null) {
            appendText(match.value)
        } else {
            tokens += InlineMarkdownToken.Link(markdownLabel ?: url, url)
            if (markdownLabel == null && candidate.length < match.value.length) {
                appendText(match.value.substring(candidate.length))
            }
        }
        cursor = match.range.last + 1
    }
    appendText(text.substring(cursor))
    return tokens
}

internal fun isSafeWebUrl(value: String): Boolean = runCatching {
    val uri = URI(value)
    uri.scheme?.lowercase(Locale.ROOT) in setOf("http", "https") &&
        !uri.host.isNullOrBlank() && uri.rawUserInfo == null
}.getOrDefault(false)
