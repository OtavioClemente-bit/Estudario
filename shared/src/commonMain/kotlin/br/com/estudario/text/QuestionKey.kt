package br.com.estudario.text

/**
 * Chave curta de um enunciado, igual no app, no site e no servidor (questionKey em
 * supabase/functions/_shared/simulation.ts): as primeiras 160 letras e algarismos, em minúsculas,
 * num hash FNV-1a de 32 bits. Espaços e pontuação não contam, então a mesma questão com as
 * assertivas quebradas em parágrafos dá a mesma chave. O simulado usa para nunca repetir questão.
 */
object QuestionKey {
    fun of(statement: String): String {
        val letters = StringBuilder()
        for (char in statement.lowercase()) {
            if (char.isLetterOrDigit()) letters.append(char)
            if (letters.length == 160) break
        }
        var hash = 0x811c9dc5.toInt()
        for (char in letters) hash = (hash xor char.code) * 16777619
        return hash.toUInt().toString(16).padStart(8, '0')
    }
}
