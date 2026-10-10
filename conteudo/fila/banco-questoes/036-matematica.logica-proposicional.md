Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Lógica proposicional e tabela-verdade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Proposição, sentença aberta e não proposição; proposições simples e compostas; negação, conjunção, disjunção inclusiva e exclusiva, condicional e bicondicional; tabelas-verdade, número de linhas e avaliação de fórmulas compostas; tautologia, contradição e contingência.
Fica de fora (outras matérias tratam): Equivalências lógicas e leis de negação em profundidade, implicação, quantificadores, argumentos e regras de inferência, que serão tratados na matéria seguinte.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Estruturas lógicas.
- Lógica sentencial (ou proposicional).
- Proposições simples e compostas.
- Lógica sentencial (ou proposicional): Proposições simples e compostas.
- Tabelas-verdade.
- Lógica: proposições, conectivos, equivalências lógicas, quantificadores e predicados.
- Lógica sentencial (ou proposicional): Tabelas-verdade.
- Tabelas verdade.
- Lógica sentencial ou proposicional.
- Lógica sentencial ou proposicional: proposições simples e compostas.
- Proposições, conectivos, equivalências, quantificadores e predicados.
- Proposições, conectivos, equivalências lógicas, quantificadores e predicados.
- Lógica sentencial (ou proposicional): Tabelas‐verdade.
- Lógica sentencial ou proposicional: tabelas-verdade.
- Compreensão de estruturas lógicas.
- Lógica sentencial (ou proposicional): Tabelas verdade.
- Número de linhas da tabela-verdade.
- Tautologia.
- Proposições, tabelas-verdade, equivalências, leis de De Morgan e diagramas lógicos.
- Conectivos lógicos e proposições simples e compostas.
- Estruturas lógicas, argumentação, proposições, tabelas-verdade, equivalências, leis de De Morgan e diagramas.
- Lógica proposicional, tabelas-verdade, equivalências, diagramas e lógica de primeira ordem.
- Lógica proposicional, conectivos, equivalências, quantificadores e predicados.
- Lógica: proposições, conectivos, equivalências, quantificadores e predicados.
- Tautologia, contradição e contingência.
- Lógica proposicional, tabelas-verdade, equivalências, leis de De Morgan e diagramas lógicos.
- Lógica proposicional.
- Tabelas-verdade, equivalências e leis de De Morgan.
- Conectivos lógicos.
- Proposições lógicas simples e compostas.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.logica-proposicional.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.logica-proposicional",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
