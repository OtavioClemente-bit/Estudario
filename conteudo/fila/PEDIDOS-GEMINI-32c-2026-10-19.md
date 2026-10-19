# Pedidos para o Gemini: mais 32 matérias novas (19/10/2026, terceira leva)

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

### 1. Teorias sociológicas e ensino de sociologia  (24 concursos, 47 tópicos)
```
Matéria 1. id: "sociologia.teorias-sociologicas-ensino". subject: "Sociologia". Título: "Teorias sociológicas e ensino de sociologia".
Cubra: Durkheim, Weber, Marx, sociologia contemporânea (Bourdieu, Giddens, Bauman, Elias), cultura, trabalho, estratificação, movimentos sociais, Estado e cidadania, pensamento social brasileiro (Freyre, Sérgio Buarque, Florestan) e o ensino de sociologia.
NÃO repita (já existe): antropologia básica já existente.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Metodologia de ensino de sociologia." | "Metodologias de ensino de sociologia." | "Aspectos da Base Nacional Comum Curricular (BNCC) para o componente curricular de sociologia." | "Sociologia da educação; a democratização da escola; educação e sociedade." | "Sociologia rural." | "Estado e sociedade, demografia, economia e sociologia." | "Sociologia econômica e das organizações: relações sociais que moldam decisões econômicas." | "Sociologia urbana: a produção social do espaço." | "A sociologia como componente curricular na educação básica." | "Sociologia brasileira: formação, desenvolvimento, principais temas e autores; interpretação do Brasil, desigualdades, modernização, estruturas sociais e dinâmicas regionais." | "Sociologia: movimentos sociais, classes sociais e lutas sociais no Brasil." | "Sociologia e conhecimento: Sociologia como ciência, objeto, métodos e especificidades."
```

### 2. Teoria da história, historiografia e ensino de história  (24 concursos, 45 tópicos)
```
Matéria 2. id: "historia.teoria-historiografia-ensino". subject: "História". Título: "Teoria da história, historiografia e ensino de história".
Cubra: escolas historiográficas (positivismo, marxismo, Annales, nova história cultural), fontes e método, memória e patrimônio, história indígena e afro-brasileira, BNCC de História e didática.
NÃO repita (já existe): história geral e do Brasil (conteúdo factual).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Conhecimento sobre as Teorias da História, historiografia, autores fundamentais e intérpretes do Brasil." | "Fontes históricas." | "Metodologia do ensino da História: Fontes históricas, temporalidade, espacialidade, identidade, cidadania." | "Conhecimento histórico contemporâneo: saber histórico e historiografia." | "Apropriações didático-pedagógicas dos procedimentos de produção do conhecimento histórico: História e historiografia." | "A Escola dos Annales, abordagens historiográficas." | "Os caminhos da historiografia nos séculos XIX, XX e XXI." | "Conceitos: movimento, tempo, cultura, fontes históricas, espaços, paisagem, sociedade, trabalho, natureza e representação, ambiente, relação entre ser humano e ambiente." | "Conceitos de tempo histórico, memória, fontes históricas e patrimônio cultural." | "Escola dos Annales." | "Fontes históricas e crítica documental." | "Historiografia do Teatro no Ocidente, com ênfase no contexto brasileiro."
```

### 3. História da filosofia e ensino de filosofia  (23 concursos, 147 tópicos)
```
Matéria 3. id: "filosofia.historia-filosofia". subject: "Filosofia". Título: "História da filosofia e ensino de filosofia".
Cubra: filosofia antiga (pré-socráticos, Sócrates, Platão, Aristóteles, helenismo), medieval, moderna (racionalismo, empirismo, Kant), contemporânea (Hegel, Marx, Nietzsche, fenomenologia, existencialismo, Escola de Frankfurt, filosofia analítica), ética, política, estética e o ensino de filosofia no ensino médio.
NÃO repita (já existe): epistemologia básica já existente.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Reflexões acerca do ensino de filosofia no ensino médio. Ensino de filosofia e interdisciplinaridade. Estratégias didáticas e seleção de conteúdos." | "Ética e filosofia da comunicação." | "Entre a patrística e a escolástica: a Filosofia medieval, seus dilemas e pensadores." | "A Filosofia da Linguagem e o fenômeno da comunicação humana." | "Kant e a Revolução Copernicana na Filosofia: da Crítica da Razão à Filosofia Moral." | "Do idealismo filosófico em Hegel ao materialismo histórico-dialético em Marx." | "Fenomenologia, existencialismo e positivismo: pensadores e conceitos." | "Filosofia Estética: arte e cultura na era da informação e da tecnologia." | "Moral, ética e ética aplicada: do \"martelo\" de Nietzsche às teorias éticas contemporâneas." | "As contribuições de Michel Foucault para a Filosofia contemporânea." | "A Filosofia no Brasil e a midiatização do saber filosófico." | "Filosofia da Técnica."
```

### 4. Fisioterapia na saúde da mulher, pediatria e geriatria  (22 concursos, 43 tópicos)
```
Matéria 4. id: "fisioterapia.saude-mulher-pediatria-geriatria". subject: "Fisioterapia". Título: "Fisioterapia na saúde da mulher, pediatria e geriatria".
Cubra: fisioterapia uroginecológica e obstétrica, pediatria e neonatologia, gerontologia, desportiva, dermatofuncional, ergonomia e legislação COFFITO.
NÃO repita (já existe): áreas cardiorrespiratória e neuro já existentes.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Código de Ética e Deontologia da Fisioterapia." | "Fisioterapia preventiva e promoção da saúde: ações educativas, prevenção de doenças e agravos." | "Gestão e organização do serviço de fisioterapia: planejamento, controle de materiais e equipamentos, manutenção de ambientes terapêuticos." | "Legislação profissional: Lei n.º 6.316/1975, Código de Ética e Deontologia da Fisioterapia (COFFITO)." | "Assistência Fisioterapêutica ao paciente com COVID." | "Treinamento Desportivo: Princípios do treinamento." | "Fisioterapia na Atenção Primária." | "Fisioterapia em Uroginecologia." | "Aspectos éticos e deontológicos do profissional Fisioterapeuta." | "Direito do Trabalho Desportivo – Lei nº 14.597/2023 e suas alterações – Lei nº 9.615/1998 e suas alterações – Lei nº 14.193/2021 e suas alterações." | "Fundamentos da Fisioterapia: História e princípios da Fisioterapia." | "Fisioterapia na Saúde Mental: Relação entre movimento, corpo e saúde mental."
```

