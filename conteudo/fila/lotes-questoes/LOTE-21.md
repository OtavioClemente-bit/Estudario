# Pedidos de questões extras, lote 21 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 1350 tópicos de edital.

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

## Pedido 1: Contabilidade de instituições financeiras e COSIF (`contabilidade.instituicoes-financeiras-cosif`, 33 tópicos de edital)

```
Matéria: contabilidade.instituicoes-financeiras-cosif (Contabilidade de instituições financeiras e COSIF)
Capítulos e quantas questões quero em cada um (total 100):
- "1. COSIF: fundamentos, estrutura, escrituração e demonstrações": 18
- "2. Liquidez interfinanceira, títulos e instrumentos derivativos": 17
- "3. Investimentos e ativos não financeiros mantidos para venda": 26
- "4. Sistema cooperativo: combinação contábil e balancete": 17
- "5. Liquidação extrajudicial: avaliação, reconhecimento e integração": 22

Já existem (não repita):
- Uma entidade regulada precisa organizar a escrituração e a remessa contábil. Qual descriçã
- Uma instituição transfere 100 mil de suas disponibilidades para aplicação em depósito inte
- Uma aplicação remunerada gerou rendimentos ao final de um mês, mas o resgate ocorrerá no m
- Uma instituição mantém títulos de dívida para receber principal e juros contratualmente pr
- Uma instituição possui swap com valor nocional de um milhão e valor justo favorável de 15 
- Uma instituição reconhece posição favorável em derivativo na data-base. Em qual classe pat
- Uma instituição decidiu vender participação societária em controlada, disponível para vend
- Um imóvel próprio, destinado à alienação imediata altamente provável em até um ano, possui
- Uma cooperativa central possui direito contra cooperativa singular integrante do mesmo sis
- Na decretação da liquidação extrajudicial de instituição financeira abrangida pela Resoluç
- Uma aplicação interfinanceira de 500 mil gerou remuneração de 7 mil no mês, ainda não paga
- Uma carteira de títulos de dívida é gerida tanto para recebimento dos fluxos contratuais q
- Uma área de tesouraria contrata swap para reduzir risco de juros e pretende aplicar contab
- Um imóvel próprio apresenta valor contábil líquido de 450 mil, valor justo de 420 mil e de
- Um crédito de difícil recuperação possui saldo contábil bruto de 300 mil e provisões de 60
- Em sistema cooperativo, central e singular reconhecem reciprocamente 150 mil de direitos e
- Na elaboração do balancete combinado referente a 2026, qual é a referência normativa geral
- Uma instituição em liquidação extrajudicial tem ativo com valor contábil líquido de 180 mi
- Na abertura da liquidação extrajudicial, há despesas antecipadas de 40 mil que não serão r
- Uma tesouraria aplica a um título de dívida a antiga classe 'disponível para venda' sem ex
- No balancete combinado, uma cooperativa singular concedeu crédito a cliente externo ao sis
- Uma instituição recebe imóvel na quitação de crédito problemático com saldo bruto de 450 m
- Ao consolidar informações operacionais, uma equipe sugere utilizar o balancete combinado c
- Na decretação da liquidação, três ativos têm valores líquidos contábeis de 100, 80 e 50 mi
- Ao examinar informações contábeis de instituição financeira já em liquidação extrajudicial
- Uma instituição autorizada consulta o COSIF para identificar o elenco de contas e a função
- Uma instituição aplica 300 mil em depósito interfinanceiro e pode reconhecer imediatamente
- Em operação de depósito interfinanceiro, a entidade que entrega recursos para aplicação te
- Na classificação atual de títulos de dívida sob a Resolução CMN nº 4.966/2021, a existênci
- Uma controlada colocada à venda imediata, com alienação altamente provável, pode integrar 
- Ao elaborar balancete combinado de um sistema cooperativo, as entidades devem manter crédi
- Uma carteira administrada para receber fluxos de principal e juros e também vender títulos
- Um swap com valor nocional de 2 milhões e valor justo positivo de 20 mil deve reconhecer g
- Um imóvel próprio tem valor contábil líquido de 500 mil e valor justo líquido das despesas
- Para bem recebido na liquidação de instrumento financeiro problemático, a primeira compara
- No balancete combinado, crédito que uma singular concedeu a cliente não participante do si
- Para o balancete combinado de data-base em 2026, a Resolução CMN nº 4.151/2012 continua a 
- A Resolução CMN nº 4.516/2016 disciplina a abertura contábil na decretação da liquidação e
- Uma instituição em liquidação extrajudicial, abrangida pela Resolução CMN nº 4.516/2016, p
- Em data-base sob a Resolução CMN nº 4.966/2021, instrumento de dívida cujo modelo visa rec
- Um bem recebido pela instituição para liquidar crédito problemático e destinado à venda de
- Se um sistema cooperativo possui aplicações e captações recíprocas corretamente identifica
- Na abertura de liquidação extrajudicial sujeita à Resolução CMN nº 4.516/2016, despesas an
- Uma carteira de derivativos classificada com valor justo favorável numa data-base pode apr
- A instituição em liquidação extrajudicial pode dispensar os critérios especiais da Resoluç
```

## Pedido 2: Programas sociais e benefícios do trabalhador na CAIXA (`conhecimentos-bancarios.programas-sociais-pis-fgts-seguro`, 33 tópicos de edital)

```
Matéria: conhecimentos-bancarios.programas-sociais-pis-fgts-seguro (Programas sociais e benefícios do trabalhador na CAIXA)
Capítulos e quantas questões quero em cada um (total 100):
- "1. PIS/Pasep: origem, cotas históricas e abono salarial": 32
- "2. FGTS: depósitos, saques, guias e certificado de regularidade": 18
- "3. Seguro-desemprego: condições, parcelas e processamento": 18
- "4. Bolsa Família: evolução legislativa, renda, benefícios e proteção": 18
- "5. Integração dos benefícios, atendimento e resolução de casos": 14

Já existem (não repita):
- Um trabalhador ingressou em empresa privada e pergunta qual norma originalmente instituiu 
- Pessoa que trabalhou com carteira assinada entre 1977 e 1986 procura valores não retirados
- Em 2026, uma empresa deve calcular o depósito mensal ordinário de FGTS de empregado comum 
- Uma empresa precisa demonstrar situação regular perante o FGTS para procedimento de contra
- Trabalhador formal dispensado sem justa causa requer seguro-desemprego pela primeira vez. 
- Uma família procura ingresso no Bolsa Família em outubro de 2026 e possui renda mensal tot
- Uma trabalhadora possui inscrição PIS antiga, trabalhou quatro meses no ano-base 2024 e at
- Empregado formal foi dispensado sem justa causa e pergunta quando pode requerer seguro-des
- Uma pessoa habilitada ao abono salarial trabalhou durante seis meses computáveis em 2024. 
- Empresa solicita emissão de CRF em 2026, mas ainda não regularizou recolhimentos exigíveis
- Em rescisão sem justa causa ocorrida em junho de 2026, empregador privado abrangido pelo F
- Trabalhador em sua segunda solicitação de seguro-desemprego foi dispensado sem justa causa
- Uma família beneficiária com três integrantes não tem crianças, adolescentes ou gestantes 
- Em 2026, uma pessoa ainda encontra em apostila a Lei nº 10.836/2004 apresentada como funda
- Um trabalhador aderiu ao saque-aniversário e foi dispensado sem justa causa durante sua vi
- Um segurado com salário médio de R$ 4.500,00 solicita seguro-desemprego formal em outubro 
- Uma trabalhadora recebe em 2024 média mensal de R$ 2.800,00, possui PIS há oito anos e tra
- Trabalhador solicita mudança de saque-aniversário para saque-rescisão em agosto de 2026 e 
- Trabalhador é dispensado sem justa causa e pede seguro-desemprego pela terceira vez, tendo
- Uma família iniciou a Regra de Proteção do Bolsa Família em setembro de 2025 devido à rend
- Uma empresa deseja comprovar regularidade do FGTS e possui CRF expedido em 2 de setembro d
- Empregador privado recolherá FGTS da competência setembro de 2026, sem rescisão, pelo FGTS
- Após rescisão por acordo entre empregado e empregador, formalizada nos termos do art. 484-
- Um empregado de empresa privada e um servidor com vínculo Pasep têm direito ao abono 2026 
- A família recebe Bolsa Família, mas, após atualização cadastral em 2026, surge novo empreg
- A Lei Complementar nº 7/1970 instituiu o PIS, enquanto a Lei Complementar nº 8/1970 instit
- Pelo art. 15 da Lei nº 8.036/1990, empregador deve descontar 8% do salário líquido de trab
- O Certificado de Regularidade do FGTS é emitido exclusivamente pela CAIXA e possui prazo d
- Para a segunda solicitação de seguro-desemprego formal, a Lei nº 7.998/1990 exige doze sal
- A Lei nº 14.601/2023 instituiu o Bolsa Família atual, enquanto a Lei nº 10.836/2004 é rele
- A simples inclusão de uma família no Cadastro Único, com renda per capita inferior a R$ 21
- No calendário de 2026, o abono salarial devido a trabalhador de empresa privada vinculada 
- Trabalhador com vínculo registrado em 1999 e sem atividade anterior a 1988 terá, por essa 
- Para o abono salarial pago em 2026, relativo ao ano-base de 2024, o MTE divulgou teto de r
- Empresa privada sujeita ao FGTS Digital, em obrigação mensal comum de 2026, conserva venci
- Empregador emitiu GFD de FGTS Digital, mas não efetuou o pagamento e tem débito exigível. 
- Trabalhador formal dispensado sem justa causa possui apenas oito salários nos últimos doze
- Uma família de três integrantes com renda familiar de R$ 600,00 apresenta renda per capita
- A opção pelo saque-aniversário do FGTS dá ao trabalhador dispensado sem justa causa, em qu
- Empregado em sua primeira solicitação de seguro-desemprego teve vinte e cinco meses de tra
- Família que entrou na Regra de Proteção do Bolsa Família em setembro de 2025 devido a rend
- Na rescisão por acordo de que trata o art. 484-A da CLT, o empregado pode sacar até oitent
- Um trabalhador que solicita abono em 2026 deve demonstrar renda mensal média de até dois s
- Empresa com contrato de aprendizagem deve aplicar percentual de dois por cento da remunera
- No atendimento de 2026, um beneficiário que recebeu Bolsa Família em 2008 deve usar a Lei 
```

## Pedido 3: CTB: habilitação e documentação de veículos (`legislacao-transito.ctb-habilitacao-veiculos`, 33 tópicos de edital)

```
Matéria: legislacao-transito.ctb-habilitacao-veiculos (CTB: habilitação e documentação de veículos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Requisitos de habilitação, formação e documentos do condutor": 20
- "2. Categorias A a E, requisitos especiais, PPD e renovação": 20
- "3. Registro do veículo, CRV, transferência e comunicação de venda": 20
- "4. Licenciamento anual, CRLV, porte e situações especiais": 20
- "5. Equipamentos obrigatórios, normas do Contran e casos integrados": 20

Já existem (não repita):
- Uma pessoa deseja iniciar processo de habilitação e possui identificação regular. Qual con
- Uma pessoa habilitada na categoria B dirige automóvel de massa total de 3.200 kg com cinco
- Uma pessoa concluiu com aprovação sua primeira habilitação. Qual documento receberá inicia
- Um automóvel foi comprado e devidamente registrado pela primeira vez. Qual documento é exp
- O proprietário de automóvel deseja circular regularmente no exercício seguinte ao registro
- Um candidato compara documentos de condutor e seus registros nacionais. Qual associação pe
- Após cumprir um ano de PPD, uma pessoa recebeu apenas uma infração média e não cometeu gra
- Uma pessoa vendeu automóvel; o comprador pretende regularizar o registro. Qual prazo ordin
- Durante fiscalização, a pessoa não apresenta CRLV em papel, mas o sistema acessível confir
- Uma relação de equipamentos obrigatórios para veículos em circulação foi elaborada tomando
- Uma pessoa com habilitação B pretende conduzir veículo elétrico de tração elétrica predomi
- Uma pessoa habilitada em B há dois anos pretende obter a categoria D. Qual período prévio 
- Uma pessoa com 58 anos renova sua CNH sem indicação de doença que recomende redução do pra
- Uma pessoa mudou seu endereço para outro imóvel do mesmo município, sem vender o veículo n
- Após ser convocado para recall de segurança em novembro de 2025, o proprietário não atende
- Uma pessoa habilitada esquece o documento de direção, mas o agente consulta sistema inform
- Uma pessoa usa ciclomotor em via pública e possui apenas categoria B, sem ACC nem A. Qual 
- Comprador não providenciou novo CRV nos trinta dias da transferência. Como o art. 134 orie
- Um condutor B pretende tracionar reboque de PBT 4.000 kg com automóvel devidamente apto pa
- Veículo teve perda total e será destinado à desmontagem, mas possui multas vinculadas. Qua
- Um automóvel comprado em outro município foi corretamente registrado, mas o proprietário d
- Um veículo novo é levado da fábrica ao município de destino ainda sem licenciamento, dentr
- Uma pessoa com 72 anos cadastrada no RNPC requer renovação da CNH. Qual exigência permanec
- Uma pessoa compara a obrigatoriedade de equipamentos entre motocicleta e automóvel, citand
- Uma bicicleta de assistência elétrica atende aos requisitos técnicos do enquadramento como
- Uma pessoa penalmente imputável, alfabetizada e com documento de identificação reúne os re
- Um condutor recebeu sua primeira Permissão para Dirigir. Como a PPD tem validade de dois a
- Uma pessoa conduz automóvel de categoria B, com peso bruto total de 3.300 kg e seis lugare
- Um proprietário conclui o registro inicial de automóvel. O CRV é o documento destinado a s
- O comprador de veículo usado dispõe, na transferência de propriedade, de trinta dias para 
- Em fiscalização, uma pessoa esqueceu o CRLV, mas a autoridade consulta o cadastro e confir
- Uma pessoa com PPD comete duas infrações de natureza média durante o primeiro ano. A ausên
- Uma pessoa habilitada B há pelo menos um ano pretende obter C e não registrou mais de uma 
- Uma pessoa com 71 anos deve renovar exame físico e mental ordinariamente a cada cinco anos
- Um contrato de compra e venda veicular é firmado eletronicamente, por comprador e vendedor
- Uma pessoa compra automóvel, obtém CRV em seu nome e supõe que, por isso, o licenciamento 
- Uma pessoa habilitada C, com menos de setenta anos, precisa observar exame toxicológico de
- Para apurar equipamentos obrigatórios em automóvel de 2026, aplica-se como regulamento ger
- Após a Lei nº 15.503/2026, a categoria B pode abranger veículo de propulsão elétrica ou hí
- Uma pessoa pretende categoria E e já dirige há dois anos com habilitação B, mas nunca poss
- Um proprietário mudou seu domicílio para outro município, mas mantém licenciamento de orig
- Um veículo irrecuperável destinado à desmontagem possui multas vinculadas. A existência de
- Uma pessoa inscrita no RNPC alcança a data de renovação da CNH em 2026. A previsão de reno
- Uma pessoa enquadra bicicleta elétrica devidamente classificada pela Resolução Contran nº 
- Uma fiscalização encontra o farol de veículo sem funcionamento embora o equipamento esteja
```

## Pedido 4: Psicologia organizacional e do trabalho (`psicologia.organizacional-trabalho`, 32 tópicos de edital)

```
Matéria: psicologia.organizacional-trabalho (Psicologia organizacional e do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da psicologia organizacional, cultura e clima": 17
- "Recrutamento e seleção: métodos, ética e serviço público": 17
- "Avaliação de desempenho, competências e devolutiva": 17
- "Treinamento, desenvolvimento e educação corporativa": 17
- "Qualidade de vida, saúde do trabalhador e prevenção": 16
- "Ergonomia cognitiva e desenho do trabalho": 16

Já existem (não repita):
- Na análise da psicologia organizacional, uma falha sistêmica de informação pode influencia
- Cultura e clima organizacionais são equivalentes, pois ambos designam apenas a satisfação 
- Artefatos culturais são sinais visíveis da organização, mas não revelam necessariamente to
- Um resultado elevado de clima organizacional obtido apenas com respostas de uma chefia com
- O recrutamento procura atrair pessoas potencialmente qualificadas, enquanto a seleção comp
- O fato de um teste psicológico estar favorável no SATEPSI dispensa o psicólogo de analisar
- Conforme a Constituição, a investidura em cargo ou emprego público depende, como regra, de
- Perguntas sobre planos de gravidez podem ser usadas como critério geral de seleção para fa
- O efeito de recência ocorre quando o avaliador dá peso excessivo aos eventos próximos ao f
- Na avaliação de competências, a simples posse de certificado de curso comprova que o traba
- O Decreto nº 7.133/2010 estabelece critérios aplicáveis indiscriminadamente ao pagamento d
- A avaliação institucional pode verificar metas organizacionais, enquanto a avaliação indiv
- Antes de prescrever treinamento, é adequado verificar se a queda de desempenho decorre de 
- Uma pesquisa de satisfação realizada ao final de uma capacitação comprova que os participa
- O PDP integra os instrumentos da PNDP no âmbito da administração pública federal alcançada
- Oferecer aulas de meditação dispensa a organização de examinar metas inalcançáveis e sobre
- A partir de 26 de maio de 2026, fatores de risco psicossociais relacionados ao trabalho pa
- A política de bem-estar e QVT dos profissionais da educação é corretamente identificada pe
- Ergonomia cognitiva limita-se a selecionar cadeiras e recomendar postura, sem examinar mem
- Interrupções contínuas e alertas redundantes podem aumentar a carga cognitiva e tornar men
- Uma autarquia pesquisa percepções de justiça, apoio da chefia e comunicação após uma reorg
- Uma empresa divulga inovação como valor, mas pune toda tentativa de melhoria que não dá ce
- Um setor apresenta alta rotatividade e baixa qualidade de atendimento. Qual investigação e
- Assinale a alternativa que apresenta uma característica típica da cultura organizacional, 
- Para investigar se a comunicação interna melhora após uma intervenção, qual escolha oferec
- O recrutamento externo se diferencia do interno porque:
- Uma banca busca reduzir variações entre entrevistadores de candidatos à mesma função. A té
- Uma organização utiliza teste com pontuação muito estável, mas sem evidência de que a pont
- Uma secretaria pretende substituir concurso para provimento efetivo por entrevista informa
- Um psicólogo escolherá teste para seleção de profissionais de saúde. Qual conjunto de caut
- Uma avaliação mensal mede volume de processos, mas ignora taxa de erros. Qual aperfeiçoame
- Ao avaliar desempenho, a chefia considera uma pessoa excelente em todas as competências po
- Ao construir escala comportamental ancorada, uma organização deve priorizar:
- Um servidor alcança meta individual, enquanto a unidade fica abaixo da meta global. À luz 
- Um sistema 360° reúne opiniões de chefia, pares e subordinados. Assinale a avaliação tecni
- Na análise de necessidades de treinamento, a primeira pergunta é se:
- Após curso de atendimento, a avaliação verifica se servidores passaram a empregar nova téc
- O objetivo de aprendizagem mais operacionalizado entre os seguintes é:
- Um setor faz treinamento de novo sistema; durante semanas, entretanto, chefias impedem seu
- Em um programa federal de desenvolvimento de servidores regido pela PNDP, o PDP deve:
- Uma instituição oferece lazer, mas mantém metas inalcançáveis e quadro insuficiente. A crí
- Uma organização reorganiza fluxos e reduz interrupções que provocavam estresse e erros. No
- Uma equipe de SST pretende atender à NR-1 quanto a fatores psicossociais relacionados ao t
- Sobre a Lei nº 14.681/2023, a alternativa correta é:
- Ao estudar absenteísmo e presenteísmo, o pesquisador deve reconhecer que:
- Um atendente erra repetidamente ao copiar código entre duas telas. A medida mais diretamen
- Qual situação ilustra fadiga de alertas?
- Na análise ergonômica, um funcionário improvisa planilha porque o sistema oficial não cont
- Na investigação de um incidente, identificou-se clique acidental em botão adjacente à opçã
- Uma central de regulação quer monitorar ergonomia cognitiva. Qual conjunto oferece análise
```

## Pedido 5: Prótese dentária: técnicas laboratoriais (`odontologia.protese-dentaria-tecnico`, 32 tópicos de edital)

```
Matéria: odontologia.protese-dentaria-tecnico (Prótese dentária: técnicas laboratoriais)
Capítulos e quantas questões quero em cada um (total 100):
- "Organização do laboratório, ética e biossegurança": 20
- "Materiais, moldes, modelos e troquéis": 20
- "Prótese total e prótese parcial removível": 20
- "Prótese fixa, fundição, cerâmicas e acabamento": 20
- "Fluxo digital CAD/CAM e controle de qualidade": 20

Já existem (não repita):
- Na organização de um laboratório de prótese dentária, qual atividade corresponde tipicamen
- Uma peça usada foi encaminhada ao laboratório sem comprovação de limpeza e desinfecção. À 
- Um laboratório independente deseja manter cadeira odontológica para atender diretamente pe
- Na rastreabilidade mínima de serviços laboratoriais, a RDC Anvisa nº 1.002/2025 exige regi
- Qual combinação representa uma barreira adequada ao risco durante o desgaste de uma armaçã
- Na classificação usual dos gessos odontológicos, o tipo IV é associado principalmente a:
- Ao usar um mesmo gesso de trabalho, o técnico adicionou água em excesso à proporção recome
- Em modelo destinado à fabricação de coroa, a margem cervical do troquel está quebrada. O q
- O alginato foi deixado em condições inadequadas e alterou sua dimensão antes do vazamento.
- Ao diferenciar articulador e delineador, qual enunciado está correto?
- Uma PPR apresenta áreas posteriores edêntulas de extremidade livre nos dois lados. Na clas
- Um conector maior metálico de PPR tem como função básica:
- O técnico selecionou um grampo com retentividade exagerada, que exige força excessiva para
- No processamento convencional de uma prótese total acrílica, qual alternativa descreve uma
- Sobre uma PPR classificada como Kennedy IV, é correto afirmar que:
- Na construção de uma ponte fixa, denomina-se pôntico:
- Por que o revestimento de fundição deve ser compatível com a liga usada?
- Após fundição, uma coroa metálica apresenta porosidade e margem aberta. Qual providência é
- Qual distinção sobre materiais cerâmicos utilizados em prótese fixa é correta?
- Na avaliação final de uma coroa, observou-se adaptação marginal inadequada, embora o glaze
- Em odontologia digital, CAD e CAM correspondem, respectivamente, a:
- Um técnico precisa reproduzir um modelo por fabricação aditiva em resina fotopolimerizável
- Um arquivo STL obtido de escaneamento será usado para projetar uma coroa. Qual limitação d
- Qual sequência descreve adequadamente um fluxo CAD/CAM de zircônia pré-sinterizada?
- Diante de duas coroas digitais produzidas com o mesmo desvio sistemático de adaptação em r
- A legislação brasileira permite ao técnico em prótese dentária prestar assistência direta 
- No laboratório, a separação entre recepção de materiais potencialmente contaminados e área
- A RDC Anvisa nº 1.002/2025 prevê área própria de limpeza e desinfecção e registros dos ser
- Por serem laboratórios, os LPD estão automaticamente dispensados de todas as exigências sa
- O gesso odontológico tipo IV é classicamente associado a alta resistência e baixa expansão
- Adicionar água acima da recomendação costuma diminuir os espaços vazios do gesso e aumenta
- Troquéis removíveis precisam reassentar em posição estável e reprodutível para que a verif
- O arco facial é utilizado para selecionar a cor dos dentes artificiais, enquanto o delinea
- Em PPR, retenção e suporte são equivalentes: ambos significam apenas resistência à saída d
- Uma arcada com área edêntula posterior unilateral sem dente distal apresenta padrão de Ken
- O polimento intenso da superfície interna da base total é indicado como rotina, mesmo quan
- No método convencional de prótese total, a eliminação da cera na mufla cria espaço destina
- Uma margem cervical aberta em coroa pode ser considerada resolvida quando a superfície rec
- Na fundição pela técnica da cera perdida, o padrão é eliminado do revestimento por aquecim
- Toda zircônia odontológica deve obrigatoriamente receber o mesmo condicionamento superfici
- Um revestimento incompatível com a faixa de temperatura da liga pode comprometer a qualida
- Na manufatura protética, a fresagem retira material do bloco, ao passo que a impressão 3D 
- Arquivos STL convencionais transportam, obrigatoriamente, o tom clínico exato de cada dent
- Uma resina impressa que sai visualmente sólida pode dispensar lavagem e pós-cura, pois a i
- Uma fresadora muito repetível pode produzir, em série, peças dimensionalmente incorretas s
```

## Pedido 6: Oficial de justiça: atos, diligências e responsabilidades (`direito.oficial-de-justica-diligencias`, 32 tópicos de edital)

```
Matéria: direito.oficial-de-justica-diligencias (Oficial de justiça: atos, diligências e responsabilidades)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios, atribuições e documentação das diligências": 20
- "2. Citações, intimações, notificações e modalidades excepcionais": 23
- "3. Atos constritivos: arresto, penhora, sequestro, busca e arrombamento": 20
- "4. Reintegração, imissão, entrega de bens e controle de nulidades": 23
- "5. Responsabilidades civis e criminais e resposta a incidentes": 14

Já existem (não repita):
- Ao concluir pessoalmente uma intimação, o oficial relata no mandado local, data, horário e
- Durante comunicação processual, uma parte apresenta proposta de autocomposição. Após sua c
- Em ação civil, duas buscas no domicílio não encontram o réu; há circunstâncias concretas d
- Após concretizada uma citação com hora certa no processo civil, quem encaminha correspondê
- Não se consegue localizar réu civil após buscas adequadas e o juízo decide pela citação po
- Um oficial não encontra o executado e pratica arresto de bens suficientes para garantir a 
- O executado tranca o imóvel para impedir penhora. O oficial constata o obstáculo e não pos
- Em disputa possessória, o demandante demonstra que exercia posse e foi totalmente privado 
- Uma parte questiona certidão de citação porque ninguém assinou ciência, apesar de o oficia
- A diligência de citação é realizada em feriado, fora do horário usual, sem ingresso forçad
- Um bem penhorado precisa de avaliação e a natureza do objeto exige conhecimento técnico qu
- Um imóvel é adjudicado na execução e o mandado busca transferir ao adjudicatário sua posse
- No cumprimento de decisão que impõe obrigação de fazer, é expedido mandado de busca e apre
- Após executar ordem de retorno ao trabalho, o oficial verifica recusa do empregador em per
- Em processo civil, a parte alega vício formal de intimação, mas a finalidade foi alcançada
- Uma diligência é retardada de propósito para favorecer pessoa próxima, por sentimento pess
- Após uma penhora, o exequente sustenta que o oficial deve apreender fisicamente imóvel reg
- A determinação de busca e apreensão domiciliar está prestes a ser cumprida às 22h, sem fla
- Em execução por quantia, o oficial realizou arresto, encontrou depois o executado, complet
- Um investigado se recusa a comparecer para interrogatório e é cogitada sua condução coerci
- Na arrematação judicial de imóvel, houve assinatura regular do auto, mas ainda se discute 
- Um agente certifica como realizada pessoalmente uma comunicação que não ocorreu, visando a
- Um particular ameaça seriamente o oficial para impedir cumprimento de penhora legal, mas o
- O oficial altera artificiosamente vestígios de uma diligência para eximir-se de responsabi
- Durante uma diligência, o oficial certifica data, hora, endereço e circunstâncias efetivam
- Como os atos do oficial têm fé pública, a certidão de uma diligência cria presunção absolu
- No procedimento civil, a citação com hora certa pressupõe duas buscas sem localizar o cita
- A citação por edital no processo civil deve ter prazo fixo de quinze dias, idêntico ao est
- A penhora recai sobre bens suficientes à satisfação da execução e não representa, por si m
- Para arrombar portas fechadas com objetivo de impedir penhora, o oficial pode agir antes d
- O art. 560 do CPC distingue a manutenção, diante de turbação, da reintegração de posse, ca
- Uma recusa de assinar a contrafé equivale, por sua própria natureza, a violência ou ameaça
- Mesmo havendo recusa de contrafé por quem foi encontrado e regularmente cientificado, o ar
- No processo civil, uma vez realizada a citação com hora certa, o art. 254 atribui ao ofici
- Após arresto do art. 830 e citação aperfeiçoada, o transcurso do prazo de pagamento sem ad
- Como o art. 280 do CPC prevê nulidade de citações e intimações irregulares, os arts. 277 e
- A prevaricação do art. 319 do Código Penal exige finalidade de satisfazer interesse ou sen
- O CPC art. 212, § 2º, permite que se realize qualquer busca domiciliar judicial depois das
- A localização de imóvel em outra comarca não impede, por si, a penhora por termo nos autos
- O precedente formado nas ADPFs 395 e 444 proibiu condução coercitiva de investigado para i
- Uma certidão afirma ter recebido o interessado cópia de mandado que nunca lhe foi entregue
- A Lei 13.869/2019 presume a finalidade específica de abuso de autoridade sempre que uma di
- Se alguém recebe imóvel por adjudicação judicial, a carta de adjudicação acompanhada de ma
- A responsabilização civil regressiva do oficial prevista no art. 155 do CPC nasce de qualq
```

