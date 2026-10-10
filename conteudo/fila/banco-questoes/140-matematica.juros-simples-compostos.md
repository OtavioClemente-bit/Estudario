Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Juros simples e compostos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Capital, juros, montante, taxa e prazo em regimes simples e compostos; conversão de taxa e período; cálculos diretos, determinação de variável e comparação entre regimes.
Fica de fora (outras matérias tratam): Descontos simples ou compostos, equivalência avançada de taxas, séries uniformes, amortização, inflação e aportes periódicos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Juros simples.
- Juros compostos.
- Juros simples e compostos.
- Capitalização.
- Juros simples e compostos: capitalização e descontos.
- Cálculo financeiro.
- Juros simples: montante e juros.
- Juros compostos: montante e juros.
- Capitalização contínua.
- Conceitos gerais: valor do dinheiro no tempo, valor presente, valor futuro, juro, taxa de juro, prazo da operação.
- Juros simples: cálculo do montante, dos juros, da taxa, do principal e do prazo.
- Juros compostos: cálculo do montante, dos juros, da taxa, do principal e do prazo.
- Juros simples e compostos: cálculos e aplicações.
- Matemática financeira aplicada: Juros simples.
- Matemática financeira aplicada: Juros compostos.
- Juros compostos. Montante e juros. Taxa real e taxa efetiva. Taxas equivalentes. Capitais equivalentes.
- Montante e juros.
- Conceito de juros e regimes de capitalizações.
- Capitalização simples: cálculo de juros e montantes.
- Capitalização composta: cálculo de juros e montantes.
- Matemática Financeira / Estatística Matemática Financeira: Regimes de capitalização em juros simples e compostos.
- Montante, juros e número de períodos.
- Juros simples e compostos: capitalização e desconto.
- Juros simples e juros compostos.
- Valor do dinheiro no tempo, capital, juros, taxas, capitalização e carência.
- Juros simples e compostos: montante, juros, taxas, principal e prazo.
- Juros simples e compostos, capitalização, descontos e taxas de juros.
- Juros simples e compostos, capitalização, desconto e taxas nominais, efetivas, equivalentes, reais e aparentes.
- Matemática financeira: juros simples e compostos e precificação e operações com títulos públicos federais.
- Juros simples: capitalização.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.juros-simples-compostos.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.juros-simples-compostos",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
