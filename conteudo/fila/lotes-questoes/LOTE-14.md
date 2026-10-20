# Pedidos de questões extras, lote 14 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 6482 tópicos de edital.

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

## Pedido 1: Coloproctologia (`medicina.coloproctologia`, 136 tópicos de edital)

```
Matéria: medicina.coloproctologia (Coloproctologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação coloproctológica e anatomia aplicada": 17
- "Hemorroidas e fissura anal": 17
- "Abscesso, fístula e afecções anorretais": 17
- "Doença diverticular e abdome agudo coloproctológico": 17
- "Doenças inflamatórias intestinais: abordagem cirúrgica": 16
- "Pólipos, colonoscopia e câncer colorretal e anal": 16

Já existem (não repita):
- Na avaliação de dor e sangramento anal, o que caracteriza as hemorroidas internas?
- Uma hemorroida interna sai durante esforço e retorna sem necessidade de redução manual. Qu
- Paciente refere dor em corte na evacuação, prolongada após defecar, e pouco sangue no pape
- Adulto apresenta tumefação flutuante, rubor e dor contínua na margem anal. Qual é o tratam
- Qual alternativa distingue corretamente diverticulose de diverticulite?
- Em suspeita de diverticulite complicada, qual exame costuma demonstrar abscesso, ar extral
- Qual princípio é especialmente importante ao operar estenoses recorrentes na doença de Cro
- Uma biópsia de pólipo revela displasia de alto grau em adenoma colônico. Qual interpretaçã
- No SUS em 2026, qual é a população indicada no protocolo anunciado para rastreamento biena
- Paciente com adenocarcinoma de reto médio confirmado será avaliado quanto à extensão local
- Hemorroidas internas grau II permanecem sintomáticas após orientações dietéticas. Qual pro
- Pessoa com fissura crônica refratária tem antecedente de incontinência de gases. Qual medi
- Qual conduta é mais compatível com fístula anal alta que envolve grande parte do complexo 
- Paciente com antecedente de diverticulite do sigmoide apresenta pneumatúria e infecções ur
- Na retocolite ulcerativa com megacólon tóxico, qual quadro aponta necessidade de prontidão
- Após colonoscopia adequada, dois adenomas tubulares menores que 10 mm e de baixo grau fora
- Pessoa com carcinoma escamoso do canal anal estágio II, sem metástases, busca tratamento c
- Qual conjunto representa conduta possível em câncer de reto localmente avançado, após esta
- Uma pessoa com diverticulite apresenta pneumoperitônio, peritonite difusa e hipotensão. Qu
- A colonoscopia removeu por fragmentos um adenoma de 25 mm, sem comprovação visual segura d
- Se a anuscopia revela hemorroida interna, não há necessidade de investigar neoplasia em pa
- Na classificação de Goligher, hemorroida interna grau III prolapsa e precisa de redução ma
- Antibióticos isolados são sempre suficientes para curar abscesso anorretal com coleção pur
- Diverticulose pode ser encontrada em pessoa sem sintomas e não corresponde obrigatoriament
- Fissura anal lateral, múltipla ou com apresentação atípica deve levar à consideração de ca
- Na ligadura elástica de hemorroida interna, posicionar a banda abaixo da linha pectínea di
- A classificação de Parks considera a relação dos trajetos fistulosos com o complexo esfinc
- A persistência de drenagem purulenta por orifício perianal após abscesso deve levantar hip
- O exame tomográfico pode definir complicações extraluminais da diverticulite, como abscess
- A segunda crise de diverticulite não complicada torna mandatória a sigmoidectomia eletiva 
- Na doença de Crohn fistulizante perianal, drenagem de coleções, preservação esfincteriana 
- Uma colonoscopia com preparo intestinal inadequado garante ausência de pólipos pequenos e 
- Após ressecção do segmento mais doente na doença de Crohn, o paciente está definitivamente
- Megacólon tóxico na retocolite ulcerativa pode demandar cirurgia urgente diante de perfura
- O FIT negativo afasta suficientemente câncer colorretal em paciente sintomático com anemia
- A recomendação de 7 a 10 anos após remoção de 1 a 2 adenomas tubulares menores que 10 mm p
- O carcinoma espinocelular anal localizado geralmente é tratado de início com quimiorradiot
- A ausência de resposta clínica completa poucas semanas após finalizar quimiorradioterapia 
- Em imunocompetente estável com diverticulite aguda leve e não complicada, diretrizes permi
- Fístulas anais altas podem receber fistulotomia ampla sem risco de incontinência, porque o
```

## Pedido 2: Capitalismo, ideologias e regimes políticos contemporâneos (`historia.capitalismo-ideologias-regimes-politicos`, 135 tópicos de edital)

```
Matéria: historia.capitalismo-ideologias-regimes-politicos (Capitalismo, ideologias e regimes políticos contemporâneos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Capitalismo e industrialização: fases, liberalismos e marxismo": 20
- "2. Crises, New Deal, bem-estar social e acumulação flexível": 20
- "3. Revoluções no século XX: Rússia, China, México e Cuba": 19
- "4. Estados nacionais, liberalismos, fascismos e democracias": 20
- "5. Romantismo, cultura imperialista, vanguardas e modernismo": 21

Já existem (não repita):
- Uma fábrica britânica de tecidos de aproximadamente 1790 passou a reunir operários e máqui
- Um texto do século XIX defende concorrência econômica, direitos individuais e limitação co
- Em uma oficina capitalista, uma trabalhadora produz valor superior ao correspondente à sua
- Uma exposição sobre mudanças industriais associa um novo ciclo produtivo ao aço, à química
- Um pesquisador analisa a dominação colonial europeia do fim do século XIX e encontra inter
- Ao comparar uma tecelagem mecanizada de 1790 e uma indústria eletroquímica de 1900, um his
- Uma cidade industrial do século XIX pode ter elevado sua produção e, ao mesmo tempo, teste
- Quando um texto de Marx descreve mais-valia, ele se refere especificamente ao dinheiro arr
- Uma corrente do século XIX podia apoiar direitos individuais e limites constitucionais ao 
- Em 1933, diante de desemprego e falências, um governo lançou obras públicas, proteção banc
- Uma montadora organiza milhares de veículos padronizados em esteiras, repartindo cada oper
- Um país capitalista do pós-1945 ampliou previdência, saúde e educação públicas mantendo em
- Em uma recessão, consumidores reduzem gastos, empresas cortam produção e o desemprego se p
- Após os choques petrolíferos dos anos 1970, uma empresa adota fornecedores especializados 
- Um aluno explica que a queda da bolsa de 1929 foi acompanhada por falências bancárias, ret
- O New Deal, introduzido em 1933, teve como objetivo central abolir a propriedade privada e
- A existência de serviços públicos de saúde, educação e previdência no pós-guerra podia coe
- A crise econômica dos anos 1970 eliminou, no mesmo momento, todas as instituições de bem-e
- Uma linha do tempo registra abdicação de Nicolau II, Governo Provisório e, meses depois, s
- No início da década de 1920, o governo bolchevique autorizou pequeno comércio e iniciativa
- Uma exposição apresenta a vitória comunista chinesa após conflito prolongado, com importan
- Um movimento iniciado em 1910 reuniu demandas contra uma longa ditadura, reivindicações ca
- Um documentário diferencia a derrubada de Batista, a declaração socialista e a crise dos m
- A revolução de fevereiro de 1917 derrubou Nicolau II, mas o governo bolchevique surgiu ape
- A fundação do Partido Comunista Chinês em 1921 coincidiu com a proclamação da República Po
- A Constituição mexicana de 1917 incluiu disposições agrárias e trabalhistas, mas isso não 
- Em Cuba, a crise dos mísseis ocorreu no ano da vitória sobre Batista, e a aliança soviétic
- Um mapa de 1871 mostra a formação do Império Alemão, liderada pela Prússia, depois de guer
- Um pesquisador estuda a unificação italiana e distingue a proclamação do Reino da Itália d
- Um relatório sobre a Alemanha de 1933 mostra perseguição a opositores, supressão de partid
- Um estudo compara as ditaduras de Hitler e Stalin. Para evitar falsa equivalência, qual co
- Um Estado realiza votações periódicas, mas proíbe partidos opositores, censura a imprensa 
- A unificação alemã de 1871 foi conduzida sob liderança da Prússia e de Bismarck, enquanto 
- O partido nazista adotou a palavra 'socialista' em seu nome, razão suficiente para classif
- A referência ao totalitarismo permite comparar mecanismos de propaganda e repressão do naz
- O artigo 21 da Declaração Universal dos Direitos Humanos, de 1948, considera eleições peri
- Um quadro do início do século XX decompõe uma figura em planos simultâneos, rompendo com a
- Um manifesto de 1909 exalta máquinas, velocidade, energia urbana e rejeição provocativa da
- Um objeto cotidiano é exibido como obra para ironizar os critérios tradicionais de arte, e
- Um texto artístico de 1924 pede acesso ao inconsciente por escrita automática e imagens on
- Uma análise da Semana de Arte Moderna de 1922 e do Manifesto Antropófago de 1928 procura e
- A defesa da emoção, da natureza e das tradições populares pôde integrar produções romântic
- Cartazes coloniais que representavam populações dominadas como inferiores demonstram que a
- No início do século XX, dadaístas e surrealistas utilizaram métodos idênticos e possuíam e
- O Manifesto Antropófago de 1928 propôs assimilação crítica e transformação de influências 
```

## Pedido 3: Testes de Software: Testes Unitários e de Integração, JUnit e Mockito (`informatica.testes-unitarios-integracao-junit-mockito`, 135 tópicos de edital)

```
Matéria: informatica.testes-unitarios-integracao-junit-mockito (Testes de Software: Testes Unitários e de Integração, JUnit e Mockito)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Níveis de teste e testes unitários": 34
- "2. JUnit 5": 33
- "3. Dublês de teste e Mockito": 33

Já existem (não repita):
- O teste unitário verifica a menor unidade do código de forma isolada, sem depender de banc
- O nível de teste que verifica se componentes distintos, como um serviço e o banco de dados
- Na pirâmide de testes, recomenda-se ter muitos testes de ponta a ponta e poucos testes uni
- No acrônimo FIRST, que descreve bons testes unitários, a letra I significa que o teste dev
- A estrutura AAA de um teste corresponde às etapas de preparar o cenário, executar a ação e
- No TDD, o ciclo de desenvolvimento segue a sequência:
- Uma cobertura de código de 100% garante que o software não possui defeitos.
- O teste de aceitação tem como objetivo principal:
- No JUnit 5, a anotação que faz um método executar antes de cada teste da classe é:
- No JUnit 5, os métodos anotados com @BeforeAll e @AfterAll devem, em regra, ser estáticos,
- No método assertEquals do JUnit, o primeiro parâmetro deve ser o valor obtido pelo código 
- Para verificar que um método lança IllegalArgumentException ao receber valor inválido, uti
- O assertAll do JUnit 5 agrupa várias asserções e relata todas as falhas encontradas, em ve
- Para executar o mesmo teste com diferentes conjuntos de dados, o JUnit 5 oferece:
- No JUnit 4, a anotação equivalente ao @BeforeEach do JUnit 5 era @Before, e a equivalente 
- Por padrão, quantas instâncias da classe de teste o JUnit 5 cria?
- Ferramentas de build como o Maven executam automaticamente os testes JUnit durante o ciclo
- A anotação @Disabled, no JUnit 5, serve para:
- O dublê de teste que apenas devolve respostas prontas a chamadas, para controlar o cenário
- O mock é o dublê de teste usado para verificar interações, como se um método foi chamado e
- Um banco de dados em memória usado no lugar do banco real durante os testes é exemplo de d
- No Mockito, para fazer o método buscar(1) de um mock retornar um objeto específico, utiliz
- A instrução verify(repositorio, never()).salvar(any()) verifica que o método salvar nunca 
- A anotação do Mockito que cria a instância da classe testada e injeta nela os mocks declar
- No Mockito, quando um argumento de uma chamada usa matcher, como any(), os demais argument
- Para capturar o objeto passado a um método do mock e fazer asserções sobre seus campos, o 
- Um método de mock do Mockito que não foi configurado com when lança exceção ao ser chamado
- O spy do Mockito se diferencia do mock porque:
- No teste unitário de um serviço que usa um repositório, a prática recomendada é mockar o r
- Para fazer um método de mock lançar uma exceção quando chamado, utiliza-se no Mockito:
- No JUnit 5, a integração com o Mockito costuma ser feita com @ExtendWith(MockitoExtension.
- O dublê de teste passado apenas para preencher um parâmetro obrigatório, sem ser usado pel
- Testes de integração costumam ser mais lentos que testes unitários, pois envolvem componen
- Um teste unitário que só passa se outro teste for executado antes dele viola principalment
- A asserção assertSame verifica se dois objetos têm o mesmo conteúdo, ainda que sejam instâ
- No JUnit 5, a anotação que define um nome legível para o teste nos relatórios é:
- No teste de integração de um repositório JPA, uma opção comum é usar um banco de dados em 
- Um desenvolvedor quer garantir que, ao cadastrar um usuário com e-mail inválido, o serviço
- No Mockito, a instrução usada para conferir se o método enviar foi chamado exatamente duas
- Testes unitários devem ser repetíveis, produzindo o mesmo resultado em qualquer ambiente e
```

## Pedido 4: Proteção ao trabalho da mulher, gestante e licença-maternidade (`direito-trabalho.trabalho-mulher-gestante`, 135 tópicos de edital)

```
Matéria: direito-trabalho.trabalho-mulher-gestante (Proteção ao trabalho da mulher, gestante e licença-maternidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Igualdade, proteção ao trabalho da mulher e discriminação": 16
- "2. Estabilidade gestacional: marco temporal, vínculos e indenização": 27
- "3. Licença-maternidade, adoção e internação hospitalar": 23
- "4. Programa Empresa Cidadã e prorrogação da licença": 21
- "5. Amamentação, insalubridade e proteção da saúde materno-infantil": 13

Já existem (não repita):
- Durante uma seleção, a empresa solicita às candidatas um atestado comprovando ausência de 
- Uma trabalhadora adulta é contratada para turno noturno, fora de situação de gravidez ou l
- Uma empregada gestante, sem situação especial de internação, consulta a duração ordinária 
- Após o parto, a empregada deseja saber até quando se prolonga a proteção contra dispensa i
- Empregada com filho de quatro meses retorna à jornada e solicita os descansos especiais pa
- A empregada de pessoa jurídica aderente ao Programa Empresa Cidadã requer tempestivamente 
- Uma gestante necessita realizar acompanhamento pré-natal durante o horário de serviço. Qua
- Um estabelecimento possui 30 empregadas maiores de 16 anos. Qual obrigação específica da C
- Uma trabalhadora sofre aborto não criminoso comprovado mediante atestado médico oficial e 
- Empregada é dispensada sem justa causa e, semanas depois, comprova que a gravidez já exist
- A concepção ocorreu comprovadamente durante aviso-prévio indenizado, depois da comunicação
- Uma gestante ocupa vínculo temporário administrativo por prazo determinado, e a instituiçã
- Uma empregada obtém guarda judicial para adotar adolescente e a empresa pretende aplicar a
- Uma mulher é despedida em ato discriminatório comprovado ligado à gravidez. Nos termos do 
- Uma pessoa jurídica aderente ao Empresa Cidadã e uma empregada acordam substituir a extens
- Após parto com complicações, mãe e recém-nascido permanecem internados por mais de duas se
- Uma lactante exerce função em local com insalubridade de grau mínimo. A empresa exige laud
- Uma empregada em gestação de risco recebe recomendação médica de prolongar repouso antes e
- Um empregador já punido pela Lei nº 9.029/1995 reincide em prática discriminatória. Se seu
- Gestante afastada de setor insalubre não encontra posto salubre disponível na empresa. Con
- Uma gestante foi dispensada sem justa causa, mas a decisão judicial ocorreu depois de term
- Duas pessoas empregadas realizam adoção conjunta de criança e requerem simultaneamente a m
- Em outubro de 2026, um material afirma que todas as alterações da Lei nº 15.371, de 31 de 
- Dois empregados de pessoas jurídicas aderentes ao Programa Empresa Cidadã decidem conjunta
- Uma criança fica hospitalizada por apenas cinco dias após o parto, sem complicação prolong
- A vedação de exigência de teste de gravidez para admissão consta do art. 373-A, IV, da CLT
- A CLT veda de maneira geral que mulheres maiores de dezoito anos trabalhem no período notu
- A garantia de emprego da gestante, prevista no art. 10, II, b, do ADCT, estende-se desde a
- A partir da Constituição de 1988, toda empregada passou a ter licença-maternidade de 180 d
- O art. 396 da CLT assegura dois descansos especiais de meia hora na jornada para amamentar
- A ADI 5938 confirmou que toda gestante exposta a insalubridade de grau médio ou mínimo pre
- Após a Lei nº 13.509/2017, a licença-maternidade prevista no art. 392-A da CLT alcança a e
- A mulher que sofre aborto não criminoso comprovado por atestado médico oficial tem direito
- A empregada gestante tem direito a dispensa de horário pelo tempo necessário para realizar
- Na equiparação salarial por trabalho de igual valor, o art. 461, § 1º, da CLT exige que os
- Em situação de internação hospitalar superior a duas semanas e vinculada ao parto, o art. 
- O desconhecimento da gravidez pela empresa afasta a indenização estabilitária se a emprega
- A Súmula 244, II, do TST distingue reintegração dentro do período de estabilidade da inden
- As mudanças promovidas pela Lei nº 15.371, de 31 de março de 2026, podem ser aplicadas a l
- A Lei nº 11.770/2008, art. 1º-A, permite que empresa aderente substitua os 60 dias de exte
- Após o julgamento da ADI 5938, a gestante que trabalha em insalubridade média deve permane
- A multa administrativa por infrações à Lei nº 9.029/1995, art. 3º, I, usa como base dez ve
- O art. 392-A, § 5º, da CLT determina que, havendo adoção conjunta por duas pessoas emprega
- A CLT, art. 390, estabelece limites de 20 kg para trabalho contínuo e 25 kg para trabalho 
- O art. 392, § 7º, da CLT concede extensão de até 120 dias após alta hospitalar mesmo em in
```

## Pedido 5: Gestão de laboratório, biossegurança e controle de qualidade (`laboratorio.gestao-biosseguranca-qualidade`, 135 tópicos de edital)

```
Matéria: laboratorio.gestao-biosseguranca-qualidade (Gestão de laboratório, biossegurança e controle de qualidade)
Capítulos e quantas questões quero em cada um (total 100):
- "Biossegurança, avaliação de riscos e níveis de contenção": 17
- "Proteção individual, coletiva e cabines de segurança biológica": 17
- "Resíduos de serviços de saúde e preparo das bancadas": 17
- "Limpeza, desinfecção, esterilização e processamento de materiais": 17
- "Controle de qualidade interno e externo: gráficos e regras": 16
- "Normas, acreditação, equipamentos, insumos e melhoria contínua": 16

Já existem (não repita):
- Durante o planejamento de aula prática, o responsável deseja definir biossegurança de modo
- Um laboratório recebe amostras humanas e opera com procedimentos que podem produzir aeross
- Uma equipe planeja cultivar em alta concentração um agente com potencial de transmissão po
- O supervisor foi indagado sobre a Lei nº 11.105/2005 e sua relação com a rotina de exames 
- Um laboratório seleciona barreira que precisa proteger o operador, o ambiente e também a a
- Uma funcionária acredita que basta usar luvas e que a higienização das mãos se tornou facu
- Uma área manipula material infeccioso em cabine de fluxo laminar horizontal destinada à pr
- Ao preparar uma cabine biológica classe II antes do processamento de amostras, qual condut
- Um técnico pergunta se poderá utilizar uma cabine classe II comum como solução universal p
- Na rotina de um laboratório clínico, onde deve começar a segregação correta dos resíduos d
- Qual correspondência apresenta corretamente os grupos de resíduos de serviços de saúde?
- Um laboratório terceiriza coleta e tratamento de RSS e afirma que, por isso, não precisa m
- Após o uso de uma agulha em coleta de sangue, a medida apropriada é:
- Uma peça reutilizável será submetida à desinfecção de alto nível. Qual ação deve preceder 
- Qual associação corresponde à classificação de risco de produtos para saúde reutilizáveis?
- Em auditoria, um lote saiu de autoclave com tempo e temperatura indicados, mas havia embal
- Ao organizar vidraria para procedimentos que requerem volume precisamente definido, qual p
- Qual descrição de um gráfico de Levey-Jennings está correta?
- Em sistema multirregras clássico de Westgard, um único controle atingiu +2,2 DP sem aciona
- Dois controles sucessivos apresentam +2,3 DP e +2,4 DP e satisfazem as condições de aplica
- Qual cenário descreve corretamente a regra R_4s clássica?
- Qual norma técnica é a referência de acreditação de laboratórios clínicos no esquema manti
- Em outubro de 2026, qual é o marco sanitário central para serviços que executam exames de 
- Um refrigerador apresentou temperatura fora da faixa especificada enquanto armazenava reag
- Ao conferir o estoque, um técnico encontra dois lotes íntegros com validade diferente, amb
- Durante o preparo de uma bancada didática, há material limpo e tubos já usados no mesmo es
- A avaliação de biossegurança considera não apenas a identidade do agente biológico, mas ta
- Todo laboratório clínico que recebe sangue humano deve obrigatoriamente funcionar em nível
- Grupo de risco biológico e nível de biossegurança são conceitos idênticos: conhecer um det
- A Lei nº 11.105/2005 abrange atividades com organismos geneticamente modificados e seus de
- A cabine de segurança biológica classe I oferece proteção ao operador e ao ambiente, sem a
- A presença de filtro HEPA em uma cabine de segurança biológica garante, por si só, remoção
- O uso de chama aberta dentro de uma cabine de segurança biológica deve ser tratado como ro
- O simples uso de dois pares de luvas dispensa tanto higienização das mãos quanto medidas c
- No gerenciamento de RSS, agulhas e outros perfurocortantes devem ser acondicionados em rec
- A terceirização da coleta e do tratamento de resíduos transfere integralmente ao contratad
- Os resíduos do grupo A podem exigir tratamentos distintos conforme seu subgrupo e suas car
- Em uma bancada de aula prática, a existência de desinfetante elimina a necessidade de defi
- A limpeza remove sujidades e prepara materiais reutilizáveis para processamento, mas não c
- Para produtos de saúde críticos e processáveis, a esterilização adequada é exigida após a 
- Uma fita indicadora química que mudou de cor demonstra sozinha que toda a carga da autocla
- O controle interno da qualidade acompanha a estabilidade do desempenho analítico da rotina
- No esquema multirregras clássico de Westgard, a regra 1_2s funciona normalmente como alert
- Em sua aplicação clássica, a regra R_4s pode ser reconhecida somando dois resultados extre
- A licença sanitária e a acreditação de laboratório clínico são conceitos equivalentes; uma
- Após detectar equipamento fora das especificações, basta corrigir a falha presente, sem av
```

## Pedido 6: Estratégia competitiva, recursos e alianças (`administracao-geral.estrategia-competitiva`, 134 tópicos de edital)

```
Matéria: administracao-geral.estrategia-competitiva (Estratégia competitiva, recursos e alianças)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da estratégia, competição e posicionamento": 15
- "2. Estrutura setorial, cinco forças e padrões de concorrência": 19
- "3. Estratégias genéricas, cadeia de valor e vantagem competitiva": 20
- "4. Recursos, capacidades e estratégias emergentes": 20
- "5. Estrutura, redes produtivas e alianças estratégicas": 26

Já existem (não repita):
- Uma fabricante já decidiu manter somente o negócio de equipamentos domésticos. Agora defin
- Duas empresas do mesmo setor adotam o mesmo processo de entrega. Uma delas elimina retraba
- Ao avaliar a rentabilidade de um mercado de entregas, a equipe examina rivais existentes, 
- Uma empresa de viagens corporativas percebe que muitas reuniões presenciais são substituíd
- Uma rede varejista responde por 65% das compras de vários pequenos fabricantes de alimento
- Uma organização desenvolveu logística padronizada e produção em grande escala, alcançando 
- Um ateliê trabalha somente com instrumentos adaptados para músicos com limitações motoras,
- Em uma empresa industrial, o recebimento, a conferência e a estocagem de matéria-prima são
- Uma empresa possui patente potencialmente valiosa e rara, mas não tem equipe, orçamento ne
- Em um setor de processamento, a entrada exige instalações dispendiosas, reputação consolid
- Em um mesmo setor, empresas econômicas competem por volume e padronização, enquanto empres
- Uma rede de serviços decide ampliar a confiança na marca. Para isso, redesenha a seleção d
- Uma empresa anuncia atendimento personalizado, mas decide simultaneamente abolir treinamen
- Uma plataforma havia planejado atender empresas de grande porte. Durante dois anos, equipe
- Uma firma domina técnica de produção que gera benefício relevante e poucos concorrentes po
- Uma empresa antes concentrada em um produto passa a atuar em três linhas com tecnologias e
- Duas empresas independentes celebram contrato plurianual de pesquisa, compartilham resulta
- Uma organização possui conhecimento técnico raro e difícil de imitar, mas sua proposta de 
- Um negócio deseja operar com custos baixos graças a poucas variantes, entregas programadas
- Um fabricante depende de poucos produtores de um componente singular; ao mesmo tempo, seus
- Uma organização pretendia internacionalizar a produção, mas a iniciativa foi cancelada. Em
- Uma fabricante mantém internamente pesquisa, produção e assistência. Após expansão, passa 
- Um recurso é valioso, raro e difícil de imitar graças ao aprendizado acumulado de equipes 
- Duas empresas planejam pesquisar juntas tecnologia sensível. Uma detém patentes e a outra 
- Duas empresas rivais desenvolvem em conjunto um padrão técnico de segurança, mas mantêm in
- Uma rede varejista já definiu os ramos em que atuará. Quando escolhe competir por conveniê
- Uma empresa que reduziu desperdícios por meio de boas práticas adotadas por todo o setor c
- Em um setor cujo ingresso exige capital elevado, canais de distribuição restritos e reputa
- Uma plataforma de reuniões virtuais que reduz a necessidade de viagens deve ser classifica
- Uma fabricante pode adotar liderança em custos mesmo praticando preços equivalentes aos de
- Uma organização possui tecnologia rara e patenteada. No teste VRIO, a proteção legal do re
- Uma empresa tem recurso raro e de difícil imitação que não resolve necessidades atuais de 
- Uma empresa planejava vender apenas para estabelecimentos grandes, mas consolidou, por aju
- Se a direção documentou determinada estratégia antes da execução, sua realização é conside
- Em uma cadeia de valor, treinamento de equipes pode integrar as atividades de apoio, enqua
- Empresas que pertencem ao mesmo grupo estratégico compartilham obrigatoriamente controle s
- Uma cadeia comercial compra grande volume de pequenos fornecedores de produtos padronizado
- Uma fabricante diversificou o portfólio e enfrenta conflitos entre linhas de negócio. Pela
- Em um setor com componentes altamente especializados e poucos fornecedores, a ameaça de in
- Uma empresa possui equipamentos semelhantes aos de seus concorrentes, mas desenvolveu uma 
- Uma empresa anunciou preços inferiores aos do setor, mas seus custos unitários são mais el
- Uma inovação no desenho das operações pode permitir simultaneamente custos relativos menor
- Uma colaboração duradoura entre duas empresas independentes para desenvolver tecnologia so
- A análise das cinco forças estabelece uma rentabilidade fixa e idêntica para todas as empr
- Uma empresa experimenta formas de entrega não previstas no plano original e, após repetida
```

## Pedido 7: Grupo econômico, sucessão de empregadores e responsabilidade (`direito-trabalho.grupo-economico-sucessao`, 134 tópicos de edital)

