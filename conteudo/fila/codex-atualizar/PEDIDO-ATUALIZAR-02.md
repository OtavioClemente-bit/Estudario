# Atualizar matérias — pedido 02 de 18 (09/10/2026)

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

### 1. `educacao.ldb-bncc-didatica` — LDB, BNCC e didática
Arquivo atual: `conteudo/materias/educacao/ldb-bncc-didatica.json` · 26 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre as correntes do pensamento pedagógico brasileiro (jesuítica, tradicional, Escola Nova, tecnicismo, pedagogia libertadora de Paulo Freire, histórico-crítica de Saviani, crítico-social dos conteúdos) e seus reflexos na organização do sistema de ensino, com 8 questões.
- Capítulo sobre a sala de aula como espaço de aprendizagem: mediação docente, interação, clima, organização do tempo e do espaço, indisciplina e gestão da sala, com 5 questões.
- Capítulo sobre teorias do currículo (tradicionais, críticas e pós-críticas; Tyler, Apple, Giroux, Silva) e currículo oculto, com 6 questões.
- Seção sobre as Diretrizes Curriculares Nacionais para a formação inicial e continuada de professores (Resoluções CNE/CP 2/2019 e 4/2024), competências docentes e prática como componente curricular, com 5 questões.
- Capítulo sobre a organização da educação brasileira: arts. 205 a 214 da CF, sistemas de ensino e regime de colaboração, Plano Nacional de Educação, Fundeb e conselhos, com 8 questões; hoje o tema é tratado em poucas linhas.
- Seção sobre interdisciplinaridade e contextualização no Ensino Médio (DCNEM, BNCC do Ensino Médio, áreas do conhecimento, itinerários) com 5 questões.
- Seção sobre as Diretrizes Curriculares Nacionais do Ensino Fundamental de 9 anos (Resolução CNE/CEB 7/2010): princípios, organização em ciclos, base nacional comum e parte diversificada, avaliação, com 6 questões.
- Seção sobre as Diretrizes Curriculares Nacionais do Ensino Médio (Resolução CNE/CEB 3/2018 e a nova redação da Lei 14.945/2024): formação geral básica e itinerários, com 6 questões.
- Seção sobre as diretrizes operacionais da EJA (Resolução CNE/CEB 1/2021): alinhamento à PNA e à BNCC, EJA a distância, carga horária e certificação, com 5 questões.
- Seção sobre as DCN gerais da educação profissional e tecnológica de nível médio (Resolução CNE/CP 1/2021): princípios, cursos técnicos, itinerário da formação técnica e profissional, com 5 questões.
- Seção sobre as dimensões do currículo (prescrito, apresentado, moldado, em ação, avaliado, oculto) e a passagem do currículo proposto à prática, com 5 questões.
- Seção sobre conteúdos do ensino (conceituais, procedimentais e atitudinais; seleção, sequência e organização) com 4 questões.

### 2. `direito-civil.direitos-reais` — Direitos reais
Arquivo atual: `conteudo/materias/direito-civil/direitos-reais.json` · 50 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o direito real do promitente comprador (arts. 1.417 e 1.418: requisitos, registro, adjudicação compulsória) e 5 questões
- função social e socioambiental da posse (teorias, conceito, concretização, posse-trabalho, art. 1.228 §4º) com 4 questões
- propriedade fiduciária e alienação fiduciária de móveis e imóveis (DL 911/1969, Lei 9.514/1997: constituição, execução extrajudicial, consolidação da propriedade, leilão) com 6 questões
- direitos de vizinhança (uso anormal da propriedade, árvores limítrofes, passagem forçada, águas, limites entre prédios, direito de construir, arts. 1.277 a 1.313) e 6 questões
- compromisso de venda e compra e adjudicação compulsória (requisitos, registro, via extrajudicial, Súmula do STJ sobre registro) com 5 questões
- condomínio geral voluntário e necessário (arts. 1.314 a 1.330: direitos e deveres dos condôminos, divisão, administração) e 5 questões
- propriedade resolúvel (condição ou termo, efeitos ex tunc e ex nunc, arts. 1.359 e 1.360) e propriedade fiduciária (arts. 1.361 a 1.368-B) com 6 questões
- teorias da posse (subjetiva de Savigny, objetiva de Ihering, sociológica) e a adotada pelo CC; 3 questões
- noções de condomínio edilício (unidades autônomas e partes comuns, convenção, assembleia, síndico) e 4 questões
- concessão de uso especial para fins de moradia (MP 2.220/2001 e art. 183 CF: requisitos, bens públicos) e 3 questões
- aquisição da posse (arts. 1.204 a 1.209: apreensão, disposição da coisa, exercício do direito; aquisição por terceiro) e 4 questões
- direitos de vizinhança (arts. 1.277 a 1.313) e 4 questões

