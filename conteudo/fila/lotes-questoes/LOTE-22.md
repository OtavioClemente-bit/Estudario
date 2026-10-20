# Pedidos de questões extras, lote 22 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 869 tópicos de edital.

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

## Pedido 1: Geologia geral e de engenharia (`engenharia.geologia-mineracao`, 21 tópicos de edital)

```
Matéria: engenharia.geologia-mineracao (Geologia geral e de engenharia)
Capítulos e quantas questões quero em cada um (total 100):
- "Minerais, petrologia e ciclo das rochas": 17
- "Dinâmica terrestre, intemperismo e geomorfologia": 17
- "Geologia estrutural e tectônica": 17
- "Estratigrafia e tempo geológico": 17
- "Hidrogeologia e proteção de aquíferos": 16
- "Cartografia, investigação e riscos geológicos de engenharia": 16

Já existem (não repita):
- Durante a identificação de uma amostra transparente e de brilho vítreo, o técnico observa 
- Em um afloramento, um corpo tabular ígneo atravessa perpendicularmente estratos sedimentar
- Um granito apresenta grandes cristais de feldspato imersos em matriz cristalina de granula
- Assinale a alternativa que representa adequadamente a gênese de uma rocha metamórfica.
- Qual situação descreve predominantemente um processo de intemperismo químico?
- Sobre a dinâmica das placas tectônicas, selecione a associação correta entre o limite e se
- Um vale retilíneo observado em imagens orbitais coincide com mudanças abruptas na orientaç
- Em relevo carbonático, surgem dolinas, cavidades e drenagens subterrâneas. A explicação ge
- Em um corte geológico, o teto de uma falha inclinada deslocou-se para baixo relativamente 
- Um plano de acamamento possui direção norte–sul e mergulho verdadeiro de 35° para leste. E
- Em um maciço fraturado, técnicos observam fluxo concentrado ao longo de uma parte da zona 
- Em uma sequência dobrada e invertida, a forma geométrica da dobra é convexa para cima, mas
- Em uma sucessão sedimentar não invertida, sem falhas e sem discordâncias, qual camada tend
- Uma superfície erosiva corta camadas antigas fortemente inclinadas; sobre ela se acumulam 
- Duas formações de litologia semelhante afloram em bacias diferentes, mas fósseis e níveis 
- Considere a sequência: folhelho dobrado, erosão da estrutura dobrada, deposição de arenito
- Um poço perfura um aquífero confinado; após a conclusão, o nível da água estabiliza acima 
- A lei de Darcy é aplicada com K = 2 × 10⁻⁵ m/s, gradiente hidráulico de 0,01 e área perpen
- Um aquífero cárstico apresenta alta conectividade de condutos e recebe aporte de contamina
- Em levantamento hidrogeológico, dois poços medem cargas hidráulicas de 120 m e 116 m, sepa
- Um mapa geológico está na escala 1:50.000. Qual distância aproximada no terreno correspond
- No planejamento territorial, uma carta indica grande predisposição física a movimentos de 
- Uma anomalia de resistividade elétrica sugere contrastes no subsolo ao longo do traçado de
- Em um sistema de informação geográfica, o técnico deseja registrar contatos geológicos com
- Na identificação macroscópica de minerais, a cor observada isoladamente é suficiente para 
- A presença de cristais maiores em uma matriz de granulação menor é compatível com textura 
- O fato de uma soleira acompanhar o acamamento sedimentar implica que a soleira e os estrat
- O metamorfismo regional pode gerar foliação pela orientação preferencial de minerais sem q
- O intemperismo químico envolve mudanças na composição ou na estrutura de minerais, enquant
- Terremotos somente podem ocorrer sobre o traçado exato dos limites entre placas tectônicas
- Um alinhamento retilíneo de drenagem identificado em imagem orbital, sem comprovação de ca
- Uma junta é uma descontinuidade na rocha para a qual não se reconhece deslocamento cisalha
- Em uma falha reversa, o teto se desloca para baixo relativamente ao muro, sendo esse o cri
- Uma zona de falha pode funcionar como conduto em faixas fraturadas e como barreira em segm
- O mergulho aparente observado em uma seção oblíqua à direção de máximo declive de um plano
- O princípio da superposição pode ser aplicado diretamente, sem ressalvas, a qualquer suces
- Em uma discordância angular, estratos antigos inclinados podem ser truncados por erosão e 
- Os limites de uma formação litoestratigráfica são obrigatoriamente isócronos, porque toda 
- Em situação típica, o nível freático constitui o limite superior saturado de um aquífero l
- Dois materiais com a mesma porosidade total necessariamente possuem igual condutividade hi
- A velocidade de Darcy, calculada como Q/A, coincide sempre com a velocidade média efetiva 
- O movimento da água subterrânea é governado pelo gradiente de carga hidráulica, de modo qu
- Entre duas cartas na mesma projeção e com qualidade equivalente, a escala 1:25.000 é maior
- A simples identificação de elevada suscetibilidade a deslizamentos implica, mesmo em local
- Métodos geofísicos, a exemplo da eletrorresistividade, fornecem medidas indiretas que deve
```

## Pedido 2: Ensino de química (`educacao.ensino-quimica`, 21 tópicos de edital)

```
Matéria: educacao.ensino-quimica (Ensino de química)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Química no currículo: BNCC, DCRC e itinerários formativos": 20
- "2. Abordagens metodológicas, contextualização e perspectiva CTSA": 20
- "3. Experimentação didática, recursos e segurança no laboratório escolar": 20
- "4. História da Química, natureza da ciência e concepções alternativas": 20
- "5. Avaliação da aprendizagem e planejamento inclusivo em Química": 20

Já existem (não repita):
- Em reunião pedagógica, um professor sustenta que a BNCC do ensino médio apresenta, por ano
- Ao preparar aulas para a rede cearense, uma candidata precisa relacionar BNCC e DCRC. Assi
- Um edital de 2026 cobra a regra geral de cargas horárias do ensino médio estabelecida pela
- Em uma sequência didática sobre água e semiárido, qual decisão revela alinhamento entre cu
- Uma escola planeja itinerário de aprofundamento relacionado a materiais e economia circula
- No ensino médio, a Química participa da área de Ciências da Natureza e suas Tecnologias se
- O Documento Curricular Referencial do Ceará torna facultativa a observância da BNCC nas in
- A legislação de 2024 prevê tratamento próprio para formação geral básica em itinerários de
- A simples adoção de um tema regional no plano de aula garante, por si só, o desenvolviment
- Qual situação melhor exemplifica uma abordagem CTSA para ensinar Química?
- Em discussão sobre representações químicas, o professor deseja evitar que estudantes confu
- Após ler previamente um texto, alunos desenvolvem um problema interdisciplinar com orienta
- O professor pergunta se um material deve ser usado numa praça pública e solicita afirmação
- Em um curso técnico, uma docente deseja integrar teoria e prática na análise de registros 
- Contextualizar um conteúdo químico exige necessariamente discutir como a ciência, a tecnol
- Em uma abordagem CTSA, decisões sociotécnicas podem exigir tanto conhecimentos científicos
- Transposição didática autoriza simplificações que devem manter coerência com o conheciment
- Uma turma pretende investigar o efeito de uma variável sobre uma observação em atividade s
- Uma escola não dispõe de laboratório equipado. Qual planejamento conserva qualidade didáti
- Durante uma atividade investigativa, a turma obtém resultado que contraria sua hipótese in
- Ao utilizar uma animação de partículas numa aula de Química, qual cuidado pedagógico é mai
- Ao planejar uma atividade de baixo custo, a professora precisa decidir sobre armazenamento
- Uma prática demonstrativa pode gerar aprendizagem se os estudantes fizerem previsões, disc
- Em razão de sua finalidade pedagógica, o laboratório escolar está dispensado de regras de 
- Quando duas condições são modificadas simultaneamente, uma diferença observada pode não pe
- Um estudante diz que substância natural “não tem química”. Qual primeira intervenção é did
- Em uma aula de história da Química, qual abordagem evita uma narrativa anacrônica e heroic
- Depois de observar uma situação que contraria sua previsão, o aluno mantém a concepção ant
- Qual enunciado descreve adequadamente a relação entre teorias e leis científicas no ensino
- Um aluno interpreta literalmente todas as cores e tamanhos de um modelo molecular usado em
- Concepções alternativas podem apresentar coerência interna para o estudante e devem ser in
- O ensino histórico da Química é suficiente quando apresenta a ordem cronológica dos grande
- Uma analogia didática útil deve permitir explicitar tanto correspondências com o conceito 
- Uma professora aplica uma atividade, identifica dificuldades e reorganiza as aulas com dev
- Uma rubrica para relatório experimental só premia “resultado correto” e apresentação estét
- A escola quer avaliar argumentação CTSA a respeito de materiais e impactos ambientais. Qua
- Em atividade coletiva inclusiva de Química, como a professora pode avaliar a aprendizagem 
- A avaliação formativa é definida por sua finalidade de orientar aprendizagens e intervençõ
- Uma prova de Química só pode ter função somativa, pois seu formato é incompatível com devo
- Ao avaliar um projeto CTSA, uma rubrica pode considerar simultaneamente qualidade das evid
```

## Pedido 3: Ensino de física (`educacao.ensino-fisica`, 21 tópicos de edital)

```
Matéria: educacao.ensino-fisica (Ensino de física)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Física escolar, currículo e planejamento por competências": 17
- "2. Experimentação de baixo custo e investigação segura": 17
- "3. Modelagem, analogias, matemática e simulações": 17
- "4. Concepções alternativas, aprendizagem e avaliação em Física": 17
- "5. CTSA, decolonialidade, ERER e inclusão em Física": 16
- "6. História e filosofia da ciência: natureza da Física e integração didática": 16

Já existem (não repita):
- O ensino de Física pode integrar modelagem, investigação e argumentação sem abandonar a fo
- No ensino médio, a Física deixa de integrar Ciências da Natureza e suas Tecnologias, pois 
- Uma atividade experimental pode ser investigativa com materiais cotidianos, desde que envo
- Uma experiência com roteiro inteiramente fechado torna-se necessariamente investigação abe
- Todo modelo didático em Física deve reproduzir perfeitamente o sistema real, sob pena de n
- A avaliação formativa pode utilizar prova escrita se suas evidências forem usadas para ori
- Abordagem CTSA pressupõe analisar também os impactos sociais e ambientais associados à pro
- Uma simulação computacional exibe o fenômeno real sem escolhas de modelagem, simplificaçõe
- Na abordagem previsão–observação–explicação, solicitar a justificativa da previsão ajuda a
- A contextualização do ensino de Física é suficiente quando o professor menciona um objeto 
- Resultados que contrariem uma hipótese devem ser excluídos imediatamente da tabela para pr
- A mesma questão discursiva de Física pode atender a finalidades diagnósticas, formativas o
- No ensino de Física para estudantes com deficiência, remover barreiras de acesso aos gráfi
- Uma analogia é pedagogicamente válida apenas se não apresentar nenhuma diferença entre dom
- Para trabalhar Natureza da Ciência, é adequado discutir o papel de instrumentos, controvér
- Em Física, uma teoria científica bem corroborada transforma-se em lei científica depois de
- Um currículo de Física atento à decolonialidade pode questionar apagamentos e assimetrias 
- Em uma atividade com simulador, alterar simultaneamente vários parâmetros pode dificultar 
- A persistência de uma ideia intuitiva após a aula prova que a observação foi inútil, pois 
- Em uma sequência CTSA sobre energia eólica, a Física determina sozinha qual política públi
- A confiabilidade de conhecimento científico e seu caráter revisável são incompatíveis, por
- Uma professora planeja uma sequência sobre consumo de energia. Qual combinação melhor expr
- No ensino fundamental e médio, respectivamente, como costuma ocorrer a organização curricu
- Em uma experiência com pêndulo simples, qual procedimento favorece a comparação entre comp
- Qual situação descreve melhor a natureza de uma simulação de Física?
- Para identificar concepções alternativas sobre força e movimento, qual instrumento oferece
- Em uma proposta CTSA sobre painéis solares, qual questão revela abordagem além de contextu
- Em aula de Física com estudante cego, o objetivo é identificar tendências em um gráfico. Q
- Um estudante afirma que a corrente elétrica é “gasta” ao passar por uma lâmpada em circuit
- Durante o laboratório, um grupo obtém dados discrepantes daqueles previstos. Qual interven
- No planejamento de uma atividade com PhET, que procedimento tende a ampliar a aprendizagem
- Qual afirmação sobre avaliação formativa em Física está correta?
- Uma equipe conclui um projeto de movimento e entrega apenas um cartaz bonito, sem explicar
- Uma atividade propõe estudar astronomias indígenas no ensino de Física. Qual encaminhament
- Qual estratégia melhor integra interdisciplinaridade e identidade da Física?
- Um professor afirma: “Se o aluno verificou a lei no experimento, sua concepção anterior fo
- Um item de concurso afirma que uma “lei científica” é o estágio final de maturação de uma 
- Ao discutir um experimento histórico, que escolha metodológica combate a imagem de ciência
- Uma sequência didática aplica modelo de partícula ao movimento de um veículo. Qual avaliaç
- Um município debate a instalação de aerogeradores. Qual é a posição coerente com CTSA?
- Em uma turma com aluno surdo, a professora pretende ensinar ressonância. Que decisão pedag
- Qual desenho avaliativo melhor diferencia evidências individuais e coletivas em investigaç
```

## Pedido 4: Navegação costeira, estimada, astronômica, radar e RIPEAM (`marinha-mercante.navegacao-ripeam`, 21 tópicos de edital)

```
Matéria: marinha-mercante.navegacao-ripeam (Navegação costeira, estimada, astronômica, radar e RIPEAM)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos: coordenadas, cartas e agulhas": 26
- "2. Navegação estimada, costeira e balizamento": 32
- "3. Navegação astronômica e radar": 19
- "4. RIPEAM: regras de governo": 23

Já existem (não repita):
- A milha náutica mede 1.852 metros e corresponde a um minuto de arco de latitude.
- A longitude de um ponto pode variar de:
- Na carta de projeção de Mercator, as distâncias devem ser medidas na escala de longitudes,
- A principal vantagem da projeção de Mercator para a navegação é que:
- O ângulo entre o norte magnético e o norte indicado pela agulha, causado pelo ferro do pró
- A declinação magnética varia conforme o local e com o passar do tempo, motivo pelo qual a 
- Um navio governa no rumo da agulha 100°. O desvio é 2° E e a declinação é 21° W. O rumo ve
- A agulha giroscópica indica o norte verdadeiro e não é afetada pelo magnetismo terrestre n
- Um navio na proa verdadeira 040° observa um farol na marcação relativa 090°. A marcação ve
- Um navio navega a 12 nós durante 2 horas e 30 minutos. A distância percorrida é de:
- Na navegação estimada, a posição é obtida a partir da última posição conhecida, do rumo, d
- O deslocamento lateral do navio causado pela ação do vento sobre suas obras mortas chama-s
- Na navegação costeira, a linha de posição considerada mais precisa é a obtida por:
- Duas linhas de posição que se cruzam em ângulo próximo de 90° fornecem posição mais confiá
- No sistema de balizamento IALA região B, adotado no Brasil, quem entra no porto vindo do m
- As marcas cardinais do sistema IALA indicam:
- A marca de perigo isolado é pintada de preto com faixas encarnadas e tem como tope duas es
- A correção da altura de um astro decorrente da elevação do olho do observador acima do nív
- No hemisfério norte, a altura da Estrela Polar acima do horizonte é aproximadamente igual 
- O instrumento usado para medir a altura dos astros acima do horizonte é o:
- O crepúsculo é o momento mais adequado para observar estrelas com o sextante, pois tanto o
- Na passagem meridiana, o astro:
- O radar de banda S, com comprimento de onda maior, tem melhor desempenho com chuva e mar g
- O controle do radar destinado a reduzir os ecos produzidos pelas ondas do mar nas proximid
- No ARPA, a sigla CPA designa:
- O uso do radar e do ARPA dispensa a vigilância visual no passadiço quando a visibilidade é
- Segundo o RIPEAM, existe risco de abalroamento quando a marcação de uma embarcação que se 
- Dois navios de propulsão mecânica navegam em rumos opostos, roda a roda, com risco de abal
- Na situação de cruzamento entre dois navios de propulsão mecânica, deve manobrar o navio q
- É considerada embarcação alcançadora aquela que se aproxima de outra vindo de direção:
- Após iniciar uma ultrapassagem, se a marcação mudar e a situação parecer de cruzamento, a 
- Na hierarquia de responsabilidades do RIPEAM, um navio de propulsão mecânica em navegação 
- A manobra para evitar abalroamento deve ser positiva, feita com antecedência e amplamente 
- À noite, um navio exibe duas luzes circulares encarnadas em linha vertical, sem luzes de m
- A luz de bordo de boreste é verde e a de bombordo é encarnada, cada uma com setor de 112,5
- A embarcação com capacidade de manobra restrita exibe, em linha vertical, as luzes:
- De dia, uma embarcação fundeada deve exibir, na parte de vante, uma esfera preta.
- Em embarcações à vista uma da outra, dois apitos curtos significam:
- Em visibilidade restrita, um navio de propulsão mecânica com seguimento deve emitir um api
- Entre os fatores que o RIPEAM manda considerar para definir a velocidade de segurança, NÃO
```

## Pedido 5: Cerimonial, protocolo e assessoria parlamentar (`processo-legislativo.cerimonial-protocolo`, 20 tópicos de edital)

```
Matéria: processo-legislativo.cerimonial-protocolo (Cerimonial, protocolo e assessoria parlamentar)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos do cerimonial, protocolo e etiqueta": 20
- "Precedência oficial e símbolos nacionais": 20
- "Planejamento de eventos oficiais e sessões solenes": 20
- "Assessoria parlamentar e comunicação com a imprensa": 20
- "Media training, discursos e pronunciamentos": 20

Já existem (não repita):
- Na preparação de uma cerimônia, a equipe define a ordem de abertura, homenagem e encerrame
- Ao orientar convidados para que silenciem celulares e aguardem sua vez de falar, a equipe 
- Uma comissão deseja organizar cerimônia acessível e sóbria, com assentos reservados e inté
- Num jantar institucional, a equipe recebeu restrições alimentares, indicação de traje e co
- Em uma solenidade, o mestre de cerimônias discorda da lista de autoridades validada pelo c
- No contexto institucional, etiqueta e precedência significam a mesma coisa, pois ambas def
- A indicação de traje no convite orienta a apresentação pessoal, mas, por si só, não altera
- A existência de um cerimonial organizado dispensa a verificação de acessibilidade, porque 
- O termo protocolo pode designar tanto regras de precedência em cerimônias quanto serviço d
- Conforme a legislação federal sobre símbolos nacionais, qual conjunto está completo e não 
- Uma equipe precisa conferir qual regra geral federal disciplina precedência em solenidades
- Em cerimônia oficial com bandeiras dispostas junto a uma tribuna, o servidor precisa deter
- Em solenidade com a execução do hino de um país convidado e do Hino Nacional Brasileiro, q
- Em uma homenagem municipal, o organizador copiou uma lista federal de autoridades de 1972 
- As cores verde e amarelo, consideradas nacionais pela legislação, são expressamente classi
- Na abertura de sessões cívicas, a Lei nº 5.700/1971 contempla a execução facultativa do Hi
- A Bandeira Nacional pode ser utilizada como pano de cobertura de uma placa a inaugurar, de
- Presidência do ato, precedência protocolar e condição de anfitrião são categorias distinta
- Uma unidade legislativa irá promover homenagem e deseja controlar providências de som, ace
- Ao escrever o texto que o mestre de cerimônias lerá para anunciar cada etapa, a equipe est
- Pouco antes de sessão solene, uma autoridade inicialmente confirmada comunica ausência. Qu
- Ao planejar a cobertura de uma sessão solene transmitida pela internet, qual verificação é
- Uma sessão de homenagem recebe grande público e convidado com mobilidade reduzida, mas o p
- O convite enviado para uma solenidade pode conter data, local, horário, finalidade e forma
- Uma sessão solene parlamentar e uma sessão deliberativa possuem necessariamente igual fina
- O pós-evento integra a gestão de cerimônias oficiais e pode reunir documentos, prestação d
- Diante de uma alteração inesperada no programa, o mestre de cerimônias pode ignorar a auto
- Qual instrumento é usado principalmente para reunir e examinar notícias e menções feitas p
- Durante a preparação de audiência de uma parlamentar com representantes de entidades, o as
- Um jornalista pede dado que o gabinete não conferiu. Qual atuação melhor atende ao dever d
- Qual distinção melhor expressa as atribuições de assessor de gabinete e titular do mandato
- Uma assessoria institucional deseja divulgar imagens e informações sobre reunião que inclu
- O release de imprensa é um comunicado informativo que, por si só, obriga os veículos de co
- O apoio parlamentar pode abranger organização de agenda, preparação de informações e inter
- A assessoria de imprensa de um órgão legislativo pode receber pedidos de veículos e articu
- Comunicação institucional e comunicação político-eleitoral são categorias sempre indistint
- Em treinamento para entrevista, o porta-voz simula perguntas difíceis, define mensagens e 
- Qual estrutura básica favorece a compreensão de um pronunciamento em solenidade de homenag
- Uma repórter pergunta por resultado estatístico cuja versão final ainda não foi divulgada 
- Ao revisar texto de quatro minutos destinado a solenidade, qual procedimento é o mais útil
- Em entrevista sobre fato controverso, o assessor sugere responder qualquer pergunta repeti
- A leitura em voz alta de um discurso ajuda a aferir ritmo, duração e clareza, sendo recome
- O treinamento para entrevistas públicas deve concentrar-se exclusivamente na postura diant
- Quando a equipe dispõe apenas de rumor não confirmado sobre uma solenidade, é legítimo ins
- As expressões on the record, off the record e background pertencem a convenções de relacio
```

## Pedido 6: História e Cultura de Pernambuco (`pernambuco.historia-cultura`, 20 tópicos de edital)

```
Matéria: pernambuco.historia-cultura (História e Cultura de Pernambuco)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Capitania, Olinda, Recife e açúcar": 20
- "2. Domínio holandês e Insurreição Pernambucana": 20
- "3. Palmares, Mascates e revoltas do século XIX": 20
- "4. Pernambuco republicano": 20
- "5. Cultura popular e herança afro-brasileira": 20

Já existem (não repita):
- No sistema de capitanias hereditárias, a capitania de Pernambuco, também chamada Nova Lusi
- Recife foi a primeira sede da capitania de Pernambuco, e Olinda surgiu depois como seu por
- O solo de massapê e o clima úmido da Zona da Mata favoreceram a lavoura de cana e fizeram 
- Sobre a economia colonial pernambucana, assinale a alternativa correta.
- A invasão holandesa de Pernambuco, em 1630, relaciona-se diretamente:
- Após a conquista holandesa, Olinda foi incendiada em 1631, e Recife passou a ser o centro 
- Entre as medidas do governo de Maurício de Nassau (1637–1644), NÃO se inclui:
- As Batalhas dos Guararapes, travadas em 1648 e 1649, foram decisivas para a vitória da Ins
- Henrique Dias e Felipe Camarão, líderes da Insurreição Pernambucana, simbolizam, respectiv
- Maurício de Nassau comandou a expulsão definitiva dos portugueses de Pernambuco em 1654.
- Sobre o Quilombo dos Palmares, assinale a alternativa correta.
- O Dia da Consciência Negra, 20 de novembro, lembra a data da morte de Zumbi dos Palmares, 
- A Guerra dos Mascates (1710–1711) opôs:
- O estopim da Guerra dos Mascates foi a elevação do Recife à condição de vila, com câmara p
- A Revolução Pernambucana de 1817 caracterizou-se por:
- A Confederação do Equador (1824) reagiu à outorga da Constituição de 1824 e ao centralismo
- A Cabanada (1832–1835), ocorrida no interior de Pernambuco e de Alagoas, foi um movimento 
- O Manifesto ao Mundo, associado à Revolução Praieira, defendia, entre outros pontos:
- Assinale a sequência cronológica correta dos movimentos pernambucanos.
- A Revolução Praieira é considerada a última grande revolta provincial do período imperial.
- O assassinato de João Pessoa, estopim da Revolução de 1930, ocorreu:
- Durante o Estado Novo (1937–1945), Pernambuco foi governado pelo interventor Agamenon Maga
- As Ligas Camponesas, que defendiam a reforma agrária, surgiram em 1955:
- A Sudene, criada em 1959 com sede no Recife, foi idealizada pelo economista Celso Furtado.
- Com o golpe de 1964, o governador de Pernambuco deposto e preso foi:
- O movimento Manguebeat, liderado por Chico Science nos anos 1990, renovou a cena cultural 
- Associe o pernambucano à sua contribuição. I. Joaquim Nabuco II. Gilberto Freyre III. Paul
- O frevo, nascido no Recife no fim do século XIX, foi reconhecido pela UNESCO como:
- A sombrinha colorida é um elemento característico do passista de frevo.
- Sobre os tipos de maracatu pernambucano, assinale a alternativa correta.
- O caboclo de lança, com sua gola bordada, é a figura central do maracatu nação, de baque v
- Associe a manifestação ou o elemento cultural ao lugar com que é mais identificado em Pern
- O São João de Caruaru é uma das maiores festas juninas do Brasil, embalada pelo forró.
- É doce típico da culinária pernambucana, reconhecido como patrimônio imaterial do estado, 
- O xangô pernambucano é uma religião de matriz africana, de tradição nagô, presente sobretu
- São elementos da herança afro-brasileira em Pernambuco, EXCETO:
- A ciranda, dança de roda de praia associada a Itamaracá, é uma das manifestações populares
- O governo de Maurício de Nassau no Brasil holandês estendeu-se de:
- O bandeirante responsável pela destruição do principal núcleo de Palmares, em 1694, foi:
- A Cabanada (1832–1835), revolta do interior de Pernambuco e Alagoas, tinha como principal 
- O artista que trabalhou o barro no Alto do Moura, em Caruaru, e se tornou referência da ar
```

## Pedido 7: Rotulagem, análise sensorial e tecnologia de alimentos (`nutricao.rotulagem-analise-sensorial-tecnologia`, 20 tópicos de edital)

```
Matéria: nutricao.rotulagem-analise-sensorial-tecnologia (Rotulagem, análise sensorial e tecnologia de alimentos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Rotulagem geral, tabela nutricional e rotulagem frontal": 17
- "2. Alegações nutricionais e alimentos para fins especiais": 17
- "3. Análise sensorial: planejamento, métodos e interpretação": 17
- "4. Bromatologia e composição química dos alimentos": 17
- "5. Tecnologia e conservação de alimentos; aditivos e coadjuvantes": 16
- "6. Especificação de compras, recebimento e armazenamento de gêneros": 16

Já existem (não repita):
- A rotulagem nutricional frontal brasileira destaca, quando cabível, teores elevados de açú
- Para decidir sobre a lupa, basta confrontar o sódio declarado por porção com o limite de 6
- Um alimento sólido com 15 g de açúcares adicionados por 100 g alcança o limiar numérico pa
- Em um alimento líquido com 2,9 g de gorduras saturadas por 100 ml, ultrapassou-se o limiar
- Um produto rotulado como sem lactose pode conter proteínas do leite capazes de provocar re
- Uma alegação de conteúdo reduzido de sódio é necessariamente sinônima de baixo teor absolu
- Todas as fórmulas infantis, fórmulas para nutrição enteral e suplementos alimentares receb
- No teste triangular, o avaliador recebe três amostras, duas iguais e uma diferente, e busc
- Uma diferença sensorial significativa em teste triangular comprova, por si só, que a nova 
- O balanceamento da ordem de apresentação e a codificação das amostras ajudam a reduzir efe
- A análise de cinzas é suficiente para informar isoladamente os teores exatos de ferro e cá
- Atividade de água e umidade são grandezas diferentes: um alimento pode conservar bastante 
- O teor de proteína calculado a partir de nitrogênio por Kjeldahl é obrigatoriamente igual 
- A pasteurização e o congelamento são processos equivalentes à esterilização e garantem eli
- A autorização sanitária de um aditivo em uma categoria de alimento não autoriza sua utiliz
- Um antioxidante alimentar deve necessariamente apresentar também a função tecnológica de c
- O prazo de validade impresso dispensa o controle de temperatura, desde que o odor do alime
- Pelo método PVPS, o lote com data de vencimento mais próxima pode sair antes de outro lote
- A aceitação de uma embalagem estufada é tecnicamente justificada sempre que o fornecedor a
- Alimentos com alegação de ausência de adição de açúcares podem apresentar açúcares totais 
- Uma barra sólida contém 620 mg de sódio por 100 g. Considerado apenas esse teor e o campo 
- Uma bebida contém 7,0 g de açúcares adicionados e 310 mg de sódio por 100 ml. Qual seria o
- Uma embalagem informa 90 mg de sódio a cada porção de 15 g. Sem arredondamentos, quanto eq
- O símbolo frontal em formato de lupa é utilizado para advertir sobre teores elevados de qu
- Uma empresa afirma que um sólido com 14 g de açúcares adicionados por 100 g atingiu o crit
- Ao encontrar “reduzido em sódio” no rótulo, que informação é central para verificar a natu
- Uma consumidora tem alergia às proteínas do leite e avalia um iogurte “zero lactose”. Qual
- Uma equipe classifica suplemento alimentar como se todas as suas regras fossem as mesmas d
- Qual informação nutricional não pode ser inferida da expressão isolada “sem adição de açúc
- Um alimento exibe a lupa “alto em sódio” e pretende usar, para o mesmo nutriente, uma aleg
- Uma indústria deseja saber se a substituição de um aroma gerou diferença perceptível, não 
- Em uma prova, a banca pergunta o que a escala hedônica usualmente mensura em avaliação sen
- Em teste triangular, sem qualquer pista externa, qual é a probabilidade de identificar a a
- Em estudo sensorial, a alternância equilibrada da sequência A-B e B-A entre provadores red
- Um painel calibrado deve identificar atributos e graduar intensidade de adstringência e ar
- Um laboratório precisa estimar o resíduo mineral global depois de procedimento padronizado
- Dois alimentos apresentam a mesma umidade percentual, mas diferentes condições de crescime
- O método Kjeldahl, aplicado a um alimento com nitrogênio não proteico, fornece resultado q
- O valor de °Brix medido em suco de frutas pode ser corretamente interpretado como:
- Um alimento de 100 g tem 70 g de água e 9 g de proteína. Qual o percentual aproximado de p
- Qual processo reduz a temperatura do alimento para retardar alterações, mas não garante es
- A empresa pretende usar o mesmo conservador em novas categorias de produtos. Que verificaç
- Maçãs cortadas escurecem na presença de oxigênio devido à ação de enzimas oxidativas. Qual
- Qual distinção descreve adequadamente as funções tecnológicas de aditivo antioxidante e co
- Uma empresa usa embalagem em atmosfera modificada e alega que não precisa mais de monitora
- Dois lotes idênticos estão aptos ao uso: um chegou antes mas vence depois. Qual regra faz 
- No recebimento, um lote de produto refrigerado chega com embalagem íntegra, mas fora da te
- Qual especificação de compra favorece comparação objetiva de ofertas e controle do gênero 
- Sobre o prazo de validade de alimento embalado, qual afirmação expressa o princípio técnic
- Ao suspeitar que um lote de matéria-prima esteja comprometido, qual procedimento ajuda a i
```

