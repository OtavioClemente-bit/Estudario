Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico e Matemático: Operações com conjuntos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 5 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (CEBRASPE, 50 questões):** mais 50 itens de Certo/Errado no estilo Cebraspe, cobrindo outros pontos e casos que o lote 1.
- **Lote 3 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 4 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 5 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Linguagem e operações de conjuntos, cardinalidade e potência, diagramas de Venn de dois e três conjuntos, inclusão-exclusão, conjuntos numéricos, intervalos reais e problemas de pesquisas e equipes.
Fica de fora (outras matérias tratam): Teoria axiomática avançada, cardinalidade de infinitos e combinatória além da necessária para operações e diagramas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Operações com conjuntos.
- Conjuntos e suas operações, diagramas.
- Conjuntos, operações e diagramas.
- Conjuntos.
- Teoria dos conjuntos.
- Operação com conjuntos.
- Conjuntos: linguagem básica, pertinência, inclusão, igualdade, união e interseção.
- Conjuntos e suas operações.
- Noções de conjuntos.
- Diagramas lógicos: conjuntos e elementos.
- Elementos de teoria dos conjuntos.
- Noções de lógica e diagramas lógicos com conjuntos e elementos.
- Noções de lógica e diagramas lógicos: conjuntos e elementos.
- Conjuntos: pertinência, inclusão, união, intersecção, complemento, diferença e problemas.
- Leis de De Morgan aplicadas a conjuntos.
- Teoria dos conjuntos e conjuntos numéricos.
- Conjuntos, operações e representação por diagramas.
- Teoria dos conjuntos e estruturas lógicas: noções de conjuntos, representação, subconjuntos e operações (união, interseção, diferença, complemento).
- Representação de conjuntos.
- Conjuntos unitários, vazio e universo.
- Igualdade, subconjuntos, operações.
- Conjunto e suas operações, diagramas.
- Noções básicas de teoria dos conjuntos.
- Representação e relação: pertinência, inclusão e igualdade.
- Conjuntos, diagramas, números inteiros, racionais e reais e operações.
- Noções de lógica e diagramas lógicos envolvendo conjuntos e elementos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.conjuntos.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.conjuntos",
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
