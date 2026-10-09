# Atualizar matérias — pedido 10 de 18 (09/10/2026)

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

### 1. `informatica.python-r-api` — Python, R, APIs e ETL
Arquivo atual: `conteudo/materias/informatica/python-r-api.json` · 18 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre análise de dados com Pandas, NumPy e Jupyter (Series, DataFrame, vetorização, notebooks) e noções de IA aplicada, com 8 questões
- capítulo sobre NumPy (ndarray, vetorização), Pandas (Series e DataFrame), SciPy e Matplotlib, com 8 questões
- capítulo sobre Python e R aplicados à ciência de dados (Pandas, NumPy, tidyverse, visualização) e 6 questões
- capítulo sobre NumPy, Pandas, SciPy, Matplotlib, Seaborn, Streamlit e noções de TensorFlow, Keras e PyTorch, com 8 questões
- capítulo sobre pacotes do R (dplyr, ggplot2, tidyr, readr) e o uso de bibliotecas, com 6 questões
- seção sobre coleta de dados por raspagem web (web scraping: requisição, parsing de HTML, limites éticos e legais) em complemento a APIs e ETL, com 5 questões
- capítulo sobre pandas, numpy, scikit-learn, matplotlib e seaborn (carga, limpeza, modelagem básica e gráficos) e 8 questões
- capítulo sobre Python e R para ciência de dados (bibliotecas de manipulação, estatística e visualização) e 6 questões
- capítulo sobre as bibliotecas Pandas, NumPy, SciPy, Matplotlib, Scikit-learn e noções de TensorFlow e PyTorch, com 8 questões
- seção sobre formatos de troca e carga em ETL (XML, JSON, CSV: estrutura, vantagens, leitura e escrita) e tecnologias de ETL, com 6 questões
- capítulo sobre Pandas (Series, DataFrame, leitura de dados, seleção, groupby, merge) e 8 questões
- seção sobre NumPy (ndarray, operações vetorizadas, broadcasting, indexação e fatiamento) e 8 questões

### 2. `informatica.microsoft-powerpoint-2016` — Microsoft PowerPoint 2016
Arquivo atual: `conteudo/materias/informatica/microsoft-powerpoint-2016.json` · 10 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre listas numeradas e com marcadores (formatação de parágrafo, níveis) e 4 questões
- seção sobre inserção e edição de tabelas, gráficos, planilhas incorporadas e SmartArt/organogramas, com 5 questões
- seção sobre slide mestre, layouts personalizados, esquema de cores e formatação de plano de fundo, com 5 questões
- seção sobre integração com Word e Excel (colar vinculado ou incorporado, objetos OLE, importar tópicos do Word) e 4 questões
- seção sobre o PowerPoint do Microsoft 365 (Designer, Morph, Zoom, gravação, coautoria na nuvem) e 6 questões
- seção sobre listas numeradas e com marcadores e objetos de desenho, com 4 questões
- seção sobre tabelas, gráficos, planilhas e organogramas, com 5 questões
- seção sobre layout, esquema de cores, plano de fundo e slide mestre, com 5 questões
- seção sobre integração com Word e Excel (colar vinculado ou incorporado, objetos OLE) e 4 questões
- seção sobre edição de apresentações no LibreOffice Impress (menus, formatos ODP, diferenças em relação ao PowerPoint) e 6 questões

