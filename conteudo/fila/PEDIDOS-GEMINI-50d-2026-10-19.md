# Pedidos para o Gemini: mais 50 matérias novas (19/10/2026, quarta leva)

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

### 1. Microbiologia ambiental, monitoramento e conservação da biodiversidade  (28 concursos, 43 tópicos)
```
Matéria 1. id: "biologia.microbiologia-ambiental-biodiversidade". subject: "Biologia". Título: "Microbiologia ambiental, monitoramento e conservação da biodiversidade".
Cubra: microbiologia do solo, da água, do ar e dos alimentos, técnicas de amostragem ambiental, indicadores biológicos, inventário de fauna e flora, espécies ameaçadas e endêmicas, biologia molecular aplicada (PCR, recombinação, marcadores), bioética e biossegurança.
NÃO repita (já existe): ecologia e zoologia (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Amostragem de solo." | "Métodos de amostragem e análises." | "Técnicas de amostragem e preparo de amostras para ensaios microbiológicos." | "Processos específicos, amostragem, coleta e manuseio de amostras." | "Desenho, condições e amostragem em estudos de estabilidade." | "Amostragem e interpretação análises." | "Amostragem, coleta, transporte e preservação de amostras." | "Métodos e Pesquisa Aplicada à Gestão Pública: métodos qualitativos: amostragem, coleta e análise de dados, codificação e análise de discurso." | "Métodos de amostragem, elaboração de questionários e grupos focais." | "Amostragem e preservação de amostras ambientais." | "Métodos de Pesquisa Científica: Planejamento experimental, delineamento de experimentos (ex: DIC, DBC, Fatorial), amostragem, coleta, tratamento e interpretação de dados agrários." | "Pesquisa experimental e coleta de dados: levantamento de campo, amostragem espacial, registro, codificação, tabulação, organização, análise e validação de dados de pesquisas geográficas e ambientais."
```

### 2. Fertilidade do solo, adubação, irrigação e drenagem  (24 concursos, 44 tópicos)
```
Matéria 2. id: "agronomia.fertilidade-irrigacao-drenagem". subject: "Agronomia". Título: "Fertilidade do solo, adubação, irrigação e drenagem".
Cubra: fertilidade e química do solo, corretivos e calagem, adubação mineral e orgânica, recomendação de fertilizantes, matéria orgânica, irrigação (métodos, manejo, eficiência), drenagem agrícola, conservação do solo e da água, energia na agropecuária.
NÃO repita (já existe): culturas e mecanização (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Fertilizantes e corretivos." | "Impactos ambientais do uso de fertilizantes químicos, adubos orgânicos e de biossólidos." | "Princípios básicos de perícia com foco em insetos que vivem e se alimentam da matéria orgânica de origem animal em decomposição." | "Matéria orgânica." | "Fertirrigação." | "Drenagem agrícola." | "Irrigação: Hidráulica, irrigação e drenagem, projetos de barragens, necessidade de irrigação das principais culturas." | "Métodos de conservação dos solos e água (solos, plantas, adubação, correção de solo, fitotecnia e administração rural)." | "Noções sobre irrigação e drenagem." | "Noções de irrigação." | "Energia na agropecuária." | "Dimensionamento de sistemas de irrigação."
```

### 3. Metodologia da pesquisa em saúde  (23 concursos, 47 tópicos)
```
Matéria 3. id: "saude.metodologia-pesquisa-saude". subject: "Saúde". Título: "Metodologia da pesquisa em saúde".
Cubra: pesquisa quantitativa e qualitativa, desenhos de estudo, amostragem, instrumentos de coleta, análise de dados, ética em pesquisa com seres humanos (CEP/Conep), revisão sistemática, redação científica e normas.
NÃO repita (já existe): bioestatística e epidemiologia (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Métodos de pesquisa qualitativa e quantitativa." | "Pressupostos teóricos e metodológicos da pesquisa em saúde e enfermagem." | "Lei nº 7.117/2009 (dispõe sobre incentivos à pesquisa científica e tecnológica, à inovação e à proteção da propriedade intelectual em ambiente produtivo e social no Estado de Alagoas)." | "Metodologia da pesquisa nas ciências humanas e sociais, pesquisa histórica, geográfica, sociológica e antropológica, pesquisa documental, bibliográfica, de campo, qualitativa e quantitativa." | "Ciência, Pesquisa e Tecnologia em saúde: metodologias de pesquisa em saúde." | "Procedimentos de coleta de dados em pesquisa qualitativa e quantitativa: normas da ABNT, APA, Vancouver e outras." | "Metodologia de pesquisa em comunicação." | "Fundamentos de pesquisa científica aplicada às Geociências: métodos científicos, planejamento, execução, acompanhamento e avaliação de projetos de pesquisa agropecuária e ambiental." | "Noções básicas de pesquisa científica." | "Pesquisa qualitativa e quantitativa." | "Pesquisa em Psicologia: métodos de pesquisa; pesquisa qualitativa e quantitativa; produção e análise de dados; indicadores psicossociais; avaliação de programas." | "Metodologia da pesquisa científica."
```

### 4. Economia urbana e avaliação econômica de políticas públicas  (19 concursos, 34 tópicos)
```
Matéria 4. id: "economia.economia-urbana-avaliacao-politicas". subject: "Economia". Título: "Economia urbana e avaliação econômica de políticas públicas".
Cubra: economia urbana e regional, mercado imobiliário e uso do solo, avaliação de impacto e custo-benefício de políticas, indicadores econômicos e sociais, análise de bases de dados públicas, estudos econômicos para políticas sociais e ambientais.
NÃO repita (já existe): macroeconomia e microeconomia (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Estudos econômicos e demográficos." | "Uso de dados para planejamento, controle, monitoramento, avaliação de políticas públicas e apoio à tomada de decisão." | "Recomendação de equipamentos e softwares para licitações e aquisições com base na relação custo-benefício e adequação à realidade legislativa." | "Bases de dados econômicos." | "Conselho de Monitoramento e Avaliação de Políticas Públicas (CMAP), conforme Decreto nº 9.834/2019." | "Planejamento técnico do trabalho: relação entre perguntas de auditoria e de avaliação de políticas, respeitadas suas finalidades e seus desenhos." | "Identificação causal e análise econômica: custo-efetividade e custo-benefício, custo de oportunidade e sensibilidade." | "Governança e responsabilidade: rastreabilidade, avaliação de impacto, revisão humana e contestação." | "Uso de informações sociais para planejamento, monitoramento e avaliação de políticas públicas." | "Processamento de dados e métodos computacionais: pipelines para grandes bases de dados; integração de microdados com recortes territoriais, validação e clustering espacial, interpolação." | "Impactos Ambientais e Avaliação de Impacto Ambiental (AIA). Poluição do ar, água e solo: fontes." | "Águas doces no Brasil: capital ecológico, uso e conservação. Avaliação de impacto ambiental: conceitos e métodos. Estudo de impacto ambiental."
```

### 5. Medicina veterinária do coletivo: bem-estar, zoonoses e controle populacional  (19 concursos, 29 tópicos)
```
Matéria 5. id: "medicina-veterinaria.saude-publica-bem-estar-animal". subject: "Medicina Veterinária". Título: "Medicina veterinária do coletivo: bem-estar, zoonoses e controle populacional".
Cubra: bem-estar animal (cinco liberdades), controle populacional de cães e gatos, centros de zoonoses, vigilância de raiva, leishmaniose e leptospirose, posse responsável, imunização e controle de parasitas, biossegurança, medicina veterinária legal e maus-tratos.
NÃO repita (já existe): clínica e produção (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Fundamentos teóricos de bem-estar animal e de bioética." | "Abuso, maus-tratos e violência: aspectos psicossociais, diagnóstico e conduta." | "Política Estadual de Proteção e Bem-Estar Animal (Lei Estadual nº 2.167/2025)." | "Código de Direito e Bem-Estar Animal de Roraima (Lei Estadual nº 1.637/2022)." | "Animais sinantrópicos, silvestres e domésticos: conceitos, características, riscos à saúde humana e estratégias de controle populacional." | "Bem-estar animal: noções de manejo humanitário, cuidados e legislação básica sobre proteção animal." | "Inspeção e visitas domiciliares: identificação de situações de risco, maus-tratos e condições inadequadas de criação de animais." | "Manejo, Sanidade e Bem-Estar Animal: Manejo, sanidade, biossegurança, bem-estar e ambiência animal." | "Comportamento e bem-estar animal: avaliação de bem-estar; comportamento social e de reprodução; manejo, transporte e controle humanitário; comportamento anormal." | "Princípios: As cinco liberdades e os três Rs do bem-estar animal." | "Diagnóstico de maus-tratos físicos, psíquicos e ambientais em animais de companhia, trabalho, produção/reprodução, experimentais e em cativeiro." | "Abordagem à violência doméstica, maus-tratos em idosos, saúde do homem e da mulher."
```

