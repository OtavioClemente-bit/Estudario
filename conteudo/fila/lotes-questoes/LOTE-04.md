# Pedidos de questões extras, lote 4 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 35265 tópicos de edital.

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

## Pedido 1: Conceito e classificação das constituições (`direito-constitucional.conceito-classificacao-constituicao`, 677 tópicos de edital)

```
Matéria: direito-constitucional.conceito-classificacao-constituicao (Conceito e classificação das constituições)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constituição, Estado, Direito Constitucional e evolução do constitucionalismo": 17
- "2. Concepções, classificações, elementos e estrutura da Constituição": 41
- "3. Princípios fundamentais, supremacia constitucional e Estado Democrático de Direito": 19
- "4. Aplicabilidade das normas e neoconstitucionalismo": 11
- "5. Poder constituinte, reforma, revisão e rigidez da Constituição de 1988": 12

Já existem (não repita):
- Um estudo diferencia o conjunto de habitantes do Estado e o vínculo jurídico-político que 
- Um pesquisador identifica documento inglês de 1215 frequentemente mencionado como antecede
- Ao examinar a primeira Constituição do Brasil independente, uma pessoa encontra monarquia 
- Um arquivo histórico relata uma Assembleia Nacional Constituinte instalada em 1987, após c
- Uma análise sustenta que o texto constitucional somente será efetivo se refletir os fatore
- Em discussão acadêmica, a Constituição é compreendida como decisão política fundamental so
- Uma professora distingue a Constituição jurídico-positiva, situada no ápice do direito pos
- Considerando seu processo de elaboração, sua forma documental e a amplitude das matérias r
- Um estudante afirma que a Constituição de 1988 é flexível porque já foi modificada diversa
- Uma Constituição possui vigência formal, mas é empregada principalmente para legitimar e p
- Uma Constituição estadual não reproduziu a invocação religiosa presente no Preâmbulo da Co
- Uma ação educativa apresenta cinco enunciados e pede identificar um fundamento expresso da
- Uma política pública pretende diminuir disparidades de renda entre diferentes regiões do p
- Uma diretriz diplomática orienta o Brasil a repudiar racismo e terrorismo nas relações int
- A Constituição assegura liberdade de exercício profissional desde sua vigência, mas admite
- Uma disposição constitucional cria obrigação de estruturar instituição pública, mas parte 
- Em um exercício de teoria das normas, procura-se categoria para regra constitucional apta 
- Em debate sobre interpretação, alguém associa pós-positivismo, centralidade dos princípios
- Uma apresentação divide marcos do neoconstitucionalismo em histórico, filosófico e teórico
- Um novo texto constitucional rompe a cadeia de validade da ordem anterior e passa a consti
- Uma proposta de emenda é apresentada por um terço dos membros do Senado Federal, sem subsc
- Uma PEC obtém apoio de três quintos dos membros da Câmara, em apenas uma votação, e três q
- Uma PEC propõe abolir a periodicidade das eleições, mantendo voto direto, secreto e univer
- Um texto afirma que em 1994 se modificou a Constituição segundo rito transitório de sessão
- Uma proposta de emenda foi rejeitada na sessão legislativa atual. Um grupo pretende aprese
- A Constituição brasileira de 1988 foi promulgada em 5 de outubro, ao término dos trabalhos
- O constitucionalismo social, marcado por experiências como México 1917 e Weimar 1919, supr
- A Constituição de 1988 é usualmente classificada, quanto à origem, como promulgada e, quan
- O Preâmbulo da Constituição de 1988 tem a mesma força normativa dos dispositivos da parte 
- O parágrafo único do art. 1º da Constituição de 1988 prevê que o poder emana do povo e pod
- Erradicar a pobreza e reduzir desigualdades sociais e regionais são fundamentos expressos 
- Uma disposição inserida no texto constitucional por sua forma pode ser formalmente constit
- Uma constituição não escrita é necessariamente uma ordem sem documentos legislativos ou de
- A Constituição de 1988 pode ser considerada dirigente porque estabelece objetivos e tarefa
- A Constituição nominal, na classificação ontológica de Loewenstein, é juridicamente inexis
- O art. 2º da Constituição de 1988 afirma que Legislativo, Executivo e Judiciário são poder
- O art. 4º da Constituição limita o dever brasileiro de integração latino-americana aos asp
- Uma norma constitucional que permite a lei impor qualificações ao exercício de profissão p
- Normas constitucionais de eficácia limitada são destituídas de caráter jurídico até a ediç
- O Presidente da República possui iniciativa para propor emenda constitucional pelo art. 60
- A forma republicana de governo aparece textualmente no art. 60, § 4º, I, como cláusula pét
- Durante a vigência de estado de defesa, é constitucionalmente vedada a emenda à Constituiç
- Como o ADCT art. 3º previu revisão após cinco anos por maioria absoluta em sessão unicamer
- Uma PEC regularmente aprovada em dois turnos de cada Casa por três quintos dos respectivos
- A matéria de proposta de emenda rejeitada pode ser reapresentada por nova PEC na mesma ses
```

## Pedido 2: Tocantins: história, geografia e economia (`tocantins.conhecimentos-estaduais`, 786 tópicos de edital)

```
Matéria: tocantins.conhecimentos-estaduais (Tocantins: história, geografia e economia)
Capítulos e quantas questões quero em cada um (total 100):
- "Povos originários, colonização e norte goiano": 17
- "Emancipação, criação do Estado e organização política": 17
- "Território, população e diversidade sociocultural": 17
- "Relevo, clima, vegetação e hidrografia": 17
- "Conservação ambiental e patrimônio histórico-cultural": 16
- "Economia, matriz energética e desenvolvimento regional": 16

Já existem (não repita):
- A Constituição Federal de 1988 criou o Tocantins por desmembramento de parte do território
- O Tocantins integra a Região Centro-Oeste porque a área de que se originou pertencia a Goi
- Miracema do Tocantins exerceu a função de capital provisória durante o primeiro ano de fun
- A pedra fundamental de Palmas foi lançada em 1988, antes da promulgação da Constituição qu
- A mineração aurífera e as bandeiras participaram da formação de antigos núcleos urbanos na
- Os povos indígenas passaram a ocupar o território tocantinense somente após a abertura da 
- As regiões intermediárias Palmas, Araguaína e Gurupi correspondem a uma regionalização geo
- A criação de uma região geográfica imediata pelo IBGE retira automaticamente a autonomia p
- No Censo Demográfico de 2022, a população do Tocantins ultrapassou 1,5 milhão de habitante
- A Ilha do Bananal é uma ilha oceânica situada na faixa costeira do Estado do Tocantins.
- O Cerrado predomina no Tocantins, coexistindo com áreas de transição para formações amazôn
- A inclusão do Tocantins na Amazônia Legal prova que o território estadual está integralmen
- A alternância entre estação chuvosa e estação seca influencia a agricultura, as vazões flu
- O Parque Nacional do Araguaia e o Parque Estadual do Jalapão são unidades de conservação p
- As terras indígenas e as unidades de conservação têm conceitos e fundamentos próprios, mes
- O tombamento federal do conjunto histórico de Natividade é posterior à instalação do Tocan
- As bonecas de cerâmica Karajá, conhecidas como ritxoko, têm relevância como expressão do p
- As usinas de Serra da Mesa e Tucuruí localizam-se no Tocantins pelo simples fato de compor
- Uma cadeia produtiva agropecuária pode ampliar as exportações sem produzir, necessariament
- O termo matriz elétrica identifica todas as atividades produtoras de bens e serviços do To
- Por ser um curso de água situado exclusivamente dentro do Tocantins, o rio Araguaia é admi
- A criação formal do Estado do Tocantins ocorreu por meio de qual instrumento?
- A sede provisória do governo tocantinense em 1989 foi qual cidade?
- A qual macrorregião o Estado do Tocantins pertence, segundo o texto constitucional de sua 
- Qual das associações descreve corretamente a geografia do Tocantins?
- Qual par contém um importante núcleo histórico tocantinense e uma atividade ligada a sua f
- Qual é o número de municípios do Tocantins divulgado pelo IBGE?
- No sistema territorial associado ao rio Araguaia, a Ilha do Bananal encontra-se entre quai
- A expressão “ritxoko”, no patrimônio cultural tocantinense, associa-se a:
- Considere a distinção entre Estado, município e região estatística. Qual alternativa está 
- Na divisão do IBGE de 2017, qual conjunto contém as três regiões geográficas intermediária
- Uma prova afirma que o Tocantins tem 1.511.460 habitantes. Como a informação deve ser inte
- Uma agência divulga visita ao Jalapão. Qual afirmação preserva a distinção territorial e a
- Qual interpretação da relação entre Amazônia Legal e vegetação do Tocantins é correta?
- Um pesquisador deseja estudar bens materiais tombados pelo Iphan em Tocantins. Qual par é 
- Uma barragem localizada em Goiás regula vazões a jusante no rio Tocantins. Qual explicação
- Um edital usa o termo “matriz produtiva tocantinense”. Qual resposta atende melhor ao conc
- Um gestor pretende instalar infraestrutura turística na Ilha do Bananal. Qual precaução de
- Considere um município com aumento de exportação de soja, mas piora de saneamento e concen
- Em questão sobre a criação do Tocantins, qual enunciado articula adequadamente passado e m
- O rio Tocantins abriga reservatórios em várias unidades federativas. Assinale a associação
- A propósito das categorias de proteção, qual alternativa apresenta uma análise juridicamen
```

## Pedido 3: Processo de execução de título extrajudicial (`processo-civil.execucao`, 783 tópicos de edital)

```
Matéria: processo-civil.execucao (Processo de execução de título extrajudicial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Títulos, requisitos, competência e responsabilidade patrimonial": 25
- "2. Espécies: entrega de coisa, fazer e não fazer": 18
- "3. Quantia certa: citação, arresto, penhora, avaliação e impenhorabilidade": 15
- "4. Expropriação, remição e proteção do terceiro": 17
- "5. Embargos, parcelamento, suspensão, prescrição intercorrente e extinção": 25

Já existem (não repita):
- Um credor apresenta instrumento particular em papel, assinado pelo devedor e por duas test
- Um contrato eletrônico de dívida foi assinado segundo modalidade admitida em lei e teve su
- O credor executa título extrajudicial que obriga a entregar veículo individualizado. Qual 
- Uma execução de título extrajudicial cobra dívida em dinheiro e a citação foi regularmente
- Ao despachar execução por quantia, foram fixados honorários iniciais de 10%. O devedor pag
- Em diligência executiva, o oficial não encontra o executado no endereço indicado, mas iden
- Um executado deseja apresentar embargos contra título extrajudicial, mas ainda não houve p
- O imóvel de pessoa que não integra a execução é penhorado em processo alheio, embora ela a
- O título extrajudicial prevê prestação de fazer, mas não fixa prazo para cumpri-la. Como s
- Uma obrigação extrajudicial de instalar equipamentos pode ser realizada por qualquer prest
- Contrato exige entrega de mercadorias definidas por gênero e quantidade e atribui a escolh
- Uma obrigação de não fazer foi violada por obra cuja remoção material ainda é possível. Qu
- Em execução extrajudicial por quantia, o exequente pede substituição da penhora por seguro
- Um bem foi avaliado em R$ 200 mil, e o edital de leilão fixou mínimo judicial de R$ 130 mi
- Antes de adjudicação ou alienação de bem penhorado, o devedor deseja impedir expropriação 
- O executado pretende discutir excesso e inexigibilidade em execução extrajudicial. Qual é,
- O executado propõe embargos tempestivos e pede suspensão, mas não há penhora, caução ou de
- Durante prazo para embargos, o executado reconhece a dívida e pretende requerer parcelamen
- Um credor pediu reconhecimento de fraude à execução envolvendo bem adquirido por terceiro.
- Pessoa possui imóvel por compromisso de compra e venda ainda não registrado e sofre penhor
- O bem de terceiro foi arrematado e ele pensa propor embargos três dias depois, antes de se
- Na execução extrajudicial por quantia contra a Fazenda Pública, a parte exequente afirma q
- Na execução de prestação alimentícia prevista em título extrajudicial, como se dá a citaçã
- Após tentativas de localização frustradas, foi constatado que não há devedor nem bens enco
- A execução está sem bens penhoráveis, e o juízo determina suspensão nos termos do art. 921
- Um título extrajudicial indica prestação certa e vencida e permite atualizar seu valor por
- Na execução de título extrajudicial por quantia certa, o executado dispõe de quinze dias c
- Em obrigação de entrega de coisa certa por título extrajudicial, a ausência de entrega de 
- Em execução de fazer fundada em título extrajudicial, quando o título não fixa prazo, o ex
- A penhora de dinheiro ocupa prioridade na ordem do art. 835, sem impedir que as demais esp
- A apresentação de embargos à execução depende de penhora prévia suficiente, pois o art. 91
- Quem não integra a execução e sofre ameaça de constrição sobre bem de sua posse pode utili
- O art. 792 considera a alienação praticada em fraude à execução nula para todos os efeitos
- Na execução de coisa incerta, se a escolha da prestação individualizada cabe ao exequente,
- Ao constatar obrigação de fazer fungível não cumprida, o juízo está impedido de autorizar 
- A proteção legal da caderneta de poupança até quarenta salários-mínimos está expressa no a
- O art. 891 considera vil qualquer lance inferior a metade da avaliação, ainda que o juiz t
- O devedor que reconhece o crédito e pede parcelamento do art. 916, dentro do prazo de emba
- No parcelamento deferido pelo art. 916, o inadimplemento de uma parcela autoriza apenas co
- A Súmula 84 admite embargos de terceiro baseados na posse oriunda de compromisso de compra
- A Súmula 393 foi redigida para toda execução civil comum e autoriza objeção de pré-executi
- O prazo geral do art. 915 para embargos à execução não recebe a contagem em dobro do art. 
- A prescrição intercorrente do art. 921, § 4º, inicia obrigatoriamente apenas depois de fin
- O reconhecimento de ofício de prescrição no curso do processo exige ouvir as partes no pra
- A falta de bens penhoráveis durante a execução extingue o processo de imediato, independen
```

## Pedido 4: Políticas públicas: conceitos, ciclo, modelos e avaliação (`administracao-publica.politicas-publicas`, 776 tópicos de edital)

```
Matéria: administracao-publica.politicas-publicas (Políticas públicas: conceitos, ciclo, modelos e avaliação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, definições e tipologias das políticas públicas": 15
- "2. Ciclo, agenda, atores e modelos explicativos da decisão": 21
- "3. Implementação, federalismo, participação e políticas inclusivas": 25
- "4. Avaliação, indicadores, causalidade e evidências": 23
- "5. Instituições, corrupção, coordenação e qualidade das políticas": 16

Já existem (não repita):
- Uma secretaria apresenta o programa de alimentação escolar, enquanto grupos de interesse d
- Uma iniciativa municipal financia pequenos equipamentos comunitários em bairros específico
- Uma cidade detectou aumento da evasão escolar, mas o assunto ainda não recebeu prioridade 
- Durante atendimento em unidade de serviço público, uma agente aplica critérios de priorida
- Uma equipe acompanha mensalmente número de visitas realizadas e percentual de metas operac
- Antes de lançar um novo benefício, o governo examina causas do problema, público-alvo, alt
- Um relatório de alfabetização apresenta recursos para professores, oficinas realizadas, es
- Dois municípios combinam oferecer serviço público conjuntamente, cada qual preservando per
- Um governo muda alíquotas sobre rendimentos elevados e direciona arrecadação adicional a t
- Uma secretaria tem dados incompletos e prazo curto para decidir entre muitas opções. Exami
- Uma proposta tecnicamente amadurecida aguardava oportunidade política. Após desastre que t
- Uma política nacional fixa metas, mas equipes locais adaptam atendimento a bairros rurais,
- Uma pesquisa estima que a renda dos participantes subiu após o programa. Contudo, também h
- Em uma conferência de saúde, participantes debatem necessidades locais e propõem diretrize
- Um plano de saúde regional registra que conselhos acompanharão políticas enquanto serviços
- Um programa disponibilizou 600 atendimentos com orçamento menor que outro programa que ofe
- Durante anos, uma política regulatória mudou pouco; após mudança súbita de atenção pública
- Duas coalizões discutem política ambiental durante uma década. Cada uma articula pesquisad
- Um programa piloto oferece benefício apenas a municípios com índice de vulnerabilidade sup
- Uma avaliação compara tratados e não tratados antes e depois de programa social. Antes, as
- Um plano educacional de 2026 afirma que ainda não existe Sistema Nacional de Educação inst
- Em programa descentralizado, a União transfere atribuições sem apoio técnico, enquanto mun
- Uma administração digitaliza seleção de beneficiários, mas o cadastro ignora moradores sem
- Uma política reproduz há décadas um modelo de atendimento porque a infraestrutura instalad
- Uma secretaria pretende identificar influência causal de capacitação por meio de sorteio e
- Ao elaborar plano de enfrentamento da insegurança alimentar, uma equipe trata a identifica
- Ao estudar uma intervenção que fixa padrões de emissão de poluentes para empresas, uma equ
- Um programa em execução apresenta relatórios periódicos de consumo de recursos e de entreg
- Uma prefeitura dividiu seu departamento de atendimento em duas unidades internas da própri
- Uma política de acolhimento social incorpora representantes da população para debater prio
- Um programa que ultrapassou a meta de 2.000 pessoas cadastradas comprovou por isso sua efe
- Uma proposta de política pública foi examinada antes da implantação quanto à necessidade, 
- Em 2026, uma equipe de educação registrou que o Sistema Nacional de Educação ainda não hav
- Um serviço da assistência social é coordenado de forma compartilhada por entes federativos
- Uma instituição substitui análise de impacto por relatório de processo que mostra entregas
- Durante a discussão sobre regulação de serviços, organizações distintas compartilham crenç
- Em município com carência de pessoal, a transferência de responsabilidades por política na
- Uma avaliação de política de geração de renda aplica diferenças em diferenças com grupos d
- Um programa inspirado na teoria de Kingdon foi explicado como resultado de pequenas altera
- Uma intervenção foi avaliada por regressão descontínua em torno de limite de elegibilidade
- Uma decisão antiga sobre localização de equipamentos públicos criou contratos, competência
- Um sorteio inicial distribuiu elegíveis entre tratamento e controle, mas a pesquisa avalio
- Em programa que atende majoritariamente pessoas com acesso à internet, pesquisadores recom
- Uma equipe afirmou que o Sistema Nacional de Educação criado pela LC nº 220/2025 eliminou 
- Uma política reduziu seu custo por atendimento, mas passou a excluir moradores de áreas re
```

## Pedido 5: Lei n. 4.320/1964 (`orcamento-publico.320-1964`, 775 tópicos de edital)

```
Matéria: orcamento-publico.320-1964 (Lei n. 4.320/1964)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei de Orçamento, princípios e elaboração da proposta": 15
- "2. Receita pública, exercício financeiro e dívida flutuante e fundada": 19
- "3. Despesa: classificação, empenho, liquidação, pagamento e adiantamento": 21
- "4. Créditos adicionais, fontes de abertura e alterações orçamentárias": 20
- "5. Programação, fundos especiais, controle e entidades vinculadas": 25

Já existem (não repita):
- Ao elaborar a LOA, uma unidade quer evidenciar o programa anual de trabalho e os critérios
- Um ente receberá R$ 900 mil e transferirá R$ 150 mil a outro ente. Para aplicar a regra do
- No envio da proposta da LOA, o Executivo prepara documentos exigidos pelo art. 22. Qual co
- Uma receita foi lançada em dezembro e efetivamente arrecadada em janeiro; uma despesa foi 
- Uma administração compromete dotação para adquirir equipamentos cuja entrega ocorrerá mais
- Após a entrega de materiais, a administração confronta nota de empenho, contrato e comprov
- Uma dotação existente ficou insuficiente; outra atividade nova não possui dotação específi
- Uma lei vincula receitas especificadas a um serviço permanente e determina acompanhamento 
- Para abrir crédito especial com justificativa, foi apurada fonte disponível e não comprome
- Uma proposta precisa contemplar receitas e despesas de capital em quadro com projeção cont
- Uma unidade contrata manutenção por valor total conhecido de R$ 120 mil, dividido em doze 
- No encerramento de dezembro, um contrato havia sido empenhado e regularmente liquidado, ma
- O setor financeiro apresenta lista de depósitos recebidos, débitos de tesouraria, serviços
- Após a promulgação da LOA, uma unidade precisa saber quanto de sua dotação poderá utilizar
- Uma unidade transfere autorização orçamentária a outra do mesmo órgão; em operação distint
- A avaliação da execução verifica legalidade dos atos, responsabilidade dos agentes por val
- Uma administração apurou excesso de arrecadação de R$ 180 mil segundo as diferenças acumul
- O balanço anterior mostra ativo financeiro de R$ 820 mil e passivo financeiro de R$ 590 mi
- Um crédito especial foi autorizado em novembro e terminou o ano com saldo não utilizado de
- Um fundo especial fechou o exercício com saldo positivo de R$ 75 mil. A lei instituidora n
- Um empenho foi anulado no mesmo exercício financeiro em que ocorreu; outro foi cancelado d
- Uma proposta orçamentária não chegou ao Legislativo até o prazo previsto na norma constitu
- A respeito dos arts. 107 a 110 da Lei nº 4.320/1964, uma entidade autárquica apresenta orç
- Um servidor solicita novo adiantamento para despesas excepcionais, mas está responsável po
- Uma despesa teve sua legalidade examinada antes da contratação, durante o fornecimento e a
- Ao formular a LOA, o ente deve observar unidade, universalidade e anualidade, princípios e
- Uma operação de crédito por antecipação de receita orçamentária deve ser incluída como rec
- Uma despesa legalmente empenhada em dezembro integra o exercício desse empenho, ainda que 
- O estágio de liquidação consiste em pagar a obrigação e transferir recursos ao credor; o e
- Quando uma dotação da LOA se mostra insuficiente, a espécie adequada de crédito adicional 
- Fundo especial instituído por lei pode aplicar suas receitas vinculadas sem dotação na LOA
- Uma obrigação inscrita como dívida ativa tributária é crédito da Fazenda Pública contra de
- A dívida fundada definida no art. 98 reúne somente compromissos exigíveis dentro de doze m
- Na elaboração da estimativa de receitas, o art. 30 considera demonstrações de arrecadação 
- Para consumo de energia cuja fatura anual total não pode ser determinada previamente, o ar
- A lei admite, em hipóteses especiais previstas na legislação específica, dispensar emissão
- Pelo art. 43, o superávit financeiro que serve de fonte aos créditos adicionais é apurado 
- O art. 75 permite que o controle da execução alcance o cumprimento físico do programa de t
- Quando duas unidades do mesmo órgão descentralizam crédito orçamentário entre si, a movime
- Crédito extraordinário destinado a emergência imprevisível exige observar tanto o art. 44 
- Uma dotação adicional suplementar autorizada no último quadrimestre pode ser reaberta no e
- A cota trimestral para uso da dotação, nos arts. 47 a 50, e a programação financeira mensa
- Um saldo positivo de fundo especial apurado em balanço é transferido ao exercício seguinte
- No art. 107, a regra de aprovação por decreto dos orçamentos das entidades ali referidas a
- O art. 114 da Lei nº 4.320/1964 fixa 1º de janeiro de 1964 para a elaboração orçamentária 
```

## Pedido 6: Sequências lógicas (`matematica.sequencias-logicas`, 775 tópicos de edital)

```
Matéria: matematica.sequencias-logicas (Sequências lógicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Padrões numéricos e leis de formação": 22
- "2. Progressões aritméticas e geométricas nas sequências": 21
- "3. Fibonacci e recorrências clássicas": 21
- "4. Letras, palavras, símbolos e padrões alternados": 18
- "5. Sequências figurais, posições e raciocínio crítico": 18

Já existem (não repita):
- Um painel exibe a quantidade de registros nos cinco primeiros ciclos como 5, 9, 13, 17, __
- Uma sequência de etapas apresenta 2, 6, 18, 54 e um termo faltante. Se a operação é mantid
- Cinco figuras são formadas com pontos em arranjos quadrados. As quatro primeiras têm 1, 4,
- Uma organização em camadas registra 1, 3, 6, 10, 15 elementos nas primeiras etapas. Qual é
- A quantidade produzida em cinco fases obedece à regra 'somar 3 ao termo anterior' e começa
- Uma PA registra inicialmente 7 unidades e acrescenta 4 unidades em cada nova posição. Qual
- Uma PG é apresentada por 3, 9, 27, __. Qual valor mantém a mesma razão multiplicativa entr
- Uma lista de números segue a regra de somar os dois valores imediatamente anteriores: 1, 1
- Letras são dispostas segundo a ordem A, D, G, J, __, usando o alfabeto de 26 letras. Qual 
- Quadrados unitários são dispostos em fila, compartilhando um lado com o próximo. As primei
- Uma medição segue uma PA com primeiro termo 4 e razão 3. Qual é o décimo segundo termo, co
- Uma cultura experimental tem medidas 2, 6, 18 e assim sucessivamente, com razão geométrica
- A quantidade diária registrada durante cinco dias forma a PA 2, 5, 8, 11, 14. Qual é a som
- Uma recorrência começa por 2 e 1 e, a partir daí, cada termo é a soma dos dois anteriores:
- Uma sequência de letras apresenta A, C, F, J, O, __. Considerando posições no alfabeto atu
- Uma sequência começa no número 3 e segue a regra: 'o novo termo é duas vezes o anterior ma
- Numa tabela, as posições ímpares são 2, 4, 6, 8 e as pares são 9, 18, 27, __. A apresentaç
- Quatro símbolos se repetem na ordem X, Y, Z, W, sem interrupção. Se a primeira posição rec
- Um mosaico acrescenta molduras de modo que a quantidade de pontos nas quatro etapas é 1, 9
- A série de registros é 3, 8, 15, 24, 35, __. As diferenças crescem de dois em dois. Qual n
- Uma lei de formação é dada pela expressão $$a_n=n(n+1)$$ para posições inteiras positivas.
- Uma sequência geométrica inicia por 81, 27, 9, 3, 1, ... e preserva a razão de um terço. Q
- Um equipamento alterna as instruções 'multiplicar por 2' e 'somar 2'. O histórico começa e
- Uma progressão geométrica finita registra 2, 6, 18, 54, 162 unidades. Qual é a soma das ci
- Uma montagem de quadrados unitários justapostos usa 4 palitos para o primeiro quadrado e m
- Na sequência de registros 4, 7, 10 e 13, o padrão de incremento constante de três unidades
- Uma lista apresenta 1, 4, 9 e 16 elementos, e a pessoa a classifica como progressão aritmé
- Uma PA começa por 5 e acrescenta 2 unidades a cada posição; seus quatro primeiros termos s
- Na sequência 3, 9, 27, 81, a passagem entre todos os termos é feita somando 6; logo, trata
- Numa sequência Fibonacci que começa por 1 e 1, após os termos 1, 1, 2, 3 e 5, o próximo va
- Ao calcular as posições de letras em sequências do alfabeto português atual, o estudante d
- Num padrão de números triangulares 1, 3, 6, 10 e 15, a próxima posição contém 21 elementos
- Uma PA tem primeiro termo 2 e razão 3. Seu décimo termo é 32, pois basta multiplicar 3 por
- Uma progressão geométrica finita apresenta os termos 2, 4, 8 e 16. A soma desses quatro te
- A regra 'cada termo é a soma dos dois anteriores', sem nenhum valor inicial fornecido, det
- Em sequência de letras A, C, F, J, os índices aumentam por 2, depois 3 e depois 4; mantend
- A série 1, 10, 2, 20, 3, 30 intercala duas leis. Como 30 é o sexto termo, a posição seguin
- Um painel repete A, B, C indefinidamente e começa com A na primeira posição. A trigésima p
- Numa repetição periódica A, B, C, D, A, B, C, D, ... , a posição 28 corresponde a A por co
- Ao montar sete quadrados de palitos em uma fila, cada quadrado novo compartilha um lado co
- Cinco quadrados de lados unitários justapostos em fila demandam 16 palitos distintos e, po
- A lista 2, 6, 12, 20, 30 não tem primeiras diferenças constantes, mas estas são 4, 6, 8 e 
- Na convenção de Fibonacci que começa com os termos 0 e 1 ocupando as posições 1 e 2, a sét
- Uma figura combina ciclo de quatro orientações distintas com um ciclo independente de três
- Observar apenas quatro termos 1, 2, 3, 4 demonstra matematicamente que a única sequência p
```

## Pedido 7: Planejamento, gestão e fiscalização de contratos públicos (`direito-administrativo.planejamento-gestao-fiscalizacao-contratos`, 770 tópicos de edital)

