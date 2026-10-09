==================================================
# MATÉRIA 153 — direito-penal.teoria-do-crime
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal: Teoria do crime** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Conceitos de infração penal e crime; fato típico, tipicidade, conduta, resultado, nexo causal; dolo e culpa; consumação e tentativa; desistência voluntária, arrependimento eficaz e posterior; ilicitude e causas de justificação.

Fica de fora (outras matérias tratam): Imputabilidade e concurso de pessoas, penas, processo penal e crimes em espécie.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Culpabilidade.
- O fato típico e seus elementos.
- Excesso punível.
- Crime impossível.
- Desistência voluntária e arrependimento eficaz.
- Crime consumado e tentado.
- Arrependimento posterior.
- Ilicitude e causas de exclusão.
- Relação de causalidade.
- Pena da tentativa.
- Crime.
- Ilicitude.
- Consumação e tentativa.
- Agravação pelo resultado.
- Descriminantes putativas.
- Dolo e culpa.
- Superveniência de causa independente.
- Erro de tipo.
- Crime doloso, culposo e preterdoloso.
- Relevância da omissão.
- Causas de exclusão da culpabilidade.
- Erro de proibição.
- Teorias do crime.
- Erro sobre a pessoa.
- Resultado.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal.teoria-do-crime",
  "subject": "Direito Penal",
  "title": "Teoria do crime",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Teoria do crime"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal.teoria-do-crime.json.

==================================================
# MATÉRIA 154 — direito-penal.aplicacao-lei-penal
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal: Aplicação da lei penal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Legalidade e anterioridade; interpretação e integração; sucessão de leis penais; tempo e lugar do crime; territorialidade, extraterritorialidade e contagem de prazo penal.

Fica de fora (outras matérias tratam): Teoria geral do crime, espécies de pena e crimes em espécie.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Aplicação da lei penal.
- Tempo e lugar do crime.
- Territorialidade e extraterritorialidade da lei penal.
- A lei penal no tempo e no espaço.
- Irretroatividade da lei penal.
- Contagem de prazo.
- Analogia.
- Lei penal excepcional, especial e temporária.
- Pena cumprida no estrangeiro.
- Interpretação da lei penal.
- Eficácia da sentença estrangeira.
- Lei penal no tempo e no espaço.
- Princípios básicos.
- Aplicação da lei penal: princípios da legalidade e da anterioridade.
- Aplicação da lei penal: Tempo e lugar do crime.
- Princípios aplicáveis ao direito penal.
- Princípios básicos do Direito Penal.
- Aplicação da lei penal: Irretroatividade da lei penal.
- Conflito aparente de normas penais.
- Princípios da legalidade e da anterioridade.
- Aplicação da lei penal: A lei penal no tempo e no espaço.
- Aplicação da lei penal: Interpretação da lei penal.
- Aplicação da lei penal: Analogia.
- Frações não computáveis da pena.
- Aplicação da lei penal: Conflito aparente de normas penais.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal.aplicacao-lei-penal",
  "subject": "Direito Penal",
  "title": "Aplicação da lei penal",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Aplicação da lei penal"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal.aplicacao-lei-penal.json.

==================================================
# MATÉRIA 155 — processo-penal.jurisdicao-competencia-sujeitos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Processo Penal: Jurisdição, competência e sujeitos do processo penal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Aplicação da lei processual penal no tempo e no espaço; jurisdição e competência (lugar da infração, domicílio do réu, natureza da infração, prerrogativa de função, conexão e continência, prevenção e distribuição, incompetência e conflito); juiz, Ministério Público, acusado, defensor, assistente de acusação e auxiliares da justiça.

Fica de fora (outras matérias tratam): Inquérito policial, ação penal, provas, prisões, citações, sentença, procedimentos, nulidades e recursos em profundidade (há matérias próprias).

Os editais pedem este assunto assim (cubra todos estes pontos):
- Aplicação da lei processual no tempo, no espaço e em relação às pessoas.
- Competência.
- Disposições preliminares do Código de Processo Penal.
- Jurisdição.
- Conexão e continência.
- Lei processual penal: fontes, eficácia, interpretação, analogia, imunidades.
- Juiz, Ministério Público, acusado, defensor, assistentes e auxiliares da justiça.
- Juiz, Ministério Público, acusado e defensor.
- Disposições gerais do Código de Processo Penal.
- Aplicação da lei processual penal.
- Perpetuatio jurisdictionis.
- Assistentes e auxiliares da justiça.
- Sujeitos da relação processual.
- Incompetência.
- Competência: Critérios de determinação e modificação.
- Conflito de competência.
- Impedimentos e suspeições.
- Juiz, ministério público, acusado, defensor, assistentes e auxiliares da justiça, atos de terceiros.
- Sujeitos do processo: juiz, Ministério Público, acusado e seu defensor, assistente, auxiliares da justiça, peritos e intérpretes, serventuários da justiça, impedimentos e suspeições.
- Sujeitos do processo: juiz, Ministério Público, acusado e seu defensor, assistente, curador do réu menor, auxiliares da justiça, assistentes, peritos e intérpretes, serventuários da justiça, impedimentos e suspeições.
- Aplicação da lei processual no tempo, no espaço e em relação às pessoas: disposições preliminares do Código de Processo Penal.
- Repartição constitucional de competência.
- Serventuários da justiça.
- Aplicação da lei processual penal no tempo, no espaço e em relação às pessoas; disposições preliminares do Código de Processo Penal.
- Juiz, Ministério Público, acusado, defensor, assistentes, auxiliares da Justiça e atos de terceiros.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-penal.jurisdicao-competencia-sujeitos",
  "subject": "Processo Penal",
  "title": "Jurisdição, competência e sujeitos do processo penal",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Jurisdição, competência e sujeitos do processo penal"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-penal.jurisdicao-competencia-sujeitos.json.

==================================================
# MATÉRIA 156 — legislacao-penal-especial.execucao-penal
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: Execução penal (LEP)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Lei 7.210/1984 (LEP): objetivos e princípios da execução, a quem se aplica, classificação e exame criminológico, órgãos da execução e estabelecimentos penais; deveres e direitos do preso, direitos restringíveis, assistência, trabalho interno e externo; remição por trabalho e estudo e perda de dias remidos; disciplina, faltas leves, médias e graves, sanções, isolamento preventivo e regime disciplinar diferenciado; regimes fechado, semiaberto e aberto, progressão com as frações atuais, progressão especial da gestante e da mãe, regressão, prisão domiciliar no aberto, permissão de saída e saída temporária; noções de livramento condicional na execução, agravo em execução e súmulas mais cobradas.

Fica de fora (outras matérias tratam): Teoria geral da pena, espécies de pena e dosimetria (direito-penal.penas), Regras de Mandela (direito.regras-minimas-tratamento-presos), detalhes dos crimes hediondos e da Lei de Drogas, medidas de segurança em profundidade, indulto e anistia em detalhe e regulamentos penitenciários estaduais.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Execução Penal.
- Execução das penas em espécie e incidentes de execução.
- Incidentes da execução.
- Execução Penal (Lei nº 7.210/1984).
- Conselho Nacional de Política Criminal e Penitenciária (arts. 62 a 64 da Lei de Execução Penal).
- Conselhos Penitenciários (arts. 69 e 70 da Lei de Execução Penal).
- Conselhos da Comunidade (arts. 80 e 81 da Lei de Execução Penal).
- Lei de execução penal.
- Livramento condicional.
- Lei nº 7.210/1984 e suas alterações (Lei de Execução Penal).
- Regimes de cumprimento da pena.
- Execução das penas em espécie.
- Lei nº 7.210/1984 e suas alterações (Execução Penal).
- Remição.
- Agravo em execução penal.
- Penas: execução das penas em espécie e incidentes de execução.
- Medidas de segurança: execução das medidas de segurança.
- Suspensão condicional da pena.
- Lei nº 7.210/1984 e alterações (execução penal).
- Regime Disciplinar Diferenciado.
- Monitoramento eletrônico.
- Execução penal (Lei nº 7.210/1984 e suas alterações).
- Execução das penas privativas de liberdade, restritivas de direito e das medidas de segurança.
- Lei Federal nº 7.210/1984 - Lei de Execução Penal.
- Órgãos da execução penal.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.execucao-penal",
  "subject": "Legislação Penal Especial",
  "title": "Execução penal (LEP)",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Execução penal (LEP)"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.execucao-penal.json.

==================================================
# MATÉRIA 157 — processo-penal.prova
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Processual Penal: Provas no processo penal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Finalidade e avaliação da prova, ônus probatório, elementos informativos da investigação, prova ilícita e derivada, cadeia de custódia, perícia, interrogatório, testemunhas e reconhecimento de pessoas.

Fica de fora (outras matérias tratam): Procedimentos especiais completos, recursos e execução penal, além de técnicas investigativas sem relação direta com a admissibilidade e avaliação probatória.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Indícios.
- Reconhecimento de pessoas e coisas.
- Provas ilícitas.
- Acareação.
- Documentos de prova.
- Ônus da prova.
- Provas: Conceito, objeto, classificação e sistemas de avaliação.
- Meios de prova: perícias, interrogatório, confissão, testemunhas, reconhecimento de pessoas e coisas, acareação, documentos, indícios.
- Provas.
- Valoração da prova.
- Requisitos e ônus da prova.
- Nulidade da prova.
- Interrogatório do acusado.
- Testemunhas.
- Princípios gerais da prova, procedimento probatório.
- Provas (Título VII do Código de Processo Penal).
- Procedimento probatório.
- Presunções.
- Confissão.
- Exame do corpo de delito e perícias em geral.
- Provas: Princípios gerais da prova, procedimento probatório.
- Valoração.
- Prova: Exame do corpo de delito e perícias em geral.
- Qualificação e oitiva do ofendido.
- Prova: do exame de corpo de delito e das perícias em geral.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-penal.prova",
  "subject": "Direito Processual Penal",
  "title": "Provas no processo penal",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Provas no processo penal"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-penal.prova.json.

==================================================
# MATÉRIA 158 — processo-penal.citacoes-sentenca-procedimentos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Processo Penal: Citações, sentença, procedimentos e Tribunal do Júri** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Citação (pessoal, por hora certa, por edital, por carta precatória e rogatória), suspensão do processo e da prescrição, revelia e intimações; sentença penal: requisitos, absolvição e condenação, correlação entre acusação e sentença, emendatio e mutatio libelli; procedimento comum ordinário, sumário e sumaríssimo; Tribunal do Júri: primeira fase, pronúncia, impronúncia, desclassificação, absolvição sumária, plenário, quesitos e desaforamento.

Fica de fora (outras matérias tratam): Ação penal, inquérito, provas e prisões em profundidade; nulidades, recursos, habeas corpus e revisão criminal (há matérias próprias).

Os editais pedem este assunto assim (cubra todos estes pontos):
- Atos processuais.
- Citações e intimações.
- Formas do procedimento.
- Processo criminal de crimes comuns.
- Sentença criminal.
- Citação, intimação, interdição de direito.
- Processos dos crimes de responsabilidade dos funcionários públicos.
- Processos em espécie: processo comum.
- Sentença: coisa julgada, habeas corpus, mandado de segurança em matéria criminal.
- Sentença: conceito, requisitos, classificação, publicação e intimação.
- Procedimento Comum Ordinário.
- Procedimento Comum Sumário.
- Processo e julgamento dos crimes de responsabilidade dos funcionários públicos.
- Processos especiais.
- Procedimentos: crimes apenados com reclusão.
- Crimes apenados com detenção.
- Sentenças.
- Comunicações, forma, lugar, prazo.
- Sentença absolutória: providências e efeitos.
- Procedimento comum: ordinário, sumário e sumaríssimo.
- Citações e intimações: forma, lugar e tempo.
- Sentença condenatória: fundamento da pena e efeitos.
- Sentença absolutória.
- Sentença condenatória.
- Processos em espécie.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-penal.citacoes-sentenca-procedimentos",
  "subject": "Processo Penal",
  "title": "Citações, sentença, procedimentos e Tribunal do Júri",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Citações, sentença, procedimentos e Tribunal do Júri"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-penal.citacoes-sentenca-procedimentos.json.

