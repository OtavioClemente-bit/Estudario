==================================================
# MATÉRIA 691 — ciencias.tabela-periodica
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **ciencias: Tabela periódica e propriedades periódicas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Organização por número atômico, grupos, períodos e blocos, famílias representativas e de transição, configuração eletrônica, metais e não metais, raio atômico e iônico, energia de ionização, eletronegatividade e tendências periódicas.

Fica de fora (outras matérias tratam): Modelos quânticos avançados, espectroscopia, reações orgânicas e propriedades nucleares aprofundadas.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Classificação periódica dos elementos químicos.
- Tabela Periódica: histórico e evolução.
- Classificação dos elementos em metais, não metais, semimetais e gases nobres.
- Configuração eletrônica dos elementos ao longo da Tabela Periódica.
- Propriedades periódicas e aperiódicas.
- Configuração eletrônica.
- Tabela periódica atual e sua estrutura.
- Lei periódica.
- Principais subgrupos de elementos físicos.
- Classificação periódica dos elementos químicos: tabela periódica atual e sua estrutura, Lei de Moseley, período, grupo e subgrupo, elemento representativo, de transição e gás nobre.
- Propriedades periódicas: raios atômico e iônico, energia de ionização e eletronegatividade.
- Classificação periódica e propriedades periódicas.
- Elementos químicos, símbolos e propriedades periódicas.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- histórico e evolução da tabela (Döbereiner, Newlands, Mendeleev, Moseley) com 6 questões
- propriedades aperiódicas (massa atômica, calor específico, índice de refração) em contraste com as periódicas, com 5 questões
- Lei de Moseley e histórico da tabela periódica, com 6 questões

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
  "id": "ciencias.tabela-periodica",
  "subject": "ciencias",
  "title": "Tabela periódica e propriedades periódicas",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Tabela periódica e propriedades periódicas"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.tabela-periodica.json.

==================================================
# MATÉRIA 692 — legislacao.crime-cibernetico-convencao
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação: Convenção sobre o Crime Cibernético (Convenção de Budapeste)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Categorias de condutas previstas na Convenção de Budapeste, poderes processuais sobre prova digital e cooperação internacional.

Fica de fora (outras matérias tratam): Estudo completo de cada tipo penal brasileiro e normas técnicas de perícia ou segurança de redes.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Decreto nº 11.491/2023 (Convenção sobre o Crime Cibernético).
- Convenção sobre o Crime Cibernético (Convenção de Budapeste).
- Provas digitais.
- Decreto nº 11.491/2023 (Crime Cibernético).
- Convenção de Budapeste: Decreto nº 11.491, de 12 de abril de 2023 - Promulga a Convenção sobre o Crime Cibernético.
- Decreto nº 11.491/2023 (Promulga a Convenção sobre o Crime Cibernético).
- Convenção sobre o Crime Cibernético (Decreto 11.491/2023).

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Aprofundar a Convenção por artigos: condutas dos arts. 2 a 10 (acesso ilegal, interceptação, interferência em dados e sistemas, uso indevido de dispositivos, fraude, pornografia infantil, direitos autorais), arts. 14 a 21 e cooperação dos arts. 23 a 35, mais reservas do Brasil e 12 questões de nível de prova de juiz.
- Capítulo sobre provas digitais: cadeia de custódia, preservação e integridade de evidência eletrônica, requisição de dados a provedores, acesso a dados armazenados e validade da prova, com questões.
- Aprofundar o Decreto 11.491/2023 por artigos (condutas dos arts. 2 a 10, medidas processuais dos arts. 16 a 21, cooperação e ponto de contato 24/7) com 10 questões de nível de delegado.
- Aprofundar as medidas processuais e a cooperação internacional da Convenção por artigos (preservação, ordem de apresentação, busca e apreensão, interceptação, extradição, auxílio mútuo) com questões de nível de delegado de PF.

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
  "id": "legislacao.crime-cibernetico-convencao",
  "subject": "Legislação",
  "title": "Convenção sobre o Crime Cibernético (Convenção de Budapeste)",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Convenção sobre o Crime Cibernético (Convenção de Budapeste)"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao.crime-cibernetico-convencao.json.

==================================================
# MATÉRIA 693 — portugues.argumentacao-adequacao-textual
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Língua Portuguesa: Argumentação, pertinência e adequação textual** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Adequação conceitual, pertinência e relevância de argumentos; relação entre tese, razões e evidências; articulação lógica e progressão; consideração de contrapontos; seleção vocabular conforme o sentido, leitor e finalidade.

Fica de fora (outras matérias tratam): Catálogo formal de falácias, produção integral de dissertação e revisão gramatical de todos os níveis linguísticos.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Argumentação e persuasão.
- Defeitos de conteúdo: descontextualização, generalização, simplismo, obviedade, paráfrase, cópia, tautologia, contradição.
- Consistência argumentativa.
- Adequação conceitual, pertinência, relevância e articulação dos argumentos e seleção vocabular.
- Adequação conceitual.
- Pertinência, relevância e articulação dos argumentos.
- Avaliação de argumentos: fatores que reforçam ou enfraquecem uma argumentação.
- Estruturação argumentativa lógica.
- Tipos de argumento.
- Textos da esfera jornalística/publicitária — Análise linguística: Analise as estratégias retóricas e de persuasão que visam promover o apelo ao consumo em anúncios publicitários.
- Argumentação no texto: referenciação, sequenciação, operadores argumentativos, modalizadores, seleção lexical, intensificadores e índices de avaliação.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Defeitos de conteúdo do texto (descontextualização, simplismo, obviedade, paráfrase, cópia, tautologia, contradição) com exemplos e questões; a matéria só toca generalização.
- Capítulo sobre avaliação de argumentos: o que reforça ou enfraquece uma conclusão (premissas, dados, contra-exemplos), com questões no estilo raciocínio crítico.
- Tipos de argumento (autoridade, exemplificação, causa e consequência, comparação, raciocínio lógico, princípio) com exemplos e questões.
- Estratégias retóricas e persuasão em anúncios publicitários (apelo ao consumo), com textos e questões.
- Operadores argumentativos, modalizadores, intensificadores e índices de avaliação, referenciação e sequenciação, com questões.

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
  "id": "portugues.argumentacao-adequacao-textual",
  "subject": "Língua Portuguesa",
  "title": "Argumentação, pertinência e adequação textual",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Argumentação, pertinência e adequação textual"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome portugues.argumentacao-adequacao-textual.json.

==================================================
# MATÉRIA 694 — ciencias.cinetica-quimica
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ciências Naturais: Cinética química** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Velocidade média e instantânea, relação estequiométrica entre velocidades, lei de velocidade experimental, fatores que alteram a rapidez, teoria das colisões, energia de ativação, temperatura, catalisadores e equação de Arrhenius.

Fica de fora (outras matérias tratam): Mecanismos detalhados de reações complexas, catálise enzimática quantitativa, leis integradas de primeira e segunda ordem e equilíbrio químico quantitativo.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Cinética química.
- Velocidades e mecanismos de reação.
- Equação de velocidade, teoria das colisões e complexo ativado.
- Influência da energia, da concentração, da pressão e dos catalisadores na velocidade das reações químicas.
- Equação de Arrhenius e consequências para o desenho de estudos de estabilidade.
- Cinéticas de reação e interpretação de dados: teoria do estado de transição e efeitos de solvente e pH.
- Reações de decomposição de fármacos de ordem zero, primeira, segunda e pseudo-primeira ordem.
- Cinética química: Velocidade da reação; teoria das colisões; a influência da temperatura e a equação de Arrhenius;

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Mecanismos de reação: etapas elementares, etapa lenta determinante da velocidade e molecularidade, com questões.
- Aplicação da equação de Arrhenius a estudos de estabilidade de medicamentos (testes acelerados, extrapolação de prazo de validade) com questões.
- Teoria do estado de transição e efeitos de solvente e de pH sobre a velocidade, com questões.
- Cinética de decomposição de fármacos: leis integradas de ordem zero, primeira, segunda e pseudo-primeira ordem, tempo de meia-vida e t90, com questões.

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
  "id": "ciencias.cinetica-quimica",
  "subject": "Ciências Naturais",
  "title": "Cinética química",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Cinética química"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.cinetica-quimica.json.

==================================================
# MATÉRIA 695 — ciencias.termoquimica
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **ciencias: Termoquímica** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Calor e temperatura em processos químicos, sistemas e vizinhança, reações endotérmicas e exotérmicas, entalpia, equações termoquímicas, calorimetria, entalpias de formação, lei de Hess e estimativas por energia de ligação.

Fica de fora (outras matérias tratam): Cinética química detalhada, equilíbrio químico, termodinâmica estatística e máquinas térmicas.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Termoquímica.
- Energia e calor.
- Reações exotérmicas e endotérmicas.
- Entalpia, entropia e energia livre.
- Entalpias de formação e de combustão das substâncias.
- Calor de reação em pressão constante e em volume constante.
- Lei de Hess.
- Termodinâmica química.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Entropia, espontaneidade e energia livre de Gibbs (ΔG = ΔH - TΔS), com exemplos e questões.
- Calor de reação a pressão constante e a volume constante (ΔH e ΔU, relação com trabalho de expansão), com questões.
- Termodinâmica química: primeira e segunda leis, energia interna, entropia e energia livre, com questões.

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
  "id": "ciencias.termoquimica",
  "subject": "ciencias",
  "title": "Termoquímica",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Termoquímica"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.termoquimica.json.