### 3. `controle-externo.tribunais-contas` — Controle externo, tribunais de contas e tomada de contas especial
Arquivo atual: `conteudo/materias/controle-externo/tribunais-contas.json` · 80 tópicos de edital pedem mais conteúdo

Falta:
- natureza jurídica (órgão autônomo, não judiciário), funções (fiscalizadora, consultiva, sancionadora, corretiva, normativa, informativa) e eficácia das decisões (título executivo, art. 71, § 3º), com 5 questões
- capítulo sobre a prestação de contas na administração pública federal (tipos de processo, contas de gestão, normativos do TCU) e 4 questões
- seção sobre organização, composição e jurisdição do TCU e dos tribunais estaduais e do DF (ministros e conselheiros, número, escolha) e 4 questões
- capítulo sobre Ministério Público de Contas (natureza, independência, composição, atribuições, STF) e 5 questões
- seção sobre a natureza jurídica dos tribunais de contas (órgão auxiliar, autonomia, não integrante do Judiciário, Súmula 347) e 4 questões
- seção sobre a Súmula 347 do STF e o controle incidental de constitucionalidade pelos tribunais de contas, com 4 questões
- seção detalhando poder geral de cautela, dever de representação, apreciação de denúncias e legalidade de atos de pessoal, e 6 questões
- seção com precedentes do STF sobre competências dos tribunais de contas (Súmula 347, cautelares, atos de pessoal, contas do chefe do Executivo) e 5 questões
- seção sobre fiscalização da arrecadação e da renúncia de receitas tributárias pelos tribunais de contas e 4 questões
- seção distinguindo contas de governo (parecer prévio) e contas de gestão (julgamento) com esses nomes e exemplos, e 4 questões
- seção sobre composição, jurisdição e organização do TCU e dos tribunais estaduais, com 4 questões; organização do TCE-PA é norma local
- seção sobre natureza, competências do art. 71, jurisdição própria e composição do TCU e 5 questões

### 4. `direito-administrativo.contratos-administrativos` — Contratos administrativos na Lei 14.133/2021
Arquivo atual: `conteudo/materias/direito-administrativo/contratos-administrativos.json` · 60 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre elaboração do contrato (cláusulas necessárias do art. 92, minuta, termo de referência e planejamento) e sobre gestão e fiscalização em nível de cargo técnico, com 6 questões
- capítulo sobre cláusulas de nível de serviço: indicadores, instrumento de medição de resultado, ajuste de pagamento por desempenho e sanções, com exemplos e 6 questões
- capítulo sobre espécies de contratos administrativos (obras, serviços, fornecimento, concessão, gestão) e sua classificação, com 5 questões
- capítulo com a doutrina clássica do contrato administrativo (natureza, classificação, mutabilidade, teorias do fato do príncipe, fato da administração e imprevisão) e jurisprudência, com 6 questões
- capítulo sobre planejamento da contratação que antecede e orienta a fiscalização (estudo técnico, termo de referência, plano de fiscalização) e 5 questões
- parte sobre espécies de contratos de compras (fornecimento contínuo, entrega parcelada, registro de preços) e 4 questões
- capítulo sobre as espécies de contratos administrativos (obra, serviço, fornecimento, concessão, gestão associada) e questões sobre classificação
- capítulo sobre classificação e modalidades de contratos administrativos (obras, serviços, compras, alienações) e 4 questões
- capítulo sobre tipos de contratos administrativos e classificação doutrinária, com 4 questões
- capítulo sobre nulidade do contrato (suspensão, declaração de nulidade, modulação de efeitos, indenização) e diferença entre anulação e revogação, com 6 questões
- capítulo sobre contratos de obras: regimes de execução, requisitos de projeto básico e executivo, medição, recebimento provisório e definitivo e 6 questões
- capítulo sobre o regime legal de obras e serviços de engenharia na Lei 14.133 (regimes de execução, projeto, BIM, matriz de riscos) e 5 questões

### 5. `administracao-publica.modelos-governanca` — Modelos de administração pública e governança
Arquivo atual: `conteudo/materias/administracao-publica/modelos-governanca.json` · 58 tópicos de edital pedem mais conteúdo