==================================================
# MATÉRIA 159 — processo-penal.nulidades-recursos-habeas-corpus
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Processo Penal: nulidades, recursos, habeas corpus e revisão criminal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Nulidades no processo penal (absolutas e relativas, princípio do prejuízo, interesse, causalidade, momento de arguição e convalidação); recursos em geral (voluntariedade, recurso de ofício, fungibilidade, unirrecorribilidade, pressupostos, efeitos, proibição da reformatio in pejus, desistência pelo Ministério Público, assistente de acusação); recurso em sentido estrito; apelação; embargos de declaração e embargos infringentes e de nulidade; carta testemunhável; habeas corpus (natureza, cabimento, legitimidade, espécies, competência e procedimento); revisão criminal (hipóteses, legitimidade, efeitos e indenização).

Fica de fora (outras matérias tratam): Aplicação da lei processual, jurisdição, competência em geral e sujeitos processuais; citações, intimações, sentença e procedimentos, inclusive o rito do júri (estão em outras matérias); recurso extraordinário, recurso especial e agravos dos tribunais superiores em detalhe; mandado de segurança em matéria criminal; agravo em execução.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Nulidades.
- Habeas corpus e seu processo.
- Recursos em geral.
- Revisão criminal.
- Habeas corpus.
- Carta testemunhável.
- Embargos infringentes e de nulidade.
- Recursos especial e extraordinário.
- Correição Parcial.
- Apelação.
- Dos recursos.
- Sentença penal, recursos e ações autônomas de impugnação.
- Sentença: coisa julgada, habeas corpus, mandado de segurança em matéria criminal.
- Nulidades e revisão criminal.
- Espécies.
- Recursos no processo penal.
- Recurso especial e extraordinário.
- Recursos.
- Protesto por novo júri.
- Recursos em geral: princípios básicos, modalidades e princípio da fungibilidade.
- Procedimentos, nulidades, sentença, coisa julgada, recursos e ações autônomas de impugnação.
- Prazos: Nulidades.
- Prazos: Recursos em geral.
- Prazos: Habeas corpus e seu processo.
- Prazos, nulidades, recursos, habeas corpus e relações jurisdicionais com autoridade estrangeira.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-penal.nulidades-recursos-habeas-corpus",
  "subject": "Processo Penal",
  "title": "Processo Penal: nulidades, recursos, habeas corpus e revisão criminal",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Processo Penal: nulidades, recursos, habeas corpus e revisão criminal"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-penal.nulidades-recursos-habeas-corpus.json.

==================================================
# MATÉRIA 160 — processo-penal.inquerito-policial
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Processo Penal: Inquérito policial** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Natureza, finalidade, características, instauração, diligências, direitos da pessoa investigada, sigilo, prazos, relatório, arquivamento e relação do inquérito com a ação penal.

Fica de fora (outras matérias tratam): Instrução judicial, meios de prova em juízo em profundidade, ação penal em espécie e medidas cautelares tratadas autonomamente.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Inquérito Policial.
- Investigação criminal (Lei nº 12.830/2013).
- Garantias do investigado.
- Atribuições da autoridade policial.
- Inquérito policial: histórico, natureza, conceito, finalidade, características, fundamento, titularidade, grau de cognição, valor probatório, formas de instauração, notitia criminis, delatio criminis, procedimentos investigativos, indiciamento, garantias do investigado; conclusão.
- Inquérito policial e ação penal.
- Persecução penal.
- Indiciamento.
- Inquérito e ação penal.
- Inquérito policial: conclusão, prazos.
- Investigação criminal e inquérito policial.
- Inquérito policial: procedimentos investigativos.
- Inquérito policial: indiciamento.
- Inquérito policial: garantias do investigado.
- Inquérito policial: conclusão e prazos.
- Investigação criminal conduzida pelo delegado de polícia.
- Inquérito policial: histórico, natureza, conceito, finalidade, características, fundamento, titularidade, grau de cognição, valor probatório.
- Inquérito policial: formas de instauração, notitia criminis, delatio criminis.
- Investigação preliminar.
- Inquérito policial, Termo circunstanciado de ocorrência.
- Comissão parlamentar de inquérito.
- Investigação criminal promovida pelo Ministério Público.
- Outras formas de investigação.
- Arquivamento de inquérito.
- Inquérito policial: histórico, natureza, conceito, finalidade, características, fundamento, titularidade, grau de cognição, valor probatório, formas de instauração, notitia criminis, delatio criminis, procedimentos investigativos, indiciamento, garantias do investigado.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-penal.inquerito-policial",
  "subject": "Processo Penal",
  "title": "Inquérito policial",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Inquérito policial"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-penal.inquerito-policial.json.

==================================================
# MATÉRIA 161 — processo-penal.prisoes-liberdade-provisoria
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Processo Penal: Prisões e liberdade provisória** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Prisão em flagrante, preventiva e temporária; audiência de custódia; relaxamento; liberdade provisória com ou sem fiança; medidas cautelares diversas; revisão, revogação e substituição da prisão processual.

Fica de fora (outras matérias tratam): Execução definitiva da pena, prisão civil e regime de cumprimento de pena, salvo distinção necessária.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Prisão e liberdade provisória.
- Prisão em flagrante.
- Prisão preventiva.
- Prisão temporária.
- Lei nº 7.960/1989 (prisão temporária).
- Medidas cautelares diversas da prisão.
- Prisão.
- Liberdade provisória.
- Fiança.
- Prisão: conceito, espécies, mandado de prisão e cumprimento.
- Restrição de liberdade.
- Restrição de liberdade: prisão em flagrante.
- Audiência de Custódia.
- Princípio da necessidade, prisão especial, liberdade provisória.
- Prisão temporária (Lei nº 7.960/1989).
- Medidas cautelares e liberdade provisória.
- Prisão, medidas cautelares e liberdade provisória.
- Prisão domiciliar.
- Flagrante.
- Temporária.
- Preventiva.
- Prisão especial, prisão albergue, prisão domiciliar e liberdade provisória.
- Prisão, liberdade provisória e prisão temporária.
- Lei nº 7.960/1989 e suas alterações (prisão temporária).
- Prisões e medidas cautelares diversas da prisão.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-penal.prisoes-liberdade-provisoria",
  "subject": "Processo Penal",
  "title": "Prisões e liberdade provisória",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Prisões e liberdade provisória"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-penal.prisoes-liberdade-provisoria.json.

==================================================
# MATÉRIA 162 — direito-penal.imputabilidade-concurso-pessoas
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal: Imputabilidade e concurso de pessoas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Imputabilidade, inimputabilidade e semi-imputabilidade; menoridade penal; doença mental e embriaguez; concurso de pessoas, coautoria e participação; comunicabilidade de circunstâncias e cooperação dolosamente distinta.

Fica de fora (outras matérias tratam): Aplicação da lei penal no tempo e espaço, teoria geral do crime além desses pontos e aplicação das penas.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Concurso de pessoas.
- Imputabilidade penal.
- Concurso de agentes: autoria e participação.
- Imputabilidade.
- Elementares e circunstâncias.
- Elementos e causas de exclusão da culpabilidade.
- Autoria e participação.
- Modificadores e avaliação pericial da imputabilidade penal e da capacidade civil.
- Doença mental, desenvolvimento mental incompleto ou retardado, perturbação mental.
- Imputabilidade penal e concurso de pessoas.
- Concurso de pessoas e concurso de crimes.
- Culpabilidade: teorias, elementos e causas de exclusão.
- Concurso de agentes.
- Modificadores e avaliação pericial da imputabilidade penal e da capacidade civil. Doença mental, desenvolvimento mental incompleto ou retardado, perturbação mental.
- Causas de exclusão da culpabilidade: Imputabilidade.
- Emoção e paixão.
- Embriaguez.
- Teoria geral da culpabilidade: Imputabilidade.
- Concurso de pessoas: Autoria, coautoria e participação.
- Concurso de pessoas: Comunicabilidade de elementares e circunstâncias.
- Teoria geral do crime: Concurso de agentes.
- Concurso de agentes: Elementares e circunstâncias.
- Menoridade.
- Requisitos.
- Circunstâncias incomunicáveis.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal.imputabilidade-concurso-pessoas",
  "subject": "Direito Penal",
  "title": "Imputabilidade e concurso de pessoas",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Imputabilidade e concurso de pessoas"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal.imputabilidade-concurso-pessoas.json.

==================================================
# MATÉRIA 163 — matematica.analise-combinatoria
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática: Análise combinatória** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Princípios aditivo e multiplicativo, fatorial, permutações simples e com repetição, arranjos, combinações e contagens em etapas.

Fica de fora (outras matérias tratam): Probabilidade, análise assintótica e combinatória avançada de grafos.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Princípios de contagem e probabilidade.
- Princípios de contagem.
- Análise combinatória.
- Análise combinatória, arranjos e permutações.
- Análise combinatória, conjuntos numéricos e sistemas de equações do primeiro e segundo graus.
- Combinações.
- Arranjos e permutações.
- Problemas de contagem.
- Análise combinatória e probabilidade: arranjos, combinações, permutações simples, probabilidade de um evento e resolução de problemas.
- Análise combinatória: princípio fundamental da contagem, arranjos, permutações e combinações.
- Binômio de Newton.
- Técnicas de Contagem e Análise Combinatória: Combinações Simples, Arranjos e Permutação com e sem repetição.
- Análise combinatória: princípios fundamentais de contagem, arranjos, permutações, combinações e binômio de Newton.
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

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "matematica.analise-combinatoria",
  "subject": "Matemática",
  "title": "Análise combinatória",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Análise combinatória"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica.analise-combinatoria.json.

==================================================
# MATÉRIA 164 — legislacao-penal-especial.lavagem-dinheiro
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: Lei de lavagem de dinheiro: aspectos penais e processuais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Lei nº 9.613/1998: delitos de lavagem, antecedentes e autonomia, fases, tipificação, dolo, autolavagem, tentativa, majorantes, colaboração, competência e procedimento, cautelares patrimoniais, alienação antecipada, dados cadastrais e efeitos da condenação, à luz da legislação e jurisprudência vigentes.

