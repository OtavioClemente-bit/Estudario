package br.com.estudario.data.ai

import kotlinx.serialization.Serializable

/**
 * Respostas do formulário que antecede a geração de edital pela IA do Estudário. São as mesmas
 * perguntas do gerador por prompt, para que a IA saiba de qual concurso e cargo se trata e o que
 * deve extrair do PDF.
 */
@Serializable
data class AiSyllabusPreferences(
    val competitionName: String,
    val role: String,
    val board: String = "",
    val year: String = "",
    /** Nome de [br.com.estudario.data.prompt.EditalScope]. */
    val scope: String = "FULL",
    /** Nome de [br.com.estudario.data.prompt.EditalDetail]. */
    val detail: String = "DIDACTIC",
    val includeDescriptions: Boolean = true,
) {
    val isComplete: Boolean get() = competitionName.isNotBlank() && role.isNotBlank()
}
