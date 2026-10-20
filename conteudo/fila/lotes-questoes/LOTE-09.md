# Pedidos de questões extras, lote 9 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 13317 tópicos de edital.

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

## Pedido 1: Obras rodoviárias (`engenharia.obras-rodoviarias`, 288 tópicos de edital)

```
Matéria: engenharia.obras-rodoviarias (Obras rodoviárias)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estudos geotécnicos, sondagens e leitura de relatórios": 21
- "2. Terraplenagem, cortes, aterros e controle tecnológico": 18
- "3. Pavimentação: materiais, camadas, execução e patologias": 17
- "4. Drenagem, obras de arte e sinalização rodoviária": 17
- "5. SICRO, orçamento, fiscalização e impactos ambientais": 27

Já existem (não repita):
- Durante leitura de boletim de sondagem SPT, um técnico confere os três segmentos sucessivo
- Um relatório geotécnico informa limite de liquidez de 46% e limite de plasticidade de 29%.
- Um projetista recebe N-SPT, umidade, densidade seca e ISC/CBR de uma amostra de subleito. 
- No ensaio de laboratório, a densidade seca máxima é 2,00 Mg/m³; em campo, foi medida densi
- O projeto prevê escavação de terreno numa encosta e adição de material compactado em depre
- Após escavar 200 m³ medidos no estado natural, um material passa a ocupar 250 m³ soltos no
- Para estimar volume geométrico de aterro entre duas seções, o técnico dispõe de áreas tran
- Em uma seção de pavimento asfáltico, a camada de solo que constitui a fundação da estrutur
- Uma frente de pavimentação realizará tratamento de base granular preparada e depois aderên
- Uma rodovia terá placas de concreto Portland com juntas de controle e transferência de car
- Para bacia pequena em condições compatíveis com método racional, adote C=0,60, i=80 mm/h e
- Em um talude de corte, a água proveniente da encosta superior desce e provoca sulcos na su
- Um projeto lista bueiro celular para travessia de drenagem, ponte sobre rio e viaduto sobr
- No plano de sinalização de um trecho, uma placa estabelece limite obrigatório de velocidad
- O orçamento de pavimentação prevê 200 toneladas de concreto asfáltico, com custo unitário 
- Na análise de orçamento de obra rodoviária, um técnico identifica interferência de chuvas 
- Uma obra de terraplenagem provoca carreamento de solo descoberto para um córrego após chuv
- No orçamento de corte, o projeto mede 500 m³ in situ e calcula empolamento de 30%, produzi
- Para dois trechos homogêneos, o orçamentista dispõe de fatores de interferência do tráfego
- Em rodovia recém-pavimentada aparecem trilhas de roda e trincas, enquanto o subleito apres
- A especificação de base exige, para o material e ensaio definidos no projeto, ISC mínimo d
- Em terreno mole, um aterro recém-lançado sofre deformações, e o relatório indica dissipaçã
- Uma ponte apresenta erosão localizada junto à fundação de um pilar, após cheias, além de d
- Durante obra com desvio temporário de tráfego, a configuração da faixa disponível muda sem
- O orçamento prevê cerca de fauna ao longo de rodovia que fragmentou dois remanescentes flo
- Em reconhecimento geotécnico de uma rodovia, o nível de água observado em determinado furo
- Se um solo apresenta limite de liquidez de 50% e limite de plasticidade de 30%, seu índice
- O empolamento de material escavado torna idênticos o volume solto no caminhão e o volume c
- Na estrutura usual de pavimento flexível, o subleito é a fundação, enquanto base, sub-base
- Um bueiro tubular sob rodovia pode constituir obra de arte corrente destinada à passagem d
- Uma composição do SICRO publicada para determinada data e região representa custo único e 
- Em aterro rodoviário, massa específica seca de campo de 1,80 Mg/m³ e máxima de referência 
- Uma equipe emprega pintura de ligação como substituto da regularização do subleito, porque
- No método racional expresso por Q=0,278 C i A, utilizando i em mm/h e A em km², o coeficie
- Uma valeta de proteção executada acima de talude de corte pode interceptar a água que desc
- O FIC e o FIT do SICRO são duas denominações do mesmo fator aplicado obrigatoriamente a qu
- No orçamento referencial, a análise da curva ABC de insumos pode direcionar pesquisa local
- Em área com atropelamento de fauna, o uso integrado de passagem adequada e cerca direciona
- Um relatório SPT informa que a cravação do amostrador foi interrompida após penetração par
- Em solo mole saturado, a execução acelerada de aterro pode provocar alteração nas pressões
- O cumprimento da densidade seca especificada para uma camada de pavimento não afasta, por 
- Em projeto de drenagem, aumentar a dimensão de um bueiro sempre elimina erosão na saída, m
- Ao analisar custos de uma obra com interferência de tráfego variável ao longo do trecho, p
- Um orçamento rodoviário que já mede e remunera separadamente administração local e mobiliz
- A recuperação de taludes com controle de enxurrada, cobertura vegetal apropriada e monitor
```

## Pedido 2: Ortopedia e traumatologia (`medicina.ortopedia-traumatologia`, 286 tópicos de edital)

```
Matéria: medicina.ortopedia-traumatologia (Ortopedia e traumatologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos das fraturas, consolidação e emergências ortopédicas": 17
- "Fraturas e luxações de membros em adultos e crianças": 17
- "Coluna, pelve e lesões capsuloligamentares e meniscais": 17
- "Osteoartrite, osteoporose e síndromes degenerativas da coluna": 17
- "Infecções osteoarticulares, tuberculose e tumores ósseos": 16
- "Ortopedia da infância: quadril, pé torto, osteocondroses e marcha": 16

Já existem (não repita):
- Uma fratura com pequena ferida cutânea que se comunica com o foco ósseo deve ser classific
- Na consolidação secundária de uma fratura, a remodelação óssea antecede a formação do calo
- A presença de pulso distal palpável exclui síndrome compartimental aguda e permite aguarda
- A consolidação primária do osso, sob estabilidade absoluta, pode produzir pouco calo visív
- A síndrome da embolia gordurosa pode surgir após fratura de osso longo e manifestar hipoxe
- Uma fratura de Salter–Harris tipo II atravessa a fise e a metáfise, sem necessariamente al
- Em uma fratura de Monteggia, espera-se fratura do rádio associada a lesão da articulação r
- Na suspeita clínica de fratura do escafoide com radiografia inicial negativa, a liberação 
- Fraturas instáveis do anel pélvico podem provocar hemorragia grave, mesmo na ausência de s
- A lesão do ligamento cruzado posterior é classicamente associada a impacto sobre a face an
- A cinta pélvica de emergência deve ser posicionada sobre as cristas ilíacas, pois essa loc
- Na osteoartrite, a radiografia pode mostrar osteófitos, esclerose subcondral e estreitamen
- A osteomalácia e a osteoporose são sinônimos, pois ambas se definem exclusivamente pela di
- Em lombalgia inespecífica, sem sinais de alarme ou déficit neurológico progressivo, a ress
- A artrite séptica deve ser encarada como urgência porque a infecção intra-articular pode l
- A presença de cristais no líquido sinovial exclui com segurança artrite séptica coexistent
- Em lesão óssea radiologicamente agressiva, a biópsia deve ser planejada preferencialmente 
- O pé torto congênito típico combina cavo, adução, varo e equino.
- Na suspeita de epifisiólise proximal do fêmur em adolescente, é prudente manter apoio de p
- Na displasia do desenvolvimento do quadril, a limitação da abdução tende a ganhar importân
- Após queda, paciente tem deformidade no antebraço. Qual etapa é indispensável antes e depo
- Homem com fratura de tíbia apresenta dor progressiva intensa, pior ao estender passivament
- Após osteossíntese compressiva estável, uma fratura une-se sem calo exuberante na radiogra
- Qual conjunto indica de modo mais característico síndrome de embolia gordurosa depois de f
- Ao receber um paciente com fratura aberta de perna após acidente, qual conduta integra o c
- Qual associação descreve corretamente a lesão de Galeazzi?
- Paciente cai com mão espalmada e apresenta dor na tabaqueira anatômica; radiografia inicia
- Uma fratura que atravessa fise e metáfise, poupando a epífise, enquadra-se em que tipo de 
- Criança com fratura supracondiliana do úmero tem mão pálida, dor e déficit motor. Qual pri
- Qual complicação é particularmente relevante na fratura intracapsular do colo femoral?
- No atendimento a fratura pélvica de alta energia com suspeita de sangramento, onde se posi
- Atleta gira o joelho, sente estalo, apresenta hemartrose rápida e Lachman alterado. Qual l
- Vítima de colisão apresenta dor vertebral e parestesia nas pernas. Qual estratégia diagnós
- No joelho, qual mecanismo é tradicionalmente associado a lesão do ligamento cruzado poster
- Paciente relata bloqueio verdadeiro do joelho após torção e dor na interlinha. A melhor in
- Qual achado radiográfico pertence ao padrão típico da osteoartrite?
- Mulher pós-menopausa sofre fratura vertebral após trauma mínimo, mas apresenta T-score aci
- Pessoa tem dor lombar sem sinais de alarme e exame neurológico normal. Qual atitude inicia
- Pessoa com dor nas pernas ao caminhar melhora ao sentar e inclinar-se para a frente. O qua
- Qual quadro aponta mais para mielopatia cervical do que para cervicalgia mecânica simples?
- Criança com febre, dor intensa no quadril e recusa de apoiar o membro precisa, prioritaria
- Qual exame é particularmente útil para demonstrar osteomielite precoce e extensão em parte
- Paciente com dor vertebral crônica, deformidade cifótica progressiva e suspeita de tubercu
- Adolescente apresenta dor óssea persistente, aumento de volume e imagem agressiva no fêmur
- Em osteomielite crônica com fístula e fragmento ósseo desvitalizado, esse fragmento denomi
- Qual combinação compõe a deformidade do pé torto equinovaro congênito?
- Adolescente com sobrepeso apresenta claudicação e dor no joelho, com rotação externa obrig
- Em lactente com displasia do desenvolvimento do quadril, qual pareamento exame/idade é mai
- Criança escolar tem claudicação, limitação da rotação interna do quadril e dor referida ao
- Adolescente atleta apresenta dor reproduzida à palpação da tuberosidade tibial, pior ao sa
```

## Pedido 3: Regressão linear (`estatistica.regressao-linear`, 285 tópicos de edital)

```
Matéria: estatistica.regressao-linear (Regressão linear)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regressão simples, correlação e mínimos quadrados": 22
- "2. ANOVA, variância residual e inferência na regressão simples": 22
- "3. Regressão múltipla, Gauss-Markov e formas funcionais": 17
- "4. Modelo normal, máxima verossimilhança, testes e previsão": 16
- "5. Diagnóstico, influência, validação e interpretação responsável": 23

Já existem (não repita):
- Uma reta de regressão relaciona gasto Y e quantidade X: valor ajustado igual a 8 mais 2 ve
- A reta ajustada de um conjunto de dados é Y previsto igual a 5 mais 3 vezes X. Para X igua
- Uma regressão simples com intercepto apresenta soma cruzada centralizada Sxy de 24 e soma 
- Uma observação apresenta resposta efetiva igual a 20 e resposta prevista pela regressão ig
- Em regressão linear simples MQO com intercepto, a correlação observada entre X e Y é menos
- A média amostral de X é 4, a média de Y é 15 e a inclinação MQO é 2. Qual intercepto faz a
- Uma amostra foi ajustada por MQO simples com intercepto. Qual propriedade dos resíduos dec
- A soma total dos quadrados é 100 e a soma residual dos quadrados é 30 em regressão simples
- Uma regressão linear simples com intercepto foi estimada a partir de 12 observações. Quant
- Uma regressão simples tem 11 observações e soma residual dos quadrados igual a 54. Qual é 
- Em regressão simples com intercepto, o quadrado médio residual é 4 e Sxx vale 16. Qual é o
- Uma inclinação estimada é 3, com erro padrão de 0,75. Qual valor assume a estatística t no
- A inclinação estimada é 2, seu erro padrão é 0,5 e o valor crítico t bilateral informado é
- Na ANOVA de regressão simples com 12 observações, SQR é 80 e SQE é 40. Qual estatística F 
- Para testar inclinação nula em regressão simples com intercepto, o teste t produziu valor 
- O modelo múltiplo ajustado é Y igual a 10 mais 2 vezes X e mais 4 vezes Z. Se Z cresce uma
- Uma regressão com intercepto utiliza 20 observações e 2 preditores. Seu R² é 0,60. Qual o 
- No modelo linear normal com intercepto e 12 observações, a soma de quadrados residuais é 3
- Para os mesmos 12 pares de uma regressão simples com intercepto, a SQE vale 36. Quanto val
- Uma regressão múltipla com intercepto, 18 observações e 2 preditores apresenta SQR=80 e SQ
- Em um modelo aditivo, a previsão de Y é 50 mais 2 vezes X menos 5 vezes D, sendo D igual a
- Uma forma funcional linearizável estima o logaritmo de Y pelo logaritmo de X, com inclinaç
- O modelo completo possui 20 observações e 4 parâmetros de média; SQE do completo é 60 e SQ
- Em regressão simples com intercepto, há 10 observações, média de X igual a 10, Sxx=50 e po
- No centro dos dados de uma regressão simples normal, a previsão ajustada é 10, n=16, s=2 e
- Num estudo de consumo em função de renda, o consumo é variável resposta e a renda é variáv
- Quando um consumidor apresenta valor observado 30 e valor ajustado 27, seu resíduo é menos
- Se o tempo X é medido em horas e o custo Y em reais, a inclinação da regressão de custo so
- Em regressão simples MQO com intercepto, correlação linear igual a menos 0,8 significa que
- Com intercepto e mínimos quadrados, a decomposição da variação da resposta em torno de sua
- Para o estimador MQO ser linear e não viesado sob Gauss-Markov, é necessário que os regres
- Com 18 observações em regressão simples com intercepto, o teste t clássico de inclinação u
- Uma regressão MQO com intercepto apresentou soma dos resíduos igual a zero. Portanto, sua 
- Na ANOVA da regressão simples com 14 observações e soma residual dos quadrados igual a 48,
- Num modelo múltiplo com três coeficientes angulares, a estatística F global é necessariame
- Uma regressão simples estimada com intercepto por MQO passa necessariamente pelo ponto dad
- Em regressão com intercepto e três categorias exaustivas e mutuamente exclusivas, incluir 
- Sob condições clássicas de Gauss-Markov e posto completo, a matriz de variâncias e covariâ
- No modelo linear normal, a estimativa da variância obtida por máxima verossimilhança divid
- Sob erros normais independentes e de variância constante, e com os demais pressupostos do 
- Rejeitar o teste F global que compara um modelo múltiplo com intercepto ao modelo apenas c
- No modelo semilogarítmico em que o logaritmo natural de Y depende linearmente de X com coe
- Mesmo quando duas colunas explicativas de uma matriz de regressão múltipla são idênticas, 
- No mesmo valor de X e sob o modelo normal clássico, o intervalo para prever uma observação
- Qualquer observação com alavancagem elevada exerce necessariamente grande influência no co
```

## Pedido 4: Interpretação de texto e gramática do espanhol (`lingua-espanhola.interpretacao-gramatica`, 284 tópicos de edital)

```
Matéria: lingua-espanhola.interpretacao-gramatica (Interpretação de texto e gramática do espanhol)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estratégias de leitura e falsos amigos": 24
- "2. Artigos, pronomes e colocação pronominal": 18
- "3. Verbos: tempos, modos e perífrases": 17
- "4. Conectores, apócope e heterogenéricos": 17
- "5. Como o tema costuma ser cobrado": 24

Já existem (não repita):
- Leia: «En la oficina, Marta dejó las carpetas encima del escritorio y salió a almorzar.» N
- Leia: «El examen fue largo: duró casi cuatro horas.» Qual é o sentido de «largo» nessa fra
- Assinale a frase que emprega corretamente o artigo neutro «lo».
- Complete: «Ayer María ___ a su hermana en el aeropuerto.» Qual forma verbal completa a fra
- Leia: «Quería viajar; sin embargo, no tenía dinero.» Que relação o conector «sin embargo» 
- Pergunta: «¿Le diste el libro a Juan?» Resposta: «Sí, ___ di ayer.» Qual forma preenche co
- Leia: «Todas las noches, Pedro toma un vaso de leche antes de dormir.» O que significa «va
- Quanto à apócope dos adjetivos, qual frase está correta?
- Texto: «La biblioteca municipal amplió su horario: ahora abre de lunes a sábado, de ocho a
- Na frase «Cuando llegues a casa, llámame», em que tempo e modo está a forma «llegues»?
- Assinale a frase com colocação pronominal correta no espanhol padrão.
- Considerando o gênero dos substantivos em espanhol, qual frase está correta?
- Na norma peninsular, qual frase combina adequadamente o marcador temporal com o pretérito 
- Qual correspondência entre a palavra espanhola e seu sentido em português está correta?
- Qual reescrita mantém o sentido de «Aunque llueva, saldremos»?
- Complete: «A mis padres ___ gusta viajar.» Qual pronome completa a frase de acordo com a n
- Texto: «Durante décadas se creyó que el teletrabajo reduciría la productividad. Los estudi
- Complete: «Si yo ___ más tiempo, estudiaría otro idioma.» Qual forma está de acordo com a 
- Leia: «No vino a la reunión, pues estaba enfermo.» Qual é o valor de «pues» nesse período?
- Qual tradução preserva o sentido de «No te imaginas lo cansada que estoy»?
- Qual é o sentido de «Llevo tres años estudiando español»?
- Quanto ao uso de «muy» e «mucho», qual frase está correta?
- Texto: «Los ciudadanos presentaron sus quejas al ayuntamiento, pero este no les respondió 
- Texto: «El nuevo reglamento entra en vigor pronto; todavía no se sabe la fecha exacta, per
- Texto: «El gobierno anunció nuevas medidas de ahorro energético. Por lo tanto, los edifici
- Na frase «Mañana voy a la oficina temprano», a palavra «oficina» corresponde, em português
- Em espanhol, a palavra «apellido» designa a alcunha carinhosa que se dá a alguém, como “Zé
- Em «Voy al mercado», a forma «al» resulta da contração da preposição «a» com o artigo «el»
- A frase «Vengo de el trabajo muy cansado» está de acordo com a norma, pois em espanhol não
- Em «Mañana iremos al museo», a forma «iremos» está no futuro do indicativo.
- Em «el agua fría», o artigo tem forma masculina, mas o substantivo «agua» continua feminin
- Em «Estudia mucho, pero no aprueba los exámenes», o conector «pero» introduz uma ideia de 
- Em «La comida del restaurante estaba exquisita», o adjetivo indica que a comida tinha sabo
- Texto: «El museo ofrece entrada gratuita a menores de doce años; los demás visitantes paga
- Em «Dámelo», os pronomes aparecem depois do verbo e unidos a ele porque, no imperativo afi
- Em «Ojalá que mañana haga sol», a forma «haga» está no presente do subjuntivo, modo exigid
- A perífrase de «Acabo de llegar a la estación» indica uma ação que ainda vai acontecer.
- Em «Hoy es el primer día del curso», a forma «primer» resulta da apócope de «primero» ante
- Em «Llegó tarde; es decir, cuando la reunión ya había terminado», o conector «es decir» in
- Em «Se lo dije a ella ayer», o pronome «se» é reflexivo e indica que a pessoa disse algo a
- A oração «Si tendría dinero, viajaría por América del Sur» está de acordo com a norma cult
- Texto: «Aunque el informe señala avances en la reducción de la pobreza, advierte que la de
- Em espanhol, «todavía» equivale à conjunção adversativa portuguesa “todavia”, podendo ser 
- Texto: «El proyecto fue aprobado; no obstante, su ejecución depende de recursos.» Substitu
- Texto: «Muchos jóvenes buscan empleo en el sector tecnológico, aunque no todos tienen form
```

## Pedido 5: Gestão Pública: Participação, Resultados e Comunicação (`administracao-publica.gestao-participativa-resultados-comunicacao`, 282 tópicos de edital)

```
Matéria: administracao-publica.gestao-participativa-resultados-comunicacao (Gestão Pública: Participação, Resultados e Comunicação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Participação social, cidadania e mecanismos deliberativos e consultivos": 20
- "2. Controle social, transparência pública, ouvidorias e accountability": 20
- "3. Gestão por resultados, contratualização e construção de indicadores": 20
- "4. Comunicação pública, intersetorialidade e gestão de redes organizacionais": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- No âmbito do Sistema Único de Saúde (SUS), a Lei nº 8.142/1990 disciplina duas instâncias 
- A Lei nº 13.460/2017 dispõe sobre a participação, a proteção e a defesa dos direitos do us
- Na instrução de processos administrativos que envolvam assunto de interesse geral, a Lei n
- A literatura sobre democracia e controle na administração pública classifica a accountabil
- Nos termos da Lei nº 12.527/2011 (Lei de Acesso à Informação — LAI), a divulgação proativa
- Na gestão por resultados aplicada à produção de serviços públicos, os indicadores de desem
- O Marco Regulatório das Organizações da Sociedade Civil (Lei nº 13.019/2014) instituiu ins
- A Constituição Federal de 1988 estabelece, em seu art. 37, § 1º, limites expressos para a 
- Ao analisar como as provas de concursos públicos costumam cobrar a diferença entre polític
- As conferências nacionais de políticas públicas reúnem periodicamente representantes do po
- De acordo com a Lei nº 12.527/2011 (Lei de Acesso à Informação), o servidor responsável pe
- Nos termos da Lei nº 13.460/2017, a Carta de Serviços ao Usuário tem por objetivo informar
- Na construção de indicadores para a gestão por resultados, a dimensão da efetividade refer
- A comunicação pública contemporânea distingue-se da mera propaganda governamental por adot
- Em questões de concursos públicos sobre parcerias reguladas pela Lei nº 13.019/2014 (MROSC
- Um município implantou o Orçamento Participativo (OP), realizando assembleias regionais em
- No estudo da cooperação em organizações públicas e da participação social, as teorias da a
- Segundo a teoria de Andreas Schedler, o conceito de accountability articula três dimensões
- Um cidadão protocolou no Serviço de Informações ao Cidadão (SIC) de uma autarquia federal 
- Considere que uma Secretaria de Educação implementou um programa de reforço escolar no con
- O art. 37, § 8º, da Constituição Federal, regulamentado pela Lei nº 13.934/2019, prevê que
- Para enfrentar o aumento da violência juvenil e da evasão escolar em determinada região me
- Em provas que avaliam a cadeia de valor público e as métricas de aferição de resultados (i
- No processo administrativo federal (Lei nº 9.784/1999), antes da tomada de decisão, a real
- A figura do amicus curiae e as comissões parlamentares de legislação participativa são mec
- Conforme a Lei nº 13.460/2017, para que uma reclamação ou sugestão seja recebida pela ouvi
- Na gestão por resultados, o indicador de eficácia avalia o grau de cumprimento das metas p
- Sob a égide da Lei nº 13.019/2014 (MROSC), o termo de fomento é o instrumento jurídico uti
- Em situações de desastres ambientais e emergências de saúde pública, a comunicação pública
- Uma armadilha frequente em provas de concursos é afirmar que o cumprimento integral da tra
- Analise a dinâmica institucional de um Conselho Municipal de Saúde regido pela Lei nº 8.14
- No âmbito das relações entre Estado e sociedade na formulação e implementação de políticas
- Um órgão público federal passou a publicar mensalmente em seu sítio eletrônico arquivos em
- Ao desenhar o sistema de monitoramento e avaliação de um programa nacional de habitação po
- A contratualização de resultados no setor público brasileiro abrange tanto ajustes interno
- Em um município atingido por grave crise socioambiental decorrente de contaminação hídrica
- Considerando as formulações avançadas cobradas em provas sobre governança colaborativa, co
- Nas questões de nível difícil sobre contratualização e gestão por resultados na Administra
- Embora o resultado de uma audiência ou consulta pública não vincule obrigatoriamente o con
- De acordo com a Lei nº 13.460/2017, compete aos conselhos de usuários, no exercício de seu
- Na Lei nº 12.527/2011 (LAI), os municípios com população de até dez mil habitantes ficam d
- Na modelagem de indicadores para avaliação de programas governamentais, os indicadores de 
- Nos termos da Lei nº 13.019/2014 (MROSC), o monitoramento e a avaliação das parcerias cele
- Na gestão de redes organizacionais intersetoriais, a interdependência entre os atores públ
- Em provas que abordam casos longos sobre comunicação pública e promoção da equidade social
```

## Pedido 6: Registro de Imóveis: Princípios, Matrícula, Prenotação e Procedimento de Dúvida (`direito-notarial-registral.registro-imoveis`, 282 tópicos de edital)

```
Matéria: direito-notarial-registral.registro-imoveis (Registro de Imóveis: Princípios, Matrícula, Prenotação e Procedimento de Dúvida)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Noções gerais e princípios": 20
- "2. Registro, averbação, livros e matrícula": 20
- "3. Protocolo, prenotação, prioridade e prazos": 20
- "4. Exigências e procedimento de dúvida": 20
- "5. Retificação, nulidade e cancelamento": 20

Já existem (não repita):
- Segundo o Código Civil, nos negócios entre vivos a propriedade imóvel transfere-se:
- O registro é eficaz desde o momento em que se apresentar o título ao oficial do registro e
- O princípio registral segundo o qual o registro só pode ser feito em nome de quem figure c
- A presunção de fé pública do registro imobiliário é absoluta, de modo que o registro não p
- O princípio da especialidade objetiva exige:
- Pelo princípio da legalidade, o oficial de registro de imóveis faz a qualificação registra
- Segundo o art. 167 da Lei 6.015/1973, é ato de AVERBAÇÃO no Registro de Imóveis:
- Na designação genérica de registro, consideram-se englobadas a inscrição e a transcrição a
- As convenções de condomínio edilício e os pactos antenupciais são registrados no Registro 
- O Livro nº 5 – Indicador Pessoal é o repositório dos nomes de todas as pessoas que figurar
- Sobre a matrícula imobiliária, é correto afirmar:
- Imóvel situado em duas circunscrições terá matrícula aberta em ambas as serventias, e os a
- É requisito da matrícula de imóvel rural a identificação com:
- Quando dois imóveis contíguos do mesmo proprietário constarem de matrículas autônomas, ele
- O número de ordem do título no Protocolo determina:
- Não serão registrados, no mesmo dia, títulos pelos quais se constituam direitos reais cont
- Apresentado título de segunda hipoteca com referência expressa à existência de outra anter
- Após a Lei 14.382/2022, protocolizado o título, o registro ou a emissão de nota devolutiva
- Não havendo exigências nem falta de pagamento, a escritura de compra e venda sem cláusulas
- Cessam automaticamente os efeitos da prenotação se, decorrido certo prazo do lançamento no
- Em caso de escrituras públicas da mesma data, apresentadas no mesmo dia e com indicação ex
- Havendo exigência a ser satisfeita, o oficial de registro de imóveis deve indicá-la:
- Não se conformando com a exigência ou não sendo possível cumpri-la, o interessado pode req
- No procedimento de dúvida, o oficial dará ciência dos termos da dúvida ao apresentante, no
- Se o interessado não impugnar a dúvida no prazo legal, ela será arquivada sem julgamento, 
- Impugnada a dúvida, será ouvido o Ministério Público e, se não forem requeridas diligência
- Da sentença proferida no procedimento de dúvida cabe apelação, com efeitos devolutivo e su
- Transitada em julgado a decisão que julgou a dúvida PROCEDENTE:
- A decisão da dúvida tem natureza administrativa e não impede o uso do processo contencioso
- No processo de dúvida, as custas:
- No procedimento de usucapião extrajudicial perante o registro de imóveis, é lícito ao inte
- Pode ser retificado de ofício ou a requerimento do interessado, pelo oficial de registro d
- Na retificação administrativa de medida perimetral, o confrontante que não assinou a plant
- Para fins de retificação de registro, NÃO se incluem como confrontantes:
- As nulidades de pleno direito do registro, uma vez provadas, invalidam-no independentement
- Bloqueada a matrícula por determinação judicial, o oficial:
- O registro, enquanto não cancelado, produz todos os efeitos legais, ainda que por outra ma
- O pedido de usucapião extrajudicial perante o registro de imóveis deve ser feito pelo inte
- Os atos enumerados no art. 167 da Lei 6.015/1973 são obrigatórios e devem ser efetuados na
- Se o imóvel não estiver matriculado ou registrado em nome do outorgante, o oficial exigirá
```

## Pedido 7: Sistemas de potência, subestações, proteção e eficiência energética (`engenharia-eletrica.sistemas-potencia-subestacoes-protecao`, 282 tópicos de edital)

