# Atualizar matérias — pedido 11 de 18 (09/10/2026)

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

### 1. `fisica.termologia` — Termologia
Arquivo atual: `conteudo/materias/fisica/termologia.json` · 15 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o ciclo de Carnot (quatro etapas: duas isotermas e duas adiabáticas), rendimento máximo 1 - Tf/Tq em kelvin e impossibilidade de rendimento 100%, com 6 questões numéricas e conceituais
- seção sobre a dilatação anômala da água (contração de 0 °C a 4 °C, densidade máxima a 4 °C, congelamento de lagos de cima para baixo) e 4 questões
- capítulo sobre propagação do calor: condução, convecção e radiação, exemplos do cotidiano (garrafa térmica, geladeira, brisa) e 6 questões
- seção sobre entalpia (H = U + pV, variação de entalpia a pressão constante) e aprofundamento da segunda lei e da entropia (ΔS, processos reversíveis e irreversíveis), com 6 questões
- capítulo sobre condução, convecção e radiação (Lei de Fourier, correntes de convecção, lei de Stefan-Boltzmann em nível introdutório) e 6 questões
- seção sobre fluxo de calor e Lei de Fourier (Φ = kAΔT/L), condutividade térmica, associação de paredes em série, com 5 questões numéricas
- seção sobre o comportamento térmico da água (calor específico elevado, dilatação anômala entre 0 e 4 °C, calores latentes e uso em regulação térmica) e 4 questões
- seção sobre a aplicação da termodinâmica à atmosfera (expansão adiabática de parcelas de ar, gradiente adiabático seco e saturado, estabilidade, calor latente na condensação) e 6 questões
- seção sobre entalpia (H = U + pV, processos a pressão constante) e aprofundamento da entropia (segunda lei, variação de entropia em processos reversíveis e irreversíveis), com 6 questões
- capítulo sobre a segunda lei aplicada a ciclos (Carnot, Otto, Diesel, Rankine em nível introdutório), enunciados de Kelvin-Planck e Clausius, eficiência de máquinas e refrigeradores, e 8 questões de nível de engenharia
- capítulo sobre transmissão do calor (condução com Lei de Fourier, convecção com lei de resfriamento de Newton, radiação com Stefan-Boltzmann) e 6 questões
- capítulo sobre mecanismos de transferência de calor (condução, convecção, radiação), fluxo de calor e resistência térmica, e 6 questões

### 2. `informatica.git-versionamento` — Git e controle de versão
Arquivo atual: `conteudo/materias/informatica/git-versionamento.json` · 11 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre gerência de configuração de software: item de configuração, baseline, controle de mudanças, auditoria e relatórios de configuração, releases e relação com o controle de versão, com 8 questões
- Seção sobre Subversion (SVN): modelo centralizado, trunk/branches/tags, commit atômico, numeração de revisões, comandos checkout/update/commit e comparação com Git, com 6 questões
- Seção sobre versionamento semântico (MAJOR.MINOR.PATCH) e automação de build/integração contínua ligada ao fluxo Git, com 6 questões
- Seção sobre gerência de configuração aplicada a branches, tags, trunk, builds e pacotes de liberação (release), com 6 questões
- Capítulo sobre gerência de configuração de software: item de configuração, baseline, controle de mudanças, auditoria e relatórios de configuração, releases e relação com o controle de versão, com 8 questões e entrega contínua
- Seção sobre Subversion (SVN): modelo centralizado, trunk/branches/tags, commit atômico, numeração de revisões, comandos checkout/update/commit e comparação com Git, com 6 questões, além de pipelines GitLab CI/CD (stages, jobs, runners, .gitlab-ci.yml) e Jira

