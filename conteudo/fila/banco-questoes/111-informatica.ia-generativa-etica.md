Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: IA, aprendizado de máquina, IA generativa e ética** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito de inteligência artificial e sua relação com aprendizado de máquina, aprendizado profundo e IA generativa; IA estreita, geral e superinteligência; IA simbólica (sistemas especialistas) e conexionista; teste de Turing e marcos históricos; subáreas (PLN, visão computacional, voz, recomendação); visão rápida dos tipos de aprendizado, incluindo autossupervisionado; neurônio artificial, pesos, viés e funções de ativação (degrau, sigmoide, tanh, ReLU, softmax); perceptron e XOR, MLP, retropropagação, gradiente descendente e taxa de aprendizado; CNN, RNN, LSTM e transformer com atenção; IA generativa: modelos discriminativos e generativos, LLM, tokens, embeddings, previsão do próximo token, pré-treinamento, ajuste fino, RLHF, janela de contexto, temperatura, data de corte, alucinação, RAG, GAN, difusão e multimodalidade; engenharia de prompt (zero-shot, few-shot, cadeia de raciocínio) e ferramentas de mercado; ética da IA (transparência, explicabilidade, justiça, privacidade, responsabilidade, supervisão humana, segurança, sustentabilidade); fontes de viés; riscos da IA generativa (vazamento, injeção de prompt, envenenamento, deepfakes, direitos autorais); LGPD e decisões automatizadas; regulação baseada em risco e normas de gestão de IA; uso de IA no setor público.
Fica de fora (outras matérias tratam): Data warehouse, OLAP, ETL, CRISP-DM, tarefas e algoritmos clássicos de mineração (árvore de decisão, k-NN, Naive Bayes, k-means, Apriori) e métricas de associação (estão em informatica.bi-mineracao-dados); ciclo de análise de dados, big data, métricas de avaliação de modelos e sobreajuste em detalhe (estão em informatica.analise-dados-ia); programação de modelos em Python ou bibliotecas específicas, matemática do gradiente, PLN clássico em detalhe (estemização, n-gramas, modelagem de tópicos) e texto artigo por artigo de projetos de lei em tramitação.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceitos básicos de inteligência artificial.
- Aprendizado de máquina, inteligência artificial e processamento de linguagem natural.
- Técnicas de prompts.
- Inteligência Artificial: fundamentos e aplicações: conceitos de inteligência artificial.
- Aprendizado da máquina.
- Introdução aos modelos generativos e modelos de linguagem.
- Ética, governança e privacidade em IA.
- Noções de aprendizado de máquina.
- Inteligência artificial e Direito.
- Inovação na gestão pública: Inteligência Artificial.
- Redes neurais, funções de ativação, gradiente, backpropagation, regularização e CNN.
- Inteligência artificial, aprendizado de máquina e sistemas de recomendação.
- Inteligência artificial: conceitos, tipos, modelos, ética e desafios.
- Inteligência Artificial.
- Deep learning.
- Redes neurais.
- Conceitos básicos de inteligência artificial: engenharia de prompts.
- Conceitos básicos de inteligência artificial: IA generativa: conceitos, exemplos e casos de uso.
- Ética e responsabilidade digital no serviço público.
- Inteligência Artificial (IA).
- Conceitos de Machine Learning.
- Principais ferramentas de mercado (Copilot, ChatGPT, META).
- Noções de aprendizado de máquina, inteligência artificial.
- Inteligência Artificial Generativa.
- Compreensão básica de Grandes Modelos de Linguagem (LLM) e de engenharia de prompt.
- Aprendizado de máquina, processamento de linguagem natural, visão computacional e deep learning.
- Transparência e imparcialidade nos usos da inteligência artificial no âmbito do serviço público.
- Impacto da Inteligência Artificial nos processos de trabalho.
- A ética na produção de conteúdo com inteligência artificial generativa.
- Noções de processamento de linguagem natural.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.ia-generativa-etica.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.ia-generativa-etica",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
