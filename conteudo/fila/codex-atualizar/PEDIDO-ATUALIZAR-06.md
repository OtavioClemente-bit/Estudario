# Atualizar matérias — pedido 06 de 18 (09/10/2026)

Uma revisão de todas as ligações edital → matéria mostrou que estas matérias **pertencem** aos tópicos
dos editais, mas estão rasas ou incompletas para o nível cobrado. Reescreva cada uma, mantendo o mesmo id,
título e escopo central, e **acrescente** o que falta (lista abaixo, tirada dos tópicos reais dos editais,
em ordem de importância).

Leia antes (só leitura): `AGENTS.md`, `conteudo/fila/PEDIDO-MATERIA-AGENTE.md`,
`conteudo/fila/FORMATO-VISUAL.md` e o modelo `conteudo/materias/direito-penal/teoria-do-crime.json`.

## Regras
- Abra a matéria publicada (caminho ao lado do id) e parta dela: aproveite o que está certo, corrija o
  que estiver raso, e cubra os itens de "falta". Se um item for de outra matéria, ignore-o.
- "version" = versão publicada + 1. Mesmo "id".
- 5 a 7 capítulos com 2.500+ caracteres úteis cada (tabelas comparativas, artigos de lei citados quando
  tiver certeza); 45+ questões (A–E e Certo/Errado, dificuldades equilibradas, gabaritos equilibrados),
  cobrindo também os itens novos; 15+ flashcards, dicas, pegadinhas, recordação ativa, fontes reais.
- Distrator bom = erro de quem estudou mal (instituto vizinho, prazo trocado, exceção esquecida). Nada de
  "sempre/nunca/exclusivamente" caricato; a certa não pode ser sempre a mais longa.
- Lei/súmula/tema que você não tem certeza: explique o conteúdo sem o número. Nunca invente norma.
- Grave cada matéria como `<id>.json` **na pasta desta conversa**, uma por vez, inteira, à mão.
- Rode `deno run --allow-read C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/fila/conferir.ts <id>.json`
  até "validador: OK". Não faça commit nem push. Não escreva no projeto.

## Matérias (nesta ordem)

### 1. `direito-constitucional.conceito-classificacao-constituicao` — Conceito e classificação das constituições
Arquivo atual: `conteudo/materias/direito-constitucional/conceito-classificacao-constituicao.json` · 37 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre aplicabilidade das normas constitucionais (plena, contida, limitada) e 6 questões
- capítulo sobre o contexto histórico da Constituição de 1988 (Assembleia Nacional Constituinte, transição democrática, constituições brasileiras anteriores) e 4 questões
- capítulo sobre neoconstitucionalismo (marcos histórico, filosófico e teórico; força normativa, princípios, ponderação, constitucionalização do direito) e 5 questões
- capítulo sobre constitucionalismo (origem, conceito, fases antigo, moderno e contemporâneo) e 5 questões
- seção sobre Estado e Constituição (elementos do Estado, Estado de Direito, relação entre Estado e ordem constitucional) e 4 questões
- seção sobre o Direito Constitucional como ramo do direito (natureza, conceito, objeto, divisões e métodos) e 4 questões
- capítulo sobre a história das constituições (Magna Carta, constituições norte-americana e francesa, constitucionalismo social, Weimar) e 5 questões
- seção sobre origens da Constituição e do constitucionalismo (Inglaterra, EUA, França) e 4 questões
- capítulo sobre o contexto histórico da Constituição de 1988 (Assembleia Constituinte, constituições brasileiras anteriores) e 4 questões
- capítulo sobre aplicabilidade e eficácia das normas constitucionais (plena, contida, limitada; normas programáticas) e 6 questões
- capítulo sobre aplicabilidade das normas constitucionais (eficácia plena, contida e limitada) e 6 questões
- capítulo sobre constitucionalismo e neoconstitucionalismo e 6 questões

