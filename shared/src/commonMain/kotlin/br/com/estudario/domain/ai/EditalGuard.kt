package br.com.estudario.domain.ai

import br.com.estudario.text.stripAccents

/**
 * Confere, sem IA e de graça, se o texto de um PDF tem cara de edital (ou de conteúdo programático)
 * antes de gastar a cota de "montar edital com IA". Um boletim, um aditamento ou um livro qualquer
 * é barrado aqui. A mesma regra roda no servidor (`_shared/edital-guard.ts`): mantenha as duas iguais.
 *
 * Texto vazio (PDF escaneado, sem texto) não é julgado: segue para o servidor, que lê o PDF.
 */
object EditalGuard {
    /** Seções que só aparecem em edital ou em anexo de conteúdo programático. */
    val SECTION_TERMS = listOf(
        "conteudo programatico", "conteudos programaticos", "objetos de avaliacao", "objeto de avaliacao",
        "programa das provas", "programas das provas", "conhecimentos basicos", "conhecimentos gerais",
        "conhecimentos especificos", "conteudo das provas",
    )

    /** Palavras de concurso (inscrição, cargo, prova...). */
    val CONTEST_TERMS = listOf(
        "edital", "concurso publico", "candidato", "inscricao", "inscricoes", "cargo", "vagas", "prova objetiva",
        "provas objetivas", "banca examinadora", "homologacao", "processo seletivo", "selecao publica", "nomeacao",
        "cadastro de reserva", "taxa de inscricao",
    )

    /** Nomes de disciplina que costumam aparecer no programa. */
    val SUBJECT_TERMS = listOf(
        "lingua portuguesa", "matematica", "raciocinio logico", "informatica", "direito constitucional",
        "direito administrativo", "direito penal", "direito civil", "direito processual", "direito tributario",
        "direito do trabalho", "legislacao", "nocoes de", "atualidades", "contabilidade", "administracao publica",
        "administracao geral", "etica", "lingua inglesa", "lingua espanhola", "historia", "geografia", "fisica",
        "quimica", "biologia", "estatistica", "economia", "auditoria", "arquivologia", "redacao",
    )

    data class Verdict(val ok: Boolean, val sections: Int, val contest: Int, val subjects: Int, val judged: Boolean)

    fun check(text: String?): Verdict {
        val normalized = stripAccents(text.orEmpty()).lowercase().replace(Regex("\\s+"), " ")
        // Pouco texto: provavelmente PDF escaneado; o servidor decide lendo o PDF.
        if (normalized.length < 400) return Verdict(ok = true, sections = 0, contest = 0, subjects = 0, judged = false)
        val sections = SECTION_TERMS.count { it in normalized }
        val contest = CONTEST_TERMS.count { it in normalized }
        val subjects = SUBJECT_TERMS.count { it in normalized }
        val ok = subjects >= 4 || (sections >= 1 && subjects >= 2) || contest >= 3
        return Verdict(ok, sections, contest, subjects, judged = true)
    }

    const val MESSAGE = "Esse PDF não parece um edital de concurso: não encontramos o conteúdo programático " +
        "(disciplinas e assuntos da prova). Envie o edital de abertura ou o anexo com o conteúdo programático. " +
        "Nada foi gasto da sua cota."
}