### 5. Defensoria Pública: tutela coletiva e grupos vulneráveis  (20 concursos, 101 tópicos)
```
Matéria 5. id: "direito.defensoria-tutela-vulneraveis". subject: "Direito". Título: "Defensoria Pública: tutela coletiva e grupos vulneráveis".
Cubra: atuação da Defensoria em tutela coletiva, direitos da criança e do adolescente na perspectiva da proteção integral, mulheres, população LGBTQIA+, pessoas em situação de rua, migrantes e refugiados, execução penal, acesso à justiça (ondas de Cappelletti), Regras de Brasília e sistema interamericano.
NÃO repita (já existe): organização constitucional da Defensoria já existente.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Acesso à justiça e garantias processuais: Devido processo legal, contraditório e ampla defesa. Efetividade da prestação jurisdicional." | "Políticas judiciárias de inclusão e cidadania: Atuação do Poder Judiciário na promoção de direitos e acesso à justiça." | "Tutela coletiva do direito à comunicação, à manifestação e à liberdade de expressão e opinião: meios, propriedade, regulação da mídia e mecanismos de participação." | "Tutela coletiva do direito à segurança pública, enfrentamento da tortura e direito das vítimas de violência de estado." | "Tutela coletiva do direito à cultura, à memória e à verdade." | "Tutela coletiva do direito à alimentação adequada: a alimentação como direito fundamental social e o Sistema Nacional de Segurança Alimentar e Nutricional." | "Tutela coletiva, defesa de direitos, políticas públicas e instrumentos normativos em espécie." | "Acesso à Justiça e o movimento mundial de efetivação dos direitos: a) relatório geral de Mauro Cappelletti e Bryant Garth no final da década de 1970, no ensaio intitulado Acesso à Justiça." | "O dever dos estados de promover o acesso à justiça." | "Acesso à Justiça: história; Estratégia nacional para ampliação do acesso à justiça." | "As três vertentes da proteção internacional da pessoa humana. Direitos humanos, direito humanitário e direito dos refugiados." | "Acesso à Justiça e o movimento mundial de efetivação dos direitos: relatório geral de Mauro Cappelletti e Bryant Garth no final da década de 1970, no ensaio intitulado Acesso à Justiça."
```

### 6. Técnica dietética, UAN e microbiologia de alimentos  (20 concursos, 37 tópicos)
```
Matéria 6. id: "nutricao.tecnica-dietetica-uan". subject: "Nutrição". Título: "Técnica dietética, UAN e microbiologia de alimentos".
Cubra: técnica dietética (pré-preparo, fatores de correção e cocção), planejamento de cardápios, gestão de UAN, boas práticas e APPCC, microbiologia e conservação de alimentos, PNAE e alimentação coletiva.
NÃO repita (já existe): dietoterapia já existente.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Planejamento de cardápios." | "Transformação e conservação de alimentos." | "Noções de microbiologia de alimentos." | "Tecnologia de alimentos: processamento e conservação de alimentos; controle de qualidade físico-químico e microbiológico; boas práticas de fabricação (BPF)." | "APPCC; rotulagem nutricional (RDC ANVISA nº 429/2020 e nº 591/2021); armazenamento, transporte e comercialização." | "APPCC (análise de perigos e pontos críticos de controle); lavratura de autos de infração." | "Patógenos emergentes: Principais características e importância na microbiologia de alimentos." | "Tecnologia de alimentos: operações unitárias, conservação de alimentos, embalagem em alimentos, processamento tecnológico de produtos de origem vegetal e animal, análise sensorial." | "Inspeção e Microbiologia de Alimentos de Origem Animal: Contaminação microbiana." | "Microbiologia de alimentos aplicada à segurança alimentar." | "Planejamento, elaboração, organização e execução de cardápios considerando necessidades nutricionais, sustentabilidade e redução do desperdício." | "Gestão de cardápios escolares, incluindo aquisição de alimentos da agricultura familiar, conforme Legislações e Resoluções do FNDE vigentes e do Programa Nacional de Alimentação Escolar (PNAE)."
```

### 7. Ultrassonografia geral e medicina fetal  (19 concursos, 194 tópicos)
```
Matéria 7. id: "medicina.ultrassonografia-medicina-fetal". subject: "Medicina". Título: "Ultrassonografia geral e medicina fetal".
Cubra: física do ultrassom e Doppler, otimização da imagem, laudo, US de abdome, pelve, tireoide, mama, partes moles, musculoesquelético, urgências e POCUS/FAST, procedimentos guiados, US obstétrica (1º trimestre, biometria, morfologia fetal, placenta, líquido, Doppler obstétrico) e medicina fetal.
NÃO repita (já existe): radiologia geral (RX, TC, RM).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Princípios físicos do ultrassom." | "Modos de imagem ultrassonográfica." | "Anatomia ultrassonográfica." | "Terminologia e semiologia ultrassonográfica." | "Ultrassonografia dos rins." | "Ultrassonografia das vias urinárias." | "Ultrassonografia da bexiga." | "Ultrassonografia da próstata e vesículas seminais." | "Ultrassonografia do escroto e conteúdo escrotal." | "Ultrassonografia do abdome total." | "Ultrassonografia do abdome superior." | "Ultrassonografia da parede abdominal."
```

