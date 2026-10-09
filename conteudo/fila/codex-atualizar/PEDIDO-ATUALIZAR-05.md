# Atualizar matérias — pedido 05 de 18 (09/10/2026)

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

### 1. `direito-civil.pessoas-juridicas` — Pessoas jurídicas
Arquivo atual: `conteudo/materias/direito-civil/pessoas-juridicas.json` · 15 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre teoria maior e teoria menor da desconsideração, art. 28 do CDC e legislação ambiental, e o incidente do CPC (arts. 133 a 137), com 8 questões
- seção sobre teorias maior e menor da desconsideração, previsões em leis especiais (CDC, ambiental, trabalhista) e o incidente do CPC, com 6 questões
- seção sobre sociedades simples e empresárias (tipos, contrato social, responsabilidade dos sócios), hoje só citadas na classificação, com 6 questões
- seção sobre teoria maior e teoria menor, origem da disregard doctrine e hipóteses de aplicação em leis especiais, com 6 questões
- seção sobre personalidade jurídica e novas tecnologias, liberdade e autonomia privada, teoria maior e teoria menor e aplicação em plataformas e empresas digitais, com 6 questões
- seção sobre novos sujeitos de direito (condomínio, massa falida, espólio, entes não humanos e debates doutrinários) e ampliação do tema de entes despersonalizados, com 5 questões
- seção sobre registro civil das pessoas jurídicas (Lei de Registros Públicos): atribuições, atos registráveis e procedimento, com 5 questões
- seção sobre a doutrina da desconsideração (teorias maior e menor, origem e comparação com outros regimes) e a responsabilidade de sócios e administradores em leis especiais, com 6 questões
- seção sobre a teoria da desconsideração em direito societário (teorias maior e menor, relação com a limitação de responsabilidade e com o CPC), com 6 questões
- seção sobre as teorias da desconsideração (maior, menor, inversa, expansiva) e sua aplicação em leis especiais, com 6 questões
- seção sobre registro de associações, fundações, partidos, entidades religiosas e sociedades (Lei de Registros Públicos, cartórios de RCPJ e Junta Comercial), com 5 questões
- seção sobre organizações religiosas (art. 44 do CC, liberdade de criação, vedação de interferência estatal e registro), com 4 questões

### 2. `processo-civil.cumprimento-sentenca` — Cumprimento de sentença
Arquivo atual: `conteudo/materias/processo-civil/cumprimento-sentenca.json` · 21 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre liquidação de sentença (liquidação por arbitramento e pelo procedimento comum, liquidação provisória, cálculo do contador, recurso cabível) e 8 questões
- aprofundar o cumprimento de sentença contra a Fazenda Pública (art. 534 e 535, defesas cabíveis, honorários, precatório e RPV, obrigações de pequeno valor, ordem cronológica) em capítulo próprio com 8 questões
- aprofundar o cumprimento de sentença contra a Fazenda Pública (art. 534 e 535, defesas cabíveis, honorários, precatório e RPV, obrigações de pequeno valor, ordem cronológica) em capítulo próprio com 8 questões; incluir também a execução de título extrajudicial contra a Fazenda
- regime de precatórios e requisições de pequeno valor (art. 100 da CF), ordem cronológica, preferências e Fazenda Pública em juízo, com 8 questões
- aprofundar o cumprimento de sentença contra a Fazenda Pública (art. 534 e 535, defesas cabíveis, honorários, precatório e RPV, obrigações de pequeno valor, ordem cronológica) em capítulo próprio com 8 questões; incluir obrigações de pequeno valor
- capítulo sobre liquidação de sentença (arbitramento e procedimento comum) e as disposições gerais do cumprimento, com 8 questões
- execução civil de sentença penal condenatória como título executivo judicial (liquidação, legitimidade, efeitos da condenação) e 4 questões
- capítulo sobre liquidação de sentença (liquidação por arbitramento e pelo procedimento comum, liquidação provisória, cálculo do contador, recurso cabível) e 8 questões; destacar formas e procedimento
- Fazenda Pública em juízo: prerrogativas processuais, remessa necessária, prazos em dobro e cumprimento contra a Fazenda, com 8 questões
- inexigibilidade do título judicial fundado em lei ou ato declarado inconstitucional pelo STF (art. 525, §§ 12 a 15 do CPC), hoje com uma menção, e 4 questões
- obrigações de fazer e não fazer contra entes públicos: multa coercitiva contra a Fazenda, responsabilização do agente, limites à tutela específica, e 4 questões

