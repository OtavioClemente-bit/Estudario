# Pedidos de questões extras, lote 23 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 481 tópicos de edital.

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

## Pedido 1: Fisioterapia na saúde da mulher, pediatria e geriatria (`fisioterapia.saude-mulher-pediatria-geriatria`, 13 tópicos de edital)

```
Matéria: fisioterapia.saude-mulher-pediatria-geriatria (Fisioterapia na saúde da mulher, pediatria e geriatria)
Capítulos e quantas questões quero em cada um (total 100):
- "Fisioterapia uroginecológica e obstétrica": 17
- "Fisioterapia pediátrica e neonatal": 17
- "Fisioterapia em gerontologia": 17
- "Fisioterapia esportiva e treinamento": 17
- "Fisioterapia dermatofuncional e ergonomia": 16
- "Ética, legislação COFFITO e organização do cuidado": 16

Já existem (não repita):
- Mulher de 43 anos relata escape urinário apenas quando tosse ou corre, sem vontade súbita 
- Na avaliação de puérpera com dor à penetração, assoalho pélvico tenso e dificuldade de rel
- Durante consulta de gestante sem complicações, qual afirmação orienta corretamente a condu
- Paciente refere urgência urinária e perdas antes de chegar ao banheiro, sem associação cla
- A incontinência urinária mista reúne manifestações de perda ao esforço e perdas acompanhad
- O uso de biofeedback dispensa treino muscular ativo, pois a visualização do sinal substitu
- O exame íntimo em fisioterapia pélvica deve ser realizado em toda primeira consulta mesmo 
- Para acompanhar marcos motores de um bebê prematuro nascido com 32 semanas e agora com 6 m
- Ao perceber assimetria persistente de movimento e dificuldade funcional em criança pequena
- Fisioterapeuta atende recém-nascido prematuro clinicamente estável, mas que apresenta sina
- Uma equipe decide acompanhar a evolução motora infantil utilizando a Caderneta da Criança 
- No cuidado fisioterapêutico infantil, brincadeiras podem funcionar como meios para trabalh
- Uma triagem de desenvolvimento alterada confirma, por si só, diagnóstico etiológico de doe
- Em prematuros, o posicionamento terapêutico temporário e supervisionado na unidade neonata
- Uma pessoa idosa decide suas atividades, mas precisa da ajuda de outra pessoa para tomar b
- Idosa com quedas recorrentes, medo de cair e fraqueza dos membros inferiores deve receber 
- Sobre o resultado do Timed Up and Go em pessoa idosa que usa dispositivo de marcha, assina
- No planejamento terapêutico de pessoa idosa com osteoartrite e limitações ao caminhar, qua
- Fragilidade é conceito de maior vulnerabilidade a eventos adversos e não pode ser deduzida
- Para evitar quedas, toda pessoa idosa com osteoporose deve interromper definitivamente exe
- Em cuidados paliativos, podem existir metas fisioterapêuticas relacionadas a conforto, tra
- Um corredor aumenta rapidamente a quilometragem sem adaptação e desenvolve dor relacionada
- Após entorse de tornozelo, jogadora anda sem dor, mas falha em testes de apoio unipodal e 
- No planejamento de retorno ao esporte após reabilitação, qual conjunto de critérios aprese
- Sobre o princípio da especificidade no treinamento de atleta que retorna a esporte com sal
- Na fisioterapia esportiva, retorno à participação, retorno ao esporte e retorno ao desempe
- O número de semanas transcorridas após uma lesão é critério suficiente para liberar qualqu
- A carga percebida pelo atleta e as demandas externas, como velocidade, distância e repetiç
- Paciente apresenta cicatriz elevada que se estende além das margens originais da ferida; a
- Em ambulatório dermatofuncional, pessoa relata edema unilateral súbito, doloroso e associa
- Uma empresa solicita intervenção ergonômica por queixas musculoesqueléticas em operadores;
- No tratamento de linfedema crônico clinicamente avaliado, qual descrição é a mais defensáv
- A fisioterapia dermatofuncional pode avaliar repercussões funcionais de cicatrizes, edema,
- Pela lógica da NR-17, oferecer ginástica laboral é suficiente para neutralizar qualquer ex
- Cicatriz hipertrófica costuma manter-se nos limites da ferida original, enquanto o queloid
- Qual diploma legal criou o Conselho Federal e os Conselhos Regionais de Fisioterapia e Ter
- Fisioterapeuta concluiu pós-graduação lato sensu em área reconhecida pelo COFFITO e deseja
- Ao organizar atendimento comunitário de gestantes, puérperas e idosos na Atenção Primária,
- Uma clínica identifica equipamento terapêutico com falha intermitente e manutenção vencida
- A Resolução COFFITO nº 424/2013 estabelece o Código de Ética e Deontologia da Fisioterapia
- Por ser empregador do fisioterapeuta, um clube esportivo pode exigir a publicação de dados
- Na atenção primária, a atuação fisioterapêutica pode incluir ações educativas, prevenção d
```

## Pedido 2: Identificação e análise das mesorregiões de Minas Gerais (`minas-gerais.identificacao-analise-mesorregioes-minas-gerais`, 13 tópicos de edital)

```
Matéria: minas-gerais.identificacao-analise-mesorregioes-minas-gerais (Identificação e análise das mesorregiões de Minas Gerais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regiões de Minas: escalas e limites": 25
- "2. Formação territorial e economia regional": 25
- "3. População, urbanização e integração": 25
- "4. Reescrita geográfica e padrão de cobrança IDECAN": 25

Já existem (não repita):
- Sobre as divisões regionais do IBGE em Minas Gerais, assinale a afirmação correta.
- Qual enunciado diferencia adequadamente as regiões imediatas das intermediárias?
- Assinale a lista formada pelos nomes das doze mesorregiões da antiga divisão do IBGE em Mi
- Sobre a divisão político-administrativa e os recortes regionais, marque a alternativa corr
- Qual alternativa descreve corretamente a leitura de dados demográficos por cor ou raça?
- Um pesquisador encontra mesorregiões em uma base estatística recente. Qual conclusão é mai
- Sobre os critérios da antiga divisão em mesorregiões, assinale a alternativa correta.
- Uma análise da ocupação mineira relaciona mineração colonial, caminhos e formação de núcle
- Em um estudo do Triângulo Mineiro, a produção agropecuária cresce e aumentam os fluxos de 
- Uma região extrai minério e outra abriga parte da transformação metalúrgica. Sobre a organ
- Qual associação entre processo histórico e transformação territorial mineira está correta?
- Uma alternativa descreve a relação entre os setores produtivos e as regiões de Minas sem t
- A BR-050 e a BR-262 são citadas em um estudo de integração regional. Qual leitura geográfi
- Em um estudo sobre desigualdade entre municípios mineiros, qual procedimento melhora a int
- Um pesquisador estuda diferenças de idade e sexo entre centros urbanos e localidades rurai
- Uma série recente mantém dados por mesorregião, enquanto um mapa atual apresenta regiões i
- Uma localidade perde jovens adultos para outros municípios, mas seu total de habitantes ai
- Dois núcleos de municípios distintos formam uma mancha urbana contínua, mas o estudo não i
- Uma tabela cruza cor ou raça, grupos de idade e sexo. Qual interpretação atende ao cuidado
- Uma área registra crescimento do agronegócio e aumento do emprego em serviços logísticos, 
- Uma área possui usinas geradoras de eletricidade, mas domicílios rurais relatam interrupçõ
- Dois municípios exibem a mesma renda média, mas um apresenta concentração maior dos rendim
- Um plano estadual usa uma regionalização própria, enquanto o estudo do IBGE emprega regiõe
- Um mapa mostra deslocamentos diários para um centro de serviços, enquanto a mancha edifica
- A população estadual aumenta, embora a taxa anual de crescimento diminua e algumas áreas r
- Em uma zona agrícola, a mecanização eleva a produtividade, reduz postos no campo e coincid
- Após a implantação de um corredor rodoviário e de melhor conectividade digital, instala-se
- Um município apresenta PIB por habitante elevado, mas resultados modestos de rendimento fa
- Uma tabela registra totais distintos de pessoas por sexo e cor ou raça entre municípios de
- No quadro regional antigo do IBGE, mesorregiões e microrregiões agrupavam municípios e não
- A divisão de 2017 do IBGE converteu as regiões intermediárias em unidades político-adminis
- A região imediata considera relações cotidianas com centros urbanos próximos e acesso a be
- A extração mineral, a agropecuária e os serviços podem participar de uma mesma cadeia regi
- As cinco categorias de cor ou raça utilizadas pelo IBGE esgotam todas as dimensões de pert
- O crescimento da população de um município depende apenas de nascimentos e mortes, pois a 
- O deslocamento diário de trabalhadores entre municípios comprova, por si só, que suas manc
- O PIB por habitante informa diretamente como os rendimentos estão repartidos entre as famí
- Uma região metropolitana legal pode incluir municípios limítrofes sem que toda a área edif
- A modernização agropecuária pode ampliar a produtividade sem gerar aumento proporcional do
- Se o total estadual cresce, nenhuma região ou município pode ter perdido moradores no mesm
- A permanência de uma mesorregião em uma base recente significa que essa área é equivalente
- Uma descrição regional que associa agricultura, mineração ou indústria a uma área deve ser
- O aumento da produção regional, isoladamente, demonstra que a desigualdade de renda diminu
- A geração de eletricidade, a transmissão e a regularidade do fornecimento aos domicílios s
```

## Pedido 3: História e Geografia do Paraná (`parana.conhecimentos-estaduais`, 13 tópicos de edital)

```
Matéria: parana.conhecimentos-estaduais (História e Geografia do Paraná)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ocupação indígena, reduções e período colonial": 20
- "2. Tropeirismo, emancipação, erva-mate e Federalista": 20
- "3. Contestado, imigração, café e ocupação do oeste": 20
- "4. Relevo, clima, vegetação e rios": 20
- "5. População, economia, infraestrutura e cultura": 20

Já existem (não repita):
- O núcleo urbano mais antigo do atual Paraná, elevado a vila em 1648 em função da busca por
- Curitiba foi elevada à condição de vila antes de Paranaguá.
- As reduções jesuíticas do Guairá, no território do atual Paraná, foram destruídas no sécul
- Os kaingang e os xokleng, que habitavam os planaltos paranaenses, pertencem ao tronco ling
- O ouro encontrado no Paraná no século XVII foi abundante e duradouro, rivalizando com a pr
- Sobre o tropeirismo no território paranaense, assinale a alternativa correta.
- Lapa, Ponta Grossa e Castro estão entre as cidades paranaenses cuja origem se liga aos pou
- Antes de se tornar província autônoma, em 1853, o território do Paraná integrava a provínc
- A Província do Paraná foi instalada em 19 de dezembro de 1853, data celebrada como a de su
- Considere as afirmações sobre a emancipação do Paraná. I. A lei imperial que criou a proví
- Na segunda metade do século XIX, o principal produto da economia paranaense, exportado sob
- A ferrovia Paranaguá–Curitiba, inaugurada em 1885, atravessa a Serra do Mar e foi importan
- O episódio conhecido como Cerco da Lapa, ocorrido em 1894 no Paraná, está ligado a qual co
- O Barão do Serro Azul, ervateiro executado em 1894, havia sido acusado por ter negociado c
- A Guerra do Contestado (1912–1916) ocorreu em área disputada entre Paraná e Santa Catarina
- O acordo de limites de 1916, que encerrou a questão do Contestado, deixou a maior parte da
- Sobre a imigração no Paraná, assinale a alternativa correta.
- As cidades de Londrina e Maringá surgiram de loteamentos planejados no Norte Novo pela Com
- Qual acontecimento, em 1975, destruiu grande parte dos cafezais paranaenses e acelerou a s
- O Território Federal do Iguaçu, criado no governo Vargas com áreas do oeste do Paraná e de
- A Revolta dos Posseiros, de 1957, ocorreu em qual região do Paraná?
- A usina de Itaipu, no rio Paraná, é um empreendimento binacional do Brasil com a Argentina
- O Paraná faz limite com quais países estrangeiros?
- Os estados brasileiros que fazem limite com o Paraná são São Paulo, Mato Grosso do Sul e S
- Assinale a sequência correta das unidades de relevo do Paraná, do litoral para o interior.
- A Escarpa Devoniana marca a transição entre o Primeiro Planalto, onde está Curitiba, e o S
- O Terceiro Planalto paranaense caracteriza-se por:
- O ponto culminante do Paraná, que também é o mais alto da Região Sul, é o Pico Paraná, loc
- A vegetação símbolo do Paraná, típica dos planaltos do centro-sul e hoje bastante reduzida
- Os maiores remanescentes de Mata Atlântica do Paraná concentram-se na Serra do Mar e no li
- Sobre a hidrografia paranaense, é correto afirmar que:
- As Cataratas do Iguaçu ficam na divisa entre o Brasil e o Paraguai, no rio Paraná.
- O clima predominante no Paraná, com geadas frequentes nas áreas mais altas do centro e do 
- O Trópico de Capricórnio atravessa o norte do território paranaense.
- No setor agropecuário, o Paraná destaca-se nacionalmente por liderar a produção e a export
- O Porto de Paranaguá é um dos maiores do Brasil na exportação de grãos e farelos.
- Associe corretamente o elemento à sua localização ou característica no Paraná. I. Polo aut
- Foz do Iguaçu está na tríplice fronteira entre Brasil, Argentina e Paraguai.
- Curitiba tornou-se referência em planejamento urbano. Um dos elementos mais associados a e
- O pinhão, semente da araucária, e o fandango caiçara do litoral são elementos da cultura p
- Considere os ciclos econômicos do Paraná e assinale a ordem cronológica correta.
```

## Pedido 4: Controle de produtos químicos e infrações de repercussão interestadual (`legislacao.produtos-quimicos-repercussao-interestadual`, 13 tópicos de edital)

```
Matéria: legislacao.produtos-quimicos-repercussao-interestadual (Controle de produtos químicos e infrações de repercussão interestadual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei nº 10.357/2001: finalidade, alcance e atividades controladas": 21
- "2. Cadastro, licença, autorização e deveres de comunicação": 21
- "3. Infrações, sanções e processo administrativo de produtos químicos": 21
- "4. Lei nº 10.446/2002: pressupostos da investigação federal e hipóteses legais": 27
- "5. Integração das duas leis, fiscalização e casos difíceis": 10

Já existem (não repita):
- Uma indústria utiliza produto químico regularmente em atividade lícita, mas a formulação e
- Uma empresa questiona quem pode definir e atualizar, por ato normativo, a relação de produ
- Uma pessoa jurídica pretende trabalhar continuamente com produto controlado e afirma possu
- Uma empresa identifica indícios concretos de possível desvio de produto químico controlado
- Durante auditoria, solicita-se o período de guarda dos documentos que fundamentam as infor
- Em fiscalização de produtos químicos controlados, constata-se operação sem licença, e a au
- Um estabelecimento flagrado com irregularidade pretende saná-la após a fiscalização. De ac
- Um caso apresenta ação coordenada que excede fronteiras estaduais e demanda repressão unif
- Uma associação criminosa com atuação em mais de um estado comete roubos contra caixas elet
- Uma investigação envolve furto contra empresa de segurança privada especializada em transp
- Uma empresa possui licença de funcionamento válida, mas vai importar insumo controlado em 
- Uma pessoa jurídica habilitada mantém produtos controlados em estoque, mas não registra op
- Uma empresa perde produto químico controlado por furto e deixa transcorrer o prazo de comu
- Uma empresa comprova estar isenta da taxa pelo art. 18 da Lei nº 10.357/2001. Qual consequ
- No processo de fiscalização, um produto apreendido apresenta risco iminente à saúde públic
- Dois agentes subtraem carga em transporte interestadual, havendo indícios de grupo organiz
- Investiga-se falsificação de produto destinado a fins medicinais e venda da mercadoria adu
- Uma investigação versa sobre crimes praticados na internet que propagam ódio ou aversão às
- Uma autoridade propõe investigação federal de infração penal não nominada nos incisos I a 
- Uma empresa é autuada por omitir informação periódica e regulariza o mapa de controle dent
- Uma pessoa física possui apenas cadastro e pretende realizar operação pontual com produto 
- Uma microempresa pleiteia emissão de CLF e redução da taxa prevista no art. 19 da Lei nº 1
- Em 2026, um procedimento interno utiliza as IN DG/PF nº 166/2020 e nº 211/2021 como únicas
- Um furto de carga em transporte interestadual ocorre por ação de indivíduo isolado, sem qu
- Determinada infração penal não aparece nos oito incisos da Lei nº 10.446/2002, mas possui 
- Uma empresa que apenas transporta produto químico controlado pode estar abrangida pelo art
- O art. 3º da Lei nº 10.357/2001 atribui exclusivamente ao particular interessado a fiscali
- O Certificado de Registro Cadastral e a Autorização Prévia têm funções distintas: o primei
- Uma pessoa jurídica que exerça atividade regular com produtos controlados pode deixar de r
- No art. 1º da Lei nº 10.446/2002, a possibilidade de investigação pela Polícia Federal não
- Uma empresa que suspeita de desvio de produto controlado pode aguardar até 48 horas para c
- Uma pessoa jurídica que presta informações periódicas sobre movimentação de produtos quími
- Ao constatar falta de licença, a fiscalização fica impedida de aplicar multa se já tiver d
- O art. 15 da Lei nº 10.357/2001 prevê trinta dias para sanar irregularidades identificadas
- Na Lei nº 10.446/2002, o art. 1º, VIII, refere-se especificamente a furto, roubo ou dano c
- A isenção da taxa prevista no art. 18 da Lei nº 10.357/2001 não afasta, por si, os demais 
- Uma filial de empresa cadastrada obtém, pelo art. 19, parágrafo único, redução de setenta 
- Quando houver risco iminente à saúde pública ou ao meio ambiente, o art. 15, § 3º, admite 
- A atualização da IN DG/PF nº 338/2026 revogou a própria Lei nº 10.357/2001 e substituiu as
- Na investigação prevista pela Lei nº 10.446/2002, a repercussão interestadual ou internaci
- O art. 1º, IV, da Lei nº 10.446/2002, na redação de 2024, exige apenas que uma carga trans
- Crimes praticados por meio da rede mundial de computadores que difundam conteúdo misógino 
- Em qualquer hipótese já enumerada nos incisos I a VIII da Lei nº 10.446/2002, a investigaç
- A pessoa jurídica regularmente licenciada pode cometer a infração administrativa do art. 1
- A possibilidade de investigação federal segundo a Lei nº 10.446/2002 determina, automatica
```

## Pedido 5: Barroco e Arcadismo (`literatura.barroco-arcadismo`, 13 tópicos de edital)

```
Matéria: literatura.barroco-arcadismo (Barroco e Arcadismo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. O universo barroco: história, conflitos e linguagem": 19
- "2. Barroco luso-brasileiro e Barroco mineiro: obras em leitura": 26
- "3. Arcadismo: Iluminismo, convenções pastorais e poesia neoclássica": 19
- "4. Arcadismo brasileiro: lírica, sátira e epopeia": 18
- "5. Comparações avançadas: Minas, Inconfidência e permanências estéticas": 18

Já existem (não repita):
- Em um poema didático, a voz afirma: 'A cidade celebra enquanto meu coração lamenta'. O con
- Um quadro cronológico adota 1601 como referência didática do Barroco no Brasil. Qual publi
- Uma seleção atribuída a Gregório de Matos apresenta poemas religiosos, amorosos e sátiras 
- Ao estudar um sermão, a leitora observa comparações, perguntas e distinções para demonstra
- Em uma aula sobre paisagem colonial, destaca-se o poema À Ilha da Maré, inserido em Música
- Um texto de feição árcade associa a felicidade a um sítio tranquilo, à vida moderada e à a
- Um catálogo informa que o marco convencional do Arcadismo brasileiro é a publicação de Obr
- Uma pastora repousa junto à fonte, entre árvores e brisa amena, num cenário inventado como
- Uma intérprete afirma que Dirceu, ao dirigir-se a Marília, deve ser lido como uma voz cons
- Dois estudantes confundem O Uraguai com Caramuru. Qual associação corrige adequadamente au
- Uma visita a igrejas de Minas Gerais observa talha, escultura e pintura intensamente ornam
- Uma síntese histórica aproxima Cláudio Manuel da Costa e Gonzaga da Inconfidência Mineira.
- Em um verso autoral, a voz afirma: 'A manhã termina; aproveitemos a alegria que ela oferec
- Um poema barroco constrói sucessivas imagens de luz e sombra, enquanto o raciocínio de um 
- Um pesquisador encontra poema de sátira colonial atribuído a Gregório de Matos em cópia ta
- Um comentário observa que Cláudio Manuel da Costa usa pastores e ideais clássicos, mas pre
- Uma obra em cartas ficcionais, por meio de uma cidade imaginária e personagens mascarados,
- Em uma ficha bibliográfica de O Uraguai, deseja-se registrar uma combinação correta de for
- Uma edição de Caramuru menciona Diogo Álvares, Paraguaçu e Moema, recorrendo à tradição ép
- Um sermão trabalha a metáfora do semeador e organiza uma série de distinções para discutir
- Uma crítica conclui que a celebração das frutas e da paisagem baiana em À Ilha da Maré con
- Um exercício contrapõe a máscara Critilo e a persona Dirceu, presentes em textos tradicion
- Um leitor afirma que, por ter escrito Obras em 1768, Cláudio Manuel da Costa já denunciava
- Em material ilustrado, uma escultura de Aleijadinho e um poema de Cláudio Manuel da Costa 
- Na leitura comparativa de um soneto amoroso barroco e de uma lira árcade, ambos mencionam 
- Ao organizar uma antologia de Gregório de Matos, a pesquisadora inclui poesia religiosa e 
- Ao identificar o autor de Caramuru, a leitora deve registrar Padre Antônio Vieira, porque 
- Uma paisagem ideal de fonte cristalina, sombra fresca e pastores pode expressar o locus am
- Quando uma voz lírica recomenda aproveitar o presente antes que ele passe, a expressão lat
- Em uma linha do tempo escolar, a publicação de Obras, de Cláudio Manuel da Costa, em 1768 
- Em fichas de leitura, a morte de Moema deve ser associada a O Uraguai, enquanto os persona
- Uma composição colonial que elogia elementos naturais do território pode ser nativista sem
- Uma cópia manuscrita de um poema satírico identificada como obra de Gregório de Matos disp
- A presença de templos com decoração barroca em Minas Gerais, no século XVIII, é historicam
- Na análise do Sermão da Sexagésima, os encadeamentos de causas e efeitos devem ser ignorad
- Em um sermão de Vieira, a comparação entre semear e pregar pode desempenhar função argumen
- Uma apostila pode concluir que todos os autores árcades brasileiros eram participantes da 
- Na leitura de Cláudio Manuel da Costa, é possível identificar convenções árcades e também 
- Um catálogo descreve O Uraguai, de Basílio da Gama, como uma epopeia de dez cantos em oita
- Em duas leituras de epopeias árcades, a crítica à atuação jesuítica em O Uraguai e a valor
- As Cartas Chilenas constituem a seção de lírica amorosa de Marília de Dirceu, pois Critilo
- O fato de Obras, de Cláudio Manuel da Costa, ter sido publicado em 1768 impede que o livro
- A descrição de frutas e paisagens de À Ilha da Maré obriga a classificá-lo como composição
- Um poema cujo eu lírico usa o nome Dirceu pode ser examinado como criação estética, sem pr
- Diante de dois poemas sobre a fugacidade da vida, a presença desse tema comum basta para a
```

## Pedido 6: Toxicologia e psiquiatria forense: tópicos especiais (`medicina-legal.toxicologia-psiquiatria-forense-especial`, 13 tópicos de edital)

```
Matéria: medicina-legal.toxicologia-psiquiatria-forense-especial (Toxicologia e psiquiatria forense: tópicos especiais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios das ciências forenses e neurociências": 17
- "2. Controle de dopagem": 17
- "3. Entomotoxicologia": 17
- "4. Praguicidas, gases tóxicos e metais": 17
- "5. Internações psiquiátricas e psicopatia": 16
- "6. Psiquiatria e cultura": 16

Já existem (não repita):
- O princípio segundo o qual todo contato deixa um vestígio, base da busca de evidências nas
- Um resultado positivo em teste de triagem por imunoensaio é suficiente para confirmar, com
- A cocaína produz seus efeitos estimulantes e de recompensa principalmente por:
- A associação entre álcool e benzodiazepínicos aumenta o risco de depressão respiratória po
- A necessidade de doses progressivamente maiores de uma substância para obter o mesmo efeit
- Entre os avanços recentes da toxicologia forense está o uso de matrizes alternativas, como
- O órgão internacional responsável pelo Código Mundial Antidopagem e pela lista anual de su
- No controle de dopagem, a amostra B destina-se a:
- O passaporte biológico do atleta permite identificar dopagem de forma indireta, pelo acomp
- O instrumento que permite ao atleta utilizar substância da lista proibida por necessidade 
- Diuréticos constam da lista proibida também como agentes mascarantes, por poderem diluir a
- Para a inclusão de uma substância na lista proibida da WADA, exige-se, entre os critérios 
- A entomotoxicologia é especialmente útil quando:
- Drogas presentes no cadáver podem acelerar ou retardar o desenvolvimento das larvas de mos
- As concentrações de drogas encontradas em larvas necrófagas correlacionam-se fielmente com
- Na coleta de insetos para estudos de entomologia e entomotoxicologia forense, recomenda-se
- Organofosforados e carbamatos causam intoxicação por:
- O herbicida cuja intoxicação se caracteriza por lesões cáusticas e fibrose pulmonar progre
- Na intoxicação por organofosforados, é achado típico a midríase acentuada, com pele seca e
- No tratamento da intoxicação por organofosforados, utiliza-se:
- Na intoxicação por monóxido de carbono, os livores cadavéricos e as vísceras tendem a apre
- O cianeto exerce sua toxicidade principalmente por:
- O pontilhado basófilo nas hemácias e a linha gengival de Burton são achados clássicos da i
- O arsênio deposita-se em cabelos e unhas, o que permite sua detecção muito tempo após a ex
- O produto clandestino conhecido no Brasil como "chumbinho", associado a intoxicações e hom
- Segundo a Lei nº 10.216/2001, a internação psiquiátrica que se dá sem o consentimento do p
- A intoxicação crônica por arsênio pode deixar nas unhas faixas brancas transversais conhec
- A internação psiquiátrica involuntária deve ser comunicada ao Ministério Público estadual,
- Pela Lei nº 10.216/2001, a internação em qualquer de suas modalidades só será indicada qua
- A internação compulsória prevista na Lei nº 10.216/2001 é aquela solicitada pela família q
- A escala PCL-R (Psychopathy Checklist-Revised), amplamente usada na avaliação da psicopati
- A psicopatia constitui diagnóstico formal e autônomo do DSM-5, distinto e sem relação com 
- Quanto à imputabilidade penal do psicopata, a posição predominante na psiquiatria forense 
- Entre os traços afetivo-interpessoais da psicopatia descritos na literatura estão o charme
- No DSM-5, a antiga categoria das "síndromes ligadas à cultura" foi substituída pela noção 
- Uma experiência de transe vivida no contexto de um ritual religioso, compartilhada pelo gr
- O quadro descrito no Sudeste Asiático, caracterizado por episódio súbito de fúria e violên
- Diferenças de língua, classe social, raça e religião entre perito e periciando aumentam o 
- A Entrevista de Formulação Cultural, proposta no DSM-5, tem como objetivo principal:
- Na análise de mortes em incêndio, a presença de carboxiemoglobina elevada no sangue indica
```

## Pedido 7: Regimento Interno do Tribunal de Justiça do Rio Grande do Norte (`rio-grande-do-norte.regimento-interno-tjrn`, 13 tópicos de edital)