```
Matéria: engenharia-eletrica.sistemas-potencia-subestacoes-protecao (Sistemas de potência, subestações, proteção e eficiência energética)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistemas de potência, grandezas trifásicas e representação em pu": 23
- "2. Faltas, proteção e coordenação de sistemas elétricos": 23
- "3. Subestações, equipamentos, aterramento e manutenção": 22
- "4. Sistemas fotovoltaicos, UPS e bancos de baterias": 17
- "5. Eficiência energética, iluminação e decisões integradas": 15

Já existem (não repita):
- Uma instalação trifásica equilibrada opera a 380 V de tensão entre linhas, corrente de lin
- Uma carga equilibrada absorve 100 kVA com fator de potência de 0,80. O medidor de potência
- Um TC ideal de relação nominal 400/5 A apresenta corrente secundária eficaz de 2 A. Qual c
- Uma proteção deve atuar sem atraso intencional de temporização quando uma corrente exceder
- Durante a análise de uma subestação, é necessário selecionar o equipamento destinado a int
- Quatro módulos fotovoltaicos idênticos, com tensão operacional de 40 V e corrente de 10 A 
- Em uma instalação crítica, a carga precisa ser alimentada pelo inversor no regime normal, 
- Uma máquina de potência elétrica ativa constante de 2 kW opera por três horas. Qual é o co
- Em uma rede trifásica, adotam-se base de 10 MVA e 13,8 kV entre linhas. Um elemento tem im
- Uma alimentação trifásica em estrela equilibrada possui tensão entre linhas igual a 380 V.
- Um transformador apresenta corrente de entrada e de saída medidas por TCs corretamente com
- No cálculo de uma falta franca entre as fases B e C, sem conexão com a terra, em sistema e
- Um banco de baterias de 48 V e 100 Ah fornece energia por UPS com eficiência global estima
- Uma carga senoidal consome 100 kW com fator de potência indutivo de 0,8. Pretende-se corri
- Uma fonte trifásica de tensão de linha pré-falta de 13,8 kV alimenta uma falta trifásica f
- O projeto de uma malha de terra de subestação foi aprovado apenas porque sua resistência g
- Uma string possui dez módulos em série, cada um com Voc de 50 V em condição de referência.
- Uma reatância é 0,10 pu nas bases de 10 MVA e 13,8 kV. Ela será expressa nas novas bases d
- Em um alimentador radial, a corrente máxima de carga contínua é 120 A e a menor corrente d
- Uma UPS possui potência aparente nominal de 5 kVA e potência ativa máxima especificada de 
- Uma string de dez módulos apresenta Voc corrigida a frio de 55 V por módulo e Vmp corrigid
- Uma carga de 100 kVA opera com fator de potência 0,80 indutivo. Após compensação ideal par
- Um transformador trifásico de 630 kVA tem primário de 13,8 kV entre linhas. Na condição de
- Uma instalação radial passa a receber uma segunda fonte de geração no extremo do alimentad
- Um ambiente de 160 m² utiliza luminárias com fluxo bruto agregado de 24.000 lm. Os fatores
- Em uma rede trifásica equilibrada, a potência aparente calculada com tensão de linha e cor
- Durante a troca de um medidor, manter o primário de um TC conduzindo corrente e abrir seu 
- Quatro módulos fotovoltaicos idênticos conectados em paralelo, sob condições ideais e igua
- Uma central fotovoltaica conectada à rede, com potência instalada de 100 kW, enquadra-se c
- No projeto de uma subestação, a corrente permanente nominal de um disjuntor e sua capacida
- A função diferencial 87 de proteção de transformador é equivalente à função 51, pois ambas
- Na avaliação de duas luminárias com fluxo luminoso equivalente, a que exige menor potência
- Em rede radial com relés 51 a montante e a jusante, programar ambos para o mesmo instante 
- Se as tensões base nos dois lados de um transformador ideal forem escolhidas conforme sua 
- Uma UPS do tipo standby fornece energia à carga continuamente pela cadeia retificador-inve
- Ao dimensionar uma string fotovoltaica, devem ser verificados tanto o valor de Voc corrigi
- Para uma carga senoidal de 100 kW com fator de potência indutivo inicial de 0,8, a instala
- A inserção de resistência no aterramento do neutro pode limitar a corrente de falta à terr
- Em um sistema equilibrado antes da falta, um curto-circuito trifásico franco no mesmo pont
- Uma carga trifásica permanece com a mesma potência ativa e a mesma tensão de linha. A comp
- No sistema por unidade com bases de 13,8 kV e 10 MVA, uma impedância física de 1,9044 ohm 
- Um banco idealizado de 48 V e 100 Ah possui energia nominal aproximada de 4.800 Wh, mas su
- Um inversor fotovoltaico convencional conectado à rede deve continuar energizando a instal
- A presença de geração distribuída em um alimentador pode modificar as contribuições de cur
- A nova redação da NR-10, publicada em 2026 e com início de vigência previsto para 1º de ju
```

## Pedido 8: Crimes eleitorais e processo penal eleitoral (`direito-eleitoral.crimes-eleitorais-processo-penal-eleitoral`, 282 tópicos de edital)

```
Matéria: direito-eleitoral.crimes-eleitorais-processo-penal-eleitoral (Crimes eleitorais e processo penal eleitoral)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos dos crimes eleitorais e elementos de tipicidade": 19
- "2. Proteção do sufrágio, liberdade do voto e autenticidade": 25
- "3. Propaganda, honra, falsidades e financiamento": 19
- "4. Investigação, competência e ação penal eleitoral": 17
- "5. Procedimento penal eleitoral, recursos e aplicação subsidiária": 20

Já existem (não repita):
- Durante visita a uma residência, uma pessoa promete pagar determinada quantia a um eleitor
- Após receber notícia de crime eleitoral, uma pessoa pergunta se apenas o eleitor prejudica
- Para obter inscrição eleitoral, alguém informa deliberadamente elementos fraudulentos em s
- Uma pessoa já votou e tenta registrar outro voto passando-se por eleitor diferente. A tent
- Uma infração eleitoral é descoberta em município onde não existe órgão da Polícia Federal.
- Um agente público utiliza sua autoridade administrativa para pressionar subordinados a vot
- No dia do pleito, um grupo distribui folhetos e solicita votos na entrada de local de vota
- Para apresentar documento autêntico de campanha, o responsável insere deliberadamente info
- Quem exerce de fato a administração financeira de campanha desvia para proveito pessoal re
- A Justiça Eleitoral determina a uma pessoa uma providência específica e individualizada, e
- Uma pessoa utiliza grave ameaça para exigir que um eleitor se abstenha de votar, mas o ele
- Uma peça de propaganda de candidatura copia frases e símbolos característicos de uma empre
- Durante campanha, alguém divulga como verdadeiro fato que sabe ser falso a respeito de can
- Uma candidata é perseguida e ameaçada em razão de sua condição de mulher, com a finalidade
- Um invasor obtém acesso a sistema informatizado do serviço eleitoral com a finalidade de a
- Uma investigação aponta delito eleitoral e crime comum praticado em conexão demonstrada co
- Verificada infração penal eleitoral e reunidos os elementos para acusação, o Ministério Pú
- Depois da acusação penal eleitoral, a defesa sustenta que as regras processuais atualizada
- Uma sentença penal eleitoral absolve o acusado e a acusação pretende recorrer da decisão f
- No dia da votação, uma pessoa comparece com pequeno adesivo de identificação de preferênci
- Uma organização contrata grupo de pessoas especificamente para publicar comentários ofensi
- Com finalidade eleitoral, uma pessoa atribui a alguém crime que sabe não ter sido cometido
- Uma pessoa providencia transporte irregular para eleitores, com propósito comprovado de al
- Em crime eleitoral cuja sanção comina apenas 'reclusão até quatro anos', sem indicar pena 
- Uma condenação proferida pela Justiça Eleitoral impõe pena de prisão a ser cumprida em est
- Em investigação de compra de votos, comprovou-se promessa de vantagem a eleitor identifica
- Uma pessoa ofendida por delito do Código Eleitoral deseja apresentar diretamente queixa-cr
- Em seção eleitoral, uma pessoa tenta votar usando a identificação de outra e é impedida an
- Uma eleitora utiliza adesivo discreto com preferência política no dia da votação e não pro
- Um documento de campanha é formalmente verdadeiro, mas contém declaração deliberadamente f
- Durante a apuração de infração eleitoral em local sem unidade da Polícia Federal, a políci
- Uma pessoa oferece dinheiro para que determinado eleitor se abstenha de votar. Se comprova
- Alguém ameaça gravemente um eleitor para que vote em determinada candidatura, semanas ante
- Em ação penal eleitoral, a defesa invoca os arts. 396-A e 397 do CPP para apresentar respo
- Uma propaganda eleitoral usa símbolos semelhantes aos de empresa pública. Para a tipificaç
- Uma candidata é perseguida com manifestações discriminatórias por sua condição de mulher p
- Um programa público de transporte gratuito de eleitores, prestado sem aliciamento e segund
- Após sentença final de absolvição em processo penal eleitoral, o recurso referido no art. 
- Investigação sobre delito de corrupção comum ocorrido durante campanha, sem qualquer ligaç
- Quando o tipo penal eleitoral estabelece reclusão até determinado máximo, mas não indica o
- Uma pessoa atribui indevidamente a terceiro a autoria de anúncio eleitoral na internet, se
- Alguém, sabendo que outra pessoa é inocente e agindo com finalidade eleitoral, divulga imp
- Depois de condenação eleitoral ainda sujeita a recurso, o art. 363 do Código Eleitoral aut
- Um eleitor é detido por autoridade cinco dias antes da eleição sem flagrante nem outra exc
- Durante a campanha, uma pessoa emite opinião crítica sobre gestão pública, sem divulgar fa
```

## Pedido 9: Petição inicial e improcedência liminar (`processo-civil.peticao-inicial`, 279 tópicos de edital)

```
Matéria: processo-civil.peticao-inicial (Petição inicial e improcedência liminar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Petição inicial, causa de pedir e requisitos dos arts. 319 e 320": 18
- "2. Pedido: certeza, determinação e espécies": 16
- "3. Cumulação, alteração do pedido e valor da causa": 19
- "4. Emenda, indeferimento e decisões sobre a inicial": 17
- "5. Improcedência liminar do pedido e distinções essenciais": 30

Já existem (não repita):
- Ao ajuizar ação de cobrança, a autora indica os fatos, o valor da causa, as provas pretend
- Um consumidor pretende demandar empresa cuja sede e nome são conhecidos, mas não conseguiu
- Em demanda fundada em relação contratual, o juiz verifica que um documento essencial à pró
- A autora descreve uma compra, seu pagamento e a não entrega do produto, relacionando esses
- Ao examinar a petição de uma ação indenizatória, o juiz detecta omissão sanável e indica c
- Uma inicial pede restituição de quantia identificada, sem enunciar separadamente correção 
- Após acidente, a vítima consegue apontar a conduta e o dano, mas ainda não é possível dete
- Contrato prevê que a prestação pode ser cumprida mediante entrega de um dos dois equipamen
- A autora solicita, primeiramente, resolução de contrato e, caso esse pedido seja rejeitado
- Uma pessoa pretende, contra a mesma empresa, restituição de valor pago e reparação de dano
- Na inicial são apresentados dois pedidos em alternativa: um com valor de R$ 18.000 e outro
- Uma ação apresenta pedido principal avaliado em R$ 24.000 e, para eventual rejeição dele, 
- Uma demanda de alimentos pede pagamento mensal de R$ 1.500, sem cobrança de parcelas venci
- O autor formula cumulativamente cobrança de R$ 6.000 e indenização de R$ 4.000, ambas requ
- Antes de o réu ser citado, a autora encontra documentação que justifica acrescentar novo p
- Depois da citação, mas antes do saneamento, o autor pretende ampliar a causa de pedir. O r
- O autor relata inadimplemento contratual e pede condenação, mas o texto da inicial não con
- Em ação destinada a revisar obrigações de empréstimo, a inicial identifica genericamente e
- Uma petição inicial foi indeferida por inépcia, com extinção integral do processo. O autor
- Uma controvérsia exclusivamente jurídica dispensa produção adicional de provas. O pedido c
- Uma ação fundada apenas na interpretação de direito estritamente local contraria súmula es
- Uma inicial contém datas e documentos suficientes para demonstrar, desde logo, prescrição 
- Após sentença de improcedência liminar baseada no art. 332, o autor apela. O juiz mantém s
- Uma pessoa cobra quatro parcelas vencidas de R$ 900 e prestações mensais futuras de R$ 900
- O autor acumula em uma ação pedidos submetidos, originalmente, a tipos diferentes de proce
- Em demanda declaratória cujo proveito econômico não pode ser imediatamente aferido, o auto
- Numa ação em que a autocomposição é admissível, o simples registro de desinteresse do auto
- Se não conhece informações de qualificação do réu exigidas pelo art. 319, II, o autor pode
- Um autor dispõe de contrato que comprova dívida de valor determinado, mas prefere deixar a
- Em ação de restituição monetária, a ausência de pedido expresso de juros legais e correção
- Numa ação de cumprimento de obrigação em prestações sucessivas, as parcelas que vencerem d
- Depois da citação do réu e antes do saneamento, a alteração do pedido pelo autor é admitid
- Em ação com petição inicial defeituosa, o juiz identifica de forma precisa as irregularida
- Se uma inicial deixa de juntar documento essencial, mas a falta é sanável, o magistrado de
- Uma sentença que indefere integralmente a petição inicial por inépcia extingue o processo 
- Um pedido de indenização por dano moral, com valor pretendido expressamente indicado na in
- Se o valor atribuído à causa for incorreto, o réu poderá impugná-lo em preliminar de conte
- Ao constatar que o valor atribuído a uma cobrança não corresponde ao conteúdo patrimonial 
- Uma controvérsia exclusivamente de direito local, que dispensa instrução, apresenta pedido
- Na ausência de necessidade de instrução, a mera existência de uma decisão judicial isolada
- A prescrição da pretensão, reconhecida liminarmente porque é verificável desde a petição e
- Proferida sentença de improcedência liminar do pedido e não interposta apelação, a providê
- No indeferimento integral da petição inicial, se o autor apela e o juiz não se retrata, o 
- Em cumulação subsidiária regular, a exigência de compatibilidade entre os pedidos do art. 
- Após o autor apelar de improcedência liminar, o juiz pode retratar-se em cinco dias; se o 
```

## Pedido 10: Amostragem e pesquisas: planos amostrais e tamanho da amostra (`estatistica.amostragem-pesquisas`, 279 tópicos de edital)

```
Matéria: estatistica.amostragem-pesquisas (Amostragem e pesquisas: planos amostrais e tamanho da amostra)
Capítulos e quantas questões quero em cada um (total 100):
- "Planejamento da pesquisa e tipos de seleção": 17
- "Planos amostrais probabilísticos": 28
- "Tamanho da amostra": 23
- "Pesos, estimadores e erros em pesquisas": 20
- "Como o tema costuma ser cobrado": 12

Já existem (não repita):
- Uma pesquisa quer estimar a renda média dos domicílios de uma cidade e sorteia a amostra a
- De um cadastro com 800 servidores, sorteiam-se 40 por amostragem aleatória simples sem rep
- Em amostragem sistemática, uma lista de 1.200 fichas será usada para obter 60 fichas, e o 
- Uma população tem três estratos com 3.000, 5.000 e 2.000 unidades. Com alocação proporcion
- Qual dos procedimentos abaixo é um exemplo de amostragem não probabilística?
- Mantidos o nível de confiança e a variabilidade, deseja-se reduzir a margem de erro de uma
- Em uma AAS de 200 domicílios retirada de um total de 5.000, 30 domicílios da amostra têm a
- Para uma pesquisa domiciliar, sorteiam-se 15 quarteirões de um bairro e entrevistam-se tod
- Deseja-se estimar uma proporção com 95% de confiança (z = 1,96) e margem de erro de 3 pont
- Para estimar o tempo médio de atendimento, admite-se desvio padrão de 10 minutos, margem d
- Uma empresa com 200 funcionários fará pesquisa de clima com margem de erro de 4%, usando a
- Três estratos têm tamanhos 1.000, 2.000 e 1.000 e desvios padrão 10, 5 e 20, respectivamen
- Em amostragem estratificada, os estratos representam 30%, 50% e 20% da população, e as méd
- Uma pesquisa por conglomerados entrevistou 1.200 pessoas e tem efeito do plano (deff) igua
- Foram sorteados 1.500 domicílios. Na visita, 100 estavam vagos ou eram estabelecimentos (i
- Em um plano em dois estágios, sorteiam-se 20 das 400 escolas de uma rede e, em cada escola
- Quantas amostras distintas de 2 unidades, sem considerar a ordem, podem ser formadas por a
- Numa pesquisa, 60% dos elegíveis responderam. A média de uma variável entre os respondente
- Uma amostra estratificada tem dois estratos: W1 = 0,6, S1 = 10, n1 = 60 e W2 = 0,4, S2 = 2
- Uma rede tem 100 escolas e 20.000 alunos. Sorteiam-se 20 escolas com probabilidade proporc
- O cálculo básico indicou 400 entrevistas completas para uma AAS. O plano real, por conglom
- Numa região com 400 fazendas e área total conhecida de 22.000 hectares, uma amostra deu ár
- Sobre a eficiência dos planos amostrais, assinale a afirmação correta.
- Considerando uma pesquisa para estimar proporção em população grande, qual das mudanças ab
- Uma lista de 1.000 apartamentos está ordenada por andar, com 10 unidades por andar, e o ap
- Em um censo, todas as unidades da população são investigadas, de modo que não há erro amos
- Na amostragem por conveniência, como o pesquisador aborda quem estiver disponível, a proba
- Na amostragem estratificada, a população é dividida em grupos que não se sobrepõem e cobre
- Mantidas a margem de erro e a variabilidade, elevar o nível de confiança de 95% para 99% p
- Em uma AAS de 50 unidades retirada de uma população de 2.000, cada unidade observada tem p
- Aumentar bastante o tamanho da amostra elimina o viés causado por uma pergunta indutora no
- Na amostragem por bola de neve, os primeiros participantes indicam outros, o que ajuda a a
- Sem informação prévia sobre a proporção, usar p = 0,5 no cálculo do tamanho da amostra lev
- Na amostragem sistemática com intervalo 25 em uma lista de 1.000 unidades, todos os conjun
- Numa população de 100 mil pessoas, com margem de 5 pontos percentuais, 95% de confiança e 
- O efeito do plano de uma amostra por conglomerados costuma ser menor que 1, o que indica q
- Na alocação proporcional, a fração amostral é a mesma em todos os estratos, e por isso tod
- Erro de mensuração só ocorre em pesquisas por amostragem; num censo, como todas as unidade
- Para estimar uma média com desvio padrão 15, margem de erro 3 e coeficiente z = 2, em popu
- Se cada conglomerado tem 11 entrevistados e a correlação intraclasse é 0,05, o efeito do p
- Na alocação ótima de Neyman, estratos com maior desvio padrão recebem proporcionalmente me
- Em AAS sem reposição de 100 unidades de uma população de 1.000, com variância populacional
- Na amostragem com reposição de 2 unidades de uma população de 5, considerando a ordem do s
- Numa classe de ponderação com peso base 50 e taxa de resposta de 80%, o ajuste por não res
- Uma enquete na internet aberta a voluntários que reúne 50 mil respostas tem margem de erro
```

## Pedido 11: Ética profissional: conselhos de classe, responsabilidade técnica e sigilo (`etica.etica-profissional-conselhos`, 277 tópicos de edital)

```
Matéria: etica.etica-profissional-conselhos (Ética profissional: conselhos de classe, responsabilidade técnica e sigilo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ética, moral, deontologia e fundamentos do exercício profissional": 17
- "2. Conselhos profissionais: natureza jurídica, registro, fiscalização e anuidades": 17
- "3. Princípios dos códigos: competência, dignidade, publicidade e conflitos de interesses": 17
- "4. Sigilo profissional, proteção de dados e responsabilidade técnica": 17
- "5. Infrações, processo ético-disciplinar e esferas de responsabilidade": 16
- "6. Ética aplicada: tecnologia da informação, engenharia, comunicação e contabilidade": 16

Já existem (não repita):
- Ética, moral e deontologia possuem exatamente o mesmo objeto: todas designam exclusivament
- A correção formal de um contrato não afasta o exame ético de condutas que exponham terceir
- O fato de determinada conduta ser socialmente tolerada ou economicamente vantajosa basta p
- A liberdade constitucional de exercício profissional convive com qualificações exigidas po
- Em uma aula preparatória, três estudantes definem ética, moral e deontologia. Qual formula
- Um analista é orientado a suprimir de relatório os testes que revelaram falhas para preser
- Sobre a posição normativa de um código profissional no ordenamento, assinale a alternativa
- Um profissional segue prática tradicional em seu setor, mas a prática omite limites releva
- Conselhos de fiscalização profissional são, em regra, autarquias corporativas com atribuiç
- Uma empresa é obrigada a registrar-se em qualquer conselho profissional cujos integrantes 
- Como a anuidade é paga ao conselho, a inadimplência equivale, por definição, à comprovação
- O fato de uma entidade privada adotar a palavra “conselho” em sua denominação não lhe conf
- Qual é a finalidade institucional mais característica de um conselho público de fiscalizaç
- Conforme o critério geral da Lei nº 6.839/1980, o registro de pessoa jurídica perante cons
- Assinale a opção que distingue corretamente quatro situações institucionais.
- Uma sociedade comercial mantém engenheiro para gerenciar compras internas, mas não presta 
- O registro regular e o diploma não dispensam o profissional de reconhecer limites de suas 
- A transparência sobre um conflito aparente de interesses torna automaticamente legítima a 
- Em trabalho técnico de asseguração, a conclusão deve seguir evidências e requisitos aplicá
- Toda publicidade de serviços profissionais é proibida, com idêntico alcance para todos os 
- Um especialista recebe pedido de laudo cuja complexidade excede sua atribuição e capacidad
- Uma perita possui participação societária importante na empresa que deverá avaliar. Qual r
- Um escritório anuncia “resultado técnico garantido” antes de conhecer as condições dos pro
- Sobre os tipos de conflitos de interesse, qual afirmação está correta?
- Sigilo profissional envolve resguardar informações confidenciais conhecidas em razão do tr
- A Lei Geral de Proteção de Dados admite tratamento de dados pessoais apenas mediante conse
- Se existe fundamento jurídico válido para fornecer determinada informação protegida, isso 
- No exercício de atividades próprias de arquitetura e urbanismo, o RRT integra regime juríd
- Qual situação representa proteção efetiva do sigilo profissional em projeto de tecnologia?
- Sobre bases legais e sigilo, assinale a alternativa correta.
- Uma engenheira assina documento de responsabilidade por obra que nunca acompanhou. Qual av
- Após receber solicitação legalmente fundada de informação confidencial, qual combinação de
- Denúncia de falta ética e decisão condenatória são etapas equivalentes, pois o simples pro
- As exigências constitucionais de contraditório e ampla defesa também alcançam processos ad
- Uma infração ética documental dolosa só pode ser apurada por conselho se, antes, houver co
- A possibilidade de responsabilidade civil, penal e ética pelo mesmo fato não significa que
- Qual medida é incompatível com um processo ético-disciplinar regular?
- Um contador falsifica documento e causa prejuízo patrimonial. Qual avaliação sobre suas po
- Em caso de falha técnica sem dano comprovado, mas com adulteração deliberada de medições, 
- Ao analisar penalidades éticas em diferentes profissões regulamentadas, qual procedimento 
- Todas as pessoas que trabalham com desenvolvimento de software estão obrigadas a inscrever
- O CONAR é entidade não governamental de autorregulamentação publicitária e não deve ser co
- Um contador pode omitir obrigação relevante de demonstrações contábeis quando recebe ordem
- Em teste de segurança contratado, a autorização para avaliar determinado sistema não permi
- Qual exemplo revela comportamento profissional adequado na área de TI?
- Uma agência produz conteúdo comercial remunerado e o apresenta de modo a simular avaliação
- Uma empresa solicita a engenheiro assinatura retroativa para projeto elaborado integralmen
- Na auditoria de informações financeiras, o profissional participa da elaboração dos número
```

## Pedido 12: Manutenção industrial (`engenharia.manutencao-industrial`, 277 tópicos de edital)

```
Matéria: engenharia.manutencao-industrial (Manutenção industrial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e manutenção corretiva e preventiva": 19
- "2. Manutenção preditiva e monitoramento da condição": 19
- "3. Manutenção detectiva, criticidade e segurança": 19
- "4. Indicadores: MTBF, MTTR, disponibilidade, MTTF e OEE": 25
- "5. Planejamento, causas, gestão de ativos e decisões": 18

Já existem (não repita):
- Uma bomba deveria fornecer pelo menos 60 m³/h, mas, mesmo ligada, entrega somente 40 m³/h 
- Uma engrenagem quebra inesperadamente durante a produção, paralisando a máquina e exigindo
- O plano de manutenção determina a troca de um filtro a cada 1.000 horas de funcionamento, 
- Uma vedação já apresenta vazamento fora de especificação, mas a troca foi autorizada para 
- Medições comparáveis de vibração de um rolamento revelam tendência crescente, e a equipe a
- Um dispositivo de parada de emergência fica sem ser acionado na rotina. Em teste autorizad
- Uma máquina reparável registrou 600 horas efetivas de operação e três falhas funcionais no
- Ao longo de três falhas, um sistema somou 12 horas de tempo de restauração, conforme a con
- Uma linha teve 270 horas de operação e 30 horas de parada corretiva, em período composto a
- Uma técnica identifica um sinal de degradação quatro semanas antes da perda de função de u
- Uma inspeção termográfica encontra área aparentemente quente em superfície metálica muito 
- Uma equipe avalia um compressor crítico com diversos modos de falha e quer definir tarefas
- Na análise FMEA de uma máquina, dois modos apresentam o mesmo produto de severidade, ocorr
- Uma unidade industrial registra disponibilidade produtiva de 90%, desempenho de 80% e qual
- Um lote de elementos descartáveis é substituído após a primeira falha definitiva, sem repa
- O planejamento possui 240 horas de tarefas pendentes e capacidade líquida de execução de 6
- Um sistema reparável apresenta MTBF de 100 horas e MTTR de 25 horas, calculados sob defini
- Uma linha possui 500 horas planejadas de produção e permaneceu operando por 450 horas. Pro
- Um equipamento passa a falhar duas vezes em 1.000 horas operacionais, em vez de quatro vez
- Uma equipe mede semanalmente pressão diferencial de um filtro e o troca quando a tendência
- Em um programa de manutenção, as ordens são concluídas rapidamente, mas a mesma falha de r
- Em outubro de 2026, um procedimento de manutenção elétrica precisa citar a versão de NR-10
- Dois relatórios apresentam MTTR para o mesmo sistema. O primeiro conta apenas trabalho efe
- Duas proteções redundantes compartilham a mesma alimentação e podem ficar inoperantes sem 
- Uma organização compara duas estratégias: sensor caro para item não crítico com reposição 
- Uma máquina em funcionamento contínuo deixa de entregar a vazão mínima especificada. Mesmo
- A substituição de uma peça após falha já identificada passa a ser classificada como preven
- Se um plano determina troca de filtro a cada 500 horas, independentemente de sua condição,
- A manutenção detectiva de um intertravamento de segurança possui como objetivo principal s
- Na convenção em que MTBF é tempo operacional dividido por número de falhas, 500 horas de f
- Um sistema registra nove horas de restauração após três falhas. Seu MTTR é nove horas, poi
- Uma organização que acompanha vibração de rolamentos e utiliza tendências e limites tecnic
- Como a termografia é técnica de medição direta, superfícies refletivas e alterações de car
- O MTTR pode ser reduzido pela diminuição da espera por sobressalentes, mesmo que a frequên
- A disponibilidade observada de uma máquina que operou por 180 horas e ficou parada por 20 
- A existência de duas proteções redundantes que compartilham a mesma fonte de alimentação n
- Se disponibilidade produtiva é 90%, desempenho 80% e qualidade 95%, o OEE é igual à média 
- Uma carteira de manutenção tem 180 horas pendentes e consegue executar 45 horas por semana
- Ao localizar anomalia em proteção elétrica, basta pressionar o botão de parada do equipame
- Para ativo reparável em regime compatível com a expressão de disponibilidade inerente, MTB
- Na FMEA, dois modos de falha com o mesmo produto entre severidade, ocorrência e detectabil
- Um intervalo P-F de seis semanas indica uma janela potencial de atuação; adotar inspeção a
- Se uma máquina apresenta disponibilidade operacional de 95%, seu OEE é necessariamente 95%
- Em outubro de 2026, a nova redação da NR-10 publicada pela Portaria MTE nº 737/2026 ainda 
- Uma atividade pode ser considerada adequadamente planejada apenas porque consta do calendá
```

## Pedido 13: Alfabetização, letramento e educação infantil (`educacao.alfabetizacao-letramento-educacao-infantil`, 275 tópicos de edital)

```
Matéria: educacao.alfabetizacao-letramento-educacao-infantil (Alfabetização, letramento e educação infantil)
Capítulos e quantas questões quero em cada um (total 100):
- "Alfabetização, letramento e práticas de linguagem": 17
- "Psicogênese da língua escrita e consciência fonológica": 17
- "Métodos de alfabetização e avaliação formativa": 17
- "Educação Infantil: criança, cuidado, rotina e experiência": 17
- "Campos de experiência, ludicidade e transição escolar": 16
- "Alfabetização na idade certa e síntese para concursos": 16

Já existem (não repita):
- Em uma atividade, o professor ensina correspondências entre letras e sons enquanto os alun
- Uma criança participa da escolha de livros e dita bilhetes, mas ainda não lê palavras de f
- Ao avaliar fluência leitora, qual combinação de dimensões é a mais apropriada?
- Na perspectiva da psicogênese, uma produção com uma letra por sílaba e correspondências so
- A professora observa uma criança escrevendo segmentos de algumas sílabas de modo completo 
- A atividade de pedir que crianças identifiquem palavras que começam com o mesmo fonema tra
- Em comparação com a identificação de rimas, segmentar uma palavra em fonemas exige:
- Um método que parte de fonemas e grafemas para combinar palavras é usualmente classificado
- Uma escola registra escrita espontânea periodicamente, analisa padrões, reorganiza agrupam
- Um estudante lê palavras com precisão, mas não identifica a informação principal de um bil
- Conforme a concepção de criança adotada nas diretrizes da Educação Infantil, a criança é:
- Na creche, durante a alimentação, a educadora conversa, permite pequenas escolhas e incent
- Quais são os eixos estruturantes da Educação Infantil previstos na organização curricular 
- Uma rotina de pré-escola planejada com referências estáveis, mas adaptável a interesses e 
- Em um projeto de observação de plantas, crianças comparam tamanhos, mudanças e condições d
- Quando crianças negociam papéis e regras em uma brincadeira coletiva, a dimensão mais dire
- Na Educação Infantil, a avaliação das aprendizagens e do desenvolvimento deve:
- Qual estratégia favorece a transição da pré-escola para o Ensino Fundamental?
- O Compromisso Nacional Criança Alfabetizada focaliza, como horizonte principal:
- Uma rede interpreta seu Indicador Criança Alfabetizada e organiza formação, apoio às escol
- Em uma prática de alfabetização, a leitura de textos com destinatário e propósito social d
- A criança pode participar de situações de leitura de histórias e produção de bilhetes dita
- A compreensão leitora envolve elementos além da decodificação, como vocabulário, inferênci
- Na psicogênese da língua escrita, os níveis constituem etapas inflexíveis que devem ocorre
- Uma escrita considerada alfabética pode apresentar erros ortográficos, pois compreender o 
- Identificar rimas e reconhecer fonemas isolados são tarefas equivalentes, pois mobilizam e
- Consciência fonológica abrange operações de percepção e manipulação de sons que podem ser 
- O método silábico é classificado entre os métodos sintéticos, ao passo que propostas que p
- Avaliação formativa tem como característica essencial registrar uma nota final, sem necess
- Diante de uma escrita silábico-alfabética, a mediação pedagógica pode propor comparação de
- O cuidado com higiene, alimentação e repouso não integra a educação na creche, pois consti
- A brincadeira é eixo estruturante da Educação Infantil e pode coexistir com planejamento, 
- A BNCC prevê seis direitos de aprendizagem na Educação Infantil, entre eles conviver, brin
- A previsibilidade da rotina escolar exige que as atividades sejam realizadas sempre do mes
- Os cinco campos de experiência da Educação Infantil devem funcionar como matérias isoladas
- O campo “Escuta, fala, pensamento e imaginação” inclui experiências de oralidade, história
- Na Educação Infantil, a avaliação pode usar portfólios e registros descritivos, mas não de
- A transição para o Ensino Fundamental é melhor quando elimina imediatamente brincadeiras e
- O horizonte do Compromisso Nacional Criança Alfabetizada é alfabetizar até o fim do 2º ano
- Um indicador agregado de alfabetização permite dispensar sondagens e observações dos profe
```

