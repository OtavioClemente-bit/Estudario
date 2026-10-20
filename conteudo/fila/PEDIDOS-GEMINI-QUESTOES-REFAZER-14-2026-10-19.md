# Refazer: 14 pedidos de questões extras que voltaram com defeito (19/10/2026)

As outras 36 matérias já foram instaladas. Estas 14 o conferidor recusou ou tinham um vício que dá para acertar sem estudar. **Abra uma conversa NOVA no Gemini**, cole a Mensagem 0 e depois cada pedido (o defeito anterior está escrito no pedido para ele não repetir).

Regra extra que vale para todos: **a correta deve ter o MESMO tamanho das erradas** (nem a mais longa nem a mais curta na maioria); **cada explicação escrita do zero**, sem frase final de molde repetida; em Certo/Errado a explicação nunca fala em "alternativa".

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
- Varie o tipo: conceito, caso prático, "assinale a incorreta", assertivas I, II e III, exceção, comparação, letra da lei. Não comece várias questões com a mesma frase de molde.
- Alternativas erradas PLAUSÍVEIS: erros que um candidato que estudou mal cometeria (troca de prazo, de órgão, de conceito vizinho, exceção virando regra). Nada absurdo ou caricato, como "publicar o nome do paciente" ou "eliminar toda fiscalização", que se elimina sem saber a matéria.
- A questão tem que cobrar o assunto DO CAPÍTULO indicado em "section", não de outro capítulo.
- Se não tiver certeza de número de artigo, lei, prazo ou súmula, não cite o número: cobre o conteúdo. Nunca invente norma, jurisprudência ou dado.
- Certo/Errado no estilo Cebraspe: afirmação única, precisa, com o erro (quando houver) num detalhe técnico, não em palavra absurda.
- Português do Brasil, nível de prova de concurso real.
```

## Refazer 1: Ortografia oficial: emprego das letras (`portugues.ortografia`, 3619 tópicos de edital)

```
ATENÇÃO: da outra vez questões com alternativas repetidas (iguais entre si). Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: portugues.ortografia (Ortografia oficial: emprego das letras)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema ortográfico oficial, alfabeto, h e maiúsculas/minúsculas": 23
- "2. S, Z, SS, C, Ç, SC, SÇ e XC: grafia e famílias lexicais": 19
- "3. X, CH, G e J: padrões, etimologia e contrastes": 19
- "4. E/I, O/U e conservação das famílias lexicais": 19
- "5. Homônimos, parônimos e aplicação da ortografia na escrita formal": 20

