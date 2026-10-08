Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Administrativo: Ato administrativo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use o texto oficial vigente (planalto.gov.br) para leis e súmulas.

## Escopo
Conceito (ato da Administração × ato administrativo × fato administrativo; silêncio administrativo); elementos/requisitos (competência, finalidade, forma, motivo, objeto) e vícios de cada um; motivação e teoria dos motivos determinantes; atributos (presunção de legitimidade e veracidade, imperatividade, autoexecutoriedade, tipicidade) e em quais atos faltam; discricionariedade × vinculação, mérito e limites do controle judicial; classificações (geral/individual, interno/externo, simples/complexo/composto, de império/gestão/expediente, constitutivo/declaratório, etc.); espécies (normativos, ordinatórios, negociais — licença × autorização × permissão —, enunciativos, punitivos); extinção (anulação, revogação, cassação, caducidade, contraposição, renúncia), convalidação e seus limites, efeitos ex tunc × ex nunc, prazo decadencial de anulação; Lei 9.784/1999 nos pontos que tocam o ato (motivação obrigatória, anulação, revogação, convalidação, delegação e avocação); Súmulas 346 e 473 do STF e entendimentos consolidados que você confirmar.

## Regras de qualidade (as mais importantes)
1. **Números e dispositivos SÃO o conteúdo.** Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial (ex.: "a Administração decai do direito de anular em 5 anos, salvo má-fé — Lei 9.784/1999, art. 54"). Nunca escreva "no prazo legal" ou "conforme a lei" quando o número existe. Se não tiver certeza de algum número, explique sem ele — nunca invente.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa com dispositivo → exemplo concreto do tipo que cai → exceção que a banca usa → como reconhecer na prova. Tabelas para comparar institutos (licença × autorização × permissão; anulação × revogação; ato complexo × composto). O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. A certa NÃO pode ser a mais longa em mais de 10 das 25. Enunciados com caso concreto, como a banca faz.
4. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto vizinho trocado (licença por autorização), prazo ou súmula trocados, efeito ex tunc por ex nunc, regra certa no caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria (absurdo, de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos só para marcar o errado).
5. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
6. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a banca escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; 5 a 8 "conceitos que geram erro"; fontes reais que você abriu.
7. Texto neutro: sem citar banca, órgão ou cargo específico no enunciado.

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "direito-administrativo.ato-administrativo",
  "subject": "Direito Administrativo",
  "title": "Ato administrativo",
  "version": 3,
  "status": "PUBLISHED",
  "aliases": ["Ato administrativo", "Atos administrativos"],
  "scope": { "covers": "o que a matéria cobre", "excludes": "o que fica de fora" },
  "theoryTitle": "Ato administrativo: elementos, atributos, espécies e extinção",
  "chapters": [ { "title": "1. Conceito e elementos", "markdown": "texto em Markdown (## e ###, listas, **negrito**, tabelas | a | b |, alertas com >)" } ],
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
  "sources": [ { "kind": "OFICIAL", "title": "Lei nº 9.784/1999", "publisher": "Presidência da República", "reference": "arts. 50 a 55", "url": "https://www.planalto.gov.br/ccivil_03/leis/l9784.htm", "accessedAt": "AAAA-MM-DD" } ]
}
```
- difficulty: "FACIL", "MEDIA" ou "DIFICIL". format: "MULTIPLE_CHOICE" ou "TRUE_FALSE". kind das fontes: "OFICIAL" ou "COMPLEMENTAR".
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir.