## Pedido 14: Materiais, metalurgia, corrosão e ensaios (`engenharia-mecanica.materiais-metalurgia-ensaios`, 275 tópicos de edital)

```
Matéria: engenharia-mecanica.materiais-metalurgia-ensaios (Materiais, metalurgia, corrosão e ensaios)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura cristalina, defeitos e propriedades dos materiais": 20
- "2. Diagramas de fases, aços e tratamentos térmicos": 21
- "3. Processos, materiais industriais e corrosão": 22
- "4. Ensaios mecânicos e caracterização microestrutural": 20
- "5. Metrologia, calibração e integração de resultados": 17

Já existem (não repita):
- Uma equipe compara a célula cúbica de corpo centrado de ferro alfa com sua representação g
- Uma liga de alumínio apresenta estrutura cúbica de faces centradas ideal. Qual fator de em
- Após deformar plasticamente a frio um lote de chapas metálicas, o laboratório constatou au
- Em um ensaio uniaxial no regime elástico, uma barra sofre deformação longitudinal de 0,001
- Duas ligas têm resistência à tração semelhante, mas a primeira apresenta maior módulo de Y
- Ao analisar uma transformação eutetoide em liga binária, uma estudante identifica que uma 
- Um aço próximo à composição eutetoide é resfriado lentamente até a temperatura ambiente. A
- Um material ferroso está no campo monofásico de austenita do diagrama Fe–Fe3C. Qual estrut
- Após austenitizar adequadamente barras de aço, uma oficina busca microestrutura geralmente
- Duas barras de aço de mesmo teor de carbono são temperadas, porém uma apresenta endurecime
- No diagrama binário em certa temperatura, coexistem fases com 10% e 50% de B. Uma liga com
- Um aço hipoeutetoide é resfriado lentamente a partir do campo austenítico. Qual combinação
- Após tratamento de envelhecimento de uma liga endurecível por precipitação, o limite de es
- Uma engrenagem de aço apresenta superfície excessivamente frágil após têmpera, e é necessá
- Uma peça de aço baixo carbono necessita núcleo relativamente tenaz e uma camada superficia
- Uma carcaça industrial de geometria complexa é fabricada por vazamento de metal líquido em
- Um painel estrutural possui fibras contínuas orientadas em um sentido, envolvidas por resi
- Um pequeno rebite de metal menos nobre está conectado eletricamente a uma grande chapa de 
- Para conter corrosão de uma tubulação metálica enterrada, são conectados ânodos de sacrifí
- Uma peça de aço inoxidável opera com solução contendo cloretos e apresenta pequenas cavida
- Dois aços exibem energias Charpy diferentes em temperatura reduzida. Um relatório conclui 
- Uma viga simplesmente apoiada, de vão de 2 m, recebe carga pontual central de 4 kN. Em seç
- Na difração de raios X, uma reflexão de primeira ordem ocorre com radiação de comprimento 
- Uma amostra de compósito começa uma análise termogravimétrica com 20 mg e termina com 15 m
- Ao comparar um comprimento de referência de 25,00 mm com a indicação de 25,04 mm de um ins
- Um bloco de cobre com estrutura CFC apresenta, na célula convencional ideal, coordenação a
- Um polímero com baixo módulo de elasticidade pode ser considerado mais rígido que o aço ap
- Em aço-carbono próximo ao equilíbrio, ferrita alfa e austenita gama são fases cristalinas 
- O revenimento de aço temperado consiste necessariamente em reaquecê-lo à região inteiramen
- Na corrosão eletroquímica de um componente de aço em solução aquosa, a dissolução do ferro
- Um espectro EDS obtido em microscópio eletrônico identifica diretamente a estrutura crista
- Um eixo submetido a carregamento alternado pode falhar por fadiga após muitos ciclos mesmo
- Na normalização convencional de aço, depois da austenitização adequada, o resfriamento típ
- Uma barra obtida por laminação a frio pode apresentar encruamento, enquanto uma peça fundi
- Um paquímetro que repete leituras muito próximas entre si possui, por essa única evidência
- Ao reduzir o tamanho médio dos grãos de um metal em condições compatíveis com a relação de
- A composição de 4,3% de carbono no diagrama metaestável Fe–Fe3C representa a reação euteto
- Em uma seção triangular de composição de sistema ternário, um ponto interno representa trê
- Na proteção catódica de aço por ânodo de sacrifício de zinco, a peça de aço atua como ânod
- Em um laminado polimérico reforçado por fibras unidirecionais, a elevada rigidez longitudi
- A expressão de torção elástica com tensão de cisalhamento igual a T r/J pode ser aplicada 
- Ao comparar a energia absorvida em ensaios Charpy de um aço em diferentes temperaturas, um
- No sistema Fe–Fe3C em equilíbrio, a perlita formada em composição próxima de 0,8% de carbo
- Em DRX de pó, quando o difratograma exibe pico a 2θ = 60°, o ângulo utilizado na lei de Br
- Um adesivo de calibração com a data da última revisão é evidência suficiente de rastreabil
```

## Pedido 15: Filosofia, tipos de conhecimento e correntes epistemológicas (racionalismo, empirismo, criticismo), pós-verdade e inteligência artificial (`etica.filosofia-conhecimento-epistemologia`, 275 tópicos de edital)

```
Matéria: etica.filosofia-conhecimento-epistemologia (Filosofia, tipos de conhecimento e correntes epistemológicas (racionalismo, empirismo, criticismo), pós-verdade e inteligência artificial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. A Filosofia como busca racional pela verdade": 25
- "2. Tipos de conhecimento": 25
- "3. Correntes epistemológicas: racionalismo, empirismo e criticismo": 25
- "4. Desafios contemporâneos: pós-verdade e inteligência artificial": 25

Já existem (não repita):
- A palavra filosofia, de origem grega, significa amor ou amizade pela sabedoria.
- A passagem do mito ao logos, que marca o nascimento da Filosofia na Grécia, consiste na su
- Para Platão e Aristóteles, a filosofia começa pela certeza absoluta transmitida pela tradi
- O método socrático em que, por meio de perguntas, o interlocutor é levado a dar à luz as p
- Na filosofia de Platão, a distinção entre doxa e episteme corresponde, respectivamente, a:
- Na alegoria da caverna, a saída do prisioneiro em direção à luz representa a passagem da i
- A concepção de verdade como adequação ou correspondência entre o pensamento e a realidade 
- A reflexão filosófica caracteriza-se por ser radical, no sentido de:
- O conhecimento empírico ou senso comum é espontâneo, assistemático e baseado na experiênci
- Por ser assistemático, o senso comum produz necessariamente conhecimentos falsos.
- É característica do conhecimento científico:
- Para Karl Popper, o que distingue uma teoria científica de uma não científica é:
- O conhecimento filosófico é racional e sistemático, mas, diferentemente do científico, não
- Um servidor afirma que certa planta cura uma doença porque sua avó sempre a usou; um pesqu
- O conhecimento religioso ou teológico apoia-se na fé e na revelação, e suas verdades são t
- A definição clássica de conhecimento como crença verdadeira justificada foi questionada po
- A pergunta central da epistemologia moderna, disputada entre racionalistas e empiristas, r
- O racionalismo sustenta que a razão é a fonte principal do conhecimento seguro, admite ide
- Descartes chega à primeira certeza indubitável, "penso, logo existo", por meio:
- Para Descartes, o critério que permite reconhecer uma ideia verdadeira é que ela seja clar
- John Locke, expoente do racionalismo, defendia que a mente humana já nasce dotada de ideia
- Segundo Locke, são exemplos de qualidades secundárias dos corpos, que dependem do sujeito 
- Francis Bacon, defensor do método indutivo, chamou de ídolos as falsas noções que distorce
- A tese "ser é ser percebido" (esse est percipi), segundo a qual as coisas existem apenas e
- David Hume sustentou que a ideia de conexão necessária entre causa e efeito não provém da 
- O chamado problema da indução, formulado por Hume, consiste em que:
- No empirismo de Hume, as impressões são as percepções vivas e imediatas, e as ideias são c
- Em relação ao método, é correto associar:
- Kant resume sua posição afirmando que todo conhecimento começa com a experiência, mas nem 
- O criticismo de Kant recebe esse nome porque:
- Para Kant, o juízo "todo corpo é extenso" é analítico, pois o predicado já está contido no
- Segundo Kant, os juízos que ampliam o conhecimento e, ao mesmo tempo, são universais e nec
- Para Kant, espaço e tempo são propriedades das coisas em si, percebidas pelo sujeito de fo
- Na filosofia kantiana, o ser humano pode conhecer:
- A chamada revolução copernicana de Kant consiste em afirmar que, no conhecimento, é o obje
- A frase "pensamentos sem conteúdo são vazios; intuições sem conceitos são cegas" expressa 
- Kant afirmou que a leitura de Hume o despertou do sono dogmático, o que o levou a investig
- O termo pós-verdade designa circunstâncias em que fatos objetivos têm menos influência na 
- A pós-verdade significa que deixou de existir qualquer fato objetivo, razão pela qual toda
- A tendência de aceitar com facilidade informações que confirmam aquilo em que já se acredi
- A expressão bolha de filtro refere-se ao isolamento informacional produzido quando algorit
- Sob a perspectiva filosófica, a pós-verdade se aproxima:
- Modelos de linguagem generativos verificam a veracidade de cada informação antes de produz
- Um sistema de inteligência artificial treinado com dados históricos de contratação passou 
- Os deepfakes, vídeos e áudios sintéticos realistas produzidos por inteligência artificial,
- A opacidade de certos modelos de inteligência artificial, chamada de problema da caixa-pre
- Entre as medidas para enfrentar a desinformação na era digital, a educação midiática propõ
- São princípios frequentemente apontados em diretrizes éticas para o uso de inteligência ar
```

## Pedido 16: Conceitos (`orcamento-publico.conceitos`, 272 tópicos de edital)

```
Matéria: orcamento-publico.conceitos (Conceitos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, funções, natureza jurídica e evolução do orçamento": 14
- "2. Princípios orçamentários e suas exceções constitucionais": 47
- "3. Orçamento-programa, planejamento e estrutura programática": 15
- "4. Estrutura constitucional, competência e controle do orçamento": 13
- "5. Elaboração, discussão, emendas, votação e aprovação": 11

Já existem (não repita):
- Uma administração elabora documento com previsão de receitas, autorização de despesas e me
- Uma política transfere recursos para famílias com renda reduzida a fim de diminuir desigua
- Uma proposta orçamentária reúne todas as receitas e despesas alcançadas pela lei anual, se
- Um projeto de LOA contém autorização para abertura de créditos suplementares nas condições
- Ao estruturar a LOA federal, é preciso distinguir os três conjuntos orçamentários do art. 
- Uma dotação é classificada conforme a unidade administrativa responsável por sua execução.
- Uma unidade organiza recursos para alcançar objetivo mensurável por conjunto de ações rela
- Uma ação orçamentária prevê construir instalação de atendimento em prazo determinado, ampl
- Uma proposta orçamentária chega à fase legislativa federal com emendas a serem analisadas.
- Uma proposta prevê receita de 120 milhões e custo próprio de arrecadação de 8 milhões. Par
- Um texto propõe vincular parte de receita de impostos à manutenção de órgão sem hipótese c
- Um avaliador afirma que a existência de orçamentos fiscal, de investimento e da seguridade
- Um programa prevê manter regularmente serviço de atendimento existente por meio de operaçõ
- Um relatório apresenta a despesa segundo saúde, educação e transporte, detalhando suas sub
- Uma auditoria compara a meta física de uma política com os serviços efetivamente entregues
- Um plano apresenta estratégia plurianual; outra lei destaca prioridades para o ano seguint
- Uma emenda ao projeto de LOA propõe ampliar despesa mediante anulação parcial de recursos 
- Para as emendas individuais à proposta federal, a RCL do exercício anterior ao encaminhame
- Em exercício no qual a RCL realizada do ano anterior foi de R$ 800 milhões, qual montante 
- O Executivo pretende enviar mensagem alterando parte da proposta orçamentária já encaminha
- Admitida RCL de referência de R$ 1 bilhão, como se distribui o limite constitucional de em
- Uma análise afirma que controle externo e interno do orçamento são a mesma atividade e que
- Uma empresa tem participação majoritária do ente público, mas a maioria do capital social 
- Após aprovação de programação discricionária na LOA, alguém afirma que o art. 165, § 10, o
- Uma emenda amplia programa previsto na proposta, é compatível com o PPA e a LDO, mas prete
- Um orçamento organizado por objetivos, programas e ações, com metas de entrega, exemplific
- O princípio da anualidade exige que o orçamento coincida sempre com período de quatro anos
- A LOA constitucional reúne orçamento fiscal, de investimento e da seguridade social, sem q
- Pelo princípio da universalidade, as receitas de fundos públicos vinculadas a ações especí
- A manutenção contínua de um serviço público existente tende a ser representada por ativida
- No processo legislativo orçamentário federal, as emendas ao projeto da LOA são apresentada
- A Constituição, art. 74, contempla a avaliação do cumprimento de metas, a legalidade e res
- A exclusividade da LOA impede que seu texto autorize abertura de créditos suplementares, m
- O PPA tem função de orientação plurianual, a LDO estabelece conexão com prioridades orçame
- Um orçamento que prevê ingressos de 50 milhões e despesas de arrecadação de 5 milhões aten
- Uma ação que reúne operações contínuas para manter serviços preexistentes corresponde norm
- Ao examinar proposta de emenda à LOA, a comissão pode admitir anulação de dotações para pe
- Se a RCL do exercício anterior ao encaminhamento do projeto de LOA é de R$ 300 milhões, o 
- A obrigatoriedade de execução de determinadas emendas individuais ou de bancada impede a c
- O art. 166, § 9º-A, da Constituição reparte o limite individual de 2% da RCL-base em parce
- Após iniciada a votação na comissão mista da parte correspondente a determinada despesa, o
- Para o orçamento de investimento das empresas previsto no art. 165, § 5º, II, importa a ma
- Uma subfunção orçamentária deve ser vinculada exclusivamente à única função que a descreve
- O art. 165, § 10, estabelece dever de executar programações para entregar bens e serviços,
- Uma emenda à lei de diretrizes orçamentárias incompatível com o PPA pode ser aprovada regu
```

## Pedido 17: Relação de trabalho e relação de emprego (`direito-trabalho.relacao-emprego`, 272 tópicos de edital)

```
Matéria: direito-trabalho.relacao-emprego (Relação de trabalho e relação de emprego)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Relação de trabalho, relação de emprego e elementos do vínculo": 27
- "2. Empregado, empregador, grupo econômico e modalidades contratuais": 33
- "3. Autônomo, eventual, avulso, estágio e serviço voluntário": 15
- "4. Trabalho temporário e terceirização: regimes e responsabilidades": 14
- "5. Enquadramento de casos complexos, fraudes e limites da interpretação": 11

Já existem (não repita):
- Uma empresa recebe serviços de pessoa física, com pagamento mensal, comparecimento regular
- Um tradutor presta serviços a três clientes, escolhe os horários e o modo de execução e po
- Uma profissional atua de casa, mas recebe ordens diárias de supervisão por plataforma digi
- Uma instituição beneficente sem finalidade lucrativa contrata assistente que trabalha pess
- Uma cuidadora presta serviços quatro dias por semana no domicílio de uma família, de modo 
- Uma estudante regularmente matriculada realiza atividade ligada ao curso, supervisionada p
- Uma pessoa integra equipe de movimentação de mercadorias fora do porto, escalada por sindi
- Uma indústria contrata empresa prestadora para executar parte de sua atividade principal. 
- Duas sociedades têm o mesmo acionista minoritário, mas atuam em mercados distintos, com ad
- Uma empregada possui diploma superior, recebe salário mensal superior ao dobro do teto do 
- Uma empresa de trabalho temporário contrata pessoa para atender demanda complementar compr
- Uma pessoa executa serviço voluntário por termo de adesão e recebe reembolso de deslocamen
- Uma pessoa assina contrato escrito de emprego intermitente e é convocada para períodos alt
- Uma pessoa física trabalha de forma não eventual, remunerada e subordinada em propriedade 
- Uma estudante realiza estágio não obrigatório, regular e supervisionado. A entidade conced
- Uma pessoa jurídica prestadora tem como sócio indivíduo que trabalhou para a futura contra
- Uma estudante tem termo de estágio e matrícula, mas a concedente a mantém em tarefas alhei
- Um trabalhador temporário cumpriu os períodos máximos dos §§ 1º e 2º do art. 10 e volta à 
- Empresas integrantes de grupo econômico efetivo são demandadas por obrigação empregatícia;
- Uma pessoa contratada como autônoma exclusivamente para um cliente é remunerada por nota f
- Um avulso não portuário recebe serviços por intermédio do sindicato, sem emprego direto co
- Uma empresa contrata diretamente empregado em experiência por 90 dias. Outra utiliza traba
- Uma empresa dispensou empregado e pretende contratá-lo após dez meses como empregado de pr
- Uma faxineira realiza serviço pessoal, remunerado e subordinado a família em âmbito reside
- Uma entidade sem finalidade lucrativa admite profissional altamente especializado, que pre
- Uma pessoa física presta serviço remunerado de modo pessoal e não eventual, sob direção ef
- Uma pessoa que recebe honorários por serviço executado uma única vez torna-se empregada pe
- A subordinação jurídica pode ser exercida mediante plataforma digital, ainda que o serviço
- Uma associação recreativa sem finalidade lucrativa não pode ser empregadora, ainda que adm
- O trabalho doméstico da LC nº 150/2015 exige, entre outros requisitos, prestação sem final
- No estágio regular, a existência de bolsa de estudos converte o vínculo em emprego, pois t
- Uma atividade principal pode ser executada por prestadora terceirizada de serviços; a legi
- A mera coincidência de dois sócios entre sociedades distintas é suficiente para configurar
- Um contrato de trabalho intermitente pode alternar períodos de prestação subordinada e ina
- Na Lei nº 9.608/1998, o voluntário perde essa condição sempre que a entidade ressarcir des
- O trabalho temporário pode atender demanda complementar resultante de fator previsível que
- O empregado doméstico e o empregado comum da CLT submetem-se ao mesmo limite obrigatório d
- Para serviços de movimentação de mercadorias em geral sem vínculo empregatício fora dos po
- A responsabilidade da contratante por obrigações trabalhistas de prestação terceirizada re
- Uma empresa dispensou empregado e, dez meses depois, pretende recebê-lo novamente como emp
- A empresa prestadora, cujo sócio aposentado trabalhou para a contratante no período dos úl
- O contrato de emprego comum pode ser tácito ou expresso, segundo o art. 442 da CLT; por ou
- A prorrogação de até 90 dias no trabalho temporário é automática, mesmo que a demanda tran
- Um autônomo contratado com observância das formalidades pode prestar serviços continuament
- Uma pessoa contratada diretamente pela empresa para prestação intermitente subordinada dev
```

## Pedido 18: Ciência política: teoria do Estado, democracia e sistema partidário (`ciencia-politica.teoria-estado-democracia-partidos`, 270 tópicos de edital)

```
Matéria: ciencia-politica.teoria-estado-democracia-partidos (Ciência política: teoria do Estado, democracia e sistema partidário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Política, poder, legitimidade e ação social": 23
- "2. Formação histórica do Estado e pensamento político": 19
- "3. Soberania, formas de Estado e sistemas de governo": 17
- "4. Democracia, representação, eleições e partidos": 18
- "5. Estado de Direito, participação e atuação judicial em políticas públicas": 23

Já existem (não repita):
- Em uma reunião comunitária, uma associação mobiliza moradores e consegue alterar a priorid
- Um grupo aceita ordens de uma liderança porque reconhece regras impessoais de investidura 
- Uma pessoa participa de uma manifestação por considerar moralmente obrigatório defender um
- Uma norma foi aprovada pelo procedimento previsto, mas é contestada por amplos setores com
- Após debate público, uma autoridade escolhe uma prioridade entre alternativas juridicament
- Em um debate sobre a origem do Estado, alguém defende autoridade soberana robusta para sup
- Uma comunidade sustenta que o governo deve proteger direitos individuais e que o consentim
- No planejamento de uma constituição hipotética, os constituintes afirmam que a comunidade 
- Uma pesquisa explica a adesão a movimentos políticos pela interação entre incentivos, cren
- Um estudo relaciona mudanças institucionais a conflitos entre classes, transformações das 
- Um povo se reconhece por língua, memória e tradições comuns, mas vive distribuído por dive
- Um país assegura constitucionalmente competências legislativas próprias a entidades territ
- Um comentário afirma que determinado país é federal, republicano e presidencialista. As ex
- Em determinado país, o gabinete responsável pela condução do governo pode perder sustentaç
- Uma constituição estabelece poderes independentes, mas prevê fiscalização legislativa, con
- Uma consulta popular é convocada para que a cidadania se manifeste sobre proposta ainda nã
- Uma comunidade escolhe representantes periodicamente e também participa de debates público
- Em uma eleição proporcional hipotética, uma pessoa recebe votação individual expressiva, p
- Dois partidos desejam atuar conjuntamente em disputas proporcionais e também permanecer li
- Um analista identifica numerosos partidos com representação, mas observa que três agrupame
- Uma política governamental é discutida publicamente; durante a execução, relatórios permit
- Uma proposta sobre melhoria de serviços transita pela formação de agenda, elaboração de al
- Diante de grave deficiência em serviço destinado a assegurar direito fundamental, uma deci
- Em uma ação coletiva sobre um serviço essencial, discute-se se o juiz pode impor de imedia
- Uma maioria recém-eleita sustenta que sua vitória autoriza abolir instrumentos de oposição
- Um partido deixa de preencher o requisito constitucional de desempenho para acessar determ
- Uma associação influencia a elaboração de uma lei, apesar de não possuir competência para 
- Uma autoridade consegue obediência mediante ameaça, de modo que já está comprovada a legit
- Uma pessoa participa de uma manifestação por fidelidade a uma convicção ética, mesmo sem e
- Uma pesquisa usa escolhas individuais para explicar mobilização coletiva; por isso, compro
- Um povo mantém identidade cultural comum embora esteja distribuído por mais de um Estado. 
- Em um Estado federal, todas as unidades integrantes detêm soberania internacional própria,
- Ao examinar uma monarquia constitucional com gabinete dependente do parlamento, um estudan
- Se um gabinete pode perder confiança política da maioria legislativa e ser substituído sem
- Uma decisão constitucional define consulta popular para ratificar norma já aprovada. Por o
- Uma eleição proporcional considera a votação total do partido ou federação e também desemp
- Como a Constituição assegura autonomia partidária, dois partidos podem celebrar coligação 
- Os integrantes de uma federação partidária devem, como regra legal, manter o vínculo por p
- Se uma legenda não preenche a cláusula constitucional de desempenho, sua personalidade jur
- Uma decisão judicial responde a uma deficiência grave em serviço de direito fundamental fi
- O reconhecimento constitucional da separação dos poderes impede, segundo o Tema 698, que q
- Um pesquisador diferencia judicialização, entendida como chegada de controvérsias ao Judic
- Uma audiência pública sobre planejamento urbano é, por natureza, um referendo, pois a mani
- Um processo político pode satisfazer a regra de maioria e ainda ferir o Estado Democrático
- Como o Estado moderno reivindica o monopólio do uso legítimo da força, a descrição socioló
- Em uma pesquisa, explicar conflitos políticos por classes e relações de produção e, em out
```

## Pedido 19: Polinômios e equações polinomiais (`matematica.polinomios-equacoes-polinomiais`, 270 tópicos de edital)

```
Matéria: matematica.polinomios-equacoes-polinomiais (Polinômios e equações polinomiais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, identidade, operações e valor numérico": 20
- "2. Produtos notáveis, fatoração e frações polinomiais": 24
- "3. Divisão, teorema do resto, fator e multiplicidades": 19
- "4. Funções polinomiais: gráficos, raízes e estudo do sinal": 17
- "5. Equações polinomiais, relações entre raízes e problemas integrados": 20

Já existem (não repita):
- Após reduzir termos semelhantes de $$P(x)=5x^4-2x^2+7-5x^4$$, qual é o grau do polinômio r
- Um cálculo algébrico exige somar $$A(x)=2x^2-3x+4$$ e $$B(x)=x^2+5x-7$$. Qual resultado es
- Ao calcular a área de um quadrado cujo lado mede $$x-3$$, um estudante precisa desenvolver
- Uma expressão $$x^2-49$$ deve ser fatorada sem recorrer à fórmula quadrática. Qual produto
- Considere $$F(x)=\frac{x^2-16}{x-4}$$. Qual representação simplificada conserva o domínio 
- Ao dividir $$P(x)=x^3-2x+1$$ pelo polinômio $$x+2$$, qual será o resto, sem precisar efetu
- Uma função afim representa a diferença entre uma receita variável e um custo fixo, por $$f
- Um gráfico de $$f(x)=x^2-5x+6$$ cruza o eixo horizontal em quais abscissas reais?
- Para que a identidade $$(ax+2)(x-1)=3x^2-x-2$$ seja válida para todo x, qual valor deve te
- Na expansão de $$ (x+2)^3 $$, um estudante precisa determinar todos os termos pelo cubo da
- Somando as frações $$\frac{1}{x-1}$$ e $$\frac{2}{x+1}$$, que resultado simplificado prese
- Uma fração racional é dada por $$\frac{x^2-5x+6}{x^2-9}$$. Qual redução é equivalente no d
- Um gráfico de $$f(x)=x^2-6x+8$$ tem vértice em qual ponto do plano cartesiano?
- Considere a função cúbica $$h(x)=(x+2)(x-1)^2$$. Onde seu gráfico apresenta valores estrit
- Efetuando a divisão de $$P(x)=x^3-2x^2+4x-5$$ por $$x-2$$, quais são o quociente e o resto
- Para a equação $$2x^2-10x+12=0$$, quais são, respectivamente, soma e produto de suas raíze
- Um polinômio cúbico é dado por $$x^3-27$$. Qual fatoração completa, com fatores de coefici
- Deseja-se decompor $$\frac{3x+1}{(x-1)(x+2)}$$ como $$\frac{A}{x-1}+\frac{B}{x+2}$$. Quais
- Na divisão de $$P(x)=x^3+2x^2+3x+4$$ pelo polinômio $$x^2-1$$, qual é o resto de grau infe
- Considere $$P(x)=(x-2)^2(x+1)^3$$. Quantas vezes as raízes 2 e −1 aparecem, respectivament
- Uma função quadrática é $$f(x)=-x^2+4x-3$$. Em que conjunto de valores reais ela é estrita
- A equação $$2x^3-3x^2-8x+12=0$$ admite fatoração inteira. Qual conjunto contém todas as ra
- Considere $$F(x)=\frac{x^3-3x^2+2x}{x^2-1}$$. Qual par informa corretamente os zeros reais
- Determine k para que $$x-2$$ seja fator de $$P(x)=x^3+kx-10$$. Qual valor satisfaz o teore
- O gráfico de $$h(x)=(x+2)(x-1)^2$$ cruza o eixo horizontal em uma raiz e toca o eixo na ou
- Após simplificar $$7x^2-7x^2+5$$, obtém-se um polinômio constante não nulo, de grau zero.
- A expressão $$x^{-2}+3$$ é um polinômio de grau dois, porque o expoente tem módulo igual a
- A identidade $$ (a+b)(a-b)=a^2-b^2 $$ vale para todos os valores reais de a e b.
- A fração $$\frac{x^2-4}{x-2}$$, após o cancelamento, passa a admitir também x igual a 2 no
- Na divisão de um polinômio P pelo fator $$x-4$$, o teorema do resto estabelece que a const
- Todo polinômio real de grau três possui necessariamente três raízes reais distintas, porqu
- Para a função $$f(x)=2x-6$$, o gráfico é uma reta crescente e os valores de f são negativo
- Desenvolvendo $$ (x-4)^2 $$, chega-se a $$x^2-16$$, porque o quadrado preserva apenas os e
- Duas frações polinomiais podem ter numeradores e denominadores diferentes e ainda represen
- Na soma de $$\frac{1}{x-1}$$ com $$\frac{1}{x+1}$$, o resultado é $$\frac{2}{2x}$$, pois n
- Se $$f(x)=x^2-6x+9$$, a parábola tem uma raiz real dupla em x igual a 3, onde toca o eixo 
- Qualquer função cúbica real apresenta exatamente três cruzamentos distintos com o eixo hor
- Em $$ax^3+bx^2+cx+d=0$$, com a não nulo, o produto das três raízes complexas, contadas com
- Se o valor de um polinômio P em x igual a 1 for zero, então $$x+1$$ é necessariamente fato
- O desenvolvimento de $$ (a-b)^3 $$ contém os quatro termos $$a^3-3a^2b+3ab^2-b^3$$, com si
- Se duas frações algébricas se simplificam à mesma expressão polinomial, elas necessariamen
- Ao dividir $$x^3+2x^2+3x+4$$ por $$x^2-1$$, é correto obter resto $$4x+6$$, de grau inferi
- Para $$h(x)=(x+2)(x-1)^2$$, o gráfico troca de sinal tanto ao passar por x igual a −2 quan
- Se um polinômio possui coeficientes reais e admite a raiz complexa $$2+3i$$, então $$2-3i$
- Na equação $$x(x-3)=0$$, é válido dividir ambos os lados por x sem ressalva, pois a operaç
```

