# Pedidos para o Gemini: mais 50 matérias novas (19/10/2026, segunda leva)

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

### 1. Infectologia clínica e antimicrobianos  (120 concursos, 630 tópicos)
```
Matéria 1. id: "medicina.infectologia". subject: "Medicina". Título: "Infectologia clínica e antimicrobianos".
Cubra: antimicrobianos (classes, espectro, uso racional, resistência), infecções relacionadas à assistência, sepse, febre de origem indeterminada, infecções em imunossuprimidos, endocardite, meningites, infecções osteoarticulares e de pele, profilaxias.
NÃO repita (já existe): doenças infecciosas e parasitárias (medicina.doencas-infecciosas-parasitarias).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Educação e saúde: ensino e cidadania como travessia de fronteiras." | "Lei Estadual nº 7233/2002 (Dá nova redação ao plano de carreiras e vencimentos dos servidores administrativos do Ministério Público)." | "Lei Federal nº 12.772/2012 (Plano de Carreiras e Cargos de Magistério Federal)." | "Capítulo III – Do Desenvolvimento nas Carreiras do Plano de Carreiras e Cargos de Magistério Federal: Seção II – Da Carreira de Magistério do Ensino Básico, Técnico e Tecnológico: arts. 14 e 15-A." | "Capítulo IV – Da Remuneração do Plano de Carreiras e Cargos de Magistério Federal: arts. 16 a 19." | "Capítulo V – Do Regime de Trabalho do Plano de Carreiras e Cargos de Magistério Federal: arts. 20 a 22." | "Lei estadual nº 10.803/2024 (Novo Plano de Cargos, Carreiras e Remunerações)." | "Lei nº 6.868/2024 (Plano de Cargos e Carreiras do IPAAM)." | "Lei n. 12.772/2012 - Dispõe sobre a estruturação do Plano de Carreiras e Cargos de Magistério Federal, [...] e dá outras providências." | "Lei nº 14.043/2007 e alterações (plano de cargos, carreiras e vencimentos dos servidores do Ministério Público do Estado do Ceará)." | "Antibioticoterapia." | "Plano de Cargos e Carreiras do Magistério."
```

### 2. Publicidade, marketing e redes sociais  (41 concursos, 122 tópicos)
```
Matéria 2. id: "comunicacao.marketing-publicidade". subject: "Comunicação". Título: "Publicidade, marketing e redes sociais".
Cubra: planejamento de comunicação, publicidade e propaganda, marketing digital, gestão de redes sociais, métricas, branding, comunicação de crise.
NÃO repita (já existe): comunicação digital; jornalismo.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Publicidade." | "Licitação para contratação de serviços de publicidade (Lei nº 12.232/2010)." | "Marketing aplicado a arquivos." | "Marketing público." | "Publicidade institucional em ano eleitoral." | "Despesas com publicidade de órgãos públicos em ano eleitoral." | "Contratação de serviços de publicidade prestados por intermédio de agências de propaganda." | "Promoção, divulgação institucional e marketing de destinos e atividades culturais." | "Fundamentos da publicidade e propaganda." | "Identidade Visual e Branding Institucional." | "Conceitos de identidade visual, marca e branding." | "Fundamentos de marketing, marketing institucional, marketing social, marketing público."
```

### 3. Informática em saúde  (26 concursos, 119 tópicos)
```
Matéria 3. id: "informatica.sistemas-saude-ti". subject: "Informática". Título: "Informática em saúde".
Cubra: prontuário eletrônico, sistemas de informação do SUS (e-SUS, SINAN, SIM, SINASC), interoperabilidade, telessaúde, proteção de dados em saúde.
NÃO repita (já existe): telessaúde e PCDT.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Sistemas de Informação em Saúde: conceitos, finalidade e utilização." | "Sistemas de Informação em Saúde: conceitos gerais, informatização dos serviços de saúde, prontuário eletrônico e proteção de dados pessoais." | "Prontuário eletrônico." | "Sistemas de Informação em Saúde." | "Sistemas de informação em saúde e utilização de dados para planejamento e avaliação de ações e serviços de saúde." | "Sistemas de informação em saúde: SISAB, SIH/SUS, SIA/SUS e e‐Gestor." | "Vigilância e controle de populações de animais sinantrópicos." | "Prontuário Eletrônico do Cidadão (PEC)." | "Sistemas de informação em saúde na atenção primária." | "Animais sinantrópicos, silvestres e domésticos: conceitos, características, riscos à saúde humana e estratégias de controle populacional." | "Informática aplicada à saúde: sistemas de informação em saúde e registros eletrônicos." | "O contexto histórico da atividade de Jesus de Nazaré."
```

### 4. Psicologia organizacional e do trabalho  (26 concursos, 61 tópicos)
```
Matéria 4. id: "psicologia.organizacional-trabalho". subject: "Psicologia". Título: "Psicologia organizacional e do trabalho".
Cubra: recrutamento e seleção, avaliação de desempenho, treinamento e desenvolvimento, clima e cultura, qualidade de vida no trabalho, saúde mental do trabalhador, ergonomia cognitiva.
NÃO repita (já existe): trabalho, saúde mental e assédio.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Decreto nº 7.133/2010 (avaliação de desempenho institucional e individual)." | "Avaliação de desempenho ambiental (NBR ISO 14031:2015)." | "Lei estadual nº 16.681: política de bem-estar, saúde e qualidade de vida no trabalho e valorização dos profissionais da educação." | "Avaliação de desempenho ambiental segundo a norma ABNT NBR ISO 14.031:2015." | "Sistema de Avaliação de desempenho." | "Bem-estar, saúde e qualidade de vida no trabalho." | "Psicologia organizacional e do trabalho." | "Gestão Ambiental e de Saúde e Segurança Ocupacional: Avaliação de desempenho Ambiental segundo a norma ABNT NBR ISO 14.031:2015." | "Avaliação de desempenho ambiental." | "Recrutamento e seleção. Concurso Público." | "Enfermagem e recursos humanos - recrutamento e seleção." | "Avaliação de Desempenho por Competências."
```

### 5. Solos, culturas e mecanização agrícola  (24 concursos, 85 tópicos)
```
Matéria 5. id: "agropecuaria.culturas-solos-mecanizacao". subject: "Agropecuária". Título: "Solos, culturas e mecanização agrícola".
Cubra: fertilidade e manejo do solo, adubação e calagem, principais culturas (grãos, olericultura, fruticultura), irrigação, mecanização agrícola, extensão rural.
NÃO repita (já existe): proteção vegetal e fitotecnia.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Boas práticas agropecuárias, defesa sanitária, zoonoses, saúde única, bem-estar animal e extensão rural." | "Mecanização agrícola." | "Extensão rural." | "Comunicação e extensão rural." | "Fertilidade do solo." | "Recomendação de adubação." | "Adubação verde." | "Olericultura." | "Fitotecnia: Olericultura; culturas anuais (milho e feijão); culturas perenes (café); cultura da mandioca." | "Solos. Física. Química. Biologia. Fertilidade. Capacidade de uso. Manejo e conservação. Gessagem. Fosfatagem. Calagem. Adubação. Inoculantes." | "Máquinas, implementos agrícolas e mecanização agrícola." | "Extensão rural e assistência técnica."
```