### 3. `informatica.correio-eletronico-outlook-2016-navegacao` — Correio eletrônico (Outlook 2016) e navegação (Google Chrome)
Arquivo atual: `conteudo/materias/informatica/correio-eletronico-outlook-2016-navegacao.json` · 25 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre Mozilla Thunderbird e Outlook Express (configuração de contas, pastas, anexos, endereços) e comparação com o Outlook 2016, com 6 questões
- capítulo sobre Gmail e webmail (rótulos, filtros, arquivar, spam, anexos e links do Drive, segurança e etiqueta digital) e 8 questões
- capítulo sobre o Microsoft Edge (abas, histórico, favoritos, downloads, InPrivate, atalhos) e, no caso do edital 30, buscas na Internet e grupos de discussão, com 8 questões
- capítulo sobre o Outlook Express (recursos, pastas, contas POP/IMAP, catálogo de endereços) e 5 questões
- capítulo sobre webmail (conceito, diferença para cliente de e-mail, acesso por navegador, protocolos HTTP/HTTPS) e 6 questões
- capítulo sobre intranet, busca na web e navegadores diversos (Edge, Firefox) complementando o Outlook, com 6 questões
- capítulo sobre conceitos e serviços da Internet (WWW, e-mail, FTP, intranet, extranet) além do correio eletrônico, com 6 questões
- capítulo sobre protocolos de correio SMTP, POP3 e IMAP (portas, sentido envio/recebimento, diferenças) com 6 questões
- capítulo sobre serviços de busca na Web (operadores, mecanismos) e navegadores além do Chrome, com 6 questões
- capítulo sobre Mozilla Firefox e Thunderbird (abas, histórico, privacidade, filtros) com 8 questões
- capítulo sobre busca na web, intranet e transferência de arquivos (FTP, download e upload) com 6 questões
- capítulo sobre Internet Explorer e Outlook Express (recursos e diferenças em relação ao Chrome e Outlook 2016) com 6 questões

### 4. `direito-tributario.icms-impostos-estaduais` — ICMS e impostos estaduais (IPVA e ITCMD)
Arquivo atual: `conteudo/materias/direito-tributario/icms-impostos-estaduais.json` · 31 tópicos de edital pedem mais conteúdo

Falta:
- estudo da LC 87/1996 (Lei Kandir): contribuintes, fato gerador, local da operação, base de cálculo, substituição tributária, créditos e estornos, com 15 questões
- LC 160/2017: convalidação de isenções e benefícios concedidos sem convênio, remissão e anistia, reinstituição, quórum e registro no Confaz, com 6 questões
- LC 192/2022 e LC 194/2022: ICMS monofásico sobre combustíveis (incidência uma única vez, alíquotas ad rem) e essencialidade de combustíveis, energia, comunicações e transporte coletivo, com 5 questões
- LC 24/1975: convênios do Confaz, quórum de aprovação e ratificação, nulidade e ineficácia de benefícios concedidos sem convênio, com 5 questões
- ICMS nas operações interestaduais, exportações e importações segundo a LC 87/1996: alíquotas interestaduais, DIFAL, manutenção de crédito na exportação, local e momento na importação, com 8 questões
- Resoluções do Senado 22/1989 e 95/1996: alíquotas interestaduais fixadas pelas resoluções, regra de aplicação por região de origem e destino, com 4 questões
- nível avançado: aprofundar LC 87/1996 (créditos, estornos, substituição tributária, diferimento), DIFAL e transição IBS/ICMS, com 10 questões
- diferimento e suspensão do ICMS: conceito, diferença entre as figuras, responsabilidade do adquirente e efeitos no crédito, com 4 questões
- estabelecimento e local da operação ou da prestação no ICMS (LC 87/1996): conceito de estabelecimento, regras de local para mercadorias e serviços, com 5 questões
- crédito do ICMS: bens e serviços que geram crédito, ativo permanente e uso e consumo, compensação e escrituração, com 5 questões
- vedação de crédito do ICMS: hipóteses legais de vedação, uso ou consumo e operações isentas, com 4 questões
- estorno de crédito do ICMS: hipóteses (saída isenta, perecimento, uso diverso), mecânica do estorno, com 4 questões

