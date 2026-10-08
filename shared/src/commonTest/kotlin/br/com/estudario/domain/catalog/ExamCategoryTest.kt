package br.com.estudario.domain.catalog

import kotlin.test.Test
import kotlin.test.assertEquals

class ExamCategoryTest {
    @Test
    fun ordemDasRegrasResolveOsCasosAmbiguos() {
        assertEquals(ExamCategory.CONTROL, ExamCategory.of("TCE-MG", "Tribunal de Contas do Estado de Minas Gerais"))
        assertEquals(ExamCategory.COURTS, ExamCategory.of("STM", "Superior Tribunal Militar"))
        assertEquals(ExamCategory.COURTS, ExamCategory.of("TRT-3", "Tribunal Regional do Trabalho da 3ª Região (MG)"))
        assertEquals(ExamCategory.SECURITY, ExamCategory.of("PMMG", "Polícia Militar de Minas Gerais", "Soldado"))
        assertEquals(ExamCategory.SECURITY, ExamCategory.of("ABIN", "Agência Brasileira de Inteligência"))
        assertEquals(ExamCategory.FISCAL, ExamCategory.of("SEFAZ-SP", "Secretaria da Fazenda e Planejamento do Estado de São Paulo"))
        assertEquals(ExamCategory.BANKS, ExamCategory.of("CEF", "Caixa Econômica Federal"))
        assertEquals(ExamCategory.HEALTH, ExamCategory.of("SES-MG", "Secretaria de Estado de Saúde de Minas Gerais"))
        assertEquals(ExamCategory.EDUCATION, ExamCategory.of("UFPB", "Universidade Federal da Paraíba"))
        assertEquals(ExamCategory.LEGISLATIVE, ExamCategory.of("Câmara", "Câmara dos Deputados"))
        assertEquals(ExamCategory.ADMINISTRATIVE, ExamCategory.of("INSS", "Instituto Nacional do Seguro Social"))
    }

    @Test
    fun seloUsaASiglaOuAsIniciais() {
        assertEquals("CBMMG", ExamCategory.monogram("CBMMG"))
        assertEquals("TRT", ExamCategory.monogram("TRT 3ª Região"))
        assertEquals("PF", ExamCategory.monogram("Polícia Federal"))
        assertEquals("CD", ExamCategory.monogram("Câmara dos Deputados"))
        assertEquals("TCE", ExamCategory.monogram("TCE-MG"))
    }
}
