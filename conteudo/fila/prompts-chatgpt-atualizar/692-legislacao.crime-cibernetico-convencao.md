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