Falta:
- capítulo comparando gestão pública e gestão privada: finalidade (interesse público x lucro), legalidade estrita x autonomia, controle e accountability, ambiente político, e o que a gestão pública pode importar da privada; 4 questões
- seção sobre empreendedorismo governamental (Osborne e Gaebler: governo catalisador, orientado a missão e resultados, competitivo, descentralizado) e novas lideranças no setor público (liderança transformacional, servidora, adaptativa); 4 questões
- capítulo sobre processos participativos: conselhos de políticas públicas, orçamento participativo, audiências e consultas públicas, ouvidorias, parcerias governo-sociedade e controle social; hoje só há menções soltas; 5 questões
- seção sobre intermediação de interesses: clientelismo, corporativismo e neocorporativismo (Schmitter), pluralismo x corporativismo e exemplos brasileiros; hoje só o clientelismo é citado; 4 questões
- capítulo sobre a evolução dos tipos de Estado e de administração: Estado oligárquico e patrimonial, autoritário-burocrático, Estado de bem-estar social e Estado regulador, ligados ao paradigma pós-burocrático; hoje bem-estar e regulador só aparecem de passagem; 6 questões
- capítulo sobre valor público (Mark Moore): conceito, triângulo estratégico (legitimidade, capacidade operacional, valor público), cadeia de valor público (insumos, processos, produtos, resultados, impactos) e avaliação de impacto; hoje o termo não aparece; 5 questões
- detalhar o Decreto 9.203/2017 além de princípios e mecanismos: definições do art. 2º, diretrizes do art. 4º, Comitê Interministerial de Governança, comitês internos, gestão de riscos e programa de integridade, e as alterações do Decreto 9.901/2019; 6 questões
- seção de gestão pública contemporânea ligando participação social, transparência e planejamento governamental (PPA, LDO e LOA em noções: função e horizonte) a indicadores de desempenho; hoje PPA/LDO/LOA ficam fora do escopo; 5 questões
- seção ligando governança e gestão ao planejamento estratégico (missão, visão, objetivos, indicadores, alinhamento estratégico em noções); 4 questões
- capítulo sobre Nova Governança Pública (New Public Governance): contexto pós-NPM, pluralismo de atores, redes e coprodução, quadro comparativo administração tradicional x NPM x NPG; 5 questões
- seção sobre governança em redes: cooperação e articulação interinstitucional, parcerias com o setor privado e com o terceiro setor (organizações sociais, termos de fomento e colaboração em noções); 4 questões
- seção sobre a reforma do serviço civil (profissionalização, carreiras típicas de Estado, concurso, avaliação de desempenho, flexibilização da estabilidade) e o papel do MARE; 3 questões

### 6. `informatica.redes-computadores` — Redes de computadores
Arquivo atual: `conteudo/materias/informatica/redes-computadores.json` · 78 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre Ethernet, Fibre Channel, iSCSI e Wi-Fi IEEE 802.11x, com 8 questões
- capítulo sobre comutação de circuitos, pacotes e células, com 5 questões
- capítulo sobre meios de transmissão guiados e não guiados (par trançado, coaxial, fibra óptica, rádio) e classificação de redes, com 5 questões
- capítulo sobre transmissão de dados: tipos de enlace (ponto a ponto, multiponto), modos simplex, half e full duplex, códigos e meios, com 6 questões
- capítulo de fundamentos de comunicação de dados (sinal analógico e digital, banda, multiplexação, modos de transmissão), com 6 questões
- seção sobre estações e servidores (tipos de servidor, papéis e serviços) e 3 questões
- capítulo sobre tecnologias de LAN e WAN (Ethernet, enlaces dedicados, frame relay, MPLS), com 5 questões
- seção sobre arquitetura cliente-servidor e redes ponto a ponto, comparando papéis, com 4 questões
- capítulo sobre tipos de meios de transmissão (cabos metálicos, fibra, rádio) e suas características, com 5 questões
- seção sobre PAN, WPAN, WMAN e WWAN, além de LAN, MAN e WAN, com 4 questões
- seção sobre gateways, hubs e bridges e diferenças em relação a switches e roteadores, com 4 questões
- capítulo de fundamentos de comunicação de dados (sinais, modulação, multiplexação, taxa de transmissão), com 6 questões

### 7. `contabilidade.conceitos-patrimonio-contas` — Conceitos, patrimônio e contas
Arquivo atual: `conteudo/materias/contabilidade/conceitos-patrimonio-contas.json` · 48 tópicos de edital pedem mais conteúdo

