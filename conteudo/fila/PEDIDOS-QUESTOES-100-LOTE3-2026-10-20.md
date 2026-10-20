# Pedidos de questões extras, lote 3 (20/10/2026: próximas 50 matérias mais usadas)

Mesmo esquema dos lotes 1 e 2. A Mensagem 0 ganhou duas regras que o lote 2 precisou de correção: **sem análise por letra dentro da explicação** e **C/E sem copiar alternativa**. Use uma conversa nova (Claude ou Gemini), cole a Mensagem 0 e depois um pedido por mensagem. Salve como `<id>.questoes.json`.

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

## Pedido 1: Psicologia da Aprendizagem e Didática (`educacao.psicologia-aprendizagem-didatica`, 1080 tópicos de edital)

```
Matéria: educacao.psicologia-aprendizagem-didatica (Psicologia da Aprendizagem e Didática)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Behaviorismo e Gestalt": 20
- "2. Piaget: epistemologia genética": 20
- "3. Vygotsky, Wallon, Ausubel e Morin": 20
- "4. Didática: planejamento, objetivos e relação professor-aluno": 20
- "5. Avaliação da aprendizagem e dos sistemas": 20

Já existem (não repita):
- No condicionamento operante de Skinner, o reforço negativo:
- O condicionamento clássico, estudado por Pavlov, ocorre quando um estímulo neutro, pareado
- A pedagogia tecnicista, com instrução programada, objetivos comportamentais e reforço imed
- Para a psicologia da Gestalt, a aprendizagem por insight caracteriza-se pela compreensão s
- São leis da percepção estudadas pela Gestalt, EXCETO:
- Na teoria de Piaget, o processo pelo qual o sujeito modifica seus esquemas para dar conta 
- Para Piaget, a equilibração é o fator que coordena os demais fatores do desenvolvimento (m
- Uma criança de cinco anos afirma que há mais água no copo alto e fino do que no baixo e la
- O pensamento hipotético-dedutivo, capaz de operar com proposições abstratas, caracteriza, 
- Sobre a teoria de Piaget, assinale a correta.
- Para Piaget, o desenvolvimento moral da criança evolui da heteronomia, em que as regras sã
- A zona de desenvolvimento proximal, conceito de Vygotsky, corresponde à distância entre:
- Para Vygotsky, a aprendizagem impulsiona o desenvolvimento, e as funções psicológicas supe
- Na perspectiva sócio-histórica, o papel do professor é, sobretudo, o de:
- Vygotsky distingue conceitos espontâneos, formados na experiência cotidiana, de conceitos 
- O teórico que destacou a afetividade e a emoção como dimensões centrais do desenvolvimento
- Na aprendizagem significativa de Ausubel, a nova informação relaciona-se de forma não arbi
- São condições para a aprendizagem significativa, segundo Ausubel:
- Entre os sete saberes necessários à educação do futuro, propostos por Edgar Morin, NÃO se 
- Abordagens atuais sobre o fracasso escolar, como a de Maria Helena Souza Patto, atribuem o
- Sobre planejamento e plano, é correto afirmar:
- O projeto político-pedagógico é construção coletiva da comunidade escolar, sendo político 
- Na versão revisada da taxonomia de Bloom, as categorias do domínio cognitivo, da mais simp
- A taxonomia de Bloom organiza os objetivos educacionais nos domínios cognitivo, afetivo e 
- No modelo de sequência didática proposto por Dolz e Schneuwly para o ensino de gêneros tex
- Paulo Freire critica a educação bancária, na qual o professor deposita conteúdos no aluno,
- Segundo Libâneo, a relação professor-aluno na sala de aula envolve, fundamentalmente, aspe
- O professor que, no primeiro dia de aula, aplica uma atividade para identificar o que os a
- A avaliação formativa ocorre ao longo do processo de ensino e tem como principal função fo
- Para Cipriano Luckesi, a diferença entre avaliar e examinar está em que:
- A LDB determina que a verificação do rendimento escolar observe avaliação contínua e cumul
- O portfólio, como instrumento de avaliação, caracteriza-se por:
- O IDEB combina dois indicadores: o fluxo escolar, medido pela taxa de aprovação, e o desem
- A avaliação institucional da escola:
- A avaliação somativa, por ocorrer ao final de uma etapa, é incompatível com qualquer uso p
- Um professor elabora uma rubrica para a avaliação de um seminário. Esse instrumento:
- Kurt Lewin, associado à tradição da Gestalt, propôs a teoria de campo, segundo a qual o co
- No estágio sensório-motor descrito por Piaget, uma conquista fundamental da criança é:
- Para Wallon, a emoção é a primeira forma de comunicação do bebê com o meio, antes mesmo da
- Os materiais introdutórios apresentados antes do conteúdo novo, para servir de ponte entre
```

## Pedido 2: Ordem social na Constituição (`direito-constitucional.ordem-social`, 1063 tópicos de edital)

```
Matéria: direito-constitucional.ordem-social (Ordem social na Constituição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e financiamento da ordem social": 18
- "2. Saúde, previdência e assistência social": 17
- "3. Educação, cultura e desporto": 25
- "4. Ciência, tecnologia, inovação e comunicação social": 17
- "5. Meio ambiente, família e direitos indígenas": 23

Já existem (não repita):
- Em um plano de políticas sociais, uma equipe indicou que a ordem social teria como base a 
- Uma pessoa sem histórico contributivo procura proteção pública por necessidade de atendime
- Uma lei publicada em 20 de novembro institui nova contribuição de seguridade social. Sem r
- Um município redesenha sua rede de saúde e propõe priorizar vacinação e prevenção sem exti
- Uma entidade privada sem fins lucrativos pretende atender usuários do SUS por convênio, en
- Uma pessoa empregada e segurada do RGPS pergunta por que seu regime previdenciário recolhe
- Uma pessoa idosa em situação de necessidade solicita o benefício constitucional assistenci
- Ao definir a matrícula obrigatória e gratuita, uma rede de ensino inclui estudantes de dif
- Na elaboração de proposta para financiamento educacional, uma equipe compara os pisos cons
- Uma família questiona a presença de ensino religioso nos horários normais de uma escola pú
- Uma comunidade pede proteção a uma tradição de música e dança transmitida oralmente entre 
- Uma associação disputa resultado de competição esportiva e pretende recorrer imediatamente
- Um município deseja estimular debate em um jornal impresso e recebe a sugestão de condicio
- Em projeto tecnológico, um Estado compartilhará laboratório com uma entidade privada, que 
- Uma atividade industrial proposta tem potencial técnico de causar degradação ambiental sig
- Um casal formado por pessoas do mesmo sexo pede reconhecimento de sua união estável como e
- Durante procedimento de demarcação, uma comunidade indígena ouve que seus direitos territo
- Um gestor pretende utilizar recursos anuais do Fundeb para remunerar profissionais da educ
- Uma proposta legislativa aumenta uma prestação de seguridade sem identificar recursos para
- Uma pessoa avalia plano de previdência complementar privada, diferente de sua proteção obr
- Uma transferência de receita de impostos sai da União e integra o orçamento de um Municípi
- Uma emissora de televisão tem concessão vigente, e uma autoridade propõe cancelá-la antes 
- Uma associação sustenta que todos os imóveis particulares situados na Mata Atlântica perte
- Uma empresa propõe pesquisa e lavra mineral em terra indígena, oferecendo remuneração à co
- Após uma condenação administrativa por poluição, uma pessoa jurídica alega que o pagamento
- Uma pessoa não segurada do RGPS procura uma unidade pública de saúde. O atendimento univer
- Ao organizar políticas de seguridade, um grupo propõe substituir a gestão quadripartite po
- Uma família solicita vaga escolar obrigatória e gratuita para criança em idade constitucio
- Um coletivo cultural pede reconhecimento de seu modo tradicional de fazer artesanato. A so
- Uma rádio solicita concessão com prazo de quinze anos, justificando que esse é o período c
- Um grupo de moradores realiza ações de preservação de uma área verde e invoca o art. 225 p
- Uma entidade beneficente certificada conforme a legislação específica pretende usufruir da
- Uma clínica privada contratada para oferecer serviço complementar no SUS pode assumir, por
- Uma escola particular apresenta seu projeto educacional à autoridade competente. Sua liber
- Uma entidade desportiva afirma que a justiça desportiva integra o Poder Judiciário e, por 
- Um projeto reúne universidade pública, laboratório estadual e entidade privada para desenv
- No reconhecimento de uma família monoparental, exige-se casamento anterior entre os genito
- Uma pessoa de 67 anos solicita gratuidade em transporte coletivo urbano. O art. 230, § 2º,
- Uma prestação pecuniária previdenciária de caráter não substitutivo do salário de contribu
- No encerramento da transição constitucional em 2026, a complementação mínima da União aos 
- Uma empresa brasileira de radiodifusão pode cumprir o art. 222 mantendo 70% apenas de seu 
- Uma proposta pretende reduzir os limites de um espaço territorial especialmente protegido.
- Uma comunidade indígena pode ser removida definitivamente de terra tradicional sempre que 
- Uma organização indígena ajuíza ação em defesa de direitos comunitários. O art. 232 exige 
- Em escola de ensino fundamental que atende comunidade indígena, a regra constitucional de 
```

## Pedido 3: Princípios fundamentais (arts. 1º a 4º) (`direito-constitucional.principios-fundamentais`, 1056 tópicos de edital)

```
Matéria: direito-constitucional.principios-fundamentais (Princípios fundamentais (arts. 1º a 4º))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Preâmbulo, República, Federação e Estado Democrático de Direito": 23
- "2. Os cinco fundamentos: soberania, cidadania, dignidade, trabalho e pluralismo": 23
- "3. Titularidade popular do poder e separação funcional dos Poderes": 18
- "4. Objetivos fundamentais: solidariedade, desenvolvimento e não discriminação": 17
- "5. Princípios das relações internacionais e integração latino-americana": 19

Já existem (não repita):
- Uma norma estadual reproduz direitos constitucionais, mas omite no seu preâmbulo a express
- Um estudo distingue a República da Federação brasileira. Qual caracterização associa corre
- Uma pessoa deseja listar apenas os fundamentos expressos do art. 1º. Qual item integra ess
- Um agente político afirma que o Estado Democrático de Direito permite atos contrários à Co
- Uma campanha educativa pretende explicar de quem emana o poder político e como se exerce c
- Uma comissão descreve Legislativo, Executivo e Judiciário como instituições submetidas ent
- Uma proposta pública cita 'erradicar a pobreza e a marginalização e reduzir as desigualdad
- Em um debate sobre a política externa brasileira, é solicitado um exemplo literal de princ
- Uma aula distingue pluralismo político de pluripartidarismo. Considerando o art. 1º, V, e 
- Uma entidade federativa declara-se soberana para deixar de cumprir Constituição federal, s
- Um gestor sustenta que a dignidade constitucional é atribuída somente aos cidadãos que par
- Uma reforma administrativa pretende concentrar todas as atividades decisórias em único Pod
- Uma política visa reduzir disparidades territoriais de acesso a serviços, sem formular pro
- Um protocolo de cooperação entre países latino-americanos abrange educação, comércio, inte
- Um intérprete precisa distinguir as expressões 'soberania' e 'independência nacional'. Con
- Em processo de elaboração de ato público, uma autoridade afirma que 'harmonia entre Podere
- Uma autoridade propõe inserir no preâmbulo estadual a mesma frase religiosa do preâmbulo f
- Uma instituição pretende negar inscrição política a determinado grupo porque suas opiniões
- Em uma revisão sobre valores constitucionais, surge a tese de que a dignidade apenas repro
- Uma proposta de reorganização do Estado pretende eliminar controle recíproco entre Legisla
- Uma proposta menciona 'promover o bem de todos, sem preconceitos de origem, raça, sexo, co
- Em conferência diplomática, uma delegação afirma que um Estado soberano deve ter status ju
- O Brasil busca negociar solução não violenta de conflito entre dois países, sem renunciar 
- Uma proposta de integração da América Latina restringe compromisso brasileiro ao comércio 
- Uma consultoria utiliza a finalidade de 'garantir o desenvolvimento nacional' para afirmar
- A expressão 'sob a proteção de Deus', no preâmbulo federal, foi considerada pelo STF desti
- Os Municípios brasileiros, por integrarem a República Federativa do Brasil, detêm soberani
- Soberania, cidadania, dignidade da pessoa humana, valores sociais do trabalho e da livre i
- O fundamento do pluralismo político, no art. 1º, V, equivale apenas ao registro de múltipl
- O art. 1º, parágrafo único, admite que o povo exerça poder por representantes eleitos ou d
- A harmonia entre Legislativo, Executivo e Judiciário, prevista no art. 2º, significa que n
- Construir sociedade livre, justa e solidária e garantir o desenvolvimento nacional são obj
- A indissolubilidade da Federação prevista no art. 1º significa que a Constituição reconhec
- O reconhecimento do valor social do trabalho e da livre iniciativa no art. 1º, IV, exige l
- A cidadania, fundamento do art. 1º, II, só se manifesta durante eleições, razão pela qual 
- A separação entre os Poderes admite que cada um desempenhe funções típicas e competências 
- O art. 3º, III, exige erradicação de toda desigualdade social e regional, mas apenas reduç
- Promover o bem de todos sem preconceitos de origem, raça, sexo, cor, idade e outras discri
- Independência nacional, autodeterminação dos povos e não intervenção são três fundamentos 
- A dignidade da pessoa humana é fundamento constitucional do art. 1º, III, e dialoga com a 
- O princípio republicano, por exigir responsabilidade e eletividade, determina que toda fun
- A Constituição resguarda o princípio da separação dos Poderes contra proposta de emenda te
- A previsão de concessão de asilo político no art. 4º, X, obriga a aprovação automática de 
- O repúdio ao terrorismo e ao racismo, a cooperação entre os povos para o progresso da huma
- O parágrafo único do art. 4º determina exclusivamente integração comercial com povos latin
```

## Pedido 4: Geografia e História de Mato Grosso (`mato-grosso.conhecimentos-estaduais`, 1034 tópicos de edital)

```
Matéria: mato-grosso.conhecimentos-estaduais (Geografia e História de Mato Grosso)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Território, biomas, relevo, clima e hidrografia": 25
- "2. População, urbanização, espaço agrário e economia": 25
- "3. Da ocupação indígena à capitania: ouro, Cuiabá e Vila Bela": 25
- "4. Do Império à República: Rusga, Guerra do Paraguai, Marcha para o Oeste e divisão": 25

Já existem (não repita):
- Mato Grosso é o terceiro maior estado brasileiro em área, atrás do Amazonas e do Pará.
- Os três biomas presentes em Mato Grosso são:
- O Pantanal mato-grossense integra a bacia hidrográfica Amazônica.
- Os rios Juruena, Teles Pires e Xingu pertencem à bacia:
- O clima predominante em Mato Grosso é tropical, com verão chuvoso e inverno seco, estação 
- A unidade de relevo que funciona como grande divisor de águas entre bacias hidrográficas e
- Em Cuiabá há um marco que indica o centro geodésico da América do Sul.
- Mato Grosso faz fronteira internacional com:
- A forte migração para Mato Grosso a partir da década de 1970 teve participação importante 
- São exemplos de cidades que cresceram ao longo da BR-163, ligadas à expansão do agronegóci
- Mato Grosso é o maior produtor nacional de soja, milho e algodão e possui o maior rebanho 
- O espaço industrial de Mato Grosso caracteriza-se principalmente pela:
- A densidade demográfica de Mato Grosso é baixa, e a maior parte da população vive em áreas
- NÃO está entre os principais problemas ambientais de Mato Grosso:
- A modernização agrícola do Cerrado mato-grossense envolveu correção dos solos, mecanização
- O Parque Indígena do Xingu, criado em 1961, está associado à atuação de:
- No século XVII, missões jesuíticas espanholas instaladas na região do Itatim foram atacada
- A descoberta de ouro no rio Coxipó-Mirim, que deu origem ao Arraial de Cuiabá em 1719, é a
- Em 1722, Miguel Sutil descobriu ricas lavras de ouro no local que se tornaria o centro da 
- As monções, que abasteciam as minas de Cuiabá no século XVIII, eram:
- A Capitania de Mato Grosso foi criada em 1748, desmembrada de São Paulo.
- Vila Bela da Santíssima Trindade, fundada em 1752 como capital da capitania, situava-se às
- O Quilombo do Quariterê, próximo a Vila Bela, foi liderado por Tereza de Benguela.
- Com o declínio da mineração, no fim do século XVIII e no século XIX, ganhou importância na
- A mineração em Mato Grosso no século XVIII utilizou intensamente a mão de obra africana es
- A Rusga, conflito ocorrido em Cuiabá em 1834, opôs:
- Na Guerra da Tríplice Aliança, as tropas paraguaias invadiram o sul de Mato Grosso, enquan
- Após a Guerra do Paraguai, a navegação pelo rio Paraguai foi assegurada, o que favoreceu o
- A política do Estado Novo voltada à ocupação e integração do interior do país, que incluiu
- O Marechal Cândido Rondon, nascido em Mato Grosso, chefiou a comissão de linhas telegráfic
- O Estado de Mato Grosso do Sul foi criado por desmembramento de Mato Grosso em:
- Durante o regime militar, a ocupação de Mato Grosso foi estimulada por rodovias como a BR-
- As tensões sociais no campo mato-grossense envolveram, entre outros fatores:
- A capital de Mato Grosso, que estava em Vila Bela, voltou para Cuiabá na década de 1830.
- Os Paiaguá, povo indígena da região do rio Paraguai, eram conhecidos sobretudo:
- O Pantanal é uma grande planície inundável, sujeita a cheias sazonais, compartilhada entre
- A elevação do Arraial de Cuiabá à condição de vila, em 1727, ocorreu sob o governador da c
- Os rios Araguaia e das Mortes, no leste de Mato Grosso, pertencem à bacia:
- O tratado que redefiniu os limites entre os domínios portugueses e espanhóis e reforçou a 
- O Forte Coimbra, Corumbá e a região de Dourados, atingidos pela invasão paraguaia, situam-
```

## Pedido 5: Medicina: urgências, emergências e trauma (`medicina.urgencias-emergencias`, 1030 tópicos de edital)

```
Matéria: medicina.urgencias-emergencias (Medicina: urgências, emergências e trauma)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Abordagem inicial, vias aéreas e ressuscitação cardiopulmonar": 20
- "2. Emergências cardiovasculares e estados de choque": 20
- "3. Emergências neurológicas, metabólicas e falência orgânica": 22
- "4. Emergências respiratórias, digestivas e intoxicações": 19
- "5. Trauma, agentes físicos e acidentes por animais peçonhentos": 19

Já existem (não repita):
- Um adulto é encontrado inconsciente, sem respiração normal e sem pulso palpável após avali
- Durante a ressuscitação de um adulto, a equipe ajusta a técnica de compressão. Qual combin
- Um paciente em PCR apresenta fibrilação ventricular no monitor e desfibrilador disponível.
- Uma pessoa chega com dor torácica opressiva, sudorese e náuseas iniciadas há 25 minutos. Q
- Um adulto apresenta dispneia súbita, ortopneia, crepitações bilaterais e pressão arterial 
- Uma pessoa apresenta assimetria facial e fraqueza no braço direito iniciadas subitamente. 
- Um adulto diabético está sudorético, confuso e apresenta glicemia capilar de 48 mg/dL. Qua
- Após picada de serpente, uma pessoa apresenta dor e edema local, mas está estável. Qual or
- Uma pessoa permanece com abalos generalizados e perda de consciência há sete minutos. Qual
- Paciente apresenta dor torácica e supradesnivelamento persistente de ST, com início há dua
- Uma pessoa com infecção pulmonar apresenta confusão, lactato elevado, extremidades mal per
- Um paciente com insuficiência adrenal conhecida interrompeu a reposição hormonal, desenvol
- Paciente com diabetes, acidose, cetose e desidratação apresenta potássio sérico de 2,8 mEq
- Após uso de analgésico opioide, um paciente está muito sonolento, com pupilas puntiformes 
- Paciente hipotenso após colisão apresenta fratura de fêmur com sangramento externo pulsáti
- Paciente com cirrose conhecida apresenta hematêmese volumosa e sinais de choque. Após inic
- Paciente com queimaduras na face após incêndio em ambiente fechado apresenta rouquidão pro
- Na reanimação de adulto após provável embolia pulmonar maciça, o monitor revela complexos 
- Uma pessoa apresenta dor torácica súbita em rasgo, irradiação dorsal, diferença de pulsos 
- Após infarto extenso, paciente tem extremidades frias, oligúria, hipotensão, crepitações e
- Uma pessoa previamente com creatinina de 0,9 mg/dL passa a 1,25 mg/dL em 36 horas, sem oli
- Duas pessoas são retiradas inconscientes de ambiente com aquecedor defeituoso. A oximetria
- Criança após picada de escorpião apresenta vômitos repetidos, sudorese intensa, taquicardi
- Adulto sem cirrose prévia apresenta icterícia recente, encefalopatia progressiva e INR de 
- Após intubação durante PCR adulta, capnografia confirma a posição traqueal e a equipe mant
- Um adulto inconsciente respira de maneira agônica e não apresenta pulso detectável em aval
- Um paciente em assistolia confirmada deve receber choque elétrico inicial, pois todos os r
- Na suspeita de síndrome coronariana aguda, o eletrocardiograma inicial deve ser realizado 
- Uma pessoa com pressão arterial de 190/110 mmHg, assintomática e sem evidência de dano agu
- Após início súbito de afasia, a equipe deve investigar a última vez em que o paciente foi 
- Em uma queimadura térmica recente, aplicar gelo diretamente sobre o tecido lesado é recome
- Após iniciar noradrenalina no choque séptico, a equipe deve continuar avaliando perfusão, 
- Paciente com dor torácica típica, ECG sem supradesnivelamento e troponina aumentada deve r
- Paciente com rebaixamento, saturação periférica satisfatória recebendo oxigênio e retenção
- Na vítima de trauma torácico com hipotensão, grande dificuldade respiratória e sinais clín
- Um adulto desmaia sem pródromos durante exercício e relata palpitações imediatamente antes
- Na hemorragia digestiva aguda volumosa, a primeira hemoglobina normal exclui hipovolemia r
- Na intoxicação por substância desconhecida acompanhada de vômitos e alteração de consciênc
- De acordo com os critérios KDIGO, para diagnosticar lesão renal aguda é obrigatório observ
- Na cetoacidose diabética com potássio sérico muito baixo, é possível que a reposição de po
- Paciente com hemiparesia súbita pode receber trombólise intravenosa antes da neuroimagem q
- A orientação internacional de sepse publicada em 2026 sugere pelo menos 30 mL/kg de crista
- No coma agudo associado a miose e bradipneia após possível consumo de opioide, a naloxona 
- Após choque elétrico de alta tensão, a inspeção de pequenas marcas cutâneas não basta para
- Em acidente ofídico com sinais sistêmicos graves, a soroterapia específica deve ser adiada
```

## Pedido 6: Equações do 1º e do 2º grau (`matematica.equacoes-1-2-grau`, 1027 tópicos de edital)

```
Matéria: matematica.equacoes-1-2-grau (Equações do 1º e do 2º grau)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Igualdade, incógnita e equações do primeiro grau": 26
- "2. Equações equivalentes, casos especiais e problemas lineares": 26
- "3. Estrutura das equações quadráticas e métodos de resolução": 17
- "4. Discriminante, fórmula geral e relações entre raízes": 17
- "5. Modelagem quadrática, restrições e estratégias de prova": 14

Já existem (não repita):
- Uma papelaria vende cada caderno por x reais. Sabendo que 3 cadernos custam 42 reais, qual
- Uma taxa fixa de 8 reais é adicionada ao dobro do valor x, produzindo 30 reais. Qual valor
- Um estudante resolve x²-16=0 no conjunto dos reais. Qual é o conjunto-solução dessa equaçã
- Depois de simplificar a igualdade 2(x+3)=2x+6, qual conclusão vale para todo x real?
- Um cálculo chega à equação 5(x-1)=5x+2. Qual é a classificação correta no universo real?
- Um terreno quadrado tem área de 81 metros quadrados. Considerando o lado positivo x, quant
- Ao fatorar x²-5x=0, um estudante procura todos os valores reais da incógnita. Quais são el
- No modelo x²+4x+4=0, quantos valores reais distintos satisfazem a igualdade?
- Uma caixa recebe uma quantidade x de peças e mais 7 peças, totalizando 25. Qual equação re
- Uma cobrança de 60 reais corresponde à metade de x acrescida de 10 reais. Qual valor de x 
- Uma placa exibe a equação 2x²-8x+6=0. Qual par contém exatamente suas raízes reais?
- Uma oficina cobra 18 reais pela visita e 12 reais por hora. Se a conta foi de 78 reais, po
- Em x²-2x-15=0, quais valores são raízes reais da equação?
- Um número x e seu consecutivo têm produto 30, considerando x natural. Qual é o menor númer
- Um técnico estuda x²-8x+16=0. Qual valor real satisfaz a igualdade e com que multiplicidad
- Considere a igualdade (x-3)/(x-3)=1. Qual é o seu conjunto-solução no universo dos números
- A soma das raízes de 3x²-15x+12=0 é solicitada sem calcular cada raiz. Qual resultado deco
- Um parâmetro k aparece na equação x²+kx+9=0. Sabendo que x=3 é raiz, qual é o valor de k?
- Deseja-se uma equação quadrática mônica cujas raízes são -2 e 5. Qual trinômio deve ser ig
- A expressão (m-2)x²+3x-9=0 foi registrada como quadrática. Qual solução ocorre quando o pa
- Uma equação 2x²-5x-3=0 precisa ser fatorada. Qual conjunto reúne todos os valores reais qu
- Um cálculo de consumo fornece a equação 2(x-1)/3+(x+2)/2=5. Qual valor de x é compatível c
- Uma área retangular mede 84 m² e seu comprimento excede a largura x em 5 metros. Qual larg
- O trinômio tem raízes 1 e -4 e coeficiente principal 2. Qual equação representa exatamente
- Considere (x²-9)/(x-3)=6 com x real. Qual afirmação descreve corretamente o conjunto-soluç
- Uma caixa contém 56 peças distribuídas em sete grupos iguais. Na equação 7x=56, cada grupo
- Ao simplificar 2(x-1)=2x+1, um aluno conclui corretamente que todos os números reais satis
- Um estudante encontra x²-49=0 e informa duas soluções reais distintas: -7 e 7.
- Uma resolução de x²-7x=0 que apresenta apenas a raiz 7 está completa, pois se pode dividir
- No estudo de 2x²+3x+5=0, o discriminante negativo indica que o conjunto das raízes reais é
- Uma equação quadrática com discriminante igual a zero apresenta duas soluções reais com va
- Para um retângulo de largura x, comprimento x+4 e perímetro 28, a equação linear correspon
- A igualdade (x+2)/(x+2)=1 admite x=-2 como solução, pois numerador e denominador seriam ig
- Um trinômio monico cujas raízes são -1 e 4 pode ser escrito como x²-3x-4.
- Se o coeficiente do termo x² de uma expressão ax²+bx+c=0 é zero, a equação continua necess
- O trinômio x²-6x+9 constitui quadrado perfeito e possui raiz real dupla igual a 3 quando i
- Na equação x²=5x, dividir os membros por x preserva as soluções originais x=0 e x=5.
- Dois naturais consecutivos com produto 20 podem ser representados por x e x+1, e o menor d
- Sabendo que 3 é uma raiz de x²+mx+9=0, conclui-se que a outra raiz necessariamente vale -3
- A equação 3x²+6x+3=0 apresenta discriminante nulo e uma raiz real dupla igual a -1.
- Duas equações quadráticas que possuem exatamente as mesmas raízes reais devem necessariame
- Na equação x²+kx+16=0, a condição de raiz real dupla em x=-4 exige k=8.
- A equação (x²-4)/(x-2)=4 admite x=2 como solução porque a forma simplificada permite escre
- Se as raízes de x²-2x-8=0 são 4 e -2, sua soma é 2 e seu produto é -8.
- O polinômio x²-10x+25, quando igualado a zero, oferece duas raízes reais distintas, x=5 e 
```

## Pedido 7: Sistema Legal de Medidas (`matematica.sistema-legal-medidas`, 1023 tópicos de edital)

