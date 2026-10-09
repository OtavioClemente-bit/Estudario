# Atualizar matérias — pedido 08 de 18 (09/10/2026)

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

### 1. `direito-constitucional.poder-executivo` — Poder Executivo
Arquivo atual: `conteudo/materias/direito-constitucional/poder-executivo.json` · 22 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre forma de governo (república e monarquia) e sistemas de governo (presidencialismo, parlamentarismo, semipresidencialismo), com 6 questões
- seção sobre forma e sistema de governo (república/monarquia; presidencialismo/parlamentarismo), com 6 questões
- seção sobre presidencialismo: noções gerais, capacidades governativas, presidencialismo de coalizão e especificidades do caso brasileiro, com 6 questões
- seção sobre o Governador do Estado: atribuições, responsabilidade, simetria com o Presidente, crimes de responsabilidade e foro, com 6 questões
- seção sobre forma e sistema de governo (república/monarquia; presidencialismo/parlamentarismo/semipresidencialismo), com 6 questões
- seção sobre sistemas de governo: presidencialismo, parlamentarismo e semipresidencialismo, com 6 questões
- seção sobre sistemas de governo (presidencialismo, parlamentarismo, semipresidencialismo) e suas diferenças, com 6 questões
- seção sobre parlamentarismo e comparação com o presidencialismo (chefias de Estado e de governo, responsabilidade do gabinete), com 6 questões
- seção sobre o Governador: eleição, atribuições, responsabilidade e simetria com o Presidente, com 6 questões
- seção sobre forma de governo (república e monarquia) e sistema de governo, com 6 questões
- seção sobre forma e sistema de governo, com 6 questões
- seção sobre atribuições e responsabilidade do Governador do Estado, com 6 questões