==================================================
# MATÉRIA 696 — legislacao.lei-6858-1980
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Legislação: Lei 6.858/1980: valores não recebidos em vida** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Pagamento de valores trabalhistas, FGTS e PIS-PASEP não recebidos em vida, ordem entre dependentes previdenciários e sucessores civis, dispensa de inventário ou arrolamento e proteção das quotas de menores.

Fica de fora (outras matérias tratam): Restituições tributárias, saldos bancários e demais hipóteses do art. 2º da Lei 6.858/1980, inventário em geral e regras previdenciárias de habilitação que não sejam necessárias para compreender a destinação prevista na lei.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Alvarás judiciais.
- Lei nº 6.858/1980.
- Leis nº 6.858/1980 e nº 5.584/1970.
- Lei n. 6.858/1980.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Alvarás judiciais em geral (jurisdição voluntária, alvará para venda ou oneração de bens de incapazes, levantamento de valores, autorização de viagem) além dos valores da Lei 6.858/1980, com questões.
- Lei 5.584/1970: assistência judiciária sindical, honorários, normas processuais trabalhistas (alçada, rito sumário, ônus da prova) com questões; a matéria trata só da Lei 6.858/1980.

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
  "id": "legislacao.lei-6858-1980",
  "subject": "Legislação",
  "title": "Lei 6.858/1980: valores não recebidos em vida",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Lei 6.858/1980: valores não recebidos em vida"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome legislacao.lei-6858-1980.json.

==================================================
# MATÉRIA 697 — administracao-publica.governo-digital
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Administração Pública: Governo digital e Lei 14.129/2021** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Diferença entre governo eletrônico e governo digital; dimensões do governo digital segundo a OCDE; Estratégia de Governo Digital 2020–2022; Lei 14.129/2021: objeto, alcance (União e adesão dos demais entes), princípios e diretrizes, conceitos legais (autosserviço, dados abertos, formato aberto, governo como plataforma, laboratório de inovação, plataformas de governo digital, registros de referência, interoperabilidade), prestação digital de serviços, plataformas de governo digital e painel de monitoramento, Base Nacional de Serviços Públicos, laboratórios de inovação, dados abertos e interoperabilidade; noções de gov.br, níveis de conta, assinaturas eletrônicas e identificação pelo CPF; dados abertos (princípios, cinco estrelas, três leis, Política de Dados Abertos e Portal de Dados Abertos), ePING e eMAG; transparência digital e inclusão digital.

Fica de fora (outras matérias tratam): Procedimento de pedidos, prazos, recursos e classificação de sigilo da Lei de Acesso à Informação e transparência fiscal da Lei Complementar 131/2009 (matéria de transparência e de acesso à informação); modelos de administração pública, governança e accountability (matéria de modelos e governança); IoT, big data e inteligência artificial (matéria de transformação digital); tratamento técnico de dados pessoais pela LGPD; certificação digital e criptografia em profundidade.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Governo eletrônico.
- Dados abertos.
- Governo eletrônico, transparência e Governo Digital; Lei nº 14.129/2021.
- Serviços públicos digitais.
- Governo Digital: Estratégia de Governo Digital para 2020 a 2022 na administração pública federal direta, autárquica e fundacional.
- Governo eletrônico e seu impacto na sociedade e na Administração Pública. Lei nº 14.129/2021.
- Governo digital.
- Governo eletrônico: Transparência da administração pública.
- Governo Eletrônico: ePING e eMAG.
- Dados abertos governamentais.
- Lei nº 14.129/2021 – Governo Digital.
- Governo eletrônico e accountability.
- Lei nº 14.129/2021 (Lei do Governo Digital).
- Governo digital e Lei nº 13.460/2017.
- Governo Digital: Estratégia de Governo Digital para 2020 a 2022 no âmbito da administração pública federal direta, autárquica e fundacional.
- Governo eletrônico e seu impacto na sociedade e na Administração Pública.
- Governo Digital e Programa de Gestão e Desempenho.
- Inovação tecnológica no setor público.
- E-govern.
- Governo eletrônico, transparência, controle social, cidadania, accountability e resultados em serviços públicos.
- Sistemas Públicos de Informação.
- Governo Digital: Lei nº 14.129, de 29 de março de 2021.
- Lei nº 14.129/2021: princípios, regras e instrumentos para o Governo Digital e eficiência pública.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- seção sobre a Lei 13.460/2017 (direitos do usuário de serviços públicos, carta de serviços, ouvidoria, conselhos de usuários) e sua relação com o governo digital, com 6 questões
- seção sobre o Programa de Gestão e Desempenho (PGD) e teletrabalho no governo digital, com 4 questões
- seção sobre inovação tecnológica no setor público (laboratórios de inovação, sandbox regulatório, contratações públicas de inovação) e 5 questões
- seção sobre controle social, cidadania, accountability e resultados em serviços públicos digitais, com 6 questões

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
  "id": "administracao-publica.governo-digital",
  "subject": "Administração Pública",
  "title": "Governo digital e Lei 14.129/2021",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Governo digital e Lei 14.129/2021"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome administracao-publica.governo-digital.json.

==================================================
# MATÉRIA 698 — matematica.equivalencias-negacoes-logicas
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática: Equivalências e negações lógicas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Negação, conjunção, disjunção, condicional, bicondicional e equivalências fundamentais, inclusive leis de De Morgan e contraposição.

Fica de fora (outras matérias tratam): Dedução em sistemas formais, lógica de predicados avançada e demonstrações algébricas extensas.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Equivalências.
- Leis de De Morgan.
- Lógica sentencial (ou proposicional): Equivalências.
- Leis de Morgan.
- Lógica sentencial (ou proposicional): Leis de De Morgan.
- Equivalências lógicas.
- Lógica sentencial (ou proposicional): Leis de Morgan.
- Lógica sentencial ou proposicional: equivalências.
- Lógica sentencial ou proposicional: leis de De Morgan.
- Equivalências e diagramas lógicos.
- Equivalências e negações.
- Tabelas-verdade, equivalências, leis de Morgan e problemas.
- Equivalências lógicas e raciocínio crítico.
- Implicação lógica e contrapositiva.
- Equivalências lógicas e leis de De Morgan.
- Leis de Morgan, tautologia, contradição e contingência.
- Equivalências lógicas e leis de Morgan.
- Equivalências e implicações lógicas.
- Equivalências e leis de De Morgan.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- implicação lógica (p ⇒ q como condicional tautológica), diferença entre implicação e equivalência e regras de inferência básicas (modus ponens, modus tollens), com cerca de 6 questões; a contrapositiva já está coberta
- tautologia, contradição e contingência (definição, verificação por tabela-verdade e exemplos como p ∨ ¬p e p ∧ ¬p) com cerca de 6 questões; as leis de De Morgan já estão cobertas
- implicação lógica (p ⇒ q como condicional tautológica), diferença entre implicação e equivalência e regras como modus ponens e modus tollens, com cerca de 6 questões

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
  "id": "matematica.equivalencias-negacoes-logicas",
  "subject": "Matemática",
  "title": "Equivalências e negações lógicas",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Equivalências e negações lógicas"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica.equivalencias-negacoes-logicas.json.

==================================================
# MATÉRIA 699 — administracao-publica.regulacao-economica
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Administração Pública: Regulação econômica** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Justificativas e teorias da regulação, poder de mercado, monopólio natural, externalidades e informação, falhas de governo e captura, incentivos e instrumentos, AIR e avaliação de resultados, governança e atuação de agências em noções.

Fica de fora (outras matérias tratam): Natureza jurídica das autarquias, cargos e mandatos, regulação detalhada de setores, defesa da concorrência processual e modelos econômicos avançados.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Regulação econômica.
- Teoria da regulação, princípios, motivação e conceitos.
- Captura regulatória, agente-principal, escolha pública e custos de transação.
- Regulação sanitária, econômica e setorial e agências reguladoras.
- Regulação estatal, sanitária e econômica, agências reguladoras e regulação de mercados.
- Atividade regulatória da Administração Pública.
- Teoria geral da regulação Análise de Impacto Regulatório.
- Teoria da captura.
- Administração regulatória.
- Abuso regulatório.
- Regulação, fomento, planejamento, serviço público e poder de polícia.
- Análise de impacto regulatório.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- agente-principal, escolha pública (public choice) e custos de transação, com 8 questões
- fundamento constitucional da administração regulatória (Estado como agente normativo e regulador) com 6 questões
- abuso regulatório e liberdade econômica (vedação a normas que criem reserva de mercado, barreiras e custos desnecessários), com 6 questões
- fomento, planejamento, serviço público e poder de polícia como formas de atuação do Estado, comparados à regulação, com 8 questões

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
  "id": "administracao-publica.regulacao-economica",
  "subject": "Administração Pública",
  "title": "Regulação econômica",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Regulação econômica"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome administracao-publica.regulacao-economica.json.

==================================================
# MATÉRIA 700 — ciencias.oscilacoes-simples-amortecidas-forcadas
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ciências Naturais: Oscilações simples, amortecidas e forçadas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Movimento harmônico simples, oscilação amortecida e forçada, ressonância, ondas em meios elásticos e som, estática e dinâmica dos fluidos, temperatura, dilatação térmica, combustão, termodinâmica e resposta do concreto a incêndios.