### 5. `informatica.sql-avancado` — SQL avançado
Arquivo atual: `conteudo/materias/informatica/sql-avancado.json` · 46 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre tuning de consultas e do SGBD (plano de execução, estatísticas, reescrita de consultas, uso de índices, particionamento, cache) e 8 questões
- capítulo sobre tuning de banco de dados (diagnóstico de lentidão, plano de execução, índices, estatísticas, parâmetros do SGBD) e 8 questões
- capítulo sobre análise de desempenho e otimização de consultas SQL (EXPLAIN, plano de execução, reescrita, junções, índices) e 8 questões
- capítulo sobre análise de desempenho e otimização de consultas (tuning), com plano de execução, estatísticas e reescrita, e 8 questões
- capítulo sobre PL/SQL (blocos anônimos, variáveis, estruturas de controle, cursores, exceções, packages) e 8 questões
- seção sobre segurança e conexões (GRANT e REVOKE, papéis, privilégios em views e procedures, pool de conexões) e 5 questões
- capítulo sobre detecção de problemas de desempenho e otimização do SGBD e de consultas SQL (plano de execução, bloqueios, estatísticas, índices) e 8 questões
- capítulo sobre PL/SQL (blocos, variáveis, cursores, exceções, packages) e 8 questões
- seção sobre estruturas de busca e indexação: acesso sequencial, árvores B e B+, hashing e bitmaps, com 8 questões
- seção sobre T-SQL e PL/SQL (variáveis, controle de fluxo, cursores, tratamento de erros, blocos) e 6 questões
- seção sobre cursores (declaração, abertura, fetch, fechamento, cursores explícitos e implícitos) e 5 questões
- capítulo sobre tuning de banco de dados (plano de execução, estatísticas, índices, parâmetros) e 8 questões

### 6. `contabilidade.passivo-patrimonio-liquido` — Passivo e patrimônio líquido
Arquivo atual: `conteudo/materias/contabilidade/passivo-patrimonio-liquido.json` · 28 tópicos de edital pedem mais conteúdo

Falta:
- Seção sobre destinação do resultado na Lei 6.404/1976: ordem (prejuízos acumulados, reserva legal de 5% com limite de 20% do capital, reservas estatutárias, retenção de lucros, dividendo mínimo obrigatório, reserva de lucros a realizar), com lançamentos e 8 questões; a matéria exclui os percentuais legais.
- Capítulo sobre contabilização das reservas de lucros (legal, estatutária, para contingências, de incentivos fiscais, de retenção, de lucros a realizar, de lucros para expansão) com limites e lançamentos, e 8 questões.
- Seção sobre reservas de capital (ágio na emissão de ações, alienação de partes beneficiárias, bônus de subscrição, prêmio na emissão de debêntures, doações e subvenções para investimento antigas), com lançamentos e 6 questões.
- Seção sobre ajustes de avaliação patrimonial (CPC 26 e Lei 6.404, art. 182, §3º): ativos financeiros ao valor justo por outros resultados abrangentes, hedge, ajustes de conversão, tributos diferidos, realização, e 6 questões.
- Seção sobre reservas de lucros (legal, estatutária, orçamentária/retenção, contingências, lucros a realizar, incentivos fiscais) com limites do art. 193 a 199 e o teto de reservas, lançamentos e 8 questões.
- Seção sobre ações em tesouraria (aquisição, alienação, cancelamento, limite pelo saldo de reservas de lucros, dedução do PL, lançamentos de venda acima e abaixo do custo) com 6 questões.
- Seção sobre dividendos (dividendo mínimo obrigatório, dividendos a pagar no passivo, declaração e pagamento, dividendo proposto após o balanço, dividendos fixos e prioritários, lançamentos) com 7 questões.
- Seção sobre fornecedores, obrigações fiscais e trabalhistas (ICMS a recolher, provisão de férias e 13º, INSS, FGTS), adiantamentos e contas a pagar, com lançamentos e 7 questões.
- Seção sobre empréstimos, financiamentos, debêntures e títulos de dívida: juros, encargos a apropriar, custo de transação, taxa efetiva, curto e longo prazo, com lançamentos e 8 questões.
- Seção sobre fornecedores, obrigações fiscais e trabalhistas (impostos a recolher, provisão de férias e 13º, encargos sociais) e outras obrigações, com lançamentos e 7 questões.
- Seção sobre empréstimos, financiamentos, debêntures e títulos de dívida com custo amortizado, taxa efetiva, encargos a apropriar e classificação no circulante e não circulante, com 8 questões.
- Seção sobre juros sobre o capital próprio: base de cálculo e limite (TJLP, 50% do lucro), dedutibilidade, IRRF, contabilização como distribuição do PL e efeito no dividendo mínimo, com 6 questões.

