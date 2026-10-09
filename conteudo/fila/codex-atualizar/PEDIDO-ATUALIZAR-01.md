# Atualizar matérias — pedido 01 de 18 (09/10/2026)

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

### 1. `informatica.seguranca` — Segurança
Arquivo atual: `conteudo/materias/informatica/seguranca.json` · 136 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre ferramentas de proteção (antivírus, antispyware, firewall pessoal e de rede, antispam, atualizações): o que cada uma faz, limites e diferenças, com 8 questões
- capítulo sobre princípios da segurança da informação (confidencialidade, integridade, disponibilidade, autenticidade, não repúdio) e conceitos de ameaça, vulnerabilidade e risco, com 8 questões
- capítulo sobre autenticação e controle de acesso (senhas fortes, fatores de autenticação, MFA, biometria, tokens, certificado digital) com 8 questões
- capítulo sobre antivírus, firewall e antispyware (função e limites de cada um) e capítulo sobre backup (tipos, periodicidade, nuvem), com 10 questões
- capítulo sobre backup: tipos (completo, incremental, diferencial), políticas, mídias, armazenamento em nuvem e restauração, com 8 questões
- seção ampliada sobre os demais tipos de malware (rootkit, backdoor, botnet, adware, keylogger, fileless, APT, zero-day, malware polimórfico) e 6 questões
- seção sobre incidentes de segurança (tipos, tratamento, resposta) e 5 questões
- seção sobre variantes de phishing e golpes (spear phishing, whaling, smishing, vishing, baiting, spimming), mail bombing e engenharia social, com 6 questões
- seção sobre ataques comuns (engenharia social, DoS/DDoS, malware) e 5 questões
- seção sobre noções básicas de criptografia (simétrica, assimétrica) e antivírus, com 6 questões
- seção sobre IDS (conceito, tipos, diferença para firewall) e 4 questões
- seção sobre IPS (conceito, diferença para IDS) e 4 questões

### 2. `informatica.banco-dados-sql` — Banco de dados e SQL
Arquivo atual: `conteudo/materias/informatica/banco-dados-sql.json` · 136 tópicos de edital pedem mais conteúdo