### 2. `processo-civil.execucao` — Processo de execução de título extrajudicial
Arquivo atual: `conteudo/materias/processo-civil/execucao.json` · 33 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre as espécies de execução: entrega de coisa, fazer e não fazer, Fazenda Pública e alimentos, com 10 questões
- capítulo sobre as espécies de execução: entrega de coisa (arts. 806 a 813), obrigações de fazer e não fazer (arts. 814 a 823), execução contra a Fazenda Pública e de alimentos, com 10 questões
- capítulo sobre execução para entrega de coisa certa e incerta (arts. 806 a 813): citação, prazo de 15 dias, depósito, multa, benfeitorias, conversão em perdas e danos, com 8 questões
- capítulo sobre execução de obrigações de fazer e não fazer (arts. 814 a 823): multa, prazo, fungibilidade, refazer a obra e conversão em perdas e danos, com 8 questões
- capítulo sobre embargos de terceiro (arts. 674 a 681): legitimidade, prazo, objeto, Súmula 84 do STJ, fraude e procedimento, com 8 questões
- capítulo sobre execução para entrega de coisa (arts. 806 a 813) e demais espécies, com 8 questões
- capítulos sobre execução para entrega de coisa e para obrigações de fazer e não fazer (arts. 806 a 823), com 10 questões
- seção sobre exceção ou objeção de pré-executividade: cabimento, matérias de ordem pública, prova pré-constituída e Súmula 393 do STJ, com 6 questões
- capítulos sobre execução para entrega de coisa e obrigações de fazer e não fazer (arts. 806 a 823), com 10 questões
- capítulos sobre entrega de coisa, fazer e não fazer, e execução contra a Fazenda Pública (arts. 910 e seguintes), com 12 questões
- capítulo sobre execução de obrigações de fazer e não fazer (arts. 814 a 823), com 8 questões
- seção sobre embargos à adjudicação, à alienação e à arrematação (art. 903 e art. 877, § 1º): prazo, hipóteses e procedimento, com 5 questões

### 3. `informatica.estruturas-dados-algoritmos` — Estruturas de dados e algoritmos
Arquivo atual: `conteudo/materias/informatica/estruturas-dados-algoritmos.json` · 40 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre hashing (função hash, colisões, tratamento) além da ordenação e pesquisa, e 4 questões
- seção sobre lógica de programação: variáveis, operadores, estruturas de seleção e repetição e teste de mesa, e 6 questões
- seção sobre depuração e teste de algoritmos e sobre estruturas de controle, com 5 questões
- seção sobre tipos de dados simples (inteiro, real, lógico, caractere) e estruturados (vetor, matriz, registro), e 5 questões
- seção sobre Portugol/VisuAlg, fluxograma e diagrama de Chapin, com exemplos e 6 questões
- seção sobre classificação de dados e tipos abstratos de dados (TAD), com 4 questões
- seção sobre grafos (conceito, representação, percursos), com 5 questões
- seção sobre variáveis e tipos de dados em pseudocódigo, com 4 questões
- seção sobre funções e procedimentos (parâmetros, retorno, escopo), com 5 questões
- seção sobre variáveis e constantes (declaração, escopo, tipos), com 4 questões
- seção sobre comandos de atribuição, entrada e saída, com 4 questões
- seção sobre avaliação de expressões aritméticas, lógicas e relacionais e precedência, com 5 questões

### 4. `administracao-publica.transparencia-administracao-publica` — Transparência na Administração Pública
Arquivo atual: `conteudo/materias/administracao-publica/transparencia-administracao-publica.json` · 24 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre controle social e cidadania: conceito, instrumentos (conselhos de políticas, ouvidorias, audiências e consultas públicas, orçamento participativo, denúncia aos tribunais de contas), Lei 13.460/2017 e cidadania ativa, com 6 questões.
- Capítulo sobre governo eletrônico (e-gov, governo digital e governo aberto), controle social, cidadania e os tipos de accountability, ligados à transparência, com 7 questões.
- Capítulo sobre participação social: atores (cidadãos, ONGs, conselhos, mídia), conselhos gestores, orçamento participativo, audiências, consulta pública e limites da participação, com 6 questões.
- Seção sobre cidadania, equidade social e qualidade na gestão pública (transparência como direito do cidadão, carta de serviços, avaliação do usuário, equidade no acesso), com 5 questões.
- Seção sobre accountability: vertical, horizontal e societal, accountability eleitoral, prestação de contas e responsabilização, diferença para responsividade, com 5 questões.
- Seção sobre controle social: conceito, formas (conselhos, ouvidorias, audiências, denúncias, portais), base constitucional (art. 74, §2º, e arts. 194 e 198) e diferença para controle interno e externo, com 6 questões.
- Seção sobre coprodução do bem público e participação cidadã (cidadão como parceiro da política pública, laboratórios de inovação, consultas, orçamento participativo) ligada à transparência ativa, com 5 questões.
- Capítulo sobre participação e controle social (conselhos, audiências, orçamento participativo, ouvidoria, consulta pública) juntamente com transparência, com 6 questões.
- Capítulo sobre administração pública participativa (democracia participativa, gestão social, conselhos gestores, orçamento participativo, consultas e audiências públicas, parcerias com a sociedade), com 6 questões.
- Seção sobre governo digital (Lei 14.129/2021, dados abertos, serviços digitais) e controle social como parte da transparência, com 6 questões.
- Seção sobre participação cidadã: formas institucionais (conselhos, conferências, audiências, orçamento participativo, iniciativa popular), vantagens e limites, com 5 questões.
- Seção sobre audiências e consultas públicas, conselhos, conferências e demais instrumentos de participação (base legal, finalidade, caráter consultivo ou deliberativo) com 5 questões.