```
Matéria: direito-trabalho.grupo-economico-sucessao (Grupo econômico, sucessão de empregadores e responsabilidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Grupo econômico: conceito e solidariedade": 33
- "2. Sucessão de empregadores e fraude": 29
- "3. Sócio retirante e espécies de responsabilidade": 12
- "4. Terceirização lícita, contratos e responsabilidade do tomador": 13
- "5. Súmula 331, decisões constitucionais e casos integrados": 13

Já existem (não repita):
- Duas empresas têm o mesmo sócio, mas nenhum elemento de atuação empresarial conjunta foi c
- Duas sociedades autônomas coordenam operações, compartilham objetivos e atuam conjuntament
- Uma empresa integrante de grupo reconhecido deixa de pagar verbas de contrato de trabalho.
- Uma fábrica é adquirida com operação em funcionamento e obrigações salariais anteriores à 
- Em transmissão de estabelecimento, descobre-se fraude comprovada para frustrar créditos tr
- A empresa alterou razão social e controle, mas manteve empregados e atividade. Segundo os 
- Sócio retirante foi regularmente averbado e há dívida do período em que participou da soci
- A modificação contratual que registra retirada de sócio foi averbada em 12 de abril de 202
- A empresa prestadora não pagou verbas relativas a empregados alocados na contratante priva
- Uma tomadora terceiriza atividade essencial de sua produção com prestadora genuína. O que 
- Uma prestadora terceirizada atende em instalações da tomadora. Quem deve assegurar higiene
- A contratante de serviços dirige terceirizados para atividade diferente da especificada no
- Uma trabalhadora da tomadora ganha mais que terceirizada em atividade semelhante. Na Lei 6
- Sócia retirou-se regularmente, mas a empresa teve nova dívida apenas após sua saída. Pela 
- Para executar responsabilidade do sócio retirante regular, qual preferência deve ser respe
- Foi demonstrada fraude na alteração societária para retirar um sócio e frustrar direitos d
- Uma contratante deseja usar seu ex-empregado demitido como empregado da nova prestadora ap
- O titular de uma prestadora trabalhou como empregado da contratante 12 meses antes. Não é 
- A Súmula 331, VI, lida conforme os precedentes constitucionais, abrange qual extensão da s
- Uma demanda inclui tomadora privada apenas após encerramento do processo, embora não tenha
- Uma empresa pública contrata prestadora que inadimple salários. Qual leitura é compatível 
- Na terceirização pública, a condenação do tomador foi fundada somente em presunção de culp
- Empresa A vende somente equipamento sem transferir atividade, clientela ou estabelecimento
- Sócio saiu por alteração registrada fraudulentamente para ocultar patrimônio, sendo ajuiza
- Uma prestadora genuína executa atividade principal da contratante e não paga rescisões. Qu
- As empresas integrantes de grupo econômico efetivamente caracterizado respondem solidariam
- A mera identidade de sócios entre duas empresas basta para configurar grupo econômico trab
- Alterações na propriedade empresarial não prejudicam, por si, os contratos e direitos adqu
- Na sucessão empresarial, a sucessora assume somente obrigações criadas depois da transferê
- Na responsabilidade do sócio retirante, a ordem prevista no art. 10-A da CLT é empresa dev
- A terceirização de atividade-fim permanece, por esse único motivo, ilícita em qualquer emp
- O art. 2º, § 2º, da CLT admite grupo econômico composto por empresas que preservem sua aut
- A responsabilidade solidária da empresa sucedida por débitos trabalhistas resulta automati
- A limitação de dois anos no art. 10-A da CLT considera a data de averbação da modificação 
- O sócio retirante regular responde pelas obrigações trabalhistas de toda a vida futura da 
- O art. 5º-A, § 5º, da Lei nº 6.019/1974 fixa responsabilidade subsidiária da contratante p
- O art. 4º-C, § 1º, determina pagamento compulsório de salário idêntico a todos os terceiri
- Pelo art. 5º-D da Lei nº 6.019/1974, empregado demitido deve observar dezoito meses para r
- O art. 5º-C da Lei nº 6.019/1974 impede a contratação de prestadora cujos sócios tenham tr
- O simples fato de uma empresa figurar como tomadora de serviços terceirizados não demonstr
- A Súmula 331, VI, limita a responsabilidade subsidiária da tomadora exclusivamente aos sal
- A alteração societária fraudulenta destinada a retirar formalmente um sócio pode atrair su
- O Tema 246 do STF estabelece que basta a inadimplência da prestadora para o ente público a
- A responsabilidade subsidiária da tomadora privada, prevista no art. 5º-A, § 5º, pode subs
- Na sucessão empresarial, apenas a comprovação de fraude permite exigir da sucessora obriga
```

## Pedido 8: Preservação, acondicionamento e microfilmagem de documentos (`arquivologia.preservacao-microfilmagem`, 134 tópicos de edital)

```
Matéria: arquivologia.preservacao-microfilmagem (Preservação, acondicionamento e microfilmagem de documentos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Preservação, conservação e restauração: conceitos e diagnóstico": 16
- "2. Ambiente de guarda, prevenção de riscos e segurança": 15
- "3. Acondicionamento, armazenamento, manuseio e movimentação": 15
- "4. Microfilmagem de arquivo: processo, qualidade e controle": 27
- "5. Efeitos jurídicos, eliminação e preservação dos originais": 27

Já existem (não repita):
- Uma coleção de documentos em bom estado está sendo protegida com controle de umidade, limp
- Em um depósito, uma equipe troca envelopes inadequados por pastas e caixas de qualidade ar
- Um arquivo público deve definir se documentos são correntes, intermediários ou permanentes
- Documentos de uso pouco frequente saem do arquivo corrente e ingressam na fase intermediár
- Um centro pretende reproduzir documentos arquivados em filme reduzido, com observância da 
- Após microfilmagem regular, a equipe precisa separar duas bobinas para reduzir perdas simu
- Um rolo de microfilme está sendo produzido em equipamento que garante reprodução fiel. Seg
- Uma instituição possui documento público classificado como de guarda permanente e já o mic
- Um arquivo de grande porte tem plantas e mapas permanentes em folhas extensas, que não cab
- Um laboratório mantém os documentos em temperatura média de 21 °C, mas registra oscilações
- Uma microfilmadora precisa capturar um mapa maior que seu campo fotográfico e dividir a im
- Ao revisar o rolo, percebeu-se que uma página da mesma série foi omitida. Qual procediment
- Um rolo está pronto e é necessário registrar os elementos que identificam a série antes de
- Uma repartição microfilmou processos administrativos ainda em análise e quer destruir os o
- Uma equipe planeja eliminar documentos públicos temporários após microfilmagem, sem consul
- Um depósito foi construído em subsolo com histórico de alagamentos, possui tubulações sobr
- Uma pessoa obteve cópia impressa extraída de microfilme e pretende apresentá-la para produ
- Na revisão da microfilmagem, imagens complementares não alcançaram o padrão de qualidade n
- Uma instituição está montando sinaléticas padronizadas antes e depois dos documentos micro
- Um processo de eliminação de documentos públicos não abrangidos pela tabela vigente foi pr
- Filmes micrográficos foram produzidos a partir de documentos necessários à prestação de co
- Uma apostila antiga apresenta como vigente a obrigação do parágrafo único do art. 15 do De
- Foi encontrada possível contaminação por fungos em cartas com tintas sensíveis à água, e a
- Uma instituição pretende padronizar o depósito de papel e filmes sob condições idênticas e
- Um lote de documentos está prestes a ingressar em instituição pública para guarda permanen
- Em uma sala de documentos estáveis, substituir grampos oxidáveis e adotar caixas adequadas
- Uma biblioteca de documentos arquivísticos pode dispensar controle ambiental se as folhas 
- A Lei nº 8.159/1991, art. 8º, § 3º, caracteriza documentos públicos permanentes como conju
- Um scanner converteu contratos em arquivos PDF; por isso, mesmo sem reprodução em filme, a
- A microfilmagem regulamentada pelo Decreto nº 1.799/1996 exige, no art. 5º, cópia do filme
- Se os originais de documentos permanentes forem reproduzidos por microfilmagem de boa qual
- A Resolução CONARQ nº 2/1995, art. 1º, § 2º, define recolhimento como entrada de documento
- Um depósito com 20 °C em média, mas com grandes oscilações diárias da umidade relativa, po
- Uma instituição registra que os documentos transferidos ao depósito intermediário devem es
- O art. 6º do Decreto nº 1.799/1996 fixa um único grau de redução para todo documento de ar
- Uma série microfilmada deve possuir imagem de abertura que a identifique e imagem de encer
- Uma instituição microfilmou documentos públicos temporários e pretende eliminá-los sem tab
- O dano por luz depende do tempo de exposição: em situação comparável, manter material por 
- Uma microfilmagem apresentou documento ausente e a equipe pretende corrigir a matriz corta
- O Decreto nº 1.799/1996 permite microfilmar documento ainda em tramitação a critério da au
- Para documentos públicos não contemplados na tabela de temporalidade aprovada, o gestor po
- Um conjunto de fotogramas de documento maior que o campo de captura deve possuir sobreposi
- O parágrafo único do art. 15 do Decreto nº 1.799/1996, relativo ao registro obrigatório de
- A Lei nº 8.159/1991, art. 10, considera os documentos públicos de valor permanente inalien
- Os microfilmes que reproduzem documentos necessários à prestação de contas podem ser desca
```

## Pedido 9: Jurisprudência dos Tribunais Superiores em Direito Administrativo (`direito-administrativo.jurisprudencia-tribunais-superiores`, 134 tópicos de edital)

```
Matéria: direito-administrativo.jurisprudencia-tribunais-superiores (Jurisprudência dos Tribunais Superiores em Direito Administrativo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios, atos administrativos, autotutela e controle externo": 20
- "2. Agentes públicos: concurso, regime funcional e processo disciplinar": 20
- "3. Responsabilidade civil do Estado e improbidade administrativa": 20
- "4. Licitações, contratos, serviços públicos, bens públicos e intervenção na propriedade": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- Um Prefeito nomeou seu irmão, médico com vasta experiência em gestão hospitalar, para o ca
- Um servidor público federal teve sua aposentadoria concedida pelo órgão de origem em fever
- Sobre o julgamento das contas de Prefeito Municipal que atua diretamente como ordenador de
- Uma autarquia estadual passou a divulgar em seu portal da transparência na internet uma ta
- Acerca do exercício da autotutela administrativa (Súmulas 346 e 473 do STF) e da prescriti
- Nos termos da Súmula Vinculante 13 do STF, a nomeação de primo da autoridade nomeante para
- Pela Súmula 473 do STF, a Administração pode anular seus próprios atos, quando eivados de 
- Segundo o Tema 445 de Repercussão Geral do STF, os Tribunais de Contas estão sujeitos ao p
- Nos processos perante o Tribunal de Contas da União, a Súmula Vinculante 3 exige a observâ
- Um estudante do último semestre de Engenharia Civil foi aprovado em concurso público para 
- A respeito do Processo Administrativo Disciplinar (PAD), assinale a alternativa que reflet
- Uma professora acumula licitamente dois cargos efetivos de magistério na rede pública esta
- Sobre o direito subjetivo à nomeação em concurso público (Temas 161 e 784 do STF) e as gar
- Policiais civis de determinado Estado paralisaram suas atividades em greve por reajuste sa
- De acordo com a Súmula Vinculante 43 do STF, é inconstitucional toda modalidade de provime
- Segundo a jurisprudência sumulada do STJ, recebida uma denúncia anônima noticiando infraçã
- Conforme a Súmula 650 do STJ, mesmo quando caracterizadas as hipóteses legais de demissão 
- Pelo Tema 973 de Repercussão Geral do STF, é constitucional a remarcação do teste de aptid
- Durante o serviço, um motorista de autarquia federal avançou o sinal vermelho e colidiu co
- Um ônibus pertencente a uma empresa privada concessionária de transporte público municipal
- Acerca da responsabilidade civil do Estado em relação a pessoas sob custódia no sistema pr
- Em relação às alterações promovidas pela Lei nº 14.230/2021 na Lei de Improbidade Administ
- Compare os seguintes cenários de cobrança de dano ao erário: (I) ação de ressarcimento por
- Segundo o Tema 897 de Repercussão Geral do STF, são imprescritíveis as ações de ressarcime
- Nos termos do Tema 940 do STF, a ação por danos causados por agente público deve ser ajuiz
- Conforme decidido pelo STF no Tema 1199 de Repercussão Geral, a revogação da modalidade cu
- De acordo com o Tema 1199 do STF, o novo regime de prescrição intercorrente previsto na Le
- Um particular ocupou sem permissão um terreno público dominical do Estado por 12 anos, ali
- Uma empresa contratada mediante licitação para prestar serviços terceirizados de limpeza a
- Em matéria de prestação de serviços públicos essenciais (como fornecimento de energia elét
- O Estado invadiu, sem decreto expropriatório e sem prévia indenização, parte da fazenda de
- Sobre a abrangência das sanções administrativas de impedimento de licitar e contratar e de
- De acordo com a Súmula 619 do STJ, a ocupação indevida de bem público configura mera deten
- Nos termos da Súmula 340 do STF, os bens públicos dominicais, por não possuírem destinação
- Pelo Tema 246 de Repercussão Geral do STF, o simples inadimplemento dos encargos trabalhis
- Segundo o Tema 1019 dos Recursos Repetitivos do STJ, o prazo prescricional aplicável à des
- Em provas objetivas que confrontam teses dos Tribunais Superiores, é comum misturar as reg
- Uma questão com caso longo narra que: (1) um servidor acumula licitamente um cargo de médi
- Em provas que exigem a distinção entre a regra geral e a exceção na jurisprudência do STF,
- Analise o caso hipotético: em 2018, um ex-gestor foi processado por ato culposo de improbi
- Assinale a alternativa em que todas as proposições estão integralmente alinhadas às súmula
- Em provas de Certo/Errado, é incorreto afirmar que a presença de advogado é obrigatória em
- Segundo a jurisprudência consolidada do STF (Tema 784), o candidato aprovado fora do númer
- Confrontando os Temas 897 e 899 de Repercussão Geral do STF, conclui-se que a pretensão de
- Pela Súmula Vinculante 3 e pelo Tema 445 do STF, a revisão ou anulação posterior de aposen
```

## Pedido 10: Elementos de máquinas e transmissões (`engenharia-mecanica.elementos-maquinas-transmissoes`, 133 tópicos de edital)

```
Matéria: engenharia-mecanica.elementos-maquinas-transmissoes (Elementos de máquinas e transmissões)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Projeto mecânico, dimensionamento, resistência e confiabilidade": 20
- "2. Engrenagens e transmissões por correias, correntes e cabos": 21
- "3. Eixos, mancais, uniões e molas mecânicas": 22
- "4. Embreagens, freios, elevadores e equipamentos de transporte": 20
- "5. Hidráulica, pneumática, circuitos e diagnóstico integrado": 17

Já existem (não repita):
- Uma haste tracionada apresenta tensão equivalente de 100 MPa e limite de escoamento de 300
- Uma árvore transmite torque constante de 300 N·m a velocidade angular de 20 rad/s. Qual é 
- Uma árvore de acionamento rompe após milhões de ciclos de flexão, embora a máxima tensão n
- Uma barra reta recebe tração centrada de 20 kN e tem área resistente uniforme de 200 mm². 
- Dois componentes independentes ligados funcionalmente em série devem funcionar simultaneam
- Um pinhão externo de 20 dentes gira a 900 rpm e aciona uma roda externa de 60 dentes. Admi
- Uma transmissão por corrente de rolos opera com pequeno número de dentes no pinhão. Observ
- Em redutor com engrenagens cilíndricas de dentes retos e eixos paralelos, o projeto consid
- Uma esteira precisa manter sincronismo nominal entre dois eixos em rotação, sem depender d
- Uma talha ideal ergue carga de 8 kN por meio de quatro segmentos de cabo que efetivamente 
- Um rolamento de esferas opera em condições compatíveis com o modelo básico da ISO 281. Man
- Uma chaveta paralela transmite torque da árvore ao cubo de uma polia. Qual combinação de v
- Uma solda de filete com duas pernas iguais de 6 mm é dimensionada pelo modelo geométrico u
- Uma união aparafusada de tampas sofre esforço externo variável de separação. O projeto est
- Uma mola linear apresenta deslocamento axial de 12 mm quando recebe força de 240 N. Na fai
- Uma embreagem simplificada possui uma interface de atrito ativa, coeficiente 0,30, força n
- Um cilindro hidráulico ideal tem área ativa de 0,002 m² e recebe diferença de pressão de 5
- Uma válvula fornece vazão efetiva constante de 6 L/min a cilindro com área de avanço de 0,
- Duas árvores circulares maciças de mesmo material suportam o mesmo momento fletor elástico
- Um redutor possui dois pares de engrenagens em série com reduções cinemáticas de 3:1 e 4:1
- Um rolamento de esferas mantém sua capacidade dinâmica C, mas a carga dinâmica equivalente
- Um cilindro hidráulico de dupla ação tem haste apenas de um lado. Pressão diferencial igua
- Uma carga vertical elevada por cilindro deve manter posição após interrupção da alimentaçã
- Ao duplicar a velocidade de uma carga de mesma massa antes de frenagem completa, desprezad
- Duas etapas de transmissão trabalham em série, cada uma com eficiência mecânica de 90% sob
- Uma árvore que gira com torque constante de 100 N·m a 50 rad/s transmite potência mecânica
- Um mancal de deslizamento lubrificado que trabalha em regime hidrodinâmico durante a opera
- No projeto de união rebitada submetida a força transversal, verificar apenas a capacidade 
- Uma correia plana que apresenta patinagem mensurável entre polia motora e movida conserva,
- Um pistão hidráulico ideal de área 0,01 m² submetido à diferença de pressão de 2 MPa desen
- Ao substituir engrenagens cilíndricas de dentes retos por helicoidais em um redutor, mante
- Uma transmissão por corrente de rolos pode apresentar oscilação instantânea da velocidade 
- Um instalador que aplica determinado torque de aperto a uma junta roscada obtém uma pré-ca
- Em uma máquina com duas unidades independentes cuja falha de qualquer uma interrompe o ser
- Numa mola helicoidal ideal de compressão, mantidos material, diâmetro médio e número de es
- Em circuito hidráulico de elevação, uma válvula de retenção pilotada pode impedir refluxo 
- Em cálculo de rolamento, a vida básica L10 de um milhão de revoluções significa que todos 
- Uma talha ideal de quatro segmentos efetivamente sustentadores precisa recolher quatro met
- Um equipamento cujo coeficiente de segurança estático contra escoamento é 2 pode ser class
- Uma árvore com superfície entalhada pode romper por fadiga sob flexão alternada mesmo quan
- Uma passarela rolante destinada ao transporte de pessoas pode ter seus requisitos de segur
- Em sistema hidráulico que ergue carga, a válvula de alívio limita a pressão pelo desvio de
- Um monta-cargas projetado exclusivamente para transportar objetos pode ser adaptado operac
- Ao ocorrer cavitação em bomba hidráulica, a formação local de vapor pode produzir ruído e 
- Um cilindro pneumático que apresenta baixa velocidade sob carga deve ser diagnosticado exc
```

## Pedido 11: Design gráfico e produção gráfica (`comunicacao.design-grafico-producao-grafica`, 133 tópicos de edital)

```
Matéria: comunicacao.design-grafico-producao-grafica (Design gráfico e produção gráfica)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, linguagem visual e história do design": 17
- "Teoria da cor, tipografia e hierarquia da informação": 17
- "Grid, composição editorial, infografia e Visual Law": 17
- "Identidade visual, branding e normas institucionais": 17
- "Pré-impressão, métodos de impressão, papel e acabamento": 16
- "Softwares, fluxo de equipe, UX/UI e acessibilidade": 16

Já existem (não repita):
- Uma campanha institucional apresenta informações sem relação dentro de uma mesma caixa e s
- A equipe deseja iniciar o projeto gráfico de um manual técnico que será impresso e publica
- Sobre a história e a linguagem do design gráfico, assinale a associação historicamente mai
- Um cartaz contém muitos recursos decorativos, mas o cidadão não identifica data, local e s
- O espaço em branco pode exercer funções de separação, respiro e hierarquia, ainda que não 
- A Gestalt determina que a aplicação do princípio de semelhança elimina a necessidade de te
- A história da impressão e a história da escrita não podem ser reduzidas à difusão dos tipo
- A adoção de um grid impede, por definição, qualquer composição assimétrica e representa a 
- Para preparar uma fotografia destinada a uma publicação offset em quatro cores, qual siste
- Na revisão de um título, o designer altera o espaço entre as letras de uma palavra inteira
- Na aplicação da WCAG 2.2, nível AA, a uma página de serviço, qual alternativa descreve cor
- Uma marca azul vibrante foi aprovada em monitor, mas o resultado na prova impressa ficou m
- Kerning se refere ao ajuste de espaçamento entre pares específicos de caracteres, não ao e
- Uma mensagem de erro identificada apenas pela cor vermelha é suficiente, do ponto de vista
- Na WCAG 2.2, o contraste mínimo de conteúdo não textual significativo e de certos sinais n
- Uma identidade gráfica exportada com os mesmos valores numéricos de cor em RGB e CMYK terá
- Em um projeto editorial, como se chama o espaçamento entre duas colunas de texto de um gri
- Uma gráfica pede um modelo para conferir visualmente dobras e sequência das páginas antes 
- Uma visualização compara quantidades absolutas de procedimentos realizados por cinco unida
- Uma equipe reorganiza um documento jurídico usando títulos, linha do tempo e quadros, mas 
- A malha de um grid pode orientar alinhamentos mesmo quando suas linhas não aparecem no res
- A imposição gráfica consiste apenas em numerar as páginas na ordem em que o público as ler
- Em infografia de dados públicos, a ausência de indicação da unidade e da fonte pode compro
- Visual Law autoriza excluir condições ou exceções jurídicas do texto sempre que isso torna
- O conjunto de regras sobre cores, assinatura, fontes e aplicações de uma instituição é des
- Uma unidade altera a proporção de uma marca oficial para preencher melhor a capa. Qual ori
- Sobre o Ato Normativo nº 313/2022 da ALECE, assinale a interpretação compatível com o text
- Uma prefeitura pretende usar, sem adaptação, o manual da Câmara dos Deputados para sua mar
- Branding é uma atividade estratégica mais ampla do que a elaboração gráfica de um logotipo
- A área de proteção de uma assinatura visual é sinônimo da sangria que deve ser acrescentad
- O Ato Normativo nº 313/2022 da ALECE remete ao manual institucional a forma de posicioname
- A disponibilização de versões RGB e CMYK de uma assinatura institucional dispensa a consul
- Ao preparar um folheto que terá uma fotografia até a borda final, qual recurso reduz o ris
- Uma imagem de 2.400 pixels de largura será aplicada em 8 polegadas de largura na peça. Qua
- Uma editora pretende imprimir uma tiragem grande e estável de livros, sem dados variáveis.
- Um arquivo chega à gráfica com fonte ausente e imagem vinculada que não foi enviada. Qual 
- A gramatura de um papel, expressa em g/m², informa sua massa por unidade de área, e não di
- Um arquivo PDF exportado pode ser considerado tecnicamente pronto para qualquer gráfica, i
- Na serigrafia, a tinta atravessa áreas permeáveis de uma tela; na flexografia, utilizam-se
- Aumentar artificialmente os metadados de resolução de uma fotografia sempre cria novos det
- No desenvolvimento de uma interface, qual artefato descreve primeiro a organização básica 
- Um formulário eletrônico exibe botões bonitos, mas usuários não sabem que informações deve
- Um portal pretende padronizar formulários, títulos, espaços e estados de botões em diversa
- Na preparação simultânea de um relatório impresso e de uma página informativa, qual decisã
- UX abrange a experiência mais ampla de interação com um produto ou serviço, enquanto UI se
- Um ícone sem texto, descrição acessível ou contexto claro é necessariamente compreendido p
- Um sistema de componentes reutilizáveis pode promover consistência, mas não elimina a nece
- Como as telas de um portal são digitais, os arquivos e permissões de uso de fontes e image
```

## Pedido 12: Pragmatismo, análise econômica do direito e decisão judicial (`direito.pragmatismo-analise-economica-direito`, 133 tópicos de edital)

```
Matéria: direito.pragmatismo-analise-economica-direito (Pragmatismo, análise econômica do direito e decisão judicial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos do pragmatismo e da decisão jurídica": 29
- "2. Análise econômica do direito, propriedade e informação": 21
- "3. Eficiência processual, litigância e acesso à justiça": 21
- "4. Precedentes, segurança jurídica e controle das consequências": 11
- "5. Economia comportamental, vieses e percepção de justiça": 18

Já existem (não repita):
- Ao decidir sobre horário de funcionamento de serviço essencial, o julgador compara dados d
- Em debate sobre interpretação normativa, uma participante afirma que argumentos jurídicos 
- Ao avaliar duas medidas, uma equipe considera seus efeitos futuros, mas não adota a maximi
- Ao anular providência administrativa por invocação genérica da eficiência, uma decisão não
- Pequenos produtores negociam autorização para uso compartilhado de passagem, mas precisam 
- Antes da venda de equipamentos, o vendedor conhece falhas internas que os compradores não 
- Depois de adquirir cobertura contra pequenos danos, uma pessoa reduz os cuidados de conser
- Uma alteração de procedimento reduz a espera de vários usuários sem elevar custos ou impor
- Um projeto de modernização promete reduzir o tempo médio de tramitação, mas poderá impedir
- Alguém calcula chance de 25% de receber R$ 8.000 e gastos próprios esperados de R$ 3.000. 
- Uma parte altera propositalmente a versão de fato incontroverso para provocar incidente qu
- Duas pessoas podem negociar livremente uso de recurso, com direitos claramente definidos e
- Uma decisão invalida contrato administrativo e comunica apenas a conclusão de ilegalidade,
- Uma medida é justificada em nome de interesse público genérico, embora exista opção igualm
- Após decisões divergentes sobre contratos idênticos em fatos juridicamente relevantes, um 
- Uma sentença copia trecho de precedente e conclui que o pedido deve ser rejeitado, mas não
- Em audiência, uma parte menciona valor indenizatório muito alto sem prova, e o valor inici
- Uma intervenção produz benefício de R$ 280 para um grupo e prejuízo de R$ 190 para outro, 
- Uma interpretação nova de norma pública de conteúdo indeterminado impõe dever técnico cuja
- Um tribunal altera jurisprudência dominante que orientava contratos continuados. Cogita li
- Ao julgar responsabilidade por decisão tomada antes de crise imprevisível, alguém afirma q
- O debate sobre medida de proteção apresenta argumentos dedutivos de coerência normativa, p
- A adoção de taxa processual adicional reduziria ações manifestamente infundadas, mas també
- Um litigante tem chance reduzida de recuperar quantia modesta e prevê gasto próprio maior 
- Duas comunicações descrevem estatisticamente o mesmo cenário, uma dizendo que 80 de 100 pe
- Em litígio sobre acesso a serviço, o julgador examina o efeito de duas interpretações, mas
- Em proposta de redistribuição de recursos, um grupo ganha R$ 200 e outro perde R$ 80. Como
- Dois vizinhos têm direitos negociáveis claramente definidos e conseguem pactuar, sem custo
- Uma seguradora desconhece condições anteriores dos clientes e, por isso, todo problema inf
- Ao reformular o fluxo de atendimento judicial, os responsáveis devem considerar tanto a du
- Uma ação possui chance pequena de êxito e gastos próprios superiores ao ganho esperado; ap
- Após contratar cobertura, uma pessoa deixa de adotar cuidado antes rotineiro porque o cust
- Uma decisão que invoca valor jurídico abstrato pode dispensar fundamento normativo e contr
- Um sistema resolve mais controvérsias por acordo, mas seus gestores também avaliam qualida
- Diante de nova informação social, um tribunal fica juridicamente impedido de revisar sua i
- Ao receber precedente citado pela defesa, o julgador entende que o fato central do caso at
- Depois de apresentar sua hipótese inicial, uma pessoa passa a privilegiar apenas documento
- Uma pretensão oferece 40% de chance de recuperar R$ 1.000 e envolve custo privado esperado
- Em conflito de vizinhança envolvendo centenas de titulares, definir claramente os direitos
- Na alteração de jurisprudência dominante abrangida pelo art. 927, § 3º, do CPC, admite-se 
- Mesmo quando o caso atual apresenta fato materialmente distinto do que sustentou precedent
- Uma parte altera conscientemente fatos para obter vantagem e é reconhecida como litigante 
- Uma situação administrativa plenamente constituída de acordo com orientação geral de sua é
- Ao analisar controvérsia de alto impacto, o decisor primeiro calcula estimativa independen
- Uma decisão considera que a opção mais útil para a coletividade pode restringir garantia i
```

## Pedido 13: Lei Orgânica do Distrito Federal (`distrito-federal.lei-organica-df`, 133 tópicos de edital)

```
Matéria: distrito-federal.lei-organica-df (Lei Orgânica do Distrito Federal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, organização e competências do DF": 20
- "2. Administração pública, servidores e bens": 20
- "3. Poder Legislativo: Câmara Legislativa e processo legislativo": 20
- "4. Fiscalização, Tribunal de Contas e Poder Executivo": 20
- "5. Funções essenciais à Justiça e segurança pública": 20

Já existem (não repita):
- Segundo a Lei Orgânica do Distrito Federal, são Poderes do Distrito Federal, independentes
- Ao Distrito Federal são atribuídas as competências legislativas reservadas aos Estados e a
- A criação ou extinção de Regiões Administrativas do Distrito Federal ocorre mediante:
- São valores fundamentais do Distrito Federal, segundo o art. 2º da LODF, EXCETO:
- É vedado ao Distrito Federal doar bens imóveis de seu patrimônio, constituir sobre eles ôn
- Pela LODF, compete ao Distrito Federal, concorrentemente com a União, legislar sobre:
- Segundo a LODF, a administração pública do DF deve fornecer a qualquer cidadão, independen
- A LODF acrescenta aos princípios constitucionais da administração pública os princípios da
- Pela LODF, a publicidade dos atos, programas e campanhas dos órgãos públicos deve ter cará
- A LODF assegura ao servidor público da administração direta, autárquica e fundacional do D
- Sobre os servidores militares do Distrito Federal na LODF, é correto afirmar:
- São bens do Distrito Federal, entre outros, as águas superficiais ou subterrâneas, ressalv
- A Câmara Legislativa do Distrito Federal reunir-se-á, anualmente, em sua sede:
- Na Câmara Legislativa, salvo disposição em contrário, as deliberações são tomadas por maio
- Sobre os Deputados Distritais, assinale a correta.
- A perda do mandato do Deputado Distrital que deixar de comparecer à terça parte das sessõe
- A Lei Orgânica do Distrito Federal poderá ser emendada mediante proposta, entre outras hip
- A proposta de emenda à LODF é discutida e votada em dois turnos, com interstício mínimo de
- É de iniciativa privativa do Governador do Distrito Federal a lei que disponha sobre:
- Se a Câmara Legislativa não se manifestar sobre projeto de iniciativa do Governador com pe
- Pela LODF, o veto do Governador será oposto no prazo de quinze dias úteis, com comunicação
- Constitui lei complementar, segundo o art. 75 da LODF, EXCETO:
- O Deputado Distrital licenciado para tratar de interesse particular, sem remuneração, não 
- O Tribunal de Contas do Distrito Federal é composto por sete Conselheiros, escolhidos:
- O Tribunal de Contas do Distrito Federal deve apreciar as contas anuais do Governador e em
- Em caso de impedimento do Governador e do Vice-Governador do DF, ou de vacância dos cargos
- Vagando os cargos de Governador e Vice-Governador do DF nos últimos dois anos do mandato, 
- São condições de elegibilidade para Governador e Vice-Governador do DF, entre outras, a fi
- O Governador e o Vice-Governador do Distrito Federal não poderão, sem licença da Câmara Le
- Qualquer cidadão, partido político, associação ou entidade sindical poderá denunciar à Câm
- Sobre os Secretários de Estado do Distrito Federal, é correto afirmar:
- Integram o Conselho de Governo do Distrito Federal, entre outros, os líderes da maioria e 
- Segundo a LODF, compete privativamente ao Governador do Distrito Federal, entre outras atr
- A Procuradoria-Geral do Distrito Federal é o órgão central do sistema jurídico do DF, cabe
- Constitui princípio da segurança pública previsto no art. 117-A da LODF:
- O caput do art. 117 da LODF, que enumerava órgãos de segurança pública relativamente autôn
- Sobre a Polícia Civil do Distrito Federal na LODF, assinale a correta.
- O Departamento de Trânsito do Distrito Federal (Detran/DF), segundo a LODF, é:
- A LODF assegura aos policiais militares, policiais civis e bombeiros militares do DF assis
- As doações em espécie recebidas pelos órgãos de segurança pública do DF constituem fundo p
- A segurança do transporte metroviário do DF, segundo a LODF, é exercida:
```