```
Matéria: matematica.sistema-legal-medidas (Sistema Legal de Medidas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema Internacional, prefixos e escrita dos símbolos": 20
- "2. Comprimento, área e medidas agrárias": 20
- "3. Volume, capacidade e massa": 20
- "4. Tempo, velocidade, escalas e resolução de problemas": 20
- "5. Problemas resolvidos e conversões combinadas": 20

Já existem (não repita):
- São unidades de base do Sistema Internacional (SI), EXCETO:
- A forma correta de escrever "quarenta quilômetros" pelo SI é "40 Km.".
- O prefixo "mili" corresponde ao fator:
- O quilograma é a única unidade de base do SI cujo nome contém um prefixo.
- Uma corrida de 4,25 km corresponde a:
- A soma 1,2 m + 35 cm + 150 mm é igual a 1,7 m.
- Uma placa tem área de 0,6 m². Essa área, em centímetros quadrados, é:
- Um hectare equivale a cem ares, e um are equivale a cem metros quadrados.
- Uma fazenda tem 3,2 km². Sua área, em hectares, é:
- Um terreno retangular mede 40 m por 25 m. Sua área corresponde a:
- Para revestir um piso retangular de 3 m por 2,4 m com placas quadradas de 40 cm de lado, s
- Um retângulo tem 1,5 m de comprimento e 80 cm de largura. Seu perímetro é:
- Um reservatório em forma de paralelepípedo mede 2 m × 1,5 m × 1,2 m. Sua capacidade, em li
- Um metro cúbico equivale a cem litros.
- Quantos frascos de 250 mL são necessários para envasar 12 L de um produto?
- Um volume de 2.500 cm³ corresponde a 2,5 litros.
- Uma piscina tem 10 m de comprimento, 5 m de largura e 1,6 m de profundidade média. Se for 
- Um caminhão transporta 3,5 t de carga. Essa massa corresponde a:
- Um comprimido tem 500 mg de princípio ativo. Uma caixa com 20 comprimidos contém 10 g dess
- Considerando que 1 L de água tem massa de 1 kg, a massa de água de um aquário cheio com 60
- Um óleo tem densidade de 0,8 kg/L. A massa de 25 L desse óleo é de 20 kg.
- O tempo de 2,4 horas corresponde a:
- Um servidor trabalhou 2 h 45 min pela manhã e 3 h 35 min à tarde. No total, trabalhou 6 h 
- Quantos segundos há em 1 h 15 min?
- Um veículo a 90 km/h tem velocidade de:
- Um carro que mantém 75 km/h percorre 100 km em 1 h 20 min.
- Num mapa de escala 1:250.000, duas cidades estão separadas por 8 cm. A distância real entr
- Se 5 km reais são representados por 2 cm num mapa, a escala do mapa é 1:250.000.
- Uma torneira despeja 15 L por minuto. O tempo para encher uma caixa de 1,8 m³ é:
- Um atleta corre 400 m em 50 s. Sua velocidade média é de 28,8 km/h.
- Uma prova começou às 13 h 40 min e durou 4 h 30 min. Ela terminou às:
- Um fio de 3,6 m será cortado em pedaços de 45 cm. O número de pedaços obtidos é:
- Um quilômetro quadrado corresponde a um milhão de metros quadrados.
- Um cubo tem aresta de 20 cm. Seu volume, em litros, é:
- Para converter metros cúbicos em decímetros cúbicos, multiplica-se por cem.
- Um medicamento deve ser tomado a cada 8 horas. Começando às 6 h de segunda-feira, a 5ª dos
- Para comparar 1,2 km, 1.150 m e 118.000 cm, a ordem crescente correta é:
- No SI, o símbolo correto da hora é "h", e o do minuto, "min".
- Uma sala retangular de 6 m por 4,5 m terá o piso coberto por um carpete que custa R$ 40,00
- Uma gleba de 1,2 ha será loteada. Reservam-se 20% da área para ruas, e o restante é dividi
- Um motorista percorre 360 km: a primeira metade a 90 km/h e a segunda metade a 60 km/h. Su
- Uma chuva de 25 mm caiu sobre um telhado de 80 m², e toda a água foi coletada numa cistern
- De acordo com as normas oficiais do Sistema Internacional de Unidades (SI), uma temperatur
- Quando escritos por extenso em português, os nomes das unidades de medida iniciam-se com l
- Assinale a alternativa em que TODAS as medidas estão grafadas em estrita conformidade com 
- Na análise laboratorial de um fármaco, verificou-se que uma cápsula contém 0,00025 g de de
- Para converter diretamente uma medida expressa em decâmetros quadrados (dam²) para decímet
- Uma propriedade rural possui área de 4,5 alqueires mineiros. Sabendo-se que 1 alqueire min
- Um sítio em formato quadrangular possui área igual a 9 hectares. Deseja-se cercar todo o p
- Uma pista de caminhada tem 850 m de extensão. Se uma pessoa der 6 voltas completas nessa p
- Na escada das unidades de capacidade, 1 hectolitro (hL) equivale a 1.000 litros.
- Uma seringa hospitalar graduada em centímetros cúbicos que contém 8,5 cm³ de vacina possui
- Uma barra maciça de ferro tem volume de 4 dm³. Sabendo que a densidade do ferro é de 7,8 g
- O intervalo de tempo de 1,45 hora equivale a 1 hora e 45 minutos.
- Em uma planta baixa desenhada na escala 1:50, um terreno retangular tem área de 24 cm² no 
- Um voo decolou às 09 h 50 min e pousou às 13 h 15 min do mesmo dia (no mesmo fuso horário)
- Uma maquete de um reservatório cilíndrico foi construída na escala 1:20 e comporta exatame
- Uma bomba hidráulica opera com vazão constante de 2,5 L/s. Em 1 hora de funcionamento inin
- Um tanque de 2,4 m³ está inicialmente vazio. Uma torneira passa a despejar água nele à raz
- Uma gráfica utiliza folhas de papel retangulares de 25 cm por 40 cm com gramatura de 80 g/
- Para pintar uma fachada retangular de 14 m de comprimento por 3 m de altura com duas demão
```

## Pedido 8: Direitos políticos e partidos (arts. 14 a 17) (`direito-constitucional.direitos-politicos`, 1022 tópicos de edital)

```
Matéria: direito-constitucional.direitos-politicos (Direitos políticos e partidos (arts. 14 a 17))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Soberania popular, cidadania, sufrágio e participação direta": 16
- "2. Alistamento eleitoral, voto e condições de elegibilidade": 16
- "3. Inelegibilidades constitucionais, reeleição e impugnação do mandato": 15
- "4. Perda e suspensão dos direitos políticos e anterioridade eleitoral": 17
- "5. Partidos políticos, fidelidade e cláusula de desempenho": 36

Já existem (não repita):
- Uma comunidade será consultada antes de se editar uma norma sobre assunto relevante. Qual 
- Depois de uma medida normativa já aprovada, os eleitores são convocados para ratificá-la o
- Uma pessoa brasileira, alfabetizada e com 17 anos completos pretende alistar-se e votar. Q
- Uma pessoa brasileira e analfabeta, com 45 anos, deseja votar e depois concorrer a eleição
- Durante o período de prestação do serviço militar obrigatório, determinado conscrito solic
- Uma pessoa brasileira preenche idade mínima e domicílio eleitoral, mas não possui filiação
- Uma pessoa com 32 anos reúne as demais condições para disputar uma vaga no Senado. Falta-l
- Uma pessoa de 20 anos pretende concorrer a uma prefeitura e já possui filiação partidária.
- Após ser eleito prefeito, o titular manifesta interesse em continuar na mesma função no pe
- Uma pessoa no exercício de mandato de governador pretende concorrer ao Senado. Qual provid
- Após a diplomação de pessoa eleita, surgem provas de fraude relativa ao pleito. Qual o pra
- Eleitores subscrevem projeto de lei federal. Qual requisito mínimo constitucional integra 
- Câmara municipal aprova uma consulta sobre questão local para ocorrer na eleição municipal
- Cônjuge de chefe do Executivo pretende concorrer na circunscrição do titular. Qual ressalv
- Militar alistável com mais de dez anos de serviço participa de eleição e é eleito. Qual se
- Uma pessoa recebe condenação criminal ainda sujeita a recurso, sem trânsito em julgado. Qu
- Lei publicada em fevereiro altera o processo eleitoral e há eleição no outubro seguinte. Q
- Duas legendas discutem coligação para uma disputa proporcional e outra majoritária. Qual r
- Um partido analisa os requisitos de desempenho para a legislatura seguinte às eleições de 
- Vereador eleito pelo sistema proporcional abandona a legenda que o elegeu. Quando o art. 1
- A curatela de uma pessoa adulta com deficiência é invocada para impedir automaticamente se
- Uma condenação colegiada gera inelegibilidade prevista em lei complementar, mas ainda não 
- Ação que questiona mandato diplomado por fraude tramita com publicidade irrestrita segundo
- Para a legislatura seguinte à eleição de 2030, um partido busca o caminho por eleição de r
- Em debate sobre percentuais constitucionais partidários, uma legenda compara programas de 
- Ao examinar uma proposta de reforma democrática, uma comissão observa que a Constituição p
- Um grupo apresenta referendo como a consulta realizada obrigatoriamente antes da adoção de
- Uma eleitora analfabeta de 50 anos afirma que pode alistar-se e votar facultativamente, ma
- Um estrangeiro residente no país, sem situação constitucional especial, pode alistar-se se
- Uma pessoa de 34 anos, apesar de possuir filiação partidária e alistamento, não satisfaz a
- Uma pessoa de 18 anos preenche, por essa razão, a idade mínima para exercer mandato de pre
- Uma pessoa de 17 anos pode realizar o alistamento, caso deseje, sem que disso resulte obri
- Um eleitor pretende candidatar-se sem filiação partidária e argumenta que ter domicílio el
- Governador em exercício que pretenda concorrer ao Senado deve renunciar ao próprio mandato
- Em eleições municipais, uma consulta local aprovada pela Câmara pode ser encaminhada à Jus
- O conscrito, no período do serviço militar obrigatório, encontra vedação constitucional ao
- Prefeito que concorre à própria primeira reeleição consecutiva precisa renunciar seis mese
- Em situação de analfabetismo, a inelegibilidade decorre do art. 14, § 4º, embora isso não 
- Lei que modifica o processo eleitoral e é publicada em janeiro somente adquire vigência em
- Uma decisão judicial colegiada pode produzir inelegibilidade prevista na legislação comple
- Para aferir cláusula de desempenho relativa à legislatura posterior às eleições de 2026, d
- Um deputado que abandona a legenda pela qual se elegeu poderá preservar o mandato se houve
- A inelegibilidade reflexa do art. 14, § 7º, alcança automaticamente todos os parentes do t
- O art. 17, § 9º, exige que 30% dos recursos dos fundos ali especificados destinados a camp
- A ação constitucional de impugnação de mandato deve ser ajuizada até noventa dias depois d
```

## Pedido 9: Pessoas naturais: personalidade, capacidade e domicílio (`direito-civil.pessoas-naturais`, 1010 tópicos de edital)

```
Matéria: direito-civil.pessoas-naturais (Pessoas naturais: personalidade, capacidade e domicílio)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Personalidade, nascimento, morte e estado da pessoa": 25
- "2. Capacidade, emancipação e autonomia da pessoa com deficiência": 16
- "3. Direitos da personalidade, nome, imagem e privacidade": 21
- "4. Ausência, curadoria e sucessões do ausente": 15
- "5. Domicílio da pessoa natural e integração dos institutos": 23

Já existem (não repita):
- Uma criança nasceu com vida, recebeu atendimento por poucos minutos e morreu antes da expe
- Uma pessoa já concebida ainda não nasceu, e seus familiares discutem se a legislação a pro
- Uma pessoa de 15 anos pretende celebrar, sem qualquer representante, um negócio civil que 
- Após evento clínico transitório, uma pessoa adulta está impossibilitada de exprimir vontad
- Pais de adolescente com 16 anos completos pretendem emancipá-lo voluntariamente, sem confl
- Uma pessoa adulta com deficiência deseja casar e administrar a própria vida, sem indicação
- Uma escritora utiliza pseudônimo em livros publicados licitamente, e terceiro passa a empr
- Uma pessoa mantém residências em duas cidades e vive alternadamente em ambas por períodos 
- Uma pessoa desapareceu em desastre com perigo grave e extremamente provável de morte. Sua 
- Duas pessoas com potencial transmissão sucessória recíproca morreram em acidente, e a prov
- Adolescente não emancipado com 17 anos pratica sozinho negócio jurídico para o qual sua as
- Uma pessoa com deficiência opta por tomada de decisão apoiada em questões patrimoniais. Qu
- Uma pessoa de 30 anos solicita no registro civil alteração imotivada do prenome e não real
- Uma empresa utiliza em campanha comercial fotografia identificável de pessoa sem autorizaç
- Uma pessoa desapareceu, deixou procurador com poderes apenas para receber correspondência 
- Uma profissional reside habitualmente em cidade A, mas exerce seu trabalho em cidade B, on
- Após o falecimento de alguém, um sobrinho do falecido, colateral de terceiro grau, pretend
- Um jovem de 16 anos completos mantém emprego remunerado, mas a renda eventual é insuficien
- Em um processo de ausência sem procurador, a sentença de abertura da sucessão provisória f
- Na sucessão provisória de pessoa desaparecida, dois interessados foram admitidos como herd
- Durante a posse provisória dos bens do ausente, um descendente e um sobrinho recebem quinh
- Após sentença de abertura da sucessão provisória, seu trânsito em julgado ocorre em certo 
- Há cinco anos não chegam notícias de uma pessoa desaparecida, que acaba de completar oiten
- Uma pessoa retorna oito anos após a abertura da sucessão definitiva; vários bens originais
- Uma pessoa que cumpre pena com sentença definitiva e outra que exerce temporariamente funç
- A idade integra o estado individual da pessoa e a filiação integra o estado familiar; uma 
- Pessoa de 17 anos não emancipada é absolutamente incapaz para todos os negócios civis, poi
- A sentença declaratória de ausência é levada a registro público, conforme o art. 9º do Cód
- O domicílio da pessoa natural corresponde obrigatoriamente a uma única residência, ainda q
- Pessoa que usa pseudônimo em atividade lícita dispõe, em relação a esse pseudônimo, da pro
- Uma pessoa de 16 anos que inicia estágio remunerado adquire de imediato emancipação legal,
- Nos contratos escritos, as partes podem especificar domicílio para exercício e cumprimento
- A pessoa que perdeu temporariamente a possibilidade de exprimir vontade entra na categoria
- O art. 14 do Código Civil considera válida a disposição gratuita do próprio corpo para dep
- Uma biografia literária publicada sem autorização prévia do biografado é ilícita por essa 
- Depois de arrecadados os bens de pessoa ausente que não deixou representante, a sucessão p
- Em razão da curatela de uma pessoa com deficiência, o juiz pode restringir genericamente s
- O domicílio profissional do art. 72 não define, por si só, o estado familiar de uma pessoa
- A mudança de domicílio civil ocorre quando a pessoa registra novo endereço em qualquer cad
- A publicidade do casamento no registro civil documenta estado familiar, mas a certidão reg
- O cônjuge, os ascendentes, os descendentes e os parentes colaterais até quarto grau integr
- Na sucessão provisória do ausente, ascendentes, descendentes e cônjuge, se comprovarem sua
- A sucessão definitiva pode ser requerida pelo art. 38 sempre que o ausente alcançar oitent
- Para pessoa que não possui residência habitual, o art. 73 define como domicílio civil o lu
- Quando o ausente regressa dentro dos dez anos seguintes à abertura da sucessão definitiva,
```

## Pedido 10: Estrutura lógica de relações e dedução (`raciocinio-logico.relacoes-arbitrarias`, 1005 tópicos de edital)

```
Matéria: raciocinio-logico.relacoes-arbitrarias (Estrutura lógica de relações e dedução)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Modelagem de relações e cruzamento de atributos": 20
- "2. Ordenação, posições e arranjos circulares": 20
- "3. Proposições, conectivos e equivalências": 20
- "4. Quantificadores, conjuntos e dedução": 20
- "5. Verdadeiros, mentirosos e verificação de argumentos": 20

Já existem (não repita):
- Ana, Beto e Cida receberam, sem repetição, um livro, uma chave e uma caneta. Ana recebeu o
- Três colegas escolheram, cada qual, uma cor entre azul e vermelho. O enunciado não proíbe 
- Numa fila de cinco participantes, Rita ocupa a terceira posição contada da esquerda. Qual 
- Na ordem de apresentação, Ana vem imediatamente antes de Bia, e Bia vem imediatamente ante
- Uma pessoa afirma: ‘Se o pacote chegou, então o recibo foi emitido’. Constatou-se que o pa
- Uma ficha contém a frase ‘João entrou e Maria saiu’. Qual é sua negação lógica?
- Em determinado grupo, todos os fotógrafos são cadastrados. Nenhuma outra informação foi da
- Sabe-se que nenhum item de madeira pertence ao lote metálico. Joana identificou uma peça d
- Lara diz ‘O envelope está na caixa azul’; Mauro diz ‘O envelope não está na caixa azul’. A
- Um gráfico de barras registra quantidades por região: Norte 8, Sul 12 e Leste 12. Qual lei
- Considere a afirmação ‘Todas as páginas do caderno foram revisadas’. Qual frase descreve e
- Quatro pessoas, Ana, Beto, Caio e Duda, sentam-se igualmente espaçadas numa mesa circular.
- A regra de acesso afirma: ‘Se uma credencial é válida, então o portão libera a passagem’. 
- Em uma tabela-verdade, p recebeu F e q recebeu V. Qual alternativa classifica corretamente
- Em uma classificação, toda pera é fruta e nenhuma fruta é objeto metálico. Qual conclusão 
- Lara, Mauro e Nina recebem, sem repetição, os cartões 1, 2 e 3. Mauro recebeu o 3; Lara nã
- De 30 fichas, 18 têm marca R, 15 têm marca S e 8 têm as duas marcas. Quantas fichas não ap
- Cinco pessoas sentam-se em círculo. Em sentido horário, Beto está imediatamente após Ana, 
- Um responsável afirmou: ‘Para cada sala do edifício existe uma chave que a abre’. Nenhuma 
- Sabe-se que alguns artesãos são músicos, todos os músicos são credenciados e nenhum creden
- Ana, Bruno e Caio são, cada qual, de um tipo: sempre dizem a verdade ou sempre mentem. Ana
- Três proposições p, q e r descrevem etapas de uma tarefa. A condicional ‘se p, então q’ é 
- Quatro pessoas A, B, C e D sentam-se, sem lugares vagos, numa fila da esquerda para a dire
- Em um conjunto de 40 registros, 25 possuem etiqueta A, 21 possuem etiqueta B e 9 apresenta
- Exatamente uma das proposições P, Q e R é verdadeira. Foi verificado que ‘P ou Q’ é falsa 
- Ana, Beto e Cida ocupam salas distintas. Ana está na sala Azul. Como cada sala pode recebe
- Em um grupo, a frase ‘todos os voluntários estão inscritos’ garante, por si só, que pelo m
- Em uma fila de cinco pessoas, Davi ocupa a terceira posição contada da esquerda. Davi tamb
- Numa sequência de apresentações, saber que Paula se apresentou antes de Rafael é suficient
- Se dois registros possuem os atributos P e Q, ambos verdadeiros, a afirmação composta ‘P o
- A condicional ‘se houver chuva, a janela será fechada’ é falsa quando não há chuva e a jan
- Em uma fila numerada, se existem exatamente duas pessoas entre Beatriz e Caio, a diferença
- Três estudantes escolheram uma bebida entre suco e água. Como cada um fez uma escolha, é l
- Se o cadastro foi aprovado, então foi emitido um número de controle. Sabendo que nenhum nú
- A negação lógica da informação ‘algum pacote foi conferido’ é ‘algum pacote não foi confer
- Sabe-se que alguns desenhistas são músicos e que nenhum músico pertence ao grupo dos apose
- Em uma mesa circular, descrever os participantes a partir de uma cadeira diferente, manten
- Em uma lista de 20 registros, 11 possuem etiqueta A, 9 possuem B e 4 possuem ambas. A quan
- Se uma proposta é aceita somente se tiver assinatura, a existência de assinatura em uma pr
- Um regulamento afirma ‘se P, então Q’. Em uma situação com Q falso, a regra obriga P falso
- Duas pessoas devem ser verazes ou mentirosas. Ana afirma ‘Beto mente’, e Beto afirma ‘Ana 
- A frase ‘para cada pessoa há uma sala que ela pode usar’ pode ser verdadeira mesmo quando 
- Se alguns cozinheiros são músicos e alguns músicos são pintores, conclui-se necessariament
- Exatamente uma dentre P, Q e R é verdadeira. Sabendo que P é falsa e Q é falsa, pode-se co
- Se todos os elementos de A pertencem a B e nenhum elemento de B pertence a C, então é nece
```

## Pedido 11: Educação de adultos, EaD e tecnologias educacionais (`educacao.ead-tecnologias-andragogia`, 1002 tópicos de edital)

```
Matéria: educacao.ead-tecnologias-andragogia (Educação de adultos, EaD e tecnologias educacionais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Andragogia, Educação de Jovens e Adultos e aprendizagem ao longo da vida": 20
- "2. Teorias psicológicas da aprendizagem, ensinar-aprender-pesquisar e formação organizacional": 20
- "3. Educação a distância: formatos, legislação, mediação e atores": 20
- "4. Tecnologias educacionais, avaliação, inclusão e recursos digitais": 20
- "5. Moodle: atividades, recursos, configuração e casos integrados": 20

Já existem (não repita):
- Em formação de adultos, a equipe parte de problemas reais, aproveita experiências anterior
- Uma pessoa adulta interrompeu o ensino médio e busca oportunidade gratuita de retomar esco
- Uma pessoa de 16 anos pretende realizar exame supletivo de conclusão do ensino fundamental
- Em curso on-line, estudantes publicam intervenções em um fórum ao longo de três dias, conf
- Uma professora quer que a turma produza comentários e responda aos colegas sobre um proble
- Uma turma deverá enviar relatórios individuais em arquivos para correção e comentários doc
- Um docente retoma as ideias já conhecidas pelos participantes antes de apresentar nova teo
- Um estudante consegue resolver uma tarefa complexa com pistas graduais de colega experient
- Uma equipe usa painel do ambiente virtual para verificar quem abriu o material. Qual infer
- Uma atividade de videoconferência ocorre ao vivo, com docente e estudantes em cidades dife
- Uma equipe elabora formação de adultos e confunde pressupostos andragógicos com normas imu
- Em exercício, uma consequência desagradável cessa quando o estudante começa a entregar tar
- Um curso corporativo exige que participantes saibam mediar conflitos em casos novos, mas a
- Uma instituição planeja graduação a distância regulada pelo Decreto nº 12.456/2025 e quer 
- Uma unidade curricular de graduação com EaD delega planejamento e avaliação integralmente 
- Um professor quer distribuir material extenso em capítulos navegáveis no Moodle, sem exigi
- Uma equipe escolhe videoaulas sem legendas como único meio de ensino e argumenta que a igu
- Uma pessoa concluiu treinamento após assistir a todas as aulas, mas continua incapaz de ex
- No planejamento de uma atividade síncrona mediada de graduação, pretende-se reunir 85 estu
- Um curso de graduação híbrido pretende ser ofertado como semipresencial, mas contabiliza 2
- Uma unidade curricular de graduação inclui EaD e prevê que 80% da nota final seja obtida e
- Uma docente deseja construir percurso no Moodle em que uma resposta equivocada leve a expl
- Em formação on-line, o professor pede pareceres individuais e, depois, avaliações por cole
- Em avaliação de EJA, participante domina cálculo de troco aprendido no trabalho, mas desco
- Uma análise institucional conclui que participantes que acessaram mais o Moodle aprenderam
- Em curso de formação de adultos, propor problemas relacionados à experiência dos participa
- A LDB prevê os exames supletivos de conclusão do ensino fundamental apenas para maiores de
- Um docente observa que estudantes resolvem tarefas apenas com pistas e exemplos, mas ainda
- Uma reunião virtual ao vivo reúne professor e alunos em cidades diferentes. Como há partic
- No Moodle, o módulo Livro pode apresentar material em capítulos e subcapítulos, sendo clas
- Um professor que deseja que estudantes entreguem textos e recebam avaliação individual dev
- Na Educação de Jovens e Adultos, o art. 37 da LDB determina considerar características dos
- Ao retirar um estímulo desagradável após comportamento desejado e observar aumento de sua 
- Quando se pretende avaliar mediação de conflitos em situações inéditas, solicitar análise 
- Segundo o Decreto nº 12.456/2025, o formato de graduação a distância dispensa integralment
- O Decreto nº 12.456/2025 diferencia o professor regente, que integra o corpo docente, do t
- A disponibilização de um vídeo sem legendas já satisfaz o princípio de acessibilidade para
- Uma atividade de Lição no Moodle pode apresentar caminhos diferentes conforme respostas, a
- Como o Decreto nº 12.456/2025 regula os formatos de graduação, os seus percentuais de pres
- Uma plataforma permite concluir um recurso após sua visualização. Esse estado de conclusão
- No Decreto nº 12.456/2025, a atividade síncrona mediada permite grupo de 85 estudantes com
- Num percurso corporativo, uma pessoa pode concluir todos os vídeos e questionários de memo
- Na graduação com unidade curricular ofertada em parte a distância, o Decreto nº 12.456/202
- Um professor usa Oficina no Moodle para que participantes entreguem pareceres e revisem os
- Uma equipe adapta textos para celular e oferece legendas, mas conclui que qualquer materia
```

## Pedido 12: Poder Judiciário: disposições gerais e magistratura (`direito-constitucional.poder-judiciario-disposicoes-gerais`, 1001 tópicos de edital)

```
Matéria: direito-constitucional.poder-judiciario-disposicoes-gerais (Poder Judiciário: disposições gerais e magistratura)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura constitucional e funções do Poder Judiciário": 31
- "2. Estatuto da Magistratura: ingresso, carreira e movimentação": 15
- "3. Quinto constitucional, garantias e vedações": 16
- "4. Autonomia, organização e funcionamento dos tribunais": 23
- "5. Precatórios e requisições de pequeno valor: regime geral": 15

Já existem (não repita):
- Uma turma de estudos afirma que o conselho responsável pelo controle administrativo da Jus
- Um relatório confunde a sede constitucional de um órgão com a extensão de sua jurisdição. 
- Ao indicar instituições que compõem o Judiciário, um estudante incluiu uma corte de contas
- Em ação civil, uma decisão judicial não apresenta fundamentos. Conforme o art. 93, IX, qua
- Uma instituição pretende suspender todas as atividades jurisdicionais durante semanas de f
- Foi proposto editar lei ordinária, de iniciativa do Executivo, para substituir integralmen
- Candidata bacharel em Direito quer ingressar diretamente como juíza de carreira. Qual exig
- Uma magistrada figura em lista de merecimento pela terceira vez consecutiva para promoção.
- Em promoção por antiguidade, o tribunal pretende afastar a pessoa mais antiga. Qual critér
- A remoção de magistrado por interesse público foi aprovada com votação por maioria absolut
- Para ocupar vaga reservada à advocacia pelo quinto constitucional, uma classe apresentou n
- Depois de receber lista sêxtupla do órgão representativo, um tribunal precisa concluir a f
- Juíza de primeiro grau exerce a função há dezoito meses e pergunta sobre sua vitaliciedade
- Juiz vitalício sofre tentativa de perda do cargo apenas por decisão administrativa. Qual p
- Uma pessoa aposentada da magistratura há dois anos deseja advogar perante o tribunal do qu
- Um tribunal pretende criar nova vara apenas por resolução interna, sem observar o caminho 
- A proposta orçamentária do Judiciário supera os limites estipulados na LDO. O que determin
- Um tribunal com trinta julgadores pretende organizar órgão especial. Qual intervalo consti
- Uma câmara isolada declara inconstitucionalidade de lei por maioria simples, sem submeter 
- Tribunal de Justiça tem 180 desembargadores em exercício e prepara eleição diretiva. Qual 
- Uma dívida judicial de pequeno valor está dentro do limite legal aplicável. Qual é o regim
- Após a EC nº 136/2025, qual data delimita, pelo art. 100, § 5º, os precatórios a incluir n
- Credora de precatório alimentício tem 67 anos e apresenta crédito muito superior ao valor 
- Um ente subnacional apresenta estoque em mora equivalente a 30% da receita corrente líquid
- Um credor de precatório alimentício sofre preterição na ordem constitucional de pagamento.
- Uma pessoa concluiu que o controle administrativo exercido pelo Conselho Nacional de Justi
- Um tribunal superior sediado na Capital Federal possui jurisdição em todo o território nac
- Uma decisão judicial pode deixar de apresentar fundamentação quando o julgamento tiver res
- Durante período sem expediente forense normal, o plantão permanente é compatível com o car
- A publicação de edital para juiz substituto por concurso somente de provas atende integral
- É obrigatória a promoção por merecimento do juiz que conste cinco vezes alternadas em list
- Para recusar o magistrado mais antigo na promoção por antiguidade, basta a maioria absolut
- A remoção por interesse público pode ser deliberada por maioria absoluta do tribunal respe
- Pelo quinto constitucional, a classe indica lista tríplice e o tribunal amplia a relação p
- O prazo constitucional de escolha pelo Executivo, após o envio da lista tríplice do art. 9
- Juiz de primeiro grau com dezoito meses de exercício já adquiriu a vitaliciedade apenas po
- Magistrado aposentado há dois anos não pode advogar no tribunal do qual se afastou, em raz
- O tribunal tem competência para propor novas varas, não decorrendo disso autorização para 
- A autonomia financeira permite ao tribunal encaminhar proposta orçamentária acima dos limi
- Em tribunal com mais de vinte e cinco julgadores, pode haver órgão especial com onze a vin
- A EC nº 134/2024 aplica a eleição especial do art. 96, parágrafo único, a qualquer Tribuna
- Os créditos alimentícios comuns na fila preferencial devem ser pagos antes de qualquer sup
- A expedição de precatório é exigida também para obrigação definida em lei como de pequeno 
- Pela redação atual do art. 100, § 5º, o marco para inclusão orçamentária dos precatórios é
- A EC nº 136/2025 estabeleceu limites percentuais progressivos de pagamento de precatórios 
```

## Pedido 13: Desenvolvimento sustentável, ODS e A3P (`sustentabilidade.desenvolvimento-sustentavel`, 994 tópicos de edital)

