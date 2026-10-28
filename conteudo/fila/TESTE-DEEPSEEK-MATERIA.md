# Teste: DeepSeek escreve uma matéria inteira (Lei 10.259/2001, Juizados Especiais Federais)

Por que esta: cai em 78 editais (TRFs, JF, INSS, AGU) e a biblioteca não tem matéria própria dela, só
pedaços dentro de outras. É lei curta (cerca de 27 artigos), então é um teste justo.

Como pedir:
- Cole o bloco PARTE 1 no chat da DeepSeek (modo com raciocínio ligado, "DeepThink", se tiver). Salve a
  resposta como `conteudo/entrada/teste-deepseek/parte-1.json`.
- Depois cole o bloco PARTE 2 na mesma conversa. Salve como `conteudo/entrada/teste-deepseek/parte-2.json`.
- Se ele cortar no meio, diga "continue exatamente de onde parou" e cole a continuação no mesmo arquivo.
- Eu junto, rodo o conferidor de matéria, leio tudo e comparo com as matérias feitas pelo Codex/Claude.

## PARTE 1

```
Você vai escrever, em português do Brasil, uma matéria completa para um app de estudo para concursos, no estilo de
um cursinho didático e caro: linguagem simples, analogias do dia a dia, exemplos concretos, sem enrolação.

ASSUNTO: Lei nº 10.259/2001 — Juizados Especiais Cíveis e Criminais no âmbito da Justiça Federal (JEF), com a
aplicação subsidiária da Lei 9.099/1995 e a base constitucional (art. 98 da CF). Inclua: competência cível (valor de
60 salários mínimos, causas excluídas, competência absoluta onde houver vara do JEF), competência criminal, quem pode
ser parte (autor e réu), dispensa de advogado na 1ª instância, prazos (sem prazo diferenciado para a Fazenda),
citações e intimações, perícias e exames técnicos, medidas cautelares, recursos (só da sentença definitiva e de
decisões sobre cautelares; Turmas Recursais), uniformização de jurisprudência (Turma Regional, Turma Nacional,
pedido ao STJ), requisição de pequeno valor (RPV, prazo de 60 dias, sequestro, renúncia ao excedente), conciliação
pelos representantes da União/autarquias, e o que mais a lei trouxer.

REGRA DE OURO: se não tiver certeza de um número de artigo, prazo, valor ou súmula, NÃO escreva o número;
explique o conteúdo. Nunca invente norma ou súmula.

Responda SÓ com um objeto JSON (sem texto fora dele), com exatamente estas chaves:
{
  "id": "legislacao.lei-10259-2001-juizados-federais",
  "subject": "Legislação",
  "title": "Lei 10.259/2001: Juizados Especiais Federais",
  "version": 1,
  "status": "PUBLISHED",
  "aliases": [8 a 15 formas como o assunto aparece em editais, ex.: "Lei nº 10.259/2001", "Juizados Especiais Federais", ...],
  "scope": { "covers": "uma frase com tudo o que a matéria cobre", "excludes": "uma frase com o que fica de fora" },
  "theoryTitle": "título curto da teoria",
  "chapters": [ 5 a 7 capítulos { "title": "1. Título do capítulo", "markdown": "texto do capítulo" } ],
  "summary": "resumo em markdown, 1.500 a 2.500 caracteres, com os números que mais caem",
  "flashcards": [ 20 a 25 { "front": "pergunta curta", "back": "resposta curta" } ],
  "tips": [ 6 a 8 dicas de prova, uma frase cada ],
  "traps": [ 6 a 8 pegadinhas, no formato "A prova diz que X. Errado/Certo: ..." ],
  "activeRecall": [ 8 { "question": "pergunta aberta", "answer": "resposta" } ],
  "errorConcepts": [ 4 a 6 { "key": "e1", "title": "confusão comum", "summary": "explicação" } ],
  "questions": [],
  "sources": [ 2 a 4 { "kind": "OFICIAL", "title": "...", "publisher": "...", "reference": "...", "url": "https://www.planalto.gov.br/...", "accessedAt": "2026-10-28" } ]
}

COMO ESCREVER CADA CAPÍTULO (markdown):
- Títulos internos com "### ". Comece com "### O problema que este assunto resolve" (2 a 4 frases com uma situação real).
- Logo depois, um quadro de siglas e termos exatamente assim (citação com lista, um termo por linha):
  > **Siglas e termos deste capítulo**
  > - **JEF**: o que é, numa frase simples.
  > - **RPV**: ...
- Use pelo menos uma tabela comparativa no capítulo quando houver comparação (ex.: JEF x Juizado Estadual; RPV x
  precatório; Turma Regional x Turma Nacional x STJ).
- Termine o capítulo com "### Como cai na prova" (3 a 5 itens com o jeito que as bancas cobram).
- Cada capítulo com 2.500 a 5.000 caracteres. Negrito só no que é número, prazo ou palavra-chave de prova.
- Os títulos dos capítulos começam com o número ("1. ", "2. "...) e não se repetem.
```