### 6. Ensino de Arte (artes visuais, música, teatro, dança)  (23 concursos, 95 tópicos)
```
Matéria 6. id: "educacao.ensino-arte-musica". subject: "Educação". Título: "Ensino de Arte (artes visuais, música, teatro, dança)".
Cubra: BNCC de arte, linguagens artísticas, história da arte (noções), abordagem triangular, música na escola, teatro e dança na educação.
NÃO repita (já existe): nenhuma.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Ensino de arte no currículo escolar." | "Aspectos históricos do ensino de Arte no Brasil." | "A legislação brasileira referente ao ensino de Arte na educação básica." | "História da arte brasileira." | "Interculturalidade: a questão da diversidade cultural no ensino de Arte." | "Concepções de ensino de Arte no Brasil." | "Abordagem Triangular (Ana Mae Barbosa)." | "História da Arte: da arte pré-histórica à arte contemporânea; principais movimentos artísticos (Renascimento, Barroco, Neoclassicismo, Romantismo, Realismo, Impressionismo e vanguardas do século XX)." | "Fundamentos e tendências pedagógicas do ensino de arte no Brasil." | "Artes visuais no Brasil: do barroco colonial brasileiro aos dias atuais." | "Linguagens artísticas: Artes visuais, música, teatro, dança, expressão corporal." | "A História da Arte Geral."
```

### 7. Cardiologia clínica  (23 concursos, 90 tópicos)
```
Matéria 7. id: "medicina.cardiologia-clinica". subject: "Medicina". Título: "Cardiologia clínica".
Cubra: hipertensão arterial, insuficiência cardíaca, doença arterial coronariana e síndromes coronarianas agudas, arritmias, valvopatias, miocardiopatias, pericardite, endocardite, ECG, ecocardiograma e testes funcionais, prevenção cardiovascular e dislipidemias.
NÃO repita (já existe): clínica cardiologia/pneumologia geral (noções); urgências.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Tratamento cirúrgico das arritmias." | "Tratamento cirúrgico das complicações do infarto do miocárdio." | "Tratamento cirúrgico das arritmias por estimulação com marca-passo cardíaco artificial." | "ECG – alterações básicas." | "Eletrocardiograma." | "Dislipidemias." | "Princípios de biologia molecular e de genética aplicados à Cardiologia." | "Dispositivos terapêuticos em cardiologia: marcapassos, desfibriladores e ressincronizadores." | "Endocardite. Pericardite." | "Tamponamento cardíaco/pericardite aguda." | "Arritmias cardíacas, Insuficiência coronariana aguda, distúrbio de condução AV agudo." | "Valvopatias adquiridas."
```

### 8. Geologia geral e de engenharia  (23 concursos, 58 tópicos)
```
Matéria 8. id: "engenharia.geologia-mineracao". subject: "Engenharia". Título: "Geologia geral e de engenharia".
Cubra: minerais e rochas, processos geológicos, geologia estrutural, estratigrafia, hidrogeologia, geologia de engenharia e riscos geológicos, mapas geológicos.
NÃO repita (já existe): geotecnia e solos.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Noções de hidrogeologia." | "Hidrogeologia." | "Estruturas ígneas e relações de campo; Texturas primárias e secundárias. Paragêneses minerais. Estudo das principais texturas das rochas ígneas e dos tipos característicos." | "Geologia histórica. Estratigrafia." | "Geologia estrutural." | "Hidrogeologia. Contaminação de aquíferos. Contaminação de solos." | "Pesquisas, planejamento e controle de serviços de geologia, geoquímica e geofísica, hidrogeologia, petrologia mineralogia, geologia estrutural e tectônica, estratigrafia e geomorfologia." | "Lei da Política Nacional de Qualidade do Ar (Lei 14.850/2024). Lei da Política Nacional de Minerais Críticos e Estratégicos (Lei 15.506/2026)." | "Bromatologia: aditivos alimentares, condimentos, pigmentos, estudo químico-bromatológico dos alimentos (proteínas, lipídios e carboidratos), vitaminas, minerais, bebidas." | "Toxicantes: químicos, minerais e plantas tóxicas." | "Geologia do petróleo, bacias sedimentares, pré-sal, geração, migração e reservatórios." | "Geologia e recursos minerais e energéticos de Mato Grosso do Sul."
```

### 9. Vigilância ambiental e controle de vetores  (22 concursos, 30 tópicos)
```
Matéria 9. id: "saude-publica.vigilancia-ambiental-zoonoses". subject: "Saúde Pública". Título: "Vigilância ambiental e controle de vetores".
Cubra: vigilância ambiental em saúde, controle de vetores (Aedes, flebotomíneos, triatomíneos), roedores e animais peçonhentos, agente de combate às endemias, qualidade da água para consumo.
NÃO repita (já existe): epidemiologia e vigilância.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Controle de vetores." | "Acidentes com animais peçonhentos – suporte básico de vida/suporte avançado de vida." | "Acidentes com animais peçonhentos, reconhecimento e seu manejo." | "Animais peçonhentos: medidas de controle para escorpiões." | "Animais peçonhentos: medidas de controle para escorpiões e ofídios." | "Toxicologia: conduta geral nas intoxicações; acidentes por animais peçonhentos." | "Animais peçonhentos e venenosos: identificação, riscos, prevenção de acidentes e medidas de controle." | "Biologia, epidemiologia e características dos acidentes por animais peçonhentos no Brasil." | "Acidentes por animais peçonhentos: ofídicos, artrópodes peçonhentos (escorpionismo, araneísmo, lepidopterismo, himenopterismo) e animais aquáticos (cnidários, equinodermos, peixes peçonhentos)." | "Atenção ao Diagnóstico e Tratamento de Acidentes por Animais Peçonhentos Atenção ao Acidente Vascular Cerebral (AVC)." | "Animais Peçonhentos e Intoxicações Exógenas." | "Controle de vetores e roedores."
```

### 10. Patologia (anatomia patológica)  (19 concursos, 29 tópicos)
```
Matéria 10. id: "medicina.patologia-medicina-laboratorial". subject: "Medicina". Título: "Patologia (anatomia patológica)".
Cubra: processos patológicos gerais (lesão celular, inflamação, reparo, neoplasias), técnicas histológicas e imuno-histoquímica, citopatologia, patologia por sistemas, necropsia clínica.
NÃO repita (já existe): patologia clínica de laboratório.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Patologia geral." | "CAPS, eMulti e unidades básicas; transtornos mentais comuns e graves; manejo em situações de crise; psicopatologia geral; depressão, ansiedade, suicídio e uso abusivo de substâncias." | "Patologia geral: Degeneração e morte celular; Alterações do crescimento e desenvolvimento; Inflamação e reparação; Alterações Vasculares; Imunidade; Neoplasia; Noções de genética." | "Mecanismos imunológicos em processos patológicos." | "PARTE 2: Patologia geral." | "Recepção e tratamento de materiais no laboratório de anatomia patológica." | "Raciocínio em patologia: etiologia, patogênese, anatomia patológica e fisiopatologia." | "Processos patológicos." | "Patologia clínica e anatomia patológica veterinária." | "Noções de patologia. Anatomopatologia, citopatologia. Conceitos fundamentais. Patologia geral das lesões. Patologia dos fenômenos cadavéricos. Anatomia patológica geral, sistêmica e forense." | "Anatomia patológica e diagnóstico." | "Histopatologia das doenças de pele."
```