## Pedido 20: Sistemas de amortização: Price, SAC e SAM (`matematica-financeira.sistemas-amortizacao`, 269 tópicos de edital)

```
Matéria: matematica-financeira.sistemas-amortizacao (Sistemas de amortização: Price, SAC e SAM)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos dos planos de amortização e leitura das tabelas": 15
- "2. Sistema de Amortização Constante (SAC)": 25
- "3. Sistema francês (Price) e a prestação constante": 25
- "4. Sistema de Amortização Misto (SAM)": 23
- "5. Sistema americano e fundo de amortização (sinking fund)": 12

Já existem (não repita):
- Um cliente toma R$ 2.000,00 emprestados à taxa de 5% no primeiro período e paga prestação 
- No primeiro período, uma dívida de R$ 2.100,00 gera R$ 210,00 de juros. O devedor paga R$ 
- No início da primeira competência de um financiamento há saldo de R$ 3.000,00. A taxa efet
- Uma operação de R$ 2.400,00 será quitada em quatro períodos iguais pelo SAC, com juros cal
- Um empréstimo de R$ 3.000,00 é pago pelo SAC em três prestações postecipadas a 10% por per
- Uma dívida de R$ 3.000,00 foi distribuída em três amortizações iguais de R$ 1.000,00, com 
- Um financiamento de R$ 2.100,00 é pago em duas prestações Price, com taxa efetiva de 10% p
- Na Tabela Price, um empréstimo inicial de R$ 3.310,00 é pago a 10% por período em três pre
- Uma tabela Price de três períodos apresenta amortizações de R$ 1.000,00 e R$ 1.100,00 nas 
- Para uma dívida de R$ 2.100,00 em dois períodos a 10%, a primeira prestação SAC é R$ 1.260
- Uma dívida de R$ 2.400,00 é amortizada em quatro parcelas iguais pelo SAC. Após a quitação
- Um empréstimo de R$ 4.000,00, com taxa efetiva de 2% por período, será amortizado pelo SAC
- Um empréstimo de R$ 2.100,00 será liquidado pela Price em dois períodos, à taxa efetiva de
- Uma tabela Price para R$ 3.310,00, três períodos e taxa de 10% apresenta três prestações i
- Num financiamento de R$ 2.100,00 em dois períodos a 10%, a segunda prestação SAC vale R$ 1
- Um financiamento de R$ 2.100,00 tem dois vencimentos a 10%, com prestações SAM de R$ 1.235
- Para D = R$ 2.100,00, n = 2 e i = 10% por período, três cronogramas completos apresentam S
- Um financiamento Price de R$ 3.310,00, a 10% por período, tem três prestações de R$ 1.331,
- Em um empréstimo pelo sistema americano clássico, R$ 5.000,00 são financiados por três per
- Uma dívida americana de R$ 2.100,00 vence após dois períodos e paga juros ao credor de 8% 
- Em contrato SAM de R$ 2.100,00, prazo de dois períodos e taxa de 10%, a primeira prestação
- Em um financiamento SAC de R$ 2.400,00 em quatro prestações à taxa efetiva de 5% por perío
- Em financiamento francês postecipado, a prestação constante é R$ 1.210,00 em dois períodos
- Um empréstimo sem cobrança de juros é liquidado em três prestações postecipadas de igual i
- No sistema americano com fundo, uma obrigação de R$ 4.000,00 deve ser liquidada com quatro
- Um financiamento sem correção monetária cobra juros de 4% por período. Para obter o juro d
- Um devedor pagou prestação de R$ 600,00, na qual R$ 100,00 correspondiam a juros. A reduçã
- No SAC regular, dívida de R$ 3.000,00 dividida em três períodos produz amortização de R$ 1
- Num plano Price de taxa fixa e sem reajustes, as prestações são constantes porque as parce
- No SAM clássico, se a primeira prestação SAC vale R$ 1.260 e a Price constante vale R$ 1.2
- No SAC de R$ 2.100 em duas amortizações a 10%, o saldo devedor depois da primeira prestaçã
- Uma Price de R$ 3.310 em três prestações de R$ 1.331, a 10% por período, apresenta juros d
- No SAC de R$ 3.000 em três parcelas a 10% por período, os juros totais são R$ 900, pois a 
- Um financiamento de R$ 2.100, a 10% em dois períodos, apresenta prestação Price de R$ 1.21
- Quando a taxa de juros da Tabela Price é zero, deve-se substituir diretamente i=0 na razão
- Uma operação de R$ 2.400 utiliza SAC com quatro amortizações postecipadas iguais de R$ 600
- Em empréstimo americano clássico de R$ 5.000 a 10% por período, o pagamento de R$ 500 de j
- No SAC sem correção, quando a amortização constante é de R$ 1.000 e a taxa por período é d
- No sistema de amortização misto, a prestação é encontrada pela média das taxas de juros ef
- Na Price regular, o saldo devedor imediatamente depois de uma prestação corresponde ao val
- Uma dívida americana de R$ 2.100 tem taxa do empréstimo de 8% ao período e fundo que rende
- Na Price de R$ 3.310 em três períodos à taxa de 10%, se a primeira amortização é R$ 1.000,
- Para mesmo capital, prazo e taxa positiva, o SAM clássico tem juros totais idênticos aos d
- Um sinking fund deve alcançar R$ 2.100 ao fim de dois períodos. Com remuneração de 10% por
- Em qualquer financiamento contratualmente denominado Price, o desembolso total mensal perm
```

## Pedido 21: Gestão de riscos de SI, continuidade de negócios e normas de cibersegurança (`informatica.gestao-riscos-continuidade-ciberseguranca`, 269 tópicos de edital)

```
Matéria: informatica.gestao-riscos-continuidade-ciberseguranca (Gestão de riscos de SI, continuidade de negócios e normas de cibersegurança)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos dos riscos e da segurança da informação": 17
- "2. ISO 31000, IEC 31010 e ISO/IEC 27005 na prática": 24
- "3. Continuidade de negócios, BIA e recuperação": 19
- "4. Políticas nacionais, estratégia cibernética e rede de incidentes": 19
- "5. Instruções normativas, nuvem, mídias sociais e PPSI": 21

Já existem (não repita):
- Ao revisar uma aplicação, uma equipe encontra uma credencial compartilhada que pode ser ex
- Um arquivo eletrônico sofreu alteração não autorizada, mas permaneceu disponível aos usuár
- Uma unidade pretende adotar princípios e processo geral de gestão de riscos para setores t
- O sistema de atendimento precisa voltar a funcionar em até três horas após uma interrupção
- Uma equipe busca os requisitos de um sistema de gestão de continuidade de negócios, distin
- Uma instituição revisa a regulamentação da PNSI em outubro de 2026. Qual decreto institui 
- Ao cumprir as regras federais de gestão de segurança, uma entidade consulta o art. 3º da I
- Uma base requer restauração com perda de, no máximo, quinze minutos de alterações. Qual pa
- Durante exercício de contingência, a equipe deseja validar a restauração de um banco de da
- Para examinar possibilidades de ataque a um serviço, uma equipe precisa escolher entre árv
- Um contrato prevê cobertura financeira de parte das perdas decorrentes de incidente, sem a
- Um órgão federal atualiza suas diretrizes de cibersegurança e encontra uma referência à E-
- A entidade possui plano de gestão de riscos, mas não elaborou nenhum documento que reúna c
- Uma equipe analisa a adesão à Rede Federal de Gestão de Incidentes Cibernéticos por empres
- Na análise de continuidade, o impacto de uma paralisação passa a ser intolerável após dezo
- A unidade implementou controles e concluiu que permanece risco inferior ao limite formal d
- O setor de privacidade encontra referência à Portaria SGD/MGI nº 852/2023 e deseja identif
- Uma instituição classifica como alto um risco após análise de probabilidade e consequência
- O relatório de identificação, análise e avaliação de riscos foi concluído em março. Em jul
- O planejamento nacional inclui conscientização da sociedade, resiliência de serviços essen
- Após contratar hospedagem em nuvem, uma organização conclui que o provedor assumiu todos o
- Um plano de continuidade foi elaborado com base na antiga ABNT NBR 15999, e uma auditoria 
- Uma análise sobre informações classificadas em nuvem usa versão antiga do art. 17 da IN GS
- Um incidente causa indisponibilidade de sistema essencial e demanda investigação de causa,
- O programa federal de privacidade passa por reformulação normativa entre 2023 e 2026. Qual
- Em um sistema de registros digitais, a modificação indevida de um documento caracteriza im
- Ao encontrar vulnerabilidade em servidor, a instituição pode concluir que o impacto financ
- Um gestor escolhe a ISO 31000:2018 para organizar princípios e processo gerais de gestão d
- Quando um plano exige RPO de trinta minutos, a organização pode considerá-lo cumprido apen
- Diante de interrupção grave, um procedimento manual temporário pode compor estratégia legí
- Uma unidade pode tratar o Decreto nº 10.222/2020 como a versão vigente da Estratégia Nacio
- No inventário de ativos de uma organização, o registro das interdependências entre serviço
- O catálogo da ISO/IEC 27005:2022 demonstra que a ISO 27005:2018 é atualmente a edição inte
- Ao renovar contrato de hospedagem, uma organização que compartilha parte das perdas financ
- Após concluir uma BIA, a instituição identifica os cenários de ameaça com precisão estatís
- O Decreto nº 11.856/2023 estabelece a Política Nacional de Cibersegurança, enquanto o Decr
- Por integrar a administração indireta federal, uma sociedade de economia mista está submet
- Os processos de mapeamento de ativos, gestão de riscos, continuidade, gestão de mudanças e
- O responsável por segurança considera desnecessário atualizar o relatório de riscos diante
- Mesmo após alcançar um RTO de duas horas em exercício de recuperação, uma unidade deve ver
- Uma base de dados com RPO de quinze minutos atende ao requisito quando o único backup rest
- Em uma simulação de indisponibilidade, verificar comunicação, tomada de decisão e recupera
- O Decreto nº 12.572/2025 revogou a PNCiber instituída em 2023 e assumiu, no mesmo instrume
- No ciclo de 2026, a análise de normas do PPSI deve considerar a Portaria SGD/MGI nº 9.511/
- A ISO 31000:2018 define modelo único e certificável de gestão de riscos e obriga todas as 
```

## Pedido 22: Técnicas radiológicas e radioproteção (`radiologia.tecnicas-radiologicas-radioprotecao`, 268 tópicos de edital)

```
Matéria: radiologia.tecnicas-radiologicas-radioprotecao (Técnicas radiológicas e radioproteção)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos físicos, formação da imagem e equipamentos": 17
- "Radiografia convencional, CR/DR, processamento e qualidade": 17
- "Posicionamento radiográfico por segmentos anatômicos": 17
- "Contrastes, mamografia e métodos seccionais": 17
- "Radioproteção, dosimetria, monitoração e blindagem": 16
- "Rotinas seguras, gestão do exame e aplicação em concursos": 16

Já existem (não repita):
- No tubo de raios X diagnóstico, qual componente é associado à emissão termiônica de elétro
- Um serviço utiliza cassetes com placas de fósforo fotoestimulável, posteriormente lidas po
- Em paciente colaborativo, qual projeção é classicamente preferida para radiografia convenc
- Quais projeções são consideradas fundamentais no posicionamento mamográfico de rotina?
- Em radioproteção, qual unidade expressa dose absorvida como energia depositada por unidade
- Assinale a modalidade que não emprega radiação ionizante para produzir a imagem.
- Em processamento de filme radiográfico, qual é a função central da etapa de fixação?
- Na radiografia de ossos longos, uma boa prática clássica, quando a indicação exigir avalia
- Ao dobrar a distância de uma fonte aproximadamente puntiforme até um ponto de medida, mant
- A imagem DR aparenta exposição adequada na tela, mas o índice de exposição permanece siste
- Uma radiografia portátil AP de tórax mostra silhueta cardíaca maior que uma PA anterior. Q
- Durante triagem para TC com contraste iodado, a pessoa relata comer camarão sem reações e 
- Em proteção radiológica, qual princípio deve orientar a seleção de um exame ionizante apen
- O controle de qualidade identifica desvio persistente no alinhamento entre o campo luminos
- No visor de tomografia, o técnico encontra CTDIvol e DLP. Qual interpretação é adequada?
- Na suspeita de fratura proximal do fêmur após queda, qual conduta técnica de posicionament
- Em imagem radiográfica convencional com elevado espalhamento, qual alteração tende a melho
- Na RM, o aparelho está sem adquirir imagens, mas segue energizado em operação normal. Qual
- Assinale a formulação correta sobre limites de dose e proteção de pacientes em radiodiagnó
- Em radiografia CR, surgem artefatos lineares recorrentes em vários pacientes usando sempre
- Uma pessoa com dor abdominal precisa de radiografia com feixe horizontal para possível pne
- Sobre a DXA e suas medidas, qual alternativa diferencia corretamente os escores de referên
- Na fiscalização de um serviço de radiologia em 2026, qual par de atos corresponde corretam
- Ao avaliar rejeições em DR, descobre-se aumento de repetição por imagens com marcador de l
- Os raios X empregados em radiografia convencional são radiações eletromagnéticas ionizante
- A radiografia digital dispensa colimação física, pois a área irradiada pode ser limitada d
- A projeção AP portátil de tórax pode alterar a magnificação aparente do coração em compara
- A tomografia computadorizada e a ressonância magnética utilizam a mesma radiação ionizante
- Os dosímetros individuais são instrumentos de monitoração ocupacional e não substituem med
- Uma imagem radiográfica nítida, mas atribuída ao paciente incorreto, deve ser considerada 
- A radiação espalhada pode reduzir o contraste da radiografia e contribuir para a exposição
- No sistema CR, a placa fotoestimulável precisa ser lida por equipamento apropriado; no DR,
- Na suspeita de fratura instável de quadril, a rotação interna forçada do membro é obrigató
- O gadolínio de determinados contrastes para ressonância magnética representa, por si só, u
- Na exposição médica de um paciente, a proteção radiológica se baseia em justificação e oti
- Em programas de qualidade, a taxa de rejeição de imagens deve ser acompanhada junto com su
- Na revelação química de filmes radiográficos, o fixador transforma inicialmente os cristai
- A incidência occipitomentoniana de Waters pode ser utilizada para destacar estruturas faci
- O CTDIvol mostrado na estação de TC corresponde exatamente à dose efetiva absorvida indivi
- A ANSN 3.01 aprovada em 2025 e a RDC Anvisa 611/2022 possuem conteúdos e âmbitos distintos
- Se a imagem digital parece visualmente adequada após ajuste de janela, é desnecessário con
- Para manter o mesmo mAs, é possível elevar a corrente do tubo e encurtar o tempo, o que po
- A ampla latitude do DR torna impossível ocorrer superexposição, porque o processamento equ
- Para pesquisar ar intraperitoneal em paciente que não pode ficar em ortostase, o decúbito 
- A exposição por raios X do paciente durante um exame e a dose ocupacional do profissional 
```

## Pedido 23: Gestão ambiental: licenciamento, unidades de conservação, recursos naturais e educação ambiental (`meio-ambiente.gestao-licenciamento-unidades-conservacao`, 268 tópicos de edital)

```
Matéria: meio-ambiente.gestao-licenciamento-unidades-conservacao (Gestão ambiental: licenciamento, unidades de conservação, recursos naturais e educação ambiental)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da gestão ambiental e dos recursos naturais": 17
- "Impactos ambientais, avaliação de impactos e EIA/RIMA": 17
- "Licenciamento ambiental: etapas, condicionantes e competências": 17
- "SNUC: categorias, criação e gestão das unidades de conservação": 17
- "Monitoramento de água, ar, solo e gerenciamento de áreas contaminadas": 16
- "Sistemas de gestão ambiental e educação ambiental aplicada": 16

Já existem (não repita):
- Um recurso natural classificado como renovável pode tornar-se escasso quando a exploração 
- No diagnóstico ambiental, os meios físico, biótico e socioeconômico são analisados isolada
- O RIMA tem função de comunicar à sociedade as conclusões do EIA de modo inteligível, sem d
- A Licença Prévia autoriza, como regra, o início da operação do empreendimento desde que o 
- O grupo de Proteção Integral do SNUC possui cinco categorias, entre elas Parque Nacional, 
- Um lançamento pode atender a limite aplicável de concentração no efluente sem que isso, po
- O impacto ambiental é sempre negativo; qualquer alteração benéfica gerada por empreendimen
- Impactos cumulativos podem ocorrer quando fontes de pequena intensidade individual atuam r
- A competência municipal para licenciar todas as atividades situadas no seu território deco
- A Reserva Particular do Patrimônio Natural integra o grupo de Uso Sustentável do SNUC, ape
- O monitoramento de qualidade da água pode dispensar informações sazonais e medições em dif
- A investigação confirmatória de uma área suspeita busca verificar a contaminação, ao passo
- Um SGA certificado segundo a ISO 14001 equivale ao licenciamento público e dispensa a empr
- A existência de uma APA instituída pela União atrai, sem exceções, a competência do Ibama 
- No gerenciamento de uma área contaminada, avaliar risco pressupõe considerar não apenas a 
- Em uma AIA, a área diretamente afetada, a área de influência direta e a área de influência
- A possibilidade de manutenção de propriedades particulares compatíveis com os objetivos de
- A concentração de um poluente no ar ambiente mede necessariamente a emissão de uma chaminé
- No ciclo PDCA de um SGA, verificar indicadores, investigar não conformidades e realizar au
- A publicação da ISO 14001:2026 torna automaticamente nulas, a partir da data de sua public
- Durante planejamento de uso de uma floresta, qual medida expressa a gestão sustentável de 
- Uma equipe identificou geração de efluente e redução de oxigênio dissolvido no rio a jusan
- No procedimento clássico de licenciamento LP–LI–LO, qual ato autoriza a etapa de operação?
- Qual alternativa apresenta exclusivamente categorias do grupo de Proteção Integral do SNUC
- Para verificar melhoria de um programa de educação ambiental sobre segregação de resíduos,
- Uma obra terá supressão de vegetação e alteração de acesso de comunidades. Qual procedimen
- Qual afirmação sobre EIA e RIMA é correta no contexto do licenciamento ambiental brasileir
- Uma empresa pretende construir galpão depois de receber LP e antes de obter LI no procedim
- Sobre uma unidade de conservação na categoria APA, escolha a descrição compatível com o SN
- Qual conjunto de parâmetros é especialmente relevante para analisar aporte de matéria orgâ
- Em indústria com emissões atmosféricas, qual distinção é tecnicamente válida?
- Qual atividade corresponde melhor à etapa Check no ciclo PDCA de um SGA?
- Considere as 12 categorias do SNUC. Qual opção as distribui corretamente?
- Uma AIA de rodovia registra: fragmentação da vegetação na faixa de obra; alteração futura 
- Sobre a repartição de competências para licenciamento, considere uma atividade situada em 
- Uma rede de monitoramento apresenta turbidez maior depois de chuvas fortes; uma obra inici
- Uma investigação confirmatória mostrou contaminação por solventes em solo e água subterrân
- Uma entidade afirma que seu projeto de educação ambiental foi eficaz porque realizou 60 pa
- Ao comparar planos de manejo de duas UCs, uma RPPN e um Parque Nacional, o técnico deve co
- Em outubro de 2026, qual afirmação é mais precisa sobre normas e certificação de sistemas 
```

## Pedido 24: ECA: ato infracional e proteção integral (`legislacao-penal-especial.eca-ato-infracional`, 266 tópicos de edital)

```
Matéria: legislacao-penal-especial.eca-ato-infracional (ECA: ato infracional e proteção integral)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Proteção integral, sujeitos de direitos e conceito de ato infracional": 16
- "2. Apreensão, garantias e procedimento de apuração do ato infracional": 19
- "3. Medidas socioeducativas e execução segundo o Sinase": 27
- "4. Remissão, medidas protetivas e integração do procedimento socioeducativo": 17
- "5. Crimes do ECA, alterações de 2025 e 2026 e jurisprudência aplicada": 21

Já existem (não repita):
- Uma criança de 11 anos praticou conduta descrita como crime de dano. Qual consequência é j
- Uma adolescente de 16 anos praticou conduta descrita em lei como contravenção penal. Quant
- Uma equipe pretende apreender adolescente que não se encontra em flagrante. Que condição p
- Foi decretada internação antes da sentença para adolescente, com decisão motivada por nece
- Em medida de prestação de serviços à comunidade, qual combinação de limites encontra respa
- O adolescente recebeu liberdade assistida para acompanhamento familiar, escolar e comunitá
- Antes do início de procedimento judicial, o Ministério Público analisa circunstâncias de a
- Um adolescente praticou ato infracional mediante grave ameaça à pessoa. Ao avaliar interna
- Adolescente cumpre internação socioeducativa, sem termo final fixado. A equipe conclui que
- O adolescente foi submetido a semiliberdade e precisa frequentar curso e trabalho externo.
- Após apuração, há prova da materialidade de ato infracional e apenas indícios suficientes 
- Uma adolescente é apreendida em flagrante de ato praticado com violência contra pessoa. Qu
- Um adolescente não foi liberado após flagrante e sua apresentação imediata ao Ministério P
- O Ministério Público oferece representação para apuração de ato infracional, sem provas pr
- Durante a apuração judicial de ato infracional, o juiz considera cabível remissão antes da
- Uma adolescente iniciou liberdade assistida e outra foi incluída em programa de internação
- Uma adolescente comete ato infracional análogo ao tráfico de drogas, sem violência ou grav
- Uma pessoa praticou ato infracional aos 17 anos, está cumprindo liberdade assistida aos 19
- Na execução de medida socioeducativa de internação, o responsável sustenta que a gravidade
- Um agente apreendeu criança fora de flagrante e sem ordem escrita judicial. A defesa invoc
- Um adulto fornece gratuitamente bebida alcoólica a adolescente, que efetivamente a consome
- Uma pessoa divulga o mesmo registro ilícito de violência sexual contra criança em três pla
- Uma imagem digital foi manipulada com inteligência artificial para simular participação de
- Um adulto pratica infração penal com adolescente que já possuía registros de atos infracio
- Adolescente confessa fato atribuído na audiência. A acusação e a defesa propõem dispensar 
- Uma pessoa de 11 anos comete conduta descrita como contravenção penal. Nos termos do ECA, 
- Um fato foi praticado aos 17 anos e apurado quando o autor completou 18. A maioridade na d
- A apreensão de adolescente e o local de sua custódia devem ser comunicados imediatamente à
- Diante de internação provisória, o ECA autoriza cumprir 45 dias cautelares e, em seguida, 
- A prestação de serviços à comunidade pode ser fixada por até seis meses, com jornada máxim
- A liberdade assistida possui prazo máximo fixo de seis meses, após o qual a medida se ence
- A remissão não representa, necessariamente, reconhecimento ou comprovação da responsabilid
- Como órgão autônomo e não jurisdicional, o Conselho Tutelar pode impor liberdade assistida
- Na execução do Sinase, medidas de liberdade assistida, semiliberdade e internação devem se
- Por ser formal, o crime de corrupção de menores do art. 244-B exige comprovar que o adulto
- Depois de iniciada a apuração judicial de ato infracional, a autoridade judiciária pode co
- O Sinase impõe prazo único de 45 dias para elaborar qualquer PIA, independentemente de a m
- Na hipótese de flagrante de ato infracional com violência ou grave ameaça à pessoa, o art.
- O prazo de 45 dias para elaborar o PIA de adolescente em internação, previsto no art. 55 d
- Em caso de descumprimento reiterado e injustificável de medida anterior, a internação-sanç
- O adolescente em internação socioeducativa que completa 18 anos deve ser imediatamente lib
- A conduta de aliciar por meio eletrônico pessoa menor de 14 anos com o fim de praticar ato
- O art. 241-B, na redação de 2026, tipifica exclusivamente a venda de conteúdo de violência
- A Lei nº 15.163/2025 inseriu no art. 230, §2º, do ECA previsão que afasta a aplicação da L
- Após a Lei nº 15.487/2026, qualquer imagem digital que represente criança fictícia configu
```

## Pedido 25: Arquitetura e urbanismo: planejamento urbano, habitação e gestão urbana (`arquitetura.planejamento-urbano-habitacao`, 265 tópicos de edital)

```
Matéria: arquitetura.planejamento-urbano-habitacao (Arquitetura e urbanismo: planejamento urbano, habitação e gestão urbana)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Urbanização, uso do solo e instrumentos de ordenação territorial": 20
- "2. Leitura físico-ambiental, infraestrutura, paisagem e espaço público": 19
- "3. Planejamento integrado, participação e governança urbana": 20
- "4. Habitação social, assentamentos precários e intervenções comunitárias": 21
- "5. Requalificação, análise espacial e avaliação de projetos urbanos": 20

Já existem (não repita):
- Uma cidade aumentou a proporção de moradores urbanos, mas suas imagens mostraram área edif
- Uma cidade com 35 mil habitantes revisa seu plano diretor. Qual característica é exigida p
- Um terreno de 500 m² contém construção com projeção de 200 m² e área computável total de 1
- Moradias são propostas numa encosta próxima a talvegue com histórico de inundações. Qual a
- O projeto de uma praça inclui uma escola infantil, bancos e abrigos de parada. Qual classi
- Um loteamento é proposto em terreno com declividade de 32%. Qual interpretação da Lei 6.76
- Três municípios limítrofes compartilham transporte coletivo cuja gestão separada gera inco
- Um projeto de plano diretor foi fechado antes de qualquer diálogo e publicado pouco antes 
- Um programa distingue HIS de HMP, porém o município ainda não definiu limite de renda de H
- Família urbana com renda mensal de 2,5 salários mínimos solicita assistência técnica para 
- Um núcleo urbano informal ocupado predominantemente por pessoas de baixa renda foi assim d
- Um projeto de regularização fundiária envolve área informal com risco de deslizamento. Que
- Cortiço central tem instalações sanitárias precárias e moradores dependentes de empregos p
- Região metropolitana possui PDUI em elaboração, e um município cogita deixar de produzir p
- Um empreendimento necessita de EIV previsto em lei municipal e de EIA exigido pela legisla
- Assentamento consolidado ocupa parte de APP junto a um córrego e está sujeito a inundação.
- Um projeto pretende demolir amplas quadras e reconstruir a malha; outro quer recuperar fac
- Depois de pavimentação extensa a montante, uma bacia passou a ter cheias mais intensas a j
- Mapa SIG aponta residências a 400 metros em linha reta de uma unidade de saúde, porém há f
- Setores X e Y possuem 2.400 habitantes cada um. X mede 0,8 km² e Y mede 0,4 km². Qual afir
- Um município com 18 mil habitantes integra região metropolitana, mas sustenta que sua popu
- Um projeto de Reurb em núcleo informal pretende omitir o levantamento do sistema viário, e
- Uma intervenção em praça propõe bancos inclinados com dispositivos para impedir permanênci
- Um estudo apresenta imagem raster da ocupação edificada e cadastro vetorial de lotes. A eq
- Uma associação organiza autogestão de recursos para melhoria habitacional e contrata traba
- Em cidade que concentra mais moradores nos bairros já construídos, o adensamento pode ocor
- No plano diretor de um município com comunidades rurais, o diagnóstico e as diretrizes ter
- Um lote pavimentado e sem árvores pode continuar situado em área legalmente protegida, poi
- Na revisão do plano diretor, basta apresentar o texto concluído em evento público, pois o 
- Em uma praça, a implantação de banco, lixeira e abrigo de parada corresponde a mobiliário 
- Se a lei municipal exigir EIV para um shopping center, o documento aprovado elimina eventu
- No programa habitacional de uma cidade, a reabilitação de edifício existente para atender 
- Uma região metropolitana que possui PDUI aprovado dispensa todos os municípios integrantes
- Ao mapear pontos de iluminação em um SIG, representá-los por feições vetoriais pontuais é 
- O conceito de HMP estabelece por lei federal a mesma faixa máxima de renda em todos os mun
- Uma praça antiga recebe nova iluminação, drenagem, arborização e passeios acessíveis, mant
- Setor de encosta com grande suscetibilidade a deslizamento apresenta obrigatoriamente o me
- Programa de assistência técnica habitacional pode priorizar intervenção sob regime de muti
- A classificação de um núcleo informal como Reurb-E é obrigatória quando sua população foi 
- A análise de vizinhança de um empreendimento que atrai viagens e modifica a paisagem pode 
- Um consórcio de cidades propõe aprovar PDUI por simples deliberação de sua diretoria, sem 
- Ao escolher locais para um serviço, um estudo que usa buffers de distância euclidiana pode
- Uma renovação em centro consolidado que eleva aluguéis e desloca residentes demonstra nece
- Em assentamento informal com trechos ambientalmente frágeis, a regularização fundiária pod
- Um plano estratégico que registrou a entrega de dez quilômetros de calçadas comprova, só c
```

## Pedido 26: Auditoria contábil: fraudes, omissão de receitas e passivos fictícios (`auditoria.fraudes-escrita-contabil`, 265 tópicos de edital)

