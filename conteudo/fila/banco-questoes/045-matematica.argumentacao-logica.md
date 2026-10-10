Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Argumentação lógica** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Estrutura de argumentos, validade, verdade das premissas, conclusão, regras de inferência proposicional, silogismos categóricos e identificação de falácias formais comuns.
Fica de fora (outras matérias tratam): Lógica de predicados avançada, semântica formal de linguagens e história da filosofia.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lógica de argumentação: analogias, inferências, deduções e conclusões.
- Compreensão do processo lógico que, a partir de um conjunto de hipóteses, conduz, de forma válida, a conclusões determinadas.
- Lógica de argumentação.
- Estruturas lógicas e lógica de argumentação.
- Lógica da argumentação e tipos de raciocínio.
- Estruturas lógicas e lógica de argumentação: analogias, inferências, deduções e conclusões.
- Falácias.
- Lógicas de argumentação: analogias, inferências, deduções e conclusões.
- Argumentos por abdução, analogia, dedução e indução.
- Silogismo.
- Estruturas lógicas, argumentação, inferências, deduções e conclusões.
- Estruturas lógicas, argumentação, analogias, inferências, deduções e conclusões.
- Lógica da argumentação.
- Raciocínio Crítico A Prova de Raciocínio Crítico objetiva testar as habilidades de raciocínio, envolvendo: (a) elaboração de argumentos.
- (b) avaliação da argumentação.
- Construção de argumentos: reconhecimento da estrutura básica de um argumento.
- Conclusões apropriadas.
- Erros de raciocínio.
- Argumentos válidos e inválidos.
- Inferências lógicas.
- Lógica de Argumentação - Estrutura do raciocínio lógico.
- Premissas e conclusões.
- Validade de argumentos.
- Argumentação dedutiva e indutiva.
- Identificação de falácias formais e informais.
- Analogias, inferências, deduções e conclusões.
- Argumentação lógica.
- Argumentos válidos e inválidos, inferência lógica, silogismos.
- Estruturas lógicas, lógica de argumentação e inferências.
- Estruturas lógicas, argumentação e inferências.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.argumentacao-logica.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.argumentacao-logica",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