Fica de fora (outras matérias tratam): Deveres administrativos de prevenção, comunicações ao órgão de inteligência financeira, sanções administrativas, detalhes da lei de organização criminosa e legislação tributária/financeira própria.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Lei nº 9.613/1998 e suas alterações (Lavagem de dinheiro).
- Lei nº 9.613/1998 (Lavagem de dinheiro).
- Lavagem de dinheiro (Lei nº 9.613/1998).
- Crimes de lavagem ou ocultação de bens, direitos e valores.
- Lei 9.613/1998 (Crimes de lavagem de dinheiro).
- Lavagem de dinheiro.
- Lei nº 9.613/1998.
- Legislação penal especial: lavagem de dinheiro.
- Mecanismos de Combate às organizações criminosas e Lavagem de Dinheiro.
- Crimes de lavagem de dinheiro: Lei nº 9.613/1998.
- Lei Federal nº 9.613/1998 - Lei de Lavagem de Dinheiro.
- Crimes de lavagem de dinheiro (Lei nº 9.613/1998 e suas alterações).
- Crimes de lavagem ou ocultação de bens, direitos e valores (Lei nº 9.613/1998).
- Lei nº 9.613/1998, e suas alterações (Lavagem de dinheiro).
- Disposições especiais e medidas assecuratórias previstas na Lei que dispõe sobre os crimes de "lavagem" ou ocultação de bens, direitos e valores.
- Crime de lavagem ou ocultação de bens, direitos e valores (Lei nº 9.613, de 3/3/1998).
- Lei dos crimes de lavagem de dinheiro (Lei nº 9.613/1998).
- Criptomoedas e lavagem de dinheiro.
- Crime de lavagem de dinheiro: Conceito e etapas.
- Leis Federais n. 9.613/98.
- Crimes de lavagem ou ocultação de bens, direitos e valores (Lei nº 9.613/1998 e suas alterações).
- Lei 9.613/1998 ("Lavagem" de Capitais ou ocultação de bens, direitos e valores).
- Lei nº 9.613/1998 e alterações da Lei nº 12.683/2012 (lavagem de dinheiro).
- Aspectos processuais da Lei nº 9.613/1998 e alterações da Lei nº 12.683/2012 (Lavagem de dinheiro).
- Lei Federal nº 9.613/1998 - Lavagem de Dinheiro.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.lavagem-dinheiro",
  "subject": "Legislação Penal Especial",
  "title": "Lei de lavagem de dinheiro: aspectos penais e processuais",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Lei de lavagem de dinheiro: aspectos penais e processuais"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.lavagem-dinheiro.json.

==================================================
# MATÉRIA 165 — direito.direitos-humanos-sistema-interamericano
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Noções de Direitos Humanos e Legislação: Sistema Interamericano de Direitos Humanos: Comissão e Corte Interamericana** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Arquitetura do sistema interamericano (OEA, Carta, Declaração Americana de 1948 e Convenção Americana), os dois trilhos de proteção (Carta e Convenção), subsidiariedade e quarta instância; Comissão Interamericana: natureza, sede, composição, eleição, funções de promoção e proteção, visitas in loco, relatorias, petições individuais e interestatais, admissibilidade e suas exceções, solução amistosa, relatórios preliminar e final, força das recomendações, medidas cautelares e envio do caso à Corte; Corte Interamericana: natureza, sede, composição, juiz ad hoc, competência contenciosa facultativa e temporal, violações continuadas, legitimidade, participação autônoma das vítimas, Defensor Interamericano, fases do processo e competência consultiva; sentença, interpretação, cumprimento e execução interna, supervisão, reparação integral e medidas provisórias; controle de convencionalidade; reconhecimento da competência da Corte pelo Brasil e principais casos brasileiros.

Fica de fora (outras matérias tratam): Leitura artigo por artigo dos direitos protegidos pela Convenção Americana, regras de suspensão de garantias e disposições finais do tratado (tratados na matéria própria da Convenção Americana); sistema global da ONU e seus comitês; estudo autônomo do Protocolo de San Salvador e das demais convenções interamericanas temáticas.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Declaração Americana dos Direitos e Deveres do Homem.
- Sistema Regional Interamericano de Proteção aos Direitos Humanos.
- Comissão Interamericana de Direitos Humanos e Corte Interamericana de Direitos Humanos: composição, funcionamento, atribuições e histórico de decisões.
- Sistema Interamericano de Direitos Humanos.
- Sistema regional interamericano de proteção dos direitos humanos.
- O Brasil e o sistema interamericano.
- Corte Interamericana de Direitos Humanos.
- Sistema interamericano de direitos humanos e Corte Interamericana.
- Comissão e Corte Interamericanas, convenções regionais, medidas cautelares, jurisprudência, proteção no Mercosul e teoria da quarta instância.
- Responsabilidade internacional dos Estados, esgotamento dos recursos internos, reparações, controle de convencionalidade e execução de decisões internacionais no Brasil.
- O Brasil e o sistema interamericano: A Organização dos Estados Americanos.
- Organização dos Estados Americanos.
- Sistemas convencionais de petições.
- A OEA e o Tratado do Rio de Janeiro.
- Sistema interamericano de proteção aos direitos humanos.
- Sistema Regional Interamericano de Proteção dos Direitos Humanos: Organização dos Estados Americanos (OEA).
- Comissão Interamericana de Direitos Humanos e Corte Interamericana de Direitos Humanos.
- Direitos Humanos: a Jurisprudência Internacional.
- Organização dos Estados Americanos (OEA): origem, órgãos e funções.
- Comissão Interamericana de Direitos Humanos: composição, funções, procedimentos e deliberações.
- Corte Interamericana de Direitos Humanos: composição, jurisdição consultiva e contenciosa, desenvolvimento do processo e forma de execução das sentenças.
- Jurisprudência consultiva e contenciosa da Corte Interamericana de Direitos Humanos.
- Sistema regional de proteção dos direitos humanos.
- OEA.
- Sistema Interamericano de Proteção aos direitos humanos (SIPDH).

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito.direitos-humanos-sistema-interamericano",
  "subject": "Noções de Direitos Humanos e Legislação",
  "title": "Sistema Interamericano de Direitos Humanos: Comissão e Corte Interamericana",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Sistema Interamericano de Direitos Humanos: Comissão e Corte Interamericana"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito.direitos-humanos-sistema-interamericano.json.

==================================================
# MATÉRIA 166 — legislacao-penal-especial.interceptacao-telefonica
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: Interceptação telefônica e telemática** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Lei 9.296/1996: base constitucional do sigilo das comunicações; conceitos de interceptação, escuta e gravação; interceptação telefônica e do fluxo telemático; diferença entre comunicação em curso, registros e dados armazenados; requisitos e vedações; legitimidade para requerer; pedido, prazo de decisão e fundamentação; execução, prazo de quinze dias e renovações; autos apartados, transcrição e inutilização; encontro fortuito, prova emprestada e juízo aparente; captação ambiental; crimes de interceptação ilegal e de captação ambiental sem autorização.

Fica de fora (outras matérias tratam): Teoria geral das provas ilícitas em profundidade, outros meios de obtenção de prova da lei de organizações criminosas (colaboração premiada, ação controlada, infiltração), quebra de sigilo bancário e fiscal e crimes de abuso de autoridade em geral.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Lei nº 9.296/1996 (interceptação telefônica).
- Interceptação telefônica (Lei nº 9.296/1996).
- Interceptação de comunicações telefônicas e do fluxo de comunicações em sistemas de informática e telemática.
- Interceptações de comunicação.
- Interceptação das Comunicações Telefônicas (Lei nº 9.296/1996).
- Interceptação telefônica: conceito, provas ilícitas e disposições legais (Lei nº 9.296/1996).
- Lei Federal nº 9.296/1996 - Interceptação de comunicações telefônicas.
- Provas (TÍTULO VII CPP): Interceptação telefônica (Lei nº 9.296/1996).
- Interceptação telefônica (Lei nº 9.296/1996 e suas alterações).
- Prova: Lei nº 9.296/1996 (interceptação telefônica).
- Interceptação telefônica (Lei nº 9.296, de 24/7/1996).
- Interceptação das comunicações.
- Quebra do sigilo telemático.
- Leis Federais n. 9.296/1996.
- Aspectos processuais da Lei nº 9.296/1996 (Interceptação telefônica).
- Lei Federal nº 9.296/1996 - Interceptação das Comunicações Telefônicas.
- Lei nº 9.296/1996 (escuta telefônica).
- Lei nº 9.296/1996 e suas alterações (Interceptação Telefônica, Telemática e Ambiental).
- Lei nº 9.296/1996.
- Interceptação das comunicações telefônicas (Lei nº 9.296/1996 e suas alterações).
- Lei nº 9.296/1996 e suas alterações (Lei de Interceptação Telefônica).
- Interceptação telefônica (Lei nº 9.296/1996 e alterações).
- Interceptação telefônica e procedimentos da Lei nº 11.343/2006.
- Interceptação telefônica (Lei n.º 9.296/1996 e alterações).
- Interceptação de comunicações telefônicas.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.interceptacao-telefonica",
  "subject": "Legislação Penal Especial",
  "title": "Interceptação telefônica e telemática",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Interceptação telefônica e telemática"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.interceptacao-telefonica.json.

==================================================
# MATÉRIA 167 — contabilidade.intangivel-impairment-provisoes
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Contabilidade: Intangível, redução ao valor recuperável e provisões** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Ativo intangível (definição, identificabilidade, reconhecimento, gerados internamente, pesquisa e desenvolvimento, vida útil definida e indefinida, amortização, baixa); redução ao valor recuperável de ativos (indícios, valor justo líquido, valor em uso, unidade geradora de caixa, goodwill, alocação e reversão da perda); provisões, passivos contingentes e ativos contingentes (reconhecimento, mensuração, desconto, reembolso, contrato oneroso, reestruturação, uso e reversão).

Fica de fora (outras matérias tratam): Estrutura conceitual, imobilizado e depreciação em geral, estoques, patrimônio líquido e reservas, combinações de negócios em detalhe, instrumentos financeiros e normas do setor público.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Provisões, passivos contingentes e ativos contingentes.
- Mensuração de passivos: Provisões.
- Ativo intangível.
- Redução ao valor recuperável de ativos.
- Reavaliação e redução ao valor recuperável.
- Ativos intangíveis: definição, reconhecimento e mensuração.
- Mensuração de passivos, provisões e passivos contingentes.
- Redução ao valor recuperável, mensuração, registro contábil, reversão.
- Ativos Intangíveis, conceito, apropriação, forma de avaliação e registros contábeis.
- Ativo intangível: reconhecimento, mensuração inicial, mensuração subsequente, reconhecimento de despesa, tratamento da amortização, vida útil, baixa e alienação.
- Ativo Imobilizado: conceituação, classificação e conteúdos das contas: Redução ao valor recuperável.
- Ativos intangíveis: definição, reconhecimento e mensuração: Impairment test: intangíveis com vida útil definida e indefinida.
- Ativos intangíveis: definição, reconhecimento, mensuração e teste de impairment.
- Ativo intangível: Reconhecimento.
- Amortização: cálculo e contabilização.
- Redução ao valor recuperável de ativos: Conceito e reconhecimento.
- Tratamento da reversão.
- Provisões, passivos contingentes e ativos contingentes: Conceito e reconhecimento.
- Provisões, passivos e ativos contingentes.
- Provisões e contingências.
- Redução ao valor recuperável (impairment).
- Impairment test: intangíveis com vida útil definida, indefinida e goodwill.
- Ativos intangíveis: definição, reconhecimento, mensuração e impairment de ativos com vida útil definida e indefinida.
- Mensuração de ativos e passivos: imobilizado, intangível, reavaliação, redução ao valor recuperável, depreciação, amortização, exaustão, provisões e passivos contingentes.
- Depreciação, exaustão, amortização, redução ao valor recuperável, intangível e imobilizado.

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "contabilidade.intangivel-impairment-provisoes",
  "subject": "Contabilidade",
  "title": "Intangível, redução ao valor recuperável e provisões",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Intangível, redução ao valor recuperável e provisões"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome contabilidade.intangivel-impairment-provisoes.json.

==================================================
# MATÉRIA 168 — matematica-financeira.sistemas-amortizacao
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática Financeira: Sistemas de amortização: Price, SAC e SAM** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Planos de amortização de empréstimos e financiamentos: principal, juros, prestações, saldos, cronogramas, sistemas SAC, Price (francês) e SAM, comparação de juros totais e pagamentos.

