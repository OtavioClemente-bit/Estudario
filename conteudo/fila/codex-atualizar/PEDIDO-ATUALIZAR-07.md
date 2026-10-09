# Atualizar matérias — pedido 07 de 18 (09/10/2026)

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

### 1. `administracao-geral.administracao-materiais-logistica` — Administração de materiais, patrimônio e logística
Arquivo atual: `conteudo/materias/administracao-geral/administracao-materiais-logistica.json` · 31 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre bens públicos e patrimônio de órgãos e empresas estatais (uso comum, uso especial, dominicais; bens das estatais) e 4 questões
- modalidades de compra (concorrência, pregão, dispensa, registro de preços) em visão resumida e 5 questões; hoje só há a ponte com a Lei 14.133
- capítulo sobre patrimônio imobiliário público: tipos de imóveis, cadastro e registro, regimes de utilização (cessão, concessão, locação) e 5 questões
- seção sobre o perfil e as responsabilidades do comprador (conhecimento do mercado, negociação, ética, conflito de interesses) e 3 questões
- seção sobre cadastro de fornecedores (cadastro único, habilitação, avaliação de desempenho, sanções) e 4 questões
- capítulo de manutenção de equipamentos: corretiva, preventiva e preditiva, planejamento e indicadores (MTBF, MTTR), com 5 questões
- capítulo de manutenção de instalações físicas: manutenção predial, conservação, contratos de serviços de apoio, com 5 questões
- processos de compras governamentais (planejamento, modalidades, registro de preços, fases) e 5 questões
- cadastro e registro de imóveis públicos (matrícula, cadastro de imóveis da União, tombamento imobiliário) e 4 questões
- análise de valor (avaliação de imóveis), alienação de imóveis públicos (autorização, avaliação prévia, licitação) e manutenção, com 5 questões
- parte imobiliária do tópico (cadastro, registro e regimes de uso de imóveis públicos) com 4 questões; a parte mobiliária já está coberta
- compras governamentais (planejamento, modalidades, registro de preços) e 5 questões; estoques já cobertos

### 2. `direito-civil.sucessoes` — Sucessões
Arquivo atual: `conteudo/materias/direito-civil/sucessoes.json` · 23 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre inventário e partilha com o procedimento do CPC (inventariante, prazos, arrolamento, colação, sonegados), com 8 questões
- seção sobre petição de herança (art. 1.824: legitimidade, herdeiro aparente, efeitos) e 4 questões
- seção sobre petição de herança e herdeiro aparente, com 4 questões
- seção sobre testamentos cerrado e particular em detalhe, testamentos especiais (marítimo, aeronáutico, militar) e codicilo, com 6 questões
- capítulo sobre legados (espécies, caducidade), direito de acrescer e substituições (vulgar, recíproca, fideicomissária), com 8 questões
- seção sobre redução das disposições testamentárias (art. 1.967) e rompimento, com 4 questões
- seções sobre sonegados, pagamento das dívidas, colação, garantia dos quinhões e anulação da partilha, com 8 questões
- seção com casos práticos de inventário e partilha (inventariante, colação, partilha em vida, sobrepartilha) e 6 questões
- seção sobre usufruto e fideicomisso em testamento e sobre capacidade de testar em detalhe, com 4 questões
- seção sobre inventário, partilha e arrolamento no CPC (procedimento, inventariante, arrolamento sumário) e 6 questões
- seção sobre legados e sua caducidade (art. 1.939), com 4 questões
- seção sobre testamentos especiais (marítimo, aeronáutico, militar), com 3 questões

### 3. `informatica.hardware-conceitos-basicos` — Hardware: conceitos básicos
Arquivo atual: `conteudo/materias/informatica/hardware-conceitos-basicos.json` · 36 tópicos de edital pedem mais conteúdo

Falta:
- mídias ópticas (CD, DVD, Blu-ray: capacidades, leitura/gravação, tipos -R e -RW) e 4 questões; placa-mãe, memórias, CPU, HD e periféricos já cobertos
- arquitetura de sistemas computacionais: modelo de von Neumann, barramentos, hierarquia de memória e interface com E/S, com 8 questões em nível de TI
- montagem e manutenção: compatibilidade de soquetes e slots, fonte, refrigeração, ordem de montagem, manutenção preventiva e corretiva e diagnóstico de defeitos, com 6 questões
- subsistema de entrada e saída (controladoras, barramentos, interrupção, DMA) e armazenamento secundário (HDD, SSD, RAID, interfaces SATA/NVMe), com 6 questões
- organização do computador: modelo de von Neumann, CPU, barramentos, hierarquia de memória e E/S, com 8 questões
- mídias ópticas CD e DVD (tipos, capacidades, leitura e gravação) e 4 questões; placa-mãe, memórias, processadores e HD já cobertos
- mídias ópticas CD e DVD (tipos, capacidades, leitura e gravação) e 4 questões; placa-mãe, memórias, processadores, HD e periféricos já cobertos
- hierarquia de memória (registradores, cache L1/L2/L3, RAM, armazenamento: velocidade, custo, localidade) e ciclo de busca-decodificação-execução, com 6 questões
- hierarquia de memória e organização da CPU (unidade de controle, ULA, registradores, ciclo de instrução), com 6 questões
- barramentos, hierarquia de memória, memória secundária e E/S (controladoras, DMA) em nível de TI, com 8 questões
- componentes da UCP (unidade de controle, ULA, registradores PC/IR/acumulador, ciclo de instrução) e interrupção/IRQ (tipos e tratamento; ver também sistemas-operacionais-conceitos), com 6 questões
- hardware de servidores: processadores multi-soquete, memória ECC, redundância de fonte e disco, RAID, rack e blade, com 6 questões

