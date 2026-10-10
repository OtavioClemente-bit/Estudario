Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Raciocínio Lógico e Matemático: Problemas aritméticos, geométricos e matriciais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Quatro operações e expressões; MMC e MDC em ciclos; frações, porcentagens e variações sucessivas; razão, proporção e regra de três simples e composta; médias; princípio fundamental da contagem; perímetros, áreas, circunferência, Pitágoras, volumes de prismas e cilindros; leitura, soma e produto de matrizes e interpretação de tabelas.
Fica de fora (outras matérias tratam): Cálculo diferencial, geometria analítica, trigonometria avançada, determinantes e inversão de matrizes, estatística inferencial e problemas sem dados suficientes.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Raciocínio lógico envolvendo problemas aritméticos, geométricos e matriciais.
- Problemas aritméticos, geométricos e matriciais.
- Raciocínio lógico envolvendo problemas aritméticos.
- Raciocínio lógico envolvendo problemas geométricos.
- Raciocínio lógico envolvendo problemas matriciais.
- Problemas.
- Problemas aritméticos, geométricos e matriciais envolvendo raciocínio lógico.
- Comparações, razão e proporção, regra de três e porcentagem.
- Problemas aritméticos, geométricos, matriciais, de contagem e probabilidade.
- Problemas aritméticos, geométricos, matriciais e de contagem e noções de probabilidade.
- Operações fundamentais: adição, subtração, multiplicação e divisão.
- Problemas aritméticos, geométricos e matriciais, contagem e probabilidade.
- Problemas aritméticos, geométricos, matriciais e de contagem e probabilidade.
- Resolução de situações-problema.
- Lógica Quantitativa - Problemas envolvendo relações entre quantidades.
- Interpretação de gráficos e tabelas e problemas aritméticos, geométricos e matriciais.
- Raciocínio matemático.
- Conjuntos e problemas aritméticos, geométricos e matriciais.
- Problemas aritméticos, geométricos e matriciais; contagem e probabilidade.
- Proporcionalidade direta e inversa, porcentagem e juros.
- Problemas aritméticos, geométricos, matriciais e de contagem.
- Gráficos, tabelas, problemas aritméticos, geométricos e matriciais.
- Gráficos, tabelas e problemas aritméticos, geométricos e matriciais.
- Raciocínio lógico e problemas aritméticos.
- Raciocínio lógico quantitativo.
- Problemas aritméticos, geométricos e matriciais, combinatória e probabilidade.
- Problemas envolvendo códigos, matrizes e cifras.
- As quatro operações fundamentais da Matemática.
- Problemas de raciocínio envolvendo situações do cotidiano.
- Sistemas de equações do primeiro e segundo graus, comparações, razão e proporção, regra de três e porcentagem.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo raciocinio-logico.problemas-aritmeticos.banco-N.json, onde N é o lote)
```json
{
  "materia": "raciocinio-logico.problemas-aritmeticos",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