### 6. Serviço social no sociojurídico: família, adoção e convivência  (19 concursos, 23 tópicos)
```
Matéria 6. id: "servico-social.servico-social-sociojuridico-familia". subject: "Serviço Social". Título: "Serviço social no sociojurídico: família, adoção e convivência".
Cubra: adoção e direito às origens, acolhimento familiar e institucional, convivência familiar e comunitária, guarda e destituição do poder familiar, depoimento especial e escuta especializada, alienação parental, atuação em varas de família e infância.
NÃO repita (já existe): perícia e estudo social (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei da Alienação Parental." | "Proteção do denunciante e tratamento seguro dos relatos: medidas preventivas, protetivas e reparadoras e competências para sua adoção." | "Convenção Interamericana sobre Conflito de Leis em matéria de Adoção de Crianças e Adolescentes." | "Escuta especializada e depoimento especial (Lei nº 13.431/2017): distinção, finalidades e procedimentos." | "Trabalho sob pressão e adoção ágil de procedimentos para garantir a segurança das operações." | "Lei nº. 12.318/2010 (Alienação Parental)." | "Resolução CNJ nº 289/2019 (Dispõe sobre a implantação e funcionamento do Sistema Nacional de Adoção e Acolhimento – SNA e dá outras providências)." | "Lei Estadual 14.167 de 10 de janeiro de 2002 (Adoção do Pregão no âmbito do Estado de Minas Gerais)." | "Lei Federal 13.431/2017 – escuta especializada de crianças e adolescentes vítimas e/ou testemunhas de violência sexual." | "Provimento nº 85/2019 (Dispõe sobre a adoção dos Objetivos de Desenvolvimento Sustentável, da Agenda 2030, pelas Corregedorias do Poder Judiciário e pelo Serviço Extrajudicial)." | "Debate sobre alienação parental e Plano Nacional de Convivência Familiar e Comunitária." | "Adoção e direito às origens."
```

### 7. Engenharia de alimentos: operações unitárias, embalagens e processos  (18 concursos, 28 tópicos)
```
Matéria 7. id: "engenharia.engenharia-alimentos-operacoes". subject: "Engenharia". Título: "Engenharia de alimentos: operações unitárias, embalagens e processos".
Cubra: operações unitárias (mistura, moagem, separação, secagem, evaporação, refrigeração e congelamento), tratamentos térmicos, layout industrial, embalagens e materiais, armazenamento, controle de qualidade e segurança na indústria de alimentos, sustentabilidade.
NÃO repita (já existe): nutrição e UAN (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Secagem, beneficiamento e armazenamento de sementes e grãos." | "Secagem e armazenamento." | "Operações Unitárias na Indústria de Alimentos." | "Evaporação." | "Mistura e homogeneização." | "Layout industrial." | "Sistemas de gestão da qualidade na indústria de alimentos." | "Embalagens e Armazenamento de Alimentos." | "Materiais de embalagem." | "Sustentabilidade e Gestão na Indústria de Alimentos." | "Princípios de rotulagem, informações obrigatórias e requisitos técnicos aplicáveis às embalagens." | "Classificação de materiais radioativos; Embalagens e sinalização; Procedimentos de transporte seguro; CNEN NN (transporte de materiais radioativos)."
```

### 8. Metodologias ativas e técnicas de gestão da sala de aula  (17 concursos, 328 tópicos)
```
Matéria 8. id: "educacao.metodologias-ativas-gestao-sala-aula". subject: "Educação". Título: "Metodologias ativas e técnicas de gestão da sala de aula".
Cubra: metodologias ativas (sala de aula invertida, aprendizagem baseada em projetos e problemas, rotação por estações, ensino híbrido, STEAM), técnicas de ensino e de gestão da sala de aula (planejar para o erro, cultura do erro, tempo de espera, todo mundo escreve, sem escapatória, certo é certo, elogio preciso, expectativas visíveis), avaliação formativa, rubricas e devolutivas, sem reproduzir texto de livros.
NÃO repita (já existe): didática geral e LDB/BNCC (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "BACICH, Lilian; MORAN, José. Metodologias ativas para uma educação inovadora: uma abordagem teórico-prática. Porto Alegre: Penso, 2017." | "BACICH, Lilian; HOLANDA, Leandro. STEAM em sala de aula: aprendizagem baseada em projetos integrando conhecimentos na educação básica. Porto Alegre: Penso, 2020." | "LEMOV, Doug. Aula nota 10 3.0: 63 técnicas para melhorar a gestão da sala de aula. Porto Alegre: Penso, 2023." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 2 – Técnica 2 – Planeje para o erro (p. 49–53)." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 2 – Técnica 4 – Planeje em dobro (p. 58–62)." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 3 – Técnica 6 – Substitua o autorrelato (p. 76–82)." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 3 – Técnica 12 – Cultura do erro (p. 111–120)." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 4 (p. 139–173) – Técnica 15 – Sem escapatória." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 4 (p. 139–173) – Técnica 16 – Certo é certo." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 4 (p. 139–173) – Técnica 17 – Puxe mais." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 5 – Técnica 26 – Arremate (p. 228–233)." | "LEMOV, Doug. Aula nota 10 3.0 – Capítulo 7 – Técnica 33 – Tempo de espera (p. 276–282)."
```

### 9. Esportes: modalidades, regras e treinamento  (17 concursos, 41 tópicos)
```
Matéria 9. id: "educacao-fisica.esportes-regras-treinamento". subject: "Educação Física". Título: "Esportes: modalidades, regras e treinamento".
Cubra: esportes coletivos (futebol, vôlei, basquete, handebol) e individuais (atletismo, natação, ginástica, lutas), regras básicas, fundamentos técnicos e táticos, iniciação esportiva, periodização e treinamento desportivo, organização de competições.
NÃO repita (já existe): fisiologia do exercício e escola (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "A crítica da economia política, o trabalho, as lutas e os movimentos sociais." | "Lei nº 14.193, de 06/08/2021 - SAF (Sociedade Anônima do Futebol)." | "Atletismo: fundamentos e regras básicas." | "Basquetebol: fundamentos, técnicas e regras." | "Esportes coletivos e individuais." | "Futebol e futsal: fundamentos e regras." | "Handebol: fundamentos, técnicas e regras." | "Lutas e práticas corporais de combate." | "Voleibol: fundamentos, técnicas e regras." | "Fundamentos e tipos de lutas." | "Pluralismos jurídicos. Seletividade dos direitos humanos, paradoxos, fragilidades e contradições. Movimentos sociais: lutas, retrocessos e resistências." | "Convenção Interamericana sobre Deveres e Direitos dos Estados nos casos de Lutas Civis."
```

### 10. Fruticultura, olericultura e pós-colheita  (17 concursos, 33 tópicos)
```
Matéria 10. id: "agronomia.fruticultura-olericultura-pos-colheita". subject: "Agronomia". Título: "Fruticultura, olericultura e pós-colheita".
Cubra: implantação de pomares, propagação, poda, condução e tratos culturais, raleio, florescimento e frutificação, principais frutíferas e hortaliças do Brasil, cultivo protegido, colheita e fisiologia pós-colheita, armazenamento e qualidade.
NÃO repita (já existe): proteção vegetal (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Métodos de propagação, plantio, preparo do solo, tratos culturais, colheita, pós-colheita e comercialização das culturas do milho, feijão de corda, mandioca e batata doce e cana de açúcar." | "Biotecnologia vegetal. Micropropagação vegetal. Embriogênese somática. Protoplastos de células vegetais. Métodos de transformação genética de plantas: bombardeamento e eletroporação." | "Acústica: elementos de onda, amplitude, frequência, período, velocidade de propagação do som, timbre, altura, eco, reverberação." | "Pós-colheita e Processamento de Produtos Agrícolas." | "Processamento de frutas, hortaliças e grãos." | "PARTE 2: Fundamentos de Produção Vegetal e Fruticultura: Importância econômica e social da fruticultura." | "Fenologia e ecofisiologia das plantas frutíferas." | "Propagação e Implantação de Pomares: Métodos de propagação sexuada e assexuada." | "Planejamento e implantação de pomares comerciais." | "Nutrição mineral de frutíferas." | "Poda, Condução e Tratos Culturais: Tipos de poda." | "Raleio de frutos."
```

### 11. Bioquímica: metabolismo e biologia molecular  (16 concursos, 28 tópicos)
```
Matéria 11. id: "bioquimica.metabolismo-biologia-molecular". subject: "Biologia". Título: "Bioquímica: metabolismo e biologia molecular".
Cubra: biomoléculas, enzimas e cinética enzimática, glicólise, ciclo de Krebs, cadeia respiratória, metabolismo de lipídios e aminoácidos, regulação metabólica, sinalização celular, replicação, transcrição, tradução e técnicas moleculares.
NÃO repita (já existe): bioquímica clínica (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Metabolismo." | "Biologia e fisiologia de organismos aquáticos: crescimento, reprodução, desenvolvimento, metabolismo e adaptações aos ambientes de cultivo." | "Fontes, funções e metabolismo dos nutrientes e alimentos." | "Tecido ósseo: metabolismo, biomecânica e estrutura." | "Bioenergética. Glicólise e gliconeogênese. Ciclo de Krebs. Oxidação dos ácidos graxos. Síntese de ácidos graxos. Cadeia de transporte de elétrons e fosforilação oxidativa." | "Metabolismo e regulação da utilização de energia. Metabolismo de carboidratos, lipídios, aminoácidos, bases nitrogenadas e proteínas." | "Metabolismo e crescimento microbiano." | "Digestão e metabolismo." | "Diferenciação entre lesões acidentais e não acidentais, toxicologia forense: alimentos, medicamentos, venenos, seus modos de ação, metabolismo, eliminação, sinais clínicos gerados." | "Regulação hormonal do metabolismo da água e eletrólitos." | "Nutrientes: definição, propriedades, biodisponibilidade, digestão, absorção, metabolismo e interação." | "Doenças do metabolismo ósseo e mineral."
```

