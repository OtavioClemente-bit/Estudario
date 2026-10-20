# Pedidos de questões extras, lote 16 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 4757 tópicos de edital.

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

## Pedido 1: Ondas e som (`fisica.ondas-som`, 104 tópicos de edital)

```
Matéria: fisica.ondas-som (Ondas e som)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza das ondas, grandezas, equação e energia": 17
- "2. Reflexão, refração, interferência, difração e ondas estacionárias": 21
- "3. Luz e ondas eletromagnéticas: interferência, difração e polarização": 21
- "4. Som, ultrassom, qualidade auditiva e efeito Doppler": 24
- "5. Aplicações integradas: ressonância, acústica, ondas luminosas e análise de situações": 17

Já existem (não repita):
- Uma onda percorre uma corda com frequência de 6 Hz e comprimento de onda de 0,50 m. Qual é
- Uma sonda espacial emite luz para comunicar-se com a Terra enquanto está no vácuo interpla
- Uma onda sonora atravessa o ar de um corredor. Em relação ao deslocamento médio das partíc
- Um sensor registra uma oscilação completa a cada 0,005 s. Qual é a frequência dessa onda p
- Uma estudante envia um pulso positivo ao longo de uma corda cuja extremidade distante está
- Um clarinete toca determinada nota e o músico aumenta a intensidade sonora sem mudar a not
- Uma onda luminosa atravessa perpendicularmente a superfície que separa ar e vidro transpar
- Uma abertura deixa passar ondas de água. Ao reduzir sua largura até ficar comparável ao co
- Uma corda de comprimento 1,0 m está fixa nas duas extremidades. Com velocidade de onda de 
- No experimento de Young, mantendo comprimento de onda e distância até a tela, a distância 
- Uma fonte pontual ideal irradia som uniformemente em campo livre. Se o ouvinte aumenta a d
- Duas fontes sonoras independentes produzem, cada uma, 60 dB de nível de intensidade no mes
- Uma onda atravessa uma interface estacionária. Sua rapidez diminui de 12 m/s para 8 m/s, e
- Na difração de luz por uma única fenda, a largura da abertura é reduzida, mantendo-se o me
- Um sonar emite pulso que volta depois de 0,20 s. Considere velocidade do som na água igual
- Um violino e uma flauta emitem a mesma frequência fundamental e intensidades médias semelh
- Uma sirene de 500 Hz se aproxima em linha reta de observador parado a 34 m/s, em ar onde o
- Duas fendas coerentes e iluminadas em fase produzem, num ponto da tela, diferença de camin
- Luz não polarizada, de intensidade 100 unidades, atravessa polarizador ideal e depois outr
- Uma película fina está entre ar e um substrato, sofrendo uma única inversão relativa de fa
- Um tubo acústico ideal de 0,85 m tem uma extremidade fechada e outra aberta. Adote rapidez
- Um laboratório mede 80 dB de nível de intensidade numa posição e 50 dB em outra, com mesma
- Num experimento de Young, luz de 600 nm ilumina duas fendas separadas por 0,30 mm, diante 
- Uma sala apresenta fala pouco inteligível por reflexões demoradas. Uma pessoa propõe placa
- Um transdutor emite ultrassom de 2,0 MHz em um meio onde a rapidez é 1.500 m/s. O comprime
- Uma bóia oscila para cima e para baixo enquanto cristas de uma onda de superfície passam p
- Uma comunicação luminosa no vácuo exige moléculas de ar para sustentar sucessivas compress
- Numa corda homogênea sob tensão constante, dobrar a frequência da fonte tende a reduzir pe
- Um pulso que atinge a extremidade rigidamente fixa de uma corda ideal retorna refletido se
- Uma luz monocromática passa do ar ao vidro por interface estacionária. A frequência é mant
- Na comparação de dois sons, elevar a intensidade mantendo a frequência fundamental signifi
- Duas ondas coerentes em fase podem interferir construtivamente em pontos nos quais chegam 
- Uma corda ideal tem a tensão quadruplicada enquanto sua densidade linear de massa é mantid
- Numa onda estacionária ideal sobre uma corda, dois nós consecutivos estão separados por me
- Ao passar da água para o ar por interface imóvel, o som passa a ter frequência menor exclu
- Dois sons diferem em 20 dB de nível de intensidade, medidos com a mesma referência. O som 
- Um eco é recebido 0,30 s depois da emissão do pulso, num meio com velocidade de 340 m/s. O
- Um experimento de duas fendas com fontes coerentes pode produzir máximos de interferência 
- Dois polarizadores ideais com eixos perpendiculares transmitem metade da intensidade de um
- No padrão de uma película fina iluminada quase perpendicularmente, uma única inversão rela
- Em tubo ideal fechado em uma ponta e aberto na outra, os modos próprios da coluna de ar in
- Uma sirene que se aproxima de observador parado em ar tranquilo produz frequência recebida
- Duas fontes sonoras independentes de 60 dB cada, contribuindo com igual intensidade no rec
- Para fonte pontual ideal que irradia isotropicamente em campo livre, dobrar a distância ao
- Na difração por fenda simples, mantendo luz monocromática, diminuir a largura da fenda apr
```

## Pedido 2: Análise de negócios, design thinking e inovação em TI (`informatica.analise-negocios-inovacao-ti`, 104 tópicos de edital)

```
Matéria: informatica.analise-negocios-inovacao-ti (Análise de negócios, design thinking e inovação em TI)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Análise de negócios: necessidades, requisitos e valor": 26
- "2. Gerenciamento de produtos: estratégia, descoberta e priorização": 14
- "3. Design thinking, prototipação e testes com usuários": 12
- "4. Design de serviços e experiência do consumidor": 28
- "5. OKRs, inovação e integração entre análise, produto e serviço": 20

Já existem (não repita):
- Uma equipe pretende reduzir o número de solicitações devolvidas por informações incompleta
- Uma aplicação já permite consultar solicitações por número. O novo requisito exige que 95%
- Na substituição de um sistema, será preciso converter uma vez os cadastros antigos para o 
- Uma especificação está clara, sem contradições e permite testes, mas a pesquisa revela que
- Em uma equipe que utiliza Scrum, diferentes interessados pedem alterações na ordem dos ite
- Antes de criar uma nova função de pagamento, uma equipe entrevista pessoas, analisa abando
- No Scrum Guide de 2020, uma equipe estabelece que seu produto permitirá aos usuários concl
- Uma equipe de inovação pesquisa amplamente dificuldades de atendimento, sintetiza um desaf
- Uma equipe quer testar se as pessoas reconhecem a ordem dos passos de um cadastro, sem inv
- Uma equipe quer comprovar se um algoritmo experimental consegue integrar dois formatos de 
- Uma pessoa precisa consultar um serviço, preencher o pedido, receber uma mensagem e acompa
- Uma jornada mostra que pedidos atrasam. A equipe acrescenta ações visíveis de atendimento,
- Uma pesquisa de recomendação apresenta 50% de promotores (notas 9–10), 30% de neutros (7–8
- Uma equipe quer avaliar como as pessoas percebem a dificuldade de resolver um problema no 
- O objetivo de um ciclo é melhorar a autonomia no atendimento digital. Qual enunciado const
- Após uma iteração, uma equipe informa que criou oito telas, mas não apresenta evidências d
- Uma equipe fez uma simulação em papel para avaliar a compreensão de um cadastro. Depois, d
- Após alteração de regra de negócio, um requisito de consulta foi revisto. A equipe precisa
- Para definir uma persona de pessoas que necessitam de atendimento assistido, uma equipe an
- Em teste de usabilidade, uma pessoa hesita diante de um botão. O moderador aponta o caminh
- No blueprint de um serviço, a pessoa vê o atendente confirmar seu pedido, mas não vê a con
- Um aplicativo recebeu boas avaliações sobre a facilidade de uso da tela inicial, mas a exp
- Uma equipe definiu como KR reduzir tempo médio de atendimento, mas a análise revelou fecha
- Uma equipe aplica RICE e compara duas funcionalidades. A primeira possui alcance muito est
- Uma organização quer aumentar a conclusão de pedidos digitais. A jornada revela abandono n
- Ao investigar a demora em um processo, a equipe registra os objetivos da mudança e procura
- Um requisito que estabelece tempo máximo de resposta para uma pesquisa em plataforma é fun
- Uma equipe utiliza o Product Backlog para organizar e refinar trabalho potencial do produt
- No Duplo Diamante, a fase Desenvolver antecede a fase Descobrir, porque primeiro se produz
- Uma equipe usa papel e cartões para simular a sequência de atendimento, observando as dúvi
- Na gestão de serviços, um mapa de jornada e um service blueprint são equivalentes, pois am
- Uma equipe descreve a conversão de base histórica, necessária apenas antes da implantação 
- Uma especificação bem escrita, consistente e testável está necessariamente validada para o
- A entrega de uma funcionalidade corresponde a output, enquanto a mudança na taxa de conclu
- Uma prova de conceito que comprova a integração de dois bancos de dados demonstra, por si 
- No blueprint de um serviço, uma conferência feita internamente e invisível à pessoa atendi
- Em pesquisa NPS com 40% de promotores, 35% de neutros e 25% de detratores, o índice é 75 p
- Em um OKR, aumentar de 55% para 80% a conclusão autônoma de solicitações pode ser resultad
- Uma pesquisa de satisfação alcançou apenas pessoas que concluíram com êxito determinada jo
- O Product Owner pode delegar a terceiros a criação de itens e outras atividades de gerenci
- No teste de usabilidade de um protótipo, um moderador informou qual opção deveria ser esco
- Se um OKR procura reduzir o tempo de resposta, acompanhar também a taxa de reabertura de s
- O acompanhamento permanente da disponibilidade de uma plataforma é incompatível com OKRs, 
- A jornada de um usuário pode indicar demora após o envio de solicitação, e o blueprint pod
- Uma equipe publicou novas telas e atingiu a meta interna de entregas, mas não acompanhou u
```

## Pedido 3: CVM, regulação do mercado de valores mobiliários, ofertas e fundos (`mercado-capitais.cvm-regulacao-ofertas-fundos`, 104 tópicos de edital)

```
Matéria: mercado-capitais.cvm-regulacao-ofertas-fundos (CVM, regulação do mercado de valores mobiliários, ofertas e fundos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. CVM, valores mobiliários e fundamentos regulatórios": 22
- "2. Participantes, mercados e companhias abertas": 22
- "3. Ofertas públicas, plataformas e BDR": 19
- "4. Fundos: classes, gestão, resgates e insolvência": 20
- "5. Securitização, CRI, CRA e deveres integrados": 17

Já existem (não repita):
- Uma empresa apresentou publicamente contrato que remunera investidores com resultado de em
- Um investidor afirmou que uma oferta registrada pela CVM já teria rendimento assegurado. Q
- Ao negociar ações já emitidas entre dois investidores, sem nova subscrição pela companhia,
- Um grupo precisa identificar a norma da CVM que trata da organização das entidades adminis
- Uma companhia passou a divulgar seus documentos periódicos de acordo com o regime de emiss
- Em determinada instituição, uma pessoa toma decisões de compra para a carteira administrad
- Uma pequena sociedade pretende captar recursos de investidores em plataforma eletrônica so
- Um fundo possui administrador fiduciário, gestor, custodiante e distribuidor. Na estrutura
- Um certificado de depósito foi emitido no Brasil com lastro em valor mobiliário de empresa
- Uma classe de fundo admite subclasses com taxas e condições de distribuição distintas. Qua
- Uma sociedade registrada na categoria B pretende negociar ações de sua emissão com o mesmo
- Uma oferta pública foi enquadrada em procedimento de registro automático da Resolução CVM 
- O administrador de fundo informou aos cotistas que efetuaria pagamento de parcela do valor
- Uma securitizadora emite certificados de recebíveis e precisa formalizar características, 
- Uma autarquia federal prepara regulação econômica de interesse geral e discute alternativa
- Uma pessoa recebe recomendações de produto financeiro sem avaliação da compatibilidade com
- Um empreendedor compara a Resolução CVM nº 88/2022 com a Resolução CVM nº 160/2022 para ca
- Uma emissão de certificados de recebíveis estabelece classe sênior com preferência sobre s
- Uma securitizadora divide a emissão de certificados em diferentes séries. Segundo a Lei nº
- Um BDR é negociado em reais e tem lastro em título estrangeiro sujeito a oscilações de pre
- Uma classe de fundo com responsabilidade limitada apura patrimônio líquido negativo e adot
- Uma companhia anuncia aquisição de créditos comerciais de origem variada para lastrear tít
- Ao instituir regime fiduciário em emissão de certificados, uma securitizadora registra cor
- Uma companhia registra emissão de certificados com termo de securitização completo, mas de
- Um investidor compara ação de companhia aberta, cota de FIDC e certificado de recebíveis. 
- Uma companhia realiza nova emissão de debêntures e recebe o dinheiro pago pelos subscritor
- Uma corretora que executa ordem de compra de ações passa, por causa dessa operação, a ser 
- Uma empresa inscrita como emissora registrada deve atender às informações periódicas exigi
- A aplicação em BDR negociado no Brasil fica protegida de mudanças do preço do ativo estran
- Um fundo possui administrador fiduciário e gestor de recursos contratados para exercer fun
- Uma securitizadora pode chamar de CRA qualquer certificado lastreado em dívidas comerciais
- Uma entidade administradora de mercado organizado realiza supervisão do ambiente de negoci
- Uma oferta registrada automaticamente fica dispensada de prestar informações aos investido
- Uma sociedade de pequeno porte que utiliza plataforma de financiamento participativo pode 
- Duas subclasses de uma classe de fundo cobram taxas distintas; por isso, cada subclasse ne
- O pagamento de amortização de cotas pode devolver parcela de seu valor ao investidor sem r
- A existência de termo de securitização permite suprimir toda informação exigida na oferta 
- Na emissão de certificados de recebíveis, o termo pode estabelecer prioridade de pagamento
- Uma autarquia propõe norma de interesse geral e deixa de fazer análise de impacto regulató
- Um BDR não patrocinado é oferecido no Brasil. Mesmo sem o patrocínio do emissor estrangeir
- Em oferta secundária de valores mobiliários de companhia aberta, o valor pago pelo adquire
- Os certificados de recebíveis de uma mesma emissão podem ter diferentes classes ou séries 
- Quando uma classe de responsabilidade limitada de fundo perde valor de mercado, a mera var
- O regime fiduciário devidamente instituído para certificados de recebíveis separa bens e d
- A análise de adequação ao perfil de um cliente é dispensável sempre que um valor mobiliári
```

## Pedido 4: Fisiologia do exercício, treinamento e medidas e avaliação (`educacao-fisica.fisiologia-exercicio-treinamento`, 103 tópicos de edital)

```
Matéria: educacao-fisica.fisiologia-exercicio-treinamento (Fisiologia do exercício, treinamento e medidas e avaliação)
Capítulos e quantas questões quero em cada um (total 100):
- "Bioenergética e respostas metabólicas ao esforço": 20
- "Respostas agudas e adaptações cardiovasculares, respiratórias e musculares": 20
- "Princípios do treinamento esportivo e capacidades físicas": 20
- "Crescimento, desenvolvimento motor, testes, medidas e avaliação": 20
- "Atividade física, saúde, riscos e primeiros socorros": 20

Já existem (não repita):
- Durante salto vertical máximo, qual fonte fornece ATP com a maior potência instantânea nos
- Em um esforço máximo de cinco segundos, apenas o sistema ATP–fosfocreatina produz ATP, sem
- Em uma corrida contínua longa e submáxima, qual característica melhor descreve o sistema c
- A ressíntese de fosfocreatina durante pausas de recuperação depende em grande parte de pro
- Um avaliador identifica lactato elevado após tiros de 400 m. Qual interpretação é tecnicam
- O lactato produzido em exercícios intensos é obrigatoriamente eliminado como resíduo, pois
- Dois corredores apresentam VO₂máx semelhante, mas diferentes velocidades em prova de resis
- O consumo excessivo de oxigênio após o exercício pode refletir reposição de reservas energ
- O que define o débito cardíaco durante o exercício?
- A ventilação-minuto pode ser calculada multiplicando-se o volume corrente pela frequência 
- Após meses de treinamento aeróbio, uma pessoa apresenta FC menor ao pedalar na mesma potên
- Uma redução da FC em uma carga fixa após treinamento aeróbio prova obrigatoriamente diminu
- Qual fenômeno caracteriza adaptação periférica ao treinamento de resistência?
- Alterações de recrutamento e coordenação neural podem elevar força antes de hipertrofia mu
- Considere FC de 150 bpm e volume sistólico de 100 mL por batimento. Qual débito cardíaco a
- Fibras musculares tipo I apresentam, em geral, maior resistência à fadiga e perfil oxidati
- Qual princípio prevê que ganhos de adaptação diminuam após interrupção prolongada do estím
- Treino específico significa que nenhuma atividade complementar pode beneficiar desempenho 
- Uma pessoa percorre 5 km ao mesmo ritmo de outra, mas relata PSE muito mais alta. Que conc
- A sobrecarga progressiva exige que o praticante treine até falha máxima em toda sessão, in
- Para aumentar a potência mecânica de um movimento, que relação física é diretamente aplicá
- Velocidade de execução, número de séries e intervalos de descanso podem alterar o estímulo
- Uma equipe organiza quatro semanas de estímulo, recuperação e progressão orientadas a um o
- Dois praticantes com carga externa idêntica obrigatoriamente terão carga interna e percepç
- Que termo corresponde ao resultado quantitativo obtido após aplicar um protocolo padroniza
- O teste representa um procedimento, enquanto avaliação envolve interpretar resultados para
- Dois jovens de 14 anos têm desempenhos físicos distintos. Qual informação impede atribuir 
- A mesma idade cronológica garante o mesmo ritmo de maturação e desenvolvimento motor entre
- Qual é a principal limitação do IMC em avaliação física individual?
- A alta confiabilidade de um instrumento, isoladamente, não comprova que ele mede corretame
- Um dinamômetro apresenta praticamente o mesmo erro de +4 kg em várias repetições. Qual int
- O teste de caminhada de seis minutos mede diretamente o VO₂máx com a mesma validade de uma
- Qual recomendação aeróbia semanal geral se aplica a adultos segundo diretrizes de saúde pú
- Cumprir as metas semanais de exercício impede, por definição, que alguém apresente tempo s
- Em corrida sob forte calor, praticante torna-se confuso e cambaleante. Qual resposta é mai
- Para adultos, a recomendação geral inclui fortalecimento muscular em pelo menos dois dias 
- Adulto cai subitamente inconsciente e apresenta somente respiração agônica. Após garantir 
- Na suspeita de parada cardíaca de adulto, o DEA deve ser aguardado sem compressões, pois o
- Qual afirmação representa corretamente a relação entre exercício e comportamento sedentári
- Confusão mental durante exercício em ambiente muito quente pode indicar golpe de calor mes
```

## Pedido 5: Nulidades e exceções no processo do trabalho (`processo-trabalho.nulidades-excecoes`, 103 tópicos de edital)

```
Matéria: processo-trabalho.nulidades-excecoes (Nulidades e exceções no processo do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Nulidades, prejuízo, arguição, saneamento e efeitos": 29
- "2. Exceção de incompetência territorial e recorribilidade incidental": 17
- "3. Impedimento, suspeição e imparcialidade judicial": 19
- "4. Poderes do juiz, direção da audiência e prevenção de nulidades": 17
- "5. Honorários advocatícios, justiça gratuita, preclusão e casos integrados": 18

Já existem (não repita):
- Durante a audiência, uma incorreção formal na ata foi corrigida imediatamente, e nenhuma o
- Uma parte conhece irregularidade relativa que afetou sua manifestação, mas terá oportunida
- Uma empresa foi notificada da ação e pretende questionar exclusivamente o foro territorial
- Protocolada tempestivamente a exceção de incompetência territorial antes da audiência trab
- No mesmo processo, o magistrado já havia atuado como advogado de uma das partes antes de p
- A parte demonstra que o magistrado mantém amizade íntima com seu adversário, circunstância
- O magistrado determina a juntada de documento necessário ao esclarecimento de ponto contro
- Ao fixar honorários advocatícios sucumbenciais no processo trabalhista, qual intervalo per
- Após pedido de correção de falha procedimental, o juiz constata ser possível repetir o ato
- O tribunal reconheceu nulidade numa audiência, mas documentos anteriores haviam sido junta
- Após ser oposta exceção territorial, os autos são conclusos para manifestação da parte que
- Na exceção territorial, o magistrado considera necessária prova oral, e as testemunhas do 
- Uma exceção territorial é acolhida e a ação remetida a unidade vinculada a Tribunal Region
- Uma parte descobre que o juiz aconselhou privadamente sua adversária sobre o objeto do pro
- Diante de alegação de impedimento, o magistrado reconhece sua situação de enquadramento no
- O juiz indefere, com fundamentação, testemunha destinada a repetir fato já incontroverso. 
- Dois pedidos autônomos tiveram resultados opostos, e há sucumbência recíproca. O juízo pre
- A parte provocou irregularidade de intimação para obter vantagem e, depois, pretende anula
- A exceção territorial foi acolhida, mas o processo seguirá em cidade diferente vinculada a
- Uma parte suscita suspeição de magistrado somente depois de atuar reiteradamente no feito 
- No procedimento do art. 146 do CPC, o magistrado não reconhece o impedimento suscitado e r
- Uma testemunha foi indeferida sem motivação embora seu depoimento fosse relevante a fato c
- No julgamento de pedidos distintos, houve sucumbência recíproca. Um litigante com justiça 
- A sentença fixou honorários sucumbenciais de 18%, contrariando o intervalo da CLT, mas tod
- Uma decisão acolhe exceção territorial e determina remessa a outro Tribunal Regional. A pa
- O art. 794 da CLT exige manifesto prejuízo às partes para que se reconheça nulidade proces
- No processo trabalhista, a parte pode aguardar o trânsito em julgado para arguir nulidade 
- A exceção de incompetência territorial é apresentada, pelo art. 800 da CLT, em cinco dias 
- Após a apresentação regular da exceção territorial, a audiência prossegue normalmente; ape
- Na disciplina do CPC aplicada de forma compatível, a atuação anterior do próprio magistrad
- O art. 791-A, caput, da CLT fixa honorários advocatícios sucumbenciais obrigatoriamente en
- Os arts. 797 e 798 da CLT autorizam preservar ato posterior cronologicamente ao vício quan
- De acordo com o art. 800, § 2º, da CLT, o reclamante tem apenas vinte e quatro horas para 
- Se o juízo entender necessária prova oral sobre competência territorial, o art. 800, § 3º,
- Uma decisão que rejeita exceção territorial abre, apenas por ser interlocutória, a mesma h
- O art. 145, § 1º, do CPC permite ao magistrado declarar suspeição por foro íntimo sem nece
- O art. 765 da CLT permite ao juiz indeferir sem fundamentação qualquer prova solicitada, p
- A CLT, art. 791-A, § 3º, prevê sucumbência recíproca em procedência parcial e proíbe compe
- Uma parte provoca deliberadamente a hostilidade do juiz e, após a provocação, alega suspei
- O art. 146, §§ 6º e 7º, do CPC determina que o tribunal, reconhecendo impedimento ou suspe
- A CLT, art. 795, § 1º, ao mencionar literalmente incompetência de foro declarada de ofício
- A CLT, art. 802, contém referência literal à designação de audiência dentro de quarenta e 
- O poder de manter a ordem em audiência previsto no art. 816 da CLT autoriza retirar arbitr
- Após a ADI 5766, a obtenção de créditos judiciais por beneficiário da justiça gratuita não
- A fixação de honorários de sucumbência em percentual acima do teto do art. 791-A produz ne
```

## Pedido 6: Convênios e benefícios fiscais do ICMS (`direito-tributario.convenios-beneficios-fiscais`, 103 tópicos de edital)

```
Matéria: direito-tributario.convenios-beneficios-fiscais (Convênios e benefícios fiscais do ICMS)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Competência do ICMS e espécies de benefícios": 15
- "2. LC 24/1975: formação, aprovação e ratificação dos convênios": 21
- "3. Guerra fiscal, invalidade e jurisprudência": 14
- "4. LC 160/2017: remissão, reinstituição e transparência": 23
- "5. Incentivos regionais, prazos e casos integrados": 27

Já existem (não repita):
- Uma unidade federada pretende conceder isenção de ICMS a um setor econômico. Qual disposit
- Em um incentivo estadual, a norma manda tributar apenas metade do valor de uma operação su
- Durante reunião regularmente convocada, dezoito unidades federadas estão representadas. Qu
- Em reunião com vinte representantes estaduais e distrital, discute-se revogação de benefíc
- Após a publicação de um convênio, determinado ente federado deixa passar os quinze dias do
- Uma reunião de convênios do ICMS ocorre com quatorze das vinte e sete unidades federadas r
- Uma ata registra o encerramento de reunião na qual foi aprovado convênio de isenção de ICM
- Um convênio autoriza que estados interessados instituam redução de base de cálculo do ICMS
- A legislação de uma unidade federada resolve perdoar crédito de ICMS já surgido em relação
- Um programa de regularização pretende remitir passivos e reinstituir incentivos de ICMS co
- Um convênio já teve o resultado de ratificação publicado na forma do art. 5º da LC 24/1975
- Um convênio concessivo alcançou unanimidade dos representantes presentes, mas uma unidade 
- Uma unidade institui crédito presumido de ICMS sem convênio válido, e a mercadoria é adqui
- Um programa estadual desloca o vencimento do ICMS para momento posterior, mas mantém integ
- Na votação de convênio de regularização da LC 160/2017, foram obtidos dezoito votos favorá
- Um incentivo estadual de 2016 pretende ser alcançado pelo regime da LC 160/2017. O estado 
- A lei de origem concede remissão de créditos vinculados a incentivos irregulares que preen
- Uma unidade federada deseja aderir a incentivo de ICMS antigo reinstituído por outra unida
- Em reunião com vinte unidades representadas, a revogação de benefício de ICMS obtém dezess
- Um convênio para remissão de incentivos antigos recebeu dezenove votos favoráveis, mas uma
- Em 2026, decreto de um estado limitou a duração de isenção de ICMS já pactuada em convênio
- Uma unidade reinstituiu benefício comercial abrangido pela LC 160 e pretende mantê-lo dura
- Uma regra de reinstituição alcança benefício para operações interestaduais com produtos ag
- Um estado busca reinstituir benefício de ICMS criado irregularmente em 2015, mas completam
- Uma operação interestadual beneficiou-se de crédito presumido irregular em 2016. O estado 
- Ao orientar uma empresa, um consultor observa que a LC 24/1975 alcança a concessão de créd
- Uma reunião com vinte representantes aprovou isenção de ICMS por dezesseis votos. Por corr
- Após a publicação de convênio de ICMS, um ente federado não emite decreto de ratificação n
- Um estado publica convênio permissivo de isenção de ICMS e conclui que a desoneração entra
- Uma lei estadual concede perdão de crédito de ICMS devido por fato passado. Essa providênc
- Um convênio de remissão e reinstituição regido pela LC 160/2017 somente pode ser aprovado 
- Uma deliberação excepcional da LC 160/2017 obteve dezoito votos favoráveis entre as vinte 
- Um incentivo de ICMS concedido irregularmente em 2016 é considerado regularizado pela LC 1
- O estado de origem concede remissão de crédito decorrente de benefício irregular devidamen
- A adesão prevista no art. 3º, § 8º, da LC 160/2017 permite a qualquer unidade federada rep
- Para benefício comercial abrangido pelo art. 3º, § 2º, III, da LC 160/2017, a LC 186/2021 
- Um diferimento que apenas desloca o vencimento de ICMS, conservando integralmente o impost
- Pelo art. 7º da LC 24/1975, os convênios ratificados obrigam também as unidades federadas 
- Em setembro de 2026, o julgamento das ADIs 7.822, 7.830, 7.844 e 7.848 reconheceu a consti
- Um incentivo reinstituído para atividade industrial se enquadra no art. 3º, § 2º, I, da LC
- Por ter instituído redução anual de 20% do direito de fruição, o art. 3º, § 2º-A, da LC 16
- Uma lei da unidade de origem concede remissão nas condições da LC 160/2017. Esse ato pode 
- Um convênio ordinário teve a primeira deliberação publicada hoje. Pela LC 24/1975, sua ent
- Em benefício reinstituído, um estado pode estender a fruição a outros contribuintes de seu
- Um incentivo instituído irregularmente antes de agosto de 2017 foi extinto antes de começa
```

## Pedido 7: Pensamento geográfico e ensino de Geografia (`geografia.pensamento-geografico-ensino`, 102 tópicos de edital)

