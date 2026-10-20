# Pedido: escrever a MATÉRIA "Jurisprudência do STF em Direito Constitucional"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** as súmulas vinculantes e as teses de repercussão geral e de controle concentrado mais cobradas do **STF em direito constitucional**: direitos fundamentais (liberdade de expressão, privacidade, inviolabilidade de domicílio, igualdade e ações afirmativas), remédios constitucionais, nacionalidade e direitos políticos, organização do Estado e repartição de competências (temas federativos), poderes (imunidades parlamentares, CPIs, processo legislativo, medidas provisórias), Poder Judiciário e CNJ, funções essenciais à Justiça, controle de constitucionalidade (efeitos, modulação, reclamação), ordem social (saúde, educação).
- **Não cobre:** jurisprudência específica de direito administrativo, tributário, penal e processual (há matérias próprias).
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
- Jurisprudência do STF, STJ, TCU e TCE-CE relacionada ao processo legislativo, constitucionalidade, Administração Pública, licitações, contratos e controle institucional.
- Jurisprudência aplicada dos tribunais superiores em direito constitucional.
- Jurisprudência constitucional e disposições constitucionais aplicáveis ao Ministério Público.
- Súmulas, jurisprudência dos Tribunais Superiores e legislação relacionada com os temas.
- Súmulas e entendimento jurisprudencial dos Tribunais Superiores.
- Súmulas e Jurisprudência dos tribunais superiores.
- Súmulas, jurisprudência e precedentes qualificados dos Tribunais Superiores.
- Jurisprudência aplicada dos tribunais superiores e legislação aplicável a cada um dos tópicos do edital.
- Jurisprudência aplicada dos tribunais superiores.
- Incidentes de inconstitucionalidade, prejulgados e súmulas de jurisprudência.
- Processo nos tribunais: uniformização da jurisprudência e declaração de inconstitucionalidade.
- Ordem dos processos no Tribunal. Uniformização da jurisprudência. Declaração de inconstitucionalidade. Súmulas. Súmula Vinculante.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "direito-constitucional.jurisprudencia-tribunais-superiores",
 "subject": "Direito Constitucional",
 "title": "Jurisprudência do STF em Direito Constitucional",
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
   "title": "1. Administração pública: princípios, alcance e organização constitucional",
   "markdown": "## A Constituição como regra de atuação administrativa\nA administração pública existe para realizar fins públicos, e não preferências do agente. O art. 37, **caput**, da Constituição de 1988 vincula a administração **direta e indireta** de **todos os Poderes** da União, dos estados, do Distrito Federal e dos municípios aos princípios da **legalidade, impessoalidade, moralidade, publicidade e eficiência**. O conhecido conjunto LIMPE é ponto de partida, não rol fechado: igualdade, razoabilidade, motivação, controle e segurança jurídica dialogam com o sistema constitucional. Por exemplo, uma unid [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "Quais são os princípios expressos do art. 37?",
   "back": "Legalidade, impessoalidade, moralidade, publicidade e eficiência; alcançam administração direta e indireta de todos os Poderes e entes."
  },
  {
   "front": "O que proíbe o art. 37, § 1º?",
   "back": "Publicidade estatal que caracterize promoção pessoal de autoridades ou servidores por nomes, símbolos ou imagens."
  }
 ],
 "tips": [
  "Leia o verbo do art. 37, XIX: lei específica **cria** autarquia, mas **autoriza instituir** as outras entidades.",
  "Antes de marcar qualquer acumulação, verifique as três hipóteses do art. 37, XVI, compatibilidade e teto; use a redação da EC 138/2025."
 ],
 "traps": [
  "**Como a prova escreve:** \"LIMPE só vale para o Executivo.\" **Por que erra:** ignora o alcance a todos os Poderes. **Certo:** art. 37, caput, aplica-se também à administração dos outros Poderes.",
  "**Como a prova escreve:** \"Empresa pública é criada diretamente por lei específica.\" **Por que erra:** confunde criação e autorização. **Certo:** a lei cria autarquia e autoriza instituir empresa pública, art. 37, XIX."
 ],
 "activeRecall": [
  {
   "question": "Uma publicidade oficial pode mencionar gestores como peça de autopromoção?",
   "answer": "Não. Art. 37, § 1º: caráter educativo, informativo ou orientação social, sem promoção pessoal."
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
   "statement": "Uma secretaria estadual pretende usar verba institucional para publicar um anúncio destacando o nome e a fotografia da autoridade responsável. À luz da Constituição, qual encaminhamento é adequado?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "Manter o anúncio se a publicidade não comprometer o orçamento e tiver ampla circulação social.",
     "correct": false
    },
    {
     "key": "B",
     "text": "Veicular a campanha desde que a autoridade autorize previamente o uso de sua própria imagem.",
     "correct": false
    },
    {
     "key": "C",
     "text": "Retirar os elementos de promoção pessoal e manter o conteúdo educativo e informativo oficial.",
     "correct": true
    },
    {
     "key": "D",
     "text": "Publicar a imagem após concordância expressa de todos os servidores vinculados à secretaria.",
     "correct": false
    },
    {
     "key": "E",
     "text": "Transferir a publicidade para entidade indireta a fim de afastar a restrição constitucional.",
     "correct": false
    }
   ],
   "explanation": "Gabarito C. O art. 37, § 1º, impede nomes, símbolos ou imagens caracterizadores de promoção pessoal em publicidade oficial. A: Custo e alcance não tornam lícita a autopromoção. B: Consentimento da autoridade não afasta impessoalidade. D: Anuência de servidores não elimina a vedação constitucional. E: A regra também alcança a administração indireta.",
   "section": "1. Administração pública: princípios, alcance e organização constitucional",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Uma autarquia municipal deve respeitar legalidade, impessoalidade, moralidade, publicidade e eficiência na execução de atividades administrativas, ainda que tenha personalidade jurídica própria.",
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
   "explanation": "Certo. O art. 37, caput, estende expressamente os princípios à administração direta e indireta dos municípios e demais entes federativos. Personalidade jurídica própria não excepciona autarquias do dever constitucional.",
   "section": "1. Administração pública: princípios, alcance e organização constitucional",
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
   "publisher": "Presidência da República — Casa Civil",
   "reference": "Constituição Federal, arts. 37 a 43; e referências estritamente instrumentais aos dispositivos mencionados nesses artigos",
   "url": "https://www.planalto.gov.br/ccivil_03/constituicao/constituicaocompilado.htm",
   "accessedAt": "2026-10-08"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "direito-constitucional.jurisprudencia-tribunais-superiores").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de súmula inventado.