### 8. Biomedicina: citologia, histotecnologia e áreas de atuação  (19 concursos, 29 tópicos)
```
Matéria 8. id: "biomedicina.citologia-histotecnologia-imagem". subject: "Biomedicina". Título: "Biomedicina: citologia, histotecnologia e áreas de atuação".
Cubra: citologia clínica (Papanicolaou, Bethesda), histotecnologia, biologia molecular diagnóstica, imaginologia, biomedicina estética, legislação e ética do biomédico.
NÃO repita (já existe): análises clínicas já existentes.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Biópsias e citologia esfoliativa." | "Vacinas, anticorpos monoclonais, biomateriais e biomedicamentos." | "Código de Ética da Profissão de Biomédico." | "Fundamentos da Biomedicina." | "Tecnologias biomédicas relacionadas às análises clínicas." | "Correlação entre os padrões de desequilíbrio da MTC e as doenças da Biomedicina com a Acupuntura." | "Equipamentos biomédicos." | "Segurança em equipamentos biomédicos." | "Magnificação de imagens, biópsias, citologia e corantes em endoscopia digestiva." | "Morfologia, citologia, fisiologia e genética de micro‐organismos." | "Biópsia e citologia." | "Interpretação de Exames: Citologia oncótica; Histopatologia; Exames laboratoriais; Exames de imagem (noções)."
```

### 9. Libras, tradução e interpretação  (18 concursos, 132 tópicos)
```
Matéria 9. id: "educacao.libras-traducao-interpretacao". subject: "Educação". Título: "Libras, tradução e interpretação".
Cubra: aspectos linguísticos da Libras (parâmetros, morfologia, sintaxe, classificadores, datilologia), história da educação de surdos, legislação da Libras e do tradutor-intérprete, tradução e interpretação (modos, ética, guia-intérprete, surdocegueira), bilinguismo.
NÃO repita (já existe): educação especial geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Lei n. 14.191/2021 (Modalidade de educação bilíngue de surdos)." | "Cultura e identidades surdas." | "Datilologia vs Sinal soletrado." | "Indicadores de tempo em Libras." | "Tipos de verbos na Libras." | "Lei 10.436/2002- Lei de Libras." | "Lei 12.319/2010- Regulamenta a profissão de tradutor e/intérprete de Libras." | "Decreto 5.626/2005 - Regulamenta a Libras." | "Intérprete educacional." | "Acessibilidade comunicacional: produção de vídeos com audiodescrição, janela de libras e legendagem descritiva conforme normas da ABNT e e-MAG." | "Código de ética do tradutor e intérprete de Língua de Sinais." | "Políticas linguísticas e surdez."
```

### 10. Grandes animais, reprodução e patologia veterinária  (17 concursos, 36 tópicos)
```
Matéria 10. id: "medicina-veterinaria.grandes-animais-reproducao-patologia". subject: "Medicina Veterinária". Título: "Grandes animais, reprodução e patologia veterinária".
Cubra: clínica de bovinos, equinos, ovinos, caprinos e suínos, fisiologia e biotecnologia da reprodução, obstetrícia veterinária, anatomia, fisiologia, patologia geral e especial, microbiologia, parasitologia e imunologia veterinárias.
NÃO repita (já existe): inspeção de produtos de origem animal; zoonoses (noções).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Produção de suínos e aves: alimentação, manejo e sanidade." | "Ovinocultura e Caprinocultura: manejo alimentar e sanitário; nutrição, produção, conservação, utilização e manejo de plantas forrageiras; silagem, feno e fenação." | "Suinocultura: reprodução, cria, recria e terminação; instalações, alimentação, nutrição e sanidade." | "Aspectos referentes à criação e o devido manejo de raças de: bovino de corte e leite, suinocultura, avicultura, ovinocultura, apicultura e piscicultura." | "Produção de Bovinos Leiteiros e Sistemas de Produção na Agricultura Familiar: Conhecimentos técnicos em Zootecnia aplicados à bovinocultura leiteira e aos sistemas produtivos da agricultura familiar." | "Doping , controle antidoping , substâncias controladas para equinos e funcionamento dos testes antidoping ." | "Normas para Controle de Equinos no Exército Brasileiro (NORCE)." | "Normas gerais para o transporte de equinos no Exército." | "Acidentes por animais peçonhentos: ofídicos, artrópodes peçonhentos (escorpionismo, araneísmo, lepidopterismo, himenopterismo) e animais aquáticos (cnidários, equinodermos, peixes peçonhentos)." | "Zootecnia. Produção animal. Reprodução animal. Alimentos e alimentação dos animais. Produção e manejo de pastagens nativas e cultivadas." | "Produção animal – Bovinocultura de Corte e de leite: Manejo, taxa de lotação, produtividade de leite, principais raças, características gerais." | "Noções de suinocultura, avicultura de corte e de postura, e de aquicultura."
```

### 11. Terapia ocupacional: áreas de atuação  (17 concursos, 27 tópicos)
```
Matéria 11. id: "terapia-ocupacional.areas-atuacao". subject: "Terapia Ocupacional". Título: "Terapia ocupacional: áreas de atuação".
Cubra: TO em saúde mental, reabilitação física e órteses, infância e desenvolvimento, gerontologia, contextos sociais e hospitalares, tecnologia assistiva, avaliação e modelos (MOHO, Canadense).
NÃO repita (já existe): fundamentos já existentes.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Patologia aplicada à terapia ocupacional." | "Princípios das órteses e próteses." | "Promoção da autonomia e independência nas atividades da vida diária e social." | "Órteses e Próteses." | "Código de Ética da Fisioterapia: Leis e atos normativos da Fisioterapia e Terapia Ocupacional." | "Oficina ortopédica (órteses e próteses)." | "Políticas públicas voltadas à população alvo em terapia ocupacional." | "Atividades de Vida Diária e Inclusão Social: Auxílio nas atividades diárias como vestir, alimentar, mobilidade." | "Código de Ética e Deontologia Profissional da Terapia Ocupacional." | "Cuidados básicos: alimentação, higiene pessoal, vestuário, conforto, repouso, mobilidade e organização do ambiente; incentivo à autonomia nas atividades da vida diária." | "Ética e legislação profissional da fisioterapia: Código de Ética da Fisioterapia; Normas do Conselho Federal de Fisioterapia e Terapia Ocupacional; Responsabilidade profissional; Sigilo profissional." | "Compreensão crítica da história da Terapia Ocupacional no Brasil."
```