### 3. `direito-trabalho.jornada-horas-extras` — Duração do trabalho: jornada, horas extras e compensação
Arquivo atual: `conteudo/materias/direito-trabalho/jornada-horas-extras.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- capítulos sobre períodos de descanso, intervalos intra e interjornada, descanso semanal remunerado e trabalho noturno (adicional e hora reduzida) com questões
- capítulos sobre intervalos, repouso semanal remunerado e trabalho noturno, com 8 questões
- capítulos sobre intervalos, repouso semanal e trabalho noturno, com 8 questões
- capítulo sobre trabalho noturno (horário, adicional, hora noturna reduzida, prorrogação) e 5 questões
- capítulos sobre intervalos, descanso semanal e trabalho noturno, com 8 questões
- capítulo sobre intervalos intrajornada e interjornada (duração, supressão parcial, natureza indenizatória) e 5 questões
- capítulos sobre repousos, férias, salário e remuneração (integrações), com questões
- capítulos sobre repousos, férias, salário e remuneração, com questões
- capítulos sobre intervalos, repouso semanal, trabalho noturno, turnos ininterruptos de revezamento e teletrabalho (art. 62, III e arts. 75-A e seguintes), com 10 questões
- seção sobre turnos ininterruptos de revezamento (conceito, jornada de seis horas, negociação coletiva, horas extras) e 6 questões
- capítulo sobre férias anuais (período aquisitivo, concessivo, fracionamento, abono, pagamento em dobro) e identificação profissional, com questões

### 4. `portugues.verbos` — Emprego e correlação de tempos e modos verbais
Arquivo atual: `conteudo/materias/portugues/verbos.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre estrutura morfossintática do período (orações coordenadas e subordinadas ligadas ao modo verbal) e 5 questões
- seção sobre estrutura morfossintática do período (orações coordenadas e subordinadas) e 5 questões
- seções sobre estrutura morfossintática e demais classes de palavras, com 8 questões
- seção sobre emprego do verbo haver (impessoal, sentido de existir/ocorrer, concordância) e infinitivo pessoal e impessoal, com 5 questões
- seção sobre aspectos verbais (perfectivo, imperfectivo, durativo) e relação com pronome relativo, conjunção e pontuação, com 5 questões
- seção sobre efeitos de sentido da ordem das expressões na frase, com 4 questões
- seção sobre articulação de tempos em narrativa literária (pretérito perfeito e imperfeito, presente histórico) e 5 questões

### 5. `conhecimentos-bancarios.garantias-fgc-pix` — Garantias, FGC, Pix e Open Finance
Arquivo atual: `conteudo/materias/conhecimentos-bancarios/garantias-fgc-pix.json` · 12 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre penhor mercantil (constituição, objeto, diferença de penhor civil), com 4 questões
- capítulo sobre o Sistema de Pagamentos Brasileiro (STR, compensação, liquidação, câmaras, papel do Banco Central) e 8 questões
- capítulo sobre alienação fiduciária em garantia (bens móveis e imóveis, efeitos na falência e recuperação, execução da garantia) com nível de prova de magistratura e 8 questões
- seção sobre o Drex (real digital, tecnologia de registro distribuído, diferenças em relação ao Pix) e 5 questões
- seção sobre real digital (Drex) e sua relação com o Open Finance, com 5 questões
- seção sobre penhor mercantil, com 4 questões
- capítulo sobre penhor mercantil (conceito, constituição, objeto, bens empenháveis) e 5 questões
- capítulo sobre bancos digitais, meios de pagamento eletrônicos e moeda digital na economia, com 8 questões
- capítulo sobre alienação fiduciária de móveis, de imóveis e no mercado de valores mobiliários, com 10 questões
- capítulo sobre sistemas de pagamento (SPB, instrumentos, liquidação, regulação) em nível de magistratura e 8 questões