### 11. Zootecnia e produção animal  (18 concursos, 68 tópicos)
```
Matéria 11. id: "agropecuaria.zootecnia-producao-animal". subject: "Agropecuária". Título: "Zootecnia e produção animal".
Cubra: bovinocultura de leite e de corte, suinocultura, avicultura, ovinocaprinocultura, piscicultura, nutrição e alimentação animal, forragicultura, reprodução e sanidade.
NÃO repita (já existe): medicina veterinária (clínica).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Bovinocultura (Corte e Leite) e Forragicultura: Sistemas de criação, manejo nutricional, reprodutivo e sanitário, alimentos, alimentação e formulação de rações." | "Pequenos Animais: Sistemas de produção para avicultura semi-intensiva de corte e postura, caprinos, ovinos e piscicultura e reprodução associada." | "Ovinocultura e caprinocultura." | "Avicultura de corte e postura." | "Avicultura." | "Suinocultura." | "Piscicultura." | "Instalações para suínos, aves, bovinos de corte e leite." | "Zootecnia: Pastagens para bovinos." | "Pecuária: suinocultura, avicultura, ovinocultura, bovinocultura de corte e aquicultura (equipamentos, principais doenças, principais aspectos do manejo reprodutivo e ciclo de produção)." | "Avicultura: avicultura de corte, postura, industrial e caipira – instalações, alimentação, nutrição, sanidade e genética." | "Piscicultura: noções básicas da criação de peixes em açudes e tanques rede; alimentação, sanidade e manejo."
```

### 12. Ensino de Matemática (didática)  (18 concursos, 38 tópicos)
```
Matéria 12. id: "educacao.ensino-matematica". subject: "Educação". Título: "Ensino de Matemática (didática)".
Cubra: BNCC de matemática, resolução de problemas, educação matemática crítica, etnomatemática, modelagem, jogos e materiais concretos, avaliação em matemática.
NÃO repita (já existe): matemática (conteúdos).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Educação matemática." | "Avaliação e educação matemática. Formas e instrumentos." | "Ensino de matemática: conhecimento científico, abordagens metodológicas e recursos didáticos." | "Ensino de Matemática." | "Fundamentos da Educação Matemática: pressupostos conceituais; princípios pedagógicos; teorias da aprendizagem matemática; avaliação no processo de aprendizagem." | "Competências e habilidades no ensino de Matemática." | "Metodologias do Ensino de Matemática na Educação Profissional e Tecnológica: Planejamento curricular na EPT." | "Uso de tecnologias digitais no ensino de Matemática." | "Educação Matemática: investigação matemática; uso de jogos, materiais manipuláveis e tecnologias digitais no ensino da matemática." | "Educação Matemática nos Anos Iniciais: construção do conceito de número; resolução de problemas; raciocínio lógico; jogos matemáticos; materiais manipuláveis; letramento matemático." | "Metodologia do ensino de Matemática: uso de material concreto, calculadora e computador." | "Tendências em Educação Matemática: resolução de problemas, modelagem matemática, investigação matemática, Etnomatemática e Matemática Crítica."
```

### 13. Radiologia e diagnóstico por imagem (médico)  (17 concursos, 238 tópicos)
```
Matéria 13. id: "medicina.radiologia-diagnostico-imagem". subject: "Medicina". Título: "Radiologia e diagnóstico por imagem (médico)".
Cubra: interpretação de radiografias, TC, RM e ultrassonografia por sistema (tórax, abdome, neuro, musculoesquelético, mama), radiologia pediátrica, contrastes e reações, radiologia intervencionista, laudos.
NÃO repita (já existe): técnicas radiológicas para técnicos (radiologia.tecnicas-radiologicas-radioprotecao).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Radiologia intervencionista." | "Ultrassonografia do abdome total, do tórax, do pescoço, obstétrica e de partes moles." | "Bases físicas da ultrassonografia." | "Neuroradiologia do sistema nervoso central e periférico, crânio, conteúdo orbitário e coluna vertebral." | "Ultrassonografia intervencionista." | "Fundamentos da Ultrassonografia Diagnóstica." | "Transdutores, equipamentos e instrumentação em ultrassonografia." | "Artefatos em ultrassonografia." | "Ultrassonografia do fígado." | "Ultrassonografia das vias biliares." | "Ultrassonografia da vesícula biliar." | "Ultrassonografia do pâncreas."
```

### 14. Atendimento ao público e comunicação organizacional  (17 concursos, 45 tópicos)
```
Matéria 14. id: "administracao.atendimento-qualidade". subject: "Administração Geral". Título: "Atendimento ao público e comunicação organizacional".
Cubra: qualidade no atendimento, atendimento presencial e telefônico, comunicação interpessoal, gestão de reclamações, ouvidoria, Código de Defesa do Usuário do Serviço Público.
NÃO repita (já existe): atendimento ao público (geral).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Atividades de ouvidoria e corregedoria." | "Ouvidoria e proteção das pessoas: distinção entre receber um relato, apurar fatos e decidir sobre responsabilidade." | "Acessibilidade, inclusão e atendimento ao público." | "Interação institucional: atendimento a auditorias, ouvidorias, solicitações judiciais e órgãos competentes." | "A História da ouvidoria no Brasil." | "Gestão do processo na ouvidoria." | "Tratativa das demandas na ouvidoria." | "Guia de Orientação para a Gestão de Ouvidorias." | "Direito público aplicado às respostas de controle: integração de manifestações de ouvidoria, dados de transparência, achados de auditoria e informações correcionais." | "Ouvidoria externa, corregedoria-geral e processo de promoção na carreira da Defensoria Pública de Pernambuco." | "Modelo de Ouvidoria Externa. Ouvidoria-Geral da Defensoria Pública de São Paulo." | "Ética, sigilo, postura profissional, trabalho em equipe e atendimento ao público interno e externo."
```

### 15. Engenharia de tráfego e transportes  (17 concursos, 20 tópicos)
```
Matéria 15. id: "engenharia.engenharia-trafego-transportes". subject: "Engenharia". Título: "Engenharia de tráfego e transportes".
Cubra: estudos de tráfego, capacidade viária, semáforos, sinalização, transporte público, planejamento de transportes, mobilidade urbana.
NÃO repita (já existe): obras rodoviárias; concessões.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Sistema de Mobilidade Urbana." | "Política Nacional de Mobilidade Urbana (Lei n.º 12.587/12)." | "Política Nacional de Mobilidade Urbana (Lei nº 12.587/2012);" | "O direito ao transporte público como expressão do direito à cidade." | "Política Nacional de Mobilidade Urbana." | "Transporte público e transporte coletivo: noções gerais." | "Sinalização viária e dispositivos de segurança em rodovias." | "Política Nacional de Mobilidade Urbana – Lei Federal nº 12.587/2012; normas técnicas aplicáveis à acessibilidade e segurança viária." | "Mobilidade urbana e transportes." | "Lei Federal n°12.587, de 03 de janeiro de 2012, que “Institui as diretrizes da Política Nacional de Mobilidade Urbana." | "Políticas públicas nacionais de interesse para auditoria do TCE/PE: Lei Federal n° 12.587/2012 (Política Nacional de Mobilidade Urbana)." | "Lei nº 12.587/2012 (Política Nacional de Mobilidade Urbana)."
```