Falta:
- subtópico de representação gráfica do patrimônio (balanço esquemático em T, ativo à esquerda e passivo/PL à direita) e situação líquida positiva, nula e negativa; 4 questões
- distinguir ato administrativo (não altera o patrimônio) de fato administrativo/contábil, com exemplos de contratação e assinatura de contrato; 5 questões de classificação
- capítulo curto sobre atos administrativos x fatos administrativos (atos não alteram o patrimônio, fatos sim) e 5 questões de identificação
- campo de atuação (contabilidade geral, de custos, pública, gerencial), usuários internos e externos da informação contábil e finalidade; 5 questões
- distinção entre receita e ganho, despesa e perda (atividade ordinária x eventual) com exemplos como venda de imobilizado; 5 questões
- campo de atuação da contabilidade, usuários internos e externos e classificação do objeto; 5 questões de conceito
- separar receitas e despesas de ganhos e perdas dentro da variação do PL, com a regra de reconhecimento de cada um; 4 questões
- apuração do resultado do exercício: encerramento de contas de receita e despesa, ARE, transferência do resultado ao PL e fechamento do balanço; 6 questões numéricas
- técnicas contábeis: escrituração, demonstrações contábeis, auditoria e análise de balanços, com a função de cada uma; 5 questões
- campo de aplicação da contabilidade (entidades com e sem fins lucrativos, setor público, ramos aplicados); 4 questões
- aspectos qualitativo (natureza dos elementos) e quantitativo (valor monetário) do patrimônio, com exemplos; 4 questões
- aspectos qualitativo e quantitativo e representação gráfica do patrimônio (balanço esquemático); 5 questões

### 8. `contabilidade.custos` — Contabilidade de custos
Arquivo atual: `conteudo/materias/contabilidade/custos.json` · 78 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre sistemas de custos e informação gerencial (usuários, finalidades, sistemas por ordem e por processo) e 5 questões
- seção de terminologia (gasto, investimento, custo, despesa, perda, desperdício) e 4 questões; hoje só há classificação básica
- seção de terminologia da contabilidade de custos (gasto, investimento, custo, despesa, perda) e objeto de custeio, com 4 questões
- capítulo sobre apropriação dos custos à produção: custos diretos, indiretos e critérios de rateio, com 5 questões
- capítulo sobre custeio baseado em atividades (ABC): atividades, direcionadores de custo, comparação com absorção, e 6 questões
- capítulo sobre apuração do custo da produção acabada, produtos em elaboração e produtos vendidos (CPA, CPP, CPV) e 6 questões
- capítulo sobre produção por ordem, contínua (processo) e conjunta, com acumulação de custos e 6 questões
- seção sobre formas de controle de custos (orçamento, custo padrão, custos controláveis) e 4 questões
- seção sobre custos controláveis e não controláveis e responsabilidade por centros de custos, com 4 questões
- seção sobre critérios de atribuição de custos (direto, rateio, bases de alocação) e apropriação à produção, com 5 questões
- capítulo sobre tipos de produção (por ordem, contínua, conjunta) com tratamento contábil e apropriação, e 6 questões
- capítulo sobre departamentalização: centros de produção e de serviços, rateio recíproco e direto, com 6 questões

### 9. `processo-civil.recursos` — Recursos no processo civil
Arquivo atual: `conteudo/materias/processo-civil/recursos.json` · 63 tópicos de edital pedem mais conteúdo

Falta:
- Detalhar recurso ordinário, embargos de divergência e agravo em REsp/RE, hoje não detalhados, com 5 questões.
- Reclamação (arts. 988 a 993): hipóteses, competência e procedimento, com 6 questões.
- Capítulo sobre IRDR (arts. 976 a 987): pressupostos, legitimidade, suspensão, julgamento e efeitos, com 6 questões.
- Embargos de divergência (arts. 1.043 e 1.044): cabimento, prazos, paradigma e comprovação da divergência, com 5 questões.
- Recurso ordinário constitucional (art. 1.027): cabimento, prazo e procedimento, com 5 questões.
- Capítulo sobre IAC (art. 947): requisitos, competência e efeito vinculante, com 4 questões.
- Recurso ordinário constitucional, hoje não detalhado, para STF e STJ, com 5 questões.
- Remessa necessária (art. 496): hipóteses, dispensas e efeitos; hoje só citada de passagem, com 6 questões.
- Procedimento do julgamento de REsp/RE repetitivos (arts. 1.036 a 1.041): afetação, suspensão, efeitos e retratação, com 6 questões.
- Duplo grau de jurisdição: conceito, fundamentos, exceções e relação com a apelação, com 4 questões.
- Recurso ordinário, agravo em REsp/RE e embargos de divergência, com 6 questões.
- Reclamação (arts. 988 a 993) e correição parcial, com 6 questões.

