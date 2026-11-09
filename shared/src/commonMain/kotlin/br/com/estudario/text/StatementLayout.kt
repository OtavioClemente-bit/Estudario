package br.com.estudario.text

/**
 * Comando colado no último item ("IV. Domínios de segurança. Associe os conjuntos..."): o comando vai
 * para um parágrafo próprio, como na prova. Vale para as questões que já estão no aparelho; as novas
 * já saem separadas do servidor. Só mexe em enunciado com itens (I., II. ou 1., 2.) e só no último.
 */
object StatementLayout {
    private val item = Regex("""(?:^|\s)(?:[IVX]{1,4}|\d{1,2})[.)]\s""")
    private val hasSecond = Regex("""(?:^|\s)(?:II|2)[.)]\s""")
    private val command = Regex(
        """([.;])\s+(?=(?:Associe|Assinale|Indique|Marque|Relacione|Correlacione|Ordene|Classifique|Julgue|Identifique|Considerando|Quais|Pode-se afirmar|A sequ[êe]ncia correta|A ordem correta|A associa[çc][ãa]o correta|(?:Est[áa]|Est[ãa]o|[ÉE]|S[ãa]o)\s+(?:apenas\s+)?(?:corret|incorret|verdadeir|fals|cert)[oa]s?)\b)""",
    )

    fun separateCommand(statement: String): String {
        if (!hasSecond.containsMatchIn(statement)) return statement
        val last = item.findAll(statement).lastOrNull() ?: return statement
        val start = last.range.last + 1
        val hit = command.find(statement, start) ?: return statement
        val cut = hit.range.first + 1
        return statement.substring(0, cut).trimEnd() + "\n\n" + statement.substring(cut).trimStart()
    }
}
