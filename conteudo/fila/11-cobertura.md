# Cobertura dos editais — matérias novas

Ranking de 07/10/2026 (550 editais). O usuário autorizou fazer TODAS as ondas abaixo, uma após a
outra, sem perguntar de novo (07/10/2026). Objetivo: biblioteca ampla para que o plano grátis use
matéria pronta em vez de gerar com IA (custo).

## Como tocar cada onda (Claude)
1. Para cada id da onda, lançar um agente: "Projeto: <repo>. Leia conteudo/fila/PEDIDO-MATERIA-AGENTE.md
   e siga exatamente. Sua matéria é <id> (escopo em conteudo/fila/11-cobertura.md)." (máx. 20 agentes
   simultâneos; 12 por onda.)
2. Quando cada uma voltar, lançar o auditor: passos 2–6 de PEDIDO-AUDITORIA-AGENTE.md direto em
   conteudo/entrada/<id>.json, version 1, sem copiar nem apagar; conferir números de artigo/súmula
   citados; matérias de programação: RODAR o código (node; tsc; Python via Pyodide no Node).
3. Com a onda toda auditada: conferir.ts em cada uma → receber.ts → ligar-editais.ts → importar.ts
   (resolver apelido repetido: genérico sai das duas; senão sai da antiga e sobe a versão dela) →
   publicar.sh → commit + push. Marcar [x] aqui.

## Onda 1 — [x] publicada em 07/10/2026
processo-civil: jurisdicao-competencia, sujeitos-processo, atos-prazos-processuais,
formacao-suspensao-extincao, recursos, execucao · processo-penal: jurisdicao-competencia-sujeitos,
citacoes-sentenca-procedimentos, nulidades-recursos-habeas-corpus · informatica: javascript,
typescript, python.

## Onda 2 — [x] publicada em 08/10/2026
- [x] **direito-constitucional.defesa-estado-instituicoes** — Estado de defesa, estado de sítio, Forças Armadas (noção, sem repetir a matéria forcas-armadas), segurança pública na CF.
- [x] **direito-constitucional.ordem-economica-financeira** — Princípios da ordem econômica, atuação do Estado, política urbana e agrícola, sistema financeiro nacional na CF.
- [x] **direito-constitucional.tributacao-orcamento** — Sistema tributário na CF (visão geral, sem repetir competência/limitações), repartição das receitas tributárias, finanças públicas e orçamentos na CF.
- [x] **direito-civil.familia** — Casamento, regimes de bens, união estável, parentesco, filiação, poder familiar, alimentos, guarda, tutela e curatela.
- [x] **direito-civil.sucessoes** — Sucessão em geral, herança, vocação hereditária, ordem da sucessão legítima, herdeiros necessários, testamento, inventário e partilha (noções).
- [x] **direito-civil.responsabilidade-civil** — Atos ilícitos, abuso de direito, responsabilidade subjetiva e objetiva, nexo, dano material, moral e estético, excludentes, responsabilidade por fato de terceiro e da coisa.
- [x] **administracao-geral.funcoes-administrativas** — Teorias da administração (noções), planejamento, organização (estruturas), direção (liderança, motivação, comunicação), controle, tomada de decisão, cultura organizacional.
- [x] **gestao-pessoas.fundamentos-subsistemas** — Gestão de pessoas: recrutamento e seleção, treinamento e desenvolvimento, avaliação de desempenho, gestão por competências, clima e qualidade de vida, comportamento organizacional.
- [x] **administracao-geral.projetos-qualidade** — Gestão de projetos (ciclo de vida, PMBOK em noções, escopo, prazo, custo, riscos), gestão da qualidade (PDCA, ferramentas, 5S, qualidade no serviço público).
- [x] **administracao-publica.modelos-governanca** — Patrimonialismo, burocracia, administração gerencial, reformas do Estado, governança pública, accountability, gestão por resultados, excelência nos serviços públicos.
- [x] **informatica.linux** — Linux: conceitos, distribuições, estrutura de diretórios, permissões, usuários, comandos básicos do terminal, pacotes.
- [x] **informatica.internet-busca-redes-sociais** — Ferramentas de busca (operadores), redes sociais, grupos de discussão, fóruns, wikis, comunicação online, segurança e privacidade no uso.

