# Atualizar matérias — pedido 09 de 18 (09/10/2026)

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

### 1. `direito-administrativo.concessao-permissao` — Concessão e permissão de serviço público
Arquivo atual: `conteudo/materias/direito-administrativo/concessao-permissao.json` · 21 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre autorização de serviço público: natureza precária e discricionária, ato unilateral, ausência de licitação, diferença para permissão e para autorização de uso, com 6 questões
- seção sobre autorização de serviço público e permissão: ato unilateral, discricionário e precário, no interesse do particular, comparado ao contrato de adesão da permissão, com 6 questões
- capítulo sobre PPP (Lei 11.079/2004): concessão patrocinada e administrativa, contraprestação, garantias, SPE e diferença para a Lei 8.987, com 8 questões
- seção sobre permissão e autorização de serviço público: natureza jurídica, discricionariedade, precariedade e indenização, com 6 questões
- capítulo sobre PPP (Lei 11.079/2004): concessão patrocinada e administrativa, contraprestação pública, repartição de riscos, garantias, SPE, com 8 questões
- capítulo sobre formas de prestação do serviço público: prestação direta e indireta, outorga x delegação, centralização, descentralização e desconcentração, com 6 questões
- capítulos sobre PPP (Lei 11.079/2004) e Programa de Parcerias de Investimentos (Lei 13.334/2016): objetivos, regras principais e diferenças para a Lei 8.987, com 8 questões
- seção sobre prazo da concessão, prorrogação e renovação, e regras de outorga (art. 5º, 18 e 23 da Lei 8.987), com 6 questões
- seção sobre conceito, classificação dos serviços públicos e princípios (continuidade, mutabilidade, generalidade, modicidade) e 6 questões
- seção sobre autorização de serviço público: ato unilateral, discricionário e precário, sem licitação, diferença para permissão, com 6 questões
- capítulo sobre PPP (Lei 11.079/2004) e comparação com a concessão comum da Lei 8.987, com 8 questões
- capítulos sobre PPP (patrocinada e administrativa), agências reguladoras na regulação das concessões e controle das desestatizações pelos tribunais de contas, com 10 questões

### 2. `direito-civil.lei-vigencia-aplicacao-integracao-interpretacao` — Lei: vigência, aplicação no tempo e no espaço, integração e interpretação
Arquivo atual: `conteudo/materias/direito-civil/lei-vigencia-aplicacao-integracao-interpretacao.json` · 17 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre métodos de interpretação (gramatical, lógico, sistemático, histórico, teleológico), efeitos (declarativa, restritiva, extensiva) e interpretação autêntica, doutrinária e judicial, com 5 questões
- seção sobre métodos e espécies de interpretação da norma (gramatical, sistemático, teleológico, histórico; declarativa, restritiva, extensiva) e sobre as formas de integração, com 5 questões
- capítulo introdutório sobre conceito e divisão do direito civil, fontes do direito, norma jurídica e visão geral do Código Civil, com 5 questões
- capítulo sobre teoria da norma jurídica: conceito, caracteres, espécies (cogentes e dispositivas, gerais e especiais), hierarquia e fontes, com 5 questões
- seção distinguindo ab-rogação (revogação total) de derrogação (revogação parcial), além de revogação expressa e tácita, com 3 questões
- seção sobre eficácia da norma, conflito de leis (antinomias) e métodos de interpretação, com 5 questões
- seção sobre antinomias aparentes e reais e critérios de solução (hierárquico, cronológico, especialidade) e conflito entre critérios, com 5 questões
- seção sobre equidade (art. 140 do CPC, julgamento por equidade), sua distinção em relação à analogia e aos princípios gerais, com 4 questões
- seção sobre analogia em profundidade (analogia legis e iuris, vedação em normas penais incriminadoras e restritivas) e métodos de interpretação, com 4 questões
- seção sobre princípios gerais do direito e equidade como critérios de decisão, com exemplos e 4 questões
- capítulo sobre teoria geral do direito internacional privado: objeto, elementos de conexão, qualificação, reenvio, ordem pública e normas internacionais, com 5 questões
- seção sobre as fontes formais do direito: lei, analogia, costumes, jurisprudência, princípios gerais e equidade, com a hierarquia entre eles e 4 questões