### 2. `informatica.ferramentas-colaborativas` — Ferramentas colaborativas e nuvem para o usuário
Arquivo atual: `conteudo/materias/informatica/ferramentas-colaborativas.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre Google Tarefas e Keep (criação de tarefas, integração com Agenda e Gmail) e 4 questões
- seção sobre Google Sala de Aula (turmas, atividades, notas, integração com Drive e Meet) e 5 questões
- seção sobre Word, Excel, PowerPoint e Outlook do Microsoft 365 (recursos online e coautoria) e 6 questões
- seção sobre Word, Excel, PowerPoint e Outlook do Microsoft 365, com 6 questões
- seção sobre e-mail, textos, planilhas e apresentações no Microsoft 365, com 6 questões
- seção sobre Cisco Webex e Skype, com 4 questões
- seção sobre Cisco Webex, Google Hangouts e Skype, com 5 questões

### 3. `geografia.industria-energia-agropecuaria-globalizacao` — Indústria, energia, agropecuária e globalização
Arquivo atual: `conteudo/materias/geografia/industria-energia-agropecuaria-globalizacao.json` · 29 tópicos de edital pedem mais conteúdo

Falta:
- matriz energética brasileira e potencial hidrelétrico (bacias, grandes usinas, usinas da Amazônia), impactos socioambientais e dados regionais do Pará, com 8 questões
- geopolítica da nova ordem mundial (fim da Guerra Fria, multipolaridade, blocos econômicos) e fragmentação (regionalismos, nacionalismos, conflitos), com 8 questões
- estágio atual do capitalismo (financeirização, capitalismo informacional, neoliberalismo) e divisão internacional do trabalho em suas fases, com 8 questões
- estrutura do agronegócio internacional (grandes tradings, bolsas de mercadorias, subsídios, OMC, principais produtores e exportadores) com dados, com 8 questões
- geografia econômica em nível de diplomata: teorias de localização, centro-periferia, fordismo e pós-fordismo e dados mundiais, com 15 questões
- divisão internacional do trabalho e globalização em nível de diplomata (centro-periferia, sistema-mundo, cadeias globais) com dados, com 10 questões
- energia, logística e reordenamento territorial pós-fordista (just in time, corredores logísticos, plataformas), com 8 questões
- sistemas agrícolas e geografia agrária mundial e brasileira (estrutura fundiária, Revolução Verde, agronegócio) com dados e mapas, com 12 questões
- distribuição geográfica da agricultura e da pecuária mundiais (principais regiões produtoras, sistemas agrícolas e rebanhos) com mapas e dados, com 8 questões
- estruturação do agronegócio no Brasil e no mundo (complexos agroindustriais, cooperativas, fronteira agrícola, exportações) com dados, com 8 questões
- políticas neoliberais (privatização, abertura comercial, desregulamentação, Consenso de Washington) e sua relação com a globalização, com 6 questões
- nova ordem mundial, espaço geopolítico (blocos, potências, conflitos) e globalização, com 8 questões

### 4. `direito-constitucional.poder-legislativo` — Poder Legislativo e estatuto dos congressistas
Arquivo atual: `conteudo/materias/direito-constitucional/poder-legislativo.json` · 21 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre processo legislativo (espécies normativas, iniciativa, tramitação, sanção e veto) em nível de visão geral e 8 questões (ou ligar também a direito-constitucional.processo-legislativo)
- seção sobre processo legislativo (espécies normativas, iniciativa, tramitação, sanção e veto) em nível de visão geral e 8 questões (ou ligar também a direito-constitucional.processo-legislativo) e fiscalização contábil, financeira e orçamentária
- seção sobre fiscalização contábil, financeira e orçamentária (arts. 70 a 75: TCU e controle externo) e 6 questões (ou ligar também à matéria de fiscalização)
- capítulo sobre o Poder Legislativo estadual (Assembleia Legislativa: composição, deputados estaduais, atribuições e simetria com o modelo federal) e 6 questões
- seção sobre processo legislativo (espécies normativas, iniciativa, tramitação, sanção e veto) em nível de visão geral e 8 questões (ou ligar também a direito-constitucional.processo-legislativo) e fiscalização e controle
- seção sobre processo legislativo (espécies normativas, iniciativa, tramitação, sanção e veto) em nível de visão geral e 8 questões (ou ligar também a direito-constitucional.processo-legislativo) e fiscalização
- capítulo sobre o Poder Legislativo estadual (Assembleias) e municipal (Câmaras e vereadores, art. 29) e 8 questões
- seção sobre processo legislativo (espécies normativas, iniciativa, tramitação, sanção e veto) em nível de visão geral e 8 questões (ou ligar também a direito-constitucional.processo-legislativo) e fiscalização contábil, financeira, orçamentária e TCU
- seção sobre controle jurisdicional das CPIs (reserva de jurisdição, mandado de segurança e habeas corpus contra atos da comissão) e 5 questões
- capítulo sobre o Poder Legislativo do Distrito Federal (Câmara Legislativa, art. 32 da CF e Lei Orgânica) em paralelo ao Congresso e 6 questões
- capítulo sobre as atribuições da Câmara Legislativa do Distrito Federal (art. 32 da CF e Lei Orgânica do DF), em paralelo às do Congresso, com 6 questões
- capítulo sobre a Câmara Legislativa do Distrito Federal e suas atribuições (art. 32 da CF e Lei Orgânica do DF) e 6 questões

### 5. `direito-trabalho.relacao-emprego` — Relação de trabalho e relação de emprego
Arquivo atual: `conteudo/materias/direito-trabalho/relacao-emprego.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre os sujeitos do contrato: conceito e caracterização de empregado (espécies como doméstico, rural e alto empregado) e de empregador (empresa, grupo econômico, equiparação) e 8 questões
- capítulo sobre os sujeitos do contrato: conceito e caracterização de empregado (espécies como doméstico, rural e alto empregado) e de empregador (empresa, grupo econômico, equiparação) e 8 questões, incluindo os poderes do empregador (diretivo, regulamentar, fiscalizador e disciplinar)
- capítulo sobre os sujeitos do contrato: conceito e caracterização de empregado (espécies como doméstico, rural e alto empregado) e de empregador (empresa, grupo econômico, equiparação) e 8 questões e visão das modalidades de contrato de trabalho

