# Pedidos para o Gemini: 50 matérias novas (19/10/2026)

Como usar: cole a **Mensagem 0** uma vez e depois um pedido por mensagem. Se ele começar a errar o formato, abra uma conversa nova e cole a Mensagem 0 de novo.
A ordem é por número de concursos que pedem o assunto (contagem por palavra-chave: é estimativa).

## Mensagem 0 (regras: cole uma vez)

```
Você vai escrever matérias de estudo para concursos públicos brasileiros para o app Estudário. Vou pedir UMA matéria por mensagem. Cada resposta deve ser UM arquivo JSON completo e válido, sem nada antes ou depois, no formato abaixo. Escreva em português do Brasil, com nível de cursinho preparatório caro: teoria completa, exemplos, como a banca cobra e pegadinhas.

FORMATO (todos os campos são obrigatórios):
{
  "id": "<o id que eu passar>",
  "subject": "<área, ex.: Medicina>",
  "title": "<título curto da matéria>",
  "version": 1,
  "status": "PUBLISHED",
  "aliases": ["<o título>", "<cada tópico de exemplo que eu passar, copiado exatamente>", "..."],
  "scope": { "covers": "<o que a matéria cobre, 20 a 2000 caracteres>", "excludes": "<o que fica de fora (pode ser vazio)>" },
  "theoryTitle": "<título da teoria>",
  "chapters": [ { "title": "<título único>", "markdown": "<texto em Markdown>" } ],
  "summary": "<resumo em Markdown, mínimo 400 caracteres>",
  "flashcards": [ { "front": "...", "back": "..." } ],
  "tips": ["..."],
  "traps": ["..."],
  "activeRecall": [ { "question": "...", "answer": "..." } ],
  "errorConcepts": [ { "key": "e1", "title": "...", "summary": "..." } ],
  "questions": [ ... ],
  "sources": [ { "kind": "OFICIAL", "title": "...", "publisher": "...", "reference": "...", "url": "https://...", "accessedAt": "2026-10-18" } ]
}

QUANTIDADES (mínimo e MÁXIMO, o conferidor recusa fora disso):
- chapters: 3 a 6 capítulos, cada um com no mínimo 800 caracteres; a teoria somada deve passar de 6.000 caracteres (o ideal é 15.000 a 30.000).
- flashcards: 12 a 30, sem frente repetida. tips: 3 a 8. traps: 3 a 8. activeRecall: 5 a 10. errorConcepts: 2 a 6, com chaves e1, e2... até e6. sources: 1 a 12. aliases: até 60, cada um com pelo menos 10 letras.
- questions: 40 ou mais, sendo pelo menos 15 Certo/Errado e pelo menos 15 de múltipla escolha com 5 alternativas (A a E), e pelo menos 8 de cada dificuldade (FACIL, MEDIA, DIFICIL).

CADA QUESTÃO:
{
  "statement": "<enunciado, 20 a 4000 caracteres>",
  "format": "MULTIPLE_CHOICE" ou "TRUE_FALSE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [ {"key":"A","text":"...","correct":false}, ... ],
  "explanation": "<mínimo 150 caracteres: por que a certa está certa e onde está o erro das outras>",
  "section": "<título EXATO de um dos capítulos>",
  "errorConceptKey": "<uma chave de errorConcepts, ou null>",
  "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
}
- TRUE_FALSE usa exatamente as opções {"key":"C","text":"Certo"} e {"key":"E","text":"Errado"}, com uma correta.
- Exatamente UMA opção correta. Sem alternativas repetidas. Sem enunciado repetido.
- A alternativa correta NÃO pode ser sempre a mais longa nem sempre a mais curta (no máximo 40% das vezes cada); distribua a letra certa entre A, B, C, D e E.
- Não comece várias alternativas ou explicações com a mesma frase de molde; varie o que se pergunta (caso clínico, conceito, conduta, exceção, "assinale a incorreta").
- Em Certo/Errado, a explicação fala do próprio item (não de "alternativas").

REGRAS DE CONTEÚDO:
- Siga diretrizes e protocolos oficiais vigentes (Ministério da Saúde, conselhos profissionais, leis federais). Se não tiver CERTEZA de um número de lei, portaria, resolução, dose ou valor, NÃO cite o número: explique o conteúdo. Nunca invente norma, julgado, estudo ou dado.
- Não cite julgados ou normas "recentes" que você não consiga confirmar.
- sources: só fontes oficiais reais (kind "OFICIAL", ou "COMPLEMENTAR" para manuais e livros conhecidos), com link real.
- Não escreva texto de rascunho ("lorem", "exemplo genérico", "a completar") nem a palavra TODO em maiúsculas.
- Numa alternativa, não cole outra frase depois do ponto final; escreva uma frase só.

Responda "OK, pode mandar a primeira" e aguarde.
```

## Pedidos

### 1. Gestão de laboratório, biossegurança e controle de qualidade  (57 concursos, 155 tópicos)
```
Matéria 1. id: "laboratorio.gestao-biosseguranca-qualidade". subject: "Laboratório". Título: "Gestão de laboratório, biossegurança e controle de qualidade".
Cubra: níveis de biossegurança, EPI e EPC, cabines de segurança biológica, descarte de resíduos de serviços de saúde, limpeza, desinfecção e esterilização de materiais, controle de qualidade interno e externo (Levey-Jennings, regras de Westgard), boas práticas e normas sanitárias de laboratório clínico, acreditação, gestão de equipamentos e insumos, vidraria e preparo de bancadas.
NÃO repita (já existe): segurança em laboratório químico (quimica.seguranca-laboratorio-volumetria).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei de Biossegurança (Lei nº 11.105/2005)." | "Uso de equipamentos de proteção individual." | "Biossegurança." | "Dos crimes de biossegurança (Lei nº 11.105/2005)." | "Equipamentos de Proteção Coletiva (EPC)." | "Preparação de bancadas para aulas práticas." | "Equipamentos de Proteção." | "Equipamentos de Proteção Individual." | "Biossegurança em hemoterapia." | "Biossegurança e princípios de segurança no uso do ultrassom." | "Biossegurança em serviços de diagnóstico por imagem." | "Biossegurança aplicada aos serviços funerários."
```

### 2. Políticas para populações vulneráveis: situação de rua, pessoa com deficiência e juventude  (49 concursos, 106 tópicos)
```
Matéria 2. id: "servico-social.populacoes-vulneraveis". subject: "Serviço Social". Título: "Políticas para populações vulneráveis: situação de rua, pessoa com deficiência e juventude".
Cubra: Política Nacional para a População em Situação de Rua e decisões sobre direitos dessa população, Política Nacional de Saúde da Pessoa com Deficiência, Estatuto da Juventude e políticas de juventude, trabalho social com famílias, trabalho em rede e interdisciplinaridade, assistência em situações de calamidade (noções). Sem inventar números de leis recentes.
NÃO repita (já existe): fundamentos e SUAS (servico-social.fundamentos-politicas-instrumentos).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Trabalho social com famílias." | "Estatuto da Juventude." | "Política Nacional para a População em Situação de Rua." | "População em situação de rua." | "Estatuto da Pessoa com Deficiência do Estado do Paraná, Lei Estadual nº 18.419/2015." | "Trabalho em rede." | "Política Nacional de Saúde da Pessoa com Deficiência." | "Pessoas em situação de rua: bases, diretrizes e instrumentos constitucionais." | "Serviço Especializado para Pessoas em Situação de Rua." | "Estatuto da Pessoa com Deficiência do Paraná." | "Política Estadual da Pessoa com Deficiência (Lei Estadual nº 14.789/2012)." | "Inclusão e atenção à saúde da pessoa com deficiência."
```

### 3. Normas Regulamentadoras (NRs) de segurança e saúde no trabalho  (48 concursos, 175 tópicos)
```
Matéria 3. id: "seguranca-trabalho.normas-regulamentadoras". subject: "Segurança do Trabalho". Título: "Normas Regulamentadoras (NRs) de segurança e saúde no trabalho".
Cubra: visão geral do sistema de NRs e a nova NR 1 (GRO e PGR), NR 4 (SESMT), NR 5 (CIPA), NR 6 (EPI), NR 7 (PCMSO), NR 9, NR 10 (eletricidade), NR 11, NR 12 (máquinas), NR 15 (insalubridade) e NR 16 (periculosidade), NR 17 (ergonomia), NR 18 (construção), NR 23, NR 24, NR 26 (sinalização), NR 32 (serviços de saúde), NR 33 e NR 35. Explique o conteúdo de cada uma; não invente números de itens.
NÃO repita (já existe): gestão de SST, higiene ocupacional e combate a incêndio (já existem 3 matérias de segurança do trabalho).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "NR 32 – Segurança e Saúde no Trabalho em Serviços de Saúde (Portaria nº 485, de 11 de novembro de 2005)." | "Segurança em instalações e serviços em eletricidade (NR 10)." | "NR 24 – Condições Sanitárias e de Conforto nos Locais de Trabalho." | "Norma Regulamentadora nº 29 – NR29." | "Norma Regulamentadora 32." | "NR 32 – Estabelece as diretrizes básicas para a implementação de medidas de proteção à segurança e à saúde dos trabalhadores em serviços de saúde." | "NR 33 – Segurança e Saúde nos Trabalhos em Espaços Confinados." | "Norma Regulamentadora NR-32." | "Legislações profissionais pertinentes (sistema CONFEA-CREA, NR10 e NR35)." | "Normas de referência da Agência Nacional de Águas – ANA: NR1; NR3; NR4; NR5; NR6; NR10; NR13. GESTÃO DE CUSTOS." | "NR13 – Resolução ANA nº 271/2025 (água e esgoto)." | "Segurança em instalações elétricas, NR-10, NBR 5410, NBR 5419."
```