### 3. `administracao-publica.atendimento-ao-publico` — Atendimento ao público
Arquivo atual: `conteudo/materias/administracao-publica/atendimento-ao-publico.json` · 23 tópicos de edital pedem mais conteúdo

Falta:
- seção que defina e exemplifique os atributos clássicos do bom atendimento (comunicabilidade, apresentação pessoal, atenção, cortesia, interesse, presteza, eficiência, tolerância, discrição, conduta e objetividade), com 10 questões que cobrem cada atributo
- seção sobre etiqueta no atendimento (comportamento, aparência, postura, cuidados no atendimento pessoal e telefônico) e 5 questões
- seção sobre apresentação pessoal e ambiente (vestuário, higiene, identificação funcional, organização do posto de trabalho), com 3 questões
- seção sobre comunicação institucional (imagem da instituição, canais oficiais, linguagem institucional, relação com a imprensa e com o cidadão) e 5 questões
- seção sobre atendimento ao público interno (colegas e outros setores) e externo (cidadão), diferenças de postura e fluxo de demandas, com 5 questões
- seção sobre atendimento telefônico e telemarketing (saudação, tom de voz, tempo de espera, transferência de ligações, registro do contato) e 5 questões
- seção sobre atendimento por canais remotos (telefone, chat, e-mail, videoatendimento, autoatendimento): cuidados de linguagem, tempo de resposta, segurança dos dados e registro, com 5 questões
- seção sobre comunicabilidade como atributo do atendimento (facilidade de se fazer entender, linguagem acessível, canais de contato), com 3 questões
- seção sobre interesse como atributo do atendimento (demonstrar disposição real em resolver a demanda, acompanhar até a solução), com 3 questões
- seção sobre presteza como atributo do atendimento (agilidade e disposição em atender, cumprimento de prazos, informação sobre andamento), com 3 questões
- seção sobre tolerância como atributo do atendimento (lidar com pessoas exaltadas, diferenças culturais e demora na compreensão sem perder a cortesia), com 3 questões
- seção sobre atendimento ao público interno (colegas e outros setores) e externo (cidadão) e sobre os atributos de qualidade (presteza, tolerância, discrição, comunicabilidade), com 8 questões

### 4. `conhecimentos-bancarios.mercado-cambio-monetario` — Mercado de câmbio e monetário
Arquivo atual: `conteudo/materias/conhecimentos-bancarios/mercado-cambio-monetario.json` · 21 tópicos de edital pedem mais conteúdo

Falta:
- instituições autorizadas a operar em câmbio (bancos, corretoras, distribuidoras, cooperativas, agências de turismo) e operações básicas (exportação, importação, remessas, câmbio manual, taxa de referência), com 8 questões
- visão geral dos mercados de crédito e de capitais (instrumentos, agentes e funcionamento) em conjunto com o monetário e o cambial, com 8 questões
- curva de juros (estrutura a termo, inclinação e interpretação), taxas de juros de curto prazo e distinção entre taxa nominal e real, com 8 questões
- operações de tesouraria bancária (gestão de caixa e liquidez, captação, mesa de operações, risco de mercado e reservas), com 6 questões
- política monetária não convencional (quantitative easing), operações compromissadas e debate sobre depósitos remunerados dos bancos no Banco Central, com 10 questões
- operações compromissadas (mecânica, lastro e uso na política monetária) em complemento à taxa Selic, com 5 questões
- política monetária não convencional (quantitative easing, forward guidance, juros negativos) em contraste com a convencional, com 8 questões
- operações básicas de câmbio (compra e venda de moeda, exportação e importação, adiantamentos de contrato, remessas e câmbio manual), com 8 questões
- contrato de câmbio: características, elementos, tipos (pronto e futuro), registro, liquidação e responsabilidades, com 6 questões
- remessas internacionais (enviadas e recebidas): vias, documentação, tributos e limites, com 6 questões
- SISCOMEX: conceito, módulos de exportação e importação, registro de operações e relação com o câmbio, com 5 questões
- operações compromissadas (definição, lastro, prazo, uso pelo Banco Central e diferença para o depósito voluntário), com 6 questões