### 6. `direito-constitucional.ordem-economica-financeira` — Ordem econômica e financeira na Constituição (arts. 170 a 192)
Arquivo atual: `conteudo/materias/direito-constitucional/ordem-economica-financeira.json` · 29 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o Sistema Financeiro Nacional (art. 192): estrutura, Banco Central, CMN, instituições financeiras e lei complementar, com 6 questões
- seção sobre repressão ao abuso do poder econômico (art. 173, § 4º): dominação de mercados, eliminação da concorrência, aumento arbitrário dos lucros, papel do CADE, com 5 questões
- seção sobre liberdade econômica e a Declaração de Direitos de Liberdade Econômica, ligada à livre iniciativa, com 5 questões
- capítulo sobre a Lei 8.629/1993: conceito de propriedade produtiva, função social rural, requisitos e procedimento da desapropriação para reforma agrária, com 6 questões
- seção sobre a defesa do meio ambiente como princípio da ordem econômica (art. 170, VI) combinada ao art. 225 da CF, com 5 questões
- seção sobre meio ambiente na Constituição (art. 225 e art. 170, VI), com 6 questões
- seção sobre o papel regulador do Estado: pressupostos (falhas de mercado), objetivos, instrumentos e regulação econômica e social, com agências reguladoras, e 5 questões
- seção sobre papel regulador do Estado: pressupostos, objetivos, instrumentos, regulação econômica e social, e 5 questões
- seção sobre agências reguladoras, monopólio estatal e empresas estatais prestadoras de serviços públicos, com 5 questões
- seção sobre proteção ambiental na Constituição (art. 225) além do art. 170, VI, com 6 questões
- capítulos sobre finanças públicas e orçamento (arts. 163 a 169) e ordem social, com questões; hoje só cobre ordem econômica e política urbana, agrícola e fundiária
- seção sobre dispositivos constitucionais de proteção ambiental (art. 225) e direitos dos indígenas (arts. 231 e 232), com 6 questões

### 7. `legislacao.lei-13709-2018-lgpd` — Lei Geral de Proteção de Dados (Lei 13.709/2018)
Arquivo atual: `conteudo/materias/legislacao/lei-13709-2018-lgpd.json` · 27 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a aplicação da LGPD no Distrito Federal (decreto distrital citado no edital: papéis de controlador, encarregado e órgãos distritais) e 4 questões sobre ele
- capítulo sobre o Marco Civil da Internet (princípios, guarda de registros, responsabilidade de provedores, neutralidade) e 8 questões, além da relação com a LGPD
- capítulo sobre aspectos internacionais da LGPD (transferência internacional de dados, hipóteses e papel da ANPD) e 5 questões, hoje excluídos do escopo
- capítulo sobre privacy by design (sete princípios, proatividade, privacidade como padrão, ligação com o art. 46, § 2º) e 5 questões
- capítulo sobre privacidade como direito da personalidade e sigilo telemático/comunicações (relação com o art. 5º da Constituição e o Marco Civil da Internet) e 5 questões
- capítulo sobre compliance e programa de governança em privacidade (boas práticas, art. 50, relatório de impacto, mapeamento de dados) e 5 questões
- capítulo sobre ética e privacidade em ciência de dados (anonimização, pseudonimização, vieses e decisões automatizadas, art. 20) e 5 questões
- capítulo sobre LGPD e responsabilidade civil, com a jurisprudência do STJ sobre vazamento de dados e dano moral, e 6 questões
- capítulo sobre dados de saúde, prontuário eletrônico e sigilo médico à luz da LGPD, e 5 questões em contexto de saúde
- capítulo sobre tratamento de dados pessoais em perícia e benefícios previdenciários (bases legais do poder público e dados de saúde) e 5 questões nesse contexto
- capítulo sobre a LGPD no SUS (dados de saúde, tutela da saúde como base legal, compartilhamento entre entes) e 5 questões
- capítulo sobre privacy by design e privacy by default (os sete princípios, ligação com o art. 46, § 2º, e com o relatório de impacto) e 5 questões