### 12. Cirurgia pediátrica  (16 concursos, 166 tópicos)
```
Matéria 12. id: "medicina.cirurgia-pediatrica". subject: "Medicina". Título: "Cirurgia pediátrica".
Cubra: afecções cirúrgicas do recém-nascido e da criança: anomalias congênitas do trato digestivo (atresias, Hirschsprung, duplicações, invaginação), parede abdominal e região inguinal, trato urinário, tórax e pescoço, trauma pediátrico, tumores sólidos da infância, cuidados pré/trans/pós-operatórios.
NÃO repita (já existe): cirurgia geral do adulto; pediatria clínica.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Patologias cirúrgicas mais frequentes na criança." | "Anomalias congênitas do pescoço." | "Afecções congênitas e hereditárias." | "Torcicolo Congênito." | "Malformações congênitas e problemas perinatais do trato respiratório." | "Biologia molecular aplicada à cirurgia pediátrica." | "Atresia tricúspide." | "Implante percutâneo no tratamento de doenças congênitas." | "Cardiopatias congênitas acianóticas." | "Cardiopatias congênitas cianóticas." | "Malformações congênitas dos membros superiores." | "Afecções cirúrgicas do pescoço: Afecções cirúrgicas da tireoide e da paratireoide Anomalias congênitas Tumores primitivos Adenopatias e infecções cervicais Tumores benignos e malignos do pescoço."
```

### 13. Engenharia de pesca e aquicultura  (15 concursos, 138 tópicos)
```
Matéria 13. id: "engenharia.engenharia-pesca-aquicultura". subject: "Engenharia". Título: "Engenharia de pesca e aquicultura".
Cubra: oceanografia e limnologia aplicadas, biologia pesqueira e avaliação de estoques, tecnologia de pesca e embarcações, aquicultura (piscicultura, carcinicultura, malacocultura), sanidade aquícola, tecnologia do pescado, legislação e ordenamento pesqueiro, projetos aquícolas.
NÃO repita (já existe): zootecnia geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Política Nacional de Desenvolvimento Sustentável da Aquicultura e da Pesca (Lei nº 11.959/2009)." | "Limnologia geral." | "Limnologia." | "Fundamentos de oceanografia e ciência pesqueira: ambientes aquáticos e marinhos, recursos pesqueiros, organismos aquáticos de interesse pesqueiro, exploração e uso sustentável dos recursos naturais." | "Legislação e políticas públicas aplicadas à aquicultura e à pesca: Política Nacional de Desenvolvimento Sustentável da Aquicultura e da Pesca (Lei Federal nº 11.959/2009 e suas alterações)." | "Recursos pesqueiros capturados pela pesca artesanal e industrial e os seus métodos de captura." | "Fundamentos de limnologia, oceanografia biológica e ecologia aquática aplicada." | "Gastos públicos na agropecuária, pesca e aquicultura." | "Programa Nacional de Desenvolvimento Sustentável da Aquicultura (Decreto n° 11.852/2023)." | "Programa Povos da Pesca Artesanal (Decreto n° 11.626/2023)" | "Rede Pesca Brasil - (Decreto n° 10.736/2021)" | "Ecologia de ecossistemas aquáticos marinhos e continentais: principais técnicas empregadas na pesca artesanal, ornamental, amadora/esportiva e industrial."
```

### 14. Design gráfico e produção gráfica  (14 concursos, 34 tópicos)
```
Matéria 14. id: "comunicacao.design-grafico-producao-grafica". subject: "Comunicação". Título: "Design gráfico e produção gráfica".
Cubra: fundamentos do design, teoria da cor, tipografia, grid e composição, identidade visual e branding, design editorial, infografia, pré-impressão, sistemas de impressão e acabamento, softwares e fluxo de produção, UX/UI básico.
NÃO repita (já existe): comunicação digital (redes sociais).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Diagramação." | "Identidade Visual." | "Fundamentos do Design Gráfico e Comunicação Visual." | "Tipografia." | "Normas institucionais de identidade visual do Poder Legislativo do Estado do Ceará." | "IDENTIDADE VISUAL INSTITUCIONAL:." | "Legal Design e Visual Law: conceitos, aplicação de elementos visuais em documentos jurídicos, infografia de dados legislativos e simplificação da informação." | "Marca e Branding. Conceito de marca. Branding. Gerenciamento de marca." | "O projeto visual e gráfico: layout, boneca, artefinal, conhecimentos de diagramação e editoração eletrônica." | "História, fundamentos e linguagem visual: história do design gráfico e sua relação com os contextos sociais, políticos, culturais e tecnológicos." | "Desenvolvimento da escrita, da tipografia e da imprensa." | "Modernismo, pós-modernismo e design gráfico contemporâneo."
```

