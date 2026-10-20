# Pedidos de questões extras, lote 6 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 23276 tópicos de edital.

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

## Pedido 1: Educação Física escolar e cultura corporal (`educacao-fisica.escola-cultura-corporal`, 517 tópicos de edital)

```
Matéria: educacao-fisica.escola-cultura-corporal (Educação Física escolar e cultura corporal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Educação Física escolar, cultura corporal e linguagem": 21
- "2. Metodologias, planejamento, avaliação e aprendizagem motora": 20
- "3. Brincadeiras, danças, ginásticas, circo, lutas e capoeira": 19
- "4. Esportes, lógica interna e práticas corporais de aventura": 18
- "5. Atividade física, saúde, inclusão e dimensões ético-sociais": 22

Já existem (não repita):
- Uma turma vivenciou capoeira e, depois, analisou cantos, formas de saudação e registros de
- Um estudante de 32 anos matriculou-se na educação básica e questionou a participação práti
- Ao elaborar o mapa curricular da Educação Física do ensino fundamental, uma equipe precisa
- Em uma aula de dança, os estudantes comparam como gestos e formações expressam pertencimen
- Uma estudante com deficiência motora quer participar de um jogo coletivo. A escola dispõe 
- Antes de iniciar uma unidade sobre jogos populares, o docente observa como a turma cria re
- Durante jogos reduzidos de handebol, a turma apresenta dificuldade para criar linhas de pa
- Uma estudante precisa receber passes enquanto colegas mudam de direção e adversários tenta
- Depois de um arremesso, o estudante ouve 'a bola atingiu a parte externa do alvo' e 'você 
- Em uma sequência de ginástica, uma turma apresentou evolução consistente em equilíbrio e c
- Um projeto de capoeira inclui roda, instrumentos, movimentos e discussão da trajetória afr
- Uma escola pretende introduzir malabares com lenços e equilíbrio no chão, articulando cria
- Em um jogo cooperativo, a turma transporta coletivamente objetos e decide como redistribui
- Uma sequência sobre dança de matriz africana envolve vivência de ritmos, escuta de represe
- Uma turma cria, em grupo, sequências de movimentos acessíveis com arcos e fitas, sem class
- Durante um jogo de handebol adaptado, estudantes precisam avançar coletivamente e proteger
- Um grupo estuda beisebol e percebe que o ataque rebate a bola para percorrer bases, enquan
- Uma escola reúne provas de arremesso de peso e bocha. Ao classificar ambas pela lógica esp
- Um docente afirma que tênis de mesa e basquetebol pertencem à mesma categoria porque os do
- Ao planejar experiências de aventura para 6º a 9º anos, uma equipe busca seguir a progress
- Uma escola decide atribuir nota de saúde a estudantes de 13 anos conforme cada um consiga 
- Uma campanha escolar chama deslocamentos a pé e brincadeiras espontâneas de 'exercício fís
- Um estudante com deficiência visual foi orientado a assistir às atividades com bola porque
- Em um projeto de percurso de aventura, há um trecho escorregadio após chuva, mas a turma d
- Uma turma analisa cobertura midiática de esportes femininos, debate estereótipos e reorgan
- Ao revisar o currículo do ensino fundamental, uma equipe identifica brincadeiras e jogos, 
- Como uma escola dispõe de pequeno espaço para atividades práticas, pode retirar Educação F
- Após vivenciar danças locais, estudantes discutem memória, pertencimento e modificações hi
- Em uma turma heterogênea, a avaliação mais fiel à Educação Física escolar é a que atribui 
- Uma professora classifica beisebol como campo e taco e basquetebol como invasão, embora am
- Um planejamento inclui malabarismo com lenços e conclui que deve registrar a unidade temát
- Durante arremessos a alvos, o retorno 'a bola caiu a dois metros' informa o resultado, enq
- Se uma avaliação é chamada formativa, o docente deverá registrar os resultados somente apó
- Ao transformar uma brincadeira competitiva em desafio comum, uma turma pode alterar critér
- Um projeto sobre capoeira analisa movimentos de ataque e defesa, mas pode dispensar música
- Uma versão de voleibol com rede baixa e bola mais lenta pode manter problemas de posiciona
- Em uma atividade com bocha e ginástica artística competitiva, os dois esportes pertencem à
- Em jogos de invasão, ensinar criação de linhas de passe e ocupação de espaços livres traba
- Na faixa de 5 a 17 anos, a recomendação da OMS de média de 60 minutos diários de atividade
- Uma proposta de saúde escolar pode mencionar que a OMS recomenda a pessoas de 5 a 17 anos 
- Para assegurar igualdade em Educação Física, a escola deve oferecer apenas o mesmo materia
- Na progressão específica de práticas corporais de aventura nos anos finais da BNCC, vivênc
- Um aluno de 31 anos exerce jornada de quatro horas e pede a prática facultativa em Educaçã
- Quando estudantes recriam uma brincadeira de matriz indígena depois de conhecer seus signi
- Durante uma trilha pedagógica, o docente decide omitir análise de riscos e impactos ambien
```

## Pedido 2: Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto (`informatica.arquitetura-software`, 516 tópicos de edital)

```
Matéria: informatica.arquitetura-software (Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de arquitetura, estilos e atributos de qualidade": 18
- "2. Arquitetura em camadas, aplicações web e interoperabilidade": 23
- "3. MVC, padrões de apresentação e arquitetura centrada no domínio": 17
- "4. Monólitos, SOA, microsserviços e consistência distribuída": 21
- "5. Padrões GoF, atribuição de responsabilidades GRASP e leitura de casos": 21

Já existem (não repita):
- Um sistema tem módulos de interface, regras de reservas e acesso ao armazenamento; todos s
- Uma rotina de processamento recebe registros, normaliza campos e encaminha o resultado par
- Uma aplicação web adota MVC. Ao receber pedido de reserva, qual componente deve tratar a i
- Um sistema publica um evento quando uma compra é confirmada; faturamento e notificações o 
- No projeto de aplicação em camadas, uma requisição chega ao frontend e uma regra deve veri
- A equipe quer permitir que um serviço seja acessado por diferentes aplicações mediante con
- Uma aplicação executa funcionalidades em um artefato de implantação único, mas possui módu
- Um cálculo de desconto precisa alternar entre políticas promocionais sem alterar a classe 
- Um componente mantém uma lista de interessados e os avisa quando um pedido muda de situaçã
- Uma camada de apresentação acessa diretamente o repositório de dados, saltando uma camada 
- Duas aplicações conseguem trocar mensagens JSON válidas, mas uma interpreta preço em centa
- Um sistema web possui várias ações de controladores, mas concentra o primeiro recebimento,
- Uma aplicação precisa executar a mesma regra de aprovação por navegador, rotina agendada e
- Em um conjunto de serviços, há necessidade de uma entrada unificada para clientes externos
- Chamadas a um serviço de terceiros falham repetidamente e continuam consumindo threads enq
- Pedido, cobrança e estoque possuem persistência independente. Um coordenador explícito man
- Uma aplicação precisa disponibilizar a mesma interface de leitura de dados e adicionar com
- Em interface reativa, propriedades da tela são sincronizadas com um objeto que expõe estad
- A equipe decidiu aplicar database per service em quatro microsserviços. Qual desenho prese
- Em Java, uma classe abstrata define a ordem de validar, executar e registrar, permitindo q
- Um relatório pode ser exibido por tela, impresso ou enviado como arquivo; cada modo pode c
- No catálogo GoF, qual relação apresenta corretamente as quantidades e a classificação dos 
- Dois serviços precisam manter estoque e pedidos consistentes por eventos. Não há coordenad
- Um legado monolítico será modernizado por partes. As novas solicitações para módulos migra
- A classe Pedido conhece seus itens e respectivos preços; uma equipe propõe calcular o tota
- Um sistema cliente-servidor pode executar cliente e servidor no mesmo computador durante t
- Uma aplicação possui camadas de apresentação, negócio e dados; portanto, sua implantação d
- No MVC de aplicação web, a View é responsável pela representação de informações, e o Contr
- A arquitetura serverless recebeu esse nome porque suas funções executam sem qualquer servi
- Uma aplicação monolítica que mantém módulos de catálogo, circulação e relatórios bem organ
- Em um editor, uma classe pode usar o padrão Command para representar a solicitação de salv
- Uma camada de apresentação que chama diretamente um componente de persistência, saltando a
- Duas aplicações que trocam XML válido garantem interoperabilidade semântica, ainda que uma
- Ao instalar um API gateway, uma equipe elimina a necessidade de service discovery em qualq
- Na arquitetura hexagonal, a camada de aplicação pode depender de interface abstrata de per
- Um circuito em estado open deve continuar enviando normalmente todas as chamadas à dependê
- No padrão GoF Abstract Factory, uma família de objetos relacionados pode ser criada por um
- Em MVC, Model, View e Controller são necessariamente distribuídos entre três servidores fí
- Uma saga pode manter a consistência de um processo que cruza serviços por meio de transaçõ
- A lei de Conway descreve a tendência de a estrutura dos sistemas refletir padrões de comun
- Num cenário de múltiplas filiais produzindo eventos, consistência eventual significa que d
- Para diminuir memória em uma aplicação com milhões de representações idênticas de caracter
- Em aplicações que usam State, a escolha do comportamento ocorre por algoritmo de desconto 
- Na aplicação de GRASP, Information Expert sugere que uma classe que conhece os itens e pre
- Uma aplicação de microsserviços consegue manter autonomia plena mesmo quando todos os serv
```

## Pedido 3: Pediatria e neonatologia: crescimento, desenvolvimento, recém-nascido e doenças comuns da infância (`medicina.pediatria-neonatologia`, 509 tópicos de edital)

```
Matéria: medicina.pediatria-neonatologia (Pediatria e neonatologia: crescimento, desenvolvimento, recém-nascido e doenças comuns da infância)
Capítulos e quantas questões quero em cada um (total 100):
- "Crescimento, desenvolvimento, puericultura e problemas ortopédicos pediátricos": 17
- "Aleitamento materno, alimentação complementar e avaliação nutricional": 17
- "Neonatologia clínica, reanimação, icterícia e prematuridade": 17
- "Triagens neonatais e erros inatos do metabolismo": 17
- "Doenças respiratórias, diarreia e infecção urinária na infância": 16
- "Exantemas, anemias, vulnerabilidades e direitos da criança": 16

Já existem (não repita):
- Sobre a avaliação antropométrica na infância, assinale o procedimento mais adequado para i
- Durante a consulta, a mãe relata que a criança deixou de pronunciar palavras que já dizia 
- Uma criança saudável completa seis meses e mantém aleitamento materno. Segundo a orientaçã
- Em reanimação neonatal, após cuidados iniciais, um recém-nascido permanece em apneia. Qual
- O teste do pezinho realizado no âmbito do PNTN tem como finalidade principal:
- Em bronquiolite viral aguda típica, sem suspeita de coinfecção bacteriana, o princípio ter
- Uma criança com diarreia aguda apresenta desidratação sem gravidade, consegue ingerir líqu
- Em lactente com história de apresentação pélvica ao nascimento, exame atual sem instabilid
- Menina com dieta pobre em ferro apresenta anemia microcítica, porém está com infecção infl
- Recém-nascido com icterícia identificada às 14 horas de vida é avaliado na maternidade. As
- Neonato com boa vitalidade ao nascer apresenta, após as primeiras mamadas, vômitos recorre
- Em criança com asma persistente, sintomas recorrentes e necessidade de prevenção de exacer
- Em lactente febril sem foco aparente e suspeita de ITU, o exame microbiológico deve ser in
- Em contexto epidemiológico que exige vigilância, um escolar apresenta febre, exantema macu
- Prematuro nascido com 32 semanas, agora com idade cronológica de 20 semanas, é avaliado po
- Lactente de oito meses recebe somente fórmula e papas peneiradas, não aceita texturas e ap
- Recém-nascido com icterícia prolongada apresenta fezes muito claras e urina escura. Qual c
- Durante reanimação de RN com 35 semanas, apesar de ventilação com pressão positiva comprov
- Um lactente de quatro meses, previamente hígido, apresenta primeiro episódio de sibilância
- Escolar apresenta anemia microcítica persistente, sem resposta hematológica esperada a fer
- Durante atendimento, surgem lesões e relato incompatíveis com trauma acidental, levantando
- Crescimento e desenvolvimento são sinônimos: ambos se restringem ao aumento de peso e esta
- Perda de habilidade de linguagem anteriormente adquirida é sinal de alerta do neurodesenvo
- A recomendação brasileira é amamentação exclusiva até seis meses, com introdução de alimen
- O escore de Apgar aos cinco minutos deve ser aguardado para decidir se um recém-nascido ap
- Um teste do pezinho positivo deve ser seguido por confirmação diagnóstica e acompanhamento
- Bronquiolite viral aguda típica tem o tratamento predominantemente baseado em medidas de s
- A obrigação de comunicação ao Conselho Tutelar só existe após confirmação judicial de maus
- Um valor antropométrico isolado pode ser compatível com normalidade, mas a queda persisten
- A ferritina, por ser específica de ferro, não sofre alteração pela resposta inflamatória e
- A icterícia iniciada durante as primeiras 24 horas de vida merece avaliação por potencial 
- A coleta do teste do pezinho antes de 48 horas pode alterar o desempenho de algumas dosage
- Na asma persistente, o broncodilatador de alívio, utilizado isoladamente por tempo indefin
- A ausência de disúria relatada por lactente febril não exclui infecção urinária, pois os s
- O sarampo pode ser suspeitado diante de febre, exantema maculopapular e sintomas respirató
- A idade corrigida auxilia a interpretação do desenvolvimento em prematuros, mas a presença
- A introdução de alimentos complementares aos seis meses implica cessação do aleitamento, p
- Em reanimação neonatal, persistência de frequência cardíaca inferior a 60/min após ventila
- Urina muito escura e fezes acólicas em neonato ictérico favorecem suspeita de colestase, d
- Um teste do pezinho dentro do padrão esperado exclui qualquer erro inato do metabolismo qu
- Na criança com diarreia, a escolha entre Planos A, B e C deve se basear na avaliação clíni
- Diante de exantema purpúrico não descorável, febre, prostração e hipotensão, o reconhecime
```

## Pedido 4: Inferência estatística e testes de hipóteses (`estatistica.inferencia-testes-hipoteses`, 506 tópicos de edital)

```
Matéria: estatistica.inferencia-testes-hipoteses (Inferência estatística e testes de hipóteses)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Amostragem, distribuições amostrais e estimação pontual": 20
- "2. Estimação intervalar, precisão e tamanho da amostra": 25
- "3. Lógica dos testes, significância, poder e proporções": 22
- "4. Testes de médias, dados pareados e qui-quadrado": 20
- "5. Testes não paramétricos, escolha do procedimento e limites": 13

Já existem (não repita):
- Uma pesquisa sorteia 80 pessoas e registra idade média de 37 anos. Qual classificação expr
- Um procedimento de estimação produz uma média esperada de 22 quando o parâmetro populacion
- Ao estimar a variância de uma população normal a partir de amostra de 12 valores, busca-se
- O desvio padrão populacional de uma medida é 16. Em amostra aleatória de 64 medições indep
- Uma população apresenta distribuição assimétrica, mas tem média e variância finitas. Para 
- Uma amostra independente apresenta média 70, tamanho 100 e desvio padrão populacional conh
- Uma amostra de 400 observações independentes produz proporção amostral de 0,50. Pelo inter
- Depois de calcular intervalo de confiança de 95% para a média, uma pessoa solicita interpr
- Uma análise busca detectar se a proporção de defeitos aumentou em relação a 0,10, sem inte
- Uma análise utiliza significância de 5% e apresenta poder de 80% para detectar efeito de t
- Para estimar uma proporção com margem de erro de 5 pontos percentuais e 95% de confiança, 
- Planeja-se estimar média populacional com desvio padrão conhecido 12, margem máxima 3 e co
- Em 200 observações independentes, 120 apresentam a característica. Para testar H0:p=0,5, q
- Duas amostras independentes apresentam 60 respostas favoráveis entre 100 no grupo 1 e 40 e
- Um conjunto independente de 16 medidas com distribuição populacional aproximadamente norma
- Dez pessoas têm uma medida registrada antes e depois de uma atividade. As diferenças depoi
- Duas amostras independentes de variáveis quantitativas foram obtidas de populações aproxim
- Uma tabela de contingência apresenta três categorias de faixa etária e quatro categorias d
- Em 100 ocorrências distribuídas em quatro categorias, a hipótese fixa probabilidades de 25
- Um teste bilateral fornece valor-p igual a 0,032. A pessoa responsável precisa decidir tan
- Em amostra Bernoulli independente, observam-se somente a sequência completa de respostas e
- Em dez ensaios Bernoulli independentes, ocorreram sete sucessos. Considerando p desconheci
- Dois estimadores de mesmo parâmetro apresentam: T1 não viesado com variância 9; T2 com vié
- Três grupos independentes fornecem uma variável ordinal com distribuições assimétricas. Bu
- Deseja-se construir teste de nível fixado para distinguir duas hipóteses completamente esp
- Uma amostra aleatória simples de 100 respostas binárias apresenta 63 sucessos. A fração 0,
- Uma pesquisa coleta respostas exclusivamente de pessoas voluntárias que acessam um formulá
- Para observações independentes com variância populacional finita, elevar o tamanho de uma 
- Se uma amostra fornece intervalo de confiança frequentista de 95% igual a [10;14], pode-se
- Para estimar uma proporção com 95% de confiança, margem 0,05 e p*=0,5, um cálculo que enco
- Em uma amostra de 16 observações independentes de população normal, sem conhecimento do de
- Se um teste com significância de 5% rejeita uma hipótese nula que é verdadeira, a decisão 
- Ao obter valor-p de 0,02, a pessoa responsável pelo teste conclui que a hipótese nula tem 
- Um teste tem poder de 0,90 para detectar uma diferença previamente especificada. Nessa alt
- Em um estudo pequeno, o teste bilateral apresenta valor-p de 0,12 ao nível de 5%. A decisã
- Em teste z de uma proporção com hipótese nula p=0,20, o erro padrão usado na estatística c
- Para construir intervalo usual de confiança da diferença de proporções independentes, a va
- Se vinte indivíduos fornecem duas medições cada, um teste t pareado trabalha com vinte dif
- Uma tabela categórica apresenta qui-quadrado elevado e valor-p pequeno ao testar independê
- Ao aplicar Kruskal–Wallis a três amostras independentes de distribuições com formas e disp
- Em modelo Bernoulli de respostas independentes com probabilidade comum p, a soma de sucess
- Para duas hipóteses simples completamente especificadas, o lema clássico de Neyman–Pearson
- No teste Kolmogorov–Smirnov de uma amostra contra distribuição normal, valores críticos us
- Realizar vinte testes independentes, cada um ao nível de 5%, mantém a probabilidade de pel
- Um intervalo de credibilidade de 95% é interpretado, sob posterior bayesiana corretamente 
```

## Pedido 5: Definição e conceito de direitos humanos (`direito.definicao-conceito-direitos-humanos`, 496 tópicos de edital)

```
Matéria: direito.definicao-conceito-direitos-humanos (Definição e conceito de direitos humanos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, terminologia, evolução histórica e estrutura normativa": 14
- "2. Fundamentos filosóficos e dignidade da pessoa humana": 26
- "3. Fontes, características, princípios e dimensões didáticas": 28
- "4. Proteção constitucional e eficácia vertical e horizontal": 16
- "5. Tratados de direitos humanos no Brasil, hierarquia e convencionalidade": 16

Já existem (não repita):
- Uma pessoa estuda a liberdade de expressão tanto na Constituição de seu país quanto em tra
- Ao elaborar uma política de atendimento respeitoso às pessoas, a equipe busca o fundamento
- Uma pessoa sofre coação ilegal à liberdade de locomoção e deseja empregar o instrumento co
- Em uma linha do tempo de proteção internacional, registra-se a Declaração Universal dos Di
- Uma cartilha agrupa direitos à educação, saúde e trabalho em uma das dimensões didáticas c
- Uma política de proteção rejeita restringir a titularidade de direitos humanos apenas a na
- Um estudo contrasta as revoluções liberais com a internacionalização pós-Segunda Guerra Mu
- No exame dos princípios constitucionais que orientam as relações internacionais brasileira
- Uma pessoa invoca proteção constitucional contra discriminação e recebe a resposta de que 
- Uma pessoa argumenta que certa lei não deveria poder legitimar tratamento degradante mesmo
- Em debate sobre consentimento para uma pesquisa, sustenta-se que usar uma pessoa apenas co
- Um pesquisador pretende verificar se uma convenção se tornou juridicamente válida conforme
- Um parecer organiza as fontes internacionais e encontra o art. 38, § 1º, do Estatuto da Co
- Uma prática reiterada de Estados é apresentada como costume internacional. Para reconhecer
- Uma norma interna admite duas interpretações razoáveis; uma restringe desnecessariamente p
- Uma associação privada exclui integrante sem lhe dar possibilidade de contestar a medida. 
- Um intérprete aplica direitos fundamentais a conflito contratual por meio das cláusulas ge
- Uma pessoa sustenta que o art. 5º, § 1º, da Constituição obriga a entrega instantânea e ir
- Uma convenção sobre direitos humanos será submetida ao rito especial para adquirir equival
- Uma lei ordinária conflitua com tratado de direitos humanos regularmente incorporado sem o
- Uma convenção internacional foi assinada pelo representante do Estado, mas ainda aguarda a
- Uma norma legal prevê prisão civil de depositário infiel e sua defesa alega que a Súmula V
- Um Estado descumpre tratado em vigor e invoca lei interna como única justificativa. Qual a
- Um parecer usa o art. 5º, § 2º, para sustentar que a Constituição admite direitos decorren
- Em um conflito entre liberdade de expressão e proteção da privacidade, um parecer afirma q
- Uma pessoa continua titular de direitos humanos mesmo quando seu Estado não reproduz integ
- A Declaração Universal dos Direitos Humanos, adotada em 1948, é tratado internacional rati
- Em uma classificação didática de direitos fundamentais, liberdade de expressão e participa
- Por serem universais, os direitos humanos asseguram exatamente as mesmas prerrogativas pol
- No art. 5º, LXVIII, da Constituição, o habeas corpus funciona como garantia de proteção à 
- As gerações de direitos humanos são sucessivas e excludentes: quando surgem direitos socia
- Na identificação de costume internacional, a prática geral deve estar acompanhada de aceit
- Toda declaração internacional de direitos humanos, por pertencer à chamada soft law, adqui
- A investigação positivista de validade normativa pode reconhecer uma lei como válida segun
- Os direitos fundamentais só podem produzir efeitos em relações entre o Estado e indivíduos
- A Declaração e Programa de Ação de Viena, no parágrafo 5º, afirma que os direitos humanos 
- O princípio da progressividade do art. 2º, § 1º, do Pacto de Direitos Econômicos, Sociais 
- O art. 5º, § 2º, da Constituição impede considerar o catálogo constitucional como lista fe
- Tratado internacional de direitos humanos aprovado por maioria absoluta em votação única d
- A Convenção Americana sobre Direitos Humanos, promulgada pelo Decreto nº 678/1992 e sem o 
- A Súmula Vinculante nº 25 eliminou formalmente as palavras referentes ao depositário infie
- A eficácia horizontal direta e a eficácia horizontal mediata diferem quanto à forma de inc
- A assinatura de tratado internacional pelo Executivo, antes das demais etapas de aprovação
- O controle de convencionalidade considera a compatibilidade do direito interno com obrigaç
- Por estabelecer um catálogo aberto de direitos, o art. 5º, § 2º, confere automaticamente f
```

## Pedido 6: Lei Orgânica Municipal: estrutura e conteúdo típico (`legislacao-municipal.lei-organica-municipal`, 495 tópicos de edital)

```
Matéria: legislacao-municipal.lei-organica-municipal (Lei Orgânica Municipal: estrutura e conteúdo típico)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Município na Federação e elaboração da Lei Orgânica": 17
- "2. Câmara Municipal, Prefeito e organização político-institucional": 17
- "3. Processo legislativo municipal e simetria constitucional": 17
- "4. Administração municipal, agentes públicos e patrimônio": 17
- "5. Finanças, orçamento, fiscalização e Tribunais de Contas": 16
- "6. Ordem econômica e social, simetria e estratégia de estudo por edital": 16

Já existem (não repita):
- A autonomia municipal abrange auto-organização, autogoverno, autolegislação e autoadminist
- A Lei Orgânica Municipal é submetida à sanção do prefeito depois de aprovada por maioria s
- Por legislar sobre assuntos de interesse local, o Município pode contrariar normas gerais 
- Para aprovar a Lei Orgânica originária, qual combinação respeita diretamente o art. 29 da 
- Uma cidade possui 15 vereadores. Para aprovação da LOM, qual número mínimo de votos favorá
- Qual das atividades abaixo se conecta mais diretamente ao art. 30, VIII, da Constituição?
- Ao comparar a Lei Orgânica com a Constituição Estadual, o candidato deve adotar qual crité
- A Câmara Municipal pode exercer funções de fiscalização e, ao mesmo tempo, administrar seu
- Como ocorre na União, cada Município possui um Poder Judiciário municipal próprio, dirigid
- A posse do prefeito e do vice-prefeito ocorre no dia 1º de janeiro do ano subsequente ao d
- Sobre a organização político-institucional do Município, assinale a afirmação compatível c
- Um vereador profere opinião vinculada ao exercício do mandato dentro da circunscrição do M
- Atribuição típica da Lei Orgânica quanto à Câmara, sem confundi-la com o regimento interno
- Em questão que trata de dupla vacância de prefeito e vice-prefeito, a providência de estud
- A iniciativa popular do art. 29, XIII, refere-se a pelo menos cinco por cento do eleitorad
- Todas as leis municipais passam pelo mesmo rito da Lei Orgânica, com dois turnos e interva
- A simetria constitucional obriga o Município a reproduzir literalmente todo artigo sobre o
- Um projeto aprovado pela Câmara sobre matéria sujeita a sanção segue, em regra, qual lógic
- Sobre sanção e promulgação no processo municipal, qual afirmação é adequada?
- Uma proposta popular de lei sobre tema local reúne assinaturas de 5% dos eleitores do Muni
- Uma Câmara pretende criar cargos e reorganizar secretarias do Executivo por proposta de ve
- Secretarias municipais integram, em regra, a administração direta, e a Lei Orgânica pode o
- O ingresso em cargo público efetivo no Município pode ser dispensado de concurso por simpl
- A existência de controle interno no Poder Executivo municipal dispensa o controle externo 
- Qual situação exemplifica matéria de administração direta municipal?
- No estudo de direitos, licenças e prazos para servidores de uma prefeitura, qual método re
- Uma questão pede identificar a função básica do regimento interno da Câmara em comparação 
- Quanto ao transporte coletivo de interesse local, a Constituição expressamente estabelece 
- O PPA, a LDO e a LOA têm funções distintas na organização do planejamento e orçamento muni
- Segundo o art. 31, o parecer prévio sobre as contas anuais do prefeito deixa de prevalecer
- A Emenda Constitucional 139/2026 explicitou, no art. 31, § 1º, vedação à extinção, criação
- Uma Câmara pretende rejeitar o parecer prévio sobre as contas anuais do prefeito. Qual quó
- Sobre a consulta às contas municipais, assinale a regra do art. 31, § 3º.
- De acordo com o art. 29-A, a folha de pagamento da Câmara Municipal, incluídos os subsídio
- Qual enunciado expressa corretamente a repartição do controle municipal?
- Cada Município brasileiro possui sua própria Lei Orgânica, razão pela qual o conteúdo típi
- O plano diretor e a Lei Orgânica são a mesma norma, pois ambos tratam de política urbana m
- A proteção municipal do patrimônio histórico-cultural local deve considerar a legislação e
- Em uma prova cujo edital informa expressamente a Lei Orgânica de Município específico, a m
- Assinale a correspondência correta entre instrumento e finalidade na ordem municipal.
- A respeito da atuação municipal na educação infantil e no ensino fundamental, assinale a a
- Diante de uma regra da Constituição Estadual que invade escolha municipal protegida direta
```

## Pedido 7: Atividade notarial e registral: teoria geral, tabelionato de notas e registro civil (`direito-notarial-registral.atos-notariais-registros-publicos`, 495 tópicos de edital)