### 4. `seguranca-publica.criminologia` — Criminologia
Arquivo atual: `conteudo/materias/seguranca-publica/criminologia.json` · 23 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre teorias sociológicas do crime: Escola de Chicago e desorganização social, associação diferencial, anomia (Durkheim e Merton), subcultura delinquente, labeling approach e teoria do conflito, com 8 questões
- capítulo sobre os modelos teóricos além das escolas: teorias sociológicas (desorganização, anomia, subcultura, associação diferencial), bioantropológicas e psicológicas, e 8 questões
- capítulo sobre modelos de reação ao crime (dissuasório/clássico, ressocializador, integrador/restaurativo) e 6 questões
- capítulo sobre os modelos de reação ao crime (dissuasório, ressocializador, integrador/restaurativo) e 6 questões
- capítulo sobre teorias sociológicas: Escola de Chicago, desorganização social, anomia, associação diferencial, subcultura delinquente, labeling approach, e 8 questões
- capítulo sobre os modelos teóricos da criminologia (teorias biológicas, psicológicas e sociológicas, consenso e conflito) e 8 questões
- capítulo sobre teorias criminológicas contemporâneas (criminologia crítica, labeling, administrativa/atuarial, tolerância zero, teoria das janelas quebradas, minimalismo e abolicionismo) e 8 questões
- capítulo sobre processos de criminalização primária e secundária, seletividade do sistema penal e etiquetamento, e 6 questões
- capítulo sobre a cifra oculta (negra) da criminalidade, cifras douradas, pesquisas de vitimização e subnotificação, e 5 questões
- capítulo sobre sistema penal e seletividade, encarceramento em massa, política criminal e penitenciária, relações de poder na prisão, policiamento, e 8 questões
- capítulo sobre criminologia crítica, minimalismo penal, garantismo, abolicionismo, mídia e crime, racismo, gênero e tendências de política criminal, com 10 questões
- capítulo sobre o perfil do delinquente nas teorias biológicas e psicológicas (Lombroso, Ferri, Garofalo, constituição, personalidade) e 5 questões

### 5. `economia.economia-brasileira` — Economia brasileira: planos, inflação, câmbio e política monetária
Arquivo atual: `conteudo/materias/economia/economia-brasileira.json` · 27 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo de panorama da economia brasileira do pós-Real até hoje (crises de 2002 e 2008, ciclo de commodities, política social e consumo, recessão de 2015-16, teto de gastos, pandemia e recuperação) com indicadores gerais, e 5 questões de atualidades.
- Seção sobre como o déficit público foi financiado nos anos 1980 (senhoriagem e imposto inflacionário, dívida mobiliária interna e overnight, estatização da dívida externa, monetização) e 4 questões.
- Capítulo sobre o comportamento recente da economia (2003-2024): políticas dos governos Lula, Dilma, Temer, Bolsonaro e atual, com crescimento, inflação, contas externas e fiscal, e 6 questões.
- Capítulo sobre o período do milagre econômico (1968-73), II PND, choques do petróleo e passagem ao ajuste dos anos 1980, ligado à evolução posterior da política econômica, com 6 questões.
- Capítulo sobre o comportamento recente da economia (2003-2024) e as políticas econômicas dos últimos governos, com indicadores de crescimento, inflação, contas externas e fiscal, e 6 questões.
- Seção sobre o debate acerca das causas da inflação nos anos 1980 (monetaristas, estruturalistas e inercialistas, proposta Larida de moeda indexada, choques heterodoxo e ortodoxo) com 5 questões; a matéria só explica inflação inercial.
- Capítulo sobre a economia pós-Real: crises externas e câmbio, tripé, expansão do crédito e do consumo, política social, desaceleração e recessão de 2014-16, teto de gastos e desafios, com 6 questões.
- Seção sobre a crise da dívida externa dos anos 1980 em detalhe (choque de juros, moratória do México, cartas de intenção ao FMI, ajuste externo e superávit comercial, transferência real de recursos, moratória de 1987), com 5 questões.
- Capítulo sobre a década de 1990: abertura comercial e financeira, privatizações e reforma do Estado, efeitos sobre indústria, inflação e balanço de pagamentos, desindustrialização e reprimarização, com 6 questões.
- Seção sobre o ajuste de 1980-84: maxidesvalorização de 1979 e 1983, superávits comerciais, desequilíbrio interno, cartas de intenção e negociações com o FMI, com 4 questões.
- Capítulo histórico cobrindo industrialização por substituição de importações, Plano de Metas, PAEG, milagre, II PND e Plano Cruzado ao período recente, com 8 questões; a matéria exclui esses planos e começa nos anos 1980.
- Capítulo sobre as reformas estruturais dos anos 1990 (abertura, privatização, renegociação das dívidas estaduais, reforma do sistema financeiro, Proer) e sobre a economia depois do Real, com 6 questões.

