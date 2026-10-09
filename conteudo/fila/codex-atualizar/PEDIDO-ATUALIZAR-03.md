# Atualizar matérias — pedido 03 de 18 (09/10/2026)

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

### 1. `auditoria.procedimentos-evidencias` — Procedimentos, evidências, amostragem e papéis de trabalho
Arquivo atual: `conteudo/materias/auditoria/procedimentos-evidencias.json` · 42 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre testes de observância (testes de controle): finalidade, exemplos, relação com o risco de controle e quando deixam de ser aplicados, com 5 questões
- seção sobre testes substantivos (testes de detalhes e procedimentos analíticos substantivos), diferença para os de observância e exemplos, com 5 questões
- seção sobre conciliações, análise de contas contábeis e revisão analítica, com 5 questões
- seção sobre conciliações, análise de contas contábeis e rotinas de revisão, com exemplos de cada técnica, e 5 questões
- seção sobre programa de auditoria: conceito, conteúdo, elaboração e ajustes, com 5 questões
- seção sobre testes de auditoria: tipos, objetivo, desenho e relação com programa e risco, com 5 questões
- seção sobre conciliações, análise de contas contábeis e revisão analítica, e sobre exame documental e conferência de cálculos como nomenclatura clássica, com 5 questões
- seção sobre programas de auditoria na fase de execução: conteúdo, elaboração e revisão, com 5 questões
- seção sobre testes de auditoria na execução: seleção, aplicação e documentação dos resultados, com 5 questões
- seção sobre matriz de planejamento e programa de auditoria, com 5 questões
- seção sobre supervisão e controle de qualidade da auditoria (revisão, responsabilidades, consulta e revisão da qualidade do trabalho), com 5 questões
- seção sobre amostragem por atributos e por unidade monetária (MUS): quando usar, intervalo de amostragem, extrapolação e exemplos numéricos, com 6 questões

### 2. `direito-empresarial.sociedades` — Sociedades
Arquivo atual: `conteudo/materias/direito-empresarial/sociedades.json` · 78 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre sociedades dependentes de autorização do Poder Executivo (art. 1.123 e ss. do CC: sociedade nacional x estrangeira, pedido de autorização, revogação) e 3 questões
- capítulo sobre S/A em profundidade: debêntures, partes beneficiárias, assembleia geral (ordinária/extraordinária, quórum), conselho de administração, diretoria, conselho fiscal e deveres/responsabilidade dos administradores, com 8 questões
- capítulo sobre a Lei 6.404/1976 além das noções: constituição da companhia, capital e ações, acionistas, assembleias, administradores, demonstrações, dividendos, e 8 questões
- seções sobre cooperativa (sociedade simples por forma, Lei 5.764/71, quotas, voto por cabeça) e sociedades coligadas (controle, filiada, simples participação, art. 1.097 e ss. do CC), mais 4 questões
- seção sobre distribuição de lucros e perdas: participação proporcional às quotas, vedação de cláusula leonina, lucros fictícios e responsabilidade, dividendo obrigatório na S/A, com 4 questões
- capítulo sobre sociedade cooperativa: natureza simples, Lei 5.764/1971, responsabilidade limitada ou ilimitada, número mínimo de cooperados, voto por cabeça, sobras e FATES, com 5 questões
- capítulo sobre sociedades coligadas no CC e na Lei 6.404 (controlada, filiada, de simples participação, participação recíproca) e 4 questões
- aprofundamento da S/A (assembleias, administradores, conselho fiscal, acionista controlador, direitos essenciais do acionista) para nível de juiz, com 6 questões
- responsabilidade civil de administradores de S/A e de limitada (deveres de diligência e lealdade, art. 158 da Lei 6.404, ação social, solidariedade) e 4 questões
- capítulo sobre transformação de sociedades (art. 1.113 e ss. do CC e LSA): conceito, direito de retirada, direitos de credores, e 3 questões
- capítulo sobre incorporação (art. 1.116 do CC e LSA): protocolo e justificação, aprovação, sucessão universal, direitos de credores e dissidentes, e 3 questões
- capítulo sobre fusão (art. 1.119 do CC e LSA): constituição de nova sociedade, extinção das fusionadas, sucessão, anulação por credor, e 3 questões