## Pedido 7: Atividades espaciais brasileiras (`legislacao.atividades-espaciais-brasil`, 31 tópicos de edital)

```
Matéria: legislacao.atividades-espaciais-brasil (Atividades espaciais brasileiras)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei nº 14.946/2024: atividades espaciais e autoridades competentes": 18
- "2. Licenciamento, registro, supervisão, segurança e responsabilidade espacial": 18
- "3. Agência Espacial Brasileira, PNDAE e SINDAE: organização institucional": 28
- "4. Salvaguardas tecnológicas e lançamento a partir de Alcântara": 18
- "5. PNAE 2022–2031: estratégia, fatores, setores e investimentos": 18

Já existem (não repita):
- Empresa brasileira desenvolve, no exterior, componente de satélite com participação de seu
- Uma empresa deseja obter títulos para realizar operações espaciais civis no Brasil. Confor
- Uma operação espacial servirá simultaneamente ao monitoramento ambiental civil e a uma apl
- Um operador foi informado de incidente em sua atividade espacial às 9 horas da segunda-fei
- A entidade que coordena o Registro Espacial Brasileiro deve reunir dados e informações de 
- Após acidente em operação espacial, investigadores do Sipae estudam causas técnicas para e
- Uma entidade deseja identificar a lei que criou a Agência Espacial Brasileira, definindo-a
- Em uma reunião sobre o Sistema Nacional de Desenvolvimento das Atividades Espaciais, qual 
- Em negociação internacional sobre lançamentos em Alcântara, surgiu dúvida acerca da finali
- Uma equipe quer identificar o ato que aprovou o PNAE referente à década 2022–2031. Qual as
- Uma empresa brasileira recebeu licença de autoridade estrangeira e pretende operar sistema
- Três pessoas jurídicas integram um consórcio operador de missão espacial e designam uma lí
- Após suspensão de autorização, um operador mantém satélite em órbita e declara não possuir
- Um projeto espacial experimental ingressará em fase de operação e seu responsável alega qu
- Uma universidade deseja integrar o SINDAE como entidade participante para executar ação es
- Um estudo apresenta o Decreto nº 10.469/2020 como estrutura regimental vigente da AEB em 2
- Ao mapear uma instalação do Centro Espacial de Alcântara, a equipe precisa distinguir área
- Um edital interno de projeto prevê acesso de brasileiros a dados técnicos protegidos por e
- Um gestor classifica os aportes de uma década em missão orbital, desenvolvimento de lançad
- Uma nota técnica sustenta que os cenários 0, 50, 100, 200 e 1000 do PNAE asseguram automat
- Uma empresa pretende transferir a terceiro o comando e controle de satélite objeto de lice
- Após sinistro durante lançamento licenciado, o operador encontra danos acima do limite def
- A autoridade espacial acompanha lançamento e identifica infração de execução sem título. Q
- Uma minuta de PNAE apresenta o objetivo de ampliar a independência nacional no desenvolvim
- Uma empresa apresenta o acordo de salvaguardas como autorização para empregar qualquer rec
- A Lei nº 14.946/2024 inclui a remoção de detritos espaciais entre as atividades espaciais 
- Na Lei nº 14.946/2024, a AEB responde por toda autorização de voo de veículo lançador no e
- O Resbra, coordenado pela AEB, pode registrar informações de artefatos, operadores, ativid
- A Lei nº 8.854/1994 criou a AEB como empresa pública de natureza militar, sem autonomia ad
- A Política Nacional de Desenvolvimento das Atividades Espaciais, atualizada pelo Decreto n
- O acordo de salvaguardas de Alcântara concede transferência imediata de tecnologia de lanç
- Se um lançamento possui dois Estados lançadores, a definição do Estado de registro será ob
- O art. 15 da Lei nº 14.946/2024 exige as mesmas garantias reais e securitárias dos entes d
- O Sipae tem finalidade exclusivamente preventiva, e suas análises e conclusões não podem s
- O órgão central do SINDAE é o INPE, enquanto a AEB integra a categoria de entidade partici
- O Decreto nº 11.192/2022 organiza a AEB com Presidência, órgãos de apoio, diretorias e uni
- No acordo de Alcântara, as áreas restritas e controladas são ambas parcelas de território 
- O PNAE 2022–2031 distingue dimensões estratégica, tática e setorial e organiza propostas e
- A Lei nº 14.946/2024 afastou expressamente todas as competências legalmente atribuídas à A
- Para atividades espaciais civis, a AEB deve regular a mitigação de detritos, enquanto a co
- Se o operador civil transfere apenas o comando e controle do artefato espacial a outra ent
- A diretriz da PNDAE sobre escopo dos programas por resultados finais exige considerar aces
- Conforme o acordo promulgado pelo Decreto nº 10.220/2020, a obtenção de licença norte-amer
- No PNAE 2022–2031, o OEE.6 corresponde à não dependência no desenvolvimento e controle de 
- A norma de criação da AEB e a atual estrutura regimental são a mesma espécie de ato: ambas
```

## Pedido 8: Princípios básicos (`ciencias.principios-basicos`, 31 tópicos de edital)

```
Matéria: ciencias.principios-basicos (Princípios básicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fluidos em repouso, densidade e conceito de pressão": 25
- "2. Lei de Stevin, pressão absoluta, vasos comunicantes e forças em paredes": 24
- "3. Princípio de Pascal e equilíbrio de dispositivos hidráulicos": 17
- "4. Empuxo, peso aparente e princípio de Arquimedes": 17
- "5. Equilíbrio de corpos, aplicações de pressão e integração dos princípios": 17

Já existem (não repita):
- Uma amostra homogênea possui massa de 2 kg e volume de 0,002 m³. Qual é sua massa específi
- Uma placa recebe força normal uniformemente distribuída de 240 N sobre área de 0,060 m². Q
- Uma tabela expressa a massa específica de um líquido como 0,80 g/cm³. Em unidades do Siste
- Em água homogênea de densidade 1.000 kg/m³, um ponto está 2 m abaixo da superfície. Adote 
- A superfície de um tanque está a 100 kPa absolutos. Na profundidade considerada, a coluna 
- Um tubo em U comunica dois braços de larguras diferentes, preenchidos pelo mesmo líquido e
- Uma prensa hidráulica ideal possui êmbolos de áreas 2 cm² e 8 cm² no mesmo nível. Aplicam-
- Um bloco totalmente submerso desloca 0,003 m³ de água de massa específica 1.000 kg/m³. Use
- Uma peça com massa de 600 g ocupa 1.000 cm³, incluindo possíveis cavidades. Sua massa espe
- Num tubo em U com líquidos imiscíveis abertos à atmosfera, coluna de óleo de 800 kg/m³ tem
- Dois pontos no mesmo líquido homogêneo estão respectivamente a 2 m e 5 m de profundidade. 
- Um recipiente fechado com líquido parado recebe acréscimo de pressão de 8 kPa por um pistã
- Os diâmetros de dois êmbolos circulares de prensa ideal no mesmo nível são 2 cm e 6 cm. O 
- Em prensa hidráulica ideal incompressível, o êmbolo menor tem área 2 cm² e desce 5 cm. Se 
- Uma parede vertical retangular tem 2 m de largura e 3 m de altura molhada, a partir da sup
- Um manômetro indica pressão relativa de −15 kPa dentro de recipiente. A pressão atmosféric
- Um bloco homogêneo com massa específica de 650 kg/m³ flutua livremente em água de 1.000 kg
- Um sólido pesa 120 N no ar e, ao ficar completamente imerso e parado em líquido, recebe em
- Um sólido atravessa uma interface entre água e óleo: desloca 0,002 m³ de água de 1.000 kg/
- Um corpo inteiramente submerso pesa 40 N e recebe empuxo de 60 N. Ele é preso por corda ao
- Uma tampa horizontal de área 0,25 m² está a 4 m de profundidade em água com densidade 1.00
- Um material homogêneo de massa específica 900 kg/m³ flutua em líquido uniforme de 1.200 kg
- Três recipientes de formas diferentes contêm água até a mesma altura de 2 m; seus fundos t
- Uma parede retangular vertical começa na superfície de um tanque e termina a profundidade 
- Num manômetro em U ideal, a coluna de água tem 40 cm e a coluna de óleo tem 50 cm, ambas m
- Um líquido e um gás podem ser tratados como fluidos porque, diferentemente de um sólido rí
- Quando uma mesma força normal é aplicada a duas superfícies, a pressão é maior naquela de 
- A massa específica de 0,9 g/cm³ corresponde a 900 kg/m³, pois o fator de conversão entre a
- Dois pontos situados à mesma profundidade, em recipientes de larguras distintas e com o me
- Num tanque aberto à atmosfera, a diferença de pressão entre a superfície livre e ponto sit
- Em vasos comunicantes com dois líquidos imiscíveis de densidades distintas, abertos à mesm
- O princípio de Pascal assegura que o acréscimo de pressão aplicado a um fluido confinado e
- Como o princípio de Pascal transmite pressão sem diminuição, a pressão total do líquido pa
- Em uma prensa hidráulica ideal de líquido incompressível, quando um êmbolo maior produz fo
- Se o diâmetro de um êmbolo circular de saída é três vezes o diâmetro do êmbolo de entrada,
- Um corpo metálico que afunda em água ainda recebe força de empuxo; o fato de afundar indic
- Um dinamômetro sustenta um bloco completamente imerso e parado sem contato com o fundo. A 
- Um corpo homogêneo com densidade de 800 kg/m³ flutua em água de 1.000 kg/m³, sem outras fo
- Um corpo de pequena massa flutuará mais facilmente que outro de maior massa, independentem
- Em um mesmo líquido homogêneo e estático, a diferença de pressão entre dois pontos separad
- Numa parede vertical retangular cujo topo está na superfície da água, a pressão hidrostáti
- Um corpo imerso recebe empuxo de 50 N e pesa 35 N. Para permanecer em equilíbrio preso por
- Num líquido homogêneo aproximadamente incompressível, um mesmo corpo rígido completamente 
- Se um corpo desloca simultaneamente parcelas de água e óleo imiscíveis, seu empuxo total e
- Se dois recipientes de formatos diferentes têm mesma área de fundo e recebem água até a me
```

## Pedido 9: Fruticultura, olericultura e pós-colheita (`agronomia.fruticultura-olericultura-pos-colheita`, 30 tópicos de edital)

```
Matéria: agronomia.fruticultura-olericultura-pos-colheita (Fruticultura, olericultura e pós-colheita)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento de pomares e propagação de frutíferas": 20
- "2. Fenologia, ecofisiologia, poda, condução e raleio": 20
- "3. Principais frutíferas cultivadas no Brasil": 20
- "4. Olericultura a campo e em cultivo protegido": 20
- "5. Colheita, fisiologia pós-colheita, armazenamento e qualidade": 20

Já existem (não repita):
- Em um hectare integralmente útil, um projeto adota linhas espaçadas de 5 m e plantas dista
- Um técnico recomenda enxertia em uma nova área de citros. Qual descrição identifica corret
- Um viveirista deseja multiplicar rapidamente um genótipo selecionado de frutífera mantendo
- Qual operação utiliza uma gema isolada como material de copa?
- No ciclo reprodutivo de frutíferas, a transferência do pólen até a superfície receptora é 
- Após safra com excesso de maçãs pequenas, deseja-se regular a carga de frutos para melhora
- Em uma prova de botânica agrícola, pergunta-se qual descrição da bananeira é correta. Assi
- Qual associação entre cultura e comportamento pós-colheita está correta?
- A batata inglesa e a batata-doce diferem quanto ao órgão de reserva comercial. Qual enunci
- Ao comparar estufa agrícola e hidroponia, qual conclusão é tecnicamente adequada?
- Um lote de banana verde fisiologicamente desenvolvida será transportado e amadurecido depo
- Um pomar de 2 hectares adotará 4 m entre linhas e 2,5 m entre plantas, porém 15% da área s
- Uma cultivar de macieira floresce de forma irregular em região de inverno pouco frio. O di
- Um agrônomo remove um ramo inteiro desde sua inserção para melhorar entrada de luz na copa
- Um produtor colherá uvas de mesa para venda distante. Qual variável merece atenção especia
- No cultivo de alface, temperaturas acima da faixa de conforto da cultivar podem acelerar a
- Durante o manejo de solução nutritiva em cultivo protegido, a condutividade elétrica aumen
- Um transporte de folhosas chegou ao mercado com folhas murchas, sem redução equivalente do
- Em uma câmara frigorífica com fruta tropical sensível, aparecem depressões superficiais e 
- Uma empresa monitora de modo contínuo e ajusta oxigênio e dióxido de carbono em uma câmara
- Uma área nova terá pomares adensados. O que deve ser avaliado para evitar sombreamento e p
- Uma macieira produz muitas flores, mas os frutos não se estabelecem. É observado pólen viá
- Um pomar de pessegueiros sem raleio oferece maior massa colhida, mas vende menor proporção
- Em um experimento, pepinos partenocárpicos são cultivados em ambiente protegido sem entrad
- Um lote de 200 kg de frutas passa a pesar 188 kg após armazenamento sem registro de descar
- Uma rede deseja combinar ventilação de embalagens com pré-resfriamento por ar forçado. Qua
- O espaçamento entre linhas e entre plantas permite estimar a densidade teórica de um pomar
- Sementes provenientes de polinização cruzada são, por definição, clones geneticamente idên
- Na enxertia, a compatibilidade dos materiais e a adequada união dos tecidos são relevantes
- Polinização e fecundação designam rigorosamente a mesma etapa do ciclo reprodutivo das ang
- O raleio de frutos pode melhorar o calibre comercial dos frutos remanescentes ao reduzir a
- A banana é fruta não climatérica e, por isso, não apresenta amadurecimento após ser retira
- A videira pode ser conduzida em espaldeira ou latada, conforme finalidade, cultivar e sist
- A cenoura é hortaliça de raiz e o tomate é classificado agronomicamente como hortaliça-fru
- O cultivo protegido dispensa ventilação e monitoramento térmico porque a cobertura impede 
- A transpiração de hortaliças após a colheita pode diminuir a massa fresca e provocar murch
- Em frutíferas temperadas, todas as cultivares possuem requisito idêntico de acúmulo de fri
- A poda de desponte encurta o ramo, enquanto a poda por desbaste retira o ramo em sua inser
- A micropropagação de frutíferas sempre implica introdução de genes exógenos no genoma das 
- O pseudocaule da bananeira é formado principalmente por bainhas foliares superpostas.
- A condutividade elétrica da solução nutritiva informa isoladamente a concentração de cada 
- É possível ocorrer dano por frio em frutas sensíveis mesmo quando a temperatura permanece 
- O manejo de carga por raleio é sempre economicamente inadequado quando diminui o número de
- Um fruto não climatérico é necessariamente insensível ao etileno e cessa toda atividade re
- Em uma cultivar de pepino partenocárpica, pode haver crescimento de frutos sem fecundação,
- A presença de frutos mais pesados após raleio demonstra, por si só, aumento da produção to
- Em área com 10% de corredores não cultiváveis, multiplicar a densidade teórica pelo total 
- Armazenamento em atmosfera controlada e armazenamento em atmosfera modificada são expressõ
- O amadurecimento de frutos climatéricos pode continuar após a colheita, mas isso não signi
- O sucesso do pomar adensado depende apenas de aumentar o número de mudas, pois a entrada d
```

## Pedido 10: Constituição do Estado do Pará (`para.constituicao-estadual`, 30 tópicos de edital)

```
Matéria: para.constituicao-estadual (Constituição do Estado do Pará)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios, soberania popular, organização do Estado e administração": 20
- "2. Servidores, militares, regiões e Municípios": 20
- "3. Poder Legislativo, processo legislativo e Tribunais de Contas": 20
- "4. Executivo, Judiciário, funções essenciais e segurança pública": 20
- "5. Orçamento, ordem econômica, questão agrária, ambiente e ordem social": 20

Já existem (não repita):
- Segundo a Constituição do Estado do Pará, podem requerer plebiscito ou referendo:
- A iniciativa popular de lei estadual no Pará exige subscrição por, no mínimo, meio por cen
- Sobre a capital do Estado do Pará, a Constituição Estadual estabelece que:
- A Constituição do Pará considera o arquipélago do Marajó área de proteção ambiental, deven
- Além dos princípios da Constituição Federal, a Constituição do Pará prevê expressamente, p
- Na Constituição do Pará, a despesa com publicidade de cada Poder não pode exceder dois por
- Sobre o concurso público na Constituição do Pará, é correto afirmar:
- Pela Constituição do Pará, viola direito constitucional o agente público que retarda a nom
- Aos militares do Estado do Pará a Constituição Estadual assegura, entre outros direitos:
- A lei orgânica municipal, segundo a Constituição do Pará, será votada em dois turnos, com 
- Na Constituição do Pará, a Câmara Municipal criará Comissão Parlamentar de Inquérito, inde
- O parecer prévio do Tribunal de Contas dos Municípios sobre as contas do Prefeito só deixa
- O Estado do Pará pode intervir nos Municípios quando:
- A Assembleia Legislativa do Pará reunir-se-á anualmente, independentemente de convocação, 
- Na constituição da Mesa e de cada comissão da Assembleia Legislativa do Pará, haverá, em q
- Perde o mandato, pela Constituição do Pará, o Deputado que deixar de comparecer, em cada s
- Ocorrendo vaga de Deputado Estadual e não havendo suplente, far-se-á eleição para preenchê
- A Constituição do Pará pode ser emendada mediante proposta de:
- A proposta de emenda à Constituição do Pará é discutida e votada em dois turnos e consider
- Sobre o veto na Constituição do Pará, assinale a correta.
- Os Tribunais de Contas do Estado e dos Municípios do Pará têm, cada um, sete Conselheiros,
- O Tribunal de Contas do Estado do Pará deve elaborar parecer prévio sobre as contas anuais
- Em caso de impedimento do Governador e do Vice-Governador, serão chamados sucessivamente a
- Ocorrendo a vacância dos cargos de Governador e Vice-Governador no último ano do período g
- Segundo a Constituição do Pará, o Governador e o Vice-Governador:
- Admitida a acusação contra o Governador do Pará por dois terços da Assembleia, ele será ju
- Sobre o Conselho do Estado previsto na Constituição do Pará, é correto afirmar:
- A Constituição do Pará determina que, para dirimir conflitos fundiários, o Tribunal de Jus
- Sobre a Justiça de Paz no Pará, assinale a correta.
- Fica autorizada a criação do Tribunal de Justiça Militar no Pará, por lei complementar de 
- Quando se tratar de lei ou ato normativo municipal, podem propor ação direta de inconstitu
- Se, decorridos quinze dias do recebimento da lista tríplice, o Governador não escolher o P
- Pela Constituição do Pará, as polícias civil e militar não intervirão em questão possessór
- A Polícia Penal do Pará, vinculada à Secretaria de Estado de Administração Penitenciária, 
- Na Constituição do Pará, o projeto de lei de diretrizes orçamentárias deve ser apresentado
- Pela Constituição do Pará, a concessão de terras do Estado com área acima de mil e quinhen
- A política agrícola, agrária e fundiária prevista na Constituição do Pará determina que as
- A Constituição do Pará proíbe a pesca de arrasto ou outra modalidade predatória nos rios, 
- A Constituição do Pará prevê que os grandes projetos instalados no Estado financiem ações 
- São isentas de impostos estaduais, pela Constituição do Pará, as operações de transferênci
- No capítulo sobre os índios, a Constituição do Pará determina que:
- A Constituição do Pará garante gratuidade nos transportes coletivos aos maiores de sessent
- Sobre a educação e a ciência na Constituição do Pará, é correto afirmar:
```

## Pedido 11: Estatuto do Índio (Lei nº 6.001/1973) (`legislacao.estatuto-do-indio-lei-6001`, 30 tópicos de edital)

```
Matéria: legislacao.estatuto-do-indio-lei-6001 (Estatuto do Índio (Lei nº 6.001/1973))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Finalidade, definições e leitura constitucional": 20
- "2. Direitos civis, tutela, registro e trabalho": 20
- "3. Terras indígenas": 20
- "4. Patrimônio, renda, educação, cultura e saúde": 20
- "5. Normas penais e disposições gerais": 20

Já existem (não repita):
- Segundo a Lei nº 6.001/1973, índio ou silvícola é:
- Pela Lei nº 6.001/1973, os índios que, em contato com grupos estranhos, conservam parte da
- O índio integrado, segundo a Lei nº 6.001/1973, perde a condição de índio por estar incorp
- De acordo com a Lei nº 6.001/1973, cumpre à União, aos Estados e aos Municípios, nos limit
- Sobre a relação entre o Estatuto do Índio e a Constituição de 1988, é correto afirmar que:
- Pela Lei nº 6.001/1973, são respeitados os usos, costumes e tradições das comunidades indí
- Segundo a Lei nº 6.001/1973, a tutela dos índios e das comunidades indígenas ainda não int
- Pela Lei nº 6.001/1973, são nulos os atos praticados entre índio não integrado e pessoa es
- Para requerer ao juiz sua liberação do regime tutelar, o índio deve preencher, entre outro
- Segundo a Lei nº 6.001/1973, a emancipação de uma comunidade indígena quanto ao regime tut
- Pela Lei nº 6.001/1973, o órgão de assistência pode reconhecer ao índio a condição de inte
- No Estatuto do Índio, o registro administrativo de nascimentos e óbitos mantido pelo órgão
- Segundo a Lei nº 6.001/1973, será nulo o contrato de trabalho ou de locação de serviços re
- Pela Lei nº 6.001/1973, não haverá discriminação entre trabalhadores indígenas e os demais
- De acordo com a Lei nº 6.001/1973, as terras ocupadas pelos índios:
- Pelo Estatuto do Índio, o reconhecimento do direito dos índios à posse permanente das terr
- Segundo a Lei nº 6.001/1973, contra a demarcação administrativa de terras indígenas:
- Pela Lei nº 6.001/1973, a intervenção da União em área indígena, determinada por decreto d
- No Estatuto do Índio, a remoção de grupo tribal durante intervenção da União:
- Segundo a Lei nº 6.001/1973, as áreas reservadas estabelecidas pela União podem organizar-
- Pela Lei nº 6.001/1973, território federal indígena é a unidade administrativa subordinada
- No Estatuto do Índio, a área destinada à exploração agropecuária, administrada pelo órgão 
- Pela Lei nº 6.001/1973, o índio, integrado ou não, que ocupe como próprio, por dez anos co
- Segundo a Lei nº 6.001/1973, as terras indígenas:
- Pelo Estatuto do Índio, os grupos tribais ou comunidades indígenas são partes legítimas pa
- Segundo a Lei nº 6.001/1973, NÃO integram o Patrimônio Indígena:
- Pela Lei nº 6.001/1973, as riquezas do solo nas áreas indígenas somente pelos índios podem
- Segundo a Lei nº 6.001/1973, os bens e rendas do Patrimônio Indígena:
- Pela Lei nº 6.001/1973, a alfabetização dos índios far-se-á exclusivamente em língua portu
- No Estatuto do Índio, a assistência aos menores indígenas para fins educacionais será pres
- Pela Lei nº 6.001/1973, a renda indígena, resultante da aplicação de bens e utilidades do 
- Segundo a Lei nº 6.001/1973, na infância, na maternidade, na doença e na velhice deve ser 
- No caso de condenação de índio por infração penal, a Lei nº 6.001/1973 determina que:
- Pela Lei nº 6.001/1973, é tolerada a aplicação, pelos grupos tribais, de sanções penais ou
- Entre os crimes contra os índios e a cultura indígena previstos no Estatuto, aquele punido
- As penas dos crimes do art. 58 do Estatuto do Índio são agravadas de um terço quando o cri
- Segundo o art. 59 da Lei nº 6.001/1973, a pena será agravada de um terço nos crimes contra
- Pela Lei nº 6.001/1973, nenhuma medida judicial será concedida liminarmente em causas que 
- Sobre a nulidade dos atos que tenham por objeto o domínio, a posse ou a ocupação de terras
- O Estatuto do Índio determinava ao órgão de proteção que divulgasse e respeitasse a Conven
```

## Pedido 12: Identificação Civil Nacional e Carteira de Identidade Nacional (`legislacao.identificacao-civil`, 30 tópicos de edital)

```
Matéria: legislacao.identificacao-civil (Identificação Civil Nacional e Carteira de Identidade Nacional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistemas de identificação: ICN, DNI e registro civil": 16
- "2. CPF único, RIC e integração dos cadastros": 29
- "3. Carteira de Identidade Nacional: expedição, dados e formatos": 20
- "4. Validade, documentos antigos e retenção da identidade": 19
- "5. Serviço de Identificação do Cidadão, governança e aplicações": 16

Já existem (não repita):
- Uma pessoa participa de procedimento de identificação civil que cruza dados biométricos e 
- Durante uma consulta técnica, pergunta-se quem deve armazenar e gerir a base de dados da I
- Um cadastro da ICN precisa combinar informações biométricas e registrais. Qual conjunto co
- O Comitê Gestor da ICN delibera sobre diretrizes de interoperabilidade. Qual quórum de dec
- Um formulário de serviço público pede CPF e outro número estadual apenas para identificar 
- Um órgão precisa reemitir documento administrativo com número identificador, após as alter
- Uma cidadã solicita sua primeira Carteira de Identidade Nacional, mas ainda não está inscr
- Na primeira emissão da carteira nacional de identidade, uma repartição questiona se é perm
- Um cidadão muda de unidade da Federação e pergunta se sua carteira civil perdeu validade t
- Uma criança com 10 anos recebe uma CIN. Considerando a idade na emissão, qual é o prazo or
- Uma adolescente completa 12 anos e, nessa data, recebe nova carteira de identidade. Qual v
- Uma pessoa de 61 anos solicita sua CIN e pergunta se deverá renová-la ao completar 71. Qua
- Uma pessoa obteve a primeira CIN em um estado e agora solicita emissão em outro ente feder
- No pedido ordinário de CIN, sem dúvida fundamentada de autenticidade ou circunstância espe
- Uma pessoa solicita inclusão de nome social em sua CIN. Qual procedimento corresponde ao a
- Uma repartição afirma que o QR Code da CIN substitui a necessidade do CPF, por constituir 
- Em uma discussão sobre o DNI previsto na Lei nº 13.444/2017, qual afirmação sobre sua emis
- Um ente federativo pretende integrar a seu cadastro informações da ICN, inclusive copiando
- Uma empresa deseja adquirir cópia da base da ICN, e outra solicita apenas conferência biom
- Os prazos da Lei nº 14.534/2023 foram fixados em duas fases. Qual combinação corresponde a
- Em estudo do Registro de Identidade Civil, identifica-se o Decreto nº 7.166/2010 e a Lei n
- Uma pessoa examina regulamentos antigos para definir a expedição da CIN em 2026. Quais dip
- O Decreto nº 11.797/2023 prevê hipóteses de uso facultativo do Serviço de Identificação do
- Um gestor confunde o identificador usado para vincular dados no SIC com o modelo físico de
- Na entrada de um prédio, a recepção pretende guardar a carteira física do visitante até su
- A Lei nº 13.444/2017 criou uma identificação civil que pode utilizar tanto bases biométric
- Como a base da ICN possui dados biométricos, a Lei nº 13.444/2017 autoriza sua comercializ
- Após a Lei nº 14.534/2023, um órgão pode exigir do usuário um número estadual adicional ao
- Na emissão da nova carteira de identidade para uma pessoa não inscrita no CPF, o órgão de 
- Uma CIN emitida aos 11 anos de idade terá validade ordinária de dez anos porque o titular 
- A pessoa com 60 anos na expedição da CIN recebe documento de validade indeterminada, segun
- A Lei nº 7.116/1983 assegura gratuidade da primeira emissão da carteira de identidade, não
- Segundo a Lei nº 5.553/1968, é lícito reter por cinco dias a identidade apresentada na ent
- A validade nacional da carteira de identidade não depende de emissão no mesmo estado em qu
- No Decreto nº 11.797/2023, a utilização do SIC é obrigatória na identificação criminal e f
- A administração estadual pode copiar os registros biométricos da ICN para seus cadastros d
- A Lei nº 9.454/1997 conserva relevância normativa para o registro civil único, mas, em doc
- O Decreto nº 7.166/2010 continua sendo o regulamento atual da emissão da CIN, pois a poste
- O requerimento para inclusão do nome social na CIN pode ocorrer por escrito, sem exigência
- Um cidadão que tinha 60 anos ou mais em 1º de março de 2022 pode ter prazo indeterminado n
- A vigência por prazo indeterminado da CIN para pessoas com 60 anos ou mais impede que se e
- As resoluções da Cefic sobre compartilhamento de dados pessoais devem respeitar proteção d
- O quórum para aprovar deliberações da Cefic é de dois terços de todos os seus membros, rep
- A possibilidade de emitir a carteira atual em papel de segurança até 1º de março de 2032 d
- No Decreto nº 11.797/2023, CPF é chave para vincular dados no SIC, e o compartilhamento de
```

## Pedido 13: Enfermagem em centro cirúrgico, recuperação anestésica e CME (`enfermagem.centro-cirurgico-cme`, 30 tópicos de edital)

