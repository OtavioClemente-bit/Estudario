Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Poder Executivo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Eleição e posse do Presidente e Vice, presidencialismo, chefias de Estado e Governo, impedimento e sucessão, dupla vacância, competências presidenciais e delegação, decretos e medidas provisórias como competências, crimes comuns e de responsabilidade, garantias, Ministros de Estado, Conselho da República e Conselho de Defesa Nacional.
Fica de fora (outras matérias tratam): Organização dos demais Poderes, processo legislativo em geral, administração pública em geral, política externa e defesa como matérias autônomas, exceto referências indispensáveis às competências presidenciais, à responsabilidade e aos Conselhos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Poder Executivo.
- Poder Executivo: atribuições e responsabilidades do presidente da República.
- Do Poder Executivo: do Presidente e do Vice-Presidente da República, Das atribuições e responsabilidades do Presidente da República.
- Atribuições e responsabilidades do Presidente da República.
- Poder Executivo: Atribuições do presidente da República e dos ministros de Estado.
- Poder Executivo: forma e sistema de governo.
- Chefia de Estado e chefia de governo.
- Das atribuições e responsabilidades do Presidente da República.
- Organização dos poderes no Estado: Poder executivo.
- Do Poder Executivo: do Presidente e do Vice-Presidente da República.
- Poder executivo: Ministros de Estado.
- Ministros de Estado.
- Constituição da República Federativa do Brasil de 1988: Poder Executivo.
- Poder Executivo: Do Presidente e do Vice-Presidente da República.
- Poder Executivo: Das Atribuições e Responsabilidades do Presidente da República.
- Poder Executivo: Da Responsabilidade do Presidente da República.
- Poder Executivo: Dos Ministros de Estado.
- Poder executivo. Atribuições e responsabilidades do Presidente da República.
- Poder executivo: Presidente da República.
- Presidente da República: Atribuições, prerrogativas e responsabilidades.
- Poder Executivo: Chefia de Estado e chefia de governo.
- Atribuições e responsabilidade do Presidente da República.
- Poder Executivo: Presidente da República, atribuições, prerrogativas e responsabilidades.
- Presidencialismo como sistema de governo: noções gerais, capacidades governativas e especificidades do caso brasileiro.
- Do Poder Executivo: das atribuições e responsabilidades do Governador do Estado.
- Poder Executivo: forma e sistema de governo e chefias de Estado e de governo.
- Poder Executivo: Presidente da República: atribuições, prerrogativas e responsabilidades.
- Do Poder Executivo.
- Chefia de Estado e de governo.
- Organização dos Poderes: Poder Executivo e Poder Judiciário.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.poder-executivo.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.poder-executivo",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