### 4. Engenharia de avaliações e perícias  (43 concursos, 66 tópicos)
```
Matéria 4. id: "engenharia.avaliacoes-pericias". subject: "Engenharia". Título: "Engenharia de avaliações e perícias".
Cubra: conceitos de valor e preço, métodos de avaliação (comparativo direto, evolutivo, renda, involutivo, custo), níveis de fundamentação e precisão, homogeneização de fatores, depreciação, avaliação para desapropriação, perícias judiciais de engenharia, vistoria, laudos e pareceres técnicos, engenharia legal e responsabilidade técnica.
NÃO repita (já existe): orçamento e fiscalização de obras (engenharia.obras-orcamento-fiscalizacao).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Engenharia legal." | "Engenharia de avaliações: métodos; níveis de rigor; depreciação; fatores de homogeneização; desapropriações; laudos de avaliação." | "Engenharia legal. Engenharia de avaliações: métodos, depreciação, desapropriações." | "Engenharia de avaliações. Métodos; depreciação; desapropriações." | "Avaliação de imóveis urbanos." | "Avaliações e perícias rurais." | "Avaliação imobiliária: Avaliação de imóveis da CAIXA e de terceiros, tanto urbanos quanto rurais com base na NBR 14.653, partes 1, 2, 3 e 4." | "Avaliação de imóveis rurais." | "Engenharia de avaliações: metodologia, fundamentação e aplicações." | "Noções de hidráulica, drenagem, hidrologia, solos, engenharia legal, licitações e contratos, segurança e higiene do trabalho, vistoria, perícia, laudos e pareceres técnicos." | "Avaliações e perícias de engenharia: Avaliações de imóveis. Desapropriações, servidões, indenizações, perícias, laudos e pareceres técnicos." | "Perícias de engenharia."
```

### 5. Microbiologia clínica: bacteriologia e micologia  (38 concursos, 87 tópicos)
```
Matéria 5. id: "laboratorio.microbiologia-clinica". subject: "Laboratório". Título: "Microbiologia clínica: bacteriologia e micologia".
Cubra: coleta e transporte de amostras, colorações (Gram, Ziehl-Neelsen), meios de cultura, identificação de cocos e bacilos, hemocultura, urocultura, coprocultura, antibiograma e mecanismos de resistência, micologia clínica (fungos leveduriformes e filamentosos), controle de qualidade em microbiologia.
NÃO repita (já existe): controle de qualidade microbiológico de medicamentos (farmácia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Microbiologia." | "Microbiologia oral." | "Microbiologia veterinária: morfologia, citologia, fisiologia, genética e ecologia geral de bactérias, fungos e vírus." | "Preparo de meios de cultura." | "Meios de cultura." | "Microbiologia da água e dos alimentos. Métodos de análise. Parâmetros legais." | "Laboratório: equipamentos, materiais, reagentes, meios de cultura e soluções." | "Noções de microbiologia de alimentos." | "Técnicas de biologia molecular aplicadas à microbiologia." | "Produção e utilização de meios de cultura e reagentes: Cuidados no preparo e armazenamento. Controle de eficiência e esterilização." | "Microbiologia: Noções gerais sobre morfologia, fisiologia e genética bacteriana." | "Patógenos emergentes: Principais características e importância na microbiologia de alimentos."
```

### 6. Parasitologia clínica  (34 concursos, 76 tópicos)
```
Matéria 6. id: "laboratorio.parasitologia-clinica". subject: "Laboratório". Título: "Parasitologia clínica".
Cubra: protozoários e helmintos de interesse humano, ciclos biológicos, métodos de exame parasitológico de fezes (direto, Hoffman, Faust, Baermann, Kato-Katz), parasitos do sangue e tecidos (malária, leishmanias, tripanossomas), coleta e conservação de amostras, diagnóstico laboratorial e interpretação.
NÃO repita (já existe): doenças infecciosas e parasitárias na clínica médica (medicina.doencas-infecciosas-parasitarias).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Parasitologia veterinária: relações entre os seres vivos." | "Âmbito da parasitologia." | "Conceitos empregados em parasitologia." | "Parasitologia e doenças emergentes." | "Esquistossomose, filariose, parasitoses por helmintos e protozoários." | "Enfermidades dos intestinos (transtornos diarreicos, cólon Irritável e parasitoses intestinais)." | "Característica e mecanismo de infecções causadas por vírus, bactérias e protozoários." | "Parasitologia: Fundamentos." | "Parasitologia: Parasitas de veiculação hídrica, doenças relacionadas à água e esgotos." | "Parasitologia - Patogenia, sintomatologia, epidemiologia, profilaxia e diagnóstico das parasitoses humanas; Fundamento das técnicas utilizadas no diagnóstico das parasitoses humanas." | "Enterocolopatias parasitárias. Helmintíases, Protozooses. Epidemiologia, quadro clínico e diagnóstico. Diagnóstico diferencial. Conduta terapêutica." | "Dermatite atópica, micoses superficiais, dermatoviroses, dermatozooparasitoses, urticária e angioedema."
```

### 7. Fisioterapia neurofuncional e traumato-ortopédica  (33 concursos, 131 tópicos)
```
Matéria 7. id: "fisioterapia.neurofuncional-traumato-ortopedica". subject: "Fisioterapia". Título: "Fisioterapia neurofuncional e traumato-ortopédica".
Cubra: cinesiologia e biomecânica, análise da marcha, avaliação e tratamento em AVC, lesão medular, paralisia cerebral, doenças neurodegenerativas e da unidade motora, fraturas, lesões ligamentares e pós-operatório ortopédico, amputações, órteses e próteses, eletroterapia, termoterapia e cinesioterapia, fisioterapia geriátrica e domiciliar, código de ética da fisioterapia.
NÃO repita (já existe): fisioterapia geral (fisioterapia.avaliacao-recursos-areas-clinicas).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Amputações." | "Prescrição e treinamento de órteses e próteses." | "Cinesiologia." | "Análise da marcha." | "Cinesiologia e Biomecânica." | "Princípios básicos da biomecânica e cinesiologia." | "Atividades e recursos terapêuticos: atividade lúdica; recursos tecnológicos; próteses e órteses; psicomotricidade." | "Cirurgia Pré-Protética: Preparação cirúrgica para próteses dentárias." | "Fisioterapia domiciliar para o idoso." | "Cinesiologia e biomecânica: movimento humano, análise funcional dos sistemas musculoesquelético e articular." | "Órteses, próteses e adaptações: indicação, uso e orientação ao paciente." | "Biofísica aplicada: Ação dos agentes físicos sobre o organismo; Conhecimentos básicos em termoterapia, eletroterapia, mecanoterapia, fototerapia e hidroterapia."
```

### 8. Locação de imóveis urbanos  (33 concursos, 50 tópicos)
```
Matéria 8. id: "direito-civil.locacao-imoveis-urbanos". subject: "Direito Civil". Título: "Locação de imóveis urbanos".
Cubra: âmbito da Lei do Inquilinato, locação residencial e não residencial, prazos, aluguel e reajuste, garantias locatícias, direitos e deveres de locador e locatário, sublocação, cessão e benfeitorias, direito de preferência, ação de despejo, revisional, renovatória e consignatória.
NÃO repita (já existe): contratos em espécie (direito-civil.contratos-especie).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei nº 8.245/1991 e suas alterações (Locação de imóveis urbanos). Procedimentos." | "Procedimentos: disposições gerais e ação de despejo." | "Lei nº 8.245/1991 e suas alterações (locação de imóveis urbanos)." | "Lei de Locações (Lei Federal nº 8.245/1991)." | "Da locação de imóveis urbanos (Lei nº 8.245/1991)." | "Ação de Despejo e Ação Renovatória." | "Locação de imóveis urbanos." | "Ação renovatória." | "Procedimento na ação renovatória." | "Ação de despejo." | "Normas para os processos de locação de imóveis pela Defensoria Pública do Estado de São Paulo." | "Ações da Lei de Locação dos Imóveis Urbanos (Lei n. 8.245/1991): despejo, consignatória de aluguel e acessórios, renovatória e revisional. Postulação e defesa."
```

### 9. Fisiologia do exercício, treinamento e medidas e avaliação  (32 concursos, 80 tópicos)
```
Matéria 9. id: "educacao-fisica.fisiologia-exercicio-treinamento". subject: "Educação Física". Título: "Fisiologia do exercício, treinamento e medidas e avaliação".
Cubra: bioenergética e sistemas energéticos, respostas e adaptações cardiovasculares, respiratórias e musculares ao exercício, princípios do treinamento esportivo, capacidades físicas, crescimento e desenvolvimento motor, testes, medidas e avaliação física, atividade física e saúde (riscos e benefícios), primeiros socorros na educação física.
NÃO repita (já existe): educação física escolar e cultura corporal (educacao-fisica.escola-cultura-corporal).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Psicomotricidade." | "Atividade Física/Movimento Humano/Performance (desempenho e condicionamento humano)." | "Avaliação física, testes, medidas e prescrição de exercícios para crianças, adolescentes, adultos, idosos e grupos especiais." | "Esporte e mídia: do jogo ao telespetáculo; o discurso midiático sobre exercício físico, saúde e estética - implicações na educação física escolar; a televisão e a mediação tecnológica do esporte." | "Aspectos fisiológicos da atividade física e do exercício físico na formação humana." | "Exercício físico: riscos e benefícios à saúde na formação de crianças, jovens e adultos." | "Motricidade Humana." | "Atividade Física/Movimento Humano/Saúde e Qualidade de Vida (aptidão física e aspectos preventivos e terapêuticos do exercício físico)." | "Sedentarismo e atividade física." | "Fatores de adesão e permanência na atividade física, no exercício físico e na prática esportiva." | "Atividade física e alimentação." | "Testes, medidas e avaliações físicocorporais."
```

### 10. Imunologia clínica e sorologia  (31 concursos, 118 tópicos)
```
Matéria 10. id: "laboratorio.imunologia-clinica". subject: "Laboratório". Título: "Imunologia clínica e sorologia".
Cubra: resposta imune inata e adaptativa, antígenos e anticorpos, sistema complemento, reações antígeno-anticorpo (aglutinação, precipitação, ELISA, imunofluorescência, quimioluminescência, Western blot), sorologia de HIV, hepatites, sífilis, toxoplasmose e rubéola, autoimunidade (FAN, fator reumatoide), imunodeficiências, citometria de fluxo.
NÃO repita (já existe): imuno-hematologia (laboratorio.imuno-hematologia-banco-sangue).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Imunologia." | "Síndrome da imunodeficiência adquirida." | "Hemoterapia, banco de sangue, sorologia, triagem e rastreabilidade." | "Dermatoses por imunodeficiência." | "Imunologia clínica." | "Imunologia nas doenças infecciosas." | "Imunologia veterinária e patologia dos animais de produção." | "Avaliação do sistema imune celular e humoral, das imunodeficiências." | "Citometria de fluxo." | "Sanidade e biossegurança na aquicultura: prevenção, diagnóstico, controle de enfermidades, boas práticas de produção e imunologia aplicada a organismos aquáticos." | "Etiologia, patogenia, epidemiologia, fisiopatologia, imunologia, sintomatologia, diagnóstico, tratamento, profilaxia e controle das seguintes doenças:." | "Neoplasias – aspectos gerais: Biologia do Tumor e Marcadores Tumorais Imunologia e Imunoterapia Tumoral Melanoma e Neoplasias Cutâneas Malignas Sarcoma de Tecidos Moles Tumores Ósseos."
```