### 15. Museologia e gestão do patrimônio cultural  (13 concursos, 71 tópicos)
```
Matéria 15. id: "cultura.museologia-patrimonio". subject: "Cultura". Título: "Museologia e gestão do patrimônio cultural".
Cubra: teoria museológica, tipologias de museus, documentação e inventário, conservação preventiva, expografia, ação educativa, Estatuto de Museus e Política Nacional de Museus, patrimônio material e imaterial, IPHAN e tombamento.
NÃO repita (já existe): história da arte.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Cultura e patrimônio, com destaque para teatros, museus e festividades tradicionais." | "Museus e novas tecnologias de informação Novas práticas museais. Museologia e inclusão social." | "Museologia aplicada: Processamento técnico de acervos (documentação e informação). Investigação (estudos analíticos), inventário, catalogação, classificação, indexação e incorporação patrimonial." | "Pesquisa museológica: conceituação, elaboração e desenvolvimento de projetos (análise de discursos, estudos de acervos e de públicos em museus). Curadoria. Narrativas expositivas." | "Princípios básicos de segurança em museus. Segurança física e patrimonial. Prevenção de riscos. Planos de emergência. Combate a incêndios. Transporte de obras. Seguros de acervos." | "Exposição e Expografia: (planejamento de exposições. Montagem e desmontagem. Design expográfico. Iluminação de exposições. Comunicação visual." | "Tecnologia e Inovação em Museus. Uso de mídias digitais. Museus virtuais. Tecnologias digitais aplicadas. Realidade virtual e aumentada. Interatividade. Digitalização e preservação digital." | "Educação em museus (práticas e metodologias). Elaboração, desenvolvimento e avaliação de ações educativas e culturais. Públicos de museus. Inclusão e diversidade." | "Gestão e Administração de Museus. Elaboração de planos museológicos. Marketing cultural. Sustentabilidade institucional." | "Acessibilidade. Acessibilidade em museus. Acessibilidade tecnológica. Sinalização e acessibilidade em exposições." | "Código de ética internacional do Conselho Internacional de Museus – ICOM (versão lusófona)." | "Política e educação patrimoniais: gestão de acervos e ações educativas em museus, arquivos e sítios históricos."
```

### 16. Cardiologia e hematologia pediátricas  (12 concursos, 31 tópicos)
```
Matéria 16. id: "medicina.cardiologia-hematologia-pediatrica". subject: "Medicina". Título: "Cardiologia e hematologia pediátricas".
Cubra: cardiologia pediátrica (sopros, cardiopatias congênitas clínicas, insuficiência cardíaca na criança, Kawasaki, febre reumática) e hematologia pediátrica (anemias, doença falciforme, hemofilias, leucemias da infância, transplante de medula óssea).
NÃO repita (já existe): cirurgia cardíaca; hematologia do adulto.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Hemofilias A e B." | "Doenças exantemáticas: sarampo, rubéola, varicela, herpes-zoster, exantema súbito, eritema infeccioso, escarlatina, síndrome mão-pé-boca, doença de Kawasaki e Monkeypox." | "Medula óssea." | "Efeito enxerto-contra-leucemia." | "Biópsia de medula óssea e mielograma." | "Hemoglobinopatias, com ênfase na doença falciforme e em suas complicações." | "Distúrbios da hemostasia: avaliação de sangramentos, hemofilias e outras coagulopatias." | "Patologia cardiovascular, hemato-linfoide e respiratória: doenças do coração e vasos, linfomas e leucemias (noções), neoplasias pulmonares e vias aerodigestivas superiores." | "Semiologia Cardiológica: Anamnese dirigida; Exame físico cardiovascular; Ausculta cardíaca; Avaliação de sopros, bulhas e sinais clínicos." | "Diagnóstico laboratorial das Hemofilias A e B e Doença de von Willebrand." | "Sopros cardíacos." | "Citologia e histologia da medula óssea."
```

### 17. Fonoaudiologia: motricidade orofacial, neonatologia e saúde coletiva  (12 concursos, 18 tópicos)
```
Matéria 17. id: "fonoaudiologia.motricidade-neonatal-coletiva". subject: "Fonoaudiologia". Título: "Fonoaudiologia: motricidade orofacial, neonatologia e saúde coletiva".
Cubra: motricidade orofacial, fonoaudiologia hospitalar e neonatal (aleitamento, triagem auditiva neonatal), saúde coletiva, educacional e do trabalho, legislação e ética.
NÃO repita (já existe): audiologia e linguagem já existentes.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Saúde pública: Prevenção e promoção da saúde em fonoaudiologia." | "Fonoaudiologia e a Instituição Escolar." | "Fonoaudiologia Educacional: atuação em ambiente escolar, prevenção de dificuldades de aprendizagem, orientação a professores, alunos e famílias." | "Fonoaudiologia na Saúde Pública: atuação no SUS, promoção, prevenção e reabilitação em saúde, programas de saúde auditiva e comunicação." | "Legislação profissional: Lei n.º 6.965/1981 (Regulamenta a profissão de Fonoaudiólogo), Código de Ética do Fonoaudiólogo, Normas do Conselho Federal de Fonoaudiologia (CFFa)." | "Fonoaudiologia educacional." | "Fonoaudiologia na Atenção Primária à Saúde." | "Ética e legislação profissional da fonoaudiologia: Código de Ética da Fonoaudiologia; Normas do Conselho Federal de Fonoaudiologia; Responsabilidade profissional; Sigilo profissional." | "Fonoaudiologia Preventiva." | "Triagem auditiva neonatal. Disfagia – Anatomia fisiologia da deglutição." | "Fonoaudiologia educacional - Promoção e prevenção nos diferentes espaços educacionais formais e não formais." | "Promoção, prevenção, diagnóstico e intervenção em saúde coletiva e ocupacional no âmbito da fonoaudiologia."
```