### 3. `matematica-financeira.taxas` — Taxas nominal, efetiva, proporcional, equivalente, real e aparente
Arquivo atual: `conteudo/materias/matematica-financeira/taxas.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre custo efetivo de operações de financiamento, empréstimo e investimento (taxa efetiva com tarifas e prazos, taxa interna do fluxo, custo real descontada a inflação), com 8 questões
- fluxo de caixa: diagrama de entradas e saídas, valor de cada fluxo no tempo e comparação de fluxos de vários pagamentos, com 6 questões
- taxa de retorno de investimentos (retorno simples e composto, taxa interna de retorno e taxa mínima de atratividade) e sua relação com a taxa efetiva, com 6 questões

### 4. `geografia.populacao-urbanizacao-brasil` — População e urbanização no Brasil
Arquivo atual: `conteudo/materias/geografia/populacao-urbanizacao-brasil.json` · 16 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a imigração para o Brasil (africanos, europeus, japoneses, árabes) e os fluxos recentes, com aspectos étnicos e culturais, e 6 questões
- capítulo sobre a dinâmica e a estrutura demográfica brasileira (transição demográfica no Brasil, pirâmide etária, envelhecimento, fecundidade e mortalidade com dados do IBGE) e 8 questões
- capítulo com o perfil demográfico brasileiro (população total e por região, composição por idade, sexo e cor ou raça, razão de dependência) e 8 questões
- seção sobre a estrutura urbana brasileira e as grandes metrópoles (São Paulo, Rio de Janeiro, metrópoles regionais, regiões metropolitanas) e 6 questões
- seção sobre a distribuição da população no território nacional (concentração litorânea, Sudeste, vazios demográficos, fronteiras de ocupação) e 6 questões
- seção sobre a evolução do crescimento populacional brasileiro no século XX (explosão demográfica, queda da mortalidade e da fecundidade, mudança da pirâmide etária) e 6 questões
- seção sobre desigualdades regionais e socioeconômicas da dinâmica demográfica (diferenciais de fecundidade, mortalidade, renda e migração entre regiões) e 6 questões
- capítulo sobre urbanismo e estrutura territorial brasileira (planejamento urbano, plano diretor, uso do solo, rede de cidades e regionalização) e 6 questões
- seção sobre cidades-mundiais e cidades globais (critérios, hierarquia mundial, posição de São Paulo e do Rio de Janeiro) ao lado de conurbação e metropolização, com 5 questões
- capítulo sobre a dinâmica intraurbana das metrópoles brasileiras (centro e periferia, verticalização, segregação, gentrificação, subcentros) e 7 questões
- seção sobre cidades médias e seu papel na modernização do território brasileiro (interiorização, agronegócio, centros regionais) e 6 questões
- seção sobre a divisão inter-regional do trabalho no Brasil e sua relação com a rede urbana, as metrópoles e as migrações internas, com 6 questões

### 5. `direito-administrativo.autarquias-agencias-reguladoras-executivas` — Autarquias, Agências reguladoras e executivas
Arquivo atual: `conteudo/materias/direito-administrativo/autarquias-agencias-reguladoras-executivas.json` · 19 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre a reforma gerencial do Estado (Plano Diretor de 1995, privatizações, Estado regulador) e o papel das agências reguladoras nesse contexto, com 4 questões
- seção sobre o regime de pessoal das agências reguladoras (quadro de servidores, cargos, dirigentes) e 3 questões, além do contrato de gestão com as agências
- seção sobre Estado regulador e defesa da concorrência (relação agência reguladora x CADE, competências concorrentes) e 4 questões
- seção sobre teorias da regulação (interesse público, captura, falhas de mercado) ligadas ao regime das agências, e 4 questões
- seção sobre a reforma do Estado (Plano Diretor de 1995, privatizações, Estado regulador) e a criação das entidades reguladoras federais, com 4 questões
- seção sobre abuso do poder regulatório (limites da regulação à livre iniciativa, liberdade econômica) e 3 questões sobre relatório de impacto regulatório e abuso regulatório
- seção sobre a ANATEL: competências, conselho diretor e regulação de telecomunicações, outorga e fiscalização de serviços, e 3 questões
- seção sobre a ANP: regulação de petróleo, gás natural e biocombustíveis, monopólio da União, contratos de concessão e partilha, e 3 questões
- seção sobre a ANVISA: vigilância sanitária, registro e fiscalização de produtos e serviços, sistema nacional de vigilância sanitária, e 3 questões
- seção sobre a ANS: regulação da saúde suplementar, planos privados de assistência, ressarcimento ao SUS e fiscalização das operadoras, e 3 questões
- seção sobre a ANA: recursos hídricos, outorga de direito de uso, Política Nacional de Recursos Hídricos e normas de referência de saneamento, e 3 questões
- seção sobre a ANTAQ: transporte aquaviário, portos, arrendamentos e regulação de infraestrutura portuária, e 3 questões

### 6. `orcamento-publico.conceitos` — Conceitos
Arquivo atual: `conteudo/materias/orcamento-publico/conceitos.json` · 16 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre estrutura programática (função, subfunção, programa, ação, projeto, atividade e operação especial) e classificação programática, com 6 questões
- capítulo sobre acompanhamento e fiscalização do orçamento (controle interno e externo, Tribunal de Contas, relatórios) e 6 questões
- capítulo sobre conteúdo, prazos e função da LDO (metas e prioridades, anexo de metas e riscos fiscais) e da LOA, com 8 questões
- capítulo sobre origem histórica, natureza jurídica e concepções tradicional, moderna e programática do orçamento, e espécies de orçamento, com 6 questões
- capítulo sobre bases constitucionais das finanças públicas (competência legislativa, normas gerais por lei complementar, repartição de receitas) e 6 questões
- capítulo sobre natureza jurídica do orçamento (lei formal, orçamento autorizativo x impositivo) e 5 questões
- capítulo sobre planejamento governamental (PPA) e controle do orçamento, com 6 questões
- capítulo sobre planos nacionais, regionais e setoriais e a evolução do planejamento brasileiro (Plano Trienal, PAEG, PND, PPA), com 6 questões
- capítulo sobre classificações orçamentárias (institucional, funcional, programática, por natureza) e sobre planos e programas, com 6 questões
- capítulo sobre emendas parlamentares impositivas individuais e de bancada (art. 166, §§ 9º a 12, da CF), limites percentuais e execução obrigatória, com 8 questões
- capítulo sobre espécies e natureza jurídica do orçamento e fiscalização financeira e orçamentária, com 6 questões
- capítulo sobre receitas e despesas extraorçamentárias (depósitos, cauções, restos a pagar) com 6 questões

### 7. `informatica.transformacao-digital` — Transformação digital, IoT, big data e inteligência artificial
Arquivo atual: `conteudo/materias/informatica/transformacao-digital.json` · 11 tópicos de edital pedem mais conteúdo

Falta:
- tecnologias emergentes aplicadas ao setor público: governo digital, plataformas como gov.br, interoperabilidade de dados, IA em serviços públicos, cidades inteligentes e riscos, com cerca de 6 questões
- bancos na era digital: bancos digitais e fintechs, Pix, open finance, canais digitais, tendências e desafios de segurança e concorrência, com cerca de 6 questões
- transformação digital no Sistema Financeiro Nacional: Pix, open finance, drex/moeda digital, bancos digitais, regulação do Banco Central e tendências, com cerca de 6 questões
- serviços públicos digitais (governo digital, gov.br, Lei do Governo Digital, acessibilidade, interoperabilidade) com cerca de 6 questões; as noções de IA já estão cobertas
- as revoluções industriais (da 1ª à 4ª) e a Indústria 4.0: fábrica inteligente, ciber-físicos, integração de tecnologias, impactos no trabalho, com cerca de 6 questões
- a Quarta Revolução Industrial: histórico das revoluções, Indústria 4.0, ciber-físicos, impactos econômicos e no transporte, com cerca de 6 questões
- estratégias de transformação digital: diagnóstico de maturidade, roadmap, governança, gestão da mudança, métricas e modelos de implantação, com cerca de 6 questões
- automação inteligente (RPA, sistemas autônomos), edge computing em profundidade e aplicações de IoT na aviação (sensores de aeronave, monitoramento, manutenção preditiva), com questões
- aspectos jurídicos da IoT: privacidade e proteção de dados (LGPD), responsabilidade por dispositivos, segurança por padrão e marco legal da IoT, além de questões de nível de delegado
- gestão de produtos digitais: product management, MVP, roadmap, backlog e métricas de produto, com cerca de 6 questões; a transformação digital geral já está coberta
- cultura digital: cultura organizacional orientada a dados, letramento digital, mentalidade ágil, gestão da mudança e liderança digital, com cerca de 6 questões

### 8. `processo-civil.acao` — Ação
Arquivo atual: `conteudo/materias/processo-civil/acao.json` · 20 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre aplicação das normas processuais e sobre preclusão, com 6 questões
- seção sobre preclusão (temporal, lógica e consumativa) com 5 questões
- capítulo introdutório de direito processual civil: conceito, fontes, princípios, aplicação das normas no tempo e no espaço, e 6 questões
- capítulo sobre princípios gerais do processo (devido processo legal, contraditório, ampla defesa, isonomia, juiz natural, publicidade, motivação, duração razoável) e 6 questões
- seção sobre substituição processual e legitimação extraordinária (hipóteses legais, regime, coisa julgada) e 5 questões, hoje excluída do escopo
- capítulo sobre os princípios constitucionais e infraconstitucionais do processo (juiz natural, inafastabilidade, duração razoável, isonomia, ampla defesa, motivação) e 8 questões
- capítulo sobre eficácia e aplicação das normas processuais no tempo e no espaço, interpretação, e prerrogativas da Fazenda Pública e da advocacia pública (prazos, reexame necessário, intimação pessoal), com 8 questões
- seção sobre cooperação jurídica internacional (auxílio direto, carta rogatória, homologação) e 5 questões
- seção sobre aplicação das normas processuais no tempo e no espaço e sobre preclusão, com 6 questões
- seção sobre aplicação das normas processuais no tempo e no espaço, com 5 questões
- capítulo sobre princípios informativos do processo e princípios processuais constitucionais, com 6 questões
- seção sobre instrumentalidade do processo (escopos jurídico, social e político) e 4 questões

### 9. `controle-externo.controle-interno-coso` — Controle interno, COSO, gestão de riscos e governança
Arquivo atual: `conteudo/materias/controle-externo/controle-interno-coso.json` · 18 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre compliance: conceito, programa de compliance (suporte da alta administração, código de conduta, controles internos, treinamento e comunicação, canal de denúncias, monitoramento) e 5 questões
- capítulo sobre a IN Conjunta MPOG/CGU nº 1/2016: objetivos, definições, estrutura dos controles internos da gestão, princípios da gestão de riscos, política de gestão de riscos, comitês de governança e responsabilidades, com 6 questões
- capítulo sobre controladoria governamental: conceito, evolução, funções (controle, orientação, transparência), forma de atuação e diferença para auditoria interna, com 5 questões
- capítulo sobre matriz de riscos e controles: construção (probabilidade x impacto, níveis de risco), mapeamento dos controles existentes, risco inerente x residual na matriz e exemplos de leitura, com 6 questões
- capítulo sobre implantação e avaliação de controles internos: desenho x operação, testes de controle, avaliação de eficácia, controles administrativos x operacionais e relatório de deficiências, com 6 questões
- capítulo sobre controle social, ética, participação e prestação de contas (accountability) na gestão pública, ligados a riscos e controles internos, com 5 questões
- capítulo sobre o IPPF/IIA: missão, princípios fundamentais, definição de auditoria interna, código de ética, normas e papel da auditoria interna na gestão de riscos, com 5 questões
- capítulo sobre noções de compliance: conceito, pilares do programa de compliance, código de conduta, canal de denúncias, treinamento e monitoramento, e lei anticorrupção em visão geral, com 5 questões
- capítulo sobre controladoria no setor público: conceito, classificação, objetivos, forma de atuação e relação com auditoria interna, com 5 questões
- capítulo sobre transparência e controle social como complemento do controle interno e externo (publicidade, acesso à informação, portais), com 4 questões
- capítulo sobre a metodologia de trabalho do Sistema de Controle Interno do Poder Executivo federal: técnicas (auditoria e fiscalização), planejamento, execução e relatórios, com 5 questões
- capítulo sobre implementação e melhoria de controles por planos de ação: priorização, responsáveis, prazos, acompanhamento e indicadores, com 4 questões

### 10. `legislacao.eca-direitos-fundamentais` — ECA: direitos fundamentais, medidas de proteção e Conselho Tutelar
Arquivo atual: `conteudo/materias/legislacao/eca-direitos-fundamentais.json` · 17 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre política de atendimento e entidades de atendimento (arts. 86 a 97), com 5 questões
- seções sobre o papel do Ministério Público e a proteção judicial de interesses individuais, difusos e coletivos (arts. 200 a 224), com 6 questões
- seções sobre direitos na primeira infância (Lei 13.257/2016), trabalho protegido e prevenção de ameaças, com 5 questões
- seção sobre antecedentes históricos (Código de Menores, doutrina da situação irregular) e inserção constitucional (art. 227), com 4 questões
- seção sobre escolha dos conselheiros, impedimentos e competência territorial do Conselho Tutelar, com 5 questões
- seção sobre cadastros de adoção (cadastro nacional de crianças e de pretendentes, SNA) e 4 questões
- capítulo sobre o Sistema de Garantia dos Direitos (eixos de promoção, defesa e controle, conselhos de direitos, fundos) e 5 questões
- seção sobre as Resoluções 113 e 117 do CONANDA (parâmetros do Sistema de Garantia) e 4 questões
- seção sobre a Lei 13.431/2017 e o Decreto 9.603/2018 (escuta especializada, depoimento especial), com 6 questões
- seção sobre o Marco Legal da Primeira Infância (Lei 13.257/2016), com 5 questões
- seção sobre o processo de escolha unificada dos conselheiros tutelares (Resolução 231/2022 do CONANDA), com 3 questões
- seção sobre aspectos gerais do direito da criança e do adolescente e a proteção da infância no Brasil (histórico e marcos), com 4 questões

### 11. `economia.macroeconomia-contas-nacionais` — Macroeconomia: contas nacionais e produto
Arquivo atual: `conteudo/materias/economia/macroeconomia-contas-nacionais.json` · 15 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre fluxo circular da renda (agentes, mercados de bens e fatores, vazamentos e injeções) e 6 questões
- capítulo aprofundado sobre balanço de pagamentos (estrutura atual BPM6: conta corrente, capital e financeira, erros e omissões, reservas, Brasil) e 8 questões
- capítulo sobre contas do sistema monetário (balanço do banco central e das autoridades monetárias, agregados) e 5 questões
- capítulo sobre política fiscal e seu efeito nos agregados e identidades macroeconômicas (gasto, tributo, multiplicador), com 6 questões
- capítulo sobre oferta e demanda por moeda (teoria quantitativa, preferência pela liquidez) e balanço de pagamentos aprofundado, com 8 questões
- capítulo sobre números-índice (Laspeyres, Paasche, Fisher) e deflatores e 6 questões
- capítulo sobre desemprego (taxa de desocupação, PEA, tipos) e inflação (medidas e IPCA), com 6 questões
- capítulo sobre equilíbrio da renda e multiplicador keynesiano (gasto autônomo, propensão marginal a consumir), com 8 questões
- capítulo sobre déficit público (nominal, primário, operacional), dívida pública e balanço de pagamentos, com 8 questões
- capítulo sobre agentes e setores econômicos, fluxo de renda e demanda agregada, com 6 questões
- capítulo sobre números-índice e nível de preços (IPC, IGP, Laspeyres, Paasche, deflator) e 6 questões
- capítulo sobre mercados de capitais, taxa de juros, câmbio e inflação ligados aos agregados, com 8 questões

### 12. `informatica.sistema-operacional-windows` — Sistema Operacional Windows 10
Arquivo atual: `conteudo/materias/informatica/sistema-operacional-windows.json` · 11 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre a área de transferência (copiar, recortar e colar, Ctrl+C/X/V, histórico da área de transferência Win+V, conteúdo temporário na memória) com 4 questões
- seção comparando a estrutura de diretórios do Windows e do Linux (unidades como C: x raiz /, separadores de caminho, atalhos x links simbólicos, diferença de maiúsculas e minúsculas) com 5 questões
- seção sobre a área de transferência, configurações de tela (resolução, cores, fontes) e impressoras (instalar, impressora padrão, fila de impressão), com 8 questões
- seção sobre backup no Windows (Histórico de Arquivos, backup e restauração, tipos completo/incremental/diferencial, cópia para disco externo ou nuvem) com 6 questões
- seção comparativa com o Linux (distribuições, diretórios, permissões, comandos básicos, software livre) e 6 questões
- seção sobre a área de transferência e comparação com o ambiente Linux (diretórios, atalhos, manipulação de arquivos), com 8 questões
- seção sobre aplicativos e acessórios do Windows 10 (Bloco de Notas, WordPad, Paint, Calculadora, Ferramenta de Recorte, Gravador de Passos, Microsoft Edge) com 6 questões
- seção sobre a área de transferência (copiar, recortar e colar, Ctrl+C/X/V, histórico da área de transferência Win+V, conteúdo temporário na memória) com 4 questões, mais questões sobre configurações do Windows 10

### 13. `direito.sistema-nacoes-unidas-direitos-humanos` — Sistema das Nações Unidas e os direitos humanos
Arquivo atual: `conteudo/materias/direito/sistema-nacoes-unidas-direitos-humanos.json` · 13 tópicos de edital pedem mais conteúdo

Falta:
- mecanismos nacionais de proteção dos direitos humanos (remédios constitucionais, Defensoria, Ministério Público, incidente de deslocamento de competência, programas de proteção) e sua relação com os internacionais, com 8 questões
- Conselho de Segurança (composição, veto e capítulo VII), Assembleia Geral, Secretariado e operações de paz, com 8 questões
- direito internacional dos direitos humanos além do sistema da ONU: princípios, jus cogens, sistemas regionais, relação com o direito interno e incorporação, com 10 questões
- organização da ONU em detalhe: Conselho de Segurança (composição, veto, capítulo VII), Assembleia Geral e votação, Secretariado, Conselho de Tutela, agências e financiamento, com 10 questões
- agências especializadas, programas e fundos da ONU (OIT, OMS, UNESCO, FAO, ACNUR, UNICEF, PNUD) com funções, com 8 questões
- sistemas africano (Carta de Banjul, Comissão e Corte Africana) e europeu (Convenção Europeia e Tribunal Europeu) e estrutura da OEA, com 10 questões
- quadro dos tratados de direitos humanos ratificados pelo Brasil (pactos, convenções específicas e protocolos), com datas e situação, com 8 questões
- lista e funções dos órgãos de tratados (Comitês de Direitos Humanos, DESC, contra a Tortura, CERD, CEDAW, da Criança e dos Direitos das Pessoas com Deficiência), formas de atuação e comunicações individuais, com 8 questões
- repercussão dos tratados de direitos humanos no direito brasileiro (incorporação, art. 5º, §§ 2º e 3º, status supralegal), com 6 questões
- ONU em nível de juiz federal: evolução histórica, órgãos internos, tipos de deliberações (resoluções, recomendações e decisões vinculantes), solução pacífica de controvérsias (capítulo VI) e sanções (capítulo VII), com 10 questões
- Declaração e Programa de Ação de Viena de 1993 (universalidade, indivisibilidade, interdependência, direito ao desenvolvimento) e sua relação com a DUDH, com 5 questões

### 14. `direito-administrativo.direito-agrario-terras-publicas` — Direito agrário e terras públicas
Arquivo atual: `conteudo/materias/direito-administrativo/direito-agrario-terras-publicas.json` · 19 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre política agrícola (art. 187 da CF: planejamento, crédito, preços, seguro, armazenamento) e aprofundamento da reforma agrária, com 8 questões em nível de juiz federal
- capítulo sobre a evolução histórica do regime das terras públicas no Brasil (sesmarias, Lei de Terras de 1850, Constituição de 1891, devolução aos Estados) e 5 questões
- capítulo sobre política agrícola (art. 187 da CF, instrumentos, planejamento e Lei da política agrícola) e 5 questões
- capítulo sobre teoria geral do direito agrário: autonomia, fontes, princípios (função social, justiça social, proteção do trabalhador rural) e conceitos de imóvel rural, empresa rural e propriedade familiar, com 6 questões
- capítulo sobre registro do imóvel rural (matrícula, georreferenciamento, CCIR, CAR, cadastro ambiental e relação com a função social) e 6 questões
- capítulo sobre os arts. 184 a 191 da CF e a Lei 8.629 (imóvel produtivo, grau de utilização e eficiência, insuscetibilidade de desapropriação, indenização em TDA) e 8 questões
- capítulo sobre o rito da desapropriação por interesse social para reforma agrária (LC 76: petição inicial, imissão na posse, contestação restrita, perícia, sentença) e 8 questões
- capítulo sobre regularização fundiária rural em terras da União (requisitos do ocupante, limites de área, alienação e concessão, prazos e cláusulas resolutivas) e 6 questões
- capítulo sobre terras indígenas (art. 231 da CF, posse permanente, usufruto, demarcação) e terras quilombolas (art. 68 do ADCT, titulação) e 8 questões
- capítulo sobre o Estatuto da Terra (módulo rural, minifúndio e latifúndio, propriedade familiar, empresa rural, colonização e contratos agrários) e 8 questões
- capítulo sobre o regime constitucional das terras indígenas (art. 231, bens da União, usufruto exclusivo, demarcação, marco temporal) e 6 questões
- capítulo sobre a ação discriminatória (Lei 6.383/1976, rito judicial, legitimidade, efeitos sobre terras devolutas) e 6 questões

### 15. `portugues.pontuacao` — Pontuação
Arquivo atual: `conteudo/materias/portugues/pontuacao.json` · 12 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre a relação entre pontuação, coesão e coerência textual (conectivos, retomadas) e 5 questões
- seção sobre norma culta e sinais gráficos além da pontuação (hífen, apóstrofo, til, trema), com 5 questões
- seções sobre ordem direta e inversa e tipos de discurso (direto, indireto, indireto livre) com pontuação própria, e 6 questões
- seção sobre sintaxe e sinais gráficos ligados à pontuação, com 5 questões
- seção sobre organização sintática e sinais gráficos, com 5 questões
- seção sobre classes de palavras e seus empregos em relação à pontuação, com 5 questões
- seção de acentuação gráfica (regras de acentuação) com 8 questões
- seção sobre uso dos porquês (por que, porque, por quê, porquê) e 5 questões
- seção sobre organização sintática das frases (termos, ordem direta e inversa) e 5 questões
- seção sobre organização sintática de termos e orações em ordem direta e inversa, com 6 questões
- seção sobre normas ortográficas vigentes e pontuação na escrita oficial e pública, com 6 questões
- seção sobre pontuação expressiva não convencional em textos instantâneos (exclamações repetidas, caixa alta, reticências) e 4 questões

### 16. `direito.declaracao-universal-direitos-humanos` — Declaração Universal dos Direitos Humanos
Arquivo atual: `conteudo/materias/direito/declaracao-universal-direitos-humanos.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre os Princípios de Yogyakarta +10 (orientação sexual e identidade de gênero) e sua relação com a DUDH, com 5 questões
- seção sobre a Agenda 2030 da ONU e os Objetivos de Desenvolvimento Sustentável em relação à DUDH, com 5 questões
- capítulo sobre os principais instrumentos internacionais de direitos humanos (pactos de 1966, convenções temáticas), com 8 questões
- capítulo sobre fontes dos direitos humanos e instrumentos internacionais, com 8 questões
- seção sobre o Código de Conduta para Funcionários Responsáveis pela Aplicação da Lei, com 5 questões
- seção sobre o valor da DUDH no direito interno brasileiro (natureza de declaração, art. 5º, §§ 2º e 3º da Constituição, tratados) e 5 questões
- capítulo sobre a Convenção Americana sobre Direitos Humanos, com 8 questões

