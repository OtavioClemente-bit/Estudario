package br.com.estudario.ui

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

class CopyStyleTest {
    @Test
    fun appSourceDoesNotContainTypographicDashes() {
        val sourceRoot = File("src")
        val forbiddenCharacters = setOf('\u2014', '\u2013')
        val violations = sourceRoot.walkTopDown()
            .filter { it.isFile && it.extension in setOf("kt", "xml") }
            .flatMap { file ->
                file.readText().lineSequence()
                    .mapIndexedNotNull { index, line ->
                        if (line.any(forbiddenCharacters::contains)) {
                            "${file.path}:${index + 1}"
                        } else {
                            null
                        }
                    }
            }
            .toList()

        assertTrue(
            "Remove typographic dashes from app copy and source comments: ${violations.joinToString()}",
            violations.isEmpty(),
        )
    }
}