```
Matéria: auditoria.fraudes-escrita-contabil (Auditoria contábil: fraudes, omissão de receitas e passivos fictícios)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fraude, erro e planejamento dos testes de auditoria": 13
- "2. Caixa, recomposição financeira e suprimentos de origem duvidosa": 25
- "3. Passivos fictícios, passivos ocultos, recebíveis e ativos não registrados": 29
- "4. Receitas, despesas, estoques, escrita fiscal e operações de importação": 19
- "5. Presunções legais, integração de provas e resolução de casos": 14

Já existem (não repita):
- Ao identificar faturamento sem documento de entrega e informação sobre possível ajuste del
- Uma empresa exibe despesas duplicadas no diário. Qual técnica mais diretamente testa se as
- Ao procurar vendas realizadas e não lançadas, por onde o exame deve começar preferencialme
- Em 15/05, o Caixa tinha R$ 4.000; houve R$ 3.000 de entradas comprovadas e R$ 9.000 em pag
- Uma duplicata a receber de R$ 11.000 foi baixada contra Caixa, mas o devedor confirmou não
- A entidade apresenta passivo de fornecedor quitado em abril e mantido em junho. Qual class
- Após confrontar extratos e razão, um crédito bancário de R$ 40.000 mostra transferência en
- Uma compra de mercadoria por R$ 30.000 teve frete diretamente atribuível de R$ 2.000 e tri
- O auditor encontra mercadoria entregue e ainda não paga, mas ausente de Estoques e Fornece
- Um cliente transferiu R$ 16.000 em dezembro por serviço a realizar integralmente em fevere
- Uma filial deixou de registrar pagamento de compra de R$ 25.000 feito pela entidade. Que d
- Um lançamento indica integralização de capital de R$ 120.000, mas não há transferência ban
- O cálculo físico mostra estoque inicial de 90 unidades, compras de 250, saídas de 270 e es
- Na auditoria de importação, a fatura registra 400 unidades, mas o recebimento físico e o e
- Uma obrigação de R$ 18.000 consta no passivo sem documentos que demonstrem credor ou exigi
- A empresa recebeu R$ 60.000 em empréstimo comprovado e o valor foi creditado em sua conta 
- Qual situação é teste clássico de superavaliação de ativo registrado?
- Uma venda de R$ 35.000 ocorreu a prazo em novembro; o cliente pagou em janeiro. Qual efeit
- A conta Caixa apresenta déficit de R$ 9.000 num dia, corrigido por crédito de R$ 14.000 no
- A empresa registrou empréstimo de sócio com contrato e extrato de crédito, mas a suposta c
- Numa reconstituição, duplicata de R$ 32.000 foi paga em dezembro, mas continua em Forneced
- Em importação, o custo inclui R$ 90.000 de mercadoria, R$ 8.000 de frete, R$ 9.000 de trib
- A empresa vendeu bem do imobilizado e contabilizou o preço recebido como vendas de mercado
- Em um teste de depósitos sem origem, o titular foi intimado e apresentou extratos de trans
- Um levantamento quantitativo indica 30 unidades de saídas incompatíveis com os registros. 
- Ao se constatar um lançamento divergente em receita, a classificação como fraude depende d
- O uso regular do limite bancário, com saldo devedor perante a instituição financeira, equi
- Um título de fornecedor efetivamente quitado, mas ainda lançado em obrigações a pagar, é e
- Em venda a prazo corretamente reconhecida, a cobrança posterior deve gerar outra receita n
- No exame de despesas artificialmente elevadas, confrontar as notas, a prestação do serviço
- O documento societário de aumento de capital, desacompanhado de prova da transferência dos
- No dia 3, o Caixa parte de R$ 10.000, recebe R$ 1.000 e paga R$ 13.500. O saldo recomposto
- A identificação de obrigação real por mercadoria recebida e não registrada evidencia passi
- Na busca de receitas omitidas, pode ser útil partir de comprovantes independentes de entre
- Para presumir omissão com base no art. 42 da Lei nº 9.430/1996, basta apontar crédito banc
- O custo de mercadoria importada pode contemplar imposto de importação não recuperável e fr
- A presença de mais produtos no inventário físico que na escrita demonstra necessariamente 
- Na auditoria de importação, comparar fatura comercial, conhecimento de carga, declaração a
- Se o déficit de Caixa ocorreu dia 10 e houve entrada dia 11, o saldo mensal positivo demon
- A falta de escrituração de pagamentos feitos pela pessoa jurídica é tratada no art. 40 da 
- O art. 12, § 3º, do DL nº 1.598/1977 autoriza aplicar indistintamente a hipótese de suprim
- Ao encontrar baixa de Clientes contra Caixa sem ingresso financeiro, a confirmação direta 
- Se R$ 12.000 de tributo efetivamente recuperável forem incluídos na aquisição e o estoque 
- Quando há depósito sem origem comprovada e passivo fictício no mesmo exercício, a legislaç
- Um contrato de empréstimo sem prova da entrega, mas com assinatura reconhecida, comprova n
```

## Pedido 27: Assistência farmacêutica e políticas de medicamentos no SUS (`farmacia.assistencia-farmaceutica-sus`, 265 tópicos de edital)

```
Matéria: farmacia.assistencia-farmaceutica-sus (Assistência farmacêutica e políticas de medicamentos no SUS)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Políticas de medicamentos, assistência farmacêutica e redes do SUS": 22
- "2. Ciclo da assistência farmacêutica e boas práticas de logística": 17
- "3. Componentes básico, estratégico e especializado da assistência": 20
- "4. Uso racional, interações medicamentosas e farmacovigilância": 24
- "5. Controle especial, incorporação de tecnologias e qualidade do cuidado": 17

Já existem (não repita):
- Ao revisar a política de medicamentos, uma unidade quer localizar o ato que aprovou origin
- Uma equipe limita a assistência farmacêutica à entrega física de medicamentos. Que disposi
- Uma comissão escolhe princípios ativos e apresentações para compor o elenco municipal, com
- Uma farmácia prevê consumo de 600 comprimidos em dois meses, dispõe de 100 unidades aptas 
- Dois lotes estão íntegros: o lote recebido em julho vence em dezembro e o recebido em agos
- O depósito central encaminha caixas para unidades e, depois, uma pessoa recebe orientação 
- Uma unidade de atenção primária organiza medicamentos para condições prevalentes, acompanh
- Para programa de controle de doença endêmica com forte importância epidemiológica, um esta
- Uma pessoa solicita medicamento ambulatorial para condição prevista em PCDT, com exigência
- Um medicamento CEAF pertence ao grupo 1A. Qual arranjo de financiamento e aquisição corres
- Uma secretaria estadual compra diretamente medicamento do CEAF com recursos transferidos p
- Um estado assume o financiamento, a compra e a dispensação de medicamento CEAF classificad
- A coordenação regional debate o grupo 3 do CEAF e quer identificar o componente cujas regr
- Uma pessoa ingere tetraciclina com suplemento de cálcio e observa redução da exposição ao 
- Uma pessoa utiliza simultaneamente dois sedativos, desenvolvendo maior depressão do sistem
- Um serviço suspeita de reação adversa grave após medicamento, mas ainda não conseguiu comp
- No recebimento, o serviço identifica frascos lacrados com rótulos descolando e não há even
- Uma farmácia recebe prescrição de substância classificada na Lista B1 da Portaria SVS/MS n
- Um serviço precisa diferenciar o controle de antimicrobianos sujeitos a retenção de receit
- Um medicamento tem registro sanitário vigente, mas ainda não integra listas nem protocolos
- Uma proposta de nova tecnologia é protocolada para avaliação administrativa de incorporaçã
- Uma decisão de incorporação de medicamento ao SUS é publicada. A equipe confunde o prazo d
- Duas opções terapêuticas oferecem ganhos diferentes de qualidade e duração de vida. O estu
- Um usuário em uso de varfarina relata ter eliminado todos os vegetais para evitar interaçã
- Uma unidade pretende comparar a Resolução CFF nº 357/2001 com a RDC Anvisa nº 44/2009 e o 
- Uma equipe inclui revisão de prescrição, orientação e acompanhamento de resultados no plan
- Um serviço escolheu novos medicamentos para sua lista e classificou essa decisão como prog
- Ao organizar lote mais recente com vencimento próximo, a farmácia priorizou seu uso antes 
- Uma unidade básica recebe regularmente medicamento padronizado para condição prevalente. P
- Uma pessoa com condição clínica enquadrada em PCDT solicita tratamento ambulatorial do CEA
- Um usuário comunica sonolência intensa após uso de medicamento e o profissional adia o reg
- No CEAF, determinado produto foi classificado no grupo 1B. O financiamento é da União, mas
- Uma secretaria recebeu remessa termolábil com registrador indicando excursão de temperatur
- Uma equipe acompanha indicadores de hipertensão na atenção primária, revisa dificuldades d
- Um medicamento do grupo 2 do CEAF é adquirido por uma secretaria estadual, mas seu financi
- No almoxarifado, uma pessoa classifica por análise ABC os itens de maior valor de consumo 
- Uma pessoa em uso de varfarina apresenta oscilação de consumo de alimentos ricos em vitami
- Duas tecnologias apresentam eficácia e resultados clínicos comprovadamente equivalentes pa
- Uma prescrição de antimicrobiano abrangido pela RDC Anvisa nº 471/2021 é tratada como se p
- Uma tecnologia registrada pela Anvisa passa por avaliação de incorporação no SUS, com evid
- Uma equipe calcula o prazo para ofertar tecnologia já incorporada: 180 dias mais prorrogaç
- Em análise de incorporação, um estudo apresenta custo adicional por QALY/AVAQ, enquanto ou
- Em uma unidade hospitalar, um erro de medicação foi identificado antes de alcançar o pacie
- Um serviço detecta reação adversa associada a possível desvio de qualidade e decide invest
- Um serviço reconhece que um medicamento está na Rename e, sem verificar PCDT ou grupo de f
```

## Pedido 28: Geometria espacial: áreas e volumes (`matematica.geometria-espacial-volumes`, 264 tópicos de edital)

```
Matéria: matematica.geometria-espacial-volumes (Geometria espacial: áreas e volumes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos métricos, poliedros e relação de Euler": 20
- "2. Cubos, paralelepípedos e prismas: áreas, volumes e diagonais": 20
- "3. Pirâmides, semelhança e troncos": 19
- "4. Cilindros, cones, esferas e tronco de cone": 24
- "5. Cavalieri, escala, composições e problemas de estimativa": 17

Já existem (não repita):
- Uma caixa cúbica fechada possui aresta de 4 cm. Qual é o volume interno idealizado, despre
- Uma embalagem retangular fechada mede 2 cm, 3 cm e 5 cm nas direções perpendiculares. Qual
- Um prisma tem área da base de 12 cm² e distância perpendicular entre as bases de 5 cm. Qua
- Uma pirâmide tem base retangular de 6 cm por 4 cm e altura perpendicular de 9 cm. Qual é o
- Um cilindro circular reto fechado apresenta raio de 2 cm e altura de 5 cm. Qual expressão 
- Um poliedro convexo tem oito faces e doze arestas. Quantos vértices possui, pela relação d
- Uma diagonal atravessa o interior de um cubo de aresta 3 cm e liga dois vértices opostos. 
- Um recipiente retangular comporta exatamente 2 dm³ de líquido. Quantos litros correspondem
- Uma esfera sólida tem raio de 3 cm. Qual é sua área superficial externa?
- Uma haste rígida será acomodada entre vértices opostos de um bloco retangular de dimensões
- Um poliedro convexo tem 4 faces triangulares e 6 faces quadrangulares. Quantas arestas pos
- Em uma exposição de poliedros regulares convexos, há uma peça composta por doze faces pent
- Uma pirâmide regular tem base quadrada de lado 10 cm e altura perpendicular 12 cm. Qual ár
- Uma cobertura de papel tem a forma da superfície lateral de um cone reto, sem disco de bas
- Uma miniatura é ampliada de modo que todos os comprimentos lineares dobrem. Como se modifi
- Um prisma reto e outro oblíquo têm a mesma altura perpendicular e a mesma área em todas as
- Uma pirâmide de base quadrada é cortada por plano paralelo à base na metade da altura, med
- Um tronco de pirâmide tem bases quadradas paralelas semelhantes de áreas 36 cm² e 9 cm², s
- Um tronco de cone circular reto tem raios de base de 5 cm e 2 cm, com altura perpendicular
- Um poliedro convexo possui doze faces, todas triangulares. Cada aresta pertence a duas fac
- Um modelo de icosaedro regular convexo é composto por vinte faces triangulares equiláteras
- Um prisma triangular reto tem base triangular de lados 3, 4 e 5 cm e altura de 10 cm. Quan
- Uma peça tem forma de semiesfera maciça com raio de 4 cm e sua base circular plana está in
- Um bloco externo mede 10 cm por 8 cm por 5 cm. Uma cavidade interna totalmente contida no 
- Um tanque cilíndrico ideal tem raio interno de 0,5 m e altura interna de 2 m. Adote pi igu
- Uma peça tridimensional convexa possui seis faces, oito vértices e doze arestas. Essas con
- Um cilindro circular e uma esfera são poliedros porque, assim como o cubo, ocupam volume e
- Em um cubo de aresta de 2 cm, a área total vale 24 cm² e o volume vale 8 cm³, apesar de as
- Uma pirâmide e um prisma com áreas de base iguais e mesma altura perpendicular também têm 
- A superfície lateral de um cilindro circular reto, quando planificada, produz um retângulo
- Um tanque de 3 m³ possui capacidade ideal de 300 litros, pois a transformação de metro cúb
- Em um poliedro convexo de dez faces, sendo seis quadrangulares e quatro triangulares, exis
- Duas retas que não se encontram no espaço são necessariamente paralelas, pois a ausência d
- A diagonal espacial de paralelepípedo retângulo de arestas perpendiculares 6 cm, 8 cm e 24
- Para qualquer prisma oblíquo, a área lateral pode ser calculada pelo perímetro da base mul
- Um corte paralelo à base que produz pirâmide superior semelhante com escala linear de um t
- O volume de um cone circular reto com raio de 3 cm e altura de 4 cm vale 36 pi cm³, pois é
- Dois sólidos têm igual altura e áreas iguais em todas as seções por planos paralelos corre
- A relação de Euler $$V-A+F=2$$, verificada para determinada peça, é suficiente por si só p
- Em uma pirâmide regular de base quadrada com lado de 10 cm e altura de 12 cm, o apótema la
- Um tronco de cone reto com raios 5 cm e 2 cm e altura 4 cm possui volume igual ao produto 
- Uma semiesfera sólida de raio 5 cm tem superfície curva de área $$50\pi$$ cm² e, se inclui
- Se todas as dimensões lineares de um recipiente geometricamente semelhante são reduzidas e
- Um corpo impermeável inteiramente submerso em recipiente cilíndrico, sem transbordamento, 
- A área lateral de um tronco de cone circular reto de raios 5 cm e 2 cm e geratriz de 5 cm 
```

## Pedido 29: Armazenamento de dados, datacenter e backup (`informatica.armazenamento-san-nas-raid-backup`, 264 tópicos de edital)

```
Matéria: informatica.armazenamento-san-nas-raid-backup (Armazenamento de dados, datacenter e backup)
Capítulos e quantas questões quero em cada um (total 100):
- "1. DAS, NAS, SAN e fundamentos dos sistemas de arquivos": 20
- "2. Fibre Channel, fabric, zoning, NPIV e S3": 23
- "3. RAID, capacidade útil e falhas de discos": 18
- "4. Replicação, disponibilidade, deduplicação e ciclo de vida": 18
- "5. Backup, restauração, fita, Bacula e continuidade": 21

Já existem (não repita):
- Uma equipe precisa disponibilizar uma pasta compartilhada por SMB, com acesso simultâneo d
- Um servidor recebe um volume remoto identificado como LUN, que será formatado com sistema 
- Um armazenamento Fibre Channel permite conexão entre dois WWPNs, mas o volume ainda não é 
- Quatro discos de 2 TB serão organizados em RAID 5 convencional. Sem reservas nem metadados
- Uma solução deve suportar falha simultânea de duas unidades com paridade distribuída e dis
- Um serviço estabelece RPO de quinze minutos e RTO de duas horas. O que descreve corretamen
- Após backup completo no domingo, ocorreram incrementais na segunda e terça. Para restaurar
- Um administrador deseja realizar backup completo e depois diferencial diário. Qual conjunt
- No Bacula, um componente mantém o catálogo de arquivos, tarefas e volumes para localizar d
- Uma aplicação deve guardar imagens com chave identificadora e metadados em buckets, consum
- Um ambiente virtualizado compartilha um único adaptador Fibre Channel físico entre várias 
- Um analista cria outro nome para um arquivo Unix, apontando ao mesmo inode e sem duplicar 
- Uma instalação tem seis unidades de 3 TB e deseja RAID 6 convencional, sem hot spare. Quan
- Num arranjo RAID 10 de quatro discos em dois pares espelhados, falham duas unidades perten
- Uma organização precisa reduzir latência no site primário, aceitando risco de perder algum
- Cem máquinas armazenam imagens quase idênticas, repetindo grandes blocos de dados. Qual me
- Em um sistema de backup Bacula, o componente que executa no equipamento protegido para ler
- Um bucket S3 recebe gravação confirmada de um novo objeto e, logo depois, uma aplicação co
- Um servidor tem dois HBAs, mas ambos atravessam o mesmo switch FC até o storage. O projeto
- Um RAID 5 de cinco unidades tem uma unidade hot spare e perde um disco de dados. Antes de 
- Dois datacenters replicam síncronamente um volume. Um usuário autorizado exclui acidentalm
- Uma aplicação é migrada para nós ativo-ativo em locais distintos. Após quebra de comunicaç
- Um backup de banco de dados copia arquivos de armazenamento enquanto transações escrevem c
- Um plano de proteção tem três pastas com cópias no mesmo volume, na mesma controladora e s
- Uma política ILM mantém imagens recentes em SSD e transfere automaticamente objetos rarame
- Uma unidade de armazenamento diretamente conectada por SAS ao servidor, sem rede intermedi
- O protocolo NFS apresenta ao cliente setores brutos de uma LUN para que ele formate o disc
- Em uma fabric Fibre Channel, zoning delimita quais portas podem se comunicar, sem substitu
- Um conjunto RAID 0 distribui blocos por dois discos e pode recuperar todos os dados após f
- Na replicação assíncrona, o destino pode estar temporariamente atrasado em relação ao prim
- Depois de full no domingo, um backup diferencial feito na quarta usa como referência apena
- Em sistema Unix, dois hard links para um arquivo podem referenciar o mesmo inode, enquanto
- Em ambiente virtualizado, habilitar NPIV equivale a instalar adaptadores Fibre Channel fís
- O versionamento de um bucket S3 pode preservar versões anteriores quando uma chave é sobre
- Durante um rebuild RAID 5, a presença de hot spare garante tolerância à perda simultânea d
- Uma política ILM pode mover dados pouco acessados para camada de armazenamento mais econôm
- No Bacula, o Catalog é o componente encarregado de receber o fluxo de arquivos e gravar os
- Uma mídia de fita pode ser utilizada como cópia offline para retenção, embora o acesso seq
- Uma solução informa latência de 1 milissegundo e 50 mil IOPS. Esses dois indicadores medem
- Uma SAN pode funcionar sobre TCP/IP por meio de iSCSI, que transporta comandos SCSI sobre 
- Como o S3 oferece consistência forte de leitura após escrita e listagem, uma aplicação pod
- Se um RAID 10 possui quatro discos distribuídos em dois pares espelhados, a perda de um di
- Um conjunto replicado sincronamente para outro datacenter, com confirmação remota de escri
- Um backup que terminou sem erro de transferência ainda pode ser inútil se capturou banco d
- Três cópias de arquivos alojadas em diretórios da mesma LUN, com as mesmas credenciais e o
- A VTL (Virtual Tape Library) é um sistema baseado em disco que emula uma biblioteca de fit
- Em uma SAN Fibre Channel, o enlace que conecta dois switches da mesma fabric é chamado de:
```

## Pedido 30: Microeconomia: oferta, demanda e equilíbrio (`economia.microeconomia-oferta-demanda`, 261 tópicos de edital)

```
Matéria: economia.microeconomia-oferta-demanda (Microeconomia: oferta, demanda e equilíbrio)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Escassez, escolhas e teoria elementar do consumidor": 16
- "2. Demanda, oferta, determinantes e equilíbrio de mercado": 28
- "3. Elasticidades, tipos de bens e resposta do consumidor": 26
- "4. Excedentes, eficiência, preços máximos e mínimos": 16
- "5. Impostos, subsídios, incidência e perda de eficiência": 14

Já existem (não repita):
- Uma pessoa dispõe de uma tarde livre: poderia trabalhar e ganhar R$ 120 ou estudar. Ao opt
- Uma economia utiliza tecnologia e recursos fixos para fabricar alimentos e tecidos. Em um 
- Uma família informa salário de R$ 4.000 recebido por mês e saldo bancário de R$ 18.000 em 
- O preço de um produto sobe de R$ 10 para R$ 12, sem mudança de renda, gosto ou preço de ou
- Uma inovação reduz o custo de fabricar determinado bem em todos os níveis de produção. Man
- No mercado de um produto, demanda é dada por Qd=120−2P e oferta por Qs=20+3P. Qual par rep
- Depois de aumento permanente da renda de uma família, o consumo de um bem cai, embora seu 
- O equilíbrio de um produto ocorre ao preço de R$ 60. Uma norma impõe preço máximo de R$ 50
- Uma consumidora dispõe de renda de R$ 120 para dois bens X e Y, com preços de R$ 20 e R$ 1
- O preço do produto Y sobe 20%, mantendo outros determinantes, e a procura do produto X cre
- Em dois momentos, o preço de um bem passou de R$ 10 para R$ 12 e a quantidade demandada de
- Quando a renda aumenta em 20%, o consumo de viagens de uma família sobe em 30%, com preços
- Uma indústria consegue aumentar a quantidade ofertada em 15% quando o preço de venda sobe 
- Por aumento de renda dos compradores e inovação produtiva, demanda e oferta de um mesmo be
- Um mercado tem demanda inversa P=100−Q e oferta inversa P=20+Q. No equilíbrio competitivo 
- No mercado P_d=100−Q e P_s=20+Q, com preço competitivo R$ 60, fixa-se preço mínimo de R$ 7
- Mantidos os preços de dois produtos, um consumidor aumenta a renda e altera suas cestas ót
- O preço de um alimento inferior sobe e, em uma situação excepcional, sua quantidade demand
- Num mercado com demanda inversa P=100−Q e oferta inversa P=20+Q, incide imposto de R$ 20 p
- Sobre o mesmo mercado P_d=100−Q e P_s=20+Q, concede-se subsídio de R$ 20 por unidade, na a
- Em equilíbrio competitivo, a elasticidade da oferta é 2 e o módulo da elasticidade da dema
- No mercado linear P_d=100−Q, P_s=20+Q, um tributo unitário de 20 gera perda de peso morto 
- Uma empresa monopolista enfrenta demanda inversa P=100−Q e custo marginal constante 20. Se
- Uma economia pequena em relação ao mercado mundial institui tarifa sobre importações, com 
- Uma pessoa escolhe X e Y perfeitamente substitutos na utilidade U=X+Y, com renda de R$ 100
- Uma economia pode operar em um ponto dentro de sua fronteira de possibilidades de produção
- Todo bem inferior é necessariamente um bem de Giffen, pois aumento da renda reduz sua proc
- Se apenas o preço de um bem aumenta, mantendo-se renda, preferências e preços relacionados
- Uma inovação que reduz custos de produção desloca a oferta de mercado para a esquerda, o q
- Em um mercado competitivo sem controles, a condição de equilíbrio é a igualdade entre quan
- Em um mercado cujo preço competitivo é 60, um teto legal de 70 reduz necessariamente o pre
- Em mercado com demanda decrescente e compradores com disposições marginais a pagar distint
- O saldo de uma conta bancária em uma data é uma variável de fluxo, enquanto o salário rece
- Se a elevação do preço de uma impressora provoca, mantidos outros fatores, diminuição da d
- Em uma curva de demanda linear decrescente, a inclinação numérica constante implica elasti
- Quando produtores dispõem de tempo para investir, contratar e ampliar capacidade, a oferta
- Um bem com elasticidade-renda de −0,7 é normal necessário, porque o módulo inferior a um d
- Para bem inferior, uma redução de seu preço provoca efeito-substituição favorável ao consu
- Se renda eleva a demanda de um bem e tecnologia amplia simultaneamente sua oferta, com cur
- No mercado competitivo P_d=100−Q e P_s=20+Q, um imposto específico de 20 reduz a quantidad
- Num subsídio de valor positivo por unidade, o preço pago pelo consumidor torna-se necessar
- Em monopólio com demanda inversa linear P=100−Q e custo marginal constante 20, imposto uni
- Quando a obrigação legal de recolher imposto específico competitivo cabe ao produtor, toda
- Em mercado competitivo com oferta perfeitamente inelástica e demanda decrescente regular, 
- Uma tarifa sobre importações aplicada por economia pequena, sem falhas prévias nem retalia
```

## Pedido 31: Microeconomia: estruturas de mercado e custos (`economia.microeconomia-estruturas-mercado`, 261 tópicos de edital)

```
Matéria: economia.microeconomia-estruturas-mercado (Microeconomia: estruturas de mercado e custos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria da produção, fatores e tecnologia da firma": 19
- "2. Custos, receita, lucro e curvas de oferta": 34
- "3. Concorrência, monopólio, monopsônio e diferenciação": 18
- "4. Oligopólio: Cournot, Bertrand, Edgeworth e mark-up": 15
- "5. Falhas de mercado, monopólio natural e regulação": 14

Já existem (não repita):
- Uma oficina passa de dois trabalhadores produzindo 24 peças por dia para três trabalhadore
- Uma fábrica mantém fixo o número de máquinas e contrata trabalhadores adicionais. A produç
- Uma empresa consegue fabricar 100 unidades usando diferentes combinações de horas de traba
- Uma firma paga aluguel contratado de instalação que não varia quando produz entre zero e 1
- Uma fábrica tem custo fixo de 100 unidades monetárias e custo variável dado por cinco veze
- Na mesma fábrica, o custo total de produzir dez unidades é 250 unidades monetárias. Qual é
- Para a função de custo total composta por 100 fixos, cinco vezes a produção e o quadrado d
- Uma firma perfeitamente competitiva vende a preço inferior ao mínimo do custo variável méd
- Uma pequena firma faturou 160 unidades monetárias, pagou 100 de despesas explícitas e sacr
- Duas firmas competitivas ofertam, ao preço de 20 unidades monetárias, respectivamente quat
- No equilíbrio competitivo de longo prazo com livre entrada, determinada firma obtém lucro 
- Um monopolista enfrenta demanda inversa de preço igual a 100 menos duas vezes Q e tem cust
- No mesmo monopólio, a quantidade escolhida é 20, o preço é 60 e o custo total corresponde 
- Um monopsônio enfrenta oferta de trabalho de salário igual a dez mais a quantidade L de tr
- Em concorrência monopolística, novas variedades entram ao longo do tempo e eliminam lucro 
- Um mercado é dominado por três produtoras, e cada uma revê sua quantidade ou preço ao ante
- Duas firmas homogêneas competem à Cournot com demanda inversa de preço igual a 100 menos a
- No duopólio Cournot com demanda de preço igual a 100 menos Q, custo marginal 20 e firmas s
- Duas firmas com mesmo custo marginal constante de 20 vendem produto homogêneo, definem pre
- Duas firmas competem por preços, mas nenhuma consegue atender individualmente todos os con
- Uma firma adota preço por custo total: cada unidade custa 80 unidades monetárias e a polít
- Um monopólio natural é regulado por método cost-plus, no qual custos reconhecidos e remune
- Uma rede monopolista possui custo marginal de 10 e custo total médio de 20 unidades monetá
- Um regulador precisa recuperar custo fixo comum por tarifas de dois serviços e avalia prec
- Antes de editar regra de interesse geral, uma equipe compara alternativas de não intervenç
- Uma firma dobra trabalho e capital e observa aumento de produção proporcionalmente superio
- Em uma oficina, a produção total passa de 35 para 43 unidades quando se contrata o quarto 
- Uma firma possui custo fixo constante de 100 unidades monetárias no curto prazo. Ao elevar
- Uma firma perfeitamente competitiva apresenta preço superior ao custo variável médio mínim
- Em mercado competitivo com duas empresas, a quantidade ofertada pela indústria a determina
- Em concorrência perfeita com livre entrada, lucro econômico igual a zero no longo prazo si
- Um monopolista de preço único enfrenta demanda descendente e amplia vendas reduzindo o pre
- Em mercado de trabalho com oferta crescente e único empregador relevante, o dispêndio marg
- Firmas em concorrência monopolística vendem variedades diferenciadas e podem manter alguma
- No oligopólio clássico, Cournot trata de escolha simultânea de preços enquanto Bertrand to
- Um cartel coordena produção para tentar aumentar preço conjunto, mas cada participante pod
- A hipótese de demanda quebrada determina matematicamente o nível inicial do preço oligopol
- Uma rede local apresenta custo médio decrescente em toda a faixa relevante de demanda, com
- A Análise de Impacto Regulatório, definida no Decreto nº 10.411/2020, é procedimento reali
- Uma tecnologia apresenta produto inicialmente crescente com adições de trabalho e capital 
- A oferta de longo prazo de uma indústria perfeitamente competitiva é necessariamente horiz
- No monopólio de preço único com máximo interior e demanda regular, a relação de Lerner exp
- Como uma firma possui grande participação de mercado, isso comprova, por si só, formação d
- O reconhecimento de uma externalidade negativa pode justificar examinar intervenção, mas n
- Em monopólio natural, um regime de price cap garante simultaneamente preço igual ao custo 
```

## Pedido 32: Crimes contra a pessoa (`direito-penal.crimes-contra-pessoa`, 261 tópicos de edital)