### 3. `informatica.cloud-devops-containers` — Nuvem avançada, contêineres, orquestração e CI/CD
Arquivo atual: `conteudo/materias/informatica/cloud-devops-containers.json` · 71 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre Rancher: gerenciamento multicluster de Kubernetes, importação e provisionamento de clusters, projetos, RBAC e catálogo de aplicações, com 4 questões
- conceito de DevOps, fluxo de merge e branch (pull request, trunk-based x GitFlow) e migração de banco de dados versionada no pipeline (Flyway, Liquibase), com 5 questões
- seção sobre Rancher como plataforma de gestão de clusters Kubernetes (multicluster, RBAC, projetos), com 4 questões
- alta disponibilidade (SLA, redundância, failover), agilidade da nuvem e recuperação de desastres (RPO, RTO, estratégias de backup e réplica) com 5 questões
- capítulo sobre DevOps: cultura, ciclo de vida, métricas DORA e relação com DevSecOps, com 4 questões
- capítulo sobre DevOps: definição, cultura colaborativa entre desenvolvimento e operações, CALMS, automação e medição, com 5 questões
- seção sobre desenvolvimento cloud native: aplicações de 12 fatores, microsserviços na nuvem, serviços gerenciados e APIs, com 4 questões
- conceito de workload em nuvem (cargas de trabalho, tipos e posicionamento) e questões sobre ele, além do que já existe sobre público, privado, IaaS, PaaS e SaaS
- capítulo sobre DevOps: conceitos e práticas de DevOps (cultura, CALMS, DORA), antes de DevSecOps, com 4 questões
- seções sobre APIs reversas (proxy reverso), Rancher e merge de código em Git, com 4 questões
- capítulo sobre DevOps: fundamentos e princípios de DevOps, e mais aprofundamento do Jenkins (Jenkinsfile declarativo, agentes, plugins), com 4 questões
- seção sobre arquitetura de nuvem para Big Data (data lake, processamento distribuído, serviços gerenciados), com 5 questões

### 4. `administracao-geral.funcoes-administrativas` — Funções administrativas: planejar, organizar, dirigir e controlar
Arquivo atual: `conteudo/materias/administracao-geral/funcoes-administrativas.json` · 46 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre natureza, finalidades e características das organizações formais modernas (objetivos, complexidade, tipos de organizações) e 5 questões
- capítulo sobre administração por objetivos (APO): Drucker, etapas, características, vantagens e limitações, e 5 questões
- seção sobre eficiência, eficácia, efetividade e qualidade (definições, exemplos e diferenças) e 6 questões
- seção sobre fatores contingenciais dos sistemas de controle (ambiente, tecnologia, porte, estratégia) e 4 questões
- seção sobre impacto das escolas da administração na gestão de pessoas (visão do homem em cada escola) e 5 questões
- seção sobre sistemas de controle organizacional (burocrático, de mercado, de clã), controle estratégico e medição de desempenho, com 4 questões
- capítulo sobre papéis do administrador (Mintzberg: interpessoais, informacionais e decisórios), habilidades (Katz) e funções gerenciais, com 6 questões
- seção sobre técnicas de apoio à decisão (brainstorming, Delphi, árvore de decisão, pesquisa operacional) e 4 questões
- seção sobre ferramentas de apoio à decisão (árvore de decisão, Delphi, brainstorming, simulação) e 4 questões
- seção sobre sistemas de medição de desempenho (indicadores, balanced scorecard) e vantagens e desvantagens dos tipos de controle, com 5 questões
- capítulo sobre papéis (Mintzberg) e habilidades (Katz) do administrador e 5 questões
- seção sobre níveis hierárquicos (institucional, intermediário, operacional) e competências gerenciais e 5 questões

### 5. `administracao-geral.projetos-qualidade` — Gestão de projetos e gestão da qualidade
Arquivo atual: `conteudo/materias/administracao-geral/projetos-qualidade.json` · 53 tópicos de edital pedem mais conteúdo

