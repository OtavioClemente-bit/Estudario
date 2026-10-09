Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Informática: BI, data warehouse, OLAP, ETL e mineração de dados** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Sistemas transacionais (OLTP) e analíticos (OLAP); características do data warehouse (orientado a assunto, integrado, não volátil e variante no tempo); data mart, ODS, data lake e lakehouse; abordagens de Inmon e de Kimball; modelagem dimensional: fatos, dimensões, granularidade, medidas aditivas, semiaditivas e não aditivas, chave substituta, hierarquias, esquemas estrela, floco de neve e constelação, dimensões que mudam lentamente; ETL e ELT, área de preparação e qualidade dos dados; operações OLAP (drill down, roll up, slice, dice, pivot, drill across e drill through) e arquiteturas ROLAP, MOLAP e HOLAP; KDD e CRISP-DM; tarefas de mineração (classificação, regressão, agrupamento, associação com suporte, confiança e lift, detecção de anomalias); algoritmos clássicos (árvore de decisão, k-NN, Naive Bayes, k-means, Apriori) e tipos de aprendizado de máquina.

Fica de fora (outras matérias tratam): Visão geral do ciclo de análise, big data, ética, viés e explicabilidade de IA e transformação digital (estão em informatica.analise-dados-ia); SQL e normalização de bancos relacionais; programação de modelos, redes neurais em detalhe e uso passo a passo de ferramentas comerciais de BI.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Modelagem dimensional.
- Técnicas de modelagem e otimização de bases de dados multidimensionais.
- Técnicas de Integração e Ingestão de Dados (ETL/ELT, Transferência de Arquivos e Integração via Base de Dados).
- Business Intelligence.
- Técnicas e tarefas de mineração de dados.
- Regras de associação.
- Mineração de dados: conceituação e características.
- ETL, manipulação, tratamento e visualização de dados.
- Sistemas transacionais.
- Arquitetura e aplicações de data warehouse com ETL e OLAP.
- Conceitos, fundamentos, características, técnicas e métodos de business intelligence (BI).
- Noções de mineração de dados: conceituação e características.
- Noções de modelagem dimensional. Conceito e aplicações.
- Noções de mineração de dados. Conceituação e características.
- Técnicas e tarefas de mineração de dados. Classificação. Regras de associação.
- Business Intelligence (BI).
- ETL, tratamento, manipulação e visualização de dados.
- OLAP.
- Sistemas de suporte à decisão.
- Conceitos básicos, arquiteturas e aplicações de datawarehousing, ETL, Olap e data mining.
- Arquitetura de business intelligence.
- Mineração de dados.
- Definições e conceitos de data warehouse e data mining.
- Data lake.
- Conceito de DataWarehouse, DataMart, DataLake, DataMesh.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- capítulo sobre integração e ingestão além do ETL/ELT: transferência de arquivos (FTP/SFTP, carga em lote) e integração via base de dados (CDC, replicação, db links, views), com 6 questões
- capítulo sobre otimização de bases multidimensionais: agregações pré-calculadas, índices bitmap, particionamento, tabelas agregadas e visões materializadas, com 6 questões
- seção sobre data mesh (domínios, dados como produto, plataforma self-service, governança federada) e 4 questões
- seção sobre visualização de dados (escolha de gráficos, dashboards, boas práticas) e tratamento/limpeza de dados, com 6 questões
- seção sobre ferramentas de BI (Power BI, Qlik, Tableau) em nível conceitual, dashboards, KPIs e aplicabilidade, com 5 questões
- capítulo sobre pipeline de dados: etapas, orquestração, processamento em lote x fluxo, idempotência, monitoramento e ferramentas de orquestração, com 6 questões
- seção sobre detecção de anomalias: abordagens estatísticas (z-score, IQR), por distância/densidade (LOF), isolation forest e uso em fraude, com 5 questões
- capítulo sobre modelagem física do DW: tabelas, índices (bitmap, B-tree), particionamento, agregados e desempenho de consultas, com 8 questões
- capítulo sobre otimização de bases multidimensionais: agregações, índices bitmap, particionamento e visões materializadas, com 6 questões
- capítulo sobre integração e ingestão: transferência de arquivos (FTP/SFTP, lote) e integração via banco de dados (CDC, replicação), com 6 questões

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "informatica.bi-mineracao-dados",
  "subject": "Informática",
  "title": "BI, data warehouse, OLAP, ETL e mineração de dados",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["BI, data warehouse, OLAP, ETL e mineração de dados"],
  "scope": { "covers": "o que a matéria cobre", "excludes": "o que fica de fora" },
  "theoryTitle": "título completo da teoria",
  "chapters": [ { "title": "1. ...", "markdown": "texto em Markdown (## e ###, listas, **negrito**, tabelas | a | b |, alertas com >)" } ],
  "summary": "resumo em Markdown",
  "flashcards": [ { "front": "pergunta curta", "back": "resposta curta" } ],
  "tips": ["dica 1"],
  "traps": ["pegadinha 1"],
  "activeRecall": [ { "question": "pergunta", "answer": "resposta" } ],
  "errorConcepts": [ { "key": "e1", "title": "nome do erro", "summary": "explicação corretiva" } ],
  "questions": [
    {
      "statement": "enunciado (pergunta ou comando)",
      "format": "MULTIPLE_CHOICE",
      "difficulty": "FACIL",
      "options": [
        { "key": "A", "text": "...", "correct": false },
        { "key": "B", "text": "...", "correct": true },
        { "key": "C", "text": "...", "correct": false },
        { "key": "D", "text": "...", "correct": false },
        { "key": "E", "text": "...", "correct": false }
      ],
      "explanation": "Gabarito B. ...",
      "section": "título EXATO de um dos capítulos",
      "errorConceptKey": "e1",
      "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
    },
    {
      "statement": "afirmação para julgar",
      "format": "TRUE_FALSE",
      "difficulty": "MEDIA",
      "options": [ { "key": "C", "text": "Certo", "correct": false }, { "key": "E", "text": "Errado", "correct": true } ],
      "explanation": "Errado. ...",
      "section": "título EXATO de um dos capítulos",
      "errorConceptKey": null,
      "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
    }
  ],
  "sources": [ { "kind": "OFICIAL", "title": "...", "publisher": "...", "reference": "...", "url": "https://...", "accessedAt": "AAAA-MM-DD" } ]
}
```
- difficulty: "FACIL", "MEDIA" ou "DIFICIL". format: "MULTIPLE_CHOICE" ou "TRUE_FALSE". kind das fontes: "OFICIAL" ou "COMPLEMENTAR".
- IMPORTANTE: escreva cada explicação de forma própria, à mão. Proibido usar frases-molde repetidas entre questões (ex.: "não descreve o critério aplicável", "A análise deve distinguir..."). Em múltipla escolha, explique por que CADA alternativa errada está errada com o conceito específico dela. Não estique alternativas com enfeites como ", segundo o caso apresentado".
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome informatica.bi-mineracao-dados.json.