### 10. `direito-tributario.impostos-especie` — Impostos por espécie e reforma tributária do consumo
Arquivo atual: `conteudo/materias/direito-tributario/impostos-especie.json` · 66 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a LC 116/2003 (fato gerador do ISS, lista de serviços, local da prestação, contribuinte e responsável, base de cálculo, alíquotas mínima e máxima, conflito com ICMS e alterações da LC 157/2016 e 175/2020) e 8 questões; a lei não é citada hoje
- capítulo sobre a Emenda Constitucional 132/2023 e a Lei Complementar 214/2025 (princípios do art. 156-A, IBS/CBS/IS, transição 2026-2033, dispositivos com vigência escalonada) e 8 questões; hoje não há citação da EC nem da LC
- seção sobre as demais espécies de competência municipal (taxas, contribuição de melhoria e COSIP) ao lado dos impostos, com 4 questões
- capítulo aprofundado sobre o IBS: fato gerador, sujeitos passivos, base de cálculo, não cumulatividade e créditos, regimes diferenciados e transição, gestão pelo Comitê Gestor, com 8 questões (hoje só há noção de competência compartilhada)
- capítulo aprofundado sobre a CBS: fato gerador, contribuinte, base, não cumulatividade e créditos, regimes específicos e diferença para o IBS, com 8 questões
- seção sobre impostos de comércio exterior: imposto de importação e de exportação, fato gerador, base de cálculo, alíquotas extrafiscais e função regulatória (art. 153, §1º CF), com 6 questões
- seção sobre o Comitê Gestor do IBS (composição, competências, arrecadação e repartição, art. 156-B e LC 214/2025) e 4 questões
- capítulo sobre IRPJ: lucro real, presumido e arbitrado, adições e exclusões, despesas dedutíveis e indedutíveis, preço de transferência e lucros no exterior, com 8 questões
- capítulo sobre o regime jurídico do imposto de renda (PF e PJ, regimes de caixa e competência, retenção na fonte, tributação exclusiva e progressividade) e 6 questões
- capítulo sobre IRPF: fato gerador, base de cálculo, deduções, tributação exclusiva e definitiva, carnê-leão e ajuste anual, com 8 questões
- capítulo sobre o novo sistema de tributação do consumo (IBS, CBS e Imposto Seletivo): incidência, não cumulatividade, créditos, alíquotas de referência e regimes específicos, com 8 questões
- seção sobre o ITR: fato gerador, base (VTN), contribuinte, alíquotas progressivas, imunidade da pequena gleba, fiscalização e arrecadação por municípios conveniados, com 6 questões

### 11. `direito-civil.obrigacoes` — Direito das obrigações
Arquivo atual: `conteudo/materias/direito-civil/obrigacoes.json` · 44 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre preferências e privilégios creditórios (arts. 955 a 965 do CC): privilégio especial e geral, ordem de pagamento, concurso de credores, e 4 questões
- seção sobre obrigações líquidas e ilíquidas (liquidação e reflexos na mora e na compensação) e 3 questões
- seção sobre obrigações principais e acessórias (princípio da gravitação) e 3 questões
- seção sobre obrigações de execução instantânea, diferida e continuada e seus efeitos sobre mora e resolução, e 3 questões
- seção sobre obrigações puras, condicionais, a termo e modais (encargo), distinguindo condição, termo e modo, e 4 questões
- capítulo sobre atos unilaterais (promessa de recompensa, gestão de negócios, pagamento indevido, enriquecimento sem causa) e 8 questões
- seção sobre obrigação natural (dívida de jogo, dívida prescrita, art. 882), obrigações de meio, de resultado e de garantia, e 5 questões
- capítulo sobre atos unilaterais (promessa de recompensa, gestão de negócios, pagamento indevido, enriquecimento sem causa) e 6 questões
- seção sobre enriquecimento sem causa (arts. 884 a 886): requisitos, ação de in rem verso e caráter subsidiário, e 4 questões
- seção sobre obrigação natural, obrigações de meio, de resultado e de garantia, e 5 questões
- seção sobre gestão de negócios (arts. 861 a 875): deveres do gestor, ratificação e indenização, e 3 questões
- seção sobre pagamento indevido (arts. 876 a 883): repetição do indébito, ônus da prova e exceções, e 4 questões

