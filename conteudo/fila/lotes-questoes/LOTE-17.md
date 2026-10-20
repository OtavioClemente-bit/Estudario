# Pedidos de questões extras, lote 17 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 3982 tópicos de edital.

## Mensagem 0 (regras: cole uma vez)

```
Você vai escrever QUESTÕES EXTRAS para matérias de concursos públicos brasileiros que já existem no app Estudário. Não escreva teoria. Em cada mensagem eu passo UMA matéria: o id, os capítulos (com quantas questões quero em cada um) e o começo dos enunciados que já existem, para você não repetir. Responda com UM JSON completo e válido, sem nada antes ou depois:

{
  "materia": "<o id que eu passar>",
  "questions": [ ...exatamente 100 questões... ]
}

DISTRIBUIÇÃO (o conferidor recusa fora disso):
- Exatamente 100 questões: 50 TRUE_FALSE (Certo/Errado) e 50 MULTIPLE_CHOICE com 5 alternativas (A a E).
- Em cada capítulo, a quantidade que eu pedir, metade Certo/Errado e metade A a E (se for ímpar, tanto faz qual sobra).
- Dificuldade: em cada capítulo, cerca de 1/3 FACIL, 1/3 MEDIA e 1/3 DIFICIL, nos dois formatos. O aluno pode pedir "só difícil em Certo/Errado" e tem que haver.
- Não cite banca: são questões autorais no estilo das provas (misture os jeitos de cobrar de Cebraspe, FGV, FCC, Vunesp, Cesgranrio, Ibfc, Idecan e bancas regionais).

CADA QUESTÃO:
{
  "statement": "<enunciado, 20 a 4000 caracteres>",
  "format": "MULTIPLE_CHOICE" ou "TRUE_FALSE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [ {"key":"A","text":"...","correct":false,"comentario":"<por que esta alternativa está certa ou errada, 1 a 2 frases>"}, ... ],
  "explanation": "<mínimo 150 caracteres: o fundamento da resposta, ensinando o conceito>",
  "section": "<título EXATO do capítulo, copiado do que eu passar>",
  "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
}
- TRUE_FALSE: opções exatamente {"key":"C","text":"Certo","correct":...} e {"key":"E","text":"Errado","correct":...}, sem "comentario". Metade das C/E de cada capítulo com gabarito Certo e metade Errado, mais ou menos.
- MULTIPLE_CHOICE: 5 alternativas A a E, TODAS com "comentario" (o app usa isso para montar a versão A a D tirando uma errada).
- Exatamente UMA opção correta. Sem alternativas repetidas. Nenhum enunciado igual ou quase igual a outro, nem aos que já existem e eu listar.
- A letra correta espalhada entre A, B, C, D e E (cada letra entre 15% e 25% das vezes). A correta NÃO pode ser a mais longa em mais de 40% das questões.
- TAMANHO: a correta deve ter o MESMO tamanho das erradas. Ela não pode ser a mais longa em mais de 30% das questões NEM a mais curta em mais de 30% (no lote anterior várias vieram com a correta sempre curtinha, tipo "Crônica.", e foram recusadas). Em questão numérica, varie a quantidade de dígitos dos distratores.
- EXPLICAÇÃO PRÓPRIA: cada explicação fala só da sua questão. PROIBIDO terminar explicações com a mesma frase de fechamento (nenhuma frase pode aparecer em mais de 3 explicações). Em cálculo, mostre a conta.
- Em Certo/Errado, a explicação nunca usa as palavras "alternativa" ou "opção".
- As 5 alternativas têm que ser diferentes entre si (não vale diferir só em maiúscula). Cada alternativa é uma frase única, sem trecho solto depois do ponto final.
- NÃO escreva "Análise das opções", "Gabarito B" nem comentários por letra ("A) Incorreta...") dentro de "explanation": a explicação traz só o fundamento; o comentário de cada alternativa vai no campo "comentario" dela (o app troca as letras na versão A a D, e a letra escrita no texto ficaria errada).
- Item Certo/Errado NÃO pode copiar o texto de uma alternativa de outra questão (nem das já existentes): escreva a afirmação com redação própria.
- Cada "comentario" com pelo menos uma frase explicativa (não só "Errada.").
- Varie o tipo: conceito, caso prático, "assinale a incorreta", assertivas I, II e III, exceção, comparação, letra da lei. Não comece várias questões com a mesma frase de molde.
- Alternativas erradas PLAUSÍVEIS: erros que um candidato que estudou mal cometeria (troca de prazo, de órgão, de conceito vizinho, exceção virando regra). Nada absurdo ou caricato, como "publicar o nome do paciente" ou "eliminar toda fiscalização", que se elimina sem saber a matéria.
- A questão tem que cobrar o assunto DO CAPÍTULO indicado em "section", não de outro capítulo.
- Se não tiver certeza de número de artigo, lei, prazo ou súmula, não cite o número: cobre o conteúdo. Nunca invente norma, jurisprudência ou dado.
- Certo/Errado no estilo Cebraspe: afirmação única, precisa, com o erro (quando houver) num detalhe técnico, não em palavra absurda.
- Português do Brasil, nível de prova de concurso real.
```

## Pedido 1: Fundeb: financiamento da educação básica (EC 108/2020 e Lei 14.113/2020) (`orcamento-publico.fundeb-lei-14113`, 86 tópicos de edital)

```
Matéria: orcamento-publico.fundeb-lei-14113 (Fundeb: financiamento da educação básica (EC 108/2020 e Lei 14.113/2020))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, composição e complementação da União": 34
- "2. Distribuição e aplicação dos recursos": 33
- "3. Fiscalização, controle social e transparência": 33

Já existem (não repita):
- Com a Emenda Constitucional 108/2020, o Fundeb tornou-se permanente, passando a ter previs
- Segundo a Lei 14.113/2020, o Fundeb é:
- A instituição do Fundeb isenta Estados e Municípios da obrigação de aplicar o mínimo const
- Cada fundo, no âmbito de cada Estado e do Distrito Federal, é composto por que percentual 
- Integram a cesta de receitas que compõe o Fundeb, entre outras, o ICMS, o IPVA e o ITCMD.
- A complementação da União aos recursos do Fundeb será equivalente a, no mínimo:
- A complementação-VAAR, de 2,5 pontos percentuais, destina-se às redes públicas que cumpram
- A modalidade de complementação da União que é distribuída a cada rede pública de ensino, m
- É permitido à União utilizar os recursos da contribuição social do salário-educação para f
- Para cumprir sua aplicação mínima constitucional em manutenção e desenvolvimento do ensino
- O não cumprimento, pela União, do dever de complementar os recursos do Fundeb importa crim
- No âmbito de cada Estado, os recursos do Fundeb são distribuídos entre o governo estadual 
- Os recursos dos fundos devem ser utilizados no exercício financeiro em que forem creditado
- Parte dos recursos recebidos à conta do Fundeb pode ser utilizada no exercício seguinte. S
- Excluídos os recursos da complementação-VAAR, a proporção mínima dos recursos anuais totai
- Para fins da subvinculação de 70%, consideram-se profissionais da educação básica apenas o
- Realizada a distribuição da complementação-VAAT às redes de ensino, deve ser destinada à e
- É vedada a utilização dos recursos do Fundeb para o pagamento de aposentadorias e de pensõ
- Entre as vedações à utilização dos recursos do Fundeb, está:
- Os recursos do Fundeb podem ser aplicados pelos Estados e Municípios indistintamente entre
- Um Município usa recursos do Fundeb para custear uma festa de aniversário da cidade, alega
- A fiscalização e o controle do cumprimento da Lei 14.113/2020 são exercidos, entre outros,
- As prestações de contas dos recursos do Fundeb são instruídas com parecer do conselho de a
- Os conselhos de acompanhamento e controle social do Fundeb existem apenas no âmbito federa
- Os conselhos de acompanhamento e controle social do Fundeb podem, por decisão da maioria d
- Compete ao Ministério Público dos Estados, do DF e Territórios e ao Ministério Público Fed
- A atuação dos membros dos conselhos de acompanhamento e controle social do Fundeb é remune
- Os conselhos do Fundeb têm composição plural, com representantes, entre outros, do Poder E
- Considere que, em determinado Estado, o fundo recebeu R$ 10 bilhões no ano, sem complement
- A complementação-VAAF é distribuída no âmbito de cada Estado e do DF sempre que o valor an
- Ao menos 15% dos recursos da complementação-VAAT devem ser aplicados em despesas de capita
- Os recursos do Fundeb, por terem natureza contábil, podem ser movimentados em qualquer con
- Quanto às diferenças entre o Fundeb atual e o anterior, é correto afirmar que o atual:
- Os Municípios atuam prioritariamente na educação infantil e no ensino fundamental, e os Es
- Para fins da Lei 14.113/2020, considera-se remuneração dos profissionais da educação básic
- A destinação de 50% da complementação-VAAT à educação infantil considera, para fixar os pe
- A atuação dos membros dos conselhos do Fundeb assegura isenção da obrigatoriedade de teste
- A complementação da União destina-se exclusivamente a assegurar recursos financeiros aos f
- Pela Lei 14.113/2020, a cota-parte do ITR devida aos Municípios não integra a cesta de rec
- A distribuição dos recursos do Fundeb faz com que alguns Municípios recebam do fundo mais 
```

## Pedido 2: Ética, vendas e atendimento financeiro (`conhecimentos-bancarios.etica-vendas-atendimento`, 86 tópicos de edital)

```
Matéria: conhecimentos-bancarios.etica-vendas-atendimento (Ética, vendas e atendimento financeiro)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ética profissional, integridade e proteção do consumidor financeiro": 19
- "2. Comportamento do consumidor, marketing de relacionamento e valor percebido": 20
- "3. Vendas consultivas, negociação e atendimento multicanal": 27
- "4. Política de relacionamento com clientes e tratamento equitativo": 18
- "5. Ouvidoria, autorregulação bancária e prevenção de conflitos": 16

Já existem (não repita):
- Uma pessoa procura crédito e o atendente condiciona a contratação à compra de um seguro ad
- Em reunião comercial, um cliente informa que deseja reserva disponível para uma emergência
- Um serviço de atendimento bancário varia bastante de qualidade conforme o atendente e o ho
- Uma instituição planeja acompanhamento após a contratação para solucionar dúvidas e revisa
- Ao comparar dois serviços, uma pessoa pondera benefícios, tarifas, tempo de atendimento e 
- Uma instituição quer reduzir a repetição de informações em diferentes contatos e registrar
- Uma pessoa questiona informação contratual que não foi esclarecida no atendimento habitual
- Uma instituição abrangida pela Resolução CMN nº 4.949/2021 deve orientar o relacionamento 
- Um banco registra 1.000 clientes no início de um período, perde 90 desses clientes e conqu
- O atendente comunica que o empréstimo terá uma parcela mensal reduzida, mas não apresenta 
- Uma instituição abrangida pela Resolução CMN nº 4.949 está formalizando sua política de re
- Uma pessoa idosa demonstra compreender as características do crédito, mas solicita explica
- Um consumidor quer reduzir o valor mensal do financiamento sem comprometer mais a renda, e
- Uma agência perdeu capacidade de atendimento por manter três guichês ociosos no período da
- Uma instituição que utiliza CRM registra histórico de atendimento e passa a compartilhar o
- Uma política institucional prevê gestão da cobrança, tratamento de demandas e procedimento
- Uma instituição de pagamento questiona se a Resolução CMN nº 4.949/2021, com a alteração d
- Uma administração publica política de relacionamento, mas nenhum empregado recebe treiname
- Um cliente permanece há muitos anos na instituição porque mudar suas operações seria traba
- Uma operação financeira é anunciada como 'sem custo', mas contém encargos materiais oculto
- Uma reclamação bancária não resolvida pelo atendimento primário chegou à ouvidoria. Além d
- Uma instituição signatária defende que a existência de norma de autorregulação SARB autori
- Uma pessoa com baixa familiaridade digital recebe proposta por aplicativo e demonstra difi
- Um consumidor compara serviço de benefícios percebidos 90 e sacrifícios 45 com outra alter
- Um responsável pela contratação de serviços financeiros identifica que o consumidor recusa
- Uma instituição oferece crédito e informa de maneira compreensível custos, riscos e obriga
- Uma pessoa mantém conta bancária por dificuldade de transferir serviços, embora esteja ins
- O acompanhamento de dúvidas e dificuldades de uso após a contratação é atividade compatíve
- Serviços bancários são considerados intangíveis porque a instituição pode armazenar atendi
- A Resolução CMN nº 4.949/2021 exige observância de ética, responsabilidade, transparência 
- A ouvidoria é o setor destinado a substituir a abordagem inicial de vendas e a conduzir a 
- Um profissional que detecta dificuldade de compreensão do consumidor pode explicar os cust
- Em uma oferta de crédito, a divulgação isolada do valor da parcela dispensa informar o cus
- Uma política de relacionamento que atribui responsabilidades e capacita profissionais, apr
- A boa qualidade em serviços financeiros depende apenas de cortesia do atendente, sendo irr
- Um CRM pode apoiar relacionamento de longo prazo ao registrar demandas de modo coerente, d
- Por pertencer à autorregulação setorial, um normativo SARB pode afastar exigência do CDC q
- Na Resolução CMN nº 4.949/2021, o tratamento equitativo considera perfis e vulnerabilidade
- Se a instituição divulga condições suficientemente precisas de crédito, pode posteriorment
- Uma ouvidoria que recebe reclamação não solucionada deve registrar, instruir, analisar e e
- A alteração de 2024 da Resolução CMN nº 4.949 excluiu algumas categorias de seu âmbito dir
- Um cliente possui benefícios percebidos de 75 pontos e sacrifícios de 25; outra alternativ
- Quando uma pessoa idosa questiona o custo do crédito e demora a decidir, o fornecedor pode
- A Resolução CMN nº 4.860/2020 exige que o trabalho da ouvidoria contribua para identificar
- A prestadora de atendimento telefônico pode dispensar esclarecimento de custos e condições
```

## Pedido 3: Custas, emolumentos e isenções (`processo-trabalho.custas-emolumentos`, 85 tópicos de edital)

```
Matéria: processo-trabalho.custas-emolumentos (Custas, emolumentos e isenções)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Custas do processo de conhecimento: cálculo e bases legais": 12
- "2. Pagamento, sucumbência, recurso e valor da causa": 11
- "3. Custas na execução: tabela completa do art. 789-A": 16
- "4. Emolumentos e justiça gratuita: arts. 789-B e 790": 13
- "5. Isenções da Fazenda Pública e integração das regras de custas": 48

Já existem (não repita):
- Uma sentença condenou uma empresa privada ao pagamento de R$ 20.000,00 em créditos trabalh
- Uma reclamação de valor atribuído à causa de R$ 30.000,00 teve todos os pedidos julgados i
- As partes fazem acordo judicial no valor de R$ 10.000,00 e não estipulam quem suportará as
- Uma executada privada é responsável pelas custas relativas a atos praticados na fase execu
- Uma parte solicita dez fotocópias de peças processuais, sem autenticação. Considerada a ta
- Uma reclamação com valor da causa de R$ 5.000,00 termina por extinção sem resolução do mér
- Uma sentença condenatória já fixou as custas e a parte vencida pretende recorrer, sem bene
- No curso de uma execução, foi certificada diligência de oficial de justiça em zona rural. 
- Uma parte recebe salário comprovado igual a 35% do teto previdenciário. Qual tratamento da
- Uma autarquia estadual que não explora atividade econômica figura como parte vencida. Qual
- Uma decisão totalmente improcedente atribuiu à causa R$ 400,00. Dois por cento resultariam
- O contador do juízo apurou na execução valor liquidado de R$ 200.000,00. Pela tabela do ar
- Em execução trabalhista, um bem avaliado em R$ 50.000,00 permaneceu dois dias em depósito 
- No processo de conhecimento, há procedência de pedido exclusivamente declaratório, com val
- Uma parte solicita três folhas de certidão ao setor competente. Pelo art. 789-B, V, e cons
- O juiz fixou custas de R$ 600,00 na sentença; a parte recolheu R$ 580,00 e recorreu tempes
- Uma parte trabalhadora recebe renda superior a 40% do teto previdenciário, mas apresenta d
- Uma empresa tem demanda de valor inferior a quarenta salários mínimos contra autarquia fed
- A parte requerente de um processo trabalhista pede dez folhas de autenticação de peças, se
- Uma entidade fiscalizadora do exercício profissional sustenta que sua natureza pública lhe
- Em 2026, uma condenação de R$ 2.500.000,00 implicaria custas de 2% iguais a R$ 50.000,00. 
- Na execução, são lavrados autos de arrematação relativos a bem vendido por R$ 60.000,00. C
- Uma fundação pública municipal que não explora atividade econômica perde demanda. A parte 
- Parte com renda acima de 40% do teto do RGPS requer justiça gratuita com declaração partic
- No rito sumaríssimo, a reclamação escrita omite valores correspondentes aos pedidos e deix
- Em reclamação trabalhista com condenação pecuniária de R$ 50.000,00, a alíquota geral de c
- Em acordo trabalhista sem cláusula de rateio, as custas calculadas sobre o valor conciliad
- Na execução trabalhista, o art. 789-A determina custas a cargo do executado e pagas ao fin
- A cobrança de certidão de duas folhas nos termos do art. 789-B segue a regra de R$ 0,28 po
- O Ministério Público do Trabalho está expressamente incluído entre os isentos de custas no
- Se houver sentença condenatória ilíquida, o art. 789, § 2º, proíbe fixação de custas até q
- Em demanda totalmente improcedente com valor da causa de R$ 50.000,00, e sem isenção ou gr
- O art. 790, § 3º, impede que pessoa com remuneração superior a 40% do teto do RGPS obtenha
- No art. 789-A, os autos de arrematação, adjudicação e remição sujeitam-se a custas de 5% s
- Uma sociedade de economia mista que explora atividade econômica torna-se isenta de custas 
- Uma reclamação escrita deverá indicar pedido certo, determinado e com valor, segundo o art
- Um recurso é interposto depois de recolhidas custas em montante inferior ao devido. A OJ 1
- No art. 789-A, III, IV e V, agravo de instrumento, agravo de petição e embargos à execução
- O art. 789-A, IX, usa como base de cálculo de custas de contador judicial o valor da arrem
- Quando a parte vencida no conhecimento não recorre, o art. 789, § 1º, situa o pagamento da
- Uma autarquia pública sem atividade econômica, vencida na ação trabalhista, fica isenta de
- Segundo o Tema 21 do TST, a parte com salário superior a 40% do teto do RGPS pode instruir
- Uma causa sumaríssima de até quarenta salários mínimos pode seguir esse rito mesmo quando 
- Em dissídios coletivos, o art. 789, § 4º, determina responsabilidade solidária das partes 
- A responsabilidade solidária do sindicato prevista no art. 790, § 1º, independe de interve
```

## Pedido 4: Forças Armadas na Constituição (`direito-constitucional.forcas-armadas`, 84 tópicos de edital)

```
Matéria: direito-constitucional.forcas-armadas (Forças Armadas na Constituição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza constitucional, composição e autoridade suprema": 22
- "2. Missões constitucionais e limites da garantia da lei e da ordem": 25
- "3. Estatuto constitucional dos militares e restrições de direitos": 16
- "4. Serviço militar obrigatório, conscrição e imperativo de consciência": 15
- "5. Controle das Forças Armadas, limites e interpretação de casos": 22

Já existem (não repita):
- Em um curso de formação, pede-se que um integrante identifique as instituições que compõem
- Durante debate sobre a estrutura militar brasileira, alguém afirma que a autoridade suprem
- Um texto de revisão afirma que as Forças Armadas são organizadas sobre dois princípios est
- Uma autoridade pretende fixar, por ato normativo de menor hierarquia, as normas gerais de 
- Um militar em serviço ativo pretende sindicalizar-se para negociar condições profissionais
- Em tempo de paz, um cidadão alistado declara objeção a atividade essencialmente militar po
- Uma questão apresenta a situação de mulheres e eclesiásticos diante da obrigação constituc
- Um cidadão presta serviço militar obrigatório inicial e pergunta se pode alistar-se como e
- Um órgão público pretende empregar as Forças Armadas cotidianamente nas atividades ordinár
- Depois de discussões entre Poderes constitucionais sobre a validade de uma decisão, propõe
- Há grave e concreta ruptura de segurança interna, e a atuação ordinária de preservação da 
- Um militar da ativa toma posse, na forma da lei, em cargo público civil permanente, não ab
- Um militar ativo ocupa função civil temporária não eletiva durante dois períodos distintos
- Um oficial recebe condenação transitada em julgado a pena privativa de liberdade de três a
- Um militar alistável com oito anos de serviço pretende concorrer a mandato eletivo. Qual r
- Um militar alistável com doze anos de serviço é eleito após candidatura nos moldes constit
- Durante processo disciplinar militar, questiona-se sanção cuja própria previsão legal é co
- Um oficial é condenado por decisão transitada em julgado a pena privativa de liberdade de 
- Uma norma administrativa autoriza todo militar da ativa a acumular qualquer emprego civil 
- Em controvérsia institucional, uma autoridade sustenta que a Constituição permite acionar 
- Um oficial da ativa cumpre dois períodos descontínuos de afastamento em função civil tempo
- Um cidadão alistado em paz invoca convicção política para recusar atividades essencialment
- Em discussão sobre soldo de praça que presta serviço militar inicial obrigatório, argument
- Uma questão afirma que, para controle de punição disciplinar militar, o julgador pode reve
- Um grupo militar propõe interpretar 'garantia dos poderes constitucionais' como atribuição
- As Forças Armadas são formadas por Marinha, Exército e Aeronáutica e organizam-se com base
- A autoridade suprema sobre as Forças Armadas pertence ao Congresso Nacional, cabendo ao Pr
- O art. 142, § 3º, IV, proíbe aos militares tanto a sindicalização quanto a greve, independ
- A Constituição assegura que militares em serviço ativo possam filiar-se a partidos polític
- O serviço militar é obrigatório nos termos da lei, e o art. 143, § 1º, prevê alternativa p
- A isenção constitucional de serviço militar obrigatório aplicável a mulheres e eclesiástic
- A Constituição exige lei complementar para estabelecer normas gerais de organização, prepa
- A ADI 6.457 reconheceu que as Forças Armadas podem exercer função de poder moderador sempr
- A atuação de garantia da lei e da ordem pode ser excepcional e subsidiária, após esgotados
- O militar da ativa que toma posse em cargo civil permanente e não se enquadra na exceção d
- No art. 14, § 8º, I, o militar alistável que conta menos de dez anos de serviço deverá afa
- Para o militar alistável com mais de dez anos de serviço, a Constituição determina passage
- A regra constitucional de inalistabilidade dos conscritos restringe-se ao período do servi
- O art. 142, § 3º, VII, exige pena privativa de liberdade igual ou superior a dois anos, tr
- Em exercício de função civil temporária não eletiva, o militar agregado pode ser promovido
- A vedação constitucional de habeas corpus em relação a punições disciplinares militares pe
- O art. 143, § 1º, inclui convicções religiosas, filosóficas e políticas como possíveis fun
- A Súmula Vinculante nº 6 declara inconstitucional qualquer remuneração de praça do serviço
- A jurisprudência da ADI 6.457 afasta atuação das Forças Armadas como poder moderador, mas 
- O art. 144, § 6º, ao chamar polícias militares e bombeiros de forças auxiliares e reserva 
```

## Pedido 5: Demografia e urbanização mundial (`geografia.demografia-urbanizacao-mundo`, 84 tópicos de edital)

```
Matéria: geografia.demografia-urbanizacao-mundo (Demografia e urbanização mundial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Distribuição populacional, estrutura demográfica e indicadores": 21
- "2. Teorias demográficas, transição e qualidade de vida": 27
- "3. Migrações internacionais, movimentos internos e deslocamentos forçados": 24
- "4. Urbanização, metropolização e redes urbanas": 15
- "5. Desigualdades socioespaciais e integração dos indicadores mundiais": 13

Já existem (não repita):
- Um território tem 600 mil residentes distribuídos por 2 mil km². Qual é sua densidade demo
- Em um relatório, constam 2.000 nascidos vivos durante o ano em população média de 200 mil 
- Um país contabilizou 15 mil nascimentos e 9 mil óbitos num ano. Desconsiderando migrações,
- Duas pirâmides etárias são comparadas: a primeira tem base relativamente larga e a segunda
- Um texto histórico sustenta que o número de habitantes tenderia a crescer mais rapidamente
- Em país que amplia saneamento e vacinação, a mortalidade cai rapidamente enquanto a natali
- Dois países apresentam índices de Gini de 0,35 e 0,55, obtidos pela mesma metodologia de r
- Uma pessoa mora na cidade A, trabalha na cidade B e retorna diariamente ao mesmo domicílio
- Uma região tinha 500 mil habitantes, dos quais 300 mil urbanos. Anos depois, passou a ter 
- Um índice sintético combina longevidade, anos de escolaridade e renda nacional bruta per c
- No mesmo ano, determinado território registrou 2.500 imigrantes e 4.000 emigrantes, descon
- Uma família deixa sua cidade por redução de empregos e escolhe outra região na qual parent
- Uma comunidade foge de conflito armado e se instala em outra região do mesmo país, sem atr
- Uma aglomeração possui 12 milhões de residentes, mas outra, com 7 milhões, concentra servi
- Duas cidades vizinhas apresentam manchas construídas que se unem fisicamente, embora mante
- Uma cidade intermediária oferece hospital especializado e ensino superior, atraindo morado
- Um território registrou 300 óbitos antes do primeiro aniversário e 25 mil nascidos vivos n
- Uma população hipotética tem 25 mil pessoas de 0 a 14 anos, 15 mil de 65 anos ou mais e 80
- A fecundidade de um país caiu abaixo do nível aproximado de reposição, mas sua população c
- Duas estimativas de urbanização são publicadas para o mesmo país: uma usa definição admini
- Numa metrópole, grupos de baixa renda concentram-se em áreas com transporte precário e equ
- Um antigo bairro residencial recebe investimentos, valorização de imóveis e substituição d
- Um estudo soma 117,8 milhões de pessoas deslocadas à força no fim de 2025 ao estoque de mi
- Em território de um milhão de habitantes, houve 12 mil nascimentos, 8 mil mortes, 6 mil im
- Uma análise contrapõe a tese que associa alta natalidade à pobreza à perspectiva que ident
- Um território com dois milhões de moradores e área de dez mil km² possui densidade média d
- A taxa de fecundidade total corresponde ao número de nascidos vivos por mil habitantes, se
- Na segunda etapa clássica da transição demográfica, a mortalidade geralmente diminui antes
- O Índice de Desenvolvimento Humano é calculado exclusivamente a partir da renda per capita
- Uma pessoa que viaja diariamente para trabalhar em município vizinho e regressa ao mesmo d
- Uma cidade pode ganhar habitantes em números absolutos enquanto a taxa de urbanização de s
- Conurbação corresponde à continuidade física das manchas urbanas de localidades vizinhas e
- Uma pirâmide etária com base estreita e proporção elevada de idosos comprova que houve que
- Na perspectiva reformista de interpretação demográfica, condições sociais desiguais e pobr
- A razão de dependência etária é calculada dividindo o número de pessoas desempregadas pelo
- Uma pessoa forçada a deixar a residência devido a violência e que se instala em outra part
- Uma megacidade é definida pelo controle de redes financeiras e decisões empresariais globa
- A taxa de mortalidade infantil costuma relacionar mortes antes de um ano ao total de nasci
- Se o índice de Gini de uma sociedade diminui, fica demonstrado que sua renda média aumento
- Uma população pode continuar aumentando depois que a fecundidade cai para baixo da reposiç
- A revisão internacional de urbanização de 2025 usa apenas um critério nacional uniforme de
- Uma cidade menos populosa que outra pode apresentar maior centralidade mundial por sediar 
- O total de 117,8 milhões de pessoas deslocadas à força no fim de 2025 é equivalente, por d
- Um índice sintético de desenvolvimento pode subir enquanto persistem desigualdades interna
- Quando a população de uma metrópole cresce mais lentamente que a de cidades médias, config
```

## Pedido 6: Processo administrativo e ações previdenciárias (`direito-previdenciario.processo-administrativo-judicial`, 84 tópicos de edital)