## Pedido 8: Planejamento educacional e gestão de sistemas de ensino (`educacao.planejamento-educacional-gestao-sistemas`, 20 tópicos de edital)

```
Matéria: educacao.planejamento-educacional-gestao-sistemas (Planejamento educacional e gestão de sistemas de ensino)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento educacional: níveis, ciclos e instrumentos": 20
- "2. Federação, Sistema Nacional de Educação e conselhos": 20
- "3. Financiamento educacional e cooperação na alocação de recursos": 20
- "4. Avaliação em larga escala, Censo e interpretação de indicadores": 20
- "5. Gestão democrática do sistema e monitoramento do desenvolvimento escolar": 20

Já existem (não repita):
- No planejamento educacional, a publicação de um plano encerra o ciclo de gestão, cabendo à
- O Plano de Ações Articuladas funciona como instrumento de planejamento e gestão de demanda
- Uma meta educacional operacionalmente verificável deve explicitar resultado esperado, praz
- O plano municipal de educação pode ignorar os referenciais de cooperação previstos no Sist
- A Lei Complementar nº 220/2025 instituiu o Sistema Nacional de Educação sem extinguir, por
- A Comissão Intergestores Bipartite da Educação é instância nacional integrada exclusivamen
- A existência de instâncias de pactuação no SNE significa que todas as políticas municipais
- A interoperabilidade dos dados educacionais promovida pelo SNE deve observar proteção de d
- A Constituição estabelece para a União a vinculação mínima de 25% da receita resultante de
- A complementação VAAT considera receitas e disponibilidades vinculadas à educação além da 
- Para a distribuição da complementação VAAR, a obtenção do maior Ideb da federação é condiç
- O CACS-Fundeb acompanha o emprego dos recursos do fundo, mas não substitui a administração
- O Índice de Desenvolvimento da Educação Básica é composto exclusivamente pela proficiência
- O Censo Escolar coleta dados anuais sobre escolas e matrículas, e suas informações de rend
- Uma matriz de referência do Saeb corresponde à totalidade dos conteúdos e experiências cur
- Uma elevação na média de proficiência pode coexistir com crescimento das desigualdades ent
- O monitoramento de uma política educacional limita-se à análise da execução financeira e d
- A construção de indicadores de desenvolvimento escolar deve especificar fontes, fórmulas, 
- A constatação de melhora no Ideb depois de um programa novo demonstra, por si só, que o pr
- A divulgação pública de indicadores educacionais agregados pode coexistir com regras de pr
- Uma secretaria vai converter um diagnóstico de abandono em planejamento executável. Qual c
- Uma equipe afirma que o PAR substitui o plano municipal decenal e a LOA. Assinale a correç
- Quanto às escalas de planejamento educacional no federalismo brasileiro, assinale a propos
- Uma prefeitura incluiu em seu plano a meta de ampliar o atendimento infantil, mas não prev
- Em avaliação de uma política de transporte, a secretaria informou que 20 ônibus foram entr
- A Lei Complementar nº 220/2025 caracteriza o SNE principalmente como:
- Ao organizar um acordo sobre vagas no ensino fundamental e transições de estudantes entre 
- Uma notícia afirma que decisões de toda política municipal de educação se tornaram obrigat
- Qual associação entre órgão e função corresponde à arquitetura de governança educacional?
- Na implantação de banco interoperável de dados escolares, qual providência respeita melhor
- Qual par apresenta corretamente os mínimos constitucionais de aplicação em manutenção e de
- Para comparar a complementação federal ao Fundeb, considere as siglas. Qual descrição está
- Uma rede deseja conhecer se receberá complementação VAAR. Qual análise é juridicamente mai
- O orçamento educacional municipal mostra crescimento nominal de despesas, mas há dúvidas s
- Sobre o controle social dos recursos do Fundeb, qual afirmação é adequada?
- Duas escolas têm o mesmo Ideb, mas diferentes taxas de aprovação. O que o analista deve fa
- Um relatório apresenta o Censo Escolar como prova nacional aplicada aos alunos para medir 
- Uma rede melhorou a aprovação escolar, mas teve queda expressiva na proficiência. Qual int
- Quando uma rede compara resultados Saeb de anos diferentes, qual cautela metodológica é es
- Para identificar defasagem entre idade e etapa escolar, qual indicador do Inep é o mais es
- Uma secretaria quer acompanhar o desenvolvimento escolar sem reduzir a análise a notas de 
- Na etapa de monitoramento de programa de busca ativa, o indicador “número de visitas às fa
- Após a implantação de tutoria, uma rede identificou melhora na proficiência média. Para in
- Em uma consulta pública sobre expansão de vagas, qual procedimento mais fortalece a gestão
- Uma rede pretende publicar dados para justificar prioridade a territórios com maior abando
```

## Pedido 9: Manutenção predial: pintura, alvenaria, hidráulica e elétrica básicas (`manutencao.oficios-manutencao-predial`, 20 tópicos de edital)

```
Matéria: manutencao.oficios-manutencao-predial (Manutenção predial: pintura, alvenaria, hidráulica e elétrica básicas)
Capítulos e quantas questões quero em cada um (total 100):
- "Pintura predial: tintas, preparação e patologias": 20
- "Alvenaria, argamassas, revestimentos e acabamentos": 20
- "Hidráulica predial: redes, peças e manutenção": 20
- "Elétrica predial básica e segurança pela NR-10": 20
- "Ferramentas, NR-35, prevenção e cálculo de materiais": 20

Já existem (não repita):
- Em revestimento tradicional, qual assertiva distingue corretamente chapisco e emboço?
- Em sistema tradicional, o reboco é a camada cuja função principal consiste em assentar blo
- No revestimento argamassado tradicional, qual camada desempenha predominantemente a regula
- No revestimento tradicional, o chapisco é aplicado para melhorar as condições de aderência
- Qual instrumento simples auxilia a conferir a verticalidade de uma alvenaria?
- O condutor de proteção elétrica pode ser substituído, sem avaliação, pelo neutro da instal
- O disjuntor é um dispositivo normalmente empregado para proteção contra sobrecorrentes, co
- Qual dispositivo atua tipicamente contra sobrecarga e curto-circuito, observadas suas cara
- Em orçamento de reparos, qual unidade corresponde ao comprimento de um trecho de tubulação
- Uma tarefa apresenta desnível de 3 m e risco de queda. Qual norma regulamentadora trata es
- A função típica do sifão de um lavatório é manter fecho hídrico que dificulta a passagem d
- Em instalações sanitárias, uma caixa sifonada pode apresentar odor após secar completament
- Um lavatório está com odor vindo do esgoto após longo período sem uso. Qual componente mer
- Vazão e pressão são expressões equivalentes, ambas medidas exclusivamente em litros por mi
- A massa corrida PVA é, por definição, um impermeabilizante capaz de corrigir vazamento de 
- Ao encontrar bolhas e infiltração ativa em uma parede pintada, qual é a prioridade técnica
- Ao preparar parede interna seca com pequenas imperfeições superficiais antes de pintar, qu
- Em uma base recém-reparada, qual fator deve ser respeitado antes da tinta?
- Na manutenção de parede com manchas de umidade, é adequado identificar e corrigir a origem
- Em um piso cerâmico, foram previstas juntas de movimentação. Qual conduta preserva sua fin
- No assentamento cerâmico, o rejunte substitui integralmente a necessidade de juntas destin
- Uma argamassa que já perdeu suas condições de aplicação não deve ser automaticamente recup
- A NR-10 vigente em outubro de 2026 é necessariamente a redação publicada em maio de 2026, 
- Em uma instalação predial, qual associação entre dispositivos e funções está correta?
- Na NR-35, para a realização de trabalho em altura abrangido pela norma, qual requisito tem
- Para serviços em altura abrangidos pela NR-35, o mero fornecimento de cinturão não dispens
- Uma equipe planeja pintura em beiral a 4 m do piso, com risco de queda. Qual providência e
- A presença de cheiro de esgoto em ralo há muito tempo sem uso pode decorrer da perda do fe
- Ao substituir um trecho de PVC soldável de água fria, qual prática corresponde ao controle
- Em conexão de PVC soldável, o tempo de cura e a compatibilidade do adesivo independem do f
- Qual prática reduz o risco de comprometer juntas soldáveis de PVC durante reparo?
- Uma conexão rosqueada de água apresenta pequeno vazamento. Qual critério deve orientar a e
- Ao retirar tinta descascada de uma fachada, o reparador encontra revestimento que se solta
- Manchas esbranquiçadas de sais em revestimento podem indicar eflorescência ligada à circul
- Qual termo descreve o depósito esbranquiçado de sais que pode aparecer em paredes úmidas?
- Selador e fundo preparador sempre possuem composição e função idênticas, podendo ser troca
- Uma parede apresenta bolor após meses de condensação. Qual tratamento é mais coerente com 
- A manutenção constata placa cerâmica solta e som oco em várias outras placas. Qual encamin
- Em um reparo de contrapiso com previsão de escoamento para ralo, conferir o caimento faz p
- A desenergização para intervenção elétrica, quando exigida, é comprovada exclusivamente po
- A proteção por DR elimina a necessidade de dispositivo de proteção contra sobrecorrente em
- Em circuito resistivo ideal, uma carga de 2.200 W alimentada a 220 V apresenta corrente de
- Pela relação simplificada P = U × I, qual corrente é esperada em carga puramente resistiva
- Um servidor conhece instalações elétricas, mas não tem autorização para intervir em quadro
- Uma equipe identifica água escorrendo perto de tomada e circuito aparentemente ligado. Qua
- Uma instalação deve obedecer à NR-10 em outubro de 2026. Como tratar a nova redação public
- Após diagnóstico, serão instaladas placas de piso em 15 m². Foi estimada reserva hipotétic
- Para 24 m² de revestimento com 2,5 cm de espessura, o volume geométrico teórico é de 0,60 
- Para uma fachada, o fabricante declara rendimento acabado de 18 m² por lata para todas as 
- Um encarregado avalia trabalho em cobertura a 2,8 m, com risco de queda, próximo a instala
- Um trecho de revestimento terá 18 m² de área e espessura uniforme de 20 mm. Qual o volume 
- Uma sala possui 40 m² líquidos de pintura e requer três demãos. O rótulo informa 10 m²/L p
- Uma tarefa com desnível de 1,5 m e possibilidade de queda dispensa qualquer prevenção, poi
- Em paredes com mancha abaixo de banheiro, a localização aparente do defeito permite conclu
- Depois de aplicar tinta sobre reboco cuja coesão está comprometida e que se solta em placa
```

## Pedido 10: Processo de enfermagem e sistematização da assistência (SAE) (`enfermagem.processo-enfermagem-sae`, 19 tópicos de edital)

```
Matéria: enfermagem.processo-enfermagem-sae (Processo de enfermagem e sistematização da assistência (SAE))
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, SAE e organização do Processo de Enfermagem": 20
- "Avaliação clínica, raciocínio diagnóstico e linguagens padronizadas": 20
- "Planejamento de resultados, intervenções e implementação": 20
- "Evolução, documentação e comunicação segura do cuidado": 20
- "Teorias de Horta, Orem, King e Roy: aplicações integradas": 20

Já existem (não repita):
- Na sistematização do cuidado, a SAE organiza condições de trabalho, enquanto o Processo de
- Pela Resolução Cofen nº 736/2024, a primeira etapa do Processo de Enfermagem é formalmente
- As etapas do Processo de Enfermagem são recorrentes e podem ser revisitadas diante de muda
- O diagnóstico de enfermagem e a prescrição de enfermagem, no âmbito do PE, são atribuições
- A realização do Processo de Enfermagem está restrita a ambientes de internação hospitalar,
- O relato do paciente sobre a intensidade de dor é dado subjetivo, ao passo que uma frequên
- A presença de fatores de risco, com pele íntegra, comprova a existência de lesão de pele e
- Um diagnóstico de enfermagem descreve o julgamento clínico sobre respostas humanas e pode 
- A NANDA-I tem como função principal padronizar intervenções e atividades de enfermagem, en
- A CIPE/ICNP pode representar diagnósticos, intervenções e resultados de enfermagem e atual
- Uma meta de cuidado expressa apenas por “melhorar o bem-estar” é tão verificável quanto um
- NOC e NIC têm funções distintas: a primeira oferece resultados sensíveis à enfermagem; a s
- Checar como executado um cuidado previsto, mas não realizado, é aceitável se o profissiona
- É possível selecionar intervenções diferentes para o mesmo diagnóstico de enfermagem, de a
- De acordo com a Resolução nº 736/2024, o planejamento inclui priorizar diagnósticos, deter
- Uma anotação que informa execução de cuidado tem a mesma finalidade da etapa Evolução, dis
- Compartilhar usuário e senha de prontuário eletrônico é prática adequada quando permite qu
- A Resolução Cofen nº 429/2012 permanece vigente e é a única referência atual sobre registr
- Na teoria de Orem, o sistema apoio-educação é adequado quando a pessoa pode realizar ações
- Roy descreve adaptação exclusivamente fisiológica, e por isso aspectos de autoconceito, fu
- Um enfermeiro da atenção básica vai organizar a consulta pela sequência atualmente previst
- Durante o treinamento da equipe, um preceptor distingue a SAE do PE. Qual explicação está 
- Um técnico de enfermagem participa da rotina assistencial com observações, cuidados prescr
- Uma pessoa atendida em domicílio precisa de acompanhamento por enfermagem. Em relação ao P
- A equipe recebe um paciente com nova dificuldade respiratória durante um cuidado em andame
- Na avaliação inicial de um paciente com suspeita de dificuldade na deambulação, qual dado 
- Paciente imobilizado apresenta pele íntegra, umidade frequente e necessidade de ajuda para
- Em uma ficha clínica aparecem “pneumonia” e “medo de permanecer sozinho durante a internaç
- Uma prova apresenta as siglas NANDA-I, NIC e NOC e pede a associação correta. Qual alterna
- Um pesquisador consulta o navegador antigo da CIPE e presume que ele contém a atualização 
- No planejamento para uma pessoa que refere dor 7/10, qual resultado esperado é mais adequa
- Uma enfermeira quer usar indicadores graduados para acompanhar evolução da autonomia do pa
- Ao elaborar a prescrição de enfermagem, qual proposta é a mais adequada?
- O prontuário mostra cuidado prescrito, mas uma intercorrência impediu sua execução. O que 
- Qual exemplo demonstra articulação adequada entre diagnóstico, resultado e intervenção, se
- Após intervenção voltada a reduzir dor, qual registro apresenta conteúdo próprio da etapa 
- Em uma anotação de enfermagem, qual frase registra informações observáveis e evita juízo m
- A instituição adota prontuário eletrônico e pede a cada profissional que utilize acesso de
- Uma candidata menciona duas normas: Resolução Cofen nº 429/2012 e nº 754/2024. Qual afirma
- Em consulta de enfermagem, uma unidade organiza registros no formato SOAP. Qual interpreta
- Em uma avaliação integral baseada em Wanda Horta, qual conjunto reúne adequadamente dimens
- Uma pessoa plenamente capaz de executar cuidados cotidianos precisa aprender a monitorar s
- Durante o acompanhamento, uma paciente participa com a enfermeira da pactuação de uma meta
- Após uma amputação, o enfermeiro avalia função física, imagem corporal, papéis familiares 
- Uma prova pergunta o significado dos metaparadigmas de enfermagem. Qual conjunto represent
```

## Pedido 11: Inquérito Policial Militar (`processo-penal-militar.inquerito-policial-militar`, 19 tópicos de edital)

```
Matéria: processo-penal-militar.inquerito-policial-militar (Inquérito Policial Militar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Polícia judiciária militar, natureza e instauração do IPM": 16
- "2. Encarregado, escrivão, diligências, sigilo e defesa": 16
- "3. Prazos do IPM, duração de diligências e restrições pessoais": 37
- "4. Relatório, solução, remessa, arquivamento e dispensa": 16
- "5. Prisão em flagrante militar, auto e controle dos atos": 15

Já existem (não repita):
- Uma autoridade reúne indícios de possível crime militar e pretende formalizar a investigaç
- A investigação de crime militar pretende esclarecer o fato e sua autoria, fornecendo subsí
- Durante sindicância instaurada em unidade militar surgem indícios concretos de infração pe
- Uma autoridade militar recebe notícia de crime recente e encontra vestígios que podem desa
- Em IPM instaurado contra oficial, a autoridade vai escolher escrivão para o procedimento, 
- Para designar encarregado de IPM, a autoridade dispõe de oficial com posto de capitão. Seg
- Durante o IPM, o encarregado precisa identificar objeto danificado, ouvir testemunhas e de
- A portaria instaura IPM com indiciado solto; não há outra causa legal de alteração do praz
- No IPM, o indiciado está preso e a ordem de prisão foi executada em data conhecida. Que pr
- Um IPM com indiciado solto aproxima-se do quadragésimo dia, mas exame pericial já iniciado
- Em investigação ordinária, o encarregado agenda oitiva de testemunha para 15h, prevendo co
- A defesa de indiciado pede cópia de depoimentos já documentados no IPM. O encarregado nega
- O encarregado encerra o IPM com descrição das oitivas, perícias, resultados e conclusão so
- Concluído o IPM, a autoridade militar considera não haver infração penal e quer mandar arq
- Em serviço, militar captura pessoa encontrada logo depois do fato com instrumentos que ind
- Uma pessoa é presa em flagrante por infração militar e a autoridade prepara a nota de culp
- Auto de prisão em flagrante já reúne dados suficientes sobre o fato e sua autoria. O event
- Uma autoridade deve delegar IPM contra oficial da ativa e não encontra, na estrutura dispo
- Durante o IPM, o encarregado encontra indícios contra oficial mais antigo que ele, exigind
- Depois de arquivado IPM por insuficiência de elementos, surge prova nova relacionada ao me
- Auto de flagrante por fato imputado a oficial será lavrado por autoridade militar. Na desi
- A autoridade militar lavra APF e constata necessidade de diligência do art. 246. Como inte
- Um APF descreve de modo completo fato e autoria. A coisa apreendida necessita de avaliação
- O Ministério Público considera imprescindível uma diligência para decidir sobre denúncia e
- Uma prisão em flagrante não contou com testemunha ocular da infração, mas duas pessoas pre
- A finalidade principal do IPM, conforme art. 9º do CPPM, é levantar sumariamente fatos que
- A portaria de IPM só pode resultar de iniciativa de ofício da autoridade militar, pois o C
- O art. 12 permite providências preliminares de preservação do local e apreensão de objetos
- O prazo básico do art. 20 para terminar IPM com indiciado preso é de quarenta dias, contad
- O art. 22 exige relatório final do encarregado, com diligências, pessoas ouvidas, resultad
- Em flagrante militar, somente militares podem efetuar a prisão; o art. 243 impede que part
- Quando o IPM é conduzido com indiciado solto, o art. 20 fixa quarenta dias desde a instaur
- A preferência do art. 15 por encarregado com posto de capitão ou capitão-tenente torna jur
- Nos termos do art. 19, uma testemunha não deve ser inquirida por mais de quatro horas cons
- Por força do art. 16 do CPPM, o advogado não pode acessar elemento probatório já documenta
- O art. 24 proíbe que a autoridade militar determine arquivamento do IPM mesmo quando o rel
- A ausência de testemunhas presenciais do fato impede lavrar auto de prisão em flagrante mi
- O art. 247 determina que a nota de culpa entregue ao preso em flagrante apresente o motivo
- A prorrogação ordinária de vinte dias prevista no art. 20, § 1º, pode ser concedida indist
- O art. 16-A, introduzido pela Lei nº 13.964/2019, prevê em hipóteses específicas de invest
- O § 4º do art. 245 exige que escrivão do auto de flagrante de oficial seja apenas primeiro
- O art. 26 permite devolução dos autos por requisição do Ministério Público para diligência
- Uma vez que o auto de prisão em flagrante esclareça fato e autoria, o art. 27 dispensa ide
- Se houver novas provas relativas a fato que motivou arquivamento anterior, o art. 25 admit
- A previsão do art. 251 de até cinco dias para remeter APF dependente de diligência autoriz
```

## Pedido 12: Hematologia laboratorial (`farmacia.analises-clinicas-hematologia-lab`, 19 tópicos de edital)

```
Matéria: farmacia.analises-clinicas-hematologia-lab (Hematologia laboratorial)
Capítulos e quantas questões quero em cada um (total 100):
- "Fase pré-analítica e fundamentos do hemograma": 20
- "Eritrograma e morfologia das hemácias": 20
- "Leucograma, plaquetas e contagem automatizada": 20
- "Coagulograma e investigação laboratorial da hemostasia": 20
- "VHS, controle de qualidade e validação dos resultados": 20

Já existem (não repita):
- Um laboratório prepara tubos para hemograma de rotina. Qual anticoagulante é o mais indica
- No eritrograma, qual parâmetro descreve diretamente o volume médio das hemácias?
- Ao ler um leucograma com diferencial, a forma mais segura de identificar aumento real de n
- Qual ensaio de triagem investiga classicamente a via extrínseca e a via comum da coagulaçã
- A velocidade de hemossedimentação é usualmente expressa em qual unidade?
- Na avaliação da lâmina, o termo anisocitose designa:
- O que diferencia uma avaliação externa da qualidade de um controle interno?
- Um paciente tem hematócrito de 42% e 4,2 milhões de hemácias/µL. Qual VCM calculado corres
- Um contador registra leucócitos de 14.000/µL e 70% de neutrófilos. Qual é a contagem absol
- Uma amostra citratada para TTPa chega muito abaixo da marca de enchimento. Qual é a conseq
- Em uma contagem automatizada, plaquetas aparentemente muito baixas acompanham flag de agre
- Uma amostra apresenta TP normal e TTPa prolongado. Qual afirmação descreve melhor o racioc
- Uma criança em faixa etária de predominância fisiológica linfocitária tem linfócitos perce
- Em paciente com hematócrito muito elevado, superior a 55%, solicita-se coagulograma. O lab
- Durante a verificação do hemograma, observam-se RBC muito reduzidas, VCM surpreendentement
- Um sangue periférico automatizado exibe elevada contagem aparente de plaquetas em presença
- Um teste de mistura 1:1 para TTPa prolongado não demonstra correção imediata. Qual interpr
- Um paciente apresenta sangramento e tempos de TP e TTPa sem alteração. Qual afirmação pres
- Dois métodos de VHS apresentam resultados discrepantes no mesmo conjunto de amostras. Cons
- Um lote novo de reagente gera deslocamento persistente das médias de controle, embora as a
- Ao identificar eritroblastos em uma amostra cujo contador não discrimina essas células, qu
- O hemograma completo reúne informações sobre hemácias, leucócitos e plaquetas, embora não 
- O termo poiquilocitose identifica variação de tamanhos das hemácias, enquanto anisocitose 
- A contagem absoluta de uma população leucocitária pode ser calculada multiplicando os leuc
- O TTPa é considerado o principal ensaio de triagem da via extrínseca isolada, com especial
- Um resultado de VHS elevada é um achado inespecífico e não permite, sozinho, diagnosticar 
- O EDTA é o anticoagulante de escolha universal para os testes plasmáticos de TP e TTPa, se
- O microscópio pode complementar o analisador hematológico quando há alterações quantitativ
- O aumento do RDW comprova deficiência de ferro, mesmo quando não há outros resultados hema
- Uma amostra com 10.000 leucócitos/µL e 10% de eosinófilos possui 1.000 eosinófilos/µL, con
- Em uma amostra com agregados plaquetários dependentes de EDTA, a baixa contagem automatiza
- Um tubo de citrato subpreenchido tende, por definição, a reduzir o tempo de coagulação de 
- O INR foi concebido especialmente para melhorar a padronização do TP no acompanhamento de 
- Na infância, pode haver fases com predomínio relativo de linfócitos, razão pela qual a lei
- Controle interno da qualidade e ensaio de proficiência externo são nomes diferentes para a
- A combinação de RBC aparentemente baixa, VCM alto e CHCM muito elevada pode sugerir agluti
- O desvio à esquerda exige necessariamente contagem leucocitária total acima do intervalo d
- O tempo de trombina pode sofrer interferência por heparina e por alterações quantitativas 
- Quando TP e TTPa estão normais, ficam descartadas alterações funcionais de plaquetas e def
- Um teste de mistura que não corrige TTPa prolongado pode sugerir inibidor, mas a conclusão
- Métodos alternativos automatizados de VHS podem ser liberados como totalmente intercambiáv
- Uma tendência persistente de controles após troca de lote pode indicar viés analítico mesm
```

## Pedido 13: Regime Jurídico Único dos Servidores do Rio Grande do Norte (LC 122/1994) (`rio-grande-do-norte.estatuto-servidores-lc-122`, 19 tópicos de edital)

```
Matéria: rio-grande-do-norte.estatuto-servidores-lc-122 (Regime Jurídico Único dos Servidores do Rio Grande do Norte (LC 122/1994))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, provimento, posse, exercício e estabilidade": 25
- "2. Vacância, deslocamentos, remuneração e vantagens": 25
- "3. Férias, licenças, afastamentos, tempo de serviço e petição": 25
- "4. Regime disciplinar e processo administrativo disciplinar": 25

Já existem (não repita):
- Para a LC 122/1994, servidor é a pessoa legalmente investida em cargo público.
- Segundo o art. 5º da LC 122/1994, NÃO é forma de provimento de cargo público:
- O concurso público tem validade de até dois anos, prorrogável uma única vez por igual perí
- Quanto às pessoas com deficiência, a LC 122/1994 determina que, em cada concurso, sejam re
- O prazo para a posse é de 30 dias, contados da publicação do ato de provimento, e pode ser
- De acordo com o art. 13, § 1º, da LC 122/1994, a posse NÃO é exigida no provimento por:
- Decorrido o prazo legal sem que o nomeado tome posse, ele é exonerado de ofício.
- Pela LC 122/1994, o servidor empossado deve entrar em exercício no prazo de:
- O servidor investido por eleição dispõe de 30 dias, contados da posse, para entrar em exer
- No estágio probatório previsto na LC 122/1994, a avaliação de desempenho do servidor é ins
- O art. 21 da LC 122/1994 prevê estabilidade após dois anos de efetivo exercício, mas, desd
- Entre os fatores de avaliação do estágio probatório listados no art. 20 da LC 122/1994, NÃ
- Segundo a LC 122/1994, a recondução do servidor estável ao cargo anteriormente ocupado dec
- Não pode reverter à atividade o aposentado que já tiver completado 70 anos de idade.
- A investidura do servidor efetivo em outro cargo, de atribuições compatíveis com a limitaç
- Na reintegração, se o cargo estiver provido, o seu ocupante é reconduzido ao cargo de orig
- Segundo o art. 33 da LC 122/1994, NÃO gera vacância de cargo público:
- A exoneração de ofício de cargo efetivo ocorre quando não satisfeitas as condições do está
- A remoção a pedido para outra localidade, independentemente de vaga, é admitida pela LC 12
- O titular de um cargo em comissão afastou-se por 45 dias consecutivos por motivo que não é
- Na substituição por motivo de férias do titular, em período integral, o substituto só rece
- O servidor perde a parcela de remuneração diária proporcional aos atrasos, ausências ou sa
- As indenizações pagas ao servidor, como ajuda de custo e diárias, não se incorporam ao ven
- As reposições e indenizações ao erário são descontadas da remuneração do servidor em parce
- A ajuda de custo, calculada sobre a remuneração do servidor, não pode exceder a importânci
- O adicional por tempo de serviço previsto no art. 75 da LC 122/1994 é devido à razão de:
- O servidor que faz jus, ao mesmo tempo, aos adicionais de insalubridade e de periculosidad
- Para a LC 122/1994, o serviço noturno é o prestado:
- O serviço extraordinário é remunerado com acréscimo, sobre a hora normal de trabalho, de:
- O servidor faz jus a 30 dias consecutivos de férias anuais, que podem ser acumuladas até o
- O servidor que opera direta e permanentemente com Raios X ou substâncias radioativas goza 
- A licença concedida dentro de 60 dias do término de outra da mesma espécie é considerada p
- Em caso de aborto atestado por médico oficial, a servidora tem direito, pela LC 122/1994, 
- A servidora que adotar criança de até um ano de idade tem 90 dias de licença; se a criança
- A licença por motivo de doença em pessoa da família, segundo o art. 98 da LC 122/1994, é c
- Após cada quinquênio ininterrupto de exercício, o servidor faz jus a três meses de licença
- Segundo o art. 103 da LC 122/1994, NÃO impede a concessão de licença-prêmio o fato de o se
- As faltas injustificadas ao serviço retardam a concessão da licença-prêmio na proporção de
- Pelo art. 111 da LC 122/1994, o servidor pode ausentar-se do serviço, sem qualquer prejuíz
- Quando o servidor é cedido para exercer cargo em comissão em outro Poder ou órgão, o ônus 
- O servidor efetivo eleito vereador, havendo compatibilidade de horário:
- O prazo para o servidor interpor pedido de reconsideração ou recurso, contado da publicaçã
- O silêncio da autoridade, no prazo que tem para decidir o requerimento ou o pedido de reco
- Pelo art. 124 da LC 122/1994, prescreve em 120 dias o direito de requerer:
- É dever do servidor cumprir as ordens superiores, exceto quando manifestamente ilegais.
- É proibido ao servidor manter sob sua chefia imediata, em cargo ou função de confiança, cô
- É vedado ao servidor ser acionista ou cotista de sociedade privada.
- NÃO consta do rol de penalidades disciplinares do art. 138 da LC 122/1994:
- Pela LC 122/1994, a penalidade de suspensão não pode exceder:
- Havendo conveniência para o serviço, a suspensão pode ser convertida em multa de 50% por d
- Os registros das penalidades de advertência e de suspensão são cancelados, se o servidor n
- Configura abandono de cargo a ausência intencional do servidor ao serviço por mais de 30 d
- Para a LC 122/1994, inassiduidade habitual é a falta ao serviço, sem causa justificada, po
- O servidor demitido por improbidade administrativa, nos termos da LC 122/1994:
- A ação disciplinar prescreve em 5 anos para infrações puníveis com demissão, em 2 anos par
- A pena de demissão de um servidor do quadro do Tribunal de Justiça do Rio Grande do Norte 
- A sindicância deve ser concluída em até 30 dias, prorrogáveis por igual período, e pode se
- O processo disciplinar previsto na LC 122/1994 é conduzido por comissão composta de:
- O afastamento preventivo do servidor, determinado pela autoridade instauradora, dura até 6
- No processo disciplinar, o indiciado que estiver em lugar incerto e não sabido é citado po
- Recebido o processo disciplinar com o relatório da comissão, a autoridade julgadora deve p
- Da revisão do processo disciplinar pode resultar agravamento da penalidade, se surgirem pr
- O julgamento do processo disciplinar fora do prazo legal não implica nulidade do processo.
```