Falta:
- Elaboração, análise de viabilidade e avaliação de projetos (justificativa, objetivos, metas, indicadores, avaliação ex ante e ex post, VPL, TIR e payback) em capítulo próprio, com questões.
- Capítulo comparando modelos de gestão de projetos (cascata/preditivo, iterativo, ágil, híbrido) e quando usar cada um, com questões.
- Gerenciamento da integração, das comunicações e da documentação do projeto (plano, mudanças, encerramento), com questões.
- Gestão de programas e portfólio em profundidade (governança, priorização, benefícios, indicadores), com questões.
- Gerenciamento de recursos e da equipe do projeto (plano de recursos, desenvolvimento e liderança da equipe, conflitos) e questões.
- Qualidade em serviços (dimensões, gaps do cliente, SERVQUAL, momentos da verdade) e questões.
- Gerenciamento das comunicações (plano, canais, matriz de comunicação) e questões.
- Gerenciamento das aquisições (tipos de contrato, processo de contratação) e questões.
- Processo decisório e solução de problemas (modelos de decisão, racionalidade limitada, técnicas) e questões.
- Mapeamento e melhoria de processos (fluxograma, BPMN, análise de processos) e questões.
- Mudança organizacional na administração pública (resistência, modelos de gestão da mudança) e questões.
- Inovação no setor público e melhoria contínua além do PDCA (Kaizen, inovação aberta) e questões.

### 6. `informatica.bi-mineracao-dados` — BI, data warehouse, OLAP, ETL e mineração de dados
Arquivo atual: `conteudo/materias/informatica/bi-mineracao-dados.json` · 62 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre integração e ingestão além do ETL/ELT: transferência de arquivos (FTP/SFTP, carga em lote) e integração via base de dados (CDC, replicação, db links, views), com 6 questões
- capítulo sobre otimização de bases multidimensionais: agregações pré-calculadas, índices bitmap, particionamento, tabelas agregadas e visões materializadas, com 6 questões
- seção sobre data mesh (domínios, dados como produto, plataforma self-service, governança federada) e 4 questões
- seção sobre visualização de dados (escolha de gráficos, dashboards, boas práticas) e tratamento/limpeza de dados, com 6 questões
- seção sobre ferramentas de BI (Power BI, Qlik, Tableau) em nível conceitual, dashboards, KPIs e aplicabilidade, com 5 questões
- capítulo sobre pipeline de dados: etapas, orquestração, processamento em lote x fluxo, idempotência, monitoramento e ferramentas de orquestração, com 6 questões
- seção sobre detecção de anomalias: abordagens estatísticas (z-score, IQR), por distância/densidade (LOF), isolation forest e uso em fraude, com 5 questões
- capítulo sobre modelagem física do DW: tabelas, índices (bitmap, B-tree), particionamento, agregados e desempenho de consultas, com 8 questões
- capítulo sobre otimização de bases multidimensionais: agregações, índices bitmap, particionamento e visões materializadas, com 6 questões
- capítulo sobre integração e ingestão: transferência de arquivos (FTP/SFTP, lote) e integração via banco de dados (CDC, replicação), com 6 questões
- seção sobre formatos de dados XML, JSON e CSV (estrutura, vantagens, uso em integração e ingestão) e 5 questões
- seção sobre dashboards, relatórios e visualização, e sobre Qlik Sense (associações em memória, conceito de data discovery) em nível conceitual, com 5 questões

### 7. `direito-administrativo.regime-juridico-fontes` — Direito Administrativo: conceito, fontes e regime jurídico-administrativo
Arquivo atual: `conteudo/materias/direito-administrativo/regime-juridico-fontes.json` · 43 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre Estado (povo, território, governo soberano), governo (função política) x administração (execução), e 5 questões
- conceitos de Estado, governo e Administração Pública (elementos do Estado, distinção entre ato de governo e ato de administração); 5 questões
- capítulo de Estado e governo: elementos, poderes, natureza e fins do Estado, governo x administração; 6 questões
- capítulo sobre Estado, governo e administração (elementos e distinção), complementando as fontes já cobertas; 4 questões
- Estado, elementos, poderes e organização do Estado (forma federativa, entes) e governo x administração; 5 questões
- elementos do Estado (povo, território, governo soberano) e sua relação com a Administração; 4 questões
- origem histórica do Direito Administrativo (Estado de Direito, caso Blanco, Conselho de Estado francês) e natureza jurídica; 4 questões
- capítulo sobre Estado, governo, elementos, poderes, organização, natureza, fins e princípios do Estado; 6 questões
- Estado, governo e Administração: conceitos, elementos, poderes e organização do Estado; 5 questões
- seção de jurisprudência do STF/STJ aplicada à supremacia e indisponibilidade do interesse público (ex.: ponderação, controle de mérito) e 5 questões
- capítulo sobre Estado, governo e Administração Pública: conceitos, elementos e distinção; 5 questões
- conceito de Estado: elementos (povo, território, soberania), poderes e funções; 5 questões