Fica de fora (outras matérias tratam): Análise de investimentos, descontos de títulos, renegociação de dívidas e regras legais de crédito.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Planos de amortização de empréstimos e financiamentos.
- Sistema de amortização constante.
- Amortizações.
- Sistema francês.
- Sistema misto.
- Sistema de Amortização Constante (SAC).
- Planos de amortização de empréstimos e financiamentos: Sistema francês (tabela PRICE).
- Planos de amortização de empréstimos e financiamentos: Sistema de amortização constante (SAC).
- Planos de amortização de empréstimos e financiamentos: Sistema de amortização misto (SAM).
- Planos ou Sistemas de Amortização de Empréstimos e Financiamentos.
- Sistema de Amortização Misto (SAM).
- Sistemas de Amortização de qualquer tipo, incluindo os sistemas com amortizações constantes (SAC).
- Sistemas de Amortização com prestações constantes (Francês ou PRICE).
- Sistema francês (tabela Price).
- Amortizações: Sistema Price (francês), Sistema de Amortização Constante (SAC) e Sistema Misto.
- Sistemas PRICE e SAC de amortização.
- Sistema PRICE de amortização por prestações constantes.
- Sistema SAC de amortizações constantes.
- Sistemas de amortização SAC e Price.
- Sistemas de amortização Price, SAC e SAM.
- Amortizações. Sistema francês. Sistema de amortização constante. Sistema misto.
- Sistemas de amortização de empréstimos: Sistema Francês – Tabela Price.
- Sistema Americano de Amortização a uma e a duas taxas (Sinking Fund).
- Sistemas de amortização de empréstimos e financiamentos.
- Sistemas Price, SAC e SAM de amortização.

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "matematica-financeira.sistemas-amortizacao",
  "subject": "Matemática Financeira",
  "title": "Sistemas de amortização: Price, SAC e SAM",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Sistemas de amortização: Price, SAC e SAM"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica-financeira.sistemas-amortizacao.json.

==================================================
# MATÉRIA 169 — matematica.razao-proporcao
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática e Raciocínio Lógico: Razão e proporção** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Razões entre grandezas, taxas unitárias, razões equivalentes, proporções, propriedade fundamental, divisão proporcional e reconhecimento de proporcionalidade direta e inversa.

Fica de fora (outras matérias tratam): Regra de três como procedimento geral, porcentagens, semelhança geométrica e matemática financeira.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Razões e proporções.
- Proporcionalidade direta e inversa.
- Razão e proporção.
- Razões e proporções: Divisão proporcional.
- Números e grandezas proporcionais: razões e proporções.
- Divisão em partes proporcionais.
- Divisão proporcional.
- Razões e proporções; divisão proporcional.
- Proporcionalidades.
- Proporções e divisão proporcional.
- Conjuntos numéricos, sistema legal de medidas, razões, proporções, divisão proporcional e regras de três.
- Razões, proporções e divisão em partes proporcionais.
- Razões e proporções e divisão em partes proporcionais.
- Proporcionalidade: razões e proporções e divisão em partes diretamente e inversamente proporcionais.
- Proporcionalidade: razões e proporções; problemas.
- Divisão em partes diretamente e inversamente proporcionais.
- Proporcionalidade direta e inversa e medidas de comprimento, área, volume, massa e tempo.
- Proporcionalidade, regras de três e divisão de grandezas em partes proporcionais.
- Números racionais; razão, proporção e grandezas proporcionais.
- Razões, proporções, porcentagens, juros e proporcionalidade direta e inversa.
- Proporções.
- Variação de grandezas: razão e proporção.
- Taxas de variação de grandezas: razão e proporção com aplicações.
- Proporcionalidade: grandezas diretamente proporcionais, grandezas inversamente proporcionais, regra de três simples e composta, gráficos e tabelas.
- Geometria: razão entre comprimentos.

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "matematica.razao-proporcao",
  "subject": "Matemática e Raciocínio Lógico",
  "title": "Razão e proporção",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Razão e proporção"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica.razao-proporcao.json.

==================================================
# MATÉRIA 170 — matematica-financeira.descontos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática Financeira: Descontos simples e compostos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Descontos simples e compostos; modalidades comercial (por fora) e racional (por dentro); valor nominal, valor atual e desconto; taxa efetiva; equivalência entre taxa de desconto e juros; prazo e conversão de unidades.

Fica de fora (outras matérias tratam): Séries uniformes de pagamentos, sistemas de amortização, análise de investimentos e atualização monetária por índices.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Desconto racional e desconto comercial.
- Descontos simples.
- Juros simples: capitais equivalentes.
- Juros compostos: capitais equivalentes.
- Desconto.
- Descontos: simples, composto.
- Capitalização e desconto.
- Descontos: simples e composto.
- Desconto composto.
- Descontos: racional composto.
- Descontos: comercial simples.
- Capitalização e descontos.
- Descontos: descontos simples comercial (bancário) e racional, e descontos compostos comercial e racional.
- Descontos.
- Descontos: cálculo do valor atual, valor nominal e taxa de desconto.
- Descontos: cálculo do valor atual, do valor nominal e da taxa de desconto.
- Descontos: simples, composto. Desconto racional e desconto comercial.
- Capitais equivalentes.
- Descontos compostos.
- Valor atual.
- Valor atual e valor nominal.
- Operação de desconto simples: racional (por dentro), comercial (por fora) e bancário.
- Equivalência entre taxa de juro e taxa de desconto.
- Desconto composto: racional e comercial.
- Operações Contábeis Diversas: Descontos comerciais e financeiros.

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "matematica-financeira.descontos",
  "subject": "Matemática Financeira",
  "title": "Descontos simples e compostos",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Descontos simples e compostos"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica-financeira.descontos.json.

==================================================
# MATÉRIA 171 — raciocinio-logico.conjuntos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Raciocínio Lógico e Matemático: Operações com conjuntos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Linguagem e operações de conjuntos, cardinalidade e potência, diagramas de Venn de dois e três conjuntos, inclusão-exclusão, conjuntos numéricos, intervalos reais e problemas de pesquisas e equipes.

Fica de fora (outras matérias tratam): Teoria axiomática avançada, cardinalidade de infinitos e combinatória além da necessária para operações e diagramas.