Fica de fora (outras matérias tratam): Modelagem avançada por equações diferenciais, projeto estrutural, dimensionamento profissional de instalações hidráulicas e análise química quantitativa de combustíveis.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Movimento harmônico simples.
- Oscilações livres, amortecidas e forçadas.
- Frequências naturais e ressonância.
- Oscilações simples, amortecidas e forçadas; Ondas em meios elásticos; Ondas sonoras; Estática e Dinâmica dos Fluidos; Temperatura; Dilatação Térmica; Combustão; Termodinâmica; Ação do incêndio sobre as estruturas de concreto;
- Ondulatória: movimento harmônico simples, oscilações livres, amortecidas e forçadas.
- Ondas: MHS, ondas sonoras e efeito Doppler.
- Oscilações e ondas: movimento harmônico simples.
- Energia no movimento harmônico simples.
- Movimento harmônico simples, oscilações livres, amortecidas e forçadas.
- Ondulatória: Movimento harmônico simples.
- Ondulatória: Oscilações livres, amortecidas e forçadas.
- Ondulatória: Frequências naturais e ressonância.
- Vibrações mecânicas.
- Mecânica dos fluidos.
- Mecânica dos fluidos: hidrostática e hidrodinâmica.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- efeito Doppler (fontes e observadores em movimento, fórmula e casos limites) com 6 questões
- vibrações mecânicas em nível de engenharia: sistema massa-mola-amortecedor, graus de liberdade, frequências naturais e transmissibilidade, com 10 questões
- mecânica dos fluidos em nível de engenharia (propriedades, viscosidade, Bernoulli, perdas de carga) com 10 questões
- hidrostática e hidrodinâmica em nível de engenharia (Bernoulli, continuidade, escoamento) com 10 questões

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
  "id": "ciencias.oscilacoes-simples-amortecidas-forcadas",
  "subject": "Ciências Naturais",
  "title": "Oscilações simples, amortecidas e forçadas",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Oscilações simples, amortecidas e forçadas"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.oscilacoes-simples-amortecidas-forcadas.json.

==================================================
# MATÉRIA 701 — ciencias.atomos-moleculas-ions
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ciências Naturais: Átomos, moléculas e íons** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Modelos e estrutura atômica; prótons, nêutrons e elétrons; número atômico Z, número de massa A, isótopos e íons; moléculas e fórmulas; unidade de massa atômica, massas isotópicas e massa atômica média ponderada; organização, grupos, períodos e famílias da tabela periódica.

Fica de fora (outras matérias tratam): Ligação química em profundidade, distribuição eletrônica completa, propriedades periódicas quantitativas, radioatividade e cálculos estequiométricos, tratados em tópicos próprios.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Teoria Atômico-Molecular.
- Modelo do átomo de hidrogênio.
- Modelos atômicos (Dalton, Thomson, Rutherford e Bohr) e evolução dos conceitos de átomo.
- Estrutura do átomo.
- Estrutura atômica.
- Partículas fundamentais do átomo.
- Número atômico e massa atômica.
- Elemento químico.
- Fórmulas químicas.
- Átomos, moléculas e íons: Teoria atômica da matéria; A visão moderna da estrutura atômica; Pesos atômicos ou massas atômicas; A tabela periódica;
- Modelos atômicos: Dalton, Thomson, Rutherford, Rutherford-Bohr, orbitais e distribuição eletrônica.
- Estrutura atômica moderna.
- Estrutura atômica e tabela periódica.
- Modelo atômico de Dalton.
- Estrutura atômica da matéria e modelos atômicos.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- modelo de Bohr para o átomo de hidrogênio: níveis de energia, transições e séries espectrais, com 6 questões
- orbitais e distribuição eletrônica (diagrama de Pauling, números quânticos) com 8 questões; a configuração eletrônica está em ciencias.tabela-periodica
- modelo quântico: orbitais, números quânticos e princípio da incerteza, com 8 questões

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
  "id": "ciencias.atomos-moleculas-ions",
  "subject": "Ciências Naturais",
  "title": "Átomos, moléculas e íons",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Átomos, moléculas e íons"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.atomos-moleculas-ions.json.

==================================================
# MATÉRIA 702 — processo-trabalho.dissidio-coletivo
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Processual do Trabalho: Dissídios coletivos e sentença normativa** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Espécies de dissídio coletivo, comum acordo para o dissídio econômico, poder normativo, extensão, ação de cumprimento, revisão e vigência da sentença normativa.

Fica de fora (outras matérias tratam): Dissídio individual, greve e suas consequências em detalhe, negociação coletiva em geral e recursos não necessários à compreensão do escopo.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Dissídios coletivos: extensão, cumprimento e revisão da sentença normativa.
- Dissídios coletivos.
- Dos dissídios coletivos: extensão, cumprimento e revisão da sentença normativa.
- Dissídios coletivos: competência, legitimidade, extensão, cumprimento, revisão de sentença normativa e efeito suspensivo.
- Conflitos coletivos de trabalho.
- Dissídios coletivos: conceito, classificação.
- Sentença normativa.
- Ação de Cumprimento.
- Competência, procedimento e efeitos da sentença normativa nos dissídios coletivos.
- Dissídios coletivos, competência, legitimidade, extensão, cumprimento e revisão da sentença normativa e efeito suspensivo.
- Dissídios coletivos, extensão, cumprimento e revisão de sentença normativa.
- Dissídios coletivos: Competência para julgamento.
- Dissídios coletivos: Legitimidade para propositura.
- Dissídios coletivos: Efeito suspensivo.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- efeito suspensivo do recurso ordinário contra sentença normativa (concessão pelo presidente do TST) com 5 questões
- efeito suspensivo do recurso ordinário contra sentença normativa, com 5 questões
- efeito suspensivo do recurso ordinário contra sentença normativa, com 6 questões

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
  "id": "processo-trabalho.dissidio-coletivo",
  "subject": "Direito Processual do Trabalho",
  "title": "Dissídios coletivos e sentença normativa",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Dissídios coletivos e sentença normativa"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-trabalho.dissidio-coletivo.json.

==================================================
# MATÉRIA 703 — processo-trabalho.nulidades-excecoes
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Processual do Trabalho: Nulidades e exceções no processo do trabalho** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Nulidades processuais trabalhistas; prejuízo manifesto; arguição e preclusão; saneamento e efeitos da nulidade; exceção de incompetência territorial, prazos e suspensão; suspeição e impedimento; recorribilidade incidental estritamente conexa.

Fica de fora (outras matérias tratam): Recursos em geral, competência material detalhada, teoria da prova, mérito contratual e incidentes sem vínculo com nulidade, território ou imparcialidade.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Exceções.
- Das nulidades e das exceções: hipóteses ensejadoras, prazo e forma de arguição.
- Juiz do Trabalho: poderes, impedimento e suspeição.
- Honorários advocatícios e nulidades: princípio, momento de arguição e preclusão.
- Nulidades e exceções: hipóteses, prazo e forma de arguição.
- Nulidades no processo trabalhista.
- Justiça do Trabalho: Impedimento e suspeição.
- Nulidades no processo do trabalho.
- Nulidades no processo do trabalho: Princípio informador.
- Nulidades no processo do trabalho: Momento de arguição.
- Nulidades no processo do trabalho: Preclusão.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Capítulo sobre poderes do juiz do trabalho (direção do processo, art. 765 da CLT, poderes instrutórios, conciliação, polícia de audiência) e questões; hoje a matéria só cobre impedimento e suspeição.
- Capítulo sobre honorários advocatícios no processo do trabalho (art. 791-A da CLT, sucumbência recíproca, beneficiário da justiça gratuita, honorários contratuais) e questões que os liguem às nulidades e à preclusão.

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
  "id": "processo-trabalho.nulidades-excecoes",
  "subject": "Direito Processual do Trabalho",
  "title": "Nulidades e exceções no processo do trabalho",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Nulidades e exceções no processo do trabalho"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-trabalho.nulidades-excecoes.json.

==================================================
# MATÉRIA 704 — ciencias.estequiometria
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ciências Naturais: Estequiometria** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Equações químicas e balanceamento, padrões de reatividade, massa molecular e molar, relações quantitativas em equações balanceadas e identificação de reagente limitante.

Fica de fora (outras matérias tratam): Cinética química, equilíbrio, cálculos de rendimento percentual, soluções e gases reais, salvo quando necessários para contextualizar uma proporção estequiométrica.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Conceitos decorrentes da Teoria Atômico-Molecular: unidade de massa atômica (u), quantidade de matéria, massa molar, volume molar.
- Cálculos estequiométricos.
- Massa molecular.
- Número de Avogadro, mol, massa molecular, volume molecular.
- Balanceamento de equação química.
- Estequiometria.
- Balanceamento de equações químicas.
- Estequiometria: cálculos com fórmulas e equações químicas; Equações químicas; Reatividade química; Massa molecular; Informações quantitativas de equações balanceadas; Reagentes limitantes.
- Grandezas químicas e estequiometria.
- Leis ponderais (Lavoisier, Proust, Dalton e Richter-Wenzel-Berzelius).
- Fórmulas químicas.
- Balanceamento de equações.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Unidade de massa atômica (u), massa atômica média e volume molar (22,4 L/mol nas CNTP, lei de Avogadro), com questões; a matéria cobre mol e massa molar mas não u nem volume molar.
- Capítulo sobre as leis ponderais (Lavoisier, Proust, Dalton e Richter-Wenzel) com exemplos e questões; hoje só há conservação de massa de passagem.
- Fórmulas químicas: mínima, molecular e percentual (composição centesimal), determinação a partir de massas, com questões.

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
  "id": "ciencias.estequiometria",
  "subject": "Ciências Naturais",
  "title": "Estequiometria",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Estequiometria"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.estequiometria.json.