### 8. `orcamento-publico.320-1964` — Lei n. 4.320/1964
Arquivo atual: `conteudo/materias/orcamento-publico/320-1964.json` · 50 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre programação financeira: quotas trimestrais e cronograma de desembolso, limites de movimentação e empenho, descentralização de créditos e recursos (destaque e provisão) e 6 questões
- seção sobre dívida pública na Lei 4.320/1964: dívida flutuante (restos a pagar, serviços da dívida a pagar, depósitos, débitos de tesouraria) e dívida fundada (empréstimos de longo prazo), arts. 92 a 100, com 5 questões
- seção sobre controle e acompanhamento da execução orçamentária (arts. 75 a 82: controle interno e externo, verificação de legalidade, fidelidade funcional, resultados) e 5 questões
- seção sobre fundos especiais (arts. 71 a 74: conceito, receitas vinculadas por lei, normas próprias de aplicação, controle e prestação de contas) e 5 questões
- seção sobre programação financeira: quotas trimestrais e cronograma de desembolso, limites de movimentação e empenho, descentralização de créditos e recursos (destaque e provisão) e 6 questões e seção sobre sistemas de informação da execução orçamentária e financeira (SIAFI, sistemas de planejamento e orçamento, acompanhamento da execução) e 4 questões
- seção sobre programação de desembolso (cronograma, quotas trimestrais, ajuste ao fluxo de caixa) e 4 questões sobre os mecanismos retificadores
- seção sobre elaboração da proposta orçamentária (arts. 22 a 33: competência do Executivo, propostas parciais, consolidação, mensagem e anexos) e 5 questões
- seção sobre elaboração da Lei de Orçamento (arts. 22 a 33: proposta orçamentária, competências e prazos, tramitação) e 5 questões
- seção sobre orçamentos e balanços de autarquias e demais entidades (arts. 107 a 110: regime financeiro, vinculação, contas) e 4 questões
- seção sobre as disposições finais da Lei 4.320/1964 (regime de transição, prazos, unificação, vigência) e 3 questões
- seção sobre descentralização orçamentária e financeira (destaque e provisão de créditos, repasse e sub-repasse de recursos, unidade gestora cedente e recebedora) e 5 questões
- seção sobre restos a pagar (processados e não processados, art. 36) e despesas de exercícios anteriores (art. 37), com 6 questões

### 9. `direito-constitucional.ministerio-publico` — Ministério Público e MPT
Arquivo atual: `conteudo/materias/direito-constitucional/ministerio-publico.json` · 30 tópicos de edital pedem mais conteúdo

Falta:
- seção de contraste com a Advocacia Pública (arts. 131 e 132: AGU, Procuradorias, representação judicial e consultoria) e 5 questões (ou ligar também a direito-constitucional.advocacia-defensoria)
- seção sobre Advocacia Pública e Defensoria Pública (arts. 131 a 135) e 8 questões (ou ligar também a direito-constitucional.advocacia-defensoria)
- seção sobre Advocacia Pública, advocacia e Defensoria Pública (arts. 131 a 135) e 8 questões (ou ligar também a direito-constitucional.advocacia-defensoria)
- capítulo sobre a organização do MPU na LC 75/1993 (ramos, chefias, órgãos colegiados, carreira e regime disciplinar) e 8 questões
- seção sobre Advocacia Pública (arts. 131 e 132) e 5 questões (ou ligar também a direito-constitucional.advocacia-defensoria)
- seção sobre iniciativa legislativa do MPU (leis de criação de cargos, remuneração e organização) e 4 questões
- seção sobre as demais funções essenciais à Justiça (Advocacia Pública, advocacia e Defensoria Pública), que o tópico menciona junto ao MP, e 6 questões (ou ligar também a direito-constitucional.advocacia-defensoria)
- seção sobre Advocacia Pública (arts. 131 e 132), que o tópico exige junto ao MP, e 5 questões (ou ligar também a direito-constitucional.advocacia-defensoria)
- capítulo sobre a Lei Orgânica Nacional do Ministério Público dos Estados (Lei 8.625/1993): órgãos, Procurador-Geral de Justiça, Conselho Superior, garantias e carreira, com 8 questões
- seção sobre iniciativa legislativa do MP (propor ao Legislativo a criação e extinção de cargos e serviços auxiliares, remuneração) e 4 questões
- seção sobre a atuação do MP na tutela de povos indígenas e comunidades tradicionais (art. 129, V e art. 231, atuação do MPF e Funai) e 5 questões
- seção sobre os órgãos da Administração Superior do MP (Procurador-Geral, Colégio, Conselho Superior e Corregedoria) e o regime disciplinar da carreira, com 6 questões