```
Matéria: direito-penal.crimes-contra-pessoa (Crimes contra a pessoa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Crimes contra a vida: homicídio, feminicídio, vicaricídio, infanticídio e aborto": 20
- "2. Lesões corporais, perigo à vida e à saúde, abandono e rixa": 19
- "3. Crimes contra a honra: calúnia, difamação, injúria e ação penal": 28
- "4. Liberdade individual, ameaça, perseguição e domicílio": 16
- "5. Correspondência, segredos, estado de filiação e critérios de classificação": 17

Já existem (não repita):
- Uma pessoa mata outra dolosamente, sem circunstância qualificadora ou especial descrita. Q
- Após a Lei nº 14.994/2024, alguém mata uma mulher em contexto de violência doméstica e fam
- Durante o parto, uma mãe sob influência do estado puerperal mata seu próprio filho. Qual d
- Uma vítima sofre lesão e fica incapaz de exercer ocupações habituais por 35 dias, sem outr
- Em ocorrência, um golpe causa perda definitiva da função de um membro, sem morte da vítima
- Uma pessoa atribui falsamente a outra a prática de furto ocorrido em data determinada. Qua
- Durante discussão, uma pessoa chama outra de expressão depreciativa à sua dignidade, sem a
- Uma pessoa passa meses enviando mensagens invasivas e seguindo a vítima, perturbando reite
- Alguém entra clandestinamente em quarto de hotel já ocupado, contra vontade do hóspede. Qu
- Uma pessoa abre indevidamente carta fechada dirigida ao vizinho para ler seu conteúdo. Qua
- Para punir homicídio privilegiado por violenta emoção, o agente reagiu uma semana após pro
- Um agressor mata o filho da companheira para puni-la e controlá-la em contexto de violênci
- Uma gestante consente que terceiro provoque aborto fora das hipóteses de não punição previ
- Após agressão dolosa dirigida a ferir, ocorre morte que o agente não quis nem assumiu o ri
- Uma vítima de lesão leve é mulher agredida em contexto doméstico enquadrado na legislação 
- Pessoa encarregada da guarda de incapaz o abandona em local perigoso, deixando-o sem defes
- Um texto público atribui a alguém fato desonroso não criminoso, capaz de atingir sua reput
- O autor de calúnia se retrata cabalmente antes da sentença, nos pressupostos do art. 143. 
- Depois da alteração de 2024, uma pessoa ameaça mulher por razões da condição do sexo femin
- Alguém registra como filho próprio uma criança que efetivamente nasceu de outra pessoa, al
- Uma injúria usa elementos relativos à raça da vítima em fato praticado após a Lei nº 14.53
- Uma difamação é publicada em rede social, com elementos típicos comprovados. Segundo o art
- Uma pessoa intimida sistematicamente outra, de forma repetitiva e intencional, por aplicat
- Profissional revela sem justa causa segredo que conheceu pelo exercício de profissão e cuj
- Uma pessoa comete violação do segredo profissional do art. 154, sem qualquer hipótese espe
- Uma pessoa pratica homicídio qualificado por emboscada. A pena abstrata prevista no art. 1
- Desde a Lei nº 14.994/2024, a morte de mulher por razões da condição do sexo feminino cont
- A lesão que causa debilidade permanente de um sentido corresponde ao art. 129, § 1º; já su
- A simples atribuição de adjetivo humilhante, sem imputação de fato específico, representa 
- A perseguição descrita no art. 147-A pressupõe reiteração de atos e pode ocorrer por meios
- Para configurar a forma de lesão grave por incapacidade para ocupações habituais do art. 1
- Nos crimes de honra, o art. 143 permite retratação cabal antes da sentença em calúnia e di
- O art. 147, § 2º, exige representação em qualquer crime de ameaça, inclusive no caso do § 
- O art. 150 inclui no conceito de casa o aposento ocupado em habitação coletiva, embora a á
- O art. 241 pune registrar como próprio um filho real de outra pessoa, enquanto o art. 242 
- O vicaricídio do art. 121-B, incluído em 2026, exige finalidade específica de causar sofri
- O perdão judicial previsto no art. 121, § 5º, alcança necessariamente homicídio doloso qua
- Pela Lei nº 15.163/2025, o art. 133 do Código Penal passou a prever pena básica de reclusã
- No art. 139, a exceção da verdade pode ser proposta indistintamente em qualquer difamação,
- O art. 140, § 3º, na redação posterior à Lei nº 14.532/2023, cuida de elementos relativos 
- A classificação penal de cyberbullying no art. 146-A pode decorrer de uma única mensagem o
- O art. 148, § 1º, III, estabelece figura mais grave de sequestro ou cárcere privado quando
- Na violação do segredo profissional do art. 154, o fato de o agente conhecer segredo por s
- O art. 154-B estabelece representação como regra para a invasão de dispositivo informático
- O parágrafo único do art. 242 autoriza presumir que qualquer registro de filho alheio como
```

## Pedido 33: Saúde ocupacional e perícia do servidor (`medicina.saude-ocupacional-pericia-servidor`, 261 tópicos de edital)

```
Matéria: medicina.saude-ocupacional-pericia-servidor (Saúde ocupacional e perícia do servidor)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Saúde do trabalhador, vigilância e nexo com o trabalho": 17
- "2. Exames ocupacionais e vigilância periódica de servidores": 19
- "3. LER/DORT, sofrimento mental e dependência no trabalho": 18
- "4. Perícia oficial em saúde, licenças e avaliação da capacidade": 19
- "5. Política institucional de saúde, readaptação e ética médica": 27

Já existem (não repita):
- Um setor passou a registrar vários casos de dor musculoesquelética após aumentar a cadênci
- Uma organização regida pela CLT prevê cinco situações de avaliação médica no seu PCMSO. Qu
- Uma pessoa empregada pela CLT ficou afastada 35 dias por doença comum e retornará às ativi
- Um servidor federal estatutário com 40 anos, sem exposição especial nem condição clínica q
- Um trabalhador relata dor persistente em ombros e mãos, piora durante tarefas repetitivas 
- Uma equipe descreve fadiga extrema associada a estresse crônico de trabalho não gerenciado
- Um servidor federal recebe licença para tratamento da própria saúde após conclusão de perí
- Uma pessoa tem contrato de trabalho formal e precisa distinguir atestado médico assistenci
- Uma comissão pretende instituir política institucional permanente com promoção, prevenção,
- Um servidor com limitação duradoura consegue exercer atribuições de outro cargo afim, comp
- Uma servidora estatutária federal foi convocada para exames periódicos previstos no Decret
- O gestor solicita acesso ao prontuário completo de um servidor para decidir sua lotação, a
- Um servidor acumula períodos de licença de saúde própria que somam 121 dias dentro de 12 m
- Uma servidora federal apresenta atestado de 10 dias, sem outras licenças da mesma espécie 
- Um servidor precisa afastar-se para prestar cuidados diretos e indispensáveis a um familia
- Uma empregada celetista encerra contrato em organização de grau de risco 2 e realizou exam
- Uma pessoa apresenta neuropatia compressiva, tem diabetes e executa movimentos repetitivos
- Ao revisar indicadores de saúde institucional após a Resolução CNJ nº 623/2025, uma equipe
- Um parecer de 2026 cita o Manual de Perícia de 2017 para exigir junta de três peritos e av
- Um servidor federal apresentou licença própria de 8 dias e, seis meses depois, outro atest
- A mãe de uma servidora federal está doente, mas pode receber a assistência necessária fora
- A inspeção médica identifica limitação permanente e recomenda readaptação em atribuições d
- Uma chefia descobre que um servidor está em acompanhamento por uso problemático de álcool 
- Um relatório institucional informa que houve 300 dias de afastamento por doença em certo g
- Dois trabalhadores retornam após 40 dias de afastamento por doença: um é empregado celetis
- Uma equipe modifica postos de trabalho antes de ocorrerem sintomas musculoesqueléticos, re
- Um servidor federal sem risco adicional, com 49 anos, recebe orientação de que o exame méd
- Uma servidora federal atua diretamente com raios X. Para essa exposição, o Decreto nº 6.85
- Um profissional atribui diagnóstico psiquiátrico de depressão maior a toda pessoa que apre
- Um tribunal desenvolve ações integradas de promoção da saúde, prevenção de doenças e vigil
- Em outubro de 2026, o gestor recusa dispensar perícia de uma licença isolada de oito dias,
- Um servidor acumula 120 dias de licença para tratamento da própria saúde no período de 12 
- Para avaliar uma licença prolongada em 2026, o setor afirma que a junta oficial exige obri
- Um servidor autoriza expressamente avaliação pericial por telessaúde nas hipóteses regulam
- Uma empregada celetista retorna após afastamento de 30 dias por acidente não ocupacional. 
- Durante uma avaliação funcional, o médico reserva as informações diagnósticas no prontuári
- Uma servidora federal precisa prestar assistência direta e indispensável a seu pai. Ela po
- Uma inspeção médica identifica limitação persistente e possibilidade de readaptação em car
- Ao comparar empregado celetista e servidor federal estatutário que ficaram afastados por d
- Um relatório institucional distingue dias de licença e número de integrantes ativos para o
- Uma servidora federal soma exatamente 120 dias de licença de saúde própria nos doze meses 
- Um servidor recebe afastamento por 14 dias para tratamento da própria saúde, sem outro afa
- Uma pessoa deixa de autorizar que seu diagnóstico seja especificado no atestado de saúde f
- Um servidor federal requer licença por condição odontológica que compromete sua capacidade
- Após diagnosticar transtorno depressivo em servidor com exposição ocupacional complexa, o 
```

## Pedido 34: Taxas nominal, efetiva, proporcional, equivalente, real e aparente (`matematica-financeira.taxas`, 261 tópicos de edital)

```
Matéria: matematica-financeira.taxas (Taxas nominal, efetiva, proporcional, equivalente, real e aparente)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Capital, montante, regimes de juros e significado das taxas": 14
- "2. Taxas proporcionais, equivalentes e capitais em juros simples": 20
- "3. Conversões nominais e taxas equivalentes no regime composto": 23
- "4. Taxas aparentes e reais, inflação acumulada e poder de compra": 25
- "5. Taxas implícitas, custo efetivo dos fluxos e capitais equivalentes": 18

Já existem (não repita):
- Uma aplicação de R$ 1.000 rende juros simples de 10% ao mês durante dois meses completos. 
- Um capital de R$ 2.000 produziu R$ 60 de juros em um único mês. Qual foi a taxa efetiva ob
- Uma operação anuncia 24% de taxa nominal ao ano, com capitalização mensal. Qual é a taxa e
- Uma taxa de 2% ao mês foi utilizada sob juros simples. Qual é sua taxa anual proporcional 
- Uma aplicação remunera 10% efetivos por semestre em capitalização composta. Qual taxa efet
- Em um ano, o valor monetário de uma aplicação cresceu 20%, enquanto a inflação acumulada f
- Os preços subiram 10% no primeiro mês e 20% no segundo, sem outras variações. Qual é a inf
- Receber R$ 1.210 daqui a dois meses deve ser comparado com um valor presente, a 10% efetiv
- Um investidor entrega R$ 1.000 hoje e recebe R$ 1.210 dois meses depois, sem fluxos interm
- Um contrato anuncia 12% nominais ao ano com capitalização mensal. Qual é a taxa efetiva an
- Uma aplicação oferece 21% efetivos ao ano sob juros compostos. Qual taxa efetiva semestral
- Uma taxa simples de 4% ao mês comercial incide durante 45 dias, adotando expressamente mês
- Um índice de preços subiu 5% e caiu 5% nos dois períodos seguintes. Qual foi a variação ac
- Uma aplicação cresce 3% nominalmente durante o ano e a inflação nesse mesmo ano é de 8%. Q
- Um credor receberá R$ 500 daqui a um mês e R$ 605 daqui a dois meses. À taxa composta de 1
- R$ 1.500 transformaram-se em R$ 1.815 após dois meses em capitalização composta. Qual taxa
- Uma operação anuncia 12% nominais ao semestre e capitaliza juros em seis períodos mensais 
- Uma aplicação rende 2% efetivos ao mês durante seis meses consecutivos; a inflação acumula
- Uma quantia aumentou 12% durante o ano, enquanto o índice de preços correspondente subiu 5
- Uma operação anuncia 20% nominais ao ano com capitalização semestral. No mesmo ano, a infl
- Um recebível paga R$ 660 em um mês e R$ 605 em dois meses. À taxa de 10% mensais compostos
- Uma dívida inicial de R$ 1.000 sofre capitalização composta de 2% ao mês por três meses, s
- Um contrato proporciona 21% efetivos em um ano. Se fosse cotado em taxa nominal anual com 
- Um empréstimo de R$ 1.000 será liquidado por R$ 1.210 após dois meses. Qual par de taxas m
- Um capital deve render 5% reais durante um ano em que a inflação acumulada foi de 8%. Qual
- Um capital de R$ 500 aplicado a juros simples de 4% ao mês durante dois meses produz monta
- Uma taxa simples de 12% ao ano é proporcional e equivalente à taxa simples de 2% ao mês du
- A taxa nominal anual de 24% com capitalização mensal corresponde à aplicação de 2% em cada
- Uma inflação de 10% seguida de outra de 10% resulta em 20% acumulados, já que índices de p
- Um valor de R$ 1.210 a receber em dois meses equivale a R$ 1.000 hoje quando a taxa de cap
- Se uma aplicação valoriza 8% e os preços sobem também 8% no mesmo intervalo, a taxa de ren
- Uma taxa efetiva anual de 21% sob juros compostos é equivalente à taxa efetiva semestral d
- Uma taxa de 2% efetivos mensais é equivalente a 24% efetivos anuais em capitalização compo
- Com taxa simples de 5% ao mês, o valor presente racional de R$ 550 recebíveis após dois me
- Se o índice geral de preços sobe 5% em um mês e diminui 5% no seguinte, a variação acumula
- Uma operação prevê R$ 1.100 após um mês e R$ 605 após dois meses. À taxa composta de 10% a
- Uma aplicação com retorno de 3% ao mês, durante um trimestre, pode ter sua taxa real trime
- Se uma aplicação sofre perda monetária de 2% enquanto o índice de preços cai 5% no mesmo p
- Uma taxa de 12% ao ano explicitamente efetiva corresponde a 1% efetivo ao mês no regime co
- Uma taxa anual efetiva de 21% pode ser cotada como taxa nominal anual de 20% capitalizada 
- Na equivalência racional por juros simples, transportar uma quantia em duas etapas sucessi
- Um número-índice de preços passou de 200 para 242 no período selecionado. A inflação acumu
- Duas operações que exibem 12% ao ano no contrato possuem obrigatoriamente idêntica taxa ef
- Se a aplicação acumula 21% monetários em dois meses e o índice de preços sobe 10% no mesmo
- Em um empréstimo com uma parcela a vencer em um mês e outra em dois meses, a taxa mensal i
```

## Pedido 35: Povos Indígenas, Quilombolas e Comunidades Tradicionais: Direitos Territoriais e Socioambientais (`direito-ambiental.povos-comunidades-tradicionais`, 259 tópicos de edital)

```
Matéria: direito-ambiental.povos-comunidades-tradicionais (Povos Indígenas, Quilombolas e Comunidades Tradicionais: Direitos Territoriais e Socioambientais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Base constitucional: terras indígenas e quilombolas": 25
- "2. Convenção 169 da OIT: autoidentificação e consulta": 25
- "3. Política Nacional dos Povos e Comunidades Tradicionais e quilombolas": 25
- "4. Gestão territorial indígena, unidades de conservação e conhecimentos tradicionais": 25

Já existem (não repita):
- Cabe à União, segundo o art. 231 da Constituição, em relação às terras tradicionalmente oc
- O STF, ao julgar a ADI 3.239, decidiu que o Decreto 4.887/2003, sobre a titulação de terra
- A Convenção 169 da OIT trata dos direitos de quais povos?
- Segundo o art. 231 da Constituição, as terras tradicionalmente ocupadas pelos índios são b
- O aproveitamento dos recursos hídricos, incluídos os potenciais energéticos, e a lavra de 
- É vedada a remoção dos grupos indígenas de suas terras, salvo, ad referendum do Congresso 
- As terras tradicionalmente ocupadas pelos índios podem ser alienadas pela União mediante a
- Segundo o art. 232 da Constituição, os índios, suas comunidades e organizações:
- Segundo o art. 68 do ADCT, aos remanescentes das comunidades dos quilombos que estejam ocu
- A Constituição determina o tombamento de todos os documentos e sítios detentores de remini
- Pela Constituição, as terras tradicionalmente ocupadas pelos índios incluem as imprescindí
- Na Convenção 169 da OIT, o critério fundamental para determinar os grupos aos quais ela se
- A consulta prevista na Convenção 169 da OIT deve ser realizada:
- A consulta prévia da Convenção 169 equivale a uma audiência pública realizada após a aprov
- O texto da Convenção 169 da OIT encontra-se hoje consolidado, no Brasil, no Decreto 10.088
- Pela Convenção 169, os povos interessados têm o direito de decidir suas próprias prioridad
- Quanto ao traslado de povos de suas terras, a Convenção 169 da OIT estabelece que ele:
- A Convenção 169 assegura aos povos o direito de participar da utilização, administração e 
- Segundo o Decreto 6.040/2007, povos e comunidades tradicionais são:
- Pelo Decreto 6.040/2007, territórios tradicionais são os espaços necessários à reprodução 
- A coordenação da implementação da Política Nacional de Desenvolvimento Sustentável dos Pov
- Entre os objetivos específicos da PNPCT está:
- Entre os princípios da PNPCT estão a segurança alimentar e nutricional, o acesso em lingua
- O objetivo geral da PNPCT é promover o desenvolvimento sustentável dos povos e comunidades
- Pelo Decreto 4.887/2003, consideram-se remanescentes das comunidades dos quilombos os grup
- A autodefinição das comunidades quilombolas prevista no Decreto 4.887/2003 é inscrita no C
- Compete ao INCRA, no âmbito federal, a identificação, o reconhecimento, a delimitação, a d
- No procedimento de titulação quilombola, para a medição e demarcação das terras:
- O título concedido às comunidades quilombolas pelo Decreto 4.887/2003 é individual e pode 
- Concluídos os trabalhos de campo de identificação e delimitação das terras quilombolas, o 
- A Política Nacional de Gestão Territorial e Ambiental de Terras Indígenas (PNGATI) utiliza
- A PNGATI busca garantir e promover a proteção, a recuperação, a conservação e o uso susten
- As Reservas Extrativistas e as Reservas de Desenvolvimento Sustentável são categorias de u
- O acesso a conhecimento tradicional associado à biodiversidade dispensa o consentimento da
- As políticas de promoção das cadeias da sociobiodiversidade têm como finalidade principal:
- Os direitos dos índios sobre as terras que tradicionalmente ocupam são qualificados pela C
- No procedimento do Decreto 4.887/2003, fica assegurada aos remanescentes das comunidades d
- O Bolsa Verde foi um programa de apoio à conservação ambiental que transferiu recursos a f
- A PNGATI prevê a participação dos povos indígenas na gestão de áreas em que há sobreposiçã
- Segundo a Constituição, são nulos e extintos, não produzindo efeitos jurídicos, os atos qu
```

## Pedido 36: Psicologia do desenvolvimento e da aprendizagem (`psicologia.desenvolvimento-aprendizagem`, 259 tópicos de edital)

```
Matéria: psicologia.desenvolvimento-aprendizagem (Psicologia do desenvolvimento e da aprendizagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Desenvolvimento humano: dimensões biológica, psicológica e social": 22
- "2. Piaget: construção do conhecimento e desenvolvimento cognitivo": 20
- "3. Vygotsky: mediação cultural, linguagem e aprendizagem": 20
- "4. Wallon: afetividade, motricidade e psicogênese da pessoa completa": 19
- "5. Relações desenvolvimento-aprendizagem, dificuldades e avaliação educacional": 19

Já existem (não repita):
- Uma criança cresceu quatro centímetros, passou a controlar melhor a postura e aprendeu a l
- Depois de aprender a pegar uma bola pequena, um bebê usa inicialmente a mesma forma de pre
- Ao tentar pegar uma peça achatada, uma criança descobre que o gesto usado para bolas não f
- Após ver a mesma água passar de um copo baixo para outro alto e estreito, uma criança afir
- Uma equipe planeja atividade que integra expressão corporal, emoções, pensamento e constru
- Um aluno não resolve sozinho um problema com duas etapas, mas chega à solução após pergunt
- Durante avaliação, uma estudante resolve sem dicas exercícios de soma de uma etapa. Segund
- Uma criança usa uma caixa de papelão como se fosse um automóvel e ainda não compreende con
- Um bebê expressa desconforto por choro, mudança de tônus e gestos, mobilizando resposta de
- Ao acompanhar dificuldades com frações, uma pessoa avalia o que cada estudante faz sozinho
- Uma criança afirma que a massa da argila continua igual depois de ser alongada, pois seria
- Uma criança primeiro organiza sua atividade por instruções em diálogo, depois verbaliza os
- Uma criança de quatro anos passa a dizer 'eu faço sozinho', contesta algumas solicitações 
- Dois estudantes têm a mesma idade, mas um controla melhor movimentos finos enquanto o outr
- Durante aula de matemática, o estudante percebe a cor dos materiais manipulados e também c
- Em atividade motora cooperativa, uma criança explora objetos com o corpo; mais tarde, em o
- Um aluno usa a palavra 'energia' em conversas cotidianas; após ensino, passa a relacioná-l
- Em uma tarefa, o estudante resolve sozinho adições simples; com pistas, resolve um problem
- Uma criança conserva quantidade de objetos ao mudar sua disposição, mas ainda responde pel
- Uma adolescente resolve argumentos abstratos em conteúdos familiares, mas tem dificuldade 
- Um estudante apresenta baixo desempenho em leitura após interrupções prolongadas de escola
- Um estudante usa marcas feitas em papel para organizar a memória de uma lista; outro utili
- Em um grupo, uma criança vivencia forte reação emocional diante de disputa por reconhecime
- Uma criança consegue conservar número, porém não explica de maneira adequada propriedades 
- Um aluno compara frações apenas pelos denominadores e recebe o comentário 'você não nasceu
- Durante reunião, afirma-se que o simples aumento da estatura de uma criança demonstra, por
- Ao ver um bebê procurar um brinquedo que foi ocultado, uma observadora pode relacionar a a
- Uma criança resolve um cálculo somente quando recebe orientações. Pela abordagem de Vygots
- Uma criança de aproximadamente quatro anos insiste em escolher suas roupas e busca reconhe
- Se uma criança não alcança um marco de desenvolvimento listado em um checklist em uma dete
- Numa brincadeira de faz de conta, uma criança atribui a uma caixa a função de carro. A sit
- Um estudante começa a usar um sistema de marcas para memorizar tarefas. Na interpretação h
- Em uma atividade, uma criança adapta a forma de agarrar uma peça após perceber que o movim
- Uma equipe reconhece que uma criança consegue ordenar bastões sem ajuda. De acordo com Vyg
- Uma criança conserva o número de peças após seu espaçamento mudar e justifica que nenhuma 
- Ao acompanhar uma turma, a equipe conclui que a teoria de Wallon reduz a afetividade às em
- Diante de leitura insuficiente após escolarização irregular, a equipe analisa oportunidade
- Um docente considera que, como os estágios de Piaget possuem idades aproximadas, toda cria
- Uma adolescente demonstra raciocínio abstrato em física, mas reage impulsivamente a confli
- Um professor oferece perguntas graduadas para que um aluno resolva um problema. Como o ter
- Uma criança conserva número ao comparar fileiras, mas ainda se concentra na altura de reci
- Uma criança usa fala privada para orientar seus passos ao montar um quebra-cabeça. Segundo
- Em perspectiva walloniana, a passagem da exploração motora para um momento de afirmação pe
- Como uma criança reproduziu corretamente com pistas uma explicação sobre frações, sua apre
- Numa atividade de Educação Infantil, interações e brincadeiras podem ser organizadas inten
```

## Pedido 37: Ação popular, mandado de injunção e habeas data (`processo-civil.acao-popular-injuncao-habeas-data`, 258 tópicos de edital)

```
Matéria: processo-civil.acao-popular-injuncao-habeas-data (Ação popular, mandado de injunção e habeas data)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ação popular: fundamentos, legitimidade e nulidades": 23
- "2. Ação popular: réus, procedimento, sentença e recursos": 23
- "3. Mandado de injunção: cabimento, legitimidade e procedimento": 16
- "4. Mandado de injunção: decisão, eficácia e norma superveniente": 16
- "5. Habeas data: via administrativa, procedimento e recursos": 22

Já existem (não repita):
- Uma pessoa com título eleitoral deseja invalidar ato de alienação de bem público por preju
- Uma associação constituída regularmente pretende propor, em nome próprio, ação popular par
- Uma autoridade pratica ato usando competência válida para obter vantagem estranha ao inter
- Uma entidade cujo ato é impugnado em ação popular avalia que apoiar o cidadão serve ao int
- Uma ação popular é julgada improcedente por deficiência das provas apresentadas. O que per
- O prazo prescricional expressamente previsto para a ação popular pela Lei 4.717/1965 é de 
- Uma pessoa não consegue exercer liberdade constitucional pela falta total de norma regulam
- Uma pessoa deseja conhecer dados próprios registrados em banco governamental. Qual instrum
- O autor popular desiste da demanda, e o edital previsto no art. 9º é publicado. Qual oport
- Na ação popular, uma sentença rejeita o pedido e outra, em processo distinto, acolhe a pre
- Uma pessoa ajuíza MI porque foi editada regulamentação insuficiente, que continua impedind
- Em MI individual, o titular do direito indica como impetrado aquele que editou decisão con
- Um relator indefere inicialmente um MI por considerá-lo manifestamente incabível. O intere
- Após a notificação no MI, decorre prazo de informações e os autos seguem para manifestação
- Um pedido coletivo de MI foi apresentado por associação legalmente constituída e em funcio
- O titular de registros pessoais pede acesso administrativo e não recebe decisão depois de 
- O interessado solicitou retificação de dado próprio incorreto e não obteve decisão adminis
- Um registro contém dado verdadeiro a respeito do impetrante, mas há pendência judicial jus
- Em MI já julgado procedente, comprova-se que, em MI anterior, o responsável não cumpriu pr
- Uma decisão de MI transitou em julgado e posteriormente surgiu norma regulamentadora favor
- Uma pessoa ajuizou MI individual e, depois, tomou ciência comprovada de MI coletivo sobre 
- Uma sentença concede habeas data para retificação de informação pessoal e a parte contrári
- A sentença condenatória de segunda instância em ação popular foi publicada e ninguém promo
- Um pedido administrativo de conhecimento de dados pessoais foi apresentado ao depositário.
- Uma sentença de MI individual estabelece condições de exercício de direito constitucional 
- Um cidadão pretende ajuizar ação popular para impedir ato lesivo ao patrimônio histórico e
- Uma sociedade empresária pode ajuizar ação popular desde que demonstre recolhimento regula
- No art. 2º da Lei 4.717/1965, desvio de finalidade consiste na prática do ato para alcança
- O Ministério Público, ao acompanhar ação popular, pode assumir a defesa jurídica do ato im
- A sentença que julga ação popular improcedente por deficiência de prova não impede que out
- Mandado de injunção pode ser concedido por simples discordância com a aplicação de lei suf
- O habeas data pode assegurar o acesso a informações relativas à própria pessoa existentes 
- Se a pessoa solicitou administrativamente apenas retificação de informação pessoal e o dep
- Na ação popular, o prazo de contestação é de vinte dias, passível de prorrogação por mais 
- No mandado de injunção, a publicação de qualquer norma regulamentadora, ainda que insufici
- O MI coletivo pode ser promovido por associação legalmente constituída há pelo menos um an
- A sentença que concede habeas data está sujeita, pela Lei 9.507/1997, a apelação necessari
- Na ação popular, sentença de improcedência ou carência está sujeita ao duplo grau obrigató
- Em MI individual, a decisão terá obrigatoriamente eficácia erga omnes, pois o reconhecimen
- Se a norma regulamentadora for editada antes da decisão no MI, a Lei 13.300/2016 prevê pre
- Depois de comprovar que o impetrado desrespeitou prazo fixado em mandado de injunção anter
- O mandado de injunção coletivo não induz litispendência em relação ao individual, mas impe
- A gratuidade do habeas data depende de comprovação de ausência de má-fé do impetrante, exa
- O habeas data possui prioridade legal sobre atos judiciais, excetuados habeas corpus e man
- A pessoa que requer habeas data para acesso a dados próprios não precisa comprovar solicit
```

## Pedido 38: Gerência e monitoramento de redes e logs (`informatica.gerencia-monitoramento-redes`, 257 tópicos de edital)