```
Matéria: enfermagem.centro-cirurgico-cme (Enfermagem em centro cirúrgico, recuperação anestésica e CME)
Capítulos e quantas questões quero em cada um (total 100):
- "Organização do centro cirúrgico e assistência perioperatória": 20
- "Tempos cirúrgicos, instrumentação e segurança do campo operatório": 20
- "Posicionamento cirúrgico e Lista de Verificação de Cirurgia Segura": 20
- "Sala de Recuperação Pós-Anestésica: avaliação, intercorrências e alta": 20
- "CME: fluxo de processamento, embalagens, esterilização e rastreabilidade": 20

Já existem (não repita):
- Na montagem do centro cirúrgico, qual atividade corresponde tipicamente ao circulante de s
- Durante uma operação, a aproximação dos planos teciduais com fios caracteriza qual tempo c
- O momento do checklist de cirurgia segura imediatamente anterior à incisão é chamado de:
- Na recuperação anestésica, qual dado deve ser priorizado quando o paciente está sonolento 
- O fluxo recomendado para o processamento de produtos para saúde na CME deve seguir qual di
- Qual equipamento é primariamente empregado para esterilização de artigos compatíveis com v
- Em uma cirurgia, o profissional solicita controle imediato de sangramento após dissecção i
- Uma pessoa será colocada em decúbito ventral para cirurgia da coluna. Qual ação de enferma
- Na conferência de cirurgia segura, qual conjunto pertence mais diretamente ao Sign out?
- Paciente na SRPA apresenta escore satisfatório em escala de recuperação, porém sangramento
- Em conformidade com a RDC 15, o rótulo de um produto esterilizado deve conter qual informa
- Qual afirmação distingue corretamente a CME Classe II da Classe I?
- Para um conjunto de produtos para saúde classificados como críticos e reprocessáveis, qual
- Qual dos seguintes grupos de parâmetros é avaliado pelo índice de Aldrete e Kroulik modifi
- Uma carga de implantes apresentou parâmetros físicos adequados e integrador químico satisf
- Um CME utiliza autoclave assistida por bomba de vácuo. Qual opção expressa a função e o mo
- Na CME, uma caixa está selada e com indicador externo alterado, mas o invólucro sai úmido 
- Antes da indução, há discordância entre o sítio cirúrgico no prontuário e a marcação do pa
- Durante a síntese, a contagem de compressas não confere com a inicial. Qual resposta é tec
- O serviço deseja usar esterilização a vapor para uso imediato devido a falha crônica no pl
- O centro cirúrgico depende apenas de uma sala física disponível; equipe treinada e equipam
- A diérese corresponde ao acesso ou à separação de tecidos durante um procedimento cirúrgic
- O Sign in da Lista de Verificação de Cirurgia Segura ocorre antes da indução anestésica.
- Na SRPA, um paciente sonolento que apresenta redução da saturação exige avaliação imediata
- A lavadora ultrassônica realiza limpeza por cavitação e, por isso, pode substituir todas a
- A RDC 15/2012 proíbe o uso de estufas para esterilização de produtos para saúde.
- O técnico de enfermagem que atua na CME deve realizar os POPs sob orientação e supervisão 
- A presença de uma pinça no campo estéril permite mantê-la em uso mesmo após contato compro
- Na posição de litotomia, a movimentação coordenada dos membros inferiores e a checagem dos
- A confirmação verbal da equipe no Time out pode ser substituída por preenchimento retrospe
- O índice de Aldrete e Kroulik modificado contempla aspectos de consciência e função cardio
- Para produto classificado como crítico e passível de reprocessamento, a limpeza pode ser d
- A rotulagem prevista na RDC 15 inclui número do lote, método de esterilização e nome do re
- O prazo de uso dos produtos esterilizados deve ser sempre igual em todos os serviços, inde
- O monitoramento físico dos ciclos de esterilização deve ser registrado a cada ciclo, e a m
- No processo de esterilização de implantáveis, indicador químico satisfatório é suficiente 
- Um ciclo de esterilização a vapor para uso imediato pode ser utilizado rotineiramente para
- A verificação de marcação do sítio cirúrgico durante o Sign in elimina a necessidade de co
- Quando a contagem de agulhas não corresponde à contagem inicial, é aceitável registrar con
- Um pacote esterilizado cujo invólucro foi perfurado deve ser retido, embora seu indicador 
```

## Pedido 14: Ultrassonografia geral e medicina fetal (`medicina.ultrassonografia-medicina-fetal`, 30 tópicos de edital)

```
Matéria: medicina.ultrassonografia-medicina-fetal (Ultrassonografia geral e medicina fetal)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos físicos, Doppler, técnica e laudo": 17
- "Ultrassonografia abdominal, rins, vias urinárias e aparelho genital masculino": 17
- "Pelve feminina, tireoide, mama, partes moles e aparelho musculoesquelético": 17
- "Ultrassonografia nas urgências, POCUS, FAST e procedimentos guiados": 17
- "Ultrassonografia obstétrica: primeiro trimestre, biometria, anatomia, placenta e líquido": 16
- "Doppler obstétrico e medicina fetal: risco, vigilância e intervenções": 16

Já existem (não repita):
- No exame de um tendão superficial, a finalidade é maximizar detalhe espacial. Qual ajuste 
- Em tecidos moles, elevar a frequência de insonação tende a melhorar a resolução e a reduzi
- Um vaso mostra aparente inversão de velocidades no espectro pulsado após ajuste de escala 
- Um tendão que se torna hipoecoico apenas quando o transdutor é inclinado deve ser interpre
- Ao preparar um laudo ultrassonográfico, foi impossível visualizar adequadamente o pâncreas
- A medida da velocidade no Doppler espectral independe do ângulo entre o feixe ultrassonogr
- Qual afirmação descreve corretamente a diferença entre Doppler colorido e power Doppler co
- A estratégia ALARA e a atenção aos índices térmico e mecânico fazem parte da segurança da 
- Paciente com dor no hipocôndrio direito apresenta parede vesicular espessada, mas não há c
- Cálculos da vesícula podem produzir focos ecogênicos móveis com sombra acústica posterior.
- Em paciente com cólica renal e hidronefrose discreta, qual conclusão é tecnicamente defens
- A simples identificação de fígado hiperecogênico quantifica com precisão o grau de fibrose
- Na investigação de aumento de volume da parede abdominal que só aparece com esforço, qual 
- O espessamento da parede vesical deve ser interpretado considerando o grau de repleção da 
- Homem jovem apresenta dor escrotal abrupta, náuseas e suspeita clínica de torção; observa-
- A ultrassonografia de próstata, por identificar seu volume, é capaz de confirmar ou afasta
- No exame de nódulo tireoidiano, quais categorias de atributos compõem a pontuação do ACR T
- Fluxo preservado no Doppler ovariano exclui de modo absoluto a torção anexial.
- Nódulo mamário classificado como BI-RADS 4 após ultrassonografia deve ser entendido como:
- Um cisto mamário simples costuma ser anecoico, circunscrito e apresentar reforço acústico 
- Uma massa anexial apresenta componente sólido vascularizado e projeções papilares. Qual a 
- O O-RADS US é ferramenta de classificação de achados ovarianos e anexiais, distinta do BI-
- No estudo do tendão do supraespinal, uma faixa hipoecoica desaparece ao corrigir o ângulo 
- Na investigação de abscesso superficial, a demonstração de vascularização periférica permi
- No FAST de paciente traumatizado, não se observa líquido livre nas janelas examinadas. Qua
- O eFAST amplia o FAST convencional com avaliação torácica voltada, entre outros objetivos,
- Na avaliação pulmonar focal, qual achado é mais específico para pneumotórax quando correta
- A ausência de deslizamento pleural é, isoladamente, patognomônica de pneumotórax.
- Durante drenagem guiada de coleção próxima à artéria, que medida reduz o risco de lesão va
- Na abordagem fora do plano (out-of-plane), o ponto ecogênico observado sempre representa a
- Paciente com choque apresenta veia cava inferior de pequeno calibre ao POCUS. Qual a concl
- Quando uma avaliação POCUS é inconclusiva, a limitação e as janelas não obtidas devem ser 
- Em gestação única com exame precoce de boa qualidade, qual medida é preferida para datação
- Translucência nucal aumentada, medida corretamente, é marcador de risco e não diagnóstico 
- A circunferência abdominal fetal para biometria deve ser adquirida preferencialmente em qu
- Uma gestação corretamente datada no primeiro trimestre deve ter sua idade gestacional auto
- No exame morfológico, a obtenção apenas da imagem cardíaca de quatro câmaras é insuficient
- O estudo ultrassonográfico da placenta deve descrever sua relação com o orifício interno c
- Gestante apresenta placenta que recobre o orifício interno do colo no exame transvaginal a
- A avaliação do líquido amniótico, por ILA ou maior bolsão vertical, identifica automaticam
- Feto sob investigação de anemia por aloimunização apresenta Doppler de artéria cerebral mé
- Fluxo diastólico ausente ou reverso na artéria umbilical pode indicar importante compromet
- Em gestações gemelares, qual informação obtida precocemente mais impacta o protocolo de vi
- Resultado de alto risco no teste de DNA fetal livre (NIPT) constitui confirmação diagnósti
- Qual conjunto caracteriza melhor a hidropisia fetal na investigação ultrassonográfica?
- A síndrome de transfusão feto-fetal é associada a anastomoses vasculares em placenta monoc
- Ao diferenciar feto pequeno para idade gestacional (PIG) de restrição de crescimento fetal
- Índice de pulsatilidade reduzido na cerebral média e pico de velocidade sistólica elevado 
```

## Pedido 15: Clínica e cirurgia de pequenos animais (`medicina-veterinaria.clinica-cirurgia-pequenos-animais`, 30 tópicos de edital)

```
Matéria: medicina-veterinaria.clinica-cirurgia-pequenos-animais (Clínica e cirurgia de pequenos animais)
Capítulos e quantas questões quero em cada um (total 100):
- "Semiologia, fisiologia, comportamento e nutrição de cães e gatos": 20
- "Doenças infecciosas, prevenção e imunoprofilaxia": 20
- "Clínica endócrina, renal, urinária e dermatológica": 20
- "Anestesiologia, analgesia e princípios de cirurgia": 20
- "Emergências, fluidoterapia e farmacologia clínica": 20

Já existem (não repita):
- Na anamnese de cães e gatos, dados sobre dieta, medicamentos prévios, evolução e ambiente 
- Polaciúria significa aumento do volume urinário total produzido em 24 horas, independentem
- A ausência de vocalização em um gato internado é suficiente para descartar dor importante.
- A parvovirose canina pode provocar gastroenterite grave em filhotes suscetíveis e exige cu
- As vacinas essenciais globais dos gatos incluem antígenos contra panleucopenia, herpesvíru
- A doença renal crônica deve ser diagnosticada apenas pela creatinina elevada em uma amostr
- A oximetria de pulso oferece informação sobre oxigenação, mas não quantifica diretamente a
- Uma elevação pontual de glicemia em felino agitado, sem documentação adicional, estabelece
- Na classificação IRIS da DRC, a avaliação da proteinúria e da pressão arterial complementa
- Vacinas não essenciais são consideradas inúteis pelas diretrizes e jamais devem ser aplica
- O início da série vacinal em filhotes considera a interferência possível de anticorpos de 
- Em paciente hipovolêmico, a via subcutânea é a modalidade preferencial para restaurar rapi
- O manejo de otite externa recorrente inclui citologia auricular e investigação de fatores 
- Um animal profundamente sedado pode continuar necessitando analgesia específica durante um
- A categoria de risco ASA substitui a avaliação de comorbidades e determina de maneira ineq
- Um gato com obstrução uretral pode apresentar hipercalemia relevante mesmo sem alterações 
- Em cães com suspeita de dermatite atópica, resposta clínica ao glicocorticoide confirma po
- Sob oxigênio suplementar, a SpO2 pode permanecer aparentemente adequada apesar de hipovent
- Na hipovolemia, volumes padronizados de cristalóide devem ser administrados sem reavaliaçã
- A escolha de antimicrobiano para piodermite deve considerar confirmação de infecção e inve
- Um tutor pode utilizar em gatos, sem avaliação veterinária, qualquer analgésico que seja s
- Um cão é levado à consulta por urinar várias vezes ao dia, em pequenos volumes, com descon
- Na avaliação nutricional, qual medida é mais adequada para estimar separadamente adiposida
- Qual conjunto representa os agentes das vacinas essenciais globais felinas segundo a WSAVA
- Qual sinal em um gato macho deve levar a suspeita imediata de obstrução uretral?
- Durante anestesia de um cão, qual equipamento auxilia diretamente a acompanhar a ventilaçã
- Qual medida faz parte da abordagem racional de antimicrobianos em pequenos animais?
- Ao observar um cão nervoso durante a consulta, qual interpretação comportamental é a mais 
- Um felino chega agitado, com glicemia elevada em única coleta, sem achados conclusivos. Qu
- Em cão com prurido crônico e otite recorrente, a conduta diagnóstica inicial mais apropria
- Qual situação justifica considerar vacina respiratória adicional em cães além do núcleo es
- Em DRC canina ou felina confirmada, qual informação é usada para subestadiamento no sistem
- Durante procedimento cirúrgico doloroso em cão sedado, qual plano favorece conforto e segu
- Em cão com suspeita de hipovolemia e pulsos fracos, qual via é indicada para reposição cir
- Qual achado sugere considerar hiperadrenocorticismo canino na lista de diferenciais?
- Um gato obstruído tem potássio sérico elevado, mas eletrocardiograma sem padrão típico. Qu
- Após analgesia e fluidos, uma cadela com piometra está hipotensa. Qual princípio deve orie
- No contexto de uma gata com perda de peso e aumento de apetite, qual associação clínica ju
- Durante anestesia inalatória com oxigênio suplementar, SpO2 está preservada, porém CO2 exp
- Uma cadela com prurido sazonal melhora com glicocorticoide. O que permite avaliar atopia d
- Um animal chega após intoxicação suspeita e apresenta consciência reduzida. Qual alternati
- No acompanhamento de DRC em gato estável, observa-se hipertensão e proteinúria persistente
- O reconhecimento da leptospirose suspeita em cão com alterações renais e hepáticas inclui 
- Os vírus da leucemia felina (FeLV) e da imunodeficiência felina (FIV) são o mesmo agente e
- Em ferida cirúrgica contaminada, a presença de tecido desvitalizado pode indicar debridame
- Um gato adulto precisa de orientação nutricional. Qual característica deve ser considerada
- Na investigação de um cão jovem não imunizado, com vômito, diarreia intensa e desidratação
- Um cão apresenta ferida extensa com sujidade e tecidos desvitalizados após trauma. Qual pr
```

## Pedido 16: Estrabismo, motilidade ocular e neuroftalmologia (`medicina.oftalmologia-estrabismo-neuroftalmologia`, 30 tópicos de edital)

```
Matéria: medicina.oftalmologia-estrabismo-neuroftalmologia (Estrabismo, motilidade ocular e neuroftalmologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Bases da motilidade ocular e visão binocular": 20
- "Estrabismos horizontais, refracionais e ambliopia": 20
- "Desvios verticais, restritivos e síndromes complexas": 20
- "Prismas, tratamento ortóptico e cirurgia de estrabismo": 20
- "Neuroftalmologia: paresias, nistagmo, nervo óptico e campimetria": 20

Já existem (não repita):
- O nervo abducente (VI) inerva o reto lateral, responsável principalmente pela abdução ocul
- O oblíquo superior é inervado pelo nervo oculomotor (III) e realiza principalmente extorsã
- O cover alternado rompe a fusão binocular e pode evidenciar um desvio maior que o encontra
- A diplopia binocular decorrente de desalinhamento ocular costuma persistir inalterada após
- Na oftalmoplegia internuclear, uma lesão do fascículo longitudinal medial pode prejudicar 
- A prescrição óptica da esotropia acomodativa deve ignorar a refração sob cicloplegia, pois
- Uma esotropia que permanece parcialmente manifesta mesmo após adequada correção hipermetró
- O fechamento de um olho sob luz intensa pode ser manifestação de exotropia intermitente, m
- A correção cirúrgica do ângulo estrábico elimina automaticamente a ambliopia e garante est
- Na insuficiência de convergência, o desalinhamento exofórico ou exotrópico frequentemente 
- Na síndrome de Brown, o achado característico é dificuldade de elevação do olho quando ele
- Na síndrome de Duane, o estreitamento da fenda palpebral e a retração do globo durante a a
- Uma dução forçada positiva por resistência passiva ocular apoia a hipótese de estrabismo r
- Na paralisia unilateral do IV nervo, a inclinação cefálica compensatória obrigatoriamente 
- Uma dioptria prismática corresponde geometricamente a um deslocamento de um centímetro a u
- A recessão de um músculo extraocular consiste, em termos gerais, em reposicionar sua inser
- Prismas terapêuticos corrigem definitivamente a paralisia oculomotora que causou a diplopi
- Um adulto com ptose súbita, diplopia, déficit de adução e midríase deve ser investigado pr
- A presença de disco óptico com aparência normal exclui neurite óptica, porque todo process
- Uma hemianopsia bitemporal reprodutível sugere acometimento da região quiasmática, devendo
- Qual conjunto relaciona corretamente movimento e principal músculo responsável?
- Ao cobrir alternadamente os olhos de uma criança, o examinador quer principalmente:
- Um adulto relata duas imagens horizontais que desaparecem assim que um olho é ocluído. O a
- O olho direito não aduz na mirada para a esquerda; há nistagmo do olho esquerdo em abdução
- Em testes motores, a principal diferença entre dução e versão é que:
- Uma criança hipermétrope tem esodesvio que desaparece com a correção hipermetrópica calcul
- Uma criança com exotropia intermitente alinhada parte do dia deve ter sua evolução avaliad
- Após correção óptica de hipermetropia, uma paciente mantém esodesvio residual consistente.
- Uma criança apresenta baixa acuidade por anisometropia desde os primeiros anos, mesmo sem 
- Em adulto com exodesvio predominantemente próximo, diplopia ou cansaço à leitura e limitaç
- Uma paciente apresenta hipertropia direita que piora ao olhar para baixo e para a esquerda
- Qual sinal separa mais caracteristicamente a síndrome de Duane da paralisia periférica iso
- Durante avaliação de limitação da elevação em adução, o teste de dução forçada sugere resi
- Ao tentar distinguir paralisia ocular de encarceramento de músculo após fratura de órbita,
- Uma criança com desvio vertical dissociado apresenta elevação lenta do olho ao ser ocluído
- Qual é a interpretação correta de uma potência de 8 dioptrias prismáticas?
- Uma esotropia pequena com diplopia binocular estável pode ser compensada opticamente, em c
- Qual descrição define ressecção muscular no tratamento do estrabismo?
- Paciente adulto apresenta ptose e diplopia de início agudo, olho desviado para fora e para
- Um laudo mostra hemianopsia homônima esquerda confiável em ambos os olhos. Qual localizaçã
```

## Pedido 17: Terapia nutricional enteral e parenteral (`nutricao.terapia-nutricional-enteral-parenteral`, 29 tópicos de edital)

```
Matéria: nutricao.terapia-nutricional-enteral-parenteral (Terapia nutricional enteral e parenteral)
Capítulos e quantas questões quero em cada um (total 100):
- "Indicação, triagem e avaliação no hospital": 20
- "Nutrição enteral: acessos, fórmulas e administração": 20
- "Planejamento, cálculos e monitorização": 20
- "Nutrição parenteral: composição, preparo e segurança": 20
- "Complicações, EMTN, boas práticas e legislação": 20

Já existem (não repita):
- Paciente com disfagia neurológica, ingestão oral insegura e intestino funcional necessita 
- No processo de assistência hospitalar, qual é a finalidade inicial da triagem nutricional?
- Durante choque com hipoperfusão intestinal persistente, uma equipe discute iniciar volume 
- Paciente com perda ponderal e inflamação aguda exibe albumina plasmática baixa. Qual inter
- Em paciente com obstrução intestinal mecânica importante e impossibilidade de trânsito, pe
- Qual descrição corresponde a um sistema fechado de nutrição enteral?
- Após colocação de sonda para alimentação, o profissional insufla ar e ausculta ruído no ep
- Uma fórmula enteral contém 1,5 kcal/mL. Qual volume fornece 900 kcal?
- Pessoa com gastroparesia e intolerância persistente à oferta gástrica necessita de nutriçã
- A banca pergunta pela classificação definida na regulamentação brasileira de fórmulas para
- Foi prescrito volume enteral de 1.200 mL para administração em 20 horas efetivas. Qual a t
- Para paciente de 60 kg, o exercício propõe meta proteica de 1,2 g/kg/dia. Qual resultado m
- A meta é fornecer 1.500 kcal com fórmula de 1,25 kcal/mL, cuja ficha informa 80% do volume
- Paciente desnutrido após jejum prolongado iniciará aporte nutricional. Qual conjunto de al
- Uma bomba deveria fornecer 1.000 mL a 50 mL/h em 20 horas, mas foi interrompida por quatro
- Qual alternativa apresenta componentes usuais da nutrição parenteral completa?
- Acesso central foi perdido e a prescrição prevê bolsa de NP altamente concentrada. Qual de
- Durante conferência de NP, observa-se precipitado visível em uma bolsa. Qual conduta é apr
- No planejamento de NP, por que acompanhar triglicerídeos e glicemia?
- Qual é o núcleo multiprofissional mínimo previsto para a EMTN?
- Um edital transcreve a RDC 63/2000 como se fosse a regra sanitária atual para TNE. Qual no
- Sobre a Portaria 272/1998, frequentemente citada em provas, qual afirmação descreve adequa
- Em um serviço, a direção pretende eliminar registros do preparo e administração porque a f
- Após início de nutrição enteral, o paciente apresenta diarreia. Qual deve ser a primeira l
- Uma questão contrasta a RDC 21/2015 e a RDC 503/2021. Qual distinção é conceitualmente cor
- A dificuldade de deglutir com segurança, por si só, não demonstra falência intestinal e po
- Uma triagem nutricional positiva estabelece, sem outras avaliações, o diagnóstico definiti
- A presença de instabilidade hemodinâmica grave com hipoperfusão intestinal persistente exi
- A administração por via parenteral é sempre a primeira escolha para pacientes que não cons
- Em paciente edemaciado, o peso aferido pode superestimar a massa corporal de referência e 
- A sonda nasogástrica termina no estômago, enquanto um acesso nasoenteral pós-pilórico pode
- A ausculta de ruídos após insuflação de ar por sonda é prova suficiente de localização cor
- A administração por acesso jejunal elimina completamente o risco de aspiração pulmonar.
- O sistema fechado de nutrição enteral reduz etapas de manipulação, mas ainda exige control
- As categorias normativas de fórmulas para nutrição enteral são obrigatoriamente as mesmas 
- Para uma prescrição de 1.600 kcal/dia com fórmula de 2 kcal/mL, o volume matemático de pro
- Uma fórmula que atende integralmente à meta energética também atende, necessariamente, à m
- O volume efetivamente recebido pode ser inferior ao prescrito quando ocorrem pausas para e
- A concentração sérica de fósforo normal antes da reintrodução de calorias exclui a possibi
- Para administrar 960 mL em 16 horas efetivas por bomba, a velocidade média calculada é de 
- A via periférica para nutrição parenteral admite, sem restrições, soluções de qualquer osm
- Soluções de aminoácidos, glicose e emulsões lipídicas podem integrar a composição da nutri
- Uma bolsa de nutrição parenteral com precipitado visível pode ser administrada se o profis
- O controle de infecção relacionado ao cateter é uma medida prioritária de segurança em pac
- Soro glicosado isolado constitui, em regra, nutrição parenteral completa por fornecer toda
- A EMTN tem como núcleo profissional mínimo médico, nutricionista, enfermeiro e farmacêutic
- O nutricionista e o médico exercem exatamente a mesma atribuição formal na terapia enteral
- A RDC Anvisa 503/2021 substituiu a RDC 63/2000 como referência normativa de requisitos mín
- A Portaria 272/1998 permaneceu definitivamente revogada após a edição da RDC 532/2021.
- A RDC 21/2015 disciplina fórmulas para nutrição enteral, enquanto a RDC 503/2021 disciplin
```

## Pedido 18: Métodos quantitativos para economia: otimização e estática comparativa (`economia.metodos-quantitativos-otimizacao`, 29 tópicos de edital)

```
Matéria: economia.metodos-quantitativos-otimizacao (Métodos quantitativos para economia: otimização e estática comparativa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Cálculo econômico, continuidade, derivadas e elasticidades": 24
- "2. Equilíbrio econômico, estática comparativa e sistemas matriciais": 20
- "3. Otimização irrestrita, utilidade, orçamento e multiplicadores": 20
- "4. Cobb-Douglas, CES, produtividade e minimização de custos": 19
- "5. Eficiência de mercado, custo de capital, CAPM e valor em risco": 17

Já existem (não repita):
- Uma empresa tem receita total R(q)=20q−q² para q entre zero e 20. Qual é a receita margina
- Uma tecnologia é F(K,L)=3KL². Mantendo capital em 3 unidades e trabalho em 2, qual é o pro
- A demanda de um produto é Q=100−2P. Quando o preço é 20 e a quantidade é positiva, qual é 
- Uma firma modela o custo de ajuste por f(x)=|x|, sendo x o desvio da produção planejada. N
- A produção segue Q=AK^0,4L^0,6. Se apenas o capital aumenta 1% em torno de uma combinação 
- Em um mercado, Qd=100−2P e Qs=20+2P. Qual é o par preço e quantidade de equilíbrio sem tri
- Sobre o mercado da questão anterior incide imposto unitário t sobre vendedores, cuja ofert
- Um modelo linear de dois preços possui matriz de coeficientes A=[[2,1],[1,3]]. Qual é seu 
- Um consumidor maximiza U=x^0,5y^0,5 com x+2y=60 e escolhas interiores. Qual cesta satisfaz
- Um consumidor tem utilidade U=x^0,25y^0,75, renda 120, preços px=2 e py=3. Qual é o consum
- Para U=ln x+ln y, renda de 100 e preços iguais a 5, qual cesta interior maximiza a utilida
- Para U(x,y)=xy², o preço de x é igual ao de y e o consumo é interior. Qual relação entre q
- Uma fábrica tem produção Q=√(KL), com K=16 e L=9. Qual é a produção indicada pela função?
- Uma tecnologia Cobb-Douglas é Q=AK^0,4L^0,7. O que ocorre se ambos os insumos forem multip
- Numa CES com parâmetro ρ=0,5 e elasticidade de substituição σ=1/(1−ρ), qual é o valor de σ
- O custo total de uma firma é C(q)=100+5q+q². Quanto custa marginalmente produzir em q=10?
- Uma maximização de utilidade usa L=U+λ(m−despesa). Se no ótimo interior regular λ=0,5, qua
- Dois mercados apresentam equações de equilíbrio 3P1−P2=20 e −P1+2P2=15. Quais são os preço
- Uma firma tem função objetivo f(x,y)=−x²−2y², sem restrições adicionais. Qual é a classifi
- Uma tecnologia CES usa a parametrização σ=1/(1−ρ) e ρ=−1. Qual elasticidade de substituiçã
- No CAPM, a taxa livre de risco é 4%, o retorno esperado de mercado é 9% e o beta de um ati
- Uma empresa se financia com 60% de patrimônio a 12% e 40% de dívida a 8%. Com alíquota fis
- O VaR de uma carteira é R$ 2 milhões para um dia e 99% de confiança. Em distribuição contí
- Uma carteira possui perdas diárias normais de média zero e desvio-padrão R$ 2 milhões. Usa
- O equilíbrio positivo x depende de um parâmetro a pela relação F(x,a)=x²−a=0. No ponto x=2
- No modelo de receita R(q)=20q−q², a receita marginal em q=5 é 10, pois a derivada da recei
- Uma função de custo que seja contínua em um ponto de quebra necessariamente possui derivad
- Se a demanda Q=200−4P é avaliada em P=25, então sua elasticidade-preço pontual assinada va
- Para F(K,L)=K²L, a derivada parcial em relação a L é 2KL, pois é preciso derivar também a 
- Um sistema linear de preços com matriz quadrada de coeficientes cujo determinante é difere
- Na CES parametrizada por σ=1/(1−ρ), o parâmetro ρ=0,5 implica elasticidade de substituição
- Com utilidade Cobb-Douglas U=x^0,5y^0,5, preços positivos e solução interior, o gasto ótim
- Na demanda linear, a elasticidade-preço permanece constante ao longo da curva porque dQ/dP
- Em um mercado descrito por Qd=100−P e Qs=10+2P, a estática comparativa de um aumento do in
- Se o determinante de uma matriz de coeficientes for nulo, qualquer sistema linear associad
- Em uma otimização com desigualdades regulares, uma restrição não ativa possui multiplicado
- Uma função Cobb-Douglas com expoentes 0,4 e 0,7 apresenta produtos marginais individuais c
- Na parametrização CES adotada, quando ρ tende a zero a função converge para uma forma Cobb
- Se o VaR diário de uma carteira a 99% é R$ 3 milhões, então a maior perda possível em qual
- Pelo CAPM básico, dois ativos com mesmo beta recebem a mesma taxa requerida de retorno, qu
- Um projeto com fluxos de caixa nominais pode ser avaliado coerentemente por uma taxa de de
- Para uma função f(x,y)=−x²−3y², o Hessiano é negativo definido e (0,0) é um máximo global 
- Eficiência informacional em sua forma semiforte garante que os preços não mudarão após a d
- Em uma distribuição contínua de perdas, um VaR de confiança 95% pode ser ultrapassado em a
- Na expressão usual do WACC, o benefício fiscal da dívida deve ser aplicado ao custo do pat
```