### 10. `direito-ambiental.fundamentos-snuc` — Fundamentos do direito ambiental: competências, Política Nacional e SNUC
Arquivo atual: `conteudo/materias/direito-ambiental/fundamentos-snuc.json` · 67 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a defesa do meio ambiente como princípio da ordem econômica (art. 170, VI, da Constituição) e sua relação com o art. 225, com 5 questões
- capítulo sobre áreas de preservação permanente (hipóteses legais, função ambiental, supressão e regime de uso) em comparação com unidades de conservação, com 6 questões
- capítulo de teoria geral: conceito, natureza, objeto, autonomia e fontes do direito ambiental, com 6 questões
- capítulo sobre bens ambientais (natureza de bem difuso, bem de uso comum do povo, patrimônio genético, águas, fauna e flora) e 6 questões
- seção sobre ética ambiental (antropocentrismo, biocentrismo, ecocentrismo, responsabilidade intergeracional) e 4 questões
- seção sobre as incumbências do Poder Público no art. 225, § 1º, da Constituição e os deveres da coletividade, com 5 questões
- seção sobre deveres ecológicos e a regulamentação da atividade econômica (art. 170, VI, art. 186, II, e art. 225 da Constituição) e 5 questões
- seção sobre os espaços e componentes do art. 225, § 4º, da Constituição (Floresta Amazônica, Mata Atlântica, Serra do Mar, Pantanal, Zona Costeira) e sobre fauna, flora, ilhas, mar territorial e praias como bens ambientais, com 6 questões
- seção sobre o Fundo Nacional do Meio Ambiente (finalidade, origem dos recursos, vinculação ao Ministério do Meio Ambiente) e 3 questões
- capítulo sobre áreas de preservação permanente (hipóteses do Código Florestal, função ambiental, supressão em casos de utilidade pública, interesse social e baixo impacto) e 6 questões
- seção sobre proteção e conservação da biodiversidade (Convenção sobre Diversidade Biológica, patrimônio genético e conhecimento tradicional, fauna e flora ameaçadas) e 6 questões
- capítulo sobre florestas (Código Florestal, florestas públicas, reserva legal e uso sustentável) e 6 questões

### 11. `direito-previdenciario.seguridade-custeio` — Seguridade social: custeio, contribuições, segurados e dependentes
Arquivo atual: `conteudo/materias/direito-previdenciario/seguridade-custeio.json` · 75 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre a previdência social em si (art. 201, caráter contributivo e equilíbrio, regimes, benefícios do RGPS em visão geral) com 5 questões.
- Capítulo sobre assistência social (art. 203 e 204, CF; LOAS: objetivos, organização, benefícios eventuais, serviços) com 6 questões.
- Origem e evolução legislativa no Brasil, com 4 questões.
- Capítulo sobre origem e evolução legislativa da seguridade no Brasil (Eloy Chaves, LOPS, CLPS, CF/88, reformas) e 4 questões.
- Capítulo sobre fontes do direito da seguridade social (Constituição, leis, decretos, regulamentos, normas do INSS), hierarquia e 4 questões.
- BPC com requisitos de idade, deficiência, renda per capita, grupo familiar, avaliação e regras da Lei 8.742, hoje em um só parágrafo, com 8 questões.
- Auxílio-inclusão (Lei 14.176/2021 e regras de acesso por quem recebia BPC e passa a trabalhar) e 4 questões.
- Origem e evolução legislativa no Brasil, hoje ausente, com 4 questões.
- Origem e evolução legislativa da seguridade no Brasil, hoje ausente, com 4 questões.
- Conceito de não segurados (excluídos do RGPS) e quadro completo dos segurados, com 4 questões.
- Isenção/imunidade das contribuições, alíquotas e base de cálculo por contribuinte, prescrição e decadência das contribuições, com 8 questões.
- Origem e evolução da seguridade e detalhe da organização, em nível de AGU, com 4 questões.