```
Matéria: rio-grande-do-norte.regimento-interno-tjrn (Regimento Interno do Tribunal de Justiça do Rio Grande do Norte)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organização, Pleno, Seção Cível, Câmaras e plantão": 25
- "2. Direção, Conselho, Corregedoria e eleições": 25
- "3. Desembargadores: posse, férias, licenças e substituições": 25
- "4. Registro, preparo, distribuição e sessões": 25

Já existem (não repita):
- Segundo o Regimento Interno do TJRN, ao Tribunal de Justiça e às suas Câmaras é devido o t
- De acordo com o art. 4º do Regimento Interno do TJRN, NÃO figura entre os órgãos do Tribun
- Pelo Regimento Interno do TJRN, o número de Desembargadores só pode ser alterado por propo
- Os mandatos de Presidente, Vice-Presidente, Corregedor-Geral de Justiça, Ouvidor e Diretor
- O Tribunal Pleno do TJRN funciona com a presença mínima de quantos Desembargadores, inclus
- Os Juízes de Direito convocados integram o quórum do Tribunal Pleno em todas as matérias, 
- Compete privativamente ao Tribunal Pleno do TJRN processar e julgar originariamente:
- Cabe ao Tribunal Pleno do TJRN aplicar a pena de demissão a servidor do Poder Judiciário e
- As medidas liminares em mandado de segurança contra ato judicial do Presidente, do Correge
- Sobre a Seção Cível do TJRN, assinale a alternativa correta.
- Pelo Regimento Interno do TJRN, as Câmaras Cíveis e a Câmara Criminal são compostas, respe
- Compete às Câmaras Cíveis do TJRN julgar os habeas corpus decorrentes de prisão civil e os
- O Presidente do TJRN integra a escala mensal do plantão jurisdicional de segundo grau, em 
- Verificando que o feito encaminhado ao plantão não tem caráter de urgência, o Desembargado
- Segundo o art. 28 do Regimento Interno do TJRN, no Tribunal Pleno o Presidente:
- O Presidente do TJRN deve apresentar ao Tribunal Pleno, na primeira reunião de fevereiro l
- Compete ao Vice-Presidente do TJRN, entre outras atribuições:
- As reclamações contra irregularidade na distribuição, enquanto os autos não estiverem conc
- Integra o Conselho da Magistratura do TJRN, além do Presidente, do Vice-Presidente e do Co
- Os Juízes Corregedores do TJRN podem ser de qualquer entrância e servir por tempo indeterm
- O Corregedor-Geral de Justiça deve realizar correição ordinária, geral ou parcial, nos ser
- Segundo o Regimento Interno do TJRN, a Ouvidoria de Justiça NÃO admite:
- O Diretor da Revista do TJRN é eleito por ocasião da eleição do Presidente, com mandato co
- A eleição do Presidente, do Vice-Presidente e do Corregedor-Geral do TJRN ocorre em sessão
- Na eleição dos dirigentes do TJRN, se nenhum Desembargador obtiver a maioria dos votos, fa
- Após a eleição, os dirigentes em exercício devem entregar aos eleitos relatório circunstan
- Em caso de vacância do cargo de Presidente do TJRN:
- Recebida a lista sêxtupla da classe dos Advogados ou do Ministério Público, o Pleno do TJR
- Na vacância definitiva do cargo de Desembargador, o Regimento do TJRN determina a convocaç
- Segundo o Regimento do TJRN, a antiguidade dos novos membros do Tribunal, para efeito inte
- É vedado o afastamento simultâneo, por férias, de mais de cinco Desembargadores no Pleno e
- O Presidente, o Vice-Presidente e o Corregedor-Geral do TJRN gozam de férias individuais d
- O recesso do Tribunal de Justiça e dos Juízos de primeira instância do RN, com suspensão d
- A licença para tratamento de saúde de Desembargador por prazo superior a trinta dias depen
- Nas férias, licenças ou afastamentos de Desembargador, a substituição automática por outro
- Os Juízes de primeiro grau convocados para substituição ou auxílio no TJRN recebem exclusi
- Faltando número na Câmara Criminal por impedimento ou suspeição, seus membros são substitu
- O recorrente que não comprovar o preparo no ato de interposição do recurso será intimado, 
- Quando ambas as partes recorrem, inclusive adesivamente, cada recurso está sujeito a prepa
- A distribuição no TJRN é feita por processamento eletrônico de dados, mediante sorteio ale
- NÃO concorre à distribuição no âmbito de sua Câmara o Desembargador:
- A ação rescisória pode ser distribuída ao Desembargador que atuou como vogal no julgamento
- Pela redação da Emenda Regimental 36/2023, as sessões destinadas à eleição e à posse do Pr
- Segundo o Regimento do TJRN, o Tribunal Pleno, as Câmaras Cíveis e a Câmara Criminal reali
- As decisões administrativas dos órgãos do TJRN devem ser motivadas, e as disciplinares são
- A sessão totalmente virtual do TJRN (Julgamento Virtual) tem início e encerramento, como r
- No Julgamento Virtual do TJRN, o membro do quórum que não se manifestar no prazo terá o ju
- Pelo art. 165-B do Regimento do TJRN, NÃO será incluído no Julgamento Virtual, ou dele ser
```

## Pedido 8: Lei de Divisão e Organização Judiciárias do Rio Grande do Norte (LC 643/2018) (`rio-grande-do-norte.organizacao-judiciaria-lc-643`, 13 tópicos de edital)

```
Matéria: rio-grande-do-norte.organizacao-judiciaria-lc-643 (Lei de Divisão e Organização Judiciárias do Rio Grande do Norte (LC 643/2018))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições gerais e divisão judiciária": 25
- "2. Tribunal de Justiça, Juizados e Turmas Recursais": 25
- "3. Juízes, substituições, Justiça de Paz e magistratura": 25
- "4. Serviços auxiliares, feriados e serviços extrajudiciais": 25

Já existem (não repita):
- Pela LC 643/2018, os magistrados devem, nos casos concretos, negar aplicação às leis que e
- Para fins de administração do Poder Judiciário, a LC 643/2018 divide o Estado do Rio Grand
- A criação de um novo município no Rio Grande do Norte implica, automaticamente, a criação 
- Entre os requisitos cumulativos para a criação de comarca previstos no art. 10 da LC 643/2
- Para a criação de comarca, o município que será termo-sede deve estar a uma distância da c
- Para a elevação de comarca da entrância intermediária para a final, a LC 643/2018 exige, e
- Segundo o art. 14 da LC 643/2018, em cada mesorregião geográfica do Estado pode existir:
- As comarcas de entrância inicial do Rio Grande do Norte têm apenas uma unidade judiciária,
- A reclassificação, a agregação e a desagregação de comarcas, bem como a redefinição de com
- A comarca agregada que não alcançar os requisitos mínimos de criação um ano após a efetiva
- Segundo o art. 23 da LC 643/2018, o Tribunal de Justiça do Rio Grande do Norte é composto 
- O Presidente, o Vice-Presidente e o Corregedor-Geral de Justiça do TJRN são eleitos em vot
- Ocorrendo vaga no cargo de Presidente do TJRN no curso do biênio, faltando menos de três m
- O Tribunal Pleno do TJRN funciona, nas sessões jurisdicionais, com a presença mínima de:
- Em caso de vacância, férias ou afastamento de Desembargador por prazo superior a 30 dias, 
- Segundo o art. 31 da LC 643/2018, compete ao TJRN processar e julgar originariamente, nas 
- O Conselho da Magistratura do TJRN é constituído:
- A Corregedoria Geral de Justiça pode ter como auxiliares juízes de entrância final, na pro
- NÃO integra o Sistema dos Juizados Especiais do Rio Grande do Norte, segundo o art. 39 da 
- Cada Turma Recursal do RN é composta por três juízes de direito de entrância final e també
- Nas comarcas em que não exista unidade de Juizado Especial, os feitos das Leis 9.099/1995 
- A reclassificação de entrância da comarca altera a situação funcional e remuneratória do J
- Nas comarcas com mais de um Juiz de Direito, a direção do foro é exercida:
- NÃO é atribuição do Juiz Diretor do Foro, segundo o art. 61 da LC 643/2018:
- Segundo a LC 643/2018, os Juízes de Paz substituem os Juízes de Direito apenas para a cele
- A Justiça de Paz do Rio Grande do Norte é composta por cidadãos eleitos pelo voto direto, 
- O Juiz de Paz eleito e diplomado toma posse em até 30 dias após a diplomação, perante o Pr
- Configura abandono das funções, causa de perda do mandato do Juiz de Paz, a ausência injus
- O ingresso na magistratura do RN dá-se no cargo de Juiz de Direito Substituto, por concurs
- Os magistrados do RN tomam posse e entram em exercício no prazo de 30 dias da publicação d
- Para efeito de promoção, remoção e acesso, a antiguidade dos juízes é o tempo de efetivo e
- A lista de antiguidade dos Juízes de Direito e Juízes Substitutos, aprovada pelo Tribunal,
- O subsídio mensal dos Desembargadores do TJRN corresponde, segundo a LC 643/2018, a:
- Os magistrados do RN têm direito a férias anuais de 60 dias, com acréscimo de um terço, e 
- A licença compensatória do magistrado pelo exercício de plantão ou pela realização de audi
- Os servidores do Poder Judiciário do Rio Grande do Norte, salvo disposição especial, são r
- Segundo o art. 97 da LC 643/2018, além do período de 20 de dezembro a 6 de janeiro e dos f
- No feriado forense de 20 de dezembro a 6 de janeiro, o plantão judiciário para medidas urg
- A delegação dos serviços notariais e de registro no Rio Grande do Norte é concedida pelo T
- Pela redação dada pela LC 680/2021, a comissão do concurso para notários e registradores n
- No processo administrativo contra notário ou registrador, as penas de repreensão, multa e 
- Na Comarca de Natal, o exercício das atividades extrajudiciais é distribuído entre nove Of
- A Unidade Judiciária de Delitos de Organizações Criminosas, criada pela LC 643/2018 na Com
```

## Pedido 9: Serviço social no sociojurídico: família, adoção e convivência (`servico-social.servico-social-sociojuridico-familia`, 12 tópicos de edital)

```
Matéria: servico-social.servico-social-sociojuridico-familia (Serviço social no sociojurídico: família, adoção e convivência)
Capítulos e quantas questões quero em cada um (total 100):
- "Proteção integral, convivência familiar e atuação sociojurídica": 20
- "Acolhimento, reintegração, apadrinhamento e Sistema Nacional": 20
- "Adoção, entrega voluntária, direito às origens e cooperação internacional": 20
- "Guarda, tutela, poder familiar e prática em varas de família": 20
- "Escuta especializada, depoimento especial e alienação parental": 20

Já existem (não repita):
- Em demanda na Vara da Infância, a única dificuldade constatada é a ausência de renda estáv
- Na classificação do ECA, qual situação exemplifica especificamente a família extensa ou am
- Um magistrado solicita a um assistente social que indique, sem escuta da rede ou análise d
- Sobre a prioridade absoluta prevista no ECA e na Constituição, assinale a alternativa adeq
- Um concurso questiona o PNCFC instituído em dezembro de 2025. Qual é a afirmação correta?
- Uma criança necessita de afastamento temporário e há serviço adequado de famílias acolhedo
- De acordo com o art. 19 do ECA, qual é o intervalo máximo para reavaliação judicial da sit
- No planejamento de saída do acolhimento, qual medida representa reintegração familiar qual
- Em relação ao Sistema Nacional de Adoção e Acolhimento, assinale a opção correta.
- Ao examinar o limite de 18 meses estabelecido no art. 19, § 2º, do ECA, qual interpretação
- Assinale a proposição correta sobre os efeitos jurídicos essenciais da adoção de criança o
- Conforme o art. 48 do ECA, como se disciplina o acesso do adotado ao processo de adoção?
- Uma adolescente de 14 anos está em processo de colocação em família substituta. Qual medid
- Uma gestante pede informações sobre entrega voluntária do bebê para adoção. Qual encaminha
- A respeito das duas convenções internacionais sobre adoção citadas em editais, qual corres
- No ECA, quais são as três modalidades de colocação em família substituta?
- Em litígio familiar, um profissional afirma que a guarda unilateral extingue, por si só, t
- Durante processo de suspensão do poder familiar, qual órgão tem competência para decidir j
- Após separação, dois responsáveis alegam que guarda compartilhada exige residência alterna
- Em decisão liminar relacionada à suspensão do poder familiar, qual cautela foi enfatizada 
- Uma criança revela violência durante atendimento no CREAS. A equipe entrevista apenas quan
- Qual é a característica definidora do depoimento especial previsto na Lei nº 13.431/2017?
- Uma equipe cogita colher cinco depoimentos idênticos da mesma criança, apenas para testar 
- Após a Lei nº 14.340/2022, qual afirmação sobre a Lei da Alienação Parental é correta?
- Em processo com alegação de alienação parental e notícia consistente de violência doméstic
- A prioridade absoluta conferida a crianças e adolescentes autoriza o Poder Judiciário a de
- No ECA, o conceito de família extensa demanda parentes próximos com quem a criança ou adol
- A falta de renda de um dos responsáveis, desacompanhada de outros fatores, é fundamento su
- O Plano Nacional de Convivência Familiar e Comunitária 2025–2035 possui caráter orientador
- A família acolhedora torna-se, ao receber a criança, sua família adotiva definitiva, indep
- A reavaliação judicial da situação de uma criança em acolhimento familiar deve ocorrer, no
- O prazo ordinário de 18 meses citado no art. 19, § 2º, do ECA refere-se à permanência inst
- O apadrinhamento de criança acolhida é modalidade que, por si só, constitui filiação adoti
- A adoção, segundo o ECA, é excepcional e irrevogável e confere ao adotado condição de filh
- O direito do adotado de conhecer suas origens biológicas implica desconstituição automátic
- No ECA, o adotado menor de 18 anos pode solicitar acesso ao processo de adoção, sendo poss
- A Convenção Interamericana promulgada pelo Decreto nº 2.429/1997 corresponde ao mesmo inst
- Guarda, tutela e adoção são as três modalidades de colocação em família substituta previst
- A atribuição de guarda unilateral a um genitor implica, automaticamente, destituição judic
- O Conselho Tutelar pode decretar a destituição do poder familiar quando constatar ausência
- A preferência pela entrevista da criança por equipe multidisciplinar e pela oitiva da outr
- Escuta especializada ocorre perante órgão da rede de proteção e limita o relato ao estrita
- Segundo a Lei nº 13.431/2017, depoimento especial é sinônimo de atendimento socioassistenc
- O art. 8º-A da Lei nº 12.318/2010, incluído em 2022, exige que a oitiva necessária de cria
- A simples formulação de alegação de alienação parental comprova que qualquer denúncia de a
```

## Pedido 10: Estatuto da Segurança Privada e das Instituições Financeiras (`legislacao.seguranca-privada`, 12 tópicos de edital)

```
Matéria: legislacao.seguranca-privada (Estatuto da Segurança Privada e das Instituições Financeiras)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regime jurídico e modalidades de segurança privada": 14
- "2. Prestadores autorizados, empresas especializadas e serviços orgânicos": 23
- "3. Profissionais: categorias, requisitos, formação, direitos e deveres": 12
- "4. Segurança de instituições financeiras e transporte de numerário": 23
- "5. Fiscalização, clandestinidade, sanções e regras do decreto de 2026": 28

Já existem (não repita):
- Uma empresa pretende contratar vigilância patrimonial para seus depósitos, mas o fornecedo
- Um grupo organiza cooperativa para oferecer vigilância patrimonial a terceiros, sustentand
- Um condomínio autorizado pretende organizar vigilantes próprios exclusivamente para proteg
- A autorização de funcionamento de empresa prestadora de vigilância patrimonial foi concedi
- Uma pessoa de vinte anos, com ensino médio completo, deseja trabalhar como vigilante patri
- Uma dependência financeira atende clientes no balcão e mantém numerário sob guarda. Qual e
- Durante transporte autorizado para suprir uma instituição financeira com numerário, utiliz
- Uma instituição financeira apresenta plano de segurança já aprovado. Para renová-lo, qual 
- Uma empresa de monitoramento eletrônico recebe autorização específica para operar sua cent
- Uma central eletrônica envia técnico externo ao local que gerou alarme. A supervisão quer 
- Um fornecedor de monitoramento argumenta que a venda isolada de câmeras, sem monitorar ala
- A empresa foi autorizada a atuar como prestadora especializada e começou suas atividades. 
- A empresa financeira quer usar caixas internos para levar numerário entre suas dependência
- Uma escola autorizada forma vigilantes e pretende começar a prestar escolta de valores com
- Uma agência financeira aprova seu plano de segurança e pretende exibir a planta de acesso 
- Uma empresa mantém serviço orgânico de vigilância, mas pretende vender patrulhamento aos e
- Um profissional concluiu a formação inicial como vigilante e quer executar nova modalidade
- Uma instituição financeira registra ocorrência criminosa nas instalações, enquadrada entre
- Um veículo transportador de valores, na hipótese geral da Lei 14.967/2024, pretende inicia
- Uma empresa autorizada apresenta pedido de renovação com documentação regular. Decorridos 
- Um prestador pretende transformar atividade de vigilância presencial em operação de empres
- Uma organização irregular oferece vigilância desarmada a terceiros sem autorização. Seu só
- Uma unidade financeira de cooperativa singular funciona em município com 18 mil habitantes
- Uma pessoa jurídica organiza prestação clandestina de segurança privada não autorizada e f
- A fiscalização identifica serviço sem autorização que utiliza arma de fogo, organizado e o
- Após a edição da Lei 14.967/2024, a antiga Lei 7.102/1983 deixou de ser a disciplina legal
- Um grupo de vigilantes certificados pode prestar serviços a terceiros como cooperativa ou 
- A Lei 14.967/2024 admite que um condomínio edilício constitua serviço orgânico de vigilânc
- Empresas de segurança patrimonial e empresas de monitoramento eletrônico estão sujeitas, p
- O requisito etário geral para vigilante e vigilante supervisor é de pelo menos vinte e um 
- A aprovação do plano de segurança de uma dependência financeira é facultativa quando o est
- No transporte de numerário destinado ao suprimento de instituições financeiras, a regra do
- O art. 26, V, permite ao técnico externo de monitoramento eletrônico realizar revistas pes
- A Lei 14.967/2024 prevê para vigilantes carga mínima de duzentas horas de formação e cinqu
- O prestador autorizado deve comunicar o início de suas atividades à secretaria de seguranç
- O art. 25, §5º, da Lei 14.967/2024 distingue serviços típicos de portaria, desarmados e se
- O plano de segurança de dependência financeira deve ser renovado anualmente, ao passo que 
- O art. 40, §5º, da Lei 14.967/2024 estabelece que pedido de renovação documentado deve ser
- Uma pessoa física que oferece segurança clandestina desarmada jamais se sujeita às medidas
- Nas agências e postos de cooperativas singulares de crédito situados em municípios de meno
- A autorização de funcionamento de empresa de monitoramento de sistemas eletrônicos deve se
- O vigilante supervisor deve ter concluído o ensino médio, além dos demais requisitos aplic
- O estatuto permite que empregados de instituição financeira executem transporte de numerár
- A Lei 14.967/2024, art. 46, prevê aos prestadores de segurança privada advertência, multa 
- A falta de autorização para prestar qualquer serviço desarmado de monitoramento preenche a
```

## Pedido 11: Anatomia patológica, histotecnologia e citopatologia (`medicina.anatomia-patologica-histopatologia`, 12 tópicos de edital)

```
Matéria: medicina.anatomia-patologica-histopatologia (Anatomia patológica, histotecnologia e citopatologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da patologia geral: lesão, inflamação e neoplasia": 17
- "Macroscopia, microscopia e elaboração do laudo anatomopatológico": 17
- "Histotecnologia, fixação, inclusão, microtomia e colorações": 17
- "Imuno-histoquímica, correlação morfológica e patologia molecular": 17
- "Citopatologia: coleta, preparo, critérios morfológicos e nomenclatura": 16
- "Necropsia, biossegurança e gestão da qualidade em patologia": 16

Já existem (não repita):
- Um paciente hipertenso apresenta aumento do volume dos cardiomiócitos sem multiplicação ce
- Após infarto isquêmico em um órgão sólido, observam-se contornos celulares preservados ini
- Ao microscópio, há retração celular, fragmentação ordenada e fagocitose de corpos delimita
- A formação de agregados organizados de macrófagos epitelioides, por vezes com células giga
- Em um corte de tecido corado por hematoxilina-eosina, qual alternativa descreve corretamen
- Qual fixador é amplamente empregado no processamento histológico rotineiro de tecidos huma
- Uma punção aspirativa por agulha fina obtém células para avaliação de lesão tireoidiana. O
- Uma lesão isquêmica cerebral antiga apresenta destruição do arcabouço e cavitação após dig
- Em uma ressecção oncológica orientada e marcada com tinta, células tumorais são encontrada
- Durante uma cirurgia, solicita-se resposta imediata sobre presença de tumor em margem sele
- No processamento histológico em parafina, qual sequência descreve corretamente as etapas a
- Um patologista deseja evidenciar membranas basais e auxiliar a pesquisa de fungos em tecid
- Em uma lâmina de imuno-histoquímica, células normais do próprio espécime deveriam expressa
- Em laudo citopatológico cervical, a categoria LSIL indica primariamente qual condição?
- Um serviço de patologia manipula fragmentos em formaldeído e utiliza lâminas de micrótomo.
- Biópsia brônquica de fumante mostra substituição do epitélio respiratório ciliado por epit
- Biópsia epitelial revela atipia acentuada em toda a espessura, membrana basal preservada e
- Uma biópsia por agulha de nódulo radiologicamente suspeito contém quase somente necrose e 
- Um painel de IHQ demonstra expressão focal de pancitoqueratina em neoplasia indiferenciada
- Após processamento de biópsia óssea com descalcificação prolongada, um biomarcador proteic
- Segundo a atualização brasileira de 2025 para rastreamento organizado do câncer do colo do
- Em necropsia clínica, o prontuário relata sepse secundária a pneumonia bacteriana, seguida
- Na hipertrofia, a principal alteração adaptativa é o aumento do volume de células preexist
- A apoptose sempre desencadeia inflamação purulenta intensa, com liberação maciça do conteú
- O tecido de granulação do processo de cicatrização é composto por novos vasos, fibroblasto
- Em um laudo oncológico, grau histológico e estádio clínico-patológico são expressões equiv
- O exame macroscópico de uma peça cirúrgica pode incluir dimensões, aspecto externo, descri
- Na coloração de rotina pela hematoxilina-eosina, a eosina é usada principalmente para cora
- A citopatologia por punção aspirativa pode avaliar células de uma lesão, mas frequentement
- A necrose liquefativa é frequente em abscessos, enquanto a coagulativa costuma preservar t
- A presença de tumor em uma margem cirúrgica permite afirmar, por si só, que o paciente pos
- A diafanização antecede a desidratação, pois os agentes clareadores removem inicialmente t
- O azul da Prússia pode ajudar a identificar depósitos de hemossiderina, enquanto o tricrôm
- Uma reação imuno-histoquímica negativa é conclusiva ainda que controles apropriados não te
- ASC-H, no contexto da citologia cervical, indica que não se pode excluir lesão intraepitel
- A observação de membrana basal íntegra nos níveis estudados de uma lesão epitelial com ati
- Um laudo negativo para malignidade em biópsia contendo exclusivamente detritos necróticos 
- Em biópsias para imunofluorescência direta, certos protocolos exigem amostra fresca ou mei
- A omissão do anticorpo primário em uma reação de imuno-histoquímica, por si só, prova inte
- Um resultado cervical classificado como HSIL confirma obrigatoriamente invasão estromal e 
- As diretrizes brasileiras aprovadas em 2025 contemplam o DNA-HPV oncogênico como teste pri
- Em um laboratório de patologia, recipientes de formaldeído usado e lâminas quebradas podem
```

## Pedido 12: Geometria espacial: áreas de sólidos (`matematica.geometria-espacial-areas`, 12 tópicos de edital)

```
Matéria: matematica.geometria-espacial-areas (Geometria espacial: áreas de sólidos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Superfície, planificação, cubo e paralelepípedo": 20
- "2. Prismas e pirâmides: áreas lateral e total": 20
- "3. Cilindro, cone e troncos": 20
- "4. Esfera, poliedros e semelhança": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- Um cubo tem aresta de 5 cm. Qual é a sua área total?
- Um paralelepípedo retângulo tem dimensões 2 cm, 3 cm e 4 cm. Calcule a sua área total.
- Um prisma reto de base quadrada tem aresta da base 3 cm e altura 10 cm. Qual é a área late
- A superfície lateral de um cilindro reto de raio 4 cm e altura 6 cm é cortada e aberta no 
- Uma bola esférica tem raio de 5 cm. Quanto mede a área da sua superfície?
- Um poliedro convexo tem 12 vértices e 30 arestas. Quantas faces ele tem?
- Um cone equilátero tem raio da base igual a 3 cm. Determine a sua área lateral.
- A aresta de um cubo B é o triplo da aresta de um cubo A. A área total de B é quantas vezes
- Um tetraedro regular tem aresta de 4 cm. Qual é a sua área total?
- Uma pirâmide quadrangular regular tem aresta da base 6 cm e altura 4 cm. Qual é a área tot
- Um cone reto tem raio da base 4 cm e geratriz 10 cm. Sua superfície lateral, planificada, 
- Um prisma hexagonal regular tem aresta da base 2 cm e altura 5 cm. Qual é a sua área total
- A diagonal de um cubo (ligando dois vértices opostos através do sólido) mede 6√3 cm. Qual 
- Numa esfera de raio 3 cm, considere o fuso esférico (gomo) cujo ângulo mede 60°. Qual é a 
- Um cilindro equilátero tem área total de 54π cm². Quanto mede a sua altura?
- Uma caixa-d'água cúbica sem tampa, de aresta 2 m, será pintada por fora em todas as faces 
- A soma das 12 arestas de um paralelepípedo retângulo é 48 cm e a sua diagonal mede √50 cm.
- Um tronco de cone reto tem raios das bases 2 cm e 5 cm e altura 4 cm. Calcule a área total
- Uma esfera de raio 10 cm é cortada por um plano a 6 cm do seu centro. Qual é a área da cal
- Três cubos de aresta 2 cm são colados em fila, face com face, formando um único bloco. Qua
- Um semicírculo de papel com raio 8 cm é enrolado, sem sobreposição, formando a lateral de 
- Uma pirâmide hexagonal regular tem aresta da base 4 cm e aresta lateral 5 cm. Qual é a áre
- Um poliedro convexo tem exatamente 8 faces triangulares e 6 faces quadrangulares. Quantos 
- Uma esfera de raio r está encaixada num cilindro reto de raio r e altura 2r, tocando as du
- Duas pirâmides semelhantes têm volumes na razão 8 : 27. A menor tem área lateral de 40 cm²
- A área total de um sólido é a soma das áreas de todas as faces ou superfícies que o limita
- Qualquer figura formada por seis quadrados iguais ligados pelos lados pode ser dobrada par
- Em um paralelepípedo retângulo de dimensões a, b e c, a área total é dada por 2(ab + ac + 
- A diagonal de um cubo de aresta a, que liga dois vértices opostos passando pelo interior d
- Planificada, a superfície lateral de um cone circular reto é um setor circular cujo raio é
- Uma meia esfera maciça (hemisfério) de raio r tem área total igual a 2πr².
- Em todo poliedro convexo, o número de vértices menos o número de arestas mais o número de 
- Um prisma triangular regular com aresta da base 6 cm e altura 10 cm tem área total igual a
- Em um cubo de aresta 4 cm, a área total é 96 cm² e a diagonal mede 4√3 cm.
- Se as três dimensões de um paralelepípedo retângulo forem todas duplicadas, a área total d
- A área lateral de um cone circular reto de raio 5 cm e altura 12 cm é igual a 65π cm².
- Uma pirâmide quadrangular regular com aresta da base 8 cm e altura 3 cm tem apótema (altur
- Um tronco de cone com raios das bases 3 cm e 6 cm e geratriz 5 cm tem área lateral de 45π 
- O octaedro regular de aresta a, formado por oito triângulos equiláteros, tem área total 2a
- Um cubo de aresta 3 cm é cortado em 27 cubinhos de aresta 1 cm. A soma das áreas totais do
- Se a área total de um cubo é 150 cm², então a sua diagonal mede 5√3 cm.
- Ao cortar uma esfera maciça de raio R ao meio por um plano que passa pelo centro, a soma d
- Numa esfera de raio 2 cm, o fuso esférico de ângulo 90° tem área de 4π cm².
- Um poliedro convexo com 6 faces quadrangulares e 2 faces hexagonais tem 18 arestas e 14 vé
- A planificação da superfície lateral de um cone de raio 3 cm e geratriz 9 cm é um setor ci
```

## Pedido 13: Zoologia e botânica (`biologia.zoologia-botanica`, 12 tópicos de edital)

```
Matéria: biologia.zoologia-botanica (Zoologia e botânica)
Capítulos e quantas questões quero em cada um (total 100):
- "Invertebrados: planos corporais e principais filos": 17
- "Vertebrados: linhagens, adaptações e embriogênese": 17
- "Anatomia e fisiologia comparada dos animais": 17
- "Morfologia e anatomia vegetal: órgãos e tecidos": 17
- "Fisiologia vegetal: água, fotossíntese e respostas": 16
- "Sistemática vegetal, reprodução e identificação botânica": 16

Já existem (não repita):
- Em um exemplar aquático séssil foram observadas câmaras revestidas de células flageladas r
- Ao estudar dois vermes, o técnico encontra no primeiro corpo achatado e ausência de celoma
- Uma comparação entre bivalves e cefalópodes procura a associação que contraria a generaliz
- Um animal marinho tem ossículos calcários internos, larva bilateral e adulto com simetria 
- Aranhas adultas são insetos porque possuem exoesqueleto de quitina e apêndices articulados
- Uma planária de vida livre pode ter tubo digestório incompleto, enquanto uma tênia adulta 
- A ocorrência de simetria pentarradial no adulto prova que equinodermos e cnidários constit
- Qual conjunto de estruturas pode ser reconhecido, ao menos em algum momento do desenvolvim
- Ao comparar um tubarão com um teleósteo marinho típico, qual afirmação sobre estruturas e 
- Na comparação dos anexos embrionários, identifique a interpretação correta da condição amn
- Em um embrião de vertebrado, observam-se estruturas segmentadas originadas do mesoderma pa
- Todos os mamíferos são vivíparos, uma vez que glândulas mamárias e pelos implicam obrigato
- Nas aves, os sacos aéreos participam especialmente da ventilação, enquanto a troca gasosa 
- O tubo neural embrionário deriva diretamente do endoderma e dá origem ao sistema cardiovas
- Qual associação entre animal e principal estrutura respiratória está correta em condições 
- Uma minhoca alonga e encurta segmentos de seu corpo para se deslocar no solo. Esse movimen
- Na comparação dos sistemas circulatórios, assinale a afirmação biologicamente correta.
- Em um lago de água doce, um teleósteo apresenta influxo osmótico de água através de superf
- A circulação aberta de insetos significa que não existe vaso dorsal ou estrutura que impul
- A excreção predominante de ácido úrico em muitas aves está associada à economia relativa d
- Homeotermia é sinônimo obrigatório de endotermia, pois nenhum vertebrado endotérmico pode 
- O examinador encontra uma estrutura subterrânea com 'olhos' capazes de originar brotos. Qu
- Em um corte de caule lenhoso com crescimento secundário, qual produto do câmbio vascular s
- Durante a análise de uma folha, um estudante distingue tecido de sustentação flexível, de 
- Após a fecundação de uma angiosperma, quais estruturas da flor originam, em regra, a semen
- O xilema conduz principalmente água e sais minerais; o floema redistribui açúcares e outra
- A presença de gema axilar na base de cada folíolo permite distinguir folhas compostas de f
- Em muitas monocotiledôneas, os feixes vasculares caulinares estão dispersos, mas essa disp
- Qual processo explica melhor a manutenção de uma coluna de água ascendente nos vasos do xi
- A respeito da origem do oxigênio liberado por plantas durante a fotossíntese oxigênica, as
- Um pesquisador identifica uma planta que capta CO₂ predominantemente à noite, estoca ácido
- Após período de seca, uma planta apresenta fechamento estomático acentuado. Qual fitormôni
- A respiração celular das plantas ocorre somente à noite, porque durante o dia as células v
- Na translocação floemática, um órgão pode ser dreno enquanto jovem e tornar-se fonte quand
- Na deficiência de nitrogênio, os sintomas obrigatoriamente se iniciam nas folhas mais nova
- Uma planta avascular apresenta rizoides e gametófito fotossintetizante dominante, do qual 
- Ao observar a face inferior de uma fronde, o estudante encontra agrupamentos de esporângio
- Na reprodução de uma angiosperma típica, a dupla fecundação envolve dois gametas masculino
- Em identificação botânica, uma amostra apresenta flores verdadeiras e sementes protegidas 
- Os esporos haploides produzidos por meiose no esporófito podem germinar e dar origem ao ga
- Gimnospermas produzem frutos verdadeiros porque suas sementes se desenvolvem dentro de ová
- Uma chave dicotômica usa alternativas de caracteres observáveis e, quando bem construída, 
```

