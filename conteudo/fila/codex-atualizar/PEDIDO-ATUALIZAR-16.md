# Atualizar matérias — pedido 16 de 18 (09/10/2026)

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

### 1. `saude-publica.epidemiologia-vigilancia` — Epidemiologia e vigilância em saúde
Arquivo atual: `conteudo/materias/saude-publica/epidemiologia-vigilancia.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- Vigilância sanitária: conceito legal (Lei 8.080/1990), áreas de abrangência, SNVS, Anvisa e poder de polícia sanitária, funções e ações, com questões.
- Epidemiologia de doenças transmissíveis e não transmissíveis no Brasil (perfil de morbimortalidade, transição epidemiológica, principais agravos) com questões.
- Programa Nacional de Imunizações: calendário, tipos de vacinas, eventos adversos, coberturas e Lei 6.259/1975, com questões.
- Epidemiologia da COVID-19, síndrome pós-COVID e impactos na saúde pública, com questões.
- Vigilância sanitária (Lei 9.782/1999, SNVS, poder de polícia sanitária) e a diferença para vigilância epidemiológica, com questões no nível de juiz federal.

### 2. `minas-gerais.controle-cheias` — Controle de Cheias
Arquivo atual: `conteudo/materias/minas-gerais/controle-cheias.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- Obras de defesa contra inundação e macrodrenagem em nível de engenharia: reservatórios de cheias, bacias de acumulação, alargamento de calhas, canalização de cursos d'água e reflorestamento da bacia, com critérios de escolha e questões.
- Reservatórios de controle de cheias (amortecimento de picos, bacias de detenção e retenção, operação) em nível de engenharia, com questões.
- Previsão de cheias, modelos de alerta e regularização de vazões, que o escopo exclui, com questões de nível de engenharia.
- Macrodrenagem em nível de engenharia: bacias de detenção e retenção, canalização de cursos d'água e reservatórios de cheias, com questões.
- Classificação das medidas de controle de cheias em intensivas e extensivas, além de estruturais e não estruturais, com exemplos e questões.

### 3. `matematica.graficos-tabelas-mapas-escalas` — Leitura de gráficos, tabelas, mapas e escalas
Arquivo atual: `conteudo/materias/matematica/graficos-tabelas-mapas-escalas.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre relação entre grandezas (direta e inversamente proporcionais) lida em tabelas e gráficos, com 5 questões
- seção sobre problemas aritméticos, geométricos e matriciais que partem de gráficos e tabelas, com 6 questões
- seção sobre relação entre grandezas proporcionais em tabelas e gráficos (função linear, taxa de variação), com 5 questões
- seção sobre figuras planas e espaciais, projeções, planificações e cortes, com 8 questões

### 4. `processo-trabalho.execucao` — Execução trabalhista: espécies e Fazenda Pública
Arquivo atual: `conteudo/materias/processo-trabalho/execucao.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre incidente de desconsideração da personalidade jurídica na execução trabalhista (cabimento, procedimento, suspensão) e 5 questões
- capítulos sobre liquidação de sentença (cálculos, artigos, arbitramento) e embargos do executado / impugnação do exequente (prazo, garantia do juízo, matéria), com 8 questões
- seção sobre prerrogativas processuais da Fazenda Pública no processo do trabalho (prazos, remessa necessária, dispensa de depósito recursal) e 5 questões
- seção sobre princípios da execução (execução menos gravosa, patrimonialidade, utilidade, privilégio do crédito trabalhista, indicação de bens) e 6 questões

### 5. `matematica.geometria-analitica` — Geometria analítica
Arquivo atual: `conteudo/materias/matematica/geometria-analitica.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- equações reduzidas e elementos da elipse, hipérbole e parábola (focos, eixos, excentricidade, vértice e diretriz) com exemplos numéricos e cerca de 8 questões; as cônicas hoje têm só definição focal
- distância de ponto à reta (fórmula), ângulo entre duas retas, área de triângulo por determinante e retas concorrentes com cálculo de interseção, com cerca de 6 questões
- coordenadas no espaço e distância entre pontos no espaço (a matéria exclui o 3D), fórmula da distância de ponto à reta e inequações a duas incógnitas como regiões do plano, com questões de nível de professor
- equação segmentária da reta (x/p + y/q = 1) e conversões entre as formas geral, reduzida e segmentária com exemplos e cerca de 4 questões
- fórmula da distância de ponto à reta |ax0+by0+c|/√(a²+b²), distância entre retas paralelas e aplicações a tangência, com exemplos resolvidos e cerca de 6 questões

### 6. `direito-trabalho.teletrabalho-intermitente` — Teletrabalho e trabalho intermitente
Arquivo atual: `conteudo/materias/direito-trabalho/teletrabalho-intermitente.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- turnos ininterruptos de revezamento (jornada de 6 horas, negociação coletiva, intervalos) com 8 questões; ver direito-trabalho.jornada-horas-extras
- novas modalidades de contratação (trabalho temporário, tempo parcial, terceirização, autônomo exclusivo) com 8 questões
- teletrabalho e saúde do trabalhador: ergonomia, doenças ocupacionais, controle de riscos e perícia médica, com 6 questões
- terceirização, trabalho autônomo e outras formas de contratação, com 8 questões