```
Matéria: geografia.pensamento-geografico-ensino (Pensamento geográfico e ensino de Geografia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação histórica da Geografia e expansão colonial": 20
- "2. Correntes teóricas e categorias fundamentais da Geografia": 18
- "3. Currículo, sociedade, lugar e territorialidade no ensino": 19
- "4. Metodologias, investigação e recursos para ensinar Geografia": 26
- "5. Avaliação, integração de escalas e desafios contemporâneos": 17

Já existem (não repita):
- Ao comparar diários de navegação e mapas de ocupação colonial, uma turma questiona quem pr
- Em uma exposição sobre a formação da Geografia moderna, estudantes precisam relacionar Hum
- Ao observar uma rua com moradias antigas e edifícios recentes, o grupo deve escolher a cat
- Em pesquisa sobre uma feira, moradores relatam memórias familiares, sentimentos de pertenc
- Um projeto escolar deseja interpretar a disputa entre pescadores, empresas e moradores pel
- Uma equipe organiza um plano de aprendizagem de Geografia e consulta a BNCC. Qual relação 
- Uma turma examina mapas de distribuição de áreas verdes, associando localização, cobertura
- Para compreender enchentes em torno da escola, a turma seleciona uma pergunta, registra ob
- Uma turma acompanha sua aprendizagem por mapas iniciais, revisões comentadas e produção fi
- Uma turma debate a afirmação de que comunidades em regiões semiáridas desenvolveriam neces
- Uma pesquisa identifica padrões de localização comercial por modelos estatísticos e depois
- Estudantes elaboram um mapa sobre falta de água. Primeiro identificam os bairros afetados 
- Em currículo sobre comunidades tradicionais, a turma analisa mapas oficiais e cartografias
- Uma turma faz uma saída a campo para investigar ilhas de calor urbanas. O planejamento inc
- Estudantes comparam uma imagem óptica produzida com luz solar e uma imagem de radar. Qual 
- Uma avaliação de leitura cartográfica mostra que a maioria reconhece cores no mapa, mas in
- Ao estudar uma indústria local, a turma relaciona empregos municipais a insumos importados
- Em seminário, afirma-se que a Geografia do século XIX se desenvolveu em diálogo com a form
- Duas pesquisas sobre uma praça produzem resultados diferentes: uma mede fluxos e outra inv
- Para regionalizar uma cidade, pesquisadores agrupam setores por renda e acessibilidade. Ou
- Uma proposta de ensino parte de memórias de moradores para discutir transformação de um ba
- Uma atividade compara mapas coropléticos de dois municípios com tamanhos populacionais mui
- Ao sobrepor camadas de áreas impermeabilizadas e pontos de inundação num SIG, estudantes e
- Em uma sequência sobre mobilidade, o mapa de transporte indica conexão rápida entre dois b
- Um instrumento de avaliação considera inicialmente localização correta de fenômenos e depo
- Em uma análise de registros históricos, a turma conclui que a expansão colonial contribuiu
- Ao selecionar fotografias antigas e atuais de uma avenida, estudantes afirmam que a paisag
- Ao trabalhar mapas mentais dos caminhos até a escola, uma sequência pode promover alfabeti
- Numa atividade sobre qualidade ambiental, a existência de uma imagem de satélite atual aut
- Em um projeto de avaliação contínua, produções iniciais, revisões comentadas e apresentaçã
- Uma sequência sobre identidades territoriais deve dispensar dados e documentos externos qu
- Ao investigar a localização de serviços públicos, a turma compara áreas, identifica padrõe
- Ao estudar territórios de comunidades tradicionais, a turma identifica a territorialidade 
- Em estudo de fotografias de uma praça, informações sobre o enquadramento, a data da imagem
- Se uma turma usa dados de um sensor remoto para elaborar mapa temático num SIG, as duas te
- Em uma pesquisa sobre migração, a combinação de relatos pessoais, indicadores demográficos
- Ao organizar uma sequência sobre paisagens rurais e urbanas, a existência de unidades temá
- Uma avaliação formativa pode utilizar rubricas e devolutivas durante a produção de um mapa
- Quando uma imagem cartográfica evidencia concentração de serviços numa área, é legítimo co
- Em estudo comparativo sobre mudanças ambientais, mapas produzidos em escalas cartográficas
- Ao reconstruir a Geografia moderna, uma turma atribui a Humboldt a formulação exclusiva de
- Em pesquisa sobre acesso a bibliotecas, uma turma reconhece que percursos vividos e tempo 
- Ao diferenciar paisagem e lugar, uma turma conclui que o lugar pode ser compreendido exclu
- Ao construir mapas colaborativos dos deslocamentos de estudantes, a divulgação irrestrita 
- Uma atividade reúne comparação de mapas, memórias dos moradores e dados econômicos sobre u
```

## Pedido 8: Magnetismo e indução eletromagnética (`fisica.magnetismo-inducao`, 101 tópicos de edital)

```
Matéria: fisica.magnetismo-inducao (Magnetismo e indução eletromagnética)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ímãs, campo magnético e propriedades dos materiais": 24
- "2. Forças magnéticas, movimento de cargas e espiras": 20
- "3. Campos produzidos por correntes e lei de Ampère": 21
- "4. Fluxo magnético, Faraday-Lenz e indução por movimento": 21
- "5. Indutância, transformadores ideais e integração dos conceitos": 14

Já existem (não repita):
- Ao partir em duas metades um ímã permanente em formato de barra, uma estudante procura obt
- Uma amostra de cobre apresenta magnetização induzida muito fraca em sentido contrário ao c
- Uma amostra de alumínio é submetida a campo externo e apresenta leve alinhamento de moment
- Um núcleo de ferro permanece parcialmente imantado depois de retirada a corrente que produ
- Em um experimento, o campo magnético é medido por grandeza $$B$$, cujo valor será registra
- Uma partícula carregada move-se exatamente paralela a campo magnético uniforme, desprezand
- Uma carga negativa e outra positiva de mesmo módulo entram com velocidades iguais em campo
- Um fio retilíneo de 0,50 m conduz corrente de 4 A, perpendicularmente a campo uniforme de 
- A intensidade do campo a 4 cm de um fio muito longo em corrente contínua é 30 μT. Mantendo
- Uma curva fechada envolve dois fios que a atravessam com correntes de 5 A em sentidos cont
- O campo no centro de uma espira circular é medido antes e depois de instalar uma segunda e
- Um solenoide ideal longo passa a ter o dobro de espiras por metro, mantendo a mesma corren
- Um material apresenta curva de magnetização com laço, remanência e campo coercitivo. Qual 
- Uma espira de área 0,020 m² está em campo uniforme de 0,50 T. A normal à espira forma 60° 
- Uma bobina de 100 espiras sofre variação uniforme do fluxo por volta de 0,020 Wb durante 0
- O fluxo de campo **para dentro da página** através de uma espira fechada está aumentando. 
- Em uma bobina linear de indutância 0,40 H, a corrente aumenta uniformemente de 1 A para 4 
- Um transformador ideal possui 500 espiras no primário e 1.000 no secundário. Alimentado co
- Uma partícula entra em campo magnético uniforme com velocidade transversal e longitudinal 
- Uma bobina plana de três voltas idênticas conduz 2 A; cada volta tem área 0,020 m². Em cam
- Uma barra condutora de 0,40 m desliza a 3 m/s sobre trilhos, perpendicularmente a campo un
- Um transformador ideal recebe 240 V eficazes e 2 A eficazes sob carga adequada. Seu secund
- Um transformador real utiliza núcleo ferromagnético de chapas isoladas e liga adequada de 
- O campo externo através de uma espira condutora fechada aponta **para dentro da página**, 
- Um indutor ideal linear de 0,40 H armazena energia com corrente de 5 A. Se a corrente dobr
- Ao representar as linhas de campo ao redor de ímã de barra, uma estudante desenha linhas q
- Um material que apresenta susceptibilidade magnética pequena e negativa deve ser classific
- Uma peça de ferro que retém parte da magnetização depois da retirada do campo externo ilus
- Uma partícula eletricamente carregada, em repouso em região de campo magnético uniforme, s
- Uma partícula carregada descreve uma trajetória circular em campo magnético uniforme, sem 
- Em uma experiência de laboratório, a unidade henry é empregada para medir fluxo magnético 
- O fato de o cobre ser excelente condutor elétrico não impede que apresente comportamento d
- Uma curva amperiana abrange duas correntes estacionárias iguais em sentidos opostos. Como 
- Dois fios retilíneos longos e paralelos conduzem correntes constantes de mesmo sentido. De
- O campo interno de um solenoide longo ideal, no ar e longe das extremidades, cresce com o 
- Uma espira condutora fechada permanece parada e sua área não muda, mas o campo magnético n
- A lei de Lenz estabelece que o campo criado pela corrente induzida está sempre em sentido 
- Um núcleo ferromagnético utilizado em dispositivo de campo alternado pode apresentar reman
- Em superfície plana de área constante, o fluxo de campo uniforme é máximo quando B é paral
- Ao percorrer lentamente um ciclo de magnetização em núcleo ferromagnético, a curva B versu
- Um transformador ideal ligado a fonte de corrente contínua perfeitamente estável produz te
- Um indutor ideal linear com indutância constante de 0,20 H, percorrido por corrente de 3 A
- Uma carga em movimento circular uniforme sob ação exclusiva de campo magnético uniforme re
- Em transformador ideal carregado, se o secundário possui quatro vezes o número de espiras 
- Quando varia o fluxo através de uma espira de metal interrompida por um pequeno espaço iso
```

## Pedido 9: Psicologia escolar e educacional (`psicologia.escolar-educacional`, 101 tópicos de edital)

```
Matéria: psicologia.escolar-educacional (Psicologia escolar e educacional)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, história e atuação institucional": 20
- "Queixa escolar, dificuldades e avaliação psicoeducacional": 20
- "Inclusão educacional e trabalho com famílias": 20
- "Bullying, convivência e mediação de conflitos": 20
- "Intervenções preventivas, gestão e ética profissional": 20

Já existem (não repita):
- Uma escola pretende reorganizar o serviço de psicologia segundo uma perspectiva institucio
- Sobre a Lei nº 13.935/2019, assinale a interpretação adequada para redes públicas de educa
- Após analisar resultados ruins, uma equipe afirma que o fracasso escolar sempre demonstra 
- Uma professora registra que determinado aluno “não aprende nada”. A primeira medida tecnic
- Uma equipe avalia dificuldades persistentes de leitura. Qual conclusão é compatível com pr
- Sobre a avaliação psicológica de estudantes e o SATEPSI, assinale a alternativa correta.
- Qual opção expressa corretamente o núcleo da inclusão educacional no contexto da Lei nº 13
- Em aula expositiva sem qualquer recurso acessível, estudante com deficiência auditiva está
- Ao planejar ações inspiradas no desenho universal para aprendizagem (DUA), a equipe deve:
- Qual situação mais se aproxima da definição legal de bullying trazida pela Lei nº 13.185/2
- Uma criança relata bullying reiterado e teme reencontrar os colegas responsáveis. Qual dev
- Sobre as respostas institucionais ao bullying e a legislação federal, qual afirmação é cor
- Uma escola organiza projeto de convivência para todas as turmas antes de ocorrerem episódi
- Depois de criar um canal de escuta, a gestão contabiliza mais relatos de humilhação. Qual 
- Um gestor solicita que o psicólogo envie ao grupo de professores todos os protocolos e rel
- Qual medida caracteriza articulação intersetorial na resposta a uma queixa escolar complex
- Uma escola atribui dificuldades generalizadas de uma turma à “falta de interesse” dos alun
- Qual interpretação melhor diferencia psicologia escolar de psicopedagogia na instituição?
- Na devolutiva de uma avaliação psicoeducacional, a prática mais apropriada consiste em:
- Em relação a transtornos específicos da aprendizagem e fatores contextuais, é correto afir
- Ao perceber que um teste psicológico tem situação desfavorável no SATEPSI, o profissional 
- Uma escola desenvolve materiais de acesso variado para alunos com e sem deficiência. Essa 
- A direção informa que adaptações educacionais dependem sempre de laudo médico entregue pre
- Qual orientação melhor enfrenta uma barreira atitudinal contra estudante com deficiência?
- Na psicologia escolar de orientação institucional, as condições pedagógicas e os vínculos 
- A Lei nº 13.935/2019 determina que a escola pública disponha obrigatoriamente de um psicól
- A passagem para perspectivas críticas da psicologia escolar é associada ao questionamento 
- A queixa de um professor de que a criança não acompanha a turma é suficiente para diagnost
- Dados de observação em aula, produção escolar e entrevistas podem ser articulados na avali
- O resultado de um teste psicológico, independentemente de sua adequação ao caso, substitui
- O direito à educação inclusiva exige mais que matrícula: pressupõe condições de acesso, pa
- Na inclusão educacional, igualdade de direitos significa necessariamente aplicar aos estud
- A identificação de barreiras atitudinais pode exigir trabalho com expectativas docentes, p
- Qualquer discussão pontual entre alunos, mesmo sem repetição ou assimetria de poder, é nec
- As ações de prevenção ao bullying podem envolver capacitação de professores, orientação às
- Na presença de intimidação repetitiva com forte desequilíbrio de poder, a mediação pública
- A orientação colaborativa a professores pode incluir discussão de manejo de sala e de estr
- O aumento do número de relatos de violência escolar após criação de um canal de escuta dem
- Em documentos produzidos no trabalho multiprofissional, o Código de Ética admite registrar
- A interdisciplinaridade pode se expressar em planejamento conjunto entre psicologia, pedag
- Com base na orientação institucional, a melhor resposta a faltas recorrentes é classificar
- A análise de um padrão de dificuldades que atinge uma turma inteira pode gerar hipóteses s
- A diferença entre dificuldade de aprendizagem e transtorno específico da aprendizagem é ir
- Um psicólogo pode recomendar intervenções escolares com base em dados contextualizados, ai
- Na avaliação psicoeducacional, a triangulação de informações dispensa examinar o impacto d
- Atuar com famílias de estudantes em processo de inclusão envolve escuta e definição de res
- Recursos planejados segundo desenho universal tornam desnecessárias, em qualquer situação,
- Oferecer recursos adequados a necessidades distintas pode ser compatível com igualdade de 
```

## Pedido 10: Aviso prévio (`direito-trabalho.aviso-previo`, 101 tópicos de edital)

```
Matéria: direito-trabalho.aviso-previo (Aviso prévio)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Cabimento e prazo proporcional": 25
- "2. Modalidades e redução de jornada": 25
- "3. Projeção e efeitos patrimoniais": 25
- "4. Reconsideração, justa causa e reescrita": 25

Já existem (não repita):
- Um analista de folha examina comunicações de encerramento de vínculos celetistas. Assinale
- Na preparação de um guia para empregados de uma transportadora, qual orientação reproduz c
- Uma atendente urbana recebe aviso trabalhado de 30 dias em dispensa promovida pelo emprega
- O cadastro funcional de um empregado dispensado será encerrado após aviso inteiramente ind
- Antes de terminar o aviso dado a uma bibliotecária celetista, a fundação empregadora recon
- Durante aviso concedido pela empresa, um controlador de estoque comete falta comprovada qu
- Um auditor revisa um aviso recebido após quatro anos completos de serviço na mesma empresa
- Um encarregado de manutenção compara orientações sobre aviso trabalhado urbano. Assinale a
- Uma trabalhadora remunerada por tarefa tem o cálculo do aviso indenizado submetido a revis
- Um comerciário recebeu antecipadamente aviso indenizado e, durante sua projeção, foi deter
- Uma clínica rompe antecipadamente contrato de experiência exercendo cláusula que assegura 
- Ao preparar uma agenda para um trabalhador de gráfica, o setor de pessoal verifica a conta
- Um empregado de agência de viagens dispensado sem justa causa pede para não cumprir o avis
- Durante aviso dado a um funcionário de oficina, o empregador pratica ato que justifica a r
- Uma defensora examina o início da prescrição após dispensa com aviso indenizado. Assinale 
- Um assistente jurídico identifica, no art. 487, I, da CLT compilada, a referência a oito d
- Uma montadora dispensa um inspetor após oito anos completos e determina aviso trabalhado p
- Um mecânico com 13 anos completos pede demissão de uma metalúrgica e informa que sairá ime
- Uma empregada de escola recebe comunicação de dispensa em 8 de junho e aviso indenizado de
- Um supervisor de lavanderia pediu demissão e deu aviso. A empresa recusou sua reconsideraç
- Em recurso sobre um vigia pré-avisado pelo empregador, discute-se a aplicação da Súmula 73
- Uma empresa de software dispensa uma programadora sem justa causa, indeniza o aviso e depo
- Um eletricista recebe aviso indenizado de 30 dias calculado sobre salário de R$ 2.400 e ho
- Um empregado de moinho é dispensado após 22 anos e sete meses na mesma empresa. O aviso se
- Uma costureira recebe por tarefa e foi dispensada após quatro anos completos. Os salários 
- Um armazém dispensa um conferente com dois anos completos e aviso de 36 dias. No vigésimo 
- Uma empregada admitida em 1º de março de 2010 foi dispensada em 2 de abril de 2012, tendo 
- Foi reconhecida a despedida indireta de uma farmacêutica após seis anos completos de servi
- Um escritório compara consequências de eventos no curso do aviso. Assinale a afirmação INC
- Uma minuta afirma que, antes do termo do aviso, a parte notificante pode reconsiderar e a 
- A garantia constitucional de aviso prévio assegura prazo mínimo de 30 dias, independenteme
- O acréscimo de três dias previsto na Lei 12.506/2011 é calculado por mês completo de servi
- A redução diária do aviso trabalhado decorrente de dispensa patronal deve manter o salário
- A data de saída do empregado no aviso indenizado é a do pagamento da indenização, mesmo qu
- A parte que recebe a reconsideração apresentada antes do termo do aviso conserva a faculda
- O término regular de contrato de experiência gera aviso prévio apenas porque o instrumento
- O empregado urbano dispensado que escolhe faltar sete dias corridos no aviso de 30 dias po
- A média dos últimos três meses de serviço é a base legal específica para calcular aviso in
- Um reajuste salarial coletivo aplicável, determinado durante o aviso, beneficia o pré-avis
- O art. 491 impõe ao empregador que praticou falta durante o aviso a perda da remuneração c
- Na dispensa após 11 anos completos, o aviso legal chega a 63 dias; na orientação do TST, a
- A concessão de aviso antes de 13 de outubro de 2011 pode ser recalculada pela Lei 12.506/2
- Se o empregado pré-avisado requer dispensa do cumprimento e comprova haver obtido novo emp
- A Súmula 73 permite retirar as verbas rescisórias indenizatórias em qualquer justa causa d
- Mesmo que a parte destinatária tenha inicialmente recusado a reconsideração, a continuidad
```

## Pedido 11: Prescrição e decadência trabalhistas (`direito-trabalho.prescricao-decadencia`, 101 tópicos de edital)

```
Matéria: direito-trabalho.prescricao-decadencia (Prescrição e decadência trabalhistas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos e prazos bienal e quinquenal": 20
- "2. Prescrição total, parcial e parcelas sucessivas": 20
- "3. Interrupção pelo ajuizamento e limites dos pedidos": 20
- "4. Menores de dezoito anos, FGTS e exceções à prescrição": 20
- "5. Decadência no inquérito por falta grave e no mandado de segurança": 20

Já existem (não repita):
- Empregado urbano dispensado sem justa causa deseja ajuizar reclamação de salários. Qual é 
- Uma trabalhadora ajuíza em 01/09/2026 reclamação por diferenças mensais, dentro do biênio 
- Uma pessoa continua empregada há dez anos e quer reclamar diferenças salariais vencidas. Q
- Empregado com 17 anos cobra diferenças salariais referentes ao seu contrato. Qual regra es
- Empregador suspende empregado estável para apurar falta grave e pretende instaurar o inqué
- Uma pessoa toma ciência de ato administrativo impugnável por mandado de segurança na esfer
- Uma reclamação trabalhista é ajuizada em juízo incompetente e depois extinta sem exame do 
- Uma empresa deixa de conceder, mês após mês, promoções previstas em plano de cargos ainda 
- Um empregado requer apenas que antigo vínculo seja anotado para prova perante a Previdênci
- Uma pretensão de cobrança por depósitos de FGTS não realizados nasceu de lesão conhecida d
- Um contrato encerrou-se em 10/03/2024 e a reclamação foi proposta em 10/02/2026. Para salá
- O empregador suprimiu formalmente em 2018 vantagem instituída apenas por regulamento, sem 
- Uma primeira reclamação pediu horas extras, mas foi extinta sem resolução do mérito. Outra
- Uma aprendiz foi desligada quando tinha 17 anos e completou 18 seis meses depois. Ao exami
- Empregado estável foi suspenso em 05/04/2026 por suposta falta grave. A empresa só protoco
- A pessoa interessada apresentou pedido de reconsideração à autoridade antes do fim dos 120
- O trabalhador requer recolhimento de FGTS sobre horas extras cujo próprio direito ao pagam
- Uma pessoa ajuíza ação pedindo anotação de vínculo antigo para comprovação previdenciária 
- A pessoa pede diferenças por promoções anteriores a cinco anos, que não foram concedidas a
- Depois de ajuizar reclamação por adicional noturno, o autor desiste da ação antes do mérit
- Duas empregadas reclamam diferenças mensais de parcela regulamentar. Na primeira hipótese,
- Contrato terminou em 01/04/2024, mas a primeira reclamação tempestiva em 01/03/2026 pediu 
- Um empregado menor de idade encerrou contrato e só alcançou a maioridade seis meses depois
- Empregado de estabilidade específica foi suspenso e o empregador ajuizou inquérito depois 
- Uma primeira demanda ajuizada tempestivamente para reconhecer vínculo foi extinta sem méri
- Um trabalhador urbano cujo contrato já terminou deve observar limite de dois anos para aju
- Respeitado o biênio, a prescrição de cinco anos das diferenças de salário deve ser contada
- Durante a menoridade, o art. 440 da CLT impede que corra prazo de prescrição contra empreg
- Para inquérito judicial de falta grave após suspensão de empregado estável, o art. 853 da 
- O art. 11, § 3º, da CLT admite que ajuizar reclamação em juízo incompetente interrompa a p
- Em ação trabalhista ajuizada dentro do biênio, uma vantagem contratual suprimida em ato ún
- Se o direito à parcela mensal também estiver assegurado por preceito de lei, incide a ress
- Ajuizada uma demanda por comissões e arquivada sem mérito, a interrupção beneficia, em seg
- A reclamação relativa a depósitos do FGTS não realizados e conhecida como lesão após 13/11
- O prazo de cento e vinte dias do mandado de segurança trabalhista conta da extinção do con
- Reconhecer judicialmente que um empregado deveria ter sido promovido em data anterior ao q
- Uma reclamação arquivada sem apreciação do mérito somente interrompe a prescrição de pedid
- A ressalva do art. 11, § 1º, relativa à anotação de vínculo para prova previdenciária não 
- Ao julgar cobrança de FGTS reflexo sobre verba remuneratória prescrita, é correto afastar 
- A empresa que suspende empregado estável por falta grave não desloca automaticamente o iní
- A apresentação de requerimento administrativo de reconsideração interrompe o prazo decaden
- Na CLT, o prazo de prescrição não corre contra empregado menor de dezoito anos, mas essa p
- Se a pessoa perde o prazo de 120 dias para mandado de segurança, todos os seus direitos pa
- Em relação a depósitos de FGTS cuja lesão iniciou antes do julgamento de 13/11/2014, a Súm
- No inquérito por falta grave, a Súmula 403 do STF classifica como prescricional e sujeito 
```

## Pedido 12: Python, R, APIs e ETL (`informatica.python-r-api`, 101 tópicos de edital)