Já existem (não repita):
- Na revisão de uma nota sobre o Acordo Ortográfico, um redator precisa indicar corretamente
- Uma questão de sistema gráfico pede a quantidade de letras do alfabeto oficial e o tratame
- Em um comunicado, a frase informa que determinado prazo termina em uma unidade de tempo, e
- Uma agenda de eventos será publicada com o mês, a estação do ano e uma festividade. Qual c
- Um relatório descreve uma atitude marcada por muito carinho. Qual adjetivo completa corret
- Em frase sobre qualidade de algo belo, é necessário empregar o substantivo abstrato deriva
- Uma ordem recomenda: 'É importante que a planta ___ vigorosa'. A forma verbal desejada é d
- No comunicado 'A ___ de estudo começa amanhã', o espaço deve ser preenchido por substantiv
- O texto informa que a equipe precisou examinar dados e produzir uma análise detalhada. Qua
- Uma minuta precisa empregar o verbo formado de 'moderno' com sentido de tornar moderno. Qu
- Um revisor encontrou a frase 'A excessão foi registrada como justificativa'. Qual interven
- Ao organizar um inventário, alguém escreve que irá 'enxarcar a roupa em água' e 'encher um
- Em 'Espero que os participantes ___ na próxima semana', deseja-se o presente do subjuntivo
- Uma frase descreve uma característica positiva de alguém: 'Trata-se de pessoa ___ na área 
- Em um documento, a expressão refere-se à medida física de uma sala e não à execução de uma
- Um termo formaliza a transferência de direitos, enquanto outro anuncia o horário de uma re
- Uma instrução trata da grafia dos nomes de disciplinas escolares e afirma que o Acordo imp
- Uma redação descreve a nacionalidade de uma participante e a qualidade de um país próspero
- Em um relato, a pessoa primeiro movimentou um objeto e depois afastou uma porção de cabelo
- Um aviso contém duas construções: 'A ___ foi adiada' e 'Espero que os participantes ___ am
- Após verificar um dado inexato, uma equipe precisa corrigi-lo; em seguida, a autoridade co
- Em um texto informativo, mencionam-se um levantamento estatístico e a capacidade de julgar
- Uma nota diz: 'A análise foi feita ___; ___, os registros foram confrontados'. Pretende-se
- Uma redação descreve deslocamento geográfico com 'o vento veio do ___' e, depois, faz refe
- Um texto diz que 'a sesssão foi transferida para outro setor; a secção de arquivos formali
- Ao conferir a base normativa da ortografia brasileira, um redator informa que o Decreto nº
- Segundo o Acordo Ortográfico, o alfabeto português permanece com 23 letras, porque k, w e 
- Em um aviso sobre horário de atendimento, escrever 'hora' com h está correto, porque o sub
- Em programação de eventos, as formas 'Setembro', 'Outono' e 'Páscoa' obedecem igualmente à
- Os adjetivos 'perigoso' e 'carinhosa' utilizam s na terminação -oso/-osa, enquanto o subst
- Na frase 'Ela é portugueza', a grafia com z está adequada à regra dos adjetivos femininos 
- A relação entre 'nascer' e 'nasça' mostra que o grupo sc do infinitivo pode corresponder a
- A sequência 'enxergar, enxugar, enxame' prova que qualquer vocábulo iniciado por en- e com
- Em 'A viagem ocorrerá em julho' e 'Espero que eles viajem em julho', empregam-se respectiv
- Por terminar com som semelhante ao de viagem, o substantivo 'pajem' deve ser reescrito 'pa
- Em um documento de medição, 'comprimento da ponte' nomeia sua extensão; já 'cumprimento da
- A palavra 'beneficiente' é a grafia recomendada para alguém que faz o bem, porque o segmen
- Os nomes 'Natal' e 'Páscoa' recebem iniciais maiúsculas como festividades, mas a referênci
- Como o substantivo 'análise' contém s, sua família regular autoriza grafar 'analisar' com 
- Na Base XIX, os nomes de disciplinas admitem inicial maiúscula ou minúscula, de modo que a
- Em um termo que apresenta a 'descrição detalhada' de bens, a substituição por 'discrição d
- Um ato que anula licença pode usar o verbo 'cassar', ao passo que procurar ou perseguir um
- Na redação de relato de inspeção, 'ad-hoc' e 'in-loco' são as grafias normais dessas locuç
- As formas 'dormir' e 'durmo' exemplificam alternância vocálica na conjugação; a existência
- Quando uma pessoa confirma expressamente decisão anterior, o verbo mais preciso é 'retific
```

## Refazer 2: Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes (`saude-publica.politicas-nacionais-saude`, 3433 tópicos de edital)

```
ATENÇÃO: da outra vez a correta saiu a mais curta demais. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: saude-publica.politicas-nacionais-saude (Políticas Nacionais de Saúde: Atenção Básica, Promoção, Humanização e Redes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Redes de Atenção à Saúde e Atenção Básica": 20
- "2. Promoção da saúde e determinantes sociais": 20
- "3. Política Nacional de Humanização (HumanizaSUS)": 20
- "4. Pacto pela Saúde e Rede de Urgência e Emergência": 20
- "5. Programa Nacional de Imunizações": 20

Já existem (não repita):
- Nas Redes de Atenção à Saúde, o ponto que funciona como centro de comunicação, coordenando
- As Redes de Atenção à Saúde propõem substituir o modelo hierárquico piramidal por uma rede
- Segundo a PNAB 2017, são princípios da Atenção Básica:
- A equipe de Saúde da Família deve ser composta, no mínimo, por médico, enfermeiro, auxilia
- Pela PNAB 2017, a população adscrita recomendada por equipe de Saúde da Família e o número
- O acompanhamento da pessoa pela mesma equipe ao longo do tempo, com vínculo e responsabili
- A Estratégia Saúde da Família é considerada pela PNAB a estratégia prioritária para expans
- Constituem diretrizes da Política Nacional de Atenção Básica, EXCETO:
- São campos de ação da promoção da saúde definidos pela Carta de Ottawa (1986), EXCETO:
- A Política Nacional de Promoção da Saúde, aprovada em 2006, foi revisada em 2014 e tem a i
- É tema prioritário da Política Nacional de Promoção da Saúde:
- A diferença central entre promoção da saúde e prevenção de doenças é que:
- No modelo de determinantes sociais de Dahlgren e Whitehead, a camada mais interna, de difí
- No modelo de Dahlgren e Whitehead, habitação, saneamento, educação, ambiente de trabalho e
- Iniquidades em saúde são diferenças nas condições de saúde entre grupos que podem ser cons
- A Política Nacional de Humanização, lançada em 2003, caracteriza-se por ser:
- São princípios da Política Nacional de Humanização:
- Na Política Nacional de Humanização, o acolhimento é entendido como etapa de recepção e tr
- O dispositivo da PNH usado nos serviços de urgência para organizar o atendimento de acordo
- O Projeto Terapêutico Singular é um conjunto de propostas de condutas articuladas para um 
- A diretriz da PNH que propõe considerar a singularidade do sujeito e seu contexto, e não a
- O princípio da PNH segundo o qual o modo de cuidar e o modo de gerir não se separam, deven
- O Pacto pela Saúde, instituído pela Portaria 399/2006, é composto por três dimensões:
- Entre as prioridades do Pacto pela Vida estavam a saúde do idoso, o controle do câncer de 
- O instrumento que sucedeu o Pacto pela Saúde na organização das responsabilidades entre os
- São componentes da Rede de Atenção às Urgências e Emergências (Portaria 1.600/2011), EXCET
- Na Rede de Urgência e Emergência, a atenção básica também tem papel de porta de entrada, d
- Entre as redes temáticas prioritárias pactuadas a partir de 2011, a rede voltada ao cuidad
- O Programa Nacional de Imunizações foi criado em 1973 e é responsável, entre outras atribu
- A faixa de temperatura em que, em geral, devem ser conservados os imunobiológicos na rede 
- Os Centros de Referência para Imunobiológicos Especiais (CRIE) destinam-se a:
- A ocorrência de um evento após a vacinação comprova, por si só, que a vacina foi a causa d
- A proteção indireta de pessoas não vacinadas quando grande parte da população está imune é
- A queda das coberturas vacinais na última década aumentou o risco de reintrodução de doenç
- Sobre os valores da Política Nacional de Promoção da Saúde revisada em 2014, assinale a al
- Os atributos derivados da atenção primária, segundo Starfield, são a orientação familiar, 
- No Pacto de Gestão de 2006, a regionalização foi tratada como eixo estruturante, com a cri
- A ambiência, diretriz da Política Nacional de Humanização, refere-se ao tratamento dado ao
- Pela PNAB 2017, a Atenção Básica deve ser a principal porta de entrada e o centro de comun
- A cobertura vacinal expressa a proporção da população-alvo vacinada, enquanto a homogeneid
- De acordo com as diretrizes das Redes de Atenção à Saúde (Portaria nº 4.279/2010), os três
- A PNAB 2017 estabeleceu que a cobertura de 100% da população por Agentes Comunitários de S
- A respeito da carga horária e composição das equipes previstas na PNAB 2017, assinale a al
- Na estrutura operacional das Redes de Atenção à Saúde, o prontuário clínico eletrônico, os
- Para a PNAB 2017, os termos Atenção Básica e Atenção Primária à Saúde são considerados equ
- Quando uma equipe de Saúde da Família adapta sua comunicação e suas práticas assistenciais
- De acordo com a Carta de Ottawa (1986), as ações de promoção da saúde são de responsabilid
- Na Política Nacional de Promoção da Saúde (Portaria nº 2.446/2014), a articulação de saber
- No modelo de determinantes sociais da saúde de Dahlgren e Whitehead, os laços de coesão gr
- Assinale a alternativa que apresenta as quatro etapas (momentos) que compõem a construção 
- Na Política Nacional de Humanização, o apoio matricial consiste na transferência definitiv
- A criação de espaços coletivos de decisão, como colegiados gestores, rodas de conversa e c
- No âmbito do Pacto pela Saúde (Portaria nº 399/2006), a repolitização da saúde, a luta pel
- Na Rede de Atenção às Urgências e Emergências (Portaria nº 1.600/2011), as Unidades de Pro
- No Componente Hospitalar da Rede de Atenção às Urgências e Emergências (RUE), foram instit
- O componente da RUE que consiste em um ambiente vinculado a um equipamento de saúde, com c
- Com o Programa Nacional de Imunizações, o Brasil conquistou a erradicação da varíola e a e
- No âmbito do Programa Nacional de Imunizações (PNI), o atraso em aceitar ou a recusa das v
- Na Rede de Frio do PNI, vacinas inativadas que contêm adjuvante de hidróxido de alumínio —
- No monitoramento das metas do Programa Nacional de Imunizações, o indicador que calcula o 
```

## Refazer 3: Reescrita de frases e parágrafos (`portugues.reescrita`, 2798 tópicos de edital)

```
ATENÇÃO: da outra vez a mesma frase ("Os distratores modificam o alcance...") em 50 explicações. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: portugues.reescrita (Reescrita de frases e parágrafos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Equivalência de sentido e substituição lexical contextual": 27
- "2. Conectores, tempo e relações entre orações": 20
- "3. Reorganização sintática, voz, pontuação e paralelismo": 22
- "4. Discursos direto, indireto e indireto livre": 14
- "5. Retextualização, registro, gênero e revisão avançada": 17

Já existem (não repita):
- No relatório de uma visita técnica lê-se: 'Apesar da chuva, a equipe concluiu a visita.' Q
- Uma anotação registra: 'O pedido chegou fora do prazo; contudo, foi examinado.' Qual alter
- Considere: 'A equipe revisou os dados na terça-feira.' Qual passagem à voz passiva analíti
- No comunicado consta: 'Como faltavam informações, a análise foi suspensa.' Qual alternativ
- Em um curso com inscritos que estavam e que não estavam de plantão, afirmou-se: 'Os técnic
- Um aviso informa: 'Houve falha no sistema; por isso, a reunião foi adiada.' Qual reescrita
- Em uma lista de participantes, escreveu-se: 'Apenas Marina apresentou a proposta.' Qual al
- O registro de presença afirma: 'Nem todos os convidados compareceram.' Qual alternativa ap
- O texto registra: 'A responsável talvez apresente os documentos amanhã.' Qual versão conse
- Em relato feito na semana seguinte, está a fala de Bia: 'Voltarei amanhã', disse ela na se
- Leia: 'Após dois anos sem publicar, Lia voltou a divulgar seus estudos.' Qual substituição
- Uma ata informa: 'Não é verdade que todos os arquivos foram conferidos.' Qual reescrita co
- A redação diz: 'Por ter recebido o alerta, a pesquisadora retornou ao laboratório.' Qual d
- O registro informa: 'A comissão aprovou o plano na reunião de terça-feira.' Qual nominaliz
- Um parecer declara: 'É possível que o serviço não funcione amanhã.' Qual alternativa manté
- A proposta contém: 'O projeto pretende reduzir desperdícios e ampliar o acesso ao serviço.
- Uma orientação estabelece: 'Caso haja autorização, divulgaremos o resultado.' Qual reescri
- No relato consta: 'Embora tivesse estudado, Sara não concluiu a atividade.' Qual reescrita
- Considere: 'Somente depois de concluída a auditoria, a diretora divulgou os dados.' Qual a
- Em uma narrativa, lê-se: 'Helena fechou o envelope. Quanto tempo mais esperaria? Ninguém l
- Para converter a mensagem 'Oi, manda o arquivo até as 16h, por favor' em uma solicitação f
- O texto registra: 'Depois de examinar as propostas, a comissão anunciou o resultado.' Qual
- Uma narrativa afirma: 'Quando a equipe chegou, o técnico já havia desligado a máquina.' Qu
- Em conversa de equipe, alguém disse: 'A gente talvez consiga concluir hoje.' Para um relat
- Na sexta-feira, um gestor declarou: 'Não pretendo adiar a divulgação, a menos que faltem d
- Em uma nota técnica, a frase 'O parecer fundamenta a decisão' pode ser reescrita, em conte
- Em uma lista de presença, 'Alguns estudantes compareceram' pode ser substituído por 'Todos
- Na justificativa 'A sessão terminou porque houve interrupção de energia', a locução 'em ra
- A transformação 'Os analistas verificaram o arquivo ontem' em 'O arquivo é verificado pelo
- No relato posterior 'Aline disse que retornaria no dia seguinte', uma forma direta compatí
- Ao reescrever 'Chegaram ontem os relatórios' como 'Chegou ontem os relatórios', conserva-s
- Em uma turma composta por concluintes e não concluintes, escrever 'Os alunos que terminara
- A frase 'Vendem-se equipamentos usados' pode ser reescrita como 'Vende-se equipamentos usa
- No trecho 'Mário recolheu as chaves. Que situação absurda! Como poderia sair agora?', o co
- No enunciado 'O aviso já havia sido enviado quando a reunião começou', a troca de 'já havi
- Uma mensagem informal como 'Me envie a resposta até sexta-feira, por favor' pode ser retex
- No período 'Somente se a autorização chegar, a visita ocorrerá', retirar o advérbio 'somen
- Em 'Rosa atravessou o corredor. Por que ninguém compreendia seu receio? Seria tão difícil 
- Uma declaração 'Não é possível que o grupo deixe de receber a mensagem' mantém o sentido c
- No registro 'Ao receber o comunicado, a pesquisadora telefonou', o desenvolvimento 'Quando
- O texto 'A pesquisadora encontrou a guia com uma luneta' é ambíguo quanto a quem portava a
- Na conversão de uma fala informal em comunicado escrito, podem ser eliminadas hesitações e
- Na frase 'A comissão avaliou as sugestões antes de publicar a decisão', a nominalização 'A
- Em um aviso, 'Mais de metade dos inscritos confirmou presença' pode ser reescrito como 'A 
- Em relatório formal, substituir 'Houve diversas falhas no registro' por 'Houveram diversas
```

## Refazer 4: Gêneros textuais e tipos textuais (`portugues.generos-textuais`, 2453 tópicos de edital)

```
ATENÇÃO: da outra vez a correta saiu a mais curta em 47 de 50 (metade do tamanho das outras). Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: portugues.generos-textuais (Gêneros textuais e tipos textuais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Gênero, tipo, domínio, suporte e situação comunicativa": 17
- "2. Tipos estruturais e modos funcionais: como distingui-los": 19
- "3. Gêneros literários, não literários, jornalísticos e de estudo": 30
- "4. Gêneros normativos, oficiais e de circulação cotidiana": 17
- "5. Gêneros digitais, intertextualidade, hibridismo e oficina de produção": 17

Já existem (não repita):
- Uma publicação intitulada 'Como plantar manjericão' apresenta materiais necessários e pass
- Um poema foi publicado num livro e posteriormente numa rede social, mantendo versos, organ
- Um mesmo portal publica notícia, carta de leitor, anúncio e tirinha. Nessa situação, a cat
- Uma reportagem relata a sequência da enchente, descreve ruas atingidas e informa índices d
- Um texto escrito em turnos apresenta '— Você concluiu o cadastro? — Sim, terminei ontem'. 
- Uma campanha divulga 'Adquira o novo filtro e ganhe entrega gratuita' junto de preço e con
- Cartaz comunitário informa: 'Reduza o descarte de plástico; preserve o rio e incentive seu
- O regulamento de convivência apresenta: 'Após as 22h, os equipamentos sonoros deverão perm
- Uma apostila explica 'evaporação', oferece exemplos progressivos e propõe exercícios para 
- Num quadro de horóscopo lê-se: 'Uma surpresa surgirá em seus relacionamentos no próximo ci
- Uma peça em versos explora imagens e ritmo para representar a saudade vivida pelo eu poéti
- Um texto contém 'HELENA: Não entrarei. (Afasta-se da janela.) PEDRO: Então partirei sozinh
- Um slogan afirma 'Nossa conexão abraça o futuro', utilizando metáfora para vender acesso à
- Uma narrativa breve apresenta animais que agem como pessoas e termina com ensinamento mora
- Um leitor escreve texto que resume enredo de romance, avalia construção das personagens e 
- Um texto publicado em periódico afirma 'Defendemos que o bairro priorize ônibus' em posici
- Um documento registra: 'Aos cinco dias do mês, reuniram-se os participantes; decidiu-se am
- Uma pessoa precisa convocar moradores para encontro de sábado, informando pauta, horário e
- Um setor deseja comunicar formalmente solicitação entre unidades, com assunto identificáve
- Uma pessoa pede por e-mail acesso ao histórico de inscrições. Para produzir mensagem adequ
- Uma campanha pelo uso consciente de água se apresenta como 'receita': 'Ingredientes: atenç
- Uma ata documenta decisão sobre fechamento temporário de biblioteca. Para transformá-la em
- Uma postagem reproduz de modo reconhecível a estrutura de um conto clássico, alterando seu
- No mesmo aplicativo, uma mensagem serve para confirmar encontro entre amigos e outra comun
- Uma página apresenta duas previsões: 'Chuva de 20 mm, estimada por modelos atmosféricos' e
- Um comunicado apresenta dados verificados sobre a interrupção do abastecimento, identifica
- Uma campanha educativa de vacinação sem oferta comercial deve ser classificada obrigatoria
- Um documento que estabelece proibições, condições e deveres de convivência pode ser normat
- Uma receita publicada em revista permanece obrigatoriamente do tipo narrativo, porque todo
- Um texto destinado a ensinar princípio físico, com explicação, exemplo e exercício, pode s
- O verso 'A noite cobriu a rua com um lençol de silêncio' utiliza linguagem conotativa e, p
- Uma peça teatral apresenta nomes de personagens, rubricas de movimentação e falas alternad
- Uma crônica de linguagem imaginativa publicada em jornal passa automaticamente a ser notíc
- Uma notícia relata acontecimento com dados de apuração, enquanto reportagem pode aprofunda
- Um poema narrativo com personagens e acontecimentos deixa de ser poema, porque na tradição
- Um recado deixado em aplicativo para avisar colega sobre mudança de horário pode manter a 
- Uma sequência de mensagens em chat profissional só pode constituir texto informal e sem fi
- Um fórum eletrônico organiza respostas em tópicos, que podem ser retomados depois, enquant
- Ao escrever e-mail de solicitação para destinatário desconhecido, basta empregar vocabulár
- A transferência do conteúdo de uma ata para aviso público requer seleção de dados úteis ao
- Uma campanha de preservação ambiental adotou título 'Ingredientes' e 'Modo de preparo' par
- Quando uma peça digital reformula conto reconhecível para produzir humor crítico, há possi
- Em uma reportagem, a presença de trechos narrativos e explicativos já caracteriza obrigato
- Uma postagem que apresenta roteiro de vídeo para ensinar reparo e inclui demonstrações vis
- Um documento com título 'Convite' relata minuciosamente deliberações de encontro encerrado
```

## Refazer 5: LDB, BNCC e didática (`educacao.ldb-bncc-didatica`, 2405 tópicos de edital)

```
ATENÇÃO: da outra vez a correta saiu a mais curta em 33 de 50. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: educacao.ldb-bncc-didatica (LDB, BNCC e didática)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direito à educação, organização do sistema e LDB": 14
- "2. BNCC, currículo e diretrizes para etapas e modalidades": 37
- "3. Correntes pedagógicas, teorias do currículo e formação docente": 16
- "4. PPP, planejamento didático e sala de aula como aprendizagem": 18
- "5. Avaliação da aprendizagem, inclusão e AEE": 15

Já existem (não repita):
- Uma escola revisa a finalidade constitucional do ensino e inclui desenvolvimento da pessoa
- Uma rede discute quem deve atuar prioritariamente na oferta de educação infantil e ensino 
- Uma escola afirma que a BNCC contém todo o currículo e impede temas pertinentes à realidad
- O conselho escolar deseja identificar a norma de diretrizes curriculares para o ensino fun
- Uma aula começa com tema gerador da comunidade e debate crítico em que os estudantes probl
- Uma escola deseja rever o documento coletivo que expressa identidade, princípios, priorida
- Antes de começar uma sequência didática, a professora investiga as hipóteses e conheciment
- Uma estudante com deficiência frequenta turma comum e recebe AEE para superar barreiras de
- Em 2026, uma secretaria atualiza sua referência de planejamento decenal e pretende não rep
- Uma rede elabora currículo de ensino médio em 2026 e compara a resolução de diretrizes de 
- Uma instituição revê material didático que ainda apresenta a Resolução CNE/CEB nº 1/2021 c
- Um estudante cursa ensino médio e curso técnico simultaneamente, com matrículas distintas 
- Uma escola declara participação democrática no currículo, mas concede a palavra apenas aos
- Um coordenador organiza currículo formulando objetivos, escolhendo experiências, ordenando
- Uma escola registra que a turma deve interpretar argumentos com evidências, mas avalia som
- Após tarefa intermediária, a professora observa erros recorrentes, altera a sequência de e
- Uma professora parte de problema da prática social, ensina conceitos científicos sistemati
- Um curso de licenciatura fundamenta sua reforma na Resolução CNE/CP nº 2/2019 como se aind
- Uma rede afirma que pode ofertar itinerários do ensino médio sem resguardar os direitos co
- Uma turma de EJA reúne trabalhadores com experiências diversas. O planejamento aproveita s
- Um estudante com deficiência necessita AEE e recursos pedagógicos específicos, mas não pos
- Um estado e um município debatem o financiamento educacional e concluem que a existência d
- Em estudo sobre conhecimento oficial, uma equipe analisa como escolhas curriculares reforç
- Uma direção fixa a média aritmética de provas finais como único critério e ignora evolução
- Um estudante com altas habilidades participa da turma comum e recebe AEE destinado a aprof
- Uma proposta escolar combina formação cidadã, desenvolvimento integral e preparação para o
- Um município considera que sua prioridade constitucional é ofertar o ensino médio e transf
- A equipe pedagógica elabora coletivamente a proposta institucional e os docentes participa
- Uma escola elimina a parte diversificada de seu currículo, justificando que a BNCC já cont
- Uma professora organiza aulas por exposição sistemática de conteúdos e exercícios de assim
- As expectativas implícitas de comportamento e hierarquias transmitidas na convivência esco
- Uma escola usa atividade inicial para conhecer saberes prévios e reorganizar o percurso de
- Ao revisar normas do ensino médio em 2026, uma rede trata a Resolução CNE/CEB nº 3/2018 co
- Uma professora articula conteúdos socialmente relevantes aos saberes da turma e promove ac
- Uma escola de EJA afirma que a Resolução CNE/CEB nº 1/2021 continua vigente e que as norma
- Um curso técnico e o ensino médio são oferecidos de modo integrado na mesma instituição, c
- Um curso superior de licenciatura fundamenta exclusivamente suas diretrizes atuais na Reso
- Ao reorganizar a sala, a professora estabelece combinados de convivência, cria oportunidad
- Após atividade intermediária revelar dificuldade generalizada, a professora mantém o plane
- Em 2026, um plano municipal novo é articulado ao Plano Nacional de Educação estabelecido p
- Um estudante com deficiência precisa de recursos de acessibilidade e apoio pedagógico. A e
- Uma sequência de ensino médio articula investigação da água de um bairro por ciências, geo
- Uma rede de EJA em 2026 adota a Política Nacional de Alfabetização antiga como único refer
- Uma escola elabora documentos individualizados de natureza pedagógica, como PAEE e PEI, a 
- Uma escola aplica somente prova final numérica e despreza todo o percurso de aprendizagem.
```

## Refazer 6: Internet e navegadores (`informatica.internet-navegadores`, 2274 tópicos de edital)

```
ATENÇÃO: da outra vez a correta saiu a mais curta em 31 de 50. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: informatica.internet-navegadores (Internet e navegadores)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Internet, web, intranet, extranet, portais e endereçamento": 37
- "2. Navegadores modernos, abas, pesquisas e produtividade": 16
- "3. Internet Explorer legado, Opções da Internet e modo IE": 16
- "4. Dados de navegação, privacidade, downloads, upload e impressão": 16
- "5. Segurança de navegação, pesquisa crítica e serviços não indexados": 15

Já existem (não repita):
- Uma pessoa utiliza o navegador para consultar páginas de um portal, mas também transfere a
- Uma organização disponibiliza um sistema de pessoal somente a usuários internos autenticad
- Um fornecedor externo recebe credenciais para consultar somente pedidos de compra da organ
- Ao digitar o endereço de um site no navegador, uma consulta permite descobrir o endereço I
- Uma pessoa precisa localizar uma palavra dentro de uma página já aberta no navegador, sem 
- Durante a pesquisa, uma guia com documento importante foi fechada por engano. Em navegador
- Uma pessoa clica no ícone de estrela para guardar o endereço de um portal que deseja visit
- Um site institucional antigo exige componentes específicos do Internet Explorer. No contex
- O endereço https://portal.exemplo.org.br/docs/manual.pdf?edicao=4#sumario é analisado para
- Uma pessoa realiza consulta em um portal interno a partir de casa por uma VPN corporativa 
- Uma pessoa apaga apenas o cache do navegador, preservando cookies e senhas. Qual efeito é 
- Depois de acessar uma conta em portal de serviços, o usuário exclui os cookies do site e p
- Um portal permite anexar documentos e pede que a pessoa selecione um arquivo local e confi
- Uma pessoa abre janela de navegação InPrivate no Edge, baixa um PDF e cria um favorito. Ap
- Uma página foi impressa para PDF por meio de Ctrl+P e outra cópia foi criada pelo comando 
- Ao receber um link para autenticação, o usuário observa https://login.portal-seguro.exempl
- Uma página de aparência fraudulenta usa HTTPS e apresenta certificado técnico válido para 
- Uma orientação antiga apresenta o filtro SmartScreen como componente do Internet Explorer.
- Um sistema interno não aparece nos resultados de buscadores e exige login pela web convenc
- No Firefox, uma pessoa navega em janela privativa, faz download de um documento e, ao fech
- Uma extensão solicita permissão para ler dados de páginas visitadas e funcionar também na 
- Uma pessoa não encontra um relatório interno em mecanismo público de busca, mas o acessa n
- Depois de remover a lista de downloads do navegador, uma pessoa abre a pasta Downloads do 
- Um navegador falha ao acessar somente portal.exemplo.net e exibe mensagem de que não conse
- Uma pessoa acessa uma página web a partir de navegador e, em outro programa, utiliza servi
- Uma intranet institucional deve funcionar sem qualquer conexão à internet, porque seu uso 
- Uma página apresenta um botão com texto 'Portal oficial'. Esse texto permite garantir que 
- Em navegador moderno no Windows, pressionar Ctrl+J normalmente abre o gerenciador ou a pág
- Uma aplicação antiga menciona recursos e menus do Internet Explorer 11. Para interpretá-la
- Em janela de navegação anônima do Chrome, o provedor e a rede institucional ficam impossib
- Uma pessoa apaga somente o cache de imagens do navegador e, por esse comando, remove obrig
- Em formulário web, a pessoa seleciona um PDF local no campo de anexos, mas ainda precisa c
- Ao usar Ctrl+P e escolher salvar como PDF, uma pessoa gera uma representação de impressão 
- O cache DNS do sistema e o cache do navegador para imagens são o mesmo mecanismo de armaze
- No Internet Explorer clássico, a seção Geral de Opções da Internet era usada apenas para d
- Uma pessoa marca como favorito um site durante sessão privada do Edge. O encerramento das 
- O SmartScreen do Microsoft Edge moderno pode auxiliar na identificação de sites suspeitos 
- Um relatório não aparece em resultados de pesquisa porque exige autenticação em portal int
- No endereço https://acesso.organizacao.exemplo.com.invalido.net/entrar, o host pertence ao
- Em instalação atual do Windows 11, o SmartScreen continua ativo dentro do modo IE exatamen
- Uma extensão expressamente autorizada para executar em janela privada fica tecnicamente im
- Um firewall aplica regras ao tráfego entre redes, enquanto a autorização no portal delimit
- Um documento baixado em janela privativa do Firefox desaparece necessariamente da pasta lo
- Serviços onion utilizam endereços .onion próprios da rede Tor. Seu uso não é necessariamen
```

## Refazer 7: Agentes públicos: classificação, cargo, emprego e função (`direito-administrativo.agentes-publicos`, 2242 tópicos de edital)

```
ATENÇÃO: da outra vez a correta saiu a MAIS CURTA em 44 de 50. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: direito-administrativo.agentes-publicos (Agentes públicos: classificação, cargo, emprego e função)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, espécies de agentes públicos e natureza dos vínculos": 43
- "2. Concurso, contratação temporária, provimento, posse e exercício": 15
- "3. Constituição: remuneração, acumulação, estágio probatório e estabilidade": 14
- "4. Exercício funcional, licenças, afastamentos, deveres e vacância": 14
- "5. Responsabilidades, controle, greve e situações difíceis": 14

Já existem (não repita):
- Uma pessoa é convocada para atuar como jurada durante julgamento e não integra quadro perm
- Uma unidade administrativa designará pessoa para função de confiança prevista no art. 37, 
- Uma empresa pública admite candidato aprovado em concurso sob regime celetista. Qual é a n
- Para atender calamidade prevista em lei, a administração pretende contratar equipe apenas 
- Uma certidão foi emitida por pessoa aparentemente investida em cargo, mas a investidura fo
- Um edital prevê concurso válido por dois anos e pretende prorrogar esse prazo uma única ve
- No regime da Lei nº 8.112/1990, determinado candidato foi nomeado para cargo efetivo. Em q
- Após a publicação de sua nomeação federal, pessoa sem impedimento legal precisa tomar poss
- Uma servidora federal toma posse no prazo, mas, sem causa justificadora, deixa transcorrer
- Uma servidora ascende de classe na mesma carreira, segundo critérios estatutários, sem ing
- Um estatuto pretende permitir que servidor seja transferido para carreira completamente di
- Após inspeção médica, servidora sofre limitação permanente e passa a exercer cargo de atri
- Uma norma local autoriza por prazo indeterminado contratação temporária para suprir falta 
- Um professor efetivo pretende acumular cargo administrativo não classificado como técnico 
- Empregada celetista concursada de empresa estatal sustenta que três anos de trabalho lhe a
- Uma unidade pretende aumentar vencimentos de servidores por mero ato administrativo, sem l
- Profissional com habilitação regulamentada deseja acumular dois empregos privativos de sua
- Durante emergência legalmente prevista, um particular é compulsoriamente convocado para co
- Servidora efetiva obtém licença legalmente prevista para tratamento de interesses particul
- Servidor recebe ordem escrita e manifestamente ilegal para divulgar informação protegida p
- Uma reforma administrativa desloca de um órgão a outro cargo efetivo vago, enquanto, em at
- Uma chefia encerra a ocupação de cargo em comissão por conveniência, e outra apura infraçã
- Servidores com atuação direta em segurança pública anunciam greve e pedem mediação coletiv
- Após apuração funcional, agente é absolvido criminalmente por decisão definitiva que afirm
- Em paralisação de servidores civis não vinculados diretamente à segurança pública, ficou d
- Durante eleição, cidadão convocado como mesário presta colaboração pública episódica sem a
- Uma pessoa sem cargo efetivo pode ser designada diretamente para função de confiança porqu
- Lei que cria cargo em comissão deve destiná-lo a atribuições de direção, chefia ou assesso
- Uma entidade pode contratar por tempo determinado para serviço permanente rotineiro simple
- Uma pessoa nomeada sob a Lei nº 8.112/1990 toma posse no vigésimo dia após a publicação do
- Servidora federal empossada dispõe, como regra, de trinta dias contados da posse para inic
- Servidor de determinada carreira não pode ingressar, pela simples transformação de seu vín
- Um professor concursado não pode acumular seu cargo com outro cargo administrativo não téc
- Profissional da saúde com profissão regulamentada pode acumular dois empregos privativos d
- O estágio probatório federal continua legalmente restrito a vinte e quatro meses, embora o
- O servidor efetivo afastado por licença regular não tem, apenas em razão da licença, seu c
- A requisição de particular para prestar colaboração compulsória em emergência e a cessão d
- Ao receber ordem manifestamente ilegal para divulgar dados protegidos, servidor deve recus
- Quando pessoa em cargo em comissão é exonerada de ofício por decisão administrativa regula
- No regime da Lei nº 8.112/1990, promoção e readaptação podem figurar simultaneamente como 
- Se servidor é absolvido criminalmente apenas por insuficiência de provas, a administração 
- Em greve de servidores civis provocada por atraso ilícito no pagamento imputável ao poder 
- Servidores que atuam diretamente em segurança pública podem realizar greve desde que a cat
- Uma contratação chamada temporária, mas destinada a suprir necessidade estrutural ordinári
- Um agente que, no exercício funcional, causa dano pode responder administrativamente ou pe
```

## Refazer 8: Clínica médica: endocrinologia, nefrologia, gastroenterologia e infectologia (`medicina.clinica-endocrino-renal-gastro-infecto`, 2166 tópicos de edital)

```
ATENÇÃO: da outra vez alternativas com trecho colado depois do ponto final. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: medicina.clinica-endocrino-renal-gastro-infecto (Clínica médica: endocrinologia, nefrologia, gastroenterologia e infectologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Endocrinologia: diabetes, tireoide, metabolismo e glândulas": 21
- "2. Nefrologia: lesão renal, glomérulos, litíase e equilíbrio acidobásico": 21
- "3. Gastroenterologia: refluxo, úlcera, intestino, vesícula e pâncreas": 20
- "4. Infectologia e hepatologia: diarreia, hepatites e doença hepática": 20
- "5. Urgências frequentes: tontura, cefaleia, rinossinusopatias e alergia": 18

Já existem (não repita):
- Uma pessoa relata poliúria, polidipsia e emagrecimento há semanas. Sua glicemia plasmática
- Uma paciente apresenta sonolência, constipação e intolerância ao frio. Os exames mostram T
- Um adolescente previamente saudável apresenta náuseas, dor abdominal, respiração profunda,
- Uma mulher chega com hipotensão, fraqueza intensa, hiperpigmentação cutânea, sódio de 127 
- Uma pessoa com nefrolitíase recorrente apresenta cálcio sérico elevado e PTH persistenteme
- A creatinina de um paciente era 0,9 mg/dL há 36 horas e passou a 1,3 mg/dL. Não há dados p
- Uma pessoa com diabetes tem TFGe de 95 mL/min/1,73 m² e relação albumina/creatinina de 160
- Após quadro infeccioso, uma pessoa apresenta urina escura, edema, hipertensão e exame de u
- Paciente com edema generalizado exibe proteinúria de 6 g/dia, albumina sérica reduzida e h
- Um paciente com cólica lombar apresenta febre de 39 °C, calafrios e hidronefrose por cálcu
- Uma pessoa com azia e regurgitação há meses passa a apresentar disfagia progressiva e perd
- Uma adulta com anemia ferropriva e diarreia crônica será investigada para doença celíaca; 
- Uma pessoa com diarreia intermitente e dor abdominal apresenta fístula perianal e estenose
- Uma pessoa com cálculos biliares apresenta febre, dor no hipocôndrio direito, icterícia e 
- Uma paciente apresenta dor epigástrica intensa irradiada para o dorso e lipase 5 vezes aci
- Uma pessoa adulta tem diarreia aquosa há dois dias, mantém boa perfusão e consegue beber l
- Em rastreamento, um paciente assintomático tem anticorpo anti-HCV reagente. Não há dados a
- Uma pessoa vacinada contra hepatite B apresenta HBsAg negativo, anti-HBc total negativo e 
- Uma pessoa com cirrose e ascite interna-se com febre e piora clínica. A paracentese revela
- Um paciente com cirrose avançada tem ascite, encefalopatia e INR elevado, mas transaminase
- Uma paciente apresenta crises vertiginosas de 20 segundos ao virar-se para a direita na ca
- Um adulto desenvolve vertigem contínua abrupta, vômitos e nistagmo, acompanhados de diplop
- Uma pessoa apresenta coriza e congestão viral há seis dias, melhora no sétimo e retorna no
- Uma pessoa em uso de inibidor da enzima conversora desenvolve edema de lábios e língua, se
- Um adulto relata cefaleia que começou subitamente e alcançou intensidade máxima em poucos 
- Uma pessoa assintomática apresenta glicemia plasmática de jejum de 112 mg/dL, sem outros c
- Uma paciente com TSH elevado e T4 livre ainda normal apresenta hipotireoidismo primário ma
- Um paciente com diurese sustentada de 0,3 mL/kg/h por sete horas preenche critério urinári
- Uma pessoa com SIAD apresenta sódio baixo por retenção de água; por essa razão, espera-se 
- Após tratamento de Helicobacter pylori associado a úlcera, pode-se documentar erradicação 
- Uma pessoa adulta apresenta IMC de 31 kg/m², mas glicemia, triglicerídeos, HDL, pressão ar
- Um exame endoscópico identifica inflamação contínua desde o reto até parte do cólon, predo
- Uma pessoa com hepatite A aguda comprovada por anti-HAV IgM apresenta risco habitual de ev
- Em uma pessoa com potássio de 6,7 mEq/L, um eletrocardiograma sem ondas T apiculadas não e
- Na investigação de hepatite C, a positividade do anti-HCV, mesmo sem HCV-RNA detectável, c
- Uma pessoa com insuficiência suprarrenal primária pode apresentar hiperpigmentação, hipote
- Uma pessoa apresenta urticária, dispneia e hipotensão logo após exposição a alérgeno conhe
- Uma pessoa com glicemia de 560 mg/dL e sódio sérico medido de 128 mEq/L pode apresentar hi
- Na acidose metabólica com bicarbonato de 12 mEq/L, uma PaCO2 de 40 mmHg representa compens
- Na avaliação de nódulo tireoidiano com TSH suprimido, pode ser apropriado pesquisar autono
- Uma pessoa apresenta HBsAg negativo e tanto anti-HBs quanto anti-HBc total positivos após 
- Uma pessoa com pancreatite aguda tem necrose pancreática à imagem, mas não apresenta evidê
- Em uma pessoa com suspeita forte de doença celíaca, deficiência de IgA total e anti-transg
- Uma pessoa em uso de inibidor da enzima conversora tem edema de língua sem habões e respos
- Em uma pessoa com crises vertiginosas de segundos provocadas por virar na cama e que está 
```

## Refazer 9: Números racionais: frações e decimais (`matematica.numeros-racionais`, 2088 tópicos de edital)

```
ATENÇÃO: da outra vez a mesma frase repetida em 36 explicações. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: matematica.numeros-racionais (Números racionais: frações e decimais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conjuntos numéricos, reta real e relações de ordem": 37
- "2. Frações, equivalência e operações fundamentais": 16
- "3. Representações decimais, dízimas e frações geratrizes": 14
- "4. Potências, raízes e prioridade das operações": 17
- "5. Problemas de frações, contagem e intervalos: estratégias avançadas": 16

Já existem (não repita):
- Uma medição foi registrada como 0,375 unidade. Qual é a classificação mais específica dess
- Um pacote contém 18 itens selecionados entre 24. Qual é a fração irredutível da parte sele
- Uma mistura recebeu 1/4 de litro de um líquido e depois 1/6 de litro de outro. Qual é o vo
- Um recipiente comporta 2/3 de litro. Ao enchê-lo com 9/10 de sua capacidade, quantos litro
- Uma medida de 3/4 de litro será dividida em porções de 9/8 de litro por unidade de referên
- Em uma planilha, a medida 0,125 deve ser expressa como fração irredutível. Qual conversão 
- Uma calculadora exibe 0,7777..., com o algarismo 7 repetindo-se sem fim. Qual fração corre
- Um cálculo usa o quadrado do número -2, com o sinal negativo incluído na base. Qual result
- De um lote com 80 peças, 5/8 foram encaminhadas para conferência. Quantas peças foram enca
- Três registros de medida são 0,3, 1/3 e 0,35. Qual alternativa apresenta a ordem crescente
- De um conjunto com 120 documentos, 1/4 foi retirado e, em seguida, 1/3 do restante também 
- A expansão decimal de uma quantidade é 0,1666..., na qual só o algarismo 6 se repete. Qual
- Em uma expressão de medidas, calcula-se 3/4 menos o quadrado de 1/2. Qual valor exato se o
- Dois marcadores estão nas posições -1,25 e +0,75 da reta real. Qual é a distância entre el
- Em um levantamento, 3/5 de todas as unidades correspondem a 42 registros. Quantas unidades
- Para comparar os registros 0,58, 7/12 e 0,6, sem arredondar o valor fracionário, qual rela
- Uma operação foi registrada sem parênteses como 24 ÷ 6 × 2. Seguindo a ordem convencional,
- Um sensor produz a dízima 0,23454545..., com anteperíodo 23 e período 45. Qual é sua fraçã
- Depois de retirar 1/4 de um estoque e 2/5 do que restou, sobraram 81 peças. Qual era a qua
- Compare os valores de (-3/4)² e de -(3/4)², respeitando os parênteses. Qual afirmação está
- Em um relatório, uma fração foi exibida como 0,083333..., sendo somente o algarismo 3 peri
- Uma sequência de frações precisa conter exatamente uma com expansão decimal finita, após a
- Calcule a expressão (3/4 - (1/2)²) ÷ (5/6), tratando o conteúdo entre parênteses como divi
- Uma marcação deve ser um racional estritamente entre 1/3 e 1/2 e estar escrita em doze avo
- Se 2/3 de uma caixa correspondem a 16 peças indivisíveis, quantas peças há em 3/8 da caixa
- Uma lista contém os números naturais 0, 1, 2 e 3. Todos esses elementos também pertencem a
- Uma medida foi anotada como 0,625. Por apresentar três casas decimais, essa medida pertenc
- Um recipiente está pela metade. As frações 2/4 e 1/2 podem representar exatamente a mesma 
- Para dividir uma medida por 1/3, uma pessoa pode multiplicar a medida por 1/3, pois a divi
- Uma folha apresenta 0,5 como taxa decimal de participação. A mesma taxa pode ser represent
- Ao obter a raiz quadrada principal de 16, um cálculo pode registrar -4 como resultado, poi
- O valor de uma divisão exibido como 0,6666..., com repetição indefinida do algarismo 6, po
- Dois saldos estão expressos por -5/6 e -2/3. O saldo -5/6 é maior porque sua fração tem nu
- Retirou-se 1/4 de um estoque. Se metade da quantidade remanescente também foi retirada, so
- No cálculo de (-3)², o resultado é -9, porque o sinal de menos permanece após a potenciaçã
- Ao somar as parcelas 1/3 e 1/6 de uma mesma unidade, obtém-se exatamente metade da unidade
- Um intervalo foi descrito como [1,3). Uma medição exatamente igual a 3 pertence a esse int
- A fração 1/8 possui representação decimal finita porque, na forma irredutível, seu denomin
- Uma calculadora mostra a expansão decimal infinita 0,999... . Esse número é estritamente m
- Para a dízima 0,1666..., a representação 0,1(6) indica anteperíodo 1 e período 6, e sua ge
- Para a=1/2 e b=1/3, elevar a soma ao quadrado fornece o mesmo resultado que a²+b², pois a 
- Se duas posições distintas a e b são racionais e a é menor que b, a média (a+b)/2 fornece 
- Para todo racional a, a raiz quadrada principal de a² é igual a a, inclusive quando a repr
- Se x=0,23454545..., com 23 antes do período 45, a eliminação da parte periódica pode ser e
- Após retirar 1/4 de um conjunto, selecionar 2/5 do restante equivale a selecionar 2/5 do t
```

## Refazer 10: Teoria do crime (`direito-penal.teoria-do-crime`, 2075 tópicos de edital)

```
ATENÇÃO: da outra vez a correta saiu a mais curta demais. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: direito-penal.teoria-do-crime (Teoria do crime)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Infração penal, teorias do crime e fato típico": 18
- "2. Causalidade, omissão, dolo, culpa e agravação do resultado": 22
- "3. Consumação, tentativa, desistência e crime impossível": 22
- "4. Erros penais, descriminantes putativas e culpabilidade": 20
- "5. Ilicitude, causas justificantes e excessos puníveis": 18

Já existem (não repita):
- Uma lei prevê para determinada infração a pena de prisão simples, alternativamente à multa
- Em uma análise segundo o finalismo, o julgador precisa identificar em qual elemento do cri
- Uma pessoa provoca resultado material por ação sem a qual ele não teria ocorrido. Que disp
- Quem assume formalmente a proteção de uma pessoa vulnerável deixa de agir quando podia imp
- Uma pessoa pratica ação prevendo o resultado danoso e efetivamente assumindo o risco de pr
- Em situação de trabalho, a pessoa deixa de adotar cautela exigida, produzindo resultado pr
- O agente inicia a execução de delito, mas é contido por terceiro antes do resultado exigid
- Após praticar parte da execução, o agente percebe que poderia prosseguir, mas decide livre
- Um agente tenta matar mediante substância totalmente inofensiva, acreditando ser veneno le
- Por equívoco de fato, alguém leva um objeto idêntico ao seu, acreditando ser de sua propri
- Em crime material, um ferimento inicial é seguido de acidente hospitalar autônomo que, por
- Alguém prevê a possibilidade de resultado lesivo, mas acredita concretamente que suas habi
- O tipo de crime prevê resultado que agrava especialmente a pena. O resultado mais grave oc
- Após executar a conduta que poderia produzir morte, a pessoa providencia socorro decisivo 
- O autor de delito patrimonial cometido sem violência ou grave ameaça à pessoa restitui vol
- O agente conhece todos os fatos relevantes, mas não percebe a proibição da conduta por err
- Por erro sobre a pessoa, o autor agride quem confundiu com a vítima visada. Qual diretriz 
- Uma pessoa supõe, por circunstâncias plenamente justificáveis, agressão armada iminente qu
- Para afastar dano de inundação atual que não provocou, alguém danifica obstáculo alheio qu
- Um ataque injusto está em curso, e a pessoa reage com meios necessários e intensidade mode
- Ao ser atacada, uma pessoa emprega força adequada e neutraliza o agressor. Depois que ele 
- Um agente inicia o ato por ameaça moral irresistível de terceiro. Em outro caso, seu braço
- Uma pessoa age acreditando, de modo evitável, que certa conduta plenamente conhecida é jur
- Durante ação, alguém utiliza meio que falha naquela ocasião, mas que era objetivamente apt
- Alguém deixa de impedir resultado, apesar de saber que havia criado previamente o risco, t
- Uma norma comina prisão simples ou multa para certa infração. De acordo com o critério do 
- Um agente inicia a execução de contravenção, mas é impedido por terceiro. A tentativa deve
- A pessoa inicia execução de crime e é interrompida por intervenção externa, sem que se com
- Se o agente pratica resultado por negligência, é possível puni-lo pela modalidade culposa 
- Quando alguém repele injusta agressão atual usando moderadamente os meios necessários, pod
- Depois de causar lesão, o agente se arrepende, mas o resultado morte ocorre apesar de soco
- O agente que responde pela criação anterior de risco e podia evitar o resultado pode ocupa
- No arrependimento posterior do art. 16, a reparação feita voluntariamente depois do recebi
- No erro de tipo essencial, o desconhecimento de elemento constitutivo do tipo exclui o dol
- A pessoa que sofre ameaça moral irresistível de terceiro terá necessariamente exclusão da 
- Uma pessoa dispara contra alguém pensando tratar-se de outra vítima; o erro sobre identida
- Uma causa superveniente relativamente independente exclui imputação do resultado mesmo qua
- Se uma pessoa pratica delito com dolo inicial e provoca resultado especialmente agravador 
- Em hipótese de erro de proibição evitável, o art. 21 determina isenção de pena exatamente 
- Na descriminante putativa fática, erro plenamente justificado sobre situação que legitimar
- No estado de necessidade, quem tinha dever legal de enfrentar o perigo pode invocar livrem
- Mesmo quando se reconhece legítima defesa no início da reação, quem prossegue dolosamente 
- Conforme a Súmula 145 do STF, a simples vigilância policial de local de delito, sem induzi
- O art. 25, parágrafo único, exige observância dos requisitos do caput ao disciplinar defes
- Uma pessoa pratica conduta formalmente típica em estrito cumprimento de dever legal e dent
```

## Refazer 11: Receita e despesa pública (`afo.receita-despesa-publica`, 1986 tópicos de edital)

```
ATENÇÃO: da outra vez a mesma frase final foi repetida em 26 explicações. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: afo.receita-despesa-publica (Receita e despesa pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Receita pública: ingressos, categorias econômicas e classificações": 19
- "2. Estágios da receita, fonte de recursos e dívida ativa": 19
- "3. Despesa pública e classificações institucional, funcional, programática e por natureza": 18
- "4. Estágios da despesa, programação e descentralização da execução": 18
- "5. Restos a pagar, exercícios anteriores, suprimento de fundos e controles": 26

Já existem (não repita):
- Uma unidade pública recebe caução em dinheiro de uma empresa, a ser devolvida quando encer
- Uma entidade pública arrecada aluguel de prédio próprio e, em operação separada, vende um 
- Um município cobra imposto predial por imposição legal e recebe pagamento contratual pelo 
- No mesmo mês, um ente contrata empréstimo de longo prazo para obras e operação de crédito 
- Um contribuinte paga imposto em banco arrecadador, mas a transferência à conta central da 
- Um aluguel devido ao ente público venceu, não foi pago e teve a liquidez e certeza apurada
- Uma receita orçamentária foi efetivamente arrecadada no exercício, embora sua estimativa n
- Recursos recebidos para finalidade específica por convênio permanecem em caixa ao terminar
- Uma unidade registra juros de empréstimo público, aquisição de equipamentos novos e amorti
- Um orçamento identifica a aplicação direta de recursos para aquisição de material de consu
- Ao montar relatório, uma equipe deseja identificar qual unidade orçamentária é responsável
- Uma despesa foi registrada como função de saúde e subfunção específica de atuação governam
- O planejamento prevê construir nova unidade pública em dois anos e manter diariamente os s
- Ao analisar o código 3.3.90.30 de uma despesa orçamentária, um estudante precisa compreend
- Um contrato de prestação continuada tem valor anual certo e pagamentos mensais pré-definid
- Uma nota fiscal foi apresentada para pagamento de equipamentos, mas o responsável ainda nã
- No encerramento do ano, uma obrigação legal foi empenhada em dezembro, mas só liquidada e 
- Uma lei orçamentária foi publicada em 2 de janeiro e a equipe prepara a programação financ
- Ao final de um bimestre, a receita prevista para o restante do exercício não comporta as m
- Uma unidade orçamentária descentraliza crédito para que unidade de órgão distinto execute 
- Em 31 de dezembro, uma despesa havia sido empenhada, seu serviço regularmente prestado e l
- Em fevereiro, reconhece-se obrigação por serviço efetivamente prestado no ano anterior, qu
- Um servidor já responde por dois adiantamentos não encerrados e solicita novo suprimento p
- Ao comparar passivos públicos com créditos a receber, uma equipe encontra restos a pagar, 
- No fechamento anual, há R$ 100 mil empenhados: R$ 40 mil liquidados e não pagos, R$ 35 mil
- Um contrato de aluguel de imóvel público gera receita originária e, quanto à categoria eco
- Uma caução devolvível depositada em dinheiro deve constar como receita corrente tributária
- Um tributo previsto e lançado foi recebido por agente arrecadador em novembro, mas recolhi
- Uma dívida de aluguel público vencida e inscrita após apuração de liquidez e certeza const
- Os valores recebidos com destinação legal a um programa específico mantêm essa vinculação 
- O pagamento de juros sobre empréstimo público e a amortização do seu principal são ambos c
- A classificação institucional permite identificar órgão e unidade orçamentária, enquanto a
- Uma receita corrente decorrente de tributo foi lançada em dezembro e paga somente em fever
- A inscrição de crédito exigível em dívida ativa exige apuração de liquidez e certeza, e nã
- O projeto de construção de nova unidade e a atividade de manutenção do atendimento continu
- Um contrato de valor anual conhecido, pago em parcelas mensais, pode ser empenhado globalm
- A modalidade de aplicação da despesa tem a mesma finalidade do elemento de despesa: indivi
- Em uma unidade que recebeu destaque orçamentário para executar ação de outra estrutura, o 
- Quando a arrecadação do primeiro bimestre frustra as metas fiscais, a LRF exige limitação 
- Um empenho à conta de crédito com vigência plurienal ainda não havia sido liquidado ao fim
- Uma obrigação empenhada e liquidada regularmente em dezembro, mas não paga, pode ser autom
- Um servidor utilizou suprimento de fundos até 31 de dezembro na esfera federal. Quanto aos
- Um crédito não tributário de aluguel do patrimônio público, inscrito para cobrança, integr
- Na esfera federal, o suprimento de fundos exige prévio empenho; a restituição de valor não
- Nos casos especiais do art. 60, § 1º, da Lei nº 4.320/1964, a dispensa de emissão da nota 
```

## Refazer 12: Lei de Improbidade Administrativa (Lei 8.429/1992) (`legislacao.lei-8429-1992-improbidade`, 1983 tópicos de edital)

```
ATENÇÃO: da outra vez a correta saiu a mais curta em 31 de 50. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: legislacao.lei-8429-1992-improbidade (Lei de Improbidade Administrativa (Lei 8.429/1992))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema de improbidade: fundamento, dolo e sujeitos": 26
- "2. Tipos legais: enriquecimento, dano ao erário e princípios": 27
- "3. Sanções, dosimetria e efeitos patrimoniais": 15
- "4. Apuração, indisponibilidade, ação e solução consensual": 18
- "5. Prescrição, retroatividade e jurisprudência atual": 14

Já existem (não repita):
- Um responsável assinou ato baseado em orientação jurídica plausível, mas depois o ato foi 
- Uma pessoa exerceu gratuitamente, por designação, funções públicas durante dois meses e pa
- Um empresário combinou deliberadamente com agente público a fraude de pagamentos e fornece
- Um gestor direcionou dolosamente a aquisição de materiais com sobrepreço e os autos compro
- Uma pessoa deixa de prestar contas disponíveis, propositalmente, para encobrir irregularid
- Um agente recebeu comissão de interessado para facilitar a liberação de recursos sob sua r
- Uma autoridade usa designações recíprocas para colocar parentes em funções de confiança, i
- Após licitação irregular, não foi demonstrado que o poder público pagou preço excessivo ou
- Um fiscal praticou ato voluntário que foi posteriormente anulado por violação regulamentar
- Na condução de processo seletivo, agente adulterou intencionalmente critérios para conferi
- Uma condenação por enriquecimento ilícito preenche o art. 9º, e a decisão busca identifica
- Reconheceu-se ato doloso que causou perda material ao erário e se enquadra no art. 10. O j
- O julgamento reconheceu somente improbidade dolosa do art. 11, sem acréscimo patrimonial e
- Um condenado por ato doloso com dano ao erário faleceu, deixando herança inferior ao preju
- Um agente entrega a declaração exigida ao assumir função pública, mas deixa o exercício me
- Uma apuração reúne indícios especialmente fortes de enriquecimento ilícito, mas a urgência
- Em pedido de indisponibilidade, discutem-se R$ 30 mil mantidos em conta-corrente, quantia 
- Um requerimento de indisponibilidade pretende garantir ressarcimento, multa civil e eventu
- Uma pessoa jurídica diretamente prejudicada ajuíza ação de improbidade, embora o art. 17 c
- Os legitimados negociam acordo de não persecução civil após descobrir desvio patrimonial e
- O juiz recebeu petição inicial de ação de improbidade em devida forma e ordenou a citação 
- Durante a ação, decisão motivada afasta cautelarmente um agente de suas funções para prote
- Um ato culposo teria ocorrido em 2020 e era discutido em processo ainda sem trânsito em ju
- Em ação de improbidade instaurada após a reforma, ocorreu marco interruptivo previsto no a
- Uma demanda pretende apenas ressarcimento de desvio de recursos, comprovadamente decorrent
- Uma pessoa designada por curto período, sem remuneração, pode ser agente público para fins
- A mera assinatura voluntária de decisão administrativa posteriormente anulada é suficiente
- O recebimento doloso de vantagem patrimonial indevida, ligada às atribuições do agente, po
- A frustração dolosa de licitação, sem qualquer demonstração de perda material, preenche ne
- O art. 11 pode ser aplicado a condutas tipificadas que causem lesividade relevante, mesmo 
- Se o fato se enquadrar apenas no art. 11, a lei prevê suspensão de direitos políticos até 
- Em caso de herança recebida após ato doloso lesivo ao erário, a obrigação de reparar pode 
- A aprovação das contas por tribunal de contas impede, como regra absoluta, a investigação 
- Quando a pessoa jurídica lesada propõe diretamente a ação de improbidade, há legitimidade 
- A pessoa física que é apenas sócia de empresa investigada por improbidade responde automat
- Uma indisponibilidade de bens pode ser examinada cautelarmente antes do trânsito em julgad
- Após as ADIs 7.156 e 7.236, é obrigatório demonstrar perigo concreto de dano em toda hipót
- Uma pessoa que deixa de prestar contas para ocultar irregularidades, apesar de dispor de c
- O prazo de contestação dos requeridos, na ação de improbidade, corresponde aos noventa dia
- O art. 23, § 1º, prevê que a instauração de inquérito civil ou processo administrativo pod
- O julgamento de julho de 2026 manteve o reinício da prescrição pela metade, de modo que qu
- A pretensão de ressarcimento ao erário por ato doloso tipificado na LIA é imprescritível s
- Segundo o Tema 1.199, a lei de 2021 que suprimiu improbidade culposa desfaz automaticament
- Após o julgamento de junho de 2026, a indisponibilidade pode cobrir valores de multa civil
- Uma absolvição criminal colegiada por insuficiência probatória impede necessariamente o pr
```

## Refazer 13: Microsoft Word 2016 (`informatica.microsoft-word-2016`, 1958 tópicos de edital)

```
ATENÇÃO: da outra vez explicações de Certo/Errado falando em "alternativa". Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: informatica.microsoft-word-2016 (Microsoft Word 2016)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura, arquivos e interface": 20
- "2. Edição, formatação e página": 22
- "3. Elementos, referências e campos": 20
- "4. Reescrita de procedimentos e como o tema costuma ser cobrado": 19
- "5. Revisão, controle de alterações, proteção e acessibilidade": 19

Já existem (não repita):
- Qual formato é usado normalmente para salvar um documento do Word 2016 sem macros?
- Qual formato serve como base reutilizável para criar documentos semelhantes sem sobrescrev
- Considerando o padrão internacional (versão em inglês) do Word, qual atalho salva as alter
- Para iniciar a verificação de ortografia e gramática no Word para Windows, qual tecla é us
- Que atalho insere uma quebra de página manual no ponto de inserção?
- Qual comando abre a visualização e as opções de impressão no Word?
- Qual guia contém o comando de colunas para formatar texto como em um boletim?
- Para inserir um cabeçalho no documento do Word 2016, qual caminho é adequado?
- Um relatório deve mudar para paisagem apenas em uma página intermediária. Qual procediment
- Como se cria um sumário automático que acompanha títulos do documento?
- Qual comando insere legenda numerada para uma figura no Word?
- Qual ação atualiza o resultado de um campo selecionado, como um número de página ou sumári
- Qual atalho marca uma entrada para o índice remissivo no Word?
- Na mala direta, qual elemento identifica onde o nome de cada destinatário aparecerá?
- Uma unidade envia comunicados personalizados a partir de uma planilha. Qual combinação com
- Qual recurso impede que o destinatário abra o arquivo sem fornecer a senha configurada?
- Qual afirmação diferencia Controlar Alterações de Restringir Edição?
- Uma servidora precisa localizar clip-art online e inserir o resultado escolhido no documen
- A secretaria recebeu linhas de nomes e matrículas separadas por tabulações e quer convertê
- Uma ordem de serviço contém quatro etapas numeradas e subetapas que devem permanecer hiera
- O texto deve passar de uma seção em duas colunas para uma única coluna na mesma página. Qu
- Em uma citação normativa, o símbolo de parágrafo (§) deve aparecer antes do número, e a ab
- Qual procedimento mantém páginas anteriores sem número e inicia a numeração em parte poste
- Qual afirmação sobre os campos do Word está correta?
- A servidora precisa imprimir apenas as páginas 3 a 5, em duas cópias, e quer conferir o re
- A primeira seção do relatório precisa de margens próprias, sem alterar as páginas seguinte
- Um quadro de lotação exige dados alinhados por servidor e matrícula; a exposição seguinte 
- A capa de um relatório deve ter rodapé distinto do restante da seção. Que opção atende ao 
- Após visualizar os registros personalizados de uma mala direta, qual etapa gera os documen
- Uma circular precisa destacar um aviso lateral, com texto próprio, ao lado de um parágrafo
- No Word 2016 em português (configuração padrão Brasil), qual conjunto de atalhos executa, 
- Ao utilizar o mouse para selecionar conteúdos rapidamente no corpo de um texto do Word 201
- Qual é a diferença de comportamento entre acionar o ícone do Pincel de Formatação com um c
- Durante a revisão de um ofício no Word 2016, o usuário observa palavras grifadas com subli
- Um servidor selecionou um parágrafo que estava em Negrito, Itálico, fonte tamanho 18 e des
- Para formatar referências bibliográficas segundo normas técnicas em que a primeira linha d
- Uma tabela extensa ocupa três páginas consecutivas de um parecer técnico, e o chefe do set
- Dois analistas receberam cópias independentes da mesma minuta de contrato e fizeram altera
- Certo ou errado: Ctrl+S salva as alterações do documento ativo na instalação padrão em ing
- Certo ou errado: um arquivo .dotx é um modelo do Word sem macros.
- Certo ou errado: Ctrl+Enter insere uma quebra de página manual no Word.
- Certo ou errado: Controlar Alterações, por si só, exige senha para abrir o documento.
- Certo ou errado: estilos de título podem servir de base a um sumário automático.
- Certo ou errado: uma quebra de página permite, sozinha, cabeçalho independente no trecho s
- Certo ou errado: F9 pode atualizar o campo selecionado no documento.
- Certo ou errado: cada linha de uma fonte de mala direta costuma representar um registro de
- Certo ou errado: digitar { PAGE } manualmente cria necessariamente um campo funcional de n
- Certo ou errado: em uma seção vinculada à anterior, o cabeçalho pode repetir o conteúdo da
- Certo ou errado: na mala direta, inserir destinatários e inserir campos de mesclagem são a
- Certo ou errado: criptografar com senha e restringir edição cumprem exatamente a mesma fun
- Certo ou errado: o índice de ilustrações pode ser montado a partir de legendas inseridas n
- Certo ou errado: restringir a edição torna dispensável revisar os campos e conferir a visu
- Certo ou errado: uma nova legenda sempre atualiza automaticamente o índice de ilustrações 
- Certo ou errado: alterar o nível de zoom na barra de status de 100% para 200% dobra o tama
- Certo ou errado: quando o comando Mostrar Tudo (¶) está ativado na guia Página Inicial, os
- Certo ou errado: pressionar Shift+Enter insere uma quebra manual de linha, deslocando o cu
- Certo ou errado: na janela de impressão (Ctrl+P), informar o intervalo personalizado '1; 4
- Certo ou errado: a Área de Transferência do Office no Word 2016 armazena no máximo 1 único
- Certo ou errado: ao aplicar o comando Editar Lista de Destinatários na Mala Direta e desma
- Certo ou errado: o comando Marcar como Final criptografa o documento com chave simétrica e
- Certo ou errado: o atalho Ctrl+Shift+F9 desvincula um campo selecionado no Word 2016, conv
- Certo ou errado: se um documento com Controlar Alterações ativo e exibido no modo 'Todas a
```

## Refazer 14: Análise combinatória (`matematica.analise-combinatoria`, 1569 tópicos de edital)

```
ATENÇÃO: da outra vez duas frases de molde repetidas em 48 explicações. Não repita isso: correta do mesmo tamanho das erradas, explicação própria em cada questão.

Matéria: matematica.analise-combinatoria (Análise combinatória)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios de contagem e organização de casos": 48
- "2. Fatorial e permutações simples e com repetição": 13
- "3. Arranjos, combinações e repetição permitida": 15
- "4. Contagens com restrições, união, complementar e distribuição": 12
- "5. Binômio de Newton e triângulo de Pascal": 12

Já existem (não repita):
- Uma loja oferece três modelos de camiseta e quatro modelos de calça. O comprador escolherá
- Uma pessoa vai optar por uma única atividade: há cinco passeios culturais ou três trilhas 
- Quatro livros diferentes devem ocupar quatro posições consecutivas de uma prateleira. Quan
- A palavra OVO tem duas letras O idênticas e uma letra V. Quantos anagramas distintos podem
- Cinco pessoas distintas concorrem às funções de coordenador e secretário. Uma mesma pessoa
- Seis estudantes distintos formarão uma comissão de dois membros, sem funções diferenciadas
- Um código usa três posições, cada uma preenchida por um dos cinco símbolos disponíveis. É 
- Um estudo usa os coeficientes da expansão de um binômio elevado ao cubo. Quanto vale a som
- Em um grupo de inscritos, nove praticam corrida, sete praticam ciclismo e quatro praticam 
- Usando os dígitos 0, 1, 2, 3, 4 e 5 sem repetição, quantos números de três algarismos pode
- A palavra BANANA tem seis letras, com três A e dois N. Quantos anagramas diferentes podem 
- Cinco pessoas distintas farão uma fila. Duas delas devem ocupar posições consecutivas. Qua
- Uma equipe de três entre oito pessoas deve conter obrigatoriamente duas pessoas específica
- Sete candidatos distintos disputarão três posições classificatórias ordenadas, sem empates
- Três bolas de sorvete serão escolhidas entre quatro sabores. Um sabor pode ser repetido, e
- Com os algarismos 1, 2, 3 e 4, sem repetição e utilizando os quatro, quantos números pares
- Na expansão de (x+2) elevado à quarta potência, qual é o coeficiente do termo que contém x
- Cinco pessoas distintas devem ocupar lugares em fila. Duas pessoas designadas não podem fi
- Uma comissão de três integrantes será escolhida entre oito pessoas. Pelo menos uma das dua
- Com dígitos de 0 a 5, sem repetir, serão formados números pares de três algarismos. Quanto
- Quatro pessoas distintas serão distribuídas em dois setores identificados, Alfa e Beta, ex
- Uma senha de quatro posições permite os dígitos 0, 1, 2, 3 e 4, com repetição. Exige-se a 
- No desenvolvimento de (x² + 1/x) elevado à sexta potência, qual é o coeficiente do termo i
- Em uma expansão de (x−2) elevado à quinta potência, pretende-se encontrar o coeficiente de
- Seis pessoas distintas serão separadas em dois grupos de três, sem nome para os grupos e s
- Uma loja oferece quatro chapéus e três lenços; quando a pessoa escolhe um chapéu e um lenç
- Se dois conjuntos de possibilidades possuem elementos em comum, a quantidade de elementos 
- O fatorial de zero é um, definição compatível com a contagem de uma única escolha vazia e 
- Ao organizar quatro livros todos distintos, existem dezesseis filas possíveis, porque a pe
- Para selecionar dois membros de um grupo de cinco, sem distribuir funções, o número de dup
- Uma senha de dois caracteres escolhidos entre seis símbolos com repetição permitida admite
- A soma dos coeficientes do desenvolvimento formal da quarta potência de um binômio é dezes
- Com os algarismos 0, 1, 2, 3 e 4, sem repetição, existem sessenta números de três algarism
- A palavra TATU admite doze anagramas distintos usando as quatro letras, pois a repetição d
- Ao selecionar três pessoas entre sete para exercer funções diferentes, a combinação simple
- Uma comissão de três entre seis pessoas, com presença obrigatória de duas pessoas predeter
- No desenvolvimento da quarta potência de (x+3), o coeficiente de x² é seis, pois basta cal
- Entre cinco pessoas distintas, se duas específicas devem ficar lado a lado em fila, o núme
- Em uma fila de cinco pessoas distintas, duas pessoas específicas devem permanecer separada
- Seis pessoas distintas podem ser separadas em dois grupos sem identificação, com três memb
- Senhas de quatro posições, preenchidas pelos símbolos 0, 1, 2, 3 e 4, com repetição, apres
- Se seis pessoas distintas ocupam lugares sem numeração em torno de mesa redonda e rotações
- Na expansão da sexta potência de (x² + 1/x), o termo independente de x tem coeficiente vin
- Se treze participantes escolheram a oficina A, dez escolheram a oficina B e cinco escolher
- Quando se escolhem três unidades entre quatro tipos disponíveis, podendo repetir o tipo e 
```