## Pedido 14: Controladoria e controle interno (`contabilidade.controladoria-controle-interno`, 18 tópicos de edital)

```
Matéria: contabilidade.controladoria-controle-interno (Controladoria e controle interno)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da controladoria e teoria dos sistemas": 17
- "Estrutura COSO e gestão de riscos": 17
- "Tipos, técnicas e informatização dos controles": 17
- "Controle interno na gestão pública brasileira": 17
- "Auditoria interna e governança: interfaces essenciais": 16
- "Indicadores, relatórios gerenciais e orçamento empresarial": 16

Já existem (não repita):
- A controladoria pode utilizar dados financeiros e operacionais para apoiar decisões sem as
- Uma organização tratada como sistema aberto independe de informações do ambiente e dispens
- O modelo COSO de controle interno possui cinco componentes integrados, entre eles ambiente
- Controles internos bem planejados asseguram, sem exceção, que fraudes e erros nunca ocorre
- Conciliação bancária que revela diferença entre registros da empresa e extrato é exemplo t
- Pela lógica constitucional brasileira, os três Poderes devem manter, de forma integrada, s
- O cumprimento de uma meta de atendimentos comprova, isoladamente, que houve melhoria socia
- A análise de desempenho deve considerar efeitos de incentivos e metas sobre outros process
- No COSO, o risco residual representa a exposição avaliada antes da adoção de qualquer cont
- A revisão periódica de permissões de usuários pode integrar controles gerais de tecnologia
- A segregação de funções exige sempre a contratação de quatro empregados distintos, não adm
- A IN Conjunta MP/CGU nº 1/2016 trata dos controles internos da gestão no âmbito dos órgãos
- O modelo das três linhas coloca os gestores operacionais na primeira linha e a auditoria i
- Orçamento flexível mantém os valores originais inalterados mesmo quando o volume efetivo d
- Na ótica sistêmica, duas organizações abertas podem alcançar resultados semelhantes median
- No COSO ICIF 2013, os componentes de avaliação de riscos e monitoramento concentram, respe
- A existência de dupla aprovação descrita em um procedimento comprova automaticamente eficá
- Transferir parte do impacto financeiro de um risco para seguradora elimina necessariamente
- Uma norma destinada ao Executivo federal basta, por si só, para fixar atribuições específi
- Se a auditoria interna opera diariamente o controle sobre pagamentos que depois deve avali
- Uma despesa realizada de R$ 115 mil ante orçamento de R$ 100 mil representa variação de 15
- Qual atividade representa melhor a função típica da controladoria?
- Em uma organização vista como sistema aberto, qual elemento descreve o feedback?
- Qual opção contém somente componentes do COSO de controle interno?
- Uma empresa usa validação prévia para impedir pedidos sem aprovação. O controle é, princip
- Em relação ao controle interno público, o que estabelece o art. 74 da Constituição Federal
- Qual das três linhas do modelo de governança executa controles operacionais no cotidiano?
- O indicador "atendimentos concluídos no mês" mede diretamente:
- O que caracteriza mais diretamente o orçamento de caixa de uma empresa?
- O setor comercial cumpriu sua meta, mas criou atrasos na logística. Qual leitura sistêmica
- Uma organização identificou um risco antes de implementar qualquer ação mitigadora. Esse n
- Em uma matriz risco–controle, qual associação é a mais coerente?
- Qual cenário distingue adequação do desenho e eficácia operacional de um controle?
- Sobre a IN Conjunta MP/CGU nº 1/2016, assinale a interpretação adequada.
- Uma unidade de auditoria interna passa a autorizar todos os pagamentos que depois avalia. 
- Uma equipe realizou 810 análises diante de 900 previstas. Qual a eficácia quantitativa?
- No orçamento de produção, a empresa prevê vender 1.000 unidades, deseja estoque final de 1
- Uma política exige reduzir custos de atendimento, mas as reclamações sobem. Qual providênc
- Ao revisar um programa de integridade, qual alternativa associa corretamente componente CO
- Uma empresa instala seguro contra sinistros e deixa de examinar riscos de paralisação de s
- Uma plataforma bloqueia pagamentos duplicados, mas usuários administradores podem alterar 
- Em um Legislativo estadual, edital cita expressamente a estrutura da secretaria de control
- Uma chefia solicita que auditoria interna escolha e aprove o apetite a risco de toda a ins
- Um painel mostra 20% mais inspeções, mas também alta de acidentes no território. Qual conc
- Uma despesa orçada em R$ 80 mil foi realizada por R$ 92 mil, com volume de produção 30% ma
```

## Pedido 15: Convenção sobre o Crime Cibernético (Convenção de Budapeste) (`legislacao.crime-cibernetico-convencao`, 18 tópicos de edital)

```
Matéria: legislacao.crime-cibernetico-convencao (Convenção sobre o Crime Cibernético (Convenção de Budapeste))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, incorporação brasileira e categorias de crimes dos arts. 2º a 6º": 20
- "2. Falsificação, fraude, conteúdo, direitos autorais e alcance dos poderes processuais": 20
- "3. Prova digital, preservação expedita, apresentação e busca: arts. 15 a 19": 20
- "4. Captura em tempo real, jurisdição e limites de medidas: arts. 20 a 22": 20
- "5. Cooperação internacional, extradição, conservação transfronteiriça e rede 24/7": 20

Já existem (não repita):
- Uma equipe precisa identificar qual ato promulgou internamente a Convenção de Budapeste e 
- Uma pessoa acessa deliberadamente sistema privado de terceiro sem autorização, sem ainda c
- Alguém capta clandestinamente, por meios técnicos, transmissão de dados não públicos duran
- Uma alteração dolosa e não autorizada apaga planilha de outro usuário, mas o funcionamento
- Uma ferramenta foi desenvolvida para testar a segurança de servidores, utilizada com autor
- Uma pessoa altera registro eletrônico para que um documento fictício pareça autêntico em p
- Um agente adultera sistema de pagamentos para obter transferência financeira indevida, pro
- Em discussão sobre o art. 9º, uma equipe observa que a própria Convenção admite reserva de
- Um questionário descreve violação intencional de direitos autorais em escala comercial por
- Uma investigação sobre delito comum necessita de prova eletrônica, embora o crime não tenh
- Uma autoridade planeja buscar dados digitais e sustenta que a Convenção dispensa proporcio
- Uma autoridade competente deseja preservar dados já guardados por provedor enquanto busca 
- Durante preservação de tráfego, a autoridade necessita descobrir quais provedores particip
- Uma ordem dirigida a provedor em atuação no território solicita nome, endereço e dados con
- Autoridade inicia busca em computador de seu território e descobre indícios fundados de ar
- Investigadores registram, no momento em que ocorrem, informações de origem, destino e dura
- Uma equipe pretende captar o conteúdo de mensagens futuras em tempo real e argumenta que o
- Um pedido extradicional referente a delito convencional é examinado à luz do art. 24, § 1º
- Um Estado pede a outro conservação expedita de dados armazenados para depois requerer sua 
- Uma Parte deseja acessar dados armazenados em servidor de outra Parte sem autorização prév
- Ao analisar a adesão brasileira, uma candidata concluiu que todas as reservas facultadas p
- Entre Partes sem tratado de assistência aplicável, chega pedido urgente de dados, e a legi
- Depois de preservar registros de tráfego por pedido internacional, a Parte requerida desco
- Em perícia sobre mensagens eletrônicas, não há registro de origem ou manuseio e nenhum exa
- Chega solicitação urgente de preservação internacional que deve ser encaminhada à rede 24/
- Em investigação digital, o registro de origem, destino, percurso e duração de uma comunica
- Uma pessoa apaga arquivo alheio e torna seu conteúdo inacessível. O ato será necessariamen
- A ordem interna de preservação de dados armazenados prevista no art. 16 pode fixar prazo n
- Para o art. 18, § 3º, dados cadastrais de assinantes compreendem o teor de mensagens priva
- A Convenção determina no art. 35 que cada Parte mantenha contato disponível 24 horas por d
- O art. 9º, § 4º, por admitir reservas sobre determinadas condutas e representações, torna 
- O art. 11, § 2º, trata da tentativa dolosa para categorias convencionais identificadas, en
- No art. 12 da Convenção, toda responsabilização da pessoa jurídica deve ter natureza penal
- Como regra geral, o art. 14, § 2º, alcança a coleta de provas eletrônicas mesmo de delitos
- Segundo o art. 19, § 2º, a busca iniciada em sistema doméstico pode ser ampliada prontamen
- O art. 21 exige que a captação em tempo real do conteúdo de comunicações se relacione a cr
- Um Estado requerido pode exigir identidade literal dos tipos penais para qualquer pedido d
- Em pedido de extradição por crime previsto na Convenção, o art. 24 não elimina causas de r
- Pelo art. 32, basta que o proprietário técnico do servidor consinta informalmente para que
- Na ausência de tratado de assistência mútua aplicável, o art. 27 contempla, para situações
- O art. 33 disciplina assistência internacional para interceptar o conteúdo semântico de me
- Em exame de vestígio digital, a falta de um registro formal pode exigir avaliação concreta
- A designação brasileira de Polícia Federal como ponto de contato 24/7 representa reserva a
- O art. 22 prevê jurisdição territorial e hipóteses de embarcação, aeronave ou nacionalidad
- A Convenção permite à Parte requerida recusar assistência relativa aos crimes dos arts. 2º
```

## Pedido 16: Contabilidade gerencial e análise de custos para decisão (`contabilidade.custos-gerencial`, 18 tópicos de edital)

```
Matéria: contabilidade.custos-gerencial (Contabilidade gerencial e análise de custos para decisão)
Capítulos e quantas questões quero em cada um (total 100):
- "Decisão gerencial, margem de contribuição e análise diferencial": 20
- "Ponto de equilíbrio, margem de segurança e metas de resultado": 20
- "Alavancagem operacional e risco da estrutura de resultados": 20
- "Custo-padrão, análise de variações e controle responsável": 20
- "Orçamento empresarial, indicadores e avaliação de desempenho": 20

Já existem (não repita):
- A margem de contribuição unitária corresponde ao preço líquido de venda subtraído dos cust
- Um produto com margem de contribuição unitária positiva necessariamente gera lucro operaci
- No modelo de um único produto, com margem de contribuição unitária positiva, o ponto de eq
- O ponto de equilíbrio econômico é idêntico ao contábil, pois ambos desconsideram o lucro d
- O grau de alavancagem operacional pode ser calculado, no modelo clássico, dividindo-se a m
- O custo-padrão é uma referência predeterminada usada para controle e análise das diferença
- No orçamento de produção, o volume a fabricar deve considerar, além das vendas previstas, 
- Um gasto passado irrecuperável deve ser somado aos custos incrementais de cada alternativa
- Ao avaliar a descontinuação de uma linha de produtos, os custos fixos comuns rateados deve
- A margem de segurança percentual, quando definida sobre as vendas previstas, é calculada c
- Em um modelo com gastos fixos positivos e margem de contribuição unitária negativa, basta 
- Se o lucro operacional for nulo no ponto de equilíbrio, o grau de alavancagem operacional 
- Para calcular a variação de eficiência da mão de obra, devem ser comparadas as horas reais
- Orçamento flexível e orçamento contínuo são sinônimos, pois ambos consistem exclusivamente
- Em uma máquina com capacidade limitada, a linha de maior margem de contribuição unitária d
- Para uma empresa com dois produtos e composição de vendas constante, uma cesta representat
- Se uma empresa aumenta seu volume dentro da faixa relevante, mantendo preço, gastos variáv
- Na convenção custo real menos custo-padrão, uma variação positiva de preço de materiais re
- Uma variação de taxa de mão de obra favorável pode coexistir com variação de eficiência de
- A decomposição ROI = margem operacional multiplicada pelo giro do investimento depende da 
- Uma loja vende produto por R$ 80, tem custo variável unitário de R$ 35 e comissão variável
- Uma empresa tem gastos fixos de R$ 12.000 e margem de contribuição unitária de R$ 30. Qual
- Com margem de contribuição total de R$ 90.000 e resultado operacional de R$ 30.000, qual é
- Qual opção melhor descreve o custo-padrão em uma empresa industrial?
- Uma empresa prevê vender 800 unidades, manter estoque final de acabados em 120 e possui es
- Qual indicador relaciona resultado operacional e receita líquida de vendas?
- Qual é a melhor definição de custo de oportunidade para decisões gerenciais?
- O custo fixo é R$ 16.000, o preço unitário é R$ 100 e os gastos variáveis somam R$ 60 por 
- Uma empresa prevê 1.000 vendas e equilíbrio em 750 unidades. Qual a margem de segurança re
- Uma fábrica tem MC total de R$ 50 mil e lucro operacional de R$ 10 mil. Mantidas as premis
- Para produzir 50 unidades, o padrão estabelece 3 kg de material por produto a R$ 10/kg. Co
- Com taxa-padrão de R$ 25/h, taxa real de R$ 27/h e 300 horas efetivamente trabalhadas, qua
- Ao comparar o custo variável orçado para 10.000 unidades com o gasto real referente a 12.0
- Um segmento apresenta MC de R$ 70 mil, fixos próprios evitáveis de R$ 45 mil e rateio de f
- Um equipamento dispõe de 240 minutos e há demanda para ambos os produtos. A gera MC de R$ 
- O mix fixo de venda corresponde a duas unidades de X (MC R$ 15 cada) para uma de Y (MC R$ 
- Uma companhia opera com lucro positivo, vende 2.000 unidades e apresenta ponto de equilíbr
- Na produção efetiva, o padrão permitiria 400 kg a R$ 8/kg. Foram consumidos 440 kg a R$ 9/
- Foram usadas 440 horas a R$ 19/h; o padrão permitido era 400 horas a R$ 20/h. Qual o desvi
- Uma unidade apresenta margem operacional de 12% e giro de investimento de 1,5 vez, calcula
- Uma empresa projeta consumir 2.400 kg de matéria-prima, deseja estoque final de 500 kg e t
- Há capacidade ociosa e um pedido excepcional por 1.000 unidades a R$ 42 cada. O custo vari
```

## Pedido 17: Propriedades coligativas das soluções (`ciencias.propriedades-coligativas`, 18 tópicos de edital)

```
Matéria: ciencias.propriedades-coligativas (Propriedades coligativas das soluções)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Soluções e número de partículas": 17
- "2. Pressão de vapor e lei de Raoult": 17
- "3. Elevação do ponto de ebulição": 17
- "4. Abaixamento do ponto de congelamento": 17
- "5. Osmose e pressão osmótica": 16
- "6. Como o tema costuma ser cobrado": 16

Já existem (não repita):
- Dissolvem-se 0,20 mol de um soluto molecular não ionizável em 0,50 kg de solvente. Qual é 
- Uma solução aquosa diluída de soluto molecular tem molalidade 0,30 mol/kg. Se Kb da água é
- Uma solução ideal de eletrólito tem i=2 e molalidade 0,25 mol/kg. Para água, Kf=1,86 °C·kg
- À mesma pressão externa, um solvente puro ferve a 100,00 °C e uma solução não volátil apre
- Uma solução ideal contém 8 mol de solvente e 2 mol de soluto não volátil. Se a pressão de 
- Em uma solução ideal, a fração molar do soluto não volátil é 0,10 e a pressão de vapor do 
- Dissolvem-se 0,50 mol de um soluto molecular em 2,0 kg de solvente. Com Kf=1,86 °C·kg/mol,
- Calcule a pressão osmótica ideal de uma solução não eletrolítica 0,10 mol/L a 300 K, usand
- Uma solução contém 0,50 kg de água e deve apresentar ΔTb=1,04 °C. Sabendo Kb=0,52 °C·kg/mo
- Qual é a pressão osmótica de uma solução não eletrolítica 0,20 mol/L a 27 °C? Use T=300 K 
- Uma solução ideal de NaCl tem molalidade 0,10 mol/kg e fator i=2. Para água, Kf=1,86 °C·kg
- Uma solução aquosa ideal tem molalidade 0,50 mol/kg, i=2 e Kf=1,86 °C·kg/mol. Se a água pu
- Uma solução ideal contém 9 mol de solvente e 1 mol de soluto não volátil. Se a pressão de 
- Uma massa de 18 g de soluto não eletrolítico é dissolvida em 0,20 kg de solvente. A soluçã
- Uma solução diluída de eletrólito tem M=0,050 mol/L e i=2 a 300 K. Usando R=0,082 L·atm·mo
- Dissolvem-se 5,85 g de NaCl (massa molar 58,5 g/mol) em 0,50 kg de água. Admitindo i=2 e K
- Uma solução molecular não eletrolítica tem molalidade 0,10 mol/kg. Para o solvente, Kf=1,8
- Compare uma solução molecular 0,25 molal (i=1) com uma solução ideal de CaCl₂ 0,10 molal (
- A pressão parcial do solvente numa solução ideal é 32 kPa, enquanto a pressão de vapor do 
- Duas soluções diluídas estão à mesma temperatura. A solução X tem M=0,15 mol/L e i=2; a so
- Uma solução tem ΔTf de 2 °C e o solvente puro congela a −5 °C. Qual é a temperatura de con
- A pressão osmótica é a pressão que, aplicada à solução, pode deter o fluxo líquido de solv
- Uma solução contém 0,30 mol de soluto em 0,60 kg de solvente. Sem dissociação, sua molalid
- Numa solução ideal de soluto não volátil, se a fração molar do solvente é 0,90 e sua press
- Na expressão ideal π = iMRT, qual conjunto de fatores determina a pressão osmótica quando 
- À mesma molalidade e com mesmo solvente em solução diluída, um eletrólito ideal que se dis
- Duas soluções diluídas do mesmo solvente têm molalidade igual, mas uma usa soluto molecula
- A molalidade de uma solução é definida por mols de soluto por
- A adição de soluto não volátil a um solvente tende a reduzir sua pressão de vapor na soluç
- Uma solução apresenta ΔTb positivo em relação ao solvente puro. O ponto de ebulição da sol
- A relação ideal π = iMRT descreve pressão osmótica de solução diluída. Qual temperatura de
- Uma solução ideal contém 0,20 mol de soluto não dissociado em 0,50 kg de solvente. Sua mol
- Na osmose ideal, uma membrana semipermeável permite passagem preferencial do
- Uma solução aquosa tem molalidade 0,5 mol/kg e Kf = 1,86 °C·kg/mol, com soluto não dissoci
- Num modelo ideal, um eletrólito que se dissocia em três partículas por unidade de soluto t
- Pressão de vapor menor devido à presença de soluto não volátil ajuda a explicar elevação d
- Para ΔTf = iKf m, a constante Kf é característica do solvente usado no modelo e não do rec
- Duas soluções com igual número efetivo de partículas por quilograma de solvente podem apre
- Molaridade e molalidade usam ambas quilogramas de solvente como denominador, portanto são 
- Um soluto não volátil impede completamente que moléculas do solvente evaporem da solução.
- A pressão osmótica ideal aumenta com a concentração molar de partículas e com a temperatur
- Uma solução hipotônica tem pressão osmótica maior que sua solução de referência em compara
- Soluto não volátil tende a elevar o ponto de ebulição em comparação com o solvente puro so
- No congelamento de uma solução diluída, o sólido formado costuma ser predominantemente sol
- Se um soluto molecular não se dissocia, fator de van 't Hoff ideal é aproximadamente um po
- A lei de Raoult ideal para solvente em solução usa sua fração molar multiplicada pela pres
- Osmose líquida tende a transportar solvente da solução mais concentrada para a mais diluíd
- Na expressão ΔTb = iKb m, dobrar molalidade dobra a elevação do ponto de ebulição no model
- Fator i real de um eletrólito em solução pode diferir do valor inteiro ideal por interaçõe
- Uma membrana semipermeável ideal permite passagem irrestrita de qualquer soluto e bloqueia
- A magnitude de ΔTf pode ser positiva embora a temperatura final de congelamento da solução
- Uma solução concentrada sempre obedece exatamente às relações lineares coligativas ideais,
- Para comparar propriedades coligativas, basta comparar a massa em gramas de soluto sem lev
- Um soluto volátil pode contribuir para a fase vapor, portanto a redução simples da pressão
- A elevação ebulioscópica não informa por si só a rapidez com que a solução ferve nem a esp
- Em aplicação anticongelante, o abaixamento do ponto de congelamento significa que o solven
- Pressão osmótica é a pressão necessária para impedir o fluxo osmótico líquido através de m
- Em soluções diluídas ideais, a constante Kb depende do soluto específico e permanece igual
- Usar molaridade no lugar de molalidade em ΔTf = iKf m é sempre correto porque volume da so
- A mesma membrana pode permitir solvente e restringir soluto, e a direção do fluxo líquido 
- Se a quantidade de partículas efetivas dobra no mesmo solvente e regime diluído, as mudanç
```

## Pedido 18: Registro civil das pessoas naturais e tabelionato de notas (`direito-notarial-registral.registro-civil-tabelionato`, 18 tópicos de edital)

```
Matéria: direito-notarial-registral.registro-civil-tabelionato (Registro civil das pessoas naturais e tabelionato de notas)
Capítulos e quantas questões quero em cada um (total 100):
- "Registro civil: função, nascimento, certidões e unidades interligadas": 17
- "Filiação, parentalidade socioafetiva e alterações de nome e gênero": 17
- "Casamento civil, habilitação, assento matrimonial e óbito": 17
- "Averbações, retificações e traslado de registros estrangeiros": 17
- "Tabelionato de notas: escrituras, procurações e atas notariais": 16
- "Inventário, partilha e divórcio na via extrajudicial": 16

Já existem (não repita):
- Uma candidata de concurso afirma que assento de nascimento e certidão de nascimento são a 
- Sobre o primeiro registro de nascimento e a emissão da primeira certidão no Brasil, assina
- Uma maternidade possui unidade interligada de registro civil. Qual descrição preserva a co
- Ao receber pedido de certidão em inteiro teor com informações pessoais sensíveis, o oficia
- Em pedido de reconhecimento voluntário de parentalidade socioafetiva, qual comportamento a
- Um interessado pede o reconhecimento socioafetivo extrajudicial de criança de oito anos. Q
- Após completar 18 anos, alguém deseja modificar seu prenome pela primeira vez, sem apresen
- Pessoa trans solicita adequação extrajudicial do prenome e do gênero, e o atendente condic
- Durante habilitação matrimonial, o cartório disponibiliza material educativo da Resolução 
- No serviço de registro civil, casal formado por duas pessoas do mesmo sexo apresenta pedid
- Nubentes elegem regime convencional distinto da comunhão parcial, em hipótese em que é adm
- Um recém-nascido nasce com vida e morre poucas horas depois. Qual conduta é compatível com
- É detectado evidente erro de grafia em assento, verificável imediatamente pelos documentos
- Uma pessoa pretende trocar o pai registral com base em alegações contraditórias entre inte
- Um brasileiro traz certidão de casamento expedida no exterior e deseja o traslado no Brasi
- A averbação do divórcio no registro de casamento tem por finalidade principal:
- Uma parte deseja lavrar ata para documentar página da internet visualizada pelo tabelião. 
- Sobre a escolha do tabelião de notas para lavratura de escritura pública, assinale a corre
- Uma procuração outorga ao representante apenas poderes gerais de administração. Qual infer
- Qual característica integra a escritura pública à luz do Código Civil?
- Inventário consensual envolve herdeiro menor que receberá parte ideal de cada bem e há man
- Um casal com filho incapaz pretende divórcio consensual no tabelionato. Qual condição espe
- O falecido deixou testamento. Qual entendimento se compatibiliza com a Resolução CNJ nº 35
- Em divórcio consensual extrajudicial, qual afirmação descreve corretamente a atuação do ad
- Depois de formalizar escritura pública de divórcio consensual, os interessados querem atua
- No Registro Civil das Pessoas Naturais, a certidão substitui a necessidade de existir asse
- As unidades interligadas das maternidades buscam facilitar o registro de nascimento, sem t
- O primeiro registro de nascimento e a primeira certidão são gratuitos, sem que isso impliq
- A referência editalícia ao papel de segurança unificado deve ser estudada isoladamente, po
- O reconhecimento voluntário de parentalidade socioafetiva no RCPN exige apuração objetiva 
- A pessoa maior de idade pode pedir, sem justificativa especial, a alteração extrajudicial 
- O reconhecimento voluntário de paternidade pode ser livremente desfeito por arrependimento
- Para alteração extrajudicial de nome e gênero de pessoa transgênero, é requisito universal
- O material informativo de preparação para o casamento civil previsto na Resolução CNJ nº 4
- A Resolução CNJ nº 175/2013 autoriza o oficial de registro a negar a habilitação de pessoa
- A ocorrência de nascimento com vida seguida de falecimento deve ser examinada de forma dis
- Toda alteração posterior em assento de casamento gera um novo registro de nascimento, porq
- A LRP admite hipóteses de retificação pelo próprio oficial sem autorização judicial prévia
- O traslado de registros civis de brasileiros lavrados no exterior está disciplinado pela R
- A Central de Serviços Eletrônicos Compartilhados do Registro Civil das Pessoas Jurídicas é
- A lavratura de ata notarial pode atestar conteúdo efetivamente visualizado pelo tabelião, 
- A fé pública de uma escritura dispensa o tabelião de conferir identidade, capacidade e von
- Após a Resolução CNJ nº 571/2024, a existência de menor interessado em inventário deixou d
- Para lavrar divórcio consensual por escritura com filhos comuns menores, a concordância ve
- A escritura válida de divórcio consensual dispensa homologação judicial, mas sua apresenta
```

## Pedido 19: Física médica: radioterapia, dosimetria e radioproteção (`saude.fisica-medica-radioterapia-dosimetria`, 18 tópicos de edital)