### 8. `administracao-publica.gestao-processos` — Gestão de processos (BPM): mapeamento, análise e melhoria
Arquivo atual: `conteudo/materias/administracao-publica/gestao-processos.json` · 27 tópicos de edital pedem mais conteúdo

Falta:
- subseção sobre ferramentas clássicas de gerenciamento (5W2H, diagrama de causa e efeito, folha de verificação, Pareto, brainstorming) aplicadas a processos e 5 questões
- subseção sobre estatística aplicada ao controle de processos (variabilidade, gráficos de controle) e 6 questões
- subseção sobre melhoria de procedimentos administrativos e legislativos (rotinas de gabinete, tramitação de proposições, padronização de trâmites) e 5 questões
- capítulo sobre tipos de indicadores (eficiência, eficácia, efetividade, produtividade, resultado e impacto) e suas variáveis, com 6 questões
- subseção sobre indicadores de desempenho organizacional (BSC, indicadores estratégicos x operacionais) ligados à gestão por processos e 5 questões
- subseção sobre indicadores de desempenho organizacional (BSC, indicadores estratégicos x operacionais) ligados à gestão baseada em processos e 5 questões
- subseção sobre estatística aplicada ao controle de processos (variabilidade, gráfico de controle, histograma) e 5 questões
- subseção sobre gestão da qualidade (princípios, ferramentas da qualidade, ISO 9001, melhoria contínua) e sua relação com a gestão de processos, com 6 questões
- subseção sobre ciclo de vida da informação e processos produtivos (coleta, armazenamento, uso, descarte) integrada ao desenho de entradas e saídas, e 4 questões
- capítulo sobre inovação na gestão pública (laboratórios, simplificação, desburocratização, melhoria de serviços ao cidadão) e 6 questões
- subseção sobre a norma ISO 9000:2015 (princípios da gestão da qualidade, abordagem de processos, certificação) e 5 questões
- subseção sobre ferramentas de gerenciamento de processos (5W2H, Pareto, Ishikawa, folha de verificação, fluxograma) com exemplos públicos e 5 questões

### 9. `economia.comercio-internacional` — Comércio internacional e economia aberta
Arquivo atual: `conteudo/materias/economia/comercio-internacional.json` · 28 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre globalização e organismos multilaterais (FMI, Banco Mundial, OMC, Bretton Woods, papel de cada um) e 4 questões
- seção sobre bens comercializáveis (tradables) e não comercializáveis (non-tradables), efeito Balassa-Samuelson, câmbio real e inflação, com 4 questões
- capítulo sobre as rodadas do GATT/OMC (Genebra, Kennedy, Tóquio, Uruguai, Doha), o que cada uma negociou, e 4 questões
- seção sobre acordos regionais x multilateralismo e novos temas (serviços, propriedade intelectual, investimentos, meio ambiente, Cingapura) e 4 questões
- seção histórica sobre mobilidade do capital e regimes cambiais (padrão-ouro, Bretton Woods, fim do sistema em 1971, câmbio flutuante pós-1973) e 4 questões
- capítulo sobre o modelo de fatores específicos (Ricardo-Viner): fator móvel x específico, efeitos do comércio sobre a renda de cada fator, e 5 questões
- seção sobre o modelo padrão de economia comercial (curvas de oferta relativa e demanda relativa, termos de troca de equilíbrio, efeito do crescimento) e 4 questões
- seção sobre fluxos financeiros internacionais e mercados de capitais (investimento direto x carteira, mercados de câmbio/eurodólar, globalização financeira) e 4 questões
- capítulo sobre modelo IS-LM-BP (Mundell-Fleming) com política fiscal e monetária sob câmbio fixo e flutuante, e crises cambiais (modelos de primeira e segunda geração), com 6 questões
- seção sobre crises cambiais (ataques especulativos, modelos de 1ª e 2ª geração, contágio) e 4 questões
- seção sobre globalização, blocos regionais e acordos bilaterais e multilaterais de comércio (exemplos concretos: UE, USMCA, RCEP, Mercosul-UE) e 4 questões
- seção sobre cadeias globais de valor/suprimento e comércio em valor adicionado, e 4 questões