```
Matéria: direito-notarial-registral.atos-notariais-registros-publicos (Atividade notarial e registral: teoria geral, tabelionato de notas e registro civil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, delegação e organização dos serviços extrajudiciais": 22
- "2. Princípios registrais e Lei de Registros Públicos": 23
- "3. Atos notariais, escrituras, procurações e documentação": 18
- "4. Inventário, divórcio e registro civil: atos extrajudiciais especiais": 17
- "5. Deontologia, fiscalização, centrais e responsabilidade": 20

Já existem (não repita):
- Após aprovação em seleção para titularidade de uma serventia extrajudicial, Helena pergunt
- Uma serventia tornou-se vaga e o órgão competente planeja a seleção para seu provimento. Q
- Em uma reunião sobre atribuições, discute-se quem pode lavrar uma ata notarial solicitada 
- Moradores de uma cidade querem lavrar escritura em tabelionato de outro Município, embora 
- Ao organizar o funcionamento do registro civil, uma titular questiona o horário mínimo ord
- Após casamento, uma pessoa solicita documento que reflita a dissolução posterior do víncul
- Um cidadão requer certidão de um registro público ordinariamente acessível, mas não explic
- O primeiro filho de uma família nasceu em uma cidade e seus pais residem em outra. Desejam
- Uma família sem declaração de pobreza solicita o primeiro traslado da certidão de nascimen
- Em análise de ingresso de título imobiliário, o oficial constata que o alienante indicado 
- Uma pessoa outorga procuração com poderes genéricos de administração e o mandatário preten
- Um tabelião redige escritura de alienação de bem imóvel, de valor superior ao limite geral
- Uma escritura de imóvel demanda documentos previstos em lei e regulamento. O interessado s
- Pessoa com deficiência visual comparece para outorgar procuração pública, compreende o neg
- Em pedido de escritura pública de compra e venda, a parte apresenta procuração particular 
- Todos os herdeiros de determinado espólio são capazes, estão de acordo e não há testamento
- Em inventário consensual, um dos herdeiros é menor. Todos os demais são capazes e não exis
- Herdeiros capazes e concordes pretendem inventário extrajudicial, mas o falecido deixou te
- Cônjuges desejam divórcio consensual por escritura pública. Têm filha menor e demonstram d
- Uma criança nasceu com vida, respirou por breve período e faleceu logo após o parto. A fam
- Uma irregularidade disciplinar pontual atribuída a titular ocorreu em setembro de 2026. A 
- O titular é demandado pela reparação de dano que teria causado culposamente na lavratura d
- Interessado utiliza a Central de Escrituras e Procurações para localizar eventual ato nota
- A corregedoria avalia aplicar penalidade disciplinar a registrador por descumprimento reit
- Após identificação de cobrança indevida de emolumentos, o delegatário invoca urgência do i
- Paulo foi aprovado no concurso para uma serventia e iniciará o exercício da delegação. Emb
- Uma escritura deve ser lavrada para partes residentes em estados diferentes. A livre escol
- Uma serventia de registro civil mantém plantão em domingos e feriados, além do atendimento
- A titular de registro de imóveis pode, por mera opção administrativa, acumular tabelionato
- Ao emitir certidão comum de assento público, o oficial explica ao solicitante que, como re
- Por se tratar de documento elaborado pelo registrador, a emissão de segunda via da certidã
- Em qualquer condição de renda, a família que registra o nascimento e pede a primeira certi
- Um casal pede, vinte dias após o nascimento, o registro na cidade onde ocorreu o parto, se
- Durante a qualificação de escritura de compra e venda, o tabelião exige identificação, ver
- O mandatário recebeu procuração geral para administrar os bens de Ana. Em razão dessa outo
- Uma ata notarial relata circunstâncias verificadas pelo tabelião em uma visita. Por ser at
- Na elaboração de uma escritura com participação de pessoa com deficiência, o tabelião deve
- Uma herdeira menor recebe parte ideal de cada bem inventariado, e o Ministério Público man
- Em inventário extrajudicial com testamento válido, a simples concordância dos herdeiros e 
- Cônjuges com filho incapaz resolveram judicialmente, antes da escritura, todas as questões
- Em inventário extrajudicial com herdeiro incapaz, o Ministério Público oferece impugnação 
- A corregedoria apura infração administrativa permanente que cessou depois de longo período
- O tabelião praticou ato em nome de seu substituto designado, e terceiro alega dano culposo
- A consulta inicial à Central de Escrituras e Procurações permite identificar a serventia, 
- A Lei 8.935/1994 obriga o juízo disciplinar a impor repreensão, depois multa, suspensão e,
```

## Pedido 8: Estatuto da Pessoa Idosa (`legislacao.estatuto-pessoa-idosa`, 493 tópicos de edital)

```
Matéria: legislacao.estatuto-pessoa-idosa (Estatuto da Pessoa Idosa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições preliminares, prioridades e Política Nacional da Pessoa Idosa": 19
- "2. Direitos fundamentais: saúde, alimentação, educação, trabalho e moradia": 19
- "3. Transporte, prioridade de tramitação e acesso à justiça": 18
- "4. Medidas protetivas, entidades, fiscalização e tutela coletiva": 17
- "5. Crimes contra a pessoa idosa, ação penal e atualizações legislativas": 27

Já existem (não repita):
- Uma pessoa completou 60 anos e procura atendimento público, mas recebe informação de que a
- No atendimento ordinário não emergencial de saúde, há pessoas idosas de 68 e de 84 anos ag
- Uma pessoa idosa precisa de alimentos, e há mais de um familiar juridicamente obrigado. De
- Uma pessoa idosa está em observação no hospital e deseja permanecer acompanhada. O profiss
- Em programa habitacional subsidiado com recursos públicos, o edital de inscrições pergunta
- Em ônibus coletivo público urbano regular, uma pessoa idosa com mais de 65 anos apresenta 
- Uma pessoa idosa com renda equivalente a dois salários mínimos procura bilhete em transpor
- A equipe de atendimento identifica ameaça aos direitos de pessoa idosa devido à omissão de
- Uma pessoa idosa com 67 anos requer benefício assistencial do art. 34, mas dispõe de meios
- Uma empresa nega emprego a candidata de 62 anos apenas pela idade, sem requisito profissio
- No transporte interestadual, as duas vagas gratuitas reservadas a pessoas idosas com renda
- Uma entidade filantrópica de longa permanência pretende receber mensalmente 85% do benefíc
- Uma entidade privada de acolhimento afirma que, por não pertencer à administração, não pre
- Em visita a instituição de longa permanência, encontra-se prova de desvio de repasse públi
- Uma pessoa idosa sofre recusa de operação bancária exclusivamente por motivo etário, sem j
- Uma filha, obrigada a prover necessidades essenciais, abandona pessoa idosa em estabelecim
- Um responsável retém cartão bancário de pessoa idosa para garantir pagamento de dívida pri
- Um fato ocorrido em 2026 configura crime previsto no Estatuto e possui pena máxima inferio
- Uma pessoa obrigada a cuidar da pessoa idosa a submete a condições degradantes, expondo su
- Uma administradora passa a aplicar a pensão de pessoa idosa em despesas próprias, retirand
- Um indivíduo aproveita-se da ausência de discernimento da pessoa idosa para induzi-la a ou
- Uma pessoa ameaça a idosa para obrigá-la a assinar doação de imóvel, embora a vítima conse
- Um tabelião lavra ato notarial envolvendo pessoa idosa sem discernimento, sem que compareç
- Um indivíduo presencia pessoa idosa em perigo iminente e deixa de prestar auxílio embora p
- Após apropriação de pensão de pessoa idosa, o investigado sustenta que a persecução do del
- Uma pessoa que completou 60 anos é abrangida pelo Estatuto da Pessoa Idosa mesmo que não r
- A preferência especial de pessoas maiores de 80 anos sobre as demais pessoas idosas preval
- Segundo o Estatuto, os alimentos devidos à pessoa idosa constituem obrigação solidária, po
- O benefício assistencial mensal de um salário mínimo previsto no art. 34 é concedido a tod
- Nos ônibus urbanos regulares abrangidos pelo art. 39, a legislação federal reserva dez por
- As duas vagas gratuitas por veículo previstas para o transporte interestadual destinam-se 
- As medidas protetivas do Estatuto podem ser aplicadas isoladamente ou em conjunto, com ate
- A Política Nacional do Idoso é regulamentada atualmente pelo Decreto nº 1.948/1996, que pe
- A Lei nº 14.364/2022 alterou a Lei nº 10.048/2000 para que acompanhantes ou atendentes pes
- O serviço de saúde que suspeita de violência contra pessoa idosa só está obrigado a notifi
- A pessoa idosa com 62 anos, parte em processo judicial, pode requerer prioridade de tramit
- Instituições não governamentais de acolhimento estão dispensadas de inscrição de programas
- Os crimes previstos no Estatuto da Pessoa Idosa são processados por ação penal pública inc
- Após a Lei nº 15.163/2025, crimes previstos no Estatuto cuja pena máxima não ultrapasse qu
- Desde a alteração legislativa de 2025, a exposição a perigo da saúde ou integridade da pes
- O art. 96, § 3º, considera crime a negativa de crédito motivada pelo superendividamento da
- Reter o cartão bancário de benefícios de pessoa idosa com a finalidade de assegurar pagame
- Coagir pessoa idosa capaz a outorgar procuração se enquadra necessariamente no art. 106, c
- O Ministério Público pode promover inquérito civil e ação civil pública para proteger inte
- Abandonar pessoa idosa em hospital, quando havia dever legal de prestar necessidades básic
```

## Pedido 9: Governança, qualidade e gestão de dados (`informatica.governanca-qualidade-dados`, 492 tópicos de edital)

```
Matéria: informatica.governanca-qualidade-dados (Governança, qualidade e gestão de dados)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, tipos de dados e modelos": 24
- "2. Governança de dados e responsabilidades": 25
- "3. Fontes, coleta, ingestão, metadados e transformação": 18
- "4. Qualidade de dados, dimensões e métricas": 17
- "5. Proteção, retenção e ciência de dados aplicada": 16

Já existem (não repita):
- Uma aplicação registra vendas em uma tabela com colunas fixas id, data e valor. Qual class
- Um sistema armazena documentos JSON cujas chaves descrevem itens e listas de tamanho variá
- Foi publicado um gráfico em imagem, sem tabela para reuso ou licença identificada. Qual af
- A direção aprova uma política sobre uso de cadastros; uma equipe depois configura as regra
- Um curador descreve o significado do campo saldo e registra seu domínio e unidade de medid
- Um cadastro contém 100 registros, dos quais 8 não possuem um campo obrigatório. Qual é a c
- Um arquivo exibe números de documentos com apenas os últimos dois dígitos visíveis; a apli
- Após definir uma regra comum para códigos de produto, uma área aplica a regra na importaçã
- Um conjunto possui 500 eventos, dos quais 25 excedem ocorrências já representadas por mesm
- Dois sistemas exibem para uma mesma transação o valor R$ 350, mas o comprovante confiável 
- Uma equipe recebe dados via API, carrega arquivos brutos no ambiente analítico e só depois
- Uma estrutura mantém decisões de padrões comuns em um núcleo e delega critérios locais aos
- Em uma matriz RACI, a pessoa indicada como A para aprovar a definição de receita líquida e
- Para auditar um indicador mensal, é preciso descobrir de quais tabelas vieram os valores e
- Um registro apresenta data 2026-02-30 no campo data de entrega, cujo domínio aceita apenas
- Um projeto usa uma tabela de correspondência protegida para relacionar códigos aleatórios 
- Uma regra de qualidade exige atualização em até duas horas. A base de eventos foi atualiza
- Ao combinar duas tabelas por codigo_cliente, uma contém duas linhas por cliente e a outra 
- Uma rotina incremental importa inclusões e atualizações de uma fonte, mas ignora exclusões
- Uma equipe quer divulgar microdados com cidade, profissão rara, mês de um evento e código 
- De 400 registros, 20 têm campo obrigatório vazio e 12 outros têm valor fora do domínio. Ne
- Um modelo para prever cancelamento usa, no treino, a variável motivo_final_do_cancelamento
- O processo de tratamento de dados pessoais terminou. Constatou-se obrigação legal de guard
- Uma pesquisa sobre evento raro reúne 1.000 casos: 990 negativos e 10 positivos. Um classif
- Uma operação idempotente de ingestão recebe duas vezes o mesmo evento com a mesma chave es
- Um arquivo de áudio é tipicamente não estruturado quanto ao seu conteúdo principal, embora
- Uma tabela relacional com colunas estáveis deve ser classificada como não estruturada porq
- Na governança, definir quem autoriza o acesso a um conjunto é questão de direitos decisóri
- No modelo compartilhado de governança, a deliberação precisa ocorrer por votação colegiada
- Uma descrição que registra o tipo, o nome e a codificação de uma coluna constitui metadado
- Se 12 de 150 registros apresentam campo obrigatório ausente, a completude desse campo é de
- A substituição de nomes por códigos recuperáveis por tabela separada caracteriza usualment
- Como dados mestres tratam entidades partilhadas entre aplicações, sua gestão requer necess
- Um processo ELT pode manter dados brutos no destino e transformar ali os campos, desde que
- O dicionário de dados é suficiente para comprovar a origem de um indicador porque contém o
- Um valor pode ser válido por respeitar seu domínio e, ao mesmo tempo, estar incorreto em c
- A abertura de dados em formato processável por máquina dispensa a avaliação de restrições 
- Em uma amostra de 80 valores conferidos com fonte confiável, 72 coincidem com o fato. A ac
- Em um join por campo não único, duas linhas de uma tabela que encontram três correspondênc
- Um sistema que registra todas as retransmissões de um evento como novas vendas permanece i
- Se as 15 falhas de preenchimento e as 5 falhas de domínio ocorrem em registros distintos d
- O art. 16 da Lei nº 13.709/2018 determina eliminação após o término do tratamento e não ad
- Se um classificador prevê negativo para todos os 1.000 casos de um conjunto com dez positi
- O uso, no treino de um preditor, de atributo somente conhecido depois do evento alvo é ace
- Um catálogo que indica o responsável pelo conjunto comprova, por si só, todos os filtros e
```

## Pedido 10: Igualdade racial e crimes de preconceito (`legislacao.igualdade-racial-crimes-preconceito`, 492 tópicos de edital)

```
Matéria: legislacao.igualdade-racial-crimes-preconceito (Igualdade racial e crimes de preconceito)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constituição, igualdade material e conceitos do Estatuto": 18
- "2. Direitos à saúde, educação, cultura, religião, terra e trabalho": 18
- "3. Sinapir, participação social, acesso à Justiça e financiamento": 18
- "4. Lei nº 7.716/1989: crimes discriminatórios e respectivas penas": 22
- "5. Injúria racial, marcos legislativos e interpretação do STF": 24

Já existem (não repita):
- Um programa de ações afirmativas utiliza o conceito de população negra expresso no Estatut
- Uma entidade comunitária e a administração pública discutem quem deve promover a igualdade
- Uma empresa cria programa especial de qualificação para corrigir desigualdades raciais his
- Um município deseja ingressar no Sistema Nacional de Promoção da Igualdade Racial. Como oc
- Uma unidade de saúde deseja formular ação específica para enfrentar desigualdades raciais 
- Pessoa praticante de religião de matriz africana encontra-se internada em unidade hospital
- Uma escola privada de ensino médio analisa sua obrigação de abordar a história geral da Áf
- Um trabalhador recebe formulário de admissão atualizado com campo para identificação de se
- Uma produção publicitária federal será contratada por entidade abrangida pelo art. 46 do E
- Ao analisar recusa discriminatória de atendimento motivada por procedência nacional, uma p
- Após janeiro de 2023, alguém dirige a uma pessoa ofensas à dignidade baseadas em sua cor. 
- Uma pessoa habilitada tem sua promoção funcional bloqueada por discriminação racial em amb
- Uma empresa privada nega, por preconceito racial, os equipamentos necessários a determinad
- A direção de uma escola privada recusa a matrícula de uma criança de 16 anos por preconcei
- Uma pessoa é impedida de hospedar-se em hotel por sua origem étnica. A conduta discriminat
- Em uma reunião pública, uma pessoa convoca terceiros a recusar contato comercial com deter
- Um material de propaganda é produzido com a cruz suástica para promover ideologia nazista.
- Um usuário publica nas redes sociais convocação racista que se enquadra no art. 20 da Lei 
- Durante atividade esportiva destinada ao público, uma pessoa pratica ato de discriminação 
- O autor de ofensa racial individual alega que a prática ocorreu como brincadeira entre con
- Um agente público, a pretexto de atuar em suas funções, incita discriminação enquadrada no
- Vítima de crime de racismo participa de ato processual cível decorrente da mesma prática. 
- Um estudo compara a Lei Caó, de 1985, e a lei de crimes raciais de 1989. Qual descrição re
- Em um histórico legislativo, afirma-se que a Lei nº 9.459/1997 e a Lei nº 14.532/2023 tive
- Um parecer examina atos de homofobia praticados após 13/06/2019 e pergunta por que podem s
- Após recusa discriminatória de acesso a serviço, um relatório afirma que a Constituição pr
- Uma empresa privada informa que não pode desenvolver ações afirmativas próprias, pois o Es
- No planejamento curricular do ensino médio privado, o estudo da história da população negr
- Uma instituição educacional afirma que o art. 22 do Estatuto obriga todas as escolas priva
- Um Estado avalia participar do Sinapir. A Lei nº 12.288/2010 admite sua integração mediant
- Ao citar a base legal do Estatuto da Igualdade Racial, um documento informa que sua instit
- Uma comunidade remanescente de quilombo ocupa suas terras e invoca o Estatuto. O art. 31 r
- Um empregador afirma que os campos étnico-raciais previstos no art. 39, § 8º, do Estatuto 
- Um município institui conselho de promoção da igualdade étnica nos moldes do art. 50, perm
- Depois de receber denúncia de discriminação, uma ouvidoria permanente prevista no art. 51 
- Uma concessionária impede, por preconceito étnico, pessoa devidamente habilitada de acessa
- Uma escola recusa por preconceito a inscrição de aluno menor de 18 anos. Segundo o art. 6º
- A elaboração de material com suástica para divulgar o nazismo pode configurar a figura esp
- Para interromper divulgação racista em página da internet alcançada pelo art. 20, § 2º, o 
- Uma sentença condenatória por crime da Lei nº 7.716/1989 pretende determinar suspensão do 
- Um funcionário público pratica crime do art. 8º da Lei nº 7.716/1989 no exercício do cargo
- Ao aplicar a Lei nº 7.716/1989, deve-se considerar, na interpretação do art. 20-C, tratame
- Após a Lei nº 14.532/2023, a ofensa individual por raça permanece tipificada no art. 140, 
- O entendimento sobre imprescritibilidade da injúria racial foi reconhecido pelo STF no HC 
- Segundo a ADO 26 e o MI 4.733, a aplicação da Lei nº 7.716/1989 à homofobia e à transfobia
```

## Pedido 11: Enfermagem na saúde da mulher, da criança e do idoso (`enfermagem.saude-ciclos-vida`, 491 tópicos de edital)

```
Matéria: enfermagem.saude-ciclos-vida (Enfermagem na saúde da mulher, da criança e do idoso)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Saúde integral da mulher: reprodução, prevenção e climatério": 17
- "2. Gestação e pré-natal: acompanhamento e identificação de riscos": 18
- "3. Parto, puerpério, amamentação e primeiros cuidados ao recém-nascido": 19
- "4. Criança e adolescente: puericultura, desenvolvimento e agravos prevalentes": 21
- "5. Pessoa idosa e pessoa com deficiência: funcionalidade, autonomia e cuidado em rede": 25

Já existem (não repita):
- Uma mulher deseja evitar gravidez e reduzir o risco de infecções sexualmente transmissívei
- Durante ação de rastreamento do câncer do colo uterino, uma pessoa assintomática de 32 ano
- Uma mulher de 58 anos, sem sintomas mamários, busca orientação sobre rastreamento populaci
- Uma pessoa descobre gravidez com oito semanas e pergunta quando iniciar acompanhamento pré
- Uma gestante iniciou pré-natal com teste não reagente para sífilis. Qual programação repre
- Uma gestante com 22 semanas pergunta sobre a vacina dTpa, apesar de ter recebido a vacina 
- Uma família pergunta se precisa oferecer água a um lactente saudável de quatro meses em al
- Um recém-nascido saudável está com quatro dias de vida e ainda não fez a triagem neonatal 
- Aos seis meses, um bebê saudável inicia alimentação complementar e permanece em aleitament
- Em puericultura, uma criança apresenta crescimento ponderal satisfatório, porém deixou de 
- Uma criança com diarreia apresenta sonolência acentuada, pouca urina e dificuldade para be
- Um adolescente de 15 anos comparece sozinho para conversar sobre sexualidade e vacinação, 
- Uma pessoa idosa decide livremente sobre seu tratamento, mas precisa de ajuda física para 
- Uma pessoa de 79 anos recebe escore 12 no IVCF-20 na atenção primária. Qual interpretação 
- Pessoa idosa previamente orientada passa, em dois dias, a alternar períodos de desatenção 
- Uma mulher de 51 anos apresenta fogachos e ciclos menstruais irregulares há seis meses, ma
- Após falha do preservativo em relação recente, uma mulher pede orientação e teme que a con
- Gestante com 33 semanas relata cefaleia intensa, visão embaçada e pressão arterial elevada
- Na conferência vacinal, gestante com 29 semanas já recebeu dTpa nesta gestação, mas ainda 
- Após o parto, recém-nascido apresenta ausência de respiração efetiva e hipotonia. A famíli
- Uma puérpera apresenta sangramento vaginal volumoso, taquicardia e palidez duas horas após
- Puérpera amamenta de maneira exclusiva, está amenorreica, mas o bebê completou sete meses.
- Durante consulta, um lactente de dez meses apresenta febre, esforço respiratório com tirag
- Pessoa com deficiência auditiva comparece para seguimento e indica que prefere comunicação
- Pessoa idosa com escore IVCF-20 de 18 relata três quedas e dificuldade para administrar se
- Uma mulher de 45 anos relata nódulo mamário endurecido que cresce há dois meses. A investi
- Gestante de 21 semanas, vacinada contra dTpa na gestação atual, já está na faixa recomenda
- Após parto sem intercorrência, recém-nascido respirando adequadamente, com bom tônus, pode
- Uma família pode oferecer água ao bebê saudável de cinco meses em aleitamento materno excl
- A criança de dois anos com peso adequado ainda precisa de avaliação de linguagem, interaçã
- Uma pessoa de 76 anos que necessita de auxílio para subir escadas deve ser tratada como in
- Gestante com diagnóstico de alto risco e seguimento especializado deve manter vínculo com 
- Puérpera com dor e vermelhidão localizada em uma mama, mas clinicamente estável, deve inte
- Uma pessoa assintomática de 29 anos com colo uterino e DNA-HPV oncogênico negativo pode se
- Mulher de 54 anos com descarga papilar sanguinolenta unilateral deve aguardar a próxima ma
- Em pessoa idosa com IVCF-20 igual a nove pontos, a estratificação corresponde a risco mode
- Na introdução alimentar de criança de um ano, pequenas quantidades de açúcar adicionado sã
- Adolescente de 16 anos pode ser acolhido individualmente para discutir saúde sexual, prese
- Uma puérpera amenorreica que amamenta exclusivamente bebê de oito meses mantém preenchidos
- Um recém-nascido recebeu alta sem teste do pezinho e retorna no oitavo dia. A equipe deve 
- Gestante com cefaleia intensa, escotomas e pressão elevada pode ser mantida em acompanhame
- Pessoa idosa com IVCF-20 de 17 pontos e quedas recorrentes deve ser considerada de alto ri
- Quando pessoa com deficiência física solicita adaptações para exame clínico, a equipe pode
- Durante visita pós-parto, a puérpera relata ideias de autoagressão e sofrimento intenso. A
- Com a adoção do rastreamento por DNA-HPV, a citologia convencional deixou de ter qualquer 
```

## Pedido 12: Seguridade social: custeio, contribuições, segurados e dependentes (`direito-previdenciario.seguridade-custeio`, 490 tópicos de edital)

```
Matéria: direito-previdenciario.seguridade-custeio (Seguridade social: custeio, contribuições, segurados e dependentes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Origem, evolução, fontes e organização constitucional da seguridade": 29
- "2. Financiamento, contribuições sociais e salário de contribuição": 31
- "3. Previdência: filiação, segurados, manutenção da qualidade e dependentes": 15
- "4. Assistência social, BPC, requisitos e auxílio-inclusão": 14
- "5. Aplicação integrada, questões de enquadramento e distinções avançadas": 11

Já existem (não repita):
- Uma pessoa utiliza atendimento público de saúde, outra contribui para o RGPS e uma terceir
- Uma linha histórica destaca a criação de caixas previdenciárias de empregados ferroviários
- Um conselho de gestão da seguridade será constituído segundo a participação prevista no ar
- Ao estudar o financiamento da seguridade, uma pessoa afirma que ele se limita às contribui
- Uma lei é publicada criando contribuição social do art. 195. Qual prazo constitucional dev
- Uma proposta pretende ampliar um serviço da seguridade social sem prever a forma de financ
- Uma pessoa presta trabalho urbano subordinado, não eventual e remunerado a empresa. Em qua
- Uma pessoa de 19 anos não trabalha, não exerce atividade que a enquadre como segurada obri
- Uma pessoa de 66 anos, em vulnerabilidade econômica e sem contribuições previdenciárias, p
- Um beneficiário do BPC afirma que sua prestação é aposentadoria e, por isso, deveria gerar
- Uma pessoa com deficiência requer BPC. Qual descrição da regra de renda do art. 20 da LOAS
- Uma pessoa idosa solicita BPC e divide residência com sobrinho casado. Como definir o grup
- Após deixar emprego sujeito ao RGPS, uma pessoa interrompe contribuições e deseja conhecer
- Um antigo segurado empregado recolheu 120 contribuições mensais, sem interrupção, antes de
- Em um processo previdenciário de enquadramento familiar, coexistem cônjuge e pais economic
- Uma pessoa segurada falece, sem dependentes de classe I, deixando pais que alegam sustento
- Um empregado recebe remuneração mensal e décimo terceiro salário na mesma competência. Par
- Uma pessoa com deficiência busca BPC por impedimento que se prolonga no tempo, e sua avali
- Após alteração legislativa de 2025, uma pessoa pergunta se menor sob guarda judicial ainda
- Uma pessoa com deficiência moderada que recebia BPC passa a atividade remunerada permitida
- Uma pessoa com deficiência que teve BPC suspenso pelo ingresso em atividade remunerada há 
- Uma análise da Constituição atualizada reconhece as fontes do art. 195 e observa a reforma
- Uma lei ordinária pretende instituir nova fonte residual de custeio da seguridade fora das
- Uma proposta expande serviços de assistência e sustenta que a fonte adicional pode ser ide
- Uma portaria administrativa estabelece idade mínima de setenta anos para BPC, apesar de a 
- Uma pessoa que não possui filiação ao RGPS pode utilizar serviços públicos de saúde, pois 
- Ao instituir contribuição social do art. 195, uma lei pode determinar cobrança imediatamen
- O conceito de seguridade do art. 194 da Constituição reúne saúde, previdência social e ass
- Uma pessoa empregada pode escolher filiar-se ao RGPS como segurada facultativa para evitar
- Uma pessoa idosa de sessenta e sete anos, sem contribuições ao RGPS e em vulnerabilidade e
- O salário de contribuição de um trabalhador corresponde necessariamente ao valor líquido r
- Um segurado empregado deixa de trabalhar e de contribuir. Na hipótese geral do art. 15, II
- Para ampliar de doze para vinte e quatro meses o prazo do art. 15, II, basta que o segurad
- Uma pessoa que deixa de contribuir como segurada facultativa tem, em regra, manutenção da 
- Os pais são dependentes da primeira classe do RGPS e têm dependência econômica presumida m
- A LOAS considera, para o BPC, um grupo familiar legalmente delimitado e ligado à vida sob 
- A renda mensal per capita de até metade do salário mínimo é critério automático do BPC em 
- Benefícios eventuais da assistência social, nos termos do art. 22 da LOAS, são provisões s
- Como o auxílio-acidente é um benefício previdenciário, sua percepção conserva sem limite d
- Conforme a redação introduzida pela Lei 15.108/2025 no art. 16, § 2º, da Lei 8.213/1991, o
- Uma pessoa com deficiência que cumpre requisitos para auxílio-inclusão pode acumular esse 
- A Constituição, após a EC 103/2019, exige na diversidade de financiamento da seguridade a 
- A criação de contribuição residual de seguridade social pode ocorrer por simples decreto, 
- Uma pessoa com deficiência que recebia BPC e ingressa em atividade remunerada de até dois 
- O BPC é benefício contributivo do RGPS porque seu pagamento é operacionalizado pelo INSS; 
```

## Pedido 13: Gestão de documentos: classificação, temporalidade e preservação digital (`arquivologia.gestao-documentos`, 487 tópicos de edital)