## Pedido 19: Ações integradas e colaboração na gestão de riscos (`defesa-civil.acoes-integradas-colaboracao-gestao-riscos`, 29 tópicos de edital)

```
Matéria: defesa-civil.acoes-integradas-colaboracao-gestao-riscos (Ações integradas e colaboração na gestão de riscos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos básicos e gestão integrada": 25
- "2. PNPDEC, responsabilidades e colaboração": 25
- "3. Ciclo, Marco de Sendai e COBRADE": 25
- "4. Reescrita aplicada e como a IDECAN cobra": 25

Já existem (não repita):
- Segundo a Lei nº 12.608/2012, qual alternativa apresenta os eixos de atuação da PNPDEC?
- A gestão integrada de riscos e desastres deve ser entendida como:
- A COBRADE tem como finalidade principal:
- Sobre a adoção de medidas de proteção diante de incerteza quanto ao risco, assinale a afir
- No vocabulário de gestão de riscos, vulnerabilidade corresponde mais diretamente:
- Qual situação caracteriza melhor uma medida de mitigação?
- Qual alternativa apresenta uma das quatro prioridades do Marco de Sendai 2015–2030?
- À luz da Lei nº 12.608/2012, assinale a providência compatível com a competência municipal
- Em prevenção de desastres relacionados a corpos d’água, qual unidade de análise a Lei nº 1
- Após uma enchente, a reconstrução de moradias em local seguro, evitando repetir a exposiçã
- Uma ocorrência de derramamento de substância perigosa é registrada para fins de classifica
- Qual frase distingue corretamente risco de desastre já ocorrido?
- Um município inclui riscos climáticos em seu planejamento de saúde, habitação e infraestru
- Sobre a composição e a atuação do SINPDEC, assinale a alternativa correta.
- Antes do período de estiagem, a equipe municipal atualiza contatos, testa alertas, capacit
- Em um sistema estadual, sensores federais identificam chuva intensa, o estado consolida a 
- Uma avaliação de risco recomenda que saúde, educação, habitação e obras municipais atualiz
- Considere as prioridades do Marco de Sendai e as ações da PNPDEC. Qual formulação distingu
- Após um vendaval, um relatório registra o tipo de desastre pela COBRADE. A equipe pretende
- Um plano diretor incorpora mapas de risco, regras de ocupação, rotas acessíveis de evacuaç
- Uma bacia atravessa três municípios e registra risco de inundação. Qual arranjo é mais coe
- Um gestor conclui que informar um código COBRADE, por si só, autoriza declarar situação de
- Em uma encosta ocupada, o poder público fiscaliza novas construções, melhora a drenagem, t
- Dois estudos divergem sobre a chance de ruptura de um talude, mas ambos identificam morado
- Ao revisar um plano de contingência, a prefeitura convida moradores, comércio, escola e un
- Depois de abrigo e atendimento imediato, o plano prevê reconstruir equipamentos em área me
- Um acidente rodoviário libera produto químico e atinge um curso d’água. Para registrar a o
- Uma prefeitura realiza exercício simulado conforme seu plano de contingência, comunica a p
- Uma análise aponta grande ameaça meteorológica, pouca exposição humana e ecossistema frági
- Qual opção relaciona adequadamente as prioridades de Sendai à atuação brasileira prevista 
- A PNPDEC abrange prevenção, mitigação, preparação, resposta e recuperação.
- O SINPDEC é composto exclusivamente por órgãos da administração pública federal.
- O município executa a PNPDEC no âmbito local e coordena ações locais em articulação com Un
- A classificação COBRADE substitui o plano de contingência quando o tipo de desastre já foi
- A incerteza quanto ao risco não impede a adoção de medidas preventivas e mitigadoras.
- A recuperação limita-se a consertar estruturas danificadas e não abrange condições de vida
- O Marco de Sendai inclui compreender o risco, fortalecer a governança, investir em resiliê
- Para prevenir desastres ligados a corpos d’água, a lei indica a bacia hidrográfica como un
- Todo alerta é necessariamente uma ação de resposta, mesmo quando é emitido antes de qualqu
- O risco de desastre diz respeito à possibilidade de perdas, enquanto desastre designa um r
- O código COBRADE, por si só, informa a exposição e a vulnerabilidade de cada local atingid
- As fases da proteção e defesa civil podem se articular e algumas ações podem atender a mai
- A prefeitura só possui atribuições de proteção e defesa civil depois que a União reconhece
- A PNPDEC deve integrar-se a políticas setoriais como saúde, meio ambiente, educação e infr
- As quatro prioridades de Sendai e as cinco ações da PNPDEC são listas idênticas com nomes 
```

## Pedido 20: Pesquisa clínica e avaliação de medicamentos (`regulacao-sanitaria.pesquisa-clinica-avaliacao-medicamentos`, 29 tópicos de edital)

```
Matéria: regulacao-sanitaria.pesquisa-clinica-avaliacao-medicamentos (Pesquisa clínica e avaliação de medicamentos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos de pesquisa clínica e boas práticas": 16
- "2. Regulação brasileira e instâncias de avaliação": 23
- "3. Consentimento e proteção dos participantes": 17
- "4. Responsabilidades e monitoramento de segurança": 23
- "5. Evidências de eficácia e segurança para registro": 21

Já existem (não repita):
- Uma equipe deseja conduzir um ensaio farmacológico e adotar padrões que simultaneamente pr
- Um protocolo determina por sorteio se participantes com determinada doença receberão o med
- Após parecer ético favorável, o patrocinador pretende iniciar no país ensaio de medicament
- Um protocolo com seres humanos exige avaliação independente de riscos, proteção da dignida
- No Sinep vigente, um CEP solicita credenciamento para analisar protocolos e surge contrové
- Durante recrutamento, uma pessoa aceita participar, mas solicita explicação dos riscos e d
- Uma pessoa com qualificação clínica dirige a execução de ensaio no centro e acompanha os v
- Uma empresa financia um ensaio, contrata equipe de monitoramento e assegura recursos para 
- Após iniciar um medicamento em investigação, um voluntário apresenta queixa clínica indese
- Um novo medicamento já passou por exploração de dose e inicia estudo confirmatório de efic
- Um protocolo foi submetido a um CEP e teve sua documentação integralmente aceita. Sem soli
- Petições primárias regulares de DDCM e DEEC são recebidas pela Anvisa para exame segundo a
- Um pesquisador pretende acompanhar, sem atribuir tratamentos, pessoas que recebem medicame
- Há tratamento comprovadamente eficaz para doença estudada. Os pesquisadores propõem fornec
- O orçamento de estudo inclui transporte dos participantes e remuneração pela adesão. Que d
- Um investigador toma conhecimento de hospitalização ligada temporalmente a ensaio e a clas
- Uma suspeita de reação adversa grave, inesperada e plausivelmente relacionada ao medicamen
- O mesmo ensaio segue protocolo em cinco centros nacionais, cada qual com equipe própria. Q
- Um CEP apenas credenciado recebe pesquisa de risco elevado envolvendo tecnologia experimen
- Uma pessoa apta a decidir deseja participar de estudo, mas não sabe ler. O pesquisador pre
- Um voluntário apresenta episódio que resulta em hospitalização. O patrocinador identifica 
- Na análise de um DDCM, a Anvisa considera avaliação técnica de autoridade reguladora estra
- Um estudo de superioridade não encontrou diferença estatisticamente significativa entre me
- Antes de começar um ensaio, a equipe discute o acesso pós-estudo. Ao final, um participant
- Um ensaio pretende concluir não inferioridade usando apenas a ausência de diferença signif
- Após o recrutamento em ensaio, um participante decide retirar seu consentimento sem justif
- Um pesquisador afirma que o simples parecer favorável do CEP autoriza a comercialização de
- Durante monitoria de ensaio, profissionais conferem a aderência ao protocolo, procedimento
- Uma pessoa apresenta náusea após receber produto experimental, mas a equipe ainda não inve
- Antes de incluir voluntários em ensaio para registro de medicamento abrangido pela RDC nº 
- Em outubro de 2026, a instância nacional ordinária de ética em pesquisa permanece sendo a 
- Um CEP recebe protocolo de risco elevado, mas possui apenas credenciamento para baixo e mo
- Ao examinar pedido de emenda ética que modifica riscos do protocolo, o CEP é dispensado de
- Um participante de pesquisa em condição de vulnerabilidade possui capacidade de compreende
- Um voluntário saudável participa de ensaio de fase I. O art. 20 da Lei nº 14.874/2024 proí
- Um participante necessita internação após reação suspeita relacionada ao medicamento. Mesm
- Um ensaio de superioridade mostra resultado estatisticamente não significativo. Por conseq
- Um ensaio de risco elevado é multicêntrico e possui protocolo único. A legislação vigente 
- Diante de suspeita de reação adversa grave e inesperada que ameaça a vida, o investigador 
- A RDC nº 945/2024 prevê que a falta de manifestação da Anvisa no prazo do art. 52 pode aca
- Uma reação adversa que causa internação, mas está descrita nas informações de referência c
- Antes de iniciar ensaio, patrocinador e pesquisador precisam tratar do plano de acesso pós
- Em um ensaio, a equipe mantém oculto do recrutador qual será o próximo grupo, mas informa 
- A avaliação sanitária por reliance pode atribuir peso a parecer de autoridade reguladora e
- Um ensaio pequeno não revelou eventos adversos raros, e houve redução significativa de mar
```

## Pedido 21: Atendimento ao público e comunicação organizacional (`administracao.atendimento-qualidade`, 29 tópicos de edital)

```
Matéria: administracao.atendimento-qualidade (Atendimento ao público e comunicação organizacional)
Capítulos e quantas questões quero em cada um (total 100):
- "Qualidade no atendimento e comunicação organizacional": 17
- "Atendimento presencial, telefônico, acessibilidade e proteção de dados": 17
- "Ouvidoria pública: manifestações, fluxo de tratamento e respostas": 17
- "Ouvidoria, corregedoria, proteção do denunciante e escuta qualificada": 17
- "Lei nº 13.460/2017: direitos, Carta de Serviços e avaliação pelo usuário": 16
- "Gestão de reclamações, indicadores, casos práticos e pegadinhas": 16

Já existem (não repita):
- A cordialidade no contato com o cidadão é suficiente, por si só, para demonstrar qualidade
- A comunicação organizacional pode ser ascendente quando equipes relatam à chefia falhas fr
- Escuta ativa implica concordar com qualquer pretensão do usuário e garantir a providência 
- A acessibilidade do atendimento inclui tanto condições físicas de acesso quanto recursos d
- O atendente deve fornecer dados pessoais de terceiros por telefone sempre que o interlocut
- Na Lei nº 13.460/2017, a noção de usuário alcança quem utiliza potencialmente o serviço pú
- A Carta de Serviços ao Usuário deve informar os serviços prestados, suas formas de acesso 
- O prazo geral para resposta da ouvidoria ao usuário é de vinte dias, com prorrogação autom
- No Executivo federal, quando uma manifestação recebida por meio presencial é admitida, seu
- A nomenclatura selecionada pelo cidadão é invariável: se ele escolher o campo “reclamação”
- A resposta conclusiva e a efetiva resolução de uma manifestação representam conceitos dist
- Os conselhos de usuários previstos na Lei nº 13.460/2017 possuem natureza consultiva e pod
- A divulgação integral de dados pessoais de denunciantes em relatório de gestão é adequada 
- No regime de ouvidorias do Executivo federal, o pedido de complementação de informações ao
- A ouvidoria que recebe denúncia de irregularidade pode tomar depoimento do agente denuncia
- A existência de relação de consumo exclui necessariamente a aplicação da Lei nº 13.460/201
- O aumento do número de reclamações após a criação de canal acessível demonstra, isoladamen
- De acordo com a Lei nº 13.460/2017, o relatório anual de gestão da ouvidoria deve consider
- O uso obrigatório do Fala.BR pelas unidades federais do SisOuv torna juridicamente inadmis
- Uma manifestação identificada com proteção da identidade e uma comunicação sem qualquer id
- Um setor quer reduzir a repetição de informações divergentes dadas no balcão e no telefone
- Uma pessoa demonstra dificuldade para entender a sigla técnica usada pela atendente. A int
- No atendimento presencial, uma pessoa com deficiência solicita comunicação acessível. Qual
- Uma cidadã informa que seu pedido foi ignorado e deseja expressar insatisfação com o servi
- O cidadão propõe permitir o envio de documentos pelo portal, sem relatar irregularidade in
- A Lei nº 13.460/2017 refere-se, principalmente, a:
- A Lei nº 13.460/2017 prevê que a Carta de Serviços ao Usuário informe, entre outros aspect
- Em atendimento telefônico, um desconhecido pede dados de processo que pertence a terceiro.
- No SisOuv federal, a resposta à reclamação deve conter, como conteúdo mínimo:
- Uma ouvidoria solicita informações a setor técnico para formular resposta. Segundo o art. 
- Em manifestação apresentada verbalmente na ouvidoria federal, a prática exigida pelo regra
- Um cidadão pede a retirada de uma exigência documental redundante de um serviço. Qual prov
- Qual indicador examina mais diretamente se providências pendentes foram eliminadas?
- A respeito de ouvidoria e corregedoria, assinale a descrição tecnicamente correta.
- A Carta de Serviços foi publicada há anos e possui horários e requisitos desatualizados. A
- Qual exemplo caracteriza comunicação horizontal no ambiente organizacional?
- Uma pessoa identificada relata irregularidade e teme exposição. À luz da proteção de denun
- Em uma denúncia no SisOuv, a unidade de ouvidoria pretende ouvir o servidor apontado como 
- Após o lançamento de uma plataforma acessível, as reclamações aumentaram 80%, mas a taxa d
- Um usuário pede a fundamentação e os documentos de determinada política pública, sem avali
- Uma ouvidoria finaliza resposta informando providência futura cujo cumprimento ainda depen
- Um gestor defende exigir reconhecimento de firma como regra absoluta, embora o atendente p
- Segundo a Lei nº 13.460/2017, sobre a avaliação dos serviços públicos é correto afirmar:
- Na triagem de uma denúncia sem identificação, faltam elementos mínimos para orientar atuaç
- Uma unidade pretende medir a qualidade contando apenas quantos atendimentos duraram menos 
```

## Pedido 22: Arquitetura hospitalar, acessibilidade e conforto ambiental (`arquitetura.arquitetura-hospitalar-acessibilidade`, 28 tópicos de edital)

```
Matéria: arquitetura.arquitetura-hospitalar-acessibilidade (Arquitetura hospitalar, acessibilidade e conforto ambiental)
Capítulos e quantas questões quero em cada um (total 100):
- "Programação físico-funcional e regulação sanitária dos EAS": 17
- "Setorização hospitalar, dimensionamento e fluxos assistenciais": 17
- "Instalações especiais e controle de riscos nos ambientes de saúde": 17
- "Acessibilidade, desenho universal e rotas acessíveis": 17
- "Conforto térmico, acústico e luminoso": 16
- "Saúde, segurança, operação e avaliação pós-ocupação": 16

Já existem (não repita):
- A programação físico-funcional de um EAS deve partir das atividades e do perfil de assistê
- Qual instrumento traduz atividades, usuários, equipamentos, capacidades e requisitos em am
- A RDC Anvisa nº 50/2002 é pertinente apenas à construção de hospitais novos e não alcança 
- Uma clínica passará a prestar procedimentos invasivos de maior complexidade. Qual conduta 
- A aprovação sanitária de um projeto físico e o licenciamento para funcionamento do serviço
- Qual par normativo está corretamente associado a seus objetos principais?
- Ao avaliar projeto de EAS, é suficiente aplicar o texto originário da RDC nº 50/2002, desc
- Qual percurso merece atenção para evitar interferência entre distribuição de itens prontos
- Uma matriz de adjacências auxilia a relacionar setores que precisam estar próximos e setor
- Um corredor atende ao requisito genérico de passagem de pedestres, mas não permite giro de
- Qualquer fluxo de roupa suja, roupa limpa, paciente e visitante exige obrigatoriamente cor
- Ao reorganizar o centro de processamento de materiais, o arquiteto deve priorizar qual cri
- Uma sala com área superior à mínima indicada para certa atividade pode ainda ser inadequad
- Na determinação de áreas e ambientes de espera de um ambulatório, que informação é particu
- O armazenamento e a circulação interna de resíduos de saúde não têm relação com a programa
- Num recinto que necessita conter contaminantes, qual condição pode contribuir para evitar 
- O posicionamento de gases medicinais e demais instalações de suporte deve considerar as at
- Uma nova máquina de diagnóstico exige climatização específica e acesso de manutenção. O qu
- Pressão negativa relativa pode integrar medidas destinadas a reduzir a saída de ar potenci
- Uma sala usa radiação ionizante. Qual estratégia para suas barreiras é tecnicamente corret
- Todo ambiente de atendimento hospitalar deve adotar pressão positiva independentemente da 
- Qual alternativa melhor representa o desenho universal?
- A proteção radiológica de uma sala de imagem com radiações ionizantes pode ser definida ap
- Em um hospital existe rampa na entrada, mas o sanitário público não admite manobra por cad
- Uma rota acessível deve ligar de modo contínuo e desobstruído os percursos usados pelos us
- Uma rampa vence 0,60 m em 7,50 m de projeção horizontal. Qual é sua inclinação calculada?
- O conceito de desenho universal significa vedar toda tecnologia assistiva e qualquer adapt
- Qual situação evidencia melhor a necessidade de acessibilidade comunicacional integrada ao
- Uma rampa com 0,48 metro de desnível e 6 metros de projeção horizontal tem inclinação de 8
- Qual grandeza luminotécnica é expressa em lux?
- O cumprimento da ABNT NBR 9050 pode ser inferido apenas pela existência de elevador, mesmo
- Uma sala de consulta apresenta elevada reverberação e conversas pouco inteligíveis. Qual i
- A iluminância é expressa em lux, e a iluminação deve ser planejada segundo as tarefas visu
- Qual comparação entre isolamento acústico e absorção sonora é correta?
- Materiais de absorção acústica destinados a diminuir a reverberação de uma sala garantem, 
- Um quarto voltado para oeste recebe forte sol da tarde e sofre ofuscamento e aquecimento. 
- Uma janela amplamente envidraçada pode, ao mesmo tempo, ampliar a luz diurna e criar probl
- Qual norma regulamentadora dedica-se especificamente à segurança e saúde do trabalho em se
- A avaliação pós-ocupação pode identificar falhas de fluxos e conforto, mas não substitui f
- Qual situação caracteriza uma avaliação pós-ocupação útil para gestão de um hospital?
```

## Pedido 23: Conselho Tutelar e Sistema de Garantia de Direitos (`legislacao.eca-conselho-tutelar`, 28 tópicos de edital)

```
Matéria: legislacao.eca-conselho-tutelar (Conselho Tutelar e Sistema de Garantia de Direitos)
Capítulos e quantas questões quero em cada um (total 100):
- "Natureza jurídica, organização e atribuições do Conselho Tutelar": 17
- "Escolha dos conselheiros, requisitos, impedimentos e funcionamento": 17
- "Medidas de proteção, escola, família e limites de intervenção": 17
- "Sistema de Garantia de Direitos e atendimento à violência": 17
- "Rede de proteção e fluxos operacionais de atendimento": 16
- "SINASE: competências, princípios e plano individual de atendimento": 16

Já existem (não repita):
- O Conselho Tutelar é órgão permanente, autônomo e não jurisdicional, incumbido de zelar pe
- O Conselho Tutelar exerce função jurisdicional porque pode rever sentenças da Vara da Infâ
- Cada município deve manter ao menos um Conselho Tutelar composto de cinco membros.
- A escolha unificada de conselheiros tutelares ocorre no primeiro domingo de outubro do ano
- A fiscalização do processo de escolha dos conselheiros tutelares é competência do Ministér
- A recondução ao Conselho Tutelar é proibida depois de um segundo mandato, mesmo que o cand
- As medidas do art. 98 podem decorrer de omissão do Estado, de conduta dos pais ou da própr
- O depoimento especial de vítima ou testemunha de violência é procedimento realizado perant
- Para notificação de faltas conforme a Lei nº 13.803/2019, exige-se ausência superior a 30%
- O Conselho Tutelar pode, por decisão própria e sem supervisão judicial, decretar a perda d
- Na escuta especializada, o relato sobre a violência deve limitar-se ao necessário para a f
- Em violência sexual, o depoimento especial segue o rito cautelar de antecipação de prova p
- Se uma criança revela violência a um profissional da escola, o adequado é acolhê-la e enca
- A ausência de CREAS no município afasta o dever de atendimento especializado a criança vít
- A Lei do SINASE regula a execução de medidas socioeducativas, incluindo a prestação de ser
- O PIA é elaborado sem participação do adolescente, pois apenas a equipe técnica conhece as
- A redação do art. 136, III, a, do ECA prevê requisição de serviços públicos inclusive na á
- Embora o art. 136 mencione o acolhimento institucional no alcance das atribuições tutelare
- No atendimento articulado, o envio indiscriminado de todo o histórico íntimo da criança a 
- A liberdade assistida, a semiliberdade e a internação devem ser reavaliadas, no máximo, a 
- Qual expressão define corretamente o Conselho Tutelar segundo o ECA?
- Um município possui um Conselho Tutelar com cinco conselheiros. Qual afirmação está corret
- Entre as atribuições legalmente previstas ao Conselho Tutelar, encontra-se:
- Para participar do processo de escolha, qual conjunto contém somente requisitos pessoais e
- Uma candidata pergunta quem responde pelo processo de escolha dos membros do Conselho Tute
- Ao analisar a continuidade de um conselheiro tutelar no cargo, qual regra de recondução de
- A família de um aluno pede que a escola identifique a situação que deve ser comunicada ao 
- Uma escola adota, no exemplo, limite permitido de faltas igual a 25% da carga horária. Seg
- Ao constatar possível abuso e necessidade de afastamento do convívio familiar, o conselhei
- Sobre o rol do art. 101 do ECA, indique a medida que não integra a faixa de incisos atribu
- Qual é a correspondência correta entre os eixos do SGDCA e suas atividades?
- Uma criança conta espontaneamente a uma professora que sofre violência em casa. Qual condu
- Um técnico da rede de proteção precisa conversar com adolescente vítima apenas para planej
- Em qual situação a Lei nº 13.431/2017 prevê o rito cautelar de antecipação de prova para d
- Em relação à repetição de depoimento especial, a previsão legal é:
- Em uma rede municipal, qual arranjo melhor corresponde ao Decreto nº 9.603/2018?
- Uma criança vítima de violência precisa de acompanhamento familiar e psicossocial especial
- Após atendimento de violência por profissional de segurança pública, qual medida está em c
- A Lei do SINASE destina-se principalmente a:
- No sistema socioeducativo, quem se responsabiliza, em linhas gerais, pelos programas de PS
- Uma equipe inicia a execução de liberdade assistida. Qual prazo legal se aplica à elaboraç
- Na elaboração do Plano Individual de Atendimento, deve participar:
- Sobre as medidas de liberdade assistida, semiliberdade e internação, é correta a seguinte 
- Um candidato afirma que o Conselho Tutelar pode determinar internação socioeducativa de ad
- Um hospital, uma escola e o CT recebem relatos diferentes sobre o mesmo abuso. Para proteg
```

## Pedido 24: TypeScript: tipos, interfaces, generics e compilação (`informatica.typescript`, 27 tópicos de edital)

```
Matéria: informatica.typescript (TypeScript: tipos, interfaces, generics e compilação)
Capítulos e quantas questões quero em cada um (total 100):
- "O que é TypeScript e como ele vira JavaScript": 20
- "Tipos básicos, inferência, any, unknown, void e never": 20
- "Interfaces, types, union, intersection e generics": 20
- "Enums, classes, modificadores e narrowing": 20
- "tsconfig, compilação e como o tema costuma ser cobrado": 20

Já existem (não repita):
- Qual afirmação descreve corretamente a relação entre TypeScript e JavaScript?
- Qual ferramenta oficial compila arquivos .ts para .js?
- Qual declaração está correta em TypeScript?
- Qual é o tipo de retorno adequado para uma função que apenas exibe uma mensagem no console
- Na declaração let id: string | number;, o que o símbolo | indica?
- Dada interface Pessoa { nome: string; idade?: number; }, qual objeto é aceito como Pessoa?
- Em uma classe TypeScript, qual modificador permite acessar o membro apenas dentro da própr
- Qual comando cria um arquivo tsconfig.json inicial no projeto?
- Sem anotação explícita, quais tipos o compilador infere para x e y em let x = 10; const y 
- Considere let v: unknown = "texto";. Qual trecho compila sem erro?
- Dada let par: [string, number];, qual atribuição é válida?
- Sobre interface e type alias, assinale a alternativa correta.
- Considere type A = { nome: string }; type B = { idade: number }; type C = A & B;. Qual val
- Dada a função function primeiro<T>(lista: T[]): T | undefined { return lista[0]; }, qual é
- Qual é a saída de enum Cor { Vermelho, Verde = 5, Azul } console.log(Cor.Vermelho, Cor.Azu
- Na função function f(v: string | number) { if (typeof v === "string") { /* bloco 1 */ } el
- No tsconfig.json, qual opção define a pasta onde serão gravados os arquivos .js gerados?
- Um projeto declara interface Usuario { id: number; } e recebe de uma API o objeto { id: "7
- Considere function pegar<T, K extends keyof T>(obj: T, chave: K): T[K] { return obj[chave]
- Dada interface Produto { id: number; nome: string; preco: number; }, qual tipo utilitário 
- Qual é a saída de enum Nivel { Baixo = 1, Medio, Alto } console.log(Nivel[2]);?
- Em type Figura = { tipo: "circulo"; raio: number } | { tipo: "quadrado"; lado: number }, o
- Sobre o código class Ponto { constructor(public x: number, private y: number) {} } const p
- Com strictNullChecks ligado, const el = document.getElementById("x"); tem tipo HTMLElement
- Um desenvolvedor quer que o tsc apenas confira os tipos do projeto, sem gravar nenhum arqu
- Todo código JavaScript sintaticamente válido também é aceito como código TypeScript do pon
- As anotações de tipo do TypeScript permanecem no JavaScript gerado e são verificadas pelo 
- Em TypeScript, o tipo number representa tanto valores inteiros quanto valores com casas de
- Depois de let total = 10;, a instrução total = "dez"; compila normalmente, porque a variáv
- Em uma interface, o ponto de interrogação após o nome de uma propriedade, como em idade?: 
- Quando nenhum modificador de acesso é escrito em um membro de classe TypeScript, ele é con
- O arquivo tsconfig.json guarda as opções do compilador TypeScript para o projeto.
- Os tipos any e unknown são equivalentes: ambos aceitam qualquer valor e permitem chamar qu
- Uma função que sempre lança uma exceção e nunca termina normalmente pode ter o tipo de ret
- Se p tem tipo { nome: string } | { idade: number }, é possível ler p.nome diretamente, sem
- Uma interface pode herdar as propriedades de outra interface por meio da palavra-chave ext
- Em enum Status { Ativo = "ATIVO", Inativo }, o membro Inativo recebe automaticamente o val
- Uma classe TypeScript pode estender uma única classe e, ao mesmo tempo, implementar várias
- A expressão "10" as unknown as number converte a string "10" no número 10 em tempo de exec
- Um membro declarado com private em TypeScript continua existindo como propriedade comum no
- Para verificar em tempo de execução se um objeto implementa determinada interface, basta u
- Na função function tamanho<T extends { length: number }>(v: T): number { return v.length; 
- Por padrão, quando o tsc encontra um erro de tipo, ele não gera nenhum arquivo .js, mesmo 
- A opção strict do tsconfig.json liga de uma vez um conjunto de verificações rígidas, entre
- Ao executar tsc arquivo.ts, informando o nome do arquivo na linha de comando, o compilador
```

## Pedido 25: Sistemas de liquidação, custódia e arrecadação (`conhecimentos-bancarios.liquidacao-custodia-arrecadacao`, 27 tópicos de edital)

