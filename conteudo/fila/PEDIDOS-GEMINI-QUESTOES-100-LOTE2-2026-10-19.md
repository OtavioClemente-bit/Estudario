# Pedidos para o Gemini: 100 questões extras por matéria, lote 2 (19/10/2026: matérias 51 a 100 em uso)

Mesmo esquema do lote 1 (PEDIDOS-GEMINI-QUESTOES-100-2026-10-19.md). A Mensagem 0 ganhou as regras que faltaram no lote 1 (tamanho da correta, explicação sem frase de molde, alternativas distintas). **Use uma conversa NOVA** e cole esta Mensagem 0. Salve cada resposta como `<id>.questoes.json` em Downloads.

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
- Varie o tipo: conceito, caso prático, "assinale a incorreta", assertivas I, II e III, exceção, comparação, letra da lei. Não comece várias questões com a mesma frase de molde.
- Alternativas erradas PLAUSÍVEIS: erros que um candidato que estudou mal cometeria (troca de prazo, de órgão, de conceito vizinho, exceção virando regra). Nada absurdo ou caricato, como "publicar o nome do paciente" ou "eliminar toda fiscalização", que se elimina sem saber a matéria.
- A questão tem que cobrar o assunto DO CAPÍTULO indicado em "section", não de outro capítulo.
- Se não tiver certeza de número de artigo, lei, prazo ou súmula, não cite o número: cobre o conteúdo. Nunca invente norma, jurisprudência ou dado.
- Certo/Errado no estilo Cebraspe: afirmação única, precisa, com o erro (quando houver) num detalhe técnico, não em palavra absurda.
- Português do Brasil, nível de prova de concurso real.
```

## Pedido 1: Formação de palavras (`portugues.formacao-de-palavras`, 1541 tópicos de edital)

```
Matéria: portugues.formacao-de-palavras (Formação de palavras)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura morfológica e elementos formadores": 21
- "2. Derivação e suas modalidades": 20
- "3. Composição, hibridismo e radicais eruditos": 20
- "4. Redução, abreviações e outros processos": 20
- "5. Diagnóstico avançado e critérios de prova": 19

Já existem (não repita):
- Na revisão de um texto, a forma “refazer” foi relacionada ao verbo “fazer”. Qual classific
- Ao comparar “pedra”, “pedreiro” e “pedregoso”, um estudante quer indicar a unidade lexical
- Na frase “A atitude parece infeliz”, a palavra “infeliz” está relacionada ao adjetivo “fel
- Em um comentário, aparece “A lealdade é essencial”. Considerando “leal” como base de “leal
- Ao decompor didaticamente “cantar” em cant-a-r, a análise pede a função do segmento -a-. Q
- Em uma notícia, a frase “A pesca foi suspensa” usa o substantivo “pesca” relacionado a “pe
- No período “O jantar atrasou, mas vamos jantar”, a primeira ocorrência de “jantar” funcion
- No trecho “Vai entardecer antes da chegada”, a forma “entardecer” foi associada a “tarde”.
- Um relatório contém “infelizmente”. Um leitor afirmou que a presença de in- e -mente basta
- Na sequência “leal, desleal, lealdade, deslealdade”, qual argumento afasta a parassíntese 
- Um glossário traz “anoitecer”, a partir de “noite”. Para aplicar corretamente o teste da p
- A frase “O ataque começou cedo” emprega o substantivo “ataque”, associado a “atacar”. Qual
- Na expressão “um campo de girassóis”, a palavra “girassol” não apresenta hífen. Qual class
- Em uma lista de vocábulos compostos, aparece “planalto”, relacionado a “plano + alto”. Qua
- Ao revisar “paraquedas”, alguém propõe que toda palavra composta escrita sem hífen seja ex
- Em uma mensagem informal, a pessoa escreve “Mandei a foto”. A palavra “foto” se relaciona 
- Em instruções de preenchimento, lê-se “Os CPFs deverão ser informados”. Qual observação co
- Ao analisar “cantávamos” como cant-a-va-mos, a pessoa confundiu o sufixo derivacional com 
- Um exercício compara “o jantar terminou” com “a pesca terminou”. A distinção mais defensáv
- No par “possível / impossível”, qual é a melhor análise do segmento inicial de “impossível
- O termo “televisão” combina o elemento “tele-” e a base “visão”. Ao indagar especificament
- Em uma narrativa, “o tique-taque do relógio” evoca o som de um mecanismo. Qual formação se
- Um editor confrontou “anti-inflamatório” e “antissocial”. Segundo a Base XVI do Acordo Ort
- Ao preparar uma lista ortográfica, surgiram “coautor” e “micro-ondas”. Qual justificativa 
- A frase “A foto está na pág. 3, e os CPFs constam no formulário” reúne três recursos disti
- Na família formada por pedra, pedreiro e pedregoso, o segmento pedr- pode ser reconhecido 
- Em “felizmente”, o segmento -mente é desinência flexional de número, pois transforma o adj
- A palavra “refazer” exemplifica derivação prefixal, pois recebe re- antes da base fazer, c
- No enunciado “O jantar começou”, jantar é derivado regressivo de jantar, porque o artigo o
- A palavra “lealdade” pode ser explicada como derivação sufixal, em que o sufixo -dade se a
- Toda palavra composta sem hífen é formada por aglutinação, já que a grafia unida comprova 
- A forma “foto”, usada em lugar de “fotografia”, é exemplo de redução vocabular e pode ser 
- Na análise escolar, “infelizmente” é necessariamente parassintética, uma vez que contém si
- A formação de “entardecer” envolve, na análise tradicional, ação conjunta de en- e -ecer s
- Na derivação regressiva, qualquer substantivo curto relacionado semanticamente a um verbo 
- A classificação de planalto como aglutinação pode ser justificada pela fusão de plano e al
- “Televisão” é um hibridismo apenas porque sua grafia possui mais de um elemento e não apre
- Em redação formal, “CPFs” pode ser grafado com s minúsculo de plural, sem apóstrofo, em co
- A Base XVI do Acordo Ortográfico determina hífen em antissocial, pois o prefixo anti- é se
- A grafia de paraquedas sem hífen não impede classificá-la, tradicionalmente, como composiç
- Em “o não foi categórico”, o vocábulo não passa a substantivo por receber um sufixo nomina
- A expressão “micro-ondas” emprega hífen segundo a Base XVI porque há encontro de vogais ig
- As palavras “pág.” e “foto” são ambas abreviaturas gráficas porque toda representação mais
- O termo “tique-taque” pode ilustrar onomatopeia mesmo sendo escrito com hífen, pois a clas
- A palavra “coautor” deve obrigatoriamente ser escrita “co-autor”, pois o prefixo co- exige
```

## Pedido 2: Razão e proporção (`matematica.razao-proporcao`, 1531 tópicos de edital)

```
Matéria: matematica.razao-proporcao (Razão e proporção)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Razões entre números, grandezas e taxas unitárias": 25
- "2. Proporções, igualdade de razões e propriedades fundamentais": 32
- "3. Proporcionalidade direta e inversa em tabelas e gráficos": 12
- "4. Divisão diretamente e inversamente proporcional": 20
- "5. Aplicações avançadas, leitura de gráficos e conferência de modelos": 11