### 7. `ciencias.tabela-periodica` — Tabela periódica e propriedades periódicas
Arquivo atual: `conteudo/materias/ciencias/tabela-periodica.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- histórico e evolução da tabela (Döbereiner, Newlands, Mendeleev, Moseley) com 6 questões
- propriedades aperiódicas (massa atômica, calor específico, índice de refração) em contraste com as periódicas, com 5 questões
- Lei de Moseley e histórico da tabela periódica, com 6 questões

### 8. `legislacao.crime-cibernetico-convencao` — Convenção sobre o Crime Cibernético (Convenção de Budapeste)
Arquivo atual: `conteudo/materias/legislacao/crime-cibernetico-convencao.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- Aprofundar a Convenção por artigos: condutas dos arts. 2 a 10 (acesso ilegal, interceptação, interferência em dados e sistemas, uso indevido de dispositivos, fraude, pornografia infantil, direitos autorais), arts. 14 a 21 e cooperação dos arts. 23 a 35, mais reservas do Brasil e 12 questões de nível de prova de juiz.
- Capítulo sobre provas digitais: cadeia de custódia, preservação e integridade de evidência eletrônica, requisição de dados a provedores, acesso a dados armazenados e validade da prova, com questões.
- Aprofundar o Decreto 11.491/2023 por artigos (condutas dos arts. 2 a 10, medidas processuais dos arts. 16 a 21, cooperação e ponto de contato 24/7) com 10 questões de nível de delegado.
- Aprofundar as medidas processuais e a cooperação internacional da Convenção por artigos (preservação, ordem de apresentação, busca e apreensão, interceptação, extradição, auxílio mútuo) com questões de nível de delegado de PF.

### 9. `portugues.argumentacao-adequacao-textual` — Argumentação, pertinência e adequação textual
Arquivo atual: `conteudo/materias/portugues/argumentacao-adequacao-textual.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- Defeitos de conteúdo do texto (descontextualização, simplismo, obviedade, paráfrase, cópia, tautologia, contradição) com exemplos e questões; a matéria só toca generalização.
- Capítulo sobre avaliação de argumentos: o que reforça ou enfraquece uma conclusão (premissas, dados, contra-exemplos), com questões no estilo raciocínio crítico.
- Tipos de argumento (autoridade, exemplificação, causa e consequência, comparação, raciocínio lógico, princípio) com exemplos e questões.
- Estratégias retóricas e persuasão em anúncios publicitários (apelo ao consumo), com textos e questões.
- Operadores argumentativos, modalizadores, intensificadores e índices de avaliação, referenciação e sequenciação, com questões.

### 10. `ciencias.cinetica-quimica` — Cinética química
Arquivo atual: `conteudo/materias/ciencias/cinetica-quimica.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- Mecanismos de reação: etapas elementares, etapa lenta determinante da velocidade e molecularidade, com questões.
- Aplicação da equação de Arrhenius a estudos de estabilidade de medicamentos (testes acelerados, extrapolação de prazo de validade) com questões.
- Teoria do estado de transição e efeitos de solvente e de pH sobre a velocidade, com questões.
- Cinética de decomposição de fármacos: leis integradas de ordem zero, primeira, segunda e pseudo-primeira ordem, tempo de meia-vida e t90, com questões.