```
Matéria: direito-previdenciario.processo-administrativo-judicial (Processo administrativo e ações previdenciárias)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Atendimento e fases do processo administrativo previdenciário": 24
- "2. Recursos administrativos, revisão e garantias de procedimento": 15
- "3. Justificação administrativa e reafirmação da DER": 22
- "4. Ações previdenciárias, interesse de agir e competência": 20
- "5. Ministério Público, gratuidade, prioridades e articulação processual": 19

Já existem (não repita):
- Uma segurada procura atendimento para requerer aposentadoria, mas falta um comprovante de 
- Em processo administrativo, toda a documentação já foi analisada e a instrução foi encerra
- Após o indeferimento de benefício, o interessado deseja apresentar a primeira impugnação a
- Uma pessoa recebe comunicação do indeferimento de benefício e pretende recorrer ao CRPS. Q
- Um requerente necessita provar determinado vínculo previdenciário com início de prova mate
- Um segurado apresentou requerimento em fevereiro e reuniu os requisitos da aposentadoria e
- Uma pessoa solicita pela primeira vez um benefício e propõe ação sem ter apresentado pedid
- Uma ação contra o INSS busca prestação decorrente de acidente do trabalho, enquadrada na h
- Uma ação previdenciária envolve pessoa civilmente incapaz e demanda intervenção como fisca
- Uma decisão administrativa indeferiu benefício sem mencionar documento essencial apresenta
- Depois de acórdão desfavorável de Junta de Recursos, um beneficiário deseja questioná-lo a
- Em prova, dois prazos são confrontados: o recurso genérico da Lei nº 9.784/1999 e o recurs
- Um interessado pede justificação administrativa isolada, sem vínculo com atualização do CN
- Para justificação de tempo de contribuição, o interessado apresenta pedido com início de p
- Uma aposentadoria foi negada e, durante a ação judicial para obter o mesmo benefício, o se
- Uma pessoa pretende revisar benefício já concedido com base em documentos referentes a fat
- Um processo de aposentadoria comum contra autarquia federal possui valor equivalente a qua
- Um trabalhador pretende provar por justificação administrativa um casamento, sem apresenta
- Em processo de reconhecimento de vínculo, o interessado alega que um incêndio destruiu doc
- Uma ação de concessão previdenciária foi proposta sem requerimento e sem demonstração de r
- Uma ação de aposentadoria foi ajuizada, e contribuições posteriores completaram requisitos
- Um beneficiário pede justiça gratuita e, depois, é vencido no processo. Qual efeito previs
- Em uma ação previdenciária proposta por pessoa de 83 anos, discute-se prioridade e atuação
- Em ação de benefício por incapacidade, o perito judicial diverge da conclusão da perícia a
- Uma pessoa jurídica pede gratuidade em ação relacionada a benefício e sustenta que sua mer
- Uma requerente compareceu com documentação incompleta, mas o atendente recebeu seu pedido 
- Concluída a instrução de pedido previdenciário, o art. 49 da Lei nº 9.784/1999 determina d
- Um segurado deseja apresentar recurso ordinário ao CRPS e foi informado de prazo de dez di
- O recurso ordinário interposto contra indeferimento de benefício pelo INSS é julgado, no â
- A justificação administrativa pode auxiliar a comprovar fatos de interesse previdenciário 
- A regra geral de interesse de agir do Tema 350 do STF impõe que todo pedido de concessão p
- Para suprir prova de tempo de contribuição por JA, o interessado descreve os pontos a just
- Uma segurada utiliza exclusivamente testemunhas para substituir certidão de casamento, por
- O art. 176-D do Decreto nº 3.048/1999 admite reafirmar o requerimento para a data em que f
- O Tema 995 do STJ proíbe levar em conta contribuições recolhidas após o ajuizamento de açã
- Ao decidir pedido de revisão de benefício já concedido, cujo fundamento foi integralmente 
- Um litígio sobre benefício por acidente do trabalho é necessariamente julgado na Justiça F
- O Juizado Especial Federal Cível pode processar causas previdenciárias de competência fede
- Nos casos em que a regra administrativa de reafirmação da DER é aplicada, a concordância d
- Em ação por incapacidade, se a perícia do juízo divergir do exame administrativo sobre o i
- Em ação individual de revisão de aposentadoria, a presença do INSS no polo passivo, por si
- Concedida gratuidade da justiça, o beneficiário fica livre de qualquer responsabilidade po
- Uma pessoa de 82 anos tem prioridade especial de tramitação em relação aos demais idosos, 
- A presunção de veracidade da insuficiência de recursos prevista no art. 99, § 3º, do CPC a
- Na justificação administrativa de tempo de contribuição, a ocorrência de força maior ou ca
```

## Pedido 7: Servidores do Judiciário da União (`legislacao.servidores-judiciario-uniao`, 84 tópicos de edital)

```
Matéria: legislacao.servidores-judiciario-uniao (Servidores do Judiciário da União)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organização das carreiras, atribuições e ingresso": 16
- "2. Desenvolvimento, funções comissionadas e gratificações por atividade": 16
- "3. Remuneração, GAJ, adicional de qualificação e atualização de 2026": 16
- "4. Previdência complementar e Funpresp-Jud": 18
- "5. Código de Conduta da Justiça Federal e integração das normas": 34

Já existem (não repita):
- Uma unidade administrativa prepara o quadro de cargos efetivos abrangidos pela Lei 11.416/
- Uma pessoa aprovada para Técnico Judiciário em concurso regido pela escolaridade vigente e
- Após aprovação em concurso público para uma carreira da Lei 11.416/2006, em qual posição o
- Um servidor passa do padrão A-3 para A-4, sem mudar a classe, após cumprir interstício e a
- A administração contabiliza funções comissionadas e cargos em comissão para aplicar as res
- Um servidor de carreira recebe vencimento básico de 5.000. Desconsideradas outras vantagen
- Uma especialização lato sensu concluída por servidor é apresentada para fins de adicional 
- Uma pessoa ingressa no serviço público federal após o início do regime complementar e não 
- Um participante automaticamente inscrito na previdência complementar solicita cancelamento
- Um gestor pretende usar a imagem institucional em campanha partidária pessoal. Qual dispos
- Um servidor concluiu o padrão A-5, possui avaliação formal favorável e participou de curso
- Um ocupante de função comissionada de natureza gerencial foi designado sem curso prévio de
- Um servidor enquadrado na especialidade de Polícia Judicial assume função comissionada, ma
- Uma pessoa apresenta um título de mestrado e outro de doutorado em temas reconhecidos como
- Um servidor possui duas especializações reconhecidas, uma segunda graduação e uma certific
- Um servidor ingressou antes da vigência do regime complementar, permaneceu com vínculo efe
- Em plano complementar de contribuição definida, a parcela da base contributiva acima de te
- Um fornecedor potencial oferece um serviço gratuito ao gestor responsável por avaliar prop
- Em outubro de 2026, uma unidade projeta 8% adicionais em julho de 2027 e outros 8% em julh
- Um técnico nomeado quando o ingresso exigia ensino médio concluiu sua primeira graduação a
- Servidor possui dois certificados de especialização (1 VR cada), uma certificação profissi
- Uma unidade oferece cargo em comissão a um servidor de carreira, que deseja optar pela rem
- Uma fundação de previdência complementar do Judiciário está elaborando seu organograma e p
- Uma pessoa propõe que a autoridade administrativa conceda automaticamente contrapartida de
- Um servidor da Justiça Federal pede divulgação de informações corretas ao público, mas um 
- Após concurso para uma das carreiras do Judiciário da União, a investidura no cargo efetiv
- Uma pessoa pretende ingressar como Técnico Judiciário em 2026 e sustenta que a escolaridad
- Se o vencimento básico hipotético é 6.000, a Gratificação Judiciária calculada pelo art. 1
- A Lei 12.618/2012 estabelece que o não ingresso no plano complementar afasta o teto do RGP
- Os gestores da Justiça Federal devem observar os princípios de integridade, lisura, transp
- Um servidor passou de A-5 a B-6 com aprovação em curso e avaliação. Por permanecer na mesm
- O conjunto de cargos em comissão previsto na Lei 11.416/2006 utiliza escalas CJ-1 a CJ-4, 
- Um servidor com doutorado e mestrado reconhecidos pode somar 5 VR e 3,5 VR de adicional de
- A Lei 15.292/2025 prevê que o AQ relativo a ações de capacitação pode acumular com outras 
- Um novo servidor automaticamente inscrito na Funpresp-Jud pode pedir restituição integral 
- Um servidor cuja remuneração é inferior ao teto do RGPS pode aderir voluntariamente ao pla
- Um gestor pode falar oficialmente à imprensa sobre medidas do órgão pelo simples fato de d
- Um técnico admitido sob requisito de nível médio, que concluiu a primeira graduação e pree
- A Lei 15.293/2025 já concedeu de forma definitiva três parcelas sucessivas de reajuste de 
- A Funpresp-Jud é destinada aos servidores e membros do Judiciário abrangidos pela Lei 12.6
- Um servidor exerceu opção válida de migração para o regime limitado ao teto, mas depois ca
- Na Funpresp-Jud, os conselhos deliberativo e fiscal devem ter composição paritária, com se
- O art. 9º da Resolução CJF 147/2011 permite receber serviços oferecidos por fornecedor pot
- A Lei 15.285/2025 admite a exceção à vedação de GAS quando o servidor em FC ou CJ continua
- De acordo com a Resolução CJF 147/2011, uma falha administrativa isolada e involuntária de
```

## Pedido 8: Crimes de responsabilidade: Lei 1.079/1950 e Decreto-Lei 201/1967 (`legislacao-penal-especial.crimes-responsabilidade-prefeitos-agentes`, 84 tópicos de edital)

```
Matéria: legislacao-penal-especial.crimes-responsabilidade-prefeitos-agentes (Crimes de responsabilidade: Lei 1.079/1950 e Decreto-Lei 201/1967)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza dos crimes de responsabilidade e fundamentos constitucionais": 16
- "2. Lei 1.079/1950: tipificações, autoridades e processo federal": 16
- "3. Governadores e prefeitos: crimes penais e consequências funcionais": 29
- "4. Decreto-Lei 201/1967: processo penal, cassação e responsabilidade dos vereadores": 25
- "5. Jurisprudência, conflitos de enquadramento e estratégia de resolução": 14

Já existem (não repita):
- O responsável por um curso apresenta duas denúncias: uma contra o Presidente por atentado 
- Uma lei estadual busca criar novo crime de responsabilidade do Governador e estabelecer ri
- Uma denúncia por crime de responsabilidade presidencial superou a análise inicial e chegou
- Após a instauração do impeachment no Senado, o acusado permanece afastado, e transcorreram
- Em sessão de julgamento de crime de responsabilidade do Presidente, a acusação foi julgada
- Um prefeito se apropria de rendas municipais em proveito próprio. Segundo o DL 201/1967, q
- A apuração penal por delito tipificado no art. 1º do DL 201/1967 foi proposta contra prefe
- Em processo de cassação por infração político-administrativa de prefeito, após o recebimen
- Uma pessoa que exerce mandato de vereador passou a residir definitivamente em outro Municí
- Uma Constituição estadual atribui à Assembleia Legislativa, sem participação de tribunal m
- Um prefeito deixa de prestar contas anuais da administração financeira municipal, ao passo
- Em procedimento legislativo de cassação municipal, a comissão já recebeu a denúncia e noti
- O Ministério Público oferece denúncia criminal por fato previsto no art. 1º do DL 201. Ant
- O prefeito foi notificado no processo de cassação político-administrativa e o julgamento n
- Um Ministro de Estado é acusado de crime de responsabilidade diretamente conexo ao imputad
- A Câmara Municipal altera seu regimento e passa a exigir votação secreta de um órgão exter
- Uma denúncia de infração do art. 4º contra prefeito é lida em sessão da Câmara Municipal. 
- Em uma apuração municipal, o prefeito destinou irregularmente verba pública, mas não há de
- Uma Câmara Municipal possui onze membros e julga três infrações político-administrativas i
- Em denúncia judicial contra prefeito fundada no art. 1º, III, do DL 201, a decisão de rece
- A comissão municipal pretende usar, em 2026, o antigo art. 7º, § 2º, do DL 201/1967 para a
- Ex-prefeito é investigado por delito funcional do art. 1º do DL 201 praticado durante e em
- A sentença penal contra prefeito aplica o art. 1º, § 2º, do DL 201 e determina perda do ca
- Comparam-se duas hipóteses de recebimento de denúncia após saída definitiva do agente do c
- Após autorização por dois terços da Câmara, uma denúncia presidencial segue ao Senado. O r
- Durante a análise de denúncia por crime de responsabilidade presidencial, reconheceu-se qu
- Em um processo de impeachment presidencial, a Câmara dos Deputados exerce diretamente a co
- O desvio de verbas municipais tipificado no art. 1º, III, do DL 201/1967 é submetido a jul
- Uma denúncia escrita de infração político-administrativa de prefeito somente poderá ser fe
- No procedimento de cassação previsto no art. 5º do DL 201, a comissão processante é compos
- Na cassação de prefeito, o voto favorável da maioria dos vereadores presentes na sessão fi
- Prefeito acusado de infração político-administrativa recebeu notificação e, na fase inicia
- Para denunciar criminalmente um prefeito pelos incisos I e II do art. 1º do DL 201, o Mini
- Na Lei 1.079/1950, há previsão de crimes de responsabilidade relacionados à probidade admi
- Um Governador foi acusado de crime de responsabilidade e sua Assembleia Legislativa pode, 
- Um vereador acusado de usar o mandato para corrupção pode responder a cassação política no
- A Câmara Municipal deve manter aberto indefinidamente o processo de cassação por infração 
- Condenação por crime de responsabilidade no Senado, conforme o art. 52, parágrafo único, d
- Depois que a Câmara autoriza o processo contra o Presidente por dois terços dos membros, a
- Um prefeito deixa o mandato depois de supostamente praticar apropriação de recursos públic
- No âmbito dos Governadores, o art. 76, parágrafo único, da Lei 1.079/1950 permite receber 
- Na disciplina específica do art. 2º, III, do DL 201/1967, cabe recurso em sentido estrito,
- Em uma cassação política de vereador por decoro, o Presidente da Câmara pode fundamentar a
- Segundo a tese fixada pelo STF no HC 232.627, em março de 2025, o foro por prerrogativa de
- A Lei 8.176/1991 disciplina o rito de impeachment dos Governadores e complementa diretamen
```

## Pedido 9: Óptica geométrica (`fisica.optica-geometrica`, 84 tópicos de edital)

```
Matéria: fisica.optica-geometrica (Óptica geométrica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Luz, propagação retilínea, espectro e sombras": 17
- "2. Reflexão e imagens em espelhos planos e esféricos": 21
- "3. Refração, índice de refração, ângulo limite e dispersão": 21
- "4. Lentes delgadas e instrumentos ópticos essenciais": 22
- "5. Olho humano, defeitos da visão e resolução integrada": 19

Já existem (não repita):
- Um raio luminoso atinge espelho plano fazendo 25° com a normal. Qual é o ângulo entre o ra
- Uma pequena lâmpada pontual ilumina uma esfera opaca diante de uma parede. No modelo de ra
- Uma pessoa está a dois metros de um espelho plano fixo. Qual é a distância entre ela e sua
- A luz entra num material transparente cujo índice absoluto é 1,5. Adotando c = 3 × 10⁸ m/s
- Dois feixes monocromáticos visíveis têm comprimentos de onda de 400 nm e 650 nm no vácuo. 
- Uma câmara escura de orifício possui um anteparo interno. Que orientação apresenta a image
- Um objeto real é colocado diante de lente delgada divergente, isolada no ar. Como é a imag
- Em uma câmera simples com objetivo convergente, qual imagem precisa ser formada na superfí
- Um raio passa do ar (índice 1,0) para vidro (índice 1,5), incidindo a 30° da normal. Quant
- Um feixe atravessa do vidro para o ar, com incidência crescente. Qual condição permite ref
- Um espelho côncavo possui distância focal de 10 cm, e um objeto real está a 30 cm do vérti
- Uma lente convergente de foco 20 cm recebe objeto a 60 cm. Qual é a posição da imagem obti
- Uma lupa convergente de foco 12 cm é usada com objeto a 8 cm. Que imagem se forma para obs
- Uma lente delgada em ar tem distância focal de −25 cm. Qual sua vergência e classificação?
- Uma pessoa enxerga objetos próximos nitidamente, mas imagens distantes focam antes da reti
- Uma pessoa apresenta dificuldade de foco próximo porque a potência refrativa do olho é ins
- Em um microscópio composto simples, qual é a função básica da lente objetiva diante de um 
- Uma luneta astronômica refratora, ajustada para imagem final no infinito, tem objetiva de 
- Ao examinar linhas orientadas em direções distintas, uma pessoa percebe nitidez desigual d
- Uma pessoa com envelhecimento do cristalino passa a ter dificuldade de acomodação para lei
- Uma pessoa se aproxima 30 cm de um espelho plano fixo, sem mudar seu tamanho. Quanto dimin
- Observa-se quase pela normal o fundo de uma piscina de 2,4 m, do ar (n≈1) para a água (n≈4
- Um espelho côncavo de foco 15 cm recebe um objeto a 10 cm. Qual imagem decorre da equação 
- Para projetar uma imagem ampliada de um pequeno objeto em uma tela, usa-se lente convergen
- Um prisma comum separa luz branca visível em cores, enquanto uma câmera focaliza cena em s
- Num meio transparente homogêneo e isotrópico, longe de interfaces, um feixe estreito pode 
- Um feixe de raios paralelos incide numa superfície fosca irregular. A reflexão difusa impl
- Uma vela colocada diante de espelho plano tem imagem virtual, direita e de altura igual à 
- Ao passar do ar para vidro transparente através de interface estacionária, um raio monocro
- Uma lente divergente fina no ar, recebendo luz de objeto real situado à sua frente, forma 
- Uma fonte luminosa extensa, posicionada atrás de uma barreira opaca, só pode formar sombra
- Numa câmera de lente única ajustada, a superfície fotossensível recebe imagem real e inver
- Um espelho côncavo de foco 10 cm e objeto real colocado a 5 cm forma imagem real e inverti
- Um feixe passa obliquamente do ar para vidro de índice maior. O ângulo com a normal diminu
- Se um raio incide perpendicularmente na superfície entre dois meios de índices diferentes,
- Um raio percorre vidro de índice maior que o do ar e alcança a interface com ângulo superi
- Uma lupa convergente com objeto entre a lente e o foco gera imagem real ampliada que pode 
- Uma pessoa míope apresenta foco de objetos muito distantes antes da retina e, no modelo ge
- A perda de acomodação para enxergar de perto durante o envelhecimento chama-se miopia e é 
- Quando a córnea tem curvaturas diferentes em meridianos distintos e certas direções aparec
- Um espelho esférico convexo forma imagem real de objeto real colocado além de seu centro d
- Uma lente convergente de foco 10 cm recebe objeto a 30 cm. A equação de Gauss prevê imagem
- No microscópio composto elementar, a objetiva funciona como lupa e cria diretamente a imag
- Uma luneta astronômica simples ajustada para observação relaxada tem objetiva de 80 cm e o
- Uma lente convergente com foco 10 cm recebe objeto a 5 cm e forma imagem real a 10 cm do l
```

## Pedido 10: Agrimensura, georreferenciamento e cadastro territorial (`engenharia.agrimensura-georreferenciamento`, 84 tópicos de edital)

```
Matéria: engenharia.agrimensura-georreferenciamento (Agrimensura, georreferenciamento e cadastro territorial)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da agrimensura e levantamentos topográficos": 17
- "Geodésia, GNSS e integração de medições": 17
- "Georreferenciamento rural, certificação e SIGEF": 17
- "Cadastro Territorial Multifinalitário e gestão cadastral": 17
- "Parcelamento, regularização fundiária e agrimensura legal": 16
- "Ajustamento de observações e controle da qualidade posicional": 16

Já existem (não repita):
- Em uma estação total, o ângulo zenital é medido a partir da vertical. Se a distância incli
- Em um levantamento cadastral, uma área será detalhada por irradiação. Qual descrição apres
- Um quadrilátero plano simples apresenta lados consecutivos de 40 m, 25 m, 40 m e 25 m, com
- Uma poligonal plana fechada possui cinco vértices, e os ângulos internos observados somam 
- O azimute, em sua convenção habitual, varia de 0° a 360° e exige uma direção de norte de r
- Ao multiplicar a escala de apresentação de uma planta por um fator dez, a precisão das obs
- O nivelamento geométrico permite determinar diferenças de nível pelo balanço entre leitura
- Um relatório apresenta um par de números UTM, mas omite fuso, hemisfério e sistema de refe
- Um receptor fornece altura elipsoidal h de 125 m e um modelo geoidal fornece ondulação N d
- Em área rural aberta, deseja-se estabelecer vértices de apoio vinculados ao SGB e depois d
- Sobre técnicas de posicionamento GNSS, qual associação está tecnicamente correta?
- A época de referência pode ser relevante em levantamentos geodésicos de alta precisão, por
- A altura elipsoidal calculada pelo GNSS é, em qualquer lugar do Brasil, idêntica à altitud
- Uma solução RTK fixa torna dispensáveis tanto a identificação correta dos marcos quanto a 
- Quanto ao efeito jurídico da certificação de uma poligonal rural pelo INCRA, assinale a af
- Segundo a 3ª edição da Norma Técnica do INCRA, qual sequência corresponde aos limiares de 
- À luz da alteração do art. 10 do Decreto nº 4.449/2002 pelo Decreto nº 12.689/2025, qual m
- O valor σP = √(0,30² + 0,40²) equivale a 0,50 m, desde que as duas componentes estejam exp
- O SIGEF substitui o Registro de Imóveis, de modo que a certificação atualiza a titularidad
- Os padrões de precisão de vértices em limites naturais são mais permissivos numericamente 
- O Decreto nº 12.689/2025 manteve intacta a obrigação escalonada de certificação rural por 
- Qual alternativa melhor descreve o Cadastro Territorial Multifinalitário no âmbito municip
- Uma prefeitura pretende adotar como fundamento das diretrizes nacionais atuais do CTM a Po
- Depois de um desmembramento urbano, a prefeitura precisa manter a coerência entre parcelas
- O cadastro parcelário e a matrícula registral têm, necessariamente, uma correspondência de
- A interoperabilidade de um CTM pode demandar identificadores estáveis, dicionários de dado
- Uma finalidade do CTM é apoiar planejamento territorial e gestão de serviços, além dos uso
- Um levantamento cadastral municipal pode ser considerado atualizado mesmo após parcelament
- Uma gleba urbana será subdividida em lotes mediante a abertura de uma nova rua pública. Se
- Um município está regularizando um núcleo urbano informal de baixa renda classificado pelo
- Ao elaborar memorial para retificação, o técnico encontra uma cerca ocupando área distinta
- Desmembramento urbano e loteamento diferem, entre outros elementos, pelo impacto previsto 
- O certificado emitido pelo SIGEF para poligonal rural é equivalente à Certidão de Regulari
- A planta topográfica com coordenadas, por si só, resolve a titularidade de uma ocupação ur
- A aprovação municipal de um projeto de parcelamento e o registro de direitos reais no Regi
- Para duas observações independentes com desvios padrões de 2 mm e 4 mm, um ajustamento pon
- Em uma poligonal, as somas dos incrementos apresentam ΣΔE = 0,06 m e ΣΔN = 0,08 m. Qual é 
- Uma rede possui grande quantidade de observações, mas todas se apoiam em pontos de control
- No nivelamento geométrico, as somas das leituras de ré e de vante foram 7,235 m e 6,915 m.
- Uma série de medidas pode ser muito precisa e, ao mesmo tempo, pouco acurada se houver err
- Um ponto de controle independente, não utilizado no cálculo dos parâmetros, pode ajudar a 
- O erro linear de fechamento de uma poligonal demonstra, por si só, que todos os vértices e
```

## Pedido 11: Pacote Anticrime (Lei nº 13.964/2019) (`processo-penal.pacote-anticrime`, 84 tópicos de edital)

```
Matéria: processo-penal.pacote-anticrime (Pacote Anticrime (Lei nº 13.964/2019))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Visão geral, sistema acusatório e juiz das garantias": 20
- "2. Arquivamento, acordo de não persecução penal e cadeia de custódia": 20
- "3. Prisões, medidas cautelares e execução no júri": 20
- "4. Mudanças no Código Penal e na execução penal": 20
- "5. Leis especiais alteradas": 20

Já existem (não repita):
- Nos termos do art. 3º-A do Código de Processo Penal, incluído pela Lei nº 13.964/2019, o p
- O juiz das garantias é responsável pelo controle da legalidade da investigação criminal e 
- No julgamento das ações que questionavam o juiz das garantias, o STF fixou que a competênc
- Segundo o STF, o juiz das garantias aplica-se também aos processos de competência do tribu
- O STF declarou inconstitucional o dispositivo do Pacote Anticrime segundo o qual o juiz qu
- Para implantar o juiz das garantias, o STF concedeu aos tribunais o prazo de:
- Pelo novo art. 28 do CPP, se a vítima não concordar com o arquivamento do inquérito, poder
- Constitui requisito do acordo de não persecução penal previsto no art. 28-A do CPP:
- O acordo de não persecução penal pode ser celebrado em crime praticado com grave ameaça, d
- NÃO se aplica o acordo de não persecução penal quando:
- No acordo de não persecução penal, a prestação de serviços à comunidade ou a entidades púb
- Enquanto não cumprido ou não rescindido o acordo de não persecução penal, não corre a pres
- Recusando-se o Ministério Público a propor o acordo de não persecução penal, o investigado
- Segundo o CPP, cadeia de custódia é o conjunto de todos os procedimentos utilizados para m
- Entre as etapas da cadeia de custódia listadas no art. 158-B do CPP, a que consiste em des
- É proibida a entrada em locais isolados e a remoção de quaisquer vestígios de locais de cr
- Após a Lei nº 13.964/2019, a prisão preventiva pode ser decretada:
- Ao receber pedido de medida cautelar, o juiz, ressalvados os casos de urgência ou de perig
- Decretada a prisão preventiva, o órgão emissor da decisão deverá revisar a necessidade de 
- Segundo o CPP alterado pelo Pacote Anticrime, a prisão preventiva pode ser decretada como 
- Para o STF, a inobservância do prazo de noventa dias para revisão da prisão preventiva imp
- Quanto à execução da condenação pelo tribunal do júri, o STF fixou em 2024 que:
- Considera-se também em legítima defesa, observados os requisitos do art. 25 do Código Pena
- Após a Lei nº 13.964/2019, o tempo de cumprimento das penas privativas de liberdade não po
- O confisco alargado, previsto no art. 91-A do Código Penal, pode ser decretado na condenaç
- No crime de roubo, se a violência ou grave ameaça for exercida com emprego de arma de fogo
- Após o Pacote Anticrime, o estelionato passou a ser crime de ação penal pública condiciona
- Pela nova redação do art. 112 da LEP, o apenado primário condenado por crime cometido sem 
- O condenado primário por crime hediondo com resultado morte progride após cumprir 50% da p
- O cometimento de falta grave durante a execução da pena privativa de liberdade interrompe 
- Com a redação dada pelo Pacote Anticrime, o regime disciplinar diferenciado (RDD) tem dura
- A pena do crime de concussão foi alterada pelo Pacote Anticrime para reclusão de dois a do
- Com a Lei nº 13.964/2019, passou a ser considerado hediondo o:
- Após o Pacote Anticrime, a colaboração premiada é definida como negócio jurídico processua
- Nos termos da Lei nº 12.850/2013, com a redação do Pacote, as declarações do colaborador, 
- A infiltração de agentes de polícia na internet, prevista na Lei de Organizações Criminosa
- A captação ambiental de sinais eletromagnéticos, ópticos ou acústicos, prevista na Lei nº 
- A captação ambiental feita por um dos interlocutores sem o prévio conhecimento da autorida
- O Pacote Anticrime alterou a Lei nº 13.608/2018 para assegurar ao informante que relata cr
- O Pacote Anticrime criou, no Estatuto do Desarmamento, o Banco Nacional de Perfis Balístic
- Segundo a Lei de Organizações Criminosas, com a redação do Pacote, o condenado por integra
```

## Pedido 12: Processo legislativo estadual e regimento de Assembleia (`processo-legislativo.regimento-assembleia-estadual`, 83 tópicos de edital)

```
Matéria: processo-legislativo.regimento-assembleia-estadual (Processo legislativo estadual e regimento de Assembleia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura, legislatura, sessões e lideranças": 22
- "2. Espécies de proposições e atos legislativos": 22
- "3. Distribuição, pareceres, questão de ordem e votação": 22
- "4. Urgência, prioridade, veto e medidas provisórias": 17
- "5. Emenda à Constituição estadual, redação final e controles especiais": 17

Já existem (não repita):
- Uma parlamentar inicia seu mandato e quer saber quanto dura uma legislatura no Regimento d
- Uma sessão plenária ordinária foi marcada para quarta-feira; a equipe precisa organizar se
- A Assembleia possui 40 membros e apenas oito comparecem ao horário previsto. Qual conclusã
- Dois partidos decidem atuar conjuntamente e comunicar uma liderança comum. Qual instituto 
- Uma proposta pretende disciplinar assunto de competência exclusiva da Assembleia, sem a pa
- Uma deputada quer apenas sugerir providência administrativa ao destinatário competente, se
- No encerramento da legislatura, há projetos ordinários, vetos, medidas provisórias e ofíci
- Durante a sessão, um parlamentar formula dúvida sobre a interpretação de disposição regime
- Uma proposição concluiu exame das comissões. O responsável pergunta qual o prazo regimenta
- Um projeto trata simultaneamente de adequação jurídica, impacto orçamentário e mérito educ
- Um parlamentar deseja conferir a contagem de votação simbólica proclamada pela Presidência
- Uma emenda a projeto contém dispositivo sobre assunto sem conexão, afinidade ou pertinênci
- Um Governador requer, justificadamente, urgência na tramitação de seu projeto de lei, sem 
- Durante análise de projeto do Governador em urgência pelo art. 222, parlamentar pretende a
- O Regimento admite adiamento da votação ordinária mediante pedido tempestivo, mas a propos
- Uma proposta de lei complementar aprovada no primeiro turno terá, em regra, interstício de
- Uma PEC catarinense foi aprovada por 24 deputados no primeiro turno, mas recebeu 23 votos 
- Quatorze dos quarenta deputados assinaram PEC de iniciativa parlamentar. Uma pessoa afirma
- Um grupo apresenta PEC popular subscrita por 2,5% do eleitorado, mas concentrada em apenas
- A CCJ deixou transcorrer o prazo de admissibilidade de PEC sem parecer, mesmo após a prorr
- Uma PEC chega ao Plenário para decisão sobre sua admissibilidade, com presença de 25 dos 4
- A Assembleia encerrou a votação de projeto sujeito à sanção e deseja encaminhar a versão f
- No exame de requerimento de prioridade, a Mesa recebeu pedido apresentado em outubro para 
- Aprovada PEC catarinense nos dois turnos, qual autoridade formaliza a alteração da Constit
- Um deputado identifica matéria idêntica a proposição já aprovada na mesma sessão legislati
- Na Assembleia de Santa Catarina, a legislatura corresponde a quatro anos e abrange quatro 
- O quórum de instalação de sessão ordinária é de maioria absoluta dos membros, o mesmo núme
- Um bloco parlamentar catarinense pode ser formado pelas representações de dois ou mais par
- O decreto legislativo é o projeto utilizado para matéria exclusiva da Assembleia, mas, por
- Quando um deputado solicita interpretação do Regimento para resolver divergência sobre a o
- Terminada uma legislatura, vetos, medidas provisórias e ofícios devem ser arquivados junto
- Concluída a tramitação nas comissões, a proposição deve ser incluída na Ordem do Dia em at
- A comissão que recebe uma proposição pode emitir parecer sobre sua competência, e relatóri
- A verificação do resultado de votação, solicitada nos termos do art. 261, resulta em conta
- O pedido de urgência do Governador provoca aprovação tácita do projeto se a Assembleia não
- O adiamento da votação em regime de prioridade pode ocorrer, excepcionalmente, quando requ
- Medida provisória estadual e projeto do Governador em urgência compartilham o mesmo prazo 
- Em votação de PEC, o quórum de três quintos deve ser obtido em ambos os turnos; o art. 270
- O ato de admissibilidade de PEC catarinense, previsto no art. 268, depende de três quintos
- Uma proposta de emenda à Constituição catarinense apresentada por iniciativa popular exige
- Uma PEC já admitida deve ser imediatamente remetida ao Governador para sanção, porque a Co
- Uma matéria pode ser declarada prejudicada por identidade com outra aprovada ou rejeitada 
- O interstício de dois turnos do art. 219 prevalece sobre a disciplina da PEC, de maneira q
- Aprovada a redação final de projeto sujeito à sanção, a Mesa remete autógrafo ao Governado
- A aprovação de PEC por três quintos dos membros em ambos os turnos permite ao Governador e
```