Já existem (não repita):
- Em um depósito há 18 caixas vermelhas e 12 azuis. Qual é a razão simplificada de caixas ve
- Uma fita mede 1,5 metro e outra mede 50 centímetros. Qual é a razão do comprimento da prim
- Uma unidade de produção fabricou 240 peças durante seis horas. Qual é a taxa média de peça
- Duas razões são iguais: 3 para 5 e 12 para x. Qual valor de x mantém a proporção?
- Uma tabela contém os pares (2,6), (4,12) e (7,21) de duas grandezas x e y. Qual classifica
- Para uma distância fixa de 120 km, um veículo gasta quatro horas a 30 km/h e três horas a 
- Uma quantidade de 300 unidades será dividida diretamente na razão 2 para 3. Quanto receber
- Um gráfico representa y=4x para valores positivos de x. Qual afirmação descreve essa relaç
- Há 24 plantas ornamentais e 36 árvores em um jardim. Qual é a razão entre as plantas ornam
- Uma escala de mistura conserva a proporção entre x para 4 e 9 para 12. Que valor deve ser 
- Uma quantidade de 540 unidades deve ser dividida diretamente segundo os pesos 2, 4 e 3. Qu
- Um total de 330 unidades será repartido em partes inversamente proporcionais aos números 2
- Uma tabela de pares (x,y) apresenta (2,7), (5,17,5) e (8,28). Qual leitura identifica corr
- Uma equipe de quatro pessoas, com rendimento individual constante, conclui um serviço em q
- Dois valores positivos estão na razão 3:5, e o maior supera o menor em 32 unidades. Quais 
- Uma embalagem contém 0,9 kg de alimento e custa 27 unidades monetárias. Qual é o preço por
- Um total de 350 unidades será dividido entre A e B, diretamente aos indicadores 4 e 3 e in
- Três quantidades A, B e C obedecem às razões A:B=2:3 e B:C=4:5. Sabendo que a soma das trê
- Uma tabela apresenta os pares (2,13), (4,19) e (6,25). Qual diagnóstico identifica correta
- Um montante de 420 unidades será dividido em parcelas inversamente proporcionais aos pesos
- Um medidor registra 100 km no instante de 2 horas e 160 km no instante de 3 horas. Qual fo
- Uma quantidade de 510 unidades será repartida inversamente aos números 2, 3 e 9. Qual é a 
- Duas taxas de velocidade foram expressas como 1,2 metro por segundo e 4,32 quilômetros por
- Três números positivos estão na razão 2:5:7 e a diferença entre o maior e o menor é 45. Qu
- Um total de 850 unidades será dividido entre A e B, diretamente aos valores 3 e 4 e invers
- Em uma sala há oito mesas grandes e quatro pequenas. A razão de mesas grandes para pequena
- Uma barra mede dois metros e outra mede cinquenta centímetros. A razão do primeiro comprim
- Na igualdade entre razões 2/3 e 8/12, o produto dos extremos coincide com o dos meios, poi
- Se uma relação apresenta y=5x+2 para x positivo, ela é diretamente proporcional, pois o va
- Em uma tabela com pares (2,15), (3,10) e (5,6), as grandezas são inversamente proporcionai
- Um total de 100 unidades dividido diretamente na razão 2:3 deve produzir parcelas iguais d
- Se uma fita é cortada em cinco trechos iguais de dois metros, o comprimento total é direta
- Uma divisão inversamente proporcional aos números 2, 3 e 6 deve usar as parcelas na razão 
- Uma produção de 420 unidades distribuída diretamente nos pesos 2, 3 e 5 produz parcelas 84
- Duas quantidades estão na razão 5:3 e a maior supera a menor em 18 unidades. Os respectivo
- Se duas grandezas estão relacionadas por y=12/x, para x positivo, os valores observados em
- Um lote contém 15 peças A e 10 peças B. A razão de A para B é 3:5, pois o denominador da r
- Se A:B=3:4 e B:C=2:5, é possível expressar A:B:C pela razão 3:4:10, compatibilizando o ter
- Uma velocidade de 0,5 metro por segundo, comparada à velocidade de 3,6 quilômetros por hor
- Um total de 360 unidades dividido entre duas pessoas diretamente aos valores 6 e 8 e inver
- Os pares (1,12), (2,19) e (3,26) descrevem proporcionalidade direta porque a segunda grand
- Em trabalho de volume fixo e com produtividade individual constante, oito pessoas que conc
- Se os valores positivos P e Q satisfazem P/4=Q/6 e P+Q=100, então P=50 e Q=50, porque as r
- Um total de 250 unidades repartido inversamente aos pesos 4 e 6 produz as parcelas 150 e 1
- Entre os registros de 100 km em 2 horas e 160 km em 3 horas, a taxa média de variação da d
```

## Pedido 3: Planejamento e ciclo orçamentário (`afo.ppa-ldo-loa-ciclo`, 1519 tópicos de edital)

```
Matéria: afo.ppa-ldo-loa-ciclo (Planejamento e ciclo orçamentário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Arquitetura constitucional do planejamento: PPA, LDO e LOA": 16
- "2. PPA e sistema federal de planejamento e orçamento": 18
- "3. LDO, LOA e classificações da programação": 28
- "4. Ciclo orçamentário, tramitação e emendas parlamentares": 25
- "5. Alterações orçamentárias e créditos adicionais": 13

Já existem (não repita):
- Um novo programa deverá estabelecer objetivos e metas regionalizados para investimentos pú
- Ao preparar o orçamento do próximo exercício, a equipe precisa consultar o instrumento que
- Ao estudar o conteúdo da LOA federal, uma pessoa identifica os orçamentos fiscal e da segu
- Uma dotação já existe na lei orçamentária, mas o valor aprovado ficou insuficiente para a 
- Uma equipe precisa identificar os passivos contingentes e as medidas previstas se certos e
- O planejamento deseja mostrar a área de ação governamental em que determinada despesa será
- Uma proposta orçamentária federal deve ser iniciada formalmente segundo a repartição const
- Uma nova ação orçamentária, sem dotação específica na LOA, precisa ser incluída após aprov
- Uma análise da vigência do PPA federal 2024–2027 sustenta que sua duração deve terminar ne
- Uma equipe apresenta o projeto da LDO federal e precisa verificar o prazo de envio constit
- Um gestor cita o art. 4º da Lei nº 10.180/2001 e apresenta somente unidades 'centrais, reg
- Uma diretriz de política pública fixa como objetivo ampliar a cobertura de determinado ser
- Ao preparar a LDO, é necessário elaborar um anexo de metas anuais de receitas, despesas, r
- A gestão orçamentária precisa estabelecer regras para limitar empenhos quando situações fi
- O texto de uma LOA inclui autorização para abertura de créditos suplementares dentro de li
- Uma proposta de emenda ao projeto de LOA, sem tratar de erro ou dispositivo textual, prete
- Uma equipe pretende incluir no orçamento de investimento estatal todas as despesas operaci
- Uma emenda individual impositiva destina recursos a um Município por transferência especia
- Para viabilizar projeto restrito às atividades de ciência, tecnologia e inovação, pretende
- Uma unidade quer abrir crédito especial financiado por fonte prevista na Lei nº 4.320/1964
- Um crédito especial autorizado por lei em novembro não foi inteiramente utilizado ao encer
- Durante análise do PLOA, determinada emenda pretende cancelar dotações do serviço da dívid
- Uma ação de manutenção recorrente de serviços de atendimento público é classificada na est
- Um analista sustenta que a fase de avaliação do orçamento pertence integralmente ao mesmo 
- Após calamidade pública imprevisível, a União precisa abrir crédito extraordinário para de
- Ao propor um investimento de três anos, o planejamento considera necessária sua inclusão n
- A equipe que prepara a LOA federal informa que ela conterá somente dois orçamentos: fiscal
- Uma equipe identifica no projeto da LDO o Anexo de Riscos Fiscais para examinar passivos c
- Um crédito adicional destinado a reforçar dotação já prevista na LOA deve ser classificado
- Uma proposta de orçamento é elaborada e enviada antes do ano a que se refere, sendo poster
- Uma unidade de orçamento pode ignorar as normas técnicas do órgão central do Sistema de Pl
- Em programação orçamentária, a classificação institucional identifica quem responde pela d
- Em projeto de LDO da União, o Anexo de Metas Fiscais precisa observar somente a regra gera
- Ao examinar emenda apresentada ao projeto de lei de diretrizes orçamentárias, a comissão v
- Uma programação de construção limitada a dois anos pode ser classificada como atividade ap
- Uma proposta de crédito especial, previamente autorizada por lei, pode ter abertura por de
- Para a abertura de crédito especial, basta a existência de uma necessidade administrativa;
- Os programas finalísticos e os programas de gestão constantes do PPA 2024–2027 possuem pap
- Em exame legislativo do PLOA, uma emenda que prevê anulação de dotações do serviço da dívi
- O crédito extraordinário, quando aberto na esfera federal por situação imprevisível e urge
- Um crédito suplementar aberto em outubro poderá ser reaberto no exercício seguinte nos lim
- A legislação pode admitir transposição de recursos entre categorias de programação em proj
- Uma emenda de comissão que identifique projeto de interesse nacional passa a ter execução 
- O art. 166, § 5º, permite propor modificação de parte do projeto de lei orçamentária por m
- Ao criar programação de investimento no PPA, uma equipe afirma que a previsão plurianual g
```

## Pedido 4: Fundamentos do direito ambiental: competências, Política Nacional e SNUC (`direito-ambiental.fundamentos-snuc`, 1513 tópicos de edital)

```
Matéria: direito-ambiental.fundamentos-snuc (Fundamentos do direito ambiental: competências, Política Nacional e SNUC)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria geral, princípios e dimensão constitucional do meio ambiente": 28
- "2. Bens ambientais e repartição de competências federativas": 15
- "3. Política Nacional do Meio Ambiente, SISNAMA, fundo e responsabilidade": 25
- "4. SNUC: categorias, criação, domínio e gestão das unidades": 19
- "5. APP, outros espaços protegidos e comparação com unidades de conservação": 13

Já existem (não repita):
- Uma nascente situada em terreno particular abastece diversas famílias. Segundo o art. 225 
- Uma indústria conhece os efeitos tóxicos de certo lançamento e planeja instalar barreiras 
- Uma empresa argumenta que suas operações lucrativas estão fora de exigências ecológicas. Q
- Para criar regras gerais sobre conservação da fauna, qual competência legislativa a Consti
- Uma equipe reúne dados de uma área para ordenar usos urbanos e rurais conforme fragilidade
- Um agente identifica a política que organiza os instrumentos nacionais de proteção e o sis
- Uma equipe precisa identificar uma categoria de unidade de conservação pertencente ao grup
- Uma propriedade contém faixa marginal de curso d'água natural e também uma unidade de cons
- Uma faixa florestal degradada, situada na margem de rio natural, está desprovida de árvore
- Uma lei estadual foi editada em matéria ambiental quando ainda não havia normas gerais fed
- Um Município enfrenta poluição localizada e pretende aprovar norma suplementar compatível 
- Um empreendimento precisa de licença ambiental e os demais entes federativos desejam manif
- Após atividade poluidora comprovadamente causadora de dano ambiental, o responsável alega 
- Uma unidade de conservação protege área privada de grande valor ecológico, gravada com per
- Uma área especialmente relevante será instituída como Reserva Biológica. Que requisito pro
- Uma nascente perene é identificada em imóvel rural fora de unidade de conservação. Qual re
- Um projeto prevê supressão de vegetação nativa em APP para atividade declarada de interess
- Uma fazenda de alta produtividade degrada as águas e o solo, alegando que o aproveitamento
- Uma empresa afirma que a Mata Atlântica e o Pantanal Mato-Grossense, por serem patrimônio 
- Um Estado pretende reduzir limites de Parque Estadual por ato do Executivo, invocando que 
- Um Refúgio de Vida Silvestre engloba glebas privadas cujos proprietários aceitam condições
- Um projeto pretende criar um fundo para conservação financiado também por doações e rendim
- Uma propriedade tem curso d'água natural perene de 8 metros de largura. Na aplicação da re
- Uma pessoa suprime ilegalmente vegetação em APP e, depois, vende o imóvel. Qual afirmação 
- Uma proposta identifica a categoria de UC cujo uso comunitário tradicional se baseia no ex
- Um terreno privado com remanescente de floresta possui relevância para a coletividade. A t
- Uma pessoa obtém lucro com exploração de recurso ambiental e afirma que o princípio usuári
- A Constituição atribui competência administrativa comum à União, aos Estados, ao Distrito 
- O Município figura expressamente, junto à União e aos Estados, no caput do art. 24 da Cons
- A Lei nº 6.938/1981 inclui zoneamento ambiental, avaliação de impactos, licenciamento e cr
- A Reserva Particular do Patrimônio Natural integra o grupo de Proteção Integral do SNUC po
- O art. 186, II, da Constituição associa a função social da propriedade rural à utilização 
- O princípio da precaução exige prova científica conclusiva de dano consumado antes de just
- A LC nº 140/2011 prevê licenciamento ambiental por um único ente federativo, admitindo a m
- A responsabilidade civil de reparar o dano ambiental exige a prévia condenação penal do po
- O Fundo Nacional do Meio Ambiente, criado pela Lei nº 7.797/1989, pode receber dotações fe
- A Lei nº 9.985/2000 exige consulta pública prévia à instituição de toda unidade de conserv
- A vegetação nativa existente em APP deve ser mantida, e a obrigação de recomposição por su
- Por integrar o patrimônio nacional previsto no art. 225, § 4º, qualquer propriedade privad
- A superveniência de lei federal de normas gerais ambientais suspende a eficácia da legisla
- A competência administrativa comum prevista no art. 23 da Constituição exige que todos os 
- O Monumento Natural e o Refúgio de Vida Silvestre integram a Proteção Integral e podem abr
- Uma unidade de conservação pode ser criada por ato do Poder Público e, pelo mesmo fundamen
- A faixa legal de APP ao longo de curso d'água natural com largura inferior a dez metros é,
- O art. 8º da Lei nº 12.651/2012 permite suprimir vegetação em APP pelo simples interesse e
```

## Pedido 5: Correio eletrônico (Outlook 2016) e navegação (Google Chrome) (`informatica.correio-eletronico-outlook-2016-navegacao`, 1454 tópicos de edital)

```
Matéria: informatica.correio-eletronico-outlook-2016-navegacao (Correio eletrônico (Outlook 2016) e navegação (Google Chrome))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Correio eletrônico: endereços, mensagens, anexos e protocolos": 18
- "2. Outlook 2016 clássico: pastas, pesquisa, regras e operações": 26
- "3. Webmail, Gmail, Thunderbird e Outlook Express": 23
- "4. Navegadores: Chrome, Edge, Firefox, busca e intranet": 18
- "5. Privacidade, segurança e solução de casos práticos": 15

Já existem (não repita):
- Ao enviar um comunicado, Laura colocou Bruno em Para, Camila em Cc e Denise em Cco. Bruno 
- Paulo recebe mensagem de Renata, com Sofia em Cc. Ele quer enviar sua manifestação para am
- Marina recebeu um documento por e-mail e precisa repassá-lo a um colaborador que não parti
- Uma pessoa abre sua caixa de mensagens do Gmail dentro do Chrome por endereço iniciado em 
- Um usuário configura um cliente de correio e precisa identificar o protocolo típico de env
- Joana usa computador e celular e deseja acompanhar as mesmas pastas de correio armazenadas
- Um assistente de configuração oferece POP3 e IMAP para a mesma conta. O usuário escolhe PO
- Uma mensagem afirma que a senha será cancelada imediatamente e contém link que imita a pág
- Durante falha de conexão, um usuário envia mensagem no Outlook 2016 clássico. O item perma
- No Outlook 2016, Patrícia quer visualizar mensagens não lidas guardadas em várias pastas s
- No Outlook 2016 clássico, um usuário quer mover automaticamente novas mensagens cujo assun
- Uma pessoa quer localizar mensagens com arquivos anexados no Outlook 2016, utilizando a si
- No Gmail, Ricardo precisa retirar uma conversa da Caixa de Entrada, mas deseja poder local
- No Gmail, Aline marcou uma única mensagem com os rótulos 'Projeto' e 'Urgente'. Qual inter
- Uma pessoa com Gmail pessoal precisa enviar um arquivo de 40 MB a colega. Qual procediment
- Felipe instala Mozilla Thunderbird e quer que mensagens de determinado remetente sejam mov
- Um enunciado compara Outlook Express com Microsoft Outlook 2016 clássico. Qual afirmação r
- No Google Chrome instalado em Windows, uma pessoa fechou uma guia por engano e deseja reab
- No Chrome, Beatriz baixa arquivo durante navegação anônima e salva favorito. Ela fecha tod
- Uma pessoa deseja abrir uma janela de navegação InPrivate no Microsoft Edge em um computad
- Uma usuária do Firefox em Windows quer abrir janela de navegação privativa por atalho, sem
- Uma pessoa precisa pesquisar páginas indexadas de um domínio específico, sem pretender ent
- Um portal de procedimentos internos só é acessível pela rede institucional ou por acesso r
- Helena envia comunicado para destinatários externos usando Cco. Um dos destinatários ocult
- Um e-mail direciona o destinatário a site falso com certificado HTTPS válido. O navegador 
- Em uma comunicação com Para, Cc e Cco, o remetente pode examinar no registro da mensagem e
- Quando uma mensagem foi enviada com cópia oculta a uma pessoa, a opção Responder a todos d
- Ao configurar cliente instalado, uma pessoa pode usar SMTP para submissão de mensagens e I
- Como POP3 e IMAP são protocolos de recebimento, os dois oferecem necessariamente sincroniz
- No Outlook 2016 clássico, uma mensagem ainda pendente de envio por falta de conectividade 
- No Outlook 2016, a simples presença da mensagem em Itens Enviados constitui comprovação su
- Uma Pasta de Pesquisa do Outlook 2016 pode apresentar mensagens que satisfazem critérios e
- Excluir o objeto Pasta de Pesquisa no Outlook 2016 determina necessariamente a eliminação 
- No Gmail, uma conversa pode possuir dois marcadores e, ao ser arquivada, deixar a Caixa de
- Quando se cria no Gmail um filtro que encaminha mensagens de determinado remetente, todas 
- No Thunderbird, quando a identificação automática das configurações do provedor não funcio
- Outlook Express é o nome de um recurso nativo de navegação privativa do Microsoft Edge e p
- Para uma conta pessoal de Gmail, o limite de anexos comuns é de 25 MB no total; um arquivo
- No Google Chrome, abrir janela anônima impede que sites acessados e administradores da red
- Uma pessoa salva um favorito e baixa um PDF em janela anônima do Chrome; ao encerrar todas
- No Firefox para Windows, Ctrl+Shift+N é o atalho característico de abrir janela privativa,
- Em um mecanismo de busca compatível, a consulta site:exemplo.gov.br restringe resultados i
- Uma página de phishing com domínio semelhante ao verdadeiro deixa de representar risco qua
- Uma tentativa direcionada de obter senha por e-mail pode caracterizar phishing mesmo que a
- Ao adicionar uma página aos favoritos do Chrome, o navegador cria necessariamente uma cópi
```

## Pedido 6: Direitos reais (`direito-civil.direitos-reais`, 1452 tópicos de edital)

```
Matéria: direito-civil.direitos-reais (Direitos reais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Posse, detenção e tutela possessória": 20
- "2. Propriedade, função social, aquisição e usucapião": 20
- "3. Direitos de vizinhança, condomínio e propriedade resolúvel": 13
- "4. Direitos reais de gozo, laje e aquisição do promitente comprador": 30
- "5. Garantias reais, propriedade fiduciária e eficácia perante terceiros": 17

Já existem (não repita):
- Lúcia, sem ser proprietária, ocupa regularmente uma casa e exerce os poderes de uso sobre 
- Marcos alugou seu apartamento para Ana, que passou a habitá-lo. Quanto à classificação da 
- Um caseiro permanece em chácara, executando ordens do proprietário, sem agir em nome própr
- Após invasão, João perdeu a posse de um galpão e pretende recuperá-la judicialmente. A tut
- Uma possuidora de boa-fé realizou reparo estrutural indispensável e instalou equipamento q
- Uma proprietária pretende construir empreendimento, mas é alertada sobre danos ambientais 
- Paulo comprou imóvel por escritura pública, mas ainda não levou o título ao registro. Em r
- Tereza possui imóvel privado como seu por 16 anos, contínua e pacificamente, mas não apres
- Rafaela possui como dona, por cinco anos sem oposição, lote urbano privado de 180 m² onde 
- Uma colecionadora possui como sua uma peça móvel há quatro anos, de forma contínua e sem c
- Um terreno privado ficou sem acesso a via pública após divisão das áreas vizinhas. O propr
- Uma indústria licenciada emite vibrações acima da tolerância local e prejudica o sono dos 
- Três irmãos possuem, em frações ideais, sítio indiviso. Um deles deseja encerrar o condomí
- Uma pessoa recebeu domínio sujeito a condição resolutiva expressa e, antes de ocorrer o ev
- O dono de terreno deseja constituir, por negócio jurídico e registro, direito para terceir
- O usufrutuário de imóvel pretende transferir a terceiro, por remuneração, apenas o exercíc
- César é titular de direito real de habitação sobre casa alheia e pretende alugá-la para re
- Dois terrenos pertencem a donos diversos. O titular de um pretende beneficiar seu imóvel m
- Uma construção-base recebeu unidade superior independente, regularmente instituída como di
- Família ocupa, para moradia, imóvel público urbano de 200 m² desde 2009, com posse ininter
- Bianca quitou promessa irretratável de compra e venda, mas o instrumento não foi registrad
- Em procedimento extrajudicial de adjudicação compulsória, notificado o promitente vendedor
- Um devedor oferece imóvel em hipoteca e entrega um relógio ao credor a título de penhor co
- Após inadimplência em financiamento de veículo com alienação fiduciária, a liminar de busc
- Em alienação fiduciária de imóvel, houve mora formalizada e o devedor não pagou no prazo. 
- Em contrato de locação, a entrega do apartamento ao inquilino permite a coexistência da po
- O empregado que guarda veículo da empresa por ordens do empregador torna-se possuidor autô
- O morador que continua ocupando a casa, apesar de bloqueios reiterados ao acesso causados 
- Ao restituir imóvel, o possuidor de má-fé tem direito à retenção por benfeitorias necessár
- Em compra e venda entre vivos de imóvel particular, a escritura assinada sem registro não 
- Uma pessoa pode adquirir por usucapião especial urbana imóvel público onde vive há dez ano
- Na usucapião ordinária reduzida do art. 1.242, parágrafo único, a posse por cinco anos pre
- Na hipótese de posse-trabalho do art. 1.228, § 4º, a ocupação coletiva qualificada transfe
- O dono de lote particular pode cortar, até o plano vertical divisório, ramos e raízes de á
- Condôminos podem pactuar, sem limitação temporal, a indivisão voluntária de um bem e afast
- A resolução de propriedade por condição prevista desde sua constituição e a resolução por 
- No condomínio edilício, a existência de apartamentos como unidades autônomas convive com a
- Pelo art. 1.369 do Código Civil, o direito de superfície pode ser constituído verbalmente 
- Constituído usufruto em favor de pessoa jurídica que continua existindo por décadas, o dir
- Uma servidão predial não utilizada por cinco anos extingue-se necessariamente por esse úni
- O comprador que quitou promessa irretratável não registrada pode pleitear adjudicação comp
- A concessão de uso especial para fins de moradia da MP 2.220/2001 pode ser adquirida por q
- É nula, como regra do art. 1.428 do Código Civil, a cláusula que permite ao credor pignora
- Na busca e apreensão de bem móvel alienado fiduciariamente, o prazo de quinze dias para ap
- Na alienação fiduciária imobiliária, a Lei 9.514/1997 prevê intimação para pagamento da mo
```

## Pedido 7: Lei de Acesso à Informação (`administracao-publica.lei-acesso-informacao`, 1440 tópicos de edital)

```
Matéria: administracao-publica.lei-acesso-informacao (Lei de Acesso à Informação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e alcance da LAI": 19
- "2. Publicidade ativa, dados abertos e governo aberto": 20
- "3. Pedidos, prazos, recursos e omissões": 20
- "4. Sigilos, informação pessoal e diálogo com a LGPD": 22
- "5. Fiscalização, responsabilidades e regulamentação federal": 19

Já existem (não repita):
- Uma pessoa solicita a um órgão federal cópia de contratos administrativos sem explicar par
- Uma autarquia mantém portal de dados sobre despesas, enquanto uma cidadã solicita document
- Um órgão não consegue fornecer imediatamente informação existente e precisa pesquisar seus
- Um município com 9 mil habitantes afirma estar totalmente fora do regime da LAI porque não
- Em relatório público de inspeção, uma tabela contém dados de execução contratual e um anex
- Após decisão fundamentada, uma informação é classificada como reservada por risco específi
- Uma organização privada sem fins lucrativos recebe verba pública para executar ação social
- Uma pessoa encontra negativa formal de acesso e é cientificada da decisão. Na primeira imp
- Uma secretaria federal publica contratos, editais, estrutura e perguntas frequentes sem es
- Uma equipe recebe base pública de dados em planilha estruturada e com licença de reuso. Qu
- Uma pessoa pretende consultar informação produzida por prestador privado durante contrato 
- Uma pessoa pede arquivo digital público cujo conteúdo já está disponível em endereço ofici
- A responsável por uma base pública federal afirma que o consentimento de cada cidadão é se
- Após receber documento classificado, uma equipe federal discute a função do Núcleo de Segu
- Uma solicitação contém informações de saúde e detalhes íntimos de pessoa identificada. Qua
- Uma comissão investiga documentos sobre violações de direitos humanos atribuídas a agentes
- Um conselho de fiscalização profissional pretende cumprir a regra nova de publicidade remu
- Serviço social autônomo beneficiário de contribuições federais decide publicar seu quadro 
- No Executivo federal, uma pessoa não recebeu qualquer decisão sobre pedido apresentado há 
- Uma unidade pública identifica custo para fotocopiar volumoso processo solicitado por pess
- Em unidade que trata informações classificadas, alguém confunde o documento de classificaç
- Uma servidora aplica o rito recursal do Decreto nº 7.724/2012 a pedido dirigido a órgão es
- Um serviço social autônomo financiado nos termos do art. 8º-A e um conselho de fiscalizaçã
- Um relatório preparatório foi usado como fundamento de um ato decisório recém-editado. Qua
- Documento classificado no grau secreto completa seu prazo máximo sem prorrogação ou outro 
- Uma pessoa consulta um portal público que disponibiliza espontaneamente registros de licit
- Uma secretaria decide exigir que qualquer pessoa explique seu objetivo profissional antes 
- Um relatório está pronto para consulta, mas seu anexo inclui o endereço residencial de pes
- Uma autarquia informa que documentos classificados no grau secreto podem permanecer sigilo
- Uma pessoa solicita informação pública cuja reprodução requer custo material. A busca e o 
- O portal de uma entidade informa que a Lei de Acesso à Informação incide apenas sobre o Po
- Para receber informação sobre despesa pública, uma cidadã utiliza o serviço de informações
- Uma unidade do Executivo federal silencia por 30 dias sobre pedido de informação. O requer
- Uma informação é classificada com base em perigo real à segurança da sociedade, mas o fund
- Uma base federal publicada como arquivo digital escaneado, sem registros estruturados e se
- Ao tratar dados pessoais para cumprir atribuição pública prevista em lei, uma entidade dev
- No Executivo federal, depois de uma negativa inicial de acesso, o recurso à Controladoria-
- Uma informação referente à intimidade de pessoa identificada pode ter acesso restrito com 
- Uma autoridade federal conclui que a publicação de informações sobre violações de direitos
- Um gestor precisa autorizar o tratamento de informação classificada por pessoal habilitado
- Um município deve usar obrigatoriamente todas as instâncias recursais administrativas do D
- Um conselho profissional pretende publicar valores efetivamente relacionados a remuneração
- Um serviço social autônomo abrangido pelo art. 8º-A da LAI cumpre integralmente a alteraçã
- Uma pessoa física vinculada ao poder público descumpre obrigações da LAI. O art. 33 admite
- O Decreto nº 8.777/2016, ao tratar de dados abertos, autoriza divulgar informações pessoai
```

## Pedido 8: Operações com conjuntos (`raciocinio-logico.conjuntos`, 1435 tópicos de edital)

```
Matéria: raciocinio-logico.conjuntos (Operações com conjuntos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Linguagem, representação, pertinência e inclusão": 12
- "2. União, interseção, diferença e leis de De Morgan": 44
- "3. Cardinalidade, conjunto das partes e diagramas de dois conjuntos": 18
- "4. Três conjuntos, inclusão-exclusão e pesquisas": 15
- "5. Conjuntos numéricos, intervalos reais e problemas integrados": 11

Já existem (não repita):
- No universo dos inteiros de 1 a 6, A é o conjunto dos números pares. Qual afirmação descre
- Considere A formado pelos números 1 e 3 e B formado pelos números 1, 2, 3 e 4. Qual relaçã
- Uma relação de códigos apresenta a coleção a, a, b, c, c. Se esses registros forem interpr
- A contém 1, 2 e 4; B contém 2, 3 e 5. Quais elementos pertencem à união dos dois conjuntos
- Considere A com os números 2, 4, 6 e 8 e B com os números 1, 2, 3 e 4. Qual é a interseção
- Considere o universo formado pelas letras a, b, c, d e e, e o conjunto A composto apenas p
- Uma pesquisa define A pelos códigos p, q e r e B pelos códigos q e s. Qual é o resultado d
- Dois grupos sem participantes comuns têm, respectivamente, 12 e 9 integrantes. Quantas pes
- Uma equipe possui exatamente três membros distintos, identificados por x, y e z. Quantos s
- De 45 pessoas entrevistadas, 27 escolheram A, 22 escolheram B e 10 escolheram as duas opçõ
- Em um levantamento, considera-se o complemento do grupo de pessoas que fazem A ou B. Qual 
- A é o conjunto dos números 1, 2, 3 e 4, e B contém 3, 4, 5 e 6. Quantos elementos pertence
- No universo dos números inteiros de 1 a 8, A contém 2, 3, 5 e 7; B contém 1, 3 e 5; C cont
- Considere os intervalos reais A fechado de menos 2 a 4 e B aberto de 1 a 7. Qual intervalo
- Uma equipe contém cinco integrantes distintos. Quantos de seus subconjuntos são simultanea
- Em pesquisa com 72 pessoas, 40 participam de A, 35 de B e 18 participam dos dois. Quantas 
- A reúne inteiros desde menos 2 inclusive até 4 exclusive, e B é o conjunto de todos os int
- Em levantamento com três competências, 28 pessoas têm A, 26 têm B, 22 têm C; as interseçõe
- Uma pesquisa com 90 pessoas registra 45 em A, 38 em B e 29 em C, com 17 em A/B, 12 em A/C,
- Em três conjuntos finitos, os totais individuais são 32, 30 e 25, as interseções de pares 
- Um conjunto possui cinco elementos distintos, entre eles a e b. Quantos subconjuntos inclu
- A reúne todos os reais menores ou iguais a 2; B reúne os reais maiores que zero e menores 
- De 100 participantes, 48 fazem A, 44 fazem B e 35 fazem C. As interseções A/B, A/C e B/C t
- No universo dos inteiros de 1 a 10, A contém 2, 4, 6 e 8 e B contém 4, 5, 6 e 7. Se forem 
- Em pesquisa, 50 fazem A, 45 fazem B e 40 fazem C. As interseções de pares A/B, A/C e B/C s
- O conjunto vazio está contido em todo conjunto, inclusive nele mesmo, embora nem sempre ap
- Um conjunto contendo apenas o número zero é igual ao conjunto vazio, pois o zero não repre
- Se uma equipe é representada por A, reunir A consigo mesmo pela operação de união não acre
- O complemento de A tem sempre os mesmos elementos, independentemente de qual universo seja
- Se duas equipes não possuem integrante em comum, a interseção dos conjuntos que as represe
- Em pesquisa sobre atividades A e B, todo membro da união deve participar simultaneamente d
- Em uma pesquisa, 28 participantes fazem A, 21 fazem B e 10 fazem ambos. O número dos que r
- Para A formado por 1 e 2 e B formado por 2 e 3, a diferença A menos B coincide com B menos
- Um conjunto finito com quatro elementos possui 16 subconjuntos distintos quando se contam 
- A lei de De Morgan afirma que negar a interseção de A e B produz a interseção dos compleme
- Em um universo de 50 pessoas, se a união das atividades A e B contém 34 indivíduos, então 
- A interseção dos intervalos reais [1, 5] e (3, 7) é [3, 5], pois o número 3 pertence ao pr
- Os números inteiros pertencentes ao intervalo real [2, 7) são exatamente 2, 3, 4, 5 e 6; o
- Três grupos possuem 35, 30 e 25 participantes; suas interseções de pares têm 12, 10 e 8 pe
- Em um diagrama de três conjuntos, as interseções totais de pares têm 11, 10 e 9 pessoas e 
- O conjunto das partes do conjunto vazio é vazio, pois não existe elemento disponível para 
- Se todos os elementos de A pertencem a B, a interseção entre A e B é exatamente A, mesmo q
- Se a interseção simultânea de A, B e C é vazia, então as três interseções de pares também 
- No universo dos números reais, nenhum número pertence simultaneamente ao conjunto dos raci
- Ao subtrair o intervalo real (−1, 4] do intervalo [−2, 3), obtém-se o intervalo [−2, −1), 
```

## Pedido 9: Figuras de linguagem (`portugues.figuras-de-linguagem`, 1419 tópicos de edital)

```
Matéria: portugues.figuras-de-linguagem (Figuras de linguagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Denotação, conotação e figuras de palavra": 24
- "2. Figuras de pensamento e construção do efeito expressivo": 19
- "3. Figuras de construção, repetição e organização sintática": 19
- "4. Figuras de som, rima, métrica e espacialidade no poema": 19
- "5. Interpretação integrada, literatura, publicidade e ambiguidades": 19

Já existem (não repita):
- Em um aviso, lê-se: “O atraso construiu um muro entre os dois grupos”. No contexto, ningué
- O texto de apresentação de uma exposição afirma: “Passei o domingo lendo uma autora contem
- Uma carta descreve uma espera curta como interminável: “Fiquei mil anos diante daquela por
- Num relato, afirma-se “A empresa decidiu reduzir a equipe” e, depois, “algumas pessoas for
- Um poema diz: “Na janela, o vento cantava baixinho”. A ação de cantar, atribuída ao vento,
- Em uma crônica, lê-se: “Ela preferiu a trilha estreita; ele, a estrada principal”. Qual re
- No slogan “Mais cuidado. Mais tempo. Mais vida.”, a palavra “Mais” reaparece no início de 
- Uma composição traz o verso “Sopram suaves sons sobre a serra”. A repetição perceptível de
- Em uma publicação publicitária, lê-se “Sua rotina ganha asas”. O serviço anunciado não env
- Na frase “Ao chegar, ouvi uma voz aveludada no salão”, o adjetivo aplica à audição uma qua
- Em uma narrativa, o contraste é apresentado assim: “Durante o dia, a praça era movimentada
- Um poema descreve “um silêncio ensurdecedor”. A aproximação do silêncio com uma intensidad
- Num texto em prosa, encontra-se “E discutiram, e decidiram, e voltaram, e recomeçaram”. O 
- Um poema possui quatro versos cujas palavras finais são, nessa ordem: “mar”, “caminho”, “o
- Um anúncio de bebida diz “Uma manhã vibrante começa aqui”. A escolha de “vibrante”, no con
- Em “O braço da poltrona foi substituído”, a palavra “braço” nomeia convencionalmente uma p
- Após duas horas de espera, uma pessoa comenta com expressão de desagrado: “Que atendimento
- Duas frases aparecem lado a lado: I — “Ela atuou como responsável durante o evento”; II — 
- Leia o trecho autoral: “A cidade abriu os olhos e viu os ônibus desaparecerem”. Consideran
- Um poema dispõe as palavras da expressão “a notícia caiu” em linhas progressivamente mais 
- Uma campanha ambiental apresenta a frase “A floresta está pedindo socorro”. Em vez de medi
- Nos versos “Na mesa deixei / uma carta sem resposta”, o grupo verbal iniciado no primeiro 
- Em “Aqueles documentos, ninguém sabe qual foi o destino deles”, o grupo nominal inicial fi
- Um texto diz “Não direi nada das quatro promessas abandonadas; não quero tocar nesse assun
- Uma versão de anúncio diz “Esperamos, esperamos, esperamos por você”; outra diz “A espera 
- Na frase “Seus argumentos eram pontes entre as ideias”, o termo “pontes” tem valor metafór
- A expressão “chorou rios de lágrimas” constitui eufemismo, porque reduz a intensidade emoc
- Em “O relógio cochichava no quarto vazio”, a atribuição de uma ação humana ao objeto pode 
- Em “Eu pedi água; minha irmã, chá”, a omissão do verbo no segundo membro é chamada de hipé
- A sequência “Bate, brinca, busca, brilha” pode apresentar aliteração pela repetição expres
- Todo adjetivo usado num anúncio constitui metáfora, ainda que apenas forneça uma informaçã
- Em “Precisamos de mais braços para concluir a mudança”, “braços” é usado para designar pes
- Na expressão “A casa clara cala”, a recorrência de vogais /a/ demonstra, por si, aliteraçã
- Na sequência “Seguimos por justiça, lutamos por justiça, insistimos por justiça”, a expres
- A expressão cotidiana “pé da mesa” deve ser classificada como uma metáfora literária inédi
- A frase “Que pontualidade exemplar!”, proferida com reprovação após uma chegada muito atra
- Um poema em versos livres não pode conter rima eventual, repetição de consoantes ou ritmo 
- Ao descrever uma bebida como “um começo leve e luminoso para o dia”, uma campanha pode usa
- No fragmento “De manhã havia esperança; à noite, desencanto”, as ideias opostas são necess
- Na frase “Ouvi com estes meus próprios ouvidos”, a redundância pode constituir pleonasmo e
- A sequência “Quem conta um conto aumenta um ponto” exemplifica onomatopeia, pois as palavr
- Na versificação tradicional em português, a contagem de sílabas poéticas costuma encerrar-
- Na frase “A praça adormeceu sob o casaco da neblina”, a presença de personificação exclui 
- Em uma composição que escreve a palavra “descer” sucessivamente em posições mais baixas na
- A reescrita de “O relógio nos persegue” como “O tempo disponível é limitado” preserva nece
```

## Pedido 10: Frase, oração e período (`portugues.frase-oracao-periodo`, 1411 tópicos de edital)

```
Matéria: portugues.frase-oracao-periodo (Frase, oração e período)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Frase, enunciado e modalidades comunicativas": 34
- "2. Oração, verbo, locução verbal e classes de palavras": 17
- "3. Período simples, composto e articulação de orações": 22
- "4. Deslocamento, substituição e pontuação na estrutura frasal": 14
- "5. Construções problemáticas, revisão e aplicações integradas": 13

Já existem (não repita):
- Num cartaz de entrada, lê-se 'Silêncio, por favor!'. Segundo a distinção entre frase e ora
- Durante uma reunião, alguém pergunta 'Você poderia fechar a janela?'. Qual descrição disti
- Em mensagem de rotina, aparece 'Os equipamentos foram revisados ontem.'. Qual é a classifi
- O aviso registra 'A oficina terminou cedo.'. Para a contagem de orações, qual alternativa 
- Considere o registro 'Lara e Rui chegaram cedo.'. O emprego de 'e' nessa frase produz qual
- Em 'Os visitantes tinham recebido os crachás', quantas orações há, considerando o valor de
- Numa ocorrência, escreve-se 'O portão abriu e os visitantes entraram.'. Quantos núcleos or
- Em 'Ao ouvir o sinal, Paula interrompeu a tarefa', como se analisam as duas estruturas ver
- O relatório registra 'A equipe respondeu rapidamente'. A palavra 'rapidamente' desempenha 
- Em 'Chegaram os resultados da pesquisa', o termo 'os resultados da pesquisa' deve ser iden
- Um aviso registra 'Houve três alterações no cronograma'. Ao revisar a construção segundo a
- Um texto informa 'Faz cinco anos que o centro funciona'. Qual correção deve ser preservada
- Em 'Paula e Luís organizaram a sala', um estudante afirmou que o período era composto devi
- Em 'Ao concluir a leitura, os estudantes saíram', um estudante propôs desenvolver a primei
- Num relatório lê-se 'O aviso chegou, a equipe iniciou o trabalho'. A relação sintática ent
- Uma comunicação apresenta 'Os arquivos, chegaram hoje'. Qual correção de pontuação é mais 
- Considere 'Na sala, o monitor explicou a tarefa'. Qual alteração preserva os participantes
- Na frase 'O parecer detalhado esclareceu a dúvida', qual identificação das classes gramati
- Compare 'Precisa-se de profissionais experientes' e 'Selecionam-se profissionais experient
- Uma placa traz 'Vende-se equipamentos novos'. Qual reformulação respeita a norma-padrão e 
- Em um diálogo, alguém escreve 'Que excelente notícia!' e depois 'A notícia foi confirmada'
- Ao revisar 'Rita leu os documentos, identificou as falhas e solicitou correções', um leito
- Num comunicado, lê-se 'Joana, confira os anexos; os demais participantes aguardem a respos
- Um revisor compara 'A diretora encontrou a colega com o telescópio' e 'Com o telescópio, a
- Um redator escreve 'Ao perceber o erro, Ana parou e pediu ajuda'. Para reconhecer a estrut
- Em um aviso de emergência, o enunciado 'Socorro!' constitui frase significativa sem ser or
- No período 'A pesquisadora estava examinando as amostras', devem ser reconhecidas duas ora
- Na frase 'Fechem a porta.', a pontuação por ponto final não impede que o enunciado seja in
- A frase 'Cuidado com o degrau!' é necessariamente um período simples, pois todo enunciado 
- Em 'O estudante e a professora conversaram', a conjunção 'e' liga termos do sujeito e o pe
- No registro 'Chegou ontem os documentos', a posição posterior do sujeito plural dispensa a
- No enunciado 'Quando a reunião acabou, as pessoas saíram', existem duas orações ligadas po
- Em 'Os relatórios, chegaram cedo', a vírgula separa corretamente sujeito e predicado apena
- A expressão 'Concluída a análise, o documento foi arquivado' apresenta uma construção redu
- Em 'Os arquivos foram enviados pelo setor', 'enviados' funciona como núcleo de uma segunda
- Em 'A equipe enviou o relatório ao setor', 'o relatório' funciona como objeto direto e 'ao
- No período 'A professora chegou; os alunos saíram', o ponto e vírgula impede que se reconh
- Um revisor encontra 'Ao concluir a apresentação, Miguel respirou aliviado'. Nesse contexto
- Numa comunicação isolada, 'Embora os relatórios estejam prontos.' constitui período comple
- No trecho 'Ao perceber a falha, os técnicos pararam e comunicaram o problema', identificam
- No exemplo 'Ana e Bia chegaram quando a chuva cessou', reconhecem-se três orações: uma par
- Num grupo misto de participantes, as versões 'Os convidados que chegaram cedo entraram' e 
- Em 'A analista encontrou a pesquisadora com uma luneta', o sintagma 'com uma luneta' tem i
- Uma construção de objetivos como 'Pretendemos organizar a documentação e revisar os regist
- Na pergunta 'Você vai abrir a porta?', o ponto de interrogação prova que não pode haver va
```

## Pedido 11: Compreensão e análise lógica de situações (`raciocinio-logico.analise-situacoes`, 1395 tópicos de edital)

```
Matéria: raciocinio-logico.analise-situacoes (Compreensão e análise lógica de situações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sequências e regularidades": 24
- "2. Raciocínio verbal, conceitos e relações lógicas": 34
- "3. Orientação temporal e espacial": 24
- "4. Problemas cotidianos e estratégia de resolução": 18

Já existem (não repita):
- Uma equipe registra os tempos de chegada, em minutos, como 6, 11, 16 e 21. Mantido o padrã
- Considere a sequência de letras B, E, I, N, T. Se o alfabeto recomeça após Z, qual letra v
- Na analogia jardineiro está para tesoura de poda assim como enfermeiro está para:
- O primeiro dia de um mês de 30 dias caiu em uma segunda-feira. Em que dia da semana caiu o
- Um relógio de controle marca 7h35 no início de uma inspeção e 9h10 no fim. Quanto durou a 
- Uma pessoa começa voltada para o norte, gira à direita, depois à esquerda e, por fim, à di
- Considere capacete, luvas, botas, rádio e óculos de proteção. Qual item não pertence ao gr
- Dois avisos automáticos são emitidos, respectivamente, a cada 6 e a cada 8 minutos. Se toc
- Uma série descrita alterna triângulo e polígono: triângulo, quadrilátero, triângulo, pentá
- A regra do setor diz: se o alarme de incêndio toca, a equipe evacua o prédio. Hoje o alarm
- Considere p verdadeira e q falsa. Qual é o valor lógico de p e não q?
- Uma equipe parte do ponto de apoio, segue 3 quadras para leste, 2 para norte e 1 para oest
- Uma escala de limpeza repete um ciclo a cada 9 dias e outra a cada 12 dias. Se os ciclos c
- Em um ano não bissexto, 1º de março caiu numa quarta-feira. Que dia da semana será 1º de a
- Todo integrante da equipe de busca recebeu uma lanterna. Caio recebeu uma lanterna. Qual c
- Uma inspeção começou às 6h48 e durou 2 horas e 37 minutos. A que horas terminou?
- Uma planificação tem quatro faces laterais em faixa e duas faces quadradas para fechar as 
- Uma sequência numérica começa em 2 e soma, sucessivamente, 3, 5, 7 e 9. Qual é o sexto ter
- A regra estabelece: se uma van está abastecida, então pode iniciar o deslocamento. Sabe-se
- Três equipes, Alfa, Bravo e Charlie, ocupam posições 1ª, 2ª e 3ª. Alfa não pode ficar em 1
- Uma sequência tem termos 4, 7, 13, 22, 34. As diferenças aumentam de modo regular. Qual é 
- Em uma rota, a equipe segue 4 quadras ao norte, 3 a leste, 1 ao sul e 2 a oeste. Qual é se
- Considere a regra se p, então q. Em qual situação a regra é necessariamente falsa?
- Um mês tem 31 dias e começa numa sexta-feira. Em que dia da semana cairá o último dia dess
- Uma fila de quatro equipes satisfaz as regras: Vega está antes de Atlas; Atlas está antes 
- Uma sequência começa em 1 e alterna, em ordem, as operações somar 4 e multiplicar por 2. O
- Uma equipe tem exatamente um entre três integrantes — Lia, Nuno e Rui — responsável por um
- Considere p verdadeira e q falsa. Qual expressão tem valor verdadeiro?
- Uma equipe se desloca 5 quadras para oeste, gira 90 graus à direita, anda 2 quadras, gira 
- Todo veículo da frota que passou pela revisão tem o selo azul. Alguns veículos com selo az
- Se as diferenças sucessivas de uma sequência são 2, 4, 6 e 8, a próxima diferença, mantend
- Se a regra é se o alarme toca, a equipe evacua, então a ausência de alarme prova que a equ
- Se hoje é quinta-feira, daqui a 28 dias será novamente quinta-feira.
- Uma pessoa voltada para o sul, ao girar 90 graus à esquerda, passa a apontar para oeste.
- Dois ciclos que se repetem a cada 4 e 10 minutos, iniciados juntos, voltarão a coincidir p
- A relação entre capacete e proteção é igual à relação entre rádio e comunicação, pois ambo
- Após quatro giros consecutivos de 90 graus no mesmo sentido, uma equipe volta a apontar pa
- Se todo agente escalado para o plantão recebeu chave, então todo servidor que recebeu chav
- A sequência 18, 13, 8, 3, -2 segue uma razão constante igual a -5.
- Em um mês de 31 dias iniciado numa sexta-feira, o dia 31 também será sexta-feira.
- Se p implica q e p é verdadeira, então q é necessariamente verdadeira.
- A negação de se a equipe está pronta, então a van parte é se a equipe não está pronta, ent
- A negação de não p ou q é equivalente a p e não q.
- Se uma atividade começa às 23h50 e dura 35 minutos, termina às 0h25 do dia seguinte.
- Em uma planificação de cubo válida, duas faces que compartilham uma aresta no papel necess
```

## Pedido 12: Direito Administrativo: conceito, fontes e regime jurídico-administrativo (`direito-administrativo.regime-juridico-fontes`, 1365 tópicos de edital)

```
Matéria: direito-administrativo.regime-juridico-fontes (Direito Administrativo: conceito, fontes e regime jurídico-administrativo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estado, governo e Administração Pública": 21
- "2. Conceito, história e critérios de delimitação do Direito Administrativo": 20
- "3. Fontes normativas, interpretação e força vinculante": 18
- "4. Sistemas de jurisdição e regime jurídico-administrativo": 28
- "5. Silêncio administrativo, LINDB e segurança jurídica aplicada": 13

Já existem (não repita):
- Um estudo sobre a formação de um Estado distingue os nacionais, os residentes e a base esp
- Um estrangeiro reside legalmente no Brasil há anos, sem adquirir nacionalidade brasileira.
- Um órgão judicial contrata limpeza e administra os próprios edifícios. Considerada a natur
- Em uma descrição institucional, o texto identifica órgãos, entidades e agentes que exercem
- No estudo histórico do Direito Administrativo, o caso Blanco de 1873 é usado como referênc
- Uma definição de Direito Administrativo considera apenas as atividades exercidas pelo Pode
- A chefia do Executivo expede decreto para detalhar a aplicação de lei federal, sem tratar 
- Um regulamento interno contraria diretamente regra da lei que deveria concretizar. Na comp
- Uma decisão administrativa definitiva é considerada imune a qualquer apreciação judicial s
- Um requerimento administrativo comum permanece sem resposta depois de concluída sua instru
- Ao explicar o art. 18 da Constituição, uma exposição afirma que Estados e Municípios são s
- Um texto jurídico define o Direito Administrativo como disciplina restrita à prestação de 
- Um decreto presidencial disciplina organização e funcionamento da administração federal se
- Um órgão federal muda orientação interpretativa e pretende aplicar a nova leitura retroati
- O art. 30 da LINDB prevê respostas a consultas e súmulas administrativas destinadas a aume
- Um agente afirma que pode renunciar a uma competência legalmente atribuída porque entende 
- Uma repartição revisa ato favorável praticado há mais de seis anos, sem constatação de má-
- A Administração pretende suprimir vantagem que passou a produzir efeitos concretos na esfe
- Um processo administrativo federal está integralmente instruído há 20 dias. A Administraçã
- Uma decisão de controle pretende invalidar contrato público apoiando-se apenas na expressã
- Uma orientação administrativa publicada em 2025 altera a leitura de expressão legal indete
- Um pedido de ato público de liberação econômica, sujeito à Lei nº 13.874/2019 e sem exceçã
- Ao examinar pedido de anulação de ato favorável antigo, o revisor encontra orientação gera
- Ao justificar sanção a gestor, uma decisão ignora restrições orçamentárias reais e circuns
- O gestor decide anular de imediato ato anterior que produziu efeitos patrimoniais concreto
- Na formulação clássica do Estado, povo, território e governo soberano são elementos concei
- Segundo a Constituição, a autonomia dos Municípios significa soberania internacional própr
- Ao identificar pessoas e estruturas que executam tarefas administrativas, adota-se o senti
- O caso Blanco, ocorrido na França em 1873, foi decidido originariamente pelo Conselho Cons
- Quando desempenham função administrativa, órgãos dos Poderes Legislativo e Judiciário da U
- Um parecer doutrinário amplamente citado pelos gestores possui, por essa única razão, a me
- O sistema brasileiro de jurisdição una admite a existência de processos e recursos adminis
- A supremacia do interesse público autoriza a Administração a afastar direito fundamental e
- A Súmula 473 do STF distingue a anulação de atos ilegais da revogação por conveniência ou 
- Pelo art. 49 da Lei nº 9.784/1999, qualquer pedido administrativo deve ser considerado def
- A Lei nº 9.784/1999 determina decisão explícita nos processos sob competência administrati
- A aprovação tácita do art. 3º, IX, da Lei nº 13.874/2019 incide automaticamente sobre toda
- Uma decisão administrativa que invoca apenas uma noção abstrata de eficiência, sem conside
- Ao invalidar contrato administrativo, o decisor pode omitir suas consequências jurídicas e
- O art. 23 da LINDB determina regime de transição quando indispensável à execução proporcio
- O art. 24 da LINDB manda desconsiderar a orientação administrativa em vigor quando o ato f
- Respostas administrativas a consultas e súmulas administrativas podem integrar instrumento
- O prazo quinquenal de decadência previsto no art. 54 da Lei nº 9.784/1999 impede a anulaçã
- A regra de jurisdição una não é incompatível com o art. 217, § 1º, da Constituição, que ex
- O Tema 138 do STF autoriza o desfazimento imediato de ato administrativo com efeitos indiv
```

## Pedido 13: Aplicação da lei penal (`direito-penal.aplicacao-lei-penal`, 1365 tópicos de edital)

```
Matéria: direito-penal.aplicacao-lei-penal (Aplicação da lei penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Legalidade, anterioridade, interpretação e analogia": 30
- "2. Sucessão de leis penais, retroatividade e ultratividade": 15
- "3. Tempo e lugar do crime, territorialidade e extensão do território": 19
- "4. Extraterritorialidade, pena estrangeira e eficácia das sentenças": 17
- "5. Prazos penais, frações e conflito aparente de normas": 19

Já existem (não repita):
- Um regulamento administrativo passa a proibir uma conduta e, sem lei penal anterior, prete
- Uma pessoa pratica conduta integralmente em 4 de maio, e seu resultado ocorre em 10 de mai
- Uma conduta criminosa é iniciada no Brasil e produz resultado em país vizinho. Para defini
- Uma lei publicada em junho torna criminosa conduta realizada e encerrada em abril, quando 
- Após condenação definitiva, uma nova lei deixa de considerar criminosa a mesma conduta, se
- Uma lei editada para situações de emergência prevê expressamente que vigorará por noventa 
- Em um porto brasileiro ocorre fato penalmente relevante no interior de embarcação privada 
- Um cidadão brasileiro pratica crime comum integralmente no exterior, e o fato não está nas
- Um prazo penal material de quatro dias tem início em 5 de novembro. Aplicando a regra do a
- Ao efetuar cálculo de pena privativa de liberdade, encontra-se parcela final de 8 dias e 9
- Após um fato penal, entra em vigor lei que reduz a pena. Antes do julgamento, outra lei vo
- Uma lei penal posterior reduz a consequência sancionatória de fato já julgado definitivame
- Uma conduta criminosa permanente inicia-se antes de nova lei agravadora e continua depois 
- Em viagem fora do território brasileiro ocorre crime no interior de embarcação brasileira 
- Pessoa a serviço da administração pública brasileira pratica no exterior crime contra essa
- Um tratado internacional obriga o Brasil a reprimir certa conduta praticada integralmente 
- Um estrangeiro comete crime contra brasileiro no exterior, sem qualquer parcela de conduta
- A mesma infração é julgada no Brasil em hipótese de aplicação válida da lei nacional, tend
- Uma sentença penal estrangeira é apresentada para produzir no Brasil obrigação de reparar 
- Uma conduta não corresponde ao sentido possível do tipo penal existente, mas se assemelha 
- Uma norma penal em branco contém tipo e pena em lei, mas seu complemento administrativo mu
- Uma pessoa compara um prazo penal material de cinco dias e um prazo processual penal comum
- Em uma conduta unitária, um fato menos abrangente é etapa necessária e sem autonomia lesiv
- Dois dispositivos penais simultaneamente vigentes parecem regular o mesmo fato. Um descrev
- Uma legislação posterior estabelece pena mínima mais alta, mas nova causa especial de redu
- Durante revisão de ato administrativo que tentou definir um novo crime, a equipe concluiu 
- Uma lei nova aboliu a criminalização de fato pelo qual alguém fora condenado. Por força do
- Em crime de conduta instantânea realizado em 3 de janeiro e consumado por resultado materi
- Um crime iniciado no Brasil e com resultado produzido no exterior deve ser considerado com
- Um prazo penal material de três dias, iniciado no dia 20, terá como dias computados 20, 21
- Ao cessar a situação excepcional que motivou uma lei penal emergencial, os fatos praticado
- Uma condenação penal já transitada em julgado não impede aplicação de lei superveniente ma
- O CP, art. 5º, § 1º, considera qualquer embarcação privada brasileira, inclusive atracada 
- Genocídio praticado fora do Brasil por pessoa domiciliada no território brasileiro figura 
- Quando brasileiro pratica crime comum no exterior, o CP, art. 7º, II, b, permite aplicação
- Crime de estrangeiro contra brasileiro praticado inteiramente no exterior pode, nos termos
- Em relação ao mesmo delito julgado no Brasil, o CP, art. 8º, prevê sempre abatimento integ
- Uma infração permanente começa antes de lei agravadora e cessa apenas depois que ela entra
- Na sucessão examinada pela Súmula 501 do STJ, é lícito selecionar apenas a pena inicial da
- Sentença penal estrangeira apta a produzir efeitos civis equivalentes no Brasil pode ser h
- Uma lei penal especial que incrimina conduta fora do Código Penal impede integralmente a a
- No cálculo de pena restritiva de direitos, a parcela final de seis horas, quando represent
- Se uma pessoa pratica vários verbos descritos em um mesmo tipo de ação múltipla em context
- Uma disposição incriminadora declara expressamente que só incide caso o fato não constitua
- Um crime instantâneo já estava completamente encerrado antes da vigência de lei penal mais
```

## Pedido 14: Contratos administrativos na Lei 14.133/2021 (`direito-administrativo.contratos-administrativos`, 1364 tópicos de edital)

```
Matéria: direito-administrativo.contratos-administrativos (Contratos administrativos na Lei 14.133/2021)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, espécies, planejamento e interpretação": 22
- "2. Formalização, publicação, garantias e vigência": 18
- "3. Alterações, reajustamento, repactuação e revisão": 19
- "4. Execução, gestão, fiscalização, preposto e desempenho": 25
- "5. Inexecução, extinção, nulidade e consolidação": 16

Já existem (não repita):
- Um contrato de manutenção foi celebrado pela Administração e surgiu controvérsia sobre lac
- No curso de contrato de obras, a Administração precisa alterar especificações para adequar
- Antes da contratação de suporte de sistemas, uma equipe compara soluções e depois define d
- Um contrato foi assinado após procedimento licitatório regular. Sem urgência, em qual praz
- A Administração comprará bens com entrega imediata e integral, sem obrigações posteriores 
- Um contrato comum de serviços prevê garantia de execução, sem justificativa especial de co
- Na execução de serviço contratado, a empresa indica pessoa para representá-la no local, re
- O fiscal constata repetidas entregas fora dos padrões de um contrato de fornecimento e pre
- A Administração pretende contratar fornecimento contínuo por quatro anos, com demonstração
- Em um contrato de reforma de prédio no valor inicial atualizado de R$ 800 mil, justifica-s
- Em contrato de serviço contínuo, o índice previsto no ajuste apresenta variação após o int
- Contrato contínuo com dedicação exclusiva de mão de obra registra alteração de custos deco
- Um contrato de serviço tecnológico prevê disponibilidade mensal mínima e mecanismo objetiv
- Um contrato resultante de contratação direta foi assinado sem urgência em uma segunda-feir
- Em contrato de obra, a Administração decide acrescer uma etapa, respeitando limites quanti
- Ao receber uma compra parcelada, o fiscal atesta provisoriamente o recebimento físico de b
- A Administração pretende prorrogar pela sexta vez contrato de fornecimento contínuo, alcan
- Em uma obra, houve duas ordens escritas de suspensão, de 46 e 47 dias úteis, sem superposi
- Em contrato com matriz de riscos expressa, certa oscilação de insumos foi atribuída ao con
- Em serviço contínuo com duração superior a um ano, o contrato tem valor total de R$ 3 milh
- Um contrato de prestação essencial apresenta vício insanável, mas sua paralisação imediata
- Durante a vigência de contrato contínuo, o contratado solicita revisão por fato extraordin
- A Administração pretende reduzir em 32% a quantidade contratada em reforma de equipamento,
- Em contrato de serviço contínuo com dedicação exclusiva de mão de obra, a empresa não paga
- Após modificação unilateral lícita de projeto que aumentou significativamente encargos, a 
- Em contrato de manutenção regido pela Lei nº 14.133/2021, a Administração pode exercer fis
- Após a assinatura de um contrato decorrente de contratação direta sem urgência, o responsá
- Em um contrato de prestação de serviços, a contratação de apoio técnico especializado pode
- No planejamento de uma contratação por escopo, a equipe classificou o estudo técnico preli
- Em contrato de obra, a Administração exige garantia de execução prevista no edital. A lei 
- Ao receber bens fornecidos por empresa contratada, o fiscal verificou apenas que as caixas
- Em serviço contínuo com dedicação exclusiva de mão de obra, o aumento de custos comprovado
- Em reforma de equipamento, a Administração pode reduzir unilateralmente a quantidade contr
- Contrato de fornecimento contínuo pode alcançar nove anos de vigência total por prorrogaçõ
- Em contrato de serviço com indicador de disponibilidade, uma redução de pagamento prevista
- Ao preparar a extinção unilateral de contrato por inadimplemento da contratada, a Administ
- Em contrato no qual a empresa contratada descumpre obrigações próprias, a presença de fisc
- Um contrato de serviço de fornecimento público em regime de monopólio, no qual a Administr
- Durante uma obra, a Administração justificou a necessidade excepcional de antecipar os efe
- O contratado solicitou recomposição econômico-financeira durante a vigência e antes de eve
- Em contrato cuja nulidade foi reconhecida após a execução parcial, a Administração fica di
- Fora das exceções legais, uma ordem escrita da Administração que paralise a execução por p
- Uma empresa vencedora de contrato de serviço decidiu repassar integralmente o objeto a out
- Ao reconhecer nulidade de um contrato, a autoridade pode, por necessidade justificada de c
- A prerrogativa do art. 104 permite à Administração modificar unilateralmente as cláusulas 
```

## Pedido 15: Processo administrativo federal (Lei 9.784/1999) (`legislacao.lei-9784-1999-processo-administrativo`, 1355 tópicos de edital)

```
Matéria: legislacao.lei-9784-1999-processo-administrativo (Processo administrativo federal (Lei 9.784/1999))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, princípios, sujeitos e instauração": 23
- "2. Competência, delegação, avocação e imparcialidade": 20
- "3. Atos, intimação, instrução e produção de provas": 19
- "4. Decisão, motivação, decisão coordenada e prazos": 19
- "5. Recursos, revisão, autotutela, decadência e prioridade": 19

Já existem (não repita):
- Um requerente entrega pedido com dados essenciais, mas esquece documento cuja ausência pod
- Uma unidade administrativa federal instaura, por iniciativa própria, procedimento para ver
- Uma pessoa afetada pela futura decisão não apresentou o requerimento que iniciou o process
- Uma autoridade pretende transferir o exercício de atribuições técnicas a outra unidade, se
- Um ato de delegação determina que unidade especializada julgue recursos apresentados contr
- Uma chefia identifica motivo excepcional e justificado para assumir provisoriamente atribu
- Uma autoridade designada para decidir um requerimento participou da instrução do mesmo pro
- Um interessado não comparece à diligência para a qual foi regularmente intimado, sem ofere
- Para uma oitiva em procedimento federal, a unidade organiza intimação com antecedência leg
- Um interessado afirma que documentos comprobatórios do pedido estão arquivados em outra un
- Um órgão consultivo deixa transcorrer o prazo sem emitir parecer obrigatório, porém não vi
- Um processo teve sua instrução encerrada hoje. Não existe lei especial com prazo diverso. 
- Três unidades federais do mesmo Poder precisam participar de decisão relevante e divergem 
- A Administração cientifica pessoa de decisão desfavorável e o prazo ordinário de dez dias 
- Uma autoridade indefere requerimento e recebe recurso administrativo devidamente fundament
- Ao julgar um recurso administrativo, a autoridade percebe possibilidade de modificar a dec
- Uma pessoa sancionada descobre documento novo capaz de reduzir a gravidade atribuída à con
- Uma autoridade revisa ato favorável com pagamentos mensais regulares e sem indícios de má-
- Um recurso é protocolado tempestivamente perante unidade incompetente. O recorrente possui
- No processo, faltou parecer de órgão consultivo. O documento era obrigatório e vinculante;
- Em reunião coordenada, um interessado devidamente habilitado como ouvinte exige votar e re
- Um órgão decide convalidar ato com erro de forma sanável. A correção não lesa o interesse 
- Após encerrar a fase de instrução, o interessado é informado de que terá direito a manifes
- A Administração toma conhecimento de nova circunstância que recomenda cessar um ato válido
- Uma pessoa com neoplasia maligna solicita prioridade em processo federal e apresenta docum
- A unidade federal instaurou processo sem requerimento externo para rever uma autorização. 
- Em processo federal de interesse individual, o requerente somente poderá se manifestar se 
- Uma unidade federal pode delegar parte das atribuições a outra unidade que não esteja hier
- Uma autoridade pode avocar por prazo indeterminado atribuições de outra unidade de igual n
- Uma pessoa intimada para comparecer à repartição deve, como regra, receber comunicação com
- Em processo de apuração de irregularidade, o não comparecimento do interessado a uma intim
- Se o interessado informa que prova documental está na própria Administração, o órgão instr
- Quando o órgão consultivo atrasa parecer obrigatório não vinculante, o processo deve neces
- Se a instrução de processo administrativo federal foi concluída, a Administração dispõe, e
- Três unidades federais que atuem na aplicação de sanção podem utilizar a decisão coordenad
- A autoridade que proferiu decisão objeto de recurso recebe inicialmente o requerimento e, 
- O recurso contra decisão administrativa tem, como regra, efeito suspensivo automático, que
- Ao apreciar recurso, a autoridade pode modificar a decisão em desfavor do recorrente, mas 
- Em processo de benefício instaurado sob determinada interpretação administrativa, a unidad
- Uma autoridade que percebe ter sido testemunha no processo deve comunicar seu impedimento 
- Em consulta pública aberta durante processo de interesse geral, quem apresenta contribuiçã
- Num recurso administrativo, o recorrente alega desrespeito a súmula vinculante. Se a autor
- Na Lei nº 9.784/1999, o prazo de cinco anos para anular ato favorável continua correndo da
- Mesmo após o interessado desistir por escrito de um pedido, o processo poderá prosseguir s
- Em decisão coordenada encerrada, a Administração pode deixar de publicar qualquer informaç
```

## Pedido 16: História e Geografia do Maranhão (`maranhao.conhecimentos-estaduais`, 1352 tópicos de edital)

```
Matéria: maranhao.conhecimentos-estaduais (História e Geografia do Maranhão)
Capítulos e quantas questões quero em cada um (total 100):
- "1. França Equinocial, portugueses e holandeses": 20
- "2. Bequimão, companhias de comércio e Independência": 20
- "3. Balaiada, República e século XX": 20
- "4. Localização, relevo, clima, rios e vegetação": 20
- "5. População, economia, transportes e cultura": 20

Já existem (não repita):
- A cidade de São Luís foi fundada em 1612 por:
- São Luís é a única capital estadual brasileira fundada por franceses.
- Na Batalha de Guaxenduba, em 1614, as tropas luso-brasileiras venceram os franceses, que d
- A criação do Estado do Maranhão, em 1621, separado do Estado do Brasil, explica-se princip
- Os holandeses ocuparam São Luís entre 1641 e 1644, quando foram expulsos com a resistência
- Assinale a sequência cronológica correta.
- A Revolta de Bequimão, de 1684, teve entre suas causas:
- Na Revolta de Bequimão, os rebeldes expulsaram os jesuítas e aboliram o monopólio comercia
- A Companhia Geral de Comércio do Grão-Pará e Maranhão, criada no período pombalino, estimu
- O Maranhão aderiu à Independência do Brasil logo em setembro de 1822, sem resistência de f
- A Batalha do Jenipapo, de 13 de março de 1823, ocorreu:
- A Balaiada (1838–1841) foi:
- O nome Balaiada deriva do apelido de Manuel Francisco dos Anjos Ferreira, o Balaio, que fa
- Negro Cosme, um dos líderes da Balaiada, comandava:
- A Balaiada foi reprimida por Luís Alves de Lima e Silva, que mais tarde receberia o título
- Após a Revolução de 1930, o Maranhão passou a ser governado por:
- Sobre a segunda metade do século XX no Maranhão, é correto afirmar que:
- A Estrada de Ferro Carajás transporta minério de ferro extraído no próprio território mara
- O Centro de Lançamento de Alcântara é uma base de:
- O Maranhão limita-se a leste com o Piauí, separado em grande parte pelo rio Parnaíba.
- Os estados que fazem divisa com o Maranhão são:
- O rio que marca grande parte da divisa entre Maranhão e Pará é o:
- Mearim, Itapecuru, Pindaré e Munim são exemplos de rios genuinamente maranhenses.
- O Maranhão possui 217 municípios.
- Os Lençóis Maranhenses caracterizam-se por:
- A Baixada Maranhense é uma área de planícies com campos alagáveis e lagos, situada no nort
- Quanto à vegetação do Maranhão, é correto afirmar que o estado apresenta:
- A mata dos cocais, presente no Maranhão, é caracterizada pela presença de palmeiras como o
- Sobre o clima do Maranhão, é correto afirmar que:
- Parte do território maranhense integra a Amazônia Legal.
- Além dos Lençóis Maranhenses, outro parque nacional situado no sul do Maranhão é o da:
- A região de Balsas, no sul do Maranhão, destaca-se pela:
- O extrativismo do babaçu no Maranhão é realizado em grande parte pelas quebradeiras de coc
- A indústria de alumínio e alumina instalada em São Luís é conhecida como:
- O Complexo Portuário de São Luís, que inclui o porto do Itaqui e terminais privados, está 
- Entre as cidades maranhenses de maior população, além da capital, estão:
- O Bumba meu boi é a principal festa popular do Maranhão e é reconhecido como patrimônio cu
- O Tambor de Crioula é uma manifestação cultural maranhense:
- O Centro Histórico de São Luís, com casarões revestidos de azulejos portugueses, é reconhe
- Associe corretamente: I – Bequimão; II – Balaiada; III – Guaxenduba; IV – Jenipapo. ( ) ex
- A capital São Luís localiza-se em uma ilha, no Golfão Maranhense.
- Os jesuítas foram apoiados pelos colonos na Revolta de Bequimão por defenderem a ampliação
```

## Pedido 17: Sujeitos do processo (`processo-civil.sujeitos-processo`, 1348 tópicos de edital)

```
Matéria: processo-civil.sujeitos-processo (Sujeitos do processo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Partes, capacidade, procuradores, deveres e despesas": 21
- "2. Procuradores, sucessão e litisconsórcio": 23
- "3. Intervenção de terceiros e institutos próximos": 24
- "4. Juiz: poderes, imparcialidade, responsabilidade e auxiliares": 18
- "5. Ministério Público, Advocacia Pública e Defensoria Pública": 14

Já existem (não repita):
- Uma adolescente de 14 anos pretende cobrar indenização. Seu representante legal procura ad
- Um réu citado por edital permanece revel e não constitui advogado. Nos termos do art. 72 d
- Um advogado possui procuração geral para o foro e deseja celebrar transação em nome da par
- Dois autores têm pedidos conexos e optam por demandar conjuntamente sem obrigação legal de
- Terceiro possui relação jurídica afetada pela solução de litígio alheio e quer atuar em fa
- Uma pessoa foi condenada por alterar conscientemente a verdade em demanda civil. Que insti
- Um fiador é demandado pelo credor pelo pagamento integral da dívida garantida e deseja tra
- Na ação civil, o juiz depende de conhecimento técnico de engenharia para esclarecer fato r
- Processo envolve interesse de criança incapaz e o Ministério Público não é parte. Que regr
- Uma pessoa natural recebe gratuidade, perde a ação e é condenada a honorários. Qual efeito
- Em processo com capacidade postulatória regular, o advogado precisa praticar ato urgente p
- Três réus deveriam integrar relação processual indivisível, que exige sentença uniforme; u
- O comprador de um imóvel é demandado por terceiro que afirma ter melhor direito de proprie
- Em demanda contra uma sociedade, a parte pede desconsideração somente durante o cumpriment
- Uma entidade especializada é admitida para contribuir com discussão relevante em caso judi
- O juiz identifica pretensão a descumprir ordem judicial e considera medida coercitiva não 
- Uma parte descobre que o juiz é amigo íntimo do advogado da parte contrária. Que enquadram
- Uma autoridade processual pratica ato jurisdicional com atraso injustificado, apesar de re
- A Fazenda Pública integra processo patrimonial individual, sem incapazes nem outro interes
- A Defensoria Pública recebe intimação pessoal de ato processual sujeito a prazo geral, sem
- Uma parte descumpre deliberadamente ordem judicial; seu advogado também é apontado no epis
- Uma causa apresenta litisconsórcio passivo necessário, mas a relação jurídica admite soluç
- O credor demanda apenas um dos devedores solidários. O réu pede chamamento de codevedor do
- Em ação proposta já com pedido de desconsideração da personalidade jurídica contra sócio, 
- Em ação civil com interesse de incapaz, o Ministério Público atua como fiscal; a Defensori
- Um incapaz pode figurar como parte em processo civil, mas necessita da representação ou as
- Na procuração geral para o foro, o advogado recebe automaticamente poder para desistir da 
- A existência de comunhão de direitos, conexão pelo pedido ou afinidade em ponto comum de f
- A assistência processual dos arts. 119 a 124 permite a terceiro intervir com base apenas e
- O CPC prevê intervenção do Ministério Público como fiscal da ordem jurídica quando process
- Em litisconsórcio necessário, faltar a citação de um integrante provoca a mesma nulidade i
- Se a denunciação da lide for indeferida, não promovida ou não permitida, o direito regress
- No incidente de desconsideração instaurado depois da petição inicial, o sócio é ouvido em 
- No art. 145 do CPC, amizade íntima do juiz com uma parte constitui hipótese de suspeição, 
- A presença da Fazenda Pública como parte no processo civil determina, por si só, a intimaç
- Se o juiz limita o litisconsórcio facultativo excessivamente numeroso, o requerimento de l
- Quando o direito litigioso é alienado durante o processo, o adquirente assume automaticame
- O juiz pode usar meios coercitivos adequados para cumprir ordem de pagamento e, ao dilatar
- Uma entidade que ingressa como amicus curiae, por determinação do juiz, adquire a competên
- O CPC/2015 disciplina oposição nos arts. 682 a 686 e, para corrigir indicação equivocada d
- No sistema de sanções processuais, a multa por ato atentatório à dignidade da justiça prev
- O juiz que é instado a ordenar providência devida e, sem justo motivo, deixa o requeriment
- O prazo em dobro da Defensoria Pública se aplica inclusive quando a lei estabelecer expres
- Quando o sócio ou a pessoa jurídica é chamado a responder incidente de desconsideração, o 
- Na assistência simples, o terceiro passa a ser titular da relação litigiosa principal, raz
```

## Pedido 18: Gestão de projetos e gestão da qualidade (`administracao-geral.projetos-qualidade`, 1344 tópicos de edital)

```
Matéria: administracao-geral.projetos-qualidade (Gestão de projetos e gestão da qualidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, governança e abordagens de projetos": 22
- "2. PMBOK, escopo, cronograma e valor agregado": 24
- "3. Viabilidade, riscos, recursos e decisões": 17
- "4. Teóricos, PDCA, custos e qualidade em serviços": 19
- "5. Ferramentas da qualidade, serviços e melhoria de processos": 18

Já existem (não repita):
- Uma equipe implanta um novo sistema de atendimento durante oito meses; depois, o serviço p
- Uma iniciativa coordena projetos de tecnologia, capacitação e infraestrutura para obter be
- Uma questão compara o PMBOK da 6ª edição com a 7ª edição. Qual par expressa corretamente a
- Ao decompor o escopo total da construção de um portal em entregas menores até pacotes de t
- Uma equipe registra o número de atrasos por categoria e quer priorizar as causas mais freq
- Uma instituição reúne propostas independentes e decide quais executar com base no alinhame
- Uma equipe quer reduzir falhas de atendimento por meio de treinamento preventivo e padroni
- Em iniciativa de melhoria, a equipe executou teste-piloto e agora confronta indicadores ob
- Projeto apresenta EV de R$ 80 mil e AC de R$ 90 mil. Qual é a variação de custo (CV), segu
- Uma pessoa precisa organizar itens necessários, manter locais definidos, limpar, padroniza
- Um projeto possui as atividades A (3 dias); depois, B (4 dias) e C (6 dias) em paralelo; D
- Um risco de indisponibilidade tecnológica pode ser reduzido mediante redundância de servid
- Uma organização seleciona entre dois investimentos, descontando entradas futuras à taxa mí
- Uma unidade percebe diferenças entre expectativa do usuário e percepção do atendimento e p
- Em um escritório de projetos, analistas disponibilizam modelos e treinamentos sem exigir u
- Uma matriz GUT pontua duas ocorrências com a mesma escala de 1 a 5: X=(5,4,3) e Y=(4,3,4).
- Com orçamento total BAC de R$ 200 mil e CPI de 0,80, a equipe prevê manutenção da mesma ef
- Uma equipe decide contratar serviço de escopo altamente estável, buscando maior previsibil
- Em projeto com seis participantes, a equipe considera canais bidirecionais diretos entre c
- No acompanhamento de uma fila de atendimentos, a equipe quer diferenciar causas especiais 
- No projeto, a atividade X não integra o caminho crítico e possui folga total de três dias.
- Uma avaliação ex ante compara investimento inicial de R$ 1.000 com dois recebimentos anuai
- Um processo está estatisticamente estável, com pontos dentro dos limites calculados, mas q
- Ao comparar desempenho de equipes em uma estrutura matricial fraca, o responsável pelo pro
- Uma equipe publica Carta de Serviços com prazos e canais, mas não acompanha satisfação, cu
- Uma iniciativa temporária para implementar sistema de serviços constitui projeto mesmo que
- Na 6ª edição do PMBOK, os cinco grupos de processos equivalem necessariamente às cinco fas
- Uma matriz de partes interessadas pode considerar poder, interesse e influência, e deve se
- O diagrama de Ishikawa comprova, por si mesmo, a relação causal entre cada hipótese levant
- Na abordagem de Juran, planejar a qualidade, controlá-la e promovê-la por melhorias formam
- No valor agregado, um projeto com EV menor que PV apresenta variação de cronograma negativ
- O risco de uma falha tecnológica que já ocorreu deve continuar classificado exclusivamente
- Em comparação de alternativas de investimento, o VPL incorpora uma taxa de desconto, enqua
- Custos de avaliação correspondem ao retrabalho descoberto pelo usuário após entrega do ser
- O escritório de projetos de natureza diretiva pode gerenciar diretamente projetos, diferen
- Um processo tem resultados dentro dos limites de controle da carta estatística; por isso, 
- O conjunto de princípios e domínios da 7ª edição do PMBOK não tornou tecnicamente inúteis 
- Na modelagem BPMN, o fluxo de sequência pode atravessar indiscriminadamente pools distinto
- Ao adaptar práticas de outro serviço por benchmarking, a equipe pode aprender com um padrã
- Uma equipe registrou redução de prazo após uma mudança e concluiu que a análise ex post es
- O nivelamento de recursos pode alterar a data de término do projeto para eliminar sobrealo
- A racionalidade limitada de Herbert Simon pressupõe capacidade integral de identificar tod
- A Lei 13.460/2017 distingue divulgação de padrões na Carta de Serviços da avaliação contin
- Na classificação clássica dos custos da qualidade, inspeção é prevenção e treinamento real
- Quando a equipe transfere contratualmente parte das consequências econômicas de um risco n
```

## Pedido 19: Comportamento organizacional: motivação, liderança, grupos, conflito, poder e mudança (`administracao-geral.comportamento-organizacional`, 1343 tópicos de edital)

```
Matéria: administracao-geral.comportamento-organizacional (Comportamento organizacional: motivação, liderança, grupos, conflito, poder e mudança)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Indivíduo e organização: percepção, atitudes, socialização e comprometimento": 18
- "2. Motivação: necessidades, expectativas, justiça, metas e desenho do trabalho": 18
- "3. Liderança, influência, autoliderança e poder nas organizações": 17
- "4. Grupos e equipes, competência interpessoal, conflito e negociação": 28
- "5. Mudança, inovação e aprendizagem organizacional": 19

Já existem (não repita):
- Após queda na produtividade de um setor, a análise separou atitudes das pessoas, normas de
- Ao discutir novo procedimento, uma pessoa diz: 'Acredito que ele é injusto; isso me irrita
- Durante o ingresso, uma instituição sinaliza informalmente oportunidades de formação, mas 
- Uma pessoa permanece na organização porque perderia benefícios acumulados e enfrentaria cu
- Durante desacordo, uma integrante busca entender as dificuldades do colega, comunica o pró
- Uma profissional deseja assumir tarefas de desenvolvimento, mas as oportunidades são bloqu
- Em programa de metas, uma pessoa acredita que consegue cumprir a tarefa, mas que os result
- Dois analistas recebem remunerações diferentes e uma pessoa compara contribuições, experiê
- Uma equipe deixa de preencher formulário desagradável quando entrega relatórios corretamen
- A organização redesenha função para permitir decidir métodos, assumir responsabilidade por
- Uma direção avalia líder por relações com integrantes, estrutura da tarefa e poder da posi
- Uma líder percebe obstáculos burocráticos à consecução de metas, esclarece quais caminhos 
- Em mesmo setor, líder mantém relações de confiança e intercâmbios diferentes com cada inte
- Uma pessoa sem cargo de chefia orienta decisões técnicas porque domina procedimento raro e
- Uma líder prioriza escuta, crescimento e autonomia dos integrantes, apoiando suas necessid
- Cinco pessoas elaboram parcelas independentes de relatórios, sem necessidade de coordenaçã
- Uma equipe temporária mantém estratégia inicial até aproximar-se da metade do prazo, quand
- Em reuniões, integrantes evitam contradizer consenso aparente, ignoram alertas técnicos e 
- Duas áreas discutem critérios de prioridade, investigam necessidades e criam solução que a
- Antes de negociar contrato, a organização identifica proposta de outro fornecedor viável c
- Uma instituição tenta implantar processo digital e identifica treinamento insuficiente com
- Após piloto bem-sucedido, uma direção celebra resultados e encerra imediatamente o program
- Uma equipe tenta reduzir atrasos ajustando cronogramas, mas posteriormente questiona a pró
- Um grupo cria ideia de atendimento inovadora em oficina, mas a organização ainda não a tes
- Integrantes resistem ao novo procedimento por desconhecerem a ferramenta e temerem erros. 
- Após um atraso, a liderança atribuiu a falha exclusivamente à desatenção de uma pessoa e i
- Uma pessoa passa por treinamento, integração com colegas e compreensão gradual das expecta
- Uma integrante prefere projetos com responsabilidade pelos resultados, metas desafiadoras 
- Uma liderança estabelece prêmios condicionados ao alcance de resultados previamente defini
- Em tarefa coletiva, quando resultados pessoais são pouco identificáveis, integrantes podem
- Uma equipe mantém seus membros por obrigação moral percebida, mesmo sem ganhos afetivos ou
- No modelo de Locke e Latham, metas específicas e desafiadoras podem favorecer desempenho q
- Uma líder adapta quantidade de direção e apoio à competência e ao comprometimento do integ
- Uma pessoa influencia colegas por sua capacidade de conceder oportunidades valorizadas. Is
- Na negociação integrativa, as partes devem concentrar-se apenas em posições declaradas e d
- Uma pessoa afirma gostar da organização, mas permanece principalmente porque acredita que 
- Uma organização entrega prêmios a todos de modo uniforme e afirma que, por isso, sua polít
- Segundo Hackman e Oldham, acrescentar tarefas sem ampliar responsabilidade decisória corre
- Na tipologia de Etzioni, o controle normativo se baseia em recompensas monetárias e costum
- Uma equipe experiente possui tarefas bem estruturadas e recebe feedback intrínseco do trab
- Após uma discussão, integrantes que inicialmente eram moderadamente favoráveis a uma propo
- Uma equipe autogerida distribui responsabilidades e utiliza liderança compartilhada confor
- Uma equipe promove inovação por meio de oficina de ideias, mas nenhuma proposta chega a se
- Uma gestão substitui ameaças pela capacitação e participação de pessoas que questionaram u
- Uma organização muda apenas o procedimento para cumprir meta antiga e afirma ter realizado
```

## Pedido 20: Variação linguística e norma linguística (`portugues.variacao-linguistica-norma-linguistica`, 1334 tópicos de edital)

```
Matéria: portugues.variacao-linguistica-norma-linguistica (Variação linguística e norma linguística)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Língua heterogênea e dimensões da variação": 14
- "2. Norma-padrão, usos cultos e comunicação oficial": 45
- "3. Oralidade, escrita, retextualização e cultura digital": 14
- "4. Publicidade, literatura e inovação expressiva": 14
- "5. Educação linguística, BNCC, avaliação e leitura crítica": 13

Já existem (não repita):
- Em uma pesquisa lexical, pessoas de diferentes regiões chamam o mesmo alimento de 'mandioc
- Uma pessoa escreve 'Te aviso depois' a um amigo e, para uma comunicação institucional, esc
- Um grupo de praticantes de uma atividade utiliza expressões próprias para reconhecer seus 
- Um estudo compara a forma antiga 'vossa mercê' com formas de tratamento desenvolvidas e di
- Uma mensagem de áudio contém entonação e pausas; um aviso escrito apresenta disposição grá
- Uma solicitação destinada a tramitar formalmente inicia com 'Oi, me dá uma força aí e reso
- Uma pesquisa registra formas efetivamente usadas por pessoas escolarizadas em contextos mo
- Em uma apresentação oral planejada, a pessoa lê texto formal; em mensagens digitais, outra
- Um grupo utiliza 'vc', 'pq' e 'tbm' em conversa por aplicativo e entende as mensagens sem 
- Uma narrativa apresenta a fala da personagem: 'Oxente, que surpresa boa!'. Para analisar e
- Um texto institucional contém 'Na minha opinião, o atendimento foi uma vergonha'. Qual alt
- Uma entrevista registra 'Acho que talvez entreguemos o material amanhã'. A notícia publica
- Uma campanha destinada a público jovem usa o slogan 'Bora conhecer o projeto?'. O texto é 
- Uma análise identifica 'feedback' como vocábulo vindo de outra língua, 'viralizar' em sent
- O setor responsável por uma portaria troca 'vai dar um jeito naquela confusão' por 'Ficam 
- Uma atividade escolar pede comparar gravações de variedades regionais e revisar um comunic
- Uma pessoa é considerada intelectualmente incapaz por apresentar pronúncia associada à sua
- Um estudante escreve texto destinado à publicação e emprega formas usuais na conversa de s
- Um áudio de conversa familiar é convertido em transcrição escrita quase literal, enquanto 
- Durante apuração jornalística, uma pessoa diz 'Eu não tenho certeza; parece que o atendime
- Uma proposta pedagógica pretende substituir, em todas as gravações de entrevistas regionai
- Ao revisar uma portaria, sugerem inserir expressões rebuscadas que dificultam a compreensã
- Uma pesquisa registra, em interações monitoradas, construções utilizadas por falantes com 
- Uma campanha digital publica imagem acompanhada da legenda 'Partiu cuidar do bairro?', emp
- Uma atividade compara o relato oral de morador, o diálogo regional de personagem e um requ
- Em uma pesquisa, pessoas de regiões diferentes usam 'aipim' e 'macaxeira' para referir-se 
- A norma culta, por definição, é um conjunto imutável de prescrições idealizadas que coinci
- Um requerimento destinado a circulação administrativa exige identificação clara do pedido 
- Um pronunciamento oral preparado e um comentário escrito espontâneo numa rede social perte
- Em anúncio dirigido a público jovem, a expressão 'Bora participar?' pode ser um recurso es
- Como a BNCC orienta combater o preconceito linguístico, a escola deve deixar de ensinar us
- Uma comunicação oficial enviada por e-mail continua sujeita, conforme o gênero, a expectat
- Se uma entrevista registra 'Talvez retornemos amanhã', retextualizá-la como 'Retornaremos 
- Em narrativa ficcional, um personagem emprega regionalismo no discurso direto, enquanto o 
- Como os emojis possuem significado único e universal, seu uso elimina a necessidade de con
- Uma palavra originária de outra língua pode integrar a comunicação cotidiana como emprésti
- Se uma pronúncia possui maior prestígio social, isso demonstra que ela dispõe de estrutura
- Uma pessoa escreve em aplicativo para familiares de sua região: 'Oxente, vc já chegou?'. N
- Uma análise sociolinguística que registra usos monitorados de pessoas escolarizadas deve d
- Uma avaliação escolar pode comparar fala regional e escrita de requerimento formal, valori
- Uma portaria publicada por meio eletrônico dispensa as exigências de impessoalidade e padr
- Um pronunciamento formal pode ser produzido oralmente com alto planejamento, enquanto um c
- Quando uma obra literária apresenta uma forma coloquial no discurso direto de personagem, 
- Se um pesquisador acompanha o uso de determinado vocábulo em materiais de épocas diferente
- Uma avaliação de produção textual destinada à circulação pública aplica critérios de clare
```

## Pedido 21: Controle de constitucionalidade (`direito-constitucional.controle-constitucionalidade`, 1329 tópicos de edital)

```
Matéria: direito-constitucional.controle-constitucionalidade (Controle de constitucionalidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Supremacia constitucional, vícios e sistemas de fiscalização": 18
- "2. Controle difuso, reserva de plenário e leis locais": 20
- "3. ADI e ADC: objeto, legitimados, procedimento e julgamento": 25
- "4. ADO e ADPF: omissão, preceitos fundamentais e subsidiariedade": 23
- "5. Representação interventiva, decisões e limites do controle não judicial": 14

Já existem (não repita):
- Uma lei estadual passou a disciplinar matéria que a Constituição reservou à competência le
- Um projeto de lei relativo à matéria constitucionalmente reservada à iniciativa do chefe d
- Ao receber projeto aprovado, o chefe do Executivo identifica incompatibilidade com a Const
- Em processo individual, um juiz de primeira instância considera determinada lei incompatív
- Uma câmara de tribunal pretende declarar inválida uma lei por ofensa direta à Constituição
- Depois de declaração definitiva de inconstitucionalidade de lei pelo STF em controle difus
- Uma lei municipal de alcance geral é apontada como lesiva a preceito fundamental da Consti
- Há alegação de inconstitucionalidade diretamente dirigida a lei estadual editada após 1988
- Uma lei federal vigente tem gerado decisões judiciais divergentes quanto à sua compatibili
- Uma pessoa física, sem condição institucional especial, deseja propor ADI diretamente ao S
- Uma confederação sindical nacional contesta norma sobre matéria sem vínculo com os interes
- No julgamento do mérito de uma ADI, estão presentes oito integrantes do STF. Quantos votos
- Uma organização com representatividade técnica solicita contribuir no julgamento de ADI qu
- Em ADI regularmente proposta, o relator solicita informações às autoridades que editaram o
- Um legitimado pretende propor ADC para defender lei federal, mas apresenta apenas artigos 
- Após declarar omissão inconstitucional imputável a órgão administrativo, o STF comunica a 
- Uma ADPF foi ajuizada apesar de haver outra medida processual apta a sanar integral e efic
- Uma norma editada em 1975 ainda é aplicada e suscita controvérsia constitucional relevante
- Em ADI, seis ministros votam pela inconstitucionalidade de lei, mas somente esses seis apo
- O STF julga inconstitucional certa aplicação de dispositivo legal, preservando integralmen
- O tribunal de justiça declara inconstitucional lei municipal em face de norma da Constitui
- Lei do Distrito Federal disciplina tema tipicamente municipal; outra lei distrital versa s
- Diante de descumprimento estadual de princípio sensível do art. 34, VII, da Constituição, 
- O STF julga procedente representação interventiva federal por violação de princípio consti
- Um Tribunal de Contas afasta, em processo específico de controle externo, aplicação de nor
- Uma comissão legislativa examina a conformidade constitucional de um projeto de lei antes 
- Uma lei promulgada em 1970, incompatível com a Constituição de 1988, é necessariamente dec
- Uma proposição legislativa questionada por contrariar regra constitucional de iniciativa p
- Um juiz singular que afasta lei incidentalmente por inconstitucionalidade deve reunir prev
- Lei estadual posterior à Constituição pode ser objeto de ADI perante o STF quando arguida 
- Câmara de tribunal que deixa de aplicar lei por inconstitucionalidade viola a reserva de p
- Após declaração definitiva de inconstitucionalidade pelo STF em controle difuso, o art. 52
- Depois de distribuir uma ADI validamente proposta, o legitimado pode desistir livremente d
- Concedida cautelar em ADC, a Lei nº 9.868/1999 prevê suspensão do julgamento de processos 
- Na ADI, as autoridades responsáveis pela edição do ato impugnado e, em seguida, o AGU e o 
- Norma constitucional exige providência legislativa, mas a regulação existente contempla ap
- Julgada procedente ADO contra omissão do Poder Legislativo, o STF deve ordenar a edição da
- Lei municipal editada antes de 1988, quando relacionada a controvérsia constitucional rele
- A simples existência de ação individual de tramitação longa, mesmo incapaz de sanar a lesã
- Em ADPF, o relator pode conceder cautelar ad referendum do Plenário em hipótese legal de e
- A representação interventiva federal por ofensa aos princípios sensíveis pode ser proposta
- Entre os princípios constitucionais sensíveis relevantes para representação interventiva f
- Se seis ministros declaram inconstitucional uma lei em ADI, os mesmos seis votos bastam pa
- Em controle abstrato federal, a declaração parcial de inconstitucionalidade sem redução de
- A doutrina atribuída a Otto Bachof permite, no modelo jurisprudencial brasileiro, declarar
```

## Pedido 22: Clínica médica: cardiologia e pneumologia (`medicina.clinica-cardiologia-pneumologia`, 1329 tópicos de edital)

```
Matéria: medicina.clinica-cardiologia-pneumologia (Clínica médica: cardiologia e pneumologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Hipertensão, aterosclerose, prevenção e doença arterial": 25
- "2. Insuficiência cardíaca, coronariopatias, arritmias e síncope": 19
- "3. Emergências cardiovasculares, tromboembolismo e hipertensão pulmonar": 19
- "4. Asma e doença pulmonar obstrutiva crônica": 19
- "5. Pneumonias, integração cardiopulmonar e prevenção": 18

Já existem (não repita):
- Uma pessoa apresenta PA de 151/94 mmHg em duas consultas diferentes, com técnica adequada 
- Pessoa assintomática apresenta PA de 128/84 mmHg em consulta padronizada. Qual classificaç
- Paciente de 67 anos teve infarto há três anos e agora recebe orientação para reduzir risco
- Paciente apresenta dispneia aos esforços, ortopneia, estertores e ecocardiograma com fraçã
- Uma pessoa com palpitações tem ECG que mostra intervalos RR irregularmente irregulares, se
- Adulto sofre parada cardiorrespiratória com fibrilação ventricular no monitor e ausência d
- Adulto apresenta crises recorrentes de chiado, tosse noturna e limitação expiratória variá
- Ex-tabagista com dispneia progressiva realiza espirometria de qualidade com VEF1/CVF pós-b
- Paciente com dor torácica e náusea apresenta ECG inicial sem supradesnivelamento de ST, ma
- Uma pessoa apresenta ortopneia, turgência jugular e sinais de congestão com fração de ejeç
- Após episódio de perda rápida de consciência durante corrida, sem pródromos, pessoa com ca
- Pessoa com dispneia súbita, taquicardia e história recente de imobilização apresenta proba
- Adulto apresenta ausência de pulso e traçado de assistolia confirmado em dois cabos. Qual 
- Pessoa com asma leve relata usar apenas salbutamol quando sente falta de ar, sem qualquer 
- Paciente com DPOC estável usa broncodilatadores de longa duração e não tem exacerbações re
- Pessoa de 70 anos com pneumonia apresenta confusão nova, FR de 32/min, PA de 118/76 mmHg e
- Paciente com hipertensão muito elevada refere dor torácica abrupta e lancinante irradiada 
- Uma pessoa com diabetes e tabagismo apresenta dor de panturrilha após caminhar três quadra
- Adulto com ICFEr estável, fração de ejeção de 30% e congestão controlada usa apenas diurét
- Paciente com infarto extenso apresenta extremidades frias, confusão, oligúria e congestão 
- Pessoa com esclerose sistêmica desenvolve dispneia progressiva e ecocardiograma sugere aum
- Paciente em crise asmática grave apresenta exaustão, sonolência, dispneia intensa e quase 
- Uma pessoa com exposição tabágica longa tem VEF1/CVF pós-broncodilatador de 0,62 e VEF1 de
- Paciente de 68 anos com suspeita de pneumonia tem confusão nova, ureia acima do limite CUR
- Uma pessoa com dispneia súbita, síncope, hipotensão persistente e tromboembolismo pulmonar
- Uma pessoa apresenta pressão arterial de consultório 146/92 mmHg em duas visitas adequadam
- Uma pessoa tem PA de 195/110 mmHg, permanece sem sintomas relevantes e não há indício de l
- Pessoa que já sofreu infarto recebe estatina, orientação de cessação do tabagismo e contro
- Paciente congesto tem FE de 60%, alterações objetivas de enchimento cardíaco e dispneia tí
- Em um adulto em parada com fibrilação ventricular, compressões torácicas adequadas e desfi
- Uma pessoa com episódios variáveis de chiado e dispneia apresenta espirometria normal quan
- Paciente tabagista com dispneia progressiva tem espirometria pós-broncodilatador mostrando
- Durante caminhada, um paciente apresenta dor de panturrilha que cede em repouso e ITB de 0
- Paciente com dispneia e elevação da troponina durante sepse deve ser investigado conforme 
- Uma pessoa desmaia durante exercício sem pródromos e possui cardiopatia estrutural. A recu
- Paciente tem embolia pulmonar confirmada, hipotensão persistente e sinais de hipoperfusão.
- Paciente com suspeita de EP e probabilidade clínica alta apresenta D-dímero abaixo do limi
- Uma pessoa com asma persistente utiliza apenas beta-agonista de curta ação sob demanda, se
- Pessoa com pneumonia tem frequência respiratória de 28/min, pressão de 118/78 mmHg e ureia
- Pessoa com esclerose sistêmica e dispneia progressiva apresenta ecocardiograma com suspeit
- Adulto em parada apresenta atividade elétrica organizada no monitor, mas não tem pulso, ca
- Paciente com DPOC e VEF1/CVF pós-broncodilatador de 0,57 apresenta VEF1 de 45% do previsto
- Pessoa com asma apresenta exaustão, sonolência e silêncio auscultatório após período de si
- Pessoa idosa com pneumonia apresenta CURB-65 baixo, mas saturação de oxigênio muito reduzi
- Uma pessoa com pneumonia e derrame pleural pequeno, sem evidência de infecção da cavidade 
```

## Pedido 23: Gestão de pessoas: fundamentos e subsistemas (`gestao-pessoas.fundamentos-subsistemas`, 1326 tópicos de edital)

```
Matéria: gestao-pessoas.fundamentos-subsistemas (Gestão de pessoas: fundamentos e subsistemas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Evolução, modelos, estratégia e subsistemas de gestão de pessoas": 23
- "2. Planejamento da força de trabalho, cargos, carreiras e remuneração": 15
- "3. Recrutamento, seleção, integração e indicadores da função de RH": 18
- "4. Desempenho, competências, aprendizagem e desenvolvimento público": 31
- "5. Cultura, clima, bem-estar e comportamento organizacional": 13

Já existem (não repita):
- Uma organização divulga oportunidades, define metas dos novos integrantes, oferece capacit
- Uma área de pessoal deixa de planejar cursos isolados e passa a relacionar competências, d
- Uma equipe reconhece que oferece esforço, competências e colaboração, mas avalia que oport
- Uma unidade estima pessoas necessárias a partir de quantidade de entregas, tempo de execuç
- Uma ficha lista tarefas realizadas por uma função, enquanto outro documento indica requisi
- Uma organização pretende manter pagamento-base previsível e oferecer parcela adicional qua
- Para preencher uma função, a organização divulga a oportunidade apenas a pessoas já vincul
- Uma prova de digitação entrega notas muito semelhantes em aplicações repetidas, mas é util
- Uma equipe executa atendimento complexo e a área de RH precisa entender decisões pouco vis
- Uma organização cria benefício de transporte e reconhecimento público a equipes, além de s
- Uma equipe de RH necessita comparar o valor relativo de dezenas de cargos, considerando co
- Uma organização oferece trilha técnica e possibilidade de movimentação lateral para amplia
- Uma unidade registrou 18 desligamentos em um ano e efetivo médio de 300 pessoas. Considera
- Para selecionar candidatos, uma equipe estabelece perguntas iguais, critérios observáveis 
- Participantes de uma formação relatam satisfação elevada imediatamente após o evento. O ge
- Uma unidade federal planeja ações de desenvolvimento para o próximo exercício a partir de 
- Uma unidade precisa produzir 1.560 análises mensais; cada análise exige, em média, 1,5 hor
- Uma atividade alterna períodos de análise, atendimento e ocorrências raras ao longo das se
- Uma organização pretende recompensar quantidade de atendimentos, mas observa risco de que 
- Uma estratégia institucional exige colaboração entre equipes. Contudo, o recrutamento priv
- Uma equipe está atualizando o PDP e lê o art. 4º do Decreto nº 9.991/2019 após a alteração
- Um gestor pergunta pelo marco de encaminhamento da proposta anual de PDP e pelo início do 
- Uma pessoa valoriza muito prêmio oferecido, mas não acredita que esforço adicional possa e
- Em avaliação 360 graus, um profissional muito comunicativo recebe notas máximas também par
- Uma organização deseja preservar saber prático de especialistas próximos do desligamento e
- Após contratar uma equipe, a unidade de RH reúne dados de rotatividade, ausências e partic
- Uma organização desenvolve gestão estratégica de pessoas e, por isso, deve abandonar regis
- Uma descrição de cargo apresenta tarefas e entregas, enquanto a análise correspondente ide
- Uma organização anuncia vagas a pessoas do mercado e realiza entrevistas para comparar can
- Uma pesquisa trimestral identifica percepção compartilhada de baixa confiança na chefia. E
- Após um curso, os participantes dizem ter gostado do conteúdo. Isso demonstra, por si só, 
- No planejamento federal de desenvolvimento, o PDP é elaborado anualmente para o exercício 
- Uma organização combina parcela-base e bônus por desempenho. Como existem duas parcelas fi
- Uma organização compara cargos segundo fatores compensáveis e pesos estabelecidos; o proce
- Uma pessoa é movida lateralmente para outro conjunto de atividades, sem ascensão de nível 
- Uma seleção combina entrevista estruturada e amostra prática de trabalho, com critérios li
- Uma análise encontra que unidades com maior número de treinamentos apresentam também maior
- Uma equipe sabe aplicar uma técnica individualmente, mas fracassa em entregas conjuntas po
- Uma chefia reúne avaliação anual dos resultados, sem pactuar metas, acompanhar condições o
- Uma unidade planeja produzir 900 entregas mensais, cada qual com esforço médio de 2 horas,
- O Decreto nº 9.991/2019 obriga, sem exceção, que o diagnóstico de competências esteja conc
- Uma política salarial usa pontos por fatores para avaliar o valor relativo dos cargos, con
- Um painel registra 120 horas de ausência em 6.000 horas previstas e conclui absenteísmo de
- Uma especialista sem autoridade formal influencia colegas porque domina uma ferramenta dif
- Uma unidade pública organiza ações da PNDP e conclui que o art. 4º do Decreto nº 9.991/201
```

## Pedido 24: Redação oficial (`portugues.redacao-oficial`, 1311 tópicos de edital)

```
Matéria: portugues.redacao-oficial (Redação oficial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, finalidade e atributos da comunicação oficial": 26
- "2. Padrão ofício, formatação, estrutura e fechos": 18
- "3. Tratamento, endereçamento, vocativo e Decreto nº 9.758/2019": 15
- "4. Gêneros documentais, funções, minutas e comunicações eletrônicas": 21
- "5. Linguagem simples, acessibilidade, adequação e revisão aplicada": 20

Já existem (não repita):
- Uma minuta diz: 'Eu considero lamentável a desatenção demonstrada pelo destinatário'. A re
- Uma comunicação interna entre duas unidades é preparada conforme a terceira edição do Manu
- Uma pessoa redige ofício de solicitação a destinatário de mesma hierarquia. Considerando a
- Um setor elabora ofício dirigido a agente federal abrangido pelo Decreto nº 9.758/2019. Qu
- Uma comissão quer registrar participantes, assuntos discutidos e deliberações de reunião, 
- Uma mensagem institucional pede o envio de uma planilha. O assunto atual é 'IMPORTANTE!!!'
- Uma correspondência será enviada com texto idêntico a quatro unidades destinatárias, por u
- Uma equipe substitui expressões técnicas dispensáveis por palavras comuns, preserva os req
- Uma comunicação deve certificar dados extraídos de registro oficial anterior. Qual documen
- Ao revisar um ofício datado do primeiro dia de um mês, uma pessoa procura a linha correspo
- Um ofício no padrão do Manual possui quatro parágrafos de texto, além do vocativo e do fec
- Uma minuta federal traz nome pessoal no endereçamento, porque só a indicação de função e s
- Um documento é preparado conjuntamente por três unidades emissoras para apenas uma unidade
- Uma ata foi redigida com lista de presentes e registro das deliberações, mas encerra sem r
- Uma mensagem institucional informa que 'proceder-se-á à efetivação da remessa dos document
- Ao examinar um e-mail oficial com planilha anexa, uma revisora encontra apenas o corpo 'Se
- Uma ação de capacitação institucional ensina equipes a produzir textos diretos e acessívei
- Uma unidade envia correspondência a agente federal e cita uma função institucional que ide
- Uma comunicação entre agente federal e integrante de outra esfera institucional está sujei
- Um expediente de encaminhamento traz apenas a cópia de um relatório requisitado anteriorme
- Uma comunicação em papel pelo padrão ofício possui três páginas. Na primeira, o responsáve
- Uma redação propõe que as três antigas espécies aviso, memorando e ofício ainda possuem di
- Uma pessoa redige despacho de encaminhamento, enquanto outra elabora parecer solicitado so
- Uma proposta de simplificação altera 'O pedido foi indeferido porque faltou o documento X;
- Uma instituição pretende enviar a mesma comunicação preparada conjuntamente por duas unida
- Em uma comunicação oficial, o uso de 'Informo' ou 'Solicito' pode ser compatível com impes
- Pela terceira edição do Manual de Redação, toda mensagem interna entre unidades administra
- O Decreto nº 9.758/2019 adotou 'senhor' como pronome de tratamento nas comunicações abrang
- Se uma pessoa solicita formalmente uma providência, o documento adequado é necessariamente
- A identificação de ofício circular indica, conforme o Manual, o envio do mesmo expediente 
- A linguagem simples é incompatível com textos institucionais porque exige eliminar fundame
- Em ofício com três parágrafos de texto, a numeração inicia no primeiro parágrafo, sem nume
- No âmbito do Decreto nº 9.758/2019, o nome do destinatário é proibido no endereçamento mes
- Um e-mail oficial com arquivo anexo deve indicar minimamente do que trata o arquivo, e seu
- A expressão 'Att.' constitui um dos dois fechos oficiais definidos pelo Manual, sendo dest
- A terceira edição do Manual, ao mencionar exposição de motivos, reserva a essa espécie a a
- O Pacto Nacional do Judiciário pela Linguagem Simples se organiza apenas em dois eixos, si
- Os vocativos 'Senhora,' e 'Senhor responsável,' devem ser seguidos de vírgula na comunicaç
- Por ser posterior ao Manual de 2018, o Decreto nº 9.758/2019 revogou o uso dos fechos 'Ate
- Em comunicação oficial destinada a agente federal abrangido, o erro cometido por um interl
- Como o art. 4º do Decreto nº 9.758/2019 prevê endereçamento sem nome pessoal como regra, u
- Quando dois emissores enviam conjuntamente expediente idêntico a diversos destinatários, a
- A ênfase em linguagem simples exige converter todas as referências legais em expressões ge
- O Manual de 2018 pode ser utilizado para a estrutura do padrão ofício, enquanto o tratamen
- A terceira edição do Manual estabelece para ata, atestado, certidão, requerimento, relatór
```

## Pedido 25: Banco de dados e SQL (`informatica.banco-dados-sql`, 1302 tópicos de edital)

```
Matéria: informatica.banco-dados-sql (Banco de dados e SQL)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, SGBD, arquitetura e modelos de dados": 21
- "2. Modelagem ER, integridade, mapeamento e normalização": 27
- "3. SQL: álgebra relacional, DDL, DML e consultas": 25
- "4. Transações, segurança, views, índices e programação no banco": 15
- "5. Modelos analíticos, data warehouse, data lake e aplicação integrada": 12

Já existem (não repita):
- Uma aplicação armazena registros de pedidos em tabelas e utiliza um programa que verifica 
- Um SGBD oferece a usuários distintos apresentações diferentes dos mesmos dados globais, oc
- No cadastro de clientes, a coluna id identifica cada linha sem repetição e não admite valo
- Uma pessoa pode realizar diversos pedidos, e cada pedido pertence a uma única pessoa. No m
- Um desenvolvedor pretende acrescentar a coluna data_entrega à tabela Pedido sem eliminar o
- Uma tabela de pagamentos tem cinco linhas, mas duas possuem valor NULL na coluna comprovan
- Uma transferência subtrai cinquenta unidades de uma conta e adiciona cinquenta a outra. A 
- Uma equipe precisa autorizar leitura da tabela Cliente ao papel analista, sem permitir mud
- Uma organização mantém usuários e cursos, permitindo que cada usuário se inscreva em vário
- A relação Item(pedido_id, produto_id, nome_produto, quantidade) tem chave composta por ped
- Em tabela Funcionario(id, setor_id, nome_setor), cada id define setor_id e cada setor_id d
- Uma relação Pedido tem linhas (1, 'A', 200), (2, 'A', 400) e (3, 'B', 900), com colunas id
- Há três clientes, IDs 1, 2 e 3, e somente o cliente 1 realizou pedido. É necessário listar
- Uma coluna tem restrição CHECK (preco > 0), sem NOT NULL. Ao inserir preco=NULL em Postgre
- Uma equipe consulta frequentemente o total de vendas por dia, mas tolera resultado defasad
- Um sistema recebe registros JSON brutos e os carrega no destino antes de executar limpeza 
- Considere Cliente com IDs 1 e 2. Pedido possui linha (id=10, cliente_id=1, status='pago').
- Uma consulta usa WHERE codigo NOT IN (SELECT codigo FROM Bloqueios), e a subconsulta retor
- Uma tabela Itens referencia Pedido(id) com ON DELETE CASCADE. Existem dois itens que apont
- Duas transações concorrentes executam consultas sob o nível READ UNCOMMITTED de PostgreSQL
- Na álgebra relacional clássica, uma operação seleciona apenas os atributos nome e cidade d
- Uma pessoa recebe SELECT sobre uma tabela diretamente e também pela associação a um papel 
- Um sistema registra alterações de dados por uma rotina executada em cada UPDATE de tabela,
- Um projeto analítico registra cada item vendido com quantidade, valor, data_id, produto_id
- Uma empresa guarda logs, imagens, JSON e tabelas brutas para análises exploratórias futura
- Uma pessoa acrescenta índice à tabela sem mudar seus atributos ou as consultas existentes.
- Na modelagem de inscrição, uma associação muitos-para-muitos entre Usuario e Curso é norma
- Ao criar tabela com id INTEGER PRIMARY KEY, um projeto impõe unicidade e proibição de NULL
- Em um cadastro SQL, o comando DELETE FROM Cliente WHERE id=7 remove a definição da tabela 
- Em consultas SQL, ORDER BY salario DESC especifica ordenação decrescente do resultado, ao 
- Em transação com dois UPDATE ainda não confirmados, a execução de COMMIT serve para cancel
- Uma consulta baseada em SELECT DISTINCT cidade FROM Cliente elimina repetições na lista de
- Na arquitetura de três esquemas, o nível interno descreve as visões particulares de cada g
- No modelo conceitual, uma notação com círculo indicando participação mínima zero em um rel
- Uma tabela de relações profissionais usa chave candidata composta e há atributo não primo 
- Uma consulta com SUM(valor) e GROUP BY categoria pode empregar HAVING SUM(valor)>1000 para
- Se um usuário perdeu privilégio SELECT diretamente concedido por REVOKE, então também perd
- Uma equipe pode adotar índice para acelerar certas consultas, embora a estrutura exija esp
- Na análise de vendas por ano, região e produto, uma tabela dimensão deve conter as medidas
- Em um modelo relacional com chave primária composta por A e B, a restrição PRIMARY KEY (A,
- No PostgreSQL, ao executar uma transação em READ UNCOMMITTED, leituras sujas são necessari
- Uma consulta SELECT x FROM T pode apresentar valores repetidos em SQL mesmo que a projeção
- Em PostgreSQL, a declaração de FOREIGN KEY na coluna cliente_id cria obrigatoriamente, no 
- No projeto de warehouse, definir que cada linha da tabela fato corresponde a um item indiv
- Um fluxo ETL carrega no destino os dados brutos antes de qualquer transformação, enquanto 
```

## Pedido 26: Windows 10 e 11: recursos básicos (`informatica.windows-10-11`, 1294 tópicos de edital)

```
Matéria: informatica.windows-10-11 (Windows 10 e 11: recursos básicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos do Windows, área de trabalho, janelas e multitarefa": 20
- "2. Explorador de Arquivos: caminhos, tipos, cópia, movimentação e exclusão": 24
- "3. Configurações, Painel de Controle, tela, aparência e dispositivos": 19
- "4. Instalação e remoção de programas e principais utilitários": 19
- "5. Contas, permissões básicas, energia, recuperação e arquiteturas de 32 e 64 bits": 18

Já existem (não repita):
- Uma pessoa precisa abrir rapidamente as pastas do computador no Windows 11 sem navegar pel
- Durante a preparação de um relatório, há três janelas abertas e o usuário quer alternar ra
- Uma usuária quer duplicar um documento da pasta Projetos para a pasta Arquivo, mantendo o 
- Uma pasta contém `foto.jpg`; o usuário altera seu nome para `foto.pdf`, sem usar conversor
- No Windows 11, uma pessoa precisa modificar resolução da tela e escala dos aplicativos. Qu
- Um arquivo foi excluído da pasta Documentos por Delete em unidade local, com as configuraç
- Um estudante precisa capturar uma região da tela para inserir em suas anotações no Windows
- Um aplicativo instalado está travado, sem responder ao mouse. Qual ferramenta do Windows p
- Após sair por poucos minutos, a pessoa quer impedir acesso de terceiros sem encerrar os ap
- O usuário do Windows 11 deseja remover um programa tradicional instalado no computador sem
- No Explorador do Windows 11, um usuário não consegue distinguir `relatorio.pdf` de `relato
- Ao arrastar um arquivo entre duas pastas de unidades locais diferentes, sem teclas modific
- O texto de um notebook parece minúsculo, mas a resolução já está na opção nativa recomenda
- Um computador mostra duas impressoras instaladas e sempre seleciona a errada para trabalho
- Após desabilitar um app em Aplicativos de inicialização no Gerenciador de Tarefas, o usuár
- Uma pessoa vê mensagem de erro após instalar um programa e encontra opções Reparar e Redef
- Um usuário precisa distinguir software de proteção contra malware daquele que controla com
- Um notebook será deixado sem uso por várias horas e dispõe de opção Hibernar. Qual caracte
- Uma pessoa perdeu uma fotografia pessoal e escolhe ponto de restauração do sistema acredit
- Um computador possui Windows 10 de 32 bits em processador x64, e o usuário deseja utilizar
- Um atalho para arquivo em pasta protegida aparece no desktop, mas uma conta padrão recebe 
- Uma pasta de projeto foi fixada em Acesso Rápido; em seguida, o usuário desafixou o item e
- Um funcionário habilita protetor de tela com imagens e acredita que isso produz o mesmo es
- Um aplicativo suspeito apareceu em download; a pessoa abre Gerenciador de Tarefas para 've
- Um programa antigo de 32 bits funcionava no Windows 10 x64. Ao migrar para Windows 11 x64,
- No Windows 10 e 11, pressionar Win+I abre o aplicativo Configurações, enquanto Win+E abre 
- Ao minimizar um aplicativo no Windows, sua instalação é removida e seus arquivos de progra
- Em pastas locais do Windows, a sequência Ctrl+C e Ctrl+V pode copiar arquivo para outra pa
- Na interface Windows, pressionar F2 sobre um arquivo selecionado o transforma no tipo de a
- A resolução de exibição corresponde às dimensões em pixels e é ajustável nas configurações
- No Windows 11, desabilitar um aplicativo na seção de inicialização do Gerenciador de Taref
- O Windows 10 dispõe de Configurações e de Painel de Controle, e o Windows 11 também mantém
- O Microsoft Defender Antivírus e o firewall desempenham a mesma função: ambos substituem a
- O usuário pode configurar protetor de tela para exibir imagens após inatividade, sem que i
- O Windows 11 possui edição nativa de 32 bits para computadores antigos, enquanto as versõe
- Em uma pasta de unidade NTFS, conta padrão visualiza o nome de um arquivo, mas recebe 'Ace
- Um documento armazenado em pasta sincronizada com a nuvem é excluído localmente; essa ação
- O ponto de restauração do Windows pode reverter determinadas configurações e componentes d
- Depois de instalar programa tradicional, o usuário o encontra em Aplicativos instalados. E
- No Windows 11, a combinação Win+Tab pode mostrar áreas de trabalho virtuais, que permitem 
- No Windows, escolher Finalizar tarefa para um editor travado preserva necessariamente todo
- A presença da opção 'Sempre manter neste dispositivo' em arquivo sincronizado pode indicar
- No Windows, entrar no Modo de Segurança equivale a formatar a unidade do sistema e reinsta
- No Windows 10, um processador com suporte a 64 bits pode estar executando instalação do si
- O término do suporte regular do Windows 10 em 14 de outubro de 2025 provocou a desativação
```

## Pedido 27: Direito das obrigações (`direito-civil.obrigacoes`, 1286 tópicos de edital)

```
Matéria: direito-civil.obrigacoes (Direito das obrigações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura da obrigação e suas classificações": 22
- "2. Modalidades da prestação, pluralidade e solidariedade": 22
- "3. Transmissão, pagamento e modos especiais de extinção": 21
- "4. Inadimplemento, cláusula penal e concurso de credores": 22
- "5. Atos unilaterais e aplicação integrada das obrigações": 13

Já existem (não repita):
- Após a prescrição da pretensão relativa a empréstimo particular, Mauro pagou espontaneamen
- Uma declaração prevê entrega de computador se o projeto for aprovado; outra fixa entrega e
- Beatriz possui crédito reconhecido contra um prestador, mas o montante depende de apuração
- Antes da tradição de um piano individualizado, um raio destruiu integralmente o instrument
- Um fornecedor prometeu entregar 200 caixas de papel de qualidade média, sem individualizaç
- Uma oficina comprometeu-se a desobstruir encanamento, serviço executável por terceiros. Di
- Em contrato, o devedor prometeu entregar uma impressora ou um scanner, sem prever a titula
- Três pessoas assinam documento comprometendo-se a pagar quantia divisível em partes iguais
- Uma credora cede o crédito a terceiro. O devedor, sem ter sido notificado nem conhecer a c
- Uma pessoa propõe assumir integralmente dívida alheia, liberando o antigo devedor. Notific
- Caio devia entregar 30 sacas de arroz, mas ofereceu em substituição produto agrícola de va
- Helena oferece integralmente a quantia vencida no local acertado, mas o credor recusa sem 
- Fiador paga dívida pela qual era responsável e busca exercer direitos que pertenciam ao cr
- Partes substituem expressamente débito de fornecimento por nova obrigação de prestar servi
- Dois comerciantes devem reciprocamente R$ 12 mil e R$ 9 mil, quantias vencidas, líquidas e
- Um devedor deveria pagar obrigação positiva e líquida em data determinada; não pagou, sem 
- Sem cláusula sobre índice de atualização ou taxa de juros e sem lei específica para dívida
- Cláusula penal de R$ 140 mil foi estipulada para obrigação principal de R$ 100 mil. O deve
- Um trabalhador realizou a colheita e não recebeu o salário; outra pessoa forneceu insumos 
- Em concurso civil, há crédito hipotecário regularmente constituído, crédito com privilégio
- Dois credores com privilégios especiais da mesma classe concorrem sobre os mesmos bens. Se
- Uma pessoa anuncia publicamente recompensa para quem localizar um documento perdido. Um vi
- Durante ausência da proprietária, um vizinho paga reparo urgente de telhado para evitar de
- Por erro demonstrável, uma pessoa paga a terceiro quantia devida a outro destinatário. O r
- Após receber acréscimo patrimonial sem causa, uma pessoa é demandada diretamente por enriq
- Em prestação de serviços que exige atuação diligente, o resultado desejado não foi alcança
- Mesmo tendo pago voluntariamente dívida prescrita, o devedor pode exigir a restituição, po
- Cedida regularmente dívida remunerada por juros e sem pacto de exclusão de acessórios, a t
- Em obrigação de restituir uma joia emprestada, a perda fortuita sem culpa do possuidor ant
- Se um devedor se obrigou apenas a entregar um quadro específico, mas pode optar por substi
- Uma obrigação em dinheiro dividida entre quatro devedores torna-se solidária pelo simples 
- Uma liberalidade impõe ao beneficiário conservar uma biblioteca recebida; sem disposição e
- Embora a prestação seja divisível, o credor de pagamento integral é obrigado a receber met
- Sem cláusula sobre local de pagamento, a obrigação monetária deve ser cumprida, em regra, 
- Quando terceiro assume dívida alheia com intuito de liberar o devedor original, basta o as
- No pagamento em parcelas periódicas, a quitação da última gera, até prova em contrário, pr
- A quitação do principal de uma dívida, sem ressalva sobre os juros, presume que os juros p
- Se duas prestações alternativas são periódicas e nada se pactuou de diferente, a faculdade
- Um credor com privilégio geral sobre todos os bens do devedor pode ignorar garantia real v
- Bem hipotecado foi destruído e substituído por indenização securitária; o direito preferen
- Cláusula penal estipulada por inadimplemento obriga o credor a provar dano efetivo para ex
- Um devedor já estava em mora quando a coisa pereceu por caso fortuito; sua responsabilidad
- Uma recompensa pública com prazo expresso para execução pode ser livremente revogada duran
- Uma vantagem patrimonial originalmente justificada por causa jurídica deve ser restituída 
- Vizinho que administra utilmente negócio alheio sem autorização adquire automaticamente a 
```

## Pedido 28: Arquivos digitais, transferência e backup (`informatica.arquivos-digitais-backup`, 1284 tópicos de edital)

```
Matéria: informatica.arquivos-digitais-backup (Arquivos digitais, transferência e backup)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Arquivos digitais: estrutura, formatos e identificação": 22
- "2. PDF, organização de arquivos e métodos de acesso": 22
- "3. Transferência de arquivos: upload, download, protocolos e velocidades": 27
- "4. Backup completo, incremental, diferencial e recuperação": 15
- "5. RPO, RTO, restauração segura e cenários integrados": 14

Já existem (não repita):
- Um usuário altera o nome de `ata.txt` para `ata.pdf` sem abrir ou converter o conteúdo do 
- Uma equipe necessita de formato para registrar tabelas e fórmulas em documento editável de
- Uma fotografia será exportada com pixels e transparência, preservando os valores de imagem
- Uma pessoa abre PDF digitalizado e percebe que consegue ver a página, mas não localizar pa
- Um programa precisa processar linha a linha um log de eventos do início ao fim, mantendo a
- Um usuário envia pelo navegador um arquivo salvo em seu computador para serviço remoto de 
- Uma conexão apresenta taxa efetiva de 8 megabits por segundo. Em unidades decimais, a taxa
- No domingo foi feito backup completo, e de segunda a quarta foram gravadas apenas alteraçõ
- Um serviço aceita perda de até duas horas de alterações, mas precisa voltar a funcionar em
- Um servidor de arquivos web devolve o documento com cabeçalho `Content-Type: application/p
- Uma coleção precisa preservar documentos digitais por longo prazo; outro projeto tem como 
- Uma aplicação encontra rapidamente registro de matrícula consultando tabela que aponta cha
- Uma equipe precisa diferenciar protocolos ao publicar arquivos em servidor. Qual afirmação
- Um arquivo de 100 MB decimais será baixado a uma taxa efetiva constante de 20 Mbps. Descon
- Há backup completo no domingo e diferenciais de segunda, terça e quarta. Todos estão ínteg
- Em um esquema com completo no domingo e incrementais diários de segunda, terça e quarta, u
- A pasta de trabalho é sincronizada automaticamente entre computador e nuvem e não há reten
- Um arquivo recebido como `relatorio.pdf` vem declarado com tipo HTTP `application/pdf`, ma
- Em um sistema Unix, o diretório atual é `/dados/2026/relatorios` e um processo acessa `../
- Um cliente interrompeu o recebimento de arquivo e solicita ao servidor HTTP `Range: bytes=
- Um arquivo tem exatamente 1 GiB e é transferido à taxa efetiva constante de 8 MiB/s. Sem s
- Uma política faz completo no domingo. O arquivo A muda segunda e de novo terça; B muda só 
- Uma sequência incremental contém completo de domingo e incrementais de segunda, terça e qu
- Uma organização mantém original e duas cópias em duas mídias, mas todos os volumes ficam m
- Um sistema possui cópias consistentes a cada 15 minutos, mas seu teste de restauração leva
- Alterar manualmente a extensão de um arquivo não garante que os bytes nele contidos passem
- Arquivos PNG e JPEG são equivalentes quanto à compressão, pois ambos utilizam necessariame
- Um PDF produzido por digitalização pode exibir páginas legíveis visualmente e, ainda assim
- Se um programa realiza upload de arquivo pelo navegador, o arquivo está obrigatoriamente s
- No backup diferencial, cada execução guarda as alterações ocorridas desde o último backup 
- RPO representa o tempo máximo para colocar um serviço novamente em operação, enquanto RTO 
- O cabeçalho HTTP Content-Type descreve o tipo de mídia associado à representação, mas sua 
- A conformidade com PDF/A satisfaz automaticamente todos os requisitos de acessibilidade pr
- Se o sistema conhece o deslocamento físico de um registro de tamanho fixo, pode acessá-lo 
- FTPS e SFTP diferem apenas na extensão do arquivo transmitido: ambos correspondem ao proto
- Em condições ideais, 250 MB decimais transferidos a 50 Mbps constantes exigem aproximadame
- No backup incremental, cada cópia contém todas as alterações feitas desde o último backup 
- Manter cópia de dados em meio desconectado pode reduzir a exposição a ransomware que procu
- Um PDF assinado apenas com a imagem gráfica de uma rubrica oferece, pela simples presença 
- Quando o servidor aceita uma requisição HTTP Range válida, pode responder com 206 Partial 
- Se o incremental de terça foi perdido, a posse do incremental de quarta garante a restaura
- Uma política com RPO de uma hora pode exigir pontos recuperáveis ao menos tão recentes qua
- O hash SHA-256 de um arquivo, quando coincide com valor fornecido por fonte não autenticad
- Uma cópia de segurança criptografada pode estar íntegra e, mesmo assim, ser inutilizável p
- Um arquivo SVG passa a armazenar obrigatoriamente apenas pixels quando ampliado na tela; p
```

## Pedido 29: Licenciamento, EIA/RIMA e responsabilidade ambiental (`direito-ambiental.licenciamento-responsabilidade`, 1265 tópicos de edital)

```
Matéria: direito-ambiental.licenciamento-responsabilidade (Licenciamento, EIA/RIMA e responsabilidade ambiental)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Licenciamento ambiental e modalidades de licença": 17
- "2. Competência federativa, estudos e participação no licenciamento": 24
- "3. Dano ambiental, reparação civil e obrigações reais": 22
- "4. Responsabilidade administrativa, penal e independência das esferas": 20
- "5. Compensação ambiental, obras públicas, EIA e casos integrados": 17

Já existem (não repita):
- Uma empresa prepara empreendimento ainda na escolha de localização e precisa obter a licen
- O projeto possui LP válida e necessita implantar edificações e sistemas de controle ambien
- Uma atividade potencialmente causadora de significativa degradação é submetida a licenciam
- O parecerista deve definir a finalidade do Rima elaborado a partir de EIA complexo. Qual i
- Um consórcio afirma que, por atingir dois municípios, sua atividade deve receber simultane
- Uma área ecológica foi contaminada e necessita recuperação. Qual regra determina o dever c
- Uma ação de degradação ambiental discute obtenção de provas técnicas em poder do empreende
- Após acidente ambiental, o órgão lavra auto por infração e a empresa sustenta que reparar 
- Uma LP é expedida conforme a Lei Geral de Licenciamento. Qual intervalo legal básico de va
- Uma empresa protocola renovação da LO 140 dias antes de expirar sua validade e aguarda dec
- Uma empresa pequena, de baixo potencial poluidor, apresenta RCE e assume compromissos para
- Uma atividade funciona sem licença e pede regularização segundo os requisitos e limites da
- Um município solicita apoio técnico estadual para analisar laudo complexo, permanecendo co
- Uma LP depende de EIA, e o estudo completo foi protocolado com documentos requeridos. Qual
- Um processo de licenciamento sujeito a EIA terá audiência pública antes da LP. Com que ant
- Uma audiência reúne moradores para questionar impactos de nova obra. O empreendedor afirma
- Uma área costeira degradada perde biodiversidade, e pescadores também sofrem perda comprov
- O titular de terreno adquiriu área já desmatada e alega boa-fé para recusar obrigação de r
- Uma pretensão busca restauração civil de ecossistema lesado há várias décadas. Qual tese d
- Uma multa é imposta a sociedade apenas por ser proprietária da mercadoria que terceiro der
- Empresa participa de licitação para obra cujo projeto básico deve considerar impactos ambi
- No licenciamento de empreendimento de significativo impacto fundado em EIA/Rima, a autorid
- Um alienante vendeu imóvel antes de ocorrer novo dano ecológico e não contribuiu de modo d
- Uma pessoa jurídica é processada por crime ambiental decorrente de decisão de seu órgão co
- O órgão estadual licencia empreendimento, mas fiscalização de outro ente identifica poluiç
- Uma empresa recebeu LP que atestou a viabilidade de localização; para iniciar a instalação
- Uma empresa protocola pedido inicial de licença e, vencido o prazo máximo de análise sem d
- Uma atividade potencialmente causadora de significativa degradação depende de EIA prévio, 
- Se houver competência legislativa concorrente de União e estados em matéria ambiental, cad
- A contaminação de estuário pode causar simultaneamente dano ecológico à biota e prejuízo p
- Em demanda de reparação civil por dano ambiental, a vítima ou legitimado deve provar neces
- Uma indústria provoca degradação e tem de recompor o ambiente, embora também possa respond
- Um município pede equipe estadual para auxiliá-lo em análise de EIA, mantendo a emissão da
- Uma LP com EIA completo tem prazo máximo geral de análise de dez meses, segundo art. 47 da
- No procedimento de LAC, qualquer empreendimento de alto potencial poluidor pode ser admiti
- Em processo que exige EIA, a Lei 15.190/2025 prevê pelo menos uma audiência pública antes 
- Um empreendimento causa dano significativo, porém seu empreendedor já pagou compensação do
- Uma obra pública submetida à Lei 14.133/2021 pode ter edital prevendo que o contratado obt
- Como o licenciamento é atribuído a um ente único pela LC 140, nenhum outro ente pode exerc
- Uma pessoa vendeu imóvel antes de novo desmatamento cometido por terceiro, sem qualquer co
- Uma ação de reparação civil ambiental pode ser rejeitada somente porque o dano ocorreu há 
- Uma equipe ambiental demonstra que a atividade causou dano ecológico e promove restauração
- A Lei 9.605/1998 permite impor pena privativa de liberdade à pessoa jurídica por crime amb
- No empreendimento de significativo impacto, a compensação do art. 36 da Lei 9.985/2000 dep
- Uma obra licitada tem cláusula atribuindo ao contratado a tarefa de obter licenciamento. P
```

## Pedido 30: Fatos e negócios jurídicos: forma, prova e defeitos (`direito-civil.negocio-juridico`, 1258 tópicos de edital)

```
Matéria: direito-civil.negocio-juridico (Fatos e negócios jurídicos: forma, prova e defeitos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fatos, atos, planos do negócio, forma e interpretação": 27
- "2. Representação, condição, termo e encargo": 19
- "3. Defeitos da vontade: erro, dolo, coação, estado de perigo e lesão": 13
- "4. Fraude contra credores, simulação e regimes de invalidade": 20
- "5. Prova do fato jurídico, ilicitude, abuso de direito e casos integrados": 21

Já existem (não repita):
- Duas pessoas celebram negócio de venda de bem permitido, por agentes capazes e usando form
- Duas pessoas celebram negócio voltado à transferência de direito real sobre imóvel de valo
- Uma empresa envia proposta e a outra não responde, sem existir prática anterior, uso comer
- Em procuração válida, o representante contrata consigo mesmo sem autorização do interessad
- Uma doação foi condicionada à aprovação do beneficiário em exame futuro cujo resultado é i
- Uma parte indica uma soma errada em documento de compra, mas objeto, preço unitário e quan
- Uma pessoa promete doar equipamento se o beneficiário assumir a obrigação de mantê-lo em f
- Um fornecedor engana intencionalmente consumidor quanto à natureza essencial de objeto, le
- Uma pessoa é ameaçada com dano físico grave e assina um negócio por medo fundado de agress
- O representante vende bem do representado em benefício próprio, em conflito que o adquiren
- Uma doação sujeita-se à condição suspensiva fisicamente impossível. Em outra, evento igual
- Uma cláusula fixa que o credor só poderá exigir prestação a partir de 1º de fevereiro, dat
- O comprador teria adquirido o produto mesmo conhecendo determinada circunstância, mas obte
- Uma pessoa precisa de auxílio imediato para salvar familiar de grave dano; a contraparte c
- Uma parte inexperiente se compromete a prestação manifestamente desproporcional. A favorec
- Um devedor já insolvente transfere gratuitamente bem a terceiro. Credor cujo crédito exist
- Devedor insolvente vende bem por preço oneroso a pessoa que sabia, ou tinha motivos objeti
- Partes formalizam venda fictícia, mas pretendem ocultar doação verdadeira, sem vício própr
- Credor tenta anular, em embargos de terceiro, transmissão onerosa por fraude contra credor
- Uma pessoa contrata sob ameaça grave que persiste por seis meses após a assinatura. Para a
- Um negócio com objeto ilícito é declarado nulo. Décadas depois, as partes alegam que a exe
- Uma pessoa apresenta arquivo eletrônico de conversa comercial. A outra parte não impugna a
- Uma pessoa de dezessete anos declara-se maior ao assumir obrigação e pretende depois usar 
- Uma pessoa detém direito de exigir uso de propriedade, mas o exerce manifestamente contra 
- Negócio anulável por dolo foi voluntariamente cumprido em parte pelo devedor, que conhecia
- Duas pessoas ajustam contrato verbal de serviço para o qual não existe imposição de forma 
- Uma pessoa faz declaração negocial sem real intenção interna de cumpri-la, mas o destinatá
- Uma doação prevê que determinado benefício cessará se ocorrer acontecimento futuro incerto
- Uma pessoa recebe bem por liberalidade com encargo de conservar biblioteca. Sem cláusula q
- Uma pessoa assina contrato por erro de soma claramente identificável, enquanto os demais t
- Um vendedor engana a outra parte sobre detalhe que apenas modificaria as condições econômi
- Representante voluntário age nos limites dos poderes recebidos, firmando declaração perant
- Quando a cláusula resolutiva de um negócio é fisicamente impossível, o art. 123 invalida n
- Uma condição suspensiva beneficiaria uma parte que, de forma maliciosa, fez ocorrer artifi
- Uma parte ameaça exercer regularmente ação judicial para exigir dívida vencida. Pelo art. 
- Uma pessoa sem experiência assume obrigação manifestamente desproporcional ao contratar; s
- Uma pessoa torna-se credora somente depois que o devedor insolvente realizou doação lesiva
- Partes fazem escritura de compra e venda apenas aparente para ocultar negócio diferente. O
- Um negócio foi celebrado por pessoa absolutamente incapaz e o juiz constata a nulidade pro
- Representante concluiu negócio em conflito de interesses com o representado, conhecido pel
- Uma pessoa pede anulação por coação quando transcorreram mais de quatro anos desde a assin
- Uma parte exibe gravação eletrônica de conversa com conteúdo relevante e a outra não impug
- Em embargos de terceiro, parte interessada pede anulação de transferência por fraude contr
- O proprietário possui formalmente direito de uso, mas o exerce de maneira manifestamente c
- Um instrumento particular destinado a negócio sujeito obrigatoriamente à escritura pública
```

## Pedido 31: Hardware: conceitos básicos (`informatica.hardware-conceitos-basicos`, 1250 tópicos de edital)

```
Matéria: informatica.hardware-conceitos-basicos (Hardware: conceitos básicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Hardware, software e arquitetura funcional do computador": 16
- "2. Placa-mãe, processador, memória e compatibilidade": 16
- "3. Armazenamento secundário: HDD, SSD, mídias ópticas e RAID": 17
- "4. Periféricos, barramentos e subsistema de entrada e saída": 36
- "5. Compatibilidade, montagem básica, manutenção e diagnóstico": 15

Já existem (não repita):
- Uma pessoa digitaliza um formulário, edita seu conteúdo e grava o resultado para uso poste
- Num modelo didático da CPU, uma instrução foi buscada na memória e precisa ser decodificad
- Após desligamento inesperado, dados que estavam apenas na área de trabalho de um programa 
- Uma unidade de gravação aceita um DVD-R convencional de uma face e camada. Qual capacidade
- Um computador sofre desligamentos durante tarefas intensas e apresenta saídas de ar cobert
- Uma estação possui tela que exibe imagem e recebe comandos pelo toque do usuário. Como ess
- Um processador foi anunciado como tendo quatro núcleos físicos e oito threads lógicas. Qua
- Um driver consulta repetidamente um registrador de status para saber se o periférico termi
- Uma organização pretende aumentar a velocidade de acesso a arquivos e avalia um SSD em com
- Num computador didático com 16 linhas independentes de endereço, cada endereço identifica 
- Uma unidade óptica será utilizada para produzir várias versões de um arquivo no mesmo DVD,
- Durante leitura de um grande bloco de dados, a controladora de armazenamento transfere o c
- Um computador suporta módulos DDR4, e o usuário adquiriu módulo DDR5 de capacidade semelha
- Uma aplicação percorre repetidamente um vetor de dados. O processador melhora os acessos s
- Uma placa-mãe dispõe de conector M.2, e há duas unidades de mesmo comprimento: uma usa SAT
- Uma controladora de rede gera interrupção ao terminar o recebimento de um pacote. Qual con
- Uma CPU executa uma instrução de soma após ter buscado o comando e identificado os operand
- Um acervo precisa ser distribuído em discos Blu-ray convencionais de camada única. Qual co
- Um computador inicia suas ventoinhas, mas não exibe imagem nem confirma POST. Qual sequênc
- Um modelo simplificado usa barramento de endereços de 20 bits, endereçamento de um byte po
- Uma controladora transfere dados por DMA e emite interrupção ao finalizar. Qual descrição 
- Um processador possui caches L1 de instruções e de dados separadas, mas usa espaço de ende
- Um técnico planeja instalar placa-mãe, CPU, dois módulos de RAM, SSD SATA e fonte em gabin
- Uma biblioteca dispõe de DVDs de camada única, cada um nominalmente com 4,7 GB. Deseja gra
- Um processador com vários níveis de cache sofre falta de dado em L1, mas o encontra em L2.
- Em um computador que segue o modelo clássico de von Neumann, a CPU acessa instruções e dad
- Se uma aplicação ocupa 8 GiB de RAM e seu computador é desligado, esses dados permanecerão
- Uma impressora multifuncional que digitaliza documentos e imprime páginas realiza tanto en
- Um disco DVD-R convencional pode ser apagado e regravado repetidamente pela mesma unidade,
- Um SSD pode empregar memória flash não volátil e interface SATA, enquanto outro SSD utiliz
- Um arranjo RAID 0 com duas unidades de disco oferece redundância de dados por espelhamento
- Ao acessar repetidamente dados próximos, a CPU pode aproveitá-los em linhas de cache; isso
- Em CPU didática, o registrador PC armazena a instrução já buscada para decodificação, enqu
- Um computador perde desempenho e desliga sob carga, enquanto o ventilador está obstruído. 
- Se uma controladora transferiu um bloco por DMA, tornou-se impossível que o dispositivo ge
- Em RAID 5 convencional com quatro discos iguais de 2 TB, a capacidade nominal útil de apro
- Uma placa-mãe que aceita DDR4 também aceita fisicamente um módulo DDR5 no mesmo slot, desd
- Num processador com execução em pipeline, a busca de uma instrução pode coincidir no tempo
- Em qualquer CPU, a cache L3 necessariamente possui latência menor que L1, pois seu tamanho
- Um conjunto RAID 5 com quatro unidades iguais de 1 TB, sem considerar reservas, fornece ap
- Um leitor óptico especificado apenas para CDs e DVDs consegue obrigatoriamente ler discos 
- Antes de substituir um módulo de memória e inspecionar conexões internas, é prudente desli
- Durante transferência de arquivo por DMA, a controladora de dispositivo torna-se equivalen
- Pastas e arquivos são estruturas de organização lógica mantidas por software sobre meios f
- Uma porta USB identificada no gabinete garante, por si só, taxa máxima de transferência, a
```

## Pedido 32: Porcentagem e variação percentual (`matematica.porcentagem`, 1220 tópicos de edital)

```
Matéria: matematica.porcentagem (Porcentagem e variação percentual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos: razão percentual, frações, decimais e bases de cálculo": 20
- "2. Aumentos, descontos, variação relativa e pontos percentuais": 20
- "3. Percentuais sucessivos e composição multiplicativa": 20
- "4. Proporcionalidade, repartição e percentuais ponderados": 20
- "5. Lucro, prejuízo, margens, bases inversas e problemas integrados": 20

Já existem (não repita):
- Em uma pesquisa com 320 participantes, exatamente 15% indicaram a opção X. Quantas pessoas
- Uma oficina informou que 72 peças revisadas correspondem a 24% das peças recebidas. Qual f
- Uma remessa de 640 caixas teve 12,5% separadas para inspeção especial. Quantas caixas fora
- Um ingresso com preço inicial de R$ 200 recebeu reajuste único de 15%. Qual é seu novo pre
- Uma mercadoria custa R$ 360 antes de receber um desconto de 25% sobre o preço anunciado. Q
- Em uma equipe de 90 integrantes, 18 participaram de treinamento. Que porcentagem da equipe
- A produção passou de 120 unidades para 150 unidades sem mudança de critério de medição. Qu
- A proporção de usuários satisfeitos subiu de 42% para 48%, com o mesmo indicador de referê
- Depois de desconto de 20% sobre seu preço inicial, um livro passou a custar R$ 176. Qual e
- Uma escola tem 240 livros de referência, dos quais 80% estão catalogados. Quantos livros j
- A quantidade de atendimentos diminuiu de 150 para 120. Considerando a quantidade inicial, 
- O preço de uma máquina aumentou de R$ 320 para R$ 400. Qual foi o reajuste percentual sobr
- Um item de R$ 500 sofre aumento de 12% e, sobre o preço já reajustado, desconto de 5%. Qua
- Um valor sofre aumento de 10% e imediatamente depois desconto de 10% sobre o resultado do 
- Um equipamento sofreu desconto de 20%. Qual aumento percentual, aplicado sobre o preço red
- Uma verba de R$ 600 será distribuída diretamente na razão 2:3:5 entre três projetos. Quant
- Um grupo de 40 pessoas apresenta 50% de aprovação, enquanto outro de 160 pessoas apresenta
- Uma quantia de R$ 840 será dividida inversamente aos números 2, 3 e 6. Quanto receberá a p
- Em um conjunto de 400 pessoas, metade integra certo grupo. Destas, 30% possuem uma caracte
- Uma mercadoria de R$ 500 sobe 20% e depois recebe desconto de 25% sobre o valor atual. Qua
- Uma mercadoria foi adquirida por R$ 200 e vendida por R$ 250, sem outros custos no enuncia
- Um produto tem custo de R$ 160. Para que o lucro represente 20% do preço de venda, por qua
- Uma mercadoria comprada por R$ 180 foi vendida por R$ 153. Qual foi o prejuízo como percen
- Uma venda de R$ 275 proporcionou lucro de 10% calculado sobre o preço de custo. Qual foi e
- Um comerciante compra item por R$ 100, marca preço 40% acima do custo e dá desconto de 10%
- Em uma turma com 200 estudantes, se 35% pertencem a determinado grupo, então 70 estudantes
- Se 25% de uma remessa com 80 itens forem selecionados para revisão, a quantidade seleciona
- Em relatório estatístico, uma razão decimal igual a 0,40 equivale a 40% do valor adotado c
- Uma mercadoria anunciada por R$ 250 passa a custar R$ 225 depois de desconto de 12% aplica
- Uma medida aumenta de 60 unidades para 75 unidades. Tomando 60 como referência inicial, a 
- Se a proporção de um indicador sobe de 25% para 30%, então o crescimento relativo da própr
- Um preço de R$ 400 cai 25%, chegando a R$ 300. Para retornar exatamente a R$ 400, precisa 
- Uma remuneração recebe dois acréscimos sucessivos, de 20% e de 10%, ambos aplicados sobre 
- Uma quantidade de 500 unidades tem 30% destinados a uma atividade. A parcela destinada cor
- Um grupo tem 10 aprovados em 20 participantes, e outro tem 10 aprovados em 80 participante
- Aplicar redução de 10% e depois aumento de 10% ao valor já reduzido resulta em fator acumu
- Quando um preço sofre dois descontos consecutivos de 20%, calculados sobre os valores vige
- Se restam 80% de uma quantidade após desconto de 20%, um aumento de 25% sobre o restante r
- Uma taxa passa de 50% para 60%. Pode-se afirmar que sua variação relativa foi de 10% porqu
- Na venda de uma mercadoria de custo 100 por preço 125, o lucro de 25 corresponde a 25% do 
- Para alcançar margem de lucro de 20% sobre o preço de venda, basta aumentar o custo da mer
- Uma quantia de 800 será repartida na razão direta 1:3:4; nesse caso, o destinatário associ
- Na repartição inversamente proporcional aos números 2 e 4, o participante associado ao núm
- Em um cadastro de 1.000 pessoas, 30% integram uma categoria. Se 20% dessa categoria têm um
- Se o dobro de equipamentos idênticos e igualmente produtivos executa uma tarefa fixa em me
```

## Pedido 33: Planejamento estratégico, BSC, SWOT e indicadores (`administracao-publica.planejamento-estrategico`, 1210 tópicos de edital)

```
Matéria: administracao-publica.planejamento-estrategico (Planejamento estratégico, BSC, SWOT e indicadores)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Identidade estratégica, etapas e gestão pública": 19
- "2. Diagnóstico SWOT, cenários, GUT e 5W2H": 20
- "3. Estratégia competitiva, forças de Porter, redes e alianças": 15
- "4. Balanced Scorecard, mapa estratégico e OKR": 23
- "5. Indicadores, metas, mensuração e resultados públicos": 23

Já existem (não repita):
- Uma instituição pública descreve sua razão de existir como 'oferecer orientação acessível 
- Em oficina de estratégia, um grupo registra 'ser referência em atendimento inclusivo até 2
- Uma unidade identifica expansão da cobertura regional de internet, condição externa favorá
- O sistema de agendamento de uma instituição apresenta instabilidade recorrente sob respons
- Uma equipe pretende ordenar problemas considerando magnitude de consequências, necessidade
- A direção já priorizou melhorar o agendamento e quer registrar quem fará a mudança, como, 
- Uma empresa avalia rivalidade entre empresas atuantes, entrada de novos competidores, alte
- Um prestador opta por atender exclusivamente um segmento geográfico pequeno com serviço de
- Ao implantar BSC conforme o modelo clássico, uma organização privada pretende registrar su
- Uma instituição completa seu diagnóstico ambiental e precisa transformar prioridades em ob
- Uma organização dispõe de especialistas em atendimento digital e nota crescimento da conec
- Uma instituição combina sistema de atendimento instável e iminente crescimento externo da 
- Em uma matriz GUT com notas de 1 a 5, o problema X recebe G=4, U=5 e T=3; o problema Y rec
- Uma empresa oferece alternativa tecnológica que resolve a mesma necessidade dos consumidor
- Um serviço público implanta BSC e coloca a entrega de valor à sociedade como finalidade ma
- Um mapa estratégico mostra seta entre 'melhorar competências digitais' e 'reduzir erros de
- Um time define o objetivo 'simplificar a experiência de atendimento' e deseja um resultado
- Uma unidade prepara planejamento sob incerteza sobre financiamento e demanda e constrói fu
- Em um plano público, os gestores tratam um possível aumento da demanda, uma reforma regula
- Um setor apresenta poucos fornecedores com elevado poder para impor preços e condições, en
- Um painel informa '90% de atendimento no prazo'. O numerador são 180 pedidos concluídos no
- Uma equipe conclui exatamente as mil entregas previstas como meta anual, mas utiliza o dob
- Uma unidade adquire equipamentos de preço reduzido, mas de durabilidade incompatível com a
- Uma política amplia atendimentos imediatos e, anos depois, pesquisa identifica redução sus
- Em um mapa BSC, a capacitação da equipe é acompanhada mensalmente e a redução efetiva de e
- Uma missão institucional procura expressar a razão de existir e a contribuição presente da
- Na SWOT, uma equipe interna qualificada é classificada como oportunidade, porque o conheci
- A matriz GUT pode usar o produto de gravidade, urgência e tendência para ajudar na prioriz
- No 5W2H, a pergunta How much identifica a quantidade de usuários a atender, enquanto Who r
- As perspectivas clássicas do BSC são financeira, clientes, processos internos e aprendizad
- Entre as cinco forças de Porter, a ameaça de substitutos refere-se apenas a novas empresas
- Ao cruzar uma força interna com uma oportunidade externa, a SWOT permite conceber estratég
- Construir cenários estratégicos significa identificar a previsão futura mais provável e tr
- Na SWOT cruzada, usar força existente para limitar efeitos de ameaça externa corresponde à
- Na matriz GUT, tendência mede prioritariamente a necessidade imediata de agir, enquanto ur
- Uma aliança estratégica pode unir organizações autônomas para compartilhar conhecimentos e
- Um resultado-chave de OKR é definido prioritariamente como tarefa executada, como instalar
- O alcance de meta numérica de entregas indica eficácia em relação ao planejado, mas não co
- O simples desenho de setas entre objetivos de aprendizado, processos e resultados no mapa 
- A quantidade de serviços diretamente entregues costuma caracterizar indicador de produto, 
- Um indicador de atendimento calculado sobre solicitações concluídas pode manter exatamente
- Uma empresa que obtém vantagem de custo em segmento competitivo estreito pode adotar enfoq
- A compra de insumos pelo preço unitário mais baixo comprova economicidade mesmo quando sua
- Um sistema de mensuração de desempenho precisa associar indicadores a objetivos, fontes de
- Em uma estratégia de atendimento digital, elevar de 70% para 90% a satisfação medida em pe
```

## Pedido 34: Obras: orçamento, planejamento e fiscalização (`engenharia.obras-orcamento-fiscalizacao`, 1199 tópicos de edital)

```
Matéria: engenharia.obras-orcamento-fiscalizacao (Obras: orçamento, planejamento e fiscalização)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Projetos, quantitativos e estrutura do orçamento": 18
- "2. Composições, encargos sociais, SINAPI e BDI": 19
- "3. Planejamento físico-financeiro, redes e recursos": 20
- "4. Execução, documentação, inspeção e fiscalização": 23
- "5. Medição, custos, ensaios, vistoria e recebimento": 20

Já existem (não repita):
- Uma equipe levantou uma parede retangular de 12 m de extensão e 3 m de altura, com abertur
- A planilha de orçamento prevê a concretagem de 12 m³ de determinado elemento, com custo di
- Em uma estimativa simplificada, uma obra tem custo global de R$ 80.000 e BDI de 25% aplica
- O contrato prevê 100 m² de revestimento, e a fiscalização confirmou 60 m² executados e ace
- Uma planilha federal necessita de referências para serviços usuais de construção de edifíc
- Uma composição para determinado serviço registra 0,25 trabalhador-hora por metro quadrado 
- Durante uma visita, o fiscal constata desvio relevante de execução que pode ser corrigido 
- Após a verificação técnica de serviços de engenharia, a Administração pretende formalizar 
- Uma composição ilustrativa prevê 2 horas de mão de obra a R$ 30/h e 0,05 m³ de insumo a R$
- Um orçamentista recebe uma hora de trabalho cotada em R$ 20 sem encargos e, para um exercí
- Uma rede de atividades tem A com 3 dias, seguida por B com 5 dias e C com 4 dias, executad
- Uma obra tem dois caminhos independentes entre início e fim, com durações de 9 e 7 dias e 
- No planejamento semanal, duas frentes simultâneas precisam de 3 e 4 trabalhadores distinto
- No MS Project, uma equipe deseja representar a dependência comum em que o lançamento de co
- Um cronograma foi aprovado com datas e custos previstos. Após dois meses, as tarefas foram
- Uma obra tem serviço contratado de 200 m² de pavimentação a R$ 500/m². A verificação mensa
- Em contrato de reforma de edifício sujeito à Lei nº 14.133/2021, estuda-se alteração unila
- A planilha prevê 100 m² de um revestimento. O contratado executa 105 m² sem autorização fo
- O orçamento apresenta administração local quantificável e fornecimento especializado de eq
- O orçamento de uma etapa prevê 180 m² a custo direto de R$ 90/m², sobre o qual incide BDI 
- Uma atividade de terraplenagem foi estimada pelo método PERT com duração otimista de 2 dia
- Uma composição apresenta consumo de mão de obra de 0,5 trabalhador-hora/m². Mantidas as co
- Durante ensaio de estanqueidade de uma instalação, detecta-se vazamento incompatível com o
- Em uma reforma de edifício existente, serão removidas divisórias e alteradas tubulações co
- Uma obra pública foi recebida definitivamente há dois anos e apresenta defeito relacionado
- Em inspeção de prédio em execução, a equipe identifica fissura, fotografa sua localização,
- Uma contratação verbal para prestação de serviço profissional de engenharia pode dispensar
- Ao escolher uma composição do SINAPI para revestimento, a equipe consulta o caderno técnic
- Em orçamento federal abrangido pelo Decreto nº 7.983/2013, o custo referencial de uma rodo
- Em cronograma de reforma, um gráfico de Gantt apresenta barras de atividades posicionadas 
- Em uma obra com custo direto de R$ 50.000, aplicar BDI de 20% significa que o preço simpli
- Uma obra tinha 50% de execução acumulada prevista para certa data, mas a medição acumulada
- O fiscal identifica necessidade de acrescentar serviços a uma obra e pode, por sua anotaçã
- A memória de quantitativos de uma fundação retangular de 2 m por 2 m por 1 m registra 4 m³
- No recebimento definitivo de serviços de engenharia, basta a manifestação unilateral do ex
- Para obra de arquitetura e urbanismo situada sob o regime profissional da Lei nº 12.378/20
- Um insumo de mão de obra do orçamento já possui custo horário com encargos sociais incluso
- Ao revisar a rede de precedências de um cronograma no MS Project, o gestor pode constatar 
- O Decreto nº 12.867/2026 determinou que a pesquisa de preços associada ao SINAPI seja real
- Uma administração local de canteiro tem recursos e custos próprios identificáveis. Sua pre
- Em reforma de edifício submetida a alteração unilateral na Lei nº 14.133/2021, a mesma tol
- Uma rede CPM possui caminhos paralelos de 10 e 9 dias até o mesmo marco final, sem restriç
- Uma atividade PERT tem estimativas de 1 dia otimista, 5 dias mais provável e 9 dias pessim
- No recebimento definitivo de projeto de obra, o art. 140, § 5º, da Lei nº 14.133/2021 não 
- Em obra que passou por medição financeira completa, o art. 140 da Lei nº 14.133/2021 torna
```

## Pedido 35: Sistema Operacional Windows 10 (`informatica.sistema-operacional-windows`, 1192 tópicos de edital)

```
Matéria: informatica.sistema-operacional-windows (Sistema Operacional Windows 10)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, área de trabalho, janelas e navegação": 19
- "2. Explorador de Arquivos, caminhos, extensões e comparação com Linux": 23
- "3. Operações com arquivos, pastas e área de transferência": 22
- "4. Configurações, aplicativos, impressoras e compartilhamento": 18
- "5. Backup, recuperação e proteção básica dos dados": 18

Já existem (não repita):
- Durante uma reunião, uma pessoa minimiza uma janela com documento ainda aberto e depois de
- Uma pessoa precisa abrir rapidamente o Explorador de Arquivos no Windows 10 enquanto outro
- Para entregar um documento em PDF, alguém muda apenas o nome `resumo.docx` para `resumo.pd
- No Explorador de Arquivos, uma pasta de projetos está aberta e o usuário pretende criar al
- Uma equipe precisa deixar `ata.txt` na pasta original e disponibilizar uma segunda instânc
- Há um atalho na área de trabalho apontando para um relatório salvo em Documentos. A pessoa
- Uma pessoa precisa recuperar trecho de texto copiado anteriormente, após já ter copiado ou
- Uma pessoa deseja tornar maiores os textos e ícones exibidos sem mudar a quantidade de pix
- Uma pasta `C:\Users\Ana\Documentos` aparece na estação com Windows 10. Em outra máquina Li
- No Explorador do Windows 10, um arquivo é arrastado de uma pasta da unidade `C:` para outr
- Uma pessoa arrasta um arquivo de `C:\Trabalho` para `C:\Arquivo` sem pressionar teclas mod
- Uma busca no Explorador, iniciada dentro da pasta Projetos, não encontra um documento guar
- Uma pasta em volume NTFS é compartilhada na rede com permissão de leitura para determinada
- Um computador com Windows 10 exibe apenas o conteúdo copiado mais recentemente. A pessoa q
- Uma pessoa quer capturar somente uma região da tela para inserir a imagem em um documento,
- Uma impressora foi definida manualmente como padrão, mas a escolha muda depois do uso de o
- Após um backup completo no domingo, foi feito backup incremental na segunda-feira. Na terç
- Uma pasta foi incluída em backup completo no domingo. Na segunda e na terça foram alterado
- Uma pessoa abre no Explorador o caminho `\\Servidor\Equipe\Plano.xlsx` a partir de uma est
- Uma pasta compartilhada concede leitura na rede, mas sua guia Segurança atribui modificaçã
- Um documento pessoal foi apagado no Explorador com Shift+Delete e não aparece na Lixeira. 
- Um aplicativo deixa de responder durante a edição de texto. Para consultar os processos e,
- Um arquivo de trabalho foi alterado indevidamente ontem, mas tinha versões salvas em backu
- Uma pasta contém documentos no Windows 10. Uma pessoa arrasta um deles para a área de trab
- Uma pessoa identifica atalho `.lnk` em um computador Windows e um link simbólico para arqu
- Uma pessoa pressiona Windows+D durante a edição de um documento e observa a área de trabal
- Ao renomear `balanco.xlsx` para `balanco.pdf` no Explorador, o Windows 10 converte automat
- Ao selecionar texto em um editor e pressionar Ctrl+C, uma pessoa conserva o trecho origina
- A ausência de um arquivo nos resultados de busca efetuada dentro de uma pasta específica c
- Uma imagem simples pode ser editada no Paint, enquanto texto sem formatação rica pode ser 
- Para mover um arquivo no Explorador por comandos de teclado, é suficiente usar Ctrl+C e Ct
- O Histórico de Arquivos do Windows 10, quando configurado para proteger arquivos pessoais 
- No ambiente Linux, um caminho absoluto típico como `/home/ana/arquivo.txt` depende da exis
- Uma pessoa tem acesso NTFS de modificar arquivo, mas o compartilhamento de rede concede ap
- Um arquivo nunca esteve incluído em backup nem foi sincronizado. Ao apagá-lo permanentemen
- No Windows 10, se estiver habilitada a opção que permite ao sistema gerenciar a impressora
- A sincronização de uma pasta com serviço de nuvem é, por definição, uma cópia de segurança
- Um arquivo com nome `foto.jpg.exe` pode ser mal interpretado por um usuário se a exibição 
- No Explorador, ao arrastar um arquivo da unidade `C:` para um pendrive `E:` sem teclas mod
- Em uma sequência de backups, o diferencial feito na quarta-feira considera as alterações d
- Uma pessoa com permissão NTFS para modificar arquivo terá autorização remota para fazê-lo 
- O histórico da área de transferência do Windows 10 pode manter itens fixados entre reinici
- O fim do suporte regular do Windows 10 em 14 de outubro de 2025 fez o sistema deixar de in
- Num sistema de arquivos Linux usual sensível a maiúsculas e minúsculas, os nomes `Relatori
- Uma política que faz cópias apenas em outra pasta da mesma unidade física protege de modo 
```

## Pedido 36: Modelos de administração pública e governança (`administracao-publica.modelos-governanca`, 1186 tópicos de edital)

```
Matéria: administracao-publica.modelos-governanca (Modelos de administração pública e governança)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Patrimonialismo, burocracia e evolução das formas de Estado": 17
- "2. Reformas administrativas brasileiras e gestão pública versus privada": 24
- "3. Gerencialismo, empreendedorismo, participação e Nova Governança Pública": 17
- "4. Governabilidade, governança, accountability e Decreto nº 9.203/2017": 25
- "5. Valor público, indicadores, resultados e excelência dos serviços": 17

Já existem (não repita):
- Uma administração distribui funções a aliados pessoais, independentemente de critérios téc
- Uma unidade adota seleção profissional, competências previamente definidas, registros de d
- Um serviço exige reapresentação de comprovantes já conferidos apenas porque cada fase prec
- O Estado passa a concentrar esforços na definição de padrões, supervisão e fiscalização de
- Representações de trabalhadores, empregadores e poder público participam de negociação ins
- Ao estudar a modernização brasileira iniciada na década de 1930, uma equipe encontra a cri
- Uma proposta de capacitação cita os princípios fundamentais expressos no art. 6º do Decret
- No estudo do Plano Diretor de 1995, uma atividade de prestação social pode contar com enti
- Uma revisão histórica pergunta qual mudança a EC nº 19/1998 promoveu expressamente no capu
- Uma unidade pública deseja importar ferramenta privada de redução de filas, porém possui d
- Uma administração reformula suas atividades para dar autonomia de execução às equipes, pac
- Uma liderança articula serviços públicos e entidades comunitárias para reduzir evasão esco
- Uma mudança de política envolve conflitos de valores, conhecimento incompleto e a necessid
- Antes de redesenhar um serviço, uma instituição abre prazo para recebimento de contribuiçõ
- Uma política depende da coordenação de serviços sociais, ensino e organizações locais, com
- Um governo dispõe de coalizão política suficiente para aprovar decisões, mas suas institui
- Ao preparar uma matriz com o Decreto nº 9.203/2017, deseja-se selecionar apenas os três me
- Uma diretriz de governança orienta a direção a identificar a ocorrência de riscos e implem
- Uma análise histórica do Decreto nº 9.203/2017 deve considerar as alterações do Decreto nº
- Um programa pretende prevenir, detectar, punir e remediar fraude, mantendo apoio da alta a
- Uma associação de moradores publica análise sobre serviço público e promove mobilização pa
- Uma iniciativa digital pode reduzir tempo de serviço, mas carece de autorização legítima e
- Duas unidades entregam 800 atendimentos de complexidade equivalente. A primeira utiliza 40
- Um programa atingiu 90% de sua meta de atendimentos, mas pretende afirmar que provocou red
- Uma instância estratégica define prioridades, acompanha riscos e verifica se controles são
- Uma administração que separa as decisões de interesses pessoais, mantém atribuições por re
- Em um programa social com grande cobertura, a expansão da proteção e dos serviços coletivo
- O art. 6º do Decreto-Lei nº 200/1967 enumera planejamento, coordenação, descentralização, 
- Uma entidade que adapta método empresarial de gerenciamento de filas à prestação pública d
- Um processo consultivo que recebe contribuições da população sobre proposta de política pú
- Uma política que apresenta 1.000 serviços concluídos e nenhuma informação de mudanças entr
- Um governo que constrói legitimidade política e apoio suficiente para decidir pode ser con
- O Plano Diretor de 1995 classificou os serviços não exclusivos como idênticos às atividade
- Um governo atua de modo catalisador quando articula diferentes executores e mantém orienta
- Em um processo decisório transparente, o uso de pesquisa de satisfação dos usuários transf
- Segundo o art. 2º, I, do Decreto nº 9.203/2017, governança pública envolve mecanismos de l
- Uma instituição pública que concluiu oitocentos dos mil atendimentos previstos pode afirma
- Uma política combina missão, objetivos, métricas e revisão de resultados; esse alinhamento
- O art. 8º-B do Decreto nº 9.203/2017, após o Decreto nº 9.901/2019, prevê que o CIG se reú
- Ao investigar gestão de riscos no Decreto nº 9.203/2017, uma unidade conclui que a alta ad
- O art. 19 do Decreto nº 9.203/2017 estabelece programa de integridade reduzido ao monitora
- Num programa que oferece assistência a famílias, aumento de cadastros é produto imediato; 
- No triângulo de Mark Moore, um projeto eficiente e com recursos suficientes pode gerar val
- Um programa com vários executores pode reunir procedimentos burocráticos impessoais, metas
- A accountability vertical é exercida principalmente por controles especializados entre ins
```

## Pedido 37: Regra de três simples e composta (`matematica.regra-de-tres`, 1182 tópicos de edital)

```
Matéria: matematica.regra-de-tres (Regra de três simples e composta)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Razões, proporções e variação direta ou inversa": 11
- "2. Regra de três simples, frações e porcentagens básicas": 38
- "3. Regra de três composta com produção, equipes e períodos": 31
- "4. Conversões de medidas, compras, transportes e estoques": 10
- "5. Divisão proporcional direta, inversa e composta": 10

Já existem (não repita):
- Uma papelaria vende 4 cadernos iguais por R$ 28,00, sem desconto por quantidade. Quanto cu
- Uma equipe de 8 trabalhadores de produtividade idêntica conclui um serviço em 15 dias. Em 
- Em um depósito, 12% de um lote de 250 peças devem ser separados para conferência. Quantas 
- Três quilogramas de determinado produto custam R$ 42,00. Qual será o preço de cinco quilog
- Um piso apresenta área de 1,5 m². Qual é essa mesma área expressa em centímetros quadrados
- Um bônus de R$ 250,00 será dividido entre duas pessoas diretamente na razão 2:3. Qual quan
- Quatro máquinas idênticas fabricam 240 peças em 6 horas. Quantas peças seis máquinas iguai
- Um trajeto de 2 km foi anotado em formulário que solicita a distância exclusivamente em me
- Cinco caminhões idênticos realizam quatro viagens cada e transportam 100 toneladas ao todo
- Em um estoque, 150 unidades correspondem a 30% de todos os itens disponíveis. Quantas unid
- Seis trabalhadores produzem 240 unidades em oito dias de cinco horas. Quantas unidades oit
- Um total de R$ 900,00 será repartido entre duas pessoas inversamente aos números 2 e 4. Qu
- A capacidade de um reservatório foi registrada como 3,5 m³. Qual é sua capacidade equivale
- Seis trabalhadores fazem 180 unidades de serviço em 12 dias de 5 horas. Quantos dias nove 
- Duas pessoas repartirão R$ 4.000 de resultado conforme capital vezes tempo. A investiu R$ 
- Oitenta litros correspondem a 25% do volume total de um tanque. Qual é a capacidade integr
- Uma quantia de R$ 960 será dividida em três parcelas diretamente proporcionais a 3, 4 e 5.
- Quatro equipes idênticas fabricam 1.008 peças em sete dias, com seis horas diárias. Mantid
- Doze caminhões com capacidade de 5 toneladas fazem quatro viagens cada e transportam 240 t
- Oito máquinas fazem determinado trabalho em dez dias, operando seis horas diárias. Mantido
- Uma gratificação de R$ 1.500 deve ser repartida diretamente às unidades de serviço e inver
- O resultado de R$ 5.400 será dividido segundo capital multiplicado pelo tempo de participa
- Um reservatório contém 1,8 m³ de água. Quantos centímetros cúbicos representam esse volume
- Seis máquinas fabricam 864 peças em quatro dias de nove horas diárias. Sob taxa por máquin
- Quatro trabalhadores fazem 432 peças em doze dias de seis horas. Quantos dias seis trabalh
- Em uma compra de dez kits idênticos sem taxa fixa, se cinco kits custam R$ 60,00, o preço 
- Um mesmo serviço leva 18 dias com seis trabalhadores e, se forem empregados nove trabalhad
- Uma operação registra duração de três horas. Ao expressá-la em minutos, devem ser anotados
- Um depósito retangular tem piso com exatamente 1 m² de área. Essa medida corresponde a 1.0
- Uma seleção reserva quinze por cento de um lote de 400 exemplares. A reserva será de 60 ex
- Na divisão de R$ 600 em partes inversamente proporcionais a 2 e 4, os dois destinatários r
- Uma tabela mostra 3 unidades de insumo para 5 de produto e 6 unidades de insumo para 10 de
- Uma entrega cobra R$ 12 fixos mais R$ 5 por caixa transportada. Como o preço aumenta quand
- Cinco trabalhadores produzem 600 peças em quatro dias de seis horas. Oito trabalhadores ig
- Três bombas iguais esvaziam um reservatório em dez horas. Cinco bombas iguais, sob vazão c
- Um recipiente com capacidade de 2,4 m³ comporta 2.400 litros de líquido, tomando a convers
- Sem conhecimento da substância ou de sua densidade, pode-se converter diretamente o estoqu
- Uma quantia de R$ 150 será repartida diretamente na razão 4:6. As parcelas são R$ 60 e R$ 
- Um depósito tinha 150 pacotes, dos quais 40%, equivalentes a 60 pacotes, eram de certo tip
- Oito trabalhadores produzem 240 peças em cinco dias de seis horas. Dez trabalhadores idênt
- Uma quantia de R$ 140 deve ser dividida em partes inversamente proporcionais a 2 e 5. A pe
- Se 12% da capacidade total de um reservatório equivalem a 180 L, o volume integral é 1.500
- Uma embalagem possui volume de 0,04 m³. Esse volume equivale a 40 L e, por consequência, a
- Pela regra expressa de divisão do resultado conforme capital multiplicado pelo tempo, inve
- Quatro trabalhadores produzem 240 peças em cinco dias de seis horas. Oito trabalhadores id
```

## Pedido 38: Poder Legislativo e estatuto dos congressistas (`direito-constitucional.poder-legislativo`, 1168 tópicos de edital)

```
Matéria: direito-constitucional.poder-legislativo (Poder Legislativo e estatuto dos congressistas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Congresso Nacional: composição, representação e deliberação": 19
- "2. Atribuições do Congresso e competências privativas das Casas": 18
- "3. Imunidades parlamentares, foro e garantias de independência": 15
- "4. Incompatibilidades, perda de mandato, licenças e suplência": 13
- "5. Sessões, comissões, CPIs e controle judicial": 35

Já existem (não repita):
- Uma pessoa afirma que o Senado Federal representa diretamente o povo em proporção à popula
- Uma unidade da Federação pretende identificar quantos senadores e suplentes lhe correspond
- Em uma deliberação ordinária da Câmara dos Deputados, sem quórum constitucional especial, 
- O Presidente da República pretende ausentar-se do País por dezenove dias. Qual instância d
- Diante da necessidade de autorizar a instauração de processo contra o Presidente da Repúbl
- Após autorização constitucionalmente exigida, instaura-se julgamento político de President
- Um deputado federal diplomado é preso em flagrante de crime inafiançável. Qual providência
- Uma comissão parlamentar de inquérito é solicitada para apurar desvio específico, com dura
- Um parlamentar recém-diplomado pretende firmar contrato não padronizado com autarquia. Ain
- Uma deliberação exige dois terços dos membros da Câmara, atualmente com 513 cadeiras. Sem 
- A Mesa de uma das Casas encaminha pedido escrito de informações a uma das autoridades abra
- Uma comissão analisa qual procedimento constitucional deve ser seguido para aprovar a esco
- Um congressista é acusado de quebra de decoro por receber vantagem indevida ligada ao mand
- Um deputado obtém licença não remunerada para tratar de interesse particular. Deseja saber
- Uma pessoa consulta o art. 57 para organizar o calendário de funcionamento ordinário anual
- Uma CPI investiga transferência de recursos e pretende obter registros bancários e dados d
- Um ex-parlamentar é acusado de crime praticado quando exercia o mandato e diretamente rela
- Uma denúncia por crime cometido após a diplomação de senador foi recebida. Partido represe
- Durante estado de sítio, a Casa pretende suspender imunidade parlamentar em razão de manif
- Dois deputados enfrentam processos distintos: o primeiro por procedimento contrário ao dec
- Abre-se vaga no Senado sem suplente disponível, quando faltam dezoito meses para o término
- Surge urgência de interesse público relevante e os Presidentes da Câmara e do Senado consi
- Comissão temática recebe atribuição regimental para discutir e votar projeto de lei dispen
- Em duas situações distintas, pretende-se: (I) sustar um ato normativo do Executivo que exc
- Uma CPI legalmente criada decide, sem fundamentação individualizada, quebrar sigilo bancár
- Em uma legislatura federal, a Câmara representa o povo pelo sistema proporcional, enquanto
- Como cada legislatura dura quatro anos, os senadores precisam ser eleitos novamente a cada
- Caso decreto normativo do Executivo ultrapasse o poder regulamentar, compete exclusivament
- O Senado Federal autoriza a instauração de processo contra o Presidente por dois terços de
- Desde a expedição do diploma, a Constituição prevê restrição à prisão de deputados e senad
- Para instituir CPI de uma Casa legislativa, basta o requerimento de um terço dos seus memb
- Após a posse, o patrocínio de causa em que seja interessada autarquia integra as vedações 
- Um parlamentar que deixou de comparecer à terça parte das sessões ordinárias, sem licença 
- A Constituição permite que deputado investido no cargo de Ministro de Estado conserve o ma
- Para a posse dos novos integrantes e a eleição das Mesas, o art. 57 determina sessões prep
- O art. 50, na redação que inclui o Presidente do Comitê Gestor do Imposto sobre Bens e Ser
- A condenação criminal transitada em julgado e a perda ou suspensão dos direitos políticos 
- A Constituição prevê que as comissões possam realizar audiências públicas com entidades da
- Uma CPI regular determina, por deliberação própria, que operadoras interceptem o conteúdo 
- Um ex-deputado responde por crime praticado durante o mandato em razão das funções. Segund
- O recebimento de denúncia por crime cometido antes da diplomação permite a sustação previs
- Durante estado de sítio, a suspensão de imunidades de congressista depende de dois terços 
- Por ter poderes de investigação próprios das autoridades judiciais, uma CPI pode realizar 
- Em convocação extraordinária do Congresso, a deliberação se limita, em regra, à matéria da
- Um parlamentar submetido a procedimento capaz de levar à perda do mandato apresenta renúnc
```

## Pedido 39: Sociedades (`direito-empresarial.sociedades`, 1166 tópicos de edital)

```
Matéria: direito-empresarial.sociedades (Sociedades)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria geral, personalidade e sociedades não personificadas": 25
- "2. Sociedades contratuais, limitada e responsabilidade dos sócios": 22
- "3. Sociedade anônima, comandita por ações e governança": 18
- "4. Cooperativas, coligações e sociedades autorizadas": 16
- "5. Saída do sócio, dissolução, liquidação e transformação": 19

Já existem (não repita):
- Ana e Bruno comprometem-se a aportar bens para desenvolver atividade econômica e dividir s
- Um grupo celebra contrato de sociedade empresária, mas ainda não inscreveu os atos constit
- Três sócios atuam antes de registrar o contrato e assumem obrigação com fornecedor. Qual é
- Na sociedade em conta de participação, Carla aporta recursos e participa dos resultados, e
- Uma sociedade paga repetidamente despesas pessoais de seu administrador, sem contraprestaç
- Em limitada com capital subscrito de 100 mil e apenas 70 mil integralizados, surge dívida 
- Um contrato de sociedade simples estabelece que um sócio contribuirá com bens e o outro co
- Dois sócios desejam constituir sociedade em nome coletivo e pretendem reservar a administr
- Na comandita simples, um sócio comanditário passa a assinar atos de gestão perante fornece
- Sócios de uma limitada pretendem integralizar parte do capital com serviços profissionais 
- Uma limitada tem capital dividido entre quatro sócios e o contrato nada prevê sobre transf
- Uma sociedade limitada deseja alterar contrato social. As quotas estão integralizadas, não
- Uma companhia pretende emitir ações preferenciais sem direito de voto que representem 60% 
- Após encerrar exercício em 31 de dezembro, uma companhia precisa analisar contas, demonstr
- Uma companhia pretende captar financiamento mediante emissão de títulos que assegurem a in
- Em cooperativa comum com diversos cooperados, um associado subscreve quantidade de quotas 
- Uma cooperativa apura sobras líquidas de R$ 100.000,00 no exercício. Quais dotações mínima
- Uma S/A tem estatuto omisso sobre dividendos. O lucro líquido é de R$ 200 mil, com destina
- Uma companhia fechada, sem conselho de administração obrigatório por sua condição, prevê d
- Sociedade X detém 12% do capital da sociedade Y, sem controlá-la. O caso é examinado estri
- Na aplicação do art. 243 da Lei nº 6.404/1976, a investidora dispõe de 22% dos votos da in
- Uma sociedade regularmente constituída no exterior pretende instalar filial que opere dire
- Uma sociedade em comandita por ações pretende nomear como diretor alguém que não seja acio
- Sócios transformam uma limitada em S/A. O contrato prévio autorizava transformação sem una
- Após liquidação de sociedade regida pelas regras civis, contas finais são aprovadas e a at
- Uma sociedade presta atividade econômica com organização típica de empresa, mas adotou a f
- Em sociedade em comum sem inscrição, os sócios só respondem pelas obrigações até o montant
- Uma pessoa jurídica transfere repetidamente recursos para pagar obrigações pessoais de sóc
- Em sociedade simples, o contrato pode excluir inteiramente o sócio que contribui apenas co
- Mesmo quando a limitada é composta por mais de um sócio, o art. 1.052 do CC impõe a todos 
- Um contrato de sociedade em nome coletivo pode atribuir administração regular a pessoa jur
- Na limitada cujo contrato nada dispõe sobre cessão, a transferência de quotas para pessoa 
- A personalidade da sociedade pode ser desconsiderada com base apenas na existência de grup
- A Lei nº 12.690/2012 permite constituir cooperativa de trabalho com pelo menos sete sócios
- No regime do Código Civil, uma sociedade que detém participação de 12% do capital de outra
- Uma sociedade sujeita a autorização específica não iniciou operações dentro de doze meses 
- Em cooperativa que apurou sobras líquidas, a Lei nº 5.764/1971 exige dotar o FATES em pelo
- Um administrador de companhia pratica ato regular de gestão em nome da sociedade sem culpa
- Como o art. 109 da Lei das S.A. assegura direitos essenciais ao acionista, qualquer espéci
- Na classificação acionária de coligadas, a participação em vinte por cento ou mais dos vot
- Numa cooperativa, o sócio pode transferir livremente suas quotas de capital a terceiro est
- Uma S/A tem estatuto omisso quanto ao dividendo obrigatório. A apuração inicial deve obser
- Uma sociedade estrangeira obteve decreto que autoriza o funcionamento no Brasil. Ela pode 
- Uma sociedade passa de limitada para anônima por transformação válida, sem dissolução ou l
- O sócio retirante de sociedade simples deixa de responder por todas as obrigações anterior
```

## Pedido 40: Educação inclusiva, diversidade e temas transversais (`educacao.diversidade-inclusao-temas-transversais`, 1159 tópicos de edital)

```
Matéria: educacao.diversidade-inclusao-temas-transversais (Educação inclusiva, diversidade e temas transversais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Inclusão escolar, direitos e evolução normativa": 22
- "2. Atendimento educacional especializado e práticas pedagógicas": 23
- "3. Educação integral, educação do campo e diversidade cultural": 19
- "4. Educação ambiental, direitos humanos e transversalidade curricular": 19
- "5. Tecnologias educacionais, letramento midiático e aplicação integrada": 17

Já existem (não repita):
- Uma escola matriculou uma estudante com baixa visão, mas manteve apostilas ilegíveis e pro
- Ao revisar uma apostila, a equipe encontra o Decreto nº 7.611/2011 citado como regra atual
- Na elaboração de um plano de inclusão, a equipe procura identificar os estudantes abrangid
- Uma instituição privada comunica à família de aluno com deficiência que cobrará uma taxa a
- Uma professora percebe que o aluno frequenta a turma comum, mas não consegue acessar vídeo
- Uma criança com deficiência utiliza leitor de tela e recebe AEE para dominar recursos de a
- Uma estudante com altas habilidades recebe encontros destinados a pesquisar assuntos de se
- Uma escola condiciona a elaboração dos apoios especializados à apresentação de laudo médic
- A equipe pedagógica discute quem deve ensinar o currículo e quem deve organizar os recurso
- Em reunião de formação, apresentam-se cargas horárias do Decreto nº 12.686/2025 em sua red
- Uma rede expandiu a jornada de quatro para sete horas diárias, mas conservou aulas repetit
- Um estabelecimento situado no perímetro urbano atende predominantemente filhos de pescador
- Uma escola do campo propõe calendário que contemple épocas de colheita e clima local sem s
- Ao preparar o currículo, uma rede prevê história e cultura afro-brasileira e indígena apen
- Uma secretaria cogita fechar uma escola do campo e deslocar todos os estudantes para munic
- Uma escola quer criar disciplina obrigatória e autônoma de educação ambiental para substit
- Uma comunidade escolar elaborou projeto sobre enchentes, cruzando medidas de precipitação,
- Um grupo elabora proposta de educação em direitos humanos com escuta dos estudantes, preve
- Após a Lei nº 14.926/2024, uma equipe atualiza seu projeto pedagógico para contemplar ques
- Durante a revisão curricular, a equipe apresenta supostas listas de macroáreas dos Temas C
- Uma rede apresenta programa de educação digital que cobre equipamentos, cultura digital, c
- Um grupo divulga vídeo alarmante sobre contaminação de rio local, sem fonte nem data verif
- Uma escola instala tablets, mas o aplicativo não funciona com leitor de tela e expõe dados
- Estudante necessita de dispositivo portátil para comunicação alternativa, e a escola prete
- Uma proposta pretende conjugar educação do campo, tempo integral, AEE e investigação ambie
- Um aluno está matriculado na classe comum, mas enfrenta barreiras de comunicação. Adaptar 
- Uma escola considera vigente em 2026 o Decreto nº 7.611/2011 porque a política nacional de
- Uma turma contém estudantes com dificuldades ocasionais de leitura. Apenas essa dificuldad
- Uma instituição privada deve garantir medidas de inclusão enumeradas no art. 28, § 1º, da 
- Uma estudante com deficiência frequenta AEE para aprender a usar um leitor de tela. A matr
- Em escola com estudante de altas habilidades, oferecer atividades de investigação e aprofu
- Ao planejar AEE, uma escola realiza estudo de caso para identificar barreiras e potenciali
- Uma rede mantém como exigência atual 80 horas de formação continuada tanto para docente de
- Uma escola oferece jornada de sete horas diárias e estrutura proposta multidimensional de 
- Um estabelecimento urbano que atende predominantemente população ribeirinha não pode ser c
- Uma escola reorganiza seu calendário em razão das fases da pesca artesanal e das condições
- Uma escola particular do ensino médio decide não ensinar história e cultura indígena porqu
- Uma secretaria afirma que a Lei nº 9.795/1999 tornou obrigatória uma disciplina autônoma d
- Uma rede inclui, em seus projetos institucionais e pedagógicos, mudanças do clima, biodive
- Em projeto sobre direitos humanos, a equipe afirma que a Resolução CNE/CP nº 1/2012 privil
- O estudo integrado de consumo responsável, impactos ambientais e direitos da população pod
- Um curso envolve investigação de mensagens virais, identificação de fontes primárias e pro
- Uma rede sustenta que a Lei nº 14.533/2023 organiza a Política Nacional de Educação Digita
- Uma escola entende que obter uma plataforma conectada à internet dispensa a avaliação de a
- Uma escola usa comunicação alternativa em dispositivo portátil como recurso de tecnologia 
```

## Pedido 41: Problemas aritméticos, geométricos e matriciais (`raciocinio-logico.problemas-aritmeticos`, 1147 tópicos de edital)

```
Matéria: raciocinio-logico.problemas-aritmeticos (Problemas aritméticos, geométricos e matriciais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Aritmética, frações, porcentagens, ciclos e juros": 21
- "2. Proporcionalidade, médias, equações e gráficos": 21
- "3. Conjuntos, contagem, probabilidade e códigos simples": 16
- "4. Geometria aplicada: perímetros, áreas, Pitágoras e volumes": 21
- "5. Matrizes, tabelas e problemas integrados": 21

Já existem (não repita):
- Uma central recebeu 120 pacotes e distribuiu três lotes de 12 pacotes cada. Quantos pacote
- Uma equipe concluiu 1/3 de um relatório pela manhã e 1/4 à tarde, ambas as frações relativ
- Para montar o maior número possível de kits idênticos sem sobras, há 18 cadernos e 30 lápi
- Um equipamento custava R$ 400; seu preço subiu 20% e, em seguida, sofreu desconto de 20% s
- Numa turma, 27 alunos praticam corrida, 21 praticam natação e 12 praticam ambas. Quantos p
- Um pátio retangular mede 7 metros de largura e 9 metros de comprimento. Quantos metros qua
- Uma tabela matricial tem primeira linha [2, 4, 6] e segunda linha [3, 5, 9]. Qual é o elem
- Quatro medições de duração de atendimento foram 5, 7, 9 e 11 minutos. Qual é a média aritm
- Um dado honesto de seis faces é lançado uma vez. Qual a probabilidade de sair um número pa
- Quatro trabalhadores de mesma produtividade concluem uma tarefa em 12 dias. Seis trabalhad
- Quatro máquinas iguais produzem 240 peças em cinco dias. Quantas peças seis máquinas iguai
- Um capital de R$ 1.500 é aplicado a juros simples de 2% ao mês durante quatro meses. Qual 
- Um terreno triangular tem base de 12 metros e altura perpendicular de 9 metros. Qual é sua
- Uma comissão de duas pessoas será escolhida entre seis voluntários distintos, sem cargos o
- Um gráfico de barras informa 90 atendimentos em janeiro e 120 em fevereiro, com a mesma un
- Uma matriz de quantidades tem linhas [2,1] e [3,2]. Multiplicando-a pelo vetor-coluna de p
- Em um grupo de 60 pessoas, 33 participam de A, 24 de B e 11 de ambas. Quantas não particip
- Em uma sessão foram vendidos oito ingressos, alguns integrais a R$ 16 e outros reduzidos a
- Um valor de R$ 1.000 sofre aumento de 30% e depois desconto de 10% sobre o preço atualizad
- Um reservatório cilíndrico circular reto tem raio interno de 3 metros e altura interna de 
- Uma urna tem quatro bolas azuis e duas vermelhas. Retiram-se três bolas sem reposição e se
- Considere A de ordem 2×3, com linhas [1,2,0] e [0,1,3], e B de ordem 3×2, com linhas [2,1]
- A diagonal de um terreno retangular mede 15 metros, enquanto um dos lados mede 9 metros. Q
- R$ 2.000 foram aplicados por dois períodos à taxa de 10% por período. Qual é o montante co
- Utilizando o alfabeto A–Z sem acentos, uma cifra de César desloca cada letra duas posições
- Dois sinais começam juntos e repetem-se a cada 6 e 8 minutos. A primeira nova coincidência
- Após um aumento de 25% e um desconto de 25% sobre o valor atualizado, um produto necessari
- Em uma turma de 50 alunos, 20 fazem A, 25 fazem B e 10 fazem as duas atividades. Logo, 15 
- A soma das frações 1/2 e 1/3 corresponde a 2/5, porque se somam diretamente numeradores e 
- Uma caixa retangular com dimensões internas de 2 metros, 3 metros e 4 metros tem capacidad
- Se duas matrizes quadradas A e B têm a mesma ordem, sua multiplicação é comutativa, isto é
- Numa urna com três bolas azuis e duas vermelhas, retirar duas azuis sem reposição tem prob
- Um gráfico em que o número de atendimentos passa de 80 para 100 mostra aumento de 20%, poi
- Se quatro máquinas iguais produzem 100 unidades em cinco horas, oito máquinas iguais, no m
- Um triângulo de base 10 cm e altura perpendicular 8 cm tem área de 80 cm², pois sua área é
- O produto entre uma matriz de ordem 2×3 e outra de ordem 3×4 é definido e resulta em matri
- Dois meses com juros compostos de 10% ao mês produzem acréscimo total de 20%, pois basta s
- Na cifra de César com deslocamento de três letras no alfabeto A–Z, a letra Z é codificada 
- Em um grupo de 100 pessoas, 50 pertencem a A, 40 a B, 30 a C, 15 a A∩B, 10 a A∩C, 12 a B∩C
- Um grupo de três participantes será selecionado entre sete pessoas distintas, sem funções 
- Para um retângulo de lados x e x+3 e área 40 unidades quadradas, a equação x²+3x−40=0 ofer
- Se dez estudantes obtiveram média 6 e vinte estudantes obtiveram média 9, a média conjunta
- Um reservatório com volume de 1 metro cúbico comporta exatamente 100 litros, pois a conver
- Somando as matrizes 2×2 A=[[1,2],[3,4]] e B=[[5,6],[7,8]], obtém-se a matriz [[6,8],[10,12
- Um indicador que sobe de 40% para 50% aumenta dez pontos percentuais e apresenta variação 
```

## Pedido 42: Orçamento público e princípios orçamentários (`afo.orcamento-publico-principios`, 1137 tópicos de edital)

```
Matéria: afo.orcamento-publico-principios (Orçamento público e princípios orçamentários)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, funções, história e natureza jurídica do orçamento público": 30
- "2. Organização constitucional, integração com planejamento e ciclo orçamentário": 14
- "3. Unidade, universalidade, anualidade, discriminação e orçamento bruto": 18
- "4. Exclusividade, não afetação, publicidade e transparência": 17
- "5. Técnicas atuais, controle de resultados e princípios contábeis relacionados": 21

Já existem (não repita):
- Uma proposta anual reúne previsão de arrecadação e autorização de gastos, acompanhadas da 
- Na formulação de um plano fiscal, o governo destina recursos à oferta de iluminação públic
- Uma programação transfere recursos para grupos socialmente vulneráveis com objetivo explíc
- Em determinado exercício, uma proposta apresenta as despesas principalmente por material, 
- A proposta de orçamento anual é iniciada na estrutura executiva e submetida à discussão e 
- Uma equipe confunde quatro fases do ciclo orçamentário com os meses de vigência do orçamen
- Um planejamento lista objetivos plurianuais, enquanto outro documento organiza diretrizes 
- Um ente reúne na mesma lei anual os orçamentos fiscal, de investimento das empresas abrang
- Um Município quer registrar no orçamento uma receita de serviço próprio e a respectiva des
- Uma unidade prevê 100 unidades de receita orçamentária e transferência obrigatória de 20 a
- Uma lei orçamentária reúne uma dotação única para 'qualquer gasto de pessoal, materiais, o
- Na preparação da LOA, pretende-se incluir norma permanente que reorganiza setores administ
- Um projeto de LOA contém, além das previsões e dotações, autorização para abrir créditos s
- Um projeto de lei ordinária pretende vincular parte da receita de imposto a fundo de final
- O ente publica apenas o texto da lei orçamentária em meio de difícil acesso e recusa divul
- Para planejar uma ação pública, o gestor decide comparar alternativas de intervenção, esti
- Uma programação associa dotações a ações, objetivos e metas de expansão de determinado ser
- Em debate sobre a natureza da LOA, afirma-se que toda dotação aprovada pode ser ignorada l
- Uma ação exigirá obras durante três anos; o gestor conclui que a LOA pode passar a vigorar
- Uma lei de orçamento contempla receitas regulares, mas não inclui caução de terceiro trans
- A lei vincula receita de um imposto à manutenção do ensino, nos termos da previsão constit
- Uma proposta contém autorização de operação de crédito por antecipação de receita e, ao me
- Um orçamento de resultados relata que foram compradas duzentas máquinas, mas nada informa 
- Em estudo de princípios, o candidato afirma que a competência patrimonial determina o exer
- Um gestor propõe orçamento base zero para um programa cuja dotação anterior era 100. Sua e
- Um orçamento direciona recursos à expansão de determinado serviço coletivo. Trata-se de ex
- O orçamento-programa se caracteriza principalmente por organizar gastos por material, pess
- No modelo orçamentário misto adotado no Brasil, os projetos das leis orçamentárias são de 
- Como a Constituição prevê três componentes para a lei orçamentária anual, é necessário apr
- O exercício financeiro brasileiro coincide com o ano civil, de 1º de janeiro a 31 de dezem
- Para obedecer ao princípio do orçamento bruto, um ente pode deduzir transferências obrigat
- O art. 165, § 8º, da Constituição admite, como ressalva ao princípio da exclusividade, que
- Uma proposta de orçamento exclui determinada despesa financiada por receita própria, alega
- A ausência de registro de caução restituível, corretamente classificada como entrada compe
- Pelo princípio da especificação, toda dotação global é permitida sem ressalvas, pois a Lei
- O art. 167, IV, da Constituição veda, em regra, a vinculação de receita de impostos a órgã
- O art. 48 da LRF admite que a publicidade da lei orçamentária, por si só, substitua a divu
- A proposta anual deve ser elaborada de forma compatível com o planejamento plurianual, com
- A previsão do dever de executar programações orçamentárias no art. 165, § 10, da Constitui
- O orçamento público pode ter funções alocativa, distributiva e estabilizadora ao mesmo tem
- A existência de programa público com duração de três anos autoriza, por si só, tratar a LO
- Uma lei que direciona recursos de impostos à manutenção e desenvolvimento do ensino, nos t
- Se a LOA autoriza contratação de operação de crédito por antecipação de receita, pode tamb
- No orçamento por resultados, a quantidade de unidades entregues é um indicador de produto,
- Os princípios de entidade, continuidade, oportunidade, registro pelo valor original, compe
```

## Pedido 43: Equivalências e negações lógicas (`matematica.equivalencias-negacoes-logicas`, 1124 tópicos de edital)

```
Matéria: matematica.equivalencias-negacoes-logicas (Equivalências e negações lógicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Proposições, negação e tabelas-verdade dos conectivos": 14
- "2. Leis de De Morgan, equivalências algébricas e diagramas": 44
- "3. Condicional, contraposição, recíproca e bicondicional": 14
- "4. Implicação lógica, equivalência semântica e inferências válidas": 14
- "5. Tautologia, contradição, contingência e problemas integrados": 14

Já existem (não repita):
- Considere a afirmação: 'A câmera está ligada e o alarme está ativo'. Qual frase é sua nega
- Considere: 'O relatório foi enviado ou a cópia foi arquivada', com 'ou' inclusivo. Qual é 
- Em uma tabela, p é falso e q é verdadeiro. Qual é o valor da proposição 'se p, então q'?
- Uma regra afirma: 'A etapa é concluída se, e somente se, o arquivo é validado'. Se ambas a
- Ao selecionar 'chá ou café, mas não ambos', uma pessoa recebe os dois itens. Qual conclusã
- Considere 'A sala está aberta'. Qual frase expressa a dupla negação sem alterar o valor da
- Uma instrução diz: 'Se a chave está cadastrada, então o sistema permite acesso'. Qual é a 
- A frase 'Se o pedido é deferido, o comprovante é emitido' foi negada. Qual ocorrência torn
- Considere o enunciado 'Não é verdade que o contrato foi assinado e (a taxa foi paga ou a c
- Uma fórmula diz: 'O registro está correto ou (o registro está correto e o arquivo está ass
- Em um texto, lê-se 'O acesso é liberado somente se a identidade é validada'. Qual relação 
- Uma frase afirma 'O acesso ocorre se, e somente se, o token é válido'. Qual situação torna
- Em uma tabela, sabe-se que p é verdadeiro e q é falso. Como fica o par 'p e q' em comparaç
- Das duas afirmações 'O sensor está ativo e a rede funciona' e 'O sensor está ativo', qual 
- Foi estabelecido: 'Se a verificação é concluída, o recibo é emitido'. Sabe-se que a verifi
- Um painel informa 'A chave foi identificada ou (o sensor está ativo e a rede funciona)'. Q
- Para refutar a afirmação 'A frase p é logicamente equivalente à frase p e q', que valores 
- Considere a fórmula 'p ou não p' em um controle de decisão, independentemente do valor atr
- Uma fórmula de controle é 'Se o alarme toca, o sensor falhou', com as duas proposições ind
- Um regulamento enuncia 'O acesso ocorre se, e somente se, o cartão é válido'. A negação de
- Um aviso diz: 'Se o aparelho superaquece, a proteção desliga o motor'. Sabe-se que a prote
- Uma pessoa afirma que a fórmula 'p e q' implica logicamente 'p ou q'. Como essa relação po
- Considere a negação de 'p ou (q e r)'. Qual enunciado é equivalente após aplicação correta
- A afirmação 'Se p, então q' foi comparada com 'Se não p, então não q'. Qual diagnóstico ló
- Em uma tabela-verdade, qual classificação cabe à fórmula 'Se (se p então q, e p), então q'
- Para negar a frase 'O arquivo está salvo e a cópia está íntegra', basta dizer 'O arquivo n
- A negação de 'O servidor funciona ou a rede está ativa', considerando o 'ou' inclusivo, é 
- Quando p é falso e q é falso, a condicional 'se p então q' é verdadeira na lógica proposic
- A expressão 'p ou q' tem valor falso quando p e q são ambos verdadeiros, pois a disjunção 
- Da frase 'Se o pagamento foi aprovado, então o comprovante existe', obtém-se contrapositiv
- A frase 'p se, e somente se, q' é falsa quando p e q são ambos falsos, porque nenhuma das 
- A proposição 'p e não p', entendendo p com o mesmo sentido e momento, é uma contradição, p
- Se uma fórmula é verdadeira na linha em que p e q são verdadeiros, isso basta para chamá-l
- A fórmula 'p e q' implica logicamente 'p', mas não é equivalente a 'p' para todas as atrib
- Da condicional 'se p então q' e da premissa 'q é verdadeiro' conclui-se validamente que 'p
- A equivalência entre 'se p então q' e 'não p ou q' vale para as quatro atribuições, inclus
- A negação da bicondicional 'p se, e somente se, q' é simplesmente a conjunção 'não p e não
- Duas expressões proposicionais são equivalentes quando sua bicondicional é uma tautologia,
- Se 'p e q' implica 'p', então as duas fórmulas são necessariamente equivalentes, porque um
- A negação de uma tautologia é uma contradição, e a negação de uma contingência continua se
- A expressão 'p ou q', com 'ou' inclusivo e p, q independentes, é uma tautologia porque o c
- A regra modus tollens permite concluir que p é falso a partir de 'se p então q' e da infor
- A negação de 'exatamente um entre p e q é verdadeiro' exige exatamente uma parcela falsa, 
- Uma expressão composta por três letras proposicionais independentes exige oito linhas em u
- A negação de 'p e (q ou r)' corresponde a 'não p ou não q ou não r', pois De Morgan transf
```

## Pedido 44: Probabilidade (`matematica.probabilidade`, 1124 tópicos de edital)

```
Matéria: matematica.probabilidade (Probabilidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Experimentos aleatórios, espaço amostral e axiomas": 23
- "2. Complemento, união, interseção e diagramas de Venn": 18
- "3. Probabilidade condicional, independência e tabelas": 26
- "4. Contagem, reposição e amostragens sucessivas": 17
- "5. Probabilidade total, Bayes e resolução integrada": 16

Já existem (não repita):
- Um dado honesto de seis faces é lançado uma vez. Qual é a probabilidade de aparecer um núm
- Duas moedas honestas são lançadas, considerando a ordem dos resultados. Qual é a chance de
- Uma caixa contém 7 fichas verdes e 3 amarelas. Sorteia-se uma ficha uniformemente. Qual é 
- Dois dados honestos distinguíveis são lançados. Em quantos dos 36 pares ordenados possívei
- Três moedas honestas são lançadas independentemente. Qual é a chance de sair pelo menos um
- Em um cadastro, 35% dos registros têm A, 45% têm B e 15% possuem ambos. Qual a chance de u
- Em 100 fichas, 60 apresentam A, 50 apresentam B e 20 apresentam ambas. Qual é a chance de 
- Em um experimento, A e B são mutuamente exclusivos. As probabilidades individuais são 20% 
- O histórico de um processo indica chance de atraso de 8%. Segundo esse modelo, qual é a ch
- Em um levantamento, 45% possuem A, 30% possuem B e 10% possuem ambas. Qual a chance de pos
- De 100 registros, 40 têm A, 30 têm B e 12 têm ambas. Seleciona-se um registro entre os que
- Em 120 fichas, 48 são A e 36 são B; 24 têm as duas marcas. Escolhendo apenas entre as fich
- Dois eventos têm probabilidades 40% e 30%, e a interseção tem chance 12%. Qual conclusão é
- Uma urna possui 5 esferas vermelhas e 3 azuis. Duas são retiradas sem reposição. Qual é a 
- Uma moeda honesta é lançada duas vezes, independentemente. Qual é a chance de sair cara pr
- Em uma lista com 8 candidatos distintos, duas pessoas serão escolhidas simultaneamente, se
- Uma caixa contém 5 bolas vermelhas e 4 azuis. Retiram-se duas sem reposição. Qual a chance
- Em quatro lançamentos independentes de moeda justa, qual é a chance de obter exatamente du
- Uma caixa contém 7 fichas vermelhas e 5 azuis. Retiram-se três, uma a uma, sem reposição. 
- De uma caixa com 4 fichas especiais e 6 comuns, retiram-se duas sem reposição. Qual é a ch
- A linha A produz 75% dos itens, com 2% de defeituosos. A linha B produz 25%, com 8% de def
- Uma linha produz 60% dos itens e apresenta 5% de defeitos; outra produz 40% e apresenta 10
- Em certa população, 10% pertencem ao grupo A; 90% dos A têm sinal positivo, mas 20% dos de
- Três unidades produzem 40%, 35% e 25% do total e têm taxas de erro, respectivamente, 5%, 1
- Uma urna possui 4 bolas vermelhas e 6 azuis. Retiram-se duas simultaneamente. Sabendo que 
- Ao lançar um dado comum de seis faces, o evento “obter o número oito” é impossível e possu
- Uma caixa tem sete fichas de uma cor e uma de outra; por existirem apenas duas cores, a pr
- Se a probabilidade de ocorrer determinado evento é 35%, a probabilidade de ele não ocorrer
- No lançamento de dois dados justos, as onze somas possíveis, de 2 até 12, são igualmente p
- Se dois eventos não podem ocorrer simultaneamente, a probabilidade da união é a soma das r
- A probabilidade da interseção de quaisquer dois eventos pode ser obtida multiplicando dire
- Se A tem chance 40%, B tem chance 30% e a ocorrência simultânea tem chance 20%, a probabil
- Em quatro lançamentos de moeda, o evento “no máximo uma cara” corresponde aos resultados c
- Se todos os resultados de A também pertencem a B, então a união de A com B é o próprio B.
- Para calcular a probabilidade de A dado B, deve-se dividir a chance de A e B pela chance t
- Dois eventos com probabilidades positivas podem ser simultaneamente independentes e mutuam
- Dois eventos têm probabilidades 50% e 20% e interseção de 10%; nessa situação, são indepen
- Numa urna com cinco vermelhas e três azuis, a chance de retirar duas vermelhas sem reposiç
- Numa urna com quatro bolas vermelhas e quatro azuis, duas retiradas com reposição dão chan
- Em quatro lançamentos independentes de moeda honesta, existem seis sequências com exatamen
- A escolha simultânea de duas pessoas distintas entre seis pessoas produz quinze grupos pos
- Conhecer a chance de uma falha ocorrer quando o item veio da linha A é suficiente para con
- Se 70% dos itens vêm de uma linha com 1% de falhas e 30% vêm de outra com 5% de falhas, a 
- A independência de três eventos está demonstrada assim que se comprovam as três independên
- Duas linhas produzem 90% e 10% dos itens, com taxas de falha de 1% e 9%; sabendo que houve
```

## Pedido 45: Aplicabilidade e interpretação das normas constitucionais (`direito-constitucional.aplicabilidade-normas-constitucionais`, 1108 tópicos de edital)

```
Matéria: direito-constitucional.aplicabilidade-normas-constitucionais (Aplicabilidade e interpretação das normas constitucionais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Norma constitucional, supremacia, preâmbulo e ADCT": 11
- "2. Eficácia plena, contida e limitada: efeitos jurídicos e aplicabilidade": 30
- "3. Vigência, efetividade, normas programáticas e direitos fundamentais": 30
- "4. Interpretação constitucional: métodos, princípios e colisões": 18
- "5. Recepção, não recepção, desconstitucionalização e aplicação integrada": 11

Já existem (não repita):
- Uma regra constitucional proíbe submeter pessoas a tortura ou tratamento desumano, sem agu
- Uma pessoa pretende exercer profissão cuja liberdade é prevista no art. 5º, XIII, da Const
- O art. 37, VII, da Constituição prevê exercício do direito de greve nos termos e nos limit
- Uma política pública é avaliada à luz do objetivo de reduzir desigualdades sociais e regio
- Ao elaborar uma Constituição local, não foi incluída a invocação religiosa presente no pre
- Uma disposição do ADCT fixa transição entre dois regimes constitucionais. Um parecer a con
- Uma norma que define direito fundamental precisa ser interpretada em 2026. Qual comando ex
- Uma lei ordinária posterior à Constituição contraria uma regra constitucional aplicável. Q
- Uma norma constitucional exige lei integradora para operacionalizar plenamente nova estrut
- Uma associação privada exclui integrante sem oportunidade de defesa em contexto relevante.
- Dois dispositivos constitucionais originários parecem tensionar valores relevantes num cas
- Um parecer afirma que a liberdade profissional somente começa a existir quando lei estabel
- Para interpretar expressão constitucional ambígua, a análise considera o sentido das palav
- Uma lei vigente admite dois sentidos juridicamente defensáveis; um respeita a Constituição
- Uma lei de 1970 foi editada validamente sob a Constituição então vigente. Em 1988, verific
- Uma análise histórica examina a Lei nº 5.250/1967, cujo conteúdo foi considerado incompatí
- Duas normas da redação originária da Constituição de 1988 parecem incompatíveis. Propõe-se
- Uma lei ordinária anterior à Constituição de 1988, validamente editada, regula matéria que
- Uma medida limita dois direitos constitucionais em tensão. Constatou-se meio apto ao fim, 
- Uma teoria de interpretação constitucional considera o texto como programa normativo e a r
- O Código Tributário Nacional foi editado como Lei nº 5.172/1966, mas exerce disciplina de 
- Ao substituir uma Constituição por outra, cogita-se manter dispositivos da Constituição an
- A compreensão social de uma garantia constitucional passa a abranger novas situações tecno
- Um parecer afirma que o dever constitucional relativo à educação é apenas programático e, 
- Uma relação contratual privada envolve restrição a direito fundamental e debate sobre auto
- A norma do art. 5º, III, da Constituição, que veda tortura e tratamento desumano ou degrad
- A liberdade profissional do art. 5º, XIII, somente passa a existir após lei estabelecer qu
- Uma norma constitucional de eficácia limitada pode orientar a interpretação de outras norm
- O objetivo de reduzir desigualdades do art. 3º, III, da Constituição é mera sugestão polít
- No julgamento da ADI 2.076, assentou-se que a invocação da proteção de Deus no preâmbulo f
- As normas do ADCT têm natureza infraconstitucional porque cumprem função transitória, pode
- O procedimento de reforma mais exigente previsto no art. 60 da Constituição contribui para
- Por força do art. 5º, § 1º, todas as normas definidoras de direitos fundamentais devem ser
- A incidência de direitos fundamentais em relações entre particulares é conhecida como efic
- Uma lei editada validamente antes da Constituição atual deve ser afastada por não recepção
- O método jurídico clássico de interpretação constitucional pode examinar linguagem, histór
- Mesmo quando uma lei possui sentido único claramente incompatível com a Constituição, a in
- Vigência e efetividade não são expressões sinônimas: uma norma constitucional pode estar e
- Conflito aparente entre duas normas constitucionais originárias pode ser resolvido declara
- Se há alternativa igualmente eficaz e menos restritiva para atingir um fim legítimo, a esc
- Uma disposição do ADCT que fixa condições de transição perde sua natureza constitucional p
- A recepção pode preservar, com estatura de lei complementar, legislação ordinária anterior
- Promulgada nova Constituição, todos os artigos da Constituição anterior que não forem repr
- A mutação constitucional pode alterar o alcance interpretativo de uma disposição, sem modi
- Uma norma de eficácia limitada deixa de ter qualquer força normativa se a lei que a comple
```

## Pedido 46: Saneamento básico, resíduos sólidos e poluição (`direito-ambiental.saneamento-residuos-poluicao`, 1089 tópicos de edital)

```
Matéria: direito-ambiental.saneamento-residuos-poluicao (Saneamento básico, resíduos sólidos e poluição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos do saneamento e seus quatro componentes": 18
- "2. Titularidade, regionalização, contratos e regulação": 18
- "3. Política Nacional de Resíduos Sólidos e logística reversa": 22
- "4. Poluição hídrica, atmosférica e por substâncias perigosas": 22
- "5. Recursos minerais, zonas marítimas e integração das políticas": 20

Já existem (não repita):
- Uma cidade incluiu em seu plano galerias para reter picos de chuva e reduzir alagamentos. 
- Uma concessão de água e esgoto prevê metas gerais de universalização para 31 de dezembro d
- Um gestor afirma que saneamento básico se limita à água tratada e ao esgotamento. Ao avali
- Uma cidade construiu apenas redes de coleta de esgoto, mas segue lançando todo o material 
- Ao comparar redes de água e empresas concorrentes, um estudo observa investimento fixo ele
- Um município pretende delegar a operação ordinária da rede de água a empresa privada que n
- Uma usuária reclama de descumprimento da qualidade contratual pela concessionária local e 
- Uma empresa estatal possui contrato de programa regular firmado antes do novo marco de 202
- Uma rede de tratamento de esgoto atende dois municípios conurbados, configurando serviço d
- Um contrato de saneamento precisará contemplar a meta nacional de tratamento de esgotos at
- Em uma unidade de triagem, há papel limpo reutilizável, garrafas recicláveis e material se
- A administração municipal reorganiza o tratamento de resíduos após estudo técnico. Qual se
- Uma rede de comércio de pilhas sustenta que o serviço de coleta de lixo doméstico deve arc
- Um empreendimento que gera resíduos industriais perigosos defende que o plano municipal de
- Em auditoria de gestão urbana, avalia-se o art. 54 da PNRS e os prazos introduzidos pela L
- Uma empresa pública libera contaminantes que danificam propriedades vizinhas. Na ação de r
- Uma fábrica provoca poluição hídrica em nível que torna necessária a interrupção do abaste
- Em inspeção de uma chaminé industrial, medem-se a massa lançada pela fonte e a concentraçã
- Um navio pretende descarregar mistura oleosa em águas sob jurisdição nacional por conveniê
- Uma indústria abandona tambores com resíduos perigosos e sustenta que, por não haver prova
- O proprietário de uma fazenda encontra jazida mineral e afirma que, por deter a matrícula 
- Após encerrar a lavra, uma empresa deixa a área degradada apesar das determinações técnica
- Uma questão aponta propriedade da União sobre cavidades naturais subterrâneas, potencial h
- Uma instalação industrial capta água bruta de rio e depois distribui água tratada aos empr
- Durante estiagem crítica, uma autoridade precisa ordenar prioridades de uso de água segund
- Uma prefeitura incluiu no plano de saneamento abastecimento de água, coleta de esgoto, man
- O plano de um serviço de esgotamento sanitário considera alcançada a meta de 90% do art. 1
- Diante de elevadas economias de escala nas redes de água, o conceito de monopólio natural 
- Uma localidade exposta a alagamentos pode tratar a construção e manutenção de sistemas de 
- Ao delegar a um terceiro estranho à sua administração a prestação de saneamento, o titular
- Uma agência estadual de saneamento que regule a tarifa de um município passa, por esse úni
- A Norma de Referência ANA nº 4/2024 aborda práticas de governança das entidades reguladora
- Os contratos de programa de saneamento regulares e vigentes quando da reforma de 2020 fora
- Uma unidade industrial pode produzir resíduos sujeitos a gerenciamento próprio, enquanto o
- Como os resíduos recicláveis já foram descartados, a Lei nº 12.305/2010 manda tratá-los co
- A recuperação energética de resíduos sólidos urbanos tem prioridade legal sobre a não gera
- A obrigação empresarial de estruturar logística reversa de pilhas e baterias subsiste mesm
- Uma autarquia que lança efluentes contaminantes pode enquadrar-se no conceito legal de pol
- Para condenar civilmente um poluidor à reparação ambiental, o art. 14, § 1º, da Lei nº 6.9
- Uma substância descartada sem causar mortandade imediata de animais não pode, por esse úni
- A interrupção necessária do abastecimento público por poluição hídrica é circunstância exp
- Uma autorização federal de pesquisa mineral implica, por si, dispensa da recuperação ambie
- Embora uma cavidade natural subterrânea esteja sob imóvel particular, ela integra a relaçã
- A Lei nº 9.433/1997 considera a bacia hidrográfica unidade territorial da política hídrica
- A exploração de jazida mineral por proprietário do solo está dispensada de título minerári
```

## Pedido 47: Funções administrativas: planejar, organizar, dirigir e controlar (`administracao-geral.funcoes-administrativas`, 1087 tópicos de edital)

```
Matéria: administracao-geral.funcoes-administrativas (Funções administrativas: planejar, organizar, dirigir e controlar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Evolução da administração, organizações e papéis gerenciais": 22
- "2. Planejamento, objetivos, estratégia e administração por objetivos": 16
- "3. Organização formal, estruturas, departamentalização e cultura": 26
- "4. Direção, motivação, comunicação e tomada de decisão": 20
- "5. Controle, indicadores e integração do processo administrativo": 16

Já existem (não repita):
- Uma equipe define como objetivo reduzir o tempo médio de resposta em 20% no próximo trimes
- Uma unidade cumpriu a meta de 400 atendimentos, mas precisou empregar 25% mais horas do qu
- Uma empresa estuda tempos de execução, padroniza ferramentas e separa a análise dos método
- Uma organização revisa seus princípios de unidade de comando, coordenação, autoridade e di
- Ao preparar a direção de uma organização, uma pessoa precisa perceber relações entre decis
- Uma cooperativa registra como propósito fornecer alimentos acessíveis e como aspiração tor
- Na análise SWOT, uma equipe experiente, computadores desatualizados, crescimento da demand
- Em um desenho organizacional, especialistas aconselham gestores operacionais, mas o comand
- O responsável acompanha a execução de um serviço e corrige a distribuição das tarefas ante
- Uma direção pactua metas articuladas com cada área, acompanha indicadores e realiza revisõ
- Uma organização reúne analistas de orçamento em uma unidade, técnicos de operações em outr
- Um engenheiro responde ao gestor de engenharia por conhecimentos e carreira, mas também ao
- Uma instituição exibe cartazes sobre transparência, anuncia esse princípio no manual inter
- Após melhorar os equipamentos e a ventilação, uma equipe deixa de reclamar das condições d
- Um gestor supõe que as pessoas podem assumir responsabilidades e exercer autocontrole quan
- Uma pessoa valoriza uma recompensa e acredita ser possível desempenhar bem, mas considera 
- Uma unidade possui protocolo estável para autorização de reposição de materiais comuns, ma
- Durante uma crise, uma gestora media conflito entre equipes; depois, distribui parte do or
- Duas unidades têm objetivos semelhantes: uma opera processo padronizado em ambiente estáve
- Uma coordenação concede à equipe local autoridade para decidir ajustes de escala, define l
- Uma organização cria indicadores de capacitação, erros de processo, satisfação dos usuário
- Após verificar índice de retrabalho de 11% contra padrão de 4%, a gestão investiga causas 
- Uma equipe deseja reunir avaliações de especialistas dispersos, em rodadas anônimas com de
- Uma iniciativa concluiu todos os formulários previstos e reduziu custos por formulário, ma
- Uma organização combina regras para atividades sensíveis, comparações de resultados entre 
- Uma associação com finalidade social estabelece responsáveis, unidades, regras e mecanismo
- Uma unidade que formaliza competências, hierarquia e procedimentos aplica necessariamente 
- Depois de fixada uma orientação geral para a organização, a unidade regional elabora seu p
- Quando uma organização enuncia a situação futura que deseja alcançar, ela define a missão;
- Uma coordenação envia instruções e pede que os destinatários expliquem o que compreenderam
- Ao encerrar um período e revisar o desempenho consolidado, uma gerência realiza controle c
- Dois setores com linhas formais de chefia distintas mantêm uma rede espontânea de troca de
- Uma organização que reúne contabilidade, compras e atendimento em unidades especializadas 
- Ao aumentar de cinco para nove o número de subordinados diretos de uma chefia, a organizaç
- Um gestor transfere formalmente poder para decidir compras até certo limite. Por isso, per
- Quando uma abordagem administrativa enfatiza pertencimento e grupos, ela se aproxima da vi
- Sob racionalidade limitada, o decisor conhece integralmente as alternativas e escolhe o má
- Uma meta mal definida compromete os indicadores usados no controle e pode produzir revisõe
- Ao negociar um contrato, um gestor exerce habilidade humana de Katz, o que significa que n
- Uma pessoa com necessidades básicas razoavelmente atendidas busca reconhecimento e crescim
- Ao adotar pressupostos da Teoria Y, uma chefia garante por definição que todas as pessoas 
- Uma pesquisa mostra clima de confiança em um período, mas práticas reiteradas de punição a
- Um painel que mede exclusivamente despesas e receitas já representa, pela mera presença de
- Em uma atividade com risco de erro irreversível, uma checagem anterior à execução pode ser
- Uma árvore de decisão que apresenta probabilidades estimadas para diferentes eventos elimi
```

## Pedido 48: Internet: busca, redes sociais, grupos de discussão e wikis (`informatica.internet-busca-redes-sociais`, 1082 tópicos de edital)

```
Matéria: informatica.internet-busca-redes-sociais (Internet: busca, redes sociais, grupos de discussão e wikis)
Capítulos e quantas questões quero em cada um (total 100):
- "Arquitetura dos mecanismos de busca, camadas da Web e indexação": 20
- "Operadores de pesquisa avançada e sintaxe de consulta": 20
- "Redes sociais, mensageiros instantâneos e dinâmica algorítmica": 28
- "Grupos de discussão, fóruns, wikis e comunicação síncrona vs. assíncrona": 20
- "Segurança em redes sociais, desinformação, privacidade e legislação aplicada": 12

Já existem (não repita):
- Um usuário quer encontrar páginas que contenham exatamente a sequência de palavras control
- Qual operador de pesquisa do Google limita os resultados especificamente a arquivos de um 
- Qual rede social tem como finalidade principal o relacionamento profissional e corporativo
- Qual ferramenta web permite que os próprios usuários criem, editem e interliguem páginas d
- Uma pessoa recebe no mensageiro uma mensagem de um contato pedindo que informe o código de
- Na pesquisa jaguar -carro realizada no Google, qual é a função exata desempenhada pelo sin
- Em redes sociais como X (Twitter), Instagram e LinkedIn, qual é a função técnica de uma ha
- Como se denomina a fraude cibernética que utiliza páginas falsas, links patrocinados engan
- Um servidor público deseja encontrar arquivos exclusivamente em formato PDF que contenham 
- Qual alternativa descreve com precisão a arquitetura de funcionamento de um mecanismo de b
- Em uma lista de discussão baseada em correio eletrônico (mailing list), o que ocorre quand
- Assinale a alternativa que apresenta um exemplo característico de comunicação síncrona na 
- Qual expressão de busca no Google restringe os resultados às páginas que contenham obrigat
- Qual medida de segurança é a mais eficaz para impedir que um invasor acesse uma conta de r
- Qual característica estrutural distingue um fórum de discussão na web de uma lista de disc
- Como é chamado o fenômeno em que os algoritmos de recomendação das redes sociais personali
- Considere a seguinte pesquisa avançada no Google: "tribunal de contas" relatório OR parece
- Acerca das camadas da internet conhecidas como Surface Web, Deep Web e Dark Web, assinale 
- Um candidato digita a expressão concurso or seleção (com a palavra or em letras minúsculas
- Um usuário recebe em um grupo de mensageiro a fotografia dramática de uma ponte caída, aco
- Qual das pesquisas abaixo emprega corretamente o operador curinga asterisco (*) conforme s
- Uma página da wiki corporativa de um órgão público foi editada equivocadamente por um esta
- Assinale a alternativa que descreve um incidente baseado em engenharia social, e não em fa
- Ao pesquisar pelo nome de seu banco em um mecanismo de busca, o usuário observa no topo da
- Assinale a alternativa que apresenta uma associação tecnicamente correta entre a plataform
- No Google, colocar uma expressão entre aspas duplas faz o mecanismo de busca procurar exat
- Navegador web e mecanismo de busca são conceitos equivalentes: o Google Chrome e o Google 
- Em redes sociais, o uso do símbolo arroba seguido do identificador de uma conta (@usuario)
- Segundo as regras de netiqueta aplicáveis a fóruns, listas de discussão e redes sociais, r
- A ativação da verificação em duas etapas em uma rede social substitui o uso da senha tradi
- A Wikipédia é o exemplo mais conhecido de sistema wiki, caracterizando-se pela construção 
- O YouTube é uma plataforma voltada primordialmente ao compartilhamento e streaming de víde
- A pesquisa edital site:gov.br realizada no Google restringe os resultados a páginas perten
- Os mecanismos de busca modernos diferenciam letras maiúsculas de minúsculas nos termos pes
- Fóruns de discussão na web e listas de discussão por e-mail são ferramentas típicas de com
- Configurar um perfil de rede social como privado garante tecnicamente que nenhuma foto ou 
- Programas automatizados conhecidos como robôs, crawlers ou spiders percorrem a web seguind
- Por desaparecerem automaticamente do perfil após 24 horas, os Stories do Instagram impedem
- Em uma lista de discussão configurada como moderada, as mensagens enviadas pelos participa
- A Deep Web é composta exclusivamente por sites dedicados a atividades ilícitas que exigem 
- No Google, digitar a expressão site: gov.br edital (com espaço em branco logo após os dois
- Manter o software antivírus atualizado no smartphone é suficiente para impedir golpes de e
- O Marco Civil da Internet (Lei nº 12.965/2014) estabelece princípios, garantias, direitos 
- Por definição técnica, todo sistema wiki permite obrigatoriamente que qualquer visitante a
- No mecanismo de busca Google, a pesquisa concurso | seleção (utilizando o caractere barra 
- Dentre as opções abaixo, assinale aquela que apresenta exclusivamente mecanismos de busca 
- Qual operador do Google deve ser utilizado por um usuário que deseja encontrar páginas que
- No vocabulário da netiqueta e dos grupos de discussão, como é chamado o ato de enviar uma 
- Qual plataforma caracteriza-se historicamente como um serviço de microblogging, voltado a 
- Qual é a diferença técnica entre as pesquisas intitle:edital concurso e allintitle:edital 
- Um órgão público publicou um vídeo institucional no YouTube e deseja enviá-lo por link aos
- Um coordenador criou uma Lista de Transmissão no WhatsApp com 50 números de telefone de se
- Um analista deseja pesquisar no Google relatórios sobre compras públicas publicados entre 
- À luz do Marco Civil da Internet (Lei nº 12.965/2014), como funciona a responsabilidade ci
- O administrador de um portal público inseriu um arquivo robots.txt na raiz do site instrui
- No WhatsApp, as mensagens e chamadas em conversas individuais e em grupos contam com cript
- No Google, o operador related: (como em related:planalto.gov.br) é utilizado para localiza
- Em comunidades online e grupos de discussão, o termo lurker designa o participante mal-int
- No aplicativo Telegram, todas as conversas comuns sincronizadas na nuvem utilizam criptogr
- Caso um usuário esteja com a verificação em duas etapas (PIN de 6 dígitos) ativa no WhatsA
```

## Pedido 49: Administração de materiais, patrimônio e logística (`administracao-geral.administracao-materiais-logistica`, 1082 tópicos de edital)

```
Matéria: administracao-geral.administracao-materiais-logistica (Administração de materiais, patrimônio e logística)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Administração de materiais e classificação": 17
- "2. Estoques, previsão e custos": 18
- "3. Compras, armazenagem e inventários": 27
- "4. Patrimônio, imóveis e manutenção": 22
- "5. Logística, suprimentos e sistemas integrados": 16

Já existem (não repita):
- Uma unidade adquire um equipamento durável para uso independente por cinco anos, sem fragi
- Para elaborar curva ABC, um gestor dispõe das quantidades consumidas no ano e dos preços u
- Uma peça é consumida a 20 unidades por dia, tem prazo de reposição de oito dias e estoque 
- O almoxarifado recebeu caixas de equipamentos, mas a equipe técnica ainda não examinou sua
- Um prédio de uma autarquia é utilizado diretamente para prestar serviço público. Na classi
- Para distribuir medicamentos entre locais próximos, uma unidade pretende realizar entrega 
- Uma máquina funciona, mas sua manutenção se tornou onerosa e seu desempenho é baixo devido
- Uma unidade pública deseja adquirir papel de características usuais e facilmente especific
- Um componente barato pode paralisar a operação se faltar, pois não há substituto imediato.
- Uma organização confere seletivamente materiais ao longo de todo o ano, seguindo cronogram
- Um item apresenta demanda anual de 2.400 unidades, custo por pedido de R$ 50 e manutenção 
- O consumo mensal observado de determinado item foi 90, 120 e 150 unidades nos três últimos
- Um estoque recebeu dez unidades a R$ 10 e depois dez a R$ 14. Havendo saída de 12 unidades
- Uma unidade requisitante recebe produtos de limpeza em quantidade e data previstas em tabe
- Um cadastro de fornecedor registra documentos e histórico de entrega. Qual interpretação s
- Uma unidade planeja contratar materiais repetidamente e conclui procedimento para registro
- Uma instalação utiliza sensores para monitorar vibração e temperatura, intervindo antes de
- Uma equipe federal pretende, em outubro de 2026, atualizar dados de imóvel antes controlad
- Um inventário rotativo comparou 100 registros de materiais com as quantidades físicas corr
- Um processo puxado consome 100 peças por dia; reposição demora dois dias; reserva de segur
- Uma entidade pública pretende alienar imóvel dominical e não identificou hipótese legal de
- Um plano MRP prevê dez conjuntos, cada um consumindo três peças X. Há oito peças utilizáve
- A equipe analisa imóvel público com RIP administrativo atualizado, mas encontra divergênci
- Uma equipe deve diferenciar sistema de revisão contínua de sistema periódico para item de 
- Vendas finais variam pouco, mas os pedidos ao fornecedor oscilam fortemente após promoções
- Uma equipe considera o valor do item e a quantidade anual efetivamente consumida para cons
- Um livro adquirido para biblioteca pública deve ser classificado como permanente simplesme
- Um item com consumo de dez unidades ao dia e reposição de cinco dias, sem necessidade de r
- A entrega física de material ao almoxarifado representa aceitação definitiva da qualidade 
- O fornecimento por requisição depende de solicitação da unidade usuária, enquanto o fornec
- Uma unidade adquire bens comuns de maneira repetida; o sistema de registro de preços, por 
- Em revisão contínua de estoques, atingir o ponto de pedido pode disparar reposição, enquan
- Um equipamento permanece na mesma instituição, mas muda de setor e responsável; por não te
- Para prever demanda com média móvel simples de três períodos de 100, 140 e 180 unidades, o
- O SPIUnet continuou sendo, em outubro de 2026, a plataforma operacional exclusiva para atu
- A classificação jurídica de bens de uso comum, especial e dominicais no Código Civil recai
- A matrícula cartorial de imóvel público corresponde ao mesmo instrumento que o RIP adminis
- No exercício gerencial de entradas de dez itens a R$ 10 e dez a R$ 14, uma saída de doze i
- Como o imóvel dominical não está afetado a uso comum ou especial, sua alienação dispensa p
- Uma unidade recebeu dez conjuntos a produzir, que exigem três peças cada; com oito peças d
- Em transporte multimodal sob a Lei 9.611/1998, vários modos podem integrar operação contra
- Um equipamento apresentou cinco falhas em 1.000 horas de funcionamento e recebeu 20 horas 
- Na Lei 12.305/2010, a logística reversa de categorias sujeitas ao art. 33 envolve responsa
- Na gestão de estoques, um item que possui alta criticidade de ruptura deve necessariamente
- No modelo de cartões kanban, com demanda de 100 unidades ao dia, prazo de dois dias, segur
```

## Pedido 50: Penas: espécies, aplicação, regimes, sursis, livramento e efeitos da condenação (`direito-penal.penas`, 1082 tópicos de edital)

```
Matéria: direito-penal.penas (Penas: espécies, aplicação, regimes, sursis, livramento e efeitos da condenação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, espécies de pena, regimes e limites": 22
- "2. Penas alternativas e multa: requisitos, conversão e cálculo": 22
- "3. Dosimetria trifásica, circunstâncias e concurso de crimes": 25
- "4. Suspensão condicional da pena e livramento condicional": 16
- "5. Efeitos da condenação, reabilitação e medidas de segurança": 15

Já existem (não repita):
- Uma sentença classifica as sanções admitidas pelo Código Penal. Qual trio reproduz exatame
- Em julgamento, o magistrado precisa distinguir o regime inicial possível para detenção daq
- Pessoa não reincidente recebe cinco anos de reclusão com circunstâncias judiciais favoráve
- Uma pessoa acumula condenações cuja soma alcança 52 anos de privação de liberdade. Qual é,
- Uma condenação a pena privativa de liberdade foi precedida de prisão provisória regularmen
- Sem violência nem grave ameaça, um crime doloso resulta em pena de três anos e requisitos 
- Um condenado recebe pena de oito meses e preenche requisitos para substituição do art. 44.
- Na fixação de multa, a sentença escolhe quantidade de dias-multa. Qual intervalo geral est
- O juiz fixa vinte dias-multa e, por hipótese didática, 50 unidades monetárias por dia. Qua
- Uma pena-base foi fixada exatamente no mínimo legal. Na segunda fase aparece atenuante gen
- Para elevar a pena-base, a sentença invoca inquérito em andamento, sem condenação transita
- Ao calcular pena por três fases, um julgador pergunta qual sequência corresponde ao art. 6
- Uma pessoa pratica dois crimes distintos por meio de duas ações, e nenhum regime especial 
- Uma única ação produz dois crimes, sem desígnios autônomos, e deve-se aplicar a regra ordi
- Vários crimes da mesma espécie ocorrem por ações distintas sob condições de tempo, lugar e
- Uma pessoa recebe um ano e seis meses de pena privativa, com condições subjetivas favoráve
- Uma pessoa não reincidente em crime doloso, com bons antecedentes, cumpre pena privativa d
- Na segunda fase de condenação por crime que envolveu violência sexual contra mulher, o age
- Condenação por infração cuja pena máxima abstrata é oito anos de reclusão revela patrimôni
- Em crime cometido com abuso de poder e violação de dever perante a administração, aplica-s
- Após encerrada a execução de pena, uma pessoa satisfaz requisitos materiais para pedir rea
- Após reconhecimento da inimputabilidade penal, discute-se a medida de segurança do art. 97
- Condenado com mais de setenta anos recebeu três anos de pena privativa e atende às demais 
- Uma pessoa cumpre pena suficiente para livramento em termos de fração e duração, mas comet
- Por uma única conduta dolosa, o agente pratica dois crimes resultantes de desígnios autôno
- A Constituição permite pena de morte apenas na hipótese constitucional de guerra declarada
- A detenção pode ter como regime inicial o fechado quando o juiz considera elevado o desval
- Uma pena de reclusão superior a oito anos sujeita o condenado, na regra quantitativa do ar
- O limite de cumprimento de penas privativas de liberdade permanece em trinta anos, apesar 
- No sistema de dias-multa, vinte dias avaliados em cinquenta unidades monetárias por dia re
- A pena restritiva de limitação de fim de semana determina permanência obrigatória de cinco
- A primeira etapa da dosimetria trifásica fixa a pena-base segundo o art. 59, antes de agra
- A pena privativa de liberdade de dois anos, substituível nos termos do art. 44, deve receb
- A multa, após trânsito em julgado, é dívida de valor executada no juízo da execução penal,
- O uso de ação penal ainda em andamento para justificar antecedentes desfavoráveis na pena-
- A condenação a dois anos e meio de privação de liberdade não se enquadra no limite comum d
- No concurso material do art. 69, uma ação e dois crimes levam à aplicação da pena mais gra
- A reabilitação do art. 93 assegura sigilo de registros sobre processo e condenação, embora
- Em crime envolvendo violência sexual contra mulher, a idade de vinte anos na data do fato 
- Condenado reincidente pode, nas condições do art. 44, § 3º, obter substituição por restrit
- A perda alargada de patrimônio prevista no art. 91-A exige que o juiz tenha aplicado efeti
- A regra geral do art. 92 de declaração motivada dos efeitos específicos da condenação comp
- O livramento condicional do art. 83 exige apenas que a pessoa tenha cumprido a fração de p
- Na disciplina do art. 97, a perícia sobre cessação de periculosidade deve ocorrer após o p
- Uma única ação dolosa que produza crimes distintos, praticados com desígnios autônomos, co
- Ao atirar contra seu desafeto, o agente erra a pontaria e atinge um terceiro que passava p
- No erro na execução (aberratio ictus), se o agente atinge também a pessoa que pretendia of
- Querendo danificar uma vidraça com uma pedra, o agente erra e fere uma pessoa. Pelo art. 7
- No erro sobre a pessoa, o agente confunde a vítima e atinge pessoa diversa da pretendida, 
```

