# Atualizar matérias — pedido 12 de 18 (09/10/2026)

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

### 1. `informatica.power-bi-visualizacao` — Power BI e visualização de dados
Arquivo atual: `conteudo/materias/informatica/power-bi-visualizacao.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- princípios de visualização de dados (razão dados/tinta, atributos pré-atentivos, uso de cor, Gestalt, evitar distorções e gráficos 3D) com exemplos de bons e maus gráficos e cerca de 6 questões; hoje só há orientações de escolha de visual
- histograma, box plot (quartis, outliers) e gráfico de dispersão em profundidade, com interpretação e cerca de 8 questões; a matéria só explica barras, linhas, dispersão em parte, pizza e cartão
- Tableau e Looker/Data Studio (visão geral, recursos e comparação com Power BI) e cerca de 5 questões; a matéria cobre só o Power BI
- storytelling com dados (narrativa, contexto, foco, destaque do ponto principal, sequência de visuais) com exemplos e cerca de 5 questões
- análise exploratória de dados: estatísticas descritivas, detecção de outliers, distribuições, correlação e uso de histogramas e box plots para explorar dados, com cerca de 8 questões
- Qlik (Qlik Sense/QlikView): modelo associativo, painéis e comparação com Power BI, com cerca de 5 questões
- Tableau (objetos, campos, dashboards) e análise exploratória de dados com histogramas, box plots e outliers, com cerca de 8 questões
- visão geral das ferramentas de apoio à análise de dados (Excel, Power BI, Tableau, Python, R, SQL) e quando usar cada uma, com cerca de 5 questões

### 2. `processo-trabalho.ministerio-publico-trabalho` — Ministério Público do Trabalho no processo
Arquivo atual: `conteudo/materias/processo-trabalho/ministerio-publico-trabalho.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- organização do MPT na LC 75/1993: Procurador-Geral do Trabalho, Colégio de Procuradores, Conselho Superior, Câmara de Coordenação e Revisão, Corregedoria, procuradorias regionais, carreira e ingresso, com 10 questões
- organização do MPT na LC 75/1993: órgãos (PGT, Colégio, Conselho Superior, Câmara de Coordenação, Corregedoria), procuradorias regionais e carreira, com 10 questões
- organização e competência: órgãos do MPT e atribuições por grau de jurisdição, com 10 questões
- órgãos e carreira do MPT e prerrogativas institucionais e funcionais (garantias, vedações, prerrogativas processuais), com 10 questões
- notificação recomendatória: finalidade, forma, efeitos e relação com inquérito civil e TAC, com 5 questões
- organização do MPT (órgãos e carreira) e competência por grau de jurisdição, com 10 questões