## Pedido 14: Código Penal Militar: parte geral (`direito-penal-militar.parte-geral`, 133 tópicos de edital)

```
Matéria: direito-penal-militar.parte-geral (Código Penal Militar: parte geral)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Aplicação da lei penal militar e classificação do crime militar": 35
- "2. Teoria do crime, dolo, culpa, erros e causas excludentes": 12
- "3. Imputabilidade penal, participação e concurso de agentes": 12
- "4. Penas principais, penas acessórias e efeitos da condenação": 28
- "5. Ação penal, extinção da punibilidade e integração da parte geral": 13

Já existem (não repita):
- Um militar comete infração comum sem conexão com as circunstâncias legais do art. 9º do CP
- Lei nova deixa de considerar crime uma conduta pela qual alguém cumpre condenação penal mi
- Agente começa execução de crime militar, mas causa externa impede a consumação. Qual reduç
- Pessoa repele agressão injusta atual mediante meios necessários e moderados para proteger 
- O autor de fato penal militar possuía dezessete anos no momento da conduta. Que regra deco
- Qual combinação contém somente penas principais ainda previstas no art. 55 do CPM?
- Uma condenação penal militar torna certa a obrigação de reparar o dano gerado pelo crime. 
- Após a Lei nº 14.688/2023, qual instituto deixou de constar como causa de extinção da puni
- Crime previsto apenas na legislação penal comum é praticado por militar da ativa contra ou
- Um fato penal militar foi cometido integralmente no exterior, com pena cumprida por conden
- Subordinado recebe ordem direta para praticar ato manifestamente criminoso e a executa con
- Um agente sacrifica direito alheio consideravelmente inferior ao direito protegido, ante p
- Laudo comprova que doença mental diminuiu consideravelmente, mas não suprimiu, a capacidad
- Três pessoas concorrem para crime militar; uma possui participação de menor importância, s
- Condenação de oficial a pena privativa superior a dois anos leva à discussão de perda de p
- Pessoa inimputável recebe medida de segurança de tratamento ambulatorial no regime do art.
- Agente comete crime militar nas condições do art. 9º, II, em lugar sujeito à administração
- Agente inicia crime militar e o meio empregado se revela absolutamente incapaz de produzir
- Em evento perigoso, agente sacrifica direito alheio mais importante para proteger pessoa m
- Três militares combinam auxiliar crime futuro, mas ninguém inicia execução e não há tipo a
- Um agente comete fato durante embriaguez completa decorrente de evento fortuito imprevisív
- Após condenação penal militar, identifica-se bem produto do crime em poder de terceiro com
- A defesa afirma que pena acessória jamais necessita constar da sentença porque sempre resu
- O Ministério Público deixa de propor ação penal militar no prazo cabível apesar de present
- Crime militar possui pena privativa de liberdade máxima abstrata exatamente de quatro anos
- Crime de tipo previsto na legislação penal comum pode ser considerado militar por extensão
- Segundo o CPM, considera-se tempo do crime sempre o dia em que se produz o resultado, mesm
- O dolo no CPM compreende tanto querer o resultado como assumir o risco de produzi-lo, de a
- O CPM considera causa justificante de estado de necessidade sempre que o mal causado seja 
- O menor de dezoito anos é penalmente inimputável sob a redação vigente do art. 50 do CPM, 
- A pena principal de impedimento foi revogada pela Lei nº 14.688/2023, tendo restado apenas
- No CPM, tornar certa a obrigação de reparar o dano resultante do crime é efeito da condena
- O art. 7º do CPM pode estabelecer incidência da lei penal militar sobre fato ocorrido fora
- O crime culposo é regra geral de punição no CPM, sendo dispensável previsão expressa da fo
- Na hipótese de erro de fato plenamente escusável sobre circunstância que constitui o crime
- Segundo o art. 53, § 1º, circunstâncias pessoais de um coautor comunicam-se obrigatoriamen
- No concurso de agentes do CPM, o agente que promove e organiza a cooperação criminosa pode
- Medidas de segurança pessoais no art. 110 do CPM podem ser detentivas ou não detentivas, e
- A reabilitação ainda figura como causa de extinção da punibilidade no art. 123, V, porque 
- O § 2º do art. 9º do CPM prevê hipóteses em que crimes militares dolosos contra vida de ci
- O art. 38 do CPM assegura impunidade ao subordinado que executa ordem manifestamente crimi
- O CPM distingue inexigibilidade de conduta diversa do estado de necessidade exculpante, ar
- Se a doença mental diminui consideravelmente, mas não elimina a capacidade de entendimento
- A imposição de penas acessórias deve, como regra, constar expressamente da sentença, com a
- A prescrição punitiva de crime militar com pena máxima abstrata exatamente de quatro anos 
```

## Pedido 15: Mastologia (`medicina.mastologia`, 133 tópicos de edital)

```
Matéria: medicina.mastologia (Mastologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Anatomia, fisiologia e desenvolvimento mamário": 17
- "Doenças benignas, inflamatórias e lesões de risco": 17
- "Rastreamento, imagem mamária, BI-RADS e biópsias": 17
- "Patologia do câncer de mama, biomarcadores e estadiamento": 17
- "Tratamento locorregional, sistêmico, reconstrução e seguimento": 16
- "Mama na gestação e lactação, no homem e aspectos éticos": 16

Já existem (não repita):
- Durante consulta no puerpério, a paciente pergunta qual hormônio é responsável pelo reflex
- Mulher relata tecido glandular na axila que aumenta de volume na gravidez e pode secretar 
- O examinador encontra retração focal da pele sobre nódulo mamário. Qual estrutura, quando 
- No planejamento cirúrgico de paciente com carcinoma inicial e axila clinicamente negativa,
- Em relação à clínica do câncer inflamatório, o aspecto de casca de laranja deriva principa
- Jovem de 22 anos tem nódulo mamário móvel, oval e circunscrito, com imagem provavelmente b
- Paciente com descarga papilar espontânea, sanguinolenta, unilateral e de ducto único procu
- Após antibiótico adequado, mulher não melhora de eritema e edema difusos da mama. Qual hip
- A core biópsia de tumor fibroepitelial crescente sugere tumor filoide; qual princípio cirú
- Em mamografia de rastreio, o laudo registra BI-RADS 0. Qual o significado principal?
- Segundo a orientação prioritária do Ministério da Saúde para rastreamento populacional de 
- Mulher de 26 anos apresenta nódulo palpável persistente. Na investigação inicial, qual mét
- Há microcalcificações agrupadas suspeitas vistas na mamografia, mas não visualizadas à ult
- Lesão BI-RADS 5 foi submetida a core biópsia, com laudo benigno que não explica as caracte
- Em mulher assintomática com achado classificado corretamente como BI-RADS 3, sem discordân
- O laudo descreve carcinoma ductal in situ (CDIS) sem evidência de invasão. Qual caracterís
- Paciente com carcinoma invasivo medindo 3,2 cm, sem informação adicional sobre pele ou par
- Um carcinoma expressa RE e RP, mas não apresenta HER2 positivo. Qual biomarcador fundament
- Em revisão da biologia molecular, um teste germinativo mostra variante de significado ince
- Uma paciente tem carcinoma mamário com eritema e edema difuso típicos de carcinoma inflama
- Uma mulher com câncer de mama receberá terapia sistêmica antes da operação para reduzir vo
- Paciente elegível para conservação mamária pergunta se cirurgia conservadora é necessariam
- Em sobrevivente de câncer de mama sem sintomas e sem suspeita clínica de recidiva, qual pr
- Gestante com nódulo mamário palpável persistente procura avaliação. Qual exame é habitualm
- Homem de 63 anos apresenta nódulo duro excêntrico à aréola e descarga sanguinolenta. Qual 
- A maior parte da drenagem linfática mamária dirige-se à axila, embora existam vias para ca
- A prolactina é o principal hormônio responsável pela ejeção do leite, enquanto a ocitocina
- A presença de politelia significa obrigatoriamente tecido glandular ectópico capaz de amam
- O encontro de um linfonodo sentinela representa, por definição, a retirada de todos os lin
- O fibroadenoma é uma lesão fibroepitelial benigna que frequentemente se apresenta como nód
- Toda secreção papilar leitosa bilateral, à expressão e por múltiplos ductos deve ser class
- O tumor filoide pode variar entre benigno, borderline e maligno, e sua cirurgia não exige 
- Mastite que persiste após tratamento adequado pode exigir avaliação para carcinoma inflama
- BI-RADS 6 significa que a imagem é normal e que não há necessidade de documentação de bióp
- Uma paciente sintomática com massa endurecida deve ser avaliada mesmo que tenha menos de 5
- Um resultado histológico benigno elimina toda possibilidade de câncer mesmo quando o exame
- Microcalcificações mamográficas suspeitas sem correspondência à ultrassonografia podem ser
- No SUS, a orientação prioritária de rastreamento bienal dos 50 aos 74 anos proíbe categori
- A categoria M1 no TNM mamário indica metástase a distância, independentemente do tamanho d
- A positividade de HER2 é sinônimo de positividade obrigatória para receptores de estrogêni
- Carcinoma de mama de 6 cm, sem extensão cutânea ou à parede torácica, se enquadra na categ
- A variante germinativa de significado incerto constitui prova de predisposição patogênica 
- Terapia adjuvante, no uso habitual da oncologia mamária, é realizada após o tratamento loc
- A reconstrução mamária imediata é sempre obrigatória após mastectomia, independentemente d
- No seguimento de pessoa tratada de câncer de mama, assintomática e sem achado suspeito, PE
- A ginecomastia corresponde a proliferação glandular mamária masculina; pseudoginecomastia 
- Uma massa mamária persistente na gestação deve ser investigada, frequentemente com ultrass
- Trastuzumabe e endocrinoterapia devem ser usados rotineiramente durante a gestação quando 
- A idade cronológica, isoladamente, deve impedir qualquer tratamento oncológico de câncer d
```

## Pedido 16: Incerteza, informação assimétrica, teoria dos jogos e economia comportamental (`economia.incerteza-informacao-teoria-jogos`, 132 tópicos de edital)

```
Matéria: economia.incerteza-informacao-teoria-jogos (Incerteza, informação assimétrica, teoria dos jogos e economia comportamental)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Escolha sob incerteza e preferências em relação ao risco": 18
- "2. Seguros, diversificação e administração econômica do risco": 19
- "3. Informação assimétrica, seleção adversa, sinalização e triagem": 17
- "4. Teoria dos jogos: estratégia, equilíbrio de Nash e dilema do prisioneiro": 18
- "5. Economia comportamental e teoremas fundamentais do bem-estar": 28

Já existem (não repita):
- Uma aplicação paga 80 ou 120 unidades monetárias, cada uma com probabilidade de 50%. O val
- Um consumidor prefere receber 500 com certeza a participar de uma loteria de valor esperad
- Uma pessoa tem probabilidade de 10% de sofrer perda de 20.000. Sem custos de carregamento,
- Após comprar seguro integral contra furto, um proprietário reduz a vigilância de sua loja 
- O vendedor de um computador usado conhece defeito interno que o comprador não consegue obs
- Em uma negociação, alguém conhece melhor sua produtividade e oferece certificação verificá
- Duas empresas escolhem simultaneamente estratégias. Em certo perfil, nenhuma consegue aume
- Um consumidor sente a perda de 100 de forma mais intensa que a satisfação obtida com ganho
- Sob concorrência perfeita e as demais hipóteses adequadas de completude dos mercados, o pr
- Para uma pessoa com utilidade da riqueza u(W)=√W, uma loteria oferece riqueza final de 100
- Um ativo rende 0 ou 100 com probabilidades iguais. Outro rende 50 de maneira certa. Em rel
- Dois investimentos A e B apresentam variâncias de retorno de 100 cada e covariância zero. 
- Um contrato cobre danos acima de uma franquia fixa de 1.000. O prejuízo segurável observad
- Uma seguradora oferece duas apólices: uma com prêmio elevado e franquia baixa; outra com p
- Num mercado de carros usados, compradores não distinguem qualidade. O preço médio oferecid
- Em jogo simultâneo, os pagamentos são (jogador 1, jogador 2): A/A=(4,4), A/B=(0,0), B/A=(0
- Uma regra torna a inscrição em um plano de poupança automática, mas admite saída simples e
- Dois ativos têm variância 100 cada e correlação exatamente −1. Uma carteira mantém 50% em 
- Com u(W)=√W, um agente enfrenta riqueza final de 400 com probabilidade 25% e 100 com proba
- Um seguro reúne igual quantidade de clientes com probabilidade de sinistro de 1% e de 9%, 
- Uma empresa de boa qualidade pode oferecer garantia custosa. O custo esperado da garantia 
- Em jogo infinitamente repetido, cooperar dá 3 por período a cada jogador; desviar unilater
- No jogo de moedas simultâneo, o jogador 1 recebe +1 quando as moedas coincidem e −1 quando
- Uma alocação eficiente é proposta para descentralização por preços competitivos após redis
- Uma política pretende corrigir externalidade negativa em um mercado onde consumidores segu
- Uma aplicação entrega 30 com probabilidade de 20% e 80 com probabilidade de 80%. O valor e
- Um seguro cobre perda de 50.000 que ocorre em 4% dos casos. Sem despesas adicionais, o prê
- O banco que desconhece o verdadeiro risco de inadimplência de cada solicitante e oferece c
- Em um jogo, se nenhum participante deseja se desviar unilateralmente de determinado perfil
- Se duas pessoas recebem 4 no perfil (A,A) e apenas 0 caso uma delas mude isoladamente para
- Aversão à perda significa que, para qualquer consumidor, uma redução monetária de 100 gera
- Com u(W)=√W, uma loteria que paga 25 ou 225 em probabilidades iguais tem utilidade esperad
- Uma carteira de dois ativos igualmente voláteis reduz integralmente o risco ao repartir pe
- Uma pessoa já sabe, antes de procurar uma apólice, que possui risco de sinistro superior à
- Uma certificação observável só pode funcionar como sinal de qualidade caso produza custo i
- Em jogo simultâneo com duas estratégias, um participante pode ter melhor resposta A quando
- Um investidor compra um ativo porque observa outros investidores comprando. Esse fato, con
- Uma alocação pode ser Paretoeficiente e, ao mesmo tempo, distribuir quase todos os recurso
- Para u(W)=√W, uma loteria equiprovável de riquezas 100 e 400 tem equivalente certo igual a
- Um contrato com franquia pode reduzir o risco moral ao fazer o segurado suportar parte da 
- Em mercado com seleção adversa de crédito, aumentar a taxa de juros oferecida a todos os c
- No dilema do prisioneiro com pagamentos (C,C)=(3,3), (D,C)=(5,0), (C,D)=(0,5) e (D,D)=(1,1
- Em um jogo infinitamente repetido, com ganhos de cooperação de 3 por período, desvio inici
- O segundo teorema fundamental do bem-estar requer condições adicionais de convexidade e su
- Uma externalidade ambiental relevante pode permanecer não internalizada em equilíbrio de m
```

## Pedido 17: Crimes de responsabilidade e crimes contra as finanças públicas (`legislacao-penal-especial.crimes-responsabilidade-fiscal`, 132 tópicos de edital)

```
Matéria: legislacao-penal-especial.crimes-responsabilidade-fiscal (Crimes de responsabilidade e crimes contra as finanças públicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais e crimes de responsabilidade": 18
- "2. Lei nº 1.079/1950: sujeitos e processo de julgamento": 18
- "3. Decreto-Lei nº 201/1967: crimes de prefeitos e processo penal": 13
- "4. Decreto-Lei nº 201/1967: cassação e responsabilidade parlamentar municipal": 13
- "5. Lei nº 10.028/2000 e Código Penal: proteção das finanças públicas": 38

Já existem (não repita):
- Uma autoridade federal pratica uma conduta enquadrada na Lei nº 1.079/1950. Ao comparar es
- Uma constituição estadual pretende criar hipótese autônoma de crime de responsabilidade e 
- Depois de apresentada denúncia por crime de responsabilidade contra o Presidente da Repúbl
- A Câmara dos Deputados autoriza o processo por crime de responsabilidade presidencial. Con
- Após condenação presidencial por crime de responsabilidade pelo Senado, a defesa discute o
- Uma pessoa com condição de cidadã pretende apresentar denúncia por crime de responsabilida
- No início de uma sessão legislativa, constata-se que não foram apresentadas as contas fede
- Em determinada gestão federal, foi autorizada a abertura de crédito contrariando limites e
- Instaurado no Senado processo por crime de responsabilidade presidencial, o julgamento não
- A Câmara autoriza o prosseguimento de acusação presidencial. A defesa afirma que o Senado 
- Um prefeito é denunciado pela prática de um dos delitos previstos no art. 1º do Decreto-Le
- Em processo criminal municipal, houve condenação por apropriação de renda pública em prove
- O gestor municipal recebeu auxílio de outro ente e deixou de prestar contas de sua aplicaç
- Um prefeito sofre condenação definitiva por delito do art. 1º do Decreto-Lei nº 201/1967. 
- Antes do recebimento da denúncia por crime do art. 1º do Decreto-Lei nº 201/1967, o acusad
- A Câmara Municipal instaura procedimento por infração político-administrativa do prefeito,
- Em procedimento político-administrativo contra prefeito, vereadores discutem os quóruns pa
- A comissão processante da Câmara não concluiu o julgamento de infração político-administra
- O acusado por infração político-administrativa municipal apresenta defesa escrita após a i
- Uma Câmara Municipal recebe notícia de renúncia escrita do prefeito e debate se deve abrir
- Um gestor ordena operação de crédito interno sem a prévia autorização legislativa exigida.
- O responsável pela execução orçamentária autoriza lançar em restos a pagar despesa que seq
- Nos dois últimos quadrimestres do último ano de um mandato, é autorizada obrigação cuja pa
- Faltando 120 dias para o fim do mandato, uma autoridade ordena ato que aumenta a despesa t
- Um responsável deixa de enviar o relatório de gestão fiscal ao Legislativo e ao Tribunal d
- Um prefeito impede o funcionamento regular da Câmara Municipal. Para apurar essa infração 
- Ao investigar apropriação de renda pública atribuída a prefeito, a Câmara Municipal deve a
- Um prefeito é processado pela conduta de ordenar despesas não autorizadas por lei, previst
- O vice-prefeito que substituiu temporariamente o titular não responde pelo procedimento do
- Recebida denúncia político-administrativa contra prefeito pela Câmara, constitui-se na mes
- O mandato do prefeito que apresentou renúncia escrita somente se extinguirá depois de vota
- Uma operação de crédito foi formalmente autorizada pelo legislativo, mas violou limite est
- Quando o responsável ordena despesa sem autorização por lei, a descrição literal do art. 3
- Um gestor presta garantia de valor de R$ 2 milhões em operação de crédito sem constituir c
- A conduta de ordenar a inscrição de despesa não empenhada em restos a pagar é definida no 
- Para caracterizar a conduta de oferta pública de títulos da dívida do art. 359-H do Código
- Uma autoridade responsável deixa de expedir ato de limitação de empenho e movimentação fin
- O art. 359-C do Código Penal utiliza o mesmo período de 180 dias anteriores ao encerrament
- A Lei nº 10.028/2000 acrescentou ao art. 10 da Lei nº 1.079/1950 condutas ligadas à dívida
- O art. 14 da Lei nº 1.079/1950 estabelece que denúncias presidenciais por crime de respons
- No julgamento presidencial por crime de responsabilidade, a condenação pelo Senado exige d
- Como o rito de cassação de prefeito integra a autonomia municipal, uma lei estadual pode r
- Ao admitir servidor contra expressa disposição de lei, o prefeito pode incidir no art. 1º,
- Uma violação ao art. 5º, II, da Lei nº 10.028/2000, consistente em propor LDO sem metas fi
- Quando uma operação de crédito irregular de prefeito também apresenta elementos de delito 
```

## Pedido 18: Conversão de demonstrações, reestruturação e políticas contábeis (`contabilidade.conversao-reestruturacao-politicas-contabeis`, 132 tópicos de edital)

```
Matéria: contabilidade.conversao-reestruturacao-politicas-contabeis (Conversão de demonstrações, reestruturação e políticas contábeis)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Moeda funcional, transações em moeda estrangeira e diferenças de câmbio": 15
- "2. Conversão de demonstrações do exterior e inflação": 22
- "3. Incorporação, fusão, cisão e contabilização de combinações": 15
- "4. Políticas contábeis, mudanças de estimativa e erros": 34
- "5. Partes relacionadas, divulgação e integração dos temas": 14

Já existem (não repita):
- Uma empresa brasileira, com moeda funcional real, compra mercadorias por USD 4.000 quando 
- Uma indústria possui dívida monetária de EUR 10.000 reconhecida a R$ 5,00 por euro. Na dat
- Uma entidade comprou equipamento por USD 5.000 a R$ 4,80, mantendo-o registrado pelo custo
- Duas sociedades aprovam operação pela qual uma delas absorve integralmente a outra, mantém
- Duas sociedades são extintas para dar origem a uma terceira, que sucede ambas em seus dire
- A administração revê a vida útil remanescente de uma máquina devido a novas informações té
- Uma controladora possui integralmente outra sociedade e não realizou transações com ela du
- Uma sociedade transfere apenas uma de suas unidades de negócio, com ativos e passivos, a o
- Uma controlada no exterior, cuja moeda funcional não é hiperinflacionária, tem ativos de E
- Uma companhia descobre em 2026 que deixou de contabilizar despesa relevante de 2025, apesa
- A entidade mantém empréstimo a receber de USD 6.000, inicialmente registrado à taxa de R$ 
- Uma entidade relata inflação acumulada de 102% nos últimos três anos em país onde opera e 
- Uma investidora adquire 100% de negócio independente por R$ 1.400.000, cujos ativos líquid
- Um credor anterior a incorporação regida pela Lei nº 6.404/1976 demonstra prejuízo causado
- Uma empresa depende economicamente de um único comprador, mas não há controle, influência 
- Uma entidade situada em economia efetivamente hiperinflacionária mantém imobilizado ao cus
- Controladora brasileira consolida uma operação estrangeira não hiperinflacionária de moeda
- Uma empresa decide voluntariamente aplicar política contábil mais relevante, atendendo ao 
- Uma operação no exterior apresenta moeda funcional em país com economia hiperinflacionária
- Sociedades A e B já eram controladas integralmente pela mesma pessoa antes e depois de uma
- Uma companhia realiza cisão parcial sob a Lei nº 6.404/1976 e estipula no ato que as recep
- Uma companhia altera sua moeda funcional de dólar para real porque mudaram sustentadamente
- Uma aquisição independente enquadrada no CPC 15 resulta em contraprestação de R$ 900 mil e
- A entidade realizou empréstimo substancial à sua controladora, classificado como transação
- Uma sociedade possui dívida de longo prazo com filial estrangeira e a liquidação não está 
- Uma entidade apresenta demonstrações em reais, mas usa dólar como moeda funcional porque é
- Uma empresa com estoque adquirido em dólar e registrado ao custo histórico deve converter 
- Duas sociedades que se extinguem para constituir uma terceira, sucessora de direitos e obr
- Na cisão parcial de sociedade, a pessoa jurídica cindida deve extinguir-se porque toda tra
- Uma companhia revê a vida útil de máquina com base em laudo novo, sem falha na mensuração 
- Uma controladora e sua controlada não celebraram contrato nem trocaram recursos no exercíc
- Em demonstrações de entidade cuja moeda funcional é de economia hiperinflacionária, o ganh
- Uma controlada no exterior com moeda funcional não hiperinflacionária pode ter todas as re
- Uma empresa com conta a receber em dólar verifica valorização do dólar entre o reconhecime
- A diretoria pode mudar livremente uma política contábil aplicada a operações semelhantes, 
- Uma reorganização empresarial envolve duas entidades já submetidas ao mesmo controlador an
- Uma incorporação de companhia por outra, sujeita à Lei nº 6.404/1976, garante ao credor an
- Uma companhia que divulga remuneração do pessoal-chave da administração pelo CPC 05 deve i
- Duas empresas que têm o mesmo integrante no conselho administrativo são partes relacionada
- Se uma economia registra inflação acumulada de 101% em três anos, o CPC 42 a declara autom
- Uma alteração voluntária legítima de política contábil sem regra transitória específica de
- Uma fusão juridicamente válida entre sociedades do mesmo grupo, sob controle comum, determ
- Um ativo não monetário mantido pelo valor justo determinado em moeda estrangeira é convert
- Como o art. 132 do CTN disciplina sucessão tributária nas reorganizações, a incorporação e
- Se uma companhia possui controlada, deve evidenciar a relação de controle ainda que não ex
```

## Pedido 19: Seres vivos e classificação (`biologia.seres-vivos-classificacao`, 132 tópicos de edital)

```
Matéria: biologia.seres-vivos-classificacao (Seres vivos e classificação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Taxonomia, sistemática, hierarquia e nomenclatura biológica": 27
- "2. Evolução, seleção natural, especiação, filogenia e cladística": 18
- "3. Domínios, reinos didáticos, vírus, bactérias e fungos": 18
- "4. Filos animais, parasitos e ciclos de transmissão": 19
- "5. Cordados, vertebrados, plantas terrestres e síntese classificatória": 18

Já existem (não repita):
- Ao organizar uma coleção, uma estudante precisa ordenar os níveis taxonômicos do mais abra
- Um catálogo zoológico registra a onça-pintada com denominação científica em formato binomi
- Pesquisadores reúnem fósseis, caracteres anatômicos e dados comparativos para reconstruir 
- Uma explicação histórica afirma que o alongamento de estruturas pelo uso durante a vida se
- Após mudança ambiental, uma variante herdável existente passou a contribuir para maior pro
- Uma ficha precisa situar uma arqueia metanogênica, uma bactéria e um cogumelo segundo a cl
- Uma amostra contém partículas com ácido nucleico envolto por capsídeo, sem metabolismo cel
- Durante classificação de organismos que decompõem matéria orgânica, encontram-se hifas, di
- Um cladograma apresenta quatro espécies reunidas a partir de um ancestral comum, mas a cla
- Duas populações de uma espécie foram separadas pelo surgimento de um rio e, após muitas ge
- Uma revisão registra *Panthera leo* e *Panthera onca*. O que pode ser inferido necessariam
- Uma lista apresenta *Gorilla gorilla* e, como proposta de espécie vegetal, *Linaria linari
- Uma cultura de microrganismos apresenta células sem núcleo membranoso; estudos as atribuem
- Em região endêmica, uma pessoa entrou em água doce contendo formas liberadas por caramujos
- Um homem consome carne bovina insuficientemente cozida com cisticercos de *Taenia saginata
- Uma pessoa ingere acidentalmente alimento contaminado com ovos de *Taenia solium* eliminad
- Uma criança ingere ovos embrionados de *Ascaris lumbricoides*. Uma descrição do ciclo afir
- Um levantamento encontra anemia associada a vermes intestinais em pessoas que caminham des
- No ciclo de *Wuchereria bancrofti*, um inseto hematófago ingere estágios circulantes no sa
- Um pesquisador distingue hospedeiro definitivo e intermediário pela fase sexuada do parasi
- Em uma comunidade foram identificados triatomíneos infectados por *Trypanosoma cruzi*. Uma
- Um animal com cnidócitos, simetria predominantemente radial e tecidos organizados foi conf
- A larva de um tunicado apresenta notocorda e tubo nervoso dorsal oco, embora o adulto seja
- Uma árvore produz estróbilos e sementes que não ficam encerradas em fruto verdadeiro. Um o
- Em aulas sobre alternância de gerações, foram comparados musgos e samambaias. Qual conjunt
- No sistema taxonômico hierárquico, o nível família reúne gêneros, e o nível gênero reúne e
- O epíteto específico 'sapiens' é, por si só, um nome científico binomial completo de espéc
- O mecanismo clássico associado a Lamarck propõe que uso e desuso de estruturas possam gera
- Na seleção natural formulada por Darwin e Wallace, cada indivíduo modifica intencionalment
- Bacteria e Archaea correspondem a domínios distintos de organismos procariontes, enquanto 
- Vírus apresentam células procarióticas próprias e podem se multiplicar autonomamente fora 
- Águas-vivas e anêmonas marinhas pertencem ao grupo dos cnidários e possuem cnidócitos, car
- O ICZN e o ICN Madrid Code vedam igualmente a repetição exata do nome genérico como epítet
- Um clado monofilético inclui ancestral comum e a totalidade de seus descendentes; comparti
- A ocorrência de separação geográfica entre populações comprova imediatamente a existência 
- Os fungos podem ser unicelulares ou multicelulares e, em geral, obtêm nutrientes por diges
- Na esquistossomose por *Schistosoma mansoni*, o miracídio liberado em água doce infecta di
- Uma pessoa pode desenvolver cisticercose após ingerir ovos de *Taenia solium* provenientes
- A malária e a doença de Chagas são helmintíases porque seus agentes causadores são vermes 
- No ciclo de *Plasmodium*, o mosquito fêmea do gênero *Anopheles* pode ser denominado hospe
- Na transmissão vetorial clássica da doença de Chagas, *Trypanosoma cruzi* é injetado diret
- Na filariose linfática por *Wuchereria bancrofti*, mosquitos ingerem microfilárias ao alim
- A classificação de um organismo como cordado exige vértebras e coluna vertebral formadas n
- Samambaias são plantas vasculares sem sementes e, em seus ciclos, o esporófito constitui a
- Todos os mamíferos são vivíparos, razão pela qual monotremados ovíparos não pertencem ao g
```