### 6. `conhecimentos-bancarios.etica-vendas-atendimento` — Ética, vendas e atendimento financeiro
Arquivo atual: `conteudo/materias/conhecimentos-bancarios/etica-vendas-atendimento.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre marketing de relacionamento (fidelização, CRM, ciclo de vida do cliente) e 5 questões
- seção sobre satisfação, valor e retenção de clientes, com 5 questões
- seção sobre a Política de Relacionamento com o Cliente (princípios, diretrizes, responsabilidades) e 5 questões
- seção sobre valor percebido pelo cliente (benefícios menos custos), com 4 questões
- seção sobre características dos serviços (intangibilidade, inseparabilidade, variabilidade, perecibilidade) aplicadas a produtos bancários, com 5 questões
- seção sobre canais remotos de venda (internet banking, mobile, telefone, correspondentes) e 5 questões
- seção sobre autorregulação bancária (Febraban, SARB) e atendimento ao consumidor, com 5 questões
- capítulo sobre comportamento do consumidor (motivação, percepção, decisão de compra) e 6 questões
- seção sobre as resoluções do CMN que tratam de política de relacionamento com o cliente, com 6 questões
- seção sobre a norma do CMN sobre ouvidoria (constituição, atribuições, prazos de atendimento), com 6 questões
- seção sobre a resolução do CMN sobre tarifas e atendimento ao cliente, com 6 questões
- seção sobre o normativo de autorregulação SARB sobre atendimento na rede de agências, com 5 questões

### 7. `geografia.transportes-economia-brasil` — Transportes e economia brasileira
Arquivo atual: `conteudo/materias/geografia/transportes-economia-brasil.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- a malha viária brasileira com dados e nomes: classificação das rodovias federais (radiais, longitudinais, transversais, diagonais, de ligação), principais eixos, estado de conservação, concessões e comparação com a matriz de transportes do país, com cerca de 8 questões; hoje o texto é só conceitual
- a integração concreta no Brasil entre indústria, cidades, rede de transportes e agropecuária: eixo Sudeste, corredores de exportação, Centro-Oeste, regiões metropolitanas e hierarquia urbana, com mapas e cerca de 8 questões; a matéria não traz nenhum dado brasileiro
- divisão inter-regional do trabalho no Brasil: papel histórico de cada região (Sudeste industrial, Nordeste, Sul, Centro-Oeste, Norte), complementaridade, desigualdades regionais e fluxos entre regiões, com cerca de 8 questões
- a internacionalização da economia brasileira: abertura comercial, Mercosul, BRICS, balança comercial, investimento estrangeiro, empresas transnacionais e pauta exportadora, com dados e cerca de 8 questões; hoje só há texto genérico
- a internacionalização da economia brasileira: abertura comercial, Mercosul, BRICS, balança comercial, investimento estrangeiro, empresas transnacionais e pauta exportadora, com dados e cerca de 8 questões
- divisão inter-regional do trabalho no Brasil: papel histórico de cada região, complementaridade, desigualdades regionais e fluxos entre regiões, com cerca de 8 questões
- a industrialização brasileira e o espaço: ciclos históricos (substituição de importações, Plano de Metas, interiorização), concentração em São Paulo, desconcentração, zonas francas e polos, com cerca de 8 questões; a matéria só tem conceitos gerais de localização industrial
- a evolução da rede brasileira de transportes (ferrovias do ciclo do café, rodoviarismo, hidrovias, portos e corredores atuais) com cronologia, matriz de transportes e cerca de 8 questões
- as fronteiras agrícolas do Brasil: Cerrado, MATOPIBA, Amazônia, arco do desmatamento, soja e pecuária, conflitos fundiários e políticas, com mapas e cerca de 8 questões; hoje o texto não cita regiões
- o agronegócio brasileiro em dados: principais culturas e regiões produtoras, complexos agroindustriais, exportações, crédito rural, agricultura familiar x patronal e sustentabilidade, com cerca de 8 questões
- espaço econômico do Brasil em dados: regiões agrícolas, parque industrial, principais fluxos comerciais e polos, com mapas e cerca de 8 questões; a matéria hoje não traz informações regionais
- a rede de transporte no Brasil em detalhe: matriz de modais com percentuais, principais rodovias, ferrovias, hidrovias, portos e aeroportos, gargalos e concessões, com cerca de 8 questões; hoje só há conceitos genéricos

