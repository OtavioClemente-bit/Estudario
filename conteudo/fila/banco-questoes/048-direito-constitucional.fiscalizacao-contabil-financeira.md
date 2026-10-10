Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Fiscalização contábil, financeira e orçamentária (arts. 70 a 75)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Arts. 70 a 75 da Constituição Federal: cinco dimensões de fiscalização, parâmetros e dever de prestar contas, controle externo federal, competências e composição do TCU, controle interno, prestação de contas, decisões, atos normativos, simetria estadual e distrital, controle municipal, EC 139/2026 e precedentes pertinentes.
Fica de fora (outras matérias tratam): Processo orçamentário em geral, controle judicial como matéria autônoma, improbidade administrativa e regime de responsabilidade fiscal; referências pontuais apenas para delimitar competências e efeitos das decisões.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Fiscalização contábil, financeira e orçamentária.
- Poder Legislativo: Fiscalização Contábil, Financeira e Orçamentária.
- Controle da atividade financeira do Estado: espécies e sistemas.
- Da fiscalização contábil, financeira e orçamentária.
- Poder legislativo: Tribunal de Contas da União (TCU).
- Tribunal de Contas da União (TCU) e suas atribuições; entendimentos com caráter normativo exarados pelo TCU.
- Tribunal de Contas da União e suas atribuições.
- Fiscalização financeira e orçamentária.
- Fiscalização e controle interno e externo dos orçamentos.
- Fiscalização contábil, financeira, orçamentária, operacional e patrimonial.
- Fiscalização contábil, financeira e orçamentária e comissões parlamentares de inquérito.
- Fiscalização contábil, financeira e orçamentária operacional e patrimonial.
- Tribunais de Contas na Constituição Federal: regime jurídico, estrutura, composição, competência, natureza jurídica e eficácia das decisões.
- Poder Legislativo e os tribunais de contas.
- Controle interno e os tribunais de contas.
- Fiscalização contábil, financeira e orçamentária, controle externo e sistema de controle interno.
- Poder Legislativo: Da Fiscalização Contábil, Financeira e Orçamentária.
- O controle externo e os sistemas de controle interno.
- Tribunal de Contas da União.
- Regras constitucionais sobre controle externo: fiscalização contábil, financeira, orçamentária, operacional e patrimonial.
- Poder Legislativo: fiscalização contábil e financeira.
- Controle, fiscalização e prestação de contas.
- Tribunal de Contas da União (TCU).
- Jurisprudência do STF sobre o controle externo.
- Súmula 347 do Supremo Tribunal Federal e os Tribunais de Contas dos Estados.
- Controle externo - fiscalização contábil, financeira e orçamentária.
- Tribunal de Contas: composição e competências.
- Administração Pública e Poder Legislativo: fiscalização contábil e financeira.
- Orçamento público: Fiscalização financeira e orçamentária.
- Tribunal de Contas da União (TCU), dos estados e do Distrito Federal e suas atribuições.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.fiscalizacao-contabil-financeira.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.fiscalizacao-contabil-financeira",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