### 11. Instalações prediais elétricas, SPDA, elevadores e climatização  (30 concursos, 60 tópicos)
```
Matéria 11. id: "engenharia.instalacoes-prediais-eletricas-climatizacao". subject: "Engenharia". Título: "Instalações prediais elétricas, SPDA, elevadores e climatização".
Cubra: instalações elétricas de baixa tensão em edificações (dimensionamento, proteção, aterramento), SPDA, instalações de incêndio (noções), elevadores e transporte vertical, ar-condicionado e climatização (sistemas, carga térmica, manutenção e qualidade do ar), instalações de gás, manutenção predial.
NÃO repita (já existe): instalações hidrossanitárias e saneamento (engenharia.saneamento-abastecimento-esgotamento); instalações elétricas industriais (engenharia-eletrica.instalacoes-maquinas-medidas).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Operação e projeto de máquinas de elevação e transporte: elevadores, monta-cargas, plataformas, escadas e esteiras rolantes, equipamentos específicos para portadores de necessidades especiais." | "Ar-condicionado." | "Elevadores." | "Lei nº 13.589/2018 – Manutenção de instalações e equipamentos de sistemas de climatização de ambientes." | "Operação e projeto de máquinas de elevação e transporte: elevadores, montacargas, plataformas, escadas e esteiras rolantes, equipamentos específicos para portadores de necessidades especiais." | "Máquinas de elevação e transporte, elevadores, monta-cargas, plataformas, escadas e esteiras rolantes." | "Ventilação e ar-condicionado." | "Sistemas de transportes vertical e horizontal (elevadores, monta-cargas, escadas e esteiras rolantes). ABNT NBR 16858. ABNT NBR 16083. Demais normas técnicas pertinentes." | "Noções de projetos complementares e de projetos de arquitetura: elevadores." | "Noções de projetos complementares e de projetos de arquitetura: ar-condicionado." | "Sistemas de Proteção contra Descargas Atmosféricas (SPDA) e medidas de proteção contra surtos, conforme a série ABNT NBR 5419 vigente." | "Instalações elétricas em baixa tensão: projeto de instalações prediais e industriais; acionamentos elétricos; motores elétricos de indução e diagramas de comando."
```

### 12. Terapia ocupacional: fundamentos e prática clínica  (29 concursos, 265 tópicos)
```
Matéria 12. id: "terapia-ocupacional.fundamentos-pratica". subject: "Terapia Ocupacional". Título: "Terapia ocupacional: fundamentos e prática clínica".
Cubra: fundamentos e modelos da terapia ocupacional, atividades de vida diária e instrumentais, estudo do brincar, do lazer e do trabalho, cinesiologia e biomecânica aplicadas, neurologia e saúde mental em terapia ocupacional, tecnologia assistiva, órteses e adaptações, infância e adolescência, inclusão social, ética e deontologia da terapia ocupacional.
NÃO repita (já existe): fisioterapia (fisioterapia.avaliacao-recursos-areas-clinicas).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Tecnologia assistiva." | "Código de Ética e Deontologia da Terapia Ocupacional." | "Fundamentos da terapia ocupacional." | "Cinesiologia e biomecânica aplicada à terapia ocupacional." | "Clínica em terapia ocupacional – saúde da criança e do adolescente." | "Estudo das atividades de vida diária." | "Estudo do brincar." | "Estudo do lazer." | "Estudo do trabalho." | "Intervenções em terapia ocupacional." | "Neuroanatomia aplicada à terapia ocupacional." | "Neurologia para a terapia ocupacional."
```

### 13. Educação em saúde e educação popular em saúde  (29 concursos, 207 tópicos)
```
Matéria 13. id: "saude.educacao-em-saude". subject: "Saúde". Título: "Educação em saúde e educação popular em saúde".
Cubra: conceitos de educação em saúde, educação popular (Paulo Freire e a política nacional), comunicação em saúde, estratégias e metodologias (grupos, oficinas, rodas de conversa, metodologias ativas), educação em saúde na atenção básica e nas escolas (PSE), participação comunitária, avaliação de ações educativas.
NÃO repita (já existe): promoção da saúde e políticas (saude-publica.politicas-nacionais-saude).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Educação em saúde: Estratégias de educação em saúde; Comunicação em saúde; Participação comunitária." | "Educação em saúde." | "Educação em saúde: planejamento, gestão e avaliação de projetos." | "Educação em saúde: ações educativas e mobilização da comunidade para prevenção de zoonoses." | "Educação em saúde: planejamento e execução de treinamentos, palestras e orientação à população." | "Orientação e educação em saúde: orientação a pacientes, familiares e cuidadores, programas de promoção da saúde e qualidade de vida." | "Educação em saúde: técnicas de orientação ao paciente, ações educativas individuais e coletivas." | "Educação em saúde - noções básicas." | "Educação em saúde e promoção da saúde do trabalhador." | "Educação em saúde e orientação aos pacientes." | "Educação em saúde bucal, controle do biofilme e utilização de fluoretos." | "Educação em saúde, mobilização social e comunicação de riscos."
```

### 14. Oncologia clínica  (28 concursos, 462 tópicos)
```
Matéria 14. id: "medicina.oncologia-clinica". subject: "Medicina". Título: "Oncologia clínica".
Cubra: biologia do câncer e carcinogênese, epidemiologia e rastreamento dos principais cânceres, estadiamento TNM, princípios de quimioterapia, radioterapia, hormonioterapia e imunoterapia, emergências oncológicas, síndromes paraneoplásicas, manejo de toxicidades, principais tumores (mama, pulmão, próstata, colorretal, colo do útero).
NÃO repita (já existe): oncologia e cuidado de enfermagem (saude.oncologia-cuidado-paciente).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Noções de radioterapia para o paciente neurocirúrgico." | "Biologia molecular relacionada ao câncer." | "Câncer de pulmão." | "Carcinogênese, biologia dos tumores e marcadores tumorais em neurocirurgia." | "Câncer da laringe e hipofaringe: glândulas salivares e seios paranasais." | "Complicações da radioterapia." | "Cardio-oncologia." | "Neoplasias articulares." | "Neoplasia endócrina múltipla." | "Oncologia pediátrica." | "Ginecológicas: doença inflamatória pélvica, câncer ginecológico, leucorreia, câncer de mama." | "Neoplasias endócrinas múltiplas."
```

### 15. Audiologia  (28 concursos, 107 tópicos)
```
Matéria 15. id: "fonoaudiologia.audiologia". subject: "Fonoaudiologia". Título: "Audiologia".
Cubra: anatomia e fisiologia da audição, etiologia e classificação das perdas auditivas, audiometria tonal e vocal, mascaramento, logoaudiometria, imitanciometria, emissões otoacústicas e PEATE, triagem auditiva neonatal, aparelhos de amplificação sonora e reabilitação auditiva, audiologia ocupacional.
NÃO repita (já existe): otorrinolaringologia médica (medicina.otorrinolaringologia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Avaliação audiológica por via aérea e por via óssea, mascaramento, logoaudiometria e imitanciometria." | "Anatomofisiologia clínica das fossas e seios paranasais, laringe, faringe e órgão da audição." | "Morfofisiopatologia da fonação e da audição." | "Audiologia: Anatomia e fisiologia da audição." | "Etiologia e classificação dos distúrbios de audição." | "Avaliação audiologia." | "Saúde pública: Prevenção e promoção da saúde em fonoaudiologia." | "Fonoaudiologia e a Instituição Escolar." | "Fundamentos da Fonoaudiologia: conceitos, áreas de atuação e princípios da fonoaudiologia, desenvolvimento da linguagem oral e escrita." | "Avaliação e Diagnóstico Fonoaudiológico: métodos e técnicas de avaliação, diagnóstico dos distúrbios da comunicação, avaliação da fala, linguagem, voz, audição e motricidade orofacial." | "Fonoaudiologia Educacional: atuação em ambiente escolar, prevenção de dificuldades de aprendizagem, orientação a professores, alunos e famílias." | "Fonoaudiologia na Saúde Pública: atuação no SUS, promoção, prevenção e reabilitação em saúde, programas de saúde auditiva e comunicação."
```

### 16. ISS, IPTU, ITBI e tributos municipais  (28 concursos, 44 tópicos)
```
Matéria 16. id: "direito-tributario.iss-tributos-municipais". subject: "Direito Tributário". Título: "ISS, IPTU, ITBI e tributos municipais".
Cubra: competência tributária municipal, ISS (fato gerador, lista de serviços, local da incidência, base de cálculo, alíquotas mínima e máxima, substituição e retenção), IPTU (progressividade, função social, base de cálculo), ITBI, taxas e contribuição de iluminação pública, contribuição de melhoria, Simples Nacional e o ISS, transição da reforma tributária para o IBS (noções).
NÃO repita (já existe): impostos em espécie (direito-tributario.impostos-especie).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei Complementar nº 116/2003." | "Legislação do ISS." | "Lei Complementar Federal nº 116/2003." | "Fiscalização do ITBI." | "Lei complementar nº 116/2003 (ISSQN)." | "ISSQN: lista de serviços, destinatário, substituição tributária, retenção e territorialidade." | "ISSQN (Imposto Sobre Serviços): fato gerador, base de cálculo, contribuintes, fiscalização e recolhimento." | "Lei Complementar Federal nº 116/2003 e suas alterações (dispõe sobre o ISSQN)." | "Apuração e fiscalização do ISS." | "Regras de retenção do ISSQN na fonte." | "Fiscalização do ISSQN." | "Fiscalização do IPTU."
```

