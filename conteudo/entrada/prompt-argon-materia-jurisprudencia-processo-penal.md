# Pedido: escrever a MATÉRIA "Jurisprudência dos Tribunais Superiores em Processo Penal"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** as súmulas e as teses mais cobradas do STF e do STJ em **direito processual penal**: inquérito e investigação (MP investigador, arquivamento após o Pacote Anticrime, ANPP), provas (ilicitude, busca domiciliar e pessoal, cadeia de custódia, reconhecimento de pessoas, acesso a celular), prisões e medidas cautelares (audiência de custódia, preventiva de ofício, revisão periódica), competência (foro por prerrogativa, conexão, Justiça Federal), ação penal, nulidades, juiz das garantias, tribunal do júri, recursos e habeas corpus.
- **Não cobre:** jurisprudência de direito penal material e de execução penal (há matérias próprias).
- **Atualização:** a jurisprudência muda; **pesquise** para confirmar se cada súmula/tese citada está **vigente e não superada** (ex.: súmulas canceladas, overruling, tema de repercussão geral ou recurso repetitivo mais recente). Hoje é 2026-10-17.
- **Nunca invente número de súmula ou de tema.** Se não tiver certeza do número, explique o entendimento sem o número.

## O que escrever (tudo escrito por você, questão por questão; nada de montar por programa)
1. **Teoria: 5 capítulos**, do básico ao avançado, cobrindo o escopo. Cada capítulo em Markdown, com explicação, exemplo concreto (caso), quadro das súmulas/teses do tema e uma seção "Pegadinhas". Último capítulo: "Como o tema costuma ser cobrado" (sem citar nome de banca: diga "provas de Certo/Errado", "provas com casos longos").
2. **summary** (resumo de 1 tela), **flashcards** (pelo menos 20; frente = súmula/tese ou pergunta curta, verso = resposta), **tips** (7), **traps** (7), **activeRecall** (8 perguntas com resposta), **errorConcepts** (5 confusões típicas, com key "e1", "e2"... "e5", título e resumo).
3. **45 questões**: 25 de múltipla escolha (A–E) e 20 de Certo/Errado (format "TRUE_FALSE", com duas opções: key "C" texto "Certo" e key "E" texto "Errado"); 15 fáceis (FACIL), 15 médias (MEDIA), 15 difíceis (DIFICIL).
   - Uma única alternativa correta; distratores plausíveis (erro típico de quem estudou mal). **Proibido distrator absurdo ou cômico.**
   - Gabarito espalhado (nenhuma letra acima de 40% das de múltipla escolha); a correta NÃO pode ser a alternativa mais longa em mais de 40% das questões.
   - Explicação de 120+ caracteres dizendo por que a certa está certa e por que as erradas erram; em Certo/Errado começa com "Certo." ou "Errado.".
   - Cada questão com "section" = título EXATO (copiado) de um capítulo e "errorConceptKey" = uma key de errorConcepts.
   - Questões autorais: sourceType "AUTHORIAL", board/agency/year/sourceUrl = null.
4. **Neutra:** sem nome de banca, órgão ou tribunal regional como contexto das questões.
5. **sources:** liste as fontes que você realmente consultou (STF, STJ, Planalto), com url e data de acesso.
6. **aliases:** copie até 20 textos EXATOS da lista "Tópicos de edital" abaixo (os mais específicos; nada genérico).