### 12. Instalações de vapor, caldeiras, refrigeração e ar comprimido  (15 concursos, 19 tópicos)
```
Matéria 12. id: "engenharia-mecanica.vapor-caldeiras-refrigeracao". subject: "Engenharia". Título: "Instalações de vapor, caldeiras, refrigeração e ar comprimido".
Cubra: geradores de vapor e caldeiras (tipos, operação, segurança e NR-13), distribuição de vapor, compressores e ar comprimido, refrigeração e ar condicionado (ciclos, cargas térmicas), bombas e ventiladores, manutenção.
NÃO repita (já existe): termodinâmica e fluidos (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Noções de equipamentos de caldeiraria." | "Sistemas de controle para ar-condicionado e refrigeração: BMS, protocolos abertos (BACnet, Modbus), sensoriamento/IoT, medição e verificação de desempenho." | "Vasos de pressão e caldeiras." | "Remoção de material particulado, de gases e vapores." | "Pressão de Vapor do Ar Úmido e Pressão de Vapor Saturante do Ar Úmido; Calor Latente; Processo Isobárico; Processo Adiabático; Umidade Relativa, Umidade Específica e Razão de Mistura." | "Cores utilizadas para instalações de fluidos (água para combate a incêndio, vapor, gases, etc)." | "Técnicas de remediação de solos e águas subterrâneas contaminadas: escavação e disposição, biorremediação, fitorremediação, extração de vapores do solo (SVE) e barreiras reativas permeáveis." | "Sistema de Refrigeração de Motores: componentes do sistema (radiador, bomba d'água, termostato, ventilador). Cálculo de capacidade de troca térmica. Fluidos refrigerantes e sua manutenção." | "Análise de imagens nos canais visível, infravermelho e vapor d’água." | "Segunda Lei da termodinâmica: máquinas térmicas, produção de energia a partir do calor, máquinas de combustão interna, ciclo de Otto, refrigerador de Carnot e refrigeração a ar." | "Materiais para Serviços Típicos: recomendações especiais para aparelhos de troca de calor, serviço com água doce, água salgada, ar comprimido, vapor e hidrocarbonetos." | "Instalações de vapor."
```

### 13. Legislação urbanística municipal: uso e ocupação do solo, obras e posturas  (12 concursos, 80 tópicos)
```
Matéria 13. id: "legislacao-municipal.uso-ocupacao-solo-obras-posturas". subject: "Legislação Municipal". Título: "Legislação urbanística municipal: uso e ocupação do solo, obras e posturas".
Cubra: conteúdo típico de leis municipais de zoneamento, parcelamento, uso e ocupação do solo (zonas, categorias de uso, taxa de ocupação, coeficiente de aproveitamento, recuos, gabarito, áreas de expansão e de proteção), código de obras (alvarás, licenças, habite-se, instalações provisórias, demolições, embargos, interdições) e código de posturas (higiene, salubridade, uso de vias e logradouros, feiras, comércio ambulante, publicidade); sem citar lei de município específico.
NÃO repita (já existe): Estatuto da Cidade (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Salubridade." | "Instalações provisórias." | "Categorias de uso." | "Áreas de expansão urbana." | "Controle da ocupação." | "Interdições." | "Demolições." | "Da aplicação provisória de interdições de direitos e medidas de segurança." | "Limpeza e conservação de ruas, praças e logradouros." | "Licença para Demolições." | "Lei nº 4.367, de 11 de julho de 2024 (regulamenta os adicionais de insalubridade e periculosidade de que trata o art. 75 da Lei Complementar nº 39, de 29 de dezembro de 1993)." | "Salubridade ambiental."
```

### 14. Técnico e auxiliar em saúde bucal  (12 concursos, 33 tópicos)
```
Matéria 14. id: "odontologia.tecnico-auxiliar-saude-bucal". subject: "Odontologia". Título: "Técnico e auxiliar em saúde bucal".
Cubra: atribuições legais de TSB e ASB, organização do consultório e ergonomia, instrumentação e manutenção de equipamentos, biossegurança e esterilização, materiais odontológicos, radiologia odontológica básica, educação em saúde bucal e prevenção, primeiros socorros.
NÃO repita (já existe): clínica odontológica (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Saúde bucal." | "Diretrizes assistenciais da saúde bucal em Belo Horizonte." | "Emergências no Consultório Odontológico." | "Atribuições do Auxiliar em Saúde Bucal conforme legislação profissional." | "Registro de dados e participação na análise de informações relacionadas ao controle administrativo em saúde bucal." | "Auxílio e instrumentação do cirurgião -dentista e do Técnico em Saúde Bucal durante intervenções clínicas, sob supervisão." | "Indicações farmacológicas na abordagem terapêutica dos principais agravos em saúde bucal." | "Perfil e Competências Profissionais do Técnico em Saúde e auxiliar em Saúde Bucal." | "Urgências e Emergências em Odontologia: suporte básico de vida; atendimento às urgências médicas no consultório odontológico; complicações anestésicas; reações alérgicas; emergências clínicas." | "Saúde Bucal na Atenção Primária: ações integradas; prevenção; educação em saúde bucal; trabalho em equipe." | "Política Nacional de Saúde Bucal: Programa Brasil Sorridente; organização da atenção em saúde bucal; equipes de Saúde Bucal na Estratégia Saúde da Família; linhas de cuidado em saúde bucal." | "Promoção e Educação em Saúde Bucal: educação em saúde; prevenção das doenças bucais; escovação supervisionada; uso racional do flúor; ações coletivas em saúde."
```

### 15. História da arte e artes visuais  (12 concursos, 33 tópicos)
```
Matéria 15. id: "artes.historia-arte-artes-visuais". subject: "Artes". Título: "História da arte e artes visuais".
Cubra: arte pré-histórica à contemporânea, arte brasileira (colonial, modernismo, arte contemporânea), linguagens visuais, leitura de imagem, cultura popular e patrimônio, arte e tecnologia.
NÃO repita (já existe): ensino de arte (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Diálogo da Arte brasileira com a Arte internacional." | "A História da Arte no Brasil." | "A História da Arte Arquitetônica." | "Metodologias do Ensino de Artes Visuais." | "Arte brasileira e suas manifestações culturais." | "Arte contemporânea e novas linguagens artísticas." | "Artes visuais: desenho, pintura, gravura e escultura." | "História da arte geral." | "Artes visuais, música e expressão corporal." | "História da arte e manifestações culturais." | "As dimensões dos conhecimentos das Artes visuais, da Dança, da Música e do Teatro." | "Linguagens das artes visuais, música, teatro e dança."
```

### 16. Agrimensura, georreferenciamento e cadastro territorial  (12 concursos, 26 tópicos)
```
Matéria 16. id: "engenharia.agrimensura-georreferenciamento". subject: "Engenharia". Título: "Agrimensura, georreferenciamento e cadastro territorial".
Cubra: levantamentos topográficos e geodésicos, GNSS, sistemas de referência, georreferenciamento de imóveis rurais e certificação, cadastro territorial multifinalitário, parcelamento e regularização fundiária, ajustamento de observações.
NÃO repita (já existe): geoprocessamento (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Engenharia rural. Topografia. Geoprocessamento. Georreferenciamento." | "Cadastro Territorial Multifinalitário - CTM." | "Levantamento Topográfico Cadastral: Confecção de plantas planialtimétricas georreferenciadas. Elaboração de memorial descritivo." | "Integração GNSS e Estação Total." | "Topografia e Georreferenciamento Aplicados à Agricultura." | "Georreferenciamento e certificação de imóveis rurais (Lei nº 10.267/2001 e Decreto nº 4.449/2002)." | "Georreferenciamento: normas para certificação de imóveis; precisão e acurácia posicional (Decreto nº 9.310/2018, NBR 17047, NBR 13133 e NBR 14166)." | "Georreferenciamento de Imóveis Urbanos." | "Gestão do Cadastro Territorial." | "Atividades Interdisciplinares referentes a elaboração de Plano Diretor no âmbito da Agrimensura; Agrimensura Legal." | "Aplicações da Matemática em problemas relacionados à Amazônia, estatísticas socioambientais, georreferenciamento, cartografia regional, dados demográficos, econômicos e ambientais do Pará." | "Missões para observação da Terra, de telecomunicações, GNSS e científicas."
```

