Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Poder Legislativo e estatuto dos congressistas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Congresso Nacional, Câmara dos Deputados e Senado Federal: composição e competências (CF, arts. 44 a 52), sessões, comissões e CPI; estatuto dos congressistas, imunidades, incompatibilidades e perda do mandato.
Fica de fora (outras matérias tratam): Processo de elaboração das espécies normativas, iniciativa, tramitação de projetos, sanção, veto e medidas provisórias; fiscalização financeira em profundidade; organização de Poderes e direitos fundamentais fora do estatuto parlamentar.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Poder Legislativo.
- Comissões parlamentares de inquérito.
- Poder legislativo: Funcionamento e atribuições.
- Do Poder Legislativo: do Congresso Nacional, das atribuições do Congresso Nacional, da Câmara dos Deputados e do Senado Federal.
- Poder legislativo: Estrutura, funcionamento e atribuições.
- Poder legislativo: Estrutura.
- Poder legislativo: Comissões parlamentares de inquérito.
- Organização dos poderes no Estado: Poder legislativo.
- Poder Legislativo: estrutura, funcionamento, atribuições e processo legislativo.
- Poder legislativo: Prerrogativas parlamentares.
- Poder legislativo: Congresso nacional, câmara dos deputados, senado federal, deputados e senadores.
- Constituição da República Federativa do Brasil de 1988: Poder Legislativo.
- Poder Legislativo: Do Congresso Nacional e suas Atribuições.
- Poder Legislativo: Da Câmara dos Deputados e dos Deputados.
- Poder Legislativo: Do Senado Federal e dos Senadores.
- Poder Legislativo: Das Reuniões e das Comissões.
- Prerrogativas parlamentares.
- Poder Legislativo: o Congresso Nacional e suas Casas (atribuições, competências, reuniões e comissões).
- Regime jurídico-constitucional dos parlamentares.
- Poder Legislativo: estrutura, funcionamento, atribuições, processo legislativo e fiscalização contábil, financeira e orçamentária.
- Congresso Nacional, Câmara dos Deputados, Senado Federal, deputados e senadores.
- Poder legislativo. Estrutura.
- Funcionamento e atribuições do Poder legislativo.
- Organização dos poderes no Estado: Comissões parlamentares de inquérito.
- Poder Legislativo: fundamento, atribuições e garantias de independência.
- Poder Legislativo, Congresso Nacional, Câmara dos Deputados, Senado Federal, deputados e senadores.
- Do Poder Legislativo: atribuições e responsabilidades das Casas Legislativas dos Estados.
- Estatuto dos parlamentares.
- Da Organização dos Poderes: Do Poder Legislativo.
- Poder Legislativo: fundamentos, atribuições e garantias de independência.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.poder-legislativo.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.poder-legislativo",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