```
Matéria: saude.fisica-medica-radioterapia-dosimetria (Física médica: radioterapia, dosimetria e radioproteção)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos físicos, interações e grandezas radiológicas": 20
- "Detectores de radiação, calibração e dosimetria clínica e ocupacional": 20
- "Radiobiologia, radiossensibilidade e fracionamento": 20
- "Teleterapia, braquiterapia e planejamento do tratamento": 20
- "Controle de qualidade, segurança operacional e radioproteção": 20

Já existem (não repita):
- Em dosimetria de radioterapia, qual grandeza tem como unidade o gray (Gy)?
- Um fóton com energia de 0,8 MeV interage com matéria. Qual conclusão é correta acerca da p
- Uma equipe extrapola a lei I=I0 exp(-μx), válida para feixe estreito monoenergético, para 
- No eixo central de um feixe de fótons megavoltagem, a dose pode aumentar nos primeiros mil
- O becquerel indica a atividade de uma fonte radioativa, enquanto o gray expressa dose abso
- O efeito Compton pode produzir um elétron de recuo e um fóton espalhado, sendo relevante n
- Na presença de equilíbrio eletrônico, kerma e dose absorvida são sempre idênticos, indepen
- Ao dobrar a distância de uma fonte pontual ideal no vácuo, a taxa de fluência cai a um qua
- Qual detector é tradicionalmente empregado na dosimetria de referência de feixes externos,
- Após troca de câmara de ionização, o físico obtém leituras estáveis e passa a utilizar o f
- Em campo estático muito pequeno de radiocirurgia, um dosímetro apresenta dependência impor
- Um dosímetro OSL é usado para monitorar exposição ocupacional. Qual afirmação caracteriza 
- TLD e OSL são exemplos de detectores cuja informação dosimétrica pode ser obtida por leitu
- Um contador Geiger–Müller, sem calibração nem caracterização energética, fornece diretamen
- No formalismo de dose absorvida na água da AIEA, é relevante corrigir a leitura instrument
- Dosímetros pessoais, levantamentos de área e monitoração ambiental são procedimentos total
- Entre os tecidos listados, qual costuma apresentar resposta aguda relevante por elevada re
- No fracionamento radioterápico, qual dos cinco Rs pode favorecer o controle de células tum
- Considere um esquema de 30 Gy em 10 frações iguais e α/β = 10 Gy. No modelo linear-quadrát
- Qual definição descreve corretamente a eficácia biológica relativa (RBE)?
- Rupturas de dupla fita no DNA são relevantes para os efeitos biológicos da radiação ioniza
- A gravidade de todo câncer radioinduzido aumenta de modo linear com a dose inicial, o que 
- Tecidos de baixa taxa de proliferação podem apresentar efeitos tardios importantes da irra
- O valor de α/β utilizado no modelo linear-quadrático é idêntico para todos os tumores e ór
- Durante o delineamento de radioterapia, o volume que representa doença tumoral macroscópic
- Um plano mostra cobertura aceitável do alvo no histograma dose-volume (DVH), mas a equipe 
- No carregamento remoto de braquiterapia de alta taxa de dose, o deslocamento controlado de
- O uso de imagens no posicionamento e verificação do paciente durante radioterapia caracter
- Na teleterapia, a fonte emissora fica externamente ao paciente, ao contrário de técnicas d
- O PTV corresponde exclusivamente à extensão tumoral visível em imagem e não deve considera
- O movimento respiratório de um tumor pulmonar pode exigir representação específica no plan
- Em radioterapia com feixes de fótons, a simples troca de uma técnica 3D-CRT por IMRT dispe
- Qual opção representa corretamente a distinção entre teste de aceitação e comissionamento 
- Na radioproteção de trabalhadores e público, qual é uma combinação clássica de medidas prá
- Em uma instalação com braquiterapia HDR, há indicação de que a fonte não foi recolhida ao 
- Conforme o enquadramento regulatório brasileiro verificado em 2026, qual norma da ANSN se 
- Intertravamentos de porta, indicadores de irradiação e dispositivos de parada de emergênci
- O limite de dose ocupacional de um trabalhador pode ser aplicado sem adaptação como dose m
- As normas brasileiras vigentes em 2026 podem aparecer sob designação ANSN, mesmo quando a 
- Uma boa constância de saída do feixe elimina a necessidade de revisar transferência de par
```

## Pedido 20: Estatuto do Servidor da Bahia (Lei nº 6.677/1994) (`legislacao.servidores-bahia-lei-6677`, 18 tópicos de edital)

```
Matéria: legislacao.servidores-bahia-lei-6677 (Estatuto do Servidor da Bahia (Lei nº 6.677/1994))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ingresso, provimento, posse e exercício": 20
- "2. Estágio, estabilidade, movimentação e vacância": 20
- "3. Vantagens, férias, licenças e ausências": 20
- "4. Direito de petição, deveres e penalidades": 20
- "5. Apuração: investigação preliminar, sindicância e processo disciplinar": 20

Já existem (não repita):
- Segundo a Lei nº 6.677/1994, são formas de provimento de cargo público:
- Pela Lei nº 6.677/1994, o concurso público tem validade de até 2 anos, prorrogável uma úni
- No Estatuto do Servidor da Bahia, havendo empate entre candidatos de concurso, terá prefer
- De acordo com a Lei nº 6.677/1994, a posse deve ocorrer em até 30 dias da publicação da no
- Pela Lei nº 6.677/1994, se a posse não ocorrer dentro do prazo, o nomeado será exonerado d
- Segundo o Estatuto do Servidor da Bahia, a posse pode ocorrer por procuração específica, e
- Conforme a Lei nº 6.677/1994, o prazo para o servidor entrar em exercício e a consequência
- Entre os requisitos básicos para ingresso no serviço público previstos na Lei nº 6.677/199
- Pela Lei nº 6.677/1994, o servidor nomeado para cargo de provimento permanente adquire est
- Segundo o Estatuto do Servidor da Bahia, o servidor estável só perderá o cargo em virtude 
- Pela Lei nº 6.677/1994, a promoção não alcança o servidor em estágio probatório, salvo:
- Na Lei nº 6.677/1994, reversão é:
- Pela Lei nº 6.677/1994, não poderá reverter o aposentado que contar 70 anos de idade.
- Segundo o Estatuto do Servidor da Bahia, extinto o cargo ou declarada sua desnecessidade, 
- Pela Lei nº 6.677/1994, recondução é o retorno do servidor estável, com direito a indeniza
- No Estatuto do Servidor da Bahia, a disponibilidade para exercício de mandato em diretoria
- Segundo a Lei nº 6.677/1994, a exoneração de ofício do ocupante de cargo de provimento per
- Pela Lei nº 6.677/1994, a vacância do cargo decorre de exoneração, demissão, aposentadoria
- No Estatuto do Servidor da Bahia, readaptação é o cometimento ao servidor de novas atribui
- Pela Lei nº 6.677/1994, o adicional por tempo de serviço é devido ao servidor com mais de 
- Segundo a Lei nº 6.677/1994, o serviço noturno, prestado entre 22 horas de um dia e 5 hora
- Pela Lei nº 6.677/1994, as férias podem ser acumuladas, por necessidade do serviço, até o 
- Conforme o Estatuto do Servidor da Bahia, o servidor que teve de 6 a 14 faltas no período 
- Pela Lei nº 6.677/1994, as férias somente podem ser interrompidas por calamidade pública, 
- Na Lei nº 6.677/1994, a licença por motivo de doença em pessoa da família que exceda 3 e n
- Segundo a Lei nº 6.677/1994, a licença para tratar de interesse particular pode ser conced
- Pela Lei nº 6.677/1994, a licença à servidora gestante é de 180 dias consecutivos, e a lic
- Conforme o Estatuto do Servidor da Bahia, o servidor pode ausentar-se do serviço, sem qual
- Pela Lei nº 6.677/1994, o servidor eleito vereador, havendo compatibilidade de horários, p
- Segundo a Lei nº 6.677/1994, sobre o direito de petição, é correto afirmar que:
- Pela Lei nº 6.677/1994, o direito de requerer prescreve em 5 anos quanto aos atos de demis
- No Estatuto do Servidor da Bahia, a penalidade de suspensão:
- Pela Lei nº 6.677/1994, as penalidades de advertência e suspensão têm seus registros cance
- Segundo a Lei nº 6.677/1994, configuram abandono de cargo e inassiduidade habitual, respec
- Pela Lei nº 6.677/1994, apurada em processo disciplinar a acumulação proibida e provada a 
- De acordo com a Lei nº 6.677/1994, compete ao chefe da repartição e a outras autoridades i
- Pela Lei nº 6.677/1994, a ação disciplinar prescreve em:
- Após a Lei nº 14.803/2024, a demissão ou a cassação de aposentadoria, em qualquer hipótese
- Segundo a Lei nº 6.677/1994, com as alterações de 2024, a investigação preliminar ou a sin
- Pela Lei nº 6.677/1994, a comissão sindicante é composta de 3 membros estáveis e tem prazo
- Segundo a Lei nº 6.677/1994, é obrigatória a instauração de processo disciplinar sempre qu
- Pela Lei nº 6.677/1994, o afastamento preventivo do servidor acusado, para que não influa 
- Conforme a Lei nº 6.677/1994, o processo administrativo disciplinar deve ser iniciado em 5
- Segundo a Lei nº 6.677/1994, ultimada a instrução, o acusado tem 10 dias para apresentar d
```

## Pedido 21: Modernismo e literatura contemporânea (`literatura.modernismo-contemporanea`, 18 tópicos de edital)

```
Matéria: literatura.modernismo-contemporanea (Modernismo e literatura contemporânea)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Antecedentes, vanguardas e Semana de Arte Moderna": 14
- "2. Primeira fase modernista: experimentação e projetos de Brasil": 14
- "3. Segunda fase: prosa social e maturação da poesia": 13
- "4. Terceira geração: Geração de 45, Clarice, Rosa e João Cabral": 13
- "5. Concretismo, poesia marginal e literatura contemporânea": 46

Já existem (não repita):
- Em uma linha do tempo, um estudante precisa identificar o evento de 1922 reconhecido como 
- Um poema propõe transformar influências culturais estrangeiras em criação própria e critic
- Um comentário associa uma rapsódia de 1928 à reinvenção de mitos, oralidade e múltiplas re
- Uma narrativa descreve experiências de uma família sob a seca, utiliza construção verbal c
- Uma leitora identifica na prosa de uma autora o mergulho na consciência, episódios cotidia
- Uma atividade compara poemas em que a distribuição das palavras na página integra o signif
- Ao estudar a produção poética dos anos 1970, uma pessoa encontra edições independentes, di
- Uma linha do tempo divide didaticamente o Modernismo em primeira, segunda e terceira fases
- Um professor propõe contrastar a liberdade de forma da poesia de 1922 com o desenvolviment
- Um estudante identifica um texto de 1924 que propõe reapresentar poeticamente o Brasil por
- Um comentário afirma que os escritores da primeira fase partilhavam um mesmo nacionalismo.
- Uma análise de *Vidas Secas* considera a construção concisa das frases e a experiência das
- Uma leitura compara a poesia de Drummond à de Cecília Meireles na segunda fase. Qual relaç
- Uma resenha denomina João Cabral de Melo Neto poeta engenheiro, mas ressalta temas sociais
- Ao examinar *Grande Sertão: Veredas*, um leitor observa uma voz que rememora episódios e r
- Um texto contemporâneo comenta sua própria construção, interrompe a narrativa para problem
- Uma crítica identifica fragmentação, simultaneidade urbana e imagens de máquina em poema d
- Um ensaio contrapõe a rapsódia *Macunaíma* e os capítulos breves de *Memórias Sentimentais
- Em um estudo, um romance de 1930 e um poema social de Drummond são comparados quanto ao en
- Uma classificação situa *Perto do Coração Selvagem* em 1943 e a consolidação da terceira f
- Uma interpretação de *A Hora da Estrela* considera a personagem Macabéa e a presença do na
- Duas análises comparam uma composição concretista rigorosamente espacial e um poema margin
- Um leitor vê em um romance contemporâneo arquivos fictícios, paródia de documentos, fragme
- Um crítico compara *Morte e Vida Severina* com um poema parnasiano formalmente regular. Qu
- Uma proposta de leitura compara Carolina Maria de Jesus, Conceição Evaristo e ficções cont
- A Semana de Arte Moderna ocorreu no Theatro Municipal de São Paulo, em fevereiro de 1922, 
- A exposição de Anita Malfatti que contribuiu para o debate modernista aconteceu em 1930, c
- Na primeira fase modernista, a exploração da linguagem coloquial e do cotidiano podia coex
- O romance Vidas Secas, de Graciliano Ramos, integra a primeira fase modernista de 1922 e f
- A obra de Clarice Lispector frequentemente mobiliza introspecção e momentos de percepção t
- O grupo Noigandres surgiu durante a Semana de 1922 e foi responsável pela publicação origi
- O termo poesia marginal pode indicar formas alternativas de editar e distribuir poemas, so
- A renovação de 1922 exigia que qualquer escritor modernista abandonasse definitivamente o 
- A metáfora de antropofagia cultural, no programa oswaldiano, propõe transformação crítica 
- A narrativa regionalista de 1930 constitui registro documental neutro; por isso, ao interp
- A obra poética de João Cabral de Melo Neto evidencia que organização rigorosa e reflexão s
- O sertão na ficção de Guimarães Rosa funciona apenas como reprodução pitoresca de costumes
- Na poesia concreta, a disposição gráfica pode participar da produção do sentido, em intera
- Na interpretação literária, pós-modernismo é uma escola brasileira fundada em data consens
- A obra *Perto do Coração Selvagem*, publicada em 1943, ilustra o fato de que uma inovação 
- A existência de verso livre, paródia e fragmentação em uma obra assegura, por si só, sua c
- Ao comparar Clarice Lispector e Guimarães Rosa, é possível reconhecer formas distintas de 
- Como *Morte e Vida Severina* emprega musicalidade e formas próximas da tradição popular, é
- A denominação Geração de 45, tomada em sentido restrito, descreve tendências de formalizaç
- A noção de escrevivência, ligada a Conceição Evaristo, equivale a exigir que personagens e
```

## Pedido 22: Resistência estrutural de navios e plataformas e vibrações (`engenharia-naval.resistencia-estrutural-vibracoes`, 18 tópicos de edital)

```
Matéria: engenharia-naval.resistencia-estrutural-vibracoes (Resistência estrutural de navios e plataformas e vibrações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Elementos estruturais, ações internas e tensões": 36
- "2. Torção, cargas no navio e resistência primária": 23
- "3. Materiais, critérios de resistência e plataformas": 23
- "4. Vibração de estruturas": 18

Já existem (não repita):
- Em uma viga submetida a carga distribuída, o momento fletor atinge valor extremo (máximo o
- NÃO é ação interna (esforço solicitante) em uma seção de barra:
- O tensor de tensões em um ponto de um sólido é representado por uma matriz 3×3 simétrica, 
- Nos planos principais de tensão, as tensões de cisalhamento são nulas.
- Em um estado plano com σx = 80 MPa, σy = 20 MPa e τxy = 40 MPa, as tensões principais são:
- A deformação de cisalhamento (distorção) corresponde à variação, em radianos, do ângulo or
- Pela lei de Hooke generalizada para material isotrópico, a deformação normal na direção x 
- Para um aço com E = 208 GPa e coeficiente de Poisson 0,3, o módulo de elasticidade transve
- As equações de compatibilidade garantem que as deformações de um corpo correspondam a um c
- Em um eixo circular maciço submetido a torção, a tensão de cisalhamento:
- Pela fórmula de Bredt, em um tubo fechado de parede fina sob torção, o fluxo de cisalhamen
- Um tubo retangular fechado de parede fina, com área encerrada pela linha média de 0,5 m² e
- Seções abertas de parede fina, como um perfil em U, têm rigidez à torção muito menor que s
- Na análise da resistência longitudinal do navio, a curva de esforço cortante é obtida:
- Quando o navio está com a crista de uma onda a meia-nau e os cavados nas extremidades (alq
- No tosamento (sagging), com cavado a meia-nau, o chapeamento do convés tende a sofrer comp
- Na resistência primária, a tensão de flexão no convés de um navio com momento fletor de 2.
- A resistência secundária refere-se ao comportamento dos painéis enrijecidos entre antepara
- Os elementos estruturais que sofrem as maiores tensões de flexão longitudinal na viga-navi
- O ensaio Charpy avalia a tenacidade do material, isto é, a energia absorvida até a ruptura
- NÃO é propriedade obtida diretamente no ensaio de tração:
- Para materiais dúcteis, como os aços estruturais navais, o critério de resistência mais ut
- Em estado plano com tensões principais de 200 MPa e 100 MPa, a tensão equivalente de von M
- Pela fórmula de Euler, a carga crítica de flambagem de uma coluna é inversamente proporcio
- A fadiga é um modo de falha que pode ocorrer sob carregamento cíclico com tensões inferior
- A plataforma oceânica que se mantém em posição por tendões verticais tracionados, ancorado
- O FPSO é uma unidade flutuante de produção, armazenamento e transferência de petróleo, ger
- Os aços navais de alta resistência têm limite de escoamento mínimo superior ao dos aços de
- Para um sistema massa-mola com um grau de liberdade, a frequência natural angular é dada p
- Um sistema com massa de 100 kg e rigidez de 40.000 N/m tem frequência natural angular de:
- Na ressonância, quando a frequência da excitação coincide com a frequência natural, a ampl
- Um sistema estrutural discretizado com n graus de liberdade possui, em geral:
- Os modos de vibração de um sistema com vários graus de liberdade são ortogonais em relação
- Um hélice de 5 pás gira a 120 rpm. A frequência fundamental de passagem de pá, principal f
- O primeiro modo de vibração vertical da viga-navio, de menor frequência, é o modo de dois 
- Para reduzir a vibração de um painel do casco que entra em ressonância com o hélice, NÃO é
- Para materiais frágeis, como o ferro fundido, o critério de resistência mais adequado é o 
- Um eixo tubular circular tem momento polar de inércia J e raio externo r. Sob torque T, a 
- A razão de amortecimento de um sistema com um grau de liberdade é ζ = c/(2√(k·m)); com ζ m
- Painéis enrijecidos, formados por chapa e reforços, são o elemento estrutural típico do fu
```

## Pedido 23: Constituição do Estado do Rio Grande do Norte (`rio-grande-do-norte.constituicao-estadual`, 18 tópicos de edital)

```
Matéria: rio-grande-do-norte.constituicao-estadual (Constituição do Estado do Rio Grande do Norte)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios, organização do Estado e Municípios": 25
- "2. Poder Legislativo e processo legislativo": 25
- "3. Tribunal de Contas e Poder Executivo": 25
- "4. Poder Judiciário e funções essenciais à Justiça": 25

Já existem (não repita):
- Entre os fundamentos do Estado do Rio Grande do Norte, previstos no art. 1º da Constituiçã
- Segundo a Constituição do Rio Grande do Norte, a idade mínima para concorrer ao cargo de D
- Quem não receber, em dez dias, informações de seu interesse requeridas a órgão público est
- Segundo o art. 16 da Constituição do RN, NÃO se inclui entre os bens do Estado:
- A alienação de bens imóveis do Estado do Rio Grande do Norte depende de licitação e de pré
- Nas matérias de competência legislativa concorrente com a União, inexistindo lei federal s
- As leis orgânicas dos Municípios do Rio Grande do Norte são votadas em dois turnos, com in
- Pela Constituição do RN, a iniciativa popular de projetos de lei de interesse específico d
- O parecer prévio emitido pelo Tribunal de Contas do Estado sobre as contas anuais do Prefe
- Segundo o art. 25 da Constituição do RN, o Estado pode intervir em Município quando deixar
- O decreto de intervenção estadual em Município é submetido à apreciação da Assembleia Legi
- Compete privativamente à Assembleia Legislativa do RN autorizar o Governador e o Vice-Gove
- Compete privativamente à Assembleia Legislativa do RN julgar anualmente as contas prestada
- A exoneração, de ofício, do Procurador-Geral de Justiça depende de aprovação da Assembleia
- Desde a expedição do diploma, os Deputados Estaduais do RN não podem ser presos, salvo em 
- Nos casos em que o Deputado Estadual deixar de comparecer a um terço das sessões ordinária
- NÃO perde o mandato o Deputado Estadual do RN que for investido no cargo de:
- A sessão legislativa anual da Assembleia Legislativa do RN vai de 2 de fevereiro a 17 de j
- Pela redação atual do art. 42, § 4º, da Constituição do RN, os membros da Mesa da Assemble
- As Comissões Parlamentares de Inquérito da Assembleia Legislativa do RN são criadas median
- O processo legislativo estadual do Rio Grande do Norte, segundo o art. 44 da Constituição 
- Segundo a Constituição do RN, a proposta de emenda à Constituição Estadual é aprovada se o
- A Constituição do RN pode ser emendada por iniciativa popular, mediante proposta subscrita
- NÃO tem iniciativa de leis complementares e ordinárias, segundo o art. 46 da Constituição 
- Solicitada urgência pelo Governador para projeto de sua iniciativa, se a Assembleia não se
- Segundo o art. 48 da Constituição do RN, depende de lei complementar a organização do Pode
- O Governador do RN pode vetar projeto de lei, total ou parcialmente, no prazo de quinze di
- Não sendo a lei promulgada pelo Governador em 48 horas após a sanção tácita ou a derrubada
- Pode ser objeto de lei delegada no Rio Grande do Norte, segundo o art. 51 da Constituição 
- Dos sete Conselheiros do Tribunal de Contas do RN, segundo a Constituição Estadual, a Asse
- A nomeação dos Conselheiros do Tribunal de Contas indicados pelo Governador é precedida de
- Se, decorridos dez dias da data fixada para a posse, o Governador eleito não tiver assumid
- Em caso de impedimento do Governador e do Vice-Governador do RN, são chamados sucessivamen
- Vagando os cargos de Governador e Vice-Governador do RN no terceiro ano do período governa
- Ocorrendo a vacância dos cargos de Governador e Vice-Governador no último ano do período g
- Compete privativamente ao Governador do RN dispor sobre a organização e o funcionamento da
- O Governador do RN deve prestar à Assembleia Legislativa, anualmente, as contas referentes
- Nos crimes de responsabilidade, o Governador do RN é julgado por Tribunal Especial compost
- Instaurado o processo por crime de responsabilidade pelo Tribunal Especial, o Governador f
- Os Secretários de Estado do RN são escolhidos dentre brasileiros, no exercício dos direito
- Compete ao Tribunal de Justiça do RN processar e julgar, originariamente, a ação direta de
- Segundo a Constituição do RN, nos crimes comuns cometidos no exercício do cargo e em razão
- Segundo o art. 71, § 2º, da Constituição do RN, com redação da EC 18/2019, NÃO está entre 
- Quando o Tribunal de Justiça do RN aprecia, em tese, a inconstitucionalidade de norma esta
- O Procurador-Geral de Justiça do RN é nomeado pelo Governador, dentre integrantes da carre
- Os membros do Ministério Público do RN adquirem vitaliciedade após dois anos de exercício,
- Sobre a Procuradoria-Geral do Estado do RN, é correto afirmar que:
- À Defensoria Pública do RN são assegurados autonomia funcional e administrativa, iniciativ
```

## Pedido 24: Processo Administrativo no Estado do Rio Grande do Norte (LC 303/2005) (`rio-grande-do-norte.processo-administrativo-lc-303`, 18 tópicos de edital)

```
Matéria: rio-grande-do-norte.processo-administrativo-lc-303 (Processo Administrativo no Estado do Rio Grande do Norte (LC 303/2005))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Aplicação, princípios, direitos e deveres": 25
- "2. Atos administrativos: validade, invalidação e competência": 25
- "3. O processo: início, prazos, instrução, decisão e recursos": 25
- "4. Processos em espécie e contagem de prazos": 25

Já existem (não repita):
- Os preceitos da LC 303/2005 aplicam-se também aos órgãos dos Poderes Legislativo e Judiciá
- Havendo lei específica que discipline determinado processo administrativo, a LC 303/2005:
- Para a LC 303/2005, a unidade de atuação dotada de personalidade jurídica, de direito públ
- Segundo a LC 303/2005, somente a lei pode criar condicionamentos aos direitos dos particul
- Entre os critérios a observar nos processos administrativos, a LC 303/2005 prevê:
- Pela LC 303/2005, a assistência por advogado no processo administrativo é sempre obrigatór
- É dever do administrado perante a Administração Pública, conforme a LC 303/2005:
- Nos atos discricionários, a LC 303/2005 considera razão de invalidade a falta de correlaçã
- A motivação do ato administrativo, segundo a LC 303/2005, pode consistir em declaração de 
- A LC 303/2005 exige motivação expressa para o ato que dispense licitação ou declare sua in
- O direito da Administração Pública estadual do RN de invalidar seus atos administrativos d
- Segundo a LC 303/2005, os atos portadores de vício de ordem formal que possa ser suprido d
- A convalidação é admitida mesmo que o ato viciado já tenha sido impugnado na esfera admini
- Não havendo outra determinação legal, o prazo máximo para a prática de atos administrativo
- De acordo com a LC 303/2005, pode ser objeto de delegação:
- As decisões adotadas por delegação devem mencionar explicitamente essa qualidade e conside
- A avocação temporária de competência atribuída a órgão hierarquicamente inferior é permiti
- Inexistindo competência legal específica, o processo administrativo deverá ser iniciado pe
- É vedada à Administração a recusa imotivada de recebimento de documentos, devendo o servid
- Com a redação dada pela LC 679/2021, têm prioridade de tramitação os processos administrat
- É impedida de atuar em processo administrativo a autoridade que tenha amizade íntima com a
- A autoridade que deixa de comunicar seu impedimento ou suspeição no processo administrativ
- Pela LC 303/2005, salvo imposição legal, o reconhecimento de firma somente será exigido qu
- Inexistindo disposição específica, os atos da autoridade responsável e dos interessados no
- A intimação para comparecimento no processo administrativo deve observar antecedência míni
- O desatendimento de intimação válida importa o reconhecimento da verdade dos fatos pelo ad
- Quem comparece à consulta pública aberta em processo de interesse difuso ou coletivo adqui
- Quando o interessado declarar que fatos e dados estão registrados em documentos existentes
- O não atendimento, no prazo fixado, de pedido de documentos necessários à apreciação do pe
- Concluída a instrução, a Administração tem até 60 dias para decidir. Ultrapassado esse pra
- A desistência formulada por um dos interessados extingue o processo para todos e impede se
- O recurso administrativo previsto na LC 303/2005 é dirigido à autoridade que proferiu a de
- Salvo disposição legal específica, o prazo para interposição do recurso administrativo e o
- O não conhecimento do recurso administrativo, por intempestividade, impede a Administração
- Salvo disposição legal ou regulamentar em contrário, o recurso administrativo não tem efei
- Contra decisão tomada originariamente pelo Governador do Estado, a LC 303/2005 prevê:
- Na invalidação de ofício de ato administrativo, a autoridade deve intimar o interessado pa
- Sobre o processo sancionatório na LC 303/2005, é correto afirmar que:
- O pedido administrativo de ressarcimento por danos causados por agente público é dirigido 
- O requerimento de certidão será apreciado pela autoridade competente em 5 dias, que determ
- As informações pessoais requeridas por escrito pelo interessado aos órgãos estaduais devem
- A LC 303/2005 proíbe a inserção ou conservação, em fichários ou registros, de dados nomina
- No processo de denúncia previsto na LC 303/2005:
- Pela LC 303/2005, o concurso público para cargo ou emprego é conduzido por comissão especi
- Os prazos da LC 303/2005, salvo disposição expressa, são contínuos e não se interrompem ao
- O descumprimento injustificado, pela Administração, dos prazos previstos na LC 303/2005:
```

## Pedido 25: Microbiologia ambiental, monitoramento e conservação da biodiversidade (`biologia.microbiologia-ambiental-biodiversidade`, 17 tópicos de edital)

```
Matéria: biologia.microbiologia-ambiental-biodiversidade (Microbiologia ambiental, monitoramento e conservação da biodiversidade)
Capítulos e quantas questões quero em cada um (total 100):
- "Microbiologia do solo: processos, comunidades e indicadores": 17
- "Microbiologia da água, do ar e dos alimentos": 17
- "Amostragem ambiental, preservação, ensaios e qualidade": 17
- "Indicadores biológicos, inventários de fauna e flora e monitoramento": 17
- "Espécies ameaçadas e endêmicas: conservação aplicada": 16
- "Biologia molecular aplicada, bioética e biossegurança": 16

Já existem (não repita):
- Em uma amostra de solo, a equipe quer reconhecer a região diretamente influenciada pelas r
- A contagem de UFC em meio de cultura representa necessariamente todas as células microbian
- Um relatório descreve a passagem de amônio para nitrito e, em seguida, nitrato, sob dispon
- A mineralização do nitrogênio orgânico e sua imobilização na biomassa microbiana descrevem
- Dois solos apresentaram respiração basal diferente em ensaio laboratorial; o solo B libero
- Uma amostra composta de solo pode reduzir a variabilidade entre subpontos de área relativa
- Uma pesquisa detecta, por qPCR, um gene associado à transformação de nitrogênio em solo de
- O quociente metabólico do solo, tomado isoladamente, possui interpretação universal: quant
- Na investigação de poluição fecal em uma amostra de água superficial, qual organismo é ind
- A ausência de E. coli na alíquota analisada prova que a água está isenta de qualquer vírus
- Em um laboratório, o laudo descreve resultado de análise de água em NMP/100 mL. Que interp
- A sedimentação passiva de partículas em placa aberta permite obter, de forma direta e equi
- Um técnico observa floração de cianobactérias em um reservatório e afirma que a concentraç
- O Índice de Qualidade das Águas empregado pela ANA agrega diferentes parâmetros, porém seu
- A equipe de inspeção analisa um alimento e encontra baixa contagem de aeróbios mesófilos. 
- Um resultado molecular positivo para DNA bacteriano em alimento constitui prova suficiente
- Ao planejar coleta de solo para microbiologia em um local heterogêneo, qual medida auxilia
- O registro inequívoco da identificação da amostra, data, hora, local, responsável e condiç
- Um pesquisador extrai DNA de amostras ambientais e suspeita que parte do sinal encontrado 
- Quatro alíquotas retiradas do mesmo frasco de água tornam-se automaticamente quatro unidad
- Em relatório ambiental, um composto não foi detectado, embora o equipamento possua limite 
- Amostragem dirigida a pontos próximos de fonte suspeita de poluição autoriza, sem ponderaç
- Um estudo de água tratada emprega ensaio microbiológico sensível a desinfetante residual. 
- O limite de quantificação e o limite de detecção são obrigatoriamente idênticos para qualq
- Um órgão ambiental executa uma campanha única para registrar quais espécies vegetais estão
- Riqueza observada de espécies é a quantidade de espécies distintas registradas sob um esfo
- Duas áreas apresentam o mesmo número de espécies detectadas, mas na área X uma espécie dom
- O aumento do número de registros em armadilhas fotográficas, sem padronizar esforço, posiç
- Para acompanhar anfíbios vocalizantes, pesquisadores instalam gravadores em intervalos reg
- Em monitoramento por modelos de ocupação, a probabilidade de detecção pode ser tratada sep
- Um inventário utiliza parcelas permanentes para plantas, enquanto o relatório apresenta ap
- Uma curva de acumulação de espécies que aparente estabilização demonstra matematicamente q
- Uma planta ocorre naturalmente apenas em determinada cadeia de montanhas, sem outra área n
- Uma espécie endêmica pode não estar formalmente classificada como ameaçada, porque endemis
- Em consulta a cadastro de espécies, o pesquisador encontra a categoria DD (Dados Insuficie
- Registro voluntário no SISBio para material microbiológico coletado fora de unidade de con
- Uma equipe encontra espécime ameaçado em unidade de conservação federal e planeja coletar 
- O cumprimento de regras de coleta no SISBio e as obrigações relacionadas ao acesso ao patr
- Dois levantamentos em anos distintos detectaram menos indivíduos de ave ameaçada no segund
- A divulgação irrestrita de coordenadas de indivíduos de espécie sujeita à coleta ilegal é 
- Em PCR convencional, a função principal dos primers é:
- A simples amplificação por PCR de DNA previamente extraído de uma amostra ambiental não tr
- Um estudo necessita obter cDNA a partir de RNA e acompanhar a amplificação em tempo real. 
- O marcador 16S rRNA é frequentemente empregado em análises de comunidades procarióticas, e
- Em estudo de biodiversidade aquática, foi detectado DNA ambiental (eDNA) de peixe-alvo em 
- Uma bancada de fluxo laminar desenhada para proteger exclusivamente a amostra é, por defin
- Ao comparar metagenômica shotgun com sequenciamento de amplicons de 16S, qual afirmação es
- A seleção de nível de contenção biológica e medidas de proteção pode ser feita independent
```