### 5. `economia.federalismo-fiscal-tributacao` — Federalismo fiscal e tributação
Arquivo atual: `conteudo/materias/economia/federalismo-fiscal-tributacao.json` · 15 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre classificação dos impostos indiretos (ad valorem, específicos, IVA, imposto único/excise) e 5 questões
- seção sobre eficiência de Pareto aplicada à tributação, com 4 questões
- seção sobre a regra de Ramsey e os critérios de equidade (capacidade contributiva, benefício), com 6 questões
- seção sobre incidência de impostos em concorrência perfeita e monopólio, com 5 questões
- seção sobre efeitos dos impostos nas decisões de consumo, poupança e trabalho, com 5 questões
- seção sobre o trade-off entre eficiência e equidade, com 5 questões
- seção sobre a necessidade econômica da tributação e as formas de tributação (impostos, taxas, contribuição de melhoria), com 5 questões
- seção sobre financiamento dos gastos públicos, tributação e equidade, com 5 questões
- seção sobre conceitos, objetivos e abrangência das finanças públicas e financiamento do gasto, com 6 questões
- seção sobre a necessidade econômica da tributação, com 4 questões
- seção sobre incidência e efeitos distributivos dos impostos sobre riqueza, patrimônio e propriedade, com 5 questões
- seção sobre o modelo de Tiebout e o federalismo ótimo (vantagens e desvantagens da descentralização), com 6 questões

### 6. `informatica.internet-navegadores` — Internet e navegadores
Arquivo atual: `conteudo/materias/informatica/internet-navegadores.json` · 13 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre portais corporativos e as características físicas e lógicas dos ambientes internet, intranet e extranet (servidores, VPN, firewall, controle de acesso) e 5 questões
- seção sobre surface web, deep web e dark web (rede Tor, endereços .onion, riscos e uso legítimo) e 5 questões
- capítulo sobre o Internet Explorer em si (menus, Opções da Internet, filtro SmartScreen, modo de compatibilidade) e 6 questões; hoje só há o modo IE do Edge
- seção sobre o funcionamento da internet (provedores, protocolos TCP/IP, DNS, cliente-servidor) e 5 questões
- capítulo sobre o Internet Explorer: busca, salvar páginas, cache e Opções da Internet, com 6 questões
- seção sobre o Mozilla Thunderbird (cliente de e-mail) e correio eletrônico em geral e 5 questões
- seção sobre portais e as características físicas e lógicas de internet, intranet e extranet (servidores, VPN, firewall, controle de acesso) e 5 questões
- seção sobre upload de arquivos (diferença para download, formulários e limites) e 3 questões
- seções sobre correio eletrônico e redes sociais (hoje não cobertos nesta matéria; ver também informatica.internet-busca-redes-sociais), com 6 questões
- seções sobre correio eletrônico, redes sociais e ferramentas colaborativas e 6 questões
- seção sobre o Mozilla Thunderbird e o Internet Explorer 11 completo (hoje só modo IE no Edge), com 6 questões
- capítulo sobre o navegador Safari (recursos, navegação privada, prevenção de rastreamento, sincronização) e 6 questões

### 7. `direito-tributario.credito-lancamento` — Crédito tributário e lançamento
Arquivo atual: `conteudo/materias/direito-tributario/credito-lancamento.json` · 16 tópicos de edital pedem mais conteúdo