### 3. `legislacao.lei-migracao` — Lei de Migração: direitos, vistos, residência e retirada compulsória
Arquivo atual: `conteudo/materias/legislacao/lei-migracao.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre extradição na Lei 13.445/2017 (conceito, espécies ativa e passiva, condições e vedações, procedimento no STF, prisão cautelar) e 8 questões
- capítulo sobre asilo político (territorial e diplomático) e refúgio (Lei 9.474/1997: conceito, procedimento, Conare, cessação) e 8 questões
- capítulo sobre refugiados e apátridas (Lei 9.474/1997 e estatuto da apatridia) e panorama das migrações internacionais, com 6 questões
- órgãos da política migratória, como o Conselho Nacional de Imigração, e seu papel, com 4 questões
- tratados internacionais sobre migração e direitos dos migrantes (convenção da ONU sobre trabalhadores migrantes, pacto global) em relação às normas nacionais, com 6 questões

### 4. `direito-constitucional.direitos-sociais` — Direitos sociais (arts. 6º a 11)
Arquivo atual: `conteudo/materias/direito-constitucional/direitos-sociais.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre contribuição sindical (fim da obrigatoriedade, contribuição confederativa e assistencial e entendimento do STF) e 5 questões
- capítulo sobre a saúde na teoria dos direitos sociais (art. 196, universalidade, judicialização, reserva do possível aplicada à saúde) e 6 questões
- capítulo sobre o direito à saúde (art. 196 da CF, dever do Estado, judicialização de medicamentos e tratamentos) e o SUS no texto constitucional, com 8 questões
- capítulo sobre os direitos sociais em espécie (saúde, educação, previdência, assistência, moradia, alimentação, lazer, transporte) com jurisprudência do STF e 8 questões
- seção sobre conceito e classificação dos direitos sociais (dimensão ou geração, natureza prestacional, eficácia e aplicabilidade) e 6 questões
- capítulo sobre os direitos dos trabalhadores individuais (art. 7º, inciso a inciso) e coletivos (arts. 8º a 11), com 10 questões em nível de magistratura
- capítulo sobre fundamentos constitucionais, ordem social, distinção entre direitos sociais e econômicos e classificação dos direitos sociais, com 8 questões
- capítulo sobre direitos sociais em espécie (alimentação, moradia, saúde, educação, trabalho) e a ideia de direito ao futuro, com jurisprudência e 8 questões
- capítulo sobre os direitos constitucionais dos trabalhadores (art. 7º, inciso a inciso) em nível de magistratura, com 10 questões
- seção sobre direitos sociais, econômicos, culturais e ambientais (DESCA), sua previsão no PIDESC e na Constituição e a proibição de retrocesso, com 6 questões
- seção sobre a dignidade da pessoa humana e os valores sociais do trabalho como fundamentos (arts. 1º e 170) e a proteção constitucional ao trabalho, com 5 questões
- seção sobre contribuição sindical (fim da obrigatoriedade, contribuição confederativa e assistencial, entendimento do STF) e autonomia das entidades sindicais, com 6 questões

### 5. `direito.constituicao-federal-1988-direitos-fundamentais` — Constituição Federal de 1988 (Direitos Fundamentais)
Arquivo atual: `conteudo/materias/direito/constituicao-federal-1988-direitos-fundamentais.json` · 10 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre direitos sociais (arts. 6º a 11) além do art. 5º, com 8 questões
- aprofundamento em nível de magistratura sobre direitos humanos na Constituição (cláusula de abertura, tratados com status constitucional e supralegal), com 8 questões
- capítulo sobre direitos sociais (arts. 6º a 11) e 8 questões
- capítulo sobre princípios fundamentais (arts. 1º a 4º: fundamentos, objetivos, relações internacionais) e 6 questões
- seção sobre sistemas de proteção dos direitos humanos (global e regional) em relação à Constituição, com 6 questões
- seção sobre dignidade da pessoa humana e direitos humanos na segurança pública (uso da força, direitos do preso), com 6 questões
- seção sobre o art. 5º, §§ 2º e 3º e o status dos tratados internacionais de direitos humanos, com 6 questões
- capítulos sobre princípios fundamentais e organização do Estado (União, Estados, Municípios, repartição de competências), com 12 questões
- seção sobre incorporação dos tratados de direitos humanos e controles de constitucionalidade e de convencionalidade, com 8 questões
- seção sobre servidores públicos civis (arts. 39 a 41: regime, estabilidade, acumulação, remuneração) além de militares estaduais, com 8 questões

### 6. `matematica.regra-de-tres` — Regra de três simples e composta
Arquivo atual: `conteudo/materias/matematica/regra-de-tres.json` · 11 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre porcentagens ligadas à regra de três, com 6 questões
- seção sobre porcentagens ligadas à proporcionalidade, com 6 questões
- seção sobre conversão entre medidas de comprimento, área, volume, massa e tempo, com 8 questões
- capítulo sobre razões e proporções (propriedades, grandezas) além da regra de três, com 8 questões
- capítulo sobre razão, proporção e variação de grandezas, com 8 questões
- capítulo sobre divisão proporcional e regra de sociedade, com 8 questões
- capítulo sobre divisão proporcional (direta, inversa, composta), com 8 questões
- seções sobre porcentagem, escalas e frações, com 8 questões