## Pedido 26: Complexo industrial da saúde e ciência e tecnologia em saúde (`saude-publica.politicas-saude-ciencia-tecnologia`, 17 tópicos de edital)

```
Matéria: saude-publica.politicas-saude-ciencia-tecnologia (Complexo industrial da saúde e ciência e tecnologia em saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Complexo Econômico-Industrial da Saúde e estratégia nacional": 24
- "2. Produtos estratégicos, parcerias e instrumentos de desenvolvimento": 24
- "3. Política Nacional de Ciência, Tecnologia e Inovação em Saúde": 20
- "4. Pesquisa aplicada, avaliação e incorporação de tecnologias no SUS": 20
- "5. Governança, articulação institucional e análise de casos": 12

Já existem (não repita):
- Uma gestão de saúde pretende reduzir interrupções na oferta de vacinas ocasionadas pela de
- Um estudo descreve fábricas de reagentes, fabricantes de equipamentos médicos, empresas de
- Um dispositivo médico já é montado nacionalmente, mas depende de software de controle cuja
- Uma instituição pública e parceiros organizam transferência de conhecimento para fabricar 
- Um estudo de financiamento seleciona pesquisas segundo problemas sanitários prioritários i
- Uma entidade deseja desenvolver localmente tecnologia digital inovadora para apoiar diagnó
- Um fabricante obteve registro sanitário de dispositivo na Anvisa e afirma que o SUS deverá
- Um grupo deseja mapear os temas de investigação que precisam de fomento para enfrentar ini
- O gestor reúne projetos clínicos para recomendar incorporação ou exclusão de tecnologia, c
- Um plano setorial de 2023 apresenta PDP, PDIL, PPVACSH, PPDN, PMIA e PDCEIS. A caracteriza
- Em proposta de PDP para produto biotecnológico, a instituição responsável pela absorção da
- Uma política científica define prioridades de doenças negligenciadas e, depois, realiza se
- Uma proposta de PDIL pretende formar parceria apenas entre duas empresas privadas sem ICT 
- Um processo administrativo de incorporação de tecnologia tramita regularmente. Qual prazo 
- Em avaliação, uma nova tecnologia custa 160 unidades e produz sete unidades de resultado, 
- Uma pessoa jurídica produz insumos e pretende atuar como Empresa Estratégica de Saúde por 
- Um projeto produtivo informou número de laboratórios reformados e volume de recursos gasto
- Um produto é desenvolvido por PDIL e ainda não foi incorporado ao SUS, embora o fornecedor
- Uma licitação pretende aplicar margem de preferência a produto nacional manufaturado por E
- Uma instituição afirma que a prioridade concedida às EES na tramitação regulatória permite
- Uma pesquisa de saúde social investiga como transporte precário afeta o acesso ao tratamen
- Uma PDP foi concluída contratualmente, mas a instituição receptora não demonstra fabricaçã
- Uma nova tecnologia foi incorporada ao SUS por decisão publicada. Um gestor acrescenta o p
- Um edital de fomento pretende reservar todos os recursos a projetos de aplicação imediata,
- Uma política financia criação de tecnologia avançada de triagem, mas seu uso depende de co
- Em reunião de planejamento, uma equipe afirma que o CEIS engloba medicamentos, dispositivo
- Um parecer de 2026 afirma que a única base normativa da estratégia nacional do CEIS perman
- Um projeto de parceria busca transferir e absorver tecnologia de produção de medicamento e
- Durante seleção científica, afirma-se que a PNCTIS contempla somente pesquisa biomédica ex
- Um medicamento obteve autorização sanitária de comercialização, mas ainda depende de avali
- Ao classificar projetos científicos, um gestor afirma que a seletividade da PNCTIS corresp
- Um projeto produtivo apresenta somente a planta física adquirida, sem dados sobre absorção
- Segundo a Lei nº 15.471/2026, o simples fato de uma empresa vender produtos ao SUS é sufic
- Um projeto de PDIL corretamente estruturado inclui empresa estratégica de saúde e institui
- Uma equipe analisa somente o custo de compra de novo exame e decide incorporá-lo, sem exam
- Ao avaliar demanda tecnológica, uma equipe utiliza a Matriz de Desafios Produtivos e Tecno
- Uma proposta de desenvolvimento sob risco tecnológico é descrita como encomenda tecnológic
- O documento da PNCTIS incentiva tanto o fomento induzido por necessidades do sistema quant
- No art. 7º da Lei nº 15.471/2026, o rol de instrumentos de parceria contém exatamente os s
- Uma PDP envolve produto biotecnológico e prevê transferência de processo, incluindo acesso
- Um produto oriundo do PDIL não foi incorporado ao SUS, mas a instituição afirma que o art.
- Uma instituição pretende aplicar margem de preferência em hipótese do art. 27, §2º, da Lei
- A PNCTIS publicada em edição de 2016 cita atos de ética em pesquisa anteriores. Um leitor 
- Uma decisão favorável de incorporação foi publicada após processo com análise técnica. O p
- Um projeto de produção local apresenta compra de equipamentos e número de contratos celebr
```

## Pedido 27: Geometria espacial e posições relativas (`matematica.geometria-espacial-posicao`, 17 tópicos de edital)

```
Matéria: matematica.geometria-espacial-posicao (Geometria espacial e posições relativas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Pontos, retas, planos e determinação": 20
- "2. Posições relativas e paralelismo": 20
- "3. Perpendicularidade, ângulos e projeções": 20
- "4. Como o tema costuma ser cobrado": 20
- "5. Estratégias de análise e demonstração": 20

Já existem (não repita):
- Quantos pontos distintos determinam uma reta única?
- Qual condição faz três pontos distintos determinarem um plano único?
- Dois planos distintos e secantes têm como interseção qual objeto?
- Retas não coplanares e sem ponto comum são classificadas como?
- Uma reta contém dois pontos distintos de um plano. Qual relação está garantida?
- Qual é o critério prático para provar que uma reta é perpendicular a um plano?
- A reta ℓ não está contida no plano α e é paralela a uma reta pertencente a α. Qual relação
- Como se mede o ângulo entre uma reta e um plano?
- Qual é a distância de um ponto exterior a um plano?
- Dois planos distintos α e β não possuem ponto comum. Como se classificam?
- Uma reta cruza outra no desenho projetado, sem dados de altura ou coplanaridade. O que se 
- Dois planos são perpendiculares. Qual é a medida do ângulo diedro entre eles?
- Dois planos secantes α e β têm reta comum r. Um plano γ paralelo a r corta α e β em retas 
- A projeção ortogonal de uma reta perpendicular a um plano é qual objeto?
- Qual configuração determina um plano único além de três pontos não colineares?
- Em um cubo, diagonais de faces opostas pertencem necessariamente ao mesmo plano?
- Se dois planos paralelos distintos são cortados por um mesmo plano secante, como são as re
- Se uma reta é perpendicular a apenas uma reta do plano no ponto de encontro, isso basta pa
- Retas reversas não se encontram e não são coplanares.
- Dois planos secantes distintos compartilham exatamente uma reta.
- A projeção ortogonal de um ponto sobre um plano é o pé da perpendicular traçada do ponto a
- Três pontos colineares determinam um plano único.
- Retas distintas sem ponto comum são sempre paralelas.
- Uma reta contida em um plano é chamada de reta paralela externa a esse plano.
- Duas retas concorrentes distintas determinam um plano.
- Dois planos paralelos distintos não têm pontos em comum.
- O ângulo entre uma reta e um plano pode ser obtido pela projeção ortogonal da reta no plan
- O ângulo diedro entre planos é medido por uma seção perpendicular à reta de interseção.
- Retas coplanares distintas sem ponto comum são paralelas.
- Se dois pontos distintos de uma reta pertencem a um plano, a reta inteira está contida nes
- Uma figura em perspectiva que parece mostrar duas retas cruzadas prova que elas têm um pon
- A perpendicularidade de uma reta a um plano pode ser provada verificando duas retas concor
- A distância entre dois planos paralelos é constante e medida por um segmento perpendicular
- Dois planos perpendiculares se encontram num único ponto isolado.
- Duas retas paralelas distintas determinam um plano único.
- A distância entre retas reversas é o comprimento da perpendicular comum às duas.
- A projeção ortogonal de uma reta perpendicular ao plano é um único ponto.
- Uma reta paralela externa a um plano não tem ponto comum com ele, enquanto uma reta contid
- Se um plano contém uma reta perpendicular a outro plano, então os dois planos são perpendi
- Dois planos secantes compartilham uma reta, e cada ponto dessa reta pertence aos dois plan
- Uma reta reversa a outra é paralela a ela porque não existe ponto de interseção.
- Uma reta contida num plano forma ângulo zero com esse plano segundo a definição pelo menor
```

## Pedido 28: Economia urbana e avaliação econômica de políticas públicas (`economia.economia-urbana-avaliacao-politicas`, 17 tópicos de edital)

```
Matéria: economia.economia-urbana-avaliacao-politicas (Economia urbana e avaliação econômica de políticas públicas)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da economia urbana e regional": 17
- "Mercado imobiliário, uso do solo e economia da habitação": 17
- "Políticas públicas: teoria da mudança, monitoramento e impacto causal": 17
- "Avaliação econômica: custo-benefício, custo-efetividade e risco": 17
- "Indicadores e bases públicas: tratamento e análise territorial": 16
- "Aplicações socioambientais, estudos econômicos e governança da avaliação": 16

Já existem (não repita):
- Um estudo identifica empresas do mesmo setor concentradas numa zona industrial e compartil
- Para estudar fluxo de pacientes entre municípios e a centralidade regional de serviços, qu
- Dois bairros têm velocidades viárias semelhantes, mas o primeiro alcança mais empregos em 
- Em análise municipal, o PIB per capita de A supera o de B. Qual interpretação é correta?
- Um imóvel próximo à estação tem valor mais alto que outro similar, mas a estação foi inaug
- Uma política aumenta potencial construtivo perto do metrô. Qual enunciado considera adequa
- Uma família considera duas moradias: A custa R$ 1.000 de aluguel e R$ 500 de transporte; B
- Após requalificação de uma área, aumentam aluguéis e saídas de antigos locatários. Qual in
- Uma prefeitura informa que 3.000 alunos concluíram um curso de qualificação. Qual classifi
- No período anterior, tratado e controle apresentavam 40 e 50 pontos; após a política, 60 e
- Uma bolsa é atribuída a alunos com índice de vulnerabilidade acima de 70 pontos. Com dados
- Um programa foi implantado em diferentes municípios em anos distintos, com efeitos variáve
- Uma ação tem custo de R$ 100 no início e entrega benefício líquido de R$ 110 um ano depois
- Ao comparar políticas de redução de emissões, benefícios globais são difíceis de monetizar
- Uma obra já teve a economia de tempo dos usuários monetizada como benefício social. Ao inc
- Uma política custa R$ 2 milhões e previne 100 ocorrências; alternativa B custa R$ 3 milhõe
- Um pesquisador quer tabular renda e características populacionais usando estatísticas ofic
- Um relatório usa o saldo mensal do Novo Caged como se representasse o número total de trab
- Um mapa apresenta bairros com alta taxa de evasão escolar concentrados espacialmente. Qual
- Na junção de microdados de pessoas com indicadores municipais de anos diferentes, qual che
- Na estrutura do CMAP, que associação está correta?
- Ao priorizar políticas para avaliação no âmbito do CMAP, qual tríade corresponde à metodol
- Um relatório de auditoria conclui que a contratação de uma política cumpriu exigências de 
- Economias de aglomeração podem coexistir com congestionamento, poluição e elevação dos cus
- A pesquisa REGIC do IBGE define hierarquia urbana exclusivamente pela população absoluta d
- A análise de uma região funcional pode incluir municípios distintos quando os fluxos regul
- O preço dos terrenos pode refletir acessibilidade, infraestrutura, amenidades e regras de 
- A expansão de potencial construtivo assegura, em qualquer horizonte e território, redução 
- Em modelos hedônicos, uma associação entre proximidade ao metrô e maior preço imobiliário 
- O monitoramento de uma política pode indicar volume de atendimentos realizados sem, por is
- Em diferenças em diferenças, a condição de tendências paralelas diz respeito ao comportame
- Pareamento por escore de propensão elimina, por construção matemática, o viés causado por 
- Uma taxa de desconto mais elevada tende a reduzir o valor presente de benefícios positivos
- Se duas alternativas entregam o mesmo tipo de resultado mensurável, custo-efetividade pode
- O VPL social positivo comprova, por si só, que todos os grupos sociais receberão benefício
- A PNAD Contínua é uma pesquisa amostral, e estimativas extraídas de seus microdados devem 
- Um índice global de autocorrelação espacial significativo, isoladamente, demonstra que o r
- Ao comparar taxas por bairro, diferenças no tamanho dos denominadores e mudanças de delimi
- No CMAP, o Comitê de Monitoramento e Avaliação de Gastos Diretos (CMAG) se ocupa de políti
- Na avaliação de mobilidade urbana, o número de quilômetros de corredor implantado é indica
- Verificar regularidade do gasto e estimar o efeito causal de uma política são perguntas di
```

## Pedido 29: Polímeros e ciência dos materiais (`engenharia.polimeros-materiais`, 17 tópicos de edital)

```
Matéria: engenharia.polimeros-materiais (Polímeros e ciência dos materiais)
Capítulos e quantas questões quero em cada um (total 100):
- "Estrutura, classificação e arquitetura de polímeros": 17
- "Polimerização, famílias e relações estrutura–propriedade": 17
- "Propriedades térmicas, mecânicas e reologia": 17
- "Processamento: extrusão, injeção e sopro": 17
- "Cerâmicos, compósitos e ensaios de caracterização": 16
- "Degradação, sustentabilidade e reciclagem de materiais": 16

Já existem (não repita):
- Uma fábrica procura uma resina que possa ser fundida e remoldada repetidamente em condiçõe
- Qual propriedade define a temperatura de fusão Tm apresentada em um termoplástico semicris
- Dentre os materiais abaixo, qual é exemplo típico de polímero natural?
- Em uma extrusora convencional de termoplásticos, qual elemento define majoritariamente a s
- No compósito de fibras de vidro com matriz polimérica, qual é a função típica da matriz?
- Qual método térmico é particularmente apropriado para acompanhar perda de massa por volati
- Uma indústria deseja fabricar recipientes ocos por extrusão-sopro. Qual componente é inici
- Um material apresenta regiões cristalinas e amorfas em sua estrutura. A denominação tecnic
- Na polimerização em etapas, em uma mistura bifuncional ideal e estequiometricamente balanc
- Qual comparação entre reciclagem mecânica e despolimerização é mais correta?
- Em um ensaio de tração, atuam 600 N sobre seção transversal inicial de 30 mm². Qual a tens
- O que caracteriza especificamente o processo de injeção-sopro, em contraste com extrusão-s
- Uma resina epóxi curada recebe aquecimento superior à temperatura de serviço. Qual resulta
- Qual técnica é especialmente apropriada para avaliar transição vítrea por variação de flux
- Uma placa compósita com fibras unidirecionais tende a apresentar qual comportamento?
- Qual associação entre material e grupo funcional do esqueleto polimérico está correta?
- Em condições idealizadas, uma amostra apresenta Mn = 80 kg/mol e Mw = 200 kg/mol. A disper
- Para um compósito unidirecional ideal com Vf = 0,60, Ef = 70 GPa e Em = 3 GPa, qual é o mó
- Durante armazenamento sob deformação mantida, a tensão requerida para conservar a forma de
- Qual afirmação a respeito de polímeros de fonte renovável e biodegradação é tecnicamente d
- Sobre crescimento em etapas e policondensação, qual alternativa é a mais rigorosa?
- Ao comparar o desempenho de corpos de prova injetados, qual combinação de fatores deve ser
- Em copolímeros, a presença de dois tipos de unidade repetitiva obriga que sua distribuição
- A celulose e o amido são exemplos de polímeros naturais, embora possuam estruturas e propr
- A extrusão de termoplásticos é empregada na obtenção contínua de tubos, chapas e perfis.
- Em qualquer formulação, as resinas epóxi curadas se fundem como termoplásticos comuns e vo
- A análise termogravimétrica acompanha a variação de massa de uma amostra submetida a um pr
- A sinterização cerâmica pressupõe necessariamente fundir integralmente todas as partículas
- Recuperar calor pela combustão de resíduos plásticos é a mesma operação que reciclar mecan
- O PET contém ligações éster na cadeia, enquanto as poliamidas possuem ligações amida.
- O módulo de Young e a tensão máxima de tração são denominações equivalentes para a mesma g
- No extrusão-sopro, a etapa imediatamente anterior à inflação caracteriza-se pela preparaçã
- Quanto maior a fração de fibras num compósito, maior será invariavelmente toda propriedade
- Uma blenda polimérica requer, por definição, que se forme uma nova cadeia copolimérica por
- Tg e Tm são grandezas idênticas: todo polímero apresenta uma única temperatura em que a fa
- A fórmula idealizada de Carothers, DPn = 1/(1−p), pressupõe condições específicas e não de
- Para fibras perfeitamente alinhadas no sentido da carga, a regra das misturas longitudinal
- Por serem termoplásticos, PE e PP podem ser misturados sem consequências para a compatibil
- Durante ensaio de fluência, a tensão é mantida aproximadamente constante e acompanha-se a 
- O código de identificação da resina, isoladamente, comprova que uma embalagem será coletad
```

## Pedido 30: Lei 6.858/1980: valores não recebidos em vida (`legislacao.lei-6858-1980`, 17 tópicos de edital)

```
Matéria: legislacao.lei-6858-1980 (Lei 6.858/1980: valores não recebidos em vida)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Objeto da Lei nº 6.858/1980, valores alcançados e finalidade": 12
- "2. Dependentes habilitados, comprovação e quotas iguais": 12
- "3. Sucessores civis, alvará judicial e dispensa de inventário": 52
- "4. Quotas de menores: poupança, idade e autorização judicial": 12
- "5. FGTS, cotas históricas PIS-PASEP e casos de fronteira": 12

Já existem (não repita):
- Uma pessoa faleceu sem receber R$ 6.000 de salários já devidos por seu empregador. Qual di
- Uma conta de FGTS pertence a trabalhador falecido. Há dois dependentes regularmente habili
- Após a morte de um empregado, o responsável por pagar valor residual necessita comprovar q
- Não existem dependentes habilitados para receber saldo do FGTS deixado por pessoa falecida
- Um dependente de 15 anos possui quota de R$ 4.000 relativa a crédito recebido pela Lei 6.8
- Uma trabalhadora deixou R$ 15.000 de verbas devidas e três dependentes habilitados. Como d
- Uma pessoa afirma que o CPC de 2015 revogou a dispensa de inventário para os valores previ
- Uma família pergunta qual item abaixo é especificamente previsto no art. 1º da Lei 6.858/1
- Um herdeiro civil pretende requerer alvará para salário não recebido por pessoa falecida. 
- Um órgão pagador recebe certidão de dependentes que contém somente prenomes, sem dados com
- Um órgão público deve remuneração de cargo a servidor falecido, não recebida em vida. Como
- Dependente de 14 anos possui quota depositada em poupança. Sua família busca levantamento 
- Há dois dependentes habilitados, um de 17 anos e outro adulto, para crédito de R$ 8.000. Q
- Uma pessoa pede que o alvará para FGTS seja negado por falta de processo de inventário for
- Uma consulta sobre o FGTS de trabalhador falecido procura o dispositivo que repete a prior
- Um valor foi recebido pela via simplificada do art. 1º e o credor exige prova de que não h
- Pessoa falecida deixou saldo de FGTS e duas filhas: uma é dependente habilitada, a outra é
- Uma quota protegida pertence a menor de 13 anos, cuja família demonstra despesas imprescin
- Uma trabalhadora falece deixando saldo de FGTS, e após verificação não se identificam depe
- Uma pessoa deixou FGTS de R$ 24.000 e um automóvel. Há um dependente habilitado e um suces
- Herdeira pesquisa em 2026 saldos de cotas do antigo Fundo PIS-PASEP do pai falecido e é in
- Em levantamento de crédito trabalhista, há notícia de dependente já habilitado à pensão po
- Uma família procura dispositivo sobre parcelas de benefício previdenciário que segurado nã
- Ao ler literalmente o art. 1º, § 2º, da Lei 6.858/1980, uma pessoa deve identificar o dest
- Um candidato confunde o prazo de cinco anos do art. 121 do ADCT, relativo ao ressarcimento
- Uma empregada morreu deixando verbas salariais vencidas e não recebidas; a Lei 6.858/1980 
- Quando existem dependentes previdenciários habilitados, os sucessores civis não habilitado
- A condição de dependente habilitado pode ser declarada por documento fornecido pela Previd
- Na ausência de dependentes habilitados, os sucessores civis podem levantar os valores prev
- A quota de dependente menor de 18 anos deve ser depositada em caderneta de poupança, com j
- Todo saldo de FGTS deixado pelo trabalhador falecido precisa aguardar inventário obrigatór
- O art. 666 do CPC de 2015 confirma que o pagamento dos valores previstos na Lei 6.858/1980
- A declaração de habilitação prevista no Decreto 85.845/1981 pode omitir filiação e data de
- Com a apresentação da declaração de dependentes habilitados prevista no art. 2º do Decreto
- O Decreto 85.845/1981 exclui valores devidos em razão de cargo ou emprego público, restrin
- Para calcular o pagamento de R$ 21.000 a três dependentes habilitados, aplica-se o critéri
- As exceções do art. 1º, § 1º, permitem ao responsável legal sacar a quota do menor para qu
- A conta individual de FGTS e o salário residual de trabalhador falecido estão entre os val
- O limite histórico de 500 OTN, mencionado no art. 2º para determinadas contas, restringe i
- Uma pessoa se apresenta como herdeira civil sem habilitação, enquanto há dependente habili
- A falta de apresentação imediata da declaração previdenciária equivale necessariamente à i
- No texto do art. 1º, § 2º, a reversão dos valores aos fundos somente é prevista quando não
- Em 2026, o antigo Fundo PIS-PASEP mantém intacta a mesma estrutura patrimonial de 1980; po
- O prazo de cinco anos previsto no art. 121 do ADCT refere-se ao ressarcimento de contas de
- A quota destinada a menor habilitado pode ser levantada antecipadamente para custear despe
```

## Pedido 31: Biomedicina: citologia, histotecnologia e áreas de atuação (`biomedicina.citologia-histotecnologia-imagem`, 17 tópicos de edital)

```
Matéria: biomedicina.citologia-histotecnologia-imagem (Biomedicina: citologia, histotecnologia e áreas de atuação)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da Biomedicina e citologia clínica": 17
- "Papanicolaou, Bethesda e rastreamento cervical": 17
- "Histotecnologia, biópsias e métodos teciduais": 17
- "Biologia molecular aplicada ao diagnóstico": 17
- "Imagenologia, equipamentos e segurança": 16
- "Biomedicina estética, legislação e ética profissional": 16

Já existem (não repita):
- Em um laboratório, uma amostra é obtida por escovado e distribuída em lâmina para avaliar 
- O material de uma biópsia precisa demonstrar organização do epitélio e relação com o estro
- Em citologia cervical, o observador identifica células escamosas com halo perinuclear e at
- Um laboratório dispõe de sedimento de derrame pleural e pretende realizar cortes seriados 
- Ao comparar tipos celulares no esfregaço cervical, qual achado favorece a identificação de
- Na terminologia Bethesda para citologia cervical, qual sigla significa lesão intraepitelia
- A categoria ASC-H deve ser interpretada como:
- Uma pessoa apresenta teste de DNA-HPV oncogênico positivo em rastreamento. Qual conclusão 
- Uma lâmina cervical é satisfatória para avaliação, mas não apresenta componente da zona de
- Considerando a atualização brasileira de 2025 para rastreamento cervical organizado, assin
- Na rotina histológica com hematoxilina e eosina, qual aspecto é esperado?
- Qual sequência descreve corretamente o processamento histológico clássico após a fixação p
- Biomédico registrado com habilitação apenas em histotecnologia prepara cortes de biópsia e
- Qual evento representa um risco de contaminação cruzada no laboratório histotécnico?
- Um laboratório realizou imuno-histoquímica em cortes teciduais, mas o controle positivo da
- Em biologia molecular, qual descrição corresponde corretamente à RT-PCR clássica?
- Uma equipe compara dois biomarcadores em oncologia: um informa risco de progressão, outro 
- Uma amostra submetida à qPCR amplificou o alvo, porém o controle negativo sem molde também
- A comparação de valores Ct/Cq entre testes moleculares diferentes exige cautela porque:
- Um laudo de sequenciamento identifica uma variante genética de significado incerto (VUS). 
- Qual modalidade de imagem emprega ondas mecânicas de alta frequência?
- Qual associação de modalidade e risco está correta?
- Segundo a regulamentação biomédica e o Decreto nº 88.439/1983, um biomédico habilitado ope
- Qual enunciado melhor explica o princípio de otimização da proteção radiológica em exames 
- Em 2026, uma questão afirma que a Resolução CFBM nº 241/2014, por si só, concede autorizaç
- Qual documento estabelece o Código de Ética da Profissão de Biomédico consultado para esta
- Uma clínica divulga “procedimento 100% seguro e resultado garantido”, omitindo eventos adv
- Um biomédico com habilitação em análises moleculares quer firmar laudos de testes genético
- Um laboratório pretende compartilhar laudos genéticos identificados em uma pasta pública n
- Sobre habilitação profissional, assinale a afirmativa correta para a atuação em distintas 
- A citologia esfoliativa examina células coletadas por descamação, raspado ou escovado, sem
- O cell block de um derrame pleural é sempre equivalente a uma biópsia de tecido íntegro e 
- Em citopatologia cervical, vacuolização citoplasmática isolada, sem alterações nucleares c
- Na nomenclatura Bethesda, HSIL se refere a lesão intraepitelial escamosa de alto grau, ao 
- Um laudo NILM pode mencionar inflamação ou alterações celulares reativas sem deixar de ser
- A aprovação das diretrizes brasileiras de 2025 aboliu a citologia cervical e tornou o test
- A fixação de material histológico visa preservar componentes teciduais e reduzir autólise 
- A desidratação é a etapa em que a parafina retira fisicamente todo o álcool do tecido, sen
- A Resolução CFBM nº 391/2025 admite atribuições histotécnicas e técnicas de imuno-histoquí
- Na PCR convencional, os ciclos de desnaturação, anelamento e extensão permitem amplificar 
- A expressão RT-PCR significa necessariamente PCR em tempo real, ainda que o ensaio não env
- Uma variante classificada como de significado incerto pode ser interpretada como causal se
- Tomografia computadorizada e radiografia utilizam raios X, ao passo que a ressonância magn
- Um sistema PACS é utilizado para organizar, armazenar e distribuir imagens médicas, mas nã
- O biomédico habilitado que realiza pós-processamento técnico de imagens radiográficas adqu
- Dados genéticos e informações identificáveis sobre saúde exigem confidencialidade e proteç
- O registro no Conselho Regional de Biomedicina, sem habilitação correlata, autoriza automa
- A controvérsia sobre a Resolução CFBM nº 241/2014 exige distinguir a decisão noticiada pel
```

## Pedido 32: Lei 12.086/2009: Promoções e Carreira dos Militares da PMDF e do CBMDF (`legislacao.militares-df-lei-12086`, 17 tópicos de edital)