## Pedido 14: Regime Jurídico dos Servidores Civis do Amapá (Lei 066/1993) (`legislacao.amapa-lei-66-1993-regime-juridico`, 12 tópicos de edital)

```
Matéria: legislacao.amapa-lei-66-1993-regime-juridico (Regime Jurídico dos Servidores Civis do Amapá (Lei 066/1993))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições preliminares e formas de provimento": 14
- "2. Concurso, posse, exercício, estágio probatório, movimentação e vacância": 14
- "3. Vencimento, vantagens e férias": 14
- "4. Licenças, afastamentos, concessões, tempo de serviço e direito de petição": 12
- "5. Deveres, proibições, responsabilidades e penalidades": 27
- "6. Sindicância, processo administrativo disciplinar e revisão": 19

Já existem (não repita):
- A Lei 066/1993 institui o regime jurídico dos servidores públicos civis do Estado do Amapá
- Segundo a Lei 066/1993, às pessoas com deficiência serão reservadas, nos concursos público
- O retorno do servidor estável ao cargo anteriormente ocupado, em razão de sua inabilitação
- Invalidada por sentença a demissão de servidor estável, ele será reintegrado com ressarcim
- Sobre a readaptação na Lei 066/1993, é correto afirmar que ela:
- Para a primeira promoção na carreira, a Lei 066/1993 exige interstício não inferior a 2 an
- O servidor em disponibilidade que, aproveitado, não entrar em exercício no prazo legal ter
- De acordo com a Lei 066/1993, a posse deve ocorrer no prazo de:
- O servidor empossado que não entrar em exercício no prazo legal terá o ato de nomeação tor
- Com a redação dada pela Lei 2.752/2022, o estágio probatório previsto na Lei 066/1993 pass
- A avaliação de desempenho do servidor em estágio probatório, realizada por comissão consti
- Na redação atual, NÃO causa suspensão da contagem do estágio probatório na Lei 066/1993:
- Na Lei 066/1993, a movimentação do servidor de uma unidade administrativa para outra do me
- O servidor que responde a processo disciplinar só poderá ser exonerado a pedido, ou aposen
- Pela Lei 066/1993, o servidor não poderá ser transferido de ofício da localidade de sua re
- O serviço extraordinário, na Lei 066/1993, será remunerado com acréscimo de 50% sobre a ho
- Quanto ao adicional noturno da Lei 066/1993, assinale a alternativa correta.
- O servidor que fizer jus, ao mesmo tempo, aos adicionais de insalubridade e de periculosid
- A gratificação natalina prevista na Lei 066/1993:
- As reposições e indenizações devidas pelo servidor à Fazenda Pública estadual serão descon
- A diária será devida pela metade quando o deslocamento do servidor não exigir pernoite for
- Sobre as férias na Lei 066/1993, é correto afirmar:
- A ajuda de custo, calculada sobre a remuneração do servidor, não pode exceder a importânci
- A licença por motivo de doença em pessoa da família, na Lei 066/1993, é concedida:
- A licença para atividade política é remunerada desde a escolha do servidor em convenção pa
- A licença-prêmio por assiduidade assegura ao servidor, a cada quinquênio de efetivo exercí
- A licença para tratar de interesses particulares pode ser concedida ao servidor em estágio
- A licença para desempenho de mandato classista, na Lei 066/1993, é concedida:
- Com as alterações da Lei 1.003/2006, a licença à servidora gestante passou a ser de 180 di
- À servidora que adotar criança de 2 anos de idade, ou obtiver sua guarda judicial para fin
- O servidor investido no mandato de vereador, havendo compatibilidade de horários, perceber
- Sem qualquer prejuízo, o servidor poderá ausentar-se do serviço por oito dias consecutivos
- No direito de petição da Lei 066/1993, os prazos para o pedido de reconsideração e para o 
- O pedido de reconsideração e o recurso administrativo têm efeito suspensivo, e a decisão q
- É proibido ao servidor manter sob sua chefia imediata, em cargo ou função de confiança, cô
- Apurada em processo disciplinar a acumulação proibida de cargos e provada a boa-fé do serv
- As sanções civis, penais e administrativas aplicáveis ao servidor podem acumular-se, sendo
- Sobre a pena de suspensão na Lei 066/1993, é correto afirmar:
- Segundo a Lei 066/1993, configura inassiduidade habitual a falta ao serviço, sem causa jus
- A ação disciplinar quanto às infrações puníveis com suspensão prescreve em 2 anos, contado
- É circunstância agravante de falta disciplinar, segundo a Lei 066/1993:
- Da sindicância regida pela Lei 066/1993 pode resultar a aplicação da pena de demissão, des
- O afastamento preventivo do servidor durante o processo disciplinar, na Lei 066/1993, pode
- A comissão do processo disciplinar é composta por 3 servidores estáveis, não podendo integ
- No processo disciplinar com dois indiciados, o prazo para apresentação de defesa escrita s
- O julgamento do processo disciplinar fora do prazo legal de 20 dias implica nulidade do pr
- Quanto à revisão do processo disciplinar na Lei 066/1993, assinale a alternativa correta.
```

## Pedido 15: Lei Orgânica da Polícia Civil da Bahia (Lei nº 11.370/2009) (`legislacao.policia-civil-bahia`, 12 tópicos de edital)

```
Matéria: legislacao.policia-civil-bahia (Lei Orgânica da Polícia Civil da Bahia (Lei nº 11.370/2009))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, princípios, finalidade e competências": 20
- "2. Estrutura e direção superior": 20
- "3. Carreiras, hierarquia, atribuições e prerrogativas": 20
- "4. Ingresso, promoção, remoção e vantagens": 20
- "5. Regime disciplinar": 20

Já existem (não repita):
- Segundo a Lei nº 11.370/2009, a Polícia Civil do Estado da Bahia é:
- Entre os princípios institucionais da Polícia Civil da Bahia previstos no art. 3º da Lei n
- Compete à Polícia Civil da Bahia exercer, de ofício e com exclusividade, as funções de pol
- A Lei nº 11.370/2009 atribui à Polícia Civil da Bahia a formalização, com exclusividade, d
- Para a Lei nº 11.370/2009, considera-se de caráter técnico-científico toda função de inves
- Assinale a alternativa que contém apenas competências da Polícia Civil da Bahia previstas 
- São símbolos institucionais da Polícia Civil da Bahia o Hino, a Bandeira, o Brasão e o Dis
- Segundo a Lei nº 11.370/2009, o Delegado-Geral da Polícia Civil é nomeado:
- Em suas faltas, férias e impedimentos, o Delegado-Geral da Polícia Civil da Bahia é substi
- A estrutura organizacional da Polícia Civil da Bahia, segundo a Lei nº 11.370/2009, é comp
- À Corregedoria da Polícia Civil da Bahia cabe velar pela observância do regime disciplinar
- As Delegacias de Polícia Especializadas, segundo a Lei nº 11.370/2009, atuam:
- Para o ingresso nas carreiras da Polícia Civil da Bahia exige-se diploma de curso superior
- Segundo a Lei nº 11.370/2009, considera-se autoridade policial:
- Pela Lei nº 11.370/2009, os cargos de Investigador, Escrivão e Perito Técnico de Polícia C
- É atribuição privativa do Escrivão de Polícia Civil, segundo a Lei nº 11.370/2009:
- Entre as atribuições privativas do Investigador de Polícia Civil está efetuar prisões e bu
- O regime de trabalho dos cargos das carreiras da Polícia Civil da Bahia, segundo a redação
- Entre as prerrogativas dos policiais civis da Bahia está a de serem recolhidos, em razão d
- Segundo a Lei nº 11.370/2009, o Delegado de Polícia Civil, no exercício da função, somente
- Pela Lei nº 11.370/2009, o porte de arma de fogo é prerrogativa do policial civil apenas e
- Os cargos de Agente de Polícia Civil da Bahia passaram a denominar-se:
- Para o ingresso nas carreiras da Polícia Civil da Bahia, a Lei nº 11.370/2009 exige, entre
- Para o ingresso nas carreiras de Investigador e de Perito Técnico de Polícia Civil, exige-
- Pela Lei nº 11.370/2009, a aprovação em concurso público em classificação superior ao núme
- São requisitos cumulativos para a promoção dos policiais civis da Bahia:
- NÃO poderá concorrer à promoção, segundo a Lei nº 11.370/2009, o servidor que:
- O servidor que deixar de ser promovido por estar respondendo a processo disciplinar e depo
- Publicado o ato de remoção para unidade em município fora da Região Metropolitana de Salva
- Contra o ato de remoção cabe pedido de reconsideração em 5 dias e, da decisão, recurso ao 
- Pela redação atual da Lei nº 11.370/2009, a Gratificação de Incentivo à Titulação correspo
- São deveres dos policiais civis da Bahia, além dos comuns aos servidores, velar pela digni
- São penalidades disciplinares previstas na Lei nº 11.370/2009:
- Na Lei nº 11.370/2009, a pena de suspensão não pode exceder trinta dias.
- Os registros das penalidades de advertência e de suspensão são cancelados, segundo a Lei n
- Constitui hipótese de demissão prevista no art. 95 da Lei nº 11.370/2009:
- Associe a autoridade à penalidade que pode aplicar, segundo a Lei nº 11.370/2009. I. Gover
- Na Lei nº 11.370/2009, a ação disciplinar prescreve em cinco anos quanto às infrações puní
- Indicar ou insinuar advogado para patrocínio de inquérito policial constitui falta discipl
- A sindicância e o processo administrativo disciplinar dos policiais civis da Bahia seguem:
```

## Pedido 16: Plano de Cargos, Carreiras e Remuneração do Poder Judiciário do RN (LC 715/2022) (`rio-grande-do-norte.plano-cargos-judiciario-lc-715`, 12 tópicos de edital)

```
Matéria: rio-grande-do-norte.plano-cargos-judiciario-lc-715 (Plano de Cargos, Carreiras e Remuneração do Poder Judiciário do RN (LC 715/2022))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, política remuneratória e regime de trabalho": 25
- "2. Quadro de pessoal, investidura, cargos e funções": 25
- "3. Progressão funcional e avaliação de desempenho": 25
- "4. Gratificações, indenização e adicionais": 25

Já existem (não repita):
- O Plano de Cargos, Carreiras e Remuneração instituído pela LC 715/2022 fixa as diretrizes 
- Segundo o art. 2º da LC 715/2022, o agrupamento de categorias funcionais com atividades af
- Pela redação dada pela LC 727/2022, a função comissionada é o conjunto de encargos de dire
- Na LC 715/2022, o padrão indica os graus verticais das escalas de vencimentos da carreira,
- A ação de alocar o servidor que estava em uma tabela salarial antiga em uma nova tabela at
- Os servidores do Judiciário do RN cumprem jornada fixada conforme as atribuições do cargo,
- Os servidores investidos em cargos de provimento em comissão ou designados para função com
- Pela LC 715/2022, o Poder Judiciário do RN pode estabelecer sua estrutura funcional e orga
- O Quadro Geral de Pessoal do Judiciário do RN compreende um Quadro Suplementar de Servidor
- A investidura nos cargos efetivos do Judiciário do RN ocorre mediante concurso público, se
- É vedada a cessão de servidores do Judiciário do RN durante o estágio probatório, exceto p
- Após o estágio probatório, o servidor que somar mais de nove anos de serviço público prest
- O tempo de serviço prestado a outros Poderes ou entidades pode ser computado para hierarqu
- Segundo o art. 13 da LC 715/2022, NÃO é carreira do Quadro dos Cargos Efetivos do Judiciár
- Os cargos a que são inerentes atividades que exigem do titular registro no órgão fiscaliza
- Cabe ao Órgão Plenário do TJRN renomear os cargos efetivos, sem alteração de atribuições e
- O servidor efetivo investido em cargo em comissão pode escolher o vencimento do cargo efet
- O substituto designado para cargo em comissão faz jus à retribuição nos afastamentos ou im
- Nas substituições de cargo em comissão por período inferior a dez dias, os dias substituíd
- Os cargos em comissão de assessoria direta à atividade judicante, nos novos provimentos ap
- O TJRN deve preencher com pessoas com deficiência habilitadas o seguinte percentual de seu
- Na licença para tratamento de saúde do servidor exclusivamente comissionado, o Judiciário 
- As funções comissionadas do Judiciário do RN, de natureza propter laborem, são identificad
- O Órgão Plenário do TJRN pode renomear e transformar, sem aumento de despesa, cargos em co
- A progressão funcional consiste na movimentação do servidor efetivo para o padrão imediata
- Na progressão por tempo na carreira prevista no art. 26, I, da LC 715/2022, o requisito é 
- Na progressão por tempo combinado com merecimento, além de três anos no padrão, o servidor
- Para fins de progressão por merecimento, as horas de curso que excederem 24 horas em um an
- Segundo o art. 27 da LC 715/2022, NÃO impede a progressão funcional durante o seu curso:
- Para efeito de progressão funcional, não se considera efetivo exercício o tempo de prisão 
- Na comissão de avaliação de desempenho dos servidores do Judiciário do RN, a LC 715/2022 a
- A Gratificação de Atividade Externa (GAE), assegurada aos Oficiais de Justiça, corresponde
- A GAE é devida em caráter permanente, integra a remuneração nas férias e licenças remunera
- Pela redação dada pela LC 746/2023, o Oficial de Justiça mantém a GAE quando designado par
- A Indenização de Produtividade por Cumprimento de Mandado (IPCM) é paga aos Oficiais de Ju
- A partir de 1º de julho de 2024, o Adicional por Titulação devido ao servidor de nível sup
- O servidor de nível superior que tiver mestrado e especialização compatíveis pode receber 
- O Adicional por Titulação NÃO é concedido quando:
- O servidor de nível superior pode usar uma segunda graduação, além da exigida para ingress
- O Adicional por Tempo de Serviço dos servidores efetivos do Judiciário do RN, pela redação
- Para o cômputo dos quinquênios do Adicional por Tempo de Serviço, conta-se também o tempo 
```

## Pedido 17: Terapia ocupacional: áreas de atuação (`terapia-ocupacional.areas-atuacao`, 11 tópicos de edital)

```
Matéria: terapia-ocupacional.areas-atuacao (Terapia ocupacional: áreas de atuação)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação do desempenho ocupacional e modelos de prática": 17
- "Saúde mental e reabilitação psicossocial no território": 17
- "Reabilitação física, órteses, próteses e tecnologia assistiva": 17
- "Infância, desenvolvimento e inclusão nas ocupações da criança": 17
- "Gerontologia, atividades de vida diária e apoio a cuidadores": 16
- "Contextos sociais e hospitalares, políticas públicas e ética aplicada": 16

Já existem (não repita):
- Ao planejar a intervenção de uma pessoa que voltou a cozinhar após um AVC, qual é o foco m
- Na estrutura do Modelo de Ocupação Humana (MOHO), o termo habituação corresponde principal
- Na reavaliação, uma terapeuta deseja registrar mudanças percebidas pela pessoa em tarefas 
- Uma usuária escolhe sua rotina de banho, mas necessita de auxílio físico para entrar no bo
- Uma pessoa consegue vestir-se em teste padronizado, mas deixa de fazê-lo no domicílio por 
- Uma oficina no CAPS é concebida como recurso terapêutico quando:
- No campo da atenção psicossocial, a sigla PTS designa:
- No acompanhamento de pessoa com uso prejudicial de álcool que mantém vínculo com serviço d
- Após estabilização clínica, um usuário deseja retomar atividade de vendedor ambulante, mas
- Quanto aos Centros de Atenção Psicossocial (CAPS), assinale a alternativa correta:
- Em reabilitação da mão, o dispositivo externo destinado a posicionar e proteger uma articu
- Qual situação exige revisão do ajuste de uma órtese de punho?
- Uma pessoa com artrite apresenta dor ao abrir potes e deseja continuar cozinhando. Qual es
- A Lei Brasileira de Inclusão permite compreender tecnologia assistiva como:
- A equipe de um CER avalia uma pessoa com amputação, usuário de prótese, que encontra dific
- Na intervenção com crianças, brincar deve ser compreendido principalmente como:
- Uma criança com TEA evita participar do recreio por intenso ruído e aglomeração. Qual medi
- Uma criança tem dificuldade para realizar atividades de escrita por postura instável. A me
- Ao observar recusa alimentar em criança com condição do neurodesenvolvimento, a conduta pr
- Uma professora relata que estudante com deficiência participa da escrita em sessão, mas nã
- Qual alternativa contém somente atividades instrumentais de vida diária (AIVD)?
- Uma pessoa idosa com comprometimento leve de memória esquece etapas ao vestir-se. Qual est
- Uma idosa sofreu queda recente, mas deseja continuar indo ao jardim. Qual planejamento equ
- Em instituição de longa permanência, uma moradora com limitação motora escolhe o horário d
- Na orientação de cuidadores de pessoa idosa com demência, o que melhor preserva capacidade
- Ao preparar a alta hospitalar de pessoa com perda de mobilidade, o terapeuta ocupacional d
- Em projeto territorial com pessoas em situação de exclusão, qual abordagem é consistente c
- Uma pessoa em cuidados paliativos deseja participar de uma refeição com familiares, apesar
- Durante reunião, um profissional afirma que o terapeuta ocupacional deve seguir exclusivam
- Uma equipe propõe publicar fotografias identificáveis de atendimentos para demonstrar resu
- No MOHO, a volição inclui interesses, valores e a percepção da pessoa sobre suas capacidad
- A COPM é instrumento de classificação de doenças que dispensa a identificação das priorida
- Na análise pela CIF, uma barreira arquitetônica pode restringir a participação mesmo quand
- O Projeto Terapêutico Singular dos CAPS deve considerar objetivos negociados com a pessoa 
- A participação em oficina terapêutica comprova, por si só, inclusão social e torna desnece
- Uma estratégia de redução de danos pode integrar cuidados em saúde mental mesmo quando a p
- Uma prótese destina-se a substituir segmento corporal ausente, ao passo que uma órtese pod
- Tecnologia assistiva, para ser assim classificada, deve obrigatoriamente conter software o
- Na indicação de órtese, o objetivo ocupacional e a verificação de pressão sobre a pele são
- O brincar pode constituir simultaneamente ocupação significativa, contexto de observação e
- Crianças com o mesmo diagnóstico de transtorno do espectro autista devem receber necessari
- A adaptação do mobiliário escolar pode ser adequada mesmo que a criança já demonstre força
- Administrar compras domésticas costuma ser classificado como atividade instrumental de vid
- Como prevenção de quedas, a conduta padrão deve ser interromper permanentemente a mobilida
- Uma pessoa idosa que precisa de ajuda para vestir-se pode preservar autonomia para selecio
- Planejar alta hospitalar envolve considerar demandas de autocuidado, dispositivos, ambient
- Na Terapia Ocupacional em contextos sociais, a exclusão de espaços de convivência deve ser
- Na divulgação de imagens identificáveis de atendimento, a finalidade educacional ou instit
```

## Pedido 18: Fonoaudiologia: motricidade orofacial, neonatologia e saúde coletiva (`fonoaudiologia.motricidade-neonatal-coletiva`, 11 tópicos de edital)

```
Matéria: fonoaudiologia.motricidade-neonatal-coletiva (Fonoaudiologia: motricidade orofacial, neonatologia e saúde coletiva)
Capítulos e quantas questões quero em cada um (total 100):
- "Motricidade orofacial: bases, avaliação e terapêutica": 20
- "Neonatologia, aleitamento e triagem auditiva neonatal": 20
- "Deglutição, disfagia e segurança da assistência hospitalar": 20
- "Saúde coletiva, atenção primária e Fonoaudiologia educacional": 20
- "Saúde do trabalhador, legislação e ética profissional": 20

Já existem (não repita):
- O nervo trigêmeo, sobretudo seu ramo mandibular, participa da inervação motora dos princip
- A postura habitual de lábios entreabertos, observada isoladamente, confirma hipotonia musc
- A avaliação miofuncional adequada integra informações sobre estruturas, funções, compensaç
- A identificação visual de frênulo lingual curto é suficiente para indicar cirurgia, indepe
- Na assistência ao prematuro, a coordenação entre sucção, deglutição e respiração é relevan
- A recomendação do Ministério da Saúde é oferecer água e chás ao lactente saudável desde o 
- O resultado normal de triagem auditiva neonatal exclui a necessidade de monitorar a audiçã
- A sucção não nutritiva, isoladamente, comprova que o recém-nascido é capaz de ingerir leit
- Aspiração corresponde à entrada de material abaixo das pregas vocais verdadeiras, diferent
- A inexistência de tosse durante avaliação clínica de deglutição afasta, com certeza, o ris
- A escolha de consistências alimentares na disfagia deve ser individualizada, considerando 
- A Resolução CFFa nº 719/2023 prevê que a videoendoscopia da deglutição seja realizada pelo
- O princípio da equidade orienta respostas proporcionais às diferentes necessidades e barre
- Matriciamento na atenção primária consiste exclusivamente em encaminhar o usuário ao espec
- A Resolução CFFa nº 605/2021 prevê ações fonoaudiológicas de promoção e prevenção tanto em
- O sucesso de um programa coletivo pode ser demonstrado exclusivamente pela quantidade de p
- A Lei nº 6.965/1981 regulamenta a profissão de fonoaudiólogo e contempla, entre outras com
- O programa de conservação auditiva de trabalhadores se resume ao fornecimento de protetore
- A Resolução CFFa nº 640/2021 aprovou atualização do Código de Ética da Fonoaudiologia, que
- Informações clínicas identificadas podem ser divulgadas livremente a toda a gestão da esco
- Qual estrutura tem papel principal na movimentação ativa da mandíbula durante a mastigação
- Uma criança apresenta boca entreaberta e ronco noturno persistente. Qual conduta é mais ad
- Em avaliação de mastigação, qual conjunto oferece melhor análise funcional?
- Um bebê tem frênulo lingual aparentemente restritivo, mas o quadro de alimentação é comple
- Na reabilitação miofuncional, qual princípio deve dirigir a escolha de exercícios?
- Qual situação contraindica presumir prontidão imediata para alimentação oral de um recém-n
- Uma mulher pergunta quando iniciar água e outros alimentos para bebê saudável em aleitamen
- Um RN apresenta falha na TAN realizada na maternidade. Qual interpretação é correta?
- Qual afirmação descreve corretamente o Método Canguru?
- Em relação à Lei nº 12.303/2010, assinale a opção correta.
- Um paciente pós-AVC não tosse durante a oferta oral, mas possui sinais de risco. Qual conc
- No exame de deglutição, material entra no vestíbulo laríngeo, mas não ultrapassa as pregas
- Qual conduta melhor expressa a indicação de espessantes alimentares?
- Segundo a Resolução CFFa nº 719/2023, a videoendoscopia/nasofibrolaringoscopia da deglutiç
- Qual conjunto melhor expressa eficiência deglutitória, distinta da segurança?
- Uma equipe distribui oferta adicional de acompanhamento a famílias com maior barreira de a
- Na APS, a equipe eMulti realiza discussão de caso e plano compartilhado com a equipe de re
- Uma escola identifica dificuldades de acesso à comunicação de estudantes e pede apoio à Fo
- Qual indicador demonstra resultado de um programa e não apenas quantidade de atividades?
- Qual alternativa distingue corretamente prevenção secundária de promoção ampla da saúde?
- Um serviço solicita ao fonoaudiólogo divulgar informações clínicas identificadas de todos 
- Qual norma é a lei federal que regulamenta a profissão de fonoaudiólogo?
- Uma empresa adota apenas protetores auriculares e não analisa o ruído dos ambientes. O que
- O Código de Ética profissional atualizado foi aprovado por qual resolução do CFFa?
- Um trabalhador relata fadiga vocal recorrente em ambiente com ruído elevado e jornadas sem
```

## Pedido 19: Cirurgia torácica (`medicina.cirurgia-toracica`, 11 tópicos de edital)

```
Matéria: medicina.cirurgia-toracica (Cirurgia torácica)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação pré-operatória e cuidados perioperatórios": 17
- "Acessos cirúrgicos, ressecções e técnicas minimamente invasivas": 17
- "Câncer de pulmão: diagnóstico, estadiamento e tratamento cirúrgico": 17
- "Pleura: derrames, empiema e pneumotórax": 17
- "Trauma torácico, mediastino e parede torácica": 16
- "Transplante pulmonar e cirurgia torácica pediátrica": 16

Já existem (não repita):
- Homem de 66 anos, candidato à lobectomia, tem VEF1 preservado. Qual avaliação acrescenta i
- Candidata possui VEF1 de 80% do previsto e serão retirados quatro entre dezenove segmentos
- No preparo de paciente tabagista com DPOC estável e indicação de ressecção pulmonar eletiv
- Em candidato a ressecção pulmonar cujo risco permanece elevado após espirometria, DLCO e c
- Durante uma lobectomia por VATS, o anestesista necessita permitir colapso do pulmão operad
- Uma equipe planeja lobectomia anatômica por videotoracoscopia. Qual proposição descreve co
- No planejamento operatório de nódulo pulmonar periférico, qual alternativa diferencia corr
- Paciente apresenta carcinoma central envolvendo um brônquio lobar, sem necessidade obrigat
- Em lobectomia por VATS surge sangramento vascular que não pode ser controlado com seguranç
- No estadiamento TNM do carcinoma pulmonar, uma biópsia mostra linfonodo subcarinal com cél
- Em um paciente com carcinoma pulmonar, a citologia de derrame pleural demonstra células ma
- No PET-CT de um paciente com câncer pulmonar potencialmente operável, há captação mediasti
- Paciente operável apresenta CPNPC periférico pequeno, sem evidência de comprometimento lin
- O patologista relata ausência de carcinoma microscópico nas margens após lobectomia. Qual 
- O líquido de um derrame apresenta proteína pleural/sérica de 0,58, relação LDH de 0,42 e L
- Paciente com pneumonia apresenta líquido pleural purulento drenado por punção. Qual aborda
- Após trauma, paciente apresenta deterioração súbita, hipotensão, hipoxemia e murmúrio vesi
- Adulto com pneumotórax espontâneo primário estável, sem dor ou dispneia importantes, dispõ
- Paciente com empiema foi submetido a dreno e antibiótico, mas persiste coleção loculada e 
- Após colisão automobilística, um adulto hemodinamicamente estável tem tórax instável com i
- Uma tomografia mostra massa sólida anterior do mediastino com hipótese diferencial entre t
- Paciente recente de esternotomia apresenta febre, dor esternal, instabilidade da ferida e 
- Pessoa com bronquiectasias supurativas avançadas está em avaliação para transplante. Que a
- Anos após transplante pulmonar, paciente apresenta redução persistente do VEF1. Antes de c
- Criança com malformação pulmonar congênita localizada e indicação cirúrgica é avaliada em 
- A mensuração de DLCO pode identificar risco relevante em candidato à ressecção pulmonar me
- O ppoVEF1 estimado por contagem segmentar dispensa avaliação cardiovascular e torna desnec
- A analgesia multimodal adequada após toracotomia contribui para tosse eficaz, mobilização 
- Ventilação monopulmonar elimina o risco de hipoxemia durante cirurgia torácica e dispensa 
- A segmentectomia anatômica diferencia-se da ressecção em cunha por envolver dissecção diri
- Em oncologia torácica, a conversão de VATS para toracotomia por sangramento de difícil con
- Ressecção em manga brônquica pode preservar parênquima em tumor central selecionado, desde
- Metástase em linfonodo hilar ipsilateral corresponde a N3 no estadiamento do carcinoma pul
- A presença de derrame pleural neoplásico confirmado em um paciente com carcinoma pulmonar 
- Linfonodo mediastinal com captação intensa de FDG no PET-CT representa confirmação histoló
- A indicação de cirurgia para câncer de pulmão depende de estadiamento, condição funcional 
- Basta um dos critérios de Light positivo para considerar o derrame pleural exsudativo.
- A presença de pus franco na cavidade pleural caracteriza empiema e frequentemente demanda 
- No pneumotórax hipertensivo com choque, a radiografia de tórax deve obrigatoriamente antec
- Em pneumotórax espontâneo primário estável e pouco sintomático, o tamanho na radiografia é
- A estabilização cirúrgica de costelas pode beneficiar selecionados com tórax instável e in
- Uma massa anterior do mediastino pode corresponder a timoma ou linfoma, e a hipótese diagn
- Mediastinite aguda pós-esternotomia deve ser manejada exclusivamente com analgésicos, sem 
- Uma queda persistente do VEF1 após transplante pulmonar deve ser atribuída de imediato à C
- Na cirurgia torácica pediátrica, a videotoracoscopia pode ser utilizada em casos seleciona
```