```
Matéria: direito-administrativo.planejamento-gestao-fiscalizacao-contratos (Planejamento, gestão e fiscalização de contratos públicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento, estudo técnico preliminar e termo de referência": 16
- "2. Pesquisa de preços, parcelamento e prevenção do fracionamento": 13
- "3. Gestão do contrato, fiscais e preposto da contratada": 28
- "4. Execução, medição de resultados e tratamento de irregularidades": 29
- "5. Contratações de TIC, governança e sistema de registro de preços": 14

Já existem (não repita):
- Uma unidade percebe aumento recorrente de falhas na conservação de instalações e precisa d
- Ao redigir um ETP, a equipe pretende aplicar o núcleo mínimo de elementos exigido pelo art
- Uma contratação de limpeza já teve a solução selecionada no ETP, mas faltam padrões de qua
- Durante a execução de obra contratada, a Administração precisa de interlocutor formal que 
- Um fiscal constata em vistoria atraso e defeitos na entrega de bens, com possibilidade de 
- Em contrato de serviços, a unidade precisa verificar conformidade da qualidade e do prazo 
- Uma unidade subdivide uma demanda previsível de equipamentos do mesmo ramo em contratações
- O contrato estabelece indicador objetivo de disponibilidade do serviço e uma faixa de redu
- Após a publicação de ata de registro de preços, o fornecedor afirma que a unidade precisa 
- Em contratação de serviços sob a Lei nº 14.133/2021, a equipe consulta procedimentos da IN
- Uma aquisição reúne itens independentes e fornecedores especializados distintos. O estudo 
- Para estimar o preço de equipamento padronizado, restaram três valores comparáveis e válid
- Ao revisar planejamento anual e controles de integridade das contratações em seu âmbito, a
- Uma pesquisa de preços para serviço comum localiza apenas duas observações válidas após co
- A Administração contratou consultoria auxiliar para fornecer laudos de medição dos serviço
- No contrato de solução de TIC submetido à IN SGD/ME nº 94/2022, pretende-se designar o mes
- Em contrato de TIC, surge vulnerabilidade relevante seis meses após a última análise de ri
- Uma equipe de fiscalização recebe serviço de reforma finalizado, mas ainda pendente de ver
- Em compra de materiais, o responsável registra entrada física das caixas, ainda sem confer
- Uma prestação mensal ficou abaixo da meta estabelecida no IMR. O ajuste do pagamento está 
- Uma unidade não participante deseja aderir a ata de registro de preços com quantitativo or
- Após obter aceite do fornecedor e autorização de adesão a uma ata federal, um não particip
- Uma ata de registro de preços teve divulgação no PNCP numa quinta-feira útil. A equipe ana
- Em contratação de TIC, a mesma empresa se dispõe a fornecer a solução e a prestar o serviç
- A unidade gestora fez, no mesmo exercício, várias aquisições previsíveis do mesmo ramo, em
- Ao planejar aquisição de equipamentos, a equipe incluiu no ETP descrição da necessidade, q
- O ETP de contratação de vigilância pode substituir o termo de referência caso apresente o 
- Em contrato de obra, a presença permanente do fiscal transfere para a Administração a resp
- Durante um serviço de manutenção, a contratada apresenta preposto aceito pela Administraçã
- Em pesquisa de preços válida para materiais padronizados, a equipe utiliza três observaçõe
- Como a unidade deseja ampliar a competição, deve dividir toda compra em lotes, ainda que o
- Em contratação de manutenção, o fiscal detectou atrasos repetidos, anotou datas e cláusula
- No procedimento de pesquisa de preços regido pela IN nº 65/2021, obter duas observações ap
- Em contrato de serviços de execução indireta sob a Lei nº 14.133/2021, a equipe consulta a
- No acompanhamento de contrato com indicadores de resultado, a falta de atingimento da meta
- Em compra de materiais, o recebimento provisório pode ocorrer de forma sumária, com verifi
- Segundo a IN SGD/ME nº 94/2022, é lícito que o mesmo servidor atue como gestor e fiscal ad
- Se um fator novo ameaçar o cronograma de contrato de TIC, a equipe deve reavaliar o mapa d
- Na contagem do prazo de uma ata de registro de preços federal, a equipe adota como termo i
- Ao planejar adesão a uma ata, o interessado demonstra vantajosidade, compara preços com o 
- Após divulgar uma ata de registro de preços, o gerenciador decide aumentar em trinta por c
- Como a empresa fornecedora de solução de TIC conhece melhor o produto, a IN SGD/ME nº 94/2
- Duas unidades registram simultaneamente ocorrência de execução insatisfatória de contrato 
- Na contratação de serviços sob a lei atual, a equipe usa a IN nº 5/2017 apenas onde compat
- Uma contratação sujeita à regra geral de dispensa por valor pode ignorar outras aquisições
```

## Pedido 8: Pessoas jurídicas (`direito-civil.pessoas-juridicas`, 769 tópicos de edital)

```
Matéria: direito-civil.pessoas-juridicas (Pessoas jurídicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, classificação e entes sem personalidade": 19
- "2. Constituição, registro, capacidade, domicílio e responsabilidade": 23
- "3. Associações, sociedades e fundações privadas": 25
- "4. Desconsideração da personalidade e regimes especiais": 17
- "5. Extinção, autonomia e aplicações contemporâneas": 16

Já existem (não repita):
- Uma entidade privada aprova seu estatuto em assembleia, mas ainda não o inscreveu no regis
- Um instituto destinado a atividades culturais é organizado pela reunião de associados, sem
- Uma administração municipal contrata uma associação civil registrada. Como se classificam 
- Uma associação reúne associados para decidir a exclusão de integrante acusado de grave inf
- Uma pessoa pretende instituir fundação privada com bens livres para financiar pesquisas. Q
- Uma associação registrada mantém três estabelecimentos e contrata serviços de conservação 
- Depois de decidir encerrar as atividades, uma pessoa jurídica ainda precisa cobrar crédito
- Uma associação deseja substituir dirigentes e alterar seu estatuto. Segundo o art. 59 do C
- Sócios exploram atividade econômica sem inscrever o contrato social. Na ausência de regra 
- Integrantes de associação inscritos em categorias distintas questionam vantagens estatutár
- Um credor empresarial pede alcançar bens de sócio apenas porque a sociedade deixou de paga
- O administrador de sociedade paga reiteradamente prestações pessoais com recursos sociais 
- Um instituidor destina por testamento bens a uma fundação educacional, mas o patrimônio é 
- O estatuto de fundação não foi elaborado e o instituidor não fixou prazo. Decorrido que pe
- Um devedor esvazia seu patrimônio pessoal e o oculta na sociedade que controla para frustr
- Consumidor lesado não obtém ressarcimento porque a personalidade do fornecedor constitui o
- Uma associação dissolvida possui patrimônio líquido remanescente, e seu estatuto nada diz 
- Fundação pretende modificar seu estatuto por votação de dirigentes. Qual conjunto de requi
- Duas sociedades pertencem ao mesmo grupo econômico, mas mantêm contas, administração e ben
- Um credor afirma que empresa mudou licitamente sua atividade e, só por isso, deve ser desc
- Uma fundação atua em dois estados, mantendo programas locais em ambos. Quem exerce a funçã
- Ao registrar estatuto de associação, o apresentante possui documento digital assinado pelo
- Credores ambientais pedem desconsideração de empresa cuja personalidade impede reparação d
- Um participante de sociedade em comum exige que terceiro prove, por escrito, a existência 
- Uma entidade afirma que seu software autônomo detém personalidade jurídica própria porque 
- Um grupo de vizinhos registrou associação para fins culturais. A existência da associação 
- Uma fundação privada que desenvolve projetos educacionais deve ser classificada como pesso
- Uma entidade religiosa pretende registrar seu ato constitutivo; o Código Civil assegura li
- Uma associação transfere quota patrimonial pertencente a associado; em regra, o adquirente
- A associação com receitas de eventos sociais passa obrigatoriamente à condição de sociedad
- Sociedade que não inscreveu seus atos constitutivos pode ser regulada como sociedade em co
- Na administração coletiva da pessoa jurídica, a regra subsidiária é decidir por maioria do
- A pessoa jurídica de direito privado tem existência legal desde a mera emissão do CNPJ, ai
- O art. 48-A admite assembleia geral eletrônica de associação, desde que os participantes m
- Se faltar a administração de associação registrada, qualquer interessado pode nomear admin
- A mera existência de grupo econômico basta para desconsiderar as personalidades jurídicas 
- Em caso de confusão patrimonial, transferências relevantes de ativos sem contraprestação e
- Na fundação privada, alterar o estatuto exige deliberação por dois terços dos competentes 
- Sob o art. 50 do Código Civil, basta a sociedade alterar licitamente sua atividade econômi
- Por força do art. 51 do Código Civil, após dissolução, a pessoa jurídica conserva existênc
- O Código Civil permite que o patrimônio líquido remanescente de associação dissolvida seja
- O Código de Defesa do Consumidor contém hipótese de desconsideração quando a personalidade
- O Código Civil atribui personalidade jurídica à sociedade em conta de participação quando 
- A ausência de previsão de bens livres específicos na criação de fundação privada pode ser 
- Embora o art. 133 do CPC discipline o incidente de desconsideração, os pressupostos materi
```

## Pedido 9: Contratos em geral (`direito-civil.contratos`, 760 tópicos de edital)

```
Matéria: direito-civil.contratos (Contratos em geral)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios, interpretação e classificação dos contratos": 30
- "2. Formação, tratativas, contratos com terceiros e atos unilaterais": 15
- "3. Contratos preliminares, aleatórios e arras": 15
- "4. Garantias: vícios redibitórios e evicção": 16
- "5. Extinção, onerosidade e contratos empresariais e públicos": 24

Já existem (não repita):
- Duas empresas celebram acordo de fornecimento, e uma parte sustenta que liberdade contratu
- Um contratante oculta informação decisiva de que tinha conhecimento para induzir a outra p
- Contrato de adesão contém duas interpretações razoáveis de cláusula previamente redigida p
- Em contrato empresarial negociado por sociedades com capacidade equivalente, uma cláusula 
- Uma parte assume expressamente o risco de que determinada quantidade de coisa futura talve
- Em reunião presencial, uma empresa apresenta oferta completa sem fixar prazo, mas a outra 
- Uma proposta escrita fixa preço de R$ 80 mil. O destinatário responde que aceita, desde qu
- Duas pessoas assinam contrato preliminar com todos os requisitos essenciais de contrato de
- Num contrato em favor de pessoa não participante da negociação, a parte estipulante quer e
- Ao concluir contrato, a compradora reserva-se faculdade de indicar terceiro que assumirá d
- Uma parte entrega sinal confirmatório de R$ 15 mil. Sem direito de arrependimento pactuado
- Contrato prevê expressamente direito de arrependimento de qualquer parte mediante arras de
- Um adquirente aceita pagar por toda possibilidade de colheita futura, assumindo expressame
- Em contrato aleatório, o comprador aceita variação da quantidade de bem futuro, mas não ri
- Uma máquina adquirida por contrato comutativo contém falha interna grave anterior à entreg
- Em venda civil entre particulares, um bem móvel é entregue com defeito oculto relevante já
- Uma pessoa compra imóvel onerosamente e depois perde sua propriedade porque terceiro demon
- Um alienante inseriu cláusula expressa de exclusão da garantia de evicção, mas o comprador
- Um equipamento móvel, entregue há 80 dias, apresenta defeito interno que, por sua natureza
- Uma coisa móvel possui garantia contratual vigente. O comprador identifica defeito oculto 
- Contrato de prestação continuada admite resilição unilateral, e um dos contratantes realiz
- Em contrato civil de execução diferida, acontecimento extraordinário e imprevisível torna 
- A parte onerada pede resolução nos termos do art. 478. A contraparte deseja conservar o co
- Duas empresas de porte semelhante repartem por cláusula expressa o risco de oscilação ordi
- Um particular divulga publicamente recompensa pela localização de documento, e terceiro sa
- Duas empresas celebram pacto civil paritário, mas uma alega que a função social do art. 42
- Um fornecedor apresenta contrato de adesão com cláusula contraditória, e sustenta que se d
- Um destinatário responde a proposta contratual alterando o prazo de entrega originalmente 
- Duas partes assinam contrato preliminar sem cláusula de arrependimento e contendo todos os
- Uma compradora entrega sinal confirmatório e depois inadimple sem justificativa. O credor 
- Em contrato bilateral, uma parte exige que a outra cumpra prestação exigível, embora ela p
- Duas sociedades mercantis com semelhante capacidade negociadora podem fixar critérios obje
- Uma empresa interrompe discussões comerciais em estágio preliminar, causando frustração ao
- Na celebração do contrato com pessoa a declarar, uma parte reserva a indicação de novo tit
- Um comprador assume apenas o risco de que determinada coisa futura exista em quantidade in
- Um bem foi recebido por contrato comutativo, contendo defeito oculto relevante já existent
- Uma pessoa adquire coisa em contrato oneroso e perde a titularidade por direito anterior d
- O art. 478 do CC permite ao devedor de contrato de execução continuada ou diferida requere
- As partes estipularam direito de arrependimento e arras penitenciais de R$ 12 mil. Se a pa
- Em contrato de móvel entre particulares, defeito oculto só poderia ser percebido após func
- Uma cláusula expressa afastou responsabilidade por evicção, mas o adquirente não tinha ciê
- Uma contratação entre pessoas privadas admite denúncia unilateral, e uma parte fez investi
- Um contrato empresarial expressamente atribuiu ao vendedor o risco de variações ordinárias
- Um particular intervém sem autorização para impedir dano iminente a bem alheio, segundo in
- Se alguém se enriquece sem justa causa à custa de outrem, cabe obrigatoriamente ação de en
```

## Pedido 10: Advocacia Pública, Advocacia e Defensoria Pública (`direito-constitucional.advocacia-defensoria`, 758 tópicos de edital)

```
Matéria: direito-constitucional.advocacia-defensoria (Advocacia Pública, Advocacia e Defensoria Pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Funções essenciais à justiça e arquitetura constitucional": 12
- "2. Advocacia-Geral da União: representação, organização e consultoria": 24
- "3. Procuradorias estaduais, distritais e peculiaridade municipal": 21
- "4. Advocacia privada e Defensoria Pública: missão, garantias e autonomia": 29
- "5. Pareceres, responsabilidade e situações avançadas": 14

Já existem (não repita):
- Em ação de cobrança ajuizada em nome da própria União, qual função institucional melhor co
- Um setor solicita avaliação jurídica de minuta de ato do Executivo federal antes da assina
- Após uma reunião, foi afirmado que a indispensabilidade do advogado significa proteção ili
- Uma comunidade vulnerável busca orientação gratuita sobre seus direitos antes de existir p
- Ao selecionar candidato para chefiar a AGU, quais são os critérios constitucionais aplicáv
- Uma pessoa ingressará na classe inicial de carreira efetiva da AGU. De acordo com o art. 1
- Em concurso para procurador de Estado, a comissão organizadora consulta as exigências espe
- Um procurador estadual completa três anos de efetivo exercício. Qual condição adicional co
- Ao consultar a LC nº 80/1994, uma defensora explica os princípios institucionais de sua in
- Uma defensoria estadual solicita reconhecimento da autonomia administrativa e da iniciativ
- Uma autarquia federal participa de ação judicial relativa ao seu patrimônio. Segundo a dis
- Foi instaurada execução fiscal de dívida ativa tributária da própria União. Qual órgão rec
- Um parecer do Advogado-Geral foi aprovado pela Presidência, mas não publicado. Qual é seu 
- Ao classificar as unidades da AGU conforme o art. 2º da LC nº 73/1993, qual estrutura inte
- Uma pessoa jurídica sem condições de custear defesa procura a Defensoria. Qual dispositivo
- Um procurador municipal sustenta que o texto do art. 132 da Constituição exige, por si só,
- Durante demanda relacionada a direitos fundamentais, a Defensoria pretende tentar concilia
- Um defensor público pretende patrocinar causa privada remunerada fora das atribuições inst
- Para harmonizar a autonomia da Defensoria com o orçamento público, como deve ser lido o ar
- Um parecer da Consultoria-Geral da União foi aprovado pelo Advogado-Geral e submetido à Pr
- Um gestor alegou que o parecer jurídico prévio favorável lhe transferiu todas as responsab
- A análise jurídica de uma contratação não foi conclusiva por existir controvérsia interpre
- Uma unidade identifica o Conselho Superior e a Corregedoria-Geral da AGU e lhes atribui, r
- Um membro da AGU identifica risco em orientar processo no qual antes atuou como advogado d
- Uma lei estadual pretende disciplinar a Defensoria como unidade administrativa sem autonom
- Em processo ajuizado em nome da União, a atuação de sua advocacia pública decorre do art. 
- Por força do art. 131, § 1º, o cidadão indicado para Advogado-Geral da União deve comprova
- Uma procuradora do Distrito Federal integra carreira à qual o art. 132 atribui representaç
- Em orientação preventiva a uma pessoa necessitada, a Defensoria Pública deve recusar atend
- O texto constitucional assegura às Defensorias Públicas estaduais autonomia funcional e ad
- Na execução de dívida ativa tributária da União, o art. 131, § 3º, confere à Procuradoria-
- Diante de cobrança judicial dirigida contra uma fundação pública federal, a distinção entr
- Um parecer do Advogado-Geral da União aprovado e publicado com despacho presidencial vincu
- No concurso para carreira de procurador estadual, a participação da OAB em todas as fases 
- A estabilidade prevista no parágrafo único do art. 132 é automaticamente adquirida com doi
- A Defensoria pode promover a defesa de direitos coletivos de pessoas necessitadas, sem que
- A competência constitucional dos procuradores estaduais prevista no art. 132 permite concl
- O art. 134, § 3º, estende às Defensorias Públicas da União e do Distrito Federal o regime 
- Por constituir instituição essencial à justiça, a Defensoria Pública da União assume repre
- A competência do Advogado-Geral da União para editar enunciados de súmula administrativa e
- No procedimento de contratação, a manifestação jurídica prévia do art. 53 da Lei nº 14.133
- Ao interpretar o art. 135, é necessário observar que sua referência às carreiras das seçõe
- A atuação profissional de advogado privado recebe do art. 133 inviolabilidade por qualquer
- A responsabilidade pessoal prevista no art. 28 da LINDB exige exame de dolo ou erro grosse
- O art. 134, § 4º, consagra como princípios institucionais da Defensoria a unidade, a indiv
```

## Pedido 11: NBC TSP: estrutura conceitual, reconhecimento e mensuração (`contabilidade-publica.nbc-tsp-estrutura-mensuracao`, 750 tópicos de edital)

```
Matéria: contabilidade-publica.nbc-tsp-estrutura-mensuracao (NBC TSP: estrutura conceitual, reconhecimento e mensuração)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da NBC TSP e evolução normativa": 18
- "2. RCPG, usuários, características e entidade que reporta": 16
- "3. Elementos, competência, reconhecimento e evidenciação": 22
- "4. Bases de mensuração, valor recuperável e cálculo": 25
- "5. NBC TSP específicas e aplicações integradas": 19

Já existem (não repita):
- Em relatório destinado aos moradores que utilizam serviços públicos, a equipe discute o pr
- Uma entidade publica informações financeiras sem que cada cidadão possa exigir relatório p
- Uma biblioteca pública utiliza seu edifício por décadas para oferecer leitura gratuitament
- Uma entidade recebe, no último dia de dezembro, serviço de limpeza contratualmente aceito 
- Um relatório mostra superávit patrimonial de 18 mil, mas os valores de algumas receitas ai
- Ao divulgar gastos com atendimento, a entidade pretende permitir que a população avalie se
- O relatório informa o valor de material estocado, mas omite perda relevante já identificad
- Duas entidades apresentam despesas semelhantes, mas uma divulga criteriosamente os métodos
- Um gestor argumenta que uma irregularidade de pequeno valor não pode ser material em nenhu
- Um departamento operacional sem personalidade jurídica própria integra demonstrações de um
- Após doação de equipamento, a entidade assume seu controle e passa a usá-lo regularmente n
- A entidade recebe computadores e assume obrigação de pagar o fornecedor após a entrega, se
- No período, uma entidade reconhece receitas patrimoniais de 90 mil e despesas patrimoniais
- Uma entidade reconhece aumento patrimonial quando o direito de receber por serviço já pres
- Diante de processo judicial com perda apenas possível, a equipe cogita registrar provisão 
- Uma entidade mantém software controlado por licença de uso plurianual com potencial de ser
- Uma entidade identifica bem reconhecível e mensurável, mas propõe apenas descrevê-lo nas n
- Um bem especializado de ensino é mantido para operar por muitos anos; em análise da termin
- Uma máquina de custo 120 mil, valor residual 20 mil e vida útil de cinco anos é utilizada 
- O laboratório gratuito mantém equipamentos para produzir atendimento sem retorno financeir
- Uma entidade dispõe de equipamento com custo histórico de 40 mil e informação de mercado q
- Em 2026, um professor compara as bases de mensuração da estrutura publicada em 2016 com a 
- Na comparação de normas aplicáveis ao imobilizado, uma questão pede identificar a substitu
- Uma unidade recebe mobiliário em dezembro e paga no mês seguinte; também divulga gastos or
- Uma obrigação de restauração ambiental decorre de dano já ocorrido e exige estimativa técn
- Uma entidade pode demonstrar patrimônio controlado para prestar serviços gratuitos mesmo s
- Ao pagar em fevereiro por serviço efetivamente recebido e reconhecido em dezembro, a entid
- Informações sobre número de atendimentos e recursos consumidos podem complementar demonstr
- A comparabilidade obriga duas entidades distintas a apresentarem o mesmo valor contábil pa
- O passivo de fornecedor pode surgir quando o equipamento é entregue e aceito, mesmo que a 
- Um resultado patrimonial positivo de 50 mil significa, necessariamente, que o saldo bancár
- Uma omissão de pequeno valor pode ser relevante pela natureza do fato, mesmo que represent
- Um relatório contábil de propósito geral se destina apenas a administradores que podem req
- Aquisição de equipamento destinado a uso por vários exercícios pode trocar caixa por imobi
- Basta que exista processo judicial contra a entidade para reconhecer provisão pelo valor t
- Uma licença de software controlada, identificável e utilizada em serviços pode ser examina
- Basta colocar informação sobre ativo reconhecível nas notas explicativas para dispensar su
- No método linear, bem de 100 mil com residual de 10 mil e vida útil de nove anos tem quota
- O valor corrente operacional da revisão R1 corresponde, por definição, ao preço de venda i
- Na revisão R1, custo histórico e valor corrente são modelos conceituais distintos, sem que
- Como a NBC TSP 37 foi publicada em março de 2026, a NBC TSP 07 deixou de ser aplicável ret
- No conjunto de normas brasileiras, a NBC TSP 09 trata da recuperabilidade de ativos não ge
- O número de uma NBC TSP corresponde necessariamente ao número da IPSAS internacional que t
- Um inventário de materiais hospitalares destinado ao consumo na prestação de serviços pode
- Uma minuta disponibilizada em audiência pública basta para tornar obrigatória nova norma d
```

## Pedido 12: Processo legislativo (`direito-constitucional.processo-legislativo`, 740 tópicos de edital)

```
Matéria: direito-constitucional.processo-legislativo (Processo legislativo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza do processo legislativo, espécies e procedimentos": 19
- "2. Emenda constitucional, iniciativa, quóruns e emendas parlamentares": 19
- "3. Tramitação, sanção, veto, promulgação e técnica legislativa": 22
- "4. Medidas provisórias, leis delegadas e procedimentos especiais": 19
- "5. Processo legislativo estadual, distrital e municipal e jurisprudência": 21

Já existem (não repita):
- Uma pessoa consulta o art. 59 da Constituição para saber qual ato resulta diretamente do p
- Uma proposta de emenda à Constituição Federal alcança três quintos dos votos na primeira v
- Um projeto trata de matéria que a Constituição reservou expressamente à lei complementar. 
- Um projeto de lei aprovado pela Casa iniciadora recebe emenda na Casa revisora. Segundo o 
- Um projeto de lei chega à sanção presidencial e transcorrem quinze dias úteis sem decisão.
- Uma medida provisória federal permanece sem apreciação quarenta e cinco dias após publicaç
- Uma pessoa elabora lei delegada em âmbito federal. Qual ato constitucional confere a deleg
- Na elaboração de Lei Orgânica municipal, a Câmara busca cumprir o art. 29, caput, da Const
- Uma assembleia estadual pretende copiar o regime federal de medidas provisórias. Qual cond
- Uma cidadã apresenta projeto de lei de interesse específico municipal por iniciativa popul
- Comissão recebe um projeto cujo rito regimental permite deliberação conclusiva e, posterio
- Grupo pretende exercer iniciativa popular federal de projeto de lei e reúne assinaturas de
- O Presidente solicita urgência constitucional para projeto de sua iniciativa. A Casa inici
- A Presidência deseja vetar apenas duas palavras de um inciso por contrariedade ao interess
- Ao revisar projeto de lei, a equipe identifica que o texto não distingue disposições intro
- Uma MP perde eficácia por não ser convertida e o Congresso deixa transcorrer sessenta dias
- Resolução do Congresso autoriza lei delegada e determina que o projeto elaborado retorne à
- Lei municipal de iniciativa parlamentar prevê serviço de monitoramento com custo para a ad
- Uma PEC foi rejeitada neste ano legislativo. Parlamentares pretendem apresentar nova PEC c
- Uma análise do art. 61, § 1º, II, b, conclui que toda lei tributária de qualquer Estado de
- Em projeto de iniciativa privativa presidencial que reorganiza carreira da administração f
- O Congresso aprovou projeto de conversão que altera texto de medida provisória ainda vigen
- Em sessão conjunta para apreciação de veto presidencial, há apoio de maioria absoluta dos 
- Uma constituição estadual prevê rito próprio de proposições e a assembleia usa apenas uma 
- Durante tramitação na Câmara dos Deputados, uma proposição recebe parecer favorável e pedi
- Um roteiro de revisão identifica, entre as espécies normativas do art. 59 da Constituição,
- Uma proposta de emenda constitucional federal, aprovada pelo rito do art. 60, deve necessa
- Uma lei complementar, quando sua matéria estiver reservada pela Constituição, deve ser apr
- Em projeto de lei sujeito ao art. 66, a ausência de manifestação presidencial por quinze d
- Uma medida provisória federal não convertida em sessenta dias pode ter sua vigência prorro
- Uma lei de origem parlamentar invade competência de iniciativa reservada do Executivo, mas
- O art. 3º da LC nº 95/1998 distingue parte preliminar, normativa e final; na última podem 
- Como a medida provisória tem força de lei, ela pode dispor sobre qualquer matéria reservad
- Um decreto legislativo aprovado para matéria de competência própria do Poder Legislativo p
- O prazo de quarenta e cinco dias que desencadeia urgência de medida provisória no art. 62,
- Pelo art. 32, caput, a Lei Orgânica do Distrito Federal depende de aprovação em dois turno
- O percentual de um por cento do eleitorado nacional distribuído por cinco Estados, previst
- No veto expresso a projeto de lei, o Presidente deve comunicar ao Presidente do Senado Fed
- No regime de urgência presidencial do art. 64, os prazos de quarenta e cinco dias por Casa
- Se uma medida provisória for rejeitada ou perder eficácia por decurso de prazo, o art. 62,
- Se o Congresso não editar decreto legislativo no prazo de sessenta dias após perda de efic
- Ao contrário de um veto a palavras isoladas, o veto parcial ao texto integral de determina
- Os percentuais federais de iniciativa popular do art. 61, § 2º, permitem aos cidadãos, pel
- Segundo o STF, a adoção de medida provisória por Estado-membro é possível se a Constituiçã
- Em lei delegada, o art. 68 permite que resolução do Congresso autorize o Presidente a disc
```

## Pedido 13: Gestão de SST, investigação de acidentes e controle de perdas (`seguranca-trabalho.gestao-sst-investigacao-acidentes-controle-perdas`, 737 tópicos de edital)

```
Matéria: seguranca-trabalho.gestao-sst-investigacao-acidentes-controle-perdas (Gestão de SST, investigação de acidentes e controle de perdas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos dos sistemas de gestão de SST": 20
- "2. SESMT: constituição, modalidades e dimensionamento": 19
- "3. Riscos ocupacionais e programas de controle de perdas": 21
- "4. Acidentes, registros, investigação e perícias": 20
- "5. Responsabilidades, PPP, NTEP e convenções da OIT": 20

Já existem (não repita):
- Uma empresa deseja implantar um processo que identifique perigos, controle atividades e re
- Em uma organização com empregados celetistas, a direção quer saber a função essencial do S
- Ao organizar seu PGR, um empreendimento reúne perigos e avaliações em um documento e defin
- Um setor passou a ouvir seus trabalhadores antes de alterar os procedimentos e a avaliar s
- Um acidente ocorreu na segunda-feira, sem morte nem afastamento. Sobre a comunicação à pre
- Uma equipe aplica o ciclo PDCA a uma inspeção de máquinas. Qual ação corresponde principal
- Uma empresa possui estabelecimento que isoladamente alcança o enquadramento do Anexo II da
- Um trabalhador se aproxima de roletes acessíveis em máquina em funcionamento. Qual medida 
- Um veículo de movimentação atingiu uma prateleira e danificou mercadorias, sem ferimentos.
- A atividade principal de um estabelecimento tem grau de risco 2; a atividade com mais trab
- Uma organização soma trabalhadores de estabelecimentos da mesma unidade federativa. Nenhum
- Após uma falha que provocou vazamento, analistas partem do evento indesejado final para ma
- Uma investigação encontrou desligamento de sensor de segurança, manutenção atrasada e pres
- Ao revisar instrumentos internacionais, uma empresa busca a convenção que aborda especific
- Ao organizar jornadas do SESMT, uma organização compara um técnico de segurança e um engen
- Uma empresa avalia fatores psicossociais de risco em seu PGR após 26 de maio de 2026. Qual
- Em controvérsia sobre insalubridade, foi solicitado exame técnico conforme o art. 195 da C
- Uma contratante recebe trabalhadores de prestadora de forma contínua no mesmo local; eles 
- Uma análise parte de uma falha de energia e acompanha diferentes consequências conforme a 
- Uma unidade reduziu o número anual de acidentes notificados, mas também deixou de realizar
- Durante um reconhecimento previdenciário por doença, surge associação epidemiológica entre
- Após acidente com lesão, a empresa mantém seguro contra acidente de trabalho. A discussão 
- Ao revisar o contexto do sistema ISO 45001 em 2026, uma empresa verifica impactos de ondas
- No mesmo estabelecimento, atividades econômicas distintas ocupam igual número de trabalhad
- A direção adquiriu protetores individuais após identificar risco grave que poderia ser eli
- Após identificar pontos de esmagamento em equipamento, uma equipe implantou proteções, ava
- Um estabelecimento concluiu que todas as empresas devem manter, em cada unidade, os cinco 
- Uma organização elaborou seu PGR com identificação e avaliação dos perigos e com plano de 
- Uma pessoa sofreu corte no trabalho sem necessidade de afastamento. Nessa hipótese, a empr
- Em uma inspeção, foi registrada colisão que danificou equipamento, mas não feriu pessoas. 
- Em treinamento sobre normas internacionais, afirmou-se que a Convenção OIT 187 regula a co
- Uma organização contratou serviço não eventual em suas instalações, e os trabalhadores da 
- Uma equipe utiliza árvore de eventos para partir de uma falha final e descobrir, retroativ
- Uma empresa substituiu equipamento perigoso por outro que eliminou a zona de esmagamento. 
- Depois de um acidente fatal, uma empresa decidiu comunicar a ocorrência à previdência apen
- Para o PPP eletrônico, as informações de agentes nocivos informadas no S-2240 têm finalida
- A empresa manteve alto índice de proteção de máquinas, mas registrou poucos acidentes. Par
- Um técnico do SESMT identificou condição de grave e iminente risco e propôs imediatamente 
- Uma empresa certificada na ISO 45001 deixou de cumprir requisitos da NR-1. A certificação 
- Em duas atividades econômicas distintas do mesmo estabelecimento há igual número de trabal
- Após acidente com amputação, concluiu-se que a CAT emitida comprova por si só o dolo de qu
- Após suspeita de doença relacionada ao trabalho, a perícia previdenciária identificou NTEP
- Dois estabelecimentos da mesma unidade federativa não atingem separadamente o Anexo II da 
- Na investigação de quase acidente, a equipe identificou intertravamento desligado por falh
- Uma empresa calculou que, por ter ocorrido certo número de quase acidentes, necessariament
```