## Pedido 13: Provas no processo do trabalho (`processo-trabalho.provas`, 83 tópicos de edital)

```
Matéria: processo-trabalho.provas (Provas no processo do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria da prova, ônus e distribuição dinâmica": 14
- "2. Depoimento pessoal, interrogatório, confissão e valoração": 18
- "3. Prova testemunhal: limites, contradita e procedimento": 18
- "4. Prova documental: oportunidade, autenticidade e falsidade": 24
- "5. Perícia, honorários, prova emprestada e decisão probatória": 26

Já existem (não repita):
- Uma pessoa reclama diferenças por horas trabalhadas além da jornada ordinária; a empresa r
- Uma parte pretende juntar gravação lícita e pertinente para comprovar fato controvertido. 
- O juiz verifica que dados essenciais de uma controvérsia estão sob acesso simples de uma p
- Uma parte foi expressamente intimada, com advertência da consequência, para prestar depoim
- O julgador examina uma causa em procedimento trabalhista comum e outra de inquérito para a
- Em procedimento sumaríssimo, a parte deseja ouvir quatro testemunhas. Qual quantidade máxi
- Uma testemunha foi contraditada unicamente porque também possui reclamação contra o mesmo 
- Uma parte apresenta cópia de contrato declarada autêntica por seu advogado. A outra impugn
- Há controvérsia sobre exposição a agentes insalubres no ambiente de trabalho e não existe 
- Uma parte quer usar em seu processo laudo técnico produzido em outra ação. A parte contrár
- O juiz redistribuiu o ônus antes da instrução porque uma empresa controla exclusivamente r
- Uma parte deixa de depor após intimação válida, mas já havia documentos nos autos em senti
- Em um processo sumaríssimo, testemunha convidada não comparece. A parte pede que seja inti
- Uma pessoa apresenta documento produzido após a defesa, destinado a esclarecer fato poster
- Uma assinatura aposta em documento particular é negada pela pessoa indicada como signatári
- A parte vencida na pretensão examinada por perícia recebeu justiça gratuita. A outra parte
- O julgador nomeia perito especialista para esclarecer uma controvérsia técnica e intima as
- Uma empresa com 28 trabalhadores sujeitos a registros de ponto deixa de apresentar control
- A empresa apresenta um preposto não empregado, mas que desconhece completamente os fatos p
- Um documento antigo, acessível à parte desde a contestação, é juntado pela primeira vez ao
- Uma testemunha é amiga íntima da pessoa reclamante, e isso fica comprovado na contradita. 
- Uma parte suscita falsidade de recibo apresentado tardiamente, quinze dias após ser intima
- Uma perícia judicial é desfavorável a determinada parte, que havia contratado especialista
- Em perícia emprestada sobre exposição a agente nocivo, a parte apresenta laudo de outro lo
- Um magistrado recebe laudo técnico bem fundamentado, mas outros elementos dos autos revela
- Em discussão sobre obrigação, a parte reclamante prova o fato constitutivo e a reclamada d
- Uma testemunha foi indicada em procedimento comum trabalhista. Como o art. 821 fixa seis t
- Uma parte foi intimada expressamente para depoimento, advertida da pena e não compareceu à
- A reclamada junta cópia declarada autêntica por seu advogado. A simples apresentação em có
- Em ação que exige exame técnico de risco ocupacional, o art. 195, § 2º, da CLT prevê desig
- A parte deseja juntar prova documental após a sentença e, na fase recursal, basta alegar q
- Para redistribuir o ônus de determinado fato à parte com maior facilidade probatória, o ju
- Em prova testemunhal, a pessoa convidada para depor no procedimento sumaríssimo deixa de c
- Uma testemunha também demanda judicialmente o empregador em processo próprio, sem evidênci
- Uma parte impugna especificamente a autenticidade da assinatura de documento juntado pela 
- O perito judicial nomeado apresenta o laudo e a parte pretende manifestar-se sobre suas co
- Uma pessoa obtém gratuidade e é vencida em pedido cujo exame dependeu de perícia. A ADI 57
- Uma prova pericial produzida em outro processo é aproveitada para examinar condição de tra
- O juiz considera insatisfatórias as provas documentais, mas só comunica na sentença que tr
- Um preposto comparece ao depoimento, mas ignora fatos centrais que deveria conhecer. A tes
- Em processo sumaríssimo, a parte apresenta duas testemunhas. Uma falta após convite compro
- Uma parte suscita falsidade de documento novo no prazo de quinze dias contado da intimação
- O julgador fixa honorários do perito oficial e, antes de iniciar a avaliação, ordena que a
- Para rejeitar conclusão pericial incompatível com as demais provas e com o método empregad
- Um laudo pericial emprestado avalia máquinas e agentes de risco diferentes daqueles do pro
```

## Pedido 14: Power Platform, RPA e low-code (`informatica.power-platform-rpa`, 82 tópicos de edital)

```
Matéria: informatica.power-platform-rpa (Power Platform, RPA e low-code)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da Power Platform e desenvolvimento low-code/no-code": 25
- "2. Power Apps: aplicativos, dados e Power Fx": 18
- "3. Power Automate: fluxos de nuvem e integração": 18
- "4. Automação robótica de processos e fluxos de desktop": 21
- "5. Power Virtual Agents, Copilot Studio e governança integrada": 18

Já existem (não repita):
- Uma equipe pretende criar uma tela de cadastro de solicitações com campos personalizados, 
- Um processo precisa disparar um aviso sempre que uma nova solicitação for incluída numa li
- Um projeto exige aplicativo centrado em tabelas, formulários e exibições com relacionament
- Um sistema antigo permite preencher campos na janela, mas não oferece serviço de integraçã
- Em um ambiente de criação visual, a equipe usa fórmulas simples para validar campos e cone
- Uma rotina deve consolidar registros automaticamente em horário recorrente, mesmo quando n
- Em um projeto de chatbot, um documento antigo menciona Power Virtual Agents. Qual identifi
- Um administrador publica um aplicativo que utiliza dados de uma lista restrita. O destinat
- Uma consulta usa fórmula não delegável sobre uma lista com 30 mil registros. A pesquisa nã
- Uma pessoa clica em botão do aplicativo para iniciar a geração de um comprovante, sem even
- Uma empresa configurou um conector para serviço de armazenamento, mas a conta usada no flu
- Um gestor precisa distinguir o uso de um fluxo em nuvem de um robô que percorre campos de 
- Um agente de atendimento coleta o número do pedido e precisa consultar seu estado antes de
- Ao desenhar uma solução com aplicação e fluxo, o responsável deseja separar componentes ed
- Uma rotina de nuvem consulta uma base, aguarda parecer e só então atualiza o status da sol
- Uma equipe trabalha com uma galeria Power Apps conectada a dezenas de milhares de linhas. 
- Uma empresa automatiza lançamento no sistema legado por interface. Após a alteração de res
- Uma solução tenta combinar conector corporativo e conector pessoal no mesmo fluxo, mas a p
- Um fluxo registra uma compra por meio de API e, em caso de resposta de rede inconclusiva, 
- Uma equipe planeja agente que consulta documentação e, eventualmente, abre uma solicitação
- Um aplicativo de tela oferece botão invisível a usuários sem determinado perfil, mas a ori
- Uma equipe desenvolve solução não gerenciada e planeja implantar pacote gerenciado no ambi
- Um processo requer processamento de solicitação por conector disponível e, apenas na etapa
- Uma rotina de RPA assistida é transferida para execução não assistida. Qual avaliação é in
- Uma equipe precisa publicar assistente para esclarecer regras internas, registrando solici
- Uma equipe usa Power Apps para criar formulário de vistoria e Power Automate para encaminh
- Um aplicativo orientado a modelo pode ter qualquer planilha externa como base principal, d
- Um fluxo de nuvem iniciado pelo clique deliberado em um botão é considerado agendado porqu
- Um fluxo de desktop preenche campos em sistema legado por meio de elementos de interface. 
- Um roteiro de atendimento criado no antigo Power Virtual Agents pode ser estudado como ant
- Compartilhar um aplicativo Power Apps com determinado usuário concede automaticamente a es
- Em um fluxo do Power Automate, a criação de um item pode ser o gatilho, ao passo que o env
- Em um aplicativo de tela, esconder o botão de edição por fórmula de visibilidade basta par
- Uma consulta não delegável é aplicada a uma lista com 60 mil registros. Ao configurar o li
- Uma organização possui integração autorizada por API para criar registros. Optar por essa 
- Se um agente conversacional reconhece o número de uma solicitação, essa extração de entida
- Um fluxo automatizado obtém sucesso ao registrar o envio de uma solicitação de aprovação, 
- Em políticas clássicas de dados, conectores classificados como Business e Non-Business pod
- Como fluxos de desktop executam ações em programas locais, alterações na resolução da sess
- A organização de aplicações e fluxos em soluções permite adotar estratégia de desenvolvime
- Uma organização migra um fluxo de desktop assistido para execução não assistida. Por execu
- Um fluxo que cria um registro pode registrar identificador de operação e verificar se ele 
- Quando uma política avançada de conectores usa lista de permissões, deve ser interpretada 
- Um agente do Copilot Studio respondeu corretamente a uma pergunta com apoio de documento. 
- Em um processo que combina serviço acessível por conector e aplicação legada sem API, o Po
```

## Pedido 15: Radioatividade e transformações nucleares (`ciencias.radioatividade`, 82 tópicos de edital)

```
Matéria: ciencias.radioatividade (Radioatividade e transformações nucleares)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Núcleo atômico, estabilidade e radiações no espectro": 18
- "2. Emissões alfa, beta, gama e produção de raios X": 19
- "3. Cinética do decaimento, meia-vida e atividade radioativa": 19
- "4. Fissão, fusão e aplicações nucleares e médicas": 27
- "5. Radioproteção, grandezas dosimétricas e normas básicas": 17

Já existem (não repita):
- Um nuclídeo tem número de massa 27 e número atômico 13. Quantos nêutrons existem em seu nú
- Um átomo de urânio-238 emite uma partícula alfa. Que núcleo-filho deve resultar antes de o
- Uma amostra radioativa possui meia-vida de cinco dias. Partindo de 80 unidades de radionuc
- Uma fonte tem atividade medida de 250 Bq. Que interpretação corresponde à unidade no Siste
- Durante o estudo de uma fonte que emite gama, qual afirmação descreve corretamente a radia
- Uma sala utiliza tubo de raios X convencional. Qual processo é responsável pelo espectro c
- Uma substância selada de cobalto-60 irradia materiais com fótons gama; em outro local oper
- Em procedimento radiológico, uma equipe diferencia dose absorvida de atividade da fonte. Q
- No decaimento beta menos do carbono-14, qual alteração ocorre no núcleo-filho, preservando
- Uma amostra passou de 640 Bq para 80 Bq em dezoito horas, sem reposição do material. Qual 
- Dois exames usam fótons, sendo um gerado por elétrons desacelerados no ânodo e outro origi
- Uma fonte aproximadamente pontual, sem blindagem ou espalhamento significativo, produz tax
- Um detector registra fótons de 511 keV emitidos aproximadamente em sentidos opostos após u
- Em exposição planejada sujeita à ANSN 3.01/2025, qual par corresponde aos limites gerais a
- Uma amostra tinha 400 átomos radioativos representativos e, após três meias-vidas, resta u
- Um serviço compara irradiação de materiais por fonte selada e derramamento de solução cont
- Na emissão beta mais de sódio-22, cuja notação inicial é A=22 e Z=11, qual transformação d
- Num tubo ideal submetido a 80 kV, elétrons acelerados chegam ao alvo e produzem bremsstrah
- Um radionuclídeo no organismo possui meia-vida física de seis horas e meia-vida biológica 
- Na fissão ilustrativa de U-235 após captura de um nêutron, surgem Ba-141 e Kr-92. Quantos 
- Uma justificativa de proteção usa o limite de dose efetiva de 1 mSv/ano para público para 
- Um material absorveu 0,006 joule de energia de radiação em uma massa de 0,003 quilograma. 
- Um programa de monitoração informa que trabalhador poderá receber 30 mSv de dose efetiva n
- Uma amostra radioativa contém núcleos que decaem por beta menos; depois, o núcleo-filho ex
- Para reduzir risco ocupacional de fonte beta energética, uma equipe decide usar camada ini
- Dois átomos com o mesmo número de prótons e diferentes números de nêutrons são isótopos do
- Na emissão alfa, o núcleo perde uma unidade de número de massa e duas unidades do número a
- A meia-vida de dez dias significa que uma amostra de radionuclídeo inicialmente com 200 un
- O gray é a unidade do número esperado de desintegrações nucleares por segundo, e o becquer
- Raios X produzidos no alvo de um tubo podem incluir um espectro contínuo de frenagem e lin
- Uma fonte radioativa selada e um aparelho gerador de raios X são equivalentes quanto ao de
- Um objeto exposto à radiação gama de fonte selada não fica necessariamente contaminado, po
- Uma fonte cuja atividade caiu de 1.200 Bq para 300 Bq passou por três meias-vidas, já que 
- A radiação beta menos pode transformar carbono-14 em nitrogênio-14, mantendo o número de m
- Radiação de luz visível e ondas de rádio são ordinariamente classificadas como ionizantes 
- No exemplo clássico de fusão deutério-trítio, a soma dos números de massa dos reagentes é 
- A atividade de uma fonte expressa em becquerel determina diretamente a dose efetiva recebi
- Na comparação de proteção externa, partículas alfa tendem a ser facilmente barradas por pa
- Em situações planejadas, a ANSN 3.01/2025 estabelece limite geral de dose efetiva de 20 mS
- Em uma emissão gama isolada a partir de núcleo excitado, o nuclídeo-filho permanece com os
- Em um tubo ideal submetido a 100 kV, todos os fótons de bremsstrahlung devem possuir energ
- O limite de dose efetiva de 20 mSv por ano para ocupacionais na ANSN 3.01/2025 admite form
- O princípio ALARA equivale a impor exposição exatamente nula em todo procedimento médico, 
- Dois processos independentes removem um radiofármaco do organismo com meias-vidas física e
- Os efeitos estocásticos da radiação são definidos por limiar de dose obrigatório e gravida
```

## Pedido 16: Políticas e resoluções do sistema prisional (`legislacao-penal-especial.politicas-sistema-prisional`, 82 tópicos de edital)

```
Matéria: legislacao-penal-especial.politicas-sistema-prisional (Políticas e resoluções do sistema prisional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Saúde prisional e atenção integral no SUS": 22
- "2. Educação, qualificação e articulação com o trabalho": 21
- "3. Saúde mental e liberdade de consciência": 21
- "4. Mulheres privadas de liberdade e revistas de ingresso": 21
- "5. Sistema penitenciário federal: inclusão e transferência": 15

Já existem (não repita):
- Uma unidade prisional pretende limitar sua política de saúde à distribuição de remédios, s
- Um gestor avalia quais pessoas são beneficiárias diretas da PNAISP, diante de unidade que 
- Um Município com população prisional e Estado já aderente à PNAISP estuda formalizar sua p
- Uma direção organiza o planejamento educacional e pergunta que modalidades o Plano Estraté
- Ao elaborar um plano estadual para educação prisional, um grupo precisa identificar a coor
- Uma rede de educação prisional procura observar a Resolução CNPCP nº 3/2009, identificando
- Uma mulher estrangeira deixa unidade prisional e necessita acesso a políticas de trabalho 
- Um servidor prepara orientação sobre liberdade religiosa e encontra referência à Resolução
- A administração revisa procedimentos de ingresso e cita a Resolução CNPCP nº 5/2014. Qual 
- Ao avaliar pessoa com transtorno mental em conflito com a lei, uma equipe identifica a Res
- Durante inspeção, constata-se que a unidade não mantém prontuário clínico para ingressante
- Um Estado propõe receber apoio para o PEESP, mas não quer firmar termo de adesão nem apres
- Uma unidade de mulheres pretende combinar trabalho, estudo e cuidados de gestantes. Qual d
- Uma administração instalou equipe de saúde dentro do estabelecimento e pretende cadastrá-l
- Familiares de pessoas custodiadas e trabalhadores de uma unidade participam de campanha de
- Uma pessoa presa provisoriamente enfrenta risco à integridade no estabelecimento de origem
- Em 2026, uma decisão precisa estipular a duração inicial da custódia em penitenciária fede
- Após ameaça grave contra colaborador preso, três atores cogitam requerer transferência fed
- Um preso provisório é aceito em estabelecimento penal federal. O juízo de origem pergunta 
- Há extrema necessidade de retirar imediatamente um preso ameaçado da unidade de origem. So
- Uma instituição religiosa quer cadastrar voluntários no sistema prisional. Seu grupo possu
- O planejamento de uma unidade feminina registra número de vagas, mas não informa quantas i
- Faltando aproximadamente dois meses para o término da custódia federal, a unidade deve ale
- Uma equipe de controle de acesso propõe revista degradante de visitantes para compensar a 
- Em discussão sobre pessoa com sofrimento mental sujeita a medida de segurança, um integran
- Uma pessoa condenada ou provisoriamente presa pode ser atendida nas ações de saúde da PNAI
- Em unidade que reúne presos provisórios e definitivos, apenas os definitivos poderão ser c
- Se um estabelecimento penal oferecer educação superior a pessoas custodiadas, poderá fazê-
- Por instituir plano educacional nacional, o Decreto nº 7.626/2011 determina adesão obrigat
- Um projeto de educação prisional que inclui mobilização interinstitucional, valorização de
- A PNAMPE termina sua proteção institucional no momento da libertação da mulher e não prevê
- A Resolução CNPCP nº 4/2014 orienta ações sanitárias de avaliação inicial, prontuário e ar
- Ao atualizar manual sobre liberdade religiosa em 2026, é correto indicar a Resolução CNPCP
- A pessoa que não professa religião pode recusar atividades confessionais, e a recusa não a
- A Resolução CNPCP nº 5/2014 permanece em vigor como norma de referência das revistas de in
- Um preso provisório cuja integridade esteja ameaçada pode, em tese, ser transferido para e
- Por força do art. 10 da Lei nº 11.671/2008, o período máximo de permanência na prisão fede
- Uma alteração de 2026 na Lei nº 11.671/2008 introduziu preferência de recolhimento federal
- A renovação da permanência em unidade federal independe de fundamentação, desde que a admi
- Diante de risco imediato e fundamentado, o juízo federal pode autorizar transferência emer
- A Resolução CNPCP nº 1/2014, por tratar de pessoas com transtorno mental em conflito com a
- Uma unidade feminina que compatibiliza horários de estudo e trabalho e adequa tarefas à ma
- O voluntário religioso que atenda às demais condições de credenciamento previstas na Resol
- Pessoas custodiadas nos regimes aberto e semiaberto são preferencialmente atendidas na red
- Se um preso provisório é transferido para estabelecimento penal federal, o processo refere
```

## Pedido 17: Mato Grosso: Estatuto dos Servidores (LC 04/1990), Código de Ética (LC 112/2002) e processo administrativo (Lei 7.692/2002) (`mato-grosso.legislacao-servidores-etica`, 82 tópicos de edital)

```
Matéria: mato-grosso.legislacao-servidores-etica (Mato Grosso: Estatuto dos Servidores (LC 04/1990), Código de Ética (LC 112/2002) e processo administrativo (Lei 7.692/2002))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estatuto (LC 04/1990): provimento, posse, exercício e estabilidade": 25
- "2. Estatuto: vacância, movimentação, direitos e vantagens": 25
- "3. Estatuto: regime disciplinar e processo disciplinar": 25
- "4. Código de Ética (LC 112/2002) e processo administrativo (Lei 7.692/2002)": 25

Já existem (não repita):
- Segundo a LC 04/1990, a posse deve ocorrer no prazo improrrogável de 30 dias contados da p
- O servidor empossado em cargo efetivo tem, pela LC 04/1990, o seguinte prazo para entrar e
- NÃO é forma de provimento prevista na LC 04/1990:
- O estágio probatório previsto na LC 04/1990 tem duração de 24 meses, e a avaliação de dese
- Entre os fatores avaliados no estágio probatório pela LC 04/1990 NÃO está:
- Embora o texto da LC 04/1990 mencione estabilidade após 2 anos de efetivo exercício, a Con
- O retorno à atividade do servidor aposentado por invalidez, quando declarados insubsistent
- A readaptação do servidor, segundo a LC 04/1990, pode acarretar aumento ou redução de sua 
- A recondução do servidor estável ao cargo anteriormente ocupado decorre de:
- Pela LC 04/1990, o concurso público tem validade de até 2 anos, prorrogável uma única vez 
- O deslocamento do servidor, com o respectivo cargo, para o quadro de outro órgão com plano
- A remoção de ofício para outra localidade, baseada no interesse público, deve ser devidame
- O adicional por tempo de serviço previsto na LC 04/1990 corresponde a:
- O serviço noturno, prestado entre 22 horas e 5 horas, tem acréscimo de 25%, computando-se 
- Quanto às férias, a LC 04/1990 permite ao servidor, a pedido, parcelá-las em:
- A licença-prêmio por assiduidade da LC 04/1990 é de 3 meses a cada quinquênio ininterrupto
- Segundo a LC 04/1990, o direito de requerer prescreve em 5 anos quanto aos atos de demissã
- A exoneração de ofício do servidor efetivo ocorre, entre outros casos, quando ele, tendo t
- Pela LC 04/1990, o servidor deve cumprir as ordens superiores, exceto quando manifestament
- É proibido ao servidor, pela LC 04/1990, manter sob sua chefia imediata:
- NÃO é penalidade disciplinar prevista na LC 04/1990:
- A suspensão não pode exceder 90 dias e, por conveniência do serviço, pode ser convertida e
- Configura inassiduidade habitual, pela LC 04/1990, a falta ao serviço sem causa justificad
- A responsabilidade administrativa do servidor é afastada em caso de absolvição criminal qu
- A ação disciplinar relativa às penalidades de repreensão e suspensão prescreve, pela LC 04
- Da sindicância pode resultar diretamente a aplicação de suspensão por até 60 dias.
- O afastamento preventivo do servidor durante o processo disciplinar, pela LC 04/1990, pode
- No processo disciplinar da LC 04/1990, havendo dois ou mais indiciados, o prazo de defesa 
- A revisão do processo disciplinar, segundo a LC 04/1990:
- Segundo a LC 112/2002, a Comissão de Ética de cada órgão é integrada por três servidores p
- Pela LC 112/2002, a censura ética é a cominação aplicável aos servidores que:
- Para fins do Código de Ética de Mato Grosso, considera-se servidor público inclusive quem 
- No processo ético da LC 112/2002, o servidor é oficiado para manifestar-se no prazo de:
- A LC 112/2002 veda ao servidor apresentar-se embriagado no serviço ou fora dele.
- Segundo a Lei 7.692/2002, NÃO pode ser objeto de delegação:
- Pela Lei 7.692/2002, o direito de a Administração estadual invalidar atos de que decorram 
- Salvo disposição legal em contrário, o prazo para interposição de recurso administrativo n
- Pela Lei 7.692/2002, o recurso administrativo tem, em regra, efeito suspensivo automático.
- Na administração centralizada, a instância máxima para o recurso administrativo, salvo dis
- A Lei 7.692/2002 prevê que o requerimento não decidido em 120 dias pode ser considerado re
```

## Pedido 18: Relações internacionais e política externa brasileira (`atualidades.politica-externa-brasileira`, 81 tópicos de edital)

```
Matéria: atualidades.politica-externa-brasileira (Relações internacionais e política externa brasileira)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, atores, processos e paradigmas das relações internacionais": 18
- "2. A evolução da política externa brasileira desde 1945": 20
- "3. Organizações multilaterais e conferências internacionais": 21
- "4. Segurança internacional e coalizões: G20, IBAS e BRICS": 21
- "5. Brasil, América do Sul, Mercosul e integração regional": 20

Já existem (não repita):
- Uma empresa brasileira negocia investimentos no exterior enquanto o Estado define posição 
- Dois Estados ampliam equipamentos defensivos porque cada um interpreta a iniciativa milita
- Ao justificar aproximação com países diferentes durante a Guerra Fria, um texto descreve a
- Uma reportagem distingue a Assembleia Geral do Conselho de Segurança da ONU e pergunta sob
- Uma equipe planeja estudo sobre integração regional e procura o documento que fundou o Mer
- Ao comparar paradigmas de relações internacionais, uma pesquisadora destaca regras que dim
- Uma proposta de política externa invoca expressamente o parágrafo único do art. 4º da Cons
- Um estudo sobre coalizões do Sul Global trata do IBAS criado em 2003. Quais países formam 
- Uma delegação analisa a Conferência das Nações Unidas sobre Desenvolvimento Sustentável de
- Uma reunião internacional discute coordenação econômica entre 19 países, União Europeia e 
- Uma equipe confunde o G20 criado para negociação agrícola na OMC em 2003 com o G20 de coor
- O Conselho de Segurança examina resolução não procedimental com nove votos favoráveis e ab
- Uma proposta institucional tem por objetivo acompanhar controvérsias jurídicas entre Estad
- Uma comissão reconstrói a diplomacia brasileira e identifica a diversificação de relações 
- Uma entidade internacional pretende reunir representantes de todos os membros para a decis
- Uma análise sobre desarmamento afirma que o Brasil aderiu ao Tratado sobre a Não Prolifera
- Um texto sobre negociações climáticas afirma que COP, CMA e CMP são reuniões simultâneas, 
- Um relatório de integração sul-americana distingue protocolos do Mercosul sobre instituiçã
- Um governo pretende aderir a declaração de cúpula do G20 e apresenta o documento como trat
- Uma análise usa as categorias autonomia pela distância, participação e diversificação para
- Um material de 2026 utiliza tabela antiga em que a Bolívia aparece apenas como associada d
- O Conselho de Segurança discute resposta coletiva a ameaça à paz, enquanto país invoca def
- Uma notícia sobre o Novo Banco de Desenvolvimento afirma que, por existir instituição fina
- Ao comparar Eco-92, Rio+20 e COP30, um relatório pretende atribuir a cada conferência uma 
- Um governo apresenta o princípio constitucional de não intervenção como motivo para rejeit
- Em estudo sobre atores globais, uma organização não governamental de direitos humanos é cl
- Um trabalho histórico identifica a Política Externa Independente de 1961 a 1964 como a mes
- Um levantamento sobre a ONU registra que seu Conselho de Segurança possui cinco membros pe
- Uma apresentação do G20 econômico informa que seu formato de 2025 continha apenas vinte pa
- Ao estudar os documentos fundadores do Mercosul, uma equipe reconhece que o Tratado de Ass
- Em teoria das relações internacionais, um analista define anarquia como inexistência de qu
- Um Estado utiliza instituições internacionais para ampliar sua participação na elaboração 
- Em votação substantiva do Conselho de Segurança, a abstenção de membro permanente exerce a
- A cooperação entre Índia, Brasil e África do Sul, formalizada em 2003, contempla coordenaç
- Um parecer atribui ao Protocolo de Ushuaia de 1998 a disciplina principal de solução de co
- Em análise histórica, a participação brasileira na MINUSTAH de 2004 a 2017 é reconhecida c
- A Rio+20 realizada em 2012 foi a conferência em que se adotou formalmente o Acordo de Pari
- O art. 51 da Carta das Nações Unidas resguarda a legítima defesa individual ou coletiva di
- Como a Rio+20 e a COP30 decorreram de conferências no Brasil, seus documentos finais passa
- Segundo dados oficiais brasileiros sobre a composição do BRICS em 2025, a Indonésia passou
- A admissão da União Africana no G20 em 2023 converteu o fórum informal em organização inte
- A Constituição brasileira, art. 4º, parágrafo único, formula objetivo de integração para o
- Em 2026, a Bolívia ainda deve ser classificada como Estado associado em processo de adesão
- Embora o Conselho de Tutela tenha suspendido suas atividades ordinárias em 1994, continua 
- Uma análise sustenta que o Brasil aderiu ao TNP em 1968, no mesmo momento da assinatura or
```

## Pedido 19: Representação de dados e aritmética binária (`informatica.representacao-dados-binario`, 81 tópicos de edital)