### 12. `economia.macroeconomia-moeda-inflacao` — Macroeconomia: moeda, inflação e política monetária
Arquivo atual: `conteudo/materias/economia/macroeconomia-moeda-inflacao.json` · 59 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre agregados monetários M1, M2, M3 e M4 no Brasil: composição de cada um, liquidez, base monetária e papel-moeda em poder do público, com 5 questões
- capítulo sobre balancete das autoridades monetárias e dos bancos comerciais, ativos e passivos monetários, fontes e usos da base e do M1, com 5 questões
- seção sobre relação entre inflação e crescimento (custos da inflação, inflação e produto de longo prazo, imposto inflacionário), com 4 questões
- seção sobre demanda por moeda (motivos transação, precaução, especulação) e construção da curva LM com política monetária deslocando a curva, com 5 questões
- seção sobre mercado de bens, função consumo, investimento e poupança, multiplicador keynesiano e derivação da curva IS, com 6 questões
- seção sobre demanda e oferta de moeda e instrumentos detalhados: operações de mercado aberto, redesconto e depósito compulsório, com 6 questões
- seção sobre equilíbrio no mercado monetário com demanda por moeda sensível à renda e aos juros e determinação da taxa de juros, com 5 questões
- seção sobre a curva LM (inclinação, deslocamentos) e distinção entre juro real e nominal no mercado monetário, com 5 questões
- seção sobre instrumentos de política monetária (open market, redesconto, compulsório, Selic e meta) com exemplos de efeito em cada, com 6 questões
- capítulo sobre política fiscal (gastos, tributos, multiplicador fiscal, efeito deslocamento) e sua interação com a política monetária, com 6 questões
- seção sobre choques de oferta, inflação de custos, estagflação e curva de oferta agregada, com 5 questões
- capítulo desenvolvendo o IS-LM: equilíbrio simultâneo, deslocamentos por política fiscal e monetária e eficácia em casos extremos, com 7 questões

### 13. `ciencias.anatomia-sistema-esqueletico-fisiologia-muscular` — Anatomia do Sistema Esquelético, Fisiologia Muscular, Circulação Sanguínea, Respiração…
Arquivo atual: `conteudo/materias/ciencias/anatomia-sistema-esqueletico-fisiologia-muscular.json` · 65 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o sistema muscular: os três tipos de músculo com estrutura, principais grupos musculares e suas ações, tipos de contração (isotônica, isométrica) e fadiga, com 6 questões
- capítulo de histologia: epitelial, conjuntivo (propriamente dito, cartilaginoso, ósseo, sangue, adiposo), muscular e nervoso com características estruturais e funcionais de cada, e 8 questões
- capítulo sobre crânio, coluna vertebral (regiões e curvaturas), caixa torácica, cinturas e classificação das articulações, com 6 questões específicas
- capítulo sobre órgãos do sistema respiratório (vias aéreas, pulmões, pleura) e mecanismo da ventilação (diafragma, inspiração e expiração), com 5 questões
- capítulo sobre composição do sangue, anatomia do coração, ciclo cardíaco, pulso, tipos de vasos e circulação sistêmica e pulmonar, com 6 questões
- capítulo sobre anatomia dos órgãos urinários e dos sistemas genitais masculino e feminino, com 6 questões
- capítulo sobre os órgãos do tubo digestório e glândulas anexas, sequência da digestão por segmento, com 5 questões
- capítulo sobre divisão do sistema nervoso, meninges, SNC, SNP, sistema somático e visceral (simpático e parassimpático), com 6 questões
- capítulo sobre imunidade ativa e passiva (natural e artificial), vacinas e soros, com 5 questões
- capítulo sobre posição anatômica, planos (sagital, coronal, transversal), eixos e termos de direção, com 4 questões
- capítulo sobre o esqueleto axial e apendicular com ossos nomeados, articulações e fraturas, com 6 questões
- capítulo sobre anatomia do coração, vasos, ciclo cardíaco, pressão arterial e ECG em noções, com 6 questões voltadas a enfermagem de emergência