### 17. Ensino de química  (12 concursos, 23 tópicos)
```
Matéria 17. id: "educacao.ensino-quimica". subject: "Educação". Título: "Ensino de química".
Cubra: química como componente curricular (BNCC e itinerários), experimentação e laboratório escolar, contextualização (química e sociedade, CTSA), avaliação da aprendizagem em química, história da química no ensino, concepções alternativas dos estudantes.
NÃO repita (já existe): química (conteúdo) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Avaliação de aprendizagem do conhecimento químico." | "Ensino de química: conhecimento científico e habilidade didática no ensino de química; construção do conhecimento no ensino da química (abordagens metodológicas)." | "Metodologias de Ensino de Química na Educação Profissional e Tecnológica: Planejamento de aulas teóricas e experimentais." | "Experimentação, Laboratório e Práticas Investigativas: Fundamentos da experimentação no ensino de Física e Ciências." | "Experimentação de baixo custo." | "Métodos de Pesquisa em Psicologia: Experimentação; Pesquisa científica: Métodos qualitativos e quantitativos; Formulação de hipóteses; Análise e interpretação de dados." | "Experimentação agrícola." | "Ensino de Química: conhecimento científico e habilidade didática no ensino de Química. A construção do conhecimento no ensino da Química: abordagens metodológicas." | "Recursos didáticos no ensino de Química (utilizados em sala de aula e em laboratório, incluindo conhecimentos básicos de técnicas, materiais e normas de segurança laboratoriais)." | "Avaliação de aprendizagem aplicada ao conhecimento químico." | "Recursos didáticos e práticas experimentais no ensino de Física: atividades em sala e laboratório, experimentação investigativa, materiais didáticos, tecnologias educacionais e normas de segurança." | "Competências e habilidades propostas para a Química como componente curricular, com base na Base Nacional Comum Curricular (BNCC) e no Documento Curricular Referencial do Ceará (DCRC)."
```

### 18. Rotulagem, análise sensorial e tecnologia de alimentos  (12 concursos, 16 tópicos)
```
Matéria 18. id: "nutricao.rotulagem-analise-sensorial-tecnologia". subject: "Nutrição". Título: "Rotulagem, análise sensorial e tecnologia de alimentos".
Cubra: rotulagem nutricional e frontal, informação nutricional complementar, alimentos para fins especiais, análise sensorial (métodos discriminativos e afetivos), composição e tecnologia de alimentos, aditivos, compras e armazenamento de gêneros.
NÃO repita (já existe): técnica dietética e UAN (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Aditivos alimentares." | "Análise sensorial." | "Bromatologia: aditivos alimentares, condimentos, pigmentos, estudo químico-bromatológico dos alimentos (proteínas, lipídios e carboidratos), vitaminas, minerais, bebidas." | "Análise Sensorial e Controle de Qualidade." | "Bromatologia. Aditivos alimentares: condimentos e pigmentos. Estudo químico‐bromatológico dos alimentos: proteínas, lipídios e carboidratos. Vitaminas. Minerais. Bebidas." | "Tecnologia de Alimentos para Animais: Produção e conservação de rações, aditivos alimentares, controle de qualidade." | "Bromatologia: aditivos alimentares." | "RDC nº 429/2020 – Rotulagem nutricional de alimentos embalados." | "Bromatologia: aditivos alimentares; condimentos; pigmentos." | "Rotulagem nutricional e informação nutricional complementar." | "Análise sensorial em alimentos."
```

### 19. Geologia: hidrogeologia, mapeamento e petrologia  (11 concursos, 27 tópicos)
```
Matéria 19. id: "geologia.hidrogeologia-cartografia-petrologia". subject: "Geologia". Título: "Geologia: hidrogeologia, mapeamento e petrologia".
Cubra: mineralogia e petrologia (ígneas, sedimentares, metamórficas), geologia estrutural, cartografia geológica, hidrogeologia (porosidade, permeabilidade, aquíferos, testes de bombeamento), geologia ambiental e geotecnia.
NÃO repita (já existe): geologia e mineração (noções) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Aquíferos, aquitargo e aquicludes." | "Mineralogia." | "Técnicas de cartografia geológica." | "Mapeamento geológico e mapeamento hidrogeológico para identificação dos aquíferos, estudo hidrodinâmico e balanço hídrico." | "Testes de bombeamento e recuperação." | "Vulnerabilidade e proteção de aquíferos." | "Ações compensadoras e mitigadoras, aquífero, avaliação de impacto, bacia hidrográfica, biodiversidade, biosfera e biota." | "Mineralogia e petrografia: conceitos fundamentais." | "Hidrogeologia: Aquíferos: tipos; propriedades. Água subterrânea: fluxo; recarga; captação. Qualidade da água: parâmetros básicos. Monitoramento de águas subterrâneas." | "Mineralogia. Gemologia." | "Petrologia ígnea." | "Petrologia sedimentar."
```

### 20. BNCC Computação, pensamento computacional e cultura digital  (10 concursos, 114 tópicos)
```
Matéria 20. id: "educacao.bncc-computacao-cultura-digital". subject: "Educação". Título: "BNCC Computação, pensamento computacional e cultura digital".
Cubra: BNCC Computação (eixos pensamento computacional, mundo digital e cultura digital), pensamento computacional (decomposição, padrões, abstração, algoritmos), robótica educacional, cidadania digital, segurança e ética no uso de tecnologias, saberes digitais docentes.
NÃO repita (já existe): EAD e tecnologias na educação (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Princípios básicos do pensamento computacional aplicados à resolução de problemas." | "BNCC Computação. Mundo digital: compreensão do universo digital, incluindo a estrutura e o funcionamento dos sistemas e o impacto da tecnologia na sociedade." | "Cultura digital: uso consciente, crítico e ético das tecnologias digitais, abordando a segurança, a privacidade e a responsabilidade social." | "Pensamento computacional: capacidade de analisar, decompor e resolver problemas de forma lógica, usando estratégias como decomposição, reconhecimento de padrões, algoritmos e abstração." | "BNCC COMPUTACIONAL: Parecer CNE/CEB nº 2/2022 e Resolução CNE/CEB nº 1/2022." | "Três Eixos Estruturantes: Pensamento Computacional, Mundo Digital, Cultura Digital." | "Língua Espanhola, mundo do trabalho, cultura digital e Educação Profissional e Tecnológica." | "Educação Digital e Cultura Digital: letramento digital; cidadania digital; pensamento computacional; criatividade e inovação; uso ético e responsável das tecnologias." | "Noções de programação e pensamento computacional: algoritmos; lógica de programação; programação em blocos; resolução de problemas." | "Cultura digital." | "Relação entre os gêneros em circulação, mídias e práticas da cultura digital." | "Cultura e cidadania digital."
```

### 21. Registro civil das pessoas naturais e tabelionato de notas  (10 concursos, 25 tópicos)
```
Matéria 21. id: "direito-notarial-registral.registro-civil-tabelionato". subject: "Direito". Título: "Registro civil das pessoas naturais e tabelionato de notas".
Cubra: registro de nascimento, casamento e óbito, averbações e retificações, reconhecimento de paternidade e socioafetividade, mudança de nome e gênero, habilitação para casamento, escrituras públicas, procurações, inventário e divórcio extrajudiciais, atas notariais.
NÃO repita (já existe): registro de imóveis e protesto (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Averbações." | "Criança e do adolescente vítima ou testemunha de violência, autorização de viagem e registro civil." | "Requalificação civil e retificação de registro civil." | "Ações declaratória e negatória de vínculo parental (em vida e póstuma, biológico, civil e socioafetivo)." | "Central de Serviços Eletrônicos Compartilhados de Registro Civil das Pessoas Jurídicas." | "Portaria nº 2.659/2023 (Regulamenta o ressarcimento de atos gratuitos em favor das serventias de registro civil do Estado)." | "Provimento nº 93/2013 e suas alterações (Dispõe sobre o funcionamento das Unidades Interligadas de Registro Civil das Pessoas Naturais em maternidades e estabelecimentos de saúde que realizam partos)." | "Provimento nº 274/2022 (Regulamenta a utilização da REDESIM nos Cartórios de Registro Civil das Pessoas Jurídicas no âmbito do Estado de Mato Grosso do Sul)." | "Recomendação nº 6/2012 (Uso de papel de segurança unificado para emissão de certidões pelos Ofícios de Registro Civil das Pessoas Naturais)." | "Resolução nº 155/2012 e suas alterações (Traslado de certidões de registro civil de pessoas naturais emitidas no exterior)." | "Resolução nº 402/2021 (Ações de caráter informativo, no âmbito do Serviço de Registro Civil das Pessoas Naturais, para melhor preparação para o casamento civil, e dá outras providências)." | "Provimento nº 14/2011 (Emissão de certidões pelos Ofícios de Registro Civil das Pessoas Naturais em papel de segurança unificado pela Casa da Moeda do Brasil)."
```

