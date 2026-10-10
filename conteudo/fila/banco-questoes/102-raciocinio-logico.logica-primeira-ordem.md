Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico: Lógica de primeira ordem** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Predicados e sentenças abertas; universo de discurso e conjunto-verdade; quantificadores universal e existencial em notação simbólica; variáveis livres e ligadas; valor lógico de proposições quantificadas em universos finitos e numéricos; verdade por vacuidade; negação de proposições quantificadas, inclusive com condicional, conjunção e quantificadores encadeados; quantificadores múltiplos e a importância da ordem; tradução entre português e fórmulas (todo, algum, nenhum, somente, existe exatamente um); distribuição dos quantificadores sobre conjunção e disjunção; validade de argumentos com instanciação e generalização e construção de contraexemplos.
Fica de fora (outras matérias tratam): Leitura básica de todo, algum e nenhum com diagramas de Venn (matematica.quantificadores-diagramas); conectivos, tabelas-verdade e classificação de fórmulas proposicionais (matematica.logica-proposicional); sistemas formais de dedução completos, teoremas de completude e indecidibilidade, lógica de segunda ordem e lógicas modais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lógica de primeira ordem.
- Lógica de primeira ordem, operações com conjuntos e problemas aritméticos, geométricos e matriciais.
- Diagramas lógicos e lógica de primeira ordem.
- Sentenças abertas.
- Diagramas e lógica de primeira ordem.
- Quantificadores, afirmações e negações.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.logica-primeira-ordem.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.logica-primeira-ordem",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