### 7. `estatistica.series-temporais-numeros-indices` — Séries temporais e números-índice
Arquivo atual: `conteudo/materias/estatistica/series-temporais-numeros-indices.json` · 10 tópicos de edital pedem mais conteúdo

Falta:
- autocorrelação (ACF/PACF) e modelos ARIMA (ordens p, d, q, identificação e diferenciação) com cerca de 8 questões; tendência e sazonalidade já estão cobertas
- modelos ARIMA e predição (previsão com médias móveis, suavização exponencial, intervalos de previsão) com questões; componentes e médias móveis já estão cobertos
- predição em séries temporais: alisamento exponencial simples, tendência linear para previsão, erro de previsão (MAE, RMSE) e questões; a matéria avisa que média móvel não é previsão
- capítulo sobre estacionariedade (média e variância constantes, estacionariedade fraca, testes de raiz unitária, diferenciação) com cerca de 6 questões
- propriedades ideais de um número-índice: identidade, reversão no tempo, reversão dos fatores, circularidade e índice ideal de Fisher, com exemplos de teste e cerca de 6 questões
- estacionariedade, modelos ARMA, ARIMA e SARIMA e análise espectral (periodograma) com questões; só a análise descritiva e as médias móveis estão cobertas
- suavização exponencial (simples, Holt, Holt-Winters) e modelos ARIMA com questões; tendência e médias móveis já estão cobertas
- estacionariedade (definição, testes e transformações) com questões; sazonalidade e tendência já estão cobertas
- índice de Preço ao Consumidor (cesta, ponderação, IPCA/INPC), índice de base fixa x encadeado e mais exemplos de índices ponderados, com questões
- alisamento exponencial, séries estacionárias, função de autocovariância e autocorrelação e modelos ARMA, ARIMA e SARIMA, com cerca de 12 questões; conceito, tendência e sazonalidade já estão cobertos

### 8. `geografia.clima-hidrografia-vegetacao` — Clima, hidrografia e vegetação
Arquivo atual: `conteudo/materias/geografia/clima-hidrografia-vegetacao.json` · 10 tópicos de edital pedem mais conteúdo

Falta:
- rede hidrográfica do Pará (bacia Amazônica, Tocantins-Araguaia, rios Xingu, Tapajós, Jari), com 8 questões
- bacia Amazônica e principais rios do Acre (Juruá, Purus, Acre), com 8 questões
- estrutura geológica, relevo e solos do Brasil, com 12 questões; ver geografia.geologia-relevo-solos
- relevo, geologia e solos do espaço natural brasileiro, com 12 questões
- circulação geral da atmosfera em nível de meteorologia (células de Hadley, Ferrel e polar, Coriolis, ZCIT, jatos), com 10 questões
- relevo brasileiro (planaltos, planícies, depressões, classificações) com 10 questões
- relevo brasileiro (planaltos, planícies, depressões) com 10 questões
- geologia, relevo, solos e águas oceânicas, com 12 questões
- relevo, domínios morfoclimáticos, pedologia e águas oceânicas, com 12 questões
- relevo e paisagens, com 10 questões