```
Matéria: sustentabilidade.desenvolvimento-sustentavel (Desenvolvimento sustentável, ODS e A3P)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, marcos internacionais e Agenda 2030": 34
- "2. A3P, compras sustentáveis e acessibilidade pública": 22
- "3. Política de sustentabilidade do Judiciário e Plano de Logística Sustentável": 18
- "4. Resíduos sólidos, clima e compromissos internacionais": 12
- "5. Pacto Global, ESG, economia ambiental e financiamento verde": 14

Já existem (não repita):
- Uma instituição reduz despesas operacionais, mas seu plano aumenta barreiras de acesso e d
- Uma equipe encontra referência a um plano de ação lançado na Rio-92 e tenta identificá-lo 
- Ao preparar relatório sobre a Agenda 2030, uma equipe precisa informar quantos objetivos e
- Um projeto quer associar consumo de materiais e descarte responsável ao objetivo correto d
- Em ação voltada à redução de desigualdades e à proteção ambiental, uma pessoa propõe usar 
- Uma unidade revisa seu plano A3P e encontra cartilha histórica com cinco eixos. Conforme a
- Uma instituição percebe consumo excessivo de copos descartáveis e primeiro questiona se é 
- Um prédio recém-reformado consome menos eletricidade, mas não oferece rota acessível às pe
- Uma unidade informa que o PLS serve apenas para contabilizar papel e que não precisa relac
- Uma revisão de PLS precisa ser proposta e o plano atualizado deve ser publicado. Qual divi
- Um setor pretende escolher apenas indicadores fáceis, excluindo descarbonização e diversid
- A unidade de sustentabilidade de um tribunal foi tratada como grupo temporário responsável
- No planejamento de sustentabilidade de um tribunal, há proposta de compensar emissões sem 
- Uma gestão decide reciclar todo papel depois de comprado, sem avaliar se a aquisição poder
- Um fornecedor organiza devolução de baterias usadas para destinação em circuito empresaria
- Uma cidade instala sistema de alerta para chuvas extremas e substitui veículos a diesel po
- Um projeto internacional afirma que o Acordo de Paris fixa a todos os países reduções numé
- Uma empresa informa que os dez princípios do Pacto Global são idênticos aos ODS. Qual orga
- Ao analisar cadeia produtiva, uma organização encontra indícios de trabalho infantil em fo
- Uma empresa divulga projeto ambiental, mas não registra conflitos de interesses ou riscos 
- Uma indústria declara redução de 25% nas emissões, mas altera silenciosamente ano-base e a
- Uma fábrica obtém lucro, mas despeja fumaça que aumenta gastos de saúde de moradores sem i
- Um projeto pretende valorar a redução de enchentes por área verde urbana usando custos evi
- Uma comunidade mantém vegetação que melhora serviços ecossistêmicos e celebra ajuste volun
- Uma empresa diz que qualquer crédito voluntário de carbono é automaticamente um título de 
- Ao planejar suas ações, uma instituição leva em conta necessidades atuais e efeitos sobre 
- A Agenda 2030 reproduziu os oito Objetivos de Desenvolvimento do Milênio sem acrescentar m
- A Agenda 21 se vincula à Rio-92, enquanto a Rio+20, realizada em 2012, integrou o percurso
- Na versão atual da A3P, o único eixo relativo a obras é o de compras públicas, pois constr
- A construção de acesso sem barreiras, com sinalização compreensível e atendimento acessíve
- O art. 2º da Resolução CNJ nº 400/2021 limita a sustentabilidade à economia de recursos am
- O Plano de Logística Sustentável deve integrar objetivos, indicadores e metas, articulando
- Segundo o art. 8º da Resolução CNJ nº 400/2021, a unidade de sustentabilidade pode institu
- A atualização de 2024 da Resolução CNJ nº 400/2021 passou a destacar descarbonização no PL
- A Resolução CNJ nº 400/2021 extinguiu a necessidade de tratar acessibilidade na sustentabi
- Na Política Nacional de Resíduos Sólidos, responsabilidade compartilhada envolve agentes a
- A Política Nacional sobre Mudança do Clima define mitigação como preparação de infraestrut
- O décimo princípio do Pacto Global trata de combater corrupção em suas diferentes formas, 
- O Pacto Global lançado em 2000 possui dezessete princípios com 169 metas e, por isso, pode
- Uma organização apresenta redução comprovada de consumo energético, políticas verificáveis
- Para evitar greenwashing, é suficiente adquirir título verde ou crédito de carbono e divul
- Quando uma atividade gera poluição e impõe despesas médicas não pagas pelo produtor às fam
- Uma estimativa monetária de custos evitados por parque urbano corresponde necessariamente 
- Um acordo voluntário remunera uma comunidade por ações verificáveis de conservação que fav
- No Sistema Brasileiro de Comércio de Emissões criado pela Lei nº 15.042/2024, qualquer cré
```

## Pedido 14: Coerência textual (`portugues.coerencia-textual`, 989 tópicos de edital)

```
Matéria: portugues.coerencia-textual (Coerência textual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Coerência textual: unidade de sentido, contexto e inferência": 17
- "2. Continuidade, progressão temática, relevância e organização dos parágrafos": 30
- "3. Coesão e os sete fatores de textualidade": 20
- "4. Não contradição, relações lógico-semânticas e leitura crítica": 17
- "5. Reescrita e preservação de sentido: técnicas e casos integrados": 16

Já existem (não repita):
- Leia: 'A ponte foi interditada depois da inspeção. Por isso, os ônibus foram desviados.' Q
- Em 'A horta começou pequena. Ela hoje abastece dez famílias', qual função a palavra 'ela' 
- Um parágrafo afirma: 'O bairro precisa de áreas verdes'. Em seguida, discute a sombra das 
- Em uma porta fechada lê-se 'Volto às 14 horas'. O aviso funciona porque seu destinatário c
- Compare: 'A loja abriu às 8h e fechou às 18h'. Como avaliar a compatibilidade das duas inf
- Leia: 'A biblioteca fechou devido a uma infiltração. Por isso, as atividades foram suspens
- Uma campanha cita e transforma o verso conhecido de uma canção para provocar humor. Que fa
- Considere 'Nem todos os convidados compareceram'. Qual conclusão é diretamente sustentada?
- Em 'O centro inaugurou uma sala de leitura. A sala oferece oficinas. As oficinas atendem m
- Um relatório sustenta a necessidade de reduzir lixo plástico. Entre parágrafos pertinentes
- Leia: 'A equipe pretende diminuir o desperdício com a campanha'. Qual reescrita mantém a i
- Uma mensagem afirma: 'O texto é compreensível, porém discordo da tese'. Qual fator não se 
- Considere 'Se houver neblina, a travessia será suspensa'. Qual alternativa altera a relaçã
- Em 'Lara encontrou Denise após ela retornar da viagem', a referência de 'ela' pode ser dis
- Um texto argumentativo repete 'é importante cuidar das praças' em quatro frases sem oferec
- Um poema descreve 'o vento conversando com as árvores', e a interpretação literal classifi
- Uma conclusão diz: 'A proposta diminuiu custos, mas exigiu investimento inicial. Logo, não
- Um relato diz: 'Sofia deixou o livro no ônibus. Ao chegar em casa, percebeu a ausência e v
- O texto informa: 'Todos os apartamentos do bloco foram vistoriados, exceto o 301'. Qual re
- No original, lê-se 'Embora o serviço tenha sido ampliado, a espera aumentou'. Qual alterna
- Uma campanha pergunta 'E se a cidade pudesse respirar?' para defender arborização, em refe
- Leia: 'O relatório foi entregue na quinta. Antes disso, os dados haviam sido revisados na 
- Um enunciado afirma 'É possível que haja alteração da data'. Qual reescrita rompe o grau d
- Um aviso diz 'A entrada ocorrerá somente depois da identificação'. Qual alteração criaria 
- Um parágrafo anuncia: 'O projeto beneficiou três regiões'. Em seguida, trata do centro, do
- No texto 'A feira foi cancelada por causa da tempestade. Por isso, não houve apresentações
- Um texto composto de frases ligadas por pronomes e conectivos é necessariamente coerente, 
- Em 'O museu abriu nova ala. Ela contém fotografias antigas', o pronome 'ela' retoma 'nova 
- O enunciado 'O parque abriu cedo e fechou à noite' é contraditório porque contém verbos qu
- Ao apresentar um tópico frasal sobre benefícios da leitura e desenvolver exemplos de ampli
- A frase 'Nem todas as inscrições foram aprovadas' permite afirmar que nenhuma inscrição fo
- No contexto de aviso afixado na entrada de uma sala, a expressão 'Retorno em dez minutos' 
- Uma propaganda retoma expressão de uma história conhecida, mas muda seu desfecho para prod
- Se um trecho diz 'A oficina abriu uma galeria. A galeria recebe fotografias. As fotografia
- Em 'Embora estivesse cansado, terminou a caminhada', substituir 'embora' por 'porque' pres
- A frase 'O jardineiro fechou os portões para proteger as mudas' expressa finalidade, e não
- Um texto pode permanecer globalmente coerente mesmo que um parágrafo apresente discussão d
- Uma pessoa pode compreender o propósito persuasivo e as razões de uma mensagem, discordar 
- A afirmação 'Talvez a reunião termine cedo' conserva integralmente seu sentido caso seja r
- A sequência 'O prédio foi entregue ontem. Antes disso, os documentos haviam sido revisados
- Do enunciado 'Algumas unidades receberam equipamentos novos' conclui-se, sem informação ad
- Uma narradora afirma: 'Ontem achei o lugar vazio; hoje estava lotado'. A oposição entre va
- Um texto diz que 'A iniciativa pretende ampliar o acesso'. A reescrita 'A iniciativa já am
- Se um parágrafo informa o problema e o seguinte apresenta consequências do mesmo problema,
- Em uma fábula, o enunciado 'a árvore aconselhou o viajante' é necessariamente incoerente, 
```

## Pedido 15: História da língua, níveis e funções da linguagem (`portugues.funcoes-linguagem-teoria`, 972 tópicos de edital)

```
Matéria: portugues.funcoes-linguagem-teoria (História da língua, níveis e funções da linguagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação histórica do português e conceitos de língua e linguagem": 11
- "2. Variação linguística, normas e níveis de linguagem": 13
- "3. Ato comunicativo: elementos, códigos e efeitos de sentido": 26
- "4. As seis funções da linguagem: marcas, exemplos e contrastes": 39
- "5. Casos integrados, leitura crítica e resolução de questões": 11

Já existem (não repita):
- Ao encontrar um aviso com a frase “Área restrita” acompanhada de um desenho de cadeado, um
- Em uma exposição, afirma-se que o português pertence às línguas românicas. Que explicação 
- Um catálogo apresenta poemas medievais redigidos em galego-português e explica sua relação
- Uma pessoa compara dois jornais: um de dois séculos atrás e outro publicado hoje, destacan
- Em regiões diferentes, o mesmo alimento é chamado “mandioca”, “aipim” ou “macaxeira”. Qual
- Uma pessoa escreve “Encaminho o documento solicitado” numa comunicação formal e diz “Já te
- Uma mensagem é escrita em português, fotografada e enviada por aplicativo. Considerando os
- Durante uma ligação com interferência, alguém pergunta “Alô, está me ouvindo agora?”. Qual
- Uma nota informa: “A temperatura registrada às 7 horas foi de 19 °C”. Qual é a função pred
- Uma estudante observa que “Saia já!” é uma ordem direta; ao analisar a frase isolada, iden
- Ao ouvir “Estou com uma alegria enorme por ter concluído o projeto!”, uma pessoa reconhece
- Um glossário afirma: “Arcaísmo é uma palavra ou construção associada a uso antigo”. Qual f
- Em “A brisa beija a beira”, o texto destaca repetição sonora e personificação. Qual função
- Uma pessoa escreve em chat “Bom dia! Você está aí?”, aguardando resposta antes de apresent
- Uma revisora distingue “norma-padrão” e “norma culta” numa discussão sobre práticas de esc
- Uma frase é dita presencialmente e depois transcrita em uma postagem. Qual análise evita c
- Duas versões de um aviso circulam: “Haverá manutenção amanhã” e “Amanhã, amanhã: o consert
- Uma mensagem de campanha informa um horário e conclui: “Inscreva-se agora para participar”
- Em um diálogo, a primeira pessoa diz “A conexão caiu”; a outra responde “Ainda está ouvind
- Um leitor compara “Ele afirmou: ‘Estou pronto’” a “Ele afirmou que estava pronto”. Que fen
- Em uma propaganda curta, a expressão “Cultive cultura” aparece com repetição sonora, mas a
- Uma pessoa diz “Está frio aqui” ao lado de uma janela aberta, olhando para quem pode fechá
- Ao ler uma postagem com o regionalismo “oxente” e a orientação “Traga sua garrafa”, um est
- Um texto para público leigo mantém siglas não explicadas e perde leitores, mesmo estando p
- Num anúncio, surge uma reformulação irônica de um verso conhecido. A questão pergunta sobr
- Ao comparar um texto medieval escrito em galego-português e uma notícia atual em português
- Um texto exibido na tela de um telefone deixa de ser linguagem verbal quando é enviado por
- Quando a mesma pessoa se expressa de modo diferente num encontro com amigos e numa apresen
- A existência de regras da norma-padrão demonstra que variedades populares não possuem regu
- Em uma ligação, a pergunta “Você me escuta?” pode priorizar a função fática quando serve p
- No enunciado “A palavra ‘saudade’ é um substantivo”, predomina a função poética, porque o 
- Um relatório escrito pode ser formal e um áudio pode ser informal; portanto, a modalidade 
- O Acordo Ortográfico promulgado pelo Decreto nº 6.583/2008 determina que todos os falantes
- Na frase “Registre sua participação”, a função apelativa pode predominar porque o enunciad
- Em “A ponte fechou ontem”, a ponte é necessariamente o canal pelo qual a mensagem chegou a
- A fala “Que alegria, consegui terminar!” pode destacar a função emotiva mesmo contendo inf
- Ao comparar “aipim” e “macaxeira” em regiões diversas, a análise deve ser obrigatoriamente
- Uma frase rimada em um convite pode mobilizar função poética e conativa ao mesmo tempo, ca
- Um narrador que relata “Lia disse que estava pronta” necessariamente usa discurso direto p
- Quando a mesma sigla é compreendida por especialistas, mas não por público leigo, pode hav
- Na frase “Está frio aqui”, o sentido comunicativo deve ser sempre classificado como refere
- Uma paródia que transforma expressão reconhecível de outro texto pode realizar intertextua
- Um sinal pictográfico de proibição acompanhado de frase em português configura linguagem e
- Em “Eu medi o consumo às oito horas e anotei 12 unidades”, a primeira pessoa não impede o 
- Se o enunciado explica que “o termo ‘rápido’ expressa velocidade”, a função dominante será
```

## Pedido 16: Direito de Família (`direito-civil.familia`, 971 tópicos de edital)

```
Matéria: direito-civil.familia (Direito de Família)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios, esponsais, casamento e dissolução": 20
- "2. Regimes de bens, pacto, outorga, união estável e bem de família": 27
- "3. Parentesco, filiação, adoção, poder familiar e patrimônio dos filhos": 21
- "4. Guarda, convivência, violência familiar e alimentos": 17
- "5. Tutela, curatela, decisão apoiada e aplicação integrada": 15

Já existem (não repita):
- Após promessa de casamento, uma pessoa desiste da cerimônia sem humilhar a outra nem prati
- Duas pessoas querem celebrar casamento, sendo uma delas de 15 anos e estando grávida. À lu
- Pessoa divorciada deseja novo casamento, mas a partilha do relacionamento anterior ainda n
- Um casal conclui a habilitação matrimonial e recebe certificado para celebrar casamento ci
- Após divórcio, uma pessoa descobre que ainda não se definiu a partilha dos bens comuns. Co
- Um casal maior e capaz celebra casamento sem convenção patrimonial válida. Segundo o CC, a
- Duas pessoas vivem publicamente como família, de modo contínuo e duradouro, sem impediment
- Uma criança é adotada regularmente e passa a integrar a família do adotante. Quanto aos ef
- Um cônjuge reconheceu, ao celebrar o casamento, a invalidade do vínculo; o outro desconhec
- Pessoas casadas pretendem alterar o regime patrimonial para separação convencional, sem pr
- Duas pessoas de 74 e 68 anos vão casar e querem comunhão parcial. Diante do art. 1.641, II
- Casados em comunhão parcial, um cônjuge presta fiança sem anuência do outro. A questão sob
- Pessoa casada mantém convivência matrimonial com o cônjuge e simultaneamente relação afeti
- Imóvel próprio onde reside uma família é cobrado por dívida de imposto predial incidente s
- Após o divórcio, um homem pretende casar-se com a ex-sogra, ascendente de sua antiga cônju
- O pai biológico pede reconhecimento de filiação de pessoa que já possui pai socioafetivo c
- Um filho de 19 anos descobre reconhecimento de filiação realizado por terceiro e expressa 
- O pai exerce poder familiar sobre adolescente de 17 anos, que recebeu imóvel por herança. 
- Pais aptos a exercer poder familiar discordam sobre guarda, mas o processo contém elemento
- Em disputa de convivência, uma mudança de domicílio pode estar ligada a ameaça real, embor
- Divorciados com filho menor desejam lavrar escritura consensual, mas a guarda e os aliment
- Uma pessoa adulta com deficiência intelectual manifesta vontade, compreende atos cotidiano
- Os pais deixam por testamento tutor para filho de 12 anos, mas a nomeação foi feita por ge
- Credor de pensão alimentícia busca penhorar imóvel residencial da família do devedor. Pelo
- Um filho menor de 16 anos exerce atividade profissional e recebe recursos por ela; em outr
- Um casal do mesmo sexo requer habilitação para casamento civil, e o registro a recusa apen
- No Código Civil, a promessa de casamento gera direito à celebração compulsória, bastando q
- Na ausência de pacto eficaz e de hipótese legal especial, o casamento submete-se à comunhã
- Para constituir união estável, o art. 1.723 do Código Civil exige convivência por pelo men
- O filho adotado regularmente possui os mesmos direitos e qualificações de filho de origem 
- A guarda compartilhada exige que a criança passe exatamente metade de cada mês na residênc
- Se pais morrem e deixam filho de doze anos sem exercício de poder familiar, a tutela pode 
- Uma pessoa de dezessete anos quer casar e apresenta autorização dos responsáveis conforme 
- Uma pessoa maior de setenta anos pode escolher regime patrimonial diferente da separação l
- A mera assinatura de contrato particular antes do casamento é suficiente para constituir p
- O reconhecimento socioafetivo de paternidade não impede, por si, o estabelecimento concomi
- A guarda unilateral extingue, por sua própria natureza, o poder familiar do genitor que nã
- Segundo a Lei 5.478/1968, art. 4º, no despacho inicial de ação de alimentos podem ser fixa
- A existência de filho comum menor impede qualquer escritura extrajudicial de divórcio cons
- A impenhorabilidade legal do imóvel residencial não impede execução nas hipóteses expressa
- A apresentação de alegação de alienação parental obriga a fixação de guarda compartilhada 
- Pai administrador dos bens do filho menor pretende hipotecar imóvel deste. A medida depend
- A condição de pessoa com deficiência produz incapacidade absoluta para atos civis e impõe 
- Na tomada de decisão apoiada, a própria pessoa com deficiência escolhe pelo menos dois apo
- Na fixação de alimentos de filho menor, o art. 1.694, § 1º, do Código Civil estabelece per
```

## Pedido 17: Computação em nuvem (`informatica.computacao-nuvem`, 969 tópicos de edital)

```
Matéria: informatica.computacao-nuvem (Computação em nuvem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e características da computação em nuvem": 16
- "2. Modelos de implantação e modelos de serviço": 22
- "3. Armazenamento e colaboração: Google Drive e OneDrive": 23
- "4. Sincronização, cópias de segurança e recuperação": 22
- "5. Segurança, privacidade e decisões em serviços de nuvem": 17

Já existem (não repita):
- Uma pessoa contrata um serviço que permite solicitar espaço de armazenamento pelo painel e
- Uma organização disponibiliza na nuvem um editor de documentos pronto, acessado pelo naveg
- Uma empresa contrata processamento e instala por conta própria o sistema operacional e os 
- Uma infraestrutura de nuvem é destinada exclusivamente às unidades de uma única organizaçã
- Ao conceder acesso a um relatório no Google Drive, a proprietária deseja que a pessoa leia
- Um usuário vê no Explorador um arquivo do OneDrive marcado como somente online. Ele viajar
- Uma pessoa exclui por engano um arquivo de uma pasta sincronizada. A exclusão é reproduzid
- Uma rotina copia todos os arquivos selecionados no domingo e, na segunda-feira, apenas os 
- Uma usuária ativou autenticação multifator na conta de armazenamento. Qual efeito de segur
- Uma plataforma aumenta automaticamente a capacidade durante o pico de acessos e a reduz qu
- Uma equipe entrega seu código a um ambiente gerenciado que executa a aplicação, enquanto o
- Três instituições independentes, sujeitas a requisitos semelhantes de confidencialidade, c
- Uma pessoa utiliza o Google Drive para computador e deseja que todos os arquivos do Meu Dr
- Uma política de cópias define backup completo no domingo, diferencial na segunda e diferen
- Uma equipe definiu perda máxima tolerável de alterações das últimas duas horas e pretende 
- Uma conta de armazenamento foi acessada com senha válida por pessoa não autorizada. Os doc
- Uma organização usa um aplicativo SaaS e concede, por engano, permissão de edição de docum
- Uma plataforma declara capacidade de atender a aumento contínuo de tráfego e oferece també
- Uma equipe mantém dados reservados em nuvem privada e, por integração própria, desloca pro
- Uma organização compara soluções de armazenamento: no serviço X, seus técnicos administram
- Uma equipe utiliza streaming no Google Drive para computador, sem marcar o arquivo X como 
- Um documento foi concedido a um colaborador como editor no Google Drive. O proprietário de
- Uma política executa backup completo no domingo, incremental na segunda e diferencial na t
- No domingo houve backup completo; na segunda, incremento I1; na terça, incremento I2. A eq
- Uma organização guarda três versões acessíveis na mesma conta sincronizada, com as mesmas 
- Uma plataforma permite que o consumidor crie recursos computacionais automaticamente pelo 
- Um serviço de nuvem que mede o consumo de armazenamento e processamento precisa obrigatori
- Se uma organização contratar nuvem de uso exclusivo administrada por terceiro em instalaçõ
- Uma pessoa utiliza um serviço de correio eletrônico oferecido como aplicativo pronto no na
- Ao compartilhar um documento do Google Drive com outra pessoa e conceder a ela o papel de 
- Depois que o usuário sincroniza uma pasta do computador com um serviço remoto, todas as al
- Um fornecedor atende clientes distintos usando recursos computacionais compartilhados e al
- Quatro organizações com requisitos regulatórios semelhantes compartilham uma infraestrutur
- Um arquivo do Meu Drive é exibido por streaming no gerenciador de arquivos, mas não foi ab
- Uma usuária compartilha um arquivo do OneDrive somente com pessoas específicas e, posterio
- Foi feito um backup completo no domingo, incremental na segunda e diferencial na terça. Po
- Uma organização fixa RPO de trinta minutos e RTO de quatro horas para um sistema de arquiv
- Um arquivo remoto é criptografado durante o armazenamento e na transmissão, mas o propriet
- Uma aplicação cresce adicionando instâncias quando o tráfego sobe, mas mantém todas as ins
- Duas equipes contratam serviços distintos: na primeira, o consumidor deve atualizar o sist
- Uma instituição utiliza uma nuvem privada para um aplicativo e, sem integração de dados ou
- Um arquivo individual do Google Drive está dentro de pasta à qual uma pessoa tem acesso de
- Uma conta do OneDrive possui histórico de versões. Por esse motivo, uma equipe pode conclu
- Uma pessoa entra em conta de nuvem com senha e verificação adicional no celular, mas ainda
- Um fornecedor informa que utiliza redundância para manter alta disponibilidade do serviço.
```

## Pedido 18: Estatuto da Pessoa com Deficiência (Lei 13.146/2015) (`legislacao.lei-13146-2015-inclusao`, 964 tópicos de edital)

```
Matéria: legislacao.lei-13146-2015-inclusao (Estatuto da Pessoa com Deficiência (Lei 13.146/2015))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Deficiência, barreiras, igualdade e atendimento prioritário": 21
- "2. Capacidade civil, apoio, curatela e direito à vida": 19
- "3. Habilitação, saúde, educação, trabalho e proteção social": 18
- "4. Moradia, cultura, transporte, acessibilidade e participação": 20
- "5. Acesso à justiça e crimes previstos no Estatuto": 22

Já existem (não repita):
- Uma pessoa com impedimento sensorial de longo prazo utiliza tecnologias assistivas, mas en
- Uma entidade pretende reconhecer deficiência apenas com exame clínico realizado por um úni
- Uma escola recusa adaptação razoável solicitada por estudante com deficiência, embora a me
- Uma pessoa com deficiência e seu acompanhante solicitam prioridade na restituição do impos
- Uma pessoa adulta com deficiência deseja casar-se e decidir sobre planejamento familiar. A
- Em emergência de saúde com risco de morte, uma pessoa com deficiência não consegue express
- Uma pessoa com deficiência necessita adquirir habilidades para exercer atividades cotidian
- Uma instituição privada de ensino decide cobrar valor adicional na mensalidade de estudant
- Um serviço de saúde tem notícia de suspeita de violência psicológica contra paciente com d
- Em seleção profissional, pretende-se eliminar candidatos com deficiência sob o argumento d
- Em programa habitacional subsidiado com recursos públicos, será construído conjunto de 200
- Uma pessoa com deficiência sem meios próprios ou familiares de subsistência solicita benef
- Uma instituição cultural pretende disponibilizar obra em formato acessível para leitores c
- Um estacionamento de uso coletivo tem 150 vagas. Para atender à regra quantitativa do art.
- Uma instituição de ensino superior recebe pedido de tempo adicional para avaliação de pess
- Uma pessoa com deficiência deseja votar em local acessível e, por solicitação própria, con
- Uma pessoa com deficiência precisa atuar como testemunha em audiência, mas o ambiente de p
- Uma empresa com representação comercial no Brasil lança site que não funciona com leitor d
- Em razão de limitações concretas de gestão de patrimônio, um juiz cogita curatela para pes
- Uma pessoa adulta com deficiência quer contar com apoio de pessoas de sua confiança para d
- Uma pessoa publica mensagens destinadas a incitar discriminação contra pessoas com deficiê
- Um curador desvia para conta própria rendimentos mensais de pessoa com deficiência. Consid
- Uma pessoa com deficiência é abandonada em entidade de acolhimento, sem lesão grave ou mor
- Um tutor retém cartão bancário de pessoa com deficiência, usado para recebimento de benefí
- Em abandono de pessoa com deficiência nas condições do art. 90, a conduta produz morte. Qu
- Uma pessoa com impedimento mental de longo prazo encontra barreiras ambientais e atitudina
- Uma pessoa com deficiência oculta deseja atendimento sem usar cordão de girassóis. O art. 
- Uma empresa recusa ajuste razoável necessário para que pessoa com deficiência utilize seu 
- Em pronto atendimento, uma pessoa com deficiência recebe prioridade absoluta perante todos
- Uma pessoa com deficiência opta por constituir união estável. A LBI assegura a preservação
- Uma pessoa com deficiência submetida a curatela fica automaticamente impedida de votar e d
- Um serviço de reabilitação organiza recursos de tecnologia assistiva e apoio técnico para 
- Um paciente com deficiência em observação hospitalar tem negada a permanência de acompanha
- Uma escola privada deve cumprir os deveres de acessibilidade expressamente alcançados pelo
- Para receber a aposentadoria prevista no art. 41 da LBI, basta ser pessoa com deficiência,
- Em programa habitacional subsidiado com recursos públicos, a pessoa com deficiência tem pr
- A reserva de 2% das vagas de estacionamento prevista no art. 47 da LBI assegura vagas para
- Uma sala de cinema exige ingresso de valor mais alto da pessoa com deficiência em razão do
- Um portal governamental oferece atendimento presencial acessível e, por isso, está liberad
- No exercício dos direitos políticos, a pessoa com deficiência pode solicitar ajuda na vota
- O art. 88, § 2º, da LBI prevê apenas aumento de um terço sobre a pena básica quando a disc
- Um curador que desvia os rendimentos de pessoa com deficiência pode incidir no art. 89 da 
- Após a Lei 15.163/2025, o art. 90 da LBI permaneceu com pena básica de seis meses a três a
- A atualização promovida pela Lei 15.249/2025 incluiu no art. 24 da LBI dever relativo à co
- O art. 91 da LBI prevê aumento de um terço na retenção de cartão para obtenção de vantagem
```

## Pedido 19: Código de Ética Médica (Resolução CFM 2.217/2018) (`medicina.codigo-etica-medica`, 964 tópicos de edital)

```
Matéria: medicina.codigo-etica-medica (Código de Ética Médica (Resolução CFM 2.217/2018))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Preâmbulo, princípios fundamentais e direitos do médico": 25
- "2. Responsabilidade, direitos humanos e relação com pacientes": 25
- "3. Sigilo, documentos médicos e prontuário": 25
- "4. Auditoria, perícia, pesquisa, publicidade e disposições gerais": 25

Já existem (não repita):
- O Código de Ética Médica aplica-se também às atividades de ensino, pesquisa e administraçã
- Pela redação dada pela Resolução CFM 2.222/2018, o Código de Ética Médica é composto de 26
- A fiscalização do cumprimento das normas do Código de Ética Médica é atribuição exclusiva 
- Segundo os princípios fundamentais, o médico exerce a profissão com autonomia e não é obri
- De acordo com o Código de Ética Médica, a natureza personalíssima da atuação profissional 
- O médico que decide suspender suas atividades, individual ou coletivamente, porque a insti
- É direito do médico internar e assistir seus pacientes em hospitais públicos e privados, a
- O médico que pratica ato a pedido do paciente e com o seu consentimento fica isento de res
- Segundo o art. 1º do Código de Ética Médica, é vedado ao médico causar dano ao paciente, p
- É vedado ao médico deixar de atender em setores de urgência e emergência, quando for sua o
- Na ausência de médico plantonista substituto, quem deve providenciar a substituição, segun
- O médico deve esclarecer o trabalhador sobre condições de trabalho que ponham em risco sua
- É vedado ao médico deixar de obter consentimento do paciente ou de seu representante legal
- Diante de pessoa capaz física e mentalmente em greve de fome, o Código de Ética Médica det
- Quando a comunicação direta do diagnóstico e do prognóstico puder provocar dano ao pacient
- O médico pode renunciar ao atendimento quando fatos prejudicarem o bom relacionamento com 
- Segundo o art. 37 do Código de Ética Médica, o atendimento médico a distância, nos moldes 
- Nos casos de doença incurável e terminal, o Código de Ética Médica determina que o médico:
- É vedado ao médico integrante da equipe de transplante participar do processo de diagnósti
- O médico em função de chefia ou de auditoria pode desrespeitar livremente a prescrição ou 
- É vedado ao médico revelar fato de que tenha conhecimento em virtude do exercício da profi
- Chamado a depor como testemunha sobre fato coberto pelo sigilo profissional, o médico deve
- Em relação a paciente adolescente com capacidade de discernimento, o Código de Ética Médic
- O médico do trabalho pode revelar informações confidenciais obtidas em exame de trabalhado
- Quanto às empresas seguradoras, o médico pode prestar informações sobre as circunstâncias 
- É vedado ao médico fazer referência a casos clínicos identificáveis ou exibir pacientes re
- Segundo o art. 84 do Código de Ética Médica, é vedado ao médico deixar de atestar óbito de
- O prontuário médico deve ser legível, preenchido em ordem cronológica com data, hora, assi
- O médico pode liberar cópias do prontuário sob sua guarda:
- É vedado ao médico negar ao paciente acesso a seu prontuário ou deixar de lhe fornecer cóp
- Entre as vedações relativas à remuneração profissional, o Código de Ética Médica proíbe:
- É vedado ao médico assinar laudos periciais, auditoriais ou de verificação médico-legal ca
- Segundo o art. 93 do Código de Ética Médica, NÃO há impedimento para que o médico atue com
- Quando em função de perito, o médico pode fazer apreciações sobre o caso na presença do ex
- O Código de Ética Médica veda a realização de exames médico-periciais de corpo de delito e
- É vedado ao médico perito ou auditor receber remuneração ou gratificação vinculada à glosa
- Quando na função de auditor ou de perito, o médico pode modificar procedimentos terapêutic
- Na pesquisa com criança, adolescente ou pessoa com capacidade de discernimento diminuída, 
- Nos anúncios profissionais em que anuncia sua especialidade, o médico deve incluir:
- Segundo as disposições gerais do Código de Ética Médica, o médico portador de doença incap
```

