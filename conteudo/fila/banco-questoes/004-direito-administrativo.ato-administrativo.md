Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Ato administrativo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 5 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (CEBRASPE, 50 questões):** mais 50 itens de Certo/Errado no estilo Cebraspe, cobrindo outros pontos e casos que o lote 1.
- **Lote 3 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 4 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 5 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito, elementos e vícios, motivação, atributos, vinculação e discricionariedade, classificações, espécies, extinção, convalidação, decadência, Lei nº 9.784/1999, Súmulas 346 e 473 do STF, Súmula Vinculante 3 e Temas 138 e 445 nos pontos pertinentes.
Fica de fora (outras matérias tratam): Procedimento licitatório detalhado, regime integral dos contratos administrativos, processo administrativo disciplinar aprofundado e regras especiais de processo administrativo estadual ou municipal.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ato administrativo: Conceito, requisitos, atributos, classificação e espécies.
- Ato administrativo.
- Atos administrativos.
- Ato administrativo: conceito, requisitos e atributos; anulação, revogação e convalidação; discricionariedade e vinculação.
- Atos administrativos em espécie.
- Atos administrativos simples, complexos e compostos.
- Atos administrativos unilaterais, bilaterais e multilaterais.
- Atos administrativos gerais e individuais.
- Atos administrativos: conceitos, requisitos, elementos, pressupostos e classificação.
- Atos administrativos vinculados e discricionários.
- Fatos da administração pública: atos da administração pública e fatos administrativos.
- Formação do ato administrativo: elementos, procedimento administrativo.
- Teoria dos motivos determinantes.
- Fato e ato administrativo.
- Mérito do ato administrativo, discricionariedade.
- Ato administrativo inexistente.
- Vícios do ato administrativo.
- Classificação dos atos administrativos.
- Elementos e requisitos de validade.
- Validade, eficácia e autoexecutoriedade do ato administrativo.
- Conceito, requisitos, atributos, classificação e espécies.
- Formação e efeitos.
- Conceito, características e atributos.
- Parecer: responsabilidade do emissor do parecer.
- Ato administrativo: conceito, requisitos, atributos, classificação, espécies e invalidação.
- Atos administrativos: fato e ato administrativo.
- Atos administrativos: mérito do ato administrativo, discricionariedade.
- Atos administrativos: teoria dos motivos determinantes.
- Ato administrativo: conceito, requisitos, atributos, classificação, espécies e extinção.
- Atos administrativos: O silêncio no direito administrativo.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.ato-administrativo.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.ato-administrativo",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
