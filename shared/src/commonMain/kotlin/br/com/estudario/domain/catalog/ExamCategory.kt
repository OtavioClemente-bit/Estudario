package br.com.estudario.domain.catalog

import br.com.estudario.text.stripAccents

/**
 * A área de um edital do catálogo, para filtrar a escolha ("Segurança pública", "Tribunais"...).
 * Sai do órgão, da sigla e do cargo, sem campo novo no banco. A ordem das regras importa: "Tribunal
 * de Contas" é controle, não tribunal; "Superior Tribunal Militar" é tribunal, não militar.
 */
enum class ExamCategory(val label: String) {
    SECURITY("Segurança pública"),
    COURTS("Tribunais e jurídicas"),
    CONTROL("Controle e contas"),
    FISCAL("Fiscal e tributária"),
    BANKS("Bancos"),
    LEGISLATIVE("Legislativo"),
    HEALTH("Saúde"),
    EDUCATION("Educação"),
    ADMINISTRATIVE("Agências e administrativos"),
    ;

    companion object {
        private fun has(text: String, vararg words: String) = words.any { text.contains(it) }

        fun of(shortName: String, agency: String, role: String = ""): ExamCategory {
            val text = " " + stripAccents("$agency $shortName $role".lowercase()).replace(Regex("[^a-z0-9]+"), " ") + " "
            return when {
                has(text, "tribunal de contas", "controladoria", "auditoria geral", " cgu ", " tcu ", " tce ") -> CONTROL
                has(text, "tribunal", "ministerio publico", "defensoria", "procuradoria", "advocacia geral", " mpu ", " trt ", " trf ", " tre ", " tj ") -> COURTS
                has(text, "policia", "bombeiro", "penitenciari", "penal", "exercito", "cadetes", "militar", "inteligencia", "guarda municipal") -> SECURITY
                has(text, "fazenda", "receita", "tesouro", "economia", "tributari", "fiscal", "sefaz") -> FISCAL
                has(text, "banco", "caixa economica", "valores mobiliarios") -> BANKS
                has(text, "camara", "assembleia", "senado", "legislativ") -> LEGISLATIVE
                has(text, "saude", "hospital", "ebserh") -> HEALTH
                has(text, "educacao", "universidade", "instituto federal", "escola", "professor") -> EDUCATION
                else -> ADMINISTRATIVE
            }
        }

        /**
         * O selo do edital: a sigla quando ela existe ("PMMG", "TRT", "CBMMG"), senão as iniciais
         * das palavras com maiúscula ("Polícia Federal" vira "PF", "Câmara dos Deputados" vira "CD").
         */
        fun monogram(shortName: String): String {
            val words = shortName.split(' ', '-', '/').filter { it.isNotBlank() }
            val first = words.firstOrNull().orEmpty()
            if (first.length in 2..6 && first.all { it.isUpperCase() || it.isDigit() }) return first
            val initials = words.filter { it.first().isUpperCase() }.joinToString("") { it.first().toString() }
            return (initials.ifBlank { first.take(3) }).take(4).uppercase()
        }

        /** As áreas que aparecem na lista, na ordem do enum e só as que têm edital. */
        fun present(categories: Collection<ExamCategory>): List<ExamCategory> = entries.filter { it in categories }
    }
}
