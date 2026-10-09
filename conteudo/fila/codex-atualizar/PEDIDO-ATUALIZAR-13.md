# Atualizar matérias — pedido 13 de 18 (09/10/2026)

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

### 1. `legislacao.retencao-documentos-identificacao-criminal` — Apresentação, retenção de documentos e identificação criminal
Arquivo atual: `conteudo/materias/legislacao/retencao-documentos-identificacao-criminal.json` · 11 tópicos de edital pedem mais conteúdo

Falta:
- as hipóteses do art. 3º (incisos I a VI) listadas com a literalidade, os documentos aceitos como identificação civil (art. 2º), os arts. 5º e 7º a 7º-C (perfil genético, banco de dados) e cerca de 10 questões com artigo, nível de delegado
- texto completo da Lei 12.037/2009 com alterações (art. 3º em seus incisos, perfil genético, banco nacional, prazos de exclusão) e questões com número de artigo, nível de delegado
- literalidade do art. 3º (hipóteses de identificação criminal), perfil genético e banco de dados, prazos de exclusão e cerca de 10 questões com artigo, nível de delegado
- a Lei 12.037/2009 com literalidade das hipóteses do art. 3º, perfil genético e exclusão de dados, e questões de promotoria sobre as garantias do identificado
- literalidade do art. 3º, perfil genético, banco nacional e as alterações recentes da lei, com cerca de 10 questões com artigo, nível de delegado
- procedimentos datiloscópico e fotográfico em detalhe (coleta, exame, laudo, identificação por impressões digitais), arts. 5º e 6º e a conexão com o banco de dados, para perito papiloscopista
- a Resolução CNJ 435/2021 (Política e Sistema Nacional de Segurança do Poder Judiciário: princípios, estrutura, segurança de pessoas e instalações), com cerca de 8 questões; a Lei 5.553 já está coberta
- as hipóteses do art. 3º com literalidade, a identificação criminal em fase de denúncia, perfil genético e controle judicial da medida, com questões de nível de magistratura
- coleta de perfil genético na identificação criminal e na execução penal (art. 9º-A da LEP), banco nacional de perfis genéticos, sigilo, exclusão e controvérsia sobre a não autoincriminação, com questões de magistratura
- identificação criminal e genética: hipóteses, procedimento, banco de perfis genéticos, direitos do identificado e jurisprudência, com questões de magistratura

### 2. `conhecimentos-bancarios.prevencao-lavagem-dinheiro` — Prevenção à lavagem de dinheiro
Arquivo atual: `conteudo/materias/conhecimentos-bancarios/prevencao-lavagem-dinheiro.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- LC 105/2001: conceito de sigilo das operações, hipóteses de quebra, comunicação ao Coaf e ao Ministério Público, com 8 questões
- Lei 9.613/98 (pessoas obrigadas, infrações administrativas) e Circular 3.978/2020 e Carta Circular 4.001/2020 (política, avaliação interna de risco, KYC, PEP, monitoramento, comunicação), com 12 questões
- Lei 9.613/98: tipos, pessoas obrigadas, deveres e sanções administrativas, Coaf, com 10 questões; aspecto penal em legislacao-penal-especial.lavagem-dinheiro
- Circular 3.978/2020, Carta Circular 4.001/2020 e Resolução CVM 50/2021 (abordagem baseada em risco, cadastro, monitoramento, comunicação), com 12 questões
- relacionamento com clientes, cadastro (KYC) e suitability, com 8 questões
- Relatórios de Inteligência Financeira do Coaf: finalidade, emissão e sigilo, com 5 questões
- art. 1º, I, da Carta Circular 4.001/2020 (operações e situações suspeitas de lavagem), com 8 questões
- LC 105/2001: sigilo das operações, exceções e quebra por ordem judicial ou pelo Banco Central, com 8 questões

### 3. `portugues.coerencia-textual` — Coerência textual
Arquivo atual: `conteudo/materias/portugues/coerencia-textual.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Fatores de textualidade além da coerência: coesão referencial e sequencial, situacionalidade e intertextualidade, com questões.
- Fatores de textualidade: intencionalidade, aceitabilidade, informatividade, situacionalidade e intertextualidade, com questões.
- Estruturação do parágrafo (tópico frasal, desenvolvimento, conclusão) e do texto em introdução, desenvolvimento e conclusão, com questões.