## Pedido 20: Psicologia judiciária (`psicologia.psicologia-judiciaria`, 132 tópicos de edital)

```
Matéria: psicologia.psicologia-judiciaria (Psicologia judiciária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Comportamento de partes e testemunhas no contexto judicial": 21
- "2. Memória, verdade judicial e técnicas de entrevista protegida": 19
- "3. Assédio moral, assédio sexual e prevenção de discriminação": 13
- "4. Negociação, conciliação, mediação e comunicação do consenso": 24
- "5. Relacionamento interpessoal, comunicação com a sociedade e a mídia": 23

Já existem (não repita):
- Uma testemunha hesita, chora e evita olhar diretamente para quem a ouve. Que interpretação
- Uma pessoa afirma: 'Vi o automóvel estacionado; por isso, concluí que o motorista aguardav
- Ao colher relato de uma testemunha, o entrevistador procura evitar interferência sobre o c
- Uma criança conta o necessário sobre situação de violência a uma equipe da rede de proteçã
- Um setor solicita parecer sobre conduta abusiva no ambiente de trabalho. Qual é a norma na
- Em reunião de conciliação, os interessados não tinham vínculo anterior e o facilitador sug
- Uma pessoa é convidada à mediação e informa que prefere não prosseguir após a primeira reu
- Duas pessoas possuem relação familiar contínua e desejam reconstruir o diálogo sobre respo
- Um facilitador pergunta: 'Se entendi corretamente, o ponto mais importante é manter contat
- Uma autoridade pretende comentar perante a imprensa seu convencimento sobre o mérito de pr
- Após assistir à cobertura televisiva do caso, a testemunha passa a mencionar cor de roupa 
- Uma criança de seis anos será ouvida como vítima de violência. Qual regra específica integ
- Uma pessoa terceirizada relata humilhações reiteradas durante trabalho no órgão judicial. 
- Uma pessoa subordinada é constrangida por superior funcional que exige favorecimento sexua
- Em mediação de conflito patrimonial, não houve consenso e uma parte não deseja nova sessão
- As pessoas celebram acordo em mediação e recebem termo final sem homologação judicial. De 
- Após a Resolução CNJ nº 672/2026, um CEJUSC celebra acordo pré-processual. De acordo com o
- Durante uma negociação, uma parte afirma 'não aceito parcelamento'; após escuta, explica q
- Uma psicóloga pretende registrar em documento que a pessoa avaliada mentiu porque apresent
- Uma notícia de assédio no ambiente de trabalho vem acompanhada de pedido para que a pessoa
- A equipe de proteção já entrevistou adolescente sobre os cuidados necessários. A autoridad
- Uma unidade recebeu denúncia de mensagens sexuais indesejadas entre colegas sem superiorid
- Uma cláusula contratual de mediação exige comparecimento à primeira reunião. A parte compa
- Depois da alteração de 2026 na Resolução CNJ nº 125/2010, um núcleo define fluxo de mediaç
- Um setor pretende divulgar manifestação de testemunha sob segredo de justiça para demonstr
- Uma testemunha pode demonstrar ansiedade intensa diante da autoridade sem que esse estado 
- Uma lembrança relatada com grande convicção e riqueza de detalhes corresponde, necessariam
- A escuta especializada prevista no art. 7º da Lei nº 13.431/2017 ocorre perante órgão da r
- O crime de assédio sexual do art. 216-A do Código Penal é identificado pela existência de 
- O CPC, art. 165, § 3º, indica atuação preferencial do mediador quando existe vínculo anter
- Em entrevista para apurar um episódio, perguntas que já introduzem arma, autoria e intençã
- Como o depoimento especial tem objetivo protetivo, a Lei nº 13.431/2017 permite que seja p
- A proteção institucional contra assédio e discriminação regulada pela Resolução CNJ nº 351
- Se contrato prevê comparecimento à primeira sessão de mediação, a Lei nº 13.140/2015 obrig
- A confidencialidade na mediação protege as informações produzidas no procedimento, mas enc
- No contexto judicial, a Resolução CFP nº 31/2022 autoriza concluir que uma testemunha ment
- Uma pessoa pode formar falsa memória e relatar com sincera convicção informação imprecisa,
- O art. 11 da Lei nº 13.431/2017 estabelece que o depoimento especial de criança menor de s
- O art. 2º, III, da Resolução CNJ nº 351/2020 permite reconhecer assédio sexual institucion
- Por envolver comunicação e escuta, a mediação deve substituir a atuação dos canais de acol
- O art. 20 da Lei nº 13.140/2015 admite encerrar a mediação sem consenso mediante termo fin
- Segundo as alterações de 2026 na Resolução CNJ nº 125/2010, a homologação pré-processual d
- O art. 8º-A, § 2º, da Resolução CNJ nº 125/2010, conforme redação de 2026, admite que sess
- A transparência institucional permite divulgar íntegra de depoimento protegido por segredo
- Quando há possibilidade de prejuízo à comunicação por barreira de linguagem ou acessibilid
```

## Pedido 21: Contravenções penais, terrorismo e leis penais diversas (`legislacao-penal-especial.contravencoes-leis-penais-diversas`, 131 tópicos de edital)

```
Matéria: legislacao-penal-especial.contravencoes-leis-penais-diversas (Contravenções penais, terrorismo e leis penais diversas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei das Contravenções Penais: regras gerais e tipos recorrentes": 25
- "2. Terrorismo e crimes contra o Estado Democrático de Direito": 18
- "3. Proteção de vítimas, testemunhas e sigilo de certames": 15
- "4. Responsabilidade político-administrativa e crimes econômicos especiais": 13
- "5. Esporte, transplantes, biossegurança e proteção da juventude": 29

Já existem (não repita):
- Uma pessoa inicia a execução de conduta prevista como contravenção, mas é interrompida ant
- Em viagem a outro país, uma pessoa pratica ali fato descrito apenas como contravenção pela
- Um responsável por estabelecimento usa equipamento sonoro abusivamente e compromete o repo
- Uma pessoa pratica vias de fato contra mulher por razões da condição do sexo feminino, na 
- Uma anotação de estudo cita o art. 65 da LCP para fundamentar acusação relativa a fato oco
- Uma investigação apura emprego de explosivo para provocar terror generalizado motivado por
- Pessoas participam de manifestação pacífica para reivindicar direitos sociais e criticam d
- Um grupo tenta, por violência, depor o governo legitimamente constituído, sem que o caso d
- Investigada realiza atos preparatórios de terrorismo com propósito inequívoco de consumar 
- Uma pessoa reúne e mantém valores destinados ao planejamento de crimes de terrorismo. Não 
- Durante a apuração de fraude eleitoral, verifica-se violação indevida de mecanismo de segu
- Uma testemunha gravemente ameaçada aceita receber escolta, mas rejeita ingresso no program
- Uma testemunha está no programa de proteção há dois anos; o perigo que justificou a admiss
- Uma pessoa divulga previamente conteúdo sigiloso de exame público com fim de beneficiar te
- Um prefeito apropria-se de renda pública em benefício próprio. Em outra situação, impede o
- Em setembro de 2026, pessoa vinculada ao comércio de combustível guarda conscientemente pr
- Projeto social afirma que uma pessoa com 28 anos é contemplada pelo Estatuto da Juventude.
- Integrante de programa de proteção tem identidade revelada em documentos e requer mudança 
- Um acusado primário presta colaboração efetiva e voluntária, produzindo os resultados exig
- Em uma disputa eleitoral, agressor usa violência psicológica para impedir o exercício de d
- Pessoa oferece pagamento a participante de competição para manipular seu resultado. Não há
- Um transportador sabe que tecidos humanos foram retirados contrariamente à Lei 9.434/1997,
- Uma pessoa libera organismo geneticamente modificado em desconformidade com normas compete
- Comerciante primário é condenado pelo art. 1º-B da Lei 8.176/1991, introduzido em 2026, em
- Funcionário público permite acesso de pessoas não autorizadas a conteúdo sigiloso de concu
- Uma contravenção é apurada após comunicação feita por terceiro, e a pessoa prejudicada não
- Pessoa brasileira praticou no exterior fato previsto exclusivamente como contravenção pela
- Alguém começa a executar uma contravenção, mas a execução é interrompida sem consumação; n
- A pessoa que, em 2026, perturba a tranquilidade de terceiro por motivo reprovável responde
- Três pessoas constituem organização destinada a executar atos de terrorismo. Ainda que não
- Participantes de movimento social que promovem protesto pacífico contra decisão governamen
- Grupo de pessoas critica publicamente decisões de poderes constitucionais e realiza passea
- Uma vítima ameaçada recusa ingresso formal em programa de proteção. O art. 2º, § 3º, da Le
- Na apreciação de pedido de ingresso no programa da Lei 9.807/1999, o conselho deliberativo
- Um trabalhador encarregado de guardar provas permite acesso de pessoa não autorizada a con
- Ao impedir o funcionamento regular da Câmara Municipal, prefeito pode responder pela infra
- O Estatuto da Juventude define jovens entre quinze e vinte e nove anos e, por isso, incrim
- Uma pessoa promete vantagem não patrimonial a participante de competição para falsear resu
- Investigado pratica ato descrito no art. 2º da Lei 13.260/2016, com motivação e finalidade
- Quando o agente tenta, com violência, abolir o Estado Democrático de Direito e restringir 
- A pessoa que compartilha questões de exame já disponibilizadas oficialmente ao público, se
- No art. 1º-B da Lei 8.176/1991, com redação incluída em setembro de 2026, a natureza do pr
- Um transportador movimenta material humano irregularmente retirado, mas desconhece a obten
- Pesquisador realiza clonagem humana. A Lei 11.105/2005 prevê o tipo do art. 26, com pena d
- Em 2026, os crimes contra o Estado Democrático de Direito continuam disciplinados principa
```

## Pedido 22: Tributos federais: IRPJ, CSLL, PIS/Cofins, Cide e ITR (`direito-tributario.tributos-federais-renda-pis-cofins`, 131 tópicos de edital)

```
Matéria: direito-tributario.tributos-federais-renda-pis-cofins (Tributos federais: IRPJ, CSLL, PIS/Cofins, Cide e ITR)
Capítulos e quantas questões quero em cada um (total 100):
- "1. IRPJ: regimes, adicional e situações internacionais": 16
- "2. CSLL: base, alíquotas e articulação com o IRPJ": 25
- "3. PIS/Pasep e Cofins: incidência, cumulatividade e créditos": 23
- "4. Cide-Combustíveis: fatos geradores, sujeitos e base específica": 18
- "5. ITR: incidência, valor da terra nua e apuração": 18

Já existem (não repita):
- Uma sociedade apurou base tributável de IRPJ de R$ 50.000 em trimestre completo, sem deduç
- Uma empresa comercial em lucro presumido, numa situação sem receitas extras nem ajuste de 
- Uma pessoa jurídica sujeita ao regime cumulativo geral de PIS/Cofins possui faturamento tr
- Um imóvel rural foi vendido em abril de determinado ano. Para identificar o fato gerador a
- Uma empresa importa combustível classificado entre os produtos do art. 3º da Lei 10.336/20
- Uma empresa com base trimestral de IRPJ de R$ 120.000 pede o cálculo da parcela sujeita ao
- Na primeira apuração trimestral de 2026, uma empresa comercial em lucro presumido excedeu 
- Uma indústria no regime não cumulativo geral tem receita tributável de R$ 100.000 e aquisi
- Uma sociedade comercial comum possui base de CSLL de R$ 100.000 apurada após os ajustes le
- Uma empresa brasileira vende mercadoria a controlada situada no exterior. O contrato fixa 
- Uma propriedade rural apresenta VTN de R$ 900.000, área total de 300 hectares e área tribu
- Um produtor de combustível legalmente abrangido realizou operação interna de 40 m³. Para u
- Uma indústria precisa adquirir equipamento de proteção sanitária indispensável ao processo
- Uma empresa realiza exportação de bens e pretende classificar automaticamente toda a recei
- Uma empresa importadora é também contribuinte da Cide-Combustíveis na venda posterior em m
- Uma pessoa explora com sua família uma gleba rural de 25 hectares em município fora das re
- Uma equipe tributária examina documentos de outubro de 2026 e afirma que PIS e Cofins já t
- Uma sociedade no lucro real apura resultado ajustado positivo de CSLL de R$ 300.000 e disp
- Uma pessoa jurídica comercial apura lucro presumido no primeiro trimestre de 2026 e no pri
- Uma empresa brasileira controla sociedade no exterior e pretende lançar os lucros da contr
- Um comerciante, no regime não cumulativo, adquiriu mercadorias para revenda com documentaç
- No segundo trimestre de 2026, sociedade comercial tem parcela da receita acima do limite d
- Para apuração do ITR, uma propriedade possui área total de 500 hectares e área aproveitáve
- Um município firmou convênio que lhe permite fiscalizar e cobrar o ITR relativo a imóveis 
- Uma empresa passou por incorporação societária e o responsável afirma que todas as bases n
- Uma empresa com base de IRPJ de R$ 30.000 em um trimestre completo, sem deduções, deverá p
- Uma pessoa jurídica comercial submetida à alíquota geral de CSLL pode reduzir a contribuiç
- Um importador de gasolina legalmente enquadrada pode ser contribuinte da Cide-Combustíveis
- Um proprietário rural deve incluir o valor de galpões, currais e instalações na base do IT
- Uma prestadora de serviços enquadrada na sistemática cumulativa comum não pode descontar c
- Uma sociedade comercial com receita bruta de R$ 100.000 no lucro presumido, sem outras par
- Um imóvel rural com área total de 200 hectares pode ter alíquota de ITR distinta de outro 
- Em apuração relativa a outubro de 2026, a empresa deve desconsiderar integralmente PIS e C
- Uma pessoa jurídica com base tributável de IRPJ de R$ 200.000 em trimestre completo deverá
- O contribuinte de Cide-Combustíveis calcula necessariamente a base multiplicando o preço u
- Conforme orientação oficial, a regra da LC 224/2025 de acréscimo relativo dos coeficientes
- Na aplicação do princípio arm's length, uma empresa pode considerar suficiente, sem anális
- Na interpretação do conceito de insumo para PIS/Cofins não cumulativos, um item pode ser r
- No lucro presumido comercial, o acréscimo de 10% estabelecido pela LC 224/2025 significa a
- A jurisprudência constitucional sobre PIS/Cofins exige atenção à composição da base de cál
- Uma empresa optante pelo lucro presumido em 2026, com receitas submetidas a coeficientes e
- A Lei 14.596/2023 estabelece critérios de preços de transferência relevantes à base tanto 
- Na declaração do ITR, o valor das florestas e pastagens naturais deve ser sempre excluído 
- Um contribuinte do regime não cumulativo deve examinar se a compra de mercadoria para reve
- Uma empresa com operação tributável em dezembro de 2026 deve aplicar exclusivamente a CBS 
```

## Pedido 23: Cabeamento estruturado e fibras ópticas (`informatica.cabeamento-fibras-opticas`, 131 tópicos de edital)

```
Matéria: informatica.cabeamento-fibras-opticas (Cabeamento estruturado e fibras ópticas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Arquitetura do cabeamento estruturado e normas aplicáveis": 16
- "2. Categorias 3, 5, 5e, 6 e 6A e certificação de pares metálicos": 22
- "3. Fibras ópticas monomodo e multimodo: propriedades e aplicações": 23
- "4. Componentes ópticos, perdas, orçamento de potência e testes": 23
- "5. Projeto, certificação e solução de falhas em redes cabeadas": 16

Já existem (não repita):
- Um técnico precisa identificar o elemento de distribuição a partir do qual parte o cabeame
- Um projeto possui cabeamento horizontal permanente e patch cords nas pontas. Qual afirmaçã
- Um relatório de infraestrutura menciona cabeamento de categoria 3 conforme a classificação
- Uma instalação nova deve ser identificada como classe D/categoria 5e segundo a ABNT NBR 14
- Na especificação de um edifício, foi indicada categoria 6A para suportar aplicações de alt
- Ao selecionar um cabo categoria 6 para um trecho de rede interna, qual associação está tec
- Um inventário antigo chama dois cabos de categoria 5 e categoria 5e, ambos associados hist
- Uma instalação de quatro pares termina corretamente nos oito contatos e passa em um simple
- Um lote de fibra OM3 será instalado entre racks para aplicação Ethernet compatível. Qual d
- Um projeto necessita interligar edifícios a 8 km com aplicação 10 Gb/s e orçamento óptico 
- A equipe possui enlace 10GBASE-SR em OM3 otimizada para 850 nm, com perdas dentro dos limi
- Um enlace de 350 m deverá operar em 10GBASE-SR a 850 nm, com módulos compatíveis e perdas 
- Uma equipe confunde os módulos 10GBASE-SR, 10GBASE-LR e 10GBASE-ER. Qual associação de tec
- O segmento permanente entre um distribuidor e tomada mede 88 m; os dois patch cords totali
- Uma rede será atualizada para 10GBASE-T em um canal de 100 m, com equipamentos compatíveis
- Um certificador detecta alta reflexão de sinal em um enlace de pares metálicos junto à tom
- Um cabo óptico apresenta falha localizada após intervenção em uma caixa de emendas. A equi
- Um enlace óptico tem 8 km de fibra a 0,35 dB/km, dois pares de conexões que totalizam 1,0 
- Um canal foi montado com cabo categoria 6A e tomadas categoria 5e em ambas as pontas. O re
- Em instalação com muitos cabos categoria 6A paralelos, o ensaio mostra acoplamento excessi
- Uma equipe deseja manter 10GBASE-T em um canal de 100 m já instalado em categoria 6, sem v
- Uma planilha classifica cabo metálico U/UTP e S/FTP. Qual interpretação das letras corresp
- Um instalador tenta acoplar conector óptico APC diretamente a uma face UPC, por terem dime
- Um enlace horizontal possui 90 m de cabo fixo e 12 m de patch cords. O instalador afirma q
- Um projeto óptico tem orçamento admissível de 4 dB para perdas no caminho, mas o ensaio OL
- Uma instalação horizontal de categoria 5e apresenta wiremap correto e teste completo de de
- Em um edifício comercial, a tomada de telecomunicações na área de trabalho é elemento da i
- A categoria 6A é especificada até 500 MHz e, por isso, sua taxa máxima de transferência de
- O cabo OM1 é uma fibra multimodo historicamente associada ao núcleo/casca de 62,5/125 micr
- Uma aplicação 10GBASE-LR sobre fibra monomodo utiliza, em condições usuais, 850 nm e apres
- O orçamento de perda óptica deve incluir atenuação do comprimento de fibra e perdas das co
- Um cabo horizontal fixo mede 88 m e dois patch cords totalizam 15 m; no modelo usual, o ca
- Uma infraestrutura de categoria 6A equipada com conectores de categoria 5e deve ser avalia
- Para 10GBASE-T, qualquer enlace categoria 6 com 100 m possui a mesma garantia de alcance d
- Na fibra multimodo OM4, um enlace 10GBASE-SR a 850 nm de 350 m pode ficar dentro do alcanc
- Um enlace óptico de 6 km, com atenuação de 0,4 dB/km e conectores que somam 0,8 dB, possui
- A medição com fonte de luz e medidor de potência permite avaliar a perda de inserção fim a
- Em uma instalação de cobre, o defeito denominado split pair não pode ocorrer quando os con
- Um enlace de 8 km apresenta fibra a 0,35 dB/km, conexões com perda total de 1,0 dB e emend
- A aprovação de um ensaio OTDR em fibra óptica dispensa necessariamente verificar a perda d
- A conexão direta entre faces ópticas APC e UPC deve ser evitada, porque suas geometrias de
- Uma fibra monomodo identificada como OS2 deve ser considerada monomodo em qualquer comprim
- O meio óptico evita interferência eletromagnética na transmissão luminosa, mas um cabo de 
- Um enlace de 350 m em 10GBASE-SR a 850 nm tem seu alcance garantido por OM3, pois a referê
- Se um trecho horizontal possui 95 m de cabo fixo e mais 8 m de cordões removíveis, ele sup
```

## Pedido 24: Engenharia química ambiental: composição e gases, umidade, balanços de massa e energia, operações unitárias de separação, cinética e reatores (`engenharia-quimica.balancos-operacoes-reatores`, 131 tópicos de edital)

```
Matéria: engenharia-quimica.balancos-operacoes-reatores (Engenharia química ambiental: composição e gases, umidade, balanços de massa e energia, operações unitárias de separação, cinética e reatores)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Composição, gases e umidade": 25
- "2. Balanços de massa e energia": 25
- "3. Operações unitárias de separação": 25
- "4. Cinética e reatores": 25

Já existem (não repita):
- Para uma mistura de gases ideais, a fração molar de cada componente é igual à sua fração v
- Um efluente gasoso contém 0,05% em volume de SO₂. Essa concentração corresponde a:
- Nas condições normais de temperatura e pressão (0 °C e 1 atm), o volume ocupado por 2 mol 
- Em uma mistura gasosa a 1 atm, com 21% em mols de oxigênio, a pressão parcial do oxigênio 
- Ar a 30 °C tem pressão parcial de vapor de água igual a 2,1 kPa, e a pressão de vapor da á
- A umidade absoluta do ar é expressa como a razão entre a pressão parcial do vapor de água 
- O ponto de orvalho de uma mistura gás-vapor é a temperatura na qual:
- Ao informar a vazão de um efluente gasoso de chaminé, é indispensável indicar as condições
- Em um processo contínuo operando em regime permanente, o termo de acúmulo da equação geral
- Em processos com reação química, conserva-se sempre:
- Na combustão CH₄ + 2 O₂ → CO₂ + 2 H₂O, um queimador recebe 1 mol de metano e 3 mol de oxig
- Em um processo com reator e reciclo do reagente não convertido, a conversão global, calcul
- Em um processo com reciclo, a corrente de purga tem a função de:
- Em um reator, formaram-se 80 mol do produto desejado e 20 mol de um subproduto indesejado.
- Para um sistema aberto em regime permanente, desprezando variações de energia cinética e p
- Um tanque recebe 100 kg/h de solução com 20% em massa de sal e evapora água até que a solu
- Na destilação flash, a alimentação parcialmente vaporizada é separada em um único estágio 
- Em uma coluna de destilação binária, a operação com razão de refluxo mínima exigiria:
- Uma mistura azeotrópica, cuja volatilidade relativa é igual a 1 na composição do azeótropo
- Um lavador de gases usa água para remover amônia de um efluente gasoso. A operação e as co
- O esgotamento (stripping) com ar é usado para remover compostos voláteis, como amônia ou s
- Segundo a lei de Henry, para gases pouco solúveis em equilíbrio com um líquido, a pressão 
- Na extração líquido-líquido, as correntes de saída que contêm, respectivamente, o solvente
- A extração líquido-líquido é preferível à destilação quando:
- Para remover material particulado muito fino de gases de exaustão com alta eficiência, os 
- O ciclone separa partículas de uma corrente gasosa pela ação da força centrífuga, sendo ef
- Segundo a lei de Stokes, a velocidade de sedimentação de uma partícula em um líquido aumen
- Em um hidrociclone usado na classificação de polpas minerais, as partículas grossas e dens
- Em uma coluna de destilação, a seção situada acima do prato de alimentação é chamada de se
- Na filtração, o material sólido retido sobre o meio filtrante, que passa ele próprio a atu
- Lavadores do tipo Venturi removem partículas e gases solúveis, mas geram um efluente líqui
- Pela lei de Arrhenius, a constante de velocidade de uma reação aumenta exponencialmente co
- No gráfico de ln k em função de 1/T para uma reação que segue a lei de Arrhenius, obtém-se
- Um reator contínuo de 2 m³ recebe vazão volumétrica de 0,5 m³/h. O tempo espacial e a velo
- No reator de mistura perfeita (CSTR), a composição é uniforme em todo o volume e igual à c
- No reator em batelada:
- Para uma reação de primeira ordem com k = 0,1 min⁻¹, o tempo espacial necessário em um CST
- Para reações de ordem positiva, um reator pistonado (PFR) exige volume maior que um CSTR p
- No projeto de um reator de leito recheado (PBR) com catalisador sólido, a equação de proje
- A associação de vários CSTRs em série, com o mesmo volume total, tende a aproximar o desem
```

## Pedido 25: Segurança institucional e de dignitários (`seguranca-institucional.seguranca-judiciaria-dignitarios`, 131 tópicos de edital)

```
Matéria: seguranca-institucional.seguranca-judiciaria-dignitarios (Segurança institucional e de dignitários)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, governança e proteção de instalações": 19
- "2. Segurança de dignitários, deslocamentos e direção defensiva": 20
- "3. Emergências, incêndios e planos de evacuação": 18
- "4. Contingências, perímetros e organização do posto de comando": 17
- "5. Gerenciamento de crises, negociação e defesa pessoal responsável": 26

Já existem (não repita):
- Uma instituição quer proteger usuários, registros, instalações e continuidade das atividad
- Uma equipe identifica entrada lateral sem controle, possibilidade de ingresso não autoriza
- Um pedido de proteção especial é apresentado nos termos do art. 13, III, da Resolução CNJ 
- Um visitante possui crachá válido para atendimento público, mas tenta entrar em arquivo re
- Antes de iniciar deslocamento de serviço, o condutor encontra problema em equipamento obri
- Um motorista transporta pessoas em veículo institucional e recebe mensagens no celular. Qu
- Durante chuva forte, um automóvel transporta equipe e precisa seguir outro veículo. Segund
- Ao aproximar-se de cruzamento com movimento de pedestres, o veículo de transporte instituc
- Uma equipe prepara instruções sobre alarme de incêndio e manutenção de rotas de saída em u
- Em uma sala técnica, princípio de incêndio alcança equipamento elétrico ainda energizado. 
- Um automóvel particular transporta autoridade para compromisso urgente, mas não se enquadr
- Para impedir acessos indevidos, a administração cogita instalar fechadura que dificulte a 
- Uma instalação recebe público e pessoas com mobilidade reduzida. Qual conjunto de providên
- Durante manutenção, um recipiente com líquido inflamável inicia combustão em área técnica.
- Uma ocorrência de grande porte reúne equipes com competências diferentes e relatos conflit
- Após vazamento de fumaça, uma área antes considerada segura passa a receber produtos da co
- Durante conversa para acalmar pessoa exaltada, o interlocutor cogita prometer providência 
- Em incidente de resposta conjunta, duas instituições com competências próprias precisam co
- Um pedido de escolta é apresentado com informações contraditórias sobre ameaça e proposta 
- Em deslocamento institucional, a equipe presume que o art. 29, VII, do CTB autoriza qualqu
- Durante preparação para simulado de incêndio, uma equipe deseja determinar número universa
- A avaliação posterior de uma crise identifica que o canal único de comunicação falhou dura
- Durante perturbação no atendimento, uma pessoa profere insultos, mas não realiza nem ameaç
- Uma área de isolamento de emergência foi ampliada para evitar exposição de pessoas, mas a 
- Uma unidade pretende elaborar plano integrado de segurança que inclua proteção de pessoas,
- Uma política de proteção que reúne segurança de pessoas, instalações, documentos e continu
- Um motorista deixa de utilizar cinto de segurança durante transporte de uma pessoa protegi
- Ao identificar equipamento elétrico energizado em chamas, uma equipe classifica inicialmen
- Um plano de abandono define apenas quem primeiro deixa o prédio e dispensa ponto de encont
- No gerenciamento de emergência, um posto de comando reúne coordenação de objetivos, inform
- Na gestão de acesso, uma autorização genérica de entrada e um crachá de visitante permitem
- No procedimento de proteção pessoal, a avaliação de risco pode justificar adaptações nos m
- Um condutor aproxima-se de cruzamento sob chuva e mantém velocidade alta porque considera 
- Em exercício de incêndio, a instituição mantém as rotas de abandono sinalizadas e sem obst
- Uma equipe de emergência mantém perímetro imutável apesar de mudança de vento e propagação
- O comando unificado pode organizar objetivos comuns em incidente com múltiplas instituiçõe
- Para reduzir tensão, uma pessoa responsável por diálogo promete providência que sabe não p
- Em uma instalação submetida à Resolução CNJ nº 435/2021, as comissões permanentes de segur
- Uma pessoa considerada dignitária utiliza automóvel particular comum. Apenas por transport
- Um exercício identifica obstrução de saída e ausência de informação compreensível sobre o 
- Na organização de incidente, um plano que registra comando unificado permite que cada inte
- Em caso de agressão injusta atual contra terceiro, a proteção defensiva, quando juridicame
- Durante incêndio envolvendo óleo de cozinha quente, lançar água diretamente sobre a substâ
- Após falha de energia que comprometeu o canal principal de avisos, uma revisão de contingê
- Uma pessoa faz reclamação verbal exaltada sem agressão injusta atual ou iminente; usar for
```

## Pedido 26: Persistência com JPA e Hibernate (`informatica.persistencia-jpa-hibernate-bancos`, 130 tópicos de edital)