## Pedido 20: Pré-Modernismo e Vanguardas Europeias (`literatura.pre-modernismo-vanguardas`, 11 tópicos de edital)

```
Matéria: literatura.pre-modernismo-vanguardas (Pré-Modernismo e Vanguardas Europeias)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Pré-Modernismo: transição literária e contradições do Brasil": 17
- "2. Euclides da Cunha, Graça Aranha e a leitura crítica dos espaços": 17
- "3. Lima Barreto, Monteiro Lobato e Augusto dos Anjos": 17
- "4. Vanguardas Europeias: Expressionismo, Fauvismo, Cubismo e Futurismo": 25
- "5. Dadaísmo, Surrealismo e leitura comparativa das vanguardas": 24

Já existem (não repita):
- Um material didático reúne obras brasileiras de 1902 a 1922, mas nota que os escritores nã
- Ao estudar Os sertões, uma leitora identifica descrições do ambiente, reflexão sobre popul
- Um romance de 1902 ambientado no Espírito Santo explora a imigração e debate concepções di
- Em uma narrativa satírica, o personagem Policarpo sustenta que o tupi-guarani deveria torn
- Um poema em soneto associa decomposição do corpo, vocabulário biológico e angústia existen
- Um pintor representa várias faces de um instrumento musical em um único plano, decompondo-
- Uma pintura apresenta montanhas azul-elétricas e árvores vermelhas, com pinceladas vigoros
- Um manifesto europeu de 1909 celebra motores, velocidade, máquinas e mudança agressiva da 
- Durante a Primeira Guerra, um coletivo artístico reúne palavras sorteadas de jornais e pro
- Um grupo propõe registrar associações espontâneas da mente e explorar sonhos com menor con
- Dois livros lançados em 1902 costumam representar a abertura didática do Pré-Modernismo. U
- Em textos de início do século XX, um observador percorre ruas da capital e registra ofício
- Uma interpretação diz que o uso de soneto em Eu, de Augusto dos Anjos, impede qualquer car
- Uma análise compara a apresentação inicial do Jeca Tatu com textos de Lobato ligados à cam
- Um estudo de documentos estéticos encontra um manifesto italiano de 1909 voltado à velocid
- Um pesquisador compara duas pinturas cubistas: uma fragmenta a garrafa em pequenas facetas
- Duas composições expressam ruptura: uma distorce a cidade até ela parecer ameaçar um perso
- Um leitor descreve Policarpo Quaresma como herói patriótico apresentado sem ironia, cujo d
- Uma interpretação sustenta que, por usar teorias científicas de sua época, Os sertões nece
- Um argumento diz que o manifesto surrealista de 1924 inspirou diretamente o livro Eu, publ
- Um estudo compara crítica do sertanejo em Urupês e reformulação do Jeca nos textos sanitár
- Uma composição contém fragmentos de jornal inseridos para reconstruir a forma de violino; 
- Um poeta procura registrar imagens inesperadas por associação psíquica livre, orientada ao
- Uma leitura reúne a descrição geográfica de Canudos, a crítica à imprensa por Isaías e as 
- Graça Aranha publicou Canaã em 1902 e participou posteriormente de debates de renovação es
- Na classificação escolar, o Pré-Modernismo brasileiro reúne autores de estilos diversos e 
- Os autores chamados pré-modernistas já haviam abandonado por completo formas poéticas e na
- Os sertões, publicado em 1902, distribui seus eixos descritivos e narrativos em A terra, O
- Os personagens Milkau e Lentz, ligados ao tema da imigração em Canaã, aparecem no romance 
- Triste fim de Policarpo Quaresma começou a circular em folhetins em 1911 e foi publicado e
- A crítica de Os sertões restringe-se à defesa irrestrita das expedições contra Canudos, po
- Em A alma encantadora das ruas, João do Rio toma experiências urbanas, trabalhadores e prá
- Em Problema vital, Monteiro Lobato reafirma que o estado do Jeca Tatu decorre necessariame
- Na obra Eu, Augusto dos Anjos combina termos da ciência e imagens de decomposição com inqu
- A diferença entre Fauvismo e Cubismo reside sobretudo em o primeiro reunir pontos de vista
- Na fase sintética do Cubismo, a incorporação de materiais de jornal, palavras e papéis à c
- O primeiro manifesto surrealista, publicado em 1924, foi o programa estético formal que un
- Uma composição feita por sorteio de palavras com intenção de provocar expectativas cultura
- No Expressionismo, o uso de figuras distorcidas e cores intensas tem como fim característi
- O Futurismo de Marinetti exaltava velocidade e tecnologia, mas seus manifestos também incl
- Na teoria das vanguardas, escrita automática surrealista e poesia dadaísta feita por sorte
- A presença de sonetos e referências herdadas do Simbolismo em Eu, de Augusto dos Anjos, é 
- Como Graça Aranha participou de eventos renovadores na década de 1920, é historicamente co
- O ano de 1909 reúne o lançamento de Recordações do escrivão Isaías Caminha e o manifesto f
- A divisão A terra, O homem e A luta demonstra que Os sertões contém apenas descrição natur
```

## Pedido 21: Service desk e suporte técnico (`informatica.service-desk-suporte-itil`, 10 tópicos de edital)

```
Matéria: informatica.service-desk-suporte-itil (Service desk e suporte técnico)
Capítulos e quantas questões quero em cada um (total 100):
- "Atendimento, triagem e escalonamento no service desk": 17
- "Incidentes, requisições, problemas e mudanças operacionais": 17
- "Ferramentas de help desk, conhecimento e métricas": 17
- "Inventário, ativos e diagnóstico de estações e periféricos": 17
- "Automação operacional com Shell e PowerShell": 16
- "Acesso remoto seguro, documentação e casos integrados": 16

Já existem (não repita):
- Uma chamada recebida por telefone deve ser registrada com protocolo e histórico, mesmo qua
- Escalonamento funcional significa necessariamente encaminhar o incidente à chefia para aut
- Em uma matriz de prioridade, impacto e urgência são conceitos diferentes, e a instituição 
- O cumprimento do SLA de primeira resposta comprova, por si só, que o incidente foi resolvi
- Um pedido de criação de conta para novo colaborador, quando previsto no catálogo e autoriz
- No suporte orientado a incidentes, um contorno que restaura o serviço pode permitir a reso
- Uma falha recorrente após aplicação de atualizações é necessariamente explicada pela atual
- Em suspeita de ransomware, a abertura do chamado de indisponibilidade dispensa comunicar a
- No GLPI, um chamado pode ser relacionado a um equipamento inventariado e a conteúdo da bas
- Um artigo destinado apenas a técnicos pode conter credenciais administrativas em texto pur
- Se 18 de 20 chamados elegíveis atingiram a meta estabelecida, a taxa simples de cumpriment
- No inventário de equipamentos, o endereço IP dinâmico é identificador mais estável que o n
- Uma resposta positiva ao comando ping prova que o serviço web de destino está funcionando 
- Limpar o cache DNS do cliente Windows pode ajudar a remover uma resposta armazenada localm
- No PowerShell, o pipeline de cmdlets normalmente transporta objetos com propriedades, ao c
- A política de execução AllSigned do PowerShell constitui, por si só, uma fronteira de segu
- Telnet e SSH oferecem, por padrão, o mesmo nível de proteção criptográfica ao conteúdo de 
- Consoles como iDRAC e iLO podem viabilizar gerenciamento de hardware sem depender de o sis
- Um atendente recebe um relato vago de lentidão do sistema. Qual registro inicial melhor fa
- Ao identificar falha de autenticação que afeta toda a unidade e exige análise do provedor 
- Qual situação exemplifica escalonamento hierárquico, e não apenas funcional?
- Um usuário cobra cumprimento de SLA, mas obteve resposta em 10 minutos e solução em 12 hor
- Na triagem de um incidente, qual pergunta ajuda mais a determinar seu impacto?
- O setor de pessoal solicita acesso de novo servidor a um módulo restrito. A medida correta
- Um sistema é restaurado com reinício controlado, mas volta a falhar semanalmente. O encami
- Após atualização recente, quarenta computadores apresentam o mesmo erro. Qual raciocínio é
- Em um formulário de atendimento, qual pedido costuma ser requisição, e não incidente?
- Um atendimento sugere ransomware em uma estação. Qual ação é mais compatível com o procedi
- Uma equipe quer identificar computadores que acumulam chamados de falha de disco. Qual rec
- Ao comparar GLPI e osTicket, a afirmação tecnicamente mais adequada é:
- Uma instrução de recuperação de acesso será publicada para usuários no autosserviço. Qual 
- Em um conjunto elegível de 25 chamados, 20 respeitaram o prazo definido. Qual foi a taxa s
- A direção deseja reduzir o estoque acumulado de chamados ainda não concluídos. Qual indica
- Após troca do roteador, uma estação abre portal por IP, mas não por nome. Qual primeiro te
- Um técnico usa ping para um servidor e recebe resposta. O que pode inferir de modo seguro?
- A mesma impressora aparece como offline para todos os usuários, enquanto impressoras vizin
- No cadastro de ativo com endereço IP atribuído por DHCP, qual identificação é mais confiáv
- Em um script Bash, qual medida reduz erros quando um caminho de arquivo contém espaços?
- Ao filtrar serviços Windows parados pelo PowerShell, qual construção representa corretamen
- Qual afirmação sobre política de execução do PowerShell está correta?
- Um script deve levantar a versão do sistema operacional Windows de modo estruturado e expo
- Ao usar try/catch no PowerShell, por que frequentemente se especifica -ErrorAction Stop em
- Uma equipe deseja acesso remoto ao desktop de servidor Windows fora da sede. Qual arquitet
- Sobre tecnologias de suporte remoto, qual alternativa identifica corretamente uma solução 
- Durante o suporte remoto, um usuário informa a senha pessoal para acelerar diagnóstico. Qu
- Em uma rede não confiável, qual comparação entre SSH, Telnet e VNC é correta?
- Após finalizar assistência remota a estação que exibia dados pessoais, qual registro de fe
- Um agente precisa fornecer a chefia indicador do tempo gasto em 4 incidentes resolvidos em
```

## Pedido 22: Instalações de vapor, caldeiras, refrigeração e ar comprimido (`engenharia-mecanica.vapor-caldeiras-refrigeracao`, 10 tópicos de edital)

```
Matéria: engenharia-mecanica.vapor-caldeiras-refrigeracao (Instalações de vapor, caldeiras, refrigeração e ar comprimido)
Capítulos e quantas questões quero em cada um (total 100):
- "Geradores de vapor: seleção, componentes e operação": 17
- "Segurança de caldeiras e vasos de pressão: aplicação da NR-13": 17
- "Redes de vapor, condensado e materiais de instalação": 17
- "Compressores e sistemas de ar comprimido": 17
- "Refrigeração, ar-condicionado, carga térmica e automação": 16
- "Bombas, ventiladores, manutenção e eficiência energética": 16

Já existem (não repita):
- Em comparação às caldeiras aquatubulares, qual característica construtiva identifica uma c
- Uma caldeira produz 1.000 kg/h de vapor, entregando ganho útil médio de 2.800 kJ/kg; o com
- Em uma inspeção operacional, houve aumento de condutividade da água da caldeira. Qual cond
- Durante a operação de caldeira a combustível, qual é uma finalidade do economizador quando
- Em caldeira aquatubular, a água circula no interior dos tubos, enquanto os gases quentes t
- A válvula de segurança é destinada principalmente à modulação rotineira da produção de vap
- Se a caldeira recebe água visualmente transparente, o acompanhamento da qualidade da água 
- Em uma caldeira a combustível, excesso exagerado de ar de combustão pode elevar perdas de 
- Uma caldeira abrangida pela NR-13 opera à pressão de 1.960 kPa. Qual é sua categoria na cl
- Uma empresa possui tubulações que operam com vapor de água. Considerando a NR-13 consultad
- O que deve ser feito, em termos de inspeção, quando uma caldeira muda de local de instalaç
- Uma equipe revisa documentos obrigatórios de uma caldeira. Qual conjunto está alinhado com
- A categoria B de caldeiras na NR-13 abrange pressões de operação superiores a 60 kPa e inf
- Toda caldeira está sempre sujeita a inspeção periódica a cada 12 meses, sem qualquer exceç
- Uma caldeira nova deve ser inspecionada inicialmente antes de entrar em funcionamento no l
- Por se tratar de equipamento automatizado com BMS, uma caldeira pode operar sem o controle
- Um purgador instalado no ramal principal de vapor falha de modo permanentemente aberto. Qu
- Um trecho de distribuição apresenta forte batimento durante a abertura abrupta de uma válv
- Qual condição permite considerar o retorno de condensado de processo à alimentação da cald
- Para selecionar um material e uma vedação de linha de vapor, qual combinação de critérios 
- Purgadores mecânicos, termostáticos e termodinâmicos são famílias existentes; sua escolha 
- O isolamento térmico elimina por completo a condensação em qualquer rede de vapor e torna 
- Purgador que falha fechado pode reter condensado, reduzir a efetividade da transferência d
- A identificação de linhas por uma cor dispensa qualquer informação adicional sobre fluido,
- Em uma instalação com compressores de ar, qual afirmação descreve corretamente a função de
- Uma fábrica registra vazão de saída de 20 m³/min de ar comprimido, com 4 m³/min perdidos p
- Qual é a função típica do secador por refrigeração em uma central de ar comprimido?
- O operador observa baixa pressão nas ferramentas pneumáticas e alta pressão à saída do com
- Um compressor declarado isento de óleo dispensa sempre separação de condensado, controle d
- Compressores operando longos períodos em alívio podem consumir eletricidade sem fornecer v
- O ponto de orvalho sob pressão indica a temperatura na qual a umidade pode começar a conde
- No reservatório de ar comprimido, a presença de condensado não afeta corrosão e nunca dema
- Em um circuito convencional de refrigeração por compressão de vapor, qual é a função princ
- Um sistema de refrigeração remove 12 kW do ambiente e consome 4 kW de potência elétrica no
- Para um insuflamento com vazão de 0,5 m³/s, densidade do ar de 1,2 kg/m³, cp de 1,0 kJ/(kg
- Uma central predial integra chillers, bombas, sensores e unidades de tratamento de ar em u
- No ciclo de refrigeração por compressão de vapor usado para resfriar um ambiente, o conden
- A carga térmica sensível representa por si só todas as necessidades de desumidificação de 
- Um protocolo aberto de BMS dispensa calibrar sensores de temperatura, umidade e pressão, p
- Se um aparelho apresenta resfriamento insuficiente, é possível concluir apenas pelo sintom
- Uma bomba transporta água com ρ=1.000 kg/m³, Q=0,01 m³/s e H=20 m, usando g=9,81 m/s². Sua
- Em condições idealizadas de leis de afinidade para ventilador de mesma geometria, a rotaçã
- Uma bomba centrífuga apresenta vibração, ruído de estalos e erosão próxima ao rotor, espec
- Um setor implementa medições de vibração e temperatura de mancais, intervenções quando ten
- Em uma bomba centrífuga, o ponto de operação decorre da interseção entre a curva da bomba 
- As leis de afinidade garantem, em qualquer instalação e para qualquer redução de rotação, 
- Antes de abrir uma linha de ar comprimido pressurizada para manutenção, basta desligar a a
- Filtros de ar-condicionado saturados podem reduzir vazão e degradar a capacidade de condic
```

## Pedido 23: O papel do Conselho de Direitos Humanos da ONU (`direito.papel-conselho-direitos-humanos-onu`, 10 tópicos de edital)

```
Matéria: direito.papel-conselho-direitos-humanos-onu (O papel do Conselho de Direitos Humanos da ONU)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Origem, natureza e composição do Conselho de Direitos Humanos": 13
- "2. Mandato de promoção e proteção, reuniões e formas de atuação": 13
- "3. Revisão Periódica Universal: bases, etapas e acompanhamento": 50
- "4. Procedimentos especiais: relatores, especialistas e comunicações": 12
- "5. Procedimento de queixas, admissibilidade e integração dos mecanismos": 12

Já existem (não repita):
- Uma estudante lê que um colegiado intergovernamental de direitos humanos foi criado pela A
- Em atividade sobre a composição do Conselho, uma equipe diferencia os Estados que podem oc
- Um Estado concluiu seu primeiro mandato no Conselho e pretende disputar nova eleição. Qual
- Um relatório menciona avaliação periódica dos direitos humanos de país que não integra os 
- Uma pesquisadora recebe notícia de especialista independente que acompanha violações relat
- Ao discutir a regularidade das reuniões do Conselho, um participante afirma que basta uma 
- Representante de organização social pergunta quantos relatórios essenciais subsidiam a Rev
- Uma vítima envia comunicação destinada a demonstrar violações graves reiteradas, submetida
- Uma delegação sugere convocar sessão especial do Conselho diante de situação urgente. Qual
- Um observador compara o primeiro ciclo da RPU com os posteriores e nota que o intervalo fo
- A Assembleia Geral tem de preencher uma vaga no Conselho. Com 193 membros na organização, 
- A Assembleia Geral aprecia suspensão dos direitos de um integrante do Conselho acusado de 
- Na RPU de determinado Estado, sorteiam-se três outros Estados para facilitar a revisão e a
- Durante a RPU, os demais Estados formularam recomendações. O país examinado manifesta conc
- Uma pessoa encaminha a relator especial dados detalhados sobre violação atual e pergunta s
- Uma organização apresenta documentação de violações graves repetidas e seus recursos inter
- Após uma triagem, uma denúncia confidencial de padrão grave de violações chega ao Grupo de
- Durante aula, alguém afirma que os 13 assentos africanos e os 13 da Ásia-Pacífico somam ma
- Um país foi membro por dois mandatos consecutivos de três anos e pretende concorrer imedia
- Numa RPU, os 47 integrantes do Conselho conduzem a revisão de Estado não membro, enquanto 
- O relatório nacional da RPU informa política pública bem-sucedida; organizações locais apr
- Um relator especial recebe notícia fundamentada de que pessoas correm risco iminente de de
- Ao examinar procedimento confidencial, um participante confunde os dois grupos de trabalho
- Uma associação pretende apresentar queixa anônima apoiada unicamente em manchetes, alegand
- Um Estado quer contestar a avaliação de certo tema no Conselho, afirmando que só decisões 
- Ao preparar um quadro de instituições, uma aluna identifica o Conselho de Direitos Humanos
- Uma delegação sustenta que, por haver 193 Estados na ONU, todos eles ocupam automaticament
- Em eleição para o Conselho, os Estados devem ser escolhidos individualmente pela Assemblei
- Uma apostila afirma que o mandato de cada Estado no Conselho dura quatro anos e meio, exat
- Em estudo sobre reuniões do Conselho, afirma-se que a Resolução 60/251 estabelece pelo men
- Um país que não ocupa cadeira no Conselho pode recusar sua Revisão Periódica Universal com
- Durante revisão de determinado Estado, as recomendações são formuladas em diálogo intergov
- Uma equipe relata que a troica da RPU é composta por cinco especialistas independentes do 
- Em processo de Revisão Periódica Universal, o ACNUDH prepara tanto uma compilação de infor
- Ao terminar sua RPU, um Estado declara não apoiar algumas recomendações. Nessa hipótese, o
- Relator especial recebe alegações detalhadas de violação em andamento e envia comunicação 
- Na orientação do ACNUDH, os procedimentos especiais só podem transmitir alegações de viola
- Quando organização civil envia documento para os procedimentos especiais, a mera confirmaç
- Após dois mandatos consecutivos de três anos, um Estado deixa de ser elegível à recondução
- A Assembleia Geral, ao considerar suspensão dos direitos de um integrante do Conselho por 
- Durante discussão de crise humanitária, um integrante do Conselho reúne apoio de outros me
- O procedimento de queixas previsto na Resolução 5/1 só admite relatos sobre países eleitos
- Na tramitação de uma queixa com alegações de violações graves reiteradas, o Grupo de Traba
- Uma denúncia anônima, baseada somente em notícia de jornal e com linguagem abusiva, deve s
- Ao examinar a situação de país com violações estruturais, é possível distinguir RPU como r
```

## Pedido 24: Biometria e sistemas de identificação (`informatica.biometria-sistemas-identificacao`, 10 tópicos de edital)

```
Matéria: informatica.biometria-sistemas-identificacao (Biometria e sistemas de identificação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de biometria, captura e identificação": 10
- "2. Intercâmbio de dados: ANSI/NIST-ITL, XML e JSON": 27
- "3. Taxas de erro e efeito dos limiares de decisão": 27
- "4. Avaliações NIST, experimentos e leitura crítica de resultados": 26
- "5. Implantação, segurança, governança e proteção de dados": 10

Já existem (não repita):
- Uma pessoa informa seu identificador antes de encostar o dedo no leitor. O sistema compara
- Uma imagem facial sem identificador informado é comparada com 80 mil cadastros para locali
- Durante o cadastramento facial, o software extrai um vetor de características de uma fotog
- Em 1.000 buscas faciais de pessoas comprovadamente presentes na galeria, 40 não retornaram
- Um sistema 1:N pesquisa 2.000 pessoas ausentes da galeria e identifica indevidamente oito 
- Em um ensaio de verificação facial, 10 de 10.000 pares de indivíduos distintos foram aceit
- Duas soluções precisam trocar registros de impressões digitais com campos e tipos padroniz
- Uma API retorna um objeto JSON sintaticamente válido com uma fotografia codificada. O que 
- Um projeto utiliza elementos hierárquicos e arquivos de esquema XSD para conferir mensagen
- Num teste de identificação, 15 das 5.000 buscas de pessoas ausentes da galeria retornaram 
- Em 4.000 buscas de pessoas presentes na galeria, 100 não produziram o candidato correto se
- Um verificador 1:1 aceita 12 de 6.000 pares de indivíduos distintos. Qual taxa e denominad
- O sistema utiliza pontuação de similaridade, e o administrador eleva o limiar de aceitação
- Uma especificação de intercâmbio cita a revisão ANSI/NIST-ITL 1-2025. Qual publicação ofic
- Uma galeria com 20 mil pessoas recebe tanto consultas de cadastrados quanto de visitantes 
- Uma equipe quer comparar algoritmos de verificação facial usando resultados públicos de te
- Um relatório traz dois resultados: rejeição de pares genuínos e não retorno da identidade 
- Uma avaliação 1:N obteve seis identificações indevidas em 3.000 buscas sem correspondente 
- Um sistema investigativo coloca o verdadeiro correspondente em segundo entre 50 candidatos
- Dois grupos apresentam FNIR diferente em um mesmo teste 1:N. O segundo utiliza imagens fre
- Um projeto exige usar a revisão ANSI/NIST-ITL 1-2025 e assume que qualquer JSON genérico j
- Um pacote biométrico passa por parser JSON e chega íntegro ao receptor, mas está sem campo
- Uma aplicação 1:N pretende reduzir as falhas de identificação de cadastrados diminuindo o 
- Um fornecedor apresenta FMR de 0,001 em pares 1:1 e afirma que uma pesquisa 1:N entre um m
- Dois estudos registram FPIR de 0,2%. O primeiro usa uma ocorrência errada em 500 buscas e 
- Uma pessoa declara determinada identidade e apresenta a face para comparação com a única r
- Um template biométrico equivale necessariamente à imagem original produzida pelo sensor, p
- A FMR descreve a proporção de comparações de indivíduos diferentes aceitas como correspond
- Toda mensagem JSON válida segundo a RFC 8259 já satisfaz os requisitos de registros biomét
- O padrão ANSI/NIST-ITL disciplina a estrutura de informações biométricas intercambiadas, s
- Se um sistema 1:N não encontra a identidade de uma pessoa cadastrada, esse evento deve ent
- Uma avaliação que contou 14 comparações 1:1 de impostores aceitas entre 7.000 pares pode c
- Em um experimento 1:N com 10.000 consultas de pessoas não cadastradas e 25 identificações 
- O cálculo de FNIR utiliza, como denominador, o total de buscas de pessoas ausentes da gale
- A edição ANSI/NIST-ITL 1-2025 encontra-se documentada na NIST SP 500-290e4, publicada em m
- O resultado de um algoritmo no FRTE 1:1 comprova que o sistema completo, instalado em qual
- Em sistemas de pontuação por similaridade, elevar o limiar de aceitação tende a diminuir f
- Se um teste define acerto somente quando a identidade verdadeira ocupa a primeira posição,
- A Lei nº 13.709/2018, art. 5º, II, classifica dado biométrico vinculado a pessoa natural c
- Um template facial deixa de estar sujeito à proteção de dados sensíveis quando é armazenad
- Se dois ensaios 1:N apontam FPIR idêntica, aquele com número muito maior de buscas pode of
- O arquivo intercambiado com criptografia autenticada e sintaxe XML correta comprova, por e
- Uma avaliação 1:N que mistura buscas com e sem correspondente em um único denominador perd
- Uma taxa FMR medida em comparações 1:1 deve ser transportada sem alteração para FPIR em bu
- A publicação da edição ANSI/NIST-ITL 1-2025 e a elaboração de suas codificações XML/JSON s
```

## Pedido 25: Ensino de biologia e ciências da natureza (`educacao.ensino-biologia`, 10 tópicos de edital)

```
Matéria: educacao.ensino-biologia (Ensino de biologia e ciências da natureza)
Capítulos e quantas questões quero em cada um (total 100):
- "Biologia escolar, currículo e letramento científico": 17
- "Aprendizagem de Biologia e abordagens metodológicas": 17
- "Recursos didáticos, laboratório e tecnologias digitais": 17
- "Educação ambiental, saúde e contextualização sociocientífica": 17
- "Evolução, genética e questões controversas em sala de aula": 16
- "Avaliação, inclusão e planejamento do ensino de Biologia": 16

Já existem (não repita):
- Ao planejar uma unidade sobre biodiversidade no Ensino Médio, qual decisão respeita a arti
- Na BNCC do Ensino Médio, Biologia, Física e Química integram a área de Ciências da Naturez
- Uma professora compara dados públicos sobre vacinação, questiona o desenho de um estudo e 
- Como o currículo de uma rede apenas reproduz a BNCC literalmente, o docente pode dispensar
- Uma banca afirma que todo problema integrado de Ciências da Natureza elimina as especifici
- Uma proposta de letramento científico deve permitir ao estudante avaliar fontes e evidênci
- Num edital de Biologia do Distrito Federal, o candidato encontra competência da área e hab
- Afirmar que modelos científicos são revisáveis implica reconhecer que qualquer explicação 
- Durante uma sondagem, vários estudantes afirmam que plantas se alimentam exclusivamente da
- A sondagem inicial de explicações dos alunos sobre nutrição vegetal pode orientar a escolh
- Em uma sequência investigativa sobre crescimento de plantas, os grupos definem hipóteses, 
- Uma aula só pode ser considerada investigativa quando apresenta experimentação com manipul
- Uma escola propõe produzir um guia acessível sobre espécies do bairro, com pesquisa, revis
- Na abordagem CTSA, avaliar consequências de uma tecnologia exige articular conhecimento ci
- Uma notícia sobre organismos geneticamente modificados é usada para discutir riscos, benef
- A aprendizagem baseada em projetos dispensa explicitação de critérios de avaliação, porque
- Ao apresentar a imagem colorida de uma organela, qual explicação docente previne uma disto
- Uma animação sobre estrutura celular deve ser tratada como representação didática, pois co
- Uma turma testa o crescimento de sementes sob diferentes condições; qual providência aumen
- Atividades verificativas e demonstrativas devem ser eliminadas do ensino de Biologia, pois
- Durante uma aula prática, foram recebidos recipientes com crescimento microbiano ambiental
- Em prática escolar com amostras ambientais, recipientes contendo crescimento microbiano de
- O professor usa simulação que prevê mudanças populacionais a partir de parâmetros escolhid
- O uso de tecnologias digitais pode apoiar o ensino de Biologia, mas não torna automaticame
- Em uma escola, a educação ambiental é retomada ao longo do ano em Ciências, Geografia e pr
- A educação ambiental, segundo a política nacional brasileira, deve estar presente de modo 
- Ao trabalhar dengue e condições de moradia, o docente articula a unidade escolar com profi
- Uma campanha escolar de saúde somente atende ao PSE se professores substituírem as equipes
- Em um projeto sobre resíduos, estudantes comparam dados públicos, visitam local autorizado
- Em debate sobre contaminação de um curso de água, a observação isolada de mau cheiro é suf
- Em debate sobre tratamento da água, surgem depoimentos conflitantes e indicadores oficiais
- O docente pode ensinar evolução biológica de modo cientificamente rigoroso e, ao mesmo tem
- Um estudante diz que teoria da evolução seria somente uma opinião porque é chamada de teor
- Uma representação linear da evolução como escada necessariamente comunica de modo adequado
- Ao usar uma árvore evolutiva no quadro, o professor quer evitar a ideia de progresso linea
- Em aulas de genética, o uso de heredogramas fictícios pode favorecer análise probabilístic
- Uma atividade de genética propõe que alunos revelem doenças familiares para construir here
- A devolutiva específica sobre um relatório de Biologia pode favorecer a avaliação formativ
- Uma turma debate aplicações de edição genética em humanos; qual estratégia diferencia adeq
- Para avaliar corretamente uma aula de microscopia, o professor deve aplicar instrumentos i
- Após avaliar relatórios, uma docente mostra critérios, comenta erros de interpretação e of
- Para avaliar leitura de gráficos sobre epidemias, qual rubrica apresenta critério mais lig
- Um aluno com deficiência visual participa de atividade sobre morfologia foliar; qual adapt
- Após prova de conceitos genéticos, uma turma apresenta erro recorrente sobre probabilidade
```

