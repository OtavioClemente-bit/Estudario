package br.com.estudario.text

private fun jsNormalizeNfd(value: String): String = js("value.normalize('NFD')")

actual fun normalizeNfd(value: String): String = jsNormalizeNfd(value)