```
Matéria: arquivologia.gestao-documentos (Gestão de documentos: classificação, temporalidade e preservação digital)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, funções e ciclo de vida": 23
- "2. Classificação arquivística e instrumentos de gestão": 19
- "3. Avaliação, temporalidade e destinação": 19
- "4. Documento digital, autenticidade e SIGAD": 23
- "5. Preservação digital, RDC-Arq e modelo OAIS": 16

Já existem (não repita):
- Uma equipe vai organizar a gestão dos documentos desde a criação até a decisão sobre sua g
- Um processo concluído permanece muito consultado para resolver pendências de execução. À l
- Após perder utilidade administrativa imediata, uma série documental foi selecionada por se
- Um conjunto documental deixa a unidade que o utilizava frequentemente e passa para custódi
- Uma instituição pretende adquirir um sistema que atenda ao e-ARQ Brasil, versão 2. Qual é 
- Uma equipe reúne os processos por funções, numera os itens cronologicamente e restringe o 
- Na elaboração de instrumentos documentais, são examinados registros de manutenção predial 
- Um arquivo digital apresenta o mesmo valor de hash em duas verificações, mas suspeita-se q
- Uma instituição possui um formulário nato-digital e uma antiga gravação em meio eletrônico
- A tabela de uma série fixa guarda de quatro anos depois do encerramento do processo. Um re
- Uma comissão federal prepara os instrumentos relativos às atividades-fim do seu âmbito. Se
- Uma fotografia integrante de série histórica perdeu a finalidade administrativa original. 
- Após a aprovação interna de uma listagem de eliminação, a equipe federal afirma que já pod
- Após a mudança do organograma, a instituição observa que o seu plano de classes passou a r
- Uma série de documentos de interesse fiscal deixa de ser utilizada na instrução cotidiana,
- Um sistema gerencia documentos desde sua captura, atribui classes e metadados, aplica rete
- Um projeto de gestão arquivística foi iniciado pela compra de uma plataforma, sem conhecer
- No modelo OAIS, o produtor envia um pacote ao repositório, que conserva outro pacote enriq
- Uma listagem de eliminação federal foi devidamente autorizada no regime de 2026. Após publ
- Uma proposta cita como vigentes as diretrizes de RDC-Arq instituídas pela Resolução CONARQ
- Um arquivo foi convertido de formato proprietário para outro aberto e bem documentado; o h
- Uma CPAD federal pretende realizar reunião ordinária. Segundo o Decreto nº 12.939/2026, ar
- A autorização para eliminar arquivos federais foi delegada à autoridade máxima de uma enti
- Um conjunto classificado funcionalmente como documento de atividade-fim possui valor perma
- Um lote de processos digitais chegará a repositório para preservação; o pacote inclui cont
- Uma unidade mantém sob consulta diária processos ainda em andamento. Classificá-los como d
- Um conjunto transferido do arquivo corrente ao intermediário passa, por esse ato, à catego
- Um registro produzido durante a execução de atividade institucional pode ser documento arq
- Um código decimal de classificação arquivística define, por seu próprio número, o prazo de
- Um documento cuja relevância histórica exige conservação definitiva pode permanecer acessí
- Se o administrador salvou versões de segurança de todos os arquivos digitais, já realizou 
- Para classificar documentos de uma mesma função após reorganização administrativa, o crité
- Uma tabela de temporalidade que determina guarda após a conclusão do processo deve ter seu
- No Executivo federal, a CPAD tem atribuição de elaborar instrumentos das atividades-fim e 
- A Resolução CONARQ nº 50/2022 limita o e-ARQ Brasil versão 2 a sistemas com documentos exc
- No modelo OAIS, o pacote de informação arquivística AIP tem finalidade de preservação, enq
- Ao converter um documento digital para formato aberto com registro técnico da operação, a 
- No programa de gestão, um diagnóstico prévio que identifique produtores, funções, sistemas
- O Decreto nº 12.939/2026 manteve em vigor, no Executivo federal, os arts. 1º a 16 do Decre
- De acordo com o Decreto nº 12.939/2026, a CPAD federal realiza ordinariamente reuniões sem
- Depois que uma autoridade máxima recebe delegação para autorizar eliminação documental fed
- Na eliminação de documentos federais regida pelo Decreto nº 12.939/2026, a CPAD e a autori
- A Resolução CONARQ nº 51/2023 passou a disciplinar apenas o desenvolvimento de SIGAD e man
- Se um conjunto possui valor permanente, o seu recolhimento digital deve buscar conservar t
- Um documento classificado como permanente deve perder a classificação funcional e todas as
```

## Pedido 14: Geodésia, sensoriamento remoto e geoprocessamento (`geoprocessamento.geodesia-sensoriamento-sig`, 486 tópicos de edital)

```
Matéria: geoprocessamento.geodesia-sensoriamento-sig (Geodésia, sensoriamento remoto e geoprocessamento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Referenciais geodésicos, elipsoide e altitudes": 20
- "2. Transformações, projeções, posicionamento e navegação": 20
- "3. Sensoriamento remoto e processamento digital": 20
- "4. Modelos espaciais, bancos geográficos e interoperabilidade": 20
- "5. Infraestrutura de dados espaciais e inferência espacial": 20

Já existem (não repita):
- Um levantamento realizado hoje precisa ser vinculado ao referencial geodésico oficial bras
- Ao preparar um perfil de terreno, um técnico precisa distinguir a superfície matemática us
- Em uma conversão vertical pelo modelo geoidal tradicional, a altitude elipsoidal de um pon
- Uma base legada em SAD69 deve ser integrada a um cadastro em SIRGAS2000. A equipe propõe a
- Uma planta topográfica será entregue em UTM para uma área próxima à longitude 48° O e situ
- Um receptor GNSS isolado obtém solução tridimensional e estima também o erro do relógio in
- Uma equipe distingue imagens óticas produzidas apenas a partir de iluminação solar de imag
- Um sensor fornece imagens com amostras codificadas em 8 bits por banda. Sem considerar dad
- Uma parcela apresenta reflectância 0,60 no infravermelho próximo e 0,20 no vermelho. O índ
- Um mapa raster representa cobertura terrestre pelos códigos 1 para mata, 2 para água e 3 p
- Um levantamento GNSS produz altura elipsoidal e o responsável pretende convertê-la com o m
- Duas bases antigas de pontos materializam referenciais clássicos distintos e apresentam di
- Dois receptores GNSS de desempenho equivalente observam igual quantidade de satélites, mas
- Uma imagem de satélite foi ajustada por pontos de controle, mas ainda mostra telhados e en
- Um analista utiliza reflectâncias de Landsat 8 para obter NDVI e recebeu a orientação de s
- Um cadastro registra um ponto exatamente sobre a fronteira de uma área protegida. Um filtr
- Um arquivo portátil deve armazenar tabelas, geometrias, referências espaciais e mosaicos d
- Um portal apresenta mapas temáticos prontos e outro permite consultar os polígonos origina
- Duas determinações altamente precisas do mesmo marco, uma referida à época 2000,4 e outra 
- Ao documentar uma conversão para UTM no hemisfério sul, um analista inclui falsas coordena
- Uma estação móvel recebe correções RTCM via NTRIP de infraestrutura de monitoramento GNSS 
- Uma amostra aleatória de 25 medidas independentes apresenta média de 100 unidades e desvio
- Dois pontos amostrados possuem valores 10 e 20, a distâncias 1 e 2 de um local não observa
- Uma análise de amostras de solo precisa estimar valores entre pontos e obter uma medida da
- Um catálogo público de geoinformação ainda menciona a antiga comissão de cartografia como 
- Uma rede de controle geodésico nacional documenta coordenadas SIRGAS2000 na época 2000,4 e
- Uma equipe utiliza altitude elipsoidal diretamente como altitude ortométrica porque ambas 
- Na produção de uma carta UTM convencional, cada fuso cobre 6° de longitude, de modo que o 
- Em uma cena recém-obtida por sensor óptico, os números digitais de seus pixels representam
- Um servidor WMS pode enviar ao cliente imagens cartográficas georreferenciadas produzidas 
- Uma transformação de coordenadas de SAD69 para SIRGAS2000 é concluída corretamente ao subs
- Ao processar pontos de uma rede geodésica antiga, um procedimento por grades pode represen
- Um sensor que registra cada amostra com 12 bits possui 256 níveis radiométricos distintos,
- Para Landsat 8, a composição padrão do NDVI emprega reflectância da banda 5 como NIR e da 
- Para reprojetar um raster categórico com classes representadas por números inteiros, a rea
- Um GeoPackage conforme o padrão OGC pode organizar feições vetoriais e tiles georreferenci
- Um cliente que recebe uma imagem PNG de WMS automaticamente obtém também as geometrias ori
- Para confrontar posições geodésicas de alta precisão medidas em épocas diferentes, pode se
- Um mapa que usa a solução hgeoHNOR2020 pode declarar indistintamente altitude normal e ort
- O acesso a correções GNSS transmitidas via NTRIP pode apoiar RTK, mas condições de multipe
- Duas camadas com o mesmo SRID declarado podem ter distâncias métricas confiáveis calculada
- Um intervalo de confiança bilateral de 95% para a média, construído corretamente sob o mod
- Ao interpolar dados altimétricos por IDW, o operador obtém automaticamente uma variância p
- A redação atualizada do Decreto nº 6.666/2008 vincula a INDE à Congeo e prevê compartilham
- Na krigagem ordinária, os pesos de interpolação são definidos apenas pela distância invers
```

## Pedido 15: Legislação Tributária: Fontes, Vigência, Aplicação, Interpretação e Integração (`direito-tributario.legislacao-tributaria`, 485 tópicos de edital)

```
Matéria: direito-tributario.legislacao-tributaria (Legislação Tributária: Fontes, Vigência, Aplicação, Interpretação e Integração)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito e fontes da legislação tributária": 20
- "2. Reserva legal (art. 97) e o que pode ser feito por decreto": 20
- "3. Vigência da legislação tributária": 20
- "4. Aplicação no tempo: irretroatividade e exceções": 20
- "5. Interpretação e integração": 20

Já existem (não repita):
- Segundo o art. 96 do CTN, a expressão "legislação tributária" compreende:
- O Código Tributário Nacional foi editado como lei ordinária, mas foi recepcionado pela Con
- Exige lei complementar para sua instituição:
- Medida provisória que majore imposto não excepcionado pela Constituição só produzirá efeit
- Fixar as alíquotas interestaduais do ICMS é atribuição de:
- Pelo art. 98 do CTN, os tratados e as convenções internacionais revogam ou modificam a leg
- São normas complementares das leis, dos tratados e dos decretos, segundo o art. 100 do CTN
- O contribuinte que segue uma instrução normativa da Receita Federal depois declarada ilega
- Segundo o CTN, o conteúdo e o alcance dos decretos:
- Segundo o art. 97 do CTN, somente a lei pode estabelecer, EXCETO:
- Equipara-se à majoração do tributo a modificação da sua base de cálculo que importe em tor
- Um Município atualizou o valor venal dos imóveis para fins de IPTU, por decreto, em percen
- Segundo a Súmula Vinculante 50, norma legal que altera o prazo de recolhimento de obrigaçã
- Pode ter suas alíquotas alteradas por ato do Poder Executivo, nos limites da lei:
- Pelo art. 97 do CTN, a dispensa ou redução de penalidades pode ser concedida por portaria 
- Salvo disposição em contrário, as decisões de órgãos de jurisdição administrativa com efic
- Salvo disposição em contrário, os convênios celebrados entre os entes federados entram em 
- A vigência, no espaço e no tempo, da legislação tributária rege-se, ressalvadas as regras 
- A legislação tributária de um Estado vigora fora de seu território nos limites em que lhe 
- Pelo art. 104 do CTN, entram em vigor no primeiro dia do exercício seguinte ao da publicaç
- O imposto de renda é exceção à anterioridade nonagesimal, mas deve respeitar a anteriorida
- Não se submetem nem à anterioridade anual nem à nonagesimal:
- A isenção concedida por prazo certo e em função de determinadas condições não pode ser rev
- A legislação tributária aplica-se imediatamente aos fatos geradores:
- A lei expressamente interpretativa aplica-se a ato ou fato pretérito, em qualquer caso, ex
- Uma empresa foi autuada com multa de 75% por deixar de entregar declaração. Antes do julga
- A lei que deixa de tratar um ato como contrário a exigência de ação ou omissão retroage me
- Sobre a aplicação da legislação tributária no tempo, assinale a correta.
- Na ausência de disposição expressa, a autoridade competente para aplicar a legislação trib
- O emprego da analogia não poderá resultar na exigência de tributo não previsto em lei, e o
- Interpreta-se literalmente a legislação tributária que disponha sobre, EXCETO:
- A lei tributária que define infrações ou lhes comina penalidades interpreta-se da maneira 
- Lei municipal que considera "serviço", para fins de ISS, uma atividade que o direito priva
- Pelo art. 109 do CTN, os princípios gerais de direito privado utilizam-se para pesquisar a
- Com a Lei Complementar 236/2026, a solução de consulta tributária passou a:
- Por força do art. 111 do CTN, uma isenção concedida a portadores de determinadas doenças n
- As práticas reiteradamente observadas pelas autoridades administrativas são consideradas n
- Sobre as fontes do direito tributário, assinale a correta.
- Pelo art. 116, parágrafo único, do CTN, a autoridade administrativa poderá desconsiderar a
- Salvo disposição em contrário, uma instrução normativa expedida pela autoridade fazendária
- Diante de lacuna na legislação tributária, a autoridade fiscal não encontrou analogia apli
```

## Pedido 16: Tutela provisória: urgência e evidência (`processo-civil.tutela-provisoria`, 483 tópicos de edital)

```
Matéria: processo-civil.tutela-provisoria (Tutela provisória: urgência e evidência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições gerais e requisitos das tutelas provisórias": 23
- "2. Poder geral de cautela, medidas do art. 301 e fungibilidade": 29
- "3. Tutela antecipada e cautelar antecedentes: prazos e estabilização": 21
- "4. Tutela da evidência, fundamentação e impugnação recursal": 14
- "5. Tutelas provisórias contra a Fazenda Pública, suspensão e jurisprudência": 13

Já existem (não repita):
- Uma pessoa requer a entrega imediata do bem que afirma lhe ser devido, antes da sentença, 
- Em demanda sobre obrigação contratual, o autor demonstra documentos favoráveis, mas não ap
- Uma ação já foi ajuizada com pedido final completo e, no curso do processo, surge urgência
- No curso de ação cível, o processo é suspenso por motivo processual e uma tutela provisóri
- Ao apreciar tutela urgente, o juiz identifica risco de prejuízo à contraparte e avalia sol
- Um credor pede constrição temporária de bens do devedor em montante adequado para garantir
- Duas pessoas disputam judicialmente a propriedade de escultura identificada por número de 
- Na inicial cautelar antecedente, a parte pede, sob esse nome, a entrega imediata da própri
- Uma demanda reúne prova documental dos fatos e tese firmada em julgamento repetitivo, enqu
- Ao conceder tutela antecipada antecedente, o juiz determina que o autor complete a petição
- O juiz considera insuficientes os elementos de inicial simplificada de tutela antecipada a
- Uma parte ajuíza tutela cautelar antecedente e o requerido é citado especificamente para o
- Uma cautelar antecedente foi efetivada para proteger o resultado de futura ação principal.
- O autor obtém tutela cautelar antecedente, mas não a efetiva nos trinta dias previstos na 
- Uma tutela antecipada foi concedida pelo procedimento do art. 303 e não houve oposição per
- Após estabilização regular de tutela antecipada antecedente, uma parte pretende ajuizar aç
- O requerente apresenta documentação integral dos fatos e aponta orientação de julgamento r
- Uma ação requer a devolução de relógio individualizado cuja custódia decorre de contrato d
- Após a contestação em processo de cobrança, há documentos suficientes dos fatos constituti
- Uma tutela de urgência é efetivada, mas a sentença final julga improcedente a pretensão de
- Em primeira instância, uma decisão interlocutória indefere tutela provisória por falta de 
- O juiz concede tutela antecipada urgente com fundamentação insuficiente sobre perigo de ir
- Em demanda contra a Fazenda Pública, uma decisão invoca a vedação do art. 7º, § 2º, da Lei
- Após liminar contra ente público, a pessoa jurídica interessada sustenta grave lesão à seg
- Em incidente de suspensão de liminar regido pelo art. 4º da Lei 8.437/1992, há pronunciame
- Uma pessoa requer tutela urgente após ajuizar a ação principal; o art. 294 permite que a t
- Para concessão de tutela de urgência em ação de obrigação de fazer, basta demonstrar perig
- Uma tutela provisória concedida durante o processo pode ser modificada após fatos novos, p
- Em cautelar de proteção patrimonial, o art. 301 limita a atuação judicial às quatro medida
- Quando documentos dos fatos são acompanhados por tese firmada em julgamento repetitivo, o 
- Após conceder arresto cautelar antecedente, o juiz aplica o art. 304 e declara sua estabil
- Se o juiz avalia risco de irreversibilidade material dos efeitos da antecipação de urgênci
- Após conceder tutela antecipada antecedente, o juiz deve fixar obrigatoriamente trinta dia
- Uma inicial denominada cautelar pede de imediato a satisfação da própria prestação final; 
- Na cautelar antecedente, caso o requerido não conteste em cinco dias, a decisão final de m
- O CPC permite apresentar o pedido principal juntamente com a inicial cautelar antecedente,
- Se a tutela cautelar antecedente perde eficácia por falta de formulação do principal, o au
- A tutela da evidência do art. 311, IV, considera os documentos suficientes trazidos na ini
- Comprovado abuso do direito de defesa, a hipótese do art. 311, I, deve ser considerada aut
- Embora o art. 304 use a expressão 'respectivo recurso', o entendimento exposto no REsp 1.9
- Com o decurso dos dois anos previstos no art. 304, § 5º, a decisão que concedeu tutela ant
- O art. 1º, § 2º, da Lei 8.437/1992 excepciona ações populares e ações civis públicas da re
- O art. 1º, § 3º, da Lei 8.437/1992 determina que toda liminar satisfativa contra a Fazenda
- A ADI 4.296 declarou inconstitucional o art. 7º, § 2º, da Lei 12.016/2009, de modo que sua
- A decisão que concede suspensão de liminar nos termos do art. 4º da Lei 8.437/1992 deve se
```

## Pedido 17: Comunicação pública, institucional e política (`comunicacao.comunicacao-publica-institucional`, 483 tópicos de edital)

```
Matéria: comunicacao.comunicacao-publica-institucional (Comunicação pública, institucional e política)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, história e finalidades da comunicação pública": 21
- "2. Comunicação organizacional: fluxos, redes e atuação profissional": 18
- "3. Estratégia, identidade, políticas públicas e gestão de crises": 22
- "4. Mídia, poder, agenda pública, pesquisas e marketing político": 22
- "5. Transparência, participação, proteção de dados e decisões avançadas": 17

Já existem (não repita):
- Um portal apresenta orientações para inscrição em serviço gratuito e abre canal para dúvid
- Uma peça financiada com recursos públicos informa as etapas de uma campanha, mas inclui re
- Uma instituição publica espontaneamente contratos e dados sobre despesas em página acessív
- Após notar que o formulário eletrônico está falhando, atendentes enviam relatório à direçã
- Duas coordenações situadas no mesmo nível hierárquico combinam um calendário conjunto de a
- Ao renovar sua comunicação, uma instituição registra missão, valores e compromissos de ate
- Um comunicado explica requisitos, documentos, etapas e prazo para solicitar determinado se
- Após interrupção de atendimento, uma equipe dispõe de fatos confirmados e ainda investiga 
- Após semanas de cobertura intensa sobre longas filas, muitos cidadãos passam a apontar o t
- Um perfil publica enquete aberta respondida voluntariamente por seus seguidores e anuncia 
- Um especialista de atendimento solicita diretamente à coordenação de tecnologia, situada e
- Durante mudança de procedimento, os trabalhadores trocam informações em conversas espontân
- Uma campanha sobre renovação de cadastro teve alto alcance, mas muitos usuários continuam 
- Uma instituição tem protocolo para vazamento de dados, contatos de emergência e porta-voz 
- Dois noticiários tratam da mesma medida: um enfatiza economia orçamentária, outro destaca 
- Uma pesquisa de opinião apresenta amostra selecionada probabilisticamente, mas formula per
- Uma pessoa solicita acesso a dados sobre despesas e um atendente exige justificativa pesso
- Uma campanha de resultados propõe divulgar lista nominativa de usuários atendidos para dem
- Ao divulgar política de transporte, uma equipe lista apenas investimentos e cerimônias, se
- Uma estratégia estuda percepções sobre determinada proposta partidária ao longo de vários 
- Um documento solicitado tem dados gerais sobre execução de serviço e uma seção legítima e 
- Nos três meses anteriores ao pleito, uma administração pretende autorizar nova publicidade
- Uma instituição deve avaliar regularmente os serviços prestados. Um analista afirma que a 
- Um tema social recebe pouca cobertura porque editores preferem outro assunto; nas matérias
- Para divulgar resultados de programa público, gestores querem personalizar mensagens com h
- Uma página oficial que explica horários, documentos exigidos e contatos do serviço desenvo
- Um comunicado enviado da direção às equipes é classificado como fluxo ascendente porque as
- Identidade institucional, imagem pública e reputação são conceitos distintos, pois comprom
- Uma enquete aberta publicada numa rede social torna-se amostra probabilística quando ultra
- Uma comunicação inicial de crise pode informar providências confirmadas e reconhecer que a
- Uma instituição que publicou grande quantidade de peças pode considerar comprovada a efeti
- A Lei nº 12.527/2011 orienta o setor público a divulgar espontaneamente informações de int
- Se uma matéria jornalística destaca as causas econômicas de um problema, a escolha do ângu
- Uma rede informal de trabalhadores pode transmitir avisos corretos e identificar problemas
- A comunicação de uma política pública deve considerar somente a fase de lançamento, já que
- Ao formular estratégia de comunicação, uma instituição pode manter validação central dos d
- A publicação de dados pessoais em um portal oficial é autorizada pela LGPD sempre que a in
- Em um documento parcialmente sigiloso solicitado pelo cidadão, é possível garantir acesso 
- Se uma pesquisa probabilística apresenta margem de erro calculada, considera-se automatica
- A divulgação continuada de problemas apontados por usuários e de providências adotadas pod
- No período eleitoral, a mera aprovação de publicidade institucional antes dos três meses a
- Na avaliação de serviços públicos, a Lei nº 13.460/2017 admite meio diferente de pesquisa 
- Na comunicação de crise, tornar invisíveis críticas publicadas por usuários equivale a apr
- O destaque dado pela mídia a uma pauta pode influenciar sua relevância pública sem signifi
- Uma peça de comunicação é necessariamente pública, em sentido democrático, quando aumenta 
```

## Pedido 18: Análise de dados e inteligência artificial (`informatica.analise-dados-ia`, 481 tópicos de edital)

```
Matéria: informatica.analise-dados-ia (Análise de dados e inteligência artificial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ciclo analítico, CRISP-DM, BI, big data e visualização": 24
- "2. Qualidade, pré-processamento, exploração e mineração de texto": 18
- "3. Aprendizado supervisionado, regularização e outras modalidades": 19
- "4. Agrupamentos, redução dimensional, associação e anomalias": 23
- "5. Métricas, validação, explicabilidade e aplicações responsáveis": 16

Já existem (não repita):
- Uma equipe inicia um projeto para reduzir o tempo de resposta de solicitações. Antes de co
- Uma analista quer mostrar a evolução mensal de solicitações entre janeiro e dezembro, pres
- Uma coluna numérica apresenta média 40 e desvio-padrão 5. Qual é o valor padronizado z-sco
- Uma base contém exemplos rotulados como risco alto ou baixo. Deseja-se um modelo que estim
- Uma equipe deseja separar registros semelhantes em três grupos sem possuir categorias prev
- Um classificador produziu 30 verdadeiros positivos e 10 falsos positivos. Qual é a precisã
- Sensores geram registros em alta frequência, imagens e textos, exigindo ingestão contínua.
- Uma equipe vai treinar um modelo preditivo. Qual procedimento reduz o risco de vazamento a
- Uma coleção de mensagens será representada numericamente, valorizando termos frequentes na
- Uma série de atendimentos de 2022 a 2026 servirá para prever o mês seguinte. Qual divisão 
- Um conjunto de árvores de decisão é ajustado por amostras aleatorizadas e subconjuntos de 
- Em um modelo linear com muitas variáveis correlacionadas, deseja-se reduzir coeficientes e
- Uma base contém dois grupos de formato irregular e alguns registros dispersos. A equipe qu
- Em 200 compras, 50 contêm o produto A, 80 contêm B e 30 contêm ambos. Qual é a confiança d
- Uma análise PCA identificou primeiro e segundo componentes com 55% e 25% da variância tota
- Um detector de eventos raros registrou 45 VP, 15 FN, 5 FP e 135 VN. Que valor representa s
- Um modelo previu três valores com erros absolutos de 1, 2 e 3 unidades. Quais são, respect
- Uma base reúne milhões de transações sem rótulos de fraude. Deseja-se atribuir escores de 
- Um modelo de people analytics prevê desligamentos com elevada acurácia global, mas erra mu
- Um problema de classificação dispõe de poucos milhares de observações, grande quantidade d
- Um classificador apresenta precisão 0,75 e recall 0,50. Qual é seu escore F1, arredondado 
- Uma equipe dispõe de duas gravações que misturam fontes sonoras e pretende recuperar sinai
- Uma classe positiva aparece em somente 1% da base, e a equipe discute uma AUC-ROC alta, em
- Um modelo passou em validação estatística, mas não alcançou a meta de redução de atrasos p
- Um simulador ensina um agente a escolher ações mediante recompensa acumulada. Outra equipe
- Ao planejar um painel de indicadores de atendimento, a equipe pode utilizar análise descri
- Em uma tabela com valores extremos, a imputação pela média é necessariamente mais robusta 
- Para prever o valor numérico de uma despesa a partir de atributos conhecidos, um problema 
- Um agrupamento k-means com k=4 comprova que a população original possui exatamente quatro 
- Em uma matriz de confusão binária, os falsos negativos são exemplos realmente positivos cl
- Uma série de transações financeiras é volumosa e heterogênea. Por isso, a quantidade de re
- Um classificador apresenta recall de 90% mas precisão de 30% em uma classe rara. É possíve
- Ao aplicar TF-IDF a reclamações textuais, a remoção indiscriminada de termos de negação pr
- Ao modelar uma base de texto com Naive Bayes, pode-se assumir independência condicional do
- Uma análise exploratória encontra forte correlação entre uso de um serviço e satisfação. E
- Em uma regra A ⇒ B, o lift compara a confiança com a frequência basal de B; um valor maior
- Uma rede neural tem erro reduzido no treinamento e erro elevado em validação. A divergênci
- Ao comparar quatro algoritmos com validação cruzada, a equipe deve tratar a partição final
- Uma previsão de receita obtém R² de −0,20 no conjunto de teste. Esse resultado é impossíve
- Em uma mistura de gaussianas aplicada a perfis de consumo, o modelo pode atribuir probabil
- Uma equipe aplicou PCA usando todos os dados antes de selecionar atributos e dividir trein
- Um classificador mantém acurácia semelhante após implantação, mas aumenta a taxa de falsos
- No monitoramento de previsões, drift de dados e drift de conceito são sinônimos: ambos des
- Em um sistema de recomendação, apresentar ao usuário apenas itens semelhantes aos já consu
- Uma equipe conclui que a importância de uma variável em um classificador prova que manipul
```

## Pedido 19: Processo Penal: nulidades, recursos, habeas corpus e revisão criminal (`processo-penal.nulidades-recursos-habeas-corpus`, 480 tópicos de edital)

```
Matéria: processo-penal.nulidades-recursos-habeas-corpus (Processo Penal: nulidades, recursos, habeas corpus e revisão criminal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Nulidades processuais: prejuízo, arguição e saneamento": 27
- "2. Teoria geral dos recursos: legitimidade, efeitos e garantias": 21
- "3. Recursos em espécie: RESE, apelação, embargos e carta testemunhável": 18
- "4. Habeas corpus: cabimento, competência e procedimento": 18
- "5. Revisão criminal, coisa julgada e integração dos meios de impugnação": 16

Já existem (não repita):
- Durante instrução penal, houve irregularidade de forma que não impediu manifestação de nen
- Em um processo, o defensor demonstrou atuação efetiva, mas outro profissional sustenta que
- Depois de interpor recurso regularmente, o Ministério Público considera desnecessário insi
- A parte apresentou por equívoco espécie recursal diversa daquela cabível, sem má-fé, e há 
- O juiz deixou de receber denúncia apresentada regularmente. Qual impugnação ordinária é ex
- Foi proferida sentença definitiva condenatória por juiz singular. A defesa deseja impugnar
- Alguém apresenta ameaça concreta de prisão ilegal e pretende obter proteção antes da efeti
- Após trânsito em julgado de condenação, surge laudo comprovando falsidade de documento dec
- Em recurso exclusivo do réu contra condenação de cinco anos, o tribunal pretende elevar a 
- Um acusado foi pronunciado na fase própria do procedimento e o defensor discute cabimento 
- O recurso em sentido estrito foi interposto tempestivamente e as partes devem arrazoá-lo; 
- Publicado acórdão com omissão relevante no exame de pedido apresentado tempestivamente, a 
- Uma decisão de segunda instância desfavorável ao réu teve votação de dois a um, com diverg
- Uma apelação foi interposta pela defesa em processo criminal comum. Após o termo, deseja a
- Após o despacho que denegou seguimento de recurso, a parte pretende providência própria pa
- Um paciente está preso ilegalmente; familiar sem formação jurídica deseja impetrar habeas 
- Em processo com dois corréus, recurso de um deles revela nulidade de prova produzida conju
- Depois de reforma legislativa em 2026, a parte interpõe RESE contra decisão compreendida n
- O ofendido não se habilitou como assistente de acusação e o Ministério Público deixou tran
- Um pedido de habeas corpus foi apresentado dentro de recurso que não veio a ser conhecido.
- Em julgamento penal colegiado ocorrido em 2026, há empate entre absolvição e condenação, c
- Após a condenação transitar em julgado e pena estar integralmente cumprida, surge prova no
- Um tribunal julga procedente revisão criminal e conclui que a pena imposta originalmente d
- Após julgamento revisional que reconhece erro condenatório, o interessado requer indenizaç
- O julgamento criminal encerrou-se com condenação, e a defesa cogita utilizar antigo protes
- O art. 563 do CPP condiciona a declaração de nulidade à existência de prejuízo para a acus
- O Ministério Público que interpôs recurso penal pode desistir dele livremente enquanto o t
- A decisão que não recebe denúncia integra as hipóteses legais de recurso em sentido estrit
- O prazo comum para interpor apelação no Código de Processo Penal é de oito dias, idêntico 
- Habeas corpus preventivo pode proteger pessoa que ainda não foi presa, mas esteja concreta
- A revisão criminal apenas pode ser ajuizada antes de se extinguir a pena, perdendo cabimen
- Quando decisão revisional é procedente, o tribunal pode absolver, modificar pena, alterar 
- Uma nulidade relativa abrangida pelo art. 572 obriga renovação do processo mesmo quando a 
- O efeito extensivo do art. 580 do CPP permite aproveitar provimento de recurso de um corré
- Na regra do CPP, as razões do RESE seguem prazo de oito dias para cada parte, assim como a
- Quando um acórdão de segunda instância é não unânime e desfavorável ao réu, o art. 609, pa
- A carta testemunhável possui, por previsão expressa no CPP, efeito suspensivo automático s
- A Lei 14.836/2024 incluiu art. 647-A no CPP, permitindo expedir habeas corpus individual o
- O protesto por novo júri permaneceu como modalidade autônoma de recurso penal após a refor
- Após a Lei 15.358/2026, a parte que interpõe RESE em situação do art. 581, V, pode pedir e
- A parte acusadora pode ajuizar revisão criminal para desconstituir absolvição definitivame
- Após diligências e apresentação do paciente, quando determinadas em habeas corpus no prime
- Desde a alteração legislativa de 2024, empate em julgamento colegiado penal exige aguardar
- O art. 630 do CPP contempla reconhecimento de justa indenização, se requerida, por prejuíz
- A Súmula 160 do STF autoriza tribunal a reconhecer, em prejuízo do réu, nulidade que a acu
```