```
Matéria: informatica.gerencia-monitoramento-redes (Gerência e monitoramento de redes e logs)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de gerência e SNMP": 22
- "2. RMON, tráfego e qualidade de serviço": 23
- "3. Logs, syslog, correlação e SIEM": 21
- "4. Zabbix, Prometheus e Grafana na operação": 17
- "5. Elastic Stack, Fluentd e arquitetura de monitoramento": 17

Já existem (não repita):
- Um administrador precisa consultar remotamente o contador de octetos de uma interface de r
- Uma equipe abre regras de firewall para consultas SNMP tradicionais ao agente e para notif
- Um switch envia notícia de falha de enlace ao gerenciador. O administrador deseja distingu
- Uma sonda de monitoramento deve guardar amostras periódicas de tráfego para comparar a uti
- Uma interface de 100 Mbit/s transmite média de 80 Mbit/s na janela de monitoramento. Qual 
- Um coletor syslog recebe evento com facility 4 e severity 3. Conforme a RFC 5424, qual val
- Em servidor monitorado por Zabbix, configura-se a coleta do uso de disco e uma expressão q
- O Prometheus registra uma métrica `http_requests_total` instrumentalizada como contador. Q
- Uma equipe instala Logstash, Elasticsearch e Kibana para analisar logs de aplicação. Qual 
- Um equipamento trabalha com SNMPv3 e deve proteger a origem e a confidencialidade dos valo
- Um contador SNMP cresce de 1.000.000 para 4.000.000 de octetos em 30 segundos, sem reset. 
- Uma sonda RMON recebe configuração para verificar quando a quantidade de erros ultrapassa 
- Um operador pretende limitar rajadas de tráfego sem descartá-las imediatamente, utilizando
- Em pipeline de syslog, o administrador verifica que logs com severity 0 representam evento
- Uma equipe configura regra para correlacionar falhas seguidas de login bem-sucedido, usand
- Uma rede não permite novas conexões de entrada em servidores monitorados. O administrador 
- Num servidor de logs, a configuração Logstash recebe registros de arquivo, extrai campos p
- Durante marcação de tráfego IP, um engenheiro precisa separar bits de classificação DiffSe
- Ao calcular velocidade por contador de octetos de 32 bits, uma equipe observa que a amostr
- Uma mensagem syslog tem facility 16 (local0) e severity 4 (warning). O analista deve regis
- Um SIEM correlaciona logs de dois servidores. Um deles está cinco minutos adiantado e outr
- Em Prometheus, uma métrica counter sobe 3.000 unidades no período de cinco minutos, sem re
- A aplicação envia logs diretamente para um ingest pipeline do Elasticsearch, sem processo 
- Um Fluentd acompanha arquivo de log por `in_tail` e salva `pos_file`. Depois de reiniciar 
- Uma equipe reúne syslog de firewall, fluxos IPFIX, métricas Prometheus e gráficos Grafana 
- Em gerenciamento SNMP, a MIB organiza objetos que podem ser identificados hierarquicamente
- A porta UDP 162 é a convencional para requisições GetRequest ao agente SNMP, enquanto a UD
- Na RFC 2819, o grupo RMON history registra amostras periódicas de variáveis, permitindo ao
- Um registro de fluxo IPFIX é uma cópia obrigatoriamente integral do conteúdo de todos os p
- Em syslog RFC 5424, facility igual a 3 e severity igual a 4 produzem PRI 28, pois o cálcul
- Uma configuração Logstash válida precisa obrigatoriamente de estágios input, filter e outp
- O nível `authPriv` do SNMPv3, quando corretamente configurado, combina autenticação e prot
- O recebimento de uma Trap SNMP garante, por definição, que houve resposta de confirmação d
- Um dispositivo monitorado com RMON pode utilizar grupo alarm para detectar cruzamento de l
- Marcar pacotes com um DSCP correspondente a EF obriga qualquer roteador da Internet a rese
- Em uma configuração Zabbix de verificação passiva, o servidor ou proxy inicia a requisição
- A expressão `rate(http_requests_total[5m])` calcula diretamente o número total de requisiç
- No Fluentd, o `pos_file` do plugin `in_tail` pode registrar a posição de leitura para perm
- O shaping trata tráfego excedente descartando-o de imediato, enquanto o policing o coloca 
- Uma mensagem syslog conforme RFC 5424 pode ser transportada por mecanismos diferentes, sen
- Se uma regra SIEM detecta vinte falhas de login de uma origem IP compartilhada por NAT, el
- No Prometheus, atribuir um label exclusivo para cada requisição HTTP pode elevar drasticam
- A existência de Elasticsearch e Kibana instalados, com painéis de eventos, comprova automa
- Quando um alerta Prometheus usa `for: 10m`, a condição normalmente precisa permanecer ativ
- A aplicação de `GetBulkRequest` em SNMPv1 oferece recuperação em lote idêntica à do SNMPv2
```

## Pedido 39: Juizados Especiais Criminais (`legislacao-penal-especial.juizados-especiais-criminais`, 257 tópicos de edital)

```
Matéria: legislacao-penal-especial.juizados-especiais-criminais (Juizados Especiais Criminais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Competência, infrações de menor potencial ofensivo e princípios": 17
- "2. Termo circunstanciado, audiência preliminar e composição civil": 25
- "3. Transação penal e procedimento sumaríssimo": 18
- "4. Suspensão condicional do processo e limites da justiça consensual": 17
- "5. Juizados federais criminais, execução e integração das regras": 23

Já existem (não repita):
- Em ocorrência envolvendo crime cuja pena abstrata é de seis meses a dois anos, cumulada co
- Uma contravenção penal foi comunicada em localidade com juizado competente. Qual disposiçã
- A autoridade policial registra fato que, em princípio, constitui infração de menor potenci
- Após lavratura de TCO, a pessoa apontada como autora assume compromisso de comparecer ao j
- Em ação penal privada, o autor do fato e a vítima fazem composição dos danos civis, reduzi
- Após tentativa de composição civil frustrada, a vítima de crime de ação pública condiciona
- Em fato de ação pública incondicionada, o Ministério Público entende cabível proposta de t
- A pessoa cumpriu transação penal homologada e consulta se a medida a torna reincidente por
- Um processo criminal de rito sumaríssimo chega à audiência de instrução. Antes de decidir 
- Um acusado em processo sumaríssimo deseja recorrer de sentença condenatória. Qual prazo e 
- Após sentença no rito dos juizados criminais, a defesa aponta contradição no julgado. Qual
- Pessoa responde por crime cuja pena abstrata vai de um a quatro anos. Sem impedimentos pes
- O Ministério Público propõe suspensão condicional do processo a pessoa sem impedimentos le
- Durante o prazo da suspensão condicional do processo, o beneficiário é processado por cont
- Uma infração de menor potencial ofensivo é conexa a delito processado no juízo comum, have
- Ação penal por lesão corporal leve praticada em violência doméstica contra mulher é aprese
- No rito sumaríssimo, a defesa precisa requerer intimação de testemunha que não levará espo
- A transação penal homologada deixou de ser cumprida. O órgão acusador pretende dar continu
- Uma pessoa responde por crimes em concurso material, cada um com pena mínima de oito meses
- Uma acusação inicialmente formulada por delito com pena mínima superior a um ano é parcial
- Crime de competência federal tem pena máxima de dois anos; não há conexão, regra especial 
- Em processo sumaríssimo, o acusado não foi encontrado para citação pessoal. A parte intere
- A suspensão condicional do processo transcorreu por dois anos. Durante a prova, o benefici
- A ação penal pública incondicionada versa sobre delito de menor potencial ofensivo, e as p
- Uma infração é descrita como menor potencial ofensivo, mas o processo tramita na Justiça M
- Um crime tem pena máxima abstrata de dois anos e pena de multa cumulativa. Na ausência de 
- Uma ocorrência de menor potencial ofensivo foi registrada. Pelo art. 69, o termo circunsta
- Uma vítima de delito de ação penal privada celebra composição civil escrita, posteriorment
- Uma transação penal homologada impõe pena restritiva de direitos. Segundo o art. 76, § 4º,
- Um acusado recebe proposta de suspensão condicional do processo por crime com pena mínima 
- Um crime de pena máxima de dois anos pertence à competência estadual, mas deve tramitar no
- Um fato inicialmente apurado no juizado foi reunido, por conexão, a processo do juízo comu
- Em ação pública condicionada, a vítima não representou durante audiência preliminar; por f
- A pessoa obteve transação penal há três anos e pretende receber outra pelo art. 76. O bene
- Durante suspensão condicional do processo, o beneficiário passou a responder por contraven
- Após sentença do juizado criminal, a defesa opôs embargos de declaração cabíveis em cinco 
- Uma autoridade encaminha TCO ao juizado e a ocorrência envolve lesão corporal contra mulhe
- Uma imputação inicialmente incompatível com o art. 89 foi desclassificada, e a nova pena m
- Uma transação penal homologada foi descumprida parcialmente. A Súmula Vinculante 35 impede
- Em concurso material com dois crimes de pena mínima de oito meses cada, a suspensão condic
- A ação pública incondicionada por infração de menor potencial ofensivo recebeu composição 
- Acusado não encontrado para citação pessoal no juizado criminal terá as peças encaminhadas
- Uma pessoa cumpriu integralmente o período de prova da suspensão condicional do processo s
- Na Justiça Federal, o art. 2º da Lei 10.259/2001 manda respeitar conexão e continência; na
- Uma pessoa envolvida em infração de menor potencial ofensivo perante a Justiça Militar pod
```

## Pedido 40: Orientação a objetos (`informatica.orientacao-objetos`, 257 tópicos de edital)