### 5. `processo-civil.coisa-julgada` — Coisa julgada
Arquivo atual: `conteudo/materias/processo-civil/coisa-julgada.json` · 22 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre ação rescisória em profundidade: cada hipótese do art. 966, I a VIII, competência originária dos tribunais, petição inicial e depósito, prazos, rescisão de capítulo, decisões não de mérito rescindíveis e rescisória contra decisão baseada em precedente, com 10 questões
- capítulo sobre sentença: conceito e classificação, requisitos essenciais do art. 489 e fundamentação, vícios (extra, ultra e citra petita), efeitos como a hipoteca judiciária, sentença sem e com resolução de mérito (arts. 485 e 487), com 8 questões
- capítulo sobre coisa julgada inconstitucional: inexigibilidade do título fundado em norma declarada inconstitucional (arts. 525, § 12, e 535, § 5º), marco temporal da decisão do STF e limites da rescisória, com 6 questões
- capítulo sobre meios autônomos de impugnação: ação anulatória do art. 966, § 4º, querela nullitatis, mandado de segurança contra ato judicial e embargos de terceiro, comparados à rescisória, com 6 questões
- explicar que a ação declaratória incidental do CPC/1973 foi extinta pelo CPC/2015 e substituída pela extensão da coisa julgada à questão prejudicial (art. 503, § 1º), além da declaração incidental pela ação declaratória (art. 19) e pela reconvenção, com 4 questões
- capítulo sobre relativização da coisa julgada: teorias (proporcionalidade, coisa julgada inconstitucional), posição do STF e do STJ, art. 525, § 12, e querela nullitatis, com 6 questões
- capítulo sobre a relação entre rescisória e vícios que a dispensam (querela nullitatis por falta ou nulidade de citação, art. 525, § 1º, I) e nulidades da sentença, com 6 questões
- capítulo sobre sentença, remessa necessária (art. 496: hipóteses e dispensas) e execução provisória (arts. 520 a 522), além do aprofundamento da rescisória, com 10 questões
- capítulo sobre sentença (requisitos e efeitos) e sobre a preclusão e a coisa julgada em relação à resposta do réu, com 6 questões
- capítulo sobre as eficácias da coisa julgada: negativa (impede nova decisão), positiva (vincula processos futuros) e preclusiva (art. 508), com exemplos e 5 questões
- capítulo sobre classificação das sentenças (autossuficientes e não autossuficientes, declaratórias, constitutivas e condenatórias) e seus efeitos, com 5 questões
- capítulo sobre litispendência e coisa julgada como pressupostos processuais negativos (art. 337, §§ 1º a 4º): tríplice identidade, efeitos de cada uma e 5 questões

### 6. `direito-empresarial.titulos-credito` — Títulos de crédito
Arquivo atual: `conteudo/materias/direito-empresarial/titulos-credito.json` · 35 tópicos de edital pedem mais conteúdo

Falta:
- classificação quanto à circulação (ao portador, à ordem e nominativos; arts. 904 a 926 do Código Civil) com capítulo e 6 questões
- capítulo sobre ações cambiais (execução, ação de regresso, ação causal, monitória, enriquecimento sem causa) e 6 questões
- integração da Lei Uniforme de Genebra ao direito brasileiro (letras e notas, cheques) e sua convivência com o Código Civil, com 5 questões
- breve histórico da legislação cambiária (origem, Genebra, Código Civil de 2002) e 3 questões
- capítulos sobre cédulas e títulos de crédito comercial, industrial, rural, à exportação, imobiliário e bancário (requisitos, garantias, circulação) e 8 questões
- letra de arrendamento mercantil (natureza, emissão, circulação) e 3 questões
- ação de anulação e substituição de título (extravio, destruição, procedimento) e 4 questões
- capítulo sobre protesto cambiário (Lei 9.492/97: finalidades, espécies, efeitos) e sobre prescrição geral dos títulos, com 6 questões
- capítulo sobre protesto (Lei 9.492/97: espécies, procedimento, cancelamento, efeitos) e 6 questões; a matéria só toca o protesto da duplicata
- classificação quanto à circulação (ao portador, à ordem e nominativos) com capítulo e 6 questões
- capítulo sobre protesto cambiário (por falta de aceite, de pagamento, por indicação, efeitos) e 6 questões
- capítulo sobre invalidades dos títulos de crédito (falta de requisitos, incapacidade, falsidade, vícios e efeitos sobre as demais obrigações) e 5 questões