### 16. Ensino de História e Geografia (didática)  (16 concursos, 36 tópicos)
```
Matéria 16. id: "educacao.ensino-historia-geografia". subject: "Educação". Título: "Ensino de História e Geografia (didática)".
Cubra: BNCC de história e geografia, fontes históricas em sala de aula, ensino de história da África e indígena, cartografia escolar, estudo do meio, avaliação.
NÃO repita (já existe): história e geografia (conteúdos).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Metodologia do ensino de História." | "Ensino de geografia nos anos iniciais." | "Ensino de história nos anos iniciais." | "Ensino de História: saber histórico escolar." | "Trabalho com documentos e diferentes Linguagens no ensino de História." | "Tecnologias digitais no ensino de História." | "Ensino de História conforme a BNCC." | "História e ensino de história." | "O ensino de história e o trabalho com as diversas linguagens (fontes históricas)." | "Produção e usos de materiais didáticos no ensino de história." | "O ensino de história, inclusão e diversidade cultural, questões étnico-raciais, de gênero e da pessoa com deficiência no Ensino Fundamental." | "Metodologia do ensino de História: saber histórico escolar; trabalho com documentos e diferentes linguagens no ensino de História."
```

### 17. Abordagens psicoterápicas  (16 concursos, 22 tópicos)
```
Matéria 17. id: "psicologia.clinica-abordagens". subject: "Psicologia". Título: "Abordagens psicoterápicas".
Cubra: psicanálise, terapia cognitivo-comportamental, análise do comportamento, gestalt e abordagens humanistas, terapia sistêmica e familiar, psicoterapia breve e de grupo; indicações e técnicas.
NÃO repita (já existe): psicopatologia e psicoterapia geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Contribuições da psiquiatria, da psicologia e da psicanálise para a saúde." | "Contribuições da Psiquiatria, Psicologia e Psicanálise." | "Gestalt aplicada ao design." | "Fundamentos Teóricos e Históricos da Psicologia: evolução histórica e principais correntes – behaviorismo, psicanálise, humanismo, cognitivismo e fenomenologia; concepções contemporâneas." | "Conhecimento básico em Gestalt Conhecimento básico em Psicodinâmica das cores." | "Teorias da personalidade: psicanalítica, humanista, cognitiva e comportamental." | "Princípios da Gestalt." | "Abordagens psicológicas (psicanálise, humanismo, behaviorismo, TCC)." | "Psicoterapia breve." | "Psicoterapias (Noções): Terapia cognitivo-comportamental; Psicoterapia psicodinâmica; Terapias de apoio." | "Transtornos do sono. 9) Psicoterapias: Abordagens psicoterápicas: psicanálise; terapia cognitivo-comportamental; psicoterapia de grupo." | "Teoria de personalidade: psicanálise Freud."
```

### 18. Educação de jovens e adultos e educação do campo  (14 concursos, 117 tópicos)
```
Matéria 18. id: "educacao.eja-educacao-campo". subject: "Educação". Título: "Educação de jovens e adultos e educação do campo".
Cubra: EJA (concepções, diretrizes, Paulo Freire), educação do campo e quilombola, pedagogia da alternância, educação prisional.
NÃO repita (já existe): EaD e andragogia (geral).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Educação em Direitos Humanos (Educação indígena; Educação quilombola; Educação das relações étnico-raciais; Educação em gênero e diversidade)." | "Processos de territorialização indígena e quilombola." | "PARTE 2: Fundamentos da Educação do Campo: Concepção de Educação do Campo." | "História da Educação do Campo no Brasil." | "Princípios político pedagógicos da Educação do Campo." | "Legislação da Educação do Campo." | "Pedagogia da alternância." | "Pedagogia da Alternância e contextualização do ensino de Ciências e Física." | "Territórios e memória na constituição da identidade dos povos de terreiro e quilombolas no Espírito Santo; Estratégias de enfrentamento do racismo na/pela educação; Branquitude e antirracismo." | "Quilombolas." | "Resolução nº 376, de 21 de junho de 2023: Institui as normas complementares para a Modalidade EJA – Educação de Jovens e Adultos no Sistema Estadual de Ensino do RS." | "Resolução CNE/CEB nº 2, de 19 de maio de 2010: Diretrizes Nacionais para a oferta de educação para jovens e adultos em situação de privação de liberdade nos estabelecimentos penais."
```

### 19. Cirurgia plástica, torácica e de cabeça e pescoço  (14 concursos, 81 tópicos)
```
Matéria 19. id: "medicina.cirurgia-plastica-toracica". subject: "Medicina". Título: "Cirurgia plástica, torácica e de cabeça e pescoço".
Cubra: princípios de cirurgia plástica (enxertos, retalhos, queimados, reconstrução), cirurgia torácica (pneumotórax, derrames, tumores, trauma torácico), cirurgia de cabeça e pescoço (tireoide, glândulas salivares, tumores).
NÃO repita (já existe): cirurgia geral; trauma.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Avaliação de risco cirúrgico em cirurgia torácica." | "Princípios gerais da cirurgia torácica oncológica." | "Complicações em cirurgia torácica." | "Cirurgia torácica minimamente invasiva." | "Cabeça e pescoço." | "Reconstrução de cabeça e pescoço." | "Semiologia, semiotécnica, diagnóstico, prevenção e tratamento de neoplasias malignas e benignas da cabeça e pescoço; patologia oral." | "Câncer de cabeça e pescoço – epidemiologia e diagnóstico." | "Complicações orais da radioterapia de cabeça e pescoço." | "Anatomia aplicada da cabeça e pescoço." | "Complicações pós-operatórias em cabeça e pescoço; Complicações cirúrgicas trans e pós-operatórias." | "Anomalias congênitas da cabeça e pescoço."
```

### 20. Neurocirurgia  (14 concursos, 68 tópicos)
```
Matéria 20. id: "medicina.neurocirurgia". subject: "Medicina". Título: "Neurocirurgia".
Cubra: hipertensão intracraniana, TCE e trauma raquimedular (abordagem cirúrgica), tumores do SNC, hidrocefalia, AVC hemorrágico e aneurismas, doenças da coluna, neurocirurgia pediátrica e funcional.
NÃO repita (já existe): neurologia clínica.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Medidas para controle da hipertensão intracraniana." | "Tumores do SNC." | "Carcinogênese, biologia dos tumores e marcadores tumorais em neurocirurgia." | "Epidemiologia dos tumores do Sistema Nervoso crânio e coluna vertebral." | "Infecção em neurocirurgia." | "Propedêutica e exames subsidiários em neurocirurgia." | "Complicações pós-operatórias em neurocirurgia." | "Cuidados perioperatórios em neurocirurgia." | "Compressão raquimedular." | "Cuidados pré e pós-operatórios em neurocirurgia." | "Hipertensão intracraniana." | "Hipertensão intracraniana / Meningite carcinomatosa."
```