## Onda 3 — [x] publicada em 08/10/2026
- [x] **sustentabilidade.desenvolvimento-sustentavel** — Desenvolvimento sustentável, ODS, A3P, compras sustentáveis, resíduos sólidos (noções), mudanças climáticas (noções).
- [x] **informatica.html-css-web** — HTML5, CSS3, HTTP, APIs REST e JSON, noções de front-end.
- [x] **informatica.engenharia-software-ageis** — Processos de software, requisitos, UML, testes, Scrum, Kanban, XP, DevOps (noções).
- [x] **informatica.governanca-ti** — ITIL 4, COBIT, gerenciamento de serviços de TI.
- [x] **informatica.redes-protocolos** — Modelo OSI e TCP/IP, endereçamento IP, roteamento, VLAN, DNS, DHCP, protocolos de aplicação.
- [x] **informatica.criptografia-certificacao** — Criptografia simétrica e assimétrica, hash, assinatura e certificado digital, ICP-Brasil (noções), VPN, IDS/IPS, firewall.
- [x] **informatica.estruturas-dados-algoritmos** — Algoritmos, complexidade, vetores, listas, pilhas, filas, árvores, ordenação e busca.
- [x] **informatica.orientacao-objetos** — Classes, objetos, encapsulamento, herança, polimorfismo, interfaces, princípios SOLID (noções).
- [x] **contabilidade.estrutura-conceitual-cpc** — Estrutura conceitual, características qualitativas, elementos das demonstrações, DMPL, principais CPCs (noções).
- [x] **legislacao.eca-direitos-fundamentais** — ECA: direitos fundamentais, prevenção, medidas de proteção, conselho tutelar, guarda/tutela/adoção. NÃO: ato infracional (há matéria).
- [x] **direito-empresarial.empresario-estabelecimento** — Empresário, empresa, registro, nome empresarial, estabelecimento, escrituração, EIRELI/SLU (atual).
- [x] **direito-empresarial.sociedades** — Sociedades em geral, limitada, anônima (noções), desconsideração, dissolução.

## Onda 4 — [x] publicada em 08/10/2026 (6 escritas por Haiku, reescritas e auditadas pelo Opus)
- [x] **direito-empresarial.titulos-credito** — Teoria geral, letra de câmbio, nota promissória, cheque, duplicata.
- [x] **direito-empresarial.falencia-recuperacao** — Recuperação judicial e extrajudicial, falência (noções centrais).
- [x] **direito-previdenciario.seguridade-custeio** — Seguridade social na CF, princípios, custeio, contribuições, salário de contribuição, segurados e dependentes.
- [x] **direito-previdenciario.beneficios-rgps** — Benefícios do RGPS: aposentadorias, auxílios, pensão, salário-maternidade, carência, regras após a EC 103/2019 (só o que tiver certeza).
- [x] **direito-tributario.icms-impostos-estaduais** — ICMS (noções gerais na CF e LC), IPVA, ITCMD.
- [x] **direito-tributario.processo-administrativo-fiscal** — Processo administrativo fiscal, consulta, execução fiscal (noções).
- [x] **direito-ambiental.fundamentos-snuc** — Princípios, competências, PNMA, SNUC, áreas de preservação.
- [x] **direito-ambiental.licenciamento-responsabilidade** — Licenciamento, EIA/RIMA, responsabilidade civil, administrativa e penal ambiental.
- [x] **direito-consumidor.relacoes-consumo** — CDC: conceitos, direitos básicos, responsabilidade pelo fato e vício.
- [x] **direito-consumidor.praticas-defesa** — Práticas comerciais, publicidade, cláusulas abusivas, defesa em juízo, SNDC.
- [x] **direito-eleitoral.justica-eleitoral-alistamento** — Organização da Justiça Eleitoral, alistamento, elegibilidade e inelegibilidade.
- [x] **direito-eleitoral.partidos-propaganda** — Partidos políticos, eleições, propaganda, prestação de contas (noções).