```
Matéria: conhecimentos-bancarios.liquidacao-custodia-arrecadacao (Sistemas de liquidação, custódia e arrecadação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema de Pagamentos Brasileiro: compensação, liquidação e riscos": 11
- "2. Selic: títulos públicos federais, custódia e taxa de juros": 29
- "3. Cetip e B3: registro, custódia e mercados de balcão": 26
- "4. STR, SPI e demais sistemas de transferência de fundos": 11
- "5. Arrecadação de tributos, tarifas públicas e conciliação bancária": 23

Já existem (não repita):
- Uma instituição informa a operação de compra de um título ao sistema, mas ainda não houve 
- Uma instituição necessita de infraestrutura para registrar e custodiar a maioria dos títul
- Uma instituição emite CDB para captar recursos de investidores. Para classificá-lo correta
- Um sistema opera liquidação bruta em tempo real de transferências de fundos no Banco Centr
- Um contribuinte necessita pagar tributo federal por documento próprio de arrecadação da Re
- Uma câmara apura os saldos devedores e credores dos participantes antes de movimentar fund
- Ao comparar o Selic e a taxa Selic, um estudante identifica corretamente as expressões qua
- Um material de estudo antigo refere-se à Cetip como companhia independente. Em relação à s
- Um participante direto do SPI liquida pagamento instantâneo interinstitucional. Conforme a
- Uma guia destinada a receita de unidade gestora federal contém código de recolhimento e un
- Num sistema de compensação multilateral, a instituição Alfa registra débitos de 120 e créd
- Uma instituição compra uma LTN e outra compra uma debênture corporativa. A classificação d
- Um investidor consulta seu saldo de debêntures e verifica posição depositada na B3. O que 
- Uma instituição possui saldo utilizável de 25 no STR e deve liquidar transferência de 40. 
- Contribuinte gerou DARF comum sem código de barras e encontrou no aplicativo do banco uma 
- Uma entidade financeira registra operação em ambiente de balcão da B3, mas não há informaç
- A partir da atualização comunicada em abril de 2026, qual orientação corresponde à emissão
- Na aplicação do art. 4º da Lei nº 10.214/2001 a uma câmara que assume posição de parte con
- Numa posição multilateral, X deve 135, recebe 75 e dispõe de 40 para liquidar o saldo. Na 
- Um título público federal prefixado sofreu desvalorização de mercado após aumento das taxa
- Uma debênture teve transferência escritural concluída no ambiente de depósito da B3. O emi
- Participantes P e Q querem liquidar ordens por dois sistemas. O primeiro liquida fundos no
- Um edital menciona Sitraf, Siloc e Compe como se fossem sistemas equivalentes de liquidaçã
- Uma unidade recebe confirmação instantânea de pagamento realizado no PagTesouro. Sua base 
- Uma concessionária tem duas faturas quitadas em canal conveniado, 130 e 270, mas recebe no
- Uma operação pode estar registrada no ambiente de mercado antes da transferência final do 
- No Selic, todos os títulos emitidos pelo Tesouro Nacional apresentam obrigatoriamente remu
- A fusão entre BM&FBOVESPA e Cetip foi concluída em março de 2017, dando origem à B3, embor
- O Sistema de Transferência de Reservas funciona mediante liquidação líquida exclusivamente
- Um DARF comum sem código de barras pode ser pago por canal habilitado para pagamento sem c
- O banco que recebe uma fatura de fornecimento de energia por convênio torna-se credor orig
- Na compensação multilateral, um participante que deve 100 e recebe 45 na mesma janela apre
- Pela Lei nº 10.214/2001, uma câmara que atua como contraparte para liquidação também se to
- A infraestrutura Selic participa do Sistema de Pagamentos Brasileiro ao oferecer custódia 
- O depósito de debêntures na Central Depositária da B3 converte os papéis em obrigações da 
- Em pagamentos Pix entre instituições participantes diretas, o SPI efetua liquidação interi
- A centralizadora Compe e o SPI desempenham exatamente a mesma função, pois ambos fazem com
- A confirmação de pagamento no PagTesouro pode ocorrer antes da baixa correspondente no sis
- Um sistema aplica compensação multilateral e estabelece patrimônio especial de uma câmara 
- Uma operação contratada com liquidação em data futura pode estar registrada antes de ocorr
- Em plataforma de balcão, o lançamento de dados da emissão de um CDB comprova que todas as 
- Embora STR e SPI operem liquidação bruta em tempo real, a infraestrutura do SPI utiliza Co
- Em 2026, uma GRU Simples avulsa ainda pode ser emitida livremente fora do Portal PagTesour
- Se um contribuinte comprova débito efetivado de guia de 270, mas a entidade arrecadadora a
- No âmbito das infraestruturas, usar entrega contra pagamento em uma operação com título pe
```

## Pedido 26: Orientação e mobilidade, deficiência visual e cães-guia (`educacao.orientacao-mobilidade-caes-guia`, 27 tópicos de edital)

```
Matéria: educacao.orientacao-mobilidade-caes-guia (Orientação e mobilidade, deficiência visual e cães-guia)
Capítulos e quantas questões quero em cada um (total 100):
- "Deficiência visual, classificação e avaliação funcional": 20
- "Orientação e mobilidade: conceitos, guia vidente e bengala": 20
- "Cães-guia: seleção, formação, ética e gestão de centros": 20
- "Direitos, acesso com cão-guia e Tratado de Marraqueche": 20
- "Tecnologia assistiva, braile e práticas educacionais acessíveis": 20

Já existem (não repita):
- Um estudante lê letras ampliadas, mas não nota pessoas que se aproximam lateralmente. Qual
- No ensino especializado, qual formulação diferencia corretamente orientação e mobilidade?
- Ao oferecer auxílio como guia vidente a pessoa que aceitou ajuda, qual procedimento é usua
- Sobre a bengala longa, assinale a afirmação correta.
- Quanto à organização básica do sistema braile, marque a opção correta.
- Qual norma federal disciplina especificamente o ingresso e a permanência de pessoa com def
- Qual é o foco principal do Tratado de Marraqueche?
- Na classificação epidemiológica da OMS para visão de longe, a cegueira é caracterizada no 
- Ao ensinar trajeto entre sala de aula e biblioteca, qual estratégia produz melhor orientaç
- Que descrição corresponde à técnica de contato constante com a bengala?
- Em um programa responsável de formação de cães-guia, qual ação pertence prioritariamente à
- Na etapa de pareamento da dupla cão-guia e usuário, o critério mais adequado é:
- Sobre o acesso de cães-guia que ainda se encontram em treinamento, é correto afirmar que:
- Em um supermercado, uma pessoa entra acompanhada de cão-guia treinado. Qual interpretação 
- Uma escola digitalizou um livro por fotografias e enviou arquivo PDF sem texto reconhecíve
- O Decreto nº 5.904/2006 prevê elementos de identificação e documentação do cão-guia. Qual 
- Uma comissão decide aprovar todo cão que concluiu o calendário de aulas, mesmo quando há r
- Sobre os efeitos do Tratado de Marraqueche, marque a interpretação juridicamente mais adeq
- Em travessia urbana, o cão-guia para inesperadamente diante de um obstáculo que o usuário 
- Qual conjunto de indicadores evita avaliar centros de cães-guia apenas pelo volume de entr
- A perda importante do campo visual pode prejudicar a mobilidade mesmo quando a pessoa cons
- Na orientação e mobilidade, a pessoa com deficiência visual deve aceitar ajuda física mesm
- O cão-guia pode contribuir para contornar obstáculos, mas o usuário continua responsável p
- O Decreto nº 5.904/2006 autoriza estabelecimentos de uso coletivo a exigir focinheira do c
- A cela braile fundamental possui seis pontos distribuídos em duas colunas com três posiçõe
- A acuidade de longe basta, isoladamente, para determinar a autonomia de mobilidade e todos
- Pontos de referência estáveis e estratégias para lidar com uma rota bloqueada integram o e
- No uso da bengala, a técnica de contato constante exige que a ponteira seja mantida contin
- A família socializadora participa da preparação do cão-guia, mas não substitui a atuação t
- A Lei nº 11.126/2005 contempla apenas a cegueira completa, excluindo expressamente pessoas
- O Tratado de Marraqueche se relaciona com formatos acessíveis e prevê mecanismos voltados 
- A existência de um arquivo eletrônico no formato PDF assegura, por si só, que um leitor de
- Lupas, leitores de tela, linhas braile e aplicativos podem ser tecnologias complementares;
- Na classificação epidemiológica da OMS, a medida de acuidade utilizada para a classificaçã
- A técnica do guia vidente em escadas requer comunicação sobre início, direção e término do
- Uma dupla pode exigir reavaliação ou encerramento do trabalho mesmo após a entrega bem-suc
- O Decreto nº 5.904/2006 proíbe a entrada de todo cão-guia em quaisquer dependências de qua
- A legislação permite ingresso de cão em socialização ou treinamento nos locais abrangidos,
- A proteção do direito autoral foi inteiramente extinta pelo Tratado de Marraqueche, permit
- Na produção de material educacional acessível, a descrição informativa de gráficos e a est
```

## Pedido 27: Contratações de TIC no Poder Judiciário (Resolução CNJ 468/2022) (`informatica.contratacoes-tic-judiciario-cnj-468`, 27 tópicos de edital)

```
Matéria: informatica.contratacoes-tic-judiciario-cnj-468 (Contratações de TIC no Poder Judiciário (Resolução CNJ 468/2022))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Alcance, conceitos e estratégia": 34
- "2. Fases, planejamento e vedações": 33
- "3. Seleção do fornecedor, gestão do contrato e disposições gerais": 33

Já existem (não repita):
- A Resolução CNJ 468/2022 revogou a Resolução CNJ 182/2013, que tratava anteriormente das c
- Segundo a resolução, Solução de TIC (STIC) corresponde a:
- O Guia de Contratações de STIC do Poder Judiciário só pode ser alterado por meio de nova r
- O Plano de Contratações de STIC deve ser elaborado:
- O Plano de Contratações de STIC deve prever, no mínimo, a caracterização do objeto, a indi
- Com a redação dada em 2025, a resolução admite que os órgãos do Judiciário realizem a cont
- As contratações de STIC seguem, segundo a resolução, as fases de:
- O integrante administrativo da equipe de planejamento da contratação não pode ser servidor
- A fase de planejamento da contratação de STIC, segundo a resolução:
- Identificado objeto similar na plataforma colaborativa do Judiciário (Connect-Jus), o ETP 
- O Mapa de Gerenciamento de Riscos deve ser juntado e atualizado no processo, pelo menos:
- A gestão de processos de TIC pode ser contratada como Solução de TIC, desde que haja pregã
- Quanto à empresa que fornece a Solução de TIC, a resolução determina que:
- É vedado prever em edital que os fornecedores apresentem, antes da contratação, funcionári
- Sobre a métrica homem-hora nas contratações de STIC, a resolução:
- É vedado estabelecer vínculo de subordinação com funcionários da empresa contratada e indi
- Se a autoridade máxima da área de TIC compuser a equipe de planejamento, o estudo técnico 
- No registro de preços, quando o órgão participante é do mesmo segmento de Justiça do geren
- Quando a Solução de TIC for enquadrada como bem ou serviço comum, a modalidade de licitaçã
- Durante a seleção do fornecedor, a equipe de planejamento apoia o pregoeiro ou a comissão 
- A equipe de gestão do contrato de STIC é composta por:
- O papel de gestor do contrato pode ser acumulado com o de fiscal técnico, desde que haja a
- Entre os papéis de fiscal, a resolução admite, excepcionalmente e com justificativa, o acú
- O gestor e os fiscais do contrato podem ser os mesmos servidores que realizaram o planejam
- Em serviços complexos ou descentralizados, a equipe de gestão do contrato pode indicar:
- Nas prorrogações de serviços prestados de forma contínua, a resolução dispensa a elaboraçã
- Para fins de prorrogação contratual, o gestor do contrato deve encaminhar a documentação à
- DOD, ETP e TR devem ser publicados no sítio eletrônico do órgão e na plataforma Connect-Ju
- Quanto à sustentabilidade, a resolução determina que:
- Os órgãos do Judiciário podem normatizar internamente suas contratações, desde que não con
- O fiscal administrativo do contrato deverá, em regra:
- A Resolução CNJ 468/2022 é a mesma norma que a IN SGD/ME 94/2022, aplicável ao Executivo f
- A participação de órgão em registro de preços de outro, segundo a resolução, fundamenta-se
- Nas licitações por preço global, cada serviço ou produto do lote deve estar discriminado e
- A equipe de planejamento deve avaliar o parcelamento do objeto em itens sempre que técnica
- Nas contratações de bens e serviços de STIC, a resolução permite exigir garantia contratua
- O Guia de Contratações de STIC deverá incorporar diretrizes para a contratação de soluções
- Na resolução, a sigla PDTIC refere-se ao:
- A participação na fase de gestão contratual exige que os integrantes indicados tenham ciên
- A fase de gestão do contrato tem por objetivo acompanhar e garantir a adequada prestação d
```

## Pedido 28: Obrigações acessórias e SPED (`contabilidade-tributaria.obrigacoes-acessorias-sped`, 26 tópicos de edital)

```
Matéria: contabilidade-tributaria.obrigacoes-acessorias-sped (Obrigações acessórias e SPED)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos das obrigações acessórias e arquitetura do SPED": 20
- "ECD e ECF: escrituração contábil e fiscal da pessoa jurídica": 20
- "EFD ICMS/IPI, EFD-Contribuições e documentos fiscais eletrônicos": 20
- "eSocial, EFD-Reinf e integração com a DCTFWeb": 20
- "Retenções na fonte, prazos, fiscalização e penalidades": 20

Já existem (não repita):
- A obrigação acessória pode ser exigível mesmo em período sem tributo a recolher.
- A ECD é o módulo destinado principalmente a reunir livros Diário e Razão em forma digital.
- A ECF substitui integralmente a ECD, tornando desnecessária a transmissão de livros contáb
- O DANFE é o próprio arquivo eletrônico da NF-e e pode sempre substituir a guarda do docume
- O eSocial recebe informações referentes a vínculos, remunerações e outros fatos relativos 
- A EFD ICMS/IPI contém apenas o total do imposto devido, sem documentos ou registros de ope
- O DARF gerado a partir da DCTFWeb está associado ao recolhimento, enquanto a transmissão d
- A EFD-Contribuições é, em sua sistemática ordinária, uma escrituração mensal de informaçõe
- Desde janeiro de 2025, os débitos anteriormente declarados pela DCTF PGD passaram a integr
- As retenções de IRRF ligadas a rendimentos do trabalho são, por definição, informadas na m
- Uma mesma contratação de serviços pode envolver retenção previdenciária e retenções tribut
- Todo pagamento de uma empresa a qualquer prestador pessoa jurídica está sujeito simultanea
- O recibo de transmissão de uma escrituração é indício de entrega, mas não garante a correç
- A EFD-Reinf substitui o eSocial porque ambos fornecem dados à DCTFWeb.
- Para fatos geradores de 2026, pode-se afirmar, sem condições, que a DCTF PGD continua a se
- A obrigação de entregar EFD ICMS/IPI pode depender de regras que envolvem o estado compete
- Informações originadas no eSocial ou na EFD-Reinf devem, em regra, ser corrigidas na orige
- A ausência de diferença entre valor bruto e líquido de uma nota prova que não há obrigação
- No controle das retenções, a condição de órgão público da fonte pagadora pode alterar as r
- A entrega de obrigação acessória fora do prazo e o pagamento intempestivo de tributo const
- Uma sociedade precisa identificar onde transmitir os livros Diário e Razão exigidos em mei
- Em relação ao Sistema Público de Escrituração Digital, qual afirmação é adequada?
- Um fiscal precisa consultar documentos de entradas e saídas e dados de apuração de ICMS e 
- O documento auxiliar que representa a nota fiscal eletrônica e ajuda a consultá-la recebe 
- Ao tratar dos vínculos empregatícios e das remunerações que alimentam declarações federais
- Uma empresa organiza receitas, operações e créditos pertinentes ao PIS/Pasep e à Cofins em
- Em setembro de 2026, contador tem débito próprio federal não enviado por eSocial nem EFD-R
- Uma equipe recebeu arquivo de NF-e autorizado e decidiu dispensar qualquer conferência con
- Ao observar IRRF sobre remuneração de empregado e IRRF não trabalhista pago a prestador, o
- Em uma operação sujeita à CSRF, que conjunto de contribuições está na sigla utilizada pela
- Uma empresa entregou arquivo de escrituração e recebeu protocolo. A fiscalização verificou
- A contabilidade da empresa apresenta lucro contábil e ajustes necessários ao tratamento fi
- Na prestação de serviço por cessão de mão de obra sujeita a retenção previdenciária, qual 
- Uma empresa pretende regularizar valor de retenção indevido que veio de evento já transmit
- No encerramento de 2026, um analista compara regras de ECD e ECF. Considerando os prazos g
- Uma prefeitura paga a sociedade prestadora de serviço e pergunta se deve aplicar automatic
- No cruzamento dos dados, encontra-se nota fiscal de venda, registro fiscal mensal e inform
- Ao identificar obrigações de um estabelecimento contribuinte do ICMS situado em unidade fe
- Em 2026, a equipe sustenta que todo débito federal declarado antes em DCTF PGD deve contin
- Um auto de infração aponta atraso na entrega de declaração e recolhimento do imposto depoi
```

## Pedido 29: Constituição do Estado do Ceará (`ceara.constituicao-estadual`, 26 tópicos de edital)

```
Matéria: ceara.constituicao-estadual (Constituição do Estado do Ceará)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Poder Legislativo: Assembleia, deputados e comissões": 20
- "2. Processo legislativo e fiscalização": 20
- "3. Poder Executivo": 20
- "4. Administração pública e servidores": 20
- "5. Segurança pública, defesa civil e controle interno": 20

Já existem (não repita):
- Segundo a Constituição do Estado do Ceará, a Assembleia Legislativa reunir-se-á, anualment
- Pela Constituição do Ceará, as comissões parlamentares de inquérito serão criadas mediante
- Os Deputados Estaduais do Ceará, desde a expedição do diploma:
- Perderá o mandato o Deputado Estadual que deixar de comparecer, em cada sessão legislativa
- É da competência exclusiva da Assembleia Legislativa do Ceará, segundo a Constituição Esta
- A Assembleia Legislativa e suas comissões, pelo voto de um terço dos seus membros, podem c
- Segundo a Constituição do Ceará, a Constituição Estadual poderá ser emendada mediante prop
- A proposta de emenda à Constituição do Ceará será discutida e votada em dois turnos e cons
- Solicitada pelo Governador a apreciação de projeto de sua iniciativa em regime de urgência
- No processo legislativo cearense, o veto do Governador deve ser oposto no prazo de quinze 
- Segundo a Constituição do Ceará, NÃO poderá ser objeto de lei delegada:
- As leis complementares estaduais do Ceará serão aprovadas por maioria absoluta dos votos d
- Sobre o Tribunal de Contas do Estado do Ceará, é correto afirmar que:
- Com a Emenda Constitucional nº 92/2017, o Tribunal de Contas dos Municípios do Ceará foi e
- Na Constituição do Ceará, o Vice-Governador substituirá o Governador:
- Em caso de impedimento do Governador e do Vice-Governador ou de vacância de ambos os cargo
- Vagando os cargos de Governador e Vice-Governador nos últimos dois anos do período governa
- Segundo a Constituição do Ceará, o Governador e o Vice-Governador que, decorridos dez dias
- Compete privativamente ao Governador do Estado do Ceará, segundo a Constituição Estadual:
- A Constituição do Ceará prevê que o Governador será julgado nos crimes comuns pelo STJ apó
- Os Secretários de Estado do Ceará serão escolhidos dentre brasileiros:
- A Constituição do Ceará fixa como limite remuneratório único dos servidores estaduais, de 
- Segundo a Constituição do Ceará, qualquer cidadão, partido político, associação ou sindica
- Pela Constituição do Ceará, é vedada a nomeação para cargos de provimento em comissão, nos
- Na Constituição do Ceará, a publicação anual, no Diário Oficial, da relação nominal dos se
- Segundo a Constituição do Ceará, são estáveis, após três anos de efetivo exercício, os ser
- Sobre os servidores militares estaduais do Ceará, assinale a alternativa correta.
- O militar estadual do Ceará que se candidatar a cargo eletivo com mais de dez anos de serv
- Segundo a Constituição do Ceará, o sistema de segurança pública e defesa civil do Estado é
- Compete à Polícia Civil do Ceará exercer com exclusividade as funções de polícia judiciári
- Sobre a Polícia Civil do Ceará, segundo a Constituição Estadual, é correto afirmar que:
- A Constituição do Ceará determina que o Estado institua delegacias especializadas de atend
- A Controladoria Geral de Disciplina dos Órgãos de Segurança Pública e Sistema Penitenciári
- A Polícia Militar do Ceará, força auxiliar e reserva do Exército, subordinada ao Governado
- Segundo a Constituição do Ceará, incumbe ao Corpo de Bombeiros Militar, no âmbito estadual
- O Conselho de Segurança Pública do Ceará é órgão com funções consultivas e fiscalizadoras 
- Segundo a Constituição do Ceará, o sistema de controle interno mantido de forma integrada 
- As atividades de controle interno no Ceará serão desempenhadas por órgãos de natureza perm
- No Ceará, os entes, entidades e pessoas que recebem recursos estaduais por convênio e não 
- Não perderá o mandato o Deputado Estadual do Ceará investido no cargo de Secretário de Est
```

## Pedido 30: Aviação civil: segurança operacional e legislação (`aviacao.seguranca-operacional-legislacao`, 26 tópicos de edital)

```
Matéria: aviacao.seguranca-operacional-legislacao (Aviação civil: segurança operacional e legislação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da segurança operacional e gerenciamento integrado": 18
- "2. Cultura de segurança e gerenciamento sistemático dos riscos": 16
- "3. SMS e SGSO: componentes, implantação e desempenho": 18
- "4. Dados, análise, proteção das fontes e programa estatal": 25
- "5. Código Brasileiro de Aeronáutica, agência reguladora e aplicação": 23

Já existem (não repita):
- Em um aeródromo, a equipe encontra uma escada estacionada na rota de movimentação de aeron
- Após meses sem ocorrências registradas, uma empresa conclui que não precisa receber relato
- Um prestador implanta barreiras para reduzir colisões de veículos no pátio. O que deve ser
- Uma organização pergunta qual obra internacional, em sua quarta edição, orienta os fundame
- Durante a manutenção, um colaborador informa espontaneamente um erro de boa-fé que foi cor
- Uma organização passa a utilizar aeronaves de tipo diferente e altera sua escala de manute
- Um centro de operações recebe relatos de condições inseguras e quer decidir como classific
- Um prestador elabora seu SMS conforme o Anexo 19 e o Doc 9859. Qual estrutura corresponde 
- Uma equipe planeja um curso sobre novas regras de comunicação de ocorrências aeronáuticas.
- Uma organização pretende comparar segurança em dois períodos: houve cinco incursões em pis
- Uma companhia estabelece reduzir aproximações instáveis e acompanha a taxa mensal por mil 
- Um aeródromo criou banco de relatos, padronizou categorias e validou registros antes de an
- Uma entidade pública define objetivos nacionais de segurança, coordena supervisão e acompa
- Um trabalhador teme informar quase colisão porque seu nome pode ser divulgado. Qual encami
- A direção anuncia uma política de segurança, mas não define responsáveis, recursos nem pro
- Uma equipe revisa métricas de aproximação, avalia eficácia de barreiras e ajusta processos
- A organização compara relatos de várias unidades e nota que os registros de aproximação in
- Uma empresa analisa um risco de baixa frequência, mas de consequências potencialmente cata
- Um gestor observa aumento nos relatos voluntários após campanha de confiança. Ao mesmo tem
- Em um plano de riscos, a equipe lista vazamento de combustível, possibilidade de incêndio,
- Durante uma análise institucional, a equipe procura a norma que criou a autarquia especial
- Uma avaliação sobre o setor aeronáutico precisa separar poder de fiscalização da agência e
- A autoridade avalia um caso de compartilhamento de dados de segurança protegidos, com soli
- Uma unidade compara a Lei nº 10.871/2004 com a Lei nº 11.182/2005. Qual distinção preserva
- Em um pátio de aeronaves, a presença de veículo em local de circulação pode ser classifica
- Uma empresa não registrou incidentes no último semestre e, por isso, pode considerar elimi
- Uma equipe oferece treinamento periódico sobre notificação de condições inseguras. A ação 
- Um prestador identifica perigo e considera desnecessária qualquer avaliação de risco, porq
- Ao implantar um sistema de gerenciamento da segurança operacional, uma organização deve co
- A Lei nº 10.871/2004, que disciplina carreiras das autarquias reguladoras, é o diploma res
- Um prestador decide acompanhar o número de incursões em pista por mil movimentos. Esse dad
- Após receber relatos sobre novo perigo, o setor de segurança decide que a criação de uma m
- Um centro de segurança classifica relatos por taxonomia padronizada antes de produzir séri
- No sistema de segurança de um prestador, a análise de mudança operacional pertence princip
- Uma operação passa a utilizar pista com maior movimento noturno. A decisão de reavaliar pe
- Se a autoridade estatal institui programa de segurança da aviação civil, o operador de aer
- A Lei nº 7.565/1986 prevê, no art. 11, soberania completa e exclusiva do Brasil sobre o es
- O sistema de coleta e processamento de dados de segurança deve preservar relatos sem anali
- Uma autoridade examina pedido de acesso a informações de segurança protegidas e analisa as
- O gestor de segurança de um prestador, ao ser formalmente nomeado, substitui a responsabil
- Um prestador adota índice de relatos por movimento, mas verifica que o aumento de registro
- A Lei nº 11.182/2005 confere à agência de aviação civil representação internacional sem re
- A Lei nº 10.871/2004, ao tratar de carreiras das autarquias reguladoras, prevê atividades 
- A adoção de um indicador com redução de ocorrências registrada em números absolutos demons
```

## Pedido 31: Perícia contábil: laudo, parecer e quesitos (`contabilidade.pericia-contabil`, 26 tópicos de edital)

```
Matéria: contabilidade.pericia-contabil (Perícia contábil: laudo, parecer e quesitos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, modalidades e responsabilidade técnica": 17
- "2. Planejamento, diligências e procedimentos da NBC TP 01": 24
- "3. Laudo, parecer, quesitos e representações da administração": 29
- "4. Atualização monetária, juros e mora nos cálculos periciais": 17
- "5. Casos integrados, quesitos complexos e controle da qualidade": 13

Já existem (não repita):
- Em conflito sobre lançamentos de receitas, foi nomeado contador regularmente inscrito no C
- Uma perícia voluntária é contratada fora de processo judicial para conferir saldo patrimon
- Antes de propor honorários para examinar centenas de documentos, o contador estima etapas,
- Em exame de extratos, o perito solicita livros e comprovantes de datas específicas mediant
- O contador confronta notas fiscais, razão e lançamentos contábeis de uma entidade para ver
- Uma assistente técnica examina o laudo apresentado e oferece documento com críticas à base
- Para uma prova aritmética, o título de R$ 10.000,00 deve ser atualizado pelo fator forneci
- Foi fixado para um exercício capital de R$ 10.000,00, juros simples de 1% ao mês e período
- Em perícia judicial, uma parte quer indicar assistente e formular quesitos após a intimaçã
- Durante uma diligência pericial, uma parte apresenta quesitos relacionados a novo ponto té
- Um perito recebe de assistente planilha e memorial que esclarecem o cálculo e os utiliza n
- Para confrontar duas demonstrações patrimoniais e destacar diferenças de saldos, o contado
- Um documento pericial inclui planilha feita pelo próprio contador e extrato bancário entre
- Uma entidade propõe entregar carta declarando que todos os registros são verdadeiros e ped
- Perícia sobre obrigação civil sem índice de atualização contratado e sem previsão legal es
- Em demonstrativo de juros legais civis pós-2024, a equipe quer empregar capitalização comp
- Em liquidação de obrigação civil regida supletivamente pelo art. 406 do Código Civil após 
- Duas partes divergem sobre qual premissa contratual deve prevalecer, mas ambas autorizam a
- A memória registra principal atualizado de R$ 12.000,00 na data-base e pagamento parcial d
- Na primeira manifestação, contador nomeado apresenta registro no CRC e é solicitado a demo
- Em diligência judicial, o detentor de arquivos recusa por escrito entregar extratos do per
- O prazo original para entrega do laudo foi fixado em 30 dias. Com motivo justificado, o co
- Partes capazes, em causa disponível à autocomposição, decidem escolher conjuntamente profi
- Depois do laudo, persiste obscuridade técnica relevante para a decisão. O juiz determina s
- Em exercício pericial, principal de R$ 8.000,00 recebe fator 1,05 e depois juros simples d
- Em litígio sobre contas comerciais, um contador regular no CRC é nomeado para verificar re
- Quando a perícia é contratada voluntariamente e não há processo judicial, a NBC TP 01 (R2)
- Uma proposta de honorários foi preparada a partir do objeto, do volume de documentos e de 
- O termo de diligência e o laudo pericial contábil têm a mesma finalidade: ambos comunicam 
- O contador analisa documentos, registros de transações e livros para confirmar os valores 
- O assistente técnico contratado pela parte deve apresentar laudo do juízo e decidir sobre 
- Um fator de correção acumulado igual a 1,04 sobre principal de R$ 5.000,00, sem juros ou p
- Depois da nomeação pericial, as partes têm cinco dias para indicar assistentes e apresenta
- Durante a diligência, surgem quesitos suplementares formulados por uma parte. O CPC prevê 
- Em um trabalho regido pela NBC TP 01 (R2), o perito pode deixar de registrar a recusa de e
- Na NBC TP 01 (R2), o item 35 relaciona tanto a comparação de demonstrações patrimoniais qu
- No âmbito da NBC TP 01 (R2), o extrato bancário recebido da parte constitui apêndice, enqu
- Uma carta assinada pela administração pode integrar o conjunto documental de uma perícia, 
- O art. 406 do Código Civil, após a Lei 14.905/2024, fixa a taxa legal em um por cento ao m
- Em liquidação judicial, o assistente técnico é autorizado pela parte a colaborar com a per
- Durante uma perícia, o contador descobre erro documental relevante que integra o objeto de
- Em cálculo supletivo regido pela taxa legal do Código Civil após 2024, a metodologia do CM
- Uma dívida corrigida de R$ 8.000,00 para R$ 8.400,00 recebe juros simples de 0,5% sobre o 
- Depois de impugnação fundada e persistência de obscuridade, foi determinada segunda períci
- Quando uma perícia de maior complexidade necessita de métodos especializados externos, a a
```

