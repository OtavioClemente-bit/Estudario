Você é professor de cursinho preparatório para concursos públicos. Escreva, por inteiro, UMA matéria de estudo sobre:

TEMA: {TEMA}
ITEM DO EDITAL: {ITEM}
ID: {ID}

Antes de escrever, pesquise e abra o texto OFICIAL da norma (site do governo, assembleia, tribunal ou diário oficial). Ensine fatos concretos que estão no texto: órgãos, composição, competências, requisitos, deveres, vedações, prazos, procedimentos e sanções. Não escreva generalidades do tipo "é importante consultar a norma" ou "o servidor deve agir com ética". Se não tiver certeza de um número de artigo, prazo ou valor, explique o conteúdo sem o número. Nunca invente norma.

Entregue como ARQUIVO para download chamado {ID}.json (JSON válido, UTF-8), com exatamente esta estrutura:

{
  "id": "{ID}",
  "subject": "Legislação",
  "title": "título curto e claro",
  "version": 1,
  "status": "PUBLISHED",
  "aliases": ["8 a 15 nomes alternativos, cada um com 10+ caracteres, incluindo o texto do item do edital"],
  "scope": { "covers": "o que a matéria cobre", "excludes": "o que fica de fora" },
  "theoryTitle": "título da teoria",
  "chapters": [ { "title": "1. Título", "markdown": "texto em Markdown" } ],
  "summary": "resumo de um parágrafo",
  "flashcards": [ { "front": "pergunta", "back": "resposta" } ],
  "tips": ["6 dicas"],
  "traps": ["6 pegadinhas de prova"],
  "activeRecall": [ { "question": "pergunta aberta", "answer": "resposta" } ],
  "errorConcepts": [ { "key": "e1", "title": "erro comum", "summary": "explicação" } ],
  "questions": [
    { "statement": "enunciado", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ {"key":"A","text":"...","correct":false}, {"key":"B","text":"...","correct":true}, {"key":"C","text":"...","correct":false}, {"key":"D","text":"...","correct":false}, {"key":"E","text":"...","correct":false} ],
      "explanation": "por que a certa está certa e as outras erradas (120+ caracteres, sem citar a letra)",
      "section": "1. Título (igual ao título de um capítulo)", "errorConceptKey": "e1",
      "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null },
    { "statement": "afirmação para julgar", "format": "TRUE_FALSE", "difficulty": "MEDIA",
      "options": [ {"key":"C","text":"Certo","correct":true}, {"key":"E","text":"Errado","correct":false} ],
      "explanation": "Certo. ...", "section": "1. Título", "errorConceptKey": null,
      "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null }
  ],
  "sources": [ { "kind": "OFICIAL", "title": "nome da norma", "publisher": "órgão", "reference": "do que trata", "url": "link oficial", "accessedAt": "2026-10-28" } ]
}

Regras:
- 4 a 6 capítulos didáticos. Cada capítulo começa com "### O problema que este assunto resolve" (ou equivalente), tem um quadro "> **Siglas e termos deste capítulo**" com lista, tabela quando houver comparação e termina com "### Como cai na prova".
- 20+ flashcards, 6 tips, 6 traps, 8 activeRecall, 4 a 5 errorConcepts.
- 45 questões: 18 Certo/Errado (metade Certo, metade Errado) e 27 de múltipla escolha com 5 alternativas. Pelo menos 10 FACIL e 10 DIFICIL.
- Só UMA alternativa defensável por questão; confira que "correct": true está na alternativa certa.
- Distribua o gabarito entre A, B, C, D e E. A certa NÃO pode ser a mais longa em mais de 30% das questões: as cinco alternativas com tamanho parecido.
- Erradas plausíveis (nada absurdo); nenhuma alternativa repetida.
- Explicação da própria questão, com 120+ caracteres, sem citar a letra.
- "section" deve ser exatamente o título de um capítulo.

Escreva tudo de uma vez e entregue só o arquivo.