### 6. `direito-empresarial.falencia-recuperacao` — Recuperação judicial, extrajudicial e falência
Arquivo atual: `conteudo/materias/direito-empresarial/falencia-recuperacao.json` · 36 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre as ações da falência: pedido e ação de restituição (art. 85), embargos de terceiro, ação de responsabilidade de sócios e administradores e rescisória do quadro-geral, com 6 questões
- capítulo sobre responsabilidade na falência: ação de responsabilização de sócios de responsabilidade ilimitada, controladores e administradores (art. 82), desconsideração da personalidade e 5 questões
- seção sobre crise econômica, financeira e patrimonial (conceitos distintos), sinais de insolvência e cessação de pagamentos, com 3 questões
- seção sobre os princípios da insolvência (par conditio creditorum, preservação da empresa, celeridade, universalidade, maximização de ativos, transparência) e 5 questões
- capítulo sobre o plano especial de recuperação judicial de ME e EPP (arts. 70 a 72): requisitos, parcelamento em até 36 parcelas, créditos abrangidos, e 5 questões
- seção sobre os meios de recuperação do art. 50 (reestruturação societária, cessão de quotas, trespasse, venda de ativos, equalização de encargos, dação em pagamento etc.) e 5 questões
- seção sobre legitimidade ativa do pedido de falência: credor, sócio, cônjuge, herdeiro, inventariante, credor empresário e credor estrangeiro (caução), e 4 questões
- seção sobre legitimidade passiva: empresário individual, sociedade empresária, empresário irregular, espólio e sócios de responsabilidade ilimitada, e 4 questões
- seção sobre o conteúdo da sentença de falência (art. 99), recurso cabível (agravo), prazo de habilitação, nomeação do administrador e 5 questões
- capítulo sobre realização do ativo (arts. 139 a 148): ordem de preferência das modalidades, leilão, propostas, ausência de sucessão do arrematante e 6 questões
- capítulo sobre modalidades de venda dos bens da massa (leilão, propostas fechadas, pregão), venda em bloco, UPI e não sucessão, com 5 questões
- seção sobre reabilitação do falido e inabilitação empresarial, efeitos da condenação por crime falimentar e extinção das obrigações, com 3 questões

### 7. `direito-internacional.publico-fundamentos` — Direito Internacional Público: fundamentos
Arquivo atual: `conteudo/materias/direito-internacional/publico-fundamentos.json` · 38 tópicos de edital pedem mais conteúdo

Falta:
- obrigações erga omnes: conceito, caso Barcelona Traction, diferença para jus cogens, legitimidade de qualquer Estado para exigir o cumprimento; 4 questões
- soberania e o conceito de Huber no caso da Ilha de Palmas (Holanda x EUA, 1928): soberania como independência e aquisição de território, e 4 questões
- princípio da não intervenção e o caso Nicarágua x EUA (CIJ, 1986): proibição do uso da força, intervenção e autodefesa coletiva; 4 questões
- caso Lotus (CPJI, 1927) e o limite à atuação do Estado: liberdade de agir salvo proibição, jurisdição extraterritorial; 4 questões
- caso Arrest Warrant (Congo x Bélgica, CIJ, 2002): imunidade de ministro das Relações Exteriores, jurisdição universal e as opiniões separadas; 4 questões
- história do Direito Internacional (Westfália, Congresso de Viena, Liga das Nações, ONU) e a evolução das fontes e do direito dos tratados; 5 questões
- solução pacífica de controvérsias (negociação, bons ofícios, mediação, inquérito, conciliação, arbitragem, CIJ), sua base na Carta da ONU, e 6 questões
- história do direito dos tratados (costume, codificação, Convenção de Viena 1969 e 1986) e fontes de interpretação; 4 questões
- surgimento e extinção de Estados (formas de criação, secessão, fusão, desmembramento, continuidade do Estado) e 4 questões
- sucessão de Estados em tratados, bens, dívidas e arquivos (Convenções de Viena de 1978 e 1983), Estado sucessor e Estado continuador; 5 questões
- território do Estado: elementos (terrestre, marítimo e aéreo), modos de aquisição e perda (cessão, ocupação, prescrição, conquista) e fronteiras; 5 questões
- proteção diplomática: conceito, requisitos (nacionalidade, esgotamento dos recursos internos), natureza, caso Nottebohm e 4 questões