### 12. `contabilidade.dfc-dva-analise` — DFC, DVA e análise de demonstrações
Arquivo atual: `conteudo/materias/contabilidade/dfc-dva-analise.json` · 71 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre análise vertical (percentuais sobre ativo total ou receita líquida) e horizontal (números-índice e variação), com exemplos resolvidos e 6 questões
- capítulo introdutório de análise de demonstrações: objetivos, usuários, padronização/reclassificação de contas, roteiro de análise e 5 questões, além de análise vertical/horizontal e prazos médios
- seção sobre lucratividade: margens bruta, operacional e líquida, análise vertical da formação do resultado (receita, CPV/CSP, despesas) e 5 questões
- seção sobre indicadores de estrutura de capital: composição do endividamento (curto x longo prazo), participação de capitais de terceiros, imobilização do patrimônio líquido e 5 questões
- capítulo de conceitos básicos de análise de balanços (objetivos, usuários, limitações, padronização das demonstrações) e 4 questões
- seção sobre notas explicativas e informações extraídas delas na análise das demonstrações (políticas contábeis, partes relacionadas, contingências, eventos subsequentes) e 4 questões
- seção sobre conceito, cálculo, vantagens e desvantagens de cada grupo de indicadores e 4 questões
- seção sobre solvência: solvência geral (ativo total sobre passivo exigível), garantia de capital de terceiros, imobilização e 5 questões
- seção sobre prazos médios (estocagem, recebimento, pagamento), rotação, ciclos operacional e financeiro e 6 questões
- seção sobre limitações da análise por indicadores (sazonalidade, políticas contábeis, comparabilidade, dados qualitativos não financeiros) e 4 questões
- seção sobre o modelo DuPont: ROE = margem líquida x giro do ativo x multiplicador de alavancagem, e decomposição da margem operacional, com 5 questões
- seção sobre retorno sobre o capital empregado/investido (ROCE, ROIC), decomposição ROA e ROE, grau de alavancagem financeira e 5 questões

### 13. `informatica.analise-dados-ia` — Análise de dados e inteligência artificial
Arquivo atual: `conteudo/materias/informatica/analise-dados-ia.json` · 79 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre algoritmos não supervisionados e de anomalias (K-means, hierárquico, DBSCAN, mistura de gaussianas, PCA/ICA, regras de associação com suporte, confiança e lift, Isolation Forest) e cerca de 10 questões específicas
- capítulo sobre os algoritmos supervisionados citados (regressão linear e logística, árvores de decisão, random forest e boosting, k-NN, SVM, Naive Bayes, redes feed-forward): como funcionam, quando usar, vantagens e limites, e cerca de 10 questões específicas
- capítulo sobre o modelo CRISP-DM (as seis fases e o que se faz em cada) e questões sobre ele
- capítulo sobre mineração de texto (tokenização, stopwords, TF-IDF, classificação e agrupamento de texto) e questões
- capítulo sobre métricas: matriz de confusão, acurácia, precisão, revocação, F1, curva ROC e AUC, MAE, MSE, RMSE e R2, com questões de cálculo e interpretação
- trecho sobre aprendizado por reforço e semissupervisionado (agente, recompensa, exemplos) e questões
- conteúdo sobre regularização Ridge e Lasso, trade-off entre viés e variância, métodos ensemble, separabilidade e seleção de variáveis, com questões
- trecho sobre sistemas de recomendação (e, no 88, PLN, visão computacional e deep learning) e questões
- conteúdo sobre people analytics (uso de dados na gestão de pessoas) e questões
- capítulo sobre técnicas de pré-processamento (normalização, padronização, discretização, imputação de ausentes, outliers, codificação de categóricos, agregações) e questões
- trecho sobre papéis em projetos de ciência de dados e big data (cientista, engenheiro e analista de dados) e questões
- conteúdo de estatística aplicada e descritiva (medidas, distribuições, correlação) e questões, que a matéria não traz