Falta:
- seção com as disposições gerais do CTN sobre crédito (arts. 139 a 141: relação com a obrigação, natureza, circunstâncias que o modificam) e o art. 142 (conceito de lançamento, atividade vinculada e obrigatória), com 5 questões
- seção sobre as hipóteses de alteração do lançamento (art. 145 do CTN: impugnação, recurso de ofício, iniciativa de ofício) e de revisão de ofício do art. 149, com 6 questões
- seção sobre alteração do lançamento (art. 145 do CTN e hipóteses de revisão de ofício do art. 149), com 6 questões
- seção sobre a natureza do crédito tributário (arts. 139 a 141 do CTN: nasce da obrigação, circunstâncias que o modificam não afetam sua natureza) e a natureza declaratória do lançamento, com 4 questões
- seção sobre a revisão do lançamento (art. 149 do CTN: lista de hipóteses, limite pelo prazo decadencial, impossibilidade de revisão por mudança de critério jurídico do art. 146), com 6 questões
- seção sobre alterações do crédito (art. 145 do CTN) e vedação de alteração do critério jurídico (art. 146), com 5 questões
- capítulo sobre natureza jurídica do lançamento (declaratória x constitutiva, art. 142 do CTN), efeitos (art. 144, lei aplicável) e características (ato vinculado, obrigatório), com 6 questões
- capítulo sobre conceito e natureza do crédito (arts. 139 a 142 do CTN) e sobre a revisão (arts. 145 e 149), com 6 questões
- seção sobre alteração do lançamento (arts. 145 e 149 do CTN), com 5 questões
- seção sobre a natureza do crédito tributário (arts. 139 a 141 do CTN) e do lançamento, com 4 questões
- seção sobre revisão do lançamento (art. 149 do CTN: hipóteses, limite decadencial, art. 146), com 6 questões
- seção sobre efeitos do lançamento (art. 144 do CTN: lei vigente à época do fato gerador, exceções) e hipóteses de alteração (arts. 145, 146 e 149), com 6 questões

### 8. `raciocinio-logico.problemas-aritmeticos` — Problemas aritméticos, geométricos e matriciais
Arquivo atual: `conteudo/materias/raciocinio-logico/problemas-aritmeticos.json` · 18 tópicos de edital pedem mais conteúdo

Falta:
- capítulo de análise combinatória (arranjos, permutações e combinações) e noções de probabilidade, com 10 questões
- leitura e interpretação de gráficos (barras, setores, linhas e histogramas) com cálculo de variações e percentuais, com 8 questões
- operações com conjuntos (união, interseção, diferença, diagramas de Venn e problemas de cardinalidade), com 8 questões
- juros simples e compostos básicos (montante, taxa e prazo) em problemas, com 8 questões
- contagem além do princípio fundamental: arranjos, permutações e combinações simples, com 8 questões
- problemas de códigos e cifras (substituição de letras e números, cifra de César, codificação por matrizes com inversa de matriz 2x2), com 6 questões
- sistemas de equações do 1º grau e equação do 2º grau aplicados a problemas, com 8 questões
- contagem (arranjos, permutações e combinações), probabilidade e operações com conjuntos, com 12 questões
- problemas resolvidos de trás para frente (regressão com operações inversas), com 6 questões

### 9. `direito-civil.contratos` — Contratos em geral
Arquivo atual: `conteudo/materias/direito-civil/contratos.json` · 20 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre arras (confirmatórias e penitenciais, arts. 417 a 420 do CC) e 4 questões
- capítulo sobre contratos mercantis/empresariais (características, paridade, risco, boa-fé) e 4 questões
- seções sobre dirigismo e intervenção estatal, contrato e propriedade e pós-eficácia contratual, com 5 questões
- seção sobre tendências do direito contratual (paridade e simetria, revisão) e a Lei da Liberdade Econômica (Lei 13.874/2019) nos contratos, com 4 questões
- seção sobre contrato com pessoa a nomear (terminologia) e gestão de negócios (arts. 861 a 875 do CC), com 4 questões
- seção sobre atos unilaterais (promessa de recompensa, gestão de negócios, pagamento indevido, enriquecimento sem causa) e 5 questões; contratos em espécie estão em outra matéria
- capítulo sobre contratos empresariais: noções, requisitos, formação e meios de prova, com 5 questões
- capítulo sobre contratos aplicados aos setores público e privado (diferenças de regime, equilíbrio econômico-financeiro) e 4 questões
- capítulo sobre a teoria dos contratos na atividade empresarial (contratos empresariais, paridade, risco) e 4 questões
- seção sobre negociações preliminares, responsabilidade pré-contratual e pré-contrato, com 4 questões
- capítulo sobre contratos de empresas (requisitos, formação, prova) e sua relação com compra e venda e prestação de serviços, com 5 questões
- seção sobre atos unilaterais (promessa de recompensa, gestão de negócios, enriquecimento sem causa) e 4 questões