### 7. `direito-tributario.competencia-tributaria` — Competência tributária
Arquivo atual: `conteudo/materias/direito-tributario/competencia-tributaria.json` · 29 tópicos de edital pedem mais conteúdo

Falta:
- capítulo com o rol de tributos de competência da União (art. 153, 154, 148, 149, 145, 195 da CF): impostos, contribuições, empréstimos compulsórios, taxas e contribuição de melhoria, com 8 questões
- capítulo sobre a repartição dos impostos entre União, Estados, DF e Municípios (arts. 153 a 156 e 147 da CF), com 8 questões
- capítulo de ligação entre competência tributária e limitações constitucionais ao poder de tributar (legalidade, anterioridade, imunidades), com 6 questões
- capítulo sobre discriminação constitucional das rendas tributárias (por fontes e pelo produto) e repartição de receitas (arts. 157 a 162 da CF), com 8 questões
- capítulo sobre repartição das receitas tributárias (arts. 157 a 162 da CF: FPM, FPE, IPI-exportação, ICMS, cota-parte dos municípios), com 8 questões
- capítulo com a enumeração das competências de União, Estados, DF e Municípios (art. 153 a 156 da CF) e 8 questões
- capítulo sobre o não exercício da competência tributária (facultatividade, caso do ITR/IGF e art. 8º do CTN), e 4 questões
- capítulo com os tributos de competência dos Estados e do DF (ICMS, IPVA, ITCMD, taxas, contribuição previdenciária do servidor e COSIP no DF) e 6 questões
- capítulo com os tributos de competência dos Municípios (IPTU, ITBI, ISS, taxas, COSIP, contribuição previdenciária) e 6 questões
- capítulo sobre Sistema Tributário Nacional e princípios constitucionais tributários (legalidade, anterioridade, isonomia, vedação ao confisco), com 8 questões
- capítulo de visão geral do Sistema Tributário Nacional (CF e CTN): espécies, princípios, competências e repartição, com 8 questões
- capítulo sobre o CTN, normas gerais em matéria tributária e papel da lei complementar (art. 146 da CF), com 6 questões

### 8. `portugues.tipologia-textual` — Tipologia textual
Arquivo atual: `conteudo/materias/portugues/tipologia-textual.json` · 20 tópicos de edital pedem mais conteúdo

Falta:
- tipos textuais por finalidade (informativo, publicitário, propagandístico, normativo, didático e divinatório) com as marcas linguísticas de cada tipo, com 10 questões
- organização estrutural dos textos: introdução, desenvolvimento e conclusão, estrutura do parágrafo (tópico frasal) e da narrativa e da dissertação, com 8 questões
- características dos textos literários e não literários (linguagem conotativa e denotativa, plurissignificação, função estética, ficcionalidade) em capítulo próprio, com 6 questões; e tipologia da frase portuguesa (declarativa, interrogativa, imperativa, exclamativa e optativa; frase nominal e verbal), com 6 questões
- características dos textos literários e não literários (linguagem conotativa e denotativa, plurissignificação, função estética, ficcionalidade) em capítulo próprio, com 6 questões
- características dos textos literários e não literários (linguagem conotativa e denotativa, plurissignificação, função estética, ficcionalidade) em capítulo próprio, com 6 questões; e tipos textuais por finalidade (informativo, publicitário, propagandístico, normativo, didático e divinatório) com as marcas linguísticas de cada tipo, com 10 questões
- tipologia da frase portuguesa (declarativa, interrogativa, imperativa, exclamativa e optativa) e estrutura da frase com ordem direta e inversa, com 8 questões
- organização retórica do texto expositivo (generalização, especificação, exemplificação, definição, classificação, explanação e elaboração) em capítulo próprio, com 6 questões
- gêneros epistolares (carta argumentativa, familiar, comercial e convite) e a dissertação como tipo, com características composicionais, com 6 questões

### 9. `direito-civil.familia` — Direito de Família
Arquivo atual: `conteudo/materias/direito-civil/familia.json` · 36 tópicos de edital pedem mais conteúdo