### 8. `legislacao.estatuto-pessoa-idosa` — Estatuto da Pessoa Idosa
Arquivo atual: `conteudo/materias/legislacao/estatuto-pessoa-idosa.json` · 25 tópicos de edital pedem mais conteúdo

Falta:
- Aprofundar o Estatuto artigo a artigo: direitos fundamentais (vida, liberdade, alimentos, saúde, educação, trabalho, previdência, habitação, transporte), entidades de atendimento, fiscalização, Ministério Público e acesso à justiça, com capítulos próprios e ao menos 25 questões a mais
- Capítulo detalhado dos crimes dos arts. 95 a 108 (discriminação, abandono, exposição a perigo, apropriação de bens, retenção de cartão, procuração, coação, imagem depreciativa) com sujeitos, penas conforme texto vigente, ação penal pública incondicionada e procedimento da Lei 9.099 para penas até 4 anos, e 15 questões
- Aprofundar o Estatuto artigo a artigo: direitos fundamentais (vida, liberdade, alimentos, saúde, educação, trabalho, previdência, habitação, transporte), entidades de atendimento, fiscalização, Ministério Público e acesso à justiça, com capítulos próprios e ao menos 25 questões a mais; e os crimes: Capítulo detalhado dos crimes dos arts. 95 a 108 (discriminação, abandono, exposição a perigo, apropriação de bens, retenção de cartão, procuração, coação, imagem depreciativa) com sujeitos, penas conforme texto vigente, ação penal pública incondicionada e procedimento da Lei 9.099 para penas até 4 anos, e 15 questões
- Capítulo sobre o Título I e os direitos fundamentais, em especial o Capítulo X (transporte: gratuidade a partir de 65 anos, reserva de assentos, desconto em transporte interestadual) com 10 questões
- Seção sobre a Política Nacional do Idoso (Lei 8.842/1994), políticas setoriais para o segmento, conselhos do idoso e fundos, com 8 questões
- Capítulo sobre a Política Nacional do Idoso (Lei 8.842/1994), o decreto que a regulamenta, e a tutela coletiva dos direitos do idoso pelo Ministério Público, com 10 questões
- Capítulo sobre os direitos fundamentais do Título II: vida, liberdade, respeito e dignidade, alimentos, saúde, educação, cultura e lazer, profissionalização e trabalho, previdência, assistência social, habitação e transporte, com 15 questões

### 9. `auditoria.conceitos-normas` — Conceitos, normas e fundamentos da auditoria
Arquivo atual: `conteudo/materias/auditoria/conceitos-normas.json` · 29 tópicos de edital pedem mais conteúdo

Falta:
- mapa das NBC TA (200 a 810) e das NBC PA (01, 290 e correlatas), com a função de cada uma e relação com as ISA, e 8 questões; hoje os princípios aparecem sem numeração
- mapa das NBC TA (200 a 810) e das NBC PA, com a função de cada uma e relação com as ISA, e 8 questões
- perícia contábil (NBC TP 01 e NBC PP 01: objeto, perito, assistente técnico, laudo) em contraste com auditoria interna e independente, e 6 questões
- perícia contábil (objeto, perito, assistente técnico, laudo) em contraste com auditoria interna e independente, e 6 questões
- mapa das NBC TA com numeração (200, 220, 230, 240, 250, 300, 315, 320, 330, 500, 530, 560, 570, 700) e 10 questões
- asseguração por atestação x trabalho direto (estrutura conceitual): quem mede o objeto, exemplos e 4 questões
- evolução histórica da auditoria (origem, escândalos e normas, SOX) e papéis comparados das auditorias interna e externa, com 5 questões
- normas profissionais e éticas do CFC: Código de Ética Profissional do Contador, NBC PG, NBC PA 290/291 e comunicados técnicos, com 6 questões
- revisão externa de qualidade (revisão pelos pares, CNAI/CVM) e conflito de interesses, com 4 questões; independência e controle de qualidade já cobertos
- normas profissionais e éticas do CFC: Código de Ética Profissional do Contador, NBC PG, NBC PA e comunicados técnicos, com 6 questões
- revisão de informações intermediárias (NBC TR 2410): objetivo, procedimentos (indagação e analíticos), conclusão em forma negativa, e 5 questões; asseguração razoável e limitada já cobertas
- Normas de Atributo do IIA (série 1000: propósito, autoridade e responsabilidade; independência e objetividade; proficiência e zelo; programa de qualidade 1300) e 6 questões