## Pedido 20: Inquérito policial (`processo-penal.inquerito-policial`, 479 tópicos de edital)

```
Matéria: processo-penal.inquerito-policial (Inquérito policial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, natureza jurídica e funções do inquérito policial": 18
- "2. Notícia da infração, instauração e atos iniciais": 20
- "3. Diligências, indiciamento, sigilo e garantias do investigado": 26
- "4. Prazos, relatório final e passagem para a ação penal": 15
- "5. Arquivamento, investigações alternativas e questões integradas": 21

Já existem (não repita):
- Uma pessoa presencia possível roubo e procura a autoridade para informar local e caracterí
- Com elementos idôneos obtidos em procedimento investigativo independente do inquérito, o t
- Ao terminar investigação sem conseguir identificar o autor, o delegado cogita encerrar o c
- Em inquérito sigiloso, advogado com procuração pede cópia de depoimento já documentado e p
- Após coleta de laudo e depoimentos, há indícios individualizados contra determinada pessoa
- Em investigação policial comum, uma pessoa está solta e não há lei especial incidente. O p
- Uma testemunha narrou evento à polícia e o relato foi juntado ao inquérito. Posteriormente
- Diante de infração penal para a qual a lei exige representação do ofendido como condição d
- Ocorrência de menor potencial ofensivo é formalizada em termo circunstanciado; o autor ass
- Após diligências policiais, o Ministério Público deseja a devolução dos autos para ato adi
- Em investigação de pessoa presa regida pela Lei 5.010/1966, uma equipe informa que o prazo
- Durante apuração, a defesa requer oitiva adicional e o ofendido propõe perícia não obrigat
- Em inquérito formalmente sigiloso, advogado sem procuração pede acesso ao procedimento par
- Delegado comunica que o relatório concluiu pela provável prática de crime, porém ainda não
- Uma comissão parlamentar de inquérito reúne documentação sobre fato determinado e remete c
- A autoridade policial quer impedir que o indiciado fale com seu advogado durante depoiment
- Uma apuração por delito da Lei 11.343/2006 envolve indiciado solto. A autoridade estima no
- Depois de arquivamento por insuficiência de indícios, a autoridade recebe nova prova docum
- O Ministério Público inicia procedimento investigatório próprio e documenta medidas de obt
- Uma vítima recebe comunicação de arquivamento de investigação sujeita à disciplina do art.
- Após relatório final, o Ministério Público observa que testemunha imprescindível ainda não
- Em crime cuja persecução depende de iniciativa privada, terceiro comunica o fato à polícia
- Superior hierárquico deseja redistribuir inquérito policial em curso apenas por preferênci
- O juiz analisa sentença que mencionou apenas relatos colhidos na fase de investigação, enq
- Investigação sobre crime de ação pública tramita com suspeito preso, mas o órgão acusador 
- Ao registrar a notícia de possível crime, o delegado abre procedimento destinado à apuraçã
- Em investigação sigilosa, o defensor com procuração tem acesso obrigatório à estratégia de
- Nos termos do CPP, art. 17, a autoridade policial não pode determinar arquivamento dos pró
- Na apuração de crime de ação pública condicionada, basta denúncia informal de qualquer viz
- Em inquérito comum com indiciado solto, aplica-se em princípio prazo de trinta dias de con
- Quando o delegado formaliza indiciamento por ato motivado, o Ministério Público fica jurid
- A pessoa investigada pode requerer diligência no curso do inquérito, e a existência dessa 
- Para calcular a conclusão do inquérito relativo a crime da Lei 11.343/2006 com investigado
- Uma comissão parlamentar de inquérito pode colher elementos sobre fato determinado e remet
- A polícia judiciária possui monopólio constitucional de toda investigação penal e, por iss
- Ao chegar ao local de delito com vestígios, a autoridade deve adotar providências de prese
- Como o inquérito tem característica inquisitiva, o investigado que permanece calado no int
- Para investigação de crime de ação privada, o CPP exige requerimento do legitimado a propo
- No relatório final, o delegado descreve as conclusões e automaticamente determina recebime
- O relatório do inquérito pode indicar testemunhas ainda não inquiridas e sua localização, 
- Arquivado inquérito por insuficiência de base para denúncia, a polícia obtém apenas repeti
- O art. 2º, § 4º, da Lei 12.830/2013 restringe a avocação ou redistribuição de inquérito em
- O prazo de quinze dias prorrogáveis por outros quinze, previsto no art. 66 da Lei 5.010/19
- Uma testemunha falece antes de poder ser ouvida em juízo, e seu depoimento investigativo r
- A vítima discordante do arquivamento, sob o art. 28, § 1º, do CPP, conta necessariamente t
```

## Pedido 21: Constituições estaduais: estrutura e simetria (`legislacao-estadual.constituicoes-estaduais`, 478 tópicos de edital)

```
Matéria: legislacao-estadual.constituicoes-estaduais (Constituições estaduais: estrutura e simetria)
Capítulos e quantas questões quero em cada um (total 100):
- "Poder constituinte decorrente, autonomia estadual e princípio da simetria": 20
- "Organização política estadual: território, Assembleia Legislativa e Governador": 20
- "Justiça estadual, Tribunal de Contas, Ministério Público e Defensoria Pública": 20
- "Administração estadual, servidores, finanças, orçamento e ordem social": 20
- "Controle de constitucionalidade e jurisprudência do STF: limites práticos": 20

Já existem (não repita):
- Sobre o lugar das Constituições estaduais no ordenamento brasileiro, assinale a afirmação 
- Cada estado-membro brasileiro possui Constituição própria, cujo conteúdo deve respeitar a 
- Que dispositivo da Constituição Federal reconhece diretamente que os estados se organizam 
- O poder constituinte decorrente é juridicamente ilimitado e pode afastar normas nacionais 
- Na estrutura política estadual típica, qual órgão exerce o Poder Legislativo?
- As Assembleias Legislativas exercem a função legislativa estadual, e o mandato dos deputad
- De acordo com o modelo constitucional brasileiro, o Tribunal de Contas estadual é integrad
- Os Tribunais de Contas estaduais integram o Poder Judiciário porque fiscalizam e julgam de
- A representação de inconstitucionalidade estadual, em regra, é julgada pelo órgão abaixo e
- Em matéria estadual, PPA, LDO e LOA desempenham funções diferentes no planejamento e na pr
- Qual combinação reúne instrumentos típicos do planejamento e orçamento do estado?
- Se um estado tem 13 deputados federais, sua Assembleia Legislativa deve possuir 39 deputad
- Qual instituição estadual recebeu da Constituição Federal autonomia funcional e administra
- O princípio da simetria exige observância de modelos federais vinculantes, mas não implica
- Um estado possui 14 deputados federais. Qual será, segundo o critério constitucional, o nú
- O Tribunal de Justiça estadual pode exercer controle abstrato de atos normativos municipai
- Uma Constituição estadual pretende ter redação própria para seu preâmbulo. À luz do STF, q
- As constituições estaduais devem conferir a um único órgão a legitimidade para representaç
- Lei complementar estadual institui região metropolitana formada por municípios limítrofes 
- O estatuto dos servidores de um estado pode conter direitos e procedimentos próprios, desd
- A respeito da escolha dos sete conselheiros de um Tribunal de Contas estadual, assinale a 
- A mera previsão de despesa financeira em um projeto de lei estadual torna sua iniciativa a
- O Governador deseja vincular a Defensoria Pública estadual à Secretaria de Justiça. Qual a
- À Defensoria Pública estadual são asseguradas autonomia funcional e administrativa e inici
- Constituição estadual prevê capítulo sobre servidores e a banca pergunta por uma licença e
- O preâmbulo da Constituição Federal deve ser reproduzido literalmente no preâmbulo de cada
- A Constituição de um estado entrega a apenas um órgão o direito de propor representação de
- A Constituição estadual pode aumentar livremente o quórum de reforma para quatro quintos, 
- Uma assembleia insere, por emenda à Constituição estadual, vantagem remuneratória individu
- Segundo a Súmula 653, a Assembleia Legislativa escolhe quatro conselheiros do TCE e o Gove
- Constituição estadual exige quórum de quatro quintos dos deputados para aprovação de suas 
- A Constituição estadual pode tornar o Ministério Público estadual integralmente subordinad
- Uma proposta estadual reserva ao Governador iniciativa privativa para leis que organizem o
- Emendas à Constituição estadual estão sujeitas a controle de constitucionalidade perante a
- Uma constituição estadual prevê vários legitimados para representação de inconstitucionali
- O STF considera inválida qualquer ampliação, pelo estado, da lista de legitimados para con
- No preenchimento das três vagas de conselheiro de TCE atribuídas ao Governador, qual arran
- Vincular formalmente a Defensoria estadual a uma secretaria de Estado é incompatível com a
- Um candidato sustenta que o Tribunal de Justiça pode julgar, em representação abstrata est
- A utilização de emenda constitucional estadual para criar vantagens concretas de pessoal p
```

## Pedido 22: Bens (`direito-civil.bens`, 476 tópicos de edital)

```
Matéria: direito-civil.bens (Bens)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, natureza e bens imóveis e móveis": 25
- "2. Fungibilidade, consumo, divisão e universalidades": 20
- "3. Bens principais, pertenças, frutos e benfeitorias": 18
- "4. Bens públicos, afetação e bem de família": 24
- "5. Bens em negócios jurídicos, obrigações e responsabilidade": 13

Já existem (não repita):
- Uma pessoa compra um terreno com uma casa firmemente incorporada ao solo. Qual é a classif
- Após um falecimento, a herança é formada apenas por saldo bancário e automóvel. Como se cl
- Durante reforma, telhas são temporariamente removidas para serem recolocadas no mesmo préd
- Um credor cede a outra pessoa um direito de crédito resultante de contrato de empréstimo. 
- Uma cooperativa mantém sacas padronizadas de produto agrícola, trocáveis por outras da mes
- Uma loja mantém bicicletas novas exclusivamente para revenda, embora cada bicicleta suport
- Dois coproprietários desejam dividir uma máquina industrial que perderá a função ao ser fi
- Uma pessoa reúne centenas de livros de sua propriedade em biblioteca organizada para pesqu
- Um prédio é vendido com maquinário removível empregado duradouramente em sua atividade, se
- Um proprietário negocia antecipadamente a safra ainda presa às árvores do pomar. Sobre a f
- Em imóvel particular, uma pessoa realiza conserto para conter vazamento que ameaçava deter
- Uma enchente deposita naturalmente sedimentos que elevam parte de um terreno, sem interven
- Contrato de entrega de coisa certa prevê o objeto principal, mas silencia sobre peça móvel
- Um imóvel pertencente a autarquia abriga suas atividades permanentes de atendimento ao púb
- Um terreno pertence a pessoa jurídica de direito público e não está destinado a praça, via
- A administração pretende alienar prédio que deixou licitamente de servir a unidade pública
- Após longa posse sem oposição, um particular pretende adquirir por usucapião terreno públi
- Uma norma institui cobrança regular pelo uso de equipamento público classificado como bem 
- O proprietário reside com sua família em imóvel sujeito a cobrança de imposto relativo ao 
- Uma pessoa solteira mora sozinha em imóvel próprio e sofre execução de dívida comum sem ex
- Um casal pretende instituir voluntariamente bem de família por escritura pública. Seu patr
- Particulares celebram negócio de transferência de direito real sobre imóvel de valor super
- Duas partes simulam uma venda para ocultar doação. O negócio dissimulado reúne os requisit
- O devedor devia entregar determinada quantia, mas o credor aceita receber um veículo espec
- Uma pessoa causa dano material a terceiro mediante conduta negligente demonstrada, com nex
- Uma empresa compra energia elétrica para alimentar equipamentos. A energia dotada de valor
- Depois da demolição de uma casa, os tijolos retirados permanecem juridicamente imóveis, po
- Em um armazém, produtos padronizados que podem ser substituídos por outros de mesma espéci
- Em um estoque comercial, uma mesa durável destinada à revenda é juridicamente inconsumível
- Se dois irmãos são coproprietários de uma única máquina, há universalidade de fato apenas 
- Uma máquina removível destinada de modo duradouro ao funcionamento de imóvel pode ser cons
- Ao vender um pomar, seu titular só poderá negociar os frutos já destacados das árvores, po
- Um município mantém praça pública destinada à fruição coletiva. Enquanto preservada a qual
- Um terreno dominical pertencente a pessoa jurídica de direito público pode ser alienado co
- Um imóvel público utilizado como escola passa à classe dominical apenas porque seu expedie
- Uma pessoa solteira, que mora sozinha em imóvel próprio, não pode invocar a impenhorabilid
- Uma escritura de instituição voluntária de bem de família depende de registro do respectiv
- O proprietário instituiu bem de família voluntário por registro e, por isso, deixou de res
- Quando o credor aceita voluntariamente uma prestação diferente da devida para extinguir a 
- Uma pessoa pretende usucapir bem público porque ele foi desafetado há anos; a desafetação,
- Em contrato de compra de imóvel, a inclusão de determinada pertença pode resultar da manif
- Se um negociante entrega objeto adquirido sob negócio simulado, a simulação conduz necessa
- No regime do art. 178, a pretensão de anular negócio por coação sujeita-se ao prazo decade
- O fiador em contrato de locação pode opor a impenhorabilidade residencial de modo absoluto
- Uma conduta negligente que viola direito e provoca dano pode fundamentar obrigação de repa
```

## Pedido 23: Controle judicial (`direito-administrativo.controle-judicial`, 474 tópicos de edital)

```
Matéria: direito-administrativo.controle-judicial (Controle judicial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistemas de jurisdição e fundamentos do controle judicial": 41
- "2. Atos administrativos, discricionariedade e meios judiciais": 29
- "3. Processo administrativo federal e responsabilidade civil": 10
- "4. Agentes públicos e regime federal de pessoal": 10
- "5. Disciplina funcional e crimes contra a Administração": 10

Já existem (não repita):
- Uma pessoa quer contestar sanção administrativa por violação do contraditório. Qual fundam
- Após recurso interno, alguém sustenta que decisão administrativa não pode mais ser discuti
- Diante de duas opções legais de localização de equipamento, o juiz prefere uma delas, sem 
- Autoridade competente expede ato para perseguir desafeto, embora declare interesse público
- Um ato legal deixa de ser conveniente e a autoridade considera retirá-lo do mundo jurídico
- Ao receber decisão desfavorável, a parte procura recurso administrativo sem prazo específi
- A autoridade que decidiu recebe recurso e não se convence a reconsiderar. Segundo a Lei nº
- A Administração descobre vício sanável que não causa lesão pública nem prejuízo a terceiro
- Uma decisão negativa em procedimento federal encerrou a instrução. Qual prazo legal básico
- Uma pessoa deseja impetrar mandado de segurança contra ato conhecido há 40 dias e apresent
- Servidor deslocado para outra unidade do mesmo quadro, sem transferência do cargo a outra 
- Ato de provimento é publicado e a pessoa nomeada deve formalizar a investidura. Qual prazo
- Após tomar posse, a pessoa investida em cargo efetivo não entra em exercício no prazo lega
- Uma unidade transfere um cargo efetivo vago a outro órgão do mesmo Poder, atendidas exigên
- Vítima de colisão com veículo oficial em serviço busca reparação. Qual responsabilidade re
- Uma autoridade quer punir servidor com suspensão acima do limite legal ordinário. Qual é o
- Comissão disciplinar é instalada para processo regido pela Lei nº 8.112/1990. Quantos serv
- Em ação disciplinar, a defesa aponta apenas conclusão tardia, sem qualquer prejuízo. Qual 
- Uma agente exige vantagem indevida de interessado em razão da função. Sem outros dados, qu
- Particular oferece dinheiro para que agente retarde ato de ofício, sem solicitação prévia.
- Após recurso federal, autoridade reconsidera punição e cogita agravá-la antes de decidir d
- Pessoa beneficiada há seis anos por ato administrativo praticado de boa-fé sofre tentativa
- Servidor é absolvido penalmente porque ficou demonstrado que o fato imputado nunca ocorreu
- Em revisão de sanção administrativa federal surgem fatos novos comprovados. O que pode oco
- Na fiscalização de despesas, um cidadão impugna judicialmente ato que desrespeita limite l
- Após esgotar recurso interno, interessado pode submeter alegada ilegalidade a exame judici
- Em contratação discricionária, a mera discordância do juiz com uma opção lícita autoriza r
- A presunção de legitimidade do ato administrativo admite prova em contrário apresentada pe
- Ao motivar remoção por necessidade de unidade inexistente, a Administração fica dispensada
- Sem prazo específico, a Lei nº 9.784/1999 estabelece, em regra, cinco dias para atos de au
- O recurso administrativo federal, na falta de disposição específica, deve ser interposto e
- O recurso administrativo federal pode questionar razões de legalidade e também de mérito d
- A revisão de processo sancionador da Lei nº 9.784/1999 pode agravar a pena quando sua inst
- Em regra, a pessoa jurídica pública responde objetivamente por dano causado por agente atu
- A teoria do risco administrativo impede apreciar culpa exclusiva da vítima mesmo quando ro
- Cargo em comissão e função de confiança são categorias idênticas quanto à exigência de ocu
- A investidura em cargo público, nos termos da Lei nº 8.112/1990, ocorre com a posse.
- Redistribuição ocorre quando o servidor é deslocado dentro do mesmo quadro sem alteração d
- Promoção consta como forma de provimento e também como hipótese de vacância na Lei nº 8.11
- A falta de advogado no processo disciplinar, por si só, viola necessariamente a Constituiç
- A comissão responsável pelo processo disciplinar regido pela Lei nº 8.112/1990 é composta 
- O excesso do prazo de conclusão do processo disciplinar torna obrigatória sua nulidade, ai
- No processo disciplinar, a revisão pode ser requerida a qualquer tempo, diante de fatos no
- Quando um funcionário exige vantagem indevida em razão da função, o verbo típico central é
- Para efeitos penais, o conceito de funcionário público do art. 327 é mais amplo que a pess
```

## Pedido 24: Atualidades: sociedade, cultura, educação e meio ambiente (`atualidades.sociedade-cultura-meio-ambiente`, 473 tópicos de edital)

```
Matéria: atualidades.sociedade-cultura-meio-ambiente (Atualidades: sociedade, cultura, educação e meio ambiente)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação social, cidadania e desigualdades": 19
- "2. Cultura, comunicação e tecnologias contemporâneas": 25
- "3. Educação, saúde, trabalho e mobilidades": 20
- "4. Meio ambiente, energia e riscos climáticos": 23
- "5. Bahia no mundo contemporâneo e integração de políticas": 13

Já existem (não repita):
- Uma cidade garante inscrição on-line idêntica a todos em um programa de formação, mas part
- Dois municípios registram a mesma renda média, mas no primeiro poucos recebem quase todo o
- Após cheias intensas, uma família deixa uma cidade baiana para residir em outro município 
- Uma cidade busca preservar o samba de roda e um conjunto de casarões históricos. Que class
- Um vídeo viral atribui uma enchente recente a determinada cidade, mas não apresenta data n
- Um relatório aponta crescimento de matrículas no ensino médio. Qual conclusão é metodologi
- Uma região de 10.000 habitantes contabiliza 20 casos novos de doença durante um ano. Qual 
- Uma pessoa realiza entregas remuneradas por aplicativo, mas não possui vínculo formal de e
- Um município propõe sirenes, mapas de áreas inundáveis e rotas de evacuação para períodos 
- Um boletim informa grande participação de fontes renováveis na oferta de eletricidade. Com
- Em uma área de vegetação nativa, a expansão de uma espécie exótica reduz populações locais
- Uma produtora cultural deseja concorrer a apoio financiado pela Política Nacional Aldir Bl
- Divulgação sobre o Censo Escolar 2025 menciona 140 mil matrículas em tempo integral e part
- Uma cidade instala tablets nas escolas e afirma ter solucionado o déficit de alfabetização
- Em região semiárida, uma sequência de anos secos coincide com erosão e perda persistente d
- Uma notícia de agosto de 2026 anuncia o início da elaboração do Plano de Ação Climática da
- Ao mapear falta de atendimento, um estudo encontra maiores obstáculos para mulheres negras
- Após tempestade excepcional, duas cidades apresentam danos muito diferentes apesar de prec
- Uma ferramenta de IA seleciona candidatos a bolsas educacionais usando histórico de morado
- Uma indústria vende equipamentos abrangidos por sistema de retorno pós-consumo e informa q
- Em dois bairros igualmente expostos a chuva intensa, um possui alertas, drenagem e moradia
- Uma análise afirma que o aquecimento provocado por ações humanas foi identificado em escal
- Diante do aumento de famílias deslocadas após conflitos e desastres, uma análise compara r
- Uma região instala parques de energia eólica, mas trabalhadores locais temem perda de ocup
- Um plano estadual propõe novos parques solares, revisão da drenagem urbana, proteção de co
- Uma associação de moradores que reivindica drenagem e participa de debates sobre orçamento
- O crescimento da renda média de uma população demonstra, por si só, que a desigualdade ent
- A transmissão entre gerações de uma prática musical do Recôncavo pode exigir ações de salv
- Uma mensagem publicada em rede social passa a constituir informação comprovada quando alca
- A incidência de determinada doença deve considerar casos novos, população de referência e 
- Por não possuir contrato formal, uma pessoa que realiza diariamente entregas remuneradas d
- A existência de moradias frágeis em encostas e a falta de sistemas de alerta podem agravar
- Um sistema de inteligência artificial que classifica famílias para acesso a benefícios dev
- A instalação de árvores e superfícies permeáveis pode integrar medidas urbanas de adaptaçã
- Por terem metas climáticas, os países que aderiram ao Acordo de Paris já comprovaram o cum
- Uma política de educação no interior baiano pode precisar conciliar transporte escolar, di
- A ampliação de matrículas em escolas de tempo integral permite afirmar, sem avaliação comp
- Na comparação entre territórios, um percentual de matrículas em tempo integral só pode ser
- Se uma pesquisa encontra queda nas temperaturas de uma cidade durante duas semanas, fica d
- Um projeto cultural que preserva acervos digitais precisa ponderar acesso público e proteç
- Como a oferta interna de eletricidade brasileira teve 88,2% de fontes renováveis em 2024, 
- A preservação de modos de vida quilombolas pode envolver simultaneamente reconhecimento id
- Em um surto de dengue, o aumento da capacidade de atendimento clínico torna desnecessários
- Uma política cultural que contempla editais para grupos locais e manutenção de espaços pod
- O anúncio de início da elaboração de um plano climático estadual em 2026 permite contabili
```

## Pedido 25: Argumentação, pertinência e adequação textual (`portugues.argumentacao-adequacao-textual`, 472 tópicos de edital)

```
Matéria: portugues.argumentacao-adequacao-textual (Argumentação, pertinência e adequação textual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tese, razões, evidências e adequação conceitual": 16
- "2. Tipos de argumentos e avaliação da força das razões": 35
- "3. Defeitos de conteúdo e consistência argumentativa": 17
- "4. Coesão, operadores, modalizadores e seleção vocabular": 16
- "5. Argumentação na esfera jornalística e publicitária": 16

Já existem (não repita):
- Leia: 'A biblioteca deveria abrir aos sábados, pois a procura por estudos coletivos se con
- Tese: 'Convém instalar cobertura na área de espera'. Qual informação seria mais pertinente
- Leia: 'Três pessoas relataram dificuldade para usar o aplicativo; logo, ninguém consegue u
- Leia: 'O fluxo foi simplificado; portanto, o atendimento poderá ficar mais rápido'. Que re
- Um anúncio fictício apresenta a frase 'Descubra agora uma maneira mais prática de organiza
- Leia: 'Segundo uma pesquisadora que estuda acessibilidade há anos, sinalização legível mel
- Leia: 'A alteração é adequada porque constitui uma medida apropriada'. Qual é a limitação 
- Compare 'A mudança pode reduzir atrasos' e 'A mudança eliminará atrasos'. Qual efeito da s
- Leia: 'Embora o serviço exija investimento inicial, a redução prevista de deslocamentos ju
- Tese: 'Ampliar atendimento após as 18 horas reduzirá pedidos não atendidos'. Qual informaç
- Conclusão: 'A sinalização nova diminuiu acidentes, pois as ocorrências caíram após sua ins
- Leia: 'A evasão de cursos ocorre apenas porque o material é caro; reduzir preços resolverá
- Uma entrevista registra: 'O resultado é promissor, mas foi observado apenas em testes prel
- Leia: 'Em duas unidades com demanda semelhante, a que ampliou o horário reduziu as filas'.
- Um anúncio fictício pergunta: 'Quem não deseja aproveitar melhor o próprio tempo?' e apres
- Leia: 'O relatório criticou o parecer porque ele não apresentou as fontes dos números'. Qu
- Uma argumentação contém: 'O transporte precisa melhorar. Melhorar o transporte é necessári
- Conclusão de relatório: 'O novo formulário é preferido pelos usuários em geral'. Base: 90%
- Leia: 'Uma pessoa reconhecida por sua carreira artística assegurou que determinado equipam
- Um texto afirma: 'A procura não cresceu de janeiro a março, mas aumentou de abril a junho'
- Leia: 'Embora as inscrições tenham diminuído, o programa deve permanecer por atender grupo
- Um levantamento registra adesão de 15% à proposta. O título 'Ninguém aderiu à proposta' é 
- Um anúncio apresenta depoimento: 'Desde que usei a ferramenta, organizo meus compromissos 
- Leia: 'Até os usuários experientes tiveram dificuldade para completar a etapa'. Que efeito
- Uma pesquisa identificou associação entre uso de biblioteca e desempenho acadêmico. Qual f
- No trecho 'É recomendável ampliar o horário, porque existem solicitações fora do período a
- Um argumento torna-se suficiente para uma conclusão universal sempre que apresenta um caso
- Na frase 'Embora a proposta tenha custos, os benefícios estimados justificam sua adoção', 
- A frase 'O método é eficaz porque funciona com eficácia' apresenta justificativa independe
- Em anúncio que diz 'Experimente agora', o imperativo funciona como recurso de convocação d
- Trocar 'talvez haja redução' por 'certamente haverá redução' preserva integralmente o grau
- Para avaliar a tese de que determinada mudança reduziu atrasos, saber que outro fator capa
- Uma afirmação pode ser chamada de descontextualizada apenas quando foi inventada integralm
- Um argumento de autoridade depende de competência da fonte no campo abordado, de modo que 
- O conector 'além disso' produz, em qualquer sequência textual, uma conclusão necessária so
- As afirmações 'Não houve aumento em maio' e 'Houve aumento em junho' podem coexistir sem c
- Em texto de caráter informativo, escrever 'a taxa explodiu' é descrição numericamente neut
- Um texto publicitário pode combinar depoimento pessoal e benefício prometido; contudo, uma
- Uma amostra não representativa de cinco pessoas pode demonstrar que certo fenômeno ocorreu
- Em 'Até os leitores familiarizados com o tema tiveram dúvidas', o operador 'até' posiciona
- Ao substituir 'Embora haja custos, a medida pode funcionar' por 'Porque há custos, a medid
- Duas notícias podem destacar aspectos diferentes do mesmo conjunto de dados sem serem cont
- Se um texto apresenta tese, a reproduz por sinônimos e conclui com o mesmo juízo, a varied
- Uma objeção que mostra redução simultânea no número de pessoas expostas ao risco pode enfr
- Num anúncio que promete benefício absoluto, inserir as palavras 'incrivelmente' e 'totalme
```

## Pedido 26: Poder constituinte e reforma constitucional (`direito-constitucional.poder-constituinte`, 470 tópicos de edital)