### 8. `ciencias.unidades-medidas` — Unidades de Medidas
Arquivo atual: `conteudo/materias/ciencias/unidades-medidas.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre sistema métrico decimal: conversões de comprimento, área, volume/capacidade (litro), massa e tempo, com tabela e 10 questões
- conversão de unidades no sistema métrico (múltiplos e submúltiplos, área e volume, litro e m³) com 10 questões
- sistema métrico completo (área, volume, capacidade) e medidas de ângulo e arco (graus, minutos, segundos, radianos) com 10 questões
- sistema legal de medidas: conversões de área, volume, capacidade e unidades de ângulo e arco, com 10 questões
- conversão de comprimento, área, volume, massa e tempo, além de potência e energia (W, kWh, cv), com 10 questões
- sistema métrico decimal e conversões (comprimento, área, volume, capacidade, massa), com 10 questões
- conversões de comprimento, massa, área, volume e tempo no sistema métrico, com 10 questões
- sistema legal de medidas (múltiplos e submúltiplos, área, volume, capacidade, massa) com 10 questões
- conversões de comprimento, superfície, volume, capacidade (litros) e tempo, com 10 questões

### 9. `direito-trabalho.calculos-trabalhistas` — Cálculos trabalhistas
Arquivo atual: `conteudo/materias/direito-trabalho/calculos-trabalhistas.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Panorama das súmulas e orientações jurisprudenciais do TST sobre direito material do trabalho (jornada, salário, rescisão, estabilidades) com questões; a matéria só cita algumas súmulas de cálculo.
- Súmulas vinculantes do STF que tratam de matéria trabalhista (competência, adicional, servidores e FGTS), com questões; a matéria traz apenas duas ou três.

### 10. `saude-publica.sus-legislacao` — SUS: legislação, princípios e controle social
Arquivo atual: `conteudo/materias/saude-publica/sus-legislacao.json` · 16 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre redes de atenção à saúde (Decreto 7.508/2011) e níveis de complexidade (atenção primária, média e alta), com 5 questões
- seção sobre acesso a bens e serviços de saúde (medicamentos, judicialização, RENAME) e 5 questões
- seção sobre níveis progressivos de assistência (atenção primária, secundária e terciária, referência e contrarreferência), com 5 questões
- seção sobre direitos dos usuários do SUS (Carta dos Direitos dos Usuários, Lei 8.080) e 4 questões
- seção sobre ações e programas do SUS (vigilâncias, imunização, programas nacionais), com 5 questões
- seções sobre sistemas de informação em saúde, Pacto pela Saúde e vigilância em saúde, com 6 questões
- seção sobre tutela do direito à saúde e judicialização (ações individuais e coletivas, STF), com 5 questões
- seção sobre avaliação e gestão da saúde no SUS (planejamento, planos de saúde, relatório de gestão, regulação), com 5 questões
- seções sobre políticas de saúde, instituições e níveis progressivos de assistência, com 5 questões
- seções sobre direitos dos usuários, ações e programas do SUS, com 5 questões
- seção sobre políticas públicas de saúde e mecanismos de gestão do SUS (planejamento, pactuação, regulação), com 5 questões
- capítulo sobre noções de direito sanitário (conceito, fontes, regulação), com 5 questões

### 11. `direito-tributario.processo-administrativo-fiscal` — Processo administrativo fiscal, consulta e execução fiscal
Arquivo atual: `conteudo/materias/direito-tributario/processo-administrativo-fiscal.json` · 11 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre as ações judiciais tributárias do contribuinte (anulatória, declaratória, repetição de indébito, consignação em pagamento, mandado de segurança) e sobre a cautelar fiscal, com 8 questões; hoje há só noções de execução fiscal
- seção sobre a ação anulatória de débito fiscal (cabimento, depósito do art. 151, II, relação com a execução fiscal, conexão) e 4 questões; hoje só aparece de passagem
- seção própria sobre a ação cautelar fiscal (Lei 8.397/1992): cabimento, hipóteses do art. 2º, legitimidade, indisponibilidade de bens, competência, e 4 questões
- seção sobre a cautelar fiscal (Lei 8.397/1992): requisitos, hipóteses de cabimento, medida liminar e indisponibilidade de bens do devedor, com 4 questões; hoje há só uma linha
- ação cautelar fiscal (Lei 8.397/1992), declaratória de inexistência de relação jurídico-tributária, anulatória, mandado de segurança, repetição de indébito, consignação em pagamento, controle de constitucionalidade e ação civil pública, com 10 questões
- seção sobre a ação anulatória de débito fiscal (o edital escreveu "execução anulatória"): cabimento, depósito prévio, efeitos sobre a exigibilidade e relação com a execução fiscal, com 4 questões
- ações judiciais do contribuinte (anulatória, repetição de indébito, consignatória, declaratória, mandado de segurança) e relação entre o contencioso administrativo e o judicial, com 6 questões
- ações anulatória, de repetição de indébito (restituição e compensação em juízo), consignatória e declaratória: cabimento, depósito e efeitos, com 8 questões
- seção sobre a ação cautelar fiscal (Lei 8.397/1992): cabimento, hipóteses, liminar e indisponibilidade, com 4 questões; a execução fiscal já está coberta
- garantia da execução (dinheiro, fiança, seguro-garantia, penhora e ordem de bens), indisponibilidade de bens (art. 185-A do CTN) e expropriação (adjudicação, leilão, remição) na execução fiscal, com 8 questões; hoje só há penhora em linhas gerais
- execuções de baixo valor: limites de valor, não ajuizamento, cobrança administrativa, protesto de CDA, transação tributária e racionalização da cobrança, com 5 questões

