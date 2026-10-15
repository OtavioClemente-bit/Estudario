package br.com.estudario.domain.catalog

/**
 * O desenho de cada matéria e de cada área de concurso, no lugar da letra ou da sigla soltas num
 * quadrado: um personagem da turma do Folha (design/turma/turma.js). O valor é o nome do ícone que o
 * site entende (art-*, ou um nome do Material que já tem personagem); o app traduz para o Glyph.
 */
object SubjectArt {
    private fun norm(text: String) = text.lowercase()
        .replace('á', 'a').replace('à', 'a').replace('â', 'a').replace('ã', 'a')
        .replace('é', 'e').replace('ê', 'e').replace('í', 'i').replace('ó', 'o').replace('ô', 'o').replace('õ', 'o')
        .replace('ú', 'u').replace('ç', 'c')

    private val rules: List<Pair<List<String>, String>> = listOf(
        listOf("ingles", "espanhol", "idioma", "lingua estrangeira") to "art-idioma",
        listOf("portugu", "redacao", "lingua", "interpretacao de texto", "gramatica", "literatura") to "art-pena",
        listOf("raciocinio", "matematic", "logic", "estatistic", "financeira", "calculo", "quantitativ") to "art-calculadora",
        listOf("informatica", "tecnologia", "ti ", "ti -", "ti –", "programacao", "software", "banco de dados", "redes", "seguranca da informacao", "desenvolvimento", "computa", "dados", "sistemas", "engenharia de software", "infraestrutura", "nuvem") to "art-laptop",
        listOf("constitucional") to "art-colunas",
        listOf("penal", "criminal", "criminologia", "drogas", "medicina legal") to "art-martelo",
        listOf("contabil", "auditoria", "afo", "orcament", "financas", "tribut", "fiscal", "custos") to "art-moedas",
        listOf("economia", "mercado", "conhecimentos bancarios", "bancari", "vendas", "atendimento") to "trending_up",
        listOf("administra", "gestao", "governanca", "projetos", "processos", "pessoas", "logistica") to "art-maleta",
        listOf("arquivolog", "documenta", "redacao oficial") to "folder_open",
        listOf("etica", "legislacao", "regimento", "estatuto", "normas", "lei ") to "art-pergaminho",
        listOf("direito", "juridic", "processual", "civil", "trabalho", "previdenci", "eleitoral") to "art-balanca",
        listOf("atualidades", "conhecimentos gerais", "geografia", "historia", "regionais", "estaduais", "municipais", "realidade") to "art-globo",
        listOf("saude", "enfermagem", "sus", "medicina", "farmac", "odontolog", "anatomia", "fisiolog") to "art-kitmedico",
        listOf("educacao", "pedagog", "didatica", "ensino") to "school",
        listOf("seguranca publica", "policia", "policial", "transito", "militar") to "shield",
        listOf("fisica", "quimica", "biolog", "ciencias") to "art-frasco",
        listOf("marinh", "naut", "maritim", "navio") to "art-barco",
    )

    /** O desenho da matéria pelo nome dela (o que vem depois de "TI – " também conta). */
    fun iconFor(subjectName: String): String {
        val text = " " + norm(subjectName) + " "
        return rules.firstOrNull { (words, _) -> words.any { text.contains(it) } }?.second ?: "library_books"
    }

    /** O desenho da área do concurso. */
    fun iconFor(category: ExamCategory): String = when (category) {
        ExamCategory.SECURITY -> "shield"
        ExamCategory.COURTS -> "art-martelo"
        ExamCategory.CONTROL -> "art-lupa"
        ExamCategory.FISCAL -> "art-recibo"
        ExamCategory.BANKS -> "art-cofrinho"
        ExamCategory.LEGISLATIVE -> "art-urna"
        ExamCategory.HEALTH -> "art-kitmedico"
        ExamCategory.EDUCATION -> "school"
        ExamCategory.ADMINISTRATIVE -> "art-maleta"
    }
}