```
Matéria: informatica.representacao-dados-binario (Representação de dados e aritmética binária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Bits, bytes e sistemas posicionais de numeração": 20
- "2. Conversões entre bases e representação de frações": 20
- "3. Inteiros com sinal, complemento de dois e operações binárias": 20
- "4. Ponto flutuante, caracteres e organização de bytes": 20
- "5. Dados, informação, conhecimento, inteligência e segurança": 20

Já existem (não repita):
- Um sensor transmite a palavra binária 10110₂ como inteiro sem sinal. Qual é o valor decima
- Uma tabela grava em hexadecimal o inteiro decimal 31. Qual cadeia de dígitos representa co
- Um registrador foi dividido em grupos de quatro bits para simplificar a leitura em hexadec
- Um campo binário sem sinal possui oito bits para identificar códigos distintos, incluindo 
- Uma medida fracionária é armazenada no padrão binário 0,11₂. Qual é seu valor decimal?
- Uma tabela contém o octal 55₈. Qual é sua representação binária, sem zeros iniciais desnec
- Duas máscaras de quatro bits, 1010₂ e 1100₂, são combinadas por XOR. Qual padrão será obti
- Um byte armazena 11111111₂ e o campo é interpretado em complemento de dois de oito bits. Q
- Um texto contém apenas a letra latina maiúscula A em UTF-8. Quantos bytes são usados para 
- Um arquivo tem exatamente 2 KiB de dados. Considerando byte de oito bits e prefixo binário
- Uma identificação numérica aparece como 73₈. Qual é seu valor na base decimal?
- Uma fração decimal exatamente igual a 0,375 deve ser representada em binário. Qual alterna
- Um campo assinado em complemento de dois de oito bits deve armazenar o menor inteiro possí
- Dois valores sem sinal de oito bits, 250 e 10, são somados em um registrador que conserva 
- Em complemento de dois com oito bits, calculam-se +100 e +50. O registrador apresenta 1001
- Uma palavra de 16 bits possui valor lógico hexadecimal 1234₁₆ e deve ser armazenada em ord
- Uma gravação utiliza a letra latina maiúscula A no código ASCII. Qual é seu valor hexadeci
- O byte FF₁₆ representa −1 em complemento de dois com oito bits. Para ampliar o valor sinal
- Uma máscara de seleção aplica a operação bit a bit 3A₁₆ AND 0F₁₆ a um byte. Qual é o resul
- Uma descrição do formato IEEE 754 binary32 deve indicar a divisão dos 32 bits em sinal, ex
- Um campo de 12 bits representa inteiros com sinal em complemento de dois. Qual intervalo d
- Um valor de configuração é escrito como AF₁₆. Um sistema de auditoria deve registrar seu e
- Uma coordenada é expressa na base hexadecimal como 2,A₁₆, com A na primeira posição após a
- Uma operação NOT é aplicada ao octeto 00001111₂. Mantendo a largura exatamente em oito bit
- Um arquivo contém o número 37, o horário e a unidade de medida; uma análise compara a séri
- Uma identificação formada por cinco bits admite 32 padrões distintos, embora seu maior val
- Em uma conversão hexadecimal, a letra A representa o número decimal onze, e a letra B repr
- A fração binária 0,1₂ corresponde exatamente a 0,5₁₀, pois a primeira posição fracionária 
- Para converter um número binário inteiro diretamente para octal, os bits devem ser agrupad
- No código ASCII, a letra latina maiúscula A corresponde ao inteiro decimal 65 e pode ser e
- Na representação digital contemporânea, um byte corresponde a quatro bits, enquanto um nib
- Em um campo de oito bits, o padrão 11111111₂ pode ser interpretado como 255 sem sinal ou −
- A fração decimal 0,1 pode ser armazenada exatamente em número finito de dígitos binários, 
- A fração decimal 3/8 é representada exatamente por 0,011₂, porque o segundo e o terceiro d
- Na soma de inteiros sinalizados em complemento de dois, qualquer carry além do bit mais si
- Aplicar AND entre 1100₂ e 1010₂ produz 1000₂, pois somente a posição em que ambos os opera
- Em UTF-8, cada valor escalar Unicode ocupa exatamente dois bytes, inclusive os caracteres 
- Uma memória anunciada como 2 KiB contém 2048 bytes segundo a convenção de prefixos binário
- Para estender o padrão 10000000₂, que representa −128 em oito bits de complemento de dois,
- Em oito bits sinalizados, 01111111₂ somado a 00000001₂ gera 10000000₂ e caracteriza overfl
- No formato IEEE 754 binary32, são armazenados 24 bits de fração e 7 bits de expoente, além
- Se a palavra 0x12345678 ocupar quatro bytes em memória little-endian, os bytes em endereço
- Converter bytes para texto hexadecimal ou Base64 fornece, por si só, confidencialidade cri
- Alterar sem autorização um registro armazenado compromete a integridade da informação, enq
- O valor octal 75₈ corresponde a 111101₂ e também a 3C₁₆, pois todas as três escritas possu
```

## Pedido 20: Arquitetura de segurança: Zero Trust, defesa em profundidade e ferramentas de proteção (`informatica.zero-trust-defesa-profundidade-ferramentas`, 81 tópicos de edital)

```
Matéria: informatica.zero-trust-defesa-profundidade-ferramentas (Arquitetura de segurança: Zero Trust, defesa em profundidade e ferramentas de proteção)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Zero Trust, identidade e privilégio mínimo": 29
- "2. Segmentação, firewalls, DMZ, proxy e controle de admissão": 16
- "3. IDS, IPS, endpoints, hardening e prevenção inicial": 15
- "4. Dados, SIEM, SOAR, DLP, CASB, UEBA e validação": 22
- "5. Segurança de nuvem, contêineres e arquitetura integrada": 18

Já existem (não repita):
- Uma estação autenticada conecta-se à rede interna e tenta abrir um repositório confidencia
- Em um ambiente Zero Trust, uma função lógica avalia políticas e contexto para decidir perm
- Uma equipe quer permitir que um usuário consulte relatórios sem alterar configurações da a
- Uma aplicação web pública recebe requisições maliciosas com padrões de injeção em parâmetr
- Uma organização publica seu servidor web em zona intermediária, com acesso restrito à rede
- Um sensor de rede inspeciona tráfego suspeito e alerta a equipe, mas não descarta pacotes.
- Um dispositivo apresenta processo suspeito, e a solução registra telemetria, permite inves
- Logs de autenticação, proxy e endpoint são correlacionados para identificar uma sequência 
- Um servidor Linux roteia tráfego entre duas redes. Deseja-se bloquear um fluxo que atraves
- Uma rede sem fio exige senha de usuário e avaliação de atualizações e agente de proteção d
- Uma pessoa precisa elevar privilégios por período curto para corrigir um serviço e depois 
- Uma plataforma detecta envio não autorizado de documento sigiloso para serviço externo, co
- Uma equipe centraliza alertas e quer executar um procedimento aprovado que abre incidente,
- Uma organização identifica diversos serviços SaaS utilizados sem gestão central e precisa 
- Em microsserviços distribuídos, uma política verifica a identidade do serviço chamador ant
- Uma política monitora tráfego leste-oeste entre serviços de mesma nuvem e impede conexões 
- Um gateway recebe decisão favorável de acesso emitida pelo mecanismo de política e libera 
- Uma empresa possui duas VLANs com roteamento amplo entre elas. Um administrador afirma que
- Um IDS marca tráfego legítimo de manutenção como ataque, e a equipe decide torná-lo IPS in
- Um painel cruza login incomum, download excessivo e alteração de privilégios para destacar
- Um contêiner contém credencial gravada em uma camada de imagem. A equipe remove a variável
- Uma solução BAS simulou com autorização tentativa de exfiltração e acionou alerta no SIEM.
- Dois serviços utilizam mTLS e ambos possuem certificados válidos. O serviço B recebe tenta
- Uma estação está conectada por VPN, mas apresenta malware e tenta usar credenciais válidas
- Um incidente começa em e-mail, passa a execução em endpoint e culmina em login suspeito na
- Após autenticar um usuário, uma arquitetura Zero Trust pode continuar considerando a postu
- Em uma aplicação protegida por WAF, a presença de filtros HTTP elimina a necessidade de pa
- Uma DMZ corretamente administrada permite hospedar serviços expostos em segmento separado,
- Se um sistema de segurança somente identifica um padrão suspeito, registra evidências e en
- Uma solução de EDR pode coletar eventos de processos e conexões de um endpoint e permitir 
- Uma organização que centraliza logs em SIEM automaticamente passa a dispor de todos os pro
- Uma política DLP pode inspecionar o contexto de uma transferência e impedir envio indevido
- Em Linux, a cadeia FORWARD da tabela filter do iptables destina-se principalmente a pacote
- Um endpoint passa por autenticação de rede, mas sua verificação de postura aponta ausência
- A tecnologia PAM tem por finalidade principal detectar injeção SQL em formulários HTTP e s
- Uma solução de XDR que recebe telemetria de correio eletrônico, identidade e endpoint pode
- Uma análise UEBA indica que uma conta executou atividade fora de sua linha de base; por is
- Em uma solução de segurança SaaS, CASB pode contribuir para descobrir serviços utilizados 
- Um contêiner de aplicação conta necessariamente com kernel independente do host; assim, a 
- Uma aplicação em nuvem autentica serviços mutuamente por mTLS, mas também exige autorizaçã
- A simples conclusão de um exercício BAS no qual o SIEM gerou alerta comprova que todos os 
- Uma regra de firewall stateful que permite respostas a conexões já autorizadas não signifi
- Como a microssegmentação controla comunicações entre workloads, um ambiente que a adota po
- Uma organização adiciona controles de identidade, segmentação, EDR, correlação de eventos 
- Se um serviço SaaS for contratado de provedor que gerencia a infraestrutura, a organização
```

## Pedido 21: Proteção ao trabalho do menor (`direito-trabalho.trabalho-menor`, 81 tópicos de edital)

```
Matéria: direito-trabalho.trabalho-menor (Proteção ao trabalho do menor)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Idade mínima, proteção integral e limites ao trabalho adolescente": 15
- "2. Aprendizagem profissional, contrato especial, salário e FGTS": 22
- "3. ECA: profissionalização, trabalho educativo e proteção escolar": 18
- "4. Jornada, férias, salário do adolescente e prescrição trabalhista": 17
- "5. Trabalho infantil, piores formas e participação artística excepcional": 28

Já existem (não repita):
- Uma adolescente de 15 anos deseja ser contratada como empregada comum em uma papelaria, em
- Uma pessoa de 17 anos foi designada para reposição de mercadorias das 23h às 2h, com adici
- Uma empresa inicia programa com adolescente de 14 anos, contrato escrito, anotação profiss
- Um aprendiz recebe remuneração mensal de R$ 1.200, considerada integralmente para seu FGTS
- Um aprendiz de 16 anos concluiu o ensino fundamental e seu programa prevê cinco horas prát
- Uma pessoa de 17 anos, empregada e regularmente matriculada na escola, pede férias no inte
- Um adolescente de 17 anos assinou recibo mensal de salário e, ao se desligar, pretende dar
- Uma entidade sem fins lucrativos desenvolve programa com adolescentes, oficinas supervisio
- Uma aprendiz de 15 anos tem cem horas mensais remuneráveis de atividades teóricas e prátic
- Um estabelecimento alcançado pelo art. 429 da CLT possui cem trabalhadores em funções que 
- Uma família pede que adolescente de 17 anos ajude todas as noites, entre 22h30 e 1h, no pe
- Uma pessoa encerrou emprego aos 17 anos e procura recuperar parcelas devidas. Ao analisar 
- Um programa formativo pretende substituir aulas regulares por atividades práticas e distri
- Um programa deseja contratar aprendiz de 25 anos com deficiência e aptidão para formação p
- Um empregador decide alterar as férias previstas no programa de aprendizagem para uma sema
- Um adolescente de 17 anos é direcionado para um escritório administrativo separado de máqu
- Uma produtora pretende incluir criança de 11 anos em gravações comerciais remuneradas e af
- Em análise de exploração de adolescente de 17 anos para transporte de substâncias ilícitas
- Um empregado adolescente de 17 anos trabalha quatro horas pela manhã em um estabelecimento
- Ao estudar a CLT, encontra-se no art. 413 a referência histórica a 48 horas semanais e a a
- Um curso tenta usar o art. 64 do ECA para admitir trabalho de pessoa de 13 anos com uma 'b
- Um aprendiz com deficiência tem desempenho considerado insuficiente, mas o próprio program
- Uma adolescente de 17 anos recebe salário igual ao de colegas adultos para a mesma função,
- Um produtor obtém alvará da Justiça da Infância para participação artística específica. De
- Uma oficina inclui atividade da Lista TIP, e um gestor afirma que o Decreto nº 6.481/2008 
- Uma pessoa de 14 anos pode ingressar em contrato especial de aprendizagem válido, mas não 
- Um empregado de 17 anos pode trabalhar de 22h a 2h se receber adicional noturno e demonstr
- O contrato de aprendizagem é contrato especial de emprego ajustado por escrito e por prazo
- O FGTS do aprendiz é depositado à alíquota geral de 8%, pois a redução para 2% aplica-se s
- O ECA, art. 63, exige que a formação técnico-profissional do adolescente preserve acesso e
- O aprendiz com ensino fundamental concluído pode trabalhar oito horas práticas e, depois d
- A Convenção nº 182 da OIT inclui nas piores formas de trabalho infantil exploração sexual 
- A redação do art. 60 do ECA, que contém referência à idade de 14 anos, autoriza atualmente
- Um aprendiz de jornada reduzida pode ter remuneração mensal inferior ao mínimo mensal inte
- Um programa de trabalho educativo deixa de ser educativo automaticamente quando adolescent
- Um empregado estudante com 17 anos possui direito específico de fazer coincidir suas féria
- A prescrição de créditos trabalhistas de uma pessoa com 17 anos corre normalmente enquanto
- O limite máximo de idade de 24 anos previsto na aprendizagem não se aplica a aprendizes co
- A Lista TIP proíbe certas atividades apenas para menores que mantenham vínculo formal de e
- A cautelar referendada na ADI nº 5.326 tratou da autorização de participação artística de 
- O art. 8º da Convenção nº 138 da OIT autoriza exceções gerais e permanentes para toda prod
- Se uma pessoa de 17 anos trabalha em dois estabelecimentos, o art. 414 da CLT determina co
- O art. 413 da CLT conserva referências históricas a 48 horas semanais e acréscimo de 25% e
- O Decreto nº 6.481/2008 prevê vedação a menores de 18 anos nas atividades da Lista TIP, ad
- Conforme o art. 433, I, da CLT, a ausência de tecnologias assistivas e suporte necessário 
```

## Pedido 22: Arquitetura e urbanismo: história, teoria e patrimônio (`arquitetura.historia-teoria-patrimonio-restauro`, 81 tópicos de edital)

```
Matéria: arquitetura.historia-teoria-patrimonio-restauro (Arquitetura e urbanismo: história, teoria e patrimônio)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos culturais e arquitetura da Antiguidade à Idade Média": 22
- "2. Renascimento, barroco, industrialização e evolução das cidades": 21
- "3. Arquitetura moderna, contemporânea e produção brasileira": 21
- "4. Patrimônio histórico-cultural urbano e instrumentos de proteção": 19
- "5. Conservação, restauro, critérios de intervenção e leitura crítica do patrimônio": 17

Já existem (não repita):
- Uma biblioteca foi projetada buscando estabilidade da estrutura, adequação de seus espaços
- O estudo de edifícios de grande vão na Roma antiga mostra emprego de sistemas que distribu
- Em comparação entre igrejas românicas e góticas, a descrição deve relacionar formas de abe
- Na análise de uma igreja do século XV, observam-se perspectiva geométrica, composição prop
- Uma cidade do século XIX recebeu novos bulevares, infraestrutura e grandes intervenções so
- No estudo da arquitetura moderna, uma escola fundada em 1919 procurou integrar ensino artí
- Um relatório de Brasília diferencia o responsável pelo desenho do Plano Piloto do autor do
- Uma comunidade deseja proteger uma celebração transmitida entre gerações, incluindo seus r
- O proprietário de uma casa tombada deseja repintar sua fachada. Conforme o art. 17 do Decr
- Em um curso de história urbana, quatro propostas são examinadas: reformas de Paris, expans
- Ao avaliar um projeto de Le Corbusier com lajes sustentadas por pilares afastados da vedaç
- Uma proposta para conjunto urbano protegido mantém cada fachada isoladamente, mas destrói 
- Um imóvel particular está em procedimento federal de tombamento, e seu proprietário foi no
- Uma praça está situada ao lado de edifício federalmente tombado. Um projeto publicitário p
- A intervenção em edifício antigo pretende recuperar partes com fissuras e infiltrações. O 
- Uma celebração tradicional foi registrada como patrimônio cultural imaterial em âmbito fed
- Ao comparar dois projetos do século XX, um defende separação funcional e grandes eixos de 
- Um edifício erguido em 1890 apresenta arcos ogivais, vitrais e pináculos, inspirados em ca
- Um proprietário particular recebe notificação no procedimento de tombamento compulsório fe
- Em uma pesquisa sobre arquitetura colonial brasileira, a equipe observa igrejas barrocas m
- Um antigo mercado coberto será adaptado para biblioteca pública. O projeto mantém elemento
- Um projeto de restauro propõe remover integralmente reformas de diferentes épocas que poss
- Uma celebração tradicional depende tanto de um espaço histórico quanto de conhecimentos tr
- O projeto de um edifício cultural brasileiro do século XX enfatiza estrutura expressiva, a
- Uma proposta de revitalização pretende ocupar vazios de um conjunto histórico, mas conside
- Uma praça grega com mercados e assembleias é analisada como ágora, enquanto a área elevada
- Ao comparar construções medievais europeias, um pesquisador identifica o românico principa
- Em um estudo sobre a expansão de Barcelona, os quarteirões chanfrados do Eixample são corr
- O Plano Piloto de Brasília foi concebido por Oscar Niemeyer, enquanto Lucio Costa ficou re
- Uma tradição artesanal transmitida por mestres de uma comunidade pode integrar o patrimôni
- O proprietário de uma edificação federalmente tombada pode iniciar sua restauração sem aut
- Um pesquisador identifica ferro e vidro em grandes coberturas de estações ferroviárias do 
- A proposta da cidade-jardim de Ebenezer Howard se limitava a determinar o uso de vegetação
- Um projeto moderno utiliza estrutura independente das vedações, permitindo planta livre e 
- Um edifício moderno de interesse cultural permanece sem tombamento federal. Disso decorre 
- Um centro histórico reúne ruas de origem colonial, edifícios ecléticos e práticas culturai
- De acordo com o Decreto-Lei nº 25/1937, construir ao lado de um imóvel tombado é questão e
- Antes de restaurar um sobrado histórico com umidade ascendente, uma equipe examina infiltr
- Uma reforma de conjunto tombado pode remover edifícios de épocas distintas e reconstruir f
- A notificação para tombamento compulsório de bem privado no regime federal abre ao proprie
- Um pesquisador classifica obra brasileira de 1960 como desprovida de potencial valor histó
- A descrição da UNESCO para Brasília diferencia os dois grandes eixos do Plano Piloto, o si
- Uma comunidade reconhecida por uma celebração registrada deve manter seus ritos rigidament
- A adaptação de edifício histórico para novo uso pode ser compatível com a preservação se f
- Ao planejar intervenção em centro histórico, basta verificar se cada edificação está tomba
```

## Pedido 23: Procedimentos ordinário, sumário e sumaríssimo (`processo-trabalho.procedimentos`, 80 tópicos de edital)

```
Matéria: processo-trabalho.procedimentos (Procedimentos ordinário, sumário e sumaríssimo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Classificação dos ritos, alçada e escolha do procedimento": 28
- "2. Petição inicial, notificação, audiência e defesa nos ritos": 19
- "3. Provas, testemunhas e perícia no sumaríssimo": 19
- "4. Sentença, rito ordinário e recurso ordinário do sumaríssimo": 17
- "5. Revista no sumaríssimo, jurisprudência e roteiro de resolução": 17

Já existem (não repita):
- Uma reclamação trabalhista individual de 25 salários mínimos, ajuizada contra empregador p
- Uma reclamação de valor equivalente a exatamente quarenta salários mínimos na data em que 
- Uma pessoa propõe reclamação de 18 salários mínimos contra fundação pública. Qual particul
- Uma reclamação sujeita ao sumaríssimo é apresentada sem endereço correto do reclamado, e o
- Uma reclamação trabalhista segue o procedimento sumaríssimo, sem questão especial sobre o 
- Num processo sumaríssimo, é produzida prova pericial admitida pelo juiz, e o laudo é dispo
- Uma decisão definitiva de primeiro grau foi proferida em reclamação processada no rito sum
- O juiz proferiu sentença em procedimento sumaríssimo e deixou de elaborar o relatório trad
- Uma reclamação trabalhista de dois salários mínimos, sem questão especial, é enquadrada co
- Uma empresa pública é reclamada em dissídio individual de 28 salários mínimos. A parte aut
- Uma reclamação sumaríssima contém pedidos sem indicação dos valores correspondentes e tamb
- Uma testemunha da parte autora foi comprovadamente convidada para audiência sumaríssima, m
- Durante audiência sumaríssima, são apresentados documentos extensos pela parte contrária. 
- A audiência única do rito sumaríssimo foi interrompida por necessidade técnica fundamentad
- Em audiência sumaríssima, uma objeção processual precisa ser decidida para que se saiba se
- A sentença sumaríssima é confirmada integralmente por seus próprios fundamentos em recurso
- Um recorrente de decisão regional em rito sumaríssimo fundamenta sua revista somente em co
- Uma causa de 30 salários mínimos tramita no sumaríssimo e depende de prova técnica legalme
- Em uma ação de 1,8 salário mínimo classificada como dissídio de alçada, a sentença trata a
- O valor de uma causa de alçada era indeterminado e foi fixado pelo juiz após frustrada con
- Em processo sumaríssimo, o reclamado falta à audiência, mas seu advogado comparece com def
- O recurso ordinário de causa sumaríssima foi recebido pelo tribunal. A parte recorrente af
- No recurso de revista de processo sumaríssimo, a parte aponta somente violação de lei fede
- Em audiência sumaríssima, o juiz decide de plano uma exceção relevante ao prosseguimento, 
- Um acórdão de recurso ordinário sumaríssimo confirma sentença pelos próprios fundamentos. 
- Uma reclamação de trinta salários mínimos é ajuizada contra empresa privada e não envolve 
- Uma reclamação de exatamente quarenta salários mínimos deve necessariamente seguir rito or
- No procedimento sumaríssimo, a CLT, art. 852-B, II, proíbe a citação por edital e exige qu
- Em reclamação de rito sumaríssimo, cada parte pode apresentar até três testemunhas, porque
- Uma sentença proferida no rito sumaríssimo pode dispensar o relatório tradicional, mas dev
- O prazo para a parte interpor recurso ordinário contra decisão definitiva de primeiro grau
- Uma demanda sumaríssima necessita de perícia que a lei impõe para a prova de certo fato. O
- Uma parte leva ao sumaríssimo testemunha que faltou sem ter sido comprovadamente convidada
- No procedimento sumaríssimo, as partes e advogados devem comunicar mudanças de endereço; s
- A CLT exige que a manifestação das partes sobre laudo pericial no sumaríssimo se dê em cin
- O art. 852-G da CLT exige decisão de plano sobre incidentes e exceções capazes de interfer
- Quando a audiência do sumaríssimo é interrompida, a CLT impede qualquer prosseguimento pos
- Nas reclamações sumaríssimas, o recurso ordinário recebido pelo tribunal é imediatamente d
- Em processo de alçada de valor equivalente a 1,5 salário mínimo, a sentença decide controv
- Na revisão de valor indeterminado fixado para alçada nos termos do art. 2º da Lei 5.584/19
- Uma demanda de valor de dez salários mínimos ajuizada contra autarquia deve tramitar em ri
- No rito sumaríssimo, documento apresentado em audiência recebe manifestação imediata da pa
- Uma sentença sumaríssima devidamente fundamentada é confirmada pelos próprios motivos no t
- O recurso de revista em rito sumaríssimo pode fundar-se em violação direta da Constituição
- A interposição de recurso de revista no sumaríssimo pode ser fundamentada exclusivamente e
```

## Pedido 24: Fintechs, bancos digitais e inovação financeira (`conhecimentos-bancarios.fintechs-inovacao-financeira`, 80 tópicos de edital)

```
Matéria: conhecimentos-bancarios.fintechs-inovacao-financeira (Fintechs, bancos digitais e inovação financeira)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Transformação digital e canais bancários": 14
- "2. Fintechs, startups, big techs e bancos digitais": 27
- "3. Fintechs de crédito, autorização e novas formas de financiamento": 14
- "4. Open Finance, pagamentos, marketplace e segmentação": 29
- "5. Shadow banking, ecossistemas digitais e avaliação de riscos": 16

Já existem (não repita):
- Uma instituição já autorizada como banco passou a abrir contas e atender clientes principa
- Uma cliente emite extrato no portal do banco pelo navegador e depois bloqueia o cartão no 
- Uma pessoa inicia uma reclamação no aplicativo, continua por telefone e recebe solução sem
- Um banco substituiu a coleta manual de propostas por identificação remota, análise integra
- Um cliente recebe mensagem falsa e aprova no aplicativo uma operação apresentada como atua
- Uma empresa de tecnologia desenvolve avaliação de crédito para instituições licenciadas, m
- Uma plataforma apresenta ao consumidor seguros de empresas distintas, com filtros de preço
- Uma empresa autorizada gerencia contas pré-pagas e transações de seus usuários, sem realiz
- Uma grande empresa de tecnologia integra pagamentos de parceiro autorizado à sua plataform
- Um aplicativo de varejo oferece parcelamento contratado com uma instituição financeira par
- Uma instituição financeira oferece, em plataforma eletrônica própria, financiamento e aqui
- Uma plataforma autorizada aproxima investidores que emprestam recursos e pessoas que desej
- Durante revisão de material, uma equipe afirma que a Resolução nº 4.656/2018 ainda é a nor
- Uma sociedade pretende operar como SEP e emitir ofertas reguladas de crédito entre pessoas
- Empresa inovadora com oito anos de CNPJ, receita de R$ 12 milhões no ano anterior e requis
- Uma plataforma usa histórico de transações para identificar clientes que precisam de ferra
- Empresa permite que estabelecimentos publiquem produtos, consumidores os encontrem e trans
- Para avaliar um empréstimo, uma instituição solicita acesso autorizado aos dados da conta 
- Uma carteira digital realiza Pix a partir de conta de pagamento pré-paga elegível. A opera
- Um serviço financeiro quer ampliar adesão de pessoas com baixa familiaridade digital sem e
- Um fundo não bancário compra carteiras de crédito e obtém recursos de investidores, criand
- Um veículo compra créditos de vencimento em cinco anos e oferece resgate de recursos em pr
- Um marketplace reúne empréstimos ofertados por entidades autorizadas; a vitrine recebe com
- Plataforma de pagamentos ganha utilidade para lojistas à medida que cresce o número de con
- Um aplicativo de varejo reúne conta de pagamento de IP, empréstimo de parceira financeira 
- Um banco mantém agência e aplicativo, de modo que o cliente consegue usar dois canais para
- Uma instituição de pagamento que mantém carteiras pré-pagas pode, por essa autorização, co
- Uma pessoa que bloqueia cartão pelo aplicativo do telefone utiliza um exemplo de mobile ba
- Uma plataforma que compara financiamentos de credoras distintas se torna automaticamente t
- Uma fintech pode ser empresa consolidada, ao passo que uma startup pode inovar em setor al
- No Open Finance, a instituição recebedora pode acessar os dados individuais de outra parti
- SCD e SEP, quando atuam nas atividades regulamentadas correspondentes, são modalidades de 
- Como SCD e SEP usam plataformas eletrônicas, a mesma pessoa jurídica pode operar simultane
- Ao segmentar usuários por frequência de transações, uma instituição deve avaliar a qualida
- Toda entidade integrante da intermediação financeira não bancária é clandestina, pois sua 
- Ao autorizar compartilhamento de dados pelo Open Finance, o cliente participa de fluxo ent
- Em 2026, a Resolução nº 4.656/2018 permanece integralmente vigente e deve prevalecer sobre
- Para o enquadramento especial de startup da LC nº 182/2021, os limites gerais incluem até 
- A SEP difere da SCD apenas no nome, pois sua atividade central também é conceder empréstim
- A integração de sistemas via APIs permite que uma instituição conecte serviços ao canal di
- Recursos em conta de pagamento pré-paga mantida por instituição de pagamento contam necess
- A alavancagem e a possibilidade de saídas rápidas de investidores podem amplificar perdas 
- Toda operação Pix utiliza blockchain como mecanismo necessário de consenso descentralizado
- Uma estrutura de BaaS pode permitir que parceiro comercial ofereça experiência financeira 
- Segundo a LGPD, todo tratamento de dados pessoais para segmentação de clientes depende nec
```

## Pedido 25: TCE-RO: Lei Orgânica e Regimento Interno (`controle-externo.tce-ro-lei-organica-regimento`, 79 tópicos de edital)

```
Matéria: controle-externo.tce-ro-lei-organica-regimento (TCE-RO: Lei Orgânica e Regimento Interno)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, competências e jurisdição do TCE-RO": 19
- "2. Prestação de contas, tomada de contas e julgamento": 15
- "3. Fiscalização, denúncias, representações e processos": 19
- "4. Direito de defesa, decisões e recursos": 15
- "5. Sanções, cautelares, organização e disposições transitórias": 32

Já existem (não repita):
- Um relatório examina as contas anuais de governo apresentadas por um Prefeito ao TCE-RO. Q
- Uma entidade privada aplicou verbas municipais recebidas por convênio e questionou sua suj
- Na análise de um ato de admissão para cargo efetivo, constatou-se necessidade de exame de 
- Em processo de contas, o relator determinou a juntada de demonstrativos e adiou a análise 
- Durante exame anual, detectou-se erro formal corrigível na documentação, sem dano ao erári
- A apuração identificou pagamento indevido que causou prejuízo aos cofres municipais. Qual 
- O TCE-RO julgou regulares as contas de um administrador, após demonstrada a correção de su
- Uma associação pretende informar possível desvio de recursos públicos ao TCE-RO. Qual inst
- Um gestor solicita orientação abstrata sobre o significado de uma exigência de prestação d
- Em decisão de contas, uma parte aponta contradição interna entre fundamentação e dispositi
- Um administrador busca impugnar decisão de julgamento de contas por erro na apreciação de 
- Uma auditoria já concluída detectou desvio financeiro e foi proferido acórdão condenatório
- Uma apuração regimental indica indício de dano e necessidade de ouvir a pessoa apontada co
- Na conferência dos prazos processuais, uma parte sustenta que os prazos regimentais correm
- Na fiscalização de contrato administrativo, o Tribunal proferiu decisão impugnável pela vi
- A equipe técnica indicou fraude comprovada praticada por licitante, e o caso chegou ao órg
- A apuração revelou dano material e risco concreto de dissipação de patrimônio capaz de fru
- A lei orgânica consolidada apresenta regra histórica sobre indicações para as sete vagas d
- Após julgar contas, surgiram documentos novos com eficácia sobre a prova que sustentou a d
- Em relatório técnico, houve indicação de infração grave sem débito, e o responsável alegou
- Uma notícia de irregularidade foi autuada no TCE-RO como PAP e passou por seletividade ant
- Em decisão urgente, o relator vislumbra risco de dano e ineficácia do julgamento final. O 
- Um processo eletrônico é distribuído e instruído, mas uma parte pretende escolher o consel
- O Tribunal discute possível infração grave praticada pelo responsável, com repercussões so
- Durante sessão de julgamento, um processo classificado para deliberação por relação envolv
- Um cidadão apresenta notícia fundamentada de desvio de bens municipais. À luz do art. 79 d
- Em processo de contas de um dirigente de autarquia, o TCE-RO deverá emitir parecer prévio 
- A simples gestão de recursos públicos repassados mediante convênio pode sujeitar um partic
- A existência de erro formal sem dano ao erário conduz, pela Lei Orgânica, à declaração de 
- Ao constatar omissão no dever de prestar contas e possível prejuízo, a autoridade administ
- Uma decisão preliminar que ordena citação de responsável tem o mesmo conteúdo jurídico da 
- Em processo de contas com débito, a decisão final que imputa pagamento da dívida possui ef
- Um responsável que perdeu prazo de reconsideração pode interpor recurso de revisão de cont
- Uma consulta regularmente recebida pelo TCE-RO sobre dúvida de interpretação jurídica pode
- Os embargos de declaração previstos na Lei Orgânica do TCE-RO são apresentados em quinze d
- Em apuração de possível irregularidade no TCE-RO, a utilização do Procedimento Apuratório 
- A multa do art. 54 da Lei Orgânica, aplicada quando houver responsável julgado em débito, 
- Para processo de fiscalização de contrato, o responsável opôs reconsideração por ser essa 
- A indisponibilidade de bens prevista no art. 107, § 2º, do Regimento pode funcionar como g
- A proporção de cinco escolhas legislativas e duas executivas prevista em versão histórica 
- Diante de irregularidade sem débito, o relator pode determinar audiência para que o aponta
- O art. 96 do Regimento admite revisão de parecer prévio sobre contas anuais do Prefeito, c
- Para fins de fiscalização patrimonial, a unidade técnica do TCE-RO pode contribuir com exa
- Um conselheiro pretende inserir processo de auditoria operacional na relação de julgamento
- Mesmo que uma tutela de urgência seja deferida sem manifestação prévia do requerido para e
```