### 14. `informatica.seguranca-normas-iso27001` — Gestão de segurança da informação: ISO/IEC 27001 e 27002
Arquivo atual: `conteudo/materias/informatica/seguranca-normas-iso27001.json` · 67 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre processo de gestão de riscos de segurança da informação (contexto, identificação, análise, avaliação, tratamento, aceitação, monitoramento) alinhado à ISO 27005, com matriz de risco, e 8 questões
- seção prática de condução de análise de riscos (ativos, ameaças, vulnerabilidades, probabilidade e impacto, método qualitativo e quantitativo) com exemplos resolvidos e 8 questões
- seção sobre privacidade e proteção de dados no SGSI (ISO/IEC 27701, controle de privacidade da 27002:2022) e 5 questões
- seção sobre estruturação do SGSI, hierarquia política-norma-procedimento, métricas e acompanhamento de desempenho (cláusula 9) com 6 questões
- capítulo sobre classificação e rotulagem da informação (critérios de sigilo, níveis, responsáveis, tratamento por classe, controles 5.12 e 5.13 da 27002:2022) e 6 questões
- capítulo sobre classificação e rotulagem de ativos de informação, inventário e responsabilidade, controle de acesso lógico e segurança física e do ambiente (temas físico e tecnológico da 27002:2022) e 8 questões
- seção sobre planejamento, identificação e análise de riscos (ativos, ameaças, vulnerabilidades, impactos, níveis de risco) e 6 questões
- seção sobre definição, implantação e gestão da política de segurança (aprovação, comunicação, revisão) e auditoria de SGSI, com 5 questões
- capítulo sobre a versão 2013 (ISO/IEC 27001:2013, Anexo A com 114 controles em 14 domínios; 27002:2013 com seções 5 a 18), PDCA e 8 questões
- seção sobre processo de gestão de riscos de segurança (identificação, análise, avaliação, tratamento) integrado à continuidade de negócios, com 6 questões
- capítulo sobre segurança física (perímetros, mesa limpa, equipamentos) e lógica (identificação, autenticação, autorização, controle de acesso) com mapeamento aos temas físicos e tecnológicos da 27002:2022 e 8 questões
- seção sobre ISO/IEC 27005 (processo de gestão de riscos, critérios, avaliação, tratamento e aceitação de riscos) e 6 questões

### 15. `informatica.arquitetura-software` — Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto
Arquivo atual: `conteudo/materias/informatica/arquitetura-software.json` · 68 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre interoperabilidade de sistemas: integração por APIs, web services, mensageria, formatos XML/JSON e padrões de integração, com 6 questões
- capítulo sobre web services (SOAP/WSDL/UDDI x REST, contrato, XML/JSON) e 6 questões
- capítulo sobre arquitetura de aplicações web: navegador, servidor web, servidor de aplicações, requisição/resposta HTTP, sessão e balanceamento, com 6 questões
- seção sobre servidor de aplicações x servidor web (contêiner de servlets, pool de conexões, transações, exemplos Tomcat/JBoss) e 4 questões
- capítulo sobre os cinco princípios SOLID (SRP, OCP, LSP, ISP, DIP) com exemplos em Java e 6 questões
- capítulo detalhando SOAP (envelope, header, body, fault), WSDL (types, message, portType, binding, service) e UDDI (registro e descoberta) e 6 questões
- capítulo sobre desenho de arquitetura de solução: visões arquiteturais, decisões e trade-offs entre atributos de qualidade, documentação (C4/ADR) e 5 questões
- capítulo sobre web services (conceitos, SOAP/REST, WSDL, aplicações) e 6 questões
- capítulo sobre interoperabilidade de sistemas (níveis técnico, sintático e semântico, integração por serviços e APIs) e 5 questões
- capítulo sobre padrões de integração (EIP, mensageria), web services SOAP e REST (verbos, recursos, códigos de resposta) e 8 questões
- capítulo sobre a PDPJ-Br: arquitetura distribuída de microsserviços, APIs RESTful (recursos, verbos HTTP, JSON) e integração entre sistemas judiciais, com 6 questões
- capítulo sobre SOAP: envelope, header, body, fault, binding HTTP, WS-Security e comparação com REST, com 5 questões

### 16. `portugues.interpretacao-textual` — Interpretação de textos
Arquivo atual: `conteudo/materias/portugues/interpretacao-textual.json` · 42 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre relação do texto com seu contexto histórico e social (época, autoria, circunstâncias de produção) e 5 questões
- capítulo sobre intertextualidade (citação, alusão, paráfrase, paródia, epígrafe) e 6 questões
- capítulo sobre hierarquia de ideias principais e secundárias e sobre recursos de argumentação (exemplificação, causa, autoridade, comparação), com 6 questões
- capítulo sobre fato x opinião (marcas de subjetividade, modalizadores, verbos de opinião) e sobre intencionalidade discursiva, com 6 questões
- capítulo sobre distinção entre fato e opinião (marcas de subjetividade, adjetivação, modalização) e 5 questões
- capítulo sobre leitura de material gráfico (tabelas, gráficos, infográficos, charges, tirinhas) e 6 questões
- capítulo sobre intertextualidade (citação, alusão, paráfrase, paródia) e questões correspondentes
- capítulo sobre fato e opinião (marcas de subjetividade, modalizadores) e 5 questões
- capítulo sobre intencionalidade discursiva (finalidade do texto, persuasão, informação, crítica) e 5 questões
- capítulo sobre leitura de texto literário (linguagem conotativa, narrador, personagens, tempo e espaço) em contraste com não literário, e 6 questões
- capítulo sobre intertextualidade e sobre organização estrutural do texto (partes, parágrafos, progressão), com 6 questões
- capítulo sobre leitura crítica e de textos não verbais e mistos (charge, tirinha, cartaz, infográfico) e 6 questões