### 4. `direito.pacto-internacional-direitos-civis-politicos` — Pacto Internacional dos Direitos Civis e Políticos (PIDCP)
Arquivo atual: `conteudo/materias/direito/pacto-internacional-direitos-civis-politicos.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Protocolos facultativos do PIDCP (comunicações individuais e abolição da pena de morte), que o escopo da matéria exclui, com questões.
- Protocolos facultativos dos Pactos de 1966 (comunicações individuais, pena de morte, Protocolo do PIDESC) com questões.

### 5. `lingua-inglesa.interpretacao-texto-ingles` — Interpretação de texto em inglês
Arquivo atual: `conteudo/materias/lingua-inglesa/interpretacao-texto-ingles.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre metáfora, metonímia e demais funções retóricas em textos em inglês (figures of speech), com 6 questões
- capítulo de inglês técnico de TI (documentação, termos de sistemas, leitura de manuais) e 6 questões com textos técnicos
- capítulo sobre funções retóricas (definir, comparar, exemplificar, concluir) e percepção de metáfora e metonímia em inglês, com 6 questões
- capítulo sobre funções retóricas, metáfora e metonímia em inglês, com 6 questões
- capítulo de inglês técnico de TI: leitura de documentação, logs, especificações e vocabulário internacional de TI, com 8 questões de textos técnicos
- capítulo sobre funções retóricas do texto (descrever, argumentar, comparar, contrastar, exemplificar, concluir) e 5 questões
- capítulo sobre metáfora e metonímia em textos em inglês, com 5 questões
- capítulo sobre expressões idiomáticas e phrasal verbs em inglês, com 6 questões
- capítulo de leitura de textos técnicos/administrativos de controle externo e TI em inglês, com vocabulário especializado e 6 questões

### 6. `processo-trabalho.recursos` — Recursos no processo do trabalho
Arquivo atual: `conteudo/materias/processo-trabalho/recursos.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre uniformização de jurisprudência no CPC e no processo do trabalho (IRDR, IAC, incidente de uniformização), com 6 questões
- seção sobre reclamação correcional, uniformização de jurisprudência e recursos de revista e embargos repetitivos, com 6 questões
- seções sobre ação civil pública, reclamação correcional, uniformização de jurisprudência, recursos repetitivos e processo judicial eletrônico, com 8 questões
- seção sobre processo judicial eletrônico no processo do trabalho (peticionamento, prazos, intimações eletrônicas), com 5 questões
- seção sobre normas do processo judicial eletrônico (peticionamento, prazos, intimações), com 5 questões
- seção sobre agravo regimental (cabimento, prazo, órgão competente), com 4 questões
- seção sobre o incidente de uniformização de jurisprudência (CLT e regimentos), com 5 questões
- seção sobre o incidente de recursos de revista e embargos repetitivos (procedimento, afetação, efeitos), com 5 questões

### 7. `direito-administrativo.sancoes-crimes-licitacoes` — Sanções administrativas e crimes em licitações
Arquivo atual: `conteudo/materias/direito-administrativo/sancoes-crimes-licitacoes.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o controle das contratações públicas (arts. 169 a 173 da Lei 14.133/2021: três linhas de defesa, controle interno e externo, papel dos tribunais de contas) e como se relaciona com as sanções, com 5 questões
- quadro comparativo dos crimes dos arts. 89 a 98 da Lei 8.666/1993 (revogada) com os arts. 337-E a 337-P do Código Penal, indicando o que mudou em tipos e penas, com 5 questões sobre a Lei 8.666
- quadro comparativo entre os crimes da Lei 8.666/1993 (arts. 89 a 98, revogada) e os do Código Penal após a Lei 14.133/2021, com 5 questões que cobrem a Lei 8.666
- quadro comparativo dos crimes dos arts. 89 a 98 da Lei 8.666/1993 com os arts. 337-E a 337-P do CP (o que foi mantido, alterado ou criado), com 5 questões sobre a Lei 8.666
- quadro comparativo dos crimes dos arts. 89 a 98 da Lei 8.666/1993 com os arts. 337-E a 337-P do CP, com atenção à vigência (ultratividade até a revogação) e 5 questões sobre a Lei 8.666
- capítulo sobre recursos administrativos contra sanções (art. 166 e seguintes da Lei 14.133/2021: recurso, pedido de reconsideração, prazos, efeito suspensivo) e sobre tutela judicial (controle judicial das sanções, mandado de segurança), com 6 questões
- quadro comparativo dos crimes contra o procedimento licitatório dos arts. 89 a 98 da Lei 8.666/1993 com os arts. 337-E a 337-P do CP, com 5 questões sobre a Lei 8.666
- seção sobre os crimes contra as finanças públicas (arts. 359-A a 359-H do Código Penal: contratação de operação de crédito, inscrição de restos a pagar, assunção de obrigação no último ano do mandato etc.) e 6 questões
- seção sobre recursos no procedimento sancionatório (recurso e pedido de reconsideração dos arts. 166 e 167: prazos de 15 dias úteis, efeito suspensivo, autoridade competente) e 5 questões

