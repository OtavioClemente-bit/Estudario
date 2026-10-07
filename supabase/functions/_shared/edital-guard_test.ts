import { assert, assertFalse } from "jsr:@std/assert@1";
import { checkEditalText, isRefusalProposal } from "./edital-guard.ts";

const edital = `EDITAL Nº 1. CONCURSO PÚBLICO PARA O CARGO DE ANALISTA. O candidato fará a inscrição e a prova objetiva.
ANEXO II – CONTEÚDO PROGRAMÁTICO. CONHECIMENTOS BÁSICOS. LÍNGUA PORTUGUESA: 1 Compreensão e interpretação de textos.
RACIOCÍNIO LÓGICO: 1 Estruturas lógicas. NOÇÕES DE DIREITO ADMINISTRATIVO: 1 Estado e administração pública.
NOÇÕES DE DIREITO CONSTITUCIONAL: 1 Constituição. CONHECIMENTOS ESPECÍFICOS: ESTATÍSTICA E ECONOMIA, itens 1 a 20.`;

const boletim = `ADITAMENTO AO BOLETIM INTERNO Nº 187. PRIMEIRA PARTE – SERVIÇOS DIÁRIOS. Oficial de dia: 2º Ten Fulano.
Adjunto: 3º Sgt Beltrano. Comandante da guarda: Cb Sicrano. SEGUNDA PARTE – INSTRUÇÃO: sem alteração.
TERCEIRA PARTE – ASSUNTOS GERAIS E ADMINISTRATIVOS. Férias: concedo trinta dias de férias ao Sd Fulano a contar
de 10 de outubro. Dispensa do serviço por motivo de saúde, conforme atestado médico apresentado. Apresentação
de militar transferido. QUARTA PARTE – JUSTIÇA E DISCIPLINA: sem alteração. Assina o comandante da unidade.`;

Deno.test("edital passa e boletim é barrado antes de gastar cota", () => {
  assert(checkEditalText(edital).ok);
  assertFalse(checkEditalText(boletim).ok);
  assert(checkEditalText("").ok);
  assertFalse(checkEditalText("").judged);
});

Deno.test("proposta de recusa da IA é reconhecida", () => {
  assert(isRefusalProposal({
    subjects: [{ name: "Não há conteúdo de syllabus no documento", topics: [{ title: "O documento contém serviços diários" }] }],
    warnings: [{ message: "Extração recusada: o documento não contém conteúdo de syllabus." }],
  }));
  assertFalse(isRefusalProposal({ subjects: [{ name: "Língua Portuguesa", topics: [1, 2, 3, 4, 5] }], warnings: [] }));
});