## Pedido 14: Demonstrações contábeis do setor público (`contabilidade-publica.demonstracoes-setor-publico`, 735 tópicos de edital)

```
Matéria: contabilidade-publica.demonstracoes-setor-publico (Demonstrações contábeis do setor público)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, normas e PCASP": 14
- "2. Balanço Orçamentário e Balanço Financeiro": 24
- "3. Balanço Patrimonial e Demonstração das Variações Patrimoniais": 24
- "4. Demonstração dos Fluxos de Caixa e DMPL": 18
- "5. Notas explicativas, informação orçamentária e análise integrada": 20

Já existem (não repita):
- Uma entidade pretende divulgar, em um quadro, receitas previstas, receitas arrecadadas, do
- Em certo exercício, a receita orçamentária arrecadada foi de 820, as despesas empenhadas f
- No encerramento do exercício, uma unidade precisa evidenciar ingressos orçamentários, rece
- O Balanço Patrimonial apresenta ativo de 1.500 e passivo de 1.180. Sem outras particularid
- Uma entidade reconheceu VPA de 560 e VPD de 610, por competência, no período. O resultado 
- No ano, ocorreram recebimento de tributos, pagamento por construção de prédio e ingresso d
- O patrimônio líquido inicial era 300. No período, houve resultado patrimonial de +50 e aju
- Ao final do exercício, uma despesa empenhada permaneceu sem pagamento e foi inscrita em re
- Um registro de execução da despesa orçamentária deve ser relacionado à estrutura do PCASP.
- A dotação atualizada de um programa foi 1.000; empenharam-se 850, arrecadaram-se receitas 
- O saldo de caixa inicial era 100. Houve ingressos efetivos de 420, pagamentos efetivos de 
- No quadro financeiro do BP, há ativo financeiro de 570 e passivo financeiro de 640. O tota
- Um ente recebeu máquina avaliada em 120 mediante pagamento imediato pelo mesmo valor, sem 
- A DFC registrou fluxo operacional de +95, investimento de −60 e financiamento de +20. O ca
- Um ativo contém incerteza significativa de mensuração, cujas premissas não aparecem na fac
- Uma unidade divulgou previsão inicial de receita de 900, previsão atualizada de 1.050 e ar
- Em 2026, uma entidade pagou 75 de restos a pagar de 2025, sem realizar novo empenho para a
- No encerramento, o BF registra receita orçamentária de 800, transferências recebidas de 30
- Tributo de 120 tornou-se crédito exigível e foi reconhecido patrimonialmente em dezembro, 
- Uma DMPL mostra PL inicial de 900, resultado da DVP de +110, ajuste direto de −35 e transf
- O contador precisa relacionar, no PCASP, ativo, variação patrimonial diminutiva, orçamento
- Em outubro de 2026, equipe prepara demonstrações referentes ao exercício iniciado em janei
- Um ente encerrou o ano com ativos patrimoniais de 2.400, passivos patrimoniais de 1.950, a
- Uma entidade recebe equipamento de 200 mediante obrigação a pagar em parcelas futuras, sem
- No exercício, a receita arrecadada foi 1.200 e a despesa empenhada 1.260. A DVP apurou VPA
- Em determinada entidade, o BO registra receitas arrecadadas e despesas empenhadas, enquant
- Um equipamento foi empenhado por 90 e pago por 70 no mesmo exercício. Para o resultado orç
- No BP, uma entidade apresenta ativo de 850 e passivo de 650. O patrimônio líquido é positi
- Ao registrar depreciação de 25 de equipamento público, sem pagamento no mês, a entidade de
- Quando uma entidade evidencia na DFC o recebimento de empréstimo bancário como fluxo de fi
- As notas explicativas, por serem facultativas para interpretar os números do BP, podem ser
- Se uma despesa de 2025 foi paga mediante restos a pagar em 2026, o pagamento deve elevar o
- Uma unidade empenhou despesa ainda não liquidada até 31 de dezembro e permaneceu sem pagá-
- Em um BP, ativos circulantes de 300 devem ser contabilizados integralmente como ativos fin
- Uma entidade com ativo financeiro de 500 e passivo financeiro de 410 possui superávit fina
- Uma entidade recebeu 150 de empréstimo e reconheceu passivo de igual valor. Embora o caixa
- Uma aplicação financeira de longo prazo, sujeita a oscilação relevante de valor, deve figu
- Uma DMPL pode explicar por que o aumento total do patrimônio líquido diverge do resultado 
- A receita realizada em 2026 superou a previsão inicial, mas ficou abaixo da previsão atual
- No BF, a inscrição demonstrativa de restos a pagar de 40 pode ser interpretada como aument
- O reconhecimento por competência de perda por deterioração de ativo pode aumentar VPD e re
- As NBC TSP 11, 12 e 13 (R1), embora publicadas no Diário Oficial em março de 2026, determi
- Se uma entidade recebe crédito tributário já reconhecido como ativo e VPA em período anter
- Um ente com receita arrecadada de 600, despesa empenhada de 570, ativo financeiro de 180 e
- Uma nota explicativa pode detalhar restrições relevantes ao uso de caixa e equivalentes, m
```

## Pedido 15: Auditoria governamental: normas, tipos, controle externo e achados (`auditoria.auditoria-governamental`, 733 tópicos de edital)

```
Matéria: auditoria.auditoria-governamental (Auditoria governamental: normas, tipos, controle externo e achados)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, evolução, controle externo e auditoria interna": 17
- "2. Normas INTOSAI, NBASP e NAT: instituições e profissionais": 26
- "3. Auditoria financeira, de conformidade e operacional": 19
- "4. Instrumentos de fiscalização e ciclo de planejamento": 17
- "5. Achados, responsabilização, relatórios e monitoramento": 21

Já existem (não repita):
- Uma entidade privada administra valores públicos recebidos para executar um projeto. Um re
- Uma unidade de auditoria interna recebe pedido para avaliar governança, riscos e controles
- Ao examinar as competências constitucionais sobre contas públicas, uma equipe distingue as
- O controle interno conhece irregularidade grave em ato de gestão já documentado. Qual prov
- Uma auditoria contábil, financeira, orçamentária, operacional e patrimonial é determinada 
- Uma equipe organiza trabalho conforme a NBASP 100, distinguindo planejamento, execução, co
- Em uma definição de auditoria do setor público, uma comissão pretende incluir a ideia de c
- Uma equipe identifica auditor, parte responsável e usuários previstos, além do objeto exam
- Durante revisão das normas internas, surge dúvida sobre a relação entre as Portarias-TCU n
- Uma instituição revisa seu programa de controle externo e deseja proteger acesso a dados, 
- Uma instituição fiscalizadora apresenta resultados acessíveis, mede benefícios da fiscaliz
- Para fortalecer condutas e prevenir conflito de interesses, uma instituição de controle de
- Um sistema institucional passa a exigir revisão independente de relatórios e supervisão da
- Uma auditoria de tecnologia demanda competências que parte da equipe ainda não possui. Qua
- Na abertura de trabalho, a equipe consulta a NBASP 100 para estruturar os elementos essenc
- Uma auditoria interna pretende adotar diretamente todas as NAT como imposição universal a 
- Um trabalho examina se uma ação pública reduziu o tempo de espera e se os serviços entregu
- Uma auditoria verifica se uma contratação observou uma exigência expressa de publicidade a
- O objetivo declarado do trabalho é avaliar informação apresentada em demonstrações finance
- Um programa atingiu a meta de 1.000 atendimentos, porém não houve alteração mensurável no 
- Antes de decidir se auditará um sistema de abastecimento, a instituição precisa conhecer u
- Ao instruir fiscalização em andamento, faltam documentos pontuais para esclarecer uma dúvi
- Uma entidade fiscalizadora examina etapas de implantação de um programa enquanto a execuçã
- Uma equipe constatou pagamentos sem conferência obrigatória e precisa organizar regra infr
- Foi constatada falha numa contratação, mas vários gestores ocuparam a unidade em períodos 
- Uma fundação pública contrata serviço com recursos próprios e será fiscalizada tanto sob a
- Ao examinar as contas anuais da Presidência da República, o tribunal de contas federal dev
- Uma equipe estrutura a auditoria em planejamento, execução, relatório e monitoramento. Tai
- Por estar inserida na estrutura administrativa, a auditoria interna governamental pode sub
- Um trabalho que confronta ato de contratação com regra aplicável tem enfoque de conformida
- O art. 74 da Constituição atribui aos sistemas internos apenas a verificação de documentos
- Uma unidade de auditoria interna pode contribuir para aperfeiçoar controles e riscos, enqu
- Em um programa de desenvolvimento institucional, a NBASP 140 é a referência específica par
- Ao avaliar a independência da instituição fiscalizadora, a equipe pode considerar acesso à
- Uma equipe usa o anexo original da Portaria-TCU 280/2010 como referência necessariamente v
- Uma instituição de controle passa a divulgar de modo acessível os resultados e a justifica
- Uma auditoria que examina a conformidade de determinada contratação se transforma automati
- Após deliberação para corrigir falhas de um sistema, a instituição verifica tanto a implem
- Em fiscalização já instaurada, surge lacuna pontual de informação. O instrumento adequado 
- Ao redigir um achado, a equipe registra o critério previsto, a condição verificada, a caus
- Uma matriz de responsabilização pode atribuir o ato irregular ao dirigente atual pelo simp
- Ao elaborar matriz de planejamento, uma equipe registra questões, critérios, fontes e proc
- Quando um programa atinge a meta numérica mas não modifica o problema público que motivou 
- Se um relatório examinou processos de duas unidades, explicitar a abrangência da análise e
- No monitoramento, a comprovação da compra de novo sistema é suficiente para afirmar que o 
```

## Pedido 16: Ministério Público e MPT (`direito-constitucional.ministerio-publico`, 727 tópicos de edital)

```
Matéria: direito-constitucional.ministerio-publico (Ministério Público e MPT)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza, missão e princípios institucionais": 19
- "2. Organização constitucional, chefias e Ministério Público do Trabalho": 21
- "3. Ingresso, garantias, vedações e iniciativa normativa": 19
- "4. Funções institucionais, ação civil e atuação constitucional do MPT": 18
- "5. CNMP, Ministério Público de Contas e limites do controle": 23

Já existem (não repita):
- Uma associação procura o órgão constitucional incumbido de defender a ordem jurídica, o re
- Um membro do MP passa a atuar em processo que antes cabia a outro membro, por substituição
- Um relatório classifica o Ministério Público do Distrito Federal e Territórios como órgão 
- O Presidente da República pretende nomear o chefe do MPU. De acordo com o art. 128, § 1º, 
- Após dois anos de efetivo exercício, um membro adquire garantia constitucional que condici
- Uma lei pretende autorizar determinado órgão a propor ações penais públicas em substituiçã
- Uma entidade ambiental pretende promover ação civil pública, mas lhe informaram que apenas
- Ao analisar a composição do Conselho Nacional do Ministério Público, um relatório deve ind
- Um município pede que o MP assuma a representação judicial da prefeitura em ações ordinári
- Um membro do MP será removido compulsoriamente por interesse público. Qual é o quórum cons
- O chefe do Poder Executivo solicita ao MP sua proposta orçamentária; a instituição deixa d
- O MPT escolhe lista tríplice para a chefia interna. Nos termos do art. 88 da LC 75/1993, q
- Um bacharel pretende ingressar na carreira do MP. Qual requisito combina corretamente moda
- O CNMP pretende rever processo disciplinar de membro do MPE já julgado. Qual limite tempor
- Uma paralisação atinge serviço essencial e pode comprometer gravemente o interesse público
- Uma unidade pretende registrar que o Ministério Público de Contas integra o Ministério Púb
- Após aposentadoria de membro do MP, pretende-se exercer advocacia perante o tribunal do qu
- Uma autoridade alega que a chefia nacional pode impor a um membro do MP o conteúdo da mani
- Um MP estadual elabora proposta de criação de cargos, mas sua direção afirma que a autonom
- Em caso de greve de serviço essencial com possibilidade de lesão pública, o empregador arg
- Um manual institucional atribui ao CNMP poder para determinar aposentadoria administrativa
- Ao elaborar quadro do CNMP, uma equipe contabilizou quatro integrantes do MPU, três do MPE
- Um estudo equipara a escolha do PGR à dos procuradores-gerais estaduais. Qual distinção pe
- Uma corte de contas propõe que membros de seu MP especial ingressem automaticamente em car
- Uma proposta compara a AGU e o MP em disputa envolvendo interesse do ente federal e direit
- Ao atuar na tutela da ordem jurídica e de direitos sociais indisponíveis, o Ministério Púb
- Como o MPU é instituição nacional, seus quatro ramos incluem o MPF, o MPT, os Ministérios 
- Um membro do MP que complete dois anos de exercício adquire vitaliciedade, de modo que a p
- Em procedimento ambiental, o Ministério Público é o único legitimado constitucional para p
- No Conselho Nacional do Ministério Público, o Procurador-Geral da República ocupa uma das 
- Ao definir a chefia estadual, a Constituição exige lista tríplice e mandato de três anos, 
- Uma chefia institucional pode organizar a distribuição administrativa de atribuições sem q
- Ao escolher o chefe do MPT, a LC 75/1993 determina nomeação pelo Presidente da República a
- O exercício de outra função pública por membro do MP encontra exceção constitucional para 
- A ordem constitucional autoriza o MPT a julgar dissídios coletivos originados de greve em 
- O Conselho Nacional do Ministério Público pode apreciar a legalidade dos atos administrati
- O MP junto aos Tribunais de Contas integra o MPU e exerce suas funções de acordo com a div
- A Constituição permite ao MP propor a criação de cargos e serviços auxiliares ao Poder Leg
- Como a proposta orçamentária do MP é autônoma, seu envio acima dos limites da LDO impede q
- O art. 128, § 5º, autoriza a iniciativa de leis complementares organizadoras dos MPs aos r
- Depois da EC 103/2019, o art. 130-A, § 2º, III, ainda autoriza o CNMP a aplicar aposentado
- No CNMP, o Corregedor nacional deve ser escolhido por voto secreto dentre membros do Minis
- Uma apuração de irregularidades em serviço público permite ao MP defender judicialmente o 
- Na LC 75/1993, o Procurador-Geral do Trabalho é nomeado pelo PGR de lista tríplice da inst
- A garantia do art. 130 concedida aos membros do MP de Contas estende a essa instituição a 
```

## Pedido 17: Partidos políticos, eleições, propaganda e prestação de contas (`direito-eleitoral.partidos-propaganda`, 726 tópicos de edital)

```
Matéria: direito-eleitoral.partidos-propaganda (Partidos políticos, eleições, propaganda e prestação de contas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Partidos políticos, organização, filiação e federações": 28
- "2. Sistemas eleitorais, convenções, registro e organização do pleito": 18
- "3. Propaganda partidária, eleitoral, internet e poder de polícia": 25
- "4. Financiamento, Fundo Partidário, contas e ilícitos de campanha": 19
- "5. Integração normativa e questões de interpretação eleitoral": 10

Já existem (não repita):
- Um grupo concluiu o registro civil de uma nova agremiação política e consulta a natureza j
- Uma associação partidária obteve personalidade no registro civil e pretende disputar eleiç
- Duas legendas desejam atuar como unidade nas disputas majoritárias e proporcionais, manten
- Na eleição proporcional, duas legendas pretendem apresentar candidatos sob coligação comum
- Uma pessoa pretende disputar eleição e pergunta pelo prazo mínimo geral de filiação à lege
- Uma legenda mantém direção estadual e comissão provisória local, além de reunir seus membr
- Em eleição proporcional hipotética, foram apurados 100.000 votos válidos para dez cadeiras
- Uma agremiação pretende escolher candidaturas em convenção conforme a Lei nº 9.504/1997, a
- Uma federação realizou convenção válida e precisa transmitir o pedido de registro de candi
- Após a convenção, uma candidatura deseja iniciar a propaganda eleitoral comum em rede soci
- Em abril do ano eleitoral, pessoa que pretende concorrer participa de entrevista, discute 
- Uma candidatura pretende contratar propaganda paga em jornal impresso e horário publicitár
- No dia da votação, uma eleitora usa broche pequeno com a legenda de preferência, sem abord
- Uma legenda veicula inserções gratuitas em rádio para apresentar programa político, sem fa
- Uma pessoa física teve rendimentos brutos de R$ 150.000 no ano anterior ao pleito e preten
- Uma equipe confunde o relatório de manutenção anual da legenda com a contabilidade de dete
- Em distribuição proporcional didática, o quociente eleitoral foi fixado em 10.000 votos e 
- Uma análise de distribuição de vagas remanescentes afirma que partidos sem 80% do quocient
- A 48 horas do término do pleito de 2026, pretende-se publicar novo vídeo sintético com voz
- Em fiscalização de propaganda irregular, um responsável pretende usar o poder de polícia p
- No ano eleitoral, uma administração anuncia nova distribuição gratuita de benefícios sem e
- Desde o registro de candidatura, uma pessoa concorrente promete emprego individual a deter
- Uma candidatura teve contas desaprovadas por falhas relevantes, mas não há conclusão sobre
- Uma plataforma recebe ordem específica de tornar conteúdo eleitoral ilícito indisponível, 
- Um pesquisador atribui à lei de Duverger a afirmação de que eleição distrital uninominal m
- A Constituição admite liberdade de criação de partidos com caráter nacional e veda coligaç
- Depois de registrar-se no cartório civil e receber personalidade jurídica, um partido está
- Federação de partidos, nos termos do art. 11-A da Lei nº 9.096/1995, pressupõe duas ou mai
- Dois partidos podem disputar juntos a eleição proporcional por coligação eventual, pois a 
- O estatuto partidário disciplina organização interna, direitos e deveres dos filiados, esc
- Para qualquer candidatura em 2026, a filiação partidária deveria ter ocorrido pelo menos u
- A propaganda partidária gratuita destinada a divulgar ideias e programas da legenda possui
- Nas eleições gerais de 2026, a Resolução TSE nº 23.609/2019 manteve obrigatória a instalaç
- A regra geral proíbe propaganda eleitoral paga na internet, mas admite impulsionamento ide
- Uma candidatura pode comprar anúncio eleitoral em rádio e televisão fora do horário gratui
- No dia da eleição, uma pessoa que usa broche individual de preferência partidária sem abor
- A rotulagem ostensiva de um deepfake eleitoral produzido para alterar a voz de adversário 
- A prestação de contas anual dos partidos referente ao exercício de 2025 deveria ser encami
- Uma empresa regularmente registrada e identificada por CNPJ pode doar recursos próprios di
- O art. 73, § 10, da Lei nº 9.504/1997 admite, em ano eleitoral, exceções à vedação de dist
- A proibição de distribuição gratuita de benefícios no art. 73, § 10, limita-se aos três me
- Prometer emprego específico a eleitor com finalidade de obter seu voto, entre o registro d
- Após os julgamentos do STF sobre o art. 109 do Código Eleitoral, toda legenda que não alca
- A desaprovação de prestação de contas eleitoral não demonstra automaticamente ilícito mate
- No dia do pleito, o art. 39-A da Lei nº 9.504/1997 autoriza grupos organizados com vestuár
```

## Pedido 18: Citações, sentença, procedimentos e Tribunal do Júri (`processo-penal.citacoes-sentenca-procedimentos`, 723 tópicos de edital)

```
Matéria: processo-penal.citacoes-sentenca-procedimentos (Citações, sentença, procedimentos e Tribunal do Júri)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Citação, intimações, revelia e suspensão do processo": 21
- "2. Sentença penal: requisitos, absolvição, condenação e correlação": 23
- "3. Procedimento comum ordinário, sumário, sumaríssimo e processo especial": 28
- "4. Tribunal do Júri: judicium accusationis e decisões da primeira fase": 14
- "5. Tribunal do Júri: plenário, quesitos, desaforamento e execução": 14

Já existem (não repita):
- Acusado preso aguarda a primeira citação em processo penal. Conforme o art. 360 do CPP, a 
- Em uma sentença condenatória, o juiz apresenta fundamentos de fato e de direito, dispositi
- Um crime sujeito ao procedimento comum possui pena privativa de liberdade máxima abstrata 
- Na primeira fase do Tribunal do Júri, o juiz está convencido da materialidade do fato e da
- Uma sessão de julgamento popular dispõe de 25 jurados convocados; após os atos próprios, é
- Um oficial procura o acusado para citação, mas constata e certifica que ele se esconde del
- Na instrução, a prova não demonstra de modo suficiente que o acusado tenha praticado o del
- Segundo o art. 61 da Lei 9.099/1995, um crime com pena máxima de 2 anos, cumulada ou não c
- Na primeira fase do Júri, o juiz não encontra indícios suficientes de autoria, embora não 
- Após pronúncia preclusa, a defesa apresenta em 5 dias relação de seis testemunhas que pret
- Acusado que está em endereço conhecido de outro país deve ser citado para se defender no p
- A denúncia descreve integralmente ameaça empregada na subtração, mas atribui ao fato class
- Em processo ordinário com denúncia recebida, o acusado é citado para apresentar defesa esc
- Na primeira fase do Júri, prova inequívoca demonstra que o acusado não participou do fato 
- No julgamento popular, os jurados afirmam materialidade e autoria por maioria. Conforme a 
- Uma denúncia por crime funcional afiançável foi instruída com inquérito policial. A defesa
- Um acusado foi regularmente citado por edital, não compareceu e não constituiu advogado. O
- Ao encerrar instrução em crime de ação pública, surge prova de circunstância relevante que
- No Júri, os autos indicam que a conduta demonstrada é homicídio culposo, não crime doloso 
- Condenação em plenário do Júri fixou pena de 9 anos. Uma manifestação sustenta que a execu
- Um crime de pena máxima abstrata de 6 anos apresenta réu citado por edital, não comparecen
- Duas sentenças absolutórias foram proferidas: na primeira, ficou provado que o fato jamais
- Infração de menor potencial ofensivo não pôde prosseguir no Juizado porque o acusado não f
- O juiz pronunciou o acusado. O réu está solto e não foi encontrado para intimação pessoal 
- Há dúvida concreta sobre a imparcialidade dos jurados na comarca, embora continue sendo cr
- O art. 361 do CPP prevê citação por edital com prazo de 15 dias quando o acusado não é enc
- A sentença penal, para ser válida, pode omitir seus fundamentos de fato e de direito desde
- No procedimento comum ordinário, a resposta escrita à acusação é apresentada, em regra, em
- Na primeira fase do Júri, a pronúncia equivale a condenação e impõe ao juiz togado dosar a
- No Tribunal do Júri, 25 jurados são sorteados para a reunião e 7 compõem o Conselho de Sen
- A citação por hora certa exige apenas que o acusado esteja ausente em sua residência na pr
- O art. 382 do CPP prevê prazo de 2 dias para qualquer das partes requerer esclarecimento d
- No procedimento ordinário, o limite de testemunhas da acusação e da defesa é de 5 para cad
- Na primeira fase do Júri, a impronúncia não impede necessariamente nova acusação baseada e
- Na votação do Júri, a primeira questão indaga se o acusado deve ser absolvido, seguida de 
- Nos crimes funcionais afiançáveis abrangidos pelos arts. 513 e 514 do CPP, a resposta prel
- O acusado citado por edital que não comparece nem constitui advogado sujeita-se, por esse 
- Emendatio libelli, conforme o art. 383 do CPP, permite ao juiz atribuir definição jurídica
- O limite de suspensão do prazo prescricional no art. 366 do CPP é calculado, conforme a Sú
- Se a instrução revela circunstância fática relevante que não constava da denúncia em ação 
- No Juizado Especial Criminal, a Lei 9.099/1995 autoriza que o acusado não encontrado seja 
- A prova de que o acusado não foi autor nem partícipe do fato, na primeira fase do Júri, po
- O desaforamento previsto no art. 427 do CPP é forma de desclassificação do delito para for
- O STF, no Tema 1.068 (RE 1.235.340), afastou o limite de 15 anos do art. 492 do CPP para e
- Respondidas afirmativamente por maioria materialidade e autoria, o art. 483 do CPP impõe q
```

## Pedido 19: Serviço Social: fundamentos, políticas sociais e instrumentos (`servico-social.fundamentos-politicas-instrumentos`, 719 tópicos de edital)

```
Matéria: servico-social.fundamentos-politicas-instrumentos (Serviço Social: fundamentos, políticas sociais e instrumentos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos históricos e teórico-metodológicos, Estado e sociedade": 20
- "2. LOAS: princípios, direitos, benefícios e organização da assistência": 20
- "3. Política Nacional de Assistência Social e rede SUAS": 21
- "4. Instrumentalidade: diagnóstico, estudo, relatório, laudo e parecer": 20
- "5. Ética, supervisão, saúde mental e juventude": 19

Já existem (não repita):
- Em atendimento a uma família que perdeu renda após fechamento de empregos locais, a equipe
- Uma pessoa sem histórico de contribuição previdenciária busca atendimento da política de a
- Uma equipe realiza levantamento territorial das vulnerabilidades e busca garantir acesso à
- Uma administração pretende substituir integralmente sua responsabilidade na assistência po
- Uma pessoa idosa com 64 anos busca o BPC unicamente pelo critério etário. Qual orientação 
- Uma equipe planeja acompanhamento de famílias para prevenção de riscos sociais e fortaleci
- Uma família relata violação de direitos e necessita acompanhamento especializado sem indic
- Uma equipe reúne entrevistas, informações documentais e análise das condições de moradia e
- Uma requisição exige a elaboração de laudo pericial especificamente sobre matéria de Servi
- Um projeto de participação social abrange pessoas com 17 e 24 anos. À luz do Estatuto da J
- Para fins didáticos, uma família legalmente considerada no BPC possui quatro integrantes e
- Durante discussão sobre o planejamento da assistência social, uma gestão afirma que o deve
- Uma pessoa em situação de violência familiar necessita acompanhamento especializado, mas h
- Após meses de acompanhamento, uma equipe precisa formalizar atendimentos, encaminhamentos 
- Depois de estudo social circunstanciado, solicita-se resposta fundamentada à pergunta espe
- Uma equipe recebe pedido de encaminhamento para serviço e dispõe de prontuário que inclui 
- Uma supervisora de estágio em Serviço Social trabalha trinta horas por semana no campo. Se
- Uma pessoa aceita livremente internação psiquiátrica após indicação médica, assinando decl
- Um grupo de jovens formula propostas para atividades comunitárias e pede participação nas 
- No planejamento de um serviço territorial, é necessário indicar a norma que aprovou a Polí
- Duas pessoas da mesma família, uma idosa de 67 anos e outra com deficiência, requerem BPC.
- Uma unidade socioassistencial informa ter dobrado o número de atendimentos, concluindo que
- Um documento de avaliação social registra fatos observados, relatos da pessoa e inferência
- Uma requisição solicita análise conclusiva pericial sobre matéria de Serviço Social e, sep
- Uma pessoa é internada sem consentimento, a pedido de familiar e mediante avaliação médica
- Ao interpretar o desemprego de determinada comunidade, uma equipe de Serviço Social pode r
- A exigência de comprovação pública e humilhante da situação de pobreza para encaminhamento
- Uma equipe que fortalece vínculos familiares por meio do PAIF, referenciado ao CRAS, atua 
- Em um estudo social, a visita domiciliar constitui requisito obrigatório e suficiente para
- O Estatuto da Juventude abrange pessoas de 15 a 29 anos, sem eliminar a aplicação prioritá
- Como a LOAS estabelece a primazia da responsabilidade estatal na assistência, é incompatív
- Em serviço de proteção especial de média complexidade, o acompanhamento pelo PAEFI pode oc
- O diagnóstico socioassistencial de determinado território pode concluir melhoria efetiva d
- Ao produzir parecer social, a opinião técnica deve responder a questão delimitada com fund
- O estágio em Serviço Social está adequadamente supervisionado quando uma pessoa sem habili
- Uma pessoa com deficiência recebe BPC e inicia aprendizagem remunerada. A legislação assis
- Na avaliação do BPC, o limite de renda per capita de um quarto do salário mínimo proíbe a 
- Diante de serviço não acessível em bairro periférico, o diagnóstico territorial precisa co
- Por estar inserida na política de assistência, uma unidade de CRAS deve conduzir prioritar
- Em perícia social solicitada a profissional habilitado, a classificação de informações com
- Um profissional de Serviço Social pode emitir diagnóstico psiquiátrico substitutivo do lau
- No estágio em Serviço Social, a articulação entre supervisão acadêmica e supervisão de cam
- Um estudante de Serviço Social de 17 anos deve ser tratado apenas pelas regras gerais da j
- Na articulação entre a assistência social e a rede de saúde mental, compartilhar informaçõ
- Uma internação psiquiátrica realizada sem consentimento, exclusivamente a pedido de tercei
```