### 12. `direito-trabalho.direito-coletivo` — Direito coletivo: organização sindical e negociação coletiva
Arquivo atual: `conteudo/materias/direito-trabalho/direito-coletivo.json` · 11 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre organização sindical: natureza jurídica e registro do sindicato, estrutura em sindicato, federação e confederação, centrais sindicais, assembleia e eleição de dirigentes, estabilidade do dirigente, com 8 questões
- capítulo sobre entidades sindicais (sindicatos, federações, confederações e centrais), natureza jurídica, prerrogativas e deveres, representação da categoria e substituição processual, com 8 questões
- capítulo sobre negociação coletiva: princípios (boa-fé, dever de negociar, intervenção sindical obrigatória), fases, cláusulas normativas e obrigacionais, lista do art. 611-A com exemplos e limites do art. 611-B, e 8 questões
- seção sobre o contrato coletivo de trabalho (conceito, distinção em relação à convenção e ao acordo coletivo, eficácia sobre os contratos individuais) e 4 questões
- capítulo sobre natureza jurídica, criação (registro no órgão competente e no cartório), administração (assembleia, diretoria, conselho fiscal) e dissolução de sindicatos, com 8 questões
- capítulo sobre natureza jurídica, criação, administração e dissolução de sindicatos (registro sindical, assembleia, mandato da diretoria, causas de dissolução), com 8 questões
- seção sobre condutas antissindicais (discriminação por filiação, dispensa de dirigente, interferência patronal, recusa de negociar) e suas consequências (nulidade, reintegração, indenização), com 6 questões
- visão geral de dissídio coletivo (natureza econômica e jurídica, comum acordo, sentença normativa) e de greve (Lei 7.783/1989: requisitos, serviços essenciais, abuso), com 8 questões
- capítulo sobre organização sindical: registro, estrutura em sindicato, federação e confederação, centrais, eleição de dirigentes e estabilidade do dirigente, com 8 questões
- visão geral do direito de greve (Lei 7.783/1989: requisitos, serviços e atividades essenciais, abuso, efeitos no contrato) e da greve do servidor público (art. 37, VII, da CF e MI 708 do STF), com 8 questões
- visão geral do direito de greve (Lei 7.783/1989: requisitos, serviços e atividades essenciais, necessidades inadiáveis, abuso e efeitos no contrato), com 6 questões