### 7. `direito-administrativo.organizacao-administrativa` — Organização administrativa: direta, indireta e órgãos públicos
Arquivo atual: `conteudo/materias/direito-administrativo/organizacao-administrativa.json` · 36 tópicos de edital pedem mais conteúdo

Falta:
- capítulo-resumo sobre terceiro setor e paraestatais (serviços sociais autônomos, OS, OSCIP), agências e consórcios, ou ligar também à matéria específica, com questões
- trecho sobre os sentidos de Administração Pública: orgânico, formal e material; amplo e estrito; objetivo e subjetivo, e questões
- trecho sobre Estado, Governo, poderes e elementos do Estado, natureza e fins da Administração, e questões
- trecho sobre consórcios públicos e entidades paraestatais/paralelas, com questões
- ligação a serviço público: acrescentar trecho sobre serviços públicos e questões
- trecho sobre servidores/agentes públicos, cargos e funções, com questões
- trecho sobre evolução e tendências da organização administrativa (burocrática, gerencial) e questões
- trechos sobre atos administrativos e requisição administrativa, com questões

### 8. `sustentabilidade.desenvolvimento-sustentavel` — Desenvolvimento sustentável, ODS e A3P
Arquivo atual: `conteudo/materias/sustentabilidade/desenvolvimento-sustentavel.json` · 26 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a Resolução CNJ 400/2021 (política de sustentabilidade do Judiciário, Plano de Logística Sustentável, unidades socioambientais) e 8 questões
- seção sobre acessibilidade na administração pública (acessibilidade de prédios, serviços e contratações) como dimensão da sustentabilidade pública e 4 questões
- capítulo sobre a política de sustentabilidade do STJ e do Poder Judiciário (Resolução CNJ 400/2021, Plano de Logística Sustentável, unidades socioambientais) e 8 questões
- capítulo sobre as competências das unidades socioambientais e o Plano de Logística Sustentável do Judiciário (Resolução CNJ 400/2021) com 8 questões
- seção sobre o Pacto Global da ONU (dez princípios) e a relação com os ODS, com 4 questões
- seção sobre as unidades socioambientais do Judiciário e o Plano de Logística Sustentável (Resolução CNJ 400/2021) com 6 questões
- capítulo sobre ESG (pilares ambiental, social e governança, princípios, normas e relatórios de sustentabilidade) e 6 questões
- seção sobre sustentabilidade organizacional/empresarial (tripé aplicado às empresas, responsabilidade social, ESG, relatórios de sustentabilidade) e 4 questões
- seção sobre economia ambiental (externalidades, bens públicos, instrumentos econômicos, custo-benefício e valoração) e 6 questões
- seção sobre financiamento ambiental, valoração da natureza e serviços ambientais (pagamento por serviços ambientais, mercados de carbono, finanças verdes) e 6 questões
- seção sobre economia circular (princípios, diferença para a economia linear, reuso, remanufatura, reciclagem) e 4 questões
- seção sobre a política externa brasileira em desenvolvimento sustentável (Rio 92, Rio+20, responsabilidades comuns porém diferenciadas, posição do Brasil) e 6 questões

### 9. `direito.definicao-conceito-direitos-humanos` — Definição e conceito de direitos humanos
Arquivo atual: `conteudo/materias/direito/definicao-conceito-direitos-humanos.json` · 21 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre os fundamentos filosóficos dos direitos humanos (jusnaturalismo, contratualismo, Kant, positivismo, teoria crítica) e 4 questões
- capítulo sobre fontes dos direitos humanos (tratados, costume, princípios, soft law) e sobre classificação e princípios (progressividade, vedação do retrocesso, pro persona), com 6 questões
- capítulo sobre terminologia e estrutura normativa dos direitos humanos (regras e princípios, direitos e garantias) e sobre as teorias de fundamentação, com 4 questões
- capítulo sobre terminologia e estrutura normativa dos direitos humanos e sobre fundamentação, com 4 questões
- trecho sintético sobre evolução histórica (Antiguidade, revoluções liberais, pós-guerra) e sobre as classificações, com 4 questões
- trecho sintético sobre evolução histórica e sobre classificações dos direitos humanos, com 4 questões
- capítulo sobre fontes dos direitos humanos (tratados, costume internacional, princípios gerais, jurisprudência, soft law) e 4 questões
- capítulo sobre eficácia vertical e horizontal dos direitos humanos (teoria da eficácia direta e indireta, Drittwirkung) e sobre terminologia, com 5 questões
- capítulo sobre terminologia e estrutura normativa dos direitos humanos e 4 questões
- capítulo sobre tratados de direitos humanos no Brasil: incorporação (assinatura, aprovação, ratificação, decreto), supralegalidade, controle de convencionalidade e conflito com a Constituição, com 6 questões; hoje só há o art. 5º, §3º
- capítulo sobre eficácia, terminologia e afirmação histórica dos direitos humanos, com 5 questões
- capítulo sobre responsabilidade internacional do Estado por violação de direitos humanos e eficácia horizontal, com 5 questões