## Pedido 26: Regimento Interno do TRF da 1ª Região (`direito.regimento-interno-trf1`, 79 tópicos de edital)

```
Matéria: direito.regimento-interno-trf1 (Regimento Interno do TRF da 1ª Região)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Composição e órgãos": 25
- "2. Especialização e competência": 25
- "3. Direção do Tribunal e atribuições": 25
- "4. Relator, sessões, prazos e recursos": 25

Já existem (não repita):
- No texto do regimento, o TRF da 1ª Região compõe-se de vinte e sete desembargadores federa
- A Corte Especial, no texto do regimento, é constituída de:
- O Tribunal possui seis Turmas de quatro desembargadores cada, e a Primeira e a Segunda Tur
- A Turma Especial de Férias exerce atividade jurisdicional nos períodos de:
- O presidente, o vice-presidente e o corregedor-geral integram normalmente as Turmas e as S
- As Seções e as Turmas são presididas:
- O Conselho de Administração é órgão destinado à formulação e implantação das políticas adm
- A competência do Plenário e da Corte Especial está sujeita às áreas de especialização das 
- Compete à Primeira Seção o processo e julgamento dos feitos relativos a:
- A matéria penal em geral, ressalvados os casos de competência originária da Corte Especial
- Uma ação sobre licitação e contrato administrativo, em grau de recurso, será julgada pela 
- Compete ao Plenário eleger o presidente, o vice-presidente e o corregedor-geral para manda
- Entre as competências do Plenário, inclui-se:
- Compete à Corte Especial processar e julgar, nos crimes comuns e de responsabilidade, os j
- Na sua função administrativa, compete à Corte Especial, entre outras atribuições:
- O mandado de segurança impetrado contra ato de juiz federal é julgado pela:
- As Seções julgam os conflitos de competência, nas matérias de sua área, verificados entre 
- Compete às Turmas, dentro da respectiva área, processar e julgar:
- A Turma que primeiro conhecer de um processo, incidente ou recurso terá a jurisdição preve
- As Seções e as Turmas podem remeter feitos à Corte Especial quando houver:
- O presidente, o vice-presidente e o corregedor-geral são eleitos entre os desembargadores 
- A eleição dos dirigentes do Tribunal ocorre por voto secreto do Plenário:
- Ocorrendo vacância do cargo de vice-presidente, o Plenário será convocado para eleger o su
- Ao presidente do Tribunal compete, entre outras atribuições:
- O presidente do Tribunal convoca e preside as sessões do Plenário, da Corte Especial e do 
- Ao corregedor-geral compete, entre outras atribuições:
- O corregedor-geral pode impor aos servidores da Justiça Federal as penalidades de censura,
- O presidente de Seção, ao presidir as sessões:
- O corregedor-geral pode expedir provimentos necessários ao funcionamento dos serviços fore
- Ao relator incumbe, entre outras atribuições:
- O relator pode homologar desistências, ainda que o feito se encontre em pauta para julgame
- Segundo o regimento, não há sustentação oral no julgamento de:
- Na sustentação oral, cada parte fala pelo tempo máximo de quinze minutos, exceto na ação p
- Na concessão de preferência para sustentação oral, o regimento dá prioridade aos advogados
- Os prazos processuais correm normalmente durante o recesso e as férias forenses.
- Mediante pedido conjunto das partes, o relator pode admitir prorrogação de prazo por tempo
- Cabe recurso ordinário ao Superior Tribunal de Justiça contra as decisões do Tribunal dene
- As Turmas podem remeter os feitos de sua competência à Seção quando:
- A Turma Especial de Férias é presidida pelo desembargador mais antigo, salvo se o presiden
- O Plenário do Tribunal é constituído:
```

## Pedido 27: Redação dissertativo-argumentativa (`portugues.redacao-dissertativo-argumentativa`, 78 tópicos de edital)

```
Matéria: portugues.redacao-dissertativo-argumentativa (Redação dissertativo-argumentativa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Compreensão da proposta, delimitação do tema e projeto de texto": 19
- "2. Introdução, organização dos parágrafos e progressão do texto": 19
- "3. Fundamentação conceitual e factual, repertório e avaliação de argumentos": 24
- "4. Coesão, coerência, progressão referencial e correção linguística": 20
- "5. Conclusão, intervenção quando pertinente e revisão da redação": 18

Já existem (não repita):
- Uma proposta solicita discutir as barreiras à leitura entre jovens de áreas rurais. Qual t
- No planejamento de uma redação sobre desperdício de água, o autor identifica uma pergunta 
- Um texto abre com o tema mobilidade urbana e enuncia que a insuficiência de integração e o
- Um parágrafo afirma que linhas de ônibus escassas dificultam o acesso a museus. Qual frase
- Em redação sobre acesso a parques, o estudante cita um levantamento existente e explica co
- O trecho 'A prefeitura ampliou os pontos de coleta. Esse serviço, contudo, continua irregu
- Um autor precisa marcar oposição entre 'O acervo foi ampliado' e 'A frequência de visitant
- Uma proposta pede analisar causas do descarte irregular de lixo, sem exigir medidas. Qual 
- A redação apresenta três argumentos diferentes, cada qual exige explicação própria, mas o 
- Uma versão de texto contém 'É de suma e extrema importância ressaltar a importância da que
- A proposta aborda dificuldades de acesso a serviços de saúde em municípios pequenos. O tex
- Num parágrafo sobre bibliotecas, o autor escreve 'a biblioteca ajuda porque auxilia, e aux
- Para defender que a instalação de computadores melhora a aprendizagem, o autor afirma que 
- Em texto sobre cultura, lê-se: 'Os bairros construíram centros culturais. Portanto, a freq
- O texto analisa falta de transporte para equipamentos culturais e, ao final, propõe apenas
- Um redator insere a frase 'Como afirmou um grande pensador, educação é tudo', sem informar
- Uma proposta sobre acesso à informação exige que o texto discuta qualidade da comunicação 
- No trecho 'O município informou à associação que ela seria responsável pela inscrição', o 
- A proposta pede uma dissertação sobre inclusão cultural. Um exemplo de um único projeto mu
- A frase 'As políticas de leitura, que foram apresentadas no documento, precisa de financia
- Uma proposta pede discutir como desigualdade territorial afeta acesso a lazer público. O a
- Uma redação defende que a falta de acervos e a jornada de trabalho restringem a leitura. O
- Um texto diz: 'Embora a campanha tenha ampliado informações, ela não eliminou a barreira d
- No projeto de redação sobre coleta seletiva, o primeiro argumento aponta ausência de rotas
- Uma tese afirma que a informatização pode reduzir filas se acompanhada de treinamento e su
- Em uma proposta sobre o acesso de idosos a serviços digitais, o autor que identifica essa 
- Um texto que defende ampliação de bibliotecas pode desenvolver apenas frases como 'bibliot
- Em texto que defende melhoria de espaços públicos, apresentar uma estatística identificada
- Uma redação que utiliza 'contudo' e 'portanto' várias vezes apresenta coerência assegurada
- Se a proposta exige apresentar medidas concretas para reduzir desperdício de alimentos, um
- Numa proposta sobre obstáculos à escolarização rural, um texto que discute exclusivamente 
- Um parágrafo argumentativo pode iniciar com tópico frasal, desenvolver mecanismo que o jus
- Um autor usa um único exemplo de bairro com praça renovada e conclui que toda cidade do pa
- Na frase 'As bibliotecas ampliaram horários; essa mudança facilitou visitas', a expressão 
- A intervenção social constitui obrigação estrutural de toda redação dissertativo-argumenta
- Uma tese sustenta que ampliar oferta de cursos reduz barreiras de qualificação; reconhecer
- Se uma redação apresenta tese sobre precariedade da infraestrutura e depois conclui que o 
- Ao avaliar a frase 'A evasão decorre de fatores econômicos, portanto a política de transpo
- Uma porcentagem citada sem período, população observada ou fonte se torna automaticamente 
- Ao propor uma política de acesso à leitura, indicar agente executor, ação, meio e finalida
- Em redação sobre acessibilidade urbana, o autor pode trocar 'pessoas com deficiência' por 
- O texto que distingue falta de acesso aos acervos de ausência de mediação de leitura apres
- Uma comparação argumentativa entre bairros com densidades e condições urbanas muito difere
- Quando a proposta exige redação formal, revisar a concordância em 'As medidas melhora o ac
- Um texto atende integralmente à proposta sobre impactos da desinformação na participação c
```

## Pedido 28: Psicologia social, comunitária e de grupos (`psicologia.social-comunitaria-grupos`, 78 tópicos de edital)

```
Matéria: psicologia.social-comunitaria-grupos (Psicologia social, comunitária e de grupos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, objeto e tradições da psicologia social": 17
- "2. Atitudes, cognição social, preconceito e influência": 17
- "3. Identidade, linguagem, representações e interação social": 17
- "4. Psicologia comunitária, território e políticas públicas": 17
- "5. Processos grupais, dinâmicas e grupos operativos": 16
- "6. Pesquisa em psicologia social: desenhos, ética e interpretação": 16

Já existem (não repita):
- A psicologia social investiga fenômenos que articulam sujeitos, grupos, instituições e con
- Estereótipos, preconceitos e discriminações são três nomes equivalentes para qualquer opin
- A conformidade pode ocorrer quando alguém modifica sua resposta para acompanhar a maioria,
- No estudo das representações sociais, ancoragem consiste em interpretar uma novidade a par
- Na psicologia comunitária, o território se reduz à delimitação cartográfica oficial e não 
- Em grupos operativos, a tarefa explícita é o objetivo declarado que orienta o trabalho col
- Uma correlação estatística entre duas variáveis demonstra necessariamente que a primeira c
- Uma análise de conflitos em serviço público que examine apenas a personalidade de servidor
- A influência social informacional ocorre quando uma pessoa adere ao comportamento do grupo
- Uma regra de atendimento redigida de forma igual para todos não pode ter efeitos discrimin
- A identidade social envolve pertencimentos coletivos e pode variar de saliência conforme o
- Convidar moradores para ratificar uma solução já integralmente definida pela equipe técnic
- Nos grupos operativos, verticalidade corresponde à história singular dos membros e horizon
- Amostragem por conveniência e amostragem probabilística fornecem sempre a mesma base para 
- É coerente com a psicologia social afirmar que conflitos podem revelar disputas de sentido
- A redução da dissonância cognitiva requer, em todos os casos, que a pessoa altere imediata
- A objetivação nas representações sociais transforma uma ideia abstrata em figura ou forma 
- Uma intervenção que articula escolas, assistência social e atenção básica pode ser interse
- No referencial pichoniano, o porta-voz deve ser identificado necessariamente pelo cargo de
- Em grupo focal, a retirada dos nomes na transcrição garante anonimato integral, mesmo quan
- Uma banca pede a definição mais abrangente do objeto da psicologia social. Assinale a alte
- Ao ouvir a frase “todas as pessoas daquele bairro são desonestas”, um pesquisador identifi
- Uma estudante muda a resposta para não contrariar colegas e teme ser rejeitada. O mecanism
- Segundo a abordagem das representações sociais, a operação de tornar uma noção abstrata ma
- Uma equipe inicia projeto comunitário e deseja evitar uma intervenção tutelar. Qual condut
- Em um grupo operativo de profissionais de saúde, a tarefa explícita é elaborar um fluxo de
- Sobre pesquisa social, o que define um estudo longitudinal?
- Em uma dinâmica de trabalho, pessoas permanecem juntas por partilharem objetivos e vínculo
- Uma coordenadora atribui faltas frequentes exclusivamente à “preguiça” dos usuários, sem e
- Um formulário digital único impede, sem adaptação justificável, acesso de usuários que dep
- Ao comparar expressões como “nós, moradores antigos” e “eles, recém-chegados”, uma pesquis
- Uma equipe realiza encontros no CRAS, mas encaminha demandas à saúde e à educação sem arti
- Durante uma reunião, a equipe evita qualquer objeção por receio de parecer desleal, ignora
- Pesquisadores aplicaram questionário por conveniência a 30 frequentadores de um equipament
- Uma pessoa muda de opinião porque percebe que colegas têm dados melhores sobre risco de en
- Durante um diagnóstico territorial, profissionais perguntam a jovens sobre mobilidade, seg
- Em grupo operativo, uma participante verbaliza receio compartilhado sobre mudanças, embora
- A associação de uma tecnologia desconhecida à imagem de uma “biblioteca que conversa” pode
- Uma pesquisa encontra menos participação nas assembleias e propõe analisar regras de convo
- Ao propor ação com migrantes, uma equipe decide impor exclusivamente seus indicadores e ut
- Uma coordenação identifica que, embora o grupo declare elaborar um plano, os encontros são
- Em uma pesquisa por grupo focal entre subordinados e seus supervisores, qual planejamento 
- Um treinamento antidiscriminatório gera avaliações positivas, mas o processo seletivo mant
- Após discussão, uma equipe cuja posição inicial já era favorável a uma política passa a ap
- Um estudo observa maior adesão a programas entre participantes de oficinas; os participant
```

## Pedido 29: Auditoria fiscal digital: EFD ICMS/IPI e NF-e (`auditoria.efd-nfe-icms`, 78 tópicos de edital)

```
Matéria: auditoria.efd-nfe-icms (Auditoria fiscal digital: EFD ICMS/IPI e NF-e)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos normativos, organização da EFD e trilha de auditoria": 15
- "2. Documentos fiscais: NF-e, NFC-e, CT-e e registros do Bloco C": 17
- "3. Inventário, produção, CIAP e apuração dos impostos": 17
- "4. Testes de créditos, bases de cálculo, alíquotas e substituição tributária": 36
- "5. Cruzamentos com SGBD, amostragem e avaliação de achados": 15

Já existem (não repita):
- Um estabelecimento vai auditar sua EFD de março e precisa primeiro identificar titular, pe
- Na aquisição de caixas de um produto, a unidade comercial difere da unidade declarada no c
- Em um cruzamento de arquivos, a equipe precisa distinguir uma nota de venda eletrônica mod
- Ao examinar um arquivo XML de NF-e, o auditor precisa localizar a identificação da operaçã
- Uma planilha apresenta C400, C405, C420 e C425 relacionados a equipamento emissor de cupom
- A escrituração declara um lote de mercadorias no H010 e produção controlada em registros K
- Uma empresa contabilizou crédito potencial de ICMS na compra de máquina para seu imobiliza
- O responsável procura um registro que consolida a apuração do ICMS próprio e outro que dis
- Em 2026, a aquisição de materiais destinados apenas a uso e consumo interno gerou crédito 
- Um arquivo mostra uma NF-e modelo 55 e outro contém uma NFC-e modelo 65. Qual par de atos 
- Uma NF-e de emissão própria aparece no C100 sem correspondente C170. O auditor constata si
- Uma revenda interestadual de mercadoria sujeita anteriormente a ICMS-ST gera discussão sob
- Na escrituração de uma nota, foram inseridos texto explicativo e código de ajuste por docu
- O estoque está expresso em unidades no 0200. Um documento de entrada traz 40 caixas, com 1
- Em determinado mês, o CIAP evidencia crédito potencial de 9.600 relativo a bem elegível e 
- O XML de uma NF-e contém valor total de ICMS em W, mas o valor global do E110 difere da so
- Um contribuinte transmite EFD validada, mas apropriou crédito de ICMS de material destinad
- Uma operação com ST tem BC-ST de 3.000, alíquota interna aplicável de 18% e ICMS próprio d
- Em banco de dados, uma NF-e tem quatro itens e dois ajustes associados ao documento. Uma c
- No confronto de NF-e autorizadas com saídas escrituradas, um documento não aparece no arqu
- O inventário H010 indica 800 unidades ao final do mês anterior. No período auditado houve 
- A auditoria de 2026 reúne um arquivo EFD de 2025 e outro de setembro de 2026, ambos com do
- Uma fiscalização de mercadorias destinadas ao exterior identificou saídas sem destaque de 
- Um sistema identificou diferenças de ICMS-ST em notas de dois estados. O analista utilizou
- Uma apuração parece duplicar o ICMS de uma NF-e: há um registro C197 e um E111 com valores
- Um auditor verificou que o grupo D do XML não aparece na NF-e ordinária de venda de mercad
- Ao conferir uma venda registrada no ECF, a equipe afirmou que C405 identifica o equipament
- A NF-e modelo 55 de determinado fornecedor foi autorizada e corretamente assinada. O audit
- No cotejamento de NF-e modelo 55, a existência de valor de IPI no grupo N permite classifi
- Um teste de apuração deve tratar E110 como informação do ICMS próprio e E210 como informaç
- Um analista encontrou C197 preenchido e concluiu que esse registro substitui o E111, dispe
- Ao examinar máquina incorporada ao imobilizado, o auditor cruza documentos de entrada com 
- Uma NFC-e modelo 65, emitida pelo próprio contribuinte, deve ser obrigatoriamente escritur
- A verificação de estoque pode exigir comparar unidades do cadastro 0200 com unidades de do
- Uma auditoria de crédito de ICMS sobre material comprovadamente destinado apenas ao uso e 
- Um teste sistemático de documentos autorizados sem correspondente na EFD deve analisar can
- A diferença entre o total de ICMS do grupo W de todas as NF-e de saída e o E110 prova, por
- Na análise de uma indústria obrigada ao Bloco K, a comparação de K230 e K235 pode apoiar t
- Uma equipe que audita EFD de 2025 e de 2026 pode aplicar indistintamente a versão 3.1.7 do
- Se uma operação de exportação está abrangida pela regra de manutenção de créditos da LC 87
- Dois contribuintes em estados diferentes vendem mercadoria do mesmo NCM. O auditor pode to
- No SQL de auditoria, relacionar documentos, itens e ajustes sem agregar previamente cada c
- A presença de item no cadastro 0200 com CFOP de entrada é suficiente para comprovar crédit
- Em caso de ressarcimento de ICMS-ST previsto pela unidade federada, uma exceção à dispensa
- Um documento NF-e autorizado não foi localizado por LEFT JOIN no C100 do arquivo inicial. 
```

## Pedido 30: Locação de imóveis urbanos (`direito-civil.locacao-imoveis-urbanos`, 78 tópicos de edital)

```
Matéria: direito-civil.locacao-imoveis-urbanos (Locação de imóveis urbanos)
Capítulos e quantas questões quero em cada um (total 100):
- "Âmbito da Lei do Inquilinato e modalidades de locação": 20
- "Aluguel, reajuste, encargos e garantias locatícias": 20
- "Deveres, sublocação, benfeitorias e preferência": 20
- "Ação de despejo e disposições procedimentais": 20
- "Ações revisional, consignatória e renovatória": 20

Já existem (não repita):
- A locação isolada de vaga autônoma de garagem integra necessariamente o regime da Lei nº 8
- Na locação residencial escrita por 30 meses, a permanência do inquilino por mais de 30 dia
- Na locação residencial verbal, decorrido o prazo inicialmente ajustado, o locador pode exi
- Para ser classificada como locação para temporada, a contratação não pode superar 90 dias,
- Locação residencial celebrada por prazo de dez anos dispensa a vênia conjugal por se trata
- As partes podem prever em contrato de locação residencial reajuste monetário automático a 
- Em regra, o locador pode exigir simultaneamente fiança e caução em dinheiro como garantias
- Na falta de qualquer das modalidades legais de garantia, a Lei do Inquilinato admite cobra
- A constituição do fundo de reserva do condomínio é despesa extraordinária que, em regra, c
- A mera ausência de resposta do locador à comunicação de sublocação comprova, por si só, se
- Salvo disposição contratual expressa em contrário, a benfeitoria necessária realizada pelo
- A preferência do locatário na venda do imóvel prevalece, mesmo em condomínio, sobre a pref
- A averbação do contrato 30 dias antes da alienação, aliada aos demais requisitos legais, p
- A ação de despejo é, em regra, a via judicial adequada para o locador reaver o imóvel, qua
- A liminar de desocupação por falta de pagamento do art. 59, § 1º, IX, incide indistintamen
- A purga da mora prevista no art. 62 permite, em regra, pagamento atualizado mediante depós
- Havendo impugnação fundamentada do locador quanto à insuficiência da purga da mora, a Lei 
- A ação revisional do art. 19 pode ser ajuizada por locador ou locatário, após três anos de
- Na ação consignatória de aluguéis, a petição inicial não precisa discriminar valores, bast
- Na ação renovatória, é indispensável que o locatário tenha explorado seu comércio no mesmo
- O direito de propor ação renovatória pode ser exercido a qualquer momento até o último dia
- Uma administradora aluga isoladamente uma vaga autônoma de garagem a terceiro. Qual regime
- Marina celebrou contrato residencial escrito de 24 meses. Encerrado o prazo, permaneceu no
- Em contrato de locação para temporada, qual afirmação corresponde à lei?
- Uma empresa aluga apartamento para moradia de seus diretores. A Lei do Inquilinato qualifi
- Contrato residencial prevê aluguel em dólares, convertido todo mês pela cotação. Conforme 
- Em locação residencial comum sem garantia, o locador quer antecipar aluguéis. Qual cobranç
- O proprietário exige caução em dinheiro de quatro aluguéis para garantir contrato urbano. 
- Síndico apresenta duas cobranças: manutenção mensal do elevador e obra estrutural de refor
- O locatário subloca metade do imóvel sem aviso nem autorização. À luz do art. 13, a operaç
- Uma locatária instala, com autorização, melhoria que aumenta a utilidade do apartamento. N
- Um proprietário pretende vender o imóvel alugado. Qual providência inicial permite ao inqu
- Um imóvel locado é vendido durante contrato por prazo certo. Qual combinação pode impedir 
- Proprietário quer retirar inquilino inadimplente trocando as fechaduras sem ajuizar ação. 
- Qual circunstância é indispensável à liminar específica do art. 59, § 1º, IX, por falta de
- Na ação de despejo por falta de pagamento, o locador formula também pedido de cobrança. Qu
- Na ação de despejo fundada em falta de pagamento, locatário pretende purgar mora. Qual con
- Na distribuição de ação locatícia sujeita ao art. 58, qual é o foro que a lei indica como 
- Duas partes discordam do aluguel após quatro anos sem acordo sobre novo valor. Qual ação b
- O locador recusa injustificadamente receber aluguéis na forma devida. Qual via especial vi
- Qual conjunto satisfaz o núcleo dos requisitos temporais da renovatória de locação comerci
- Um comércio cumpre os requisitos da renovatória, mas ajuíza a demanda apenas três meses an
- Em ação revisional proposta pelo locatário, o juiz fixa aluguel provisório, quando requeri
```

## Pedido 31: Publicidade, marketing e redes sociais (`comunicacao.marketing-publicidade`, 78 tópicos de edital)

```
Matéria: comunicacao.marketing-publicidade (Publicidade, marketing e redes sociais)
Capítulos e quantas questões quero em cada um (total 100):
- "Planejamento estratégico de comunicação e fundamentos de marketing": 17
- "Publicidade, propaganda e contratação pública": 17
- "Marca, identidade visual e branding institucional": 17
- "Marketing digital e gestão estratégica de redes sociais": 17
- "Métricas, avaliação de campanhas e otimização": 16
- "Comunicação de crise, ética e proteção de dados": 16

Já existem (não repita):
- Uma secretaria elaborou uma matriz FOFA para sua campanha e quer classificar a falta de pe
- Qual formulação representa melhor um objetivo de comunicação compatível com o critério SMA
- Uma agência recebeu um pedido para criar uma campanha, mas faltam informações sobre a popu
- Uma autarquia publica uma orientação no próprio portal e também compra anúncios em rádio p
- Ao planejar comunicação com idosos que usam um serviço municipal, uma equipe constrói uma 
- No contexto da classificação de ações de publicidade do governo federal, que tipo se desti
- Um prefeito quer veicular campanha pública com seu nome e foto em destaque como responsáve
- Em contratação de serviços de publicidade por agência regida pela Lei nº 12.232/2010, qual
- Qual conjunto contém os quatro quesitos do plano de comunicação publicitária previstos no 
- Em licitação de agência sob a Lei nº 12.232/2010, qual medida protege a avaliação técnica 
- Nos três meses anteriores ao pleito, uma prefeitura mantém no Instagram postagem instituci
- Os empenhos publicitários não cancelados foram R$ 120 mil, R$ 180 mil e R$ 240 mil nos trê
- Uma instituição trocou o desenho do símbolo gráfico e afirmou ter concluído toda a gestão 
- Após extensa campanha, uma pesquisa mostra que o público percebe o órgão como pouco acessí
- Uma campanha municipal quer incentivar descarte correto de resíduos e superar barreiras a 
- Ao comparar uma ação de SEO com anúncios em mecanismos de busca, qual distinção está corre
- Uma equipe rastreia conversas sobre um serviço em diferentes comunidades, inclusive sem me
- Uma equipe pretende testar dois títulos de anúncio antes de ampliar investimento; qual des
- Uma cidadã relata falha real de atendimento em comentário de perfil público, sem ofensa ou
- Uma campanha registrou 1.000 cliques no link e 50.000 impressões; qual CTR, considerando e
- Uma ação custou R$ 900 e gerou 45.000 impressões; qual é seu custo por mil impressões?
- Uma publicação teve alcance de 8.000 contas e 24.000 impressões; qual interpretação é a me
- O relatório atribuiu todas as inscrições ao último canal clicado, ignorando contatos anter
- Um órgão enfrenta denúncia ainda não apurada e forte repercussão; qual deve ser a primeira
- Uma equipe pretende reutilizar listas de contatos coletadas para atendimento presencial em
- Na análise FOFA, ameaças e oportunidades referem-se ao ambiente externo, enquanto forças e
- Para demonstrar eficácia de uma campanha, basta comprovar que o número previsto de postage
- Uma pesquisa qualitativa permite aprofundar percepções e motivações, mas seus achados não 
- Segundo a Constituição, a publicidade oficial pode ter promoção pessoal explícita desde qu
- Na classificação federal, publicidade legal é aquela voltada a publicar informações cuja d
- A Lei nº 12.232/2010 admite que um contrato de serviços publicitários inclua, indistintame
- O plano de comunicação publicitária da Lei nº 12.232/2010 inclui raciocínio básico, estrat
- A manutenção de publicidade institucional em rede social oficial durante o período vedado 
- No primeiro semestre eleitoral, o art. 73, VII, estabelece parâmetro vinculado a seis veze
- Identidade visual corresponde exatamente às percepções espontâneas que o público tem sobre
- Branding pode envolver posicionamento, identidade, experiências de atendimento e consistên
- Marketing social é necessariamente igual a filantropia empresarial e tem como único indica
- Uma equipe pode adaptar linguagem e formato de uma orientação pública às redes sociais sem
- A exclusão de qualquer comentário crítico é prática necessária de moderação em perfis inst
- Se um anúncio teve 30.000 impressões e alcance de 10.000 contas, a frequência média calcul
- CTR é o custo do investimento dividido pelas impressões e multiplicado por mil, sendo sinô
- Um aumento de solicitações após uma campanha não comprova, por si só, que a campanha tenha
- Uma taxa de engajamento de 5% calculada sobre alcance pode ser comparada diretamente a out
- Uma comunicação inicial de crise pode informar os fatos conhecidos, as providências tomada
- O legítimo interesse é uma justificativa automática e suficiente para qualquer uso de dado
```

## Pedido 32: Uso da força e direitos humanos (`seguranca-publica.uso-forca-direitos-humanos`, 78 tópicos de edital)