```
Matéria: informatica.persistencia-jpa-hibernate-bancos (Persistência com JPA e Hibernate)
Capítulos e quantas questões quero em cada um (total 100):
- "1. JPA, Hibernate e ciclo de vida das entidades": 28
- "2. Mapeamento de relações, consultas e desempenho": 13
- "3. Hibernate Envers: auditoria histórica e revisões": 30
- "4. Flyway e evolução controlada do esquema": 13
- "5. PostgreSQL, H2, Oracle, SQL Server e autenticação SSO": 16

Já existem (não repita):
- Uma equipe Java precisa adotar padrão de persistência sem se vincular diretamente a API es
- Um sistema permanece na linha Hibernate ORM 4.3 e pretende utilizar o padrão JPA correspon
- Durante uma transação, um programador chama `persist` passando uma entidade nova que possu
- Em mapeamento bidirecional, `Pedido` contém `cliente`, propriedade associada à coluna `cli
- Após configurar histórico de alterações de `Cliente`, a aplicação precisa indicar ao Hiber
- Uma equipe mantém os scripts `V1__criar_cliente.sql` e `V2__adicionar_email.sql`, já aplic
- Para testes rápidos, uma aplicação Java pretende abrir banco H2 que reside na memória dura
- Uma consulta precisa filtrar entidades `Pedido` pelo atributo Java `status`, utilizando JP
- Uma aplicação quer impedir atualizações perdidas quando duas transações alteram a mesma en
- Uma conta local autenticada por provedor central é salva no PostgreSQL; a aplicação precis
- Um objeto `Cliente` foi carregado e saiu do contexto após fechamento do EntityManager. O d
- Em uma transação, alterações de duas entidades estão pendentes. O código chama `flush`, ma
- Um relatório consulta 200 pedidos e, ao mostrar o nome de cada cliente associado, dispara 
- Uma entidade de cadastro tem `@ManyToOne` sem `fetch` expresso e uma coleção `@OneToMany` 
- Após gravar revisões auditadas, um desenvolvedor quer recuperar o estado de `Cliente` na r
- Uma equipe executou `V2__criar_indice.sql` em produção e depois modificou seu conteúdo no 
- Uma instituição pretende começar a usar Flyway em banco Oracle já povoado com tabelas hist
- Em mapeamento de pedidos e clientes, a equipe colocou `cascade=REMOVE` em `Pedido.cliente`
- Duas transações obtêm entidade versionada com `@Version=4`. A primeira atualiza e confirma
- Uma aplicação Envers registra várias alterações auditadas na mesma transação e usa `Validi
- Uma empresa executa atualização de registros por SQL nativo externo ao Hibernate e espera 
- Uma aplicação usa scripts `V7__tabela.sql` e `R__visao.sql`. O script R é modificado após 
- Um projeto mapeia `@GeneratedValue(strategy=IDENTITY)` e tem obrigação de suportar Oracle 
- Testes passam no H2 com `MODE=PostgreSQL`, mas DDL de produção falha no PostgreSQL real. C
- Uma aplicação com SSO recebe um ID Token OIDC e pretende usar esse conteúdo como prova de 
- Uma classe Java marcada com `@Entity` e dotada de identificador `@Id` pode participar do m
- A série Hibernate 4.3 implementa diretamente Jakarta Persistence 3.2 e exige que todas as 
- O contexto de persistência mantém identidade de entidades gerenciadas, de modo que consult
- A chamada `EntityManager.clear()` exclui definitivamente todas as linhas correspondentes à
- O Envers permite marcar uma entidade com `@Audited` e consultar informações históricas rel
- O comando `flyway validate` executa todas as migrações pendentes para comparar a nova estr
- Em associação bidirecional, `mappedBy="cliente"` na coleção de pedidos indica que a propri
- A associação `@ManyToOne` da JPA tem, como padrão de carregamento, `LAZY`, assim como `@On
- Em JPQL, `select p from Pedido p where p.status = :status` expressa consulta por entidade 
- Uma consulta JPQL que utiliza `JOIN` sobre associação carrega necessariamente todo o relac
- No Envers, o número de revisão identifica alterações auditadas dentro de uma transação, nã
- Como uma migração `R__` possui descrição sem número de versão, o Flyway a executa uma vez 
- Em banco preexistente que ainda não usa Flyway, o recurso `baseline` permite registrar ver
- Um objeto detached permanece automaticamente monitorado pelo `EntityManager` já encerrado;
- Na estratégia ValidityAuditStrategy do Envers, as revisões de início e fim permitem defini
- O cache de segundo nível do Hibernate é necessariamente idêntico ao cache de primeiro níve
- O PostgreSQL utiliza controle multiversão para isolamento transacional, mas isso não exclu
- O modo `MODE=Oracle` do H2 assegura que consultas e migrações criadas em H2 se comportarão
- O OIDC acrescenta uma camada de autenticação ao OAuth 2.0 e utiliza um ID Token, de modo q
- No SQL Server, a propriedade `IDENTITY(1,1)` já equivale, por si só, a uma restrição PRIMA
```

## Pedido 27: Direito econômico: fundamentos, intervenção do Estado e ordem econômica (`direito-economico.fundamentos-intervencao-ordem-economica`, 129 tópicos de edital)

```
Matéria: direito-economico.fundamentos-intervencao-ordem-economica (Direito econômico: fundamentos, intervenção do Estado e ordem econômica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, evolução, sistemas e escolas do direito econômico": 32
- "2. Economia comportamental, escolhas e regulação baseada em evidências": 13
- "3. Intervenção estatal: regulação, exploração econômica e recursos naturais": 21
- "4. Liberdade econômica, parcerias e disciplina da concorrência": 15
- "5. Ordem econômica internacional, integração e defesa comercial": 19

Já existem (não repita):
- Uma norma organiza a atividade de operadores privados e estabelece padrões de concorrência
- Ao preparar uma análise sobre a ordem econômica brasileira, um estudante deve identificar 
- Um pesquisador compara escolas de análise do poder econômico. Qual associação entre tradiç
- Uma campanha pública passa a apresentar, como opção inicial alterável, o recebimento de av
- Uma organização pública divulga um plano de investimentos setoriais e afirma que as metas 
- Uma lei autoriza a criação de uma sociedade de economia mista dedicada a atividade autoriz
- O dono de uma fazenda descobre jazida mineral em seu terreno e afirma que a titularidade d
- Um empreendimento preenche as condições legais de atividade econômica de baixo risco. Qual
- Dois países retiram tarifas do comércio recíproco, mas continuam a aplicar tarifas distint
- Uma proposta de fiscalização adota metas de eficiência, mas a autoridade ainda não editou 
- Ao avaliar um projeto de norma geral sobre tarifas, o responsável deseja comparar previame
- Um consumidor superestima a probabilidade de acidente após acompanhar diversas reportagens
- Uma empresa pública presta serviço público sob regime legal próprio e está submetida ao es
- Uma empresa nacional afirma que terá direito a medida antidumping porque os produtos impor
- Uma companhia conquistou parcela expressiva do mercado porque inovou e reduziu custos, sem
- A inclusão de projeto de transporte em programa de parcerias foi anunciada como transferên
- Um país importador investiga aumento expressivo de quantidade de produtos importados, prej
- Um parecer sustenta que uma empresa com mais de 20% do mercado relevante praticou necessar
- Uma proposta de norma federal geral apresenta urgência demonstrável. A equipe entende que 
- Uma estatal que atua em mercado concorrencial pretende receber privilégio tributário vedad
- No debate sobre criação de uma contribuição voltada à intervenção econômica, propõe-se que
- Uma negociação proposta na década de 1990 pretendia criar área de livre comércio envolvend
- Um Estado busca apoiar importações que favoreçam consumidores e, simultaneamente, pretende
- Um órgão deseja preservar opções de usuários, mas anuncia penalidade relevante para quem n
- Uma região elimina grande parte das tarifas internas, cria tarifa externa comum com exceçõ
- Uma cooperativa questionou se a livre iniciativa exclui a função social da propriedade. Se
- Uma autoridade pretende orientar o mercado por incentivos e planejamento. Conforme o art. 
- Uma iniciativa de política pública altera a disposição visual de duas opções, mantendo as 
- O empreendedor de uma atividade de baixo risco dispensada de licença prévia concluiu que t
- Dois países retiraram barreiras tarifárias comerciais recíprocas sem adotar tarifa comum n
- A importação de produtos por valor inferior ao preço cobrado pelo produtor doméstico demon
- Uma comissão avalia as normas aplicáveis a empresa pública e sociedade de economia mista. 
- Um estudo de mercado aplica a escola de Chicago como regra jurídica vinculante, concluindo
- Um plano federal prevê concessões de infraestrutura no âmbito do PPI. A inclusão de projet
- O proprietário do terreno explorado por uma concessionária mineral pode exigir a proprieda
- Uma equipe usa o resultado de consulta pública para comparar, antes da edição de uma norma
- Diante de importações crescentes que causaram prejuízo grave, a indústria só poderá buscar
- Um grupo econômico possui participação elevada em mercado, mas a investigação ainda não id
- A administração pública central decidiu criar contribuição de intervenção no domínio econô
- Durante investigação comercial, verificou-se dano causado por produto estrangeiro subsidia
- A instituição de uma tarifa externa comum entre países integrantes é elemento suficiente p
- Uma lei federal autoriza uma empresa estatal voltada a relevante interesse coletivo; para 
- A criação de opção padrão num formulário, com saída difícil e ocultação de informações ess
- Após investigação, foi sugerida aplicação cumulativa de direito antidumping e medida compe
- Um importador declarou falsamente o conteúdo de mercadoria estrangeira em documento aduane
```

## Pedido 28: Vigilância ambiental e controle de vetores (`saude-publica.vigilancia-ambiental-zoonoses`, 129 tópicos de edital)

```
Matéria: saude-publica.vigilancia-ambiental-zoonoses (Vigilância ambiental e controle de vetores)
Capítulos e quantas questões quero em cada um (total 100):
- "Vigilância ambiental em saúde e gestão territorial": 17
- "Aedes e manejo integrado de vetores": 17
- "Flebotomíneos e triatomíneos": 17
- "Roedores e animais peçonhentos": 17
- "Agente de Combate às Endemias e trabalho de campo": 16
- "Qualidade da água para consumo humano: Vigiagua e Sisagua": 16

Já existem (não repita):
- Em um bairro onde uma indústria lança contaminantes próximos a poços, qual ação representa
- Durante inspeção, identifica-se tambor com substância tóxica e moradias abastecidas por ág
- O descarte irregular de resíduos cria recipientes com água, alimento para ratos e abrigo p
- No ciclo do Aedes aegypti, quais fases são necessariamente aquáticas?
- Um levantamento pesquisou 200 imóveis e detectou formas imaturas de Aedes em 20 deles; o I
- Em 150 imóveis pesquisados foram encontrados 30 recipientes positivos para Aedes; qual int
- Uma equipe instala ovitrampas em pontos definidos da cidade; a informação obtida está mais
- Durante alta infestação por Aedes, uma gestão anuncia que fará apenas nebulização espacial
- Qual intervenção é exemplo de controle biológico de Aedes, quando tecnicamente indicada?
- Em visita, o agente encontra imóvel fechado em área com focos de Aedes; qual procedimento 
- Ao planejar controle de flebotomíneos em área com leishmaniose, qual condição ambiental me
- No ciclo urbano da leishmaniose visceral, o cão e o flebotomíneo cumprem papéis diferentes
- Qual é o mecanismo clássico de transmissão vetorial de Trypanosoma cruzi por triatomíneos?
- Uma moradora encontrou inseto semelhante a barbeiro dentro do quarto; qual orientação é ad
- Numa ocupação urbana com presença de ratos, qual conjunto reúne medidas básicas de manejo 
- Qual é a orientação adequada sobre uso de raticidas no controle de roedores de interesse s
- Em conjunto habitacional com escorpiões, qual medida estrutural contribui para reduzir enc
- Ao ocorrer picada de serpente em trabalhador rural, qual primeira orientação comunitária é
- A Lei nº 11.350/2006 atribui ao ACE, em termos gerais, qual núcleo de atividades?
- Qual exemplo demonstra cooperação adequada entre ACS e ACE no território?
- Um ACE é escalado para aplicar produto químico sem treinamento ou proteção compatível; qua
- Quanto à qualidade da água distribuída à população, qual distinção de responsabilidade é c
- Uma equipe consulta dados do Sisagua; qual associação dos módulos está correta?
- Uma amostra de água de poço apresenta aparência límpida, mas há suspeita de contaminação p
- A vigilância em saúde ambiental envolve reconhecer exposições a fatores ambientais potenci
- A presença de uma fonte química perigosa e a comprovação de intoxicação de uma pessoa são 
- Em áreas com enchentes recorrentes, planejamento sobre água segura, resíduos e abrigo de a
- A eliminação de recipientes com água é medida relevante contra Aedes, porque larvas e pupa
- O Índice de Breteau representa diretamente o percentual de habitantes de uma área que adoe
- A existência de ovos do Aedes que resistem a períodos desfavoráveis reforça a necessidade 
- O uso de nebulização espacial substitui todas as medidas de remoção de criadouros, pois de
- Na comparação entre Aedes e flebotomíneos, a limpeza de substratos terrestres úmidos com m
- Em qualquer município onde se observe um único flebotomíneo, é obrigatória a pulverização 
- O triatomíneo é conhecido popularmente como barbeiro e sua presença no domicílio deve ser 
- No mecanismo vetorial clássico da doença de Chagas, o Trypanosoma cruzi é necessariamente 
- Uma ação de melhoria habitacional que vede frestas e remova abrigos pode integrar o contro
- O manejo integrado de roedores considera alimento, água e abrigos no ambiente e, por isso,
- Para reduzir infestação de ratos, recomenda-se que qualquer morador faça misturas de ratic
- No controle de escorpiões, reduzir baratas, entulhos, frestas e outros abrigos pode ser ma
- O uso de botas e luvas apropriadas ao mover entulhos pode reduzir exposição a serpentes, a
- Por ser profissional de saúde, o ACE tem autorização geral para prescrever soro antiofídic
- Uma visita registrada como realizada, mesmo tendo sido impossível entrar no imóvel, consti
- O Sisagua possui informações de cadastro das formas de abastecimento, de controle dos resp
- Na qualidade da água, a vigilância da autoridade de saúde elimina a obrigação do prestador
- A avaliação de água após suspeita de contaminação química não pode basear-se apenas na tra
```

## Pedido 29: Princípios e fontes do Direito do Trabalho (`direito-trabalho.principios-fontes`, 129 tópicos de edital)

```
Matéria: direito-trabalho.principios-fontes (Princípios e fontes do Direito do Trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, formação histórica e fontes do Direito do Trabalho": 20
- "2. Hierarquia, sujeitos, relação de emprego e aplicação normativa": 19
- "3. Proteção, primazia da realidade e continuidade da relação de emprego": 22
- "4. Indisponibilidade, renúncia, transação, quitação e alteração lesiva": 21
- "5. Negociação coletiva, arts. 611-A e 611-B e limites da prevalência": 18

Já existem (não repita):
- Após paralisações e debates sobre novas formas de prestação laboral, foi promulgada lei pa
- Uma controvérsia não tem disciplina legal ou contratual específica, e o intérprete conside
- Uma associação sem fins lucrativos contrata uma pessoa para serviços pessoais, contínuos, 
- Uma pessoa presta serviços como profissional independente, organizando clientes e horários
- Um instrumento denomina o prestador 'consultor autônomo', mas registros e testemunhos most
- Uma empresa propõe reduzir vantagem individual regularmente contratada e prejudicar econom
- Uma empresa firmou ACT e sua categoria possui CCT vigente com cláusula distinta sobre assu
- Uma pessoa simplesmente assina documento desistindo de receber direito salarial assegurado
- Uma empregada com diploma superior recebe salário mensal equivalente a 1,7 vez o teto prev
- Na vigência do emprego, as partes querem firmar termo de quitação anual de obrigações mens
- Um acordo coletivo disciplina validamente tema relativamente disponível. Um intérprete sus
- Uma negociação coletiva pactua banco de horas anual, dentro das demais condições jurídicas
- Uma CCT estabelece que o décimo terceiro salário terá valor nominal inferior ao legal para
- A empresa afirma que uma súmula pode criar nova obrigação trabalhista ainda que inexista s
- Uma controvérsia envolve interpretação possível de cláusula ambígua, e outra envolve saber
- Duas entidades sindicais de categorias profissional e econômica celebram instrumento geral
- Uma negociação coletiva válida restringiu direito relativamente disponível, mas a cláusula
- Uma pessoa possui diploma superior, recebe salário mensal de 2,3 vezes o teto do RGPS e pa
- Um ACT pretende reduzir o intervalo intrajornada de trabalhadores com jornada superior a s
- ACT prevê adicional noturno superior ao diurno, mas inferior ao exigido legalmente; CCT es
- Uma controvérsia envolve alegação empresarial de que o contrato terminou espontaneamente, 
- O empregador determina a reversão de empregado da função de confiança ao cargo efetivo ant
- Uma negociação coletiva dispõe sobre redução temporária do salário e da jornada. A equipe 
- A categoria firma acordo para pagar horas extras com acréscimo de 35%, e a empresa sustent
- Ao examinar ACT regularmente celebrado, discute-se o controle judicial de seus elementos e
- Uma mobilização social por melhorias nas condições de trabalho constitui exemplo de fonte 
- Por ser o Direito do Trabalho um ramo autônomo, o art. 8º, § 1º, da CLT impede a utilizaçã
- Uma instituição beneficente que admite trabalhadores pessoais, remunerados, subordinados e
- A remuneração basta para caracterizar a relação de emprego, mesmo quando o prestador é jur
- Se registros e provas demonstram subordinação e pessoalidade diferentes da descrição de um
- O princípio da inalterabilidade contratual lesiva permite reduzir uma vantagem do empregad
- O reconhecimento das convenções e acordos coletivos de trabalho aparece expressamente na C
- Para beneficiar-se do art. 444, parágrafo único, basta ao empregado possuir diploma superi
- Quando partes compõem divergência verdadeira sobre direito patrimonial disponível mediante
- O termo de quitação anual do art. 507-B da CLT, firmado perante o sindicato profissional, 
- Em conflito entre condições de ACT e CCT que alcançam a mesma relação, o art. 620 da CLT a
- Uma convenção coletiva pode reduzir o valor dos depósitos mensais de FGTS desde que regist
- A orientação in dubio pro operario é tradicionalmente associada à escolha entre interpreta
- Ao enfrentar lacuna contratual e legal, o intérprete está proibido de empregar analogia ou
- Segundo a redação atual dos §§ 1º e 2º do art. 468 da CLT, a reversão de função de confian
- A tese do Tema 1.046 permite afastar por acordo coletivo direitos absolutamente indisponív
- Para os fins previstos nos arts. 611-A e 611-B da CLT, seu art. 611-B, parágrafo único, de
- A declaração individual de renúncia a férias mínimas legais transforma-se em transação vál
- A Constituição, art. 7º, VI, prevê irredutibilidade salarial com ressalva para convenção o
- Por proteger o empregado, o princípio da norma mais favorável torna inaplicável o art. 620
```

## Pedido 30: Crimes contra a dignidade sexual (`direito-penal.crimes-dignidade-sexual`, 129 tópicos de edital)

```
Matéria: direito-penal.crimes-dignidade-sexual (Crimes contra a dignidade sexual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Proteção da dignidade sexual e estupro do art. 213": 21
- "2. Estupro de vulnerável: art. 217-A e legislação de 2025–2026": 21
- "3. Importunação sexual e assédio sexual: arts. 215-A e 216-A": 19
- "4. Registro não autorizado da intimidade sexual: art. 216-B": 18
- "5. Comparações, penas, causas de aumento e casos integradores": 21

Já existem (não repita):
- Uma pessoa adulta é constrangida, mediante ameaça grave e concreta, a praticar ato libidin
- Após a reforma do Título VI pela Lei nº 12.015/2009, um estudante afirma que o estupro exi
- Um caso hipotético descreve estupro do art. 213 sem lesão grave ou morte e sem circunstânc
- Uma pessoa de 13 anos é submetida a ato libidinoso por adulto. O enunciado menciona aparen
- Qual das situações seguintes descreve a vulnerabilidade prevista no art. 217-A, § 1º, sem 
- Um adulto pratica contra outro adulto, sem sua anuência, ato libidinoso destinado à satisf
- Um gerente constrange uma pessoa subordinada para obter favorecimento sexual, utilizando s
- Duas pessoas adultas participam de momento íntimo consensual, mas uma delas registra cland
- Em outubro de 2026, uma questão cobra a pena básica do art. 217-A após a Lei nº 15.280/202
- Uma pessoa adulta encontra-se efetivamente inconsciente e impossibilitada de oferecer resi
- Uma avaliação pede a pena do estupro do art. 213 quando da conduta resulta lesão corporal 
- Uma pessoa em posição superior constrange subordinada de 17 anos para obter favorecimento 
- Alguém produz montagem digital para inserir outra pessoa em cena sexual de caráter íntimo,
- Um adulto usa violência física para impor a prática de ato libidinoso a outra pessoa adult
- Um superior hierárquico constrange subordinado para obter favorecimento sexual, mas a víti
- Uma questão compara a pena básica do art. 216-B com a pena da importunação sexual. Qual al
- A Lei nº 15.353/2026 modificou o art. 217-A. Qual afirmação traduz precisamente a nova reg
- Um homem de 30 anos pratica ato libidinoso com pessoa de 13 anos; a defesa invoca que o co
- Na hipótese de estupro de vulnerável em que da conduta resulta morte, qual pena está estab
- Um caso de estupro envolve dois agentes atuando juntos e a questão exige identificar a maj
- Em estudo da dosimetria dos crimes contra a dignidade sexual, qual aumento está previsto p
- Um material de estudo afirma que o art. 225 determina ação penal pública incondicionada pa
- Uma pessoa adulta sob efeito de substância está sem capacidade de reação e, por essa condi
- Uma pessoa adulta participa voluntariamente de encontro íntimo, mas não autoriza filmagem;
- Ao revisar os cinco tipos do recorte, o estudante procura o único que exige expressamente 
- Uma pessoa adulta, não enquadrada nas hipóteses de vulnerabilidade legal, é constrangida m
- Em sua redação vigente, o art. 213 exige conjunção carnal, pois a prática de outro ato lib
- A Lei nº 12.015/2009 alterou a denominação do Título VI do Código Penal, que passou a refe
- No art. 217-A, o consentimento manifestado por pessoa de 13 anos torna a vulnerabilidade r
- O art. 215-A prevê importunação sexual para ato libidinoso praticado contra alguém sem sua
- O simples fato de um comportamento sexual impróprio ocorrer no local de trabalho é suficie
- Gravar clandestinamente cena íntima e privada sem autorização dos participantes pode confi
- O art. 217-A, § 1º, considera automaticamente incapaz de discernir toda pessoa com qualque
- Com a Lei nº 15.280/2025, a pena básica do estupro de vulnerável passou a reclusão de 10 a
- Para o art. 216-A, é indispensável que o agente obtenha efetivamente a vantagem sexual pre
- O parágrafo único do art. 216-B estabelece punição para montagem em registro visual, sonor
- Segundo o art. 217-A, § 5º, na redação de 2026, a gravidez resultante do crime é condição 
- Uma pessoa adulta efetivamente incapaz de oferecer resistência por outra causa pode estar 
- Pelo Tema Repetitivo 1.121 do STJ, ato libidinoso típico com menor de 14 anos e finalidade
- A Lei nº 15.353/2026 acrescentou o § 4º-A ao art. 217-A para afirmar a natureza absoluta d
- Com a atualização legislativa de 2025, o art. 217-A, § 3º, fixou reclusão de 20 a 40 anos 
- No art. 216-B, o consentimento para participar de encontro íntimo não equivale automaticam
- O art. 226, inciso IV, alínea 'a', prevê aumento de um quarto no estupro coletivo e afasta
- O art. 234-A, III, estabelece aumento de metade a dois terços quando resulta gravidez de c
- A jurisprudência do STJ exige contato físico direto entre agente e vítima para toda hipóte
```

## Pedido 31: Mercado de capitais (`conhecimentos-bancarios.mercado-capitais`, 129 tópicos de edital)

```
Matéria: conhecimentos-bancarios.mercado-capitais (Mercado de capitais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Mercado de capitais, CVM, companhias e ofertas públicas": 22
- "2. Ações, mercado à vista, bolsa, balcão e ouro": 25
- "3. Debêntures, notas comerciais, fundos e distribuição": 25
- "4. Mercado a termo, opções e os fundamentos dos derivativos": 12
- "5. Mercados futuros, swap, garantias e integração dos instrumentos": 16

Já existem (não repita):
- Uma companhia emite novas ações para financiar a construção de sua fábrica, e investidores
- Uma pessoa compra ações de outro investidor em pregão regular. Qual afirmação descreve cor
- Uma sociedade anônima deseja ter suas ações admitidas à negociação em ambiente organizado,
- Uma companhia pretende captar recursos com ações e contrata intermediário para colocação s
- Uma investidora adquire título de dívida emitido por sociedade por ações, com pagamentos p
- Um investidor deseja comprar 25 ações de papel cujo lote padrão é 100 unidades, por interm
- Uma pessoa compra call sobre ações e paga prêmio à contraparte. Qual faculdade principal a
- Uma empresa comercializa proteção contra queda de preço por meio de opção, permitindo ao t
- Uma posição em futuro de commodity sofre variação adversa antes do vencimento e precisa li
- Uma distribuição pública prevê que o coordenador procurará compradores, sem assumir obriga
- Uma oferta de ações combina R$ 8 milhões de emissão nova com R$ 2 milhões de ações antigas
- Um instrumento de dívida de livre negociação é emitido exclusivamente de forma escritural 
- Uma pessoa investe em cotas de fundo aberto e conclui que poderá sacar o valor patrimonial
- Um sistema de negociação de valores mobiliários é administrado por entidade autorizada, co
- Um investidor tem ouro qualificado como ativo financeiro adquirido por instituição habilit
- Um produtor de café compromete-se hoje a vender cem sacas por R$ 220 cada daqui a sessenta
- Uma empresa deve pagar dívida pós-fixada e contrata operação para pagar remuneração fixa e
- Uma investidora adquire 100 calls com strike de R$ 30 e prêmio de R$ 2 por ação. No vencim
- Uma empresa entra comprada em um futuro com multiplicador de 100 unidades. O preço de ajus
- Um intermediário aceita distribuir uma emissão durante prazo previsto e assumir a compra d
- Uma debênture oferece possibilidade de conversão em ações da própria emissora, observadas 
- Uma investidora compra put de strike R$ 50, pagando prêmio de R$ 3 por unidade. No vencime
- Uma importadora terá de pagar em dólares por mercadorias futuras e teme alta da moeda. Qua
- Uma pessoa adquire 30 ações à vista de determinado papel e considera que a mera negociação
- Um investidor toma posição lançadora descoberta de call, recebendo prêmio, e prevê ganho l
- Uma sociedade emite ações novas e recebe valores de investidores subscritores. Essa distri
- O registro de uma distribuição pública de valores mobiliários pela CVM assegura ao investi
- Uma ação ordinária costuma conferir direito de voto, enquanto ações preferenciais podem co
- Ao adquirir debênture simples emitida por companhia, o investidor ingressa no quadro de ac
- Uma pessoa paga prêmio para ser titular de opção de venda. Esse pagamento adquire o direit
- Em contratos futuros padronizados, o depósito inicial de margem de garantia equivale ao pr
- Uma sociedade limitada pode emitir nota comercial escritural não conversível em ações, nos
- O investidor que solicita compra de ações em mercado à vista por meio de ordem limitada te
- Na oferta primária, a companhia recebe recursos da subscrição de ações novas; na oferta se
- O investidor de cotas de um fundo de ações torna-se proprietário direto de todas as ações 
- No balcão organizado, existe entidade administradora de mercado autorizado e regras própri
- Como notas comerciais foram historicamente conhecidas como commercial papers de curto praz
- O produtor que deverá vender sua safra e teme queda no preço pode assumir posição vendedor
- Em contrato a termo tradicional, as partes fixam hoje preço de entrega futura. Esse modelo
- Uma posição compradora em futuro com multiplicador de 200 unidades obtém ganho diário simp
- O titular de call comprada que pagou prêmio de R$ 4 por unidade e tem strike de R$ 40 ganh
- Um swap para pagar taxa fixa e receber indexador variável pode modificar a exposição finan
- No underwriting de melhores esforços, o intermediário é obrigado a adquirir integralmente 
- Nas condições divulgadas pela Receita Federal para pessoa física, o limite mensal de R$ 20
- Uma classe de fundo de investimento com cotas negociadas em bolsa garante ao cotista resga
```

## Pedido 32: Infectologia clínica e antimicrobianos (`medicina.infectologia`, 129 tópicos de edital)

```
Matéria: medicina.infectologia (Infectologia clínica e antimicrobianos)
Capítulos e quantas questões quero em cada um (total 100):
- "Classes de antimicrobianos, mecanismos e espectros": 17
- "Uso racional, microbiologia clínica e resistência": 17
- "Infecções associadas à assistência e profilaxias": 17
- "Sepse, choque séptico e febre de origem indeterminada": 17
- "Endocardite infecciosa e meningites": 16
- "Imunossuprimidos, infecções osteoarticulares e de pele": 16

Já existem (não repita):
- Em uma infecção por bacilo gram-negativo, qual medicamento NÃO deve ser presumido ativo co
- Qual antimicrobiano é especialmente lembrado pela ausência de atividade contra Pseudomonas
- Qual par associa corretamente o alvo principal dos betalactâmicos ao parâmetro farmacodinâ
- Homem com pneumonia alveolar por MRSA tem indicação de terapia sistêmica. Qual agente NÃO 
- Em um paciente estável com cultura profunda confirmando bactéria sensível a agente estreit
- Uma cultura de urina positiva é encontrada em adulta não gestante sem sintomas urinários e
- Uma enterobactéria apresenta resistência por produção de enzima ESBL. Qual afirmação é mai
- Qual ação tem maior coerência com um Programa de Gerenciamento de Antimicrobianos instituc
- Ao avaliar prevenção de infecção associada a cateter venoso central, qual medida é adequad
- Sobre precauções-padrão em serviços de saúde, assinale a correta.
- Qual conduta é apropriada para profilaxia antimicrobiana cirúrgica em procedimento com ind
- Um serviço propõe diagnosticar toda IRAS apenas quando a infecção surge após 48 horas de i
- Segundo as diretrizes SSC de 2026, paciente com sepse apenas possível, sem choque, exige q
- Em choque séptico com hipotensão persistente após avaliação de volume, qual vasopressor te
- Homem de 45 anos mantém febre documentada há semanas, estável, sem foco evidente e sem imu
- Em pessoa com sepse e hipoperfusão, como interpretar a sugestão da SSC 2026 de cristaloide
- Paciente com prótese valvar, bacteremia persistente e suspeita de endocardite apresenta ET
- Em adulto com forte suspeita de meningite bacteriana e previsão de atraso para tomografia 
- Qual interpretação é correta para o líquor de meningite bacteriana típica?
- Sobre profilaxia de endocardite infecciosa associada a atendimento odontológico, assinale 
- Paciente neutropênico de alto risco chega com febre e instabilidade. Qual estratégia antib
- Homem com monoartrite aguda febril apresenta cristais no líquido sinovial. O que pode ser 
- Em infecção de partes moles com dor desproporcional, rápida progressão e toxicidade sistêm
- Uma úlcera de pé diabético crônica, sem eritema, calor, secreção purulenta nem sinais sist
- A vancomicina, em administração sistêmica, oferece cobertura apropriada para enterobactéri
- Aminoglicosídeos possuem atividade predominantemente concentração-dependente e podem causa
- O fato de um isolado ser sensível no antibiograma garante cura mesmo na presença de absces
- O mecanismo de resistência do MRSA envolve alteração de proteína-alvo de ligação à penicil
- A presença isolada de febre e proteína C reativa aumentada confirma necessidade de antibió
- Um programa institucional de gerenciamento de antimicrobianos pode contemplar protocolos l
- O uso de luvas dispensa a higiene das mãos antes e depois do contato indicado com paciente
- A permanência de cateter urinário sem indicação deve ser revista, pois a retirada precoce 
- A cultura positiva da ponta de um cateter, isoladamente, confirma infecção primária de cor
- Na profilaxia cirúrgica, manter antibiótico por rotina até retirada do dreno é recomendado
- Segundo a SSC 2026, qSOFA isolado é preferível a NEWS ou SIRS como único instrumento de ra
- Em adultos com choque séptico, a orientação SSC 2026 é iniciar antimicrobianos imediatamen
- Em sepsis-3, a utilização de vasopressor e lactato elevado após ressuscitação adequada aju
- Em FOI clássica estável, um regime prolongado de antibióticos empíricos deve anteceder a r
- Um ecocardiograma transtorácico inicial negativo exclui endocardite em paciente com prótes
- Na suspeita forte de endocardite em paciente estável, hemoculturas múltiplas devem ser pri
- Meningite bacteriana grave deve ter antibiótico atrasado até que todos os pacientes receba
- A presença de cristais no líquido sinovial elimina a necessidade de investigar artrite sép
- Na neutropenia febril de alto risco, cobertura empírica inicial antipseudomonas é importan
- Uma úlcera do pé diabético sem sinais clínicos de infecção não deve receber antimicrobiano
```

## Pedido 33: Engenharia de pesca e aquicultura (`engenharia.engenharia-pesca-aquicultura`, 128 tópicos de edital)

```
Matéria: engenharia.engenharia-pesca-aquicultura (Engenharia de pesca e aquicultura)
Capítulos e quantas questões quero em cada um (total 100):
- "Oceanografia, limnologia e ecologia aquática aplicada": 17
- "Biologia pesqueira, dinâmica populacional e avaliação de estoques": 17
- "Tecnologia de pesca, artes, embarcações e operações": 17
- "Aquicultura: sistemas, produção, sanidade e biossegurança": 17
- "Tecnologia do pescado, segurança dos alimentos e valorização": 16
- "Legislação, ordenamento e elaboração de projetos aquícolas": 16

Já existem (não repita):
- Um profissional é contratado para estudar estratificação, nutrientes e oxigênio em um rese
- Em viveiros com grande biomassa, o oxigênio dissolvido tende a alcançar o menor valor diár
- Uma embarcação captura 750 kg de peixe em 25 horas de operação padronizada. Qual é a CPUE 
- Qual das modalidades abaixo é definida especificamente pelo cultivo de moluscos como ostra
- O setor de tecnologia do pescado recebe lotes de peixes para filetagem. Qual medida preven
- A norma federal que institui a Política Nacional de Desenvolvimento Sustentável da Aquicul
- Que arte utiliza uma linha principal com vários anzóis dispostos em ramais?
- Em uma coorte sem entradas nem saídas extras, foram estocados 10.000 alevinos e restaram 8
- Durante uma vistoria de tanque-rede, detectou-se aumento de pH e temperatura e amônia tota
- Em uma série histórica, as capturas por viagem mantiveram-se elevadas apesar de indicadore
- Um fiscal registra captura não desejada de tartarugas em uma pescaria cujo alvo são peixes
- Em um cultivo intensivo, foram fornecidos 3.000 kg de ração e houve ganho de biomassa de 2
- Uma empresa observa deterioração por manejo impróprio anterior à recepção e suspeita de hi
- Um empreendedor pretende instalar cultivo em reservatório de domínio da União. Qual docume
- Qual alternativa diferencia corretamente o significado de L50 da maturação do L50 de seleç
- Na recepção industrial, uma planilha prevê identificar lote, origem, data, temperaturas e 
- Uma equipe compara CPUE de dois anos e encontra crescimento de 20%, ao mesmo tempo em que 
- Uma fazenda em sistema de recirculação apresenta aumento de amônia e nitrito após falha de
- Um projeto de cultivo de moluscos bivalves situa-se em enseada com histórico de floração a
- Após tempestade, um reservatório apresenta mistura da coluna de água e queda abrupta de OD
- Um modelo logístico de produção excedente expressa dB/dt = rB(1 − B/K) − C. O que represen
- No dimensionamento de produção de um ciclo, foram estocados 18.000 peixes, com sobrevivênc
- No planejamento do cultivo em águas da União, o gestor sugere que a cessão de uso de espaç
- Uma equipe avalia uma rede cuja malha foi aumentada para reduzir captura de juvenis. Qual 
- Em oceanografia, a zona fótica recebe luz suficiente para sustentar produção fotossintétic
- A CPUE resulta da divisão do esforço empregado pela captura realizada, sendo usualmente ex
- A pesca com espinhel utiliza anzóis distribuídos em ramais associados a uma linha principa
- Carcinicultura corresponde ao cultivo de crustáceos e a piscicultura corresponde ao cultiv
- O congelamento transforma pescado impróprio em seguro para consumo porque destrói todos os
- A Lei nº 11.959/2009 contém a política brasileira de desenvolvimento sustentável da aquicu
- Em viveiros de cultivo, pH e alcalinidade são exatamente a mesma variável e, por isso, pod
- A elevação do esforço de pesca pode aumentar a captura total mesmo sem aumento da biomassa
- A arqueação bruta de uma embarcação de pesca representa diretamente sua massa de combustív
- Nos sistemas de recirculação aquícola, a biofiltração que promove nitrificação consome oxi
- A rastreabilidade do pescado é desnecessária quando o produto apresenta cor e odor aceitáv
- O Decreto nº 11.852/2023 instituiu o ProAqui, que inclui iniciativas de desenvolvimento su
- A ressurgência consiste no afloramento de águas subsuperficiais e pode intensificar a prod
- O comprimento de primeira maturação e o comprimento de primeira captura de uma rede são se
- A observação de CPUE aparentemente estável em uma pescaria direcionada a agregações remane
- Em um modelo logístico simples, o rendimento excedente máximo em B = K/2 prova que qualque
- O uso de redes de malha maior elimina completamente a captura incidental, mesmo quando alt
- Em sistemas de bioflocos, a atividade microbiana e o cultivo intensivo podem elevar a dema
- A CAA de um ciclo pode ser calculada corretamente dividindo a ração fornecida pela biomass
- Como bivalves podem acumular biotoxinas de florações algais, o monitoramento sanitário da 
- A existência de cessão de uso de espaço em corpo hídrico federal implica, automaticamente,
```

## Pedido 34: Infraestrutura ferroviária, hidroviária e portuária (`transportes.ferrovias-hidrovias-portos`, 127 tópicos de edital)

```
Matéria: transportes.ferrovias-hidrovias-portos (Infraestrutura ferroviária, hidroviária e portuária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema ferroviário: componentes, traçado e projeto": 18
- "2. Operação ferroviária, custos, manutenção e outorgas": 25
- "3. Hidrovias: levantamentos, navegação, dragagem e balizamento": 21
- "4. Eclusas, portos interiores e operações aquaviárias": 18
- "5. Obras portuárias: acesso, proteção, atracação e desempenho": 18

Já existem (não repita):
- Na inspeção de um segmento ferroviário, foram inventariados trilhos, dormentes, lastro, pl
- Em uma ferrovia, os trilhos começaram a apresentar variação de distância entre si por degr
- Dois trens solicitam simultaneamente rotas que se cruzam em aparelhos de mudança de via de
- Uma ferrovia de via singela precisa permitir que composições de sentidos opostos se cruzem
- Um operador prepara uma composição convencional com veículos destinados à tração e outros 
- Após chuvas intensas, o lastro ficou saturado e houve recalque recorrente da via mesmo dep
- Uma empresa pretende construir e explorar nova ferrovia federal no regime privado de autor
- Uma administradora ferroviária descuida da conservação de sua linha, de vagões e de equipa
- Na negociação de transporte ferroviário de cargas, o cliente contrata o deslocamento e tam
- Em uma via singela, o programador percebe que dois trens em sentidos opostos chegarão ao m
- Uma barcaça parte do trecho de menor nível e precisa vencer a barragem por eclusa de câmar
- Para projetar um canal de navegação em rio, uma equipe precisa conhecer bancos submersos, 
- Em um trecho de rio, o leito acumula areia periodicamente e perde a profundidade prevista 
- O projeto de uma hidrovia identifica soleira de rocha sã obstruindo a seção de passagem; a
- Após dragagem de um canal, a autoridade responsável instala boias e balizas para indicar r
- Um empreendimento atende embarcações que trafegam entre localidades da mesma bacia fluvial
- Um porto necessita reduzir a energia das ondas na sua bacia de operação, sem que a nova ob
- Uma empresa instalará terminal de carga fora da poligonal do porto organizado e pretende e
- Em via singela, a administração pretende aumentar a frequência de partidas, mas os encontr
- Uma ferrovia tem custo total de R$ 900 mil para produzir 300 mil TKU em certo período. O p
- Em outubro de 2026, uma equipe revisa controles de contratos de ferrovias federais e utili
- Uma gestora deve priorizar reparos em ponte ferroviária muito utilizada e em estação pouco
- Uma barcaça tem calado de 2,40 m e a administração hidroviária exige folga mínima de 0,50 
- Um terminal possui equipamento de descarga capaz de 900 t/h, mas correias transportadoras 
- Durante a descida de uma embarcação por eclusa, a água está no nível de montante e as port
- Ao conferir o projeto de uma via férrea, a equipe classificou cortes, aterros e drenagem c
- Em um relatório de manutenção, a limpeza do lastro foi apresentada como intervenção destin
- Em ferrovia de via singela, a existência de pátios com linhas de cruzamento adequadas ao c
- Em canal hidroviário cujo obstáculo principal é uma soleira de rocha resistente, a dragage
- Uma embarcação posicionada na câmara inferior de uma eclusa pode ser elevada até o nível m
- Na avaliação de novo empreendimento federal, a equipe tratou a autorização da Lei nº 14.27
- Uma oficina monitora tendência de aquecimento em rolamentos ferroviários e programa a subs
- Se um trem transporta 600 toneladas úteis por 150 km, a produção ferroviária é de 750 TKU 
- Uma administradora ferroviária que delega a terceiros a execução de determinada obra de co
- No planejamento de dragagem, o levantamento batimétrico pode ser dispensado quando existe 
- Em projeto de porto interior, dimensionar o terminal apenas pelo equipamento de cais pode 
- Uma instalação portuária localizada fora da área do porto organizado, explorada mediante a
- Em 10 de outubro de 2026, uma análise das outorgas ferroviárias federais considera que a R
- Uma empresa autorizatária federal definiu que todos os preços cobrados dos clientes deverã
- Para escolher o método de retirada de obstrução de um rio, o projeto combina batimetria co
- Um canal com profundidade medida de 3,0 m admite sem outras verificações embarcação de cal
- Uma eclusa apresenta desnível elevado entre montante e jusante. Durante a descida de uma b
- No projeto de um berço portuário, as defensas são dimensionadas para sustentar o esforço p
- Um terminal com berço de grande produtividade apresenta filas crescentes porque o acesso t
- Como um quebra-mar reduz a agitação causada pelas ondas, sua implantação garante também a 
```

## Pedido 35: Segurança em laboratório químico e volumetria (`quimica.seguranca-laboratorio-volumetria`, 127 tópicos de edital)

```
Matéria: quimica.seguranca-laboratorio-volumetria (Segurança em laboratório químico e volumetria)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Prevenção de acidentes e proteção no laboratório": 24
- "2. Toxicidade, vapores perigosos, inflamáveis e incompatibilidades": 21
- "3. Biossegurança, material contaminado e descarte de resíduos": 21
- "4. Volumetria, vidrarias e titulações de neutralização": 17
- "5. Volumetria por precipitação, EDTA, redox e controle analítico": 17

Já existem (não repita):
- Em bancada com solvente volátil tóxico, um técnico usa luvas e óculos, mas trabalha fora d
- O laboratório recebe uma luva com Certificado de Aprovação, mas desconhece sua compatibili
- Um profissional precisa transferir 10,00 mL de solução corrosiva para um ensaio volumétric
- Em área que gera materiais perfurocortantes de serviço de saúde, o técnico encontra lâmina
- Uma bureta indica 0,80 mL no início e 25,30 mL ao fim da titulação. Qual volume de titulan
- Uma amostra de HCl consome NaOH em reação estequiométrica 1:1. Qual informação define o po
- Amostra de 20,00 mL de HCl é neutralizada por 10,00 mL de NaOH 0,200 mol/L. Qual concentra
- No preparo de solução de concentração conhecida, qual vidraria é especialmente apropriada 
- Uma solução em frasco secundário contém reagente classificado como perigoso, mas há soment
- Um técnico pretende manipular solvente volátil em cabine de segurança biológica com recirc
- Um operador manuseia produto de odor pouco perceptível que apresenta classificação de toxi
- Uma alíquota de 25,00 mL de H₂SO₄ é neutralizada por 20,00 mL de NaOH 0,1000 mol/L. Consid
- Na determinação de cloreto, 10,00 mL de amostra consomem 15,00 mL de AgNO₃ 0,0200 mol/L, c
- Em titulação ideal, ácido fraco monoprótico reage com base forte. Qual expectativa para pH
- Uma titulação de amostra consome 20,30 mL de titulante; o ensaio em branco correspondente 
- Um analista prepara NaOH por pesagem direta, mas o sólido pode absorver umidade e CO₂. Qua
- Na determinação seletiva de Ca²⁺, uma alíquota de 25,00 mL consome 10,00 mL de EDTA 0,0500
- Uma reação redox já balanceada segue Ox + 5 Red → produtos. Dez mL de Ox 0,0200 mol/L reag
- Uma amostra de 10,00 mL é diluída para 100,0 mL. Uma alíquota de 20,00 mL da diluída conso
- Em retrotitulação 1:1, são adicionados 0,0100 mol de reagente a um analito. A titulação do
- Um laboratório obtém em replicatas valores muito próximos entre si, mas sistematicamente a
- Ao planejar diluição de ácido concentrado conforme procedimento validado, uma pessoa prete
- Um laboratório pretende reduzir exposição a vapor tóxico recorrente. Há capela adequada di
- Uma bancada de serviço de saúde acumula lâminas usadas contaminadas e solução com metal tó
- Numa titulação redox, a reação balanceada exige dois mols do redutor para cada mol do titu
- Uma pessoa encontra frasco de produto químico perigoso com rótulo danificado. Antes de usá
- Um técnico conclui que luva com Certificado de Aprovação é obrigatoriamente resistente a q
- Ao organizar o estoque, um profissional separa substâncias oxidantes de solventes combustí
- Uma cabine de segurança biológica com recirculação do ar é automaticamente equivalente a c
- Em serviço de saúde, lâminas e agulhas usadas são classificadas como materiais perfurocort
- A ausência de odor perceptível durante uso de solvente classificado como tóxico por inalaç
- Em uma titulação de HCl com NaOH, cujos coeficientes estequiométricos são ambos iguais a u
- Durante titulação colorimétrica, a primeira mudança de cor persistente do indicador garant
- Na análise de cloreto, uma bureta marca 1,10 mL no início e 18,60 mL ao fim. O volume efet
- Na neutralização entre ácido fraco e base forte, o ponto de equivalência deve apresentar p
- Uma solução de NaOH preparada por pesagem pode necessitar padronização antes de análise qu
- Na titulação de H₂SO₄ com NaOH, a igualdade C_ácidoV_ácido = C_baseV_base determina direta
- Uma alíquota de 10,00 mL de solução é completada a 100,0 mL sem perdas nem reação. A dilui
- Em laboratório de saúde, uma agulha usada com resíduo químico perigoso deve ser destinada 
- Em uma titulação complexométrica seletiva de cálcio com EDTA, nas condições usuais de form
- Uma retrotitulação começa com 0,012 mol de reagente e revela 0,003 mol em excesso. Como nã
- Em método validado, um branco consome 0,40 mL de titulante pela matriz, enquanto a amostra
- Autoclavar um frasco com resíduo simultaneamente biológico e contendo metal pesado tóxico 
- Uma série de titulações fornece resultados muito repetíveis, porém afastados do padrão de 
- Numa reação redox balanceada 1 Ox : 2 Red, a determinação dos mols de redutor pode ser fei
```

## Pedido 36: Sistema financeiro internacional e finanças internacionais (`economia.sistema-financeiro-internacional`, 126 tópicos de edital)

```
Matéria: economia.sistema-financeiro-internacional (Sistema financeiro internacional e finanças internacionais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Da conversibilidade metálica ao fim de Bretton Woods": 20
- "2. Mercados internacionais, euromoedas e globalização": 19
- "3. FMI, BIS e bancos de desenvolvimento": 22
- "4. Acordos de Basileia e governança financeira": 18
- "5. Abertura financeira, ciclos de liquidez e desafios contemporâneos": 21

Já existem (não repita):
- Um país do século XIX mantém sua moeda conversível em uma quantidade predeterminada de our
- Durante uma aula, descreve-se o arranjo criado em 1944 que associava moedas nacionais ao d
- Ao preparar uma linha do tempo, um estudante registra o fechamento da janela do ouro pelo 
- Um quadro histórico mostra o abandono da conversibilidade do dólar em ouro em 1971. A qual
- Uma economia pretende manter câmbio fixo e elevada mobilidade internacional de capitais. D
- Um banco localizado em Singapura recebe depósitos em dólares americanos de clientes intern
- Uma empresa mantém sociedade em centro financeiro offshore e realiza operações documentada
- Uma companhia constrói uma fábrica no exterior e um fundo compra títulos de empresas estra
- Uma empresa com receitas apenas em moeda local deve 10 milhões de dólares sem proteção cam
- Um intermediário capta recursos externos renováveis a cada três meses para financiar ativo
- Um país enfrenta dificuldade para financiar seu balanço de pagamentos e busca supervisão m
- Durante uma discussão sobre governança do FMI, alguém sustenta que as quotas são irrelevan
- Uma autoridade registra ativos de reserva recebidos em alocação internacional criada em 19
- Em uma reunião de autoridades monetárias, discute-se a cooperação institucional estabeleci
- Um governo da América Latina procura financiamento de longo prazo e apoio técnico para amp
- Uma instituição revisa sua política de risco à luz da evolução dos padrões internacionais 
- Um relatório compara os mínimos internacionais básicos de capital no quadro de Basileia II
- Um banco apresenta ativos líquidos de alta qualidade de 120 unidades e saídas líquidas pro
- Um banco dispõe de financiamento estável disponível de 90 unidades e necessita de 100 unid
- Um banco calcula RWA de 110 unidades por modelos internos e de 200 por abordagens padroniz
- Após acumulação de dívida externa em moeda estrangeira, um país teme novos ingressos de ca
- Após período prolongado de juros internacionais baixos, investidores retiram recursos de u
- Um país quer preservar política monetária independente e permite transações financeiras in
- Bancos de diversas jurisdições ampliam crédito em dólares fora dos Estados Unidos e enfren
- Uma proposta tecnológica reúne depósitos bancários tokenizados, criptoativos sem emissor e
- Um pesquisador relata que a saída de ouro, sob paridade metálica, podia exigir condições m
- Um material didático afirma que, em Bretton Woods, toda pessoa que apresentasse notas de d
- Ao descrever o dilema de Triffin, um estudante observa que a criação de passivos internaci
- Na cronologia de um curso, atribui-se a março de 1973 o anúncio americano de fechamento da
- Uma instituição sediada em Londres capta dólares americanos de clientes e opera no segment
- Uma sociedade empresarial registrada em centro offshore pratica necessariamente evasão fis
- Ao distinguir integração econômica, uma análise classifica a instalação estrangeira de uni
- Uma instituição usa empréstimos externos renovados a cada noventa dias para manter ativos 
- Um país solicita ao FMI avaliação de sua política econômica sem assinar acordo de emprésti
- Um estudo de reservas internacionais classifica os direitos especiais de saque criados em 
- Em um seminário sobre instituições, afirma-se que o BIS foi criado em 1930 e que o Comitê 
- Um governo procura o BID, criado em 1959, para que a instituição fixe a taxa básica de jur
- O acordo de Basileia II associa exigências mínimas de capital, avaliação supervisora e dis
- Um banco apresenta CET1 de 4,6% dos ativos ponderados pelo risco e, por esse único dado, c
- Um banco com NSFR de 105% mantém recursos estáveis disponíveis superiores aos recursos est
- Em aplicação plena das reformas finais de Basileia III, o output floor de 72,5% determina 
- Uma economia identifica vulnerabilidades acumuladas em dívidas denominadas em moeda estran
- Uma companhia com receita local e dívida em dólares sem cobertura cambial vê sua moeda se 
- Um país combina mobilidade internacional de capitais com política monetária autônoma e ace
- Depósitos bancários tokenizados, moedas digitais de bancos centrais e criptoativos sem emi
```

## Pedido 37: Econometria: séries temporais, dados em painel e avaliação de impacto (`estatistica.econometria-series-painel-avaliacao-impacto`, 126 tópicos de edital)

```
Matéria: estatistica.econometria-series-painel-avaliacao-impacto (Econometria: séries temporais, dados em painel e avaliação de impacto)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Processos estocásticos, distribuições, estacionariedade e cointegração": 26
- "2. Modelos ARIMA, Box-Jenkins, sazonalidade e previsão": 19
- "3. VAR, volatilidade ARCH/GARCH e regressões não lineares": 17
- "4. Dados em painel, efeitos fixos, aleatórios e instrumentos": 17
- "5. Avaliação de impacto: contrafactual, diferenças em diferenças, descontinuidade e controle sintético": 21

Já existem (não repita):
- Em 100 observações, um evento X ocorre em 50, Y em 40 e ambos em 30. Uma análise deseja ve
- Uma série segue AR(1) com coeficiente autorregressivo 0,6 e perturbações de ruído branco. 
- O indicador observado satisfaz Y_t=Y_{t−1}+ε_t, com choques de média zero e variância posi
- Ao descrever a flutuação mensal de uma variável, foram incluídos o choque atual e o choque
- Uma análise aplica o teste ADF a uma série de preços em nível. Qual hipótese nula é adotad
- Um pesquisador complementa o teste ADF com o KPSS para uma série mensal. Qual interpretaçã
- Uma série em nível precisa ser diferenciada uma vez para estabilizar sua média e a diferen
- Um sistema utiliza as defasagens de inflação e atividade econômica para melhorar previsões
- Receitas e despesas são I(1), mas o resíduo da relação de longo prazo entre elas é estacio
- Uma série estacionária apresenta FAC com pico somente na primeira defasagem e FACP que dec
- Uma série mensal exibe picos recorrentes em defasagens 12 e 24. Deseja-se representar a es
- Após ajustar um ARIMA, os resíduos mantêm autocorrelação significativa em diversas defasag
- Num GARCH(1,1), ω=2, α=0,2 e β=0,6. Se o último erro ao quadrado foi 9 e a última variânci
- Um modelo logit para adesão a um serviço apresenta coeficiente ln(2) para X. Mantidas as d
- Em painel de unidades acompanhadas por vários anos, inclui-se um efeito fixo individual. U
- Antes de um programa, médias são 50 no grupo tratado e 40 no controle; depois, passam a 70
- Um benefício é concedido a pessoas com índice inferior a 60. Perto do limite, o recebiment
- Um regressor X sofre simultaneidade. Um instrumento Z apresenta Cov(Z,Y)=12 e Cov(Z,X)=3. 
- Uma única região adota um programa e há várias regiões não tratadas. O estudo constrói com
- Em painel, efeitos individuais omitidos podem estar correlacionados com X. Um teste de Hau
- Na avaliação por regressão descontínua, pessoas conseguem ajustar estrategicamente suas po
- Um painel utiliza EF individuais e temporais, mas um choque que varia entre unidades e per
- Duas séries são I(1) e uma combinação linear entre elas é I(0). O objetivo é modelar ajust
- Uma série estacionária segue AR(1) com média 100, φ=0,5 e última observação 120. Sob o mod
- Diversas regiões adotam uma política em anos distintos, com efeitos heterogêneos por coort
- Uma série mensal tem média, variância e autocovariâncias que, sob a mesma defasagem, se ma
- Um processo de ruído branco exige que seus valores sejam independentes e normalmente distr
- Se uma série segue ARIMA(1,1,0), o modelo prevê um componente autorregressivo de primeira 
- Num modelo MA(1), o parâmetro de primeira ordem multiplica diretamente o valor da variável
- Um VAR com duas séries pode utilizar defasagens de ambas em cada equação; detectar precedê
- Um logit com coeficiente positivo igual a 0,7 implica aumento fixo de 0,7 ponto percentual
- Dados em painel permitem acompanhar as mesmas unidades ao longo do tempo e explorar tanto 
- Uma série não rejeita a hipótese nula do ADF ao nível de significância escolhido. Esse res
- Duas séries individualmente I(1) podem admitir uma combinação linear I(0), situação compat
- Para série mensal cuja sazonalidade anual é relevante, o SARIMA deve necessariamente usar 
- Um modelo de volatilidade GARCH(1,1) pode representar aglomeração de choques grandes e peq
- O estimador usual de efeitos fixos por transformação within identifica separadamente o coe
- Um instrumento estatisticamente relevante pode ser inadequado para identificação causal se
- Em diferenças em diferenças, a hipótese essencial para a interpretação causal é a igualdad
- Em um AR(1) estacionário com média de longo prazo 100, coeficiente 0,5 e último valor 120,
- Em GARCH(1,1), quando α+β fica perto de um, os choques sobre a variância condicional tende
- Se a probabilidade inicial de um evento é 0,20, a duplicação de suas chances em um modelo 
- Se o teste clássico de Hausman não rejeita sua hipótese nula, fica demonstrado que o efeit
- Uma regressão descontínua sharp em torno de um corte pode identificar efeito causal local,
- O controle sintético assegura estimativa causal válida mesmo com mau ajuste antes da polít
```

## Pedido 38: Recursos no processo do trabalho (`processo-trabalho.recursos`, 126 tópicos de edital)

```
Matéria: processo-trabalho.recursos (Recursos no processo do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema recursal, efeitos, prazos e preparo": 19
- "2. Recursos ordinário, agravos e embargos de declaração": 24
- "3. Recurso de revista, transcendência e embargos no TST": 17
- "4. Uniformização, recursos repetitivos, IRDR e ação coletiva": 26
- "5. Processo judicial eletrônico, intimações e estratégia recursal": 14

Já existem (não repita):
- Durante a instrução trabalhista, o juízo decide incidentalmente uma questão probatória sem
- Uma Vara encerra o processo trabalhista por sentença definitiva, e a parte sucumbente pret
- Uma sentença contém omissão relevante acerca de pedido expressamente examinado pelas parte
- Durante a execução trabalhista, decisão recorrível fixa valores e o devedor pretende conte
- Foi negado seguimento a um recurso ordinário por falta de pressuposto recursal, fora de hi
- A parte interpõe recurso ordinário tempestivo contra sentença que impõe pagamento, sem obt
- Uma decisão é regularmente intimada e o prazo recursal de oito dias começa a correr depois
- Um empregador deve efetuar depósito recursal em recurso sujeito a preparo. Qual é a finali
- O processo retorna de decisão de Vara e chega a acórdão de TRT, em recurso ordinário, que 
- Uma entidade é beneficiária da justiça gratuita e pretende recorrer de condenação trabalhi
- Uma empresa de pequeno porte apresenta recurso trabalhista que exige depósito recursal. Qu
- Uma parte apresenta embargos de declaração tempestivos, assinados e por representante regu
- Uma revista é negada no TRT porque o acórdão regional segue precedente qualificado do TST,
- Uma reclamação segue rito sumaríssimo e o acórdão regional decide controvérsia apenas sobr
- Numa execução trabalhista comum, a parte pretende recurso de revista por suposta divergênc
- A discussão envolve centenas de processos com controvérsia sobre idêntica questão de direi
- Um órgão colegiado se depara com questão jurídica de grande repercussão social, mas não há
- Em recurso de revista, o reclamante-recorrente postula direito social previsto na Constitu
- O TRT, ao decidir admissibilidade de revista, afirma não processá-la porque a controvérsia
- Um relator do TST nega transcendência monocraticamente em agravo de instrumento de recurso
- Depois de firmada tese num incidente de recursos de revista repetitivos, recurso sobrestad
- Em um procedimento eletrônico, decisão aparece como disponibilizada no diário na segunda-f
- Uma intimação enviada a portal eletrônico cadastrado não é consultada em dez dias corridos
- Uma ação civil pública trabalhista foi julgada originariamente por Vara, em sentença defin
- Em recurso de revista, a parte afirma existir nulidade por negativa de prestação jurisdici
- Uma decisão interlocutória sobre prova, proferida durante o conhecimento trabalhista, em r
- Após sentença definitiva de Vara, a parte interpõe recurso ordinário em cinco dias úteis p
- Uma parte interpõe recurso contra sentença trabalhista, e a decisão não lhe atribui suspen
- Na fase de execução, uma decisão recorrível versa sobre atualização do débito. O instrumen
- Uma sentença contém omissão sobre argumento capaz de influenciar a conclusão. A parte pode
- O agravo de instrumento trabalhista tem por finalidade normal o reexame direto de todos os
- Em agravo de instrumento contra negativa de recurso sujeito a depósito, o art. 899, § 7º, 
- Uma entidade sem fins lucrativos, não caracterizada como filantrópica e sem justiça gratui
- Uma parte opõe embargos de declaração tempestivos, assinados e por representante regular. 
- Em causa sujeita ao rito sumaríssimo, a divergência entre dois TRTs sobre lei federal, sem
- Quando o TRT examina pressupostos de admissibilidade de recurso de revista, a CLT, art. 89
- Após julgamento de incidente de revista repetitiva, um acórdão regional contradiz a orient
- Um órgão julgador enfrenta questão relevante de direito com grande repercussão social, mas
- No rito de revista, a parte pode exigir que a presidência do TRT julgue a ausência de tran
- Após o julgamento do ArgInc-1000845-52.2016.5.02.0461, a irrecorribilidade da decisão mono
- Como os antigos §§ 3º a 6º do art. 896 da CLT instituíam uniformização jurisprudencial reg
- No diário eletrônico regido pelo art. 4º da Lei nº 11.419/2006, ato disponibilizado na seg
- Em intimação pelo portal da Lei nº 11.419/2006, ausência de consulta dentro de dez dias co
- Quando os requisitos legais e regimentais estão presentes, agravo interno ou regimental po
- Uma ação coletiva trabalhista termina por sentença definitiva proferida em Vara. Por se tr
```

## Pedido 39: Libras, tradução e interpretação (`educacao.libras-traducao-interpretacao`, 126 tópicos de edital)

```
Matéria: educacao.libras-traducao-interpretacao (Libras, tradução e interpretação)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos linguísticos da Libras, parâmetros e datilologia": 17
- "Morfologia, verbos, classificadores e sintaxe espacial": 17
- "História da educação de surdos, cultura e identidades": 17
- "Legislação da Libras, educação bilíngue e profissão": 17
- "Tradução, interpretação, ética e atuação educacional": 16
- "Guia-interpretação, surdocegueira, bilinguismo e mídia acessível": 16

Já existem (não repita):
- A Língua Brasileira de Sinais possui estrutura gramatical própria e não constitui mera tra
- As expressões faciais usadas em Libras servem exclusivamente para transmitir emoções, sem 
- O alfabeto manual é utilizado na datilologia e pode apoiar a identificação de nomes própri
- Classificadores em Libras são idênticos ao alfabeto manual, pois sua função exclusiva cons
- No modelo da LDB, a educação bilíngue de surdos prevê Libras como primeira língua e portug
- A Lei nº 10.436/2002 determina que a Libras substitua a modalidade escrita da língua portu
- O intérprete educacional pode apoiar o acesso linguístico do estudante surdo sem assumir, 
- Legenda descritiva e janela de Libras são a mesma tecnologia, pois ambas traduzem diretame
- O Congresso de Milão de 1880 é um marco associado ao fortalecimento de práticas oralistas 
- Sinais icônicos são compreendidos universalmente por qualquer pessoa que visualize o gesto
- O uso de referências espaciais pode permitir que um verbo direcional indique relações entr
- Na Libras, a indicação de tempo exige que todo verbo receba uma terminação manual específi
- A Lei nº 14.191/2021 incluiu na LDB os arts. 60-A e 60-B, relacionados à educação bilíngue
- A presença de uma janela de Libras visual é necessariamente suficiente para a comunicação 
- Na interpretação consecutiva, o profissional costuma reformular uma unidade comunicativa a
- O Decreto nº 5.626/2005 prevê, como regra, Libras obrigatória em todas as graduações e em 
- Segundo a legislação profissional atualizada em 2023, trabalhos de tradução e interpretaçã
- A duração de trabalho dos profissionais disciplinados no art. 8º-A da Lei nº 12.319/2010 é
- O art. 60-A da LDB permite a oferta de educação bilíngue de surdos em escola comum, sem re
- As medidas transitórias do Decreto nº 5.626/2005 previstas para dez anos devem ser interpr
- Uma pessoa surdocega que emprega alfabeto manual tátil pode necessitar de mediação distint
- Fidelidade na interpretação significa reproduzir obrigatoriamente a ordem linear das palav
- A Libras admite variação regional e geracional, o que não retira seu estatuto de língua na
- Conceber bilinguismo para estudantes surdos como mera alternância ocasional de sinais e fa
- Em uma prova sobre os parâmetros fonológicos da Libras, qual elemento corresponde à direçã
- Uma escola afirma que Libras seria um conjunto universal de gestos compreendido da mesma m
- Um profissional precisa informar a grafia de um sobrenome desconhecido por seu interlocuto
- Na Libras, a pessoa sinalizante usa configuração de mão e trajetória para mostrar como um 
- A respeito da Lei nº 10.436/2002, marque o enunciado que reproduz corretamente o núcleo do
- Qual combinação corresponde à modalidade de educação bilíngue de surdos prevista no art. 6
- Durante uma aula, o aluno surdo faz uma pergunta ao professor por Libras. Que atuação é co
- Para permitir que pessoas com deficiência auditiva identifiquem uma campainha e também as 
- Considere uma glosa em maiúsculas apresentada em um manual, sem imagem nem descrição de ex
- Um estudante sinaliza ONTEM antes de descrever um fato, sem flexionar manualmente todos os
- Sobre os processos de formação e flexão verbal da Libras, qual descrição é defensável?
- Uma professora descreve a educação de surdos baseada apenas em treinamento de fala e leitu
- Para avaliar as identidades surdas numa pesquisa escolar, qual pressuposto respeita a abor
- Em relação ao Decreto nº 5.626/2005, qual afirmativa está de acordo com a distinção curric
- Em uma audiência, o intérprete pede que uma sigla técnica seja esclarecida antes de prosse
- Uma pessoa surdocega conserva visão periférica restrita e prefere acompanhar sinais em um 
- Uma banca afirma que a direção de um verbo na Libras sempre indica deslocamento físico no 
- Em uma sequência sinalizada, o profissional observa levantamento de sobrancelhas associado
- Sobre a trajetória histórica do INES, qual enunciado lida corretamente com os registros do
- Um edital usa a redação original da Lei nº 12.319/2010 e omite a Lei nº 14.704/2023. Para 
- Um profissional possui diploma superior em outra área e curso de especialização de 360 hor
- Uma instituição organiza tradução/interpretação em atividade contínua de 75 minutos com ap
- Uma escola oferece intérprete de Libras para aulas em português oral, mas não disponibiliz
- Um treinamento em vídeo contém locução, ruído de alarme e um fluxograma essencial. Qual co
```

## Pedido 40: Cardiologia clínica (`medicina.cardiologia-clinica`, 126 tópicos de edital)

```
Matéria: medicina.cardiologia-clinica (Cardiologia clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "Hipertensão arterial, prevenção cardiovascular e dislipidemias": 17
- "Doença arterial coronariana e síndromes coronarianas agudas": 17
- "Insuficiência cardíaca e miocardiopatias": 17
- "Arritmias, distúrbios de condução e dispositivos cardíacos": 17
- "Valvopatias adquiridas, pericardite e endocardite": 16
- "Eletrocardiograma, ecocardiograma e testes funcionais": 16

Já existem (não repita):
- A pressão de consultório de 150/94 mmHg corresponde à faixa de HAS grau 1 do PCDT brasilei
- Na hipertensão mascarada, a pressão encontra-se elevada no consultório e normal na MAPA ou
- Um paciente com PA não controlada usando três classes anti-hipertensivas deve ter adesão e
- Em prevenção cardiovascular primária, o ácido acetilsalicílico é obrigatório para todos os
- Angina relacionada ao esforço pode decorrer de disfunção microvascular, mesmo sem estenose
- Qualquer valor de troponina acima do percentil 99 define infarto do miocárdio por trombose
- O IAM sem supradesnivelamento persistente de ST exige obrigatoriamente a presença de supra
- Após infarto, o surgimento de novo sopro acompanhado de descompensação pode exigir ecocard
- Fração de ejeção ventricular esquerda de 60% exclui insuficiência cardíaca em qualquer sit
- Em paciente com ICFEr, um diurético de alça pode reduzir congestão, mas não substitui o tr
- Uma fração de ejeção que melhora após tratamento comprova resolução definitiva da cardiomi
- Uma variante genética de significado incerto, sem outros achados, basta para indicar CDI e
- Fibrilação atrial frequentemente apresenta intervalos RR irregularmente irregulares e ausê
- A forma paroxística da fibrilação atrial dispensa avaliação para anticoagulação por aprese
- Bloqueio AV de Mobitz I caracteriza prolongamento progressivo do intervalo PR antes de oco
- O cardiodesfibrilador implantável e o marcapasso convencional apresentam exatamente a mesm
- Estenose valvar produz dificuldade ao fluxo anterógrado e frequentemente sobrecarga de pre
- A estenose aórtica típica causa sopro diastólico aspirativo com irradiação predominante pa
- Em suspeita de endocardite e paciente clinicamente estável, a coleta apropriada de hemocul
- Todo derrame pericárdico identificado ao ecocardiograma é, por definição, tamponamento car
- À velocidade do ECG de 25 mm por segundo, um quadrado pequeno horizontal representa 40 mil
- Holter e MAPA são métodos equivalentes, pois ambos registram continuamente a pressão arter
- A fração de ejeção no ecocardiograma representa diretamente o débito cardíaco e independe 
- No bloqueio completo de ramo esquerdo, alterações secundárias de ST–T podem limitar a inte
- Homem de 52 anos apresenta PA de consultório de 148/92 mmHg em avaliação isolada, sem sina
- Qual situação indica de forma mais clara a utilidade de MAPA de 24 horas na avaliação de h
- Em hipertenso com valores persistentes acima da meta usando IECA, antagonista de cálcio e 
- Em paciente com coronariopatia aterosclerótica documentada e LDL ainda elevado apesar de e
- Mulher com angina estável ao esforço faz angiotomografia sem lesões epicárdicas obstrutiva
- Qual achado é necessário para sustentar diagnóstico de infarto do miocárdio, distinguindo-
- Qual é a afirmação correta sobre a síndrome coronariana aguda sem supradesnivelamento pers
- Após um infarto, paciente apresenta sopro sistólico novo e piora clínica. Qual exame é pri
- Mulher com dispneia, hipertrofia ventricular e sinais de elevação de pressões de enchiment
- Entre as seguintes opções, qual reúne de modo mais apropriado os pilares modificadores de 
- Em investigação de miocardiopatia hipertrófica familiar, qual dado deve motivar atenção à 
- Qual é a interpretação adequada quando uma pessoa com ICFEr apresenta recuperação sustenta
- Paciente em consulta de rotina apresenta ECG sem ondas P organizadas e RR irregularmente i
- Para prevenir AVC em paciente com fibrilação atrial, qual conceito é o mais adequado?
- Em qual cenário é mais apropriado pensar especificamente em terapia de ressincronização ca
- Em monitorização eletrocardiográfica, qual padrão caracteriza bloqueio atrioventricular de
- Idoso apresenta sopro sistólico ejetivo no foco aórtico com irradiação para carótidas e di
- Uma paciente com fibrilação atrial e estenose mitral reumática moderada ou importante prec
- Paciente apresenta dor torácica pior em decúbito e inspiração, aliviada ao inclinar-se par
- Em suspeita de endocardite de prótese valvar, o ecocardiograma transtorácico não mostrou v
- Um eletrocardiograma foi registrado a 25 mm/s e 10 mm/mV. Quantos segundos representam cin
- Em paciente com palpitações episódicas que não aparecem no ECG de repouso, qual exame regi
- Qual alternativa descreve adequadamente uma limitação do teste ergométrico convencional na
- Em paciente com dispneia e suspeita de IC, qual descrição do ecocardiograma transtorácico 
```

## Pedido 41: Técnica legislativa e redação normativa (`direito.tecnica-legislativa-redacao-normativa`, 126 tópicos de edital)

```
Matéria: direito.tecnica-legislativa-redacao-normativa (Técnica legislativa e redação normativa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, fontes e estrutura das normas": 20
- "2. Articulação, clareza, precisão e técnica de redação": 20
- "3. Alteração legislativa, vigência, revogação e consolidação": 22
- "4. Proposições legislativas, emendas e manifestações técnicas": 20
- "5. Atos internos, controle de qualidade e aplicação prática": 18

Já existem (não repita):
- Uma minuta de lei foi identificada por uma linha em maiúsculas que informa a espécie norma
- Ao revisar projeto que contém comandos substantivos, medidas transitórias e cláusula de en
- A redação de uma norma contém três hipóteses enumeradas diretamente a partir do caput de u
- Um artigo de projeto contém somente uma disposição complementar ao caput. Qual forma de id
- A parte final de uma lei precisará retirar de vigência dois artigos de diploma anterior. Q
- Durante a análise de uma proposição em tramitação, pretende-se retirar inteiramente um art
- Uma proposta será instruída por documento cujo objetivo é examinar fundamento de validade,
- Para manifestação institucional de reconhecimento a uma iniciativa comunitária, sem produz
- Na esfera regulada pelo Decreto nº 12.002/2024, um colegiado edita ato normativo que disci
- Uma lei proposta tem capítulo principal sobre cadastro de unidades públicas e artigo isola
- Uma minuta define a expressão 'pessoa solicitante' e posteriormente alterna sem justificat
- Em texto legislativo há os dispositivos Art. 8º, Art. 9º e o artigo seguinte. Segundo a LC
- Uma lei é publicada em 3 de junho e fixa vacância de cinco dias pela regra do art. 8º, § 1
- Pretende-se inserir dispositivo entre os arts. 4º e 5º de lei vigente sem modificar a nume
- Um projeto reúne leis esparsas de mesma matéria em diploma único, atualizando terminologia
- Uma autoridade pretende editar portaria para criar obrigação que a lei não prevê, sem comp
- Uma comissão deseja apresentar versão global alternativa ao projeto analisado, reestrutura
- Uma consolidação federal pretende juntar três leis de cadastro, fundir dispositivos repeti
- Uma equipe prepara projeto de consolidação de legislação federal e cogita inserir nele uma
- Em uma revisão de regulamento federal, alguém afirma que o art. 9º do Decreto nº 12.002/20
- Uma proposta federal destina-se somente a revogar um decreto antigo e não estabelece disci
- Uma lei tem o Art. 6º marcado como revogado e seu Art. 7º permanece em vigor. Pretende-se 
- Uma proposta normativa apresenta parecer de mérito que demonstra benefício econômico, mas 
- Um projeto de consolidação declara expressamente revogado dispositivo cuja revogação implí
- Em ato do Executivo federal há incorreção de redação em desacordo com o Decreto nº 12.002/
- Uma lei complementar disciplina a elaboração e a redação de leis em cumprimento ao parágra
- Em uma minuta de lei, as cláusulas de vigência e revogação, quando necessárias, devem fica
- Na apresentação de projeto sobre cadastramento público, uma frase curta que sintetiza seu 
- Um projeto contém preâmbulo com a listagem dos requisitos de inscrição e deixa a competênc
- Um artigo com apenas um parágrafo, destinado a ressalvar a regra principal, deve identific
- Uma minuta enumera hipóteses do caput por incisos numerados em algarismos arábicos, reserv
- Um texto normativo define 'unidade responsável' e usa exatamente essa expressão nas regras
- Depois de acrescentar um inciso em artigo da lei, o redator mantém a frase 'conforme o inc
- Uma lei de regulação de cadastros começa indicando no art. 1º seu objeto e os destinatário
- Uma lei publicada em 1º de março estabelece vacância de três dias, contados pela LC nº 95/
- Uma lei nova substitui parte de outra e apresenta no final cláusula que enumera precisamen
- Ao acrescentar novo artigo entre o Art. 8º e o Art. 9º de lei vigente, pode-se renumerar a
- Ao analisar proposta do Executivo federal, a equipe separa documento que examina problema,
- Um projeto de consolidação altera o requisito material para concessão de um direito, mas r
- Ao consolidar normas de uma matéria em diploma único, podem-se revogar formalmente as leis
- Uma medida provisória federal ainda não convertida em lei pode integrar imediatamente cons
- No âmbito do Decreto nº 12.002/2024, em regra, portarias e instruções normativas são edita
- Para acelerar a aprovação de regulamento federal, uma autoridade pode usar despacho de sua
- Na instrução de proposta normativa federal submetida ao regime do Decreto nº 12.002/2024, 
- Segundo o art. 76 do Decreto nº 12.002/2024, toda inobservância de suas regras redacionais
```

## Pedido 42:  diplomática, legislação arquivística e gestão documental no Judiciário (`arquivologia.diplomatica-legislacao-arquivistica`, 125 tópicos de edital)

```
Matéria: arquivologia.diplomatica-legislacao-arquivistica ( diplomática, legislação arquivística e gestão documental no Judiciário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Diplomática, tipologia e gênese dos documentos": 31
- "2. Autenticidade e análise diplomática no meio digital": 15
- "3. Legislação arquivística, acesso e proteção documental": 16
- "4. Gestão documental e digitalização no Proname": 19
- "5. Memória institucional e políticas de preservação": 19

Já existem (não repita):
- Uma unidade produz uma certidão de trânsito em julgado e a cadastra com a expressão comple
- Um despacho eletrônico foi produzido por autoridade competente, sem alteração posterior, m
- Documentos pouco consultados permanecem armazenados até alcançar o prazo definido para rec
- Após avaliação, um setor pretende eliminar documentos públicos cujo prazo de guarda termin
- Uma instituição decide formalizar sua política integrada de gestão de documentos e memória
- Um cidadão requer informação administrativa a um tribunal, sendo encaminhado ao Serviço de
- Uma equipe precisa identificar a resolução que estabeleceu diretrizes próprias para digita
- Uma equipe examina regras de credenciamento e tratamento de informação classificada previs
- Uma unidade de arquivo cria exposição, visitas mediadas e conteúdo digital sobre sua histó
- Um setor escolhe requisitos para um sistema informatizado de gestão arquivística de docume
- Um arquivo PDF nasceu de um sistema eletrônico e nele foi validamente registrado o ato adm
- Um processo encerrado ainda é requisitado com frequência pela unidade produtora, embora nã
- Um grupo precisa compreender a função do CONARQ após alterações do Decreto 4.073/2002, inc
- Um centro de memória organiza exposição sobre antigas decisões, mas propõe agrupar origina
- Uma unidade precisa distinguir a função do plano de classificação da tabela de temporalida
- Um arquivo privado é reconhecido como de interesse público e social nos termos da Lei 8.15
- Após digitalizar milhares de autos físicos, uma equipe pretende adotar a eliminação anteci
- Um registro digital recebeu assinatura verificável, mas não contém referência à atividade 
- Uma unidade afirma que a Lei 8.159/1991 ainda contém, nos arts. 22 a 24, o regime vigente 
- Para planejar a preservação a longo prazo de documentos digitais, uma instituição compara 
- Durante a migração de formato de autos digitais de guarda prolongada, a equipe percebe alt
- Uma instituição pretende eliminar uma série de autos encerrados porque sua tabela de tempo
- Um memorial deseja usar documentos de processos de épocas distintas para sustentar uma nar
- Uma comissão analisa uma imagem digital íntegra de um antigo documento em papel que aprese
- Uma equipe confunde a competência do Arquivo Nacional com a dos arquivos do Poder Judiciár
- Uma unidade produz ata de reunião e ata de inspeção em diferentes atividades. Embora os ti
- Um despacho foi assinado eletronicamente pela autoridade competente. Em razão da autentici
- Um arquivo pouco consultado aguarda decisão de destinação. A Lei 8.159/1991 admite seu enq
- Uma instituição terminou a captura digital de autos físicos e verificou sua legibilidade. 
- Uma equipe desenvolve visitas guiadas e exposições a partir de processos de valor históric
- Uma instituição utiliza o Decreto 7.845/2012 para afirmar que ele disciplina indistintamen
- Um tribunal implementa instrumento de classificação ligado às atividades, tabela de tempor
- Um arquivo privado é reconhecido como de interesse público e social. Isso significa que se
- Durante avaliação de autenticidade digital, a verificação de hash e o exame de metadados d
- Uma equipe sustenta que o art. 23 da Lei 8.159/1991 ainda fornece, em sua redação originár
- Ao migrar documento nato-digital para formato preservável, a equipe mantém registro do eve
- Uma comissão identifica série de guarda permanente e determina sua eliminação por já exist
- No tratamento de arquivos digitais, um identificador, registro do produtor, histórico de v
- Para simplificar uma exposição, um centro de memória muda a proveniência documental no ins
- O Manual de Gestão Documental do Poder Judiciário e o Manual de Gestão de Memória têm obje
- Por constar de diretrizes de preservação digital, um repositório arquivístico confiável as
- Ao descrever um lote digitalizado, registrar falhas identificadas na captura, refazimentos
- Ao reconhecer um arquivo privado como de interesse público e social, a administração deve 
- A análise de uma minuta que registra versões anteriores de decisão institucional pode reve
- Como a Resolução CNJ 215/2015 cuida de transparência, ela revoga as normas de destinação d
```

## Pedido 43: Mensuração a valor justo (CPC 46) e ajuste a valor presente (CPC 12) (`contabilidade.valor-justo-ajuste-valor-presente`, 125 tópicos de edital)

```
Matéria: contabilidade.valor-justo-ajuste-valor-presente (Mensuração a valor justo (CPC 46) e ajuste a valor presente (CPC 12))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Definição de valor justo e premissas": 34
- "2. Técnicas de avaliação e hierarquia": 33
- "3. Ajuste a valor presente (CPC 12)": 33

Já existem (não repita):
- Segundo o CPC 46, valor justo é o preço que seria recebido pela venda de um ativo ou que s
- O valor justo, conforme o CPC 46, corresponde a um preço:
- Ao mensurar o valor justo de um ativo que pretende manter por longo prazo, a entidade deve
- Uma entidade pode vender certo ativo em dois mercados. No mercado X, de maior volume e ati
- Na ausência de mercado principal, o valor justo é mensurado com base no mercado mais vanta
- Sobre custos de transação e de transporte na mensuração do valor justo, é correto afirmar 
- Os participantes do mercado, para fins do CPC 46, são compradores e vendedores independent
- O conceito de maior e melhor uso, no CPC 46, aplica-se:
- Para determinar o maior e melhor uso de um ativo não financeiro, basta que o uso seja fisi
- Uma indústria usa um terreno como pátio de estacionamento, mas a legislação local permite,
- O valor justo de um passivo reflete o risco de descumprimento, que inclui, entre outros, o
- No reconhecimento inicial, o preço da transação pode não representar o valor justo quando:
- As três abordagens de avaliação previstas no CPC 46 são:
- A técnica de fluxo de caixa descontado, que converte fluxos futuros em um único valor pres
- A abordagem de custo reflete o valor que seria necessário atualmente para substituir a cap
- Ao aplicar técnicas de avaliação, a entidade deve:
- Na hierarquia do valor justo, o Nível 1 compreende:
- O preço cotado de um ativo similar em mercado ativo é classificado como informação de Níve
- Uma entidade mensura um investimento usando principalmente uma curva de juros observável, 
- No Nível 1, a entidade pode ajustar o preço cotado por um fator de bloqueio quando possui 
- Taxas de juros e curvas de rendimento observáveis em intervalos comumente cotados são exem
- Para mensurações recorrentes de valor justo classificadas no Nível 3, a entidade deve divu
- Pela Lei 6.404/1976, os elementos do ativo e do passivo decorrentes de operações de longo 
- Quanto à obrigatoriedade do ajuste a valor presente, é correto afirmar que ele é:
- A taxa de desconto usada no ajuste a valor presente deve refletir as avaliações atuais de 
- Uma empresa vende mercadorias por R$ 110.000, para recebimento em um ano. A taxa de juros 
- No exemplo de venda a prazo com ajuste a valor presente, a parcela de juros embutida no pr
- Na compra de estoques a prazo, com juros embutidos relevantes, o ajuste a valor presente l
- A conta de ajuste a valor presente de clientes tem natureza de conta redutora do ativo.
- A apropriação dos juros decorrentes do ajuste a valor presente deve ser feita, em regra, p
- Uma empresa compra um equipamento por R$ 121.000, a pagar em dois anos, quando a taxa de m
- No caso do equipamento comprado por R$ 121.000 a pagar em dois anos, com valor presente de
- O ajuste a valor presente e a mensuração a valor justo são sempre conceitos equivalentes, 
- Em regra, NÃO está sujeito ao ajuste a valor presente previsto no CPC 12:
- Se uma entidade mensura um passivo a valor justo e sua própria qualidade de crédito piora,
- Um mercado ativo, para fins do CPC 46, é aquele em que as transações ocorrem com frequênci
- Na mensuração do valor justo de uma commodity, o preço no mercado principal é de R$ 50 por
- As mudanças nas técnicas de avaliação do valor justo, quando apropriadas, são contabilizad
- No mercado mais vantajoso, a escolha do mercado leva em conta os custos de transação, mas,
- Projeções de fluxos de caixa elaboradas com dados próprios da entidade, sem informações de
```

## Pedido 44: Odontopediatria (`odontologia.odontopediatria`, 125 tópicos de edital)

```
Matéria: odontologia.odontopediatria (Odontopediatria)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Crescimento craniofacial e desenvolvimento das dentições": 24
- "2. Diagnóstico, cárie dentária, dieta e higiene": 20
- "3. Manejo comportamental, controle da dor e restaurações": 20
- "4. Tratamento pulpar e traumatismos dentários": 18
- "5. Periodonto, estomatologia, gestação e atenção ao bebê": 18

Já existem (não repita):
- Uma criança de seis anos apresenta novo dente posterior ao último molar decíduo, sem perda
- Em um prontuário, o profissional descreve a composição da dentição decídua completa. Qual 
- Uma criança apresenta dente decíduo com mobilidade progressiva e imagem de reabsorção radi
- Após observar uma mancha branca rugosa e opaca em incisivo decíduo, sem cavitação, qual ab
- A família de um bebê pergunta quando iniciar a escovação com dentifrício fluoretado. Qual 
- Uma criança teme o sugador odontológico. O profissional explica sua função, demonstra-o re
- Para conferir a dose de lidocaína a 2%, o profissional utiliza um tubete de 1,8 mL. Quanto
- Um molar decíduo restaurável tem pulpite reversível e exposição cariosa com indicação de r
- Após queda, um incisivo decíduo é completamente expulso do alvéolo. O responsável pergunta
- Uma criança apresenta gengiva eritematosa e sangramento à escovação, sem perda de inserção
- Na avaliação do espaço da dentição mista, a profissional compara o diâmetro mesiodistal de
- Durante o planejamento de restauração profunda em segundo molar decíduo, qual particularid
- Duas crianças consomem volume semelhante de bebida açucarada. Uma a toma de uma só vez e o
- Um molar permanente recém-erupcionado tem fissuras profundas, sem cavidade, em criança de 
- Uma criança não permite procedimento eletivo por medo intenso, mas está sem infecção ou do
- Um molar decíduo perdeu grande parte da coroa por cárie, porém pode ser mantido e protegid
- Após avulsão de incisivo permanente, a criança chega rapidamente com o dente conservado em
- Um incisivo permanente de raiz incompleta sofreu exposição pulpar traumática. Há diagnósti
- Lactente apresenta pequenas pápulas esbranquiçadas no palato, desde o nascimento, sem dor 
- Gestante no primeiro trimestre apresenta dor dentária intensa e edema por infecção odontog
- Na análise de uma criança de oito anos, observa-se perda precoce do segundo molar decíduo 
- Criança com vários dentes restaurados apresenta nova mancha branca ativa e lanches açucara
- Em um atendimento, foram administrados 2,5 mL de anestésico com lidocaína a 2%. Qual quant
- Um dente permanente imaturo apresenta necrose pulpar e ápice aberto, enquanto outro, també
- Uma criança apresenta perda localizada de inserção periodontal incompatível com a quantida
- No exame de uma criança em dentição decídua, espaços mesiais aos caninos superiores e dist
- Quando uma criança perde fisiologicamente o segundo molar decíduo, o dente permanente suce
- Após erupcionar o primeiro incisivo do bebê, a escovação com pequena quantidade de dentifr
- Uma criança que coopera com a técnica falar-mostrar-fazer pode passar por remoção dolorosa
- Após avulsão completa de incisivo decíduo, o atendimento deve avaliar tecidos, orientar a 
- Em uma criança de alto risco, a presença de mancha branca ativa sem cavitação obriga a rea
- Uma criança que consome pequenas porções açucaradas repetidamente durante o dia pode apres
- Dois tubetes completos de 1,8 mL de lidocaína a 2% contêm 36 mg do agente no total, valor 
- Em molar decíduo restaurável com indicação adequada, a pulpotomia remove a polpa coronária
- Uma gestante com abscesso odontogênico deve aguardar obrigatoriamente o segundo trimestre 
- Em uma criança com perda precoce de segundo molar decíduo e primeiro molar permanente erup
- Ao avaliar uma mordida cruzada posterior com desvio funcional, pode-se atribuir toda alter
- Uma criança sem cavidades atuais, mas com consumo frequente de açúcar, higiene insuficient
- A estabilização protetora de criança com resistência ao tratamento pode ser escolhida como
- Lactente com febre, gengiva difusamente inflamada, múltiplas úlceras e dificuldade de inge
- Em permanente imaturo necrótico, a apicificação reproduz necessariamente o mesmo espessame
- Após avulsão de incisivo permanente, o período extra-alveolar seco superior a 60 minutos i
- Todo dente presente ao nascimento deve ser extraído, ainda que esteja estável, sem lesar t
- Em adolescente na puberdade com gengivite e biofilme, mudanças hormonais podem intensifica
- A ausência isolada de resposta ao teste de sensibilidade em incisivo permanente recém-erup
```

## Pedido 45: Precedentes, jurisprudência do TST e instruções normativas (`processo-trabalho.jurisprudencia-tst-instrucoes-normativas`, 125 tópicos de edital)

```
Matéria: processo-trabalho.jurisprudencia-tst-instrucoes-normativas (Precedentes, jurisprudência do TST e instruções normativas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema de precedentes, autoridade e técnica de aplicação": 14
- "2. IRDR, IAC e recursos repetitivos trabalhistas": 14
- "3. Súmulas, orientações jurisprudenciais e uniformização regional": 25
- "4. Súmulas vinculantes e repercussão geral trabalhista": 25
- "5. Instruções normativas, direito intertemporal e aplicação dos precedentes": 22

Já existem (não repita):
- Uma decisão isolada de uma turma foi invocada como vinculante para todos os processos, emb
- Um tribunal pretende instaurar incidente diante de centenas de processos com a mesma contr
- Uma controvérsia jurídica relevante apresenta grande repercussão social, mas não aparece r
- Uma pessoa interpôs recurso de revista apenas para que fosse novamente avaliado qual depoi
- Uma parte sustenta que a Justiça do Trabalho deve executar de ofício contribuições referen
- Uma reclamação discute ação possessória proposta em razão de greve dos trabalhadores de em
- Uma equipe jurídica quer saber qual instrução normativa do TST trata das regras do CPC de 
- Um órgão colegiado deseja afastar incidência de lei por considerá-la inconstitucional, sem
- Um tribunal regional verifica duas formas de resolver controvérsia: demandas repetidas ou 
- Em consulta a material antigo, foi encontrada a Súmula TST 219 como enunciado ainda vigent
- O tribunal regional respondeu expressamente à questão jurídica discutida, porém não citou 
- Após dispensas coletivas, trabalhadores alegam que não ocorreu participação sindical prévi
- Uma convenção coletiva restringiu direito não absolutamente indisponível sem indicar contr
- Uma reclamação ajuizada depois de 11/11/2017 suscita regra processual alterada pela Reform
- Em 2026, alguém cita como vigente a OJ nº 13 das Turmas da 3ª Região, ignorando sua situaç
- Uma ação trabalhista foi julgada em conformidade com tese de recurso repetitivo, e o recur
- Uma demanda indenizatória por acidente laboral foi ajuizada pelo empregado contra seu empr
- Após admissão de IRDR, o autor de um processo representativo desiste da ação. Uma parte su
- O IRDR foi admitido, processos da região foram suspensos e decorreu um ano sem julgamento.
- Na terceirização de serviços, uma decisão responsabilizou subsidiariamente ente público ap
- Uma empresa privada terceirizou atividade diretamente ligada ao seu objeto social, e o aut
- Uma parte afirma que a Súmula nº 27 da 3ª Região segue aplicável às parcelas posteriores à
- Em ação regional, foi invocada a antiga OJ nº 8 da 1ª SDI da 3ª Região sobre bloqueio de v
- Um acórdão afirma aplicar precedente obrigatório, mas cita apenas conclusão isolada sem ex
- Ao consultar a IN nº 40 para escolher recurso contra decisão sobre admissibilidade parcial
- Uma decisão anterior pode ter força persuasiva sem necessariamente integrar a lista de pre
- O IRDR pode ser instaurado com base apenas na repercussão social de uma questão isolada, m
- A Súmula TST nº 126 é importante para diferenciar discussão jurídica da tentativa de reexa
- A Súmula Vinculante nº 23 trata da execução de ofício das contribuições previdenciárias de
- A Instrução Normativa TST nº 39/2016 trata de aplicação subsidiária e supletiva do CPC ao 
- O Tema STF 638 exige autorização sindical como condição de validade de toda dispensa colet
- Uma orientação jurisprudencial regional deve ser consultada com a identificação do órgão e
- Uma súmula trabalhista cancelada pela Resolução TST nº 225/2025 continua necessariamente a
- O incidente de assunção de competência do CPC 947 pode ser utilizado para prevenir ou comp
- A Súmula Vinculante nº 53 autoriza a execução de ofício de qualquer contribuição previdenc
- A IN nº 41/2018 do TST foi elaborada para orientar a aplicação temporal de normas processu
- No Tema 1046, o STF declarou que normas coletivas podem suprimir direitos absolutamente in
- A CLT, art. 896-C, prevê técnica de julgamento de recursos de revista repetitivos baseada 
- O CPC 489, § 1º, permite que o julgador cite apenas o número de um precedente sem identifi
- Admitido IRDR, a desistência da causa individual que motivou sua instauração não impede o 
- A negativa de seguimento de recurso de revista por conformidade com precedente repetitivo 
- O Tema 1118 do STF rejeita a responsabilização subsidiária do poder público fundada apenas
- A OJ nº 8 da 1ª SDI do TRT da 3ª Região permaneceu vigente após o julgamento do IRDR Tema 
- A IN TST nº 40 deve ser consultada em versão atualizada porque houve alterações em 2024, 2
- O Tema 725 do STF mantém a proibição geral de terceirização de atividade-fim e afasta a re
```

## Pedido 46: Instalações prediais elétricas, SPDA, elevadores e climatização (`engenharia.instalacoes-prediais-eletricas-climatizacao`, 124 tópicos de edital)

```
Matéria: engenharia.instalacoes-prediais-eletricas-climatizacao (Instalações prediais elétricas, SPDA, elevadores e climatização)
Capítulos e quantas questões quero em cada um (total 100):
- "Projeto e dimensionamento das instalações elétricas prediais": 17
- "Proteções elétricas, aterramento, surtos e SPDA": 17
- "Segurança contra incêndio e instalações prediais de gás": 17
- "Elevadores, transporte vertical e acessibilidade": 17
- "Ar-condicionado, ventilação e cálculo de carga térmica": 16
- "PMOC, qualidade do ar e manutenção predial integrada": 16

Já existem (não repita):
- Um aquecedor puramente resistivo de 4.400 W funciona em 220 V. Qual corrente aproximada de
- No levantamento das cargas de um edifício, qual expressão define corretamente a demanda?
- Ao comparar o mesmo circuito instalado isolado e instalado com muitos outros condutores em
- Um circuito monofásico longo apresenta queda de tensão excessiva e as demais condições se 
- No estudo da coordenação da proteção de sobrecarga, Ib, In e Iz representam, respectivamen
- Em um circuito terminal predial, um disjuntor conduz a corrente normal com folga, mas a co
- Qual dispositivo é especificamente empregado para detectar corrente diferencial residual d
- O responsável pela reforma decide unir o neutro ao pino de proteção de uma tomada, sem ava
- Em um prédio novo, qual composição traduz adequadamente os elementos clássicos de um SPDA 
- Um edifício com SPDA externo pretende proteger equipamentos eletrônicos sensíveis contra e
- A série ABNT NBR 5419 verificada em 2026 está corretamente associada a qual abordagem?
- Qual exemplo caracteriza principalmente uma medida passiva de segurança contra incêndio?
- Em relação às exigências de segurança contra incêndio para prédios públicos de estados dis
- Uma central de GLP é projetada em edifício comercial. Qual peculiaridade física do gás é m
- Uma instalação nova de gás combustível ainda não foi liberada para ocupação. Qual procedim
- Um empreendimento avalia o uso de monta-cargas para transportar trabalhadores entre pavime
- Uma equipe compara ABNT NBR 16858 e ABNT NBR 16083 em edital de manutenção de elevadores. 
- Dois edifícios têm a mesma população, mas os elevadores de um deles param em muito mais pa
- Uma pessoa ficou presa no elevador após pane elétrica. Qual encaminhamento é tecnicamente 
- Qual componente do ciclo de compressão de vapor absorve calor do ambiente a ser resfriado?
- Um equipamento de refrigeração é anunciado com capacidade de 24.000 BTU/h. Qual capacidade
- Por que instalar um split convencional não comprova, por si só, atendimento à ventilação n
- Ao dimensionar o resfriamento de um auditório, qual situação afeta especialmente a parcela
- Para um edifício de uso coletivo artificialmente climatizado, qual instrumento é expressam
- A manutenção de uma unidade de tratamento de ar é acionada quando o aumento de perda de ca
- Um sistema reparável apresenta MTBF de 90 horas e MTTR de 10 horas, calculados segundo a m
- No projeto elétrico predial, o diagrama unifilar representa de modo simplificado os circui
- O valor da corrente calculado por P/U, para uma carga resistiva, basta para determinar com
- Se as demais grandezas forem mantidas, dobrar o comprimento de um circuito tende a aumenta
- A capacidade de interrupção de um disjuntor é irrelevante para edifícios residenciais desd
- O DR e o DPS respondem a grandezas distintas: o primeiro detecta corrente diferencial resi
- Para garantir o funcionamento do condutor de proteção, recomenda-se ligá-lo ao neutro dire
- Um SPDA externo pode reduzir danos decorrentes da descarga atmosférica, mas a proteção dos
- A série ABNT NBR 5419:2026 permite concluir, sem avaliação de risco ou características da 
- Extintores, detecção e alarme são exemplos típicos de medidas ativas de proteção contra in
- A passagem de dutos de ar por barreiras corta-fogo dispensa análise de selagem e compartim
- O GLP gasoso normalmente apresenta densidade superior à do ar, condição relevante na avali
- Em redes prediais de gás, uma conexão nova pode ser liberada exclusivamente por inspeção v
- Um monta-cargas deve ser tratado automaticamente como elevador de passageiros quando sua c
- A análise de tráfego de elevadores deve considerar a demanda de usuários, o ciclo de viage
- Em termos de escopo, a ABNT NBR 16083 dedica-se às instruções de manutenção de equipamento
- Em situação de parada de elevador entre pavimentos, qualquer funcionário do condomínio pod
- Num sistema frigorígeno por compressão, o evaporador absorve calor, enquanto o condensador
- Dois ambientes com a mesma área de piso necessariamente apresentam a mesma carga térmica d
- Um equipamento indicado como 1 TR necessariamente consome 3,52 kW de energia elétrica enqu
- Sistemas que apenas recirculam o ar da sala não garantem, por esse fato isolado, a renovaç
- A Lei nº 13.589/2018 exige PMOC para edifícios de uso público e coletivo com ambientes int
- Um PMOC arquivado, sem comprovação de execução das inspeções e medidas previstas, é sufici
- A manutenção preditiva utiliza indicadores e tendências do estado do equipamento para orie
- No acompanhamento da qualidade do ar interior, um valor adequado de temperatura basta para
```

## Pedido 47: Ataques Cibernéticos: DDoS, Amplificação, Smurf, Força Bruta, Phishing, Spear Phishing e APT (`informatica.ataques-ddos-forca-bruta-phishing-apt`, 123 tópicos de edital)

```
Matéria: informatica.ataques-ddos-forca-bruta-phishing-apt (Ataques Cibernéticos: DDoS, Amplificação, Smurf, Força Bruta, Phishing, Spear Phishing e APT)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Negação de serviço: DDoS, amplificação e smurf": 34
- "2. Força bruta e engenharia social: phishing e spear phishing": 33
- "3. APT: ameaça persistente avançada": 33

Já existem (não repita):
- O ataque de negação de serviço distribuído (DDoS) tem como principal objetivo comprometer 
- A principal diferença entre DoS e DDoS é que o DDoS:
- No SYN flood, o atacante envia grande quantidade de pedidos de conexão TCP sem completar o
- No ataque de amplificação, o atacante:
- Serviços baseados em UDP, como DNS e NTP, são frequentemente explorados em ataques de ampl
- O ataque smurf caracteriza-se pelo envio de:
- Desabilitar o encaminhamento de broadcast direcionado nos roteadores é uma medida de mitig
- A filtragem de pacotes com endereço de origem falsificado (antispoofing) nas bordas das re
- O HTTP flood é exemplo de ataque de negação de serviço na camada de aplicação, com requisi
- No smurf, a vítima é inundada porque:
- O ataque de dicionário é uma forma de força bruta que utiliza listas de senhas comuns e pa
- O ataque que usa pares de usuário e senha vazados de outros serviços, explorando a reutili
- No password spraying, o atacante testa muitas senhas diferentes contra uma única conta até
- É medida eficaz de defesa contra ataques de força bruta em formulários de login:
- O uso de salt no armazenamento de senhas dificulta ataques de força bruta off-line contra 
- A diferença entre phishing e spear phishing é que o spear phishing:
- O whaling é uma forma de spear phishing direcionada a altos executivos de uma organização.
- O golpe de phishing realizado por mensagens SMS é denominado:
- Phishing e pharming são sinônimos, pois ambos dependem de o usuário clicar em link recebid
- Os mecanismos SPF, DKIM e DMARC ajudam a combater o phishing porque:
- Mensagens de phishing costumam explorar urgência ou ameaça, como bloqueio de conta, para l
- A sigla APT refere-se a:
- Uma APT caracteriza-se por ataques rápidos e ruidosos, que buscam derrubar o serviço o qua
- Na intrusão inicial de uma APT, um vetor bastante comum é:
- Após a intrusão inicial, a APT costuma estabelecer persistência, escalar privilégios e rea
- O objetivo mais típico de uma APT é:
- Não existe solução única contra APT; a defesa exige camadas, monitoramento contínuo, privi
- Os atores por trás de APTs costumam ser:
- APTs podem explorar vulnerabilidades ainda desconhecidas pelos fabricantes, chamadas de ze
- O pilar da segurança da informação diretamente afetado por um ataque de amplificação bem-s
- Uma botnet é uma rede de computadores infectados controlados remotamente pelo atacante, mu
- O fator de amplificação, em ataques de reflexão, corresponde à razão entre:
- O ataque de força bruta afeta principalmente a disponibilidade, pois seu objetivo é tirar 
- O golpe em que o criminoso telefona fingindo ser do banco para obter dados da vítima é cha
- Um servidor DNS configurado como resolvedor aberto para qualquer endereço da internet pode
- Um funcionário recebe e-mail com seu nome, cargo e menção a um projeto real, pedindo que a
- O compartilhamento de indicadores de comprometimento e de inteligência de ameaças ajuda a 
- A variante do smurf que usa pacotes UDP em vez de ICMP é conhecida como:
- O ataque de negação de serviço que explora o handshake de três vias do TCP é o:
- O spear phishing, por ser direcionado e personalizado, tende a ter taxa de sucesso maior q
```

## Pedido 48: Programa Nacional de Direitos Humanos e participação social (`direito.programa-nacional-direitos-humanos`, 123 tópicos de edital)

```
Matéria: direito.programa-nacional-direitos-humanos (Programa Nacional de Direitos Humanos e participação social)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais e estrutura jurídica do PNDH-3": 18
- "2. Eixos I a III: participação, desenvolvimento e igualdade": 18
- "3. Eixos IV e V: violência institucional, justiça e educação em direitos humanos": 17
- "4. Direito à memória e à verdade, reparação e não repetição": 17
- "5. Participação social: decreto histórico de 2014, revogação e sistema de 2023": 30

Já existem (não repita):
- Um grupo pretende consultar o dispositivo que aprovou formalmente o PNDH-3 e sua estrutura
- Um estudante organiza os temas do PNDH-3 e registra a quantidade de eixos orientadores e d
- Uma proposta estabelece diálogo continuado com associações civis para avaliar políticas de
- Um programa propõe preservar arquivos históricos de repressão política e disponibilizar do
- Uma medida prevê educação em direitos humanos no âmbito do serviço público, com oficinas r
- Uma política pretende proteger recursos ambientais, considerando expressamente seus impact
- Uma política objetiva erradicar tortura praticada por agentes estatais e reduzir letalidad
- Um historiador consulta a lei que instituiu uma comissão temporária com a finalidade de es
- Uma autoridade consulta o texto de 2026 e pergunta pela situação jurídica do Decreto nº 8.
- Um relatório afirma que os prazos, metas e recursos de implementação do PNDH-3 serão defin
- Uma comissão analisa uma proposta de educação comunitária em direitos humanos promovida fo
- Em uma revisão de políticas, uma equipe pretende construir indicadores públicos e integrar
- A comissão responsável por montar um ciclo de participação em 2014 queria receber contribu
- Uma proposta legislativa busca remover normas remanescentes de períodos autoritários incom
- Uma comissão de monitoramento pretende verificar o que o art. 5º do Decreto nº 7.037/2009 
- Uma equipe propõe incentivar medidas alternativas à prisão e melhorar condições de cumprim
- A administração federal quer estruturar um sistema composto por órgão central e órgãos set
- Ao analisar um ciclo de participação da gestão, um avaliador diferencia plebiscito e refer
- Após a revogação do Decreto nº 8.243/2014 pelo Decreto nº 9.759/2019, a norma revogadora f
- Uma investigação histórica necessita documentos públicos classificados, testemunhos e dili
- Uma exposição comunitária reúne relatórios de violações antigas, sem pedido de reparação p
- Uma revisão de 2026 descobre que o art. 4º do Decreto nº 7.037/2009 foi revogado em 2019. 
- Um plano busca aproximar defesa de direitos humanos e fiscalização de políticas públicas. 
- Uma equipe confunde o sistema federal de 2023 com o conselho criado naquele mesmo ano. Qua
- Um pesquisador compara a Lei nº 9.140/1995, a Lei nº 12.528/2011 e o PNDH-3 e encontra trê
- Um programa de capacitação utiliza o art. 2º do Decreto nº 7.037/2009 para classificar edu
- Uma iniciativa de esclarecimento sobre repressão política é atribuída ao Eixo Orientador I
- Uma proposta institucional combina medidas de proteção a vítimas de crimes e a pessoas ame
- Um texto histórico do Decreto nº 8.243/2014 define audiência pública como mecanismo destin
- Uma política pretende produzir indicadores públicos de monitoramento e avaliar sistematica
- Uma análise de 2026 considera o Decreto nº 8.243/2014 plenamente vigente porque o Decreto 
- Uma proposta de preservação de documentos relativos a violações cometidas durante períodos
- Uma equipe afirma que a criação da Comissão Nacional da Verdade pela Lei nº 12.528/2011 at
- Em discussão sobre desenvolvimento, a integração de direitos ambientais aos direitos human
- Um gestor sustenta que o art. 5º do Decreto nº 7.037/2009 obrigou automaticamente estados 
- O Sistema de Participação Social criado pelo Decreto nº 11.407/2023 estrutura-se em órgão 
- O art. 3º do Decreto nº 7.037/2009 determina elaboração de Planos de Ação de Direitos Huma
- Embora a comissão de verdade instituída pela Lei nº 12.528/2011 tivesse duração temporária
- Um estudo de normas afirma que, após a revogação do art. 4º do Decreto nº 7.037/2009 pelo 
- Uma política dirigida a pessoas historicamente excluídas utiliza medidas acessíveis para a
- Em 2026, o Conselho de Participação Social criado pelo Decreto nº 11.406/2023 e o Sistema 
- O art. 4º, VII, do Decreto nº 11.406/2023 incluiu na composição do Plenário do Conselho de
- No PNDH-3, a Diretriz 22 organiza sobretudo a integração de sistemas de informações e o mo
- A Lei nº 9.140/1995, na redação posterior de seu art. 1º, e a Lei nº 12.528/2011 possuem o
- Por reconhecer participação social, a Diretriz 1 do PNDH-3 torna vinculante qualquer propo
```

## Pedido 49: Resoluções do Contran (`legislacao-transito.resolucoes-contran`, 123 tópicos de edital)

```
Matéria: legislacao-transito.resolucoes-contran (Resoluções do Contran)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Competência do Contran e sinalização vertical": 21
- "2. Sinalização horizontal e integração dos manuais": 21
- "3. Fiscalização de álcool e outras substâncias: norma de 2026": 20
- "4. Crianças, passageiros, equipamentos e transporte de cargas": 19
- "5. Fiscalização eletrônica, validade das resoluções e leitura de casos": 19

Já existem (não repita):
- Um município pretende padronizar suas placas e consulta o fundamento legal do poder regula
- Uma placa ao longo de rodovia indica velocidade máxima permitida de 80 km/h. A que família
- Em uma avenida, o projeto divide duas faixas do mesmo sentido por marca longitudinal branc
- Um condutor em rodovia bidirecional encontra linha amarela contínua junto à sua faixa. Que
- Em operação de fiscalização realizada em setembro de 2026, um agente usa pré-teste passivo
- Durante abordagem atual, o agente registra apenas odor de álcool, sem segundo sinal de alt
- Uma criança de oito anos e 1,30 m viaja em automóvel particular. Qual referência normativa
- Um veículo de carga transporta caixas fixadas somente por cordas, presas à carroceria. Qua
- Um registro eletrônico mostra imagem do veículo e sua velocidade medida, mas a fiscalizaçã
- Ao preparar projeto de sinalização de cruzamento, uma equipe consulta o volume referente à
- Um projeto inclui placa octogonal com ordem de parada e placa triangular invertida para pr
- Em interseção, a ordem dada pelo agente de trânsito diverge momentaneamente da indicação s
- Uma pessoa é abordada sem sinais observáveis de alteração psicomotora e afirma que a ausên
- Um veículo de transporte remunerado individual está efetivamente prestando serviço. Qual a
- Uma carreta está dentro do PBT e dos limites de eixo, mas parte da carga desliza durante f
- Durante etilometria, há indício de álcool residual na cavidade bucal, prejudicando a obten
- O veículo passa por radar fixo redutor instalado em ponto crítico, com painel exibindo vel
- Uma equipe encontra versões distintas do sinal R-32 no Volume I e pretende descobrir qual 
- Em abordagem de outubro de 2026, o fiscalizado apresenta alteração psicomotora registrada 
- Uma operação pretende empregar medidor portátil de velocidade em trecho previamente identi
- Uma rodovia de dois sentidos possui dupla linha amarela: contínua junto ao veículo X e sec
- Um veículo particular só possui banco dianteiro e transporta criança dentro da faixa etári
- Uma pessoa analisa fatos de 10 de outubro de 2026 e encontra orientação baseada na Resoluç
- Um órgão pretende usar imagem de câmera simples, sem medidor homologado, como prova numéri
- Uma equipe compara equipamento de medição de velocidade com pórtico de cobrança automática
- Uma norma do Contran detalha como devem ser instalados e interpretados sinais de trânsito,
- Uma placa de advertência de curva acentuada produz, por si só, a mesma regulamentação numé
- Em sinalização horizontal, uma linha longitudinal amarela costuma separar sentidos opostos
- Uma marca amarela seccionada permite ao condutor ultrapassar outro veículo mesmo quando a 
- Um agente obtém resultado positivo no pré-teste passivo de álcool e orienta a realização d
- A Resolução 819/2021 estabelece que todo transporte de criança de idade inferior a dez ano
- O Contran publicou em agosto de 2026 norma nova para fiscalização de álcool e outras subst
- O etilômetro utilizado para medição de álcool é igualmente o equipamento definido pela Res
- A constatação de alteração psicomotora exclusivamente por sinais observados na fiscalizaçã
- O resultado indicativo de pré-teste passivo pode ser utilizado sozinho para caracterizar i
- Ao transportar criança com menos de dez anos e altura inferior a 1,45 m em automóvel parti
- O transporte remunerado individual de passageiros recebe dispensa indistinta e permanente 
- A Resolução 945/2022 exige que a carga permaneça ancorada e acondicionada para evitar desl
- Duas normas regulamentares tratam de cargas: a Resolução 882/2021 cuida apenas de disposit
- Um medidor portátil de velocidade está sujeito às condições de planejamento e uso ostensiv
- Um radar fixo está instalado e registra imagem legível, razão pela qual as condições de ap
- Sinalização insuficiente ou incorreta pode impedir aplicação de sanção por sua inobservânc
- Pela Resolução 798/2020, a velocidade considerada no registro de excesso corresponde diret
- O Manual Brasileiro de Sinalização de Trânsito é vinculado à Resolução 973/2022, enquanto 
- Como a Resolução 1.031/2026 menciona prazo de sessenta dias após publicação, todo seu cont
```

## Pedido 50: Citação, garantia da execução e penhora (`processo-trabalho.citacao-garantia-penhora`, 122 tópicos de edital)

```
Matéria: processo-trabalho.citacao-garantia-penhora (Citação, garantia da execução e penhora)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Citação para pagar ou garantir, mandado e depósito da condenação": 22
- "2. Garantia do juízo, depósito, nomeação de bens e seguro-garantia": 32
- "3. Penhora, bloqueio eletrônico, arresto e constrições especiais": 18
- "4. Bens impenhoráveis, salários, poupança e proteção patrimonial": 14
- "5. Bem de família: Lei nº 8.009/1990, exceções e situações controvertidas": 14

Já existem (não repita):
- Uma execução trabalhista exige pagamento em dinheiro. Após a citação executiva regular, qu
- Um executado foi procurado por oficial duas vezes no intervalo de 48 horas e não foi encon
- Na execução trabalhista, a pessoa executada não pagou a obrigação e deseja garanti-la com 
- Um devedor possui saldo bancário penhorável, veículo e imóvel. Qual classe ocupa a primeir
- Um imóvel residencial pertence à família executada por débito trabalhista comum, sem qualq
- Na execução, o credor busca quantia em conta bancária do executado. Segundo o CPC, art. 85
- O juízo bloqueou eletronicamente valores e intimou o executado. Pelo CPC, art. 854, § 3º, 
- Uma execução alcança dinheiro em caderneta de poupança. Qual limite consta literalmente da
- Um bem de família pertence ao fiador de um contrato de locação, e a execução se relaciona 
- Após citação, a dívida foi integralmente paga. O art. 881 da CLT descreve qual formalizaçã
- Uma execução foi garantida por fiança bancária substitutiva de penhora. Para dívida de ref
- Um executado pretende nomear uma máquina de difícil venda quando existem valores financeir
- Uma pessoa executada possui crédito a receber de terceiro, ainda não pago. Como se efetiva
- Uma pessoa executada discute em outro processo o recebimento de quantia. Pretende-se reser
- Uma empresa executada não tem outros bens penhoráveis suficientes, e se cogita constrição 
- Foi bloqueada a quantia de R$ 90.000 para execução de R$ 60.000. O excesso foi detectado n
- Uma pessoa usa ferramentas e máquinas indispensáveis à sua atividade profissional, enquadr
- Uma execução é proposta por obrigação de financiamento destinado especificamente à aquisiç
- Uma pessoa solteira mora sozinha no imóvel de sua propriedade e sofre execução por dívida 
- Uma pessoa executada tem o único imóvel residencial alugado a terceiros e usa o aluguel pa
- Em determinada situação, pretende-se penhorar parte de remuneração mensal e de poupança. Q
- Uma petição fundamenta a penhora da residência em antigo inciso I do art. 3º da Lei 8.009/
- A penhora recai sobre quotas de sócio de sociedade empresária. O CPC, art. 861, prevê praz
- No curso da execução, uma penhora recai sobre imóvel de pessoa casada. O CPC, art. 842, ex
- Uma pessoa insolvente adquire deliberadamente residência de valor muito superior para tran
- Na cobrança de obrigação trabalhista pecuniária, o art. 880 da CLT prevê citação para paga
- Na execução trabalhista, o prazo de pagamento do art. 880 da CLT é de três dias, porque o 
- O art. 882 da CLT admite depósito atualizado com despesas, seguro-garantia judicial ou nom
- Na ordem preferencial do CPC, art. 835, os veículos de via terrestre ocupam a primeira pos
- O art. 854 do CPC permite a indisponibilidade eletrônica de ativos financeiros sem ciência
- O bem de família do executado perde automaticamente toda proteção legal quando o crédito c
- A fiança bancária e o seguro-garantia judicial, para fins de substituição de penhora, equi
- Se houver bloqueio eletrônico excessivo, o juiz deverá aguardar requerimento da parte e pr
- A penhora de crédito devido por terceiro exige intimação do terceiro para não pagar direta
- A penhora no rosto dos autos é destinada à apreensão de veículo diretamente no estacioname
- Segundo o art. 833, X, do CPC, a quantia depositada em caderneta de poupança até quarenta 
- O art. 833, § 2º, do CPC reafirma a impenhorabilidade incondicional de todo salário, inclu
- A Lei nº 8.009/1990, art. 2º, exclui veículos de transporte, obras de arte e adornos suntu
- O inciso I do art. 3º da Lei nº 8.009/1990 continua em vigor e afasta a proteção da residê
- Na penhora de quotas sociais, o art. 861 do CPC prevê prazo razoável, em regra não superio
- A penhora de percentual do faturamento de empresa executada pode ser fixada em qualquer pa
- A Súmula 486 admite proteção do único imóvel residencial alugado a terceiros quando o alug
- A Súmula 549 impede a penhora do único imóvel do fiador de contrato de locação, porque a f
- Na penhora de imóvel ou direito real imobiliário, o art. 842 do CPC determina intimação ta
- O art. 5º da Lei nº 8.009/1990 permite à família detentora de várias residências escolher 
```