## Pedido 20: Geometria plana (`matematica.geometria-plana`, 962 tópicos de edital)

```
Matéria: matematica.geometria-plana (Geometria plana)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Elementos básicos, ângulos, paralelas e polígonos": 20
- "2. Congruência, semelhança, Tales e bissetriz": 21
- "3. Triângulo retângulo, relações métricas e trigonometria elementar": 20
- "4. Perímetros, áreas de polígonos e figuras compostas": 20
- "5. Circunferências, arcos, tangentes, setores, coroas e regiões mistas": 19

Já existem (não repita):
- Duas retas paralelas são cortadas por uma transversal. Um ângulo alterno interno mede 68°.
- Em um triângulo, os ângulos internos medem 42°, 73° e x. Qual é a medida de x?
- Um quintal retangular mede 9 m de comprimento e 4 m de largura. Qual é sua área?
- Um triângulo retângulo possui catetos de 5 cm e 12 cm. Qual é a medida da hipotenusa?
- Um círculo tem raio de 3 m. Qual é a sua área exata, em metros quadrados?
- Um hexágono convexo possui seis vértices. Quantas diagonais ligando vértices não vizinhos 
- Dois triângulos são semelhantes e a razão entre lado maior do segundo e lado correspondent
- Um trapézio tem bases paralelas de 8 cm e 14 cm e altura perpendicular de 5 cm. Qual é a á
- Um ângulo inscrito em circunferência intercepta arco de 120°. Qual é a medida desse ângulo
- Uma placa quadrada tem lado de 2 m. Quanto mede sua área em centímetros quadrados?
- Duas hastes medem 7 cm e 11 cm. Qual valor pode ser o comprimento de uma terceira haste pa
- Três paralelas recortam duas transversais. Numa delas, trechos consecutivos medem 4 cm e 6
- Dois triângulos apresentam, em correspondência, lados de 5 cm e 8 cm envolvendo um ângulo 
- Uma rampa de comprimento 12 m forma ângulo de 30° com o piso horizontal. Qual é a altura v
- Dois pontos da malha cartesiana são A(2,4) e B(8,10). Qual é o ponto médio do segmento AB?
- Um setor circular tem raio 6 cm e ângulo central de 60°. Qual é o comprimento do arco que 
- A bissetriz interna parte do vértice de um triângulo cujos lados adjacentes medem 16 cm e 
- De um ponto externo P a uma circunferência traçam-se duas tangentes nos pontos A e B. Sabe
- Uma pista tem formato de setor de 90° de uma coroa circular concêntrica, com raio externo 
- Um hexágono regular de lado 4 cm foi dividido pelo centro em seis triângulos equiláteros c
- Dois polígonos semelhantes têm razão entre perímetro do maior e do menor igual a 5/2. A ár
- Os pontos A(−2,3), B(4,3) e C(4,11) são vértices de triângulo. Qual é seu perímetro, em un
- Uma circunferência de raio 5 cm circunscreve um quadrado, com os quatro vértices deste sob
- Em círculo de raio 4 cm, uma corda delimita arco menor de 90° e o segmento circular menor 
- Uma praça retangular mede 12 m por 8 m. Retira-se um quadrado de lado 3 m de um canto, sem
- Um ângulo externo de triângulo mede a soma dos ângulos internos situados nos dois vértices
- Um octógono convexo tem 28 diagonais porque o número de diagonais de um polígono de oito l
- O perímetro de um quadrado de lado 7 cm é 28 cm, e a área desse mesmo quadrado é 49 cm².
- Triângulos que apresentam os três ângulos internos correspondentes congruentes são necessa
- Em uma circunferência, a tangente no ponto T é perpendicular ao raio que liga o centro ao 
- Em qualquer triângulo de lados 5 cm, 6 cm e 9 cm, o teorema de Pitágoras permite obter a á
- Se as projeções dos catetos sobre a hipotenusa de um triângulo retângulo medem 4 cm e 9 cm
- Dois lados correspondentes iguais e um ângulo oposto a um desses lados igual formam o caso
- Um polígono regular com doze lados possui cada ângulo externo medindo 30° e cada ângulo in
- O comprimento do contorno de um setor circular de 90° e raio 8 cm é somente 4π cm, pois os
- Uma planta em escala 1:50 representa uma região de área 12 cm². O terreno correspondente p
- Para dois pontos A(1,2) e B(4,6), a distância euclidiana é 7 unidades, pois as diferenças 
- Se D e E pertencem respectivamente aos lados AB e AC do triângulo ABC, e DE é paralelo a B
- Se uma bissetriz interna divide ângulo de vértice de triângulo em dois ângulos iguais, ent
- O segmento circular menor determinado por uma corda que subtende ângulo central de 90° em 
- No quadrado circunscrito a uma circunferência de raio r, o lado do quadrado mede r vezes r
- Um hexágono regular de lado 4 cm possui perímetro de 24 cm e apótema de 2 raiz de 3 cm; po
- Um paralelogramo com lado oblíquo de 10 cm, base 8 cm e altura perpendicular à base de 6 c
- Um retângulo de 12 m por 8 m perde um recorte quadrado de lado 3 m num canto. A área reman
- Dois ângulos inscritos em uma mesma circunferência têm medidas necessariamente iguais, ind
```

## Pedido 21: Roraima: história, geografia e economia (`roraima.conhecimentos-estaduais`, 957 tópicos de edital)

```
Matéria: roraima.conhecimentos-estaduais (Roraima: história, geografia e economia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ocupação territorial, presença portuguesa e fronteiras": 20
- "2. Do Território Federal ao Estado: política, municípios e migração": 20
- "3. Povos indígenas, terras, patrimônios e destinos turísticos": 20
- "4. Geografia física: relevo, lavrado, solos, clima e águas": 20
- "5. Economia, redes de circulação e desenvolvimento regional": 20

Já existem (não repita):
- Os povos indígenas habitavam o território correspondente ao atual Roraima antes da instala
- O Forte São Joaquim do Rio Branco foi construído em 1775 como parte da estratégia portugue
- Boa Vista é a capital de Roraima e o estado possui atualmente quinze municípios.
- O lavrado é uma denominação regional para paisagens de savana, com campos abertos e árvore
- O rio Branco é formado pela confluência dos rios Uraricoera e Tacutu.
- A Serra do Tepequém fica em Amajari e é associada tanto a um passado de garimpo quanto ao 
- A transformação de Roraima em Estado federado ocorreu em 1962, quando o antigo território 
- A decisão arbitral de 1904 sobre a Questão do Pirara foi proferida por Vítor Emanuel III, 
- Na condição de território federal, Roraima elegia diretamente seus governadores exatamente
- A Terra Indígena Yanomami está localizada exclusivamente no município de Boa Vista e corre
- A concentração das chuvas em parte de Roraima ocorre no meio do ano, diferentemente do pad
- A BR-174 é uma importante via terrestre que conecta Manaus a Boa Vista e prossegue em dire
- O fato de as ruínas do Forte São Joaquim integrarem o patrimônio histórico de Roraima sign
- Embora a Constituição tenha transformado Roraima em Estado em outubro de 1988, sua instala
- O reconhecimento constitucional dos direitos originários indígenas permite equiparar jurid
- É inadequado interpretar a expansão de soja sobre áreas do lavrado como ambientalmente neu
- Por ser o rio principal de Roraima, o rio Branco pertence à bacia do Tocantins-Araguaia e 
- A chamada interiorização, no âmbito da Operação Acolhida, designa principalmente o desloca
- Um edital pergunta qual instalação materializa a política portuguesa de defesa no vale do 
- Qual associação entre data e processo de formação política de Roraima está correta?
- Assinale o par correto de países fronteiriços com Roraima.
- Qual dos elementos naturais caracteriza o lavrado roraimense?
- O rio Branco é formado, sobretudo, pela união de quais rios?
- Ao elaborar um painel sobre a organização territorial atual, qual informação deve aparecer
- Que povo indígena é um dos presentes em Roraima?
- Em qual município se localiza a Serra do Tepequém?
- Qual via é fundamental para a ligação rodoviária entre Manaus, Boa Vista e Pacaraima?
- Ao explicar a Questão do Pirara, um candidato deverá afirmar que a disputa dizia respeito 
- Um professor afirma que a figura do governador mudou com a estadualização. Qual proposição
- O primeiro governador eleito que assumiu quando Roraima foi instalado como Estado, em 1991
- Qual par de terras indígenas é destacado de forma correta na história contemporânea roraim
- Qual definição caracteriza melhor o conceito de tombamento, quando aplicado ao patrimônio 
- Em relação ao regime de precipitações, qual alternativa evita generalização equivocada sob
- Assinale a sequência hidrográfica correta para as águas do rio Branco.
- A criação do território em 1943 foi relacionada, entre outros fatores, à:
- Uma matéria relaciona migração venezuelana, Pacaraima e Operação Acolhida. Qual é a associ
- Ao analisar uma narrativa histórica, a banca apresenta o seguinte argumento: “Os aldeament
- Um examinador mistura Questão do Pirara, criação do território e instalação do Estado. Qua
- Considere a discussão sobre a Terra Indígena Raposa Serra do Sol. Qual conclusão está alin
- Um relatório menciona uma paisagem aberta do norte roraimense e conclui que toda a área er
- Sobre o antigo Forte São Joaquim, considere suas relações com o relevo e a drenagem. O loc
- Ao comparar o peso dos setores econômicos com notícias sobre a expansão da soja, assinale 
- Em uma questão ambiental sobre a Terra Indígena Yanomami, qual relação causal é mais defen
- Um servidor público precisa classificar três atrativos: Monte Roraima, Viruá e Orla Tauman
- No exame do uso da terra no lavrado, a alternativa mais adequada a uma política pública de
```

## Pedido 22: Juros simples e compostos (`matematica.juros-simples-compostos`, 952 tópicos de edital)

```
Matéria: matematica.juros-simples-compostos (Juros simples e compostos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos: dinheiro no tempo, capital, montante, taxa e prazo": 14
- "2. Juros simples: crescimento linear e resolução de incógnitas": 30
- "3. Juros compostos: crescimento exponencial, VP, VF e logaritmos": 28
- "4. Comparação de regimes, taxa nominal, efetiva e capitalização contínua": 16
- "5. Problemas integrados: isolamentos, cronologia e interpretação de resultados": 12

Já existem (não repita):
- Um capital de R$ 1.000 é aplicado durante três meses à taxa de 2% ao mês, em juros simples
- Uma dívida de R$ 2.000 acumula juros simples de 5% ao mês durante quatro meses. Qual monta
- Um enunciado informa taxa de 0,75% ao mês. Qual número deve ser usado como taxa decimal na
- Um investimento de R$ 1.000 rende 10% por mês, com capitalização composta, durante dois me
- Uma aplicação de R$ 2.000 permanece dois meses a juros compostos de 5% ao mês. Qual é o ju
- Uma operação em juros simples adota taxa de 24% ao ano e mês comercial como fração de um a
- Um pagamento único de R$ 1.320 ocorrerá em dois meses. À taxa simples de 10% ao mês, qual 
- Uma quantia de R$ 5.000 resulta em R$ 5.600 após três meses de juros simples. Qual taxa me
- Uma aplicação de R$ 1.200 alcançou R$ 1.488 em juros simples de 4% ao mês. Qual foi o praz
- Um capital de R$ 1.000 transforma-se em R$ 1.440 após dois períodos compostos iguais. Qual
- Uma aplicação composta de R$ 1.000 passa a R$ 1.331, com taxa de 10% a cada período. Quant
- Uma quantia futura de R$ 2.420 será recebida em dois períodos, com taxa composta de 10% po
- Uma taxa efetiva de 10% por semestre incide por dois semestres em capitalização composta. 
- Aplicam-se R$ 1.000 por três períodos à taxa de 10% por período. Quanto o montante compost
- Um saldo devedor de R$ 10.000 passa por dois meses de carência sem pagamento, capitalizand
- Sob convenção de mês comercial de 30 dias, R$ 2.000 são aplicados a juros simples de 3% ao
- Uma dívida de R$ 500 é corrigida por juros compostos de 10% por período durante dois perío
- Um capital de R$ 1.600 passa a R$ 1.936 em dois períodos compostos iguais. Qual taxa efeti
- Um investimento único de R$ 1.000 rende sob capitalização contínua à intensidade de 10% ao
- Um contrato adota capitalização mista: juros compostos nos dois meses completos e simples 
- Um contrato anuncia 18% ao ano nominais com capitalização semestral e rateio proporcional 
- Em meio período, um capital de R$ 1.000 é submetido à taxa de 21% por período. Compare jur
- O montante de uma única aplicação sob capitalização contínua, à intensidade de 10% por per
- Para uma taxa de 5% ao mês durante 2,5 meses, a convenção mista aplica dois meses composto
- Um capital de R$ 1.500 se transforma em R$ 2.160 após dois períodos compostos. Qual taxa e
- Uma aplicação de R$ 1.000 à taxa de 3% simples ao mês, por quatro meses, produz R$ 120 de 
- Se uma operação informa taxa de juros de 2% ao mês, o número correto a inserir em uma fórm
- Em juros simples, um principal de R$ 1.000 submetido a 10% por período durante dois períod
- Um empréstimo de R$ 1.000 a 10% compostos por dois meses acarreta exatamente R$ 200 de jur
- Se o contrato adota taxa zero, uma aplicação única de R$ 5.000 conserva montante de R$ 5.0
- Ao fim de uma aplicação simples de R$ 2.000 com R$ 200 de juros, o montante da operação é 
- Considerando um ano com doze meses, a taxa simples de 24% ao ano é proporcional à taxa de 
- Uma aplicação composta de 10% por semestre possui taxa efetiva anual de 20%, já que as dua
- Em juros compostos de 10% por período, um capital de R$ 1.000 alcança R$ 1.331 após três p
- Se o capital de R$ 1.000 rende 12% compostos por período durante dois períodos, seu fator 
- Um recebimento único de R$ 2.420 ocorrerá após dois períodos a 10% compostos por período. 
- Se um contrato prevê dois meses de carência sem parcelas e estabelece capitalização compos
- Para um capital único de R$ 1.210 com vencimento em dois períodos à taxa composta de 10% p
- A taxa de 10% nominal anual, com capitalização trimestral e rateio proporcional, correspon
- Em capitalização contínua, uma aplicação de R$ 1.000 com intensidade de 10% ao ano durante
- Para meio período com taxa positiva de 21% por período, a capitalização composta exponenci
- Pela convenção mista, um capital de R$ 1.000 a 10% por período, por um período e meio, acu
- Para descobrir o prazo de uma operação composta, com capital, montante e taxa dados, deve-
- Para comparar capitais únicos disponíveis em datas diferentes, a matemática financeira dev
- Um contrato anuncia taxa de 20% nominais ao ano com capitalização semestral e rateio propo
```

## Pedido 23: Tipologia textual (`portugues.tipologia-textual`, 951 tópicos de edital)

```
Matéria: portugues.tipologia-textual (Tipologia textual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tipos, gêneros e sequências: narração e descrição": 23
- "2. Exposição, explicação e argumentação: operações e organização retórica": 24
- "3. Injunção, diálogo e textos classificados pela finalidade": 28
- "4. Estrutura textual, progressão de parágrafos, dissertação e cartas": 12
- "5. Texto literário e não literário; frase, entonação e ordem": 13

Já existem (não repita):
- Leia: 'A campainha tocou. Helena abriu a porta, encontrou um envelope e correu até a varan
- Leia: 'O corredor era comprido, de paredes azuis; ao fundo, uma janela estreita iluminava 
- Um texto afirma: 'A evaporação é a passagem da água líquida para o estado gasoso; ocorre t
- Num folheto, lê-se: 'Retire a tampa, encaixe o filtro e pressione o botão por três segundo
- Observe: '— Você recebeu o aviso? — Recebi ontem. — Pode encaminhá-lo? — Claro, envio agor
- Leia: 'O museu permanecerá aberto de terça a sábado, das 9 às 17 horas. Aos domingos, ence
- Um anúncio afirma: 'Conheça o novo tênis leve, resistente à água e feito para acompanhar s
- No trecho 'A madrugada vestiu de prata os telhados', a escolha verbal afasta a leitura est
- Um regulamento de acesso informa: 'O visitante deverá apresentar identificação na entrada;
- Um material de ensino diz: 'Uma dezena reúne dez unidades. Por exemplo, duas dezenas corre
- Um texto diz: 'Os astros indicam que um reencontro inesperado trará novas escolhas na próx
- Uma campanha traz o enunciado: 'Compartilhe livros que você já leu. Uma comunidade leitora
- Leia: 'As ruas alagaram porque a rede de drenagem não suportou o volume de chuva em poucas
- Um texto explica: 'Os resíduos podem ser agrupados, quanto à origem, em domésticos, indust
- Um parágrafo começa com 'A arborização melhora o conforto urbano' e acrescenta dados sobre
- Um texto começa por 'As áreas de descanso deveriam ser ampliadas', apresenta benefícios e 
- Uma mensagem traz vocativo, relato de férias, despedida e assinatura. Seu objetivo é compa
- Considere 'No silêncio da sala, surgiram as primeiras respostas.' Qual análise separa adeq
- No bilhete 'Tomara que todos cheguem em segurança!', a exclamação acompanha um desejo. Qua
- A frase 'Na manhã seguinte, os técnicos revisaram o projeto' é comparada a 'Os técnicos re
- Leia em regulamento: 'Antes de entrar, identifique-se. É obrigatório guardar os equipament
- Um artigo apresenta dados sobre mobilidade e, no último parágrafo, afirma que a cidade dev
- Uma correspondência inicia com saudação e afirma: 'Solicito que o horário seja ampliado, p
- Uma peça publicitária emprega a frase 'Seu futuro ganha asas'. Que critério impede classif
- Uma exposição diz: 'Todo material reciclável exige triagem adequada. No caso do vidro, a s
- Uma notícia relata eventos em ordem temporal e também descreve o local dos acontecimentos.
- Um texto que descreve uma paisagem precisa estar integralmente no pretérito perfeito, pois
- O enunciado 'A fotossíntese é um processo que transforma energia luminosa em energia quími
- Em uma receita, as frases 'misture os ingredientes' e 'leve ao forno' são expositivas por 
- Um anúncio que enumera vantagens de um aplicativo e conclama o público a experimentá-lo po
- O tópico frasal de um parágrafo deve ser a primeira frase; quando a ideia principal surge 
- A frase 'Que alegria!' é nominal e exclamativa: não apresenta verbo e expressa reação afet
- No fragmento 'A praça fechou porque começou uma tempestade', a presença de 'porque' obriga
- Uma sequência expositiva pode generalizar sobre reciclagem e depois especificar o caso do 
- O texto 'Amanhã haverá manutenção no sistema, segundo calendário divulgado' é obrigatoriam
- Um texto de estudo que apresenta conceito, exemplos graduados e exercícios de fixação poss
- Uma carta argumentativa deixa de ser gênero epistolar ao incluir tese e razões; por isso, 
- O enunciado 'Você poderia enviar o arquivo?' possui forma interrogativa, mas pode desempen
- Um regulamento com frases como 'O usuário deverá manter os corredores livres' tem finalida
- Um relato literário inicia pelo desfecho, retorna aos fatos anteriores e termina antes de 
- Um anúncio com a imagem verbal 'Esta cidade merece respirar' é texto literário em qualquer
- Uma carta comercial solicita regularização de cadastro, expõe documentos faltantes e acres
- Um parágrafo apresenta tese, traz um exemplo e considera objeção, mas não contém 'portanto
- A frase 'Na varanda dormiam os gatos' é declarativa e apresenta inversão em relação à orde
- Uma campanha para incentivar doação de sangue e um anúncio de desconto em roupas pertencem
```

## Pedido 24: ECA: direitos fundamentais, medidas de proteção e Conselho Tutelar (`legislacao.eca-direitos-fundamentais`, 944 tópicos de edital)

```
Matéria: legislacao.eca-direitos-fundamentais (ECA: direitos fundamentais, medidas de proteção e Conselho Tutelar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Proteção integral, direitos fundamentais e primeira infância": 22
- "2. Convivência familiar, acolhimento, poder familiar e adoção": 23
- "3. Prevenção, política de atendimento, entidades e Sistema de Garantia dos Direitos": 17
- "4. Medidas de proteção e providências aplicáveis aos pais ou responsáveis": 19
- "5. Conselho Tutelar, Ministério Público e tutela judicial de direitos": 19

Já existem (não repita):
- Uma pessoa completou doze anos nesta semana. Para aplicação das definições etárias do ECA,
- Uma unidade pública organiza plano de atendimento preferencial a crianças e adolescentes e
- Adolescente de quinze anos busca formação com contrato regular de aprendizagem, que respei
- Uma família não consegue comprar material escolar por desemprego recente, sem outras causa
- Uma equipe busca acolhimento temporário para criança afastada excepcionalmente da convivên
- Uma pessoa recebe judicialmente a guarda de adolescente e deve assegurar-lhe cuidado cotid
- Pessoa de vinte e cinco anos pretende adotar criança de dez anos e atende às demais condiç
- Um comerciante pretende vender bebida alcoólica a adolescente acompanhado da mãe, que cons
- Um município quer organizar rede de proteção envolvendo escola, saúde, Defensoria e consel
- Uma cidade instala o primeiro Conselho Tutelar, composto de cinco pessoas eleitas para man
- Uma psicóloga da rede de assistência conversa com criança vítima de violência para identif
- Uma autoridade policial precisa ouvir adolescente testemunha de violência com finalidade p
- Uma política integrada atende bebês e crianças pequenas até o final do sexto ano de vida, 
- Adolescente de quinze anos participa de avaliação para colocação em família substituta, co
- Criança com direitos ameaçados precisa de apoio psicossocial e inclusão em programa famili
- Conselho Tutelar identifica negligência dos pais e necessidade de atendimento, mas cogita 
- Uma pessoa habilitada à adoção acredita que o cadastro nacional é dispensável porque o jui
- Uma entidade de acolhimento recebe criança em situação emergencial antes de ordem judicial
- Uma entidade não governamental executa programa protetivo, mas alegou que o financiamento 
- Uma rede de escolas não oferece vagas suficientes a crianças de zero a cinco anos, e busca
- Uma criança sem pais ou responsável identificável é localizada em município diferente do l
- Dois irmãos foram escolhidos para atuar no mesmo Conselho Tutelar e afirmam que o impedime
- Adolescente de quinze anos pretende viajar a comarca contígua à de sua residência, na mesm
- Uma associação dedicada à defesa de direitos da infância, constituída há dois anos, preten
- Criança está em acolhimento institucional há dezessete meses, sem decisão judicial atualiz
- O ECA incorpora a doutrina de proteção integral para todas as crianças e adolescentes, sup
- De acordo com o ECA, falta de recursos financeiros dos responsáveis é motivo suficiente, p
- A política de atendimento prevista no ECA deve ser articulada por ações governamentais e n
- O Conselho Tutelar, por ser órgão autônomo, pode decretar a adoção de criança acolhida e r
- Pelo art. 98 do ECA, ameaça de direito causada por omissão estatal, por responsável ou pel
- A colocação em família substituta por guarda, tutela ou adoção é automaticamente equivalen
- Na Lei nº 13.431/2017, a escuta especializada ocorre na rede de proteção e deve limitar pe
- No ECA, a reavaliação de criança acolhida é semestral, e o limite usual de permanência ins
- A Resolução CONANDA 113/2006 estrutura o Sistema de Garantia dos Direitos em promoção, def
- Para candidatar-se ao Conselho Tutelar, a pessoa precisa ser maior de dezoito anos, sendo 
- A política de proteção integral exige que a criança ou adolescente seja ouvido, conforme s
- Em acolhimento emergencial, o art. 93 autoriza que a entidade, ao receber criança sem orde
- O depoimento especial de criança ou adolescente vítima de violência busca produção de prov
- A atuação do Conselho Tutelar abrange diretamente as medidas de proteção dos incisos VIII 
- A escolha dos membros do Conselho Tutelar ocorre em data unificada, a cada quatro anos, no
- Uma criança tem direito a vaga em pré-escola e a rede pública oferece atendimento irregula
- Uma pessoa de quinze anos pode participar de trabalho comum noturno perigoso se os respons
- Para fins do art. 83 do ECA, a viagem nacional desacompanhada de pessoa menor de dezesseis
- No processo de escolha de membros do Conselho Tutelar, o conselho municipal de direitos at
- O Marco Legal da Primeira Infância restringe as políticas aos serviços médicos e afasta a 
```

## Pedido 25: Quantificadores e diagramas (`matematica.quantificadores-diagramas`, 939 tópicos de edital)

```
Matéria: matematica.quantificadores-diagramas (Quantificadores e diagramas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Todo, algum e nenhum": 34
- "2. Conversões, negações e inferências": 33
- "3. Diagramas, casos-limite e como o tema costuma ser cobrado": 33

Já existem (não repita):
- Considere as classes de solicitações e de itens registrados. Qual é o sentido lógico de “t
- Qual frase expressa que a interseção entre a classe de relatórios e a de cópias autenticad
- A proposição “algum formulário está incompleto” afirma necessariamente que:
- Qual é a negação lógica de “todo comprovante foi conferido”?
- Uma equipe afirma: “algum pedido foi analisado”. Qual conclusão é equivalente quanto à rel
- Se todo cadastro validado foi conferido, e todo cadastro conferido foi arquivado, qual con
- A frase “nem toda pasta foi digitalizada” corresponde logicamente a qual alternativa?
- No diagrama de duas classes, um X colocado na região comum a “documentos recebidos” e “doc
- Qual afirmação é a negação de “nenhuma guia foi anexada”?
- Uma proposição “todo A é B” é verdadeira e sabe-se que existe um elemento A. O que se pode
- Considere: todo lote lacrado foi identificado; nenhum lote identificado ficou sem etiqueta
- Se algum A é B e todo B é C, qual conclusão necessariamente acompanha as premissas?
- Qual alternativa descreve corretamente a relação expressa por “nenhum registro temporário 
- Uma conclusão é consequência lógica das premissas quando:
- Se todo A é B e nenhum B é C, qual relação decorre entre A e C?
- A partir somente de “todo arquivo digitalizado foi indexado”, é correto afirmar que existe
- Em um diagrama com três classes, sabe-se que algum A é B e que todo A é C. Onde o elemento
- Qual frase nega exatamente “algum pedido foi recebido e numerado”?
- Premissas: todo A é B; algum C é A; nenhum B é D. Qual conclusão necessariamente decorre?
- Considere “algum A não é B”. Qual afirmação sobre sua conversão é correta?
- Sabe-se que nenhum A é B e que algum C é A. Qual posição do elemento que testemunha a segu
- As premissas dizem: todo A é B e todo A é C. Qual conclusão existencial é logicamente gara
- Se algum A é B, nenhum B é C e todo D é A, qual afirmação é necessariamente verdadeira?
- Qual situação torna falsa a conclusão “todo A é C” sem contrariar as premissas “todo A é B
- Qual é a negação lógica de “todo A é B e nenhum C é D”?
- Em um diagrama, um X está na interseção de A e B, mas sobre a fronteira entre estar ou não
- Todo item revisado foi numerado. Nenhum item numerado ficou sem índice. Algum item revisad
- Se “algum A não é B” é verdadeira, qual alternativa pode ser verdadeira ao mesmo tempo sem
- Se todo A é B e algum C não é B, qual conclusão sobre esse elemento de C é obrigatória?
- A expressão “somente os itens conferidos foram liberados” é corretamente traduzida como:
- Premissas: algum A é B; todo B é C; nenhum C é D. Qual conclusão é necessariamente válida?
- Todo A é B. Algum B não é C. Essa informação basta para concluir que algum A não é C?
- Um diagrama marca como vazia a região A∩B e também mostra um X na região A∩C, sem posição 
- Indique C para certo ou E para errado: “De ‘todo A é B’ segue-se que todo B é A.”
- Indique C para certo ou E para errado: “A afirmação ‘algum A é B’ garante que existe ao me
- Indique C para certo ou E para errado: “A negação de ‘todo A é B’ é ‘nenhum A é B’.”
- Indique C para certo ou E para errado: “A proposição ‘nenhum A é B’ pode ser reescrita com
- Indique C para certo ou E para errado: “De ‘todo A é B’ pode-se concluir que a classe A co
- Indique C para certo ou E para errado: “A frase ‘nem todo A é B’ equivale a dizer que exis
- Indique C para certo ou E para errado: “Se algum A é B e todo B é C, então algum A é C.”
- Indique C para certo ou E para errado: “Uma região sombreada em diagrama convencional de V
- Indique C para certo ou E para errado: “De ‘algum A não é B’ segue-se necessariamente que 
- Indique C para certo ou E para errado: “Se todo A é B e nenhum B é C, então nenhum A é C.”
- Indique C para certo ou E para errado: “Um argumento é inválido quando existe uma configur
- Indique C para certo ou E para errado: “Se todo A é B e todo A é C, então existe elemento 
- Indique C para certo ou E para errado: “Se nenhum A é B, a negação dessa afirmação exige p
- Indique C para certo ou E para errado: “De algum A ser B e de nenhum B ser C segue que ess
- Indique C para certo ou E para errado: “Se algum A não é B, então a classe B necessariamen
- Indique C para certo ou E para errado: “Para provar inválida uma conclusão, basta encontra
```