## Pedido 20: Execução penal (LEP) (`legislacao-penal-especial.execucao-penal`, 718 tópicos de edital)

```
Matéria: legislacao-penal-especial.execucao-penal (Execução penal (LEP))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Finalidade, incidência, classificação, assistência e trabalho": 17
- "2. Órgãos, estabelecimentos, deveres e direitos na execução": 23
- "3. Disciplina, faltas graves, sanções e regime disciplinar diferenciado": 17
- "4. Regimes, progressão de 2026, saídas e remição": 17
- "5. Livramento, monitoração, outras penas, incidentes, agravo e precedentes": 26

Já existem (não repita):
- Um preso provisório pede atendimento de saúde, mas a administração alega que a LEP benefic
- Uma comissão deve classificar pessoas condenadas e organizar seu programa individualizador
- Um preso provisório, apto a trabalhar, recusa uma atividade externa oferecida pela unidade
- Um conselho precisa visitar unidades prisionais, entrevistar presos e apresentar relatório
- A direção de estabelecimento pretende suspender o contato reservado com advogado invocando
- Um condenado em regime fechado quer trabalhar externamente em obra pública. Qual limite pe
- Um interno foge do estabelecimento enquanto cumpre pena privativa de liberdade. Qual class
- Após suspeita de falta grave, a direção determina isolamento preventivo antes da conclusão
- Um condenado trabalhou regularmente 18 dias e pede remição exclusivamente pelo trabalho. Q
- Um condenado em fechado precisa sair para receber tratamento médico impossível na unidade.
- Em julho de 2026, para crime hediondo praticado após as alterações legislativas de 2026, p
- Uma mulher condenada, primária, mãe de criança, busca progressão especial pelo art. 112, §
- Após falta grave reconhecida com defesa, o juiz analisa perda de 30 dias previamente remid
- Um conselho nacional formula diretrizes da execução e possui composição e mandato regulado
- A direção solicita RDD e o pedido chega ao juízo competente sob a redação da Lei 15.407/20
- Uma pessoa em semiaberto pede saída temporária para visitar a família em feriado, com fund
- Durante a execução, um condenado recebe notícia de decisão judicial que indeferiu pedido d
- O preso cumpriu quatro dias de curso com três horas diárias, atividade certificada, e não 
- Uma sentença relativa a crime hediondo com resultado morte praticado após a Lei 15.358/202
- Uma mulher gestante, primária e com comportamento adequado, quer a progressão especial. A 
- O juiz da execução avalia condenado em regime aberto que solicita recolhimento em residênc
- A execução de pena privativa de liberdade de dois anos em regime aberto já alcançou seis m
- Para crime comum não violento praticado após a Lei 15.402/2026, a pessoa é primária e não 
- Um condenado pratica falta grave reconhecida durante execução. A defesa sustenta que o fat
- Uma pessoa recebeu autorização judicial para progressão ao semiaberto e poderá usar equipa
- A LEP prevê efetivar disposições de sentença ou decisão criminal e proporcionar condições 
- O preso provisório está obrigado a prestar trabalho externo sob a mesma disciplina do cond
- Cabe ao Conselho da Comunidade visitar os estabelecimentos existentes na comarca ao menos 
- As faltas graves são definidas apenas por regulamentos de cada unidade federativa, enquant
- Um condenado que trabalha regularmente nove dias pode obter três dias de remição pelo trab
- Após a promulgação de 2024, a saída temporária para visita familiar continua expressamente
- Condenado reincidente por crime não violento praticado depois das alterações de maio de 20
- As sanções disciplinares de isolamento, suspensão ou restrição de direitos podem ser aplic
- No RDD, a LEP prevê como características duração de até dois anos e banho de sol diário de
- Em caso de falta grave, a LEP determina perda automática da totalidade dos dias remidos pe
- A direção do estabelecimento pode conceder permissão de saída com escolta para tratamento 
- O agravo em execução interposto contra decisão que nega progressão tem efeito suspensivo a
- Para fato praticado após maio de 2026 e não incluído nas exceções do art. 112, a regra ger
- O diretor de presídio pode incluir diretamente em RDD, pelo prazo de dois anos, condenado 
- A Lei 15.402/2026 acrescentou ao art. 126 previsão expressa de que cumprir pena restritiva
- Na redação vigente de 2026, o condenado reincidente por crime hediondo com resultado morte
- A falta grave decorrente de fato definido como crime doloso pode ser reconhecida na execuç
- Uma falta grave reconhecida interrompe indistintamente os prazos para progressão, livramen
- O juízo pode determinar monitoração eletrônica em caso de progressão para semiaberto, conf
- Como a Lei 15.358/2026 aumentou frações de progressão em certas hipóteses, o juiz deve apl
```

## Pedido 21: História do Brasil: da colonização ao Império (`historia.brasil-colonia-imperio`, 717 tópicos de edital)

```
Matéria: historia.brasil-colonia-imperio (História do Brasil: da colonização ao Império)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ocupação portuguesa, instituições coloniais e sociedade açucareira": 17
- "2. Resistências, fronteiras, mineração e crise do sistema colonial": 20
- "3. Vinda da Corte, Independência e Primeiro Reinado": 21
- "4. Regências, revoltas provinciais e unidade territorial": 18
- "5. Segundo Reinado: política, café, escravidão, guerras e crise final": 24

Já existem (não repita):
- Ao interpretar um mapa com a linha de Tordesilhas, uma estudante procura explicar a partil
- Um comerciante português organiza um entreposto costeiro para armazenar pau-brasil e negoc
- Uma carta de doação de 1534 concede a um particular direitos de administração e receitas e
- Em 1549, Tomé de Sousa chega a Salvador para dirigir uma nova organização administrativa. 
- Uma propriedade canavieira do século XVII reúne latifúndio, cultivo para exportação e uso 
- Um mapa histórico marca 1630 como início de ocupação neerlandesa em Pernambuco e 1637 como
- Um memorial sobre a Serra da Barriga apresenta os anos de 1694 e 1695 e destaca a trajetór
- Uma aula explica por que áreas portuguesas avançaram muito além da antiga linha de Tordesi
- Um produtor de ouro do século XVIII enfrenta cobrança de uma fração régia e possibilidade 
- Duas conspirações do fim do século XVIII são comparadas quanto à participação social e às 
- Um estudo distingue a Guerra dos Emboabas, a dos Mascates e a Revolta de Vila Rica. Qual o
- Em 1808, a monarquia portuguesa muda seu centro administrativo para o Rio de Janeiro e abr
- Um texto associa o ano de 1817 a uma experiência republicana regional reprimida pelas forç
- Ao estudar a Carta de 1824, uma pessoa observa quatro poderes e eleições em etapas. Qual d
- Uma análise relaciona conflitos externos de D. Pedro I ao reconhecimento de um país em 182
- Uma província demanda mais espaço de legislação local após a abdicação. O Ato Adicional de
- Um arquivo cita revolta de 1835 organizada por africanos muçulmanos, muitos escravizados o
- Um debate em 1840 reúne apoiadores de antecipar o governo de D. Pedro II e defensores de r
- Uma pesquisadora compara a descentralização de 1834 com sua interpretação legislativa em 1
- Ao classificar movimentos pela fase política em que começaram e terminaram, qual afirmação
- Um relatório de 1844 atribui à mudança aduaneira aumento da receita estatal; décadas depoi
- Dois diplomas promulgados em setembro de 1850 afetaram o mercado de trabalho e a estrutura
- Um pesquisador diferencia uma crise diplomática com britânicos em 1862–1863 do conflito no
- Em 1871, nasce uma criança de mulher escravizada. Considerando a Lei nº 2.040, qual interp
- Uma síntese atribui a crise monárquica ao desgaste após 1870, à abolição e à organização r
- Ao estudar a formação da América portuguesa, uma pesquisadora encontra sociedades indígena
- Ao descrever o comércio do pau-brasil, um material afirma que o escambo garantia equivalên
- Uma exposição assinala que a criação do Governo-Geral, em 1549, procurou coordenar a admin
- Uma legenda atribui a Maurício de Nassau a chefia da Insurreição Pernambucana que, em 1644
- O registro de Palmares como uma rede de mocambos, com produção e defesa próprias, permite 
- O Tratado de Madri de 1750 manteve-se integralmente em vigor desde sua assinatura, sem anu
- A mineração setecentista fomentou vilas e circuitos de abastecimento, além de ampliar a fi
- Como a Corte portuguesa foi transferida para o Rio de Janeiro em 1808, o Brasil tornou-se 
- A elevação do Brasil a Reino Unido a Portugal e Algarves, em 1815, modificou sua posição i
- Segundo a Constituição de 1824, o Senado imperial tinha membros eleitos diretamente por su
- O acordo que encerrou a Guerra da Cisplatina em 1828 consolidou a independência uruguaia, 
- O Ato Adicional de 1834 estabeleceu federação de províncias soberanas e extinguiu o Poder 
- A Cabanagem envolveu forte participação popular no Pará entre 1835 e 1840 e chegou à tomad
- A Sabinada, ocorrida em 1835, foi dirigida por africanos muçulmanos e buscou abolir o cati
- Ao montar uma linha do tempo, um pesquisador coloca a Farroupilha com início em 1835 na Re
- A introdução da Tarifa Alves Branco em 1844 extinguiu o trabalho escravizado na cafeicultu
- As leis nº 581 e nº 601, de setembro de 1850, trataram de assuntos diferentes: repressão a
- Pela Lei do Ventre Livre de 1871, filhos de mulheres escravizadas tornavam-se livres somen
- A queda da monarquia em 1889 pode ser entendida pela convergência do desgaste militar após
- Brasil, Argentina e Uruguai formalizaram a Tríplice Aliança em 1864, antes de qualquer hos
```

## Pedido 22: Contabilidade de custos (`contabilidade.custos`, 716 tópicos de edital)

```
Matéria: contabilidade.custos (Contabilidade de custos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Terminologia, classificações e sistemas de acumulação": 24
- "2. Apropriação de custos, estoques, ordens, processos e ABC": 22
- "3. Absorção, custeio variável e comportamento do resultado": 18
- "4. Relação custo-volume-lucro, ponto de equilíbrio e segurança": 18
- "5. Mix de produtos, alavancagem operacional e decisões integradas": 18

Já existem (não repita):
- Uma indústria identifica a depreciação de suas máquinas de produção e as comissões de vend
- Ao fabricar móveis de modelos distintos, uma empresa mede a madeira incorporada por unidad
- Uma empresa compra matéria-prima a prazo por 12.000 e mantém todo o lote no almoxarifado a
- Uma fábrica suporta aluguel fixo de 20.000 por mês. Ela eleva a produção de 1.000 para 2.0
- Uma empresa recebe 8.000 em materiais para fabricação, dos quais utiliza 5.000 e mantém 3.
- Uma fábrica realiza encomendas individualizadas de equipamentos e outra produz garrafas pa
- Uma linha apresenta aparas normais inerentes ao corte e, separadamente, destruição extraor
- Uma indústria apresenta estoque inicial de produtos em elaboração de 4.000, CPP de 30.000 
- Uma empresa possui estoque inicial de acabados de 5.000, transfere CPA de 30.000 e encerra
- Em produção normal de 1.000 peças, uma empresa incorre em custo variável de fabricação de 
- Um produto é vendido a 60, com custo variável fabril de 24 e comissão variável de 6 por un
- Uma linha vende 500 unidades no mês; a margem de contribuição unitária, após custos e desp
- Uma empresa recebe 100 por peça vendida e incorre em 65 de gastos variáveis pertinentes po
- Uma atividade apresenta gasto fixo operacional de 21.000, preço unitário de 70 e gastos va
- Uma empresa tem índice de contribuição de 40% e custos e despesas fixos mensais de 12.000.
- O gasto fixo operacional mensal é 12.000 e a contribuição unitária é 30. Para obter lucro 
- Um estabelecimento vende 500 unidades no mês, sabendo que o ponto de equilíbrio é 350 unid
- Uma operação registra margem de contribuição total de 24.000 e lucro operacional de 8.000,
- Pelo ABC, a atividade preparação de máquinas consome 9.000 em 30 preparações. Um produto d
- Os custos fixos de fabricação somam 10.000 para capacidade normal de 1.000 unidades. A pro
- Sem estoques iniciais, a empresa fabrica 1.200 unidades e vende 1.000. Os fixos fabris de 
- Uma empresa vende X e Y na razão física constante 3:2. A margem unitária de X é 20 e a de 
- Um conjunto de vendas passa da proporção de dois X e um Y para um X e dois Y. As contribui
- Existe capacidade livre para atender pedido extraordinário de 200 unidades a 52 cada; o cu
- Em um período, material consumido foi 30.000, mão de obra direta 20.000 e CIF 15.000. A el
- Um comprador recebe insumos a prazo e o setor de produção somente utilizará o material no 
- Em uma fábrica, a rastreabilidade do material ao produto permite concluir que todo custo d
- Ao fabricar máquinas personalizadas para clientes com especificações distintas, uma empres
- Uma empresa mantém seguro que cobre exclusivamente a planta industrial; por não variar com
- A manutenção geral da fábrica, quando não rastreável economicamente a uma peça determinada
- Em um processo de corte, as aparas inerentes ao método produtivo e a destruição de materia
- Uma indústria inicia o mês sem produtos em elaboração, registra custo de produção do perío
- Mantidos o custo de produção do período e o estoque inicial em elaboração, um aumento do e
- Uma empresa possui mercadorias cujo custo registrado supera o valor realizável líquido ao 
- Uma planta tem custos indiretos fixos de 10.000 e capacidade normal de 1.000 peças; se pro
- Uma equipe prepara relatório gerencial por custeio variável, reconhecendo o custo fixo fab
- Para calcular margem de contribuição unitária de um produto, a empresa deve deduzir o cust
- No modelo custo-volume-lucro, o ponto de equilíbrio contábil iguala margem de contribuição
- Com margem unitária de 20 e gastos fixos de 12.000, uma empresa que vende exatamente 600 u
- Um produto tem contribuição unitária de 20, vendas de 1.000 unidades e gastos fixos de 16.
- Em análise multiproduto, utilizar participações físicas como pesos de índices percentuais 
- Uma indústria produz mais unidades do que vende e aumenta estoques, com custos fixos de fa
- Com custos fixos industriais de 12.000 e capacidade normal de 1.200 peças, uma produção de
- Uma empresa que adota direcionadores de preparação e inspeção pelo ABC pode incluir no val
- Um mix tem margem de 20 por X e de 10 por Y. Se passa de duas unidades X e uma Y por pacot
```

## Pedido 23: Provas no processo penal (`processo-penal.prova`, 713 tópicos de edital)

```
Matéria: processo-penal.prova (Provas no processo penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria geral, princípios, ônus e valoração probatória": 25
- "2. Prova ilícita, derivação, nulidades e garantias de admissibilidade": 17
- "3. Exame de corpo de delito, perícia e cadeia de custódia": 20
- "4. Interrogatório, confissão, ofendido, testemunhas e acareação": 18
- "5. Reconhecimento, documentos, indícios e avaliação integrada da prova": 20

Já existem (não repita):
- A acusação apresenta apenas declarações colhidas em investigação, não confirmadas em juízo
- Um processo contém testemunho de quem viu diretamente o fato e outra informação sobre obje
- Uma prova foi obtida com violação de norma constitucional que protege direito fundamental.
- Uma agressão deixou vestígios físicos disponíveis e o acusado confessou os fatos. Qual pro
- É necessária perícia criminal e há perito oficial com diploma de nível superior disponível
- Uma equipe identifica mancha suspeita no local e a descreve detalhadamente antes de retirá
- Durante interrogatório judicial, o acusado decide não responder às perguntas sobre o fato.
- Uma testemunha comparece à audiência trazendo notas com datas, mas não deseja ler depoimen
- Duas testemunhas prestam versões conflitantes sobre quem conduzia um automóvel no instante
- O procedimento de reconhecimento de pessoa desconhecida exige providência inicial para evi
- A acusação obtém um arquivo ilícito, mas outra investigação formal e autônoma já havia loc
- Uma lesão de crime com vestígios desapareceu antes da realização do exame técnico, sem que
- Em perícia não existe perito oficial disponível. A autoridade organiza exame por pessoas h
- O vestígio foi transportado ao laboratório e há registro de transferência formal de posse,
- Durante audiência, deseja-se perguntar à testemunha sobre fatos, permitindo posterior escl
- Um acusado declara em juízo que esteve no local do delito, mas nega participação e depois 
- A defesa contradita testemunha afirmando que ela é amiga íntima do ofendido. Pelo CPP, art
- Uma vítima reconheceu suspeito por fotografia única mostrada antes de descrever caracterís
- Uma testemunha identifica em juízo pessoa que conhece desde a infância, ao explicar que a 
- A acusação pretende demonstrar presença no local com dados comprovados de entrada, desloca
- Em processo por crime violento, a única identificação do acusado decorre de reconhecimento
- Após um reconhecimento inválido, surge filmagem íntegra obtida antes desse ato por fonte a
- Há falha na anotação de transporte de vestígio, mas o material continua identificável por 
- Em procedimento comum, a audiência encerra a oitiva das testemunhas e passa ao interrogató
- Duas pessoas chamadas separadamente para reconhecer um objeto concordam quanto à identific
- O juiz deve avaliar a prova produzida sob contraditório judicial, não podendo sustentar co
- De acordo com o CPP, art. 239, uma suspeita intuitiva, ainda sem circunstância conhecida e
- A prova obtida em violação a norma constitucional ou legal é inadmissível no processo pena
- Nos crimes que deixam vestígios, a confissão judicial livre e detalhada dispensa a realiza
- Pelo art. 200 do CPP, a confissão é divisível e retratável e seu valor depende do exame co
- A fonte independente prevista no art. 157, § 2º, pode ser presumida porque um investigador
- Quando os vestígios de um crime desapareceram antes do exame, o CPP, art. 167, admite prov
- O assistente técnico indicado pela parte assume automaticamente o lugar do perito oficial 
- Em depoimento de testemunha, o art. 212 do CPP atribui às partes a formulação direta de pe
- O art. 226 do CPP foi considerado pelo Tema 1.258 do STJ mera sugestão sem força obrigatór
- Qualquer lacuna documental na cadeia de custódia acarreta nulidade automática da prova e d
- A existência de prova fotográfica previamente produzida, válida e autônoma deve ser ignora
- Reconhecimento formalmente válido, realizado conforme o art. 226 do CPP, deve ainda guarda
- Um reconhecimento inicialmente sugestivo é necessariamente sanado se a pessoa reconhecedor
- A testemunha que apenas identifica em juízo alguém que conhecia pessoalmente muito antes d
- A Resolução CNJ nº 484/2022 exige, no art. 5º, que o procedimento de reconhecimento regist
- Quando documentos particulares são apresentados na ação penal, o CPP, art. 231, permite su
- A acareação do CPP, art. 229, pode envolver acusados, testemunhas e ofendidos nas combinaç
- A inquirição de testemunhas deve ocorrer com todas reunidas, permitindo que ouçam versões 
- O CPP, art. 182, permite que o juiz aceite ou rejeite laudo pericial no todo ou em parte, 
- Segundo o CPP, a busca pessoal independerá de mandado:
- Por determinação judicial, a entrada em domicílio para cumprimento de mandado de busca dev
- O mandado de busca domiciliar deve indicar, o mais precisamente possível, a casa e o nome 
- Quanto à busca pessoal em mulher, o CPP determina que:
```

## Pedido 24: Controle externo, tribunais de contas e tomada de contas especial (`controle-externo.tribunais-contas`, 712 tópicos de edital)

```
Matéria: controle-externo.tribunais-contas (Controle externo, tribunais de contas e tomada de contas especial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, modalidades de fiscalização e natureza das Cortes de Contas": 28
- "2. Organização do TCU, cortes estaduais e Ministério Público de Contas": 19
- "3. Competências, cautelares, pessoal, devido processo e Súmula 347": 21
- "4. Prestação e tomada de contas federais e tomada de contas especial": 19
- "5. Precedentes do STF e integração das competências de controle": 13

Já existem (não repita):
- Um órgão realiza auditoria para medir eficácia, eficiência e resultados de um programa de 
- Uma pessoa jurídica privada administra recursos recebidos da União para executar projeto s
- Ao examinar as contas anuais do Presidente da República, qual atribuição constitucional ca
- Um processo termina com imputação de débito ao responsável por dano ao erário e a decisão 
- Na escolha dos integrantes do TCU, quantos ministros existem e como se distribuem as indic
- Um tribunal de contas estadual será preenchido conforme o modelo de composição obrigatório
- Em processo de contas, um procurador do Ministério Público de Contas atua em defesa da ord
- A administração federal pratica ato de nomeação para cargo em comissão. Em regra, o art. 7
- Uma unidade entrega contas com impropriedade meramente formal, sem dano ao erário, e o Tri
- Uma auditoria detecta benefício tributário com impacto sobre a arrecadação, mas sem pagame
- Os responsáveis pelo controle interno tomam conhecimento de irregularidade grave relativa 
- No preenchimento de três vagas de escolha do Executivo estadual em tribunal de contas com 
- Uma lei estadual concede ao Ministério Público de Contas orçamento e autonomia administrat
- Em fiscalização, o Tribunal determina que uma unidade corrija ato ilegal; o prazo se esgot
- O processo de aposentadoria inicial chegou ao tribunal de contas há mais de cinco anos sem
- Uma unidade prestadora de contas federal publica informações de gestão, mas não está entre
- A autoridade federal identifica desvio de recursos públicos sob sua supervisão e precisa a
- Um prefeito atuou como ordenador de despesas e teve contas de gestão julgadas irregulares,
- Na apreciação das contas de prefeito para efeito de inelegibilidade, parecer prévio da Cor
- O TCU decide afastar a aplicação de lei federal em todos os processos da Administração, co
- Uma Corte identifica risco de dano grave em licitação antes de concluir a fiscalização e c
- O Congresso recebe notícia de irregularidade contratual e solicita providências ao Executi
- Um município deixou de comprovar aplicação de verba recebida em convênio federal e o gesto
- Ao examinar processo federal de tomada de contas especial em 2026, o responsável emprega e
- Uma conta anual foi julgada irregular com débito atualizado e juros; outra teve falha form
- Uma auditoria verifica que recursos federais foram repassados a organização privada. Como 
- O TCU atua como órgão hierarquicamente subordinado ao Congresso Nacional e integra o Poder
- Para julgar as contas anuais do Presidente, o Congresso Nacional conta com parecer prévio 
- O Tribunal de Contas da União é composto por sete ministros, dos quais quatro são escolhid
- Um gestor sofre imputação de débito e multa em decisão do TCU. A Constituição confere a es
- Uma unidade apresenta impropriedade formal sem dano, e o TCU a julga regular com ressalva.
- Na fiscalização de órgão que concedeu incentivo tributário com renúncia de receita, o trib
- Os procuradores do Ministério Público de Contas possuem independência funcional, razão pel
- No preenchimento do TCE estadual, quatro dos sete conselheiros devem ser escolhidos pelo P
- Ao apreciar a legalidade de admissão em cargo efetivo, o TCU atua conforme o art. 71, III.
- Se o controle interno toma conhecimento de irregularidade com recursos da União e deixa de
- Em 2026, o único normativo aplicável à instauração de tomada de contas especial federal co
- Uma unidade federal deve publicar informações de prestação de contas previstas na IN-TCU n
- Ao analisar as contas de gestão de prefeito que ordenou despesas, a Corte de Contas identi
- Para efeitos da inelegibilidade prevista na legislação eleitoral, o julgamento das contas 
- A Súmula 347 do STF autoriza que o Tribunal de Contas declare a inconstitucionalidade de l
- A Súmula Vinculante 3 ressalva a apreciação da legalidade de concessão inicial de aposenta
- Uma Corte determina providências sobre execução de contrato fiscalizado. O art. 71 permite
- O Tema 1287 do STF permite imputação administrativa de débito e multa por Tribunal de Cont
- Quando o TCU julga contas regulares com ressalva por falha formal sem dano, deve aplicar a
```

## Pedido 25: Jurisdição, competência e sujeitos do processo penal (`processo-penal.jurisdicao-competencia-sujeitos`, 712 tópicos de edital)

```
Matéria: processo-penal.jurisdicao-competencia-sujeitos (Jurisdição, competência e sujeitos do processo penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fontes e aplicação da lei processual penal": 25
- "2. Jurisdição e critérios de competência penal": 17
- "3. Conexão, continência, prevenção, incompetência e conflitos": 21
- "4. Juiz, Ministério Público, acusado, defensor e garantias de imparcialidade": 18
- "5. Assistente de acusação, auxiliares da Justiça e conflitos práticos": 19

Já existem (não repita):
- Durante processo iniciado antes de mudança legislativa, entra em vigor lei genuinamente pr
- Um dispositivo processual apresenta lacuna sobre situação análoga a outra regulada, sem pr
- Uma infração se consuma na cidade B após o agente iniciar execução na cidade A, sem incidê
- A execução de delito tentado começa na cidade X e seu último ato executório acontece na ci
- A vítima de estelionato por transferência de valores mora na cidade A e o beneficiário da 
- Duas pessoas são denunciadas como coautoras de uma única infração. Que instituto do CPP ju
- O magistrado designado para julgar processo penal foi anteriormente ouvido como testemunha
- Um acusado está ausente e não constituiu advogado para acompanhá-lo no processo penal. Qua
- O ofendido ingressa na ação penal pública como assistente do Ministério Público após parte
- O lugar exato do fato é desconhecido, mas o réu tem uma única residência comprovada na cid
- Em acusação exclusivamente privada, o local da infração é conhecido. O querelante pretende
- Uma pessoa comete infração exclusivamente para ocultar crime anterior. Caso se processem a
- Duas infrações distintas dependem da análise do mesmo documento e a prova da falsidade em 
- Dois juízos igualmente competentes praticam atos sobre o mesmo fato; um deles tomou antes 
- Duas autoridades judiciais declaram-se competentes para julgar exatamente o mesmo fato pen
- O juiz criminal tem amizade íntima comprovada com uma das partes e não se afastou espontan
- Uma servidora da secretaria judicial é suspeita de parcialidade por relação com parte. Qua
- Uma autoridade praticou, durante o exercício de função com foro constitucional, delito dir
- Investigações descrevem dois crimes conexos, sendo um de competência federal constituciona
- Uma anotação de estudo diz que o Júri prevalece em toda conexão com outro órgão da jurisdi
- Há conexão entre infração de competência comum e crime que cabe à Justiça Militar, mas não
- A competência do Tribunal do Júri fora reconhecida inicialmente por conexão. Na fase perti
- Um corréu em processo penal público deseja ingressar formalmente como assistente do Minist
- Membro do Ministério Público participou de diligências investigatórias e, com elementos su
- Uma pessoa nomeada para realizar perícia já prestou depoimento no mesmo processo e opinou 
- Uma lei estritamente processual penal passa a vigorar durante processo em andamento; pelo 
- Para qualquer delito tentado, ainda que conhecido o lugar do último ato de execução, o CPP
- Duas pessoas acusadas de praticar a mesma infração penal podem estar abrangidas pela conti
- O CPP permite processar e julgar acusado foragido sem defensor, desde que a ausência seja 
- Em ação penal pública, o ofendido pode ser admitido como assistente do Ministério Público,
- O artigo 254 do CPP enumera como impedimento objetivo a amizade íntima do juiz com qualque
- Em estelionato por transferência de valores, o art. 70, § 4º, do CPP considera o domicílio
- Se uma infração permanente ocorre em territórios de duas jurisdições distintas, a competên
- Quando a prova de um crime influencia a prova de outro crime distinto, pode haver conexão 
- O art. 79 do CPP determina reunião e julgamento conjunto de crimes da jurisdição comum e m
- Conforme o art. 115 do CPP, a parte interessada, o órgão ministerial junto a um dos juízos
- Um membro do Ministério Público que participou de diligência investigatória fica automatic
- Nos termos do art. 281 do CPP, intérpretes são equiparados a peritos para todos os efeitos
- O STF, em março de 2025, determinou que a prerrogativa de função cesse obrigatoriamente ao
- Segundo a Súmula 122 do STJ, crimes conexos de competência federal e estadual são, em regr
- Após a alteração do CPP pela Lei nº 15.358/2026, a regra do art. 78, I, mantém prevalência
- O art. 80 do CPP admite separação facultativa de processos conectados quando o excesso de 
- O abandono do processo pelo defensor sem justo motivo conserva, no caput do art. 265 do CP
- Ao decidir sobre admissão de assistente de acusação, o juiz deve ouvir previamente o Minis
- No processo penal, o fato de uma pessoa nomeada perita já ter deposto sobre o objeto da pe
```

## Pedido 26: Direitos de nacionalidade (arts. 12 e 13) (`direito-constitucional.nacionalidade`, 707 tópicos de edital)