```
Matéria: informatica.python-r-api (Python, R, APIs e ETL)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Python: sintaxe, estruturas e funções": 17
- "2. Linguagem R: vetores, tabelas e ecossistema tidyverse": 33
- "3. Análise de dados: NumPy, pandas, notebooks e bibliotecas": 24
- "4. APIs, HTTP, autenticação, paginação e coleta web": 13
- "5. ETL, formatos, qualidade e tratamento de falhas": 13

Já existem (não repita):
- Uma rotina Python define `valores = [5, 10, 15, 20]` e executa `valores[1:3]`. Qual result
- Um código declara `itens = ['a', 'b']` e depois `itens.append(['c', 'd'])`. Qual é a nova 
- Uma função Python percorre `for i in range(2, 6)` e soma os valores de `i`. Qual será a so
- Um dicionário Python contém `precos = {'arroz': 12}`. Qual expressão obtém um valor padrão
- Um vetor R é definido por `v <- c(4, 8, 12)`. Qual expressão devolve o valor 8?
- Ao analisar `x <- c(2, NA, 6)` em R, uma pessoa usa `mean(x, na.rm=TRUE)`. Que valor resul
- Em uma tabela pandas cujo índice possui rótulos 10, 20 e 30, qual seleção recupera a prime
- Com NumPy, o array `np.array([1, 3, 5])` é multiplicado por 2. Qual é o resultado esperado
- Uma API devolve HTTP 201 ao registrar um novo recurso. Qual interpretação é a mais adequad
- Uma integração coleta arquivos, normaliza campos de datas e grava registros limpos no dest
- Uma função Python foi definida como `def f(x): y = x * 3`, sem instrução `return`. Ao exec
- Um relatório R precisa calcular vendas médias por região. O código primeiro usa `group_by(
- Um analista usa arrays NumPy de formatos `(4, 3)` e `(3,)` em uma adição. Qual será o form
- Uma tabela pandas contém a coluna `idade` com valores 20, ausente e 40. O analista utiliza
- Uma API exige autenticação e devolve `401` quando uma chamada chega sem credencial válida.
- Uma API retorna lista de dez registros e o campo `next_cursor='abc'`. O total documentado 
- Uma rotina de ETL recebe documento JSON válido com dados de erro e status HTTP 500. O prog
- Um arquivo contém texto `id;preco` e linhas `01;2,50`, `02;3,75`. Qual cuidado evita erro 
- Uma matriz NumPy tem formato `(3, 2)` e o analista tenta somá-la diretamente ao vetor de f
- Duas tabelas pandas serão unidas por `id`. Na primeira, o identificador 8 aparece duas vez
- Um notebook apresenta célula 1 com `taxa=2`, célula 2 com `resultado=taxa*5` e célula 3 co
- Um cliente HTTP envia duas vezes a mesma requisição PUT de substituição do recurso `/itens
- Uma coleta de página HTML por requisição GET não encontra linhas que são mostradas no nave
- Uma rotina salva o cursor de próxima página antes de persistir os registros recebidos. Se 
- Um sistema carrega eventos com chave `evento_id`. Após falha de rede, o lote será reenviad
- Uma rotina Python que executa `dados=[2,4]; dados.extend([6,8])` termina com quatro valore
- Em Python, `lista = [1,2]` e `lista * 3` calculam os valores 3 e 6 por multiplicação eleme
- Na linguagem R, a expressão `c(2, 5, 7)[1]` acessa o valor 2, porque a indexação padrão de
- O valor `NA` utilizado para representar ausência em R pode ser tratado como número zero em
- Uma tabela pandas admite agrupamento por coluna e cálculo de total por grupo por meio de `
- A operação `df.loc[0]` seleciona a primeira linha de qualquer DataFrame pandas, independen
- Ao operar um array NumPy de formato `(2, 4)` com vetor de formato `(4,)`, o broadcasting p
- Em R, `dplyr::group_by()` calcula automaticamente uma média por grupo mesmo que não seja u
- A função `tidyr::pivot_longer()` pode converter colunas de diferentes períodos em linhas c
- O código HTTP `204 No Content` sinaliza sucesso e, pela semântica do status, deve vir obri
- Em requisição HTTP, `Content-Type` informa o tipo do corpo enviado, enquanto `Accept` pode
- Quando o servidor responde HTTP 429, a estratégia adequada é reenviar imediatamente chamad
- Em Jupyter, a execução das células pode modificar o estado do kernel fora da ordem visual;
- Matplotlib e Seaborn são bibliotecas de processamento HTTP e paginação de APIs, enquanto R
- NumPy, pandas e SciPy ocupam papéis complementares: arrays, tabelas e rotinas científicas,
- Uma operação PUT, por ser idempotente, precisa devolver sempre corpo e status HTTP exatame
- Ao criar uma junção muitos-para-muitos no pandas, uma chave duplicada em ambos os lados po
- Em ETL, a conversão automática do identificador textual `'0018'` para inteiro 18 é semanti
- Uma carga que compara registros extraídos, rejeitados e persistidos consegue descobrir per
- Uma rotina que recebe sempre HTTP 400 para payload inválido deve repetir a mesma solicitaç
```

## Pedido 13: Resposta a incidentes cibernéticos e análise de malware (`informatica.resposta-incidentes-analise-malware`, 101 tópicos de edital)

```
Matéria: informatica.resposta-incidentes-analise-malware (Resposta a incidentes cibernéticos e análise de malware)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, equipes e governança de incidentes": 16
- "2. Ciclo de resposta: NIST, ISO e decisões operacionais": 20
- "3. Telemetria, logs, ataques a redes e correlação": 19
- "4. Inteligência, threat hunting, vulnerabilidades e APT": 19
- "5. Análise de malware, engenharia reversa e integração da resposta": 26

Já existem (não repita):
- Uma ferramenta registra 12 falhas de autenticação, mas ainda não há prova de acesso indevi
- Após detecção de movimentação suspeita, uma equipe precisa articular isolamento, comunicaç
- Uma política interna relaciona documentos clássicos de resposta. Em qual etapa do ciclo da
- Uma sessão administrativa foi usada indevidamente e precisa ser interrompida sem aguardar 
- Uma auditoria de Windows mostra diversos eventos de ID 4625 para a mesma conta. Que interp
- Um programa suspeito só é examinado por strings e cabeçalhos, sem execução. Qual modalidad
- Um dispositivo transmite mensagens de eventos a um coletor padronizado. Qual tecnologia co
- Uma investigação revisa documento governamental que orienta registro de eventos e preserva
- Um analista descobre que determinado programa se propaga automaticamente entre máquinas vu
- Após incursão, o serviço é restaurado de cópia íntegra, mas uma conta criada pelo invasor 
- Um equipamento emite mensagem Syslog de facility 4 e severity 3. De acordo com a RFC 5424,
- Uma implementação do Graylog precisa extrair usuário de mensagens heterogêneas, enriquecer
- Uma equipe investiga proativamente hipótese de persistência por ferramenta legítima, mesmo
- Um documento cita as Normas Complementares nº 05, nº 08 e nº 21. Qual associação entre seu
- Um processo não usual é criado depois de acesso administrativo. Na auditoria Windows habil
- Um relatório de incidente inclui um hash calculado sobre uma cópia de disco. Qual afirmaçã
- Uma aplicação de análise automatizada detecta um Security Hotspot no código-fonte. Qual in
- Uma organização compara seu guia de quatro fases, rotulado NIST SP 800-61 Rev. 2, com a ed
- Um hunt encontra execução de ferramenta assinada, mas fora do horário usual, após criação 
- Um sistema de logs recebe mensagens da mesma conta em fusos e relógios divergentes. Antes 
- Após suspeita de malware, uma estação mantém conexões ativas e memória ainda não coletada.
- Um sistema enfrenta milhares de requisições por segundo de múltiplas máquinas comprometida
- Ao estudar um executável empacotado e sem símbolos, um perito técnico obtém pseudocódigo p
- Um relatório combina hash de executável antigo e TTP de uso indevido de credenciais. Se ad
- Uma ferramenta de revisão de código informa vulnerabilidade, enquanto outra observa ativid
- Depois de identificar acesso indevido a uma conta, a equipe revogou os tokens ativos para 
- Quando um sensor registra uma tentativa de acesso negada por regra de autenticação, a exis
- Durante a apuração de um incidente, a equipe calcula hash do arquivo coletado e documenta 
- Após detectar ransomware, a equipe restaura um backup e considera automaticamente erradica
- Ao consultar o registro Security do Windows configurado para auditoria, um investigador as
- Um executável em pacote comprimido deve ser classificado como malware, pois o uso de compa
- Ao investigar tarefa agendada suspeita sem alerta prévio, uma equipe formula hipótese, def
- Uma instrução de procedimento técnico afirma que a Norma Complementar nº 05 trata priorita
- Em mensagens conformes à RFC 5424, facility 2 e severity 5 resultam em PRI igual a 21, pel
- Ao correlacionar falhas de login e êxito posterior, o sistema de monitoramento demonstra a
- Um programa de gestão de incidentes utiliza ISO/IEC 27035-1:2023 como referência para prep
- Depois da publicação final da NIST SP 800-61 Rev. 3 em 2025, a Rev. 2 permaneceu como ediç
- Em ambiente Graylog, pipelines podem extrair e enriquecer campos e rotear mensagens para s
- Durante exame de executável desconhecido, a equipe obtém pseudocódigo por descompilação e 
- Em incidente com exploração confirmada, instalar atualização da aplicação pode eliminar um
- Uma campanha é atribuída definitivamente a um agente específico porque o código malicioso 
- Na hipótese de um malware reconhecer virtualização e suspender atividades suspeitas, a aus
- Ao verificar evento Windows 1102, o investigador pode concluir sem outros dados que o regi
- Na gestão de risco, um serviço com CVE grave, ainda sem sinal de exploração, demanda análi
- Se duas cópias de evidência têm hash igual, é possível afirmar que a origem e a autoria do
```

## Pedido 14: Transformação digital, IoT, big data e inteligência artificial (`informatica.transformacao-digital`, 100 tópicos de edital)

```
Matéria: informatica.transformacao-digital (Transformação digital, IoT, big data e inteligência artificial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Transformação digital, Indústria 4.0 e estratégia de produtos": 25
- "2. Governo digital, plataformas de serviços, interoperabilidade e acessibilidade": 23
- "3. Sistema financeiro digital: bancos, fintechs, Pix, Open Finance e Drex": 14
- "4. Arquitetura IoT, nuvem, edge computing, segurança e LGPD": 19
- "5. Big data, ciência de dados, IA, automação inteligente e governança": 19

Já existem (não repita):
- Uma organização escaneou formulários de papel, mas manteve exigências duplicadas e filas i
- Uma fábrica usava controladores programáveis isolados e passou a conectar sensores, manute
- Um serviço público deseja oferecer ao cidadão solicitação digital com acompanhamento do an
- Uma pessoa precisa transferir dinheiro no domingo à noite e pergunta sobre o funcionamento
- Um sistema acompanha pressão de tubulações por dispositivos conectados e pode enviar coman
- Uma equipe pretende iniciar a transformação de atendimento ao cidadão e deseja evitar comp
- Um serviço digital foi implantado, mas cidadãos com dificuldades de conexão ainda precisam
- Uma pessoa autoriza instituição receptora a consultar dados de sua conta mantida em outra 
- Uma aeronave registra vibração fora do padrão e precisa acionar alerta mesmo com conectivi
- Uma plataforma recebe milhões de leituras de sensores a cada minuto em diferentes formatos
- Um sistema extrai dados de atendimentos, transforma códigos inconsistentes e só depois car
- Uma equipe utiliza exemplos de transações marcadas como fraudulentas ou legítimas para apr
- Uma máquina industrial tem sensores de temperatura, modelo virtual atualizado e comandos a
- Um robô de escritório copia dados entre sistemas segundo regras fixas, sem modelo treinado
- Uma equipe quer testar se cidadãos conseguem completar uma solicitação por celular antes d
- Um projeto do sistema financeiro testa entrega de ativo tokenizado contra pagamento e cont
- Uma organização recebe dados de localização de identificadores pessoais por sensores conec
- Um serviço autentica cidadãos com Conta gov.br e exige nível ouro indiscriminadamente para
- Uma plataforma na nuvem oferece ambiente gerenciado para implantar aplicações desenvolvida
- Uma administração conectou cadastros e pretende disponibilizar todas as informações em por
- Uma fintech pretende usar Open Finance para oferecer crédito e solicita que todos os clien
- Uma equipe de produto recebeu 50 pedidos de funcionalidades, mas usuários ainda abandonam 
- Uma prefeitura implantou modelo para recomendar prioridades de inspeção e percebeu maior t
- Uma instituição pretende reformar todo seu atendimento de uma vez, apesar de não ter testa
- Uma cidade inteligente conecta semáforos, sensores e plataforma de análise. Em caso de per
- Um atendimento que apenas troca formulários impressos por imagens digitais, preservando ex
- A Terceira Revolução Industrial relaciona-se à eletrônica e automação programável, enquant
- A Lei nº 14.129/2021 associa governo digital à interoperabilidade, acessibilidade e simpli
- O Pix depende de chave previamente cadastrada para transferências e limita liquidação aos 
- O Decreto nº 9.854/2019 considera comunicação uma capacidade obrigatória de dispositivo Io
- Uma plataforma que coleta grande volume de registros necessariamente possui dados confiáve
- No aprendizado supervisionado, exemplos rotulados podem ensinar um classificador a disting
- No Open Finance, uma instituição pode obter senhas e tokens do cliente quando ele consente
- O uso de edge computing em sensores industriais pode reduzir atrasos de resposta e tráfego
- Um roadmap de produto digital corresponde à lista de todas as tarefas técnicas ordenadas s
- Ao integrar dois serviços públicos, é possível reutilizar registro de referência sob condi
- RPA exige obrigatoriamente treinamento com rótulos, porque qualquer robô que preenche tela
- Uma linha de produção que usa sensores, máquinas conectadas, dados em tempo quase real e a
- A presença de câmera ou sensor conectado isenta o tratamento de imagens identificáveis dos
- Um modelo implantado pode perder desempenho quando o padrão de dados muda; monitorar drift
- Como o Piloto Drex testa transações de ativos digitais, conclui-se que em outubro de 2026 
- Em infraestrutura IoT crítica, a existência de análise na nuvem não elimina a necessidade 
- Em um data lake, a coleta em múltiplos formatos dispensa catálogo, controle de acesso e li
- Bancos digitais e fintechs podem oferecer produtos por canais remotos, mas o uso de interf
- As funções Govern, Map, Measure e Manage do NIST AI RMF formam sequência linear obrigatóri
```

## Pedido 15: Suspensão e interrupção do contrato (`direito-trabalho.suspensao-interrupcao`, 99 tópicos de edital)

```
Matéria: direito-trabalho.suspensao-interrupcao (Suspensão e interrupção do contrato)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Distinção e efeitos dos afastamentos": 25
- "2. Ausências justificadas do art. 473 da CLT": 25
- "3. Doença, acidente, encargo público e lay-off": 25
- "4. Como o tema costuma ser cobrado": 25

Já existem (não repita):
- Em qual situação o afastamento é, em regra, interrupção do contrato?
- Um empregado vai se casar e pede ausência remunerada. Qual limite consta do art. 473, II, 
- Qual prazo mínimo de aviso ao sindicato exige a suspensão para qualificação profissional d
- Assinale a afirmação correta sobre o retorno de empregado afastado para cumprir encargo pú
- A consulta médica é do filho de cinco anos do empregado. Qual ausência remunerada específi
- Qual requisito integra a suspensão do contrato para curso de qualificação profissional?
- A chefia aplica suspensão disciplinar por 31 dias consecutivos. Qual consequência estabele
- O irmão de uma assistente administrativa faleceu. Por quanto tempo ela pode faltar sem pre
- Um técnico de informática doou sangue voluntariamente e apresentou comprovante. Qual regra
- Um auxiliar de escritório quer acompanhar a companheira grávida nas consultas de pré-natal
- Classifique: (I) os primeiros quinze dias de afastamento por doença comum; (II) o período 
- Uma recepcionista sofreu acidente do trabalho e ficou quatro meses afastada recebendo bene
- Durante o afastamento de um analista, a categoria conquistou reajuste em convenção coletiv
- Uma secretária é intimada a depor como testemunha em processo judicial. Como fica a ausênc
- Com base em acordo coletivo e aceite formal dos empregados, uma empresa pretende suspender
- Durante a suspensão para qualificação profissional, o empregador decide repassar um valor 
- Um empregado é dispensado sem justa causa dois meses depois de voltar de suspensão para qu
- Uma contadora foi aposentada por incapacidade permanente. Qual efeito o art. 475 da CLT at
- Um analista contratado por prazo determinado é convocado para encargo público obrigatório.
- Qual das situações abaixo é exemplo de suspensão do contrato de trabalho?
- Uma empregada, representante de entidade sindical, viaja para reunião oficial de organismo
- Em um mesmo período de doze meses de trabalho, uma empregada comprovou quatro dias de ausê
- Um assistente jurídico fica dez dias afastado por doença comum, com atestado. Quem arca co
- A empresa quer estender a suspensão para qualificação além do prazo inicialmente pactuado.
- Um estagiário efetivado como auxiliar presta vestibular em dois dias e apresenta comprovan
- A licença legalmente abonada do art. 473 suspende o pagamento salarial durante a ausência.
- A adoção e a guarda compartilhada também estão incluídas no afastamento de cinco dias do a
- A ausência para alistamento eleitoral pode ocorrer por até dois dias consecutivos ou não, 
- Na doença comum, o empregador paga o salário apenas nos primeiros cinco dias de afastament
- O empregador pode aplicar suspensão disciplinar por mais de trinta dias consecutivos sem c
- Certo ou errado: o afastamento do empregado para cumprir encargo público não autoriza o em
- Durante a suspensão, o empregado mantém o vínculo e tem direito às vantagens atribuídas à 
- O exame vestibular precisa ser comprovado e destina-se ao ingresso em estabelecimento de e
- A empresa pode deixar de remunerar os primeiros quinze dias de incapacidade por acidente d
- No lay-off, a suspensão pode ser repetida a cada doze meses, desde que haja novo aceite fo
- O tempo para comparecimento a juízo é abonado pelo período necessário, conforme o art. 473
- Certo ou errado: o repouso semanal remunerado é exemplo de interrupção do contrato de trab
- Certo ou errado: a ausência pelo falecimento do avô do empregado está entre as hipóteses d
- Na suspensão do art. 476-A, o contrato pode ficar suspenso por até cinco meses, uma vez a 
- Os cinco dias do art. 473, III, começam a ser contados no primeiro dia útil posterior ao n
- O retorno do empregado afastado para encargo público depende de aviso por telegrama ou car
- A extensão a vinte dias do § 2º do art. 473 alcança qualquer nascimento ou adoção, sem con
- Se o curso de qualificação não for ministrado ou o empregado continuar trabalhando, a susp
- Certo ou errado: durante a suspensão para qualificação profissional, a ajuda compensatória
- O empregador deve informar o empregado sobre a possibilidade de ausência para exames preve
```

## Pedido 16: Blockchain e criptoativos (`informatica.blockchain-criptoativos`, 99 tópicos de edital)

```
Matéria: informatica.blockchain-criptoativos (Blockchain e criptoativos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. DLT, blocos, criptografia, consenso e redes": 39
- "2. Bitcoin, Ethereum, forks e exploradores de blocos": 15
- "3. Contratos inteligentes, oráculos e aplicações em finanças digitais": 19
- "4. Criptoativos, carteiras, custódia, mercados e análise de dados": 15
- "5. Regulação brasileira, prevenção à lavagem e rastreabilidade": 12

Já existem (não repita):
- Uma empresa propõe registrar movimentações de estoque em vários computadores, com cópias s
- Ao alterar um dado antigo em bloco já referenciado por blocos seguintes, uma pessoa perceb
- Para autorizar transferência de criptoativo sob seu controle, Ana assina digitalmente a op
- Uma equipe compara o consenso do Bitcoin com o do Ethereum atual. Qual associação consider
- Uma pessoa consulta no explorador de blocos um identificador de transação e quer acompanha
- Ao estudar a história da rede Bitcoin, uma pessoa compara o documento inicial e o bloco gê
- Em uma carteira Bitcoin, a transação consome saída não gasta de 0,5 BTC para pagar 0,2 BTC
- Uma plataforma automatiza liberação de garantia com contrato inteligente, mas precisa veri
- Ao examinar projeto de tokenização, uma pessoa encontra unidades intercambiáveis, cada uma
- Uma pessoa utiliza exchange que guarda as chaves e apenas lhe apresenta saldo na plataform
- Um protocolo emite token que pretende acompanhar uma moeda fiduciária, mas existe risco de
- Uma empresa pretende transformar registros públicos on-chain em painel de indicadores, ass
- Uma empresa realiza, em nome de clientes, custódia e transferências de ativos virtuais. Qu
- Um relatório descreve um token que representa contrato de investimento coletivo ofertado p
- O explorador mostra transferências de endereço vinculado a uma exchange, mas sem identific
- Uma pessoa aplica em um ETF que acompanha o preço de criptoativos, sem receber chaves priv
- Um analista vê duas transações Bitcoin que utilizam saídas associadas ao mesmo controle e 
- Após o halving de 2024, uma questão afirma que cada bloco Bitcoin validado gera subsídio d
- Um contrato de empréstimo descentralizado calcula liquidação a partir de preço informado p
- Uma prestadora realiza transferências por carteiras intermediárias, usa mixer e depois con
- Uma empresa promove token que representa direito de participação nos lucros de empreendime
- Um enunciado considera que pontos de programa de fidelidade, utilizados apenas para resgat
- Uma exchange centralizada utiliza endereço compartilhado por vários clientes e realiza com
- Uma questão compara resolução regulatória de autorização de prestadoras, outra de funciona
- Um esquema recebe recursos sob promessa falsa de rentabilidade em operações com ativos vir
- Uma rede permissionada pode utilizar blocos encadeados e restringir a validação a particip
- Ao conhecer o hash de uma transação Bitcoin, qualquer participante consegue revertê-lo par
- No Bitcoin, o halving reduz periodicamente o subsídio para novos blocos e não diminui os s
- Ethereum continua utilizando prova de trabalho como consenso principal em 2026, razão pela
- Um explorador de blocos pode mostrar hash de transação, endereço, bloco e confirmações, ma
- Um contrato inteligente Ethereum pode verificar diretamente qualquer ocorrência fora da re
- Uma carteira de autocustódia mantém controle das credenciais de movimentação com o titular
- O fato de um usuário possuir NFT associado a obra digital transfere automaticamente a titu
- No modelo UTXO do Bitcoin, uma transação pode consumir uma saída anterior e gerar outra de
- O regulador de prestadoras de serviços de ativos virtuais indicado pelo Decreto nº 11.563/
- O art. 5º da Lei nº 14.478/2022 abrange, entre os serviços prestados a terceiros, a transf
- Pontos de fidelidade destinados apenas ao acesso a produtos determinados integram necessar
- Uma stablecoin referenciada a moeda fiduciária pode perder paridade se houver falha na man
- Um dashboard de transações on-chain identifica com certeza todos os beneficiários finais e
- Nos termos da Lei nº 14.478/2022, a utilização de ativo virtual em conduta de lavagem pode
- Quando várias operações com criptoativos passam por mixers, isso prova por si só a existên
- Um contrato inteligente pode executar corretamente uma regra de liquidação baseada em preç
- Um token cuja oferta se enquadra como contrato de investimento coletivo deixa de poder ser
- A regulamentação do Banco Central de 2025 distingue processos de autorização, regras de fu
- A expressão hard fork identifica necessariamente uma alteração temporária sem mudanças nas
```

## Pedido 17: Equilíbrio químico (`ciencias.equilibrio-quimico`, 99 tópicos de edital)

```
Matéria: ciencias.equilibrio-quimico (Equilíbrio químico)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Reversibilidade, equilíbrio dinâmico e lei da ação das massas": 18
- "2. Quociente de reação e deslocamento do equilíbrio": 18
- "3. Teorias ácido-base, pares conjugados e constantes de ionização": 30
- "4. Autoionização da água, pH, pOH e cálculos aquosos": 19
- "5. Integração de questões, gráficos conceituais e decisões de equilíbrio": 15

Já existem (não repita):
- Num recipiente fechado, as quantidades de reagentes e produtos se tornam constantes, embor
- Em uma reação reversível com valor de K igual a 5, mantém-se a mesma temperatura, mas escr
- Para uma reação genérica em recipiente fechado, um cálculo fornece quociente de reação Q i
- Em um equilíbrio reversível mantido a temperatura constante, acrescenta-se catalisador efi
- Em solução aquosa, a amônia recebe um próton da água e forma íon amônio. Segundo Brønsted–
- Um laboratório prepara uma solução aquosa muito diluída com concentração aproximada de íon
- Duas soluções do mesmo ácido fraco, à mesma temperatura, apresentam diferentes concentraçõ
- Uma solução aquosa neutra encontra-se a 25 °C, com produto iônico da água igual a dez elev
- Considere uma reação gasosa ideal com quatro mols gasosos estequiométricos entre reagentes
- Para o sistema em equilíbrio com reação direta exotérmica, a temperatura sobe enquanto a c
- Para uma reação simples com constante Kc igual a 3, toda a equação química é multiplicada 
- Um ácido fraco monoprótico apresenta, no equilíbrio aquoso, concentrações aproximadas de H
- Um recipiente rígido abriga reação gasosa ideal em equilíbrio. Introduz-se gás inerte, sem
- O trifluoreto de boro reage com amônia pela aceitação de um par eletrônico livre da amônia
- Um ácido monoprótico fraco tem concentração analítica inicial de 0,10 mol/L e grau de ioni
- Uma solução idealizada a 25 °C possui concentração de íons OH− igual a 10^−3 mol/L. Quais 
- Em solução de ácido fraco HA, adiciona-se um sal solúvel que fornece A− e não modifica sig
- Em água a 25 °C, um ácido fraco HA tem Ka = 10^−6. Qual é o Kb aproximado de sua base conj
- A reação gasosa ideal abaixo, representada por um mol de H2 e um mol de I2 formando dois d
- A água pura a 80 °C apresenta iguais concentrações de H3O+ e OH− e pH aproximado de 6,31. 
- Duas soluções aquosas ideais a 25 °C têm pH 2 e pH 5. Qual é aproximadamente a razão entre
- A síntese gasosa ideal de NH3 a partir de N2 e H2 é exotérmica e passa de quatro para dois
- Um ácido monoprótico fraco obedece à aproximação de Ostwald em duas soluções. A segunda te
- Na reação ideal H2 + I2 ⇌ 2HI, foram medidas em equilíbrio [H2] = 0,50 mol/L, [I2] = 0,25 
- Num sistema ideal A ⇌ B, o equilíbrio tem Kc = 4. Em dado instante, [A] = 0,40 mol/L e [B]
- Em um equilíbrio reversível, as concentrações podem permanecer constantes mesmo com reaçõe
- Numa reação gasosa reversível em equilíbrio, acrescentar catalisador em temperatura consta
- Na teoria de Brønsted–Lowry, a espécie que doa próton atua como ácido, e a espécie que rec
- Uma solução aquosa com pH igual a 3, a 25 °C e em condições escolares ideais, possui conce
- Em uma solução de ácido fraco HA, adicionar íon A− de um sal solúvel tende a reduzir a ion
- Para qualquer reação, a constante de equilíbrio em concentrações inclui a concentração de 
- Se um sistema apresenta Qc maior que Kc para a reação escrita, sua evolução líquida tende 
- Para a reação endotérmica no sentido direto, o aquecimento reduz a constante de equilíbrio
- Na formação de um aduto entre BF3 e NH3, o BF3 pode agir como ácido de Lewis porque recebe
- Adicionar gás inerte a recipiente rígido com mistura ideal em equilíbrio sempre favorece o
- Ao diluir moderadamente uma solução de ácido monoprótico fraco, mantendo-se a temperatura,
- Duas soluções a 25 °C, com pH 2 e pH 4, diferem apenas duas vezes na concentração de H3O+,
- No equilíbrio de um ácido HA, a adição de sua base conjugada A− altera o grau de ionização
- Na reação NH3 + H2O ⇌ NH4+ + OH−, as espécies NH3 e OH− formam um par ácido-base conjugado
- Água pura aquecida pode permanecer neutra mesmo apresentando pH inferior a 7, desde que as
- Se uma reação gasosa ideal tem a mesma soma de coeficientes gasosos em cada lado, a simple
- Para uma reação química exotérmica no sentido direto, um aumento de temperatura pode acele
- Quando um sistema gasoso ideal sob pressão total constante recebe gás inerte, seu volume a
- Se Ka de um ácido HA vale 10^−5 a 25 °C e Kw vale 10^−14, a base conjugada A− terá Kb próx
- No equilíbrio heterogêneo entre carbonato de cálcio sólido, óxido de cálcio sólido e dióxi
```

## Pedido 18: Lei de Migração: direitos, vistos, residência e retirada compulsória (`legislacao.lei-migracao`, 98 tópicos de edital)

```
Matéria: legislacao.lei-migracao (Lei de Migração: direitos, vistos, residência e retirada compulsória)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, garantias e política migratória brasileira": 17
- "2. Vistos, documentos, registro e autorização de residência": 17
- "3. Repatriação, deportação, expulsão e garantias": 33
- "4. Extradição ativa e passiva: condições, vedações e procedimento": 18
- "5. Asilo, refúgio, apatridia e proteção internacional": 15

Já existem (não repita):
- Uma brasileira passou a morar no exterior por prazo indeterminado. Segundo a Lei nº 13.445
- Uma família estrangeira com residência irregular procura matricular criança em escola públ
- Uma pessoa obteve visto válido antes da viagem e entende que nenhuma verificação de ingres
- Uma pessoa chega ao Brasil para uma viagem turística breve, sem desejo de aqui estabelecer
- Um residente com visto temporário apresenta-se para identificação civil. Qual característi
- Pessoa impedida legalmente de ingressar é devolvida administrativamente ao país de procedê
- Uma pessoa já se encontra em território brasileiro em situação migratória irregular, sem f
- Uma pessoa é retirada do país com impedimento de reingresso por período determinado, após 
- O Brasil pede a outro país a entrega de pessoa para responder a processo penal brasileiro.
- Uma pessoa notificada pessoalmente por irregularidade documental dispõe de prazo de regula
- Durante deportação administrativa, a pessoa recebe decisão e interpõe recurso previsto em 
- O impedimento de reingresso associado à expulsão foi fixado em prazo superior a duas vezes
- Uma pessoa foi reconhecida como refugiada e procura saber quem apreciou originariamente o 
- Uma pessoa busca proteção por fundado temor de perseguição por opinião política fora do pa
- Uma pessoa sem nacionalidade reconhecida por qualquer Estado pede reconhecimento dessa con
- Uma pessoa recebe asilo político territorial e deseja saber sua natureza jurídica à luz do
- Em outubro de 2026, uma nota de estudo apresenta como vigente a regulamentação do CNIg con
- Um Estado pede ao Brasil a extradição de pessoa brasileira nata, para julgamento criminal 
- Estado estrangeiro solicita extradição por crime cuja pena de prisão prevista na legislaçã
- Uma autoridade recebe pedido de extradição formulado por Estado estrangeiro e cogita autor
- Após seu interrogatório no procedimento de extradição passiva, a pessoa reclamada precisa 
- Depois de pronunciamento favorável do STF e autorização do Executivo, o Estado requerente 
- Um imigrante condenado definitivamente vive no Brasil desde que ingressou aos onze anos de
- Um relatório afirma que o Pacto Global para Migração de 2018, por sua própria adoção, revo
- Uma pessoa reconhecida como refugiada obtém voluntariamente nova nacionalidade e passa a g
- A Lei nº 13.445/2017 inclui entre as diretrizes da política migratória a não criminalizaçã
- Uma pessoa sem residência migratória regular perde o acesso à educação pública por força d
- Visto de visita pode abranger turismo e negócios em estada curta, sem intenção de estabele
- Toda pessoa que recebe visto de visita está autorizada a manter emprego remunerado comum n
- A deportação administrativa da Lei de Migração pressupõe situação migratória irregular de 
- A expulsão descrita no art. 54 é uma forma de citação penal destinada a levar o acusado a 
- O art. 30 da Lei de Migração admite autorização de residência, mediante registro, inclusiv
- A pessoa estrangeira que adquira imóvel no Brasil passa a ter direito automático de residê
- Na deportação, a notificação regular para sanar irregularidade deve conceder, como regra, 
- Pelo art. 51, a falta de manifestação da Defensoria Pública da União, mesmo quando previam
- Em extradição passiva, o art. 82 impede a entrega de brasileiro nato e também afasta a ext
- Como a extradição ativa é solicitada pelo Brasil, todo pedido estrangeiro dirigido ao Bras
- O art. 1º, III, da Lei 9.474/1997 admite reconhecimento de refúgio por grave e generalizad
- Uma pessoa em situação de refúgio poderá ser repatriada para país onde sua vida esteja ame
- O pedido estrangeiro de extradição depende de pronunciamento prévio do Supremo Tribunal Fe
- Na extradição passiva, o prazo de sessenta dias para retirada pelo Estado requerente é con
- A obtenção de nova nacionalidade acompanhada da proteção efetiva do novo país figura no ar
- O Decreto 9.873/2019 continuou disciplinando em outubro de 2026 a composição atual do Cons
- Uma pessoa reconhecida como apátrida tem direito às salvaguardas do art. 26 da Lei 13.445/
- O Pacto Global para Migração de 2018 e a Convenção da ONU de 1990 sobre trabalhadores migr
```

## Pedido 19: Realidade do Distrito Federal e da RIDE (`distrito-federal.realidade-df-ride`, 98 tópicos de edital)

```
Matéria: distrito-federal.realidade-df-ride (Realidade do Distrito Federal e da RIDE)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Interiorização da capital e construção de Brasília": 20
- "2. Plano Piloto, urbanismo e patrimônio": 20
- "3. Organização político-administrativa": 20
- "4. Geografia física: Cerrado, clima e águas": 20
- "5. População, economia, RIDE e cultura": 20

Já existem (não repita):
- A primeira Constituição republicana brasileira, de 1891, ao tratar da sede do governo, pre
- A Comissão Exploradora do Planalto Central (1892–1894), que demarcou a área conhecida como
- No centenário da Independência, em 7 de setembro de 1922, foi lançada a pedra fundamental 
- A Lei nº 2.874/1956, que tratou da mudança da capital, criou a empresa encarregada de cons
- O projeto urbanístico vencedor do concurso para o Plano Piloto de Brasília é de autoria de
- Durante a construção de Brasília, a presidência da Novacap coube a:
- Brasília foi inaugurada em:
- A leitura do Plano Piloto em escalas é central para entender seu tombamento. As quatro esc
- Sobre as superquadras do Plano Piloto, assinale a alternativa correta.
- Quanto ao reconhecimento de Brasília como patrimônio, é correto afirmar que:
- O Lago Paranoá, presente na paisagem do Plano Piloto, é:
- De acordo com a Constituição Federal de 1988, o Distrito Federal:
- Quanto às competências legislativas, o Distrito Federal:
- Os órgãos de segurança pública do Distrito Federal previstos na Constituição Federal são o
- O Poder Legislativo do Distrito Federal é exercido pela Câmara Legislativa, composta por:
- Sobre as regiões administrativas (RAs) do Distrito Federal, assinale a alternativa correta
- O bioma predominante no Distrito Federal é:
- O clima do Distrito Federal caracteriza-se por:
- A Estação Ecológica de Águas Emendadas, em Planaltina, é conhecida porque:
- O Distrito Federal faz divisa com:
- A Região Integrada de Desenvolvimento do Distrito Federal e Entorno (RIDE-DF):
- Moradores de municípios como Águas Lindas de Goiás, Valparaíso de Goiás e Novo Gama desloc
- A estrutura econômica do Distrito Federal caracteriza-se por:
- A região administrativa de Ceilândia tem esse nome porque surgiu:
- Sobre Planaltina e Brazlândia, assinale a alternativa correta.
- Juscelino Kubitschek assumiu publicamente o compromisso de transferir a capital em comício
- O termo candangos designava os diplomatas estrangeiros que se transferiram para Brasília l
- O Núcleo Bandeirante originou-se da chamada Cidade Livre, núcleo comercial provisório que 
- Taguatinga foi projetada por Lucio Costa como uma das asas residenciais do Plano Piloto.
- No Plano Piloto, o Eixo Monumental reúne a Esplanada dos Ministérios e a Praça dos Três Po
- O tombamento do conjunto urbanístico de Brasília proíbe a realização de qualquer nova obra
- Desde 1990, o governador do Distrito Federal é escolhido por eleição direta; antes disso, 
- O Tribunal de Justiça do Distrito Federal e dos Territórios é organizado e mantido pelo Go
- O Ministério Público do Distrito Federal e Territórios integra o Ministério Público da Uni
- Assim como o TJDFT e o MPDFT, a Defensoria Pública do Distrito Federal é hoje organizada e
- No inverno do Distrito Federal, a umidade relativa do ar costuma cair a níveis muito baixo
- Todo o território do Distrito Federal está inserido na bacia hidrográfica do rio São Franc
- Durante a crise hídrica de 2016 a 2018, o Distrito Federal adotou racionamento de água e p
- As cidades que mais cresceram no DF desde 1960 ocuparam apenas áreas já urbanizadas, sem p
- Árvores tortuosas, de casca grossa e raízes profundas, adaptadas à seca e ao fogo, são car
- A RIDE-DF é uma região metropolitana instituída por lei estadual, nos mesmos moldes das re
- Pelos resultados do Censo 2022, Brasília figura entre as três cidades mais populosas do pa
- Os edifícios do Congresso Nacional e da Catedral de Brasília foram projetados pelo paisagi
- Em 8 de janeiro de 2023, as sedes dos Três Poderes, na Praça dos Três Poderes, foram invad
- A mão de obra que construiu Brasília veio quase exclusivamente da Região Sul, o que explic
- O Festival de Brasília do Cinema Brasileiro, o Clube do Choro e o rock dos anos 1980, com 
- Em razão do alto PIB per capita, as regiões administrativas do DF apresentam renda e acess
```

## Pedido 20: Ligações químicas (`ciencias.ligacoes-quimicas`, 97 tópicos de edital)

```
Matéria: ciencias.ligacoes-quimicas (Ligações químicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Elétrons de valência, símbolos de Lewis e regra do octeto": 17
- "2. Ligações iônicas e metálicas": 21
- "3. Ligações covalentes, ressonância e força": 24
- "4. Eletronegatividade e polaridade da ligação": 20
- "5. Exceções ao octeto e integração dos tipos de ligação": 18

Já existem (não repita):
- Um átomo neutro de oxigênio, pertencente ao grupo 16, será representado por qual número de
- Ao construir a fórmula iônica de cloreto de magnésio, usam-se os íons Mg²⁺ e Cl⁻. Qual pro
- Uma substância apresenta moléculas formadas por dois átomos de hidrogênio. Segundo a regra
- Uma amostra de cloreto de sódio sólido puro é submetida a circuito elétrico; depois é fund
- Qual descrição representa o modelo introdutório da ligação metálica em um fio de cobre?
- Na molécula Cl₂, cada cloro completa o octeto por uma ligação simples. Quantos pares não l
- Uma comparação entre os vínculos H–Cl e Cl–Cl pretende identificar o caráter polar. Qual a
- Um estudante precisa escrever a estrutura de Lewis da água. Quantos pares isolados devem a
- Uma fórmula de NaCl representa adequadamente qual espécie no sólido cristalino comum?
- Entre moléculas diatômicas N₂ e HF, qual apresenta vínculo covalente polar no modelo intro
- Para uma estrutura de CO₂, C contribui quatro elétrons de valência e cada O, seis. Qual to
- Para formação de óxido de alumínio, admita Al³⁺ e O²⁻. Qual fórmula expressa a menor propo
- Em HCl, o cloro atrai mais densidade eletrônica que o hidrogênio. Qual notação de cargas p
- Um desenho correto de BF₃ por três ligações simples B–F apresenta quantos elétrons ao redo
- Na comparação entre ligações C–C, C=C e C≡C, envolvendo os mesmos elementos, qual tendênci
- No nitrato NO₃⁻, três estruturas válidas de Lewis alternam qual oxigênio aparece com ligaç
- As ligações C=O de CO₂ são polares, mas a molécula linear é apolar. Qual razão resolve ess
- Uma liga à base de cobre sofre deformação plástica e conserva condução elétrica. Qual expl
- O composto NH₄NO₃ possui cátions amônio e ânions nitrato. Qual classificação das interaçõe
- Uma tabela apresenta energia de dissociação de H–H em fase gasosa igual a 436 kJ/mol. Que 
- Em uma estrutura de Lewis do NO₃⁻ com uma ligação N=O e duas N–O simples, qual conjunto de
- A molécula NO neutra é representada com elétron desemparelhado. Qual justificativa vem dir
- Em PCl₅, o fósforo aparece com cinco ligações simples P–Cl no modelo tradicional de Lewis.
- Uma comparação de energias de rede iônica e de dissociação de um vínculo covalente simples
- Uma amostra de diamante apresenta ponto de fusão muito elevado apesar de ser formada apena
- Na molécula H₂, os dois elétrons compartilhados permitem que cada hidrogênio atinja o duet
- Em uma rede cristalina de NaCl, a fórmula NaCl descreve uma molécula independente constitu
- Em uma ligação covalente simples representada por um traço na estrutura de Lewis, o traço 
- Uma peça metálica conduz corrente elétrica quando sólida porque seus cátions se deslocam l
- Na molécula Cl₂, a diferença de eletronegatividade entre os átomos ligados é nula, razão p
- Um cristal iônico sólido ideal conduz facilmente porque seus cátions e ânions são tão móve
- Para montar a fórmula neutra de Mg₃N₂, considera-se três íons Mg²⁺ e dois íons N³⁻, cuja s
- Se a ligação C–O é polar, conclui-se que toda molécula contendo duas ligações C–O será pol
- No cálculo de elétrons de valência de uma espécie molecular com carga positiva unitária, s
- O BF₃ é exemplo de violação da regra do octeto por expansão de valência do boro, que apare
- Uma forma de Lewis de NO₃⁻ pode atribuir carga formal +1 ao nitrogênio, −1 a cada oxigênio
- As três estruturas de ressonância do nitrato indicam que os três oxigênios trocam fisicame
- Os elementos do terceiro período, como fósforo e enxofre, podem ser apresentados em estrut
- Como uma ligação dupla costuma ser mais forte que uma simples entre os mesmos elementos, a
- O fato de H₂O possuir ligações O–H polares e de CO₂ possuir ligações C=O polares não obrig
- A presença de energia reticular alta em certo cristal iônico permite comparar numericament
- No modelo de ligação metálica, a deslocalização dos elétrons favorece condução elétrica e 
- A molécula NO possui número ímpar de elétrons de valência, mas isso exige que ela seja um 
- A coexistência de atração iônica entre NH₄⁺ e NO₃⁻ e de ligações covalentes dentro desses 
- O diamante, por ser composto de carbono com ligações covalentes, deve apresentar moléculas
```

## Pedido 21: Setor elétrico brasileiro e energias renováveis (`legislacao.setor-eletrico-energias-renovaveis`, 96 tópicos de edital)

```
Matéria: legislacao.setor-eletrico-energias-renovaveis (Setor elétrico brasileiro e energias renováveis)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos legais, instituições e governança": 19
- "2. Outorgas, infraestrutura, acesso e comercialização": 22
- "3. Usuários, Resolução ANEEL 1.000/2021 e proteção do fornecimento": 21
- "4. Tarifas, encargos, consumidores livres e geração distribuída": 18
- "5. Fontes renováveis, comparação econômica e expansão segura": 20

Já existem (não repita):
- Um estudo de modernização da infraestrutura pergunta qual entidade, nos termos do art. 1º 
- O responsável por estudos de projeções de carga e alternativas de ampliação da oferta elét
- Uma empresa geradora celebrou contrato de energia e precisa compreender quem registra e co
- Consumidor do Grupo A contrata energia diretamente de comercializador habilitado e deixa d
- Morador solicitou religação normal após regularizar suspensão em imóvel urbano. Segundo a 
- Uma residência instalou microgerador solar de 10 kW e aderiu validamente ao SCEE. Segundo 
- Uma unidade beneficiária da Tarifa Social consumiu 65 kWh em um mês de 2026. Qual regra vi
- Um parque eólico apresenta estimativa de custo por MWh ao longo da vida útil, mas seu aces
- Uma concessionária vai ampliar rede de distribuição e precisará de faixa em terreno partic
- Indústria atendida em 13,8 kV com padrão de entrada pronto pede ligação e medição. Qual pr
- Ao assumir imóvel alugado urbano com dívida de energia do ocupante anterior, o novo locatá
- Uma empresa migra para o ACL e passa a comprar energia por preço negociado. Ela continua c
- Em outubro de 2026, uma pequena loja de baixa tensão consulta o calendário de abertura do 
- Uma unidade cativa instala gerador solar de 60 kW em corrente alternada, atendidos os requ
- Em 2026, unidade participante do SCEE fora do regime protegido do art. 26 da Lei nº 14.300
- Uma distribuidora avalia proposta de tarifa branca do Grupo B e pretende orientar o consum
- Após alteração legislativa de 2025, um relatório institucional descreve o âmbito de fiscal
- Diante de déficit de potência em horários noturnos, estudo propõe instalar baterias ligada
- Um empreendimento necessita implantar subestação e, em outro terreno, somente restringir e
- Consumidor de área rural, com suspensão legítima já regularizada, requer religação normal 
- Uma unidade residencial possui morador cadastrado que utiliza equipamento elétrico indispe
- Empresa com fornecimento no ACL pleiteia adesão simultânea ao SCEE para compensar excedent
- Uma central solar não despachável de 4 MW, implantada em 2026, busca classificação ordinár
- O estudo de duas usinas compara apenas LCOE: solar com custo unitário inferior e geração c
- Ao identificar a base histórica do Proinfa, um relatório enumera tecnologias incentivadas 
- Um relatório descreve a Lei nº 9.427/1996 como ato que instituiu a ANEEL, autarquia em reg
- No Sistema Interligado Nacional, a CCEE é responsável por coordenar fisicamente o despacho
- Uma indústria habilitada a atuar no Ambiente de Contratação Livre pode negociar a compra d
- Em uma residência urbana com suspensão regularizada, a religação normal pode ser realizada
- No serviço regulado, a bandeira tarifária verde indica condições de geração mais favorávei
- Projeto de geração solar distribuída com potência instalada de 150 kW enquadra-se como mic
- A energia maremotriz aproveita o movimento das marés, enquanto a geotérmica utiliza calor 
- Embora seja autarquia reguladora, a ANEEL não inclui armazenamento entre as atividades reg
- Uma distribuidora projeta a expansão da rede, enquanto outra entidade avalia sistematicame
- Ao escolher fornecedor no mercado livre, um consumidor passa a ter acesso à transmissão e 
- A mudança de titularidade de uma conta de energia na área urbana, com documentação adequad
- Uma unidade beneficiária da Tarifa Social consome 70 kWh em outubro de 2026. O desconto de
- Na transição prevista pelo art. 27 da Lei nº 14.300/2022, participantes do SCEE fora das h
- Na comparação econômica de parques renováveis, basta identificar o menor LCOE do empreendi
- O consumidor do Grupo A pôde exercer, a partir de 1º de janeiro de 2024, a opção de migraç
- A Declaração de Utilidade Pública emitida para a instalação de uma linha de transmissão tr
- Uma microgeradora cativa pretende destinar excedentes dentro das modalidades previstas na 
- Projeto solar não despachável de 4 MW iniciado em 2026 enquadra-se automaticamente no limi
- A lista original de fontes do Proinfa instituído pelo art. 3º da Lei nº 10.438/2002 compre
- Uma central solar fotovoltaica dispõe de potência nominal de 10 MW. Por essa informação is
```

## Pedido 22: FGTS (`direito-trabalho.fgts`, 95 tópicos de edital)

```
Matéria: direito-trabalho.fgts (FGTS)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, trabalhadores abrangidos, depósitos e base de cálculo": 19
- "2. Guias, FGTS Digital, obrigações declaratórias e Certificado de Regularidade": 19
- "3. Hipóteses de saque e modalidades saque-rescisão e saque-aniversário": 22
- "4. FGTS na rescisão: depósitos finais e indenizações de 40% e 20%": 21
- "5. Prescrição do FGTS, fiscalização, regularização e questões integradas": 19

Já existem (não repita):
- Uma pessoa foi contratada como empregada comum e recebeu R$ 2.500,00 de remuneração integr
- Um contrato regular de aprendizagem prevê remuneração de R$ 1.800,00 no mês. Qual valor de
- Uma folha inclui gratificação de Natal de R$ 3.000,00 sujeita à alíquota ordinária. Quanto
- Na competência de agosto de 2026, um empregador sujeito ao FGTS Digital prepara seu recolh
- Uma empresa regular precisa demonstrar a regularidade perante o FGTS em procedimento que e
- Uma pessoa aderiu ao saque-aniversário e foi dispensada sem justa causa em outubro de 2026
- Um trabalhador do regime saque-rescisão é dispensado sem justa causa; a base histórica atu
- Empregado e empregador extinguem o contrato por acordo nos termos do art. 484-A da CLT. Qu
- Um empregado comum foi desligado por iniciativa própria, sem configurar outra hipótese leg
- Um trabalhador questiona a ausência de recolhimentos de FGTS vencidos em 2022. Qual prazo 
- O salário contratual mensal é R$ 3.200,00, e horas extras remuneratórias de R$ 400,00 fora
- A empregadora doméstica paga remuneração de R$ 2.000,00. Considerando apenas os percentuai
- Uma diferença de FGTS referente à competência novembro de 2022 foi descoberta após adoção 
- Um empregador informa corretamente folha e valores do FGTS no eSocial, mas não paga a guia
- Uma empresa deseja apresentar Certificado de Regularidade do FGTS emitido há 35 dias, apes
- Um trabalhador possui R$ 1.000,00 de saldo total nas contas do FGTS e está no saque-aniver
- Um trabalhador optante do saque-aniversário solicita retorno ao saque-rescisão em janeiro 
- Um empregado teve despedida por culpa recíproca reconhecida pela Justiça do Trabalho. A ba
- Em uma dispensa imotivada, a base histórica atualizada de depósitos do vínculo era R$ 30.0
- Uma pretensão de recolhimento de FGTS já tinha prescrição em curso em 13/11/2014. Qual sol
- Um empregador desligou empregado com direito a saque em 5 de abril e ainda não recolhera o
- Um empregador entregou ao empregado R$ 2.000,00 em dinheiro, chamando o pagamento de FGTS 
- Um empregado trabalhou durante contrato que terminou em 31/12/2023 e só ajuizou em outubro
- Uma demanda exige FGTS reflexo de parcela salarial que a própria decisão considerou prescr
- Um antigo cotista do Fundo PIS-PASEP busca valores históricos e questiona por que eles não
- Para empregado comum abrangido pela Lei 8.036/1990, o recolhimento mensal do FGTS correspo
- A gratificação de Natal não integra a base de cálculo do FGTS porque os depósitos mensais 
- No contrato de aprendizagem regularmente enquadrado, a contribuição mensal para o FGTS pos
- O Certificado de Regularidade do FGTS, apresentado pelo empregador, comprova que todos os 
- Na dispensa sem justa causa sujeita ao art. 18, § 1º, da Lei 8.036, a indenização compensa
- Desde março de 2024, a adoção do FGTS Digital transformou a antiga GFIP e a GRF em documen
- O recolhimento mensal do FGTS no ambiente digital vence, em regra, no dia 20 do mês seguin
- No FGTS Digital, uma guia emitida com dados do eSocial pode ser considerada integralmente 
- A sistemática saque-aniversário constitui escolha opcional do titular; mesmo quando a disp
- Na extinção por acordo entre empregado e empregador, prevista no art. 484-A da CLT, o valo
- O Tema 608 do STF consagrou a prescrição quinquenal do FGTS não recolhido, mas o encerrame
- Para depósitos cujo prazo de prescrição já corria em 13/11/2014, a modulação do Tema 608 m
- A dispensa por culpa recíproca ou força maior, quando reconhecida pela Justiça do Trabalho
- Quando o empregado aderiu ao saque-aniversário, a multa rescisória de quarenta por cento d
- O art. 26-A da Lei 8.036/1990 considera não quitado o FGTS pago diretamente ao empregado, 
- O recolhimento doméstico da reserva de 3,2% previsto no art. 22 da LC 150/2015 deve ser so
- O recolhimento de FGTS permanece obrigatório durante o afastamento para serviço militar ob
- Em outubro de 2026, toda pessoa desligada sem justa causa que esteja no saque-aniversário 
- O CRF pode ser exigido nas hipóteses do art. 27 da Lei 8.036, inclusive situações de habil
- As cotas históricas do extinto Fundo PIS-PASEP e o abono salarial são parcelas mensais do 
```

## Pedido 23: Direito digital: regulação da internet, plataformas e novas tecnologias (`direito-digital.regulacao-internet-plataformas-novas-tecnologias`, 95 tópicos de edital)

```
Matéria: direito-digital.regulacao-internet-plataformas-novas-tecnologias (Direito digital: regulação da internet, plataformas e novas tecnologias)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constituição, governança da internet e neutralidade": 17
- "2. Registros, LGPD e responsabilidade das plataformas": 23
- "3. Direitos autorais, software, consumo e direito de resposta": 18
- "4. Crimes digitais, startups e tecnologias emergentes": 24
- "5. Processo eletrônico, audiências virtuais e transformação digital": 18

Já existem (não repita):
- Uma empresa fornece acesso à internet residencial, enquanto outra administra uma rede soci
- Uma plataforma econômica e uma operadora de acesso precisam preservar seus registros obrig
- Uma operadora reduz deliberadamente a velocidade de vídeos de plataforma concorrente somen
- Uma pessoa questiona qual garantia constitucional passou a mencionar expressamente os dado
- Uma pessoa registra um domínio terminado em .br e sustenta ter adquirido, com isso, os dir
- Uma loja online precisa tratar o endereço do comprador para entregar mercadoria, mas ele n
- Uma plataforma é notificada pela pessoa retratada para retirar vídeo não consentido de int
- Uma autoridade pede preservação cautelar de registros de conexão para futura apuração e re
- Uma reportagem verídica e licitamente publicada há muitos anos permanece disponível. A pes
- Um comentário de terceiro configura crime contra a honra e seu ofendido pretende responsab
- Um estudante encontra fotografia artística em site aberto e a republica integralmente em a
- Uma desenvolvedora lançou um programa de computador em 2020, sem registrá-lo. Qual alterna
- Uma pessoa contratada para desenvolver um sistema cria o código dentro das atividades prev
- Uma pessoa adquire produto em loja virtual e comunica desistência quatro dias depois do re
- Uma matéria jornalística publicada em 1º de março causa agravo a uma pessoa, que deseja ex
- Uma invasão de dispositivo informático de uso alheio ocorreu em 2026 com o propósito de ob
- Uma organização inovadora com inscrição no CNPJ há oito anos e receita anual de R$ 12 milh
- Uma instituição insere em blockchain um documento falso recebido de terceiro. A gravação é
- Um código automatizado transfere um token mediante evento recebido de um oráculo, mas uma 
- Uma plataforma recusa acesso a serviço por decisão tomada unicamente a partir de perfil au
- Um pesquisador acessa banco de dados acadêmico não indexado por buscadores, por meio de cr
- Uma petição foi efetivamente transmitida e protocolada no sistema judicial às 23h58 do últ
- Um ato foi disponibilizado no Diário da Justiça eletrônico numa segunda-feira, dia útil, s
- Uma intimação é enviada a portal eletrônico próprio da Lei nº 11.419/2006. A pessoa regula
- Uma pessoa tenta protocolar sua petição durante atendimento por vídeo no Balcão Virtual de
- Uma entidade explica a seus usuários que o Marco Civil da Internet reconhece privacidade, 
- Ao registrar domínio na internet para uma empresa, seu titular adquire por esse único ato 
- Uma operadora de conexão mantém sob sigilo seus registros de conexão por um ano, em ambien
- Uma prestadora de conexão pode guardar por seis meses os registros de acesso do assinante 
- Uma empresa pode fundamentar o uso estritamente necessário dos dados do cliente para execu
- Como a LGPD se apoia no consentimento, uma empresa só pode tratar dados pessoais quando ex
- Um programa de computador original pode ser juridicamente protegido embora o desenvolvedor
- Quando uma desenvolvedora publica gratuitamente uma ilustração original em rede social, a 
- Uma plataforma que, após notificação específica da vítima, deixa de retirar diligentemente
- Após os Temas 987 e 533, a simples presença de qualquer comentário ilícito transforma a pl
- Uma notícia verídica e licitamente obtida, divulgada anos atrás, pode permanecer acessível
- Comentários publicados por usuários abaixo de matéria jornalística são, para fins do proce
- Uma autorização concedida dentro de sandbox regulatório para startup pode estabelecer cond
- Uma denúncia de invasão informática ocorrida em 2026 só poderá resultar no crime do art. 1
- Uma pessoa que elabora sistema de busca pode empregar método computacional semelhante ao d
- O lançamento de informação falsa numa blockchain pública, uma vez validado pelo consenso d
- Mesmo quando código denominado contrato inteligente executa automaticamente uma transferên
- Quando intimação é enviada ao portal próprio da Lei nº 11.419/2006, a ausência de consulta
- Se o sistema judicial estiver tecnicamente indisponível para transmissão da petição no últ
- Ao realizar audiência telepresencial, é legítimo dispensar identificação dos participantes
```

## Pedido 24: Soluções (`ciencias.solucoes`, 95 tópicos de edital)

```
Matéria: ciencias.solucoes (Soluções)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistemas homogêneos, dissolução e eletrólitos": 14
- "2. Solubilidade, saturação e fatores externos": 18
- "3. Unidades de concentração: massa, quantidade de matéria e frações": 34
- "4. Diluição, preparo de soluções, misturas e conversões": 21
- "5. Propriedades coligativas e resolução integrada": 13

Já existem (não repita):
- Uma amostra foi preparada dissolvendo integralmente 10 g de açúcar em 90 g de água, sem fo
- Em certa temperatura, uma solução aquosa contém exatamente a quantidade máxima de um sal q
- A solubilidade de uma substância é 25 g por 100 g de água a 25 °C. Qual massa máxima pode 
- Uma solução foi preparada com 18 g de soluto, completando-se o volume total para 300 mL. Q
- Em um balão de 150 mL há 0,30 mol de substância dissolvida, com volume final aferido. Qual
- Um laboratório dissolveu 0,20 mol de um composto não volátil em 400 g de água. Qual é a mo
- Após dissolução completa, uma mistura contém 15 g de soluto e 85 g de água. Qual é o títul
- Uma mistura líquida idealizada contém 3 mol de soluto e 9 mol de solvente. Qual é a fração
- Para preparar uma solução, mediram-se 25 mL de etanol e acrescentou-se água até completar 
- Uma solução apresenta massa de 250 g e ocupa volume de 200 mL. Qual é sua densidade em g/m
- Em temperatura fixa, um gás apresenta concentração de equilíbrio de 0,8 mmol/L quando sua 
- Um sólido tem solubilidade de 65 g/100 g de água a 70 °C e 25 g/100 g a 20 °C. Uma solução
- Uma alíquota de 40 mL de solução 1,5 mol/L foi transferida para balão e completada até 300
- Misturam-se 100 mL de uma solução 0,20 mol/L e 300 mL de outra, do mesmo soluto, com 0,60 
- Uma substância de massa molar 50 g/mol foi dissolvida, usando-se 15 g para preparar 250 mL
- Uma solução possui densidade 1,10 g/mL e título de 8% em massa. Qual sua concentração comu
- Uma solução líquida idealizada contém 2 mol de substância A e 3 mol de substância B, sem o
- Um frasco contém 210 g de solução homogênea com volume final de 175 mL. Qual valor de dens
- Uma solução inicialmente contém 15 g de soluto e 135 g de água. Evaporam-se 30 g de água, 
- Para uma solução aquosa ideal de soluto molecular não volátil com molalidade 0,20 mol/kg, 
- Compare duas soluções aquosas diluídas ideais: A contém 0,20 mol/kg de glicose, e B contém
- À mesma temperatura, considere solução A de CaCl2 0,10 mol/L totalmente dissociado e soluç
- Um sólido hipotético dissolve 80 g/100 g de água a 80 °C e 30 g/100 g a 25 °C. Uma solução
- Misturam-se 60 mL de solução 1,0 mol/L e 140 mL de solução 0,50 mol/L do mesmo soluto, sem
- Uma solução tem densidade 1,20 g/mL, 25% m/m de soluto de massa molar 60 g/mol, e o restan
- Uma amostra com sal inteiramente dissolvido e composição uniforme em toda a fase líquida é
- Uma solução descrita como concentrada deve ser classificada como saturada, pois os dois te
- Uma amostra de solução tem massa de 180 g e volume de 150 mL. Sua densidade corresponde a 
- Para preparar 200 mL de solução aquosa contendo certo soluto, o procedimento de adicionar 
- Uma solução aquosa de sacarose possui moléculas de sacarose dispersas, enquanto uma soluçã
- Ao acrescentar sal a uma amostra de água até restar excesso sólido em equilíbrio, a fase l
- À mesma temperatura e para um gás que obedece à forma C = kP da lei de Henry, dobrar sua p
- Uma substância sólida apresenta dissolução mais rápida quando a mistura é agitada; por iss
- Se há 0,40 mol de soluto em volume final de 800 mL de solução homogênea, então a concentra
- Ao misturar duas soluções cujo soluto reage quimicamente entre si ou com outro componente,
- Em solução binária de dois componentes, as frações molares somam 1, porque a soma dos mols
- Ao misturar 30 mL de etanol com 120 mL de água, pode-se declarar 20% v/v sem medir a mistu
- Uma solução com densidade de 1,20 g/mL e título de 5% m/m contém 60 g de soluto por litro 
- Na adição de soluto não volátil a um solvente ideal, a pressão de vapor do solvente aument
- Para soluto molecular não eletrolítico em solução diluída ideal, uma molalidade de 0,50 mo
- Soluções ideais com mesma molalidade de glicose e NaCl, este completamente dissociado, apr
- À mesma temperatura, duas soluções ideais de solutos não eletrolíticos apresentam pressões
- Para a lei de Henry em uma mistura gasosa, basta multiplicar a constante pela pressão tota
- Uma solução líquida pode estar saturada sem sólido depositado no recipiente, desde que sua
- Ao aquecer uma solução sem perder soluto, a molaridade conserva necessariamente o mesmo va
```

## Pedido 25: Ações especiais no processo do trabalho (`processo-trabalho.acoes-especiais-rescisoria-seguranca`, 94 tópicos de edital)

```
Matéria: processo-trabalho.acoes-especiais-rescisoria-seguranca (Ações especiais no processo do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Inquérito para apuração de falta grave e procedimentos especiais": 22
- "2. Ação rescisória, coisa julgada e limites da revisão": 33
- "3. Mandado de segurança e tutela de urgência trabalhista": 15
- "4. Ação civil pública e ações anulatórias": 17
- "5. Homologação extrajudicial, consignação e ação monitória": 13

Já existem (não repita):
- Um trabalhador com estabilidade sindical, acusado de falta grave, é suspenso. A empresa pr
- Em inquérito judicial para apuração de falta grave, a parte pretende indicar cinco testemu
- Uma decisão trabalhista de mérito transitou em julgado, mas a parte descobre documento dec
- Uma pessoa apresenta ação rescisória trabalhista e recebe intimação para recolher depósito
- Uma parte toma ciência de ato de autoridade supostamente ilegal e tem todos os documentos 
- Uma decisão transitada em julgado está sendo executada e a parte ajuíza ação rescisória se
- Duas partes resolvem submeter acordo trabalhista extrajudicial para homologação. Seus repr
- Uma associação constituída há três anos, com finalidade estatutária compatível, pretende p
- Uma pessoa pretende exigir quantia a partir de documento escrito sem força de título execu
- Um acordo extrajudicial especifica apenas diferenças de férias e é submetido conjuntamente
- Uma liminar de tutela provisória é deferida em decisão interlocutória antes da sentença, e
- Uma associação formalizada há oito meses, com pertinência temática, pretende ajuizar ação 
- Uma decisão transitada em julgado considerou inexistente fato demonstrado nos autos, mas e
- A sentença trabalhista contém tutela provisória concedida no próprio julgamento, e a parte
- Uma ação rescisória se fundamenta em prova nova cuja existência a parte ignorava e da qual
- Uma empresa oferece pagamento de verbas incontroversas, mas a pessoa credora recusa recebi
- Após negociação, determinada cláusula de convenção coletiva é apontada como violadora de d
- Um inquérito é ajuizado para apuração de falta grave de representante sindical protegido p
- Após decisão judicial definitiva, a parte verifica que o julgamento foi proferido por inte
- Uma tutela provisória é concedida antes da sentença, o impetrante ajuíza segurança alegand
- Uma ação coletiva pretende compelir empresa a adequar proteção contra risco ocupacional e 
- Em homologação de acordo extrajudicial, as partes assinam petição conjunta e utilizam patr
- Um ato de disposição de direitos praticado pelas partes foi homologado em fase de execução
- Uma petição de homologação de acordo extrajudicial especifica duas verbas, mas as partes p
- O devedor ajuíza consignação judicial ordinária para quitar obrigação e, deferido o depósi
- Uma pessoa empregada cuja estabilidade exige inquérito é suspensa por alegada falta grave.
- No inquérito judicial para apuração de falta grave, cada parte só poderá apresentar três t
- Em ação rescisória trabalhista, o art. 836 da CLT prevê depósito prévio de vinte por cento
- Uma ação rescisória pode ser ajuizada por mera discordância com a prova produzida na decis
- Mandado de segurança contra ato de autoridade exige direito líquido e certo demonstrável p
- Para homologar acordo extrajudicial trabalhista, é suficiente petição unilateral do empreg
- Uma tutela provisória é deferida antes da sentença, não há recurso próprio imediato e a pa
- A rescisória trabalhista ajuizada durante execução suspende automaticamente o cumprimento 
- A ação civil pública pode buscar imposição de obrigações de fazer ou não fazer para preven
- A petição conjunta de acordo extrajudicial interrompe a prescrição de todos os direitos de
- Em ação civil pública, associação com finalidade pertinente constituída há apenas oito mes
- Uma empresa deseja cumprir obrigação pecuniária diante da recusa injustificada de recebime
- Em ação rescisória fundada em prova nova do CPC art. 966, VII, o art. 975, § 2º, estabelec
- Uma sentença judicial trabalhista concede tutela provisória em seu próprio dispositivo. A 
- Ação rescisória que impugne apenas um capítulo autônomo da decisão transitada em julgado p
- Quando a ação de homologação extrajudicial é definitivamente rejeitada, o art. 855-E deter
- Uma cláusula coletiva negociada é questionada por violação de direito indisponível. A ação
- No rito de consignação judicial ordinária, se o depósito não é realizado no prazo de cinco
- Na discussão sobre falta grave de empregado com estabilidade sindical, a necessidade de in
- Conforme o CPC art. 966, § 4º, atos de disposição de direitos homologados na execução são,
```

## Pedido 26: Química orgânica: funções e nomenclatura (`ciencias.quimica-organica-funcoes-nomenclatura`, 94 tópicos de edital)

```
Matéria: ciencias.quimica-organica-funcoes-nomenclatura (Química orgânica: funções e nomenclatura)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Carbono, ligações, hibridação e estrutura molecular": 24
- "2. Cadeias, substituintes e nomenclatura dos hidrocarbonetos": 28
- "3. Funções oxigenadas e propriedades": 17
- "4. Funções nitrogenadas, halogenadas e nomenclatura mista": 17
- "5. Propriedades comparadas, reações básicas e nomenclatura integrada": 14

Já existem (não repita):
- Ao representar o metano, CH4, um estudante encontra quatro ligações simples no carbono cen
- Na molécula de eteno, CH2=CH2, um exercício pede apenas as ligações que unem os dois átomo
- Um composto apresenta a cadeia CH3–CH2–OH, em que o oxigênio está na hidroxila final. Como
- Um frasco está rotulado com a estrutura CH3–CO–CH3, contendo carbonila ligada a dois grupo
- A substância CH3–O–CH2–CH3 apresenta oxigênio entre um grupo metila e um etila. Qual alter
- Na estrutura CH3–CH(CH3)–CH2–CH3, a cadeia mais longa possui quatro carbonos, com um grupo
- Etanol e metoximetano possuem fórmula molecular C2H6O, mas pontos de ebulição muito difere
- A substância CH3–CH2–COOH contém três carbonos e um grupo carboxila terminal. Qual é sua f
- Um laboratório precisa diferenciar CH3–CH(OH)–CH3 de CH3–CH2–CH2–OH. Qual é o nome do prim
- Usando a convenção de NOX para ligações com H e O, determine o número de oxidação do carbo
- Um estudante desenhou HC≡CH e pretende contar todas as ligações σ e π da molécula, incluin
- Uma comparação em solução aquosa envolve fenol e etanol em condições semelhantes. Qual con
- Um éster usado em aromatizantes tem fórmula condensada CH3–CH2–C(=O)–O–CH3. Qual é o seu n
- Dada a estrutura CH2=CH–CH(CH3)–CH3, escolha o nome que privilegia a posição da ligação du
- Uma amida simples, CH3CONH2, é comparada a uma amina como CH3NH2. Qual explicação é mais a
- Considere CH3CH2Br e CH3COCl. Ambos contêm halogênio, mas pertencem a classes diferentes. 
- Uma amostra contém CH3–C(=O)–CH3. Pede-se a hibridação do carbono da carbonila, e não a do
- No composto HO–CH2–CH2–COOH, coexistem hidroxila alcoólica e carboxila. Ao selecionar a fu
- Um composto contém substituinte C6H5–CH2– ligado a uma cadeia alifática. Um candidato escr
- Em condições aquosas comparáveis, um estudante explica a menor basicidade da anilina (C6H5
- Um álcool de fórmula CH3–CH(OH)–CH3 sofre oxidação controlada adequada, sem ruptura da cad
- Duas substâncias isoméricas de fórmula C2H3N são representadas pelas conectividades CH3–C≡
- O fragmento CH3–C(=O)–O–C(=O)–CH3 tem duas carbonilas conectadas por um oxigênio central. 
- Na molécula de ácido metanoico, H–C(=O)–OH, calcule o NOX do único carbono considerando C–
- Dois álcoois primários, propan-1-ol e octan-1-ol, possuem um grupo hidroxila cada. Em água
- No etino HC≡CH, cada carbono participa de ligação tripla com o outro e tem arranjo linear,
- No 2-metilpropano, CH3–CH(CH3)–CH3, o carbono central deve ser classificado como secundári
- A molécula CH3–O–CH3 possui cadeia heterogênea na classificação escolar, pois um oxigênio 
- A estrutura CH3–CHO pertence à função cetona, pois o carbono da carbonila se encontra na e
- Ácidos carboxílicos comuns, como o ácido etanoico, tendem a ser suficientemente ácidos par
- O nitrobenzeno, C6H5NO2, possui grupo amino e deve ser classificado como amina aromática, 
- O composto CH3CH2Cl pode ser chamado cloroetano; o halogênio nele está ligado a carbono al
- Na estrutura CH2=CH–CH(CH3)–CH3, o nome 2-metilbut-3-eno é preferível a 3-metilbut-1-eno, 
- A estrutura CH3–C(=O)–N(CH3)2 é uma amida N,N-dissubstituída e pode ser nomeada N,N-dimeti
- O metoximetano, CH3OCH3, estabelece entre suas próprias moléculas ligações de hidrogênio O
- Uma amostra de etanol sofre oxidação em condições apropriadas para produzir etanal. Essa t
- No metilbenzeno, a presença do anel aromático determina que todos os carbonos da molécula 
- O ácido etanoico pode associar-se em dímeros por ligações de hidrogênio, contribuindo para
- A cadeia CH3–CO–CH3 deve ser classificada como insaturada na classificação escolar de cade
- Na molécula CH3NHCH3, o nitrogênio se liga a dois grupos metila e conserva um hidrogênio; 
- Os grupos fenil e benzil possuem mesma conectividade: em ambos o ponto de ligação ao resta
- A fórmula CH3CH2C≡N possui três átomos de carbono no total; por isso sua nomenclatura subs
- Todo composto de fórmula C4H8 é necessariamente um alceno de cadeia aberta, porque a fórmu
- Na estrutura HOCH2CH2COOH, a carboxila deve ser tratada como função principal e a hidroxil
- Com o mesmo grupo funcional hidroxila e sem ionização significativa, o octan-1-ol tende a 
```

## Pedido 27: Princípios do processo do trabalho e aplicação do CPC (`processo-trabalho.principios-processo`, 93 tópicos de edital)

```
Matéria: processo-trabalho.principios-processo (Princípios do processo do trabalho e aplicação do CPC)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fontes e princípios constitucionais do processo do trabalho": 27
- "2. Proteção processual, conciliação, oralidade e simplicidade": 20
- "3. Jus postulandi, concentração, imediatidade e finalidade social": 20
- "4. Interlocutórias, impulso oficial e limites da decisão": 17
- "5. Aplicação subsidiária e supletiva do CPC: testes e casos": 16

Já existem (não repita):
- Em uma reclamação trabalhista, uma parte pede aplicação de regra do CPC embora a CLT já tr
- Durante a instrução, uma parte quer condenação fundada apenas em sua vulnerabilidade econô
- Uma audiência trabalhista começa sem acordo entre as partes. Qual providência é compatível
- Uma pessoa comparece para formular reclamação trabalhista verbal. Qual procedimento decorr
- Uma parte, sem advogado, deseja propor reclamação pessoal no primeiro grau trabalhista. Qu
- Uma audiência de instrução precisa ser continuada porque houve força maior que impediu seu
- O julgador dirige a instrução, determina esclarecimentos e busca andamento célere. Qual di
- No curso do processo, uma decisão interlocutória comum não se enquadra nas exceções reconh
- Uma equipe encontra divergência entre o texto de lei processual aplicável e instrução inte
- Um processo exige apuração sobre a estrutura normativa da Justiça do Trabalho. Qual combin
- Durante audiência, as partes não conciliaram inicialmente. Depois de produzidas provas, de
- Uma parte afirma que, por força da oralidade, reclamação verbal dispensaria redução a term
- O empregado deseja atuar pessoalmente, sem advogado, em mandado de segurança associado a c
- Uma audiência é suspensa por força maior e não pôde terminar no mesmo dia. Considerando o 
- Ao decidir a partir dos depoimentos colhidos pessoalmente em audiência, a pessoa julgadora
- Uma execução trabalhista será iniciada após sentença, e ambas as partes estão representada
- O processo apresenta lacuna sobre um aspecto do contraditório e regra do CPC é coerente co
- Decisão acolhe exceção de incompetência territorial, determinando remessa dos autos a juíz
- Uma decisão interlocutória proferida por Tribunal Regional contraria diretamente orientaçã
- O art. 791 admite atuação pessoal em reclamações trabalhistas. Entretanto, partes apresent
- Em uma demanda, o julgador deixa de exigir formalidade dispensável e usa linguagem acessív
- Existe pedido de reintegração de empregado estável, mas a reintegração mostra-se desaconse
- Um caso envolve incidente da fase de execução não disciplinado pela CLT. A equipe sugere a
- Uma parte pretende impor a audiência de mediação do CPC, art. 334, para substituir a dinâm
- As partes, ambas representadas, pedem ao julgador início de execução de ofício, citando a 
- Em reclamação trabalhista, o julgador deve observar o contraditório e a ampla defesa asseg
- Uma lei processual civil passa a valer automaticamente no processo trabalhista por ser mai
- O art. 764 da CLT incentiva a conciliação em conflitos trabalhistas, permitindo composição
- Para obter celeridade, o julgador pode impor acordo a litigantes que recusaram a proposta,
- Uma reclamação trabalhista pode ser apresentada verbalmente e reduzida a termo, segundo o 
- Por força do art. 791 da CLT, empregado e empregador podem, sem advogado, apresentar qualq
- Uma audiência que não termina no mesmo dia por força maior pode continuar na primeira data
- A Instrução Normativa nº 39/2016 é uma lei de mesma hierarquia que a CLT e, por esse motiv
- A concentração e a imediatidade podem coexistir com registros escritos de depoimentos e fu
- Na hipótese geral de execução trabalhista após a Reforma de 2017, o juiz pode iniciá-la de
- Uma decisão interlocutória comum, fora das exceções, tem seu conteúdo reexaminável em regr
- A regra de irrecorribilidade imediata das interlocutórias significa que nenhuma decisão in
- O art. 15 do CPC de 2015 prevê aplicação supletiva e subsidiária a processos trabalhistas 
- Em ação trabalhista, partes podem transplantar automaticamente o negócio jurídico processu
- Se decisão acolhe incompetência territorial e envia autos a juízo ligado a Tribunal Region
- O princípio protetivo, por si, autoriza condenar a parte demandada em parcelas estranhas a
- A Instrução Normativa nº 39/2016 apresenta orientação não exaustiva sobre o CPC no process
- Quando existir conflito entre a CLT e regimento interno a respeito de regra processual leg
- No caso legal de reintegração de empregado estável que se mostre desaconselhável pelas con
- A audiência civil de conciliação do art. 334 do CPC deve substituir obrigatoriamente as te
```

## Pedido 28: Gestão, liderança e educação permanente em enfermagem (`enfermagem.gestao-lideranca-educacao-permanente`, 93 tópicos de edital)

```
Matéria: enfermagem.gestao-lideranca-educacao-permanente (Gestão, liderança e educação permanente em enfermagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da administração e organização do trabalho em enfermagem": 17
- "2. Liderança, comunicação, decisões e gestão de conflitos": 17
- "3. Dimensionamento, classificação de pacientes e elaboração de escalas": 17
- "4. Gestão de materiais, custos e logística da assistência": 17
- "5. Qualidade, indicadores e segurança da assistência": 16
- "6. Educação permanente e continuada: política, métodos e avaliação": 16

Já existem (não repita):
- Ao organizar rotinas de coleta e reduzir deslocamentos desnecessários, uma coordenadora ap
- A gerente estabelece metas de redução do tempo de espera e escolhe ações e responsáveis pa
- Em uma passagem de plantão, qual comportamento fortalece a comunicação de informações crít
- No planejamento de pessoal de enfermagem, a classificação de pacientes é utilizada princip
- Uma unidade usa o consumo anterior para estimar necessidades futuras de curativos e, depoi
- A quantidade de profissionais disponíveis por turno é exemplo típico de indicador de qual 
- Qual atividade melhor caracteriza a educação permanente em saúde?
- Um novo enfermeiro assume setor com equipe experiente, mas encontra risco imediato de medi
- Qual afirmação descreve corretamente a atualização normativa do dimensionamento de enferma
- Um material barato é indispensável para manter via aérea durante uma emergência. Se faltar
- Em 30 dias, uma unidade registrou 6 quedas e 3.000 pacientes-dia. Qual é a taxa de quedas 
- Após vários incidentes de identificação, a gerência decide avaliar uma ação de EPS. Qual d
- Um organograma e um fluxograma são instrumentos gerenciais distintos. Qual correspondência
- Dois profissionais divergem de forma recorrente sobre distribuição de pacientes complexos.
- Em cálculo meramente didático, há 18 pacientes e previsão hipotética de 5 horas diárias de
- O consumo médio de um insumo é 15 unidades por dia e a entrega leva 4 dias. Sem considerar
- Após implantar um sistema de relato sem punição automática, as notificações de incidentes 
- Sobre o Parecer Normativo 1/2024/Cofen, assinale a opção correta.
- Um treinamento foi realizado, mas o erro persiste porque etiquetas de identificação contin
- Um hospital pretende comparar taxa de quedas entre setores com perfis assistenciais muito 
- Na teoria clássica de Fayol, planejar, organizar, dirigir e controlar são funções associad
- A abordagem das relações humanas reduz a relevância das interações de grupo e considera ap
- Ter cargo de chefia não garante, por si só, capacidade de influenciar positivamente a equi
- O estilo democrático de liderança pressupõe que o enfermeiro renuncie às suas responsabili
- A elaboração da escala deve considerar cobertura assistencial, jornada, competências e fol
- Na gestão de materiais, previsão e provisão são sinônimos e descrevem exclusivamente a con
- No modelo de Donabedian, estrutura, processo e resultado são dimensões usadas para avaliar
- A educação permanente em saúde se caracteriza por aprendizagem ligada a problemas reais e 
- O sistema de classificação de pacientes elimina a necessidade de verificar ausências e jor
- A curva ABC ajuda a priorizar controle financeiro do estoque, mas um item de baixo custo p
- Um quase erro que não atingiu o paciente deve ser automaticamente classificado como evento
- Uma capacitação expositiva pode fazer parte de estratégia de educação permanente, desde qu
- A comunicação em circuito fechado pressupõe que a informação crítica seja enviada sem conf
- Pelo critério FEFO, a prioridade de saída deve considerar a data mais próxima de venciment
- A abordagem contingencial sustenta que o modelo administrativo ideal é imutável e independ
- Horas de assistência calculadas para um dia não equivalem, isoladamente, ao total de profi
- A Resolução Cofen 543/2017 permanece vigente porque o Parecer Normativo 1/2024 apenas alte
- No Parecer Normativo 1/2024/Cofen, o IST mínimo citado é de 15%, associado a férias e ausê
- Um aumento de notificações após cultura de segurança não punitiva prova necessariamente qu
- Avaliar a educação permanente apenas pela satisfação dos participantes é insuficiente para
```

## Pedido 29: Direito ambiental internacional (`direito-ambiental.direito-ambiental-internacional`, 93 tópicos de edital)

```
Matéria: direito-ambiental.direito-ambiental-internacional (Direito ambiental internacional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fontes, princípios e marcos históricos": 14
- "2. Estocolmo, Rio-92 e cooperação ambiental": 14
- "3. Convenção sobre Diversidade Biológica": 28
- "4. Patrimônio mundial cultural e natural": 26
- "5. Dano transfronteiriço e procedimentos preventivos": 18

Já existem (não repita):
- Durante revisão documental, uma equipe afirma que a Declaração de Estocolmo de 1972 é o tr
- Um material preparatório associa números de princípios às conferências internacionais de 1
- Uma indústria licenciada no Estado A libera partículas que alcançam o Estado B. Ao examina
- Moradores de uma região ameaçada por depósito de resíduos solicitam dados ambientais e par
- Uma política pública pretende concretizar a Convenção sobre Diversidade Biológica. Qual co
- Um programa conserva espécies ameaçadas em seu habitat natural mediante criação de áreas p
- Uma pesquisadora identifica o tratado internacional de 1972 relativo ao patrimônio cultura
- Uma área natural brasileira integra a Lista do Patrimônio Mundial e necessita medidas urge
- Uma empresa obtém amostras genéticas em outro país e pretende desenvolver produto comercia
- O Estado A projeta obra que poderá afetar significativamente o ambiente do Estado B. Qual 
- Um novo pesticida apresenta indícios de dano grave e possivelmente irreversível, mas os es
- Em negociação sobre a proteção ambiental global, a delegação de um país propõe considerar 
- Um projeto de exploração florestal pode reduzir a diversidade de espécies que também ocupa
- Uma falha industrial súbita ameaça contaminar um rio que segue para país vizinho. Qual pri
- Um sítio natural classificado como patrimônio mundial passa a sofrer risco específico e gr
- Uma equipe precisa identificar o ato brasileiro de promulgação da Convenção sobre Diversid
- Um empreendimento será desenvolvido conjuntamente no Brasil e em país limítrofe. No regime
- Uma área contém monumentos arqueológicos de relevante valor histórico e formações geológic
- Um país pretende estruturar normas de reparação para vítimas de poluição transfronteiriça 
- Uma autoridade nacional deve decidir sobre instalação de empreendimento com potencial de d
- Uma empresa que opera perto da fronteira detecta perigo grave e iminente à biodiversidade 
- Um Estado cogita aprovar uma atividade deliberada que pode danificar indiretamente patrimô
- Um gestor argumenta que o art. 3º da CDB dá ao Estado liberdade absoluta para explorar rec
- Uma análise jurídica cita a Declaração do Rio de 1992 para afastar, automaticamente, todos
- Um bem cultural não integra a Lista do Patrimônio Mundial, mas seu valor histórico é recon
- Uma cartilha registra que a Declaração de Estocolmo de 1972 contém 26 princípios e que sua
- Uma equipe afirma que a Declaração do Rio de 1992 é uma convenção ambiental que criou obri
- Ao avaliar empreendimento planejado que poderá causar efeitos ambientais adversos signific
- Uma publicação atribui à Convenção do Patrimônio Mundial de 1972 o objetivo triplo de cons
- O planejamento de uma área protegida para manter populações de espécies no ambiente natura
- Após a inscrição de um bem natural na Lista do Patrimônio Mundial, sua preservação passa a
- A administração pretende licenciar obra desenvolvida conjuntamente no Brasil e em país lim
- Um levantamento afirma que o Princípio 15 do Rio autoriza postergar providências ambientai
- Uma instituição mantém sementes de espécies nativas em coleção controlada, fora de seus ha
- Diante de desastre industrial já iniciado, com risco súbito ao ambiente de país vizinho, a
- O art. 15 da CDB admite que o acesso concedido a recursos genéticos seja estruturado por t
- Uma empresa pretende executar atividade em dois Estados brasileiros. Por haver mais de uma
- O art. 11 da Convenção do Patrimônio Mundial prevê tanto a Lista do Patrimônio Mundial qua
- A Constituição brasileira exige estudo prévio de impacto ambiental, com publicidade, para 
- Uma leitura integrada de Estocolmo 21, Rio 2 e CDB 3 permite reconhecer que os Estados exe
- O art. 14, § 1º, c, da CDB trata exclusivamente de danos internos à biodiversidade, razão 
- Nos termos da Convenção do Patrimônio Mundial, a cooperação internacional complementa a at
- O art. 12 da Convenção do Patrimônio Mundial estabelece que a falta de inscrição de um bem
- O Princípio 7 do Rio reconhece responsabilidades comuns, porém diferenciadas, e menciona c
- A Lei Complementar nº 140/2011 permite considerar tacitamente concedida licença ambiental 
```

## Pedido 30: Python: fundamentos da linguagem (`informatica.python`, 93 tópicos de edital)

```
Matéria: informatica.python (Python: fundamentos da linguagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Execução, sintaxe, tipos e operadores": 21
- "2. Strings, coleções e compreensão": 20
- "3. Condicionais, laços, funções e escopo": 22
- "4. Exceções, arquivos, módulos e scripts": 19
- "5. Orientação a objetos e integração dos fundamentos": 18

Já existem (não repita):
- Um script recebe `valor = input()` e o usuário digita 12. Em seguida, executa `print(valor
- Ao calcular `-7 // 2` para determinar o número de grupos inteiros em um programa, qual val
- Uma aplicação define `s = 'python'` e precisa exibir somente os caracteres nas posições 1,
- Um programa executa `dados = [1]`, depois `dados.append([2,3])`. Qual é o conteúdo final d
- Uma rotina precisa armazenar elementos distintos, sem indexação por posição. Qual literal 
- Um laço usa `for n in range(2,9,3): print(n)`. Quais números aparecem, na ordem?
- Em um laço de busca, nenhum item atende ao critério e o `for` chega ao fim sem `break`. Qu
- Uma função `def soma(a,b=3): return a+b` recebe a chamada `soma(5)`. Qual é o valor devolv
- Um programa abre `relatorio.txt` usando `with open('relatorio.txt','w') as f:` e escreve '
- Ao executar `def f(): pass` e depois `resultado = f()`, o programa consulta o valor de `re
- Um código executa `a=[1,2]; b=a; b.append(3)`. Em seguida, imprime `a`. Qual saída revela 
- Um relatório possui `d={'a':1,'b':2}` e executa `d['a']=5`. Qual estado do dicionário está
- Uma compreensão é escrita como `[x*2 for x in range(6) if x%2==1]`. Que lista resulta da a
- Um trecho executa `try: n=int('4')` e tem `except ValueError`, `else: print('ok')` e `fina
- Após terminar um bloco `with open('x.txt','r') as arq: ...`, o programa tenta executar `ar
- Um módulo usa `import math` e depois quer calcular a raiz quadrada de 81. Qual chamada cor
- Uma classe declara `def __init__(self, valor): self.valor = valor`. Depois cria `obj = Cla
- Um programa constrói `a=[[0]]*3` e executa `a[0].append(1)`. Qual é o conteúdo de `a` após
- Uma função é definida com `def f(v, itens=[]): itens.append(v); return len(itens)`. São ch
- Em uma busca, `for x in [1,2,3]:` executa `break` quando encontra `x == 2`, e há um `else`
- Considere `a=[1,[2]]; b=a.copy(); b[1].append(3); b.append(4)`. Qual é o conteúdo final de
- Num módulo existe `x=5` e a função `def f(): x=x+1; return x`. Ao chamar `f()`, o que acon
- Uma classe `Base` define `valor(self)` retornando 2. `Filha(Base)` sobrescreve `valor` e r
- Um bloco `try` calcula `1/0`; seu `except ZeroDivisionError` imprime 'erro'; `else` imprim
- Um dicionário é criado por `d={1:'a',True:'b',1.0:'c'}`. Quantas chaves distintas há e qua
- Uma pessoa usa `print(type(3.0).__name__)` e obtém `float`, porque a presença da parte dec
- Um programa atribui `s='abc'` e executa `s[0]='A'`. A instrução substitui o primeiro carac
- Em uma análise de vendas, `len([10,20,30])` devolve `3`, pois a função conta o número de e
- Uma rotina percorre `range(3)` e registra quatro valores, de 0 a 3, porque o argumento é i
- Um programa precisa adicionar dados a um arquivo existente sem apagar o conteúdo anterior;
- Uma classe usa `def __init__(self, n): self.n=n`. Ao instanciar a classe, `self` deve ser 
- Uma lista é ordenada com `valores.sort()`. Após a operação, a própria lista tem seus eleme
- Na condição `if [0]:`, o ramo condicional é ignorado porque o único elemento da lista é o 
- Um dicionário `{'x':10,'y':20}` é percorrido com `for k in d:`. Nesse caso, os valores atr
- Uma função definida como `def f(*args): return args` devolve uma lista quando é chamada co
- Em uma compreensão `{x: x*x for x in range(3)}`, a expressão produz um dicionário com chav
- Ao importar um arquivo que contém a guarda `if __name__ == '__main__':`, o bloco protegido
- Na função `def f(**kwargs): return kwargs`, a chamada `f(cor='azul', tamanho=3)` produz um
- Dado `d={'a':1}`, a expressão `d.get('b',0)` levanta `KeyError` por tentar acessar uma cha
- Um programa executa `resultado = 0 and (1/0)`; a expressão devolve zero sem efetuar a divi
- Um objeto `a=[1]` é atribuído a `b=a`. Após `b=b+[2]`, o conteúdo de `a` passa a `[1,2]`, 
- Em um `try` cujo bloco termina sem exceção, o `else` pode executar e o `finally` será real
- Uma classe define `itens=[]` no corpo. Duas instâncias sem sobrescrever esse atributo rece
- Um leitor usa `json.loads('{"n": 2}')` e obtém um objeto `dict` de Python contendo a chave
- Em herança, `super()` só funciona dentro de `__init__` e não pode acessar a implementação 
```

## Pedido 31: Átomos, moléculas e íons (`ciencias.atomos-moleculas-ions`, 93 tópicos de edital)

```
Matéria: ciencias.atomos-moleculas-ions (Átomos, moléculas e íons)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria atômica e evolução dos modelos da matéria": 19
- "2. Constituição dos átomos, íons, moléculas e fórmulas químicas": 23
- "3. Massas atômicas, abundância isotópica e organização periódica": 21
- "4. Bohr, hidrogênio, níveis de energia e séries espectrais": 21
- "5. Orbitais, números quânticos, princípio da incerteza e configuração básica": 16

Já existem (não repita):
- Uma representação histórica descreve os átomos como esferas maciças indivisíveis e os comp
- No experimento com folha metálica delgada, quase todas as partículas alfa atravessaram com
- Um átomo neutro do nuclídeo ³⁵₁₇Cl foi isolado para uma análise. Qual contagem de prótons,
- Um átomo neutro de magnésio, com Z=12, perde dois elétrons ao formar Mg²⁺. Quantos elétron
- Em um material há átomos ¹²C e ¹⁴C, ambos com seis prótons, mas quantidades distintas de n
- Uma amostra apresenta três unidades de fórmula de Ca(OH)₂. Quantos átomos de oxigênio estã
- Um elemento fictício apresenta isótopos com massas de 10 u e 11 u, de abundâncias respecti
- Um elemento pertence ao grupo 17 da tabela periódica, junto de flúor e bromo. Qual é a fam
- Usando a aproximação de Bohr para o hidrogênio, qual é a energia do nível n=2, quando o ní
- No espectro de emissão do hidrogênio, um elétron passa do nível n=3 ao nível n=2. Como se 
- Dois nuclídeos, ¹⁴C (Z=6) e ¹⁴N (Z=7), possuem número de massa igual, mas núcleos com carg
- Em uma proposta de números quânticos para um elétron de camada n=2, aparece um orbital des
- Para o carbono neutro, Z=6, os dois elétrons do subnível 2p ocupam orbitais de mesma energ
- Um átomo de sódio neutro, Z=11, é representado pela ordem básica de preenchimento para o e
- Uma imagem de nuvem eletrônica mostra regiões onde há maior chance de encontrar o elétron,
- Um átomo de hidrogênio em n=1 absorve um fóton de 10,2 eV, considerando os níveis ideais E
- Um modelo de ocupação coloca dois elétrons no mesmo orbital 2s, ambos com spin +1/2. Qual 
- Uma molécula orgânica possui fórmula molecular C₆H₁₂O₆. Qual é a fórmula empírica obtida r
- Uma amostra é composta por 75% de isótopos de 35 u e 25% de isótopos de 37 u. Qual média p
- Hélio possui configuração eletrônica fundamental 1s², mas está colocado com Ne e Ar no gru
- No modelo de Bohr, o elétron do hidrogênio passa de n=4 (−0,85 eV) a n=2 (−3,4 eV). Qual r
- Ao selecionar os quatro números quânticos de um elétron para um orbital do subnível 3d, é 
- O íon He⁺ tem núcleo com Z=2 e apenas um elétron, sendo hidrogenoide. Pela fórmula ideal −
- Compare ²³₁₁Na⁺ e ¹⁹₉F⁻. A respeito dos elétrons e dos nêutrons das duas espécies, qual co
- Um átomo neutro possui configuração de subnível 2p³. Na representação fundamental simplifi
- Na experiência de Rutherford com folha metálica, a maioria das partículas alfa atravessou 
- Ao transformar um átomo neutro de sódio, Z=11, em Na⁺, ocorre aumento do número de prótons
- Em uma amostra do mesmo elemento químico, átomos de números de massa diferentes, mas com o
- O número de massa A de um nuclídeo é calculado como média ponderada das massas de todos os
- Na representação convencional da tabela periódica em forma longa, os grupos correspondem à
- Uma transição de emissão de hidrogênio que termina no nível n=1 integra a série de Balmer 
- No modelo de Bohr para o hidrogênio, uma transição eletrônica de n=3 para n=2 provoca emis
- Para um elétron com n=3, admite-se número azimutal l=3, correspondente a um orbital f da t
- Uma representação inicial dos átomos de carbono neutros em estado fundamental escreve 1s² 
- Como a fórmula química NaCl contém um só símbolo de sódio e um de cloro, o sólido cristali
- A unidade u é definida como um doze avos da massa do carbono-12 em estado fundamental, e a
- Os nuclídeos ¹⁴₆C e ¹⁴₇N são isótopos porque compartilham A=14, apesar de terem números de
- Duas linhas de emissão do hidrogênio que terminam em n=2 pertencem à mesma série Balmer; s
- Por apresentar carga nuclear Z=2, o átomo de hélio neutro pode ser tratado sem ajustes com
- Para o íon He⁺, com um elétron e Z=2, a energia ideal do nível n=2 na aproximação hidrogen
- Um subnível p contém um único orbital espacial capaz de acomodar seis elétrons, sendo perm
- Em um átomo, dois elétrons podem ter os mesmos n, l e m_l desde que seus spins sejam opost
- O princípio da incerteza de Heisenberg significa apenas insuficiência dos instrumentos atu
- O elemento cloro pertence ao grupo 17 e ao terceiro período; mesmo quando se torna Cl⁻, co
- A quádrupla n=2, l=1, m_l=2 e m_s=−1/2 é permitida para um elétron em 2p porque o número m
```

## Pedido 32: Museologia e gestão do patrimônio cultural (`cultura.museologia-patrimonio`, 93 tópicos de edital)

```
Matéria: cultura.museologia-patrimonio (Museologia e gestão do patrimônio cultural)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, teoria museológica e tipologias": 17
- "Gestão de museus, legislação e ética profissional": 17
- "Documentação, inventário, pesquisa e gestão de acervos": 17
- "Conservação preventiva, segurança e gestão de riscos": 17
- "Curadoria, exposições, expografia e tecnologias": 16
- "Educação museal, acessibilidade e proteção do patrimônio cultural": 16

Já existem (não repita):
- A Museologia limita-se à execução técnica de exposições, enquanto a museografia estuda as 
- Museus comunitários e ecomuseus podem valorizar referências culturais existentes no territ
- A Política Nacional de Museus foi lançada em 2003, antes da Lei nº 11.904/2009, que instit
- O inventário e a catalogação de acervos são técnicas idênticas: ambas consistem exclusivam
- A conservação preventiva busca reduzir riscos de deterioração por medidas de monitoramento
- No Brasil, o registro de patrimônio cultural imaterial e o tombamento de bens materiais sã
- A definição de museu adotada internacionalmente pelo ICOM em 2022 possui, por si só, a mes
- O Plano Museológico deve articular missão, diagnóstico, programas, projetos e avaliação, s
- A Resolução Normativa Ibram nº 6/2021, relativa ao INBCM, dispensa os museus de manter a d
- A perda de dados que vinculam um objeto à sua procedência pode constituir dano patrimonial
- A digitalização do acervo é medida suficiente de preservação digital, pois arquivos eletrô
- As ações educativas e de salvaguarda relacionadas ao patrimônio imaterial devem reconhecer
- Uma iniciativa que pesquisa memórias de um território e organiza percursos participativos 
- O Código de Ética do ICOM, mesmo após revisão internacional aprovada em 2026, revoga as re
- Um museu pode registrar "autoria desconhecida" em ficha catalográfica sem violar o rigor d
- Para bens de materiais diversos, adotar um único valor de umidade relativa, sem avaliar co
- A avaliação formativa de uma exposição pode provocar alterações no percurso e nas legendas
- Quando uma festividade tradicional possui edifício de referência histórica, o registro da 
- Em um curso introdutório, um candidato deve identificar o conceito que melhor descreve a m
- Qual situação exemplifica mais claramente um ecomuseu ou uma iniciativa territorial de mus
- O principal instrumento de planejamento estratégico de um museu, que integra missão, diagn
- Qual órgão federal atua especificamente na política nacional do setor de museus brasileiro
- Uma equipe atribuiu número de identificação, denominação, medidas e procedência a cada ite
- O Inventário Nacional dos Bens Culturais Musealizados (INBCM) é coordenado por qual instit
- Qual medida é um exemplo inequívoco de conservação preventiva?
- O termo que designa o planejamento espacial, de suportes, iluminação e comunicação de uma 
- O instrumento de proteção mais associado, no âmbito federal brasileiro, à inscrição de um 
- São exemplos de categorias dos Livros de Registro do patrimônio cultural imaterial:
- Ao comparar tipologias, um museu municipal de ciência com visitas virtuais e atividades no
- Uma instituição possui calendário anual de visitas, mas não definiu missão, prioridades, i
- Sobre a Política Nacional de Educação Museal, assinale a afirmação compatível com sua disc
- Ao avaliar proposta de doação de uma peça sem comprovação de origem, a equipe responsável 
- Qual prática distingue a preservação da integridade da documentação museológica em movimen
- Na análise de riscos de uma coleção, o museólogo identifica objetos sem qualquer relação c
- No empréstimo internacional de uma obra sensível, qual decisão combina prevenção e gestão 
- A equipe aplica testes de leitura das legendas com visitantes antes da montagem definitiva
- Um museu digitalizou milhares de fotografias e afirma estar imune a perdas de informação. 
- Uma celebração tradicional reúne cânticos, saberes culinários e percursos rituais. Qual pr
- Uma instituição quer interpretar memórias do trabalho e anuncia que terá apenas fotografia
- Uma direção pretende vender parte do acervo musealizado para custear a programação cultura
- Um edital faz referência a normas internacionais sobre museus e a diretrizes brasileiras d
- Ao revisar banco de dados de 5.000 objetos, a equipe percebe que 90 itens têm dois números
- Um pesquisador propõe preencher a autoria não identificada de uma peça com o nome de um ar
- Um museu abriga papel, metais e têxteis em sala sujeita a oscilações ambientais. Qual solu
- Durante um incêndio na reserva técnica, uma ordem administrativa determina que todos resga
- Uma exposição sobre conhecimentos de comunidade tradicional projeta depoimentos sem consen
- Uma equipe apresenta como "plenamente acessível" uma mostra que oferece somente um aplicat
- Um município deseja reconhecer uma feira tradicional enquanto prática social e proteger um
- Em oficina patrimonial, moradores questionam a narrativa oficial sobre um local e apresent
```

## Pedido 33: Terrorismo, genocídio e crimes contra o Estado Democrático de Direito (`legislacao-penal-especial.terrorismo-genocidio-estado-democratico`, 92 tópicos de edital)

```
Matéria: legislacao-penal-especial.terrorismo-genocidio-estado-democratico (Terrorismo, genocídio e crimes contra o Estado Democrático de Direito)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais, terrorismo e suas elementares": 14
- "2. Organização, financiamento, preparação e processamento do terrorismo": 14
- "3. Genocídio: grupo protegido, intenção especial e punição": 23
- "4. Do fim da Lei de Segurança Nacional aos tipos do Título XII": 34
- "5. Convenções, direitos humanos, competência e aplicação integrada": 15

Já existem (não repita):
- Em aula sobre defesa constitucional, uma pessoa afirma que o terrorismo recebe tratamento 
- Um grupo sabota violentamente instalação essencial por preconceito religioso, visando prov
- Pessoas participam de manifestação reivindicatória para defender garantias constitucionais
- Um participante integra conscientemente organização voltada à prática de terrorismo legalm
- Uma pessoa executa atos preparatórios inequívocos para atentado terrorista, ainda sem inic
- Uma pessoa pretende destruir parcialmente, como tal, grupo religioso, transferindo forçada
- Quatro pessoas se associam com a finalidade de praticar genocídio nos termos da Lei 2.889/
- Uma pessoa consulta legislação atual e vê referência à Lei 7.170/1983 sobre segurança naci
- Um grupo tenta, mediante violência, depor governo legitimamente constituído. Para identifi
- Uma pessoa tenta abolir violentamente o Estado Democrático, impedindo o exercício de poder
- Uma campanha virtual critica intensamente os poderes constitucionais, sem violência ou gra
- Uma pessoa recebe recursos destinados especificamente a financiar atividade terrorista, co
- Após crime descrito na Lei de Terrorismo, discute-se a atribuição de investigação e julgam
- Uma pessoa incita direta e publicamente a prática de genocídio e o crime incitado é efetiv
- Um Estado solicita auxílio internacional para investigar delito terrorista coberto pela Co
- Uma pessoa é investigada por genocídio por submeter intencionalmente grupo étnico a condiç
- Uma pessoa recruta indivíduos para treinamento destinado à prática terrorista, mas os atos
- Uma pessoa inicia preparação inequívoca de terrorismo e voluntariamente impede a continuaç
- Um indivíduo realiza tentativa de genocídio sem consumação e a defesa discute o cálculo le
- Uma pessoa fornece senha a agente estrangeiro para facilitar espionagem abrangida pelo art
- Uma pessoa divulga material classificado como secreto, com o objetivo comprovado de expor 
- Um ataque interfere na aferição do resultado de eleição mediante violação indevida da segu
- Dois países pretendem transferir temporariamente pessoa presa para prestar depoimento rela
- No precedente relativo ao massacre de Haximu, houve condenação exclusivamente por genocídi
- Um pedido de assistência por terrorismo é apresentado como cooperação internacional, mas a
- Em investigação sobre sabotagem, a tipificação de terrorismo exige verificar, além do ato 
- Em passeata com propósito de defender liberdades constitucionais, a ressalva do art. 2º, §
- Uma pessoa presta auxílio relevante a organização voltada a terrorismo legalmente definido
- Uma investigação sobre preparação inequívoca de terrorismo deve encerrar-se sem responsabi
- Um agente atua com intenção de destruir parcialmente grupo étnico e determina a transferên
- Uma pessoa consulta a Lei 7.170/1983 para punir conduta ocorrida após a entrada em vigor d
- Uma manifestação jornalística critica decisões dos poderes constitucionais sem violência o
- Uma pessoa mantém recursos com finalidade comprovada de financiar crimes da Lei 13.260/201
- Uma ocorrência enquadrada na Lei 13.260/2016 é investigada no âmbito da competência federa
- Três pessoas se associam para praticar genocídio, sem outro participante, e se conclui que
- Em incitação direta e pública para cometer genocídio, comprovada posteriormente a consumaç
- Uma pessoa tenta depor governo legitimamente constituído por violência e sustenta que o ar
- Uma eleição tem sua apuração perturbada por violação indevida dos mecanismos de segurança 
- Em cooperação por terrorismo, o art. 11 da Convenção Interamericana afasta a invocação de 
- Uma pessoa entrega a agente estrangeiro informação classificada como ultrassecreta, cuja r
- O art. 5º da Lei 2.889/1956 pune tentativa de genocídio com pena reduzida em dois terços, 
- Uma autoridade estrangeira requer medida patrimonial relacionada a terrorismo; não há trat
- Em processo por genocídio, a condenação do caso Haximu foi revista pelo Supremo para deter
- Uma pessoa é acusada de violência para impedir direitos políticos de outra por religião. O
- Uma investigação de terrorismo pede a aplicação de tratado estrangeiro para julgar atos pr
```

## Pedido 34: Governança corporativa, ASG e finanças sustentáveis (`mercado-capitais.governanca-esg-financas-sustentaveis`, 92 tópicos de edital)

```
Matéria: mercado-capitais.governanca-esg-financas-sustentaveis (Governança corporativa, ASG e finanças sustentáveis)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Governança corporativa, agência e stakeholders": 22
- "2. Ofertas públicas de aquisição e microestrutura dos mercados": 17
- "3. Divulgação ASG, TCFD e normas ISSB": 26
- "4. Instrumentos sustentáveis, taxonomia e índices": 20
- "5. Inovação, criptoativos e estruturas de investimento": 15

Já existem (não repita):
- Uma companhia separa propriedade acionária e gestão, e sua diretoria passa a aprovar vanta
- Uma pessoa possui o maior lote de ações, mas não assegura eleição da maioria dos administr
- Após estudar riscos e alternativas, a administração aprova um investimento que posteriorme
- Ao avaliar uma empresa listada no Novo Mercado, um investidor a compara com o IGC B3. Qual
- A administração consulta empregados, clientes e comunidades afetadas por uma fábrica. Sob 
- Um investidor anuncia OPA para obter o controle de companhia aberta, sem que o atual contr
- O bloco controlador de companhia aberta é alienado a R$ 60 por ação. Sem proteção estatutá
- Uma OPA original oferece R$ 20 por ação. Um terceiro pretende lançar uma OPA concorrente n
- Uma sociedade emite novas ações e recebe dinheiro diretamente dos investidores. Em seguida
- Uma companhia organiza relatório de clima em governança, estratégia, gestão de riscos e mé
- Uma emissora deseja divulgar riscos de sustentabilidade que afetam seu acesso a financiame
- Uma empresa compra eletricidade de distribuidora, queima combustível em caldeira própria e
- Em outubro de 2026, uma companhia aberta avalia se fará relatório financeiro de sustentabi
- Uma securitizadora anuncia certificados de recebíveis como instrumentos de financiamento a
- Uma emissão prevê que o cupom da dívida aumentará se a empresa não cumprir meta verificáve
- Uma empresa apresenta um projeto como compatível com a Taxonomia Sustentável Brasileira ap
- Um analista deve classificar três índices: um de sustentabilidade empresarial, outro de go
- Um projeto vende tokens que dão direito à participação em receitas geradas principalmente 
- Uma plataforma eletrônica promove oferta de participação econômica em pequeno empreendimen
- Uma empresa divulga ser neutra em carbono sem indicar escopos, período, método, linha de b
- Uma companhia pretende cancelar o registro de negociação e estrutura OPA. Parte dos acioni
- Após celebrar instrumento definitivo de alienação direta de controle de companhia aberta, 
- Uma companhia opta, no regime da Resolução CVM 193 após a alteração de 2026, por divulgar 
- Uma emissora quer distinguir as debêntures incentivadas daquelas de infraestrutura e afirm
- Um protocolo DeFi usa contrato inteligente para distribuir pagamentos e vende tokens que p
- Em companhia aberta, a contratação de empresa ligada ao controlador, em condições prejudic
- Quando trabalhadores e comunidades são reconhecidos como stakeholders, adquirem automatica
- Uma OPA que busca adquirir o controle por meio de proposta dirigida aos acionistas é conce
- Na negociação secundária de ação já emitida entre dois investidores, o dinheiro pago ao ve
- Em projeto financiado por green bond estruturado por uso de recursos, a destinação aos pro
- Se uma companhia obtém asseguração limitada de inventário de emissões, seus lucros futuros
- Uma empresa que usa eletricidade comprada em suas unidades deve analisar emissões indireta
- A presença de ações no ISE B3 prova que a empresa tem risco de crédito nulo, razão pela qu
- Um token representativo de direito a receitas futuras pode demandar análise como valor mob
- Um ambiente de sandbox regulatório suspende toda regra sobre proteção dos investidores e d
- Na Resolução CVM 215, o dever de laudo de avaliação quando a OPA é formulada por controlad
- Toda OPA facultativa que inclua permuta de valores mobiliários deve necessariamente seguir
- Ao avaliar a negociação secundária de título tokenizado em plataforma eletrônica, a difere
- Por classificar atividades de contribuição climática, a Taxonomia Sustentável Brasileira c
- Uma atividade sujeita ao futuro funcionamento pleno do SBCE deve distinguir unidades recon
- Um fundo pode utilizar expressão ligada à sustentabilidade em sua denominação ainda que se
- Ao securitizar direitos creditórios de uma cadeia produtiva, o emissor transforma fluxos e
- A emissão de token que representa uma tonelada de carbono cria necessariamente um crédito 
- Em campanha remunerada de recomendação de investimento, ocultar vínculo comercial pode amp
- Conforme o art. 58 da Resolução CVM 215, cada aumento de preço realizado após o lançamento
```

## Pedido 35: Neurocirurgia (`medicina.neurocirurgia`, 92 tópicos de edital)

```
Matéria: medicina.neurocirurgia (Neurocirurgia)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, avaliação neurocirúrgica e hipertensão intracraniana": 17
- "Traumatismo cranioencefálico e trauma raquimedular: indicações cirúrgicas": 17
- "Tumores do sistema nervoso central e neuro-oncologia cirúrgica": 17
- "Hidrocefalia, neurocirurgia pediátrica e infecções de dispositivos": 17
- "AVC hemorrágico, aneurismas e neurocirurgia vascular": 16
- "Doenças da coluna, neurocirurgia funcional e cuidados perioperatórios": 16

Já existem (não repita):
- No TCE grave adulto monitorado, a pressão de perfusão cerebral pode ser estimada pela dife
- A tomografia de crânio sem contraste costuma ser o exame inicial para investigação rápida 
- O hematoma epidural traumático costuma ter aspecto semilunar que cruza suturas, enquanto o
- Uma derivação ventriculoperitoneal é uma forma de desvio interno de líquor habitualmente u
- A ressecção cirúrgica de tumores encefálicos deve ser maximalista mesmo quando a única for
- A clipagem microcirúrgica e a embolização endovascular são estratégias de exclusão do aneu
- A estimulação cerebral profunda é um procedimento voltado a modular circuitos neurais, com
- No TCE grave de adultos, a elevação sustentada da PIC acima de 22 mmHg é um dos critérios 
- Um hematoma subdural agudo de 11 mm de espessura não necessita de evacuação se o desvio de
- Diante de déficit medular traumático inexplicado pela TC, a RM pode identificar lesão medu
- A codeleção 1p/19q isolada, independentemente do estado mutacional de IDH, basta para esta
- Terceiro-ventriculostomia endoscópica e derivação ventriculoperitoneal podem ser opções em
- Em hematoma cerebelar espontâneo com compressão de tronco encefálico, a drenagem ventricul
- Em paciente com sinais de síndrome da cauda equina, a ausência de dor lombar intensa não p
- No TCE grave, a hiperventilação profilática profunda e prolongada deve ser rotina porque o
- Um epidural traumático com 28 cm³, 12 mm de espessura, desvio de 3 mm, Glasgow 14 e nenhum
- Quando o líquor mostra sinais de disseminação leptomeníngea tumoral, a ressecção macroscóp
- Na hidrocefalia pós-hemorrágica da prematuridade, punções lombares seriadas de rotina e ac
- A isquemia cerebral tardia após HSA aneurismática pode surgir mesmo sem correspondência si
- A diretriz AHA/ASA de 2022 inclui hematoma cerebelar espontâneo superior a 15 mL entre os 
- Em suspeita de ventriculite relacionada a uma DVE, culturas diárias de LCR de vigilância s
- Em um adulto com TCE grave, qual expressão aproxima a pressão de perfusão cerebral?
- Um paciente apresenta coleção biconvexa hiperdensa após trauma temporal. Qual hipótese ana
- Qual exame fornece melhor caracterização inicial da medula e de ligamentos após TC óssea e
- Na hidrocefalia aguda com necessidade de drenagem temporária do líquor e possibilidade de 
- Qual par de marcadores define, no contexto apropriado, o oligodendroglioma difuso do adult
- Qual estratégia intervencionista trata diretamente a fonte de sangramento de um aneurisma 
- Em que situação a cirurgia de coluna tende a exigir avaliação urgente, em vez de simples t
- Qual afirmação define adequadamente a finalidade da estimulação cerebral profunda?
- Paciente com epidural traumático de 34 cm³, Glasgow 15 e sem paresia. Considerando diretri
- Adulto com TCE apresenta subdural agudo de 9 mm, desvio de linha média de 7 mm e Glasgow 1
- No TCE grave, qual estratégia integra o controle de PIC sem deteriorar desnecessariamente 
- Qual combinação de achados orienta um glioma para biópsia estereotáxica, em vez de ressecç
- Criança com estenose do aqueduto e hidrocefalia obstrutiva é avaliada para evitar implante
- Após HSA aneurismática confirmada, qual objetivo se relaciona à clipagem precoce ou emboli
- Paciente com mielopatia cervical progressiva, hiperreflexia e estenose central concordante
- Após implantação de DVP, criança evolui com febre, sonolência e hiperemia sobre o trajeto 
- Paciente com TCE grave apresenta PAM de 82 mmHg e PIC de 27 mmHg. Qual é a PPC aproximada 
- Hematoma epidural mede 26 cm³, 12 mm de espessura e 3 mm de desvio; paciente tem Glasgow 9
- Uma paciente com glioma difuso IDH-mutado não apresenta codeleção 1p/19q, mas tem promotor
- Prematuro com hidrocefalia pós-hemorrágica recebe avaliação para reduzir necessidade de sh
- Paciente com hemorragia cerebelar espontânea de 18 mL, hidrocefalia e compressão do tronco
- Homem com HSA aneurismática chega a centro especializado estável após identificação de ane
- Após cirurgia de tumor da fossa posterior com DVE, há febre e piora de consciência. Qual r
- Pessoa com lombalgia e neoplasia conhecida evolui com fraqueza progressiva, alteração de m
```

## Pedido 36:  identificação, balística e fotografia pericial (`criminalistica.identificacao-balistica-fotografia`, 91 tópicos de edital)

```
Matéria: criminalistica.identificacao-balistica-fotografia ( identificação, balística e fotografia pericial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fotografia pericial, documentação e cadeia de custódia": 18
- "2. Identificação humana: papiloscopia e bancos de perfis genéticos": 33
- "3. Identificação de armas de fogo e balística forense": 18
- "4. Rastreabilidade de metais preciosos e análise isotópica": 16
- "5. Integração dos bancos forenses, qualidade e perspectivas tecnológicas": 15

Já existem (não repita):
- Durante o levantamento de uma sala, o examinador descreve minuciosamente no laudo a posiçã
- Ao examinar um cadáver em seu local de encontro, a equipe precisa definir o registro fotog
- Uma marca de contato não se distingue a olho nu sobre papel e torna-se visível após tratam
- Um laboratório consulta perfis de DNA de vestígios criminais por meio da rede instituída p
- Após um disparo, recolhem-se um estojo deflagrado próximo à arma e um elemento metálico qu
- Um estudo pretende descrever o movimento de um projétil entre a saída do cano e o primeiro
- Um conjunto de estojos e projéteis é destinado a uma base nacional de confronto balístico.
- Em uma amostra de ouro, realiza-se fluorescência de raios X para investigar quais elemento
- Dois arquivos fotográficos apresentam o mesmo resumo SHA-256, calculado nas mesmas condiçõ
- Uma lesão plana é fotografada em ângulo oblíquo, enquanto uma régua permanece afastada de 
- Para evidenciar marcas discretas, o examinador ajusta o contraste de cópia de uma fotograf
- Uma impressão latente parcial é pesquisada em sistema automatizado, que retorna uma lista 
- Vestígio biológico analisado apresenta mais alelos por marcador do que seria esperado para
- No confronto de dois projéteis de canos diferentes, aparecem estriações semelhantes em vár
- Em dois locais, recolheram-se estojos com marcas compatíveis de face de culatra e percusso
- Uma amostra e referências geológicas são avaliadas pela razão 87Sr/86Sr. A amostra é compa
- Um banco reúne resultados laboratoriais e arquivos fotográficos de vestígios de vários cas
- Uma equipe preservou o local, reconheceu uma mancha relevante e fotografou suas dimensões 
- Ao descrever o regime jurídico dos perfis genéticos, um relatório afirma que a coincidênci
- Uma impressão de dedo encontrada em superfície irregular permite observar parte de um dese
- Uma coincidência em marcadores STR de material encontrado em um objeto é relatada com freq
- Dois estojos possuem calibre compatível e marcas de percussor semelhantes. A arma consider
- Uma pesquisa no Banco Nacional de Perfis Balísticos retorna cinco candidatos ordenados por
- Um laudo propõe determinar a origem exclusiva de ouro natural pela razão entre dois isótop
- Fotografia, perfil de DNA e vinculação balística são associados a um mesmo caso. A foto do
- Em um local com arma e vestígios, a fotografia de relação deve documentar onde um elemento
- Segundo o art. 164 do Código de Processo Penal, a fotografia do cadáver pode ser feita ape
- Em papel poroso, a revelação de impressões papilares latentes pode empregar reagentes como
- O Banco Nacional de Perfis Genéticos, instituído pelo Decreto 7.950/2013, tem como finalid
- O número de raias de um cano e sua orientação constituem características de classe, de mod
- Uma medição de composição elementar por XRF que encontre 90% de ouro em uma liga demonstra
- Para acompanhar a integridade de imagens, recalcular um hash no arquivo recebido pode dete
- Para impressões latentes em suportes de qualquer natureza, a ninidrina deve substituir a a
- No procedimento ACE-V, a análise da qualidade do vestígio antecede a avaliação conclusiva,
- Uma coincidência de perfil genético entre vestígio e referência demonstra necessariamente 
- Um projétil ainda dentro do cano se relaciona a fenômenos de balística interna; depois da 
- O Sistema Nacional de Análise Balística foi criado pelo Decreto 7.950/2013 como rede de co
- A razão isotópica 87Sr/86Sr pode variar entre ambientes geológicos e contribuir para estud
- Por ter documentado dezenas de fotografias de vestígios, uma equipe considera dispensáveis
- A Lei 15.295/2025 alterou regras brasileiras de identificação por perfil genético, inclusi
- Quando um perfil genético tem frequência estimada de um em um milhão na população de refer
- Na balística comparativa, marcas semelhantes produzidas por uma ferramenta de fabricação c
- Uma razão entre dois isótopos estáveis do próprio ouro pode ser medida diretamente como as
- Em um banco integrado, registrar origem da amostra, protocolo analítico, versões de arquiv
- Um sistema de inteligência artificial treinado em imagens de estojos retorna semelhança de
```

## Pedido 37: Regras Mínimas da ONU para o Tratamento de Pessoas Presas (`direito.regras-minimas-tratamento-presos`, 91 tópicos de edital)

```
Matéria: direito.regras-minimas-tratamento-presos (Regras Mínimas da ONU para o Tratamento de Pessoas Presas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regras de Mandela: dignidade, custódia, separação e condições materiais": 16
- "2. Saúde, disciplina, isolamento, coerção e proteção contra abusos": 16
- "3. Informação, visitas, petições, inspeções e reintegração social": 16
- "4. Regras de Bangkok: mulheres presas, maternidade, revistas e alternativas": 39
- "5. Convenção Americana, princípios da ONU e integração das garantias": 13

Já existem (não repita):
- Uma pessoa recém-custodiada é ameaçada de castigo físico como método de disciplina. Qual p
- Uma unidade planeja colocar pessoas sem sentença condenatória junto das condenadas para si
- Durante fiscalização, verifica-se que a água potável é entregue somente em horários de ref
- Uma pessoa presa adoece e lhe é exigido pagamento por atendimento básico porque ainda não 
- Em apuração de falta interna, determina-se a redução da alimentação e da água de um grupo,
- Uma pessoa deseja formalizar reclamação sobre condições de higiene e teme represálias. Qua
- Uma gestante privada de liberdade entra em trabalho de parto e é mantida algemada para fac
- Uma pessoa detida solicita revisão judicial da legalidade da prisão e, se ilegal, a soltur
- Uma pessoa passa 23 horas diárias sozinha sem contato humano significativo durante dez dia
- Um regulamento interno pretende permitir isolamento solitário por 16 dias consecutivos, co
- Ao ingressar em uma unidade, uma pessoa apresenta lesões e informa ter sofrido violência d
- Uma mulher presa será revistada pessoalmente por equipe masculina treinada, embora haja fu
- Uma mãe presa recebe como punição a interrupção integral do contato com seu filho, por dec
- Uma criança vive temporariamente na unidade com sua mãe. A administração precisa decidir s
- Um sistema de fiscalização de estabelecimentos se limita a revisões conduzidas pela própri
- Uma pessoa presa precisa conversar reservadamente com a defesa, mas se oferece apenas comu
- O regime de custódia prevê deixar pessoa isolada por 21 horas diárias sem contato humano s
- Uma mulher lactante com bebê no estabelecimento recebe segregação disciplinar por infração
- Uma pessoa presa demonstra capacidade para trabalho educativo e quer acesso a estudo duran
- Uma pessoa estrangeira presa em outro país não recebeu orientação sobre comunicação consul
- Uma gestante responsável por criança dependente enfrenta decisão sobre prisão. Há alternat
- Uma pessoa aguardando julgamento é tratada como condenada e não tem oportunidade de comuni
- Uma direção alega que a inspeção administrativa interna cumpre todas as exigências interna
- Durante avaliação de prisão pré-julgamento, a autoridade identifica alternativas adequadas
- Um relatório afirma que a Convenção Americana e as Regras de Mandela possuem idêntica natu
- Uma pessoa privada de liberdade conserva sua dignidade inerente e deve ser protegida contr
- A separação de pessoas em prisão provisória e condenadas é providência facultativa sem pre
- A Regra 22 de Mandela assegura alimentação adequada e água potável quando necessária, sem 
- Como a administração deve manter a disciplina, a Regra 43 de Mandela admite retirar a água
- No estabelecimento prisional, a assistência de saúde deve ter padrão equivalente ao oferta
- Pela Regra 83 de Mandela, a inspeção de estabelecimentos prisional deve ser efetuada somen
- As Regras de Bangkok complementam os padrões gerais de custódia, considerando necessidades
- O confinamento por exatamente 22 horas diárias sem contato humano significativo não se enq
- Uma sanção de isolamento por 17 dias consecutivos, cumprida por ao menos 22 horas diárias 
- Pelas Regras de Mandela, a equipe de saúde é responsável por decidir a imposição de confin
- O mecanismo de reclamação das Regras de Mandela prevê proteção contra represálias e apreci
- A proteção de Bangkok à revista pessoal determina que mulheres presas sejam revistadas pre
- Pela Regra 24 de Bangkok, instrumentos de contenção não devem ser utilizados em mulheres d
- As Regras de Tóquio, aprovadas pela resolução A/RES/45/110, disciplinam apenas medidas não
- A Convenção Americana, em seu art. 5.4, prevê separação entre processados e condenados, re
- A Regra 64 de Bangkok determina soltura automática de mulheres grávidas ou responsáveis po
- O Princípio 16 do Conjunto de Princípios de 1988 protege, entre outras situações, a notifi
- Em razão de infração disciplinar comprovada, a administração poderá proibir integralmente 
- A Regra 6.1 de Tóquio orienta utilização da prisão anterior ao julgamento como último recu
- Como a Convenção Americana integra o sistema internacional de direitos humanos, a exigênci
```

## Pedido 38: Marinharia: cabos, poleame, fundeio, amarração, convés e conservação (`marinha-mercante.marinharia-conves-conservacao`, 91 tópicos de edital)

```
Matéria: marinha-mercante.marinharia-conves-conservacao (Marinharia: cabos, poleame, fundeio, amarração, convés e conservação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Cabos, nós e poleame": 21
- "2. Fundeio, amarração e aparelhos de carga": 21
- "3. Equipamentos de convés e sua manutenção": 17
- "4. Corrosão, preparação de superfície e pintura": 41

Já existem (não repita):
- Cabos de poliamida (náilon) são muito elásticos, enquanto os de poliéster têm baixa elasti
- O cabo de fibra sintética que flutua na água, por ter densidade menor que a dela, é o de:
- Na formação de um cabo de fibra, os fios torcidos formam:
- A bitola de um cabo de aço deve ser medida pelo diâmetro, com paquímetro, entre as cristas
- O nó que forma uma alça fixa, que não corre, e é usado para içar ou salvar pessoas é o:
- Para unir dois cabos de bitolas diferentes, o nó mais indicado é o nó direito.
- O utensílio do marinheiro, de forma cônica, usado para abrir as pernas do cabo e fazer cos
- No poleame, o moitão tem uma única roldana (gorne), e o cadernal tem dois ou mais gornes.
- O aparelho de laborar formado por dois cadernais é a:
- Em um aparelho de laborar com quatro pernas de cabo sustentando o bloco móvel, desprezando
- O rendimento real de um aparelho de laborar é menor que o teórico por causa do atrito nas 
- No ferro (âncora), a argola onde se prende a amarra chama-se:
- O ferro do tipo almirantado caracteriza-se por possuir cepo, uma barra transversal que aju
- A bordo, os ferros principais, situados na proa e prontos para uso, são chamados de:
- A amarra é dividida em quartéis de cerca de 15 braças (27,5 m), unidos entre si por manilh
- A boia de arinque tem a função de:
- Os mordentes e as boças são usados para travar a amarra, aliviando o esforço sobre o molin
- Quando o ferro arrasta pelo fundo sem segurar o navio, diz-se que o navio está:
- Na máquina de suspender, a coroa de Barbotin é a peça que engrena os elos da amarra, permi
- Na amarração do navio ao cais, as espias que impedem o movimento longitudinal do navio (pa
- Durante a manobra de amarração, o tripulante deve evitar ficar no seio do cabo e na linha 
- O cabo fino com um peso na extremidade, lançado ao cais para depois puxar a espia de amarr
- No pau de carga, o cabo que regula a inclinação da lança chama-se:
- Os estropos usados na movimentação de carga devem respeitar a carga de trabalho segura e o
- O sistema de manutenção planejada dos equipamentos de convés consiste em:
- As portas estanques devem permanecer fechadas com o navio no mar, salvo uso autorizado, e 
- Na inspeção periódica das amarras, verifica-se, entre outros pontos:
- Os turcos de embarcações são estruturas usadas para:
- Os arranjos de reboque de emergência, exigidos em petroleiros de grande porte nas duas ext
- Na inspeção de guindastes navais, NÃO é item normalmente verificado:
- Cabeços, buzinas e tamancas devem ser inspecionados quanto a soldas, desgaste por atrito e
- A corrosão do aço do casco na água do mar é um processo eletroquímico, em que a região anó
- Na proteção catódica por ânodos de sacrifício, os ânodos instalados no casco de aço são no
- O aço inoxidável resiste à corrosão por formar uma camada passiva de óxido de cromo, mas p
- No padrão de preparação de superfície, a sigla Sa 2½ refere-se à limpeza por:
- A carepa de laminação deve ser removida antes da pintura, pois tende a se soltar e provoca
- O componente da tinta responsável por formar a película e dar aderência ao substrato é a:
- Para aplicar tinta sobre o aço, recomenda-se que a temperatura da superfície esteja pelo m
- Em um esquema de pintura para o fundo do casco, a camada destinada a impedir a fixação de 
- Tintas epóxi bicomponentes secam principalmente por cura química, enquanto as alquídicas s
- A osmose é um problema típico de cascos de:
```

## Pedido 39: Equilíbrio geral e economia do bem-estar (`economia.equilibrio-geral-bem-estar`, 90 tópicos de edital)

```
Matéria: economia.equilibrio-geral-bem-estar (Equilíbrio geral e economia do bem-estar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Equilíbrio geral de troca pura, preços e lei de Walras": 17
- "2. Caixa de Edgeworth, curva de contrato e ótimo de Pareto": 37
- "3. Teoremas fundamentais do bem-estar e distribuição": 17
- "4. Equilíbrio geral com produção e condições de eficiência": 14
- "5. Falhas de mercado, critérios sociais e medidas monetárias de bem-estar": 15

Já existem (não repita):
- Numa economia em que pão e manteiga são consumidos conjuntamente, uma mudança no preço do 
- Duas pessoas possuem, juntas, 10 unidades de X e 8 de Y. Qual distribuição é factível numa
- Em determinado vetor de preços, o mercado de X apresenta excesso de demanda de 2 unidades 
- Ao desenhar uma caixa de Edgeworth para duas pessoas e dois bens, uma estudante quer repre
- Uma transferência de recursos tornaria A melhor sem alterar a satisfação de B. A distribui
- Uma alocação está no interior da caixa de Edgeworth, com curvas de indiferença suaves e pr
- Uma economia apresenta equilíbrio competitivo sob as condições usuais do primeiro teorema 
- Uma fábrica vende produto sem considerar o dano que sua fumaça impõe aos vizinhos. Como se
- Um serviço é de consumo não rival e sua exclusão é tecnicamente muito custosa. Como o mode
- Com preços de X e Y iguais a 2 e 3, respectivamente, o excesso de demanda de X é de 6 unid
- Com dotações totais de 10 unidades de X e 10 de Y, dois consumidores possuem a mesma utili
- Um planejador identifica uma alocação Pareto-eficiente e pretende sustentá-la por preços c
- Uma economia produz X e Y segundo a fronteira Y=100-X², com X entre 0 e 10. Qual é a taxa 
- Uma economia utiliza trabalho e capital para produzir dois bens. Na análise de eficiência 
- Um programa concede a uma pessoa valor monetário fixo independentemente de quantas unidade
- Um dano de produção custa 15 à firma por unidade marginal e impõe perda externa adicional 
- Com função social maximin, duas alternativas geram utilidades comparáveis I=(90,10) e II=(
- Numa economia com três bens, X e Y têm excesso de demanda zero e o preço do bem Z é estrit
- Dois consumidores têm preferências lineares e a alocação é eficiente em um canto da caixa 
- Uma empresa concorrencial lança resíduo que prejudica terceiros, sem cobrança ou mercado p
- Uma economia produz dois bens sobre fronteira cuja taxa marginal de transformação de Y por
- Após alta de preço, uma pessoa mantém renda nominal de 100. A despesa mínima aos novos pre
- Um aumento de preço reduz a utilidade de u0 para u1. Aos preços antigos, a despesa necessá
- Uma mudança gera ganho monetário de 150 para um grupo e perda de 90 para outro; nenhum pag
- Uma alocação Pareto-eficiente está associada a tecnologia com importantes não convexidades
- Duas pessoas possuem conjuntamente 12 unidades de X e 6 de Y. Uma distribuição em que A re
- Uma economia registra excessos de demanda de +3 unidades de X e -3 de Y, com preços iguais
- Em uma caixa de Edgeworth, um ponto representa simultaneamente a cesta de A e a de B, cuja
- Uma distribuição muito desigual pode ser eficiente de Pareto; para rejeitá-la como injusta
- Sob hipóteses usuais de concorrência e ausência de falhas relevantes, o primeiro teorema e
- Uma mercadoria pode ser classificada como bem público puro quando seu uso por alguém não r
- Uma economia registra excesso de demanda zero em todos os mercados exceto no de Z, cujo pr
- Dois consumidores com preferências suaves encontram-se em uma solução interior de troca. S
- Um tributo proporcional à quantidade produzida é equivalente a uma transferência de riquez
- Uma economia produz sobre sua fronteira de possibilidades de produção. Isso basta, sem exa
- Em dois setores que utilizam trabalho e capital, a eficiência na produção exige que as TMS
- Um consumidor sofre aumento de preço e o avaliador calcula a variação compensatória pela d
- Uma firma impõe poluição não precificada sobre terceiros, causando custo marginal social a
- Em economia de troca pura, um bem Z possui preço zero e excesso de oferta. Ainda que os de
- Uma distribuição localizada em canto da caixa de Edgeworth pode ser Pareto-eficiente mesmo
- Uma alocação eficiente em economia com não convexidades relevantes pode ser sempre impleme
- Um pesquisador multiplica todos os preços nominais de uma economia de troca pura por cinco
- Em preferências quase lineares com renda suficiente para escolhas interiores e ausência de
- Uma política gera ganho monetário suficiente para que vencedores possam compensar perdedor
- Considerando utilidades comparáveis, uma função maximin prefere a alocação com utilidades 
```

## Pedido 40: Políticas para populações vulneráveis: situação de rua, pessoa com deficiência e juventude (`servico-social.populacoes-vulneraveis`, 90 tópicos de edital)

```
Matéria: servico-social.populacoes-vulneraveis (Políticas para populações vulneráveis: situação de rua, pessoa com deficiência e juventude)
Capítulos e quantas questões quero em cada um (total 100):
- "1. População em situação de rua: direitos, política nacional e jurisprudência": 20
- "2. Saúde da pessoa com deficiência e normas estaduais relacionadas": 20
- "3. Estatuto da Juventude e políticas públicas para jovens": 20
- "4. Trabalho social com famílias, trabalho em rede e interdisciplinaridade": 20
- "5. Assistência social em calamidades, emergências e reconstrução": 20

Já existem (não repita):
- O Decreto federal nº 7.053/2009 caracteriza a população em situação de rua como grupo hete
- O Centro POP é, por definição, unidade de acolhimento institucional em que todos os usuári
- A concepção da LBI inclui a interação entre impedimentos de longo prazo e barreiras que di
- A legislação brasileira define, para o Estatuto da Juventude, a faixa de 15 a 29 anos.
- O PAIF é oferta típica do CRAS e inclui trabalho social com famílias para prevenir ruptura
- Em calamidades, a assistência social deve substituir inteiramente a Defesa Civil nas taref
- Segundo a ADPF 976, estados, municípios e Distrito Federal somente precisam observar as di
- As ações da PNAISPD se limitam à prescrição de órteses e próteses em centros de reabilitaç
- No Estatuto da Juventude, os adolescentes de 15 a 18 anos deixam de ser protegidos pelo EC
- O simples envio de ofício a outra política pública é suficiente para caracterizar trabalho
- Benefícios eventuais, nos termos das orientações oficiais, podem atender situações de cala
- A ID Jovem exige, como condição necessária, comprovação de matrícula em instituição de ens
- A política federal de população em situação de rua prevê acesso a políticas de saúde, habi
- Na PNAISPD, o eixo de dados e sistemas de informação substitui o dever de acessibilidade n
- O Estatuto da Juventude reconhece território, mobilidade, sustentabilidade e meio ambiente
- A interdisciplinaridade está automaticamente garantida sempre que assistente social e psic
- A falta de documentos de pessoa atingida por desastre deve ser avaliada sem constituir, po
- A Lei Estadual nº 14.789/2012 instituiu o Estatuto da Pessoa com Deficiência do Paraná, en
- Uma pessoa adulta em situação de rua procura apoio durante o dia. Qual equipamento é a ref
- Qual conceito descreve melhor a deficiência segundo a LBI?
- Qual norma instituiu o Estatuto da Juventude brasileiro?
- Ao iniciar atendimento no PAIF, qual postura é mais adequada?
- Em desastre com desalojamento de famílias, qual situação é uma hipótese geral de concessão
- Uma prefeitura pretende implementar a política para população em situação de rua e pergunt
- Para ações que integrem saúde e acessibilidade da pessoa com deficiência no SUS, qual polí
- Uma estudante de 17 anos participa de conselho de juventude. Sobre proteção normativa, ass
- Ao acompanhar família com demanda de saúde, escola e moradia, o que melhor demonstra inter
- Em abrigo temporário após deslizamento, qual medida é coerente com proteção social inclusi
- No desenho de uma ação para pessoas em situação de rua, qual entendimento acompanha o Decr
- Qual situação melhor representa o modelo biopsicossocial no atendimento?
- Em relação à ID Jovem, indique a afirmação correta.
- Ao distinguir o acompanhamento familiar de um atendimento isolado, qual elemento é decisiv
- Diante da perda de documentos após enchente, a providência socioassistencial mais adequada
- Uma banca afirma que a norma federal trata o morador de rua como pessoa obrigatoriamente s
- Analise a relação entre PNAISPD e RCPD no SUS. Qual afirmação é a mais adequada?
- Em ação intersetorial para adolescentes de 16 anos, qual interpretação do Estatuto da Juve
- Uma equipe tem assistente social, enfermeiro e psicólogo, mas emite pareceres isolados. Qu
- Ao comparar diplomas estaduais de direitos da pessoa com deficiência, qual correspondência
- Uma família foi deslocada pela cheia de um rio. Ao distinguir benefício eventual do Serviç
- Sobre os direitos de mobilidade no Estatuto da Juventude, qual enunciado descreve a regra 
```

## Pedido 41: Teoria de carteiras, CAPM e derivativos (`financas.carteiras-capm-derivativos`, 90 tópicos de edital)

```
Matéria: financas.carteiras-capm-derivativos (Teoria de carteiras, CAPM e derivativos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Risco, retorno e diversificação": 18
- "2. Markowitz, fronteira eficiente e utilidade": 17
- "3. CAPM, APT, índices e avaliação de desempenho": 22
- "4. Eficiência informacional, vieses e estratégias": 17
- "5. Derivativos, proteção, arbitragem e empréstimo de ativos": 26

Já existem (não repita):
- Uma aplicação adquirida por 100 foi vendida por 108 e pagou 2 em dividendos durante o perí
- Uma carteira reúne ações de diversos emissores, mas segue sensível a uma recessão econômic
- Uma carteira de dois ativos apresenta retornos com correlação zero e desvios-padrão positi
- As carteiras X e Y oferecem ambas retorno esperado de 9%, com desvios-padrão de 10% e 13%,
- Uma pessoa compra uma opção de compra de ação e paga o prêmio contratado. Qual posição con
- Uma posição comprada em contrato futuro passa de preço de ajuste 120 para 124, com multipl
- O retorno de uma ação tende a variar com o retorno de mercado. Qual conceito mede sua expo
- Um fundo procura reproduzir a composição e o retorno de um índice amplo, mantendo baixo de
- Um analista tenta prever retornos anormais usando somente uma série antiga de preços e vol
- Uma put foi comprada com preço de exercício 50 e prêmio 3. No vencimento, a ação vale 40. 
- A e B têm desvios-padrão de 20% e 10%, pesos de 50% cada e correlação nula. Qual é aproxim
- Uma carteira tem 40% alocados em ativo arriscado com retorno esperado de 15% e 60% em ativ
- Considere Rf de 4%, retorno esperado de mercado de 10% e beta de uma ação igual a 1,5. Qua
- Num modelo APT com Rf=3%, exposições fatoriais de 1,2 e 0,5 e prêmios respectivos de 4% e 
- Uma carteira entregou retorno de 12% e teve desvio-padrão de 16%, enquanto a taxa livre de
- Uma carteira teve retorno de 12%, taxa livre de risco de 4%, mercado de 10% e beta de 1. Q
- Uma estratégia apresenta retorno ativo médio de 2% frente ao benchmark e tracking error de
- Um swap simplificado estabelece que uma parte receba 12% e pague 9% sobre nocional de 100.
- Uma pessoa compra uma ação por 100 e simultaneamente adquire put com exercício 100 e prêmi
- Uma empresa exportadora receberá dólares em três meses, mas paga despesas em reais. Qual o
- Duas aplicações têm desvios-padrão de 20% e 10% e correlação -1. Sem venda a descoberto, q
- No CAPM, um ativo tem beta zero, mas desvio-padrão positivo devido a risco específico. Se 
- Após anúncio público inesperado, a cotação se ajusta quase imediatamente e não há padrão d
- Um investidor toma ações emprestadas para realizar venda a descoberto, enquanto o doador c
- Um gestor compara índice ponderado pelo valor de mercado do free float e índice de preços 
- Diante de diversos emissores cujos riscos específicos são pouco correlacionados, a ampliaç
- Uma pessoa possui carteira em que todos os ativos têm retornos positivamente correlacionad
- Um investidor identifica duas carteiras de retorno esperado igual, sendo uma delas menos v
- No CAPM, o desvio-padrão isolado do ativo substitui seu beta quando se calcula a remuneraç
- Uma pessoa compra uma opção de venda e paga o prêmio. O instrumento lhe confere, nas condi
- Em posição comprada num contrato futuro, a elevação do preço diário gera recebimento somen
- Um investidor com utilidade U=E(R)-(A/2)Var(R), A=4, compara carteiras com retorno esperad
- Ao comparar uma carteira eficiente combinada com ativo livre de risco, um analista afirma 
- Um modelo APT de dois fatores associa as exposições de um ativo aos prêmios respectivos e 
- Uma estratégia com retorno ativo médio de 2% e tracking error de 4% deve apresentar índice
- Após divulgação inesperada de informações financeiras, a cotação pode variar imediatamente
- Um índice ponderado por free float atribui necessariamente maior peso à ação de maior preç
- Um tomador obtém ações emprestadas para liquidar uma venda a descoberto. A operação envolv
- Uma carteira amplamente diversificada apresenta beta zero, mas risco específico positivo. 
- Duas aplicações têm desvios de 20% e 10%, correlação -1 e pesos de um terço e dois terços,
- Para um mesmo subjacente e vencimento, contrato a termo convencional e contrato futuro neg
- Uma empresa com receita futura atrelada a mercadoria usa venda de contrato futuro sobre at
- Uma oportunidade obtida pelo uso de informação privada não incorporada ao preço é incompat
- Um investidor lança call coberta por ações que já detém. O prêmio pode compensar parte de 
- Se não houver rendimentos do ativo até o vencimento, o preço a termo livre de arbitragem s
```

## Pedido 42: Observabilidade, SRE e resiliência de aplicações (`informatica.observabilidade-sre-resiliencia`, 90 tópicos de edital)

```
Matéria: informatica.observabilidade-sre-resiliencia (Observabilidade, SRE e resiliência de aplicações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Observabilidade: logs, métricas, traces e instrumentação": 19
- "2. SRE, SLI, SLO, orçamento de erros e resposta a incidentes": 15
- "3. Padrões de resiliência, escalabilidade e controle de concorrência": 26
- "4. Infraestrutura como código, contêineres, configuração e segurança operacional": 14
- "5. Recuperação de desastres, implantações progressivas e arquitetura operacional": 26

Já existem (não repita):
- Ao investigar uma compra que percorreu gateway, pedidos e pagamentos, a equipe quer recons
- Uma aplicação registra em tempo real o número de sessões abertas, que aumenta com novos ac
- Uma equipe acompanha a porcentagem efetiva de pedidos concluídos sem erro, calculada a par
- Um portal define SLO de 99% de pedidos válidos em uma janela de 10.000 pedidos elegíveis. 
- Um serviço externo apresenta falhas sucessivas. A aplicação passa a interromper temporaria
- Uma equipe amplia um serviço web de duas para seis instâncias de aplicação, mantendo o tam
- Em um cluster, um pod está funcional, mas ainda não terminou de carregar dados necessários
- Após análise de impacto, um sistema define tempo-alvo máximo de recuperação de duas horas 
- Após implantar uma nova versão, a equipe observa que 95% das requisições foram atendidas a
- Um aplicativo tem SLO de sucesso de 99,9%, mas sofre taxa corrente de erros de 0,5% na mes
- Uma API apresenta falhas intermitentes de rede. O cliente já usa tentativas com atrasos ex
- Uma aplicação armazena em cache perfis consultados com frequência. Em caso de ausência da 
- Um administrador altera manualmente a configuração de uma instância criada por IaC, sem at
- Uma equipe compartilha módulos Terraform entre ambientes e pretende impedir mudanças inesp
- Uma aplicação usa duas regiões para atender tráfego e permite que usuários sejam encaminha
- Um sistema precisa apresentar nova versão a apenas 5% dos usuários, comparar taxa de erro 
- Um time observa no painel agregado p95 de latência satisfatório, mas usuários de uma regiã
- Um endpoint instrumentado recebe IDs de usuários como rótulo de cada série temporal. Em al
- Uma API possui SLO de 99,8% em 50.000 operações. Após 70 falhas elegíveis, a equipe analis
- O serviço de cobrança aplica retry automático após timeout. O servidor processou o primeir
- Uma equipe quer identificar degradação da versão nova sem comprometer a compatibilidade do
- Uma arquitetura tolera falha de instâncias da aplicação, mas uma dependência compartilhada
- Uma organização exige RTO de 30 minutos e RPO de 5 minutos para banco crítico. Possui apen
- Um contêiner demora dois minutos para inicializar. A sonda liveness começa após dez segund
- Uma API adicionará campo obrigatório a todas as mensagens, mas durante o canary clientes a
- Em uma requisição que atravessa três microsserviços, o compartilhamento do identificador d
- Se uma aplicação registra 200 ms de latência média, pode-se concluir que pelo menos 95% da
- Um SLO de 99,9% de requisições boas permite, pela definição de orçamento em eventos, uma f
- Em um sistema distribuído, o circuit breaker tem como comportamento principal repetir a ch
- Ao adicionar nós idênticos para executar instâncias adicionais do mesmo serviço, uma equip
- No Kubernetes, uma readiness probe com falha tem por função principal reiniciar imediatame
- Se um serviço estabelece RTO de uma hora, sua estratégia de contingência deve ser capaz de
- Em logs centralizados, indexar o identificador único de cada cliente como rótulo de uma mé
- Um alerta de SLO pode usar duas janelas de observação para identificar consumo rápido do o
- Durante falha de uma dependência, enviar a mesma ordem de pagamento repetidamente após tim
- Uma fila permite que produtores continuem a trabalhar sem aguardar a conclusão imediata do
- Como módulos Terraform são arquivos versionados, o estado da infraestrutura pode ser publi
- Um sistema com dois sites ativos dispensa estratégia independente de backup, pois todas as
- Na implantação blue-green, a equipe pode manter dois ambientes e alternar o encaminhamento
- Duas tarefas podem ser concorrentes em uma única CPU sem executar instruções literalmente 
- Um cache com TTL de dez minutos assegura, independentemente de novas escritas na origem, q
- Para manter SLO de latência, a equipe mede fração de operações abaixo de 300 ms e contabil
- Ao usar GSLB baseado em DNS, a alteração de um registro para região secundária torna o dir
- Se a equipe aplica um patch e o pacote em disco muda, ainda é prudente conferir a versão e
- Um teste de recuperação que obtém HTTP 200 da aplicação prova, sem inspecionar armazenamen
```

## Pedido 43: Audiologia (`fonoaudiologia.audiologia`, 89 tópicos de edital)

```
Matéria: fonoaudiologia.audiologia (Audiologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da audição e classificação das perdas auditivas": 20
- "Audiometria tonal, audiometria vocal e mascaramento": 20
- "Imitanciometria, emissões otoacústicas e PEATE": 20
- "Triagem auditiva neonatal, diagnóstico e acompanhamento infantil": 20
- "Aparelhos auditivos, reabilitação e audiologia ocupacional": 20

Já existem (não repita):
- A porção basal da membrana basilar coclear apresenta maior sensibilidade a frequências ele
- As células ciliadas externas participam da amplificação coclear e estão relacionadas à ger
- A perda auditiva mista caracteriza-se por limiares de via óssea normais em todas as frequê
- No audiograma tonal convencional, as frequências são representadas horizontalmente e nívei
- O índice percentual de reconhecimento de fala é sinônimo de limiar de reconhecimento de fa
- Um timpanograma tipo A exclui a possibilidade de perda auditiva sensorioneural.
- A Lei nº 12.303/2010 prevê a realização gratuita de emissões otoacústicas evocadas em cria
- O vibrador de via óssea garante, por sua posição sobre a mastoide direita, que apenas a có
- Na técnica de mascaramento, aumentar indiscriminadamente o ruído contralateral pode gerar 
- Uma curva timpanométrica plana tipo B acompanhada de volume equivalente de conduto muito p
- A ausência de emissões otoacústicas pode ocorrer mesmo com células ciliadas externas funci
- O resultado de PEATE automático informa diretamente limiares audiométricos por frequência 
- Um neonato que falha somente na orelha esquerda na triagem auditiva está dispensado de ret
- Na adaptação de AASI, verificação eletroacústica e validação do benefício comunicativo são
- Quando limiares confiáveis mostram via aérea de 60 dB NA e via óssea de 40 dB NA na mesma 
- Emissões otoacústicas presentes junto de PEATE ausente ou bastante alterado, após controle
- Reflexos acústicos ausentes, isoladamente, definem lesão eferente do nervo facial, indepen
- Se o recém-nascido com fatores importantes de risco apresenta emissões otoacústicas presen
- O uso habitual de protetor auditivo, por si só, dispensa audiometria ocupacional de referê
- Um entalhe em 4 kHz num único exame audiométrico comprova, sem histórico de exposição ou a
- Qual estrutura exerce a maior parcela da transdução aferente sensorial para o nervo coclea
- Em uma orelha, via aérea de 45 dB NA e via óssea de 10 dB NA, ambas confiáveis, sugerem pr
- Qual teste é utilizado rotineiramente para medir variações de admitância do sistema tímpan
- No exame de reconhecimento de fala, que medida apresenta o percentual de palavras corretam
- Na triagem auditiva neonatal, o que representa o resultado “falha” na primeira etapa?
- O que caracteriza melhor o objetivo central das emissões otoacústicas evocadas na avaliaçã
- Em adaptação de aparelho auditivo, qual prática verifica objetivamente se a saída amplific
- Em audiometria por via óssea sem mascaramento, por que a resposta não pode ser atribuída a
- Na análise de uma curva B, qual combinação reforça suspeita de perfuração timpânica ou tub
- Na técnica do platô de mascaramento, qual resultado oferece sustentação para aceitar o lim
- Qual conjunto de achados exige maior atenção para investigar distúrbio do espectro da neur
- Um recém-nascido passou na TAN, porém meses depois os responsáveis referem ausência de res
- Qual estratégia distingue corretamente verificação de validação do AASI?
- Na audiologia ocupacional, o que se deve concluir de um entalhe audiométrico isolado em 4 
- Adulto tem limiares de via aérea e óssea ambos próximos de 55 dB NA sem gap relevante, tim
- Em uma frequência, VA = 65 dB NA e VO = 40 dB NA, com medidas confiáveis e mascaramento ad
- Na timpanometria de um lactente pequeno, a escolha da frequência da sonda é relevante porq
- Um trabalhador faz audiometria sequencial com piora aparente em frequências altas. Qual se
- Qual descrição distingue adequadamente emissões otoacústicas, reflexos acústicos e PEATE?
- Em um bebê com fatores de risco, as EOAT estão presentes, mas o PEATE-A falha. Que interpr
- Uma usuária de AASI atinge o alvo de ganho em microfone-sonda, mas relata compreensão ruim
- Considere uma criança com passagem satisfatória pela TAN, mas antecedente familiar de perd
```

## Pedido 44: Avaliação de impacto regulatório e governança regulatória (`regulacao.avaliacao-impacto-regulatorio-governanca`, 89 tópicos de edital)

```
Matéria: regulacao.avaliacao-impacto-regulatorio-governanca (Avaliação de impacto regulatório e governança regulatória)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Qualidade regulatória, princípios e governança": 26
- "2. Planejamento e construção da intervenção regulatória": 19
- "3. AIR: âmbito, relatório, metodologias e decisão": 23
- "4. Implementação, monitoramento e ARR": 16
- "5. Estoque normativo, transparência e decisões integradas": 16

Já existem (não repita):
- Em determinado setor, há planejamento público dos temas normativos prioritários para os pr
- Uma equipe compara custos e efeitos prováveis de três propostas antes da publicação de qua
- Depois de um ano de vigência de uma obrigação, são examinados resultados efetivamente obse
- Uma revisão reúne normas de igual matéria, elimina duplicidades textuais e preserva o alca
- Ao comparar alternativas, um relatório apresenta expressamente a manutenção da situação at
- Um grupo propõe medir o sucesso de nova regra apenas pelo número de formulários recebidos.
- Uma minuta apenas disciplina rotina interna de organização de arquivos, sem produzir efeit
- Uma proposta federal de interesse geral foi enquadrada como de baixo impacto, e a autorida
- Para cumprir o art. 6º do Decreto 10.411/2020, uma equipe analisa obrigação eletrônica que
- Duas alternativas reduzem o mesmo tipo de falha: a primeira custa R$ 120 mil e evita 60 fa
- Após AIR, a proposta de ato terá impacto significativo sobre o comércio internacional. Sem
- Uma regra nova passou a vigorar depois de dispensa de AIR por urgência. Para a ARR exigida
- Depois de AIR técnica recomendar alternativa A, a autoridade entende que B atende melhor a
- Ao selecionar normas para avaliação posterior, uma equipe estrutura uma agenda de ARR conf
- Uma intervenção diminui o risco de acidentes em uma atividade, mas pode aumentar deslocame
- Uma proposta normativa da administração tributária federal institui novo envio periódico d
- A edição de norma de baixo impacto foi dispensada de AIR. A instituição decide não realiza
- Uma consulta pública terminou em 5 de maio, e a deliberação final ocorreu em 20 de junho. 
- Foi escolhida uma nova obrigação após AIR, e o relatório detalha sua implantação, mas nada
- Uma norma reduziu o tempo de processamento após ser publicada, mas simultaneamente ocorreu
- Uma norma foi publicada com falha em etapa procedimental prevista pelo Decreto 10.411/2020
- Em revisão do estoque, pretende-se reunir três atos sobre a mesma matéria e, aproveitando 
- Uma AIR usa custo-benefício monetizado. A alternativa X tem benefícios de R$ 900 mil e cus
- Uma equipe inicia estudo regulatório descrevendo o problema de informações incorretas, ide
- Após a publicação de uma regra, o estudo dos impactos observados e do alcance de seus obje
- Uma consulta pública recebe manifestações de usuários e agentes regulados. O recebimento d
- A publicação de muitas normas novas é evidência suficiente de qualidade regulatória, ainda
- Um estudo compara a situação antes de uma exigência com indicadores coletados ao longo de 
- Uma consolidação reúne comandos correlatos e, para facilitar fiscalização, acrescenta obri
- Para comparar uma regra mais restritiva com outras opções, o relatório de AIR considera ta
- Uma decisão que dispensa AIR por baixo impacto pode limitar-se a registrar a expressão 'ba
- Na AIR de obrigação digital, identificar custos de adequação para usuários e pequenas empr
- Quando uma proposta de norma tem impacto significativo sobre comércio internacional e não 
- Após comparar alternativas de regulação, a equipe decide adotar metodologia multicritério 
- Para toda norma cuja AIR tenha sido dispensada por qualquer motivo, inclusive baixo impact
- Uma agenda de ARR pode contemplar avaliação temática restrita a parte de uma norma, desde 
- Uma norma federal interna de organização de pessoal, de efeitos restritos à própria admini
- Em uma dispensa de AIR para adequação a padrões internacionais, a consulta pública formal 
- Se a autoridade escolhe solução diferente da recomendada no relatório de AIR, o decreto de
- Ao constatar redução de falhas depois de nova regra e implantação simultânea de sistema pr
- Após decidir editar uma regra, a instituição define plano de implantação e indicadores, ma
- Depois de consulta pública, críticas, sugestões e identificação de participantes devem ser
- Uma administração inclui apenas normas de mais de cinco anos em sua agenda de ARR, afirman
```

## Pedido 45: Lei Henry Borel (Lei 14.344/2022) e intimidação sistemática (bullying) (`legislacao-penal-especial.henry-borel-bullying`, 89 tópicos de edital)

```
Matéria: legislacao-penal-especial.henry-borel-bullying (Lei Henry Borel (Lei 14.344/2022) e intimidação sistemática (bullying))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Violência doméstica contra criança e adolescente: conceito e rede de proteção": 25
- "2. Atendimento policial, afastamento do agressor e medidas protetivas": 25
- "3. Ministério Público, noticiante, crimes e alterações legais": 25
- "4. Intimidação sistemática (bullying)": 25

Já existem (não repita):
- Segundo a Lei 14.344/2022, configura violência doméstica e familiar contra a criança e o a
- A Lei 14.344/2022 recebeu o nome de Lei Henry Borel e instituiu um dia nacional de combate
- A violência doméstica e familiar contra a criança e o adolescente constitui, segundo a lei
- A Lei Henry Borel, ao alterar a Lei 13.431/2017, passou a prever como forma de violência c
- Na formulação de suas políticas, os Estados e o Distrito Federal devem dar prioridade, no 
- NÃO está entre as finalidades da intervenção do Sistema de Garantia dos Direitos da Crianç
- No atendimento à criança em situação de violência doméstica, a autoridade policial deverá,
- Havendo risco atual ou iminente à vida da criança, o agressor será imediatamente afastado 
- Quando o afastamento do agressor do lar for determinado pelo delegado ou pelo policial, o 
- O Conselho Tutelar pode representar às autoridades competentes para requerer o afastamento
- Recebido o expediente com pedido de medida protetiva em favor de criança em situação de vi
- As medidas protetivas de urgência da Lei 14.344/2022 só podem ser concedidas após audiênci
- Segundo a Lei 14.344/2022, podem requerer medidas protetivas de urgência:
- Entre as medidas protetivas de urgência que o juiz pode impor ao agressor, a Lei 14.344/20
- Entre as medidas protetivas de urgência à vítima, o juiz pode determinar:
- Em qualquer fase do inquérito policial ou da instrução criminal, caberá a prisão preventiv
- O responsável legal pela criança vítima, desde que não seja o autor das agressões, deve se
- Nos casos de risco à integridade física da vítima ou à efetividade da medida protetiva de 
- Qualquer pessoa que tenha conhecimento ou presencie violência doméstica e familiar contra 
- Sobre o noticiante ou denunciante de violência contra criança e adolescente, a Lei 14.344/
- Cabe ao Ministério Público, nos casos de violência doméstica contra criança e adolescente,
- O crime de descumprir decisão judicial que defere medida protetiva de urgência prevista na
- No crime de descumprimento de medida protetiva da Lei Henry Borel, preso o agente em flagr
- A conduta de deixar de comunicar à autoridade pública a prática de violência ou de formas 
- Após a Lei Henry Borel, aos crimes cometidos contra a criança e o adolescente, independent
- Nos casos de violência doméstica e familiar contra a criança e o adolescente, o ECA, alter
- A Lei 14.344/2022 incluiu no Código Penal, como homicídio qualificado, o homicídio pratica
- A pena do homicídio contra menor de 14 anos é aumentada de dois terços se o autor é ascend
- Aos procedimentos da Lei 14.344/2022 aplicam-se subsidiariamente, no que couber, o ECA, a 
- Para a Lei 13.185/2015, intimidação sistemática (bullying) é todo ato de violência física 
- Na classificação da Lei 13.185/2015, a intimidação sistemática que consiste em ignorar, is
- Entre os objetivos do Programa de Combate à Intimidação Sistemática está privilegiar, tant
- Pela Lei 13.185/2015, há intimidação sistemática na rede mundial de computadores (cyberbul
- O crime de intimidação sistemática previsto no art. 146-A do Código Penal, em sua forma pr
- Configura a forma qualificada de intimidação sistemática virtual (cyberbullying) a conduta
- O bullying motivado por raça, cor, etnia, orientação sexual ou identidade de gênero pode c
- Segundo a Lei 13.185/2015, são exemplos de atos que caracterizam a intimidação sistemática
- As medidas protetivas de urgência concedidas com base na Lei 14.344/2022 devem ser registr
- A Lei 14.344/2022 aplica-se apenas quando o agressor reside com a criança no momento dos f
- Os entes federativos podem criar, para a criança e o adolescente em situação de violência 
```

## Pedido 46: Constitucionalização e teoria geral do direito privado (`direito-civil.constitucionalizacao-teoria-geral-direito-privado`, 88 tópicos de edital)

```
Matéria: direito-civil.constitucionalizacao-teoria-geral-direito-privado (Constitucionalização e teoria geral do direito privado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direito público, direito privado e estrutura da norma jurídica": 14
- "2. Relações jurídicas, direitos subjetivos e situações existenciais": 21
- "3. Constitucionalização do direito civil e eficácia horizontal": 24
- "4. Código Civil de 2002: princípios, sistema e limites da autonomia": 23
- "5. Liberdade econômica, pessoas jurídicas e soluções de conflitos normativos": 18

Já existem (não repita):
- Após assinar um contrato de locação entre particulares, Lara sustenta que a Constituição n
- Uma empresa pretende afastar, por cláusula contratual, regra de proteção que não admite de
- Um contrato não prevê determinada consequência e não há regra legal específica aplicável a
- Em uma compra e venda, o vendedor não entrega o bem após o vencimento da obrigação. A posi
- Uma pessoa exerce, nas condições admitidas em lei e contrato, o direito de resolver unilat
- Uma pessoa é ameaçada de ter sua imagem usada indevidamente em anúncio comercial. Qual med
- Uma associação privada exclui uma integrante sem lhe permitir manifestação, em situação co
- Uma pessoa é questionada sobre as diretrizes doutrinárias associadas ao Código Civil de 20
- Dois empresários discutem uma cláusula ambígua e pretendem interpretá-la considerando a co
- Ao celebrar contrato empresarial, duas empresas pactuaram alocação específica de risco e, 
- Um empreendedor deseja instalar atividade enquadrada regularmente como de baixo risco em i
- Uma sociedade paga repetidamente despesas particulares de seu administrador e mantém patri
- Uma lei geral nova passa a disciplinar setor que já possui lei especial anterior. Não há i
- Diante de contrato comercial que possui regra geral civil e disciplina protetiva especial 
- Uma empresa solicita licença econômica sujeita ao art. 3º, IX, da Lei nº 13.874/2019 e ent
- Uma locatária demonstra que determinado locador agiu dentro da titularidade formal do seu 
- Dois comerciantes assinam contrato paritário com cláusulas de risco. Um deles sustenta que
- As partes estipularam no contrato critérios próprios de interpretação, lacunas e integraçã
- Uma sociedade integra um conglomerado e contrai dívida, mas não há desvio de finalidade ne
- Uma associação privada delibera sobre exclusão de membro que possui posição relevante em s
- Um contrato foi validamente celebrado antes da entrada em vigor do Código Civil de 2002 e 
- Uma empresa pretende cobrar preço livre de qualquer disciplina setorial por invocar o art.
- Uma licença de atividade econômica com impacto significativo no meio ambiente é solicitada
- Uma lei especial protetiva incide sobre contrato civil, e as partes incluíram cláusula afi
- Um contrato civil paritário foi celebrado por duas sociedades, mas surgem provas de grave 
- Uma empresa afirma que a autonomia privada basta para afastar os direitos fundamentais qua
- Diante de lacuna real da legislação aplicável ao conflito privado, a pessoa responsável po
- Em relação contratual na qual o devedor deixou de cumprir prestação exigível, a violação d
- Uma pessoa sofre ameaça concreta de uso ilícito do próprio nome em campanha comercial; com
- Segundo o Código Civil, o abuso de direito depende de comprovar que o titular desconhecia 
- Uma sociedade adquire personalidade própria, e o art. 49-A do Código Civil estabelece que 
- Um particular afirma que o reconhecimento constitucional da propriedade exclui sua função 
- O titular de direito potestativo exige, como estrutura necessária da relação, que o sujeit
- Duas leis sobre o mesmo setor podem coexistir quando a lei nova geral não é incompatível c
- Ao determinar que negócios jurídicos sejam interpretados conforme boa-fé e usos locais, o 
- Em um contrato empresarial, o art. 421-A permite considerar elementos concretos para afast
- Uma pessoa conclui que a Lei nº 13.874/2019 permite funcionamento econômico em qualquer ho
- Uma autoridade cria exigência técnica sem relação com o fim regulatório e eleva os custos 
- Uma contratação privada alcançada por regime protetivo especial envolve direito social fun
- Uma pessoa solicita ato público de liberação econômica sujeito ao art. 3º, IX, da Lei nº 1
- Uma pessoa jurídica apenas altera legitimamente seu ramo econômico, sem propósito de lesar
- Um negócio foi celebrado sob legislação anterior ao Código Civil de 2002. Para fins do art
- Uma autoridade municipal que não aderiu ao mecanismo de aprovação tácita da Lei nº 13.874/
- No diálogo das fontes, a existência de uma regra especial compatível com preceitos gerais 
- No exercício de direito potestativo, a outra parte tem, por definição, obrigação de realiz
```

## Pedido 47: Ensino de Ciências no ensino fundamental (`educacao.ensino-ciencias-ensino-fundamental`, 87 tópicos de edital)

```
Matéria: educacao.ensino-ciencias-ensino-fundamental (Ensino de Ciências no ensino fundamental)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Alfabetização científica e natureza do conhecimento científico": 17
- "2. Metodologias investigativas, experimentação e temas transversais": 20
- "3. Universo, história da astronomia, Sistema Solar e estrutura da Terra": 19
- "4. Ar, água, saneamento, ecossistemas e relações tróficas": 22
- "5. Diversidade dos seres vivos, saúde do adolescente, genética e evolução": 22

Já existem (não repita):
- Uma turma estuda relações entre materiais, organismos e fenômenos astronômicos. Qual conju
- Uma estudante lê informações divergentes sobre contaminação de um rio e decide comparar me
- Ao examinar a germinação de sementes, um grupo afirma: 'se receberem menos água, a germina
- Dois recipientes com água são usados para investigar o efeito da temperatura sobre o tempo
- Em um cartaz sobre o Sistema Solar, estudantes precisam identificar corretamente o astro c
- Após observar alternância entre claridade e escuridão em um globo iluminado, uma turma pro
- Uma atividade compara os constituintes do ar seco próximo à superfície. Qual gás deve apar
- Uma horta escolar abriga capim, gafanhotos e sapos. Na cadeia capim → gafanhoto → sapo, qu
- Uma amostra de água retirada de um poço é transparente e não apresenta odor. Qual conclusã
- Ao explicar herança biológica, alguém afirma que duas versões de um mesmo gene em determin
- Uma turma analisa tabelas de germinação produzidas com o mesmo tipo de sementes e água, ma
- Uma sequência de aulas compara uma explicação histórica com um modelo científico atual e d
- Uma representação didática mostra a Terra em duas posições opostas na órbita solar e indic
- Uma localidade apresenta surtos de doença de transmissão hídrica em área com esgotamento s
- No estudo de um lago, a turma encontra peixes, algas, microrganismos, temperatura, nutrien
- Ao montar cartões de seres vivos, estudantes agrupam organismos por ancestralidade comum e
- Após uso contínuo de um antimicrobiano, aumenta na população bacteriana a frequência de va
- Uma atividade escolar sobre puberdade e prevenção de infecções sexualmente transmissíveis 
- Uma turma confronta a visão geocêntrica clássica com observações das fases completas de Vê
- Dois grupos mediram germinação com temperaturas distintas, mas também usaram sementes de i
- Em uma teia alimentar, fungos e bactérias degradam restos de produtores e consumidores; ao
- Uma postagem afirma que o aumento de gases de efeito estufa, o buraco na camada de ozônio 
- Em atividade sobre herança mendeliana com dominância completa, dois heterozigotos Aa têm q
- Uma turma pretende estudar água em seu bairro combinando dados de consumo, análise de risc
- Uma aula apresenta asas de insetos e aves como estruturas com função semelhante, mas orige
- Uma turma reúne exemplos de investigação, consumo de recursos e relações de organismos. O 
- Em uma aula sobre densidade, estudantes apenas repetem uma receita e copiam uma conclusão 
- Em um painel de escalas temporais, o Universo é apresentado com aproximadamente 13,8 bilhõ
- Ao montar uma tabela de composição do ar seco, um estudante registra oxigênio como compone
- Uma estudante observa a iluminação alternada de um globo em rotação diante de fonte lumino
- Uma maquete mostra que o verão ocorre porque a Terra se aproxima muito mais do Sol, indepe
- Uma classe avalia um sistema de abastecimento e reconhece que filtração e desinfecção cump
- Em um ecossistema com produtores, consumidores e fungos decompositores, a energia solar in
- Um projeto escolar diferencia as alterações biológicas da puberdade dos aspectos psicológi
- Uma característica autossômica determinada por alelo dominante deve ocorrer em maior númer
- Ao comparar modelos astronômicos elaborados em épocas distintas, uma classe percebe que no
- Uma estudante explica que a sombra projetada pela Terra sobre a Lua é a causa regular de t
- Para estudar mudanças em uma floresta, uma turma utiliza simulação digital de cadeias alim
- Uma turma encontra correlação entre uso de uma praça e incidência de doença durante alguns
- Uma atividade de classificação compara organismos por características anatômicas e sequênc
- Um estudante atribui a intensificação do efeito estufa à diminuição da camada de ozônio, a
- Num cruzamento mendeliano simples Aa × Aa, cada descendente apresenta 25% de probabilidade
- Uma turma organiza todos os microrganismos unicelulares e também os vírus na categoria dos
- Em população de insetos com variação herdável, indivíduos de determinada variante deixam m
- Em investigação sobre evaporação, resultados diferentes da hipótese inicial são descartado
```

## Pedido 48: Atos, termos e prazos processuais (`processo-trabalho.atos-termos-prazos-processuais`, 87 tópicos de edital)

```
Matéria: processo-trabalho.atos-termos-prazos-processuais (Atos, termos e prazos processuais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Atos, termos e prazos processuais": 41
- "2. Distribuição, custas e emolumentos": 38
- "3. Partes, procuradores, assistência e honorários": 11
- "4. Nulidades e como o tema costuma ser cobrado": 10

Já existem (não repita):
- À luz do art. 770 da CLT, assinale a afirmação correta sobre os atos processuais.
- A secretaria precisa efetuar penhora em feriado. Segundo a CLT, qual requisito específico 
- Como são contados os prazos processuais previstos no Título X da CLT?
- Qual é o período anual de suspensão dos prazos processuais trabalhistas previsto expressam
- A CLT disciplina a base geral das custas relativas ao processo de conhecimento na Justiça 
- Qual alternativa distingue corretamente custas de execução e emolumentos?
- A empresa recebe intimação numa terça-feira para cumprir prazo de cinco dias úteis. Não há
- A parte beneficiária da justiça gratuita perde o pedido de perícia. Sobre honorários peric
- Um advogado interpõe recurso de competência do TST em nome de empregado que havia atuado p
- A empresa nomeia como preposto pessoa que conhece os fatos, mas não integra seu quadro de 
- A parte aponta nulidade pela primeira vez em recurso, embora já tivesse falado nos autos d
- Uma empresa é vencida no pedido e há decisão líquida. Considerada a regra geral de custas 
- Numa reclamação individual, alguns pedidos são julgados procedentes e outros improcedentes
- Em ação trabalhista, o advogado obtém condenação em favor do cliente. Qual enunciado descr
- O distribuidor recebe reclamações trabalhistas numa localidade com mais de um juízo compet
- Na hipótese de acordo celebrado antes do julgamento, as partes não ajustam quem pagará as 
- A autora tem salário superior a 40% do teto do RGPS, mas demonstra que não consegue pagar 
- A entidade fiscalizadora do exercício profissional invoca a isenção do art. 790-A da CLT p
- Uma associação sindical ajuíza, em nome próprio, demanda para defender direitos individuai
- A parte beneficiária da justiça gratuita perde integralmente um pedido, mas vence outro. A
- Em causa própria, advogado vence parcialmente a demanda trabalhista. Qual enunciado se aju
- O juiz declara nulo ato processual por irregularidade formal, mas não identifica prejuízo 
- A parte percebe vício processual, aguarda a sentença e só então o suscita na primeira mani
- A nulidade de determinado ato é reconhecida. Atos posteriores sem relação de dependência c
- A parte que deu causa ao vício pede que o juízo declare a nulidade para reabrir prazo que 
- Uma causa de valor indeterminado termina por condenação líquida. Para calcular custas do c
- Um advogado cobra do cliente honorários previstos em contrato e, na mesma ação, recebe hon
- Uma autarquia pública estadual não explora atividade econômica e litiga na Justiça do Trab
- Uma advogada inscrita na OAB é a única patrona da causa e dá à luz durante o processo. Qua
- As partes celebram acordo antes da sentença e convencionam expressamente que uma delas arc
- Certo ou errado: a contagem dos prazos processuais trabalhistas prevista no art. 775 da CL
- Certo ou errado: a Justiça do Trabalho deve manter sigilosos todos os atos processuais, ai
- Certo ou errado: o art. 775-A suspende os prazos entre 20 de dezembro e 20 de janeiro, inc
- Certo ou errado: a parte pode escolher o juízo trabalhista que receberá sua reclamação qua
- Certo ou errado: o art. 791-A prevê honorários de sucumbência também quando o advogado atu
- Certo ou errado: em execução trabalhista, as custas previstas no art. 789-A são de respons
- Certo ou errado: o preposto do empregador deve necessariamente integrar o quadro de empreg
- Certo ou errado: o jus postulandi alcança indistintamente ações rescisórias, mandados de s
- Certo ou errado: a isenção do art. 790-A da CLT alcança as entidades fiscalizadoras do exe
- Certo ou errado: para reconhecer nulidade, a CLT exige manifesto prejuízo às partes litiga
- Certo ou errado: o juiz pode prorrogar prazo pelo tempo estritamente necessário quando ent
- Certo ou errado: se a parte concorreu para produzir o vício, pode invocá-lo depois para ob
- Certo ou errado: honorários contratuais e honorários de sucumbência têm a mesma origem e s
- Certo ou errado: a nulidade de um ato atinge automaticamente todos os atos posteriores, ai
- Certo ou errado: o teto geral das custas do processo de conhecimento é quatro vezes o limi
```

## Pedido 49: Serviços auxiliares da Justiça do Trabalho e peritos (`processo-trabalho.servicos-auxiliares-peritos`, 87 tópicos de edital)

```
Matéria: processo-trabalho.servicos-auxiliares-peritos (Serviços auxiliares da Justiça do Trabalho e peritos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Secretarias trabalhistas: organização, atribuições e chefia": 32
- "2. Distribuidores, cartórios e oficiais de justiça avaliadores": 34
- "3. Peritos judiciais: nomeação, impedimentos, quesitos e laudo": 12
- "4. Gratuidade da justiça: renda, comprovação, pessoas jurídicas e evolução jurisprudencial": 11
- "5. Honorários periciais trabalhistas: sucumbência, adiantamento e ADI 5766": 11

Já existem (não repita):
- Uma unidade de apoio recebe processos, realiza autuação e mantém protocolo de entrada e sa
- O responsável pela chefia de secretaria precisa secretariar uma audiência e lavrar a ata c
- Uma localidade possui duas unidades de primeira instância da Justiça do Trabalho. Qual reg
- O setor de distribuição mantém dois registros alfabéticos dos feitos, um por parte reclama
- Um mandado relacionado à execução de julgado deve ser cumprido mediante diligência externa
- O juiz precisa conhecer a origem e extensão de uma condição técnica controvertida, impossí
- Após a nomeação de perito pelo juízo, uma parte quer apresentar quesitos, indicar assisten
- Uma pessoa ajuizou reclamação em outubro de 2026 e recebe salário de R$ 4.200 por mês, sem
- A parte perdeu o pedido cujo exame exigiu prova pericial e não tem gratuidade reconhecida.
- Antes de realizar perícia no processo trabalhista, o juízo exige que a parte deposite o va
- Uma secretaria regional recebe autos despachados para remessa a relator e mantém arquivo d
- O distribuidor dá baixa em feito por determinação competente e recebe solicitação de certi
- Em localidade com mais de uma Vara, um oficial deixou de cumprir ato deprecado durante nov
- Após a nomeação do perito, este precisa apresentar proposta de remuneração, comprovação de
- O perito judicial comunica que fará vistoria amanhã, e os assistentes técnicos informam qu
- Um perito nomeado aceita a tarefa, mas o juízo recebe notícia fundamentada de que lhe falt
- A parte recebe sentença desfavorável em uma demanda mas possui gratuidade. Em outra ação, 
- Em ação ajuizada antes de 16/09/2026, uma pessoa natural recebia mais de 40% do teto previ
- Uma empresa pede gratuidade, apresentando apenas declaração genérica de dificuldades econô
- Uma parte sem gratuidade deverá pagar honorários periciais depois de ter perdido o pedido 
- Em ação ajuizada antes de 16/09/2026, a parte contrária impugna com provas a declaração de
- O STF decidiu a ADC 80 em 03/09/2026 e modulou seus efeitos. Para processos ajuizados a pa
- Após atraso injustificado, o perito é substituído e deve restituir remuneração recebida po
- O perito entregou laudo, mas as partes identificaram divergências específicas e precisam d
- Em ação com dois pedidos, o reclamante obtém horas extras, mas perde pretensão de adiciona
- Uma secretaria de primeira instância deve registrar decisões, controlar protocolo e fornec
- O art. 714 da CLT atribui ao distribuidor a lavratura das atas de audiência e a subscrição
- O distribuidor deve respeitar ordem de entrada dos feitos e fornecer recibo correspondente
- O oficial de justiça avaliador nomeado para cumprimento de mandado equivale ao perito técn
- Conforme art. 465, § 1º, do CPC, as partes têm quinze dias após a intimação da nomeação do
- O art. 721, § 2º, da CLT impõe, sem exceções e para todo mandado, transferência automática
- Segundo o CPC, art. 466, § 1º, o assistente técnico é profissional da confiança da parte e
- Após a ADC 80, o art. 790, § 4º, impede conceder gratuidade à pessoa natural que receba ac
- A parte vencida na pretensão examinada em perícia, não sendo beneficiária de justiça gratu
- A CLT, art. 790-B, § 3º, reproduz a regra geral de depósito antecipado do CPC e determina 
- O CPC, art. 473, exige que o laudo pericial identifique objeto, apresente análise técnica,
- O assistente técnico indicado pela parte depende do mesmo cadastro oficial do perito do ju
- O art. 477, § 1º, do CPC prevê quinze dias comuns para as partes se manifestarem sobre lau
- Em processos ajuizados após a publicação da ata da ADC 80, a presunção de hipossuficiência
- A decisão da ADC 80 em 3 de setembro de 2026 afastou a referência de 40% do teto previdenc
- O STF decidiu na ADC 80 que qualquer pessoa com salário superior a R$ 5.000 perde definiti
- Conforme o art. 790-B, § 1º, da CLT, os honorários periciais arbitrados devem observar o l
- A ADI 5766 manteve em vigor a regra do art. 790-B, § 4º, permitindo que o beneficiário da 
- Na aplicação trabalhista da prova técnica, o juiz pode ter de observar o regime especial d
- A parte beneficiária que perde apenas o pedido submetido à perícia deve necessariamente pa
```

## Pedido 50: Programação para sistemas embarcados: C, C++ e Rust (`informatica.programacao-sistemas-embarcados`, 86 tópicos de edital)

```
Matéria: informatica.programacao-sistemas-embarcados (Programação para sistemas embarcados: C, C++ e Rust)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos embarcados, C17 e controle de memória": 19
- "2. C++17, STL, RAII e gerenciamento de recursos": 19
- "3. Rust: propriedade, empréstimos e execução sem std": 19
- "4. Qt 6: objetos, eventos, sinais e comunicação": 18
- "5. OpenSSL 3, libcurl e segurança de integração": 25

Já existem (não repita):
- Uma placa lê um registrador de estado que pode mudar sem intervenção do código C. O progra
- Um buffer de oito bytes precisa deslocar seus quatro primeiros bytes duas posições para a 
- O firmware em C++17 guarda quatro amostras inteiras cujo número não muda. Qual característ
- Um `std::vector<int>` contém cinco amostras. A aplicação executa `reserve(100)` para evita
- Um módulo C++17 recebe a responsabilidade única de liberar dinamicamente um objeto quando 
- Em Rust, um programa move uma `String` da variável `origem` para `destino` e tenta imprimi
- Um microcontrolador executará código Rust sem sistema operacional. O projeto usa o atribut
- Um `QObject` receptor pertence à thread de interface e recebe um sinal por `Qt::QueuedConn
- Uma aplicação C enviará telemetria HTTP por meio da interface easy da libcurl. Qual chamad
- Uma função C++17 devolve `std::string_view` construída a partir de uma `std::string` local
- Um driver Rust converte a leitura do sensor em `Result<u16, Falha>`, distinguindo medição 
- Em Qt 6, uma conexão `Qt::AutoConnection` é acionada por sinal emitido na mesma thread de 
- Um aplicativo migra para OpenSSL 3 e precisa selecionar implementações de algoritmos cript
- Um equipamento informa que o compilador está no modo C17. Qual afirmação sobre essa edição
- Duas threads incrementam uma contagem em memória. O código em C17 emprega `volatile int`, 
- Uma aplicação precisa compilar estritamente como C++17 e representar um valor que pode est
- Uma rotina conserva um ponteiro para `vector.data()` e depois adiciona elementos ao vetor 
- Um cliente TLS em firmware aceita uma conexão cifrada mesmo quando não valida a cadeia de 
- Um cliente libcurl recebe uma resposta HTTP 404 integralmente e `curl_easy_perform` devolv
- Um `QObject` filho possui pai e uma equipe tenta migrá-lo isoladamente para outra thread c
- Em C17, uma aplicação manipula um registrador cujo bit zero limpa uma interrupção quando r
- Um projeto Rust `no_std` precisa de `Vec<u8>` para armazenar mensagens de tamanho variável
- Um objeto `std::thread` em C++17 ainda está associado a uma thread executável quando seu d
- Uma aplicação usa OpenSSL 3 para obter SHA-256 e avalia carregar o provider `legacy`. Qual
- Um trabalhador Qt recebe sinais com `Qt::QueuedConnection`, mas sua thread não executa eve
- Ao copiar uma mensagem binária para região parcialmente sobreposta do mesmo buffer, um pro
- Uma rotina C17 que declara `volatile uint32_t contador` pode incrementar essa variável de 
- Um projeto usa `std::unique_ptr` para representar posse exclusiva de objeto alocado; seu d
- Uma aplicação em C++17 utiliza `std::vector::reserve(64)` e passa a ter 64 elementos válid
- Em um módulo Rust, `Option<u16>` pode representar `Some(25)` quando há medição e `None` qu
- Num cliente HTTP com libcurl, o resultado `CURLE_OK` permite concluir que a API remota res
- Para uma aplicação C17, a existência de `uint8_t` permite representar precisamente oito bi
- Em C++17, `std::string_view` mantém a `std::string` observada viva até a destruição da vis
- Um firmware Rust com `#![no_std]` pode utilizar `Option` e `Result` por meio de `core`, ai
- Ao utilizar `Qt::QueuedConnection` para enviar dados de uma thread a um `QObject` receptor
- Ao migrar uma aplicação para OpenSSL 3, os providers passam a oferecer implementações de o
- Quando uma aplicação configura `CURLOPT_CONNECTTIMEOUT_MS`, a libcurl passa a limitar a du
- Um firmware C++17 usa `std::lock_guard<std::mutex>` para proteger trecho crítico. O destru
- Em C17, uma leitura de `volatile uint32_t` em endereço arbitrário fixado pelo programador 
- Em Rust seguro, um empréstimo mutável pode ser criado depois do último uso de empréstimos 
- Uma chamada `unsafe` em Rust torna desnecessária a verificação manual de validade de ponte
- No Qt 6, um `QObject` com pai não pode ser movido isoladamente para outra thread por `move
- O hash SHA-256 de um pacote, transmitido junto ao próprio pacote por canal não autenticado
- Uma integração com a interface multi de libcurl permite conduzir várias transferências com
- Uma biblioteca `no_std` em Rust pode usar `Vec` apenas por acrescentar `extern crate alloc
```