```
Matéria: seguranca-publica.uso-forca-direitos-humanos (Uso da força e direitos humanos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direitos fundamentais e princípios do uso diferenciado da força": 18
- "2. Lei nº 13.060/2014 e Decreto nº 12.341/2024: instrumentos e gestão": 32
- "3. Princípios Básicos de Havana (1990): os 26 itens e suas situações": 23
- "4. Código de Conduta da ONU e direitos de pessoas abordadas e presas": 15
- "5. Responsabilização, controle e jurisprudência nacional e interamericana": 12

Já existem (não repita):
- Durante avaliação de uma abordagem, foi solicitado o rol completo dos princípios gerais ex
- Em uma ocorrência, há instrumento de menor potencial ofensivo disponível, mas seu emprego 
- Um inventário classifica equipamento de contenção como instrumento de menor potencial ofen
- Uma pessoa desarmada foge sem representar risco imediato de morte ou lesão, e um profissio
- Após intervenção estatal, uma pessoa sofreu ferimento e necessita atendimento. Que providê
- Na elaboração do calendário de formação, uma instituição propõe curso sobre uso da força s
- Uma unidade adquiriu equipamentos e pergunta quantos instrumentos de menor potencial ofens
- Em situação de resistência sem ameaça iminente grave, há alternativa não violenta capaz de
- Durante custódia, um profissional recebe ordem para empregar sofrimento físico como castig
- Uma análise de ocorrência identifica possibilidade de emprego intencionalmente letal de ar
- Uma intervenção reúne as condições materiais do item 9 de Havana, mas emitir advertência c
- Uma reunião é ilícita conforme legislação aplicável, mas os participantes não agem com vio
- Durante custódia em estabelecimento prisional, um instrumento de menor potencial ofensivo 
- Uma prova compara a lista de princípios da Lei nº 13.060/2014 com a lista geral do Decreto
- Um profissional tem acesso a informação íntima durante atuação oficial. A divulgação não é
- Pessoa sob custódia relata condição de saúde que exige exame imediato, mas o profissional 
- Uma pessoa encarregada da aplicação da lei descobre sinais de violação do Código de Condut
- Uma pessoa custodiada tenta fugir, mas não apresenta o perigo grave contra a vida ou integ
- Um superior soube, ou pelas informações disponíveis deveria saber, que subordinados usavam
- Um profissional recebe ordem manifestamente ilícita de empregar força causadora de morte o
- Um plano institucional prevê mecanismo de avaliação nacional do uso da força e quer saber 
- Na revisão de um relatório institucional, alguém afirma que a ADPF nº 635 foi concluída em
- Durante uma operação, pessoa é ferida por disparo e a perícia não consegue identificar con
- Em caso relativo a mortes e violência em operações de segurança, discute-se o dever estata
- Em estabelecimento prisional, ocorreu uso de instrumento de menor potencial ofensivo sem l
- O Decreto nº 12.341/2024 enumera legalidade, precaução, necessidade, proporcionalidade, ra
- A prioridade de emprego de instrumentos de menor potencial ofensivo, prevista na Lei nº 13
- A expressão 'menor potencial ofensivo' não significa incapacidade de causar morte ou lesão
- A capacitação para uso da força exigida pelo art. 4º do Decreto nº 12.341/2024 é bienal e 
- Conforme o item 4 dos Princípios de Havana, devem-se buscar meios não violentos antes da f
- O artigo 5 do Código de Conduta de 1979 permite invocar emergência pública ou ordem superi
- Quando a força provoca ferimento, prestar assistência médica imediata e comunicar a famíli
- O item 14 de Havana autoriza arma de fogo na dispersão de toda reunião violenta independen
- O artigo 4 do Código de Conduta de 1979 protege informações confidenciais obtidas na ativi
- De acordo com o Decreto nº 12.341/2024, art. 6º, III, basta que cada equipe operacional di
- Na situação de custódia em que se emprega arma de fogo ou instrumento de menor potencial o
- O artigo 8 do Código de Conduta da ONU trata da corrupção, enquanto o artigo 7 se limita à
- A Constituição assegura à pessoa presa o direito de permanecer calada e a assistência da f
- Segundo o item 10 de Havana, quando a advertência anterior ao uso da arma de fogo é dispen
- O item 24 dos Princípios de Havana prevê responsabilização de superiores que sabiam ou dev
- Na tese do STF firmada no Tema 1.237, laudo pericial inconclusivo sobre a origem de dispar
- O julgamento concluído em 3 de abril de 2025 na ADPF nº 635 homologou apenas parcialmente 
- O art. 2º, parágrafo único, VI, do Decreto nº 12.341/2024 permite responsabilizar definiti
- No regime de Havana, o item 25 prevê proteção de profissionais que recusam ordem para uso 
- A proteção do direito à vida prevista na Convenção Americana obriga o Estado a evitar arbi
```

## Pedido 33: Ensino de Matemática (didática) (`educacao.ensino-matematica`, 78 tópicos de edital)

```
Matéria: educacao.ensino-matematica (Ensino de Matemática (didática))
Capítulos e quantas questões quero em cada um (total 100):
- "BNCC, currículo e letramento matemático": 17
- "Fundamentos da aprendizagem, problemas e investigação": 17
- "Educação matemática crítica, etnomatemática e modelagem": 17
- "Jogos, materiais manipuláveis e tecnologias digitais": 17
- "Avaliação da aprendizagem em Matemática": 16
- "Planejamento de sequências, EPT e análise de casos": 16

Já existem (não repita):
- A BNCC estabelece aprendizagens essenciais e orienta os currículos, mas não obriga todas a
- Na BNCC de Matemática do Ensino Fundamental, as unidades temáticas são Números, Álgebra, G
- A Álgebra, segundo a organização da BNCC, deve ser introduzida apenas quando o estudante i
- Na perspectiva didática, uma tarefa que exige estratégia não imediatamente conhecida pelo 
- Ao empregar materiais manipuláveis, a compreensão conceitual do estudante fica automaticam
- Na avaliação diagnóstica de Matemática, o professor procura identificar saberes prévios, e
- É possível usar calculadora em Matemática sem renunciar ao desenvolvimento da interpretaçã
- Na avaliação formativa, basta aplicar testes semanais e registrar as notas, sendo desneces
- As etapas de compreender, planejar, executar e revisar propostas por Pólya devem ocorrer e
- Em uma investigação matemática, a observação de diversos exemplos favoráveis pode sustenta
- A etnomatemática propõe reconhecer práticas matemáticas culturalmente situadas e dialogar 
- Qualquer questão verbal sobre compras caracteriza modelagem matemática, ainda que forneça 
- Em um jogo didático, os registros e a discussão das estratégias dos participantes constitu
- Uma matriz de referência do Saeb equivale ao currículo integral da escola, motivo pelo qua
- Em aula inclusiva de Matemática, o professor pode oferecer uma representação tátil ou oral
- Ao identificar um erro recorrente em justificativas, a ação formativa adequada pode inclui
- Em Matemática, o professor pode institucionalizar conceitos depois da investigação sem con
- Se um software de geometria dinâmica preservar visualmente uma relação em cem arrastos, es
- A Educação Matemática Crítica pode examinar quem escolheu os dados de um gráfico e quais c
- A modelagem na EPT deve restringir o aluno à execução de cálculos exigidos pelo setor prod
- Em sequência didática alinhada à BNCC, definir a evidência observável de aprendizagem ante
- Quando uma rubrica emprega o critério “o estudante é inteligente”, ela produz um descritor
- Na resolução de problemas, o mesmo enunciado pode mudar de exercício para problema conform
- Uma representação estatística tecnicamente correta pode ser criticada quanto a categorias 
- Uma professora planeja uma aula com base na BNCC. Qual decisão está corretamente alinhada 
- Qual alternativa apresenta exclusivamente unidades temáticas da Matemática no Ensino Funda
- Qual característica distingue, didaticamente, um problema de um exercício rotineiro?
- Em uma prática inicial de investigação, o que o professor deve favorecer após a observação
- Qual é uma finalidade apropriada do material dourado em uma aula de Matemática?
- Uma avaliação é considerada diagnóstica quando se destina principalmente a:
- Qual prática torna um jogo matemático mais efetivo como recurso de ensino?
- Ao utilizar a matriz de referência do Saeb, o professor deve compreender que ela:
- Uma turma registra quatro maneiras de resolver um desafio. Que intervenção melhor correspo
- Em atividade de Educação Matemática Crítica sobre tarifas de transporte, o foco mais carac
- Uma escola quer desenvolver atividade etnomatemática com artesãos da região. Qual estratég
- Em uma atividade de modelagem de consumo de água, que etapa não pode ser omitida sem preju
- Ao trabalhar geometria com um aplicativo de arrastar figuras, a melhor conclusão didática 
- Um professor recebe produções escritas e percebe que parte da turma confunde as condições 
- Em uma atividade de Matemática inclusiva, uma aluna necessita de representação tátil. O pl
- Qual objetivo revela integração curricular adequada entre Matemática e um curso técnico da
- Depois de observar padrões em uma planilha, estudantes afirmam que uma relação vale sempre
- Uma rubrica exige “clareza matemática” sem explicar o que será observado. Qual melhoria é 
- Em um projeto, o gráfico está corretamente construído, mas a pesquisa excluiu alunos de ou
- Um docente chama de modelagem um exercício em que todos os dados, equações e resultados es
- No planejamento reverso de uma sequência de Matemática, qual encadeamento é mais coerente?
- Em situação de aprendizagem significativa, segundo Ausubel, o professor deve valorizar pri
- A coordenação pedagógica propõe usar resultados do Saeb como currículo de Matemática compl
- Em um projeto de EPT, alunos apresentam excelente cálculo, mas ignoram hipóteses que torna
```

## Pedido 34: Farmacotécnica e farmácia magistral (`farmacia.farmacotecnica-magistral`, 78 tópicos de edital)

```
Matéria: farmacia.farmacotecnica-magistral (Farmacotécnica e farmácia magistral)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, vias de administração e delineamento de formas farmacêuticas": 20
- "Formas sólidas e operações farmacêuticas": 20
- "Formas líquidas, semissólidas, veículos e excipientes": 20
- "Cálculos farmacêuticos, estabilidade, embalagem e acondicionamento": 20
- "Boas práticas magistrais, controle da qualidade e preparações estéreis": 20

Já existem (não repita):
- Uma farmácia recebe prescrição individualizada para preparar cápsulas destinadas a uma pac
- A respeito das vias de administração, qual associação apresenta um objetivo farmacotécnico
- No desenvolvimento de um fármaco com solubilidade aquosa limitada, qual abordagem é tecnic
- Um paciente deseja triturar comprimido revestido de liberação modificada antes da ingestão
- Na pré-formulação de base fraca sensível à hidrólise, qual afirmação é a mais tecnicamente
- Na classificação farmacotécnica, a mesma preparação pode ser líquida e não estéril ou líqu
- Qualquer forma farmacêutica administrada pela via nasal apresenta obrigatoriamente as mesm
- Diminuir o tamanho de partícula tende a elevar a área superficial disponível, mas não gara
- Uma suspensão pode ser renomeada como solução desde que, após agitação vigorosa, o sistema
- Na manipulação de um pó com IFA de baixa dosagem, a finalidade primária da diluição geomét
- Qual situação representa risco real ao utilizar lubrificante hidrofóbico em excesso em com
- Ao se comparar peneiramento e pulverização, qual descrição está correta?
- Um lote de cápsulas apresenta massas próximas, mas teores individuais muito variáveis; qua
- Em uma preparação semissólida com sólido insolúvel, qual procedimento ajuda a reduzir grum
- Um pó muito fino sempre flui melhor que um granulado, porque a área superficial elevada im
- A granulação pode melhorar escoamento e reduzir segregação, mas sua aplicação deve conside
- Em comprimidos, desintegração e dissolução representam fenômenos idênticos e podem ser usa
- Ao fracionar um comprimido de liberação modificada, observar uma superfície visualmente ho
- Um líquido contém partículas sólidas dispersas e deve ser agitado para redispersá-las ante
- Em uma emulsão óleo em água (O/A), qual fase constitui o meio contínuo?
- Qual afirmação separa corretamente cremeação de coalescência em emulsões?
- Uma suspensão oral desenvolve sedimento muito compacto e de difícil redispersão após armaz
- O farmacêutico precisa escolher excipiente para reduzir catalisação oxidativa por metais t
- Conservantes antimicrobianos presentes em um xarope podem auxiliar a proteção microbiológi
- Toda formulação apresentada como gel farmacêutico possui exclusivamente água como fase líq
- A presença de fase oleosa dispersa em fase aquosa pode ser compatível com creme O/A, cuja 
- Se um fármaco é insolúvel em água, qualquer conservante antimicrobiano o transforma em sol
- Para obter 100 mL de preparação a 2% m/v, a massa teórica de soluto é:
- Uma prescrição solicita 50 cápsulas, cada uma com 40 mg de IFA; qual massa teórica deve se
- Uma farmácia dispõe de solução a 10 mg/mL e precisa preparar 100 mL a 2 mg/mL com diluente
- Uma matéria-prima contém 80% m/m do ativo desejado, sem outras correções de equivalência; 
- Uma fórmula sensível à luz será acondicionada em frasco âmbar; qual análise adicional é in
- Uma concentração de 1% m/v equivale, aritmeticamente, a 10 mg/mL.
- A embalagem primária é, por definição, a caixa externa que não possui contato físico com o
- No cálculo de uma diluição simples, C1V1=C2V2 pressupõe conservação da quantidade de solut
- O prazo de validade de um insumo, independentemente do veículo e da embalagem, pode ser co
- Na RDC Anvisa 67/2007, qual comportamento melhor representa o fluxo seguro de manipulação 
- Uma matéria-prima recém-recebida aguarda liberação pelo controle de qualidade; qual medida
- Qual afirmação distingue corretamente filtração comum de filtração esterilizante?
- O teste de esterilidade de uma preparação parenteral apresenta resultado satisfatório, mas
- Em uma sala de manipulação estéril, qual conjunto de medidas melhor reduz o risco durante 
- Limpeza e desinfecção, isoladamente, são sempre equivalentes a esterilização validada de p
- Rastreabilidade permite vincular cada preparação aos insumos, seus lotes, etapas realizada
- Filtração esterilizante por membrana qualificada de 0,22 µm remove obrigatoriamente todas 
- Mesmo após esterilização tecnicamente adequada, uma preparação pode ser reprovada se o ati
```

## Pedido 35: Leis de Newton, forças e atrito (`fisica.leis-newton-atrito`, 77 tópicos de edital)

```
Matéria: fisica.leis-newton-atrito (Leis de Newton, forças e atrito)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios de Newton, referenciais e composição de forças": 22
- "2. Diagramas de corpo livre, peso, normal, tração e molas": 22
- "3. Atrito estático, atrito cinético e planos inclinados": 19
- "4. Movimento curvilíneo, força centrípeta e curvas inclinadas": 19
- "5. Estratégias de resolução, sistemas e casos integrados": 18

Já existem (não repita):
- Um carrinho move-se em linha reta com velocidade constante e despreza-se qualquer resistên
- Uma caixa de 3 kg recebe resultante horizontal de 12 N para a direita. Qual é sua aceleraç
- Uma pessoa pressiona uma parede com força horizontal. Qual afirmação identifica corretamen
- Duas pessoas puxam a mesma caixa horizontalmente, uma com 25 N para a direita e outra com 
- Uma mochila de massa 4 kg está próxima da superfície terrestre. Adote g=10 m/s². Qual é a 
- Uma caixa de 6 kg repousa sobre mesa horizontal sem outras forças verticais além do peso e
- Uma caixa em repouso recebe força horizontal de 15 N e seu atrito estático máximo é 20 N. 
- Um bloco já desliza em piso horizontal sob normal de 50 N. Seu coeficiente de atrito cinét
- Dois cabos aplicam num mesmo ponto forças perpendiculares entre si de 6 N e 8 N. Qual é o 
- Uma mola ideal de constante 300 N/m é alongada em 0,02 m. Qual é o módulo da força restaur
- Um bloco de massa 2 kg está sobre rampa de 30° em relação à horizontal. Use g=10 m/s² e se
- Uma caixa de 2 kg repousa numa rampa de 30° sem outras forças perpendiculares. Use g=10 m/
- Uma caixa de 5 kg desliza sobre o piso; é puxada por 50 N para a direita e sofre atrito ci
- Durante uma curva à esquerda, o passageiro sente-se lançado para a direita. Qual interpret
- Um corpo de 3 kg percorre trajetória circular de raio 2 m com rapidez constante de 4 m/s. 
- Uma pessoa de massa 5 kg está em elevador acelerando verticalmente para cima a 2 m/s². Use
- Dois blocos de 4 kg e 6 kg estão em contato sobre mesa sem atrito. Uma força horizontal de
- Um bloco de 2 kg repousa numa rampa de 30°, com g=10 m/s², sen 30°=0,5 e cos 30°=0,866. O 
- Um automóvel faz curva plana de raio 40 m. Com coeficiente estático lateral 0,4 e g=10 m/s
- Uma curva ideal sem atrito tem raio de 25 m, velocidade de projeto de 10 m/s e g=10 m/s². 
- Um bloco escorrega para baixo em rampa de 30° com coeficiente cinético 0,2. Use g=10 m/s²,
- Uma luminária de peso 100 N é sustentada por dois cabos simétricos, cada um formando 30° c
- Uma mola horizontal de constante 100 N/m, deformada 0,08 m, puxa um bloco inicialmente em 
- Dois corpos de 2 kg e 8 kg recebem, separadamente, resultantes externas de igual módulo 24
- Um corpo de 2 kg descreve curva horizontal de raio 3 m a 6 m/s. O atrito estático disponív
- Uma caixa de 2 kg, sob resultante externa de 10 N para a direita, adquire aceleração de 5 
- Um ciclista percorre uma curva mantendo o mesmo módulo de velocidade; por isso, sua result
- Ao empurrar uma parede, uma pessoa recebe dela força de igual módulo e direção, mas de sen
- Um livro em repouso sobre uma mesa horizontal está livre de forças, uma vez que sua aceler
- Uma bola de massa 3 kg sofre força gravitacional local de 30 N quando se adota g=10 m/s².
- Uma caixa apoiada em rampa inclinada, sem forças adicionais e sem aceleração normal, receb
- Uma mola ideal de rigidez 150 N/m esticada 0,04 m exerce força elástica restauradora de mó
- Uma caixa em repouso sob puxão de 8 N e limite de atrito estático de 20 N sofre necessaria
- Para um bloco de 4 kg numa rampa de 30°, com g=10 m/s² e sen 30°=0,5, a componente do peso
- Ao caminhar para a frente sem escorregar, uma pessoa recebe do solo força de atrito estáti
- Para um mesmo corpo e raio de curva, aumentar a rapidez de 5 m/s para 10 m/s quadruplica a
- No referencial inercial da estrada, a força centrífuga é uma interação real adicional que 
- Em uma curva sem atrito lateral, a superelevação ideal faz com que a componente horizontal
- Numa curva ideal sem atrito de raio 20 m, com g=10 m/s² e tangente da inclinação igual a 0
- Dois blocos de 2 kg e 3 kg aceleram juntos em mesa sem atrito sob força externa de 15 N ap
- Uma pessoa de massa 5 kg está em elevador acelerando para cima a 2 m/s², com g=10 m/s². A 
- Um bloco em rampa de 30° pode ficar em repouso, sem outras forças tangenciais, se o coefic
- A superlargura em uma curva aumenta diretamente a componente horizontal da normal, desempe
- Um veículo de 1000 kg percorre curva plana de raio 50 m a 10 m/s, com g=10 m/s² e coeficie
- Para interpretar forças num carro em curva, é indiferente usar o referencial inercial da e
```

## Pedido 36: Psiquiatria da infância e adolescência e interconsulta (`psiquiatria.infancia-adolescencia-interconsulta`, 77 tópicos de edital)

```
Matéria: psiquiatria.infancia-adolescencia-interconsulta (Psiquiatria da infância e adolescência e interconsulta)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação clínica, desenvolvimento, classificação e rede de cuidados": 17
- "Transtorno do espectro autista e deficiência intelectual": 17
- "TDAH, transtornos de aprendizagem e diagnóstico diferencial": 17
- "Ansiedade, depressão, autolesão e uso de substâncias na adolescência": 17
- "Psicofarmacologia pediátrica, ética, reabilitação e prevenção": 16
- "Interconsulta psiquiátrica, delirium e psiquiatria de ligação": 16

Já existem (não repita):
- Uma criança de seis anos apresenta dificuldade persistente de contato social e interesses 
- Ao avaliar adolescente com suspeita de TDAH, o profissional encontra sintomas apenas duran
- A família de uma menina com TEA pergunta se toda criança autista tem comprometimento intel
- Um aluno tem dificuldade persistente de leitura, mas funcionamento intelectual global e au
- Uma adolescente apresenta isolamento, irritabilidade persistente, anedonia e queda de rend
- Em entrevista com adolescente que relata pensamentos de morte, a melhor abordagem inicial 
- Qual manifestação é especialmente associada ao delirium hipoativo no paciente hospitalizad
- Em criança com deficiência intelectual, qual informação tem papel decisivo além da avaliaç
- Na interconsulta, a equipe relata agitação súbita após infecção, oscilação do alerta e dif
- Qual é uma distinção adequada entre psiquiatria de ligação e interconsulta pontual?
- Ao orientar família sobre tratamento de TEA, qual proposta respeita melhor as evidências e
- Uma criança de quatro anos com desatenção e inquietude relevantes recebe avaliação especia
- Antes de iniciar um estimulante indicado para TDAH, qual conjunto de aspectos é especialme
- Após autolesão de adolescente atendida no pronto-socorro, qual conjunto de ações é mais ad
- Sobre instrumentos de avaliação de risco após comportamento autolesivo, assinale a alterna
- Um adolescente hospitalizado por doença crônica solicita conversar sem responsável present
- Um estudante apresenta dificuldade de cálculo após longa ausência de escolarização, sem hi
- Em criança com TEA que apresenta agitação nova e intensa após internação, qual abordagem i
- A definição mais adequada de deficiência intelectual, para distinguir de baixo rendimento 
- Ao considerar antidepressivo para depressão moderada a grave em adolescente, qual medida é
- Qual conduta mais se aproxima da prevenção de delirium em paciente internado com fatores d
- Uma criança com atraso de linguagem tem exame auditivo ainda não realizado. Qual posição é
- No hospital geral, um parecer psiquiátrico de qualidade deve prioritariamente:
- Sobre uso de substâncias na adolescência, qual afirmação orienta uma avaliação adequada?
- Em exame psiquiátrico infantil, qual erro pode levar a avaliação inconsistente?
- Na psiquiatria infantojuvenil, uma escala de rastreamento substitui integralmente a histór
- A avaliação do TEA considera tanto dificuldades persistentes de comunicação e interação so
- Toda pessoa com TEA apresenta necessariamente deficiência intelectual.
- Sintomas de TDAH precisam ser contextualizados em diferentes ambientes e associados a prej
- A dificuldade persistente em leitura é suficiente, isoladamente, para diagnosticar deficiê
- A depressão na infância pode apresentar irritabilidade persistente e perda de interesse em
- Perguntar de modo direto e acolhedor sobre pensamentos suicidas deve ser evitado, pois ind
- Na interconsulta, a presença de desatenção aguda e flutuante associada à alteração do níve
- O diagnóstico de deficiência intelectual deve considerar funcionamento adaptativo e não ap
- Uma criança com suspeita de TEA jamais poderá utilizar comunicação aumentativa e alternati
- A coexistência de TDAH e TEA é possível e exige avaliação do prejuízo ligado a cada condiç
- A intervenção em dificuldades de aprendizagem deve considerar a qualidade das oportunidade
- A ausência de tristeza verbalizada afasta o diagnóstico de depressão em crianças e adolesc
- Após episódio de autolesão, a avaliação psicossocial pode ser iniciada paralelamente ao tr
- O escore de uma escala de risco pode ser usado, isoladamente, para decidir que um adolesce
- No acompanhamento de crianças em uso de estimulantes, sono, apetite, crescimento e parâmet
- Antipsicóticos, quando prescritos para sintomas associados ao TEA, curam os déficits centr
- Delirium hipoativo pode passar despercebido quando predominam retraimento e lentificação, 
- Uma recomendação internacional de medicamento para TDAH implica, automaticamente, que o fá
- A interconsulta psiquiátrica deve considerar condições clínicas e farmacológicas antes de 
```

## Pedido 37: JavaScript: fundamentos da linguagem (`informatica.javascript`, 77 tópicos de edital)

```
Matéria: informatica.javascript (JavaScript: fundamentos da linguagem)
Capítulos e quantas questões quero em cada um (total 100):
- "Tipos, variáveis e operadores": 20
- "Funções, escopo e closures": 20
- "Objetos, arrays e JSON": 20
- "DOM, eventos, assincronia e módulos": 20
- "Como o tema costuma ser cobrado": 20

Já existem (não repita):
- Em JavaScript, qual é o resultado de console.log(typeof 'texto')?
- Qual é a saída de console.log('5' + 3)?
- Qual palavra-chave declara uma variável de escopo de bloco que não pode ser reatribuída?
- Qual é a saída de console.log([1, 2, 3].map(n => n * 2))?
- Qual método associa uma função ao clique de um botão guardado na variável botao?
- Qual é o resultado da comparação 0 === '0'?
- Qual método converte um objeto JavaScript em texto no formato JSON?
- Dada const dobro = n => n * 2;, qual é a saída de console.log(dobro(7))?
- Qual é a saída de console.log(typeof null, typeof [])?
- Qual é a saída do código console.log(x); var x = 5;?
- Qual é a saída de console.log([1, 2, 3, 4].reduce((acc, n) => acc + n, 0))?
- Considere function criar() { let c = 0; return () => ++c; } const f = criar(); f(); f(); Q
- Em um formulário, qual chamada, feita dentro do ouvinte do evento submit, impede o envio e
- Qual é a saída de console.log(0 || 'a', 0 ?? 'b')?
- Qual é a saída de console.log(JSON.stringify({ a: 1, b: undefined, c: 'x' }))?
- Qual é o valor devolvido por uma função declarada como async function f() { return 5; } qu
- Qual é a saída, na ordem, de console.log(1); setTimeout(() => console.log(2), 0); Promise.
- Qual é a saída de for (var i = 0; i < 3; i++) { setTimeout(() => console.log(i), 0); }?
- Qual é a saída de console.log(1 + 2 + '3', '1' + 2 + 3)?
- Qual é a saída de const a = [10, 1, 2]; a.sort(); console.log(a);?
- Sobre Promise.all([p1, p2, p3]), assinale a alternativa correta.
- Qual é a saída de const x = { v: 1 }; const y = x; y.v = 2; console.log(x.v, x === y, { v:
- O arquivo m.js contém export default function dobro(n) { return n * 2; } e export const PI
- Qual é a saída de const f = (a, b = 10, ...r) => a + b + r.length; console.log(f(1), f(1, 
- Qual é a saída de console.log([3, 0, 5].map(x => x || 1).filter(x => x > 1).length)?
- Em JavaScript, uma variável declarada com let pode receber um novo valor mais adiante no m
- JavaScript e Java são a mesma linguagem; o primeiro nome é apenas a versão do Java usada n
- O método filter devolve um novo array apenas com os itens para os quais a função passada r
- Em JavaScript, a string '0' é um valor falsy, assim como o número 0.
- document.querySelector('.item') devolve o primeiro elemento da página que possui a classe 
- Em JSON, as chaves dos objetos podem ser escritas sem aspas ou com aspas simples, como em 
- Uma função JavaScript que termina sem executar nenhum return devolve undefined.
- Declarar const lista = [1, 2]; e depois executar lista.push(3); gera erro, pois const torn
- A expressão null == undefined resulta em true, enquanto null === undefined resulta em fals
- O método forEach devolve um novo array com os valores retornados pela função a cada item, 
- Atribuir a elemento.innerHTML um texto digitado pelo usuário pode permitir a injeção de có
- Uma função escrita como declaração (function nome() {}) só pode ser chamada em linhas post
- Uma promise pode estar pendente, cumprida ou rejeitada e, depois de cumprida ou rejeitada,
- A comparação NaN === NaN resulta em true, por isso é a forma indicada de testar se um cálc
- Closure é a capacidade de uma função continuar acessando as variáveis do escopo em que foi
- Arrow functions têm this próprio, definido pelo objeto que as chama, e por isso são a esco
- Mesmo com atraso 0, o callback de setTimeout só executa depois de terminar o código síncro
- Um módulo ES pode ter várias exportações default, desde que cada uma tenha um nome diferen
- Por padrão, um clique em um botão dentro de uma div dispara primeiro o ouvinte do botão e 
- Em const b = { ...a };, o spread faz uma cópia profunda de a, de modo que alterar um objet
```

## Pedido 38: Trabalho, energia e potência (`fisica.trabalho-energia-potencia`, 77 tópicos de edital)