## Pedido 32: Lei Orgânica da Advocacia-Geral da União (LC nº 73/1993) (`legislacao.lei-organica-agu-lc-73`, 25 tópicos de edital)

```
Matéria: legislacao.lei-organica-agu-lc-73 (Lei Orgânica da Advocacia-Geral da União (LC nº 73/1993))
Capítulos e quantas questões quero em cada um (total 100):
- "1. A AGU na Constituição e suas funções": 20
- "2. Composição e órgãos da AGU": 20
- "3. PGU, Consultorias e PGFN": 20
- "4. Carreiras, deveres, proibições e correições": 20
- "5. Citações, pareceres, súmulas e nomeações": 20

Já existem (não repita):
- Segundo a Constituição e a LC nº 73/1993, a Advocacia-Geral da União é a instituição que:
- O Advogado-Geral da União é nomeado pelo Presidente da República, dentre cidadãos maiores 
- Na execução da dívida ativa de natureza tributária, a representação da União cabe, segundo
- O ingresso nas classes iniciais das carreiras da Advocacia-Geral da União far-se-á mediant
- Pela LC nº 73/1993, as Procuradorias e Departamentos Jurídicos das autarquias e fundações 
- São órgãos de direção superior da Advocacia-Geral da União, segundo a LC nº 73/1993:
- A Procuradoria-Geral da Fazenda Nacional subordina-se ao Advogado-Geral da União técnica e
- É atribuição do Advogado-Geral da União, segundo a LC nº 73/1993:
- Compete ao Advogado-Geral da União representar a União junto ao Supremo Tribunal Federal e
- A Corregedoria-Geral da Advocacia da União tem, entre suas atribuições:
- O Conselho Superior da AGU é presidido pelo Advogado-Geral da União, que tem voto de desem
- Segundo a LC nº 73/1993, a representação judicial da União perante os tribunais superiores
- Às Consultorias Jurídicas dos Ministérios compete examinar, prévia e conclusivamente, os t
- Para a LC nº 73/1993, NÃO se considera causa de natureza fiscal, de atuação da Procuradori
- A Procuradoria-Geral da Fazenda Nacional desempenha as atividades de consultoria e assesso
- Compete à Procuradoria-Geral da Fazenda Nacional, segundo a LC nº 73/1993:
- Os órgãos jurídicos das autarquias e fundações públicas, segundo a LC nº 73/1993, têm comp
- Para inscrição em concurso de ingresso nas carreiras da AGU, segundo a LC nº 73/1993, o ca
- Os dois primeiros anos de exercício em cargo inicial das carreiras da AGU correspondem a e
- Além das proibições decorrentes do exercício de cargo público, é vedado aos membros efetiv
- O membro efetivo da AGU que houver proferido parecer favorável à pretensão deduzida em juí
- Sobre as correições na AGU, é correto afirmar que:
- As promoções dos membros efetivos da AGU são processadas semestralmente pelo Conselho Supe
- Nas causas de competência do Supremo Tribunal Federal em que a União seja interessada, ela
- As intimações e notificações da União são feitas nas pessoas do Advogado da União ou do Pr
- O parecer do Advogado-Geral da União aprovado pelo Presidente da República e publicado jun
- É privativo do Presidente da República submeter assuntos ao exame do Advogado-Geral da Uni
- Quanto à Súmula da Advocacia-Geral da União, a LC nº 73/1993 dispõe que:
- O Procurador-Geral da Fazenda Nacional é nomeado pelo Presidente da República mediante ind
- Segundo a LC nº 73/1993, são escolhidos obrigatoriamente dentre os membros efetivos da Adv
- Os pareceres das Consultorias Jurídicas aprovados pelo Ministro de Estado obrigam também o
- Os cargos de Procurador-Geral da União, Procurador-Geral da Fazenda Nacional, Consultor-Ge
- Pela LC nº 73/1993, o Advogado-Geral da União pode delegar ao Procurador-Geral da União a 
- Aos titulares de cargos de confiança e aos membros efetivos da AGU é vedado manter sob sua
- Os concursos públicos de ingresso nas carreiras da AGU devem ser realizados quando o númer
- Nas causas de natureza fiscal de competência dos juízos de primeiro grau, a União é citada
- Os membros efetivos da Advocacia-Geral da União têm os direitos e deveres previstos na Lei
- O parecer do Advogado-Geral da União aprovado pelo Presidente da República, mas não public
- Às Procuradorias da União organizadas em cada Estado e no Distrito Federal incumbe represe
- Sobre o Advogado-Geral da União, assinale a alternativa INCORRETA.
```

## Pedido 33: Direito Empresarial Digital: Startups, Assinatura Eletrônica, Ativos Virtuais e Dados (`direito-empresarial.direito-empresarial-digital`, 25 tópicos de edital)

```
Matéria: direito-empresarial.direito-empresarial-digital (Direito Empresarial Digital: Startups, Assinatura Eletrônica, Ativos Virtuais e Dados)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Startups e inovação (LC 182/2021)": 25
- "2. A empresa digital: sociedade, atos e documentos": 25
- "3. Comércio eletrônico, ativos virtuais e smart contracts": 25
- "4. Dados e segredos empresariais": 25

Já existem (não repita):
- Para a LC 182/2021, um dos requisitos de enquadramento como startup é ter receita bruta de
- Para enquadramento como startup na LC 182/2021, a empresa deve ter, no máximo:
- O investidor que aporta recursos em startup por meio de mútuo conversível, nos termos da L
- A desconsideração da personalidade jurídica da startup pode alcançar o investidor por inst
- São instrumentos de investimento em startups previstos na LC 182/2021, que não integram o 
- O ambiente regulatório experimental (sandbox regulatório) permite que o órgão regulador af
- A modalidade especial de licitação criada pela LC 182/2021 destina-se a:
- O Inova Simples é regime especial simplificado que concede às iniciativas autodeclaradas d
- Com a Lei 14.195/2021, as empresas individuais de responsabilidade limitada (EIRELI) exist
- A legislação brasileira admite a participação e o voto a distância em assembleias e reuniõ
- A Lei de Liberdade Econômica assegura o direito de arquivar documentos por meio digital, e
- Pela MP 2.200-2/2001, documentos eletrônicos assinados com certificado emitido no âmbito d
- Segundo a MP 2.200-2/2001, somente as assinaturas com certificado ICP-Brasil têm validade 
- Na classificação da Lei 14.063/2020, a assinatura eletrônica que utiliza certificado digit
- A assinatura eletrônica avançada, na Lei 14.063/2020, é a que:
- O registro empresarial nas juntas comerciais foi amplamente digitalizado, com atos de cons
- Um consumidor comprou um aparelho pela internet. Pelo art. 49 do CDC, ele pode desistir da
- O Decreto 7.962/2013 exige que os sites de comércio eletrônico informem, em local de desta
- Nas compras coletivas pela internet, o Decreto 7.962/2013 exige a informação de:
- Pela Lei 14.478/2022, as prestadoras de serviços de ativos virtuais dependem de autorizaçã
- Segundo a Lei 14.478/2022, o conceito de ativo virtual não abrange:
- A Lei 14.478/2022 incluiu no Código Penal o crime de fraude com a utilização de ativos vir
- Quanto aos smart contracts, é correto afirmar:
- Quando um token negociado em plataforma digital configurar valor mobiliário, a regulação a
- A blockchain pode ser descrita como um registro distribuído e encadeado, resistente a alte
- A proteção do segredo empresarial no Brasil depende de prévio registro do segredo no INPI.
- Um ex-empregado divulga a um concorrente, sem autorização, a fórmula confidencial de um pr
- Na LGPD, a pessoa natural ou jurídica a quem competem as decisões referentes ao tratamento
- A multa simples prevista na LGPD pode chegar a 2% do faturamento da pessoa jurídica no Bra
- Sobre dados anonimizados, a LGPD estabelece que:
- O tratamento de dados pessoais pela empresa exige o enquadramento em uma base legal, como 
- Em operações de fusão e aquisição de empresas digitais, a análise de bases de dados, de su
- O encarregado pelo tratamento de dados pessoais, na LGPD, tem como função:
- Uma startup com receita bruta de R$ 20 milhões no ano anterior, ainda que inovadora e com 
- Sobre a relação entre o investidor de startup e a recuperação judicial da empresa investid
- A escrituração contábil e fiscal das empresas no Brasil é feita, em grande parte, em ambie
- Uma empresa mantém em sigilo sua base de clientes, com acesso restrito e acordos de confid
- O direito de arrependimento previsto no art. 49 do CDC aplica-se às contratações realizada
- Entre as sanções administrativas que a ANPD pode aplicar com base na LGPD, inclui-se:
- O programa de ambiente regulatório experimental previsto na LC 182/2021 é instituído:
```

## Pedido 34: Prescrição intercorrente no processo do trabalho (`processo-trabalho.prescricao-intercorrente`, 25 tópicos de edital)

```
Matéria: processo-trabalho.prescricao-intercorrente (Prescrição intercorrente no processo do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, legislação e delimitação da prescrição intercorrente": 20
- "2. Prazo bienal e determinação descumprida pelo exequente": 20
- "3. Direito intertemporal: Lei nº 13.467/2017 e IN nº 41/2018 do TST": 20
- "4. Declaração provocada ou de ofício e consequências na execução": 20
- "5. Casos integrados, sequência decisória e distinções de prova": 20

Já existem (não repita):
- Em execução trabalhista, o executado sustenta que a inércia da parte credora ultrapassou o
- Na execução, o juízo determinou ao exequente a prática de um ato necessário e este não o c
- Após constatar os pressupostos legais de prescrição intercorrente, o magistrado observa qu
- Uma parte argumenta que a prescrição intercorrente pode ser declarada apenas pelo juízo da
- Um processo contém determinação judicial dirigida ao exequente antes da reforma e outra ex
- O executado não pagou a condenação por três anos, mas o exequente cumpriu todas as determi
- Em um estudo histórico do art. 11-A, identifica-se a data a partir da qual entrou em vigor
- Um material de estudo atualizado em 2026 afirma que a Súmula nº 114 do TST ainda proíbe a 
- O processo de execução permanece sem andamento há mais de dois anos, porque a secretaria n
- O título executivo foi constituído em 2014. O juiz expediu ordem ao exequente em 2019 e el
- Em 2022, o juiz determinou que o exequente praticasse providência em quinze dias. A parte 
- Na execução, o exequente descumpriu validamente uma determinação de 2021 e não houve outra
- Numa execução, o credor não atende ao despacho, mas o executado não apresenta incidente ne
- Após atender integralmente à determinação judicial, o exequente aguarda dois anos por prov
- Um despacho dirigido ao credor foi proferido em setembro de 2017, e seu descumprimento se 
- O magistrado identifica eventual prescrição intercorrente e pretende examiná-la sem pedido
- Em comparação de dois autos, um título foi formado em 2015 e houve nova ordem em 2022; no 
- Execução iniciada em 2010 possuía determinação ao credor expedida em 2016. A parte descump
- O exequente recebeu regularmente em 2020 determinação judicial clara, não a cumpriu após s
- Uma apostila produzida em 2026 usa como fundamento vigente a antiga Súmula 114 do TST, sem
- A parte executada pede pronúncia de prescrição intercorrente com fundamento em despacho qu
- A ordem de 2021 previa que o exequente entregasse documento em vinte dias. A parte o apres
- O estudo da transição processual identifica o ato do TST que aprovou a IN nº 41/2018 e seu
- Um professor pede identificar o ato formal que, em junho de 2025, retirou a antiga Súmula 
- Em execução anterior à reforma, a determinação não cumprida foi feita em 10 de novembro de
- No curso de execução, o exequente descumpre determinação judicial regularmente comunicada;
- O termo inicial do art. 11-A, § 1º, da CLT corresponde obrigatoriamente ao trânsito em jul
- Na vigência do art. 11-A da CLT, o juiz pode reconhecer prescrição intercorrente de ofício
- A prescrição intercorrente trabalhista atualmente depende, em todos os casos, de petição d
- A Lei nº 13.467/2017 introduziu o art. 11-A na CLT e entrou em vigor em 11 de novembro de 
- Em outubro de 2026, continua vigente e aplicável a Súmula nº 114 do TST, que impede generi
- Em execução cujo título foi formado em 2013, o juiz determina em 2020 providência ao exequ
- Em execução iniciada antes de 2017, qualquer determinação judicial proferida em 2016 que n
- Se o juízo concede ao exequente oportunidade para cumprir determinação na execução, a simp
- A ausência de bens penhoráveis por dois anos constitui, por previsão expressa do art. 11-A
- A Resolução nº 221, de 21 de junho de 2018, editou a IN nº 41 do TST, cujo art. 2º trata e
- A possibilidade legal de pronunciar prescrição intercorrente de ofício impede que o exeque
- A mera demora da secretaria da unidade judiciária na execução não deve ser tratada, por si
- O art. 2º da IN nº 41/2018 utiliza como condição temporal indispensável a formação do títu
- Em execução, o juiz identifica ordem de 2021 regularmente dirigida ao exequente, omissão c
- A Resolução TST nº 225/2025 revogou o art. 11-A da CLT juntamente com a Súmula 114, restab
- Em hipótese com determinação de 2019 e cumprimento tempestivo do exequente, a paralisação 
- A execução trabalhista ajuizada antes da Lei nº 13.467/2017 está definitivamente imune à p
- Embora a antiga Súmula nº 114 do TST negasse historicamente a prescrição intercorrente, se
- Em uma execução antiga, o juiz pode reconhecer prescrição intercorrente apenas porque o pr
```

## Pedido 35: Navegação aérea, planejamento de voo e despacho (`aviacao.navegacao-planejamento-voo`, 25 tópicos de edital)

```
Matéria: aviacao.navegacao-planejamento-voo (Navegação aérea, planejamento de voo e despacho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Coordenadas, navegação estimada e cartas aeronáuticas": 16
- "2. Radionavegação, GNSS, PBN e aviônicos": 22
- "3. Planejamento, perfis de voo e combustível": 15
- "4. Peso, balanceamento e gráficos de desempenho": 21
- "5. Falha de motor, voos longos e despacho integrado": 26

Já existem (não repita):
- Uma carta indica um trecho de 180 NM, e a velocidade no solo prevista é 120 kt. Desconside
- Um navegador identifica uma aeronave no radial 090 de um VOR. Qual interpretação posiciona
- Durante um voo VFR diurno de avião sob os requisitos gerais do RBAC 91, sem o caso excepci
- Uma aeronave transporta 800 kg a um braço de 2 m e 200 kg a 4 m do datum. Qual é a posição
- Em uma etapa de cruzeiro, a GS é de 180 kt e o fluxo de combustível é de 60 kg/h. Qual é o
- Um piloto observa que o DME mostra distância não nula ao cruzar verticalmente uma instalaç
- Um avião VFR noturno sujeito ao RBAC 91.151 chegará ao primeiro local de pouso com combust
- Um operador dispõe de carta de rota e de carta de aproximação por instrumentos para um des
- O planejamento apresenta limite estrutural de decolagem de 4.000 kg e limite de desempenho
- O trecho possui rumo verdadeiro 140° e variação magnética de 15°W. Sem considerar deriva n
- Em uma região sem auxílios terrestres próximos, o sistema de navegação combina GNSS e unid
- Em um exemplo didático IFR de avião, são previstos 100 kg até o destino, 40 kg até a alter
- Um plano informa peso de rampa de 5.250 kg e prevê 75 kg de combustível consumidos antes d
- Um helicóptero executa operação IFR sob a regra geral do RBAC 91, com destino e alternativ
- O indicador ADF de cartão fixo apresenta marcação relativa de 050°, e a proa magnética da 
- Duas velocidades de cruzeiro são comparadas para a mesma aeronave e condição: uma maximiza
- Uma aproximação publicada exige capacidade RNP compatível. A aeronave dispõe de receptor G
- Um trecho de 300 NM será voado com TAS constante de 180 kt e vento de proa alinhado de 30 
- Um avião multimotor apresenta tabela com razão de subida normal e tabela específica OEI. A
- Duas alternativas estão a 700 NM ao longo de uma rota reta. Para desviar de um ponto inter
- Uma rota remota ultrapassa a cobertura contínua de auxílios terrestres. O sistema GNSS da 
- Em uma aeronave, há 1.000 kg fixos a braço 2 m e 300 kg de carga móvel inicialmente a 4 m.
- Uma operação comercial está sujeita a requisitos específicos de combustível e despacho, al
- Uma tabela de desempenho apresenta combustível acumulado desde a decolagem até 5.000 ft de
- No planejamento integrado, o peso operacional sem combustível é 2.700 kg, a carga é 550 kg
- Em uma navegação estimada, 150 NM percorridas a 100 kt de velocidade no solo correspondem 
- Em uma rota sobre a latitude 60°N, um grau de longitude conserva aproximadamente 60 NM, co
- Durante uma aproximação instrumental, a mera apresentação de coordenadas por GNSS certific
- Ao preparar um destino, o planejador deve conferir se a informação da carta publicada perm
- Para TAS de 160 kt e vento de proa alinhado de 25 kt, a GS aproximada será 135 kt, desde q
- Um sistema de navegação inercial mantém estimativa de posição sem referência externa e, po
- Quando a aeronave cruza a vertical de uma estação DME a altitude considerável, a distância
- Em planejamento de saída e chegada, uma carta SID pode substituir uma carta STAR porque am
- Na regra geral do RBAC 91.151, um helicóptero VFR deve dispor, além do voo até o primeiro 
- Para helicóptero IFR sob o RBAC 91.167, após destino e alternativa, o adicional geral corr
- Em um gráfico de subida com dados acumulados desde a decolagem, o consumo entre duas altit
- Para uma mesma aeronave, velocidade de melhor alcance e velocidade de máxima autonomia são
- Para calcular o CG, somam-se os produtos dos pesos pelos respectivos braços e divide-se a 
- Em um avião bimotor de transporte, ao planejar eventual falha de motor em rota, basta redu
- Em rota remota, o planejamento de desvio deve avaliar capacidade de navegação e comunicaçã
- Se uma aeronave possui 720 kg de combustível de rampa e consome 40 kg no táxi, o combustív
- Num cruzeiro de 50 minutos a fluxo constante de 72 kg/h, o gasto correspondente é 72 kg, p
- Em um ETP situado entre duas alternativas, os tempos de desvio se igualam; havendo GS dife
- Em operação sujeita ao RBAC 121, é suficiente adotar automaticamente os 30 minutos de aviã
- Em uma operação planejada, atender ao limite de massa de decolagem não dispensa verificar 
```

## Pedido 36: Perspectivas sobre a Gestão de Riscos e Desastres (`defesa-civil.perspectivas-sobre-gestao-riscos-desastres`, 25 tópicos de edital)

```
Matéria: defesa-civil.perspectivas-sobre-gestao-riscos-desastres (Perspectivas sobre a Gestão de Riscos e Desastres)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Risco, ameaça e desastre: conceitos fundamentais": 25
- "2. PNPDEC, legislação atual e compromissos internacionais": 25
- "3. COBRADE e os cinco eixos de atuação": 25
- "4. Reescrita e como a prova cobra": 25

Já existem (não repita):
- À luz do art. 3º da Lei nº 12.608/2012, assinale a alternativa que apresenta todos os eixo
- Qual medida se relaciona mais diretamente à terceira prioridade de Sendai?
- Um município sofre danos extensos e sua capacidade de resposta fica substancialmente compr
- Um relatório usa código COBRADE para descrever o tipo de ocorrência e, em documento distin
- Ao interpretar o Marco de Sendai, assinale a alternativa que melhor descreve a distribuiçã
- Um plano de prevenção de inundação abrange municípios conectados pelo mesmo sistema de dre
- Dois bairros recebem precipitação semelhante; um tem moradias expostas, pouca mobilidade e
- Após uma enchente que alcança três municípios, uma equipe propõe mapear a bacia, revisar o
- Uma cidade combina zoneamento que impede novas moradias em área suscetível, barreiras que 
- Em 2026, um candidato estuda o instrumento nacional de planejamento e afirma que ele subst
- Após evacuação preventiva, uma família permanece na casa de parentes e outra precisa de ab
- Uma empresa cujo empreendimento apresenta risco de desastre elabora plano de contingência.
- Sobre metas globais e prioridades do Marco de Sendai, assinale a alternativa que preserva 
- Durante uma enxurrada, equipes resgatam moradores, prestam atendimento urgente, distribuem
- Após inundação, a reconstrução de moradias preserva os mesmos pontos baixos, mantém serviç
- Uma ficha registra duas ocorrências: movimento de massa após instabilidade de encosta e li
- Um programa combina revisão do uso do solo para evitar novas exposições, preparação para r
- Uma prefeitura planeja reduzir a ocupação em encosta instável, atualizar mapeamentos com e
- Em uma área de barragem, são propostas quatro ações: prever alertas, responsáveis e logíst
- Após inundações repetidas, uma equipe propõe recuperar casas e vias sem alterar a exposiçã
- Após vazamento de produto perigoso em instalação industrial, a Defesa Civil precisa regist
- A administração decide instalar sensores, manter equipe técnica de plantão e emitir alerta
- A respeito das competências locais previstas na Lei nº 12.608/2012, assinale a alternativa
- Depois de um deslizamento, um programa recupera serviços públicos, restaura o ambiente e r
- Um centro de operações mantém protocolos testados, define responsáveis por alertas e prevê
- Em relação a risco, evento adverso e desastre, assinale a alternativa correta.
- A Lei nº 12.608/2012 trata da incerteza quanto ao risco de desastre. Assinale a interpreta
- Sobre a Classificação e Codificação Brasileira de Desastres, assinale a alternativa corret
- O Marco de Sendai para Redução do Risco de Desastres foi adotado para o período 2015–2030.
- Uma equipe municipal capacita moradores, testa rotas de fuga e exercita o recebimento de a
- A PNPDEC deve integrar-se a políticas setoriais como ordenamento territorial, saúde e meio
- Na COBRADE, um código numericamente maior sempre representa desastre mais grave do que um 
- Canais de alerta acessíveis, equipes treinadas e rotas seguras podem influenciar as perdas
- A preparação só pode ocorrer quando o perigo se torna iminente, pois planos e treinamentos
- Em uma cheia que atravessa mais de um município, observar conexões a montante e a jusante 
- As metas globais de Sendai determinam automaticamente se um município deve receber reconhe
- Entre os objetivos da PNPDEC está estimular cidades resilientes e processos sustentáveis d
- A COBRADE apresenta níveis hierárquicos de classificação, incluindo categoria, grupo, subg
- O código COBRADE representa diretamente o grau de vulnerabilidade social das pessoas expos
- À União compete coordenar o SINPDEC em articulação com os Estados, o Distrito Federal e os
- A probabilidade de perdas pode ser nula sempre que um território ainda não tiver registrad
- A primeira prioridade de Sendai é compreender o risco em dimensões que incluem ameaça, exp
- Um plano de contingência limitado a abrigos pode omitir recursos de prevenção, preparação 
- Uma prioridade de Sendai é aprimorar a preparação para resposta eficaz e reconstruir melho
- Os cinco eixos brasileiros formam sequência cronológica obrigatória e não podem se sobrepo
```

## Pedido 37: Barragens (`minas-gerais.barragens`, 24 tópicos de edital)

```
Matéria: minas-gerais.barragens (Barragens)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Barragens em Minas Gerais: conceitos, tipos e inventários": 15
- "2. Política Nacional de Segurança de Barragens: alcance, classes e instrumentos": 32
- "3. Segurança de barragens em Minas Gerais: legislação, fiscalização e emergência": 23
- "4. Monitoramento, prevenção e impactos socioambientais": 15
- "5. Resíduos, efluentes, disposição final e resposta socioambiental": 15

Já existem (não repita):
- Uma empresa construiu estrutura de terra destinada a acumular água para uso industrial. Em
- Uma pesquisadora consulta a publicação anual da Feam e conclui que ela apresenta os dados 
- Uma barragem acumula quantidade de água abaixo do limite volumétrico mencionado na PNSB, m
- Uma estrutura apresenta bom estado de conservação, mas eventual ruptura atingiria uma popu
- Empreendedor de barragem de rejeitos minerais classificada com DPA baixo questiona a neces
- Uma comunidade está no trecho do vale em que a inundação hipotética chegaria antes de qual
- Responsável por barragem diz que a presença de técnicos fiscalizadores transfere ao poder 
- Uma inspeção registra problema de drenagem que pode evoluir e gerar liberação incontroláve
- Em estudo sobre barragens mineiras, uma equipe relaciona dois rompimentos históricos com s
- Uma empresa quer cumprir a hierarquia de gestão de resíduos sólidos, destinando rejeitos a
- Um projeto de barragem de água apresenta 12 m de altura, volume inferior a três milhões de
- Uma barragem em condições técnicas satisfatórias permanece próxima a uma área densamente h
- Empreendedor mineiro pretende programar auditoria de segurança de barragem abrangida pela 
- Uma barragem mineira alcançada pela Lei nº 23.291/2019 passa por auditoria com recomendaçõ
- Uma fiscalização encontra barragem de rejeitos de mineração alteada a montante e fora de o
- Um relatório de monitoramento registra pressão nos poros crescente medida por piezômetro, 
- Após ruptura de estrutura com rejeitos, a água de um curso a jusante torna-se visualmente 
- Durante treinamento, um técnico sugere classificar como acidente um aumento de fissura sem
- Após inspeção, constata-se que comunidade no vale necessita de orientação sobre alertas, e
- Instalação mineral passa a filtrar parte do rejeito antes da destinação, reduzindo seu teo
- Estudo estadual de cenário de ruptura identifica comunidade a jusante, situada além dos pr
- Em outubro de 2026, um servidor consulta normas nacionais de barragens minerárias e afirma
- Uma barragem de mineração enquadrada na PNSB sofre vazamento e causa danos ambientais. O e
- A autoridade competente identifica descumprimento prolongado de exigência de segurança e c
- Após rompimento de barragem mineral, gestor decide tratar sedimentos sólidos e devolver di
- Uma barragem destinada ao armazenamento de água e outra destinada à contenção de rejeitos 
- Uma empresa possui barragem hidrelétrica em Minas Gerais e declara que, por estar no terri
- Uma barragem conservada pode receber CRI baixo e DPA alto, porque a categoria de risco lev
- O Plano de Segurança da Barragem é documento dedicado apenas à retirada de moradores em em
- O empreendedor de barragem permanece responsável por sua segurança apesar da existência de
- Segundo a Lei nº 23.291/2019, a Feam deve publicar inventário anual, mas a norma estadual 
- Em outubro de 2026, a ANM segue referência da Resolução nº 95/2022 na segurança de barrage
- Um relatório localiza o rompimento de Fundão em 2015 na bacia do rio Paraopeba e o de Córr
- A avaliação de efeitos de barragens exige considerar não só a segurança do maciço, mas tam
- Uma empresa aplica a ordem da Política Nacional de Resíduos Sólidos começando pela disposi
- Uma barragem de rejeitos de mineração possui CRI e DPA baixos e o empreendedor pretende op
- Reservatório industrial contendo resíduo perigoso possui maciço inferior a 15 m, capacidad
- No âmbito específico da Lei mineira nº 23.291/2019, a periodicidade da auditoria técnica d
- Em projeto sujeito à Lei mineira nº 23.291/2019, uma comunidade situa-se além de 10 km da 
- Em parecer datado de outubro de 2026, a equipe aplica a Resolução ANM nº 95/2022 às barrag
- A mudança introduzida pela Lei nº 15.355/2026 no art. 3º, I, da PNSB suprimiu a atenção às
- Ao apurar infração administrativa de segurança de barragem, a autoridade deve respeitar co
- Leitura de piezômetro indica anomalia persistente e surgência de água turva no talude. Com
- Uma instalação industrial que espessa e filtra rejeitos reduz o teor de água do material r
- Uma mineradora contrata empresa especializada para transportar e dar destinação a rejeitos
```

## Pedido 38: Raciocínio crítico: hipóteses e planos de ação (`raciocinio-logico.raciocinio-critico-planos-acao`, 24 tópicos de edital)

