Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Orçamento Público: Lei n. 4.320/1964** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Lei n. 4.320/1964: estrutura e princípios da Lei de Orçamento; receita pública; classificação e execução da despesa; créditos adicionais; programação e execução do orçamento, com casos aplicados à administração judiciária.
Fica de fora (outras matérias tratam): Lei Complementar n. 101/2000 em profundidade, regras gerais de licitações e contratos, classificação completa da receita e da despesa conforme manuais atuais e contabilidade patrimonial não necessária à compreensão dos artigos do edital.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei nº 4.320/1964.
- Créditos adicionais.
- Programação e execução orçamentária e financeira.
- Execução orçamentária e financeira.
- Orçamento público no Brasil: Títulos I, IV, V e VI da Lei nº 4.320/1964.
- Créditos ordinários e adicionais.
- Lei nº 4.320/1964 e alterações.
- O orçamento público no Brasil: Créditos ordinários e adicionais.
- Programação e execução orçamentária e financeira: Alterações orçamentárias.
- Despesa pública: Dívida flutuante e fundada.
- Alterações orçamentárias.
- Lei Federal nº 4.320/1964 e suas alterações.
- Lei nº 4.320/64.
- Acompanhamento da execução.
- Dívida flutuante e fundada.
- Créditos adicionais, especiais, extraordinários, ilimitados e suplementares.
- Programação de desembolso e mecanismos retificadores do orçamento.
- Leis de Créditos Adicionais.
- Fundos Especiais de Despesa.
- Normas gerais de Direito Financeiro (Lei federal nº 4.320/1964).
- Lei nº 4.320/1964: Lei de Orçamento.
- Lei nº 4.320/1964: proposta orçamentária.
- Lei nº 4.320/1964: elaboração da Lei de Orçamento.
- Lei nº 4.320/1964: exercício financeiro.
- Lei nº 4.320/1964: créditos adicionais.
- Lei nº 4.320/1964: execução do Orçamento.
- Lei nº 4.320/1964: fundos especiais.
- Lei nº 4.320/1964: controle da execução orçamentária.
- Lei nº 4.320/1964: autarquias e outras entidades.
- Lei nº 4.320/1964: disposições finais.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo orcamento-publico.320-1964.banco-N.json, onde N é o lote)
```json
{
  "materia": "orcamento-publico.320-1964",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