### 10. `direito-consumidor.relacoes-consumo` — Relações de consumo: consumidor, fornecedor e responsabilidade
Arquivo atual: `conteudo/materias/direito-consumidor/relacoes-consumo.json` · 23 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre natureza, fontes e características do CDC (microssistema, norma de ordem pública e interesse social, aplicação do CDC e diálogo com o Código Civil, princípios) e 5 questões
- seção sobre fundamentos constitucionais da defesa do consumidor (art. 5º, XXXII; art. 170, V; art. 48 do ADCT), natureza de norma de ordem pública e interesse social e relação do CDC com o Código Civil (diálogo das fontes), com 5 questões
- seção sobre a desconsideração da personalidade jurídica no CDC (art. 28, hipóteses e teoria menor) e 4 questões; decadência e prescrição já têm cobertura
- seção sobre relações de consumo na internet e comércio eletrônico (Decreto 7.962/2013, dever de informação, arrependimento, fornecedor virtual e plataformas) e 5 questões
- seção sobre princípios do CDC e boa-fé objetiva (função de interpretação, controle e integração; vulnerabilidade; equilíbrio contratual) e 5 questões
- seção com súmulas e precedentes do STJ e STF em direito do consumidor (ex.: aplicação do CDC a bancos, seguros, planos de saúde, responsabilidade de comerciante), com 8 questões
- seção sobre serviços públicos no CDC (art. 22: contínuos, adequados, eficientes; remuneração por tarifa x taxa; corte do fornecimento) e 4 questões
- seção sobre distinção entre acidente de consumo (fato) e vício (incidente), com a teoria dos vícios redibitórios do Código Civil, e 4 questões
- seção sobre a incidência do CDC em serviços públicos, atividade bancária, securitária, imobiliária e transporte aéreo (súmulas do STJ, Convenções internacionais), com 6 questões
- seção sobre vulnerabilidade técnica, jurídica, fática e informacional e hipervulnerabilidade (idosos, crianças, doentes, analfabetos), com 4 questões

### 11. `matematica.juros-simples-compostos` — Juros simples e compostos
Arquivo atual: `conteudo/materias/matematica/juros-simples-compostos.json` · 15 tópicos de edital pedem mais conteúdo

Falta:
- visão geral de cálculo financeiro no nível do contador: valor do dinheiro no tempo, VP e VF, fluxos de caixa e ligação com descontos e séries, com 6 questões
- capítulo de conceitos: valor do dinheiro no tempo, valor presente e valor futuro, fluxo de caixa, taxa e prazo da operação, com 6 questões
- capitalização contínua (M = C·e^(it)) e comparação com capitalização simples e composta, com 4 questões
- isolar principal, taxa e prazo no regime composto (uso de tabela financeira e de logaritmos), com exemplos resolvidos e 6 questões
- conceito de valor do dinheiro no tempo e de carência (período sem pagamento e incorporação de juros ao principal), com 5 questões
- isolar taxa, principal e prazo no regime composto (tabela financeira e logaritmos), com exemplos e 6 questões
- precificação e operações com títulos públicos federais (valor presente de LTN e NTN, taxa de juros e preço), com 5 questões
- conceitos fundamentais: valor do dinheiro no tempo, VP e VF, fluxo de caixa e regimes de capitalização, com 6 questões
- capitalização mista (parte inteira composta e parte fracionária simples) e contínua, com 5 questões
- valor presente e valor futuro: cálculo nos regimes simples e composto e interpretação em fluxo de caixa, com 5 questões
- uso de tabelas financeiras de fator de acumulação (1+i)^n e cálculo de principal, taxa e prazo com elas, com 6 questões
- atualização de títulos e de débitos em perícia: correção monetária e índices, juros de mora (simples e compostos), imputação de pagamentos (juros antes do principal) e planilhas de atualização, com 6 questões