## Pedido 26: PCASP e MCASP (`contabilidade-publica.pcasp-mcasp`, 930 tópicos de edital)

```
Matéria: contabilidade-publica.pcasp-mcasp (PCASP e MCASP)
Capítulos e quantas questões quero em cada um (total 100):
- "1. MCASP, objeto da contabilidade pública e evolução normativa": 17
- "2. Estrutura do PCASP: oito classes, códigos e atributos": 22
- "3. Regime orçamentário, competência patrimonial e variações": 30
- "4. Escrituração, integridade, restos a pagar e controles": 18
- "5. Consolidação, cinco níveis de análise e resolução de questões": 13

Já existem (não repita):
- Durante reunião, alguém define o MCASP como a lista de códigos contábeis e o PCASP como o 
- Um material de estudo afirma que a edição corrente do MCASP é a décima e que a décima prim
- Uma entidade pública pretende excluir direitos e obrigações de seus relatórios porque o or
- No estudo do MCASP, a equipe procura onde encontrar o tratamento do Plano de Contas Aplica
- Duas entidades registram fatos semelhantes com códigos incompatíveis e a consolidação apre
- Ao observar a classe 3 do PCASP, a equipe deve associá-la a qual categoria de informação?
- Uma unidade precisa classificar registros da fixação de dotação e da posterior execução or
- Uma conta do PCASP é apresentada com sete níveis de desdobramento. Quantos dígitos compõem
- Uma pessoa utiliza conta de classe 8 para registrar uma obrigação patrimonial porque se tr
- Ao implantar detalhamento de uma conta já padronizada até o sexto nível, o ente pretende t
- Uma receita tributária foi arrecadada em janeiro, embora seu crédito já estivesse regularm
- Um serviço foi consumido em dezembro, reconhecido como obrigação patrimonial, e pago em ja
- Uma entidade empenha a compra de um equipamento, que é entregue e incorporado ao ativo por
- No encerramento do exercício, há empenho legalmente emitido, mas a prestação correspondent
- Uma despesa de R$ 9.000 foi empenhada e permaneceu sem pagamento em 31 de dezembro; R$ 6.0
- Uma entidade pretende lançar débito em Ativo (classe 1) e crédito em Receita Realizada (cl
- Na aprovação de dotação, um registro de débito em Dotação Inicial e crédito em Crédito Dis
- Uma entidade assina contrato com obrigações futuras e registra atos potenciais. Qual inter
- Uma entidade informa que o controle de disponibilidade por destinação de recursos cria, po
- A contabilidade de uma entidade equilibrou os débitos e créditos apenas no somatório geral
- Para consolidar contas de entidades do mesmo OFSS, é identificada uma transferência recípr
- Uma conta patrimonial tem o dígito 1 no quinto nível, com identificação de Consolidação. N
- Uma operação envolve uma entidade municipal e outra da União, com contas patrimoniais Inte
- Em um arquivo PCASP 2027, a publicação consta de 2026. Para um lançamento do exercício de 
- Uma entidade incorpora um equipamento, registra controle contratual pendente e prepara a c
- A Parte IV do MCASP aborda a estrutura do PCASP e as suas regras de integridade.
- No PCASP, a classe 4 representa variações patrimoniais aumentativas, pertencentes à nature
- As classes 5 e 6 do PCASP se destinam, respectivamente, aos controles da aprovação e da ex
- Toda despesa empenhada exige, na mesma data, o reconhecimento de VPD integral, independent
- O MCASP vigente em outubro de 2026 é a 11ª edição, aplicável desde o exercício de 2025.
- O código padrão do PCASP possui sete níveis e sete dígitos, um dígito em cada nível.
- A aquisição de bem permanente pode gerar despesa orçamentária e simultaneamente incorporaç
- Se um tributo foi reconhecido patrimonialmente antes da arrecadação, a receita orçamentári
- A classe 2 do PCASP corresponde somente a obrigações exigíveis, sem incluir o patrimônio l
- É válido debitar uma conta de classe 1 e creditar uma de classe 6 no mesmo lançamento semp
- Os restos a pagar processados correspondem, em regra, a empenhos não pagos cuja despesa já
- No PCASP, contas de controle de atos potenciais são acompanhadas nas classes 7 e 8 e não d
- A classificação Intra OFSS, no quinto nível patrimonial, é mantida na consolidação do ente
- O dígito 1 do quinto nível patrimonial identifica Consolidação e, por esse critério, a con
- Na consolidação nacional, saldos recíprocos Inter OFSS entre entes distintos permanecem po
- As contas de DDR criam disponibilidade de caixa nova ao serem transferidas entre estados d
- Para uma conta padronizada até o sexto nível, o ente pode detalhar o sétimo sem alterar li
- Quando o fato gerador e a liquidação coincidem, o MCASP obriga sempre a passagem contábil 
- Publicar o PCASP 2027 durante 2026 significa que o exercício de 2026 deve abandonar imedia
- A Lei Complementar nº 101/2000, art. 50, II, afasta o regime de competência para registrar
```

## Pedido 27: Poder Executivo (`direito-constitucional.poder-executivo`, 925 tópicos de edital)

```
Matéria: direito-constitucional.poder-executivo (Poder Executivo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formas de governo, sistemas e presidencialismo brasileiro": 17
- "2. Eleição, posse, substituição e dupla vacância": 11
- "3. Atribuições presidenciais, decretos, delegação e medidas provisórias": 28
- "4. Responsabilidade do Presidente e dos governadores": 26
- "5. Ministros de Estado e conselhos consultivos": 18

Já existem (não repita):
- Uma Constituição estabelece chefia política temporária, mandatos eletivos e responsabiliza
- Numa análise da Constituição brasileira, identificou-se que a mesma autoridade representa 
- Em país com chefe de Estado separado, o gabinete dirigido pelo primeiro-ministro perde apo
- Um presidente eleito compõe ministérios com representantes de vários partidos para obter a
- Um candidato afirma que o órgão titular do Poder Executivo federal é formado constituciona
- Em eleição presidencial, computaram-se dez milhões de votos válidos e mais um milhão de br
- Uma equipe organiza a cerimônia de posse de presidente e vice eleitos em 2026 e consulta a
- O presidente da República está temporariamente impedido, sem declaração de vacância, e o v
- Presidente e vice estão simultaneamente impedidos de exercer funções, sendo necessário apl
- As vagas de presidente e vice surgem definitivamente no primeiro ano do mandato. Segundo o
- Presidente e vice deixam definitivamente os cargos quando resta um ano do mandato. O art. 
- Presidente e vice planejam viagem oficial de dezoito dias ao exterior e desejam cumprir a 
- O presidente pretende extinguir, por decreto autônomo, funções públicas que se encontram d
- Ao assinar ato de delegação de competências privativas, o presidente escolhe um ministro d
- Uma proposta urgente e relevante é convertida em medida provisória presidencial, e o Congr
- A Câmara dos Deputados admite por dois terços acusação contra presidente por infração pena
- Uma acusação por crime de responsabilidade do Presidente recebe autorização de dois terços
- A Câmara admitiu acusação penal comum contra o presidente por dois terços de seus membros,
- Depois das eleições de 2026, um governador eleito recebe convite para cerimônia de posse m
- O Ministério Público oferece denúncia por crime comum contra governador estadual no exercí
- Um texto estadual confere à própria Assembleia Legislativa tanto admissibilidade quanto ju
- Um ato de consulta trata da estabilidade das instituições democráticas e o presidente prec
- Uma consulta presidencial examina critérios de utilização de áreas indispensáveis à segura
- Uma autoridade pretende escolher ministro de Estado brasileiro naturalizado, com 42 anos e
- Em processo de crime de responsabilidade, o presidente foi suspenso após instauração no Se
- Uma monarquia parlamentar realiza eleições frequentes para o Parlamento, mas mantém monarc
- Um gabinete ministerial perde apoio político da maioria parlamentar no Brasil. Como conseq
- Um pleito presidencial registrou votos brancos e nulos em quantidade elevada. Para calcula
- Em outubro de 2026, a regra constitucional vigente determina que presidente e vice eleitos
- Diante de impedimento temporário do presidente, o vice-presidente disponível assume o exer
- Um presidente deseja delegar ao ministro de Estado a edição de medidas provisórias. O art.
- No presidencialismo brasileiro, um decreto destinado apenas a organizar funcionamento admi
- Uma medida provisória foi editada sob alegação de urgência para modificar regras de proces
- A acusação de crime comum contra presidente foi admitida na Câmara, mas a denúncia ainda n
- O presidente afastado pelo recebimento de denúncia em crime comum é definitivamente excluí
- Uma pessoa brasileira naturalizada, maior de 21 anos e em exercício dos direitos políticos
- O Conselho de Defesa Nacional é composto por seis cidadãos natos de mais de 35 anos, com m
- Um governador eleito em 2026 terá posse em 6 de janeiro de 2027, e, nas eleições ordinária
- O governador de um Estado foi denunciado por crime comum no exercício do cargo. Por simetr
- Uma Constituição estadual atribuiu à própria Assembleia o julgamento final de governador p
- O art. 86, §§ 3º e 4º, impede prisão cautelar de qualquer governador e afasta a persecução
- O presidente delegou ao Advogado-Geral da União a competência de prover cargos públicos fe
- Caso governador e vice deixem definitivamente seus cargos no último biênio estadual, a Con
- O presidente consulta separadamente os dois conselhos sobre a possibilidade de decretar es
- Depois de receber parecer do Conselho de Defesa Nacional favorável à declaração de guerra,
```

## Pedido 28: Atualidades: política, economia, relações internacionais e tecnologia (`atualidades.politica-economia-relacoes-internacionais`, 919 tópicos de edital)

```
Matéria: atualidades.politica-economia-relacoes-internacionais (Atualidades: política, economia, relações internacionais e tecnologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Democracia, política brasileira e desafios sociais": 20
- "2. Economia brasileira e internacional: indicadores e mecanismos": 18
- "3. Geopolítica, conflitos e cooperação internacional": 22
- "4. Clima, sustentabilidade, energia e transportes": 19
- "5. Tecnologia, informação, saúde, educação e transformações sociais": 21

Já existem (não repita):
- Um portal disponibiliza, sem requerimento, relatórios mensais de gastos públicos. Que moda
- Uma notícia afirma que toda disputa das eleições gerais de 2026 terá segundo turno em 25 d
- Ao comparar violência em duas cidades de populações distintas, qual medida favorece uma co
- Uma região exibe rendimento domiciliar per capita médio de R$ 1.484. Qual interpretação re
- Uma análise conclui que novos parques eólicos na Bahia elevaram igualmente o rendimento de
- A taxa de inflação diminuiu de 6% para 4%, com ambos os resultados positivos. Como descrev
- Um boletim registra crescimento de 0,5% do PIB no segundo trimestre de 2026 frente ao trim
- Em um mês, o IPCA em doze meses ultrapassa a faixa superior da meta contínua brasileira. Q
- Uma empresa importa máquinas cotadas em dólares, e a moeda nacional se deprecia. Mantidas 
- Uma nota econômica cita previsão de crescimento mundial de 3,0% em 2026 feita em julho des
- Famílias abandonam suas casas devido a combates, mas permanecem dentro do próprio país. Qu
- Uma publicação chama o G20 de união aduaneira com tarifa externa comum. Qual é a caracteri
- Uma declaração internacional do BRICS apoia cooperação financeira. Qual conclusão é compat
- Um fechamento temporário de rota marítima eleva os gastos logísticos de importadores. Qual
- Os relatórios publicados em 2026 apontam menos deslocados globais ao final de 2025 que ao 
- Uma cidade amplia galerias pluviais para enfrentar chuvas intensas mais frequentes. Qual é
- Em 2025, a fração renovável da matriz elétrica brasileira superou a da matriz energética. 
- Uma reportagem afirma que a COP30 aprovou ampliar financiamento de adaptação e que todo o 
- Um complexo tem 2 GW de potência instalada. Um texto afirma que isso equivale automaticame
- Um informe de 2026 atribui à Bahia cerca de 37% da produção eólica nacional em 12 meses. Q
- Um sistema de IA produz uma resposta convincente com referência bibliográfica inexistente.
- Uma escola registra rede móvel em toda a região, mas estudantes não dispõem de aparelhos o
- Uma empresa deseja tratar dados pessoais disponíveis em perfis públicos na internet. Qual 
- Duas cidades apresentam 800 e 500 casos acumulados de doença, respectivamente, mas populaç
- Um relatório cita uso de IA na educação e promete melhora universal do aprendizado apenas 
- Uma decisão submetida a voto majoritário pode estar sujeita a limites constitucionais e a 
- A existência de portal com publicação espontânea de despesas é exemplo típico de transparê
- A comparação entre taxas de violência de municípios requer atenção ao período, à população
- A média regional de renda domiciliar por pessoa comprova que todos os municípios da região
- A diminuição de uma taxa de inflação ainda positiva constitui desinflação, e não necessari
- IPCA-15 e IPCA mensal possuem necessariamente a mesma janela de coleta e podem ter seus re
- Uma depreciação cambial pode encarecer insumos importados e modificar os custos de indústr
- No regime brasileiro de meta contínua, uma única leitura mensal do IPCA acumulado acima de
- Um grupo obrigado a deixar sua cidade devido à violência pode ser deslocado interno mesmo 
- O G20 constitui uma união aduaneira, com tarifa externa comum diretamente aplicável a todo
- A redução do estoque mundial de deslocados forçados entre dois anos pode coexistir com nov
- Uma declaração política favorável à integração financeira do BRICS prova por si só a criaç
- A substituição de combustível fóssil por eletricidade de menor emissão em um sistema de tr
- A participação de fontes renováveis na matriz energética brasileira e na matriz elétrica b
- A potência eólica instalada, medida em gigawatts, não permite determinar sozinha a energia
- O chamado da COP30 para expandir o financiamento da adaptação equivale à comprovação de qu
- Sistemas de inteligência artificial generativa podem produzir referências bibliográficas i
- A simples retirada do nome em uma base de dados garante que nenhuma pessoa possa ser reide
- Uma área com cobertura móvel pode ainda enfrentar exclusão digital por falta de dispositiv
- Em epidemiologia, prevalência corresponde exclusivamente aos casos novos surgidos durante 
```

## Pedido 29: Microsoft PowerPoint 2016 (`informatica.microsoft-powerpoint-2016`, 914 tópicos de edital)

```
Matéria: informatica.microsoft-powerpoint-2016 (Microsoft PowerPoint 2016)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ambiente, arquivos e construção de slides": 21
- "2. Textos, objetos, tabelas e integração entre aplicativos": 24
- "3. Temas, layouts, mestres e formatação visual": 19
- "4. Transições, animações e recursos multimídia": 18
- "5. Exibição, impressão, atalhos e solução de casos": 18

Já existem (não repita):
- Uma pessoa precisa criar uma apresentação editável no PowerPoint 2016, mantendo slides, ob
- Com a miniatura de um slide selecionada, deseja-se obter outro slide com os mesmos objetos
- Ao preparar um novo slide, a pessoa precisa de uma área para título e outra para texto e g
- Um arquivo foi aberto e o usuário procura os comandos Salvar Como e Imprimir, além de info
- Uma sequência de instruções não exige ordem de execução, mas deve aparecer em tópicos visu
- Uma pessoa deseja representar cargos e linhas de subordinação em um slide sem elaborar man
- Um conjunto de slides deve adotar cores e fontes coerentes, sem alterar individualmente ca
- Para modificar padrões compartilhados por diversos layouts, como aparência de títulos e po
- Ao mudar de um slide para outro, deseja-se mostrar um efeito de dissolução entre as página
- Durante a apresentação, um título já visível deve pulsar para chamar a atenção dos especta
- O apresentador quer começar a projeção do primeiro slide, mesmo que esteja editando o déci
- Uma apresentação extensa precisa ter dezenas de slides reorganizados por arraste de miniat
- Um material de apoio deve conter três miniaturas de slides por folha e espaço para comentá
- Uma planilha externa é atualizada semanalmente e o slide deve refletir os valores novos qu
- Um slide tem dois efeitos de animação: o segundo deve começar depois que o primeiro termin
- Ao abrir a apresentação, um título aparece fora da posição prevista pelo layout. A pessoa 
- Uma apresentação será enviada para outro computador e contém vídeo armazenado em arquivo e
- A pessoa quer utilizar um documento do Word com títulos hierarquizados como base para cria
- Após configurar título e rodapé em um layout mestre, certos slides não exibem o elemento e
- Um slide contém texto com vários tópicos, e cada tópico precisa aparecer em um clique dist
- Durante a projeção, um slide específico deve permanecer no arquivo para eventual revisão, 
- Foi copiado um gráfico do Excel. O destino precisa manter dados editáveis dentro do PPTX, 
- Em um slide, três formas sobrepostas devem ser deslocadas juntas sem perder a possibilidad
- Uma pessoa vai imprimir apenas a hierarquia textual de uma apresentação, deixando imagens 
- Em um conjunto de slides, deseja-se escurecer apenas o plano de fundo da página final, pre
- Uma apresentação aberta no PowerPoint 2016 está em modo Normal. O usuário pressiona Ctrl+M
- Ao selecionar um objeto de desenho no slide, o uso de Ctrl+D cria uma segunda apresentação
- No PowerPoint 2016, uma pessoa pode empregar SmartArt para representar um processo ou uma 
- Na apresentação em andamento, aplicar uma transição de entrada a uma fotografia equivale a
- Em uma apresentação com dezenas de páginas, a exibição Classificação de Slides favorece a 
- Uma tabela com valores mensais foi colada no PowerPoint como imagem. Alterar os números no
- Quando um layout padrão foi modificado manualmente em relação à posição de seus espaços re
- Na guia Design, o comando Aplicar a Todos, relativo à formatação do plano de fundo, é a ma
- O Painel de Animação permite examinar a ordem de efeitos em objetos e alterar opções de te
- Para começar a apresentação no slide atualmente selecionado, deve-se pressionar F5; o atal
- Um usuário salva a apresentação como um arquivo PPSX para que, ao ser aberto, ele entre di
- Na impressão de estrutura de tópicos, o PowerPoint conserva a aparência integral de imagen
- Um vídeo inserido e incorporado em um PPTX tende a aumentar o tamanho do arquivo, mas dimi
- Um gráfico do Excel incorporado e um gráfico vinculado possuem comportamento idêntico: amb
- A mesma apresentação pode possuir mais de um slide mestre, e alterações em determinado lay
- O comando Ocultar Slide retira o slide definitivamente do arquivo, razão pela qual ele não
- Ao selecionar uma fotografia e aplicar um efeito de entrada, o PowerPoint pode controlar o
- Aplicar um tema no PowerPoint 2016 consiste apenas em inserir uma imagem atrás de cada sli
- Um documento do Word estruturado em níveis adequados de títulos pode fornecer tópicos para
- Quando a finalidade é imprimir um slide por folha juntamente com as observações do apresen
```

## Pedido 30: Redes de computadores (`informatica.redes-computadores`, 913 tópicos de edital)

```
Matéria: informatica.redes-computadores (Redes de computadores)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Comunicação de dados, meios físicos e classificação das redes": 26
- "2. Topologias, dispositivos, Ethernet e técnicas de comutação": 27
- "3. Modelos OSI e TCP/IP, transporte, IP e endereçamento": 15
- "4. Serviços DNS, DHCP, ARP/NDP, armazenamento e WAN": 16
- "5. Wi-Fi, segurança, aplicações e solução de problemas": 16

Já existem (não repita):
- Uma instituição interliga equipamentos dentro de um único edifício por cabos Ethernet. Pel
- Um sistema de rádio permite que duas pessoas conversem, mas cada uma precisa terminar sua 
- Uma equipe instalará enlace longo em ambiente com interferência eletromagnética intensa. E
- Dois computadores estão ligados a um switch Ethernet de camada 2. Qual informação ele util
- Uma aplicação web solicita dados de um servidor. No modelo OSI, em qual camada os endereço
- Um computador recebe endereço IP, máscara, gateway e servidor DNS automaticamente ao ingre
- Ao abrir página por nome, o navegador precisa obter endereço IPv4 associado ao domínio. Qu
- Um computador IPv4 precisa encaminhar quadro Ethernet para endereço IP de vizinho na mesma
- Uma estação usa Wi-Fi e a equipe deseja identificar a família de padrões que estabelece as
- Uma ligação Ethernet moderna funciona em full-duplex entre estação e switch. Um candidato 
- Uma rede WAN histórica utiliza células fixas no transporte. O equipamento informa que cada
- Uma equipe quer oferecer armazenamento em blocos sobre a infraestrutura TCP/IP existente, 
- Uma captura de rede contém um quadro Ethernet que transporta pacote IPv4, cujo conteúdo é 
- Um pacote IPv4 atravessa diversos roteadores. O campo TTL é atualizado ao longo do percurs
- Uma estação IPv6 precisa obter endereço de enlace de vizinho e descobrir roteador disponív
- Em teste, um canal transmite nominalmente 20 Mb/s, mas uma aplicação entrega somente 14 Mb
- Em uma WAN, os dispositivos inserem e trocam rótulos associados a classes de encaminhament
- Uma organização revisa documentação de uma WAN legada que menciona circuitos virtuais iden
- Uma WLAN empresarial exige autenticação de acesso com identidade individual, enquanto um g
- Uma estação IPv6 consegue autoconfigurar endereço com anúncios de roteador, mas ainda rece
- Dois grupos usam VLANs diferentes no mesmo conjunto de switches. Precisam trocar pacotes I
- Uma estação acessa um servidor web pelo endereço IP numérico, mas falha ao usar o domínio 
- Um programa envia duas partes de dados por TCP e o destinatário lê um número diferente de 
- Uma operadora transporta vários canais ópticos independentes pela mesma fibra utilizando d
- Uma aplicação IPv6 precisa anunciar informação a vários vizinhos locais sem recorrer a bro
- Um relógio inteligente troca pequenas mensagens com o telefone de seu usuário, a curta dis
- Em uma videoconferência, se a taxa nominal do enlace dobrar, a latência de propagação será
- Em rede cliente-servidor, uma máquina pode fornecer arquivos a outras estações e, simultan
- Uma estação conectada a switch Ethernet de camada 2 deixa de receber quadros broadcast des
- No modelo OSI, a camada de transporte está acima da camada de rede e abaixo das camadas de
- A versão IPv4 usa endereço com 128 bits, e a versão IPv6 utiliza 32 bits para melhorar a e
- Em WLAN com meio compartilhado, mecanismos associados ao CSMA/CA procuram reduzir colisões
- Por operar em frequência mais elevada, uma WLAN de 5 GHz oferece necessariamente maior alc
- Ao comparar técnicas de comutação, uma solução baseada em células ATM de 53 bytes utiliza 
- Como a tecnologia Fibre Channel recebeu seu nome devido ao uso histórico de fibra, suas es
- Na obtenção inicial comum de configuração DHCPv4, um cliente pode emitir Discover, receber
- Uma aplicação que envia datagramas UDP herda do protocolo de transporte as mesmas garantia
- Uma ligação trunk pode transportar quadros associados a diversas VLANs, enquanto uma porta
- Uma operadora anuncia serviço privado de interligação por MPLS e afirma que a troca de rót
- Uma WLAN corporativa pode adotar autenticação de acesso baseada em 802.1X/EAP e mecanismos
- Em IPv6, o ARP da RFC 826 continua sendo empregado para descobrir vizinhos, e o NDP só res
- Na comutação por circuitos virtuais de uma WAN histórica, os dados podem seguir caminho ló
- Como HTTPS usa tradicionalmente a porta 443/TCP, a implementação de HTTP/3 deve transporta
- Um enlace entre dois extremos pode ser ponto a ponto e, ao mesmo tempo, operar em full-dup
- O TTL indicado em uma resposta DNS controla a quantidade de roteadores pelos quais o pacot
```

## Pedido 31: Justiça Eleitoral, alistamento, elegibilidade e inelegibilidade (`direito-eleitoral.justica-eleitoral-alistamento`, 899 tópicos de edital)

```
Matéria: direito-eleitoral.justica-eleitoral-alistamento (Justiça Eleitoral, alistamento, elegibilidade e inelegibilidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Justiça Eleitoral: órgãos, competências e Ministério Público": 26
- "2. Alistamento e domicílio eleitoral": 18
- "3. Cadastro, revisão do eleitorado e justificativa": 25
- "4. Elegibilidade e inelegibilidades": 17
- "5. Registro de candidaturas: prazos, documentos e substituição": 14

Já existem (não repita):
- Um servidor estuda a composição ordinária mínima do Tribunal Superior Eleitoral. Qual dist
- Na organização da apuração, uma junta eleitoral é formada segundo o Código Eleitoral. Qual
- Uma pessoa brasileira de 17 anos pergunta se alistamento e voto são obrigatórios para sua 
- Uma pessoa quer transferir o título para outro município, sem hipótese especial de dispens
- Eleitora com inscrição suspensa perdeu o título e solicita documento substituto, sem modif
- O procurador regional eleitoral atua em uma causa no tribunal regional. Qual enunciado des
- Uma eleitora trabalha e mantém vínculos familiares em município diferente de sua residênci
- Uma pessoa foi marcada como falecida por lançamento incorreto no cadastro, apesar de estar
- Uma associação apresenta candidato escolhido em convenção e pretende registrar a candidatu
- Alguém solicita transferência de domicílio em 10 de outubro de 2026, após o primeiro turno
- Um aspirante ao cargo de vereador terá dezoito anos em setembro, mas ainda terá dezessete 
- Filha de prefeito em exercício quer concorrer a cargo eletivo no mesmo município, sem poss
- Eleitor faltou ao primeiro turno de 2026, realizado em 4 de outubro, e não apresentou just
- O tribunal determina revisão coletiva do eleitorado de determinada zona. Quanto à duração,
- Pessoa escolhida validamente em convenção não teve o registro requerido pela agremiação. Q
- Depois de decisão de TRE sobre inelegibilidade em eleição estadual, a parte avalia recurso
- Servidor público militar removido por necessidade de serviço pede transferência eleitoral 
- Eleitor deixou de votar, não justificou e não pagou multa em três turnos consecutivos, pre
- Uma candidatura é substituída após renúncia, ocorrida 15 dias antes da votação. Qual restr
- Um candidato a deputado federal completa 21 anos depois da posse ordinária, mas dentro dos
- Uma pessoa quer candidatar-se a prefeito. Ela pretende obter o registro diretamente no TSE
- O cônjuge do chefe do Executivo pretende disputar o mesmo cargo quando o titular ainda pod
- O cadastro contém informação de ocorrência que poderá ser usada futuramente para avaliar i
- Na instrução do registro de candidatura, alguém afirma que a certidão de quitação eleitora
- Uma pessoa inscrita no Brasil estava no exterior em 4/10/2026 e volta ao país depois do pr
- O art. 118 da Constituição inclui TSE, TREs, juízes eleitorais e juntas eleitorais entre o
- O corregedor-geral eleitoral do TSE é escolhido necessariamente entre os três ministros do
- Uma pessoa analfabeta pode alistar-se e votar facultativamente, embora seja inelegível seg
- Conscritos, enquanto prestam serviço militar obrigatório, podem alistar-se normalmente por
- O RAE, previsto no art. 41 da Resolução 23.659/2021, formaliza alistamento, transferência,
- Para candidatar-se a governador, pessoa brasileira necessita ter pelo menos 35 anos, porqu
- Uma pessoa pode justificar sua ausência ao primeiro e ao segundo turnos de forma separada,
- O pedido de segunda via do título eleitoral, nos termos do art. 40 da Resolução 23.659/202
- A Constituição, art. 14, § 6º, prevê renúncia do chefe do Executivo até seis meses antes d
- Nas eleições brasileiras, a convenção para escolher candidaturas pode ocorrer até 15 de ag
- A função de promotor eleitoral perante juízes e juntas é exercida por membro do Ministério
- Uma pessoa transferida por desastre ambiental deve observar necessariamente o intervalo de
- A anotação cadastral de fato potencialmente gerador de inelegibilidade, nos termos do art.
- A pessoa escolhida em convenção, mas omitida pela agremiação no pedido de registro, pode p
- Na eleição de 2026, o prazo ordinário para justificar ausência ao segundo turno, se realiz
- Na Resolução 23.659/2021, o eleitor possui dez dias para recorrer do indeferimento do alis
- A Resolução 23.659/2021 permite obter certidões eletrônicas de inscrição, situação e quita
- Por força da Constituição, decisões do Tribunal Superior Eleitoral admitem recurso ordinár
- Na redação conferida pela Lei 15.230/2025 ao art. 11, § 2º, da Lei 9.504/1997, a idade par
- No cadastro eleitoral, a anotação ASE ou FASE constitui quinta operação independente, além
```

## Pedido 32: Terceiro setor, entes de colaboração e convênios (`direito-administrativo.terceiro-setor`, 883 tópicos de edital)