### 17. Avaliação da aprendizagem e gestão escolar  (27 concursos, 212 tópicos)
```
Matéria 17. id: "educacao.avaliacao-gestao-escolar". subject: "Educação". Título: "Avaliação da aprendizagem e gestão escolar".
Cubra: avaliação diagnóstica, formativa e somativa, instrumentos e critérios, avaliação em larga escala (SAEB, IDEB), gestão democrática da escola, projeto político-pedagógico, conselhos escolares, relação escola, família e comunidade, coordenação pedagógica, observação, registro e documentação pedagógica.
NÃO repita (já existe): LDB, BNCC e didática (educacao.ldb-bncc-didatica).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Relação escola, família e comunidade." | "Aplicação pedagógica e administrativa dos indicadores para planejamento escolar, avaliação institucional, gestão democrática, políticas educacionais, formação docente e desenvolvimento profissional." | "Avaliação diagnóstica, formativa e somativa." | "Princípios da Educação estadual: gestão democrática, direito à educação, qualidade da educação e valorização dos profissionais da educação." | "Gestão escolar." | "Gestão democrática da escola pública." | "Gestão democrática do ensino público: Princípios legais e participação da comunidade escolar." | "Avaliação da aprendizagem: diagnóstica, formativa e somativa." | "Planejamento e avaliação institucional." | "Avaliação institucional." | "Avaliação da aprendizagem: avaliação diagnóstica, formativa e somativa." | "Sistemas informatizados de gestão escolar."
```

### 18. Fonoaudiologia: linguagem, fala, voz, motricidade orofacial e disfagia  (27 concursos, 141 tópicos)
```
Matéria 18. id: "fonoaudiologia.linguagem-fala-voz-disfagia". subject: "Fonoaudiologia". Título: "Fonoaudiologia: linguagem, fala, voz, motricidade orofacial e disfagia".
Cubra: desenvolvimento da linguagem oral e escrita, distúrbios fonológicos, gagueira, afasia, disartria e apraxia, voz e disfonias, motricidade orofacial, disfagia orofaríngea e neurogênica, atuação em leito hospitalar, traqueostomia e intubação, câncer de cabeça e pescoço, código de ética da fonoaudiologia.
NÃO repita (já existe): audiologia (fonoaudiologia.audiologia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Desenvolvimento da linguagem oral e escrita." | "Atuação fonoaudiológica em leito hospitalar – princípios básicos." | "Afasia, disartria e apraxia - avaliação e terapia." | "Disfagia neurogênica – avaliação e terapia." | "Disfonias neurogênicas – avaliação e terapia." | "Avaliação e terapia fonoaudiológica nas queimaduras de face e pescoço e inalatórias." | "Avaliação e terapia fonoaudiológicas nos casos de cirurgia ortognática e trauma de face." | "Tumores na cavidade oral e orofaringe – atuação fonoaudiológica." | "Atuação fonoaudiológica na área materno‐infantil." | "Enfoque fonoaudiológico na prática da amamentação." | "Disartria e gagueira: definição, etiologia, avaliação e terapia." | "Desenvolvimento e avaliação da linguagem oral e escrita."
```

### 19. Geriatria e gerontologia  (27 concursos, 78 tópicos)
```
Matéria 19. id: "medicina.geriatria-gerontologia". subject: "Medicina". Título: "Geriatria e gerontologia".
Cubra: envelhecimento normal e patológico, avaliação geriátrica ampla, grandes síndromes geriátricas (quedas, incontinência, instabilidade, imobilidade, iatrogenia, insuficiência cognitiva), demências e delirium, polifarmácia e medicamentos inapropriados, fragilidade e sarcopenia, cuidados paliativos no idoso, Estatuto da Pessoa Idosa e políticas de saúde do idoso.
NÃO repita (já existe): neurologia (medicina.neurologia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Biologia do envelhecimento. Teorias do envelhecimento." | "Aspectos biológicos e fisiológicos do envelhecimento." | "Iatrogenia e farmacologia em geriatria." | "Atuação em geriatria: envelhecimento normal e doenças da senilidade." | "Endocrinologia do envelhecimento." | "Acidentes e Violência à Criança, Puberdade e Adolescência, Direito e saúde do Idoso, Prevenção de Acidentes." | "Noções de psicogeriatria." | "Endocrinologia do envelhecimento: principais alterações hormonais da terceira idade." | "Epidemiologia do envelhecimento." | "Exames complementares e instrumentos de avaliação, Avaliação Geriátrica Ampla." | "Rim e envelhecimento." | "Gerontopsiquiatria: transtornos mentais associados ao envelhecimento."
```

### 20. Dermatologia  (26 concursos, 156 tópicos)
```
Matéria 20. id: "medicina.dermatologia". subject: "Medicina". Título: "Dermatologia".
Cubra: anatomia e fisiologia da pele, semiologia e lesões elementares, dermatites (atópica, de contato, seborreica), urticária, acne e rosácea, psoríase, infecções da pele (bacterianas, virais, micoses, ectoparasitoses), hanseníase, doenças bolhosas, manifestações cutâneas de doenças sistêmicas, lesões pré-malignas e câncer de pele, terapêutica dermatológica.
NÃO repita (já existe): doenças infecciosas (medicina.doencas-infecciosas-parasitarias).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Doenças dermatológicas/imunológicas: urticária, angioedema, anafilaxia, infecções cutâneas." | "Dermatoses metabólicas." | "Dermatoses mais frequentes na criança." | "Patologia cutânea." | "Micoses superficiais e profundas." | "Doenças dermatológicas." | "Problemas dermatológicos na infância." | "Fundamentos de Dermatologia: Anatomia e fisiologia da pele e anexos cutâneos; Semiologia dermatológica; Lesões elementares da pele; Métodos diagnósticos em dermatologia." | "Dermatofitoses." | "Dermatológicas: escabiose; pediculose; dermatofitoses; eczema; dermatite de contato; onicomicoses; infecções bacterianas imunológicas; doença do soro; edema angioneurótico; urticária; anafilaxia." | "Sistemas de controle para ar-condicionado e refrigeração: BMS, protocolos abertos (BACnet, Modbus), sensoriamento/IoT, medição e verificação de desempenho." | "Fundamentos de Dermatologia."
```

### 21. Pneumologia  (26 concursos, 111 tópicos)
```
Matéria 21. id: "medicina.pneumologia". subject: "Medicina". Título: "Pneumologia".
Cubra: asma e DPOC (diagnóstico, espirometria, tratamento), pneumonias, tuberculose pulmonar, derrame pleural e pneumotórax, tromboembolismo pulmonar, doenças intersticiais, câncer de pulmão (noções), tabagismo e seu tratamento, insuficiência respiratória, distúrbios respiratórios do sono.
NÃO repita (já existe): cardiologia e pneumologia (só noções; medicina.clinica-cardiologia-pneumologia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Pneumotórax." | "Asma brônquica." | "Tratamento do derrame pleural neoplásico." | "Derrame pleural: avaliação propedêutica." | "Alcoolismo e tabagismo." | "Avaliação ultrassonográfica de derrame pleural." | "Métodos diagnósticos laboratoriais e radiológicos em pneumologia." | "Prevenção de doenças relacionadas ao álcool, tabagismo e uso de drogas na organização." | "Prevenção cardiovascular: dislipidemia, tabagismo, atividade física." | "Pneumonias, doença pulmonar obstrutiva crônica e asma. Tromboembolismo venoso e tuberculose." | "Terapia renal substitutiva: hemodiálise, diálise peritoneal, transplante renal, hemoperfusão no tratamento das intoxicações, plasmaférese em nefrologia." | "Procedimentos diagnósticos em pneumologia."
```

### 22. Jornalismo, assessoria de imprensa e editoração  (26 concursos, 97 tópicos)
```
Matéria 22. id: "comunicacao.jornalismo-assessoria-editoracao". subject: "Comunicação". Título: "Jornalismo, assessoria de imprensa e editoração".
Cubra: gêneros e técnicas jornalísticas (lead, pirâmide invertida, notícia, reportagem, entrevista), critérios de noticiabilidade, apuração e checagem, assessoria de imprensa (release, clipping, relacionamento com a mídia), ética jornalística, editoração e processo gráfico (preparação de originais, revisão, diagramação), jornalismo digital.
NÃO repita (já existe): comunicação pública e institucional; mídias digitais; audiovisual (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Critérios de seleção, redação e edição." | "Processo gráfico: conceito de editoração, preparação de originais." | "Fotojornalismo." | "Assessoria de imprensa." | "Critérios de noticiabilidade no jornalismo." | "Ética: papel social da comunicação; código de ética do jornalista; regulamentação da profissão de jornalista." | "Manual de Jornalismo da EBC (Empresa Brasil de Comunicação)." | "Jornalismo e Interesse público." | "Jornalismo e Direitos fundamentais." | "Gêneros de redação: definição e elaboração de notícia, reportagem, entrevista, editorial, crônica, coluna, pauta, informativo, release." | "A questão da imparcialidade e da objetividade no Jornalismo." | "Editoração eletrônica e diagramação."
```

### 23. Enfermagem em saúde coletiva e na atenção básica  (26 concursos, 50 tópicos)
```
Matéria 23. id: "enfermagem.saude-coletiva-atencao-basica". subject: "Enfermagem". Título: "Enfermagem em saúde coletiva e na atenção básica".
Cubra: consulta de enfermagem, programas de saúde (hipertensão, diabetes, tuberculose, hanseníase, saúde da mulher, criança, idoso), doenças e agravos não transmissíveis, visita domiciliar, acolhimento, educação em saúde, vigilância em saúde na atenção básica, ações de enfermagem na ESF.
NÃO repita (já existe): fundamentos e procedimentos (enfermagem.fundamentos-procedimentos-cuidado-clinico); ciclos de vida (enfermagem.saude-ciclos-vida).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Acolhimento." | "Doenças e agravos não-transmissíveis." | "Orientações Técnicas: Serviços de Acolhimento para Crianças e Adolescentes (documento aprovado pela Resolução Conjunta CNAS e CONANDA nª 1/2009)." | "Serviços de acolhimento." | "Cadastramento familiar e territorial: finalidade e instrumentos, interpretação demográfica, conceito de territorialização, micro-área, área de abrangência, visita domiciliar." | "Cadastramento familiar e territorial: finalidade e instrumentos, interpretação demográfica, conceito de territorialização, microárea, área de abrangência, visita domiciliar." | "Ações de enfermagem na Atenção Básica Diabetes, Hipertensão Arterial, Prevenção de Câncer de Mama e Cérvico-Uterino." | "Recebimento/aquisição/acolhimento de animais silvestres." | "Visita domiciliar, ações educativas." | "Acolhimento e adaptação." | "Acolhimento, orientação e acompanhamento." | "Acolhimento, comunicação e preparação do paciente para o atendimento."
```