### 12. `direito-urbanistico.estatuto-cidade` — Política urbana, Estatuto da Cidade, parcelamento do solo e Reurb
Arquivo atual: `conteudo/materias/direito-urbanistico/estatuto-cidade.json` · 23 tópicos de edital pedem mais conteúdo

Falta:
- Seção sobre ética ambiental e meio ambiente urbano (cidades sustentáveis como diretriz do art. 2º, I, equilíbrio ecológico, EIV x EIA, função socioambiental da propriedade) e 4 questões.
- Seção sobre cidades sustentáveis: direito às cidades sustentáveis no art. 2º, I, ODS 11, Nova Agenda Urbana, mobilidade e saneamento e sua relação com o plano diretor, com 4 questões.
- Seção sobre parcelamento do solo rural (Decreto-Lei 58/1937, módulo rural, fracionamento mínimo, regra do INCRA) e sobre aspectos coletivos do parcelamento urbano (ação civil pública), com 6 questões; hoje só há o parcelamento urbano.
- Capítulo aprofundando a regularização fundiária (Lei 13.465/2017): Reurb-S e Reurb-E, legitimação fundiária e de posse, CRF, procedimento, atuação do Ministério Público e papel do município, com 8 questões; hoje é só em noções.
- Seção sobre ocupações irregulares: loteamento clandestino e irregular, responsabilidades civil, administrativa e penal (crimes da Lei 6.766), ação civil pública, regularização e áreas de risco/preservação, com 6 questões.
- Seções sobre gerenciamento costeiro (Lei 7.661/1988, Plano Nacional, faixa de praia, terrenos de marinha) e sobre patrimônio histórico e cultural (tombamento, IPHAN, entorno), além do parcelamento do solo, com 8 questões.
- Capítulo sobre planejamento urbano e uso do solo sob o ângulo técnico: zoneamento, parâmetros de uso e ocupação, sistema viário, áreas públicas e projeto urbano, com 6 questões.
- Seção sobre instrumentos de gestão urbana e ambiental: EIA/RIMA, licenciamento ambiental, instrumentos econômicos e administrativos e sua relação com plano diretor e EIV, com 6 questões.
- Seção sobre parcelamento do solo rural (módulo rural, fracionamento mínimo, loteamento rural e chácaras de recreio, registro) ao lado do urbano, com 6 questões.
- Seção sobre o Decreto-Lei 58/1937: loteamento e venda de terrenos a prestações, inscrição do loteamento, compromisso de compra e venda e parcelamento do solo rural, com 5 questões.
- Seção sobre a legislação estadual de parcelamento do solo (Lei Estadual 17.492/2018) e sua relação com a Lei 6.766/1979 (competência concorrente), com 5 questões.
- Capítulo de planejamento urbano e estruturação do espaço urbano (forma urbana, morfologia, uso do solo, sistemas viário, de áreas verdes e de equipamentos, planejamento físico-territorial), com 6 questões.

### 13. `direito.filosofia-sociologia-juridica` — Filosofia e sociologia jurídica
Arquivo atual: `conteudo/materias/direito/filosofia-sociologia-juridica.json` · 17 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre conceito de justiça e teorias da justiça (Aristóteles: justiça distributiva e corretiva; Rawls; justiça procedimental; sentido lato e estrito) e 6 questões
- Capítulo sobre a lógica do razoável de Recaséns Siches e crítica ao raciocínio lógico-dedutivo, com 5 questões
- Capítulo sobre a superação da interpretação puramente lógico-dedutiva (escola da exegese, jurisprudência dos interesses, lógica do razoável) e 5 questões
- Capítulo sobre teoria geral da política (Estado, poder, legitimidade, formas de governo) e sua relação com o direito, com 5 questões
- Capítulo sobre Dworkin e o direito como integridade (romance em cadeia, juiz Hércules, resposta correta), com 5 questões
- Capítulo sobre realismo jurídico (norte-americano e escandinavo) e crítica ao formalismo, com 5 questões
- Capítulo sobre os grandes filósofos e a justiça (Platão, Aristóteles, Kant, Rawls, utilitarismo) e 6 questões
- Capítulo sobre a lógica do razoável de Recaséns Siches, com 5 questões
- Capítulo sobre igualdade, equidade, liberdade, dignidade e bem comum como valores jurídicos, com 5 questões