Os editais pedem este assunto assim (cubra todos estes pontos):
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

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "raciocinio-logico.conjuntos",
  "subject": "Raciocínio Lógico e Matemático",
  "title": "Operações com conjuntos",
  "version": 6,
  "status": "PUBLISHED",
  "aliases": ["Operações com conjuntos"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome raciocinio-logico.conjuntos.json.

==================================================
# MATÉRIA 172 — direito-trabalho.equiparacao-salarial
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito do Trabalho: Equiparação salarial** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Equiparação salarial e desvio de função; FGTS; prescrição e decadência; CIPA; insalubridade e periculosidade; proteção ao trabalho do menor e aprendizagem.

Fica de fora (outras matérias tratam): Proteção ao trabalho da mulher, direito coletivo, greve, categorias sindicais, estabilidade gestante e licença-maternidade.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Prescrição e decadência.
- Equiparação salarial.
- Equiparação salarial; princípio da igualdade de salário; desvio de função.
- Segurança e medicina no trabalho: CIPA; atividades insalubres ou perigosas.
- Desvio de função.
- Princípio da igualdade de salário.
- Da equiparação salarial: hipóteses ensejadoras e forma de aferição.
- Do princípio da igualdade de salário.
- Do desvio de função.
- Equiparação salarial: Princípio da igualdade de salário.
- Equiparação salarial, igualdade salarial, desvio e acúmulo de função.
- Segurança e medicina no trabalho, CIPA e atividades insalubres ou perigosas.
- Equiparação salarial, igualdade salarial e desvio de função.
- Da igualdade salarial (Lei nº 14.611/2023).
- Saúde e segurança no trabalho: conceitos de saúde e segurança.
- Lei nº 14.611/2023.
- Equiparação salarial: Desvio de função.
- Equiparação salarial, princípio da igualdade de salário e desvio de função.
- Equiparação salarial; princípio da igualdade de salário.
- Segurança e medicina do trabalho, CIPA, EPI e atividades insalubres e perigosas.
- Equiparação salarial, igualdade salarial, desvio de função, prescrição e decadência.
- Segurança e medicina do trabalho: CIPA, EPI e atividades insalubres e perigosas.
- Equiparação salarial: Caracterização, requisitos, excludentes.
- Equiparação salarial: Desvio e acúmulo de função.
- Da equiparação salarial.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-trabalho.equiparacao-salarial",
  "subject": "Direito do Trabalho",
  "title": "Equiparação salarial",
  "version": 5,
  "status": "PUBLISHED",
  "aliases": ["Equiparação salarial"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-trabalho.equiparacao-salarial.json.

==================================================
# MATÉRIA 173 — processo-penal.acao-penal
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Processo Penal: Ação penal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Conceito, condições e princípios da ação penal; ação pública incondicionada e condicionada; representação e requisição; ação privada exclusiva, personalíssima e subsidiária da pública; queixa, decadência, renúncia, perdão e perempção.

Fica de fora (outras matérias tratam): Inquérito policial em profundidade, procedimento judicial de cada rito, recursos e ação civil ex delicto.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Ação Penal.
- Ação penal: conceito, características, espécies e condições.
- Ação penal: conceito, condições e pressupostos processuais.
- Acordo de não persecução penal.
- Ação penal pública e privada.
- Ação penal pública: titularidade e condições de procedibilidade.
- Denúncia.
- Ação penal pública.
- Denúncia e queixa.
- Da ação penal.
- Ação penal e ação civil ex delicto.
- Teoria geral da pena: Ação penal.
- Elementos identificadores da relação processual: Pretensão punitiva.
- Denúncia: Forma e conteúdo.
- Ação penal de iniciativa privada.
- Ação Penal Privada.
- Ação penal no crime complexo.
- Ação civil.
- Ação penal e sujeitos processuais.
- Renúncia, perdão, perempção e decadência.
- Ação penal (Parte Geral, Título VII).
- Denúncia: forma, conteúdo, recebimento e rejeição.
- Ação penal de iniciativa privada: titularidade, queixa, renúncia, perdão e perempção.
- Ação penal, ação civil ex delicto, jurisdição, competência, questões e processos incidentes.
- Penas: Ação penal.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-penal.acao-penal",
  "subject": "Processo Penal",
  "title": "Ação penal",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Ação penal"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-penal.acao-penal.json.

==================================================
# MATÉRIA 174 — fisica.estatica-hidrostatica-gravitacao
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **fisica: Estática, hidrostática e gravitação** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Condições de equilíbrio, momento de força, centro de massa, pressão em fluidos, princípio de Pascal, empuxo, lei da gravitação universal, campo gravitacional e movimento orbital.

Fica de fora (outras matérias tratam): Dinâmica geral de corpos rígidos, hidrodinâmica detalhada, eletromagnetismo e relatividade.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Estática dos corpos rígidos.
- Gravitação universal.
- Binário.
- Leis da Gravitação Universal.
- Leis de Kepler.
- Sistema de forças.
- Densidade e pressão.
- Princípio de Pascal, Lei de Stevin, Princípio de Arquimedes.
- Hidrostática: fundamentos, massa, peso, densidade, pressão e teorema fundamental da hidrostática.
- Vasos comunicantes, Teorema de Pascal e prensa hidráulica.
- Teorema de Arquimedes, corpos imersos e flutuantes.
- Estática dos corpos rígidos e estática dos fluidos.
- Estática, hidrostática e gravitação universal.
- Estática dos fluidos.
- Mecânica dos Fluidos: hidrostática.
- Gravitação universal, estática dos corpos rígidos e estática dos fluidos.
- Mecânica: Gravitação universal.
- Mecânica: Estática dos corpos rígidos.
- Mecânica: Estática dos fluidos.
- Estática dos fluidos: Princípios de Pascal.
- Estática dos fluidos: Princípios de Arquimedes.
- Estática dos fluidos: Princípios de Stevin.
- Atração gravitacional da Terra.
- Mecânica dos sólidos: estática e dinâmica dos corpos rígidos.

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "fisica.estatica-hidrostatica-gravitacao",
  "subject": "fisica",
  "title": "Estática, hidrostática e gravitação",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Estática, hidrostática e gravitação"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome fisica.estatica-hidrostatica-gravitacao.json.

==================================================
# MATÉRIA 175 — direito-trabalho.trabalho-mulher-gestante
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito do Trabalho: Proteção ao trabalho da mulher, gestante e licença-maternidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Proteção da mulher no trabalho, estabilidade gestacional, licença-maternidade e adotante, Programa Empresa Cidadã, pausas para amamentação e afastamento de gestantes e lactantes de ambientes insalubres.

Fica de fora (outras matérias tratam): Salário-maternidade previdenciário em seus cálculos, estabilidade de outras categorias, discriminação não relacionada à mulher e normas de saúde não ligadas à gestação e lactação.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Proteção ao trabalho da mulher.
- Proteção ao trabalho da mulher; estabilidade da gestante; licença-maternidade.
- Licença-maternidade.
- Estabilidade da gestante.
- Proteção ao trabalho da mulher: Estabilidade da gestante.
- Da proteção ao trabalho da mulher, da gestante e do menor.
- Da estabilidade da gestante.
- Da licença-maternidade.
- Proteção ao trabalho da mulher, estabilidade gestante, licença-maternidade e Lei nº 9.029/1995.
- Licença maternidade.
- Da proteção ao trabalho da mulher.
- Trabalho da mulher: estabilidade da gestante, trabalho noturno e trabalho proibido.
- Proteção ao trabalho do menor e da mulher, estabilidade da gestante e licença-maternidade.
- Proteção ao trabalho do menor e da mulher e estabilidade da gestante e licença-maternidade.
- Proteção ao trabalho da mulher: Licença maternidade.
- Proteção ao trabalho da mulher; estabilidade da gestante.
- Proteção ao trabalho da mulher, da gestante e do menor, estabilidade da gestante e licença-maternidade.
- Proteção ao trabalho da mulher, gestante e menor; estabilidade gestante e licença-maternidade.
- Proteção ao trabalho da mulher: Licença maternidade e Lei nº 9.029/1995.
- Proteção do trabalho da mulher e do menor.
- Proteção do trabalho da mulher e do menor; profissionalização e proteção no trabalho do adolescente.
- Das normas especiais de tutela do trabalho: da proteção do trabalho da mulher.
- Da estabilidade da gestante; da licença-maternidade (art. 10 do ADCT).

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-trabalho.trabalho-mulher-gestante",
  "subject": "Direito do Trabalho",
  "title": "Proteção ao trabalho da mulher, gestante e licença-maternidade",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Proteção ao trabalho da mulher, gestante e licença-maternidade"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-trabalho.trabalho-mulher-gestante.json.

==================================================
# MATÉRIA 176 — matematica.matrizes-determinantes
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática: Matrizes e determinantes** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Conceito e notação de matrizes, lei de formação, tipos especiais, igualdade, transposta, soma, produto por escalar, produto de matrizes, determinantes de ordem 2 e 3 (Sarrus), noções de Laplace, propriedades dos determinantes, matriz inversa e regra de Cramer.

Fica de fora (outras matérias tratam): Métodos gerais de resolução de sistemas (substituição, adição, escalonamento e problemas), tratados em matematica.sistemas-lineares; álgebra linear abstrata (espaços vetoriais, autovalores).

Os editais pedem este assunto assim (cubra todos estes pontos):
- Matrizes.
- Determinantes.
- Matrizes determinantes e sistemas lineares.
- Álgebra linear.
- Matrizes, determinantes e sistemas lineares.
- Matrizes e determinantes.
- Álgebra linear: vetores, matrizes, operações, identidade, inversa e transposta.
- Álgebra linear: vetores, matrizes, produtos escalar e vetorial.
- Matrizes identidade, inversa e transposta e transformações lineares.
- Álgebra linear: vetores e matrizes.
- Matrizes identidade, inversa e transposta.
- Álgebra linear: Notação de vetores e matrizes.
- Matriz identidade, inversa e transposta.
- Matrizes: aspectos introdutórios.
- Representação de matrizes.
- Matrizes especiais.
- Matriz transposta.
- Igualdade de matrizes.
- Operações com matrizes.
- Determinantes: conceito.
- Ordem do determinante.
- Propriedades dos determinantes.
- Regras para cálculo do determinante.
- Eixo Matrizes e Determinantes.
- Conceitos, representações e operações com matrizes.

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "matematica.matrizes-determinantes",
  "subject": "Matemática",
  "title": "Matrizes e determinantes",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Matrizes e determinantes"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica.matrizes-determinantes.json.

==================================================
# MATÉRIA 177 — matematica.progressoes-pa-pg
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática: Progressões aritméticas e geométricas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Sequências, termo geral, razão, classificação, soma finita e infinita de progressões aritméticas e geométricas, interpolação e aplicações.

Fica de fora (outras matérias tratam): Recorrências lineares de ordem superior, séries de potências e análise de convergência avançada.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Progressões aritméticas e geométricas.
- Progressões aritméticas.
- Progressões geométricas.
- Progressões aritmética e geométrica.
- Progressão aritmética e geométrica.
- Princípios de contagem, progressões aritméticas e geométricas.
- Sequências.
- Progressões aritméticas e progressões geométricas.
- Lei de formação de sequências e determinação de seus elementos.
- Sequências numéricas: progressão aritmética.
- Sequências numéricas: progressão geométrica.
- Sequências numéricas, progressões aritméticas e geométricas.
- Sequências numéricas e progressões.
- Sequências aritméticas, geométricas e mistas.
- Progressões aritméticas e geométricas: conceito; classificação; fórmula do termo geral; representação genérica; soma dos n primeiros termos; soma dos infinitos termos de uma progressão geométrica.
- Conjuntos, sequências, progressões aritméticas e geométricas.
- Progressão aritmética e progressão geométrica.
- Regularidades e padrões em sequências; progressão aritmética e progressão geométrica.
- Sequências e regularidades: sequências aritmética e geométrica.
- Sequências: Progressões aritmética e geométrica.

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
9. **Fórmulas e figuras (o app desenha exatamente este formato):** toda fórmula em LaTeX entre cifrões DUPLOS (`$$A \cup B$$`; fórmula principal em bloco, com $$ sozinho na linha antes e depois). Nunca use cifrão simples. Gráfico ou figura: um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, por exemplo `{"tipo":"barras","titulo":"...","itens":[{"rotulo":"A","valor":3}]}`; tipos: pizza, barras, linha, funcao ({"tipo":"funcao","funcoes":[{"expr":"x^2-4","nome":"f(x)"}],"xmin":-4,"xmax":4}) e geometria ({"tipo":"geometria","pontos":[{"nome":"A","x":0,"y":0}],"circulos":[{"centro":"A","raio":2,"rotulo":"A"}],"segmentos":[{"de":"A","ate":"B","rotulo":"5"}]} — a chave do fim do segmento é "ate", nunca "para"; ponha título e rótulos com os valores do texto, e confira que a figura obedece ao enunciado (ângulo reto onde diz reto) — use círculos para diagramas de Venn). De 1 a 3 por capítulo, quando ajudar. Dentro do JSON da matéria, a barra do LaTeX é dobrada (`\\cup`). Nunca use "|" dentro de fórmula numa linha de tabela.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "matematica.progressoes-pa-pg",
  "subject": "Matemática",
  "title": "Progressões aritméticas e geométricas",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Progressões aritméticas e geométricas"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica.progressoes-pa-pg.json.

==================================================
# MATÉRIA 178 — informatica.libreoffice
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Noções de Informática: LibreOffice** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Arquivos, pastas, formatos e configurações do LibreOffice; Writer, Calc e Impress; edição e formatação; atalhos, tabelas, campos, índices e impressão; fórmulas e referências, gráficos, filtros, ordenação, obtenção de dados externos e proteção de documentos. Referência operacional: LibreOffice 7.1.6 ou superior, privilegiando funções presentes na linha 7.1.

Fica de fora (outras matérias tratam): Macros avançadas, administração de bancos de dados, desenvolvimento de extensões e funcionalidades posteriores à referência indicada que não integrem o conteúdo comum dos editais.

Os editais pedem este assunto assim (cubra todos estes pontos):
- LibreOffice: manipulação de arquivos e pastas, configurações, etc.
- OpenOffice.
- LibreOffice: Writer, Calc e Impress.
- LibreOffice - Writer (conceitos e principais recursos).
- LibreOffice - Calc (conceitos e principais recursos).
- LibreOffice - Impress (conceitos e principais recursos).
- LibreOffice versão 4.4: Writer, Calc e Impress.
- Br Office: planilhas eletrônicas Calc.
- LibreOffice/Apache OpenOffice – Writer: estrutura básica dos documentos, edição e formatação de textos, cabeçalhos, parágrafos, fontes, colunas, marcadores simbólicos e numéricos, tabelas, impressão, controle de quebras e numeração de páginas, legendas, índices, inserção de objetos, campos predefinidos, caixas de texto.
- LibreOffice/Apache OpenOffice – Calc: estrutura básica das planilhas, conceitos de células, linhas, colunas, pastas e gráficos, elaboração de tabelas e gráficos, uso de fórmulas, funções e macros, impressão, inserção de objetos, campos predefinidos, controle de quebras e numeração de páginas, obtenção de dados externos, classificação de dados.
- LibreOffice/Apache OpenOffice – Impress: estrutura básica das apresentações, conceitos de slides, anotações, régua, guias, cabeçalhos e rodapés, noções de edição e formatação de apresentações, inserção de objetos, numeração de páginas, botões de ação, animação e transição entre slides.
- Br Office: editor de texto Writer.
- Br Office: editores de texto Writer e planilhas eletrônicas Calc.
- Br Office: editores de texto Writer.
- Edição de textos, planilhas e apresentações nos ambientes Microsoft Office e LibreOffice.
- Editores de texto, planilhas e apresentações nos ambientes Microsoft Office e LibreOffice.
- Edição de textos, planilhas e apresentações no Microsoft Office e LibreOffice.
- Aplicativos para edição de textos, planilhas e apresentações com LibreOffice.
- Edição de textos, planilhas e apresentações (Microsoft Office e LibreOffice).
- Conceitos e modos de utilização de aplicativos para a edição de textos, planilhas e apresentações com a suíte de escritório LibreOffice.
- Principais aplicativos comerciais para edição de textos e planilhas e geração de material escrito e multimídia (Br.Office e Microsoft Office).
- Windows 7 e LibreOffice versão 4.4: Writer, Calc e Impress.
- LibreOffice 4.4: Writer, Calc e Impress.
- LibreOffice 7 ou superior.
- Ferramentas de texto, planilha e apresentação do LibreOffice: Writer, Calc e Impress.

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
  "id": "informatica.libreoffice",
  "subject": "Noções de Informática",
  "title": "LibreOffice",
  "version": 5,
  "status": "PUBLISHED",
  "aliases": ["LibreOffice"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome informatica.libreoffice.json.

==================================================
# MATÉRIA 179 — processo-civil.acao-popular-injuncao-habeas-data
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Processual Civil: Ação popular, mandado de injunção e habeas data** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Ação popular (CF, art. 5º, LXXIII, e Lei 4.717/1965): legitimidade do cidadão, objeto e atos lesivos, vícios de nulidade, réus e litisconsórcio passivo, intervenção móvel da pessoa jurídica, papel do Ministério Público, liminar, procedimento, sentença, reexame necessário, coisa julgada secundum eventum probationis, prescrição, custas e má-fé. Mandado de injunção (CF, art. 5º, LXXI, e Lei 13.300/2016): cabimento, omissão total e parcial, mandado individual e coletivo, legitimados, procedimento, efeitos da decisão, revisão, norma superveniente, teorias concretista e não concretista e evolução no STF. Habeas data (CF, art. 5º, LXXII, e Lei 9.507/1997): cabimento, registros de caráter público, fase extrajudicial prévia e seus prazos, procedimento, recursos, prioridade e gratuidade.

Fica de fora (outras matérias tratam): Mandado de segurança e habeas corpus (estão em processo-civil.remedios-constitucionais-mandado-seguranca-habeas) e ação civil pública (outra matéria). Ação direta de inconstitucionalidade por omissão só aparece como contraste.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Ação popular.
- Mandado de injunção.
- Habeas data.
- Ação civil pública e ação popular.
- Ações diversas: Ação popular.
- Ações diversas: Mandado de injunção.
- Ações diversas: Habeas data.
- Lei nº 4.717/65 (Ação popular).
- Lei nº 9.507/97 (habeas data).
- Recursos: Ação popular.
- Ação Popular (Lei nº 4.717/1965).
- Lei da Ação Popular (Lei Federal n. 4.717/65).
- Habeas data: natureza, conceitos, hipóteses de cabimento e detalhes procedimentais.
- Ação popular: natureza, conceitos, hipóteses de cabimento e detalhes procedimentais.
- Mandado de injunção e inconstitucionalidade por omissão.
- Mandado de injunção, habeas data e ação popular.
- Ação Popular, Mandado de Segurança, Ação Civil Pública.
- Habeas data (Lei nº 9.507/1997).

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-civil.acao-popular-injuncao-habeas-data",
  "subject": "Direito Processual Civil",
  "title": "Ação popular, mandado de injunção e habeas data",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Ação popular, mandado de injunção e habeas data"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-civil.acao-popular-injuncao-habeas-data.json.

==================================================
# MATÉRIA 180 — biologia.ecologia-ciclos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Biologia: Ecologia e ciclos biogeoquímicos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Níveis de organização ecológica, habitat e nicho, populações e sucessão em noções, cadeias e teias alimentares, pirâmides e produtividade, relações ecológicas, ciclos da água, carbono, nitrogênio, fósforo e enxofre, biomas em noções e impactos ambientais.

Fica de fora (outras matérias tratam): Classificação detalhada dos seres vivos, fisiologia, genética, legislação ambiental, licenciamento, gestão de resíduos em profundidade e modelagem ecológica avançada.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Ciclos biogeoquímicos.
- Relações ecológicas limitadoras do crescimento populacional.
- Conceitos de ecologia, biomas e ecossistemas brasileiros.
- Ecologia: Conceitos básicos.
- Interações Ecológicas.
- Biomas do Brasil.
- Cadeias e teias alimentares.
- Bioacumulação.
- Biodiversidade no planeta e no Brasil.
- Interação entre os seres vivos: impactos na ecologia.
- Relações tróficas: Cadeias e teias alimentares.
- Relações tróficas: Distribuição natural da matéria e da energia.
- Relações tróficas: Concentração de pesticidas e de subprodutos radiativos.
- Ecologia.
- Ecologia: Ecossistemas naturais.
- Ecologia e meio ambiente.

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
  "id": "biologia.ecologia-ciclos",
  "subject": "Biologia",
  "title": "Ecologia e ciclos biogeoquímicos",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Ecologia e ciclos biogeoquímicos"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome biologia.ecologia-ciclos.json.

==================================================
# MATÉRIA 181 — processo-trabalho.procedimentos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Processual do Trabalho: Procedimentos ordinário, sumário e sumaríssimo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Cabimento dos procedimentos por valor; rito ordinário, sumário de alçada e sumaríssimo; regras dos arts. 852-A a 852-I da CLT; limites e procedimento das testemunhas; recursos no sumaríssimo.

Fica de fora (outras matérias tratam): Audiência trabalhista em geral, requisitos de reclamação fora do enfoque dos ritos, regras gerais de prova e recursos não relacionados ao sumaríssimo.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Procedimento ordinário e sumaríssimo.
- Do procedimento ordinário e sumaríssimo.
- Procedimentos nos dissídios individuais.
- Procedimentos ordinário e sumaríssimo.
- Rito sumaríssimo no dissídio individual.
- Procedimentos ordinário e sumaríssimo e procedimentos especiais.
- Procedimentos: espécies e atos.
- Procedimentos nos dissídios individuais: reclamação; jus postulandi; revelia; exceções; contestação; reconvenção; partes e procuradores; audiência; conciliação; instrução e julgamento; justiça gratuita.
- Rito sumaríssimo.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-trabalho.procedimentos",
  "subject": "Direito Processual do Trabalho",
  "title": "Procedimentos ordinário, sumário e sumaríssimo",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Procedimentos ordinário, sumário e sumaríssimo"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-trabalho.procedimentos.json.

==================================================
# MATÉRIA 182 — legislacao-penal-especial.crimes-ambientais
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: Crimes ambientais e responsabilidade penal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Lei nº 9.605/1998: responsabilização penal de pessoas físicas e jurídicas, crimes contra a fauna e a flora, poluição, ordenamento e administração ambiental, penas, sanções administrativas, reparação do dano e aspectos processuais penais, com alterações vigentes até 08/10/2026.

Fica de fora (outras matérias tratam): Licenciamento ambiental aprofundado, legislação de recursos hídricos, regime completo de unidades de conservação fora da Lei nº 9.605/1998 e procedimentos especiais não pertinentes à matéria.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Crimes contra o meio ambiente.
- Lei nº 9.605/1998 e suas alterações (crimes contra o meio ambiente).
- Lei nº 9.605/1998 (crimes contra o meio ambiente).
- Crimes ambientais (Lei nº 9.605/1998).
- Lei de Crimes Ambientais (Lei nº 9.605/1998 e suas alterações).
- Responsabilidade penal da pessoa jurídica.
- Responsabilidade penal das pessoas jurídicas.
- Crimes ambientais.
- Crimes contra o meio ambiente (Lei nº 9.605/1998).
- Infrações ambientais.
- Lei nº 9.605/1998.
- Lei nº 9.605/1998 (infrações ambientais) e suas alterações (aspectos penais e processuais penais).
- Ilícitos contra a fauna: tráfico, maus-tratos, caça e espécies exóticas.
- Lei nº 9.605/1998: sanções penais e administrativas derivadas de condutas e atividades lesivas ao meio ambiente.
- Decreto nº 6.514/2008: infrações e sanções administrativas ao meio ambiente.
- Crimes ambientais e infrações administrativas ambientais.
- Crimes ambientais: espécies e sanções penais previstas.
- Competência para julgar os crimes contra o meio ambiente.
- Crimes contra a fauna.
- Crimes contra a flora.
- Crimes contra o ordenamento urbano e patrimônio cultural.
- Crimes de poluição.
- Crimes e infrações administrativas contra o meio ambiente (Lei nº 9.605/1998 e regulamentos).
- Lei 9.605/1998 (Crimes contra o Meio Ambiente).
- Lei Federal nº 9.605/1998 - Lei dos Crimes Ambientais.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.crimes-ambientais",
  "subject": "Legislação Penal Especial",
  "title": "Crimes ambientais e responsabilidade penal",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Crimes ambientais e responsabilidade penal"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.crimes-ambientais.json.

==================================================
# MATÉRIA 183 — processo-penal-militar.processo-penal-militar
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Processo Penal Militar: Processo Penal Militar** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Código de Processo Penal Militar: polícia judiciária militar, ação penal militar, competência da Justiça Militar estadual (inclusive a do júri para crime doloso contra a vida de civil), prisões e menagem.

Fica de fora (outras matérias tratam): Direito Penal Militar material, crimes militares em espécie, detalhes procedimentais do IPM e execução penal.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Ação penal militar e seu exercício.
- Polícia judiciária militar.
- Recurso em sentido estrito.
- Processo penal militar e sua aplicação.
- Denúncia.
- Juiz, auxiliares e partes do processo.
- Menagem.
- Lei processual penal militar e sua aplicação.
- Medidas preventivas e assecuratórias.
- Aplicação provisória de medidas de segurança.
- Atos probatórios.
- Ação penal militar.
- Direito Processual Penal e Direito Processual Penal Militar: Prova.
- Prazos: Ação penal militar e seu exercício.
- Prazos: Justiça militar da União.
- Processo Penal Militar e sua aplicação; polícia judiciária militar e inquérito policial militar.
- Processo, juiz, auxiliares e partes; denúncia.
- Competência da Justiça Militar da União.
- Liberdade provisória. Aplicação provisória de medidas de segurança.
- Composição do Conselho Permanente de Justiça e Conselho Especial de Justiça.
- Processo.
- Questões prejudiciais.
- Exceções.
- Incidente de sanidade mental do acusado.
- Incidente de falsidade de documento.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "processo-penal-militar.processo-penal-militar",
  "subject": "Processo Penal Militar",
  "title": "Processo Penal Militar",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Processo Penal Militar"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-penal-militar.processo-penal-militar.json.

==================================================
# MATÉRIA 184 — legislacao-penal-especial.organizacoes-criminosas
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: Organizações criminosas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Lei nº 12.850/2013: conceito e tipificação, alterações até outubro de 2026, obtenção de provas, colaboração premiada, ação controlada, infiltração de agentes, dados e procedimento; referência pontual à Lei nº 12.694/2012.

Fica de fora (outras matérias tratam): Exame integral dos regimes de associação criminosa comum, terrorismo, lavagem de dinheiro, cálculos de pena e disciplina exaustiva de técnicas de outras leis.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Lei nº 12.850/2013 e suas alterações (Crime Organizado).
- Organizações criminosas (Lei nº 12.850/2013).
- Lei nº 12.850/2013 (Crime organizado).
- Lei nº 12.850/2013.
- Lei Federal nº 15.358/2026 - Marco Legal de Combate ao Crime Organizado no Brasil.
- Convenção de Palermo (Decreto nº 5.015/2004).
- A proteção de acusados ou condenados colaboradores.
- Instrumentos legais de obtenção de prova: delação premiada, infiltração de agente policial em organizações criminosas, ação controlada.
- Crime organizado: Lei nº 12.850/2013.
- Lei de Organização Criminosa (Lei nº 12.850/2013).
- Lei Federal nº 12.850/2013 - Lei das Organizações Criminosas.
- Lei nº 12.694/2012 e Lei nº 12.850/2013 (crime organizado).
- Crime organizado (Lei nº 12.850/2013 e suas alterações).
- Lei n.º 12.850/2013.
- Legislação penal especial: organização criminosa.
- Lei nº 12.850/2013, e suas alterações (crime organizado).
- Ações praticadas por organizações criminosas.
- Convenção das Nações Unidas contra o Crime Organizado Transnacional (Convenção de Palermo).
- Colaboração premiada.
- Meios de obtenção de prova previstos na Lei que define organização criminosa.
- A delação ou colaboração premiada.
- Lei nº 12.850/2014 (Crime organizado).
- Lei do crime organizado (Lei nº 12.850/2013).
- Organizações Criminosas e Lavagem de Dinheiro.
- Legislação penal especial: organizações criminosas.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.organizacoes-criminosas",
  "subject": "Legislação Penal Especial",
  "title": "Organizações criminosas",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Organizações criminosas"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.organizacoes-criminosas.json.

==================================================
# MATÉRIA 185 — legislacao-penal-especial.crimes-ordem-tributaria
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: Crimes contra a ordem tributária, econômica e relações de consumo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Lei 8.137/1990: arts. 1º a 4º, 7º a 17, redações vigentes e alterações de 2025-2026; crimes tributários materiais/formais e funcionais, ICMS declarado, SV 24, colaboração, penas, parcelamento, extinção, insignificância e competência; Lei 8.176/1991 atualizada em 2026; noções de crimes do CDC.

Fica de fora (outras matérias tratam): Processo administrativo fiscal aprofundado; temas gerais de direito do consumidor; descaminho e delitos previdenciários do Código Penal em estudo próprio; lavagem, sistema financeiro e infrações administrativas concorrenciais.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Crimes contra a ordem tributária, econômica e contra as relações de consumo.
- Crimes contra a ordem tributária (Lei nº 8.137/1990).
- Lei Federal nº 8.137/1990 e suas alterações (crimes contra a ordem tributária).
- Crimes contra a ordem econômica.
- Crimes contra as relações de consumo no Código de Defesa do Consumidor.
- Crimes contra a ordem tributária (Lei nº 8.137/1990 e suas alterações).
- Crimes contra a ordem tributária.
- Lei nº 8.137/1990 e suas alterações (Crimes contra a ordem econômica e tributária e as relações de consumo).
- Lei nº 8.137/1990 e alterações (crimes contra a ordem tributária, econômica e outras relações de consumo).
- Lei Federal nº 8.137/1990 - Crimes contra a ordem tributária, econômica e contra as relações de consumo.
- Lei nº 8.137/1990 e suas alterações (crimes contra a ordem tributária).
- Lei Federal nº 8.137/1990, que define os crimes contra a ordem tributária.
- Crimes contra a ordem econômica e o Sistema de Estoques de Combustíveis.
- Crimes contra a economia popular.
- Ilícito tributário: ilícito administrativo tributário, ilícito penal tributário, crimes contra a ordem tributária.
- Lei 8.137/90 (Crimes contra a ordem tributária, econômica e contra as relações de consumo).
- Lei Federal nº 8.078/1990 (Título II) - Infrações penais previstas no Código de Defesa do Consumidor.
- Do ilícito tributário: ilícito administrativo tributário, ilícito penal tributário, crimes contra a ordem tributária.
- Crimes contra a ordem tributária e ação cautelar fiscal.
- Crimes contra a ordem tributária, econômica e contra as relações de consumo (Lei nº 8.137/1990 e suas alterações).
- Ilícito penal tributário.
- Lei nº 8.137/1990.
- Crime contra a ordem tributária.
- Crimes em licitações, contratos e contra a ordem tributária.
- Crimes contra as relações de consumo.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.crimes-ordem-tributaria",
  "subject": "Legislação Penal Especial",
  "title": "Crimes contra a ordem tributária, econômica e relações de consumo",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Crimes contra a ordem tributária, econômica e relações de consumo"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.crimes-ordem-tributaria.json.

==================================================
# MATÉRIA 186 — legislacao-penal-especial.juizados-especiais-criminais
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: Juizados Especiais Criminais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Lei 9.099/1995 na esfera criminal: infração de menor potencial ofensivo, termo circunstanciado, composição civil dos danos, transação penal e suspensão condicional do processo.

Fica de fora (outras matérias tratam): Juizados especiais cíveis, procedimento criminal comum e juizados especiais federais em seus aspectos próprios.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Juizados Especiais Criminais.
- Termo circunstanciado de ocorrência.
- Lei nº 9.099/1995 e suas alterações (Juizados especiais criminais).
- Juizados especiais criminais (Lei nº 9.099/1995).
- Suspensão Condicional do Processo.
- Juizados especiais criminais (Lei nº 9.099/1995 e Lei nº 10.259/2001).
- Lei nº 9.099/1995 e suas alterações (Juizados Especiais Cíveis e Criminais).
- Lei Federal nº 9.099/1995 - Juizados Especiais Criminais.
- Crimes de menor potencial ofensivo.
- Lei nº 9.099/1995 e Lei nº 10.259/2001 e alterações (juizados especiais criminais).
- Os Juizados Especiais Cíveis e Criminais – aplicação na Justiça Federal.
- Transação Penal.
- Termo Circunstanciado.
- Lei 9.099/1995 e alterações (Juizados Especiais Criminais).
- Lei Federal nº 10.259/2001 - Juizados Especiais Cíveis e Criminais no âmbito da Justiça Federal.
- Juizados especiais criminais: aplicação na justiça federal.
- Juizados especiais criminais (Lei nº 9.099/1995 e suas alterações).
- Lei nº 9.099/1995 (juizados especiais).
- Juizados especiais criminais e legislação processual penal especial indicada no edital.
- Lei nº 9.099/1995, e suas alterações e Lei nº 10.259/2001, e suas alterações (juizados especiais criminais).
- Juizados Especiais Federais Penais.
- Justiça penal consensual.
- Juizados Especiais Federais Criminais: normas constitucionais e legais.
- Procedimento Especial nos Juizados.
- Execução penal no âmbito dos Juizados Especiais Federais.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.juizados-especiais-criminais",
  "subject": "Legislação Penal Especial",
  "title": "Juizados Especiais Criminais",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Juizados Especiais Criminais"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.juizados-especiais-criminais.json.

==================================================
# MATÉRIA 187 — legislacao-penal-especial.eca-ato-infracional
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: ECA: ato infracional e proteção integral** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Estatuto da Criança e do Adolescente: conceito e consequências do ato infracional, apreensão e garantias, medidas socioeducativas e de proteção, remissão e crimes contra criança e adolescente.

Fica de fora (outras matérias tratam): Direito penal juvenil comparado, políticas estaduais específicas e temas sem relação com as disposições penais e protetivas do ECA.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Estatuto da Criança e do Adolescente.
- Lei nº 8.069/1990 e suas alterações (Estatuto da Criança e do Adolescente).
- Estatuto da Criança e do Adolescente (Lei nº 8.069/1990).
- Lei nº 8.069/1990 (Estatuto da Criança e do Adolescente).
- Lei Federal nº 8.069/1990 - Estatuto da Criança e do Adolescente.
- Lei nº 8.069/1990 (ECA) e suas alterações (aspectos penais e processuais penais).
- Crimes contra a criança e o adolescente.
- Lei 8.069/1990 (Estatuto da Criança e do Adolescente).
- Estatuto da Criança e do Adolescente (Lei nº 8.069/1990 e suas alterações).
- Garantias processuais.
- Medidas socioeducativas, ato infracional e garantias processuais.
- Apuração do ato infracional, responsabilização, execução das medidas e remissão.
- Lei nº 8.069/1990, e suas alterações (Estatuto da Criança e do Adolescente).
- Lei nº 8.069/1990, e suas alterações ‐ Dos Crimes e das Infrações Administrativas (Estatuto da Criança e do Adolescente).
- Ato infracional, inimputabilidade, garantias individuais e processuais, medidas socioeducativas e remissão.
- Lei nº 8.069/1990: Estatuto da Criança e do Adolescente (apenas aspectos penais e processuais penais).
- Lei nº 8.069/90 e suas alterações (Estatuto da Criança e do Adolescente).
- Leis Federais n. 8.069/1990.
- Leis Federais n. 12.594/2012.
- Crimes praticados contra a criança e o adolescente (Lei nº 8.069/1990 e suas alterações).
- Da apuração de ato infracional atribuído ao adolescente.
- Aspectos processuais da Lei nº 8.069/1990 (Estatuto da Criança e do Adolescente).
- Lei 8.069/1990 (Estatuto da Criança e do Adolescente): Da Apuração de ato infracional atribuído à adolescente.
- Lei nº 8.069/1990 e alterações (Estatuto da Criança e do Adolescente).
- Dos Crimes no Estatuto da Criança e do Adolescente (Lei nº 8.069/1990).

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.eca-ato-infracional",
  "subject": "Legislação Penal Especial",
  "title": "ECA: ato infracional e proteção integral",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["ECA: ato infracional e proteção integral"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.eca-ato-infracional.json.

==================================================
# MATÉRIA 188 — direito-penal.crimes-incolumidade-fe-publica
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal: Crimes contra a incolumidade pública e a fé pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Noções sobre crimes de perigo comum, incêndio, explosão e outras condutas perigosas, falsificação de moeda, falsidade documental, uso de documento falso e proteção penal da fé pública.

Fica de fora (outras matérias tratam): Crimes contra a vida e o patrimônio em geral, delitos funcionais e legislação especial que não esteja relacionada ao recorte.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Crimes contra a fé pública.
- Crimes contra a incolumidade pública.
- Crimes contra a Fé Pública: falsidade de títulos e outros papéis públicos.
- Falsidade documental.
- Fraudes em certames de interesse público.
- Crimes contra a fé-pública.
- Crimes contra a fé pública. Falsidade de títulos e outros papéis públicos; falsidade documental; fraudes em certames de interesse público.
- Crimes contra a fé pública e falsidade documental.
- Dos crimes contra a incolumidade pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a incolumidade pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a paz pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a fé pública.
- Crimes contra a fé pública: falsidade de títulos e outros papéis públicos; falsidade documental; fraudes em certames de interesse público.
- Dos crimes contra a fé pública: da falsidade documental.
- Crime: crimes contra a incolumidade pública.
- Crime: crimes contra a fé pública.
- Crimes contra a incolumidade pública e a paz pública.
- Crimes contra a fé pública: falsidade documental.
- Dos crimes contra a fé pública.
- Os crimes contra a incolumidade pública (Parte Especial – Título VIII).
- Crimes contra a fé pública de interesse da Administração Pública.
- Penas: Crimes contra a incolumidade pública.
- Penas: Crimes contra a paz pública.
- Penas: Crimes contra a fé pública.
- Crimes contra a fé pública em detrimento do INSS.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal.crimes-incolumidade-fe-publica",
  "subject": "Direito Penal",
  "title": "Crimes contra a incolumidade pública e a fé pública",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Crimes contra a incolumidade pública e a fé pública"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal.crimes-incolumidade-fe-publica.json.

==================================================
# MATÉRIA 189 — direito-penal-militar.parte-geral
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal Militar: Código Penal Militar: parte geral** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Código Penal Militar, parte geral: crime militar em tempo de paz, aplicação da lei penal militar, imputabilidade, excludentes de ilicitude e culpabilidade, penas principais e acessórias.

Fica de fora (outras matérias tratam): Crimes militares em espécie, processo penal militar, inquérito policial militar e disciplina administrativa.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Aplicação da lei penal militar.
- Penas acessórias.
- Concurso de agentes.
- Crimes militares em tempo de paz.
- Crime.
- Penas.
- Efeitos da condenação.
- Penas principais.
- Crimes propriamente militares.
- Crimes impropriamente militares.
- Crimes militares por extensão.
- Medidas de segurança.
- Penas: Aplicação da lei penal militar.
- Penas: Crimes militares.
- Penas: Crimes militares em tempo de paz.
- Crimes própria e impropriamente militares.
- Crime militar: caracterização do crime militar (art. 9º do CPM); propriamente e impropriamente militar.
- Aplicação da lei penal militar, crime, imputabilidade e concurso de agentes.
- Penas principais e acessórias e efeitos da condenação.
- Direito Penal Militar: Aplicação da lei penal militar.
- Penas acessórias: Efeitos da condenação.
- Ação penal: Extinção da punibilidade.
- Crimes própria e impropriamente militares; critérios de classificação.
- Crimes militares em tempo de paz: definição de crime militar.
- Crimes própria e impropriamente militares: critérios de classificação.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal-militar.parte-geral",
  "subject": "Direito Penal Militar",
  "title": "Código Penal Militar: parte geral",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Código Penal Militar: parte geral"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal-militar.parte-geral.json.

==================================================
# MATÉRIA 190 — direito-penal.crimes-contra-pessoa
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal: Crimes contra a pessoa** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Crimes contra a vida, integridade física, honra, liberdade individual, inviolabilidade domiciliar, segredo e estado de filiação, com distinção entre elementos objetivos, subjetivos e formas de persecução quando relevantes.

Fica de fora (outras matérias tratam): Crimes patrimoniais, crimes sexuais tratados em capítulo próprio e regras gerais da teoria do crime, salvo como apoio à compreensão.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Crimes contra a Pessoa.
- Código Penal Brasileiro (art. 140).
- Crimes contra a honra.
- Código Penal Brasileiro (Artigo 140).
- Crimes contra a pessoa: dos crimes contra a vida.
- Crimes contra a pessoa: das lesões corporais.
- Crimes contra a pessoa: da periclitação da vida e da saúde.
- Crimes contra a pessoa: dos crimes contra a inviolabilidade dos segredos.
- Crimes em espécie: contra a pessoa, contra o patrimônio, contra a dignidade sexual, contra a família, contra a incolumidade pública, contra a paz pública, contra a fé pública, contra a Administração Pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a pessoa.
- Crime: crimes contra a pessoa.
- Crimes contra a pessoa e contra o patrimônio.
- Dos crimes previstos na parte especial do Código Penal: dos crimes contra a pessoa.
- Os crimes contra a pessoa (Parte Especial – Título I).
- Penas: Crimes contra a pessoa.
- Crimes contra a vida.
- Crime de stalking.
- Lesão corporal: Interpretação do artigo 129 do Código Penal.
- Parte Especial: dos crimes contra a pessoa.
- Crimes contra a vida: homicídio simples, qualificado e privilegiado.
- Homicídio culposo e perdão judicial.
- Crimes contra a inviolabilidade de correspondência e dos segredos.
- Dos crimes contra a pessoa.
- Parte especial do Código Penal e os crimes em espécie: crimes contra a pessoa.
- Parte especial do Código Penal: crimes contra a pessoa.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal.crimes-contra-pessoa",
  "subject": "Direito Penal",
  "title": "Crimes contra a pessoa",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Crimes contra a pessoa"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal.crimes-contra-pessoa.json.

==================================================
# MATÉRIA 191 — legislacao-penal-especial.crimes-transito
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação Penal Especial: Crimes de trânsito** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Crimes previstos no Código de Trânsito Brasileiro: homicídio e lesão culposos na direção, omissão de socorro, afastamento do local, condução com capacidade psicomotora alterada, competição não autorizada, condução sem habilitação com perigo de dano e entrega do veículo a pessoa sem condições legais ou seguras; distinção entre ilícitos penais e administrativos.

Fica de fora (outras matérias tratam): Dosimetria da pena, procedimento penal completo, jurisprudência controvertida e crimes sem relação direta com a condução de veículo automotor.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Crimes de Trânsito (Lei nº 9.503/1997).
- Crimes na direção de veículos automotores.
- Lei nº 9.503/1997 (crimes de trânsito - Código de Trânsito Brasileiro).
- Lei 9.503/1997 (Dos Crimes de Trânsito): Disposições Gerais e Crimes em Espécie.
- Lei nº 9.503/1997 e suas alterações (crimes de trânsito, Código de Trânsito Brasileiro).
- Lei nº 9.503/1997 e suas alterações (crimes de trânsito).
- Crime: Lei nº 9.503/1997 e alterações (crimes de trânsito).
- Legislação penal especial: crimes de trânsito.
- Lei nº 9.503/1997 (Código de Trânsito Brasileiro): Crimes de trânsito.
- Legislação penal especial: trânsito.
- Leis Federais n. 9.503/1997.
- Lei nº 9.503/1997 (crimes de trânsito, Código de Trânsito Brasileiro).
- Lei Federal nº 9.503/1997 - Crimes de Trânsito.
- Lei Federal nº 9.503/1997 - Código de Trânsito Brasileiro - crimes de trânsito.
- Lei Federal nº 9.503/1997 - Código de Trânsito Brasileiro — crimes de trânsito.
- Lei 9.503/1997 (Dos Crimes de Trânsito).
- Crimes de trânsito (Lei 9503/1997).
- Crimes previstos no Código Brasileiro de Trânsito (Lei n° 9.503/1997).
- Dos crimes no Código de Trânsito Brasileiro (Lei nº 9.503/1997).
- Lei nº 9.503/1997 e suas alterações.
- Crimes de trânsito (Lei nº 9.503/1997 e suas alterações).
- Lei nº 9.503/1997 (Código de Trânsito Brasileiro).
- Crimes definidos no Código de Trânsito Brasileiro.
- Dos crimes de trânsito (Lei nº 9.503/1997 e suas alterações).
- Leis especiais: Código de Trânsito Brasileiro (Lei nº 9.503/1997 e suas alterações).

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "legislacao-penal-especial.crimes-transito",
  "subject": "Legislação Penal Especial",
  "title": "Crimes de trânsito",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Crimes de trânsito"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao-penal-especial.crimes-transito.json.

==================================================
# MATÉRIA 192 — direito-penal-militar.crimes-militares-especie
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal Militar: Crimes militares em espécie** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Crimes militares em espécie: motim e revolta, desrespeito a superior, insubordinação, deserção, abandono de posto, embriaguez em serviço, peculato e concussão militares.

Fica de fora (outras matérias tratam): Parte geral do Código Penal Militar, processo penal militar, IPM e crimes militares não enumerados no escopo.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Deserção de oficial e de praça; insubmissão.
- Crimes contra o serviço militar e o dever militar.
- Crimes contra o serviço militar e o dever militar: Insubmissão.
- Crimes contra o serviço militar e o dever militar: Deserção.
- Crimes contra o serviço militar e o dever militar: Abandono de posto e de outros.
- Crimes militares.
- Crimes militares em tempo de paz contra a autoridade ou disciplina militar, o serviço e dever militar e a Administração Militar.
- Crimes contra a autoridade ou disciplina militar.
- Crimes contra o serviço e o dever militar.
- Crimes contra a Administração Militar.
- Crimes militares em tempo de paz: dos crimes contra a autoridade ou disciplina militar.
- Crimes militares em tempo de paz: dos crimes contra o serviço militar e o dever militar.
- Crimes militares em tempos de paz: crimes contra a segurança externa do país.
- Crimes militares em tempos de paz: crimes contra a autoridade ou disciplina militar.
- Crimes militares em tempos de paz: crimes contra o serviço militar e o dever militar.
- Crimes militares em tempos de paz: crimes contra a pessoa.
- Crimes militares em tempos de paz: crimes contra o patrimônio.
- Crimes militares em tempos de paz: crimes contra a incolumidade pública.
- Crimes militares em tempos de paz: crimes contra a administração militar.
- Crimes militares em tempos de paz: crimes contra a administração da justiça militar.
- Direito Penal Militar: crimes contra a autoridade ou disciplina militar, crimes contra a administração militar, crimes contra a Justiça Militar e genocídio.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal-militar.crimes-militares-especie",
  "subject": "Direito Penal Militar",
  "title": "Crimes militares em espécie",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Crimes militares em espécie"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal-militar.crimes-militares-especie.json.

==================================================
# MATÉRIA 193 — direito-penal.crimes-dignidade-sexual
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal: Crimes contra a dignidade sexual** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Código Penal: estupro, estupro de vulnerável, importunação sexual, assédio sexual e registro não autorizado da intimidade sexual, com distinção dos elementos dos tipos.

Fica de fora (outras matérias tratam): Crimes sexuais do ECA, processo penal, violência doméstica em geral e delitos não mencionados no recorte.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Crimes contra a dignidade sexual.
- Dos crimes contra a dignidade sexual.
- Crimes em espécie previstos no Código Penal: Crimes contra a dignidade sexual.
- Crimes contra os costumes.
- Crime: crimes contra a dignidade sexual.
- Crimes contra a dignidade sexual e contra a família.
- Penas: Crimes contra a dignidade sexual.
- Lei Federal nº 12.015/2009 (Corrupção de Menores).
- Parte Especial: crimes contra a dignidade sexual.
- Crimes contra a dignidade sexual. Crimes contra a família.
- Crimes contra os costumes (Código Penal). Crime de corrupção de menores.
- Crimes contra os costumes (Código Penal).

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal.crimes-dignidade-sexual",
  "subject": "Direito Penal",
  "title": "Crimes contra a dignidade sexual",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Crimes contra a dignidade sexual"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal.crimes-dignidade-sexual.json.

==================================================
# MATÉRIA 194 — direito-penal.crimes-contra-patrimonio
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Penal: Crimes contra o patrimônio** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Crimes patrimoniais de furto, roubo, extorsão, usurpação, dano, apropriação indébita, estelionato, outras fraudes, receptação e disposições gerais do capítulo.

Fica de fora (outras matérias tratam): Crimes contra a administração pública, crimes contra a pessoa tratados autonomamente e regras gerais da teoria do crime, salvo como apoio.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Crimes contra o patrimônio.
- Dos crimes contra o patrimônio.
- Crimes em espécie previstos no Código Penal: Crimes contra o patrimônio.
- Crimes contra o patrimônio: Furto.
- Crime: crimes contra o patrimônio.
- Os crimes contra o patrimônio (Parte Especial – Título II).
- Penas: Crimes contra o patrimônio.
- Estelionato contra o INSS.
- Parte Especial: crimes contra o patrimônio.
- Crimes em espécie do Código Penal: Crimes contra o patrimônio.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-penal.crimes-contra-patrimonio",
  "subject": "Direito Penal",
  "title": "Crimes contra o patrimônio",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Crimes contra o patrimônio"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-penal.crimes-contra-patrimonio.json.

==================================================
# MATÉRIA 195 — direito-trabalho.ferias
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito do Trabalho: Férias** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Períodos aquisitivo e concessivo; duração conforme faltas injustificadas; fracionamento individual após a Reforma Trabalhista; férias coletivas; remuneração acrescida do terço constitucional; abono pecuniário; pagamento em dobro por concessão tardia; férias proporcionais e vencidas na rescisão.

Fica de fora (outras matérias tratam): Repouso semanal remunerado, feriados, licenças, aviso-prévio e demais parcelas rescisórias, salvo sua interação estrita com o cálculo ou a quitação das férias.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Remuneração e abono de férias.
- Concessão e época das férias.
- Férias: direito a férias e sua duração.
- Das férias: do direito a férias e da sua duração.
- Da concessão e da época das férias.
- Da remuneração e do abono de férias.
- Férias: direito a férias e sua duração; concessão e época das férias; remuneração e abono de férias.
- Férias.
- Férias: direito e duração.
- Férias: direito do empregado, época de concessão e remuneração.
- Férias: Remuneração e abono de férias.
- Férias: direito, duração, períodos aquisitivo e concessivo, remuneração, abono e férias coletivas.
- Férias: Direito a férias e duração.
- Férias: direito, duração, concessão, época, férias coletivas, remuneração e abono.
- Férias: direito a férias e duração; concessão e época das férias; remuneração e abono de férias.
- Férias: direito a férias e sua duração; concessão época das férias; remuneração e abono de férias.
- Férias: direito, duração, concessão, época, remuneração e abono.
- Férias: Concessão e época das férias.
- Férias: direito, duração, concessão e época.
- Férias: Período concessivo e período aquisitivo de férias.
- Férias coletivas.
- Das normas gerais de tutela do trabalho: das férias anuais.
- Das férias: do direito a férias e da sua duração; da concessão e da época das férias; das férias coletivas; da remuneração e do abono de férias.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula, tema e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
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
  "id": "direito-trabalho.ferias",
  "subject": "Direito do Trabalho",
  "title": "Férias",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Férias"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-trabalho.ferias.json.