```
Matéria: legislacao.militares-df-lei-12086 (Lei 12.086/2009: Promoções e Carreira dos Militares da PMDF e do CBMDF)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Objeto, efetivo e conceitos de promoção": 20
- "2. Atos, vagas, antiguidade e merecimento": 20
- "3. Inclusão nos Quadros e condições de acesso": 20
- "4. Quadro de Acesso, merecimento, comissões e recursos": 20
- "5. Título II — CBMDF: o que muda em relação à PMDF": 20

Já existem (não repita):
- Segundo a Lei 12.086/2009, NÃO são considerados no limite do efetivo fixado para a PMDF:
- Na PMDF, interstício é o tempo mínimo que cada policial militar deve cumprir no posto ou g
- No âmbito da PMDF, as promoções ocorrem pelos critérios de:
- A solicitação de promoção por ato de bravura na PMDF pode ser feita pelo próprio interessa
- O policial militar promovido por ato de bravura que não consegue satisfazer as condições e
- A promoção por ato de bravura exclui, em caso de falecimento, a promoção post mortem que r
- O policial militar da PMDF será ressarcido de preterição, entre outras hipóteses, quando:
- As promoções post mortem, por ato de bravura e em ressarcimento de preterição ocorrem apen
- Sobre os atos de promoção na PMDF, assinale a correta.
- Na PMDF, a promoção por merecimento é aplicada exclusivamente para o acesso ao último post
- São fontes de vagas para as promoções na PMDF, EXCETO:
- Feita a apuração das vagas a preencher para uma data de promoção na PMDF, esse número não 
- O policial militar da PMDF agregado por motivo diverso do desempenho de cargo de natureza 
- Não pode constar em Quadro de Acesso da PMDF o policial militar que:
- O policial militar considerado provisoriamente não habilitado para o acesso, por ser presu
- As promoções na PMDF são efetuadas anualmente nos dias:
- Integram os Quadros da PMDF previstos na Lei 12.086/2009, entre outros, o Quadro de Oficia
- Para a inclusão nos Quadros de Oficiais Administrativos, Especialistas e Músicos da PMDF, 
- As vagas para o Curso de Habilitação de Oficiais Administrativos, Especialistas e Músicos 
- Para ser confirmado na graduação de Soldado, mediante promoção a Soldado PM de 1ª Classe, 
- O Aspirante-a-Oficial do QOPM será promovido a Segundo-Tenente na primeira data de promoçã
- O candidato ao QOPMS ou ao QOPMC que não concluir com aproveitamento o curso inicial de ca
- Na PMDF, o Curso de Altos Estudos para Praças é exigido para o acesso à graduação de:
- Na impossibilidade de o policial militar realizar o teste de aptidão física por motivo de 
- O curso de especialização ou habilitação exigido a cada período de cinco anos, como condiç
- Os limites quantitativos de antiguidade da PMDF definem que concorrem às promoções:
- Não podem constar no Quadro de Acesso por Merecimento da PMDF os Oficiais que estiverem no
- Na promoção por merecimento da PMDF, para a primeira vaga, será selecionado:
- O Oficial que constar do Quadro de Acesso por Merecimento em primeiro lugar em três datas 
- A Comissão de Promoção de Praças da PMDF é presidida:
- A Comissão de Promoção de Oficiais da PMDF tem como membros efetivos três coronéis designa
- O policial militar da PMDF que se julgar prejudicado na composição do Quadro de Acesso pod
- A progressão funcional do policial militar do Distrito Federal continua durante a inativid
- Com a redação dada pela Lei 12.086/2009 à organização básica, o cargo de Comandante-Geral 
- A PMDF é organizada e mantida pela União, subordinada ao Governador do Distrito Federal e 
- As promoções das Praças do Corpo de Bombeiros Militar do Distrito Federal são efetuadas no
- No CBMDF, a promoção post mortem não resulta em ocupação de vaga e pode ocorrer a qualquer
- Na avaliação de desempenho para a promoção por merecimento no CBMDF, a comparação entre os
- Entre as condições básicas para promoção no CBMDF está obter aproveitamento mínimo de sete
- O limite quantitativo de antiguidade geral para as promoções no CBMDF é de:
- No CBMDF, o recurso referente à composição do Quadro de Acesso deve ser solucionado em até
```

## Pedido 33: Código de Conduta Ética e Integridade dos Correios (2021) (`etica.codigo-conduta-correios`, 17 tópicos de edital)

```
Matéria: etica.codigo-conduta-correios (Código de Conduta Ética e Integridade dos Correios (2021))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Introdução, abrangência e identidade corporativa": 25
- "2. Princípios éticos e compromissos da empresa com os empregados": 25
- "3. Compromissos dos empregados": 25
- "4. Relações externas, Comissão de Ética, brindes e denúncias": 25

Já existem (não repita):
- O Código de Conduta Ética e Integridade dos Correios foi aprovado pelo Conselho de Adminis
- Segundo o Código, sua finalidade principal é:
- O Código de Conduta dos Correios aplica-se apenas aos empregados concursados, não alcançan
- Os editais de licitação e os contratos administrativos dos Correios devem conter cláusula 
- De acordo com o Código, os agentes por ele abrangidos devem observar também, no que couber
- Entre as leis que fundamentam o Código estão a Lei de Improbidade Administrativa, a Lei An
- O negócio dos Correios, conforme o Código, é definido como:
- A visão dos Correios é ser referência no ecossistema global de logística, com soluções de 
- NÃO figura entre os valores dos Correios listados no Código:
- O princípio da impessoalidade, no Código dos Correios, significa:
- O princípio da integridade envolve honestidade, probidade, coerência entre discurso e prát
- Pelo princípio da transparência, os Correios devem divulgar todas as informações internas,
- O princípio que trata da atuação com responsabilidade ambiental, econômica, social e cultu
- Entre os compromissos dos Correios com seus empregados está garantir canais formais de com
- Para a ascensão profissional, o Código determina que os Correios privilegiem o critério:
- Os Correios comprometem-se a respeitar a liberdade de associação sindical e a manter diálo
- A divulgação de informações aos empregados, segundo o Código, deve limitar-se ao estritame
- É vedado ao empregado dos Correios utilizar informação privilegiada obtida no exercício pr
- A prática de "mesa e tela limpas", prevista no Código, tem por objetivo:
- Ao tomar conhecimento de incidente que possa violar a privacidade de dados pessoais, o emp
- O empregado pode publicar livremente, em ambientes externos, estudos e pareceres de sua au
- No exercício do direito de greve, o Código determina que o empregado preserve o patrimônio
- Diante de ato irregular praticado por superior hierárquico, o Código orienta o empregado a
- O empregado deve eximir-se de atividades que caracterizem conflito de interesses e comunic
- Quanto ao sigilo profissional, o Código determina que o empregado:
- O empregado pode manter relações comerciais com empresas concorrentes dos Correios, desde 
- Aos atos de solicitar, receber, oferecer ou dar vantagem indevida previstos no Código pode
- Os Correios comprometem-se a contratar fornecedores e parceiros com base em critérios econ
- Nas relações concorrenciais, os Correios buscam evitar, entre outras condutas:
- A penalidade que a Comissão de Ética pode aplicar ao autor de infração ética é a:
- Além da censura, a Comissão de Ética pode lavrar Acordo de Conduta Pessoal e Profissional 
- A Norma de Funcionamento que regula a atividade da Comissão de Ética dos Correios é aprova
- A Comissão de Ética pode apurar condutas em desacordo com as normas éticas somente mediant
- O treinamento sobre o Código de Conduta Ética e Integridade para empregados e administrado
- Segundo o Código, é permitida a aceitação de brinde que:
- Brindes que ultrapassem o valor unitário de R$ 100,00 serão incorporados ao patrimônio dos
- O órgão dos Correios responsável por receber as denúncias e manifestações das partes inter
- O Código garante sigilo, confidencialidade e proteção institucional ao denunciante de boa-
- Diante de situação não contemplada no Código, o empregado deve buscar orientação no Regula
- Nas relações com os clientes, os Correios comprometem-se a responder às solicitações de in
```

## Pedido 34: Igualdade Racial na Bahia: Constituição, Estatuto e SEPROMI (`legislacao.igualdade-racial-bahia`, 17 tópicos de edital)

```
Matéria: legislacao.igualdade-racial-bahia (Igualdade Racial na Bahia: Constituição, Estatuto e SEPROMI)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constituição da Bahia: Capítulo XXIII, Do Negro": 20
- "2. Estatuto Estadual: objetivos, definições e sistema": 20
- "3. Saúde, educação, cultura e terra": 20
- "4. Trabalho, racismo institucional, comunicação, mulheres e juventude": 20
- "5. Justiça, segurança pública e liberdade religiosa": 20

Já existem (não repita):
- Segundo o art. 286 da Constituição do Estado da Bahia, a prática do racismo constitui:
- O art. 286 da Constituição da Bahia afirma que a sociedade baiana é cultural e historicame
- Com países que mantiverem política oficial de discriminação racial, a Constituição da Bahi
- Pela Constituição da Bahia, os cursos de formação e aperfeiçoamento do servidor público ci
- Segundo a Constituição da Bahia, sempre que for veiculada publicidade estadual:
- A Constituição da Bahia estabelece o dia 13 de maio, data da Lei Áurea, como Dia da Consci
- O Capítulo XXIII da Constituição da Bahia, intitulado "Do Negro", compreende os artigos:
- O Estatuto da Igualdade Racial e de Combate à Intolerância Religiosa do Estado da Bahia fo
- Para o Estatuto da Igualdade Racial da Bahia, população negra é:
- O Sistema Estadual de Promoção da Igualdade Racial (SISEPIR) é coordenado:
- O Conselho para o Desenvolvimento da Comunidade Negra (CDCN) integra o SISEPIR como órgão 
- Pela Lei nº 13.182/2014, os municípios baianos são obrigados a integrar o SISEPIR.
- A Ouvidoria de Promoção da Igualdade Racial, instituída pelo Estatuto baiano, está vincula
- Pela Lei nº 14.521/2022, a Secretaria de Promoção da Igualdade Racial da Bahia passou a de
- A SEPROMI foi criada pela Lei nº 10.549/2006, que reorganizou a Administração estadual bai
- Segundo o Estatuto da Igualdade Racial da Bahia, alunos adeptos de religiões afro-brasilei
- O Estatuto baiano insere no calendário escolar o mês de agosto, em memória da Revolta dos 
- Nas informações dos órgãos estaduais de saúde, o Estatuto da Igualdade Racial da Bahia det
- Sobre a capoeira, o Estatuto da Igualdade Racial da Bahia dispõe que:
- O programa reconhecido pelo Estatuto baiano para apoiar blocos e agremiações de matriz afr
- O Estatuto prevê a possibilidade de consulta prévia, livre e informada aos povos e comunid
- O Estatuto da Igualdade Racial da Bahia institui reserva de vagas para a população negra n
- As ações afirmativas de reserva de vagas previstas no Estatuto baiano tinham vigência de d
- O quesito "raça/cor" constará obrigatoriamente dos cadastros de servidores públicos estadu
- No combate ao racismo institucional, o Estatuto da Igualdade Racial da Bahia prevê que:
- O Estatuto baiano veda a exposição da imagem de pessoas custodiadas em estabelecimentos pr
- Quanto à juventude negra, o Estatuto da Igualdade Racial da Bahia assegura:
- O Estatuto da Igualdade Racial da Bahia determina a criação, na estrutura da Polícia Civil
- Pelo Estatuto baiano, o Estado manterá registro e monitoramento das ações de policiamento 
- Segundo o Estatuto, o procedimento unificado para registro e investigação dos crimes de ra
- O Centro de Referência de Combate ao Racismo e à Intolerância Religiosa, reconhecido pelo 
- A Rede de Combate ao Racismo e à Intolerância Religiosa, instituída pelo Estatuto baiano, 
- O Estatuto assegura o acesso de adeptos de religiões afro-brasileiras a estabelecimentos e
- O Estatuto da Igualdade Racial da Bahia determina que o Estado estimule a Defensoria Públi
- Assinale a alternativa que associa corretamente o tema à norma baiana que o disciplina.
- Segundo o Estatuto da Igualdade Racial da Bahia, as ações afirmativas devem observar, entr
- O Estatuto baiano garante o reconhecimento, como patrimônio histórico e cultural, das mani
- Nas contratações de obras, produtos e serviços, o Estatuto da Igualdade Racial da Bahia de
- Segundo o Estatuto baiano, o conjunto de ações voltadas à proteção e defesa dos direitos d
- O Estatuto prevê a assistência religiosa e veda a difusão, pelos meios de comunicação, de 
```

## Pedido 35: Realismo, Naturalismo, Parnasianismo e Simbolismo (`literatura.realismo-naturalismo-parnasianismo-simbolismo`, 17 tópicos de edital)

```
Matéria: literatura.realismo-naturalismo-parnasianismo-simbolismo (Realismo, Naturalismo, Parnasianismo e Simbolismo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Rupturas estéticas e contexto brasileiro de fins do século XIX": 16
- "2. Realismo: ironia, narradores e conflitos sociais": 23
- "3. Naturalismo: determinismo, ambiente e representação social": 19
- "4. Parnasianismo: culto da forma e poesia brasileira": 19
- "5. Simbolismo: sugestão, musicalidade e transcendência": 23

Já existem (não repita):
- Uma leitora encontra um narrador que elogia o prestígio de uma família, mas logo revela os
- Ao organizar uma linha do tempo, um estudante precisa identificar os dois romances publica
- Num excerto inventado, a vida das personagens muda segundo as condições do alojamento, o t
- Um poema apresenta símbolos ligados a sinos, névoa, silêncio e aspiração espiritual, aprox
- Um estudante reúne autores para representar a chamada tríade parnasiana brasileira. Que co
- Uma avaliação questiona se o ciúme de Bento Santiago, narrador de *Dom Casmurro*, demonstr
- Em uma ficha, é preciso indicar a obra de 1890 de Aluísio Azevedo em que a habitação colet
- Um soneto descreve minuciosamente relevos e reflexos de uma peça antiga, explorando precis
- Qual acontecimento histórico é anterior à Proclamação da República e essencial para contex
- Uma questão atribui a *Missal* e *Broquéis* o mesmo gênero literário. A informação correta
- Num comentário de leitura, uma aluna diz que um conto sobre capturar uma mulher escravizad
- Um pesquisador afirma que o Naturalismo é identificável porque registra uma doença em dete
- Uma professora explica que o Parnasianismo prestigiou o soneto, mas não o criou. O que con
- Um poema combina a expressão autoral 'o azul do silêncio' e imagens de perfume musical. Qu
- Dois poemas apresentam rigor métrico. Um privilegia a superfície de um objeto; o outro evo
- Uma redação crítica afirma que a mulher machadiana se resume à condição de infiel, porque 
- Em um manual antigo, a conduta de um grupo é atribuída a supostas características raciais 
- Uma questão define o começo do Simbolismo brasileiro por obras de 1893 e conclui que o Par
- Um leitor atribui a *Memórias póstumas* o propósito de registrar fatos sem interferência d
- Uma comparação de duas cenas aponta que ambas retratam desigualdade, mas só uma sustenta q
- Uma análise compara *Profissão de fé*, de Bilac, à produção de Cruz e Sousa. Qual afirmaçã
- Um comentário sobre Alphonsus de Guimaraens reduz *Ismália* a uma descrição documental da 
- Ao examinar *O mulato*, uma leitora nota a denúncia do preconceito racial, mas também ling
- Um professor propõe separar *Vaso grego*, de Alberto de Oliveira, de um poema de Cruz e So
- Em uma leitura sobre indígenas e negros na transição do Romantismo ao Realismo, qual formu
- Um texto crítico afirma que, ao narrar em primeira pessoa, o defunto-autor de Machado torn
- Uma estudante compara a permanência histórica do Parnasianismo com o surgimento do Simboli
- Como *Dom Casmurro* é narrado por Bento Santiago em primeira pessoa, o relato comprova de 
- Ao examinar *O cortiço*, uma leitora pode considerar o espaço coletivo um elemento ativo n
- Em um poema, a presença de um soneto é suficiente para demonstrar que a obra pertence ao P
- Um poema que associa a cor azul a um som distante pode explorar sinestesia, recurso freque
- Os livros *Missal* e *Broquéis*, de Cruz e Sousa, foram publicados em 1893 e pertencem res
- Ao analisar um trecho em que uma personagem elogia o altruísmo de um homem e a narrativa r
- Um texto naturalista que descreve condições de trabalho também precisa afirmar expressamen
- Olavo Bilac, Alberto de Oliveira e Raimundo Correia constituem o grupo frequentemente iden
- Uma leitura de Cruz e Sousa deve tratar as referências à alvura como prova automática de q
- A crítica social de *Pai contra mãe* pode ser lida pela relação entre a sobrevivência de u
- Na passagem do Romantismo ao Realismo, as personagens indígenas e negras deixam de existir
- No Simbolismo, referências místicas podem operar como imagens de uma busca subjetiva pelo 
- Em *Dom Casmurro*, a organização retrospectiva das lembranças de Bento deve ser equiparada
- A aproximação de *O cortiço* ao Naturalismo se sustenta quando a análise relaciona a morad
- Uma representação naturalista de preconceito racial, por criticar o racismo do enredo, é i
- Um soneto simbolista pode conservar rimas e rigor métrico ao mesmo tempo que desenvolve im
- A publicação de *Missal* e *Broquéis* em 1893 permite concluir que Olavo Bilac interrompeu
- Na leitura de *Vaso grego* e de um poema simbolista, a análise das funções descritiva e su
- A obra de Alphonsus de Guimaraens, por recorrer à religiosidade, necessariamente produz ap
```

## Pedido 36: Estatuto dos Funcionários Públicos Civis do Estado de São Paulo (Lei 10.261/1968) (`sao-paulo.estatuto-funcionarios-lei-10261`, 16 tópicos de edital)

```
Matéria: sao-paulo.estatuto-funcionarios-lei-10261 (Estatuto dos Funcionários Públicos Civis do Estado de São Paulo (Lei 10.261/1968))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, provimento, posse e exercício": 25
- "2. Tempo de serviço, vacância, férias, licenças e estabilidade": 25
- "3. Deveres, proibições, responsabilidades e penas": 25
- "4. Providências preliminares, soluções consensuais e procedimento disciplinar": 25

Já existem (não repita):
- A Lei nº 10.261/1968 aplica-se aos funcionários públicos civis dos três Poderes do Estado 
- Para o Estatuto paulista, o conjunto de cargos da mesma denominação recebe o nome de:
- Segundo o art. 11 da Lei 10.261/1968, NÃO figura entre as formas de provimento de cargo pú
- Nos concursos regidos pelo Estatuto paulista, as provas são avaliadas na escala de 0 a 100
- O ex-funcionário demitido a bem do serviço público pode ser readmitido, sem ressarcimento 
- A readaptação, investidura em cargo mais compatível com a capacidade do funcionário, depen
- Sobre a reintegração no Estatuto paulista, assinale a afirmativa correta.
- A posse deve ocorrer em 30 dias contados da publicação do ato de provimento, prazo que pod
- O funcionário que toma posse mas não entra em exercício dentro do prazo legal, já consider
- O prazo para a posse pode ser suspenso por até 120 dias, a critério do órgão médico oficia
- O funcionário não pode ser removido ou transferido de ofício para cargo a ser exercido for
- O servidor preso em flagrante ou preventivamente será considerado afastado do exercício do
- Considera-se de efetivo exercício o afastamento do funcionário, por até 2 dias, em razão d
- O tempo de serviço gratuito prestado ao Estado é computado apenas para fins de aposentador
- Entre as causas de vacância do cargo listadas no art. 86 do Estatuto paulista, inclui-se:
- As férias do funcionário podem ser gozadas de uma só vez ou fracionadas em até três períod
- O período de férias do funcionário é reduzido para 20 dias quando, no exercício anterior, 
- O funcionário ocupante exclusivamente de cargo em comissão pode obter licença para tratar 
- Considere a licença à funcionária gestante prevista na Lei 10.261/1968. É correto afirmar 
- Com a redação dada pela Lei nº 18.473/2026, a licença-paternidade do funcionário público c
- Na licença por motivo de doença em pessoa da família, o funcionário que já está afastado h
- A licença para tratar de interesses particulares, sem vencimento, pode ser concedida depoi
- Quanto à licença-prêmio prevista no Estatuto paulista, assinale a afirmativa correta.
- Para o Estatuto paulista, considera-se também acidente, para fins de licença, a lesão sofr
- Segundo o próprio Estatuto, a estabilidade diz respeito ao cargo ocupado, e não ao serviço
- É dever do funcionário público civil paulista, segundo o art. 241 da Lei 10.261/1968:
- Considera-se cumprido o dever de representar aos superiores sobre irregularidades quando o
- É vedado ao funcionário constituir-se procurador de partes ou servir de intermediário pera
- A proibição de participar da gerência de sociedades comerciais que mantenham relações com 
- Fora dos casos de alcance, desfalque, remissão ou omissão de recolhimento, a indenização d
- O servidor absolvido pela Justiça por decisão transitada em julgado que negue a existência
- A pena de suspensão, aplicada em caso de falta grave ou de reincidência, não pode exceder:
- A autoridade que aplicar a pena de suspensão poderá convertê-la em multa, na base de 50% p
- Para o Estatuto paulista, caracteriza inassiduidade, punível com demissão, a ausência ao s
- A pena de demissão por ineficiência no serviço só será aplicada quando verificada a imposs
- Entre as condutas abaixo, aquela que o Estatuto paulista pune com demissão a bem do serviç
- Pelo art. 260, os Coordenadores são competentes para aplicar penalidades até a de suspensã
- Extingue-se pela prescrição, em dois anos, a punibilidade da falta sujeita às penas de rep
- Nos termos do art. 261 da Lei 10.261/1968, interrompe a prescrição disciplinar:
- A demissão e a demissão a bem do serviço público acarretam incompatibilidade para nova inv
- Instaurada sindicância ou processo administrativo, o afastamento preventivo do servidor, o
- A apuração preliminar, de natureza simplesmente investigativa, é realizada quando a infraç
- Nas práticas autocompositivas previstas no Estatuto, a eventual recusa do funcionário em p
- Para que a autoridade proponha a celebração de Termo de Ajustamento de Conduta, o funcioná
- O prazo de cumprimento do Termo de Ajustamento de Conduta não pode ser inferior a um nem s
- Sobre a suspensão condicional da sindicância, prevista no art. 267-N do Estatuto paulista,
- Será instaurada sindicância quando a falta disciplinar, por sua natureza, puder determinar
- Na sindicância regida pela Lei 10.261/1968, a autoridade sindicante e cada acusado podem a
- O processo administrativo deve ser instaurado por portaria no prazo improrrogável de 8 dia
- No processo administrativo, se o acusado não for encontrado ou se furtar à citação, esta s
- No processo instaurado exclusivamente para apurar inassiduidade, a defesa somente poderá v
- Quanto ao recurso contra decisão que aplica penalidade, previsto no art. 312 do Estatuto, 
- A revisão de punição disciplinar de que não caiba mais recurso pode ser pedida a qualquer 
- De acordo com a Lei 10.261/1968, os prazos previstos no Estatuto são contados:
- Recebido o processo administrativo relatado, a autoridade que determinou sua instauração d
```

## Pedido 37: Alergia e imunologia clínica (`medicina.alergia-imunologia-clinica`, 16 tópicos de edital)

```
Matéria: medicina.alergia-imunologia-clinica (Alergia e imunologia clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "Bases imunológicas e diagnóstico da alergia": 20
- "Asma e rinoconjuntivite alérgica": 20
- "Dermatite atópica, urticária e angioedema": 20
- "Alergia alimentar, medicamentosa e anafilaxia": 20
- "Erros inatos da imunidade, imunoterapia e segurança clínica": 20

Já existem (não repita):
- Um adulto tem IgE específica para amendoim detectável, mas ingere o alimento toda semana, 
- Uma operária apresenta eczema pruriginoso localizado exatamente sob um componente metálico
- Na consulta, uma criança apresenta prick test positivo para leite, sem história imediata c
- Em uma reação adversa a medicamento com exantema surgido dias após o início e sem sintomas
- Paciente com espirros em salvas, coriza aquosa e prurido nasal recorrentes após exposição 
- Uma pessoa com asma apresenta melhora dos sintomas após usar broncodilatador de curta ação
- Adolescente com sibilância recorrente tem espirometria normal no intervalo entre episódios
- Usuária de descongestionante nasal tópico diário há semanas refere obstrução progressiva a
- Paciente apresenta placas pruriginosas que aparecem em locais diferentes e desaparecem ind
- Mulher tem urticas praticamente diárias há nove semanas sem associação reprodutível com in
- Homem relata episódios de edema de face sem urticas, associados a dor abdominal e pouca re
- Qual intervenção pertence aos cuidados fundamentais de dermatite atópica com ressecamento,
- Após ingerir alimento suspeito, uma criança apresenta urticária generalizada, tosse, sibil
- Adulto tem história antiga de diarreia isolada durante uso de amoxicilina na infância, sem
- Durante exame contrastado, uma paciente informa alergia a camarão, mas nunca recebeu contr
- Após uso de anticonvulsivante, pessoa desenvolve febre, exantema extenso, edema facial e e
- Criança apresenta pneumonias graves recorrentes, infecções oportunistas e dificuldade de c
- Paciente com rinoconjuntivite relevante por ácaros, história consistente e sensibilização 
- Uma pessoa com asma mal controlada comparece para receber dose programada de imunoterapia 
- Em auditoria de serviço de imunoterapia, não existem registros de lote, concentração, dilu
- A sensibilização IgE demonstrada por teste laboratorial, mesmo sem qualquer reação clínica
- O teste de contato, também chamado patch test, é utilizado principalmente na investigação 
- Antihistamínicos usados antes do prick test podem interferir na resposta cutânea e contrib
- Na hipersensibilidade tipo III, o principal mecanismo é o reconhecimento de alérgenos por 
- A presença de espirros, coriza aquosa e prurido nasal recorrentes após exposições ambienta
- Na avaliação de asma, sintomas pouco frequentes e uso eventual de broncodilatador são sufi
- No manejo da asma em adultos e adolescentes, as recomendações atuais apoiam estratégias co
- A ocorrência de dor ocular intensa e redução de acuidade visual em paciente com olho verme
- Na urticária comum, cada urtica é tipicamente fugaz e desaparece em até cerca de 24 horas,
- Toda urticária espontânea crônica é causada por alergia alimentar IgE-mediada, sendo indic
- No angioedema mediado por bradicinina, é comum haver ausência de urticas e resposta insufi
- Na investigação de angioedema hereditário por deficiência de inibidor de C1 esterase, a do
- Diante de anafilaxia clinicamente reconhecida, a adrenalina intramuscular deve ser prioriz
- A ausência de urticária e rubor exclui anafilaxia mesmo quando surgem broncoespasmo e hipo
- Uma síndrome cutânea grave tardia com mucosite e descolamento epidérmico após fármaco é si
- Na farmacovigilância, a notificação ao VigiMed depende da comprovação definitiva de nexo c
- Erros inatos da imunidade podem manifestar-se por infecções recorrentes ou graves, e a hip
- Um hemograma normal e imunoglobulinas séricas dentro dos valores de referência em uma únic
- A indicação de imunoterapia alérgeno-específica deve considerar a relevância clínica do al
- O preparo seguro de extratos para imunoterapia dispensa identificação de lote e registro d
```

## Pedido 38: Lei 14.735/2023: Lei Orgânica Nacional das Polícias Civis (`legislacao.lei-organica-nacional-policias-civis`, 16 tópicos de edital)

```
Matéria: legislacao.lei-organica-nacional-policias-civis (Lei 14.735/2023: Lei Orgânica Nacional das Polícias Civis)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, princípios e diretrizes": 17
- "2. Competências da polícia civil": 17
- "3. Organização e funcionamento": 22
- "4. Cargos, concurso, investidura e promoção": 22
- "5. Atribuições, direitos, deveres, vedações e disposições finais": 22

Já existem (não repita):
- Segundo a Lei 14.735/2023, as polícias civis são dirigidas por delegado de polícia em ativ
- As polícias civis integram o Sistema Único de Segurança Pública apenas como órgãos consult
- A iniciativa da lei orgânica da polícia civil de cada Estado cabe, segundo a Lei 14.735/20
- Os entes federativos podem legislar de forma suplementar sobre as matérias disciplinadas n
- Entre os princípios institucionais básicos da polícia civil previstos na Lei 14.735/2023, 
- A discrição e a preservação do sigilo necessário à efetividade da investigação e à salvagu
- Conforme as diretrizes da Lei 14.735/2023, o planejamento e a distribuição do efetivo poli
- Segundo as diretrizes da lei, a capacitação profissional dos policiais civis deve ser cont
- A Lei 14.735/2023 atribui à polícia civil, privativamente, as funções de polícia judiciári
- As atribuições relativas às competências da polícia civil podem ser exercidas por servidor
- Compete à polícia civil organizar e executar a atividade pericial oficial:
- Entre as competências da polícia civil está a de organizar e executar os serviços de ident
- Assinale a competência da polícia civil prevista no art. 6º da Lei 14.735/2023.
- A polícia civil pode custodiar o policial civil condenado ou preso provisório, por meio de
- A Lei 14.735/2023 admite que a polícia civil celebre convênios e acordos de cooperação téc
- NÃO integra a estrutura organizacional básica da polícia civil, como órgão essencial, segu
- O Delegado-Geral de Polícia Civil deve apresentar, até 90 dias após sua nomeação, planejam
- A proposta de estrutura organizacional que integra o planejamento estratégico do Delegado-
- O Conselho Superior de Polícia Civil é presidido pelo Delegado-Geral e composto por repres
- O Corregedor-Geral de Polícia Civil é:
- Nos processos disciplinares em que for aplicada a penalidade de demissão, é garantido o du
- Aos policiais civis que tenham sido lotados em unidades da Corregedoria-Geral, a Lei 14.73
- A Escola Superior de Polícia Civil pode realizar cursos de graduação e de pós-graduação la
- Segundo a Lei 14.735/2023, devem ter efetivo composto exclusivamente de policiais civis as
- Os Institutos de Criminalística, de Medicina Legal e de Identificação, quando integrados à
- Sobre as unidades técnico-científicas não integradas à polícia civil, a Lei 14.735/2023 ga
- São cargos do quadro de servidores da polícia civil, todos de nível superior, segundo a Le
- Para o cargo de oficial investigador de polícia exige-se diploma de ensino superior comple
- Para o cargo de delegado de polícia, a Lei 14.735/2023 exige bacharelado em Direito e:
- No concurso para delegado de polícia, a Ordem dos Advogados do Brasil participa apenas da 
- No concurso para delegado de polícia, o tempo de atividade policial civil é considerado na
- Os concursos públicos para o cargo de delegado de polícia devem adotar a prova oral como e
- Durante o curso de formação profissional, de caráter eliminatório, pode ser concedida ajud
- O servidor policial civil que pedir exoneração antes de completar 3 anos de exercício deve
- A investidura em cargo da polícia civil é feita na classe inicial, e os editais dos concur
- O policial civil que passa a exercer funções em outro ente federativo, mediante permuta ou
- Cabe ao delegado de polícia presidir o inquérito policial, no qual deve atuar com isenção,
- O oficial investigador de polícia exerce atribuições apuratórias, cartorárias, procediment
- O perito oficial criminal exerce atribuições de perícia oficial de natureza criminal sob r
- É direito assegurado aos policiais civis em atividade, nos termos do art. 30 da Lei 14.735
- A estabilidade do policial civil dar-se-á após 2 anos de efetivo exercício no cargo, confo
- Em virtude da atividade de risco exercida, o policial civil pode ser promovido, de forma p
- À policial civil que retorna da licença-maternidade, a Lei 14.735/2023 garante retorno e p
- A vedação de divulgar técnicas de investigação utilizadas pelas polícias civis aplica-se i
- Quanto à custódia de presos em prédios e unidades das polícias civis, a Lei 14.735/2023 es
- A Lei 14.735/2023 instituiu o Conselho Nacional da Polícia Civil, com competência consulti
- A Lei 14.735/2023 fixa como dia nacional da polícia civil a data de:
- As funções gratificadas de assessoramento e de chefia da polícia civil podem ser ocupadas 
```