### 17. `seguranca-publica.uso-forca-direitos-humanos` — Uso da força e direitos humanos
Arquivo atual: `conteudo/materias/seguranca-publica/uso-forca-direitos-humanos.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- os Princípios Básicos de Havana (1990) apresentados com seus itens numerados (disposições gerais, uso de armas de fogo, policiamento de reuniões, custódia, qualificação e relatórios) e cerca de 8 questões; hoje o documento nem é citado
- o Decreto 12.341/2024 (regulamento da Lei 13.060: definições, níveis de força, instrumentos, protocolos, capacitação, comissão e prestação de contas) com cerca de 8 questões; ele não é citado na matéria
- o Decreto 12.341/2024 e suas alterações (definições, uso diferenciado, regras para armas de fogo e instrumentos de menor potencial ofensivo, treinamento e registro) com cerca de 8 questões
- o Código de Conduta para Funcionários Responsáveis pela Aplicação da Lei (Resolução ONU 34/169, 1979): oito artigos e comentários (dignidade, uso da força, segredo, tortura, saúde, corrupção), com cerca de 6 questões
- os Princípios Básicos de Havana (1990) apresentados com seus itens numerados (disposições gerais, uso de armas de fogo, policiamento de reuniões, custódia, qualificação e relatórios) e cerca de 8 questões
- o Código de Conduta para Funcionários Responsáveis pela Aplicação da Lei (Resolução ONU 34/169, 1979): artigos e comentários, com cerca de 6 questões
- as regras da ONU sobre uso da força e de armas de fogo (Princípios de Havana e Código de Conduta 34/169) com seus itens e cerca de 8 questões
- os Princípios Básicos de Havana (1990) item a item e o Decreto 12.341/2024 que regulamenta a Lei 13.060, com cerca de 10 questões; a Lei 13.060 já está coberta
- uso da força por agentes estatais em nível de magistratura: parâmetros do sistema interamericano e da jurisprudência do STF (ADPF 635, responsabilidade do Estado por mortes em operações), com questões

### 18. `direito-consumidor.praticas-defesa` — Práticas comerciais, proteção contratual e defesa do consumidor em juízo
Arquivo atual: `conteudo/materias/direito-consumidor/praticas-defesa.json` · 16 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre as ações de responsabilidade do fornecedor (CDC, arts. 101 e 102): foro do domicílio do autor, chamamento ao processo do segurador, vedação da denunciação da lide, e 5 questões.
- seções sobre superendividamento (Lei 14.181/2021), serviços regulados e telecomunicações nas relações de consumo e jurisprudência dominante do STJ, com 10 questões.
- seção sobre cadastro positivo (Lei 12.414/2011): natureza, consentimento, direitos do cadastrado, relação com o art. 43 do CDC, e 4 questões.
- seção sobre a ação de responsabilização do fornecedor (arts. 101 e 102 do CDC): foro do autor, chamamento ao processo do segurador, vedação da denunciação da lide, ação regressiva, e 5 questões.
- seção sobre a organização do SNDC (Decreto 2.181/1997): Senacon, Procons estaduais e municipais, entidades civis, conflito de atribuições e sistemas de informação, e 4 questões.
- seção sobre a tutela específica nas obrigações de fazer e não fazer (art. 84 do CDC): obrigação específica, multa diária, medidas de apoio, conversão em perdas e danos, e 5 questões.
- seção sobre comércio eletrônico (Decreto 7.962/2013): informações claras na oferta, atendimento facilitado, direito de arrependimento e responsabilidade do intermediário, com 5 questões.
- seção sobre a ação revisional de contratos de consumo: modificação de cláusulas desproporcionais e revisão por onerosidade excessiva (art. 6º, V), juros e encargos em contratos bancários, com 5 questões.
- capítulo sobre superendividamento (Lei 14.181/2021): conceito, mínimo existencial, deveres de informação na concessão de crédito, repactuação e plano de pagamento, e 8 questões.
- seção sobre conciliação no superendividamento: audiência global, plano de pagamento, prazos máximos e processo por superendividamento (arts. 104-A e 104-B do CDC), com 5 questões.
- seção sobre liquidação e cumprimento da sentença coletiva (CDC, arts. 97 a 100): liquidação por vítimas e sucessores, habilitação dos interessados, indenização fluida e execução coletiva, com 5 questões.
- seção sobre as ações de responsabilidade do fornecedor (arts. 101 e 102) e sobre a tutela específica das obrigações de fazer e não fazer (art. 84), com 8 questões.