### 22. Polímeros e ciência dos materiais  (10 concursos, 23 tópicos)
```
Matéria 22. id: "engenharia.polimeros-materiais". subject: "Engenharia". Título: "Polímeros e ciência dos materiais".
Cubra: estrutura e classificação de polímeros, polimerização, propriedades térmicas e mecânicas, processamento (extrusão, injeção), cerâmicos e compósitos, ensaios de materiais, degradação e reciclagem.
NÃO repita (já existe): materiais metálicos (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Polímeros sintéticos e naturais." | "Polímeros sintéticos: polímeros de adição (polietileno, poliestireno, PVC e teflon) e polímeros de condensação (poliéster e poliamida)." | "Materiais modernos Cristais líquidos. Polímeros. Biomateriais. Cerâmicas. Supercondutividade Filmes finos." | "Fundamentos de biologia molecular e técnicas moleculares: reação da polimerase em cadeia (PCR), teste ácido nucleico (NAT) e PCR em tempo real (PCR-TR)." | "Polímeros: classificação; propriedades; aplicações." | "Hemorragia digestiva alta não varicosa - estratificação (Glasgow-Blatchford, Rockall - noções), hemostasia por injeção, térmica e clipes." | "Angiotécnica: substâncias para injeção vascular e do sistema linfático, modelagem das cavidades cardíacas e diafanização do coração." | "Processos de injeção de termoplásticos." | "Extrusão de termoplásticos." | "Extrusão sopro." | "Injeção sopro." | "Processamento de compósitos de matriz polimérica."
```

### 23. Cirurgia torácica  (10 concursos, 19 tópicos)
```
Matéria 23. id: "medicina.cirurgia-toracica". subject: "Medicina". Título: "Cirurgia torácica".
Cubra: avaliação pré-operatória pulmonar, toracotomias e videotoracoscopia, câncer de pulmão (estadiamento e ressecções), derrames e empiema, pneumotórax, trauma torácico, doenças do mediastino e da parede torácica, transplante pulmonar.
NÃO repita (já existe): cirurgia plástica/torácica (noções) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Mediastino e Pleura." | "Pré e pós-operatório em cirurgia torácica." | "Avaliação e manejo inicial de dor torácica, dispneia, palpitações e edema." | "Avaliação de tosse, dispneia, sibilância, dor torácica e hemoptise." | "Reconstruções da parede torácica." | "Dor torácica." | "Esternotomia, toracotomia e acessos minimamente invasivos." | "Videolaparoscopia e videotoracoscopia pediátricas." | "Pneumotórax e derrames pleurais na criança, empiema pleural e drenagem torácica em Pediatria." | "Mediastinite e doenças cirúrgicas do mediastino." | "Cirurgia torácica pediátrica." | "Sistemas orgânicos específicos: pele e tecido celular subcutâneo; tireoide e paratireoide; tumores da cabeça e pescoço; parede torácica; pleura; pulmão; e, mediastino."
```

### 24. Anatomia patológica, histotecnologia e citopatologia  (10 concursos, 16 tópicos)
```
Matéria 24. id: "medicina.anatomia-patologica-histopatologia". subject: "Medicina". Título: "Anatomia patológica, histotecnologia e citopatologia".
Cubra: patologia geral (adaptação, degeneração, necrose, inflamação, neoplasias), macroscopia e microscopia, processamento histológico e colorações, imuno-histoquímica, citopatologia, necropsia e biossegurança em laboratório de patologia.
NÃO repita (já existe): análises clínicas (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "CAPS, eMulti e unidades básicas; transtornos mentais comuns e graves; manejo em situações de crise; psicopatologia geral; depressão, ansiedade, suicídio e uso abusivo de substâncias." | "Técnicas histopatológica." | "Investigação Histopatológica." | "Patologia clínica e anatomia patológica veterinária." | "Patologia geral: adaptação, degeneração, necrose e apoptose, inflamação e reparo, alterações vasculares, imunidade, neoplasia e noções de genética e patologia molecular." | "Técnica anatomopatológica: fixação, macroscopia, histotécnica, citopatologia, congelação, imuno-histoquímica, patologia molecular, controle de qualidade e principais artefatos." | "Interpretação de Exames: Exames laboratoriais; Ultrassonografia (noções); Exames hormonais; Exames citológicos e histopatológicos." | "Interpretação de Exames: Exames laboratoriais; Exames de imagem; Histopatologia." | "Anatomia Patológica: Diagnóstico de doenças através da necropsia, estudo de alterações morfológicas em tecidos, métodos histopatológicos." | "Noções de citopatologia." | "Patologia geral veterinária: lesão e morte celular, degenerações, necrose, apoptose, distúrbios circulatórios, inflamação, distúrbios do crescimento e neoplasias."
```

### 25. Limpeza urbana e operação de serviços de saneamento  (9 concursos, 13 tópicos)
```
Matéria 25. id: "engenharia.limpeza-urbana-servicos-saneamento". subject: "Engenharia". Título: "Limpeza urbana e operação de serviços de saneamento".
Cubra: limpeza urbana (varrição, capina, manejo de praias, canais, feiras e mercados), coleta e transporte de resíduos, operação e controle operacional de sistemas de água e esgoto, indicadores de desempenho, regulação dos serviços, manutenção de redes e equipamentos públicos.
NÃO repita (já existe): saneamento e resíduos (projeto) já existem.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Parâmetros de controle operacional: controle de níveis, vazão, pressão e monitoramento de unidades operacionais." | "Parâmetros operacionais: pressão, vazão, tempo de funcionamento (horímetro) e controle operacional do sistema." | "Capina e roçagem." | "Feiras de arte, crítica de arte, etc. (local, regional, nacional e internacional)." | "Varrição, lavagem e higienização das instalações, salas, pátios, banheiros e os equipamentos." | "Noções básicas sobre auxílio nos trabalhos ligados à área de capina, construção civil, pintura e outras correlatas às atribuições do cargo." | "Varrição de calçadas." | "Manejo de praias, canais, feiras, mercados, áreas públicas e equipamentos urbanos." | "Controle operacional."
```

### 26. Rotinas contábeis no setor público: conciliações, restos a pagar e encerramento  (9 concursos, 11 tópicos)
```
Matéria 26. id: "contabilidade-publica.rotinas-conciliacoes-encerramento". subject: "Contabilidade Pública". Título: "Rotinas contábeis no setor público: conciliações, restos a pagar e encerramento".
Cubra: conciliação bancária e de contas, despesas de exercícios anteriores, restos a pagar processados e não processados, duodécimo, inscrição e baixa, encerramento do exercício, reflexos patrimoniais, demonstrativos fiscais (RREO e RGF).
NÃO repita (já existe): PCASP e demonstrações (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Reflexo patrimonial das despesas de exercícios anteriores." | "Fiscalização financeira e orçamentária da Câmara: limites de gastos, devolução de duodécimos e responsabilidade fiscal." | "Lançamentos de encerramento do exercício financeiro." | "Conciliação e Análise de Contas: Conciliação bancária. Análise e conciliação de contas patrimoniais e de resultado. Técnicas de conciliação contábil." | "Procedimentos de encerramento do exercício." | "Reflexo patrimonial de despesas de exercícios anteriores." | "Análise e Conciliação de Contas: Conciliações bancárias; Ajustes contábeis; Identificação e correção de erros; Controle de saldos." | "Encerramento do exercício financeiro e prestação de contas anual."
```

### 27. Endoscopia digestiva  (8 concursos, 27 tópicos)
```
Matéria 27. id: "medicina.endoscopia-digestiva". subject: "Medicina". Título: "Endoscopia digestiva".
Cubra: equipamentos e preparo, endoscopia alta e colonoscopia diagnósticas e terapêuticas, hemostasia, polipectomia e mucosectomia, CPRE e vias biliares, gastrostomia, próteses endoscópicas, complicações.
NÃO repita (já existe): gastroenterologia (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Preparo para exame de endoscopia digestiva alta e colonoscopia." | "Endoscopia digestiva alta, baixa e retossigmoidoscopia." | "Magnificação de imagens, biópsias, citologia e corantes em endoscopia digestiva." | "Colangiografia retrógrada endoscópica, cápsula endoscópica, enteroscopia, ecoendoscopia e endoscopia pediátrica." | "Neuroendoscopia." | "Indicação e interpretação de exames laboratoriais, endoscopia digestiva, colonoscopia e exames de imagem." | "Colonoscopia na urgência." | "Cateterismo da papila e esfincterotomia endoscópica." | "Procedimentos endoscópicos: Ecoendoscopia." | "Ultrassom endoscópico." | "Gastrostomia e passagem de cateter enteral." | "Posicionamento de endopróteses esofagianas, biliares e enterais."
```

### 28. Gastronomia e cozinha profissional  (8 concursos, 24 tópicos)
```
Matéria 28. id: "gastronomia.cozinha-profissional". subject: "Gastronomia". Título: "Gastronomia e cozinha profissional".
Cubra: técnicas de corte e de cocção, fundos e molhos, brigada de cozinha, ficha técnica e custo de preparações, panificação e confeitaria, cozinha brasileira regional, boas práticas e segurança alimentar, gestão de cozinha.
NÃO repita (já existe): técnica dietética (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Procedimentos básicos para a realização de serviços de: copa, cozinha, banheiro, lavanderia, pátio." | "Técnicas de pré-preparo, preparo e cocção." | "Ficha técnica." | "Banco de leite, lactário e sondário hospitalar, cozinha dietética: características físicas, normas, procedimentos, equipamentos." | "Nutrição básica e cozinha profissional." | "Gastronomia saudável e sustentável." | "Gerência de cozinhas." | "Técnicas de cozinha." | "Cozinha asiática." | "Cozinha mediterrânea." | "Ervas, especiarias e molhos." | "Fundamentos de panificação e confeitaria."
```