### 8. `seguranca-publica.seguranca-publica-constituicao` — Segurança pública na Constituição
Arquivo atual: `conteudo/materias/seguranca-publica/seguranca-publica-constituicao.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- atribuições da Polícia Rodoviária Federal (art. 144, § 2º: patrulhamento ostensivo das rodovias federais) e da Polícia Federal e ferroviária, com cerca de 5 questões; hoje a PRF mal aparece
- regime dos militares dos estados, DF e territórios (art. 42: organização, patentes, direitos, vedações) e questões; a matéria trata só das atribuições do art. 144
- normas gerais de organização, efetivos, material bélico, garantias, convocação e mobilização das PM e CBM (art. 22, XXI e art. 144, § 6º) e questões; só atribuições e subordinação estão cobertas
- capítulo sobre perfil constitucional e funções institucionais da Polícia Rodoviária Federal (patrulhamento ostensivo de rodovias federais, § 2º do art. 144) e questões
- atribuições da Polícia Federal em detalhe (art. 144, § 1º, I a IV: infrações contra a ordem política e social, tráfico, contrabando e descaminho, polícia marítima, aeroportuária e de fronteiras, exclusividade da polícia judiciária da União) com 8 questões
- parte de defesa do Estado e das instituições democráticas (estado de defesa, estado de sítio, Forças Armadas) e questões; a segurança pública já está coberta
- art. 42 (militares estaduais) e art. 125, §§ 4º e 5º (Justiça Militar estadual, competência para julgar militares e juiz de direito do juízo militar) com questões; o art. 144 está coberto
- atribuições da Polícia Rodoviária Federal (patrulhamento ostensivo de rodovias federais, fiscalização, atuação em acidentes) e questões específicas
- capítulo sobre Forças Armadas (art. 142: destinação, hierarquia, garantia da lei e da ordem) e comparação de competências com as polícias, além de questões

### 9. `controle-externo.intosai-issai` — INTOSAI, ISSAI e independência das EFS
Arquivo atual: `conteudo/materias/controle-externo/intosai-issai.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- ISSAIs 200 (auditoria financeira), 300 (operacional) e 400 (conformidade) com princípios próprios de cada uma, 10 questões
- ISSAI 20 (princípios de transparência e accountability das EFS), com 8 questões
- princípios fundamentais da auditoria financeira (ISSAI 200), com 6 questões
- princípios fundamentais da auditoria operacional (ISSAI 300): economicidade, eficiência, eficácia, com 8 questões
- norma para auditoria operacional (ISSAI 3000): planejamento, evidências, relatório, com 8 questões
- transparência e accountability das EFS (ISSAI 20), com 8 questões

### 10. `legislacao.identificacao-civil` — Identificação Civil Nacional e Carteira de Identidade Nacional
Arquivo atual: `conteudo/materias/legislacao/identificacao-civil.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- Decreto 11.797/2023 (Serviço de Identificação do Cidadão): finalidades, órgãos e fluxo de identificação, com questões.
- Lei 9.454/1997 (número único de Registro de Identidade Civil) e sua relação com a ICN e o CPF, com questões.
- Lei 7.116/1983 com Decreto 89.250/1983 e Lei 5.553/1968 (retenção e exibição de documentos de identificação), com questões.
- Lei 9.454/1997 e Decreto 7.166/2010 (Registro de Identidade Civil, cadastro nacional, número único), com questões.
- Lei 9.454/1997 (número único de Registro de Identidade Civil): instituição, cadastro nacional e atribuição do número, com questões.

### 11. `informatica.microsoft-excel-2016` — Microsoft Excel 2016
Arquivo atual: `conteudo/materias/informatica/microsoft-excel-2016.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre as principais funções por categoria (matemáticas, estatísticas, data e hora, financeiras, texto, lógicas e de busca) com exemplos e 8 questões
- Capítulo de Excel avançado: tabelas dinâmicas e gráficos dinâmicos, PROCV/PROCX e funções aninhadas, validação de dados, formatação condicional e análise de hipóteses, com 8 questões
- Capítulo/seção sobre SE, PROCV e SOMASE (sintaxe e erros comuns) e sobre validação de dados, com 6 questões; hoje só SOMASE e SE aparecem
- Seção sobre SE, E, OU, SOMASES, CONT.SES, PROCV, PROCX, ÚNICO, funções de erro (SEERRO, ÉERRO), de texto e de data, com 8 questões
- Seção sobre identificar e tratar valores em branco e duplicados (remover duplicatas, formatação condicional, filtros, funções de contagem) com 5 questões
- Seção sobre limpeza e transformação de dados (texto para colunas, remover duplicatas, funções de texto, preenchimento relâmpago, estatísticas descritivas) com 6 questões
- Capítulo sobre tabelas dinâmicas e gráficos dinâmicos (campos, valores, filtros, segmentação, atualização) com 8 questões