```
Matéria: raciocinio-logico.raciocinio-critico-planos-acao (Raciocínio crítico: hipóteses e planos de ação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Argumentos, razões e hipóteses explicativas": 26
- "2. Premissas subjacentes, necessidade e força das evidências": 19
- "3. Formulação e escolha de planos: eficácia, eficiência e conveniência": 22
- "4. Riscos, condições de sucesso e evidências que alteram planos": 18
- "5. Decisão integrada, restrições e revisão do plano": 15

Já existem (não repita):
- Depois de instalar novos painéis informativos, uma biblioteca registrou redução de dúvidas
- Um setor afirma que novo horário causou menos faltas porque a redução começou no mês segui
- Uma equipe quer enviar mensagens para diminuir faltas provocadas por esquecimento. Qual co
- O plano para reduzir tempo de atendimento tinha meta de 20 minutos; após implantação, o in
- Duas propostas produzem 100 atendimentos adicionais de qualidade equivalente: X custa 2.00
- Uma proposta depende de peças de um único fornecedor, que anuncia paralisação por dois mes
- Após distribuir folhetos sobre prevenção, uma equipe afirma ter comprovado queda no número
- Um setor pretende digitalizar o atendimento e espera atender também pessoas sem equipament
- Antes de um programa, foram observados 40 erros em 400 atendimentos; depois, 30 em 200. O 
- O limite de um projeto é 25 mil e a meta mínima é 50 solicitações extras: plano P custa 20
- Uma administração propõe trocar todo o agendamento telefônico por um sistema on-line, gara
- Um relatório conclui que a capacitação elevou a produtividade porque as unidades que aderi
- Uma sala apresenta infiltração. Duas hipóteses são consideradas: H1, vazamento de tubulaçã
- Duas intervenções mantêm igual qualidade: plano M custa 18 mil e gera 600 entregas adicion
- Um plano de reduzir filas atingiu tempo médio inferior à meta, mas metade dos usuários pri
- Um plano depende de capacitar uma equipe e depois instalar equipamento. Todos os treinamen
- Uma campanha reduziu reclamações de 100 para 70, mas no mesmo período o formulário eletrôn
- Uma alternativa tem benefício de 80 com probabilidade de 0,75 e benefício de 20 com probab
- Uma avaliação compara unidades com e sem nova rotina. As primeiras passaram de 100 a 70 er
- Um plano promete reduzir perdas de água instalando sensores. Um parecer informa que 90% da
- O plano de reduzir acidentes em área industrial pressupõe que supervisores aplicarão uma l
- Um estudo considera dois planos para reduzir falhas: R custa 30 mil, reduz 60 e atende à m
- Em um piloto digital, 95% dos voluntários experientes completaram uma tarefa. Propõe-se ex
- Uma proposta custa 50 com certeza e oferece ganho de 100 com probabilidade 0,7 ou zero com
- Uma proposta afirma: 'Se houver verba suficiente, o plano será realizado'. O relatório com
- Ao investigar queda nas reclamações, uma pessoa formula a hipótese de que instruções mais 
- Uma equipe encontrou o melhor resultado após alterar o horário e concluiu que a alteração 
- Um plano de lembretes pretende diminuir ausências por esquecimento. A suposição de que os 
- Dois planos atingiram a mesma meta com custos distintos; portanto, têm obrigatoriamente ef
- Em campanha de redução de erros, o número de oficinas realizadas é indicador de atividade 
- Um novo formulário digital reduziu reclamações de 60 para 40, mas tornou opcional registra
- Uma solução digital exige equipamentos funcionando e atendimento assistido. A existência d
- Um piloto com participantes selecionados exclusivamente entre os mais familiarizados com t
- Um programa custa 9 mil e entrega 300 unidades adicionais de qualidade igual à de outro qu
- Depois de entregar todos os equipamentos previstos, uma equipe conclui que a intervenção f
- Ao decidir entre alternativas, é coerente eliminar previamente a opção que ultrapassa o or
- Uma pesquisa identifica que a quantidade de reclamações caiu simultaneamente em várias uni
- Uma proposta só funcionará se sua equipe receber treinamento e se a plataforma estiver dis
- Uma ação reduziu o total de falhas de 20 para 18, mas o volume de operações caiu de 200 pa
- Se o planejamento afirma que 'com recursos suficientes o plano será concluído' e a conclus
- Uma intervenção apresentou saldo esperado de 30 conforme probabilidades informadas, portan
- Um projeto digital atingiu a meta de redução de espera no grupo conectado, mas excluiu a m
- Em avaliação de um plano, a descoberta de que os participantes receberam os materiais dent
- Um piloto de novo sistema é concluído por pessoas muito experientes, mas o plano será apli
- Uma alternativa cumpriu todos os marcos de instalação e não atingiu a meta de redução de d
```

## Pedido 39: Gêneros literários e elementos da narrativa (`literatura.generos-elementos-narrativa`, 23 tópicos de edital)

```
Matéria: literatura.generos-elementos-narrativa (Gêneros literários e elementos da narrativa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Literatura, linguagem artística e gêneros literários": 32
- "2. Voz lírica, poesia e texto dramático": 15
- "3. Foco narrativo, narrador e construção das personagens": 19
- "4. Enredo, conflito, tempo e espaço na narrativa": 19
- "5. Discursos, vozes, descrição e arquitetura do texto narrativo": 15

Já existem (não repita):
- Um poema afirma: 'Escondi o verão na gaveta das cartas', sem indicar que a experiência per
- Leia: 'LIA: Ouvi o portão. (Apaga a luz e se esconde atrás da mesa.)'. A expressão entre p
- Um conto mostra alguém perder a chave, procurar ajuda e descobrir que a porta fora aberta 
- Uma composição contém personagens que dialogam em cena e indicação '(senta-se lentamente j
- Em 'A saudade construiu uma casa no meu peito', a expressão desloca ações e objetos concre
- Leia: 'Eu atravessei a ponte e escondi a carta antes que meus colegas chegassem'. A presen
- Em 'Clara sorriu para o grupo, mas sentiu vergonha ao lembrar o erro', o narrador fala em 
- Em uma história, a protagonista quer concluir um projeto, mas sua irmã se opõe por temer r
- Um conto cresce em tensão até o instante em que a personagem precisa escolher entre revela
- Um narrador informa: 'O trem saiu às 6h20; a viagem durou duas horas e o retorno aconteceu
- Uma composição não apresenta rimas regulares nem número fixo de sílabas por verso, mas usa
- Uma personagem apresenta traços contraditórios desde o início, mas mantém seus valores cen
- O narrador não declara que Sérgio está ansioso, mas relata que ele confere a fechadura rep
- O conto inicia com a chegada de uma carta; depois informa: 'Dez anos antes, a remetente pr
- O narrador diz que uma reunião durou cinco minutos, mas a personagem a percebeu como horas
- Um trecho descreve corredor estreito de moradia coletiva, separação entre quartos de morad
- No texto '— Deixarei esta casa amanhã — declarou Bruno', uma reescrita posterior por narra
- Leia: 'Nádia fechou a gaveta. Outra carta? Justo agora, quando precisava esquecer tudo!' S
- Uma narrativa em terceira pessoa acompanha por páginas apenas as dúvidas de Elisa sobre um
- O narrador afirma que nunca escondeu nada do leitor, mas capítulos posteriores reproduzem 
- Na cronologia reconstruída, ocorreu primeiro um furto, depois investigação e por fim julga
- Uma narrativa diz 'Durante quatro anos, ela tentou encontrar o irmão' e em seguida pula pa
- Uma protagonista deseja expor uma fraude, mas teme perder a confiança da família e adia a 
- Leia uma cena: 'DORA: Eu concordo. (Faz uma pausa, rasga discretamente a carta.)'. Que con
- Num romance com dois relatos sobre o mesmo encontro, um narrador descreve um abraço como r
- Um texto em versos apresenta um personagem, um obstáculo e acontecimentos encadeados. Mesm
- Em qualquer obra ficcional narrada na primeira pessoa, o narrador corresponde à pessoa his
- O narrador que conta em primeira pessoa como presenciou acontecimentos da vida de uma amig
- No texto dramático, uma rubrica como '(caminha até a janela e silencia)' deve ser lida com
- Uma personagem pode ser antagonista ao impedir o objetivo de outra sem ter motivação maldo
- Na narrativa em terceira pessoa, a identificação de pensamentos de uma personagem é incomp
- Uma personagem pode ser redonda, por reunir motivações complexas, e ao mesmo tempo estátic
- Em 'Dois dias depois ela abriu a carta e se recordou da reunião da infância', a evocação d
- No relato de uma espera que dura dois minutos pelo relógio, mas parece interminável à pers
- O discurso indireto livre requer travessão inicial e verbo de elocução como 'perguntou', p
- Um mesmo apartamento pode ser descrito como espaço físico por suas paredes, social pela or
- Um desfecho aberto representa erro estrutural por não solucionar integralmente o conflito,
- Em 'Rosa fechou a porta. Voltaria? Jamais!', a interrogação e a recusa podem sugerir discu
- Se um narrador em primeira pessoa desconhece um segredo preservado por outra personagem, e
- Um livro pode apresentar o julgamento na abertura e, capítulos depois, a investigação que 
- Ao narrar 'Durante dez anos, a família aguardou notícias' em uma única frase, o escritor u
- Uma peça pode apresentar monólogo carregado de metáforas e lirismo sem deixar de ser predo
- O discurso direto e o indireto são intercambiáveis sem ajustes de referência: em relato po
- Ao alternar dois narradores sobre o mesmo fato, uma obra pode produzir sentidos divergente
- Se uma história contém diálogos e recursos figurados, a presença dessas marcas exige class
```

## Pedido 40: Aerodinâmica, mecânica de voo e propulsão aeronáutica (`engenharia-aeronautica.aerodinamica-voo-propulsao`, 23 tópicos de edital)

```
Matéria: engenharia-aeronautica.aerodinamica-voo-propulsao (Aerodinâmica, mecânica de voo e propulsão aeronáutica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de aerodinâmica: pressão, sustentação e arrasto": 16
- "2. Camada limite, Reynolds, estol e compressibilidade": 19
- "3. Mecânica de voo, equilíbrio, estabilidade e controles": 19
- "4. Desempenho, situações críticas e serviços de tráfego aéreo": 22
- "5. Propulsão aeronáutica: motores, turbinas, eficiência e combustíveis": 24

Já existem (não repita):
- Ao comparar dois pontos de uma mesma linha de corrente, aproximadamente horizontal, em esc
- Em túnel de vento, uma asa é ensaiada com densidade do ar de 1,2 kg/m³ e velocidade verdad
- Uma aeronave em curva experimenta separação significativa do escoamento após exceder a inc
- Uma equipe deseja comparar efeitos inerciais e viscosos no escoamento sobre um perfil. Qua
- Uma aeronave voa a 240 m/s em atmosfera na qual a velocidade local do som é 300 m/s. Qual 
- Durante aproximação, o piloto aciona flaps para uma configuração autorizada. Qual efeito a
- Em uma turbina a gás aeronáutica, que componente entrega trabalho ao compressor mediante e
- Duas massas de 800 kg e 200 kg estão localizadas, respectivamente, a 2 m e a 4 m de uma re
- Um perfil em túnel de vento apresenta densidade 1,2 kg/m³, velocidade 50 m/s, área de refe
- Para um ensaio de camada limite, são dados densidade 1,2 kg/m³, velocidade 30 m/s, corda d
- Uma aeronave realiza curva coordenada, nivelada e estacionária com inclinação de 60 graus.
- Em avião de configuração convencional, uma carga adicional é colocada na parte traseira, d
- Uma aeronave mantém aproximadamente a mesma velocidade equivalente e configuração em duas 
- Em uma aproximação para pouso, a velocidade no solo na frenagem inicial aumenta em 10%, ma
- Uma aeronave voa com 70 m/s de velocidade verdadeira relativa ao ar e enfrenta vento de pr
- Um motor idealizado produz empuxo pela aceleração de 20 kg/s de fluxo de 100 m/s para 300 
- Uma aeronave recebe orientação de um centro de controle de área durante fase de rota. Qual
- A polar simplificada de uma asa tem CD0 = 0,020 e constante de arrasto induzido K = 0,05, 
- Em uma condição de voo idealizada, o peso de uma aeronave cresce 44%, sem mudar densidade,
- Considere a mesma composição de ar como gás ideal em duas temperaturas absolutas de 256 K 
- Em motor turbofan de alta razão de derivação, qual mecanismo ajuda a explicar eficiência p
- Em certa condição de voo, uma hélice recebe 500 kW de potência no eixo e fornece tração de
- Um planador em voo estabilizado possui razão sustentação/arrasto igual a 12 em ar calmo, s
- Em um modelo estacionário simplificado de motor a reação, a variação de momento contribui 
- Uma aeronave passa a operar com peso 21% maior e densidade do ar 19% menor, mantendo área 
- Em um ensaio de asa com densidade, área e coeficiente de sustentação constantes, elevar a 
- Em um curso de aerodinâmica, atribuiu-se a sustentação de um perfil à necessidade física d
- Dois perfis são ensaiados com a mesma densidade, viscosidade e velocidade, mas a corda car
- Uma aeronave que entra em estol durante curva a velocidade superior àquela observada em vo
- Em uma turbina a gás, o compressor acrescenta energia ao ar para elevar pressão, enquanto 
- Em um carregamento com massas distintas distribuídas longitudinalmente, o centro de gravid
- Ao comparar duas condições de funcionamento de um motor a reação, o indicador TSFC pode se
- Duas aeronaves idênticas mantêm pressão dinâmica equivalente, peso e configuração iguais, 
- Em uma curva coordenada nivelada de 60 graus, o fator de carga ideal é dois, e a velocidad
- Um sistema fly-by-wire impede o estol em todas as condições, inclusive quando perde sensor
- Em perfil com gradiente adverso de pressão, a mistura mais intensa de uma camada limite tu
- O serviço de controle de tráfego aéreo substitui a responsabilidade do comandante pela con
- Uma aeronave com velocidade verdadeira de 70 m/s encontra vento de proa constante e alinha
- O Mach crítico de uma asa significa que toda a aeronave já atingiu Mach 1 no fluxo livre e
- No estudo de um compressor de turbina a gás, estol local de palhetas e surge global não sã
- A aproximação do solo por uma asa finita, mantendo configuração e condições comparáveis, a
- Para um gás ideal de composição e razão de calores específicos fixas, a velocidade do som 
- Na equação de empuxo de um motor a reação, a força resultante pode ser determinada em qual
- No modelo de polar CD = CD0 + K CL² e voo nivelado com peso fixo, é possível observar cres
- A alteração da ICA 100-12 e da ICA 100-37 anunciada para entrar em vigor em 26 de novembro
```

## Pedido 41: Receita de contratos com clientes (CPC 47) e instrumentos financeiros (CPC 48) (`contabilidade.receita-contratos-instrumentos-financeiros`, 23 tópicos de edital)

```
Matéria: contabilidade.receita-contratos-instrumentos-financeiros (Receita de contratos com clientes (CPC 47) e instrumentos financeiros (CPC 48))
Capítulos e quantas questões quero em cada um (total 100):
- "CPC 47: contratos e obrigações de performance": 20
- "CPC 47: preço, alocação e reconhecimento de receita": 20
- "CPC 47: ativos, passivos, custos e evidenciação": 20
- "CPC 48: classificação, reconhecimento e mensuração": 20
- "CPC 48: perdas esperadas, hedge e divulgação": 20

Já existem (não repita):
- No CPC 47, a emissão de nota fiscal antes de entregar o bem constitui, por si só, condição
- Um contrato verbal pode ser considerado contrato com cliente para o CPC 47, desde que crie
- Duas promessas com preços individuais separados na nota comercial são necessariamente duas
- A modificação de contrato que adiciona bens distintos por preço que reflete seus preços in
- Se a cobrança de um contrato depende de bônus por desempenho, o CPC 47 manda sempre reconh
- Na alocação de preço a obrigações distintas, o método geral utiliza os preços de venda ind
- Para reconhecer receita ao longo do tempo pela condição de ausência de uso alternativo, ba
- Uma obrigação pode ser satisfeita ao longo do tempo quando o cliente recebe e consome simu
- Nos serviços reconhecidos ao longo do tempo, a entidade é obrigada a usar sempre os custos
- Direito à contraprestação que depende apenas da passagem do tempo caracteriza, em regra, c
- A entrada de caixa de assinatura anual de serviço ainda não prestado costuma originar pass
- Comissão paga exclusivamente quando um contrato é ganho, caso se espere recuperá-la, jamai
- Custos anormais de materiais desperdiçados durante a execução devem ser ativados obrigator
- No CPC 48, dois títulos de dívida com fluxos SPPI podem receber categorias distintas por s
- Se um título de dívida expõe o investidor à variação do preço de commodities em seus fluxo
- O resultado acumulado em outros resultados abrangentes de investimento patrimonial não man
- A perda esperada de 12 meses equivale necessariamente ao valor das parcelas contratuais co
- Em contas a receber comerciais abrangidas pela abordagem simplificada obrigatória, a provi
- A compra de derivativo cambial para reduzir risco garante automaticamente o enquadramento 
- As NBC TSP 30, 31, 32 e 33 são normas voltadas ao setor público e não devem ser tratadas s
- Uma indústria vende máquina por R$ 24.000 e manutenção distinta, incluída no pacote, e o c
- Qual requisito NÃO integra o conjunto básico usado pelo CPC 47 para identificar contrato c
- Uma fabricante vende equipamento padronizado e treinamento que também pode ser adquirido d
- Contratos próximos, com o mesmo cliente, foram negociados como pacote para um objetivo com
- Um contrato de R$ 30.000 compreende bem A com preço individual de R$ 20.000 e serviço B di
- Sobre contraprestação variável no CPC 47, assinale a interpretação correta.
- Uma obrigação satisfeita ao longo do tempo possui preço alocado de R$ 200.000 e 40% de pro
- Uma entidade constrói bem sem uso alternativo, mas não possui direito exigível de pagament
- Em contrato de doze meses, o cliente paga R$ 12.000 antes de qualquer serviço, que será pr
- Uma empresa já transferiu o bem, mas o direito a receber ainda depende de concluir outro m
- Qual gasto possui melhor enquadramento como custo incremental para obtenção de contrato, a
- Uma empresa incorre em custo específico para preparar recursos que serão utilizados em ser
- Uma debênture gera somente fluxos contratuais SPPI e é administrada para receber principal
- Um título de dívida possui fluxos SPPI e integra carteira cujo objetivo é receber os fluxo
- Uma aplicação de dívida possui remuneração vinculada diretamente ao preço do ouro, fora da
- No reconhecimento inicial de ativo financeiro que será mensurado a valor justo por meio do
- Acerca de investimento em ações de outra entidade elegível à opção irrevogável de VJORA no
- Uma carteira de recebíveis comerciais sem componente de financiamento significativo enquad
- Uma operação de crédito, inicialmente no estágio 1 do modelo geral de PCE, sofre aumento s
- Uma operação de hedge de fluxo de caixa atende aos critérios do CPC 48. Onde normalmente s
- Uma empresa pretende compensar ativo financeiro e passivo financeiro no balanço porque são
```

## Pedido 42: Câmara Municipal: regimento interno e processo legislativo municipal (`legislacao-municipal.regimento-camara-municipal`, 23 tópicos de edital)

```
Matéria: legislacao-municipal.regimento-camara-municipal (Câmara Municipal: regimento interno e processo legislativo municipal)
Capítulos e quantas questões quero em cada um (total 100):
- "Autonomia municipal, hierarquia normativa e funções da Câmara": 17
- "Mesa Diretora, Presidência e comissões parlamentares": 17
- "Sessões, quóruns, proposições e debates": 17
- "Tramitação legislativa, deliberação, sanção e veto": 17
- "Fiscalização do Executivo, contas e transparência": 16
- "Vereadores: direitos, deveres, prerrogativas e mandato": 16

Já existem (não repita):
- O regimento interno de uma Câmara Municipal ocupa, no sistema normativo local, qual posiçã
- Conforme a Constituição Federal, qual procedimento caracteriza a votação da Lei Orgânica d
- Qual afirmação representa adequadamente a relação entre a Mesa Diretora e o Plenário?
- Ao analisar constitucionalidade e técnica legislativa de um projeto, uma comissão permanen
- Em regra, que espécie de sessão se destina a homenagens e cerimônias institucionais?
- Uma Câmara Municipal tem 15 integrantes. Para matéria que exige maioria absoluta dos membr
- Um vereador encaminha indicação ao Poder Executivo sugerindo manutenção de iluminação públ
- Um Plenário aprova uma moção de aplauso a uma entidade social. Qual é a interpretação corr
- O presidente da Câmara determinou que um projeto submetido ao Plenário seria considerado a
- A respeito das sessões extraordinárias, assinale a formulação mais segura em estudo geral 
- Qual contraste entre comissões permanentes e temporárias é correto?
- Em Câmara de 15 vereadores, uma deliberação exige dois terços dos membros. Compareceram ap
- A Constituição Federal prevê iniciativa popular de projetos de lei de interesse específico
- Ao receber projeto de lei aprovado pela Câmara, o prefeito identifica contrariedade ao int
- Um projeto de lei foi aprovado pelo Plenário da Câmara. Em termos de procedimento legislat
- No julgamento das contas anuais do prefeito, qual é a relação constitucional básica entre 
- Qual proteção constitucional está expressamente prevista para vereadores no art. 29, VIII?
- Uma Câmara delibera sobre projeto de resolução que altera procedimentos internos do seu re
- Um edital afirma que a Lei Orgânica só pode ser promulgada após sanção expressa do prefeit
- O Tribunal de Contas emitiu parecer prévio pela rejeição das contas de prefeito. A Câmara 
- Durante uma fiscalização, vereador exige que o secretário municipal contrate imediatamente
- Sobre o Decreto-Lei nº 201/1967, qual distinção está de acordo com a estrutura da norma?
- Quanto às hipóteses de perda do mandato de vereador, qual alternativa distingue adequadame
- Qual comparação entre limites fiscais relativos à Câmara Municipal evita erro frequente de
- Uma CPI municipal instaurada validamente pretende condenar criminalmente uma testemunha ao
- A Câmara Municipal integra o Poder Legislativo municipal e exerce, entre outras, funções l
- O regimento interno da Câmara Municipal tem força para afastar exigência expressa da Const
- A Mesa Diretora pode exercer atribuições administrativas, enquanto a apreciação de proposi
- Uma comissão temporária tem, por sua natureza, competência permanente para apreciar indefi
- A sessão ordinária costuma integrar o calendário regular, ao passo que a extraordinária ob
- A indicação parlamentar é instrumento que, por si só, transforma uma sugestão em lei execu
- A inviolabilidade constitucional do vereador relaciona-se a opiniões, palavras e votos pro
- Em Câmara de 11 membros, a maioria absoluta equivale a seis integrantes, independentemente
- O parecer desfavorável de comissão permanente equivale, sempre e em todo município, ao vet
- Segundo a Constituição, projetos de lei de iniciativa popular sobre interesse específico m
- Uma proposta legislativa aprovada no Plenário necessariamente entra em vigor no mesmo inst
- A fiscalização externa do Município é exercida pela Câmara com auxílio do órgão de contas 
- O parecer prévio do órgão de contas sobre as contas anuais do prefeito perde sua eficácia 
- A Lei Orgânica Municipal é promulgada pela Câmara após votação em dois turnos, com interst
- A Lei Complementar nº 101/2000 fixa para o Legislativo municipal limite de pessoal de 54% 
- A existência de uma CPI municipal permite que a comissão aplique diretamente penas crimina
- O Decreto-Lei nº 201/1967 distingue hipóteses de cassação do mandato de vereador, prevista
- Durante o rito político-administrativo de cassação de prefeito do art. 5º do Decreto-Lei n
- O art. 31, § 3º, da Constituição prevê que as contas dos Municípios fiquem anualmente por 
- Para projeto de resolução de organização regimental interna da Câmara, a sanção expressa d
```

## Pedido 43: Jurisdição voluntária: homologação de acordo extrajudicial (`processo-trabalho.homologacao-acordo-extrajudicial`, 23 tópicos de edital)

```
Matéria: processo-trabalho.homologacao-acordo-extrajudicial (Jurisdição voluntária: homologação de acordo extrajudicial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza do procedimento e iniciativa por petição conjunta": 20
- "2. Representação jurídica independente e preservação dos prazos rescisórios": 20
- "3. Exame em quinze dias, audiência facultativa e sentença": 20
- "4. Suspensão da prescrição e retorno do prazo após negativa": 20
- "5. Validade, quitação e leitura integrada dos arts. 855-B a 855-E": 20

Já existem (não repita):
- Após negociar um ajuste fora do processo, os interessados desejam sua homologação pela via
- Uma petição conjunta traz assinaturas dos dois interessados, mas ambos estão representados
- A trabalhadora pretende utilizar advogado de seu sindicato para pedir homologação de acord
- Após encerrar contrato de trabalho, as partes protocolam pedido conjunto e entendem que os
- O pedido conjunto foi distribuído e a equipe precisa identificar o período previsto para a
- O juízo recebe documentação suficiente e entende não haver necessidade de ouvir as partes 
- Os interessados apresentam petição de homologação que especifica diferenças de férias e gr
- Uma decisão negou a homologação, e seu trânsito em julgado já foi certificado. Segundo o a
- O empregador encerrou contrato e afirma que a distribuição da petição conjunta autoriza pa
- Após o término do contrato, o pedido conjunto foi distribuído no terceiro dia. A outra par
- A petição de homologação foi assinada por dois patronos distintos, mas surgiram documentos
- Um acordo privado foi assinado em 2 de junho, e a petição conjunta foi distribuída em 8 de
- Uma petição conjunta discrimina determinada pretensão e é apresentada enquanto já havia tr
- Uma decisão de negativa de homologação foi publicada ontem, mas ainda pode ser objeto de r
- Dois interessados comparecem pessoalmente e assinam a petição conjunta sem advogados, aleg
- A petição pede homologação de ajuste que indica expressamente duas verbas. Outra parcela n
- A respeito da homologação de acordo extrajudicial, um resumo afirma que a manifestação con
- Após a distribuição de petição conjunta, decorrem os quinze dias do art. 855-D sem sentenç
- Uma sentença recusa homologação e transita em julgado numa sexta-feira; não há feriado na 
- Um termo contém cláusula de quitação ampla e está acompanhado por petição conjunta com adv
- Uma petição apresentada com patronos diferentes especifica apenas a pretensão X. A sentenç
- A parte apresenta acordo extrajudicial com petição conjunta, mas pretende justificar pagam
- Dois interessados apresentam petição conjunta, porém cada um está assessorado por uma pess
- A parte compara três eventos: assinatura do termo, distribuição da petição e sentença nega
- Um guia afirma que o juiz deverá excluir isoladamente toda cláusula questionada e homologa
- Diante de um acordo celebrado sem processo em andamento, os interessados apresentam juntos
- Em petição conjunta, os dois interessados podem escolher o mesmo advogado quando declarare
- Ao formular o pedido de homologação, o trabalhador pode ser assistido por advogado do sind
- A distribuição do pedido conjunto amplia para quinze dias após o protocolo o prazo do art.
- Após a distribuição do requerimento conjunto, o art. 855-D prevê quinze dias para análise,
- Ainda que o juízo considere suficiente a documentação apresentada, a audiência das partes 
- A petição de homologação apresentada ao juízo suspende o prazo prescricional da ação em re
- Em acordo extrajudicial, uma das partes sustenta que a ausência de multa rescisória decorr
- Uma petição foi distribuída alguns dias depois da assinatura do ajuste, e o marco inicial 
- Se a sentença negativa ainda puder ser impugnada, a prescrição das pretensões especificada
- Uma transação regular quanto a petição conjunta e advogados distintos ainda pode ser exami
- Ao afirmar que a petição conjunta interrompe a prescrição, reiniciando integralmente o pra
- O acordo levado à homologação pode ter sido negociado inteiramente fora de processo anteri
- A homologação extrajudicial prevista nos arts. 855-B a 855-E é automática quando a petição
- A petição especificou dois direitos e foi rejeitada por decisão transitada em julgado numa
- O art. 855-E prevê que, uma vez apresentada a petição, a suspensão prescricional tenha dur
- Para avaliar possível fraude em petição conjunta, o juízo pode considerar requisitos do ar
- Os arts. 855-B a 855-E determinam expressamente que toda cláusula de quitação geral seja r
- A opção do trabalhador pela assistência de advogado sindical não elimina a vedação de advo
- Para efeitos de prescrição, o art. 855-E equipara a decisão homologatória à decisão negati
```

## Pedido 44: Crimes contra a Seguridade e a Previdência Social (`legislacao-penal-especial.crimes-previdenciarios`, 22 tópicos de edital)