### 10. `processo-civil.jurisdicao-competencia` — Processo Civil: jurisdição e competência
Arquivo atual: `conteudo/materias/processo-civil/jurisdicao-competencia.json` · 38 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre competência funcional (por fase, grau e objeto) ao lado da territorial, com 5 questões
- capítulo sobre competência em razão do valor da causa e da matéria (Justiça especializada, vara especializada) e 5 questões
- capítulo sobre competência em razão do valor e da matéria no CPC e 5 questões
- capítulo sobre competência da Justiça Federal cível (art. 109 da CF: União, entes federais, causas internacionais) e 6 questões
- seção sobre poderes da jurisdição (decisão, coerção, documentação, instrução) e órgãos jurisdicionais, com 4 questões
- seção sobre escopos da jurisdição, poderes e órgãos jurisdicionais e 5 questões
- capítulos sobre competência em razão do valor, matéria e função, e competência originária dos tribunais, com 8 questões
- capítulo sobre competência em razão do valor e da matéria e competência funcional, com 6 questões
- seção sobre os poderes compreendidos na jurisdição (notio, vocatio, coertio, judicium, executio) e 3 questões
- seções sobre poderes e órgãos da jurisdição e formas de composição da jurisdição civil, com 5 questões
- capítulo sobre competência funcional, com 5 questões
- capítulos sobre competência em razão do valor, matéria e função, com 6 questões

### 11. `contabilidade.estoques-ativo-imobilizado` — Estoques e ativo imobilizado
Arquivo atual: `conteudo/materias/contabilidade/estoques-ativo-imobilizado.json` · 34 tópicos de edital pedem mais conteúdo

Falta:
- tipos de inventário (periódico x permanente), preço específico e UEPS, com 6 questões
- método UEPS (LIFO) e média ponderada móvel com cálculos comparativos de CMV e estoque final nos três métodos, e 6 questões; o PEPS já está coberto
- exaustão (recursos minerais e florestais, base de cálculo por unidades extraídas) e 4 questões; depreciação e amortização já cobertas
- CMV, CPV e CSP: fórmula (estoque inicial + compras líquidas - estoque final), diferença entre mercadorias, produtos e serviços, lançamentos e 8 questões
- tipos de inventário, preço específico e UEPS, tratamento de tributos recuperáveis nas compras e vendas (ICMS, PIS/COFINS), apuração do CMV e 10 questões
- exaustão (base e quota por unidades extraídas) junto à depreciação e amortização, com 4 questões
- apuração do CMV, método do preço específico e média ponderada móvel (UEPS por contraste), com 6 questões
- inventário periódico e permanente (registros, apuração do CMV em cada um), inventário físico e ajustes, com 5 questões
- inventário periódico, permanente e físico, com registros e 5 questões
- exaustão e redução ao valor recuperável (impairment; ver também intangivel-impairment-provisoes), com 5 questões
- operações com mercadorias: compras, devoluções, descontos, fretes, apuração do CMV e resultado bruto, com 8 questões
- redução ao valor recuperável do imobilizado (valor em uso, valor justo líquido, reversão), com 5 questões; ver intangivel-impairment-provisoes