### 24. Técnicas radiológicas e radioproteção  (25 concursos, 204 tópicos)
```
Matéria 24. id: "radiologia.tecnicas-radiologicas-radioprotecao". subject: "Radiologia". Título: "Técnicas radiológicas e radioproteção".
Cubra: física das radiações e formação da imagem, equipamentos, radiologia convencional, CR e DR, posicionamento radiográfico por segmento (tórax, abdome, crânio e face, coluna, bacia, membros), contrastes e reações, mamografia, tomografia, ressonância e densitometria (noções técnicas), radioproteção (princípios, dosimetria, monitoração, blindagem), processamento e câmara escura.
NÃO repita (já existe): radiologia odontológica (odontologia.clinica-cirurgia-materiais-radiologia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Densitometria óssea." | "Radioproteção." | "Tomografia computadorizada (TC), ressonância magnética (RM), angio-RM." | "Princípios de radioproteção." | "Câmara escura - manipulação de filmes, chassis, écrans reveladores e fixadores, processadora de filmes." | "Câmara clara - seleção de exames, identificação; exames gerais e especializados em radiologia." | "Fundamentos da tomografia computadorizada e da ressonância magnética." | "Mamografia." | "Mamografia. Técnicas de posicionamento. Tumores benignos. Tumores malignos." | "Normas de radioproteção." | "Correlação da ultrassonografia com tomografia computadorizada, ressonância magnética e outros métodos de imagem." | "Tecnologia Radiologia Computadorizada – CR e Radiologia Digital – DR."
```

### 25. Psicologia social, comunitária e de grupos  (25 concursos, 47 tópicos)
```
Matéria 25. id: "psicologia.social-comunitaria-grupos". subject: "Psicologia". Título: "Psicologia social, comunitária e de grupos".
Cubra: objeto e métodos da psicologia social, atitudes, preconceito e estereótipos, influência social, identidade e representações sociais, psicologia comunitária, processos grupais e grupos operativos, interação social, psicologia e políticas públicas, métodos de pesquisa em psicologia.
NÃO repita (já existe): psicoterapia e psicopatologia (psicologia.psicoterapia-psicopatologia-clinica).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Psicologia Social." | "Interação social." | "Grupos operativos." | "Psicologia comunitária." | "Processos grupais." | "Interação social e desenvolvimento da autonomia." | "Objeto de estudo, métodos e finalidades da psicologia social." | "Ideologia, representações sociais, linguagem e identidade." | "Interfaces entre os conhecimentos da psicologia social e de outras áreas da psicologia." | "Psicologia Social e Comunitária." | "Psicologia Social e Institucional. Processos grupais e dinâmicas de grupo; relações sociais e institucionais; preconceito, estigma e exclusão social; identidade social e cultura." | "Psicologia Social: Relações interpessoais e dinâmicas grupais."
```

### 26. Controladoria e controle interno  (25 concursos, 40 tópicos)
```
Matéria 26. id: "contabilidade.controladoria-controle-interno". subject: "Contabilidade". Título: "Controladoria e controle interno".
Cubra: conceito e funções da controladoria, teoria dos sistemas aplicada às organizações, controle interno (componentes do COSO, tipos e técnicas de controle), controle interno na gestão pública, auditoria interna (noções), indicadores e relatórios gerenciais, orçamento empresarial.
NÃO repita (já existe): auditoria (pasta auditoria) e controle externo.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Aplicabilidade do conceito de controladoria à gestão pública." | "Relatórios gerenciais." | "Gestão e controladoria: Processo decisório." | "Controladoria. Teoria dos sistemas aplicada às organizações. Definição e funções da controladoria em uma organização." | "Estrutura e Atribuições: Secretaria de Controle Interno da ALEAM." | "Legislação Aplicada ao Controle Interno." | "Controle Interno no Poder Legislativo do Estado do Ceará." | "Sistema de Controle Interno na Administração Pública Estadual, conforme a Constituição Estadual." | "Regulamento do Sistema de Controle Interno do Estado." | "Informática aplicada ao controle interno." | "Controladoria: Teoria dos sistemas aplicada às organizações." | "Definição e funções da controladoria numa organização."
```

### 27. Gastroenterologia e hepatologia  (24 concursos, 125 tópicos)
```
Matéria 27. id: "medicina.gastroenterologia-hepatologia". subject: "Medicina". Título: "Gastroenterologia e hepatologia".
Cubra: doença do refluxo, dispepsia e úlcera péptica, H. pylori, hemorragia digestiva, doenças inflamatórias intestinais, síndrome do intestino irritável, pancreatites, hepatites, cirrose e suas complicações, insuficiência hepática, doenças das vias biliares, câncer gástrico e colorretal (rastreamento).
NÃO repita (já existe): cirurgia do abdome (medicina.cirurgia-geral-abdome-perioperatorio).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Insuficiência hepática." | "Hipertensão porta e cirrose." | "Úlcera péptica." | "Abordagem ambulatorial do paciente com as seguintes enfermidades: enfermidades do estômago e do esôfago (síndromes dispépticas, úlcera péptica, gastrites e esofagites de refluxo)." | "Enfermidades do fígado e vias biliares (cirrose, hepatite e colecistopatias)." | "Cirrose hepática." | "Hemorragia digestiva baixa." | "Hepatites virais." | "Gastroenterocolites infecciosas e virais. Hepatites por vírus." | "Gastrites, úlcera péptica e Helicobacter pylori." | "Ectasias vasculares do cólon. Hemorragia digestiva baixa." | "Diarreia aguda e persistente, tratamento da desidratação, refluxo gastroesofágico."
```

### 28. Endocrinologia e metabologia  (23 concursos, 203 tópicos)
```
Matéria 28. id: "medicina.endocrinologia". subject: "Medicina". Título: "Endocrinologia e metabologia".
Cubra: diabetes mellitus (diagnóstico, tratamento, insulinoterapia, complicações agudas: cetoacidose e estado hiperosmolar, hipoglicemia), doenças da tireoide (hipo, hiper, nódulos, crise tireotóxica), adrenal, hipófise, obesidade e dislipidemias, osteoporose e metabolismo do cálcio, endocrinologia pediátrica (noções).
NÃO repita (já existe): clínica geral endócrino/renal (medicina.clinica-endocrino-renal-gastro-infecto).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Cetoacidose diabética." | "Crise tireotóxica." | "Hiperplasia adrenal congênita." | "Hipoparatireoidismo." | "Insuficiência adrenal." | "Neuroendocrinologia." | "Endocrinologia da gestação." | "Hipotireoidismo." | "Hipotálamo e hipófise." | "Anatomia e fisiologia da tireoide." | "Hipertireoidismo e tireotoxicose." | "Tireoidites."
```

### 29. Fiscalização ambiental  (23 concursos, 81 tópicos)
```
Matéria 29. id: "meio-ambiente.fiscalizacao-ambiental". subject: "Meio Ambiente". Título: "Fiscalização ambiental".
Cubra: poder de polícia ambiental, infrações e sanções administrativas ambientais (o decreto federal que as regulamenta), instrumentos de fiscalização (auto de infração, advertência, embargo, apreensão, interdição), processo administrativo sancionador ambiental, dosimetria e valoração, vistoria e relatório de fiscalização, queimadas e incêndios florestais, crimes ambientais (relação com a lei de crimes).
NÃO repita (já existe): licenciamento e unidades de conservação (meio-ambiente.gestao-licenciamento-unidades-conservacao); direito ambiental.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei estadual nº 7.910/2025 (Fiscalização ambiental e recursos hídricos)." | "Vistoria e inspeção ambiental, incluindo planejamento de fiscalização, critérios de risco, técnicas básicas de vistoria em campo, coleta de dados, registros fotográficos, medições e entrevistas." | "Cálculo e dosimetria para mensuração e valoração de autos de infração." | "Decreto federal nº 6.514/2008 (infrações e sanções administrativas ao meio ambiente)." | "Dosimetria." | "Lei Estadual nº 7.910/2025 (Institui diretrizes para a fiscalização Ambiental e dos Recursos Hídricos)." | "Fundamentos de dosimetria e radiobiologia." | "Taxa de controle e fiscalização ambiental – TCFA (Lei nº 10.165/2000)." | "Monitoramento ambiental e análise de recursos naturais: técnicas de levantamento, avaliação, acompanhamento e conservação do solo e da água; monitoramento da seca, queimadas e degradação ambiental." | "Princípios de dosimetria clínica e ocupacional." | "Dosimetria pessoal, de área e ambiental." | "Taxa de Controle e Fiscalização Ambiental."
```

### 30. Psiquiatria da infância e adolescência e interconsulta  (22 concursos, 43 tópicos)
```
Matéria 30. id: "psiquiatria.infancia-adolescencia-interconsulta". subject: "Psiquiatria". Título: "Psiquiatria da infância e adolescência e interconsulta".
Cubra: transtornos do neurodesenvolvimento (TEA, TDAH, deficiência intelectual), transtornos de aprendizagem, ansiedade e depressão na infância, comportamento suicida em adolescentes, uso de substâncias na adolescência, psicofarmacologia pediátrica (noções), interconsulta psiquiátrica no hospital geral, delirium e psiquiatria de ligação.
NÃO repita (já existe): transtornos mentais do adulto (psiquiatria.transtornos-mentais-principais).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Interconsulta psiquiátrica." | "Transtorno do Espectro Autista (TEA)." | "Transtorno do Déficit de Atenção e Hiperatividade (TDAH)." | "Sistema Tratamento de Água para Hemodiálise- STDAH." | "Exame psiquiátrico e exames complementares em Psiquiatria da Infância e Adolescência." | "Classificação diagnóstica em Psiquiatria da Infância e da Adolescência." | "Questões Forenses em Psiquiatria Infantil." | "Prevenção em Psiquiatria da Infância e Adolescência." | "Modelos de reabilitação em Psiquiatria da Infância e Adolescência." | "Modelos de Atendimento em Psiquiatria da Infância e Adolescência em Saúde Pública." | "Transtorno do espectro autista e transtorno de déficit de atenção e hiperatividade: avaliação clínica e diagnóstico diferencial." | "Política Nacional de Proteção dos Direitos da Pessoa com Transtorno do Espectro Autista."
```

