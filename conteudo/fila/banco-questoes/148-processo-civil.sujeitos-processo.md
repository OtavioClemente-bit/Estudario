Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Processual Civil: Sujeitos do processo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Partes e procuradores (capacidade, deveres, litigância de má-fé, ato atentatório, despesas, honorários, gratuidade, procuração, sucessão); litisconsórcio; intervenção de terceiros (assistência, denunciação da lide, chamamento ao processo, desconsideração da personalidade jurídica, amicus curiae); juiz (poderes, deveres, responsabilidade, impedimento e suspeição); auxiliares da justiça; Ministério Público, Defensoria Pública e advocacia pública no processo civil.
Fica de fora (outras matérias tratam): Jurisdição e competência, ação e suas condições, atos e prazos processuais em geral, petição inicial, procedimento comum e recursos, tratados em matérias próprias.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Sujeitos do processo.
- Intervenção de terceiros.
- Litisconsórcio e assistência.
- Sujeitos do processo: Capacidade processual e postulatória.
- Sujeitos do processo: Procuradores.
- Sujeitos do processo: Sucessão das partes e dos procuradores.
- Partes e Procuradores.
- Auxiliares da Justiça.
- Litisconsórcio.
- Intervenção de terceiros: oposição, nomeação à autoria, denunciação à lide e chamamento ao processo.
- Da intervenção de terceiros.
- Sujeitos do processo: Deveres das partes e procuradores.
- Poderes, deveres e responsabilidade do juiz.
- Sujeitos do processo: Litisconsórcio.
- Juiz.
- O juiz.
- Partes e procuradores: capacidade processual e postulatória.
- Amicus curiae.
- Substituição processual.
- Sujeitos do processo: Partes e procuradores.
- Deveres e substituição das partes e procuradores.
- Litisconsórcio e intervenção de terceiros.
- Magistratura.
- Do Ministério Público.
- Advocacia.
- Das partes e procuradores: da capacidade processual e postulatória.
- Do litisconsórcio e da assistência.
- Do Juiz.
- Impedimento e suspeição.
- Capacidade processual e postulatória.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo processo-civil.sujeitos-processo.banco-N.json, onde N é o lote)
```json
{
  "materia": "processo-civil.sujeitos-processo",
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