```
Matéria: direito-constitucional.poder-constituinte (Poder constituinte e reforma constitucional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, titularidade e poder constituinte originário": 26
- "2. Poder derivado reformador, decorrente e auto-organização": 17
- "3. Procedimento de emenda e limites formais ou circunstanciais": 21
- "4. Cláusulas pétreas e limites materiais da reforma": 16
- "5. Revisão, mutação constitucional, recepção e casos comparados": 20

Já existem (não repita):
- Durante uma discussão sobre a origem da Constituição vigente, afirmou-se que sua autoridad
- Em uma assembleia constituinte nacional, representantes eleitos elaboram nova Constituição
- Uma assembleia estadual elabora a Constituição do respectivo Estado com observância da Con
- Uma proposta de emenda obtém três quintos dos votos dos membros das duas Casas, em dois tu
- Durante a tramitação de uma PEC federal, discute-se qual das situações impede que a Consti
- Uma PEC pretende eliminar a autonomia política dos Estados, convertendo-os em unidades adm
- Um estudante associa a revisão constitucional do ADCT a um procedimento de votação específ
- Sem alterar o texto constitucional, uma interpretação institucional consolidada passa a at
- Lei ordinária editada antes da Constituição de 1988 continua materialmente compatível com 
- Uma PEC federal é subscrita por parlamentares, sem participação do Executivo. Segundo o ar
- No mesmo ano legislativo, uma PEC foi rejeitada. Novo grupo pretende apresentar outra PEC 
- Para propor uma PEC, diversas Assembleias Legislativas deliberam sobre a iniciativa previs
- A primeira votação de uma PEC na Câmara alcança três quintos dos membros, mas a segunda ob
- Uma proposta pretende acrescentar mecanismo de controle recíproco entre Poderes, sem retir
- Ao estudar norma constitucional que nasceu com o texto de 1988, alguém propõe sua invalida
- Durante estudo comparativo, um grupo identifica o quórum de dois terços e interstício míni
- Uma lei federal pré-constitucional é incompatível em seu conteúdo com a Constituição de 19
- Após seis emendas de revisão promulgadas em 1994, surge a proposta de tratar a revisão do 
- Uma emenda constitucional é questionada por diminuir substancialmente garantia individual 
- O texto de uma PEC aprovado pelo Senado difere substancialmente daquele aprovado pela Câma
- Um grupo propõe revogar primeiro a vedação de abolir a federação, para em seguida extingui
- Um Estado pretende tornar válida sua Constituição local por autoridade soberana própria, r
- Tratado internacional de direitos humanos é aprovado em cada Casa do Congresso, em dois tu
- Em 1994, durante a revisão constitucional, surge proposição tendente a abolir garantia ind
- O Distrito Federal altera sua lei orgânica observando dois turnos, dez dias de interstício
- Uma nova Constituição é promulgada após substituição da ordem política vigente. Ainda que 
- Uma PEC de iniciativa presidencial, depois de aprovada nos dois turnos da Câmara e do Sena
- Um Estado edita sua Constituição por meio da autonomia que a Constituição Federal lhe reco
- Uma PEC recebe maioria absoluta de votos em cada Casa, com dois turnos em ambas. Esses req
- Uma proposta de emenda que extinguisse a autonomia essencial dos Estados poderia ser consi
- Uma lei editada antes da Constituição atual, embora incompatível em seu conteúdo com a nov
- A proposta de emenda subscrita por mais da metade das Assembleias Legislativas, com manife
- Em determinada sessão legislativa, uma PEC foi prejudicada. Como a regra do art. 60, § 5º,
- Embora o art. 60, § 4º, preveja cláusulas pétreas, toda modificação de redação de disposit
- Uma norma constitucional originária de 1988 não pode ser declarada inválida apenas por sup
- A aprovação de lei orgânica municipal requer dois turnos, interstício mínimo de dez dias e
- Durante a revisão do art. 3º do ADCT, o Congresso poderia afastar as cláusulas pétreas por
- Quando a interpretação constitucional evolui sem modificação das palavras do texto, pode h
- Uma PEC aprovada por três quintos dos membros em dois turnos da Câmara não precisará alcan
- Se um tratado internacional de direitos humanos for aprovado em cada Casa do Congresso, em
- Em situação de calamidade pública não acompanhada de intervenção federal, estado de defesa
- Uma lei anterior à Constituição atual pode ser recepcionada mesmo que tenha sido produzida
- O caráter ilimitado do poder constituinte originário perante a Constituição anterior signi
- Uma emenda promulgada pelo procedimento do art. 60 não pode ser considerada inconstitucion
- A lei orgânica do Distrito Federal é promulgada pela Câmara Legislativa após dois turnos, 
```

## Pedido 27: Retextualização de gêneros e níveis de formalidade (`portugues.retextualizacao`, 469 tópicos de edital)

```
Matéria: portugues.retextualizacao (Retextualização de gêneros e níveis de formalidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Retextualização, situação comunicativa e níveis de formalidade": 20
- "2. Da fala à escrita: seleção, coesão e precisão": 20
- "3. Transformação entre mensagem, e-mail, ofício e memorando": 20
- "4. Notícias, resumos e relatórios de ocorrência: conservar fatos": 20
- "5. Reescrita avançada: modalização, inferências e revisão crítica": 20

Já existem (não repita):
- Mensagem original: “Oi, você pode me enviar o relatório até sexta? Preciso revisar.” Qual 
- Um aviso informal diz: “Acho que a sala está fechada hoje.” Qual reescrita conserva a ince
- Ao converter a fala “Eu, eu entrei às oito, né?” em registro informativo, sem necessidade 
- Texto original: “A entrega foi remarcada para terça-feira.” Assinale a reescrita mais fiel
- Em registro de ocorrência, lê-se: “Por volta das 10h, o portão foi encontrado aberto.” Qua
- Uma mensagem para colega diz: “Pode confirmar que recebeu o arquivo?” Qual formulação é ap
- Uma notícia será construída com base em “A reunião terminou às 15h e a decisão ainda não f
- Texto-base: “Todos receberam o aviso, mas nem todos confirmaram a leitura.” Qual reformula
- No registro “A pessoa relatou que o elevador parou”, que versão conserva a procedência da 
- Um áudio informa: “Depois do teste, a rede caiu”. Para relatório em que a causa não foi ap
- Em um comunicado interno, o original é: “Se a autorização sair hoje, começaremos amanhã”. 
- Uma instrução orienta a transformar um comunicado interno em e-mail institucional, sem adi
- Texto de ocorrência: “Luís disse que uma janela havia sido quebrada; ninguém verificou a i
- A transformação de “Não foi possível concluir a vistoria hoje” exige conservar a negação e
- Um texto-base usa “A equipe entende que a alteração pode melhorar o atendimento”. Qual red
- Segundo o Manual de Redação da Presidência da República, 3ª edição (2018), uma comunicação
- Ao resumir “O serviço voltou após duas horas, mas algumas unidades ainda operavam lentamen
- Texto-base: “Só três dos nove pedidos poderiam ser avaliados hoje.” Assinale a alternativa
- Uma mensagem relata: “Disseram que o estoque estaria vazio, mas ninguém conferiu”. Para no
- O texto-base diz: “Embora a entrega tenha atrasado, a conferência ocorreu no prazo”. Qual 
- Uma pessoa gravou na segunda-feira: “A análise termina amanhã”. O relato será escrito na q
- Ao transformar “A equipe não confirmou que todos os registros foram corrigidos” em resumo,
- Um relatório deverá registrar: “A testemunha disse que talvez o carro estivesse no pátio à
- Na reescrita de “O pedido foi enviado depois da reunião; não há informação sobre motivo do
- Mensagem de voz: “Acho que duas caixas sumiram ontem; Paulo falou que viu alguém saindo, m
- A mensagem “Envie o arquivo, por favor” pode ser retextualizada como “Solicito o envio do 
- Na passagem de “Talvez a reunião ocorra hoje” para comunicado formal, a frase “A reunião o
- Um e-mail institucional pode acrescentar assunto descritivo e saudação cortês a uma mensag
- Em um relatório, “por volta das 14h” pode ser transformado em “às 14h em ponto”, porque o 
- A retextualização de “A testemunha disse que a porta estava aberta” pode preservar a fonte
- A frase “Nem todos os avisos chegaram” equivale, em qualquer situação, a “Nenhum aviso che
- Num registro de fatos, a conversão de “Depois da inspeção houve atraso” em “Houve atraso a
- No padrão federal da 3ª edição do Manual de Redação da Presidência da República (2018), av
- Ao resumir o trecho “O serviço voltou, embora os atendimentos permanecessem lentos”, deve-
- A transformação de “Acho que o equipamento falhou” em “Foi constatada a falha do equipamen
- Ao redigir notícia com base em informação de terceiro não confirmada, a formulação “segund
- A mudança de “Se houver aprovação, enviaremos a resposta” para “Como houve aprovação, envi
- Na retextualização para relatório, retirar “é...”, uma hesitação sem significado informati
- Dizer “Até quatro pedidos poderão ser avaliados” reproduz necessariamente o significado de
- Se um áudio gravado na terça-feira diz “Entregaremos amanhã”, a reescrita produzida na sex
- A frase “A equipe não confirmou que todos os itens foram revisados” permite inferir com se
- Em relato formal, “A pessoa informou que possivelmente havia ocorrido um vazamento” preser
- Quando o texto-base diz “O atendimento foi retomado após o reparo”, é linguisticamente obr
- Em relatório elaborado com base em “A pessoa relatou ter visto alguém sair, mas não o iden
- Se “O pedido poderá ser atendido após a análise” for reescrito como “O pedido deverá ser a
```

## Pedido 28: Educação profissional e tecnológica e currículo integrado (`educacao.educacao-profissional-tecnologica`, 461 tópicos de edital)

```
Matéria: educacao.educacao-profissional-tecnologica (Educação profissional e tecnológica e currículo integrado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organização legal da educação profissional e tecnológica": 18
- "2. Institutos Federais: identidade, finalidades e organização": 18
- "3. Formação humana integral e fundamentos do currículo integrado": 18
- "4. Projeto Político-Pedagógico do IFRN: identidade e gestão democrática": 26
- "5. Práticas pedagógicas integradoras e desafios de implementação": 20

Já existem (não repita):
- Uma pessoa concluiu o ensino fundamental e ingressou em curso de uma única instituição que
- Uma estudante cursa ensino médio e, em paralelo, mantém matrícula própria em habilitação t
- Após concluir o ensino médio, um trabalhador ingressa em curso técnico sem refazer a forma
- Ao organizar ofertas de uma escola, a equipe separou qualificação profissional, curso técn
- Uma escola busca identificar a lei que instituiu a Rede Federal de EPT e criou os Institut
- Um Instituto Federal mantém cursos de formação inicial, técnicos e superiores em diversos 
- Uma equipe prepara o plano anual de vagas do Instituto Federal e precisa aplicar o art. 8º
- Durante debate curricular, alguém define trabalho como princípio educativo. Qual interpret
- Uma turma pesquisa o consumo de água da comunidade articulando química, história, matemáti
- O IFRN convoca representantes de diferentes segmentos para discutir objetivos e valores in
- Uma instituição analisa se conhecimentos obtidos por uma pessoa no trabalho podem contribu
- Um campus quer articular ensino médio e formação técnica em instituições distintas mediant
- Uma proposta de extensão prevê pesquisar problema ambiental e devolver soluções tecnológic
- Um plano de curso propõe ensinar fundamentos científicos comuns a diferentes técnicas, em 
- Uma escola afirma que desenvolver aspectos intelectuais, técnicos, culturais, sociais e ét
- Ao analisar o PPP do IFRN, uma equipe encontra referências às concepções de ser humano, so
- Dois docentes desenvolvem atividade conjunta: cada um apresenta conteúdo separado, sem que
- A gestão de um Instituto Federal propõe cumprir o mínimo de 20% do art. 8º mediante oferta
- Uma direção sustenta que os Institutos Federais, por serem equiparados às universidades, p
- Em um curso técnico integrado, professores decidem substituir o estudo das bases científic
- Um coordenador afirma que um PPP institucional coerente pode ser homologado por um grupo r
- Uma avaliação de projeto integrador atribui toda a nota ao funcionamento final de uma máqu
- Um projeto articula três componentes curriculares, mas as decisões metodológicas foram imp
- Uma escola apresenta currículo integrado por somar aulas técnicas e gerais no mesmo horári
- Uma equipe pretende utilizar a resolução de 2023 que alterou dispositivo da organização di
- Após analisar a LDB, um estudante conclui que os cursos técnicos integrados unem a formaçã
- Ao matricular um jovem simultaneamente no ensino médio e em curso técnico com registros se
- Uma turma discute o trabalho como atividade humana que transforma a natureza e a vida soci
- O art. 8º da Lei nº 11.892/2008 exige que os Institutos Federais destinem pelo menos 20% d
- Uma proposta institucional afirma que o Projeto Político-Pedagógico do IFRN constitui docu
- Em um projeto de curso técnico, a simples presença de professores de três disciplinas na m
- Um curso técnico concomitante pode ser realizado em instituições distintas por convênio de
- A experiência profissional acumulada por um trabalhador gera, por força do art. 41 da LDB,
- Uma instituição multicampi que organiza ensino, pesquisa e extensão de forma articulada à 
- A condição de autarquia dos Institutos Federais torna desnecessária a autorização do Conse
- Em discussão curricular, a compreensão dos fundamentos científico-tecnológicos de diferent
- Por defender formação integral, o PPP do IFRN reduz suas orientações à preparação para tar
- Um projeto didático em que estudantes formulam hipóteses, recolhem dados, discutem limites
- Uma instituição registra que a equiparação dos Institutos Federais às universidades federa
- No conceito de formação omnilateral, o domínio de várias tarefas de uma mesma ocupação já 
- Ao incluir impactos ambientais, relações sociais, fundamentos técnicos e participação dos 
- Na avaliação de uma atividade interdisciplinar, é mais coerente com a formação integral co
- O art. 10 da Lei nº 11.892/2008 distingue o Colégio de Dirigentes, de caráter consultivo, 
- Uma direção afirma que a atualização pontual de um artigo da Organização Didática do IFRN,
```

## Pedido 29: História e Geografia do Amapá (`amapa.conhecimentos-estaduais`, 458 tópicos de edital)

```
Matéria: amapa.conhecimentos-estaduais (História e Geografia do Amapá)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Cabo Norte, povos indígenas e disputas coloniais": 20
- "2. Período pombalino, Macapá, Mazagão e Cabanagem": 20
- "3. Contestado franco-brasileiro e Laudo Suíço": 20
- "4. Território Federal, grandes projetos, Estado e cultura": 20
- "5. Geografia física, população e economia": 20

Já existem (não repita):
- Antes da colonização europeia, o território do atual Amapá:
- A região entre a foz do Amazonas e o rio Oiapoque, disputada por europeus nos séculos XVI 
- O Tratado de Utrecht, de 1713, encerrou de forma definitiva a disputa de fronteira entre P
- A controvérsia gerada pelo Tratado de Utrecht dizia respeito principalmente:
- A disputa europeia pelo Cabo Norte tinha como um de seus motivos centrais:
- A elevação de Macapá à condição de vila, em 1758, insere-se na política de ocupação da Ama
- A vila de Nova Mazagão, criada em 1770, foi povoada principalmente por:
- A Fortaleza de São José de Macapá foi construída para:
- A Fortaleza de São José de Macapá foi iniciada em 1764 e inaugurada em 1782, com uso de tr
- O governador do Estado do Grão-Pará e Maranhão que impulsionou a fundação de vilas na regi
- A Cabanagem foi uma revolta das elites proprietárias do Grão-Pará contra a abolição da esc
- Sobre a economia e o trabalho na região do Amapá no período colonial, é correto afirmar qu
- O Curiaú, nas proximidades de Macapá, é conhecido como comunidade de origem quilombola.
- A intensificação da disputa franco-brasileira no fim do século XIX esteve associada:
- A República do Cunani, proclamada em 1886 no território contestado, foi reconhecida oficia
- Francisco Xavier da Veiga Cabral, o Cabralzinho, é lembrado na história amapaense por:
- O conflito de 1895 na vila do Amapá, entre o grupo de Cabralzinho e forças vindas de Caien
- O árbitro escolhido para resolver o Contestado franco-brasileiro foi:
- O Laudo Suíço, de 1900:
- A defesa brasileira no Contestado franco-brasileiro foi conduzida pelo Barão do Rio Branco
- O Contestado franco-brasileiro e a Guerra do Contestado ocorrida entre Paraná e Santa Cata
- Após o Laudo Suíço e até 1943, a área do atual Amapá esteve vinculada:
- O Território Federal do Amapá foi criado em:
- A criação dos territórios federais em 1943, entre eles o do Amapá, buscava fortalecer a pr
- Janary Nunes, primeiro governador do Território Federal do Amapá, transferiu a capital da 
- A empresa ICOMI, que atuou na Serra do Navio a partir dos anos 1950, explorava principalme
- A Estrada de Ferro do Amapá foi construída para:
- O Projeto Jari, iniciado no fim da década de 1960 pelo empresário Daniel Ludwig, voltava-s
- Sobre os grandes projetos econômicos do Amapá no século XX, como ICOMI e Jari, a avaliação
- O Amapá foi transformado em Estado pela Constituição Federal de 1988, e sua instalação, co
- O Marabaixo é uma manifestação cultural amapaense:
- A Festa de São Tiago, realizada em Mazagão Velho, encena a luta entre cristãos e mouros e 
- O Amapá faz fronteira internacional com:
- A Linha do Equador atravessa a cidade de Macapá, onde se localiza o Marco Zero.
- O estado do Amapá é composto por 16 municípios.
- Quanto ao relevo do Amapá, é correto afirmar que:
- O clima predominante no Amapá é o:
- A vegetação do Amapá é formada exclusivamente por floresta de terra firme, sem ocorrência 
- A pororoca, fenômeno que ficou famoso no rio Araguari, consiste:
- A população do Amapá concentra-se principalmente em Macapá e Santana, que formam o princip
- Assinale a alternativa que apresenta povos indígenas presentes no Amapá.
- A Área de Livre Comércio de Macapá e Santana caracteriza-se por:
- A Ponte Binacional sobre o rio Oiapoque liga o Amapá ao Suriname.
- Sobre energia e transporte no Amapá, é correto afirmar que:
- O Parque Nacional Montanhas do Tumucumaque é uma das unidades de conservação localizadas n
- O debate sobre a exploração de petróleo na Margem Equatorial, na costa do Amapá, envolve:
- Ordene corretamente os fatos: I – Laudo Suíço; II – Tratado de Utrecht; III – criação do T
```

## Pedido 30: Atendimento ao público (`administracao-publica.atendimento-ao-publico`, 453 tópicos de edital)

```
Matéria: administracao-publica.atendimento-ao-publico (Atendimento ao público)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Qualidade, postura e atributos clássicos do bom atendimento": 26
- "2. Etiqueta, apresentação e atendimento presencial e telefônico": 22
- "3. Comunicação institucional, escrita e canais digitais acessíveis": 18
- "4. Públicos interno e externo, triagem, conflitos e acompanhamento": 17
- "5. Ética, discrição, segurança e melhoria contínua do atendimento": 17

Já existem (não repita):
- Uma usuária não entende a expressão 'instrução pendente'. O atendente a substitui por 'fal
- No início do expediente, um profissional verifica a limpeza da roupa, utiliza identificaçã
- Ao receber uma demanda longa, uma atendente deixa a pessoa concluir, faz perguntas de escl
- Um cidadão contesta uma informação recebida, e a profissional responde em tom respeitoso, 
- Após registrar um pedido, o atendente verifica se ele chegou ao setor competente e informa
- A solicitação pode ser registrada no mesmo dia, mas um profissional a adia sem razão e tam
- Uma unidade reduz devoluções de solicitações entre setores ao implantar triagem correta, s
- Uma pessoa necessita que a orientação seja repetida em ritmo mais lento. A atendente manté
- Durante atendimento em guichê, uma servidora evita mencionar em voz alta dados pessoais de
- Um conhecido solicita atendimento antecipado sem prioridade legal ou agendamento, e o prof
- O balcão está desorganizado, com papéis de atendimentos anteriores à vista e bloqueio parc
- No atendimento presencial, o profissional alterna o olhar entre usuário e sistema e precis
- Uma ligação demanda consulta demorada ao histórico. A atendente precisa colocar a pessoa e
- Uma equipe revisa a apresentação pessoal para atendimento ao público. Que critério evita c
- No início de uma chamada, a pessoa não sabe se ligou ao canal correto e começa a relatar i
- Uma manifestação pública pede explicação sobre decisão ainda não divulgada, e o profission
- Um setor interno solicita documento necessário para responder a um cidadão. A unidade reme
- Uma mensagem institucional contém termos técnicos corretos, mas a maioria dos destinatário
- Uma pessoa comenta em rede social que o serviço exige documentos que não aparecem na descr
- Uma reclamação chega à equipe de atendimento externo e precisa de análise por outra área. 
- Num atendimento de central telefônica, uma pessoa relata situação complexa que não cabe ao
- Um canal de chat identifica dúvida que depende de interpretação humana, mas o robô repete 
- Após conversar por telefone com pessoa insatisfeita, o atendente recebe solicitação de env
- O canal remoto recebe grande número de ligações e sua equipe decide cronometrar cada conve
- Após implantar resposta automática para pedidos simples, a equipe reduz tempo de espera, m
- Em atendimento presencial, a atendente permite que o usuário conclua o relato e reformula 
- Um atendente utiliza linguagem técnica e, ao perceber que a pessoa não compreendeu, repete
- Uma pessoa chega ao balcão com roupa considerada informal, mas apresenta solicitação legít
- Uma pessoa precisa ouvir a mesma instrução duas vezes. A profissional demonstra tolerância
- Antes de transferir uma chamada, a profissional confirma qual área pode solucionar o assun
- Uma solicitação chega por e-mail sem que o setor tenha prazo confirmado. Para mostrar inte
- Um cidadão manifesta insatisfação porque aguardou mais tempo que o previsto na Carta de Se
- Um setor interno depende de dados de outro para responder ao cidadão. Como os profissionai
- Um canal institucional recebe pergunta que exige manifestação oficial ainda não divulgada.
- Num chat, o usuário pede alternativa acessível porque não consegue concluir o formulário e
- Durante videoatendimento, uma pessoa evita contato visual direto. A profissional prefere c
- Ao organizar a fila de um serviço, a unidade deve aplicar estritamente a ordem de chegada,
- Uma pessoa informa que um atendimento foi inadequado e outra apresenta proposta para simpl
- Uma central telefônica aumenta o total de chamadas encerradas por hora, mas cresce o númer
- Uma pessoa busca andamento individual de processo em canal remoto. Antes de divulgar dados
- Uma repórter solicita confirmação de decisão não publicada, e o atendente, sem atribuição 
- Um setor recebeu encaminhamento correto de demanda externa, mas ainda precisa analisar doc
- Ao receber sucessivas ligações sobre uma mesma solicitação, a equipe decide priorizar conh
- Após revisão de atendimento por e-mail, a equipe compara tempo de resposta, clareza das me
- Em ligação de acompanhamento, o atendente acredita que manter discrição exige negar ao pró
```

## Pedido 31: Contratação direta: dispensa e inexigibilidade (`direito-administrativo.contratacao-direta`, 452 tópicos de edital)

```
Matéria: direito-administrativo.contratacao-direta (Contratação direta: dispensa e inexigibilidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Dever de licitar, distinções e processo de contratação direta": 20
- "2. Inexigibilidade do art. 74: exclusividade e escolha justificada": 20
- "3. Dispensa do art. 75: valores, engenharia, emergência e fracionamento": 20
- "4. Alienação dispensada, limites e vedações na contratação direta": 20
- "5. Credenciamento e procedimentos auxiliares relacionados à contratação direta": 20

Já existem (não repita):
- Um ente necessita de equipamento cuja fabricação e comercialização são comprovadamente exc
- Uma unidade pretende adquirir produtos comuns de vários fornecedores, e o custo anual da m
- Uma contratação direta está instruída com justificativa da escolha, mas o processo não dem
- Uma autoridade autoriza contratação por dispensa de licitação e pretende manter o processo
- Um setor indica uma marca específica, embora produtos concorrentes satisfaçam os requisito
- Uma apresentação cultural será contratada diretamente com profissional consagrado, represe
- Uma unidade projeta executar pequeno serviço de engenharia em 2026 e verifica que o somató
- Uma compra de equipamentos comuns totalizará R$ 60.000,00 no exercício e ramo da unidade g
- Uma rede precisa contratar diversos profissionais aptos a prestar serviço padronizado, per
- Uma unidade gestora adquiriu R$ 40.000,00 de materiais do mesmo ramo e pretende adquirir o
- Um consórcio público pretende executar obra pequena por dispensa por valor em 2026, sem ou
- Uma contratação de insumos comuns foi licitada há oito meses, não surgiram propostas válid
- Uma enchente interrompe acesso a unidade de atendimento, exigindo reparo indispensável da 
- Uma entidade pública pretende doar equipamentos móveis para atividade de interesse social,
- A Administração pretende vender imóvel recebido em processo judicial e supõe que essa orig
- Uma contratação de parecer jurídico especializado funda-se na notória especialização de pr
- Um ente precisa alugar determinado imóvel porque sua localização e instalações tornam nece
- Em 2026, certa unidade contrata manutenção de automóvel de sua propriedade, incluídas peça
- Uma contratação direta comum foi assinada e o instrumento deve ser divulgado no PNCP para 
- A Administração quer aproveitar licitação de fornecimento realizada há 14 meses, sem propo
- Durante garantia técnica de equipamento, somente peça do fornecedor original preserva a ga
- A Administração pretende contratar bens comuns padronizados mediante credenciamento eletrô
- Um procedimento recebe documentação de empresas para verificar antecipadamente habilitação
- Para dispensa por valor, uma unidade publica aviso eletrônico oferecendo oportunidade de a
- Durante credenciamento federal, pessoa jurídica mantém vínculo econômico relevante com age
- Uma aquisição comum pode ser licitada como regra, mas as hipóteses de dispensa ou inexigib
- Uma compra de bens comuns em 2026 é dispensável por valor quando seu custo anual da mesma 
- Uma contratação direta de fornecedor exclusivo exige documentação idônea da exclusividade,
- Como não houve disputa licitatória, o art. 72 permite formalizar inexigibilidade mediante 
- Em 2026, a dispensa por valor de obras e serviços de engenharia está ligada ao art. 75, I,
- A doação de um bem móvel pertencente à Administração deve ser enquadrada necessariamente n
- Em dispensa por valor fundada no art. 75, I ou II, a lei recomenda preferencialmente aviso
- Uma unidade faz três contratações de obras do mesmo ramo em um exercício e sustenta que ba
- Artista consagrado é representado por empresa que comprova exclusividade permanente e cont
- Um imóvel público foi adquirido mediante procedimento judicial; o art. 76, §1º, permite su
- Após ocorrência que ameaça continuidade de serviço, o ente pode usar o art. 75, VIII, para
- O aviso preferencial de três dias úteis da dispensa por valor substitui a divulgação do co
- Num credenciamento com seleção a critério de terceiros, o beneficiário direto pode escolhe
- A inexigibilidade do art. 74, III, foi fundamentada na notória especialização de equipe té
- Um consórcio público devidamente constituído pode utilizar, nas dispensas por valor de 202
- Um interessado em credenciamento federal mantém vínculo econômico com agente que atua na c
- Para uma doação com encargo de bem público abrangida pelo art. 76, §6º, o instrumento deve
- Uma obra de pesquisa e desenvolvimento deve obrigatoriamente respeitar o mesmo teto geral 
- A Lei nº 15.266/2025 acrescentou ao art. 79 uma hipótese de credenciamento por comércio el
- Uma ata de registro de preços, por ser procedimento auxiliar previsto no art. 78, IV, prod
```

## Pedido 32: Geotecnia: solos, empuxos, taludes e barragens (`engenharia.geotecnia-solos-barragens`, 450 tópicos de edital)

```
Matéria: engenharia.geotecnia-solos-barragens (Geotecnia: solos, empuxos, taludes e barragens)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Caracterização dos solos, índices físicos, compactação e água": 20
- "2. Exploração do subsolo, sondagens e adensamento de solos moles": 20
- "3. Pressões no solo, empuxos e estruturas de contenção": 21
- "4. Fundações, provas de carga, recalques e acompanhamento de execução": 19
- "5. Estabilidade de taludes e segurança de barragens de terra": 20

Já existem (não repita):
- Uma amostra possui volume de sólidos de 200 cm³ e volume de vazios de 100 cm³. Qual é sua 
- Uma amostra de solo apresenta 500 g de sólidos secos e 100 g de água. Qual valor correspon
- Um ensaio de consistência fornece limite de liquidez de 50% e limite de plasticidade de 30
- Em uma amostra saturada submetida a fluxo laminar, k=1×10⁻⁵ m/s, gradiente i=0,5 e área A=
- Sob determinada camada saturada, a tensão vertical total é de 100 kPa e a pressão neutra h
- Em sondagem SPT, foram anotados 4, 7 e 9 golpes para avançar sucessivamente três segmentos
- Uma camada argilosa tem tensão efetiva atual de 100 kPa e tensão de pré-adensamento estima
- Para mesma argila e grau médio de adensamento, o caminho de drenagem passa de 2 m para 4 m
- Um aterro é executado sobre argila mole saturada com recalques lentos. A equipe propõe ins
- Durante construção de aterro instrumentado, pretende-se medir evolução do excesso de press
- Um muro vertical liso sustenta areia seca com terreno horizontal e ângulo de atrito efetiv
- Em muro de 6 m, areia seca homogênea tem γ=18 kN/m³ e Ka=1/3. Sem sobrecarga e água, qual 
- Aterro horizontal sobre muro vertical de 6 m recebe sobrecarga uniforme q=12 kPa, com Ka=1
- Uma contenção de terreno granular apresenta água acumulada no tardoz até 4 m de altura, se
- Um muro de gravidade apresenta capacidade resistente adequada ao deslizamento, mas a funda
- Uma sapata quadrada de 3 m de lado recebe carga vertical centrada de 900 kN. Desprezados p
- Dois apoios de uma estrutura, separados por 5 m, sofreram recalques verticais de 20 mm e 5
- Estacas atravessam depósito argiloso sujeito a adensamento após lançamento de aterro. O so
- Na avaliação de uma estaca, deseja-se observar a resposta carga–deslocamento sob carregame
- Uma estaca hélice contínua é executada próxima a edificações sensíveis a vibrações. Que me
- Em uma superfície potencial de ruptura, c'=10 kPa, σ'n=100 kPa e φ'=30°. Usando Mohr-Coulo
- Após longa permanência em nível elevado, o reservatório de barragem de terra sofre rebaixa
- Uma barragem apresenta nova surgência de água turva a jusante, com vazão crescente e partí
- Uma barragem está conservada, mas uma eventual ruptura atingiria área densamente ocupada. 
- Uma barragem sob fiscalização da ANA apresenta fissuração progressiva e anomalia que pode 
- Em uma obra de aterro, a compactação mecânica tem como objetivo reduzir o volume de vazios
- Em solo com índice de vazios e=0,80, a porosidade também é 80%, uma vez que as duas grande
- Em ponto saturado sujeito a pressão neutra positiva, a tensão efetiva vertical se calcula 
- Em ensaio SPT com três incrementos de 15 cm que exigiram 3, 6 e 8 golpes, o índice N-SPT u
- Um muro que se afasta suficientemente do aterro granular pode mobilizar o estado ativo, cu
- Em camada argilosa homogênea de 4 m, mudar de drenagem em uma única face para drenagem nas
- Na idealização de Rankine com superfície horizontal, uma sobrecarga uniforme q gera parcel
- Uma sapata transmite tensão média menor que a resistência admissível do terreno. Isso é su
- Surgência turva e progressivamente mais volumosa na fundação a jusante de uma barragem pod
- Em muro vertical sujeito à sobrecarga uniforme no terreno horizontal, a resultante lateral
- Para areia saturada com Gs=2,65 e índice de vazios e=0,65, o gradiente hidráulico crítico 
- Em condição hidrostática com tensão total vertical fixa, o aumento da pressão neutra de 40
- Uma argila com OCR=2 possui tensão máxima efetiva anterior maior que a atual. Na estimativ
- Em solo sobreadensado cuja tensão final permanece abaixo da tensão de pré-adensamento, dev
- Uma contenção muito rígida que praticamente impede deslocamento horizontal do aterro pode 
- No estado passivo de Rankine, basta um deslocamento da parede para fora do aterro para mob
- Para uma estaca, uma prova de carga estática registra deslocamentos em função das cargas a
- Quando o aterro sobre argila mole recalca relativamente à estaca, o atrito negativo que su
- Em barragem de terra, vertedouro com capacidade inadequada pode favorecer galgamento, ao p
- Uma barragem com elevado dano potencial associado devido à população a jusante deve aprese
```