### 21. Economia regional, urbana e do trabalho  (14 concursos, 40 tópicos)
```
Matéria 21. id: "economia.economia-regional-politicas". subject: "Economia". Título: "Economia regional, urbana e do trabalho".
Cubra: teorias de localização, desenvolvimento regional, economia urbana, indicadores socioeconômicos, mercado de trabalho e políticas de emprego.
NÃO repita (já existe): economia brasileira; macro.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Economia e sociedade: indicadores socioeconômicos, emprego, renda, inflação e desenvolvimento econômico e social." | "Aspectos geopolíticos e planos de desenvolvimento regional." | "CARGOS DE ANALISTA E TÉCNICO LEGISLATIVO indicadores sociais; desenvolvimento regional; desigualdades territoriais; dinâmica populacional; políticas públicas estaduais; realidade social cearense." | "Economia regional." | "Economia urbana." | "Fundos constitucionais e de desenvolvimento regional." | "Indicadores Socioeconômicos e Avaliação." | "Logística e desenvolvimento regional." | "Noções de economia urbana, urbanismo e sociologia urbana." | "Economia regional: teorias clássicas da localização, polos de crescimento, teoria da base de exportação e teoria do desenvolvimento desigual." | "Experiências internacionais com políticas de desenvolvimento regional e zonas econômicas especiais." | "Políticas de desenvolvimento regional no Brasil."
```

### 22. Zoologia e botânica  (14 concursos, 15 tópicos)
```
Matéria 22. id: "biologia.zoologia-botanica". subject: "Biologia". Título: "Zoologia e botânica".
Cubra: classificação e características dos grandes grupos de invertebrados e vertebrados, anatomia e fisiologia comparada, botânica (morfologia, anatomia, fisiologia vegetal, sistemática).
NÃO repita (já existe): seres vivos e classificação (geral).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Desenvolvimento embrionário nos vertebrados." | "Biotecnologia animal. Formação e padronização do mesoderma em vertebrados: gene TGFβ e HOX. Indução neural em vertebrados e controle gênico da padronização do tubo neural." | "Botânica básica aplicada ao laboratório didático." | "Botânica. Morfologia Vegetal e Fisiologia. Fitofisionomia. Biomas." | "Botânica e identificação de espécies vegetais." | "Botânica e fisiologia vegetal." | "Noções de zoologia aplicada ao manejo de áreas verdes." | "Resolução CFMV nº 1.236/2018: crueldade, abuso e maus-tratos contra animais vertebrados." | "Botânica Geral. Taxonomia vegetal, anatomia vegetal." | "Tramitação de envio e recebimento de material zoológico e montagem de lâminas para estudos de invertebrados." | "Anatomia e Fisiologia Vegetal." | "Botânica aplicada: identificação de estruturas vegetais e uso de lupas e microscópios."
```

### 23. Alergia e imunologia clínica  (13 concursos, 73 tópicos)
```
Matéria 23. id: "medicina.reumatologia-imunologia-alergia". subject: "Medicina". Título: "Alergia e imunologia clínica".
Cubra: asma alérgica e rinite, urticária e angioedema, anafilaxia, alergia alimentar e a medicamentos, imunodeficiências primárias, testes alérgicos, imunoterapia.
NÃO repita (já existe): clínica reumatologia (medicina.clinica-reumatologia).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Anafilaxia." | "Alergia alimentar." | "Imunoterapia passiva e patógenos causadores de infecções hospitalares." | "Neoplasias – aspectos gerais: Biologia do Tumor e Marcadores Tumorais Imunologia e Imunoterapia Tumoral Melanoma e Neoplasias Cutâneas Malignas Sarcoma de Tecidos Moles Tumores Ósseos." | "Alergia e inflamação." | "Imunodeficiências Primárias." | "Alergia e reação adversas a alimentos." | "Alergias Ocupacionais." | "Alergia ao Látex." | "Imunoterapia." | "Alergia a insetos." | "Alergia a penicilina."
```

### 24. Conselho Tutelar e Sistema de Garantia de Direitos  (13 concursos, 24 tópicos)
```
Matéria 24. id: "legislacao.eca-conselho-tutelar". subject: "Legislação". Título: "Conselho Tutelar e Sistema de Garantia de Direitos".
Cubra: atribuições do conselho tutelar, processo de escolha, medidas de proteção, rede de proteção, SINASE (noções), fluxos de atendimento.
NÃO repita (já existe): ECA (direitos fundamentais e política de atendimento).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei n. 13.803/2019 (Notificação de faltas escolares ao Conselho Tutelar)." | "Lei nº 13.431/2017 e Decreto nº 9.603/2018 - Sistema de garantia de direitos da criança e do adolescente vítima ou testemunha de violência." | "Lei nº 13.431/2017 - Sistema de Garantia de Direitos da Criança e do adolescente vítima ou testemunha de violência e Decreto Federal que a regulamenta." | "Lei nº 13.431/2017 (Estabelece o sistema de garantia de direitos da criança e do adolescente vítima ou testemunha de violência) e suas alterações." | "Lei nº 13.431/2017 (Sistema de garantia de direitos da criança e do adolescente vítima ou testemunha de violência)." | "Lei do Sistema de Garantia de Direitos da Criança e do Adolescente Vítima ou Testemunha de Violência (Lei nº 13.431/17)." | "Lei nº 13.431/17 (Estabelece sistema de garantia de direitos da criança e do adolescente vítima ou testemunha de violência)."
```

### 25. Mastologia  (12 concursos, 124 tópicos)
```
Matéria 25. id: "medicina.mastologia". subject: "Medicina". Título: "Mastologia".
Cubra: anatomia e fisiologia da mama, doenças benignas, rastreamento e diagnóstico por imagem, câncer de mama (estadiamento, cirurgia, tratamento sistêmico, reconstrução), mama na gestação e no homem.
NÃO repita (já existe): ginecologia e oncologia (já tratam da mama em linhas gerais).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lesões não palpáveis da mama." | "Sarcomas de mama." | "Anomalias do desenvolvimento mamário." | "Ultrassonografia da mama." | "Avaliação ultrassonográfica de nódulos e massas mamárias." | "Sistemas de classificação e estratificação de risco das lesões mamárias." | "Embriologia do sistema urogenital e mamário." | "Câncer de mama em jovens e idosas. 40 . Câncer de mama bilateral. 41 . Recidivas locais pós-cirurgias. 42 . Seguimento após câncer de mama. 43 . Doença metastática e cuidados paliativos." | "44 . Bioética e medicina legal aplicada a mastologia." | "Mama (RX, US e RM); Técnicas de posicionamento; Patologias inflamatórias e infecciosas; Tumores benignos e malignos; BI-RADS®." | "PARTE 2: Aspecto anatomopatológicos em câncer de mama." | "Biologia molecular em câncer de mama."
```

### 26. Psicologia escolar e educacional  (12 concursos, 24 tópicos)
```
Matéria 26. id: "psicologia.escolar-educacional". subject: "Psicologia". Título: "Psicologia escolar e educacional".
Cubra: atuação do psicólogo na escola, queixa escolar, dificuldades e transtornos de aprendizagem, avaliação psicoeducacional, inclusão, bullying, mediação de conflitos, orientação a professores e famílias.
NÃO repita (já existe): psicologia do desenvolvimento e aprendizagem.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Psicopedagogia." | "Psicologia escolar: histórico, caracterização, interdisciplinaridade, perspectivas atuais e controvérsias da atuação em psicologia escolar." | "Atuação em psicologia escolar: colaboração com o processo de ensino e de aprendizagem." | "Psicologia Educacional: o papel do psicólogo na escola; inclusão educacional; lei nº 13.146/2015." | "Psicopedagogia: conceitos, fundamentos, objeto de estudo e caráter interdisciplinar; construção do conhecimento; teorias psicogenéticas." | "Psicologia Escolar e Educacional: Atuação do Psicólogo na Escola." | "História da psicologia escolar no Brasil." | "Papel e funções do psicólogo escolar." | "O papel do psicopedagogo na gestão da educação." | "Ação do psicopedagogo na organização do trabalho pedagógico nas instituições públicas." | "Métodos e técnicas de pesquisa aplicada a Psicopedagogia." | "Psicologia Escolar e Educacional: fundamentos, objetivos, campos de atuação e atribuições do psicólogo no contexto educacional; atuação institucional, preventiva e interdisciplinar."
```