### 9. `direito.pacto-internacional-direitos-economicos-sociais` — Pacto Internacional dos Direitos Econômicos, Sociais e Culturais (PIDESC)
Arquivo atual: `conteudo/materias/direito/pacto-internacional-direitos-economicos-sociais.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- Protocolo Facultativo ao PIDESC (comunicações individuais) e demais protocolos, que o escopo exclui, com questões.
- Justiciabilidade dos direitos econômicos, sociais, culturais e ambientais: realização progressiva, reserva do possível, vedação do retrocesso e jurisprudência, com questões.
- Direitos econômicos, sociais, culturais e ambientais além do texto do Pacto (direito ao meio ambiente, Protocolo de San Salvador, comentários gerais), com questões.
- Direitos a moradia, água e meio ambiente e comentários gerais do Comitê sobre esses direitos, com questões; a matéria cobre trabalho, saúde, alimentação, educação e cultura.

### 10. `direito-tributario.responsabilidade-tributaria` — Responsabilidade tributária
Arquivo atual: `conteudo/materias/direito-tributario/responsabilidade-tributaria.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- seção própria sobre substituição tributária: para trás (diferimento) e para frente (art. 150, § 7º, da CF, base de cálculo presumida, restituição quando o fato gerador não ocorre ou ocorre em valor menor, ADI 1851 e Tema 201 do STF), com 6 questões
- seção própria sobre substituição tributária (para trás e para frente, art. 150, § 7º, da CF, restituição do fato gerador presumido) e 6 questões, além de reforço das infrações do CTN
- seção sobre responsabilidade no processo tributário e na execução fiscal: redirecionamento ao sócio-administrador, CDA e ônus da prova, Súmula 435 do STJ, incidente de desconsideração, com 6 questões
- seção própria sobre substituição tributária (para trás e para frente, art. 150, § 7º, da CF, restituição) e 6 questões
- capítulo sobre direito tributário sancionador: princípios (legalidade, culpabilidade, proporcionalidade, vedação de confisco em multas), retroatividade benigna (art. 106, II, do CTN), multas de ofício, qualificada e isolada, e 8 questões
- seção própria sobre substituição tributária progressiva (para frente): art. 150, § 7º, da CF, base de cálculo presumida, restituição, ADI 1851 e Tema 201 do STF, com 6 questões
- seção sobre responsabilidade tributária nos impostos estaduais e municipais: ICMS (substituição, solidariedade, LC 87/1996), ISS (responsável pelo tomador, LC 116/2003), IPVA, IPTU e ITBI (sucessão e responsabilidade do adquirente), com 6 questões
- seção própria sobre substituição tributária: para trás (diferimento) e para frente (art. 150, § 7º, da CF, base presumida, restituição, ADI 1851 e Tema 201 do STF), com 6 questões

### 11. `matematica.geometria-plana` — Geometria plana
Arquivo atual: `conteudo/materias/matematica/geometria-plana.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- Seção sobre circunferência: posições relativas de reta e circunferência, tangentes, cordas, ângulo central e inscrito, arco, polígonos inscritos e circunscritos, com 8 questões
- Seção sobre sólidos (prismas, cilindro, pirâmide, cone, esfera): elementos, áreas e volumes, que hoje estão fora do escopo (geometria espacial); 8 questões
- Seção sobre plano cartesiano: coordenadas, distância entre dois pontos, ponto médio e figuras no plano, com 5 questões
- Seção sobre razões trigonométricas no triângulo retângulo (seno, cosseno, tangente, ângulos de 30°, 45° e 60°) com 6 questões
- Seção sobre volume de prismas, cilindros e outros sólidos básicos, com 6 questões
- Seção sobre volume de sólidos básicos (cubo, paralelepípedo, cilindro) com 6 questões
- Seção sobre elementos de figuras espaciais (faces, arestas, vértices, planificações) com 5 questões
- Seção sobre congruência de triângulos (casos LAL, ALA, LLL) e semelhança de figuras espaciais com razão de volumes, 6 questões
- Seção sobre o teorema de Tales (feixe de paralelas e transversais, teorema da bissetriz) com 6 questões
- Seção sobre diagonais de polígonos e de sólidos, e ângulos em figuras espaciais, com 5 questões
- Seção sobre eixos e centro de simetria de figuras planas e espaciais, com 5 questões
- Seção sobre pontos notáveis do triângulo (baricentro, incentro, circuncentro, ortocentro) com propriedades e 6 questões

### 12. `ciencias.leis-basicas-eletricidade` — Leis básicas em eletricidade
Arquivo atual: `conteudo/materias/ciencias/leis-basicas-eletricidade.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre corrente alternada em regime permanente: fasores, impedância, reatância indutiva e capacitiva, circuitos RL, RC e RLC, potência ativa, reativa e aparente, com 10 questões
- Seção sobre medidas elétricas: amperímetro, voltímetro, ohmímetro e multímetro (ligação em série/paralelo, resistência interna, escalas) com 6 questões
- Seção sobre sistemas trifásicos equilibrados e desequilibrados (ligação estrela e triângulo, tensões de fase e de linha, potência trifásica), com 8 questões
- Seção sobre fator de potência (triângulo de potências, correção com capacitores) com 6 questões
- Seção sobre condutividade, resistividade, condutores, isolantes e semicondutores (classificação dos materiais, variação com a temperatura) com 6 questões
- Seção sobre materiais ôhmicos e não ôhmicos (curva V x I, lâmpada, diodo, termistor) com 5 questões
- Seção sobre geradores e receptores (força eletromotriz, resistência interna, rendimento, equação do gerador) com 8 questões
- Seção sobre circuitos lineares e superposição, teoremas de Thevenin e Norton e transformação de fontes, com 8 questões
- Capítulo sobre corrente alternada em regime permanente: fasores, impedância, reatância indutiva e capacitiva, circuitos RL, RC e RLC, potência ativa, reativa e aparente, com 10 questões e análise de circuitos em CC e CA
- Seção sobre potência e fator de potência em circuitos trifásicos equilibrados e desequilibrados, com 8 questões
- Capítulo de eletrotécnica geral: transformadores, motores e geradores elétricos, trifásico e fator de potência, com 10 questões
- Seção sobre teoremas de circuitos (superposição, Thevenin, Norton, máxima transferência de potência) com 8 questões