### 29. Alergia e imunologia clínica  (8 concursos, 18 tópicos)
```
Matéria 29. id: "medicina.alergia-imunologia-clinica". subject: "Medicina". Título: "Alergia e imunologia clínica".
Cubra: mecanismos de hipersensibilidade, asma, rinite e conjuntivite alérgicas, dermatite atópica, urticária e angioedema, alergia alimentar e a medicamentos, anafilaxia, imunodeficiências primárias, testes alérgicos e imunoterapia.
NÃO repita (já existe): reumatologia e imunologia (noções) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Imunoterapia passiva e patógenos causadores de infecções hospitalares." | "Hipersensibilidade dentinária: diagnóstico e tratamento." | "Neoplasias – aspectos gerais: Biologia do Tumor e Marcadores Tumorais Imunologia e Imunoterapia Tumoral Melanoma e Neoplasias Cutâneas Malignas Sarcoma de Tecidos Moles Tumores Ósseos." | "Preparação de extratos alérgicos." | "Doenças autoimunes com manifestações alérgicas." | "Diagnóstico diferencial entre doenças alérgicas e infecciosas em adultos." | "Alergia a contrastes iodados e anestésicos." | "Farmacovigilância em alergia e imunologia." | "Avaliação de alergias ocupacionais em ambientes industriais." | "Fundamentos de imunologia clínica e mecanismos das reações de hipersensibilidade." | "Anamnese dirigida, identificação de desencadeantes e diagnóstico diferencial das doenças alérgicas." | "Identificação inicial de sinais sugestivos de imunodeficiência e critérios de encaminhamento."
```

### 30. Ensino de física  (8 concursos, 15 tópicos)
```
Matéria 30. id: "educacao.ensino-fisica". subject: "Educação". Título: "Ensino de física".
Cubra: física como componente curricular, experimentação de baixo custo, modelagem e simulações, contextualização e CTSA, concepções alternativas, avaliação em física, história e filosofia da ciência no ensino.
NÃO repita (já existe): física (conteúdo) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Metodologia e Abordagens no Ensino de Física: Ensino de Física, abordagem CTS/CTSA, uso de experimentos/laboratório, modelagem científica, analogias e História e Natureza da Ciência." | "Decolonialidade e Relações Étnico-Raciais (ERER) na Física: Decolonialidade no ensino de física, saberes não ocidentais/afrodiaspóricos/indígenas e desconstrução do racismo científico." | "Conhecimento científico, habilidade didática e abordagens metodológicas no ensino de Física." | "Ensino de física." | "Ensino de física: Conhecimento científico, habilidade didática e práticas de ensino." | "Ensino de física: Abordagens metodológicas." | "O ensino de Física para estudantes com deficiência."
```

### 31. Câmara Municipal: regimento interno e processo legislativo municipal  (8 concursos, 8 tópicos)
```
Matéria 31. id: "legislacao-municipal.regimento-camara-municipal". subject: "Legislação Municipal". Título: "Câmara Municipal: regimento interno e processo legislativo municipal".
Cubra: conteúdo típico de regimentos internos de câmaras municipais: Mesa, presidência, comissões permanentes e temporárias, sessões (ordinárias, extraordinárias, solenes), quórum, proposições (projetos, indicações, requerimentos, moções), tramitação, votação, veto e sanção, fiscalização do Executivo, julgamento das contas do prefeito, prerrogativas e perda do mandato do vereador; sem citar município específico.
NÃO repita (já existe): regimento da Câmara dos Deputados (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei Federal nº 13.460/2017 (Carta de Serviços ao Usuário — Ato da Mesa Diretora nº 05/2026)." | "Direitos e deveres dos Prefeitos e Vereadores." | "Direitos e deveres dos vereadores."
```

### 32. Service desk e suporte técnico  (7 concursos, 13 tópicos)
```
Matéria 32. id: "informatica.service-desk-suporte-itil". subject: "Informática". Título: "Service desk e suporte técnico".
Cubra: atendimento de chamados, níveis de suporte e escalonamento, ferramentas de help desk, gerenciamento de incidentes e requisições (ITIL), base de conhecimento, inventário de ativos, scripts de automação (Shell e PowerShell), acesso remoto seguro.
NÃO repita (já existe): ITIL/governança (noções) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Atendimento remoto: uso de softwares de acesso remoto e segurança na conexão." | "Ferramentas de help desk (GLPI, osTicket, ServiceNow)." | "Escalonamento de chamados entre níveis." | "Atendimento presencial e remoto (telefone, e-mail, chat, acesso remoto)." | "Registro e análise de chamados recorrentes para identificação de problemas." | "Utilização de ferramentas de acesso remoto para suporte." | "Ferramentas de Acesso Remoto e Gerenciamento. Consoles de gerenciamento (iDRAC, iLO), softwares de acesso remoto, controle de inventário e monitoramento de ativos." | "Documentação e Processos Operacionais. Checklists técnicos; registro de incidentes e mudanças; uso de ferramentas de chamados (ex: GLPI)." | "Protocolos e fluxos de regulação: recebimento, registro, classificação administrativa dos chamados, encaminhamento das solicitações e cumprimento das orientações do médico regulador." | "Comunicação com ambulâncias e unidades de saúde: radiocomunicação, comunicação telefônica, transmissão de informações, registro das orientações e acompanhamento dos chamados." | "Softwares de acesso remoto: telnet, SSH, Microsoft Remote Desktop e VNC." | "Instalação de programas e periféricos em computadores. Acesso remoto a computadores."
```

### 33. Arquitetura hospitalar, acessibilidade e conforto ambiental  (7 concursos, 12 tópicos)
```
Matéria 33. id: "arquitetura.arquitetura-hospitalar-acessibilidade". subject: "Arquitetura". Título: "Arquitetura hospitalar, acessibilidade e conforto ambiental".
Cubra: projeto de estabelecimentos assistenciais de saúde (programação físico-funcional, dimensionamento, fluxos, instalações especiais), acessibilidade em edificações (desenho universal e norma técnica de acessibilidade), conforto térmico, acústico e luminoso, saúde e segurança nas edificações.
NÃO repita (já existe): projeto e instalações gerais (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Normas Técnicas (ABNT): NBR 9050 Acessibilidade – Noções sobre acessibilidade em edificações; NBR 16.280:2024 – reformas em edificações – sistema de gestão de reformas – requisitos." | "Acessibilidade em edificações e espaços urbanos." | "Luminotécnica, conforto acústico e térmico." | "Dimensionamento do Edifício Hospitalar: normas do Ministério da Saúde/ANVISA; setores hospitalares, unidades hospitalares." | "Conforto térmico e ambiência animal." | "Ambiência e conforto térmico." | "RDC nº 50 de 21/02/2002 (Dispõe Sobre o Regulamento Técnico para Planejamento, Programação, Elaboração E Avaliação De Projetos Físicos De Estabelecimentos Assistenciais De Saúde)." | "Fundamentos de segurança em estabelecimentos assistenciais de saúde." | "RDC nº 50/2002 – Regulamento Técnico para planejamento, programação, elaboração e avaliação de projetos físicos de estabelecimentos assistenciais de saúde."
```

### 34. Fisioterapia em neurologia periférica, quedas e pé diabético  (7 concursos, 10 tópicos)
```
Matéria 34. id: "fisioterapia.fisioterapia-neuro-funcional-geriatrica". subject: "Fisioterapia". Título: "Fisioterapia em neurologia periférica, quedas e pé diabético".
Cubra: síndromes neurológicas periféricas, lesões de nervos e plexos, prevenção de quedas e avaliação funcional do idoso, pé diabético e úlceras, órteses e próteses de membros, humanização e prática baseada em evidências em fisioterapia.
NÃO repita (já existe): neurofuncional e cardiorrespiratória (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Prevenção de quedas e promoção da autonomia da pessoa idosa." | "Saúde da pessoa idosa: funcionalidade, quedas e uso de múltiplos medicamentos." | "Quedas, fragilidade e prevenção em idosos." | "Atenção do pé diabético." | "Noções de Primeiros Socorros: Atendimento básico em emergências; Cuidados em casos de quedas, engasgos e malestar; Acionamento de ajuda adequada." | "Noções de Primeiros Socorros: Atendimento inicial em emergências; Quedas, convulsões e desmaios; Acionamento de serviços de emergência; Cuidados básicos até atendimento especializado." | "Síndromes Periféricas." | "Amputação: próteses e órteses; e mastectomias." | "Próteses: próteses de membros superiores e inferiores. Componentes protéticos. Encaixes. Sistemas de suspensão. Articulações. Pés protéticos. Mãos e componentes funcionais. Indicações e cuidados."
```