```
Matéria: direito-constitucional.nacionalidade (Direitos de nacionalidade (arts. 12 e 13))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, cidadania e brasileiros natos: nascimento, registro e opção": 25
- "2. Brasileiros naturalizados e igualdade dos portugueses residentes": 18
- "3. Igualdade, exceções constitucionais, brasileiros no exterior e estrangeiros": 20
- "4. Perda, reaquisição, dupla nacionalidade, apatridia e polipatria": 20
- "5. Idioma, símbolos constitucionais e resolução de casos integrados": 17

Já existem (não repita):
- Uma criança nasceu no Brasil, filha de dois estrangeiros que trabalham em empresas privada
- Um casal de estrangeiros encontra-se no Brasil em missão oficial a serviço do país de naci
- Uma brasileira dá à luz no exterior durante missão oficial desempenhada a serviço da Repúb
- Um bebê nasce no exterior de mãe brasileira que não está a serviço do Brasil. Seu nascimen
- Uma pessoa nascida fora do Brasil, filha de brasileiro, sem registro consular competente, 
- Uma pessoa originária de país de língua portuguesa pretende adquirir nacionalidade brasile
- Um estrangeiro de país não lusófono mora há dezesseis anos ininterruptos no Brasil, não te
- Uma portuguesa possui residência permanente no Brasil e requer o reconhecimento do Estatut
- Uma brasileira naturalizada pretende ocupar função constitucionalmente reservada ao nato. 
- Um município pretende criar seu próprio hino e uma bandeira local, mantendo os símbolos fe
- Uma lei ordinária estabelece que brasileiros naturalizados não podem exercer função públic
- Um brasileiro naturalizado é solicitado em extradição por crime comum que, segundo o enunc
- Uma brasileira que reside no exterior procura auxílio consular em processo judicial local.
- Um brasileiro sujeito ao alistamento militar mora no exterior no ano em que completa 18 an
- Em 2026, uma brasileira nata obtém por naturalização outra nacionalidade e não pede perda 
- Em processo de nacionalidade derivada, foi comprovada fraude ligada à obtenção da naturali
- Uma brasileira é reconhecida como nacional apenas pelo Brasil e apresenta pedido expresso 
- Uma brasileira nata renunciou à nacionalidade mediante pedido expresso aceito, sem se torn
- Uma criança nascida no Brasil de pais estrangeiros sem serviço oficial recebe nacionalidad
- Uma pessoa nasceu em país que não lhe atribui nacionalidade territorial e não recebeu naci
- Uma criança nascida no exterior foi adotada por brasileira e registrada em repartição cons
- Um brasileiro naturalizado é solicitado em extradição por comprovado envolvimento em tráfi
- Um português com estatuto de igualdade civil pretende obter gozo de direitos políticos no 
- Um material lista os símbolos da República e inclui a moeda nacional como elemento constit
- Uma criança brasileira nata não pode votar em razão da idade. Um examinador conclui que el
- Uma criança nasce em território brasileiro de pais estrangeiros que não estão a serviço de
- Uma pessoa nascida no exterior de mãe brasileira, regularmente registrada em repartição co
- Para originários de países de língua portuguesa, o art. 12, II, a, destaca residência inin
- Um estrangeiro com exatamente quinze anos ininterruptos de residência no Brasil, sem conde
- A atribuição de igualdade de direitos a português residente no Brasil, sob reciprocidade, 
- O art. 13, §1º, considera como símbolos nacionais a bandeira, o hino, o selo, as armas e a
- Uma lei ordinária não pode criar livremente novos cargos reservados a brasileiros natos, p
- Pelo art. 5º, LI, brasileiro naturalizado só pode ser extraditado por comprovado envolvime
- Uma brasileira que reside no exterior pode buscar assistência consular para orientações e 
- Um brasileiro residente no exterior, sujeito às obrigações de alistamento, fica delas disp
- Após a EC 131/2023, uma brasileira pode adquirir outra nacionalidade sem que essa aquisiçã
- No art. 12, §4º, I, o cancelamento da naturalização por fraude pode ser determinado defini
- A ausência de reconhecimento da nacionalidade de uma pessoa por todos os Estados, conforme
- Um brasileiro que seria considerado apátrida se perdesse a nacionalidade brasileira pode o
- Uma pessoa nascida no Brasil, reconhecida aqui como brasileira nata por jus soli e também 
- Uma pessoa continua juridicamente nacional de determinado Estado, mas perdeu seu passaport
- No Tema 1.253, decidido em 2026, foi assegurada a nacionalidade brasileira originária à pe
- Um português com igualdade de direitos civis, residente no Brasil há um ano, recebe automa
- A EC 131/2023 inseriu no art. 12, §5º, a previsão de que renunciar à nacionalidade pelo §4
- O art. 13, §2º, admite símbolos próprios apenas dos Estados e dos Municípios, excluindo ex
```

## Pedido 27: Processo Civil: jurisdição e competência (`processo-civil.jurisdicao-competencia`, 702 tópicos de edital)

```
Matéria: processo-civil.jurisdicao-competencia (Processo Civil: jurisdição e competência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Jurisdição civil: função, princípios, poderes e formas de exercício": 24
- "2. Limites da jurisdição nacional e cooperação internacional": 14
- "3. Competência interna: território, matéria, pessoa, valor, função e Justiça Federal": 24
- "4. Competência absoluta e relativa; eleição de foro e declaração de incompetência": 19
- "5. Conexão, continência, prevenção, cooperação e conflitos de competência": 19

Já existem (não repita):
- Uma ação civil foi distribuída a órgão jurisdicional que integra regularmente o Poder Judi
- Um juiz determina providência coercitiva para assegurar o cumprimento de ordem legítima, c
- Interessados pedem ao Judiciário a expedição de alvará nos termos do art. 725 do CPC, sem 
- Duas empresas capazes pretendem submeter a árbitro disputa acerca do preço de entrega de m
- Uma pessoa estrangeira domiciliada em Porto Alegre é demandada em cobrança contratual de n
- Uma sucessão de pessoa domiciliada no exterior envolve partilha de apartamento situado em 
- Uma ação de cobrança foi ajuizada no exterior antes de demanda idêntica perante juízo bras
- Uma decisão estrangeira precisa produzir efeitos jurisdicionais no Brasil por via ordinári
- Uma ação de cobrança fundada em direito pessoal é proposta, sem foro especial ou convenção
- Uma demanda possessória versa sobre terreno localizado em Recife. As partes contratam foro
- Uma pessoa pretende obter alimentos e reside em Salvador, ao passo que o devedor mora em V
- Em litígio cível de reparação contratual figura como ré autarquia federal, inexistindo exc
- Uma cláusula contratual escrita e expressa escolhe comarca sem conexão com domicílio, resi
- Em causa de competência territorial relativa ordinária, sem foro aleatório ou cláusula abu
- O juiz reconhece incompetência depois de deferir uma medida judicial. Antes de qualquer re
- Duas ações propostas em comarcas distintas apresentam causa de pedir comum, mas pedidos di
- Duas ações têm as mesmas partes e causa de pedir. A primeira exige integral cumprimento co
- Um contrato internacional contém cláusula de foro exclusivamente estrangeiro. A ação, entr
- Dois ex-cônjuges obtiveram sentença estrangeira de divórcio consensual, sem partilha de pa
- Depois da distribuição de uma ação, lei de organização judiciária extingue o órgão que a p
- Processo cível tramita no juízo estadual; empresa pública federal ingressa alegando intere
- Em contrato empresarial, foi eleita comarca vinculada ao local da obrigação, mas há prova 
- O autor ajuíza demanda em comarca sem vínculo com residência ou domicílio de qualquer part
- Duas ações, com mesmas partes e causa de pedir, possuem pedidos parcialmente e totalmente 
- Dois juízos consideram-se competentes e passam a divergir especificamente sobre reunir ou 
- Em uma atividade de classificação processual, afirma-se que a jurisdição civil é exercida 
- Uma controvérsia sobre direitos da personalidade estritamente indisponíveis pode ser neces
- A expedição de alvará judicial pode integrar procedimento de jurisdição voluntária, no qua
- Uma demanda estrangeira anteriormente ajuizada determina litispendência obrigatória no Bra
- Uma consumidora domiciliada no Brasil litiga sobre contrato internacional de consumo. Seu 
- Uma sentença estrangeira de divórcio consensual, sem partilha de bens brasileiros, somente
- Causa cível entre Estado estrangeiro e pessoa domiciliada no Brasil, nos termos do art. 10
- Uma sociedade de economia mista federal é parte em ação contratual e, por essa qualidade i
- Em ação de reparação de dano decorrente de acidente de veículo, o autor pode, segundo o ar
- A eleição de foro territorial por instrumento escrito, mesmo sem vínculo com domicílio, re
- A competência de determinada causa foi definida quando a inicial foi distribuída. Posterio
- A regra de incompetência relativa da Súmula 33 impede qualquer declinação de ofício mesmo 
- Em ação atribuída por lei a outro órgão por competência absoluta, o juiz pode reconhecer a
- Depois da citação, o juiz pode em qualquer circunstância declarar de ofício a ineficácia d
- Reconhecida incompetência do juízo após decisão urgente já proferida, a legislação preserv
- Duas causas são conexas, mas uma delas já foi sentenciada. Por força do art. 55, § 1º, o C
- Dois processos sem pedido nem causa de pedir comuns podem ser reunidos quando, julgados se
- Se ação contida, com pedido menor, foi proposta antes da ação continente, que abrange inte
- Em processos propostos separadamente e passíveis de reunião, considera-se prevento o juízo
- O art. 66 do CPC reconhece conflito de competência apenas quando dois juízos afirmam ou ne
```

## Pedido 28: Criptografia, certificação digital e segurança de redes (`informatica.criptografia-certificacao`, 701 tópicos de edital)

```
Matéria: informatica.criptografia-certificacao (Criptografia, certificação digital e segurança de redes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos criptográficos, algoritmos e proteção de dados": 21
- "2. Hash, assinatura digital e verificação de integridade": 20
- "3. Certificados digitais, PKI, ICP-Brasil e autenticação": 24
- "4. TLS/HTTPS, protocolos seguros e ciclo de sessões": 17
- "5. VPN, IPsec, firewall, IDS, IPS, SIEM e segurança em camadas": 18

Já existem (não repita):
- Um setor precisa cifrar arquivos extensos e já dispõe de uma chave secreta compartilhada d
- Uma especificação técnica descreve AES-256 aplicado à cifragem de um disco e pergunta qual
- Uma pessoa deseja enviar um segredo a destinatário com par de chaves assimétricas já valid
- Após transferir um arquivo, uma equipe calcula SHA-256 antes e depois para comparar conteú
- Um documento eletrônico foi assinado por titular de certificado válido, com esquema de ass
- Uma pessoa comparece ao ponto de atendimento de certificação, apresenta documentos e tem s
- Uma aplicação precisa verificar a identidade pública vinculada a uma chave e recebe certif
- Ao acessar site por HTTPS, o navegador verifica o certificado do servidor e inicia uma con
- Uma organização emprega TLS na conexão com seu portal, mas o banco grava registros em text
- Duas aplicações que compartilham chave secreta precisam detectar modificação e autenticar 
- Ao validar assinatura de arquivo, a aplicação percebe que o certificado do titular foi rev
- Uma pessoa solicita certificado A3 e recebe proposta de uso de chave protegida em HSM remo
- Uma conta institucional exige senha e uso de smartcard que detém chave criptográfica. O us
- Um serviço permite envio antecipado de requisição na retomada de sessão TLS 1.3 por mecani
- Um cliente inicia conexão TLS 1.3 com ClientHello e o servidor responde com ServerHello. Q
- Um navegador abre HTTPS com certificado cuja cadeia é reconhecida, mas o certificado foi e
- Dois gateways interligam redes privadas pela Internet e um pacote IP completo é encapsulad
- Na implantação de VPN baseada em IPsec, a equipe configura IKEv2 antes de transmitir dados
- Em uma rede, o sensor identifica tráfego compatível com tentativa de invasão e descarta au
- Uma equipe quer reunir logs do firewall, das aplicações e dos serviços de autenticação e c
- Uma imagem é usada para ocultar dados alterando discretamente o bit menos significativo de
- Um analista estuda cifra clássica de substituição monoalfabética e observa que certas letr
- Uma conexão TLS 1.3 usa acordo efêmero de chaves com forward secrecy, e a chave privada de
- Uma pessoa envia e-mail pelo serviço que usa TLS entre servidores, mas deseja que o conteú
- Um projeto de proteção de rede adota firewall stateful, catraca de acesso à sala de servid
- Uma unidade utiliza AES para proteger arquivos em disco e guarda a chave secreta em serviç
- Uma equipe calcula SHA-256 de documento para acompanhar alterações. Como o digest é cripto
- No processo usual de emissão de certificado digital da ICP-Brasil, a Autoridade de Registr
- Uma política criptográfica trata MD5, SHA-1 e SHA-256 como igualmente recomendados para cr
- Uma aplicação retoma sessão TLS 1.3 usando dados 0-RTT. A redução da latência pode trazer 
- Uma VPN foi implementada como túnel entre a residência de um usuário e gateway corporativo
- Um sensor observa eventos de rede suspeitos e envia alertas a uma central sem interromper 
- A MP nº 2.200-2/2001 exige que todo documento eletrônico seja assinado obrigatoriamente co
- Um arquivo apresenta hash SHA-256 de 32 bytes. Esse resumo equivale a 256 bits e, na repre
- Em conexão IPsec, AH e ESP prestam obrigatoriamente o mesmo serviço de confidencialidade, 
- Uma central reúne registros de firewall, servidores, autenticação e aplicações para correl
- No handshake TLS 1.3, a autenticação do servidor requer que ele transmita sua chave privad
- Uma pessoa autentica-se com senha e token físico criptográfico, mas recebe apenas autoriza
- Por centralizar registros e correlacioná-los, toda plataforma SIEM deve necessariamente es
- A especificação TLS 1.3 foi atualizada pela RFC 9846, publicada em julho de 2026, que subs
- No IPsec em modo túnel, a proteção mantém apenas o cabeçalho IP externo original, sem enca
- Um sensor de rede alerta para varreduras sem interrompê-las e um serviço SIEM cruza esses 
- Uma imagem teve bits LSB modificados para transportar informação secreta. Isso constitui n
- Um firewall stateful pode acompanhar o estado de conexões para decidir sobre pacotes, enqu
- Um certificado de assinatura digital é mantido válido durante toda sua vigência e seu titu
```

## Pedido 29: Avaliação da aprendizagem e gestão escolar (`educacao.avaliacao-gestao-escolar`, 694 tópicos de edital)

```
Matéria: educacao.avaliacao-gestao-escolar (Avaliação da aprendizagem e gestão escolar)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação da aprendizagem: funções, critérios e instrumentos": 20
- "Devolutivas, registros e documentação pedagógica": 20
- "Avaliação institucional, Saeb, Ideb e uso de indicadores": 20
- "Gestão democrática, projeto político-pedagógico e conselhos": 20
- "Coordenação pedagógica, famílias e sistemas de gestão": 20

Já existem (não repita):
- A avaliação diagnóstica busca identificar conhecimentos prévios e necessidades de aprendiz
- Toda avaliação realizada sem atribuição de nota é, obrigatoriamente, uma avaliação formati
- A avaliação somativa pode sintetizar resultados ao final de uma unidade ou período, segund
- A elaboração de critérios claros de avaliação permite tornar compreensível o que se espera
- O Saeb é uma iniciativa de avaliação externa em larga escala coordenada pelo Inep, que uti
- O Ideb é calculado exclusivamente a partir das notas individuais obtidas em provas aplicad
- O projeto político-pedagógico deve expressar escolhas e prioridades da escola e orientar a
- A participação das famílias e da comunidade é incompatível com a gestão democrática, que d
- Uma prova aplicada ao final de uma sequência pode gerar informação formativa se o professo
- Uma rubrica analítica dispensa a identificação dos objetivos de aprendizagem, pois os desc
- Um registro que diga apenas que certo estudante é desmotivado contém descrição factual suf
- A documentação pedagógica pode reunir produções sucessivas, falas e observações para evide
- As matrizes de referência do Saeb equivalem à íntegra dos conteúdos do currículo escolar e
- A autoavaliação institucional participativa fica incompleta quando encerra a coleta de opi
- O conselho escolar e o conselho de classe têm, necessariamente, as mesmas competências adm
- Como o Ideb sintetiza fluxo e desempenho, duas escolas com idêntico valor do índice podem 
- Os questionários contextuais do Saeb, isoladamente, permitem concluir que determinado fato
- O fortalecimento da autonomia escolar, em regime democrático, autoriza o conselho a descon
- A análise coletiva de baixo desempenho exige considerar tanto as evidências de aprendizage
- Como sistemas eletrônicos produzem relatórios padronizados, a exposição pública de históri
- Antes de abordar frações, uma professora pede desenhos que representem 1/2 e 1/3 para conh
- Qual opção expressa uma devolutiva mais coerente com a avaliação formativa de um texto arg
- Qual combinação representa corretamente as fontes centrais de informação do Ideb?
- Em uma escola pública, qual iniciativa caracteriza de modo mais evidente a gestão democrát
- Qual documento orienta a identidade, as prioridades e as ações educativas de uma escola, d
- Uma função central da coordenação pedagógica na escola é:
- Em uma atividade de ciências realizada em grupos, a professora quer avaliar o domínio indi
- Ao organizar uma rubrica para apresentação oral, a equipe distingue o padrão de qualidade 
- Uma professora registra: "na roda, Ana explicou o raciocínio usando três exemplos". Qual p
- Em relação à matriz de referência do Saeb, assinale a afirmação correta.
- Uma instituição realizou questionários com famílias e docentes sobre clima escolar, mas nã
- Uma responsável trabalha em turno noturno e não consegue acessar o aplicativo escolar. Qua
- Após prova final, a equipe constata que alunos dominaram conceitos centrais, mas mantivera
- Uma escola identifica queda no índice de proficiência e decide que a causa é exclusivament
- Uma rede compara escolas que têm Ideb semelhante e conclui que são equivalentes em qualida
- Um conselho escolar aprova prioridade pedagógica após debates, mas a decisão exige despesa
- A escola construiu PPP participativamente, mas o documento não é usado nas reuniões, decis
- Ao rever portfólios, um coordenador identifica ganhos em procedimentos, mas pouca evolução
- Sobre os resultados do Saeb, qual afirmação mostra leitura metodológica adequada?
- Uma plataforma gera relatório automático de estudantes em risco por ausência, a partir de 
```

## Pedido 30: Vocabulário, dicionários e abreviações (`portugues.vocabulario-dicionarios-abreviacoes`, 693 tópicos de edital)

```
Matéria: portugues.vocabulario-dicionarios-abreviacoes (Vocabulário, dicionários e abreviações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Vocabulário em movimento: neologismos e arcaísmos": 25
- "2. Estrangeirismos e latinismos no uso real": 26
- "3. Tipos de dicionário e consulta responsável": 19
- "4. Anatomia do verbete e interpretação de marcas": 19
- "5. Abreviaturas, siglas, acrônimos e símbolos": 11

Já existem (não repita):
- Num relatório sobre comunicação digital, lê-se: «O vídeo viralizou em poucas horas». Consi
- Em romance ambientado em século passado, uma personagem diz «Vosmecê pode entrar». O efeit
- Um revisor compara «software» e «futebol» numa notícia. Qual afirmação descreve corretamen
- Uma equipe recebeu a orientação «realizar a inspeção in loco». Nesse enunciado, a expressã
- Antes de receber os resultados de um experimento, uma pesquisadora afirma: «Não é adequado
- Ao preparar um estudo, uma pessoa quer descobrir a origem histórica e as transformações de
- Alguém consulta o Volp para confirmar a grafia e a classe gramatical de uma palavra. Qual 
- Num verbete didático aparece: «ponte, s.f. ... 3. fig. ligação entre pessoas». Na terceira
- Em uma lista de referências a dispositivos de um texto, o redator usou «art. 4º» e preciso
- Numa tabela, a equipe deve registrar uma massa de cinco quilogramas segundo a simbologia d
- Depois de coletar entrevistas e comparar documentos, a autora afirmou: «A avaliação foi co
- Um estudante lê «manga rasgada» e «manga madura» e procura uma obra que apresente os signi
- Um dicionário apresenta «manga¹» e «manga²» como entradas distintas; em «manga¹», surgem s
- Para estudar um problema pontual, foi instituído um grupo ad hoc. A expressão latina desta
- Em um texto institucional aparece pela primeira vez a denominação «Organização das Nações 
- Um editor exige que determinada passagem seja reproduzida ipsis litteris na ata. Para aten
- Em fotografia histórica, uma fachada traz a inscrição «Pharmacia Central». Um comentário a
- Um redator quer substituir «essencial» em «uma etapa essencial ao procedimento» e encontra
- Ao corrigir «A amostra foi mantida a 20 °Cs durante 2 hs.», uma pessoa deve aplicar a regr
- Uma análise registra que «printar» se relaciona ao inglês «print» e que sua forma verbal s
- Uma palavra aparece registrada no Volp, mas ainda não tem verbete disponível no DLP eletrô
- Num mesmo documento, aparecem «Anac» e «IBGE». Pelas orientações de grafia de siglas e acr
- Um repertório apresenta «coroa, s.f. ... 4. fig. ...» e, em outra entrada, «coroa, reg. ..
- Um quadro técnico traz «10 M» para representar dez metros e «10 K» para expressar dez quil
- Um período termina com enumeração: «Foram comprados cadernos, lápis, pastas etc.». Sobre a
- Num texto de tecnologia, «nuvem» denomina armazenamento remoto de arquivos. O emprego ilus
- Em uma carta antiga, a grafia «pharmacia» comprova que a palavra contemporânea «farmácia» 
- No comando «o valor deve ser calculado per capita», a locução significa que o cálculo deve
- Se um relatório registra «8 kgs de material», a grafia é recomendada porque símbolos de un
- Num verbete, a indicação «s.f.» identifica uma categoria gramatical, enquanto a marca «fig
- Uma tradução traz «a comissão preservará o status quo». No contexto, o latinismo designa n
- Ao procurar a tradução de «manga» num dicionário bilíngue, convém primeiro observar se o t
- Se um verbete de «rede» apresenta três acepções numeradas, a terceira é obrigatoriamente a
- Conforme a convenção de abreviaturas textuais, «Sr.» e «Srs.» podem distinguir singular e 
- Uma pesquisa quer saber a origem histórica de «janela». Como o Volp contém a grafia da pal
- Na formulação «uma condição sine qua non para concluir o projeto», o latinismo qualifica e
- Em verbete de caráter lexicográfico, a remissão «ver X» assegura que X substitui a entrada
- Ao descrever «printar», uma análise pode reconhecer simultaneamente a origem estrangeira d
- Em «o debate ocorreu grosso modo como previsto», o latinismo informa que o relato foi tran
- Na apresentação de grandeza em documento técnico, «4 L» pode representar quatro litros; a 
- Por estabelecer que siglas, símbolos e abreviaturas reguladas podem ter maiúsculas, a Base
- Uma lista de sinônimos para «claro» não autoriza trocar essa palavra por qualquer termo li
- Se o verbete indica «reg.» apenas na acepção 2, a marca regional deve ser automaticamente 
- Num texto formal, a abreviatura «etc.» e o símbolo «kg» têm natureza distinta: a primeira 
- Se uma forma é marcada «ant.» em certo dicionário, seu emprego por escritor contemporâneo 
```

## Pedido 31: Interpretação de texto em inglês (`lingua-inglesa.interpretacao-texto-ingles`, 692 tópicos de edital)

```
Matéria: lingua-inglesa.interpretacao-texto-ingles (Interpretação de texto em inglês)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estratégias de leitura, informação explícita e inferência": 21
- "2. Vocabulário em contexto, falsos cognatos e expressões": 21
- "3. Gramática para interpretar: tempos, modais, condicionais e coesão": 15
- "4. Organização discursiva, propósito, tom e figuras de linguagem": 19
- "5. Leitura de textos técnicos e administrativos em inglês": 24

Já existem (não repita):
- Leia o trecho: 'The city opened three small libraries last year. The project aims to make 
- Um aviso informa: 'Registration closes on 18 March. Late applications will not be accepted
- Leia: 'The team postponed the launch because the final safety checks were incomplete.' O q
- Leia: 'The solution might reduce costs, but it has not been tested.' Qual paráfrase preser
- Um e-mail diz: 'We eventually found the missing report after checking several folders.' Ne
- Leia: 'Anna and Luis submitted their proposals. The former suggested training; the latter 
- O texto afirma: 'The team will carry out the inspection on Monday.' Qual interpretação de 
- Leia: 'Although the room was small, the meeting was productive.' A função principal de 'al
- Uma nota informa: 'If the network is unavailable, save your work locally and try again lat
- Leia: 'The report describes current expenses and suggests three changes. It does not evalu
- Um artigo apresenta o argumento de que ciclovias melhoram a mobilidade e acrescenta: 'For 
- Leia: 'The device is sensitive to temperature changes, so technicians use a protective cas
- Uma apresentação descreve a nova plataforma como 'a bridge between researchers and local c
- Considere o registro técnico: '10:05 ERROR: connection lost. 10:06 retry scheduled for 10:
- Em comunicado lê-se: 'The newsroom demanded further evidence before publishing the claim.'
- Um memorando registra: 'The committee put off the discussion until Friday, but it did not 
- Leia: 'If the inspectors had received the data earlier, they would have completed the revi
- Leia: 'The author calls the tiny pilot results a drop in the ocean and warns against broad
- Um artigo afirma: 'The findings are promising; however, the sample was small and the metho
- Leia a instrução: 'Do not restart the device unless the update has finished.' Qual paráfra
- Um relatório registra: 'The data tells a different story: fewer people used the service af
- Uma documentação informa: 'The old interface is deprecated, but it will remain available u
- Após uma implantação malsucedida, o e-mail diz: 'A brilliant start—three crashes before lu
- Em um manual consta: 'The feature works only if the account is verified. Additional permis
- Compare: Text A: 'A pilot reduced waiting time in two clinics.' Text B: 'Results from the 
- Leia: 'The article discusses why many residents use buses rather than cars.' Para identifi
- O aviso 'Only two workshops are open on Sunday' permite concluir que pelo menos três ofici
- Em 'The company actually reduced its prices', o advérbio 'actually' pode ser entendido com
- Na frase 'Although the road was flooded, the bus arrived', o conector 'although' introduz 
- No enunciado 'The application might work offline', a palavra 'might' impede apresentar o f
- Em 'The authors turned down the request', a expressão 'turned down' confirma que os autore
- No texto 'The platform is a bridge between distant teams', a palavra 'bridge' pode express
- Leia o log: '11:00 warning issued; 11:01 restart scheduled.' O segundo evento demonstra qu
- Na sequência 'The printer had stopped before the technician arrived', o past perfect local
- Um resumo afirma 'Two methods were tested. The former was faster.' Logo, o método mais ráp
- Leia: 'For example, several small clinics reduced waiting times.' A expressão inicial apre
- Em documentação técnica, 'The feature is deprecated' informa necessariamente que o recurso
- Na frase 'The newsroom approved the final version', 'newsroom' pode designar, por metoními
- O enunciado 'If the server had responded, the process would have finished' descreve fato r
- Compare: A: 'The trial improved service in one town.' B: 'The national program has not bee
- Em 'The update is required only if the account is verified', a conta verificada constitui 
- Um artigo chama certa proposta de 'a silver bullet' e depois ressalta que ela não resolve 
- Leia: 'The files were reviewed before publication.' A voz passiva prova que o responsável 
- Em documentação de incidentes, 'The team applied a workaround; the permanent fix is being 
- Após relatar quatro falhas, um texto comenta 'What a perfect launch!' Essa frase deve ser 
```

## Pedido 32: Prova (`processo-civil.prova`, 691 tópicos de edital)

```
Matéria: processo-civil.prova (Prova)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria geral da prova, ônus e admissibilidade": 22
- "2. Produção antecipada, documentos, depoimentos e audiências": 36
- "3. Prova pericial, perito, laudo e inspeção judicial": 16
- "4. Sentença, fundamentação, efeitos e coisa julgada": 14
- "5. Liquidação, cumprimento de sentença e integração dos temas": 12

Já existem (não repita):
- Em ação de cobrança, o autor afirma ter entregue mercadorias e o réu sustenta que quitou i
- Em processo sobre reparação de bem, foi requerido exame de peça mecânica sem necessidade d
- Um consumidor pretende documentar defeito de produto antes de decidir se entrará em juízo,
- Em audiência, a parte intimada pessoalmente a depor e advertida das consequências não comp
- A parte autora ajuizou ação com documentos essenciais, e depois surgiu recibo de fato ocor
- Um parecer traz somente conclusão de que uma construção vale R$ 900 mil, sem descrever mét
- Uma decisão encerra a fase de conhecimento resolvendo integralmente o mérito. Segundo o ar
- O juiz precisa observar diretamente danos em fachada de imóvel e decide visitar o local pa
- Em cumprimento de sentença que condenou réu a pagar R$ 40 mil, o devedor deixa transcorrer
- As partes discutem contrato e uma delas apresenta laudo de outro processo sobre a mesma má
- Em litígio, a única cópia de dados técnicos fica sob controle do réu e o autor demonstra d
- A parte pretende obter contrato em poder do adversário e o descreve por categoria, finalid
- A inicial é regular e o caso admite autocomposição. O juiz vai designar audiência de conci
- As partes foram intimadas da nomeação de perito especializado e desejam apontar suspeição,
- Perito nomeado atrasou o laudo sem motivo legítimo. O juiz avalia sua substituição e as co
- O julgador publica sentença, mas percebe que a soma aritmética de parcelas no dispositivo 
- Uma sentença condenatória estabelece todas as parcelas e seus índices, restando apenas atu
- Para sustentar ação indenizatória, a parte apresenta dados obtidos por invasão não autoriz
- Em demanda patrimonial sobre direito disponível, a parte confessou fato desfavorável, mas 
- Duas partes plenamente capazes celebraram negócio disponível e, em conjunto, pretendem sel
- Ao rejeitar indenização, o juiz cita um artigo legal, sem explicar como seus elementos se 
- Uma sentença condena ao pagamento de prestação em dinheiro; a parte vencedora pretende reg
- Em sentença de mérito, uma questão prejudicial incidental foi expressamente decidida após 
- Uma condenação reconhece indenização por perda de produção, mas a quantificação depende de
- Intimado no cumprimento definitivo para pagar R$ 50 mil, o executado paga voluntariamente 
- Em ação de cobrança, a parte autora junta documento sobre o fornecimento, e o réu apresent
- Uma pessoa requer produção antecipada de prova para verificar situação que possa evitar fu
- A parte requerida a prestar depoimento pessoal recebeu intimação pessoal com advertência d
- Ao designar audiência de conciliação do procedimento comum, o juiz fixa a sessão para dali
- O perito apresentou laudo com objeto, análise técnica, método devidamente esclarecido e re
- Em sentença condenatória já liquidada, a parte devedora deixa de pagar em quinze dias. O a
- O juiz admite laudo produzido em outra demanda para esclarecer controvérsia atual, oportun
- Um juiz redistribui ônus probatório diretamente na sentença, sem aviso anterior nem possib
- Uma parte requer exibição de uma categoria de documentos, descrevendo-a com precisão possí
- O juiz nomeou profissional sem conhecimento técnico suficiente ao objeto e, após impugnaçã
- Um juiz constata que laudo pericial existente é insuficiente para resolver ponto técnico d
- Uma sentença de mérito apresenta relatório e dispositivo, mas ignora fundamento jurídico e
- Diante de condenação ilíquida que exige produção de prova de fato novo para a quantificaçã
- Em processo civil, dados obtidos mediante invasão indevida de conta digital tornam-se admi
- As partes, plenamente capazes, litigam sobre direito disponível e escolhem em comum acordo
- Em ação sobre direito disponível, uma confissão judicial expressa foi prestada por uma par
- Questão prejudicial decidida expressamente e necessária ao julgamento de mérito pode, sob 
- Sentença publicada contém erro material de soma e o juiz o corrige de ofício. A retificaçã
- No cumprimento definitivo de sentença por R$ 70 mil, o executado paga R$ 50 mil no prazo d
- Em condenação cujo montante depende apenas da aplicação de índice e multiplicação das parc
```