## Pedido 39: Estatuto do Servidor de Pernambuco (Lei nº 6.123/1968) (`legislacao.servidores-pernambuco-lei-6123`, 16 tópicos de edital)

```
Matéria: legislacao.servidores-pernambuco-lei-6123 (Estatuto do Servidor de Pernambuco (Lei nº 6.123/1968))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, provimento, posse e exercício": 20
- "2. Estágio, estabilidade, disponibilidade e movimentação": 20
- "3. Vacância, jornada, férias e licenças": 20
- "4. Vencimento, vantagens, concessões e direito de petição": 20
- "5. Regime disciplinar e processo administrativo": 20

Já existem (não repita):
- Segundo a Lei nº 6.123/1968, são formas de provimento de cargo público:
- Pela Lei nº 6.123/1968, não haverá posse nos casos de promoção e de reintegração.
- A posse, na Lei nº 6.123/1968, deve ocorrer no prazo de:
- O decurso do prazo para a posse, sem que ela se realize, importa em não aceitação do provi
- Pela Lei nº 6.123/1968, é permitida a nomeação em caráter interino para cargo efetivo, enq
- Segundo a Lei nº 6.123/1968, não será aberto concurso para preenchimento de cargo público 
- O estágio probatório previsto na Lei nº 6.123/1968 tem duração de:
- No processo de apuração de inaptidão durante o estágio probatório, a Lei nº 6.123/1968 ass
- Pela Lei nº 6.123/1968, a estabilidade diz respeito ao serviço público e não ao cargo, e o
- Assinale a alternativa que define corretamente a forma de provimento indicada, segundo a L
- O aproveitamento do funcionário em disponibilidade far-se-á obrigatoriamente na primeira o
- Sobre a promoção na Lei nº 6.123/1968, assinale a alternativa correta.
- Pela Lei nº 6.123/1968, o funcionário que se aposentar ou falecer sem que tenha sido reali
- A duração normal do trabalho prevista na Lei nº 6.123/1968 para o funcionário em geral é d
- Pela Lei nº 6.123/1968, é proibida a acumulação de férias, salvo imperiosa necessidade do 
- A licença-prêmio da Lei nº 6.123/1968 corresponde a:
- Não será concedida licença-prêmio se, no decênio correspondente, o funcionário houver come
- Pela Lei nº 6.123/1968, a licença sem remuneração para trato de interesse particular pode 
- A servidora gestante, nos termos da Lei nº 6.123/1968, tem direito a licença-maternidade d
- O funcionário não poderá permanecer em licença para tratamento de saúde por período superi
- Segundo a Lei nº 6.123/1968, se antes do ato de exoneração o servidor houver praticado inf
- A gratificação adicional por tempo de serviço prevista na Lei nº 6.123/1968 corresponde a:
- A gratificação pela prestação de serviço extraordinário corresponde a 50% a mais do valor 
- Por motivo de casamento ou de falecimento de cônjuge, pais, filhos ou irmãos, o funcionári
- Pela Lei nº 6.123/1968, nenhum funcionário poderá perceber vencimento inferior ao maior sa
- Quanto ao direito de petição na Lei nº 6.123/1968, assinale a alternativa correta.
- O funcionário decai do direito de pleitear na esfera administrativa em cinco anos, quanto 
- Verificada em processo administrativo acumulação proibida e comprovada a boa-fé, o funcion
- São deveres do funcionário previstos no art. 193 da Lei nº 6.123/1968, EXCETO:
- Pela Lei nº 6.123/1968, é proibido ao funcionário exercer comércio ou participar de socied
- Pela Lei nº 6.123/1968, a responsabilidade administrativa do funcionário é afastada quando
- Considera-se abandono de cargo, nos termos da Lei nº 6.123/1968:
- Na Lei nº 6.123/1968, os diretores de repartição são competentes para aplicar:
- Assinale os prazos de prescrição das faltas disciplinares previstos na Lei nº 6.123/1968.
- A demissão aplicada com a nota a bem do serviço público impede a participação do ex-servid
- A sindicância prevista na Lei nº 6.123/1968:
- O inquérito administrativo da Lei nº 6.123/1968 é promovido por comissão de três funcionár
- No inquérito administrativo, citado o indiciado, o prazo para apresentar defesa é de dez d
- Segundo a Lei nº 6.123/1968, a suspensão preventiva, imposta quando a presença do funcioná
- Pela Lei nº 6.123/1968, constitui fundamento suficiente para a revisão do inquérito admini
```

## Pedido 40: Lei Orgânica do Tribunal de Contas do Estado do Rio Grande do Norte (LC 464/2012) (`rio-grande-do-norte.lei-organica-tce-rn-lc-464`, 16 tópicos de edital)

```
Matéria: rio-grande-do-norte.lei-organica-tce-rn-lc-464 (Lei Orgânica do Tribunal de Contas do Estado do Rio Grande do Norte (LC 464/2012))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, competências e jurisdição": 25
- "2. Organização, Conselheiros, Auditores e Ministério Público de Contas": 25
- "3. Processo, contas, fiscalização, atos de pessoal e consulta": 25
- "4. Sanções, prescrição, cautelares, recursos e controle interno": 25

Já existem (não repita):
- Pela LC 464/2012, o controle externo no Rio Grande do Norte está a cargo da Assembleia Leg
- Em relação às contas anuais do Governador do Estado, compete ao Tribunal de Contas do Rio 
- Verificada ilegalidade em contrato e não atendida a determinação do Tribunal, a sustação d
- A decisão do Tribunal de Contas do RN que responde a uma consulta sobre interpretação de l
- A apreciação, para fins de registro, da legalidade dos atos de admissão de pessoal pelo Tr
- Pela LC 464/2012, quando considerar grave a infração cometida, o Tribunal pode declarar o 
- A jurisdição do Tribunal de Contas do RN alcança os sucessores dos administradores e respo
- Os sujeitos à jurisdição do Tribunal de Contas do RN podem liberar-se da responsabilidade 
- A cópia das declarações de bens e rendimentos das autoridades listadas no art. 5º da LC 46
- O Tribunal de Contas do Estado do Rio Grande do Norte tem sede em Natal e compõe-se de:
- Dos sete Conselheiros do Tribunal de Contas do RN, quatro são escolhidos pela Assembleia L
- NÃO é requisito para a nomeação de Conselheiro do Tribunal de Contas do RN:
- Os Conselheiros do Tribunal de Contas do RN têm as mesmas garantias e prerrogativas dos De
- É vedado ao Conselheiro do Tribunal de Contas do RN exercer qualquer outro cargo ou função
- Parentes consanguíneos ou afins, em linha reta ou colateral, não podem ocupar simultaneame
- O Presidente e o Vice-Presidente do Tribunal de Contas do RN são eleitos para mandato de d
- Se a vaga de Presidente do Tribunal de Contas do RN ocorrer dentro dos sessenta dias finai
- Nos impedimentos e ausências dos Conselheiros, a substituição é feita, mediante convocação
- O Auditor do Tribunal de Contas do RN, quando no exercício das demais atribuições da judic
- O Auditor que substitui Conselheiro por motivo diverso da vacância só recebe o vencimento 
- Os membros do Ministério Público junto ao Tribunal de Contas do RN participam das sessões 
- Pela redação dada ao art. 10 pela LC 775/2024, as Câmaras do Tribunal de Contas do RN são 
- No caso de processo disciplinar envolvendo Conselheiro ou Auditor, cabe à Corregedoria do 
- O Tribunal Pleno e as Câmaras do Tribunal de Contas do RN instalam-se e deliberam, respect
- Após a LC 684/2021, os prazos previstos na LC 464/2012 são peremptórios e contados em dias
- À parte é assegurado o direito de defesa, no Tribunal de Contas do RN, sempre que do proce
- Na LC 464/2012, o ato pelo qual o Tribunal dá ciência ao responsável do processo contra el
- As sessões do Tribunal de Contas do RN são públicas, salvo no julgamento disciplinar de Co
- Segundo o art. 56 da LC 464/2012, a apreciação das contas anuais do Governador do Estado e
- O parecer prévio do Tribunal de Contas sobre as contas do Prefeito, submetido à Câmara Mun
- Não sendo as contas municipais enviadas ao Tribunal no prazo e na forma exigidos, o Tribun
- Pela redação dada ao art. 65 pela LC 775/2024, diante de omissão no dever de prestar conta
- Contas que apresentam apenas impropriedade técnica ou falha formal, sem indício de má-fé o
- Segundo o art. 75 da LC 464/2012, NÃO é hipótese que leva ao julgamento de contas irregula
- Ordenado o trancamento de contas consideradas iliquidáveis, o Tribunal pode autorizar o de
- É parte legítima para denunciar irregularidades ou ilegalidades ao Tribunal de Contas do R
- Distribuída ao Relator, a denúncia é submetida, em caráter sigiloso, a instrução prelimina
- Na representação feita ao Tribunal de Contas do RN, observa-se o procedimento das denúncia
- NÃO figura entre os instrumentos de fiscalização listados no art. 82 da LC 464/2012, após 
- As regras de sustação de contratos pela LC 464/2012 estendem-se aos aditivos contratuais q
- Ao apreciar a legalidade de um ato de aposentadoria sujeito a registro, o Tribunal de Cont
- Os atos de admissão e de concessão de aposentadoria sujeitos a registro devem ser remetido
- NÃO tem legitimidade para formular consulta ao Tribunal de Contas do RN, segundo o art. 10
- Se o Tribunal de Contas do RN rever a interpretação adotada na solução de uma consulta, a 
- Verificada fraude à licitação, o Tribunal de Contas do RN declara a inidoneidade do licita
- A ação punitiva do Tribunal de Contas do RN prescreve em cinco anos, contados da data da p
- Segundo o art. 112 da LC 464/2012, NÃO interrompe a prescrição da ação punitiva:
- O reconhecimento da prescrição da ação punitiva impede o Tribunal de Contas do RN de julga
- Condenado por decisão transitada em julgado, o responsável é citado para efetuar e comprov
- Em caso de comprovada urgência, as medidas cautelares podem ser determinadas por decisão d
- O Termo de Ajustamento de Gestão, proposto pelo Ministério Público junto ao Tribunal, não 
- Contra despacho do Relator, no Tribunal de Contas do RN, cabe:
- O erro na escolha do recurso não prejudica o seu conhecimento pelo Tribunal de Contas do R
- A revisão de decisão definitiva em processo de controle externo, perante o Pleno do Tribun
- O responsável pelo controle interno que tomar conhecimento de irregularidade e não der ciê
- O art. 111-A, incluído pela LC 684/2021, que fixava o prazo de cinco anos para o Tribunal 
- Nas matérias da LC 464/2012, aplicam-se subsidiariamente, nos pontos em que com ela não co
```

## Pedido 41: Constituição de Minas Gerais: administração pública, servidores e segurança pública (`legislacao.constituicao-minas-gerais-servidores-seguranca`, 15 tópicos de edital)

```
Matéria: legislacao.constituicao-minas-gerais-servidores-seguranca (Constituição de Minas Gerais: administração pública, servidores e segurança pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios e organização da administração pública": 25
- "2. Servidores: acesso, cargos, remuneração e acumulação": 25
- "3. Servidores civis: política de pessoal, direitos e estabilidade": 25
- "4. Defesa social e segurança pública": 25

Já existem (não repita):
- Segundo o art. 13 da Constituição do Estado de Minas Gerais, a administração pública sujei
- Pela Constituição mineira, o agente público motivará o ato administrativo que praticar, ex
- Nos termos do art. 14 da Constituição mineira, dependem de lei específica a instituição e 
- A lei que autorizar a alienação das ações que garantem ao Estado o controle de empresa púb
- A desestatização de empresa estadual prestadora de serviço de distribuição de gás canaliza
- Ressalvada hipótese específica prevista na própria Constituição, o Estado de Minas Gerais 
- É permitido à Assembleia delegar ao Executivo, por lei genérica, o poder de criar, extingu
- Quanto à responsabilidade civil, a Constituição mineira estabelece que as pessoas jurídica
- Os Poderes do Estado e dos Municípios publicarão, trimestralmente, o montante das despesas
- Para a aquisição de bem imóvel a título oneroso pelo Estado de Minas Gerais, a Constituiçã
- Nos termos do art. 19 da Constituição mineira, a administração fazendária e seus servidore
- Segundo a Constituição mineira, o prazo de validade do concurso público é de:
- A contratação por tempo determinado para atender a necessidade temporária de excepcional i
- As funções de confiança são exercidas exclusivamente por servidores ocupantes de cargo efe
- O limite remuneratório aplicável aos servidores da administração direta, autárquica e fund
- Havendo compatibilidade de horários, a Constituição mineira permite a acumulação remunerad
- O servidor afastado para exercer mandato eletivo terá seu tempo de serviço contado para to
- O servidor da administração direta, autárquica ou fundacional investido no mandato de Pref
- Para cumprir os limites de despesa com pessoal, a primeira providência que o Estado deve a
- Os acréscimos pecuniários percebidos por servidor público não serão computados nem acumula
- Serão concedidas ao servidor ocupante de cargo efetivo e de função pública férias-prêmio c
- O adicional de desempenho previsto na Constituição mineira é pago mensalmente, em valor va
- A Constituição mineira assegura ao servidor público civil assistência gratuita em creche e
- O servidor que, por acidente ou doença, se tornar inapto para as atribuições específicas d
- Segundo a Constituição mineira, o servidor nomeado em virtude de concurso para cargo de pr
- O servidor estável só perderá o cargo em virtude de sentença judicial transitada em julgad
- Extinto o cargo ou declarada sua desnecessidade, o servidor estável:
- É garantida a liberação do servidor público civil eleito para a diretoria de entidade sind
- Pela Constituição mineira, com redação dada pela reforma previdenciária, a aposentadoria c
- A Constituição mineira admite que lei complementar estabeleça critérios diferenciados de i
- De acordo com o art. 133 da Constituição mineira, a defesa social organiza-se de forma sis
- O Conselho de Defesa Social é órgão consultivo do Governador na definição da política de d
- Integram o Conselho de Defesa Social três representantes da sociedade civil, sendo um da O
- Pela Constituição mineira, os órgãos de segurança pública do Estado subordinam-se ao Secre
- Segundo o art. 139 da Constituição mineira, são atividades privativas da Polícia Civil:
- O ingresso na Polícia Civil dá-se na classe inicial das carreiras, mediante concurso públi
- Para o ingresso na carreira de Delegado de Polícia, a Constituição mineira exige:
- Pelo texto do art. 141 da Constituição mineira, o Chefe da Polícia Civil é:
- O cargo de Delegado de Polícia integra, para todos os fins, as carreiras jurídicas do Esta
- A Constituição mineira prevê que lei disporá sobre serviços autônomos de assistência psico
- O Município pode constituir guardas municipais destinadas à proteção de seus bens, serviço
```

## Pedido 42: Disposições especiais sobre duração e condições de trabalho (`direito-trabalho.disposicoes-especiais`, 15 tópicos de edital)

```
Matéria: direito-trabalho.disposicoes-especiais (Disposições especiais sobre duração e condições de trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Bancários, telefonia, músicos e operadores cinematográficos": 25
- "2. Motoristas, ferroviários e tripulantes": 25
- "3. Minas, jornalistas, professores e químicos": 25
- "4. Reescrita e estratégias de prova": 25

Já existem (não repita):
- Sobre a jornada de bancários prevista na CLT, assinale a alternativa correta.
- Um empregado bancário exerce função de chefia e recebe gratificação exatamente igual a um 
- Nas empresas abrangidas pelo art. 227 da CLT, qual é o limite normal dos operadores de tel
- A respeito do trabalho ininterrupto dos operadores abrangidos pela seção de telefonia, ass
- Sobre músicos profissionais e trabalho em espetáculo, qual alternativa está de acordo com 
- A jornada normal de operadores cinematográficos e ajudantes é de até seis horas, com qual 
- Uma sala de cinema precisa de exibição extraordinária que prorrogará o trabalho dos operad
- Para os ferroviários, o art. 238 considera trabalho efetivo:
- Um ferroviário efetivo aguarda chamado em sua própria casa, em escala de sobreaviso. Qual 
- Qual é o limite de permanência no posto do tripulante abrangido pela seção marítima da CLT
- Uma trabalhadora permanece por uma hora e quarenta minutos em serviço contínuo no interior
- A respeito de prorrogação da jornada em mina subterrânea, assinale a alternativa correta.
- Uma redação pretende elevar de cinco para sete horas a jornada normal de jornalista. Qual 
- Uma professora leciona no turno da manhã e da noite no mesmo estabelecimento, com interval
- Uma fábrica mantém laboratório de controle químico e pretende dispensar a admissão de prof
- Após o julgamento da ADI 5.322, é correto afirmar que o tempo de espera do motorista profi
- A seção de telefonia fixa seis horas contínuas diárias ou trinta e seis semanais para os o
- Para os empregados de estações do interior com serviço intermitente ou de pouca intensidad
- O empregado que atua no interior de câmara frigorífica recebe pausa de vinte minutos depoi
- A CLT permite ao professor lecionar em mais de um turno no mesmo estabelecimento, desde qu
- Os arts. 254 a 292 da CLT ainda estabelecem, sem revogação, o regime especial de estiva e 
- Em minas subterrâneas, a jornada normal efetiva é de seis horas por dia ou trinta e seis p
- O art. 303 estabelece jornada normal de cinco horas para jornalistas abrangidos, tanto de 
- Uma empregada de portaria atua em banco. Qual limite especial pode alcançar essa função?
- Em serviço de telefonia abrangido pela CLT, qual consequência pode decorrer da necessidade
- Para empregados sujeitos a horários variáveis na seção de telefonia, qual é a duração máxi
- No regime ferroviário de prontidão, que limite e forma de cômputo se aplicam?
- Como a CLT organiza as categorias de pessoal ferroviário para aplicação das regras especia
- Além de quem trabalha no interior de câmara frigorífica, a quem se estende a pausa previst
- Como é tratado, para pagamento do salário, o deslocamento entre a boca da mina subterrânea
- Em empresa jornalística, quais profissionais podem estar abrangidos pela seção especial do
- Segundo a CLT, qual referência serve de base para fixar a remuneração mensal dos professor
- A regra de admissão obrigatória de químicos pode alcançar certas indústrias que obtêm prod
- Nos bancos, a escala do quadro de portaria deve prever empregados em função meia hora ante
- Para empregados sujeitos a horários variáveis na seção de telefonia, a CLT prevê até sete 
- A seção especial de telefonia da CLT também abrange operadores de radiotelegrafia embarcad
- Na remuneração mensal dos professores, cada mês é considerado composto por quatro semanas 
- O deslocamento da boca da mina subterrânea até o local de trabalho e no retorno é computad
- As horas de prontidão ferroviária são contadas à razão de dois terços do salário-hora norm
- A CLT limita a admissão obrigatória de químicos às empresas que fabricam explosivos e deri
```

## Pedido 43: Grandes animais, reprodução e patologia veterinária (`medicina-veterinaria.grandes-animais-reproducao-patologia`, 15 tópicos de edital)

```
Matéria: medicina-veterinaria.grandes-animais-reproducao-patologia (Grandes animais, reprodução e patologia veterinária)
Capítulos e quantas questões quero em cada um (total 100):
- "Anatomia, fisiologia e semiologia comparadas": 17
- "Clínica de bovinos: doenças metabólicas, digestivas, mamárias e de rebanho": 17
- "Clínica de equinos, bem-estar, transporte e controle de substâncias": 17
- "Clínica de ovinos, caprinos e suínos e manejo sanitário produtivo": 17
- "Fisiologia reprodutiva, biotecnologias e obstetrícia veterinária": 16
- "Patologia geral e especial, microbiologia, parasitologia e imunologia": 16

Já existem (não repita):
- Uma vaca consome dieta rica em fibra, enquanto um equino alimenta-se predominantemente de 
- Em ruminantes, o propionato produzido pela fermentação ruminal constitui importante precur
- Um criador solicita orientação após constatar que um bezerro parece vigoroso, mas não mamo
- O cotilédone é uma estrutura materna que se une à carúncula fetal para formar o placentoma
- Durante a avaliação de claudicação de um cavalo, qual alternativa descreve a sequência lóg
- O suíno possui estômago monogástrico, enquanto o equino é fermentador de intestino grosso,
- Qual associação placentária e anatômica comparativa está correta para uma questão de fisio
- Em investigação de surto em rebanho, a espécie, a distribuição dos casos por lote e o mome
- Uma vaca leiteira recém-parida apresenta fraqueza, decúbito, extremidades frias e redução 
- O California Mastitis Test identifica diretamente a espécie bacteriana responsável pela ma
- Em um lote com múltiplos casos de mastite, qual medida tem melhor fundamento epidemiológic
- A acidose ruminal pode estar relacionada a mudança rápida para dieta muito concentrada, co
- Em vacas após o parto, uma propriedade confunde retenção de membranas fetais e metrite. Qu
- A taxa de lotação em bovinocultura é um indicador de animais ou unidades animais por área 
- Qual combinação de agentes é plausível em um quadro de diarreia neonatal em bezerros, a de
- O PNCEBT é direcionado à sanidade bovina e bubalina, incluindo estratégias de vacinação co
- Cavalo com dor abdominal persistente, taquicardia progressiva e perfusão ruim após medidas
- Uma melhora temporária da dor após analgesia exclui, com segurança, obstrução intestinal e
- Em equídeos, a anemia infecciosa equina é causada por qual tipo de agente?
- Mormo é enfermidade bacteriana de equídeos que integra ações sanitárias oficiais e não dev
- Ao planejar o transporte rodoviário de equinos pertencentes a organização militar, qual cr
- As normas militares de controle de equinos, a exemplo da NORCE, substituem automaticamente
- Em competição equestre regulada por autoridade esportiva, como interpretar uma substância 
- A vacinação para influenza equina reduz riscos e pode diminuir gravidade, mas não assegura
- Um rebanho de ovinos apresenta anemia marcada, edema submandibular e perda de desempenho, 
- O sistema FAMACHA, baseado na coloração conjuntival, permite identificar com certeza a esp
- Em uma fazenda, cabras no final de gestação múltipla apresentam anorexia e depressão. A pr
- Em ovinos e caprinos, o uso indiscriminado e repetido de anti-helmínticos pode selecionar 
- Leitões recém-desmamados passam a apresentar diarreia aquosa e desidratação rápida. Qual h
- Na suinocultura, nascidos vivos, natimortos, mumificados, taxa de parto e mortalidade pré-
- Um programa de biosseguridade suína prevê quarentena de animais introduzidos e divisão de 
- A suspeita consistente de doença suídea de notificação obrigatória deve ser comunicada ao 
- No controle endócrino do ciclo estral, qual função é corretamente atribuída à progesterona
- Em geral, a égua apresenta maior atividade reprodutiva durante dias longos, enquanto muito
- Qual é uma vantagem operacional da inseminação artificial em tempo fixo (IATF) em bovinos?
- A prostaglandina F2-alfa induz luteólise quando há corpo lúteo responsivo, mas sua adminis
- Em uma distocia, o veterinário descreve cabeça fletida lateralmente e membros anteriores e
- Em suínos, a duração gestacional média aproximada de 114 dias é frequentemente memorizada 
- Na transferência de embriões bovinos, qual afirmação descreve adequadamente a técnica de p
- Prolapso uterino puerperal e prolapso vaginal observado no final da gestação são diagnósti
- Em uma necropsia, área de infarto em órgão sólido exibe preservação inicial dos contornos 
- Uma amostra fixada em formol destinada à histopatologia deve ser utilizada preferencialmen
- Em um surto respiratório, a PCR detecta material genético de certo agente em swab. O que e
- A imunidade passiva colostral fornece anticorpos maternos ao recém-nascido e tende a ser t
- Qual achado diferencia, em termos gerais, inflamação crônica de inflamação aguda?
- Um valor elevado de ovos por grama de fezes equivale diretamente ao número exato de vermes
- Um hemograma demonstra neutrofilia e um laudo histopatológico indica inflamação purulenta.
- Linfócitos B podem diferenciar-se em plasmócitos produtores de anticorpos, enquanto linfóc
```

## Pedido 44: Regimento Interno do Tribunal de Contas do Estado do Rio Grande do Norte (Resolução nº 009/2012) (`rio-grande-do-norte.regimento-interno-tce-rn`, 15 tópicos de edital)

```
Matéria: rio-grande-do-norte.regimento-interno-tce-rn (Regimento Interno do Tribunal de Contas do Estado do Rio Grande do Norte (Resolução nº 009/2012))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Pleno, sessões, comissões e Câmaras": 25
- "2. Dirigentes, Corregedoria, Escola de Contas e Ouvidoria": 25
- "3. Conselheiros e Auditores": 25
- "4. Ministério Público de Contas, serviços técnicos e processo": 25

Já existem (não repita):
- O recesso das atividades deliberativas do Pleno e das Câmaras do TCE-RN vai de 20 de dezem
- Segundo o Regimento Interno do TCE-RN, compete ao Pleno, e não às Câmaras:
- Cabe ao Pleno do TCE-RN organizar a lista tríplice dos Auditores e dos membros do Ministér
- Para a validade das decisões do Pleno do TCE-RN, o Regimento exige a existência de, pelo m
- Na mesa das sessões do Pleno, à direita do Conselheiro que preside fica:
- Na discussão de um processo no Pleno do TCE-RN, cada Conselheiro pode fazer uso da palavra
- O Conselheiro do TCE-RN pode abster-se de votar sempre que não se sentir habilitado a deci
- No Pleno do TCE-RN, a votação simbólica consiste:
- Vencido o Relator, redigirá o acórdão ou o parecer prévio o Conselheiro que emitir o prime
- O Conselheiro ou Auditor que pedir vista de processo no TCE-RN o terá pelo prazo máximo de
- Iniciado o julgamento do processo, o Relator continua competente para determinar diligênci
- As sessões ordinárias do Pleno do TCE-RN, salvo deliberação em contrário, realizam-se:
- Entre a publicação da pauta e a sessão de julgamento no TCE-RN deve mediar, pelo menos, o 
- O exame das contas prestadas pelo Governador, para emissão do parecer prévio, é feito no T
- NÃO é comissão permanente do TCE-RN, segundo o art. 67 do Regimento:
- No caso de empate na votação em decisão de Câmara do TCE-RN, cabe o voto de desempate ao C
- Na eleição do Presidente do TCE-RN, funciona como escrutinador:
- O Presidente e o Vice-Presidente eleitos em dezembro tomam posse em sessão extraordinária 
- Pela redação dada ao art. 130 pela Resolução nº 06/2024, durante a vacância do cargo, a Di
- Em caráter excepcional e havendo urgência, o Presidente do TCE-RN pode decidir matéria da 
- Dos atos e decisões administrativas do Presidente do TCE-RN cabe recurso:
- Antes de instaurar sindicância ou processo administrativo sobre notícia de irregularidade 
- A Comissão Disciplinar Permanente da Corregedoria do TCE-RN é composta por:
- A sindicância ou o processo disciplinar contra membro do Tribunal ou Auditor é instruído p
- A Escola de Contas do TCE-RN, dirigida pelo Conselheiro-Diretor, leva o nome de:
- As demandas recebidas pela Ouvidoria sobre supostas faltas praticadas por membros do Tribu
- Antes da posse do Conselheiro nomeado, o TCE-RN realiza sessão administrativa secreta e de
- A declaração de idoneidade e reputação ilibada exigida do Conselheiro nomeado deve ser fir
- O Conselheiro do TCE-RN deve apresentar relatório acompanhado de voto nos processos que lh
- Os casos de suspeição ou impedimento de Conselheiro não reconhecidos por ele são decididos
- Salvo casos excepcionais apreciados pelo Tribunal, o número máximo de Conselheiros que pod
- Após a Resolução nº 01/2013, as férias individuais dos Conselheiros do TCE-RN não podem se
- Sem prejuízo dos vencimentos, o Conselheiro pode afastar-se de suas funções por casamento 
- Quando o Auditor convocado apresenta voto em sessão, fica assegurada a sua participação at
- Segundo o art. 128 do Regimento, a antiguidade dos Conselheiros é regulada, em primeiro lu
- NÃO é hipótese de vaga do cargo de Conselheiro prevista no art. 129 do Regimento:
- O processo de verificação de invalidez de Conselheiro, para fins de aposentadoria, deve se
- As questões disciplinares envolvendo Conselheiros são decididas pelo TCE-RN em sessão secr
- O Auditor do TCE-RN que não estiver convocado para substituir Conselheiro, segundo a redaç
- É facultativa a presença dos Auditores às sessões do Pleno do TCE-RN, salvo quando convoca
- Segundo o art. 151 do Regimento, os Auditores do TCE-RN:
- Em todos os feitos em que lhe cabe funcionar, o Ministério Público junto ao TCE-RN é o últ
- Se o requerimento de diligência formulado pelo Procurador de Contas antes de emitir o pare
- Ao servidor do TCE-RN, no exercício de suas atividades, é vedado atuar em processo de inte
- A distribuição de processos aos relatores do TCE-RN, Conselheiros e Auditores, obedece aos
- Se o Relator deixar o TCE-RN, as listas de jurisdicionados e os processos que lhe cabiam p
- No TCE-RN, a sustentação oral deve ser requerida ao Presidente do respectivo colegiado até
- No processo perante o TCE-RN, nenhum ato será declarado nulo se do vício não resultar prej
- Por despacho motivado, o Relator pode restringir o acesso aos autos às partes e a seus adv
```

