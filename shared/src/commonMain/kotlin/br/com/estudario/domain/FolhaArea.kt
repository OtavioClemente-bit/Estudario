package br.com.estudario.domain

import br.com.estudario.domain.catalog.ExamCategory
import br.com.estudario.text.stripAccents

/**
 * A área de carreira do armário do Folha. Cada área tem a sua coleção de roupas e acessórios; as peças
 * da área do edital principal da pessoa saem rápido (por dias estudados) e as das outras áreas também
 * saem, só que bem mais devagar. O que já saiu fica na conta, mesmo trocando ou apagando o edital.
 */
enum class FolhaArea(val label: String, val tagline: String) {
    SAUDE("Saúde", "Jaleco, plantão e cuidado"),
    MILITAR("Militar", "Forças Armadas e bombeiros"),
    POLICIA("Polícia", "Segurança pública"),
    TRIBUNAIS("Tribunais", "Tribunais, MP e defensorias"),
    FISCAL("Fiscal", "Receita e tributos"),
    CONTROLE("Controle", "Tribunais de contas e auditoria"),
    BANCOS("Bancos", "Bancos e mercado"),
    LEGISLATIVO("Legislativo", "Câmaras, assembleias e Senado"),
    EDUCACAO("Educação", "Escolas, institutos e universidades"),
    TI("Tecnologia", "Cargos de TI de qualquer órgão"),
    ADMINISTRATIVO("Administrativo", "Agências, prefeituras e órgãos em geral"),
    ;

    companion object {
        private fun has(text: String, vararg words: String) = words.any { text.contains(it) }

        /**
         * A área de um edital pelo órgão, pela sigla e pelo cargo. O cargo de TI vence o órgão (analista de
         * TI de tribunal veste o de tecnologia); Forças Armadas e bombeiros são militares; as polícias, polícia.
         */
        fun of(shortName: String, agency: String, role: String = ""): FolhaArea {
            val roleText = " " + stripAccents(role.lowercase()).replace(Regex("[^a-z0-9]+"), " ") + " "
            if (has(roleText, "tecnologia da informacao", " informatica", " ti ", "desenvolv", "ciencia de dados", "analista de sistemas", "suporte tecnico", "redes de computadores", "seguranca da informacao", "programador", "banco de dados")) return TI
            val text = " " + stripAccents("$agency $shortName $role".lowercase()).replace(Regex("[^a-z0-9]+"), " ") + " "
            if (has(text, "exercito", "marinha", "aeronautica", "forcas armadas", "cadetes", "espcex", "eear", "fuzileiro", "bombeiro", "sargento", "oficial militar")) return MILITAR
            return when (ExamCategory.of(shortName, agency, role)) {
                ExamCategory.SECURITY -> if (has(text, "militar") && !has(text, "policia")) MILITAR else POLICIA
                ExamCategory.COURTS -> TRIBUNAIS
                ExamCategory.CONTROL -> CONTROLE
                ExamCategory.FISCAL -> FISCAL
                ExamCategory.BANKS -> BANCOS
                ExamCategory.LEGISLATIVE -> LEGISLATIVO
                ExamCategory.HEALTH -> SAUDE
                ExamCategory.EDUCATION -> EDUCACAO
                ExamCategory.ADMINISTRATIVE -> if (has(text, "saude", "enferm", "medic", "farmac", "odonto", "fisioterap", "nutricion", "psicolog")) SAUDE
                    else if (has(text, "professor", "pedagog", "docente")) EDUCACAO else ADMINISTRATIVO
            }
        }
    }
}