### 10. `informatica.nosql-big-data` — Bancos NoSQL e Big Data
Arquivo atual: `conteudo/materias/informatica/nosql-big-data.json` · 24 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre bancos e armazenamento de big data gerenciados em nuvem (serviços de data lake, warehouse e NoSQL gerenciado) e 4 questões
- capítulo sobre engenharia de dados (ingestão em lote e contínua) e sobre SQLite (banco embarcado, arquivo único, uso em apps) com 4 questões
- seção sobre bancos de dados em memória (Redis, Memcached, SAP HANA, persistência x volatilidade, usos) e 4 questões
- capítulo sobre o fluxo ingestão, processamento e disponibilização (ferramentas de ingestão como Sqoop, NiFi, Kafka Connect; camadas bruta/tratada/servida) e 5 questões
- seção sobre bancos de dados em memória (Redis, Memcached, persistência x volatilidade) e 4 questões
- seção comparando modelo relacional, multidimensional (cubos, fatos e dimensões) e NoSQL, com 4 questões
- seção sobre armazenamento de objetos (object stores, buckets, diferença para sistema de arquivos distribuído) e 3 questões
- seção sobre pipeline de dados (etapas, orquestração com Airflow/Oozie, ETL x ELT) e 4 questões
- seção sobre ETL e ELT, pipelines e camadas de data lake, com 5 questões
- seção sobre pipelines de dados (ingestão, transformação, orquestração) e 4 questões
- seção sobre ETL e ELT (diferenças, quando usar cada um) e 5 questões
- seção sobre bancos em memória e sobre propriedades de SGBDs (atomicidade, durabilidade, desempenho) e 3 questões

### 11. `historia.brasil-republica` — História do Brasil República
Arquivo atual: `conteudo/materias/historia/brasil-republica.json` · 30 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre estratificação social da Primeira República (elite cafeeira, imigração, urbanização, operariado, classes médias) e 5 questões
- capítulo sobre o Brasil na Primeira Guerra Mundial (neutralidade, entrada em 1917, efeitos econômicos e sociais) e 5 questões
- contexto da Guerra Fria na República de 1945 a 1991 (alinhamento aos EUA, doutrina de segurança nacional, anticomunismo) e 6 questões
- capítulo sobre o Brasil na Grande Guerra (neutralidade, torpedeamentos, declaração de guerra em 1917, participação naval, efeitos internos) e 5 questões
- capítulo sobre o Modernismo (Semana de Arte Moderna de 1922, fases, autores, relação com a sociedade) e 5 questões
- capítulo sobre sociedade e cultura na Era Vargas (educação, rádio, nacionalismo cultural, Ministério da Educação, intelectuais) e 5 questões
- capítulo sobre industrialização e urbanização de 1945 a 1964 (êxodo rural, crescimento das cidades, indústria automobilística) e 5 questões
- capítulo sobre sociedade e cultura de 1945 a 1964 (cinema, rádio e TV, Bossa Nova, movimentos sociais, ligas camponesas) e 5 questões
- capítulo sobre sociedade e cultura no regime militar (movimento estudantil, censura, canção de protesto, Tropicália, cinema) e 5 questões
- partidos e eleições na Nova República (pluripartidarismo, PT, PSDB, PMDB, eleições presidenciais desde 1989) e 6 questões
- capítulo sobre impactos da globalização no Brasil (abertura comercial, privatizações, reformas dos anos 1990) e 5 questões
- capítulo sobre mudanças sociais na redemocratização (movimentos sociais, direitos civis, desigualdade, programas sociais) e 5 questões