### 31. Farmácia hospitalar e farmácia clínica  (21 concursos, 45 tópicos)
```
Matéria 31. id: "farmacia.farmacia-hospitalar-clinica". subject: "Farmácia". Título: "Farmácia hospitalar e farmácia clínica".
Cubra: estrutura e funções da farmácia hospitalar, seleção e padronização (comissão de farmácia e terapêutica), sistemas de distribuição (coletivo, individualizado, dose unitária), farmacotécnica hospitalar e nutrição parenteral (preparo), centro de informação de medicamentos, farmácia clínica e cuidado farmacêutico, conciliação, interações e reações adversas, farmacovigilância e farmacoepidemiologia, gestão de estoque.
NÃO repita (já existe): assistência farmacêutica no SUS (farmacia.assistencia-farmaceutica-sus).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Farmácia clínica." | "Farmacovigilância." | "Farmácia hospitalar." | "Farmacoepidemiologia." | "Estrutura física e organizacional e funções da Farmácia Hospitalar." | "Serviços/centros de informação de medicamentos." | "Farmacovigilância e farmacoepidemiologia." | "Garantia de qualidade em farmácia hospitalar." | "Boas práticas de farmacovigilância para as Américas." | "RDC 44/2009 - Boas Práticas Farmacêuticas para o controle sanitário do funcionamento, da dispensação e da comercialização de produtos e da prestação de serviços farmacêuticos em farmácias e drogarias." | "Farmácia Hospitalar: Objetivos funções e estrutura básica." | "Atenção Farmacêutica e Farmácia Clínica: Conceitos, filosofia."
```

### 32. Resíduos sólidos: Política Nacional e gestão  (21 concursos, 42 tópicos)
```
Matéria 32. id: "meio-ambiente.residuos-solidos-pnrs". subject: "Meio Ambiente". Título: "Resíduos sólidos: Política Nacional e gestão".
Cubra: Política Nacional de Resíduos Sólidos (princípios, objetivos, instrumentos, responsabilidade compartilhada, logística reversa, planos de resíduos), classificação de resíduos, resíduos de serviços de saúde e da construção civil, coleta seletiva e catadores, reciclagem, compostagem, aterro sanitário e encerramento de lixões.
NÃO repita (já existe): saneamento geral (engenharia.saneamento-abastecimento-esgotamento).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Política Estadual de Resíduos Sólidos do Amazonas (Lei N. 4457/2017 e Decreto Nº 50890 DE 16/12/2024)." | "Gerenciamento dos Resíduos de Serviços de Saúde." | "Decreto Estadual nº 50.890/2024 (Regulamenta a exigência de sistemas de Logística Reversa)." | "Resíduos sólidos e contaminação de solos e águas subterrâneas: Qualidade do solo e da água subterrânea." | "RDC n°. 222, de 28 de março de 2018 que regulamenta as Boas Práticas de Gerenciamento dos Resíduos de Serviços de Saúde e dá outras providências." | "Fluidos refrigerantes e meio ambiente: Protocolo de Montreal/Kigali, GWP/ODP, refrigerantes alternativos, segurança (NBR 16069/ISO 5149), logística reversa de fluidos." | "Plano de Gerenciamento de Resíduos de Serviços de Saúde." | "Gerenciamento de resíduos de serviços de saúde: RDC nº 222, de 28 de março de 2018 que regulamenta as Boas Práticas de Gerenciamento dos Resíduos de Serviços de Saúde e dá outras providências." | "Uso de cores para o correto descarte de resíduos sólidos (cores e procedimentos para descarte)." | "Resíduos e Sustentabilidade: Uso de cores para o correto descarte de resíduos sólidos (cores e procedimentos para descarte)." | "Coleta, segregação e descarte de resíduos de serviços de saúde." | "Segregação e manejo de resíduos de serviços de saúde."
```

### 33. Legislação e ética farmacêutica  (20 concursos, 50 tópicos)
```
Matéria 33. id: "farmacia.legislacao-etica-farmaceutica". subject: "Farmácia". Título: "Legislação e ética farmacêutica".
Cubra: controle sanitário do comércio de medicamentos, a lei que define farmácia como estabelecimento de saúde, medicamentos sujeitos a controle especial (listas, receituários, escrituração), medicamentos genéricos e similares, responsabilidade técnica, Código de Ética Farmacêutica, atribuições do farmacêutico. Sem inventar números de artigos.
NÃO repita (já existe): toxicologia regulatória (farmacia.toxicologia-geral-regulatoria).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei Federal nº 5.991/1973 – Dispõe sobre o controle sanitário do comércio de drogas, medicamentos, insumos farmacêuticos e correlatos." | "Decreto Federal nº 74.170/1974 – Regulamenta a Lei Federal nº 5.991/1973." | "Código de Ética Farmacêutica." | "Medicamentos sujeitos a controle especial." | "Substâncias e medicamentos sujeitos a controle especial." | "Lei nº 5.991/1973." | "Lei nº 13.021/2014." | "Ética profissional e legislação do exercício farmacêutico: código de Ética Farmacêutica, responsabilidades técnicas do farmacêutico." | "Ética, Bioética e Código de Ética Farmacêutica." | "Lei nº 5.991, de 17 de dezembro de 1973." | "Medicamentos genéricos, similares e referência." | "Portaria nº 344/1998 - Regulamento técnico de substâncias sujeitas a controle especial."
```

### 34. Bioquímica clínica e urinálise  (20 concursos, 43 tópicos)
```
Matéria 34. id: "laboratorio.bioquimica-clinica-urinalise". subject: "Laboratório". Título: "Bioquímica clínica e urinálise".
Cubra: fase pré-analítica, glicemia e curva glicêmica, hemoglobina glicada, perfil lipídico, função renal (ureia, creatinina, clearance), função hepática e enzimas, eletrólitos e gasometria, proteínas, marcadores cardíacos, urinálise (exame físico, químico e sedimentoscopia), espectrofotometria e automação.
NÃO repita (já existe): patologia clínica geral (medicina.patologia-clinica-laboratorio).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Bioquímica oral." | "Bioquímica básica." | "Bioquímica." | "Bases genéticas e bioquímicas das doenças." | "Alterações físicas, químicas e bioquímicas durante o processamento e armazenamento." | "Bioquímica: água e tampões biológicos; estrutura e propriedades de aminoácidos, proteínas, enzimas, lipídios, carboidratos e membranas biológicas." | "Bioquímica Clínica: metabolismo, enzimas, proteínas, eletrólitos, função renal, função hepática, perfil lipídico, marcadores cardíacos e interpretação de exames laboratoriais." | "Avaliação Nutricional: métodos de avaliação do estado nutricional; antropometria; avaliação clínica, bioquímica e dietética; diagnóstico nutricional; triagem nutricional." | "Automação laboratorial em bioquímica clínica." | "Farmacologia endócrina e segurança do paciente: interações medicamentosas relevantes, ajuste por função renal e hepática, adesão terapêutica." | "Fundamentos de Urologia: Anatomia e fisiologia do sistema urinário e reprodutor masculino; Fisiopatologia das doenças urológicas; Função renal e urinária." | "Técnicas Bioquímicas: Espectrofotometria: princípios, aplicação em dosagem de substâncias, interpretação de curvas de calibração."
```

### 35. Otorrinolaringologia  (19 concursos, 145 tópicos)
```
Matéria 35. id: "medicina.otorrinolaringologia". subject: "Medicina". Título: "Otorrinolaringologia".
Cubra: anatomia e fisiologia do ouvido, nariz, seios paranasais, faringe e laringe, otites, perdas auditivas e zumbido, vertigem, rinites e rinossinusites, epistaxe, amigdalites e apneia do sono, doenças da laringe e disfonia, corpo estranho, tumores de cabeça e pescoço (noções), urgências otorrinolaringológicas.
NÃO repita (já existe): audiologia (fonoaudiologia.audiologia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Anomalias congênitas da laringe." | "Anestesia em otorrinolaringologia." | "Doenças ulcerogranulomatosas em otorrinolaringologia." | "Doenças benignas da laringe." | "Antibióticos, antifúngicos, antiviróticos, anti-inflamatórios, anti-histamínicos, descongestionantes nasais, corticosteroides e anestesias em otorrinolaringologia." | "Emergências em otorrinolaringologia." | "Doenças sistêmicas e sua inter-relação com alterações otorrinolaringológicas." | "Tonteira e Vertigem." | "Abordagem do paciente com zumbido e diagnósticos diferenciais." | "Anatomia dos Seios paranasais: técnicas e procedimentos." | "Raiva. Rickettsioses. Sinusites. Difteria." | "Semiologia, sintomatologia, diagnóstico e tratamento das principais afecções da laringe, glândulas salivares, órgão auditivo e seios paranasais."
```

### 36. Imuno-hematologia e banco de sangue  (19 concursos, 55 tópicos)
```
Matéria 36. id: "laboratorio.imuno-hematologia-banco-sangue". subject: "Laboratório". Título: "Imuno-hematologia e banco de sangue".
Cubra: sistemas ABO e Rh e outros grupos (Kell, Duffy, Kidd, MNS), tipagem direta e reversa, pesquisa e identificação de anticorpos irregulares, prova cruzada, Coombs direto e indireto, fenotipagem, triagem de doadores e sorologia, hemocomponentes e hemoderivados (preparo, armazenamento, validade), agência transfusional, reações transfusionais e hemovigilância.
NÃO repita (já existe): hematologia clínica e hemoterapia médica (medicina.hematologia-hemoterapia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Uso de hemoderivados e hemocomponentes." | "Anemias, transfusões de hemocomponentes, distúrbios de coagulação, policitemias, leucemias agudas e crônicas, síndromes paraneoplásicas, emergências oncológicas." | "Teste de Coombs." | "Prova cruzada." | "Hematologia: anemias; discrasias de células plasmáticas; distúrbios da coagulação; doenças linfoproliferativas; leucopenia; transfusão de hemoderivados." | "Uso clínico de hemocomponentes." | "Uso de hemoderivados." | "Sistema ABO e Rh." | "Outros sistemas de grupos sanguíneos (Rh, Kell, Duffy, Kidd, MNS)." | "Tipagem sanguínea direta e reversa." | "Pesquisa de anticorpos irregulares (PAI)." | "Prova de compatibilidade (Prova Cruzada)."
```