### 27. Ensino de Língua Inglesa (didática)  (11 concursos, 19 tópicos)
```
Matéria 27. id: "educacao.ensino-lingua-inglesa". subject: "Educação". Título: "Ensino de Língua Inglesa (didática)".
Cubra: BNCC de inglês, inglês como língua franca, abordagens e métodos de ensino, letramentos e multiletramentos, avaliação, uso de tecnologias.
NÃO repita (já existe): gramática e interpretação de inglês.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Interculturalidade e interdisciplinaridade no ensino de Língua Inglesa." | "Ensino de Língua Inglesa no contexto da Base Nacional Comum Curricular." | "Metodologias ativas no ensino de inglês." | "Tendências pedagógicas do ensino de língua inglesa: abordagem da linguagem sob novos enfoques." | "Ensino de inglês e suas metodologias." | "O ensino de Inglês para estudantes com deficiência." | "O ensino de inglês para classes com níveis variados de conhecimento da língua." | "O ensino de inglês para diferentes idades." | "O Ensino de Língua Inglesa e o Documento de Referência Curricular para Mato Grosso para Ensino Fundamental e Médio (DRC/MT)." | "Conhecimento científico, abordagens metodológicas e recursos didáticos no ensino de Língua Inglesa."
```

### 28. Administração de recursos materiais e patrimônio público  (10 concursos, 10 tópicos)
```
Matéria 28. id: "administracao.gestao-publica-orcamento-municipal". subject: "Administração Pública". Título: "Administração de recursos materiais e patrimônio público".
Cubra: gestão de estoques, almoxarifado, compras, patrimônio (tombamento, inventário, baixa), logística pública.
NÃO repita (já existe): administração de materiais (geral).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Gestão do patrimônio público." | "Vigilância e proteção do patrimônio público contra danos." | "Preservação e conservação do patrimônio público." | "Organização e gestão de laboratórios de informática: normas de utilização; controle de equipamentos; organização dos ambientes tecnológicos; inventário de bens." | "Zelo pelo patrimônio público." | "Patrimônio público – bens móveis, imóveis e depreciação." | "Preservação do patrimônio público." | "Estabilidade de taludes em rocha: Modos de ruptura: planar, cunha e tombamento." | "Patrimônio Público."
```

### 29. Processo legislativo e Poder Legislativo estadual  (9 concursos, 385 tópicos)
```
Matéria 29. id: "direito.processo-legislativo-estadual". subject: "Processo Legislativo". Título: "Processo legislativo e Poder Legislativo estadual".
Cubra: organização das assembleias legislativas, mesa, comissões, espécies normativas estaduais, tramitação, quórum, imunidades e prerrogativas dos deputados estaduais.
NÃO repita (já existe): regimento da câmara federal.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Regimento Interno da Assembleia Legislativa do Estado de Rondônia: Resolução nº 32, de 21 de agosto de 1990 e suas alterações." | "Código de Ética e Decoro Parlamentar da Assembleia Legislativa do Estado de Rondônia." | "Plano de Carreira, Cargos e Remuneração e o Quadro de Pessoal da Assembleia Legislativa do Estado de Rondônia: Lei Complementar nº 731, de 30 de setembro de 2013 e suas alterações." | "Resolução nº 015/2024 (estrutura administrativa da Assembleia Legislativa do Estado de Roraima)." | "Regimento Interno da Assembleia Legislativa do Estado de Roraima (Resolução nº 08 de 13 de dezembro de 2023 e suas alterações)." | "Resolução Legislativa n. 469/2010 – Institui o Regimento Interno da Assembleia Legislativa do Estado do Amazonas Conhecimentos Específicos." | "Resolução nº 751/2022 e alterações (Regimento Interno da Assembleia Legislativa do Estado do Ceará)." | "Regimento Interno e Processo Legislativo da Assembleia Legislativa do Estado do Pará: organização, composição e funcionamento da Assembleia Legislativa." | "Regimento Interno da Assembleia Legislativa do Estado do Ceará." | "Do Poder Legislativo: Disposições Gerais: Artigo 49; Das Atribuições da Assembleia Legislativa: Artigo 52." | "Resolução Legislativa n. 469/2010 – Institui o Regimento Interno da Assembleia Legislativa do Estado do Amazonas." | "Organização e funcionamento da Assembleia Legislativa do Estado do Ceará – ALECE."
```

### 30. Coloproctologia  (9 concursos, 68 tópicos)
```
Matéria 30. id: "medicina.coloproctologia". subject: "Medicina". Título: "Coloproctologia".
Cubra: doenças anorretais (hemorroidas, fissura, fístula, abscesso), doença diverticular, doenças inflamatórias intestinais (abordagem cirúrgica), câncer colorretal e anal, pólipos, colonoscopia.
NÃO repita (já existe): gastroenterologia e cirurgia geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Doença diverticular dos cólons." | "Anomalias anorretais." | "Fissura anal." | "Afecções cirúrgicas do duodeno, jejuno e íleo: Doença ulcerosa Doença diverticular Doenças infecciosas e inflamatórias Neoplasias Obstrução intestinal Problemas diversos." | "Divertículos de esôfago, duodeno e cólon." | "Doença hemorroidária. Etiopatogenia. Quadro clínico e diagnóstico. Diagnóstico diferencial. Conduta terapêutica." | "Criptite e papilite. Abscesso anorretal. Etiopatogenia e classificaçãoo dos abscessos. Quadro clínico e diagnóstico. Conduta terapêutica." | "Fissura anal. Etiopatogenia. Quadro clínico e diagnóstico. Diagnóstico diferencial. Conduta terapêutica." | "Fístula anal. Etiopatogenia. Classificação. Quadro clínico. Diagnóstico diferencial. Conduta terapêutica." | "Malformações congênitas do cólon e da região anorretal. Classificação. Considerações embriológicas. Anomalias associadas. Diagnóstico. Conduta terapêutica." | "Abdomen agudo em coloproctologia - Aspectos gerais do diagnóstico e tratamento." | "Doença diverticular do cólon. Etiopatogenia. Fisiopatologia. Quadro clínico e diagnóstico diferencial. Complicações. Conduta terapêutica."
```

### 31. Medicina nuclear e radioterapia  (9 concursos, 49 tópicos)
```
Matéria 31. id: "saude.medicina-nuclear-radioterapia". subject: "Saúde". Título: "Medicina nuclear e radioterapia".
Cubra: radiofármacos e cintilografia, PET-CT, radioproteção em medicina nuclear, princípios de radioterapia (teleterapia, braquiterapia), planejamento e efeitos adversos.
NÃO repita (já existe): técnicas radiológicas.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Noções de radioterapia para o paciente neurocirúrgico." | "Braquiterapia." | "Bases Físicas da Radioterapia." | "Sistemas de Imobilização em Radioterapia Externa." | "Radioterapia Conformacional." | "Radiocirurgia e Radioterapia Estereotáxica." | "Radioterapia a volumes Alargados. 8.Radioterapia em Medicina Nuclear." | "Interações entre Radioterapia e Quimioterapia." | "Radioterapia Intraoperatória." | "Emergências em Radioterapia." | "Radioterapia em Lesões Benignas." | "Quimioterapia, radioterapia e cirurgias."
```

