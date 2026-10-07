package br.com.estudario.domain.ai

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EditalGuardTest {
    private val edital = """
        EDITAL Nº 1, DE 15 DE JANEIRO DE 2024. CONCURSO PÚBLICO PARA PROVIMENTO DE VAGAS NO CARGO DE ANALISTA.
        O candidato deverá efetuar a inscrição e pagar a taxa de inscrição. A prova objetiva terá 120 itens.
        ANEXO II – CONTEÚDO PROGRAMÁTICO. CONHECIMENTOS BÁSICOS. LÍNGUA PORTUGUESA: 1 Compreensão e interpretação
        de textos de gêneros variados. 2 Reconhecimento de tipos e gêneros textuais. RACIOCÍNIO LÓGICO: 1 Estruturas
        lógicas. NOÇÕES DE DIREITO ADMINISTRATIVO: 1 Estado, governo e administração pública. NOÇÕES DE DIREITO
        CONSTITUCIONAL: 1 Constituição: conceito e classificações. CONHECIMENTOS ESPECÍFICOS: ESTATÍSTICA E ECONOMIA.
    """.trimIndent()

    private val programaSolto = """
        LÍNGUA PORTUGUESA 1. Interpretação de texto. 2. Ortografia oficial. 3. Acentuação gráfica. 4. Crase.
        MATEMÁTICA 1. Conjuntos numéricos. 2. Razão e proporção. 3. Porcentagem. 4. Juros simples e compostos.
        INFORMÁTICA 1. Sistema operacional Windows. 2. Planilhas eletrônicas. 3. Correio eletrônico e navegadores.
        LEGISLAÇÃO 1. Lei nº 8.112/1990. 2. Lei nº 9.784/1999. 3. Lei de Acesso à Informação e transparência pública.
        ATUALIDADES 1. Fatos políticos, econômicos e sociais do Brasil e do mundo nos últimos doze meses.
    """.trimIndent()

    private val boletim = """
        ADITAMENTO AO BOLETIM INTERNO Nº 187. PRIMEIRA PARTE – SERVIÇOS DIÁRIOS. Oficial de dia: 2º Ten Fulano.
        Adjunto: 3º Sgt Beltrano. Comandante da guarda: Cb Sicrano. SEGUNDA PARTE – INSTRUÇÃO: sem alteração.
        TERCEIRA PARTE – ASSUNTOS GERAIS E ADMINISTRATIVOS. Férias: concedo trinta dias de férias ao Sd Fulano a
        contar de 10 de outubro. Dispensa do serviço por motivo de saúde, conforme atestado médico apresentado.
        Apresentação de militar transferido. Escala de serviço para a próxima semana conforme quadro anexo.
        QUARTA PARTE – JUSTIÇA E DISCIPLINA: sem alteração. Assina o comandante da unidade.
    """.trimIndent()

    @Test fun editalPassa() = assertTrue(EditalGuard.check(edital).ok)
    @Test fun anexoDeConteudoProgramaticoPassa() = assertTrue(EditalGuard.check(programaSolto).ok)
    @Test fun boletimEBarrado() = assertFalse(EditalGuard.check(boletim).ok)
    @Test fun pdfSemTextoNaoEJulgado() {
        val verdict = EditalGuard.check("")
        assertTrue(verdict.ok)
        assertFalse(verdict.judged)
    }
}