Falta:
- esponsais (promessa de casamento): natureza, efeitos e responsabilidade pelo rompimento, com 2 questões
- habilitação, celebração, eficácia do casamento e direitos e deveres dos cônjuges, inclusive casamento homoafetivo, com 5 questões
- bem de família (legal e convencional) e suas regras de impenhorabilidade, com 4 questões
- bem de família (legal e convencional): impenhorabilidade, hipóteses de exceção e regras do Código Civil, com 4 questões
- bem de família (legal e convencional): impenhorabilidade e exceções, com 4 questões
- alienação parental: conceito, atos típicos, consequências, medidas judiciais e relação com guarda, com 5 questões
- capítulo sobre adoção (requisitos, efeitos, irrevogabilidade, adoção pelo cônjuge, consentimento) e investigação de paternidade, com 6 questões
- repercussões civis da violência doméstica e da violência obstétrica no direito de família (guarda, alimentos, destituição do poder familiar), com 4 questões
- capítulo sobre princípios constitucionais do direito de família (dignidade, igualdade entre cônjuges e filhos, afetividade, convivência familiar, melhor interesse) e 5 questões
- ação de alimentos de rito especial (Lei 5.478/1968): petição, audiência, alimentos provisórios, revisão e exoneração, com 5 questões
- investigação de paternidade e reconhecimento de filhos (Lei 8.560/1992): averiguação oficiosa, presunção, alimentos, com 5 questões
- alienação parental (Lei 12.318/2010): atos, perícia, sanções e guarda, com 5 questões

### 10. `direito-administrativo.extincao-ato-administrativo` — Extinção do ato: anulação, revogação, convalidação, cassação e caducidade
Arquivo atual: `conteudo/materias/direito-administrativo/extincao-ato-administrativo.json` · 17 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre teoria das nulidades no direito administrativo (ato nulo, anulável e inexistente; vícios em cada elemento do ato; efeitos ex tunc e ex nunc) e 8 questões
- seção sobre prescrição administrativa (prazos de prescrição das ações contra a Fazenda e da pretensão punitiva, distinção entre decadência e prescrição) e 5 questões
- seção sobre ato administrativo inexistente (conceito, diferença para o nulo, imprescritibilidade) e 4 questões
- capítulo sobre vícios do ato administrativo (competência, finalidade, forma, motivo e objeto) e 6 questões
- capítulo sobre ato inexistente, nulidades e vícios, incluindo a teoria dos motivos determinantes, com 8 questões
- seções sobre silêncio administrativo, vinculação e discricionariedade (relação com a revogação e a anulação) e 6 questões
- capítulo sobre invalidade e nulidade do ato (nulo x anulável, efeitos da declaração) e 6 questões
- capítulo sobre ato de governo, não ato e teoria dos vícios e defeitos do ato, com 6 questões
- seção sobre preservação do ato: ratificação, reforma e conversão, ao lado da convalidação, e 5 questões

### 11. `economia.microeconomia-oferta-demanda` — Microeconomia: oferta, demanda e equilíbrio
Arquivo atual: `conteudo/materias/economia/microeconomia-oferta-demanda.json` · 36 tópicos de edital pedem mais conteúdo

Falta:
- capítulo introdutório de conceitos fundamentais (escassez, custo de oportunidade, fronteira de possibilidades de produção, sistemas econômicos, fluxo circular, variáveis estoque e fluxo) e 6 questões
- seção sobre subsídios (incidência econômica do subsídio, efeito sobre preços, quantidade e excedentes) além do imposto, com 5 questões
- seção sobre elasticidade-preço da oferta (cálculo, classificação, fatores como prazo e capacidade ociosa) e 5 questões
- seção sobre elasticidade-preço da oferta (hoje só uma linha em tabela), com fatores determinantes, e 5 questões
- seção sobre os determinantes do peso morto (quanto mais elásticas oferta e demanda, maior a perda; relação com o quadrado da alíquota) e 4 questões
- seção sobre incidência de tributos em concorrência perfeita e em monopólio (monopólio não é tratado) e 5 questões
- seção sobre classificação de bens (normais, inferiores, de luxo e necessários) pela elasticidade-renda e 5 questões
- capítulo sobre efeito-renda e efeito-substituição, curva renda-consumo e curva de Engel, e 6 questões
- capítulo sobre teoria do consumidor (preferências, restrição orçamentária, curvas de indiferença, equilíbrio do consumidor) e 8 questões
- capítulo sobre subsídios e tarifas (efeito sobre eficiência e distribuição da renda), além dos impostos já tratados, com 6 questões
- seção sobre classificação de bens, incluindo bens de Giffen e bens inferiores, e 5 questões
- capítulo sobre demanda de mercado e receita total, média e marginal, com a relação entre elasticidade e receita total, e 6 questões