### 17. `contabilidade-publica.pcasp-mcasp` — PCASP e MCASP
Arquivo atual: `conteudo/materias/contabilidade-publica/pcasp-mcasp.json` · 58 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre realização das variações patrimoniais (reconhecimento por competência de VPA e VPD, momento do fato gerador) com exemplos e 5 questões
- capítulo sobre ativo no setor público: definição, controle de recursos, reconhecimento, circulante e não circulante, com 5 questões
- capítulo sobre passivo no setor público: definição, obrigação presente, reconhecimento, circulante e não circulante, com 5 questões
- parte sobre saldo patrimonial (patrimônio líquido) e como resulta de ativo menos passivo, com 4 questões
- parte sobre classificação completa das variações patrimoniais (quantitativas e qualitativas, aumentativas e diminutivas) e 4 questões
- capítulo sobre variações patrimoniais qualitativas (permutativas): fatos que trocam elementos sem alterar o patrimônio líquido, com exemplos e 5 questões
- capítulo sobre a Parte II do MCASP (reconhecimento, mensuração e evidenciação patrimonial, depreciação, reavaliação, provisões) e 8 questões
- capítulo sobre sistemas contábeis (orçamentário, financeiro, patrimonial e de compensação) e sua relação com variações patrimoniais, com 5 questões
- capítulo sobre variações qualitativas (permutativas) com exemplos numéricos, além das quantitativas, e 4 questões
- capítulo sobre a Parte I do MCASP (10ª edição): receita orçamentária, despesa orçamentária, estágios e fonte/destinação de recursos, com 8 questões
- capítulo sobre a Parte II do MCASP (procedimentos contábeis patrimoniais), com 8 questões
- capítulo sobre comparação entre 9ª, 10ª e 11ª edições do MCASP, o ementário de natureza de receita e a estrutura do PCASP, com 6 questões

### 18. `afo.lei-responsabilidade-fiscal` — Responsabilidade na gestão fiscal
Arquivo atual: `conteudo/materias/afo/lei-responsabilidade-fiscal.json` · 67 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre limites de despesa com pessoal por ente e Poder (percentuais da RCL, limite prudencial, alerta, medidas de recondução, arts. 19 a 23) e 8 questões; hoje a matéria explica o mecanismo mas não os percentuais
- capítulo sobre destinação de recursos públicos ao setor privado (arts. 26 a 28: autorização por lei específica, condições, cobertura de déficit de empresas, subvenção e socorro a instituições financeiras) e 6 questões
- capítulo sobre gestão patrimonial (conservação do patrimônio, destinação de ativos, precatórios, dívida ativa; art. 43 em diante) e 6 questões
- seção sobre escrituração e consolidação das contas, restos a pagar e dívida pública (art. 50 e correlatos) e 5 questões
- capítulo sobre a Lei nº 4.320/1964 (estrutura do orçamento, receitas e despesas, créditos adicionais, balanços) em relação com a LRF e 8 questões
- capítulo sobre o Manual de Demonstrativos Fiscais (estrutura geral, partes I a IV, anexos do RREO e do RGF e atualizações da edição vigente) e 8 questões
- capítulo sobre disposições finais e transitórias (prazos de adequação, vedações finais, vigência) e 4 questões
- capítulo sobre o regime fiscal sustentável da LC nº 200/2023 (limite de crescimento da despesa, meta de resultado primário, bandas, gatilhos e consequências) e 8 questões
- capítulo sobre regras fiscais constitucionais (teto de gastos da EC nº 95/2016 e emendas posteriores) e requisitos para proposições que reduzem receita ou aumentam despesa, com 6 questões
- seção sobre federalismo fiscal e atividade financeira do Estado (conceito, receitas e despesas públicas, efeitos da LRF sobre os entes) e 5 questões
- seção sobre o Demonstrativo da Despesa com Pessoal (RGF Anexo 1) do Manual de Demonstrativos Fiscais: estrutura, deduções e apuração, com 5 questões
- seção sobre o Demonstrativo da Dívida Consolidada Líquida (RGF Anexo 2): dívida consolidada, deduções, disponibilidade de caixa e limite, com 5 questões