## Pedido 33: Grupos vulneráveis, antidiscriminação e ações afirmativas (`direito.grupos-vulneraveis-antidiscriminacao`, 448 tópicos de edital)

```
Matéria: direito.grupos-vulneraveis-antidiscriminacao (Grupos vulneráveis, antidiscriminação e ações afirmativas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da igualdade e modalidades de discriminação": 26
- "2. Ações afirmativas, igualdade racial e critérios de acesso": 17
- "3. Proteção de mulheres, crianças, adolescentes e pessoas idosas": 20
- "4. Deficiência, diversidade sexual, gênero e mobilidade humana": 16
- "5. Povos tradicionais, exclusão habitacional e mecanismos internacionais": 21

Já existem (não repita):
- Uma política pública oferece formulário digital idêntico a toda a população, mas pessoas c
- Um proprietário anuncia que não alugará seu imóvel a pessoas de determinada origem naciona
- Um atendimento adota requisito aparentemente neutro que, sem fundamento proporcional, excl
- Uma mulher indígena com deficiência relata barreiras específicas resultantes da combinação
- Uma instituição adota bolsa de permanência para estudantes de grupo historicamente excluíd
- Uma pessoa consulta o Estatuto da Igualdade Racial para saber quem integra a população neg
- Uma pessoa é insultada por sua raça, em situação abrangida pelo art. 2º-A da Lei nº 7.716/
- Em uma nova seleção pública federal abrangida pela Lei nº 15.142/2025, discute-se o percen
- Uma estudante que cursou integralmente o ensino médio público pretende ingresso em univers
- Um regulamento exige deslocamentos presenciais sucessivos sem necessidade funcional compro
- Uma mulher perde oportunidade profissional porque o processo seletivo considera sua condiç
- Uma mulher sofre perseguição e controle financeiro por ex-companheiro com quem não mora ma
- Uma criança de 11 anos e um adolescente de 15 anos solicitam proteção contra negligência. 
- Uma atividade educativa distingue os dois Protocolos Facultativos de 2000 ligados à Conven
- Uma pessoa de 61 anos procura proteção específica contra discriminação etária. O enquadram
- Uma plataforma nega recurso de leitura acessível a participante com deficiência, sem demon
- Um grupo debate qual instrumento internacional de direitos humanos foi incorporado com equ
- Um requerimento afirma que a interpretação fixada na ADO 26 e no MI 4.733 criou um novo ti
- Uma pessoa deixa o país porque busca melhores salários, sem relatar perseguição ou violaçã
- Uma pessoa foge de seu país por grave e generalizada violação de direitos humanos e pede r
- Uma comunidade indígena exige respeito a suas línguas, organização social e direitos origi
- Uma comunidade quilombola reivindica titulação territorial com base em autoatribuição, tra
- Uma família usa abrigo provisório para pernoitar, após perder a moradia regular e fragiliz
- Uma resolução internacional de 2007 contém declaração sobre autodeterminação e direitos te
- Uma seleção federal abrangida pela Lei nº 15.142/2025 oferta 7 vagas iniciais. Considerand
- Um setor substitui atendimento exclusivamente presencial por canais acessíveis para reduzi
- Um edital usa critério aparentemente neutro, mas ele exclui desproporcionalmente pessoas c
- Uma instituição oferece mentoria e bolsas para permanência de grupo excluído historicament
- Em seleção federal de pessoal aberta após a vigência da Lei nº 15.142/2025, aplica-se nece
- Uma mulher sofre violência patrimonial em relação íntima de afeto, apesar de já não residi
- Uma pessoa de 60 anos procura proteção estatutária contra negligência. Como ainda não comp
- Uma universidade federal reserva vagas sob a Lei nº 12.711/2012. O percentual mínimo geral
- Uma candidata beneficiária de reserva de vagas na Lei nº 15.142/2025 alcança posição sufic
- Uma criança de 11 anos é considerada criança pelo ECA e também pela Convenção sobre os Dir
- Uma pessoa com deficiência solicita adaptação razoável para participar de atividade públic
- Uma pessoa é reconhecida juridicamente como pessoa com deficiência. Essa condição, isolada
- Um cidadão estrangeiro procura os direitos previstos na Lei nº 13.445/2017. Por não possui
- Uma mulher refugiada com deficiência enfrenta obstáculos específicos de gênero, deslocamen
- Uma candidata encontra edital de seleção federal abrangida pela Lei nº 15.142/2025 com ape
- Um edital federal de seleção de pessoal foi publicado antes da entrada em vigor da Lei nº 
- Uma comunidade quilombola fundamenta seu direito territorial exclusivamente no art. 231 da
- Um serviço público considera pessoa em situação de rua também aquela que utiliza unidade d
- A Declaração das Nações Unidas sobre os Direitos dos Povos Indígenas, de 2007, e a Convenç
- Uma pessoa deixa seu país em razão de grave e generalizada violação de direitos humanos. A
- Segundo o art. 5º, § 3º, da Constituição, tratado sobre direitos humanos aprovado por maio
```

## Pedido 34: Projeto arquitetônico, conforto ambiental e instalações (`arquitetura.projeto-conforto-instalacoes`, 445 tópicos de edital)

```
Matéria: arquitetura.projeto-conforto-instalacoes (Projeto arquitetônico, conforto ambiental e instalações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, programa de necessidades, leiaute e patrimônio": 20
- "2. Representação arquitetônica, perspectiva e CAD": 20
- "3. Insolação, carta solar, brises e comportamento térmico": 20
- "4. Ventilação, exaustão, acústica e iluminação artificial": 20
- "5. Instalações elétricas, elevadores e compatibilização do projeto": 20

Já existem (não repita):
- Durante a concepção de um centro de atendimento, a equipe precisa listar os ambientes, cap
- Um desenho mostra a disposição das salas e paredes, como se o edifício fosse seccionado ho
- Uma linha desenhada com 4 cm em uma planta na escala 1:100 representa que distância real?
- Em um modelo tridimensional de peça arquitetônica, os três eixos são representados com ori
- O estudo de uma fachada pretende conhecer os ângulos de direção e altura do sol em diferen
- Uma sala recebe ruído perceptível através de frestas da porta, enquanto seu tempo de rever
- Um projeto luminotécnico indica fluxo de luz em lúmens e precisa identificar a grandeza co
- Um quadro elétrico necessita de proteção contra sobrecorrentes de um circuito. Qual dispos
- Uma edificação existente está listada em inventário cultural local, mas não há informação 
- O projeto de reforma dispõe de entrada com rampa, mas há degraus entre a chegada e o eleva
- O arquivo CAD apresenta portas repetidas em dezenas de salas. Pretende-se alterar o símbol
- Uma janela é atingida por sol alto no período mais crítico e o estudo solar pretende obstr
- Para certa radiação incidente, uma placa absorve 25% e reflete 60%. Admitido balanço de en
- O ar de uma sala climatizada passa repetidamente pelo aparelho, mas não há entrada previst
- Num modelo simplificado sem perdas, 2.000 lúmens incidem uniformemente sobre superfície de
- O projeto elétrico requer proteção diferenciada para sobrecorrentes, correntes diferenciai
- Em outubro de 2026, um memorial afirma que a nova redação da NR-10, estabelecida pela Port
- O projeto prevê uma intervenção contemporânea no entorno imediato de edificação tombada em
- Uma representação exibe as arestas horizontais oblíquas ao quadro convergindo a dois ponto
- Uma proteção horizontal avança 0,80 m perpendicularmente sobre fachada vertical. Com sol d
- Uma parede homogênea tem espessura de 0,20 m e condutividade térmica de 0,50 W/(m·K). Sem 
- Um ambiente de 180 m³ recebe vazão efetiva de 720 m³/h de ar exterior. Qual é o número teó
- Em duas salas, o mesmo revestimento absorvente reduziu a reverberação, mas ruído de impact
- Dois ambientes de trabalho atendem à mesma iluminância média, porém um tem forte desconfor
- O projeto de um edifício alto prevê elevador de alta velocidade, mas patamar sem espaço ad
- Ao elaborar o projeto de um ambiente de atendimento, o programa de necessidades deve descr
- Por serem instrumentos de proteção do patrimônio, inventário cultural e tombamento federal
- Uma planta baixa e um corte vertical constituem representações diferentes e complementares
- Em uma planta na escala 1:50, um traço de 2 cm no papel representa 2 m reais, porque o den
- A carta solar auxilia a identificar altura solar e azimute em horários e datas distintos, 
- Uma superfície opaca transmite aproximadamente zero radiação incidente; por isso, necessar
- A iluminância sobre uma superfície expressa em lux relaciona-se ao fluxo luminoso incident
- Em CAD, mover um conjunto de portas para camada invisível apaga definitivamente sua geomet
- Uma fachada sob incidência de sol relativamente alto pode receber brise horizontal, desde 
- Ventilação cruzada depende apenas de haver duas janelas, mesmo que ambas estejam na mesma 
- A absorção acústica empregada em superfícies internas pode reduzir a reverberação sem solu
- No planejamento de instalações elétricas, um dispositivo DR substitui a proteção por disju
- A rota até um elevador deve ser compatibilizada com a circulação acessível do edifício; um
- Em perspectiva cônica de dois pontos, todas as linhas verticais do edifício convergem obri
- Em condução térmica estacionária unidimensional numa camada homogênea, aumentar sua espess
- Uma vazão nominal de 600 m³/h para sala de 120 m³ comprova automaticamente que todas as zo
- Duas lâmpadas com a mesma temperatura de cor correlata podem apresentar reprodução de core
- Em outubro de 2026, a redação da NR-10 consolidada pela Portaria MTE nº 737/2026 já se enc
- Em edifício de uso coletivo, a concepção de elevadores precisa considerar não só segurança
- Em área urbanisticamente protegida, intervir na visibilidade e ambiência de um bem tombado
```

## Pedido 35: Enfermagem: legislação, ética, processo de enfermagem e gerenciamento (`enfermagem.legislacao-etica-gestao`, 445 tópicos de edital)

```
Matéria: enfermagem.legislacao-etica-gestao (Enfermagem: legislação, ética, processo de enfermagem e gerenciamento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Exercício profissional, fiscalização e bioética": 23
- "2. SAE, Processo de Enfermagem e documentação": 22
- "3. Resoluções específicas, APH e Telenfermagem": 19
- "4. Segurança do paciente, infecções e técnicas básicas": 16
- "5. Gestão assistencial, APS, liderança e indicadores": 20

Já existem (não repita):
- Uma pessoa concluiu a formação em enfermagem e recebeu proposta para começar a trabalhar, 
- Em uma unidade, um profissional realiza consulta de enfermagem, formula diagnóstico e pres
- O enfermeiro registra dados clínicos, identifica necessidades, prescreve cuidados, acompan
- Em um atendimento, um técnico de enfermagem administra cuidados prescritos, registra o que
- Uma pessoa retorna ao serviço para investigar causas de quedas. A profissional coleta dado
- Uma unidade apresenta dúvidas sobre a utilização de desfibrilador externo automático (DEA)
- Ao organizar uma equipe de suporte intermediário de vida em atendimento pré-hospitalar móv
- Um enfermeiro atende uma pessoa por consulta de enfermagem mediada por tecnologia. Segundo
- Um gestor compara 60 pacientes acompanhados entre 100 elegíveis na atenção primária. Qual 
- Uma pessoa consciente recusa um procedimento não emergencial após receber explicações sobr
- Uma enfermaria teve evolução copiada do dia anterior, sem nova observação, apesar de alter
- No atendimento de urgência, não foi possível obter acesso venoso periférico. Um enfermeiro
- Em uma parada cardiorrespiratória, o DEA está indisponível e há desfibrilador manual. Na e
- Em serviço de emergência, um profissional quer usar luvas para substituir a higiene das mã
- Uma pessoa teve medicamento destinado a outro paciente separado, mas o erro foi identifica
- A direção de um hospital quer reduzir infecções associadas à assistência e, ao mesmo tempo
- Ao receber relato de piora respiratória, um enfermeiro usa comunicação SBAR com dados do p
- No APH móvel, enfermeiro capacitado identifica sinais de pneumotórax hipertensivo com risc
- Um curso apresenta a Resolução nº 718/2023 como autorização atual para ampliar administraç
- Uma ambulância de suporte avançado móvel fica sem médico durante o turno, mas a instituiçã
- Uma instituição deseja implantar Processo de Enfermagem em unidade domiciliar e alega que 
- Uma equipe entende que todo serviço móvel deve criar NSP por força da RDC nº 36/2013 e con
- Na auditoria de um prontuário eletrônico, a checagem de medicação foi feita antes da admin
- Uma unidade informa diminuição de quedas de 12 para 8 entre dois meses. O primeiro teve 1.
- Uma unidade de APS comemorou aumento de 50% no número de consultas, alegando que isso comp
- Em uma unidade ambulatorial, técnico e auxiliar podem integrar a execução e a checagem dos
- O Código de Ética aprovado pela Resolução nº 564/2017 considera facultativo registrar alte
- Em serviço de saúde, a Resolução nº 704/2022 autoriza a equipe de enfermagem a utilizar o 
- O técnico de enfermagem pode formular diagnóstico de enfermagem em nome próprio quando o e
- Em uma pessoa acompanhada pela APS, a etapa de evolução do PE permite comparar os resultad
- Segundo a Resolução nº 713/2022, o suporte intermediário no APH móvel pode ser composto ap
- Em consulta de enfermagem mediada por tecnologia, o consentimento verbal pode ser document
- Uma capacitação em serviço pode usar a Resolução nº 688/2022 e sua alteração nº 718/2023 c
- Uma ocorrência de medicação que foi interceptada antes de alcançar a pessoa pode ser estud
- Os cinco momentos de higiene das mãos dispensam a higiene após retirar luvas quando não fo
- Na atenção primária, o aumento do número de consultas realizadas deve ser examinado como i
- Uma equipe hospitalar pode dispensar a CCIH porque possui NSP instituído nos termos da RDC
- No método SBAR, informar uma medição clínica atual, o contexto relevante e uma recomendaçã
- A Resolução nº 641/2020 permite que qualquer profissional da equipe utilize dispositivo ex
- A Resolução nº 648/2020 recomenda revalidação das habilidades em punção intraóssea no máxi
- A Resolução nº 723/2023 admite que o enfermeiro realize descompressão torácica por agulha 
- Na gestão de enfermagem, o fato de o serviço possuir protocolos assistenciais e equipe red
- Um hospital que apresentou 10 quedas em 2.000 pacientes-dia e outro com 8 quedas em 400 pa
- Na Telenfermagem, a identificação de situação que exige atendimento emergencial impõe reco
- Como a Resolução nº 736/2024 modificou o Processo de Enfermagem, a Resolução nº 358/2009 c
```

## Pedido 36: Odontologia clínica: dentística, cirurgia oral menor, materiais, radiologia e patologia bucal (`odontologia.clinica-cirurgia-materiais-radiologia`, 441 tópicos de edital)

```
Matéria: odontologia.clinica-cirurgia-materiais-radiologia (Odontologia clínica: dentística, cirurgia oral menor, materiais, radiologia e patologia bucal)
Capítulos e quantas questões quero em cada um (total 100):
- "Dentística restauradora e propriedades dos materiais dentários": 17
- "Cirurgia oral menor: exodontia, cicatrização, hemostasia e suturas": 17
- "Radiologia odontológica: indicação, técnicas intrabucais e interpretação": 17
- "Patologia e diagnóstico bucal: raciocínio clínico e biópsia": 17
- "Terapêutica odontológica e emergências médicas no consultório": 16
- "Pacientes com necessidades especiais, ética e responsabilidade técnica": 16

Já existem (não repita):
- Uma restauração adesiva apresenta dificuldade de controle salivar. Qual benefício clássico
- Sobre o cimento de ionômero de vidro convencional, qual alternativa caracteriza sua presa 
- Na aplicação de adesivo universal a uma cavidade com margens amplas em esmalte, qual estra
- Uma resina composta fotopolimerizável foi inserida em camada muito maior do que a espessur
- Na dentina, o sistema convencional do tipo condiciona e lava e o sistema autocondicionante
- Um cimento de ionômero de vidro modificado por resina pode apresentar mecanismo de endurec
- A liberação de flúor pelo ionômero torna dispensáveis o diagnóstico de atividade de cárie 
- Após uma exodontia simples, há sangramento persistente em pequena quantidade. Qual interve
- Paciente volta três dias após exodontia com dor intensa, alvéolo com perda do coágulo e se
- Uma pessoa faz uso de anticoagulante oral e precisa de exodontia de baixo risco hemorrágic
- Em cirurgia oral menor, o principal objetivo de um ponto simples interrompido adequadament
- A alveolite isolada, sem sinais de disseminação infecciosa, exige sempre antibioticoterapi
- A suspensão indiscriminada de antiagregantes ou anticoagulantes para uma exodontia pode ex
- Ao identificar provável comunicação bucosinusal após exodontia superior, deve-se ignorar o
- Qual radiografia intrabucal é particularmente útil para avaliar faces proximais de dentes 
- Em radiografia periapical, os contatos proximais aparecem nitidamente sobrepostos, prejudi
- Em relação à proteção radiológica na odontologia, qual raciocínio melhor traduz a conduta 
- Na interpretação de imagem odontológica, o termo radiolúcido caracteriza região que:
- Na técnica do paralelismo, procura-se posicionar o receptor paralelo ao longo eixo do dent
- A recomendação internacional recente de não utilizar rotineiramente colar tireoidiano em p
- Radiografias devem ser solicitadas com base em uma necessidade diagnóstica individual, e n
- Paciente apresenta placa branca na mucosa oral que não se desprende à raspagem e não possu
- Qual achado deve elevar particularmente a suspeita e a prioridade de investigação de cânce
- Há lesão extensa heterogênea na borda lateral da língua, com área eritematosa e endurecime
- O que distingue uma biópsia excisional de uma incisional em patologia bucal?
- Uma lesão oral que não cicatriza em até 15 dias, especialmente sem causa evidente, deve se
- Toda placa eritematosa de boca é, por definição, câncer invasivo confirmado, mesmo sem exa
- A citologia ou a radiografia podem auxiliar a investigação de lesões orais, mas não substi
- Durante procedimento odontológico, paciente apresenta anafilaxia com dificuldade respirató
- Pessoa com diabetes e sudorese/tremor mantém consciência e consegue engolir. A glicemia ca
- Um adulto imunocompetente apresenta dor dentária localizada, sem febre, comprometimento si
- Na ocorrência de crise convulsiva dentro do consultório, qual atitude imediata é adequada?
- Uma pressão arterial elevada durante o atendimento, sem qualquer avaliação adicional, just
- A ausência de urticária visível afasta de maneira definitiva o diagnóstico de anafilaxia e
- Anestésicos locais atuam predominantemente bloqueando canais de sódio dependentes de volta
- Na consulta de pessoa com deficiência auditiva, qual postura favorece cuidado ético e aces
- Qual conduta é coerente com o consentimento informado em odontologia?
- Sobre o responsável técnico (RT) de clínica odontológica, assinale a proposição correta.
- No Brasil, qual diploma legal regula especificamente o exercício da Odontologia pelo cirur
- Para um paciente com deficiência intelectual, indicação de anestesia geral decorre obrigat
- Um mesmo evento pode dar origem a apurações ética, civil e penal, mas os pressupostos de c
- A assinatura de um termo de consentimento afasta qualquer responsabilidade profissional me
```

## Pedido 37: Práticas comerciais, proteção contratual e defesa do consumidor em juízo (`direito-consumidor.praticas-defesa`, 439 tópicos de edital)

```
Matéria: direito-consumidor.praticas-defesa (Práticas comerciais, proteção contratual e defesa do consumidor em juízo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Oferta, publicidade, comércio eletrônico e práticas abusivas": 21
- "2. Cobrança, dados, cadastro positivo e proteção contratual": 29
- "3. Crédito responsável, superendividamento e repactuação": 14
- "4. Defesa judicial individual e coletiva e tutela específica": 20
- "5. Sanções administrativas, SNDC, serviços regulados e convenção coletiva": 16

Já existem (não repita):
- Uma loja anunciou aparelho com características e preço determinados, mas se recusou a vend
- Um anúncio deixa de informar custo indispensável à contratação, levando o público a acredi
- O fornecedor envia a uma residência revista não solicitada e, depois, cobra o exemplar. Qu
- Um prestador entregou orçamento completo em 1º de abril e nada foi ajustado sobre sua vali
- Na compra em loja virtual, o consumidor percebe erro de quantidade antes de confirmar o pe
- Um consumidor pagou R$ 50 além do devido e não se configurou engano justificável. Qual qua
- Uma anotação negativa sobre dívida de consumo permanece em cadastro e refere-se a período 
- Uma pessoa é incluída em cadastro positivo em 2026 e afirma que seria indispensável autori
- Um contrato de adesão escrito contém cláusulas em fonte corpo 10, com limitação de direito
- Contrato de compra financiada prevê multa moratória de 3% do valor de cada prestação atras
- Uma pessoa natural de boa-fé não consegue pagar dívidas de consumo vencidas e futuras sem 
- Na oferta de financiamento ao consumidor, a proposta informa prestações e CET, mas diz que
- Um consumidor apresenta proposta consensual de pagamento em audiência global do art. 104-A
- Sem conciliação com determinado credor, inicia-se processo judicial de superendividamento 
- Uma financeira pressiona consumidor idoso a contratar empréstimo e não examina informações
- Consumidores distintos receberam cobranças uniformemente indevidas e podem calcular indivi
- Uma associação de defesa do consumidor foi criada há oito meses e pretende ação coletiva d
- O fornecedor foi condenado a retirar cláusula ilegal de contratos em curso. A obrigação po
- Em ação de responsabilidade civil contra fornecedor que contratou seguro, o consumidor pro
- Uma ação coletiva de direitos individuais homogêneos foi julgada improcedente. Consumidor 
- Uma campanha publicitária enganosa reiterada é constatada em processo administrativo. Qual
- Há reclamações relacionadas a práticas de fornecedores em diferentes localidades. Qual des
- Uma convenção coletiva de consumo foi assinada por entidades de fornecedores e consumidore
- Autor de ação individual toma ciência nos autos de ação coletiva sobre os mesmos fatos e p
- Produto regularmente utilizado causa risco coletivo à saúde pública. Os legitimados preten
- Anúncio que descreve de modo suficientemente preciso o preço e as características de um pr
- Publicidade que omite dado essencial sobre o custo de um serviço não pode ser enganosa, po
- Quando serviço é oferecido ao consumidor, o orçamento prévio do art. 40 deve discriminar m
- Uma cobrança indevida de R$ 100 que nunca foi paga gera automaticamente crédito de R$ 200 
- No contrato de adesão escrito, o CDC exige letras legíveis de tamanho não inferior ao corp
- Superendividamento no art. 54-A é fenômeno reservado às pessoas jurídicas, independentemen
- Na abertura de cadastro positivo após a LC nº 166/2019, não se exige consentimento prévio 
- A nota de crédito e o histórico detalhado de adimplemento recebem o mesmo tratamento: ambo
- Ao quitar antecipadamente financiamento de consumo, total ou parcialmente, o consumidor te
- No processo de repactuação do art. 104-A, dívidas decorrentes de financiamentos imobiliári
- O art. 104-B admite processo judicial para revisar e integrar contratos e instituir plano 
- Direitos difusos e direitos individuais homogêneos apresentam necessariamente titulares de
- Em ação coletiva do CDC, o art. 87 afasta adiantamento das despesas indicadas e prevê cons
- Se o fornecedor assegurado pretende envolver a seguradora na ação de responsabilidade civi
- A tutela específica de obrigação de fazer ou não fazer pode contar com multa diária fixada
- Na defesa coletiva de interesses individuais homogêneos, improcedência da ação sempre form
- A Secretaria Nacional do Consumidor integra o sistema de proteção, mas órgãos estaduais e 
- Uma convenção coletiva de consumo assinada entre entidades passa a obrigar todos os fornec
- Nos termos do CDC, a aplicação de sanção administrativa a fornecedor não impede que o cons
- Segundo o art. 104 do CDC, a existência de ação coletiva de direito difuso causa litispend
```

## Pedido 38: Administração de SGBD: manutenção, segurança e bancos distribuídos (`informatica.administracao-sgbd`, 438 tópicos de edital)

```
Matéria: informatica.administracao-sgbd (Administração de SGBD: manutenção, segurança e bancos distribuídos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da administração e arquitetura dos SGBDs": 29
- "2. Backup, restauração e continuidade operacional": 16
- "3. Manutenção, desempenho e tratamento de erros": 20
- "4. Segurança, auditoria e governança operacional": 19
- "5. Bancos distribuídos, replicação e ferramentas de evolução de dados": 16

Já existem (não repita):
- Uma organização precisa estabelecer quem é responsável pela qualidade do cadastro e, separ
- Um servidor reutiliza páginas de dados muito acessadas para reduzir leituras no dispositiv
- Ao examinar um banco ADABAS, a equipe encontra valores de descritores associados a identif
- Uma aplicação adotará banco transacional em memória e exige persistência após reinício ine
- O responsável modifica a localização física dos arquivos de uma tabela, sem alterar seu no
- Uma cópia gerada por pg_dump será usada para reconstruir tabelas e seus dados em outro amb
- Uma base PostgreSQL deve voltar exatamente ao instante anterior a uma exclusão indevida. Q
- Um ambiente Oracle 21c precisa automatizar backups físicos e recuperar arquivos de dados a
- Em SQL Server 2019, um banco está no modelo SIMPLE e a equipe pretende restaurar às 10h43 
- Após um backup completo consistente do MySQL, um incidente exige refazer somente mudanças 
- Um serviço admite perder no máximo 10 minutos de dados, mas pode ficar inoperante por 90 m
- Após grande volume de UPDATE e DELETE no PostgreSQL, a equipe quer tratar versões antigas 
- Um plano de execução estima 30 linhas onde existem aproximadamente 300 mil, produzindo jun
- Duas transações alteram registros em ordem inversa e cada uma espera um bloqueio detido pe
- Uma rotina PL/SQL consulta um único cliente, mas a consulta retorna mais de um registro pa
- Um banco PostgreSQL apresenta muitas linhas mortas e estatísticas defasadas após cargas de
- Uma conta da aplicação somente consulta pedidos, mas recebeu permissão para criar usuários
- Uma base Oracle usa TDE e uma conta autorizada ainda consulta dados por SELECT. Por que is
- Um formulário concatena texto recebido do usuário ao comando SQL, permitindo alterar a clá
- O primário confirma uma escrita e falha antes de enviá-la à réplica assíncrona, que é prom
- Uma atualização deve alterar registros em dois nós com decisão coordenada de confirmação. 
- Um script de migração Flyway já aplicado foi editado, alterando seu checksum em relação ao
- No Pentaho Data Integration, um fluxo lê linhas, filtra registros e carrega uma tabela; ou
- Uma tabela de vendas é separada por região entre servidores distintos para distribuir capa
- Uma equipe pretende investigar quais identidades consultaram dados sensíveis. Os registros
- No planejamento de armazenamento, a equipe aumenta o buffer cache para reduzir leituras fí
- No ADABAS, uma pesquisa por descritor pode usar lista invertida mantida no Associator, rel
- Uma empresa define RPO de cinco minutos. Isso estabelece prazo máximo de cinco minutos par
- No SQL Server 2019 em SIMPLE, é possível produzir backups de log periódicos para obter rec
- Uma rotina PostgreSQL executa ANALYZE após mudanças de distribuição. Sua finalidade princi
- Uma aplicação necessita apenas de leitura de uma visão. Retirar privilégios administrativo
- Um backup do PostgreSQL realizado com pg_dump contém automaticamente a cadeia integral de 
- Duas transações aguardam uma à outra por bloqueios em recursos diferentes. A dependência c
- No Oracle 21c, RMAN pode participar de rotinas de backup e recuperação, ao passo que PL/SQ
- Se o fluxo de transações MySQL tiver binary log íntegro, esse log dispensa uma base consis
- Uma nova indexação pode reduzir leituras de uma consulta seletiva, mas aumentar custo de a
- No Flyway, alterar manualmente uma migração versionada já aplicada pode produzir divergênc
- O emprego de TDE no Oracle garante que uma identidade com privilégio SELECT não possa rece
- Em replicação assíncrona PostgreSQL, pode existir atraso entre commit confirmado no primár
- O protocolo 2PC elimina o risco de espera por coordenador durante falhas porque todos os p
- Uma transformação no Pentaho Data Integration pode processar linhas por steps, enquanto um
- Como uma réplica sincroniza alterações do primário, ela protege por si só contra um DELETE
- Uma transação distribuída em saga usa confirmações locais e pode empregar compensações, di
- No SQL Server em FULL, a simples seleção do modelo garante PITR ilimitado mesmo quando os 
- Um plano de manutenção auditável deve declarar janelas de execução, responsáveis, métricas
- O H2 Database é um SGBD relacional escrito em Java que pode funcionar embutido na aplicaçã
- Em um projeto Java, a equipe quer um banco que suba junto com os testes de integração, em 
```