```
Matéria: informatica.orientacao-objetos (Orientação a objetos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Objetos, quatro pilares e contraste entre paradigmas": 28
- "2. Relacionamentos, composição, coesão, acoplamento e SOLID": 23
- "3. Construção, identidade, Object e contratos de igualdade": 15
- "4. Sobrescrita, sobrecarga, resolução estática e conversões de referência": 19
- "5. Interfaces, classes abstratas, métodos default e projeto consistente": 15

Já existem (não repita):
- Uma classe Conta mantém saldo e disponibiliza depositar e sacar, impedindo que clientes at
- Considere `Animal a = new Gato();` e método de instância `emitirSom()` sobrescrito por Gat
- Um programa de processamento de contas é escrito em C por funções e estruturas; outro, em 
- Um Pedido contém linhas que só possuem significado naquele pedido e são removidas com ele,
- Uma classe implementa cálculo de tributo, desenho de telas e conexão de dispositivos sem r
- Em Java, dois objetos diferentes do tipo Produto têm mesmo identificador, e a classe sobre
- Um método tem versões `buscar(Object)` e `buscar(String)`. Uma variável `Object valor` con
- Um código executa `Animal a = new Cachorro();` e depois tenta `Gato g = (Gato) a;`, sendo 
- Duas interfaces independentes declaram `default String nome()` com implementações diferent
- Uma subclasse sobrescreve `public Number obter()` de sua superclasse. Qual mudança isolada
- Uma classe Base possui apenas construtor `Base(int codigo)`. Uma classe Filha estende Base
- Uma equipe utiliza Java 25. O construtor de uma subclasse valida parâmetro antes de `super
- Em um cadastro, um método `public boolean equals(Pessoa p)` foi criado, mas `equals(Object
- Um serviço de alto nível instancia diretamente `ArquivoLocal` para salvar dados, tornando 
- Uma interface reúne imprimir, digitalizar e transmitirFax. Uma impressora simples precisa 
- Considere Base com campo `String nome="base"` e método `String get(){return "base";}`. Fil
- Uma interface Conversor define `static String versao()` e `default String converter()`. A 
- Uma subclasse ContaRestrita substitui sacar(valor), permitido pelo contrato de Conta para 
- A classe Base possui `static String id(){return "B";}` e `String f(){return "B";}`; Filha 
- Considere `class A { String f(Object x){return "Aobj";} }` e `class B extends A { String f
- Uma superclasse chama em seu construtor o método de instância `mostrar()`, sobrescrito pel
- Uma superclasse define `public String identificar()` retornando "classe". Uma interface im
- A variável `Object ref=null;` é testada por `ref instanceof String`. Em seguida, o program
- Uma interface exige métodos salvar e excluir, e uma classe abstrata a implementa oferecend
- Duas referências `x` e `y` representam pessoas diferentes e, após comparação lógica, seus 
- Dois objetos do mesmo tipo podem possuir os mesmos valores de campos e ainda assim represe
- A programação procedural depende necessariamente de estado global, porque funções com parâ
- Em um modelo no qual uma equipe mantém membros que continuam existindo independentemente d
- Uma classe só está adequadamente encapsulada quando todos os seus atributos são private e 
- No Java, é possível que uma classe concreta implemente várias interfaces, embora possa ter
- Métodos `static` declarados em uma classe-base são sobrescritos virtualmente quando a subc
- Uma classe que sobrescreve `equals` para considerar duas instâncias iguais deve garantir q
- A atribuição `Animal ref = new Gato()` altera a classe efetiva da instância para Animal, d
- Uma classe abstrata pode conter métodos concretos e campos de instância, mas não pode ser 
- Uma implementação de subclasse pode reduzir método herdado `public void executar()` para `
- Um módulo que recebe uma interface de armazenamento e pode trabalhar com implementações lo
- Em Java 25, a presença de qualquer instrução antes de `super(...)` explícito, inclusive va
- Um método `default` de interface fornece implementação de instância que uma classe pode he
- Se `Base x = new Sub()` e Base expõe somente `f(Object)`, enquanto Sub adiciona `f(String)
- Quando o construtor de uma superclasse invoca método de instância sobrescrito, é possível 
- Se duas interfaces independentes fornecem implementações `default` equivalentes, a classe 
- Se um contrato de superclasse admite operação para certo conjunto de entradas, uma subclas
- A anotação `@Override` em `boolean equals(MinhaClasse outra)` funciona normalmente para so
- Quando uma superclasse oferece implementação concreta pública de `processar()` e uma inter
- O princípio de inversão de dependências exige que toda classe Java seja declarada abstrata
```

## Pedido 41: Processo judicial tributário e execução fiscal (`direito-tributario.processo-judicial-execucao-fiscal`, 254 tópicos de edital)

```
Matéria: direito-tributario.processo-judicial-execucao-fiscal (Processo judicial tributário e execução fiscal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e vias de solução da lide tributária": 18
- "2. Execução fiscal: título, partes, citação e garantias": 20
- "3. Defesa, prescrição intercorrente e cobrança judicial": 20
- "4. Ações tributárias do contribuinte e mandado de segurança": 25
- "5. Cautelar fiscal, custas federais e execução coletiva": 17

Já existem (não repita):
- Um município pretende cobrar judicialmente IPTU inscrito em dívida ativa e consulta qual d
- Uma autarquia busca executar multa administrativa validamente inscrita em dívida ativa, em
- Ao receber a CDA, uma contribuinte apresenta documentos que mostram erro no valor exigido.
- Uma empresa é citada regularmente na execução fiscal e deseja pagar ou garantir o crédito 
- Citado em execução fiscal, um devedor pretende garantir o juízo sem entregar dinheiro. Qua
- Uma pessoa executada realizou depósito judicial integral e deseja apresentar embargos fisc
- Um executado comprova prescrição pelas datas constantes dos autos, sem necessidade de ouvi
- Antes de sofrer lançamento concreto, uma empresa pretende que o juiz reconheça não haver r
- Uma pessoa recolheu tributo em valor superior ao devido por erro no cálculo e pretende rec
- Uma contribuinte foi cientificada de ato fiscal concreto ilegal, demonstrável por document
- Antes da sentença de embargos, a Fazenda identifica erro de digitação na CDA, mas não dese
- Em execução fiscal, a alegação de excesso depende de perícia e exame de documentos contábe
- Em execução fiscal, a Fazenda recebe informação de que não foram encontrados devedor nem b
- Dois entes públicos exigem tributo idêntico da mesma pessoa sobre um único fato gerador, e
- A parte quer ajuizar ação anulatória de lançamento, mas o recebimento da petição foi condi
- Uma cautelar fiscal preparatória foi concedida e a exigência tornou-se irrecorrível admini
- Em outubro de 2026, uma parte calcula custas federais utilizando os novos anexos da Lei nº
- Para suspender exigibilidade de crédito tributário sem decisão liminar, a empresa oferece 
- O contribuinte requereu restituição administrativamente e recebeu decisão definitiva que a
- Após LC nº 236/2026, a Fazenda protesta extrajudicialmente uma CDA de crédito tributário d
- Uma associação venceu ação civil pública e, após sessenta dias do trânsito em julgado, não
- Em execução fiscal, a Fazenda pretende substituir CDA para modificar uma pessoa jurídica d
- Uma empresa requer mandado de segurança para declarar seu direito de compensar tributo, ma
- Um município ajuizou execução de valor diminuto sem tentativa de conciliação e sem protest
- Uma execução fiscal ficou sem bens encontrados. A Fazenda protocolou pesquisas patrimoniai
- Uma autarquia ajuizou execução de multa administrativa inscrita em dívida ativa. A LEF é a
- Uma certidão de dívida ativa descreve tributo e valor inscrito. Por gozar de presunção abs
- Uma pessoa executada oferece seguro-garantia judicial compatível com a dívida e os encargo
- Uma empresa é citada na execução fiscal e calcula trinta dias corridos para pagar ou garan
- Duas pessoas jurídicas de direito público exigem tributo idêntico sobre mesmo fato gerador
- Uma pessoa utiliza mandado de segurança para cobrar diretamente valores de tributo indevid
- Uma fundação pública contemplada pelo art. 4º, I, da Lei nº 9.289/1996 é isenta de custas 
- Uma pessoa deve tributo há poucos dias, sem qualquer conduta de risco apontada. Esse atras
- Uma associação autora de ação civil pública não promoveu execução da sentença condenatória
- Um contribuinte ajuíza ação declaratória sem pedir liminar e sem realizar depósito. O ajui
- Antes de sentença de embargos, a Fazenda corrige erro material da CDA, sem mudar o sujeito
- O contribuinte recebeu negativa administrativa do pedido de restituição. O prazo do art. 1
- Após a LC nº 236/2026, o contribuinte que ajuíza ação judicial sobre o mesmo objeto de rec
- Após não localizar bens em execução, a Fazenda é cientificada da diligência infrutífera, m
- Em restituição de tributo cuja natureza comporta transferência do encargo financeiro, a pe
- Uma dívida ativa tributária foi inscrita há poucos dias. Em razão do art. 2º, § 3º, da LEF
- Em outubro de 2026, o cálculo das custas federais não deve antecipar os novos anexos subst
- A medida cautelar fiscal exige constituição prévia do crédito tributário em qualquer circu
- Um contribuinte prova documentalmente seu direito à declaração de compensação tributária p
- Depois de expirado o ano de suspensão de execução fiscal tributária sem bens, passam a cor
```

## Pedido 42: LINDB na Administração Pública (arts. 20 a 30) (`direito-administrativo.lindb-administracao-publica`, 254 tópicos de edital)

```
Matéria: direito-administrativo.lindb-administracao-publica (LINDB na Administração Pública (arts. 20 a 30))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Decisão responsável, consequências práticas e invalidação": 20
- "2. Realidade do gestor, transição e proteção da confiança": 21
- "3. Compromissos, compensação, consulta e estabilidade administrativa": 19
- "4. Responsabilidade pessoal, pareceres e erro grosseiro": 20
- "5. Aplicação integrada no controle e precedentes administrativos": 20

Já existem (não repita):
- Uma decisão de desativar imediatamente um serviço público afirma apenas que a medida prote
- Ao invalidar contrato que sustenta serviço contínuo, a autoridade reconhece o vício, mas n
- Um processo examina atraso em atendimento ocorrido em região isolada, com escassez documen
- Uma interpretação administrativa inédita de cláusula indeterminada impõe adaptação técnica
- Um ato foi concluído sob entendimento geral amplamente adotado. Anos depois, nova orientaç
- Uma autoridade firma compromisso do art. 26 após ouvir o setor jurídico, mas ainda não pub
- Em processo administrativo, uma decisão pretende impor compensação por benefício indevido 
- Antes de editar regulamento externo, uma autoridade cogita consulta pública pelo art. 29 d
- Uma entidade publica súmula administrativa dirigida às suas unidades. Pelo art. 30, o ente
- Ao aplicar sanção por falha de gestão, a decisão ignora penalidade anterior da mesma natur
- Uma autoridade declara inválido ato e cogita limitar o alcance temporal da declaração para
- Durante revisão de ato concluído, o controlador identifica prática administrativa reiterad
- Uma proposta de compromisso oferece ao interessado dispensa permanente de obrigação reconh
- Uma falha funcional produziu prejuízo elevado, mas a apuração identificou apenas divergênc
- Uma autoridade adota parecer técnico cuja falha grave não era perceptível pelos elementos 
- Uma consultoria jurídica é obrigatoriamente ouvida antes de ato, mas a norma não atribui e
- Um controlador considera irregular uma política pública apenas porque faria escolha de ges
- Em 2025, ato concluído sob prática reiterada e pública de 2022 é revisto após mudança juri
- Uma situação plenamente constituída é protegida pelo art. 24, mas um contrato ainda gera e
- Em procedimento de controle, há erro grosseiro alegado com base apenas em dano de grande v
- A autoridade máxima de uma entidade edita enunciado interno sobre aplicação normativa e re
- Uma autoridade invoca o art. 25 da LINDB para sustentar um procedimento material de valida
- Após 2024, parecer jurídico afirma que o caput do art. 18 do Decreto nº 9.830/2019 permane
- Parecer opcional, consulta obrigatória e parecer vinculante são utilizados em três process
- Uma autoridade que fundamenta ato apenas em eficiência administrativa deve considerar cons
- Ao declarar a invalidade de contrato administrativo, basta indicar o dispositivo violado; 
- Em apuração sobre atraso de serviço em área de difícil acesso, a autoridade deve considera
- Sempre que a Administração publicar nova interpretação de regra indeterminada, terá de con
- Um compromisso administrativo do art. 26, embora validamente negociado com os interessados
- A responsabilidade pessoal do agente por decisão técnica, segundo o art. 28 da LINDB, resu
- Uma decisão sancionadora deve levar em conta natureza e gravidade da infração, danos, circ
- A alteração posterior de uma orientação geral invalida por si mesma atos concluídos sob pr
- Ao estabelecer compensação por benefício indevido em processo administrativo, a autoridade
- Um regulamento administrativo emitido por entidade pública, por força do art. 30 da LINDB,
- O Decreto nº 9.830/2019 permite, ao modular invalidação, restringir seus efeitos ou fixar 
- Uma autoridade pode celebrar compromisso do art. 26 que exonere permanentemente destinatár
- Uma jurisprudência administrativa majoritária da época, ainda que posteriormente superada,
- O elevado montante de dano ao erário é, por si só, prova de erro grosseiro do parecerista,
- A imposição de novo condicionamento por orientação inédita sobre norma indeterminada pode 
- O caráter opinativo de parecer jurídico impede responsabilização pessoal do parecerista me
- A adoção de parecer técnico não transfere automaticamente ao decisor eventual erro grossei
- A revogação do caput do art. 18 do Decreto nº 9.830/2019 pelo Decreto nº 12.002/2024 elimi
- O art. 25 da LINDB, mantido em vigor pela Lei nº 13.655/2018, estabelece homologação judic
- No exame de contrato concluído, o controlador pode considerar jurisprudência majoritária e
```

## Pedido 43: Organizações criminosas (`legislacao-penal-especial.organizacoes-criminosas`, 253 tópicos de edital)

```
Matéria: legislacao-penal-especial.organizacoes-criminosas (Organizações criminosas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito legal, tipificação e Convenção de Palermo": 19
- "2. Colaboração premiada: negociação, homologação e proteção": 23
- "3. Ação controlada, infiltração e acesso a dados": 19
- "4. Atualizações de 2025–2026 e Marco Legal de Combate ao Crime Organizado": 20
- "5. Procedimento, garantias, crimes correlatos e aplicação integrada": 19

Já existem (não repita):
- Quatro pessoas mantêm divisão estável de tarefas para praticar infrações nacionais com pen
- Uma estrutura criminosa cumpre os demais requisitos da lei doméstica, mas pretende crimes 
- Alguém conscientemente custeia o funcionamento de organização criminosa, embora não execut
- Uma organização usa arma de fogo em sua atuação. Qual consequência específica consta do ar
- Ao estudar a Convenção de Palermo, uma pessoa pergunta o número mínimo de integrantes no c
- Uma pessoa investigada oferece fatos úteis em colaboração premiada, acompanhada de defesa 
- Durante a elaboração de um acordo de colaboração, pretende-se convidar quem julgará o proc
- O relato de um colaborador é a única base para pedido de prisão preventiva contra outra pe
- Investigadores mantêm vigilância de conduta atribuída a organização criminosa e retardam i
- Um acordo de colaboração produziu prova de recuperação parcial de produto criminoso. Qual 
- Uma pessoa apresenta proposta de colaboração sem ter contato com a defesa, e a negociação 
- Uma infiltração presencial foi solicitada para apuração de organização criminosa, mas o fa
- Uma operação de infiltração virtual chegou a autorizações renovadas por períodos semestrai
- Uma investigação pede sem ordem judicial o conteúdo de conversas privadas, afirmando exist
- Uma norma de 2025 tipificou o ajuste entre duas ou mais pessoas para praticar violência ou
- Uma facção ultraviolenta com três integrantes impõe controle territorial por grave ameaça.
- Uma apuração recai sobre crime previsto na Lei nº 15.358/2026, com indiciado solto. Qual p
- Uma célula de facção ultraviolenta pratica atos do art. 2º da Lei nº 15.358/2026 para cont
- Uma pessoa finge integrar milícia privada para intimidar comerciantes e obter vantagens, s
- Uma facção ultraviolenta emprega recursos de vigilância e comunicação sofisticados para fa
- Uma operação de ação controlada atravessará a fronteira com outro Estado. Qual condição ad
- Uma infiltração policial demanda análise do juiz, após representação com manifestação do M
- Depois de sentença, pessoa condenada presta colaboração efetiva. Qual benefício excepciona
- Em processo envolvendo organização criminosa, há pessoa acusada presa. Qual limite inicial
- Em um caso há risco comprovado à integridade de quem julga, e a Lei nº 12.694/2012 autoriz
- A organização criminosa do art. 1º, § 1º, da Lei nº 12.850/2013 exige pelo menos quatro pe
- Uma infração nacional com pena máxima exatamente igual a quatro anos basta por si só para 
- O art. 2º da Lei nº 12.850/2013 prevê pena de três a oito anos de reclusão e multa para pr
- O juiz participa das negociações e fixa os benefícios que as partes deverão aceitar para c
- A ação controlada da Lei nº 12.850/2013 envolve retardamento observado da intervenção e co
- O acesso direto a dados cadastrais do art. 15 da Lei nº 12.850 inclui, sem autorização jud
- A Convenção de Palermo define grupo criminoso organizado a partir de três pessoas e infraç
- O emprego de arma de fogo na atuação da organização criminosa acarreta aumento de um sexto
- A proposta de colaboração premiada recebida demarca confidencialidade das tratativas, e se
- A redução de pena em até dois terços, prevista no art. 4º, caput, aplica-se obrigatoriamen
- A infiltração virtual, prevista no art. 10-A, pode ser renovada por períodos de até seis m
- A Lei nº 15.358/2026 modificou o art. 1º, § 1º, da Lei nº 12.850/2013 para substituir quat
- O tipo de favorecimento ao domínio social estruturado do art. 3º da Lei nº 15.358/2026 inc
- Se a ação controlada envolver transposição de fronteiras, o art. 9º da Lei nº 12.850/2013 
- O agente infiltrado responde pelos excessos praticados, e o art. 13, parágrafo único, limi
- O art. 4º, § 16, da Lei nº 12.850/2013 impede condenação exclusivamente baseada em declara
- O art. 2º, § 4º, I, da Lei nº 12.850/2013, após a Lei nº 15.487/2026, contempla tanto part
- Nos crimes do novo marco de 2026, o art. 5º determina que o simples descumprimento do praz
- O prazo inicial de instrução com réu preso na Lei nº 12.850/2013 é de 120 dias, com possív
- Em processo da Lei nº 12.850/2013, uma pessoa investigada será ouvida amanhã e sua defesa 
```

## Pedido 44: Integração de sistemas: XML, SOAP e serviços web (`informatica.integracao-servicos-web-soap-xml`, 253 tópicos de edital)

```
Matéria: informatica.integracao-servicos-web-soap-xml (Integração de sistemas: XML, SOAP e serviços web)
Capítulos e quantas questões quero em cada um (total 100):
- "1. XML: documentos, elementos, atributos e namespaces": 18
- "2. XML Schema, XPath e XSLT: validação e transformação": 17
- "3. SOAP, WSDL e UDDI: serviços orientados a contratos": 15
- "4. REST, JSON, OpenAPI/Swagger e Apache CXF": 32
- "5. Soluções de integração, mensageria e e-PING": 18

Já existem (não repita):
- Um sistema recebe o documento `<nota><valor>50</valor></nota>`, mas não possui esquema ass
- Uma aplicação produz `<usuario ativo='sim'><nome>Ana</nome></usuario>`. Na modelagem apres
- Durante a importação de cadastro, um campo de descrição deve registrar literalmente `A & B
- Dois sistemas usam a etiqueta `codigo` para identificar entidades distintas. Qual recurso 
- O consumidor recebe `<Item>1</item>` em um arquivo XML. Por que o processamento de boa for
- Um contrato XML precisa restringir o campo `situacao` aos valores `NOVO`, `EM_ANALISE` e `
- Um documento XML exige que o campo `complemento` possa ser omitido, sem obrigar a presença
- Uma aplicação precisa transformar `<preco>90</preco>` em `<valor>90</valor>`, conservando 
- Ao selecionar todos os elementos `pedido` com atributo `situacao` igual a `aberto`, qual r
- Um XSD declara `xs:sequence` com `nome` seguido de `idade`. O XML recebido contém os mesmo
- Uma equipe precisa identificar o componente obrigatoriamente presente em uma mensagem SOAP
- Um consumidor já conhece o endpoint de um serviço SOAP e recebe seu contrato WSDL, mas não
- Na documentação de um serviço legado, aparecem `types`, `message`, `portType`, `binding`, 
- Uma arquitetura publica descrições de provedores, serviços disponíveis e interfaces técnic
- Ao migrar uma integração SOAP 1.1 para SOAP 1.2 sobre HTTP, uma equipe quer identificar um
- Um desenvolvedor escreve `{'nome':'Lia','ativo':true}` e afirma tratar-se de documento JSO
- Uma equipe documentou `GET /produtos/{id}`, seus parâmetros e respostas por uma descrição 
- Um cliente repete um `DELETE` depois de perder a resposta HTTP da primeira tentativa. A se
- Uma API opera por recursos identificados por URIs, métodos HTTP, interface uniforme e ausê
- Em um projeto Java, há um contrato SOAP baseado em WSDL e uma API de recursos HTTP. Preten
- Uma mensagem sobre `PedidoCriado` é publicada em RabbitMQ pelo protocolo AMQP 0-9-1. A apl
- Uma plataforma deseja que o mesmo evento publicado seja copiado para todas as filas conect
- O produtor recebeu confirmação de publicação do RabbitMQ. Logo depois, o consumidor falhou
- Um serviço externo precisa informar alterações de situação assim que ocorram e propõe cham
- Um projeto de integração governamental deve adotar diretrizes comuns de interoperabilidade
- Um XML cujo elemento raiz contém dois elementos filhos pode ser bem formado mesmo que não 
- Em XML, a declaração `xmlns='urn:compras'` insere automaticamente os atributos sem prefixo
- Em um documento XML, `<pessoa idade='18'/>` é representação sintaticamente possível de um 
- A codificação de caracteres indicada na declaração XML determina qual é o nome lógico de u
- O recurso `xs:maxOccurs='unbounded'` pode permitir repetição sem limite máximo especificad
- Uma folha XSLT substitui o parser XML e garante, por si, que todos os tipos exigidos pelo 
- Uma expressão XPath que utiliza `@codigo` seleciona atributos chamados `codigo` no context
- Um elemento que aceita `xsi:nil='true'` pode ser omitido do XML mesmo que a declaração cor
- No SOAP 1.2, a representação de uma falha SOAP utiliza uma estrutura Fault no Body da mens
- A operação abstrata descrita por um WSDL executa diretamente o código de negócio do servid
- UDDI pode registrar informações técnicas de serviços para localização, sem que isso o tran
- Na versão 2.0 de WSDL, a definição abstrata é identificada pelo termo `portType`, exatamen
- Um contrato OpenAPI pode descrever parâmetros e respostas de operações HTTP sem obrigar qu
- Um servidor usa JSON nas respostas e permite consultas por HTTP GET. Esses dois fatores, i
- Uma operação HTTP PUT pode manter sua propriedade de idempotência mesmo quando respostas s
- O Apache CXF disponibiliza apenas serviços SOAP com JAX-WS, pois implementar recursos REST
- No RabbitMQ com exchange fanout, uma mensagem publicada pode ser encaminhada a várias fila
- A confirmação de publicação emitida pelo broker constitui prova suficiente de que o consum
- Um webhook recebido duas vezes com mesmo identificador de evento deve ser processado consi
- A adoção da arquitetura e-PING torna dispensáveis contratos explícitos de API, autenticaçã
```

## Pedido 45: Licitações e contratos das estatais (Lei 13.303/2016, arts. 28 a 91) (`direito-administrativo.licitacoes-contratos-estatais-lei-13303`, 251 tópicos de edital)

```
Matéria: direito-administrativo.licitacoes-contratos-estatais-lei-13303 (Licitações e contratos das estatais (Lei 13.303/2016, arts. 28 a 91))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Obrigatoriedade de licitar, dispensa e inexigibilidade": 21
- "2. Princípios, diretrizes e normas gerais da licitação": 20
- "3. Obras e serviços: regimes de execução e regras especiais": 20
- "4. Procedimento licitatório: fases, julgamento e procedimentos auxiliares": 20
- "5. Contratos: regime, garantias, alterações, sanções e controle": 19

Já existem (não repita):
- Pela Lei 13.303/2016, os contratos das estatais com terceiros destinados à prestação de se
- A comercialização, de forma direta, de produtos especificamente relacionados com o objeto 
- Uma estatal pretende formar uma parceria societária com empresa escolhida por suas caracte
- Na Lei 13.303/2016, é dispensável a licitação para obras e serviços de engenharia de valor
- Os valores-limite de dispensa de licitação previstos na Lei 13.303 podem ser alterados por
- Quando não acudirem interessados à licitação anterior e ela, justificadamente, não puder s
- A contratação de serviços de publicidade e divulgação pode ser feita por inexigibilidade q
- Comprovado sobrepreço em contratação direta por dispensa ou inexigibilidade, a Lei 13.303 
- A aquisição de equipamento que só possa ser fornecido por representante comercial exclusiv
- Cada empresa pública e sociedade de economia mista deve publicar e manter atualizado regul
- NÃO é princípio expressamente listado para as licitações e contratações da Lei 13.303:
- A licitação nas estatais busca a proposta mais vantajosa considerando, inclusive, o ciclo 
- Durante a execução de uma obra, apurou-se pagamento de quantidades superiores às efetivame
- Para a aquisição de bens e serviços comuns, a Lei 13.303 estabelece como diretriz a adoção
- O valor estimado do contrato a ser celebrado pela estatal é sigiloso, mas a informação dev
- O orçamento estimado deverá constar do instrumento convocatório quando o critério de julga
- Está impedida de participar de licitação de uma estatal a empresa cujo sócio detentor de m
- Na licitação de estatal para aquisição de bens pelo critério de menor preço, o prazo mínim
- Nas licitações de estatais em regime de contratação integrada ou semi-integrada, o prazo m
- Na contratação semi-integrada, a contratada elabora e desenvolve o projeto executivo e exe
- O regime de execução em que a licitação parte de um anteprojeto de engenharia e a contrata
- A cláusula contratual que define riscos e responsabilidades entre as partes e caracteriza 
- Quem elaborou o projeto básico de uma obra pode participar da licitação para sua execução,
- A Lei 13.303 admite, na contratação de obras e serviços, remuneração variável vinculada ao
- Sobre a contratação simultânea de mais de um contratado para o mesmo serviço, é correto af
- O regime de execução indicado quando os quantitativos da obra são imprecisos, contratando-
- Na sequência de fases da licitação da Lei 13.303, a habilitação ocorre:
- A fase de habilitação pode, excepcionalmente, anteceder as fases de apresentação de propos
- No modo de disputa fechado, os licitantes apresentam lances públicos e sucessivos, crescen
- É critério de julgamento previsto na Lei 13.303, voltado a contratos de eficiência em que 
- Em caso de empate entre duas propostas, o primeiro critério de desempate previsto na Lei 1
- Se, mesmo após a negociação com os licitantes, não for obtido valor igual ou inferior ao o
- Salvo inversão de fases, o procedimento licitatório das estatais tem fase recursal única, 
- A revogação da licitação pela estatal deve fundar-se em razões de interesse público decorr
- NÃO é procedimento auxiliar das licitações previsto na Lei 13.303:
- Os contratos celebrados pelas estatais com base na Lei 13.303 regulam-se pelas suas cláusu
- A estatal pode alterar unilateralmente o contrato para melhor adequação às finalidades de 
- Quanto à garantia contratual na Lei 13.303, é correto afirmar:
- Os contratos regidos pela Lei 13.303 não excederão 5 anos de duração, salvo exceções legai
- No caso particular de reforma de edifício ou de equipamento, o limite para acréscimos acor
- A inadimplência do contratado com os encargos trabalhistas, fiscais e comerciais transfere
- Sobre a subcontratação nos contratos das estatais, assinale a alternativa correta.
- Pela inexecução total ou parcial do contrato, a estatal pode aplicar ao contratado, garant
- Qualquer cidadão é parte legítima para impugnar edital de licitação de estatal por irregul
- Os órgãos de controle externo e interno têm acesso irrestrito aos documentos das estatais,
```

## Pedido 46: Finanças Públicas (`economia.financas-publicas`, 250 tópicos de edital)

```
Matéria: economia.financas-publicas (Finanças Públicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Expansão do setor público, escolha coletiva e bens públicos": 14
- "2. Federalismo fiscal, descentralização e transferências": 24
- "3. Estrutura tributária, Ramsey, Laffer e neutralidade": 22
- "4. Contabilidade fiscal: NFSP e conceitos de resultado": 21
- "5. Dívida, inflação, senhoriagem e equivalência ricardiana": 19

Já existem (não repita):
- Após décadas de urbanização e maior renda real, um país passa a demandar mais serviços urb
- Durante uma emergência prolongada, a população aceita impostos extraordinários; depois do 
- Uma entidade empresarial gasta recursos para conseguir subsídio seletivo que lhe transfere
- Dois moradores atribuem valores marginais de 6 e 4 unidades monetárias à mesma unidade adi
- Famílias com plena liberdade de mudança comparam municípios que oferecem combinações difer
- Dois municípios do mesmo estado apresentam bases tributárias por habitante muito diferente
- Um programa transfere metade de cada unidade gasta pelo município em vacinação, desde que 
- Duas regiões desejam volumes distintos de serviços locais. Não há economia de escala relev
- Uma mercadoria sofre cobrança de 3 unidades monetárias por litro, independentemente do pre
- Em uma venda, o preço sem imposto é 100 unidades monetárias e a alíquota é de 20% calculad
- Uma cadeia tem vendas pré-imposto de 100 na primeira etapa e 200 na segunda, tributadas a 
- Em um sistema hipotético, pessoa com renda 2.000 paga imposto de 200; pessoa com renda 10.
- No caso simplificado da regra de elasticidade inversa, dois produtos têm elasticidades com
- Uma análise informa que a arrecadação cairia se a alíquota atual de certo tributo fosse el
- A conta fiscal de um período mostra despesa primária de 950, receita primária de 900 e jur
- Em um exercício histórico com inflação relevante, a NFSP nominal foi 130 e a correção mone
- Uma análise fiscal pretende identificar como o desequilíbrio foi financiado e parte da var
- Um governo tem dívida inicial de 60% do PIB, juros nominais de 10% e crescimento nominal d
- Tributo de 100 unidades é fixado nominalmente e recolhido um mês depois. Os preços crescem
- O governo reduz impostos correntes e emite dívida de mesmo valor, sem mudar gasto ou o val
- Dois usuários compartilham um bem público indivisível. Na quantidade Q*, o primeiro tem av
- Uma região presta serviço que beneficia vizinhos e possui altos custos fixos. A autoridade
- Considere um bem vendido por preço final de 125 unidades, com imposto de 20% por dentro. Q
- Um país apresenta superávit primário de 30, juros nominais de 90 e ajuste patrimonial que 
- A dívida inicial equivale a 80% do PIB; os juros nominais são 4%, e o PIB nominal cresce 6
- Um país se industrializa e aumenta de forma persistente sua demanda por transporte, admini
- Ao optar por morar em município com tributos elevados e serviços mais amplos, uma família 
- Duas localidades valorizam serviços diferentes e podem fornecê-los sem economias de escala
- Uma família com renda elevada paga mais imposto em unidades monetárias do que outra de bai
- No período em que a despesa primária é 500 e a receita primária é 450, há necessidade de f
- Como alimentos essenciais frequentemente apresentam demanda relativamente inelástica, a re
- Um ente central cobre 40% de cada unidade de despesa local com saneamento, desde que a loc
- Uma autoridade observa que a alíquota atual está no ponto de máxima arrecadação da curva d
- Para verificar como o déficit público foi financiado, um analista parte da variação da dív
- Em um exercício histórico, uma NFSP nominal de 100 e correção monetária pertinente de 30 g
- Cinco eleitores possuem preferências unidimensionais de pico único, com gastos favoritos d
- Duas pessoas avaliam uma unidade adicional de segurança coletiva em 7 e 9, e o custo margi
- Uma tributação seletiva incide sobre um tipo específico de bebida. Ela pode ser estruturad
- Estados com capacidades arrecadatórias díspares configuram desequilíbrio fiscal vertical, 
- Uma cadeia com vendas pré-tributo de 100 e 300 em duas etapas tem IVA de 10% com créditos 
- Um corte temporário de impostos financiado por títulos públicos é neutro para o consumo so
- Uma administração diminui o tempo entre fato gerador e recebimento do imposto, sem mudar a
- Uma economia registra inflação zero, mas a demanda por saldos reais de base monetária cres
- Em um modelo sem ajustes patrimoniais, com dívida inicial de 50% do PIB, juros nominais de
- Após valorização cambial e reclassificação patrimonial, a dívida pública bruta cai no perí
```

## Pedido 47: Economia brasileira: planos, inflação, câmbio e política monetária (`economia.economia-brasileira`, 249 tópicos de edital)

```
Matéria: economia.economia-brasileira (Economia brasileira: planos, inflação, câmbio e política monetária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Do crescimento acelerado à crise da dívida e à inflação inercial": 20
- "2. Planos heterodoxos: Cruzado, Bresser, Verão e Collor": 26
- "3. Plano Real, abertura e ajuste cambial de 1999": 27
- "4. Economia brasileira de 2003 a 2024: ciclos e políticas": 15
- "5. Banco Central, Selic, IPCA e meta contínua de inflação": 12

Já existem (não repita):
- Um analista situa a interrupção do crédito externo voluntário latino-americano em 1982. Qu
- Um governo precisa financiar parte do déficit mediante emissão monetária. A população cons
- Em 1986, preços foram congelados e reajustes salariais automáticos seriam acionados após d
- Ao comparar estabilizações brasileiras, um estudante identifica o plano iniciado em 1990 p
- Uma família acompanhou preços expressos em URV durante o primeiro semestre de 1994, mas co
- No arranjo de metas de inflação brasileiro, qual instituição estabelece a meta do IPCA e q
- Após estabilização de preços, a política brasileira deixou de administrar bandas cambiais 
- Um estudante afirma que o tripé macroeconômico brasileiro consiste em inflação indexada, c
- Os salários, aluguéis e outros preços são atualizados segundo a inflação anterior, mesmo s
- O Brasil registrou superávits comerciais no ajuste externo de 1983-1984 enquanto a demanda
- Uma família perdeu poder de compra durante a inflação dos anos 1980 e passou a aplicar rec
- Um texto histórico atribui a criação da URP e o abandono do gatilho salarial a uma tentati
- Um economista contrasta o plano de 1994 com os congelamentos anteriores: houve primeiro me
- Após a estabilização do real, uma taxa de câmbio relativamente valorizada contribuiu para 
- No início dos anos 2000, o Brasil experimentou aumento de exportações de commodities, créd
- Uma proposta de análise diz que a política econômica brasileira em 2020 foi invariavelment
- O Banco Central deseja contrair liquidez bancária para manter a Selic efetiva próxima da m
- A inflação acumulada em doze meses supera a tolerância superior da meta contínua por um ún
- Um analista compara desindustrialização e reprimarização ao avaliar a composição produtiva
- Uma análise associa o crescimento de 1968-1973, os choques do petróleo e o II PND à vulner
- Desde janeiro de 2025, o regime brasileiro fixa meta de IPCA de 3% com margem de 1,5 ponto
- Ao comparar regras fiscais da década de 2010 com as mudanças de 2023, um relatório precisa
- Em 2002, a moeda brasileira sofreu pressão de desvalorização em cenário de incerteza polít
- Uma pessoa afirma que autonomia do Banco Central significa liberdade para definir sozinho 
- Após seis meses consecutivos com o IPCA acumulado em doze meses acima do limite de tolerân
- Nos anos 1980, o encarecimento dos juros internacionais e a dificuldade de obter novos emp
- Durante o ajuste externo brasileiro de 1983-1984, a obtenção de superávit comercial compro
- O Plano Cruzado, de 1986, procurou conter a inflação com nova moeda, congelamento de preço
- A Unidade Real de Valor (URV) foi criada no Plano Bresser, em 1987, para substituir o dinh
- O Plano Real incluiu preparação fiscal, fase de preços e contratos referidos à URV e, em 1
- O regime brasileiro de câmbio flutuante foi adotado no mesmo momento do início do Plano Cr
- No regime de metas, o Copom estabelece a meta da taxa Selic, enquanto o Conselho Monetário
- A abertura comercial dos anos 1990 significou que todos os setores industriais brasileiros
- Uma moeda apreciada na fase inicial do Real pôde reduzir custos relativos de importações e
- A crise financeira internacional de 2008 ocorreu durante o Plano Cruzado e foi a razão dir
- A retração da atividade brasileira em 2015-2016 deve ser analisada pela evolução real da p
- O teto de gastos federal foi criado em 2023 pela Lei Complementar 200, ao passo que a Emen
- A Lei Complementar 179/2021 fixou a estabilidade de preços como objetivo fundamental do Ba
- O Decreto 12.079/2024 considera a meta contínua de inflação descumprida quando o IPCA acum
- A Resolução CMN 5.141/2024 estabelece, para a meta contínua iniciada em janeiro de 2025, I
- Na sistemática vigente desde 2025, o Copom pode modificar a meta de inflação e o intervalo
- Na sistemática de metas iniciada em 2025, a comunicação formal do descumprimento inclui ca
- Em operação de mercado aberto, o Banco Central pode retirar liquidez ao comprar títulos pú
- Um país pode registrar superávit na balança comercial e, ainda assim, déficit nas transaçõ
- O câmbio flutuante adotado em 1999 impediu, por definição, qualquer intervenção pontual do
```

## Pedido 48: Processo organizacional (`administracao-publica.processo-organizacional`, 249 tópicos de edital)

```
Matéria: administracao-publica.processo-organizacional (Processo organizacional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Funções administrativas e planejamento nos três níveis": 21
- "2. Administração por objetivos, BSC, gestão de pessoas e equipes terceirizadas": 33
- "3. Comunicação, direção, coordenação e redes públicas": 17
- "4. Controle, indicadores de desempenho e avaliação de resultados": 15
- "5. Gestão por processos e projetos, governança e aplicações integradas": 14

Já existem (não repita):
- Uma unidade estabelece que pretende reduzir o prazo médio de resposta em seu serviço no pr
- Uma instituição estabeleceu objetivo estratégico de ampliar acesso ao atendimento. Uma áre
- Uma equipe define a escala da próxima semana, horários de triagem e sequência de conferênc
- Durante diagnóstico SWOT, identificam-se equipe altamente capacitada e demanda externa ine
- Um planejamento contém a frase oferecer serviço público acessível e confiável à população,
- Uma equipe estabelece objetivos conjuntamente com a direção, acompanha indicadores em reun
- Uma organização decide construir Balanced Scorecard na concepção clássica de Kaplan e Nort
- Uma unidade registra 1.200 pedidos mensais, com tempo médio de 15 minutos de trabalho dire
- A chefia percebe atraso persistente em serviço prestado por equipe terceirizada e pretende
- Uma unidade judicial organiza política de desenvolvimento e bem-estar do próprio quadro, c
- Em reunião formal, uma equipe de atendimento apresenta à direção um relatório sobre aument
- Uma mensagem institucional informa que determinada etapa foi homologada, mas parte dos rec
- Duas unidades de mesmo nível trocam informações pelo sistema oficial para ajustar o horári
- Uma organização divulga relatório de atividades com gráfico de desempenho, mas omite o per
- Um atendimento integrado depende de três organizações que enviam casos umas às outras. Cad
- Antes que pedidos entrem na fila de análise, um sistema impede envio de formulário sem ide
- Durante o expediente, painel alerta que a fila de atendimento ultrapassou o limite definid
- Unidades comparáveis concluíram respectivamente 600 atendimentos em 300 horas e 600 em 400
- Uma unidade programou 500 respostas e entregou 450 no período de referência. Sem dados de 
- Uma equipe implantou atendimento virtual e registrou aumento de acessos, mas deseja provar
- Uma equipe registra como os requerimentos são efetivamente recebidos, conferidos, devolvid
- Uma instituição concluiu a implantação de portal de serviços, com início, escopo e encerra
- Um fluxo sequencial de atendimento possui duas etapas com capacidades estáveis de 100 e 60
- Em outubro de 2026, uma unidade avalia qual estratégia nacional do Judiciário está em exec
- Em uma organização pública, comitê institucional estabelece diretrizes, examina riscos e m
- Ao fixar objetivos e escolher meios antes de implementar mudanças no atendimento, uma unid
- Uma escala de atendimento para a semana seguinte constitui planejamento estratégico apenas
- No diagnóstico SWOT de um serviço, deficiência de sistema pertencente à própria unidade é 
- Na administração por objetivos, a direção deve limitar-se a comunicar metas imutáveis aos 
- No Balanced Scorecard clássico, as perspectivas incluem finanças, clientes, processos inte
- Quando serviços de apoio são terceirizados, a organização contratante passa a exercer ordi
- Uma instrução recebida por mensagem institucional pode apresentar ruído semântico quando a
- Um processo mapeado em AS IS deve representar somente a rotina futura ideal, excluindo exc
- No dimensionamento do quadro próprio, o volume de tarefas e seu tempo médio podem servir c
- Uma comunicação formal entre equipe e direção constitui fluxo descendente, independentemen
- Uma equipe que verifica a qualidade de uma amostra de serviços somente depois de concluído
- A execução de 450 das 500 atividades previstas evidencia, por si só, efetividade de 90% so
- A gestão de rede entre organizações diferentes precisa identificar responsabilidades por e
- Como a Resolução CNJ nº 692 foi publicada em 2026, ela já havia substituído a Estratégia N
- O art. 5º do Decreto nº 9.203/2017 descreve liderança, estratégia e controle como mecanism
- Em uma cadeia de tarefas com capacidades diárias de 100 e 60 solicitações, elevar a primei
- Em regime de fluxo estável, a Lei de Little prevê que 20 solicitações concluídas diariamen
- Se um portal digital foi implantado no prazo, esse fato demonstra automaticamente que o pr
```

## Pedido 49: Regulação econômica (`administracao-publica.regulacao-economica`, 249 tópicos de edital)

```
Matéria: administracao-publica.regulacao-economica (Regulação econômica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamento constitucional, funções estatais e justificativas da regulação": 20
- "2. Falhas de mercado, poder econômico, monopólio natural e informação": 19
- "3. Agente-principal, escolha pública, captura e custos de transação": 24
- "4. Instrumentos, incentivos, liberdade econômica e prevenção do abuso regulatório": 19
- "5. AIR, ARR, consulta, governança e ciclo de melhoria da regulação": 18

Já existem (não repita):
- O governo formula um plano de investimento nacional e informa prioridades para empresas pr
- Para acelerar a adoção de equipamentos menos poluentes, a administração oferece apoio fina
- Em uma cidade pequena, duplicar uma rede subterrânea de distribuição produziria custos fix
- Uma fábrica lança partículas no ar e impõe custos de limpeza e saúde a moradores sem que t
- Antes de comprar equipamento de segunda mão, compradores não conseguem distinguir exemplar
- Uma atividade privada é submetida a inspeção e requisitos sanitários previstos legalmente 
- A autoridade formula exigências com base exclusiva em informações das empresas já instalad
- Uma norma impede a entrada de empresas menores mediante especificação técnica cara, sem re
- Antes de criar regra para informação de preços, a autoridade estuda o problema, compara di
- Uma associação com poucos beneficiários pressiona intensamente por restrição que impõe cus
- Uma instituição delega manutenção a empresa, mas não observa integralmente o esforço aplic
- Uma exigência de três cadastros redundantes obriga pequenas empresas a contratar consultor
- O plano plurianual de uma política pública indica expectativa de expansão do investimento 
- Uma nova norma obriga operadores a contratar laudo de fornecedor exclusivo, embora métodos
- Uma regra de qualidade já está em vigor há anos. A administração mede acidentes, despesas 
- Uma infraestrutura é regulada por teto tarifário. Após alguns anos, operadores reduzem man
- A administração federal pretende editar regra de interesse geral após AIR e abre consulta 
- A expansão de um mercado torna viável que empresas concorram por operações complementares,
- Uma autoridade exige nova certificação técnica para tecnologia recente, apesar de provas c
- No exame de regra que favorece poucos agentes estabelecidos, descobre-se que toda informaç
- Uma política combina orientação de prioridades para investimento privado, incentivo à inov
- Em situação urgente, é proposta uma norma de interesse geral. A autoridade dispensa AIR, f
- Uma norma foi editada após dispensa de AIR fundada especificamente em urgência. Em quanto 
- Uma norma remunera prestadores conforme quantidade de atendimentos, e surgem atendimentos 
- Uma política pública oferece crédito favorecido a operadores que adotam padrão ambiental, 
- Ao prever fiscalização, incentivo e planejamento, o art. 174 da Constituição admite funçõe
- O planejamento da atividade econômica previsto no art. 174 é determinante tanto para o set
- A atividade industrial que transfere custos de poluição para moradores sem internalizá-los
- O monopólio natural é identificado exclusivamente pelo número de empresas existentes em um
- O uso de subsídio para incentivar investimentos ambientalmente desejáveis é exemplo de fom
- A Análise de Impacto Regulatório do Decreto nº 10.411/2020 tem como propósito central veri
- A relação agente-principal pode apresentar risco moral quando, depois da delegação, o agen
- A seleção adversa descreve principalmente a redução de esforço do prestador após receber p
- A concentração dos ganhos de uma regra em poucos participantes, combinada com custos peque
- A existência de reuniões técnicas entre reguladores e empresas fiscalizadas constitui prov
- A obrigação de preencher cadastros redundantes pode elevar custos de transação sem aumenta
- A Lei nº 13.874/2019 proíbe a edição de qualquer exigência técnica que aumente custos para
- O Decreto nº 10.411/2020 distingue hipóteses em que sua regra de AIR não incide de hipótes
- No modelo agente-principal, basta publicar uma norma com metas para eliminar diferenças de
- A instituição de obrigação técnica desnecessária que favoreça operadores tradicionais pode
- Após receber relatório de AIR, a autoridade fica juridicamente vinculada à alternativa mai
- Na dispensa de AIR por urgência regida pelo Decreto nº 10.411/2020, a nota técnica deve id
- Nos atos normativos cobertos pelo Decreto nº 10.411/2020, o prazo mínimo não urgente de co
- Uma ARR pode revelar que a exigência criada para reduzir risco alcançou seu objetivo, mas 
- Se uma medida regulatória reduzir preços após a sua edição, isso demonstra, por si só, cau
```

## Pedido 50: História geral (`historia.historia-geral`, 248 tópicos de edital)

```
Matéria: historia.historia-geral (História geral)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Humanização, primeiras civilizações e sociedades americanas antigas": 19
- "2. Grécia, Roma, Europa medieval e reinos africanos": 20
- "3. Mundo moderno: Renascimento, Reformas, navegações e monarquias": 21
- "4. Revoluções atlânticas, industrialização e ideologias do século XIX": 21
- "5. Guerras mundiais, Revolução Russa, descolonização e Guerra Fria": 19

Já existem (não repita):
- Um sítio arqueológico apresenta cultivo de plantas domesticadas, aldeamentos e armazenamen
- Um museu expõe tabuletas com escrita cuneiforme, registros administrativos de cidades e re
- Um mapa indica Tenochtitlán como centro de domínio tributário mesoamericano no início do s
- Uma descrição de Atenas no século V a.C. menciona assembleias de cidadãos e deliberação po
- Um relato menciona redes comerciais transaarianas de ouro e sal, peregrinação de Mansa Mus
- No século XVI, um movimento criticou indulgências, difundiu ideias sobre justificação pela
- Uma política econômica dos séculos XVI e XVII combina proteção de manufaturas, monopólios 
- Uma linha do tempo apresenta revolta de escravizados na colônia de Saint-Domingue, conflit
- Um historiador descreve a deposição do último imperador romano do Ocidente em 476 e afirma
- Um documento medieval descreve camponeses vinculados a terras senhoriais e sujeitos a pres
- Uma inscrição do antigo vale do Nilo refere-se a reis núbios associados a Napata, enquanto
- Um manual associa o Tratado de Tordesilhas, de 1494, a uma partilha europeia do ultramar. 
- Um autor do século XVIII francês defende que a terra cultivada é origem fundamental do pro
- Um quadro compara uma máquina têxtil movida por vapor no fim do século XVIII com siderurgi
- Uma formação imperial do século XIX promoveu unificação nacional sob direção da Prússia, c
- Um documento apresenta a sequência: deposição do czar, Governo Provisório e depois tomada 
- Em debate sobre a questão operária no fim do século XIX, um participante defende proprieda
- Relato de viagem do século XIV registra intercâmbio africano de ouro, sal e estudos islâmi
- Em documento de 1884–1885, representantes de potências europeias negociam parâmetros para 
- Uma fonte descreve reformas de Estado, industrialização e expansão militar japonesa após 1
- Um relato informa que, em 1948–1949, aviões ocidentais abasteceram parte de Berlim durante
- Um aluno apresenta 1917, 1922 e 1924 como anos equivalentes de fundação da URSS. Para corr
- Uma sequência descreve Índia e Paquistão independentes em 1947, Gana independente em 1957 
- Um texto afirma que a invasão alemã da Polônia começou a Segunda Guerra na Europa em setem
- Um historiador organiza três fotografias: multidões diante do Muro de Berlim, cerimônia da
- Uma pesquisadora identifica instrumentos de pedra lascada usados por hominíneos muito ante
- O Reino de Kush, com centros em Napata e Meroé, foi politicamente subordinado ao Egito dur
- A civilização maia desenvolveu centros urbanos e escrita hieroglífica, enquanto o Império 
- A democracia ateniense clássica pode ser considerada sufrágio universal porque mulheres, e
- A cidade de Grande Zimbábue, ligada a comunidades shona, desenvolveu arquitetura monumenta
- O Concílio de Trento, entre 1545 e 1563, foi convocado por Martinho Lutero para estabelece
- Uma política mercantilista podia combinar proteção de manufaturas e monopólios coloniais, 
- Na expansão marítima de fins do século XV, os europeus encontraram um continente americano
- A Revolução Gloriosa de 1688–1689 na Inglaterra contribuiu para fortalecer limitações parl
- A Declaração de Independência das Treze Colônias e a Declaração dos Direitos do Homem e do
- A Revolução Haitiana começou com rebeliões de escravizados na colônia francesa de Saint-Do
- Os movimentos de unificação italiana e alemã, concluídos no século XIX, constituíram proce
- A chamada segunda industrialização difundiu setores como eletricidade, aço e química, sem 
- O socialismo científico de Marx e Engels e o anarquismo de Bakunin defendiam igualmente qu
- A Primeira Guerra Mundial começou em 1914 após o atentado de Sarajevo, mas alianças milita
- As Revoluções Russas de Fevereiro e Outubro de 1917 fundaram formalmente a União Soviética
- O Holocausto ocorreu no contexto de perseguição e extermínio sistemáticos promovidos pelo 
- A Conferência de Bandung, em 1955, e a Resolução 1514 (XV), adotada em 1960, foram instrum
- A Guerra Fria envolveu confrontos por intermediários, competição ideológica e corrida nucl
- A queda do Muro de Berlim em 1989 determinou a dissolução formal imediata da União Soviéti
```