### 10. `direito.convencoes-onu-direitos-humanos` — Convenções da ONU sobre genocídio, refúgio e proteção contra discriminações, tortura e desaparecimento
Arquivo atual: `conteudo/materias/direito/convencoes-onu-direitos-humanos.json` · 27 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o Protocolo Facultativo à CEDAW (comunicações individuais e procedimento de inquérito, Decreto 4.316/2002) e 4 questões
- capítulo sobre a Lei 2.889/1956 (tipos de genocídio no direito brasileiro, penas, competência) e 6 questões
- capítulos sobre a Convenção sobre os Direitos da Criança, a Convenção sobre os Direitos das Pessoas com Deficiência e a Convenção Americana (hoje só tratadas em outras matérias), com 6 questões cada
- capítulo sobre prevenção da tortura: Protocolo Facultativo (OPCAT), Subcomitê, Sistema Nacional de Prevenção e Combate à Tortura, audiência de custódia e fiscalização de locais de privação de liberdade, com 6 questões
- capítulo sobre a Lei 9.474/1997 (CONARE, procedimento de reconhecimento, cessação, perda, extradição e expulsão) e 6 questões
- capítulo sobre convenção da criança, pessoas com deficiência e direitos civis, políticos, econômicos, sociais e culturais, em visão de conjunto, com 8 questões
- capítulo sobre crimes de guerra, crimes contra a humanidade, Estatuto de Roma e Tribunal Penal Internacional (competência, complementaridade), com 8 questões
- capítulo sobre o refúgio no direito brasileiro: Lei 9.474/1997, CONARE, Lei de Migração e acolhimento, com 6 questões
- capítulo sobre o ACNUR: mandato, funções, cooperação com Estados e estatuto, com 5 questões
- capítulo sobre dispositivos legais e administrativos do refúgio no Brasil (Lei 9.474/1997 e CONARE), com 6 questões
- capítulo sobre CONARE, Polícia Federal e controle judicial das decisões sobre refúgio, com 6 questões
- capítulo sobre os tratados multilaterais globais de direitos humanos em geral e seus mecanismos de controle (comitês, relatórios, petições), com 8 questões

### 11. `processo-civil.prova` — Prova
Arquivo atual: `conteudo/materias/processo-civil/prova.json` · 34 tópicos de edital pedem mais conteúdo

Falta:
- Prova ilícita: conceito, art. 5º, LVI, CF, ilicitude por derivação, proporcionalidade e consequências no processo civil, com 5 questões.
- Aprofundar sentença (requisitos, classificação, vícios, efeitos) e coisa julgada em nível de magistratura; o capítulo atual é visão geral, com 6 questões.
- Requisitos essenciais da sentença (relatório, fundamentação, dispositivo, art. 489) e efeitos (hipoteca judiciária, art. 495), com 5 questões.
- Requisitos da sentença e publicação/intimação, retratação do juiz, vinculação do julgador e alteração após publicação (art. 494), com 4 questões.
- Capítulo sobre classificação das decisões (despacho, interlocutória, sentença, acórdão), requisitos e vícios da sentença e 5 questões.
- Ampliar o capítulo de sentença (requisitos, efeitos) e coisa julgada, em nível de Defensoria, com 5 questões.
- Capítulo da perícia judicial nos arts. 156 a 158 (perito, impedimentos, responsabilidade) e 464 a 480, em nível de perito contábil, com 6 questões.
- Requisitos de nomeação do perito (arts. 156 a 158, 465 a 468): habilitação, cadastro, escusa e substituição, com 4 questões.
- Hipóteses de substituição do perito (art. 468: falta de conhecimento, descumprimento de prazo) e comunicação ao órgão de classe, com 3 questões.
- Conteúdo mínimo do laudo pericial (art. 473) e linguagem e fundamentos do perito, com 4 questões.
- Perícia complexa e perito de várias áreas (art. 475), com 3 questões.
- Quesitos impertinentes (art. 470) e suplementares (art. 469), complementação de laudo, com 4 questões.