### 14. `estatistica.variaveis-aleatorias-distribuicoes` — Variáveis aleatórias e distribuições
Arquivo atual: `conteudo/materias/estatistica/variaveis-aleatorias-distribuicoes.json` · 42 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre as demais distribuições além de binomial e normal: Poisson, geométrica, hipergeométrica, uniforme, exponencial, t, qui-quadrado e F, com 10 questões
- capítulo sobre momentos de ordem k (brutos e centrais), assimetria e curtose e relação entre momentos, com 5 questões; a esperança já está coberta
- capítulo sobre distribuições especiais (Poisson, geométrica, hipergeométrica, uniforme, exponencial, t, qui-quadrado, F) com média e variância de cada uma e 10 questões
- capítulo sobre distribuições de probabilidade além de binomial e normal: Poisson, uniforme, exponencial, geométrica e noções de t, qui-quadrado e F, com 8 questões de identificação e cálculo
- distribuições t de Student, qui-quadrado e F de Snedecor (graus de liberdade, forma, uso das tabelas) e 6 questões; densidade, distribuição acumulada, valor esperado e normal já estão cobertos
- momentos (brutos e centrais), covariância e coeficiente de correlação entre duas variáveis (propriedades, Var(X±Y) com covariância) e 8 questões
- Poisson, geométrica, uniforme, exponencial, qui-quadrado, t e F (Bernoulli e binomial já cobertas), com 10 questões
- capítulo com as principais distribuições discretas (Poisson, geométrica, hipergeométrica, uniforme discreta) e contínuas (uniforme, exponencial, t, qui-quadrado, F) e 10 questões
- capítulo sobre a distribuição uniforme discreta e contínua: função de probabilidade/densidade, F(x), média (a+b)/2, variância (b-a)²/12 e 6 questões; hoje há só um exemplo resolvido
- Poisson, exponencial e uniforme com média e variância e 6 questões; a parte de probabilidade básica (eventos, axiomas) fica em estatistica.probabilidade-avancada
- função geradora de momentos (definição, obtenção de média e variância por derivadas, FGM de binomial e normal) e 4 questões
- distribuições de probabilidade além de binomial e normal: Poisson, geométrica, uniforme, exponencial, t e qui-quadrado, com 8 questões

### 15. `contabilidade.escrituracao-operacoes` — Escrituração e operações contábeis
Arquivo atual: `conteudo/materias/contabilidade/escrituracao-operacoes.json` · 55 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre os livros contábeis obrigatórios (Diário, Razão e livros fiscais exigidos) e a documentação contábil, com formalidades e 5 questões
- capítulo com lançamentos de juros, descontos, tributos sobre vendas, aluguéis, variação monetária e cambial, folha de pagamento, provisões, depreciação e baixa de bens, e 12 questões
- capítulo sobre plano de contas: conceito, elenco de contas, função e funcionamento, codificação e contas sintéticas e analíticas, com 6 questões
- capítulo com lançamentos de juros, descontos, tributos, aluguéis e variação monetária/cambial, com exemplos numéricos, e 8 questões
- capítulo com contabilização de folha de pagamento (salários, encargos, provisão de férias e 13º), compras, vendas, provisões, depreciação e baixa de bens, e 8 questões
- capítulo sobre plano de contas (classificação patrimoniais e de resultado, natureza devedora e credora, origens e aplicações de recursos) e sobre a estrutura das contas, com 6 questões
- capítulo com lançamentos de juros, descontos, tributos, aluguéis, variação cambial, folha de pagamento, compras, vendas, provisões, depreciação e baixa de bens, e 12 questões
- capítulo sobre plano de contas e sua relação com as demonstrações contábeis (como as contas alimentam balanço e DRE), com 5 questões
- capítulo sobre apuração do resultado do exercício (encerramento das contas de resultado, ARE e transferência ao patrimônio líquido), com 5 questões
- subseção comparando regime de competência e regime de caixa com lançamentos de cada um e 5 questões específicas sobre o regime de caixa
- capítulo sobre escrituração de operações com mercadorias (compra, venda, devoluções, CMV pelo inventário periódico e permanente) e 6 questões
- lançamentos de juros (ativos e passivos, apropriação por competência, juros sobre empréstimos e descontos) e 4 questões

### 16. `portugues.reescrita` — Reescrita de frases e parágrafos
Arquivo atual: `conteudo/materias/portugues/reescrita.json` · 24 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre retextualização: adequação de textos a gêneros e níveis de formalidade (formal para informal e vice-versa), com exemplos e 6 questões
- seção sobre discurso indireto livre (identificação, diferença para direto e indireto, efeito de sentido) e 5 questões
- seção sobre discurso indireto livre e sua distinção de direto e indireto, com transformações entre eles, e 5 questões
- capítulo sobre reescrita entre gêneros e níveis de formalidade (retextualização, troca de registro), e 6 questões
- seção de treino para confronto de frases corretas e incorretas (concordância, regência, pontuação, paralelismo) e 8 questões
- seção sobre discurso indireto livre, com identificação e conversão entre os três tipos de discurso, e 5 questões
- seção sobre discurso indireto livre, além do direto e indireto já tratados, e 4 questões
- capítulo sobre reescrita em gêneros textuais e níveis de formalidade variados (registro formal e informal) e 6 questões
- seções sobre ordem direta e inversa (sujeito posposto, inversão e efeito de ênfase) e sobre discurso indireto livre, e 6 questões
- seção sobre discurso indireto livre, com exemplos de narrativa e transformação entre os discursos, e 4 questões
- capítulo sobre adequação ao gênero e ao nível de formalidade na reescrita (registro, vocabulário, tratamento) e 6 questões
- capítulo sobre reescrita de frases em gêneros e níveis de formalidade variados, e 6 questões

