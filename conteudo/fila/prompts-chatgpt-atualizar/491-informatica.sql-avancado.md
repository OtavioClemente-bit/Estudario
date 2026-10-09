Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Informática: SQL avançado** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Normalização aprofundada (1FN, 2FN, 3FN, FNBC, dependências funcionais e desnormalização); views comuns, atualizáveis e materializadas; índices (B-tree, compostos, únicos, custo de escrita, plano de execução); triggers (BEFORE, AFTER, INSTEAD OF, por linha e por comando, OLD e NEW); funções e stored procedures; transações (COMMIT, ROLLBACK, SAVEPOINT), níveis de isolamento e anomalias de concorrência.

Fica de fora (outras matérias tratam): Conceitos básicos de banco de dados, modelo relacional, SELECT, junções, agregação, NULL e noções de ACID, data warehouse e ETL (estão em informatica.banco-dados-sql); administração e ajuste fino de produtos específicos.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Tunning de banco de dados.
- Técnicas de análise de desempenho e otimização de consultas SQL.
- Noções para Otimização de Performance em Larga Escala.
- Tabelas, visões (views) e índices.
- Técnicas de análise de desempenho e otimização de consultas (tuning).
- SQL ANSI, linguagens procedurais embarcadas e processamento de transações.
- SQL (Procedural Language / Structured Query Language).
- Views, funções, stored procedures, triggers, segurança e conexões.
- Técnicas para detecção de problemas e otimização de desempenho do SGBD e de consultas SQL.
- Criação e manipulação de visões (view).
- Criação, manutenção e execução de stored procedures, funções, packages e triggers.
- Triggers, funções, stored procedures.
- Linguagem Transact-SQL (TSQL), PLSQL.
- Gatilho (trigger).
- Visão (view).
- Function e stored procedures.
- Cursores.
- Tuning em Banco de Dados.
- SQL: consultas, subconsultas, gatilhos, visões, funções, procedimentos armazenados e cursores.
- Detecção de problemas e otimização de desempenho de SGBD e consultas SQL.
- SQL ANSI, consultas, procedures, packages, funções, triggers e views.
- Implementação de SGBD, transações, propriedades e processamento.
- Otimização e concorrência de consultas, recuperação, segurança, distribuição de dados e transações.
- Ajuste de desempenho de bancos de dados, aplicações e comandos SQL.
- Transações e continuidade de operação.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- capítulo sobre tuning de consultas e do SGBD (plano de execução, estatísticas, reescrita de consultas, uso de índices, particionamento, cache) e 8 questões
- capítulo sobre tuning de banco de dados (diagnóstico de lentidão, plano de execução, índices, estatísticas, parâmetros do SGBD) e 8 questões
- capítulo sobre análise de desempenho e otimização de consultas SQL (EXPLAIN, plano de execução, reescrita, junções, índices) e 8 questões
- capítulo sobre análise de desempenho e otimização de consultas (tuning), com plano de execução, estatísticas e reescrita, e 8 questões
- capítulo sobre PL/SQL (blocos anônimos, variáveis, estruturas de controle, cursores, exceções, packages) e 8 questões
- seção sobre segurança e conexões (GRANT e REVOKE, papéis, privilégios em views e procedures, pool de conexões) e 5 questões
- capítulo sobre detecção de problemas de desempenho e otimização do SGBD e de consultas SQL (plano de execução, bloqueios, estatísticas, índices) e 8 questões
- capítulo sobre PL/SQL (blocos, variáveis, cursores, exceções, packages) e 8 questões
- seção sobre estruturas de busca e indexação: acesso sequencial, árvores B e B+, hashing e bitmaps, com 8 questões
- seção sobre T-SQL e PL/SQL (variáveis, controle de fluxo, cursores, tratamento de erros, blocos) e 6 questões

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
  "id": "informatica.sql-avancado",
  "subject": "Informática",
  "title": "SQL avançado",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["SQL avançado"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome informatica.sql-avancado.json.