### 32. Atuação do agente comunitário de saúde  (9 concursos, 26 tópicos)
```
Matéria 32. id: "saude-publica.agente-comunitario". subject: "Saúde Pública". Título: "Atuação do agente comunitário de saúde".
Cubra: atribuições do ACS, cadastramento e territorialização, visita domiciliar, acompanhamento de grupos prioritários, educação em saúde, sistemas de informação da atenção básica.
NÃO repita (já existe): políticas nacionais de saúde.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Territorialização." | "Atribuições e Competências do Agente Comunitário de Saúde." | "Ferramentas de trabalho do Agente Comunitário de Saúde." | "O Agente Comunitário de Saúde um agente de mudanças." | "Territorialização de políticas públicas." | "Saúde Coletiva: vigilância em saúde; planejamento em saúde; territorialização; indicadores; programas e linhas de cuidado; políticas públicas de saúde." | "Geoprocessamento em Saúde, cadastramento e territorialização."
```

### 33. Medicina de família e comunidade  (9 concursos, 14 tópicos)
```
Matéria 33. id: "medicina.medicina-familia-comunidade". subject: "Medicina". Título: "Medicina de família e comunidade".
Cubra: atributos da atenção primária, abordagem familiar (genograma, ecomapa), método clínico centrado na pessoa, prevenção quaternária, rastreamento, abordagem das queixas comuns, cuidado longitudinal, saúde do homem e da mulher na APS.
NÃO repita (já existe): políticas nacionais de saúde; clínica médica.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Medicina Preventiva e Social; medicina de família e comunidade; clínica médica e medicina interna." | "Família: as novas modalidades de família, metodologias de abordagem familiar." | "Abordagem Familiar na Infância e Adolescência." | "Método clínico centrado na pessoa." | "Metodologias de abordagem familiar." | "Princípios, competências e habilidades da Medicina de Família e Comunidade." | "A abordagem familiar e assistência domiciliar." | "A abordagem familiar e a assistência domiciliar." | "Abordagem familiar no território da APS." | "Fundamentos da Medicina de Família e Comunidade." | "Abordagem familiar e ferramentas." | "Promoção da saúde e prevenção (inclusive prevenção quaternária)."
```

### 34. Enfermagem em centro cirúrgico, recuperação anestésica e CME  (9 concursos, 14 tópicos)
```
Matéria 34. id: "enfermagem.centro-cirurgico-cme". subject: "Enfermagem". Título: "Enfermagem em centro cirúrgico, recuperação anestésica e CME".
Cubra: organização do centro cirúrgico, tempos cirúrgicos e instrumentação, posicionamento cirúrgico, SRPA, cirurgia segura, central de material e esterilização (processamento, embalagem, esterilização, rastreabilidade).
NÃO repita (já existe): controle de infecção e esterilização (geral).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Recuperação pós-anestésica." | "Enfermagem em Centro Cirúrgico e Centro de Material." | "Noções de apoio em instrumentação cirúrgica, sob supervisão do enfermeiro." | "Enfermagem no centro cirúrgico: materiais e equipamentos básicos que compõem as salas de cirurgia e recuperação anestésica." | "Centro cirúrgico; 13." | "Fundamentos da instrumentação cirúrgica." | "Centro Cirúrgico (CC) e sala de operações." | "Recuperação anestésica: cuidados pós-operatórios imediatos, complicações e critérios de alta da sala de recuperação pós-anestésica (SRPA)." | "Assistência de Enfermagem em Centro Cirúrgico, Sala de Recuperação Pós Anestésica (SRPA) e Central de Material e Esterilização (CME)."
```

### 35. Ética no serviço público: Decreto 1.171 e conflito de interesses  (7 concursos, 38 tópicos)
```
Matéria 35. id: "etica.etica-servico-publico-federal". subject: "Ética". Título: "Ética no serviço público: Decreto 1.171 e conflito de interesses".
Cubra: Código de Ética do Servidor Civil Federal, comissões de ética, Lei de Conflito de Interesses, sistema de gestão da ética, integridade.
NÃO repita (já existe): ética e função pública (geral).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Código de Ética do Servidor Público Estadual." | "Decreto Estadual nº 33.094/2023: Aprova o Código de Ética Profissional do Servidor Público Civil do Poder Executivo Estadual." | "Corrupção como fenômeno político e social: distinções entre corrupção, falha de gestão, conflito de interesses e ilícitos específicos." | "Decreto estadual nº 69.474, de 10 de abril de 2025, que dispõe sobre o conflito de interesses no âmbito da Administração Pública direta e autárquica."
```

### 36. Fiscalização de obras e posturas  (7 concursos, 19 tópicos)
```
Matéria 36. id: "direito.direito-urbanistico-obras". subject: "Direito Urbanístico". Título: "Fiscalização de obras e posturas".
Cubra: alvará de construção, habite-se, embargo e demolição, normas de edificação, posturas urbanas, poder de polícia municipal sobre obras (conceitos gerais, sem leis locais).
NÃO repita (já existe): política urbana e Estatuto da Cidade.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Fiscalização de obras, posturas, serviços urbanos, vias, calçadas, loteamentos e logradouros públicos." | "Controle e fiscalização de obras, posturas e uso do espaço público." | "Código de obras e licenciamento." | "Diretrizes para Contratação e Fiscalização de Obras da Rede Ebserh - Volumes I, II e III." | "Alvarás e habite-se." | "Direitos e deveres do Agente de Fiscalização de Obras." | "Vistoria e habite-se." | "Código de obras." | "Monitoramento do cumprimento do código de obras interno (Código de Obras e Edificações da UFSCar: Resolução CoAd no 22/2020) e demais normas institucionais." | "Licenciamento, aprovação e regularização de obras públicas: alvarás, licenças ambientais, habite-se e alterações de projeto."
```

### 37. Indicadores sociais e análise de dados públicos  (7 concursos, 17 tópicos)
```
Matéria 37. id: "estatistica.indicadores-sociais-dados". subject: "Estatística". Título: "Indicadores sociais e análise de dados públicos".
Cubra: construção e interpretação de indicadores, números-índice, taxas e coeficientes, fontes de dados oficiais (IBGE, DATASUS), leitura de tabelas e gráficos.
NÃO repita (já existe): estatística descritiva.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Clima, Sustentabilidade e Responsabilidade Socioambiental e Climática: Conceitos dos principais indicadores sociais e ambientais." | "Indicadores sociais; elaboração de laudos, pareceres e relatórios sociais." | "Indicadores Sociais e Fontes de Dados: indicadores sociais, econômicos e demográficos; utilização de bases estatísticas; produção e interpretação de dados sociais; fontes oficiais de informação." | "Indicadores de desenvolvimento (IDH, PIB, indicadores sociais e regionais)." | "Indicadores sociais; programas, projetos, serviços e benefícios de assistência social." | "Conceitos da pedagogia educacional nos padrões de qualidade e indicadores sociais." | "Indicadores sociais (IDH, índice de Gini, entre outros) e análise crítica."
```