### 17. `direito-empresarial.empresario-estabelecimento` — Empresário e estabelecimento empresarial
Arquivo atual: `conteudo/materias/direito-empresarial/empresario-estabelecimento.json` · 45 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre o processo decisório do registro (decisão singular e colegiada, exigências, recurso ao plenário e ao DREI), com 5 questões
- seção sobre inatividade da empresa (arquivamento do registro após 10 anos sem movimento, reativação) e 3 questões
- seção sobre órgãos de registro de empresa (DREI, Juntas Comerciais), com 5 questões
- seção sobre os órgãos do SINREM (DREI e Juntas Comerciais), atos de registro (matrícula, arquivamento, autenticação) e 5 questões
- capítulo sobre fundamentos do direito empresarial: autonomia, fontes e características, com 4 questões
- capítulo sobre origem e evolução histórica do direito comercial, autonomia, fontes e características, com 5 questões
- seção sobre órgãos de registro de empresa (DREI, Juntas Comerciais, competências), com 5 questões
- capítulo sobre origem e evolução histórica, autonomia, fontes e características do direito empresarial, com 5 questões
- capítulo sobre origem, evolução histórica, autonomia, fontes e características do direito empresarial, com 4 questões
- capítulo sobre fundamentos do direito empresarial (evolução, autonomia, fontes), com 4 questões
- seção sobre prepostos e auxiliares da empresa (gerente, contabilista, art. 1.169 a 1.178 do Código Civil), com 5 questões
- capítulo sobre prepostos (gerente, contador, caixeiro): poderes, responsabilidade e prazo de ação, com 6 questões

### 18. `afo.orcamento-publico-principios` — Orçamento público e princípios orçamentários
Arquivo atual: `conteudo/materias/afo/orcamento-publico-principios.json` · 37 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre a base constitucional e legal do orçamento no Brasil (CF arts. 165 a 169, Lei 4.320/1964 e LRF) em visão geral, com a estrutura orçamentária brasileira; 6 questões
- conceito de orçamento público com espécies (legislativo, executivo e misto), natureza jurídica (lei formal, autorizativa x impositiva) e 6 questões de direito financeiro
- base constitucional e legal do orçamento brasileiro (arts. 165 a 169 da CF, Lei 4.320/1964, LRF) e a organização do sistema orçamentário federal; 5 questões
- seção sobre a evolução do orçamento público (origem na Magna Carta, orçamento tradicional, de desempenho, orçamento-programa, PPBS, base zero e por resultados; marcos no Brasil) e 6 questões
- princípios contábeis aplicados ao setor público (entidade, continuidade, oportunidade, registro pelo valor original, competência, prudência) e relação com os princípios orçamentários; 5 questões
- história e evolução do orçamento (da Magna Carta ao orçamento-programa, marcos brasileiros) e natureza jurídica do orçamento (lei formal, autorizativa x impositiva); 6 questões
- espécies de orçamento (legislativo, executivo, misto) e natureza jurídica (lei formal, teorias sobre o caráter autorizativo), além de 6 questões de direito financeiro
- evolução do orçamento, espécies (legislativo, executivo, misto) e natureza jurídica do orçamento; 6 questões
- evolução do orçamento (tradicional, de desempenho, programa, base zero, resultados) cronologicamente, ligada aos princípios; 5 questões
- normas legais aplicáveis ao orçamento (CF art. 165 a 169, Lei 4.320/1964, LRF) com a função de cada uma nos métodos, técnicas e instrumentos; 6 questões
- evolução conceitual do orçamento público (de instrumento de controle à ferramenta de planejamento e gestão), com 5 questões
- espécies de orçamento (legislativo, executivo e misto) e natureza jurídica do orçamento (lei formal, autorizativa x impositiva); 6 questões