```
Matéria: direito-administrativo.terceiro-setor (Terceiro setor, entes de colaboração e convênios)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Terceiro setor, serviços sociais autônomos e fundações de apoio": 16
- "2. Organizações sociais, OSCIP, contratos de gestão e termos de parceria": 22
- "3. MROSC: colaboração, fomento, cooperação e execução das parcerias": 20
- "4. Convênios, contratos de repasse e Portaria Conjunta nº 33/2023": 27
- "5. Consórcios, controle externo, responsabilidade e quadro comparativo": 15

Já existem (não repita):
- Uma associação privada sem fins lucrativos passa a realizar ações sociais com recursos púb
- Uma entidade privada sem fins lucrativos foi qualificada para executar metas culturais por
- Uma associação sem fins lucrativos, constituída há dois anos e em funcionamento regular, b
- A Administração formula plano de atividades sociais e pretende transferir recursos a uma O
- Uma OSC formula plano comunitário e solicita repasse financeiro para executá-lo em parceri
- Uma instituição pretende parceria sob a Lei nº 13.019/2014 para campanha educativa, sem re
- Uma transferência federal de interesse recíproco será processada por instituição financeir
- Para executar transferência federal abrangida pela Portaria Conjunta nº 33/2023, o propone
- Dois municípios subscrevem protocolo de intenções e pretendem criar pessoa para realizar o
- Uma entidade de serviço social autônomo recebe receitas de origem pública e sustenta que, 
- Uma instituição científica pretende contratar entidade privada constituída especificamente
- Após firmar contrato de gestão, uma OS deve divulgar regras próprias para compras com recu
- Um projeto de publicização busca entregar atividade cultural não exclusiva a entidade priv
- Antes de celebrar termo de parceria com OSCIP, a Administração identifica conselho de polí
- Uma parceria de cooperação do MROSC não envolve dinheiro, mas prevê comodato de imóvel púb
- Uma atividade de interesse público relevante pode parar abruptamente. A Administração pret
- Uma OSC federal parceira compra materiais com recursos do MROSC e registra pesquisa de pre
- Três entes federativos constituem consórcio sob a forma de associação pública, após protoc
- Um consórcio público necessita de recursos financeiros entregues periodicamente pelos ente
- Um ajuste federal encontra-se abaixo ou exatamente no limite do regime simplificado do art
- Um plano de trabalho sujeito à Portaria nº 33/2023, já considerando a alteração de julho d
- Na análise de prestação de contas final de convênio sujeito à Portaria nº 33/2023 atualiza
- Há indicação de possível dano por desvio de recursos em convênio federal, mas ainda não fo
- Uma entidade privada recebe recursos sociais e um usuário sofre dano durante atividade que
- Uma parceria MROSC para inclusão social contém cláusula pela qual a OSC poderá realizar fi
- Uma associação sem fins lucrativos é qualificada como organização social e firma contrato 
- Uma organização social qualificada se torna pessoa jurídica de direito público e passa a i
- Uma OSC apresenta por iniciativa própria plano social financiado com transferência de recu
- Um acordo de cooperação regido pela Lei nº 13.019/2014 deve necessariamente transferir rec
- Uma transferência federal de interesse recíproco é operacionalizada por instituição financ
- O contrato de rateio da Lei nº 11.107/2005 é o instrumento geral pelo qual organizações so
- Uma fundação de apoio é contratada por instituição científica para gerir financeiramente p
- Uma associação com estatuto compatível funciona regularmente há dois anos e requer qualifi
- Em termo de parceria com OSCIP, a consulta prévia a conselho de políticas públicas existen
- O MROSC impõe chamamento público em qualquer acordo de cooperação, inclusive quando inexis
- Uma OS firmou contrato de gestão. O art. 17 da Lei nº 9.637/1998 determina publicar regula
- A Portaria Conjunta nº 33/2023, em sua redação atual, rege diretamente os termos de colabo
- Em um consórcio constituído como associação pública, o art. 6º, § 1º, da Lei nº 11.107/200
- Uma Administração pretende celebrar termo de fomento sem chamamento e afirma que basta pre
- Uma OSC utiliza recursos de termo de fomento para pagar equipe própria diretamente dedicad
- Como o contrato de gestão de OS envolve pessoa privada, a seleção dessa entidade e as comp
- Na análise de contas finais de convênio federal regido pela Portaria nº 33/2023, o prazo p
- Constatada uma informação documental incompleta em convênio, o concedente é obrigado a ins
- Uma entidade privada que efetivamente presta serviço público pode responder objetivamente 
- Um plano de trabalho de convênio federal informa somente que serão realizadas 'ações diver
```

## Pedido 33: IA, aprendizado de máquina, IA generativa e ética (`informatica.ia-generativa-etica`, 874 tópicos de edital)

```
Matéria: informatica.ia-generativa-etica (IA, aprendizado de máquina, IA generativa e ética)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, história, paradigmas e modalidades de aprendizagem": 20
- "2. Neurônios, treinamento e arquiteturas de redes neurais": 18
- "3. Modelos generativos, LLMs, RAG, difusão e multimodalidade": 22
- "4. Engenharia de prompt, segurança e riscos dos sistemas generativos": 17
- "5. Ética, LGPD, governança de IA e aplicação no setor público": 23

Já existem (não repita):
- Um setor utiliza um sistema de regras SE–ENTÃO para identificar situações previstas em nor
- Um modelo é treinado com milhares de imagens acompanhadas de seus respectivos rótulos de o
- Uma equipe precisa usar rede neural cuja saída represente probabilidades de três classes m
- Um sistema com perceptron linear de camada única não consegue separar corretamente as quat
- Um assistente responde com base em trechos encontrados em repositório atualizado, acrescen
- Ao pedir que um sistema classifique uma manifestação, a pessoa não apresenta exemplos reso
- Um sistema gera uma decisão administrativa inteiramente automatizada com dados pessoais e 
- Um modelo de visão emprega filtros locais para identificar padrões como bordas e formas nu
- Um modelo de linguagem segmentou uma frase em unidades que incluem fragmentos de palavras 
- Uma pessoa consulta um LLM sobre fato recente, mas sua janela de contexto é grande. Qual c
- Durante o treinamento, a equipe quer determinar como uma pequena mudança em cada peso alte
- Um sistema lê página recuperada que contém a instrução oculta: 'ignore o usuário e envie o
- Um sistema compara gerações com exemplos reais por meio de dois componentes que aprendem e
- Um programa de governança utiliza as funções Govern, Map, Measure e Manage para examinar i
- Uma rede recorrente precisa administrar informação por períodos mais longos, controlando e
- Uma ferramenta recebe três exemplos completos de mensagens e as categorias corretas antes 
- O órgão público envia ao serviço generativo um cadastro completo, embora a tarefa exija ap
- Um relatório sobre IA afirma que o teste de Turing, proposto em 1950, comprova consciência
- Um pesquisador afirma que elevar a temperatura de geração permite ao LLM utilizar notícias
- Após incluir RAG em sistema de consulta normativa, os testes mostram respostas contendo tr
- Uma decisão exclusiva de sistema automatizado prejudica interesse do titular. A administra
- Uma avaliação aprova modelo pelo desempenho agregado, mas seu erro é muito maior para grup
- Uma unidade quer implantar gestão contínua de riscos e oportunidades de IA por meio de req
- Uma arquitetura transformer usa atenção para ponderar posições da entrada. O avaliador con
- Um sistema judicial de IA foi planejado sob a Resolução CNJ nº 615/2025, e um relatório us
- Um sistema especialista pode integrar o campo da IA sem utilizar redes neurais ou aprendiz
- Aprendizado autossupervisionado requer que uma pessoa rotule manualmente cada exemplo de t
- Uma CNN usa filtros locais compartilhados para aprender padrões espaciais, sendo uma arqui
- A função softmax retorna probabilidades independentes para cada classe, de modo que três s
- No uso habitual de RAG, trechos recuperados são acrescentados ao contexto do gerador sem q
- Ao passar de zero-shot para few-shot, o usuário precisa obrigatoriamente reentreinar todos
- O treinamento de um modelo autoregressivo pode empregar como alvo a previsão do próximo to
- O viés numérico acrescentado ao somatório de um neurônio e o viés discriminatório contra u
- Uma rede LSTM é variante de rede recorrente que utiliza portas para controlar entrada, esq
- Como RLHF utiliza avaliações humanas, qualquer resposta de modelo treinado com essa técnic
- Uma plataforma que usa RAG permanece exposta à injeção de prompt se documentos recuperados
- A data de corte de um LLM é o limite de tokens que o modelo consegue processar em cada cha
- A Recomendação da UNESCO sobre a Ética da IA, de 2021, contempla direitos humanos, transpa
- Se uma instituição possui certificação de sistema de gestão conforme ISO/IEC 42001:2023, f
- Na redação atual do art. 20 da LGPD, o titular pode solicitar revisão de decisão tomada un
- No transformer, os pesos de atenção fornecem uma explicação causal completa e verificável 
- No aprendizado por reforço, um agente pode aprender política de ações em função de recompe
- Envenenamento de dados refere-se unicamente ao aumento de temperatura de um LLM durante a 
- A Resolução CNJ nº 615/2025 recebeu alteração em seu art. 15 pela Resolução nº 674/2026, s
- O ISO/IEC 42001:2023 e o NIST AI RMF 1.0 constituem a mesma norma legal brasileira: ambos 
```

## Pedido 34: Discurso, modalização e atos de fala (`portugues.discurso-modalizadores-atos-fala`, 870 tópicos de edital)

```
Matéria: portugues.discurso-modalizadores-atos-fala (Discurso, modalização e atos de fala)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Discurso direto e indireto: identificação e transposição": 15
- "2. Discurso indireto livre, enunciação e polifonia": 15
- "3. Modalizadores, modalidade e efeitos argumentativos": 27
- "4. Atos de fala, intenção comunicativa e interação": 15
- "5. Cooperação, lógica da linguagem e signos verbais e não verbais": 28

Já existem (não repita):
- Em uma notícia, lê-se: O técnico afirmou: “Eu entregarei o resultado amanhã”. Qual análise
- Na segunda-feira, Ana declarou: “Voltarei aqui amanhã”. Uma pessoa relata a declaração na 
- O repórter ouviu da organizadora: “Os convidados já chegaram?” Qual relato indireto mantém
- João escreve no próprio diário, no mesmo dia em que falou: “Eu disse à equipe que entregar
- Em um relato, consta: “A testemunha confessou: ‘Talvez eu tenha me confundido’”. Ao reescr
- Leia: Clara observou o corredor vazio. Onde estavam todos? Aquela reunião seria um desastr
- Um editorial afirma: “Segundo os entrevistados, a mudança foi insuficiente”. Sobre a respo
- Num comunicado, lê-se: “A coordenadora voltou a orientar os candidatos”. Qual informação é
- Durante uma reunião com a janela aberta e muito barulho, alguém diz ao colega perto dela: 
- Após uma proposta confusa, a participante comenta: “Que clareza admirável!”, em tom de iro
- Depois de observar luz acesa e carro estacionado, Paulo afirma: “Ela deve estar em casa”. 
- Uma orientação afixada à entrada informa: “Visitantes devem apresentar identificação”. Qua
- Um regulamento afirma: “Não é obrigatório imprimir a confirmação”. Qual reescrita mantém o
- Um comunicado registra: “Infelizmente, talvez o evento seja adiado”. Como se combinam os m
- Um pesquisador escreveu: “Os registros parecem indicar melhora”. O editor propõe: “Os regi
- No silêncio da biblioteca, uma estudante diz ao colega que fala alto: “Você consegue falar
- Uma pessoa diz “Cuidado com o degrau!”, e a outra recua a tempo. O que corresponde ao ato 
- Uma pessoa sem qualquer função na assembleia brinca: “Declaro encerrada a reunião”, mas a 
- Durante a organização de um trabalho, Bruno afirma: “Amanhã haverá uma entrega”, enquanto 
- Ao revisar um texto, uma leitora comenta ao autor: “Talvez seja melhor explicitar a fonte”
- Ao perguntar “Qual é a sala da palestra?”, alguém recebe “É em algum lugar do prédio princ
- Um sistema informa: “Se o arquivo foi assinado, ele recebeu protocolo”. Sabe-se apenas que
- Questionado sobre presença na oficina, o responsável responde: “Alguns participantes viera
- Na análise de um cartaz, há um desenho de pegadas e, no chão real próximo, pegadas molhada
- Uma placa exibe “Piso molhado” ao lado do desenho de alguém escorregando. Qual interpretaç
- Em “Lúcia avisou: ‘Partirei amanhã’”, o futuro e o advérbio temporal pertencem à perspecti
- Uma entrevista ocorreu e foi noticiada no mesmo dia. Mesmo assim, converter “Trabalho aqui
- Ao relatar “O visitante perguntou: ‘O portão foi aberto?’”, a frase “O visitante perguntou
- Quando uma ordem direta “Fechem os computadores!” é reescrita como “A instrutora ordenou q
- Em “Rafael reviu o texto. Como pudera esquecer aquele dado? Que descuido!”, as perguntas e
- No período “A médica explicou que o atendimento prosseguiria”, o emprego da terceira pesso
- O aviso “O equipamento voltou a apresentar ruído” pressupõe, numa leitura habitual, a ocor
- Se alguém diz “A luz está acesa” a uma colega perto do interruptor, fica linguisticamente 
- Diante da ausência de resposta ao telefone, a expressão “Ele provavelmente está ocupado” a
- Em “Não é necessário aguardar autorização”, a negativa impõe ao destinatário a obrigação d
- No comunicado “Lamentavelmente, talvez a atividade atrase”, os advérbios acumulam funções:
- Ao substituir “A proposta parece viável” por “A proposta é certamente viável”, o redator p
- Quando uma visitante pergunta “Você pode abrir a porta, por gentileza?” a alguém com a cha
- Se um alerta “Atenção ao degrau!” assusta o ouvinte e ele para de andar, o ato ilocucionár
- A frase “Eu declaro inaugurada a sessão”, emitida em cerimônia regular por pessoa autoriza
- Uma pessoa diz “Desculpe pelo atraso”, mas o interlocutor não aceita a justificativa. A re
- Perguntado sobre o número de uma sala, um atendente responde apenas “É no terceiro andar”,
- De “Se a senha funciona, a página abre” e “A página abriu” conclui-se validamente que a se
- Num aviso ilustrado, a palavra “saída” pode funcionar como signo simbólico por convenção l
- Se uma câmera registra o desenho de uma pegada numa parede, esse desenho constitui necessa
```

## Pedido 35: Autarquias, Agências reguladoras e executivas (`direito-administrativo.autarquias-agencias-reguladoras-executivas`, 865 tópicos de edital)

```
Matéria: direito-administrativo.autarquias-agencias-reguladoras-executivas (Autarquias, Agências reguladoras e executivas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Administração indireta: entidades, criação, personalidade e controle": 40
- "2. Reforma do Estado, regulação econômica, concorrência e limites normativos": 17
- "3. Agências reguladoras e executivas: autonomia, governança, dirigentes e pessoal": 18
- "4. Regulação setorial: Anatel, ANP, Anvisa e ANS": 12
- "5. Atos administrativos: elementos, atributos, espécies e invalidação": 13

Já existem (não repita):
- Uma lei específica federal pretende instituir pessoa jurídica de direito público para exec
- Uma empresa estatal possui patrimônio próprio e capital social integralmente detido por en
- Uma entidade estatal pretende admitir acionistas privados, mantendo maioria do capital vot
- Uma entidade federal foi criada por lei para atuar no setor técnico, tem personalidade púb
- Uma autarquia pretende obter qualificação federal de agência executiva e dispõe de plano e
- Após privatização de empresa prestadora de serviço setorial, o Estado passa a definir padr
- Uma autoridade administrativa constata que ato anterior era legal, mas deixou de ser conve
- Uma indústria solicita registro de produto sujeito à vigilância sanitária e precisa saber 
- Duas prestadoras de telecomunicações anunciam concentração empresarial sujeita a controle 
- Uma estatal discute atividades de exploração de petróleo em área sujeita a regime contratu
- Uma operadora de plano privado foi cobrada pelo atendimento de beneficiário no SUS, confor
- Uma agência prepara norma geral com potencial impacto financeiro sobre consumidores e empr
- Após indicação presidencial e aprovação do Senado, um novo dirigente federal de agência re
- Uma empresa estatal planeja reformar seu Conselho de Administração e a assessoria cita um 
- Uma agência edita restrição de entrada no setor sem demonstrar problema público relevante 
- Um agente emitiu ato favorável de competência originalmente atribuída a superior, mas dele
- Uma autoridade declara interditar estabelecimento por risco técnico específico. O laudo de
- Um dirigente indicado para completar vaga aberta no curso de mandato recebeu período reman
- Uma agência deseja aprovar minuta normativa geral após consulta pública de vinte dias, sem
- Uma fundação instituída como pessoa de direito público desempenha atividade social sob reg
- Após ato normativo da agência, uma empresa afirma que a independência regulatória impede f
- Uma sociedade estatal está revendo governança sob a Lei 13.303/2016. Seu conselho possui o
- A atualização da Lei 9.478/1997 ampliou explicitamente a atividade regulatória da ANP. Uma
- Uma autarquia concedeu benefício administrativo regular na aparência, com efeitos patrimon
- Um antigo dirigente de agência encerrará mandato e foi convidado a prestar consultoria rem
- Uma autarquia federal criada diretamente por lei específica possui personalidade jurídica 
- Uma empresa pública poderá ter parte de seu capital social detido por investidores privado
- A sociedade de economia mista é pessoa jurídica de direito privado que deve assumir a form
- Uma associação privada qualificada como organização social, ao firmar contrato de gestão c
- A Anatel possui natureza de autarquia sob regime especial e Conselho Diretor, com competên
- A qualificação federal de agência executiva exige constituir uma nova pessoa jurídica priv
- Uma decisão administrativa comprovadamente ilegal pode ser objeto de anulação no exercício
- Uma operadora de plano privado será fiscalizada pela Anvisa quanto ao cumprimento do regim
- A Anvisa coordena o Sistema Nacional de Vigilância Sanitária, que também compreende instit
- Uma agência pode criar reserva artificial de mercado sem motivação de interesse público, p
- Os diretores de agências reguladoras federais, no regime geral da Lei 9.986/2000, possuem 
- A análise concorrencial de atos de concentração no setor de telecomunicações é definitivam
- Um ato administrativo com vício sanável de competência não exclusiva pode ser convalidado 
- A Lei 13.848/2019 continua a excluir a Agência Nacional de Proteção de Dados de seu rol de
- O Decreto 2.487/1998 qualifica automaticamente uma fundação federal como agência executiva
- Em ato favorável que produz pagamentos mensais, a Lei 9.784/1999 conta os cinco anos de de
- A competência normativa técnica de uma agência reguladora permite-lhe instituir qualquer r
- A teoria da captura regulatória ajuda a explicar por que um órgão técnico pode sofrer infl
- Uma autoridade pode convalidar ato cujo objeto é proibido por lei ou cuja finalidade decla
- O regime de dirigentes das agências federais prevê impedimento pós-mandato de seis meses p
```

## Pedido 36: História do Brasil República (`historia.brasil-republica`, 853 tópicos de edital)

```
Matéria: historia.brasil-republica (História do Brasil República)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundação republicana, oligarquias, sociedade e crise de 1889 a 1930": 23
- "2. Era Vargas: rupturas, trabalho, cultura e Segunda Guerra (1930–1945)": 20
- "3. Democracia de 1946–1964, Guerra Fria, desenvolvimento e cultura": 19
- "4. Regime de 1964–1985: autoritarismo, economia, sociedade e abertura": 20
- "5. Nova República, cidadania, partidos, eleições e constituições comparadas": 18

Já existem (não repita):
- Um texto sobre a organização política brasileira informa que a primeira Constituição repub
- Uma pesquisa identifica dois governantes militares nos primeiros cinco anos da República, 
- Um relato da Primeira República descreve chefias locais que ofereciam favores, dependência
- Um estudo sobre o café no início do século XX examina acordo para sustentar preços por mec
- Uma exposição sobre conflitos sociais apresenta marinheiros que se insurgiram contra casti
- Um painel de 1922 mostra leituras literárias, obras visuais e concertos que romperam com e
- Uma cronologia destaca a Consolidação das Leis do Trabalho enquanto o país vivia a ditadur
- Um conjunto de obras e fábricas é associado ao plano econômico '50 anos em 5', à expansão 
- Um movimento de multidões nas ruas em 1984 pleiteou a volta da escolha direta do president
- Uma exposição informa que o Brasil rompeu a neutralidade após o torpedeamento de navios me
- Um estudo de transformações sociais entre 1890 e 1930 descreve fazendeiros exportadores, i
- Um arquivo reúne relatos da Revolta de 1924 e da Coluna Miguel Costa–Prestes, identificand
- Uma cronologia da cidadania aponta Código Eleitoral com voto feminino e secreto antes de u
- Em documentos dos anos 1930, dois agrupamentos se opõem: um defende nacionalismo autoritár
- Um cartaz de 1941 promove feitos do Estado Novo e programas radiofônicos sob fiscalização 
- Ao tratar das transformações de 1945 a 1964, um estudo relaciona migração do campo para a 
- A análise de um período da Guerra Fria registra alinhamento brasileiro aos Estados Unidos,
- Um pesquisador analisa participantes da Semana de 1922 e distingue-os de artistas que ader
- Uma cronologia traz neutralidade brasileira em 1939, torpedeamentos e declaração de guerra
- Um texto sobre 1961 menciona renúncia presidencial, resistência à posse do vice, Campanha 
- Dois registros do regime de 1964 tratam da reorganização partidária de 1965 e de medida re
- Uma análise dos governos militares afirma que toda a diplomacia do período repetiu, sem mu
- Um programa cultural analisa obras que misturam referências populares e estrangeiras no fi
- Uma linha do tempo partidária informa o fim do bipartidarismo e a formação ou reorganizaçã
- Uma cronologia econômica associa o lançamento do Plano Real, a eleição presidencial de 199
- Em 1891, o Brasil adotou uma Constituição republicana que instituiu a federação e separou 
- A política do café com leite obrigava a sucessão presidencial alternada e ininterrupta ent
- A Revolta da Vacina de 1904 ocorreu em contexto de vacinação obrigatória e reformas urbana
- O DIP foi criado em 1939 por governo republicano pluripartidário e tinha como atividade ce
- A Constituição de 1946 foi promulgada no processo de redemocratização após o Estado Novo e
- A primeira eleição presidencial direta da Nova República ocorreu em janeiro de 1985, quand
- Em 1917, ataques submarinos a navios mercantes brasileiros contribuíram para o reconhecime
- A Semana de Arte Moderna de 1922 foi a primeira manifestação de crítica estética ocorrida 
- O Código Eleitoral de 1932 antecedeu a Constituição de 1934 e contribuiu para a ampliação 
- O rádio, o samba e a educação foram experiências culturais desligadas do Estado Novo, pois
- Entre 1945 e 1964, Bossa Nova, Cinema Novo, televisão em expansão e Ligas Camponesas compu
- Durante o governo Dutra, o ambiente da Guerra Fria produziu alinhamento ao bloco soviético
- O milagre econômico, concentrado aproximadamente entre 1968 e 1973, combinou rápido cresci
- O Ato Institucional nº 5 foi editado em dezembro de 1968 e, por permitir fechamento do Leg
- A Tropicália do final dos anos 1960 pertence à mesma fase política da Semana de Arte Moder
- O chamado pragmatismo responsável, associado ao governo Geisel, é exemplo de que a polític
- A Lei da Anistia de 1979 restaurou de imediato eleições presidenciais diretas para 1980, e
- A sequência de ampliação do eleitorado distingue o reconhecimento do voto feminino no Códi
- As Constituições de 1937, 1946 e 1988 resultaram, cada uma, de assembleias constituintes d
- A eleição presidencial de 1989 foi indireta, e o Plano Real nasceu somente após Fernando H
```

## Pedido 37: Engenharia de software e métodos ágeis (`informatica.engenharia-software-ageis`, 852 tópicos de edital)

```
Matéria: informatica.engenharia-software-ageis (Engenharia de software e métodos ágeis)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, processos de desenvolvimento, análise e projeto": 17
- "2. Engenharia de requisitos, backlog, MVP, Lean e estimativas": 18
- "3. UML, qualidade de software e modelagem complementar": 17
- "4. Manifesto Ágil, Scrum 2020, Kanban 2025, XP e Lean": 28
- "5. Testes, automação, TDD, BDD, DevOps e evolução contínua": 20

Já existem (não repita):
- Uma equipe recebeu requisitos bem conhecidos e planeja executar análise, projeto, implemen
- Uma equipe entrega primeiro cadastro funcional, depois pagamentos e finalmente relatórios,
- Um documento determina: '95% das consultas devem responder em até dois segundos sob 500 se
- Uma pessoa especifica a história 'Como usuário, quero recuperar minha senha para retomar m
- A equipe precisa representar classes, atributos, operações e multiplicidades entre entidad
- Em um diagrama de caso de uso, 'Concluir compra' incorpora necessariamente 'Validar pagame
- Uma equipe consulta o Scrum Guide de novembro de 2020 para organizar responsabilidades. Qu
- Durante Sprint de duas semanas, Developers realizam reunião diária para inspecionar avanço
- Uma função de cálculo é verificada isoladamente com entradas e saídas controladas, sem int
- Equipe corrige um defeito em serviço de pagamentos e executa novamente compras, cancelamen
- Durante elaboração do RUP, a equipe enfrenta risco de arquitetura relacionado à integração
- Antes de escolher ferramenta para uma nova funcionalidade, a equipe observa o processo de 
- Uma equipe marcou histórias com 3, 5 e 8 story points e pretende converter cada ponto em o
- Em diagrama de sequência para confirmar pedido, uma mensagem adicional de auditoria é envi
- Uma equipe deseja alinhar práticas de qualidade com a edição atual da ISO/IEC 25010 e enco
- Em Sprint, uma história atende à Definition of Done na quarta-feira. A equipe deseja liber
- Em workflow Kanban, seis itens já foram iniciados, quatro ainda não terminaram e dois ence
- Uma equipe primeiro escreve teste para cálculo de frete e observa falha, depois implementa
- Um caso de uso 'Finalizar pedido' admite opcionalmente 'Aplicar desconto de fidelidade' qu
- Uma equipe realiza oficina para transformar regra 'transferências acima do limite exigem c
- Dois departamentos utilizam a palavra 'cliente' com regras de identificação distintas. Ao 
- Em Sprint ativo, o Sprint Goal tornou-se obsoleto após mudança de mercado. A equipe discut
- Uma equipe Kanban define a expectativa: '85% dos itens iniciados estarão finalizados em oi
- A equipe de um MVP obteve cobertura de 100% das linhas de código, mas falhou ao testar o c
- Um pipeline integra mudanças, executa testes automatizados e cria pacote pronto para produ
- A engenharia de software trata de requisitos, projeto, construção, testes, entrega e manut
- Um requisito escrito como 'o sistema deve ser rápido' já é plenamente verificável, dispens
- Diagrama de sequência da UML é apropriado para representar a ordem de mensagens trocadas e
- Segundo o Scrum Guide 2020, o Product Backlog é documento imutável que fica congelado no c
- O desenvolvimento orientado a testes pode seguir a sequência: teste que falha, implementaç
- No RUP, testes são uma fase exclusiva que ocorre obrigatoriamente depois da transição, sem
- A rastreabilidade de requisitos pode conectar origem, especificação, projeto, implementaçã
- No método MoSCoW, a categoria Won't indica que o requisito foi tecnicamente proibido e não
- A edição ISO/IEC 25010:2023 define nove características para o modelo de qualidade de prod
- A Sprint Review é um portão obrigatório de aprovação de qualquer Increment, de modo que pr
- No Kanban Guide 2025, controlar explicitamente trabalho em andamento ajuda a formar sistem
- Uma suíte que alcança 100% de cobertura de linhas demonstra, por definição, que todos os r
- O Product Owner do Scrum pode delegar a terceiros a elaboração ou detalhamento de itens do
- No Scrum 2020, o Product Owner define sozinho como cada item selecionado será implementado
- Na UML, o relacionamento <<extend>> parte do caso de uso que adiciona comportamento e apon
- Uma equipe concluiu 32 story points no último Sprint e outra concluiu 20; logo, mesmo sem 
- No Lean Software Development, preservar opções para decidir em momento responsável pode re
- Uma ferramenta que examina o código-fonte e aponta possíveis vulnerabilidades sem executá-
- Num serviço integrado, um teste de contrato pode detectar divergência entre estrutura de r
- Se uma aplicação é continuamente construída, testada e mantida pronta para implantação, a 
```

## Pedido 38: Política urbana, Estatuto da Cidade, parcelamento do solo e Reurb (`direito-urbanistico.estatuto-cidade`, 847 tópicos de edital)