## Pedido 26: Redução de riscos e desastres (`defesa-civil.reducao-riscos-desastres`, 9 tópicos de edital)

```
Matéria: defesa-civil.reducao-riscos-desastres (Redução de riscos e desastres)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, prevenção e leitura do risco": 25
- "Medidas de redução e planejamento territorial": 25
- "Preparação, resposta e recuperação resiliente": 25
- "Leitura de prova: reescrita e cobrança da IDECAN": 25

Já existem (não repita):
- Qual enunciado distingue corretamente ameaça de risco de desastre?
- Qual alternativa apresenta o conjunto de frentes previsto para a PNPDEC?
- Como a Lei 12.608/2012, após alteração de 2023, trata a incerteza sobre a existência de ri
- Um distrito cresce em uma encosta que permanece sujeita ao mesmo padrão de chuva, mas a oc
- Após um deslizamento anterior, qual ação reduz fatores do risco antes de uma nova ocorrênc
- A política nacional deve integrar ações de defesa civil a quais campos de planejamento?
- Qual unidade deve orientar a análise preventiva de desastres relacionados a cursos d’água?
- Qual par classifica corretamente uma galeria de drenagem e um zoneamento que veda novas ca
- Um município impede loteamento em várzea ainda desocupada e reassenta famílias de um talud
- Uma comissão estima cheia recorrente, define recursos, atores, rotas e respostas antes do 
- Qual medida não estrutural reduz a criação de novas moradias expostas a inundação?
- Por que uma obra de drenagem em um município deve ser avaliada no contexto da bacia hidrog
- Qual conjunto representa medidas não estruturais úteis ao planejamento de risco?
- Um novo loteamento é proposto em terreno sem moradias, mas mapas indicam enxurradas e uma 
- Uma contenção protege casas existentes, mas pode deslocar água para a margem oposta, então
- Qual par descreve corretamente dois modos complementares de reduzir risco?
- Famílias já instaladas em área de risco alto recebem moradia definitiva em local seguro e 
- Um laudo ainda diverge sobre a probabilidade de ruptura de um talude ocupado, mas a previs
- Após novos mapas apontarem áreas de risco alto e muito alto, qual combinação reflete atrib
- Após uma enxurrada, a equipe precisa registrar o tipo oficial do desastre e planejar rotas
- Qual prioridade do Marco de Sendai trata de conhecer ameaças, exposição, vulnerabilidades 
- Qual combinação faz o alerta antecipado resultar em preparação comunitária efetiva?
- Que finalidade têm os exercícios simulados previstos para a proteção comunitária?
- Uma sirene alcança a parte baixa do povoado, mas a rota única cruza uma ponte vulnerável e
- Depois de uma inundação, uma escola é reconstruída em terreno seguro, com acesso inclusivo
- Qual reescrita preserva o sentido de que preparação acontece antes do impacto?
- Uma alternativa afirma que toda reconstrução deve reproduzir exatamente as condições anter
- Uma assertiva diz que a COBRADE mede vulnerabilidade de cada morador e decide a rota de fu
- Um item afirma que o risco só pode ser reduzido por obras e que o município deve agir apen
- Qual alternativa corrige uma frase que define resposta como toda ação definitiva de restau
- O risco de desastre considera a probabilidade de perdas quando uma ameaça alcança pessoas 
- A incerteza científica sobre um cenário de desastre impede a adoção de medidas preventivas
- A PNPDEC abrange prevenção, mitigação, preparação, resposta e recuperação em abordagem int
- A PNPDEC usa a bacia hidrográfica como unidade de análise da prevenção ligada a corpos d’á
- Impedir novos loteamentos em área ainda desocupada corrige risco existente, enquanto reass
- Zoneamento preventivo, mapas e planos de contingência são exemplos de medidas não estrutur
- Uma mensagem de alerta isolada completa a preparação mesmo sem rotas conhecidas, responsáv
- A COBRADE padroniza tipos de desastre para registro e comunicação, mas não substitui mapa 
- Reconstruir melhor significa devolver à comunidade exatamente o cenário anterior, pois tod
- Na recuperação, restaurar serviços e reduzir vulnerabilidades existentes podem ser objetiv
- Como a incerteza do modelo e o aumento de ocupação coexistem, o gestor deve aguardar dano 
- Uma obra física pode reduzir impactos em área já ocupada, enquanto zoneamento e monitorame
- A prioridade de Sendai de preparação para resposta eficaz torna dispensáveis governança lo
- Busca e salvamento são ações imediatas de resposta, ao passo que reconstruir a mesma morad
- O município pode produzir alertas antecipados de forma isolada, sem articulação com União 
```

## Pedido 27: Rotinas contábeis no setor público: conciliações, restos a pagar e encerramento (`contabilidade-publica.rotinas-conciliacoes-encerramento`, 9 tópicos de edital)

```
Matéria: contabilidade-publica.rotinas-conciliacoes-encerramento (Rotinas contábeis no setor público: conciliações, restos a pagar e encerramento)
Capítulos e quantas questões quero em cada um (total 100):
- "Conciliação bancária, de contas e controle de saldos": 17
- "Restos a pagar: inscrição, processamento, execução e baixa": 17
- "Despesas de exercícios anteriores e reflexos patrimoniais": 17
- "Duodécimo, gestão da Câmara e restrições fiscais": 17
- "Encerramento do exercício, ajustes e prestação de contas": 16
- "RREO, RGF e conferências fiscais do encerramento": 16

Já existem (não repita):
- Na conciliação bancária, qual documento é usado como evidência externa principal para conf
- Uma despesa foi empenhada e liquidada em dezembro, mas permaneceu sem pagamento em 31 de d
- Assinale a afirmativa correta a respeito de despesa de exercício anterior (DEA).
- Segundo a Constituição, os recursos duodecimais devidos aos órgãos abrangidos pelo art. 16
- Qual relatório possui periodicidade geral quadrimestral no regime da Lei de Responsabilida
- No encerramento do exercício, qual providência está correta em relação a um saldo real de 
- Em 31 de dezembro, há empenho de R$ 9.000 relativo a material ainda não entregue nem liqui
- A divulgação do Relatório Resumido da Execução Orçamentária (RREO) segue, como regra, o pe
- O razão bancário aponta R$ 45.000 e o extrato, R$ 43.800. Consta ordem de pagamento de R$ 
- Uma entidade reconheceu corretamente, no ano X, VPD e obrigação de R$ 15 mil por serviço c
- Ao pagar no ano X+1 um RP processado regularmente inscrito no encerramento de X, o órgão d
- Uma Câmara Municipal pretende transferir a um fundo os saldos financeiros recebidos por du
- Sobre lançamento de encerramento, identifique a afirmação tecnicamente correta.
- A unidade possui caixa bruto de R$ 160 mil de uma fonte vinculada e obrigações financeiras
- Um fornecedor reivindica obrigação de exercício encerrado que não foi processada na época 
- No último quadrimestre, o Relatório de Gestão Fiscal deve trazer, entre outras informações
- No encerramento, uma prestação de serviço foi efetivamente recebida e gera obrigação, embo
- Sobre o limite da Câmara Municipal, analise a alternativa correta, considerando a Constitu
- Ao conferir fornecedores, o auditor encontra saldo contábil sem fatura comprobatória e not
- Uma obrigação patrimonial por compra de equipamento foi reconhecida no exercício X, mas o 
- No RGF, apuração por fonte vinculada mostra disponibilidade líquida de R$ 65 mil antes da 
- Em exercício encerrado, a equipe constata omissão material de obrigação já exigível no ano
- Um RP não processado de X é liquidado em X+1 após comprovação de entrega, e depois pago. Q
- Um município com 40 mil habitantes decidiu exercer opção legal de periodicidade fiscal. O 
- Para fins de restos a pagar, despesa empenhada e liquidada, mas não paga ao fim do exercíc
- Conciliação bancária consiste exclusivamente em fazer o saldo do razão coincidir com o ext
- Pela LRF, o RREO é divulgado bimestralmente, em regra, até trinta dias após o encerramento
- O art. 168 da Constituição determina repasses duodecimais até o dia vinte de cada mês.
- Se uma obrigação de exercício anterior já foi inscrita validamente em restos a pagar, seu 
- Ao encerrar o exercício, a entidade pública deve eliminar dos livros todo saldo de bancos 
- O RGF é emitido, como regra geral, ao final de cada quadrimestre.
- Uma ordem de pagamento registrada na contabilidade e ainda não debitada no banco pode ser 
- O cancelamento de um empenho ou de um RP extingue automaticamente qualquer direito materia
- Quando uma DEA corresponde a serviço consumido e a VPD já foi corretamente reconhecida no 
- O saldo financeiro de recursos duodecimais pode ser restituído ao caixa único do Tesouro d
- A revisão de encerramento deve abranger inventários, ajustes de competência e conciliações
- O saldo total da conta bancária de um órgão pode ser usado indistintamente para justificar
- No último período fiscal do ano, o RGF inclui informações sobre disponibilidade de caixa e
- Por ainda não terem sido liquidados formalmente, todos os RPNP são necessariamente desprov
- A aquisição de bem permanente que se enquadre como ativo pode gerar registro patrimonial d
- O teto global de despesa da Câmara Municipal e o teto de gasto com folha previstos no art.
- A constatação de dívida legítima omitida no exercício anterior impõe avaliar a correção co
- É vedada, em qualquer hipótese, a divulgação semestral do Relatório de Gestão Fiscal, porq
- O art. 42 da LRF restringe, nos últimos dois quadrimestres do mandato, a contratação de ob
- Ao encontrar divergência entre extrato e razão, o conciliador deve sempre lançar o valor d
```

## Pedido 28: Controle de Cheias (`minas-gerais.controle-cheias`, 9 tópicos de edital)

```
Matéria: minas-gerais.controle-cheias (Controle de Cheias)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Cheias, inundações, enxurradas, alagamentos e risco": 12
- "2. Medidas de controle: estruturais, não estruturais, intensivas e extensivas": 22
- "3. Reservatórios de cheias, detenção, retenção e macrodrenagem": 27
- "4. Planejamento territorial e proteção em cidades ribeirinhas": 17
- "5. Monitoramento, alertas, contingência e risco residual": 22

Já existem (não repita):
- Após chuvas em toda a bacia, um rio sobe e extravasa para uma planície ribeirinha que norm
- Depois de chuva intensa, uma rua rebaixada, longe das margens fluviais, acumula água porqu
- Uma chuva concentrada em pequeno vale inclinado produz fluxo veloz com pedras e galhos des
- Duas comunidades enfrentam a mesma cheia, mas uma possui moradias frágeis e pouca possibil
- O município decide implantar regra de uso do solo que proíbe novos loteamentos em planície
- Uma cidade constrói dique ao longo de trecho de rio para dificultar a entrada de água dura
- Uma bacia artificial recebe águas de chuvas fortes e libera o conteúdo gradualmente, fican
- Uma estrutura mantém lago permanente e dispõe de espaço adicional para receber parte da ág
- Uma obra coleta água de vias por bocas de lobo e pequenas galerias antes da chegada ao can
- Antes do período chuvoso, equipes definem responsáveis, rotas de saída, abrigos e exercíci
- Depois de pavimentação e retirada de áreas vegetadas na parte superior da bacia, o escoame
- Uma cidade pretende reduzir danos com obras de infiltração distribuídas, reflorestamento e
- O alargamento de trecho urbano permite maior passagem de água, mas o bairro abaixo dele re
- O reservatório de controle recebe grande entrada de água durante uma tempestade e descarre
- Uma bacia de detenção sofre ocupação de seu fundo seco por estruturas temporárias durante 
- Uma área de várzea ainda não construída será destinada a parque linear e espaço de expansã
- Uma comunidade está próxima a rio com alta suscetibilidade a transbordamento, mas não há r
- Uma estação mede elevação de nível a montante e um sistema envia aviso compreensível às fa
- Um bairro recebeu um dique que impede transbordamentos moderados, mas ruas internas passar
- Uma bacia de retenção foi implantada em bairro ribeirinho e já possui volume de água perma
- Uma prefeitura limpa galerias e bocas de lobo de todo um bairro, mas o rio receptor perman
- Uma obra de canalização acelera o escoamento num município; logo abaixo, outro território 
- Em um vale mineiro, a chuva forte ocorreu principalmente nas cabeceiras durante a noite. P
- Há alerta hidrológico para área ribeirinha de alta exposição. O trajeto habitual para o ab
- Após obra de reservação, moradores passaram a acreditar que o bairro estava definitivament
- Um rio pode experimentar cheia, com elevação de vazão ou nível, sem que ocorra inundação d
- O alagamento urbano, por definição, depende de transbordamento de grande rio sobre sua pla
- Escoamento superficial concentrado e veloz, com arraste de detritos após chuva intensa em 
- A elaboração de mapa de risco e a fiscalização da ocupação urbana são obras estruturais de
- Uma bacia de detenção pode armazenar temporariamente a água da chuva e ficar sem lâmina de
- Uma bacia de retenção mantém água apenas durante chuvas e, por definição, deve secar integ
- Construir jardins de chuva e estruturas de infiltração em diferentes bairros pode ser ação
- Alargar a calha de um curso d'água garante que nenhum bairro a jusante receberá escoamento
- Reservatórios destinados a amortecimento de cheias podem reduzir e retardar o pico de vazã
- Por proteger margens contra erosão, o reflorestamento da bacia assegura que eventos extrem
- Mesmo em município sem chuva no momento, o nível de um rio pode seguir aumentando devido à
- O mapeamento de áreas fisicamente suscetíveis à inundação, sem qualquer informação sobre m
- Um reservatório apresenta volume parcialmente ocupado por água permanente e assoreamento. 
- Um dique de proteção fluvial elimina a possibilidade de alagamentos atrás de sua estrutura
- A limpeza da microdrenagem pode reduzir alagamentos localizados, mas pode não impedir inun
- As ações de fiscalização territorial e prevenção de novas ocupações em área perigosa só po
- Um plano de contingência pode ser testado por simulações de evacuação, verificando comunic
- Uma estação indicar aumento da cota fluvial significa, por si só, que a vazão volumétrica 
- Após implantação de obra de reservação, ainda se justifica monitorar chuvas e rios, manter
- Quando um reservatório urbano retarda a chegada do pico de cheia a jusante, isso comprova 
```

## Pedido 29: Guardas municipais e segurança cidadã (`seguranca-publica.guarda-municipal`, 9 tópicos de edital)

```
Matéria: seguranca-publica.guarda-municipal (Guardas municipais e segurança cidadã)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos constitucionais e jurisprudência": 17
- "Princípios e competências da Lei nº 13.022/2014": 17
- "Criação, ingresso, capacitação e controles": 17
- "Uso diferenciado da força, abordagem e busca pessoal": 17
- "Direitos humanos e atendimento responsável": 16
- "Prevenção, segurança escolar e mediação comunitária": 16

Já existem (não repita):
- Todo Município brasileiro é obrigado pela Constituição Federal a criar uma guarda municipa
- A Lei nº 13.022/2014 considera a guarda municipal instituição de caráter civil, com missão
- Os bens dominiais municipais estão excluídos do alcance da competência geral de proteção d
- O STF admite a atuação das guardas no policiamento ostensivo e comunitário, mas exclui exp
- O patrulhamento preventivo figura entre os princípios mínimos de atuação das guardas munic
- O simples uso de uniforme da guarda municipal atribui ao servidor todas as competências le
- O atendimento em flagrante pela guarda municipal, por si só, não lhe confere autorização p
- A guarda municipal pode ser instituída permanentemente por ordem verbal do prefeito, sem l
- Uma guarda municipal que utilize armas de fogo necessita de corregedoria, mesmo que tenha 
- A ouvidoria independente da direção da guarda municipal só é exigida quando há efetivo sup
- O Código de Conduta de uma guarda municipal pode ser substituído por regulamento disciplin
- O princípio do uso progressivo obriga o agente a aumentar a força em sucessivas etapas, me
- A Lei nº 13.060/2014 exige, entre outros critérios, legalidade, necessidade, razoabilidade
- Atirar em pessoa que foge desarmada e sem risco imediato de morte ou lesão é medida legíti
- É absolutamente inválida qualquer busca pessoal realizada por guarda municipal, mesmo com 
- Usar exclusivamente a aparência ou a condição econômica de alguém para justificar busca pe
- Se o uso da força provocar ferimento, deve ser prestado socorro imediato ainda que a inter
- Quando um conflito envolve agressão grave, guardas podem impor acordo de mediação e dispen
- A segurança escolar preventiva pode abranger ações educativas para cultura de paz com alun
- O controle externo da atividade policial pelo Ministério Público, afirmado pelo STF, equiv
- Qual alternativa descreve a competência geral das guardas prevista no art. 4º do Estatuto?
- No Tema 656, qual atividade foi expressamente excluída da atuação constitucional das guard
- Uma guarda municipal possui apenas dez servidores. Qual controle institucional é obrigatór
- A atuação municipal denominada prevenção primária da violência prioriza:
- Quanto à natureza jurídica, a guarda municipal é:
- Uma guarda municipal de 22 integrantes inicia uso de armas de fogo. Qual consequência prev
- Durante patrulhamento, a equipe cogita revista coercitiva sem mandado. Qual enunciado deve
- Ao presenciar infração penal em flagrante durante evento municipal, a guarda deve, na form
- No emprego progressivo da força, a tentativa de negociação não resolveu a ocorrência. Qual
- A respeito dos mecanismos de controle das guardas municipais, assinale a correta.
- Qual atuação de segurança escolar se harmoniza com o art. 5º, XVIII, da Lei nº 13.022/2014
- Um Município deseja instituir guarda municipal permanente apenas por decreto. Segundo a le
- Uma pessoa desarmada foge da guarda sem representar risco imediato de morte ou lesão. Conf
- O Decreto nº 11.841/2023, em conjunto com o Estatuto, admite que as guardas:
- Em conflito de vizinhança há grave ameaça contra uma das partes. A conduta mais adequada é
- Sobre a escolaridade exigida para ingresso, de acordo com a Lei nº 13.022/2014:
- Qual conclusão concretiza a não discriminação no uso da força previsto no Decreto nº 12.34
- Um uso legítimo de força produziu ferimento. O que exige o ordenamento federal?
- A respeito do regime institucional das guardas, qual enunciado é compatível com a Lei nº 1
- Acerca do julgamento do Tema 656 pelo STF, qual alternativa expressa corretamente seu alca
```

## Pedido 30: Drenagem urbana e manejo de águas pluviais (`engenharia.engenharia-sanitaria-drenagem`, 9 tópicos de edital)

```
Matéria: engenharia.engenharia-sanitaria-drenagem (Drenagem urbana e manejo de águas pluviais)
Capítulos e quantas questões quero em cada um (total 100):
- "Hidrologia urbana, bacias e chuvas de projeto": 17
- "Microdrenagem, macrodrenagem e galerias pluviais": 17
- "Dimensionamento hidrológico e hidráulico": 17
- "Controle de cheias, risco e planejamento de bacias": 17
- "Drenagem sustentável, qualidade da água e manutenção": 16
- "Norma ANA nº 12/2025, responsabilidades e gestão do serviço": 16

Já existem (não repita):
- A substituição de superfícies permeáveis por pavimentos hidraulicamente conectados à rede 
- Uma chuva com tempo de retorno de dez anos só poderá ocorrer uma vez em cada década, sob p
- Em método racional, é usual adotar a duração de chuva igual ao tempo de concentração da ba
- Para a mesma localidade e período de retorno, a intensidade média de chuva adotada em curv
- A capacidade hidráulica de uma galeria subterrânea dispensa verificar a capacidade de inte
- O nível elevado de um córrego receptor pode impor remanso à saída de uma galeria pluvial e
- Microdrenagem e macrodrenagem são distinguidas nacionalmente, em qualquer município, por u
- Quando a área de contribuição é informada em hectares, o método racional usa Q = 0,00278·C
- O método racional produz, diretamente e sem hipóteses adicionais, todo o hidrograma de ent
- Na expressão de Manning para escoamento uniforme, o raio hidráulico corresponde à área mol
- Se, em certo instante, a vazão que entra em um reservatório é maior que a que sai, o volum
- Reservatórios de detenção e retenção possuem obrigatoriamente o mesmo regime de armazename
- A canalização que acelera as águas em um trecho pode agravar picos a jusante, por isso a a
- Uma obra calculada para determinado tempo de retorno elimina o risco de inundação por chuv
- Jardins de chuva e biovaletas podem contribuir para reduzir o escoamento e melhorar a qual
- O escoamento pluvial proveniente de vias urbanas é, por definição, livre de contaminantes 
- A primeira carga de lavagem é uma tendência possível de maior concentração de poluentes no
- A NR ANA nº 12/2025 foi aprovada pela Resolução ANA nº 245, de 17 de março de 2025, e trat
- Segundo a NR ANA nº 12/2025, a regulação infranacional é dispensável sempre que o titular 
- A NR ANA nº 12/2025 instituiu automaticamente uma tarifa de drenagem de valor idêntico e c
- O fenômeno que explica o aumento da vazão de pico após pavimentação de uma bacia é, princi
- Em uma curva IDF, mantidos a duração e o local, o aumento do tempo de retorno tende a resu
- Considerando T = 20 anos e modelo de excedências anuais estacionário, a chance anual aprox
- Para selecionar chuva de projeto em município diferente do local estudado, a conduta tecni
- Uma bacia inclui telhados ligados a jardins e telhados ligados diretamente a tubulações; o
- Na microdrenagem urbana, o dispositivo que capta água da sarjeta e a encaminha à rede é a:
- Em inspeção de rede, observa-se que uma galeria não descarrega adequadamente enquanto o ri
- Em uma rede separadora, identifica-se despejo contínuo de esgoto em galeria de águas pluvi
- Projeto de galeria propõe tubo de maior diâmetro, mas mantém boca de lobo subdimensionada;
- Para planejamento de macrodrenagem, a abordagem mais adequada consiste em:
- Uma sub-bacia de 20 ha apresenta C = 0,60 e i = 90 mm/h; pelo método racional, a vazão é a
- No emprego da equação de Manning, a definição correta de raio hidráulico é:
- Uma chuva de 40 mm incide sobre área de 10 ha; admitindo coeficiente volumétrico de escoam
- Para avaliar o armazenamento de uma bacia com hidrograma variável, a informação essencial 
- O manual da Adasa de 2023 usa o método racional em regra para bacias até 100 ha; a conclus
- Reservatório recebe 6 m³/s e descarrega 4 m³/s por 300 segundos, em valores constantes; a 
- Um reservatório com espelho d’água permanente e função de amortecer picos corresponde, na 
- Uma medida não estrutural de gestão de cheias é:
- Uma obra de canalização acelerou o hidrograma afluente e aumentou a cheia no bairro de jus
- A proposta que trata corretamente o risco residual de uma obra de drenagem é:
- Qual solução tende a integrar vegetação, tratamento e infiltração do escoamento superficia
- Na qualidade da água pluvial urbana, o termo poluição difusa descreve:
- Em área com suspeita de solo contaminado e aquífero vulnerável, o uso de trincheira infilt
- No projeto de tratamento de águas pluviais conforme a NR ANA 12/2025, deve-se considerar:
- Para reduzir falhas recorrentes em jardins de chuva por deposição de sedimentos, a providê
- A Resolução ANA nº 245, de 17 de março de 2025, aprovou:
- Qual conjunto representa as infraestruturas do art. 7º da NR ANA 12/2025?
- A responsabilidade de delegar a regulação dos serviços de DMAPU a entidade infranacional, 
- Ao elaborar um plano municipal de DMAPU nos termos da NR 12/2025, é pertinente:
- Sobre instrumentos de cobrança do serviço de DMAPU, a interpretação correta da NR ANA 12/2
```

## Pedido 31: Processo Administrativo da Bahia (Lei nº 12.209/2011) (`legislacao.processo-administrativo-bahia-lei-12209`, 9 tópicos de edital)

```
Matéria: legislacao.processo-administrativo-bahia-lei-12209 (Processo Administrativo da Bahia (Lei nº 12.209/2011))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Abrangência, princípios, administrados e atos processuais": 20
- "2. Início, instrução, decisão e extinção": 20
- "3. Prazos, comunicação, recursos, competência e impedimentos": 20
- "4. Processo eletrônico e processo sancionatório": 20
- "5. Outros processos especiais: justificação, invalidação, reparação, seleção e cautelares": 20

Já existem (não repita):
- Segundo a Lei nº 12.209/2011, suas disposições aplicam-se diretamente:
- Para a Lei nº 12.209/2011, processo administrativo é a relação jurídica que se traduz em p
- Na Lei nº 12.209/2011, entidade é definida como:
- Entre os princípios expressamente listados no art. 3º da Lei nº 12.209/2011, estão a ofici
- Pela Lei nº 12.209/2011, a norma administrativa deve ser interpretada da forma que melhor 
- Segundo a Lei nº 12.209/2011, é assegurada prioridade de tramitação ao processo em que o p
- Pela Lei nº 12.209/2011, o administrado tem o direito de fazer-se assistir, facultativamen
- De acordo com a Lei nº 12.209/2011, inexistindo disposição específica, os atos da autorida
- Segundo a Lei nº 12.209/2011, a atuação de associação como postulante depende de comprovaç
- Pela Lei nº 12.209/2011, o processo administrativo inicia-se:
- Constatado que o requerimento não atende aos requisitos legais, o postulante será intimado
- Sobre a instrução do processo na Lei nº 12.209/2011, é correto afirmar que:
- Pela Lei nº 12.209/2011, a participação em consulta pública confere ao terceiro a condição
- Segundo a Lei nº 12.209/2011, quando os elementos colhidos puderem agravar a situação jurí
- Na Lei nº 12.209/2011, a Administração que deixa de emitir decisão final responde pelos pr
- Pela Lei nº 12.209/2011, havendo vários postulantes, a desistência formulada por um deles:
- Segundo a Lei nº 12.209/2011, os atos administrativos ilegais de que decorram efeitos favo
- Pela Lei nº 12.209/2011, é permitida a convalidação de ato cuja legalidade já tenha sido i
- De acordo com a Lei nº 12.209/2011, a autoridade julgadora deve decidir no prazo de 30 dia
- Pela Lei nº 12.209/2011, nos processos que envolvam licitações e contratos, os pronunciame
- Segundo a Lei nº 12.209/2011, os prazos começam a correr a partir do primeiro dia útil apó
- Na Lei nº 12.209/2011, a notificação e a intimação feitas por edital consideram-se efetiva
- Pela Lei nº 12.209/2011, a notificação é condição de validade do processo administrativo, 
- Segundo a Lei nº 12.209/2011, o ato de comunicação será obrigatoriamente pessoal quando:
- Sobre o recurso hierárquico na Lei nº 12.209/2011, é correto afirmar que:
- Pela Lei nº 12.209/2011, requerida a concessão de efeito suspensivo ao recurso, a autorida
- Segundo a Lei nº 12.209/2011, o pedido de reconsideração é admitido:
- Pela Lei nº 12.209/2011, o julgamento do recurso não pode agravar a situação do recorrente
- Segundo a Lei nº 12.209/2011, é indelegável:
- Pela Lei nº 12.209/2011, a avocação temporária de competência de órgão hierarquicamente in
- Na Lei nº 12.209/2011, configura causa de suspeição, e não de impedimento, o servidor que:
- Segundo a Lei nº 12.209/2011, quando a petição eletrônica for enviada para atender prazo p
- Pela Lei nº 12.209/2011, em relação ao administrado credenciado, inexistindo confirmação d
- Na Lei nº 12.209/2011, a sindicância investigativa, instaurada quando não há elementos suf
- Pela Lei nº 12.209/2011, o prazo para conclusão do processo sancionatório, com decisão fin
- Segundo a Lei nº 12.209/2011, ressalvada legislação específica, o prazo prescricional para
- No processo sancionatório da Lei nº 12.209/2011, o acusado é notificado para apresentar de
- Pela Lei nº 12.209/2011, a decisão da autoridade julgadora no processo sancionatório fica 
- Segundo a Lei nº 12.209/2011, no processo de justificação administrativa, o requerimento d
- Pela Lei nº 12.209/2011, nas indenizações pagas em processo administrativo de reparação de
- Segundo a Lei nº 12.209/2011, no processo seletivo para cargo, emprego ou função pública:
- Pela Lei nº 12.209/2011, a pesquisa sobre a conduta e a vida pregressa do candidato só pod
- Segundo a Lei nº 12.209/2011, as medidas cautelares inominadas:
```

