# Pedido: escrever a MATÉRIA "Jurisprudência dos Tribunais Superiores em Direito Administrativo"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** as súmulas (comuns e vinculantes) e as teses mais cobradas do STF e do STJ em **direito administrativo**: princípios (nepotismo, publicidade), atos administrativos e autotutela (anulação, decadência, Tema 445), agentes públicos (concurso e nomeação, estágio probatório, acumulação, teto, greve, estabilidade, PAD e súmulas sobre defesa técnica), responsabilidade civil do Estado (temas de repercussão geral), improbidade após a Lei 14.230/2021 (Tema 1199 e seguintes), licitações e contratos (Lei 14.133), serviços públicos e concessões, bens públicos, intervenção na propriedade e desapropriação, controle pelos tribunais de contas.
- **Não cobre:** jurisprudência de direito constitucional geral, tributário, previdenciário, processo civil e penal (há matérias próprias).
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
- Súmulas, jurisprudência dos Tribunais Superiores e legislação relacionada com os temas.
- Súmulas e entendimento jurisprudencial dos Tribunais Superiores.
- Súmulas e Jurisprudência dos tribunais superiores.
- Jurisprudência aplicada dos tribunais superiores.
- Súmulas, jurisprudência e precedentes qualificados dos Tribunais Superiores.
- Jurisprudência aplicada dos tribunais superiores e legislação aplicável a cada um dos tópicos do edital.
- Processo administrativo tributário estadual e jurisprudência dos Tribunais Superiores.
- Jurisprudência pacificada dos tribunais superiores.
- Súmulas e jurisprudência vinculante do STF e STJ.
- Jurisprudência e súmulas de Direito Administrativo do STJ e do STF, súmulas vinculantes e temas de repercussão geral ou recursos repetitivos.
- Licitações, contratação direta, modalidades, procedimentos, sanções e jurisprudência.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "direito-administrativo.jurisprudencia-tribunais-superiores",
 "subject": "Direito Administrativo",
 "title": "Jurisprudência dos Tribunais Superiores em Direito Administrativo",
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
   "title": "1. Conceito, atos da Administração, fatos e formação",
   "markdown": "## Definição e distinções\n**Ato administrativo em sentido estrito** é declaração unilateral de vontade da Administração, ou de quem exerça função administrativa, sob regime de direito público, apta a produzir efeitos jurídicos imediatos. Uma licença individual e a imposição de multa são exemplos de declarações jurídicas. A execução material da ordem de remover objeto da via não é a ordem em si: a ordem é ato, a remoção concreta constitui atuação material. **Atos da Administração** formam gênero mais amplo, abrangendo atos administrativos, certas atuações regidas pelo direito privado e relações [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "O que é ato administrativo em sentido estrito?",
   "back": "Declaração unilateral jurídica no exercício da função administrativa, sob regime de direito público."
  },
  {
   "front": "Ato material equivale a ato administrativo?",
   "back": "Não. A execução física pode ser fato administrativo, distinto da declaração decisória."
  }
 ],
 "tips": [
  "Localize os cinco elementos fazendo as perguntas quem, para quê, como, por quê e o quê.",
  "Separe motivo, que é a causa fática e jurídica, de motivação, que é a exposição dessa causa."
 ],
 "traps": [
  "A prova equipara presunção de legitimidade a prova definitiva da legalidade. É relativa, sujeita a controle e impugnação.",
  "A prova afirma que toda decisão pública é autoexecutória. A execução direta depende de previsão legal ou urgência compatível."
 ],
 "activeRecall": [
  {
   "question": "Que diferença existe entre ato perfeito, válido e eficaz?",
   "answer": "Perfeito concluiu formação; válido cumpre lei; eficaz pode produzir os efeitos."
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
   "statement": "Uma autoridade decide interditar um prédio, e posteriormente uma equipe instala as barreiras. Como diferenciar os dois acontecimentos?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "A decisão é ato administrativo; a instalação das barreiras é atuação material.",
     "correct": true
    },
    {
     "key": "B",
     "text": "A decisão é fato natural sem vontade; a instalação é manifestação jurídica constitutiva.",
     "correct": false
    },
    {
     "key": "C",
     "text": "Ambos são contratos, pois envolvem a Administração e os usuários.",
     "correct": false
    },
    {
     "key": "D",
     "text": "A instalação é ato normativo; a decisão é simples execução material.",
     "correct": false
    },
    {
     "key": "E",
     "text": "A instalação torna a decisão juridicamente inexistente por ser posterior.",
     "correct": false
    }
   ],
   "explanation": "Gabarito A. A ordem declara efeitos jurídicos, enquanto barreiras realizam fisicamente a ordem. B troca ato por fato natural; C pressupõe acordos não descritos; D inverte execução e declaração; E confunde execução posterior com inexistência do ato decisório.",
   "section": "1. Conceito, atos da Administração, fatos e formação",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Uma ordem administrativa que cria obrigação jurídica distingue-se da atuação material que posteriormente executa fisicamente essa ordem.",
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
   "explanation": "Certo. A declaração unilateral é ato administrativo em sentido estrito; sua execução física pode configurar fato administrativo ou atuação material. O fato de ambos ocorrerem no mesmo procedimento não transforma suas naturezas em idênticas.",
   "section": "1. Conceito, atos da Administração, fatos e formação",
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
   "title": "Lei nº 9.784, de 29 de janeiro de 1999",
   "publisher": "Presidência da República — Planalto",
   "reference": "Processo administrativo federal: arts. 2º, 11 a 15, 22, 50 e 53 a 55",
   "url": "https://www.planalto.gov.br/ccivil_03/leis/l9784.htm",
   "accessedAt": "2026-10-10"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "direito-administrativo.jurisprudencia-tribunais-superiores").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de súmula inventado.