### 35. Ensino de biologia e ciências da natureza  (6 concursos, 17 tópicos)
```
Matéria 35. id: "educacao.ensino-biologia". subject: "Educação". Título: "Ensino de biologia e ciências da natureza".
Cubra: biologia como componente curricular, abordagens metodológicas, recursos didáticos e aulas práticas, educação ambiental e em saúde na escola, temas controversos (evolução, genética), avaliação.
NÃO repita (já existe): biologia (conteúdo) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Prática pedagógica e áreas do conhecimento. Linguagens e suas Tecnologias. Matemática e suas Tecnologias. Ciências da Natureza e suas Tecnologias. Ciências Humanas e Sociais Aplicadas." | "A construção do conhecimento no ensino de Biologia: abordagens metodológicas." | "Recursos didáticos no ensino de Biologia (utilizados em sala de aula e laboratório, incluindo conhecimentos básicos de técnicas, materiais e normas de segurança laboratoriais)." | "O ensino de Biologia e as novas tecnologias da informação e comunicação." | "Competências e Habilidades do Currículo do Distrito Federal para o componente curricular de Biologia no Ensino Médio e para a área de Ciências da Natureza e suas tecnologias no Ensino Médio." | "Competências e Habilidades do Currículo do Distrito Federal para o componente curricular de Física no Ensino médio e para a área de Ciências da Natureza e suas tecnologias no Ensino Médio." | "Competências e Habilidades do Currículo do Distrito Federal para o componente curricular de Química no Ensino Médio e para a área de Ciências da Natureza e suas Tecnologias no Ensino Médio." | "Conhecimento científico, habilidade didática e abordagens metodológicas no ensino de Biologia." | "Tecnologias da informação e comunicação no ensino de Biologia." | "Ensino de Biologia: metodologias, laboratório, letramento científico, BNCC e DCRC." | "A construção do conhecimento no ensino de biologia e as abordagens metodológicas." | "Conhecimento científico, abordagens metodológicas e recursos didáticos no ensino da Filosofia."
```

### 36. Tributos municipais: IPTU, ITBI, cadastro e fiscalização  (6 concursos, 13 tópicos)
```
Matéria 36. id: "direito-tributario.iptu-itbi-fiscalizacao-municipal". subject: "Direito Tributário". Título: "Tributos municipais: IPTU, ITBI, cadastro e fiscalização".
Cubra: IPTU (progressividade, base de cálculo, planta genérica de valores, imunidades e isenções), ITBI, taxas e contribuição de melhoria municipais, cadastro imobiliário e mobiliário, lançamento, notificação e ciência, termo de início e de diligência, auto de infração, malha fiscal e cruzamento de dados, nota fiscal de serviço eletrônica.
NÃO repita (já existe): ISS (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Cadastro imobiliário." | "Cadastro mobiliário." | "Planta genérica de valores." | "Nota fiscal de serviços eletrônica." | "Cadastro: Conceito de cadastro imobiliário, finalidades e importância. Técnicas cadastrais. Cadastro rural e urbano." | "Cadastro mobiliário e imobiliário." | "Cadastro imobiliário e Planta de Valores Genéricos (PVG)." | "Malhas fiscais." | "Termo de diligência." | "Automação de malhas fiscais." | "Georreferenciamento aplicado ao cadastro imobiliário." | "Instrução Normativa Nº 199/2022-SRE, que dispõe sobre a autorregularização, pelo contribuinte, de irregularidades decorrentes de divergências ou inconsistências identificadas em malhas fiscais."
```

### 37. Neuropsicologia e psicomotricidade  (6 concursos, 12 tópicos)
```
Matéria 37. id: "psicologia.neuropsicologia-psicomotricidade". subject: "Psicologia". Título: "Neuropsicologia e psicomotricidade".
Cubra: bases biológicas do comportamento, funções cognitivas (atenção, memória, linguagem, funções executivas), avaliação neuropsicológica e instrumentos, transtornos do neurodesenvolvimento, reabilitação cognitiva, psicomotricidade (esquema e imagem corporal, lateralidade, tônus).
NÃO repita (já existe): avaliação psicológica geral (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Neuropsicologia." | "Omnilateralidade e politecnia: as contribuições de Marx para a Educação." | "Desenvolvimento motor e psicomotricidade." | "Psicomotricidade e suas aplicações." | "Reabilitação cognitiva e funcional." | "Reabilitação Neuropsiquiátrica: Reabilitação cognitiva; Intervenções psicossociais; Funcionalidade e qualidade de vida; Abordagem interdisciplinar." | "Neuropsicologia e Psicomotricidade: Bases biológicas do comportamento; Funções cognitivas; Distúrbios neuropsicológicos; Psicomotricidade; Disfunções cerebrais e transtornos associados." | "Noções básicas de psicomotricidade." | "Esquema corporal." | "Lateralidade." | "Bases Neurobiológicas da Aprendizagem: Neuroanatomia funcional, funções executivas, atenção e memória."
```

### 38. Medicina do trabalho e perícias ocupacionais  (6 concursos, 6 tópicos)
```
Matéria 38. id: "medicina.medicina-do-trabalho-pericias". subject: "Medicina". Título: "Medicina do trabalho e perícias ocupacionais".
Cubra: PCMSO e exames ocupacionais, nexo técnico epidemiológico, doenças ocupacionais por órgão e sistema, LER/DORT, toxicologia ocupacional, perícia médica previdenciária e trabalhista, CAT, readaptação e reabilitação profissional.
NÃO repita (já existe): saúde do trabalhador (noções) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Integração, orientação, acompanhamento, readaptação, reabilitação e retorno ao trabalho." | "Readaptação funcional no setor público no que concerne ao campo do Serviço Social." | "Readaptação funcional e retorno ao trabalho." | "Orientação, acompanhamento e readaptação profissionais." | "Da perícia médica."
```

### 39. Saúde coletiva: trabalho em saúde, educação permanente e redes de atenção  (5 concursos, 193 tópicos)
```
Matéria 39. id: "saude-publica.saude-coletiva-trabalho-vivo-redes". subject: "Saúde Pública". Título: "Saúde coletiva: trabalho em saúde, educação permanente e redes de atenção".
Cubra: ideias centrais da saúde coletiva brasileira: trabalho vivo em ato e tecnologias leves (Merhy), educação permanente em saúde e quadrilátero da formação (Ceccim), apoio matricial e clínica ampliada, redes de atenção à saúde, saúde mental coletiva, saúde digital e proteção de dados no SUS, participação social e conferências de saúde.
NÃO repita (já existe): legislação do SUS e políticas nacionais (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Relatório Final da 17ª Conferência Nacional de Saúde, 2023." | "Saúde: A cartografia do trabalho vivo. 4ª ed. São Paulo: Hucitec, 2014." | "Saúde Mental Coletiva: transoceanizando políticas e práticas locais-globais." | "Tofani, Luís Fernando Nogueira et al. Caos, organização e criatividade: revisão integrativa sobre as Redes de Atenção à Saúde." | "Apoio matricial." | "Trabalho em Equipe Multiprofissional: Integração com equipes de saúde; Encaminhamento de pacientes; Acompanhamento longitudinal; Educação permanente." | "Educação permanente em saúde; Orientação de profissionais e pacientes." | "Educação em Saúde e Capacitação: Planejamento de ações educativas; Capacitação de profissionais de saúde; Educação permanente em saúde." | "Trabalho Interdisciplinar em Saúde: Atuação em equipe multiprofissional; Apoio matricial; Projeto terapêutico singular." | "Interdisciplinaridade em Saúde Mental: Trabalho em equipe multiprofissional; Projeto terapêutico singular; Apoio matricial." | "Educação e Treinamento: Capacitação de equipes; Educação permanente; Treinamento de pessoal auxiliar." | "Educação Permanente em Saúde: Atualização contínua; Participação em treinamentos; Integração da equipe; Melhoria da qualidade da assistência."
```

### 40. Física médica: radioterapia, dosimetria e radioproteção  (5 concursos, 10 tópicos)
```
Matéria 40. id: "saude.fisica-medica-radioterapia-dosimetria". subject: "Saúde". Título: "Física médica: radioterapia, dosimetria e radioproteção".
Cubra: interação da radiação com a matéria, grandezas e unidades, detectores e dosimetria, radiobiologia e radiossensibilidade, teleterapia e braquiterapia, planejamento e controle de qualidade, normas de radioproteção.
NÃO repita (já existe): medicina nuclear e radioterapia (noções) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Fundamentos de dosimetria e radiobiologia." | "Detectores de radiação." | "Princípios de dosimetria clínica e ocupacional." | "Dosimetria pessoal, de área e ambiental." | "Radiossensibilidade de tecidos e órgãos." | "Detectores de radiações: princípios de funcionamento." | "Medição: grandezas e unidades empregadas em proteção radiológica e dosimetria."
```

### 41. Orientação e mobilidade, deficiência visual e cães-guia  (5 concursos, 10 tópicos)
```
Matéria 41. id: "educacao.orientacao-mobilidade-caes-guia". subject: "Educação". Título: "Orientação e mobilidade, deficiência visual e cães-guia".
Cubra: deficiência visual (classificação, funções visuais), orientação e mobilidade, técnicas de bengala e guia vidente, formação e treinamento de cães-guia, legislação de acesso com cão-guia, tecnologia assistiva para cegos, braile.
NÃO repita (já existe): educação especial geral (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Tratado de Marraqueche para Facilitar o Acesso a Obras Publicadas às Pessoas Cegas, com Deficiência Visual ou com Outras Dificuldades para Ter Acesso ao Texto Impresso." | "Treinamento de Cães-Guia." | "Responsabilidade técnica e ética na formação de cães-guia." | "Estrutura e Gestão de Centros de Formação de Cães-Guia." | "Orientação e mobilidade." | "Extensão e Pesquisa Aplicada na Área de Cães-Guia." | "Metodologias de ensino aplicadas à formação técnica e tecnológica na área de Cães-Guia." | "Atendimento odontológico nas deficiências de audiocomunicação: surdez e deficiência visual." | "Deficiências sensoriais e motoras: surdez, baixa visão/cegueira, tetraparesias — rotinas de comunicação, guia tátil e apoio do cuidador." | "Sistema visual: funções visuais; definição e classificação de baixa visão."
```