## Pedido 45: Radiocomunicações marítimas, GMDSS, busca e salvamento, AIS e SSAS (`marinha-mercante.radiocomunicacoes-gmdss`, 15 tópicos de edital)

```
Matéria: marinha-mercante.radiocomunicacoes-gmdss (Radiocomunicações marítimas, GMDSS, busca e salvamento, AIS e SSAS)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios das radiocomunicações e operação": 18
- "2. GMDSS: conceito, áreas e funções": 30
- "3. Satélites, localização e informações de segurança": 34
- "4. Busca e salvamento, AIS e SSAS": 18

Já existem (não repita):
- Quanto maior a frequência de uma onda eletromagnética, menor é o seu comprimento de onda.
- Uma onda de 150 MHz, propagando-se à velocidade de 300.000 km/s, tem comprimento de onda d
- A propagação em VHF ocorre essencialmente em linha de visada, e seu alcance depende da alt
- A faixa que permite comunicações a longas distâncias graças à reflexão das ondas na ionosf
- As baterias dos equipamentos rádio do navio constituem fonte de reserva de energia, que de
- Em radiotelefonia VHF, o canal destinado a socorro, urgência, segurança e chamada é o:
- O canal 70 de VHF pode ser usado para chamadas de voz quando o canal 16 estiver congestion
- Um tripulante sofre infarto a bordo, sem que o navio corra perigo. A mensagem adequada par
- O prefixo SÉCURITÉ, repetido três vezes, precede mensagens de segurança, como avisos meteo
- Ao perceber que transmitiu um alerta de socorro DSC por engano, o operador deve:
- Em MF, a frequência de socorro em radiotelefonia é 2.182 kHz, e a de DSC é 2.187,5 kHz.
- O GMDSS foi concebido para que um alerta de socorro seja recebido por autoridades em terra
- A área marítima do GMDSS situada dentro da cobertura de estação costeira de MF com DSC, ma
- A área marítima A4 do GMDSS corresponde às regiões polares, fora da cobertura dos satélite
- A dotação de equipamentos do GMDSS exigida de um navio depende principalmente:
- O MMSI, que identifica a estação do navio nas chamadas DSC, é composto por:
- O alerta de socorro DSC é repetido automaticamente pelo equipamento até que seja reconheci
- O reconhecimento de um alerta de socorro DSC recebido em VHF é feito, normalmente:
- Entre as funções do GMDSS NÃO se inclui:
- Os navios sujeitos ao GMDSS devem manter escuta automática contínua em DSC e, sempre que p
- O sistema INMARSAT utiliza satélites geoestacionários e atende ao GMDSS principalmente na 
- A EPIRB detectada pelo sistema COSPAS-SARSAT transmite seu sinal codificado na frequência 
- A EPIRB de flutuação livre é liberada automaticamente do suporte, quando o navio afunda, p
- O SART radar, ao ser interrogado pelo radar de 9 GHz de um navio, aparece na tela como uma
- O SART radar é detectado pelos radares de navegação que operam na banda S (cerca de 3 GHz)
- O receptor NAVTEX recebe informações de segurança marítima na frequência internacional de:
- No receptor NAVTEX, o operador pode rejeitar qualquer tipo de mensagem, inclusive os aviso
- A sigla NBDP designa:
- O SafetyNET difunde informações de segurança marítima por satélite, alcançando navios fora
- O sistema internacional de satélites que detecta e localiza sinais de radiobalizas de emer
- No Brasil, a coordenação do serviço de busca e salvamento marítimo cabe à:
- A região de busca e salvamento sob responsabilidade do Brasil limita-se ao seu mar territo
- O comandante de um navio é obrigado a prestar socorro a pessoas em perigo no mar, desde qu
- O AIS (Sistema de Identificação Automática) opera na faixa de:
- São dados dinâmicos transmitidos pelo AIS:
- O AIS substitui o radar na prevenção de abalroamentos, pois todas as embarcações são obrig
- Ao ser acionado, o Sistema de Alerta de Proteção do Navio (SSAS):
- O SSAS deve ter pelo menos dois pontos de acionamento, um deles no passadiço, protegidos c
- O comandante pode desligar o AIS quando julgar que mantê-lo ligado compromete a segurança 
- Para a orientação final (homing) dos meios de busca até a radiobaliza, a EPIRB transmite t
```

## Pedido 46: Técnica legislativa, redação de normas e linguagem simples (`processo-legislativo.tecnica-legislativa-linguagem-simples`, 14 tópicos de edital)

```
Matéria: processo-legislativo.tecnica-legislativa-linguagem-simples (Técnica legislativa, redação de normas e linguagem simples)
Capítulos e quantas questões quero em cada um (total 100):
- "Estrutura das leis e planejamento normativo": 20
- "Articulação, clareza, precisão e remissões": 20
- "Alteração, revogação, vigência e consolidação": 20
- "Proposições, pareceres, emendas e controle de qualidade": 20
- "Linguagem simples, direito visual e políticas nacional e estaduais": 20

Já existem (não repita):
- Qual elemento da lei tem a função principal de identificar numericamente a espécie normati
- Em projeto de lei destinado a regulamentar serviços de informação ao cidadão, qual texto p
- O autor de uma proposição precisa informar, logo no início do texto, o assunto e os destin
- Uma lei contém regras de implementação, providências transitórias, cláusula de vigência e 
- Qual alternativa descreve melhor o princípio da unidade de objeto na elaboração de lei, re
- Ao representar os incisos de um artigo na técnica federal da LC nº 95/1998, que sinalizaçã
- Um artigo possui apenas um parágrafo. Qual forma é tecnicamente correta para identificar e
- Em uma cadeia de subdivisões, qual unidade é normalmente utilizada para desdobrar uma alín
- A equipe troca, sem necessidade, a expressão definida como usuário por beneficiário, inter
- Qual remissão interna é, em regra, preferível por reduzir ambiguidades após futuras inserç
- A lei em vigor possui arts. 8º e 9º. Para criar um artigo intermediário sem reorganizar o 
- Um projeto prevê apenas a frase final revogam-se as disposições em contrário. Qual revisão
- Uma consolidação reúne normas da mesma matéria e preserva o conteúdo. Qual providência pod
- Qual afirmação identifica a distinção técnica entre simples compilação editorial e consoli
- Conforme a disciplina da LC nº 95/1998, quando uma lei estabelece período de vacância, em 
- Uma comissão recebe projeto de lei e emite manifestação fundamentada, com análise e conclu
- Uma emenda elimina integralmente um artigo da proposição, sem inserir comando substitutivo
- Em um projeto, a justificação descreve a urgência do problema, mas o articulado não define
- Sobre a Lei nº 15.263/2025, assinale o enunciado que melhor corresponde ao alcance institu
- Qual situação aplica adequadamente as técnicas de linguagem simples previstas na Lei nº 15
- Na estrutura da LC nº 95/1998, a ementa integra a parte preliminar da lei.
- O preâmbulo contém necessariamente todas as obrigações materiais criadas pela lei.
- Excetuadas as codificações, a técnica federal recomenda unidade de objeto e veda matéria e
- Na disciplina federal, a forma ordinária de identificação do décimo artigo é Art. 10º.
- Os parágrafos podem complementar a regra do caput e também expressar exceções.
- Na cadeia federal de articulação, as alíneas são identificadas por algarismos romanos e os
- Para obter precisão, é preferível conservar o mesmo termo para uma categoria jurídica e ev
- A remissão ao dispositivo anterior é, em todos os casos, superior à referência expressa ao
- Em alteração pontual de lei, inserir art. 8º-A pode preservar a numeração dos artigos post
- Em qualquer projeto de consolidação, é proibida até mesmo a reorganização formal dos númer
- A técnica federal exige que a cláusula revogatória enumere expressamente os dispositivos a
- Consolidação permite inovar livremente o direito material, pois a reunião de leis elimina 
- A justificação de uma proposição apresenta razões e fundamentos, mas não dispensa que o ar
- Emenda supressiva tem por finalidade acrescentar dispositivo à proposição em exame.
- Um parecer pode separar relatório, análise e conclusão, respeitadas as competências da com
- A aprovação de emenda por comissão competente sempre corrige eventual vício de iniciativa 
- A Lei nº 15.263/2025 define linguagem simples considerando palavras, estrutura e leiaute a
- A Política Nacional de Linguagem Simples alcança exclusivamente a administração direta da 
- Na comunicação oficial com comunidades indígenas, a lei nacional prevê versão em português
- A Lei estadual nº 18.246/2022, do Ceará, e a Lei Complementar estadual nº 176/2014, do Par
```

## Pedido 47: Neuropsicologia e psicomotricidade (`psicologia.neuropsicologia-psicomotricidade`, 14 tópicos de edital)

```
Matéria: psicologia.neuropsicologia-psicomotricidade (Neuropsicologia e psicomotricidade)
Capítulos e quantas questões quero em cada um (total 100):
- "Bases neurobiológicas do comportamento e desenvolvimento cerebral": 17
- "Atenção, memória, linguagem e funções executivas": 17
- "Avaliação neuropsicológica: processo, instrumentos e interpretação": 17
- "Neurodesenvolvimento e principais perfis neuropsicológicos": 17
- "Reabilitação cognitiva, funcionalidade e intervenção interdisciplinar": 16
- "Psicomotricidade: corpo, lateralidade, tônus e aplicações": 16

Já existem (não repita):
- Na relação entre cérebro e comportamento, qual afirmativa descreve melhor a neuropsicologi
- A participação do córtex pré-frontal em funções executivas significa que todas elas depend
- Que estrutura contribui de forma especialmente relevante para a formação de novas memórias
- A neuroplasticidade admite mudanças funcionais associadas à aprendizagem e a experiências,
- Ao estudar uma tarde inteira, a estudante consegue manter a concentração na tarefa sem int
- A atenção seletiva envolve priorizar estímulos relevantes em meio a distratores.
- Qual exemplo representa melhor a memória semântica?
- A memória de trabalho corresponde exclusivamente à conservação permanente de habilidades m
- Em avaliação neuropsicológica, um percentil 10 de referência adequada indica que o avaliad
- Um rastreador cognitivo isolado pode orientar investigação, mas não é suficiente, por si, 
- Na caracterização do TEA, qual associação contempla os dois conjuntos nucleares de manifes
- Toda pessoa autista apresenta obrigatoriamente deficiência intelectual e ausência de lingu
- Uma pessoa com falhas de memória passa a utilizar alarmes para lembrar horários de medicaç
- A CIF considera atividades, participação e fatores ambientais, além de funções e estrutura
- Qual expressão identifica a organização sensório-motora da representação do próprio corpo 
- Lateralidade e orientação direita-esquerda são expressões rigorosamente sinônimas em psico
- Em uma tarefa complexa, qual interpretação do baixo desempenho seria mais adequada antes d
- Privação de sono e dor podem interferir no desempenho cognitivo sem que isso, isoladamente
- Após ouvir uma série de números, uma pessoa deve repeti-los em ordem inversa. Qual capacid
- Uma dificuldade de nomeação pode decorrer de diferentes processos, de modo que o resultado
- Paciente pronuncia palavras com articulação imprecisa, mas preserva compreensão, nomeação 
- Na avaliação cognitiva, a baixa evocação livre sempre demonstra que a informação não foi a
- O psicólogo encontra um teste de atenção publicado no exterior, traduzido informalmente pa
- Testes psicológicos considerados favoráveis no SATEPSI devem ser usados com observância do
- Uma criança apresenta desatenção apenas durante aulas em língua que ainda está aprendendo.
- Dificuldades escolares persistentes podem justificar investigação, mas não autorizam, por 
- Uma intervenção computacional melhora a velocidade da pessoa no próprio exercício, mas não
- A presença de sintomas de desatenção em uma única situação é, por si só, suficiente para c
- Uma criança prefere escrever com a mão direita e chutar com o pé esquerdo, sem prejuízo id
- Estratégias compensatórias podem ser indicadas mesmo quando determinado processo cognitivo
- Durante um exame motor, o profissional distingue tônus de força. Qual é a descrição corret
- A imagem corporal inclui aspectos subjetivos e afetivos, enquanto o esquema corporal relac
- Um indivíduo preserva habilidades motoras aprendidas, mas após encefalite tem grande dific
- Preservação da aprendizagem procedural associada a dificuldade de memória episódica nova é
- Em tarefa de evocação, a pessoa recorda poucas palavras espontaneamente, mas melhora acent
- Uma lentificação psicomotora pode prejudicar tarefas de funções executivas cronometradas, 
- Após AVC, a pessoa deixa de explorar estímulos do lado esquerdo da folha, embora a avaliaç
- Na negligência espacial unilateral, a redução de resposta a uma região do espaço é necessa
- Uma pontuação z = -1,0 foi obtida em teste com normas adequadas. Qual conclusão é tecnicam
- A inclusão de um instrumento na relação de não privativos do SATEPSI implica, necessariame
- No estudo de possível transtorno do desenvolvimento intelectual, que combinação de dados é
- O funcionamento adaptativo é dispensável na avaliação de possível transtorno do desenvolvi
- Após TCE, paciente melhora na tarefa de planejamento feita em consultório, mas continua in
- A elevação de escores após reaplicação de um teste pode refletir, ao menos em parte, efeit
- Um adolescente conhece a posição de seus membros, mas relata forte sofrimento com sua apar
- O uso preferencial de mão e pé contralaterais constitui critério diagnóstico autônomo de d
- Uma criança indica corretamente a própria mão esquerda, mas erra ao apontar a mão esquerda
- Uma criança pode orientar corretamente o corpo no espaço e ainda ter sofrimento referente 
```

## Pedido 48: Administração de recursos materiais e patrimônio público (`administracao.gestao-publica-orcamento-municipal`, 14 tópicos de edital)

```
Matéria: administracao.gestao-publica-orcamento-municipal (Administração de recursos materiais e patrimônio público)
Capítulos e quantas questões quero em cada um (total 100):
- "Planejamento de compras e suprimentos públicos": 17
- "Estoques: cálculo, reposição e inventário": 17
- "Almoxarifado: recebimento, guarda e distribuição": 17
- "Patrimônio: tombamento, inventário e proteção": 17
- "Depreciação, baixa e destinação de bens": 16
- "Logística pública, proteção patrimonial e controle": 16

Já existem (não repita):
- O planejamento de compras sob a Lei nº 14.133/2021 deve considerar a expectativa de consum
- A existência de preços registrados obriga a Administração a adquirir integralmente as quan
- Na classificação ABC, os itens da classe A são necessariamente os que apresentam mais unid
- Um estoque com 180 unidades disponíveis e consumo médio de 60 unidades por mês apresenta c
- A entrega física de computadores no almoxarifado implica aceitação técnica automática de s
- A requisição de almoxarifado ajuda a documentar unidade requisitante, quantidade entregue,
- Tombamento administrativo de mobiliário e tombamento de patrimônio histórico-cultural desi
- A Lei nº 4.320/1964 prevê registros analíticos de bens permanentes e estabelece relação en
- Um bem totalmente depreciado deve ser necessariamente baixado do patrimônio, mesmo permane
- O Decreto nº 12.785/2025 revogou o Decreto nº 9.373/2018 e excetua os Comandos da Marinha,
- Na disciplina do Decreto nº 12.785/2025, um bem pode ser ocioso mesmo quando está em boas 
- Diferenças entre contagem física e saldo eletrônico devem ser eliminadas por ajuste direto
- A mudança física de uma impressora entre setores dispensa atualização de localização e res
- O desconto de grande volume deve ser avaliado em conjunto com validade, capacidade de depó
- A centralização integral de estoques é sempre a alternativa logística mais eficiente, mesm
- No Decreto nº 12.785/2025, a classificação de bem recuperável depende exclusivamente do cu
- No método linear, um bem de R$ 50.000, residual de R$ 5.000 e vida útil de cinco anos gera
- O Decreto nº 12.785/2025 estabelece automaticamente a rotina de baixa patrimonial de qualq
- A falta de notebook em inventário autoriza cobrar imediatamente seu valor do servidor que 
- Um sistema informatizado de patrimônio dispensa a verificação física dos bens, já que regi
- Uma secretaria solicita nova compra de papel quando há material suficiente em depósito cen
- Chegaram trinta cadeiras ao depósito, mas cinco divergem do modelo especificado. Qual proc
- Uma unidade possui 200 caixas e consome 50 caixas mensais. Qual é a cobertura aproximada, 
- Qual atividade caracteriza o tombamento administrativo de um bem permanente?
- Em inventário, um monitor foi encontrado em sala diferente da cadastrada. Qual providência
- Qual é a razão principal para armazenar objetos pesados nas partes inferiores das estantes
- Em qual norma aparecem os arts. 94 a 96, que tratam de registros e inventários de bens púb
- O consumo é de 6 unidades por dia, o prazo de reposição de 8 dias e a segurança de 12 unid
- Um órgão consumiu 900 unidades em um ano e manteve estoque médio de 150 unidades. Qual foi
- Uma remessa de insumos apresenta lotes com diferentes datas de vencimento. Qual critério d
- Uma unidade encontra equipamento barato para comprar, mas com manutenção dispendiosa e con
- Uma impressora federal está apta e tem uso economicamente vantajoso, mas permanece sem apr
- Uma prefeitura pretende desfazer-se de computadores antigos. Qual afirmação respeita o alc
- A recuperação de um equipamento federal custaria 55% do valor de mercado, mas estudo técni
- Um veículo custa R$ 84.000, tem residual estimado de R$ 4.000 e vida útil de oito anos. Pe
- Durante inventário, a equipe encontra uma cadeira pública na sala, mas não há cadastro cor
- Em um almoxarifado pequeno, há poucos servidores para separar todas as funções. Que contro
- No âmbito federal abrangido pelo Decreto nº 12.785/2025, bens móveis antieconômicos sem al
- Uma autarquia organiza entregas para escolas distantes. Qual elemento deve integrar sua de
- A administração quer contar partes do almoxarifado em dias distintos, monitorando continua
```

## Pedido 49: Gastronomia e cozinha profissional (`gastronomia.cozinha-profissional`, 14 tópicos de edital)

```
Matéria: gastronomia.cozinha-profissional (Gastronomia e cozinha profissional)
Capítulos e quantas questões quero em cada um (total 100):
- "Organização da cozinha profissional e técnicas de pré-preparo": 17
- "Métodos de cocção, fundos, molhos e saborização": 17
- "Ficha técnica, rendimento, custos e gestão da produção": 17
- "Panificação, confeitaria e controle de processos": 17
- "Cozinha brasileira regional, repertório e sustentabilidade": 16
- "Boas práticas, segurança dos alimentos e qualidade do serviço": 16

Já existem (não repita):
- A mise en place inclui organizar ingredientes, equipamentos e tarefas antes do serviço, se
- Na brigada clássica, o pâtissier é o responsável principal pelos molhos salgados, e o sauc
- A brunoise é formada por pequenos cubos uniformes, ao passo que a julienne designa tiras f
- A técnica de cocção a vapor é um exemplo de calor úmido.
- A reação de Maillard e a caramelização são processos idênticos, pois ambas exigem apenas a
- O molho hollandaise é uma emulsão quente cuja estabilidade depende, entre outros fatores, 
- O custo de ingredientes de uma receita dividido pelo número de porções obtidas fornece o c
- Com 9 kg de peso bruto e 7,5 kg de peso líquido, o fator de correção PB/PL é igual a 0,83.
- Um prato de R$ 40 cujo custo de ingredientes é R$ 10 apresenta percentual de custo de alim
- Na panificação, o fermento químico e o biológico são sempre intercambiáveis na mesma quant
- Na porcentagem do padeiro, a farinha é a referência de 100%, mesmo quando os outros ingred
- A fermentação excessiva pode enfraquecer a estabilidade estrutural do pão, mesmo quando a 
- A gastronomia brasileira regional reúne matrizes culturais diversas e não se reduz a uma t
- Na tradição gastronômica capixaba, a moqueca é geralmente caracterizada pelo uso obrigatór
- A preferência por ingrediente local elimina automaticamente impactos ambientais e a necess
- Uma preparação pode estar contaminada por agentes patogênicos mesmo quando apresenta aspec
- O uso de luvas descartáveis permite dispensar a lavagem das mãos na manipulação de aliment
- A existência de Manual de Boas Práticas e de POP comprova, por si só, a inocuidade de cada
- Qual corte culinário corresponde a folhas organizadas em tiras finas, como manjericão para
- Na organização clássica de uma cozinha, quem costuma substituir o chef e coordenar a opera
- Uma cozinha usou uma tábua para frango cru e deseja cortar hortaliças já higienizadas para
- Ao preparar cenouras para uma guarnição com cocção uniforme, qual fator relacionado ao cor
- Durante o serviço, os pratos chegam ao passe em tempos muito diferentes. Qual intervenção 
- Qual método usa predominantemente calor seco?
- Que preparação clássica é feita a partir de leite espessado com roux?
- Qual técnica é especialmente adequada a um corte de carne mais firme, rico em tecido conju
- Qual alternativa diferencia corretamente um roux de uma redução?
- Uma hollandaise começou a separar após aquecimento excessivo. Qual hipótese técnica é mais
- Uma receita consome R$ 75 em ingredientes e rende 15 porções. Qual é o custo dos ingredien
- Qual documento reúne insumos, gramagens, etapas, rendimento e padrão de montagem de um pra
- Uma preparação exige 8 kg de hortaliça limpa, e o fator de correção é 1,25. Qual peso brut
- Um prato custa R$ 12 em ingredientes e é vendido por R$ 48. Qual é o percentual de custo d
- Um restaurante recebeu lote com vencimento anterior ao de outro lote já estocado. Qual cri
- Em panificação, a fermentação biológica de leveduras contribui principalmente para qual fe
- Qual ingrediente é tomado como 100% na porcentagem do padeiro?
- Uma fórmula contém 2 kg de farinha e 1,4 kg de água. Qual é a hidratação percentual?
- Qual processo é típico do método espumoso em bolos do tipo pão de ló?
- Um pão apresenta miolo denso e expansão insatisfatória. Qual diagnóstico é tecnicamente pl
- Qual ingrediente é especialmente associado a preparações tradicionais do Norte como tacacá
- Qual prato é fortemente associado à culinária mineira?
- No preparo tradicional do acarajé baiano, qual base é especialmente característica?
- Um restaurante quer formular cardápio regional com menor desperdício. Qual decisão concili
- Ao comparar moqueca baiana e capixaba, qual afirmação é mais precisa?
- Qual exemplo representa um perigo físico para o alimento?
- Para reduzir contaminação cruzada em cozinha profissional, a conduta correta é:
- Segundo a cartilha de Boas Práticas da Anvisa, quais controles integram os POP ali destaca
- Um cozinheiro percebe que uma panela grande de alimento pronto ficará horas esfriando sobr
- Um lote exposto de forma insegura à temperatura ambiente será servido após reaquecimento. 
```

## Pedido 50: Engenharia de tráfego e transportes (`engenharia.engenharia-trafego-transportes`, 13 tópicos de edital)

```
Matéria: engenharia.engenharia-trafego-transportes (Engenharia de tráfego e transportes)
Capítulos e quantas questões quero em cada um (total 100):
- "Estudos de tráfego: dados, pesquisas e indicadores": 17
- "Capacidade viária, fluxo e nível de serviço": 17
- "Semáforos e operação de interseções": 17
- "Sinalização viária e segurança dos deslocamentos": 17
- "Transporte público e planejamento de transportes": 16
- "Mobilidade urbana, acessibilidade e política nacional": 16

Já existem (não repita):
- Um posto contou 420 veículos em 21 minutos de operação normal. Qual é a taxa horária equiv
- Uma contagem contínua das 7h às 8h registrou, em quartos de hora consecutivos, 120, 180, 2
- A prefeitura quer redesenhar linhas de ônibus para atender deslocamentos entre bairros. Qu
- Em um estudo de fluxo estável, a equipe adotou velocidade média espacial de 36 km/h e dens
- A quantidade de veículos que atravessou determinada seção em vinte minutos é um volume obs
- Uma contagem manual realizada em uma única hora de um dia útil pode ser multiplicada por 2
- Dois trechos com igual volume de veículos devem necessariamente apresentar a mesma velocid
- O FHP mede a concentração temporal dentro da hora observada, e seu valor é igual a 1 quand
- No estudo operacional de um corredor, o engenheiro encontrou demanda estimada de 1.500 veí
- Para um grupo semaforizado, adote fluxo de saturação de 1.800 veículos/h, verde efetivo de
- Em uma avenida, a demanda excede a capacidade de uma interseção a jusante; o contador inst
- Um avaliador deseja comparar o nível de serviço de uma interseção semaforizada com o de um
- Capacidade viária corresponde à maior taxa de atendimento sob condições definidas e não ao
- Se a relação entre demanda e capacidade de uma aproximação semaforizada for de 0,80, não h
- Na relação q = k × v, é possível manter unidades coerentes de veículos/km/faixa e km/h par
- O nível de serviço F em qualquer tipo de instalação viária é definido universalmente por u
- A principal função da defasagem entre semáforos consecutivos de um corredor urbano é:
- Um semáforo possui ciclo de 100 s, verde efetivo de 45 s para certo grupo de faixas e flux
- Em um corredor de ônibus com detecção de aproximação, a autoridade decide prolongar o verd
- Uma proposta para reduzir atrasos em cruzamento isolado usa a fórmula clássica C = (1,5L +
- No controle semafórico por tempo fixo, a sequência e as durações seguem planos predefinido
- Verde exibido e verde efetivo são sempre numericamente idênticos, motivo pelo qual qualque
- Prolongar o ciclo semafórico tende a reduzir a participação relativa das perdas por partid
- Uma onda verde bem dimensionada garante progressão sem qualquer parada simultaneamente a t
- Uma placa que determina parada obrigatória aos condutores pertence à categoria de sinaliza
- Ao examinar uma travessia com placas parcialmente encobertas e marcas horizontais apagadas
- Um órgão irá sinalizar uma rota cicloviária e um cruzamento operado por semáforos. Quais v
- Ao comparar número absoluto de sinistros entre duas vias, uma com dez vezes mais tráfego q
- A sinalização vertical de advertência alerta para condições de risco, enquanto a de regula
- Uma rampa acessível isolada é suficiente para tornar universalmente acessível uma viagem q
- O sistema de sinalização deve ser coerente com a geometria, a visibilidade e as condições 
- Em estudos de engenharia de tráfego, o Manual Brasileiro de Sinalização de Trânsito é rele
- Uma linha urbana programa 5 partidas a cada hora, distribuídas de forma regular. Qual é o 
- Uma linha opera ida, volta e regulação em ciclo de 96 minutos e deseja manter 8 minutos en
- Um corredor prevê integração física entre linhas alimentadoras e troncais, mas não muda as
- Para atender demanda de pico de 900 passageiros/h no sentido dominante, suponha capacidade
- Uma linha com frequência regular de seis veículos por hora apresenta intervalo médio progr
- Integração operacional, integração física e integração tarifária designam exatamente o mes
- Ao estimar tempo porta a porta no transporte coletivo, devem ser incluídos acesso ao ponto
- O planejamento de uma rede de ônibus pode prescindir de dados de origem e destino porque u
- Segundo diretriz expressa da Política Nacional de Mobilidade Urbana, o planejamento deve d
- Qual dos itens abaixo integra expressamente o conteúdo do Plano de Mobilidade Urbana previ
- Em outubro de 2026, um parecer afirma que os arts. 8º, 9º e 10 da Lei nº 12.587/2012 já fo
- Considerando o art. 24 da PNMU, qual afirmação descreve corretamente um critério legal de 
- A PNMU estabelece a gestão democrática e o controle social entre seus princípios, além de 
- A necessidade do Plano de Mobilidade Urbana decorre exclusivamente de o município ter mais
- Segundo a PNMU, no município sem sistema de transporte público coletivo ou individual, o P
- A aprovação do Plano de Mobilidade Urbana deve ignorar o plano diretor municipal, pois mob
```