Falta:
- capítulo de DDL com sintaxe de CREATE/ALTER/DROP, tipos de dados, restrições na criação de tabelas e questões de DDL
- capítulo de administração de dados/BD (papéis do DBA, projeto e implantação, documentação) e questões
- capítulo de arquitetura de SGBD (níveis de abstração, três esquemas, componentes), organização de arquivos e métodos de acesso, com questões
- modelagem lógica e física (projeto físico, mapeamento ER para relacional) e questões; hoje só há modelagem conceitual e 1FN a 3FN
- capítulo de DCL (GRANT/REVOKE, roles, privilégios), segurança de bancos de dados e questões
- capítulo sobre views, materialized views e índices (tipos, finalidade, uso) e questões
- notações de diagrama (Crow's Foot, IDEF1X, EER, UML) e modelagem orientada a objeto, com questões
- cobertura de PL/SQL, procedures, triggers e extensões procedurais, com questões
- capítulo de álgebra relacional (seleção, projeção, junção, união) e questões
- modelos NoSQL, multidimensional, hierárquico e em rede com exemplos e questões
- capítulo sobre subconsultas (IN, EXISTS, correlacionadas), UNION e questões de consultas em várias tabelas
- capítulo de concorrência, recuperação após falha, bancos distribuídos e orientados a objetos, com questões

### 3. `gestao-pessoas.fundamentos-subsistemas` — Gestão de pessoas: fundamentos e subsistemas
Arquivo atual: `conteudo/materias/gestao-pessoas/fundamentos-subsistemas.json` · 107 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre desenho, descrição e análise de cargos (métodos de coleta, vantagens e desvantagens), administração de cargos, carreiras e salários, remuneração (fixa, variável, estratégica), benefícios e incentivos, com 8 questões
- seção sobre o órgão de recursos humanos: funções, atribuições básicas e objetivos, políticas de pessoal e sistemas de informações gerenciais, com questões
- seção sobre modelos de gestão de pessoas e sua evolução, fatores condicionantes de cada modelo e gestão estratégica de pessoas (integração com a estratégia, limites e possibilidades), com questões
- capítulo sobre gestão de pessoas no setor público: tendências, provimento e concurso, política nacional de desenvolvimento de pessoas (Decreto 9.991/2019, plano de desenvolvimento), com questões
- seção sobre indicadores e métricas de gestão de pessoas (rotatividade, absenteísmo, presenteísmo, people analytics) e avaliação da função de RH, com questões
- seção sobre gestão do conhecimento, aprendizagem organizacional, educação corporativa e aspectos pedagógicos/didáticos do treinamento (objetivos de ensino), com questões
- seção sobre escolas e teorias da administração e seu impacto na gestão de pessoas, com questões
- seção sobre equilíbrio organizacional (incentivos x contribuições) e objetivos e desafios da gestão de pessoas, com questões
- capítulo sobre planejamento de recursos humanos, dimensionamento da força de trabalho, movimentação (promoção, transferência) e desligamento, com questões
- seção sobre o tema (relações com empregados/sindicais, higiene e segurança, orientação e integração, gestão participativa, poder e influência), com questões
- seção sobre gerenciamento da diversidade nas organizações, com questões

### 4. `afo.receita-despesa-publica` — Receita e despesa pública
Arquivo atual: `conteudo/materias/afo/receita-despesa-publica.json` · 110 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre as classificações orçamentárias em detalhe: institucional, funcional (função/subfunção), programática (programa/ação) e por natureza da despesa (categoria, grupo, modalidade, elemento), mais as classificações adicionais do MTO, e cerca de 8 questões
- capítulo sobre dívida ativa (tributária e não tributária: inscrição, certidão, cobrança, tratamento como receita) e cerca de 6 questões
- capítulo sobre programação financeira e execução: cronograma de desembolso, limitação de empenho, descentralização/destaque e provisão de créditos, acompanhamento e controle da execução, e questões
- capítulo sobre classificação da receita: categoria econômica, origem, espécie, rubrica/alínea, tributárias x não tributárias, ordinárias x extraordinárias, patrimoniais, classificações do MTO, e cerca de 6 questões
- capítulo sobre fonte/destinação de recursos (vinculação, fonte ordinária e vinculada) e fontes/origens da receita, com questões
- capítulo sobre dívida flutuante e fundada (conceito, componentes, restos a pagar como dívida flutuante) e questões
- estrutura programática (programa, ação, produto, meta) com questões
- capítulo sobre procedimentos contábeis orçamentários do MCASP (registro de previsão, realização, empenho, liquidação, pagamento, evidenciação) e questões
- classificação do gasto público por finalidade, natureza e agente (funcional e econômica), com questões
- receitas originárias x derivadas em capítulo próprio, com exemplos e cerca de 5 questões
- renúncia de receita, deduções da receita, restituição/anulação e regime de execução (caixa x competência), com questões
- capítulo sobre Conta Única do Tesouro Nacional (conceito, previsão legal, unidade de tesouraria) e questões

### 5. `economia.economia-setor-publico` — Economia do Setor Público
Arquivo atual: `conteudo/materias/economia/economia-setor-publico.json` · 122 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre evolução do papel do Estado (visão clássica, Estado produtor e regulador), formas e dimensões da intervenção, composição de receitas e gastos públicos e avaliação do gasto (custo-benefício), com 5 questões
- seção sobre contabilidade fiscal (NFSP, conceitos nominal, operacional e primário, acima e abaixo da linha), financiamento do déficit (efeito expulsão, equivalência ricardiana), déficit cíclico e estrutural, sustentabilidade da dívida e relação com poupança e setor externo, com 6 questões
- seção sobre financiamento do déficit (tributos, dívida, moeda, imposto inflacionário), instrumentos e gestão da dívida pública (interna e externa, mobiliária e contratual), indicadores e evolução do déficit e da dívida no Brasil, com 6 questões
- seção sobre soluções privadas e públicas para externalidades (teorema de Coase, custos de transação, regulamentação, licenças negociáveis) e tragédia dos comuns, com 5 questões
- capítulo sobre eficiência de Pareto, teoremas do bem-estar, equilíbrio competitivo e função de bem-estar social, com 6 questões
- seção sobre as demais falhas de mercado (assimetria de informação, seleção adversa, risco moral, monopólio natural, bens meritórios) e falhas de governo, com 6 questões
- seção sobre tipos de tributos e classificação econômica (diretos e indiretos, IVA, excise), regra de Ramsey, determinantes do peso morto (elasticidades), incidência em concorrência e monopólio, tributação do patrimônio e efeitos sobre consumo, poupança e gasto, com 6 questões
- seção introdutória sobre conceito, abrangência, objetivos e metas das finanças públicas, com 3 questões

### 6. `direito-administrativo.controle-judicial` — Controle judicial
Arquivo atual: `conteudo/materias/direito-administrativo/controle-judicial.json` · 26 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre os sistemas de controle jurisdicional (contencioso administrativo francês x jurisdição una), inafastabilidade, inexigência de esgotar a via administrativa e 8 questões
- controle e responsabilização da Administração: sistemas de controle jurisdicional e meios de controle judicial (mandado de segurança, ação popular, ação civil pública), com 8 questões
- capítulo sobre sistemas administrativos: sistema inglês (jurisdição una), francês (contencioso administrativo) e o adotado no Brasil, com 6 questões
- controle judicial da discricionariedade: conceitos jurídicos indeterminados, discricionariedade técnica, teoria dos motivos determinantes e limites, com 8 questões
- meios de controle jurisdicional (mandado de segurança, ação popular, ação civil pública, habeas data, ação ordinária) e 8 questões
- controle jurisdicional da atividade financeira do Estado (revisão judicial de decisões de tribunais de contas, ação de improbidade e ressarcimento) e 6 questões
- controle administrativo e jurisdicional: sistemas de controle, relação entre as vias e inexigência de esgotamento da via administrativa, com 8 questões
- meios de controle judicial (mandado de segurança, ação popular, ação civil pública, habeas data, mandado de injunção) e 8 questões
- iniciativa e legitimidade para provocar a apreciação judicial (ações cabíveis, interesse de agir, controle abstrato e concreto) e 6 questões
- controle judicial por meio do processo civil (mandado de segurança, ação popular, ação civil pública, tutelas de urgência contra a Fazenda) e 8 questões
- métodos de controle da discricionariedade (razoabilidade, proporcionalidade, motivação, desvio de finalidade, teoria dos motivos determinantes), com 8 questões

### 7. `portugues.classes-de-palavras` — Classes de palavras
Arquivo atual: `conteudo/materias/portugues/classes-de-palavras.json` · 30 tópicos de edital pedem mais conteúdo

Falta:
- capítulo ligando as classes de palavras às funções sintáticas no período (sujeito, predicado, complementos, adjuntos, orações) e 6 questões de morfossintaxe
- capítulo sobre flexão verbal (pessoa, número, tempo, modo, verbos regulares e irregulares) e 5 questões; a flexão nominal já existe
- capítulos sobre artigos, numerais, pronomes e verbos (aspectos morfológicos, sintáticos, semânticos e textuais) e 10 questões, pois a matéria os exclui
- seção sobre a estrutura do período (termos da oração, coordenação e subordinação) articulada com as classes e 6 questões
- seção sobre elementos mórficos e formação de palavras (radical, afixos, derivação, composição) e sobre flexão verbal, com 6 questões
- seções sobre estrutura e formação de palavras e sobre vozes verbais e flexão verbal, com 8 questões
- seção sobre flexão verbal (conjugação, tempos, modos, formas irregulares e defectivas) e 5 questões; flexão dos nomes já existe
- seção ligando as classes às funções sintáticas no período (sujeito, predicado, complementos, adjuntos) e 6 questões de morfossintaxe
- seção sobre textualidade (coesão e coerência) mostrando o papel de conjunções, pronomes e advérbios como elementos de coesão, com 5 questões
- seção sobre tempos e modos verbais (valores do indicativo, subjuntivo e imperativo) e 5 questões; a flexão das demais classes já existe
- capítulos sobre artigo, numeral, pronome e verbo (emprego e diferenciação) e 8 questões; as demais classes da lista já existem
- seção sobre funções sintáticas dos termos ligados a cada classe (sujeito, objetos, adjuntos, complementos) e 6 questões

### 8. `matematica.estatistica-descritiva` — Estatística descritiva
Arquivo atual: `conteudo/materias/matematica/estatistica-descritiva.json` · 63 tópicos de edital pedem mais conteúdo

Falta:
- desvio médio (cálculo e interpretação) e 3 questões
- curvas de frequência (formas simétrica, assimétrica, unimodal) e leitura de histograma com classes de larguras diferentes, mais 3 questões
- assimetria (relação média-mediana-moda), curtose (platicúrtica, mesocúrtica, leptocúrtica), quartis e 5 questões
- quartis (cálculo e interpretação de Q1, Q2 e Q3) e 4 questões
- assimetria, curtose, medidas de posição como quartis e 5 questões
- coeficiente de variação, amplitude interquartil e 4 questões
- análise exploratória: boxplot, outliers pela regra 1,5 x amplitude interquartil, diagrama de dispersão e 5 questões
- dados agrupados em classes (ponto médio, média por classes, mediana por interpolação) e polígono de frequências, mais 5 questões
- coeficiente de variação, assimetria e curtose e 5 questões
- assimetria e curtose e 4 questões
- assimetria, curtose e 5 questões
- boxplot, outliers e sumarização exploratória (resumo de cinco números) e 4 questões

### 9. `afo.ppa-ldo-loa-ciclo` — Planejamento e ciclo orçamentário
Arquivo atual: `conteudo/materias/afo/ppa-ldo-loa-ciclo.json` · 80 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o Sistema de Planejamento e de Orçamento Federal (Lei 10.180: órgão central, setoriais, funções do sistema) e questões, além de PPA/LDO/LOA já tratados
- capítulo sobre o Sistema de Planejamento e de Orçamento Federal (órgão central, órgãos setoriais, seccionais, atribuições) e o processo de orçamentação, com 5 questões
- emendas parlamentares ao orçamento (individuais, de bancada, de comissão, de relator), impositividade e limites constitucionais (art. 166 e 166-A); 6 questões
- LDO em detalhe: conteúdo do art. 165 §2º, Anexo de Metas Fiscais, Anexo de Riscos Fiscais e critérios de limitação de empenho, além de 5 questões
- classificações orçamentárias (institucional, funcional, programática, natureza da despesa) e estrutura programática; 6 questões
- estrutura do PPA, base legal (art. 165 CF), regionalização, conteúdo (diretrizes, objetivos, metas), tipos de programas (finalísticos e de gestão) e 5 questões; hoje o PPA é tratado só de forma geral
- espécies de leis orçamentárias e sua natureza jurídica (lei formal e ordinária), iniciativa, rito e prazos de tramitação no Congresso (CMO, emendas, devolução); 5 questões
- alterações orçamentárias além de créditos adicionais: remanejamento, transposição e transferência (art. 167, VI), com 4 questões
- processo legislativo orçamentário (iniciativa privativa do Executivo, CMO, emendas, votação em sessão conjunta, prazos do art. 166) e 5 questões
- requisitos e tramitação dos créditos adicionais (projeto de lei x decreto, vigência, autorização legislativa) e limites legais da abertura e do reforço; 4 questões
- outros planos e programas (planos nacionais, regionais e setoriais, art. 165 §4º), sistemas de informação orçamentária e acompanhamento da execução, com 4 questões
- interfaces com a Lei 4.320/1964 e a LRF (AMF, ARF, limitação de empenho, exigências para despesas) e 5 questões

### 10. `direito-eleitoral.justica-eleitoral-alistamento` — Justiça Eleitoral, alistamento, elegibilidade e inelegibilidade
Arquivo atual: `conteudo/materias/direito-eleitoral/justica-eleitoral-alistamento.json` · 52 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre registro de candidatos (Lei 9.504: pedido, prazo, convenções, documentos, indeferimento, substituição) e 8 questões
- conteúdo da Resolução TSE 23.659/2021 (cadastro eleitoral: alistamento, atualização, segunda via, restrições) e 6 questões
- segunda via do título: requisitos, prazo, quem pode requerer e procedimento; 3 questões
- restabelecimento de inscrição cancelada por equívoco: hipóteses, requerimento e efeitos; 3 questões
- formulário de atualização da situação do eleitor (FASE/RAE): finalidade e quando se usa; 3 questões
- título eleitoral: conteúdo, emissão, versão digital e documentos; 3 questões
- acesso às informações do cadastro eleitoral: quem pode consultar, sigilo e certidões; 3 questões
- restrição de direitos políticos no cadastro (suspensão, perda, pendências, quitação eleitoral) e 4 questões
- revisão do eleitorado: hipóteses (fraude, abuso de transferência), procedimento e consequências; 4 questões
- justificação do não comparecimento (prazo, forma, multa, quitação) e acórdão TSE 649/2005; 5 questões
- enumeração das hipóteses da LC 64/1990 (art. 1º, I a VII), prazos e termo inicial, e 8 questões
- revisão do eleitorado: hipóteses, procedimento e efeitos; 4 questões

### 11. `direito-administrativo.servico-publico` — Serviço público: conceito, princípios e classificação
Arquivo atual: `conteudo/materias/direito-administrativo/servico-publico.json` · 66 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre regulamentação do serviço público e sobre formas de prestação e competência (arts. 21, 25 e 30 da CF: União, Estados, Municípios), com 6 questões
- capítulo sobre elementos constitutivos do serviço público (subjetivo, material e formal) com 5 questões
- capítulo sobre formas de prestação (direta, indireta, centralizada e descentralizada por outorga e delegação) e meios de execução, com 6 questões
- capítulo sobre elementos constitutivos do serviço público com 5 questões
- capítulo sobre formas de prestação (direta e indireta, outorga x delegação) e meios de execução, com 6 questões
- capítulo sobre formas de prestação e meios de execução (órgãos, entidades da administração indireta, particulares) com 6 questões
- capítulo sobre elementos do serviço público, formas de prestação e delegação (visão geral) com 6 questões
- capítulo sobre elementos, formas de prestação, execução e delegação (visão geral) com 6 questões
- capítulo sobre elementos constitutivos do serviço público (subjetivo, material, formal) com 5 questões
- capítulo sobre regulamentação e controle do serviço, forma, meios e requisitos de prestação, com 6 questões
- capítulo sobre caracteres jurídicos do serviço público (regime de direito público, mutabilidade, continuidade, igualdade dos usuários) com 5 questões
- seção sobre garantias do serviço público (dever do Estado, controle, responsabilidade) com 4 questões

### 12. `auditoria.auditoria-governamental` — Auditoria governamental: normas, tipos, controle externo e achados
Arquivo atual: `conteudo/materias/auditoria/auditoria-governamental.json` · 54 tópicos de edital pedem mais conteúdo

Falta:
- trecho comparando auditoria interna (vinculada à gestão, IIA, foco em governança e controles) com auditoria externa (independente, EFS/tribunal de contas) e seus papéis, mais 3 questões
- capítulo sobre as normas de auditoria do TCU (Portarias-TCU 280/2010 e 185/2020, estrutura e princípios) e 4 questões
- definições de conceitos básicos das normas gerais (auditoria governamental, controle, auditoria operacional, de regularidade, auditado, evidência) e 3 questões
- objetivos gerais das normas de auditoria governamental (padronizar conceitos e procedimentos, qualidade e credibilidade) e 2 questões
- objetivos específicos das normas de auditoria governamental e 2 questões
- aplicabilidade das normas (a quais auditorias e entidades se aplicam, obrigatoriedade para tribunais de contas) e 2 questões
- amplitude e atualização das normas (revisão periódica, adoção das ISSAI e NBASP) e 2 questões
- capítulo sobre as normas relativas aos tribunais de contas (visão geral dos blocos: objetivos, responsabilidade, competências, independência, estrutura, pessoal e avaliação) e 4 questões
- objetivos dos tribunais de contas segundo as normas de auditoria governamental e 2 questões
- responsabilidade e zelo dos tribunais de contas (prestação de contas, transparência, cuidado devido) e 2 questões
- competências dos tribunais de contas (julgar, fiscalizar, apreciar, sancionar, representar) ligadas ao art. 71 da CF e 3 questões
- independência e autonomia dos tribunais de contas (funcional, financeira e administrativa; garantias dos membros) e 3 questões

### 13. `informatica.engenharia-software-ageis` — Engenharia de software e métodos ágeis
Arquivo atual: `conteudo/materias/informatica/engenharia-software-ageis.json` · 92 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre Lean (princípios, desperdícios, Lean IT, Lean Inception) e questões
- capítulo sobre gestão e priorização do product backlog (MoSCoW, valor x risco, Kano), refinamento, histórias, épicos e critérios de aceitação, com questões
- capítulo sobre análise e projeto de sistemas (paradigma estruturado x orientado a objetos, modelagem de negócio, projeto de software) e questões
- capítulo sobre qualidade de software (atributos ISO/IEC 25010, métricas, análise estática, revisões/inspeções) e questões
- técnicas de estimativa (story points, planning poker, T-shirt sizing) e de priorização, com questões
- capítulo sobre automação de testes (pirâmide de testes, xUnit, Selenium, testes de API/contrato, mocks e stubs) e questões
- outras abordagens ágeis (modelagem ágil, Scrumban, FDD, DDD, MDD/MDA) em capítulo próprio e questões
- tópico sobre produto mínimo viável (MVP), com exemplos, e questões
- refatoração, revisão de código, programação em pares e dívida técnica em capítulo próprio e questões
- BDD/ATDD (Gherkin, dado-quando-então, testes de aceitação) e testes ágeis em capítulo próprio, com questões
- experiência do usuário (UX), usabilidade, design thinking e épicos/features, com questões
- ferramentas, artefatos, métricas e indicadores ágeis (burndown, velocity, quadros, radiadores de informação) e questões

### 14. `direito-constitucional.controle-constitucionalidade` — Controle de constitucionalidade
Arquivo atual: `conteudo/materias/direito-constitucional/controle-constitucionalidade.json` · 72 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre ADI interventiva: art. 34, VII, e art. 36, III, legitimado (PGR), Lei 12.562/2011, decisão e decreto interventivo; 5 questões
- capítulo sobre sistemas gerais de controle (político, jurisdicional, misto; modelos americano e austríaco) e a evolução do sistema brasileiro, com 6 questões
- capítulo sobre controle de leis municipais: via difusa e RE, ADPF contra lei municipal, representação no TJ em face da Constituição Estadual; 6 questões
- capítulo sobre sistemas gerais de controle (político, jurisdicional, misto; modelos americano e austríaco) e 6 questões
- capítulo sobre controle nos Estados e no DF: representação de inconstitucionalidade no TJ, parâmetro (Constituição Estadual), legitimados, simetria, recurso extraordinário contra a decisão; 8 questões
- capítulos sobre histórico e sistemas, objetos e parâmetros de controle, procedimento da ADI/ADC (petição, informações, AGU, PGR, cautelar) e técnicas de decisão (interpretação conforme, sem redução de texto), com 14 questões
- capítulo sobre controle não judicial: comissões (CCJ), veto jurídico, sustação de atos pelo Congresso (art. 49, V), negativa de cumprimento pelo Executivo e Tribunal de Contas (Súmula 347); 6 questões
- capítulo sobre interpretação da Constituição (métodos, princípios, interpretação conforme) e sobre normas constitucionais inconstitucionais (Bachof), com 6 questões
- capítulo sobre representação interventiva federal e estadual: princípios sensíveis, legitimação, rito e efeitos; 5 questões
- capítulo sobre sistemas de controle (político, jurisdicional, misto; difuso x concentrado no direito comparado) e 5 questões
- capítulo sobre reclamação constitucional: cabimento (preservar competência, garantir autoridade de decisão, súmula vinculante, ADI/ADC), legitimidade e procedimento; 5 questões
- capítulo sobre representação de inconstitucionalidade perante o TJ (parâmetro estadual, legitimados do art. 125, § 2º, e efeitos) e 6 questões

### 15. `direito-administrativo.agentes-publicos` — Agentes públicos: classificação, cargo, emprego e função
Arquivo atual: `conteudo/materias/direito-administrativo/agentes-publicos.json` · 62 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre as disposições constitucionais dos arts. 37 a 41 aplicadas ao servidor: acumulação (art. 37, XVI e XVII), teto e subsídio (art. 37, X a XV), estabilidade (art. 41), associação sindical e direitos do art. 39, § 3º; mais 8 questões
- capítulo sobre exercício (início, dispensa, frequência) e afastamentos do servidor: licenças, cessão, afastamento para mandato eletivo, estudo no exterior; 6 questões
- capítulo sobre direitos e deveres do servidor (vencimento, férias, licenças, deveres e proibições) e 6 questões; os regimes jurídicos já estão cobertos
- capítulo sobre direitos e deveres dos servidores civis (vantagens, licenças, deveres e proibições) e 6 questões sobre eles
- capítulo sobre os preceitos constitucionais do servidor (art. 37, I a XXI; arts. 39 a 41; acumulação, teto, estabilidade) e 8 questões
- capítulo sobre deveres e prerrogativas do agente público (poder-dever, probidade, hierarquia, prerrogativas de função) e 5 questões; poderes administrativos ficam na matéria própria
- capítulos sobre estabilidade (art. 41), acumulação de cargos (art. 37, XVI), regime disciplinar em visão geral e regime previdenciário do servidor (art. 40) e 10 questões
- capítulo sobre vacância e suas modalidades e 5 questões
- capítulo sobre preceitos constitucionais aplicáveis ao servidor (arts. 37 a 41) e 6 questões
- seção sobre requisição: requisição de particulares como colaboração compulsória e requisição de servidor (distinção de cessão), com 4 questões
- capítulos sobre vacância, estabilidade, vitaliciedade, remuneração e direitos e deveres do servidor, com cerca de 12 questões; responsabilidade já está coberta
- capítulo sobre a Lei 11.416/2006 (carreiras do Judiciário da União: estrutura, vencimentos, gratificações) e sobre direitos do estatuto federal, com 8 questões

### 16. `legislacao.lei-13146-2015-inclusao` — Estatuto da Pessoa com Deficiência (Lei 13.146/2015)
Arquivo atual: `conteudo/materias/legislacao/lei-13146-2015-inclusao.json` · 37 tópicos de edital pedem mais conteúdo

Falta:
- capítulos que faltam do Livro I da lei: direito à vida, habilitação e reabilitação, moradia, assistência social, previdência, cultura, esporte e lazer, transporte, participação na vida pública e acesso à justiça, com 12 questões
- capítulos faltantes: direito à vida, habilitação e reabilitação, moradia, assistência social, previdência, cultura, esporte, transporte, participação política e justiça, com 12 questões
- capítulos sobre direito à vida, habilitação e reabilitação, moradia, assistência social, previdência, cultura, esporte, transporte, participação política e acesso à justiça, com 12 questões
- capítulos faltantes da Lei 13.146: direito à vida, habilitação, moradia, assistência social, previdência, cultura, esporte, transporte, participação política e justiça, com 12 questões
- capítulos sobre direito à vida, habilitação, moradia, assistência social, previdência, cultura, transporte, participação na vida pública e acesso à justiça, com 12 questões
- capítulos sobre direito à vida, habilitação e reabilitação, moradia, assistência social, previdência, cultura, esporte, transporte, participação política e justiça, com 12 questões
- seção sobre as garantias constitucionais (arts. 7º, 23, 24, 37, 203, 208, 227 e outros) e capítulos sobre moradia, assistência, transporte, cultura e participação, com 8 questões
- capítulos que faltam da lei: direito à vida, habilitação e reabilitação, moradia, assistência social, previdência, cultura, esporte, transporte, participação política e acesso à justiça, com 12 questões
- capítulos que faltam: direito à vida, habilitação e reabilitação, moradia, assistência social, previdência, cultura, esporte, transporte, participação política e acesso à justiça, com 12 questões
- capítulos sobre direito à vida, habilitação, moradia, assistência social, previdência, cultura, transporte, participação política e justiça, e referência à Convenção, com 10 questões
- capítulo sobre a Lei 12.764/2012 (Política Nacional de Proteção dos Direitos da Pessoa com TEA: conceito, diretrizes, direitos e sanção) e capítulos faltantes da Lei 13.146, com 10 questões
- capítulos que faltam da lei: direito à vida, habilitação e reabilitação, moradia, assistência social, previdência, cultura, esporte, transporte, participação política e justiça, com 12 questões

### 17. `portugues.significacao-contextual-palavras-expressoes` — Significação contextual de palavras e expressões
Arquivo atual: `conteudo/materias/portugues/significacao-contextual-palavras-expressoes.json` · 36 tópicos de edital pedem mais conteúdo

Falta:
- parônimos e hiperônimos/hiponímia, com exemplos de pares e 8 questões
- pragmática da linguagem: contexto situacional, atos de fala, implicaturas e dêixis na construção do significado, com 5 questões
- parônimos (e homônimos homógrafos/homófonos) com lista dos pares mais cobrados e 8 questões
- parônimos e heterônimos (e homônimos homógrafos/homófonos), com exemplos e 8 questões
- campos semânticos e campos lexicais: conceito, exemplos e como a questão pede o campo de um termo, com 5 questões
- parônimos, hiponímia e hiperonímia e implícitos (pressupostos e subentendidos), com exemplos e 8 questões
- ironia, comparação e citação (discurso citado) como mecanismos de produção de sentido, com 6 questões
- paronímia e homonímia com pares comumente cobrados, e 6 questões
- leitura de verbetes de dicionário (acepções, abonações, abreviaturas) e escolha da acepção adequada ao contexto, com 5 questões
- monossemia, parônimos e hiperonímia, com exemplos e 6 questões
- hiponímia, hiperonímia e campo semântico, em nível de professor (conceitos e exemplos), com 6 questões
- paronímia: conceito, diferença de homonímia e lista dos pares mais cobrados, com 8 questões

### 18. `matematica.argumentacao-logica` — Argumentação lógica
Arquivo atual: `conteudo/materias/matematica/argumentacao-logica.json` · 28 tópicos de edital pedem mais conteúdo

Falta:
- Seção sobre argumentos por analogia (comparação entre casos, força da analogia, analogia entre estruturas de argumentos) e sobre indução, com 5 questões; a matéria cobre inferências e deduções, mas não analogias.
- Seção sobre tipos de raciocínio (dedutivo, indutivo, abdutivo e por analogia), com a diferença entre garantia de verdade da conclusão e probabilidade, e 5 questões.
- Seção sobre analogias (argumento por analogia e analogia entre estruturas lógicas) e sobre indução, com 5 questões; hoje só há inferências e deduções.
- Seção sobre analogias e indução como argumentação, com 5 questões.
- Seção sobre falácias informais (ad hominem, apelo à autoridade, falso dilema, petição de princípio, generalização apressada, bola de neve, espantalho) além das formais já tratadas, com 7 questões.
- Seção sobre analogias e indução como formas de argumentação, em complemento à dedução, com 5 questões.
- Seção sobre abdução, analogia e indução em comparação com a dedução (força dos argumentos, conclusão provável x necessária), com 6 questões.
- Seção sobre analogias e indução em argumentação, com 5 questões.
- Seção de raciocínio crítico sobre elaboração de argumentos: formular premissas e conclusões, argumento forte x fraco, justificar posição com evidências, com 6 questões.
- Seção de raciocínio crítico sobre avaliação de argumentação: pressupostos, relevância das evidências, fatores que fortalecem ou enfraquecem a conclusão, com 6 questões.
- Seção sobre erros de raciocínio (falácias informais, viés de confirmação, correlação e causalidade, generalização indevida, falso dilema) com 6 questões.
- Seção sobre argumentos dedutivos e indutivos: diferença de natureza, generalização estatística, força indutiva e relação com a validade, com 6 questões.