### 12. `processo-civil.tutela-provisoria` — Tutela provisória: urgência e evidência
Arquivo atual: `conteudo/materias/processo-civil/tutela-provisoria.json` · 25 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre o poder geral de cautela (arts. 297 e 301 do CPC): fundamento, atipicidade das medidas cautelares, adequação e proporcionalidade, e 3 questões.
- seção sobre fungibilidade entre tutela cautelar e tutela antecipada (art. 305, parágrafo único) e adaptação do procedimento, e 4 questões.
- seção sobre o arresto como cautelar do art. 301: conceito, requisitos, finalidade de garantir futura execução por quantia e diferença para o sequestro, e 3 questões.
- seção sobre o sequestro (coisa certa litigiosa, preservação do bem disputado) e sua distinção do arresto e do arrolamento de bens, e 3 questões.
- seção sobre princípios e características da tutela provisória (sumariedade da cognição, provisoriedade, precariedade, revogabilidade, inexauribilidade) e 3 questões.
- seção sobre fungibilidade nas tutelas de urgência (art. 305, parágrafo único): conversão da cautelar em antecipada e vice-versa, e 4 questões.
- seção sobre as cautelares típicas do art. 301 (arresto, sequestro, arrolamento de bens, registro de protesto contra alienação) e sobre busca e apreensão como medida cautelar, explicando que o CPC/2015 extinguiu os procedimentos cautelares específicos, e 6 questões.
- seção sobre arresto, sequestro e busca e apreensão como medidas cautelares do CPC/2015 (art. 301), com a explicação de que o procedimento cautelar específico deixou de existir, e 6 questões.
- seção sobre os princípios da tutela de urgência e de evidência e sobre fungibilidade (art. 305, parágrafo único), que o tópico lista expressamente, e 6 questões.
- capítulo sobre tutelas provisórias contra a Fazenda Pública: vedações das Leis 8.437/1992 e 9.494/1997, art. 1.059 do CPC, suspensão de liminar e de segurança, e 6 questões.
- seção que liga a antecipação de tutela à tutela específica das obrigações de fazer e não fazer (arts. 497 e 536: multa, medidas de apoio, conversão em perdas e danos) e 4 questões.
- seção sobre restrições a liminares contra o Poder Público (Leis 8.437/1992 e 9.494/1997, art. 1.059 do CPC) e suspensão de liminar, com 6 questões.

### 13. `direito-constitucional.processo-legislativo` — Processo legislativo
Arquivo atual: `conteudo/materias/direito-constitucional/processo-legislativo.json` · 14 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre os tipos de procedimento legislativo (ordinário/normal, abreviado, sumário, sumaríssimo, especial e concentrado), com a definição de procedimento legislativo e a diferença para processo legislativo, e 5 questões; hoje só há menção ao processo abreviado.
- Seção sobre a natureza jurídica do processo legislativo (conjunto de atos e relação jurídica procedimental) e os princípios gerais (legalidade, publicidade, devido processo legislativo, simetria, separação de poderes), com 4 questões.
- Glossário regimental com anteprojeto, autógrafos, unicameralismo/bicameralismo, blocos parlamentares, comissões, correção de erro, deliberação, destaque, emendas (tipos), iniciativa e legislatura, com 6 questões; a matéria trata poucos desses termos.
- Capítulo de tramitação regimental das proposições: PLO, PLC, projeto de decreto legislativo, projeto de resolução, indicação, parecer de comissão, emenda e requerimentos, com 6 questões.
- Capítulo sobre processo legislativo nos Estados, DF e Municípios: princípio da simetria, normas de reprodução obrigatória, iniciativa reservada ao Governador e ao Prefeito, emenda à Constituição Estadual e Lei Orgânica, e 6 questões.
- Capítulo sobre o processo legislativo estadual: Assembleia Legislativa, iniciativa do Governador, emenda à Constituição Estadual (art. 60 por simetria), veto e promulgação estaduais, medida provisória estadual, e 6 questões.
- Capítulo sobre processo legislativo estadual (iniciativa, emenda à Constituição Estadual, simetria) e sobre a organização da Assembleia Legislativa (mesa, comissões, sessões), com 6 questões.
- Seção sobre iniciativa em âmbito estadual: iniciativa do Governador, do Tribunal de Justiça, do Ministério Público e da Defensoria Pública, iniciativa compartilhada/concorrente e popular estadual, com 5 questões; hoje a matéria só cobre a iniciativa federal.
- Seção sobre limites à iniciativa legislativa estadual: simetria com o art. 61, §1º, vícios de iniciativa e sua não convalidação pela sanção, Súmula do STF sobre sanção, reserva de administração e 5 questões.
- Capítulo sobre tramitação regimental de proposições (leitura, distribuição às comissões, pareceres, pauta, turnos, redação final) em casa legislativa estadual, com 5 questões.
- Capítulo sobre as proposições legislativas além do projeto de lei: requerimentos, indicações, moções, emendas (tipos) e veto, com seu rito regimental, e 6 questões.
- Capítulo sobre consultoria jurídica legislativa: controle prévio de constitucionalidade e juridicidade, parecer da CCJ, vícios formais de iniciativa e técnica legislativa na atividade parlamentar, e 5 questões.