### 12. `direito-trabalho.seguranca-medicina-trabalho` — Segurança e medicina do trabalho: CIPA, insalubridade e periculosidade
Arquivo atual: `conteudo/materias/direito-trabalho/seguranca-medicina-trabalho.json` · 42 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre as Normas Regulamentadoras em geral (Portaria 3.214/1978, estrutura das NRs, obrigações do empregador e do empregado, fiscalização e embargo/interdição) e 8 questões
- seção de visão geral de segurança e higiene do trabalho (arts. 154 a 201 da CLT: SESMT, exames médicos, edificações, máquinas, prevenção da fadiga) e 6 questões
- seção de visão geral de saúde, higiene e segurança do trabalho (SESMT, exames médicos e programas de prevenção) e 6 questões
- capítulo sobre meio ambiente do trabalho (art. 200, VIII e art. 225 da CF; conceito, princípios, responsabilidade do empregador e efeitos jurídicos) e 8 questões
- capítulo sobre engenharia de segurança e higiene do trabalho (riscos, medidas de controle, NRs principais) e 8 questões
- seção de visão geral de segurança e higiene do trabalho (SESMT, exames médicos, edificações, máquinas) e 6 questões
- seção sobre segurança e higiene do trabalho em geral (SESMT, exames médicos, programas de prevenção) e referências à discriminação; trabalho de menores e mulheres devem ser ligados às matérias próprias
- seção sobre a Lei 6.514/1977, que reformulou o capítulo de segurança e medicina na CLT, e sobre o conjunto CLT e normas regulamentadoras, com 5 questões
- seção sobre a Portaria 3.214/1978 que aprova as NRs, sua natureza jurídica e relação com a CLT, com 4 questões
- seção sobre política de saúde e segurança nas empresas (SESMT, programas de prevenção, responsabilidades) e 6 questões
- seção sobre as Normas Regulamentadoras em geral (Portaria 3.214/1978, estrutura das NRs, obrigações do empregador e do empregado, fiscalização e embargo/interdição) e 8 questões, mais as convenções e recomendações da OIT ratificadas
- capítulo sobre programas de gestão de riscos (PPRA/PGR e PCMSO), ergonomia, qualidade de vida e riscos psicossociais, com 8 questões

### 13. `portugues.generos-textuais` — Gêneros textuais e tipos textuais
Arquivo atual: `conteudo/materias/portugues/generos-textuais.json` · 12 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre a tipologia informativo, publicitário, propagandístico, normativo, didático e divinatório (definição, exemplos de cada e diferença entre publicitário e propagandístico), com 10 questões
- Seção sobre textos literários e não literários (linguagem conotativa x denotativa, função poética, gêneros literários narrativo, lírico e dramático) com 8 questões
- Capítulo sobre a tipologia informativo, publicitário, propagandístico, normativo, didático e divinatório (definição, exemplos de cada e diferença entre publicitário e propagandístico), com 10 questões e sobre textos literários e não literários
- Seção sobre a classificação dos tipos narrativo, descritivo, dissertativo, injuntivo, expositivo e dialogal (o dialogal não aparece) com 6 questões
- Seção sobre produção de textos nos gêneros cobrados (estrutura, registro e finalidade ao escrever), pois a matéria só treina o reconhecimento, com 6 questões
- Seção sobre gêneros da esfera cotidiana (e-mail, recado, chat, regras comunitárias, comentários em redes) com função social e registro, e 8 questões
- Seção sobre gêneros interpessoais do cotidiano (e-mails, bilhetes, fóruns, comentários em redes sociais, chats) com função social e propósito, e 8 questões

### 14. `conhecimentos-bancarios.mercado-capitais` — Mercado de capitais
Arquivo atual: `conteudo/materias/conhecimentos-bancarios/mercado-capitais.json` · 39 tópicos de edital pedem mais conteúdo