### 12. `administracao-publica.lei-acesso-informacao` — Lei de Acesso à Informação
Arquivo atual: `conteudo/materias/administracao-publica/lei-acesso-informacao.json` · 12 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a relação entre a LAI e a LGPD (Lei 13.709/2018): informação pessoal x dado pessoal, bases legais de tratamento, direitos do titular e papel da ANPD, com 6 questões
- capítulo sobre o Decreto nº 7.845/2012 (credenciamento de segurança, tratamento de informação classificada, Núcleo de Segurança e Credenciamento), com 6 questões
- capítulo sobre a Lei 13.709/2018 (princípios, bases legais, direitos do titular, ANPD) em articulação com a LAI, com 6 questões
- capítulo sobre a Lei da Transparência fiscal (LC 131/2009 e portais de transparência da execução orçamentária e financeira) em comparação com a LAI, com 5 questões
- seção sobre governo aberto: Open Government Partnership, Declaração de Governo Aberto e seus pilares (transparência, participação, prestação de contas, tecnologia), com 5 questões
- capítulo sobre proteção de dados pessoais (LGPD: princípios, bases legais, direitos do titular) e sua relação com a classificação de informações, com 6 questões
- seção sobre a regulamentação estadual da LAI (decreto do Estado do RS e competência dos entes para regulamentar) e 5 questões
- seção sobre a regulamentação estadual da LAI aplicável à Polícia Militar de São Paulo (decreto estadual, SIC estadual, recursos) e 5 questões

### 13. `criminalistica.fundamentos-pericia` — Criminalística: fundamentos da perícia
Arquivo atual: `conteudo/materias/criminalistica/fundamentos-pericia.json` · 16 tópicos de edital pedem mais conteúdo

Falta:
- divisão da cadeia de custódia em fase externa (reconhecimento a transporte) e fase interna (recepção a descarte), com 4 questões; as dez etapas do art. 158-B já estão no texto
- levantamento de local de crime contra a pessoa x contra o patrimônio: finalidades, vestígios típicos (arrombamento, ferramentas, impressões papiloscópicas) e roteiro de cada tipo, com 5 questões
- microvestígios: conceito, exemplos (fibras, pelos, resíduos, vidro, tinta), coleta por fita e aspiração, cuidados contra contaminação, com 4 questões
- áreas da Criminalística moderna e exames (papiloscopia, biologia/DNA, química e toxicologia, engenharia legal, informática forense, contabilidade forense, ambiental, audiovisual), com 6 questões
- locais de crime contra a vida (homicídio, suicídio, acidente): vestígios, diferenciação e levantamento, com 6 questões
- locais de morte: classificação (morte violenta, suspeita), vestígios e relação entre perito criminal e médico-legista, com 6 questões
- locais de morte violenta: homicídio, suicídio e acidente, sinais indicativos, com 5 questões
- local de morte por arma de fogo: estojos, projéteis, trajetória, posição do corpo e distância do tiro (ligação com balística), com 5 questões
- local de morte por instrumentos contundentes, cortantes, perfurantes ou mistos: vestígios, padrões de manchas de sangue, instrumento, com 5 questões
- local de morte por asfixia (enforcamento, estrangulamento, sufocação, afogamento): vestígios no local e no cadáver, diferenciação entre suicídio e homicídio, com 5 questões
- histórico da Criminalística (Hans Gross, Bertillon, Locard, Vucetich, Reiss; origem no Brasil), com 4 questões; hoje só se cita Hans Gross
- doutrina da Criminalística: finalidades, divisões (geral e especial), princípios e métodos, com 4 questões