## Pedido 33: Tributação e orçamento na Constituição (`direito-constitucional.tributacao-orcamento`, 684 tópicos de edital)

```
Matéria: direito-constitucional.tributacao-orcamento (Tributação e orçamento na Constituição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios gerais do Sistema Tributário Nacional": 22
- "2. Impostos constitucionais e reforma da tributação sobre consumo": 12
- "3. Repartição constitucional das receitas tributárias": 30
- "4. Finanças públicas, PPA, LDO e LOA": 19
- "5. Emendas, créditos, regra de ouro, duodécimos e pessoal": 17

Já existem (não repita):
- Um Município quer cobrar tributo pela disponibilização de um serviço público individualizá
- Uma lei estabelece graduação de determinado imposto sobre patrimônio conforme a situação e
- Dois entes federados discordam sobre o alcance de suas competências tributárias e necessit
- Pretende-se instituir contribuição para custeio e expansão da iluminação pública em um Mun
- Uma equipe classifica os impostos segundo a competência para instituí-los. O imposto sobre
- Uma operação com bens e serviços é alcançada pelo imposto de competência compartilhada ins
- Um Município optou pela fiscalização e cobrança do ITR dos imóveis localizados em seu terr
- No planejamento federal, pretende-se fixar metas e prioridades e orientar a elaboração do 
- Na execução do orçamento, deve-se repassar em parcelas mensais os recursos devidos ao Pode
- Uma associação de contribuintes questiona qual lei deve estabelecer normas gerais sobre ob
- Para operação ocorrida em 2026, aplica-se a transição constitucional do IBS e da CBS. Qual
- Na análise das transferências constitucionais indiretas, examina-se o percentual de IR, IP
- Uma lei estadual disciplina a distribuição municipal dos 25% do ICMS pertencentes aos Muni
- Ao montar a LOA federal, analisa-se em que orçamento registrar investimentos de empresa em
- No projeto da LOA, foram apresentadas emendas individuais e se discute o limite global con
- O projeto de LOA inclui autorização para abertura de créditos suplementares e contratação 
- Uma proposta pretende estabelecer condições gerais para concessão, ampliação ou prorrogaçã
- Após a reforma tributária, um Estado recebe IPVA relativo a embarcação particular cujo pro
- Para repartir aos Municípios os 25% da arrecadação do IBS que foram distribuídos ao Estado
- O Estado recebe parcela da União vinculada à exportação de produtos industrializados, conf
- No primeiro ano de determinado mandato federal, a equipe prepara o projeto de PPA e analis
- Um projeto busca autorizar operações de crédito em montante superior às despesas de capita
- Um crédito especial foi autorizado nos últimos quatro meses do exercício financeiro, mas p
- Por emenda individual, recursos federais são transferidos diretamente a um Município, sem 
- Um ente pretende criar cargos e conceder aumento remuneratório na administração direta. Há
- Uma lei prevê imposto sobre situação patrimonial que não depende de serviço público especí
- A cobrança de taxa para custear limpeza geral de vias públicas pode fundamentar-se exclusi
- Ao instituir um imposto, o legislador deve considerar, sempre que possível, o caráter pess
- O IBS e a CBS passaram a integrar, ambos como impostos, a competência tributária exclusiva
- O IR retido na fonte sobre rendimentos pagos por um Município e por suas autarquias integr
- A LOA federal é formada pelo orçamento fiscal e pelo de investimento das empresas controla
- Uma fiscalização iniciada em 2026 deve aplicar à fase de teste do IBS os mesmos percentuai
- No desenho constitucional da reforma, o IBS utiliza legislação uniforme no território naci
- Ao distribuir aos Municípios a receita de IBS atribuída ao Estado, deve-se aplicar no míni
- Se a administração disponibiliza dados orçamentários em meio eletrônico com mecanismos de 
- Enquanto aplicado o calendário federal do ADCT, o projeto de LDO deve ser enviado até 31 d
- A exclusividade da LOA admite que o próprio texto orçamentário autorize créditos suplement
- Na aprovação de emendas individuais ao orçamento, metade do limite de 2% da receita corren
- A partir da EC 132/2023, a base do art. 159, I, para FPE, FPM e fundos regionais passou a 
- Uma emenda individual foi aprovada no limite constitucional, sem qualquer impedimento técn
- Em transferência especial oriunda de emenda individual, a transferência ocorre diretamente
- Um crédito suplementar aprovado nos quatro últimos meses do exercício financeiro pode, por
- Decretada calamidade pública de âmbito nacional segundo o art. 167-B, a Constituição prevê
- Ao fim do exercício, o saldo financeiro não utilizado dos duodécimos pode ser transferido 
- Se as medidas constitucionais iniciais de adequação da despesa com pessoal forem insuficie
```

## Pedido 34: Enfermagem: fundamentos, procedimentos e cuidado clínico (`enfermagem.fundamentos-procedimentos-cuidado-clinico`, 683 tópicos de edital)

```
Matéria: enfermagem.fundamentos-procedimentos-cuidado-clinico (Enfermagem: fundamentos, procedimentos e cuidado clínico)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Processo de Enfermagem, avaliação e segurança da assistência": 19
- "2. Medicamentos, cálculos, curativos e dispositivos": 20
- "3. Imunização, vigilância, atenção primária e prevenção": 20
- "4. Cuidados clínicos, sondagens, exames e doenças prevalentes": 20
- "5. Acolhimento, urgências, trauma e suporte à vida": 21

Já existem (não repita):
- Uma pessoa internada apresenta dispneia. Após colher dados subjetivos e objetivos, o enfer
- Uma pessoa aguarda medicação em quarto com leitos trocados após remanejamento. Antes da ad
- Durante aferição de pressão arterial em adulto, um manguito muito estreito é empregado em 
- A equipe revê o protocolo de administração de medicamentos após quase erro de via de aplic
- Prescrevem-se 300 mg de medicamento, disponível em solução com 150 mg por mL. Qual volume 
- Uma bomba não está disponível e a prescrição estabelece 600 mL em cinco horas com equipo d
- Paciente acamado apresenta escore de Braden menor em nova avaliação. Qual interpretação so
- Uma pessoa em repouso prolongado apresenta eritema localizado não branqueável sobre proemi
- Durante atendimento de recém-nascido com registro vacinal ainda vazio, a equipe consulta o
- Uma câmara de vacinas fica por período documentado fora da temperatura recomendada. Que pr
- Uma criança apresenta febre horas após vacina. O episódio é registrado como ESAVI e perman
- Uma pessoa com diabetes recebe encaminhamento para exame especializado e continua acompanh
- Pessoa em investigação de tuberculose pulmonar potencialmente transmissível será atendida 
- Em paciente com nutrição enteral, foi introduzida uma sonda e a equipe pretende iniciar di
- Um paciente está com cateter urinário de demora e sistema fechado. Qual medida de manutenç
- Uma pessoa com diabetes tem glicemia capilar baixa, sudorese e redução do nível de consciê
- Um exame aponta potássio muito elevado, mas a amostra foi identificada como hemolisada. Qu
- Após colisão, uma pessoa apresenta hemorragia externa exsanguinante e suspeita de trauma c
- Uma pessoa aguarda consulta por queixa leve, mas passa a apresentar dispneia intensa, sudo
- Em parada cardíaca de adulto, a equipe realiza RCP antes de obter via aérea avançada. Qual
- Durante ressuscitação, o monitor mostra assistolia confirmada e a equipe pede carga de des
- Uma pessoa adulta consciente apresenta obstrução grave de via aérea por corpo estranho e n
- Um paciente em dieta enteral recebe prescrição de comprimido de liberação prolongada. A eq
- Uma prescrição solicita 0,25 g de medicamento. O frasco reconstituído contém 500 mg em 4 m
- Uma pessoa adulta com pressão elevada relata dor torácica intensa, sudorese fria e dificul
- Em acompanhamento de pessoa com dispneia, o enfermeiro registra dados, identifica diagnóst
- Após troca de enfermaria, identificar o paciente somente pelo número de seu leito satisfaz
- Na administração de medicamento, conferir a dose calculada não elimina a necessidade de av
- Em atendimento a pessoa com doença transmissível, o uso de luvas de procedimento substitui
- Uma lesão por pressão classificada como estágio 1 tem pele íntegra e eritema não branqueáv
- Na escala de Braden, um aumento do escore de risco significa, por definição, maior risco d
- Uma adolescente atrasou uma dose prevista pelo PNI, mas possui registros confiáveis das do
- Ao registrar febre como ESAVI após vacinação, a equipe comprova que o imunobiológico causo
- No acompanhamento de uma pessoa com tuberculose pulmonar potencialmente transmissível, pre
- Em paciente com dieta por sonda recém-posicionada, a ausculta de ruído após injeção de ar 
- Durante 12 horas, um paciente recebe 1.500 mL de líquidos mensurados e elimina 1.900 mL. O
- Uma pessoa com hipoglicemia e rebaixamento importante de consciência pode receber bebida a
- Uma equipe analisa erros frequentes de passagem de plantão e modifica coletivamente o flux
- Para otimizar o aprazamento, a equipe decide programar todos os medicamentos no mesmo horá
- Uma prescrição informa 0,3 g e a solução disponível contém 100 mg/mL. Convertida a unidade
- Uma lesão em área de pressão tem leito coberto por escara que impede avaliar a profundidad
- Paciente com cateter vesical de demora apresenta fluxo contínuo para coletor abaixo do nív
- Se uma pessoa inicialmente classificada para atendimento de baixa prioridade evoluir com c
- Em parada cardíaca de adulto, o algoritmo diferencia fibrilação ventricular e taquicardia 
- A orientação de 2025 para obstrução grave de via aérea por corpo estranho em adulto consci
```

## Pedido 35: Segurança do paciente e qualidade em saúde (`saude.seguranca-paciente-qualidade`, 682 tópicos de edital)

```
Matéria: saude.seguranca-paciente-qualidade (Segurança do paciente e qualidade em saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, taxonomia de incidentes e governança da segurança": 20
- "Metas de segurança e protocolos assistenciais prioritários": 20
- "Cultura de segurança, notificação e gestão de riscos": 20
- "Qualidade em saúde, indicadores, acreditação e melhoria contínua": 20
- "Humanização, equipes, cuidado centrado na pessoa e confidencialidade": 20

Já existem (não repita):
- Durante uma conciliação medicamentosa, um profissional intercepta a troca de paciente ante
- Uma paciente recebeu medicamento destinado a outra pessoa, foi monitorada e não apresentou
- Ao comparar instrumentos de segurança em um hospital, qual relação está correta?
- Sobre o âmbito da RDC Anvisa nº 36/2013, assinale a afirmativa juridicamente mais adequada
- Um hospital mantém protocolos publicados, porém sem indicadores, responsáveis ou análise d
- Antes da coleta de sangue, o paciente informa nome completo e data de nascimento, ambos co
- Em uma transferência, a enfermeira comunica situação, breve histórico, avaliação e recomen
- Uma unidade revisa insulinas e medicamentos com embalagens parecidas. O conceito de medica
- Em relação à lista de verificação de cirurgia segura, a sequência temporal correta é:
- Uma banca afirma que prevenir lesão por pressão é irrelevante porque não corresponde a uma
- Após um erro de medicação sem dano, a chefia deseja identificar de imediato um culpado e p
- O serviço passou de 18 para 55 notificações mensais após implantar canal protegido de rela
- A direção quer antecipar falhas possíveis ao implantar um novo sistema de infusão e corrig
- Conforme a RDC Anvisa nº 36/2013, um evento adverso assistencial que resultou em óbito dev
- Durante análise de evento, a equipe escreve como causa única “enfermeiro desatento” e ence
- Qual dos exemplos é indicador de PROCESSO, pela abordagem estrutura-processo-resultado?
- Duas unidades tiveram 10 quedas em um mês. Uma acumulou 2.000 pacientes-dia; a outra, 20.0
- Uma equipe executou nova rotina de confirmação de identidade. Agora mede adesão, compara c
- Uma direção afirma que obter acreditação prova que nunca ocorrerá evento adverso no hospit
- Ao reduzir o tempo médio de atendimento, um serviço observa mais omissões de orientações n
- Uma unidade de pronto atendimento utiliza acolhimento com classificação de risco. Qual cri
- Ao revisar o PNH, uma equipe identifica três princípios centrais. Qual conjunto está corre
- Em reunião, médicos, enfermeiros, nutricionistas e fisioterapeutas pactuam um plano único,
- Um conhecido da família de uma paciente solicita, por aplicativo pessoal, o laudo sigiloso
- No planejamento da alta, a equipe explica medidas com palavras acessíveis e pede à pessoa 
- O conceito de incidente de segurança abrange tanto eventos com dano como situações que pod
- Toda ocorrência de dano durante internação é, por definição, um evento adverso assistencia
- O Plano de Segurança do Paciente e o Núcleo de Segurança do Paciente designam o mesmo órgã
- A exclusão de consultórios individualizados do escopo específico da RDC 36/2013 significa 
- Número do quarto e do leito é identificador suficiente para confirmar a pessoa antes de um
- Comunicação em circuito fechado inclui repetição pelo receptor e validação do conteúdo pel
- A dupla checagem independente de medicamento de alta vigilância torna desnecessária a veri
- A lista de verificação cirúrgica pode ser preenchida retrospectivamente no fim do procedim
- Uma cultura justa distingue erro humano de conduta deliberadamente temerária e busca respo
- Zero notificações de incidentes em um hospital provam que não houve incidentes naquele per
- A análise de causa raiz e a FMEA são ferramentas prospectivas destinadas exclusivamente a 
- O prazo de até 72 horas para notificar óbito atribuído a evento adverso decorre da RDC nº 
- No modelo de Donabedian, a disponibilidade de equipamentos é estrutura; o uso de checklist
- Duas instituições com igual número de quedas terão necessariamente igual taxa por mil paci
- No PDCA, Check corresponde a implantar permanentemente todas as mudanças sem medir seus ef
- A acreditação institucional dispensa um serviço das notificações sanitárias obrigatórias.
- O acolhimento com classificação de risco pode priorizar pessoas de maior gravidade mesmo q
- A transversalidade da PNH busca ampliar canais de comunicação e integração entre profissio
- Uma reunião com várias categorias profissionais é necessariamente interdisciplinar, ainda 
- O consentimento explícito é a única hipótese legal que autoriza tratar dados de saúde de p
```

## Pedido 36: Leitura de gráficos, tabelas, mapas e escalas (`matematica.graficos-tabelas-mapas-escalas`, 672 tópicos de edital)

```
Matéria: matematica.graficos-tabelas-mapas-escalas (Leitura de gráficos, tabelas, mapas e escalas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tabelas, frequências e representação de dados": 33
- "2. Gráficos, séries temporais e variação percentual": 33
- "3. Proporcionalidade, funções e problemas com dados": 12
- "4. Mapas, plantas e escalas numéricas e gráficas": 11
- "5. Figuras planas e espaciais, projeções e cortes": 11

Já existem (não repita):
- Uma tabela registra 30 inscrições pela manhã, das quais 12 são no curso A. Qual é a partic
- Um quadro de dupla entrada informa 18 atendimentos do serviço B de manhã e 10 à tarde. Qua
- Entre 80 registros organizados em tabela, 20 pertencem à classe de 10 a menos de 20 unidad
- Um gráfico de setores divide corretamente o total de gastos; o setor de transporte ocupa u
- Uma série mensal apresenta 40 unidades em janeiro, 50 em fevereiro, 45 em março e 60 em ab
- Em um mapa de escala 1:100.000, a distância retilínea entre duas localidades mede 3 cm. Qu
- Uma parede mede 5 cm em planta de escala 1:100. Qual é seu comprimento real, supondo repre
- Uma sala retangular tem, conforme tabela de medidas, 4 m de largura e 6 m de comprimento. 
- A planificação de um cubo é montada apenas com suas faces geométricas, sem abas de colagem
- Um indicador de conclusão passou de 30% para 36% entre dois anos. Qual descrição informa t
- Uma série informa 80 atendimentos em um mês e 100 no mês seguinte, sob igual duração de ob
- Uma tabela apresenta vendas de 240 unidades no setor X e 180 no setor Y. Em que percentual
- A tabela de compras mostra 2 kg por R$ 18 e 5 kg por R$ 45, sem taxa fixa adicional. Qual 
- Quatro máquinas iguais concluem trabalho fixo em 6 horas. Se oito máquinas trabalham com i
- Uma tabela registra os pontos (x=2, y=7) e (x=4, y=11), pertencentes à mesma função afim. 
- Uma tabela mostra que no dia 1 foram vendidas 8 unidades de A a R$ 15 cada e 5 de B a R$ 2
- Numa carta de escala 1:50.000, mede-se segmento retilíneo de 7 cm. Qual distância em quilô
- Em histograma de classes desiguais, a classe A tem largura 10 e frequência 20; a B, largur
- Um valor de 100 cresce 20% em certo ano e cai 20% no seguinte, com percentuais aplicados s
- Uma escala gráfica informa que um segmento de 2 cm equivale a 5 km reais. Um trajeto retil
- Uma sala retangular aparece numa planta 1:200 como figura de 4 cm por 3 cm. Qual área real
- Uma maquete de reservatório, semelhante ao real e construída na escala 1:10 em todas as di
- Um cilindro circular reto tem raio de 3 cm e altura de 8 cm. Um corte axial passa pelo seu
- Uma tabela apresenta 8 peças de tipo A vendidas por R$ 15 cada e 5 de B por R$ 20. Qual fo
- Um mapa tem escala 1:250.000 e registra 4,8 cm entre dois pontos em linha reta. À velocida
- Numa tabela de dupla entrada, a frequência total de uma categoria que ocupa duas linhas é 
- Em planta desenhada na escala 1:100, um segmento de 1 cm corresponde a 100 metros reais, p
- Em gráfico de setores que reparte um único total em categorias mutuamente exclusivas, um q
- Quando os dados consistem em quantidades de três categorias independentes, o gráfico de li
- Um quilômetro equivale a cem mil centímetros, de modo que uma distância real de 2 km conté
- Na planificação comum de um cubo, sem abas de colagem, devem surgir oito faces quadradas, 
- Uma taxa que passa de 30% para 36% cresce seis pontos percentuais, embora o seu aumento re
- Uma função afim representada por reta de equação y=2x+3 descreve proporcionalidade direta,
- Se uma quantidade fixa de trabalho exige 24 horas-máquina, os pares (4 máquinas, 6 horas) 
- Entre dois mapas do mesmo formato, a escala 1:250.000 é maior que a escala 1:25.000, porqu
- Uma matriz 2 por 3 possui duas linhas e três colunas, totalizando seis entradas; cada posi
- Se uma produção aumenta 20% e depois diminui 20%, a produção final retorna exatamente ao v
- Em tabela de frequências de classes ordenadas e não sobrepostas, a última frequência absol
- Em histograma com classes de larguras diferentes, a comparação das alturas isoladas fornec
- Se duas figuras semelhantes são representadas em escala linear 1:200, a área real correspo
- O corte de um cilindro circular reto por plano que passa por seu eixo produz uma seção cir
- Uma maquete geometricamente semelhante tem todas as medidas lineares em um décimo das reai
- Uma tabela informa 600 atendimentos durante seis meses e 500 durante três meses seguintes.
- Se uma tabela registra oito vendas ao preço unitário de R$ 15 e cinco vendas ao preço de R
- Um gráfico de linha informa produção de 40 unidades em janeiro e 60 em abril. Apenas esses
```

## Pedido 37: Nuvem avançada, contêineres, orquestração e CI/CD (`informatica.cloud-devops-containers`, 671 tópicos de edital)

```
Matéria: informatica.cloud-devops-containers (Nuvem avançada, contêineres, orquestração e CI/CD)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Nuvem, modelos, arquitetura, continuidade e migração": 18
- "2. Contêineres, Docker, redes, persistência e segurança": 20
- "3. Kubernetes, Rancher e orquestração de clusters": 21
- "4. CI/CD, DevOps, estratégias de publicação e migrações": 24
- "5. IaC, GitOps, DevSecOps e governança operacional": 17

Já existem (não repita):
- Uma aplicação recebe recursos pela rede de forma automática, usa capacidade compartilhada 
- Uma equipe contrata máquinas virtuais gerenciando diretamente sistema operacional, aplicat
- Para suportar a queda de uma instalação sem transferir a operação para outra região geográ
- Uma política admite perder no máximo os últimos dez minutos de alterações e determina recu
- Um desenvolvedor substitui uma máquina virtual por contêiner Linux executado em host compa
- Um banco executado em Docker deve preservar seus arquivos mesmo após a remoção do contêine
- Uma aplicação web deve manter quatro Pods equivalentes e atualizar suas imagens gradualmen
- Uma plataforma importa clusters Kubernetes já existentes e organiza namespaces em projetos
- Uma aplicação passa por compilação, testes e validação automáticos a cada alteração; ao fi
- O Dockerfile de uma imagem web contém `EXPOSE 8080`. Após executar `docker run app:v2` sem
- Dois serviços declarados em `compose.yaml` são iniciados com `docker compose up -d`. O ser
- Um serviço em Kubernetes está vivo, mas durante uma atualização temporária não consegue re
- Uma aplicação Kubernetes tem Pods substituídos durante atualizações. Clientes precisam man
- Um ambiente tem três clusters Kubernetes sob uma mesma plataforma administrativa. Pretende
- Uma versão nova deve ser entregue inicialmente a 5% dos usuários, e a ampliação dependerá 
- Uma migração `V3__add_coluna.sql` já foi aplicada pelo Flyway em produção e seu checksum r
- Uma equipe quer provisionar redes e instâncias com planos de mudanças revisáveis e também 
- Um administrador do Rancher concede a uma equipe acesso de manutenção a determinados names
- Um serviço tem quatro réplicas, todas alocadas em um único nó de uma zona. A meta é resist
- Durante rolling update, versões antiga e nova da aplicação coexistem. A nova exige renomea
- Uma equipe consulta o referencial DORA atualizado e deseja separar métricas de vazão e ins
- Um cluster recebe alteração manual em uma configuração, divergindo do arquivo aprovado em 
- Um Deployment mantém cinco réplicas, mas o serviço enfrenta tráfego crescente. O cluster p
- Uma aplicação tem meta de disponibilidade de 99,9% no período hipotético de trinta dias. A
- Uma pipeline constrói imagem de aplicação, verifica dependências e publica a versão. A equ
- Ao implantar um serviço em nuvem, a existência de provisionamento sob demanda, recursos ag
- Ao adotar execução de funções serverless, uma equipe deixa de ter qualquer responsabilidad
- Um volume nomeado do Docker pode conservar arquivos depois que o contêiner que o utilizava
- Um Pod Kubernetes com dois contêineres corresponde necessariamente a duas máquinas virtuai
- Se uma aplicação permanece viva, mas temporariamente incapaz de atender requisições, a fal
- No Rancher, o objeto denominado projeto é um recurso nativo da API Kubernetes e, por isso,
- Ao revisar código Terraform, uma equipe executa `terraform plan` para examinar alterações 
- Uma equipe realiza integração contínua de código com testes automatizados. Isso significa 
- Um serviço Docker Swarm pode declarar quantidade desejada de réplicas e deixar que o orque
- Ao inserir uma senha em um Secret Kubernetes, a codificação Base64 do manifesto assegura, 
- Um pipeline GitLab CI/CD define jobs em arquivo `.gitlab-ci.yml` e os organiza em stages; 
- No Docker Compose, a forma simples de `depends_on` garante que um banco terminou recuperaç
- Após uma migração versionada de banco registrada com checksum, uma nova alteração deve pre
- Uma política fixa RPO de vinte minutos e RTO de duas horas. Logo, a equipe deve conseguir 
- Ao importar um cluster já existente para o Rancher, a plataforma pode oferecer gestão de a
- Criar um objeto Ingress com regras de host e caminho basta para executar o roteamento HTTP
- Em uma implantação canário, é possível direcionar inicialmente pequena parcela do tráfego 
- Na classificação DORA atualizada em 2024 e ainda documentada em 2026, permanecem exatament
- Uma solução GitOps pode detectar divergência entre o manifesto aprovado no repositório e o
- Por manter cópia síncrona das operações em outra réplica, um serviço pode prescindir de qu
```

## Pedido 38: Gestão de processos (BPM): mapeamento, análise e melhoria (`administracao-publica.gestao-processos`, 664 tópicos de edital)

```
Matéria: administracao-publica.gestao-processos (Gestão de processos (BPM): mapeamento, análise e melhoria)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, cadeia de valor e governança dos processos": 26
- "2. Levantamento, AS-IS, TO-BE, SIPOC e modelagem BPMN": 25
- "3. Diagnóstico, tempos, capacidade e indicadores de desempenho": 17
- "4. Melhoria, redesenho, padronização e tecnologias BPM": 18
- "5. Aplicações na administração pública, inovação e avaliação contínua": 14

Já existem (não repita):
- Uma unidade organiza a emissão de certidões desde o recebimento do pedido até a entrega ao
- Em uma organização, o macroprocesso 'prestar serviços de atendimento' contém o processo 'e
- Uma equipe entrevista servidores, observa atendimentos reais e registra devoluções que atu
- Em BPMN, após conferir o pedido, uma unidade precisa escolher apenas entre 'documentação c
- Um atendimento leva 180 minutos do protocolo até a entrega, dos quais 25 minutos correspon
- Uma área institui um plano para reduzir retrabalho, especificando o que será feito, por qu
- Uma entidade implanta robô que copia campos de formulários para um sistema legado por meio
- Em uma cadeia de valor, a emissão de licenças ao solicitante constitui atividade diretamen
- Um projeto de mapeamento envolve várias unidades e precisa identificar o responsável por n
- Uma organização utiliza tabela com fornecedores, entradas, macroetapas, saídas e destinatá
- Uma unidade pretendia concluir 200 atendimentos dentro do prazo e concluiu 170. Sem avalia
- Em determinado serviço, o protocolo processa 90 pedidos por dia, a análise consegue 35 e a
- Um fluxo interno exige que duas equipes executem verificações independentes antes de uma d
- Um processo exige 240 minutos entre pedido e entrega. Desses, 36 minutos são atividades qu
- Um serviço reduz em ciclos curtos a duplicação de informações e testa sucessivos pequenos 
- Um serviço público implantou triagem eletrônica: os atendimentos no prazo cresceram, mas o
- Em um modelo BPMN de colaboração, o participante 'solicitante' está em uma piscina e a ins
- Uma análise detecta que dois setores pedem a mesma informação, mas um deles também executa
- Uma unidade melhorou seu prazo médio de atendimento de 12 para 8 dias; contudo, no novo pe
- Um processo recebe em média 50 solicitações diárias. A etapa crítica tem capacidade susten
- Após implantar automação, uma instituição passou a emitir 120 documentos com 60 horas de e
- Uma instituição adota workflow para encaminhar aprovações e RPA para transcrever dados ent
- Uma organização escolhe novo software e decide remover de uma só vez a lógica anterior de 
- Uma equipe deseja comparar dois cenários de atendimento antes da implementação do TO-BE: s
- Em um projeto, o indicador operacional registra diminuição de erros de digitação, a meta d
- Ao analisar a prestação de um serviço do requerimento até a resposta ao interessado, uma e
- Em uma entidade com estrutura funcional, a gestão de processos torna-se inviável, pois BPM
- O AS-IS deve retratar o funcionamento efetivamente observado, inclusive devoluções, ativid
- Na matriz SIPOC, a coluna 'Customers' identifica necessariamente as empresas contratadas q
- Em um diagrama BPMN, um gateway exclusivo pode selecionar a aprovação ou devolução de um p
- O lead time de um requerimento inclui somente os minutos de trabalho ativo, devendo o temp
- Uma oficina que define atividades, responsáveis, local, prazo, justificativa, método e cus
- Se uma etapa processa quarenta pedidos diários e as demais suportam pelo menos setenta, am
- Quando um fluxo de conferência necessita executar análise documental e consulta cadastral 
- Ao alcançar 88% de solicitações concluídas no prazo diante de meta de 90%, uma entidade po
- No contexto de melhoria de processos, o brainstorming pode gerar hipóteses sobre atrasos, 
- Um BPMS e um robô de RPA são necessariamente a mesma tecnologia, porque ambos gerenciam de
- O uso de um painel com metas de atendimento, duração das etapas e satisfação do usuário po
- A atuação do dono do processo dispensa a participação dos executores na validação do AS-IS
- Um processo com 300 minutos de lead time, 80 minutos de trabalho ativo e apenas 45 minutos
- Uma reengenharia necessariamente consiste em microajustes graduais e contínuos, enquanto K
- Um órgão que testa novo procedimento com grupo-piloto, mede erros e espera antes e depois,
- No BPMN, o fluxo de sequência pode cruzar as fronteiras de duas piscinas porque essa seta 
- Mesmo que o indicador de produtividade melhore após automação, a efetividade de um serviço
- Uma simulação que indica queda de dez para três dias no prazo de um processo prova, indepe
```

