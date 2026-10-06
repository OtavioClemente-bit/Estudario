package br.com.estudario.text

actual fun normalizeNfd(value: String): String = value.asDynamic().normalize("NFD") as String
