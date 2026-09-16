# Caminho das pedras: gerar conteúdo para o Meu Concurso V2 com IA

Os prompts abaixo podem ser usados no GPT, Claude, Gemini ou qualquer outra IA. Envie o prompt junto com o edital ou texto-base solicitado e peça somente o JSON puro, sem bloco de código ou explicações. Depois salve a resposta com extensão `.estudo` e importe no aplicativo.

## Fluxo recomendado

1. Envie o edital à IA que preferir e use o Prompt A. Salve a resposta pura como `edital-[orgao]-[cargo].estudo` e importe em **Mais › Importar pacote .estudo**.
2. Confira no app os nomes de concurso, matéria e tópico.
3. Gere um tópico por vez com o Prompt B. Isso evita JSON cortado e melhora a profundidade do livro.
4. Salve a resposta em UTF-8 com extensão `.estudo`, importe e confira a prévia.
5. Ao corrigir ou ampliar um pacote, mantenha o mesmo `packageId` e escolha **Atualizar**. Progresso de leitura, marcações, favoritos e histórico de questões serão mantidos. **Criar cópia** guarda as duas versões.

## Prompt A — transformar o edital em tópicos

```text
Vou anexar ou colar o conteúdo programático de um edital. Gere um arquivo .estudo para o aplicativo Meu Concurso.

OBJETIVO: reproduzir integralmente matérias, tópicos, subtópicos e ordem. Não omita, resuma, una ou invente itens.

REGRAS:
- Responda somente com JSON válido, sem ``` e sem texto fora do JSON.
- Use version 2 e packageId estável: edital-ORGAO-CARGO-ANO.
- IDs minúsculos, únicos, sem acentos, separados por hífen.
- Nesta etapa deixe teorias, resumos e questoes vazios.
- Represente subdivisões recursivamente em subtopicos.

RAIZ:
{
  "version": 2,
  "packageId": "edital-ORGAO-CARGO-ANO",
  "concurso": { "nome": "NOME DO CONCURSO — CARGO", "principal": true },
  "padroesQuestao": { "banca": "BANCA", "orgao": "ORGAO", "ano": 2026 },
  "materias": [
    {
      "id": "materia",
      "nome": "Matéria",
      "ordem": 0,
      "topicos": [
        {
          "id": "materia-topico",
          "titulo": "Título exato",
          "descricao": "Escopo do edital",
          "ordem": 0,
          "prioridade": "NORMAL",
          "observacoes": "",
          "teorias": [],
          "resumos": [],
          "questoes": [],
          "subtopicos": []
        }
      ]
    }
  ]
}

Confira internamente o JSON, todos os itens, a ordem, os IDs únicos e vírgulas. Se o PDF estiver ilegível, informe a página em vez de inventar.

EDITAL:
[COLE OU ANEXE AQUI]
```

## Prompt B — pacote completo de um tópico

```text
Gere um arquivo .estudo version 2 para o aplicativo Meu Concurso.

DADOS:
- Concurso exato no app: [CONCURSO]
- Matéria exata no app: [MATÉRIA]
- Tópico exato no app: [TÓPICO]
- Banca/órgão/ano: [PREENCHA]
- Quantidade de questões: [10]
- Fontes ou texto-base: [COLE OU ANEXE]
- Data-base para conteúdo mutável: [DATA]

CONTEÚDO:
- teorias: um livro didático, longo, autossuficiente e completo. Divida em capítulos substanciais com fundamentos, desenvolvimento, exemplos, aplicações, comparações, pegadinhas e síntese. Explique termos na primeira ocorrência. Não invente leis, números, jurisprudência ou versões.
- summary: resumo completo e estruturado de toda a teoria; não é apenas uma lista curta.
- quickReview: revisão de poucos minutos, diferente do summary.
- tips: bizus objetivos e úteis.
- traps: confusões e armadilhas típicas de prova.
- activeRecall: perguntas curtas para o aluno responder mentalmente sem consultar.
- questions: questões autorais no estilo da banca, cinco alternativas A–E, uma correta, distratores plausíveis e explicação detalhada.
- errorConcepts: conceitos que costumam gerar erro, cada um com title e summary corretivo.

RESPONDA SOMENTE COM ESTE JSON PREENCHIDO, SEM BLOCO DE CÓDIGO:
{
  "version": 2,
  "packageId": "conteudo-materia-topico",
  "competition": "[CONCURSO EXATO]",
  "subject": "[MATÉRIA EXATA]",
  "topic": "[TÓPICO EXATO]",
  "teorias": [
    {
      "id": "livro-topico",
      "titulo": "Livro — Tópico",
      "capitulos": [
        { "id": "cap-01", "titulo": "1. Fundamentos", "markdown": "Texto longo em Markdown..." },
        { "id": "cap-02", "titulo": "2. Desenvolvimento", "markdown": "Texto longo em Markdown..." }
      ]
    }
  ],
  "summary": "# Resumo completo\n\nConteúdo estruturado...",
  "quickReview": "# Revisão rápida\n\nConteúdo para poucos minutos...",
  "tips": ["Bizu 1", "Bizu 2"],
  "traps": ["Pegadinha 1", "Pegadinha 2"],
  "activeRecall": ["Pergunta de recuperação ativa 1?", "Pergunta 2?"],
  "questions": [
    {
      "id": "q-001",
      "board": "BANCA",
      "year": 2026,
      "statement": "Enunciado",
      "options": [
        { "key": "A", "text": "Alternativa", "correct": false },
        { "key": "B", "text": "Alternativa correta", "correct": true },
        { "key": "C", "text": "Alternativa", "correct": false },
        { "key": "D", "text": "Alternativa", "correct": false },
        { "key": "E", "text": "Alternativa", "correct": false }
      ],
      "explanation": "Justificativa detalhada.",
      "difficulty": "MEDIA",
      "tags": ["tema", "subtema"]
    }
  ],
  "errorConcepts": [
    { "id": "erro-01", "title": "Conceito confundido", "summary": "Correção clara." }
  ],
  "tags": ["matéria", "tópico"]
}

Antes de responder, valide internamente: JSON puro e válido; livro realmente completo; summary diferente de quickReview; todos os arrays presentes; quantidade solicitada de questões; exatamente uma correta em cada questão; IDs únicos; nenhuma vírgula sobrando.
```

## Prompt C — validar um arquivo pronto

```text
Valide este arquivo .estudo version 2 sem reduzir o conteúdo. Confira JSON, campos obrigatórios, IDs únicos, livro, summary, quickReview, tips, traps, activeRecall, quantidade de questions, exatamente uma alternativa correta por questão e errorConcepts. Responda apenas “VÁLIDO” se estiver correto. Caso contrário, liste os erros por caminho JSON e forneça o JSON completo corrigido, sem bloco de código.
```

Use questões autorais “no estilo da banca”, não cópias literais de bancos protegidos. Para livros muito grandes, prefira um tópico por arquivo e peça capítulos em rodadas menores se a IA sinalizar limite de resposta.