### 18. Cirurgia cardiovascular e cardiopatias congênitas  (11 concursos, 66 tópicos)
```
Matéria 18. id: "medicina.cirurgia-cardiovascular". subject: "Medicina". Título: "Cirurgia cardiovascular e cardiopatias congênitas".
Cubra: anatomia cirúrgica do coração, circulação extracorpórea e proteção miocárdica, revascularização do miocárdio, cirurgia valvar, complicações do infarto, aorta (dissecção, aneurismas), cardiopatias congênitas (classificação, CIA, CIV, PCA, tetralogia, transposição, coarctação, hipoplasia de VE) e procedimentos paliativos.
NÃO repita (já existe): cardiologia clínica; cirurgia vascular periférica.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Tetralogia de Fallot." | "Ventrículo único e fisiologia univentricular." | "Ultrassonografia Doppler da aorta abdominal e artérias ilíacas." | "Cirurgia da doença arterial coronariana - cardiopatia isquêmica." | "Tratamento endovascular de doenças da aorta." | "Miocardiopatias: hipertrófica, hipertensiva, dilatada e idiopática." | "Doenças reumatológicas e cardiopatias." | "Cardiopatias de interesse epidemiológico no Brasil." | "Afecções cirúrgicas arteriais e venosas: Doenças da Aorta Doenças Arteriais Periféricas Doenças Venosas Doenças do Sistema Linfático Úlceras de perna." | "Distúrbios Cardíacos e Hemodinâmicos: Manejo de arritmias, insuficiência cardíaca, hipertensão pulmonar, e emergências relacionadas a cardiopatias." | "Revascularização do miocárdio." | "Cirurgias valvares."
```

### 19. Tomografia, ressonância, mamografia e medicina nuclear (técnicas)  (11 concursos, 29 tópicos)
```
Matéria 19. id: "radiologia.tc-rm-mamografia-nuclear". subject: "Radiologia". Título: "Tomografia, ressonância, mamografia e medicina nuclear (técnicas)".
Cubra: tomografia computadorizada (princípios, protocolos, contraste), ressonância magnética (física, sequências, segurança), mamografia (posicionamento, controle de qualidade), densitometria, radiologia intervencionista e medicina nuclear para técnicos.
NÃO repita (já existe): radiologia convencional já existente.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Conhecimento geral sobre Ressonância Magnética Mamária (para correlacionar com outros exames de imagem de mama)." | "Técnica e interpretação de mamografia." | "Medicina nuclear aplicada à neurologia." | "Tomografia computadorizada e ressonância magnética cardiovascular." | "Angiotomografia vascular." | "Angiorressonância magnética." | "Tomografia de coerência óptica intracoronariana." | "Fundamentos da tomografia computadorizada: princípios físicos, reconstrução de imagem, protocolos e aplicações clínicas." | "Fundamentos da ressonância magnética: princípios físicos, sequências de pulso, artefatos e aplicações diagnósticas." | "Imagenologia do sistema cardiovascular: anatomia radiológica, avaliação por tomografia e ressonância magnética cardíaca." | "Densitometria óssea: princípios físicos, técnica de execução, interpretação e osteometabolismo." | "Mamografia: Técnicas de posicionamento."
```

### 20. Cirurgia e traumatologia bucomaxilofacial  (9 concursos, 27 tópicos)
```
Matéria 20. id: "odontologia.cirurgia-bucomaxilofacial". subject: "Odontologia". Título: "Cirurgia e traumatologia bucomaxilofacial".
Cubra: exodontias e dentes inclusos, infecções odontogênicas, cistos e tumores dos maxilares, trauma de face, cirurgia ortognática, ATM, implantodontia, anestesia e complicações, urgências.
NÃO repita (já existe): clínica odontológica geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Princípios básicos e fundamentais da cirurgia bucomaxilofacial." | "Conceitos, propósitos e limites da cirurgia bucomaxilofacial." | "Anestesias local e geral em cirurgia bucomaxilofacial." | "Reimplantes e transplantes dentários." | "Técnicas Cirúrgicas: Aspectos Legais da Prática Cirúrgica: Especialidades Odontológicas: Endodontia, Periodontia, Dentística, Cirurgia Bucomaxilofacial, entre outras." | "Exodontia: técnicas e acidentes Implantodontia: Estudo e prática de implantes dentários, incluindo técnicas cirúrgicas, materiais e planejamento." | "Cirurgia Ortognática: Correção de deformidades faciais e maxilares." | "Traumatologia Bucomaxilofacial: Manejo de traumas na região da face e maxila." | "Cirurgia dos dentes inclusos." | "Doenças odontogênicas do seio maxilar." | "Cirurgia ortognática." | "Tomografia computadorizada interativa e plano de tratamento em implantodontia."
```

### 21. Engenharia de produção  (9 concursos, 15 tópicos)
```
Matéria 21. id: "engenharia.engenharia-producao". subject: "Engenharia". Título: "Engenharia de produção".
Cubra: PCP, gestão de operações, qualidade (ferramentas, seis sigma, ISO 9001), ergonomia, pesquisa operacional, logística, custos industriais, gestão de projetos, engenharia de métodos e tempos.
NÃO repita (já existe): administração geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Pesquisa operacional, engenharia do trabalho e engenharia da sustentabilidade: modelagem, simulação e otimização." | "Organização industrial, leiaute/arranjo físico." | "Arranjo Físico de máquinas, equipamentos e facilidades." | "Pesquisa Operacional." | "Arranjo físico (layout)." | "Layout e Fluxo, abrangendo tipos de processos, arranjo físico e fluxo de materiais, análise de carga-distância e balanceamento de linha." | "Pesquisa operacional: programação matemática, modelagem física, projeto e análise de sistemas logísticos." | "Pesquisa operacional. Programação Matemática. Modelagem e Simulação. Processos Decisórios." | "Arranjo Físico." | "Engenharia de Operações e Pesquisa Operacional: Organização industrial, layout/arranjo físico." | "Engenharia de Operações e Pesquisa Operacional: Modelagem, Simulação e Otimização."
```