## Pedido 39: Coisa julgada (`processo-civil.coisa-julgada`, 437 tópicos de edital)

```
Matéria: processo-civil.coisa-julgada (Coisa julgada)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sentença, capítulos decisórios e efeitos": 14
- "2. Autoridade, limites e questões prejudiciais da coisa julgada": 20
- "3. Preclusão, eficácia preclusiva e vícios transrescisórios": 15
- "4. Ação rescisória: hipóteses, legitimidade, procedimento e prazos": 35
- "5. Coisa julgada inconstitucional, revisão excepcional e conexões processuais": 16

Já existem (não repita):
- Uma decisão rejeitou definitivamente um pedido de indenização após exame de seu mérito e j
- Um autor manifesta desistência da ação depois de oferecida a contestação, e o réu concorda
- Uma sentença reproduz artigo legal, sem relacionar o comando aos fatos controvertidos, e c
- Uma decisão definitiva condena a pagar dívida e menciona, nos fundamentos, a data de envio
- Em cobrança contratual, uma questão prejudicial foi expressamente decidida, era essencial 
- Uma pessoa estranha à demanda anterior teme ser prejudicada por sentença definitiva profer
- Após perder definitivamente ação de cobrança, uma parte procura reapresentar a mesma prete
- Uma parte ajuíza ação autônoma para desconstituir decisão definitiva por prova falsa decis
- O pedido formulado é de condenação limitada a R$ 40 mil, mas a sentença concede R$ 65 mil 
- Uma condenação ao pagamento de quantia é impugnada por recurso com efeito suspensivo, e o 
- Uma sentença líquida condena determinado município que não é capital, em valor corresponde
- Uma questão sobre validade contratual foi decidida expressamente e era indispensável ao re
- Uma decisão definitiva regula prestações periódicas considerando renda comprovada. Anos de
- Uma decisão de mérito transitou em julgado após ter sido proferida por juízo absolutamente
- A decisão definitiva declarou inexistente um fato registrado de maneira incontroversa nos 
- Uma pessoa sem gratuidade pretende ajuizar rescisória por hipótese do art. 966 e consulta 
- Em demanda julgada definitivamente, a última decisão do processo transitou em julgado em d
- Após o trânsito em julgado, uma parte descobre documento decisivo que já existia, mas de c
- Uma decisão definitiva não examinou o mérito, mas impediu a admissibilidade do recurso cab
- Uma decisão aplicou enunciado de julgamento repetitivo a situação fática demonstravelmente
- Uma obrigação é reconhecida em título transitado em julgado depois de decisão constitucion
- Uma pessoa permaneceu revel porque não houve citação válida no conhecimento. Em caso disti
- Durante cumprimento de sentença contra ente público, descobre-se que pronunciamento consti
- Uma decisão definitiva afastou obrigação tributária em relação de trato continuado. Depois
- Uma sentença ainda é atacada por recurso desprovido de efeito suspensivo. A parte vencedor
- Uma sentença aprecia definitivamente o pedido de indenização e não está sujeita a recurso.
- Ao homologar desistência da ação depois de contestação e concordância do réu, o pronunciam
- Em uma cobrança, a validade do contrato foi resolvida expressamente como questão prejudici
- Uma sentença condenatória afirma, na fundamentação, determinada data como fato verdadeiro.
- Em processo entre duas partes, um terceiro estranho à relação processual é afetado indiret
- Uma parte perdeu ação de cobrança e pretende repetir exatamente a demanda usando defesa an
- Após alteração superveniente relevante da situação que fundamentou prestações periódicas, 
- Uma decisão definitiva foi proferida por julgador suspeito, embora não impedido, e a parte
- Uma decisão definitiva possui três capítulos autônomos, e o fundamento rescisório atinge s
- Uma pessoa recebeu gratuidade da justiça e pretende propor rescisória. Mesmo assim, o depó
- Uma parte ajuíza ação rescisória, mas não obtém tutela provisória para sustar o cumpriment
- O art. 975, § 2º, autoriza ação rescisória fundada em prova nova por dois anos contados da
- Uma sentença líquida contra município não capital produz proveito econômico de 90 salários
- Uma decisão constitucional sobre a norma do título foi proferida somente após o trânsito e
- No cumprimento contra ente público, o art. 535, §§ 5º e 7º, permite discutir inexigibilida
- Uma sentença extra petita foi proferida em processo com citação regular e contraditório ef
- Uma decisão não de mérito transitada em julgado impediu a admissibilidade de recurso, e há
- Em relação tributária de trato continuado, os Temas 881 e 885 afirmam que decisões inciden
- Depois de publicada a sentença, detectou-se erro material evidente de digitação na identif
- Uma sentença sujeita à remessa necessária do art. 496 não recebe recurso voluntário. A fal
```

## Pedido 40: Sistemas lineares (`matematica.sistemas-lineares`, 436 tópicos de edital)

```
Matéria: matematica.sistemas-lineares (Sistemas lineares)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Equações lineares, soluções e modelagem": 20
- "2. Substituição, adição e escalonamento": 20
- "3. Classificação, parâmetros e sistemas homogêneos": 22
- "4. Matrizes, determinantes e regra de Cramer": 19
- "5. Interpretação gráfica e problemas avançados": 19

Já existem (não repita):
- Ao conferir o controle de duas quantidades, obtiveram-se as equações $$x+y=7$$ e $$x-y=1$$
- Um estudante precisa selecionar um sistema composto apenas por equações lineares em x e y.
- Um técnico sugeriu cinco pares para satisfazer $$3x-2y=4$$ e $$x+y=3$$. Qual sugestão efet
- Em uma distribuição de unidades, a quantidade x é o dobro de y e a soma delas é nove. Que 
- Duas planilhas registram $$2x+2y=10$$ e $$x+y=5$$. Considerando x e y reais, como se class
- Duas quantidades positivas somam 23 e a maior supera a menor em cinco. Se x é a maior, qua
- Foram vendidos 20 ingressos, alguns de valor 12 e outros de valor 8, com arrecadação total
- Em um estudo de retas, as equações são $$x+y=4$$ e $$x+y=5$$. Como se classifica a interse
- O sistema $$2x+y=11$$ e $$x-y=1$$ será resolvido por substituição. Qual par representa a s
- Um conjunto de medições obedece a $$3x+y=13$$ e $$x+y=7$$. Aplicando a eliminação, qual re
- O sistema de três incógnitas informa $$x+y+z=9$$, $$x-y=1$$ e $$z=2$$. Qual trio ordenado 
- Após simplificar um sistema, restam $$x+2y=8$$ e $$2x+4y=16$$. Qual interpretação da reduç
- Ao eliminar incógnitas das expressões $$2x+y=5$$ e $$4x+2y=11$$, qual é o resultado corret
- Uma solução por substituição foi solicitada para $$3x-y=5$$ e $$x+y=3$$. Que par resulta d
- Uma dupla de condições é $$2x+3y=16$$ e $$x+y=6$$. Qual par corresponde ao resultado da el
- Na matriz aumentada, o sistema corresponde a $$3x+2y=13$$ e $$x-y=1$$. Qual par se encontr
- Um sistema em três incógnitas tem as condições $$x+y+z=6$$, $$2x-y+z=6$$ e $$x-y+2z=7$$. Q
- Para estudar um parâmetro real k, considere $$x+y=4$$ e $$2x+2y=k$$. Qual valor torna o si
- Uma discussão algébrica envolve $$kx+y=3$$ e $$2x+y=3$$. Qual enunciado classifica correta
- Um sistema homogêneo tem equações $$x+y=0$$ e $$2x-3y=0$$. Qual afirmação sobre suas soluç
- O sistema homogêneo $$x+2y=0$$ e $$2x+4y=0$$ foi colocado em discussão. Qual conclusão é m
- Um gráfico reúne as retas de $$x+y=5$$ e $$2x+2y=10$$. Que descrição corresponde à interse
- Para $$2x+y=5$$ e $$x+y=3$$, um aluno pretende usar Cramer. Quais são, respectivamente, D,
- A matriz dos coeficientes de um sistema 2x2 tem primeira linha (3,2) e segunda linha (6,4)
- Um sistema triangular está escrito como $$x+y+z=6$$, $$y+z=3$$ e $$z=1$$. Qual é a sua sol
- Em uma conferência, o par (4,2) satisfaz as igualdades $$x+y=6$$ e $$x-y=2$$; portanto res
- Ao analisar a igualdade $$x+y=5$$, uma pessoa registrou o par (2,1) como solução válida de
- Em um sistema com duas equações, um par só é solução se a substituição de suas coordenadas
- Um estudante concluiu que $$x+y=3$$ e $$x+y=4$$ representam a mesma reta, porque os coefic
- Na igualdade $$x+2y=6$$, atribuir livremente um valor real a y e calcular x a partir dele 
- Ao classificar expressões, foi considerada linear a equação $$x^2+y=4$$, sob o argumento d
- Para resolver $$x+y=7$$ e $$x-y=3$$, somar as equações elimina y e permite encontrar diret
- Se um sistema linear é homogêneo, a inexistência de termos independentes diferentes de zer
- Durante o escalonamento, uma linha se transformou em 0=0. Essa linha não acrescenta restri
- Uma matriz quadrada de coeficientes possui determinante zero. Por isso, sem examinar os te
- No sistema $$x+2y=3$$ e $$2x+4y=6$$, a matriz dos coeficientes tem determinante zero e exi
- Em um escalonamento, multiplicar uma equação inteira por zero e substituir a linha origina
- O sistema $$x+y+z=3$$, $$x-y=1$$ e $$z=1$$ tem solução única com x igual a 1,5 e y igual a
- Se três planos no espaço se intersectam dois a dois, necessariamente compartilham um mesmo
- Um sistema quadrado 2x2 possui matriz dos coeficientes com linhas (2,1) e (1,2). Independe
- Como $$x+y=4$$ e $$2x+2y=9$$ têm determinante zero, existe uma única solução que pode ser 
- Uma produção tem x igual ao dobro de y e total igual a seis. O par ordenado (4,2) resolve 
- Um sistema homogêneo tem as equações $$x+2y=0$$ e $$2x+4y=0$$. Sua única solução é a orige
- Para $$x+y=5$$ e $$2x+2y=k$$, o valor k igual a dez torna o sistema possível e indetermina
- Quando o determinante dos coeficientes de um sistema quadrado é zero, a regra de Cramer co
```

## Pedido 41: Variáveis aleatórias e distribuições (`estatistica.variaveis-aleatorias-distribuicoes`, 434 tópicos de edital)

```
Matéria: estatistica.variaveis-aleatorias-distribuicoes (Variáveis aleatórias e distribuições)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Variáveis aleatórias e funções de probabilidade": 33
- "2. Esperança, variância, momentos e dependência": 23
- "3. Bernoulli, binomial e modelos de contagem": 13
- "4. Distribuição normal, padronização e áreas": 16
- "5. Distribuições especiais, relações e identificação": 15

Já existem (não repita):
- Uma central registra o número de interrupções de energia por dia, podendo observar 0, 1, 2
- Uma variável discreta assume 0, 1 e 2 com probabilidades 0,20, 0,35 e p, respectivamente. 
- O tempo de conclusão de uma tarefa é modelado por uma variável contínua T com função densi
- Uma caixa entrega prêmio X de 0 unidade com chance 0,4 e de 4 unidades com chance 0,6. Qua
- Uma variável X tem variância igual a 5. Se Y=X+12 representa a mesma medição acrescida de 
- Um sensor registra X=1 quando detecta defeito e X=0 caso contrário. A chance de defeito é 
- Um processo realiza 10 verificações independentes, cada qual com chance 0,2 de reprovação.
- Três componentes independentes têm cada um probabilidade 0,1 de falhar. Qual é a chance de
- Em três inspeções independentes, a chance de encontrar falha em cada uma é 0,2. Qual é a p
- Uma variável segue N(40,25), sendo o segundo parâmetro a variância. Qual percentual aproxi
- Uma leitura de 76 unidades provém de uma população normal com média 60 e desvio padrão 8. 
- Para Z normal padrão, sabe-se que Φ(1)=0,8413. Qual é, aproximadamente, P(Z>1)?
- Duas medições X e Y têm desvios padrão 2 e 3, e covariância igual a -3. Qual é sua correla
- Considere Var(X)=4, Var(Y)=9 e Cov(X,Y)=2. Qual é a variância da soma X+Y?
- Uma distribuição possui esperança 3 e segundo momento bruto E(X²)=11. Qual é o segundo mom
- Uma sequência independente de tentativas tem probabilidade constante 0,2 de sucesso. Qual 
- Uma central recebe ocorrências segundo Poisson com taxa média de 1,5 por hora. Em duas hor
- Um tempo de espera T segue exponencial com taxa 0,5 por minuto. Sabendo que T já excedeu 3
- Um comprimento X é uniforme contínuo em [2,10]. Dado que X>4, qual a probabilidade de X>6?
- Uma urna contém três bolas vermelhas e duas azuis. Duas bolas são retiradas sem reposição.
- Se Z é normal padrão e V é qui-quadrado com 7 graus de liberdade, independentes, qual a di
- Uma variável tem distribuição t com 5 graus de liberdade. Qual é a sua variância teórica?
- Se U segue qui-quadrado com 4 graus de liberdade e V, independente, qui-quadrado com 6, qu
- Para X e Y com variâncias 4 e 9 e covariância 2, qual é a variância da diferença X-Y?
- Uma variável X tem P(X=0)=1/4, P(X=1)=1/2 e P(X=2)=1/4. Qual par representa E(X) e Var(X)?
- Um painel mostra F(2)=0,7 para uma variável discreta que pode assumir os valores inteiros 
- Um pesquisador observa que uma densidade contínua assume valor 1,4 em certo ponto e conclu
- Duas medições têm covariância zero. Como resultado, o analista pode concluir que elas são 
- Uma compensação fixa de 4 unidades é adicionada a todos os valores de X. A esperança aumen
- Para um registro Bernoulli com probabilidade de sucesso 0,2, a variância teórica é igual a
- Ao retirar cinco peças sem reposição de um lote pequeno, a contagem de defeituosas segue n
- Se X segue distribuição normal com média 12, a probabilidade de X ultrapassar 12 é 0,5, qu
- Para uma variável com média μ, o segundo momento bruto é o mesmo que a variância, independ
- No modelo N(100,16), uma observação 108 está a meio desvio padrão acima da média porque o 
- Em cinco ensaios binomiais independentes com chance p de sucesso, a probabilidade de ao me
- Se o número de chamadas por minuto é Poisson com λ=4, a variância da contagem nesse minuto
- Em tentativas independentes com p=0,25, a variável geométrica que conta tentativas até o p
- Se X é uniforme contínua entre 2 e 8, a probabilidade de X pertencer ao intervalo entre 3 
- Para um tempo exponencial parametrizado por taxa λ=2 por minuto, a espera média é de dois 
- Se U é a soma dos quadrados de seis variáveis normais padrões independentes, sua distribui
- Uma variável F com graus de liberdade 4 no numerador e 8 no denominador tem esperança 4/(4
- A variância de uma t de Student com exatamente dois graus de liberdade é 2/(2-2) e, por is
- Se X e Y possuem variâncias 4 e 9 e covariância positiva igual a 2, a variância de X-Y é 9
- Para uma variável discreta com F(1)=0,4 e F(2)=0,8, sem valores possíveis estritamente ent
- Uma variável segue distribuição normal com média 10 e desvio padrão 2. Como aproximadament
```

## Pedido 42: Crimes ambientais e responsabilidade penal (`legislacao-penal-especial.crimes-ambientais`, 434 tópicos de edital)

```
Matéria: legislacao-penal-especial.crimes-ambientais (Crimes ambientais e responsabilidade penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Responsabilidade ambiental e aplicação das penas": 22
- "2. Crimes contra fauna, pesca, tráfico e maus-tratos": 19
- "3. Flora, vegetação, incêndios e patrimônio urbano e cultural": 20
- "4. Poluição, substâncias perigosas e administração ambiental": 18
- "5. Ação penal, competência, sanções administrativas e reparação": 21

Já existem (não repita):
- Uma empresa é investigada por crime ambiental decidido por seu colegiado em benefício da p
- Uma pessoa é acusada de capturar animal silvestre nativo sem autorização, fora de hipótese
- Uma pessoa pratica maus-tratos contra um cão, sem que o caso dependa das hipóteses de tatu
- Em uma área florestal protegida, alguém provoca incêndio intencional na vegetação. Qual di
- Uma empresa constrói estabelecimento potencialmente poluidor sem a licença exigida, em sit
- Uma pessoa registra ocorrência de crime tipificado na Lei nº 9.605/1998. Qual é, pelo art.
- Uma autoridade apura infração administrativa ambiental nos termos da Lei nº 9.605/1998. Qu
- Após autuação por infração de menor potencial ofensivo, avalia-se transação penal ambienta
- Uma pessoa mantém em casa, sem licença, animal silvestre não ameaçado de extinção. Segundo
- Uma pessoa pesca com explosivos em águas sujeitas à legislação ambiental. Qual tipificação
- Uma pessoa apresenta pedido fundamentado de poda por risco de acidente, comprovado por lau
- Uma empresa lança poluentes em níveis capazes de causar danos à saúde humana, embora nenhu
- Uma pessoa elabora relatório ambiental falsificado e o apresenta em procedimento de licenc
- Em um caso de crime contra a fauna sem transnacionalidade nem interesse federal concreto d
- Um órgão ambiental examina infração administrativa de menor lesividade no Decreto nº 6.514
- Uma entidade sofre pena penal restritiva de proibição de contratar com o poder público, no
- Em 2026, alguém realiza tatuagem com fins estéticos em um gato, sem indicação terapêutica.
- Um incidente provocado deliberadamente em 2026 produz desastre ambiental que prejudica o b
- Uma licença ambiental é concedida em 2026 por negligência, sem prova de conhecimento e dol
- Um empreendimento que exigiria Estudo Prévio de Impacto Ambiental é instalado ilegalmente,
- Um agente público obsta fiscalização de resíduos perigosos. Ao diferenciar a tipificação p
- Há persecução de crime ambiental com envio transnacional de espécimes de fauna silvestre p
- Em crime contra exemplar de espécie vegetal ameaçada incluída em lista oficial nacional, d
- Após encerrada a instrução no processo administrativo federal ambiental, uma pessoa autuad
- Um responsável paga integralmente a multa administrativa por desmatamento, mas a área perm
- Uma pessoa jurídica pode responder penalmente por crime ambiental quando a infração deriva
- A Lei nº 9.605/1998 exige que a pessoa física responsável seja condenada antes de admitir 
- O art. 29 da Lei nº 9.605/1998 pune, nas condições descritas, a captura e a caça de espéci
- A captura de pescado em período proibido submete-se diretamente ao art. 29, pois esse disp
- Em 2026, o art. 32, § 1º-B, também trata de quem realiza ou permite tatuagem ou piercing e
- Após as mudanças de 2025, o art. 60 mantém pena de detenção de um a seis meses para ativid
- Nas infrações penais previstas na Lei nº 9.605/1998, o art. 26 estabelece ação penal públi
- Na guarda doméstica não autorizada de espécie silvestre não ameaçada, o art. 29, § 2º, ext
- O art. 41 da Lei nº 9.605/1998, alterado em 2024, alcança provocar incêndio em floresta ou
- No art. 54, o crime básico de poluição requer comprovar que determinada pessoa sofreu dano
- Segundo o art. 69-A, § 1º, há modalidade culposa para elaboração ou apresentação de docume
- O art. 27 da Lei nº 9.605/1998 impede a transação penal ambiental quando a composição prév
- O Decreto nº 6.514/2008, art. 5º, § 1º, prevê multa consolidada de até R$ 1.000 como parâm
- No processo administrativo ambiental federal, encerrada a instrução, o autuado tem vinte d
- O art. 49, § 2º, inserido em 2025, pode afastar o crime de poda de árvore nas condições es
- Após a Lei nº 15.190/2025, o art. 67 continua punindo expressamente a concessão culposa de
- Em crime ambiental de caráter transnacional que envolva animais silvestres nas condições e
- Quando se paga multa administrativa por infração ambiental, ficam extintos, pelo art. 225,
- Nos crimes ambientais com suspensão condicional do processo, o art. 28 da Lei nº 9.605/199
- O Decreto nº 6.514/2008, art. 141, permite converter a multa administrativa em serviços de
```

## Pedido 43: Lei 8.112/1990: sindicância e processo administrativo disciplinar (`legislacao.lei-8112-1990-processo-disciplinar`, 433 tópicos de edital)

```
Matéria: legislacao.lei-8112-1990-processo-disciplinar (Lei 8.112/1990: sindicância e processo administrativo disciplinar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, admissibilidade e espécies de sindicância": 18
- "2. Instauração, comissão, imparcialidade e medidas cautelares": 19
- "3. Rito ordinário: instrução, defesa, relatório e julgamento": 17
- "4. Rito sumário, prazos especiais e solução consensual": 17
- "5. Nulidades, controle, recursos e revisão disciplinar": 29

Já existem (não repita):
- Uma comunicação descreve lançamentos incompatíveis em um sistema, mas ainda não há element
- Uma apuração identifica crescimento expressivo de patrimônio declarado sem correspondência
- Após análise preliminar, há indícios de falta funcional punível, em tese, com advertência,
- Os fatos reunidos indicam possível infração que, confirmada, pode justificar suspensão sup
- Uma comissão encerrou a apuração de falta de pequeno potencial ofensivo, com defesa efetiv
- Em procedimento ordinário, após a publicação da portaria, são colhidas provas, apresentada
- A autoridade instaura PAD ordinário e deverá constituir comissão conforme o art. 149 da Le
- Um dos três membros estáveis da comissão possui cargo efetivo de nível inferior ao do indi
- Durante a instrução, surgem indícios concretos de que o investigado está pressionando test
- A comissão de PAD ordinário foi constituída e publicada na segunda-feira. A autoridade per
- Foi indicado para a comissão de PAD o irmão do acusado, apesar de reunir os requisitos de 
- Um integrante de comissão mantém amizade íntima com o acusado, sem parentesco ou interesse
- No PAD, o indiciado solicita perícia sobre fato que pode ser comprovado por leitura simple
- Uma testemunha intimada a depor apresenta um documento com seu relato previamente escrito 
- No mesmo PAD ordinário, dois servidores foram formalmente indiciados e citados. Não se tra
- O indiciado foi regularmente citado no PAD ordinário, mas deixou transcorrer o prazo sem d
- Após apresentar relatório final conclusivo, a comissão de PAD ordinário remete os autos à 
- Há suspeita de ausência intencional superior a trinta dias consecutivos, documentada por r
- Antes de instaurar rito sumário por acumulação supostamente ilegal de vínculos, a autorida
- Uma portaria instaura PAD sumário por acumulação ilegal e designa dois integrantes estávei
- Uma autoridade confunde as regras atuais das sindicâncias do sistema correcional federal. 
- No rito sumário por acumulação ilegal, um servidor omitiu-se nos dez dias iniciais, mas, r
- Em PAD sumário por acumulação, a comissão lavrou termo de indiciação no prazo de três dias
- Após condenação em PAD, o servidor apresenta pedido de reconsideração dentro de trinta dia
- Anos após sanção disciplinar, aparecem documentos novos, antes desconhecidos, capazes de d
- Uma autoridade recebeu informação documentada de irregularidade plausível relacionada ao e
- A sindicância patrimonial instaurada por incompatibilidade entre evolução de bens e rendim
- Havendo risco concreto de interferência de um servidor nos depoimentos, a autoridade insta
- Uma autoridade poderá instaurar PAD ordinário com comissão de dois servidores estáveis, de
- Um indiciado regularmente citado não apresentou defesa escrita; nessa circunstância, a Adm
- A SINVE destinada a identificar autoria pode durar sessenta dias, porém só admite uma pror
- Uma sindicância acusatória por possível advertência, não solucionável por TAC, requer obse
- O PAD ordinário instaurado com três servidores estáveis deve ser concluído em trinta dias 
- Em comissão de apuração, a amizade íntima de um de seus membros com o acusado pode ensejar
- Ao ouvir testemunhas, a comissão pode aceitar que cada uma entregue depoimento escrito pre
- Em PAD ordinário, dois servidores formalmente indiciados receberam citação pessoal, e a co
- No PAD ordinário, a autoridade que julga poderá agravar a proposta de penalidade formulada
- Em uma apuração com indiciado em lugar incerto e não sabido, a citação por edital feita no
- Um PAD ordinário foi concluído fora do prazo previsto no art. 152. Ainda que o acusado não
- Ao apurar abandono de cargo pelo rito sumário, a comissão deve individualizar o período de
- Detectada acumulação ilegal, o servidor notificado deixa transcorrer os dez dias para opçã
- Um agente efetivo com infração de menor potencial ofensivo pretende celebrar TAC. A verifi
- Depois de decisão punitiva, o servidor protocolou pedido de reconsideração ao decisor orig
- Uma comissão revisora instaurada após surgimento de documentos novos dispõe de sessenta di
- Diante de fatos novos que comprovem conduta mais grave, a autoridade poderá agravar a pena
```

## Pedido 44: Crédito público, dívida pública e precatórios (`direito-financeiro.credito-publico-divida-precatorios`, 433 tópicos de edital)

```
Matéria: direito-financeiro.credito-publico-divida-precatorios (Crédito público, dívida pública e precatórios)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Crédito público: conceito, natureza e classificações": 25
- "2. Dívida pública: dívida fundada, flutuante e títulos do Tesouro": 20
- "3. Endividamento: competências, limites e operações de crédito": 18
- "4. Antecipação de receita e controle da dívida pública": 19
- "5. Precatórios: ordem, prioridades e alterações constitucionais recentes": 18

Já existem (não repita):
- Um ente obtém recursos por empréstimo bancário voluntário para financiar uma obra. Qual é 
- Ao conferir o balanço, uma pessoa identifica depósitos de terceiros e serviços da dívida a
- O Estado contrata por oito anos financiamento para construir uma ponte. Pela Lei 4.320/196
- Um município busca financiamento internacional para modernizar sua rede de abastecimento. 
- Na análise da dívida consolidada líquida de município com RCL de R$ 100 milhões, qual é o 
- Ao estruturar novo empréstimo, a administração prevê a modalidade de arrendamento mercanti
- Um gestor pretende contratar operação de antecipação de receita para cobrir insuficiência 
- A pessoa titular de precatório alimentar tem 65 anos e crédito superior ao teto das requis
- Após a EC 136/2025, precatório apresentado dentro do marco constitucional anual deve const
- Uma emissão do Tesouro oferece remuneração atrelada ao IPCA mais taxa contratada e não dis
- Ao calcular o comprometimento anual do serviço da dívida de um Estado sujeito à Resolução 
- O ente pretende contratar empréstimos que superariam as despesas de capital do exercício. 
- Um ente contrata operação financeira com prazo de onze meses, mas registra sua receita na 
- Um município tem RCL anual de R$ 80 milhões. Considerando operação comum sujeita ao limite
- Uma ARO foi contratada validamente durante um exercício e o gestor pretende encerrá-la em 
- Município tem estoque de precatórios em mora em 1º de janeiro equivalente a 20% da RCL do 
- Após análise de contratos, constatou-se obrigação de financiamento e pagamento de juros no
- Um ente ultrapassa o limite da dívida consolidada ao final de um quadrimestre, sem situaçã
- Uma operação ordinária de crédito de R$ 18 milhões é proposta por ente com RCL de R$ 100 m
- Estado possui, em 1º de janeiro, estoque de precatórios em mora equivalente a 90% da RCL a
- Um Município já atingiu o percentual ordinário anual de precatórios da faixa do art. 100, 
- Requisitório contra Município expedido após 1º de agosto de 2025 está sujeito a atualizaçã
- No último ano do mandato municipal, o gestor pretende obter ARO para enfrentar insuficiênc
- Uma pessoa de 61 anos sucede hereditariamente crédito alimentício em precatório no valor d
- Em planejamento fiscal, a equipe atribui ao Senado o poder de limitar a dívida mobiliária 
- Uma administração recebe empréstimo voluntário para investir em uma escola pública. A oper
- Na classificação do art. 92 da Lei 4.320/1964, restos a pagar incluem também os serviços d
- Uma entidade pública emite títulos negociáveis para captar recursos. A obrigação integra a
- Um Município considera permitido manter dívida consolidada líquida de até duas vezes sua r
- Uma ARO tomada para cobrir insuficiência temporária de caixa deve ser liquidada, com encar
- Um precatório apresentado em 20 de março de determinado ano enquadra-se no marco de 1º de 
- Ao determinar o limite ordinário de contratação de novas operações por um Município, a equ
- Um financiamento por arrendamento mercantil pode submeter-se às normas de operações de cré
- O Tesouro IPCA+ com juros semestrais e o Tesouro IPCA+ sem cupons podem possuir remuneraçã
- Quando o Senado autoriza operação externa de interesse municipal, ficam dispensados os par
- O limite de saldo devedor de ARO previsto no art. 10 da Resolução do Senado 43/2001 é de 7
- Crédito alimentar devido a pessoa de 62 anos tem superpreferência sem teto de valor, já qu
- Após a EC 136/2025, o art. 100, § 23, fixou limite de pagamento anual de precatórios igual
- Um ente emite dívida com vencimento em dez meses e inclui a receita respectiva no orçament
- Uma ARO liquidada até 10 de dezembro pode ser contratada no último ano do mandato, pois a 
- Pela regra de ouro, operações de crédito que excedam despesas de capital podem enquadrar-s
- Para requisitórios contra Municípios expedidos após 1º de agosto de 2025, a soma da correç
- Um Município que já depositou o valor correspondente ao limite anual ordinário do art. 100
- Valores efetivamente transferidos pelo ente às contas especiais do Judiciário para pagamen
- A majoração decenal de 0,5 ponto percentual dos limites subnacionais de precatórios, previ
```

