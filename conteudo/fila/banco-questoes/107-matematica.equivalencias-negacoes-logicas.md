Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Equivalências e negações lógicas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Negação, conjunção, disjunção, condicional, bicondicional e equivalências fundamentais, inclusive leis de De Morgan e contraposição.
Fica de fora (outras matérias tratam): Dedução em sistemas formais, lógica de predicados avançada e demonstrações algébricas extensas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Equivalências.
- Lógica sentencial (ou proposicional): Equivalências.
- Leis de De Morgan.
- Lógica sentencial (ou proposicional): Leis de De Morgan.
- Equivalências lógicas.
- Leis de Morgan.
- Lógica sentencial (ou proposicional): Leis de Morgan.
- Lógica sentencial ou proposicional: equivalências.
- Lógica sentencial ou proposicional: leis de De Morgan.
- Equivalências e negações.
- Tabelas-verdade, equivalências, leis de Morgan e problemas.
- Equivalências lógicas e raciocínio crítico.
- Implicação lógica e contrapositiva.
- Leis de Morgan, tautologia, contradição e contingência.
- Equivalências lógicas e leis de Morgan.
- Equivalências e implicações lógicas.
- Equivalências e leis de De Morgan.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.equivalencias-negacoes-logicas.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.equivalencias-negacoes-logicas",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