### 22. Ciências da religião e ensino religioso  (8 concursos, 50 tópicos)
```
Matéria 22. id: "religiao.ciencias-religiao-ensino-religioso". subject: "Religião". Título: "Ciências da religião e ensino religioso".
Cubra: fenomenologia da religião, tradições religiosas (cristianismo, judaísmo, islamismo, religiões afro-brasileiras e indígenas, orientais), laicidade e liberdade religiosa, BNCC do ensino religioso, capelania e assistência religiosa.
NÃO repita (já existe): filosofia geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "A Igreja Católica (Concílio do Vaticano II, Teologia da Libertação, renúncia de Bento XVI e Pontificado do Papa Francisco)." | "Teologia da Graça no século XX: centralidade trinitária e convergência ecumênica." | "Experiência e teologia do Espírito Santo no Novo Testamento (Paulo, os Sinópticos, Atos dos Apóstolos e João)." | "Liturgia e o mistério pascal de Cristo." | "A relação íntima entre liturgia, martyria e diaconia." | "O ano litúrgico." | "A Liturgia das Horas." | "A Palavra na Liturgia." | "Símbolo e Sacramento." | "Cristo, sacramento do Pai." | "A Igreja, sacramento de Cristo." | "A teologia do mysterion."
```

### 23. Prótese dentária: técnicas laboratoriais  (7 concursos, 19 tópicos)
```
Matéria 23. id: "odontologia.protese-dentaria-tecnico". subject: "Odontologia". Título: "Prótese dentária: técnicas laboratoriais".
Cubra: materiais (gessos, ceras, resinas, ligas, cerâmicas), modelos e troqueis, prótese total, parcial removível e fixa, articuladores, fundição, acabamento, CAD/CAM, biossegurança no laboratório.
NÃO repita (já existe): prótese clínica (noções).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Equipamentos e instrumentais de uso no laboratório de prótese dental." | "Superfícies de gesso." | "Materiais Utilizados em Aparelhos Gessados: Tipos de gesso e materiais sintéticos; Faixas, talas e ataduras; Equipamentos e instrumentos utilizados; Armazenamento e conservação dos materiais." | "Aberturas em aparelhos gessados e técnicas de retirada de talas, gessos e trações." | "Aspectos éticos e legais englobando a responsabilidade civil dos laboratórios e profissionais de prótese. Atribuições do auxiliar e técnico de laboratório de prótese dentária." | "Regulamentação e legislação sobre prótese dentária." | "Responsabilidades legais e éticas do técnico em prótese dentária." | "Materiais alternativos: acrílico, compósitos, gesso; Manipulação e Aplicação dos Materiais." | "Prótese total (dentadura)." | "Prótese parcial removível (parcial metálica e acrílica)." | "Interpretação de modelos de gesso e radiografias." | "Análise de modelos em gesso e procedimentos iniciais para a confecção da prótese."
```

### 24. Hemodinâmica e cardiologia intervencionista  (6 concursos, 25 tópicos)
```
Matéria 24. id: "medicina.hemodinamica-cardiologia-intervencionista". subject: "Medicina". Título: "Hemodinâmica e cardiologia intervencionista".
Cubra: cateterismo cardíaco, cineangiocoronariografia, intervenção coronária percutânea, stents, angioplastia primária, intervenções estruturais (TAVI, valvoplastias, fechamento de CIA/PCA), radioproteção e contraste em hemodinâmica, complicações.
NÃO repita (já existe): cardiologia clínica geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Angioplastia Aorto-ilíaca." | "Angioplastia fêmoro-poplítea." | "Angioplastia infra-poplítea." | "Angioplastia carotídea." | "História da Hemodinâmica." | "Complicações da angioplastia." | "Ecocardiografia nas urgências e emergências: avaliação hemodinâmica nos estados de choque; trauma cardíaco; tamponamento cardíaco." | "Atuação terapêutica em cardiologia intervencionista." | "Tratamento intervencionista: trombólise, angioplastia, clipagem, embolização." | "Métodos Diagnósticos em Cardiologia: Eletrocardiograma (ECG); Teste ergométrico; Ecocardiograma; Monitorização ambulatorial (Holter e MAPA); Cateterismo cardíaco; Exames laboratoriais em cardiologia." | "Cateterismo cardíaco e cardiologia intervencionista pediátrica." | "Cateterismo cardíaco diagnóstico e intervencionista."
```

### 25. Musicoterapia  (3 concursos, 42 tópicos)
```
Matéria 25. id: "saude.musicoterapia". subject: "Saúde". Título: "Musicoterapia".
Cubra: fundamentos e métodos da musicoterapia (Benenzon, Nordoff-Robbins, Bruscia), avaliação, áreas de atuação (saúde mental, TEA, reabilitação, hospitalar, gerontologia), ética e legislação profissional.
NÃO repita (já existe): ensino de música.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Fundamentos da Musicoterapia: Conceito de Musicoterapia; História da Musicoterapia; Princípios teóricos e científicos; Áreas de atuação; Modelos de Musicoterapia." | "Processos Terapêuticos em Musicoterapia: Comunicação não verbal; Relação terapêutica; Expressão emocional; Desenvolvimento da criatividade; Organização e estruturação do comportamento." | "Avaliação em Musicoterapia: Avaliação inicial; Diagnóstico terapêutico; Planejamento terapêutico; Monitoramento e evolução; Registro de atendimentos." | "Musicoterapia e Saúde Mental: Transtornos de ansiedade; Depressão; Transtornos do humor; Transtornos psicóticos; Abordagem terapêutica." | "Musicoterapia em Neurologia e Reabilitação: Reabilitação neurológica; Acidente vascular cerebral (AVC); Doenças neurodegenerativas; Estimulação cognitiva; Reabilitação motora." | "Musicoterapia no Desenvolvimento Humano: Transtorno do espectro autista (TEA); Transtorno do déficit de atenção/hiperatividade (TDAH); Desenvolvimento infantil; Transtornos de aprendizagem." | "Musicoterapia na Saúde Coletiva: Atuação no SUS; Promoção da saúde; Prevenção de agravos; Atividades em grupo e comunidade." | "Musicoterapia em Contextos Específicos: Hospitais; CAPS (saúde mental); Instituições de longa permanência; Educação especial." | "Psicologia Aplicada à Musicoterapia: Teorias psicológicas; Desenvolvimento humano; Comportamento e emoção; Relação terapeuta–paciente." | "Ética Profissional: Código de ética do Musicoterapeuta; Sigilo profissional; Relação terapêutica; Responsabilidade profissional." | "Fundamentos da Musicoterapia: conceitos, história, princípios, objetivos, fundamentos teóricos e campos de atuação profissional." | "Formação e identidade profissional do musicoterapeuta: aspectos vivenciais, desenvolvimento da musicalidade clínica, competências profissionais e processo de formação."
```

