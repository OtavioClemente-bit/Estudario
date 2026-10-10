Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Análise combinatória** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Princípios aditivo e multiplicativo, fatorial, permutações simples e com repetição, arranjos, combinações e contagens em etapas.
Fica de fora (outras matérias tratam): Probabilidade, análise assintótica e combinatória avançada de grafos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Princípios de contagem e probabilidade.
- Princípios de contagem.
- Análise combinatória.
- Análise combinatória, conjuntos numéricos e sistemas de equações do primeiro e segundo graus.
- Combinações.
- Arranjos e permutações.
- Análise combinatória e probabilidade: arranjos, combinações, permutações simples, probabilidade de um evento e resolução de problemas.
- Análise combinatória: princípio fundamental da contagem, arranjos, permutações e combinações.
- Binômio de Newton.
- Problemas de contagem.
- Técnicas de Contagem e Análise Combinatória: Combinações Simples, Arranjos e Permutação com e sem repetição.
- Princípios fundamentais de contagem.
- Arranjos, permutações, combinações.
- Combinações, arranjos e permutação.
- Contagem, probabilidade e geometria básica.
- Problemas de contagem e probabilidade.
- Contagem e análise combinatória; binômio de Newton.
- Análise combinatória: princípio fundamental da contagem.
- Permutação simples e com repetição.
- Arranjos e combinações.
- Fatorial.
- Permutação.
- Combinação.
- Arranjo.
- Noções básicas de contagem.
- Contagem: princípio fundamental da contagem, permutações, arranjos e combinações.
- Matrizes: Análise combinatória.
- Matrizes: Binômio de Newton.
- Princípios simples de contagem e probabilidade.
- Análise combinatória e conjuntos numéricos.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.analise-combinatoria.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.analise-combinatoria",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