### 13. `informatica.windows-10-11` — Windows 10 e 11: recursos básicos
Arquivo atual: `conteudo/materias/informatica/windows-10-11.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- Seção sobre configurações do sistema (Configurações e Painel de Controle: resolução de tela, cores, fontes, impressoras, aparência, plano de fundo, proteção de tela) com 8 questões
- Seção sobre instalação e desinstalação de programas (Aplicativos e recursos, Painel de Controle, Microsoft Store, instaladores .exe/.msi) com 5 questões
- Seção sobre principais utilitários do Windows (Gerenciador de Tarefas, Bloco de Notas, Paint, Ferramenta de Captura, Windows Defender) e configurações com 8 questões
- Seção sobre o Painel de Controle e a solução de problemas (solucionadores, restauração do sistema, inicialização segura, Gerenciador de Tarefas) com 8 questões
- Seção sobre versões de 32 e 64 bits do Windows 10 (diferenças de memória, compatibilidade de programas) com 5 questões
- Seção sobre permissões básicas de arquivos e pastas, multitarefa, inicialização e encerramento do Windows e de programas (Gerenciador de Tarefas, reiniciar, suspender, hibernar), com 8 questões

### 14. `estatistica.amostragem-correlacao-tcl` — Amostragem, correlação e teorema central do limite
Arquivo atual: `conteudo/materias/estatistica/amostragem-correlacao-tcl.json` · 12 tópicos de edital pedem mais conteúdo

Falta:
- distribuição conjunta e marginais de duas variáveis aleatórias, covariância populacional, independência e covariância nula (não implica independência), com 8 questões
- lei dos grandes números (fraca e forte, convergência em probabilidade) em seção própria, com enunciado formal e 5 questões
- noções de inferência estatística (estimador e estimativa, viés, consistência, ideia de intervalo de confiança e teste de hipóteses), com 6 questões
- desigualdade de Tchebycheff (enunciado e uso para limitar probabilidades) e lei dos grandes números em seções próprias, com 8 questões
- aproximação normal da binomial (condições, correção de continuidade) e lei dos grandes números, com 8 questões
- medidas descritivas (média, mediana, moda, variância, desvio padrão e quartis) em seção própria, com 6 questões
- definição e cálculo de variância amostral e populacional e sua relação com a covariância, com 5 questões
- regra empírica da distribuição normal (68%, 95% e 99,7%) e sua relação com o TCL, com 5 questões

### 15. `administracao-publica.processo-organizacional` — Processo organizacional
Arquivo atual: `conteudo/materias/administracao-publica/processo-organizacional.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- gestão de pessoas do quadro próprio (dimensionamento, desempenho, desenvolvimento, capacitação) e de terceirizados (fiscalização, níveis de serviço e gestão do contrato), com 8 questões
- administração por objetivos (APO): etapas, características, vantagens e críticas, com 5 questões
- processo de comunicação (emissor, canal, receptor, ruído), barreiras, comunicação formal e informal e fluxos, com 6 questões
- indicadores de desempenho (eficiência, eficácia, efetividade, economicidade), noções de BSC e tipos de controle (prévio, concomitante e posterior), com 8 questões
- tipos e etapas do controle administrativo e medição do desempenho com indicadores, com 8 questões
- governança pública (princípios, mecanismos de liderança, estratégia e controle) e métodos e técnicas de planejamento estratégico (SWOT, BSC, mapa estratégico), com 10 questões
- comunicação pública e institucional na gestão pública (transparência, comunicação governamental) e gestão de redes interorganizacionais, com 8 questões
- comunicação organizacional: comunicação interna e externa, formal e informal, redes e barreiras, com 6 questões

### 16. `direito-constitucional.aplicabilidade-normas-constitucionais` — Aplicabilidade e interpretação das normas constitucionais
Arquivo atual: `conteudo/materias/direito-constitucional/aplicabilidade-normas-constitucionais.json` · 15 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o preâmbulo (natureza, valor jurídico, não é norma de reprodução obrigatória, STF ADI 2076) e sobre os princípios constitucionais, e 6 questões
- seção sobre eficácia e aplicabilidade dos direitos fundamentais (art. 5º, § 1º, aplicabilidade imediata, eficácia vertical e horizontal) e 5 questões
- capítulo sobre o preâmbulo e sobre as disposições constitucionais transitórias (ADCT: natureza e eficácia) além das normas programáticas, e 6 questões
- capítulo sobre diferença entre regras e princípios (Dworkin, Alexy), critérios de solução de conflitos e ponderação, e 6 questões
- capítulo sobre a supremacia da Constituição (rigidez, parâmetro de validade, base do controle de constitucionalidade) e 4 questões
- capítulo sobre supremacia da Constituição (rigidez constitucional, hierarquia das normas, ligação com o controle) e 4 questões
- capítulo sobre conceito e natureza da norma constitucional, espécies (regras, princípios, postulados) e distinções, e 6 questões
- capítulo sobre princípios constitucionais: conceito, função, distinção em relação às regras e classificação (fundamentais, gerais, setoriais), e 6 questões
- seção sobre conflito de normas constitucionais (antinomia, normas originárias, hierarquia entre normas, critérios de solução) e 5 questões
- seção sobre natureza e eficácia das normas de direitos fundamentais (art. 5º, § 1º, dimensão objetiva, eficácia horizontal) e 5 questões
- seção sobre supremacia da Constituição e noção de poder constituinte (originário e derivado) ligadas à aplicabilidade, e 5 questões
- capítulo sobre princípios, regras e postulados normativos (Alexy, Humberto Ávila): conceito, diferenças e solução de conflitos, e 6 questões