Falta:
- trecho sobre bolsas de mercadorias e de futuros (contratos futuros, ajuste diário, margem de garantia) e 4 questões
- trecho sobre commercial papers (nota promissória comercial): emissor, prazo, oferta e diferença para debêntures, com 3 questões
- trecho sobre diferenças entre companhia aberta e fechada (registro na CVM, negociação, divulgação) e 4 questões
- trecho sobre underwriting (garantia firme, melhores esforços, subscrição) e 4 questões
- trecho sobre funcionamento do mercado à vista (lote padrão e fracionário, formação de preço, liquidação) e 4 questões
- trecho sobre mercado de balcão organizado e não organizado e 4 questões
- trecho sobre operações com ouro como ativo financeiro (negociação em bolsa, tributação básica, características) e 3 questões
- capítulo sobre derivativos, com o mercado a termo (funcionamento, risco, liquidação) e 4 questões; a matéria não tem nenhum derivativo
- capítulo sobre mercado de opções (call e put, prêmio, preço de exercício, posições) e 5 questões
- capítulo sobre mercado futuro (ajuste diário, margem, hedge e especulação) e 5 questões
- trecho sobre operações de swap (troca de indexadores, funcionamento básico) e 4 questões
- capítulo sobre instrumentos do mercado financeiro e de capitais (renda fixa, renda variável, derivativos) e 6 questões

### 15. `direito-administrativo.intervencao-estado-propriedade` — Intervenção do Estado na propriedade
Arquivo atual: `conteudo/materias/direito-administrativo/intervencao-estado-propriedade.json` · 30 tópicos de edital pedem mais conteúdo

Falta:
- desapropriação indireta (apossamento administrativo, ação indenizatória, prescrição), desapropriação por zona e objeto/beneficiários da desapropriação (bens expropriáveis, delegatários), com cerca de 8 questões
- capítulo sobre desapropriação indireta (conceito, apossamento sem procedimento, ação indenizatória, prescrição, súmulas do STJ) e 6 questões
- capítulo sobre retrocessão (natureza jurídica, direito de preferência, tredestinação lícita x ilícita, art. 519 CC) e 5 questões
- capítulo sobre intervenção do Estado no domínio econômico (arts. 173 e 174 CF: monopólio, repressão ao abuso do poder econômico, controle de preços, regulação) e 6 questões
- capítulo sobre a ação de desapropriação (DL 3.365: petição inicial, contestação limitada a vício do processo e preço, perícia, sentença, juros compensatórios e moratórios, honorários) e 8 questões
- hipóteses de utilidade pública do art. 5º do DL 3.365 e de interesse social do art. 2º da Lei 4.132, e rito da reforma agrária (LC 76/1993), com 6 questões
- capítulo sobre o procedimento expropriatório em fases (declaratória, executória administrativa e judicial, acordo, imissão, sentença, registro) e 6 questões
- estudo sistemático do DL 3.365/41: hipóteses do art. 5º, competência, contestação (art. 20), indenização (arts. 26 e 27), juros e honorários, com 8 questões
- hipóteses do art. 2º da Lei 4.132/62, prazos e indenização, com 5 questões específicas
- desapropriação por zona, retrocessão e desapropriação indireta (capítulos próprios e 8 questões)
- conceito de polícia edilícia e posturas municipais (alvará, licença de construção, código de obras), com 4 questões
- zonas fortificadas, faixa de fronteira e limitações florestais/ambientais à propriedade, com 5 questões

### 16. `economia.microeconomia-estruturas-mercado` — Microeconomia: estruturas de mercado e custos
Arquivo atual: `conteudo/materias/economia/microeconomia-estruturas-mercado.json` · 37 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre regulação de monopólio natural e formação de preços regulados (preço igual ao custo marginal, custo médio, price cap, Ramsey) e 6 questões
- capítulo sobre teoria da produção e da firma (função de produção, curto e longo prazos, rendimentos) e 6 questões
- seção sobre monopsônio (poder de compra, preço e quantidade) e oligopsônio ao lado de monopólio e oligopólio, e 5 questões
- capítulo sobre teoria da produção: isoquantas, taxa técnica de substituição, lei dos rendimentos decrescentes, curto e longo prazos, e 8 questões
- capítulo sobre fatores de produção, isoquantas, substituição entre insumos, rendimentos decrescentes e excedente do produtor, e 8 questões
- seção sobre curva de oferta da firma e da indústria no curto e no longo prazo (soma horizontal, entrada e saída) e 5 questões
- capítulo sobre modelos clássicos de oligopólio (Cournot, Bertrand, Edgeworth), cartéis, liderança de preços e comparação com concorrência perfeita, e 8 questões
- capítulo sobre modelos de mark-up, demanda quebrada, concentração, barreiras à entrada e diferenciação de produto, e 6 questões
- capítulo sobre teoria da firma e da produção (função de produção, produtividade, rendimentos) ligada a custos e maximização de lucro, e 6 questões
- capítulo sobre fatores de produção, produtividade, rendimentos (decrescentes e de escala) e oferta da firma, e 6 questões
- capítulo sobre regulação e formação de preços em estruturas de concorrência imperfeita (monopólio natural, price cap, taxa de retorno) e 6 questões
- capítulo sobre regulação e formação de preços em mercados de concorrência imperfeita (preço regulado, price cap, custo médio) e 6 questões