### 14. `portugues.termos-da-oracao` — Termos da oração
Arquivo atual: `conteudo/materias/portugues/termos-da-oracao.json` · 12 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre ordem direta e inversa (inversão do sujeito, anástrofe, efeitos sobre a análise) e 5 questões
- capítulo sobre período composto por coordenação e subordinação e relações sintático-semânticas entre orações, com 8 questões
- capítulo sobre funções sintáticas no período composto, com 6 questões
- seção sobre ordem direta e inversa e sobre termos e orações do período, com 6 questões
- seções sobre crase e sobre emprego, formas de tratamento e colocação dos pronomes, com 8 questões
- seção sobre ordem direta e inversa e relação entre termos e orações, com 6 questões
- seção sobre crase com 5 questões
- seções sobre crase e emprego dos pronomes, com 8 questões
- capítulo sobre relações sintático-semânticas entre orações, períodos e parágrafos, com 6 questões
- seção sobre ordem dos termos e foco informativo em manchetes e textos jornalísticos, com 5 questões

### 15. `direito-tributario.garantias-administracao-tributaria` — Garantias, administração tributária e certidões
Arquivo atual: `conteudo/materias/direito-tributario/garantias-administracao-tributaria.json` · 16 tópicos de edital pedem mais conteúdo

Falta:
- dívida ativa não tributária da União: inscrição e cobrança pela Procuradoria competente (art. 39 da Lei 4.320/1964 e Lei 6.830/1980), créditos que a compõem, diferenças de prescrição e de privilégio em relação à tributária, e 4 questões
- cadastro fiscal: inscrição, alteração e baixa no CPF/CNPJ e no cadastro de contribuintes, deveres cadastrais e sua relação com a emissão de certidões, com 3 questões
- capítulo sobre a LC 105/2001: sigilo das operações de instituições financeiras, hipóteses legais de quebra, acesso do fisco a dados bancários sem autorização judicial (ADIs 2390 e 2859 e RE 601.314), diferença para o sigilo fiscal do art. 198 do CTN, e 5 questões
- pagamento indevido e restituição/repetição do indébito (arts. 165 a 169 do CTN): hipóteses, legitimidade e tributo indireto (art. 166), prazo de cinco anos, juros e ação anulatória da decisão denegatória, com 5 questões
- execução fiscal (Lei 6.830/1980: petição inicial e CDA, citação, penhora e ordem de bens, embargos, exceção de pré-executividade, prescrição intercorrente, redirecionamento) e medida cautelar fiscal (Lei 8.397/1992: cabimento, requisitos, indisponibilidade de bens), com 8 questões
- dívida ativa não tributária da União (art. 39 da Lei 4.320/1964 e Lei 6.830/1980) e distinção entre dívida ativa e dívida pública (crédito público), com 4 questões
- poder de polícia fiscal: conceito (art. 78 do CTN), suas manifestações (fiscalização, interdição, apreensão, multas), limites e sanções políticas (Súmulas 70, 323 e 547 do STF), com 4 questões
- apreensão de mercadorias e documentos como ato de fiscalização: hipóteses, termo de apreensão e depósito, devolução, limites constitucionais e vedação de apreensão como meio de cobrança (Súmula 323 do STF), com 4 questões
- excesso de exação (art. 316 do Código Penal) e responsabilidade pessoal/funcional do agente fiscal por abuso ou excesso na fiscalização, com 3 questões
- substituição tributária: "para frente" e "para trás", base legal (art. 128 do CTN e art. 150, § 7º, da CF), restituição se o fato gerador presumido não ocorrer, e 4 questões (ou ligar também a direito-tributario.responsabilidade-tributaria)
- preço público (tarifa) x taxa: natureza contratual e não tributária, critérios de distinção e Súmula 545 do STF, com 3 questões
- cadastros fiscais e de inadimplentes (CPF/CNPJ, cadastro de contribuintes, CADIN) e sua relação com a emissão de certidões, com 3 questões