### 12. `matematica.funcoes` — Funções afim e quadrática
Arquivo atual: `conteudo/materias/matematica/funcoes.json` · 10 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre classificação de funções (injetora, sobrejetora, bijetora), função composta e função inversa, com exemplos e 8 questões
- Seção sobre estudo do sinal (onde a função é positiva, negativa ou nula) e resolução de inequações de 1º grau (zero da função, quadro de sinais), com 5 questões
- Seção sobre estudo do sinal (onde a função é positiva, negativa ou nula) e resolução de inequações de 2º grau (raízes, concavidade, intervalos), com 5 questões
- Inequações de 1º e 2º graus, equações/inequações exponenciais e logarítmicas e aprofundamento de exp/log; as funções trigonométricas pertencem à matéria de Trigonometria; 8 questões
- Seção sobre igualdade de funções (mesmo domínio, contradomínio e lei de formação) com 4 questões
- Seção sobre funções injetoras, sobrejetoras e bijetoras (definição, teste da reta horizontal, contradomínio) com 5 questões
- Seção sobre função inversa (condição de existência, cálculo da inversa de função afim, simetria dos gráficos) com 5 questões
- Seção sobre composição de funções (f∘g, domínio da composta, ordem da composição) com 5 questões
- Seção sobre funções pares e ímpares (simetria, f(-x)) e estudo detalhado de crescimento e decrescimento em intervalos, com 6 questões
- Seção sobre estudo do sinal (onde a função é positiva, negativa ou nula) e resolução de inequações de funções afim e quadrática (zeros e quadro de sinais) com 6 questões

### 13. `direito-administrativo.lindb-administracao-publica` — LINDB na Administração Pública (arts. 20 a 30)
Arquivo atual: `conteudo/materias/direito-administrativo/lindb-administracao-publica.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre a parte geral da LINDB (arts. 1º a 19: vigência, obrigatoriedade, antinomias, integração, direito adquirido, ato jurídico perfeito e coisa julgada, eficácia no espaço), com 10 questões
- Seção sobre a aplicação da LINDB (arts. 20, 22, 24 e 28) pelos Tribunais de Contas e órgãos de controle, com limites ao controle externo, e 6 questões
- Seção sobre responsabilidade do parecerista e do administrador que segue ou contraria o parecer (art. 28, parecer vinculante x facultativo, erro grosseiro, entendimento do STF) com 6 questões
- Seção sobre precedentes administrativos e súmulas administrativas (art. 30, caráter vinculante das respostas a consultas e regulamentos), com 5 questões

### 14. `orcamento-publico.gestao-patrimonial-contabil` — Gestão patrimonial e contábil na Lei 4.320/1964
Arquivo atual: `conteudo/materias/orcamento-publico/gestao-patrimonial-contabil.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre fatos contábeis na contabilidade pública: fatos permutativos, modificativos (aumentativos e diminutivos) e mistos, relação com variações patrimoniais e lançamentos de exemplo, com 8 questões
- classificação dos bens públicos, dívida pública fundada (consolidada) e flutuante, com conceitos e 8 questões
- princípios de contabilidade aplicados ao setor público (entidade, continuidade, oportunidade, competência, prudência), conceito de patrimônio público e variações patrimoniais quantitativas e qualitativas, com 8 questões
- variações patrimoniais quantitativas e qualitativas (aumentativas e diminutivas) e controle do patrimônio, com exemplos de lançamento e 8 questões
- comparativo entre contabilidade pública e empresarial (finalidade, regime, patrimônio, resultado, princípios) e divisão da contabilidade pública em ramos, com 6 questões
- bens públicos e sua classificação, dívida ativa (tributária e não tributária), dívida pública fundada e flutuante e fundos especiais (arts. 71 a 74), com 10 questões
- plano de contas (estrutura e finalidade do PCASP) e sistemas de escrituração (contas orçamentárias, financeiras, patrimoniais e de compensação), com 6 questões
- sistemas de contas na contabilidade pública (orçamentário, financeiro, patrimonial e de compensação) com registros e lançamentos típicos, com 6 questões