## Pedido 39: Progressões aritméticas e geométricas (`matematica.progressoes-pa-pg`, 662 tópicos de edital)

```
Matéria: matematica.progressoes-pa-pg (Progressões aritméticas e geométricas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sequências e reconhecimento de regularidades": 18
- "2. Progressão aritmética: termo geral, interpolação e soma": 24
- "3. Progressão geométrica: termo geral, sinais e meios geométricos": 21
- "4. Somas finitas de PG e séries geométricas infinitas": 20
- "5. Problemas mistos, contagem e aplicações das progressões": 17

Já existem (não repita):
- Uma planilha registra a sequência 4, 9, 14 e 19. Mantida a mesma regra aditiva, qual valor
- Uma PA começa em 7 e a cada novo registro acrescenta 3 unidades. Qual é o oitavo termo da 
- Uma sequência aritmética tem primeiro termo 20 e razão −4. Qual é o valor de seu quinto te
- Um padrão numérico começa com 3, 6, 12 e 24. Qual é a razão multiplicativa constante que o
- Em quatro dias consecutivos foram registradas 5, 8, 11 e 14 unidades. Qual é a soma dos va
- Uma população idealizada de microrganismos dobra? Não: triplica a cada etapa e inicia com 
- Uma sucessão registra os valores 9, 9, 9, 9. Qual classificação é válida para esses quatro
- Uma quantia evolui geometricamente segundo 80, 40, 20 e 10. Qual será a quinta quantia man
- Quatro parcelas consecutivas formam a PG 3, 6, 12, 24. Qual é o valor total dessas parcela
- Uma PA satisfaz a₄ = 14 e a₁₀ = 38. Qual é o seu primeiro termo?
- Deseja-se inserir sete meios aritméticos entre 10 e 42. Qual é o primeiro dos meios inseri
- Uma PA contém vinte termos, com primeiro igual a 5 e último igual a 62. Qual é sua soma?
- Uma PG de razão positiva satisfaz a₃ = 18 e a₅ = 162. Qual é o valor de a₇?
- Uma PG começa em 81 e cada termo seguinte vale um terço do anterior. Qual é a soma de seus
- Uma sequência começa com 2, 5, 10, 17 e 26, seguindo a lei aₙ=n²+1. Qual será o sexto valo
- Uma PG inicia em 4 e possui razão −2. Qual é o sexto termo da sequência?
- Uma PA tem os valores 3, 7, 11 e assim sucessivamente. Qual é a soma de seus seis primeiro
- Uma PA possui três termos consecutivos iguais a 12, x e 30. Qual deve ser o valor central 
- Uma PG tem segundo termo 12 e quarto termo 108, sabendo-se que sua razão é positiva. Qual 
- Uma PA inicia em 2 e a soma dos seus dez primeiros termos é 155. Qual será seu décimo segu
- Uma PG apresenta primeiro termo 5, terceiro termo 45 e segundo termo negativo. Qual será s
- Uma PG positiva tem razão 1/2 e a soma de seus infinitos termos é 48. Quanto somam seus tr
- Uma PG começa com 5, tem razão 2 e a soma dos n primeiros termos é 155. Qual é o número de
- Uma PA tem soma dos oito primeiros termos igual a 92 e soma dos oito seguintes igual a 284
- Uma PG com primeiro termo 9 e razão negativa possui soma infinita igual a 6. Qual é o valo
- Uma PA é reconhecida quando a diferença entre quaisquer dois termos consecutivos é constan
- Na PG 3, 6, 12, 24, a razão geométrica é obtida subtraindo termos consecutivos, cujo resul
- Na PA 12, 9, 6, 3, a razão é −3, de modo que a sequência é decrescente mesmo possuindo ter
- Toda PG de razão negativa é numericamente decrescente, pois a multiplicação alternada torn
- Numa PA cujo primeiro termo é a₁, a expressão a₁+(n−1)r calcula corretamente o termo da po
- A soma de n termos de uma PA é n(a₁+aₙ), porque cada par de extremos contribui integralmen
- Em três termos consecutivos de uma PA, o termo central equivale à média aritmética dos ext
- Em uma PG com extremos 4 e 36 e um único meio, o termo central deve ser necessariamente a 
- Na PG finita de razão um, a soma dos n primeiros termos é n vezes o termo inicial.
- A série 5−5+5−5+... tem soma infinita convergente, porque seus termos positivos e negativo
- Para calcular o termo de posição dez de uma PA, devem-se somar nove razões ao primeiro ter
- Em uma PA de razão 4, a fórmula aₙ=a₁+4n representa corretamente a sequência para índices 
- Uma PG com primeiro termo não nulo e razão −1/3 admite soma infinita finita, apesar de alt
- Toda PG de razão um e primeiro termo não nulo apresenta soma infinita igual ao seu primeir
- A sequência constante 7, 7, 7 é simultaneamente PA de razão zero e PG de razão um.
- Na sequência 0, 0, 0, a razão geométrica pode ser determinada unicamente pelo quociente en
- Em PA de razão não nula, a soma dos n primeiros termos é uma expressão quadrática em n, po
- Se a₂=4 e a₄=36 em uma PG real de termos não nulos, a razão só pode ser 3 porque o quocien
- Em uma PG de termos não nulos, para quaisquer três posições consecutivas vale aₙ²=aₙ₋₁aₙ₊₁
- Duas progressões, uma PA e uma PG, com primeiros termos iguais a 2 e segundos termos iguai
```

## Pedido 40: Procedimentos especiais e juizados especiais cíveis (`direito-processual-civil.procedimentos-especiais`, 661 tópicos de edital)

```
Matéria: direito-processual-civil.procedimentos-especiais (Procedimentos especiais e juizados especiais cíveis)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Procedimentos contenciosos: consignação, contas e proteção possessória": 23
- "2. Outros procedimentos contenciosos e tutela documental": 23
- "3. Jurisdição voluntária, protesto, notificação e interpelação": 22
- "4. Juizados Especiais Cíveis estaduais: competência, partes e recursos": 16
- "5. Juizados Federais e da Fazenda Pública: competências e uniformização": 16

Já existem (não repita):
- Em obrigação pecuniária, o devedor fez depósito bancário nos termos do CPC e notificou o c
- Uma pessoa administrou dinheiro alheio e foi demandada por quem especificou as razões pela
- Uma pessoa perdeu inteiramente a posse de terreno após expulsão física, tendo prova da pos
- Uma credora possui documento escrito da dívida, mas ele não tem eficácia de título executi
- Um pedido de jurisdição voluntária segue as disposições gerais do CPC. Depois de instruído
- Uma pessoa capaz pretende cobrar dívida de 18 salários mínimos pelo Juizado Especial Cível
- Em sentença do Juizado Especial Cível estadual, a parte decidiu recorrer. Qual prazo de in
- Uma pessoa propõe causa federal cível de 45 salários mínimos contra autarquia federal, não
- Uma microempresa ajuíza ação de responsabilidade contratual de 50 salários mínimos contra 
- O credor recusou expressamente o depósito bancário consignado pelo devedor. Este possui co
- Um litígio coletivo sobre posse de imóvel relata esbulho ocorrido há dois anos. Antes de a
- Pessoa estranha a uma execução teve bem próprio constrito e pretende protegê-lo. O leilão 
- Uma sociedade precisa apurar haveres de sócio que se retirou, sem pretender extinguir toda
- Um interessado requer notificação judicial de outra pessoa e solicita sua averbação em reg
- No Juizado Especial Cível estadual, uma parte recorreu de sentença e precisa recolher prep
- Uma pessoa pretende ajuizar no Juizado Especial Federal demanda de divisão e demarcação de
- Uma autarquia estadual é demandada no Juizado da Fazenda Pública e pede prazo recursal em 
- Na ação de exigir contas, o juiz reconheceu o direito do autor e ordenou que o réu apresen
- Foi proposta ação monitória contra a Fazenda Pública, apoiada em prova escrita não executi
- Uma pessoa requereu que aviso judicial fosse divulgado ao público por edital, mas não demo
- Um autor do Juizado Cível estadual obteve sentença e opôs embargos de declaração no quinto
- Duas Turmas Recursais de regiões federais distintas divergem acerca da interpretação de le
- Uma pessoa ajuíza causa de 30 salários mínimos contra autarquia federal, incluída na compe
- Para determinar competência do Juizado Especial da Fazenda Pública, a demanda inclui parce
- Em processo no Juizado da Fazenda Pública, o juízo recebe requerimento de reexame necessár
- O procedimento comum do CPC pode aplicar-se subsidiariamente aos procedimentos especiais q
- Uma pessoa ainda exerce a posse do imóvel, mas o vizinho a perturba repetidamente; a ação 
- A ação monitória do CPC admite pretensões de pagamento de quantia, entrega de coisa e cump
- O Juizado Especial Cível estadual adota teto de sessenta salários mínimos para toda causa 
- Uma pessoa física pode figurar como autora no Juizado Especial Federal Cível em demanda co
- O Juizado Especial da Fazenda Pública estadual somente admite ações contra a União e suas 
- No procedimento geral de jurisdição voluntária, serão citados interessados e intimado o Mi
- A notificação judicial prevista no art. 726 do CPC decide definitivamente o mérito do dire
- Um pedido de notificação judicial pretende averbação em registro público; o art. 728 do CP
- Os embargos à ação monitória previstos no CPC exigem depósito integral do valor cobrado an
- No Juizado Especial Cível estadual, os embargos de declaração interrompem, e não apenas su
- A competência do Juizado Especial Federal Cível é relativa onde instalado, permitindo esco
- No Juizado da Fazenda Pública, a legislação especial afasta prazo diferenciado para as pes
- Na ação de demarcação e divisão de terras particulares propostas cumulativamente, o art. 5
- Um pedido de protesto judicial voltado a resguardar manifestação formal de interesse pode 
- Após desaparecimento dos autos de processo civil, a restauração julgada deverá prosseguir 
- No Juizado Especial Federal, divergência entre Turmas Recursais sobre interpretação de lei
- Uma ação de improbidade administrativa de valor correspondente a cinco salários mínimos in
- A citação para audiência de conciliação no Juizado Especial da Fazenda Pública deve ocorre
- O pedido de uniformização previsto no art. 18 da Lei nº 12.153/2009 pode ser apreciado dir
```

## Pedido 41: Gramática inglesa para leitura (`lingua-inglesa.gramatica-para-leitura`, 655 tópicos de edital)

```
Matéria: lingua-inglesa.gramatica-para-leitura (Gramática inglesa para leitura)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura da frase, artigos e classes de palavras": 20
- "2. Tempos verbais, aspecto e sequência temporal": 20
- "3. Verbos modais, condicionais e voz passiva": 20
- "4. Pronomes, conectores, comparações e preposições": 20
- "5. Inferência fina, falsos cognatos e integração gramatical": 20

Já existem (não repita):
- Leia: “We waited for an hour before the meeting.” Que justificativa explica corretamente o
- Em “A reader opened a file. The file contained a warning”, qual leitura respeita os artigo
- Um texto diz “Only a few responses were incomplete”. Qual paráfrase mantém o sentido básic
- Observe: “The list of changes is available online.” Por que aparece “is”, e não “are”?
- Em “Who discovered the error yesterday?”, qual análise explica a construção interrogativa?
- Compare: “The service operates daily” e “The service is operating now”. Qual interpretação
- Leia: “The team has monitored the system since January.” O marcador “since” indica:
- Em “The application had closed before the user returned”, qual sequência é sustentada pelo
- Leia: “We will publish the findings when the review ends.” Qual explicação é apropriada?
- Qual frase preserva a referência temporal de “The authors published the report in 2022”?
- Leia o aviso: “Visitors must not enter this area.” Qual reformulação mantém a ordem?
- Um texto informa: “The device may be unavailable tomorrow.” O modal “may” expressa:
- No trecho “The documents must be checked before release”, que interpretação é correta?
- Leia: “If we had a larger sample, the results would be more reliable.” A frase sugere:
- Em “If the records had arrived, the review would have finished”, qual inferência é melhor?
- Leia: “The models were expensive. This surprised the readers.” O pronome “This” provavelme
- Compare: “Despite the delay, the test continued.” Qual reescrita mantém a relação lógica?
- O texto diz “The revised plan led to fewer errors.” A escolha de “fewer” é coerente porque
- Leia: “The messages that arrived late were ignored.” O que a relativa introduzida por “tha
- Leia: “They look forward to receiving the results.” Por que “receiving” aparece com -ing?
- Em “The initial prediction was actually wrong”, qual tradução de “actually” é mais adequad
- Um resumo afirma: “Not all participants completed the form.” Qual conclusão é sustentada?
- Leia: “After several failed attempts, the team eventually found a solution.” O advérbio as
- Considere: “The researchers claim that the treatment may work.” Qual paráfrase preserva a 
- Leia: “The files have not yet been reviewed, although they might be useful.” Qual leitura 
- No período “The blue devices are available”, o adjetivo “blue” permanece sem flexão de plu
- Em “She gave me useful advice”, a palavra “advice” pode ser substituída, sem ajuste, por “
- Em “They are discussing the proposal now”, a construção “are discussing” destaca uma ativi
- No aviso “You do not have to sign the form”, a locução expressa proibição de assinatura, e
- Em “Although it was late, the meeting continued”, o conector “although” introduz concessão
- Em “The team has worked here for five years”, “for five years” indica o ponto exato de iní
- No trecho “When the meeting starts, we will present the results”, o verbo “starts” pode re
- Em “The proposal might have been rejected”, o autor afirma como fato confirmado que a prop
- Em “The method was tested by two groups”, “the method” realiza a ação, enquanto “two group
- Em “The old version, which was unstable, was replaced”, a relativa entre vírgulas é aprese
- Na frase “The software failed because of the server was offline”, o emprego de “because of
- Em “Actually, the plan was canceled”, o advérbio “actually” pode corrigir uma expectativa 
- Em “They had been waiting for hours when the signal arrived”, o past perfect continuous si
- A frase “If she had received the message, she would have replied” apresenta uma hipótese c
- Em “The files must have been copied”, “must have been copied” nomeia uma obrigação futura 
- No enunciado “The report was delayed, which upset the readers”, “which” pode retomar o fat
- Em “The more carefully they read, the fewer mistakes they made”, a estrutura indica que le
- Na frase “Not all responses were accurate”, não se pode concluir que nenhuma resposta foi 
- O período “The team eventually reached a decision” afirma que a equipe tomava decisões oca
- Em “Evidence suggests the policy might work”, substituir “might” por “will certainly” aume
```

## Pedido 42: LibreOffice (`informatica.libreoffice`, 654 tópicos de edital)

```
Matéria: informatica.libreoffice (LibreOffice)
Capítulos e quantas questões quero em cada um (total 100):
- "1. LibreOffice, formatos de arquivos, pastas e configurações": 27
- "2. Writer: edição, estilos, tabelas, campos e índices": 19
- "3. Calc: células, fórmulas, funções e referências": 18
- "4. Calc: classificação, filtros, gráficos, dados externos e proteção": 18
- "5. Impress: estrutura, mestres, apresentações e animação": 18

Já existem (não repita):
- Uma equipe quer criar relatório com edição de texto, estilos de títulos e sumário automáti
- Após trabalhar numa planilha, um usuário precisa entregá-la no formato nativo OpenDocument
- Uma pessoa precisa compartilhar uma versão de leitura de relatório e manter o documento or
- Numa instalação desktop com atalhos padrão, o usuário quer salvar as alterações do documen
- Um relatório precisa de sumário automático com capítulos e subseções. O autor aplicou apen
- Um documento possui capa sem numeração visível e capítulos com páginas numeradas. Qual rec
- No Writer, um responsável precisa inserir número de página que acompanhe automaticamente a
- No Calc, há números 5, 10 e 15 em A1, A2 e A3. Qual fórmula retorna a soma do intervalo in
- A fórmula =A2+B2 está em C2 e é copiada uma linha abaixo, para C3. Qual será a expressão r
- Uma planilha precisa manter fixa a taxa armazenada em F1 ao copiar fórmulas em várias linh
- Uma tabela Calc com cem registros deve mostrar apenas as vendas acima de R$ 1.000, preserv
- Uma lista contém nome, cidade e salário em colunas adjacentes. Para ordenar por cidade e d
- Um analista quer um gráfico no Calc que represente pares de valores numéricos X e Y sem tr
- Uma tabela disponível em página HTML precisa alimentar uma planilha e admitir atualizações
- Uma pessoa pretende impedir a exclusão e a renomeação de abas numa pasta de planilhas Calc
- Uma equipe deseja padronizar a aparência de títulos e elementos recorrentes em vários slid
- Em apresentação, o título aparece gradualmente enquanto o mesmo slide permanece projetado.
- O responsável quer retirar provisoriamente um slide da exibição habitual, mas pretende man
- Uma apresentação deve exibir um efeito visual ao passar do slide três para o quatro, sem a
- Uma planilha possui uma primeira linha com títulos das colunas, e a lista será classificad
- Uma fórmula contém `=$A2*B$1` e é copiada da célula C2 para D3, deslocando-se uma coluna à
- Num documento Writer, alterar somente o texto do rodapé de uma página afeta outras páginas
- Uma planilha com filtro ativo exibe só linhas de certa cidade. Um usuário imagina que orde
- No Impress, a equipe precisa imprimir várias miniaturas dos slides na mesma folha, sem tra
- Uma planilha está com estrutura protegida por senha, mas é aberta e seus números continuam
- Uma estudante cria apresentação no Impress e a salva como arquivo ODP. Essa extensão corre
- Uma pessoa renomeia manualmente o arquivo dados.ods para dados.xlsx no gerenciador de arqu
- No Writer, aplicar estilos de títulos a parágrafos permite que eles sejam reconhecidos na 
- Uma fórmula em C2 contém =A2+B2. Quando copiada uma linha abaixo, a expressão permanece =A
- No Impress, aplicar uma transição altera a apresentação durante a passagem entre slides, e
- Depois de inserir novos capítulos num documento Writer com sumário automático, o usuário d
- Uma célula no Calc contém =$A1+B$2 e a fórmula é copiada uma linha abaixo; o primeiro term
- Ao aplicar autofiltragem em uma tabela Calc, as linhas não compatíveis com os critérios sã
- Uma tabela do Calc pode alimentar gráfico que se atualiza quando seus dados são alterados,
- No Impress, ocultar determinado slide equivale a excluí-lo da apresentação, pois ele deixa
- Uma planilha é protegida contra renomeação de abas, mas continua abrindo normalmente para 
- No Writer, estilos de parágrafo e estilos de página são equivalentes, pois ambos são apena
- Ao copiar a fórmula =$B$2*C3 uma coluna à direita, da célula D3 para E3, o resultado será 
- No Calc configurado para separar argumentos por ponto e vírgula, =SOMA(A1;A5) soma obrigat
- No Calc, a opção de classificação com mais de uma chave permite ordenar primeiro por cidad
- A utilização de Planilha → Vincular a dados externos no Calc é equivalente a colar valores
- No Impress, modificar um elemento no slide mestre pode afetar slides que utilizam esse mes
- Exportar apresentação do Impress como PDF preserva necessariamente todas as animações pers
- Uma planilha possui linhas congeladas na janela para leitura durante rolagem. Isso não gar
- Uma apresentação personalizada no Impress pode usar um subconjunto dos slides existentes, 
```

## Pedido 43: Edificações: execução, manutenção predial e patologia (`engenharia.edificacoes-execucao-manutencao-patologia`, 654 tópicos de edital)

```
Matéria: engenharia.edificacoes-execucao-manutencao-patologia (Edificações: execução, manutenção predial e patologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento, canteiro e segurança na execução": 17
- "2. Solos, fundações, estruturas e controle tecnológico": 17
- "3. Vedações, acabamentos, coberturas e instalações": 18
- "4. Manutenção, reformas, acessibilidade e inspeção": 27
- "5. Patologia, diagnóstico e recuperação das construções": 21

Já existem (não repita):
- Durante a instalação de um canteiro, a equipe discute quem deve preparar e implementar o P
- Antes de definir a solução de fundação, o projeto solicita reconhecimento das camadas do t
- Uma edificação de pequeno porte terá cargas distribuídas a uma área de apoio superficial. 
- No recebimento de concreto, uma equipe obtém o abatimento do tronco de cone e quer interpr
- Ao executar alvenaria de vedação com janelas, pretende-se reduzir a concentração de tensõe
- Um banheiro recém-entregue apresenta infiltração na parede junto ao ralo. Qual medida é ma
- A gestão predial decide programar a limpeza de calhas antes do período de chuvas para dimi
- Uma intervenção pretende remover parte de uma parede que pode conter instalações e funções
- Durante vistoria, surgem bolhas na pintura e manchas em parede. Qual interpretação inicial
- Para apoiar uma obra próxima a edifícios existentes, avalia-se o risco de deformações por 
- Duas vigas são fabricadas antes da montagem: uma com armaduras passivas e outra com armadu
- Em projeto com hidrantes, sinalização de saídas e extintores, um responsável sugere que ex
- Uma bomba ainda funciona, mas sensores registram aumento gradual de vibração. A manutenção
- Durante inspeção predial predominantemente sensorial, não se identificam sinais aparentes 
- Uma reforma instalou rampa na entrada, mas manteve um degrau entre o acesso e os sanitário
- Viga de concreto apresenta fissura longitudinal junto à armadura e destacamento do cobrime
- No controle de qualidade de pilares, a equipe encontra armaduras fora de posição e registr
- Um canteiro tem 8 trabalhadores e altura prevista de 8 m. Discute-se utilizar a exceção da
- A execução de escavação ocorre junto a uma edificação existente, com rede enterrada indica
- Um telhado passa a apresentar vazamentos durante chuvas acompanhadas de vento. O interior 
- Antes de fechar um forro que abrigará circuitos, tubulações, rede de dados e dreno do ar-c
- Um condomínio deseja demolir parte de parede durante reforma e apresenta apenas esboço arq
- Uma parede apresenta fissura inclinada que aumenta ao longo de meses, perto de uma fundaçã
- Em fachada com revestimento desplacado, há eflorescência recorrente após chuvas. O que dev
- Uma concretagem apresenta lote com abatimento esperado, mas documentação incompleta sobre 
- Durante a mobilização, sacos de cimento foram identificados por lote e armazenados em loca
- Um serviço executado a mais de 2 m do nível inferior é enquadrado automaticamente como tra
- Na execução de viga de concreto armado, conferir a posição dos espaçadores e o cobrimento 
- Uma fundação formada por estacas profundas passa a ser classificada como fundação rasa qua
- Antes de fechar um forro que oculta tubulações e dutos, a equipe deve verificar compatibil
- A troca de uma bomba depois que ela para por falha caracteriza manutenção preventiva, porq
- Na montagem de estruturas metálicas, a necessidade de contraventamento provisório deve ser
- Ao obter abatimento do tronco de cone compatível com o esperado, a equipe já comprova a re
- Vazamentos em cobertura podem resultar de arremates, rufos, calhas, fixações e sobreposiçõ
- Em redes de esgoto predial, qualquer declividade maior que zero assegura funcionamento, to
- Instalar uma rampa na entrada não garante, isoladamente, uma rota acessível quando degraus
- A elaboração do manual previsto na ABNT NBR 14037 substitui o sistema de gestão da manuten
- Ao acompanhar tendências de temperatura e vibração de equipamento para escolher o momento 
- Um canteiro com 9 trabalhadores e altura de 9 m pode empregar a exceção do item 18.4.2.1 d
- Escavação próxima a construção existente pode exigir acompanhamento documentado de possíve
- Se um teto apresenta mancha junto à tubulação de climatização, a simples aplicação de tint
- Um elemento pré-moldado de concreto, mesmo com resistência confirmada, pode requerer análi
- Uma inspeção predial predominantemente sensorial, realizada sem ensaios intrusivos, permit
- Uma fissura em pilar pode demandar reparo localizado ou reforço estrutural; a escolha entr
- Em edificação cujo projeto de segurança exige sistemas fixos de combate a incêndio, basta 
```

## Pedido 44: Controle de infecção, esterilização e microbiologia (`saude.controle-infeccao-esterilizacao`, 654 tópicos de edital)

```
Matéria: saude.controle-infeccao-esterilizacao (Controle de infecção, esterilização e microbiologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Infecções relacionadas à assistência e vigilância epidemiológica": 22
- "2. Precauções, higiene das mãos e barreiras à transmissão": 22
- "3. Limpeza, desinfecção e classificação de produtos para saúde": 19
- "4. Esterilização, centro de material e monitoramento dos ciclos": 18
- "5. Microbiologia de produtos e interpretação de ensaios": 19

Já existem (não repita):
- Em uma enfermaria, um paciente apresenta cultura de rastreio positiva para bactéria resist
- Um hospital atualiza sua política de prevenção de infecções. Qual combinação representa co
- Uma unidade registrou quatro infecções associadas a cateter em um mês, durante 2.000 catet
- Após cirurgias realizadas no serviço, alguns pacientes apresentam infecção depois da alta.
- Uma comissão nota concentração inesperada de infecções por agente semelhante em leitos pró
- Um técnico vai aferir sinais vitais de pessoa sem isolamento específico, havendo possibili
- Depois de medir a pressão arterial de um paciente, um profissional precisa manipular um ac
- Há suspeita de doença respiratória transmitida por aerossóis, e o atendimento exige entrad
- Uma profissional remove as luvas depois de cuidar de um paciente e vai registrar a evoluçã
- Na preparação da pele para punção, utiliza-se solução apropriada a tecido vivo. Qual conce
- Uma pinça que penetrará tecido estéril foi lavada, mas o setor pretende liberá-la apenas a
- Um endoscópio flexível tem contato com mucosas, sem penetração planejada em tecidos estére
- Uma lavadora encerrou o ciclo de limpeza, mas a inspeção encontra resíduos no interior de 
- Um manguito de pressão arterial toca normalmente apenas pele íntegra. Qual alternativa ind
- Um produto apresenta biofilme persistente em lúmen. A equipe propõe aumentar o desinfetant
- Uma carga de esterilização apresenta indicador químico externo alterado e registro físico 
- Durante uma rotina de esterilização a vapor, um operador pergunta como distinguir os regis
- Uma carga de implantes termina um ciclo com parâmetros físicos adequados, mas o indicador 
- Um pacote chega ao armazenamento com sua selagem rompida. A documentação comprova ciclo va
- O indicador biológico de uma carga apresenta resultado positivo, embora registros físicos 
- Um laboratório examina um creme não estéril e encontra contagem microbiana mensurável. A e
- Um produto contém conservante capaz de inibir o crescimento no meio de ensaio. A análise é
- Um ensaio de esterilidade tem amostra sem crescimento e controle negativo adequado, porém 
- Após esterilização terminal validada, uma amostra de lote não apresenta crescimento. O ser
- Um dispositivo processado apresenta ensaio microbiológico sem crescimento, mas um teste es
- Uma cultura de rastreio positiva, sem sinais de infecção, pode indicar colonização, e não 
- Uma infecção identificada após a alta deve ser excluída da vigilância de infecção de sítio
- Na investigação de possível surto, medidas de prevenção justificadas pelo risco podem ser 
- O programa hospitalar de controle de infecções pode substituir o monitoramento de indicado
- Em procedimento que envolve risco de contato com sangue, as precauções-padrão incluem sele
- O uso de luvas durante a retirada de secreções permite omitir a higiene das mãos após a re
- Ao passar do exame físico para a manipulação de um cateter no mesmo paciente, existe novo 
- Em precaução para aerossóis, máscara cirúrgica e respirador PFF2/N95 são equivalentes quan
- As precauções adicionais por contato, gotículas ou aerossóis complementam as precauções-pa
- Um artigo semicrítico que toca mucosa pode dispensar limpeza prévia se a desinfecção de al
- Um artigo que entra no sistema vascular é crítico pela classificação de risco de Spaulding
- Um manguito de pressão arterial utilizado sobre pele íntegra é semicrítico porque particip
- Uma articulação com matéria orgânica residual deve retornar à etapa de limpeza e inspeção 
- A integridade do invólucro depois de esterilização é irrelevante para disponibilização de 
- A RDC nº 15/2012 distingue registro de parâmetros físicos em cada ciclo, indicador biológi
- Quando um indicador químico externo muda de cor ao fim do ciclo, fica demonstrado que todo
- Na carga que contém implantes, a leitura negativa do indicador biológico correspondente é 
- O teste farmacopéico de esterilidade sem crescimento demonstra que todas as unidades do lo
- Um controle positivo que não apresenta crescimento esperado impede utilizar ausência de cr
- A ausência de bactérias viáveis no ensaio de esterilidade permite dispensar teste de endot
```

## Pedido 45: Fiscalização contábil, financeira e orçamentária (arts. 70 a 75) (`direito-constitucional.fiscalizacao-contabil-financeira`, 650 tópicos de edital)