### 14. `administracao-publica.nocoes-arquivologia` — Noções de Arquivologia
Arquivo atual: `conteudo/materias/administracao-publica/nocoes-arquivologia.json` · 20 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre acondicionamento e armazenamento: mobiliário, caixas, condições ambientais e segurança, com 4 questões
- capítulo sobre acondicionamento e armazenamento físico de documentos de arquivo (mobiliário, caixas, ambiente, riscos) e 4 questões
- trecho sobre acondicionamento, armazenamento e conservação física (temperatura, umidade, pragas, mobiliário) e 4 questões
- capítulo sobre tipologias documentais e suportes físicos (papel, filme, magnético, digital; gêneros, espécies e tipos) com 5 questões
- conceitos de processo, pasta, fichário e prontuário, e diferença entre documento e informação, com 4 questões
- capítulo sobre métodos de arquivamento (alfabético, numérico simples e cronológico, geográfico, ideográfico) com exemplos e 6 questões
- capítulo sobre sistemas de arquivamento (direto, indireto, semi-indireto), mobiliário e fichários, com 4 questões
- capítulo sobre a legislação arquivística: Lei 8.159/1991 (política nacional de arquivos), Conarq e arquivos públicos e privados, com 5 questões
- capítulo sobre descrição arquivística e instrumentos de pesquisa (guia, inventário, catálogo, índice) e 4 questões
- trecho sobre restauração de documentos (técnicas e cuidados), além da preservação, com 3 questões
- trecho sobre histórico e função dos arquivos e classificação dos arquivos (públicos e privados; setoriais, centrais, especializados) com 4 questões
- capítulo sobre gêneros (textual, iconográfico, sonoro), espécies e tipos documentais, com 4 questões