```
Matéria: direito-urbanistico.estatuto-cidade (Política urbana, Estatuto da Cidade, parcelamento do solo e Reurb)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constituição, competências, função social e planejamento municipal": 16
- "2. Estatuto da Cidade e instrumentos para concretizar a função social": 18
- "3. Parcelamento urbano, projetos, restrições, registro e tutela coletiva": 31
- "4. Reurb: modalidades, instrumentos, processamento e CRF": 19
- "5. Gestão democrática, impactos ambientais e proteção dos espaços urbanos": 16

Já existem (não repita):
- Um município pretende definir regras de uso, ocupação e parcelamento do solo urbano, sem i
- Uma lei municipal estabelece diretrizes urbanísticas e a prefeitura quer explicar quando d
- Uma cidade possui 17 mil habitantes, mas integra uma região metropolitana. Seu administrad
- Uma pessoa ocupa, sem oposição, há cinco anos, terreno particular urbano de 180 m² para su
- O município notificou proprietário para adequado aproveitamento do solo urbano, na forma d
- O proprietário de terreno urbano pretende vendê-lo a terceiro, mas a área está delimitada 
- Um terreno de 500 m² admite área edificável de referência de 1.000 m² segundo o parâmetro 
- Um empreendedor subdivide gleba urbana e cria ruas públicas para acessar os novos terrenos
- Uma gleba é dividida em quatro lotes para edificação sem criar ou modificar vias, aproveit
- Em projeto ordinário de loteamento urbano, sem exceção legal aplicável, qual combinação de
- Notificado para aproveitamento compulsório, o proprietário recebeu prazo de quatro meses p
- Um proprietário já foi notificado para aproveitar imóvel ocioso e vende a área antes do fi
- Após cinco anos de IPTU progressivo sancionatório, o proprietário não cumpriu o aproveitam
- O proprietário pretende alienar imóvel submetido à preempção; comunicou proposta assinada 
- Um proprietário urbano pretende conceder a outra pessoa uso construtivo do terreno por per
- Um loteador recebeu aprovação do projeto urbano, mas pretende iniciar vendas antes de leva
- Um empreendedor propõe parcelar gleba situada em terreno com declividade de 35%, alegando 
- Uma área ocupada informalmente tem população predominantemente de baixa renda, assim recon
- Uma pessoa ocupa imóvel público integrante de núcleo informal e solicita especificamente t
- Uma municipalidade aprovou projeto de regularização e precisa expedir documento que acompa
- Em núcleo urbano informal sem oposição há mais de cinco anos, a área total dividida pelo n
- Um projeto comercial de grande porte necessita EIV por lei municipal e também EIA pela leg
- Um legitimado requer Reurb-E e a prefeitura permanece inerte por mais de 180 dias quanto à
- Uma ocupação informal consolidada alcança parte de área de preservação permanente e outra 
- Um condomínio costeiro aprovado pelo município pretende erguer barreira que impeça acesso 
- Uma cidade com mais de vinte mil habitantes deve possuir plano diretor aprovado pela Câmar
- A propriedade urbana cumpre sua função social sempre que o titular paga regularmente os tr
- O Estatuto da Cidade, Lei nº 10.257/2001, estabelece normas de ordem pública e interesse s
- Dividir gleba para edificação utilizando ruas existentes, sem abertura ou modificação de v
- A Reurb-S destina-se a núcleos urbanos informais predominantemente ocupados por população 
- Uma lei que institui o plano diretor pode limitar-se ao perímetro urbano, pois o art. 40, 
- A usucapião especial urbana individual exige posse como sua por cinco anos sem oposição de
- O Estatuto da Cidade permite que o Município passe diretamente à desapropriação sancionató
- A aprovação municipal de projeto de parcelamento urbano deve ser seguida de apresentação a
- A legitimação de posse prevista no art. 25 da Lei nº 13.465/2017 é instrumento de aquisiçã
- A Lei nº 10.257/2001 determina que a elaboração do plano diretor ocorra com audiências e d
- Segundo o art. 30, § 3º, da Lei nº 13.465/2017, a ausência de resposta do Município por 18
- O art. 38 do Estatuto da Cidade estabelece que a elaboração de estudo prévio de impacto de
- Uma operação urbana consorciada pode ser constituída por acordo exclusivamente privado ent
- O Decreto-Lei nº 58/1937 tratou historicamente de loteamento e venda de terrenos urbanos e
- Denominar comercialmente uma gleba rural como conjunto de chácaras de recreio afasta a inc
- A Lei nº 7.661/1988 considera praias bens públicos de uso comum do povo e protege o acesso
- O art. 3º, parágrafo único, III, da Lei nº 6.766/1979 proíbe de maneira absoluta parcelame
- A usucapião especial urbana coletiva, na redação atual do art. 10 do Estatuto, contempla n
- Em Reurb de núcleo informal situado em área de preservação permanente, a mera emissão de C
```

## Pedido 39: Geografia e história de Rondônia (`rondonia.geografia-historia`, 847 tópicos de edital)

```
Matéria: rondonia.geografia-historia (Geografia e história de Rondônia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Território, limites, relevo, clima e população": 19
- "2. Hidrografia, navegação, ambiente e infraestrutura": 19
- "3. Economia, ocupação recente e zoneamento socioeconômico-ecológico": 19
- "4. Conquista colonial, fronteiras e resistências nos vales amazônicos": 24
- "5. Borracha, questão acreana, ferrovia e criação do estado": 19

Já existem (não repita):
- Uma pessoa deseja identificar somente os limites estaduais brasileiros de Rondônia. Qual c
- Uma tabela rotula o total de 1.581.196 moradores de Rondônia. Qual indicação metodológica 
- Em uma atividade escolar, foi pedido um padrão climático geral para Rondônia e seu efeito 
- No escoamento fluvial de mercadorias, um estudo acompanha o rio que passa por Porto Velho 
- Uma cooperativa pretende listar cadeias rurais importantes em Rondônia. Qual combinação é 
- Uma visita histórica associa o Forte Príncipe da Beira a um rio fronteiriço no sudoeste de
- Ao montar a cronologia administrativa, um aluno precisa registrar o nome inicial do territ
- Um roteiro de patrimônio ferroviário visita os dois extremos da Estrada de Ferro Madeira-M
- Um mapa educacional simplifica o ZSEE instituído pela LC 233/2000. Quantas zonas gerais de
- Um texto relaciona a incorporação do Acre ao Brasil ao compromisso de construir uma rota d
- Uma pesquisadora calcula a densidade usando 1.581.196 habitantes e aproximadamente 237.753
- Um plano prevê extrativismo florestal manejado em área do ZSEE situada na Zona 2. Qual lei
- Uma análise histórica pretende definir o alcance da abertura internacional da navegação no
- Um guia associa fortificações e defesa luso-brasileira no vale do Guaporé. Qual cronologia
- Uma narrativa explica por que seringueiros recebiam mercadorias antecipadas e deviam entre
- Ao comparar duas descrições da formação do Madeira, qual sequência evita confundir o papel
- Uma série cartográfica indica que o Território Federal do Guaporé surgiu com parcelas de d
- Uma propriedade rural está no setor 1.3 da segunda aproximação do ZSEE. Qual diagnóstico t
- Um comerciante sustenta que a legislação imperial de 1866 permitiu acesso estrangeiro auto
- Uma exposição apresenta o crescimento dos seringais do Beni como ocupação econômica de fro
- Dois registros mostram aumento da extração de borracha em períodos internacionais diferent
- Uma análise de expansão populacional ao longo da BR-364 na segunda metade do século XX dev
- Ao avaliar mapas econômicos de Rondônia de 1960 a 1980, um pesquisador identifica áreas co
- Um estudo histórico compara o surgimento de Porto Velho ao de núcleos de colonização agríc
- Em um quadro jurídico, deseja-se resumir exatamente os artigos 1º e 2º da LC nº 41/1981. Q
- Em um mapa de Rondônia, a Bolívia aparece como país fronteiriço e Amazonas, Acre e Mato Gr
- Uma ficha estadual identifica Guajará-Mirim como capital atual e Porto Velho como cidade d
- Uma legenda de hidrografia que registra Ji-Paraná e Machado como nomes alternativos do mes
- A ferrovia Madeira-Mamoré foi construída porque não existia conexão fluvial do Madeira com
- A associação da BR-364 com migração, assentamentos e formação de núcleos urbanos é pertine
- O Território Federal criado em 1943 já recebeu na lei de origem o nome de Rondônia, em hom
- Uma tabela que compara 1.581.196 habitantes no Censo 2022 com estimativa de 2026 deve info
- Como o Guaporé faz parte da rede formadora do Madeira, é correto afirmar que Guaporé e Ben
- Um documento de gestão hídrica de Rondônia pode listar Guaporé, Mamoré, Abunã, Madeira, Ja
- Num inventário de geração de energia, classificar Samuel como usina do rio Madeira e Jirau
- Pela divisão da LC nº 233/2000, a Zona 1 admite usos agropecuários, agroflorestais e flore
- A subzona 1.4 foi definida pela LC nº 233/2000 como área de baixa vulnerabilidade à erosão
- A história dos vales do Guaporé e Madeira deve considerar sociedades indígenas anteriores 
- O decreto imperial de 1866 abriu a navegação do Madeira até suas nascentes bolivianas e di
- A instalação do Forte Príncipe da Beira no século XVIII integra a estratégia portuguesa de
- Os quilombos do vale do Guaporé surgiram exclusivamente como consequência do trabalho ferr
- O Tratado de Petrópolis de 1903 relaciona a questão acreana à garantia de uma rota para a 
- A Estrada de Ferro Madeira-Mamoré substituiu toda a navegação fluvial da Amazônia e foi pr
- A Lei Complementar nº 41/1981 manteve os limites do antigo Território Federal de Rondônia 
- A primeira e a segunda aproximações do zoneamento de Rondônia tiveram necessariamente seis
```

## Pedido 40: Instalações, máquinas e medidas elétricas (`engenharia-eletrica.instalacoes-maquinas-medidas`, 831 tópicos de edital)

```
Matéria: engenharia-eletrica.instalacoes-maquinas-medidas (Instalações, máquinas e medidas elétricas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de eletrotécnica, medidas elétricas e segurança": 20
- "2. Aterramento, proteção contra choques e equipotencialização": 19
- "3. Luminotécnica, conforto visual e eficiência energética": 20
- "4. Transformadores monofásicos e trifásicos": 20
- "5. Motores, geradores, rendimento e integração dos sistemas": 21

Já existem (não repita):
- Ao medir a tensão sobre uma resistência de aquecimento em uma bancada, a pessoa responsáve
- Uma resistência elétrica de potência constante de 2 kW funciona durante 4 horas. Qual é a 
- O osciloscópio indica uma tensão perfeitamente senoidal de pico igual a 170 V em um circui
- Em uma instalação de baixa tensão, procura-se detectar uma corrente que foge do percurso n
- Uma luminária fornece fluxo de 2.400 lúmens que, numa aproximação ideal, incide uniformeme
- Um transformador monofásico ideal possui 500 espiras no primário e 100 no secundário; a te
- Uma turbina aciona um equipamento que fornece potência elétrica ao sistema. Qual descrição
- Um motor síncrono de quatro polos é alimentado por rede de 60 Hz. Qual é sua velocidade sí
- Para medir a corrente alternada de uma única fase num quadro, utiliza-se alicate amperímet
- Em uma instalação TT, as massas possuem eletrodo de terra próprio e a proteção precisa enf
- Um motor trifásico equilibrado recebe 380 V entre linhas, 10 A de linha e fator de potênci
- Um enrolamento trifásico opera em estrela equilibrada e apresenta 220 V entre cada fase e 
- Na estimativa por método dos lúmens, deseja-se 300 lx em 60 m². Cada luminária fornece 3.0
- É necessário alimentar um equipamento que demanda separação galvânica entre o circuito de 
- Um motor de indução trifásico de quatro polos alimentado a 60 Hz gira a 1.746 rpm em regim
- O responsável por um transformador real compara perdas medidas em vazio e sob diferentes c
- Durante uma inspeção de continuidade em instalação recém-construída, foi constatado que o 
- Antes de liberar trabalho numa instalação desenergizada, a equipe segue a redação da NR-10
- Um transformador trifásico atende enrolamento secundário em triângulo equilibrado, cada fa
- Uma máquina rotativa fornece 20 N·m de torque útil no eixo a 1.500 rpm. Qual é a potência 
- Em operação contínua, um motor recebe 5 kW de potência elétrica ativa e fornece 4,5 kW de 
- Um transformador tem ligação Y no primário e Δ no secundário. Um técnico pretende aplicar 
- Uma instalação possui transformador de 100 kVA alimentando cargas equilibradas de fator de
- Um transformador de corrente permanece com corrente no primário durante manutenção de medi
- Um voltímetro de entrada de 1 MΩ mede a tensão sobre o resistor inferior de um divisor for
- Um eletricista conecta o voltímetro em paralelo à carga e seleciona uma faixa de tensão co
- Após instalar um dispositivo diferencial-residual, uma equipe elimina o disjuntor do circu
- Um recinto apresenta 600 lúmens distribuídos uniformemente por uma superfície de 3 m². Nes
- A potência de placa de um transformador de 50 kVA garante o fornecimento de 50 kW de potên
- Em sistema de geração, uma máquina primária entrega torque e rotação a um gerador, que con
- No motor de indução trifásico convencional em motorização, a velocidade mecânica permanece
- Uma reforma substitui o condutor de proteção PE pelo neutro de operação N em todos os trec
- Em esquema TT, a corrente de falta envolvendo massas aterradas pode ser insuficiente para 
- Duas fontes luminosas têm índice de reprodução de cor semelhante. Dessa semelhança decorre
- No método dos lúmens, mantendo fluxo por luminária, área, meta de iluminância e fator de u
- Em enrolamento trifásico equilibrado conectado em triângulo, a tensão entre linhas é raiz 
- No transformador real, parte importante das perdas em carga decorre do aquecimento resisti
- Se um transformador ideal monofásico recebe uma tensão alternada de 60 Hz e reduz sua ampl
- Na redação da NR-10 vigente em outubro de 2026, a sequência para liberação segura de insta
- Um instrumento digital identificado como true RMS realiza medidas de tensão alternada exat
- Uma carga trifásica senoidal mantém potência ativa e tensão de linha constantes. Se a comp
- Para assegurar separação galvânica entre entrada e saída, um autotransformador de bobina c
- Em um motor de indução apto à partida estrela-triângulo, a ligação inicial em estrela pode
- Para conectar um gerador síncrono à rede elétrica, verificar apenas que a frequência insta
- Em transformador de corrente com primário conduzindo corrente, abrir inadvertidamente o se
```

## Pedido 41: Fonologia, fonemas e divisão silábica (`portugues.fonologia-fonemas`, 829 tópicos de edital)

```
Matéria: portugues.fonologia-fonemas (Fonologia, fonemas e divisão silábica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Letras, fonemas e correspondências entre escrita e fala": 20
- "2. Vogais, semivogais, ditongos, tritongos e hiatos": 16
- "3. Consoantes, encontros consonantais, dígrafos e separação silábica": 24
- "4. Sílaba tônica, classificação, ortoépia, prosódia e compreensão textual": 16
- "5. Divisão silábica, translineação e resolução integrada": 24

Já existem (não repita):
- Ao revisar a palavra 'chave', um estudante afirma que ela tem cinco letras e quatro fonema
- Na leitura-padrão de 'táxi', a letra x representa dois fonemas consonantais. Qual resultad
- Uma atividade pede classificar o encontro ai de 'pai' segundo a divisão em sílabas. Qual a
- Na palavra 'saúde', as vogais a e ú aparecem em sílabas sucessivas. Que análise de encontr
- Ao separar 'Paraguai' como Pa-ra-guai, o grupo uai permanece na última sílaba. Qual classi
- O vocábulo 'carro' contém rr, que representa um único fonema. Qual divisão silábica corres
- Em 'prato', as consoantes p e r são pronunciadas e permanecem na mesma sílaba. Como se cla
- Uma estudante marca a sílaba der como a mais forte na pronúncia de 'caderno'. Em que class
- Em um documento, 'vice-almirante' precisa ser quebrada exatamente no hífen entre os dois e
- Ao analisar 'guerra', um candidato identifica dois grupos de letras com valor de dígrafo c
- Uma explicação compara os grupos qu de 'quilo' e 'quase' quanto à pronúncia do u. Qual aná
- Na frase 'Os pais visitaram o país vizinho', as palavras destacadas diferem na divisão sil
- Em uma revisão da Base XX, o grupo sc de 'nascer' é tratado como dígrafo separável. Qual d
- Durante uma leitura, a pessoa acentua oralmente a primeira sílaba de 'rubrica'. Que correç
- No par 'O público aguarda' e 'Eu publico amanhã', a forma escrita distingue duas tonicidad
- O composto 'guarda-chuva' é transferido de linha exatamente no hífen que liga seus element
- Um revisor precisa dividir 'cooperar' em sílabas e identifica duas vogais o sucessivas. Qu
- Um editor afirma que a separação a-ba-de é válida como silabação, mas prefere não deixar a
- Uma atividade apresenta 'cresça' e 'excelente' e pede a divisão das sequências sç e xc nas
- Um material compara 'Paraguai' e 'Piauí', ambas contendo várias letras vocálicas próximas.
- Uma questão contrasta 'mãe' e 'lua'. Qual conclusão associa corretamente a estrutura dos e
- Um texto contém a palavra composta 'ex-alferes', cuja quebra de linha recai exatamente no 
- No aviso 'A fábrica fabrica equipamentos', as duas formas da mesma sequência de letras têm
- Uma revisão confronta os grupos br de 'abraço' e pt de 'apto' na divisão. Qual alternativa
- Uma mesma página traz 'carro' e 'chave' e precisa de translineação em ambos os vocábulos s
- Na palavra 'chave', as letras ch representam um único fonema consonantal; por isso a conta
- A letra x representa um único fonema em qualquer palavra portuguesa; consequentemente 'táx
- O encontro ai da palavra 'pai' constitui ditongo decrescente, porque a semivogal acompanha
- Por representar um único fonema, o dígrafo rr deve permanecer junto na separação de carro,
- A separação sa-ú-de mantém a e ú em sílabas diferentes, configurando hiato na palavra saúd
- Quando vice-almirante é dividida ao fim de linha exatamente entre seus componentes, basta 
- Em quilo, o u de qu não é pronunciado; em quase, o u é articulado na sílaba qua, embora as
- A separação correta de 'filho' é fil-ho, já que o dígrafo lh deve ser dividido assim como 
- A palavra 'caderno' é paroxítona mesmo sem sinal de acento gráfico, pois a penúltima sílab
- Se uma pessoa pronuncia 'rubrica' com tonicidade na primeira sílaba, a divergência decorre
- No texto 'O público espera; eu publico amanhã', a forma público é proparoxítona e publico 
- A Base XX mantém as letras do grupo sc unidas na palavra nascer, justificando a divisão na
- O vocábulo saguão apresenta encontro vocálico da sequência uão na mesma sílaba, classifica
- A Base XX determina que a letra x mantenha um único valor fonêmico em qualquer posição; po
- Na expressão 'Os pais visitam o país', a primeira palavra possui ditongo numa sílaba e a s
- Na translineação de guarda-chuva entre os dois elementos, a grafia guarda- no fim de uma l
- A Base XX prevê, além da regra geral de encontros consonantais estáveis como br, exceções 
- Como 'táxi' possui cinco fonemas na leitura de referência, o número de sílabas também é ci
- As palavras 'rubrica' e 'recorde', usadas nas acepções usuais de assinatura abreviada e me
- A divisão a-ba-de deixa uma sílaba inicial de uma única letra; por esse motivo, a Base XX 
```

## Pedido 42: Serviço público: conceito, princípios e classificação (`direito-administrativo.servico-publico`, 811 tópicos de edital)

```
Matéria: direito-administrativo.servico-publico (Serviço público: conceito, princípios e classificação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, elementos constitutivos e classificações do serviço público": 30
- "2. Princípios, serviço adequado e limites à interrupção": 19
- "3. Competência constitucional, prestação, descentralização e controle": 17
- "4. Lei nº 13.460/2017: usuário, direitos, deveres e Carta de Serviços": 20
- "5. Manifestações, ouvidorias, conselhos e avaliação dos serviços": 14

Já existem (não repita):
- Um serviço de transporte coletivo municipal atende passageiros mediante itinerários e padr
- Ao analisar o fornecimento de energia elétrica com consumo registrado em cada unidade, qua
- A iluminação das vias públicas beneficia indistintamente os transeuntes, sem medição indiv
- Uma norma técnica exige que equipamentos de serviço sejam conservados, modernizados e ampl
- Uma rede sofre rompimento inesperado que ameaça a segurança de quem usa o serviço. A inter
- Um Município mantém serviço público mediante departamentos internos sem criar entidade dot
- Foi solicitado ao Município que organize a prestação do transporte coletivo de interesse l
- Uma pessoa jurídica ainda não utiliza serviço público, mas poderá dele se beneficiar. Para
- Um usuário consulta a Lei nº 13.460/2017 para exigir informação sobre documentos necessári
- Uma reclamação é recebida conforme a Lei nº 13.460/2017, e a ouvidoria deve encaminhar a d
- Uma ouvidoria solicita formalmente informações a uma unidade vinculada para examinar manif
- Em discussão sobre serviço adequado, alguém confunde a redação da Lei nº 8.987/1995 com a 
- O poder público cria, por lei, entidade de direito público com personalidade própria para 
- Uma manifestação verbal é apresentada no atendimento. A unidade oferece formulário simplif
- Para avaliar atendimento público, um grupo pretende conferir se o conselho de usuários pod
- Uma interrupção por inadimplemento foi programada para iniciar na sexta-feira, após aviso 
- Uma pessoa utiliza serviço público, recebe orientação de acesso a informações e também pre
- Um ente pretende criar taxa especificamente para custear iluminação de vias públicas, just
- Uma empresa privada executa atividade de serviço público mediante vínculo jurídico pertine
- Ao solicitar um serviço, pessoa apresenta identificação com fé pública contendo CPF. A uni
- Um prestador pretende substituir a pesquisa anual de satisfação por método contínuo de ava
- O fornecimento foi interrompido por inadimplemento numa véspera de feriado, após aviso pré
- Em uma relação de serviços, alguém atribui à esfera municipal a manutenção do serviço post
- A administração apresenta Carta de Serviços apenas com lista dos serviços e endereço da un
- Uma ouvidoria reúne números de reclamações, seus motivos e providências em relatório de ge
- O enquadramento de iluminação de vias como serviço uti universi considera a fruição coleti
- Toda atividade privada sujeita a autorização administrativa e fiscalização constitui, por 
- O art. 6º, § 1º, da Lei nº 8.987/1995 inclui continuidade, segurança, atualidade, cortesia
- O princípio da continuidade, previsto na Lei nº 8.987/1995, torna juridicamente vedada qua
- A criação de departamentos internos para dividir o atendimento de serviço público pode car
- O art. 30, V, da Constituição atribui aos Estados os serviços públicos de interesse local,
- A Lei nº 13.460/2017 inclui, no conceito de usuário, pessoa jurídica que apenas potencialm
- Durante atendimento, o prestador exige prova nova sobre fato que já consta de documento vá
- Uma Carta de Serviços ao Usuário deve incluir informações sobre etapas, previsão de prazo 
- A ouvidoria poderá recusar manifestação apresentada verbalmente porque, pelo art. 10 da Le
- Nos termos do art. 16 da Lei nº 13.460/2017, a ouvidoria encaminha decisão final ao usuári
- Conforme a Lei nº 13.460/2017, os conselhos de usuários possuem natureza deliberativa e su
- A repartição constitucional dos serviços distingue os serviços locais de gás canalizado do
- A transferência da execução de serviço público a uma empresa privada altera a competência 
- Serviço de iluminação pública é classificado tradicionalmente como uti universi, e a Súmul
- Em suspensão por inadimplemento, a comunicação prévia do usuário afasta a vedação de inici
- Quando não existe ouvidoria, a Lei nº 13.460/2017 admite apresentar a manifestação diretam
- O prazo de vinte dias prorrogável uma vez por igual período, previsto no art. 16, parágraf
- O art. 23, § 1º, da Lei nº 13.460/2017 permite realizar avaliação por pesquisa anual ou po
- O art. 10-A da Lei nº 13.460/2017 permite exigir outro número identificador em todos os se
```

## Pedido 43: Emprego e colocação de pronomes (`portugues.pronomes`, 810 tópicos de edital)

```
Matéria: portugues.pronomes (Emprego e colocação de pronomes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Pronomes pessoais: função sintática, regência e referência": 20
- "2. Pronomes de tratamento e concordância nominal e verbal": 20
- "3. Demonstrativos, possessivos, indefinidos e coesão referencial": 20
- "4. Pronomes relativos: antecedentes, concordância e regência": 20
- "5. Colocação pronominal e reescrita: próclise, ênclise e mesóclise": 20

Já existem (não repita):
- Uma servidora escreveu: 'Separei os processos para ___ revisar amanhã'. O espaço correspon
- Um relatório foi enviado ao diretor. Na reescrita 'Enviei ___ o relatório', o pronome deve
- Uma pessoa se dirige diretamente a uma interlocutora em contexto de tratamento tradicional
- Em conversa presencial, a falante segura uma pasta junto a si, aponta outra ao lado do ouv
- Em 'A pesquisadora ___ tese foi premiada fará palestra', deseja-se empregar relativo posse
- Uma equipe registrou a frase 'Não me comunicaram a alteração'. Ao reescrevê-la sem alterar
- Uma revisão deve substituir o objeto direto em 'Vou revisar o documento'. Considerando o i
- O texto afirma: 'O prazo terminou. ___ ocasionou a devolução dos autos'. Considerando a re
- Uma pessoa deseja identificar a localidade de destino do deslocamento: 'A cidade ___ iremo
- Um redator deseja manter tom formal tradicional na frase futura '___ a documentação amanhã
- Um servidor redige comunicação dirigida a agente público da administração federal diretame
- Ao substituir o sujeito 'nós' por 'você' na frase 'Nós concluímos a revisão', que reescrit
- A frase 'A coordenadora comunicou à analista que sua proposta foi aceita' admite mais de u
- Uma nota diz: 'Marina e Beatriz participaram: ___ redigiu a ata; ___ fez a revisão'. Prete
- Um texto formal contém 'A norma ___ me referi foi alterada'. O verbo é 'referir-se a', e o
- Um redator combina futuro do presente e negação: 'Não ___ enviarei a correspondência'. Des
- Um texto traz 'Certa candidata apresentou uma proposta' e 'A candidata apresentou uma prop
- Na norma formal, uma oração inicia por 'Em ___ tratando de urgência, convém responder rapi
- Em 'Aqueles relatórios que foram recebidos serão avaliados', substitui-se o sujeito por 'a
- Na reescrita do período 'O autor de quem precisamos enviou os dados', deseja-se manter o v
- Uma comunicação escrita dirige-se a agente público federal abrangido pelo Decreto 9.758/20
- Uma autora escreveu: 'Os pesquisadores ___ trabalhos me referi concordaram com o relatório
- Uma ata informa dois deslocamentos: 'A cidade ___ a equipe visitou recebeu o seminário; o 
- Compare 'Informei o gerente do atraso' e 'Informei o atraso ao gerente'. A intenção é subs
- Deseja-se manter a condição futura em 'Se eu não entregar o documento amanhã, haverá atras
- Na frase 'Eles entregaram o relatório à destinatária', o termo 'à destinatária' pode ser s
- Em uma comunicação dirigida diretamente a um interlocutor, 'Sua Excelência' é a forma trad
- Em 'O texto estava ilegível. Isso exigiu nova cópia', o demonstrativo 'isso' retoma a situ
- Na expressão 'a diretora cujo o parecer foi aprovado', o uso de artigo depois de 'cujo' é 
- Em 'Jamais me entregaram a planilha', a colocação do pronome átono antes do verbo é compat
- Em texto formal, a frase 'O material é para mim conferir' segue a mesma norma de 'O materi
- A frase 'A senhora solicitou seus documentos' usa verbo na terceira pessoa, mas a identifi
- Em 'Esta é a informação: o prazo foi prorrogado', 'esta' é anafórico porque retoma somente
- Na norma-padrão, 'A regra à qual me referi' preserva a preposição exigida pelo verbo prono
- Em 'Tinha comunicado-me a decisão', a ênclise ao particípio 'comunicado' é admitida pela n
- Em 'A professora a cujos pareceres me referi participou da reunião', a preposição 'a' deco
- O relativo 'onde' é considerado a escolha formal obrigatória tanto para lugar físico quant
- Na condição futura 'Se eles não o fizerem, haverá atraso', a próclise se justifica pelo co
- A frase 'Vossa Excelência fostes informada' mantém a norma-padrão por usar verbo de segund
- Em 'Nós redigíamos relatórios' e 'Nós redigimos relatórios', pode haver diferença aspectua
- Em 'A diretora apresentou à analista sua proposta', o pronome possessivo 'sua' identifica 
- As estruturas 'Informei o usuário da alteração' e 'Informei a alteração ao usuário' podem 
- Na reescrita formal de 'Encontrei o supervisor', o pronome 'lhe' substitui adequadamente '
- A distinção escolar entre 'este' para anunciar conteúdo e 'esse' para retomar informação j
- A reescrita de 'Vendem-se imóveis' para 'Vende-se imóveis', mantendo leitura de voz passiv
```

## Pedido 44: Atos e prazos processuais (`processo-civil.atos-prazos-processuais`, 809 tópicos de edital)