## Pedido 32: Romantismo (`literatura.romantismo`, 9 tópicos de edital)

```
Matéria: literatura.romantismo (Romantismo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Contexto histórico, projeto nacional e linguagem romântica": 32
- "2. Primeira e segunda gerações poéticas: indianismo e ultrarromantismo": 17
- "3. Terceira geração: poesia social, abolicionismo e representação negra": 17
- "4. Prosa romântica: folhetim, nacionalismo, costumes e personagens": 17
- "5. Teatro romântico, identidades e interpretação comparada": 17

Já existem (não repita):
- Um estudante identifica a publicação de 1836 convencionalmente usada como marco do Romanti
- Um poema apresenta a terra natal como lugar superior ao espaço estrangeiro e manifesta sau
- Uma leitura escolar destaca o indígena como guerreiro honrado e símbolo da pátria em const
- Um soneto descreve amor inalcançável, solidão, fantasia e desejo de desaparecer. Qual enqu
- Uma composição emprega apóstrofes, imagens de grandes proporções e indignação contra a esc
- Ao classificar Senhora, um leitor observa que dinheiro, casamento e prestígio interferem n
- Uma narrativa do século XIX circulou em capítulos sucessivos de jornal, terminando cada pa
- Um quadro de estudos associa corretamente um romance de José de Alencar à dupla de persona
- O narrador de um romance usa humor para acompanhar Leonardo, figura de trajetória pouco he
- Uma peça cômica satiriza estratégias familiares e interesses materiais por meio de diálogo
- Um material chama de 'primeira geração' os romances urbanos publicados na década de 1840 e
- Em Senhora, Aurélia adquire poder econômico e participa da negociação matrimonial. Que int
- Ao comparar a representação da escravidão em Castro Alves e no romance Úrsula, uma pesquis
- Uma voz lírica diz 'minha lembrança recria o lugar perdido'. Um candidato conclui que o po
- Um leitor afirma que as três gerações poéticas representam compartimentos fechados e que G
- Um professor pede análise de I-Juca-Pirama e da função de honra e vínculo familiar na cara
- Uma interpretação de Lira dos vinte anos encontra tanto idealização amorosa quanto cenas i
- Ao relacionar o índio na poesia de Gonçalves Dias e no romance Iracema, um estudo identifi
- Um poema usa perguntas indignadas, símbolos de grande escala e invocação da liberdade. A l
- Uma questão coloca lado a lado a Independência de 1822, a literatura patriótica das década
- Em Memórias de um sargento de milícias, Leonardo não encarna o guerreiro moralmente exempl
- Uma turma compara a representação do sofrimento escravista em O navio negreiro e Úrsula. Q
- Uma peça ambientada em família do século XIX utiliza falas e situações cômicas para critic
- Um pesquisador interpreta o sucesso do romance romântico observando capítulos de jornais, 
- Uma descrição idealiza a natureza nacional e constrói personagem indígena heroica, mas a o
- O lançamento de Suspiros poéticos e saudades, de Gonçalves de Magalhães, em 1836, é consid
- No estudo da poesia romântica, a geração ultrarromântica é identificada primordialmente pe
- Na poesia indianista de Gonçalves Dias, a figura indígena pode ser construída como símbolo
- Senhora, romance de José de Alencar, pertence à produção indianista e narra a relação de I
- Castro Alves é referência da terceira geração romântica, na qual recursos retóricos grandi
- Uma narrativa publicada em folhetim passa automaticamente a integrar a modalidade indianis
- O repertório temático de Gonçalves Dias vai além do indianismo e inclui poesia amorosa, re
- Em Lira dos vinte anos, de Álvares de Azevedo, a presença de ironia impede o reconheciment
- O navio negreiro deve ser interpretado como depoimento autobiográfico literal de seu autor
- Os romances urbanos do período romântico podem expor a relação entre casamento, patrimônio
- O protagonista Leonardo, de Memórias de um sargento de milícias, é construído segundo o me
- O romance Úrsula, de Maria Firmina dos Reis, amplia as possibilidades de estudar escravidã
- A divisão em três gerações do Romantismo brasileiro deve ser aplicada indistintamente às p
- A idealização de um indígena em romance romântico constitui evidência suficiente para desc
- O teatro de costumes pode utilizar situações cômicas para questionar comportamentos e inte
- Uma comparação rigorosa entre poesia de Castro Alves e Úrsula deve considerar as diferença
- Por estar associado ao ano de 1836, o Romantismo brasileiro já reunia nesse momento toda a
- O uso de uma natureza idealizada como símbolo da pátria pode coexistir, em uma narrativa i
- Na interpretação da poesia, basta que uma voz mencione morte ou saudade para classificar o
- A trajetória de Aurélia em Senhora permite examinar uma personagem feminina com capacidade
```

## Pedido 33: Hidrodinâmica naval: resistência, propulsão e comportamento no mar (`engenharia-naval.hidrodinamica-resistencia-comportamento-mar`, 9 tópicos de edital)

```
Matéria: engenharia-naval.hidrodinamica-resistencia-comportamento-mar (Hidrodinâmica naval: resistência, propulsão e comportamento no mar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Semelhança, modelos e camada limite": 26
- "2. Perfis, asas, resistência e propulsão": 33
- "3. Corpo rígido, ondas e teoria espectral": 41

Já existem (não repita):
- O número de Froude, Fn = V/√(gL), está associado à resistência de ondas, enquanto o número
- Em ensaios de modelos de navios em tanque de provas com água, não é possível igualar simul
- Um navio de 200 m será ensaiado com um modelo na escala 1:25. Para manter o mesmo número d
- Pelo método de Froude, o coeficiente de resistência residual obtido no ensaio do modelo, n
- A linha de correlação modelo-navio ITTC-1957 expressa o coeficiente friccional como:
- O método ITTC-1978 introduz o fator de forma (1 + k), que multiplica o coeficiente friccio
- Nos modelos de navio, usam-se estimuladores de turbulência na proa para:
- Na camada limite, a velocidade do fluido junto à superfície do casco é nula, em razão da c
- A separação da camada limite, que gera esteira turbulenta e resistência de pressão viscosa
- A camada limite turbulenta produz menor atrito que a laminar e, por isso, separa mais cedo
- Pela teoria do perfil delgado, em envergadura infinita, o coeficiente de sustentação varia
- Pelo teorema de Kutta-Joukowski, a sustentação por unidade de envergadura de um perfil é p
- O estol de um perfil hidrodinâmico, como a pá de um leme, caracteriza-se por:
- Em uma asa de envergadura finita, o arrasto induzido diminui quando se aumenta a razão de 
- O arrasto induzido de uma asa finita decorre principalmente:
- Em navios lentos e de formas cheias, como petroleiros e graneleiros, a parcela predominant
- O bulbo de proa reduz a resistência de ondas na faixa de velocidade de projeto ao gerar um
- As corcovas e os cavados observados na curva de resistência de ondas em função da velocida
- A potência efetiva de um navio é o produto da resistência total ao avanço pela velocidade,
- Sobre os coeficientes propulsivos, assinale a alternativa correta.
- O coeficiente de esteira (w) indica que o hélice trabalha numa região em que a água é arra
- Um navio tem resistência total de 500 kN a 10 m/s. Se o coeficiente propulsivo é 0,625, a 
- A rugosidade e a incrustação do casco aumentam principalmente a resistência friccional do 
- Dos seis graus de liberdade de um navio, o movimento de rotação em torno do eixo longitudi
- Entre os movimentos do navio, apenas afundamento, balanço e caturro possuem força ou momen
- Na modelagem dos movimentos do navio em ondas, o termo de massa adicional representa:
- Pela teoria linear de ondas, em águas profundas, uma onda regular com período de 10 s tem 
- Em águas profundas, as partículas de água de uma onda regular descrevem órbitas circulares
- A altura significativa de ondas (H1/3) corresponde:
- O espectro de Pierson-Moskowitz representa um mar plenamente desenvolvido em oceano aberto
- Na teoria espectral linear, o espectro de resposta de um movimento do navio é obtido:
- Um navio que navega com mar de proa encontra as ondas com frequência maior que a frequênci
- NÃO é critério usualmente adotado na avaliação do comportamento do navio em ondas:
- O balanço do navio pode atingir grandes amplitudes quando a frequência de encontro das ond
- Assinale a alternativa que associa corretamente o movimento e o eixo.
- As equações gerais do movimento de um corpo rígido aplicadas ao navio combinam a segunda l
- Na teoria espectral linear, o RAO (operador de amplitude de resposta) expressa a razão ent
- A resistência de pressão viscosa (ou de forma) de um casco está diretamente associada:
- Uma onda em águas profundas quebra quando sua esbeltez (razão entre altura e comprimento) 
- Para reduzir o balanço de um navio em ondas, NÃO é recurso adequado:
```

## Pedido 34: Medicina do trabalho e perícias ocupacionais (`medicina.medicina-do-trabalho-pericias`, 8 tópicos de edital)

```
Matéria: medicina.medicina-do-trabalho-pericias (Medicina do trabalho e perícias ocupacionais)
Capítulos e quantas questões quero em cada um (total 100):
- "PCMSO, exames ocupacionais e vigilância médica": 20
- "Nexo causal, NTEP, CAT e documentação": 20
- "Doenças ocupacionais por sistemas e LER/DORT": 20
- "Toxicologia ocupacional: avaliação, biomarcadores e prevenção": 20
- "Perícias, capacidade laboral, readaptação e reabilitação profissional": 20

Já existem (não repita):
- Uma empresa revisou o PGR e identificou exposição a vapores no setor de limpeza industrial
- Um empregado muda de setor para atividade que acrescenta exposição relevante a poeira mine
- O PCMSO deve ser planejado a partir dos riscos ocupacionais identificados e classificados,
- Em uma auditoria, a equipe encontrou ASOs guardados junto a laudos com diagnósticos acessí
- Uma empresa sustenta que o PCMSO é suficiente para neutralizar ruído intenso porque realiz
- O ASO substitui o prontuário médico e permite que a empresa consulte livremente o diagnóst
- Qual ação exemplifica uso apropriado do relatório analítico do PCMSO?
- Uma servidora de empresa pública regida pela CLT sofre acidente típico na terça-feira, sem
- A NR-7 prevê exame de mudança de riscos ocupacionais, distinto de uma simples mudança de t
- A empresa omite a CAT de doença ocupacional suspeita. Qual afirmação expressa a alternativ
- Para identificar especificamente um possível NTEP, qual combinação é analisada?
- Uma CAT emitida e registrada garante, por si só, concessão imediata de benefício previdenc
- Em ação trabalhista, o perito identifica provável contribuição da exposição a solvente par
- Uma banca afirma que todo CID incluído na Lista de Doenças Relacionadas ao Trabalho do Min
- Se a empresa não emitir a CAT, a pessoa acidentada ou o médico poderá realizar a comunicaç
- Uma trabalhadora apresenta dor no punho; o posto exige força de pinça contínua, posturas e
- Em trabalhadores expostos a sílica cristalina respirável, qual agravo deve integrar a aval
- O NTEP se baseia no cruzamento do código da atividade econômica com o código da doença, e 
- Um profissional apresenta chiado que piora nas jornadas e já tinha asma desde a infância. 
- Qual achado favorece suspeita de neuropatia mediana no túnel do carpo, embora não determin
- A ausência de associação CID–CNAE na matriz do NTEP impede o reconhecimento de qualquer ne
- Um trabalhador tem perda auditiva e histórico de ruído ocupacional, mas também outras expo
- Um profissional de saúde contrai doença infecciosa após trabalho com contato com pacientes
- LER/DORT é expressão coletiva que pode abranger tendinopatias, neuropatias e síndromes dol
- Sobre a toxicologia ocupacional, qual enunciado diferencia corretamente risco e perigo?
- Uma empresa mede apenas concentração de solvente no ar e afirma que não existe exposição t
- O diagnóstico de síndrome do túnel do carpo, por si só, demonstra que todo trabalhador aco
- Pintor apresenta sintomas após contato com solvente, e amostra urinária é colhida em janel
- Após vazamento de gás de combustão, funcionário com cefaleia apresenta oximetria comum apa
- A silicose está relacionada à exposição à sílica cristalina respirável, especialmente quan
- Um laboratório industrial substitui um produto carcinogênico por alternativa menos perigos
- Trabalhador com doença musculoesquelética controlada mantém plena funcionalidade para sua 
- Em trabalhador com asma, sintomas que pioram na jornada e melhoram em afastamentos dispens
- Ao elaborar laudo judicial, qual conduta é mais adequada diante da ausência de registros a
- Qual elemento diferencia o auxílio-acidente de benefício destinado a incapacidade temporár
- Monitoramento biológico e monitoramento ambiental são complementares, porque investigam di
- Após longa licença por tendinopatia, a pessoa retorna ao mesmo posto com trabalho acima da
- Qual afirmação descreve melhor a reabilitação profissional previdenciária?
- Uma leitura usual de oximetria de pulso dentro da faixa habitual afasta com segurança into
- Em perícia de nexo para dermatite em profissional da limpeza, qual achado seria especialme
- A perita judicial identifica doença com nexo plausível, mas sem redução atual da aptidão p
- A hierarquia de prevenção favorece eliminar ou substituir agentes perigosos antes de depen
- Para uma possível exposição a benzeno, qual conjunto de preocupações é mais apropriado?
- Qual conduta exemplifica melhor avaliação funcional no retorno de empregado com limitação 
- Incapacidade para a ocupação habitual e presença de doença relacionada ao trabalho são con
- A perícia previdenciária e a perícia judicial trabalhista têm os mesmos destinatários e o 
- Uma alta previdenciária dispensa automaticamente a avaliação de riscos e adaptações exigid
- O programa de reabilitação profissional do INSS busca viabilizar reinserção, mas não garan
```

## Pedido 35: Química geral (`quimica.quimica-geral`, 8 tópicos de edital)

```
Matéria: quimica.quimica-geral (Química geral)
Capítulos e quantas questões quero em cada um (total 100):
- "Estrutura atômica, periodicidade e ligações químicas": 17
- "Funções inorgânicas, reações e estequiometria": 17
- "Soluções e unidades de concentração": 17
- "Equilíbrio químico, ácidos, bases e pH": 17
- "Termoquímica, cinética e eletroquímica": 16
- "Química orgânica básica: estruturas, funções e nomenclatura": 16

Já existem (não repita):
- Uma espécie de número atômico 11 e número de massa 23 forma um cátion monovalente. O conju
- Na comparação entre H₂O e CO₂, qual afirmação descreve corretamente a polaridade global?
- Qual configuração corresponde a um átomo neutro no estado fundamental com Z = 12?
- Considerando as tendências gerais da tabela periódica, qual alteração costuma reduzir o ra
- Entre as espécies seguintes, qual é predominantemente associada a ligação iônica em um sól
- Na reação 2H₂ + O₂ → 2H₂O, com 2 mol de H₂ e 2 mol de O₂, a quantidade máxima de água form
- Na reação Zn + Cu²⁺ → Zn²⁺ + Cu, qual espécie funciona como agente oxidante?
- Uma amostra de 50 g contém 80% em massa de CaCO₃. Usando massa molar 100 g/mol, a quantida
- Uma síntese tem produção teórica de 25 g e fornece 20 g de produto isolado. Seu rendimento
- Para formar 18 g de água segundo 2H₂ + O₂ → 2H₂O, com reagentes puros, que massa de O₂ é c
- Dissolver 0,50 mol de soluto até obter 2,0 L de solução fornece molaridade de:
- Para obter 250 mL de solução 0,20 mol/L a partir de estoque 1,0 mol/L, que volume do estoq
- Uma solução apresenta 10 g de soluto em 200 g de solução. Sua concentração em porcentagem 
- Uma solução de NaCl contém 5,85 g de soluto em volume final de 0,50 L. Se M(NaCl) = 58,5 g
- Qual é a unidade característica da molalidade?
- Em uma mistura de reação na qual Q < K, a tendência espontânea inicial é:
- A 25 °C, para uma solução aquosa ideal com [H₃O⁺] = 10⁻³ mol/L, qual pH aproximado?
- Em equilíbrio gasoso ideal N₂(g) + 3H₂(g) ⇌ 2NH₃(g), diminuir o volume a temperatura const
- Uma solução-tampão formada por ácido fraco HA e sua base conjugada A⁻, com concentrações c
- Para AgCl(s) ⇌ Ag⁺(aq) + Cl⁻(aq), a expressão aproximada de Kps é:
- Uma reação libera 150 kJ nas condições consideradas. Qual é o valor de ΔH para a reação no
- Qual efeito de um catalisador sobre uma reação reversível em temperatura fixa?
- Em uma pilha galvânica Zn|Zn²⁺||Cu²⁺|Cu, com zinco oxidando, qual eletrodo funciona como â
- Dadas E°red(Cu²⁺/Cu) = +0,34 V e E°red(Zn²⁺/Zn) = −0,76 V, o potencial padrão da pilha Zn/
- Para uma reação cuja lei experimental é v = k[A]²[B], qual é a ordem global?
- Qual composto pertence à função álcool?
- O nome do composto CH₃–CO–CH₃ é:
- Qual par representa isomeria de função?
- Qual característica diferencia fenol de álcool na classificação básica de funções orgânica
- Qual é a denominação usual sistemática de CH₃COOCH₂CH₃?
- Átomos de um mesmo elemento têm necessariamente o mesmo número de prótons, mas podem apres
- Em átomo neutro, o número de elétrons é obrigatoriamente igual ao número de massa A.
- O princípio de exclusão de Pauli permite, em um mesmo orbital, no máximo dois elétrons de 
- Todas as moléculas que possuem ligações covalentes polares são necessariamente moléculas p
- O número de oxidação de um elemento em sua substância simples é zero.
- Na reação Zn + Cu²⁺ → Zn²⁺ + Cu, o zinco atua como agente oxidante.
- Em equações químicas, alterar índices subscritos é um procedimento válido para balancear s
- Se um reagente está em excesso, a quantidade máxima de produto deve ser calculada pelo rea
- Uma solução 0,50 mol/L contém meio mol de soluto por litro de solução final.
- Molalidade e molaridade são unidades equivalentes em qualquer temperatura e composição.
- Ao diluir sem reação uma solução, a quantidade de soluto permanece a mesma embora a molari
- Em soluções aquosas diluídas, 1 mg/L equivale exatamente a 1 ppm, independentemente da den
- Em equilíbrio químico dinâmico, as velocidades direta e inversa são iguais, embora as conc
- A adição de catalisador a um equilíbrio reversível modifica a constante K na mesma tempera
- A 25 °C, no modelo ideal diluído, uma solução de pH 4 apresenta concentração de H₃O⁺ aprox
- O pH de neutralidade é necessariamente igual a 7,0 em água pura a qualquer temperatura.
- Em reações exotérmicas sob pressão constante, ΔH para o sentido escrito é negativo.
- Uma reação exotérmica é necessariamente rápida porque sua entalpia de reação é negativa.
- Nas pilhas e nas células eletrolíticas, a oxidação ocorre no ânodo e a redução no cátodo.
- Multiplicar uma semirreação por dois exige multiplicar por dois o seu potencial padrão de 
- A função cetona apresenta um grupo carbonila ligado a dois carbonos da estrutura.
- Etanol e metoximetano possuem mesma fórmula molecular, mas diferentes funções orgânicas.
- A presença de uma ligação dupla C=C garante a existência de isomeria geométrica E/Z.
- O grupo –COO– do etanoato de etila identifica a função éster.
```

## Pedido 36: Enfermagem em terapia intensiva adulto, pediátrica e neonatal (`enfermagem.uti-neonatal-pediatrica`, 8 tópicos de edital)

```
Matéria: enfermagem.uti-neonatal-pediatrica (Enfermagem em terapia intensiva adulto, pediátrica e neonatal)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos do cuidado ao paciente crítico e Processo de Enfermagem": 17
- "Via aérea, ventilação mecânica e prevenção de pneumonia associada": 17
- "Hemodinâmica, drogas vasoativas e vigilância de perfusão": 17
- "Cateteres, dispositivos e controle de riscos associados à UTI": 17
- "Assistência à criança gravemente enferma em terapia intensiva": 16
- "Cuidados intensivos neonatais, prematuridade e vínculo familiar": 16

Já existem (não repita):
- Um paciente crítico apresenta saturação estável, mas queda progressiva do débito urinário 
- Segundo a Resolução Cofen nº 736/2024, o Processo de Enfermagem organiza-se em:
- No cuidado ao paciente intubado, uma finalidade da PEEP é:
- Ao identificar secreções e aumento de pressão de via aérea em paciente grave intubado, a c
- Entre os cuidados mais importantes para prevenção de infecção ligada ao cateter venoso cen
- Na criança grave, a ausência de hipotensão arterial:
- No acompanhamento de um recém-nascido de risco, o cuidado relacionado à temperatura consis
- O Método Canguru na atenção ao recém-nascido de baixo peso corresponde a:
- Sobre noradrenalina e dobutamina em terapia intensiva, assinale a associação predominante 
- Um traçado arterial invasivo aparece subitamente amortecido, sem mudança clínica compatíve
- Durante passagem de plantão de paciente sob ventilação e vasoativos, a informação mais úti
- Em um ventilador mecânico, a FiO₂ representa:
- Na prevenção de PAV em adultos, assinale a ação correta quando não houver contraindicação:
- Durante a infusão de droga vasoativa, o alarme de oclusão exige atenção porque:
- Em cateter vesical de demora de paciente intensivo, a medida de prevenção mais adequada é:
- Uma criança não verbal apresenta agitação e expressão facial de desconforto após procedime
- Em RN prematuro com suporte de oxigênio, a assistência segura é:
- Segundo a Resolução Cofen nº 639/2020, o manejo de parâmetros da ventilação mecânica pelo 
- Uma criança taquicárdica com pulsos periféricos fracos mantém pressão medida dentro da fai
- Antes de infundir solução por sonda enteral recém-instalada em paciente crítico, deve-se:
- Sobre o uso do Método Canguru em recém-nascido clinicamente instável, é correto afirmar:
- Durante uso de pressão arterial invasiva, um transdutor colocado em altura inadequada pode
- Na prevenção de infecção por CVC em RN, qual raciocínio é o mais seguro?
- Uma pessoa intubada, sedada, apresenta sinais de ansiedade ao despertar; seus familiares f
- Em paciente ventilado, há queda de saturação e alarme de alta pressão; o monitor cardíaco 
- Na UTI, a observação de tendências clínicas é mais informativa do que confiar exclusivamen
- O Processo de Enfermagem se encerra após o preenchimento da avaliação inicial e não requer
- Saturação periférica de oxigênio mede diretamente a quantidade de gás carbônico eliminada 
- A higiene das mãos integra os cuidados de prevenção de infecção relacionada a dispositivos
- A dobutamina é predominantemente inotrópica, ao passo que a noradrenalina atua sobretudo c
- O recém-nascido prematuro não percebe dor porque ainda não consegue expressá-la verbalment
- A presença da família, quando segura e organizada pela equipe, pode integrar a humanização
- A Resolução Cofen nº 639/2020 autoriza o enfermeiro a ajustar qualquer parâmetro do ventil
- A aspiração traqueal de paciente grave intubado deve ser orientada pela avaliação da neces
- É seguro lavar rapidamente uma linha contendo vasoativo concentrado para resolver qualquer
- A troca de cateter central a cada plantão é medida de prevenção de infecção indicada indep
- Em criança gravemente enferma, a hipotensão pode surgir tardiamente após mecanismos compen
- O contato pele a pele do Método Canguru substitui a incubadora e o suporte intensivo mesmo
- A linha arterial é uma via indicada para administrar medicamentos intravenosos de rotina s
- O transdutor arterial mal nivelado pode produzir valor de pressão discordante da situação 
- Em neonatologia, a oferta de oxigênio deve priorizar sempre a maior saturação possível, in
- A prevenção de pneumonia associada à ventilação mecânica deve ser copiada integralmente do
- Uma pressão arterial aparentemente preservada afasta hipoperfusão se a saturação também es
- Em um CVC, a desinfecção dos conectores e a revisão diária da necessidade fazem parte da p
- O uso de instrumentos comportamentais de dor é dispensável em crianças incapazes de verbal
- A comunicação sobre prognóstico deve incluir promessas categóricas para reduzir ansiedade 
```

## Pedido 37: Fisioterapia em neurologia periférica, quedas e pé diabético (`fisioterapia.fisioterapia-neuro-funcional-geriatrica`, 8 tópicos de edital)

```
Matéria: fisioterapia.fisioterapia-neuro-funcional-geriatrica (Fisioterapia em neurologia periférica, quedas e pé diabético)
Capítulos e quantas questões quero em cada um (total 100):
- "Síndromes neurológicas periféricas e avaliação fisioterapêutica": 20
- "Lesões de nervos e plexos: recuperação e reabilitação": 20
- "Pessoa idosa: funcionalidade, fragilidade e prevenção de quedas": 20
- "Pé diabético, úlceras e proteção da pele": 20
- "Órteses, próteses, humanização e prática baseada em evidências": 20

Já existem (não repita):
- Na síndrome do túnel do carpo, o comprometimento do nervo mediano pode produzir parestesia
- Toda mononeuropatia periférica determina perda sensitiva simétrica, em padrão de meias, no
- A eletroneuromiografia pode complementar a localização e a caracterização de lesões perifé
- Em pessoa com fraqueza ascendente de instalação rápida e dificuldade respiratória, a condu
- A neuropraxia envolve bloqueio de condução sem perda axonal importante, enquanto a axonotm
- O fisioterapeuta deve prescrever mobilização neural intensa a qualquer paciente com dor ir
- A lesão do nervo fibular comum pode causar pé caído e favorecer tropeços durante a fase de
- A neurotmese é, entre as categorias clássicas de Seddon, a de maior interrupção estrutural
- Autonomia é a capacidade de decidir, enquanto independência funcional corresponde à possib
- O registro de quedas anteriores, o uso de medicamentos e a inspeção do ambiente doméstico 
- Na prevenção de quedas, programas com exercícios progressivos de equilíbrio e fortalecimen
- A ocorrência de quedas repetidas deve ser aceita como consequência natural e inevitável da
- O IVCF-20 pode apoiar o reconhecimento da vulnerabilidade clínico-funcional, sem substitui
- No pé diabético, ausência de dor exclui ulceração clinicamente relevante porque toda lesão
- O monofilamento de 10 g é empregado na pesquisa da perda da sensibilidade protetora, sendo
- Toda úlcera plantar neuropática deve ser tratada exclusivamente com curativo, pois redistr
- Antibióticos sistêmicos devem ser prescritos para qualquer úlcera diabética, ainda que não
- Uma úlcera com necrose e sinais de isquemia demanda avaliação rápida da perfusão e encamin
- Órtese e prótese são termos sinônimos, pois ambos designam exclusivamente dispositivos que
- A prática baseada em evidências combina resultados científicos, experiência clínica e valo
- Paciente refere formigamento predominante no quinto dedo e na metade ulnar do quarto dedo,
- Pessoa com diabetes de longa data apresenta redução bilateral distal de sensibilidade vibr
- Ao registrar o caso de pessoa com neuropatia, o fisioterapeuta descreve fraqueza dos dorsi
- Durante avaliação de um indivíduo com suspeita de neuropatia periférica, surge fraqueza ra
- Em relação à interpretação do exame neurológico periférico, qual afirmação é a mais adequa
- Após compressão transitória de um nervo, o exame sugere bloqueio de condução sem important
- Paciente com comprometimento do fibular comum passa a arrastar a ponta do pé no balanço da
- Em reabilitação de lesão do plexo braquial traumática com fraqueza importante e perda de a
- Qual interpretação é correta sobre prognóstico de lesão nervosa periférica?
- Após reparo cirúrgico de nervo periférico, o paciente solicita iniciar imediatamente along
- Qual componente da avaliação funcional corresponde principalmente a uma atividade instrume
- Uma pessoa idosa teve duas quedas, apresenta sedação após medicamentos e mora em casa com 
- O Short Physical Performance Battery (SPPB) avalia, principalmente, a combinação de quais 
- Uma idosa executa o TUG em tempo elevado. Qual interpretação deve orientar a conduta?
- Sobre fragilidade e cuidado centrado na pessoa idosa, assinale a afirmação correta.
- Para pesquisar perda da sensibilidade protetora no pé de uma pessoa com diabetes, qual ins
- Paciente com diabetes apresenta ferida plantar em região de pressão, calosidade circundant
- Uma pessoa com perda de sensibilidade nos pés pergunta qual hábito doméstico oferece maior
- Para uma úlcera neuropática plantar de antepé sem contraindicações relevantes, qual princí
- Durante atendimento, observa-se pé diabético com necrose, extremidade fria e perfusão susp
- Qual exemplo representa uma órtese, e não prótese, na reabilitação de pessoa com pé caído?
- Durante adaptação a prótese transtibial, o usuário relata dor persistente no encaixe e apr
- No plano pré-protético de pessoa com amputação de membro inferior, qual conjunto de metas 
- Qual decisão expressa de modo adequado a prática baseada em evidências ao escolher recurso
- Um usuário com prótese de membro inferior tem neuropatia e ferida incipiente no pé contral
```

## Pedido 38: Lei Orgânica do Ministério Público de São Paulo (LC 734/1993) (`sao-paulo.lei-organica-mp-lc-734`, 8 tópicos de edital)

