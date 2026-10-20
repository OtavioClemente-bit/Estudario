# Pedidos de questões extras, lote 20 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 2015 tópicos de edital.

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

## Pedido 1: Trauma e queimaduras: atendimento inicial ao politraumatizado (`medicina.cirurgia-trauma-queimaduras`, 47 tópicos de edital)

```
Matéria: medicina.cirurgia-trauma-queimaduras (Trauma e queimaduras: atendimento inicial ao politraumatizado)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação inicial, xABCDE e via aérea": 20
- "Hemorragia, choque e trauma toracoabdominopélvico": 20
- "Trauma neurológico, raquimedular e de extremidades": 20
- "Trauma na criança, gestante e pessoa idosa": 20
- "Queimaduras térmicas e lesões por eletricidade": 20

Já existem (não repita):
- No atendimento inicial do politraumatizado com hemorragia externa exsanguinante em membro,
- A avaliação secundária do traumatizado inclui exame da cabeça aos pés e história AMPLE, ap
- Uma vítima de colisão está rouca, apresenta sangue em orofaringe e piora progressiva da co
- No trauma com risco de lesão cervical, a hiperextensão forçada do pescoço deve ser utiliza
- No exame primário, paciente hipotenso apresenta dispneia intensa e ausência unilateral do 
- O fato de a vítima responder verbalmente indica que a via aérea está pérvia naquele moment
- Após correção de uma obstrução da via aérea, a pressão arterial cai e aparece sangramento 
- O resultado de radiografia torácica deve ser obtido antes da descompressão de um pneumotór
- Em um politraumatizado com choque hemorrágico e suspeita de fratura instável de pelve, ond
- A ausência de hipotensão não afasta choque hemorrágico inicial, sobretudo em adultos joven
- Paciente de trauma fechado está instável e tem FAST positivo para líquido livre abdominal.
- O exame FAST negativo exclui lesão intestinal e hemorragia retroperitoneal após trauma abd
- Uma pessoa com grande hemorragia traumática apresenta TCE grave concomitante. A equipe dis
- Em hemorragia traumática grave, hemocomponentes precoces e controle definitivo do sangrame
- Homem com fratura pélvica após atropelamento apresenta sangue no meato uretral e choque. Q
- Na hemorragia traumática, o ácido tranexâmico deve ser administrado rotineiramente mesmo q
- Na classificação tradicional de gravidade do TCE pela Escala de Coma de Glasgow, qual faix
- A prevenção de hipóxia e hipotensão é prioridade para reduzir lesão cerebral secundária no
- Em paciente com suspeita de síndrome compartimental após fratura de tíbia, qual achado sug
- A presença de pulsos periféricos palpáveis exclui síndrome compartimental aguda de uma ext
- Um idoso com trauma de cabeça apresenta coleção extra-axial em crescente na TC, após uso d
- Choque medular é a mesma entidade que choque neurogênico, ambos definidos necessariamente 
- Traumatizado com lesão cervical alta está hipotenso e bradicárdico. Qual interpretação é a
- O uso rotineiro de altas doses de corticoide é medida universalmente recomendada para toda
- Criança de cinco anos após atropelamento apresenta taquicardia, palidez, sonolência e ench
- Em crianças traumatizadas, a elasticidade da parede torácica pode permitir contusão pulmon
- Gestante com 28 semanas sofre colisão e está hipotensa em decúbito dorsal. Qual intervençã
- A ressuscitação e a estabilização da mãe constituem, em geral, a primeira medida de proteç
- Uma gestante com gravidez viável sofreu trauma abdominal, permanece estável e não apresent
- Em idosos em uso de betabloqueadores, a ausência de taquicardia expressiva exclui perda sa
- Pessoa idosa, em uso de anticoagulante, apresenta queda ao nível do solo, Glasgow 15 e exa
- No trauma da gestante, deve-se negar tomografia materna potencialmente salvadora sempre qu
- Adulto com queimadura de espessura parcial envolvendo todo o braço esquerdo e toda a face 
- Eritema doloroso sem bolhas, limitado à epiderme, não deve ser somado à SCQ usada para est
- Uma paciente de 70 kg tem queimadura térmica de espessura parcial de 30% SCQ. Pela fórmula
- Na criança, a regra dos nove do adulto é tão precisa quanto a tabela de Lund–Browder para 
- Sobre a diretriz de ressuscitação de queimados da American Burn Association publicada em 2
- Nas primeiras oito horas previstas pela fórmula de Parkland, o relógio deve ser contado do
- Após exposição a fogo e fumaça em ambiente fechado, paciente tem rouquidão, cefaleia e SpO
- Em lesão elétrica de alta tensão, queimaduras cutâneas pequenas excluem necrose muscular p
```

## Pedido 2: Enfermagem em saúde coletiva e na atenção básica (`enfermagem.saude-coletiva-atencao-basica`, 47 tópicos de edital)

```
Matéria: enfermagem.saude-coletiva-atencao-basica (Enfermagem em saúde coletiva e na atenção básica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organização da APS, território e acolhimento": 17
- "2. Consulta de enfermagem e gestão do cuidado longitudinal": 17
- "3. Hipertensão, diabetes e outras DANT: cuidado programático": 17
- "4. Tuberculose e hanseníase na rotina da enfermagem": 17
- "5. Saúde da mulher, da criança e da pessoa idosa: ações na APS": 16
- "6. Visitas domiciliares, educação e vigilância em saúde": 16

Já existem (não repita):
- Na Estratégia Saúde da Família, o cadastro e a adscrição favorecem a responsabilidade sani
- Acolhimento é uma atividade administrativa localizada exclusivamente na recepção da Unidad
- No diagnóstico situacional da microárea, condições de moradia, trabalho, mobilidade e apoi
- Por ser a porta preferencial do SUS, a APS é o único local legítimo para iniciar atendimen
- Conforme a Resolução Cofen nº 736/2024, a consulta de enfermagem é organizada de acordo co
- Na Atenção Primária, o enfermeiro está impedido de solicitar qualquer exame e de prescreve
- Para o Processo de Enfermagem, diagnóstico de enfermagem e diagnóstico médico são expressõ
- No cuidado longitudinal da hipertensão, uma leitura de pressão isolada e sem consideração 
- O exame periódico dos pés da pessoa com diabetes pode identificar perda de sensibilidade, 
- As DANT abrangem exclusivamente doenças crônicas como diabetes e câncer, não incluindo aci
- O tratamento diretamente observado da tuberculose pode apoiar a adesão ao permitir observa
- A ausência de três semanas completas de tosse exclui tuberculose pulmonar e impede a equip
- Durante o acompanhamento da hanseníase, alterações de sensibilidade ou força e sinais de c
- O rastreamento de câncer é dirigido a pessoas elegíveis sem sintomas, ao passo que sinais 
- A diretriz brasileira de 2025 prevê DNA-HPV oncogênico como teste primário do rastreamento
- Na atenção à saúde da pessoa idosa, a idade cronológica por si só comprova dependência fun
- Na visita domiciliar, objetivos, consentimento, avaliação do ambiente, registro das condut
- Educação em saúde efetiva consiste em transmitir ordens para que usuários as cumpram, sem 
- Segundo a lógica da vigilância em saúde, a notificação compulsória pode ser exigida já na 
- Após registrar no Sinan a ocorrência de agravo notificável, a enfermagem encerra obrigator
- Uma UBS percebe concentração de usuários com diabetes que interrompem consultas após a mud
- No acolhimento da UBS, uma pessoa relata dor torácica intensa iniciada há poucos minutos. 
- Um candidato afirma que a microárea é somente a lista de domicílios de um agente comunitár
- Uma equipe recebe uma pessoa nova que reside temporariamente na região e não consta do cad
- Uma enfermeira registra avaliação inicial, identifica necessidades, formula intervenções, 
- Em um programa da APS com protocolo assistencial aprovado, qual proposição descreve corret
- Uma usuária com HAS recebeu alta hospitalar com nova medicação, mas a UBS desconhece o aju
- Em consulta de enfermagem, a identificação de dificuldades para organizar medicamentos e d
- Uma pessoa hipertensa apresenta nova fraqueza de um lado do corpo e alteração da fala dura
- Na consulta de diabetes, a HbA1c está no alvo individual, mas há ferida no pé e alteração 
- Uma usuária com diabetes relata insegurança no uso do glicosímetro após receber uma explic
- Ao planejar uma ação sobre DANT, a equipe identifica hipertensão, acidentes de trânsito e 
- Pessoa com tosse persistente, suor noturno e perda de peso chega à UBS. Qual combinação de
- A pessoa com tuberculose falta ao TDO após mudar de turno no emprego. Qual resposta da enf
- Uma pessoa apresenta área hipocrômica na pele e queixa de diminuição da sensibilidade loca
- Durante tratamento de hanseníase, uma pessoa refere piora rápida da força da mão e dor em 
- Uma mulher assintomática procura informações sobre rastreamento organizado do câncer do co
- Uma mulher de 58 anos, assintomática e sem situação especial informada, pergunta pela peri
- Uma mulher de 46 anos percebe retração recente do mamilo e procura a UBS. Qual estratégia 
- Na ESF, duas pessoas de 78 anos são acompanhadas: uma pratica atividades independentes; ou
- Antes de visitar uma pessoa idosa com quedas recorrentes, qual preparação mais qualifica o
- Após palestra sobre diabetes, a equipe quer avaliar se a atividade educativa foi útil. Qua
- Em uma microárea, aparecem várias pessoas com sintomas de agravo sob suspeita de surto. Qu
- A equipe observa boa média municipal de acompanhamento de hipertensão, mas uma microárea r
```

## Pedido 3: Cirurgia e traumatologia bucomaxilofacial (`odontologia.cirurgia-bucomaxilofacial`, 47 tópicos de edital)

```
Matéria: odontologia.cirurgia-bucomaxilofacial (Cirurgia e traumatologia bucomaxilofacial)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, planejamento, anestesia e segurança cirúrgica": 17
- "Exodontias, dentes inclusos e traumatismos dentoalveolares": 17
- "Infecções odontogênicas, cistos e tumores dos maxilares": 17
- "Traumatologia da face: avaliação, fraturas e reparação": 17
- "Cirurgia ortognática e articulação temporomandibular": 16
- "Implantodontia, complicações e urgências bucomaxilofaciais": 16

Já existem (não repita):
- Na cirurgia bucomaxilofacial, a avaliação do risco sistêmico e da via aérea integra o plan
- Toda exodontia cirúrgica deve ser precedida de tomografia de feixe cônico, independentemen
- Uma injeção intravascular acidental de anestésico local pode gerar manifestações sistêmica
- Síncope vasovagal e anafilaxia são intercorrências equivalentes, de modo que o tratamento 
- A classificação de Winter descreve principalmente a angulação de um terceiro molar inferio
- A presença de um terceiro molar incluso sem sintomatologia determina obrigatoriamente sua 
- Dentes decíduos avulsionados não devem ser reimplantados por causa do risco de dano ao des
- A alveolite pós-exodontia equivale necessariamente a infecção purulenta sistêmica, justifi
- Na infecção odontogênica com abscesso drenável, tratar o foco causal e efetuar drenagem qu
- A presença de elevação da língua, edema submandibular bilateral e dificuldade respiratória
- O cisto dentígero se relaciona tipicamente à coroa de um dente incluso, enquanto o cisto r
- Ameloblastoma e cisto radicular possuem sempre o mesmo comportamento biológico e podem rec
- Alteração da oclusão habitual após trauma facial é sinal clínico importante e pode sugerir
- A fratura Le Fort III é definida pela separação horizontal baixa da maxila, imediatamente 
- O tratamento fechado de fratura condilar mandibular pode ser apropriado em casos seleciona
- Suspeita de fratura de base de crânio é uma condição que exige cautela especial e pode con
- A osteotomia Le Fort I é técnica utilizada para mobilização cirúrgica da maxila em determi
- Estalido isolado da articulação temporomandibular, sem dor ou prejuízo funcional, constitu
- A instalação imediata de implante no alvéolo após exodontia obriga a colocação imediata de
- No planejamento da implantodontia, a disponibilidade óssea e a posição protética desejada 
- Durante a anamnese para extração complexa, qual informação influencia diretamente risco he
- Durante aplicação de anestésico local, o paciente desenvolve sinais neurológicos súbitos e
- Em avaliação pré-operatória de paciente com alteração neurossensorial no mento após cirurg
- Quanto às práticas de segurança, assinale a medida mais adequada antes de cirurgia com sít
- Qual sistema é utilizado para descrever a angulação do terceiro molar inferior?
- Radiografia mostra terceiro molar inferior próximo ao canal mandibular, com sinais sugesti
- Após extração de molar superior, suspeita-se de comunicação bucosinusal. Qual atitude é ma
- Uma criança perde completamente um incisivo decíduo após queda. Qual orientação específica
- Adulto apresenta dor intensa alguns dias após exodontia, alvéolo com coágulo desorganizado
- Paciente com abscesso odontogênico flutuante, sem sinais de obstrução respiratória, procur
- Edema endurecido bilateral do assoalho bucal, elevação de língua, voz abafada e disfagia s
- Em radiografia, observa-se área radiolúcida envolvendo a coroa de um canino incluso, aderi
- Uma lesão expansiva multilocular da mandíbula provoca deslocamento dentário e reabsorção r
- Adulto imunocompetente com dor pulpar aguda, sem febre, edema difuso ou comprometimento si
- Após trauma mandibular, qual achado é particularmente útil para levantar suspeita de fratu
- Fratura facial com padrão de disjunção craniofacial do terço médio é denominada:
- Em fratura mandibular com perda de continuidade e cominuição, há pouco suporte ósseo capaz
- Paciente com trauma zigomático apresenta diplopia e restrição dos movimentos oculares. Qua
- Sobre fraturas condilares mandibulares, assinale a estratégia clinicamente defensável:
- Qual procedimento é classicamente associado ao reposicionamento cirúrgico da maxila?
- Na osteotomia sagital bilateral dos ramos mandibulares, qual complicação neurossensorial m
- Paciente apresenta clique de ATM, mas não tem dor, bloqueio ou prejuízo funcional. Qual ab
- Qual associação entre procedimento e finalidade em disfunções da ATM está correta?
- No planejamento seguro de implante em região mandibular posterior, qual referência anatômi
- Qual afirmação distingue corretamente instalação imediata de implante e carga imediata?
- Um implante apresenta sangramento à sondagem, inflamação e perda óssea progressiva após a 
- Na recuperação pós-cirúrgica, paciente apresenta urticária, edema de língua, sibilância e 
```

## Pedido 4: Teoria das Relações Internacionais e cooperação internacional (`relacoes-internacionais.teoria-cooperacao-internacional`, 46 tópicos de edital)

```
Matéria: relacoes-internacionais.teoria-cooperacao-internacional (Teoria das Relações Internacionais e cooperação internacional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e correntes clássicas": 15
- "2. Abordagens estruturais, críticas e sociedade internacional": 18
- "3. Construtivismo e análise da política externa": 22
- "4. Fundamentos, história e modalidades da cooperação": 24
- "5. Instrumentos, projetos e governança da cooperação": 21

Já existem (não repita):
- Um país considera ampliar sua capacidade defensiva porque não existe autoridade internacio
- Durante um debate, um analista defende que interesses nacionais devem ser examinados com p
- Dois países ampliam relações comerciais, enquanto universidades, empresas e associações pa
- Países interessados em compartilhar dados ambientais instituem relatórios, reuniões periód
- Um governo considera determinado país um parceiro confiável por uma história compartilhada
- Dois Estados ganhariam com um acordo de pesquisa, mas um receia que o parceiro obtenha con
- Após ampliar sistemas defensivos, um país vê seu vizinho reforçar armamentos por recear um
- Um relatório sustenta que instituições representativas e mecanismos de controle democrátic
- Em um fórum, Estados mantêm sua soberania, mas reconhecem regras comuns de diplomacia, dir
- Um projeto internacional mantém países fornecedores de matérias-primas dependentes de bens
- Uma parceria entrega tecnologias a um país, mas conserva no fornecedor a propriedade das p
- Um Estado invoca sua jurisdição doméstica diante de proposta internacional. Qual afirmação
- Um texto constitucional é citado para justificar iniciativa conjunta entre povos voltada a
- Uma proposta de convênio científico recebe apoio social, mas pesquisas indicam oposição cr
- Um país responde repetidamente a demandas externas com soluções padronizadas porque suas r
- Negociadores aceitam reduzir o alcance de um acordo ambiental porque precisam obter apoio 
- Dois governos descrevem a cooperação regional como dever de solidariedade e passam a redef
- Um fórum climático cria padrões de relato e inspeção, mas seus membros continuam disputand
- Um país em desenvolvimento solicita oficialmente a outro intercâmbio de técnicas agrícolas
- Um país oferece técnicos, uma instituição internacional mobiliza recursos e outro país rec
- Dois Estados assinam documento chamado Memorando de Entendimento para criar programa de pe
- Em relatório estatístico, um analista classifica toda missão técnica internacional como Aj
- Um programa registra 500 participantes em oficinas e declara ter reduzido a desigualdade e
- Universidades de regiões distintas criam pesquisa conjunta com intercâmbio de pesquisadore
- Países acordam difundir tecnologias ambientalmente adequadas, ampliar pesquisa conjunta e 
- Ao avaliar acordo de pesquisa, uma equipe afirma que a anarquia internacional pode coexist
- Após um conflito, um diplomata sustenta que o idealismo histórico afastava o uso de instit
- Em projeto demandado oficialmente por país em desenvolvimento, técnicos brasileiros compar
- Uma rede de pesquisa conduzida com organismo internacional é necessariamente cooperação bi
- No Brasil, a independência nacional, a não intervenção, a igualdade entre os Estados e a c
- Para ampliar sua cooperação técnica Sul-Sul, o Brasil pode considerar automaticamente inic
- Ao explicar a corrida por capacidade militar entre países, uma análise neorrealista pode a
- Um acordo cria regras de troca de informações e monitoramento. Segundo o institucionalismo
- Em negociação ambiental, empresas, associações científicas e organizações sociais particip
- Uma análise da Escola Inglesa afirma que sociedade internacional e sociedade mundial são e
- Ao longo de sucessivas missões educacionais, Estados reformulam as expectativas de conduta
- Quando pesquisas indicam grande apoio a um projeto de cooperação, a opinião pública passa 
- Ao examinar acordo escrito entre Estados regido pelo direito internacional, é necessário c
- Após o Plano de Ação de Buenos Aires de 1978, todos os projetos de cooperação Sul-Sul pass
- Durante negociações multilaterais, a delegação modifica sua proposta após constatar que a 
- Um Estado que celebrou tratado válido pode deixar de cumpri-lo alegando que uma norma inte
- Um organismo internacional e instituições brasileiras desenvolvem um projeto de cooperação
- O êxito de uma missão científica deve ser avaliado exclusivamente pela quantidade de pesqu
- Um projeto conjunto de difusão de tecnologias ambientalmente adequadas e fortalecimento de
- No sistema da Carta das Nações Unidas, a igualdade soberana dos Estados torna inaplicáveis
```

## Pedido 5: Transportes terrestres e regulação pela ANTT (`legislacao.transportes-terrestres-antt`, 46 tópicos de edital)

```
Matéria: legislacao.transportes-terrestres-antt (Transportes terrestres e regulação pela ANTT)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regulação dos transportes terrestres e a Lei nº 10.233/2001": 20
- "2. Sistema Nacional de Viação e infraestrutura integrada": 19
- "3. Transporte rodoviário remunerado de cargas e RNTRC": 19
- "4. Lei das Ferrovias e Programa de Parcerias de Investimentos": 28
- "5. Processo sancionador e Regimento Interno da ANTT": 14

Já existem (não repita):
- Uma empresa pretende operar viagens regulares de ônibus entre capitais de dois estados. Co
- Ao elaborar parecer sobre a natureza institucional da ANTT, um analista precisa distinguir
- Em uma fiscalização de rodovia federal concedida, constatou-se descumprimento de obrigação
- Uma transportadora utiliza rodovia e ferrovia para deslocar uma mesma carga e invoca a exp
- Uma proposta de política pública busca reduzir custo logístico e acidentes em corredor fed
- Um planejamento nacional considera estrada estadual, ferrovia federal e infraestrutura aer
- Uma equipe precisa listar os subsistemas que integram o Sistema Federal de Viação. Qual re
- Uma estrada municipal conecta-se a rodovia federal e passa a integrar corredor logístico. 
- Uma comissão examina se a União pode explorar infraestrutura integrante do SFV mediante di
- Um estudo sustenta que a alteração da Lei nº 12.379/2011 pela Lei das Ferrovias foi releva
- Uma sociedade empresária possui caminhões e oferece fretes remunerados a vários embarcador
- Uma pessoa física pretende atuar profissionalmente em transporte remunerado de cargas de t
- Transportadores organizaram sociedade cooperativa para oferecer serviços rodoviários remun
- Em outubro de 2026, uma empresa cadastrada no RNTRC sustenta que a inscrição inicial dispe
- Entidade parceira oferece auxílio a transportadores para inscrição digital no RNTRC e pret
- Uma operadora propõe construir ferrovia em regime privado, por autorização. Qual configura
- No projeto de ferrovia privada, o requerente supõe que a autorização só permite transporte
- Um projeto de corredor logístico foi qualificado para estruturação no PPI. Qual consequênc
- Uma ferrovia apresenta bitolas incompatíveis em trechos contíguos, elevando o custo de tra
- Após alteração legislativa de 2026, um estudo considera a abrangência do PPI. Qual afirmaç
- Em processo sancionador da ANTT sujeito ao art. 41 da Resolução nº 5.083/2016, já na redaç
- A autoridade decidiu processo sancionador geral e o interessado pretende impugná-lo por mo
- A Diretoria Colegiada proferiu a decisão inicial em processo abrangido pelo art. 57, § 3º,
- Um auto tramita no procedimento administrativo simplificado previsto na Resolução nº 5.083
- Um regulador analisa alternativas antes de editar norma de transporte e, anos depois, aval
- Uma operadora de transporte rodoviário interestadual de passageiros deve considerar as com
- Uma rodovia que integra um corredor nacional passa obrigatoriamente a ser de jurisdição fe
- Uma cooperativa regularmente constituída para transporte rodoviário remunerado de cargas p
- Uma empresa autorizada a explorar ferrovia em regime privado pode operar por prazo indeter
- O processo sancionador da ANTT referente a descumprimento de obrigações de transporte pode
- Uma norma interna da ANTT pode atribuir ao agente fiscalizador competência legislativa par
- Uma rede ferroviária sob jurisdição federal e outra rodoviária sob jurisdição municipal po
- Um transportador que apenas desloca produtos próprios de seu depósito para sua loja, sem r
- Uma ferrovia estruturada no Programa de Parcerias de Investimentos precisa ter seu regime 
- No processo sancionador geral da ANTT, todo recurso de decisão administrativa é recebido a
- O art. 22 da Lei nº 10.233/2001 inclui o transporte rodoviário de cargas e a exploração da
- Pelo art. 39 da Lei nº 12.379/2011, cabe somente à União definir os elementos físicos dos 
- No modelo de inscrição do RNTRC atualizado em 2026, o apoio de entidade conveniada a taref
- Pelo art. 20 da Lei nº 14.273/2021, o contrato de autorização ferroviária contempla necess
- Em procedimento simplificado abrangido pelo art. 83, § 4º, da Resolução nº 5.083/2016, os 
- A autonomia financeira e funcional da ANTT, prevista na Lei nº 10.233/2001, dispensa a agê
- A Lei nº 14.273/2021 alterou o conceito de SNV para reconhecer infraestrutura física e ope
- Desde a inclusão da CTC na Lei nº 11.442/2007, a categoria ETC deixou de exigir sede brasi
- A autorização de exploração ferroviária privada pode ser prorrogada por períodos sucessivo
- O Regimento Interno da ANTT aprovado pela Resolução nº 5.976/2022 permaneceu integralmente
```

## Pedido 6: Mercosul e integração econômica regional (`direito-internacional.mercosul-integracao-economica`, 46 tópicos de edital)

```
Matéria: direito-internacional.mercosul-integracao-economica (Mercosul e integração econômica regional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Etapas da integração econômica e efeitos tarifários": 14
- "2. Tratado de Assunção e construção do Mercosul": 14
- "3. Protocolo de Ouro Preto: órgãos, normas e vigência": 29
- "4. Las Leñas, Protocolo de Olivos e Tribunal Permanente de Revisão": 29
- "5. ALADI, ALCA, OMC e tributação no regionalismo comercial": 14

Já existem (não repita):
- Dois países eliminaram os direitos alfandegários sobre produtos originários do parceiro, m
- Três economias concordam em liberalizar substancialmente seu comércio recíproco e aplicar 
- Uma importadora pretende obter preferência tarifária para bem comprado em país parceiro, e
- Um pesquisador procura o documento de 26 de março de 1991 que iniciou o projeto do Mercado
- Um resumo afirma que o Tratado de Assunção fixou data para constituir o mercado comum. Qua
- Uma nota administrativa precisa identificar quem conduz politicamente a integração e emite
- Para executar uma política de integração, um órgão competente do Mercosul publica Resoluçã
- No acompanhamento da Tarifa Externa Comum, o órgão especializado do Mercosul edita Diretri
- Durante reunião decisória do Mercosul, alguns representantes pretendem aprovar norma por m
- O Mercosul pretende celebrar acordo com terceiro Estado por órgão competente. Qual previsã
- Um pesquisador precisa separar atos originários de normas derivadas do Mercosul. Qual docu
- Uma norma do Mercosul sujeita ao procedimento de vigência simultânea foi incorporada e com
- Uma empresa sustenta que, como o GMC aprovou Resolução obrigatória, ela ingressou automati
- Uma autoridade judicial brasileira necessita encaminhar a outro Estado Parte pedido de pro
- Uma pessoa residente permanentemente em Estado Parte propõe ação civil em outro Estado Par
- Um processo busca reconhecimento e execução de sentença civil originária de outro Estado P
- Uma sentença estrangeira oriunda de Estado Parte é apresentada ao Estado requerido sem pro
- Dois Estados Partes iniciam controvérsia comercial sob Olivos e não pactuaram prazo difere
- Fracassadas as negociações diretas de Olivos, um Estado deseja arbitrar a controvérsia enq
- O Estado A foi notificado de laudo arbitral ad hoc do Mercosul e pretende interpor recurso
- Um Estado pede ao TPR, em recurso de revisão de laudo ad hoc, a reavaliação integral de de
- Após negociações diretas encerradas, dois Estados Partes acordam expressamente que o litíg
- Uma controvérsia entre Estados Partes poderia ser examinada sob Olivos ou no mecanismo da 
- Uma negociação da ALADI envolve apenas três de seus países membros, enquanto outra é celeb
- Um estudante sustenta que o GATT proíbe qualquer zona de livre comércio mesmo quando satis
- Em uma zona de livre comércio, dois Estados reduzem barreiras comerciais entre bens origin
- Um acordo comercial que adota Tarifa Externa Comum converte seus Estados participantes, po
- Ao elaborar parecer sobre o Mercosul, uma estudante afirma que o Tratado de Assunção foi a
- Um material didático conclui que, como o Tratado de Assunção fixou 31 de dezembro de 1994,
- Uma tabela normativa vincula Decisões ao CMC, Resoluções ao GMC e Diretrizes à CCM, confor
- Uma reunião decisória do Mercosul aplica a regra geral de maioria absoluta dos presentes, 
- Em uma demanda trabalhista transfronteiriça, as autoridades consideram o Protocolo de Las 
- Uma sentença estrangeira apresentada por meio da Autoridade Central pode produzir eficácia
- Uma norma derivada está sujeita ao procedimento de vigência simultânea de Ouro Preto. Todo
- Uma empresa calcula obrigação aduaneira de importação usando tabela intitulada 'Tarifa Ext
- Em acordo de complementação econômica celebrado apenas por alguns países da ALADI, o carát
- Um estudo de tributação aduaneira afirma que o art. I do GATT exige comparar a tributação 
- Depois que bem importado ingressa no mercado, a imposição de tributo interno superior por 
- Após frustradas as negociações diretas de Olivos, os Estados ficam obrigados a submeter a 
- Diante de laudo ad hoc proferido com base em julgamento ex aequo et bono, um Estado conclu
- O reconhecimento de laudo arbitral estrangeiro pela via de Las Leñas independe de exame de
- Em conflito entre dois Estados Partes, encerradas as negociações diretas, eles podem acord
- Uma Diretriz da CCM sujeita a incorporação interna produz vigência simultânea em todo o Me
- Uma união aduaneira entre integrantes da OMC deve considerar, na relação comercial com não
- Um parecer afirma que o fundamento multilateral específico para acordos regionais relativo
```

## Pedido 7: CTB: infrações, penalidades e processo administrativo (`legislacao-transito.ctb-infracoes-penalidades`, 46 tópicos de edital)