### 37. Obrigações acessórias e SPED  (18 concursos, 24 tópicos)
```
Matéria 37. id: "contabilidade-tributaria.obrigacoes-acessorias-sped". subject: "Contabilidade Tributária". Título: "Obrigações acessórias e SPED".
Cubra: SPED (ECD, ECF, EFD ICMS/IPI, EFD-Contribuições, EFD-Reinf, eSocial), DCTF e DCTFWeb, retenções na fonte (IR, CSRF, INSS) em pagamentos a pessoas jurídicas, nota fiscal eletrônica, escrituração fiscal, prazos e penalidades em linhas gerais. Explique os conceitos sem inventar números de instruções normativas.
NÃO repita (já existe): tributos federais (direito-tributario.tributos-federais-renda-pis-cofins).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Instrução Normativa SRF nº 459/04." | "Instrução Normativa SRF nº 971/09." | "Instrução Normativa SRF nº 480/04." | "Instrução Normativa SRF nº 480/2004." | "Escrituração das receitas e despesas. Obrigações acessórias. Selo Digital." | "Instrução Normativa RFB nº 1234/2012." | "Imposto de Renda Retido na Fonte como receita dos Estados e obrigações acessórias correspondentes." | "Instrução Normativa da Secretaria da Receita Federal nº 1.234/2012 e suas alterações." | "Instrução Normativa RFB nº 2237, de 04 de dezembro de 2024." | "Legislação básica e suas atualizações: Instrução Normativa RFB No 2110, de 17 de outubro de 2022." | "Instrução Normativa RFB nº 2.110/2022 e Instrução Normativa RFB nº 1.234/2012." | "Fiscalização de obrigações acessórias."
```

### 38. Farmacotécnica e farmácia magistral  (17 concursos, 38 tópicos)
```
Matéria 38. id: "farmacia.farmacotecnica-magistral". subject: "Farmácia". Título: "Farmacotécnica e farmácia magistral".
Cubra: formas farmacêuticas sólidas, líquidas, semissólidas e estéreis, operações farmacêuticas, cálculos farmacêuticos, excipientes e estabilidade, manipulação e boas práticas de manipulação, embalagem e acondicionamento, vias de administração, preparações estéreis.
NÃO repita (já existe): boas práticas de fabricação industrial (farmacia.boas-praticas-fabricacao-validacao).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Farmacotécnica." | "Manipulação de medicamentos." | "Formas farmacêuticas." | "Acondicionamento de formas farmacêuticas." | "Vias de administração; classificação das formas farmacêuticas." | "Pós, granulados e comprimidos." | "Tecnologia das formas farmacêuticas revestidas, líquidas, não estéreis e oriundas de vegetais." | "A Farmácia Farmacotécnica e Tecnologia Farmacêutica." | "Farmacotécnica e delineamento de formas farmacêuticas." | "Farmacotécnica: Formas Farmacêuticas, Diluição e Estabilidade, Fracionamento de formulações sólidas e líquidas." | "Farmacotécnica: formas farmacêuticas sólidas, semissólidas e líquidas de administração oral, parenteral, retal, tópica, oftálmica e vaginal." | "Manipulação de medicamentos e produção de antissépticos e desinfetantes."
```

### 39. Angiologia e cirurgia vascular  (16 concursos, 129 tópicos)
```
Matéria 39. id: "medicina.angiologia-cirurgia-vascular". subject: "Medicina". Título: "Angiologia e cirurgia vascular".
Cubra: doença arterial periférica, isquemia aguda, aneurismas da aorta e periféricos, doença carotídea, insuficiência venosa crônica e varizes, trombose venosa profunda e tromboembolismo, linfedema, pé diabético, acessos vasculares, trauma vascular.
NÃO repita (já existe): cardiologia clínica (medicina.clinica-cardiologia-pneumologia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Aneurismas." | "Aneurisma de aorta." | "Trombose venosa profunda." | "Linfedemas." | "Doença arterial periférica." | "Cirurgia dos aneurismas da aorta torácica." | "Varizes e Insuficiência venosa crônica." | "Pé Diabético." | "Varizes dos membros inferiores." | "Trombose venosa superficial." | "Diagnóstico ultrassonográfico da trombose venosa." | "Insuficiência venosa e mapeamento venoso."
```

### 40. Fisioterapia cardiorrespiratória e em terapia intensiva  (16 concursos, 43 tópicos)
```
Matéria 40. id: "fisioterapia.cardiorrespiratoria-terapia-intensiva". subject: "Fisioterapia". Título: "Fisioterapia cardiorrespiratória e em terapia intensiva".
Cubra: anatomia e fisiologia cardiorrespiratória do adulto, criança e neonato, avaliação e monitorização, técnicas de higiene brônquica e expansão pulmonar, oxigenoterapia e gases medicinais, ventilação mecânica invasiva e não invasiva (papel do fisioterapeuta, desmame), reabilitação pulmonar e cardíaca, fisioterapia em UTI adulto, pediátrica e neonatal.
NÃO repita (já existe): terapia intensiva médica (medicina.terapia-intensiva).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Urgências cardiorrespiratórias." | "Anatomia geral e dos órgãos e sistemas cardiorrespiratórios (adulto, pediátrico e neonatal)." | "Instrumentos de medida, avaliação e monitorização cardiorrespiratória (adulto, pediátrico e neonatal)." | "Suporte ventilatório invasivo e não invasivo (adulto, pediátrico e neonatal)." | "Gases medicinais." | "Anatomia do Sistema cardiorrespiratório: técnicas e procedimentos." | "Abordagem ventilatória do recém-nascido: oxigenioterapia, modalidades de suporte ventilatório não invasivo, estratégias de ventilação mecânica com foco na doença de base." | "Urgência e Emergência Pediátrica: Manejo de emergências respiratórias, choques, parada cardiorrespiratória, intoxicações, trauma, e distúrbios metabólicos agudos." | "Fisiologia do Exercício: Sistema Cardiorrespiratório." | "Doenças Cardiovasculares: Reabilitação Cardiovascular." | "Reabilitação Pulmonar." | "Principais indicações de fisioterapia respiratória Saúde Pública."
```

### 41. Técnica legislativa, redação de normas e linguagem simples  (16 concursos, 23 tópicos)
```
Matéria 41. id: "processo-legislativo.tecnica-legislativa-linguagem-simples". subject: "Processo Legislativo". Título: "Técnica legislativa, redação de normas e linguagem simples".
Cubra: estrutura das leis (parte preliminar, normativa e final), articulação (artigos, parágrafos, incisos, alíneas), regras de redação e alteração de leis, consolidação, redação de proposições, pareceres e emendas, linguagem simples (princípios e a política nacional), redação legislativa clara.
NÃO repita (já existe): redação oficial (manual de redação já existe em português).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Política Nacional de Linguagem Simples (Lei nº 15.263/2025)." | "Lei Estadual nº 18.246/2022 e diretrizes de linguagem simples no âmbito da Administração Pública." | "Lei Complementar estadual nº 176/2014 (elaboração de leis)." | "Lei nº 15.263, de 14 de novembro de 2025 (Política Nacional de Linguagem Simples)." | "Linguagem Simples." | "Consolidação das Leis do Trabalho – CLT." | "O emprego de linguagem simples e do direito visual (legal design e visual law) em prol do acesso à justiça." | "Consolidação das Leis do Trabalho aplicada à gestão organizacional." | "LEGISLAÇÃO e NORMAS APLICADAS À SEGURANÇA DO TRABALHO: Consolidação das Leis do Trabalho (CLT)." | "Regime de emprego público e disposições da Consolidação das Leis do Trabalho (CLT) aplicáveis." | "Linguagem simples, Visual Law e Legal Design na comunicação dos atos e decisões judiciais, com ênfase no dever de clareza, acessibilidade, transparência e compreensão da decisão judicial."
```

### 42. Terapia nutricional enteral e parenteral  (15 concursos, 23 tópicos)
```
Matéria 42. id: "nutricao.terapia-nutricional-enteral-parenteral". subject: "Nutrição". Título: "Terapia nutricional enteral e parenteral".
Cubra: indicações e contraindicações, triagem e avaliação nutricional do paciente hospitalizado, vias de acesso, fórmulas enterais, cálculo de necessidades, nutrição parenteral (composição, preparo, administração), complicações, equipe multiprofissional de terapia nutricional, boas práticas e regulamentos técnicos (sem inventar números).
NÃO repita (já existe): nutrição geral e dietoterapia (nutricao.dietoterapia-avaliacao-politicas).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Terapia nutricional enteral e parenteral." | "Nutrição parenteral." | "Misturas intravenosas, nutrição parenteral e manipulação de citostáticos." | "Nutrição Clínica: Terapia nutricional oral, enteral e parenteral nesses distúrbios." | "Nutrição enteral." | "Portaria nº 272/1998 (Terapêutica de Nutrição Parenteral)." | "Nutrição Clínica: dietoterapia; terapia nutricional oral, enteral e parenteral; prescrição dietética; acompanhamento nutricional." | "Terapia nutricional nas diversas patologias." | "Banco de Leite Humano (BLH), aleitamento materno e terapia nutricional neonatal e pediátrica." | "Terapia nutricional enteral e parenteral em crianças, adultos e idosos: indicação, contraindicação, características, prescrição, monitorização, complicações." | "Terapia Nutricional Enteral." | "Regulamento Técnico para a Terapia de Nutrição Enteral (Resolução - RDC nº 63, de 6 de julho de 2000)."
```

### 43. Educação especial e inclusiva  (14 concursos, 22 tópicos)
```
Matéria 43. id: "educacao.educacao-especial-inclusiva". subject: "Educação". Título: "Educação especial e inclusiva".
Cubra: marcos legais e a política nacional de educação especial na perspectiva inclusiva, atendimento educacional especializado (AEE) e sala de recursos, deficiências (intelectual, visual, auditiva, física), TEA, altas habilidades, Libras e Braille (noções), adaptações curriculares e tecnologia assistiva, avaliação e plano de AEE.
NÃO repita (já existe): diversidade, inclusão e temas transversais (educacao.diversidade-inclusao-temas-transversais).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Deficiência intelectual." | "Atendimento educacional especializado." | "Deficiência intelectual e desenvolvimento da aprendizagem." | "Sistema Braille e recursos de leitura tátil." | "A atuação do terapeuta ocupacional no campo da Educação Especial no Brasil." | "Investigação de atraso do desenvolvimento, regressão de habilidades, deficiência intelectual e alterações motoras." | "Ética e Legislação Profissional: ética docente; direitos da pessoa surda; responsabilidade profissional; atendimento educacional especializado." | "Musicoterapia em Contextos Específicos: Hospitais; CAPS (saúde mental); Instituições de longa permanência; Educação especial." | "Público-Alvo da Educação Especial: Identificação e caracterização do público-alvo da Educação Especial." | "Diretrizes Operacionais para o Atendimento Educacional Especializado na Educação Básica, modalidade Educação Especial." | "Política Nacional de Educação Especial na Perspectiva da Educação Inclusiva." | "Altas habilidades/superdotação."
```

