Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Direito Processual Civil: Processo Civil: jurisdição e competência** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Jurisdição (conceito, características, princípios, jurisdição contenciosa e voluntária, arbitragem como equivalente) e competência no CPC: competência internacional concorrente e exclusiva, critérios de fixação (matéria, pessoa, função, valor e território), foros gerais e especiais, perpetuação da jurisdição, competência absoluta e relativa, foro de eleição, prorrogação, conexão, continência, prevenção, reunião de processos, alegação e declaração de incompetência e conflito de competência.

Fica de fora (outras matérias tratam): Petição inicial, procedimento comum, condições da ação e elementos da ação em detalhe, sujeitos do processo, recursos e competência originária dos tribunais em detalhe, regras de competência do processo penal e do processo do trabalho.

Os editais pedem este assunto assim (cubra todos estes pontos):
- Modificações de competência e declaração de incompetência.
- Competência funcional e territorial.
- Jurisdição.
- Competência.
- Competência em razão do valor e da matéria.
- Competência: em razão do valor e da matéria.
- Em razão do valor e da matéria.
- Competência: em razão do valor e da matéria; competência funcional e territorial; modificações de competência e declaração de incompetência.
- Jurisdição: conceito, modalidades, poderes, princípios e órgãos.
- Jurisdição: conceito.
- Competência: conceito.
- Modificações da competência.
- Declaração de incompetência.
- Critérios determinativos da competência.
- A jurisdição.
- Jurisdição: conceito, características e princípios.
- Competência: conceito, distribuição e princípios.
- Competência interna e internacional.
- Competência interna e internacional (concorrente e exclusiva), homologação de sentença estrangeira.
- Jurisdição: conceito, características, espécies, escopos, critérios, limites e princípios.
- Competência, critérios, modificação, prevenção, prorrogação e perpetuação.
- Incompetência absoluta e relativa e meios de suscitação.
- Jurisdição: conceito, características, princípios, jurisdição contenciosa e voluntária e meios alternativos de pacificação.
- Competência: critérios, modificação, competências absoluta e relativa, declaração de incompetência, conflitos e competência originária dos tribunais superiores.
- Competência: em razão do valor e da matéria; competência funcional e territorial.

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
  "id": "processo-civil.jurisdicao-competencia",
  "subject": "Direito Processual Civil",
  "title": "Processo Civil: jurisdição e competência",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Processo Civil: jurisdição e competência"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome processo-civil.jurisdicao-competencia.json.