### 42. Farmacovigilância, tecnovigilância e uso racional de medicamentos  (5 concursos, 6 tópicos)
```
Matéria 42. id: "farmacia.farmacovigilancia-assistencia-hospitalar". subject: "Farmácia". Título: "Farmacovigilância, tecnovigilância e uso racional de medicamentos".
Cubra: farmacovigilância e notificação de eventos adversos, tecnovigilância e hemovigilância, erros de medicação e segurança do paciente, farmacoeconomia, seleção e padronização (comissão de farmácia e terapêutica), uso racional de antimicrobianos.
NÃO repita (já existe): farmácia hospitalar (noções) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Uso racional de antimicrobianos." | "Farmacologia em Neuropediatria: Antiepilépticos; Psicoestimulantes; Neuromoduladores; Uso racional de medicamentos em pediatria." | "Tecnovigilância." | "Uso racional de antimicrobianos e resistência microbiana nas infecções gastrointestinais, biliares e abdominais."
```

### 43. Direito internacional privado: família, alimentos e cooperação  (4 concursos, 6 tópicos)
```
Matéria 43. id: "direito-internacional.privado-familia-cooperacao". subject: "Direito". Título: "Direito internacional privado: família, alimentos e cooperação".
Cubra: elementos de conexão e LINDB, Convenção da Haia sobre sequestro internacional de crianças, adoção internacional, alimentos no exterior (Convenção de Nova York e da Haia), cooperação jurídica internacional (carta rogatória, auxílio direto, homologação de sentença estrangeira), nacionalidade e direitos de migrantes.
NÃO repita (já existe): direito internacional público (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Convenção Relativa à Proteção das Crianças e à Cooperação em Matéria de Adoção Internacional." | "Convenção sobre Obrigações Alimentares." | "Convenção de Haia de 1980 sobre os aspectos civis do sequestro internacional de crianças e Decreto nº 3.413/2000."
```

### 44. Enfermagem em oncologia, hemoterapia e terapia infusional  (4 concursos, 6 tópicos)
```
Matéria 44. id: "enfermagem.enfermagem-oncologica-hemoterapia". subject: "Enfermagem". Título: "Enfermagem em oncologia, hemoterapia e terapia infusional".
Cubra: cuidado de enfermagem ao paciente oncológico, quimioterapia (manipulação segura, extravasamento), radioterapia, cateteres venosos centrais e PICC, terapia infusional, hemotransfusão e reações transfusionais, cuidados paliativos.
NÃO repita (já existe): saúde do adulto geral (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Quimioterapia, radioterapia e cirurgias." | "Abordagem do paciente submetido à radioterapia ou quimioterapia." | "Tratamento odontológico no paciente oncológico submetido à quimioterapia."
```

### 45. Direito penal: dogmática avançada e bem jurídico  (4 concursos, 4 tópicos)
```
Matéria 45. id: "direito-penal.dogmatica-avancada-bem-juridico". subject: "Direito". Título: "Direito penal: dogmática avançada e bem jurídico".
Cubra: teorias do bem jurídico (individual e supraindividual), funcionalismo (Roxin e Jakobs), imputação objetiva, direito penal do inimigo, sociedade de risco e expansão do direito penal, crimes de perigo abstrato, autoria e domínio do fato, responsabilidade penal da pessoa jurídica, princípios limitadores.
NÃO repita (já existe): teoria do crime básica (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Bem jurídico supraindividual." | "Crimes militares em tempo de guerra: favorecimento ao inimigo."
```

### 46. Ecocardiografia  (3 concursos, 35 tópicos)
```
Matéria 46. id: "medicina.ecocardiografia". subject: "Medicina". Título: "Ecocardiografia".
Cubra: princípios físicos e aquisição de imagem transtorácica e transesofágica, Doppler, avaliação de função sistólica e diastólica, valvopatias, cardiomiopatias, doenças do pericárdio, cardiopatias congênitas no adulto, eco de estresse.
NÃO repita (já existe): cardiologia clínica (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Princípios técnicos da aquisição de imagem transtorácica em adultos: anatomia cardíaca ecocardiográfica; principais janelas e vistas ecocardiográficas em adultos." | "Ecocardiografia transesofágica: indicações, aplicabilidade, complicações, segurança, procedimentos relacionados." | "Ecocardiografia tridimensional: princípios físicos, aplicações clínicas." | "Ecocardiografia com contraste: indicações, aplicabilidade, aspectos práticos de uso e segurança." | "Ecocardiografia nas urgências e emergências: avaliação hemodinâmica nos estados de choque; trauma cardíaco; tamponamento cardíaco." | "Avaliação da estrutura e função ventricular sistólica esquerda e direita por ecocardiografia." | "Avaliação da função ventricular diastólica por ecocardiografia." | "Avaliação da estrutura, volume e função dos átrios por ecocardiografia." | "Ecocardiografia na avaliação da sincronia cardíaca." | "Ecocardiografia na doença valvar cardíaca: doenças das valvas mitral, aórtica, tricúspide e pulmonar." | "Ecocardiografia nas doenças do pericárdio." | "Ecocardiografia nas doenças da aorta."
```

### 47. Planejamento educacional e gestão de sistemas de ensino  (3 concursos, 17 tópicos)
```
Matéria 47. id: "educacao.planejamento-educacional-gestao-sistemas". subject: "Educação". Título: "Planejamento educacional e gestão de sistemas de ensino".
Cubra: planejamento educacional (planos nacional, estadual e municipal), financiamento e regime de colaboração, sistemas de ensino e conselhos, avaliação em larga escala e indicadores, gestão democrática, monitoramento do desenvolvimento escolar.
NÃO repita (já existe): PNE e gestão escolar (já existem).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Monitoramento do desenvolvimento escolar." | "Planejamento da Educação." | "Planejamento educacional: níveis, etapas e instrumentos."
```

### 48. Engenharia agrícola: máquinas, construções rurais e armazenagem  (2 concursos, 3 tópicos)
```
Matéria 48. id: "engenharia.engenharia-agricola-rural". subject: "Engenharia". Título: "Engenharia agrícola: máquinas, construções rurais e armazenagem".
Cubra: máquinas e implementos agrícolas, tratores, construções e instalações rurais, ambiência animal, secagem e armazenagem de grãos, eletrificação rural, energia no meio rural.
NÃO repita (já existe): culturas e mecanização (noções) já existe.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Construções Rurais: Habitações rurais." | "Princípios de funcionamento, dimensionamento e seleção de máquinas e implementos agrícolas." | "Construções Rurais e Ambiência."
```

### 49. Estrabismo, motilidade ocular e neuroftalmologia  (1 concursos, 22 tópicos)
```
Matéria 49. id: "medicina.oftalmologia-estrabismo-neuroftalmologia". subject: "Medicina". Título: "Estrabismo, motilidade ocular e neuroftalmologia".
Cubra: anatomia e fisiologia da motilidade ocular, visão binocular, esotropias e exotropias, desvios verticais, ambliopia, óptica de prismas, paralisias oculomotoras, nistagmo, neuropatias ópticas, campimetria.
NÃO repita (já existe): oftalmologia geral (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Motilidade ocular e da visão binocular." | "Esotropias." | "Exotropias." | "Estrabismos verticais e oblíquos." | "Estrabismos restritivos." | "Síndromes de estrabismos especiais, entre elas Duane, Brown e Distúrbios Congênitos de Desinervação dos Nervos Cranianos (Congenital Cranial Dysinnervation Disorders)." | "Estrabismos associados a distúrbios genéticos." | "Estrabismos incomitantes." | "Prescrição refracional nos estrabismos." | "Prescrição de prismas." | "Óptica de prismas." | "Indicações e técnicas de cirurgia para estrabismo."
```

### 50. Imagem da mama: mamografia, ultrassom e ressonância  (1 concursos, 2 tópicos)
```
Matéria 50. id: "medicina.mastologia-mamografia-imagem". subject: "Medicina". Título: "Imagem da mama: mamografia, ultrassom e ressonância".
Cubra: técnica e posicionamento em mamografia, BI-RADS, achados (nódulos, calcificações, distorções), rastreamento, tomossíntese, ultrassonografia e ressonância mamárias, procedimentos guiados e controle de qualidade.
NÃO repita (já existe): mastologia clínica (já existe).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Alterações benignas comuns: cistos, fibroadenomas, lipomas, calcificações benignas." | "Sinais mamográficos suspeitos: nódulos espiculados, distorções arquiteturais, microcalcificações suspeitas."
```