## Tópicos de edital (textos reais, para os aliases)
- Súmulas do Supremo Tribunal Federal e Superior Tribunal de Justiça relacionados ao conteúdo.
- Jurisprudência aplicada dos tribunais superiores e atuação da Defensoria Pública no Código de Processo Penal.
- Cooperação jurídica internacional, medicina legal e jurisprudência aplicável.
- Súmulas e jurisprudência dos tribunais superiores sobre legislação especial.
- Súmulas, jurisprudência dos Tribunais Superiores e legislação relacionada com os temas.
- Súmulas e Jurisprudência dos tribunais superiores.
- Súmulas, jurisprudência e precedentes qualificados do STF e STJ.
- Jurisprudência aplicada dos tribunais superiores e legislação aplicável a cada um dos tópicos do edital.
- Jurisprudência e Súmulas dos Tribunais Superiores.
- Processos e procedimentos especiais previstos no edital e jurisprudência dos tribunais superiores.
- Jurisprudência aplicada dos tribunais superiores.
- Ação penal originária dos tribunais, Lei Maria da Penha, Lei nº 12.850/2013 e jurisprudência dos tribunais superiores.
- Súmulas e jurisprudência do STF e STJ.
- Súmulas dos Tribunais em matéria criminal.
- Súmulas dos tribunais superiores em matéria processual penal.
- Súmulas vinculantes e súmulas dos tribunais superiores em matéria processual penal.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "processo-penal.jurisprudencia-tribunais-superiores",
 "subject": "Direito Processual Penal",
 "title": "Jurisprudência dos Tribunais Superiores em Processo Penal",
 "version": 1,
 "status": "PUBLISHED",
 "aliases": [
  "(até 20 textos exatos da lista abaixo)"
 ],
 "scope": {
  "covers": "...",
  "excludes": "..."
 },
 "theoryTitle": "...",
 "chapters": [
  {
   "title": "1. Direito de ação, condições, princípios e peças acusatórias",
   "markdown": "## Direito de ação e pretensão punitiva\nA **ação penal** é o instrumento pelo qual se provoca a jurisdição para examinar uma imputação criminal. O exercício da ação não se confunde com condenação: uma demanda regular pode terminar em absolvição. A **pretensão punitiva** estatal corresponde à possibilidade de buscar aplicação de sanção penal a infração, com respeito ao contraditório, ampla defesa e juiz natural. A **Constituição, art. 129, I**, atribui ao Ministério Público a promoção privativa da **ação penal pública** na forma da lei. A **Constituição, art. 5º, LIX**, permite ação privada sub [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "Qual é a regra de iniciativa da ação penal?",
   "back": "Ação pública, salvo se a lei expressamente declarar iniciativa privada (CP, art. 100)."
  },
  {
   "front": "Quem oferece denúncia?",
   "back": "O titular constitucional da ação penal pública, nos termos do art. 129, I, da Constituição e art. 24 do CPP."
  }
 ],
 "tips": [
  "Em cada caso, descubra primeiro quem é titular: iniciativa pública, privada exclusiva, personalíssima ou subsidiária.",
  "Separe três prazos distintos: 5/15 dias para denúncia, 6 meses de decadência geral e 12 meses nas hipóteses da Lei 15.438/2026."
 ],
 "traps": [
  "A prova trata toda comunicação de ocorrência como representação formal: é preciso vontade inequívoca nas circunstâncias do CPP, art. 39.",
  "A prova fala em decadência doméstica de seis meses em 2026: Lei 15.438 acrescentou hipótese especial de doze meses."
 ],
 "activeRecall": [
  {
   "question": "Qual diferença entre denúncia e queixa-crime?",
   "answer": "Denúncia é peça inicial da ação pública; queixa é iniciativa privada do ofendido ou legitimado."
  }
 ],
 "errorConcepts": [
  {
   "key": "e1",
   "title": "...",
   "summary": "..."
  }
 ],
 "questions": [
  {
   "statement": "Uma infração penal está sujeita à ação pública incondicionada. Com suporte probatório mínimo, qual iniciativa corresponde ao regime geral do Código Penal?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "Denúncia pelo titular da ação pública, sem depender de representação da vítima.",
     "correct": true
    },
    {
     "key": "B",
     "text": "Queixa oferecida pela vítima, condicionada à concordância do titular público.",
     "correct": false
    },
    {
     "key": "C",
     "text": "Denúncia pela vítima, após requisição formulada pelo magistrado competente.",
     "correct": false
    },
    {
     "key": "D",
     "text": "Queixa subsidiária apresentada antes de começar o prazo para denúncia pública.",
     "correct": false
    },
    {
     "key": "E",
     "text": "Representação que se transforma automaticamente em sentença condenatória judicial.",
     "correct": false
    }
   ],
   "explanation": "Gabarito A. CP, art. 100, e CPP, art. 24: a iniciativa pública incondicionada é exercida por denúncia e não depende de representação. B descreve peça privada; C atribui denúncia à vítima; D exige inércia pública ainda inexistente; E confunde condição de procedibilidade com decisão de mérito.",
   "section": "1. Direito de ação, condições, princípios e peças acusatórias",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Uma denúncia de ação pública pode ser rejeitada por falta de justa causa quando não há suporte informativo mínimo que vincule o acusado ao fato narrado.",
   "format": "TRUE_FALSE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "C",
     "text": "Certo",
     "correct": true
    },
    {
     "key": "E",
     "text": "Errado",
     "correct": false
    }
   ],
   "explanation": "Certo. O CPP, art. 395, III, autoriza rejeição por ausência de justa causa mesmo se a peça atender formalmente ao art. 41. A exigência consiste em lastro mínimo, não certeza condenatória, e não se confunde com inépcia manifesta da narração ou com ausência de condição de procedibilidade.",
   "section": "1. Direito de ação, condições, princípios e peças acusatórias",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  }
 ],
 "sources": [
  {
   "kind": "OFICIAL",
   "title": "Constituição da República Federativa do Brasil de 1988, texto compilado",
   "publisher": "Presidência da República",
   "reference": "Art. 5º, LIX, e art. 129, I: ação penal privada subsidiária da pública e titularidade pública da persecução",
   "url": "https://www.planalto.gov.br/ccivil_03/constituicao/constituicao.htm",
   "accessedAt": "2026-10-09"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "processo-penal.jurisprudencia-tribunais-superiores").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de súmula inventado.