## Pedido 45: Prevenção e combate a incêndio, brigadas e primeiros socorros (`seguranca-trabalho.prevencao-combate-incendio-primeiros-socorros`, 433 tópicos de edital)

```
Matéria: seguranca-trabalho.prevencao-combate-incendio-primeiros-socorros (Prevenção e combate a incêndio, brigadas e primeiros socorros)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da combustão, comportamento do fogo e segurança estrutural": 20
- "2. Classes de incêndio, métodos de extinção e equipamentos": 18
- "3. Detecção, alarme, rotas de saída e legislação estadual": 21
- "4. Brigadas, planos de emergência e abandono de área": 16
- "5. Primeiros socorros, proteção do socorrista e ação integrada": 25

Já existem (não repita):
- Durante um treinamento, descreve-se combustível, comburente e calor como elementos necessá
- Em um incêndio compartimentado, gases quentes sobem por uma passagem vertical e aquecem o 
- Em uma sala com incêndio crescente, a intensa radiação da camada quente provoca ignição ge
- Um depósito apresenta princípio de incêndio em caixas de papel e peças de madeira, com for
- Há um princípio de incêndio em painel elétrico que continua energizado. A equipe treinada 
- Uma oficina armazena solvente inflamável em recipientes e ocorre pequeno foco no líquido d
- Em laboratório, pequenas aparas de magnésio entram em combustão. A equipe observa que o ag
- Uma fritadeira industrial contém gordura superaquecida em combustão. Qual conduta é compat
- Em vistoria de uma edificação, foram identificadas portas corta-fogo e uma central de dete
- Durante avaliação de chuveiros automáticos convencionais com elementos termossensíveis, al
- Um trabalhador aciona dispositivo instalado na parede ao perceber fumaça. A central recebe
- Uma empresa possui brigada treinada e entende que os demais trabalhadores não precisam rec
- Uma porta sinalizada como saída de emergência está liberada, mas o corredor de acesso perm
- Em expediente, uma saída permanece equipada com mecanismo de travamento que, sem chave, po
- Uma análise normativa de segurança contra incêndio em Minas Gerais apresenta o Decreto nº 
- Em Minas Gerais, um estudo precisa localizar os critérios de formação e composição de brig
- Em um PPCI relativo a edificação do Rio Grande do Sul, uma equipe propõe adotar sem adapta
- O responsável por centro de treinamento pretende indicar quais documentos técnicos tratam,
- Uma edificação tem empregados fixos treinados que atuam apenas extraordinariamente na prev
- Num simulado, constata-se que visitantes desconhecem a rota alternativa e que pessoas com 
- Um incêndio em compartimento fechado torna a porta muito quente e há sinais de gases acumu
- Durante inspeção após incêndio, uma viga de aço aparenta conservar sua forma, e o gestor a
- Uma pessoa sem resposta e sem respiração normal é encontrada em área externa já segura apó
- Após vazamento, um trabalhador encontra pessoa caída e ainda em contato com cabo elétrico 
- Uma pessoa sofre queimadura térmica localizada, com roupa aderida à pele, e encontra-se em
- Durante princípio de incêndio em material sólido com brasas, o resfriamento com agente ade
- Em um galpão, o ar quente produzido pelo incêndio se desloca até o teto; essa transferênci
- A instalação de um detector automático de fumaça permite perceber indícios de incêndio, ma
- Em um painel elétrico que continua energizado, recomenda-se jato comum de água porque a ca
- Em rota de abandono, sinalização indica a saída, mas materiais armazenados impedem a passa
- Durante o expediente, a saída de emergência pode permanecer trancada por chave guardada no
- Uma organização oferece treinamento de combate apenas à brigada, mas também informa todos 
- Uma indústria utiliza extintor de pó ABC em equipamento contendo magnésio em combustão, af
- No sistema convencional de chuveiros automáticos com elemento termossensível individual, a
- Após período com pouco oxigênio em compartimento fechado, uma possível combustão violenta 
- Em cozinha profissional, há princípio de fogo em óleo de fritura; o uso de um extintor ide
- Uma empresa mineira utiliza o Decreto nº 44.746/2008 como fonte normativa vigente porque e
- Ao identificar legislação estadual do Rio Grande do Sul, verifica-se que a Lei Complementa
- Para edificação do Rio Grande do Sul, a aplicação integral de critérios numéricos da IT 12
- Em edifício com grande circulação de visitantes, o plano de emergência define alarmes, rot
- Em indústria com brigada orgânica, os membros da população fixa ficam automaticamente habi
- Em acidente por líquido aquecido, a vítima está em ambiente seguro e apresenta queimadura 
- Uma pessoa está inconsciente e não respira normalmente. Antes de acionar ajuda, o leigo de
- Uma pessoa adulta apresenta engasgo, mas consegue tossir com vigor e falar. A conduta inic
- Diante de pessoa caída e em contato com circuito ainda energizado, o socorrista pode tocá-
```

## Pedido 46: Comércio internacional e economia aberta (`economia.comercio-internacional`, 431 tópicos de edital)

```
Matéria: economia.comercio-internacional (Comércio internacional e economia aberta)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teorias do comércio, especialização e distribuição dos ganhos": 21
- "2. Balanço de pagamentos, fluxos financeiros e reservas": 15
- "3. Câmbio nominal e real, paridades, regimes e crises": 23
- "4. Proteção comercial, GATT/OMC e integração regional": 30
- "5. Comércio exterior aplicado, Incoterms e conexões de economia aberta": 11

Já existem (não repita):
- Duas economias produzem tecido e vinho. A é mais produtiva em ambos, mas sacrifica apenas 
- Uma economia exporta soja a preço internacional crescente em relação ao preço de fertiliza
- Sob as hipóteses clássicas de Heckscher-Ohlin, um país é relativamente abundante em trabal
- Dois países industrializados importam e exportam modelos diferentes de automóveis, cada fa
- Uma companhia residente paga juros de empréstimo obtido de credor não residente. Na classi
- Uma empresa residente contrai nova dívida de 30 unidades monetárias perante investidor não
- Uma economia registra exportação de mercadoria para não residente e uma doação corrente en
- A cotação de moeda doméstica por dólar passou de 4,80 para 5,20, enquanto demais condições
- Num exercício, E é moeda doméstica por estrangeira e os preços externos ficam constantes. 
- Uma taxa de juros doméstica anual é maior que a externa e há contratos futuros cambiais lí
- Uma economia pretende fixar rigorosamente a taxa cambial e permitir livre movimento intern
- Após forte depreciação nominal, a conta comercial piora inicialmente por contratos existen
- Uma economia pequena importa trigo a preço mundial fixo. O governo institui tarifa que ele
- Um país substitui tarifa por cota de importação inicialmente calibrada para produzir o mes
- Um estudo afirma que a OMC surgiu do resultado de negociações de 1986 a 1994, mas que o ac
- Um acordo elimina tarifas entre parceiros, mas preserva tarifas externas individuais. Outr
- Em um país pequeno, o preço mundial de um produto é 100, dos insumos importados necessário
- No modelo Ricardo-Viner, sobe o preço relativo do bem exportável. O trabalho pode mover-se
- Uma economia vê forte aumento de produtividade em bens comerciáveis. Trabalhadores passam 
- No BPM6, uma economia apresenta conta corrente de menos 15 e conta capital igual a zero, s
- No Mundell-Fleming simplificado, com alta mobilidade de capitais, preços rígidos e ausênci
- Uma importação foi contratada sob Incoterm CIF 2020. O vendedor paga frete e seguro até de
- Uma mercadoria precisa de enquadramento na NCM, apuração de valor aduaneiro e definição de
- Após integração regional, um país deixa de produzir internamente um insumo de custo 125 e 
- Uma crise cambial se desenvolve sob taxa fixa após expansão monetária persistente e venda 
- Mesmo quando o país A utiliza menos horas de trabalho que B na produção dos dois bens, B p
- Melhoria dos termos de troca significa queda do preço relativo das exportações frente às i
- Na estrutura BPM6, o pagamento de frete internacional contratado com transportador não res
- Uma cotação de seis reais por dólar em vez de cinco reais por dólar indica apreciação nomi
- Uma tarifa ad valorem de 10% sobre produto importado de valor aduaneiro de 200 unidades im
- A Rodada Doha, iniciada em 2001, instituiu a OMC pela primeira vez, substituindo integralm
- Pelos Incoterms 2020, a condição FOB é adequada ao transporte marítimo ou hidroviário e in
- Em Heckscher-Ohlin, a previsão de exportar bens intensivos em capital decorre necessariame
- Em um modelo de fatores específicos, aumento do preço do bem exportável tende a favorecer 
- Uma companhia residente que paga dividendos a acionista não residente registra a remessa d
- Com E definido em moeda doméstica por estrangeira, uma inflação doméstica superior à exter
- Entre as negociações da Rodada Uruguai, de 1986 a 1994, estavam apenas reduções de tarifas
- A constituição de união aduaneira acrescenta uma tarifa externa comum à liberalização come
- Uma depreciação cambial melhora imediatamente a balança comercial em qualquer economia, in
- Sob o teorema Stolper-Samuelson e suas hipóteses, a elevação do preço relativo do bem inte
- Na conta financeira BPM6, a emissão de passivo externo novo aumenta positivamente o saldo 
- O efeito Balassa-Samuelson pode explicar apreciação real em uma economia cuja produtividad
- Há criação de comércio quando uma união aduaneira desloca importações de produtor externo 
- Pelo Incoterm DPU 2020, a obrigação de entrega do vendedor compreende colocar a mercadoria
- Na história dos sistemas internacionais, a conversibilidade do dólar em ouro foi interromp
```

## Pedido 47: Transparência na Administração Pública (`administracao-publica.transparencia-administracao-publica`, 428 tópicos de edital)

```
Matéria: administracao-publica.transparencia-administracao-publica (Transparência na Administração Pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais e transparência ativa e passiva na LAI": 20
- "2. Pedido de informação, prazos, recursos, sigilo e dados pessoais": 16
- "3. Transparência fiscal, governo digital e qualidade dos dados": 23
- "4. Controle social, cidadania, ouvidorias e participação na gestão pública": 23
- "5. Accountability, governança, integridade e capacidade de resposta na gestão pública": 18

Já existem (não repita):
- Um órgão publica sem solicitação prévia estrutura administrativa, despesas, contratos e re
- Uma pessoa solicita pelo SIC a planilha de projetos que não está publicada no portal, com 
- Uma organização pretende fiscalizar o pagamento público de serviço de limpeza e precisa id
- Ao solicitar dados de obras, um interessado é informado de que precisa explicar o motivo d
- Um grupo de usuários deseja acompanhar a qualidade de determinado serviço, sugerir melhori
- Eleitores acompanham compromissos assumidos por representantes e usam a eleição seguinte p
- Um município com oito mil habitantes invoca a dispensa do art. 8º, § 4º, da LAI para deixa
- Informação solicitada não está imediatamente disponível no SIC. Qual prazo máximo inicial 
- Uma solicitação LAI foi indeferida e a pessoa quer recorrer à autoridade superior. Quais p
- Uma usuária registra reclamação sobre atendimento e recebe protocolo de ouvidoria, não ped
- Ao verificar transparência da receita pública, uma cidadã pergunta se devem ser disponibil
- Na implantação do Siafic, discutem-se informações de execução fiscal em tempo real. Qual p
- Uma equipe quer liberar dados públicos para organizações criarem painéis, sem expor inform
- Uma associação identifica indícios de irregularidade no uso de recursos federais e quer fo
- Órgão de controle estatal revisa contas de outra unidade pública, examina irregularidades 
- Um pedido de acesso alcança documento que contém resultados de gasto público e dado reside
- Um serviço oferece formulários on-line, mas continua exigindo que a pessoa apresente repet
- Moradores participam da definição de problemas, testam solução com equipe pública e avalia
- Um serviço público disponibiliza informações sobre documentos exigidos, etapas, locais, pr
- Uma instituição publica relatórios detalhados, mas não responde a reclamações recorrentes 
- Equipe de governança detecta conflito de interesses potencial e falta de controles de prev
- Um tribunal nega acesso e alguém sustenta que o recurso subsequente deve necessariamente s
- Um requerente recebe indicação do link de relatório público, mas declara que não possui me
- Em discussão orçamentária, moradores sugerem realocação de recursos e gestores afirmam que
- Uma organização utiliza os princípios do art. 3º do Decreto 9.203/2017 para orientar boas 
- Uma unidade publica de ofício sua estrutura e lista de despesas, sem esperar solicitações.
- Na Lei de Acesso à Informação, qualquer pessoa só pode requerer dados públicos se demonstr
- O art. 48-A da LRF, introduzido pela LC nº 131/2009, exige divulgação de informações detal
- O conselho de usuários instituído pela Lei 13.460/2017 possui competência sancionadora ger
- Quando pessoas usam eleições para premiar ou punir politicamente representantes conforme s
- Informação requerida pelo SIC pode ser fornecida apenas após vinte dias, mesmo quando já s
- O prazo geral de resposta final da ouvidoria na Lei nº 13.460/2017 é de trinta dias, prorr
- A existência de dados digitais em portal governamental dispensa observância de normas de p
- Um cidadão, partido político, associação ou sindicato pode, na forma da lei, denunciar irr
- Uma instituição que divulga relatórios financeiros completos cumpre necessariamente todos 
- Quando um documento contém informações públicas e trecho legalmente protegido, o art. 7º, 
- A restrição de informações pessoais relativas à intimidade prevista no art. 31 da LAI depe
- O Decreto nº 10.540/2020 considera divulgação em tempo real da informação fiscal até o pri
- O instituto do orçamento participativo transfere integralmente à assembleia de cidadãos a 
- Ao envolver moradores no planejamento, na experimentação e na avaliação de um serviço, a a
- Embora o art. 16 da LAI estabeleça via recursal à CGU para hipóteses do Executivo federal,
- Conselhos de usuários previstos na Lei nº 13.460/2017 possuem natureza consultiva e podem 
- Como o Decreto nº 9.203/2017 cita transparência e integridade, suas regras internas de gov
- O uso de bases abertas e portais digitais pode ampliar controle social, mas a avaliação de
- Responsividade e accountability são idênticas: se uma instituição responde rapidamente às 
```

## Pedido 48: Cumprimento de sentença (`processo-civil.cumprimento-sentenca`, 428 tópicos de edital)

```
Matéria: processo-civil.cumprimento-sentenca (Cumprimento de sentença)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições gerais, títulos judiciais e liquidação de sentença": 21
- "2. Quantia certa: cumprimento definitivo, provisório e impugnação": 22
- "3. Fazer, não fazer e entregar coisa: tutela específica e multas": 20
- "4. Cumprimento de sentença de alimentos": 17
- "5. Fazenda Pública, impugnação, RPV e precatório": 20

Já existem (não repita):
- Uma sentença reconhece indenização, mas o valor depende apenas de aplicar taxa e período j
- Para quantificar a perda de valor de uma máquina, a sentença determina liquidação por aval
- Uma sentença fixa dever de indenizar, mas a extensão do prejuízo exige provar despesas oco
- Após intimação regular, um devedor particular de R$ 10.000 não paga no prazo do art. 523. 
- Uma sentença ordena entregar um automóvel individualizado, mas o obrigado descumpre o praz
- Um alimentante é intimado pessoalmente, pela técnica coercitiva do art. 528, para pagar pr
- Uma decisão judicial transitada em julgado condena um ente público a pagar quantia. Inicia
- A sentença possui parte líquida de R$ 30.000 e parte ilíquida que exige perícia. O credor 
- Uma sentença de pagar quantia é impugnada por recurso sem efeito suspensivo. O credor pret
- Uma pessoa credora ajuíza cumprimento de quantia contra Município e requer a multa do art.
- No cumprimento definitivo comum, terminou o prazo de quinze dias para pagar sem satisfação
- Em liquidação pelo procedimento comum, é indispensável apurar documentos referentes a fato
- Uma sentença impõe o desfazimento de construção irregular. Embora a parte credora não tenh
- Uma cobrança alimentar inclui dez prestações antigas e as duas mais recentes, imediatament
- A Fazenda Pública impugna apenas uma parcela da conta judicial, sem discutir a outra. Como
- Sentença condenatória penal já transitada em julgado reconhece responsabilidade por dano. 
- O credor pretende levantar depósito no cumprimento provisório de sentença de pagar quantia
- Após trânsito em julgado de decisão exequenda, o Supremo Tribunal Federal profere decisão 
- A Fazenda contesta cálculo em cumprimento de sentença e alega excesso sem informar qualque
- Uma multa diária foi fixada para obrigação de não fazer, mas nova realidade evidencia que 
- Uma pensão alimentar corrente de R$ 1.000 é descontada mensalmente de rendimentos líquidos
- Uma sentença penal condenatória transitada em julgado reconheceu fato danoso, mas não indi
- Uma entidade pública deve R$ 80.000 decorrentes de sentença judicial, valor superior ao li
- O executado particular impugna a execução por excesso, mas não informa quanto considera de
- O Estado é demandado em execução fundada em título extrajudicial, não em decisão do proces
- Uma condenação ilíquida depende unicamente de atualização matemática por índice definido n
- Na liquidação por arbitramento de indenização, o juiz fica impedido de considerar parecere
- Um devedor privado que não paga quantia certa no prazo do art. 523 sujeita o saldo inadimp
- No cumprimento de sentença que impõe pagamento à Fazenda Pública, a ausência de pagamento 
- No cumprimento de ordem judicial de não fazer, o juiz pode determinar medidas executivas a
- Na via coercitiva de cobrança de alimentos, o devedor tem quinze dias após intimação do ad
- Mesmo durante a tramitação de recurso contra a sentença, é possível promover sua liquidaçã
- Durante cumprimento provisório, o executado deposita tempestivamente a quantia apenas para
- Após transcorrerem os quinze dias para pagamento da quantia executada, a apresentação de i
- Quando o credor opta pela cobrança de alimentos pela técnica patrimonial do art. 528, § 8º
- No cumprimento de título judicial contra Fazenda, uma impugnação parcial permite imediato 
- Na liquidação pelo procedimento comum, a parte pode voltar a discutir a própria responsabi
- Se a multa coercitiva de fazer ou não fazer se tornou excessiva quanto a parcelas vincenda
- Se a obrigação reconhecida por sentença está sujeita a condição suspensiva, o credor pode 
- Uma sentença condenatória penal transitada em julgado é considerada título executivo judic
- Uma decisão do Supremo Tribunal Federal declarando inconstitucional a norma de fundamento 
- No cumprimento de alimentos de pessoa assalariada, o desconto parcelado do débito anterior
- Quando a Fazenda é executada por título extrajudicial, seu instrumento de defesa obrigatór
- A decisão que estabelece astreintes pode ser objeto de cumprimento provisório, com depósit
- Em condenação pecuniária contra Fazenda com valor acima do limite legal de pequeno valor, 
```

## Pedido 49: Estruturas, fundações, materiais e acompanhamento de obras (`arquitetura.estruturas-fundacoes-materiais`, 426 tópicos de edital)

```
Matéria: arquitetura.estruturas-fundacoes-materiais (Estruturas, fundações, materiais e acompanhamento de obras)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistemas estruturais, cargas e fundações": 19
- "2. Materiais, execução estrutural e controle tecnológico": 23
- "3. Esquadrias, coberturas e impermeabilização": 19
- "4. Canteiro, segurança e logística de execução": 21
- "5. Apropriação de serviços, custos, controle de obras e fiscalização": 18

Já existem (não repita):
- Em edifício convencional de concreto armado, uma laje descarrega em vigas, estas em pilare
- O projeto de uma pequena edificação prevê uma laje de fundação que distribui as ações de d
- Durante a descarga de concreto fresco, foi realizado ensaio de abatimento do tronco de con
- Em telhado inclinado, o responsável quer proteger o encontro superior das duas águas. Qual
- Uma edificação tem cobertura plana exposta a chuva e movimentação térmica. A equipe precis
- Uma empresa iniciou canteiro de obras com cinco trabalhadores e altura de 4 m. Qual exigên
- Uma equipe consumiu vinte horas-homem e executou quarenta metros quadrados de alvenaria ac
- Uma parede de 4 m de comprimento e 3 m de altura possui abertura de 2 m². O contrato manda
- Um relatório de sondagem informa N-SPT de 18 em uma camada de solo. O projetista pretende 
- Dois apoios de uma mesma edificação apresentam recalques de 4 mm e 19 mm, respectivamente,
- Uma central entregou concreto com abatimento menor que o especificado. Um trabalhador prop
- Em uma laje recém-concretada, os trabalhadores encerraram o adensamento e a chefia conclui
- O telhado tem desnível de 1,2 m entre cumeeira e beiral e projeção horizontal de 4,0 m. Qu
- Uma janela foi entregue com medidas corretas, mas apresentou entrada de água durante chuva
- Em canteiro com altura de 6 m e doze trabalhadores, pretende-se atribuir elaboração do PGR
- Um fornecedor descarregou 8 m³ de concreto e a conferência geométrica mostrou 7,5 m³ efeti
- Um relatório de controle mostra slump conforme a especificação, mas os resultados de resis
- Uma sapata recebe força vertical centrada de 600 kN, distribuída em base retangular de 2 m
- A impermeabilização de um terraço foi executada, mas surgiram infiltrações junto a ralos e
- Em uma fase nova da obra, empresa subcontratada inicia escavação com riscos de soterrament
- Uma viga de 5 m de comprimento, seção retangular de 0,30 m × 0,60 m, será executada sem in
- Uma equipe concluiu 20 m² de revestimento, mas a composição orçamentária estabelece 1,1 kg
- Ao comparar métodos construtivos, o projetista quer avaliar substituição de paredes de ved
- Uma obra possui 14 m² de impermeabilização fisicamente aplicada; 3 m² apresentam arremate 
- Na fiscalização de vigas concretadas, um lote de aço apresenta bitolas distintas das indic
- Uma parede portante em alvenaria estrutural pode participar da transmissão de cargas para 
- O índice N-SPT de sondagem corresponde diretamente à tensão admissível do terreno, sendo s
- O ensaio de abatimento auxilia o controle de consistência do concreto fresco, enquanto sua
- Uma laje foi adensada corretamente durante a concretagem. Esse procedimento substitui a cu
- A NBR 9575 orienta seleção e projeto da impermeabilização e a NBR 9574 orienta execução, i
- A NR-18 dispensa a elaboração do PGR em todos os canteiros com altura de até 7 m e dez tra
- Ao medir revestimento por área líquida, um contrato pode determinar o desconto de vãos; a 
- Duas sapatas suportam forças semelhantes, mas têm recalques desiguais que causam fissuras 
- Para fundação em terreno com lençol freático profundo, é obrigatório adotar estacas, ainda
- Uma concretagem exige que a retirada de escoramentos seja planejada em função da resistênc
- O principal ensaio da família NBR 10821 para esquadrias é a medição do comprimento e da al
- Uma cobertura tem desnível vertical de 0,90 m e projeção horizontal de 3,0 m; sua inclinaç
- Uma empresa contratada introduziu nova atividade com risco de soterramento. A NR-18 permit
- Na apropriação de alvenaria, registram-se insumos e horas realmente consumidos na execução
- Em laje com área de 24 m² e espessura uniforme de 0,15 m, sem vigas nem aberturas incluída
- Uma equipe executa 30 m² de serviço utilizando 15 homem-horas. A razão unitária de produçã
- Uma impermeabilização aplicada em laje exposta pode falhar nos encontros com ralos e tubul
- Na fase de montagem de estrutura metálica, o uso de peças fabricadas fora do canteiro elim
- Um canteiro com 8 m de altura e oito trabalhadores deve manter PGR, e sua elaboração segue
- No controle físico-financeiro, o registro de materiais entregues e pagos ao fornecedor bas
```

## Pedido 50: Repartição de competências e intervenção (`direito-constitucional.competencias-intervencao`, 425 tópicos de edital)

```
Matéria: direito-constitucional.competencias-intervencao (Repartição de competências e intervenção)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Federalismo e categorias de competência": 12
- "2. União: competências materiais e legislação privativa": 16
- "3. Competências comuns, concorrentes e suplementação": 16
- "4. Competências dos Estados, Municípios e Distrito Federal": 15
- "5. Intervenção federal e estadual: hipóteses e procedimento": 41

Já existem (não repita):
- Uma equipe administrativa discute qual ente tem atribuição constitucional para emitir moed
- Uma assembleia estadual aprova projeto que cria tipo penal aplicável somente a fatos ocorr
- Uma unidade federativa deseja legislar sobre questão específica relacionada a matéria do a
- Em uma crise sanitária, gestores de diferentes níveis de governo organizam serviços de ate
- Um Estado quer editar norma sobre ensino e inovação educacional respeitando o espaço const
- Inexiste lei federal de normas gerais sobre determinada matéria incluída no art. 24. O Est
- Um Estado editou regra ambiental em lacuna federal. Anos depois, lei federal de normas ger
- Uma matéria de interesse predominantemente regional não está reservada expressamente a out
- A administração de uma cidade pretende fixar horário de abertura e fechamento de lojas loc
- Uma proposta de lei distrital trata de assunto que, se ocorrido em Estado, seria de compet
- Um relatório distingue a fiscalização estatal de dados pessoais da edição de regras gerais
- Uma assembleia quer regulamentar disciplina regional de urbanismo, compatível com normas f
- Lei municipal detalha procedimentos locais de coleta de resíduos, compatíveis com normas a
- Uma unidade da Federação sofre tentativa de separação territorial. A autoridade federal ex
- Um Estado suspendeu pagamentos de sua dívida fundada por dois anos e seis meses consecutiv
- Um Estado suprime de modo relevante a autonomia política de seus Municípios, em desconform
- A administração de um Município estadual deixa de apresentar contas devidas, apesar de suc
- Um Poder Legislativo estadual encontra-se impedido de exercer suas funções por coação exte
- Uma unidade estadual resiste ao cumprimento de decisão judicial. A situação é tratada como
- Há recusa reiterada de Estado à execução de lei federal, e se pretende obter provimento ju
- Em situação de intervenção federal, o chefe do Executivo edita decreto que identifica ampl
- O STF provê representação sobre princípio constitucional sensível. O decreto interventivo 
- Em um Território Federal há Município que deixa de prestar contas exigidas. Qual ente pode
- Uma disputa administrativa ambiental exige regras de cooperação entre União, Estados, Dist
- Na representação interventiva do PGR, sete ministros comparecem ao julgamento e seis afirm
- Uma autoridade informa que a União pode emitir moeda e que essa atividade está prevista co
- Um Estado decide legislar sobre direito penal sem autorização federal, argumentando que a 
- Em discussão sobre normas gerais de educação, afirma-se que Municípios figuram expressamen
- Em programa de proteção de florestas e combate à poluição, a União, os Estados, o Distrito
- Na ausência de normas gerais federais sobre matéria concorrente, um Estado pode legislar p
- Lei federal posterior de normas gerais ambientais elimina automaticamente todo o texto de 
- Um Estado pretende exercer competência não atribuída expressamente a outro ente nem vedada
- Uma Câmara Municipal sustenta que sua competência para fixar horário de comércio local dec
- O Distrito Federal aprova disciplina legislativa de interesse local e outra sobre matéria 
- Um Estado suspende o pagamento de dívida fundada há apenas um ano. Por essa razão isolada,
- A falta de prestação de contas da administração estadual direta e indireta pode ser examin
- Um Município integrante de Estado descumpre condição do art. 35. A União pode intervir dir
- Fora das hipóteses excepcionais de dispensa, o decreto interventivo deve ser submetido ao 
- Um Poder Judiciário estadual é vítima de coação. A intervenção federal fundada no art. 34,
- Em representação interventiva federal, a sessão de mérito só pode decidir com presença de 
- A Lei nº 12.562/2011 permite proclamar a procedência da representação interventiva federal
- No exercício financeiro de 2025, um Município deixa de aplicar o mínimo constitucional exi
- Decretada intervenção, as autoridades afastadas ficam definitivamente impedidas de reassum
- Em intervenção por violação de princípio sensível, o decreto que afasta autoridades e assu
- Um Município disciplina o horário de funcionamento de estabelecimentos comerciais comuns d
```

