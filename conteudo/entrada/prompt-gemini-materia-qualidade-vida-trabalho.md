# Pedido: escrever a MATÉRIA "Qualidade de Vida no Trabalho"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** conceitos e modelos de qualidade de vida no trabalho (Walton, Hackman e Oldham, Westley, Limongi-França), programas de QVT, saúde e bem-estar do servidor, estresse e burnout, ergonomia organizacional, clima organizacional e indicadores, QVT no serviço público.
- **Não cobre:** saúde e segurança do trabalho técnica (NRs), psicologia clínica e legislação trabalhista.
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
- Atividade física, exercício físico, saúde e qualidade de vida.
- Ergonomia: espaço físico, postura, posição de trabalho.
- Segurança, Sustentabilidade e Organização. Organização e ergonomia no trabalho. Descarte correto de resíduos eletrônicos. Reaproveitamento de componentes quando possível.
- Atividade Física/Movimento Humano/Saúde e Qualidade de Vida (aptidão física e aspectos preventivos e terapêuticos do exercício físico).
- Gestão de Riscos Atuariais: identificação, avaliação e monitoramento de riscos; risco biométrico; risco financeiro; risco de longevidade; risco operacional; risco de governança; análise de sensibilidade; testes de estresse; modelagem de longo prazo; sustentabilidade fiscal.
- Sociologia do Trabalho e do Serviço Público: transformações do trabalho; relações laborais; carreiras públicas; organização do trabalho no setor público; condições de trabalho; qualidade de vida; diversidade e inclusão; assédio moral e sexual; relações étnico-raciais; equidade e acessibilidade.
- Ergonomia visual e experiência do usuário em espaços.
- Eletrotécnica aplicada, quadros de comando, controle e proteção, NR-13, normas técnicas, proteção contra incêndio e explosões, ergonomia, meio ambiente, fiscalização, medições, documentação da obra e recebimento de serviços.
- Qualidade de vida, saúde do trabalhador.
- Qualidade de vida no trabalho. Qualidade de vida e gestão de pessoas. Programas de Qualidade de Vida no Trabalho (QVT). Modelos de QVT. Medição da qualidade no trabalho e Implantação de programas de QVT.
- Produção de relatórios técnicos relacionados à segurança e saúde do trabalhador, incluindo registros de inspeções, laudos, recomendações de melhoria, acompanhamento de ações preventivas e documentação de cumprimento das normas de segurança.
- Relação trabalho e saúde: indicadores para o planejamento e intervenções na saúde do trabalhador.
- Conceitos: Design, inovação, criatividade, linguagem e planejamento visual, função (prática, estética, simbólica), semiótica peirceana aplicada ao design, design estratégico, desenho universal, ergonomia, gestalt.
- Relações Humanas no Trabalho: Psicologia aplicada ao trabalho (conceito de doença, saúde e prevenção de acidentes), Saúde Mental: fatores que interferem; importância do lazer, saúde mental e trabalho, Promoção de saúde e qualidade de vida.
- Assistência de enfermagem em saúde do trabalhador (doenças ocupacionais e não ocupacionais).
- Noções de vigilância em saúde do trabalhador.
- Organização, estrutura, finalidades e atribuições do serviço de saúde do trabalhador.
- Políticas e legislação em saúde do trabalhador: Política Nacional de Saúde do Trabalhador e da Trabalhadora (Portaria GM/MS nº 1.823/2012).
- Promoção da saúde do trabalhador.
- Níveis de atenção à saúde do trabalhador: atenção primária (promoção da saúde, proteção específica) secundária e terciária.
- Sustentabilidade, saúde ambiental e inclusão no trabalho: conceitos de saúde ambiental no contexto da saúde do trabalhador.
- Impactos das mudanças climáticas e eventos climáticos extremos na saúde do trabalhador.
- (atualizada) – Ergonomia. NR.
- Saúde, bem-estar e ergonomia.
- Ergonomia.
- Ergonomia e conforto: Especificação de mobiliário e equipamentos para postos de trabalho, com base no conforto ergonômico, incluindo as necessidades de Pessoas Com Deficiência (PCD), conforme definição Decreto Legislativo nº 186/2008 e promulgado pelo Decreto nº 6.949/2009.
- Ergonomia e conforto: Elementos da Ergonomia Cognitiva, incluindo os elementos voltados a Pessoas Com Deficiência (PCD).
- Órgãos e instituições relacionados à segurança e à saúde do trabalhador, siglas e atribuições.
- Código Civil e CLT com foco na saúde do trabalhador.
- Qualidade de vida no trabalho, segurança do trabalho, saúde ocupacional e bem-estar.
- Exercício físico, saúde e qualidade de vida.
- Política Nacional de Saúde do Trabalhador e da Trabalhadora (PNSTT).
- Programas de saúde do trabalhador, bem-estar, higiene e qualidade de vida no trabalho.
- Qualidade de vida no trabalho.
- Noções de ergonomia e anatomia bucal e dental (notação dentária).
- Orientação e educação em saúde: orientação a pacientes, familiares e cuidadores, programas de promoção da saúde e qualidade de vida.
- Psicologia Organizacional e do Trabalho: gestão de pessoas, avaliação de desempenho, treinamento e desenvolvimento, ergonomia, clima organizacional, relações de trabalho e qualidade de vida.
- Ergonomia em odontologia: postura profissional, organização do ambiente de trabalho e prevenção de doenças ocupacionais.
- Qualidade de vida no trabalho e meio ambiente: noções de ergonomia, promoção da saúde ocupacional, integração com preservação ambiental.
- Legislações relativas à saúde e à segurança do trabalho: Política Nacional de Saúde do Trabalhador e da Trabalhadora.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "gestao-pessoas.qualidade-vida-trabalho",
 "subject": "Gestão de Pessoas",
 "title": "Qualidade de Vida no Trabalho",
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
   "title": "1. Evolução, modelos, estratégia e subsistemas de gestão de pessoas",
   "markdown": "## Evolução dos modelos e fundamentos\nA gestão de pessoas coordena as contribuições humanas para os resultados da organização e as oportunidades de desenvolvimento, reconhecimento e condições adequadas de trabalho. Na abordagem mais antiga de administração de pessoal predominavam registros e rotinas; no modelo de recursos humanos consolidou-se a integração de políticas de seleção, treinamento e remuneração; no modelo estratégico as pessoas são vistas como sujeitos que criam valor, compartilham c [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "Quais são os seis subsistemas de gestão de pessoas?",
   "back": "Agregar, aplicar, recompensar, desenvolver, manter e monitorar. Eles são interdependentes."
  },
  {
   "front": "O que distingue gestão estratégica de rotinas de RH?",
   "back": "Integra as práticas de pessoas com prioridades organizacionais e entre si, sem eliminar rotinas administrativas."
  }
 ],
 "tips": [
  "Identifique o verbo da questão: atrair = agregar; descrever atribuições = aplicar; pagar = recompensar; capacitar = desenvolver; proteger saúde = manter; medir rotatividade = monitorar.",
  "Em cargos, diferencie o que o trabalho faz, o que ele exige e como o trabalhador se desempenha; três perguntas, três instrumentos."
 ],
 "traps": [
  "A prova diz que gestão estratégica extinguiu o departamento de pessoal. Errado: ela integra e reposiciona as rotinas, sem dispensar registros.",
  "A prova descreve requisitos de formação e chama de descrição de cargo. Requisitos pertencem à análise; descrição registra tarefas."
 ],
 "activeRecall": [
  {
   "question": "Qual diferença há entre alinhamento vertical e horizontal das práticas de pessoas?",
   "answer": "Vertical relaciona políticas à estratégia; horizontal assegura coerência entre seleção, avaliação, remuneração, desenvolvimento e manutenção."
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
   "statement": "Uma organização divulga oportunidades, define metas dos novos integrantes, oferece capacitação e acompanha ausências. Qual ação pertence primordialmente ao subsistema agregar pessoas?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "Atrair candidatos e selecionar pessoas conforme o perfil de trabalho requerido.",
     "correct": true
    },
    {
     "key": "B",
     "text": "Estabelecer metas e realizar avaliações periódicas de desempenho individual.",
     "correct": false
    },
    {
     "key": "C",
     "text": "Organizar cursos para reduzir lacunas de habilidades já diagnosticadas.",
     "correct": false
    },
    {
     "key": "D",
     "text": "Examinar registros de ausências para identificar tendências e riscos.",
     "correct": false
    },
    {
     "key": "E",
     "text": "Aprimorar condições psicossociais de segurança e bem-estar no trabalho.",
     "correct": false
    }
   ],
   "explanation": "Gabarito A. Agregar engloba recrutamento e seleção. B integra aplicar, por definir e avaliar entregas; C pertence a desenvolver; D é monitorar, porque converte registros em informação; E é manter, já que cuida das condições para permanência saudável.",
   "section": "1. Evolução, modelos, estratégia e subsistemas de gestão de pessoas",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Após contratar uma equipe, a unidade de RH reúne dados de rotatividade, ausências e participação em capacitações para orientar decisões. Essa coleta e interpretação correspondem primordialmente ao subsistema monitorar pessoas.",
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
   "explanation": "Certo. Monitorar pessoas inclui sistemas de informações, registros, indicadores e análises para decisões. Os resultados podem orientar desenvolver ou manter, mas reunir e interpretar dados de rotatividade e ausências é função informacional típica do monitoramento.",
   "section": "1. Evolução, modelos, estratégia e subsistemas de gestão de pessoas",
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
   "title": "Decreto nº 9.991, de 28 de agosto de 2019, com alterações posteriores",
   "publisher": "Presidência da República",
   "reference": "Arts. 1º a 7º, 16; Política Nacional de Desenvolvimento de Pessoas e Plano de Desenvolvimento de Pessoas",
   "url": "https://www.planalto.gov.br/ccivil_03/_ato2019-2022/2019/decreto/d9991.htm",
   "accessedAt": "2026-10-10"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "gestao-pessoas.qualidade-vida-trabalho").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de lei, artigo ou meta inventado.