### 12. `auditoria.planejamento-risco-materialidade` — Planejamento, riscos, materialidade e controles internos
Arquivo atual: `conteudo/materias/auditoria/planejamento-risco-materialidade.json` · 28 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a matriz de planejamento da auditoria (objetivo, questões de auditoria, critérios, fontes de informação, procedimentos, achados esperados e limitações) com exemplo preenchido e 5 questões
- capítulo sobre determinação do escopo (objetivo, abrangência, período, unidades, limitações de escopo e sua relação com a estratégia) e 4 questões
- capítulo sobre atividades preliminares do planejamento (levantamento de informações sobre o objeto, análise preliminar de riscos e controles, definição de objetivos, equipe, cronograma) e 4 questões
- capítulo sobre fraude e erro (conceitos e diferença, triângulo da fraude e fatores de risco, responsabilidade do auditor, ceticismo, comunicação à administração e à governança) e 6 questões
- conceito de relevância na auditoria (importância qualitativa e contextual, distinção entre materialidade e relevância, aplicação na seleção de objetos) e 4 questões
- capítulo sobre termos do trabalho de auditoria (pré-condições, acordo/carta de contratação, mudança dos termos) ligado à estratégia global e 4 questões
- capítulo sobre programa de auditoria (objetivos, procedimentos por área, natureza/época/extensão, amostras, responsáveis, prazos, revisão do programa) e 5 questões
- matriz de planejamento (estrutura e preenchimento), relevância e o uso de amostragem já na fase de planejamento, com 6 questões
- gestão de riscos no setor público (modelos como COSO ERM e ISO 31000, papéis das três linhas, matriz/mapa de riscos do órgão) e sua ligação com o plano de auditoria baseado em riscos, com 6 questões
- atividades preliminares do planejamento, determinação de escopo e conceito de relevância, em um capítulo e 6 questões
- matriz de planejamento e programa de auditoria (estrutura, preenchimento e diferença entre os dois instrumentos) e 6 questões
- atividades preliminares e determinação de escopo no planejamento baseado em risco, com 5 questões

### 13. `estatistica.regressao-linear` — Regressão linear
Arquivo atual: `conteudo/materias/estatistica/regressao-linear.json` · 38 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre regressão múltipla (notação matricial, estimador de MQ, propriedades de não viés e variância mínima/Gauss-Markov), testes t e F de significância, R² ajustado e formas funcionais linearizáveis (log-log, semilog), com 8 questões.
- Capítulo sobre inferência nos parâmetros: erro padrão de b, estatística t, teste de H0: b=0, intervalo de confiança do coeficiente, estimativa de sigma² por QME e graus de liberdade (n−2), com 6 questões numéricas.
- Seção sobre a tabela ANOVA da regressão (SQR, SQE, graus de liberdade, quadrados médios, estatística F e sua relação com t² na simples), com 5 questões.
- Capítulo sobre regressão múltipla e o modelo clássico normal (erros normais independentes, distribuição dos estimadores, t e F exatos), com 6 questões.
- Seção que mostra que, com erros normais, o estimador de máxima verossimilhança dos coeficientes coincide com o de mínimos quadrados, e que o estimador de MV de sigma² é viesado, com 4 questões.
- Capítulo sobre regressão múltipla (interpretação ceteris paribus, multicolinearidade, previsão com várias variáveis) além da simples, com 6 questões.
- Seção sobre o critério de máxima verossimilhança em regressão (equivalência com MQ sob normalidade, função de log-verossimilhança) em comparação com mínimos quadrados, com 4 questões.
- Capítulo sobre regressão múltipla (coeficientes parciais, R² ajustado, testes t e F, multicolinearidade) e 6 questões; hoje só há regressão simples.
- Seção de econometria sobre a regressão simples: hipóteses de Gauss-Markov, estimador BLUE, erro padrão e testes t/IC de coeficientes e interpretação econômica, com 5 questões.
- Seção sobre ANOVA da regressão: decomposição SQT=SQR+SQE com graus de liberdade, quadrados médios, teste F de significância global e relação com R², com 5 questões.
- Seção sobre intervalo de previsão: fórmula do erro padrão da previsão individual e da média condicional, largura mínima em x̄, comparação IC x IP, com 5 questões numéricas.
- Capítulo sobre regressão múltipla, inferência nos coeficientes (t, IC), ANOVA com teste F, e 8 questões; hoje a matéria só trata da simples e da análise de resíduos.

### 14. `direito-civil.pessoas-naturais` — Pessoas naturais: personalidade, capacidade e domicílio
Arquivo atual: `conteudo/materias/direito-civil/pessoas-naturais.json` · 35 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre estado civil e estado da pessoa (estado individual, familiar e político; ações de estado; prova pelo registro) e 4 questões
- seção sobre estado civil e estado da pessoa e 4 questões
- capítulo-ponte sobre pessoas jurídicas (conceito, início, espécies) para o Livro I da Parte Geral e 4 questões
- capítulo-ponte sobre pessoas jurídicas (conceito, início, espécies) ao lado das pessoas naturais e 4 questões
- capítulo comparando pessoa natural e jurídica (início, capacidade, representação, desconsideração) e 5 questões
- seção sobre estado civil e estado da pessoa e 4 questões, além de mais questões sobre nome
- capítulo sobre pessoa jurídica (capacidade, representação, domicílio da PJ, art. 75) e 5 questões
- seção sobre estado da pessoa (individual, familiar, político), características e ações de estado, com 4 questões
- seção sobre o procedimento de curatela e interdição (legitimados, curatela compartilhada, atos que o curador pode praticar, prestação de contas) e 5 questões
- seção sobre estado da pessoa (individual, familiar, político) e 4 questões
- capítulo sobre pessoas jurídicas (personalidade, capacidade, direitos da personalidade da PJ) e 5 questões
- capítulo sobre pessoas jurídicas, associações e fundações e 8 questões