### 14. `processo-civil.formacao-suspensao-extincao` — Formação, suspensão e extinção do processo; julgamento conforme o estado do processo e saneamento
Arquivo atual: `conteudo/materias/processo-civil/formacao-suspensao-extincao.json` · 19 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre pressupostos processuais (de existência e de validade, subjetivos e objetivos, negativos) e 6 questões
- seção sobre a relação jurídica processual aprofundada (sujeitos, pressupostos, objeto e vícios) e 5 questões
- capítulo sobre relação jurídica, pressupostos, espécies de procedimento, objeto do processo, mérito e questões principais, preliminares e prejudiciais, com 8 questões
- capítulo sobre pressupostos processuais e sobre os tipos de procedimento (comum e especiais) com 6 questões
- seção sobre revelia (arts. 344 a 346: presunção de veracidade, exceções do art. 345 como direitos indisponíveis, intervenção do revel, prazos) e 6 questões
- seção sobre audiência de instrução e julgamento (ordem dos atos, tentativa de conciliação, depoimentos, debates, razões finais) e 6 questões
- seção sobre audiência de instrução e julgamento e visão geral das provas (meios de prova e ônus), com 8 questões
- seção sobre atos processuais (forma, tempo, lugar) e a distinção entre processo e procedimento, com 5 questões
- seção sobre revelia (arts. 344 a 346: presunção de veracidade, exceções do art. 345, intervenção do revel, prazos) e 6 questões
- seção sobre pressupostos processuais e sobre os poderes e deveres das partes e do juiz (arts. 76 a 100 e 139), com 8 questões
- seção sobre ação declaratória incidental e questões prejudiciais, e sobre a réplica a fatos impeditivos, modificativos ou extintivos (arts. 350 e 351), com 5 questões
- seção sobre a audiência preliminar (conciliação e mediação do art. 334, fixação de pontos controvertidos) e declaração de saneamento, com 5 questões

### 15. `direito-administrativo.crimes-administracao-publica` — Crimes contra a Administração Pública
Arquivo atual: `conteudo/materias/direito-administrativo/crimes-administracao-publica.json` · 19 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre crimes praticados por particular contra a Administração em geral (usurpação de função pública, resistência, desobediência, desacato, tráfico de influência, corrupção ativa, contrabando e descaminho) e 8 questões
- crimes contra as finanças públicas (contratação irregular de operação de crédito, despesa não autorizada, assunção de obrigação no fim do mandato, aumento de despesa com pessoal) e 5 questões
- capítulo sobre resistência, desobediência e desacato (elementos, sujeitos, diferenças entre eles) e 6 questões
- capítulo sobre crimes praticados por particular contra a Administração em geral (usurpação de função pública, resistência, desobediência, desacato, tráfico de influência, corrupção ativa, contrabando e descaminho) e 8 questões, além dos crimes contra a administração da justiça
- violação de sigilo funcional e de proposta de concorrência, com 5 questões
- capítulo sobre crimes praticados por particular contra a Administração em geral (usurpação de função pública, resistência, desobediência, desacato, tráfico de influência, corrupção ativa, contrabando e descaminho) e 8 questões, incluindo os crimes contra a Administração Pública estrangeira (corrupção ativa e tráfico de influência em transação comercial internacional)
- crimes contra o INSS e a previdência (apropriação indébita previdenciária, sonegação de contribuição previdenciária, estelionato contra a previdência) e 6 questões
- crimes de particular contra a Administração e demais capítulos do Título XI (administração da justiça, finanças públicas), com 10 questões
- capítulo sobre crimes contra a administração da justiça (denunciação caluniosa, comunicação falsa de crime, falso testemunho, coação no curso do processo, fraude processual, favorecimento pessoal e real, exercício arbitrário das próprias razões) e 8 questões
- crimes contra as finanças públicas (contratação irregular de operação de crédito, despesa não autorizada, assunção de obrigação no fim do mandato, aumento de despesa com pessoal) e 5 questões; crimes licitatórios ficam na matéria própria

### 16. `portugues.redacao-oficial` — Redação oficial
Arquivo atual: `conteudo/materias/portugues/redacao-oficial.json` · 17 tópicos de edital pedem mais conteúdo

Falta:
- capítulo com os tipos de documentos oficiais além do ofício (memorando, mensagem de correio eletrônico, exposição de motivos, ata, atestado, certidão, requerimento, relatório) com finalidade e estrutura de cada um, e 6 questões
- capítulo sobre o Decreto nº 9.758/2019 (forma de tratamento e endereçamento a agentes públicos federais: quem usa Excelentíssimo, Senhor, vedação a tratamentos honoríficos) ligado ao Manual, e 5 questões
- capítulo sobre o Decreto nº 9.758/2019: regras de tratamento e endereçamento nas comunicações com agentes públicos federais, com exemplos de vocativo e endereçamento, e 6 questões
- quadro dos principais modelos de expediente (ofício, memorando, mensagem eletrônica, exposição de motivos, ata) com finalidade de cada um, e 5 questões
- capítulo sobre o Pacto Nacional do Judiciário pela Linguagem Simples (objetivos, técnicas de escrita simples, exemplos de reescrita de texto jurídico) e 6 questões
- seção sobre linguagem simples (frases curtas, voz ativa, palavras comuns, estrutura por importância) e comunicação assertiva em texto institucional, e 5 questões
- capítulo sobre memorando: finalidade, uso interno entre unidades, estrutura e diferença para o ofício, com 5 questões
- capítulo sobre elaboração de minutas e expedientes administrativos (despacho, parecer, nota, informação, requerimento): finalidade e estrutura, e 5 questões
- capítulo com cada documento da lista (histórico escolar, certificado, ata, atestado, circular, requerimento, relatório, remessa) com finalidade e estrutura, e 8 questões
- capítulo sobre redação de ata, requerimento e ofício: estrutura da ata (abertura, desenvolvimento, encerramento, lavratura), requerimento e fecho, e 6 questões
- capítulo sobre tipos de documentos oficiais, sua composição (cabeçalho, corpo, fecho, assinatura) e estrutura, para além do ofício, e 6 questões
- capítulo sobre linguagem simples: conceito, princípios e aplicação em documentos públicos, com exemplos de reescrita, e 5 questões