```
Matéria: legislacao-penal-especial.crimes-previdenciarios (Crimes contra a Seguridade e a Previdência Social)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Proteção penal da seguridade e apropriação indébita previdenciária": 18
- "2. Sonegação previdenciária, diferenças típicas e responsabilização": 18
- "3. Fraudes documentais e manipulação de sistemas administrativos": 29
- "4. Estelionato previdenciário, benefícios e distinções de fraude": 14
- "5. Constituição do crédito, parcelamento, extinção da punibilidade e jurisprudência": 21

Já existem (não repita):
- Uma empresa descontou contribuições de trabalhadores, declarou os valores devidos e deixou
- Uma empresa suprimiu deliberadamente remunerações da folha de pagamento, reduzindo a base 
- Funcionário autorizado inseriu vínculo fictício em cadastro público, com objetivo de obter
- Uma pessoa que exerce função pública modificou, sem solicitação competente, o programa de 
- Um responsável incluiu pessoa que não era segurada obrigatória em folha destinada à compro
- Após identificar conduta típica do art. 168-A, uma pessoa afirma que a pena básica seria d
- Antes do início da ação fiscal, responsável por não repasse declarado espontaneamente conf
- Antes da fiscalização, pessoa responsável por sonegação confessou espontaneamente as contr
- Alguém obteve benefício previdenciário indevido mediante artifício e prejuízo a entidade d
- Uma autuação por contribuição não recolhida ainda está sob impugnação administrativa; pret
- Em meses próximos, uma pessoa praticou, respectivamente, não repasse de contribuição desco
- Após uma decisão definitiva sobre exigência fiscal contributiva, foi preparada representaç
- Em apropriação previdenciária, a fiscalização já começou, mas o responsável primário e de 
- Em documento de informação previdenciária, foram omitidos deliberadamente nome do segurado
- Alteração não autorizada de sistema de informática por funcionário provocou dano demonstra
- Uma pessoa fraudou requerimento previdenciário para que terceiro recebesse mensalidades in
- Responsável por tributos tem decisão administrativa definitiva declarando-o devedor contum
- Uma empresa lançou corretamente nos custos contribuições devidas à Previdência, mas deixou
- Um servidor autorizado a editar cadastros falsificou dados para favorecer terceiro; em out
- Devedor contumaz definitivamente declarado e inscrito no Cadin deixou de possuir essa cond
- O parcelamento de débito de contribuição foi solicitado antes do recebimento da denúncia, 
- Documento falso foi usado apenas para obter prestação indevida; depois do estelionato, com
- Em procedimento fiscal por supressão contributiva, um débito foi parcelado e a pretensão p
- Em discussão sobre art. 337-A, a autoridade fiscal realizou autuação inicial ainda impugna
- Uma mesma pessoa praticou atos autônomos de apropriação previdenciária e sonegação contrib
- Uma empresa reteve contribuição descontada na remuneração e não a repassou na forma devida
- A sonegação de contribuição previdenciária do art. 337-A prevê, como pena básica, detenção
- Funcionário autorizado que altera deliberadamente dados corretos em banco público com o fi
- O art. 313-B, referente à modificação não autorizada de sistema, exige finalidade especial
- A inclusão de pessoa sem qualidade de segurado obrigatório em folha destinada a comprovaçã
- Para haver estelionato previdenciário, a existência de informação cadastral incorreta bast
- A extinção prevista no art. 168-A, § 2º, pressupõe, na sua hipótese ordinária, declaração,
- O art. 337-A, § 1º, reproduz literalmente o art. 168-A, § 2º, impondo pagamento integral c
- De acordo com o Tema 1.166, o delito do art. 168-A, § 1º, I, é material e a constituição d
- O Tema 1.353 permite reconhecer crime continuado entre os arts. 168-A e 337-A quando o int
- Se modificação não autorizada de programa público causa dano a administrado, o art. 313-B,
- Quando há estelionato em prejuízo de entidade de direito público, a pena do art. 171 deve 
- Na hipótese abrangida pelo art. 83, § 2º, da Lei nº 9.430/1996, parcelamento formalizado a
- A LC nº 225/2026 permite afastar extinção de punibilidade por simples juízo informal de in
- A Lei nº 10.684/2003, art. 9º, contempla suspensão da pretensão punitiva durante parcelame
- O limite de R$ 1.510,00 indicado no art. 337-A, § 3º, é valor permanentemente fixo que o §
- Segundo a Súmula 17, falsidade documental que se esgota no estelionato e não mantém potenc
- Quando o próprio beneficiário recebe periodicamente prestação previdenciária mediante frau
- Conforme o art. 83, § 1º, da Lei nº 9.430/1996, quando há concessão de parcelamento do cré
- O art. 337-A só pode ser praticado se houver prova de intenção especial de apropriação def
```

## Pedido 45: Informações de sustentabilidade: IFRS S1, IFRS S2 e relato integrado (`contabilidade.sustentabilidade-ifrs-s1-s2-relato-integrado`, 22 tópicos de edital)

```
Matéria: contabilidade.sustentabilidade-ifrs-s1-s2-relato-integrado (Informações de sustentabilidade: IFRS S1, IFRS S2 e relato integrado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, instituições e materialidade da informação de sustentabilidade": 30
- "2. IFRS S1: requisitos gerais, conteúdo e apresentação": 21
- "3. IFRS S2: riscos climáticos, emissões, cenários e metas": 17
- "4. CBPS, regime brasileiro e atualização regulatória de 2026": 16
- "5. Relato integrado: capitais, princípios e elementos de conteúdo": 16

Já existem (não repita):
- Uma empresa pergunta qual instituição internacional elaborou os padrões IFRS S1 e IFRS S2,
- Uma entidade deseja saber qual pronunciamento brasileiro corresponde ao requisito geral de
- Uma companhia identifica escassez de água que pode elevar custos e comprometer empréstimos
- Em relatório IFRS S1, uma empresa descreve conselhos de supervisão, planos, avaliação de r
- Uma indústria gera gases ao queimar combustível em caldeira própria. Em qual escopo deve c
- Após enchentes recorrentes atingirem instalações produtivas, uma entidade classifica a ame
- Uma empresa decide divulgar informações de acordo com as normas IFRS de sustentabilidade. 
- Uma empresa utiliza eletricidade comprada da rede para operar suas instalações. Em qual es
- Uma organização quer explicar como confiança de clientes e relações comunitárias apoiam su
- Um empreendimento gera impacto severo sobre comunidade vizinha, embora ainda não haja efei
- Uma estrutura de divulgação avalia simultaneamente os efeitos econômicos da sustentabilida
- Uma preparadora de relatórios precisa localizar o pronunciamento nacional que trata especi
- Ao preparar um relatório em outubro de 2026, uma companhia consulta a redação vigente da R
- Uma entidade opta voluntariamente pelo relatório financeiro de sustentabilidade e pretende
- Uma companhia que aderiu ao regime voluntário arquivará pela primeira vez o relatório fina
- Uma empresa já se encontra no segundo exercício de arquivamento voluntário de informações 
- Uma entidade relata risco de estiagem, aumento de custo da energia, investimentos preventi
- Uma companhia exposta à elevação do nível do mar compara resultados de trajetórias climáti
- Uma fabricante terceiriza etapas relevantes e precisa informar emissões associadas ao tran
- Sem padrão temático ISSB específico para certo risco de sustentabilidade, uma empresa busc
- Uma companhia aberta, no exercício iniciado em 2027, decide não arquivar relatório finance
- Uma entidade publica apenas parte dos indicadores recomendados e declara ter aderido sem r
- Uma empresa usa recursos financeiros para capacitar empregados, desenvolver software e mel
- Um relatório apresenta narrativa muito extensa, repetitiva e fragmentada, sem conexões ent
- Uma organização pretende organizar o relato integrado seguindo os elementos de conteúdo da
- Uma entidade afirma que os pronunciamentos CBPS 01 e CBPS 02 correspondem, respectivamente
- Um relatório atribui ao IASB a edição original da IFRS S2 em 2023, argumentando que as nor
- Uma empresa descreve processos de supervisão, efeitos do risco na estratégia, mecanismos d
- Uma companhia entende que, pela IFRS S1, materialidade significa exclusivamente impacto ec
- Uma empresa exposta a secas prolongadas e a nova regulação de emissões está sujeita, respe
- Uma instalação que adquire eletricidade da rede classifica as emissões da geração dessa en
- Uma indústria com fornecedores internacionais deve examinar emissões indiretas materiais d
- Uma companhia que ainda não possui plano de transição deve criá-lo obrigatoriamente apenas
- Ao analisar riscos climáticos, uma companhia usa vários cenários para testar a resiliência
- Uma entidade que prepare relato integrado conciso fica dispensada de observar requisitos d
- Uma companhia que opta pelo relatório de sustentabilidade regulado em 2026 deve publicá-lo
- Uma entidade optante que fará seu primeiro arquivamento, segundo o art. 5º da Resolução CV
- Uma entidade optante já em seu terceiro exercício de relatório financeiro de sustentabilid
- Ao analisar a legislação vigente em outubro de 2026, uma companhia afirma que o art. 2º or
- O CBPS foi formalizado pela Resolução CFC nº 1.670/2022 para desenvolver documentos de sus
- Uma entidade que optou pelo relatório em 2026 pode interromper sua divulgação já em 2027 s
- No relato integrado, investimento financeiro em formação de empregados pode reduzir recurs
- A Estrutura Internacional de Relato Integrado estabelece oito princípios orientadores, ent
- Os oito elementos de conteúdo do relato integrado são formulados como perguntas interligad
- Uma empresa que publica relato integrado com os seis capitais e diversas metas ambientais 
```

## Pedido 46: Tributos municipais: IPTU, ITBI, cadastro e fiscalização (`direito-tributario.iptu-itbi-fiscalizacao-municipal`, 22 tópicos de edital)

```
Matéria: direito-tributario.iptu-itbi-fiscalizacao-municipal (Tributos municipais: IPTU, ITBI, cadastro e fiscalização)
Capítulos e quantas questões quero em cada um (total 100):
- "Competência municipal, IPTU e limitações constitucionais": 17
- "ITBI: transmissão, avaliação e limites da não incidência": 17
- "Taxas municipais e contribuição de melhoria": 17
- "Cadastro imobiliário, mobiliário, PVG e georreferenciamento": 17
- "Lançamento, notificação, termos e auto de infração": 16
- "Malhas fiscais, dados integrados e NFS-e": 16

Já existem (não repita):
- Uma banca solicita a base de cálculo legal do IPTU de um prédio urbano. Qual resposta desc
- O município pretende graduar as alíquotas do IPTU conforme o valor venal e também diferenc
- Após a EC nº 132/2023, um edital afirma que o Executivo municipal jamais pode atualizar a 
- Um templo religioso funciona em imóvel alugado e atende aos pressupostos da imunidade cons
- De acordo com o CTN, o IPTU pode alcançar propriedade, domínio útil ou posse juridicamente
- A progressividade no tempo do IPTU é simples técnica de arrecadação idêntica à progressivi
- Uma isenção municipal de IPTU e uma imunidade constitucional são equivalentes porque ambas
- Em uma venda onerosa de apartamento em município diferente do domicílio do comprador, a qu
- Um imóvel tem valor venal cadastral de IPTU de R$ 280 mil, enquanto a transação é declarad
- Sobre a imunidade do ITBI na integralização de capital de sociedade, qual assertiva reflet
- A Lei Complementar nº 227/2026 incluiu critérios de estimação do valor venal para ITBI. Qu
- A transmissão de imóvel por herança deve, por esse motivo apenas, ser tratada como compra 
- O Tema 1348, relativo à imunidade do ITBI em integralização de capital de empresa imobiliá
- O registro do título é central à transmissão da propriedade imobiliária, mas a cessão oner
- Qual descrição corresponde a taxa municipal pela prestação de serviço público?
- Uma obra pública valoriza determinado lote em R$ 12 mil, e o rateio municipal do custo apo
- A respeito da base de cálculo de uma taxa de poder de polícia, qual opção se ajusta à dist
- Um bairro recebeu nova obra viária que beneficiou alguns imóveis e não alterou o valor de 
- A iluminação pública, por ser serviço geral e indivisível, não se enquadra como serviço es
- A denominação legal de uma cobrança como taxa de fiscalização é suficiente para legitimá-l
- O limite global da contribuição de melhoria corresponde ao custo da obra, enquanto o limit
- Em uma prefeitura, qual registro serve prioritariamente para organizar dados físicos e fis
- Uma empresa opera em três estabelecimentos diferentes. O município pretende organizar suas
- Em avaliação fiscal didática, um terreno de 200 m² vale R$ 500/m² e uma edificação de 100 
- Uma ortofoto mostra área de telhado maior que a constante no cadastro. Como proceder de fo
- A inscrição no cadastro imobiliário municipal é, por si só, ato registral que transmite o 
- Planta Genérica de Valores pode organizar parâmetros relativos à localização e ao tipo de 
- É tecnicamente correto equiparar todos os endereços do cadastro mobiliário a inscrições im
- O CTN define lançamento tributário como atividade administrativa destinada principalmente 
- Para o IPTU lançado de ofício, o que dispõe a Súmula 397 do Superior Tribunal de Justiça q
- Em 2026, fiscal inicia ação presencial em estabelecimento. Segundo o art. 196 do CTN alter
- Um auto de infração aponta cobrança por área construída maior, mas não individualiza perío
- Um aviso interno produzido por uma malha fiscal informatizada equivale, em todos os casos 
- Termo de diligência fiscal deve documentar atividades realizadas, dados examinados e provi
- Depois da LC 236/2026, a denúncia espontânea relativa à infração fiscalizada conserva semp
- Uma malha fiscal cruza dados de imóveis e notas eletrônicas e identifica provável omissão 
- Para o presente conteúdo, qual é a principal função da Nota Fiscal de Serviços eletrônica?
- Um município programa malha que compara NFS-e, cadastros e imagens aéreas. Qual requisito 
- Um edital municipal cita instrução normativa de secretaria estadual para estabelecer autor
- A emissão de uma NFS-e comprova necessariamente a quitação do tributo relativo à operação 
- Dados oriundos de georreferenciamento, registros imobiliários e NFS-e podem ser cruzados p
- Por utilizar tecnologia automática, uma malha fiscal está dispensada de registrar a regra 
```

## Pedido 47: Fiscalização de obras e posturas (`direito.direito-urbanistico-obras`, 22 tópicos de edital)

```
Matéria: direito.direito-urbanistico-obras (Fiscalização de obras e posturas)
Capítulos e quantas questões quero em cada um (total 100):
- "Competência municipal, direito de construir e poder de polícia": 20
- "Licenciamento edilício: projeto, alvará e regularização": 20
- "Vistoria final, conclusão da obra e habite-se": 20
- "Código de obras, edificações e posturas urbanas": 20
- "Ação fiscal, notificação, embargo, interdição e demolição": 20

Já existem (não repita):
- A fiscalização municipal de obras pode se fundamentar na competência constitucional para o
- A titularidade do lote assegura ao proprietário o direito absoluto de construir, sem neces
- O poder de polícia administrativa pode abranger a exigência de licença, a realização de vi
- A função fiscalizatória permite ingresso forçado e irrestrito em residência a qualquer hor
- A existência de alvará vigente prova automaticamente que novo pavimento, não representado 
- Mesmo quando uma intervenção dispensa alvará específico, ela pode continuar sujeita às exi
- O pagamento de sanção pecuniária transforma imediatamente em regular qualquer obra clandes
- Uma licença ambiental pertinente pode coexistir com a necessidade de licenciamento edilíci
- O habite-se está ligado ao controle administrativo da construção concluída, enquanto o alv
- O habite-se constitui prova plena e suficiente da propriedade registrada da edificação, di
- A expedição do documento de conclusão torna a edificação imune a novas fiscalizações, mesm
- As exigências federais de acessibilidade podem repercutir na licença e na certificação de 
- Um código de posturas regula exclusivamente a estética das fachadas, não alcançando condut
- O fato de entulho permanecer na calçada apenas durante algumas horas elimina automaticamen
- Intervenções em vias e espaços públicos devem considerar, durante e após sua execução, cir
- Sem conhecer a legislação municipal, é possível afirmar que existe um recuo obrigatório nu
- Um auto de infração deve individualizar a conduta apurada, não sendo suficiente a expressã
- O embargo de obra e a demolição são idênticos, porque ambos pressupõem destruição da const
- Um risco estrutural grave e iminente pode justificar medida protetiva urgente autorizada p
- A possibilidade de execução direta de determinadas medidas administrativas permite cobrar 
- Um particular afirma que a Prefeitura não pode verificar ampliação realizada no próprio te
- Para fins de estudo, qual conjunto apresenta atividades possíveis da polícia administrativ
- Após atendimento de requisitos objetivos definidos em lei para licença de construção, o ag
- Ao fiscalizar obra numa residência, o agente pretende ingressar à força somente porque pos
- O fiscal identifica terceiro pavimento executado em obra cujo projeto aprovado apresenta a
- Qual documento está associado, em regra, à autorização administrativa para iniciar execuçã
- Uma construção irregular está submetida a pedido de regularização. Qual condição deve nort
- Empreendimento sujeito a licenças edilícia e ambiental apresenta somente a segunda e afirm
- Após a conclusão física de um edifício, o interessado solicita documento municipal de regu
- Em vistoria de conclusão, verifica-se divergência relevante entre a edificação executada e
- Na reforma de edificação de uso coletivo, qual aspecto deve ser considerado para licenciam
- Após emissão do habite-se, o responsável amplia a edificação e aparece risco à circulação.
- Construtor deixa sacos de material sobre a calçada, obstruindo passagem segura de pedestre
- Quanto à distinção habitual entre código de obras e código de posturas, qual enunciado é o
- Uma obra intervém em via pública movimentada. Qual comportamento atende melhor à proteção 
- Assinale a alternativa adequada acerca da afirmação “todas as cidades brasileiras adotam o
- Qual medida administrativa é voltada, em regra, a interromper a execução de obra, sem impl
- Um auto apenas registra “obra irregular”, sem local, fato concreto ou base normativa. Qual
- Prédio já concluído apresenta condições que justificam restringir a utilização por risco t
- Em processo de fiscalização, cogita-se demolir construção por desconformidade. Qual regra 
```

## Pedido 48: Sistema Único de Segurança Pública (SUSP) (`seguranca-publica.susp-lei-13675`, 22 tópicos de edital)

```
Matéria: seguranca-publica.susp-lei-13675 (Sistema Único de Segurança Pública (SUSP))
Capítulos e quantas questões quero em cada um (total 100):
- "1. PNSPDS: finalidade, princípios e diretrizes": 25
- "2. Estrutura e integrantes do SUSP": 25
- "3. Conselhos, planos e avaliação": 25
- "4. Integração e como o tema costuma ser cobrado": 25

Já existem (não repita):
- Qual princípio orientador afasta o uso arbitrário e desnecessário da força na atuação inte
- Um integrante pretende compartilhar registros com outro órgão, mas o dado está sujeito a r
- Qual é a função da PNSPDS dentro do arranjo instituído pela lei?
- Ao preparar ação conjunta, equipes identificam responsáveis, canais de comunicação e atrib
- A PNSPDS admite prevenção articulada com serviços como saúde e educação, respeitadas as fi
- Um conselho pode sugerir políticas de prevenção e acompanhar atividades sem ordenar direta
- A cooperação entre órgãos é compatível com a autonomia federativa e com a preservação das 
- As avaliações da política podem apoiar a revisão de programas e prioridades a partir dos r
- Ser integrante do SUSP elimina a necessidade de observar a competência definida para a ins
- Os conselhos podem recomendar providências legais às autoridades competentes no acompanham
- Uma operação combinada será sempre mista, ainda que seu objetivo seja exclusivamente osten
- A aceitação mútua de registros de ocorrência é um mecanismo de integração previsto para os
- Qual lei instituiu a Política Nacional de Segurança Pública e Defesa Social e o SUSP?
- O SUSP promove integração dos órgãos de segurança para atuação
- Qual grupo representa integrantes estratégicos do SUSP segundo a lei?
- Os conselhos de Segurança Pública e Defesa Social têm, entre suas funções legais, caráter
- Planos nacionais, estaduais, distritais e municipais servem principalmente para
- A integração operacional pode ocorrer por
- Qual conjunto inclui formas de integração previstas para os integrantes do SUSP?
- Os integrantes estratégicos do SUSP incluem os entes federativos por meio de seus respecti
- As operações combinadas no SUSP podem assumir quais modalidades?
- A participação de educação e saúde em políticas de prevenção significa que essas áreas pas
- Qual afirmativa descreve corretamente a autonomia dos sistemas estaduais, distrital e muni
- A função de acompanhamento social exercida pelos conselhos substitui as instâncias decisór
- Metas e indicadores de segurança pública servem para
- O compartilhamento de informações no SUSP permite acesso irrestrito a todo dado protegido 
- A PNSPDS e o SUSP foram instituídos pela Lei 13.675/2018.
- Integrar-se ao SUSP faz com que cada instituição adquira automaticamente todas as competên
- Os entes federativos por seus Poderes Executivos e os conselhos de segurança são integrant
- Os conselhos de segurança pública possuem competência jurisdicional para condenar agentes 
- A integração no SUSP pode envolver aceitação mútua de registros de ocorrência e intercâmbi
- Operações combinadas podem ser ostensivas, investigativas, de inteligência ou mistas confo
- A criação do SUSP impede estados e municípios de organizar programas e projetos próprios d
- A política nacional prevê que políticas de segurança considerem também o contexto social e
- Os conselhos podem acompanhar instituições do sistema e recomendar providências às autorid
- A existência de metas torna desnecessária a avaliação dos resultados da política de segura
- Os princípios da política incluem respeito a direitos humanos, participação social e uso p
- A autonomia dos entes federativos é incompatível com operações planejadas em conjunto no S
- Um conselho pode propor diretrizes e realizar acompanhamento social sem se tornar autorida
- A elaboração de planos no sistema deve considerar prevenção, metas e avaliação, e não apen
- A integração de dados no SUSP dispensa critérios de segurança da informação e proteção de 
```

## Pedido 49: Montadores, compiladores, ligadores e interpretadores (`informatica.compiladores-montadores-ligadores-interpretadores`, 21 tópicos de edital)

```
Matéria: informatica.compiladores-montadores-ligadores-interpretadores (Montadores, compiladores, ligadores e interpretadores)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Níveis de linguagem e tradução": 17
- "2. Montadores": 17
- "3. Compiladores: fases e estrutura": 17
- "4. Ligadores e carregadores": 17
- "5. Interpretadores, máquinas virtuais e JIT": 16
- "6. Pipeline de compilação e outros tradutores": 16

Já existem (não repita):
- O programa que traduz código escrito em linguagem de montagem (assembly) para código de má
- A linguagem de montagem (assembly) é independente da arquitetura do processador, podendo o
- O tradutor que converte todo o programa-fonte de alto nível para uma linguagem de nível ma
- O código-objeto gerado pela compilação de um módulo já está pronto para execução, dispensa
- O componente do sistema operacional que lê o arquivo executável, aloca memória, carrega có
- Em um montador de duas passagens, a primeira passagem é usada principalmente para:
- O problema da referência à frente, em montadores, ocorre quando um rótulo é utilizado ante
- As diretivas (pseudoinstruções) de um montador:
- Ao contrário da sub-rotina, a macro é expandida pelo macroprocessador em cada ponto de uso
- A fase do compilador que agrupa os caracteres do programa-fonte em tokens, como identifica
- A ausência de um ponto e vírgula exigido pela gramática da linguagem é tipicamente detecta
- Atribuir um texto a uma variável declarada como inteira, em linguagem de tipagem estática,
- A fase de otimização do compilador pode alterar o resultado produzido pelo programa, desde
- A divisão do compilador em front-end e back-end, ligados por código intermediário, permite
- Em linguagens como C, o pré-processador atua antes da compilação propriamente dita, tratan
- A estrutura de dados que acompanha todas as fases da compilação, guardando nomes, tipos e 
- A divisão por zero em operação cujo divisor só é conhecido durante a execução é exemplo tí
- São funções essenciais do ligador (linker):
- O erro de "referência indefinida" a uma função declarada mas não implementada em nenhum mó
- Em comparação com a ligação dinâmica, a ligação estática de bibliotecas tende a gerar:
- Arquivos com extensão .dll, no Windows, e .so, no Linux, correspondem a bibliotecas de lig
- A relocação, realizada pelo ligador ou pelo carregador, é necessária porque:
- Uma vantagem da ligação dinâmica é permitir a atualização de uma biblioteca compartilhada 
- Em relação aos compiladores, os interpretadores apresentam, em regra:
- O interpretador gera um arquivo executável completo do programa antes de iniciar sua execu
- Na plataforma Java, o código-fonte é compilado para:
- A compilação just-in-time (JIT) consiste em:
- O interpretador CPython, implementação de referência do Python, compila o código-fonte par
- Em programas interpretados, um erro em linha raramente executada pode passar despercebido 
- No pipeline típico do GCC, a sequência correta de etapas e arquivos é:
- O compilador que, executado em um computador pessoal x86, gera código para um microcontrol
- Desmontadores e descompiladores são ferramentas usadas em engenharia reversa, por exemplo 
- A ferramenta que traduz código TypeScript para JavaScript, linguagens de nível de abstraçã
- Bootstrapping, no contexto de compiladores, refere-se à prática de escrever um compilador 
- A análise léxica costuma ser implementada com base em expressões regulares e autômatos fin
- A compilação antecipada (ahead-of-time, AOT) distingue-se da compilação JIT porque:
- A eliminação de código que nunca é executado, a propagação de constantes e a retirada de c
- A tabela de símbolos de um montador registra os rótulos do programa e os endereços a eles 
- Em sistemas com ligação dinâmica, diversos processos podem compartilhar na memória uma úni
- O compilador cruzado é aquele que só consegue gerar código para a mesma arquitetura e sist
```

## Pedido 50: Engenharia agrícola: máquinas, construções rurais e armazenagem (`engenharia.engenharia-agricola-rural`, 21 tópicos de edital)

```
Matéria: engenharia.engenharia-agricola-rural (Engenharia agrícola: máquinas, construções rurais e armazenagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tratores, acionamentos e desempenho energético": 20
- "2. Máquinas e implementos: dimensionamento, regulagem e manutenção": 20
- "3. Construções rurais, habitações, instalações e ambiência animal": 20
- "4. Secagem, beneficiamento, silos e conservação de grãos": 20
- "5. Eletrificação rural, fontes energéticas e eficiência da propriedade": 20

Já existem (não repita):
- A potência disponível na barra de tração de um trator pode ser inferior à potência nominal
- A tomada de potência do trator é destinada exclusivamente a transmitir força horizontal a 
- Uma máquina com largura útil de 2 m e velocidade de 5 km/h possui capacidade de campo teór
- Na determinação da capacidade de campo efetiva, eficiência operacional de 70% deve multipl
- A adição ilimitada de lastro a um trator aumenta seu rendimento de tração sem qualquer pos
- Manutenção preditiva usa indicadores de condição, como vibração e análise de óleo, para em
- Em um circuito hidráulico ideal, elevar a pressão aumenta diretamente a vazão volumétrica 
- Uma barra pulverizadora deve ser calibrada levando em conta, entre outras variáveis, a vaz
- A retirada de uma proteção do eixo cardã é admissível durante a operação sempre que o oper
- O desempenho térmico de uma instalação animal depende apenas da temperatura do ar, sendo i
- Ventilação e organização do fluxo de animais e trabalhadores influenciam simultaneamente p
- Em clima quente e muito úmido, um sistema de resfriamento evaporativo tende a encontrar me
- Para uma amostra com 20% de água em base úmida, a umidade em base seca também é exatamente
- Durante uma secagem sem perdas de sólidos, a massa de matéria seca dos grãos permanece apr
- A aeração de manutenção em silos tem como função principal resfriar e uniformizar a temper
- O armazenamento em silo torna novamente íntegros os grãos quebrados e recupera sua qualida
- Na secagem com aquecimento do ar sem adição ou retirada imediata de vapor, a umidade relat
- Uma bomba que demanda 4 kW de potência elétrica e opera durante cinco horas consome 20 kWh
- Um sistema fotovoltaico convencional conectado à rede garante, por si só, alimentação das 
- Uma versão revisada da NR-10 publicada em maio de 2026 deve ser tratada como plenamente vi
- Qual interface do trator é especialmente adequada para fornecer torque rotativo a um tritu
- Um trator desenvolve força útil de 15 kN, mantendo velocidade real de 2 m/s. Qual potência
- A velocidade periférica teórica da roda é 8 km/h, mas o trator avança a 6,8 km/h. Qual pat
- Um conjunto possui largura útil de 4 m, velocidade de 7,5 km/h e rendimento de campo de 80
- Em solo mais solto, o operador percebe patinagem alta. Qual medida traduz melhor o raciocí
- Um pulverizador utiliza 150 L/ha, espaçamento de 0,50 m entre pontas e velocidade de 6 km/
- Em um equipamento com monitoramento de vibração dos rolamentos, a troca é programada quand
- Para evitar acionamento inesperado durante desobstrução de máquina agrícola, qual procedim
- Uma máquina de 5 m de largura opera a 4 km/h e efetivamente cobre 1,3 ha/h. A eficiência a
- Um cilindro hidráulico tem área de pistão de 0,01 m² e recebe diferença de pressão de 2 MP
- Ao implantar um galpão rural de criação, qual conjunto é mais coerente com conforto, segur
- Em alojamento de animais submetido a clima quente e úmido, a principal limitação de um res
- No projeto de uma habitação rural, qual requisito responde diretamente à salubridade dos o
- Na comparação de dois aviários iguais em temperatura do ar, um com cobertura aquecida e ou
- Uma instalação está construída em encosta e observa-se acúmulo de água junto às fundações.
- Uma amostra contém 18 kg de água e 72 kg de matéria seca. Qual seu teor de umidade em base
- Um lote de 1.000 kg entra no secador com 20% de umidade em base úmida e sai com 12%, sem p
- Um silo apresenta zonas localizadas de aquecimento, embora a temperatura média permaneça m
- Ao aquecer ar para secagem sem adicionar água ao ar, o efeito inicial esperado é:
- Qual proposição diferencia adequadamente aeração de conservação e secagem?
- Uma carga elétrica consome 3 kW por 6 horas a potência constante. O consumo de energia é:
- Um motor de rendimento 80% entrega 8 kW mecânicos. Desprezando outras perdas, sua potência
- Sobre fontes de energia no meio rural, escolha a afirmação correta:
- Em uma instalação com motores trifásicos e condutores longos, qual medida se mostra pertin
- Uma instalação rural com fotovoltaico conectado à rede precisa manter ventiladores essenci
```