### 15. `direito-civil.contratos-especie` — Contratos em espécie: compra e venda, doação, locação, empréstimo, prestação de serviço, mandato e fiança
Arquivo atual: `conteudo/materias/direito-civil/contratos-especie.json` · 28 tópicos de edital pedem mais conteúdo

Falta:
- capítulos sobre os contratos típicos que a matéria exclui (empreitada, depósito, seguro, comissão, agência, corretagem, transporte, transação), com 20 questões
- capítulos sobre os contratos típicos que a matéria exclui (empreitada, depósito, seguro, comissão, agência, corretagem, transporte, constituição de renda, transação), com 20 questões
- capítulo sobre empreitada (espécies, riscos, responsabilidade do empreiteiro, rescisão, prazo de garantia) com 6 questões
- capítulo sobre contrato estimatório (consignação de coisa móvel, risco, restituição) e 4 questões
- capítulos sobre empreitada e depósito (voluntário e necessário, depositário infiel, depósito irregular), com 10 questões
- capítulos sobre comissão, agência, distribuição, corretagem, transporte, seguro, constituição de renda e transação, com 20 questões
- bloco sobre locação em que a Fazenda Pública é parte (regras aplicáveis ao ente público como locador ou locatário), com 4 questões
- capítulos sobre os contratos nominados que a matéria exclui (empreitada, depósito, seguro, comissão, agência, corretagem, transporte, transação), com 20 questões
- capítulo sobre atos unilaterais (promessa de recompensa, gestão de negócios, pagamento indevido, enriquecimento sem causa) e 6 questões
- capítulo sobre contrato estimatório e 4 questões
- capítulo sobre contratos imobiliários: SFH, SFI (alienação fiduciária de imóvel) e Lei de Locações, com 8 questões
- capítulos sobre empreitada, depósito, aval, sociedade, parceria rural e transporte, com 14 questões

### 16. `direito-processual-civil.procedimentos-especiais` — Procedimentos especiais e juizados especiais cíveis
Arquivo atual: `conteudo/materias/direito-processual-civil/procedimentos-especiais.json` · 47 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre Juizados Especiais Federais (Lei 10.259/2001: competência, procedimento, recursos, turma de uniformização) com 6 questões; a parte criminal está em outra matéria
- capítulo sobre protesto, notificação e interpelação (arts. 726 a 729 do CPC) e 4 questões
- capítulo detalhado sobre o Juizado da Fazenda Pública (Lei 12.153/2009: competência, legitimidade, prazos, recursos, execução) e 6 questões
- capítulo sobre Juizados Especiais Federais (Lei 10.259/2001) em detalhe e 6 questões
- capítulo sobre Juizados Especiais Federais (Lei 10.259/2001) e 6 questões
- capítulo sobre Juizados Especiais Federais (Lei 10.259/2001: competência de 60 salários mínimos, partes, procedimento, recursos, execução por RPV) e 6 questões
- capítulo sobre Juizados Especiais da Fazenda Pública (Lei 12.153/2009) e 6 questões
- capítulos sobre Juizados da Fazenda Pública e Juizado Especial Federal (competência, procedimento, recursos) e 8 questões
- habeas corpus no processo civil (cabimento e hipóteses) e questões sobre ele; a parte de Juizados Especiais Cíveis já está coberta
- tutela de interesses individuais, difusos e coletivos (conceitos, legitimidade, coisa julgada coletiva) e 8 questões; a parte de procedimentos especiais já está coberta
- rito detalhado da ação de divisão e demarcação (fase de perícia, memorial, sentença, cumulação, pagamento de quinhões) e 5 questões
- rito detalhado da dissolução parcial de sociedade (legitimidade, citação dos sócios, apuração de haveres, data da resolução) e 5 questões