### 38. Hematologia laboratorial  (6 concursos, 6 tópicos)
```
Matéria 38. id: "farmacia.analises-clinicas-hematologia-lab". subject: "Farmácia". Título: "Hematologia laboratorial".
Cubra: hemograma (eritrograma, leucograma, plaquetas), morfologia celular, contagem automatizada, coagulograma, provas de hemostasia, velocidade de hemossedimentação, controle de qualidade em hematologia.
NÃO repita (já existe): hematologia clínica médica; imuno-hematologia.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Análise e interpretação de hemograma completo e mielograma." | "Interpretação do hemograma na infância." | "Interpretação do hemograma." | "Hematologia clínica veterinária: interpretação de hemogramas, leucogramas, avaliação da medula óssea e doenças hematopoiéticas e hemolinfáticas."
```

### 39. Suporte técnico e manutenção de computadores  (5 concursos, 5 tópicos)
```
Matéria 39. id: "informatica.suporte-tecnico-hardware". subject: "Informática". Título: "Suporte técnico e manutenção de computadores".
Cubra: montagem e manutenção de hardware, instalação de sistemas e drivers, diagnóstico de falhas, impressoras, redes locais básicas, atendimento ao usuário.
NÃO repita (já existe): hardware conceitos básicos.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Suporte Técnico e Sustentação de Sistemas Manutenção corretiva, adaptativa, evolutiva e preventiva." | "Projetos de sistemas e tecnologia: análise, orientação e suporte técnico em sistemas mecanizados, eletrônicos e de processamento de dados." | "Manutenção e Suporte Técnico: Atendimento ao usuário." | "Suporte técnico e atendimento ao usuário: Atendimento e Gestão de Chamados: Abertura, classificação e priorização de tickets." | "Suporte técnico: diagnóstico e solução de problemas e orientação a usuários."
```

### 40. Fiscalização de trânsito e atuação do agente  (4 concursos, 24 tópicos)
```
Matéria 40. id: "legislacao.transito-agente". subject: "Legislação". Título: "Fiscalização de trânsito e atuação do agente".
Cubra: competências dos órgãos municipais de trânsito, autuação e procedimentos, medidas administrativas, sinalização, educação para o trânsito.
NÃO repita (já existe): CTB (sistema, infrações, habilitação).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Distribuição, autuação e registro."
```

### 41. Drenagem urbana e manejo de águas pluviais  (4 concursos, 5 tópicos)
```
Matéria 41. id: "engenharia.engenharia-sanitaria-drenagem". subject: "Engenharia". Título: "Drenagem urbana e manejo de águas pluviais".
Cubra: hidrologia urbana, microdrenagem e macrodrenagem, dimensionamento, controle de cheias, drenagem sustentável.
NÃO repita (já existe): saneamento e hidrologia (gerais).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Galeria de águas pluviais." | "Qualidade das águas pluviais urbanas: poluição difusa, contaminantes e medidas de controle." | "NR nº 12/2025 (aprovada pela Resolução ANA n. 245, de 17 de março de 2025): Dispõe sobre a estruturação dos serviços públicos de drenagem e manejo de águas pluviais urbanas."
```

### 42. Supervisão, orientação e coordenação pedagógica  (3 concursos, 12 tópicos)
```
Matéria 42. id: "educacao.gestao-supervisao-orientacao". subject: "Educação". Título: "Supervisão, orientação e coordenação pedagógica".
Cubra: papel do coordenador e supervisor pedagógico, orientação educacional, formação continuada na escola, conselho de classe, articulação escola-família.
NÃO repita (já existe): avaliação e gestão escolar.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Funções do(a) pedagogo(a) na supervisão e orientação educacional." | "Origem, evolução e contextualização da Orientação Educacional no Brasil." | "Fundamentos legais, pressupostos teóricos, atribuições e ética profissional do Orientador Educacional." | "Legislação e regulamentação da profissão de Orientador Educacional." | "O serviço de Orientação Educacional na Educação Básica." | "Orientação educacional, vocacional e profissional no contexto escolar, observados os limites e as atribuições profissionais." | "A orientação educacional como mediadora do sucesso na aprendizagem e da permanência do aluno na escola." | "Orientação Pedagógica da Orientação Educacional (2019)." | "A orientação educacional e a construção de processos solidários e de engajamento normativo: o orientador educacional como mediador das relações escola-família-comunidade."
```

### 43. Contabilidade gerencial e análise de custos para decisão  (3 concursos, 3 tópicos)
```
Matéria 43. id: "contabilidade.custos-gerencial". subject: "Contabilidade". Título: "Contabilidade gerencial e análise de custos para decisão".
Cubra: margem de contribuição, ponto de equilíbrio, alavancagem operacional, custo-padrão, orçamento empresarial, indicadores gerenciais.
NÃO repita (já existe): custos (geral).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Custo-padrão." | "Contabilidade Gerencial."
```

### 44. Guardas municipais e segurança cidadã  (2 concursos, 2 tópicos)
```
Matéria 44. id: "seguranca-publica.guarda-municipal". subject: "Segurança Pública". Título: "Guardas municipais e segurança cidadã".
Cubra: Estatuto Geral das Guardas Municipais, competências e uso progressivo da força, abordagem, direitos humanos na atuação, prevenção da violência, mediação de conflitos.
NÃO repita (já existe): segurança pública geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "O uso da força na Carta das Nações Unidas." | "Lei nº 13.022/2014 (Estatuto Geral das Guardas)."
```

### 45. Serviço Social na saúde e no hospital  (1 concursos, 2 tópicos)
```
Matéria 45. id: "servico-social.saude-hospitalar". subject: "Serviço Social". Título: "Serviço Social na saúde e no hospital".
Cubra: atuação do assistente social no SUS e no hospital, alta social, rede de proteção, parâmetros de atuação, saúde mental e redução de danos, cuidados paliativos, orientação previdenciária e de benefícios.
NÃO repita (já existe): fundamentos e SUAS.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Saúde e Serviço Social: atribuições do assistente social na saúde; trabalho em equipe multiprofissional; serviço social em hospitais, atenção básica e urgência/emergência." | "Serviço Social e família; serviço social na saúde."
```

### 46. Enfermagem em terapia intensiva adulto, pediátrica e neonatal  (1 concursos, 1 tópicos)
```
Matéria 46. id: "enfermagem.uti-neonatal-pediatrica". subject: "Enfermagem". Título: "Enfermagem em terapia intensiva adulto, pediátrica e neonatal".
Cubra: cuidados ao paciente crítico, ventilação mecânica e via aérea (cuidados de enfermagem), drogas vasoativas, cateteres e monitorização, cuidados ao recém-nascido de risco e à criança grave.
NÃO repita (já existe): urgência e emergência de enfermagem.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Psicologia aplicada ao paciente crítico e familiares."
```

### 47. Enfermagem na saúde da mulher e obstétrica  (0 concursos, 0 tópicos)
```
Matéria 47. id: "enfermagem.saude-mulher-obstetrica". subject: "Enfermagem". Título: "Enfermagem na saúde da mulher e obstétrica".
Cubra: pré-natal de baixo risco pelo enfermeiro, parto normal e humanização, puerpério, aleitamento, planejamento reprodutivo, prevenção do câncer de colo e mama, violência contra a mulher.
NÃO repita (já existe): saúde nos ciclos de vida (noções).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): (só o título)
```
