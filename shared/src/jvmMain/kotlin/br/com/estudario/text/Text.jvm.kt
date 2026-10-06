package br.com.estudario.text

import java.text.Normalizer

actual fun normalizeNfd(value: String): String = Normalizer.normalize(value, Normalizer.Form.NFD)