```
Matéria: fisica.trabalho-energia-potencia (Trabalho, energia e potência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Trabalho de uma força e seus sinais": 25
- "2. Energia cinética e teorema trabalho–energia": 16
- "3. Energia potencial gravitacional e conservação mecânica": 27
- "4. Mola ideal: força elástica, trabalho e energia potencial": 14
- "5. Potência, rendimento e aplicações integradas": 18

Já existem (não repita):
- Uma pessoa empurra um carrinho em linha reta com força horizontal constante de 30 N, no se
- Uma força de intensidade 20 N forma ângulo de 60° com o deslocamento horizontal de 5 m de 
- Um bloco avança 3 m sobre superfície horizontal e sofre força de atrito cinético constante
- Um corpo se desloca horizontalmente enquanto uma força vertical constante atua sobre ele. 
- Uma bola, tratada como partícula sem rotação, tem massa de 2 kg e velocidade de 3 m/s. Qua
- Uma caixa de 4 kg desce verticalmente 2 m, sob gravidade de 10 m/s². Qual trabalho a força
- Uma mola ideal de constante elástica 200 N/m é comprimida em 10 cm a partir de seu comprim
- Um elevador realiza 600 J de trabalho útil em 3 segundos, mantendo a razão média entre tra
- Uma máquina recebe 500 J de energia e transforma 400 J em trabalho mecânico útil. Qual é s
- Um bloco de massa 4 kg tem velocidade inicial de 2 m/s e final de 4 m/s. Qual é o trabalho
- Sobre um bloco em deslizamento de 5 m, atua força de atrito cinético constante de 40 N, em
- Uma mola ideal de constante elástica 100 N/m, inicialmente comprimida em 20 cm, retorna at
- Um corpo de 2 kg é solto do repouso a 5 m de altura e passa por 2 m de altura, sem resistê
- Uma força constante de 50 N puxa um objeto por deslocamento de 8 m, fazendo ângulo de 30° 
- Um dispositivo eleva uma carga de 200 kg por 3 m em 10 segundos, sem alteração relevante d
- Um corpo de 10 kg parte do repouso a 3 m de altura. Durante a descida até o solo, o atrito
- Uma mola horizontal ideal de 400 N/m está comprimida 20 cm e lança um bloco de 1 kg inicia
- Uma mola de constante 500 N/m é alongada da deformação de 10 cm para 20 cm, dentro do regi
- Um bloco de 3 kg passa a deslizar a 6 m/s por 4 m numa superfície horizontal. O atrito con
- Um motor recebe potência constante de 1.200 W, da qual 75% é convertida em trabalho útil. 
- Uma bola de 2 kg passa da altura de 5 m para 2 m, sob campo gravitacional uniforme de 10 m
- Um bloco de 1 kg é solto do repouso 1 m acima de uma pista horizontal sem atrito. Ao fim d
- Uma mola ideal de constante 100 N/m é esticada lentamente por uma força externa, de 10 cm 
- Uma força constante age paralelamente à velocidade de um móvel. Ao comparar dois instantes
- Uma máquina recebe potência constante de 1.500 W, com rendimento de 80%, e eleva 100 kg po
- Uma força constante de 25 N realiza trabalho de 100 J ao deslocar um corpo 4 m em sua mesm
- O trabalho mecânico de uma força constante é sempre positivo quando o módulo da força e a 
- Mantida a massa de um corpo, dobrar sua velocidade de translação faz sua energia cinética 
- Para uma mola ideal comprimida em deformação de módulo 0,10 m, a energia potencial elástic
- No campo gravitacional uniforme, o trabalho da força peso durante a descida depende das al
- Uma máquina que realiza trabalho de 300 J em 3 segundos tem potência média de 300 W, porqu
- Quando as forças sobre um bloco realizam trabalhos de +90 J e −30 J, e as demais têm traba
- Quando uma mola ideal comprimida retorna ao comprimento natural e perde sua energia potenc
- Um corpo desce por trajetória sem atrito nem trabalho externo relevante. À medida que perd
- Um bloco percorre 5 m sob atrito cinético constante de 10 N contrário ao deslocamento. O t
- Se um equipamento recebe potência de 1.000 W e converte 80% dela em potência mecânica útil
- Se dois dispositivos realizam a mesma quantidade de trabalho, mas o segundo conclui a tare
- No modelo de gravidade constante, um corpo de 2 kg que sobe 3 m realiza, pela ação do peso
- Ao alongar lentamente uma mola ideal desde o comprimento natural, a força externa realiza 
- Um corpo inicia trajetória com 150 J de energia mecânica e sofre trabalho dissipativo resu
- Se o trabalho resultante sobre um corpo entre dois instantes é zero, conclui-se que nenhum
- Mantida uma força constante paralela à velocidade de um corpo, o dobro de velocidade corre
- Em sistema sem atrito, um bloco que parte de certa altura e comprime uma mola até parar co
- Se uma força de atrito realiza −35 J e representa o único trabalho não conservativo releva
- Uma máquina que recebe 1.000 J de energia e entrega 1.200 J de trabalho útil pode apresent
```

## Pedido 39: Ensino de História e Geografia (didática) (`educacao.ensino-historia-geografia`, 77 tópicos de edital)

```
Matéria: educacao.ensino-historia-geografia (Ensino de História e Geografia (didática))
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos didáticos e BNCC de História e Geografia": 17
- "Ensino de História: saber escolar, fontes e diferentes linguagens": 17
- "História da África, povos indígenas, diversidade e inclusão": 17
- "Ensino de Geografia e alfabetização cartográfica": 17
- "Estudo do meio, interdisciplinaridade e tecnologias digitais": 16
- "Planejamento, avaliação e cobrança em concursos": 16

Já existem (não repita):
- Uma professora deseja organizar aulas de Ciências Humanas de acordo com a BNCC. Qual afirm
- Uma escola planeja investigar o abastecimento de alimentos da feira do bairro, incluindo o
- Qual ação demonstra melhor a passagem de um código de habilidade da BNCC para objetivo obs
- Ao transformar conhecimentos acadêmicos em saber histórico escolar, a docente pretende man
- Uma fotografia de 1960 é apresentada à turma para compreender alterações de uma avenida. Q
- Um livro didático lançado em 1995 é usado em dois estudos: Independência do Brasil e modos
- Uma turma reuniu relatos orais divergentes sobre o fechamento de um mercado. A resposta ma
- Um professor utiliza um filme recente ambientado em século passado. Para trabalhar com rig
- Após a alteração do art. 26-A da LDB pela Lei nº 11.645/2008, o estudo de História e Cultu
- Uma professora pretende revisar imagens sobre povos indígenas no livro didático. Que proce
- Ao ensinar História da África no Ensino Fundamental, qual critério didático é mais consist
- Uma turma inclui estudante com deficiência visual em uma atividade de leitura de mapa hist
- Em uma atividade de alfabetização cartográfica, a turma aprende a explicar o significado d
- Um docente compara a escala 1:20.000 com a escala 1:200.000. Mantidas condições cartográfi
- Nos anos iniciais, uma professora propõe observar a sala, fazer maquete, desenhar sua vist
- Uma estudante ampliou numa copiadora um mapa cujo original apresentava escala numérica 1:5
- Uma excursão escolar ao centro histórico terá caráter efetivo de estudo do meio quando:
- História e Geografia planejam juntas investigar a transformação de uma praça. Qual prática
- Alunos encontram uma imagem digital descrita como “fotografia original de 1900”, sem autor
- Em um estudo do meio sobre rotas escolares, alunos pretendem divulgar mapa colaborativo co
- Uma prova inicial verifica como os estudantes interpretam legenda e orientação antes da se
- Uma turma produz croquis para localizar equipamentos públicos. O professor pretende adotar
- Após uma avaliação de análise de fontes históricas, muitos estudantes confundiram descriçã
- Em pesquisa coletiva com entrevista e mapa do bairro, o professor quer avaliar aprendizage
- No Ensino Fundamental, a BNCC situa História e Geografia na área de Ciências Humanas.
- Na BNCC, a unidade temática de Geografia denominada “Formas de representação e pensamento 
- Para desenvolver o saber histórico escolar, é suficiente reproduzir a periodização acadêmi
- Os códigos de habilidades da BNCC, por representarem objetivos diretamente observáveis, to
- Cartas, depoimentos, objetos e fotografias podem ser usados como fontes históricas escolar
- Uma linha do tempo pode apoiar o ensino de História, mas a sucessão correta de datas não d
- Como os testemunhos orais são atravessados pela memória, eles não podem constituir evidênc
- Um jornal de 1975 pode ser fonte primária para estudar a imprensa de 1975 e, simultaneamen
- O estudo de História e Cultura Afro-Brasileira e Indígena é obrigatório em estabelecimento
- Realizar atividades relacionadas à cultura afro-brasileira somente em uma comemoração anua
- A obrigatoriedade de incluir histórias e culturas indígenas no currículo geral dispensa re
- Ao disponibilizar fonte histórica em formato acessível para estudante com deficiência, o p
- Legenda, título, fonte e escala podem ser elementos relevantes na leitura crítica de mapas
- Em escala cartográfica de 1:50.000, uma distância de 2 cm no mapa corresponde a 1 km no te
- Na comparação entre mapas em escalas 1:5.000 e 1:500.000, o segundo possui maior escala ca
- A elaboração de croquis pode apoiar o desenvolvimento do pensamento espacial mesmo que o d
- O estudo do meio inclui preparação e levantamento de hipóteses, observação orientada e aná
- Usar um mapa digital em aula elimina a necessidade de verificar a data da base cartográfic
- A interdisciplinaridade entre História e Geografia se caracteriza exclusivamente pela coin
- Em atividade de campo com estudantes, a qualidade da pesquisa depende também de consentime
- A avaliação diagnóstica pode identificar conhecimentos prévios dos estudantes antes de uma
- Uma prova escrita pode desempenhar função formativa quando seus resultados geram devolutiv
- Uma rubrica para avaliar cartografia escolar deve priorizar exclusivamente o acabamento es
- Avaliar um produto coletivo permite concluir, sem qualquer registro adicional, que cada in
```

## Pedido 40: Convenções internacionais contra corrupção, crime organizado e terrorismo (`direito.convencoes-anticorrupcao-crime-organizado-terrorismo`, 76 tópicos de edital)

```
Matéria: direito.convencoes-anticorrupcao-crime-organizado-terrorismo (Convenções internacionais contra corrupção, crime organizado e terrorismo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tratados, incorporação nacional e garantias constitucionais": 12
- "2. Mérida, Convenção Interamericana e convenção da OCDE": 37
- "3. Palermo: grupos organizados, transnacionalidade e instrumentos de cooperação": 23
- "4. Terrorismo: bombas, recursos financeiros e ação interamericana": 16
- "5. Casos integrados, assistência internacional e limites das medidas": 12

Já existem (não repita):
- Uma equipe prepara estudo sobre medidas preventivas e restituição internacional de dinheir
- Uma comissão analisa tratado que define grupo criminoso estruturado e cooperação contra de
- Um programa de integridade regional usa convenção adotada em Caracas, em 1996, com reserva
- Uma sociedade pretende negociar com o governo estrangeiro e estuda o tratado dedicado ao s
- Um estudo descreve o emprego intencional de explosivo contra instalação de transporte públ
- Uma entidade recebe fundos sabendo que serão usados em atentado terrorista previsto em ins
- Três pessoas mantêm grupo estruturado que planeja crimes graves lucrativos há meses. Segun
- Uma infração está sujeita a pena de prisão cujo máximo previsto é quatro anos. Qual critér
- Após confisco de recursos públicos desviados e localizados no exterior, um grupo estuda o 
- Países americanos pretendem reforçar identificação de clientes e comunicação de operações 
- Uma empresa registra despesa inexistente para ocultar vantagem prometida a agente público 
- Um projeto legislativo justifica a criação de um tipo penal de enriquecimento ilícito excl
- Uma rede organizada executa toda a infração em determinado país, mas uma parte substancial
- Uma pessoa oferece vantagem indevida a agente público nacional para que ele deixe de cumpr
- Uma investigação regional encontrou crescimento patrimonial injustificado de agente públic
- Um país não prevê responsabilidade penal de empresas por suborno de agente estrangeiro, ma
- Uma pessoa tenta instalar artefato explosivo em estação de transporte público para causar 
- O Estado requerente comprovou peculato de recursos públicos, houve confisco de bens em out
- Uma tese afirma que a legislação interna só deve tipificar lavagem decorrente de grupo org
- Uma transferência ilícita de fundos é feita com ciência de que financiará ato terrorista a
- Ao comparar o texto promulgado da Convenção Interamericana contra a Corrupção com um resum
- Uma equipe estrangeira propõe realizar diligências policiais autônomas em território de ou
- Um país solicita assistência judicial em caso de suborno de funcionário estrangeiro. O Est
- Em revisão da Convenção para Supressão do Financiamento do Terrorismo, discute-se o prazo 
- Uma proposta sustenta que Mérida impõe obrigação de criminalização idêntica para a oferta 
- Uma autoridade recebe pedido estrangeiro de diligência e observa o procedimento legal bras
- Em aula sobre recuperação internacional de valores públicos, afirma-se que a Convenção de 
- Um grupo de três pessoas atua de forma estruturada e duradoura para obter lucro com infraç
- Ao comparar dois instrumentos regionais, um pesquisador identifica o Decreto nº 5.639/2005
- Uma empresa promete benefício indevido a funcionário público estrangeiro para obter negóci
- Ao classificar infração grave para o art. 2º, b, de Palermo, a pessoa responsável pela aná
- Um programa público de integridade propõe normas éticas, mecanismos contra conflitos de in
- Diante de solicitação de auxílio jurídico por suborno de agente estrangeiro, o art. 9º, § 
- Uma instalação privada de distribuição de energia ao público sofre atentado com artefato m
- Um valor foi disponibilizado intencionalmente para financiar ato terrorista previsto na co
- Após apreensão e confisco de produto de peculato que foi enviado ao exterior, o Estado req
- Uma rede ilícita tem integrantes variáveis e não possui funções formalmente fixadas. Por e
- Uma fraude é executada integralmente em um Estado, mas produz efeitos substanciais em outr
- Uma proposta de implementação de Palermo limita os crimes nacionais de lavagem e corrupção
- Um legislador analisa tráfico de influência e enriquecimento ilícito à luz dos arts. 18 e 
- Uma nota técnica registra que o Brasil formulou reserva ao art. IX da Convenção Interameri
- Na elaboração de uma política conjunta contra terrorismo, países americanos tomam como ref
- Uma análise de Mérida conclui que o art. 16 impõe a mesma obrigação incondicional de crimi
- Uma entidade recebe intencionalmente fundos para ato terrorista convencional, mas os ativo
- Como Mérida e Palermo promovem assistência mútua, o pedido feito por um Estado Parte permi
```

## Pedido 41: Serviço Social: saúde mental, drogas, saúde do trabalhador e assédio (`servico-social.saude-mental-drogas-saude-trabalhador`, 76 tópicos de edital)

```
Matéria: servico-social.saude-mental-drogas-saude-trabalhador (Serviço Social: saúde mental, drogas, saúde do trabalhador e assédio)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Reforma psiquiátrica, direitos e Rede de Atenção Psicossocial": 22
- "2. Políticas sobre drogas, prevenção, tratamento e reinserção": 20
- "3. Saúde do trabalhador, riscos psicossociais e exames ocupacionais": 26
- "4. Readaptação, inclusão e avaliação biopsicossocial": 15
- "5. Assédio, discriminação, programa federal e Convenção nº 190": 17

Já existem (não repita):
- Uma pessoa com transtorno mental recebe proposta de internação, embora os recursos de cuid
- Após pedido de familiar, uma pessoa é hospitalizada sem seu consentimento, mediante avalia
- Equipe que atende usuários de álcool e outras drogas deseja compreender a finalidade do Si
- Uma equipe elabora ações de aproximação e prevenção de agravos para pessoas que continuam 
- A equipe de saúde ocupacional precisa planejar os exames obrigatórios de empregados segund
- Empregado retorna às atividades após ficar 35 dias afastado por doença. Pelo item 7.5.9 da
- Após limitação física persistente verificada em inspeção médica, procura-se cargo de atrib
- Em avaliação de deficiência, profissional examina impedimentos do corpo, barreiras ambient
- Uma instituição federal elabora seu plano de enfrentamento do assédio com ações formativas
- Uma pessoa denuncia humilhações em ambiente de trabalho. Qual resposta inicial é coerente 
- Um estabelecimento realizou internação psiquiátrica involuntária na segunda-feira. Qual co
- Durante transição do hospital para moradia comunitária, uma pessoa com longa institucional
- Uma comunidade terapêutica pretende manter em internação involuntária, por 60 dias, pessoa
- Uma unidade de saúde promove internação de dependente de drogas com base no art. 23-A da L
- Uma equipe revisa exames periódicos de empregados: há grupo exposto a riscos do PGR e outr
- Após levantamento epidemiológico, profissionais identificam transtornos mentais possivelme
- Pessoa com impedimento físico de longa duração dispõe de tecnologia assistiva, mas encontr
- Uma empresa terceirizada presta serviços em instituição federal. Trabalhador terceirizado 
- Após grave intercorrência clínica durante internação psiquiátrica, a direção comunica o ep
- Uma internação involuntária para desintoxicação de pessoa dependente de drogas chega ao 85
- Instituição quer cumprir a NR-1 sobre fatores psicossociais apenas aplicando teste de ansi
- Diante de alteração de saúde possivelmente relacionada ao trabalho, empregador pretende ap
- Uma pessoa apresenta impedimento mental de longo prazo e encontra barreiras sociais que re
- No Decreto nº 12.122/2024, após publicação do plano federal, instituições abrangidas devem
- Uma equipe prepara material sobre a Convenção nº 190 da OIT em 10/10/2026 e quer distingui
- Uma unidade de atendimento organiza o cuidado de pessoa com transtorno mental dando prefer
- Na Lei nº 10.216/2001, internação compulsória é aquela que ocorre sem consentimento da pes
- O art. 3º, § 2º, da Lei nº 11.343/2006 prevê que o Sisnad atue articuladamente com o Siste
- Na NR-7, o exame de mudança de riscos ocupacionais ocorre apenas após o trabalhador assumi
- O Programa Federal de Prevenção e Enfrentamento do Assédio e da Discriminação prevê os eix
- Na Lei nº 10.216/2001, a internação psiquiátrica pode ser indicada mediante simples relató
- A Lei nº 11.343/2006, art. 26-A, condiciona o acolhimento em comunidade terapêutica acolhe
- Pela Portaria GM/MS nº 5.201/2024, a notificação compulsória de transtorno mental relacion
- O item 7.5.9.1 da NR-7 dispõe que a avaliação médica no retorno ao trabalho deve definir a
- A avaliação biopsicossocial do art. 2º, § 1º, da Lei nº 13.146/2015 considera apenas imped
- Nos termos do art. 24 da Lei nº 8.112/1990, a readaptação guarda relação com limitações de
- O Decreto nº 12.122/2024 obriga publicação de dados médicos pessoais detalhados das pessoa
- Nos termos da Lei nº 10.216/2001, art. 7º, o encerramento da internação voluntária pode oc
- A Lei nº 11.343/2006 autoriza internação involuntária de usuário de drogas em comunidade t
- A Portaria MTE nº 1.419/2024 levou à redação da NR-1 vigente a partir de 26 de maio de 202
- Segundo o art. 2º, § 1º, da Lei nº 13.146/2015, a avaliação biopsicossocial da deficiência
- O conceito de violência e assédio do art. 1º da Convenção 190 da OIT admite práticas ou am
- Como a Convenção 190 da OIT foi aprovada pela Câmara dos Deputados em setembro de 2026, em
- O art. 3º da Convenção 190 da OIT inclui comunicações vinculadas ao trabalho realizadas po
- O art. 6º, § 2º, do Decreto nº 12.122/2024 conta o prazo de 120 dias para planos setoriais
```

## Pedido 42: Cálculo e álgebra linear (`matematica.calculo-algebra-linear`, 76 tópicos de edital)

```
Matéria: matematica.calculo-algebra-linear (Cálculo e álgebra linear)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Limites, continuidade e comportamento de funções": 19
- "2. Derivadas, crescimento e problemas de máximos e mínimos": 19
- "3. Integrais, áreas orientadas e teorema fundamental": 19
- "4. Espaços vetoriais, transformações lineares e normas L1 e L2": 25
- "5. Autovalores, autovetores e aplicações espectrais": 18

Já existem (não repita):
- Uma expressão de custo médio simplificada é dada, para x diferente de 3, por $$\frac{x^2-9
- Um sensor registra valor 2 para instantes negativos e valor −1 para instantes não negativo
- Uma função por partes usa x+1 quando x é menor que 1 e a constante k quando x é maior ou i
- A posição de um objeto segue $$s(t)=t^3-4t$$, com t em segundos. Qual é a velocidade insta
- O deslocamento vetorial de um equipamento é registrado por (2,−5). Quais são, respectivame
- A taxa de entrada de água em um reservatório é dada por $$q(t)=2t$$, em litros por minuto.
- O custo simplificado de operação é $$C(x)=x^2-6x+10$$, com x real. Em qual ponto ocorre o 
- Uma aplicação leva vetores do plano a novos pares de coordenadas. Qual regra define uma tr
- Um sinal de velocidade é modelado por $$v(t)=t^3$$. Ao integrar no intervalo de menos dois
- Uma transformação do plano é representada por matriz diagonal com fator −2 na primeira coo
- Um modelo de consumo usa a expressão $$f(x)=(2x+1)^3$$. Qual é sua derivada em relação a x
- Um analista encontra derivada igual a zero na origem para $$f(x)=x^3$$. Qual diagnóstico é
- Uma transformação linear atua do espaço real tridimensional para o plano real e sua imagem
- Para calcular a acumulação de uma função $$f(x)=x^2$$ entre zero e três, qual é o valor da
- Ao usar o vetor (−3,4) como registro de erro em duas coordenadas, o responsável calcula du
- Um problema de acumulação envolve a integral indefinida $$\int\frac{2x}{x^2+1}\,dx$$. Qual
- Para estimar um índice quando x cresce sem limite, utiliza-se a razão $$\frac{4x^2+3x-1}{2
- Um jardim retangular usa 24 metros de cerca em todo o perímetro. Admitindo lados não negat
- Considere a matriz com primeira linha (2,1) e segunda linha (0,2). Um sistema precisa deci
- Uma matriz triangular superior tem primeira linha (1,2) e segunda linha (0,3). Qual altern
- Um gráfico da função $$f(x)=x$$ é examinado entre menos um e um. Qual é a integral definid
- A transformação $$T(x,y)=(x+y,x-y)$$ é aplicada a vetores reais. Sobre seu núcleo e sua im
- Uma matriz triangular tem linhas (−1,4) e (0,2). Ao calcular suas direções próprias, quais
- Um modelo utiliza o gráfico $$f(x)=|x-1|$$ entre zero e dois. Qual é a área sob a curva e 
- Uma matriz A possui o autovetor v=(1,−2) associado ao autovalor −3. Qual é a norma L2 do v
- Uma função racional não está definida em x igual a dois. Um estudante conclui que, por ess
- Um programador implementa uma transformação linear de vetores e observa que a entrada zero
- Ao encontrar derivada nula em um ponto interior de uma função derivável, um analista já po
- Um vetor de componentes três e menos quatro possui norma L1 igual a sete, pois o cálculo u
- Uma resposta de integral indefinida de uma função contínua apresenta apenas uma primitiva 
- A matriz diagonal de fatores três e menos dois representa uma transformação cujos autovalo
- Um gráfico possui uma região acima e outra abaixo do eixo horizontal. Como ambas possuem á
- Uma função por partes assume fórmulas diferentes de cada lado de um ponto, mas ambas se ap
- Uma função cúbica tem derivada igual a zero na origem e derivada positiva dos dois lados. 
- Uma taxa de entrada de água de duas vezes o tempo, em litros por minuto, é integrada entre
- Ao adicionar uma unidade fixa a cada coordenada de qualquer vetor do plano, obtém-se trans
- Para um vetor real com coordenadas menos três e quatro, a norma L2 é cinco, pois a raiz da
- Uma aplicação linear admite o vetor zero como autovetor de qualquer autovalor, pois ele sa
- Um sensor tem taxa instantânea descrita por função diferenciável com derivada estritamente
- Na função que associa a cada entrada a quarta potência dessa entrada, a segunda derivada n
- Uma função contínua representa o custo de produção de lotes entre duas quantidades permiti
- Uma matriz identidade de ordem três tem somente o autovalor um, repetido três vezes. Por c
- Uma matriz quadrada possui autovalor zero. Isso significa que existe vetor não nulo levado
- A integral de uma função ímpar e contínua em um intervalo simétrico é igual a zero. Portan
- Uma transformação linear possui domínio de dimensão cinco e imagem de dimensão três. Pelo 
```

## Pedido 43: Termoquímica (`ciencias.termoquimica`, 76 tópicos de edital)

```
Matéria: ciencias.termoquimica (Termoquímica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Energia, calor, sistemas, primeira lei e sinais das reações": 23
- "2. Entalpia, equações termoquímicas e calorimetria a pressão e volume constantes": 20
- "3. Entalpias padrão de formação e combustão, Lei de Hess e cálculo estequiométrico": 27
- "4. Energia de ligação, aproximações e armadilhas de cálculo calorimétrico": 14
- "5. Entropia, segunda lei, energia livre de Gibbs e espontaneidade": 16

Já existem (não repita):
- Uma amostra é aquecida pelo contato com um corpo mais quente. No estudo de termoquímica, c
- Um recipiente selado contendo gás recebe energia por aquecimento, sem permitir fuga de mol
- Na combustão em recipiente a pressão constante, as substâncias reagem e liberam energia ao
- Um gás recebe 80 J de calor e, durante sua expansão, realiza 20 J de trabalho sobre o exte
- Em um perfil de entalpia, produtos são representados em patamar superior ao dos reagentes,
- Em um calorímetro ideal, 100 g de água aquecem 5 °C por reação que ocorre no recipiente. C
- Uma reação em solução é conduzida em recipiente aberto, a pressão aproximadamente constant
- Uma combustão ocorre numa bomba calorimétrica rígida, sem trabalho além do de expansão, qu
- Uma equação termoquímica representa a formação de dois mols de produto com variação de ent
- Ao preencher tabela de entalpias padrão de formação, considera-se carbono grafite e oxigên
- Na oxidação de CO gasoso a CO₂ gasoso, considere entalpias de formação de -110 kJ/mol para
- A formação de CO₂ a partir de carbono e oxigênio libera 394 kJ; a oxidação de CO a CO₂ lib
- Uma estimativa por energias de ligação indica 500 kJ gastos para romper ligações reagentes
- Durante uma reação num calorímetro isolado, o vaso e o banho apresentam capacidade térmica
- Em processo hipotético gasoso, a variação da energia interna é -100,0 kJ e a variação do n
- Na combustão de um mol de CH₄(g), formam-se CO₂(g) e dois mols de H₂O(l). Usando entalpias
- Num recipiente ideal sem perdas, misturam-se 100 g de água a 80 °C e 300 g de água a 20 °C
- A entalpia padrão de formação da água é aproximadamente -285,8 kJ/mol no estado líquido e 
- Um processo tem entalpia de +30 kJ/mol e entropia de +0,100 kJ/(mol K), aproximadamente co
- Um processo tem entalpia de -20 kJ/mol e entropia de -0,050 kJ/(mol K), aproximadamente co
- Durante transformação a mesma quantidade de temperatura, a entropia do sistema diminui 5 J
- Considere processo com entalpia +60 kJ/mol e entropia +0,150 kJ/(mol K), aproximadamente i
- A reação balanceada de formação de um mol de água líquida libera 285,8 kJ nas condições in
- Em reação hipotética, a bomba calorimétrica mede energia interna de -120 kJ por avanço. Pa
- A oxidação de um mol de CO(g) a CO₂(g) libera aproximadamente 283 kJ, conforme equação bal
- Uma amostra em frasco aberto libera vapor e troca energia com o ambiente; por admitir pass
- Na convenção de trabalho realizado sobre o sistema como positivo, um gás que recebe calor 
- Se os produtos têm entalpia inferior à dos reagentes na mesma reação balanceada e sob as c
- Em calorímetro perfeitamente rígido e sem outros trabalhos, o calor da reação é igual à va
- A entalpia padrão de formação de oxigênio molecular gasoso no estado de referência é conve
- Ao inverter o sentido de uma reação termoquímica balanceada, mantém-se inalterado o sinal 
- Na estimativa por energias médias de ligações gasosas, a ruptura consome energia, e a form
- Em experimento isolado, água e recipiente absorvem conjuntamente 6 kJ, mas a reação pode s
- Na formação de um mol de água gasosa, não se pode usar sem correção a entalpia padrão de f
- Se 0,20 mol de combustível liberou 10 kJ numa bomba calorimétrica ideal, a variação molar 
- A soma das mudanças de entropia do sistema e da vizinhança fornece a variação de entropia 
- Para um processo com entalpia positiva e entropia positiva, a expressão de Gibbs indica fa
- Numa equação termoquímica em que 2 mols de água líquida são produzidos com variação de -57
- Se uma reação libera 100 kJ a pressão constante, a mesma reação numa bomba rígida deve lib
- Uma transformação endotérmica pode ser espontânea a temperatura suficientemente alta se se
- Como oxigênio gasoso em seu estado padrão possui entalpia padrão de formação nula, sua ent
- Uma transformação com energia livre de Gibbs negativa é termodinamicamente favorecida em d
- Para processo hipotético com entalpia -30 kJ/mol e entropia -0,100 kJ/(mol K), a energia d
- Em calorímetro ideal isolado, se uma reação aquece 200 g de água em 10 K e o calor específ
- Segundo a Lei de Hess, a variação de entalpia de uma reação química depende do número de e
```

## Pedido 44: Turismo sustentável: conceitos, Política Nacional de Turismo, OMT, gestão pública, eventos, pareceres e avaliação (`turismo.fundamentos-politica-gestao-sustentavel`, 75 tópicos de edital)

```
Matéria: turismo.fundamentos-politica-gestao-sustentavel (Turismo sustentável: conceitos, Política Nacional de Turismo, OMT, gestão pública, eventos, pareceres e avaliação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Turismo, lazer e hospitalidade": 25
- "2. Política Nacional de Turismo e diretrizes da OMT": 25
- "3. Instrumentos de gestão pública do turismo": 25
- "4. Eventos, relatórios, pareceres e avaliação de programas": 25

Já existem (não repita):
- Segundo a Lei nº 11.771/2008, consideram-se turismo as atividades realizadas por pessoas f
- Um morador de Cuiabá vai a Chapada dos Guimarães para conhecer as cachoeiras e retorna no 
- Em relação ao Brasil, a chegada de turistas estrangeiros ao Pantanal mato-grossense corres
- Todo turismo é atividade de lazer, e toda atividade de lazer pressupõe deslocamento turíst
- Joffre Dumazedier atribui ao lazer três funções principais, que são:
- A hospitalidade pode ser analisada nos domínios doméstico, social ou público e comercial, 
- O segmento turístico que utiliza de forma sustentável o patrimônio natural e cultural, inc
- Em uma gruta com espeleotemas frágeis, a gestão define número máximo de visitantes por dia
- São exemplos de impactos negativos do turismo mal planejado em destinos naturais: acúmulo 
- São princípios da Política Nacional de Turismo, segundo a Lei nº 11.771/2008:
- Entre os objetivos da Política Nacional de Turismo está democratizar e propiciar o acesso 
- O Plano Nacional de Turismo, previsto na Lei nº 11.771/2008, é elaborado:
- No Sistema Nacional de Turismo, a Embratur tem como função principal a promoção, o marketi
- O cadastro obrigatório dos prestadores de serviços turísticos, como meios de hospedagem, a
- São considerados prestadores de serviços turísticos pela Lei nº 11.771/2008, entre outros:
- A Lei nº 11.771/2008 prevê penalidades aos prestadores de serviços turísticos que descumpr
- A Organização Mundial do Turismo é uma organização não governamental de empresas do setor 
- De acordo com a OMT, turismo sustentável é aquele que:
- O Código Mundial de Ética do Turismo, adotado no âmbito da OMT, inclui entre seus princípi
- Entre as diretrizes do turismo sustentável defendidas pela OMT, está:
- No planejamento turístico de um município, o levantamento sistemático dos atrativos, dos s
- O Programa de Regionalização do Turismo propõe a gestão descentralizada da atividade por m
- O Mapa do Turismo Brasileiro:
- Os conselhos municipais de turismo, compostos por representantes do poder público, do seto
- Em um parque estadual, a abertura de nova trilha para visitação deve observar principalmen
- A concessão de serviços de apoio à visitação em unidades de conservação transfere à inicia
- A organização de um evento costuma dividir-se em pré-evento, trans-evento e pós-evento. Pe
- Em um evento voltado ao turismo sustentável, são boas práticas a coleta seletiva, a reduçã
- Na perspectiva adequada de educação ambiental, um evento com esse objetivo deve:
- O parecer técnico sobre um projeto turístico é peça decisória que, por si só, aprova ou re
- A estrutura usual de um parecer técnico compreende:
- Ao analisar projeto de pousada às margens de um rio em área de visitação, o técnico deve v
- Um relatório técnico deve ter linguagem clara, objetiva e impessoal, apresentar metodologi
- Um programa de ecoturismo cumpriu todas as metas de capacitação previstas (eficácia), mas 
- O monitoramento de um programa turístico é feito de forma contínua durante a execução, enq
- Para avaliar se um programa elevou o fluxo de visitantes de um destino, é necessário conhe
- No monitoramento de um destino, são exemplos de indicadores de resultado e de impacto do t
- Pela definição legal, a pessoa que se muda para outra cidade e lá permanece residindo por 
- A Política Nacional de Turismo inclui, entre seus objetivos, promover a acessibilidade às 
- O turismo sustentável, segundo a OMT, aplica-se apenas a segmentos realizados em áreas nat
- Indicadores de produto medem as entregas diretas de um programa, como trilhas sinalizadas 
```