### 16. `direito-administrativo.procedimento-licitatorio` — Procedimento licitatório, anulação, revogação e recursos
Arquivo atual: `conteudo/materias/direito-administrativo/procedimento-licitatorio.json` · 19 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a fase preparatória: estudo técnico preliminar (conteúdo, dispensa, finalidade) e 6 questões
- seção sobre termo de referência (conteúdo, elementos do art. 6º, relação com o ETP) e 6 questões
- seção sobre habilitação (arts. 62 a 70: jurídica, fiscal, social e trabalhista, técnica, econômico-financeira) e 8 questões
- capítulo sobre estudo técnico preliminar (elementos do art. 18 §1º, dispensa, finalidade) e 6 questões
- seção sobre o panorama de sanções (art. 156) e crimes em licitações (Código Penal, arts. 337-E a 337-P) com 6 questões
- seção sobre projeto básico e projeto executivo (conteúdo, anteprojeto, obras e serviços de engenharia) e 6 questões
- seção sobre habilitação e impedimentos (art. 14: vedações de participação; arts. 62 a 70: requisitos de habilitação) e 8 questões
- seção sobre aplicação do procedimento a obras e serviços de engenharia (projeto básico, regimes de execução, orçamento, BDI) e 6 questões
- seção sobre os artefatos de planejamento (ETP, termo de referência, projeto básico) e sua sequência na fase preparatória, com 8 questões
- seção sobre julgamento objetivo das propostas, desclassificação e critérios de desempate (art. 60) com 6 questões
- seção sobre habilitação (jurídica, regularidade fiscal, social e trabalhista, qualificação técnica, econômico-financeira, arts. 62 a 70) e 8 questões
- seção sobre termo de referência e projeto básico (conteúdo, diferenças, aplicação a bens, serviços e obras) e 6 questões

### 17. `portugues.ortografia` — Ortografia oficial: emprego das letras
Arquivo atual: `conteudo/materias/portugues/ortografia.json` · 15 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o emprego do hífen conforme o Acordo Ortográfico (prefixos, palavras compostas e locuções) e 8 questões; a acentuação gráfica já fica em matéria própria
- alterações do Acordo Ortográfico em bloco: alfabeto, trema, regras do hífen e acentuação de ditongos e hiatos, com 8 questões
- notações léxicas (hífen, apóstrofo, til) e emprego do hífen conforme o Acordo, com 6 questões
- regras do hífen e mudanças de acentuação trazidas pela Reforma Ortográfica, com 8 questões
- hífen e divisão silábica (separação de sílabas, encontros vocálicos e consonantais, dígrafos), com 8 questões
- expressões latinas comuns na redação oficial (grafia, sentido e uso correto de locuções como ad hoc, data venia, in loco) e gramática aplicada à redação, com 6 questões

### 18. `direito.lei-4657-setembro-1942` — Lei nº 4.657, de 4 de setembro de 1942: LINDB, arts. 1º a 6º
Arquivo atual: `conteudo/materias/direito/lei-4657-setembro-1942.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre arts. 7º a 19 da LINDB (lei do domicílio, estatuto pessoal, bens, obrigações, sucessão, pessoa jurídica, homologação de sentença estrangeira) e arts. 20 a 30, com 10 questões
- conflito intertemporal em cada ramo: irretroatividade penal e retroatividade benéfica, direito constitucional (emenda x direito adquirido), direito do trabalho (norma mais benéfica e contrato em curso), com 6 questões
- arts. 7º a 19 da LINDB (direito internacional privado) para o nível de Direito Civil, com 8 questões
- eficácia da lei no espaço: territorialidade moderada, extraterritorialidade e regras de conexão a partir do art. 7º, com 5 questões