```
Matéria: sao-paulo.lei-organica-mp-lc-734 (Lei Orgânica do Ministério Público de São Paulo (LC 734/1993))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios, autonomia e estrutura": 25
- "2. Órgãos da Administração Superior": 25
- "3. Funções, inquérito civil e carreira": 25
- "4. Deveres, garantias, prerrogativas e regime disciplinar": 25

Já existem (não repita):
- Segundo a LC 734/1993, são princípios institucionais do Ministério Público de São Paulo a 
- Pela LC 734/1993, NÃO é órgão da Administração Superior do Ministério Público:
- A Corregedoria-Geral do Ministério Público integra, ao mesmo tempo, a Administração Superi
- Entre os órgãos auxiliares do Ministério Público de São Paulo, a LC 734/1993 inclui:
- A proposta orçamentária do Ministério Público de São Paulo é elaborada dentro dos limites 
- Os atos de gestão administrativa do Ministério Público, inclusive convênios, contratações 
- Segundo a LC 734/1993, as Procuradorias de Justiça e as Promotorias de Justiça são, respec
- As Promotorias de Justiça realizam reuniões mensais, de participação facultativa, para tra
- A Promotoria de Justiça cujos cargos têm, ao mesmo tempo, as funções das Promotorias Crimi
- O Procurador-Geral de Justiça é nomeado pelo Governador, dentre Procuradores de Justiça in
- Se o Governador não efetivar a nomeação do Procurador-Geral de Justiça nos 15 dias seguint
- Em caso de vacância do cargo ou de afastamento do Procurador-Geral de Justiça por período 
- A proposta de destituição do Procurador-Geral de Justiça, de iniciativa da maioria absolut
- A destituição do Procurador-Geral de Justiça tem cabimento, segundo o art. 13 da LC 734/19
- O Órgão Especial do Colégio de Procuradores de Justiça é composto por quantos Procuradores
- A eleição e a destituição do Corregedor-Geral do Ministério Público estão entre as atribui
- O Conselho Superior do Ministério Público de São Paulo é integrado pelo Procurador-Geral, 
- Compete ao Conselho Superior do Ministério Público decidir sobre o vitaliciamento de membr
- O Conselho Superior do Ministério Público pode determinar a disponibilidade ou a remoção d
- O Corregedor-Geral do Ministério Público é livremente nomeado pelo Procurador-Geral de Jus
- Compete ao Corregedor-Geral do Ministério Público, entre outras atribuições:
- As recomendações feitas pelo Corregedor-Geral aos órgãos de execução do Ministério Público
- Na LC 734/1993, da decisão que indefere representação para instauração de inquérito civil 
- Pela LC 734/1993, da instauração de inquérito civil cabe recurso do interessado ao Conselh
- Homologada pelo Conselho Superior a promoção de arquivamento do inquérito civil, o órgão d
- Os conflitos de atribuição entre membros do Ministério Público de São Paulo são decididos 
- Entre as atribuições processuais do Procurador-Geral de Justiça previstas no art. 116 da L
- Cabe aos Promotores de Justiça exercer as atribuições do Ministério Público junto aos órgã
- É obrigatória a abertura de concurso de ingresso na carreira do Ministério Público de São 
- Se o relatório do Corregedor-Geral concluir contra o vitaliciamento do membro em estágio p
- O membro vitalício do Ministério Público de São Paulo perde o cargo:
- Instaurado processo administrativo disciplinar contra membro não vitalício do Ministério P
- Segundo o art. 170 da LC 734/1993, é vedado ao membro do Ministério Público:
- É dever do membro do Ministério Público titular residir na respectiva comarca, salvo autor
- Os membros do Ministério Público de São Paulo têm direito, anualmente, a férias de:
- A vitaliciedade dos membros do Ministério Público de São Paulo é adquirida após dois anos 
- Constitui prerrogativa do membro do Ministério Público de São Paulo:
- Quando, no curso de investigação, houver indício de infração penal praticada por membro do
- Assinale a alternativa que apresenta as sanções disciplinares aplicáveis aos membros do Mi
- A pena de censura é aplicada por escrito e publicamente, com divulgação no Diário Oficial,
- Na LC 734/1993, a autoridade competente para aplicar as sanções disciplinares aos membros 
- Pela LC 734/1993, prescreve em quatro anos a punibilidade das faltas sujeitas às penas de 
- Na LC 734/1993, quando cabíveis as penas de advertência, censura ou suspensão, a apuração 
- Constitui infração disciplinar do membro do Ministério Público o abandono do cargo por pra
```

## Pedido 39: Enfermagem na saúde da mulher e obstétrica (`enfermagem.saude-mulher-obstetrica`, 8 tópicos de edital)

```
Matéria: enfermagem.saude-mulher-obstetrica (Enfermagem na saúde da mulher e obstétrica)
Capítulos e quantas questões quero em cada um (total 100):
- "Pré-natal de risco habitual: consulta, vigilância e encaminhamento": 17
- "Parto normal, segurança e assistência humanizada": 17
- "Puerpério: avaliação materna, riscos e continuidade do cuidado": 17
- "Aleitamento materno: manejo clínico e apoio à lactação": 17
- "Planejamento reprodutivo e prevenção dos cânceres ginecológicos e mamário": 16
- "Violência contra a mulher: acolhimento, notificação e rede de proteção": 16

Já existem (não repita):
- O enfermeiro pode realizar consultas no pré-natal de risco habitual, respeitando competênc
- A captação da gestante para o pré-natal deve ser adiada até o início do segundo trimestre,
- O direito ao acompanhante de escolha da parturiente, nos serviços abrangidos pela lei fede
- Para prevenir laceração em toda parturiente de baixo risco, a episiotomia deve ser feita r
- Nos primeiros seis meses, a orientação geral para bebê saudável em aleitamento materno exc
- A ausência de lesões visíveis no exame físico afasta a possibilidade de violência psicológ
- Uma gestante inicialmente classificada como de risco habitual pode exigir reclassificação 
- Para humanizar a assistência, é correto proibir intervenções obstétricas em qualquer circu
- O contato pele a pele imediato entre mãe e recém-nascido vigoroso e estável favorece víncu
- Febre associada a dor uterina e lóquios com odor fétido é achado fisiológico do puerpério 
- Após os seis meses, a introdução de alimentação adequada implica suspensão obrigatória do 
- A ausência de boletim de ocorrência policial não impede a oferta de atendimento de saúde a
- Na diretriz nacional aprovada em 2025, o teste molecular para DNA-HPV oncogênico tornou-se
- Para rastreamento mamográfico populacional de mulheres assintomáticas, o Ministério da Saú
- Na esterilização voluntária, a legislação atual exige autorização escrita do cônjuge, aind
- Em uma puérpera, cefaleia intensa com alteração visual e pressão arterial elevada pode rep
- A notificação de violência sexual à vigilância em saúde e o registro de boletim de ocorrên
- Dor mamilar persistente durante mamadas deve motivar observação da pega e avaliação da día
- Na primeira consulta de pré-natal na APS, qual conjunto de ações é mais adequado?
- Qual das medidas constitui boa prática para parturiente com evolução fisiológica?
- Uma puérpera apresenta sangramento intenso e tontura pouco depois do parto. Qual a priorid
- Sobre o aleitamento recomendado para o lactente saudável, assinale a opção correta.
- Uma mulher solicita informação sobre contracepção na UBS. Qual atitude respeita o planejam
- Mulher relata ameaças recorrentes do parceiro durante consulta. Qual é a conduta inicial m
- Qual é uma indicação para procurar atendimento urgente durante o pré-natal?
- No cuidado ao recém-nascido vigoroso e estável, uma intervenção geralmente incentivada é:
- Qual achado mamário durante a lactação indica necessidade de avaliar técnica de amamentaçã
- Gestante de risco habitual retorna com cefaleia forte, escotomas e pressão arterial muito 
- Parturiente pede para caminhar e receber massagens enquanto evolui sem complicações. A res
- No terceiro dia após parto, a mulher apresenta febre, dor uterina e lóquios fétidos. A hip
- Mãe de recém-nascido saudável pergunta se deve oferecer água em dia de calor. Qual respost
- Uma pessoa de 28 anos, capaz, sem filhos, pede informações sobre esterilização voluntária.
- Mulher vítima de violência sexual procura unidade de urgência sem boletim e relata exposiç
- Gestante faltou às consultas e retorna após longo intervalo; exames anteriores estavam nor
- Puérpera relata dor intensa em uma perna, aumento de volume unilateral e dificuldade para 
- Uma mulher de 32 anos, assintomática e já sexualmente ativa, pergunta pelo rastreio atual 
- Uma gestante com risco habitual e exames prévios normais informa redução marcante dos movi
- Uma equipe propõe amniotomia e ocitocina para toda mulher de baixo risco na admissão, sem 
- Na consulta puerperal, mulher sem antecedente de hipertensão gestacional apresenta pressão
- Lactante com dor localizada, rubor e febre teme que seja obrigatória a interrupção definit
- Uma mulher assintomática de 45 anos solicita mamografia no SUS, após ouvir sobre as regras
- Pessoa com colo do útero, 36 anos, tem DNA-HPV oncogênico positivo. A equipe deve:
- Sobre mulher adulta que relata violência sexual e não deseja fazer boletim, assinale a alt
- Na coleta de dados sobre violência doméstica, a mulher está acompanhada de pessoa possivel
- No atendimento de planejamento reprodutivo, mulher solicita laqueadura no período do parto
```

## Pedido 40: Qualidade da Água (`minas-gerais.qualidade-agua`, 7 tópicos de edital)

```
Matéria: minas-gerais.qualidade-agua (Qualidade da Água)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Monitoramento de rios e mananciais em Minas Gerais": 25
- "2. Indicadores, amostragem e leitura dos resultados": 25
- "3. Poluição, impactos e conservação das águas": 25
- "4. Reescrita e como a IDECAN cobra": 25

Já existem (não repita):
- Qual é a finalidade central do monitoramento da qualidade da água?
- Sobre o programa Águas de Minas, assinale a afirmação correta.
- Qual alternativa apresenta uma etapa compatível com o monitoramento da qualidade das águas
- Em relação à interpretação de uma coleta, assinale a opção adequada.
- O que caracteriza um indicador de qualidade da água?
- Qual medida contribui diretamente para conservar as margens de um curso d’água?
- Uma fonte pontual de poluição é melhor descrita como aquela que
- Após chuva, uma estação registra maior turbidez em trecho próximo a solo descoberto. Qual 
- Uma equipe compara amostras a montante e a jusante de uma área com possível lançamento. Qu
- Sobre o aspecto visual e a qualidade da água, assinale a afirmação correta.
- Uma bacia combina coleta e tratamento de esgoto, controle de erosão e proteção de margens.
- Qual afirmação descreve adequadamente o papel do IQA?
- Em trecho rural, parte do material aplicado ao solo alcança o rio após escoamento superfic
- Uma campanha identifica redução de oxigênio dissolvido e aumento de matéria orgânica em um
- Uma série de coletas mostra diferença entre anos no mesmo ponto. Qual procedimento fortale
- Como a proteção de vegetação ciliar se relaciona à conservação da água?
- Qual afirmação confunde monitoramento com atribuição causal?
- Uma diferença entre resultados a montante e a jusante foi observada, mas há afluentes entr
- Em um período chuvoso, o índice geral muda pouco, mas um parâmetro específico apresenta al
- Uma amostra de rio tem turbidez alta, sem alteração aparente em um indicador de contaminaç
- Uma equipe quer avaliar se a condição de um rio mudou após várias campanhas. Qual desenho 
- Uma avaliação confirma lançamento de esgoto sem tratamento em um trecho. Qual intervenção 
- Ao interpretar dados de qualidade de águas superficiais e subterrâneas, qual cautela é apr
- Um índice geral favorável aparece no relatório, mas o enunciado pergunta sobre presença de
- Dois pontos apresentam resultados diferentes, mas foram amostrados em épocas e condições d
- Após identificar erosão em margens e lançamento de efluente, um relatório recomenda tratar
- Qual sequência representa melhor a produção e o uso de informação no monitoramento?
- Uma campanha registra aumento de sedimentos depois de chuva em área com cobertura vegetal 
- Uma equipe relata que dois períodos tiveram IQA semelhante. Qual conclusão não decorre nec
- Um gestor afirma que a recuperação da vegetação ciliar tornou desnecessário continuar medi
- O monitoramento da qualidade da água pode envolver planejamento da rede, coleta e análise 
- Uma amostra transparente permite dispensar exames laboratoriais porque não há contaminante
- O programa Águas de Minas acompanha águas superficiais e subterrâneas em Minas Gerais.
- O IQA, por ser sintético, identifica sozinho a fonte de cada poluente registrado na amostr
- Uma fonte pontual está associada a um ponto de lançamento identificável.
- O escoamento superficial pode transportar partículas e outros materiais de áreas da bacia 
- Se dois períodos apresentam IQA semelhante, todos os parâmetros e trechos avaliados tivera
- A vegetação ciliar pode contribuir para proteger margens, mas integra medidas mais amplas 
- Uma diferença entre pontos a montante e a jusante comprova sempre que uma fonte localizada
- O monitoramento relaciona planejamento de pontos, coleta, análise e interpretação contextu
- Uma única coleta em um ponto basta para estabelecer uma tendência de qualidade para todo o
- A interpretação de um resultado deve considerar o ponto, o período, o método e a pergunta 
- A variabilidade natural dos rios torna os dados de qualidade inúteis para a gestão dos rec
- Reduzir a carga de esgoto na fonte e acompanhar a resposta do curso d’água são ações compa
- A aparência da água exclui a necessidade de análises laboratoriais quando não há mudança d
```

## Pedido 41: Procedimentos Operacionais: Documentos Aeronáuticos, Cartas IFR, Carga e Passageiros (`aviacao.procedimentos-operacionais-ifr-documentos`, 7 tópicos de edital)

```
Matéria: aviacao.procedimentos-operacionais-ifr-documentos (Procedimentos Operacionais: Documentos Aeronáuticos, Cartas IFR, Carga e Passageiros)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Documentos aeronáuticos e abreviaturas": 25
- "2. Cartas e procedimentos IFR": 25
- "3. Carga externa e artigos perigosos": 25
- "4. Segurança dos passageiros": 25

Já existem (não repita):
- A AIP reúne informações aeronáuticas de caráter permanente e divide-se nas partes GEN, ENR
- A informação de que a pista principal de um aeródromo estará interditada por três dias par
- A AIC é a publicação destinada a informações que não se enquadram na AIP nem em NOTAM, com
- Alterações temporárias de longa duração, ou que contenham muito texto e gráficos, são publ
- Com o ajuste altimétrico QFE, o altímetro de uma aeronave pousada indica zero na pista.
- O ajuste-padrão de 1013,2 hPa, usado acima da altitude de transição para voar em níveis de
- O ROTAER é o manual auxiliar de rotas aéreas que reúne dados dos aeródromos, como pistas, 
- A sigla ATIS corresponde ao:
- As siglas FIR, TMA e CTR correspondem, respectivamente, a:
- A carta que apresenta o procedimento padronizado para a aeronave sair do aeródromo e ingre
- A STAR é a carta de chegada padrão por instrumentos, que liga a fase em rota à área termin
- A altitude mínima de setor (MSA) proporciona separação mínima de 1.000 ft acima dos obstác
- Em uma aproximação de precisão ILS, o mínimo operacional em que o piloto decide prosseguir
- Aproximações baseadas em VOR, NDB e RNAV com navegação lateral (LNAV) são classificadas co
- Ao atingir a DA/DH sem as referências visuais necessárias para o pouso, o piloto deve:
- No padrão-padrão de espera (holding), as curvas são feitas:
- A mensagem MINIMUM FUEL transmitida ao controle de tráfego aéreo constitui declaração de e
- A emergência de combustível é declarada com MAYDAY FUEL quando o combustível previsto no p
- Em descida de emergência por despressurização em voo IFR, o piloto deve considerar a MSA e
- A carta de aeródromo (ADC) apresenta:
- O ILS é exemplo de aproximação de precisão por fornecer guia lateral, pelo localizador, e 
- Na carga externa com helicópteros, a combinação em que a carga é alijável e permanece susp
- A classe D de carga externa com helicópteros envolve o transporte de pessoas externamente 
- Antes de uma operação de carga externa, entre os cuidados essenciais está:
- O transporte de artigos perigosos em aeronaves civis segue o RBAC 175 e as Instruções Técn
- Na classificação de artigos perigosos, os materiais radioativos pertencem à classe:
- Certas baterias de lítio são classificadas como artigos perigosos diversos, da classe 9.
- No transporte de artigos perigosos como carga, cabe ao expedidor:
- O piloto em comando deve ser informado por escrito sobre os artigos perigosos embarcados, 
- A classe C de carga externa com helicópteros corresponde à carga:
- No briefing de segurança aos passageiros antes da decolagem, devem ser abordados, entre ou
- Passageiros com necessidades especiais devem receber briefing individual sobre os procedim
- Ao embarcar em um helicóptero com rotores girando, o passageiro deve aproximar-se:
- Em terreno inclinado, a aproximação ao helicóptero com rotor girando deve ser feita pelo l
- Os passageiros ocupantes de assentos junto às saídas de emergência devem ser capazes de au
- Próximo a um helicóptero com rotores girando, objetos longos, como antenas e ferramentas, 
- Passageiros devem ser conduzidos no pátio e não podem circular livremente na área de movim
- Em falha de comunicações em condições IMC, o piloto deve seguir a rota e os níveis autoriz
- No planejamento de um voo IFR, além da AIP, das cartas e do ROTAER, o piloto deve consulta
- Em uma aproximação de não precisão, ao atingir a MDA sem referências visuais, o piloto dev
```

## Pedido 42: Vetores: operações, produto escalar, produto vetorial e misto (`matematica.vetores-produto-escalar-vetorial`, 7 tópicos de edital)

```
Matéria: matematica.vetores-produto-escalar-vetorial (Vetores: operações, produto escalar, produto vetorial e misto)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Vetores e operações básicas": 25
- "2. Produto escalar": 25
- "3. Produto vetorial e produto misto": 25
- "4. Roteiros de resolução e aplicações em engenharia": 25

Já existem (não repita):
- O módulo do vetor v = (2, 3, 6) é igual a 7.
- Dados os pontos A(1, 2, 3) e B(4, 6, 3), o vetor AB é:
- O versor do vetor v = (0, 3, 4) é:
- Multiplicar um vetor por um escalar negativo mantém sua direção, mas inverte seu sentido.
- Dois vetores de módulos 3 e 5 formam entre si um ângulo de 60°. O módulo de sua soma é:
- Os vetores u = (2, −4, 6) e v = (−1, 2, −3) são paralelos.
- No espaço tridimensional, três vetores formam uma base se, e somente se, forem:
- A soma de vetores é comutativa e associativa, e o vetor nulo é seu elemento neutro.
- O produto escalar dos vetores u = (1, 2, 3) e v = (4, −5, 6) é:
- Se o produto escalar de dois vetores não nulos é igual a zero, os vetores são perpendicula
- O valor de m para que u = (m, 2, −1) e v = (3, m, 4) sejam ortogonais é:
- O ângulo entre os vetores u = (1, 1, 0) e v = (1, 0, 1) é de:
- Se o produto escalar entre dois vetores não nulos é negativo, o ângulo entre eles é obtuso
- A projeção vetorial de u = (3, 4) sobre v = (1, 0) é:
- Uma força F = (10, 0, 0) N desloca um corpo de d = (3, 4, 0) m. O trabalho realizado pela 
- Uma força constante perpendicular ao deslocamento de um corpo realiza trabalho máximo.
- O fluxo magnético de um campo uniforme através de uma superfície plana pode ser calculado 
- Para qualquer vetor v, o produto escalar v·v é igual:
- O produto vetorial u × v, com u = (1, 0, 0) e v = (0, 1, 0), é:
- O produto vetorial dos vetores u = (1, 2, 3) e v = (4, 5, 6) é:
- O produto vetorial é anticomutativo, isto é, u × v = −(v × u).
- O produto vetorial de dois vetores paralelos e não nulos é o vetor nulo.
- O produto vetorial é associativo, isto é, (u × v) × w = u × (v × w) para quaisquer vetores
- A área do paralelogramo determinado pelos vetores u = (2, 0, 0) e v = (0, 3, 0) é:
- A área do triângulo de vértices A(0, 0, 0), B(4, 0, 0) e C(0, 3, 0) é:
- O vetor resultante do produto vetorial u × v é perpendicular tanto a u quanto a v.
- O módulo do produto vetorial de dois vetores de módulos 4 e 5 que formam ângulo de 30° é:
- O produto misto dos vetores u = (1, 0, 0), v = (0, 2, 0) e w = (0, 0, 3) é:
- Se o produto misto de três vetores é igual a zero, esses vetores são coplanares.
- O volume do tetraedro formado por três vetores a partir de um mesmo vértice é igual a:
- Uma força F = (0, 10, 0) N é aplicada no ponto de posição r = (2, 0, 0) m em relação a um 
- A força magnética sobre uma carga em movimento, F = q(v × B), é sempre perpendicular à vel
- Um condutor retilíneo de 0,5 m, percorrido por corrente de 4 A, está perpendicular a um ca
- O torque produzido por uma força aplicada em uma chave de boca é máximo quando a força é p
- Em relação ao produto escalar e ao produto vetorial, assinale a alternativa correta.
- Os vetores u = (1, 2, 3), v = (2, 4, 6) e w = (0, 1, 1) são linearmente dependentes.
- Na base canônica, o produto vetorial k × i é igual a:
- O plano que passa pelos pontos A(0, 0, 0), B(1, 0, 0) e C(0, 1, 0) tem vetor normal dado p
- Na força de Lorentz, F = q(E + v × B), a parcela elétrica pode realizar trabalho sobre a c
- A potência mecânica instantânea de uma força pode ser obtida pelo produto escalar entre a 
```

## Pedido 43: Limpeza urbana e operação de serviços de saneamento (`engenharia.limpeza-urbana-servicos-saneamento`, 7 tópicos de edital)

```
Matéria: engenharia.limpeza-urbana-servicos-saneamento (Limpeza urbana e operação de serviços de saneamento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Limpeza urbana: planejamento e execução dos serviços": 17
- "2. Coleta e transporte de resíduos: execução e rastreabilidade": 17
- "3. Operação e controle de sistemas de abastecimento de água": 17
- "4. Operação do esgotamento sanitário e atendimento a ocorrências": 17
- "5. Manutenção, segurança e continuidade dos equipamentos públicos": 16
- "6. Indicadores, regulação, fiscalização e gestão da qualidade": 16

Já existem (não repita):
- Em uma programação municipal de serviços de logradouros, qual atividade caracteriza varriç
- No serviço de manutenção de canteiros e margens de vias, a roçagem distingue-se da capina 
- Para a Política Nacional de Resíduos Sólidos, a definição que melhor descreve rejeito é:
- Qual etapa corresponde corretamente ao transbordo operacional de resíduos urbanos?
- O operador anotou leituras sucessivas de um horímetro instalado em bomba de recalque. Esse
- Qual ocorrência configura, tipicamente, perda real de água na rede de distribuição?
- Um equipamento recebe inspeções e troca de peças em intervalos fixados pelo fabricante. Is
- Uma equipe terá de executar roçagem junto a ponto de ônibus com fluxo intenso. A primeira 
- Uma rota tinha 90 trechos de coleta programados e executou 81 no turno. Qual foi o cumprim
- Uma bomba manteve vazão constante de 18 m³/h durante duas horas. Qual o volume corresponde
- O nível de um reservatório caiu rapidamente ao longo de uma hora, sem alarme de extravasam
- Ao verificar poço de elevatória com nível alto, bomba sem funcionamento e horímetro sem av
- Uma ordem de serviço foi aberta às 8h, a equipe chegou às 9h e o abastecimento retornou às
- No uso de dados públicos de saneamento, qual prática aumenta a comparabilidade de resultad
- Conforme a organização brasileira do saneamento, qual afirmação diferencia adequadamente a
- Num relatório de coleta seletiva, 1.000 domicílios têm acesso à rota, mas só 600 apresenta
- Após tempestade, há resíduos acumulados sobre grelhas de drenagem e previsão de nova chuva
- O prestador quer distinguir perdas físicas de perdas ligadas à medição. Qual conjunto de a
- Uma ETE apresentou análises adequadas em determinada data, mas houve extravasamentos seman
- Uma bomba crítica foi reparada repetidas vezes por superaquecimento. Qual decisão revela g
- A Norma de Referência ANA nº 14/2025, para limpeza urbana e resíduos, contém entre seus in
- Um servidor comparou a edição SINISA 2025 com o ano civil de 2025 como se fossem equivalen
- Um medidor indica 20 m³/h em um instante, enquanto o horímetro acumulou 10 horas no períod
- Uma entidade quer avaliar desempenho da varrição em dois municípios com extensões viárias 
- A varrição de calçadas e sarjetas pode integrar a limpeza urbana, mesmo quando o lixo domi
- Na roçagem de área pública próxima a pedestres, os protetores do equipamento podem ser ret
- A coleta seletiva consiste no recolhimento de resíduos previamente segregados de acordo co
- Na gestão dos resíduos, todo material coletado deve ser imediatamente classificado como re
- Se a vazão se manteve constante em 10 m³/h por três horas, o volume do período foi 30 m³.
- Uma leitura de horímetro de 50 horas permite concluir que a bomba deslocou exatamente 50 m
- O acompanhamento conjunto de pressão, vazão e nível pode auxiliar a localizar anomalias e 
- A troca de hidrômetros submedidores é uma medida voltada exclusivamente à redução de vazam
- A falha de uma estação elevatória pode causar elevação de nível, refluxo e extravasamento,
- O despejo emergencial de esgoto bruto em uma galeria pluvial é a solução operacional prefe
- Uma OS de manutenção bem instruída permite relacionar falha, intervenção, responsáveis, ho
- A manutenção corretiva é necessariamente ilegal e nunca pode integrar um plano de gestão d
- Um indicador deve ter unidade, período e critérios de coleta definidos para viabilizar com
- A presença de 100% de atendimento do roteiro planejado comprova, isoladamente, que 100% do
- A NR ANA nº 9/2024 abrange indicadores operacionais associados a perdas na distribuição de
- O SINISA e o SINIR são expressões equivalentes e ambos se destinam exclusivamente ao regis
- A NR ANA nº 14/2025 contém, entre outros, indicador relacionado à continuidade da varrição
- As normas de referência da ANA significam que a agência federal passou a dirigir diretamen
- A conformidade de uma amostra de efluente não prova, por si só, que toda a rede permaneceu
- Ao detectar atmosfera potencialmente perigosa em poço de visita, a equipe pode dispensar c
```

## Pedido 44: Tomografia, ressonância, mamografia e medicina nuclear (técnicas) (`radiologia.tc-rm-mamografia-nuclear`, 7 tópicos de edital)

```
Matéria: radiologia.tc-rm-mamografia-nuclear (Tomografia, ressonância, mamografia e medicina nuclear (técnicas))
Capítulos e quantas questões quero em cada um (total 100):
- "Tomografia computadorizada: formação da imagem, protocolos e contraste": 20
- "Ressonância magnética: física, sequências, angio-RM e segurança": 20
- "Mamografia e avaliação técnica da mama: posicionamento, tomossíntese e qualidade": 20
- "Densitometria óssea e radiologia intervencionista: execução e segurança": 20
- "Medicina nuclear para técnicos: radiofármacos, SPECT, PET e radioproteção": 20

Já existem (não repita):
- Em uma TC de abdome, qual referência define o zero da escala de Hounsfield?
- Durante um protocolo de angiotomografia de aorta, a equipe busca capturar o pico de opacif
- Um aparelho exibe CTDIvol de 12 mGy para uma série de 30 cm. Qual informação e interpretaç
- Um exame de TC tem boa resolução em planos axial e coronal, mas a série reconstruída com c
- Uma paciente relata reação prévia a contraste iodado e faz uso de metformina. Na preparaçã
- A TC, diferentemente da RM, obtém as informações de atenuação por meio de raios X e utiliz
- Alterar o nível e a largura da janela de visualização depois de uma TC reduz retrospectiva
- O aumento do pitch em TC helicoidal necessariamente reduz a dose, mesmo em aparelhos com m
- Uma TC contrastada em múltiplas fases deve ter cada aquisição justificada, pois fases adic
- Qual modalidade e sequência são particularmente úteis para pesquisar restrição à difusão n
- Um exame de RM usa sequência FLAIR para estudar lesões periventriculares. Qual é o efeito 
- Paciente portador de implante identificado como MR Conditional foi encaminhado para RM. Qu
- Um protocolo vascular por RM procura evitar contraste intravenoso e destacar sangue em mov
- Após uma RM com DWI de alto sinal em determinada lesão, o mapa ADC não apresenta queda cor
- A RM convencional forma sinal sem empregar raios X, mas mantém riscos relacionados a campo
- A sequência STIR suprime preferencialmente o sinal da gordura e pode ser útil na demonstra
- Por segurança, todo marcapasso é classificado como MR Unsafe e impede qualquer RM, indepen
- Movimento do paciente pode produzir fantasmas na RM, frequentemente manifestos na direção 
- Quais projeções formam, em regra, o par básico de incidências da mamografia bilateral?
- Na imagem mediolateral oblíqua, nota-se prega inframamária fechada e exclusão parcial da p
- Uma mamografia é realizada em sistema de tomossíntese digital. Qual definição técnica está
- Uma paciente com prótese mamária procura avaliação por imagem. Qual manobra pode aumentar 
- Qual indicador se relaciona especialmente à avaliação de exposição em mamografia e deve se
- A compressão mamária pode reduzir a espessura atravessada pelo feixe e limitar movimento e
- Na avaliação técnica de mamografia MLO, a prega inframamária aberta e a inclusão de tecido
- Uma imagem mamográfica com tecido posterior ausente pode ser corrigida integralmente apena
- A indicação e a atribuição de uma categoria BI-RADS são tarefas de interpretação diagnósti
- Qual unidade expressa a densidade mineral óssea areal obtida usualmente por DXA?
- Em qual situação o Z-score é, em geral, mais apropriado do que o T-score para a interpreta
- Duas medidas de DXA no mesmo sítio apresentam pequena diferença, inferior ao limite de alt
- Durante angiografia por subtração digital, qual situação tende a gerar artefato na imagem 
- Em sala de angiografia intervencionista, qual estratégia reduz de maneira mais direta a ex
- A densitometria DXA baseia-se em raios X de dois espectros de energia para estimar massa m
- Osteófitos e calcificações podem elevar artificialmente a DMO aparente da coluna lombar na
- A dose ocupacional em radiologia intervencionista pode ser estimada com precisão a partir 
- A radiologia intervencionista requer simultaneamente controle de imagem, práticas asséptic
- Qual característica define melhor o princípio físico de imagem do PET?
- Em medicina nuclear, qual diferença física é essencial entre SPECT convencional e PET?
- Um estudo PET/CT apresenta foco aparente de captação junto ao diafragma que não coincide e
- Um paciente fez TC sem contraste e outro recebeu 99mTc para cintilografia. Quem pode conti
- Antes de FDG-PET, qual condição pode modificar a biodistribuição e dificultar a interpreta
- O 99mTc é empregado em muitos estudos de medicina nuclear e possui meia-vida física de apr
- No PET/CT, a exposição pode incluir componentes provenientes do radiofármaco e da aquisiçã
- A meia-vida efetiva de um radiofármaco que sofre decaimento e eliminação biológica é sempr
- Em medicina nuclear, contaminação significa presença indesejada de material radioativo, en
```