### 26. Clínica e cirurgia de pequenos animais  (3 concursos, 8 tópicos)
```
Matéria 26. id: "medicina-veterinaria.clinica-cirurgia-pequenos-animais". subject: "Medicina Veterinária". Título: "Clínica e cirurgia de pequenos animais".
Cubra: semiologia, clínica médica de cães e gatos (doenças infecciosas, endócrinas, renais, dermatológicas), anestesiologia e cirurgia veterinária, emergências, terapêutica e farmacologia veterinária.
NÃO repita (já existe): produção animal; inspeção.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Normas para o controle de caninos no Exército Brasileiro (NORCCAN)." | "Imunoprofilaxia – vacinas e vacinações aplicadas a caninos e equinos." | "Morfologia e fisiologia canina." | "Comportamento e Etologia Canina." | "Comunicação canina." | "Nutrição canina." | "Clínica médica de pequenos animais."
```

### 27. Manutenção predial: pintura, alvenaria, hidráulica e elétrica básicas  (3 concursos, 5 tópicos)
```
Matéria 27. id: "manutencao.oficios-manutencao-predial". subject: "Manutenção". Título: "Manutenção predial: pintura, alvenaria, hidráulica e elétrica básicas".
Cubra: pintura (tintas, preparo de superfícies, patologias), alvenaria e revestimentos, hidráulica predial básica, elétrica básica e NR-10, ferramentas, segurança no trabalho e NR-35, cálculo de materiais.
NÃO repita (já existe): engenharia de edificações (nível superior).
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Chapisco, emboço e reboco." | "Acabamentos: Revestimento, reboco, arestamento, pavimentação, contrapiso em solo cimento." | "Massa corrida." | "Pintura de Paredes: Corrigindo buracos na parede." | "Projetos de engenharia civil (arquitetônico; fundações; estruturas – concreto, metálica, alvenaria estrutural; instalações – hidráulicas, elétricas; terraplenagem; pavimentação)."
```

### 28. Ciências atuariais  (3 concursos, 3 tópicos)
```
Matéria 28. id: "previdencia.ciencias-atuariais". subject: "Previdência". Título: "Ciências atuariais".
Cubra: matemática atuarial, tábuas de mortalidade, rendas aleatórias, regimes financeiros (repartição, capitalização), avaliação atuarial de RPPS e previdência complementar, provisões matemáticas, hipóteses atuariais, gestão de riscos atuariais e ética profissional.
NÃO repita (já existe): noções de RPPS já existentes.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): "Asset Liability Management (ALM); relação entre ativos e passivos atuariais."
```

### 29. Lei Orgânica Municipal: estrutura e conteúdo típico  (0 concursos, 0 tópicos)
```
Matéria 29. id: "legislacao-municipal.lei-organica-municipal". subject: "Legislação Municipal". Título: "Lei Orgânica Municipal: estrutura e conteúdo típico".
Cubra: o Município na CF/88 (autonomia, competências, arts. 29 a 31), estrutura típica de uma Lei Orgânica (organização do Município, Câmara Municipal, Prefeito, processo legislativo municipal, administração e servidores, finanças e orçamento, ordem econômica e social), simetria com a CF e com a Constituição Estadual; deixar claro que cada município tem a sua e o candidato deve ler a do seu edital.
NÃO repita (já existe): constituição federal geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): (só o título)
```

### 30. Estatuto do servidor municipal e estadual: conteúdo típico  (0 concursos, 0 tópicos)
```
Matéria 30. id: "legislacao-municipal.estatuto-servidor-municipal-estadual". subject: "Legislação Municipal". Título: "Estatuto do servidor municipal e estadual: conteúdo típico".
Cubra: como são os regimes jurídicos e estatutos de servidores de estados e municípios: provimento, posse, exercício, estágio probatório, estabilidade, vacância, remuneração, vantagens, férias, licenças, afastamentos, direitos e deveres, regime disciplinar, processo administrativo disciplinar, comparação com a Lei 8.112/1990 e pegadinhas comuns; deixar claro que o candidato deve ler o estatuto do seu edital.
NÃO repita (já existe): Lei 8.112 já existente.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): (só o título)
```

### 31. Administração municipal: organização, carreira e posturas  (0 concursos, 0 tópicos)
```
Matéria 31. id: "legislacao-municipal.administracao-municipal". subject: "Legislação Municipal". Título: "Administração municipal: organização, carreira e posturas".
Cubra: organização administrativa municipal (secretarias, autarquias, fundações), planos de cargos e carreiras, código de posturas, código tributário municipal (noções), processo administrativo municipal, controle interno e Câmara de Vereadores, ética do agente público municipal; conteúdo típico, sem citar lei de município específico.
NÃO repita (já existe): direito administrativo geral.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): (só o título)
```

### 32. Constituições estaduais: estrutura e simetria  (0 concursos, 0 tópicos)
```
Matéria 32. id: "legislacao-estadual.constituicoes-estaduais". subject: "Legislação Estadual". Título: "Constituições estaduais: estrutura e simetria".
Cubra: poder constituinte decorrente, simetria, estrutura típica das constituições estaduais (organização do Estado, Assembleia Legislativa, Governador, Tribunal de Justiça, TCE, Ministério Público e Defensoria estaduais, administração e servidores, finanças, ordem social), jurisprudência do STF sobre limites; deixar claro que cada estado tem a sua.
NÃO repita (já existe): constituição federal.
Aliases: tópicos reais de edital, copie exatamente, mas SÓ os que forem deste assunto (descarte os que não forem): (só o título)
```