### 15. `historia.historia-geral` — História geral
Arquivo atual: `conteudo/materias/historia/historia-geral.json` · 10 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre ideias econômicas (mercantilismo, fisiocracia, liberalismo clássico de Adam Smith) e 4 questões
- seção sobre independências na América (América espanhola, Haiti) e 4 questões
- seção sobre liberalismo e nacionalismo no século XIX (unificações italiana e alemã) e 4 questões
- capítulo sobre a América pré-colombiana (maias, astecas, incas, povos nativos) e 5 questões
- seção sobre ideologias do século XIX: socialismo utópico e científico, anarquismo e doutrina social da Igreja, com 6 questões
- seção sobre período entreguerras, Segunda Guerra e globalização até os dias atuais, com 6 questões
- capítulo sobre pré-história e processo de humanização (paleolítico, neolítico, hominização) e 5 questões
- seção sobre Egito, Núbia, Kush, Mesopotâmia, Palestina, Fenícia e Pérsia, com 6 questões
- capítulo sobre reinos africanos do século V ao XV (Gana, Mali, Songai, Axum, Zimbábue) e 5 questões
- capítulo sobre sociedades americanas, africanas e asiáticas do século XVIII à contemporaneidade, com 8 questões

### 16. `geografia.biomas-gestao-ambiental-brasil` — Biomas e gestão ambiental no Brasil
Arquivo atual: `conteudo/materias/geografia/biomas-gestao-ambiental-brasil.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre poluição das vias hídricas e mudanças climáticas (causas, efeito estufa, políticas) e 5 questões
- seção sobre poluição, biocidas (agrotóxicos) e espécies ameaçadas de extinção no Brasil, com 5 questões
- capítulo sobre manejo florestal, política florestal, inventário florestal e viveiros, com 6 questões
- seção sobre fitofisionomias de cada bioma (Cerrado, Caatinga, Mata Atlântica) e evolução da fauna e flora, com 6 questões
- seção sobre fitofisionomias de cada bioma e evolução da fauna e flora, com 6 questões
- seção sobre ecossistemas aquáticos brasileiros (manguezais, rios, lagos, zona costeira) e 5 questões
- seção sobre integração do Brasil à economia internacional em relação à agenda ambiental, com 4 questões

### 17. `biologia.corpo-humano-saude` — Corpo humano e saúde
Arquivo atual: `conteudo/materias/biologia/corpo-humano-saude.json` · 10 tópicos de edital pedem mais conteúdo

Falta:
- seção ampliada sobre sistema nervoso (neurônios, sinapse, SNC e SNP, autônomo) e 6 questões
- seção sobre sistema locomotor (ossos, articulações, tipos de músculo, contração) e 6 questões
- seção sobre sistema tegumentar (pele, camadas, anexos, funções) e 5 questões
- seção ampliada sobre sistema nervoso (neurônios, sinapse, SNC e SNP, sistema nervoso autônomo) e 6 questões
- seção sobre anatomia e fisiologia comparada entre humanos e outros vertebrados, com 6 questões
- seção sobre sistema endócrino (glândulas, hormônios, retroalimentação, eixo hipotálamo-hipófise) e 6 questões
- seção sobre ciclo menstrual e controle hormonal (FSH, LH, estrogênio, progesterona) e 5 questões
- seção sobre sistema endócrino (glândulas, hormônios, retroalimentação) e 6 questões

### 18. `portugues.pronomes` — Emprego e colocação de pronomes
Arquivo atual: `conteudo/materias/portugues/pronomes.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Categorias nominais (gênero e número de substantivos e adjetivos) e categorias verbais (tempo, modo, voz, aspecto), com questões.
- Emprego de nomes (substantivo e adjetivo: gênero, número, grau, flexão) além dos pronomes, com questões.
- Categorias nominais (gênero e número) e verbais (tempo, modo, voz, aspecto), com questões.