==================================================
# MATÉRIA 705 — direito-trabalho.fgts
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito do Trabalho: FGTS** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Lei 8.036/1990: depósitos, remuneração-base e alíquotas, saques, indenizações de 40% e 20%, prescrição do FGTS à luz do Tema 608 do STF e efeitos do FGTS na rescisão.

Fica de fora (outras matérias tratam): Contribuições previdenciárias, seguro-desemprego, regras completas de financiamento habitacional e regimes próprios de servidores.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- FGTS.
- Do Fundo de Garantia do Tempo de Serviço (FGTS).
- Certificado de Regularidade do FGTS.
- Fundo de Garantia do Tempo de Serviço (FGTS).
- Fundo de Garantia do Tempo de Serviço.
- FGTS: possibilidades e condições de utilização e saque.
- Programa Seguro-Desemprego, Abono Salarial, PIS e FGTS.
- FGTS: utilização e saque, Certificado de Regularidade e Guia de Recolhimento.
- Fundo de Garantia do Tempo de Serviço: possibilidades e condições de utilização e saque.
- Lei nº 8.036/1990 (FGTS): possibilidades e condições de utilização/saque.
- Guia de Recolhimento (GRF).
- FGTS e PIS/PASEP.
- Gratificação de Natal e FGTS.
- Do FGTS.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Certificado de Regularidade do FGTS (CRF): finalidade, exigência para contratos e financiamentos, validade e casos de emissão, com questões.
- Guia de Recolhimento do FGTS (GRF) e GFIP/eSocial: quem recolhe, prazo, campos e retificação, com questões.
- Relação entre FGTS e PIS/PASEP (cotas, abono salarial, saque do PIS) com questões; a matéria não trata de PIS/PASEP.
- Incidência do FGTS sobre o 13º salário e demais parcelas remuneratórias (gratificação natal, horas extras, aviso prévio), com questões.

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
  "id": "direito-trabalho.fgts",
  "subject": "Direito do Trabalho",
  "title": "FGTS",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["FGTS"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-trabalho.fgts.json.

==================================================
# MATÉRIA 706 — etica.conflito-interesses-nepotismo
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ética: Conflito de interesses e nepotismo na administração federal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Prevenção e identificação de conflitos de interesses durante e após o exercício de cargo ou emprego federal; informação privilegiada; consulta e medidas preventivas; nepotismo, parentesco, favorecimento e designações recíprocas.

Fica de fora (outras matérias tratam): Regras gerais de licitação, processo disciplinar completo, improbidade e regimes de conflito de interesses de carreiras específicas.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Lei de Conflito de Interesses - LCI (Lei nº 12.813/2013).
- Lei de Conflito de Interesses.
- Conflito de interesses no Poder Executivo Federal e impedimentos posteriores ao exercício do cargo ou emprego; Código de Ética da Alta Administração.
- Conflito de interesses.
- Conflito de interesses no exercício de cargo ou emprego do Poder Executivo federal.
- Conflito de interesses no Poder Executivo federal.
- Conflito de interesses no exercício de cargo ou emprego do Poder Executivo Federal e impedimentos posteriores ao exercício.
- Conflito de interesses: Lei nº 12.813/2013.
- Ética pública, conflito de interesses e Código de Ética da Alta Administração.
- Lei de Conflito de Interesses (Lei nº 12.813/2013).
- Conflito de interesses e nepotismo (Lei 12.813/2013 e Decreto 7.203/2010).

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Código de Conduta da Alta Administração Federal (finalidade, normas de conduta, Comissão de Ética Pública, sanções) e as quarentenas, com questões.
- Código de Conduta da Alta Administração Federal e ética pública (princípios, Comissão de Ética Pública, sanção de censura) com questões.

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
  "id": "etica.conflito-interesses-nepotismo",
  "subject": "Ética",
  "title": "Conflito de interesses e nepotismo na administração federal",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Conflito de interesses e nepotismo na administração federal"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome etica.conflito-interesses-nepotismo.json.

==================================================
# MATÉRIA 707 — direito.regras-minimas-tratamento-presos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direitos Humanos: Regras Mínimas da ONU para o Tratamento de Pessoas Presas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Regras de Mandela sobre dignidade, separação e condições de custódia, saúde, disciplina, isolamento, contatos, reclamações, inspeções e reintegração social.

Fica de fora (outras matérias tratam): Execução penal brasileira em detalhe, arquitetura penitenciária, investigação criminal e padrões regionais de visitação.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Regras mínimas da ONU para o tratamento de pessoas presas.
- Regras de Mandela, Regras de Bangkok, princípios das Nações Unidas para o tratamento de pessoas presas e Convenção Americana sobre Direitos Humanos.
- Regras Mínimas das Nações Unidas para o Tratamento de Presos (Regras de Mandela).
- Regras Mínimas Padrão das Nações Unidas para o Tratamento de Prisioneiros (Regras de Mandela).
- Segurança pública e direitos humanos: Direitos da pessoa presa.
- Regras Mínimas das Nações Unidas para o Tratamento de Presos (Regras Nelson Mandela).
- Regras das Nações Unidas para o Tratamento de Mulheres Presas e Medidas não Privativas de Liberdade para Mulheres Infratoras (Regras de Bangkok).
- Das Regras de Mandela.
- Regras de Bangkok (Resolução 2.010/16 das Nações Unidas).
- Regras internacionais sobre pessoas presas, mulheres privadas de liberdade e medidas não privativas de liberdade.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Regras de Bangkok, princípios da ONU para o tratamento de presos e Convenção Americana (garantias da pessoa presa), com questões.
- Regras de Bangkok: tratamento de mulheres presas, gestantes, filhos, revista e medidas não privativas de liberdade, com questões.
- Regras de Bangkok (Resolução 2010/16 da ONU): conteúdo e aplicação, com questões.
- Regras de Bangkok e Regras de Tóquio (medidas não privativas de liberdade), com questões.

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
  "id": "direito.regras-minimas-tratamento-presos",
  "subject": "Direitos Humanos",
  "title": "Regras Mínimas da ONU para o Tratamento de Pessoas Presas",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Regras Mínimas da ONU para o Tratamento de Pessoas Presas"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito.regras-minimas-tratamento-presos.json.

==================================================
# MATÉRIA 708 — minas-gerais.barragens
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ciências Humanas: Barragens** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Tipos e inventários de barragens em Minas Gerais, segurança, monitoramento, prevenção, efeitos socioambientais e gestão de resíduos sólidos e líquidos.

Fica de fora (outras matérias tratam): Dimensionamento estrutural, cálculos de engenharia, licenciamento detalhado de cada empreendimento e dados numéricos de inventários sujeitos a atualização.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Segurança de barragens.
- Barragens: Barragens em Minas Gerais: Inventário das barragens existentes, incluindo barragens de contenção de rejeitos e barragens hidrelétricas. Segurança de Barragens: Avaliação da segurança das barragens em Minas Gerais, incluindo monitoramento e prevenção de acidentes. Impactos Socioambientais: Análise dos impactos socioambientais das barragens, incluindo deslocamento de comunidades e alterações nos ecossistemas aquáticos. Gestão de Resíduos: Tratamento e disposição adequada de resíduos sólidos e líquidos associados às barragens em Minas Gerais.
- Política Nacional de Segurança de Barragens.
- Barragens.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Política Nacional de Segurança de Barragens (Lei 12.334/2010 e alterações): classificação, plano de segurança, responsabilidades e sanções, em nível de juiz, com questões.
- Política Nacional de Segurança de Barragens em detalhe (objetivos, fundamentos, instrumentos, SNISB, classificação por risco e dano potencial) com questões.
- Tipos construtivos e componentes de barragens (terra, enrocamento, concreto), noções de engenharia e fiscalização de obras, com questões.

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
  "id": "minas-gerais.barragens",
  "subject": "Ciências Humanas",
  "title": "Barragens",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Barragens"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome minas-gerais.barragens.json.

==================================================
# MATÉRIA 709 — direito-trabalho.estabilidade-garantias
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito do Trabalho: Estabilidade e garantias provisórias de emprego** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Estabilidade decenal e direito adquirido; dirigente sindical; cipeiro; garantia acidentária do art. 118 da Lei 8.213/1991; representantes dos empregados na Comissão de Conciliação Prévia; despedida, reintegração e indenização substitutiva, com legislação e jurisprudência aplicáveis.

Fica de fora (outras matérias tratam): Garantia da gestante, estabilidade dos servidores estatutários, outras garantias especiais de emprego e estudo geral da organização sindical, da segurança do trabalho, da previdência ou do procedimento de conciliação.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Estabilidade e garantias provisórias de emprego: formas de estabilidade; despedida e reintegração de empregado estável.
- Estabilidade e garantias provisórias de emprego: formas de estabilidade.
- Despedida e reintegração de empregado estável.
- Da estabilidade e das garantias provisórias de emprego: das formas de estabilidade.
- Da despedida e da reintegração de empregado estável.
- Estabilidade e garantias provisórias de emprego.
- Estabilidade e garantias provisórias de emprego: Despedida e reintegração de empregado estável.
- Estabilidade e garantias provisórias de emprego, despedida e reintegração.
- Estabilidade.
- Reintegração do empregado estável.
- Estabilidade especial.
- Estabilidade, garantias provisórias, direitos coletivos, sindicatos e greve.
- Estabilidade e reintegração do empregado estável.
- Inquérito para apuração de falta grave.
- Estabilidade Provisória no Emprego: Estabilidade provisória conforme o artigo 118 da Lei nº 8.213/1991.
- Estabilidade, garantias provisórias, FGTS, prescrição e decadência trabalhistas.
- Estabilidade e garantias provisórias de emprego: formas de estabilidade, despedida e reintegração de empregado estável.
- Aviso prévio, estabilidade e garantias provisórias de emprego, reintegração e força maior.
- Aviso prévio, estabilidade e garantias provisórias, reintegração e força maior.
- Aviso prévio, estabilidade, garantias provisórias de emprego, reintegração e força maior.
- Estabilidade e garantias provisórias de emprego: Espécies de estabilidade.
- Da estabilidade e das garantias provisórias de emprego.
- Formas de estabilidade.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- força maior (arts. 501 a 503 da CLT: conceito, imprevisibilidade, redução proporcional do salário, indenização reduzida pela metade) com cerca de 6 questões; estabilidade e garantias já estão cobertas
- força maior (arts. 501 a 503 da CLT: conceito, imprevisibilidade, redução proporcional do salário, indenização reduzida pela metade e extinção da empresa) com cerca de 6 questões; estabilidade e garantias já estão cobertas e o aviso prévio tem matéria própria

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
  "id": "direito-trabalho.estabilidade-garantias",
  "subject": "Direito do Trabalho",
  "title": "Estabilidade e garantias provisórias de emprego",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Estabilidade e garantias provisórias de emprego"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-trabalho.estabilidade-garantias.json.

==================================================
# MATÉRIA 710 — direito-constitucional.trf-juizes-federais
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Constitucional: Tribunais Regionais Federais e juízes federais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Arts. 106 a 110 da Constituição Federal: órgãos da Justiça Federal, composição e competências dos TRFs, competências dos juízes federais, incidente de deslocamento de competência em grave violação de direitos humanos, seções judiciárias e Justiça Federal nos Territórios.

Fica de fora (outras matérias tratam): Competências do STF e do STJ, organização administrativa não prevista nos arts. 106 a 110, processo civil e penal infraconstitucional e demais ramos do Poder Judiciário.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Tribunais Regionais Federais e Juízes Federais.
- Competência da Justiça Federal.
- Justiça federal: organização e competência.
- Justiça Federal e direitos humanos.
- A Justiça Federal de 1º Grau.
- Tribunais Regionais Federais e juízes federais;
- Organização judiciária da Justiça Federal e do Estado de Minas Gerais.
- A Justiça Militar e a Justiça Federal.
- Tribunais regionais federais e juízes federais; tribunais e juízes do trabalho; tribunais e juízes eleitorais.
- Juízes federais e estaduais.
- A Justiça Federal de 1.º Grau.
- Poder Judiciário Federal.
- TRF.
- Justiça Federal Comum.
- Incidente de deslocamento de competência para a Justiça Federal em crimes contra direitos humanos.
- Justiça Federal — organização e competência —.
- Tribunais regionais federais.
- Organização da Justiça Federal.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- organização judiciária de Minas Gerais (TJMG, comarcas, juízes de direito) em comparação com a Justiça Federal, com 8 questões
- Justiça Militar e conflitos de competência com a Justiça Federal, com 6 questões; ver direito-constitucional.tribunais-juizes-militares-estados
- juízes estaduais e competência delegada/residual em relação aos juízes federais, com 6 questões

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
  "id": "direito-constitucional.trf-juizes-federais",
  "subject": "Direito Constitucional",
  "title": "Tribunais Regionais Federais e juízes federais",
  "version": 4,
  "status": "PUBLISHED",
  "aliases": ["Tribunais Regionais Federais e juízes federais"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome direito-constitucional.trf-juizes-federais.json.

==================================================
# MATÉRIA 711 — processo-trabalho.embargos-execucao
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Processual do Trabalho: Embargos à execução, impugnação e embargos de terceiro** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Art. 884 da CLT: prazo, matérias e garantia; impugnação do exequente e à sentença de liquidação; embargos de terceiro do CPC, arts. 674 a 681; agravo de petição, prazo, delimitação e prosseguimento da parte incontroversa.

Fica de fora (outras matérias tratam): Citação inicial da execução, avaliação, leilão e expropriação, recursos ordinários, recurso de revista e embargos declaratórios, tratados em tópicos próprios.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Embargos à execução; impugnação à sentença; embargos de terceiro.
- Dos embargos à execução.
- Da impugnação à sentença.
- Dos embargos de terceiros.
- Embargos à execução no processo trabalhista.
- Embargos do executado e impugnação do exequente.
- Embargos à execução, impugnação à liquidação e embargos de terceiro.
- Embargos à execução; impugnação à sentença; embargos de terceiros.
- Liquidação, embargos e impugnações.
- Embargos à execução, impugnação à sentença e embargos de terceiros.
- Embargos à execução, impugnação, embargos de terceiros, praça, leilão, arrematação e custas.
- Embargos à execução, impugnação, embargos de terceiro, praça, leilão, arrematação e custas na execução.
- Impugnação à sentença.
- Embargos de terceiro.
- Embargos à execução: Impugnação à sentença de liquidação.
- Embargos à execução: Embargos de terceiro.
- Dos embargos à execução; da impugnação à sentença; dos embargos de terceiros.
- Recursos no processo de execução.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- liquidação de sentença (cálculo, artigos, arbitramento, impugnação à conta) integrada aos embargos, com 8 questões; ver processo-trabalho.liquidacao-sentenca
- praça, leilão, arrematação e custas na execução, com 10 questões; ver processo-trabalho.praca-leilao-arrematacao
- praça, leilão, arrematação e custas na execução, com 10 questões

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
  "id": "processo-trabalho.embargos-execucao",
  "subject": "Direito Processual do Trabalho",
  "title": "Embargos à execução, impugnação e embargos de terceiro",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Embargos à execução, impugnação e embargos de terceiro"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-trabalho.embargos-execucao.json.

==================================================
# MATÉRIA 712 — fisica.trabalho-energia-potencia
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Física: Trabalho, energia e potência** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Trabalho de força constante, energia cinética e potencial, conservação e transformação da energia mecânica, potência e rendimento.

Fica de fora (outras matérias tratam): Impulso, quantidade de movimento, colisões e desenvolvimento termodinâmico não integram este recorte.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Trabalho.
- Potência.
- Trabalho, potência, rendimento, energia: mecânica, cinética, energia potencial e energia mecânica.
- Conservação de energia mecânica.
- Energia: conservação e suas transformações, impulso.
- Energia.
- Trabalho: trabalho da força-peso e da força elástica, trabalho de uma força, potência e rendimento.
- Energia cinética: trabalho e variação de energia cinética.
- Sistemas conservativos: energia potencial gravitacional, energia mecânica e conservação de energia mecânica.
- Trabalho, potência, energia cinética e potencial, atrito, conservação e transformação da energia.
- Energia, conservação e suas transformações, impulso.
- Trabalho, potência, energia cinética e potencial; conservação de energia.
- Trabalho, potência, energia, conservação e transformações de energia.
- Mecânica: Trabalho, potência e energia mecânica.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- impulso e quantidade de movimento (teorema do impulso) com 8 questões; ver fisica.impulso-quantidade-movimento-colisoes
- trabalho da força elástica e energia potencial elástica (mola) com 8 questões
- impulso e quantidade de movimento com 8 questões

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
  "id": "fisica.trabalho-energia-potencia",
  "subject": "Física",
  "title": "Trabalho, energia e potência",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Trabalho, energia e potência"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome fisica.trabalho-energia-potencia.json.

==================================================
# MATÉRIA 713 — ciencias.gases
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ciências Naturais: Gases** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Características macroscópicas e modelo cinético dos gases, pressão, leis de Boyle, Charles, Gay-Lussac e Avogadro, relação combinada, equação do gás ideal, conversões de temperatura e pressão, desvios do modelo ideal e noções de reconhecimento de riscos de gases tóxicos.

Fica de fora (outras matérias tratam): Termodinâmica avançada, cálculos de equilíbrio químico, toxicologia clínica detalhada, limites ocupacionais específicos e procedimentos operacionais de resposta a emergências.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Gases.
- Teoria cinética dos gases.
- Transformações gasosas.
- Lei de Avogadro.
- Leis dos gases.
- Densidade dos gases.
- Difusão e efusão dos gases.
- Misturas gasosas.
- Gases: Características dos gases; Pressão; As leis dos gases; A equação do gás ideal; Gases tóxicos.
- Teoria cinética dos gases ideais.
- Termodinâmica: gases perfeitos.
- Teoria cinética dos gases, energia interna, lei de Joule e transformações gasosas.
- Termodinâmica: Teoria cinética dos gases.
- Transformações gasosas isotérmicas, isobáricas, isocóricas e adiabáticas.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- difusão e efusão dos gases (lei de Graham) com 6 questões
- gases perfeitos na termodinâmica: energia interna, primeira lei, trabalho em transformações e ciclos, com 10 questões
- transformação adiabática (relação entre P, V e T, expoente de Poisson) com 6 questões

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
  "id": "ciencias.gases",
  "subject": "Ciências Naturais",
  "title": "Gases",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Gases"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.gases.json.

==================================================
# MATÉRIA 714 — matematica.polinomios-equacoes-polinomiais
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática: Polinômios e equações polinomiais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Definição, grau, coeficientes, operações, divisão e resto, fatoração, raízes, multiplicidade, relações entre raízes e coeficientes e equações polinomiais.

Fica de fora (outras matérias tratam): Resolução numérica aproximada de equações de grau elevado e teoria abstrata de corpos e anéis.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Fatoração algébrica.
- Polinômios.
- Polinômios e equações polinomiais.
- Polinômios: conceito.
- Identidade de polinômios.
- Operações com polinômios.
- Propriedades fundamentais da divisão de polinômios.
- Raiz ou zero de um polinômio.
- Fração polinomial e frações polinomiais idênticas.
- Polinômios e equações polinomiais: operações, valor numérico, raízes.
- Funções polinomiais.
- Polinômios e Produtos Notáveis.
- Equações polinomiais.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- frações polinomiais e identidade de frações (decomposição e simplificação) com 6 questões
- funções polinomiais (afim, quadrática, cúbica): gráficos, raízes e sinais, com 8 questões
- produtos notáveis (quadrado da soma, diferença de quadrados, cubo) com 8 questões

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
  "id": "matematica.polinomios-equacoes-polinomiais",
  "subject": "Matemática",
  "title": "Polinômios e equações polinomiais",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Polinômios e equações polinomiais"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica.polinomios-equacoes-polinomiais.json.

==================================================
# MATÉRIA 715 — ciencias.radioatividade
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **ciencias: Radioatividade e transformações nucleares** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Núcleo atômico e estabilidade, radioatividade natural, emissões alfa, beta e gama, equações nucleares, meia-vida e atividade, aplicações e proteção radiológica em nível introdutório, fissão e fusão nucleares.

Fica de fora (outras matérias tratam): Projeto de reatores, dosimetria clínica avançada, cadeia de decaimento extensa e cálculos nucleares especializados.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Radioatividade.
- Energia nuclear.
- Núcleo atômico.
- Natureza das emissões radioativas.
- Leis da radioatividade.
- Cinética da desintegração radioativa.
- Fenômenos de fissão nuclear e fusão nuclear.
- Riscos e aplicações das reações nucleares.
- Princípios básicos da emissão de radioatividade, radiações ionizantes e decaimento radioativo.
- Espectro e fontes de radiações ionizantes.
- Geração de raios X e irradiação gama, beta e alfa.
- Proteção contra radiações ionizantes.
- Medicina nuclear.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Capítulo sobre espectro eletromagnético, radiações ionizantes e não ionizantes e fontes naturais e artificiais de radiação (radônio, raios cósmicos, equipamentos), com questões.
- Capítulo sobre produção de raios X (tubo, bremsstrahlung, raios X característicos), irradiadores gama e comparação de poder de penetração de alfa, beta, gama e X, com questões.
- Aprofundar proteção radiológica: grandezas dosimétricas (dose absorvida, equivalente, efetiva), limites de dose, princípios de justificação, otimização (ALARA) e limitação, e normas de radioproteção, com questões; hoje só nível introdutório.

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
  "id": "ciencias.radioatividade",
  "subject": "ciencias",
  "title": "Radioatividade e transformações nucleares",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Radioatividade e transformações nucleares"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.radioatividade.json.

==================================================
# MATÉRIA 716 — geografia.demografia-urbanizacao-mundo
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Demografia, migrações e urbanização: Demografia e urbanização mundial** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Distribuição e estrutura populacional, indicadores demográficos, transição demográfica, migrações, urbanização, redes urbanas e desigualdades socioespaciais.

Fica de fora (outras matérias tratam): Projeções estatísticas específicas sem série e estudo local de bairros sem dados fornecidos.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Dinâmica populacional e indicadores da qualidade de vida das populações.
- Movimentos migratórios internacionais e crescimento demográfico.
- Demografia e urbanização.
- A Geografia da população.
- A Geografia da população: Distribuição espacial da população no Brasil e no mundo.
- A Geografia da população: Os grandes movimentos migratórios internacionais e intranacionais.
- Os grandes movimentos migratórios internacionais e intranacionais.
- Processo de urbanização e formação de redes de cidades.
- Geografia da população: distribuição no Brasil e no mundo, migrações internacionais e intranacionais, dinâmica populacional e qualidade de vida.
- Geografia da população: distribuição espacial no Brasil e no mundo, movimentos migratórios e dinâmica populacional e indicadores de qualidade de vida.
- Geografia da população: distribuição, migrações internacionais e intranacionais, dinâmica demográfica e indicadores de qualidade de vida.
- Geografia da população: distribuição espacial no Brasil e no mundo.
- População: teorias, estrutura e distribuição.
- Índices, fluxos e taxas populacionais.
- Questões populacionais: movimentos e problemas.
- Geografia urbana e regional: Formação, dinâmicas, características e transformações das cidades.
- Geografia da População: Dinâmica, crescimento, distribuição e estrutura da população.
- Teorias demográficas.
- Movimentos migratórios.
- Geografia da população.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Distribuição espacial da população no Brasil (regiões, densidade, concentração litorânea, rede urbana nacional) com questões; a matéria é de escala mundial.
- Indicadores de qualidade de vida (IDH, expectativa de vida, mortalidade infantil, analfabetismo, Gini) com questões.
- Capítulo sobre teorias demográficas (malthusiana, neomalthusiana, reformista) e teoria da transição, com questões.

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
  "id": "geografia.demografia-urbanizacao-mundo",
  "subject": "Demografia, migrações e urbanização",
  "title": "Demografia e urbanização mundial",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Demografia e urbanização mundial"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome geografia.demografia-urbanizacao-mundo.json.

==================================================
# MATÉRIA 717 — ciencias.principios-basicos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ciências Naturais: Princípios básicos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Pressão, densidade, pressão hidrostática e absoluta, vasos comunicantes, princípio de Pascal, empuxo, peso aparente e condições de flutuação e equilíbrio de corpos.

Fica de fora (outras matérias tratam): Escoamento de fluidos, equação de Bernoulli, viscosidade, tensão superficial e hidrodinâmica, salvo quando úteis para delimitar o conteúdo.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Princípios de Pascal, Arquimedes e Stevin.
- Densidade.
- Estática dos fluidos.
- Densidade e pressão.
- Princípio de Pascal, Lei de Stevin, Princípio de Arquimedes.
- Princípios básicos: hidrostática, pressões e empuxos; Equilíbrio dos corpos flutuantes.
- Mecânica dos Fluidos.
- Mecânica dos Fluidos: propriedades e natureza dos fluidos.
- Hidrostática e forças da água sobre superfícies.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Hidrodinâmica e mecânica dos fluidos de engenharia (escoamento, equação da continuidade, Bernoulli, perda de carga), que o escopo exclui, com questões.
- Propriedades dos fluidos: viscosidade, compressibilidade, tensão superficial, fluido newtoniano e massa específica, com questões.
- Forças hidrostáticas sobre superfícies planas e curvas submersas (empuxo resultante, centro de pressão) para engenharia, com questões.

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
  "id": "ciencias.principios-basicos",
  "subject": "Ciências Naturais",
  "title": "Princípios básicos",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Princípios básicos"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.principios-basicos.json.

==================================================
# MATÉRIA 718 — processo-trabalho.custas-emolumentos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Processual do Trabalho: Custas, emolumentos e isenções** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
CLT, arts. 789 a 790-A: custas no processo de conhecimento, bases de cálculo e pagamento, custas na execução, emolumentos, justiça gratuita e isenções.

Fica de fora (outras matérias tratam): Art. 790-B da CLT, honorários periciais e honorários advocatícios; despesas não tratadas nos arts. 789 a 790-A.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Custas e emolumentos.
- Das custas e emolumentos.
- Custas na execução.
- Custas e emolumentos para a Fazenda Pública.
- Hipóteses de isenção.
- Das custas na execução.
- Custas e gratuidade de justiça.
- Valor da causa, custas e emolumentos, custas para a Fazenda Pública e hipóteses de isenção.
- Valor da causa, custas e emolumentos e hipóteses de isenção da Fazenda Pública.
- Valor da causa, custas e emolumentos e hipóteses de isenção.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Valor da causa no processo do trabalho (art. 840, § 1º, e rito sumaríssimo) e sua ligação com custas, com questões.

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
  "id": "processo-trabalho.custas-emolumentos",
  "subject": "Direito Processual do Trabalho",
  "title": "Custas, emolumentos e isenções",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Custas, emolumentos e isenções"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-trabalho.custas-emolumentos.json.

==================================================
# MATÉRIA 719 — portugues.estilistica-vicios-linguagem
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Língua Portuguesa: Estilística, vícios de linguagem e qualidades da expressão** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Recursos expressivos e adequação estilística; pleonasmo vicioso, ambiguidade, cacofonia, barbarismo e solecismo; clareza, precisão, concisão, correção e adequação ao contexto comunicativo.

Fica de fora (outras matérias tratam): Catálogo exaustivo de figuras de linguagem, análise literária de estilos individuais e estudo completo de variação linguística ou redação oficial.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Vícios de linguagem e estilo: ruptura de registro linguístico, coloquialismo, barbarismo, anacronismo, rebuscamento, redundância e linguagem estereotipada.
- Vícios de linguagem e estilo: ruptura de registro, coloquialismo, barbarismo, anacronismo, rebuscamento, redundância e linguagem estereotipada.
- Vícios de linguagem.
- Comunicação assertiva: linguagem simples, concisa e objetiva.
- Vícios de linguagem: redundância, prolixidade, ambiguidade e impropriedades textuais.
- Precisão.
- Clareza.
- Concisão.
- Estilística.
- Vícios de linguagem e qualidade da boa linguagem.
- Recursos estilísticos e estruturais (aspectos textuais, gramaticais e convenções da escrita).
- Noções de estilística.
- Problemas estruturais das frases.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Ruptura de registro, coloquialismo, anacronismo, rebuscamento e linguagem estereotipada (clichê), com exemplos e questões.
- Convenções da escrita e recursos estruturais do texto (paralelismo, sequência, estrutura do período), com questões.
- Problemas estruturais de frases (ambiguidade estrutural, paralelismo, truncamento, ordem das palavras), com reescritas e questões.

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
  "id": "portugues.estilistica-vicios-linguagem",
  "subject": "Língua Portuguesa",
  "title": "Estilística, vícios de linguagem e qualidades da expressão",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Estilística, vícios de linguagem e qualidades da expressão"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome portugues.estilistica-vicios-linguagem.json.

==================================================
# MATÉRIA 720 — fisica.leis-newton-atrito
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Física: Leis de Newton, forças e atrito** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Leis de Newton, diagramas de forças, equilíbrio, planos inclinados, atrito estático e cinético, ação e reação e força centrípeta.

Fica de fora (outras matérias tratam): Cálculo de trabalho, energia, impulso e colisões fica fora deste recorte de dinâmica.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Leis de Newton e suas aplicações.
- Leis de Newton.
- Composição de forças: forças de mesma direção e sentido, forças de mesma direção e sentidos diferentes.
- Duas forças concorrentes.
- Princípios fundamentais da Dinâmica (Leis de Newton): inércia e sua relação com sistemas de referência.
- Força peso, força de atrito, força centrípeta e força elástica.
- Força centrífuga, superelevação e superlargura.
- Leis de Newton e suas aplicações; atrito.
- Forças de campo e de contato.
- Movimento e força.
- Mecânica: Leis de Newton e suas aplicações.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Sistemas de referência inerciais e não inerciais e a relação com a primeira lei, com questões.
- Força elástica (lei de Hooke, molas) e força peso em diferentes referenciais, com questões.
- Força centrífuga (referencial não inercial), superelevação e superlargura de curvas em rodovias e ferrovias, com questões.

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
  "id": "fisica.leis-newton-atrito",
  "subject": "Física",
  "title": "Leis de Newton, forças e atrito",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Leis de Newton, forças e atrito"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome fisica.leis-newton-atrito.json.

==================================================
# MATÉRIA 721 — geografia.geologia-relevo-solos
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Geologia, relevo e solos: Geologia, relevo e solos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Estrutura interna da Terra, tectônica de placas, rochas, agentes do relevo, formas de relevo, solos e conservação.

Fica de fora (outras matérias tratam): Classificações geotécnicas detalhadas e cálculo de estabilidade de encostas.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- O espaço natural: estrutura geológica e características do relevo.
- Geologia, geomorfologia, geotecnia, tectônica, minerais, rochas, intemperismo, erosão e sedimentologia.
- Pedologia: gênese, morfologia, composição e propriedades dos solos.
- Pedologia, ciclagem de nutrientes e classificação dos solos.
- Geologia, geomorfologia e hidrologia da Bacia Amazônica.
- Pedologia, ciclagem de nutrientes e classificação de solos.
- Geologia, geomorfologia e hidrologia.
- A dinâmica da Litosfera.
- Relevo terrestre.
- Minerais e rochas.
- Solos: práticas de manejo e conservação.
- Estudo da Terra: Considerações gerais, composição e estrutura da Terra.
- Tectônica de placas.
- Geomorfologia.
- O espaço geográfico brasileiro: características físicas, estrutura física e geomorfológica.
- Tipos de solos.
- Exploração e conservação do solo.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Pedologia: classificação de solos (SiBCS: latossolo, argissolo etc.), horizontes e ciclagem de nutrientes, com questões.
- Hidrologia (bacias hidrográficas, ciclo da água, águas subterrâneas), que a matéria não cobre, com questões.
- Tipos de solos e suas características (latossolos, argissolos, neossolos, etc.), com questões.

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
  "id": "geografia.geologia-relevo-solos",
  "subject": "Geologia, relevo e solos",
  "title": "Geologia, relevo e solos",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Geologia, relevo e solos"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome geografia.geologia-relevo-solos.json.

==================================================
# MATÉRIA 722 — biologia.seres-vivos-classificacao
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Biologia: Seres vivos e classificação** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Taxonomia, evolução e nomenclatura; domínios e reinos didáticos; vírus e bactérias; filos animais e ciclos de parasitos; cordados e plantas terrestres.

Fica de fora (outras matérias tratam): Citologia, genética e ecologia em profundidade; sistemas humanos, terapias e classificação molecular especializada.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Classificação dos seres vivos: categorias taxonômicas.
- Evolução, Sistemas de Classificação e Organização Taxonômica dos seres vivos.
- Sistemas de classificação.
- As categorias taxonômicas.
- Classificação dos grandes grupos de organismos.
- Ciclos biológicos das principais espécies de invertebrados causadores de doenças.
- Diversidade da vida: principais características dos representantes de cada domínio e de cada reino da natureza.
- Regras de nomenclatura.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Evolução (Darwin e Lamarck, seleção natural, especiação, filogenia e cladística) ligada à classificação, com questões.
- Ciclos biológicos de invertebrados causadores de doenças (esquistossomose, teníase, ascaridíase, malária, doença de Chagas, filariose etc.), com questões.
- Regras de nomenclatura biológica (binomial, gênero e espécie, grafia, código de nomenclatura), com questões.

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
  "id": "biologia.seres-vivos-classificacao",
  "subject": "Biologia",
  "title": "Seres vivos e classificação",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Seres vivos e classificação"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome biologia.seres-vivos-classificacao.json.

==================================================
# MATÉRIA 723 — fisica.magnetismo-inducao
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **fisica: Magnetismo e indução eletromagnética** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Ímãs e campo magnético, força magnética sobre cargas e fios, movimento de cargas, campo gerado por correntes, fluxo magnético, lei de Faraday-Lenz, indutância e transformadores ideais.

Fica de fora (outras matérias tratam): Eletrostática, circuitos resistivos detalhados, ondas eletromagnéticas e eletrônica.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Campo magnético.
- Eletromagnetismo.
- Lei de Ampère.
- Lei de Faraday.
- Ímãs.
- Eletromagnetismo: propriedades magnéticas, paramagnéticas e diamagnéticas.
- Magnetismo e indução eletromagnética.
- Eletromagnetismo: Forças devido aos campos magnéticos e momentos magnéticos.
- Campo magnético e leis de Ampère e Faraday.
- Eletromagnetismo: Campo magnético e forças sobre cargas em movimento.
- Eletromagnetismo: Lei de Ampère e aplicação a fios e espiras.
- Eletromagnetismo: Lei de Faraday e indução eletromagnética.
- Eletromagnetismo: Propriedades elétricas e magnéticas dos materiais.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- propriedades magnéticas dos materiais (paramagnetismo, diamagnetismo, ferromagnetismo, histerese), com 8 questões
- propriedades elétricas e magnéticas dos materiais (dielétricos, condutores, para/dia/ferromagnetismo), com 8 questões

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
  "id": "fisica.magnetismo-inducao",
  "subject": "fisica",
  "title": "Magnetismo e indução eletromagnética",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Magnetismo e indução eletromagnética"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome fisica.magnetismo-inducao.json.

==================================================
# MATÉRIA 724 — matematica.trigonometria
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática: Trigonometria** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Ângulos e medidas; razões trigonométricas; ciclo unitário; identidades fundamentais; funções seno, cosseno e tangente; equações e gráficos; relações trigonométricas em triângulos.

Fica de fora (outras matérias tratam): Séries de Fourier, análise de sinais e aplicações avançadas de cálculo.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Trigonometria.
- Funções Trigonométricas.
- Trigonometria no triângulo.
- Ciclo trigonométrico.
- Equações trigonométricas.
- Inequações trigonométricas.
- Relações trigonométricas.
- Redução de arcos do 1º quadrante.
- Operações com arcos.
- Eixo Trigonometria.
- Trigonometria no triângulo retângulo: seno, cosseno e tangente.
- Trigonometria no triângulo qualquer: medidas de distâncias inacessíveis.
- Trigonometria na circunferência: o seno, cosseno e tangente.
- Função seno e função cosseno e suas aplicações.
- Geometria: trigonometria em triângulos retângulos.
- Geometria: lei dos senos e dos cossenos.
- Trigonometria do triângulo retângulo e estudo do seno, cosseno e tangente.
- Trigonometria do triângulo retângulo.
- Estudo do seno, do cosseno e da tangente.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- inequações trigonométricas (resolução no ciclo, intervalos) com 8 questões
- operações com arcos: seno, cosseno e tangente da soma e diferença, arco duplo e metade, transformação em produto, com 12 questões

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
  "id": "matematica.trigonometria",
  "subject": "Matemática",
  "title": "Trigonometria",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Trigonometria"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica.trigonometria.json.

==================================================
# MATÉRIA 725 — ciencias.solucoes
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Ciências Naturais: Soluções** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Soluções líquidas; solubilidade, coeficiente e curvas; estados de saturação; efeitos de temperatura e pressão; unidades de concentração; propriedades coligativas em soluções diluídas.

Fica de fora (outras matérias tratam): Equilíbrio de solubilidade com Kps, titulações, propriedades de soluções não ideais em detalhe e cálculos de atividade química.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Soluções.
- Concentração das soluções.
- Diluição de soluções.
- Solubilidade.
- Soluções, concentração das soluções (grama/litro e mol/litro).
- Noções de química: soluções.
- Relações de proporcionalidade entre solutos e solvente: concentração em quantidade de matéria, concentração em massa, fração em quantidade de matéria, fração em massa, fração em volume, densidade.
- Soluções: Coeficiente de solubilidade; unidades de concentração; Propriedades coligativas;
- Misturas, soluções e propriedades coligativas.
- Relações de proporcionalidade entre solutos e solvente: concentração em quantidade de matéria, concentração em massa, fração em quantidade de matéria, fração em massa, fração em volume.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Acrescentar fração em quantidade de matéria (fração molar), fração em massa (título), fração em volume e densidade da solução, com conversões e questões; hoje só há g/L, mol/L, molalidade e % m/m.
- Acrescentar fração em quantidade de matéria (fração molar), fração em massa (título) e fração em volume, com conversões e questões.

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
  "id": "ciencias.solucoes",
  "subject": "Ciências Naturais",
  "title": "Soluções",
  "version": 5,
  "status": "PUBLISHED",
  "aliases": ["Soluções"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome ciencias.solucoes.json.

==================================================
# MATÉRIA 726 — matematica.funcao-exponencial-logaritmica
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Matemática: Funções exponencial e logarítmica** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Funções exponenciais e logarítmicas; propriedades e gráficos; equações simples; aplicações de crescimento e decaimento; relação entre exponencial e logaritmo.

Fica de fora (outras matérias tratam): Equações avançadas, logaritmos complexos, cálculo diferencial e modelos com ajuste estatístico.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Funções exponenciais e logarítmicas.
- Funções exponenciais.
- Funções logarítmicas.
- Funções exponenciais: conceito, representação gráfica, problemas.
- Equações e inequações exponenciais: conceito, representação gráfica, problemas.
- Funções logarítmicas: conceito, representação gráfica, problemas.
- Equações e inequações logarítmicas: conceito, representação gráfica, problemas.
- Função exponencial e função logarítmica.
- Função exponencial.
- Função logarítmica.
- Funções exponencial e logarítmica.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Inequações exponenciais (sentido da desigualdade conforme a base) com resolução e questões; a matéria só traz equações simples.
- Inequações logarítmicas (condição de existência e sentido da desigualdade) com resolução e questões.

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
  "id": "matematica.funcao-exponencial-logaritmica",
  "subject": "Matemática",
  "title": "Funções exponencial e logarítmica",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Funções exponencial e logarítmica"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome matematica.funcao-exponencial-logaritmica.json.

==================================================
# MATÉRIA 727 — portugues.figuras-de-linguagem
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Língua Portuguesa: Figuras de linguagem** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Figuras de palavra, pensamento, construção e som; efeitos de sentido em metáforas, metonímias, ironias, eufemismos, hipérboles, antíteses, personificações, repetições e recursos afins.

Fica de fora (outras matérias tratam): Análise sintática completa, gêneros textuais e figuras de linguagem em outras línguas.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Figuras de linguagem.
- A linguagem lógica e a figurada.
- Estilística: figuras de linguagem.
- Figuras de linguagem e relações de sentido na construção do texto.
- Linguagem lógica e figurada.
- Figuras de linguagem e suas relações de sentido na construção do texto.
- Figuras de linguagem e linguagem denotativa e conotativa.
- Textos literários e não literários, denotação, conotação, figuras de linguagem e estrutura textual.
- Denotação, conotação, figuras de linguagem e estrutura textual.
- Textos da esfera jornalística/publicitária — Análise linguística: Identifique o uso expressivo da adjetivação e dos processos metafóricos como estratégias direcionadas à persuasão e ao apelo publicitário.
- Textos da esfera literária — Análise linguística: Analise a construção estética do texto mediante figuras de linguagem e conotação (metáforas, personificações, metonímias etc.).
- Textos da esfera literária — Análise linguística: Perceba os efeitos de sentido provocados por escolhas intencionais de rimas, métricas, aliterações, assonâncias e disposições gráfico-espaciais no poema (estruturado e/ou livre).

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Adjetivação e metáfora como estratégia persuasiva em publicidade, com textos de anúncios e questões.
- Rima, métrica, aliteração, assonância e disposição gráfica em poemas (versos livres e estruturados), com questões.

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
  "id": "portugues.figuras-de-linguagem",
  "subject": "Língua Portuguesa",
  "title": "Figuras de linguagem",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Figuras de linguagem"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome portugues.figuras-de-linguagem.json.

==================================================
# MATÉRIA 728 — geografia.brasil-territorio-estado
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Geografia: Brasil: território, Estado e divisão regional** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Conceitos de Estado, nação, país e território; soberania e fronteiras; organização político-administrativa brasileira; escalas e critérios de regionalização; divisão regional do IBGE.

Fica de fora (outras matérias tratam): População e urbanização detalhadas, geopolítica mundial, disputas de fronteira específicas e divisão regional de outras instituições.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Disparidades regionais e planejamento no Brasil.
- Formação territorial do Brasil.
- O Brasil político: nação e território e organização do Estado Brasileiro.
- Nação e território.
- Geografia do Brasil: território, fronteiras, divisão regional e população.
- Geografia econômica: Disparidades regionais e planejamento no Brasil.
- Temas clássicos da Geografia Política: as fronteiras e as formas de apropriação política do espaço.
- Relações Estado e território.
- Geografia política: teorias geopolíticas, fronteiras, apropriação do espaço, Estado, território e formação territorial do Brasil.
- Geografia política: teorias geopolíticas, poder mundial, fronteiras, apropriação política do espaço, Estado e território e formação territorial do Brasil.
- Geografia política: teorias geopolíticas, poder mundial, fronteiras, apropriação política do espaço, Estado e território.
- Temas clássicos da Geografia Política: fronteiras e organização do território.
- Geografia política do Brasil e inserção territorial internacional.
- O Brasil político: nação, território e organização do Estado.
- Regiões brasileiras, marcas do Brasil em todos os cantos.
- Critérios de delimitação de regiões.
- Territorialização: fronteiras em movimento.
- O território brasileiro.
- A formação territorial do Brasil: ocupação, organização.
- Regionalizações.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Desigualdades regionais e planejamento regional no Brasil (PNDR, SUDENE, SUDAM, polos de desenvolvimento, concentração industrial), com questões.
- Formação territorial do Brasil: ocupação, expansão, tratados de limites e organização do território ao longo da história, com questões.

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
  "id": "geografia.brasil-territorio-estado",
  "subject": "Geografia",
  "title": "Brasil: território, Estado e divisão regional",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Brasil: território, Estado e divisão regional"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome geografia.brasil-territorio-estado.json.

==================================================
# MATÉRIA 729 — literatura.barroco-arcadismo
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Literatura: Barroco e Arcadismo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Contextos, características e procedimentos do Barroco e do Arcadismo, incluindo manifestações luso-brasileiras e autores representativos.

Fica de fora (outras matérias tratam): Romantismo, escolas literárias posteriores e estudo biográfico aprofundado.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Barroco e Arcadismo.
- Barroco mineiro.
- Arcadismo brasileiro.
- O Barroco no Brasil.
- O Arcadismo no Brasil.
- Barroco no Brasil, contexto social e histórico e produção de Gregório de Matos Guerra.
- Arcadismo no Brasil e sua relação com a Inconfidência Mineira e a transição para o Romantismo.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Barroco no Brasil: Gregório de Matos, Padre Vieira e Botelho de Oliveira, com leitura de trechos e questões.
- Arcadismo no Brasil: Cláudio Manuel da Costa, Tomás Antônio Gonzaga, Basílio da Gama e Santa Rita Durão, com leitura de trechos e questões.

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
  "id": "literatura.barroco-arcadismo",
  "subject": "Literatura",
  "title": "Barroco e Arcadismo",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Barroco e Arcadismo"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome literatura.barroco-arcadismo.json.

==================================================
# MATÉRIA 730 — literatura.modernismo-contemporanea
==================================================
Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Modernismo brasileiro e produção literária contemporânea: Modernismo e literatura contemporânea** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Antecedentes, Semana de Arte Moderna, fases modernistas, poesia, prosa, vanguardas, gêneros e literatura contemporânea.

Fica de fora (outras matérias tratam): Biografias exaustivas e catálogo completo de obras e autores.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Pós-modernismo.
- Modernismo brasileiro (1ª, 2ª e 3ª gerações) e tendências contemporâneas.
- O Modernismo.
- Literatura Moderna no Brasil em seus três momentos.
- Modernismo no Brasil – poesia e prosa.
- Estilo dos textos literários contemporâneos.
- Modernismo brasileiro: fases, autores, poesia, prosa, tradição e diversidade artística.
- Pós-modernidade no Brasil sob perspectiva crítico-literária.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Terceira geração modernista e tendências contemporâneas (Clarice Lispector, Guimarães Rosa, João Cabral, Geração de 45) com questões; a matéria cobre antecedentes e as duas primeiras fases.
- Pós-modernismo e produção posterior a 1945 (Geração de 45, concretismo, poesia marginal), com questões.

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
  "id": "literatura.modernismo-contemporanea",
  "subject": "Modernismo brasileiro e produção literária contemporânea",
  "title": "Modernismo e literatura contemporânea",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Modernismo e literatura contemporânea"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome literatura.modernismo-contemporanea.json.