### 15. `matematica-financeira.rendas-series-pagamentos` — Rendas, séries uniformes e pagamentos
Arquivo atual: `conteudo/materias/matematica-financeira/rendas-series-pagamentos.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- rendas variáveis (fluxos de caixa não uniformes, gradientes e valor presente de fluxos irregulares) com 8 questões
- séries não uniformes (fluxos variáveis) com cálculo de valor presente e futuro, 6 questões
- equivalência com fluxos irregulares, data focal e taxa de retorno (TIR) com 8 questões
- equivalência de capitais com pagamentos únicos (data focal, desconto composto) e comparação de planos de pagamento, 8 questões
- equivalência de capitais com pagamentos únicos (data focal, desconto composto) e comparação de planos, 8 questões
- rendas variáveis e comparação dos sistemas Price, SAC e SAM (tabelas de amortização), com 8 questões; a matéria exclui amortização

### 16. `direito-tributario.conceito-especies-tributos` — Conceito e espécies de tributos
Arquivo atual: `conteudo/materias/direito-tributario/conceito-especies-tributos.json` · 27 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre taxas contratuais e facultativas (taxa x preço público, facultatividade do serviço, jurisprudência) e 3 questões
- seção sobre contribuições parafiscais (conceito de parafiscalidade, sujeito ativo delegado, destinação a entidades paraestatais) e 3 questões
- seção sobre competência tributária das espécies e imunidades (noções e relação com as espécies) e 4 questões
- seção sobre a CIDE (art. 149 e 177 §4º CF: finalidade, destinação, exemplos como CIDE-combustíveis e CIDE-remessas) e 4 questões
- seção sobre contribuições sociais gerais, de seguridade, CIDE e de categorias profissionais, além da COSIP (art. 149-A, já parcial), e 5 questões
- capítulo sobre legislação tributária no CTN (fontes, vigência, aplicação, interpretação e integração, arts. 96 a 112) e 6 questões
- seção sobre classificação dos tributos (vinculados e não vinculados, diretos e indiretos, fiscais e extrafiscais) e limitações da competência tributária, com 5 questões
- seção sobre a norma tributária e a classificação dos impostos (diretos e indiretos, reais e pessoais, fiscais e extrafiscais) e 4 questões
- seção sobre impostos diretos e indiretos (critério do repasse econômico, contribuinte de direito e de fato, art. 166 CTN) e 4 questões
- seção sobre impostos reais e pessoais (conceito, exemplos, relação com capacidade contributiva) e 3 questões
- seção sobre competência tributária e capacidade tributária ativa (distinção, indelegabilidade, delegação de arrecadação) e 4 questões
- seção sobre competência tributária e capacidade tributária ativa (distinção, delegação a autarquias e paraestatais) e 4 questões

### 17. `direito-tributario.limitacoes-poder-tributar` — Limitações ao poder de tributar
Arquivo atual: `conteudo/materias/direito-tributario/limitacoes-poder-tributar.json` · 18 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre imunidades específicas fora do art. 150, VI: ITR em pequenas glebas, exportações em IPI e ICMS, ITBI na integralização de capital e reorganização societária, entidades beneficentes nas contribuições (art. 195, § 7º), e 8 questões.
- seção sobre as limitações do CTN, arts. 9º a 15 (disposições gerais e especiais): vedações do art. 9º, art. 11 (procedência ou destino), alcance da imunidade nos arts. 12 e 13, e 5 questões.
- seção sobre tipicidade tributária (tipicidade fechada) e sua relação com a legalidade, com 3 questões.
- nota que distinga a antiga anualidade (orçamentária) da anterioridade tributária atual, com 3 questões.
- seção sobre segurança jurídica e proteção da confiança no direito tributário (irretroatividade, anterioridade, modulação, boa-fé do contribuinte, art. 146 do CTN) e 4 questões.
- seção sobre os princípios que faltam: generalidade, seletividade, não cumulatividade, praticidade e proteção da confiança, com 8 questões.
- capítulo sobre imunidades específicas fora do art. 150, VI (ITR, exportações, ITBI, contribuições sociais) e 6 questões; a distinção imunidade x isenção x não incidência já consta.
- seção aprofundada sobre capacidade contributiva: pessoalidade, graduação, mínimo existencial, progressividade e entendimento do STF, e 5 questões.
- capítulo sobre imunidades específicas (ITR, exportações em IPI e ICMS, ITBI, contribuições sociais, taxas e certidões) e 6 questões.
- seção sobre imunidades previstas fora do art. 150, VI (entidades beneficentes nas contribuições, exportações, ITR, ITBI) e 6 questões.
- capítulo sobre imunidades em espécie fora do art. 150, VI e sobre classificações de imunidade, com 6 questões.
- seção sobre reserva absoluta e reserva relativa de lei, delegações legislativas e atos infralegais em matéria tributária, com 4 questões.

### 18. `processo-trabalho.partes-procuradores` — Partes, procuradores, jus postulandi e honorários
Arquivo atual: `conteudo/materias/processo-trabalho/partes-procuradores.json` · 15 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre assistência judiciária no processo do trabalho (assistência sindical da Lei 5.584/1970, requisitos e extensão a não associados, diferença para a gratuidade da justiça) e 6 questões
- litisconsórcio no processo do trabalho (espécies, litisconsórcio necessário e facultativo, efeitos) e intervenção de terceiros, com 6 questões
- honorários contratuais (contratados) e sua distinção dos sucumbenciais, inclusive honorários periciais e de assistência sindical, com 5 questões
- capítulo sobre assistência judiciária no processo do trabalho (assistência sindical da Lei 5.584/1970, requisitos e extensão a não associados, diferença para a gratuidade da justiça) e 6 questões; além de honorários contratuais (contratados) e sua distinção dos sucumbenciais, inclusive honorários periciais e de assistência sindical, com 5 questões
- representação da massa falida e das empresas em recuperação judicial e litisconsórcio no processo do trabalho, com 6 questões
