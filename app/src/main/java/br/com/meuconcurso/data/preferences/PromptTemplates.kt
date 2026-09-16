package br.com.meuconcurso.data.preferences

object PromptTemplates {
    const val EDITAL = """Vou enviar o conteúdo programático de um edital. Gere um arquivo .estudo compatível com o aplicativo Meu Concurso.

OBJETIVO: montar a estrutura completa, preservando todas as matérias, tópicos, subtópicos e a ordem oficial. Não omita, resuma, una nem invente itens.

REGRAS OBRIGATÓRIAS:
- Responda somente com JSON válido, sem bloco Markdown e sem texto adicional.
- Use version 2 e packageId único.
- IDs minúsculos, únicos, sem acentos e separados por hífen.
- Nesta etapa deixe teorias, resumos e questoes como listas vazias.
- Represente subdivisões recursivamente em subtopicos.
- Mantenha os mesmos IDs em todos os pacotes futuros. O ID é a identidade permanente do item no Plano Mestre.
- Use contentOriginType: EDITAL para texto literal do edital, DIDACTIC_SUBDIVISION para divisão didática e AUXILIARY_CONTENT para fundamento complementar.

ESTRUTURA DE CADA TÓPICO:
{
  "id": "materia-topico",
  "titulo": "Título conforme o edital",
  "descricao": "Escopo curto",
  "ordem": 0,
  "prioridade": "NORMAL",
  "contentOriginType": "EDITAL",
  "observacoes": "",
  "teorias": [],
  "resumos": [],
  "questoes": [],
  "subtopicos": []
}

RAIZ DO ARQUIVO:
{
  "version": 2,
  "packageId": "edital-ORGAO-CARGO-ANO-v1",
  "concurso": { "id": "concurso-orgao-cargo-ano", "nome": "NOME DO CONCURSO — CARGO", "principal": true },
  "padroesQuestao": { "banca": "BANCA", "orgao": "ORGAO", "ano": 2026, "origem": "Material de estudo gerado" },
  "materias": [{ "id": "materia", "nome": "Matéria", "ordem": 0, "topicos": [] }]
}

Antes de responder, confira internamente: JSON válido, todos os itens do edital presentes, IDs sem repetição e nenhuma vírgula sobrando.

TEXTO DO EDITAL:
[COLE O EDITAL AQUI]"""

    const val CONTEUDO = """Crie um arquivo .estudo version 2 para o aplicativo Meu Concurso com LIVRO COMPLETO, RESUMO COMPLETO, REVISÃO RÁPIDA, MEMORIZAÇÃO ATIVA e QUESTÕES para o tópico indicado.

DADOS:
- Concurso: [NOME EXATO NO APP]
- Matéria: [NOME EXATO NO APP]
- Caminho do tópico: [TÓPICO PAI > SUBTÓPICO]
- Banca/órgão/ano: [PREENCHA]
- Quantidade de questões: [10]
- Fontes ou texto-base: [COLE OU ANEXE AQUI]

TEORIA COMPLETA — TRATE COMO UM LIVRO:
- Escreva material longo, didático e autossuficiente, não um resumo ampliado.
- Divida em capítulos e seções numa sequência pedagógica: fundamentos, desenvolvimento, exemplos, aplicações, pegadinhas da banca e revisão do capítulo.
- Explique termos na primeira ocorrência, use exemplos concretos, comparações, tabelas Markdown quando ajudarem e conecte os conceitos.
- Cada capítulo deve possuir vários parágrafos substanciais. Não use frases soltas para simular profundidade.
- Cubra integralmente o escopo informado, sem inventar leis, números, jurisprudência ou versões. Indique a data de referência quando o conteúdo puder mudar.
- O campo markdown de cada capítulo aceita títulos, subtítulos, listas, negrito, tabelas e citações.

RESUMO COMPLETO E REVISÃO RÁPIDA:
- O summary consolida toda a teoria de modo estruturado, ainda detalhado o bastante para estudar.
- O quickReview é uma revisão de poucos minutos: conceitos-chave, diferenças, fórmulas e regras.
- Em tips, escreva bizus objetivos. Em traps, erros e confusões típicas de prova.
- Em activeRecall, escreva perguntas curtas que obriguem o aluno a lembrar sem olhar a resposta.
- Em errorConcepts, agrupe conceitos que provavelmente originam erros, com título e explicação corretiva.

QUESTÕES:
- Produza questões inéditas no estilo da banca, sem copiar questões protegidas.
- Para questões inéditas, use questionSourceType AUTHORIAL. Nunca apresente questão autoral como real e não invente banca, órgão, ano ou sourceId.
- Use REAL somente quando houver questão original verificável. Use REAL_ADAPTED quando estiver parafraseada e inclua sourceId e sourceUrl somente se forem conhecidos.
- Cada questão terá 5 alternativas A–E, exatamente uma correta, distratores plausíveis e explicação detalhada.
- Use dificuldade FACIL, MEDIA ou DIFICIL e tags específicas.

FORMATO OBRIGATÓRIO (modelo simples recomendado para um tópico):
- Responda somente com JSON válido, sem ``` e sem qualquer texto fora do JSON.
- Use packageId estável no padrão conteudo-MATERIA-TOPICO. Para corrigir ou ampliar depois, mantenha o mesmo packageId; o app oferecerá “Atualizar”.
- Repita exatamente concurso, matéria e tópico existentes no aplicativo.
- IDs devem ser únicos em todo o pacote.

USE EXATAMENTE ESTA ESTRUTURA:
{
"version": 2,
"packageId": "conteudo-materia-topico",
"competition": "NOME EXATO DO CONCURSO",
"subject": "NOME EXATO DA MATÉRIA",
"topic": "NOME EXATO DO TÓPICO",
"teorias": [
  {
    "id": "teoria-topico-v1",
    "titulo": "Teoria completa — Nome do tópico",
    "capitulos": [
      { "id": "cap-01", "titulo": "1. Fundamentos", "markdown": "Texto longo em Markdown..." },
      { "id": "cap-02", "titulo": "2. Desenvolvimento", "markdown": "Texto longo em Markdown..." }
    ]
  }
],
"summary": "# Resumo completo\n\nConteúdo estruturado...",
"quickReview": "# Revisão rápida\n\nConteúdo para poucos minutos...",
"tips": ["Bizu objetivo 1", "Bizu objetivo 2"],
"traps": ["Pegadinha ou confusão frequente 1"],
"activeRecall": ["Pergunta de recuperação ativa 1?", "Pergunta 2?"],
"questoes": [
  {
    "id": "q-topico-001",
    "questionSourceType": "AUTHORIAL",
    "sourceId": null,
    "sourceUrl": null,
    "enunciado": "...",
    "alternativas": [
      { "chave": "A", "texto": "...", "correta": false },
      { "chave": "B", "texto": "...", "correta": true },
      { "chave": "C", "texto": "...", "correta": false },
      { "chave": "D", "texto": "...", "correta": false },
      { "chave": "E", "texto": "...", "correta": false }
    ],
    "explicacao": "...",
    "dificuldade": "MEDIA",
    "tags": ["tag"]
  }
],
"errorConcepts": [
  { "id": "erro-conceito-01", "title": "Conceito que gera confusão", "summary": "Correção clara e curta." }
],
"tags": ["tema", "subtema"]
}

Antes de responder, valide internamente: JSON puro e válido; livro realmente completo; summary diferente de quickReview; arrays presentes; quantidade solicitada de questões; exatamente uma correta por questão; nenhuma vírgula sobrando."""
}