### 17. `direito-tributario.obrigacao-fato-gerador` — Obrigação tributária e fato gerador
Arquivo atual: `conteudo/materias/direito-tributario/obrigacao-fato-gerador.json` · 16 tópicos de edital pedem mais conteúdo

Falta:
- capítulos sobre responsabilidade tributária (sucessores e terceiros), imunidade e isenção, e 8 questões
- capítulo sobre responsabilidade tributária (arts. 128 a 138) e 6 questões
- capítulo sobre aspectos e elementos da hipótese de incidência (material, espacial, temporal, pessoal e quantitativo: base de cálculo e alíquota) e 5 questões
- capítulo sobre responsabilidade tributária (arts. 128 a 138: substituição, sucessores, terceiros e infrações) e 8 questões, pois a matéria exclui isso em profundidade
- capítulo sobre lançamento (arts. 142 a 150: modalidades e constituição do crédito) ligando-o à obrigação tributária, e 5 questões
- capítulo sobre constituição do crédito tributário e lançamento e questões que o integrem ao fato gerador e ao domicílio
- capítulo sobre responsabilidade tributária de terceiros e sucessores e sobre crédito tributário, com 8 questões
- capítulo sobre responsabilidade tributária (substituição, sucessores, terceiros, infrações) e 6 questões
- capítulo sobre a regra-matriz de incidência (antecedente e consequente, critérios material, espacial, temporal, pessoal e quantitativo) e 6 questões
- explicação dos critérios da hipótese (material, espacial, temporal) e da consequência (pessoal e quantitativo) com questões
- seção sobre classificações dos fatos geradores (instantâneo, periódico, continuado; simples e complexo; vinculado e não vinculado) e 4 questões
- seção sobre efeitos do fato gerador (nascimento da obrigação, momento de ocorrência, lei aplicável) e 4 questões

### 18. `direito-constitucional.principios-fundamentais` — Princípios fundamentais (arts. 1º a 4º)
Arquivo atual: `conteudo/materias/direito-constitucional/principios-fundamentais.json` · 12 tópicos de edital pedem mais conteúdo

Falta:
- dignidade da pessoa humana (art. 1º, III): conteúdo, dimensões, valor-fonte dos direitos fundamentais e jurisprudência do STF, com 5 questões
- preâmbulo da Constituição: conteúdo, valor jurídico e a posição do STF sobre sua força normativa, com 4 questões
- pluralismo político e demais pluralismos (art. 1º, V): conceito, desdobramentos e jurisprudência, com 4 questões
- Estado Democrático de Direito: elementos do Estado de Direito e da democracia, fundamentos doutrinários e consequências constitucionais, com 5 questões
- dignidade da pessoa humana e sua ligação com os direitos humanos (art. 1º, III e art. 4º, II), com 5 questões
- dignidade da pessoa humana como fundamento da República: conteúdo e relação com os direitos humanos, com 5 questões
- princípio republicano: conceito, eletividade, temporariedade e responsabilidade dos governantes, e 4 questões
- conceito, classificação e conteúdo dos princípios constitucionais positivos e fundamentais, e 5 questões
- Estado de Direito (legalidade, separação de poderes, direitos fundamentais) com 4 questões
- soberania (interna e externa, soberania popular x estatal, relação com a ordem internacional) e 4 questões
- república (conceito, forma de governo, diferença para monarquia) e 4 questões
- democracia (direta, representativa e participativa, art. 1º, parágrafo único) e 4 questões