```
Matéria: processo-civil.atos-prazos-processuais (Atos e prazos processuais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, forma dos atos, negócios e processo eletrônico": 20
- "2. Atos judiciais, tempo, lugar e classificação dos prazos": 19
- "3. Contagem, suspensão, preclusão e regimes especiais de prazo": 16
- "4. Comunicação processual: citações, intimações e cartas": 24
- "5. Nulidades, distribuição, registro e valor da causa": 21

Já existem (não repita):
- Em um processo, uma manifestação foi apresentada em formato diferente do costumeiro, sem d
- Um processo versa sobre guarda de criança e contém informações familiares sensíveis. Uma p
- Em acordo firmado entre o juiz e as partes, foi estabelecida para 15 de maio uma audiência
- Duas partes plenamente capazes convencionam ajuste no procedimento de litígio sobre direit
- O juiz indefere a produção de determinada prova durante a instrução, sem encerrar a fase d
- Em procedimento comum, o juiz decide o mérito e encerra a fase cognitiva por pronunciament
- Em autos processuais, há mera juntada de petição e vista obrigatória decorrente da própria
- No último dia do prazo, que não é feriado e não sofreu indisponibilidade, uma petição é tr
- Uma parte é intimada para praticar ato e não existe prazo previsto na lei nem fixado pelo 
- Um prazo processual de cinco dias úteis começa a ser contado numa terça-feira, sem feriado
- Um prazo processual encontra-se em curso quando chega 20 de dezembro. Na ausência de regra
- Um magistrado organiza os atos de seu gabinete e verifica os prazos legais para despacho, 
- Um Município é intimado pessoalmente para apresentar manifestação com prazo legal geral de
- Foi proferido despacho de citação para que uma pessoa demandada passe a integrar a relação
- Um juiz de primeiro grau precisa que outro juízo brasileiro, situado em comarca diferente,
- Uma intimação é feita por carta registrada, com aviso de recebimento. O aviso de recebimen
- Em processo eletrônico, uma intimação de advogado que não exige vista ou intimação pessoal
- Uma pessoa jurídica de direito público cadastrada recebe citação pelo Domicílio Judicial E
- Em processo integralmente eletrônico, dois réus têm procuradores diferentes, pertencentes 
- A parte apresenta validamente uma manifestação processual cuja oportunidade já exercitou i
- Em comarca onde o transporte é especialmente difícil, o juiz avalia prorrogação de prazo p
- Um réu comparece espontaneamente ao processo e apresenta defesa, embora tenha havido falha
- O oficial de justiça tenta citar pessoa em casa por duas vezes sem encontrá-la, registra s
- Uma intimação foi praticada com pequena divergência formal, mas a parte recebeu ciência in
- Numa demanda, há pedidos alternativos de R$ 40 mil e R$ 70 mil. Em outra, há pedido princi
- Em processo sobre guarda de criança, a tramitação pode ocorrer sob segredo de justiça, sem
- Durante julgamento, o juiz identifica fundamento jurídico de ofício ainda não debatido e d
- No calendário processual fixado de comum acordo pelo juiz e pelas partes, a audiência já d
- Se uma petição eletrônica chega ao sistema às 21h30 do último dia útil do prazo, pelo horá
- Em prazo processual de sete dias fixado pelo juiz, sábados e domingos normalmente não entr
- O período anual de suspensão do curso dos prazos processuais previsto no CPC inicia-se em 
- O servidor responsável demora além de cinco dias para praticar ato que deveria cumprir seg
- Uma autarquia de direito público é intimada pessoalmente de decisão e tem prazo legal expr
- A intimação pessoal de Município em processo eletrônico pode fundamentar prazo em dobro pa
- Em autos eletrônicos, dois corréus são representados por advogados de escritórios distinto
- No regime do CPC, a Defensoria Pública dispõe de prazo em dobro para manifestações process
- Uma intimação comum de advogado, sem exigência legal de pessoalidade, é disponibilizada no
- Depois de falha na citação inicial, o réu comparece espontaneamente e oferece contestação.
- Uma empresa privada cadastrada não confirma o recebimento de citação eletrônica dentro de 
- Uma citação eletrônica é confirmada em prazo regular no Domicílio Judicial Eletrônico. Con
- Um feito exigia intervenção obrigatória do Ministério Público, mas não houve intimação. O 
- Uma ação é ajuizada novamente depois de ter sido extinta sem resolução do mérito. Mesmo co
- Depois de intimada na pessoa de seu advogado para recolher as custas e despesas de ingress
- Em ação de alimentos, a parte autora pede prestação mensal de R$ 900, sem outros pedidos. 
- Em outubro de 2026, a regra acrescida ao art. 196 do CPC pela Lei 15.479/2026, sobre forma
```

## Pedido 45: Balanço patrimonial e demonstração do resultado (`contabilidade.balanco-dre`, 803 tópicos de edital)

```
Matéria: contabilidade.balanco-dre (Balanço patrimonial e demonstração do resultado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Demonstrações contábeis, estrutura conceitual e legislação societária": 16
- "2. Balanço patrimonial: classificação e grupos do ativo, passivo e PL": 29
- "3. Avaliação patrimonial, direitos a receber e mudanças legislativas": 16
- "4. Demonstração do resultado, receitas, custos, despesas e competência": 27
- "5. Encerramento do exercício, conciliações e questões integradas": 12

Já existem (não repita):
- Em 31 de dezembro, uma sociedade registra ativos de 480.000 e obrigações de 310.000. Sem o
- Uma entidade deseja apresentar, em peças distintas, os recursos e obrigações de 31 de deze
- Uma empresa vende mercadorias a prazo em dezembro, entrega os bens naquele mês e recebe em
- O fechamento revela capital social de 100.000, reservas de lucros de 25.000 e prejuízos ac
- Uma companhia fechada apresenta patrimônio líquido de 1.700.000 no balanço. À luz da dispe
- Uma loja mantém mercadorias para comercialização no ciclo normal e um veículo utilizado de
- Em janeiro, uma empresa compra por 40.000 mercadorias que permanecem no depósito até abril
- Em dezembro, a empresa paga antecipadamente seguro relativo aos primeiros seis meses do an
- Uma indústria possui ciclo operacional documentado de 18 meses e estoque que será vendido 
- Uma entidade possui crédito contratual não operacional a receber somente após o exercício 
- Uma sociedade concede empréstimo de curtíssimo prazo, fora de seus negócios usuais, a um a
- Na data do balanço, uma dívida vence em oito meses e a entidade não tem direito contratual
- Uma aplicação de liquidez imediata, adquirida originalmente com vencimento de dois meses e
- Uma empresa apresenta duplicatas de 90.000 e estimativa fundamentada de perdas esperadas d
- Uma venda a prazo por dois anos gera diferença entre preço nominal e valor presente da con
- Um estoque tem custo de 72.000, mas preço estimado de venda de 68.000 e custos para conclu
- No encerramento, a empresa controla um prédio usado na operação, uma patente adquirida com
- No balanço de 31 de dezembro, uma sociedade tem fornecedores ligados ao ciclo de produção 
- Uma entidade vende equipamento e manutenção por dois anos em um único contrato, tendo amba
- Durante contrato de construção, o cliente controla o ativo à medida que é criado e a entid
- Uma empresa apura vendas brutas de 360.000, devoluções de 20.000, tributos sobre vendas de
- Em 28 de dezembro, cliente antecipa 24.000 por serviço de suporte a ser integralmente pres
- Uma sociedade apura lucro líquido de 75.000 e reconhece, segundo norma aplicável, outros r
- Uma companhia elabora demonstração das mutações do patrimônio líquido com dados sobre lucr
- Ao preparar demonstrações de exercício iniciado em 2026, um analista sustenta que a public
- Na data do encerramento, uma entidade com ativos de 230.000 e passivos de 90.000 apresenta
- Uma prestação de serviço concluída em dezembro e recebida em janeiro deverá figurar como r
- No balanço de uma indústria, uma máquina corpórea destinada ao processo de produção tende 
- Uma loja compra mercadorias para revenda em novembro e só as vende em fevereiro; pela comp
- Na organização societária atual do balanço, a rubrica ativo não circulante compreende real
- Uma aplicação adquirida há três anos e com vencimento daqui a dois meses torna-se equivale
- Uma companhia aberta que elabora as demonstrações exigidas pelo art. 176 da Lei nº 6.404/1
- Uma companhia empresta dinheiro a acionista em operação não usual à exploração de seu obje
- Estoque destinado à venda no ciclo operacional normal de uma empresa pode figurar no circu
- Uma empresa possui duplicatas de 50.000 e perdas esperadas de 2.000; como os clientes aind
- Mercadorias com custo registrado de 18.000 e valor realizável líquido de 15.500 devem ser 
- Os sócios integralizam 70.000 no capital em dinheiro e, por aumentar o caixa e o patrimôni
- Com lucro líquido de 45.000 e outros resultados abrangentes positivos líquidos de 6.000, u
- Em 31 de dezembro, dívida vence em oito meses e a entidade não possui direito de diferir l
- Uma sociedade cumpriu obrigação de desempenho, mas o direito de receber depende de aprovaç
- Embora o CPC 51 tenha sido divulgado em janeiro de 2026, a Resolução CVM nº 237/2025 o tor
- Recebíveis comerciais abrangidos pelo CPC 48 podem exigir reconhecimento de perdas de créd
- No encerramento de um exercício com lucro de 30.000, a distribuição desse montante aos sóc
- Uma sociedade tem 40.000 a receber de cliente e 22.000 a pagar a fornecedor independente. 
- Em janeiro, um cliente liquida duplicata de venda cuja receita foi reconhecida em dezembro
```

## Pedido 46: Direitos sociais (arts. 6º a 11) (`direito-constitucional.direitos-sociais`, 801 tópicos de edital)

```
Matéria: direito-constitucional.direitos-sociais (Direitos sociais (arts. 6º a 11))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, catálogo constitucional e efetivação dos direitos sociais": 20
- "2. Direitos dos trabalhadores: art. 7º, incisos e proteção constitucional": 22
- "3. Liberdade sindical, contribuições, greve e representação (arts. 8º a 11)": 20
- "4. Saúde como direito fundamental social: art. 196, SUS e tutela jurisdicional": 19
- "5. Efetividade, direitos coletivos e comparação de casos constitucionais": 19

Já existem (não repita):
- Após atualização de material de direitos fundamentais, um estudante quer identificar qual 
- Em processo de gestão de pessoal, questiona-se qual é o limite constitucional ordinário da
- Uma empresa remunera o serviço extraordinário com acréscimo de 30% sobre a hora normal, su
- Uma organização sindical pretende representar a mesma categoria de outra entidade na mesma
- Uma empresa possui 200 empregados e deseja aplicar literalmente a garantia de representant
- Ao estudar a proteção constitucional da saúde, uma equipe precisa localizar a norma que a 
- Empregador comunica a trabalhadora gestante que sua licença constitucional terá 90 dias, a
- Em consulta sobre contribuições sindicais, trabalhador não filiado recebe cobrança de cont
- Uma jovem de quinze anos pretende trabalhar regularmente em atividade não enquadrada como 
- Uma autoridade quer organizar o SUS conforme a Constituição, observando descentralização, 
- Uma lei organiza programa permanente de transferência de renda dirigido a brasileiros em v
- Um trabalhador encerrou vínculo empregatício e deseja conhecer os limites constitucionais 
- Uma convenção coletiva reduz temporariamente salário para reorganização produtiva, observa
- Uma entidade sindical institui contribuição assistencial por convenção coletiva aos trabal
- Um empregador pergunta se o empregado filiado e eleito para direção sindical, inclusive su
- Trabalhadores de atividade essencial deliberam greve, e uma autoridade afirma que a Consti
- Uma associação promove estudo sobre ensino, saúde e benefícios sociais. Pretende equiparar
- Uma equipe quer fixar o fundamento constitucional que autoriza participação complementar d
- Uma lei propõe obrigar todos os trabalhadores a pagar contribuição sindical prevista nos a
- Uma diretriz de saúde pública prioriza ações preventivas e mantém assistência terapêutica 
- Em demanda ajuizada por paciente que requer medicamento registrado na Anvisa, mas não incl
- Em 2026, sindicato pretende exigir contribuições assistenciais referentes a período anteri
- Uma empregada doméstica reivindica horas extras e FGTS, e a empresa afirma que o art. 7º, 
- Uma equipe analisa recurso escasso e negação de atendimento básico essencial. A Administra
- Uma empregada filiada a sindicato deseja votar e candidatar-se à direção após aposentar-se
- Ao organizar material de direitos fundamentais, uma professora classifica saúde, educação 
- Uma empresa garante descanso semanal remunerado apenas aos domingos por entender que o art
- Um sindicato organiza negociação coletiva sobre condições de emprego. O art. 8º, VI, da Co
- O art. 196 da Constituição faz do direito à saúde benefício dependente de contribuições pr
- Um jovem de quatorze anos ingressa regularmente em atividade como aprendiz, em trabalho qu
- Uma norma administrativa condiciona a fundação de sindicato a autorização discricionária e
- Um serviço público é organizado em rede regionalizada e hierarquizada, com direção única e
- Os arts. 578 e 579 da CLT, conforme legislação de 2017 validada pelo STF na ADI 5.794, imp
- Um trabalhador demitido ajuíza ação um ano depois do fim do contrato e pede créditos refer
- O art. 9º da Constituição confere aos empregadores a decisão sobre oportunidade da greve e
- Uma cláusula de convenção coletiva prevê contribuição assistencial para associados e não a
- A tese do Tema 6 do STF permite, por regra geral, fornecimento judicial de qualquer medica
- Uma organização pública demonstra redução de recursos e debate alternativas para preservar
- O art. 8º, II, admite duas entidades sindicais de mesmo grau e categoria em idêntica base 
- A Lei nº 13.467/2017 e o julgamento da ADI 5.794 não alteram a exigibilidade restrita da c
- Uma empresa emprega 201 pessoas, mas impede a eleição do representante do art. 11 alegando
- Um governo modifica o formato de entrega de serviços sociais e comprova ampliação do acess
- O parágrafo único do art. 6º da Constituição, incluído pela EC 114/2021, fixa diretamente 
- Conforme o art. 10 da Constituição, trabalhadores e empregadores devem participar dos cole
- A incorporação do PIDESC pelo Decreto nº 591/1992 faz desaparecer a necessidade de impleme
```

## Pedido 47: Empresário e estabelecimento empresarial (`direito-empresarial.empresario-estabelecimento`, 793 tópicos de edital)

```
Matéria: direito-empresarial.empresario-estabelecimento (Empresário e estabelecimento empresarial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Evolução do Direito Empresarial e teoria da empresa": 23
- "2. Empresário individual, capacidade, ruralidade e modalidades unipessoais": 23
- "3. Registro mercantil, SINREM, decisões e inatividade": 20
- "4. Nome empresarial, firma, denominação e escrituração": 16
- "5. Estabelecimento empresarial, trespasse e sucessão civil": 18

Já existem (não repita):
- Uma pessoa compra regularmente mercadorias de fabricantes, mantém estoque e coordena empre
- Um profissional realiza pessoalmente consultas especializadas, com apoio de secretária, se
- Ao estudar uma fábrica, alguém distingue o proprietário que dirige o negócio, a produção h
- Uma pessoa natural se inscreve como empresária individual e passa a realizar operações em 
- Um agricultor desenvolve como profissão principal atividade rural organizada e decide soli
- Uma empresa requer arquivamento de alteração de ato constitutivo na Junta Comercial. Qual 
- Um pedido de arquivamento apresenta falha documental sanável. A Junta formula exigência, c
- Uma pessoa abre loja física e reúne estoque, equipamentos e signos distintivos para explor
- Uma sociedade vende a outra todo o estabelecimento como unidade, mas pretende dar eficácia
- Uma pessoa incapaz herdou estabelecimento anteriormente explorado por seu pai. Seu represe
- Após 11 anos sem arquivar novos atos, uma sociedade ainda registrada recebe informação de 
- Uma sociedade anônima apresenta seu ato de constituição a arquivamento, enquanto um empres
- Uma sociedade recebeu decisão definitiva singular desfavorável da Junta e quer percorrer c
- Uma sociedade limitada escolhe nome composto por expressão fantasia seguida de 'Ltda.', ao
- Um empresário pequeno, enquadrado especificamente no art. 970 e na LC nº 123/2006, art. 68
- Após vender seu estabelecimento, o alienante não conserva bens suficientes para pagar cred
- Na alienação de estabelecimento, há dívida anterior regularmente contabilizada cujo vencim
- Em uma sociedade, o ato constitutivo foi assinado em 1º de março e apresentado a arquivame
- Uma sociedade apresentou arquivamento de transformação societária e recebeu decisão colegi
- Ao avaliar o direito registral vigente, uma pessoa cita o antigo art. 60 para exigir comun
- Uma pessoa compra estabelecimento e deseja utilizar o nome empresarial do alienante no nov
- Uma loja vendida como estabelecimento possui dívidas anteriores regularmente contabilizada
- Um empresário aliena estabelecimento e, dois anos depois, pretende abrir negócio concorren
- No trespasse de loja, um contrato de fornecimento não pessoal foi transferido ao adquirent
- Após publicação do trespasse, devedor de crédito integrante do estabelecimento paga ao ant
- Uma pessoa atua habitualmente na circulação de mercadorias, mobilizando capital, compras, 
- Profissional que exerce pessoalmente atividade intelectual passa a ser empresário apenas p
- Um produtor rural cuja profissão principal é atividade rural decide registrar-se na Junta 
- O empresário individual inscrito adquire, como efeito automático de sua matrícula, persona
- Uma empresa apresenta ato de alteração ao Registro Público de Empresas Mercantis. Arquivam
- O estabelecimento empresarial é o imóvel físico onde funciona a loja; caso ela passe a fun
- Uma EIRELI existente quando entrou em vigor a Lei nº 14.195/2021 foi transformada, por for
- Uma sociedade deixa de apresentar atos à Junta Comercial por dez anos, e o antigo art. 60 
- Em ato submetido a decisão singular definitiva desfavorável, o interessado pode recorrer a
- A firma do empresário individual pode ser composta exclusivamente de expressão fantasia, s
- Para dar eficácia perante terceiros à alienação de um estabelecimento, são necessárias ave
- Se o alienante do estabelecimento não conservar bens suficientes ao pagamento de seu passi
- A venda do estabelecimento com débitos anteriores regularmente contabilizados torna o adqu
- Um terceiro contratante rescindiu, sem justa causa, contrato não pessoal transferido com o
- Em contrato de trespasse sem autorização expressa para concorrer, o alienante fica sujeito
- No contrato de trespasse, uma dívida regularmente contabilizada e ainda não vencida inicia
- O adquirente de estabelecimento pode, se o contrato permitir, usar o nome do alienante pre
- Mesmo diante de cessão de créditos decorrente da transferência do estabelecimento já publi
- Se contrato de usufruto de estabelecimento tem prazo de dois anos e não há autorização exp
- Em registro mercantil, o pedido de reconsideração de exigência e o recurso ao plenário seg
```

## Pedido 48: Extinção do ato: anulação, revogação, convalidação, cassação e caducidade (`direito-administrativo.extincao-ato-administrativo`, 792 tópicos de edital)

```
Matéria: direito-administrativo.extincao-ato-administrativo (Extinção do ato: anulação, revogação, convalidação, cassação e caducidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Extinção do ato administrativo e autotutela": 19
- "2. Invalidade, nulidades e vícios do ato": 16
- "3. Anulação, decadência administrativa e prescrição": 17
- "4. Revogação e demais modos de extinção": 30
- "5. Convalidação, preservação e questões avançadas": 18

Já existem (não repita):
- Uma administração constata que concedeu licença contrariando requisito legal vigente à épo
- Após mudança justificada de prioridades, a administração decide retirar autorização discri
- Um ato federal favorável foi praticado em 2021 e nenhum fato indica má-fé. Que prazo traz 
- Uma autorização válida exigia manutenção de dispositivos de proteção. O beneficiário remov
- No procedimento de revisão, descobre-se assinatura de autoridade sem competência exclusiva
- Um ato favorável produziu pagamentos mensais contínuos a partir de agosto de 2021. Qual é 
- Uma decisão administrativa válida fixou termo final em 30 de novembro. Chegada a data, seu
- Uma administração pretende retirar benefício individual que já produziu efeitos concretos,
- Após editar ato discricionário regular, a administração informa que não existe mais intere
- A autoridade comprovou que a autorização fora motivada por vistoria favorável que jamais o
- Uma norma posterior proíbe atividade antes regularmente autorizada, sem prever preservação
- Uma autoridade competente confirma ato anteriormente assinado por agente sem atribuição nã
- Uma concessão inicial de aposentadoria chega ao tribunal de contas em 4 de maio de 2022. Q
- Um ato federal favorável foi editado com defeito legal, mas a autoridade demonstra comprov
- Uma decisão administrativa possui três comandos independentes, e apenas o último contém ob
- Em ato administrativo a lei reservou expressamente a decisão a uma autoridade específica, 
- Em 2026, procedimento punitivo federal de poder de polícia está paralisado há quatro anos,
- Uma decisão favorável federal com pagamentos mensais iniciou efeitos em junho de 2020. Em 
- A administração pretende invalidar ato favorável de 2018 sem medida de impugnação tempesti
- Uma autoridade diz que pode revogar licença vinculada porque discorda da escolha feita sob
- Um documento forjado por particular, sem qualquer investidura pública, simula decisão de a
- Uma autoridade procura corrigir desvio de finalidade mediante nova assinatura de superior,
- Depois de deferir ato favorável, a administração identifica ilegalidade e decide anulá-lo 
- Um ato foi editado de forma válida, mas lei posterior extinguiu a possibilidade jurídica d
- No âmbito federal, uma autoridade sustenta que pode convalidar ato defeituoso mesmo quando
- Uma autorização válida foi retirada porque a Administração deixou de considerá-la oportuna
- Um benefício administrativo favorável é pago mensalmente. Para o prazo do art. 54, § 1º, d
- Depois de autorizada atividade em conformidade com o direito, seu titular descumpre condiç
- Um ato com desvio de finalidade pode ser convalidado por simples ratificação, desde que a 
- A Administração pode reconhecer ilegalidade de ato próprio sem autorização judicial prévia
- A autorização expirou no termo originalmente previsto; por isso a forma técnica de extinçã
- Ao decidir convalidar ato sanável, a Administração deve motivar a decisão, e não apenas re
- Uma autoridade entende que anulação por ilegalidade e revogação por conveniência possuem, 
- A alegação de que uma vistoria ocorreu foi expressamente adotada como motivo para deferir 
- O art. 54 da Lei nº 9.784/1999 e a Lei nº 9.873/1999 estabelecem o mesmo instituto, pois a
- Uma autorização legalmente concedida tem efeito extinto por lei posterior que proíbe a ati
- Em um ato cuja competência era exclusiva, a ratificação por autoridade diversa elimina o v
- Uma decisão administrativa que revoga ato por conveniência precisa indicar razões de fato 
- Concessão inicial de pensão aguardou exame em corte de contas por mais de cinco anos desde
- Na aplicação do art. 54, § 2º, uma medida formal de autoridade administrativa que impugne 
- Um documento falso, criado por particular sem investidura, e uma decisão ilegal de agente 
- Um ato de vários comandos independentes contém uma disposição ilegal que pode ser excluída
- Se houve ilegalidade na concessão de direito já usufruído, a autoridade deve desfazê-lo di
- Um ato favorável contém vício formal secundário corrigível, e a correção não causa dano a 
- A prescrição intercorrente de procedimento punitivo federal paralisado por mais de três an
```

## Pedido 49: Recursos no processo civil (`processo-civil.recursos`, 788 tópicos de edital)

```
Matéria: processo-civil.recursos (Recursos no processo civil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema recursal, cabimento e admissibilidade": 19
- "2. Disposição, efeitos recursais e apelação": 19
- "3. Agravo de instrumento, agravo interno e embargos de declaração": 17
- "4. Julgamento nos tribunais, poderes do relator e colegialidade": 20
- "5. Recursos excepcionais, filtros e questões avançadas": 25

Já existem (não repita):
- Em ação de cobrança, a decisão final resolve integralmente os pedidos e a parte vencida qu
- Ao preparar recurso contra sentença, a representação processual verifica o prazo geral do 
- Uma decisão interlocutória rejeita pedido de gratuidade da justiça. Ainda durante o proces
- O relator profere decisão monocrática negando provimento ao recurso. A parte pretende subm
- Um acórdão contém dois comandos incompatíveis entre si no dispositivo. Sem pretender simpl
- O recorrente recolheu custas inferiores às exigidas e comprovou o pagamento parcial ao int
- Uma parte apresenta apelação tempestiva e a adversária pretende recorrer apenas se o recur
- Depois de interpor recurso contra sentença, o recorrente decide desistir da impugnação. A 
- O tribunal recebe apelação de sentença condenatória que não se enquadra nas exceções do ar
- Uma interlocutória indefere providência não listada no art. 1.015, sem urgência concreta. 
- O juiz rejeita alegação de convenção de arbitragem em decisão interlocutória. Uma parte pr
- Uma decisão não incluída literalmente nos incisos do art. 1.015 causará prejuízo irreversí
- No agravo interno, o órgão colegiado nega provimento por unanimidade, mas a insurgência er
- O tribunal reforma sentença que extinguiu a ação sem exame do mérito. A instrução está com
- Em apelação, três integrantes do colegiado votam e o resultado é de dois votos contra um. 
- Após a intimação da sentença, a parte apresenta tempestivamente embargos de declaração por
- Na sessão de apelação, a parte pede sustentação oral, sem existir exceção legal que a impe
- Em recurso interposto após a Lei 14.939/2024, não foi juntada prova de feriado local, embo
- Em outubro de 2026, pretende-se interpor recurso especial contra acórdão publicado após a 
- A presidência do tribunal de origem inadmite recurso especial por motivo diverso da aplica
- Ao julgar admissibilidade de recurso especial, discute-se o quórum para rejeitar a relevân
- Autor e réu foram parcialmente vencidos. O autor apela e o réu apresenta adesivo tempestiv
- Em agravo de instrumento, por dois votos a um, o colegiado mantém decisão interlocutória q
- Uma parte sustenta violação direta à Constituição em recurso excepcional; outra aponta vio
- A parte desiste do recurso especial, mas a questão nele discutida já teve relevância feder
- Um servidor cartorário expede despacho destinado apenas à movimentação do processo, sem re
- Terceiro que demonstra que uma decisão atingirá direito próprio não pode interpor recurso 
- Ao contar prazo de quinze dias para apelação no procedimento comum, a parte deve observar 
- Se uma das partes apresentar agravo de instrumento, a outra poderá interpor agravo adesivo
- Diante de sentença omissa quanto a pedido expressamente formulado, a parte pode opor embar
- Quando a sentença condena ao pagamento de alimentos, a apelação contra ela possui necessar
- Uma decisão interlocutória da fase de conhecimento não comporta agravo de instrumento nem 
- Em agravo interno, a decisão colegiada de negar provimento por unanimidade impõe multa obr
- Embargos de declaração tempestivos apenas suspendem o prazo dos demais recursos, que recom
- Ao julgar procedente pedido de instituição de arbitragem, a sentença pode produzir efeitos
- O cabimento excepcional do agravo de instrumento fora das hipóteses literais do art. 1.015
- No julgamento de apelação, o voto vencido é descartado da formação do acórdão e não poderá
- O relator identifica em recurso um vício documental suprível. Antes de inadmiti-lo por ess
- Depois da alteração de 2024, a ausência de prova de feriado local na interposição de recur
- Em julgamento de apelação cujo resultado foi de dois votos a um, sem hipótese de exclusão,
- A decisão que julgou parcialmente o mérito foi mantida em agravo de instrumento por maiori
- A Lei 15.484/2026 determina que a inexistência de relevância federal no recurso especial s
- Após a Lei 15.484/2026, qualquer decisão que negue seguimento a recurso especial com base 
- Para obter pré-questionamento ficto, basta opor embargos de declaração, ainda que o tribun
- O relator entende que recurso especial versa sobre questão constitucional, e não sobre dir
```

## Pedido 50: Estatuto do servidor municipal e estadual: conteúdo típico (`legislacao-municipal.estatuto-servidor-municipal-estadual`, 788 tópicos de edital)

```
Matéria: legislacao-municipal.estatuto-servidor-municipal-estadual (Estatuto do servidor municipal e estadual: conteúdo típico)
Capítulos e quantas questões quero em cada um (total 100):
- "Regimes jurídicos subnacionais e matriz constitucional": 20
- "Provimento, posse, exercício, estágio probatório e vacância": 20
- "Remuneração, vantagens, férias, licenças e afastamentos": 20
- "Deveres, proibições, responsabilização e processo disciplinar": 20
- "Comparação com a Lei 8.112 e método de estudo do estatuto do edital": 20

Já existem (não repita):
- Em concurso municipal, qual fonte define, em regra, o prazo de posse do aprovado para carg
- Um estado adota lei estatutária para servidores efetivos e outra estrutura de empregos cel
- A administração municipal quer ocupar permanentemente cargos efetivos usando contratos tem
- Depois de aprovação em concurso, uma candidata recebe ato de nomeação. Qual evento corresp
- Um servidor ingressou em cargo exclusivamente comissionado e trabalhou quatro anos. Qual a
- Acerca da aquisição da estabilidade, assinale o conjunto constitucionalmente adequado.
- Após invalidada judicialmente sua demissão, um servidor estável deve retornar à situação f
- Uma chefia chama de demissão o desligamento voluntário solicitado por servidor. Qual corre
- Sobre adicionais e gratificações previstos em estatuto estadual, a resposta mais segura é:
- Um edital prevê questões sobre férias de servidores municipais. Qual garantia decorre da C
- Um servidor requer licença por interesse particular e alega que é obrigatoriamente remuner
- Um servidor estatutário eleito prefeito precisa aplicar as regras constitucionais de manda
- Ao instaurar um PAD com potencial de demissão, a administração deve:
- Uma sindicância municipal foi aberta apenas para esclarecer fatos. Como deve ser caracteri
- O estatuto local prevê afastamento preventivo na investigação. A medida, quando legalmente
- Qual aspecto do PAD pode variar legitimamente entre leis municipais e estaduais?
- No estudo de estatuto estadual, qual estratégia evita o erro de decorar um único prazo fed
- Em 2026, um professor concursado deseja acumular seu cargo com outro cargo público de natu
- Ao comparar estatuto estadual com Lei 8.112, o pesquisador encontra licença que só está de
- Uma questão diz: segundo a lei do Município X, o prazo recursal no PAD é de dez dias, mas 
- A aprovação em concurso para emprego público costuma ser exigida pelo art. 37, II, ainda q
- A ADI 2.135 determinou que os servidores estatutários anteriormente admitidos passassem a 
- Cargo exclusivamente em comissão pode ser destinado indistintamente a quaisquer atividades
- A lei municipal pode estabelecer condições de provimento, desde que respeite o concurso e 
- A nomeação e o exercício são conceitos equivalentes, razão pela qual os três anos constitu
- A avaliação especial de desempenho por comissão é condição constitucional para adquirir es
- Servidor exclusivamente temporário torna-se estável pelo art. 41 após trabalhar três anos 
- A exoneração pode decorrer de pedido do interessado, ao passo que demissão normalmente des
- A reintegração pode decorrer da invalidação de demissão, com efeitos e consequências deter
- O direito constitucional ao adicional de pelo menos um terço de férias determina que todos
- Para saber se um adicional é incorporável, deve-se verificar sua natureza e as condições p
- Toda licença por interesse particular concedida a servidor estadual é necessariamente remu
- Investido no mandato de vereador e havendo compatibilidade de horários, o servidor pode re
- A mera abertura de processo disciplinar comprova autoria e torna dispensável a defesa.
- A sindicância pode assumir função investigativa, e a possibilidade de sanção demanda as ga
- Prazos e composição de comissão constantes da Lei 8.112 aplicam-se obrigatoriamente a qual
- Afastamento preventivo previsto em lei pode ser cautelar e não representa necessariamente 
- A partir da EC 138/2025, a combinação de um cargo de professor com outro cargo de qualquer
- A Lei 8.112/1990 é o estatuto geral de todos os servidores municipais, mesmo que não exist
- Em questão que exija a legislação de um estado específico, deve-se conferir a versão vigen
```