```
Matéria: legislacao-transito.ctb-infracoes-penalidades (CTB: infrações, penalidades e processo administrativo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Infrações de trânsito: tipificação, gravidade, multa e pontos": 21
- "2. Sujeitos responsáveis, identificação do condutor e rodovias concedidas": 18
- "3. Penalidades e medidas administrativas: suspensão, cassação e providências sobre veículos": 21
- "4. Autuação, notificações, defesa prévia e processamento da penalidade": 20
- "5. Recursos, prazos decisórios, controle da legalidade e casos integrados": 20

Já existem (não repita):
- Uma fiscalização enquadrou determinada infração como leve. Para a incidência da multa bási
- O fiscal constatou conduta tipificada como gravíssima, mas o dispositivo prevê multiplicad
- Um automóvel está em condições documentais regulares, mas o motorista identificado comete 
- Uma autoridade de trânsito pretende aplicar multa e também retirar o veículo da via, em si
- Uma pessoa recebeu autuação por infração média sujeita a multa e não praticou outra infraç
- A câmera registra uma infração que não permitiu identificação imediata de quem dirigia. Qu
- O motorista soma pontos em doze meses e possui duas infrações gravíssimas na contagem. Qua
- Uma notificação de autuação foi expedida regularmente. Qual prazo mínimo ela deve conceder
- O infrator pretende impugnar multa aplicada após indeferimento de defesa prévia. Qual afir
- Uma empresa proprietária de caminhonete é notificada de infração atribuível a motorista nã
- Uma carga teve apenas um remetente; o peso declarado na nota fiscal ficou abaixo do efetiv
- Em rodovia federal concedida, um usuário afirma que toda multa por excesso de velocidade é
- Uma irregularidade de equipamento determina retenção do veículo. Ela não pode ser sanada a
- Em procedimento ordinário de multa, foi apresentada defesa prévia tempestiva. Qual prazo d
- O proprietário utiliza o sistema de notificação eletrônica, adere antes do envio da notifi
- Após recurso tempestivo contra penalidade, o processo é recebido pelo órgão competente par
- O responsável pela infração recebe decisão de não provimento proferida pela JARI. Deseja p
- Dois motoristas acumulam, em doze meses, 33 pontos com duas gravíssimas cada um. O primeir
- Um motorista é autuado por infração cuja própria descrição legal prevê suspensão específic
- O órgão de trânsito expediu notificação da autuação 31 dias após a infração, sem hipótese 
- Na análise de duas ocorrências, um veículo é retido por defeito sanável e outro está sujei
- O manifesto da carga aponta peso bruto total declarado superior ao limite legal permitido 
- Uma infração foi aplicada por órgão federal. Após julgamento desfavorável na JARI, o respo
- Uma motorista recebeu notificação de multa, pretende apresentar recurso e deseja pagar no 
- Uma autoridade expediu a notificação da penalidade fora do prazo do art. 282, § 6º; em out
- Em fiscalização de trânsito, uma infração gravíssima acarreta, na regra geral do art. 259,
- A apreensão do veículo continua sendo penalidade do art. 256 do CTB e sua aplicação se con
- A advertência por escrito do art. 267 é aplicável à infração leve ou média passível de mul
- O proprietário do veículo responde por todos os atos praticados pelo motorista durante a c
- A remoção do veículo, quando prevista no CTB, constitui medida administrativa que pode coe
- A regra do art. 281, § 1º, II, exige que o destinatário receba fisicamente a notificação d
- Quando o infrator não foi identificado no momento da autuação, o CTB admite que o principa
- Se pessoa jurídica deixa de indicar o condutor no prazo do art. 257, § 7º, a multa originá
- Para quem exerce atividade remunerada ao volante, o art. 261, § 5º, fixa o limite de quare
- Para o motorista que não exerce atividade remunerada, duas infrações gravíssimas no períod
- A notificação de autuação deve informar prazo de defesa prévia não inferior a trinta dias,
- Quando não existe defesa prévia, o art. 282, § 6º, exige, na regra geral de multa, a exped
- A falta de expedição tempestiva da notificação de penalidade, nos prazos do art. 282, § 6º
- Como o art. 280 exige a assinatura do infrator, o auto lavrado por equipamento eletrônico 
- O recurso tempestivo e legítimo contra a penalidade tem efeito suspensivo por força do art
- O recurso contra multa só pode ser admitido pela JARI depois do pagamento integral da pena
- O prazo de dez dias do art. 285, § 2º, relaciona-se à remessa pela autoridade autuadora à 
- O recurso de segunda instância administrativa deve ser apresentado dentro de vinte e quatr
- No segundo grau administrativo, multa aplicada por órgão ou entidade da União segue, em re
- Quando rodovia federal é concedida, a concessionária assume automaticamente todas as compe
```

## Pedido 8: Mineração (`minas-gerais.mineracao`, 45 tópicos de edital)

```
Matéria: minas-gerais.mineracao (Mineração)
Capítulos e quantas questões quero em cada um (total 100):
- "1. História e papel econômico da mineração em Minas Gerais": 25
- "2. Minerais metálicos e não metálicos": 25
- "3. Impactos ambientais e gestão de rejeitos": 25
- "4. Reescrita e como a IDECAN cobra": 25

Já existem (não repita):
- Qual mineral teve papel marcante na mineração mineira do período colonial?
- Qual relação entre minério de ferro e cadeia produtiva está correta?
- No recorte, como deve ser classificado o manganês?
- Qual alternativa apresenta um uso compatível com o nióbio?
- Qual conjunto reúne apenas exemplos de minerais não metálicos citados no tópico?
- O que se entende por rejeito de mineração neste tema?
- Que interpretação combina história colonial e economia mineira atual?
- Uma notícia informa que certo minério foi produzido em Minas Gerais. O que se pode conclui
- Qual aplicação do calcário é compatível com o tópico?
- Sobre mármore e quartzito, assinale a afirmação adequada.
- Qual distinção entre estéril e rejeito está correta?
- Uma área minerada apresenta solo exposto e perda de vegetação próxima a habitat natural. Q
- Qual conjunto de ações é compatível com gestão preventiva de rejeitos?
- O que é mais adequado associar ao encerramento de uma mina?
- Uma comparação entre mineração colonial e atual aponta ouro no passado e produção mineral 
- Um texto afirma que a extração de manganês contribui para a economia e que parte do minéri
- Durante a lavra, a remoção de cobertura é seguida por erosão e sedimentos em drenagem próx
- Após observar alteração na água a jusante de uma mina, qual procedimento sustenta melhor u
- Uma equipe precisa classificar dois materiais: rocha retirada para abrir acesso ao minério
- Uma área de lavra ocupa espaço próximo a habitat e deixa solo exposto. Qual conjunto de ef
- Qual combinação expressa uma sequência coerente de prevenção e resposta a riscos em estrut
- Qual afirmação resume corretamente a economia mineral contemporânea sem inventar dados de 
- Qual escolha de disposição trata corretamente o reaproveitamento de rejeitos e a segurança
- Depois do encerramento da lavra, a área apresenta erosão e solo instável. Qual medida se a
- Qual leitura relaciona corretamente classificação mineral e uso econômico?
- Uma síntese afirma que a mineração teve relevância histórica e econômica, mas requer gestã
- Uma operação planeja disposição de rejeitos e precisa prevenir falhas durante toda a vida 
- Uma mina encerrou a extração, mas persistem erosão e alteração de habitat. Qual afirmação 
- Uma questão combina o papel histórico do ouro com os riscos atuais da disposição de rejeit
- A exploração do ouro contribuiu para a ocupação do interior e para a organização econômica
- Se um mineral é produzido em Minas Gerais, pode-se afirmar sem consultar dados que toda a 
- Ferro, ouro, manganês e nióbio estão entre os exemplos de minerais metálicos no recorte de
- O rejeito é o material removido para abrir acesso ao minério, enquanto o estéril surge nec
- A remoção de vegetação e a alteração do solo podem criar pressões sobre habitats e sobre a
- Após o encerramento da lavra, a gestão ambiental pode incluir recuperação de áreas, estabi
- A presença de uma mina próxima a um curso d’água comprova que qualquer alteração de qualid
- A segurança de estruturas de disposição de rejeitos requer atenção continuada a condições,
- O calcário deve ser classificado como minério metálico porque pode ter valor econômico e p
- Todas as minas de Minas Gerais usam o mesmo método e produzem os mesmos impactos ambientai
- O possível reaproveitamento de rejeitos não elimina a necessidade de caracterizar e contro
- A interrupção da extração garante a recomposição imediata da biodiversidade e dispensa aco
- A destinação comercial de um produto mineral não altera, por si só, a distinção entre recu
- Se uma estrutura de rejeitos é acompanhada, os demais impactos da mina sobre solo, água e 
- A segurança das barragens de mineração em Minas Gerais é fiscalizada pela Agência Nacional
```

## Pedido 9: Plataforma .NET, C# e ASP.NET (`informatica.dotnet-csharp-aspnet`, 45 tópicos de edital)

```
Matéria: informatica.dotnet-csharp-aspnet (Plataforma .NET, C# e ASP.NET)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Arquiteturas de componentes: COM, .NET e Java EE": 26
- "2. Fundamentos de C#: tipos, objetos, coleções e memória": 17
- "3. ASP.NET clássico: páginas, validação, configuração e autenticação": 19
- "4. ADO.NET: conexões, comandos, leitura e transações": 17
- "5. LINQ e mapeamento objeto-relacional: execução, consulta e persistência": 21

Já existem (não repita):
- Uma aplicação em C# destinada ao .NET Framework executará suas operações sob supervisão do
- Uma aplicação C# utiliza uma biblioteca compilada que contém tipos e metadados em um arqui
- Uma equipe quer definir um conjunto de operações que diferentes classes de C# poderão impl
- Um formulário ASP.NET Web Forms deve impedir o envio de um campo de texto obrigatório vazi
- Um sistema ASP.NET Web Forms do .NET Framework 4.0 necessita configurar a página de login 
- Uma consulta SQL retorna centenas de linhas e o programa precisa lê-las uma a uma, sequenc
- Em C#, uma lista de números deve ser filtrada para conservar apenas os maiores que 10 usan
- Um projeto utiliza Entity Framework para relacionar objetos Cliente e Pedido a registros d
- Um sistema .NET Framework precisa consumir um componente COM legado registrado no Windows.
- Considere `class Item { public int Valor; }`. Após `var a = new Item(); a.Valor = 2; var b
- No código `enum Fase { Inicio, Analise = 5, Final }`, nenhum outro valor foi definido. Qua
- Em Web Forms, a equipe já incluiu controles de validação e precisa reunir suas mensagens n
- Em configuração de Forms Authentication clássico, a aplicação usa uma regra de autorização
- Uma aplicação precisa executar `SELECT COUNT(*) FROM Pedidos WHERE Situacao = @s` e recebe
- Um relatório deve carregar tabelas em memória, permitir edição enquanto a conexão está fec
- Em uma aplicação com `List<int> itens`, foi atribuído `var q = itens.Where(x => x > 4);` e
- Uma biblioteca construída para o .NET Framework pretende ser utilizada por outras linguage
- Considere `struct Caixa { public List<int> Valores; }`. Após `a.Valores = new List<int> { 
- Em um processamento C#, uma exceção é capturada, registrada e deve seguir à camada superio
- Uma página Web Forms exibe login por Forms Authentication e exige o preenchimento de campo
- Um sistema transfere valores entre duas contas por comandos separados na mesma conexão e d
- Uma consulta ADO.NET filtra por ID recebido de formulário e, opcionalmente, permite escolh
- Uma consulta LINQ é feita sobre sequência que pode estar vazia ou conter vários elementos.
- Uma consulta ORM carrega cem pedidos e, ao percorrer cada pedido, acessa propriedade Clien
- Um projeto exige aderência histórica estrita às APIs disponíveis no lançamento do .NET Fra
- Ao iniciar uma aplicação .NET Framework, o código C# previamente compilado em CIL pode ser
- Uma aplicação de .NET Framework substituiu um componente COM por uma interface C# e passou
- Um programador cria `Queue<string>` para atender solicitações na ordem em que chegam; essa
- Após abrir uma conexão ADO.NET em bloco using, o programador pode assumir que o coletor de
- Em um formulário Web Forms que possui regras de validação associadas ao botão de envio, a 
- Em uma consulta ADO.NET, `ExecuteScalar()` é a API cuja finalidade principal é percorrer s
- Uma consulta LINQ to Objects pode combinar `Where` para filtrar objetos de uma lista e `Se
- Uma classe C# que já deriva de uma classe de negócio pode derivar diretamente também de ou
- Um programa C# usa `try`, captura uma FormatException e executa um bloco `finally` para re
- Uma aplicação configura Forms Authentication e considera, por esse fato isolado, que todos
- Em um formulário Web Forms, a validação de um campo por `CompareValidator` pode comparar s
- Em ADO.NET, alterar a linha de um DataTable pertencente a um DataSet desconectado torna a 
- Ao consumir um objeto COM no .NET Framework, um RCW pode intermediar as chamadas e a conve
- Ao executar LINQ sobre `IEnumerable<T>` em memória, toda expressão `Where` é convertida pr
- Uma consulta LINQ to Objects foi materializada por `ToList()`. Depois disso, a modificação
- Ao consultar uma entidade por Entity Framework e alterar propriedade rastreada, o sistema 
- Uma consulta de ORM que carrega uma coleção inicial e dispara nova consulta para cada elem
- Em um projeto que se declara estritamente compatível com C# 4.0 e com as APIs iniciais do 
- Em um aplicativo de transferência financeira, um par de comandos ADO.NET que participam da
- Um sistema Web Forms clássico pode omitir verificações de autorização no servidor desde qu
```

## Pedido 10: Engenharia de produção (`engenharia.engenharia-producao`, 45 tópicos de edital)

```
Matéria: engenharia.engenharia-producao (Engenharia de produção)
Capítulos e quantas questões quero em cada um (total 100):
- "Sistemas produtivos, operações e arranjos físicos": 17
- "Planejamento e controle da produção (PCP)": 17
- "Qualidade, Seis Sigma e gestão de processos": 17
- "Pesquisa operacional, logística e decisões": 17
- "Engenharia econômica, custos e projetos industriais": 16
- "Engenharia do trabalho, métodos, tempos e ergonomia": 16