### 44. Oftalmologia  (13 concursos, 100 tópicos)
```
Matéria 44. id: "medicina.oftalmologia". subject: "Medicina". Título: "Oftalmologia".
Cubra: anatomia e fisiologia ocular, erros de refração, olho vermelho (conjuntivites, ceratites, uveítes), glaucoma, catarata, retinopatia diabética e hipertensiva, descolamento de retina, urgências e trauma ocular, estrabismo e ambliopia, triagem visual na infância, manifestações oculares de doenças sistêmicas.
NÃO repita (já existe): nenhuma.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Anestesia em oftalmologia." | "Glaucoma: classificação, quadro clínico, diagnóstico e tratamento clínico e cirúrgico." | "Principais Doenças Oftalmológicas." | "Oftalmologia preventiva." | "Patologia, diagnóstico e tratamento das doenças do(a): órbita, conjuntiva, esclera, úvea, retina, vítreo, cristalino e aparelho lacrimal." | "Retina e Vítreo: Doenças vasculares da retina, Degenerações da mácula, Distrofias, Degenerações periféricas da retina, Descolamentos da retina." | "Urgências em oftalmologia: clínicas e cirúrgicas." | "Refração: noções de óptica oftálmica: Vícios de refração; Prescrição de óculos e lentes de contato." | "Infecções no período neonatal. Sepse precoce e tardia. Meningites, pneumonias, conjuntivite, onfalite, osteomielite, artrite, infecção do trato urinário, infecções fúngicas e virais." | "Refração: noções de óptica oftálmica; Vícios de refração." | "Tratamento cirúrgico dos vícios de refração." | "Problemas oftalmológicos mais comuns na infância: Prevenção da ambliopia, conjuntivites, tracoma e afecções das vias lacrimais. Retinoblastoma."
```

### 45. Cerimonial, protocolo e assessoria parlamentar  (13 concursos, 22 tópicos)
```
Matéria 45. id: "processo-legislativo.cerimonial-protocolo". subject: "Processo Legislativo". Título: "Cerimonial, protocolo e assessoria parlamentar".
Cubra: conceitos de cerimonial, protocolo e etiqueta, ordem de precedência oficial, símbolos nacionais em solenidades, organização de eventos oficiais, sessões solenes, assessoria parlamentar e de gabinete, comunicação com a imprensa e media training, redação de discursos e pronunciamentos.
NÃO repita (já existe): regimentos (processo-legislativo.regimento-camara-congresso).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Media training." | "Etiqueta no trabalho." | "Etiqueta: indumentária e preparação de mesa." | "Práticas e comportamentos higiênicos, de conduta, apresentação pessoal e etiqueta." | "SEGURANÇA EM INSTRUMENTAÇÃO INDUSTRIAL: Riscos ocupacionais: elétricos; mecânicos; químicos. Procedimentos de segurança: bloqueio e etiquetagem (LOTO); permissões de trabalho." | "Conceitos de cerimonial, protocolo e etiqueta; evolução histórica do cerimonial." | "Etiqueta social e profissional; comunicação interpessoal e oratória; relações públicas e comunicação institucional." | "Ética digital e netiqueta. Cyberbullying e crimes digitais. Fake news e verificação de informações." | "Ética digital e netiqueta. Cyberbullying e crimes digitais. Fake news e checagem de fatos (fact-checking)."
```

### 46. Gestão, liderança e educação permanente em enfermagem  (12 concursos, 38 tópicos)
```
Matéria 46. id: "enfermagem.gestao-lideranca-educacao-permanente". subject: "Enfermagem". Título: "Gestão, liderança e educação permanente em enfermagem".
Cubra: teorias administrativas aplicadas à enfermagem, liderança e tomada de decisão, dimensionamento de pessoal, escalas, gestão de materiais e custos, indicadores e qualidade da assistência, gestão de conflitos e relações humanas no trabalho, educação permanente e continuada em saúde (política nacional).
NÃO repita (já existe): legislação e ética de enfermagem (enfermagem.legislacao-etica-gestao).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Liderança em Enfermagem." | "Educação Permanente." | "Gestão clínica, monitoramento e educação permanente." | "Relações Humanas no trabalho." | "Administração em Enfermagem de Saúde Pública." | "Metodologias de Educação Permanente." | "Educação em Saúde: promoção da saúde; prevenção de doenças; educação permanente; ações coletivas; visitas domiciliares; grupos educativos; práticas integrativas e complementares em saúde (PICS)." | "Educação em Saúde: promoção da saúde; prevenção de doenças; educação permanente; ações coletivas; visitas domiciliares; grupos educativos." | "Trabalho em Equipe Multiprofissional: Integração com equipes de saúde; Encaminhamento de pacientes; Acompanhamento longitudinal; Educação permanente." | "Trabalho em Equipe e Capacitação: Atuação multiprofissional; Integração com equipes de saúde; Treinamento e capacitação de pessoal; Educação permanente." | "Educação Permanente em Saúde: Capacitação e treinamento de equipes; Educação em serviço; Atualização profissional; Desenvolvimento de competências." | "Educação permanente em saúde; Orientação de profissionais e pacientes."
```

### 47. Processo de enfermagem e sistematização da assistência (SAE)  (12 concursos, 14 tópicos)
```
Matéria 47. id: "enfermagem.processo-enfermagem-sae". subject: "Enfermagem". Título: "Processo de enfermagem e sistematização da assistência (SAE)".
Cubra: etapas do processo de enfermagem (histórico, diagnóstico, planejamento, implementação, avaliação), classificações de diagnósticos, intervenções e resultados (NANDA-I, NIC, NOC, CIPE), registros e anotações de enfermagem, teorias de enfermagem (Horta, Orem, King, Roy), exemplos aplicados.
NÃO repita (já existe): legislação e ética de enfermagem (enfermagem.legislacao-etica-gestao).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Resolução CONANDA nº 245/2024." | "Resolução Conanda nº 169/2014." | "Normas e resoluções do CONANDA indicadas no edital." | "Resolução CONANDA nº 231/2022." | "Resolução CONANDA nº 252/2024." | "Orientação ao munícipe." | "Metaparadigmas e teorias de enfermagem." | "Feridas e Curativos na Atenção Ambulatorial: avaliação e classificação das feridas; controle de infecção; prevenção e tratamento; sistematização da Assistência de Enfermagem."
```

### 48. Enfermagem em saúde mental e psiquiatria  (9 concursos, 9 tópicos)
```
Matéria 48. id: "enfermagem.saude-mental". subject: "Enfermagem". Título: "Enfermagem em saúde mental e psiquiatria".
Cubra: relacionamento terapêutico e comunicação, instrumentos de intervenção de enfermagem em saúde mental, cuidado nos principais transtornos mentais, urgência psiquiátrica e contenção, uso de psicofármacos (cuidados de enfermagem), RAPS e reforma psiquiátrica (aplicadas à enfermagem), álcool e outras drogas.
NÃO repita (já existe): saúde mental e RAPS (saude-publica.saude-mental-raps-internacao).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Instrumentos de intervenção de enfermagem em saúde mental." | "Assistência de enfermagem em saúde mental." | "Instrumentos de intervenção de enfermagem em saúde mental: relacionamento interpessoal, comunicação terapêutica, psicopatologias, psicofarmacologia." | "O contexto da evolução da enfermagem psiquiátrica." | "Instrumentos de intervenção de enfermagem em saúde mental: relacionamento interpessoal, comunicação terapêutica." | "Processo do cuidar em enfermagem em saúde mental e psiquiatria." | "Fundamentos da enfermagem psiquiátrica."
```

### 49. Receita de contratos com clientes (CPC 47) e instrumentos financeiros (CPC 48)  (6 concursos, 11 tópicos)
```
Matéria 49. id: "contabilidade.receita-contratos-instrumentos-financeiros". subject: "Contabilidade". Título: "Receita de contratos com clientes (CPC 47) e instrumentos financeiros (CPC 48)".
Cubra: modelo de cinco etapas do reconhecimento de receita, obrigações de desempenho, preço da transação, ativos e passivos de contrato, custos de contrato; classificação e mensuração de ativos financeiros, perda esperada, hedge (noções), apresentação e divulgação.
NÃO repita (já existe): estrutura conceitual e CPCs específicos (contabilidade.estrutura-conceitual-cpc; contabilidade.arrendamento-subvencoes-cpc-especificos).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Instrumentos financeiros: definições, avaliação e tratamento contábil." | "Reconhecimento, mensuração, apresentação e evidenciação de instrumentos financeiros." | "NBC TSP 30 – Instrumentos financeiros: Apresentação." | "NBC TSP 31 – Instrumentos financeiros: Reconhecimento e mensuração." | "NBC TSP 32 – Instrumentos financeiros: Reconhecimento e mensuração (Contabilidade de Hedge – Aplicação Residual)." | "NBC TSP 33 – Instrumentos financeiros: Divulgações." | "Receitas de contratos com clientes." | "Ativos de contrato, ativos financeiros de contrato e ativos intangíveis vinculados aos contratos de concessão e à prestação dos serviços de abastecimento de água e esgotamento sanitário." | "Instrumentos financeiros: aspectos conceituais, reconhecimento, mensuração e evidenciação. Recuperabilidade de instrumentos financeiros. Contabilidade de hedge." | "Instrumentos Financeiros."
```

### 50. Química geral  (6 concursos, 8 tópicos)
```
Matéria 50. id: "quimica.quimica-geral". subject: "Química". Título: "Química geral".
Cubra: estrutura atômica e tabela periódica, ligações químicas, funções inorgânicas, reações e estequiometria, soluções e concentrações, equilíbrio químico e pH, termoquímica, cinética, eletroquímica, química orgânica básica (funções e nomenclatura).
NÃO repita (já existe): análise instrumental e volumetria (já existem 2 matérias de química).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Tecnologias associadas à química orgânica: petroquímica, polímeros sintéticos, aditivos em alimentos, agroquímica, drogas, medicamentos e biotecnologia." | "Química Farmacêutica: Estrutura química de fármacos; Relação estrutura-atividade; Propriedades físico-químicas; Reações químicas aplicadas a fármacos." | "Química geral, orgânica, analítica e físico-química."
```