### 17. `processo-civil.remedios-constitucionais-mandado-seguranca-habeas` — Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção e ação popular
Arquivo atual: `conteudo/materias/processo-civil/remedios-constitucionais-mandado-seguranca-habeas.json` · 33 tópicos de edital pedem mais conteúdo

Falta:
- capítulo detalhado sobre mandado de segurança (Lei 12.016/2009): cabimento e exclusões do art. 5º, legitimação ativa e passiva e litisconsórcio, liminar (art. 7º), competência, sentença e recursos, coisa julgada do MS coletivo (art. 22), e 10 questões; hoje há só decadência, honorários e legitimados do coletivo.
- capítulo de processo coletivo: microssistema (LACP, CDC, Lei 4.717), conceitos de direitos difusos, coletivos e individuais homogêneos, legitimação, coisa julgada secundum eventum litis, litispendência e liquidação e execução coletiva, e 10 questões.
- seção sobre habeas corpus no processo civil: prisão civil do devedor de alimentos (art. 528 do CPC), vedação da prisão do depositário infiel, espécies de HC, legitimidade, competência e procedimento, e 4 questões.
- seção, em nível de Procurador, sobre ação civil pública: inquérito civil, liminar e multa (art. 12), coisa julgada (art. 16), execução e fundo de reparação, recursos (art. 14), e 6 questões.
- cobertura da Lei 7.347/1985 além dos arts. 1º a 5º: inquérito civil (art. 9º), fundo (art. 13), liminar (art. 12), recursos (art. 14), coisa julgada (art. 16), e 7 questões.
- cobertura completa da Lei 12.016/2009: cabimento e exceções (art. 5º), liminar e suspensão de segurança (arts. 7º e 15), competência, MS coletivo (arts. 21 e 22), recursos, e 8 questões.
- aprofundamento em ação civil pública e ação popular (coisa julgada, liminar, execução, relação entre as ações) e 6 questões; a ação de improbidade fica em matéria própria.
- aprofundamento em MS individual e coletivo (liminar, competência, coisa julgada coletiva) e no procedimento do mandado de injunção (Lei 13.300/2016), com 8 questões.
- seção sobre mandado de segurança no processo do trabalho: cabimento contra ato judicial e decisões de tutela provisória, competência dos tribunais, prazo de 120 dias e orientações do TST, com 5 questões.
- seção sobre ritos e tutela antecipada na ação civil pública (art. 12: liminar com ou sem justificação, multa, suspensão pelo presidente do tribunal) aplicada à probidade, patrimônio público, consumidor e meio ambiente, com 6 questões.
- capítulo sobre mandado de segurança individual e coletivo com natureza, conceito, hipóteses de cabimento e detalhes procedimentais (art. 5º, liminar, competência, sentença, recursos, art. 22), e 10 questões.
- capítulo sobre ação civil pública com natureza, conceito, cabimento e procedimento (inquérito civil, liminar, multa, coisa julgada, execução, recursos), e 8 questões.

### 18. `informatica.redes-protocolos` — Redes: modelos, endereçamento e protocolos
Arquivo atual: `conteudo/materias/informatica/redes-protocolos.json` · 42 tópicos de edital pedem mais conteúdo

Falta:
- noções de IPsec (AH e ESP, modos transporte e túnel) e 3 questões; hoje só citado como assunto de criptografia
- camada física, enlace de dados e subcamada MAC (quadro Ethernet, MAC, CSMA/CD, switches e domínios de colisão) e 5 questões
- SNMP (gerente, agente, MIB, traps, portas 161/162) e Ethernet (quadro, MAC) e 4 questões
- IPsec (AH, ESP, modos transporte e túnel) e 3 questões
- LACP (agregação de links, IEEE 802.3ad) e 3 questões
- IPSec, SSL/TLS, NFS, SMB, LDAP e 6 questões
- OSPF (áreas, LSA, DR/BDR, custo) e BGP (eBGP/iBGP, atributos, escolha de rotas) e 4 questões
- controle de fluxo e erro do TCP (ACK, janela deslizante, retransmissão, controle de congestionamento) e 4 questões
- OSPF e BGP em nível de especialista (áreas, LSA, DR/BDR, eBGP/iBGP), RIP em detalhe e switches multilayer, com 5 questões
- janela deslizante, reconhecimento, sockets e controle de erro do TCP, com 5 questões
- LDAP, NFS e SMB e handshake SSL/TLS (conceito em protocolos de aplicação) e 4 questões
- RIP v1 e v2 (diferenças, classful x classless, split horizon), OSPF e BGP em detalhe e 4 questões