### 17. `direito-tributario.suspensao-extincao-exclusao` — Suspensão, extinção e exclusão do crédito tributário
Arquivo atual: `conteudo/materias/direito-tributario/suspensao-extincao-exclusao.json` · 28 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre pagamento (arts. 157 a 164: local, prazo, imputação, juros de mora, correção) e sobre pagamento indevido (arts. 165 a 169), além de 6 questões.
- capítulo sobre repetição do indébito (arts. 165 a 169 do CTN): hipóteses, legitimidade e tributos indiretos (art. 166), prazo, juros e correção, ação anulatória de decisão administrativa denegatória, e 8 questões.
- seção ampliada sobre pagamento (arts. 157 a 164): local e prazo, imputação, juros e multa de mora, pagamento parcial, e 5 questões.
- seção sobre a ação de repetição de indébito: legitimidade, tributos indiretos, prazo prescricional de cinco anos, juros e correção, e 6 questões.
- seção sobre prazos e termos de decadência (arts. 150, § 4º, e 173) e prescrição (art. 174), interrupção e suspensão, e 8 questões; o recorte atual exclui esses prazos.
- capítulo sobre repetição do indébito (arts. 165 a 169): hipóteses, art. 166, prazo, juros e correção, e 8 questões.
- seção sobre a LC 118/2005: contagem do prazo de cinco anos para repetição a partir do pagamento antecipado (art. 3º), interrupção da prescrição pelo despacho que ordena a citação (art. 174, parágrafo único, I) e demais alterações no CTN, com 4 questões.
- capítulo sobre restituição do indébito (arts. 165 a 169): hipóteses, tributo indireto (art. 166), prazo, juros e correção, e 6 questões.
- seções sobre restituição do indébito e sobre prazos de prescrição e decadência, que a matéria não traz, com 8 questões.
- seção sobre restituição de tributo que comporte transferência do encargo financeiro (art. 166), restituição proporcional de juros e multas (art. 167) e correção monetária, com 6 questões.
- seção sobre pagamento indevido (art. 165): hipóteses (cobrança ou pagamento espontâneo indevido ou maior, erro de alíquota ou cálculo, reforma de decisão condenatória), prazo e juros, e 5 questões.
- seções sobre repetição do indébito e sobre os prazos de decadência e prescrição, com 8 questões.

### 18. `portugues.frase-oracao-periodo` — Frase, oração e período
Arquivo atual: `conteudo/materias/portugues/frase-oracao-periodo.json` · 21 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre classes de palavras na estrutura do período (substantivo, adjetivo, verbo, advérbio, preposição e conjunção reconhecidos pela função na oração), com 6 questões
- capítulo sobre a análise do período composto: classificação das orações coordenadas (assindética e sindéticas) e subordinadas (substantivas, adjetivas, adverbiais) com 8 questões
- capítulo sobre os termos da oração (sujeito, predicado, objetos direto e indireto, predicativo, adjuntos e complementos) com identificação em frases e 8 questões
- seção sobre operações na estrutura da frase: deslocamento de termos, substituição de segmentos, modificação e correção de construções, com 8 questões de reescrita
- capítulo sobre os termos da oração (sujeito, predicado, objetos, predicativo, adjuntos, complementos nominais) com análise de frases e 8 questões
- seção sobre norma padrão e pontuação na organização do período (vírgula entre termos e orações, ponto e vírgula, dois-pontos) e 8 questões
- seção sobre norma culta e pontuação na estrutura do período (vírgula, ponto e vírgula, dois-pontos e travessão) e 8 questões
- seção sobre problemas de construção da frase (ambiguidade, falta de paralelismo, truncamento, repetição) e emprego da norma culta, com 8 questões
- seção sobre norma culta (concordância e regência em nível básico) e pontuação na estrutura da frase, com 8 questões
- seção sobre norma culta e pontuação na organização sintática da frase, com 8 questões
- seção sobre operações de deslocamento, substituição, modificação e correção na estrutura da frase, com 8 questões de reescrita
- seção sobre operações estruturais na frase (deslocamento, substituição, modificação, correção) e problemas de construção como ambiguidade e falta de paralelismo, com 8 questões
