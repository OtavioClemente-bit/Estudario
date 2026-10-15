package br.com.estudario.domain.catalog

/**
 * O desenho de cada matéria e de cada área de concurso, no lugar da letra ou da sigla soltas num
 * quadrado. O valor é o nome do ícone (Material Symbols): o site usa direto, o app traduz para o
 * ImageVector equivalente. Mesma regra nos dois lados.
 */
object SubjectArt {
    private fun norm(text: String) = text.lowercase()
        .replace('á', 'a').replace('à', 'a').replace('â', 'a').replace('ã', 'a')
        .replace('é', 'e').replace('ê', 'e').replace('í', 'i').replace('ó', 'o').replace('ô', 'o').replace('õ', 'o')
        .replace('ú', 'u').replace('ç', 'c')

    private val rules: List<Pair<List<String>, String>> = listOf(
        listOf("ingles", "espanhol", "idioma", "lingua estrangeira") to "translate",
        listOf("portugu", "redacao", "lingua", "interpretacao de texto", "gramatica", "literatura") to "menu_book",
        listOf("raciocinio", "matematic", "logic", "estatistic", "financeira", "calculo", "quantitativ") to "calculate",
        listOf("informatica", "tecnologia", "ti ", "ti -", "ti –", "programacao", "software", "banco de dados", "redes", "seguranca da informacao", "desenvolvimento", "computa", "dados", "sistemas", "engenharia de software", "infraestrutura", "nuvem") to "computer",
        listOf("constitucional") to "account_balance",
        listOf("penal", "criminal", "criminologia", "drogas", "medicina legal") to "gavel",
        listOf("contabil", "auditoria", "afo", "orcament", "financas", "tribut", "fiscal", "custos") to "payments",
        listOf("economia", "mercado", "conhecimentos bancarios", "bancari", "vendas", "atendimento") to "trending_up",
        listOf("administra", "gestao", "governanca", "projetos", "processos", "pessoas", "logistica") to "business_center",
        listOf("arquivolog", "documenta", "redacao oficial") to "folder_open",
        listOf("etica", "legislacao", "regimento", "estatuto", "normas", "lei ") to "policy",
        listOf("direito", "juridic", "processual", "civil", "trabalho", "previdenci", "eleitoral") to "balance",
        listOf("atualidades", "conhecimentos gerais", "geografia", "historia", "regionais", "estaduais", "municipais", "realidade") to "public",
        listOf("saude", "enfermagem", "sus", "medicina", "farmac", "odontolog", "anatomia", "fisiolog") to "medical_services",
        listOf("educacao", "pedagog", "didatica", "ensino") to "school",
        listOf("seguranca publica", "policia", "policial", "transito", "militar") to "shield",
        listOf("fisica", "quimica", "biolog", "ciencias") to "science",
        listOf("marinh", "naut", "maritim", "navio") to "sailing",
    )

    /** O desenho da matéria pelo nome dela (o que vem depois de "TI – " também conta). */
    fun iconFor(subjectName: String): String {
        val text = " " + norm(subjectName) + " "
        return rules.firstOrNull { (words, _) -> words.any { text.contains(it) } }?.second ?: "auto_stories"
    }

    /** O desenho da área do concurso. */
    fun iconFor(category: ExamCategory): String = when (category) {
        ExamCategory.SECURITY -> "shield"
        ExamCategory.COURTS -> "gavel"
        ExamCategory.CONTROL -> "fact_check"
        ExamCategory.FISCAL -> "receipt_long"
        ExamCategory.BANKS -> "account_balance"
        ExamCategory.LEGISLATIVE -> "how_to_vote"
        ExamCategory.HEALTH -> "medical_services"
        ExamCategory.EDUCATION -> "school"
        ExamCategory.ADMINISTRATIVE -> "business_center"
    }
}