```
Matéria: direito-constitucional.fiscalizacao-contabil-financeira (Fiscalização contábil, financeira e orçamentária (arts. 70 a 75))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e alcance da fiscalização constitucional": 16
- "2. Controle externo e competências do Tribunal de Contas da União": 25
- "3. Estrutura, garantias e eficácia das decisões de contas": 24
- "4. Controle interno, fiscalização legislativa e prestação de contas": 17
- "5. Simetria federativa, municípios e jurisprudência constitucional": 18

Já existem (não repita):
- Uma auditoria verificou se o serviço público alcançou o atendimento planejado com recursos
- Uma associação privada recebeu recursos federais para administrar um projeto e alegou que 
- Uma autoridade sustenta que renúncias de receitas não podem ser examinadas porque não repr
- Uma unidade federal afirma que o controle externo pertence ao TCU, enquanto o Congresso ex
- Ao fiscalizar uma subvenção concedida a entidade privada, o auditor avalia o destino dos v
- Depois de receber as contas anuais do Presidente da República, o TCU prepara sua manifesta
- Uma pessoa responsável pela gestão de recursos de uma entidade federal tem as contas exami
- Duas pessoas foram admitidas em órgão da administração federal: uma para cargo efetivo e o
- Uma comissão parlamentar de inquérito identificou despesas suspeitas em unidade administra
- Um município executa recursos enviados pela União por convênio e argumenta que somente o T
- Após identificar ato administrativo contrário à lei, o TCU fixa prazo para regularização. 
- Uma decisão do TCU imputa a ex-gestor débito e multa. A administração pretende promover a 
- Um estudo institucional descreveu o TCU como corte composta por nove membros com jurisdiçã
- Durante a escolha de novos Ministros do TCU, discute-se o preenchimento das vagas presiden
- Um candidato à nomeação para Ministro do TCU tem 68 anos, 12 anos de atividade profissiona
- A comissão mista permanente apurou indícios de despesa não autorizada e pediu explicações 
- Ao avaliar a execução de programas, o controle interno conclui haver irregularidade e deci
- Uma associação civil reúne documentos sobre aplicação irregular de recursos federais e que
- O TCU editou instrução normativa sobre forma e documentos exigidos na prestação de contas 
- Processo de concessão inicial de aposentadoria chegou ao TCU, mas sua legalidade não foi a
- Uma proposta legislativa posterior a maio de 2026 pretende extinguir um Tribunal de Contas
- A Câmara de um município tem quinze vereadores. O Tribunal de Contas emitiu parecer prévio
- Um prefeito é responsabilizado pessoalmente por irregularidades na execução de convênio qu
- Uma deliberação do Tribunal de Contas imputou débito ao responsável. Em execução proposta 
- Ao comparar o TCU e um Tribunal de Contas estadual, um estudante verificou os números de i
- Ao inventariar veículos e imóveis federais e verificar sua destinação, uma equipe realiza 
- Uma fundação de direito privado que guarde recursos públicos federais está dispensada de p
- Por solicitação de comissão parlamentar de inquérito, o TCU pode realizar auditoria em uni
- Diante de contrato federal impugnado, o TCU é a autoridade constitucional que determina di
- Uma lei que conceda renúncia de receitas não poderá ser examinada no sistema de fiscalizaç
- Se uma melhoria posterior altera os fundamentos legais de aposentadoria previamente regist
- Quando uma auditoria operacional é instaurada para analisar resultados de programa federal
- Na fiscalização de empresa supranacional de cujo capital a União participe indiretamente, 
- No sistema integrado de controle interno federal, operações de crédito, avais, garantias e
- Quando o TCU considera irregular a despesa indicada no art. 72 e a comissão prevê grave le
- Se, diante da sustação de um contrato, o Congresso ou o Executivo não adotarem em noventa 
- Por força do art. 71, § 4º, o TCU deve encaminhar ao Congresso relatórios mensais e semest
- Os Ministros do TCU têm as garantias, prerrogativas, impedimentos, vencimentos e vantagens
- Nas três vagas de Ministros do TCU escolhidas pelo Presidente da República, duas são preen
- Embora o art. 73 tenha sido alterado quanto à idade dos Ministros do TCU, em 2026 permanec
- O fato de existir Tribunal de Contas com competência de controle externo não dispensa cada
- Com a EC 139/2026, estados e municípios passaram a poder instalar novos Tribunais de Conta
- Se a Câmara Municipal deixa transcorrer longo período sem julgar as contas anuais do prefe
- O Tema 835 impede que Tribunais de Contas imponham débito e multa por irregularidades pess
- A eficácia executiva atribuída pelo art. 71, § 3º, a uma condenação em débito não torna im
```

## Pedido 46: Responsabilidade civil (`direito-civil.responsabilidade-civil`, 650 tópicos de edital)

```
Matéria: direito-civil.responsabilidade-civil (Responsabilidade civil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, ato ilícito e abuso de direito": 24
- "2. Responsabilidade subjetiva, objetiva e causalidade": 24
- "3. Danos indenizáveis e critérios de reparação": 21
- "4. Responsabilidade por terceiros, animais e coisas": 14
- "5. Situações especiais, indenização e integração dos institutos": 17

Já existem (não repita):
- Durante reforma, um morador deixa material cortante sem proteção na passagem comum e uma v
- Um proprietário utiliza deliberadamente uma faculdade de seu imóvel de maneira manifestame
- Para afastar perigo iminente, uma pessoa destrói parte do portão de terceiro inocente, no 
- Uma pessoa, por imprudência comprovada, provoca colisão e dano a veículo alheio, sem hipót
- Uma empresa desenvolve normalmente atividade cuja natureza implica risco para direitos alh
- Uma pessoa paga por conserto necessário após terceiro danificar seu equipamento. Essa desp
- Em colisão, a vítima sofre despesas de tratamento e violação de interesses existenciais. Q
- Durante a entrega de encomenda, o empregado causa dano a terceiro em razão de atividade fu
- Um cão sob custódia de seu detentor fere transeunte sem provocação. Considerando o art. 93
- Pedestre atravessa a via repentinamente em local proibido e, segundo prova conclusiva, ess
- Dois motoristas cometem imprudências que concorrem comprovadamente para colisão. Ao determ
- Em ação indenizatória, constata-se excessiva desproporção entre a gravidade leve da culpa 
- Adolescente incapaz causa prejuízo e seus responsáveis não dispõem de meios suficientes. C
- Um empregador indeniza terceiro por ato danoso de empregado maior e capaz, praticado em ra
- Um mesmo fato origina investigação criminal e pedido de reparação civil. O juízo criminal 
- Construção desaba e causa dano, comprovada falta de reparo cuja necessidade era manifesta.
- Objeto é lançado indevidamente da unidade habitada por locatário e fere pessoa na calçada.
- Motorista dirige com prudência e manutenção regular; defeito oculto de fabricação de pneu,
- Empresa coloca produto em circulação e este causa dano por característica atribuível ao pr
- Após lesão, trabalhadora comprova gastos médicos, renda perdida durante convalescença e re
- Atividade industrial polui curso d'água e causa dano ambiental comprovadamente ligado ao l
- Sociedade empresária tem sua reputação comercial atingida por afirmação ofensiva e busca r
- Campanha usa comercialmente, sem autorização, fotografia identificável de pessoa. Discute-
- Homicídio ilícito deixa familiares que recebiam alimentos da vítima e gera despesas de tra
- Três pessoas praticam conjuntamente ato ilícito lesivo; a vítima pretende exigir reparação
- Uma associação dispõe de direito legítimo, mas passa a exercê-lo de modo manifestamente in
- Ao destruir estritamente o necessário de bem pertencente a pessoa inocente para remover pe
- Uma pessoa exerce normalmente atividade naturalmente arriscada e dela decorre dano demonst
- Uma pessoa ocasionalmente realiza tarefa que termina em acidente. A mera gravidade do resu
- Depois de dano ilícito a máquina, seu proprietário paga pelo reparo e apresenta nota fisca
- Vítima apresenta dano estético permanente e sofrimento moral distintos decorrentes do mesm
- Empregado causa prejuízo a terceiro enquanto executa tarefa correspondente às suas funções
- Uma criança incapaz provoca dano e seus responsáveis não têm recursos. O art. 928 autoriza
- Animal sob detenção de pessoa fere pedestre. Na hipótese regida pelo art. 936 do Código Ci
- Parte de uma construção desaba por ausência de reparos cuja necessidade era manifesta. O a
- De unidade ocupada por locatário cai objeto em lugar indevido e causa dano a passante. O a
- Responsável indeniza terceiro por ato de seu filho relativamente incapaz. Pelo art. 934, e
- Em acidente, a vítima e o ofensor concorrem culposamente para o resultado. O art. 945 orie
- Em processo indenizatório, a vítima não contribuiu para o resultado, mas o juiz cogita red
- Uma lesão comprovadamente ligada à conduta ilícita reduz de modo permanente a capacidade p
- Em ação civil por ato danoso, a defesa alega que qualquer decisão penal absolutória exting
- Poluidor causa dano ecológico em razão de sua atividade, e o nexo está demonstrado. A ausê
- Associação ajuíza demanda coletiva por danos patrimoniais a conjunto paisagístico e histór
- Empresa coloca produto em circulação e este causa prejuízo diretamente atribuível ao produ
- Duas pessoas participam da mesma ofensa ilícita que produz dano indivisível a terceiro. O 
```

## Pedido 47: Procedimento comum (`processo-civil.procedimento-comum`, 648 tópicos de edital)

```
Matéria: processo-civil.procedimento-comum (Procedimento comum)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura do procedimento comum e elaboração da petição inicial": 25
- "2. Emenda, indeferimento e improcedência liminar": 13
- "3. Autocomposição e audiência de conciliação ou mediação": 13
- "4. Providências preliminares, saneamento, provas e julgamento no rito": 34
- "5. Tutelas provisórias e visão geral dos procedimentos especiais": 15

Já existem (não repita):
- Uma ação cível não possui rito específico na lei. Ao escolher o procedimento de conhecimen
- Em procedimento especial, a lei não define como praticar determinado ato. A regra compatív
- Um autor dispõe dos dados básicos do réu, mas não consegue descobrir seu CPF. É possível c
- O juiz identifica defeito sanável na petição inicial, indica exatamente o documento indisp
- A petição inicial não contém pedido nem causa de pedir e seu autor não realiza a correção 
- Uma causa dispensa produção de provas e seu pedido contraria diretamente súmula pertinente
- O juiz vai designar audiência de conciliação do art. 334 e precisa respeitar a antecedênci
- A autora indica na inicial que não deseja conciliar, enquanto o réu manifesta interesse em
- O processo necessita produção de prova sobre fatos controvertidos. Na decisão de saneament
- Em audiência inicial regularmente designada, uma parte falta sem justificativa, embora ten
- Após a citação e antes do saneamento, o autor pretende modificar a causa de pedir. O réu c
- Após sofrer um dano cuja extensão depende de perícia ainda impossível de concluir, o autor
- Uma ação contém pedidos subsidiários: a providência principal tem valor de R$ 80 mil, e a 
- O juiz entende que a pretensão contraria precedente qualificado, mas há controvérsia fátic
- Após saneamento, foi determinada prova testemunhal e as partes não estão em audiência coop
- Os documentos juntados bastam para julgar um pedido, sem necessidade de perícia ou audiênc
- Um credor apresenta documentos suficientes e invoca tese firmada em recursos repetitivos, 
- O autor deseja cumular pedidos sujeitos a técnicas procedimentais distintas, mas cuja apre
- Em ação revisional de financiamento, o autor apenas pede revisão genérica do contrato, não
- Há dois réus e apenas um deles declara não querer audiência de conciliação, enquanto o aut
- No saneamento, o juízo identifica controvérsia factual complexa e pretende delimitar quest
- Há forte probabilidade do direito e perigo da demora, mas a medida solicitada é antecipató
- Dois interessados buscam tutelas antecedentes: um obtém tutela antecipada nos termos do ar
- Um pedido de tutela da evidência é formulado liminarmente: há prova documental e tese firm
- Um credor dispõe de prova escrita da dívida, mas o documento não possui eficácia de título
- Ao ajuizar demanda cível sem procedimento específico, o autor deve obrigatoriamente escolh
- A petição inicial deve indicar fatos e fundamentos jurídicos, pedido, valor da causa, prov
- Diante de inicial sanável, o art. 321 autoriza o indeferimento imediato, dispensando que o
- Se o pedido contrariar súmula do STF e a causa dispensar fase instrutória, o juiz poderá j
- A indicação de desinteresse apenas do autor, na petição inicial, dispensa a audiência do a
- A tutela provisória de urgência depende de elementos que indiquem probabilidade do direito
- No art. 334 do CPC, a citação do réu deverá ocorrer com antecedência mínima de trinta dias
- Após citação válida e antes do saneamento, o autor poderá alterar pedido ou causa de pedir
- O juiz que identifica imediatamente prescrição pode somente indeferir a petição inicial se
- Concluído o saneamento, as partes possuem prazo comum de cinco dias para solicitar esclare
- Em demanda que exige prova testemunhal, a decisão de saneamento permite automaticamente de
- Um dos pedidos cumulados está pronto para julgamento, mas outro necessita prova pericial. 
- A tutela da evidência do art. 311 depende cumulativamente de probabilidade do direito e ri
- Uma pretensão acompanhada exclusivamente de documentos e tese firmada em recursos repetiti
- A estabilização da tutela antecipada antecedente descrita no art. 304 produz automaticamen
- Concedida tutela antecipada antecedente, o aditamento da inicial deve ocorrer em quinze di
- A improcedência liminar pode basear-se em súmula de qualquer tribunal sobre qualquer matér
- As partes podem submeter delimitação consensual de questões fáticas e jurídicas à homologa
- Na concessão de tutela de urgência, o art. 300, § 1º, exige caução em todos os casos, sem 
- Na ação monitória, a existência de prova escrita sem força de título executivo fundamenta 
```

## Pedido 48: Circuitos, sinais, controle e automação (`engenharia-eletrica.circuitos-sinais-controle-automacao`, 646 tópicos de edital)

```
Matéria: engenharia-eletrica.circuitos-sinais-controle-automacao (Circuitos, sinais, controle e automação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Leis de circuitos, análise nodal, malhas e equivalentes": 19
- "2. Circuitos dinâmicos, regime senoidal e quadripolos": 21
- "3. Sinais, sistemas LIT e transformadas": 20
- "4. Modelagem, estabilidade e projeto de controle": 21
- "5. Automação industrial, CLP, instrumentação e redes": 19

Já existem (não repita):
- Um resistor de 2 Ω está ligado entre um nó a 6 V e o nó de referência, de 0 V. Considerand
- Em um nó de um circuito, entram 4 A por um ramo e saem 1 A por outro. A corrente no tercei
- Uma fonte ideal de tensão de 12 V em série com resistência de 4 Ω alimenta uma carga exter
- Um circuito linear contém uma fonte independente de tensão e uma fonte de corrente control
- Uma rede RC de primeira ordem apresenta resistência equivalente de 10 kΩ vista pelo capaci
- Após a comutação de um circuito RL, a resistência equivalente vista pelo indutor é 4 Ω e a
- Um RLC série possui R=4 Ω, L=1 H e C=0,25 F. A classificação de sua resposta natural, a pa
- Uma rede linear de duas portas é descrita por parâmetros z, com correntes convencionadas e
- Um resistor de 3 Ω e um indutor de reatância de +4 Ω estão em série sob fonte senoidal. O 
- Uma carga puramente resistiva de 20 Ω recebe tensão senoidal de 100 V eficazes. Qual potên
- Em um sistema linear e invariante no tempo inicialmente em repouso, aplica-se a entrada im
- Ao calcular a transformada unilateral de Laplace da derivada dx/dt de um sinal com x(0−)=3
- Um sinal x(t) é atrasado em τ segundos sem mudança de amplitude. Na convenção X(jω)=∫x(t)e
- Uma senoide de 300 Hz é amostrada uniformemente a 400 amostras por segundo, sem filtro ant
- Considere um sistema discreto causal com função de transferência H(z)=1/(1−0,5z⁻¹). O juíz
- Um controlador opera com planta G(s)=4/(s+2) e sensor H(s)=1 em realimentação negativa. Qu
- Considere A=[[0,1],[0,0]] e B=[[0],[1]] em um modelo de estados contínuo de ordem 2. Sobre
- Para o polinômio característico contínuo p(s)=s³+2s²+3s+4, a primeira coluna da tabela de 
- Considere realimentação unitária negativa e planta G(s)=10/[s(s+2)]. Suponha a malha fecha
- O sistema escalar contínuo ẋ=−x+u é controlado por realimentação de estado u=−2x+r. Admit
- Em uma malha com medição ruidosa de alta frequência, decide-se acrescentar a componente de
- Uma planta industrial apresenta ganho variável ao longo do turno. Um controlador atualiza 
- Um projeto de automação utiliza Modbus TCP e PROFINET em segmentos distintos da planta. So
- Um transmissor linear converte pressão de 0 a 200 kPa em sinal de 4 a 20 mA. Se um registr
- Em uma lógica Ladder de comando de motor, pretende-se que o botão de partida seja momentân
- Em um painel com três ramos conectados a um mesmo nó, a soma das correntes que entram deve
- Durante a análise de um circuito resistivo linear com duas fontes independentes, o cálculo
- Ao fechar uma chave em circuito RC com resistências finitas e sem fontes impulsivas, a ten
- Uma equipe examina um circuito RL submetido a uma fonte de tensão CC estável há muito temp
- Um transmissor de nível com saída linear 4–20 mA e faixa de 0% a 100% emite 4 mA ao medir 
- Em uma planta com CLP local e tela SCADA de supervisão, a perda apenas da tela SCADA impli
- Para identificar z11 de um quadripolo sob a convenção de correntes entrando nas duas porta
- Um sistema LIT discreto possui resposta ao impulso h[n]=(0,5)^n u[n]. O sistema é BIBO est
- Um sinal senoidal de 600 Hz, após amostragem uniforme a 800 amostras por segundo e sem fil
- Ao obter H(s)=Y(s)/X(s) de um sistema LIT para estudar resposta a entradas, assume-se usua
- Um sistema discreto causal possui função de transferência H(z)=1/(1−1,2z⁻¹). Por ser um si
- Em um processo no qual o CLP foi configurado para controlar localmente as saídas, a indisp
- O polinômio característico s³+s²+s+2 representa necessariamente um sistema estável, pois t
- Para o polinômio s³+3s²+2s+4, a primeira coluna de Routh é 1, 3, 2/3, 4. Conclui-se que nã
- Em uma realização de estados não mínima, a estabilidade BIBO observada em uma função de tr
- Em modelo de estados contínuo de ordem n, se a matriz de controlabilidade [B AB ... A^(n−1
- Em um controle PID sujeito a saturação do atuador, acrescentar a componente derivativa eli
- Em lógica Ladder de partida e retenção, colocar a condição permissiva de parada em série c
- A edição IEC 61131-3:2025 apresenta Instruction List (IL) como uma das linguagens textuais
- Uma malha adaptativa altera os ganhos do controlador durante a operação. Esse fato, isolad
```

## Pedido 49: Funções afim e quadrática (`matematica.funcoes`, 645 tópicos de edital)

```
Matéria: matematica.funcoes (Funções afim e quadrática)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Relações, definição, domínio, imagem, igualdade e classificações": 15
- "2. Função afim, taxa de variação, gráficos, sinal e inequações lineares": 38
- "3. Função quadrática: concavidade, vértice, zeros, imagem e sinal": 22
- "4. Composição, inversa, igualdade formal, paridade e monotonicidade": 13
- "5. Exponenciais, logaritmos, inequações e modelos comparados": 12

Já existem (não repita):
- Uma relação de A={0,1,2} para B={1,2,3} associa 0 a 1, 1 a 1 e 2 a 3. A respeito da defini
- O custo de uma operação é modelado por uma expressão com raiz quadrada real: $$f(x)=\sqrt{
- A função $$f:\mathbb{R}\to\mathbb{R},\quad f(x)=x^2$$ foi usada em um gráfico. Que conjunt
- Uma associação de A={1,2,3} para B={a,b,c} envia 1 para a, 2 para b e 3 para c. Como se cl
- Uma cobrança varia conforme $$f(x)=4x-12.$$ Em qual quantidade real a cobrança líquida mod
- O saldo de um modelo é dado por $$f(x)=-3x+9.$$ Em quais entradas reais o saldo é estritam
- Uma parábola representa $$f(x)=x^2-5x+6.$$ Quais são as duas interseções do gráfico com o 
- A receita de uma atividade pode ser aproximada por $$R(x)=-(x-2)^2+7.$$ Qual ponto indica 
- Uma quantidade é multiplicada por um terço a cada período e segue a função $$f(t)=(1/3)^t.
- Considere f dos reais para os reais, f(x)=x+1, e g dos reais não negativos para os reais, 
- Uma função é definida por $$f:[0,+\infty)\to[0,+\infty),\quad f(x)=x^2.$$ Como se classifi
- Uma escala converte entradas pela função $$f(x)=3x-6,$$ dos reais nos reais. Qual lei perm
- Se $$f(x)=x^2$$ e $$g(x)=x+2,$$ quais valores são obtidos, respectivamente, em $$(f\circ g
- O gráfico da função $$f(x)=x^3-x,$$ com domínio real, foi examinado quanto à simetria. Qua
- Para $$f(x)=x^2-4x+3,$$ determine todos os números reais que atendem à condição $$f(x)\le0
- Uma inequação é dada por $$\log_{1/2}(x+1)\ge-1.$$ Qual intervalo contém todas as soluções
- Um índice é dado pelo quociente $$(x-1)/(x+2).$$ Para quais entradas reais ele é menor ou 
- Se $$f(t)=\sqrt{t}$$ tem domínio real não negativo e $$g(x)=3-x^2$$ tem domínio real, dete
- Definiu-se $$f:(-\infty,0]\to[0,+\infty),\quad f(x)=x^2.$$ Qual lei representa a inversa c
- Uma transformação reversível em reais é $$f(x)=-2x+8.$$ Qual função recupera o valor origi
- Uma grandeza obedece à inequação $$\left(\frac13\right)^{2x-1}<\left(\frac13\right)^3.$$ Q
- O modelo utiliza a expressão $$\log_2(x^2-5x+6).$$ Para quais entradas reais a expressão l
- Duas funções usam a mesma regra x² e o mesmo domínio real. A primeira tem contradomínio R;
- Um índice de rendimento é dado por $$P(x)=-(x-5)^2+40,$$ mas apenas entradas reais no inte
- Se $$f(x)=2x+1$$ e $$g(x)=x^2,$$ quais entradas reais resolvem $$(g\circ f)(x)=9$$?
- Uma relação definida sobre A={1,2} associa 1 a 5 e 2 também a 5. Ela pode ser função, porq
- Como a função $$f:\mathbb{R}\to\mathbb{R},\ f(x)=x^2$$ possui contradomínio real, ela deve
- Para a função $$f(x)=2x+4,$$ a condição de positividade estrita corresponde ao conjunto de
- Uma reta descrita pela lei $$f(x)=-2x+4$$ é estritamente crescente, porque intercepta o ei
- Uma função quadrática com coeficiente principal negativo, definida em todos os reais, apre
- A expressão $$\sqrt{x-1}$$ só existe nos reais quando x>1, porque a raiz quadrada real é i
- Duas funções declaradas com o mesmo domínio, o mesmo contradomínio e saídas iguais em cada
- As funções $$f:\mathbb{R}\to\mathbb{R}$$ e $$g:\mathbb{R}\to[0,+\infty),$$ ambas dadas por
- Para uma função real representada no plano, se alguma reta horizontal atravessa seu gráfic
- Para $$f:\mathbb{R}\to\mathbb{R},\quad f(x)=x^2,$$ pode-se afirmar que a função é bijetora
- Se $$f(t)=\sqrt{t}$$ e $$g(x)=x-2,$$ a composta f∘g tem domínio real [2,+∞), mesmo que g, 
- Para quaisquer funções afins f e g com domínios reais, a composição f∘g é idêntica à compo
- A função real $$f(x)=x^4-3$$ é par, porque substituindo x por −x obtém-se o mesmo valor e 
- Na base logarítmica 1/2, a desigualdade $$\log_{1/2}(x)>\log_{1/2}(4)$$ é equivalente a x>
- Se uma função quadrática de domínio real tem coeficiente principal positivo e discriminant
- A função $$f:\mathbb{R}\to\mathbb{R},\ f(x)=x^2$$ possui inversa global como função real d
- Quando uma função bijetora dos reais nos reais admite inversa, o gráfico dessa inversa pod
- Na inequação $$(x-1)/(x+2)\le0,$$ o número x=−2 deve entrar no conjunto solução, pois a de
- A função $$f:\mathbb{R}\to\mathbb{R},\ f(x)=x^3$$ é bijetora: tem saídas distintas para en
- Para a função $$f:\mathbb{R}\to\mathbb{R},\ f(x)=x^2,$$ a existência da pré-imagem do conj
```

## Pedido 50: Lógica de primeira ordem (`raciocinio-logico.logica-primeira-ordem`, 643 tópicos de edital)

```
Matéria: raciocinio-logico.logica-primeira-ordem (Lógica de primeira ordem)
Capítulos e quantas questões quero em cada um (total 100):
- "Predicados, sentenças abertas e universo de discurso": 20
- "Quantificadores: leitura, escopo e valor lógico": 20
- "Negação de proposições quantificadas": 20
- "Quantificadores múltiplos e tradução": 20
- "Validade de argumentos e como o tema costuma ser cobrado": 20

Já existem (não repita):
- Qual das expressões abaixo é uma sentença aberta?
- Considere o universo U = {1, 2, 3, 4} e o predicado P(x): “x é par”. Qual é o conjunto-ver
- No universo U = {1, 2, 3}, qual das proposições é verdadeira?
- Qual é a leitura correta da fórmula ∃x P(x)?
- Qual fórmula é equivalente a ¬∀x P(x)?
- Seja P(x): “x é múltiplo de 3”, com universo dos números naturais. Qual afirmação está cor
- Na expressão ∀x (x > y), com universo dos números inteiros, qual afirmação é correta?
- Qual é a negação de “Existe aluno que faltou à aula”?
- No universo U = {−1, 0, 1, 2}, qual proposição é verdadeira?
- Qual é a negação de ∀x (P(x) → Q(x))?
- Qual é a negação de “Algum processo foi arquivado e não foi digitalizado”?
- No universo dos números inteiros, considere P: ∀x ∃y (y > x) e Q: ∃y ∀x (y > x). Qual afir
- Sendo A(x, y): “x admira y”, no universo das pessoas, qual fórmula traduz “Alguém admira t
- Sendo A(x): “x foi aprovado” e N(x): “x foi nomeado”, qual fórmula traduz “Somente os apro
- Das premissas ∀x (S(x) → P(x)) e S(a) conclui-se P(a). Qual justificativa descreve esse ra
- Analise o argumento: “Todo médico é graduado. Ana é graduada. Logo, Ana é médica.” Qual di
- No universo U = {1, 2, 3}, considere I: ∀x ∃y (x + y = 4); II: ∃x ∀y (x + y ≤ 4); III: ∃y 
- Qual é a negação de “Todo candidato domina algum tema”?
- Das premissas ∃x (P(x) ∧ Q(x)) e ∀x (Q(x) → R(x)), qual conclusão decorre necessariamente?
- No universo dos números inteiros, qual é a situação da proposição ∀x ((x² < 0) → x = 5)?
- Qual das equivalências abaixo é válida para quaisquer predicados P e Q?
- Para mostrar que o argumento ∀x (P(x) → Q(x)), ∃x (Q(x) ∧ R(x)) ∴ ∃x (P(x) ∧ R(x)) é invál
- Qual fórmula expressa “existe exatamente um x tal que P(x)”?
- Qual é a negação de ∃x ∀y (P(x, y) → Q(y))?
- Das premissas ∀x (A(x) → ¬B(x)) e ∃x (C(x) ∧ B(x)), qual conclusão é válida?
- A sentença aberta “x + 2 = 5” torna-se proposição quando se atribui um valor a x ou quando
- A proposição ∃x P(x) afirma que existe exatamente um elemento do universo com a propriedad
- A negação de ∃x P(x) é ∃x ¬P(x).
- No universo U = {2, 4, 6}, a proposição ∀x (x é par) é verdadeira.
- A fórmula ¬∃x P(x) é equivalente a ∀x ¬P(x).
- Sendo P(x): “x > 3”, a expressão P(x) já possui valor lógico definido, mesmo sem universo 
- Para mostrar que ∀x P(x) é falsa, basta exibir um único elemento do universo que não satis
- As fórmulas ∀x ∃y P(x, y) e ∃y ∀x P(x, y) são sempre equivalentes.
- No universo dos números naturais {0, 1, 2, ...}, a proposição ∀x (x + 1 > x) é falsa, porq
- A tradução de “Todo aluno aprovado recebeu certificado” é ∀x (A(x) → C(x)), e não ∀x (A(x)
- A frase “Algum servidor é auditor” traduz-se corretamente por ∃x (S(x) → A(x)).
- Na lógica de primeira ordem clássica, cujo universo é sempre não vazio, se ∀x P(x) é verda
- A negação de ∀x (P(x) ∧ Q(x)) é ∃x (¬P(x) ∨ ¬Q(x)).
- O argumento “Todo atleta treina; Bruno não é atleta; logo, Bruno não treina” é válido.
- A fórmula ∃x (P(x) ∧ Q(x)) implica ∃x P(x) ∧ ∃x Q(x), mas a recíproca nem sempre vale.
- A fórmula ∀x (P(x) ∨ Q(x)) é equivalente a ∀x P(x) ∨ ∀x Q(x).
- A negação de “Todo número natural tem um natural maior que ele” é “Nenhum número natural t
- Das premissas ∀x (P(x) → Q(x)) e ∃x ¬Q(x), conclui-se validamente ∃x ¬P(x).
- No universo U = {1, 2, 3}, a proposição ∃x ∀y (x ≤ y) é verdadeira.
- Das premissas ∃x P(x) e ∃x Q(x), conclui-se validamente ∃x (P(x) ∧ Q(x)).
```