### 11. `ciencias.termoquimica` — Termoquímica
Arquivo atual: `conteudo/materias/ciencias/termoquimica.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Entropia, espontaneidade e energia livre de Gibbs (ΔG = ΔH - TΔS), com exemplos e questões.
- Calor de reação a pressão constante e a volume constante (ΔH e ΔU, relação com trabalho de expansão), com questões.
- Termodinâmica química: primeira e segunda leis, energia interna, entropia e energia livre, com questões.

### 12. `legislacao.lei-6858-1980` — Lei 6.858/1980: valores não recebidos em vida
Arquivo atual: `conteudo/materias/legislacao/lei-6858-1980.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Alvarás judiciais em geral (jurisdição voluntária, alvará para venda ou oneração de bens de incapazes, levantamento de valores, autorização de viagem) além dos valores da Lei 6.858/1980, com questões.
- Lei 5.584/1970: assistência judiciária sindical, honorários, normas processuais trabalhistas (alçada, rito sumário, ônus da prova) com questões; a matéria trata só da Lei 6.858/1980.

### 13. `administracao-publica.governo-digital` — Governo digital e Lei 14.129/2021
Arquivo atual: `conteudo/materias/administracao-publica/governo-digital.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre a Lei 13.460/2017 (direitos do usuário de serviços públicos, carta de serviços, ouvidoria, conselhos de usuários) e sua relação com o governo digital, com 6 questões
- seção sobre o Programa de Gestão e Desempenho (PGD) e teletrabalho no governo digital, com 4 questões
- seção sobre inovação tecnológica no setor público (laboratórios de inovação, sandbox regulatório, contratações públicas de inovação) e 5 questões
- seção sobre controle social, cidadania, accountability e resultados em serviços públicos digitais, com 6 questões

### 14. `matematica.equivalencias-negacoes-logicas` — Equivalências e negações lógicas
Arquivo atual: `conteudo/materias/matematica/equivalencias-negacoes-logicas.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- implicação lógica (p ⇒ q como condicional tautológica), diferença entre implicação e equivalência e regras de inferência básicas (modus ponens, modus tollens), com cerca de 6 questões; a contrapositiva já está coberta
- tautologia, contradição e contingência (definição, verificação por tabela-verdade e exemplos como p ∨ ¬p e p ∧ ¬p) com cerca de 6 questões; as leis de De Morgan já estão cobertas
- implicação lógica (p ⇒ q como condicional tautológica), diferença entre implicação e equivalência e regras como modus ponens e modus tollens, com cerca de 6 questões

### 15. `administracao-publica.regulacao-economica` — Regulação econômica
Arquivo atual: `conteudo/materias/administracao-publica/regulacao-economica.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- agente-principal, escolha pública (public choice) e custos de transação, com 8 questões
- fundamento constitucional da administração regulatória (Estado como agente normativo e regulador) com 6 questões
- abuso regulatório e liberdade econômica (vedação a normas que criem reserva de mercado, barreiras e custos desnecessários), com 6 questões
- fomento, planejamento, serviço público e poder de polícia como formas de atuação do Estado, comparados à regulação, com 8 questões

### 16. `ciencias.oscilacoes-simples-amortecidas-forcadas` — Oscilações simples, amortecidas e forçadas
Arquivo atual: `conteudo/materias/ciencias/oscilacoes-simples-amortecidas-forcadas.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- efeito Doppler (fontes e observadores em movimento, fórmula e casos limites) com 6 questões
- vibrações mecânicas em nível de engenharia: sistema massa-mola-amortecedor, graus de liberdade, frequências naturais e transmissibilidade, com 10 questões
- mecânica dos fluidos em nível de engenharia (propriedades, viscosidade, Bernoulli, perdas de carga) com 10 questões
- hidrostática e hidrodinâmica em nível de engenharia (Bernoulli, continuidade, escoamento) com 10 questões

### 17. `ciencias.atomos-moleculas-ions` — Átomos, moléculas e íons
Arquivo atual: `conteudo/materias/ciencias/atomos-moleculas-ions.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- modelo de Bohr para o átomo de hidrogênio: níveis de energia, transições e séries espectrais, com 6 questões
- orbitais e distribuição eletrônica (diagrama de Pauling, números quânticos) com 8 questões; a configuração eletrônica está em ciencias.tabela-periodica
- modelo quântico: orbitais, números quânticos e princípio da incerteza, com 8 questões

### 18. `processo-trabalho.dissidio-coletivo` — Dissídios coletivos e sentença normativa
Arquivo atual: `conteudo/materias/processo-trabalho/dissidio-coletivo.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- efeito suspensivo do recurso ordinário contra sentença normativa (concessão pelo presidente do TST) com 5 questões
- efeito suspensivo do recurso ordinário contra sentença normativa, com 5 questões
- efeito suspensivo do recurso ordinário contra sentença normativa, com 6 questões