## Pedido 45: Hidrelétricas (`minas-gerais.hidreletricas`, 6 tópicos de edital)

```
Matéria: minas-gerais.hidreletricas (Hidrelétricas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Usina, rio e território": 25
- "2. Três Marias e Furnas: água, energia e usos": 25
- "3. Ecossistemas, água e gestão socioambiental": 25
- "4. Reescrita das relações espaciais e como a IDECAN cobra": 25

Já existem (não repita):
- Sobre a formação de um reservatório hidrelétrico, assinale a afirmação correta.
- Qual alternativa descreve com precisão a relação entre hidreletricidade e impactos socioam
- Sobre a referência territorial de Três Marias na gestão das águas mineiras, assinale a cor
- Em relação ao reservatório de Furnas e à gestão das águas em Minas Gerais, assinale a afir
- Qual conclusão é compatível com uma análise socioambiental equilibrada de uma hidrelétrica
- Qual alternativa caracteriza corretamente um efeito direto possível da implantação de um r
- Uma comunidade usa a margem de um reservatório para recreação e nota mudanças no acesso du
- Qual leitura da mudança ecológica associada ao barramento é a mais consistente?
- O represamento de um rio pode afetar processos físicos além de elevar a água a montante. A
- Um boletim indica alteração em um parâmetro de qualidade da água em um reservatório. Qual 
- Sobre o uso da água em reservatórios hidrelétricos, assinale a afirmação correta.
- Qual enunciado sobre a atuação dos comitês de bacia é adequado ao estudo de um reservatóri
- Uma análise de impactos sociais de um reservatório deve evitar qual procedimento?
- Uma comparação entre dois aproveitamentos hidrelétricos deve respeitar a singularidade de 
- Qual conjunto de medidas é coerente com a gestão socioambiental de um reservatório?
- Como deve ser interpretada a possibilidade de uso recreativo das margens de uma represa?
- Uma reescrita preserva o sentido de que a formação de reservatório pode reorganizar habita
- Uma família dependia de área produtiva depois inundada por um reservatório. Qual encadeame
- Uma avaliação comparativa identifica a geração elétrica como característica comum e condiç
- Após um resultado de monitoramento, um estudante atribui a alteração da água somente à usi
- A análise aponta que barramento, habitat e usos humanos se relacionam, mas os dados dispon
- Um município propõe manter sempre o nível do reservatório para favorecer o lazer, embora o
- Em uma área, o barramento reduz a continuidade de ambientes aquáticos; em outra, os dados 
- Uma comparação afirma que as consequências sociais de um reservatório mais conhecido são n
- A água de um reservatório apresenta mudança em um parâmetro; a bacia também possui áreas u
- Uma proposta de compensação para área afetada por reservatório foi apresentada sem medidas
- Uma reportagem relata que um trecho a jusante recebe água em condições diferentes após mud
- Em uma reunião de gestão de bacia, usuários divergem sobre usos da água no entorno de um r
- O enunciado informa que um reservatório foi criado em vale com vegetação, atividades rurai
- Um aluno reescreve a frase “a operação do reservatório pode interferir em usos a jusante” 
- A formação de um reservatório pode transformar parte do vale fluvial e modificar a paisage
- Como a hidreletricidade usa um recurso renovável, sua produção garante ausência de efeitos
- O IGAM reúne informações e serviços relacionados ao monitoramento da qualidade das águas e
- A geração de eletricidade por um reservatório comprova que as áreas inundadas não tinham u
- A inundação de áreas é um efeito físico diretamente associado à formação de um reservatóri
- As consequências de um reservatório ficam limitadas à aparência da paisagem e não podem al
- Usos como geração, abastecimento e recreação podem compartilhar um reservatório e também e
- Se a água de um reservatório apresenta alteração em um ponto, todas as atividades da bacia
- A gestão por bacias permite considerar relações entre montante, reservatório e jusante, em
- A continuidade de uma atividade humana junto ao reservatório prova que habitats aquáticos 
- A operação de uma hidrelétrica pode influenciar usos a jusante, e a avaliação depende das 
- A mudança de água corrente para ambiente de reservatório pode reorganizar habitats, sem pr
- Uma medição de qualidade da água que registre alteração identifica, sem outros dados, a us
- A atuação de um comitê de bacia pode substituir integralmente o operador da usina e os órg
- A distinção entre um efeito direto da inundação e uma consequência social encadeada ajuda 
```

## Pedido 46: Estatuto da Polícia Civil de Alagoas (Lei 3.437/1975) (`alagoas.estatuto-policia-civil-lei-3437`, 6 tópicos de edital)

```
Matéria: alagoas.estatuto-policia-civil-lei-3437 (Estatuto da Polícia Civil de Alagoas (Lei 3.437/1975))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições preliminares, autoridades e cargos": 25
- "2. Provimento, posse, exercício e movimentação": 25
- "3. Vencimento, vantagens, acumulação e deveres": 25
- "4. Regime disciplinar, processo, Conselho e disposições finais": 25

Já existem (não repita):
- O regime jurídico dos funcionários da Polícia Civil de Alagoas compõe-se das normas especi
- Os ocupantes de cargos em comissão e funções gratificadas com atribuições de natureza poli
- Com a redação dada pela Lei 9.798/2026, o membro da Polícia Judiciária de Alagoas pode, ha
- Pela Lei 3.437/1975, a Polícia Civil do Estado de Alagoas é subordinada diretamente, para 
- Segundo o art. 8º da Lei 3.437/1975, NÃO é autoridade policial civil:
- Os cargos da Parte Suplementar do Quadro da Polícia Civil de Alagoas são de provimento em 
- A função policial, conforme a Lei 3.437/1975, caracteriza-se pelo dever de determinar, fis
- Para o provimento do cargo de Médico Legista, a Lei 3.437/1975 exige:
- São formas de provimento dos cargos de natureza policial previstas no art. 36 da Lei 3.437
- Além dos requisitos gerais, para os cargos policiais de provimento efetivo a Lei 3.437/197
- Para os cargos de Corregedor Geral de Polícia, Chefe de Gabinete da SSP, Diretores de Depa
- O concurso para cargos efetivos da Polícia Civil de Alagoas é realizado pela Secretaria de
- O policial civil nomeado deve tomar posse em 30 dias da publicação do ato no Diário Oficia
- Segundo a Lei 3.437/1975, não haverá posse nos casos de:
- Durante o estágio probatório, o responsável pela unidade de trabalho do policial encaminha
- A elevação do policial civil da classe final de uma série de classes a cargo de classe ini
- É vedada a transferência ao policial civil que, nos dois anos anteriores ao pedido, tenha 
- O policial civil de Alagoas pode ser removido para órgão de outra área da administração es
- Sobre a remoção por conveniência da disciplina, é correto afirmar que:
- Na precedência hierárquica da Polícia Civil de Alagoas, o primeiro critério é o maior níve
- A ajuda de custo prevista na Lei 3.437/1975:
- O auxílio-moradia do policial removido de uma para outra sede corresponde a:
- O policial civil processado penalmente em decorrência do cumprimento do dever tem direito 
- O acidente em serviço que gera auxílio-acidente deve ser atestado pelo chefe do órgão de l
- Na gratificação por cursos de formação, treinamento, especialização ou aperfeiçoamento, a 
- A proibição de acumular cargos prevista na Lei 3.437/1975 estende-se a cargos, funções ou 
- Entre os deveres do policial civil, a Lei 3.437/1975 inclui zelar pela dignidade da função
- Constitui transgressão disciplinar, nos termos do art. 88 da Lei 3.437/1975:
- Segundo a Lei 3.437/1975, é causa agravante de falta disciplinar o fato de ter sido cometi
- A pena de suspensão prevista no Estatuto da Polícia Civil de Alagoas não excederá:
- Quando houver conveniência para o serviço, a pena de suspensão poderá ser convertida em mu
- A detenção disciplinar acarreta a perda dos vencimentos do policial durante o período de c
- O policial que se recusa a cumprir a ordem de detenção disciplinar ou desatende suas norma
- Segundo o art. 106 da Lei 3.437/1975, é causa de demissão a falta ao serviço, sem justa ca
- Compete privativamente ao Governador do Estado aplicar ao policial civil as penas de:
- Os Delegados em geral e os Diretores de repartições podem aplicar as penas de repreensão e
- A suspensão preventiva de até 30 dias e a detenção disciplinar preventiva podem ser prorro
- O inquérito disciplinar previsto na Lei 3.437/1975 é promovido por:
- O processo disciplinar deve preceder a aplicação de suspensão por mais de quinze dias, des
- A simples alegação de injustiça da penalidade constitui fundamento suficiente para a revis
- O Conselho Superior de Polícia, previsto na Lei 3.437/1975, é presidido pelo:
- A carteira de identidade policial, expedida pelo Instituto de Identificação do Estado, con
- O policial civil preso em flagrante, preventivamente ou por pronúncia tem direito à prisão
- O policial civil que se invalidar definitivamente em razão do serviço, segundo a Lei 3.437
```

## Pedido 47: Indicadores sociais e análise de dados públicos (`estatistica.indicadores-sociais-dados`, 5 tópicos de edital)

```
Matéria: estatistica.indicadores-sociais-dados (Indicadores sociais e análise de dados públicos)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos e construção de indicadores sociais": 20
- "Taxas, coeficientes, proporções e números-índice": 20
- "Indicadores de desenvolvimento e desigualdade": 20
- "Bases oficiais: IBGE, DATASUS e Atlas do Desenvolvimento": 20
- "Leitura crítica de tabelas, gráficos e indicadores públicos": 20

Já existem (não repita):
- Um registro de 500 domicílios atendidos, sem especificar universo de referência, é suficie
- A ficha técnica de um indicador deve explicitar definição operacional, fonte, período de r
- A validade de um indicador refere-se à adequação de sua mensuração ao fenômeno que se pret
- Quando um programa eleva o número de beneficiários, está necessariamente comprovado que ge
- Se 18 de cada 60 domicílios elegíveis recebem um serviço, a proporção de cobertura é de 30
- Uma proporção que passa de 20% para 30% teve crescimento de 10%, independentemente de se e
- Para calcular o coeficiente de mortalidade infantil, utiliza-se usualmente como denominado
- Com índice simples de base 100, o valor 140 indica que a variável cresceu 140% em relação 
- Se o indicador aumenta de índice 110 para índice 121, sua variação relativa entre esses do
- A taxa bruta de mortalidade é sempre suficiente para comparar a qualidade da atenção à saú
- PIB per capita corresponde necessariamente ao rendimento domiciliar mensal recebido por ca
- A medida sintética de desenvolvimento humano contempla dimensões de saúde/longevidade, edu
- Em medidas comparáveis do índice de Gini de rendimentos, passar de 0,42 para 0,55 expressa
- Um município cujo índice de Gini é menor do que o de outro possui necessariamente PIB per 
- A PNAD Contínua produz estimativas baseadas em amostra, ao passo que o Censo Demográfico é
- A ocorrência de internação registrada em SIH/SUS equivale necessariamente a uma pessoa dis
- O SINASC e o SIM podem ser utilizados conjuntamente para construir indicadores de mortalid
- Na combinação de coberturas de municípios com quantidades diferentes de domicílios, a médi
- Em um gráfico de dispersão de indicadores municipais, uma associação forte entre duas vari
- Um aumento do número de casos registrados após expansão da capacidade de testagem pode ref
- Uma secretaria precisa monitorar atendimento de 750 famílias entre 1.000 famílias elegívei
- Em avaliação pública, a classificação mais adequada do número de unidades habitacionais ef
- Um analista compara duas taxas denominadas 'acesso à água', calculadas em fontes diferente
- A precisão de uma pesquisa amostral é menor para determinada desagregação territorial do q
- A secretaria mede se o tempo de espera percebido pelo usuário melhorou. Qual afirmação sob
- Em um município hipotético houve 12 óbitos de menores de um ano e 800 nascidos vivos no pe
- Uma cobertura passa de 25% para 35%. A variação expressa em pontos percentuais corresponde
- Considere número-índice base 100: o indicador de produção assumiu 125 no segundo período. 
- Uma série utiliza base antiga: índice de 150 no ano atual e de 125 no ano escolhido como n
- Ao comparar valores de renda monetária em dois anos distantes, qual procedimento é essenci
- Dois municípios têm o mesmo PIB per capita. Qual conclusão é logicamente defensável sem ou
- No índice de Gini de distribuição de renda, mantidos conceito e população, a passagem de 0
- O conjunto de dimensões utilizado para sintetizar desenvolvimento humano em IDH/IDHM compr
- No Atlas do Desenvolvimento Humano no Brasil, o IDHM é disponibilizado por uma parceria de
- Em uma curva de Lorenz de rendimentos, o distanciamento crescente em relação à diagonal de
- Um pesquisador precisa de tabelas agregadas oficiais do IBGE com recorte por ano, variável
- Para buscar registros oficiais de nascidos vivos em uma base do Ministério da Saúde, qual 
- Um servidor lê uma tabela de SIH/SUS sobre internações hospitalares. Qual cuidado torna su
- No acompanhamento de taxa de mortalidade infantil, utilizar óbitos por local de ocorrência
- Dois relatórios divulgam valores diferentes para um indicador cujo nome é igual. Qual hipó
- Em dois setores, o primeiro tem 1.000 domicílios e cobertura de 80%; o segundo, 4.000 domi
- Em mapa municipal com população muito desigual, comparar apenas números absolutos de ocorr
- Uma tabela apresenta 50% de cobertura em 2022 e 60% em 2023. O crescimento relativo da tax
- A frequência relativa global de um evento diminui, mas sua contagem absoluta aumenta. Qual
- Um gráfico de linhas de renda familiar alterna metodologia de cálculo entre dois anos. A i
- Num painel de educação, a 'taxa de frequência escolar' foi obtida dividindo alunos frequen
- Num índice base 100, ano A = 80 e ano B = 100. Qual é o crescimento relativo do índice de 
- Um gestor anuncia que, porque o índice de Gini caiu, não há mais pobreza no município. O p
- Uma amostra domiciliar mostra estimativa para a capital, mas não fornece resultado municip
- Um mapa de desigualdade usa cores fortes para representar o Gini, mas omite legenda, data 
```

## Pedido 48: Estatuto dos Policiais Militares de Alagoas (Lei nº 5.346/1992) (`legislacao.estatuto-pm-alagoas-lei-5346`, 5 tópicos de edital)

```
Matéria: legislacao.estatuto-pm-alagoas-lei-5346 (Estatuto dos Policiais Militares de Alagoas (Lei nº 5.346/1992))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Finalidade, natureza, conceitos e ingresso": 20
- "2. Hierarquia, cargo, comando, direitos, deveres e ética": 20
- "3. Ausente, desertor, desaparecido, extraviado e exclusão do serviço ativo": 20
- "4. Promoção, uniformes, agregação, reversão e excedente": 20
- "5. Afastamentos, licenças, recompensas, tempo de serviço e disposições finais": 20

Já existem (não repita):
- Segundo a Lei nº 5.346/1992, a Polícia Militar do Estado de Alagoas é:
- Pela Lei nº 5.346/1992, é privativa de brasileiro nato a carreira de oficial da Polícia Mi
- Segundo os conceitos do Estatuto da PMAL, posto e graduação são conferidos, respectivament
- Para o Estatuto da PMAL, ordem pública é a situação de convivência pacífica e harmoniosa d
- Pelo Estatuto da PMAL, com a redação de 2007, a altura mínima para ingresso é de:
- No serviço temporário, o voluntário matriculado no curso de formação de sargentos, oriundo
- No círculo de oficiais intermediários da Polícia Militar de Alagoas encontra-se o posto de
- Pelo Estatuto da PMAL, os aspirantes a oficial e os cadetes são denominados praças especia
- Persistindo o empate na antiguidade entre policiais militares depois de aplicados os crité
- Segundo o Estatuto da PMAL, o Comandante Geral da Polícia Militar tem honras, regalias, di
- No Estatuto da PMAL, os cabos e soldados são:
- Pelo Estatuto da PMAL, no cumprimento de ordens recebidas, o executante fica isento de res
- Entre os direitos e prerrogativas dos policiais militares previstos no art. 30 do Estatuto
- Pelo Estatuto da PMAL, somente em caso de flagrante delito o policial militar poderá ser p
- Segundo o Estatuto da PMAL, a violação dos deveres e das obrigações policiais militares:
- Pelo Estatuto da PMAL, as punições disciplinares de detenção ou prisão não poderão ultrapa
- No Estatuto da PMAL, o aspirante a oficial e as praças com estabilidade assegurada, presum
- Segundo o Estatuto da PMAL, é considerado desertor o policial militar que, por mais de oit
- No Estatuto da PMAL, a praça sem estabilidade assegurada, oficialmente declarada desertora
- Pelo Estatuto da PMAL, o policial militar desaparecido por mais de trinta dias será oficia
- Segundo o Estatuto da PMAL, em caso de naufrágio, sinistro aéreo, catástrofe ou calamidade
- São formas de exclusão do serviço ativo da Polícia Militar de Alagoas:
- Pelo Estatuto da PMAL, não será concedida transferência para a reserva remunerada, a pedid
- Segundo o Estatuto da PMAL, o policial militar julgado incapaz definitivamente em razão de
- Pelo Estatuto da PMAL, a demissão aplica-se exclusivamente aos oficiais, ao passo que o li
- Segundo o Estatuto da PMAL, o oficial será demitido ex officio quando:
- Pelo Estatuto da PMAL, a demissão a pedido do oficial com mais de cinco anos de oficialato
- Segundo o Estatuto da PMAL, a anulação de incorporação será aplicada ao policial militar q
- No Estatuto da PMAL, a promoção dos oficiais é feita por ato do Governador, mediante propo
- Na redação dada pela Lei nº 6.150/2000, a agregação é a situação na qual o policial milita
- Pelo Estatuto da PMAL, o policial militar da ativa que se alistar como candidato a cargo e
- Segundo o Estatuto da PMAL, é agregado o policial militar afastado por ter sido julgado in
- Pelo Estatuto da PMAL, o policial militar promovido por bravura, sem haver vaga, passa à s
- Segundo o Estatuto da PMAL, são considerados afastamentos temporários:
- Pelo Estatuto da PMAL, a interrupção das férias por interesse da Segurança Nacional ou de 
- Segundo o Estatuto da PMAL, a licença para trato de interesse particular:
- Pelo Estatuto da PMAL, a licença para acompanhar tratamento de saúde de pessoa da família 
- Segundo o Estatuto da PMAL, NÃO é computável para qualquer efeito o tempo:
- Pelo Estatuto da PMAL, o direito de recorrer na esfera administrativa prescreve em quinze 
- Segundo o Estatuto da PMAL, o oficial da reserva remunerada pode ser convocado para o serv
- Pelo Estatuto da PMAL, é vedado o casamento ao cadete durante o Curso de Formação de Ofici
```

## Pedido 49: Ética no serviço público: Decreto 1.171 e conflito de interesses (`etica.etica-servico-publico-federal`, 5 tópicos de edital)

```
Matéria: etica.etica-servico-publico-federal (Ética no serviço público: Decreto 1.171 e conflito de interesses)
Capítulos e quantas questões quero em cada um (total 100):
- "Decreto nº 1.171/1994: regras deontológicas e deveres": 20
- "Decreto nº 1.171/1994: vedações e aplicação em casos concretos": 20
- "Comissões de Ética e Sistema de Gestão da Ética": 20
- "Lei nº 12.813/2013: conflito de interesses durante e após o cargo": 20
- "Integridade pública, prevenção e coordenação institucional": 20

Já existem (não repita):
- O Decreto nº 1.171/1994 aprova código direcionado ao servidor público civil do Poder Execu
- Segundo o anexo do Decreto nº 1.171/1994, basta ao servidor verificar se sua conduta é for
- O dever de falar a verdade, no Código de Ética, deve ser interpretado juntamente com as hi
- Uma ordem superior para favorecer um amigo do gestor deve ser observada sem questionamento
- É vedado ao servidor utilizar influências decorrentes do cargo para privilegiar terceiro, 
- A solidariedade entre colegas autoriza que um servidor deixe de comunicar comportamento an
- Deturpar documento encaminhado para providências viola vedação específica do Código, ainda
- O uso de informação privilegiada obtida no serviço para beneficiar um amigo somente será v
- A Comissão de Ética Pública integra o Sistema de Gestão da Ética do Poder Executivo Federa
- O Decreto nº 6.029/2007 estabelece que cada comissão setorial terá sete membros titulares,
- A existência de um procedimento ético afasta a exigência de contraditório, pois a decisão 
- Ao encontrar indícios de infração disciplinar, a comissão de ética poderá encaminhar os au
- A configuração de conflito de interesses, segundo a Lei nº 12.813/2013, independe do efeti
- A restrição de não usar informação privilegiada recebida na função pública cessa automatic
- Por força do art. 10 da Lei nº 12.813/2013, os deveres do art. 4º e as situações do art. 5
- Os impedimentos profissionais de seis meses do art. 6º, II, são aplicáveis a todos os agen
- O Sitai, do Decreto nº 11.529/2023, conta com a CGU como órgão central e com unidades seto
- O Sistema de Gestão da Ética e o Sitai são o mesmo sistema jurídico, instituído originalme
- Um plano de integridade organiza medidas e ações para determinado período, não se confundi
- Se não houve denúncia recebida sobre conflito de interesses, fica juridicamente demonstrad
- Durante atendimento, um servidor indeferiu pedido contrário à lei, explicou a razão em lin
- Uma banca pergunta qual é o critério especialmente destacado no item II das regras deontol
- Uma chefe orienta equipe a informar prazos deliberadamente falsos para reduzir reclamações
- Assinale a atitude que expressa adequadamente o dever relativo à hierarquia previsto no Có
- Ao organizar atendimento, o gestor identifica atraso evitável de meses em pedidos simples;
- Ao utilizar o carro e o motorista oficial para realizar compras particulares, sem autoriza
- Um funcionário altera a data de documento para ocultar atraso, argumentando que isso melho
- Uma servidora usa informação interna não divulgada para favorecer empresa de um amigo, sem
- Qual conduta, tomada isoladamente, traduz a proibição de conivência presente no Código do 
- Em atendimento a um cidadão, o servidor se recusa a entregar o resultado já disponível por
- Qual associação entre órgão e composição corresponde ao Decreto nº 6.029/2007?
- No Sistema de Gestão da Ética do Poder Executivo Federal, quem coordena, avalia e supervis
- Investigado em procedimento ético recebe notícia de prova nova juntada depois da manifesta
- A comissão de ética conclui que houve falta ética atribuída a servidor, sem decidir sobre 
- Durante apuração ética surgem elementos plausíveis de crime praticado por agente do órgão;
- Uma consultoria privada tem interesse direto em deliberação colegiada de que participa um 
- De acordo com a Lei nº 12.813/2013, qual requisito NÃO é exigido para a configuração de co
- Uma pessoa desligou-se de cargo abrangido pela Lei de Conflito de Interesses e pretende us
- Qual alternativa descreve corretamente a extensão promovida pelo art. 10 da Lei nº 12.813/
- Uma ex-dirigente busca vínculo profissional em empresa atuante na área de competência de s
- Um Ministério estrutura um programa para prevenir fraude, detectar desvios e monitorar pro
- No desenho institucional do Decreto nº 11.529/2023, como se relacionam programa de integri
- Uma unidade setorial do Sitai detecta risco de direcionamento de contratações; qual medida
- Na comparação entre os sistemas federais, qual par está corretamente associado?
- Um órgão deseja demonstrar maturidade em integridade; qual conjunto de iniciativas é mais 
```

## Pedido 50: Suporte técnico e manutenção de computadores (`informatica.suporte-tecnico-hardware`, 5 tópicos de edital)

```
Matéria: informatica.suporte-tecnico-hardware (Suporte técnico e manutenção de computadores)
Capítulos e quantas questões quero em cada um (total 100):
- "Rotinas de bancada, montagem e manutenção física": 20
- "Instalação de sistemas operacionais, drivers e recuperação": 20
- "Diagnóstico estruturado de falhas e ferramentas": 20
- "Impressoras, periféricos e redes locais no suporte": 20
- "Atendimento ao usuário, chamados, segurança e qualidade": 20

Já existem (não repita):
- Na manutenção preventiva de computadores, inspeções, limpeza segura e monitoramento podem 
- Um LED aceso na fonte de alimentação demonstra, por si só, que todas as tensões permanecer
- Antes de substituir uma unidade de armazenamento de um usuário, o técnico deve verificar a
- A tecnologia UEFI e a tabela de partições GPT designam o mesmo recurso técnico, sendo admi
- O Gerenciador de Dispositivos pode ajudar a investigar drivers, fornecendo estado de dispo
- Um alerta amarelo exibido ao lado de um dispositivo no Windows comprova que o componente e
- Quando um computador não detecta a unidade de armazenamento no firmware, reinstalar imedia
- O comando sfc /scannow e o utilitário chkdsk têm objetivos idênticos, pois ambos examinam 
- Um resultado favorável em teste rápido de memória elimina, com certeza, a possibilidade de
- O diagnóstico eficaz procura isolar variáveis e relacionar sintomas a mudanças recentes an
- A falta de resposta ao ping comprova, isoladamente, que o computador de destino e seu serv
- No Windows, ipconfig /flushdns limpa entradas do cache de resolução DNS do cliente, mas nã
- Uma impressora que executa cópia local necessariamente dispõe de comunicação de rede funci
- Um endereço IPv4 da faixa 169.254.0.0/16 pode sinalizar autoconfiguração após falha em obt
- Em chamados de suporte, a prioridade deve depender apenas da ordem cronológica de abertura
- Uma instalação de software autorizada pode ser registrada como requisição de serviço, mesm
- No atendimento remoto, é aceitável solicitar ao usuário o código de autenticação multifato
- Mesmo quando há solução temporária que restabelece o serviço, a investigação da causa-raiz
- Limpar indiscriminadamente a fila de impressão compartilhada é ação sem impacto sobre outr
- Ao encerrar um chamado de falha intermitente que não foi reproduzida, o registro honesto d
- Durante a abertura de um gabinete para limpeza, qual conduta oferece melhor segurança oper
- Após instalar um módulo de memória, o equipamento liga, mas apresenta código POST de erro 
- Uma equipe prepara instalação limpa de sistema em computador que contém dados instituciona
- Qual afirmação separa corretamente conceitos usados no processo de instalação de um sistem
- Um adaptador de rede apresenta erro após atualização de driver. Qual estratégia é tecnicam
- Ao investigar um Windows que não inicia corretamente após mudança de hardware, qual sequên
- Um técnico pretende verificar arquivos protegidos do Windows depois de identificar possíve
- Uma unidade mecânica apresenta ruído anormal, lentidão crescente e alerta de saúde. Qual d
- Um computador reinicia durante atividades intensas e não falha em repouso. Qual abordagem 
- Na análise de um computador que congela após login, qual afirmação sobre ferramentas e tes
- Uma impressora conectada por USB não aparece nas configurações do Windows. Qual verificaçã
- Qual comando do Windows exibe IP, gateway, DNS e dados detalhados dos adaptadores?
- Um site abre pelo endereço IP, mas não pelo nome; outros fatores permanecem iguais. Qual p
- Uma fila compartilhada tem documentos travados. Qual é a melhor primeira conduta do suport
- Uma estação recebe endereço IPv4 169.254.x.x, não possui gateway válido e outros usuários 
- Um escritório relata parada simultânea de impressoras e sistemas em 40 estações, enquanto 
- Qual situação caracteriza mais diretamente uma requisição de serviço, e não incidente?
- Durante apoio remoto, usuário oferece senha pessoal e código MFA para acelerar uma instala
- Após contornar temporariamente falha recorrente de rede, qual registro melhor orienta a co
- Ao concluir manutenção em notebook com dados protegidos, qual conjunto de evidências const
```

