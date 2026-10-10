Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico e Matemático: Operações com conjuntos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.conjuntos.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.conjuntos",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
