Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Estatística descritiva** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
População, amostra, variáveis, frequências absoluta e relativa, tabelas, gráficos de barras, setores, linhas e histogramas, leitura e interpretação, média aritmética e ponderada, mediana, moda, amplitude, variância e desvio padrão.
Fica de fora (outras matérias tratam): Inferência estatística, estimação, testes de hipóteses, distribuições de probabilidade, regressão e medidas para dados agrupados que exijam interpolação além de estimativas por ponto médio.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Noções de estatística: média, moda, mediana e desvio padrão.
- Noções de Estatísticas: medidas de tendência central (moda, mediana, média aritmética simples e ponderada) e de dispersão (desvio médio, amplitude, variância, desvio padrão).
- Histogramas e curvas de frequência.
- Estatística descritiva e análise exploratória de dados: gráficos, diagramas, tabelas, medidas descritivas (posição, dispersão, assimetria e curtose).
- Estatística: média, moda, mediana e desvio padrão.
- Estatística descritiva.
- Medidas de posição: média, moda, mediana e quartis.
- Gráficos, diagramas, tabelas, medidas descritivas (posição, dispersão, assimetria e curtose).
- Medidas de dispersão: amplitude, variância, desvio-padrão, coeficiente de variação, amplitude interquartil.
- Análise exploratória de dados.
- Descrição univariada: população e amostra; estatística descritiva e inferencial; variáveis estatísticas e níveis de mensuração.
- Dados em série e agrupados; distribuições de frequência, histogramas e polígonos de frequências.
- Medidas de tendência central, variabilidade absoluta e relativa, assimetria e curtose.
- Métodos para sumarização e análise exploratória de dados.
- Variáveis quantitativas e qualitativas.
- Estatística descritiva e análise exploratória de dados.
- Organização e apresentação de variáveis.
- Estatística descritiva, medidas de tendência central e dispersão, histogramas e gráficos.
- Conceitos gerais: variável, tipos de variáveis.
- Frequências: absoluta e relativa, frequências acumuladas.
- Medidas de tendência central (em dados brutos ou agrupados em classes): média aritmética, média geométrica, média ponderada, moda e mediana.
- Medidas de Posição: quartis e percentis.
- Medidas de dispersão (em dados brutos ou agrupados em classes): amplitude, variância, desvio padrão e coeficiente de variação.
- Representação tabular e gráfica.
- Medidas de dispersão: amplitude, amplitude interquartil, variância, desvio padrão e coeficiente de variação.
- Medidas de posição: média, moda, mediana e separatrizes.
- Distribuição de frequências: absoluta, relativa, acumulada.
- Noções de estatística: média, moda, mediana e desvio-padrão.
- Média, moda, mediana e desvio padrão.
- Medidas de Tendência Central.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.estatistica-descritiva.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.estatistica-descritiva",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
