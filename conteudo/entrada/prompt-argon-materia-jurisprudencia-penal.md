# Pedido: escrever a MATÉRIA "Jurisprudência dos Tribunais Superiores em Direito Penal"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** as súmulas (comuns e vinculantes) e as teses mais cobradas do STF e do STJ sobre a **parte geral e a parte especial do Código Penal** e sobre a **legislação penal especial** mais cobrada (drogas, Lei Maria da Penha, crimes hediondos, estatuto do desarmamento, crimes de trânsito): princípio da insignificância, aplicação da pena e dosimetria, regime inicial, concurso de crimes, crime continuado, prescrição, furto e roubo, receptação, crimes contra a administração pública, tráfico privilegiado, violência doméstica etc.
- **Não cobre:** processo penal (nulidades, prisões, provas), execução penal e jurisprudência de outras áreas (há matérias próprias).
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
- Entendimento dos tribunais superiores acerca dos institutos de direito penal.
- Jurisprudência aplicada dos tribunais superiores em direito penal.
- Jurisprudência aplicada dos tribunais superiores e atuação da Defensoria Pública no Código de Processo Penal.
- Jurisprudência dos tribunais superiores e legislação penal relacionada aos temas.
- Cooperação jurídica internacional, medicina legal e jurisprudência aplicável.
- Súmulas e jurisprudência dos tribunais superiores sobre legislação especial.
- Súmulas, jurisprudência dos Tribunais Superiores e legislação relacionada com os temas.
- Súmulas e Jurisprudência dos tribunais superiores.
- Súmulas, jurisprudência e precedentes qualificados do STF e STJ.
- Jurisprudência aplicada dos tribunais superiores e legislação aplicável a cada um dos tópicos do edital.
- Jurisprudência e Súmulas dos Tribunais Superiores.
- Doutrina e Jurisprudência aplicada dos tribunais superiores.
- Processos e procedimentos especiais previstos no edital e jurisprudência dos tribunais superiores.
- Jurisprudência aplicada dos tribunais superiores.
- Ação penal originária dos tribunais, Lei Maria da Penha, Lei nº 12.850/2013 e jurisprudência dos tribunais superiores.
- Jurisprudência pacificada dos tribunais superiores.
- Súmulas e jurisprudência do STF e STJ.
- Súmulas penais do STF, STJ e do TJDFT.
- Súmulas dos Tribunais em matéria criminal.
- Criminologia, política criminal, execução penal, jurisprudência e precedentes relevantes conforme o conteúdo do edital.
- Súmulas do STF e do STJ.
- Observância dos tratados e convenções internacionais de direitos humanos e da jurisprudência da Corte Interamericana de Direitos Humanos no âmbito do direito penal.
- Súmulas dos tribunais superiores em matéria processual penal.
- Súmulas vinculantes e súmulas dos tribunais superiores em matéria processual penal.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "direito-penal.jurisprudencia-tribunais-superiores",
 "subject": "Direito Penal",
 "title": "Jurisprudência dos Tribunais Superiores em Direito Penal",
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
   "title": "1. Legalidade, anterioridade, interpretação e analogia",
   "markdown": "## Reserva legal e anterioridade\nO **Código Penal (CP), art. 1º**, e a **Constituição Federal (CF), art. 5º, XXXIX**, enunciam que não há crime sem lei anterior que o defina, nem pena sem prévia cominação legal. São duas exigências associadas: **reserva legal** (definição por lei em sentido próprio) e **anterioridade** (a lei incriminadora precisa existir antes da conduta). Para alguém ser responsabilizado por comportamento ainda não descrito como crime, não basta que a ação pareça injusta, imoral ou socialmente perigosa. Um ato normativo administrativo ou uma portaria não podem, isoladamente, instituir novo tipo penal nem criar pena. A legalidade também exige **taxatividade**, isto é, descr [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "Quais normas consagram legalidade penal?",
   "back": "CP, art. 1º, e CF, art. 5º, XXXIX: lei anterior deve definir crime e cominar pena."
  },
  {
   "front": "Quando lei penal severa pode retroagir?",
   "back": "Não pode retroagir em prejuízo de fato já concluído, conforme CF, art. 5º, XL."
  }
 ],
 "tips": [
  "Decore a sequência CP 1–3: legalidade, sucessão benéfica e ultratividade excepcional/temporária.",
  "Assinale tempo = atividade (CP 4º); lugar = ubiquidade (CP 6º), sem inverter os critérios."
 ],
 "traps": [
  "A prova diz que lei mais benéfica não retroage após condenação definitiva. Errado: CP 2º, parágrafo único; Súmula 611/STF.",
  "A prova afirma que vencida a lei temporária, deixa de haver punição por fatos de sua vigência. Errado: ultratividade do CP 3º."
 ],
 "activeRecall": [
  {
   "question": "Que dispositivos formam o núcleo constitucional da legalidade e da retroatividade benéfica?",
   "answer": "CF 5º, XXXIX e XL, com CP 1º e CP 2º; tipo e pena dependem de lei prévia, e lei nova favorável retroage."
  }
 ],
 "errorConcepts": [
  {
   "key": "e1",
   "title": "Legalidade, analogia e interpretação",
   "summary": "CP 1º e CF 5º, XXXIX, exigem lei prévia. Analogia não pode criar crime ou agravar pena; interpretação extensiva permanece no sentido legal possível."
  }
 ],
 "questions": [
  {
   "statement": "Um regulamento administrativo passa a proibir uma conduta e, sem lei penal anterior, pretende sujeitar seus praticantes a pena de reclusão. Qual princípio resolve o caso?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "Intervenção mínima, pois permite criar crimes apenas quando o dano for elevado.",
     "correct": false
    },
    {
     "key": "B",
     "text": "Legalidade, porque crime e pena dependem de lei anterior que os preveja.",
     "correct": true
    },
    {
     "key": "C",
     "text": "Ubiquidade, porque a punição depende dos locais da ação e do resultado.",
     "correct": false
    },
    {
     "key": "D",
     "text": "Territorialidade, porque qualquer ato nacional pode definir sanção criminal.",
     "correct": false
    },
    {
     "key": "E",
     "text": "Especialidade, porque um regulamento mais específico prevalece sobre lei geral.",
     "correct": false
    }
   ],
   "explanation": "Gabarito B. CP 1º e CF 5º, XXXIX, exigem lei anterior para estabelecer tipo e pena. A é diretriz de limitação, não fonte penal; C identifica lugar pelo CP 6º; D rege espaço penal, não criação de crimes; E seleciona normas válidas, sem converter regulamento em lei incriminadora.",
   "section": "1. Legalidade, anterioridade, interpretação e analogia",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Durante revisão de ato administrativo que tentou definir um novo crime, a equipe concluiu que, sem lei anterior em sentido próprio, a punição criminal violaria a reserva legal do CP, art. 1º, e da CF, art. 5º, XXXIX.",
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
   "explanation": "Certo. A legalidade penal exige lei anterior para definição da conduta e cominação de pena. Uma disposição administrativa pode regulamentar atividades nos limites legais, mas não supre o requisito de lei formal criadora do tipo incriminador.",
   "section": "1. Legalidade, anterioridade, interpretação e analogia",
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
   "title": "Código Penal — Decreto-Lei nº 2.848, de 7 de dezembro de 1940, versão compilada",
   "publisher": "Presidência da República",
   "reference": "Arts. 1º a 12; redação vigente consultada e distinção literal de frações monetárias no art. 11",
   "url": "https://www.planalto.gov.br/ccivil_03/decreto-lei/del2848compilado.htm",
   "accessedAt": "2026-10-09"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "direito-penal.jurisprudencia-tribunais-superiores").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de súmula inventado.
