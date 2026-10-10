Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Análise de dados e inteligência artificial** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Ciclo de análise; mineração de dados; inteligência de negócios e visualização; big data; aprendizado de máquina supervisionado e não supervisionado; avaliação, sobreajuste, viés, explicabilidade e usos de inteligência artificial; transformação digital orientada por dados.
Fica de fora (outras matérias tratam): Programação detalhada de modelos, demonstrações matemáticas avançadas, treinamento de redes neurais em código e implantação em produto específico.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceitos de Inteligência Artificial, Análise de Dados e Big Data.
- Técnicas para pré-processamento de dados.
- Visualização e análise exploratória de dados.
- Regressão linear e logística, árvores de decisão, random forests, SVM e K-NN.
- Visualização de dados e storytelling.
- Ciência de dados.
- Modelo de referência CRISP-DM.
- Modelagem preditiva. Aprendizado de máquina. Mineração de texto.
- Manipulação, tratamento e visualização de dados.
- Aprendizado supervisionado: regressão, classificação, métricas, regularização e validação de modelos.
- Aprendizado não supervisionado, PCA, K-Means, misturas gaussianas e regras de associação.
- Aprendizado supervisionado: Regressão e Classificação.
- Métodos e etapas de análise de dados.
- Big Data.
- Conceitos básicos de inteligência artificial: aprendizado supervisionado, não supervisionado e por reforço.
- Tendências, projeções e analytics.
- Inteligência artificial, análise de dados e big data.
- Noções de análise de dados.
- Análise de Agrupamentos (Clusterização).
- Análise de agrupamentos (clusterização). Detecção de anomalias.
- Modelagem estatística e aprendizado de máquina: visualização e comunicação de resultados.
- Aprendizado supervisionado e não supervisionado.
- Processos de treinamento, validação e teste.
- Noções de Análise e Mineração de Dados: Estrutura e Organização dos Dados (dados estruturados e não estruturados), Coleta, Tratamento, Armazenamento e Visualização de dados.
- Noções de Inteligência Artificial e Aprendizado de Máquina: Compreensão básica das principais técnicas de aprendizado de máquina, como agrupamento (clustering), classificação, detecção de anomalias.
- Aprendizado não supervisionado: redução de dimensionalidade e PCA.
- K-Means, mistura de Gaussianas e regras de associação.
- Técnicas de classificação, regressão, agrupamento, redução de dimensionalidade e associação.
- Aprendizado não supervisionado, PCA, K-Means, mistura de Gaussianas e regras de associação.
- Métricas de avaliação, overfitting, underfitting, regularização e seleção de modelos.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.analise-dados-ia.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.analise-dados-ia",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