Já existem (não repita):
- No arranjo físico por produto, as estações de trabalho tendem a ser dispostas conforme a s
- Uma redução da distância de transporte garante que o novo layout fabril é superior, indepe
- Se uma linha dispõe de 360 minutos úteis por dia e sua demanda diária é de 120 unidades, s
- Melhorar em 30% o desempenho de uma máquina que não é gargalo implica, necessariamente, au
- Qual arranjo físico é tipicamente mais adequado à construção de um navio de grandes dimens
- Uma matriz origem–destino informa 8 viagens entre A e B, com 15 metros por viagem, e 6 ent
- Uma célula possui 15 minutos de trabalho elementar por peça e opera com ciclo de 5 minutos
- Uma fábrica de peças customizadas processa lotes pequenos com roteiros variados e possui m
- A lista de materiais (BOM) descreve relações entre produto final, subconjuntos e component
- A política Kanban é exemplo de sistema exclusivamente empurrado, pois toda ordem nasce de 
- No MRP, se uma peça deve ser recebida na semana 7 e seu lead time é de duas semanas, a lib
- A regra SPT, por priorizar a operação mais curta, minimiza necessariamente o atraso máximo
- Qual documento do PCP detalha, por período, os itens finais a fabricar e alimenta a explos
- Uma empresa usa suavização exponencial simples com α = 0,25. A previsão anterior foi 200 e
- Para fabricar 40 produtos X, cada um exige três componentes Y. O estoque disponível de Y é
- Um recurso crítico já opera continuamente sem ociosidade e a etapa anterior amplia sua pro
- O gráfico de Pareto auxilia a priorização das categorias mais relevantes de defeitos, segu
- Limites estatísticos de controle e limites de especificação de um produto são necessariame
- Na mesma base de cálculo, o Cpk é maior que o Cp sempre que a média do processo se aproxim
- Em outubro de 2026, a ISO 9001:2015 continuava sendo a edição internacional vigente, sem n
- Uma equipe quer visualizar hipóteses de causas de um problema de retrabalho usando categor
- Foram encontrados 24 defeitos em 1.000 unidades avaliadas, cada uma com 4 oportunidades de
- Um processo estável tem LIE = 10, LSE = 22, média = 19 e desvio-padrão = 2. Qual Cpk?
- Uma indústria inicia projeto Seis Sigma. Depois de medir perdas e validar seu sistema de m
- Na programação linear, a função objetivo e as restrições são expressas por relações linear
- A classificação ABC de estoques considera necessariamente apenas a quantidade física armaz
- Pela Lei de Little, em um sistema estável com vazão média de 12 pedidos por hora e permanê
- O preço-sombra de uma restrição linear representa uma variação marginal do ótimo, sendo ap
- Qual técnica procura selecionar rotas em uma rede de transportes que minimizem a soma dos 
- Sob demanda anual D = 8.000 unidades, custo por pedido S = R$ 25 e custo anual de posse H 
- Um modelo fornece solução matemática ótima, mas suas restrições omitem uma limitação físic
- Qual das opções expressa corretamente o papel da simulação Monte Carlo na análise de uma r
- A margem de contribuição unitária corresponde ao preço unitário deduzido dos custos e desp
- Um custo de produção diretamente identificável com determinada ordem de fabricação não pod
- No gerenciamento do valor agregado, se EV = R$ 80 mil e AC = R$ 100 mil, o desvio de custo
- O índice de desempenho de prazo SPI = 0,80 indica que um projeto terminará, necessariament
- Uma linha vende cada unidade por R$ 50 e possui gastos variáveis de R$ 30 por unidade. Qua
- Uma fábrica tem gastos fixos mensais de R$ 15.000 e margem de contribuição unitária de R$ 
- Em uma análise de valor agregado, PV = 120, EV = 100 e AC = 80 (mil reais). Quais CV e SV,
- Um plano de implantação inclui atividades com durações otimista a = 2 dias, mais provável 
- A ergonomia considera tanto fatores físicos quanto demandas cognitivas e aspectos da organ
- O tempo normal de uma operação é obtido diretamente pelo menor tempo observado, sem qualqu
- Se a tolerância equivale a 20% do tempo total, então um tempo normal de 4 minutos resulta 
- A NR-17 determina que toda e qualquer estação de trabalho, sem distinção de circunstâncias
- Qual elemento é tipicamente investigado pela ergonomia cognitiva?
- O tempo observado é de 6 minutos e o fator de ritmo é 1,10. Qual o tempo normal?
- Um tempo normal é de 8 minutos e a tolerância de 20% foi definida como fração do tempo tot
- Uma fábrica automatiza postos repetitivos, mas aumenta a carga de supervisão e a frequênci
```

## Pedido 11: Tributação estadual de São Paulo (`direito-tributario.tributacao-estadual-sao-paulo`, 45 tópicos de edital)

```
Matéria: direito-tributario.tributacao-estadual-sao-paulo (Tributação estadual de São Paulo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema estadual paulista, ICMS e estrutura da legislação": 15
- "2. ICMS especial: convênios, combustíveis, Simples Nacional e precedentes": 15
- "3. ITCMD paulista: fatos geradores, valores e isenções": 19
- "4. IPVA paulista, lançamento de ofício e garantia do contribuinte": 21
- "5. Nos Conformes, PAT-SP e jurisprudência vinculante": 30

Já existem (não repita):
- Uma empresa paulista precisa identificar o ato que institui o ICMS estadual, e não apenas 
- Uma transportadora realiza viagem remunerada que se inicia em Campinas e termina em Soroca
- Na apuração didática de uma empresa, operações geraram R$ 1.800 de débito de ICMS e existe
- Uma sociedade movimenta mercadoria de seu estabelecimento em Santos para filial própria na
- Uma doação paulista tributável, fora das hipóteses de isenção, possui base de R$ 50.000. A
- Em um ano civil, a mesma pessoa faz duas doações à mesma sobrinha. Como deverão ser tratad
- Um automóvel de passeio comum, sem qualquer isenção ou redução e com base de R$ 75.000, se
- Em janeiro de 2026, uma motocicleta de 180 cm³ pertence a pessoa física, com registro e li
- Uma proposta de isenção de ICMS precisa do convênio do regime geral da LC 24/1975. Qual vo
- Uma microempresa paulista optante pelo Simples revende mercadoria sujeita, no caso, ao ICM
- Um inventário paulista compreende, em hipótese autorizada, apenas um imóvel que será trans
- Uma cobrança de IPVA não pago parcialmente é constituída de ofício. Qual é a função especí
- Em procedimento de conformidade, um contribuinte é classificado NC no programa paulista No
- Uma empresa recebe auto de infração paulista decorrente de lançamento de ofício. Dentro de
- Uma decisão de primeira apreciação rejeita a defesa do autuado paulista, e o crédito indic
- Um contribuinte contesta o art. 5º da LC 939/2003, dizendo que qualquer lançamento em livr
- O Estado pretende regularizar incentivo antigo de ICMS concedido sem o convênio exigido. Q
- Uma decisão inicial desfavorável à Fazenda estadual afastou débito originário de 20.000 UF
- O contribuinte pretende recorrer de acórdão do Tribunal de Impostos e Taxas apenas porque 
- Um servidor analisa incidência sobre gasolina no regime da LC 192/2022, considerada a alte
- Uma pessoa faleceu deixando depósitos bancários de valor total de 900 UFESPs em situação a
- Em 2026, determinada sociedade empresária é dona de uma motocicleta com motor de 160 cm³ e
- Em 2026, um parecer trata o Tema 825 como proibição permanente de qualquer ITCMD envolvend
- Uma empresa paulista passa a classificar fornecedores conforme o programa Nos Conformes e 
- Na apreciação de um caso, a Fazenda paulista foi vencida em decisão recorrível do PAT, e i
- Uma loja paulista vende mercadorias próprias a consumidores, transferindo-lhes a titularid
- Uma transportadora leva passageiros entre dois bairros do mesmo município paulista e concl
- Ao distinguir a norma paulista instituidora da norma executiva, um contribuinte identifica
- Um proprietário contesta cobrança de IPVA alegando que o Decreto 54.714/2009, por ter inst
- Uma indústria paulista pode, observadas as condições legais, compensar débito de ICMS deco
- Uma reunião de convênio de ICMS aplica corretamente a LC 24/1975 ao exigir quatro quintos 
- Para regularizar incentivos antigos de ICMS na hipótese da LC 160/2017, convênio específic
- Ao revender diesel abrangido pelo regime monofásico da LC 192/2022, um estabelecimento pod
- Uma empresa paulista optante pelo Simples Nacional pode continuar obrigada ao ICMS recolhi
- Uma pessoa faz três doações ao mesmo beneficiário dentro do ano civil paulista e aplica a 
- Na legislação paulista do ITCMD consultada, doação tributável de valor venal de R$ 100.000
- Uma sucessão foi aberta pela morte em 2024, mas o inventário será concluído em 2026. Para 
- No exercício de 2026, motocicleta de 180 cm³ de pessoa física com situação regular de regi
- Um veículo paulista que completou exatamente vinte anos desde a fabricação é isento de IPV
- Na fiscalização paulista, livros contábeis apoiados em documentos hábeis podem gozar de pr
- Ao lançar dados do programa Nos Conformes, o Estado coloca um contribuinte na categoria NC
- Uma pessoa autuada por lançamento de ofício no ICMS paulista tem, segundo o art. 35 da Lei
- No processo paulista, decisão que manteve integralmente auto de infração superior a 20.000
- No Tribunal de Impostos e Taxas paulista, o contribuinte que interpõe recurso especial dev
- Após a edição da LC 227/2026, a tese do Tema 825 permite ignorar a nova legislação naciona
```

## Pedido 12: Riscos da atividade bancária e risco de mercado (`conhecimentos-bancarios.riscos-bancarios-var-basileia`, 44 tópicos de edital)

```
Matéria: conhecimentos-bancarios.riscos-bancarios-var-basileia (Riscos da atividade bancária e risco de mercado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, classificação e interdependência dos riscos bancários": 21
- "2. Risco de crédito, clientes, operações e capital de Basileia": 23
- "3. Volatilidade, Value at Risk e risco de carteira": 23
- "4. Expected shortfall, cenários, estresse e validação": 20
- "5. Gregas de opções e proteção delta-gama": 13

Já existem (não repita):
- Uma instituição concede financiamento a empresa que deixa de pagar parcelas após forte que
- Uma carteira de títulos de taxa fixa perde valor quando a taxa exigida pelo mercado aument
- Após saques inesperados, uma instituição dispõe de ativos valiosos, mas só consegue transf
- Um defeito no processamento interno duplica transferências e gera despesas para corrigir p
- Uma pane em infraestrutura essencial interrompe liquidações em várias instituições, compro
- Para um empréstimo, estima-se PD de 2%, LGD de 40% e EAD de R$ 100.000. Qual é a perda esp
- Uma nova garantia aumenta a recuperação estimada num empréstimo, sem modificar o perfil do
- Um resumo do acordo de 2004 apresenta três pilares para a regulação prudencial. Qual conju
- Um relatório indica VaR de um dia de R$ 100 mil a 99% de confiança. Como interpretar essa 
- Uma posição linear de R$ 1 milhão tem volatilidade diária de 2%, média desprezível e retor
- O VaR diário de certa carteira é R$ 10.000. Admitindo retornos independentes, escala tempo
- Em 100 cenários igualmente prováveis, as cinco maiores perdas são R$ 10 mil, 15 mil, 20 mi
- Uma instituição compara perdas diárias realizadas às projeções de VaR feitas anteriormente
- A análise de uma carteira considera, conjuntamente, alta de juros, depreciação cambial e q
- Uma posição em opções tem delta agregado de +250 unidades do ativo subjacente. Qual operaç
- Uma opção apresenta delta 0,50 e gama 0,20 por unidade. Para aumento pequeno de R$ 1 no at
- Dois segmentos de carteira apresentam desvios-padrão monetários de R$ 3 milhões e R$ 4 mil
- Uma instituição possui RWA de R$ 500 milhões. Considerando apenas o mínimo internacional d
- Duas carteiras têm o mesmo VaR a 95%, mas a primeira apresenta perdas raras muito mais sev
- Ao aplicar o padrão revisado de modelos internos para capital de risco de mercado, qual af
- Duas posições lineares têm desvios monetários iguais a R$ 10 mil e correlação de −0,5. Qua
- Um modelo de VaR a 99% foi avaliado ao longo de 250 observações diárias sem mudanças signi
- Uma carteira apresenta delta +120 e gama +30. Cada contrato de opção usado para hedge acre
- Uma equipe começa pelo resultado de quebra de capacidade de pagamento e procura as combina
- Uma posição de opções tem delta líquido zero e gama positivo. Após movimento expressivo do
- Uma empresa deixa de pagar financiamento após deterioração de sua geração de caixa. A anál
- Uma instituição que dispõe de ativos de qualidade não pode enfrentar risco de liquidez, po
- Uma falha em infraestrutura compartilhada que compromete liquidações de muitos participant
- A queda do preço de uma ação mantida para negociação caracteriza, por si, risco operaciona
- Para uma operação com PD de 5%, LGD de 20% e EAD de R$ 80.000, a perda esperada simplifica
- Em modelos de perda esperada, EAD expressa a chance de inadimplência e PD corresponde à ex
- Basileia II organiza seu arcabouço em exigências mínimas de capital, revisão supervisora e
- Pelo padrão geral de Basileia, o colchão de conservação de 2,5% substitui o requisito míni
- Ao elevar o nível de confiança de 95% para 99%, mantendo carteira, horizonte e distribuiçã
- Uma carteira com VaR diário de R$ 50 mil a 99% apresenta, pelas premissas do modelo, perda
- Em uma distribuição contínua, o expected shortfall a 97,5% pode ser interpretado como a mé
- Um teste de estresse que impõe alta de juros e queda do câmbio é o mesmo que backtesting, 
- A conversão de volatilidade diária para prazo de quatro dias pela raiz de quatro exige pre
- A volatilidade implícita de uma opção é calculada exclusivamente pela dispersão de seus re
- Se o delta de uma opção vale 0,40 e o gama é positivo, um pequeno aumento no preço do ativ
- Para neutralizar o gama não nulo de uma carteira de opções, basta combinar a carteira com 
- Duas posições lineares com desvios iguais e correlação perfeitamente negativa podem aprese
- Uma série de backtesting sem exceções comprova que o modelo de risco capturará corretament
- O padrão internacional revisado para modelos internos de risco de mercado utiliza ES unila
- Uma instituição economicamente solvente, cujo valor estimado dos ativos supera as obrigaçõ
```

## Pedido 13: Química orgânica: isomeria e reações (`ciencias.quimica-organica-isomeria-reacoes`, 44 tópicos de edital)

```
Matéria: ciencias.quimica-organica-isomeria-reacoes (Química orgânica: isomeria e reações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Isomeria constitucional: cadeia, posição e função": 13
- "2. Estereoquímica: geometria, quiralidade, R/S e projeções": 27
- "3. Reatividade, efeitos eletrônicos, adição, substituição e eliminação": 27
- "4. Oxidação, redução, combustão, esterificação e hidrólise": 20
- "5. Polimerização e integração de transformações com estereoquímica": 13

Já existem (não repita):
- Duas amostras apresentam fórmula molecular C4H10. Uma corresponde a CH3–CH2–CH2–CH3 e a ou
- Um estudante compara but-1-eno e but-2-eno, ambos C4H8, identificando que a cadeia conserv
- Dois frascos contêm etanol, CH3CH2OH, e metoximetano, CH3OCH3. Ambos apresentam fórmula C2
- Para verificar se etanol (C2H6O) e etanal (C2H4O) podem ser classificados como isômeros, u
- Um desenho apresenta dupla ligação carbono-carbono. Para que esse alqueno simples tenha du
- Uma equipe compara propan-2-ol e butan-2-ol, avaliando apenas a presença de carbono tetraé
- Ao reagir eteno com bromo molecular em condições usuais de adição, obtém-se 1,2-dibromoeta
- Uma amostra de propan-2-ol é submetida à oxidação branda adequada, sem ruptura da cadeia d
- Um hidrocarboneto propano é queimado com fornecimento suficiente de oxigênio e combustão c
- Um alqueno apresenta ligantes Br e H num carbono da dupla, e Cl e H no outro. Se Br e Cl e
- Uma molécula de 2,3-dibromobutano possui dois centros estereogênicos e uma possibilidade d
- Em projeção de Fischer de um único centro estereogênico, grupos CIP 1, 2, 3 e 4 ocupam, re
- Comparam-se ácido etanoico, fenol e etanol em condições aquosas introdutórias. Qual ordem 
- Bromometano reage com hidróxido em meio apropriado, formando metanol e brometo. Em classif
- O propeno é tratado com HBr em condições que favorecem mecanismo radicalar pela presença d
- Ácido etanoico reage com etanol sob catálise ácida apropriada e aquecimento, em equilíbrio
- Metanoato de metila é submetido à hidrólise em solução aquosa de NaOH suficiente. Quais pr
- Duas estruturas com mesma conectividade possuem configurações (R,R) e (R,S) em dois centro
- Um frasco contém um enantiômero configurado como R e outro contém o S correspondente. Sem 
- Em solvente prótico apropriado, um haleto terciário sofre substituição e forma produto com
- Na nitração controlada do fenol, o grupo OH modifica a distribuição eletrônica do anel aro
- Ao reagir etanal com uma fonte de HCN, um químico prevê ataque ao carbono de C=O e formaçã
- Em ensaio, dois mols de propano são postos em contato com apenas nove mols de O2, sem supr
- Duas sínteses industriais simplificadas são comparadas: eteno produz polietileno e a reaçã
- Uma sequência conceitual parte de propanona, forma propan-2-ol com redutor apropriado e de
- Butano e 2-metilpropano possuem mesma fórmula molecular, mas diferem no esqueleto carbônic
- O but-1-eno apresenta isômeros geométricos E e Z porque qualquer alqueno, mesmo contendo c
- Num alqueno cujos carbonos da dupla possuem substituintes diferentes, o descritor Z indica
- O propan-2-ol apresenta enantiômeros porque o carbono que porta OH possui quatro ligantes 
- Em presença de catalisador apropriado, a hidrogenação do eteno converte C=C em C–C, forman
- Etanol transforma-se em etanal por uma redução carbonílica, uma vez que o álcool passa a a
- A substituição SN2 em centro tetraédrico estereogênico envolve ataque concertado pelo lado
- Na adição de HBr a propeno com peróxidos que favorecem a via radicalar, a orientação usual
- O 2,3-dibromobutano pode apresentar dois enantiômeros e uma forma meso distinta; a molécul
- Quando um éster é tratado com base aquosa em quantidade adequada, os produtos principais s
- Na combustão completa de dois mols de propano, a equação balanceada exige dez mols de oxig
- A nitração típica de benzeno constitui substituição nucleofílica SN2 em um carbono do anel
- A formação de poliéster por reação típica entre diol e diácido pode liberar pequenas moléc
- Uma projeção de Fischer com centro estereogênico pode ser girada de 90 graus no plano do p
- A maior acidez do fenol em relação ao etanol pode ser explicada, em nível introdutório, pe
- Toda reação SN1 de substrato quiral gera obrigatoriamente mistura racêmica exata de 50% de
- A adição de nucleófilo a uma carbonila de aldeído pode formar novo vínculo no carbono carb
- A oxidação branda de álcool terciário produz normalmente uma cetona sem romper ligações ca
- A redução de um aldeído como etanal para etanol implica diminuição do grau de oxidação do 
- Toda polimerização por crescimento em cadeia libera água em cada etapa de propagação, de m
```

## Pedido 14: Necropsia: técnicas de apoio, preservação de cadáveres e biossegurança (`medicina-legal.necropsia-tecnicas-biosseguranca`, 44 tópicos de edital)

```
Matéria: medicina-legal.necropsia-tecnicas-biosseguranca (Necropsia: técnicas de apoio, preservação de cadáveres e biossegurança)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Recepção, identificação e cadeia de custódia": 17
- "2. Transporte, movimentação e armazenamento": 17
- "3. Sala de necropsia e auxílio ao exame": 17
- "4. Preservação de cadáveres, tecidos e peças": 17
- "5. Biossegurança: riscos, EPI, limpeza e resíduos": 16
- "6. Acidentes com material biológico e aspectos éticos e legais": 16

Já existem (não repita):
- Ao receber um corpo no instituto médico-legal, a primeira providência do auxiliar de necro
- Vestes úmidas ou impregnadas de sangue retiradas de um cadáver devem ser imediatamente aco
- A identificação definitiva de um cadáver é feita por métodos técnicos, como:
- Ao arrolar valores e documentos encontrados com o cadáver, o procedimento adequado é:
- Cada vestígio coletado durante a necropsia deve ser lacrado, identificado e ter registrado
- Ao perceber divergência entre o nome constante do documento de encaminhamento e a etiqueta
- Na movimentação manual de cadáveres, constitui técnica segura:
- A bolsa mortuária deve ser impermeável, resistente, com fechamento e identificação externa
- A conservação de cadáveres em câmaras frias baseia-se no fato de que a baixa temperatura:
- A temperatura das câmaras frias deve ser controlada e registrada diariamente, e falhas no 
- Antes de transferir o corpo da maca para a mesa de necropsia, é desnecessário travar as ro
- A incisão utilizada no couro cabeludo para a abertura do crânio na necropsia, que vai de u
- O instrumento utilizado para seccionar as costelas durante a abertura do tórax é o:
- Para exames toxicológicos, o sangue periférico, colhido de vasos femorais, é preferível ao
- Durante a necropsia, compete ao auxiliar de necropsia:
- A serra oscilante utilizada na abertura do crânio gera aerossóis e ruído, exigindo proteçã
- Ao final da necropsia, as incisões devem ser suturadas e o corpo higienizado e recomposto,
- A mesa de necropsia deve ser preferencialmente de:
- O fixador mais utilizado para a preservação de tecidos e peças anatômicas destinados a exa
- Para garantir a fixação adequada, o volume de formol deve ser aproximadamente:
- Amostras destinadas a exames toxicológicos e de DNA devem ser colocadas em formol, para ev
- O conjunto de técnicas de conservação temporária e de apresentação do corpo para velório e
- O formaldeído é irritante para olhos e vias respiratórias e é classificado como cancerígen
- No ambiente de necropsia, a exposição a aerossóis com risco de transmissão de tuberculose 
- Segundo as precauções padrão, apenas os cadáveres com diagnóstico confirmado de doença inf
- Na necropsia, a luva anticorte é utilizada principalmente para:
- A máscara de proteção respiratória indicada na necropsia de caso suspeito de tuberculose é
- A NR-32, que trata da segurança no trabalho em serviços de saúde, proíbe o reencape e a de
- Em superfícies contaminadas com sangue e outros fluidos, a sequência correta de procedimen
- A eliminação de vetores e pragas, como insetos e roedores, no ambiente do instituto é deno
- Segundo a RDC Anvisa nº 222/2018, os resíduos perfurocortantes pertencem ao grupo:
- O descarte de perfurocortantes deve ser feito imediatamente após o uso, em recipiente rígi
- Resíduos químicos, como o formol usado na fixação de peças, devem ser descartados como res
- Após um acidente perfurocortante com material biológico na pele, a conduta imediata corret
- Quando indicada, a profilaxia pós-exposição ao HIV deve ser iniciada o quanto antes, ideal
- Em caso de respingo de sangue nos olhos durante a necropsia, deve-se:
- Segundo o art. 162 do Código de Processo Penal, a autópsia será feita:
- Nos casos de morte violenta, bastará o simples exame externo do cadáver quando não houver 
- O servidor que fotografa um cadáver com seu celular pessoal e divulga as imagens em redes 
- No Código Penal, a conduta de vilipendiar cadáver ou suas cinzas está prevista como crime:
- Questionado por familiares sobre a causa da morte, o auxiliar de necropsia deve informar o
```

## Pedido 15: Atuação do agente comunitário de saúde (`saude-publica.agente-comunitario`, 43 tópicos de edital)

```
Matéria: saude-publica.agente-comunitario (Atuação do agente comunitário de saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "Atribuições, competências e limites do ACS": 20
- "Territorialização, microárea e cadastramento": 20
- "Visita domiciliar e acompanhamento de grupos prioritários": 20
- "Educação popular, mobilização comunitária e intersetorialidade": 20
- "e-SUS APS, Siaps, registros e segurança da informação": 20

Já existem (não repita):
- Ao ouvir de uma moradora que não consegue comparecer à UBS, qual atitude corresponde às at
- Que prática expressa a Educação Popular em Saúde aplicada pelo ACS?
- Entre ACS e ACE, qual comparação é mais adequada?
- Em visita, gestor solicita que ACS sem curso técnico concluído realize glicemia capilar de
- No processo de territorialização, o conceito de microárea corresponde a:
- Um loteamento recém-ocupado surge no território. Qual providência atende ao planejamento d
- O ACS encontra um imóvel desocupado, mas pertencente a sua microárea. Na dimensão domicili
- Qual informação pertence especialmente ao cadastro domiciliar e territorial?
- Uma microárea contém idoso acamado sem cuidador e adulto saudável. Como o ACS deve planeja
- Uma gestante perdeu consultas e não atende ligações. Qual conduta configura busca ativa?
- Durante visita, o ACS encontra carteira de vacinação da criança com possíveis pendências. 
- Idosa relata quedas frequentes em escada de casa. Qual é a participação adequada do ACS?
- Para organizar grupo sobre alimentação com famílias de baixa renda, qual conduta favorece 
- Em roda de conversa, alguém pergunta qual morador recebe tratamento de tuberculose. O ACS 
- Ao acompanhar condicionalidades relacionadas a programa social, é competência do ACS:
- Como planejar uma atividade educativa inclusiva para adolescentes da microárea?
- Assinale a relação correta entre e-SUS APS e e-SUS Território.
- Considerando os sistemas do Ministério da Saúde em 2026, é correto afirmar que:
- Em dias diferentes, o ACS atualiza cadastro e realiza visita de acompanhamento. Que regist
- A equipe quer apresentar distribuição de condições de saúde numa reunião aberta. Qual abor
- Uma família mudou de endereço para fora da microárea. A medida que preserva qualidade do v
- O ACS encontra pessoa acamada com relato de piora súbita da respiração. Sua prioridade é:
- O indicador mostra muitas doses de vacinas não registradas. Qual é a interpretação inicial
- Pessoas com sofrimento psíquico evitam a unidade por receio de serem expostas. Como o ACS 
- Após um encontro comunitário, a equipe registrou apenas a quantidade de presentes. Qual pa
- O ACS contribui para integrar comunidade e equipe de atenção primária por meio de visitas,
- O vínculo de confiança com as famílias autoriza o ACS a prescrever livremente medicamentos
- A realização de visitas domiciliares rotineiras, casa a casa, é descrita como atividade pr
- Aferição de pressão arterial em caráter excepcional exige apenas aparelho disponível, disp
- O planejamento e a análise de dados obtidos em visitas podem fazer parte das atividades co
- A microárea deve ser entendida apenas como desenho geográfico, sem interesse pelas caracte
- Territorialização inclui mapear equipamentos sociais, barreiras de deslocamento, riscos sa
- O cadastramento domiciliar deve ser realizado apenas no primeiro encontro, sendo inadequad
- O cadastro individual e o cadastro domiciliar e territorial são complementares, pois descr
- Para garantir igualdade, toda família deve receber obrigatoriamente o mesmo número de visi
- Buscar contato respeitoso com gestante que deixou o pré-natal é um exemplo de busca ativa 
- A visita feita pelo ACS equivale necessariamente a consulta clínica domiciliar realizada p
- O acompanhamento de crianças pelo ACS inclui observar situação vacinal e evolução de peso 
- Ao encontrar sinais novos de agravamento em pessoa acamada, o ACS deve esperar a transmiss
- A educação em saúde baseada no diálogo pode começar pelo reconhecimento dos saberes e difi
- Compete ao ACS cancelar benefícios sociais quando famílias descumprem alguma recomendação 
- O ACS pode promover a participação social no planejamento de ações sem revelar dados de sa
- e-SUS APS designa uma estratégia de informatização que compreende diferentes softwares e a
- Em 2026, o Siaps é apresentado oficialmente como sucessor progressivo do Sisab, não sendo 
- O compartilhamento público de nomes associados a diagnósticos é permitido ao ACS quando a 
```

## Pedido 16: Lei Orgânica da Polícia Civil de Minas Gerais (LC nº 129/2013 e regime disciplinar da Lei nº 5.406/1969) (`legislacao.lei-organica-pcmg`, 43 tópicos de edital)

```
Matéria: legislacao.lei-organica-pcmg (Lei Orgânica da Polícia Civil de Minas Gerais (LC nº 129/2013 e regime disciplinar da Lei nº 5.406/1969))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, princípios e funções da PCMG": 17
- "2. Competência, estrutura orgânica e órgãos de administração": 17
- "3. Administração superior: Chefia, Conselho Superior e Corregedoria": 17
- "4. Estatuto: prerrogativas, direitos, remoção, jornada e licenças": 17
- "5. Carreiras: ingresso, estágio probatório e desenvolvimento": 16
- "6. Regime disciplinar da Lei nº 5.406/1969": 16

Já existem (não repita):
- De acordo com a LC nº 129/2013, a investigação criminal realizada pela Polícia Civil de Mi
- A indivisibilidade, a interdisciplinaridade e a indelegabilidade estão entre os princípios
- Entre as diretrizes que a PCMG deve observar em sua atuação, conforme o art. 3º da LC nº 1
- São símbolos institucionais da PCMG o hino, o brasão, a bandeira e o lema.
- Segundo o art. 10 da LC nº 129/2013, a função de polícia judiciária compreende:
- Pela LC nº 129/2013, a investigação criminal começa com o conhecimento de ato ou fato pass
- À PCMG é assegurada autonomia administrativa e financeira, cabendo-lhe elaborar sua progra
- Nos termos da LC nº 129/2013, a Polícia Civil de Minas Gerais subordina-se diretamente:
- As funções constitucionais da PCMG podem ser delegadas, por convênio, a servidores de outr
- Na estrutura orgânica da PCMG, o Instituto de Identificação subordina-se à:
- O Instituto de Criminologia e o Colégio Ordem e Progresso, unidades da estrutura da PCMG, 
- O Corregedor-Geral, os superintendentes e o Diretor da Academia de Polícia Civil são escol
- As Delegacias Regionais de Polícia Civil e as Divisões de Polícia Especializada são dirigi
- O Chefe da Polícia Civil de Minas Gerais é nomeado pelo Governador do Estado dentre:
- O Chefe Adjunto da PCMG tem prerrogativas, vantagens e padrão remuneratório do cargo de Se
- Nos afastamentos eventuais do Chefe da PCMG e também do Chefe Adjunto, a substituição cabe
- O Conselho Superior da PCMG possui em sua estrutura o Órgão Especial, a Câmara Disciplinar
- A Câmara Disciplinar do Conselho Superior da PCMG, que julga recursos contra atos do Corre
- O regimento interno do Conselho Superior da PCMG é aprovado por maioria simples e instituí
- Havendo indícios suficientes de transgressão disciplinar, a Corregedoria-Geral pode propor
- O afastamento de servidor da PCMG para fins disciplinares por período superior a noventa e
- O Inspetor-Geral de Escrivães de Polícia e o Inspetor-Geral de Investigadores de Polícia i
- Segundo a LC nº 129/2013, são atividades privativas da Academia de Polícia Civil, vedado s
- A direção da Superintendência de Polícia Técnico-Científica da PCMG cabe:
- No exercício da perícia oficial criminal, o Perito Criminal e o Médico-Legista têm autonom
- A Superintendência de Investigação e Polícia Judiciária pode incumbir Delegado de Polícia,
- O órgão da PCMG encarregado do relacionamento com outros órgãos públicos e de manter canal
- Constitui prerrogativa do policial civil prevista no art. 45 da LC nº 129/2013:
- O Delegado de Polícia só pode ser preso por ordem judicial escrita, salvo em flagrante de 
- Condenado por decisão transitada em julgado a pena que não implique demissão e cuja nature
- A ajuda de custo devida ao integrante das carreiras da PCMG removido ex officio com mudanç
- A remoção a pedido ou por permuta, a remoção para acompanhar cônjuge e a remoção por conve
- A remoção de policial civil por conveniência da disciplina depende da existência de vaga n
- A prestação de serviço em regime de plantão implica descanso imediato e subsequente pelo p
- A carga horária semanal dos policiais civis é de quarenta horas, admitida jornada diária o
- A licença por motivo de doença em pessoa da família do policial civil é concedida:
- Sem prejuízo da remuneração, o policial civil pode afastar-se das funções por cinco dias c
- O período em trânsito para a realização de diligências policiais em localidade diversa da 
- NÃO é carreira policial civil prevista no art. 76 da LC nº 129/2013:
- O policial civil submete-se a estágio probatório de três anos, contado a partir da entrada
- Na etapa de provas e títulos dos concursos da PCMG, além de prova objetiva, discursiva e t
- O concurso para ingresso na carreira de Delegado de Polícia é realizado, nas provas de con
- A comissão de acompanhamento e avaliação especial de desempenho do Escrivão de Polícia em 
- Não há subordinação hierárquica entre o Escrivão de Polícia, o Investigador de Polícia, o 
- Nos termos da LC nº 129/2013, progressão é:
- Perde o direito à progressão e à promoção, no período aquisitivo, o policial civil que:
- A promoção especial do Escrivão de Polícia e do Investigador de Polícia exige, entre outro
- A coleta de vestígios em locais de crime compete, com primazia, ao Perito Criminal; depois
- Para a concessão do Adicional de Desempenho (ADE), considera-se satisfatório o resultado n
- Segundo a Lei nº 5.406/1969, as transgressões disciplinares classificam-se, conforme a int
- Será sempre classificada como grave a transgressão de natureza infamante e desonrosa, a of
- NÃO consta entre as penas disciplinares previstas no art. 154 da Lei nº 5.406/1969:
- Pela Lei nº 5.406/1969, as penas disciplinares devem ser aplicadas na sequência em que est
- Sobre a pena de suspensão na Lei nº 5.406/1969, é correto afirmar que:
- Considera-se abandono de cargo o não comparecimento do servidor policial por mais de trint
- Pela escala do art. 161 da Lei nº 5.406/1969, os Delegados de Polícia de carreira não incl
- Reconhecida, no julgamento da transgressão, qualquer causa justificativa, como a legítima 
- É circunstância atenuante no julgamento da transgressão disciplinar, segundo a Lei nº 5.40
- O processo administrativo é obrigatório quando a falta, por sua natureza, puder determinar
- Segundo a Lei nº 5.406/1969, o processo administrativo disciplinar deve ser concluído no p
- As Comissões Processantes Permanentes são constituídas de três servidores estáveis da Polí
- A revisão do processo administrativo disciplinar pode ser requerida a qualquer tempo e, se
- O processo administrativo disciplinar deverá ser sobrestado sempre que houver ação penal e
```

## Pedido 17: Teoria da história, historiografia e ensino de história (`historia.teoria-historiografia-ensino`, 42 tópicos de edital)

```
Matéria: historia.teoria-historiografia-ensino (Teoria da história, historiografia e ensino de história)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria da História e escolas historiográficas": 20
- "2. Fontes históricas, métodos, conceitos e crítica documental": 20
- "3. Memória, identidade e patrimônio cultural": 20
- "4. Historiografia indígena, afro-brasileira e intérpretes do Brasil": 20
- "5. BNCC de História, planejamento e didática investigativa": 20

Já existem (não repita):
- Em uma aula de teoria da História, a professora afirma que o historiador não reconstitui o
- Qual associação entre autores e a formação da Escola dos Annales está correta?
- Durante uma pesquisa, o estudante verifica papel, assinatura, proveniência e data de um of
- Na análise da proteção cultural brasileira, qual é a correspondência mais apropriada?
- Após a Lei nº 11.645/2008, a temática história e cultura afro-brasileira e indígena deve s
- Um grupo produz uma linha do tempo e compara processos que ocorreram simultaneamente em lo
- Qual alternativa melhor descreve a BNCC no planejamento do ensino de História?
- Ao usar um testemunho de antigo morador, qual conduta é mais compatível com a história ora
- Uma banca relaciona Fernand Braudel à análise histórica. A formulação correta é:
- Ao investigar mudanças na historiografia cultural, um pesquisador associa Roger Chartier s
- Um jornal antigo é usado para pesquisar a forma como a imprensa representava trabalhadores
- Uma exposição escolar apresenta três relatos incompatíveis sobre o mesmo monumento. A deci
- Sobre os intérpretes do Brasil, qual associação é apropriada?
- Uma professora escolhe fontes digitais sobre uma praça e deseja avaliar sua credibilidade.
- Ao avaliar uma produção de texto baseada em fontes, o critério mais coerente com uma apren
- Uma docente compara a descrição de um povo indígena feita por um viajante com uma narrativ
- A noção de classe social em E. P. Thompson é frequentemente mobilizada para ressaltar que:
- Uma questão afirma que a micro-história consiste em escolher temas triviais e ignorar dinâ
- Em pesquisa sobre memórias de um bairro, cinco páginas digitais repetem a mesma afirmação 
- Qual projeto exemplifica educação patrimonial participativa, conforme a concepção contempo
- Um edital afirma que o conceito de 'homem cordial' prova que brasileiros são naturalmente 
- Na análise de um documento oficial do século XIX que descreve um grupo social como incapaz
- Em uma oficina de História, estudantes interpretam um filme de ficção ambientado no passad
- Uma investigação escolar encontra um registro de patrimônio imaterial e conclui que a polí
- Na avaliação de História do Ensino Fundamental, uma banca contrapõe 'pluralidade interpret
- A historiografia pode ser entendida tanto como a produção de estudos históricos quanto com
- A Escola dos Annales defendia que somente guerras e biografias de governantes são objetos 
- O estudo de fotografias, objetos e depoimentos pode integrar a investigação histórica, des
- Uma fonte publicada por órgão público deve ser considerada imparcial e, por isso, dispensa
- A memória individual e coletiva pode ser pesquisada historicamente mesmo quando apresenta 
- No Brasil, o reconhecimento de patrimônio cultural está constitucionalmente restrito a pré
- A BNCC estabelece aprendizagens essenciais e admite que currículos e práticas escolares co
- O conceito de longa duração de Braudel elimina a necessidade de considerar eventos e conju
- A crítica externa busca estabelecer elementos como procedência e integridade do documento,
- A distinção entre fonte primária e secundária é fixa: jornais são sempre fontes secundária
- Uma periodização histórica é um recorte interpretativo e pode não servir da mesma maneira 
- O tombamento e o registro federal de bem imaterial são procedimentos idênticos, pois ambos
- A Lei nº 11.645/2008 incluiu explicitamente a história e a cultura indígena no art. 26-A d
- A história indígena é incompatível com a investigação documental, pois sociedades que valo
- A história cultural contemporânea pode estudar práticas de leitura e circulação dos textos
- O fato de três sites reproduzirem uma única notícia é prova de três confirmações independe
- A educação patrimonial dialógica pode envolver disputas de interpretação e deve considerar
- Ao ensinar história afro-brasileira, destacar a diversidade de experiências e produções in
- Uma atividade em que os alunos distinguem documento, hipótese interpretativa e lacunas da 
- No ensino de História, respeito à pluralidade obriga o professor a apresentar afirmações s
```

## Pedido 18: Parecer jurídico e responsabilidade do parecerista (`direito-administrativo.parecer-juridico-responsabilidade`, 42 tópicos de edital)

```
Matéria: direito-administrativo.parecer-juridico-responsabilidade (Parecer jurídico e responsabilidade do parecerista)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Parecer administrativo: natureza e classificação": 14
- "2. Parecer obrigatório: prazos, ausência e divergência": 22
- "3. Manifestação jurídica na contratação pública": 22
- "4. Responsabilidade do parecerista e parâmetros jurisprudenciais": 22
- "5. Condutas do administrador: seguir, divergir e não consultar": 20

Já existem (não repita):
- A autoridade solicita espontaneamente opinião jurídica antes de decidir questão para a qua
- Norma aplicável obriga a autoridade a ouvir a assessoria antes de decidir, mas não impõe q
- Em processo federal, a emissão de parecer por órgão consultivo cuja manifestação é obrigat
- Um parecer obrigatório e vinculante não foi entregue no prazo fixado. Segundo o art. 42, §
- A autoridade adota os fundamentos de parecer anterior como justificativa expressa de sua d
- A consultoria analisou a legalidade de uma contratação ao final da fase preparatória de li
- Um parecerista fundamentou sua análise, mas a autoridade pretende afastar entendimento jur
- Um parecer jurídico razoavelmente fundamentado foi seguido pela autoridade; posteriormente
- O parecer assinala uma solução jurídica, mas a autoridade entende existir alternativa lega
- Uma manifestação obrigatória não vinculante não foi emitida no prazo de 15 dias, sem exceç
- Uma administração pretende dispensar análise jurídica individual porque a contratação é de
- O setor jurídico recebeu processo licitatório e elaborou parecer com linguagem obscura, se
- Durante auditoria, surge divergência interpretativa razoável e o parecer foi posteriorment
- Um parecer prévio avaliou a versão inicial de uma minuta obrigatoriamente submetida à asse
- Após manifestação obrigatória não vinculante tempestiva, a autoridade decide em sentido co
- Em instrução de contratação direta, há referência à análise jurídica e aos pareceres técni
- Uma autoridade pretende atribuir responsabilidade a parecerista por dano elevado, embora n
- Uma autoridade incorpora parecer técnico como fundamento e depois se apura erro grosseiro 
- Uma manifestação jurídica examinou minutas em procedimento regido historicamente pelo art.
- Em contratação submetida à Lei 14.133/2021, a autoridade observou estritamente orientação 
- Uma manifestação obrigatória e vinculante é contrária à autorização pretendida. A autorida
- Um parecerista avaliou assunto novo, juridicamente complexo, expôs alternativas e adotou i
- O órgão consultivo foi obrigado a se manifestar sobre minuta A; a autoridade editou minuta
- Uma decisão incorreta foi tomada após parecer com tese plausível, mas o controle afirma qu
- Em processo de contratação regido pela Lei 14.133/2021, a autoridade dispensa consulta jur
- Sem exigência normativa de consulta, uma autoridade solicita parecer jurídico antes de dec
- Uma administração precisa ouvir consultoria jurídica por determinação normativa; por isso,
- Em processo administrativo federal sujeito à Lei 9.784/1999, o art. 42 prevê quinze dias p
- Em razão de atraso de parecer obrigatório não vinculante, o processo administrativo deve f
- A autoridade concorda expressamente com os fundamentos jurídicos de parecer anterior e os 
- Em contratação pública, o art. 53, § 5º, da Lei 14.133/2021 permite que a unidade demandan
- Uma consultoria jurídica foi obrigatoriamente consultada acerca de minuta contratual; após
- Uma autoridade segue conclusão favorável de parecer jurídico e, depois, sua decisão causa 
- Ao final da fase preparatória de uma licitação, a análise da contratação sob a ótica de le
- Uma manifestação obrigatória não vinculante foi emitida regularmente, mas a autoridade a r
- Em responsabilização de parecerista, a existência de matéria juridicamente complexa e de d
- Em processo licitatório, qualquer parecer emitido conforme o art. 53 da Lei 14.133/2021 to
- Em contratação direta sujeita à Lei 14.133/2021, a análise jurídica alcança o procedimento
- Em uma auditoria, verificou-se prejuízo expressivo após parecer jurídico. Pela disciplina 
- Uma autoridade decide com estrita observância de orientação jurídica elaborada conforme o 
- O MS 24.584/DF, relativo à aprovação jurídica de minutas no antigo regime de contratações,
- O art. 12, § 6º, do Decreto 9.830/2019 impede estender automaticamente ao decisor a respon
- Uma autorização depende de parecer obrigatório e vinculante, contrário ao pedido. Segundo 
- Parecer obrigatório não vinculante atrasado pode ser dispensado para permitir decisão nos 
- Um setor usa minuta de instrumento contratual anteriormente padronizada, mas promove alter
```

## Pedido 19: Toxicologia Geral e Regulatória (`farmacia.toxicologia-geral-regulatoria`, 42 tópicos de edital)

```
Matéria: farmacia.toxicologia-geral-regulatoria (Toxicologia Geral e Regulatória)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos e relação dose-resposta": 25
- "2. Toxicocinética e toxicodinâmica": 25
- "3. Fatores, interações e avaliação de toxicidade": 25
- "4. Ensaios in vitro: citotoxicidade e genotoxicidade": 25

Já existem (não repita):
- Toxicidade e risco são sinônimos, pois ambos dependem apenas das propriedades intrínsecas 
- Comparando duas substâncias, A com DL50 de 5 mg/kg e B com DL50 de 500 mg/kg, por via oral
- O NOAEL corresponde à maior dose testada na qual não se observa efeito adverso.
- Em um estudo crônico, o NOAEL de um aditivo foi de 50 mg/kg de peso corpóreo por dia. Apli
- Os dois componentes do fator de incerteza tradicional de 100 usado no cálculo da IDA refer
- Para substâncias genotóxicas e carcinogênicas, admite-se em geral que pode não haver dose 
- No Sistema Globalmente Harmonizado (GHS), a categoria 1 de toxicidade aguda corresponde às
- As fases da intoxicação, em sequência, são:
- Substâncias lipossolúveis e na forma não ionizada tendem a atravessar as membranas biológi
- Um ácido fraco ingerido por via oral tende a estar predominantemente na forma:
- Na distribuição de um xenobiótico, a fração ligada às proteínas plasmáticas é a responsáve
- Organoclorados persistentes, por sua alta lipossolubilidade, tendem a se acumular principa
- As reações de oxidação mediadas pelo citocromo P450 são exemplos típicos de reações de:
- São reações de conjugação da fase II da biotransformação a glicuronidação, a sulfatação e 
- A biotransformação sempre reduz a toxicidade do composto original.
- O ciclo êntero-hepático tende a:
- Os inseticidas organofosforados exercem sua toxicidade principalmente pela:
- A toxicodinâmica estuda o que o organismo faz com o agente tóxico, como sua absorção e exc
- A principal via de excreção da maioria dos xenobióticos e de seus metabólitos hidrossolúve
- Quando uma substância sem efeito tóxico próprio aumenta a toxicidade de outra, ocorre:
- No sinergismo, o efeito combinado de duas substâncias é maior que a soma de seus efeitos i
- Polimorfismos genéticos em enzimas de biotransformação podem explicar diferenças individua
- A sequência correta das etapas da avaliação de risco é:
- Os estudos de toxicidade de doses repetidas, como os subcrônicos e crônicos, servem, entre
- A avaliação da capacidade de uma substância de causar malformações no embrião ou feto inte
- As aflatoxinas são micotoxinas produzidas por fungos do gênero Aspergillus e são reconheci
- O limite máximo de resíduo (LMR) de agrotóxicos em alimentos:
- Recém-nascidos podem ser mais suscetíveis a certos agentes tóxicos por terem sistemas de b
- No ensaio de citotoxicidade pelo MTT, avalia-se:
- A liberação da enzima lactato desidrogenase (LDH) no meio de cultura indica perda da integ
- O teste de Ames detecta, principalmente:
- A fração S9 é adicionada ao teste de Ames para simular a ativação metabólica de substância
- O ensaio do micronúcleo é utilizado para detectar:
- O ensaio cometa detecta quebras na fita de DNA em células individuais, observadas pela mig
- Genotoxicidade e mutagenicidade são conceitos idênticos, pois todo dano ao DNA resulta em 
- A avaliação de genotoxicidade de uma substância costuma empregar uma bateria de testes por
- Os chamados 3Rs, que orientam o uso ético de animais em experimentação, são:
- Os ensaios in vitro reproduzem integralmente a toxicocinética do organismo vivo, dispensan
- O monóxido de carbono exerce sua toxicidade principalmente ao:
- O índice terapêutico é a razão entre a dose tóxica e a dose eficaz, e quanto maior esse ín
```

## Pedido 20: Constituição de Minas Gerais: controle da administração, Tribunal de Contas e Poder Executivo (`minas-gerais.constituicao-controle-tce-executivo`, 42 tópicos de edital)

```
Matéria: minas-gerais.constituicao-controle-tce-executivo (Constituição de Minas Gerais: controle da administração, Tribunal de Contas e Poder Executivo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Controle da administração e fiscalização": 34
- "2. Tribunal de Contas do Estado": 33
- "3. Poder Executivo": 33

Já existem (não repita):
- A Constituição do Estado de Minas Gerais afirma expressamente que a sociedade tem direito 
- Segundo a Constituição Mineira, o controle direto dos atos da administração é exercido:
- É direito da sociedade, segundo a Constituição Mineira, manter-se informada de ato ou omis
- Os Poderes do Estado de Minas Gerais, o Tribunal de Contas e o Ministério Público devem di
- Pela Constituição Mineira, a fiscalização exercida sobre atos geradores de receita ou desp
- Compete privativamente à Assembleia Legislativa de Minas Gerais, quanto às contas do Gover
- O Tribunal de Contas do Estado de Minas Gerais deve emitir parecer prévio sobre as contas 
- Cabe ao Tribunal de Contas de Minas Gerais apreciar, para fins de registro, a legalidade d
- Apurada ilegalidade em contrato celebrado pelo Estado, o Tribunal de Contas de Minas Gerai
- Se a Assembleia Legislativa não efetivar as medidas cabíveis em relação a contrato impugna
- As decisões do Tribunal de Contas de Minas Gerais de que resulte imputação de débito ou mu
- Entre as competências do Tribunal de Contas previstas no art. 76 da Constituição Mineira, 
- A Constituição Mineira determina que o Tribunal de Contas, no exercício de suas competênci
- O Tribunal de Contas do Estado de Minas Gerais é composto de:
- Para ser Conselheiro do Tribunal de Contas de Minas Gerais, exige-se, entre outros requisi
- A regra original da Constituição Mineira, segundo a qual dois Conselheiros seriam nomeados
- O Conselheiro do Tribunal de Contas de Minas Gerais tem as mesmas garantias, prerrogativas
- Ocorrida a vacância de cargo de Auditor do Tribunal de Contas de Minas Gerais, o edital do
- O Procurador-Geral do Ministério Público junto ao Tribunal de Contas de Minas Gerais é nom
- O Governador e o Vice-Governador de Minas Gerais são eleitos simultaneamente para mandato 
- Em caso de impedimento do Governador e do Vice-Governador de Minas Gerais, serão chamados 
- Vagando os cargos de Governador e Vice-Governador de Minas Gerais nos últimos dois anos do
- Se, decorridos dez dias da data fixada para a posse, o Governador ou o Vice-Governador ele
- Sem autorização da Assembleia Legislativa, o Governador de Minas Gerais não pode ausentar-
- Pela redação atual da Constituição Mineira, o Governador deve residir obrigatoriamente na 
- Compete privativamente ao Governador do Estado de Minas Gerais, entre outras atribuições:
- O Governador de Minas Gerais deve prestar anualmente à Assembleia Legislativa as contas re
- Entre as atribuições privativas do Governador de Minas Gerais, incluem-se:
- A Constituição Mineira permite ao Governador relevar, atenuar ou anular penalidades admini
- Nos crimes comuns, o Governador do Estado de Minas Gerais é processado e julgado:
- Segundo o entendimento do STF aplicado à Constituição Mineira, o recebimento de denúncia c
- São crimes de responsabilidade do Governador, segundo a Constituição Mineira, os atos que 
- O Secretário de Estado, em Minas Gerais, é escolhido entre brasileiros maiores de vinte e 
- Perde o mandato o Governador de Minas Gerais que assumir outro cargo ou função na administ
- A Assembleia Legislativa de Minas Gerais procede à tomada de contas do Governador quando e
- Os Auditores do Tribunal de Contas de Minas Gerais, quando em substituição a Conselheiro, 
- As disponibilidades de caixa do Estado de Minas Gerais e de seus órgãos e entidades devem 
- Segundo a Constituição Mineira, deve prestar contas a pessoa física ou jurídica que:
- O Tribunal de Contas de Minas Gerais encaminha relatórios de suas atividades à Assembleia 
- Pela Constituição Mineira, os Conselheiros e os Auditores do Tribunal de Contas são nomead
```

## Pedido 21: Fertilidade do solo, adubação, irrigação e drenagem (`agronomia.fertilidade-irrigacao-drenagem`, 41 tópicos de edital)

```
Matéria: agronomia.fertilidade-irrigacao-drenagem (Fertilidade do solo, adubação, irrigação e drenagem)
Capítulos e quantas questões quero em cada um (total 100):
- "Química, fertilidade e diagnóstico do solo": 17
- "Calagem, corretivos e manejo da matéria orgânica": 17
- "Adubação mineral, orgânica e recomendação de fertilizantes": 17
- "Métodos, manejo e dimensionamento da irrigação": 17
- "Drenagem, salinidade e conservação do solo e da água": 16
- "Energia na agropecuária, sustentabilidade e problemas integradores": 16

Já existem (não repita):
- No cálculo de saturação por bases, usa-se usualmente a soma de bases dividida pela CTC pot
- O pH medido em água indica diretamente toda a quantidade de acidez potencial existente no 
- Solos arenosos pobres em matéria orgânica geralmente exigem atenção ao risco de perdas de 
- O fósforo, por apresentar grande mobilidade por fluxo de massa na maioria dos solos brasil
- Um laudo apresenta SB = 3 cmolc/dm³ e T = 6 cmolc/dm³. Qual é a saturação por bases?
- Qual combinação representa corretamente, na notação usual, a CTC efetiva e a saturação por
- Um técnico encontra amarelecimento em folhas velhas, crescimento reduzido e resultado quím
- Em qual situação a representatividade da amostragem de solo é mais comprometida?
- Um calcário com PRNT de 80% normalmente exige, para a mesma necessidade expressa em base P
- O gesso agrícola e o calcário neutralizam a acidez superficial do solo por mecanismos e ef
- A adição de resíduos com elevada relação carbono/nitrogênio pode causar imobilização tempo
- Um biossólido de origem conhecida pode ser aplicado sem análise de composição ou qualidade
- Uma recomendação hipotética, já expressa para PRNT 100%, indica 3,0 t/ha de calcário. Mant
- Qual ação é mais coerente com a função da matéria orgânica na qualidade do solo?
- Qual afirmação diferencia adequadamente calcário calcítico, dolomítico e gesso?
- Ao usar esterco em um planejamento de adubação, qual informação é indispensável para estim
- Em uma fórmula comercial 05–20–10, o número 20 corresponde convencionalmente ao percentual
- O parcelamento de fertilizantes nitrogenados pode contribuir para sincronizar a oferta com
- A fertirrigação dispensa controle de compatibilidade entre fertilizantes, pois qualquer pr
- Fósforo transportado com sedimentos e nitrogênio na forma de nitrato lixiviado são exemplo
- Uma área recebe 200 kg/ha de formulação 10–10–20. Quantos quilogramas por hectare de K₂O e
- Em um sistema de gotejamento com fertirrigação, qual conjunto operacional reduz risco de e
- Um técnico quer melhorar eficiência do fósforo em solo com elevada sorção. Qual estratégia
- Qual prática mais diretamente reduz o risco de perdas por aplicação excessiva de adubo org
- A demanda de irrigação de uma cultura pode ser estimada a partir da evapotranspiração de r
- A aplicação de 1 mm de água sobre uma área de 1 ha corresponde a 10 m³ de volume.
- Quanto menor for a eficiência de aplicação de um sistema, menor deve ser a lâmina bruta pa
- Tensiômetros monitoram o potencial matricial da água no solo e não fornecem diretamente a 
- Em uma área de 2 ha, a lâmina líquida é 18 mm e a eficiência de aplicação assumida é 75%. 
- Qual alternativa descreve uma limitação operacional típica do gotejamento?
- Uma irrigação exige volume de 600 m³ e a instalação entrega vazão efetiva constante de 50 
- Na escolha entre aspersão, superfície e irrigação localizada, qual critério deve orientar 
- O principal objetivo da drenagem agrícola em solos encharcados inclui restabelecer condiçõ
- A salinidade é sinônimo de sodicidade, pois os dois termos indicam exclusivamente excesso 
- A lavagem de sais em área irrigada depende de saída adequada para a água drenada e de aval
- O cultivo em nível e o terraceamento são exatamente a mesma prática, pois ambos consistem 
- Em uma baixada agrícola, o lençol freático permanece próximo à superfície, e raízes aprese
- Qual indicador se relaciona mais diretamente à avaliação da salinidade da solução do solo?
- Uma encosta com enxurradas erosivas apresenta sulcos crescentes após chuvas fortes. Qual c
- Em uma área com excesso de sódio trocável e infiltração reduzida, qual explicação é mais c
- No cálculo da potência hidráulica de uma bomba, aumentar a altura manométrica com vazão e 
- Uma bomba que absorve potência elétrica constante de 6 kW durante 4 horas consome aproxima
- A altura manométrica total de um sistema de irrigação corresponde sempre apenas à diferenç
- Reduzir perdas no talhão equivale necessariamente a reduzir no mesmo volume o consumo de á
- Uma bomba utiliza 5 kW de potência elétrica média durante 6 horas. Qual é o consumo estima
- Considere ρ = 1.000 kg/m³, g = 10 m/s², Q = 0,01 m³/s e H = 20 m. Qual potência hidráulica
- Um projeto requer 360 m³ de água por dia e dispõe de 9 horas de bombeamento. Qual vazão mé
- Qual intervenção tende a melhorar simultaneamente o desempenho hidráulico e energético da 
```

## Pedido 22: Liquidação de sentença trabalhista (`processo-trabalho.liquidacao-sentenca`, 41 tópicos de edital)

```
Matéria: processo-trabalho.liquidacao-sentenca (Liquidação de sentença trabalhista)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Função da liquidação, título judicial e limites do art. 879": 34
- "2. Liquidação por cálculos, arbitramento e artigos": 33
- "3. Construção da conta: bases, parcelas, deduções e erros": 11
- "4. Contraditório e impugnação fundamentada aos cálculos": 11
- "5. Atualização do crédito: ADCs 58 e 59, modulação e Lei 14.905/2024": 11

Já existem (não repita):
- Uma sentença reconheceu diferenças salariais de seis meses, mas não fixou o total a pagar.
- Na liquidação, o credor quer acrescentar verba expressamente rejeitada na sentença definit
- O título fixou 10 horas extras em cada um de quatro meses, salário-hora de R$ 20 e adicion
- Uma decisão permite obter o crédito por operações matemáticas sobre cartões e recibos já j
- A conta foi elaborada e tornou líquida a condenação trabalhista. Qual prazo o art. 879, § 
- A parte afirma apenas 'os valores da conta estão todos errados', sem identificar rubricas,
- Após as ADCs 58 e 59, a conta usa a SELIC integral durante período judicial anterior a 30/
- O título concede determinado adicional sobre 30 horas e o cálculo já dispõe do valor salar
- A sentença deferiu indenização por deterioração de equipamento, mas o montante depende de 
- Sentença deferiu ressarcimento por viagens no período estabelecido, mas é preciso comprova
- A liquidação envolve centenas de competências e cálculo sofisticado de remuneração variáve
- Uma planilha contém diferenças salariais de R$ 8.000, reflexos de R$ 2.000 e atualização d
- A sentença de conhecimento foi publicada já com planilha completa e valor condenatório líq
- Uma liquidação é aberta com base em sentença ilíquida. Após manifestação das partes, o pro
- Um processo não possui título definitivo com índices expressamente fixados, e o crédito te
- A conta de condenação ilíquida foi preparada, e o prazo de oito dias para impugnação começ
- Uma condenação manda pagar apenas o adicional de 50% sobre 32 horas cujas horas normais já
- Uma condenação reconheceu diferença mensal de R$ 300 por seis meses e um reflexo especific
- Sentença deferiu indenização cujo valor exige prova de quantas ocorrências se verificaram 
- Em demanda com sentença ilíquida, a parte foi intimada para examinar a conta de R$ 25.000.
- Uma planilha refez a atualização de crédito já integralmente pago, em tempo e modo oportun
- Há dois títulos definitivos: o primeiro fixou expressamente TR e juros de 1% ao mês; o seg
- Uma conta abrange créditos trabalhistas já judicializados entre 2023 e dezembro de 2024, s
- Após 30/08/2024, o cálculo de juros legais pelo art. 406 do Código Civil resulta negativam
- Sentença fixou diferenças mensais de R$ 200, R$ 350 e R$ 150 em três competências. A plani
- A sentença trabalhista ilíquida deve ser submetida à liquidação prévia, que pode ser reali
- Se surgir prova favorável na liquidação, a parte pode incluir verba expressamente rejeitad
- Quando o título define salários, adicionais e horas e faltam apenas operações matemáticas,
- O prazo do art. 879, § 2º, para as partes impugnarem conta tornada líquida é de dez dias s
- A parte que impugna valores da conta deve indicar fundamentadamente os itens e os valores 
- A tese das ADCs 58 e 59 manteve a TR como índice geral e exclusivo de correção de todo cré
- A nomeação de perito para elaborar cálculos complexos é facultada pelo art. 879, § 6º, sem
- A liquidação por artigos, chamada procedimento comum no art. 509, II, do CPC, só é cabível
- Se a sentença reconheceu apenas o adicional de 50% sobre horas normais já pagas, a conta n
- Depois de proferida sentença de conhecimento já líquida, os cálculos integrantes da decisã
- Ao abrir prazo comum de oito dias para impugnar cálculo, o juízo deve observar a contagem 
- A elaboração da conta elimina a necessidade de intimação prévia das partes para apresentaç
- A modulação das ADCs 58 e 59 protege pagamentos já realizados em tempo e modo oportunos se
- Na fase judicial de 2023, a utilização integral da SELIC autorizada pelas ADCs 58 e 59 per
- A partir de 30/08/2024, conforme aplicação articulada da Lei 14.905/2024, a atualização de
- Se a taxa legal de juros resultante da diferença entre SELIC e IPCA for negativa em determ
- Uma condenação líquida de conhecimento já inclui valor e memória definitivos. A ausência d
- A expressão 'juros e correção nos termos da lei' em sentença transitada em julgado equival
- Em liquidação por artigos, a prova de número de eventos abrangidos pelo direito já concedi
- Conforme o art. 879 da CLT, o mesmo prazo comum de oito dias do § 2º deve necessariamente 
```

## Pedido 23: Metodologia da pesquisa em saúde (`saude.metodologia-pesquisa-saude`, 41 tópicos de edital)

```
Matéria: saude.metodologia-pesquisa-saude (Metodologia da pesquisa em saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, perguntas de pesquisa e delineamentos": 20
- "Amostragem, instrumentos e coleta de dados": 20
- "Organização, análise e interpretação dos dados": 20
- "Ética em pesquisa: participantes, CEP e governança": 20
- "Revisões sistemáticas, redação científica e normalização": 20

Já existem (não repita):
- Uma equipe pretende compreender como mulheres atendidas em uma unidade de saúde atribuem s
- Qual alternativa expressa de modo mais preciso a finalidade da randomização em um ensaio?
- Uma avaliação de programa mede inicialmente a adesão e, depois, entrevista usuários para e
- Em relatório, pesquisadores descrevem um estudo sem alocação aleatória que compara indicad
- Uma enfermeira afirma: “Como o estudo medirá percepções com uma escala numérica, não é pre
- Em uma investigação de experiência de cuidado, participantes são recrutados propositalment
- Uma pesquisadora recruta pacientes que aparecem durante o plantão e os chama de amostra pr
- Ao adaptar uma escala de cuidado elaborada em outro país, a melhor providência metodológic
- Uma pesquisa qualitativa encerra entrevistas após justificar que novos depoimentos passara
- Em um banco extraído de prontuários, cada pessoa tem várias consultas. A pesquisadora cont
- Ao analisar entrevistas sobre barreiras no acolhimento, a equipe cria códigos, agrupa sent
- Uma planilha apresenta valores de idade iguais a 999, empregados no serviço para indicar i
- Em estudo de métodos mistos, uma tabela coloca lado a lado achados numéricos, falas e a in
- Em uma análise qualitativa, o pesquisador descarta entrevista que contraria a hipótese cen
- Um resultado mostra associação entre uma variável do cuidado e satisfação, mas o desenho n
- Qual é a composição institucional central do Sinep prevista no Decreto nº 12.651/2025?
- Um diretor autoriza a coleta de entrevistas na unidade, mas o projeto requer apreciação ét
- Uma equipe substitui nomes por números, mas guarda a chave que relaciona os números aos pa
- Assinale a afirmação metodologicamente e eticamente mais correta sobre consentimento em pe
- Uma banca apresenta a seguinte lista de correspondências. Qual está correta?
- Uma equipe conclui revisão sistemática sem somar quantitativamente os resultados por heter
- Na busca de revisão sistemática, qual uso de operadores tende a recuperar sinônimos de um 
- Uma publicação biomédica usa citações numeradas na ordem de aparecimento e lista de referê
- Uma revisão escolheu apenas estudos com efeitos favoráveis após conhecer os resultados, em
- Na pesquisa em saúde, o método deve ser escolhido a partir da pergunta e dos objetivos, e 
- Por definição, a pesquisa qualitativa carece de critérios de rigor, pois sua matéria-prima
- Em ensaio randomizado, a ocultação da sequência de alocação e o cegamento são conceitos di
- Um estudo que aplica um questionário estruturado e realiza entrevistas, sem nenhuma integr
- A escolha de pacientes por ordem de chegada em uma unidade pode ser não probabilística, me
- Em estudos qualitativos, a saturação deve ser sempre declarada quando forem realizadas doz
- Uma escala validada em determinado país e faixa etária deve ser automaticamente aceita com
- No planejamento amostral, população-alvo, população acessível, critérios de elegibilidade 
- Identificar campos duplicados, distinguir valores ausentes de valores válidos e manter dic
- Na análise temática, contar quantas vezes um termo aparece constitui por si só interpretaç
- Reflexividade implica reconhecer como a posição e os pressupostos do pesquisador podem inf
- Quando resultados qualitativos divergem de resultados quantitativos em um desenho misto, o
- A Lei nº 14.874/2024 instituiu o Sistema Nacional de Ética em Pesquisa com Seres Humanos, 
- A autorização administrativa do diretor de um hospital substitui automaticamente a aprovaç
- Dados clínicos identificáveis extraídos de prontuários podem gerar risco à privacidade, me
- A existência de uma hipótese legal de tratamento de dados pessoais na LGPD extingue, por s
- Uma revisão sistemática pode apresentar síntese narrativa estruturada sem metanálise quand
- O fluxograma PRISMA registra etapas de identificação e seleção de estudos, mas não substit
- Nas referências biomédicas em estilo Vancouver, o DOI de um artigo, quando disponível, sig
- Em um artigo com estrutura IMRaD, discutir limitações e comparar achados à literatura pert
```

## Pedido 24: Meteorologia aeronáutica (`aviacao.meteorologia-aeronautica`, 41 tópicos de edital)

```
Matéria: aviacao.meteorologia-aeronautica (Meteorologia aeronáutica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Atmosfera, pressão, ventos, climatologia e sistemas frontais": 20
- "2. METAR, SPECI, TAF e decodificação operacional": 16
- "3. SIGMET, AIRMET, cartas de altitude e serviços meteorológicos": 20
- "4. Gelo, turbulência, tesoura de vento e fenômenos de risco": 28
- "5. Integração meteorológica na decisão antes e durante o voo": 16

Já existem (não repita):
- Um observador recebeu `METAR SBXX 101200Z 09008KT 9999 SCT020 24/17 Q1016`. Qual leitura p
- Um aeródromo apresenta `SCT020` como única camada de nuvens reportada. Qual descrição é ad
- Durante o planejamento, a equipe precisa da previsão meteorológica de aeródromo válida par
- Uma observação rotineira foi seguida, antes da próxima emissão programada, por deterioraçã
- O grupo `BKN008` é apresentado na aproximação, sem camada inferior mais extensa. Que teto 
- Uma carta informa vento `270/40` na altitude planejada. Se a aeronave segue rumo verdadeir
- Um mapa sinótico mostra isóbaras muito próximas em determinada região, enquanto outro exib
- A equipe encontra previsão de baixa pressão associada a convergência em superfície e movim
- Uma aeronave aproxima-se de extensa área com trovoadas organizadas e risco significativo e
- Um TAF contém `TEMPO 1016/1019 3000 TSRA`. Como interpretar o intervalo?
- A superfície de um aeródromo está com alta pressão, céu claro e vento fraco durante a noit
- Durante voo, há ar a −8 °C, nuvens com água líquida super-resfriada e início de acumulação
- Uma previsão de área em baixos níveis aponta turbulência moderada, sem alcance dos critéri
- Uma carta sinaliza vento de oeste intenso perto da corrente de jato; a imagem de satélite 
- Em uma aproximação com células convectivas próximas, há relato de vento de proa inicial se
- Em um informe de gelo, o relato descreve massa densa e relativamente transparente sobre o 
- Um boletim apresenta `Q1009`; outro apresenta `Q1018`. O primeiro aeródromo também registr
- No mesmo dia, METAR às 12 UTC indica 9999 e SCT025, mas TAF prevê `TEMPO 1018/1021 2000 TS
- Em relatório de pista, existe `18012G22KT` e `BKN008`; em carta de vento, registra-se 180/
- Durante análise de carta no Hemisfério Sul, um vórtice de baixa pressão apresenta circulaç
- Há SIGMET válido para gelo severo em FL100–FL200 de uma FIR. Uma aeronave voará em FL080 m
- O responsável pelo planejamento compara `TAF ... 1012/1112` com `METAR ... 101200Z`. Qual 
- Ao verificar região montanhosa, um mapa indica vento forte transversal à serra, nuvens len
- A leitura de um boletim revela CAVOK, mas uma carta de altitude e informações de rota apon
- Uma previsão regional indica passagem de frente fria ativa; adiante, observa-se sequência 
- Em uma manhã de voo, o grupo `101200Z` de um METAR indica dia 10 e hora 12 UTC, não necess
- Em um relatório de vento `36010KT`, a direção 360° indica que o vento se dirige para o nor
- Diante de mudança relevante de visibilidade entre observações rotineiras, SPECI é a previs
- Ao visualizar `Q1013` em METAR, o planejador pode identificar QNH de 1.013 hPa, correspond
- Uma camada OVC015, isolada como a mais baixa a definir teto, representa céu encoberto cuja
- A presença de trovoadas organizadas em rota é descrita preferencialmente apenas pelo METAR
- Uma equipe analisa uma frente quente e conclui que sua passagem não pode reduzir teto nem 
- Em aproximação, nevoeiro radiativo formado durante noite de vento fraco e céu claro pode c
- Uma aeronave encontra nuvem com temperatura do ar negativa, porém nenhuma água líquida sup
- O termo `BECMG` em TAF é compatível com mudança gradual das condições predominantes ao lon
- Quando a carta de altitude registra vento de oeste a 40 nós, a carta demonstra, sem necess
- Em condições de chuva congelante, gotas líquidas abaixo do ponto de congelamento podem con
- Um SIGMET informa gelo severo na FIR entre FL100 e FL200. A validade e o alcance vertical 
- A previsão `TEMPO 1018/1021 2000 TSRA` comprova que a trovoada ocorrerá ininterruptamente 
- Um METAR favorável ao meio-dia não dispensa consultar TAF, cartas e avisos para uma chegad
- Ao encontrar CAT em rota sem chuva, o radar meteorológico de precipitação necessariamente 
- O operador dispõe de AIRMET válido para baixos níveis e de aviso SIGMET em altitude mais e
- Uma aeronave com rumo verdadeiro 270° recebe componente aproximadamente de proa de vento v
- Após um relato de microburst na aproximação, o aumento inicial de velocidade indicada perm
- Uma carta indica vento e temperatura previstos para um nível e horário específicos. Sua ap
```

## Pedido 25: Constituição do Estado de Alagoas (`alagoas.constituicao-estadual`, 40 tópicos de edital)

```
Matéria: alagoas.constituicao-estadual (Constituição do Estado de Alagoas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estado, municípios e administração pública": 20
- "2. Servidores civis e militares": 20
- "3. Poder Legislativo": 20
- "4. Fiscalização, Tribunal de Contas e controle interno": 20
- "5. Poder Executivo e segurança pública": 20

Já existem (não repita):
- Segundo a Constituição do Estado de Alagoas, a Constituição poderá ser emendada mediante p
- Na Constituição de Alagoas, a proposta de emenda constitucional é discutida e votada em do
- A Constituição alagoana não poderá ser emendada na vigência de intervenção federal, de est
- Segundo o texto atualizado da Constituição de Alagoas, a iniciativa das leis complementare
- São de iniciativa privativa do Governador do Estado de Alagoas as leis que fixem ou modifi
- Quanto às emendas parlamentares que aumentem a despesa prevista, a Constituição de Alagoas
- Se o Governador de Alagoas solicitar urgência para projeto de sua iniciativa e a Assemblei
- Pela Constituição de Alagoas, o Governador que considerar o projeto inconstitucional ou co
- Sobre o veto na Constituição de Alagoas, assinale a alternativa correta.
- A matéria de projeto de lei rejeitado somente poderá constituir objeto de novo projeto, na
- Sobre as leis delegadas na Constituição de Alagoas, é correto afirmar:
- Na Constituição de Alagoas, as leis complementares são aprovadas por:
- Segundo a Constituição de Alagoas, o Deputado Estadual, desde a expedição do diploma, será
- Na Constituição de Alagoas, a Mesa da Assembleia pode encaminhar pedidos escritos de infor
- Compete exclusivamente à Assembleia Legislativa de Alagoas autorizar, por dois terços de s
- Se o Governador de Alagoas não apresentar as contas anuais à Assembleia dentro de sessenta
- Segundo o texto da Constituição de Alagoas, a sessão legislativa não será interrompida sem
- Perderá o mandato, segundo a Constituição de Alagoas, o Deputado que deixar de comparecer,
- Segundo a Constituição de Alagoas, o controle externo, a cargo da Assembleia Legislativa, 
- Constatada irregularidade nos atos de gestão dos recursos públicos, o Tribunal de Contas d
- O Tribunal de Contas do Estado de Alagoas é integrado por:
- Para ser nomeado Conselheiro do Tribunal de Contas de Alagoas, exige-se ter mais de trinta
- Sobre os auditores do Tribunal de Contas de Alagoas, assinale a correta.
- O Tribunal de Contas de Alagoas aprecia as contas prestadas anualmente pelo Governador, re
- Ao apreciar, para fins de registro, a legalidade dos atos de admissão de pessoal, o Tribun
- Formalizada denúncia de cidadão ao Tribunal de Contas de Alagoas, o Tribunal promoverá sua
- Pela redação atual da Constituição de Alagoas, é vedada a recondução do Presidente e do Vi
- Os responsáveis pelo controle interno em Alagoas, ao tomarem conhecimento de irregularidad
- Impedidos o Governador e o Vice-Governador de Alagoas, serão sucessivamente chamados ao ex
- Ocorrendo a vacância dos cargos de Governador e Vice-Governador de Alagoas nos últimos doi
- O art. 105 da Constituição de Alagoas veda a reeleição do Governador para o período subseq
- Perderá o mandato o Governador de Alagoas que se ausentar do território estadual por perío
- Se, decorridos dez dias da data fixada para a posse, o Governador ou o Vice-Governador de 
- Segundo a Constituição de Alagoas, compete ao Governador nomear, após aprovação pela Assem
- Sobre a responsabilidade do Governador de Alagoas, considere o texto da Constituição Estad
- Os Secretários de Estado de Alagoas serão escolhidos dentre brasileiros natos, maiores de 
- Sobre o Conselho do Estado de Alagoas, assinale a alternativa correta.
- São responsáveis pela segurança pública na Constituição de Alagoas, respeitada a competênc
- Segundo a Constituição de Alagoas, à Polícia Civil, dirigida por delegado de polícia, incu
- Na Constituição de Alagoas, as guardas municipais, destinadas à proteção de bens, serviços
- Em caso de dupla vacância dos cargos de Prefeito e Vice-Prefeito nos dois últimos anos do 
- Segundo a Constituição de Alagoas, o parecer prévio do Tribunal de Contas sobre as contas 
- Sobre a intervenção do Estado de Alagoas em seus municípios, a Constituição Estadual prevê
- A Constituição de Alagoas estabelece que a publicação prévia de atos e contratos no órgão 
- Um servidor civil de Alagoas cumpriu dois anos de efetivo exercício em cargo de provimento
- Segundo a Constituição de Alagoas, o militar estadual em atividade que aceitar cargo públi
- Na Constituição de Alagoas, as patentes dos oficiais da Polícia Militar, com as prerrogati
- Pela Constituição de Alagoas, o prazo de validade do concurso público será de até dois ano
```

## Pedido 26: Desburocratização: Lei 13.726/2018 e Decreto 9.094/2017 (`administracao-publica.desburocratizacao-lei-13726`, 40 tópicos de edital)

```
Matéria: administracao-publica.desburocratizacao-lei-13726 (Desburocratização: Lei 13.726/2018 e Decreto 9.094/2017)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei 13.726/2018: objeto e dispensas": 25
- "2. Lei 13.726/2018: comunicação, grupos de trabalho e Selo": 25
- "3. Decreto 9.094/2017: diretrizes e racionalização de exigências": 25
- "4. Decreto 9.094/2017: firma, autenticação, Carta de Serviços e sanções": 25

Já existem (não repita):
- A Lei 13.726/2018 racionaliza atos e procedimentos administrativos:
- Pela Lei 13.726/2018, devem ser suprimidas ou simplificadas as formalidades desnecessárias
- Na relação dos órgãos públicos com o cidadão, a Lei 13.726/2018 dispensa o reconhecimento 
- A Lei 13.726/2018 dispensa a autenticação de cópia de documento, cabendo ao agente adminis
- Segundo a Lei 13.726/2018, a apresentação de título de eleitor:
- Pela Lei 13.726/2018, dispensa-se a apresentação de autorização com firma reconhecida para
- Pode substituir a certidão de nascimento, segundo a Lei 13.726/2018, EXCETO:
- É vedada, pela Lei 13.726/2018, a exigência de prova relativa a fato que já houver sido co
- Quando, por motivo não imputável ao solicitante, não for possível obter documento comproba
- Os órgãos de um Poder não podem exigir do cidadão certidão expedida por outro órgão do mes
- Pela Lei 13.726/2018, a comunicação entre o Poder Público e o cidadão pode ser feita por q
- Os grupos setoriais de trabalho previstos na Lei 13.726/2018 têm por objetivo:
- O Selo de Desburocratização e Simplificação é concedido por comissão formada por represent
- São critérios para a concessão do Selo de Desburocratização e Simplificação, EXCETO:
- Segundo a Lei 13.726/2018, serão premiados anualmente:
- A participação do servidor no desenvolvimento e na execução de projetos de desburocratizaç
- Os órgãos ou entidades que receberem o Selo de Desburocratização e Simplificação serão ins
- São diretrizes do Decreto 9.094/2017 nas relações dos órgãos do Executivo federal com os u
- Para o Decreto 9.094/2017, usuários dos serviços públicos são as pessoas físicas e jurídic
- Se um órgão do Executivo federal precisa de certidão que consta em base de dados oficial d
- Pelo Decreto 9.094/2017, os serviços de protocolo não podem recusar o recebimento de reque
- Após protocolizado um requerimento, o agente verifica que seu órgão é incompetente para de
- Pelo Decreto 9.094/2017, o número de inscrição no CPF é suficiente e substitutivo para a a
- Segundo o Decreto 9.094/2017, as exigências necessárias para o requerimento devem ser feit
- Para complementar informações ou solicitar esclarecimentos, o Decreto 9.094/2017 admite qu
- Pelo Decreto 9.094/2017, o reconhecimento de firma e a autenticação de cópia de documentos
- Constatada, a qualquer tempo, a falsificação de firma ou de cópia de documento, o órgão co
- A Carta de Serviços ao Usuário, prevista no Decreto 9.094/2017, tem por objetivo informar 
- Para detalhar o padrão de qualidade do atendimento, a Carta de Serviços ao Usuário deve es
- Para cada serviço prestado, a Carta de Serviços ao Usuário deve trazer informações sobre, 
- O servidor público civil que descumprir o Decreto 9.094/2017 estará sujeito às penalidades
- Os usuários dos serviços públicos que tiverem os direitos garantidos no Decreto 9.094/2017
- O Decreto 9.094/2017 revogou o Decreto 5.378/2005, que instituía o Programa Nacional de Ge
- Pelo Decreto 9.094/2017, os órgãos do Executivo federal devem utilizar pesquisa de satisfa
- Sobre a Lei 13.726/2018 e o Decreto 9.094/2017, assinale a correta.
- A Lei 13.726/2018 permite que a juntada de documento pessoal do usuário seja substituída p
- Na Lei 13.726/2018, foram vetados, entre outros, os arts. 2º e 4º.
- Um cidadão precisa comprovar sua idade perante um órgão municipal, mas só tem em mãos a ca
- Pelo Decreto 9.094/2017, a apresentação de documentos pode ser feita por cópia autenticada
- Entre as práticas de atendimento previstas no Decreto 9.094/2017, está a gratuidade dos at
```

## Pedido 27: Teoria de Voo e Sistemas de Aeronaves (Aviões e Helicópteros) (`aviacao.teoria-voo-sistemas-aeronaves`, 40 tópicos de edital)

```
Matéria: aviacao.teoria-voo-sistemas-aeronaves (Teoria de Voo e Sistemas de Aeronaves (Aviões e Helicópteros))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos: forças, estol, CG e estabilidade": 25
- "2. Voo de alta velocidade e multimotores": 25
- "3. Aerodinâmica de helicópteros": 25
- "4. Sistemas e equipamentos de aeronaves": 25

Já existem (não repita):
- O estol de uma asa ocorre quando é ultrapassado o ângulo de ataque crítico, e não necessar
- Em uma curva nivelada com maior inclinação, a velocidade de estol da aeronave:
- O arrasto induzido é maior em altas velocidades e diminui à medida que a aeronave reduz a 
- Os winglets instalados nas pontas das asas têm como finalidade principal:
- Com o centro de gravidade localizado mais à ré, dentro ou próximo do limite traseiro, a ae
- A posição do centro de gravidade costuma ser expressa em percentual da corda média aerodin
- A tendência inicial de uma aeronave de retornar ao equilíbrio após uma perturbação caracte
- Windshear é uma variação brusca do vento em direção ou velocidade, especialmente perigosa 
- O Mach crítico é o menor número Mach de voo em que:
- O enflechamento das asas e o uso de perfis supercríticos são recursos utilizados para elev
- A tendência de a aeronave picar com o aumento do número Mach, causada pelo recuo do centro
- O Dutch roll é uma oscilação acoplada de guinada e rolagem, típica de asas enflechadas, e 
- Em alguns jatos, os ailerons externos são bloqueados em alta velocidade para evitar a inve
- Os spoilers, ao serem defletidos, produzem:
- Os geradores de vórtice energizam a camada limite e retardam o descolamento do escoamento.
- As wing fences, em asas enflechadas, têm a finalidade de:
- A velocidade mínima de controle (VMC) de um multimotor é a menor velocidade em que se mant
- Manter a hélice do motor inoperante embandeirada contribui para reduzir a VMC, pois diminu
- No helicóptero, o efeito de solo reduz a potência necessária para pairar, porque diminui o
- A diferença de velocidade relativa entre a pá que avança e a pá que recua, no voo translac
- O fenômeno que limita a velocidade máxima à frente do helicóptero, em que a pá que recua o
- No anel de vórtice (vortex ring state), aplicar mais potência no coletivo é a forma mais e
- O anel de vórtice tende a ocorrer, em helicópteros, durante:
- Na autorrotação, o rotor principal continua girando sem potência do motor graças ao fluxo 
- No helicóptero, o comando que altera o passo de todas as pás do rotor principal igualmente
- Pela precessão giroscópica, a resposta a uma força aplicada ao disco do rotor manifesta-se
- A ressonância com o solo é uma oscilação potencialmente destrutiva que ocorre com o helicó
- A função principal do rotor de cauda em um helicóptero de rotor único é:
- A sustentação translacional efetiva melhora a eficiência do rotor quando o helicóptero gan
- Nas aeronaves, as superfícies de comando primárias responsáveis pela arfagem, pela rolagem
- O embandeiramento da hélice de um motor inoperante, em aeronaves multimotoras, reduz o arr
- Sobre os sistemas de proteção contra gelo, é correto afirmar que:
- O velocímetro mede a pressão dinâmica, isto é, a diferença entre a pressão captada pelo tu
- Em caso de despressurização em altitude elevada, a conduta típica da tripulação inclui:
- Em motores a pistão, o gelo no carburador pode se formar mesmo com temperatura externa aci
- O tipo de motor a turbina mais característico de helicópteros, que entrega potência a um e
- Nos motores a pistão aeronáuticos, a ignição costuma ser feita por magnetos duplos, indepe
- O piloto automático, além de manter atitude, altitude e rumo, pode incorporar o yaw damper
- Nas aeronaves, o ar sangrado dos motores é utilizado tipicamente pelo sistema:
- A sustentação de uma asa é proporcional, entre outros fatores, ao quadrado da velocidade d
```

## Pedido 28: Física aplicada: corrente alternada, óptica ondulatória, hidrodinâmica e metrologia (`fisica.corrente-alternada-optica-fluidos-medicao`, 40 tópicos de edital)

```
Matéria: fisica.corrente-alternada-optica-fluidos-medicao (Física aplicada: corrente alternada, óptica ondulatória, hidrodinâmica e metrologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Corrente alternada: valores, reatâncias e impedância": 17
- "2. Potência em corrente alternada e transformadores": 17
- "3. Equações de Maxwell e ondas eletromagnéticas": 17
- "4. Interferência, difração e polarização": 17
- "5. Hidrodinâmica: vazão, continuidade e Bernoulli": 16
- "6. Metrologia: dosimetria, erros e incertezas": 16

Já existem (não repita):
- Uma tensão alternada senoidal tem valor de pico de 311 V. Seu valor eficaz é, aproximadame
- Em um indutor ideal submetido a tensão alternada, a corrente fica adiantada de 90° em rela
- Ao dobrar a frequência da tensão aplicada a um capacitor ideal, sua reatância capacitiva:
- Um circuito RLC série tem R = 30 Ω, X_L = 70 Ω e X_C = 30 Ω. A impedância do circuito vale
- Na condição de ressonância de um circuito RLC série, a impedância é mínima e igual à resis
- A frequência da rede elétrica de distribuição no Brasil é de 50 Hz, a mesma adotada na mai
- Uma carga consome potência aparente de 1.000 VA com fator de potência 0,8. A potência ativ
- A potência reativa, associada à troca de energia entre a fonte e os campos de indutores e 
- Um transformador ideal tem 200 espiras no primário e 50 no secundário. Ligado a 220 V no p
- Transformadores funcionam tanto em corrente alternada quanto em corrente contínua, pois de
- A transmissão de energia elétrica em alta tensão reduz as perdas nas linhas porque diminui
- Entre as equações de Maxwell, a que expressa a inexistência de monopolos magnéticos é a:
- A contribuição original de Maxwell à lei de Ampère foi o termo da corrente de deslocamento
- Sobre as ondas eletromagnéticas, é correto afirmar que:
- A partir de suas equações, Maxwell obteve para a velocidade das ondas eletromagnéticas no 
- Na sequência do espectro eletromagnético, os raios X têm frequência maior que a da luz vis
- No experimento de dupla fenda de Young, o espaçamento entre as franjas claras na tela:
- A difração torna-se mais evidente quando a dimensão da abertura ou do obstáculo é comparáv
- O fenômeno que evidencia que a luz é uma onda transversal, e que não ocorre com o som no a
- Luz não polarizada de intensidade I₀ atravessa um polarizador e, em seguida, um analisador
- Quando a luz incide em uma superfície no ângulo de Brewster, a luz refletida sai totalment
- As cores observadas em bolhas de sabão e em finas camadas de óleo sobre a água são explica
- Água escoa por um tubo horizontal cuja área se reduz de 20 cm² para 5 cm². Se a velocidade
- Pela equação de Bernoulli, em um escoamento horizontal de fluido ideal, a pressão é menor 
- A equação de Bernoulli representa, para fluidos ideais, a aplicação do princípio da:
- Um reservatório aberto tem um pequeno orifício 5 m abaixo da superfície livre da água. Ado
- A equação da continuidade A₁v₁ = A₂v₂ aplica-se a fluidos compressíveis em qualquer regime
- No Sistema Internacional, a unidade de dose absorvida de radiação, equivalente a um joule 
- O becquerel, unidade de atividade de uma fonte radioativa, corresponde a uma desintegração
- Ao dobrar a distância a uma fonte radioativa pontual, a intensidade da radiação recebida, 
- Um conjunto de medições muito próximas entre si, mas afastadas do valor verdadeiro, caract
- O erro sistemático pode ser eliminado simplesmente aumentando o número de repetições da me
- Em uma série de medições com desvio padrão amostral s, para reduzir à metade a incerteza p
- A incerteza expandida é obtida multiplicando-se a incerteza combinada pelo fator de abrang
- As componentes de incerteza avaliadas a partir de certificados de calibração, especificaçõ
- Para corrigir o baixo fator de potência de uma instalação com muitos motores, que são carg
- Em circuitos de corrente alternada, a impedância de um circuito RLC série é a soma aritmét
- A lei de Faraday, uma das equações de Maxwell, estabelece que um campo magnético variável 
- No medidor de Venturi, a pressão do fluido na seção estreita do tubo é maior que na seção 
- O sievert, unidade de dose equivalente e de dose efetiva, leva em conta o tipo de radiação
```

## Pedido 29: Parasitologia clínica (`laboratorio.parasitologia-clinica`, 39 tópicos de edital)

```
Matéria: laboratorio.parasitologia-clinica (Parasitologia clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, hospedeiros e ciclos biológicos": 20
- "Protozoários intestinais e identificação laboratorial": 20
- "Helmintos humanos e marcadores morfológicos": 20
- "Exame parasitológico de fezes: métodos, coleta e controle": 20
- "Parasitos sanguíneos e teciduais: diagnóstico e interpretação": 20

Já existem (não repita):
- Na giardíase, a ingestão de cistos viáveis pode iniciar a infecção humana por via fecal-or
- O método de Hoffman, Pons e Janer baseia-se na flutuação de ovos e cistos em sulfato de zi
- O encontro de ovos de Trichuris trichiura com tampões nas duas extremidades é um achado mo
- A gota espessa é menos sensível que o esfregaço delgado para detectar baixa parasitemia na
- No exame parasitológico de fezes, larvas de Strongyloides stercoralis podem ser recuperada
- A fita adesiva perianal é geralmente mais indicada que o EPF rotineiro na investigação de 
- Um exame coproparasitológico negativo, em amostra isolada, exclui qualquer parasitose inte
- No ciclo da malária, o mosquito Anopheles é o hospedeiro definitivo, por sediar a reproduç
- Os ovos de Ancylostoma duodenale e Necator americanus encontrados nas fezes permitem, sozi
- Para pesquisa de motilidade de trofozoítos, o Lugol é preferível à solução salina por pres
- No método Kato-Katz, a utilização de massa fecal calibrada permite expressar ovos de helmi
- Na fase crônica da doença de Chagas, a negatividade de um exame parasitológico sanguíneo i
- A pesquisa morfológica de amastigotas de Leishmania em material de medula óssea pode demon
- Para procurar microfilárias de Wuchereria bancrofti com periodicidade noturna, o horário d
- A presença de cistos compatíveis com o complexo Entamoeba histolytica/dispar no exame dire
- A demonstração de ovos de Toxocara nas fezes humanas é o método de escolha na investigação
- Para Baermann-Moraes, a fixação prévia das fezes em formalina é recomendada porque estimul
- Ovos de Taenia saginata e Taenia solium são morfologicamente semelhantes, de modo que sua 
- Em fezes diarreicas destinadas à avaliação de trofozoítos, contar o prazo de processamento
- Um teste sorológico reagente para leishmaniose visceral pode persistir após tratamento e, 
- Em amostra fecal fresca, o laboratório deseja demonstrar a motilidade de um trofozoíto. Qu
- Uma criança apresenta suspeita de enterobíase, mas o EPF foi negativo. Qual orientação pré
- No laboratório, qual técnica é tipicamente utilizada para quantificar ovos de Schistosoma 
- Qual característica microscópica é clássica de ovos de Schistosoma mansoni nas fezes?
- Qual forma de Strongyloides stercoralis é recuperada mais caracteristicamente de fezes fre
- Um laudo de rotina identifica ovos de ancilostomídeos. Qual conclusão preserva o limite ta
- Qual alternativa representa corretamente o princípio do método de Hoffman, Pons e Janer?
- Na investigação de malária, qual método é especialmente utilizado no Brasil como pesquisa 
- Uma amostra de fezes diarreicas foi coletada há quatro horas sem conservante, embora tenha
- Qual achado costuma demandar coloração álcool-ácido resistente modificada, antígeno ou téc
- Por que uma amostra de fezes preservada previamente em formol costuma ser inadequada para 
- Um laboratório encontra cistos atribuíveis morfologicamente ao complexo Entamoeba histolyt
- Um técnico deseja separar cistos de certos protozoários de detritos fecais por centrifugaç
- Quanto à cisticercose humana por Taenia solium, qual situação representa a via relevante p
- Qual alternativa descreve adequadamente uma limitação do método Kato-Katz?
- Na investigação laboratorial da filariose linfática por Wuchereria bancrofti de periodicid
- Uma banca afirma que o achado de ovos de Taenia spp. permite diferenciar T. saginata de T.
- Na doença de Chagas, um indivíduo em fase crônica tem exame direto do sangue negativo. Qua
- Após detecção de amastigotas em aspirado de medula óssea, qual combinação representa melho
- Em uma amostra humana suspeita de toxocaríase visceral, qual justificativa explica o baixo
- Uma amostra de sangue foi colhida em tubo anticoagulado e o esfregaço foi preparado após a
- Um microscopista observa ovos alongados com espículo terminal em amostra urinária de viaja
- Na fase intestinal da estrongiloidíase, por que um EPF único pode ter baixa sensibilidade 
- Um paciente tem pesquisa sorológica reagente para leishmaniose visceral muito tempo após t
- Uma gestante tem IgM anti-Toxoplasma positiva isolada. Sob perspectiva do laboratório, qua
- Considere dois achados: (I) ovos de Ascaris em fezes e (II) ausência de ovos em paciente n
- Quando um método morfológico mostra cistos de amebas intestinais não patogênicas, qual lei
- Qual diferença de transmissão é correta quando se comparam Strongyloides stercoralis e Sch
- Uma banca propõe usar Kato-Katz como teste principal para diferenciar E. histolytica e E. 
- Um trabalhador relata contato com água contaminada e apresenta ovo fecal com espículo late
```

## Pedido 30: Medicina veterinária do coletivo: bem-estar, zoonoses e controle populacional (`medicina-veterinaria.saude-publica-bem-estar-animal`, 39 tópicos de edital)

```
Matéria: medicina-veterinaria.saude-publica-bem-estar-animal (Medicina veterinária do coletivo: bem-estar, zoonoses e controle populacional)
Capítulos e quantas questões quero em cada um (total 100):
- "Bem-estar animal, ética e avaliação das cinco liberdades": 17
- "Controle populacional, guarda responsável e manejo de coletividades": 17
- "Vigilância de zoonoses, UVZ e prevenção de riscos coletivos": 17
- "Raiva: cadeia de transmissão, vigilância e profilaxia de exposições": 17
- "Leishmaniose visceral, leptospirose, vacinação e parasitas": 16
- "Biossegurança, medicina veterinária legal e proteção contra maus-tratos": 16

Já existem (não repita):
- Um cão recebe água e ração regularmente, mas vive sem sombra e não consegue se afastar de 
- Ao planejar uma pesquisa com animais, a equipe substitui uma etapa por simulação validada.
- Uma prefeitura considera dois abrigos igualmente satisfatórios por terem pisos limpos e a 
- Segundo a Lei federal nº 13.426/2017, qual elemento deve orientar um programa público de c
- Há recursos limitados para esterilização, e dados revelam grande número de ninhadas em um 
- Uma secretaria informa que seu programa teve sucesso porque passou de 800 para 1.000 cirur
- Ao receber reclamação sobre um gato clinicamente saudável circulando no bairro, qual provi
- Em um foco de leishmaniose visceral urbana, cães infectados e flebotomíneos estão presente
- Uma equipe identifica roedores e pombos em região com resíduos expostos. Qual abordagem re
- Qual situação deve levantar suspeita para vigilância da raiva animal?
- Uma pessoa é mordida por cão conhecido, saudável e acessível ao acompanhamento. Qual orien
- Um morcego é encontrado no chão próximo a crianças, sem agressão testemunhada. Qual condut
- Em área urbana com leishmaniose visceral, qual par reservatório-vetor é o mais relevante p
- O município encontra aumento de leishmaniose visceral e quer diminuir transmissão humana. 
- Após enchente, trabalhadores sem botas retiram lama de casas com sinais de infestação por 
- Qual medida estrutural reduz o risco comunitário de leptospirose e a manutenção de roedore
- Durante recolhimento de animal potencialmente agressivo e com suspeita infecciosa, qual ab
- Em visita domiciliar, observa-se cão sem água, exposto ao sol e com sofrimento contínuo, m
- Qual alternativa relaciona corretamente os dois diplomas legais específicos de Roraima pre
- Durante apuração de maus-tratos, um profissional fotografa lesões, recolhe amostras e redi
- A oferta permanente de água não basta, isoladamente, para assegurar bem-estar se há dor, m
- Os três Rs da experimentação animal significam retirar, recolher e redistribuir animais er
- Um animal que apresenta movimentos repetitivos persistentes pode estar expressando comprom
- A Lei federal nº 13.426/2017 estabelece que todos os cães e gatos de um município devem se
- O cuidado reprodutivo de cães e gatos deve integrar educação em guarda responsável e acomp
- Por funcionar no SUS, a UVZ tem como rotina obrigatória castração, microchipagem, consulta
- Uma visita sanitária pode revelar suspeita de maus-tratos, mas a fiscalização e a responsa
- Um reservatório participa da manutenção do agente infeccioso no ciclo epidemiológico, ao p
- Por serem animais sinantrópicos, todos os pombos e roedores encontrados em áreas urbanas d
- Após mordedura por animal suspeito, a pessoa deve aguardar o desfecho da observação do ani
- O acompanhamento de cão ou gato agressor por dez dias, quando viável, é informação usada n
- A observação domiciliar por dez dias de um morcego encontrado caído no chão segue a mesma 
- Na leishmaniose visceral urbana, cães podem ter relevância como reservatórios, mas a trans
- A ausência de sintomas no cão com infecção por Leishmania prova que ele não tem relevância
- Em localidades sujeitas a enchentes, corrigir descarte de resíduos e abrigo de roedores é 
- A leptospirose é transmitida principalmente pela picada de flebotomíneos, sendo dispensáve
- No manejo de animais de abrigo, a separação por risco sanitário, a higiene adequada e a ob
- Em uma apuração de suspeita de maus-tratos, a inexistência de feridas externas obriga o pr
- A Lei estadual nº 1.637/2022 e a Lei estadual nº 2.167/2025, ambas de Roraima, têm objetos
- Para que um registro fotográfico seja aceito como prova técnica suficiente, pode-se dispen
```

## Pedido 31: Enfermagem em saúde mental e psiquiatria (`enfermagem.saude-mental`, 39 tópicos de edital)

```
Matéria: enfermagem.saude-mental (Enfermagem em saúde mental e psiquiatria)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, evolução e comunicação terapêutica": 20
- "Avaliação e cuidado de enfermagem nos transtornos mentais": 20
- "Crise psiquiátrica, segurança e contenção": 20
- "Psicofarmacologia: administração, monitoramento e educação": 20
- "Cuidado territorial, RAPS, álcool e outras drogas": 20

Já existem (não repita):
- No território, pessoa acompanhada pelo CAPS recebe alta de uma unidade de urgência. Qual a
- Após contenção e administração prescrita de medicação sedativa, um paciente apresenta dess
- A Resolução Cofen nº 746/2024 permite usar contenção mecânica rotineiramente para facilita
- A pessoa em crise de pânico que relata dor torácica deve ser tranquilizada sem qualquer ex
- Na prática da enfermagem em CAPS e UBS, o Projeto Terapêutico Singular (PTS) deve ser ente
- Usuário em lítio apresenta vômitos, desequilíbrio na marcha e confusão após episódio de de
- No atendimento pré-hospitalar móvel, a Resolução Cofen nº 746/2024 prevê situação excepcio
- Um homem apresenta fala desorganizada, sudorese, temperatura elevada e agitação após consu
- Na suspeita de intoxicação ou abstinência com agitação, a avaliação de sinais vitais, resp
- Durante a entrevista, paciente refere ouvir vozes ordenando que se machuque. Qual interven
- A fase de término do relacionamento terapêutico contempla a avaliação do percurso e a prep
- A Resolução Cofen nº 427/2012 é a norma vigente e continua a impor, por si só, o intervalo
- A redução de danos pode integrar o cuidado de enfermagem mesmo quando a pessoa não pactuou
- O relacionamento terapêutico exige que a enfermagem concorde com a veracidade de todas as 
- Na comunicação terapêutica, perguntas abertas e escuta ativa permitem compreender o signif
- Conforme a Resolução Cofen nº 746/2024, qual condição autoriza contenção mecânica?
- No cuidado à pessoa com episódio maníaco, qual plano de enfermagem é mais indicado?
- Qual manifestação em pessoa que iniciou antipsicótico sugere acatisia?
- Pessoa idosa previamente orientada apresenta desatenção flutuante e alteração comportament
- O aparecimento de febre, rigidez muscular, alteração de consciência e instabilidade autonô
- Monitorar hidratação, função renal, função tireoidiana e litemia prescrita é pertinente à 
- Na avaliação de uma pessoa com depressão, perguntar de modo direto sobre pensamentos suici
- Uma pessoa com esquizofrenia e desorientação súbita deve ser considerada exclusivamente em
- Após início de antidepressivo serotoninérgico associado a outro produto, surgem agitação, 
- A administração prolongada de benzodiazepínicos elimina o risco de dependência e torna seg
- Durante a entrevista em saúde mental, a observação não verbal torna desnecessário investig
- Em uma internação breve, o enfermeiro prepara a pessoa para a transferência e revê metas a
- Uma pessoa pergunta por que o antipsicótico exige avaliação de peso, pressão, glicemia e l
- Em uma unidade de urgência, a pessoa está agitada, sem agressão atual e ainda consegue dia
- Acolhimento em saúde mental é uma atitude de responsabilização e escuta qualificada, e não
- Delirium é o mesmo fenômeno que delírio persecutório, já que ambos descrevem crenças falsa
- Ao realizar contenção mecânica de maneira excepcional, qual documentação é exigida pela no
- Quando for indispensável a contenção mecânica, as razões, a duração, as avaliações e os ev
- Entre os efeitos que exigem cautela especial ao administrar benzodiazepínico em pessoa ido
- Um usuário acompanhado pelo CAPS deixa de precisar da atenção primária, pois o CAPS assume
- Sobre redução de danos no cuidado a pessoas que usam álcool e outras drogas, assinale a al
- Uma pessoa refere que só aceitará conversar sobre medicação se não for repreendida por ter
- Acatisia pode manifestar-se como inquietação intensa e deve ser considerada na avaliação d
- Uma usuária afirma: “Ninguém me leva a sério”. Qual resposta inicial demonstra comunicação
- Em pessoa com uso crônico de álcool, tremor, sudorese, taquicardia e confusão após interru
```

## Pedido 32: Esportes: modalidades, regras e treinamento (`educacao-fisica.esportes-regras-treinamento`, 39 tópicos de edital)

```
Matéria: educacao-fisica.esportes-regras-treinamento (Esportes: modalidades, regras e treinamento)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos do esporte e iniciação esportiva": 17
- "Futebol, futsal e handebol: regras e sistemas de jogo": 17
- "Voleibol e basquetebol: técnica, regras e leitura tática": 17
- "Atletismo e natação: provas, execução e arbitragem": 17
- "Ginásticas esportivas e lutas: princípios, técnica e segurança": 16
- "Treinamento desportivo, periodização e organização de competições": 16

Já existem (não repita):
- Em uma sessão de iniciação ao futebol, o professor deseja treinar uma decisão tática, e nã
- Em jogos esportivos, qual associação entre família e modalidade está correta?
- Um grupo heterogêneo aprende basquete. O treinador quer ampliar decisões sem descaracteriz
- Um atleta realiza passe de voleibol mecanicamente correto, porém direciona a bola a colega
- Qual modalidade emprega, em sua forma oficial convencional, cinco jogadores por equipe na 
- Uma equipe de handebol usa um defensor avançado à frente dos demais jogadores de linha. Qu
- Em partida adulta de handebol, um jogador de linha segura a bola sem driblar. Qual limite 
- Em partida convencional de futebol de campo, um atacante recebe diretamente a bola de arre
- No voleibol convencional, uma equipe vence um primeiro set quando, no mínimo, atinge qual 
- Durante um rali de voleibol, a equipe toca a bola no bloqueio e depois faz recepção, levan
- A comissão técnica do basquete FIBA cria uma tabela de pontuação. Qual combinação respeita
- Um professor quer observar uma decisão tática no basquete, não apenas contar finalizações.
- Qual sequência corresponde ao medley individual da natação competitiva?
- Na aferição da chegada em uma prova de corrida, qual parte do corpo define a ordem de pass
- Em uma aula, o professor afirma que a prova de estilo livre obriga o participante a nadar 
- Ao montar um evento de atletismo, qual prova é classificada ordinariamente como prova de c
- Qual conjunto corresponde a aparelhos tradicionais da ginástica artística feminina em comp
- Em iniciação às lutas, que proposta respeita melhor progressão e segurança?
- Um edital compara o judô ao boxe. Qual interpretação das formas de oposição é mais precisa
- Em uma prova de ginástica artística, a banca afirma que pontuação de dificuldade e avaliaç
- Qual hierarquia descreve corretamente unidades comuns da periodização desportiva, da mais 
- Em equipe que pretende atingir o melhor desempenho em torneio-alvo, qual uso da estratégia
- Um torneio de turno único terá oito equipes, e cada dupla se enfrentará uma vez. Quantas p
- Uma competição de eliminatória simples reúne oito equipes e prevê apenas os confrontos nec
- O fundamento técnico é o modo de executar uma ação motora, enquanto a tática envolve a esc
- Um jogo reduzido só pode ser chamado de iniciação esportiva se reproduzir integralmente o 
- A classificação de natação como modalidade individual impede que haja provas por equipes, 
- Na observação de um jogo, o êxito eventual de uma finalização prova, por si, que a escolha
- O futsal utiliza cinco jogadores por equipe, já incluído o goleiro, e não aplica a regra d
- A expressão defesa 6-0 no handebol significa que a equipe joga oficialmente sem goleiro e 
- O jogador de linha do handebol pode recomeçar seu drible a qualquer momento depois de segu
- Receber a bola diretamente de um tiro de canto no futebol de campo não configura, por esse
- Em um quinto set de voleibol, a referência é quinze pontos, exigindo-se vantagem mínima de
- O líbero no voleibol representa um sétimo atleta simultâneo na quadra e pode bloquear norm
- Em regras FIBA, um lance livre convertido vale um ponto e uma cesta de campo pode valer do
- O jogador que realiza um bloqueio no voleibol consome, obrigatoriamente, o primeiro dos tr
- No medley individual de natação, a ordem dos estilos é borboleta, costas, peito e livre.
- Em corridas do atletismo, determina-se a ordem de chegada pela primeira ponta do calçado q
- No revezamento medley da natação, a ordem regulamentar das modalidades inicia por costas, 
- A expressão nado livre é, em todas as circunstâncias regulamentares, sinônimo obrigatório 
- Na ginástica artística feminina, trave de equilíbrio e barras assimétricas integram o prog
- Na ginástica para todos, a finalidade é necessariamente obter classificação por pontuação 
- Em modalidades de luta, a técnica de controle de distância e a escolha do momento de ataca
- Por terem a mesma finalidade de disputa, judô e boxe autorizam necessariamente os mesmos a
- Em pontos corridos de turno único com seis equipes, há quinze confrontos se cada dupla jog
- Intensidade e volume são conceitos equivalentes, pois ambos expressam exclusivamente o núm
- Em mata-mata simples com oito equipes e sem disputas extras, são necessárias sete partidas
- Critérios de desempate em campeonatos seguem, por natureza, ordem universal na qual confro
```

## Pedido 33: Tecnologia, inteligência artificial e prova digital no processo penal (`processo-penal.tecnologia-inteligencia-artificial-provas-digitais`, 39 tópicos de edital)

```
Matéria: processo-penal.tecnologia-inteligencia-artificial-provas-digitais (Tecnologia, inteligência artificial e prova digital no processo penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Inteligência artificial, predição criminal e garantias constitucionais": 21
- "2. Investigação digital, sigilo telemático e cadeia de custódia": 20
- "3. Perfis genéticos, biometria e vigilância probatória": 18
- "4. Crianças, adolescentes e investigação em ambientes digitais": 18
- "5. Processo eletrônico, transmissão de atos e valoração da prova tecnológica": 23

Já existem (não repita):
- Um sistema informa que determinada pessoa tem probabilidade elevada de cometer novo delito
- Uma solução de IA destinada a apoiar a valoração de provas em processo contencioso é enqua
- Uma investigação demanda registros de conexão com a internet conservados pelo responsável 
- Diante da possibilidade de expiração dos logs, a autoridade requer ao provedor que os pres
- Uma imagem forense apresenta hash igual ao da cópia originalmente documentada. Que conclus
- Na guarda de perfis genéticos decorrentes de identificação criminal, a equipe pergunta que
- Em ação penal ocorre absolvição, e a pessoa busca excluir perfil genético inserido no banc
- Uma criança vítima de violência é entrevistada pela rede de proteção para colher o estrita
- Em investigação de delitos contra criança, cogita-se infiltração policial na internet nos 
- Uma petição penal é enviada ao sistema judicial eletrônico às 23h40 do último dia do prazo
- O sistema judicial apresenta indisponibilidade técnica no último dia para peticionar e é r
- Um investigado deseja contestar a autoria de mensagem exibida em captura de tela. Qual con
- Durante investigação em rede social, a equipe identifica perfil público suspeito. Qual ope
- Em investigação de crime sexual, discute-se submeter a vítima de seis anos ao depoimento e
- Uma decisão discute prazo de interceptação do fluxo de comunicações em sistema telemático,
- Após condenação penal e cumprimento da pena, uma pessoa requer exclusão do perfil genético
- Um sistema de pontuação judicial atribui credibilidade menor ao depoimento de determinadas
- Uma ferramenta de transcrição automática omite a palavra 'não' de um depoimento gravado, a
- Uma investigação deseja utilizar identificador facial encontrado em fotografia e também af
- Uma pessoa é condenada à pena de reclusão em regime inicial fechado e ingressa no estabele
- Um aplicativo divulga material sexual ilícito produzido com manipulação digital de imagem 
- Durante coleta de prova de nuvem, o arquivo baixado foi convertido de formato antes de cal
- Uma rede social recebe pedido informal para fornecer mensagens privadas de usuário. Qual a
- Em processo penal eletrônico, alegou-se que uma captura de tela seria prova indiscutível p
- Um procedimento investigativo envolve tratamento de dados pessoais para finalidade exclusi
- Uma solução judicial de IA que impossibilita qualquer revisão humana dos resultados contra
- Na Resolução CNJ 615/2025, todas as aplicações classificadas como alto risco são necessari
- Segundo o Marco Civil da Internet, a obrigação geral de guarda dos registros de acesso a a
- Se duas cópias digitais apresentarem o mesmo hash, estará automaticamente demonstrado que 
- A Lei 12.037/2009, art. 5º-A, prevê que coincidências entre perfis genéticos em banco ofic
- Quando houver violência sexual, a Lei 13.431/2017 exige que a criança preste sucessivos de
- Uma interceptação do fluxo de comunicações em sistema de informática ou telemática encontr
- A cadeia de custódia prevista no art. 158-B do CPP termina com a etapa de processamento, d
- A Lei 15.295/2025 ampliou hipóteses de identificação genética e alterou tanto a Lei 12.037
- Pelo art. 190-A do ECA, uma operação de infiltração virtual inicialmente autorizada por no
- A Lei 15.487/2026 atualizou a disciplina de imagens ilícitas envolvendo crianças e adolesc
- O art. 11 da Lei 11.419/2006 atribui caráter de original a qualquer print anexado ao proce
- Uma indisponibilidade técnica do sistema judicial eletrônico no encerramento do prazo prod
- Uma equipe dispõe de fonte pública suficiente para comprovar determinado fato, mas pretend
- Para aferir aderência ao direito em uma ferramenta de IA que analisa provas, conhecer sua 
- A exclusão do tratamento penal das regras gerais da LGPD pelo art. 4º, III, permite coleta
- O art. 190-E do ECA prevê documentação e encaminhamento ao juízo dos atos eletrônicos da i
- O art. 241-E do ECA, com a atualização de 2026, restringe o conceito de material sexual il
- Uma ordem para conservar registros por prazo superior ao mínimo legal não equivale, só por
- A assinatura eletrônica de uma petição no processo penal prova também a veracidade históri
```

## Pedido 34: Disposições Constitucionais Aplicáveis ao Direito Penal (`direito-penal.disposicoes-constitucionais-penais`, 38 tópicos de edital)

```
Matéria: direito-penal.disposicoes-constitucionais-penais (Disposições Constitucionais Aplicáveis ao Direito Penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios constitucionais penais: legalidade, anterioridade e lei penal no tempo": 20
- "2. Constitucionalismo das penas: pessoalidade, individualização, penas permitidas e vedadas": 20
- "3. Mandados constitucionais de criminalização: crimes inafiançáveis, imprescritíveis e equiparados": 20
- "4. Tribunal do Júri, extradição, garantias do preso, competência legislativa, foro e imunidades": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- Um ato normativo expedido pelo Poder Executivo federal tipifica nova conduta criminosa e c
- Após o trânsito em julgado de sentença penal condenatória que impôs pena de quatro anos de
- Condenado definitivamente ao cumprimento de pena privativa de liberdade, ao pagamento de m
- A Constituição da República de 1988 elenca expressamente modalidades de penas proibidas no
- Entre os mandados constitucionais de criminalização previstos no art. 5º da Constituição F
- No que tange ao tratamento constitucional dispensado aos crimes hediondos, à tortura, ao t
- O art. 5º, XXXVIII, da Constituição Federal reconhece a instituição do Tribunal do Júri, c
- Um cidadão nascido em território estrangeiro obteve a naturalização brasileira em 2018. Em
- Assembleia Legislativa de determinado Estado da Federação aprova lei ordinária estadual ti
- O princípio constitucional da legalidade penal exige que a definição da conduta criminosa 
- A Constituição da República veda de modo absoluto a aplicação da pena de morte no Brasil, 
- Às presidiárias serão asseguradas condições para que possam permanecer com seus filhos dur
- Segundo a Constituição Federal, os crimes de tortura, tráfico ilícito de entorpecentes, te
- O brasileiro nato jamais será extraditado pelo Estado brasileiro, ainda que venha a pratic
- O preso tem direito constitucional à identificação dos responsáveis por sua prisão ou por 
- Durante a execução de um crime de extorsão mediante sequestro que durou quarenta dias, ent
- A respeito da relação entre o princípio da reserva legal penal (CF, art. 5º, XXXIX) e as m
- Considerando o princípio constitucional da individualização da pena (CF, art. 5º, XLVI) e 
- Em relação ao crime de racismo previsto no art. 5º, XLII, da Constituição Federal, a evolu
- Um réu primário, de bons antecedentes, que não se dedica a atividades criminosas nem integ
- Um Procurador do Estado, que possui foro por prerrogativa de função perante o Tribunal de 
- Submetido a julgamento perante o Tribunal do Júri pela prática de homicídio simples doloso
- Acerca das imunidades parlamentares previstas na Constituição Federal para deputados feder
- As normas penais em branco heterogêneas, cujo preceito primário é complementado por ato no
- Quando uma nova lei penal favorece o réu em determinado aspecto, mas o prejudica em outro,
- Embora o art. 5º, XLIII, da Constituição Federal mencione apenas que os crimes hediondos e
- Por serem classificados como crimes inafiançáveis pelo art. 5º, XLIII, da Constituição Fed
- O crime de latrocínio, por envolver dolo na conduta que resulta na morte da vítima para su
- A Constituição Federal veda a concessão de extradição de estrangeiro por crime político ou
- Nos termos do art. 5º, LXI, da Constituição Federal, ninguém será preso senão em flagrante
- Um deputado federal praticou crime de corrupção passiva no exercício do mandato e em razão
- Além dos mandados constitucionais de criminalização previstos no art. 5º, a Constituição F
- Durante uma discussão em rede social, um usuário dirige ofensas graves e humilhantes a um 
- Um indivíduo foi denunciado por crime praticado antes de sua diplomação como Senador da Re
- Analise a situação em que um Vice-Governador de Estado e um Deputado Federal praticam, em 
- O Estado do País X requer ao Brasil a extradição de um nacional daquele país que responde,
- Em matéria de competência legislativa penal e de definição de crimes de responsabilidade, 
- Ao réu submetido à medida de segurança por inimputabilidade penal (doença mental) aplicam-
- As leis penais temporárias e excepcionais, mesmo após o encerramento de seu período de vig
- A obrigatoriedade do trabalho remunerado do condenado à pena privativa de liberdade, previ
- Os tratados internacionais de direitos humanos que preveem a imprescritibilidade dos crime
- O sigilo das votações no Tribunal do Júri (CF, art. 5º, XXXVIII, "b") impede a realização 
- A imunidade material parlamentar prevista no art. 53, caput, da Constituição Federal torna
- Conforme a Súmula Vinculante 11 do Supremo Tribunal Federal, o uso de algemas só é lícito 
- No âmbito das garantias constitucionais contra o arbítrio punitivo, a decretação do estado
```

## Pedido 35: Cerimonial Público e Ordem Geral de Precedência (Decreto 70.274/1972) (`seguranca-institucional.cerimonial-publico-decreto-70274`, 38 tópicos de edital)

```
Matéria: seguranca-institucional.cerimonial-publico-decreto-70274 (Cerimonial Público e Ordem Geral de Precedência (Decreto 70.274/1972))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Quem preside e precedência": 25
- "2. Representação, hinos e Pavilhão": 25
- "3. Bandeira Nacional": 25
- "4. Posse, visitas e ordem geral de precedência": 25

Já existem (não repita):
- Pelo Decreto 70.274/1972, o Presidente da República presidirá sempre a cerimônia a que com
- Nos Estados, o Governador presidirá as solenidades a que comparecer, exceto:
- A precedência entre os Ministérios de Estado é determinada pelo critério histórico de cria
- Quando estiverem presentes personalidades estrangeiras, terá precedência sobre os demais M
- No respectivo Estado, têm precedência sobre as autoridades federais, nessa ordem:
- Em visita a um Estado, o Presidente do Supremo Tribunal Federal passa logo após o Governad
- A precedência entre os Governadores dos Estados é determinada pela ordem de constituição h
- Em igualdade de categoria, em cerimônia de caráter federal, a precedência segue a ordem:
- Os antigos Chefes de Estado que não exerçam função pública passam logo após:
- Os Cardeais da Igreja Católica, como possíveis sucessores do Papa, têm situação correspond
- Nos casos omissos, cabe a quem prestar esclarecimentos protocolares e determinar a colocaç
- Os inativos passam logo antes dos funcionários em serviço ativo de igual categoria.
- Em jantares e almoços oficiais, o convidado impedido pode fazer-se representar por pessoa 
- Quando o Presidente da República se fizer representar em uma solenidade, o lugar que compe
- Nenhum convidado poderá fazer-se representar nas cerimônias a que comparecer o Presidente 
- Nas cerimônias em que se tenha de executar Hino Nacional estrangeiro:
- A execução do Hino Nacional, em regra, só terá início depois que o Presidente da República
- O Pavilhão Presidencial deve ser hasteado nos órgãos, autarquias e fundações federais, est
- Os cartões de convite do Presidente da República têm as Armas Nacionais gravadas a ouro, p
- Na presença de Chefe de Estado estrangeiro, o Presidente da República pode substituir a Fa
- Quando várias bandeiras são hasteadas ou arriadas simultaneamente, a Bandeira Nacional:
- A Bandeira Nacional só pode ser hasteada entre as 8 e as 18 horas.
- No Dia da Bandeira, 19 de novembro, o hasteamento da Bandeira Nacional é realizado:
- Quando em funeral, a Bandeira fica a meio-mastro e, no hasteamento ou arriamento, deve ser
- Com outras bandeiras em linha de mastros, o lugar de honra da Bandeira Nacional é a posiçã
- Considera-se direita de um dispositivo de bandeiras a direita de uma pessoa colocada junto
- Nas escolas públicas ou particulares, durante o ano letivo, o hasteamento solene da Bandei
- A Bandeira Nacional nunca se abate em continência.
- Distendida e sem mastro, a Bandeira Nacional deve ser colocada:
- A Bandeira Nacional da Praça dos Três Poderes é substituída com solenidades especiais no p
- Em todo o País, a Bandeira Nacional é hasteada em funeral quando:
- Na ordem geral de precedência em Brasília, figuram no mesmo nível, logo após o Vice-Presid
- Na ordem geral de precedência em Brasília, os Presidentes do Congresso Nacional, da Câmara
- Na posse presidencial, a cerimônia do compromisso constitucional é organizada e executada 
- Na nomeação dos novos Ministros de Estado, o primeiro decreto assinado é o de nomeação do 
- O Presidente da República retribui pessoalmente todas as visitas que recebe de autoridades
- Em visita oficial do Presidente da República a um Estado, a autoridade que o recebe no loc
- A transmissão temporária do Poder, por impedimento do Presidente da República, realiza-se 
- Nos Municípios, quem preside as solenidades municipais é:
- Os Ministros de Estado presidirão, segundo o Decreto 70.274/1972:
```

## Pedido 36: Geologia: hidrogeologia, mapeamento e petrologia (`geologia.hidrogeologia-cartografia-petrologia`, 38 tópicos de edital)

```
Matéria: geologia.hidrogeologia-cartografia-petrologia (Geologia: hidrogeologia, mapeamento e petrologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Mineralogia, petrografia e petrologia das rochas": 20
- "Geologia estrutural: deformação, falhas, dobras e descontinuidades": 20
- "Cartografia geológica e hidrogeológica: campo, escalas e interpretação": 20
- "Hidrogeologia quantitativa: aquíferos, fluxo, ensaios e monitoramento": 20
- "Geologia ambiental e geotecnia: risco, investigação e proteção dos aquíferos": 20

Já existem (não repita):
- Ao identificar uma amostra transparente, com dureza aproximadamente 7 e fratura conchoidal
- Uma rocha máfica de textura predominantemente afanítica, originada da consolidação de lava
- No diagnóstico de uma rocha sedimentar detrítica, qual atributo é especialmente útil para 
- Uma amostra de calcário submetida a metamorfismo e recristalização predominantemente no es
- Em uma falha normal simples, o movimento relativo do bloco de teto é caracterizado por:
- Uma carta geológica apresenta escala 1:100.000. Qual distância real corresponde a 2 cm med
- Assinale a unidade geológica que armazena água e a transmite lentamente, sendo frequenteme
- Em meio poroso homogêneo, saturado, com K = 10⁻⁴ m/s e espessura saturada b = 20 m, a tran
- Um poço foi desligado após ensaio a vazão constante e o rebaixamento residual passou a ser
- Durante a análise de mapa potenciométrico horizontal em aquífero aproximadamente isotrópic
- Em uma sequência deformada cuja polaridade estratigráfica é conhecida, uma dobra com rocha
- Uma investigação encontra argila com alta porosidade total e baixa condutividade hidráulic
- O mapa geológico indica contato tracejado entre duas unidades, sem exposição contínua de a
- Em uma análise de perigo e risco geológico, duas encostas de suscetibilidade semelhante di
- Qual resultado, isoladamente, é menos específico para identificar a substância responsável
- Em solo saturado, mantida constante a tensão total, um aumento da poropressão provoca qual
- Dois poços captam níveis aquíferos diferentes e suas medições são feitas em meses distinto
- No método de Cooper–Jacob para poço em condições apropriadas, a inclinação da reta rebaixa
- Qual afirmação descreve corretamente a vulnerabilidade intrínseca de um aquífero?
- Ao preparar estudo de estabilidade de um talude rochoso, qual conjunto de dados é mais dir
- A escala de Mohs é uma escala linear, na qual mineral de dureza 8 possui exatamente o dobr
- O basalto é, em geral, uma rocha ígnea vulcânica de composição máfica, enquanto o gabro re
- A transformação de arenito quartzoso em quartzito pode ocorrer por recristalização metamór
- A existência de foliação em uma rocha demonstra necessariamente que ela se formou por soli
- Na descrição de um plano geológico, o mergulho verdadeiro corresponde à maior inclinação e
- Uma junta geológica e uma falha são equivalentes, pois ambas exigem deslocamento relativo 
- Uma carta na escala 1:25.000 pode oferecer maior detalhamento espacial que uma carta na es
- Toda antiforme contém necessariamente as rochas mais antigas em seu núcleo, mesmo quando n
- A presença de uma falha em rocha cristalina não assegura, isoladamente, elevada transmissi
- É tecnicamente adequado misturar níveis de poços de diferentes aquíferos e datas, sem qual
- Em meio aproximadamente isotrópico, o fluxo subterrâneo horizontal tende a ocorrer dos mai
- A porosidade total e a condutividade hidráulica são numericamente equivalentes porque amba
- Para um aquífero homogêneo com condutividade hidráulica horizontal constante e espessura s
- No ensaio de vazão constante, o rebaixamento observado no próprio poço produtor é sempre i
- O teste de recuperação é executado após a interrupção do bombeamento e registra a evolução
- O simples fato de um aquífero ser confinado garante que qualquer poço nele perfurado apres
- O balanço hídrico de um aquífero exige definir limites e período de análise, computando en
- A Rede Integrada de Monitoramento das Águas Subterrâneas (RIMAS), mantida pelo SGB, contri
- Uma área de alta vulnerabilidade intrínseca de aquífero está, por definição, comprovadamen
- Elevar a poropressão mantendo a tensão total constante diminui a tensão efetiva e pode con
```

## Pedido 37: Tributação estadual de Minas Gerais (`direito-tributario.tributacao-estadual-minas-gerais`, 37 tópicos de edital)

```
Matéria: direito-tributario.tributacao-estadual-minas-gerais (Tributação estadual de Minas Gerais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema tributário mineiro e Lei nº 6.763/1975": 15
- "2. ICMS e RICMS/MG: incidência, apuração e substituição tributária": 19
- "3. IPVA mineiro: fato gerador, sujeito passivo, base e alíquotas": 23
- "4. ITCD mineiro: transmissões gratuitas, base, contribuintes e pagamentos": 24
- "5. RPTA: fiscalização, lançamento, impugnação e recursos": 19

Já existem (não repita):
- Uma empresa mineira vende mercadorias a um comerciante e precisa identificar a lei estadua
- Em uma operação ocorrida em setembro de 2026, um contribuinte consulta regras de substitui
- Uma pessoa compra em uma loja um veículo novo em março, e outra compra um veículo usado em
- Uma pessoa transfere gratuitamente R$ 200 mil a outra, em situação sujeita ao ITCD de Mina
- Após receber intimação de lançamento de crédito tributário, um sujeito passivo pretende in
- Uma empresa presta transporte apenas entre dois endereços de um mesmo município mineiro. C
- Uma empresa possui débito de ICMS de R$ 1.800 nas saídas e créditos juridicamente admitido
- Ao analisar o IPVA de uma motocicleta comum em Minas Gerais, sem enquadramento em benefíci
- Um herdeiro recebe bem por falecimento e pretende efetuar o pagamento ordinário do ITCD pr
- Um contribuinte apresenta impugnação eletrônica de lançamento tributário mineiro e deseja 
- Uma lei mineira prevê redução da base de cálculo de determinado produto e outro dispositiv
- Uma revendedora mineira recolhe ICMS sobre a operação própria e, nos casos legalmente prev
- Uma empresa adquire do exterior veículo automotor diretamente para seu uso em Minas Gerais
- Na dissolução de união estável, uma pessoa recebe, gratuitamente, bens acima de sua meação
- Uma impugnação contra lançamento tributário mineiro foi apresentada com defeito de procura
- Um contribuinte examina uma operação mineira de 2026 e encontra referência ao Anexo IX do 
- Uma sociedade que aluga veículos pede IPVA de 1% em sua frota. A administração verifica se
- Durante inventário, uma parte sustenta que o ITCD não pode surgir antes de ser instaurado 
- Após acórdão da Câmara de Julgamento, a parte analisa possibilidade de Recurso de Revisão 
- Uma empresa mineira precisa cumprir escrituração digital e documentar a saída de mercadori
- Uma operação realizada em 15 de junho de 2023 é discutida em 2026. O estudo do RICMS/MG ne
- Após impugnação de Auto de Infração, a fiscalização reformula lançamento com aumento de cr
- Uma pessoa domiciliada em outro estado recebe de doador domiciliado em Minas Gerais determ
- Uma pessoa recebe decisão que nega seguimento de impugnação por irregularidade de represen
- Um contribuinte fez transmissão causa mortis, recolheu ITCD com desconto em 85 dias, mas e
- Uma empresa mineira que realizou operação de ICMS em 2026 deve utilizar como referência re
- No caso de veículo automotor usado adquirido por particular no segundo semestre, a data da
- Ao receber doação sujeita ao ITCD mineiro, o donatário é, como regra, contribuinte do impo
- Ao ser intimado de lançamento tributário em Minas Gerais, o sujeito passivo dispõe de 10 d
- Uma empresa que transporta mercadorias entre dois municípios mineiros pode realizar presta
- Em uma apuração de ICMS, determinada empresa pode somar integralmente o imposto das entrad
- Uma indústria mineira deve verificar o Anexo V do RICMS/2023 ao analisar regras de emissão
- Na importação direta de veículo automotor pelo consumidor, o fato gerador do IPVA mineiro 
- Quando alguém recebe gratuitamente parcela patrimonial além da meação em uma partilha, é n
- No e-PTA mineiro, a impugnação escrita necessariamente deve ser encaminhada pelos correios
- Uma questão que mencione a disciplina da substituição tributária em junho de 2022 pode exi
- Em uma autuação sobre operação mercantil efetiva, a simples ausência de nota fiscal é sufi
- Uma concessionária vende veículo novo em outubro a consumidor mineiro. Na apuração inicial
- Na legislação mineira consultada, a alíquota residual de IPVA de 4% alcança necessariament
- Uma transmissão causa mortis sujeita ao ITCD estadual pode gerar obrigação de imposto mesm
- O desconto possível para ITCD causa mortis recolhido em até 90 dias afasta qualquer exigên
- Quando a fiscalização reformula lançamento contestado para ampliar o crédito e acrescentar
- O sujeito passivo que apresenta impugnação administrativa sem o comprovante de taxa de exp
- O contribuinte cuja representação em impugnação apresenta vício deve ser intimado para san
- O Recurso de Revisão dirigido à Câmara Especial do contencioso tributário mineiro pode ser
```

## Pedido 38: História da arte e artes visuais (`artes.historia-arte-artes-visuais`, 37 tópicos de edital)

```
Matéria: artes.historia-arte-artes-visuais (História da arte e artes visuais)
Capítulos e quantas questões quero em cada um (total 100):
- "Panorama histórico da arte: da pré-história ao século XIX": 20
- "Vanguardas, modernidade e arte contemporânea internacional": 20
- "História da arte no Brasil: matrizes, colonial, modernismo e contemporaneidade": 20
- "Linguagens visuais, técnicas e leitura crítica da imagem": 20
- "Cultura popular, patrimônio e arte e tecnologia": 20

Já existem (não repita):
- A arte rupestre brasileira pode ser estudada como fonte arqueológica, mas seus significado
- Na arquitetura gótica europeia, o arco ogival e a abóbada nervurada figuram entre soluções
- A perspectiva linear produz profundidade exclusivamente por meio de perda gradual de nitid
- A xilogravura utiliza uma matriz de madeira que recebe entalhes antes da impressão.
- Na história da arte brasileira, Aleijadinho é referência da escultura e da arquitetura col
- Toda obra contemporânea precisa ser produzida com computador e disponibilizada na internet
- O Cubismo, em diversas experiências de Picasso e Braque, questionou o espaço perspectivo ú
- O Rococó europeu e o Barroco são denominações rigorosamente equivalentes, pois ambos descr
- A Semana de Arte Moderna de 1922 marcou debates modernistas no Brasil, embora experiências
- O registro federal de patrimônio imaterial tem a mesma finalidade e o mesmo procedimento d
- Uma tiragem de gravura pode reunir vários exemplares considerados originais, dependendo do
- A antropofagia cultural modernista defendia a cópia fiel de todas as vanguardas europeias 
- A identificação iconográfica de atributos em uma pintura equivale, necessariamente, à inte
- Na produção brasileira dos anos 1950 e 1960, a passagem do concretismo ao neoconcretismo e
- Como o ready-made usa um objeto já fabricado, sua relevância estética reside obrigatoriame
- As práticas artísticas indígenas no território brasileiro só passaram a ter valor históric
- O uso de fotografia e de arquivo em uma instalação contemporânea pode integrar sua linguag
- Uma obra de net art deve necessariamente ser vendida como NFT, porque rede digital e certi
- Para salvaguardar uma manifestação imaterial, é necessário impedir qualquer mudança em sua
- A chamada lei da frontalidade descreve convenções recorrentes em imagens egípcias, mas não
- Um monumento medieval apresenta arco ogival, abóbadas nervuradas e arcobotantes. Qual clas
- Ao estudar uma pintura que explora efeitos transitórios da luz e pinceladas visíveis, o mo
- O procedimento de imprimir uma imagem a partir de uma matriz de madeira caracteriza a:
- Qual artista é associado à obra Abaporu, realizada em 1928?
- Em uma imagem, o grau de claridade ou escuridão de uma cor ou área é chamado de:
- Qual é o instrumento federal associado principalmente à proteção de um edifício histórico 
- Qual alternativa identifica corretamente o ready-made associado a Marcel Duchamp?
- O que melhor descreve os Bichos de Lygia Clark?
- Entre os tipos de patrimônio, a transmissão coletiva de um ofício tradicional se enquadra 
- Um quadro apresenta figuras fragmentadas, planos geométricos e uma garrafa vista por ângul
- Uma imagem de santo é identificada pelo atributo iconográfico que carrega. Em um passo pos
- Acerca do modernismo brasileiro, assinale a alternativa historicamente consistente.
- Um quadro renascentista representa uma rua com linhas de telhados e calçamento convergindo
- Uma artista cria um ambiente transitável com fotografias, objetos, sons e projeções, cujo 
- A política de preservação de um folheto de cordel e do saber tradicional que o sustenta ex
- Sobre o Neoclassicismo e o Romantismo europeus, é correto afirmar que:
- Ao comparar modernismo brasileiro e vanguardas europeias, a conclusão mais bem fundamentad
- Uma prova afirma que a arte do Egito antigo utiliza a lei da frontalidade. A formulação co
- Uma peça de Duchamp é retirada de seu uso cotidiano e inserida em circuito expositivo. Qua
- Uma curadoria mostra um Parangolé e pede ao visitante que considere o vestir e o movimento
- Uma instituição apresenta a digitalização de fotografias de seu acervo e afirma que a oper
- Em uma xilogravura de cordel, a impressão material e o saber compartilhado pela comunidade
- Um curador apresenta arte indígena contemporânea ao lado de arte rupestre e sugere que a p
- Qual afirmação sobre o concretismo e o neoconcretismo brasileiros é mais defensável?
- Uma obra de IA generativa é inserida em uma exposição. Qual procedimento analítico oferece
```

## Pedido 39: Impulso, quantidade de movimento e colisões (`fisica.impulso-quantidade-movimento-colisoes`, 37 tópicos de edital)

```
Matéria: fisica.impulso-quantidade-movimento-colisoes (Impulso, quantidade de movimento e colisões)
Capítulos e quantas questões quero em cada um (total 100):
- "Quantidade de movimento linear": 29
- "Impulso e variação do momento": 21
- "Conservação do momento em sistemas": 29
- "Colisões elásticas e inelásticas": 21

Já existem (não repita):
- Qual é a quantidade de movimento de um corpo de 2 kg que se desloca a 3 m/s?
- Uma força constante de 10 N atua durante 4 s. Qual impulso ela produz?
- Um corpo de 3 kg passa de 2 m/s para 6 m/s no mesmo sentido. Qual é a variação de sua quan
- Um impulso de 30 N·s é produzido durante 0,5 s. Qual é o módulo da força média?
- Um carrinho de 2 kg a 4 m/s colide com outro de 2 kg em repouso. Eles ficam unidos. Qual é
- Um carrinho de 1 kg a 6 m/s alcança um carrinho de 2 kg em repouso e os dois permanecem un
- Uma bola de 4 kg muda sua velocidade de 2 m/s para 5 m/s no mesmo sentido. Qual impulso re
- Um corpo de 3 kg a 4 m/s colide e fica unido a outro de 1 kg inicialmente parado. Qual é a
- Uma bola de 0,2 kg move-se a 10 m/s para a direita e retorna a 6 m/s para a esquerda. Qual
- Dois carrinhos idênticos colidem elasticamente em uma dimensão. O primeiro chega a 5 m/s e
- O gráfico força-tempo é triangular: a força cresce de 0 a 20 N e retorna a 0 ao longo de 4
- Um corpo de 3 kg a 4 m/s colide com outro de 1 kg que se move a −2 m/s. Eles ficam unidos.
- Uma bola de 0,5 kg muda sua velocidade de −4 m/s para 2 m/s. Qual é o impulso resultante s
- Um automóvel de 1.500 kg viaja a 20 m/s. Qual é sua quantidade de movimento?
- Um impulso de 8 N·s age sobre um corpo de 2 kg inicialmente em repouso. Qual é sua velocid
- Um carro de 1.000 kg reduz a velocidade de 20 m/s a zero em 5 s. Qual é a força média resu
- Uma força constante de 50 N atua por 0,2 s sobre um objeto. Qual é o impulso recebido?
- Um carrinho de 0,5 kg a 8 m/s colide com outro de 1,5 kg em repouso. Eles ficam unidos. Qu
- Duas massas iguais colidem elasticamente em uma dimensão. A primeira vinha a 8 m/s e a seg
- Um objeto possui quantidade de movimento de 24 kg·m/s e massa de 6 kg. Qual é sua velocida
- Um objeto de massa 2 kg move-se a 3 m/s para leste. Qual é o módulo de sua quantidade de m
- Uma força constante de 5 N atua durante 4 s no sentido positivo. Qual módulo de impulso pr
- Um corpo de 4 kg tem velocidade de −2 m/s no eixo escolhido. Qual é seu momento linear esc
- O momento linear de um corpo muda de 6 para 18 kg·m/s. Qual impulso resultante recebeu?
- Dois corpos de 2 kg e 3 kg movem-se juntos a 4,8 m/s. Qual é o momento total do sistema?
- Em colisão perfeitamente inelástica de sistema isolado, qual descrição geral é correta?
- Um sistema inicialmente em repouso se separa em dois corpos sem impulso externo relevante.
- Se a massa de um objeto permanece igual e sua velocidade dobra, como muda o módulo do mome
- Num gráfico força-tempo, a força constante é 16 N durante 2 s. Qual área representa o impu
- Uma colisão é elástica no modelo. Quais grandezas totais se conservam no sistema isolado?
- Em colisão inelástica de sistema isolado, qual grandeza necessariamente se conserva?
- Um corpo de 2 kg a 6 m/s colide e fica unido a outro de 4 kg parado. Qual velocidade comum
- Uma força resultante média de 10 N atua por 0,5 s. Qual módulo de impulso produz?
- Qual unidade SI corresponde à quantidade de movimento linear?
- Uma bola tem momento inicial +10 kg·m/s e recebe impulso −14 N·s. Qual momento final possu
- Durante uma colisão breve, o que justifica aproximar o momento total do sistema como conse
- Um objeto muda sua velocidade de +4 para −2 m/s, com massa 3 kg. Qual impulso resultante r
- Após uma colisão inelástica isolada, o que pode acontecer com a energia cinética total?
- Em uma dimensão, um corpo de 2 kg a +5 m/s colide com outro de 3 kg a −1 m/s. Qual momento
- Uma força constante de 6 N atua para a esquerda por 3 s. Tomando direita como positiva, qu
- Quantidade de movimento linear é uma grandeza vetorial com direção igual à da velocidade.
- Um corpo parado possui quantidade de movimento linear nula, ainda que tenha massa positiva
- A quantidade de movimento total do sistema é a soma vetorial dos momentos de seus componen
- Dois corpos que se movem em sentidos opostos têm momentos que devem ser somados como módul
- Para força constante, o módulo do impulso é o módulo da força vezes o intervalo de atuação
- O impulso resultante é igual à variação da quantidade de movimento do corpo.
- A área algébrica sob o gráfico força-tempo representa o impulso no intervalo considerado.
- Uma força de grande módulo sempre produz impulso maior do que qualquer força menor, indepe
- Se o impulso externo resultante é desprezível, o momento total do sistema pode ser tratado
- Forças internas podem alterar os momentos individuais, enquanto o momento total do sistema
- A conservação do momento linear exige que cada participante mantenha sua velocidade durant
- Uma colisão elástica conserva energia cinética total e momento linear total no sistema iso
- Em qualquer colisão inelástica isolada, o momento total se perde porque a energia cinética
- Na colisão perfeitamente inelástica, os participantes ficam juntos e passam a compartilhar
- Em uma colisão inelástica, a energia total mais ampla pode conservar-se enquanto a energia
- Para classificar colisão como elástica, basta verificar que o momento total se conserva.
- A unidade kg·m/s é equivalente a N·s para quantidade de movimento e impulso.
- Em uma dimensão, velocidades em sentidos opostos podem ser representadas por sinais difere
- O impulso da força resultante pode mudar o sentido do momento linear do corpo.
- A fronteira do sistema ajuda a decidir se uma força deve ser contabilizada como interna ou
```

## Pedido 40: Servidores de Minas Gerais: Estatuto (Lei nº 869/1952), carreiras de Defesa Social (Lei nº 15.301/2004) e Código de Conduta Ética (Decreto nº 46.644/2014) (`legislacao.servidores-minas-gerais-estatuto-carreiras-etica`, 37 tópicos de edital)

```
Matéria: legislacao.servidores-minas-gerais-estatuto-carreiras-etica (Servidores de Minas Gerais: Estatuto (Lei nº 869/1952), carreiras de Defesa Social (Lei nº 15.301/2004) e Código de Conduta Ética (Decreto nº 46.644/2014))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei nº 869/1952: posse, exercício, remoção e readaptação": 17
- "2. Tempo de serviço, frequência, estabilidade e direito de petição": 17
- "3. Deveres, proibições e processo administrativo": 17
- "4. Penalidades, competência, reabilitação e prescrição": 17
- "5. Lei nº 15.301/2004: carreiras do Grupo de Atividades de Defesa Social": 16
- "6. Código de Conduta Ética do Agente Público (Decreto nº 46.644/2014)": 16

Já existem (não repita):
- Segundo a Lei nº 869/1952, a posse deverá verificar-se no prazo de:
- Pela Lei nº 869/1952, há posse nos casos de promoção, remoção e reintegração.
- Na Lei nº 869/1952, a autoridade competente para dar exercício ao funcionário é:
- O funcionário designado para estudo ou aperfeiçoamento fora do Estado, com ônus para os co
- O funcionário preso por crime comum ou denunciado por crime funcional será afastado do exe
- Segundo a Lei nº 869/1952, a readaptação será sempre ex officio.
- Na Lei nº 869/1952, é considerado de efetivo exercício o afastamento do funcionário em vir
- Pela Lei nº 869/1952, o tempo de serviço prestado gratuitamente é computado para todos os 
- Embora o art. 187 da Lei nº 869/1952 previsse estabilidade após dois anos de exercício, a 
- Segundo a Lei nº 869/1952, a estabilidade diz respeito ao serviço público e não ao cargo, 
- No direito de petição previsto na Lei nº 869/1952, o pedido de reconsideração:
- Ao funcionário estudante, a Lei nº 869/1952 permite tolerância no comparecimento ao expedi
- Entre os deveres do funcionário previstos no art. 216 da Lei nº 869/1952, está:
- A Lei nº 869/1952 proíbe ao funcionário criticar, em qualquer circunstância, os atos da ad
- O inquérito administrativo previsto na Lei nº 869/1952 caracteriza-se por ser:
- Na Lei nº 869/1952, o processo administrativo é realizado por comissão composta de três fu
- Concluída a instrução do processo administrativo da Lei nº 869/1952, o acusado é citado pa
- A Lei nº 869/1952 proíbe expressamente ao servidor praticar assédio moral contra outro ser
- Segundo a Lei nº 869/1952, configura abandono de cargo, punível com demissão, o não compar
- Pela Lei nº 869/1952, a pena de suspensão não poderá exceder noventa dias, e o funcionário
- É causa de demissão a bem do serviço público, na Lei nº 869/1952:
- Segundo a Lei nº 869/1952, a aplicação das penas de repreensão e suspensão de até trinta d
- Na Lei nº 869/1952, as penas de repreensão, multa e suspensão prescrevem em dois anos, e a
- A reabilitação administrativa prevista na Lei nº 869/1952, que retira dos registros as ano
- O funcionário licenciado para tratamento de saúde que se dedicar a qualquer atividade remu
- Provada a boa-fé na acumulação ilegal de cargos, a Lei nº 869/1952 permite ao servidor opt
- Integra as carreiras do Grupo de Atividades de Defesa Social instituídas pela Lei nº 15.30
- Segundo a Lei nº 15.301/2004, a mudança de lotação de cargos e a transferência de servidor
- Na Lei nº 15.301/2004, a posição do servidor no escalonamento horizontal, dentro do mesmo 
- A cessão de servidor das carreiras da Lei nº 15.301/2004 para órgãos não previstos na lei,
- Pela Lei nº 15.301/2004, os ocupantes das carreiras de Técnico Assistente e de Analista da
- Para o Código de Conduta Ética do Agente Público de Minas Gerais, considera-se agente públ
- O Código de Conduta Ética de Minas Gerais permite ao agente público aceitar presentes de p
- O Conselho de Ética Pública (Conset) de Minas Gerais é composto por:
- As sanções previstas no Código de Conduta Ética do Agente Público de Minas Gerais são:
- Segundo o Decreto nº 46.644/2014, o exercício de apuração de falta ética prescreve em dois
- Ao deixar o cargo, a autoridade pública integrante da Alta Administração estadual deve obs
- Em cada órgão e entidade do Poder Executivo estadual há uma Comissão de Ética, composta po
- O agente público que fizer denúncia infundada ao Conset ou à Comissão de Ética não está su
- A autoridade pública da Alta Administração deve enviar ao Conset declaração sobre sua situ
```

## Pedido 41: Educação escolar indígena (`educacao.educacao-escolar-indigena`, 36 tópicos de edital)

```
Matéria: educacao.educacao-escolar-indigena (Educação escolar indígena)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos constitucionais, LDB e direitos educacionais": 17
- "Princípios e identidade da escola indígena": 17
- "Territórios etnoeducacionais e gestão participativa": 17
- "Línguas, professores indígenas e formação profissional": 17
- "Currículo, calendário, materiais e práticas interculturais": 16
- "Avaliação da aprendizagem e relação escola-comunidade": 16

Já existem (não repita):
- Ao ser constituído, um território etnoeducacional passa a exercer as competências de um no
- A LDB concebe a educação escolar indígena bilíngue e intercultural como instrumento exclus
- Ao organizar seu calendário, uma escola indígena deve preferencialmente:
- A escola indígena pode dialogar com conhecimentos científicos contemporâneos sem renunciar
- Uma política pública oferece recursos para formação docente e livros indígenas, mantendo p
- O caráter comunitário da escola indígena permite dispensar a participação das famílias e l
- Em uma comunidade que usa mais de uma língua, qual medida é mais consistente para organiza
- A Constituição assegura às comunidades indígenas, além do português no ensino fundamental 
- Uma secretaria afirma que só pode instituir território etnoeducacional dentro de um único 
- Quanto aos objetivos da LDB para os programas de educação escolar indígena bilíngue e inte
- Um grupo desenvolve um livro escolar com relatos de anciãos. Qual providência melhor respe
- A política de organização em territórios etnoeducacionais possui finalidade educacional e 
- Um instrumento de avaliação institucional de escola indígena, para ser coerente, deve:
- O ensino da história e das culturas indígenas no currículo geral de uma rede substitui a n
- Sobre a distinção entre educação indígena e educação escolar indígena, assinale a melhor a
- Uma avaliação exclusivamente em português pode medir também barreiras linguísticas, e não 
- A valorização das línguas indígenas impede que uma escola indígena organize atividades de 
- Livros e recursos didáticos produzidos com participação indígena podem incluir narrativas,
- O projeto político-pedagógico da escola indígena deve ser entendido como:
- Uma escola que tenha aula semanal de vocabulário indígena é, por esse único fato, necessar
- A presença de várias línguas em um território exige diagnóstico do contexto sociolinguísti
- Ao considerar a diversidade de línguas indígenas e suas diferentes situações de uso, uma r
- Num projeto intercultural sobre rios, qual estratégia pedagógica é mais pertinente?
- Qual combinação expressa de modo mais completo o princípio comunitário?
- Uma professora compara o resultado de prova escrita em português com apresentações bilíngu
- Uma escola elabora projetos com a língua local, define atividades com a comunidade e artic
- A formação de professores indígenas deve articular conhecimentos pedagógicos e científicos
- Qual decisão exemplifica mais claramente uma postura assimilacionista inadequada?
- A participação de professores e lideranças indígenas na avaliação institucional contribui 
- Os territórios etnoeducacionais podem compreender terras indígenas descontínuas e atravess
- Em formação continuada de professores indígenas, a proposta mais adequada consiste em:
- Ao comparar uma terra indígena e um território etnoeducacional, a afirmação correta é:
- A produção de um livro escolar autoriza a divulgação de qualquer conhecimento tradicional 
- Uma banca pede a interpretação correta da garantia constitucional de língua e aprendizagem
- Uma comunidade deseja ofertar estudos de matemática, português e conhecimentos históricos 
- Depois de ouvir a comunidade, uma escola reorganiza avaliação, calendário e materiais, ass
- A elaboração do PPP da escola indígena deve ser conduzida exclusivamente por consultoria e
- A avaliação diferenciada na escola indígena veda todo instrumento quantitativo, inclusive 
- A escola indígena diferenciada pode organizar práticas curriculares próprias, sem que isso
- A adequação do calendário escolar às atividades socioculturais permite suprimir qualquer e
```

## Pedido 42: Jurisprudência do STF em Direito Constitucional (`direito-constitucional.jurisprudencia-tribunais-superiores`, 35 tópicos de edital)

```
Matéria: direito-constitucional.jurisprudencia-tribunais-superiores (Jurisprudência do STF em Direito Constitucional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direitos fundamentais, remédios constitucionais, nacionalidade e direitos políticos": 20
- "2. Organização do Estado, federalismo e repartição constitucional de competências": 20
- "3. Organização dos Poderes: processo legislativo, medidas provisórias, CPIs e imunidades": 20
- "4. Poder Judiciário, CNJ, funções essenciais à Justiça e ordem social": 20
- "5. Controle de constitucionalidade no STF e como o tema costuma ser cobrado": 20

Já existem (não repita):
- Durante uma operação policial de rotina, agentes públicos realizaram a prisão em flagrante
- Em contrato de alienação fiduciária em garantia firmado entre instituição financeira e par
- Agentes de segurança pública ingressaram à noite, sem mandado judicial e sem consentimento
- No curso de procedimento fiscal regularmente instaurado para apurar omissão de receitas, a
- Um cidadão que cumpriu integralmente pena criminal há vinte anos ajuizou ação indenizatóri
- A dissolução da sociedade ou do vínculo conjugal por meio de divórcio consensual realizado
- Concedida a ordem em ação de mandado de segurança individual para anular ato administrativ
- A impetração de mandado de segurança coletivo por entidade de classe em favor de seus asso
- Por força da posição preferencial da liberdade de expressão e da vedação constitucional à 
- Um Município editou lei complementar contendo dois dispositivos: o artigo 1º fixou o horár
- Determinada Constituição Estadual foi emendada para criar novas hipóteses de crimes de res
- Um Estado da Federação estruturou, por meio de decreto e legislação organizacional própria
- Visando proteger os usuários de serviços de telefonia móvel e internet, uma Assembleia Leg
- A respeito da repartição constitucional de competências em matéria ambiental, urbanística 
- Assim como ocorre em relação às farmácias e aos supermercados, compete ao Município, por i
- É inconstitucional lei estadual que concede remissão ou anistia de multas e pontos na cart
- Diante da omissão do Congresso Nacional em editar a lei complementar federal determinadora
- Para que o Supremo Tribunal Federal dê provimento a pedido de intervenção federal em Estad
- Um Deputado Estadual apresentou projeto de lei dispondo sobre a concessão de adicional de 
- No curso dos trabalhos de uma Comissão Parlamentar de Inquérito (CPI) regularmente instala
- O Presidente da República encaminhou ao Congresso Nacional projeto de lei dispondo sobre a
- Um Deputado Federal, utilizando seu perfil pessoal em rede social durante período de féria
- Acerca do regime constitucional das Medidas Provisórias (art. 62 da CF) e da jurisprudênci
- As Comissões Parlamentares de Inquérito (CPIs), tanto no âmbito federal quanto no estadual
- Segundo a jurisprudência do Supremo Tribunal Federal fixada em repercussão geral (Tema 917
- Perante uma Comissão Parlamentar de Inquérito, tanto os investigados quanto as testemunhas
- O Poder Judiciário não pode impor medidas cautelares penais diversas da prisão a Deputados
- O Governador de um Estado nomeou sua irmã, economista doutora em finanças públicas e servi
- Servidores públicos de determinada carreira do Poder Executivo estadual ajuizaram ação jud
- Diante de graves indícios de infração funcional praticada por um Desembargador de Tribunal
- O Ministério Público instaurou procedimento investigatório criminal (PIC) para apurar crim
- A respeito da jurisprudência do Supremo Tribunal Federal sobre o direito constitucional à 
- As universidades públicas federais e estaduais, no exercício de sua autonomia didático-cie
- Segundo o Supremo Tribunal Federal (Tema 822), o ensino domiciliar (homeschooling) não con
- Por força do princípio da simetria constitucional com o art. 128, § 3º, da Constituição Fe
- O Supremo Tribunal Federal considera constitucional a percepção de honorários advocatícios
- A respeito da cláusula de reserva de plenário (art. 97 da Constituição Federal) e da Súmul
- Determinado Tribunal Regional proferiu acórdão contrariando frontalmente o teor de uma Súm
- A respeito da técnica de modulação temporal dos efeitos das decisões proferidas pelo Supre
- Sobre o regime jurídico-constitucional das Súmulas Vinculantes previsto no artigo 103-A da
- Analise a evolução jurisprudencial do Supremo Tribunal Federal acerca da Reclamação Consti
- Não viola a cláusula de reserva de plenário (art. 97 da Constituição Federal) nem a Súmula
- O efeito vinculante das decisões definitivas de mérito proferidas pelo STF em Ação Direta 
- Como a declaração de inconstitucionalidade de uma lei em controle concentrado produz, em r
- No controle preventivo judicial de constitucionalidade realizado pelo Supremo Tribunal Fed
```

## Pedido 43: História e Geografia do Espírito Santo (`espirito-santo.historia-geografia-espirito-santo`, 35 tópicos de edital)

```
Matéria: espirito-santo.historia-geografia-espirito-santo (História e Geografia do Espírito Santo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Povos originários, capitania e período colonial": 25
- "2. Século XIX: escravidão, café e imigração": 25
- "3. Século XX: crise do café e industrialização": 25
- "4. Geografia física e economia atual": 25

Já existem (não repita):
- A Capitania do Espírito Santo foi doada a Vasco Fernandes Coutinho, que fundou o primeiro 
- O nome Espírito Santo, dado à capitania, está associado:
- No litoral capixaba, à época da chegada dos portugueses, viviam povos do tronco tupi, como
- José de Anchieta viveu seus últimos anos no Espírito Santo e morreu na aldeia de Reritiba,
- Durante o ciclo do ouro em Minas Gerais, a política da Coroa portuguesa em relação ao Espí
- O Convento da Penha, em Vila Velha, é um importante marco religioso e cultural do período 
- O líder temiminó que se aliou aos portugueses na luta contra os franceses no Rio de Janeir
- A economia colonial capixaba baseou-se principalmente na mineração de ouro e diamantes no 
- A Insurreição de Queimados, ocorrida em 1849 na atual Serra, foi:
- No século XIX, o café expandiu-se no Espírito Santo a partir do sul da província, em áreas
- Os principais grupos de imigrantes europeus que formaram colônias no Espírito Santo no séc
- Santa Isabel e Santa Leopoldina estão entre os núcleos coloniais que receberam imigrantes 
- A imigração europeia no Espírito Santo, no século XIX, caracterizou-se pela formação de:
- Na Primeira República, a economia capixaba diversificou-se plenamente e deixou de depender
- Nos anos 1960, a política de erradicação de cafezais improdutivos provocou no Espírito San
- O complexo portuário de Tubarão está ligado principalmente à exportação de minério de ferr
- Entre os grandes projetos industriais instalados no Espírito Santo a partir dos anos 1960 
- O Espírito Santo produz petróleo e gás natural, inclusive em reservatórios da camada pré-s
- O rompimento da barragem de rejeitos em Mariana (MG), em 2015, afetou o Espírito Santo por
- A industrialização da Grande Vitória trouxe crescimento urbano acelerado, ocupação de área
- A política federal de erradicação dos cafezais nos anos 1960 estimulou, no Espírito Santo,
- O Espírito Santo faz limite com:
- A capital Vitória localiza-se em uma ilha, e a Região Metropolitana da Grande Vitória incl
- O Pico da Bandeira, um dos pontos mais altos do Brasil, localiza-se:
- No relevo capixaba, os tabuleiros são baixos planaltos sedimentares presentes sobretudo na
- O maior rio que atravessa o território do Espírito Santo é o:
- A vegetação original predominante no Espírito Santo é a Mata Atlântica, com restingas e ma
- O Espírito Santo destaca-se nacionalmente como maior produtor de café:
- Cachoeiro de Itapemirim e o sul do estado formam importante polo de extração e beneficiame
- São expressões culturais típicas do Espírito Santo:
- O clima predominante no Espírito Santo é o tropical, com temperaturas mais amenas nas área
- A economia capixaba atual apoia-se, entre outras atividades, em:
- Os rios Jucu e Santa Maria da Vitória são importantes para o abastecimento de água da Gran
- A primeira vila fundada pelos portugueses no Espírito Santo foi Vitória, já na ilha onde h
- A ocupação mais efetiva do interior capixaba, no século XIX, esteve associada principalmen
- A escravidão foi base da economia capixaba no período colonial e imperial, havendo quilomb
- A fábrica de celulose instalada no norte do Espírito Santo está associada a plantações de:
- A siderurgia capixaba, implantada na segunda metade do século XX, tem produção de aço conc
- Sobre o período colonial no Espírito Santo, é correto afirmar que a resistência de povos d
- O Espírito Santo localiza-se na Região Nordeste do Brasil, fazendo divisa com Sergipe ao n
```

## Pedido 44: Abordagens psicoterápicas (`psicologia.clinica-abordagens`, 34 tópicos de edital)

```
Matéria: psicologia.clinica-abordagens (Abordagens psicoterápicas)
Capítulos e quantas questões quero em cada um (total 100):
- "Matrizes teóricas, enquadre e escolha clínica": 17
- "Psicanálise e psicoterapias psicodinâmicas": 17
- "Terapia cognitivo-comportamental e vertentes contextuais": 17
- "Análise do comportamento e intervenções funcionais": 17
- "Gestalt-terapia e perspectivas humanistas": 16
- "Terapia sistêmica, familiar, breve e de grupo": 16

Já existem (não repita):
- Em reunião de equipe, a psicóloga afirma que escolher uma técnica exige explicitar a hipót
- Ao comparar escolas de psicoterapia, assinale a correspondência conceitual adequada.
- Uma pessoa deseja uma psicoterapia e pergunta qual escola é comprovadamente superior para 
- No Brasil, qual documento estabelece diretrizes e deveres específicos para psicólogas(os) 
- Uma paciente vê a psicoterapeuta como alguém que inevitavelmente a abandonará e reage ao s
- Um psicólogo percebe irritação recorrente ao atender uma pessoa e decide examinar o própri
- Uma questão afirma que todo silêncio ou discordância de paciente psicodinâmico comprova re
- A respeito das psicoterapias psicodinâmicas breves, assinale a opção correta.
- Durante reunião, um aluno se cala após pensar “se eu falar, todos rirão”. Na conceitualiza
- Qual procedimento da TCC procura testar no mundo uma previsão específica do paciente, em v
- Uma pessoa com sintomas depressivos deixa de realizar atividades antes valorizadas e passa
- Em um protocolo cognitivo-comportamental para obsessões e compulsões, o termo exposição co
- Na terapia de aceitação e compromisso (ACT), desfusão cognitiva consiste, principalmente, 
- No esquema A–B–C da análise do comportamento, a letra C designa:
- A pessoa adia uma tarefa difícil; a ansiedade diminui imediatamente e o adiamento torna-se
- Uma professora reforça inicialmente qualquer tentativa de leitura e, depois, apenas sequên
- Qual afirmação caracteriza adequadamente o behaviorismo radical?
- Um terapeuta conclui que um elogio foi reforçador apenas porque o paciente sorriu no momen
- Na Gestalt-terapia, o princípio do aqui-e-agora significa que o profissional:
- Ao descrever as condições facilitadoras rogerianas, a combinação correta é:
- Uma psicoterapeuta convida a paciente, se desejar, a imaginar diálogo com uma pessoa ausen
- No enfoque fenomenológico da Gestalt-terapia, qual conduta é mais coerente?
- Uma terapeuta questiona: “Quando o pai se cala, como a mãe responde e o que o filho faz em
- Em terapia familiar, qual instrumento focaliza vínculos e eventos entre gerações?
- O traço definidor de uma psicoterapia breve, independentemente da linha teórica, é:
- Sobre confidencialidade na psicoterapia de grupo, assinale a afirmação mais adequada.
- A aliança terapêutica envolve colaboração quanto a objetivos, tarefas e vínculo e pode ser
- A existência de recomendações da OMS para TCC na ansiedade significa que toda abordagem é 
- A Resolução CFP nº 13/2022 trata de diretrizes e deveres para a psicoterapia exercida por 
- Segundo o raciocínio psicodinâmico, transferência pode incluir hostilidade, medo, expectat
- A contratransferência, ao ser percebida, autoriza o terapeuta a satisfazer necessidades em
- Defesas psíquicas são necessariamente conscientes e fraudulentas, motivo pelo qual devem s
- Em TCC, crenças intermediárias podem assumir a forma de regras condicionais, como “se eu e
- Reestruturação cognitiva adequada consiste em ordenar que toda pessoa substitua pensamento
- Exposição com prevenção de resposta pode integrar protocolos para obsessões e compulsões, 
- Na ACT, aceitação e desfusão implicam desistir de agir sobre problemas sociais ou ambienta
- No condicionamento operante, reforçamento positivo e negativo se distinguem por adicionar 
- Se a retirada de um privilégio reduz a frequência de uma resposta, trata-se de punição neg
- A análise funcional pode atribuir a mesma função a comportamentos de aparência idêntica se
- Para o behaviorismo radical, eventos privados como pensamentos e sentimentos devem ser exc
- Na Gestalt-terapia, figura e fundo são categorias que se referem à organização dinâmica da
- Na abordagem centrada na pessoa, consideração positiva incondicional significa aprovar e i
- Um experimento de cadeira vazia pode ser proposto em trabalho gestáltico, mas não é obriga
- A causalidade circular, empregada em terapia sistêmica, demonstra que integrantes de uma r
- Uma psicoterapia breve pode ter matriz psicodinâmica, cognitivo-comportamental ou interpes
- Em psicoterapia de grupo, a construção de coesão, a aprendizagem interpessoal e o sentimen
```

## Pedido 45: Regime de Recuperação Fiscal e Equilíbrio Fiscal dos Estados (LC 159/2017 e LC 178/2021) (`direito-financeiro.regime-recuperacao-fiscal-estados`, 34 tópicos de edital)

```
Matéria: direito-financeiro.regime-recuperacao-fiscal-estados (Regime de Recuperação Fiscal e Equilíbrio Fiscal dos Estados (LC 159/2017 e LC 178/2021))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regime de Recuperação Fiscal (LC 159/2017): fundamentos, requisitos e adesão": 20
- "2. Plano de Recuperação, Conselho de Supervisão e vedações do RRF": 20
- "3. Programa de Acompanhamento (PATF) e Plano de Equilíbrio Fiscal (PEF) na LC 178/2021": 20
- "4. Impactos na LRF, limites de pessoal, endividamento e evoluções recentes (2024–2025)": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- A respeito dos entes federativos elegíveis ao Regime de Recuperação Fiscal (RRF) regido pe
- Com as alterações promovidas pela Lei Complementar nº 178/2021 na Lei Complementar nº 159/
- Nos termos do art. 6º da Lei Complementar nº 159/2017, o Conselho de Supervisão do Regime 
- Qual autoridade é competente para homologar o Plano de Recuperação Fiscal e dar início à v
- O Plano de Promoção do Equilíbrio Fiscal (PEF), disciplinado pela Lei Complementar nº 178/
- De acordo com a Lei Complementar nº 178/2021, qual é o prazo de vigência do Plano de Promo
- Após a alteração promovida pela Lei Complementar nº 178/2021 no art. 18, § 2º, da Lei de R
- Pela regra de transição do art. 15 da Lei Complementar nº 178/2021, os Poderes ou órgãos q
- Em questões de concursos públicos que avaliam o princípio da solidariedade entre os Podere
- O Regime de Recuperação Fiscal instituído pela Lei Complementar nº 159/2017 orienta-se, en
- Municípios com população superior a quinhentos mil habitantes que apresentem dívida consol
- As vedações previstas no art. 8º da Lei Complementar nº 159/2017 são absolutas, não admiti
- O Programa de Acompanhamento e Transparência Fiscal (PATF), instituído pela Lei Complement
- Na apuração da Despesa Total com Pessoal prevista na Lei de Responsabilidade Fiscal, é per
- Em provas de concursos públicos, considera-se correta a afirmação de que a redução extraor
- O Estado Delta pleiteia sua habilitação ao Regime de Recuperação Fiscal (LC 159/2017). No 
- Entre as medidas de ajuste estrutural previstas no art. 2º, § 1º, da Lei Complementar nº 1
- Durante a vigência do Regime de Recuperação Fiscal, determinado Estado necessita realizar 
- O art. 11 da Lei Complementar nº 159/2017 autoriza o Estado em Regime de Recuperação Fisca
- Considere um Município brasileiro com nota de Capacidade de Pagamento (CAPAG) C que deseja
- No que tange à contabilização das despesas com inativos e pensionistas na Despesa Total co
- Sobre o Programa de Pleno Pagamento de Dívidas dos Estados (Propag), instituído pela Lei C
- Em provas que abordam a jurisprudência do Supremo Tribunal Federal sobre responsabilidade 
- O deferimento do pedido de adesão ao Regime de Recuperação Fiscal pela Secretaria do Tesou
- Enquanto o Estado estiver elaborando seu Plano de Recuperação Fiscal após o deferimento do
- No Regime de Recuperação Fiscal, a redução extraordinária integral das prestações da dívid
- Durante o Regime de Recuperação Fiscal, é vedado o empenho ou a contratação de despesas co
- A adesão de um Estado ou Município ao Plano de Promoção do Equilíbrio Fiscal (PEF) da Lei 
- Enquanto o ente federativo estiver cumprindo a trajetória anual de redução de pelo menos d
- A Lei Complementar nº 206/2024 permitiu a suspensão por trinta e seis meses dos pagamentos
- Analise as proposições abaixo acerca das condições e exigências legais para a adesão e per
- O Ministério Público de um Estado submetido ao Regime de Recuperação Fiscal (LC 159/2017) 
- A Lei Complementar nº 159/2017 (após a reforma da LC 178/2021) diferencia tecnicamente as 
- Comparando o Regime de Recuperação Fiscal (LC 159/2017) com o Plano de Promoção do Equilíb
- A Lei Complementar nº 178/2021 promoveu importantes alterações na Lei de Responsabilidade 
- Um Estado da Federação encontra-se acima dos limites máximos da LRF tanto para a Despesa T
- No âmbito do Programa de Pleno Pagamento de Dívidas dos Estados (Propag — Lei Complementar
- Em provas de concursos da área de controle e finanças públicas, um erro recorrente dos can
- Para que um Estado seja considerado habilitado a aderir ao Regime de Recuperação Fiscal na
- O membro titular do Conselho de Supervisão do Regime de Recuperação Fiscal indicado pelo T
- O Estado em Regime de Recuperação Fiscal fica impedido de realizar leilões de pagamento de
- De acordo com as alterações promovidas pela Lei Complementar nº 178/2021 na Lei de Respons
- Em relação às vedações do art. 8º da Lei Complementar nº 159/2017, a concessão de vantagem
- Para que um Estado ingresse no Programa de Pleno Pagamento de Dívidas dos Estados (Propag 
- Segundo a jurisprudência do Supremo Tribunal Federal, o Poder Executivo de um Estado em Re
```

## Pedido 46: Jurisprudência dos Tribunais Superiores em Processo Penal (`processo-penal.jurisprudencia-tribunais-superiores`, 34 tópicos de edital)

```
Matéria: processo-penal.jurisprudencia-tribunais-superiores (Jurisprudência dos Tribunais Superiores em Processo Penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Investigação preliminar, juiz das garantias e acordo de não persecução penal": 20
- "2. Provas no processo penal: ilicitude, buscas, reconhecimento de pessoas, celular e cadeia de custódia": 20
- "3. Prisões cautelares, audiência de custódia, competência criminal e foro por prerrogativa": 20
- "4. Ação penal, nulidades, Tribunal do Júri, recursos e habeas corpus": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- Durante inquérito policial instaurado para apurar crime de estelionato, o advogado constit
- Um promotor de justiça conduziu diretamente Procedimento Investigatório Criminal (PIC), co
- Segundo a interpretação conforme à Constituição fixada pelo Supremo Tribunal Federal no ju
- No julgamento das Ações Diretas de Inconstitucionalidade que impugnaram dispositivos da Le
- Em ação penal por crime de receptação simples praticado em 2018, o réu permaneceu em silên
- Após a reforma promovida pela Lei 13.964/2019 e o julgamento da ADI 6.298 pelo STF, o arqu
- No julgamento conjunto das ADIs 2.943, 3.309 e 3.318, o Supremo Tribunal Federal reafirmou
- Nas investigações ou ações penais iniciadas após a proclamação do resultado do julgamento 
- Caso o membro do Ministério Público se recuse de maneira manifestamente infundada a propor
- Policiais militares em patrulhamento receberam uma ligação anônima informando que determin
- Na delegacia de polícia, para apurar a autoria de um crime de roubo, o investigador aprese
- É ilícita a prova obtida mediante o espelhamento de conversas telefônicas via aplicativo W
- Acerca da busca pessoal em via pública (art. 244 do CPP) e da atuação das guardas municipa
- Durante prisão em flagrante por crime de associação criminosa, os policiais apreenderam o 
- De acordo com a jurisprudência da Terceira Seção do STJ, qualquer inobservância formal nas
- Analise as proposições abaixo a respeito da jurisprudência dos Tribunais Superiores sobre 
- Segundo a jurisprudência do STJ, quando a vítima declara que já conhecia previamente o aut
- O acesso policial direto à agenda e aos registros de chamadas de aparelho celular encontra
- Recebendo auto de prisão em flagrante regular, o juiz realizou audiência de custódia na qu
- Um indivíduo apresentou carteira nacional de habilitação (CNH) falsificada — documento que
- Nos termos da Súmula Vinculante 11 do Supremo Tribunal Federal, o uso de algemas é excepci
- Durante o exercício do mandato e em razão direta de suas atribuições funcionais, um deputa
- A respeito da revisão periódica da prisão preventiva a cada 90 dias (art. 316, parágrafo ú
- Conforme a Súmula Vinculante 45 do STF, a competência constitucional do Tribunal do Júri p
- Considerando as súmulas do STF e do STJ sobre conexão, continência e competência da Justiç
- Segundo a jurisprudência do STF e do STJ, embora seja ilegal a conversão de ofício da pris
- Os crimes praticados por candidato durante a campanha eleitoral, antes da diplomação e da 
- Em relação à nulidade por ausência ou deficiência de defesa técnica no processo penal, o S
- Um auditor fiscal foi vítima de crime de difamação em razão direta de ato de fiscalização 
- Segundo a tese fixada pelo Supremo Tribunal Federal no Tema 1068 de Repercussão Geral, a s
- Após o recebimento da denúncia por crime de menor potencial lesivo à administração, o acus
- Acerca das hipóteses de cabimento do habeas corpus segundo as súmulas do Supremo Tribunal 
- Intimado pessoalmente da sentença condenatória, o réu assinou termo manifestando o desejo 
- No tocante ao procedimento do Tribunal do Júri, às citações por edital (art. 366 do CPP) e
- De acordo com a Súmula 701 do Supremo Tribunal Federal, no mandado de segurança impetrado 
- Pela Súmula Vinculante 35 do STF, a homologação da transação penal prevista no art. 76 da 
- Em uma questão de prova sobre violência doméstica e familiar contra a mulher, narra-se que
- Ao resolver questões práticas sobre competência criminal e corréus sem prerrogativa de for
- Em provas que cobram nulidades no julgamento de recursos, aplica-se a Súmula 708 do STF, s
- Em provas com estudos de caso sobre prisão domiciliar de mulheres gestantes ou mães de cri
- Quando uma questão de prova exige distinguir o regime de prescrição no processo suspenso (
- Em questões que relacionam processo penal e execução provisória ou faltas graves, é recorr
- Examine as assertivas a seguir, frequentemente exploradas em questões complexas sobre juri
- De acordo com a Súmula 526 do STJ, o reconhecimento de falta grave decorrente do cometimen
- Em provas que abordam a teoria da fonte independente (art. 157, § 2º, do CPP), considera-s
```

## Pedido 47: Técnica dietética, UAN e microbiologia de alimentos (`nutricao.tecnica-dietetica-uan`, 34 tópicos de edital)

```
Matéria: nutricao.tecnica-dietetica-uan (Técnica dietética, UAN e microbiologia de alimentos)
Capítulos e quantas questões quero em cada um (total 100):
- "Técnica dietética: operações, fatores e rendimento": 17
- "Planejamento de cardápios e fichas técnicas": 17
- "Gestão de UAN e alimentação coletiva": 17
- "Boas práticas, APPCC e rotulagem": 17
- "Microbiologia, tecnologia e conservação de alimentos": 16
- "PNAE: alimentação escolar e agricultura familiar": 16

Já existem (não repita):
- Uma cozinha precisa obter 16 kg de beterraba limpa; o fator de correção observado é 1,25. 
- Em teste de receita, 5 kg de arroz cru limpo produzem 13 kg após a cocção. Qual é o índice
- Uma preparação deverá render 30 kg de abóbora cozida; o índice de cocção é 0,75 e o fator 
- Assinale a alternativa que descreve corretamente duas transformações da técnica dietética.
- Em técnica dietética, o peso líquido é obtido após excluir do peso bruto as partes descart
- Um fator de correção de 1,25 indica perda de 25% do peso bruto do alimento.
- Para uma mesma preparação, o índice de cocção pode ser inferior a um quando perdas de água
- A caramelização e a reação de Maillard são termos sinônimos para um único processo que exi
- Qual instrumento padroniza ingredientes, técnica, rendimento, porção e custo de uma prepar
- Durante o almoço, 8 kg de preparação ficaram na cozinha sem distribuição e 4 kg retornaram
- Um buffet dispõe de 300 clientes previstos, per capita de 90 g de feijão cru limpo e preço
- Qual decisão melhora a sustentabilidade do cardápio sem desprezar segurança, aceitabilidad
- A Ficha Técnica de Preparação favorece a reprodução da receita por equipes diferentes ao e
- Restos alimentares devolvidos pelos consumidores podem ser incorporados ao lote de sobra l
- A avaliação da aceitabilidade de um cardápio pode utilizar registros de consumo e de resto
- O per capita de arroz cru limpo deve ser multiplicado pelo índice de cocção antes de calcu
- Dois lotes de iogurte foram recebidos em datas diferentes, mas o mais recente vence antes.
- Qual organização de fluxo físico-funcional tende a reduzir contaminação cruzada em UAN?
- Uma UAN compra R$ 30 mil de gêneros no mês, mas utiliza R$ 25 mil do estoque na produção e
- Em contrato de terceirização de alimentação coletiva, qual dever permanece com a instituiç
- A regra PVPS prioriza o lote cuja validade vence antes, mesmo que tenha sido recebido depo
- A contratação de uma empresa terceirizada extingue o dever da instituição de acompanhar a 
- Comparar refeições produzidas por trabalhador entre duas UANs exige considerar diferenças 
- O custo dos alimentos consumidos no mês é sempre igual ao valor de compras realizadas naqu
- Qual documento registra instruções sequenciais de higienização de uma superfície, com resp
- Em um plano APPCC, a verificação de que um monitoramento detectou temperatura abaixo do li
- Qual alternativa apresenta a correspondência normativa correta para rotulagem nutricional 
- A equipe classificou como PCC a etapa de tratamento térmico capaz de controlar um patógeno
- As boas práticas e a higiene operacional são pré-requisitos para a implantação eficaz do s
- No APPCC, risco e perigo são sinônimos e ambos significam apenas a probabilidade de contam
- A RDC Anvisa 591/2021 é um dos principais atos normativos de rotulagem nutricional de alim
- Registrar um limite crítico em papel, sem monitorá-lo, é suficiente para demonstrar contro
- Qual afirmação sobre refrigeração de alimentos é tecnicamente adequada?
- Arroz cozido permaneceu por horas em resfriamento lento antes de ser acondicionado. Qual m
- Na análise de estabilidade de alimento desidratado, qual parâmetro expressa a disponibilid
- Uma conserva de baixa acidez foi fechada hermeticamente sem tratamento adequado. Qual peri
- A ausência de odor desagradável em um alimento é prova suficiente de que ele não contém ag
- Listeria monocytogenes merece atenção em alimentos prontos refrigerados porque pode multip
- A pasteurização corretamente realizada garante destruição de todos os esporos bacterianos 
- A contaminação por norovírus pode causar doença mesmo que o vírus não se multiplique no al
- Em 2026, qual é o percentual mínimo dos recursos federais do PNAE destinado a compras da a
- Qual afirmação descreve corretamente a distribuição de recursos de compra por grau de proc
- Em escola do PNAE com funcionamento parcial, a oferta mínima semanal de frutas in natura, 
- Qual medida é compatível com os recursos federais do PNAE e com o modelo de controle socia
- O PNAE envolve fornecimento de alimentação escolar e ações de educação alimentar e nutrici
- A exigência mínima de 45% de compras da agricultura familiar e o mínimo de 85% de alimento
- Na alimentação escolar de período integral, a resolução FNDE de 2026 exige frequência maio
- O CAE substitui a atuação do nutricionista responsável técnico na elaboração das Fichas Té
```

## Pedido 48: Lei Orgânica Nacional das PMs e CBMs (`legislacao.lei-organica-nacional-pm-bm`, 34 tópicos de edital)

```
Matéria: legislacao.lei-organica-nacional-pm-bm (Lei Orgânica Nacional das PMs e CBMs)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais, natureza e princípios": 16
- "2. Competências, atuação integrada e limites": 16
- "3. Organização, quadros, ingresso e formação": 16
- "4. Garantias, vedações e disciplina militar": 16
- "5. Mobilização, transição e Decreto-Lei nº 667/1969": 36

Já existem (não repita):
- Uma equipe revisa o fundamento constitucional da lei nacional de organização das instituiç
- Um gestor sustenta que, por serem forças auxiliares e reserva do Exército, as corporações 
- Após ler os arts. 3º e 4º da Lei 14.751/2023, uma equipe precisa classificar a previsão de
- Durante planejamento de uma ação ambiental, surge dúvida sobre quais sistemas nacionais in
- Uma corporação militar é convidada a realizar formação de natureza militar para integrante
- Uma instituição planeja patrulhamento ostensivo e ações de preservação da ordem pública. Q
- Em local atingido por incêndio com possível origem criminosa, a equipe pretende realizar a
- Um regulamento apresenta cinco categorias de órgãos para estrutura básica preferencial de 
- Uma pessoa aprovada para a carreira militar apresenta tatuagem visível com desenho floral 
- Uma instituição revisa seu cronograma de implantação da exigência de escolaridade superior
- Uma proposta altera a organização da polícia militar do Distrito Federal. Qual iniciativa 
- Uma comissão descreve o Quadro de Oficiais Especialistas (QOE) e sua progressão na Lei 14.
- Um militar de carreira com quatro anos de efetivo serviço é removido de sua unidade após o
- Um militar da ativa pretende adquirir participação minoritária em sociedade comercial sem 
- Após nomeação, o novo comandante-geral prepara um plano com metas, indicadores, prestação 
- Uma autoridade administrativa cogita divulgar em rede social imagens de pessoa sob custódi
- Ao revisar a legislação de 1969, uma pessoa conclui que a Lei 14.751/2023 extinguiu comple
- Duas corporações pretendem firmar cooperação interinstitucional. Uma delas propõe ceder in
- Um oficial pertencente ao quadro de praças ingressou posteriormente no Quadro de Oficiais 
- Uma autoridade explica o vínculo federal das corporações militares estaduais. Afirma que a
- Uma operação envolve perícia de incêndio, e o local ainda não foi liberado pela perícia cr
- Um militar deseja passar para a inatividade remunerada a pedido com remuneração integral, 
- Ao preparar revisão normativa, um analista encontra referência a prisão disciplinar e à ve
- Um militar convocado para atuação em outra unidade da Federação responde a processo por cr
- Uma revisão identifica o dispositivo pelo qual a Lei 14.751/2023 revogou expressamente art
- Um grupo de militares afirma que o dever de observar a hierarquia não elimina os princípio
- Uma equipe apura um crime comum sem relação com infração militar e afirma que o art. 5º da
- Durante planejamento de atendimento a desastre, a autoridade considera que a execução de a
- Na organização de uma corporação, a unidade de logística, saúde e orçamento, responsável p
- A elaboração da lei de organização de uma corporação militar estadual deve observar as nor
- Um candidato apresenta tatuagem visível com desenho geométrico sem apologia à violência ne
- Uma unidade federativa conta o prazo de seis anos para implantar a exigência de escolarida
- A perícia administrativa de incêndio realizada pelos bombeiros, em situação normal de comp
- Um integrante da própria corporação deseja prestar concurso para ingresso no Quadro de Ofi
- Uma proposta de plano de carreira estabelece que o Quadro de Oficiais Especialistas tem pr
- Ao analisar o direito a seguro de vida e acidentes ou indenização ao militar vitimado em s
- Para determinar a estabilidade de um militar de carreira, um parecer aplica o prazo de um 
- O governador pode nomear comandante-geral entre oficiais da ativa do último posto do QOEM 
- Uma apuração interna invoca o art. 18 do Decreto-Lei 667/1969, na redação da Lei 13.967/20
- Ao reexaminar pensão e inatividade militar, uma unidade federativa desconsidera os arts. 2
- Um planejamento federal prevê apoio de corporação militar estadual a órgão da União por co
- Uma situação de resgate em enchente é tratada como atribuição privativa dos bombeiros mili
- Para examinar pedido de inatividade, uma comissão identifica que o militar havia preenchid
- A regra geral do art. 24-A, I, a, do Decreto-Lei 667/1969 permite, sem exame de transição,
- Militar de determinada unidade federativa foi mobilizado e, nas circunstâncias previstas n
```

## Pedido 49: Passadiço, serviço de quarto, manobra do navio, meteorologia e marés (`marinha-mercante.passadico-manobra-meteorologia`, 34 tópicos de edital)

```
Matéria: marinha-mercante.passadico-manobra-meteorologia (Passadiço, serviço de quarto, manobra do navio, meteorologia e marés)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Serviço de quarto e gerenciamento do passadiço": 37
- "2. Manobra do navio: leme, hélice e efeitos": 23
- "3. Atracação, fundeio e reboque": 23
- "4. Meteorologia e oceanografia": 17

Já existem (não repita):
- O oficial de quarto é o representante do comandante e responde pela navegação segura do na
- Se, no momento da troca de quarto, o navio estiver executando manobra para evitar perigo, 
- À noite, o oficial que vai assumir o quarto deve estar com a visão adaptada à escuridão an
- Com o prático a bordo, a responsabilidade pela segurança do navio é transferida integralme
- NÃO é situação em que o oficial de quarto deve chamar o comandante:
- No gerenciamento de recursos do passadiço, a comunicação em circuito fechado consiste em:
- As quatro etapas do plano de viagem são avaliação, planejamento, execução e monitoramento.
- O embarque do prático pela escada de prático deve ser feito, em regra, pelo bordo:
- O leme só é eficaz quando há fluxo de água passando por ele, seja pelo seguimento do navio
- Em um navio de hélice de passo direito, ao dar máquinas atrás, a tendência é:
- O hélice de passo direito gira no sentido horário, visto de ré, quando o navio dá máquinas
- Na curva de giro, o deslocamento lateral do navio no momento em que completa uma guinada d
- O squat, afundamento do navio em águas rasas ao navegar, aumenta aproximadamente com o qua
- Ao navegar próximo à margem de um canal estreito, o efeito de margem tende a:
- Em águas rasas, o navio responde mais lentamente ao leme e seu diâmetro de giro aumenta.
- A interação entre dois navios que se cruzam ou se ultrapassam próximos em um canal pode pr
- O impelidor de proa (bow thruster) auxilia a manobra lateral da proa em baixas velocidades
- Na escolha de um fundeadouro, é considerado o tipo de fundo que oferece melhor tensa (agar
- Com previsão de mau tempo no fundeadouro, deve-se aumentar o filame (comprimento de amarra
- Na manobra de fundeio, o ferro deve ser largado quando o navio:
- Com o navio fundeado, o oficial de quarto dispensa a verificação da posição, já que o ferr
- Ao atracar, se possível, o navio deve receber a corrente:
- No reboque em mar aberto, o cabo de reboque deve ter comprimento suficiente para formar ca
- Uma manobra comum na desatracação de um navio de hélice único é:
- O uso do ferro de manobra, largado com pouco filame, pode auxiliar o controle da proa em a
- No hemisfério sul, os ventos em torno de um centro de baixa pressão (ciclone) circulam no 
- Pela lei de Buys-Ballot, no hemisfério sul, um observador de costas para o vento tem a bai
- Uma queda rápida da pressão atmosférica indicada pelo barômetro normalmente anuncia:
- No litoral brasileiro, as frentes frias costumam avançar de sul ou sudoeste para norte ou 
- A escala Beaufort classifica:
- O nevoeiro mais comum no mar, formado quando ar quente e úmido passa sobre água mais fria,
- No psicrômetro, quando os termômetros de bulbo seco e de bulbo úmido indicam a mesma tempe
- O instrumento que mede a velocidade do vento é o:
- As marés de maior amplitude, chamadas de águas vivas, ocorrem nas luas:
- Na maré semidiurna ocorrem duas preamares e duas baixa-mares por dia, com intervalo aproxi
- O estofo de maré corresponde ao período em que:
- A Corrente do Brasil é uma corrente fria que flui para o norte ao longo da costa brasileir
- A altura da maré prevista nas Tábuas de Marés deve ser somada à profundidade indicada na c
- O vento sentido a bordo de um navio em movimento, resultante do vento verdadeiro e do desl
- Durante o dia, em regiões costeiras, a brisa que sopra do mar para a terra chama-se:
```

## Pedido 50: RBAC, Regras do Ar, Radiocomunicações e CRM (`aviacao.rbac-regras-ar-crm`, 33 tópicos de edital)

```
Matéria: aviacao.rbac-regras-ar-crm (RBAC, Regras do Ar, Radiocomunicações e CRM)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Mapa dos RBAC": 25
- "2. Regras do ar e serviços de tráfego aéreo": 25
- "3. Radiocomunicações, sinais e falhas": 25
- "4. CRM: recursos na cabine": 25

Já existem (não repita):
- Os Regulamentos Brasileiros da Aviação Civil (RBAC) são editados pela ANAC.
- O RBAC que trata de licenças, habilitações e certificados para pilotos é o:
- O RBAC 91 estabelece requisitos gerais de operação aplicáveis às aeronaves civis em geral,
- Uma empresa de táxi aéreo que opera helicópteros no transporte aéreo público tem suas oper
- O RBAC 119 trata da certificação de operadores de transporte aéreo público, que, uma vez c
- O gerenciamento do risco de fadiga humana, com limites de jornada e sistemas de gerenciame
- O RBAC 120 dispõe sobre a prevenção do uso indevido de substâncias psicoativas na aviação 
- Sobre os RBAC 141 e 142, assinale a alternativa correta.
- O transporte de artigos perigosos em aeronaves civis é disciplinado pelo RBAC 175.
- Segundo o RBAC-E 94, uma aeronave remotamente pilotada com peso máximo de decolagem de 20 
- No RBAC-E 94, a operação além da linha de visada visual do piloto remoto é designada pela 
- As licenças e habilitações de comissários e mecânicos de voo são tratadas pelo RBAC 63, e 
- O regulamento aplicável às empresas estrangeiras que realizam transporte aéreo internacion
- As regras do ar e os serviços de tráfego aéreo no Brasil, como os da ICA 100-12, são edita
- Duas aeronaves motorizadas voam em rotas convergentes, aproximadamente no mesmo nível. Dev
- Quando duas aeronaves se aproximam de frente, com risco de colisão, ambas devem alterar o 
- Na ordem de preferência de passagem entre tipos de aeronaves, a que tem prioridade sobre t
- Entre duas aeronaves que se aproximam para pouso no mesmo aeródromo, tem prioridade a que 
- Sobre as regras de voo visual (VFR) e por instrumentos (IFR), é correto afirmar:
- O serviço de tráfego aéreo destinado a notificar os órgãos apropriados quando uma aeronave
- Os NOTAM divulgam informações aeronáuticas de caráter temporário ou urgente, enquanto a AI
- O piloto em comando pode desviar-se das regras do ar em situação de emergência, na medida 
- O código transponder a ser selecionado em caso de falha de comunicações é:
- O código transponder 7500 indica emergência geral a bordo, como pane de motor.
- A mensagem iniciada por MAYDAY, repetida três vezes, indica:
- Uma aeronave em voo, sem rádio, recebe da torre luz verde contínua. O significado é:
- Para uma aeronave no solo, a luz vermelha intermitente emitida pela torre significa afaste
- Em falha de comunicações em condições meteorológicas visuais, a conduta do piloto inclui:
- Com falha de recepção, o piloto pode transmitir às cegas suas posições e intenções, pois o
- Ao observar um acidente com outra aeronave, o piloto deve, sempre que possível, manter a v
- O Crew Resource Management (CRM) pode ser definido como:
- A consciência situacional envolve perceber os elementos do ambiente, compreender seu signi
- Um gradiente de autoridade muito acentuado na cabine tende a:
- No CRM, a assertividade do copiloto, ao expor uma dúvida sobre a segurança mesmo diante do
- No método de checklist desafio-e-resposta (challenge and response):
- No gerenciamento de ameaças e erros (TEM), uma ameaça é um evento que ocorre fora da influ
- Em situação de alta carga de trabalho, a priorização de tarefas recomendada na cabine é:
- Estresse, fadiga e distração reduzem o desempenho da tripulação, e uma das respostas do CR
- No ciclo de tomada de decisão ensinado em CRM, após escolher e executar uma opção, a tripu
- O RBAC 60 trata da qualificação e do uso de dispositivos de treinamento para simulação de 
```