## Onda 5
- [x] **direito-internacional.publico-fundamentos** — Fontes, tratados, sujeitos, nacionalidade e estrangeiro (noções), organizações internacionais.
- [x] **auditoria.auditoria-governamental** — Normas de auditoria governamental, tipos, controle externo, achados.
- [x] **controle-externo.controle-interno-coso** — Controle interno, COSO, gestão de riscos, governança.
- [x] **contabilidade.intangivel-impairment-provisoes** — Intangível, redução ao valor recuperável, provisões e contingências.
- [x] **economia.economia-brasileira** — Planos econômicos, inflação, câmbio e política monetária no Brasil (histórico até o presente com cuidado).
- [x] **economia.financas-publicas** — Funções do governo, bens públicos, externalidades, tributação e eficiência, déficit e dívida.
- [x] **administracao-publica.planejamento-estrategico** — Planejamento estratégico, BSC, SWOT, indicadores.
- [x] **raciocinio-logico.logica-primeira-ordem** — Quantificadores, predicados, negação de proposições quantificadas, validade.
- [x] **matematica.matrizes-determinantes** — Matrizes, operações, determinantes, sistemas (Cramer).
- [x] **informatica.bi-mineracao-dados** — Data warehouse, OLAP, ETL, mineração de dados, aprendizado de máquina (noções).
- [x] **informatica.seguranca-normas-iso27001** — Gestão de segurança, ISO 27001/27002, continuidade, gestão de incidentes.
- [x] **informatica.sql-avancado** — Triggers, views, functions, stored procedures, transações, índices, normalização.

## Onda 6
- [ ] **criminalistica.fundamentos-pericia** — Local de crime, cadeia de custódia, vestígios, documentoscopia e balística (noções).
- [ ] **medicina-legal.traumatologia-tanatologia** — Traumatologia, tanatologia, sexologia forense (noções).
- [ ] **legislacao-penal-especial.lavagem-dinheiro** — Lei de lavagem de dinheiro (aspectos penais e processuais).
- [ ] **legislacao-penal-especial.crimes-ordem-tributaria** — Crimes contra a ordem tributária, econômica e relações de consumo.
- [ ] **legislacao-penal-especial.interceptacao-telefonica** — Interceptação telefônica e telemática.
- [ ] **legislacao-penal-especial.execucao-penal** — LEP: regimes, progressão, remição, faltas, direitos do preso.
- [ ] **informatica.cloud-devops-containers** — Modelos de nuvem avançados, contêineres, orquestração, CI/CD.
- [ ] **informatica.arquitetura-software** — Arquiteturas em camadas, MVC, microsserviços, padrões de projeto.
- [ ] **arquivologia.gestao-documentos** — Gestão de documentos, classificação, tabela de temporalidade, preservação digital.
- [ ] **direito-civil.contratos-especie** — Compra e venda, doação, locação, empréstimo, prestação de serviço, mandato, fiança.
- [ ] **historia.brasil-colonia-imperio** — História do Brasil da colonização ao Império.
- [ ] **historia.brasil-republica** — História do Brasil República.

## Onda 7
- [ ] **lingua-espanhola.interpretacao-gramatica** — Leitura e gramática do espanhol para concurso.
- [ ] **administracao-geral.comportamento-organizacional** — Motivação, liderança, grupos, conflito, poder, mudança.
- [ ] **administracao-publica.gestao-processos** — Gestão de processos (BPM), mapeamento, melhoria. (Ver processo-organizacional para não repetir.)
- [ ] **direito-administrativo.improbidade-anticorrupcao** — Lei anticorrupção (empresas), acordo de leniência. (Improbidade já existe; não repetir.)
- [ ] **direito-processual-civil.procedimentos-especiais** — Procedimentos especiais e juizados especiais cíveis (noções).
- [ ] **informatica.ia-generativa-etica** — IA, aprendizado de máquina, IA generativa, ética e uso no setor público.
- [ ] **informatica.sistemas-operacionais-conceitos** — Processos, threads, memória, sistemas de arquivos.
- [ ] **informatica.redes-sem-fio-voip** — Wi-Fi, padrões, segurança sem fio, VoIP (noções).
- [ ] **estatistica.amostragem-pesquisas** — Planos amostrais, tamanho da amostra, pesquisas.
- [ ] **matematica.geometria-espacial-areas** — Áreas de sólidos (complementa volumes).
- [ ] **direito.direitos-humanos-sistema-interamericano** — Comissão e Corte Interamericana (ver matérias existentes para não repetir).
- [ ] **legislacao.estatuto-servidor-estadual-generico** — NÃO FAZER (é específico de cada estado; deixar para a IA).

Depois da onda 7: olhar `library_misses` no servidor (pedidos sem matéria pronta) e priorizar por
demanda real.
