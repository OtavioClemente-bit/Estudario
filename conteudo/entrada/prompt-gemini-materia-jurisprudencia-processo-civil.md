# Pedido: escrever a MATÉRIA "Jurisprudência dos Tribunais Superiores em Processo Civil"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** as súmulas e as teses mais cobradas do STF e do STJ em processo civil (CPC/2015): normas fundamentais, competência, gratuidade, intervenção de terceiros, tutelas provisórias, prazos e intimações, provas, sentença e coisa julgada, precedentes (IRDR, IAC, repetitivos), recursos (cabimento, preparo, agravo de instrumento e taxatividade mitigada, embargos de declaração), cumprimento de sentença e execução (impenhorabilidades, honorários), Fazenda Pública em juízo, mandado de segurança e ação civil pública. Confira se cada súmula ou tese está vigente e não superada.
- **Não cobre:** jurisprudência de direito civil material, trabalhista e penal.
- **Atualização:** pesquise na legislação oficial (Planalto, MEC, STF/STJ conforme o tema) para garantir que tudo está vigente. Hoje é 2026-10-18.
- **Nunca invente número de lei, artigo, meta, súmula ou tema.** Se não tiver certeza do número, explique o conteúdo sem o número.

## O que escrever (tudo escrito por você, questão por questão; nada de montar por programa)
1. **Teoria: 5 capítulos**, do básico ao avançado, cobrindo o escopo. Cada capítulo em Markdown, com explicação, exemplo concreto (caso), quadro-resumo do tema e uma seção "Pegadinhas". Último capítulo: "Como o tema costuma ser cobrado" (sem citar nome de banca: diga "provas de Certo/Errado", "provas com casos longos").
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
- Súmulas vinculantes e entendimento sumulado pelos tribunais superiores em matéria processual civil.
- Súmulas e Jurisprudência com entendimento dominante dos Tribunais Superiores.
- Súmulas e jurisprudência do Superior Tribunal de Justiça e do Supremo Tribunal Federal.
- Jurisprudência dos tribunais superiores.
- Jurisprudência e Súmulas do STF e do STJ.
- Jurisprudência do Supremo Tribunal Federal e do Superior Tribunal de Justiça.
- Jurisprudência STF, STJ e TJ.
- Súmulas do STF e do STJ.
- Juizados especiais, mediação, legislação processual civil especial e súmulas do STF e do STJ indicadas no programa.
- Jurisprudência dos Tribunais Superiores e do Tribunal de Justiça do Estado da Bahia em matéria relativa ao conteúdo programático de Direito Processual Civil no presente edital.
- Jurisprudência aplicada dos tribunais superiores.
- Jurisprudência aplicada dos tribunais superiores.DIREITOS DIFUSOS E COLETIVOS.
- Jurisprudência dos Tribunais Superiores.
- Jurisprudência aplicada dos tribunais superiores e atuação da Defensoria Pública no Código de Processo Civil.
- Súmulas, jurisprudência dos Tribunais Superiores e legislação relacionada com os temas.
- Súmulas e entendimento jurisprudencial dos Tribunais Superiores.
- Jurisprudência pacificada dos tribunais superiores em matéria processual civil.
- Processo nos tribunais: uniformização da jurisprudência e declaração de inconstitucionalidade.
- Edição, revisão e cancelamento de enunciado de súmula vinculante pelo Supremo Tribunal Federal (Lei nº 11.417/2006).
- Súmulas vinculantes e julgados com repercussão geral em matéria processual civil.
- Leis especiais e jurisprudência.
- Legislação processual especial e jurisprudência pertinente conforme o edital.
- Súmulas dos Tribunais Superiores e do TRF3.
- Jurisprudência dominante dos tribunais superiores em matéria processual civil.
- Jurisprudência dominante dos Tribunais Superiores em matéria processual civil.
- Jurisprudência aplicável dos Tribunais Superiores.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "processo-civil.jurisprudencia-tribunais-superiores",
 "subject": "Direito Processual Civil",
 "title": "Jurisprudência dos Tribunais Superiores em Processo Civil",
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
   "title": "1. Ação popular: fundamentos, legitimidade e nulidades",
   "markdown": "## Proteção constitucional\nA ação popular prevista no **art. 5º, LXXIII, da Constituição Federal** permite ao **cidadão** buscar a anulação de ato lesivo ao patrimônio público ou de entidade de que o Estado participe, à moralidade administrativa, ao meio ambiente e ao patrimônio histórico e cultural. Não se reduz à defesa de valores financeiros: um ato contra a moralidade administrativa ou contra patrimônio cultural também integra a finalidade constitucional. A **Lei nº 4.717/1965** disciplina o [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "Quem propõe ação popular?",
   "back": "O cidadão com cidadania política, provada por título eleitoral ou equivalente; CF, art. 5º, LXXIII, e Lei 4.717, art. 1º, § 3º."
  },
  {
   "front": "Pessoa jurídica propõe ação popular?",
   "back": "Não, conforme Súmula 365 do STF."
  }
 ],
 "tips": [
  "Identifique primeiro a finalidade: anular ato lesivo, superar omissão regulamentar ou acessar/corrigir dado próprio.",
  "Na ação popular, distinga cidadão-autor de entidade que pode trocar de posição e MP que acompanha e pode prosseguir."
 ],
 "traps": [
  "A prova apresenta associação como autora de ação popular: ainda que defenda o meio ambiente, pessoa jurídica não é cidadão; Súmula 365/STF.",
  "A prova afirma que toda improcedência popular produz coisa julgada material impeditiva de nova demanda: por deficiência de prova admite outra ação fundada em prova nova."
 ],
 "activeRecall": [
  {
   "question": "Qual a diferença entre inexistência de motivos e desvio de finalidade na ação popular?",
   "answer": "A primeira diz respeito à inexistência/inadequação dos pressupostos do ato; a segunda ao objetivo perseguido, diferente do fim legal."
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
   "statement": "Uma pessoa com título eleitoral deseja invalidar ato de alienação de bem público por prejuízo à coletividade. Qual elemento define sua legitimidade para a ação popular?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "A condição de cidadão, comprovada por título eleitoral ou equivalente.",
     "correct": true
    },
    {
     "key": "B",
     "text": "A comprovação de perda patrimonial particular em seu nome.",
     "correct": false
    },
    {
     "key": "C",
     "text": "A representação obrigatória de entidade pública interessada.",
     "correct": false
    },
    {
     "key": "D",
     "text": "A constituição prévia de associação de defesa patrimonial.",
     "correct": false
    },
    {
     "key": "E",
     "text": "A autorização expressa de todos os demais interessados.",
     "correct": false
    }
   ],
   "explanation": "Gabarito A. A CF, art. 5º, LXXIII, e o art. 1º, § 3º, da Lei 4.717 exigem cidadania. B cria dano individual desnecessário; C exige representação inexistente; D confunde associação com cidadão; E não encontra fundamento na legitimação popular individual.",
   "section": "1. Ação popular: fundamentos, legitimidade e nulidades",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Um cidadão pretende ajuizar ação popular para impedir ato lesivo ao patrimônio histórico e cultural, sem alegar dano patrimonial pessoal. A ausência de prejuízo próprio não exclui, por si só, seu interesse constitucional de agir.",
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
   "explanation": "Certo. A CF, art. 5º, LXXIII, prevê tutela do patrimônio cultural por cidadão, sem exigir prejuízo individual. A legitimidade deriva da cidadania política, e a ação busca defesa de interesse protegido, não reparação particular do autor.",
   "section": "1. Ação popular: fundamentos, legitimidade e nulidades",
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
   "title": "Constituição da República Federativa do Brasil de 1988",
   "publisher": "Presidência da República",
   "reference": "Art. 5º, incisos LXXI, LXXII, LXXIII e LXXVII",
   "url": "https://www.planalto.gov.br/ccivil_03/constituicao/constituicaocompilado.htm",
   "accessedAt": "2026-10-09"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "processo-civil.jurisprudencia-tribunais-superiores").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de lei, artigo ou meta inventado.