### 17. `processo-civil.atos-prazos-processuais` — Atos e prazos processuais
Arquivo atual: `conteudo/materias/processo-civil/atos-prazos-processuais.json` · 29 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre os atos do juiz (art. 203): conceito de sentença, decisão interlocutória, despacho e atos ordinatórios do servidor, com exemplos e 6 questões de classificação
- classificação dos atos judiciais (sentença, decisão interlocutória, despacho e atos meramente ordinatórios), seus conceitos e requisitos de fundamentação; 6 questões
- princípios informativos da teoria dos prazos (legalidade, utilidade, continuidade, peremptoriedade e preclusão, razoabilidade) e 4 questões
- classificação dos prazos: legais, judiciais e convencionais; próprios e impróprios; dilatórios e peremptórios; comuns e particulares; com 6 questões
- negócios processuais típicos (foro de eleição, calendário, saneamento consensual, ônus da prova, renúncia a recurso) e atípicos (art. 190), limites e controle; 5 questões
- teoria geral das nulidades processuais: nulidade absoluta e relativa, inexistência, anulabilidade, irregularidade, sanação, princípios (instrumentalidade, prejuízo, causalidade); 6 questões
- conceito, classificação e princípios dos prazos (legais, judiciais, próprios e impróprios, dilatórios e peremptórios), mais 5 questões
- prazos especiais da Fazenda Pública e demais entes: prazo em dobro do art. 183, intimação pessoal, quando não se aplica, remessa necessária e exemplos com contagem; 6 questões
- conceituação e classificação das nulidades: absolutas, relativas, anuláveis, inexistência, mera irregularidade, vícios sanáveis e insanáveis, e 6 questões
- normas fundamentais do CPC (arts. 1º a 12) aplicadas aos atos processuais: boa-fé, cooperação, contraditório, publicidade, ordem cronológica; 6 questões
- vícios dos atos processuais e suas consequências: ato inexistente, nulo, anulável e irregular, declaração e efeitos da nulidade; 6 questões
- julgamentos virtuais, inteligência artificial no processo e transmissão ao vivo de sessões, além do processo tecnológico, com base nas resoluções do CNJ; 5 questões

### 18. `direito-administrativo.terceiro-setor` — Terceiro setor, entes de colaboração e convênios
Arquivo atual: `conteudo/materias/direito-administrativo/terceiro-setor.json` · 19 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre consórcios públicos (Lei 11.107/2005: protocolo de intenções, contrato de rateio, contrato de programa, natureza jurídica do consórcio) e 5 questões, ou ligar também à matéria de consórcios
- seção sobre consórcios administrativos (consórcios públicos x convênios, natureza jurídica, contrato de rateio) e 5 questões
- seção sobre conselhos de políticas públicas e publicização no contexto das mudanças institucionais da gestão pública, com 4 questões
- visão de administração pública gerencial: conselhos de políticas públicas, agências e consórcios no contexto das mudanças institucionais, e 4 questões
- seção sobre responsabilidade civil das entidades do terceiro setor prestadoras de serviço público (art. 37, § 6º, da CF) e responsabilidade do Estado por danos de OS/OSCIP/entidades conveniadas, com 4 questões
- visão de administração pública gerencial: conselhos de políticas públicas, agências reguladoras e executivas e consórcios nas mudanças institucionais, e 4 questões
- seção sobre a Portaria Conjunta MGI/MF/CGU 33/2023 (plano de trabalho, prestação de contas, TCE, Transferegov) e 6 questões
- seção sobre os quatro setores (Estado, mercado, terceiro setor e quarto setor) e suas características, com 4 questões
- quadro comparativo das parcerias da Administração com particulares (concessão, PPP, convênio, termos do MROSC, contrato de gestão) e 5 questões
- capítulo sobre regime de contratações das entidades privadas do terceiro setor (regulamento próprio de compras de OS e OSCIP, compras com recursos de parceria no MROSC, vedações) e 5 questões
- seção sobre terceirização, redes de governança e parcerias com OSC na gestão pública, com 4 questões
- quadro comparativo das parcerias do Poder Público com particulares (concessão, PPP, convênio, termos do MROSC, contrato de gestão) e 5 questões