### 13. `direito-administrativo.consorcios-publicos` — Consórcios Públicos
Arquivo atual: `conteudo/materias/direito-administrativo/consorcios-publicos.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- o Decreto 6.017/2007 (regulamento dos consórcios: protocolo de intenções, estatuto, assembleia geral, contrato de programa, exclusão e retirada, convênios com o consórcio) e a Lei 11.107 com seus artigos e cerca de 8 questões; o decreto não é citado
- consórcios em nível de magistratura: objetivos e conteúdo do protocolo de intenções, consórcios entre entes de esferas diferentes, regras de licitação e contratação do consórcio, responsabilidade dos consorciados, exclusão e Decreto 6.017/2007, com cerca de 8 questões de jurisprudência e lei
- o Decreto 6.017/2007 (regulamento dos consórcios: protocolo de intenções, estatuto, assembleia geral, contrato de programa, exclusão e retirada) com cerca de 8 questões; o decreto não é citado
- o Decreto 6.017/2007 (regulamento dos consórcios: protocolo, estatuto, assembleia, contrato de programa, exclusão e retirada) com cerca de 8 questões de nível de procurador; o decreto não é citado
- consórcios em nível de magistratura: natureza jurídica, protocolo de intenções, responsabilidade dos consorciados, licitação e contratação pelo consórcio, retirada e exclusão, e Decreto 6.017/2007, com cerca de 8 questões
- consórcios em nível de magistratura: natureza jurídica, federalismo cooperativo, protocolo de intenções, responsabilidade dos consorciados e Decreto 6.017/2007, com cerca de 8 questões

### 14. `direito-trabalho.intervalos-descanso-noturno` — Intervalos, descanso semanal e trabalho noturno
Arquivo atual: `conteudo/materias/direito-trabalho/intervalos-descanso-noturno.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- trabalho extraordinário (horas extras, adicional, limite diário, acordo de prorrogação) com 8 questões
- trabalho extraordinário (horas extras, adicional, limite diário, acordo de prorrogação) com 8 questões; a teoria está em direito-trabalho.jornada-horas-extras
- base de cálculo do DSR para quem recebe por hora, comissão ou horas extras habituais, com 6 questões