## PARTE 2

```
Agora escreva 20 questões INÉDITAS sobre a mesma matéria, no estilo das bancas (CEBRASPE, FCC, FGV), e responda SÓ
com um array JSON de 20 objetos (sem texto fora dele). Use os títulos EXATOS dos capítulos que você escreveu.

Formato de cada questão:
{
  "statement": "enunciado; pode trazer um caso curto",
  "format": "MULTIPLE_CHOICE" ou "TRUE_FALSE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "por que está errada" }, ... ],
  "explanation": "150 a 400 caracteres: a regra, aplicada ao caso, SEM citar letra de alternativa",
  "section": "título exato de um capítulo",
  "errorConceptKey": "e1" ou null,
  "sourceType": "AUTHORIAL", "sourceUrl": null, "board": null, "agency": null, "year": null
}

REGRAS:
1. 14 questões MULTIPLE_CHOICE com 5 alternativas (A–E) e 1 correta; 6 questões TRUE_FALSE com 2 opções:
   { "key": "C", "text": "Certo", ... } e { "key": "E", "text": "Errado", ... }. Das 6 de Certo/Errado, 3 Certo e 3 Errado.
2. Dificuldade: 6 FACIL, 8 MEDIA, 6 DIFICIL. Espalhe pelos capítulos.
3. A alternativa correta NÃO pode ser a mais longa nem a mais curta em mais de 4 das 14 questões. Alternativas
   com tamanho parecido.
4. A letra correta varia (cerca de 3 por letra).
5. Distratores plausíveis: erros que um candidato médio cometeria (trocar 60 por 40 salários, achar que a Fazenda
   tem prazo em dobro, que cabe recurso de qualquer decisão interlocutória, que pessoa jurídica de grande porte
   pode ser autora, que RPV se paga em 90 dias, confundir Turma Regional com Turma Nacional). Proibido alternativa
   absurda ou eliminável sem saber o conteúdo.
6. O enunciado NÃO pode entregar a resposta (não escreva a regra e depois pergunte a regra).
7. Exatamente uma correta; confira cada gabarito duas vezes. Se não tiver certeza de um número, não faça a questão
   depender dele.
8. Não repita a mesma ideia em duas questões.
```

## PARTE 3 (a biblioteca exige pelo menos 40 questões; cole na mesma conversa)

```
Preciso de mais 24 questões INÉDITAS da mesma matéria, no mesmo formato JSON, SÓ o array (sem texto fora dele),
sem repetir nenhuma ideia das 20 anteriores. Use os títulos exatos dos capítulos.

DISTRIBUIÇÃO:
- 12 TRUE_FALSE (6 Certo e 6 Errado) e 12 MULTIPLE_CHOICE com 5 alternativas.
- Dificuldade: 6 FACIL, 10 MEDIA, 8 DIFICIL.
- Pelo menos 3 questões por capítulo.

CORREÇÕES (o que estava errado antes; não repita):
- Conselhos de fiscalização profissional SÃO autarquias federais e PODEM ser réus no JEF.
- Não cite precedente do STF sobre escolha de foro; não existe "prazo de 30 dias para a Fazenda contestar" (os 30 dias
  são a antecedência mínima da citação para a audiência).
- Não diga que cabe recurso contra o indeferimento de cautelar; a lei fala da decisão que defere.

REGRA MAIS IMPORTANTE (nas 20 anteriores falhou: a certa era a mais longa em 11 de 14):
- Nas de múltipla escolha, a alternativa correta deve ser a MAIS LONGA em no máximo 3 das 12. Escreva as erradas com
  o mesmo tamanho e o mesmo nível de detalhe da certa. Prefira alternativas curtas e parelhas.
- Nada de alternativa absurda. Enunciado não pode entregar a resposta.
- Nas de Certo/Errado, o item Errado deve trocar UM detalhe plausível (um número, um órgão, um verbo), não ser
  obviamente falso.
```