## Pedido 45: Tribunais e juízes militares e dos Estados (`direito-constitucional.tribunais-juizes-militares-estados`, 75 tópicos de edital)

```
Matéria: direito-constitucional.tribunais-juizes-militares-estados (Tribunais e juízes militares e dos Estados)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura constitucional da Justiça Militar da União": 20
- "2. Lei 8.457/1992: órgãos, Auditorias e Conselhos da Justiça Militar da União": 19
- "3. Justiça dos Estados: Constituição estadual, Tribunal de Justiça e organização judiciária": 37
- "4. Justiça Militar estadual: criação, composição, competências e exceção do júri": 12
- "5. Competências comparadas, situações limítrofes e precedentes": 12

Já existem (não repita):
- Uma pessoa estuda quais órgãos constam expressamente da seção constitucional relativa à Ju
- Ao conferir a composição do STM, um estudante soma seus ministros militares e civis. Qual 
- Um ministro civil do STM será escolhido em 2026. Conforme o art. 123, parágrafo único, alt
- Um integrante de força federal é acusado de crime militar definido em lei. Qual artigo con
- Segundo a Lei 8.457/1992, quantas Circunscrições Judiciárias Militares dividem o territóri
- Uma praça de força federal responde isoladamente por crime militar. Segundo o art. 27 da L
- Uma Constituição estadual será aplicada para identificar a competência de seu Tribunal de 
- Um Estado pretende criar Tribunal de Justiça Militar próprio. Além de proposta do TJ e lei
- Um civil é acusado de delito contra instituição militar estadual. Qual ramo recebe o caso 
- Um militar estadual questiona judicialmente a legalidade de punição disciplinar. Quem julg
- O chefe do Executivo escolhe os dois ministros civis do STM reservados às carreiras previs
- Um oficial de força federal, que não é oficial-general, responde por crime militar sem civ
- Um regulamento interno desatualizado afirma que o oficial militar de maior posto preside o
- Um civil responde por fato que se enquadra nos incisos I ou III do art. 9º do Código Penal
- Um Estado deseja instituir representação de inconstitucionalidade de lei municipal em face
- Ao revisar programa de acesso à Justiça estadual, um gestor compara Câmaras regionais e ju
- Um militar estadual é acusado de homicídio doloso contra civil, sem circunstância que reti
- Um policial militar estadual é investigado por crime militar e por crime comum distintos, 
- Um oficial-general da ativa de força federal é acusado de crime militar definido em lei. A
- Um Estado tem efetivo militar exatamente de 20.000 integrantes e pretende criar um TJM sep
- Militar estadual pratica crime militar de lesão corporal culposa contra civil, sem imputaç
- Dois militares estaduais são acusados de crime militar contra bem da própria corporação, s
- Um procedimento busca a decisão de perda do posto e da patente de oficial de força estadua
- Uma proposta afirma que, por ser mencionado ao lado dos Estados no art. 92, o Distrito Fed
- Um civil e um militar federal figuram conjuntamente como acusados em processo que se enqua
- O art. 123 da Constituição estabelece STM com quinze ministros vitalícios, sendo dez milit
- A Justiça Militar da União recebe da Constituição competência para julgar apenas infrações
- Na Lei 8.457/1992, o Conselho Especial de Justiça processa e julga oficiais, ressalvados o
- O art. 125, § 1º, atribui ao governador a iniciativa da lei de organização judiciária esta
- Em crimes militares de militares estaduais contra civis que não sejam dolosos contra a vid
- O STM possui cinco ministros civis, dois dos quais devem ser escolhidos entre advogados e 
- Desde a reforma da Lei 8.457/1992 pela Lei 13.774/2018, o juiz federal da Justiça Militar 
- A representação de inconstitucionalidade prevista no art. 125, § 2º, tem por parâmetro ape
- A Justiça Militar estadual pode ser criada por lei estadual mediante proposta do Tribunal 
- O fato de o Estado possuir vinte mil militares exatamente obriga a criação de Tribunal de 
- A competência do Tribunal do Júri deve ser ressalvada, na esfera estadual, quando militar 
- A existência de um crime comum simultâneo a crime militar cometido por policial militar es
- Na Justiça Militar da União, o art. 30, I-B, da Lei 8.457/1992 admite julgamento monocráti
- Um oficial-general da força federal acusado de crime militar será julgado, em primeiro gra
- A Constituição permite que o Tribunal de Justiça estadual funcione descentralizadamente em
- Por constarem ao lado dos tribunais estaduais no art. 92, os órgãos do Poder Judiciário do
- A Justiça Militar estadual não possui competência penal para julgar civil acusado de crime
- Se militar estadual questionar judicialmente sanção disciplinar, o julgamento em primeiro 
- Em Estado sem Tribunal de Justiça Militar separado, o Tribunal de Justiça pode desempenhar
- Como o STM integra os órgãos constitucionais da Justiça Militar, os Tribunais de Justiça M
```

## Pedido 46: Apresentação, retenção de documentos e identificação criminal (`legislacao.retencao-documentos-identificacao-criminal`, 75 tópicos de edital)

```
Matéria: legislacao.retencao-documentos-identificacao-criminal (Apresentação, retenção de documentos e identificação criminal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Apresentação, retenção e devolução de documentos pessoais — Lei 5.553/1968": 44
- "2. Identificação civil e hipóteses de identificação criminal — arts. 1º a 4º da Lei 12.037/2009": 14
- "3. Identificação datiloscópica, fotográfica e coleta do perfil genético": 15
- "4. Sigilo, exclusão de dados e Banco Nacional Multibiométrico — arts. 6º a 7º-C": 14
- "5. Casos difíceis, controle das medidas e comparação de regimes": 13

Já existem (não repita):
- Na entrada de um prédio, a recepção exige documento de identidade para registrar visitante
- Em procedimento administrativo que exige apresentação de documento de identificação, o ate
- Após decorrido o prazo previsto para extrair os dados de documento pessoal, uma entidade p
- Uma entidade conserva documentos de identificação pessoal de clientes como garantia de pag
- Uma pessoa apresenta fotocópia autenticada de documento pessoal, e o destinatário pretende
- Uma pessoa apresenta passaporte válido e legível em investigação, sem indícios de inconsis
- Uma pessoa apresenta carteira de identificação militar, suficiente para a finalidade inves
- Durante identificação, são exibidas duas carteiras com nomes idênticos, mas datas de nasci
- Uma carteira pessoal está rasurada em campo essencial, havendo suspeita de alteração. Qual
- Uma identificação criminal legalmente cabível deve ser conduzida de modo a reduzir exposiç
- Uma gerência ordena que a recepção retenha documentos dos clientes como caução. A pessoa a
- Embora o documento civil seja idôneo, a identificação criminal foi considerada essencial à
- Uma pessoa apresenta carteira íntegra, mas constam em registros policiais qualificações di
- Em caso concreto, foi recebida denúncia por crime contra criança abrangido pelo art. 241-A
- Após reconhecer uma hipótese legal de identificação criminal, devem-se documentar os proce
- O investigado não teve denúncia oferecida, e o inquérito foi definitivamente arquivado. Ap
- Uma pessoa foi condenada e já cumpriu a pena. Deseja a exclusão de seu perfil genético arm
- A comparação de perfis genéticos produz coincidência relevante para investigação. Que regi
- Um banco de identificação civil pretende interoperar com o Banco Nacional Multibiométrico.
- Em investigação instaurada, a autoridade legitimada deseja consultar registros do Banco Na
- Um documento civil idôneo foi apresentado, mas houve despacho judicial que considerou iden
- Ocorre prisão em flagrante por crime contra a liberdade sexual compreendido no art. 3º, VI
- Uma denúncia foi recebida por participação em organização criminosa, mas a descrição do ca
- Uma pessoa foi absolvida, e existe registro fotográfico nos autos e perfil genético no ban
- Uma entidade propõe vender parte dos registros do Banco Nacional Multibiométrico para pesq
- Uma recepção de local privado condiciona o ingresso à apresentação de carteira de identida
- Uma entidade pretende manter por oito dias documento apresentado para extração de dados, f
- Uma pessoa civilmente identificada apresenta carteira de trabalho apta a individualizá-la.
- Duas carteiras de identidade com informações inteiramente coerentes são apresentadas na me
- Identificação criminal admitida pela Lei 12.037/2009 compreende processos datiloscópico e 
- Mesmo quando a identificação criminal é considerada essencial à investigação, o art. 3º, I
- Uma carteira deteriorada foi considerada insuficiente para individualização do investigado
- Uma pessoa condenada concluiu o cumprimento da pena e pediu exclusão imediata do seu perfi
- Enquanto não houver trânsito em julgado de sentença condenatória, é proibido mencionar a i
- Após instauração de inquérito, a autoridade legitimada pode consultar diretamente o Banco 
- Recebida denúncia por crime praticado com grave violência contra a pessoa, a hipótese do a
- Pela versão atual do art. 5º da Lei 12.037, a coleta genética continua restrita à identifi
- Em flagrante relativo a um dos crimes enumerados no art. 3º, VII, a regra do art. 5º, § 2º
- Se o Banco Nacional Multibiométrico firmar convênio com uma base de identificação eleitora
- Em investigação, coincidência de impressões digitais obtida a partir de registros do Banco
- Um documento de identidade foi emitido há muitos anos, mas ainda permite ler todos os cara
- Uma pessoa já condenada à reclusão em regime inicial fechado ingressa no estabelecimento d
- Na rejeição definitiva de denúncia, o art. 7º da Lei 12.037 determina retirada automática 
- Uma pessoa identificada geneticamente durante investigação é absolvida. O art. 7º-A, I, da
- Uma empresa propõe a compra de parte dos registros do Banco Nacional Multibiométrico para 
```

## Pedido 47: Quebra de sigilo bancário, fiscal e de dados e acesso a dados cadastrais (`legislacao-penal-especial.sigilo-bancario-fiscal-dados`, 75 tópicos de edital)

```
Matéria: legislacao-penal-especial.sigilo-bancario-fiscal-dados (Quebra de sigilo bancário, fiscal e de dados e acesso a dados cadastrais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sigilo bancário na Lei Complementar 105/2001": 42
- "2. Poder Legislativo, Fisco, sigilo fiscal e crimes": 42
- "3. Dados cadastrais e registros eletrônicos na investigação": 16

Já existem (não repita):
- Pela Lei Complementar 105/2001, as instituições financeiras conservarão sigilo em suas ope
- Para os efeitos da Lei Complementar 105/2001, NÃO está expressamente listada como institui
- As empresas de fomento comercial (factoring) obedecem, para os efeitos da Lei Complementar
- Segundo a Lei Complementar 105/2001, NÃO constitui violação do dever de sigilo:
- A comunicação às autoridades competentes da prática de ilícitos penais ou administrativos,
- A quebra de sigilo bancário, nos termos da Lei Complementar 105/2001, poderá ser decretada
- A Lei Complementar 105/2001 destaca, entre os crimes em que a quebra de sigilo pode ser de
- O delegado de polícia, por ato próprio e fundamentado, pode requisitar diretamente às inst
- As informações bancárias ordenadas pelo Poder Judiciário serão prestadas preservando-se se
- A comissão de inquérito administrativo destinada a apurar responsabilidade de servidor púb
- Quando o Banco Central ou a CVM verificarem, no exercício de suas atribuições, a ocorrênci
- O sigilo bancário não pode ser oposto ao Banco Central do Brasil no desempenho de suas fun
- Segundo a Lei Complementar 105/2001, as comissões parlamentares de inquérito obterão as in
- As autoridades fiscais tributárias podem examinar documentos e registros de instituições f
- Ao julgar a constitucionalidade do acesso da administração tributária a dados bancários se
- As informações das operações financeiras transferidas pelas instituições financeiras à adm
- Segundo o art. 198 do Código Tributário Nacional, é vedada a divulgação, pela Fazenda Públ
- O sigilo fiscal impede a divulgação de informações relativas a representações fiscais para
- O intercâmbio de informação sigilosa no âmbito da Administração Pública, segundo o CTN, de
- Para o STF, é constitucional o compartilhamento, sem prévia autorização judicial, dos rela
- A quebra de sigilo fora das hipóteses autorizadas na Lei Complementar 105/2001 constitui c
- O servidor público que utilizar ou viabilizar a utilização de informação obtida com a queb
- Nos termos da Lei 12.850/2013, o delegado de polícia e o Ministério Público terão acesso, 
- A Lei 9.613/1998 (lavagem de dinheiro) também garante à autoridade policial e ao Ministéri
- Na Lei 12.850/2013, as empresas de transporte devem possibilitar ao juiz, ao Ministério Pú
- Na Lei 12.850/2013, recusar ou omitir dados cadastrais, registros, documentos e informaçõe
- Segundo o art. 13-A do CPP, nos crimes de sequestro e cárcere privado, tráfico de pessoas 
- O art. 13-B do CPP permite que o delegado ou o Ministério Público requisitem às operadoras
- Na hipótese do art. 13-B do CPP, não havendo manifestação judicial no prazo de 12 horas, a
- No Marco Civil da Internet, o administrador de sistema autônomo deve manter os registros d
- A autoridade policial que requer cautelarmente ao provedor a guarda de registros de conexã
- Pelo Marco Civil da Internet, a disponibilização dos registros de conexão e de acesso a ap
- Segundo o Marco Civil da Internet, o requerimento judicial de registros de conexão ou de a
- O Marco Civil da Internet impede que autoridades administrativas com competência legal req
- O conteúdo das comunicações privadas armazenadas por provedores de internet só pode ser di
- Em investigação de lavagem de dinheiro, o delegado pretende obter: (I) o endereço do inves
- O fornecimento de dados sobre operações de crédito adimplidas a gestores de bancos de dado
- O dever de sigilo previsto na Lei Complementar 105/2001 é extensivo ao Banco Central do Br
- A quebra do sigilo bancário do investigado, em inquérito policial, deve ser decretada por:
- Assinale a alternativa que associa corretamente a informação e a necessidade de ordem judi
```

## Pedido 48: Gases (`ciencias.gases`, 75 tópicos de edital)

```
Matéria: ciencias.gases (Gases)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Propriedades dos gases, teoria cinética, pressão e temperatura": 22
- "2. Leis clássicas, equação ideal, conversões e densidade": 29
- "3. Misturas gasosas, pressões parciais, difusão e efusão": 19
- "4. Energia interna, primeira lei, trabalho e transformações elementares": 17
- "5. Adiabáticas, gases reais, desvios e reconhecimento de gases perigosos": 13

Já existem (não repita):
- Um gás comprimido aplica uma força normal de 600 N sobre uma superfície de 0,20 m². Qual p
- Um recipiente com gás foi mantido a 27 °C. Para inserir sua temperatura absoluta numa equa
- Uma amostra fixa de gás ideal ocupa 2 L sob pressão de 3 atm. Em processo isotérmico, pass
- Um gás ideal ocupa 3 L a 300 K, sob pressão constante e quantidade fixa. Ao atingir 600 K,
- Um gás ideal está em frasco rígido e vedado, inicialmente a 100 kPa e 250 K. Sua temperatu
- Em pistão de pressão e temperatura constantes, dois mols de gás ideal ocupam 5 L. Se forem
- Considere gás ideal a 300 K, sob 1 atm, ocupando aproximadamente 24,6 L. Com R igual a 0,0
- Uma mistura ideal tem 1 mol de gás A e 3 mol de gás B, sob pressão total de 400 kPa. Qual 
- Dois gases ideais A e B apresentam massas molares de 2 e 32 g/mol e efundem por orifícios 
- Dois gases A e B têm massas molares 9 e 36 g/mol e efundem em condições comparáveis. Qual 
- Um manômetro de um recipiente indica 120 kPa acima do ar externo, cuja pressão absoluta é 
- Em condições definidas como 0 °C e 1 atm, 1 mol de um gás ideal de massa molar 28 g/mol oc
- Uma amostra fixa ocupa 3 L sob 1 atm e 300 K. Ela passa a 2 atm e 450 K. Tratando-a como i
- Duas amostras de gases A e B efundem por orifícios idênticos. As massas molares são 4 e 16
- Um gás recebe 800 J de calor e realiza 300 J de trabalho durante uma transformação. Sob a 
- Um gás se expande sob pressão constante de 100 kPa, passando de 4 L para 7 L. Quanto traba
- Uma amostra em recipiente rígido recebe 400 J de calor, sem outros tipos de trabalho. Qual
- Em experimento de efusão sob mesmas condições, gás A tem massa molar 4 g/mol e taxa três v
- Uma amostra gasosa foi recolhida sobre água. A pressão total é 100 kPa e a pressão parcial
- Dois mols de gás ideal monoatômico aquecem de 300 K para 400 K. Com R igual a 8,314 J/(mol
- Um mol de gás ideal monoatômico expande-se adiabaticamente e sofre redução de 40 K. Com R 
- Um gás ideal passa por expansão adiabática reversível com expoente γ igual a 1,4 e seu vol
- Em uma condição experimental, calculou-se para um gás real o quociente entre pressão-volum
- Em uma adiabática reversível de gás ideal monoatômico com expoente γ igual a 5/3, o volume
- Um gás ideal completa um ciclo e retorna ao estado inicial. Ao longo do percurso, recebe c
- Segundo o modelo cinético de gás ideal, a pressão sobre as paredes do recipiente decorre d
- Um gás em volume fixo passa de 20 °C para 40 °C e, por isso, dobra sua pressão, pois a tem
- Numa mistura de gases ideais, a pressão total é a soma das pressões parciais, e cada press
- Na lei de Graham, para a mesma temperatura e condições comparáveis, a taxa de efusão é inv
- Para uma amostra fixa de gás ideal em transformação isotérmica, a variação de energia inte
- Em qualquer transformação adiabática de gás ideal, a temperatura permanece constante, porq
- A lei combinada relaciona pressão, volume e temperatura absoluta entre dois estados de um 
- Uma leitura manométrica de 150 kPa num recipiente corresponde à pressão absoluta de 150 kP
- Dois gases de massas molares 2 e 32 g/mol apresentam, sob mesmas condições para efusão ide
- Sob convenção em que o trabalho realizado pelo gás é positivo, se o gás recebe 600 J de ca
- Num ciclo completo de um gás ideal, a energia interna final coincide com a inicial; por is
- Para um mesmo gás ideal, sob pressão constante, duplicar a temperatura em kelvin mantém su
- Numa transformação isocórica de gás ideal sem outras formas de trabalho, o trabalho de exp
- A simples condição de ausência de troca de calor permite aplicar a relação de Poisson entr
- Em expansão adiabática reversível de gás ideal com expoente igual a cinco terços, aumentar
- Uma expansão adiabática de gás ideal provoca necessariamente queda da temperatura, mesmo q
- Para gás ideal monoatômico clássico, as capacidades caloríficas molares podem ser expressa
- A ausência de cheiro perceptível em ambiente com combustão descarta a presença de monóxido
- O sulfeto de hidrogênio pode apresentar odor de ovo podre, mas a percepção olfativa pode s
- Para qualquer gás real em qualquer pressão e temperatura, o fator de compressibilidade é n
```

## Pedido 49: Cédulas de crédito rural, industrial, comercial, à exportação, bancário e CPR (`direito-empresarial.cedulas-credito`, 74 tópicos de edital)

```
Matéria: direito-empresarial.cedulas-credito (Cédulas de crédito rural, industrial, comercial, à exportação, bancário e CPR)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regime geral dos títulos de crédito e mapa das cédulas": 16
- "2. Crédito rural: modalidades, finalidade e garantias cedulares": 20
- "3. Cédulas industrial, comercial e à exportação: remissões e particularidades": 26
- "4. Cédula de Crédito Bancário: requisitos, saldo devedor e força executiva": 19
- "5. Cédula de Produto Rural: liquidação, registro, garantias e execução": 19

Já existem (não repita):
- Uma cooperativa financia custeio de produção agrícola e pretende formalizar promessa de pa
- Uma cédula de financiamento rural tem simultaneamente penhor sobre máquinas e hipoteca de 
- Uma instituição financia estabelecimento dedicado a prestação de serviços, emitindo cédula
- Uma pessoa emite Cédula de Crédito Bancário por crédito contratado sem hipoteca, penhor ou
- Um produtor se compromete por Cédula de Produto Rural física a entregar quantidade e quali
- Uma CCB regularmente formalizada é levada a cobrança judicial após inadimplemento. Em qual
- Uma operação financia bens destinados à exportação e atividades de apoio essenciais, sendo
- Um emissor de CPR emitida em setembro de 2026 precisa cumprir, em regra, o prazo legal de 
- Uma cédula de crédito rural registra financiamento de R$ 90 mil, mas foram liberados R$ 60
- Uma cédula industrial regularmente assinada ainda não foi inscrita no registro aplicável. 
- Uma CCB é transferida a adquirente que não é instituição financeira, por endosso que ident
- Um banco emitiu CCB vinculada a limite rotativo de R$ 100 mil, mas o cliente utilizou apen
- Uma CPR financeira prevê obrigação referida a 50 toneladas de produto, com preço final de 
- Um exportador contrata financiamento por Cédula de Crédito à Exportação. Um parecerista af
- Uma CPR escritural é transferida em sistema eletrônico de titularidade autorizado. Para ef
- Uma CCB garante dívida por hipoteca, mas a cédula não foi registrada em cartório. O devedo
- Um credor pretende executar diretamente contrato simples de abertura de crédito, com extra
- Uma CPR emitida em outubro de 2026 prevê garantia hipotecária de imóvel rural. A parte afi
- O valor da garantia de uma CCB foi reduzido por deterioração. O credor notificou por escri
- O titular original de CPR física transferiu o título por endosso completo. Diante da falta
- Uma empresa usa cédula industrial assinada para garantir dívida por penhor, mas não foi ef
- Uma CCB é emitida originalmente em favor de instituição financeira domiciliada no exterior
- Um produtor pretende vender antes da quitação bens empenhados por cédula rural. O credor a
- Um financiamento comercial está garantido por alienação fiduciária de coisas descritas pel
- Uma pessoa legitimada emite CPR referente a atividade de comercialização de insumos agríco
- Uma cédula rural pignoratícia e hipotecária pode vincular simultaneamente bens empenhados 
- O Decreto-Lei nº 413/1969 disciplina os títulos de crédito à exportação, enquanto a Lei nº
- Uma empresa de prestação de serviços pode enquadrar financiamento em Cédula de Crédito Com
- A Cédula de Crédito Bancário só é título executivo extrajudicial quando hipotecada, porque
- Na CPR física vencida e inadimplida, a Lei nº 8.929/1994, art. 15, prevê execução para ent
- Uma CPR emitida em março de 2026 deve, como regra geral, ser registrada ou depositada em a
- Na Cédula de Crédito Bancário, o art. 28 da Lei nº 10.931/2004 admite força executiva extr
- Uma cédula de crédito industrial obriga seus signatários apenas depois de inscrita, pois o
- Uma CCB pode circular mediante endosso em preto para adquirente que não seja instituição f
- Para execução de CCB de cheque especial, o Tema Repetitivo 576 dispensou demonstrativos so
- A Lei nº 6.313/1975, art. 3º, aplica às cédulas de crédito à exportação as regras correspo
- Uma CPR emitida sob a forma escritural exige que todo negócio posterior seja transcrito no
- O art. 42 da Lei nº 10.931/2004 distingue a validade da CCB, que independe de registro, da
- Uma CPR de 2026, devidamente depositada em entidade autorizada, contém hipoteca. Seu depós
- Uma CCB de crédito rotativo recompõe automaticamente, dentro da vigência do título, o limi
- Conforme o art. 10, II, da Lei nº 8.929/1994, o endossante de CPR física responde diretame
- O art. 4º-A, § 4º, da Lei nº 8.929/1994 determina que a CPR relativa aos produtos dos inci
- Uma CCB de crédito rotativo foi emitida por R$ 150 mil, valor do limite, mas houve utiliza
- No crédito comercial, o art. 4º da Lei nº 6.840/1980 preserva a eficácia da alienação fidu
- Para CPR emitida em maio de 2026, a regra de dispensa de registro por critérios de valor o
```

## Pedido 50: Jurisprudência dos Tribunais Superiores em Processo Civil (`processo-civil.jurisprudencia-tribunais-superiores`, 74 tópicos de edital)

```
Matéria: processo-civil.jurisprudencia-tribunais-superiores (Jurisprudência dos Tribunais Superiores em Processo Civil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Normas fundamentais, competência, gratuidade da justiça e intervenção de terceiros": 20
- "2. Tutelas provisórias, prazos, intimações, provas, sentença e coisa julgada": 20
- "3. Sistema de precedentes (IRDR, IAC e repetitivos) e teoria geral e espécies de recursos": 20
- "4. Cumprimento de sentença, processo de execução, impenhorabilidades e honorários advocatícios": 20
- "5. Fazenda Pública em juízo, mandado de segurança, ação civil pública e como o tema costuma ser cobrado": 20

Já existem (não repita):
- Em ação de cobrança fundada em contrato paritário celebrado entre duas empresas, o juiz ve
- Uma associação civil sem fins lucrativos ajuíza ação declaratória e requer o benefício da 
- Se uma autarquia federal requerer seu ingresso como assistente simples em processo que tra
- Um usuário propõe ação de obrigação de fazer cumulada com indenização por danos morais em 
- Após o juiz estadual remeter os autos à Justiça Federal em razão da inclusão de uma empres
- As ações cíveis propostas em face de sociedade de economia mista controlada majoritariamen
- A respeito da intervenção do amicus curiae regulada pelo Código de Processo Civil e interp
- Em ação de reparação de danos causados em acidente de trânsito, embora a vítima não possa 
- Mesmo após o trânsito em julgado da sentença de mérito proferida por um dos juízos suscita
- Após tomar ciência da sentença pela consulta ao sistema eletrônico do tribunal, o advogado
- Em ação de investigação de paternidade, o réu devidamente intimado recusa-se, de forma inj
- Segundo a jurisprudência sumulada do Supremo Tribunal Federal, não cabe recurso extraordin
- Uma sentença condenatória transitou em julgado na vigência do CPC/2015 sem qualquer menção
- O autor ajuizou ação indenizatória formulando pedido certo e determinado no valor exato de
- No incidente de exibição de documentos instaurado contra a parte adversa no curso do proce
- Concedida a tutela antecipada requerida em caráter antecedente e não havendo aditamento ou
- Segundo o entendimento pacificado pela Corte Especial do Superior Tribunal de Justiça, oco
- Conforme a jurisprudência sumulada do Supremo Tribunal Federal, é plenamente cabível ação 
- Inconformado com acórdão de tribunal de segundo grau que deixou de aplicar o entendimento 
- A parte vencida na apelação opõe embargos de declaração com o objetivo manifesto de obter 
- Quando a interposição do recurso ocorrer após o encerramento do expediente bancário, admit
- Ao julgar o Tema 988 sob a sistemática dos recursos repetitivos, a Corte Especial do Super
- O vice-presidente de um tribunal de segundo grau profere decisão negando seguimento a recu
- Para a admissão do Incidente de Assunção de Competência (IAC) perante o tribunal, exige-se
- Acerca do prequestionamento ficto (art. 1.025 do CPC) e do valor do voto vencido (art. 941
- Do julgamento do mérito do Incidente de Resolução de Demandas Repetitivas (IRDR) caberá re
- Segundo a jurisprudência do Superior Tribunal de Justiça, a técnica de ampliação do colegi
- Em execução de título extrajudicial, o credor requer a penhora do único imóvel residencial
- O executado possui um único imóvel residencial, mas nele não reside porque o alugou a terc
- A vaga de garagem vinculada a apartamento residencial, ainda que possua matrícula própria 
- Transitada em julgado sentença que condenou o réu a cumprir obrigação de fazer sob pena de
- No cumprimento definitivo de sentença para pagamento de quantia certa entre particulares, 
- Os honorários advocatícios incluídos na condenação ou destacados do montante principal dev
- Considerando a Súmula 549 do STJ e as teses de repercussão geral fixadas pelo Supremo Trib
- De acordo com a tese firmada pela Corte Especial do STJ no Tema 1.076 dos recursos repetit
- Para a satisfação de crédito de natureza não alimentar, a jurisprudência da Corte Especial
- A respeito dos efeitos patrimoniais e da condenação em honorários advocatícios de sucumbên
- Um fornecedor privado detém título executivo extrajudicial líquido, certo e exigível firma
- De acordo com a Súmula 625 do Supremo Tribunal Federal, a existência de controvérsia sobre
- Após o trânsito em julgado de sentença coletiva que condenou uma autarquia federal ao paga
- Sobre o instituto da remessa necessária (reexame necessário) nas causas decididas contra a
- O Ministério Público não possui legitimidade ativa para propor ação civil pública na defes
- No julgamento do Tema 1.075 de repercussão geral, o Supremo Tribunal Federal examinou a co
- Conforme tese de repercussão geral fixada pelo Supremo Tribunal Federal (Tema 530), é líci
- Reconhecida a continência entre duas ações civis públicas — uma proposta perante a Justiça
```