### 15. `direito-trabalho.dano-moral-responsabilidade` — Dano extrapatrimonial e responsabilidade civil trabalhista
Arquivo atual: `conteudo/materias/direito-trabalho/dano-moral-responsabilidade.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- dano material (danos emergentes e lucros cessantes) e sua cumulação com dano moral, com 8 questões
- igualdade e não discriminação nas relações de trabalho (sexo, raça, deficiência, idade; base constitucional e legal) com 8 questões
- dano material e dano estético nas relações de trabalho, com 8 questões
- súmulas do TST sobre dano moral e assédio e seu uso em prova, com 6 questões
- perícia em casos de assédio: avaliação psicológica, nexo causal e prova pericial, com 6 questões
- políticas de prevenção e enfrentamento do assédio e da discriminação (canais de denúncia, compliance, programas internos) com 6 questões
- relação entre dano moral e acidente do trabalho (indenização por doença ocupacional, nexo, culpa), com 8 questões; a matéria exclui acidente do trabalho (ver direito-trabalho.acidente-trabalho)

### 16. `direito-trabalho.principios-fontes` — Princípios e fontes do Direito do Trabalho
Arquivo atual: `conteudo/materias/direito-trabalho/principios-fontes.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- distinção entre renúncia e transação, validade da transação, quitação e limites da vontade do empregado, com 8 questões
- direitos constitucionais dos trabalhadores (art. 7º) com 8 questões; ver direito-trabalho.direitos-constitucionais-trabalhadores
- sujeitos do Direito do Trabalho (empregado e empregador, equiparados) com 6 questões
- conceito, formação histórica, interpretação, aplicação e tendências do Direito do Trabalho, com 8 questões
- direitos constitucionais dos trabalhadores e distinção entre relação de trabalho e relação de emprego, com 8 questões

### 17. `direito-administrativo.contratacao-direta` — Contratação direta: dispensa e inexigibilidade
Arquivo atual: `conteudo/materias/direito-administrativo/contratacao-direta.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre as vedações ligadas à contratação direta (fracionamento de despesa para fugir da licitação, impedimentos de contratar, vedação a agentes com conflito de interesse) e 5 questões
- seção sobre as vedações ligadas à contratação direta (fracionamento de despesa, impedimentos de contratar, conflito de interesse) e 5 questões
- seção sobre vedações e hipóteses de dispensa e inexigibilidade específicas de obras e serviços de engenharia (limites para obras, fracionamento) e 6 questões
- seção sobre vedações e hipóteses de dispensa e inexigibilidade específicas de obras e serviços de engenharia (limites, fracionamento, emergência) e 6 questões
- capítulo sobre procedimentos auxiliares (credenciamento, pré-qualificação, procedimento de manifestação de interesse, sistema de registro de preços, registro cadastral) e 8 questões

### 18. `informatica.blockchain-criptoativos` — Blockchain e criptoativos
Arquivo atual: `conteudo/materias/informatica/blockchain-criptoativos.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- uso de criptoativos na lavagem de dinheiro (etapas de ocultação, mixers, exchanges, rastreamento on-chain, deveres de prevenção, Lei 9.613 aplicada a criptoativos) com cerca de 8 questões; hoje a prevenção à lavagem aparece só de passagem
- bitcoin em particular (mineração, UTXO, halving, limite de 21 milhões, carteiras) e principais criptomoedas como Ethereum e altcoins, com cerca de 6 questões
- histórico do Bitcoin (white paper de 2008, bloco gênese, forks) e das principais criptomoedas (Ethereum, stablecoins, altcoins), com questões
- uso de criptomoedas no mercado financeiro: exchanges, corretoras, ETFs e derivativos, pagamentos e remessas, DeFi, moeda digital de banco central, com cerca de 6 questões
- exploradores de blocos (block explorers): consulta de blocos, transações, endereços e taxas, leitura de hash e confirmações, com exemplos e cerca de 5 questões
- marco legal das criptomoedas no Brasil (Lei 14.478/2022 e regulamentação), crimes envolvendo criptoativos (estelionato, lavagem, pirâmides) e aspectos de investigação e apreensão, com questões de nível de delegado
- sistemas de suporte à decisão analítica (BI, dashboards, data warehouse e mineração aplicados a decisão) e aplicações práticas de blockchain, com questões; os conceitos de blockchain já estão cobertos
- criptografia simétrica e assimétrica, algoritmos e certificação digital (a matéria só trata de hash e assinatura em noções) e regulação de ativos virtuais pela Lei 14.478/2022, com questões de nível de juiz
