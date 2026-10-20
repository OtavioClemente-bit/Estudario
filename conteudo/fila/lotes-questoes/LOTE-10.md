# Pedidos de questões extras, lote 10 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 11480 tópicos de edital.

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

## Pedido 1: Legislação da Anvisa: SNVS, Infrações Sanitárias e Registro de Produtos (`regulacao-sanitaria.legislacao-anvisa-snvs-infracoes`, 247 tópicos de edital)

```
Matéria: regulacao-sanitaria.legislacao-anvisa-snvs-infracoes (Legislação da Anvisa: SNVS, Infrações Sanitárias e Registro de Produtos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. SNVS e Anvisa (Lei 9.782/1999)": 25
- "2. Organização e atos da agência (Decreto 3.029/1999 e regimento)": 25
- "3. Infrações sanitárias (Lei 6.437/1977)": 25
- "4. Registro e funcionamento (Lei 6.360/1976 e Decreto 8.077/2013)": 25

Já existem (não repita):
- A Anvisa é uma autarquia sob regime especial vinculada ao Ministério da Saúde.
- Caracteriza o regime especial da Anvisa, nos termos da Lei 9.782/1999:
- O Sistema Nacional de Vigilância Sanitária é composto exclusivamente pela Anvisa e por sua
- A finalidade institucional da Anvisa, segundo a Lei 9.782/1999, é:
- Compete à Anvisa exercer a vigilância sanitária de portos, aeroportos e fronteiras.
- Diante de violação da legislação pertinente ou de risco iminente à saúde, a Anvisa pode:
- A Taxa de Fiscalização de Vigilância Sanitária é uma das receitas da Anvisa e decorre do e
- Entre os bens e produtos submetidos ao controle e à fiscalização sanitária pela Anvisa, in
- A Anvisa é dirigida por uma diretoria colegiada, cujos membros exercem mandato fixo, e con
- O Decreto 3.029/1999 tem como objeto:
- As Resoluções da Diretoria Colegiada (RDC) são os principais atos normativos da Anvisa.
- O instrumento de planejamento que reúne os temas prioritários a serem regulados pela agênc
- A análise de impacto regulatório é feita após a publicação da norma, apenas para medir seu
- Na consulta pública de uma proposta normativa da Anvisa:
- A Anvisa disponibiliza consulta pública à regularidade de empresas e produtos, como regist
- As decisões da diretoria colegiada da Anvisa são tomadas por maioria de seus membros.
- Segundo a Lei 6.437/1977, responde pela infração sanitária:
- Exclui-se a imputação de infração sanitária quando a irregularidade decorre de força maior
- Não figura entre as penalidades previstas na Lei 6.437/1977:
- Pela Lei 6.437/1977, as infrações sanitárias classificam-se em leves, graves e gravíssimas
- Constitui circunstância atenuante na aplicação de penalidade sanitária:
- A reincidência e a obtenção de vantagem pecuniária com a infração são circunstâncias agrav
- Havendo concurso de circunstâncias atenuantes e agravantes, a pena sanitária deve ser apli
- No processo de análise fiscal, sendo o resultado condenatório, a perícia de contraprova é 
- As infrações sanitárias previstas na Lei 6.437/1977 são imprescritíveis, por envolverem a 
- O processo administrativo sanitário, na Lei 6.437/1977, inicia-se com:
- Diante de risco à saúde, a autoridade sanitária pode determinar cautelarmente a interdição
- A intervenção, como penalidade sanitária, é aplicável a estabelecimento que:
- Pela Lei 6.360/1976, nenhum medicamento, inclusive importado, pode ser industrializado, ex
- Estão abrangidos pela Lei 6.360/1976, entre outros:
- Para o registro de um medicamento, exige-se a comprovação de:
- A autorização de funcionamento de empresa (AFE) e a licença sanitária do estabelecimento s
- As empresas sujeitas à Lei 6.360/1976 devem contar com responsável técnico legalmente habi
- O Decreto 8.077/2013:
- O monitoramento de produtos após a comercialização, como a farmacovigilância e a tecnovigi
- O registro de produto na Lei 6.360/1976 tem validade indeterminada, dispensando qualquer r
- Sobre rotulagem e propaganda de produtos sujeitos à vigilância sanitária, é correto afirma
- Comprovada a falta de segurança ou de eficácia de um produto registrado, o registro pode s
- Uma empresa importadora coloca à venda um saneante sem o registro exigido. Um distribuidor
- Sobre a relação entre a Anvisa e os demais entes do Sistema Nacional de Vigilância Sanitár
```

## Pedido 2: Cuidados paliativos e bioética (`saude.cuidados-paliativos-bioetica`, 247 tópicos de edital)

```
Matéria: saude.cuidados-paliativos-bioetica (Cuidados paliativos e bioética)
Capítulos e quantas questões quero em cada um (total 100):
- "Princípios bioéticos, autonomia, consentimento e sigilo": 17
- "Terminalidade, limites terapêuticos e diretivas antecipadas": 17
- "Fundamentos e organização dos cuidados paliativos no SUS": 17
- "Avaliação e manejo dos sintomas em cuidados paliativos": 17
- "Comunicação, luto e cuidados na UTI e na atenção primária": 16
- "Ética em pesquisa com seres humanos": 16

Já existem (não repita):
- Uma pessoa adulta, orientada e capaz recusa procedimento invasivo após compreender riscos 
- Qual é a melhor descrição do consentimento livre e esclarecido na assistência?
- Um homem lúcido pede à equipe que não revele seu diagnóstico à vizinha, que insiste em obt
- Para decidir sobre procedimento de alto risco, qual conduta demonstra avaliação adequada d
- Em hospital com recursos escassos, a gestão estabelece critérios clínicos transparentes pa
- Paciente com doença terminal recebe controle intensivo de sintomas enquanto a equipe evita
- O que caracteriza a distanásia?
- Qual diferença melhor distingue planejamento antecipado de cuidados (PAC) e diretivas ante
- Uma equipe considera sedação paliativa em pessoa com sofrimento refratário. Qual condição 
- Mulher capaz declara que, em futura fase irreversível, não quer ventilação invasiva sem be
- Qual grupo pode receber cuidados paliativos?
- A equipe multiprofissional de cuidados paliativos deve prioritariamente:
- Na PNCP atualizada em 2026, qual alternativa descreve corretamente as equipes de apoio?
- No planejamento de cuidado paliativo, qual item integra adequadamente um Projeto Terapêuti
- Pessoa com doença neurológica avançada recebe tratamento específico e apresenta sofrimento
- Um paciente com dor persistente precisa de reavaliação paliativa. Qual é o melhor primeiro
- Qual afirmação descreve adequadamente a dispneia?
- Pessoa em uso de opioide apresenta piora progressiva de constipação. Qual abordagem é mais
- Paciente internado desenvolve desatenção, sonolência e flutuação cognitiva, sem agitação m
- Pessoa em cuidados paliativos relata dispneia intensa, mas não tem hipoxemia documentada. 
- Ao comunicar um diagnóstico grave, qual atitude favorece a compreensão e o vínculo?
- Sobre o luto, qual afirmação está correta?
- Em uma UTI, a equipe considera limitar intervenção invasiva sem benefício proporcional. Qu
- No domicílio, uma pessoa com doença avançada sofre crises recorrentes à noite. Qual medida
- Em 2026, qual arranjo institucional corresponde ao marco brasileiro de ética em pesquisa c
- Os princípios da beneficência e da não maleficência são sinônimos: ambos exigem apenas pro
- O sigilo profissional pode persistir mesmo após a morte da pessoa atendida.
- O consentimento do paciente pode ser considerado esclarecido mesmo quando ele não recebeu 
- Uma pessoa com diagnóstico de demência deve ser considerada incapaz para toda e qualquer d
- Em cuidados paliativos, a eutanásia corresponde à mesma prática que controlar dor intensa 
- Evitar procedimentos obstinados e inúteis em paciente terminal pode ser compatível com a é
- As diretivas antecipadas de vontade podem tornar eticamente obrigatório executar qualquer 
- O planejamento antecipado de cuidados tem valor apenas após o paciente perder capacidade d
- Pessoas com doenças não oncológicas também podem receber cuidados paliativos conforme suas
- A Política Nacional de Cuidados Paliativos determina que todas as ações paliativas ocorram
- Na organização atualizada da PNCP, a EMCP desempenha apoio macrorregional e matriciamento,
- Na avaliação de dor em pessoa capaz de se comunicar, o relato do próprio paciente é inform
- Delirium hipoativo é impossível, pois a presença de agitação intensa constitui critério ob
- Em paciente com dispneia e saturação normal, a ausência de hipoxemia exclui sofrimento res
- Sedação paliativa e eutanásia são termos equivalentes porque ambas têm por objetivo clínic
- O acolhimento ao luto pode integrar a assistência de familiares e cuidadores antes e depoi
- A técnica SPIKES dispensa investigar o que o paciente deseja saber porque todos precisam r
- Em UTI, a decisão proporcional de não oferecer medida fútil implica interromper também ana
- A análise ética por Comitê de Ética em Pesquisa e o consentimento do participante são proc
- A legislação brasileira contemporânea sobre pesquisa com seres humanos admite que dados pe
```

## Pedido 3: Conhecimentos sobre o Estado de Sergipe: história, geografia, economia e cultura (`sergipe.conhecimentos-estaduais`, 246 tópicos de edital)

```
Matéria: sergipe.conhecimentos-estaduais (Conhecimentos sobre o Estado de Sergipe: história, geografia, economia e cultura)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sergipe no Brasil e no Nordeste; condicionantes geoambientais": 25
- "2. Ocupação, povoamento e sociedade colonial": 25
- "3. Emancipação, República e organização do espaço": 25
- "4. População, rede urbana, economia contemporânea e cultura": 25

Já existem (não repita):
- Sergipe é o menor estado brasileiro em extensão territorial.
- O rio que estabelece a divisa entre Sergipe e Alagoas é o:
- O clima de Sergipe é semiárido em todo o território, inclusive no litoral.
- Entre o litoral e o interior de Sergipe, a unidade de relevo formada por superfícies plana
- A Usina Hidrelétrica de Xingó, cujo reservatório formou cânions explorados pelo turismo, l
- A vegetação predominante no sertão sergipano é a:
- Os principais rios sergipanos correm, em geral, no sentido oeste-leste, em direção ao Ocea
- A conquista militar do território sergipano e a fundação de São Cristóvão, em 1590, são at
- No período colonial, o território de Sergipe del Rey permaneceu subordinado à Bahia.
- Entre as economias fundadoras de Sergipe, a atividade que ocupou o interior e o sertão, us
- O Vale do Cotinguiba, com solos de massapê, foi área importante de engenhos de cana-de-açú
- Na sociedade colonial sergipana, o poder político e econômico concentrava-se:
- O povo indígena Xokó, reconhecido em Sergipe, vive na Ilha de São Pedro, no município de:
- A comunidade quilombola da Mussuca, conhecida pelo samba de pareia, localiza-se no municíp
- Durante o século XVII, a região de Sergipe foi atingida pela ocupação holandesa, com destr
- A data magna de Sergipe, que marca sua emancipação em relação à Bahia, é:
- A capital de Sergipe foi transferida de São Cristóvão para Aracaju em 1855, entre outros m
- Lampião e Maria Bonita foram mortos em 1938 na Grota de Angico, localizada no município se
- A descoberta de petróleo em Carmópolis, na década de 1960, impulsionou a presença da Petro
- A única mina de potássio em operação no Brasil, a de Taquari-Vassouras, fica no município 
- O Estado de Sergipe é dividido em 75 municípios.
- NÃO é um dos territórios de planejamento adotados pelo governo de Sergipe:
- Na divisão clássica do IBGE, as mesorregiões de Sergipe são o Sertão Sergipano, o Agreste 
- Na regionalização geoeconômica tradicional de Sergipe, o agreste caracteriza-se por:
- Compõem a Região Metropolitana de Aracaju os municípios de Aracaju, Nossa Senhora do Socor
- Itabaiana destaca-se no interior sergipano como centro de comércio, de feira e de transpor
- A população de Sergipe é majoritariamente rural, e Aracaju concentra parcela pequena dos h
- O setor que responde pela maior parte do PIB sergipano é o de:
- As descobertas de petróleo e gás em águas profundas da Bacia de Sergipe-Alagoas são vistas
- A citricultura, especialmente a produção de laranja, tem destaque no centro-sul sergipano,
- A Praça São Francisco, reconhecida como Patrimônio Mundial pela UNESCO em 2010, localiza-s
- Laranjeiras é reconhecida como centro de manifestações culturais afro-brasileiras e popula
- O barco de fogo, tradição dos festejos juninos sergipanos, é característico do município d
- O povoado de Mangue Seco, famoso por suas dunas, fica em território sergipano, na margem n
- Entre os desafios do desenvolvimento sergipano está a segurança hídrica no sertão, sujeito
- A orla de Atalaia, um dos principais cartões-postais turísticos do estado, localiza-se em:
- A industrialização sergipana a partir da segunda metade do século XX incluiu a produção de
- Os manguezais sergipanos concentram-se nos estuários dos rios, próximos ao litoral, e sust
- Na dinâmica demográfica recente de Sergipe, observa-se:
- A transferência da capital de São Cristóvão para Aracaju, em 1855, foi promovida pelo pres
```

## Pedido 4: Biblioteconomia: catalogação, classificação e indexação (`biblioteconomia.representacao-descritiva-tematica`, 245 tópicos de edital)

```
Matéria: biblioteconomia.representacao-descritiva-tematica (Biblioteconomia: catalogação, classificação e indexação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da catalogação e da descrição bibliográfica": 18
- "2. AACR2, ISBD, RDA, FRBR e autoridades": 19
- "3. MARC 21, identificadores e interoperabilidade": 19
- "4. Classificação bibliográfica: CDD e CDU": 17
- "5. Indexação, tesauros e política de indexação": 27

Já existem (não repita):
- Uma biblioteca precisa separar dados de edição e autoria dos assuntos tratados num manual 
- Uma instituição possui três exemplares idênticos da mesma edição, cada um com código patri
- Uma equipe recebe registros produzidos por várias bibliotecas participantes e os revisa em
- Uma usuária solicita a tradução de um romance para o português; a biblioteca tem também ex
- Uma equipe precisa transportar registros bibliográficos estruturados entre sistemas com ca
- Na revisão de um registro MARC 21, o responsável pretende corrigir o título propriamente d
- Uma biblioteca atribui números da Classificação Decimal de Dewey a obras gerais sobre ling
- Um tesauro apresenta remissiva USE do termo popular escolhido na busca para outro descrito
- Uma obra impressa recebe metadados MARC 336, 337 e 338 segundo categorias utilizadas com R
- Um serviço reúne formas autorizadas de um mesmo escritor provenientes de catálogos naciona
- Um livro apresenta o título `Água limpa`, o subtítulo `gestão integrada` e uma indicação d
- Uma biblioteca oferece leitura digital e impressa de obra monográfica em produtos editoria
- Ao comparar CDD e CDU, um catalogador encontra o sinal `:` entre duas notações CDU. Qual o
- Num catálogo, os termos `educação`, `Brasil` e `história` são atribuídos separadamente e c
- Numa avaliação de busca, foram recuperados 12 documentos, dos quais 9 relevantes; no acerv
- Uma biblioteca encontra no catálogo a descrição de uma nova edição publicada em 2025, mas 
- Ao migrar catálogo AACR2 para sistema que admite RDA e MARC 21, a equipe pergunta qual afi
- Em dois catálogos, uma mesma edição aparece em cinco cópias físicas. Uma delas tem anotaçõ
- Um registro MARC contém `264 #1 $a Recife : $b Horizonte, $c 2024.` Qual interpretação est
- Um catalogador precisa codificar uma relação composta da CDU de maneira que a sequência do
- Em um tesauro, `Poluição do ar` possui a relação TR com `Saúde coletiva`. A interpretação 
- Uma política institucional eleva a exaustividade da indexação sem mudar automaticamente o 
- Uma mesma obra tem duas traduções distintas para o português, editadas em volumes separado
- Uma biblioteca importa registro MARC 21 com título correto no 245, mas trata 008 como se f
- Uma biblioteca especializada encontra baixa recuperação de estudos sobre clima porque a eq
- Uma biblioteca utiliza os atributos título, edição e editora para distinguir duas publicaç
- Ao preparar uma ficha bibliográfica, a equipe usa a ISBD como modelo de campos MARC numera
- Um volume particular da edição de 2024 de um romance, identificado por etiqueta de patrimô
- Na catalogação de um livro, o MARC 21 utiliza preferencialmente o campo 650 para registrar
- Uma biblioteca de acervo geral pode usar a classe 900 da CDD para reunir obras de história
- Um tesauro informa `Termo A TR Termo B`; isso indica necessariamente que o Termo B é um te
- Ao combinar os descritores independentes `saúde` e `adolescência` na pesquisa, uma usuária
- Ao aceitar dados de catalogação na fonte de um livro, a biblioteca deve presumir que a des
- Ao consultar um registro bibliográfico MARC 21, a catalogadora pode encontrar informação d
- Para reproduzir uma relação simples de assuntos na CDU, o catalogador deve usar `+`, porqu
- No FRBR, uma tradução pode ser tratada como expressão e sua publicação numa edição específ
- Uma política de indexação que aumenta a especificidade necessariamente aumenta também o nú
- Num registro MARC, os campos 336, 337 e 338 podem distinguir texto, mídia não mediada e vo
- Ao importar um registro MARC, um analista interpreta o campo 008 como campo de dados com d
- Em um catálogo cooperativo que utiliza RDA, registros podem continuar codificados em MARC 
- Uma notação da CDU usa o sinal `::` para expressar simples adição de assuntos intercambiáv
- Em um tesauro institucional, o descritor `conservação` tem remissiva de uma forma não pref
- Em um acervo com 80 documentos relevantes, uma pesquisa recupera 20 resultados, dos quais 
- Uma instituição pode adotar uma mesma forma autorizada de nome para reunir registros de ed
- Uma biblioteca que troca CDD por CDU pode reutilizar a classe principal 400 para linguagem
```

## Pedido 5: Fabricação mecânica: metrologia, torno, ferramentaria e soldagem (`engenharia-mecanica.fabricacao-metrologia-soldagem`, 245 tópicos de edital)

```
Matéria: engenharia-mecanica.fabricacao-metrologia-soldagem (Fabricação mecânica: metrologia, torno, ferramentaria e soldagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Metrologia e ajustagem": 24
- "2. Torno mecânico e usinagem": 27
- "3. Ferramentaria e elementos de fixação": 26
- "4. Processos de soldagem": 23

Já existem (não repita):
- Um paquímetro com escala principal em milímetros e nônio de 20 divisões tem resolução de:
- O paquímetro permite medir dimensões externas, internas, de profundidade e de ressalto.
- Em um micrômetro métrico cujo fuso tem passo de 0,5 mm e cujo tambor tem 50 divisões, a re
- A catraca do micrômetro tem a função de garantir pressão de medição constante, evitando er
- O instrumento mais indicado para verificar o alinhamento entre os eixos de um motor e de u
- O calibrador do tipo passa/não passa informa o valor exato da dimensão medida, dispensando
- A folga entre as pontas de um anel de segmento, colocado dentro da camisa, é medida com:
- Para detectar ovalização e conicidade de uma camisa de cilindro, deve-se medir seu diâmetr
- No aperto dos parafusos de um cabeçote de motor com torquímetro, recomenda-se:
- O micrômetro deve ser guardado com as faces de medição encostadas e apertadas, para proteg
- Para fixar no torno uma peça de forma irregular ou excêntrica, a placa mais indicada é a:
- Ferramentas de metal duro permitem velocidades de corte bem maiores que as de aço rápido, 
- Na geometria da ferramenta de corte, a soma dos ângulos de folga, de cunha e de saída é ig
- Para tornear um eixo de 100 mm de diâmetro com velocidade de corte de 31,4 m/min, a rotaçã
- Mantida a velocidade de corte, quanto maior o diâmetro da peça no torno, menor deve ser a 
- A operação de torno que produz superfície plana perpendicular ao eixo da peça é o:
- Os fluidos de corte têm como funções refrigerar, lubrificar, ajudar a remover o cavaco e p
- Para afiar uma ferramenta de metal duro no esmeril, usa-se rebolo de:
- Na regulagem do torno, a ponta da ferramenta deve ficar na altura do centro da peça.
- Os cavacos acumulados no torno devem ser removidos com a mão protegida por luva, com a máq
- A chave que envolve completamente a porca, reduzindo o risco de espanar o sextavado, é a c
- Na serra de arco, a lâmina deve ser montada com os dentes voltados para a frente, pois o c
- A ferramenta usada para abrir um assento cônico, destinado à cabeça de um parafuso escarea
- As limas classificam-se pelo formato, pelo tipo de picado e pela aspereza, sendo a murça i
- Para abrir rosca interna M10 × 1,5 com macho, o diâmetro aproximado da broca para o furo é
- Os machos são usados para abrir roscas externas em parafusos, e os cossinetes para abrir r
- A rosca Whitworth, chamada de rosca inglesa, tem perfil triangular com ângulo de:
- O avanço de uma rosca de duas entradas com passo de 2 mm é de 4 mm por volta.
- O elemento que une eixo e cubo de uma polia para transmitir torque é a:
- Na junta soldada, a região do metal de base que não se fundiu, mas teve sua estrutura alte
- O pré-aquecimento de aços de maior teor de carbono antes da soldagem reduz a velocidade de
- Na designação de posições de soldagem, a posição sobrecabeça corresponde ao número:
- No eletrodo revestido E7018, o algarismo 8 indica revestimento básico de baixo hidrogênio,
- O processo de soldagem que usa eletrodo de tungstênio não consumível e proteção por gás in
- A diferença entre os processos MIG e MAG está no gás de proteção: o MIG usa gás inerte, e 
- Na soldagem oxiacetilênica de aço carbono, a chama mais utilizada é a:
- O oxicorte é adequado para cortar chapas de aço inoxidável e de alumínio com a mesma facil
- É prática correta de segurança no uso de equipamento oxiacetilênico:
- Na soldagem com eletrodo revestido, corrente excessiva tende a provocar mordeduras e respi
- A bordo, a soldagem em local fora da oficina exige, entre outras medidas:
```

## Pedido 6: Propriedade industrial e direitos autorais (Leis 9.279/1996, 9.610/1998 e 9.609/1998) (`direito-empresarial.propriedade-industrial-direitos-autorais`, 244 tópicos de edital)

```
Matéria: direito-empresarial.propriedade-industrial-direitos-autorais (Propriedade industrial e direitos autorais (Leis 9.279/1996, 9.610/1998 e 9.609/1998))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Patentes de invenção e de modelo de utilidade": 34
- "2. Desenho industrial e marcas": 33
- "3. Direitos autorais e programa de computador": 33

Já existem (não repita):
- Segundo a Lei 9.279/1996, é patenteável a invenção que atenda aos requisitos de novidade, 
- O objeto de uso prático, suscetível de aplicação industrial, que apresente nova forma ou d
- Pela Lei 9.279/1996, a patente de invenção vigora pelo prazo de 20 anos e a de modelo de u
- Segundo o art. 10 da Lei 9.279/1996, NÃO se considera invenção nem modelo de utilidade:
- Não será considerada estado da técnica a divulgação da invenção feita pelo próprio invento
- Quanto aos seres vivos, a Lei 9.279/1996 estabelece que:
- Descobertas, teorias científicas e métodos matemáticos podem ser patenteados como invenção
- O titular de patente que exerce os direitos dela decorrentes de forma abusiva, ou que por 
- A autarquia federal responsável pela concessão de patentes e pelos registros de marcas e d
- Extinta a patente pelo decurso do prazo de vigência, seu objeto cai em domínio público, po
- A forma plástica ornamental de um objeto, que proporcione resultado visual novo e original
- O registro de desenho industrial vigora pelo prazo de:
- São suscetíveis de registro como marca os sinais distintivos visualmente perceptíveis, não
- A marca usada para atestar a conformidade de um produto ou serviço com determinadas normas
- À marca registrada no Brasil considerada de alto renome é assegurada proteção especial em 
- A marca notoriamente conhecida em seu ramo de atividade, nos termos da Convenção da União 
- O registro de marca vigora pelo prazo de 10 anos, contados da data do depósito do pedido, 
- Caducará o registro da marca, a requerimento de quem tenha legítimo interesse, se, decorri
- Não são registráveis como marca, entre outros, os sinais de caráter genérico, necessário, 
- Constitui causa de extinção do registro de marca, segundo o art. 142 da Lei 9.279/1996:
- Pelo princípio da especialidade, em regra, a proteção conferida pelo registro da marca res
- A Lei 9.279/1996 tipifica como crime de concorrência desleal a conduta de quem:
- A proteção aos direitos autorais previstos na Lei 9.610/1998 independe de registro.
- Segundo o art. 8º da Lei 9.610/1998, NÃO é objeto de proteção como direito autoral:
- Os textos de leis, decretos e decisões judiciais são protegidos pelo direito autoral em fa
- Para a Lei 9.610/1998, autor é:
- Os direitos morais do autor são inalienáveis e irrenunciáveis.
- Os direitos patrimoniais do autor de uma obra literária perduram por:
- O prazo de proteção aos direitos patrimoniais sobre obras audiovisuais e fotográficas é de
- Além das obras cujo prazo de proteção expirou, pertencem ao domínio público:
- A citação de passagens de qualquer obra, em livros, jornais ou revistas, para fins de estu
- A utilização de obra intelectual por qualquer modalidade, como a reprodução integral para 
- A tutela dos direitos relativos ao programa de computador, pela Lei 9.609/1998, é assegura
- A proteção do programa de computador depende de registro obrigatório no órgão designado pe
- Uma empresa criou um aplicativo e um novo formato ornamental para a embalagem do produto q
- Assegurar a integridade da obra, opondo-se a modificações que possam prejudicá-la ou ating
- O desenho industrial protege aspectos funcionais ou técnicos do objeto, como a melhoria na
- A técnica operatória ou cirúrgica e os métodos terapêuticos ou de diagnóstico, para aplica
- A marca de alto renome e a marca notoriamente conhecida recebem proteção idêntica: ambas e
- A caducidade do registro de marca pode ser parcial, quando o uso comprovado não abranger t
```

## Pedido 7: Corpo humano e saúde (`biologia.corpo-humano-saude`, 243 tópicos de edital)

```
Matéria: biologia.corpo-humano-saude (Corpo humano e saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Nutrição, trocas gasosas, circulação e excreção": 23
- "2. Sistema nervoso e sistema endócrino: controle e integração": 19
- "3. Sistema locomotor e tegumentar: sustentação, movimento e proteção": 21
- "4. Reprodução humana, ciclo menstrual e comparação dos vertebrados": 18
- "5. Linfa, imunidade, doenças transmissíveis e integração fisiológica": 19

Já existem (não repita):
- Após a ingestão de uma refeição rica em lipídios, ocorre liberação de bile no duodeno. Qua
- Durante a inspiração tranquila, o diafragma se contrai e o volume da caixa torácica se mod
- Um esquema mostra sangue saindo do ventrículo direito em direção aos pulmões, por vasos de
- Após a filtração glomerular, água e solutos úteis podem retornar ao sangue em segmentos do
- Um corte histológico da pele revela epiderme superficial e derme logo abaixo. Qual localiz
- Num movimento de flexão do cotovelo, a contração do bíceps transmite força ao osso por uma
- Ao tocar uma superfície quente, uma pessoa retira a mão antes de formular uma decisão cons
- Após refeição rica em carboidratos, a concentração de glicose sanguínea aumenta. Qual resp
- Em um ciclo ovulatório, um aumento acentuado de LH precede a liberação do oócito. Que even
- Em comparação de vertebrados, um animal aquático apresenta brânquias, circulação simples e
- Em ambiente quente, aumenta a sudorese, e o suor alcança a superfície corporal. Qual proce
- Uma lâmina histológica apresenta células musculares estriadas ramificadas, conectadas por 
- Num neurônio, um potencial de ação alcança a terminação do axônio e promove a liberação de
- Um estudo do eixo hipotálamo–hipófise acompanha ADH e ocitocina. Qual associação entre sín
- Depois da ovulação, uma estrutura ovariana passa a secretar progesterona em quantidade ele
- Ao comparar a ventilação de aves e mamíferos, qual explicação atribui corretamente aos sac
- Em uma fibra muscular esquelética estimulada, a elevação de Ca²⁺ no sarcoplasma permite a 
- Uma amostra da camada mais superficial da pele não apresenta vasos sanguíneos, mas contém 
- Uma pessoa assustada apresenta taquicardia e redução temporária da motilidade digestiva. Q
- Em situação hipotética de queda primária de produção de T3 e T4 pela tireoide, com hipotál
- Em anatomia comparada, um vertebrado adulto apresenta dois átrios, um ventrículo e partici
- Após ovulação, um ciclo não resulta em fecundação nem implantação. Em condições fisiológic
- Após vacinação, são geradas populações de linfócitos capazes de responder mais rapidamente
- Num estudo sobre verminoses, a equipe diferencia ascaridíase e esquistossomose para recome
- Ao comparar padrões de excreção nitrogenada em vertebrados, a conservação de água ganha re
- Uma amostra da epiderme humana não possui vasos sanguíneos e recebe nutrientes por difusão
- Na remodelação óssea, osteoblastos reabsorvem a matriz mineralizada e osteoclastos deposit
- No sistema nervoso, os axônios mielinizados podem conduzir potenciais de ação de maneira s
- Em uma pessoa saudável, a secreção de glucagon tende a aumentar logo após refeição com ele
- Na fisiologia menstrual, o pico pré-ovulatório de LH está associado à ruptura folicular e 
- Em aves, os sacos aéreos constituem o local predominante de hematose, enquanto os pulmões 
- No ciclo urbano mais frequente de dengue, o vírus é transmitido por fêmeas infectadas de A
- A hipoderme rica em tecido adiposo integra a epiderme e representa seu estrato mais profun
- No músculo esquelético, a aproximação relativa dos filamentos de actina e miosina encurta 
- Um reflexo medular rápido deve iniciar obrigatoriamente com decisão voluntária do córtex c
- Em um eixo endócrino com tireoide funcional, concentrações elevadas de T3 e T4 tendem a re
- No ciclo menstrual ovulatório, FSH e LH são produzidos nos ovários e liberados na circulaç
- Crocodilianos constituem exceção ao padrão cardíaco de três câmaras típico de diversos rép
- No sistema linfático, o baço filtra principalmente a linfa que retorna dos tecidos, enquan
- Durante a contração esquelética, ATP participa tanto do ciclo de pontes cruzadas actina–mi
- Toda sinalização sináptica química entre neurônios é excitatória, porque a liberação de ne
- No mecanismo normal de conservação de água, o ADH aumenta a permeabilidade hídrica de segm
- Na anatomia comparada, anfíbios adultos e mamíferos possuem o mesmo padrão de circulação d
- Ao manter o joelho parcialmente flexionado, músculos antagonistas podem apresentar ativida
- Uma resposta motora de origem somática é conduzida por neurônios eferentes que inervam pri
- O tecido caracterizado por células justapostas, pouca substância intercelular e ausência d
- O músculo estriado cardíaco é de contração involuntária e apresenta discos intercalares en
- No tecido nervoso, as células da glia são as responsáveis exclusivas pela condução do impu
```

## Pedido 8: Direito Internacional Público: fundamentos (`direito-internacional.publico-fundamentos`, 241 tópicos de edital)

```
Matéria: direito-internacional.publico-fundamentos (Direito Internacional Público: fundamentos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação, soberania e personalidade internacional": 18
- "2. Fontes, costume, normas imperativas e obrigações comunitárias": 19
- "3. Tratados, Convenção de Viena e incorporação brasileira": 30
- "4. Imunidades, responsabilidade, nacionalidade e mobilidade": 16
- "5. Organizações internacionais, solução de controvérsias e sucessão": 17

Já existem (não repita):
- Um novo ente político possui população permanente, território determinado, governo estável
- Uma pesquisadora sustenta que a obrigação convencional só se torna exigível na ordem inter
- Dois Estados apresentam à Corte Internacional de Justiça alegações baseadas em tratado, co
- Governos repetem há décadas determinada cortesia nas recepções oficiais, esclarecendo, por
- Um tratado bilateral contém cláusula incompatível, desde sua celebração, com norma imperat
- Um funcionário consular pratica ato particular sem vínculo com funções consulares e invoca
- Pessoa brasileira adquire voluntariamente uma segunda nacionalidade em 2025 e não apresent
- Visitante estrangeiro permanece no Brasil sem regularização migratória e recebe notificaçã
- Dois Estados discordam sobre uso compartilhado de um rio e decidem buscar terceiro que fac
- Após aprovação congressual de tratado sujeito a ratificação, o Presidente deposita instrum
- Estado pretende formular reserva a tratado multilateral que expressamente proíbe reservas 
- Tratado em vigor entre dois Estados não foi registrado no Secretariado da ONU. Um dos Esta
- Após a independência de um território, diversos Estados reconhecem seu novo governo, mas e
- Dois Estados disputam soberania sobre pequena ilha. Um invoca apenas documento antigo de d
- Um parecer cita o caso Lotus como prova de que a liberdade estatal internacional seria ili
- Estado A auxilia financeiramente e militarmente grupos armados para derrubar governo de B,
- Autoridade belga expede mandado de prisão contra ministro das Relações Exteriores congolês
- Estudante afirma que Barcelona Traction reconheceu a proteção diplomática belga aos acioni
- Tratado bilateral não contém regra sobre retirada. Um Estado afirma que pode denunciá-lo i
- Durante negociação, representante de Estado sofre coação por ameaça dirigida pessoalmente 
- Após separação territorial, um parecer equipara a Convenção de Viena de 1978 sobre tratado
- Órgão estatal pratica conduta atribuível ao Estado que viola obrigação internacional, mas 
- Um Estado sustenta que resolução recomendatória da Assembleia Geral o vincula da mesma for
- Pessoa investida como funcionária consular celebrou contrato inteiramente privado em seu p
- Um nacional tenta apresentar pedido individual diretamente à CIJ contenciosa contra seu Es
- Uma unidade federativa brasileira anuncia que celebrará, em nome próprio, tratado internac
- Autoridades de dois países afirmam que uma prática de cortesia internacional se tornou cos
- Um Estado assina tratado sujeito a ratificação e, antes de decidir se se tornará parte, ad
- Uma pessoa brasileira naturalizada adquire nacionalidade estrangeira em 2025 e, por isso, 
- Dois Estados optam pela arbitragem de controvérsia de fronteira, em vez de negociação dire
- Um diplomata invoca o art. 31 da Convenção de 1961 para sustentar imunidade civil absoluta
- Em litígio internacional, dois Estados autorizam expressamente a Corte Internacional de Ju
- Como o caso Barcelona Traction empregou a expressão obrigações erga omnes, conclui-se que 
- Um Estado não diretamente lesado pode, nos pressupostos do art. 48 dos artigos da CDI de 2
- Tratado em vigor impõe dever ambiental a um Estado, mas lei posterior nacional autoriza su
- Migrante sofre condenação penal definitiva por crime comum doloso passível de pena privati
- Em país receptor, um cônsul presta assistência a nacional preso. A Convenção de Viena sobr
- Em parecer elaborado em outubro de 2026, registra-se que a Convenção de Viena de 1978 sobr
- Tratado não prevê denúncia ou retirada. O Estado interessado envia aviso com 12 meses de a
- Tratado era válido quando celebrado, mas posteriormente surgiu norma imperativa de Direito
- Se tribunal de um Estado admitir conhecer ação civil relativa a atividade comercial de out
- Um brasileiro nato adquire outra nacionalidade sem pedir perda da brasileira e depois é so
- Agente público pratica ato atribuível ao Estado que viola proibição internacional e causa 
- Dois Estados aceitam jurisdição contenciosa da Corte Internacional de Justiça e um deles d
- Com a dissolução de uma Federação e o surgimento de dois Estados, a Convenção de 1983 sobr
```

## Pedido 9: Introdução ao direito penal, política criminal e escolas penais (`direito-penal.introducao-politica-criminal-escolas`, 241 tópicos de edital)

```
Matéria: direito-penal.introducao-politica-criminal-escolas (Introdução ao direito penal, política criminal e escolas penais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, caracteres e funções do direito penal": 27
- "2. Bem jurídico e princípios limitadores": 16
- "3. Política criminal, prevenção e populismo penal": 23
- "4. Evolução histórica e escolas penais": 18
- "5. Criminologia crítica, cultura e limites da resposta penal": 16

Já existem (não repita):
- Uma pessoa afirma que, por ter sido vítima de um delito, passou a deter o direito de impor
- Após reiteradas infrações administrativas, foi proposta uma norma infralegal que criaria c
- Um estudo sobre a subtração de aparelho telefônico diferencia o valor tutelado e a coisa c
- Em debate legislativo, sustenta-se que comportamentos moralmente reprováveis podem receber
- Ao justificar a escolha da quantidade de pena dentro dos limites aplicáveis, um julgador m
- Uma lei posterior reduziu a pena prevista para conduta praticada anteriormente. O estudo d
- Uma pesquisadora descreve corrente histórica que enfatizava o crime como entidade jurídica
- Um texto do século XIX associa o estudo do delito às características do autor, à observaçã
- Foi proposta lei que introduz um tipo penal inexistente, enquanto outra discussão tratou d
- Uma pessoa pertencente a comunidade com normas próprias alega que não compreendeu a ilicit
- Um relatório concluiu que houve aumento da criminalidade apenas porque cresceu o número de
- Depois da condenação, familiares foram informados de que herdariam a pena criminal pelo fa
- Uma pessoa subtraiu objeto de pequeno valor e a defesa invocou insignificância. Qual funda
- Ao comparar três autores do positivismo criminal, um estudante precisa distinguir o enfoqu
- Diante de infração que causa dano relevante, duas correntes discutem reduzir a atuação pun
- Em julgamento de conduta vinculada a uma tradição, discutiram-se simultaneamente liberdade
- Uma política anuncia que a simples previsão de pena deve dissuadir a população; outra inve
- Ao redigir uma lei incriminadora, foram usados termos tão indeterminados que o destinatári
- Uma empresa praticou conduta lesiva ao meio ambiente e a discussão questiona se é possível
- Uma lei posterior deixou de considerar criminosa uma conduta anteriormente punida, mas foi
- Para responder a um problema social, foram cogitadas duas vias: uma lei estadual instituin
- Uma proposta aumenta penas em resposta a caso de grande repercussão, mas não apresenta dad
- Em seminário histórico, um expositor descreve o criminoso nato como verdade comprovada e u
- Uma pessoa conhece os fatos que realiza, mas afirma acreditar, por circunstâncias culturai
- Em processo relativo a fato também gerador de danos civis, discutiu-se se parentes de pess
- Uma vítima pede que a pena seja aplicada por iniciativa pessoal, mas a descrição do delito
- Um regulamento administrativo pode criminalizar uma infração já sujeita a multa quando o c
- Em discussão sobre tutelar penalmente todas as faltas éticas de uma atividade, o princípio
- A alegação de que uma conduta lesiva é aceita pela tradição do grupo dispensa o exame do t
- Ao analisar a crítica iluminista à arbitrariedade das penas, um texto identifica Cesare Be
- A aprovação de um novo tipo penal é criminalização secundária, porque exige posterior atua
- Diante de fato patrimonial de pequeno impacto, eventual reconhecimento da insignificância 
- Ao classificar a Escola Positiva italiana, uma pesquisa atribui a Enrico Ferri a rejeição 
- Para uma atividade que cause lesão relevante ao meio ambiente, a Constituição prevê a poss
- A elevação de prisões após uma alteração legislativa comprova, isoladamente, aumento da cr
- Para justificar novo delito, uma proposta invoca urgência e sugere que a descrição da cond
- Se um fato preenche literalmente a descrição legal e envolve objeto barato, sua tipicidade
- Ao comparar uma campanha que promete intimidar potenciais infratores com outra que busca r
- Com a morte da pessoa condenada, o princípio da pessoalidade autoriza exigir dos herdeiros
- Na apuração de conduta culturalmente motivada, o pertencimento comunitário pode ser elemen
- A orientação de Franz von Liszt e as propostas associadas à nova defesa social constituem 
- Em infração ambiental, a possibilidade de responsabilidade penal da pessoa jurídica não de
- Quando o erro sobre a ilicitude do fato for evitável, a lei determina redução obrigatória 
- Uma pesquisa que investiga por que certos comportamentos são classificados como crimes e p
- A necessidade de resposta imediata a um fato de repercussão autoriza criar, por medida pro
```

## Pedido 10: Indústria, energia, agropecuária e globalização (`geografia.industria-energia-agropecuaria-globalizacao`, 239 tópicos de edital)

```
Matéria: geografia.industria-energia-agropecuaria-globalizacao (Indústria, energia, agropecuária e globalização)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Industrialização, localização e reorganização produtiva": 18
- "2. Energia, matrizes e impactos hidrelétricos": 16
- "3. Geografia agrária, estruturas fundiárias e sistemas produtivos": 23
- "4. Agronegócio, cadeias globais, comércio e logística": 21
- "5. Globalização, divisão internacional do trabalho e fragmentação": 22

Já existem (não repita):
- Uma indústria transforma minério volumoso em peças de menor massa. O custo dominante é tra
- Um polo reúne firmas de componentes, oficinas de manutenção e técnicos especializados que 
- Uma empresa recebe pequenas remessas de componentes poucas horas antes de utilizá-los, apo
- Ao comparar fontes utilizadas no transporte, na indústria e na geração de eletricidade, um
- Um relatório distingue duas grandes usinas hidrelétricas paraenses, uma no Tocantins e out
- Uma tabela mostra que poucos estabelecimentos rurais reúnem grande parte da área agropecuá
- Um estabelecimento é administrado pela família, emprega principalmente seus membros e vend
- Um complexo da soja abrange produção de sementes, financiamento, lavoura, armazéns, esmaga
- Uma empresa pesquisa equipamentos no país X, produz peças no Y, monta no Z e distribui glo
- Países eliminam grande parte das tarifas internas e estabelecem uma tarifa externa comum, 
- Uma planta deixa a capital, mas passa a produzir perto de um entroncamento ferroviário e d
- Uma companhia anuncia capacidade de 10 mil MW numa hidrelétrica, mas a geração varia muito
- Uma região adota novas sementes, máquinas, irrigação e adubação. A produção por hectare cr
- Uma rota de grãos passa a usar ferrovia ligada a um terminal hidroviário, reduzindo perdas
- Uma trading antecipa recursos para uma safra, contrata silos e vende parte da commodity a 
- Dois territórios concorrem por uma indústria: o primeiro oferece salário menor e transport
- Uma hidrelétrica na Amazônia tem reservatório relativamente limitado, mas altera trechos d
- Uma pesquisa indica que a agricultura familiar ocupa 23% da área e responde por 67% das pe
- Um contrato futuro de soja é usado por produtor para reduzir incerteza sobre o preço de ve
- A produção agrícola de um país recebe apoio à pesquisa pública e, em outro programa, pagam
- Uma economia exporta grande volume de minério, mas suas empresas dominam determinados soft
- Uma multinacional mantém montagem no exterior, mas passa a comprar peças de duas regiões d
- Num mapa, corredores ferroviários aproximam o interior produtor de terminais portuários, m
- Um pesquisador compara a energia total consumida no país com a eletricidade ofertada e ide
- Uma aliança comercial remove tarifas entre membros, mas não estabelece tarifa externa comu
- Uma fábrica organiza tarefas em sequência de montagem, com modelos padronizados e produção
- Um relatório classifica uma usina hidráulica como fonte renovável; disso se conclui que se
- No Censo Agropecuário de 2017, uma pequena parcela de estabelecimentos com 1.000 hectares 
- Uma montadora reduz estoques por meio de entregas programadas e, por isso, torna-se menos 
- A cadeia de uma commodity agrícola pode envolver agricultores, fornecedores de máquinas, s
- A ampliação de linhas elétricas entre a Região Norte e o Sudeste significa que toda a ener
- Uma empresa projeta um produto em um país, importa componentes de outro e monta em um terc
- Um conjunto de países estabelece livre comércio interno, mas conserva tarifas externas nac
- Um país amplia geração solar e eólica e investe em armazenamento e redes. A combinação res
- Os dados de área dos estabelecimentos rurais permitem inferir diretamente a participação d
- Uma usina a fio d'água pode alterar a vazão de um trecho fluvial mesmo sem manter um reser
- Se uma região passa a escoar grãos por ferrovia e hidrovia, reduzindo o custo por tonelada
- Uma propriedade familiar pode participar de mercados nacionais, obter crédito, utilizar tr
- Ao analisar a produção mundial de grãos, um pesquisador afirma que os maiores produtores s
- Uma multinacional desloca parte da cadeia para mercados próximos por receio de interrupçõe
- Em um estudo de localizações, o custo de trabalho é menor na cidade A e o transporte é mai
- Se o dado de renovabilidade da matriz elétrica supera o da energética no mesmo período, um
- Uma política nacional apoia pesquisa agropecuária geral e outra garante artificialmente pr
- A inserção de um país na DIT pode ser heterogênea: um setor exporta produtos primários e o
- Uma política de redução de estoques em uma cadeia industrial torna o sistema mais robusto 
```

## Pedido 11: Declaração Universal dos Direitos Humanos (`direito.declaracao-universal-direitos-humanos`, 238 tópicos de edital)

```
Matéria: direito.declaracao-universal-direitos-humanos (Declaração Universal dos Direitos Humanos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, preâmbulo e universalidade": 20
- "2. Direitos de integridade e garantias jurídicas (arts. 3º a 12)": 20
- "3. Circulação, participação e liberdades públicas (arts. 13 a 21)": 20
- "4. Direitos econômicos, sociais e culturais (arts. 22 a 27)": 20
- "5. Ordem internacional, deveres e interpretação (arts. 28 a 30)": 20

Já existem (não repita):
- Uma pessoa sustenta que a DUDH só produziria qualquer orientação após ser ratificada como 
- Um programa afirma que pessoas nascidas em território não independente não podem invocar o
- Em uma região, pessoas são submetidas a servidão sob o argumento de que o trabalho gera be
- Uma pessoa é impedida de ajuizar medida efetiva contra violação de direito fundamental rec
- Uma pessoa deseja abandonar o país onde se encontra e, posteriormente, voltar ao país de s
- Um regulamento exige filiação compulsória de todos os moradores a uma associação local. Qu
- Uma pessoa pede o reconhecimento da igualdade de remuneração em dois postos de trabalho eq
- Uma criança tem o ingresso recusado no ensino elementar porque sua família não pode custea
- Um autor de produção científica procura proteção jurídica tanto ao reconhecimento de sua a
- Uma proposta pretende interpretar a DUDH como autorização para extinguir os direitos de de
- Uma autoridade afirma que a proibição de discriminação do art. 2º só contempla os motivos 
- Em uma acusação criminal, o julgador declara culpada a pessoa antes da produção de prova, 
- Em 2026, pretende-se aplicar pena superior à prevista quando um fato foi praticado em 2024
- Uma pessoa perseguida procura asilo e outra o invoca para escapar de processo genuíno por 
- Uma pessoa muda de convicção religiosa e passa a compartilhar publicamente sua nova crença
- Uma administração deseja organizar eleições públicas, periódicas, genuínas e livres; há di
- Na elaboração de políticas sociais, afirma-se que o art. 22 impõe nível uniforme de presta
- Um material afirma que a educação superior deve ser igualmente acessível conforme o mérito
- Uma regra local limita expressão de opiniões por mero ato administrativo sem indicação de 
- Uma associação invoca a DUDH para afirmar que seu direito de reunião inclui poder retirar 
- Uma pessoa é acusada por fato que, na data da prática, não era crime no direito nacional, 
- Duas pessoas solicitam proteção em país estrangeiro: uma sofreu perseguição pessoal; a out
- Um texto afirma que a igualdade no casamento prevista na DUDH se esgota na celebração e qu
- Uma política de assistência condiciona a proteção social de uma criança à existência de ca
- Um parecer distingue: limites estabelecidos por lei para finalidades legítimas em sociedad
- Ao planejar uma atividade educativa sobre direitos humanos, um grupo informa que a DUDH fo
- Uma política que exclui moradores de território sob tutela afirma estar compatível com a D
- Uma pessoa acusada criminalmente tem, pela DUDH, direito à presunção de inocência até a co
- Em matéria de liberdade de locomoção, a DUDH assegura à pessoa o direito automático de ent
- Um projeto cultural permite que pessoas participem dos benefícios do progresso científico,
- Em um processo, a existência de recurso efetivo contra violação de direitos fundamentais e
- Uma pessoa é privada arbitrariamente de sua nacionalidade e impedida de mudá-la. O art. 15
- Ao estabelecer o direito ao repouso e às férias periódicas remuneradas, o art. 24 da DUDH 
- Na disciplina do art. 21, a participação na direção dos assuntos públicos pode ocorrer dir
- Segundo o art. 29 da DUDH, qualquer ato administrativo pode restringir liberdades sem fund
- Um enunciado afirma que os ideais de liberdade de palavra e de crença, além da libertação 
- Ao vedar condenação retroativa, o art. 11, item 2, da DUDH leva em conta apenas se a condu
- Uma pessoa submetida a ataques injustificados à reputação e a interferências arbitrárias n
- A proteção do art. 17 da DUDH alcança apenas propriedade individual, motivo pelo qual a pr
- A garantia do art. 23, item 4, abrange o direito de fundar sindicatos e de a eles aderir p
- Conforme o art. 26, o acesso ao ensino superior deve obedecer à condição de mérito, enquan
- A maternidade e a infância têm direito a cuidados e assistência especiais, e a DUDH garant
- A referência do art. 28 a uma ordem social e internacional capaz de realizar direitos equi
- O art. 29, item 3, determina que direitos e liberdades previstos na DUDH não sejam exercid
- Por mencionar deveres para com a comunidade, a DUDH autoriza interpretar o art. 30 como pe
```

## Pedido 12: Organizações internacionais, solução de controvérsias, mar, refugiados e TPI (`direito-internacional.organizacoes-mar-refugiados-tpi`, 238 tópicos de edital)

```
Matéria: direito-internacional.organizacoes-mar-refugiados-tpi (Organizações internacionais, solução de controvérsias, mar, refugiados e TPI)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organizações internacionais, ONU, direitos humanos e trabalho": 15
- "2. Solução pacífica de controvérsias, segurança coletiva e sanções": 16
- "3. Direito do mar e regime dos espaços internacionais": 17
- "4. Refugiados, conflitos armados e neutralidade": 14
- "5. Tribunal Penal Internacional e integração dos regimes": 38

Já existem (não repita):
- Um curso de formação em relações internacionais classifica entidades previstas na Carta de
- Dois Estados discutem uma fronteira e uma pessoa física deseja atuar como parte autora de 
- Um documento atribui ao Conselho de Direitos Humanos a função de realizar o Exame Periódic
- Após a adoção de uma recomendação internacional sobre condições de trabalho, um Estado é i
- Dois Estados querem evitar escalada de disputa territorial e escolhem uma terceira pessoa 
- Em resposta a uma ameaça à paz, o Conselho de Segurança impõe interrupção de relações econ
- Uma embarcação estrangeira pretende atravessar águas a dez milhas náuticas da linha de bas
- Uma pesquisa sobre a faixa marítima em que o Estado costeiro possui direitos próprios sobr
- Uma fiscalização costeira pretende controlar infrações aduaneiras em navio situado a vinte
- Um Estado pleiteia direitos sobre recursos minerais de leito oceânico além da marca de 200
- Um navio comercial registrado em um Estado transita pelo alto-mar fora de exceções especia
- Uma aeronave civil tem proprietários de vários países, mas matrícula regularmente registra
- Um Estado declara que a primeira missão de exploração permanente de determinado corpo cele
- Uma pessoa deixa seu país por grave e generalizada violação de direitos humanos, sem conse
- Uma pessoa que pediu refúgio enfrenta decisão de retorno imediato a fronteira na qual sua 
- Em conflito armado não internacional, um grupo captura pessoas que não participam das host
- Um Estado anuncia neutralidade em guerra entre dois outros Estados e propõe permitir que u
- Um navio é retido no âmbito de aplicação da Convenção do Direito do Mar e existe controvér
- Um Estado ingressa unilateralmente na CIJ contra outro sem cláusula compromissória, acordo
- O Conselho de Segurança determina ameaça à paz e considera primeiro restrições comerciais,
- A justiça de um Estado investiga genuinamente os mesmos fatos imputados a um suspeito de c
- Um relatório classifica uma campanha de violência indiscriminada como crime de competência
- Uma pessoa tinha dezessete anos quando teria participado de crime previsto no Estatuto de 
- Um chefe de Estado é acusado de crime internacional abrangido pelo Estatuto de Roma e a de
- Uma situação de possíveis crimes do Estatuto de Roma é enviada ao TPI por mecanismo previs
- Dois Estados discutem responsabilidade por poluição transfronteiriça e aceitam apresentar 
- Uma declaração sobre direitos humanos aprovada pela Assembleia Geral da ONU assume necessa
- Dois países escolhem uma pessoa imparcial para aproximar suas delegações e reabrir negocia
- Uma embarcação navega a dezoito milhas da linha de base. Como o mar territorial pode ating
- Uma pessoa foge de grave e generalizada violação de direitos humanos e procura proteção no
- Durante conflito armado não internacional, o art. 3º comum às Convenções de Genebra deixa 
- A pessoa suspeita de crime previsto no Estatuto de Roma não pode ser julgada pelo TPI caso
- Um Estado registra a mesma aeronave civil simultaneamente em dois países e sustenta que am
- Um Estado costeiro institui regras de conservação de cardumes em sua ZEE, mas admite passa
- Uma organização regional pode autorizar por decisão isolada suas forças a impor medidas co
- Após ratificar uma convenção da OIT, um Estado recebe pedidos de informações sobre sua apl
- Uma pessoa entra sem visto no Brasil e imediatamente solicita refúgio. A entrada irregular
- Durante planejamento de ataque a objetivo militar, são previstos danos incidentais a civis
- Um grupo privado obtém autorização para lançar estação de pesquisa na Lua. O Tratado do Es
- O ocupante de função de chefe de Estado pode responder perante o TPI por crime de competên
- Um Estado efetivamente investiga os mesmos fatos de competência do TPI, com independência 
- O Estado costeiro que possui plataforma continental juridicamente reconhecida além das duz
- Diante de ataque potencialmente ilícito contra civis, a avaliação humanitária das partes n
- Uma sociedade empresária é denunciada diretamente como ré criminal perante o Tribunal Pena
- Se uma pessoa é condenada pelo TPI, o Estatuto de Roma autoriza pena capital como alternat
```

## Pedido 13: Sistemas de informação (`informatica.sistemas-informacao`, 238 tópicos de edital)

```
Matéria: informatica.sistemas-informacao (Sistemas de informação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria Geral de Sistemas e transformação de dados em conhecimento": 22
- "2. Componentes, arquiteturas e tipos operacionais e gerenciais de SI": 24
- "3. Inteligência de negócios, armazéns analíticos e gestão de conteúdo": 17
- "4. Ciclo de vida, modelos de desenvolvimento e impacto organizacional": 19
- "5. Teoria da informação: fonte, canal, ruído, entropia, codificação e compressão": 18

Já existem (não repita):
- Uma unidade recebe solicitações, confere os registros, produz respostas e acompanha reclam
- Depois de entregar relatórios, uma equipe recolhe avaliações dos destinatários e altera o 
- Uma planilha contém o número 42; outra anotação informa que houve 42 atendimentos no turno
- Um sistema eletrônico tem computadores e programas, mas falha devido a cadastros incorreto
- Ao registrar cada venda e atualizar o estoque correspondente, um programa precisa manter e
- A coordenação recebe todo mês um relatório consolidado com volumes por setor, variações e 
- Uma instituição quer compartilhar cadastros de compras, estoque, finanças e recursos human
- Uma equipe acompanha histórico de contatos, solicitações, preferências e respostas enviada
- Após construir um novo sistema, a equipe planeja treinar usuários, migrar dados, disponibi
- Um painel de gestão permite alterar projeções de procura, número de profissionais e custo 
- Uma organização implanta um repositório integrado de registros vindos de vendas, estoque e
- Em um cubo de análise, a visualização passa de totais anuais para valores por trimestre e 
- Em um processo de integração, os dados são extraídos, carregados primeiro no ambiente anal
- Uma equipe digitaliza contratos e precisa localizar documentos por metadados, preservar ve
- Uma plataforma separa interface do usuário, regras de negócio e persistência de dados, emb
- Uma equipe utiliza aplicação pronta em nuvem pelo navegador e não administra o sistema ope
- Um sistema está funcionando, mas muda a regra externa que determina um campo obrigatório d
- Um sistema de análise recebe dados de vendas e permite consultar o total por ano, região e
- Um serviço de nuvem oferece ao cliente máquinas virtuais, discos e rede; o cliente instala
- Durante a evolução de uma aplicação, os usuários recebem primeiro o cadastro e depois o mó
- Um produto atende a todos os critérios descritos no documento de requisitos, mas não resol
- Duas fontes discretas produzem símbolos independentes em cada escolha: a primeira tem duas
- Uma transmissão usa código que acrescenta bits para detectar alterações causadas por ruído
- Uma instituição troca o aplicativo anterior por outro em um único fim de semana, enquanto 
- Um analista considera a entropia de Shannon de duas fontes binárias: uma produz 0 e 1 igua
- Em um sistema de atendimento, a fronteira delimita componentes e responsabilidades conside
- Em uma operação organizacional, o feedback corresponde à entrega do produto final e não en
- Um sistema de informação pode combinar computadores, programas, redes, dados, pessoas e pr
- Como um ERP integra compras e finanças, sua finalidade principal é o relacionamento com cl
- Um sistema destinado a registrar muitas vendas, pagamentos e movimentações de estoque é co
- Ao término dos testes de um sistema recém-criado, seu ciclo de vida fica completo; ativida
- Em arquitetura de três camadas, apresentação, lógica de negócio e dados podem estar separa
- Um serviço acessível pela internet é classificado como nuvem pública segundo o NIST mesmo 
- Quando o consumidor provisiona servidores virtuais, instala o sistema operacional e mantém
- Em BI, OLAP tem como finalidade predominante confirmar cada transação de venda enquanto OL
- Um data warehouse pode integrar registros de aplicações operacionais diferentes e permitir
- A transformação em ETL ocorre após os dados serem carregados no repositório de destino, ao
- Uma solução ECM pode controlar metadados, versões, acesso, fluxo de aprovação e retenção d
- No desenvolvimento em cascata, a equipe revisita sistematicamente as mesmas fases em ciclo
- Uma equipe testa a aplicação e confirma que ela cumpre os requisitos documentados, mas usu
- Ao implantar um sistema por piloto, os dois sistemas, antigo e novo, devem necessariamente
- Uma fonte que emite quatro símbolos equiprováveis possui entropia de dois bits por símbolo
- Uma fonte binária com probabilidades 90% e 10% tem mais entropia de Shannon por símbolo do
- Em uma transmissão com risco de ruído, bits redundantes de código de canal podem aumentar 
- A compressão com perdas permite reconstruir exatamente cada byte original, enquanto a comp
```

## Pedido 14: Farmacologia geral: farmacocinética e farmacodinâmica (`farmacia.farmacologia-geral`, 238 tópicos de edital)

```
Matéria: farmacia.farmacologia-geral (Farmacologia geral: farmacocinética e farmacodinâmica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, vias de administração e absorção": 20
- "2. Distribuição, ligação proteica e barreiras biológicas": 19
- "3. Metabolismo, excreção e parâmetros de eliminação": 19
- "4. Receptores, transdução de sinal e mecanismos farmacodinâmicos": 22
- "5. Curvas dose–resposta, farmacometria e segurança": 20

Já existem (não repita):
- Dois comprimidos de mesma dose apresentam perfis plasmáticos distintos após a administraçã
- Uma solução foi aplicada diretamente na corrente sanguínea por via intravenosa. Qual carac
- Um estudo acompanha a concentração plasmática de determinado fármaco durante várias horas.
- Após absorção intestinal satisfatória de um fármaco oral, apenas parte da dose alcança a c
- Uma formulação retardou a absorção de um medicamento, mas manteve exposição total semelhan
- Uma substância é um ácido fraco de pKa igual a 4. Ao atingir meio de pH 3, qual tendência 
- Uma substância que sofreria intensa primeira passagem hepática é administrada de modo a se
- Em estudo com doses iguais de um mesmo fármaco e farmacocinética linear, AUC intravenosa é
- Um composto atravessa a membrana por uma proteína transportadora, obedecendo ao gradiente 
- Após dose intravenosa de 600 mg, estimou-se a concentração plasmática inicial extrapolada 
- Um ligante apresenta ligação importante e reversível à albumina. Qual afirmação descreve m
- Uma estimativa didática de dose de ataque usa concentração-alvo de 6 mg/L, Vd de 40 L e F 
- Um novo composto lipofílico alcança o sistema nervoso central, mas sua penetração é parcia
- Uma substância é diretamente conjugada ao ácido glicurônico sem oxidação anterior. Como de
- Um pró-fármaco necessita de transformação enzimática para gerar um metabólito farmacologic
- Uma substância circula parcialmente ligada a proteínas, sofre filtração glomerular e també
- Uma infusão intravenosa produz AUC de 80 mg·h/L após dose única de 400 mg, em comportament
- Uma substância segue eliminação de primeira ordem, com Vd de 40 L e CL de 4 L/h. Qual meia
- Para fármaco com cinética linear e meia-vida de 8 horas, inicia-se infusão constante. Apro
- Uma via de eliminação torna-se saturada em concentrações elevadas. Nesse intervalo, observ
- O fármaco X alcança 80% de seu Emax em concentração menor que Y, mas Y produz efeito máxim
- Após introdução de antagonista que disputa reversivelmente o mesmo sítio, a curva de um ag
- Um agonista A apresenta Emax inferior ao do agonista pleno B no mesmo sistema. Ao ocupar o
- Em um estudo quantal, 50% dos animais apresentam efeito terapêutico a 12 mg e 50% apresent
- No modelo Emax com resposta basal nula e coeficiente de Hill 1, a concentração de um agoni
- Um fármaco administrado por injeção intravenosa chega imediatamente à circulação sistêmica
- Uma molécula administrada por via oral foi totalmente absorvida pelo intestino. Isso permi
- Ao comparar duas formulações, um pesquisador constatou que a segunda atingiu seu pico plas
- Em dois perfis concentração–tempo, o produto com maior Cmax deve necessariamente ter maior
- Após um bolus intravenoso, a relação entre quantidade do fármaco e sua concentração plasmá
- Um composto apresenta forte ligação à albumina plasmática. A fração ligada é filtrada no g
- Uma base fraca atravessa dois compartimentos separados por membrana. A acidificação de um 
- Em uma análise metabólica, observou-se conjugação direta de um princípio ativo com ácido g
- Uma substância sofre hidrólise por enzima plasmática e outra sofre redução por enzima inte
- Na eliminação renal, secreção tubular pode ser mediada por proteínas transportadoras e apr
- Um fármaco apresenta depuração de 4 L/h e volume aparente de distribuição de 20 L. Se o Vd
- Quando a eliminação se aproxima da ordem zero em um intervalo de saturação, a quantidade r
- Durante infusão constante de fármaco linear, dobrar a taxa de entrada, sem modificar Vd ne
- Um agonista parcial desloca o agonista pleno de receptores compartilhados. Mesmo gerando a
- Dois ligantes apresentam a mesma afinidade por determinado receptor. Por essa razão, é nec
- Num experimento com antagonista ortostérico competitivo reversível, a necessidade de conce
- Ao observar redução do Emax após bloqueio de receptores, um pesquisador pode concluir sem 
- Em experimento quantal, a dose associada à resposta previamente definida em metade da amos
- Em um estudo experimental, a DL50 indica a dose responsável por produzir metade do efeito 
- Em comparação farmacocinética linear de vias oral e intravenosa com doses distintas, a est
```

## Pedido 15: Cartografia e fusos horários (`geografia.cartografia-fusos`, 237 tópicos de edital)

```
Matéria: geografia.cartografia-fusos (Cartografia e fusos horários)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Linguagem cartográfica, orientação e escalas": 18
- "2. Coordenadas geográficas, datum, UTM e posicionamento por satélites": 19
- "3. Projeções, generalização, mapas temáticos e interpretação crítica": 24
- "4. Cartas, navegação, aerofotogrametria e produção cartográfica": 19
- "5. Fusos horários, hora legal do Brasil e problemas integrados": 20

Já existem (não repita):
- Em uma planta de acesso a uma escola, um percurso mede 4 cm na escala 1:50.000. Qual é a d
- Uma equipe compara duas representações da mesma cidade: uma em 1:10.000 e outra em 1:200.0
- Um ponto de coleta está registrado em 12° S e 44° O. Como interpretar, respectivamente, es
- Uma equipe pretende combinar coordenadas recentes de cadastro nacional com bases histórica
- Um mapa do mundo na projeção de Mercator mostra regiões polares visualmente muito ampliada
- Num mapa com norte verdadeiro voltado para cima, uma seta aponta exatamente para o leste. 
- Em carta que apresenta curvas de nível equidistantes verticalmente, determinada encosta te
- Um analista lê no relatório técnico que a projeção UTM divide a Terra em zonas meridianas.
- Durante encontro remoto, Brasília (UTC−3) marca 14h e uma equipe está em Rio Branco, no Ac
- Uma imagem obtida por satélite apresenta pixel de 5 m, enquanto outra, da mesma região, ap
- Uma avenida mede 1,2 km no terreno e deverá ser representada numa planta de escala 1:20.00
- Ao registrar uma posição no meridiano central de determinado fuso UTM, uma equipe consulta
- Uma equipe deseja comparar incidência de doença por 100 mil habitantes entre municípios de
- Um receptor de posição combina sinais de GPS e Galileo e envia coordenadas a um programa d
- Na carta topográfica, um ponto é informado apenas por E=610.000 m e N=7.400.000 m. O técni
- Uma equipe observa duas fotografias aéreas sobrepostas de área montanhosa, obtidas de posi
- Um mapa de rotas no hemisfério norte utiliza uma projeção azimutal equidistante centrada n
- Um aviso informa transmissão às 23h30 de sexta-feira em Brasília (UTC−3), e um participant
- Um mapa regional de escala 1:10.000 será publicado como mapa nacional de escala muito meno
- Um projeto regional em médias latitudes busca preservar ângulos locais e representa uma ár
- Um técnico recebe coordenadas E/N de uma faixa Gauss-Krüger e tenta inseri-las como UTM se
- Uma carta em papel, inicialmente na escala 1:100.000, é reproduzida com todas as dimensões
- Um voo hipotético sai de uma cidade UTC−3 na segunda-feira às 22h, dura 7 horas e chega a 
- Uma imagem orbital foi captada em quatro bandas e exibida com infravermelho próximo como v
- Uma equipe consulta a Lei nº 12.876/2013 para posicionar municípios do oeste do Amazonas e
- Num mapa de orientação convencional com norte para cima, o leste fica à direita e o oeste 
- Na escala numérica 1:100.000, cada centímetro da carta representa cem quilômetros no terre
- Uma coordenada de 25° S informa latitude ao sul do Equador, ao passo que 48° O informa lon
- O Brasil utiliza atualmente apenas um fuso horário de UTC−3, visto que o horário de Brasíl
- Em uma imagem de sensoriamento remoto, a resolução temporal está relacionada à frequência 
- Uma coordenada UTM de 500.000 metros leste identifica um ponto único da Terra independente
- O sistema UTM utiliza sessenta zonas de seis graus de longitude e não se confunde com a di
- Como o mapa em Gall-Peters conserva as áreas relativas, ele também preserva obrigatoriamen
- Uma reprodução ampliada de mapa mantém uma barra de escala gráfica ampliada na mesma propo
- Um mapa coroplético que compara municípios por números absolutos de ocorrências sempre per
- Na convenção UTM, o valor de Northing atribuído ao Equador no hemisfério sul é 10.000.000 
- O fuso UTM 23 está compreendido entre os meridianos 48° O e 42° O e tem meridiano central 
- Uma carta náutica pode apresentar orientação geográfica e referências magnéticas distintas
- Uma cidade hipotética em UTC+2 marca 01h de terça-feira. No mesmo instante, Brasília em UT
- Segundo a divisão restabelecida pela Lei nº 12.876/2013, o Acre e a parte do Amazonas loca
- Na comparação entre uma planta urbana digital e outra impressa, a primeira necessariamente
- Um satélite que observa a mesma área diariamente pode ter pixel de 30 m, enquanto outro qu
- Para ensinar projeções de mapa-múndi, basta escolher aquela que conserva simultaneamente á
- Na produção de mapa regional a partir de levantamento urbano detalhado, selecionar vias pr
- Como o Observatório Nacional registra atualmente quatro fusos brasileiros, a adoção nacion
```

## Pedido 16: Engenharia ambiental: qualidade da água, poluição e ecotoxicologia (`engenharia-ambiental.poluicao-qualidade-agua-ecotoxicologia`, 237 tópicos de edital)

```
Matéria: engenharia-ambiental.poluicao-qualidade-agua-ecotoxicologia (Engenharia ambiental: qualidade da água, poluição e ecotoxicologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Poluição, compartimentos ambientais e destino dos contaminantes": 20
- "2. Qualidade da água, enquadramento, autodepuração e eutrofização": 22
- "3. Tratamento de efluentes, solos contaminados e biorremediação": 22
- "4. Poluição do ar, emissões veiculares, mercúrio e ozônio estratosférico": 18
- "5. Ecotoxicologia, agrotóxicos, resíduos especiais e instrumentos de fiscalização": 18

Já existem (não repita):
- Durante uma obra, constatou-se alteração da paisagem sem evidência de dano, enquanto uma d
- Em uma bacia hidrográfica, um tubo de indústria e o escoamento de fertilizantes provenient
- Um laboratório informa valores de DBO5,20 e DQO de águas residuárias de uma fábrica. Qual 
- Uma equipe constatou qualidade inferior à classe definida para um trecho de rio e sugeriu 
- Uma estação precisa reduzir a parcela biodegradável da carga orgânica após remoção de arei
- Em reservatório de baixa renovação, o aporte de fósforo e nitrogênio foi seguido por inten
- Um efluente mantém DBO de 200 mg/L. Sua vazão aumenta de 100 para 200 m³/dia sem alteração
- Um estudo deve enquadrar águas de um aquífero e comparar seu uso pretendido com requisitos
- Uma unidade industrial lançará efluente tratado diretamente em um rio. Qual providência é 
- Após analisar um terreno industrial, o laudo informa concentrações acima de valor orientad
- Um plano de monitoramento municipal cita a Resolução CONAMA 491/2018 como única norma para
- Fabricante de veículos novos busca comprovar atendimento às exigências de emissão aplicáve
- Uma fiscalização identifica importação de mercúrio metálico e possível exposição de comuni
- Uma empresa substitui CFCs por HFCs em equipamentos de refrigeração. A revisão ambiental d
- Durante auditoria de estabelecimento potencialmente poluidor, são verificados cadastro, pa
- Um ensaio de curta duração informa a concentração de efluente que causou mortalidade em 50
- Após medições em cadeia aquática, peixes predadores apresentaram maior concentração de met
- Em sistema de tratamento com nitrificação seguida de desnitrificação, deseja-se explicar a
- Dois efluentes sem reação relevante se misturam: corrente I, 10 L/s a 100 mg/L; corrente I
- Uma estação de esgoto sanitário entrega efluente com DBO5,20 de 135 mg/L e remoção comprov
- Uma organização pretende importar mercúrio metálico para uso permitido e apresenta apenas 
- Área industrial apresenta solo com chumbo e hidrocarbonetos. Um relatório sugere que a bio
- Um bioinsumo destinado ao controle de pragas tem baixa toxicidade aguda humana, mas pode a
- Um relatório afirma que o ozônio encontrado junto à superfície urbana oferece a mesma prot
- Uma rede de oficinas precisa definir rotas de retorno para pneus, pilhas e óleo lubrifican
- Em uma bacia, o escoamento superficial que transporta nutrientes de várias lavouras pode s
- Ao analisar uma amostra de rio, a detecção de alta DBO5,20 indica diretamente que o oxigên
- Uma estação utiliza grades antes de tanques biológicos. O gradeamento é etapa preliminar q
- O Proconve, instituído pela Resolução CONAMA 18/1986, fixa as mesmas metas de enquadrament
- Ao avaliar presença de mercúrio em peixes, distinguir a espécie química do metal é relevan
- Uma empresa inscrita no CTF/APP fica automaticamente dispensada de apresentar o RAPP e de 
- Em investigação de aquífero, a advecção pode transportar a pluma no sentido predominante d
- Um corpo hídrico superficial enquadrado em determinada classe passa automaticamente a segu
- A redução da DBO de 300 para 60 mg/L ao longo de uma estação de tratamento corresponde a e
- O padrão intermediário PI-2 da Resolução CONAMA 506/2024 passou a valer apenas em 2033, ép
- A leitura de uma CL50 deve considerar organismo-teste e duração da exposição; o valor obti
- Em uma área contaminada por chumbo, a bioestimulação pode assegurar a destruição do elemen
- A presença de fósforo em reservatório sujeito a florações deve ser examinada junto com ren
- Em rio de baixa vazão, a comprovação de que um efluente industrial atende isoladamente ao 
- Uma concentração de mercúrio mais alta em um predador do que em suas presas é compatível c
- Na CONAMA 430/2011, o máximo de 120 mg/L de DBO para efluente de sistema de tratamento de 
- A redução de importação de HFCs vinculada à Emenda de Kigali e o controle de CFCs pelo Pro
- Se dois efluentes possuem a mesma concentração de poluente, a corrente de maior vazão nece
- Em acompanhamento de área contaminada, a redução do composto original após tratamento biol
- Na fiscalização de pilhas, pneus e óleo lubrificante usado, a entrega desses materiais a u
```

## Pedido 17: Segurança pública na Constituição (`seguranca-publica.seguranca-publica-constituicao`, 236 tópicos de edital)

```
Matéria: seguranca-publica.seguranca-publica-constituicao (Segurança pública na Constituição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da segurança pública e divisão constitucional dos órgãos": 19
- "2. Polícia Federal, polícia judiciária, patrulhamento federal e segurança viária": 23
- "3. Polícias militares, bombeiros e regime constitucional dos militares estaduais": 22
- "4. Guardas municipais, polícias penais e cooperação constitucional": 18
- "5. Forças Armadas, defesa das instituições e distinções constitucionais": 18

Já existem (não repita):
- Uma comunidade debate a responsabilidade pela preservação da ordem pública e pela proteção
- Ao conferir a lista de órgãos do art. 144, um estudante identifica a instituição incluída 
- Um órgão permanente da União executa patrulhamento ostensivo em uma rodovia federal. Qual 
- Uma instituição estadual realiza rondas visíveis preventivas para preservar a ordem em bai
- Após deslizamento de encosta, um corpo de bombeiros militar participa do socorro e das açõ
- Uma infração penal comum de competência estadual precisa ser apurada por órgão com funções
- Uma unidade prisional necessita de órgão constitucionalmente vinculado à segurança dos est
- Um município pretende instituir guarda para proteção dos seus bens, serviços e instalações
- Uma carreira municipal integra órgão executivo que realiza educação, engenharia e fiscaliz
- Durante operação, a Polícia Federal apura lesão a patrimônio de uma empresa pública federa
- Em investigação sobre contrabando, equipes policiais atuam ao lado de autoridades fazendár
- Em sinistro de trânsito ocorrido em rodovia federal, são necessárias fiscalização, levanta
- Um oficial de polícia militar estadual questiona quem confere a sua patente, diferenciando
- Uma lei federal estabelece normas gerais sobre efetivos, material bélico e mobilização das
- Após lei municipal, uma guarda passa a realizar patrulhamento comunitário ostensivo de seg
- Uma unidade federativa analisa a criação de Justiça Militar estadual e a distribuição do j
- Em uma crise institucional, autoridades invocam o art. 142 para afirmar que as Forças Arma
- Uma infração ocorre em vários estados, mas não há informação sobre necessidade de repressã
- Um edital reproduz a descrição de órgão federal permanente, mantido pela União, voltado ao
- Um militar estadual em serviço ativo analisa a possibilidade de filiação partidária e de p
- Ao revisar a redação do art. 144, § 6º, um estudante procura identificar as corporações su
- Após o Tema 656, uma lei municipal atribui à guarda policiamento preventivo comunitário e 
- Uma ação judicial questiona ato disciplinar de militar estadual e outro processo envolve c
- O Presidente considera instaurar medida constitucional excepcional diante de comoção nacio
- Para organizar PM e CBM, uma norma nacional de 2023 detalha efetivos, material de seguranç
- Durante revisão da Constituição, uma pessoa afirma que segurança pública constitui dever e
- A Constituição coloca guardas municipais no inciso VI do art. 144, ao lado de polícias pen
- Uma patrulha da Polícia Rodoviária Federal executa policiamento ostensivo de rodovia feder
- A polícia civil tem atribuição constitucional de apurar infrações penais, inclusive milita
- Em atendimento a desastre natural, o corpo de bombeiros militar desempenha atividade relac
- O art. 144, § 8º, obriga cada município a instituir guarda municipal e colocá-la sob coman
- A competência da Polícia Federal para exercer funções de polícia marítima, aeroportuária e
- Como a Polícia Federal possui exclusividade de polícia judiciária da União, o art. 144 imp
- Uma lei nacional sobre organização e efetivos das polícias militares pode se fundamentar n
- A polícia penal federal subordina-se ao governador do Distrito Federal, porque o art. 144,
- O art. 125, § 4º, ressalva competência do júri quando a vítima for civil, e o § 5º prevê j
- Segundo o art. 144, § 10, segurança viária é exclusivamente patrulhamento policial de rodo
- A Lei 14.751/2023, editada em contexto de normas gerais do art. 22, XXI, contempla organiz
- Como o art. 144, § 3º, prevê Polícia Ferroviária Federal para ferrovias federais, a PRF re
- O Tema 656 admite atuação de guarda municipal em policiamento ostensivo e comunitário de s
- No art. 125, § 5º, os Conselhos de Justiça são os responsáveis singulares pelo julgamento 
- No estado de defesa, o Presidente decreta medida excepcional para áreas determinadas e sub
- Segundo a ADI 6.457, as Forças Armadas exercem poder moderador constitucional quando avali
- O art. 42 distingue militares estaduais dos integrantes das Forças Armadas, estende dispos
- A Lei 14.751/2023, por tratar de mobilização das PM e CBM, alterou o art. 144, § 6º, para 
```

## Pedido 18: Prótese dentária, oclusão e DTM (`odontologia.protese-oclusao-dtm`, 234 tópicos de edital)

```
Matéria: odontologia.protese-oclusao-dtm (Prótese dentária, oclusão e DTM)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Prótese fixa: planejamento, biomecânica e preparos": 17
- "2. Moldagem, fluxo digital, metalocerâmica e cimentação": 19
- "3. Prótese parcial removível: classes, componentes e mecânica": 19
- "4. Prótese total, relações intermaxilares, articuladores e oclusão": 28
- "5. DTM: diagnóstico clínico, condutas conservadoras e integração protética": 17

Já existem (não repita):
- Um molar apresenta cúspide vestibular fragilizada por extensa restauração anterior, mas ma
- Uma coroa provisória desprende-se quando recebe tração aproximadamente paralela à trajetór
- Após tratamento endodôntico, um pré-molar necessita de núcleo retido por pino e de coroa. 
- Em um arco com dentes anteriores e pré-molares, mas sem molares posteriores em ambos os la
- Um paciente conserva dentes posteriores e apresenta uma área desdentada anterior única, qu
- Na prova de armação de uma PPR, observa-se que o apoio oclusal não entra completamente em 
- Ao analisar uma PPR inferior, o clínico observa um elemento metálico que cruza o arco pela
- Durante a montagem de modelos para planejamento protético, utiliza-se arco facial com um a
- Uma prótese total mandibular resiste razoavelmente à remoção vertical, mas desloca-se late
- Pessoa sem dor nem travamento relata estalos discretos em uma ATM há anos, com abertura fu
- Durante moldagem definitiva de uma coroa, a margem cervical está recoberta por sangramento
- Uma coroa de zircônia necessita de protocolo adesivo e apresenta retenção geométrica limit
- Durante seleção de cor, duas amostras apresentam matiz semelhante, porém uma aparenta ser 
- No projeto de uma PPR de extremidade livre, deseja-se diminuir a transmissão de torque ao 
- Em PPR mandibular de extremo livre, a mastigação e alimentos pegajosos alternam forças de 
- Um arco possui uma área intercalar posterior de três dentes e outra área intercalar de um 
- Em um arco inferior com pouca profundidade funcional no sulco lingual, avalia-se a substit
- Durante confecção de prótese total, registra-se a altura facial na postura mandibular de r
- Durante prova dos dentes artificiais de uma prótese total dupla, o clínico analisa contato
- Uma pessoa com dor mastigatória persistente apresenta dificuldade funcional, alteração do 
- Uma ponte fixa planejada para substituir três dentes posteriores teria dois pilares de bai
- Uma restauração indireta adesiva de cerâmica vítrea será instalada em preparo com margens 
- Uma PPR classe II será planejada junto a pilar com socavado importante de tecido mole no t
- Na prova de uma prótese total dupla, os roletes foram registrados em relação articular rep
- Paciente tem dor em região de ATM e episódio de travamento com limitação de abertura; o ex
- Em molar com cúspide parcialmente destruída, o planejamento de uma onlay prevê cobertura c
- Uma coroa se desloca com tração axial; por esse motivo, aumentar a forma de resistência às
- Na análise de uma arcada desdentada em ambos os segmentos posteriores, sem dentes distais 
- Na prótese total mandibular que gira durante a mastigação, a propriedade em falha denomina
- Paciente apresenta estalido articular esporádico, indolor e sem limitação. A ausência de r
- Em preparo para coroa com margem encoberta por tecido inflamado, o escaneamento óptico eli
- No desenho de PPR de extremidade livre, a maior compressibilidade da mucosa em relação ao 
- Na classificação de Kennedy, uma área anterior cruzando a linha média mantém a classe IV m
- Ao confeccionar uma prótese total dupla, registrar relação cêntrica e dimensão vertical sã
- Em pessoa com mialgia mastigatória crônica, a avaliação do DC/TMD deve limitar-se a palpaç
- Uma coroa sobre dente tratado endodonticamente possui pino bem adaptado, mas quase nenhuma
- Uma coroa metalocerâmica com infraestrutura plana, sem suporte anatômico para porcelana e 
- Ao projetar uma PPR de extremidade livre, é possível que um grampo em I seja inadequado po
- Uma PPR classe II com base distal instável pode dispensar a rigidez do conector maior se r
- Na montagem de prótese total, uma diferença entre RC e MI exige conferência dos registros 
- Em reabilitação fixa de vários elementos, o escaneamento intraoral com CAD/CAM não elimina
- Uma prótese total recém-instalada apresenta úlcera na borda por sobreextensão. O manejo ad
- Se uma pessoa com dor relacionada à mastigação apresenta estalo articular e exame que repr
- Se a investigação clínica de travamento articular justificar exame de imagem dos tecidos m
- Pessoa com clique indolor e função preservada, mas com discreta diferença entre relação cê
```

## Pedido 19: Contabilidade avançada: investimentos, combinação de negócios e consolidação (`contabilidade.contabilidade-avancada`, 233 tópicos de edital)

```
Matéria: contabilidade.contabilidade-avancada (Contabilidade avançada: investimentos, combinação de negócios e consolidação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Participações societárias, influência significativa e negócios em conjunto": 19
- "2. Equivalência patrimonial, dividendos e ajustes de aquisição": 20
- "3. Combinação de negócios pelo método de aquisição": 20
- "4. Consolidação, transações internas e mudanças de participação": 27
- "5. Demonstrações separadas, fusão, incorporação e cisão": 14

Já existem (não repita):
- Uma sociedade comprou 25% das ações votantes de outra e participa regularmente da definiçã
- Uma investidora detém 30% de coligada que apurou lucro de R$ 120.000. Não existem ajustes 
- Uma coligada avaliada pelo MEP declara dividendos totais de R$ 50.000. A investidora tem 4
- Duas entidades celebram contrato que exige consentimento unânime sobre produção e financia
- A controladora possui 80% de uma controlada e prepara demonstrações consolidadas. Como dev
- A empresa X absorve integralmente a empresa Y, que deixa de existir, e X sucede Y em direi
- Uma entidade compra 100% de um negócio por R$ 750.000. Os ativos líquidos identificáveis v
- Uma controladora vende à controlada mercadorias com lucro de R$ 40.000. Ao fim do período,
- Uma sociedade transfere 30% de seus ativos e parte dos passivos para outra entidade, mante
- Uma sociedade possui controlada e prepara sua demonstração contábil individual brasileira.
- Uma investidora com 35% de coligada registra lucro atribuível de R$ 70.000 e dividendos de
- Uma empresa paga R$ 500.000 por 40% de coligada com patrimônio contábil de R$ 1.000.000. A
- Uma empresa adquire 80% de um negócio por R$ 900.000. Os ativos líquidos identificáveis sã
- Uma investidora adquiriu 40% de coligada cuja mais-valia total de equipamento é R$ 100.000
- Uma empresa obtém 100% de negócio por R$ 430.000, quando os ativos líquidos identificáveis
- Uma coligada vende à investidora mercadorias com lucro de R$ 60.000, que permanecem inteir
- Uma entidade adquire um negócio e paga R$ 40.000 de auditoria de diligência e R$ 15.000 po
- Uma sociedade já possuía 25% de coligada contabilizada pelo MEP e agora adquire mais ações
- Uma coligada gera prejuízo atribuível de R$ 50.000. A investidora tem investimento por MEP
- A controla 80% de B. B vende mercadorias a A com margem de R$ 50.000, toda ainda em estoqu
- A controla 70% de B e paga R$ 150.000 para adquirir mais 10% do capital, mantendo o contro
- Uma controladora vende ações e perde o controle de sua antiga controlada. Permanecem 25% d
- Uma sociedade tem 35% das ações votantes de investida, mas um acordo dá a terceiro a direç
- A possui 60% do capital de B. Em suas demonstrações separadas, elaboradas conforme CPC 35 
- Uma sociedade tem ativos de R$ 900.000 e passivos de R$ 300.000. Em cisão parcial, transfe
- A entidade possui 22% dos votos de outra sociedade, sem fatores que afastem a regra geral.
- Uma sociedade que detém 55% do capital econômico de uma investida necessariamente a contro
- Uma investidora reconheceu lucro de coligada pelo MEP. Quando os dividendos atribuíveis sã
- Dois participantes têm direitos somente sobre os ativos líquidos de um empreendimento conj
- Uma controladora e sua controlada possuem respectivamente conta a receber e a pagar de R$ 
- Uma adquirente apurou goodwill de R$ 120.000 na compra de um negócio. Como a vida útil é i
- Uma coligada reconheceu ganho em outros resultados abrangentes. Na aplicação do MEP, a inv
- Uma investidora que aplica MEP deve reconhecer, além do lucro proporcional da coligada, o 
- Uma adquirente pagou R$ 400.000 por 100% de negócio cujos ativos líquidos identificáveis s
- Durante uma aquisição de controle, a compradora gastou R$ 25.000 com consultoria jurídica 
- Uma controladora alienou parte da participação e perdeu efetivamente o controle. Na consol
- Uma controladora aumenta de 70% para 85% sua participação em controlada. Como mantém o con
- Em operação em conjunto, uma participante tem direito a 30% da produção e é obrigada a arc
- Um investidor possui 18% do poder de voto, participa formalmente de decisões de políticas 
- Uma coligada apura lucro de R$ 50.000 vendendo mercadorias à investidora que possui 40% do
- Uma controlada vende mercadorias à controladora obtendo lucro de R$ 100.000; metade dos pr
- Embora o CPC 18 tenha recebido a revisão R3, seu item 2A mantém a exigência de equivalênci
- Uma adquirente paga R$ 660.000 por 75% de um negócio cujos ativos líquidos identificáveis 
- Uma adquirente assume contraprestação contingente classificada como passivo em uma combina
- Uma companhia realiza cisão parcial transferindo um conjunto de ativos e passivos entre en
```

## Pedido 20: Crimes contra a ordem tributária, econômica e relações de consumo (`legislacao-penal-especial.crimes-ordem-tributaria`, 233 tópicos de edital)

```
Matéria: legislacao-penal-especial.crimes-ordem-tributaria (Crimes contra a ordem tributária, econômica e relações de consumo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ilícito tributário e crimes dos particulares: arts. 1º e 2º da Lei 8.137/1990": 24
- "2. Crime funcional, ICMS declarado, lançamento definitivo e competência": 20
- "3. Penas, pagamento, parcelamento, colaboração e insignificância": 20
- "4. Crimes contra a ordem econômica e combustíveis: Leis 8.137 e 8.176": 17
- "5. Crimes nas relações de consumo e disposições penais do CDC": 19

Já existem (não repita):
- Uma empresa omite receitas na declaração com propósito de reduzir imposto, e o procediment
- Um auditor compara as penas abstratas dos arts. 1º e 2º da Lei 8.137/1990. Qual correspond
- Um contribuinte é investigado por crime material do art. 1º, II, enquanto seu auto de infr
- Uma pessoa mantém sistema informatizado capaz de produzir registros contábeis paralelos àq
- Na fiscalização de uma loja, verifica-se exposição de produto impróprio ao consumo por neg
- Um servidor utiliza sua posição para defender interesse particular perante a repartição tr
- Uma empresa efetua parcelamento regularmente antes do recebimento da denúncia por crime tr
- Após colocar um brinquedo no mercado, o fabricante descobre perigo relevante e deixa de av
- Quatro comerciantes combinam artificialmente a quantidade a vender em determinada região p
- Um empresário informa reiteradamente o ICMS devido, mas não o recolhe. Para enquadrá-lo no
- Um fiscal exige vantagem indevida para cobrar somente parte de tributo que deveria lançar 
- Na sentença por crime do art. 2º, o julgador examina agravante do art. 12 da Lei 8.137. Qu
- Em setembro de 2026, um comerciante compra deliberadamente combustível que sabe ter sido s
- Empresa suspeita de fraude tributária encontra referência ao art. 5º da Lei 8.137/1990 em 
- A Lei 8.137/1990 prevê benefício por revelação espontânea de trama criminosa praticada em 
- Em processo relativo a tributo federal, o débito imputado ao agente é de R$ 18 mil. Em qua
- Uma empresa anuncia no rótulo inexistente presença de determinado insumo e usa essa altera
- O investigado pelo art. 1º, V, da Lei 8.137/1990 negou documento fiscal obrigatório em ven
- Contribuinte foi formalmente declarado devedor contumaz por decisão administrativa definit
- Em 2026, um comerciante adquire combustível de valor muito inferior ao usual, em condições
- Considerando a Lei 8.137/1990 depois da LC 224/2025, qual circunstância foi incluída expre
- Uma sentença aplica art. 12 da Lei 8.137 como agravante a delito funcional do art. 3º, afi
- Uma pessoa pretende invocar o Tema 157 do STJ para débito exclusivamente estadual de ICMS 
- A condenação por crime do art. 1º-A da Lei 8.176/1991 envolve um estabelecimento comercial
- Para os crimes dos arts. 1º-A e 1º-B da Lei 8.176, um agente ocupava emprego público à épo
- Um fiscal lavrou auto de infração ainda sujeito a recurso e, por esse simples ato, conside
- Um contribuinte retém tributo descontado de terceiro que deveria recolher ao erário. O art
- Um agente público patrocina interesse particular perante o fisco em razão da função. O art
- Uma pessoa remete notícia escrita e documentos sobre fraude tributária às autoridades comp
- Uma comerciante expõe, por negligência, mercadoria imprópria ao consumo. A forma culposa d
- Em outubro de 2026, o art. 1º, II, da Lei 8.176/1991 ainda descreve expressamente o uso ir
- Uma venda efetiva é realizada sem nota fiscal obrigatória. Segundo orientação de 2025, o d
- Ao dosar pena de crime funcional do art. 3º da Lei 8.137/1990, o julgador aplicou a agrava
- Em crime praticado em coautoria, partícipe revela espontaneamente à autoridade policial to
- O Tema 157 do STJ fixa R$ 20 mil como limite de insignificância obrigatório e indistinto p
- Uma indústria descobre nocividade relevante de produto após colocá-lo no mercado e deixa d
- Um fornecedor oculta, no lançamento inicial de produto perigoso, alerta obrigatório de noc
- Um comerciante primário é condenado pelo art. 1º-B da Lei 8.176/1991. A lei de 2026 admite
- Uma empresa que atrasou tributos por alguns meses, sem decisão administrativa definitiva d
- Um comerciante praticou em outubro de 2026 a circulação de combustível sabidamente proveni
- No concurso material entre condutas econômicas, a multa de 10 a 360 dias-multa prevista pe
- Condenado pelo art. 1º-A da Lei 8.176/1991 ocupava emprego público. O art. 1º-C prevê perd
- Na recusa de emissão de nota fiscal obrigatória, a falta de resposta à exigência da autori
- Após receber a denúncia penal, uma sociedade faz pedido de parcelamento tributário e alega
- Um agente exerce comércio irregular de combustível que sabe proveniente de crime, mas o au
```

## Pedido 21: Sobrevivência no mar, emergências e combate a incêndio a bordo (`marinha-mercante.sobrevivencia-emergencias-incendio-bordo`, 232 tópicos de edital)

```
Matéria: marinha-mercante.sobrevivencia-emergencias-incendio-bordo (Sobrevivência no mar, emergências e combate a incêndio a bordo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sobrevivência no mar e equipamentos de salvatagem": 20
- "2. Comunicação, sinalização, abandono e evacuação": 17
- "3. Segurança a bordo e fainas de emergência": 23
- "4. Prevenção e combate a incêndio": 40

Já existem (não repita):
- O sinal de alarme geral de emergência a bordo consiste em sete ou mais apitos curtos segui
- A tabela mestra (muster list) é o documento afixado a bordo que indica:
- A ordem de abandono do navio é dada pelo comandante.
- Para um náufrago sozinho na água fria, com colete salva-vidas, a atitude que mais reduz a 
- Na embarcação de sobrevivência, em regra, a água não é distribuída nas primeiras 24 horas,
- O dispositivo que libera automaticamente a balsa salva-vidas inflável quando o navio afund
- Os botes de resgate (rescue boats) destinam-se principalmente a resgatar pessoas na água e
- Em navios petroleiros, as baleeiras devem ser:
- A boia salva-vidas pode ser equipada com luz de acendimento automático e retinida, que é u
- O meio de proteção térmica (TPA), presente nas embarcações de sobrevivência, serve para:
- As balsas salva-vidas infláveis devem passar por serviço anual em estação credenciada, alé
- A EPIRB é uma radiobaliza que transmite, via satélite, o alerta de socorro com a identific
- O SART, ao ser varrido pelo radar de um navio ou aeronave de busca, gera na tela do radar 
- O sinal pirotécnico que produz fumaça laranja, indicado para uso diurno, é o:
- Os sinais pirotécnicos devem ser disparados assim que a embarcação de sobrevivência é lanç
- Durante o içamento de uma pessoa por helicóptero, o cabo ou gancho do guincho:
- Na cinta de resgate (strop) usada no içamento por helicóptero, a alça deve ficar sob as ax
- Ao ser obrigado a pular do navio na água, com colete salva-vidas, o tripulante deve:
- O Brasil tem uma região de busca e salvamento (região SAR) sob sua responsabilidade, coord
- A entrada em espaços confinados a bordo, como tanques e duplos fundos, exige, entre outras
- O Sistema de Gestão de Segurança, previsto no Código ISM, estabelece procedimentos, respon
- Ao ver uma pessoa cair no mar, as primeiras ações de quem presenciou devem ser:
- A curva de Williamson é uma manobra usada em caso de homem ao mar, que traz o navio de vol
- Diante de um pequeno derrame de óleo no convés durante abastecimento, NÃO é ação correta:
- A familiarização com os equipamentos de segurança, as rotas de fuga, os postos e os alarme
- O trabalho a quente a bordo, como solda e corte, exige permissão de trabalho, retirada de 
- O tetraedro do fogo é formado por:
- A propagação do calor através de uma antepara de aço aquecida, que pode iniciar um incêndi
- Um incêndio em painel elétrico energizado deve ser combatido, preferencialmente, com:
- Incêndios de classe A envolvem materiais sólidos comuns, como madeira, papel e tecidos, qu
- Aplicar jato pleno de água sobre óleo combustível em chamas é a técnica mais eficaz de ext
- O método de extinção que atua retirando ou reduzindo o oxigênio disponível para a combustã
- O pó químico atua na extinção do fogo principalmente pela quebra da reação em cadeia da co
- Ao usar o aparelho de respiração autônomo (ARA) no combate a incêndio, o bombeiro deve:
- Ao detectar um princípio de incêndio a bordo, a primeira providência deve ser:
- No combate a incêndio a bordo, fechar portas, escotilhas e dampers e parar a ventilação do
- O sistema fixo de CO₂ da praça de máquinas só deve ser descarregado após a evacuação total
- O sistema que mantém a atmosfera dos tanques de carga de petroleiros com baixo teor de oxi
- Depois de extinto o incêndio, o rescaldo consiste em:
- A roupa de bombeiro a bordo inclui, entre outros itens, botas e luvas isolantes, capacete,
- Os exercícios de abandono e de incêndio a bordo de navios de carga são, em regra, realizad
```

## Pedido 22: Lei de Drogas (`legislacao-penal-especial.lei-drogas`, 231 tópicos de edital)

```
Matéria: legislacao-penal-especial.lei-drogas (Lei de Drogas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema legal, porte para consumo e Tema 506": 25
- "2. Tráfico e figuras penais dos arts. 33 a 39": 21
- "3. Majorantes, art. 40-A, colaboração e individualização": 21
- "4. Prisão, laudos, inquérito e técnicas de investigação": 17
- "5. Rito especial, defesa e cobrança integrada": 16

Já existem (não repita):
- Após a publicação da Lei nº 11.343/2006, um material de estudo ainda apresenta a Lei nº 6.
- Uma pessoa é encontrada com cannabis para consumo próprio, sem sinais concretos de comerci
- Em um ponto de distribuição, uma pessoa oferece gratuitamente porções de droga a terceiros
- Um investigado sem condenações anteriores pretende obter a causa de diminuição do tráfico 
- Duas pessoas combinam, por ocasião de um único encontro, vender uma porção de droga e não 
- Uma carga de drogas tinha destino comprovado em país estrangeiro, mas foi apreendida antes
- Um inquérito por tráfico investiga pessoa presa. Na ausência de duplicação judicial, qual 
- Após oferecimento de denúncia por tráfico, antes de recebê-la, o juiz precisa assegurar ma
- Em processo por tráfico, foi recebida a denúncia, sem necessidade de avaliação de dependên
- Uma pessoa oferece gratuitamente droga a uma amiga, ocasionalmente, para que ambas consuma
- Uma oficina clandestina armazena aparelhos especialmente destinados à preparação de drogas
- Uma pessoa transmite rotineiramente informações de fiscalização a uma associação dedicada 
- Uma substância controlada é ministrada por negligência a paciente que dela não necessita, 
- Depois de consumir droga, uma pessoa conduz uma pequena embarcação e realiza manobra que e
- Em crime enquadrado no art. 33, prova-se que o agente integra grupo paramilitar e agiu no 
- Em investigação de tráfico, o acusado colabora voluntariamente, identifica outros envolvid
- No flagrante de tráfico, o exame inicial da substância é assinado por pessoa idônea porque
- Uma pessoa é flagrada com 28 g de cannabis em numerosos invólucros individualizados, uma b
- Uma operação documentou previamente vendas ilícitas e utilizou agente disfarçado, que rece
- A apreensão ocorreu dentro de um estado, mas documentos e mensagens demonstram inequivocam
- Em fato praticado após a Lei nº 15.358/2026, agente integrante de agrupamento indicado pel
- Na dosimetria de condenação pelo art. 33, a decisão pretende considerar a natureza e quant
- Drogas foram apreendidas em investigação sem prisão em flagrante. A autoridade pretende de
- Na investigação de rede de tráfico, planeja-se deixar prosseguir uma remessa monitorada pa
- Em processo sujeito ao rito especial, após a defesa prévia, o juiz recebe a denúncia e a a
- Ao analisar um produto potencialmente capaz de causar dependência, o examinador verifica s
- Em qualquer apreensão superior a 40 gramas de cannabis, a quantidade basta para determinar
- A pessoa que entrega gratuitamente droga a destinatários para distribuição pode incidir no
- A simples união episódica de duas pessoas para uma venda isolada de drogas já preenche o c
- Conduzir uma aeronave após consumir droga, expondo a incolumidade alheia a dano potencial,
- Em investigação por tráfico, a pessoa solta sujeita-se, como regra especial, a inquérito d
- A majorante do art. 40, referente a transnacionalidade, locais protegidos e outras situaçõ
- Introduzido pela Lei nº 15.358/2026, o art. 40-A determina pena em dobro para todas as inf
- A colaboração voluntária que contribui para identificar demais envolvidos e recuperar prod
- A prescrição ou administração culposa de droga desnecessária a um paciente é punida pelo a
- Para formalizar um flagrante de tráfico, o art. 50 admite laudo inicial de constatação por
- No rito especial do tráfico, o art. 55 exige que a notificação para defesa prévia escrita,
- Se for determinada avaliação para atestar dependência de drogas, o art. 56, § 2º, admite q
- Em uma carga de drogas comprovadamente destinada ao exterior, a Súmula 607 exige que a rem
- A expressão reiteradamente ou não, existente no art. 35, não dispensa prova do vínculo ass
- Em condenação na qual foi reconhecida a causa de diminuição do art. 33, § 4º, o delito per
- Na técnica de não atuação policial do art. 53, a autorização judicial, após manifestação d
- Em uma apreensão acompanhada de prisão em flagrante, o art. 50-A determina que a destruiçã
- O art. 33, § 1º, IV, exige elementos probatórios razoáveis de conduta criminal preexistent
- Após o Tema 506, a aquisição de cannabis para consumo próprio passou a gerar condenação cr
```

## Pedido 23: Medicina veterinária preventiva: controle populacional, defesa sanitária animal, inspeção de produtos de origem animal e ética profissional (`medicina-veterinaria.defesa-sanitaria-inspecao-etica`, 231 tópicos de edital)

```
Matéria: medicina-veterinaria.defesa-sanitaria-inspecao-etica (Medicina veterinária preventiva: controle populacional, defesa sanitária animal, inspeção de produtos de origem animal e ética profissional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Saúde única e controle populacional de animais domésticos": 25
- "2. Defesa sanitária animal: programas, trânsito, vacinação e focos": 25
- "3. Inspeção de produtos de origem animal": 25
- "4. Ética, legislação veterinária e responsabilidade técnica": 25

Já existem (não repita):
- O conceito de saúde única reconhece que a saúde humana, a saúde animal e a saúde ambiental
- Um município pretende reduzir de forma duradoura a população de cães e gatos errantes. A e
- A legislação federal permite que órgãos de controle de zoonoses eliminem cães e gatos sadi
- A captura e eliminação de parte de uma população de cães errantes não produz redução durad
- Em áreas rurais, cães errantes ou soltos podem predar rebanhos e animais silvestres e tran
- Ao suspeitar de febre aftosa em um rebanho, o médico-veterinário particular deve:
- No PNCEBT, a vacinação contra a brucelose é obrigatória para fêmeas bovinas e bubalinas na
- No programa de controle da raiva dos herbívoros, uma das principais medidas é o controle p
- Para participar de um leilão, um equino deve apresentar exames negativos para as doenças a
- A principal medida de prevenção da encefalopatia espongiforme bovina é a proibição de usar
- A defesa sanitária animal no território do estado de Mato Grosso é executada pelo órgão es
- A Guia de Trânsito Animal (GTA) é exigida apenas quando os animais se destinam a outro est
- Em uma barreira sanitária, o fiscal encontra caminhão com bovinos sem GTA. A conduta compa
- Ao confirmar um foco de doença de controle oficial, a primeira medida do serviço veterinár
- O vazio sanitário consiste em manter as instalações sem animais por um período, após limpe
- Em granjas avícolas, são medidas de biosseguridade:
- Na fiscalização de revendas de produtos veterinários, verifica-se, entre outros pontos, a 
- Um frigorífico de bovinos que vende carne para outros estados e exporta deve estar registr
- O SISBI-POA permite que estabelecimentos inspecionados por serviços estaduais ou municipai
- O Regulamento da Inspeção Industrial e Sanitária de Produtos de Origem Animal (RIISPOA) vi
- No abate humanitário de bovinos, a sequência correta é:
- Na inspeção ante mortem, os animais passam por descanso, jejum e dieta hídrica antes do ab
- Na inspeção post mortem, carcaças com lesões suspeitas identificadas nas linhas de inspeçã
- Quando uma carcaça é destinada ao aproveitamento condicional, significa que:
- São características sensoriais de pescado fresco:
- A pesquisa de histamina é especialmente importante na fiscalização de peixes como atum e s
- A refrigeração e o congelamento destroem as bactérias presentes nos alimentos de origem an
- Sobre os tratamentos térmicos do leite, é correto afirmar que:
- O sistema APPCC (Análise de Perigos e Pontos Críticos de Controle) é preventivo e inclui a
- Em relação à interface entre inspeção sanitária e proteção ambiental em abatedouros, é cor
- O lançamento do soro de leite de laticínios em rios causa grave poluição, devido à sua ele
- A Lei nº 5.517/1968 dispõe sobre o exercício da profissão de médico-veterinário e cria o C
- Quanto à natureza e às funções do CFMV e dos CRMVs, é correto afirmar que:
- Entre as penalidades disciplinares aplicáveis ao médico-veterinário pelos conselhos, NÃO s
- Segundo o Código de Ética do Médico Veterinário, é permitido emitir atestado sanitário de 
- O médico-veterinário deve manter sigilo sobre fatos de que tenha conhecimento no exercício
- A responsabilidade técnica de um médico-veterinário em estabelecimento agropecuário é form
- São deveres do responsável técnico em um laticínio, EXCETO:
- O responsável técnico responde apenas eticamente perante o CRMV, não podendo ser responsab
- Em criadouros de fauna silvestre e em programas de manejo de fauna em licenciamento ambien
- Sobre a eutanásia de animais, segundo as normas éticas da medicina veterinária, é correto 
```

## Pedido 24: Tributos sobre lucro e faturamento e retenções na fonte (`contabilidade-tributaria.tributos-retencoes-fonte`, 229 tópicos de edital)

```
Matéria: contabilidade-tributaria.tributos-retencoes-fonte (Tributos sobre lucro e faturamento e retenções na fonte)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e tratamento contábil dos tributos": 18
- "2. IRPJ e CSLL: apuração, provisões e tributos diferidos": 18
- "3. PIS/Pasep, Cofins, CIDE, ISS e participações governamentais": 29
- "4. Retenções federais na fonte: IRPJ, CSLL, Cofins, PIS/Pasep e ISS": 21
- "5. Retenção previdenciária, conciliação tributária e situações avançadas": 14

Já existem (não repita):
- Uma empresa apurou lucro tributável de R$ 100.000 em trimestre completo e não possui compe
- Uma prestadora em regime geral de CSLL a 9% apurou base tributável de R$ 50.000, sem compe
- Uma sociedade prestou serviço por R$ 8.000 e, ao receber, sofreu retenção antecipada de R$
- Uma empresa do regime não cumulativo deseja identificar as alíquotas gerais de PIS/Pasep e
- Um pagamento sujeito apenas à retenção das três contribuições mencionadas nos arts. 30 e 3
- Uma atividade tributável por ISS consta da lista da LC nº 116/2003, possui preço de R$ 20.
- Uma contratante recebeu nota de R$ 10.000 por serviço que efetivamente se enquadra no art.
- Uma sociedade com prejuízo fiscal acumulado deseja reconhecer ativo fiscal diferido por in
- Uma pessoa jurídica remunera contrato de transferência de tecnologia com residente no exte
- Em uma compra pública federal de R$ 20.000, a linha correta do Anexo I prevê IR 1,2%, CSLL
- Uma unidade administrativa municipal paga pessoa jurídica por fornecimento de bens enquadr
- Uma retenção calculada em R$ 9,50 incide em pagamento federal abrangido pela IN nº 1.234/2
- Um ativo apresenta valor contábil de R$ 120.000 e base fiscal de R$ 90.000. Sua recuperaçã
- Uma fatura de limpeza hospitalar de R$ 10.000 admite material previsto no contrato sem val
- Uma fornecedora reconheceu receita de serviços de R$ 10.000 e recebeu R$ 9.535 porque o pa
- Uma empresa executa serviço de construção civil do subitem 7.02 da LC nº 116/2003 em munic
- Uma contratante toma serviço de monitoramento eletrônico de segurança, sem disponibilizaçã
- Uma entidade reconheceu ganho patrimonial diretamente em outros resultados abrangentes, co
- Uma empresa revisa seu orçamento tributário para fatos geradores de 2026 e supõe que CBS d
- Uma associação sem fins lucrativos busca dispensa da retenção federal em pagamento recebid
- Uma nota de R$ 10.000 está, conforme enunciado contratual, sujeita simultaneamente a reten
- Uma empresa brasileira remunera licença para utilização de software de empresa estrangeira
- Uma empresa presta limpeza predial não hospitalar e destaca materiais na nota. O contrato 
- Uma contratante pretende estender a retenção previdenciária a um serviço que não correspon
- Ao estudar exploração de petróleo em regime de concessão, alguém afirma que royalties e pa
- Ao encerrar o período, uma empresa classifica IRPJ e CSLL como tributos sobre lucro para f
- Uma empresa com lucro tributável trimestral de R$ 100.000 calcula IRPJ de 25% sobre toda a
- Uma empresa no regime cumulativo geral, sem particularidades, pode calcular PIS de 0,65% e
- Uma prestadora em regime geral de CSLL a 9% recebe pagamento com retenção antecipada de CS
- Um prestador calcula ISS de serviço listado em R$ 500, sobre preço de R$ 10.000 à alíquota
- Toda fatura de serviço recebida por ente da administração pública federal sofre retenção p
- No pagamento de fornecedor por R$ 10.000 com retenção recuperável de R$ 465, a contratante
- Uma multa fiscal definitivamente indedutível reconhecida no resultado gera, por si só, dif
- Uma compra pública federal sujeita à retenção conjunta de 5,85% pode envolver 4,65% de CSL
- Na elaboração das demonstrações referentes a 2026, uma empresa retira PIS/Cofins e ISS de 
- Em serviço incluído no art. 6º, § 2º, da LC nº 116/2003, o fato de o tomador ser imune ou 
- Uma prestadora não cumulativa recebe retenção de Cofins a 3% na hipótese geral da IN nº 1.
- Para calcular retenção previdenciária em fatura de limpeza hospitalar, a IN nº 2.110/2022 
- A Lei nº 10.336/2001, art. 4º, obriga calcular CIDE-combustíveis em qualquer operação usan
- Uma fonte pagadora federal calcula retenção de R$ 9,50 e utiliza Darf eletrônico efetuado 
- Uma autarquia municipal paga fornecimento enquadrado no art. 3º-A da IN nº 1.234/2012 e fi
- Uma entidade reconhece ajuste de mensuração patrimonial diretamente em outros resultados a
- O art. 113 da IN nº 2.110/2022 declara exemplificativas as listas de serviços dos arts. 11
- Ao examinar dispensa de retenção em outubro de 2026 para entidade sem fins lucrativos, a f
- Uma empresa pode utilizar retenção antecipada de CSLL como se fosse retenção de IRPJ, comp
```

## Pedido 25: Fonoaudiologia: linguagem, fala, voz, motricidade orofacial e disfagia (`fonoaudiologia.linguagem-fala-voz-disfagia`, 229 tópicos de edital)

```
Matéria: fonoaudiologia.linguagem-fala-voz-disfagia (Fonoaudiologia: linguagem, fala, voz, motricidade orofacial e disfagia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Desenvolvimento da linguagem oral, escrita e alterações fonológicas": 17
- "2. Fluência, afasia, disartria e apraxia de fala": 17
- "3. Voz, disfonias e avaliação vocal": 17
- "4. Motricidade orofacial, amamentação, trauma e cirurgias da face": 17
- "5. Disfagia orofaríngea e neurogênica: avaliação, riscos e reabilitação": 16
- "6. Prática hospitalar, vias aéreas, câncer de cabeça e pescoço e ética profissional": 16

Já existem (não repita):
- Em avaliação fonoaudiológica, a dificuldade de compreender instruções verbais simples apon
- O uso de turnos de conversa e a adequação da mensagem ao interlocutor são investigados pre
- Uma criança substitui sistematicamente contrastes sonoros por um padrão de simplificação e
- Um escolar lê palavras isoladas com precisão, mas tem dificuldade persistente para inferir
- Em uma prova, o examinador descreve uma criança que utiliza uma variante regional sistemát
- Após um AVC, um adulto apresenta dificuldade adquirida de nomeação, compreensão de sentenç
- Pessoa com doença neurológica apresenta articulação imprecisa consistente, lentidão e redu
- Em apraxia de fala adquirida, qual achado favorece a hipótese em comparação com disartria 
- Um paciente com lesão hemisférica produz discurso fluente, mas pouco informativo, e aprese
- Ao orientar cuidadores de pessoa que gagueja, a conduta comunicativa mais consistente com 
- Qual estrutura é fundamental para a produção da voz laríngea habitual?
- Professora relata fadiga vocal e rouquidão prolongada; a inspeção sonora isolada permitiri
- Uma pessoa apresenta soprosidade após cirurgia cervical e passa a tossir durante refeições
- Na avaliação vocal, CAPE-V e GRBAS são usados principalmente para sistematizar:
- Entre medidas preventivas para profissional com uso intenso da voz, qual está mais bem fun
- Na avaliação de motricidade orofacial, qual conjunto representa funções tipicamente estuda
- Após cirurgia ortognática recente, o paciente apresenta rigidez e dificuldade de mastigar.
- Em recém-nascido prematuro com fadiga durante oferta oral, qual variável é indispensável p
- Após queimadura extensa de face e pescoço, a proposta fonoaudiológica deve priorizar:
- Ao avaliar um bebê com dificuldade de pega, mas sem exame funcional prévio, qual raciocíni
- Linguagem expressiva envolve formular mensagens, enquanto linguagem receptiva diz respeito
- Qualquer diferença entre pronúncia regional e padrão escolar deve ser diagnosticada como t
- Uma criança pode apresentar bom reconhecimento de palavras escritas e, ainda assim, dificu
- A consciência fonêmica e a consciência fonológica são expressões perfeitamente equivalente
- Afasia é um transtorno adquirido de linguagem que pode comprometer compreensão, nomeação, 
- Disartria e apraxia de fala são sinônimos, já que ambas decorrem necessariamente de fraque
- A observação de bloqueios com esforço, prolongamentos e evitação comunicativa pode integra
- Na afasia do tipo Wernicke, a produção verbal é obrigatoriamente não fluente e a compreens
- A intensidade vocal e a frequência fundamental descrevem dimensões distintas da produção d
- A percepção de rouquidão permite confirmar nódulos vocais com a mesma precisão de uma aval
- Uma disfonia pós-intubação, associada a dificuldade de alimentação, pode demandar investig
- Para qualquer paciente com disfonia, o repouso vocal absoluto por tempo indefinido é super
- A avaliação miofuncional orofacial deve considerar não apenas estruturas isoladas, mas tam
- A presença de respiração predominantemente oral prova que a etiologia é sempre fraqueza la
- Na alimentação de prematuros, condições respiratórias e coordenação sucção–deglutição–resp
- Após cirurgia ortognática, exercícios resistidos máximos devem ser aplicados independentem
- A aspiração silenciosa pode ocorrer sem manifestação de tosse durante a deglutição.
- Na fisiopatologia da deglutição, penetração laríngea e aspiração são equivalentes porque a
- A triagem de disfagia orienta a identificação de risco, mas não substitui avaliação clínic
- A sonda enteral elimina a possibilidade de pneumonia aspirativa, pois impede aspiração de 
- A videoendoscopia da deglutição permite avaliar secreções e estruturas faringolaríngeas, a
- O espessamento de líquidos previne de maneira garantida a pneumonia por aspiração em qualq
- O cuff de uma cânula de traqueostomia, quando insuflado, não oferece proteção absoluta con
- A presença de traqueostomia permite instalar válvula de fala em qualquer paciente, indepen
- Após laringectomia total, as vias respiratória e digestiva ficam anatomicamente separadas,
- Em pessoa recentemente extubada, o simples decorrer de um prazo fixo, sem avaliação clínic
- Consentimento esclarecido, sigilo, registro fiel e respeito aos limites técnicos fazem par
- Registrar apenas “paciente estável”, sem descrever consistências testadas, sinais, decisão
```

## Pedido 26: Infraestrutura urbana, sistema viário e topografia (`arquitetura.infraestrutura-urbana-topografia`, 229 tópicos de edital)

```
Matéria: arquitetura.infraestrutura-urbana-topografia (Infraestrutura urbana, sistema viário e topografia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento integrado, equipamentos e mobilidade urbana": 19
- "2. Sistema viário, geometria, circulação e pavimentação": 19
- "3. Drenagem, abastecimento de água e esgotamento sanitário": 23
- "4. Resíduos, topografia, curvas de nível e georreferenciamento": 20
- "5. Soluções de baixo impacto, integração e leitura de projetos": 19

Já existem (não repita):
- Em um plano de mobilidade, o projetista precisa escolher a diretriz de prioridade de circu
- Ao avaliar a implantação de um bairro, uma equipe precisa distinguir equipamentos comunitá
- Uma via urbana apresenta interseções em nível, acesso a imóveis laterais e ligação entre d
- Um eixo de rua sobe 4 metros ao longo de 100 metros medidos horizontalmente. Qual é a decl
- Uma rua alaga porque a água superficial não ingressa adequadamente na rede. Qual conjunto 
- Um projeto prevê rede de esgoto sanitário separada da rede de águas pluviais. Qual interpr
- Na revisão do plano municipal de resíduos, propõe-se enviar para aterro os materiais passí
- Duas regiões do mesmo mapa possuem curvas de nível com igual equidistância vertical. Na re
- Uma avenida classificada como coletora não possui sinalização regulamentadora de velocidad
- Um loteamento é proposto em terreno sujeito a inundações recorrentes. A equipe argumenta q
- Para estimar vazão de pico pelo método racional, admitem-se C = 0,60, intensidade de 60 mm
- Uma cobertura de 120 m² recebe chuva uniforme de 20 mm. Admitindo aproveitamento de 75% da
- Numa planta topográfica de escala 1:2.000, a distância medida entre duas esquinas é de 3 c
- Dois levantamentos georreferenciados da mesma cidade apresentam posições diferentes para u
- Ao comparar alternativas de pavimentação, uma equipe confunde pavimento flexível e rígido 
- Uma cidade pretende reorganizar a coleta seletiva de resíduos residenciais. Qual solução f
- Um empreendimento prevê área para um equipamento de saúde, mas a demanda local já é elevad
- Propõe-se implantar jardim de chuva em solo de permeabilidade muito baixa, com lençol freá
- Na implantação de uma via, as cotas do terreno natural e do greide projetado alternam-se, 
- Uma bacia de contribuição recebe novas áreas impermeabilizadas a montante. O projeto manté
- Um setor urbano terá 2.000 moradores e consumo médio didático de 150 litros por habitante 
- Um empreendimento pretende lotear terreno com inclinação de 32%, afirmando que o limite de
- Após chuva intensa, duas bacias urbanas recebem volumes comparáveis. A primeira descarrega
- Um bairro com 12.000 moradores gera, em média, 0,8 kg de resíduos por pessoa ao dia. Sem c
- No projeto de eficiência energética de um conjunto de edifícios, uma equipe especifica aqu
- Em um loteamento, o estudo de implantação prevê rede de água, drenagem, esgotamento, energ
- Uma via com cruzamentos semaforizados e acesso direto aos lotes pode ser classificada como
- Ao planejar uma rota de ônibus e travessias de pedestres, considerar as conexões com escol
- Em sistema separador absoluto, a ligação do esgoto sanitário à boca de lobo é uma solução 
- Em um plano de limpeza urbana, material separado para reciclagem continua sendo resíduo, a
- Em mapa de curvas de nível, a equidistância indica a distância horizontal constante entre 
- Num projeto de iluminação de rua, a fotocélula pode comandar a ligação das luminárias quan
- Uma rua local possui placa regulamentando velocidade de 20 km/h. A placa deve ser desconsi
- Na representação planialtimétrica de uma quadra, medir distâncias e rumos integra a planim
- Dois lotes apresentam o mesmo consumo de água, mas um fica em cota topográfica elevada. A 
- Ao projetar drenagem de um setor impermeabilizado, a equipe propõe reservatório de detençã
- Um sistema de informação geográfica recebe coordenadas SAD69 e SIRGAS2000. Basta declarar 
- Em projeto de parcelamento, definir a localização aproximada dos terrenos para equipamento
- Se um mapa apresenta curvas com equidistância de 2 m e um trecho que cruza as curvas de 10
- No estudo de implantação de loteamento em encosta, a restrição do art. 3º, parágrafo único
- Uma poligonal topográfica fechada que apresenta erro linear de fechamento mensurável compr
- Uma administração deseja reduzir o envio de rejeitos ao aterro. Separar materiais recicláv
- Uma cisterna recebe água de chuva de cobertura e a armazena sem tratamento. Como o volume 
- Um cálculo de drenagem utiliza o método racional com intensidade em mm/h e área em hectare
- Como o SIRGAS2000 é referencial geodésico oficial brasileiro, qualquer planta em coordenad
```

## Pedido 27: Extinção do contrato: justa causa, rescisão indireta e verbas (`direito-trabalho.rescisao-contrato`, 229 tópicos de edital)

```
Matéria: direito-trabalho.rescisao-contrato (Extinção do contrato: justa causa, rescisão indireta e verbas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Modalidades de extinção, proteção contra dispensa e dispensa coletiva": 20
- "2. Justa causa do empregado: hipóteses do art. 482 e prova da gravidade": 25
- "3. Rescisão indireta, culpa recíproca e modalidades especiais": 28
- "4. Extinção por acordo, FGTS e verbas rescisórias": 15
- "5. Quitação, prazo do art. 477, multas e casos integrados": 12

Já existem (não repita):
- Uma empresa dispensa empregado sem imputação de falta grave e possui base atualizada de R$
- Empregado descumpre ordem individual lícita e direta de seu superior relativa à execução d
- Durante meses, a empresa descumpre obrigação contratual essencial de pagar pontualmente a 
- Uma trabalhadora pleiteia rescisão indireta por descumprimento contratual grave do emprega
- Empregado e empregador pactuam validamente a extinção contratual do art. 484-A. A base atu
- Na extinção por acordo do art. 484-A, existem férias proporcionais e décimo terceiro propo
- Trabalhador e empresa encerram validamente o vínculo pelo art. 484-A e discutem a moviment
- Um contrato sem prazo determinado é encerrado, e a empresa precisa entregar documentação d
- A empresa atrasa o pagamento das verbas rescisórias além do prazo do art. 477, § 6º, sem q
- Uma empresa pretende dispensar simultaneamente grande quantidade de empregados e entende q
- Um profissional perde habilitação legal essencial ao trabalho por ato deliberadamente prat
- Trabalhador responde a processo criminal, mas ainda não existe condenação definitiva. A em
- Após o fim de benefício previdenciário, empregado deixa de comparecer por mais de 30 dias 
- É reconhecida judicialmente culpa recíproca na ruptura contratual. Quanto às parcelas abor
- Duas partes encerram por acordo válido um contrato de trabalho e discutem o Programa de Se
- Em processo trabalhista, a empresa admite que deve R$ 5.000,00 de verbas rescisórias, mas 
- Um empregado recebe por produção por peça. O empregador reduz unilateralmente o volume de 
- Antes de uma dispensa coletiva, empresa realizou reuniões efetivas com a entidade sindical
- Após prova de faltas graves concorrentes de ambas as partes, é reconhecida judicialmente c
- Trabalhador é dispensado por justa causa validamente comprovada após adquirir férias venci
- Empregado com férias proporcionais de R$ 1.800,00 e terço de R$ 600,00 concorda livremente
- Uma pessoa exerce atividade econômica paralela sem autorização do empregador. A empresa de
- Na rescisão, empregador entrega recibo discriminando saldo de salário e férias vencidas, m
- O pagamento de verbas rescisórias ocorreu após dez dias do término contratual, mas há prov
- Após agressão física sofrida do empregador, sem circunstância de legítima defesa, empregad
- Em dispensa sem justa causa, a indenização prevista no art. 18, § 1º, da Lei 8.036/1990 co
- Em justa causa fundada no art. 482, d, da CLT, basta ter sido instaurada ação penal contra
- O descumprimento grave das obrigações contratuais pelo empregador pode fundamentar rescisã
- Na extinção por acordo prevista no art. 484-A da CLT, as férias proporcionais e o décimo t
- O art. 477, § 6º, da CLT prevê até dez dias após o término contratual para entrega dos doc
- O saque de até 80% do FGTS na rescisão consensual do art. 484-A significa que a empresa de
- Ao planejar dispensa coletiva, a empresa deve observar a intervenção sindical prévia exigi
- A perda da habilitação legal necessária ao exercício profissional enseja automaticamente j
- Quando a rescisão indireta se baseia nas alíneas d ou g do art. 483 da CLT, o § 3º permite
- Na culpa recíproca juridicamente reconhecida, a Súmula 14 do TST assegura aviso prévio e d
- Um recibo rescisório descreve e quantifica apenas saldo salarial e férias vencidas. Pelo a
- Empregado deixa de comparecer durante trinta dias apesar de possuir justificativas comprov
- Quando a culpa recíproca é reconhecida na rescisão por faltas de ambas as partes, a indeni
- A empresa contesta R$ 8.000,00 de verbas rescisórias e admite outros R$ 3.000,00, mas deix
- Empregado pede demissão após oito meses de contrato por prazo indeterminado. A Súmula 261 
- Basta ao empregador provar que empregado realizou uma única venda particular fora do exped
- Na justa causa do empregado suficientemente comprovada, a ausência de aviso patronal e de 
- O atraso salarial reiterado é alegado como falta patronal grave e o empregador paga as qua
- Na extinção genuinamente consensual do art. 484-A, o aviso indenizado e a indenização de F
- Após informar o sindicato sobre desligamentos coletivos já inteiramente consumados, o empr
```

## Pedido 28: Telecomunicações, antenas e radiodifusão (`engenharia-eletrica.telecomunicacoes-antenas-radiodifusao`, 229 tópicos de edital)

```
Matéria: engenharia-eletrica.telecomunicacoes-antenas-radiodifusao (Telecomunicações, antenas e radiodifusão)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da comunicação, comutação e redes convergentes": 19
- "2. Codificação digital, modulações e compartilhamento do canal": 20
- "3. Antenas, eletromagnetismo e medições": 19
- "4. Circuitos de radiofrequência: osciladores, misturadores e amplificadores": 19
- "5. Propagação, satélites, fibras ópticas e radiodifusão digital": 23

Já existem (não repita):
- Uma estação transmite amostras discretas de um sinal de voz por um canal físico sujeito a 
- Um codificador PCM de voz produz 8.000 amostras por segundo e registra cada amostra com 8 
- Uma rede corporativa substituiu percursos telefônicos reservados por transporte de voz em 
- Uma aplicação de conversação mantém atraso médio próximo de 40 ms, mas enfrenta mudanças f
- Um enlace de fibra recebe três canais ópticos em comprimentos de onda diferentes no mesmo 
- Em um transmissor QPSK sem codificação corretora e sem sobrecargas, são emitidos 500 mil s
- Quatro usuários dividem um enlace em intervalos repetidos de transmissão, de modo que cada
- Um modulador 16-QAM emite 1 milhão de símbolos por segundo, sem redundância nem overhead. 
- Um sinal analógico limitado a 4 kHz foi digitalizado com amostragem de 6 kHz, sem filtro d
- Em uma recepção OFDM com multipercurso, uma equipe introduz prefixo cíclico antes do bloco
- Uma antena transmissora tem diretividade alta, porém dissipação importante nos condutores.
- Uma linha ideal de 50 Ω termina em carga puramente resistiva de 75 Ω. Qual combinação de c
- No cadastro de antenas, um modelo tem ganho de 0 dBd. Qual é aproximadamente seu ganho exp
- Um protótipo de antena apresenta bons níveis aparentes de potência irradiada, mas o ensaio
- Um receptor super-heteródino deve receber 90 MHz em uma frequência intermediária de 10 MHz
- Ao ajustar um oscilador LC, a equipe constata fase total nula e ganho de malha unitário em
- Um enlace ideal opera a 200 MHz e percorre 2 km em espaço livre. Usando FSPL(dB) = 32,44 +
- Dois estágios de amplificação apresentam primeiro estágio com fator de ruído linear F1 = 2
- Um amplificador sintonizado apresenta ressonância central em 10 MHz e largura aproximada d
- Uma rede satelital compara um satélite geoestacionário a uma constelação de baixa órbita p
- Uma especificação de enlace utiliza fibra monomodo conforme UIT-T G.652 e informa dispersã
- Ao preparar material técnico sobre televisão aberta digital no Brasil, uma pessoa precisa 
- Um projetista considera canal ideal de banda 1 MHz com relação sinal-ruído linear de 3. Qu
- Um enlace terrestre de micro-ondas tem visada geométrica livre entre duas antenas, mas uma
- Uma antena receptora ideal opera em comprimento de onda de 0,5 m com ganho linear G = 4, a
- Um sistema de radiodifusão FM representa a informação pela variação da frequência instantâ
- Uma central de enlaces que atribui horários distintos de transmissão a vários usuários imp
- Em uma rede convergente de voz e dados, monitorar perda, latência e jitter é útil para dif
- Um conversor PCM reduz a taxa de bits ao aumentar a quantidade de bits destinados a repres
- O ganho de uma antena expresso em dBi é referido a um irradiador isotrópico ideal, e não a
- Se um analisador vetorial de redes mede S11 praticamente nulo na frequência de operação, a
- Em um enlace de dados com sequências extensas de zeros, a codificação Manchester pode faci
- Um código corretor de erros funciona removendo sistematicamente a redundância de origem; p
- Uma malha de controle de fase pode estabilizar a frequência média de um oscilador de RF, e
- Em um receptor super-heteródino, a filtragem aplicada somente após o misturador distingue 
- Um multiplicador de frequência que utiliza uma não linearidade para gerar harmônicas pode 
- No planejamento de comunicações por satélite, uma estação terrena pode supor que um satéli
- Uma rede óptica pode empregar WDM para transportar portadoras ópticas de diferentes compri
- Para um enlace de rádio que permanece no mesmo percurso e mantém os ganhos das antenas con
- No cálculo preliminar de um enlace óptico monomodo G.652, o responsável deve avaliar tanto
- O Decreto nº 12.595/2025 tornou instantaneamente obrigatória a substituição de todos os tr
- Uma antena com refletor parabólico adequadamente iluminado pode apresentar feixe principal
- Um orçamento de enlace em decibéis deve somar ao ganho de antena transmissora, em dBi, o v
- Ao migrar um sistema de multiplexação temporal para OFDMA, passa-se de uma organização por
- Em um receptor 16-QAM, a igualdade entre a frequência do oscilador local e a da portadora 
```

## Pedido 29: Normativos do CNJ: PDPJ-Br, segurança cibernética e Justiça digital (`legislacao.normativos-cnj-pdpj-transformacao-digital`, 228 tópicos de edital)

```
Matéria: legislacao.normativos-cnj-pdpj-transformacao-digital (Normativos do CNJ: PDPJ-Br, segurança cibernética e Justiça digital)
Capítulos e quantas questões quero em cada um (total 100):
- "1. PDPJ-Br, PJe e governança do processo eletrônico": 23
- "2. Governança técnica, revisão de código e MoReq-Jus": 29
- "3. Juízo 100% Digital, Núcleos 4.0 e audiências remotas": 15
- "4. ENSEC-PJ, proteção da informação e gestão de incidentes": 19
- "5. Automação, jurimetria, IA e limites da Justiça digital": 14

Já existem (não repita):
- Um tribunal pretende compartilhar módulos processuais sem substituir integralmente seu sis
- Uma equipe precisa distinguir a aplicação processual prioritária do ambiente nacional de c
- Após criar um módulo para a plataforma nacional, uma equipe pergunta qual ato disciplina a
- Uma organização mantém documentos processuais em microsserviços e busca identificar a reso
- Durante processo submetido ao Juízo 100% Digital, surge necessidade de realizar presencial
- Uma parte autora deseja distribuir ação a Núcleo de Justiça 4.0 especializado na matéria. 
- Um participante comparece a uma unidade judiciária diferente daquela onde o ato é presidid
- Depois de detectar falhas de proteção de dados, uma unidade quer identificar a norma que i
- Uma ferramenta estatística calcula a mediana de duração das ações de uma classe, sem formu
- Um fornecedor oferece, sem cobrar licença, módulo de processo eletrônico cujo código, docu
- Uma rede de tribunais precisa definir prioridades comuns, responsabilidades locais e inter
- Ao organizar processo eletrônico em vários microsserviços, uma equipe entende que apenas o
- No Juízo 100% Digital, uma parte demandada recebe a primeira notificação de ação trabalhis
- A parte autora opta por um Núcleo de Justiça 4.0, mas o demandado apresenta oposição na pr
- Uma pessoa participa de audiência a partir de sua residência, fora de qualquer unidade jud
- Para reforçar sua segurança, um tribunal pretende distinguir a instância nacional de asses
- Um setor prepara norma interna de IA e encontra referências antigas à Resolução nº 332/202
- Um tribunal deseja contratar infraestrutura de nuvem de pessoa jurídica privada para a PDP
- Na implementação da PDPJ-Br, um ramo especializado dispensa a constituição de comitê gesto
- As partes aderiram ao Juízo 100% Digital e, antes da sentença, concordam em deixar o regim
- Ao designar magistrados para Núcleo de Justiça 4.0, o tribunal abre edital de inscrições e
- Em processo de violência doméstica contra a mulher, a vítima pede depoimento remoto a part
- Um modelo de IA pretende atribuir notas de propensão criminosa a pessoas com base em carac
- Duas soluções de IA são propostas: uma classifica documentos para rotina administrativa; a
- Um usuário autenticado consegue entrar no sistema, mas seu perfil precisa delimitar quais 
- Uma unidade pode incorporar módulo processual compartilhado sem migrar integralmente de se
- Ao publicar um módulo compartilhável da PDPJ-Br, uma equipe pode dispensar autenticação un
- Uma equipe que desenvolve um serviço para a PDPJ-Br deve observar as diretrizes técnicas d
- O tribunal que utiliza sistema de gestão de documentos judiciais criado antes de 2023 pode
- No Juízo 100% Digital, a realização excepcional de perícia presencial, quando inviável sua
- Como o Núcleo de Justiça 4.0 atende a competência territorial ampliada, sua criação pela R
- Um advogado solicita atendimento ao magistrado lotado no Juízo 100% Digital. A Resolução n
- Quando o advogado comparece à sala de uma unidade judiciária diferente daquela que preside
- Na arquitetura institucional da segurança cibernética judicial, a Resolução nº 396/2021 pr
- Um órgão pode equiparar a Portaria CNJ nº 162/2021 à Resolução nº 396/2021, porque a porta
- O levantamento estatístico de tempos de tramitação por classe processual é um exemplo de j
- Uma ferramenta judicial de IA que atribui notas de credibilidade a testemunhos com base em
- A Resolução nº 615/2025 prevê avaliação do grau de risco de soluções de IA pelo tribunal d
- Um tribunal mantém processo eletrônico em plataforma modularizada e decide aplicar os requ
- Se uma parte opta pelo Juízo 100% Digital na distribuição e o feito é sorteado para unidad
- Em Núcleo de Justiça 4.0, a oposição da parte demandada até a primeira manifestação é inca
- Em ação relativa à violência doméstica contra a mulher, a oitiva telepresencial da vítima 
- O art. 29 da Resolução nº 396/2021 trata como equivalentes a autenticação do usuário e a a
- Na Resolução nº 615/2025, a utilização de IA de alto risco está sujeita a auditorias regul
- Ao atualizar regulamento interno de IA em 2026, é correto afirmar que a Resolução nº 674/2
```

## Pedido 30: Federalismo fiscal e tributação (`economia.federalismo-fiscal-tributacao`, 227 tópicos de edital)

```
Matéria: economia.federalismo-fiscal-tributacao (Federalismo fiscal e tributação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Finanças públicas, funções econômicas e federalismo fiscal": 17
- "2. Transferências intergovernamentais, equalização e estrutura tributária": 25
- "3. Impostos, incidência econômica e distribuição do ônus": 26
- "4. Eficiência, excedentes, peso morto e regra de Ramsey": 15
- "5. Progressividade, decisões privadas, patrimônio e trade-offs": 17

Já existem (não repita):
- Uma cidade quer financiar iluminação de ruas, disponível mesmo a quem não contribui indivi
- Dois municípios do mesmo estado possuem capacidade arrecadatória muito diferente, embora d
- Uma transferência financeira faz o repasse do governo central aumentar conforme a despesa 
- Uma lei hipotética determina tributo de 8 unidades monetárias por litro comercializado, in
- Uma empresa recolhe formalmente imposto sobre vendas, mas eleva preço e reduz a margem, re
- Em mercado competitivo simples, a demanda de um produto é muito pouco sensível ao preço, e
- Uma reforma hipotética deixa todas as pessoas com a mesma alíquota média de 12% sobre a re
- Em modelo competitivo sem externalidades, um imposto reduz transações mutuamente vantajosa
- Uma política pública prioriza que contribuintes com maior capacidade econômica assumam mai
- Um governo local oferece coleta urbana com baixo custo por morador, enquanto serviço regio
- Um município recebe repasse constitucional obrigatório, destinado a certa finalidade públi
- No modelo simplificado de IVA por crédito, uma empresa compra insumo por 100 e vende produ
- Antes de um imposto específico, o preço competitivo era 30; depois o consumidor paga 34 e 
- No modelo competitivo, as elasticidades da oferta e da demanda em módulo são 3 e 1, respec
- Em curvas lineares, imposto unitário de 10 reduz transações de 100 para 80 unidades. Qual 
- Dois contribuintes possuem rendas de 2.000 e 8.000 e recolhem 100 e 800, respectivamente. 
- Uma pessoa recebe salário líquido menor após aumento tributário e cogita trabalhar mais ho
- Um estudo assume famílias plenamente móveis, informadas sobre serviços e tributos locais e
- Um município rico recebe transferências proporcionais à arrecadação efetuada em seu territ
- Um monopolista enfrenta imposto específico por unidade vendida. A equipe quer estimar repa
- Sob hipóteses simplificadoras de tributação de bens independentes, a política busca arreca
- Um imóvel comercial tem fluxo esperado de renda constante, mas passa a enfrentar tributo a
- Uma fábrica emite poluente cujo dano marginal não se reflete no preço privado; o governo i
- Duas alocações são comparadas: na primeira, há enorme desigualdade, mas nenhuma mudança di
- Uma análise identifica que o tributo sobre consumo pesa mais na renda anual de quem poupa 
- Uma região dispõe de municípios com preferências diferentes por serviços locais. A descent
- Dois municípios possuem capacidade fiscal diferente; quando o governo central lhes transfe
- Uma cobrança hipotética de 5 unidades monetárias por pacote vendido é específica; outra de
- Em um mercado competitivo com demanda inelástica e oferta bastante elástica, os vendedores
- Num mercado sem externalidades, um tributo que reduz transações abaixo do volume eficiente
- Um imposto com alíquota média de 10% para pessoas de renda baixa e de 5% para pessoas de r
- A Constituição prevê repartição de receitas e há divulgação oficial de repasses como FPE e
- Em um sistema de transferências, a vinculação do recurso a educação é suficiente para clas
- No IVA não cumulativo de fatura-crédito, um produtor com débito de saída de 60 e crédito r
- Uma mercadoria possui imposto seletivo calculado como 12% de seu preço. A expressão excise
- No modelo linear, um imposto de 4 por unidade faz a quantidade vendida cair de 60 para 50.
- Duas opções de tributação produzem a mesma arrecadação. Nesse caso, ambas necessariamente 
- No modelo de dois períodos, um imposto que diminui retorno líquido da poupança altera o pr
- A regra simplificada de Ramsey determina alíquotas relativamente maiores para os bens de d
- Dois tributos sobre consumo podem produzir classificações distributivas distintas quando a
- Se um monopolista paga imposto específico sobre vendas, a incidência econômica segue neces
- Se a oferta física de terra de uma região é fixa, imposto recorrente que reduza sua renda 
- A seleção territorial do modelo de Tiebout prova que a competição entre municípios produz 
- Em projeto regional cujo benefício se espalha para outros municípios, uma transferência de
- Um imposto pigouviano adequado à externalidade ambiental é necessariamente ineficiente seg
```

## Pedido 31: Java EE/Jakarta EE e Spring (`informatica.java-ee-spring`, 227 tópicos de edital)

```
Matéria: informatica.java-ee-spring (Java EE/Jakarta EE e Spring)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Plataforma Java EE/Jakarta EE e arquitetura de aplicações web": 15
- "2. Servlets, JSP, JSF, Ajax e JBoss Seam": 19
- "3. Persistência: JDBC, JPA, Hibernate, Envers e Flyway": 17
- "4. Spring Framework e Spring Boot": 22
- "5. Spring Cloud, Eureka, Zuul, MapStruct, Swagger e Maven": 27

Já existem (não repita):
- Uma equipe mantém uma aplicação web Java que precisa executar servlets e aproveitar recurs
- Um projeto foi construído especificamente sobre Jakarta EE 8 e utiliza a API de persistênc
- Em uma aplicação de consultas de pedidos, uma classe recebe HTTP, outra verifica descontos
- Um servlet de consulta foi mapeado para `/consulta` e atende milhares de chamadas GET após
- Uma tela institucional usa páginas JSP para exibir uma listagem construída no servidor. Qu
- Uma interface de pesquisa deve mostrar sugestões de nomes enquanto a pessoa digita, preser
- Após validar um formulário, o servlet precisa apresentar uma JSP de resultado usando os at
- Um sistema antigo utiliza JBoss Seam 2 para manter dados de um formulário distribuído em t
- Uma biblioteca Java construída com Maven precisa ser utilizada como dependência por outros
- Uma aplicação `WAR` será implantada em contêiner externo que já fornece a API Servlet. No 
- Um endpoint JDBC recebe um identificador informado pelo usuário e precisa consultar `SELEC
- Uma entidade `Cliente` foi carregada por um `EntityManager` já encerrado, sofreu alteraçõe
- Uma transação JPA insere um registro e chama `em.flush()`; logo depois, uma falha de regra
- Um módulo antigo é descrito como baseado em JPA 2.0, enquanto outro segue Jakarta EE 8. Qu
- Uma equipe seleciona Hibernate ORM para implementar persistência JPA, preservando o uso da
- Um requisito pede consultar o valor anterior de um campo de entidade `Contrato` em determi
- Um script Flyway `V3__adicionar_indice.sql` já foi aplicado em produção. Alguém altera seu
- Uma consulta JPA retorna quarenta registros `Pedido`; em seguida, a apresentação acessa um
- Há duas implementações Spring de `Notificador`, ambas registradas como beans; `AlertaServi
- Em configuração Spring padrão baseada em proxy, um método público `processar()` de bean Sp
- Uma aplicação Spring Boot tem auto-configuração de `DataSource` disponível no classpath. D
- Um singleton Spring `PedidoService` recebe no construtor um bean declarado como `prototype
- Uma classe Spring MVC expõe `@GetMapping("/itens/{id}")` e devolve um objeto `ItemDTO` dir
- No `application.yml` de um cliente Eureka, o endereço do registro deve ser `http://localho
- Em uma aplicação legada Spring Cloud Netflix, a configuração `zuul.routes.users: /usuarios
- Um `HttpServlet` mantém o último identificador consultado em campo mutável de instância. C
- Numa tela JSF, falha de conversão do valor digitado dispensa a fase de validação e faz o f
- Uma página JSP pode usar Expression Language para apresentar a propriedade `nome` de um ob
- Uma busca incremental feita com Ajax exige comunicação WebSocket, pois requisições HTTP in
- Ao executar `mvn package` em projeto Maven convencional, as fases precedentes pertinentes 
- Uma entidade de faturamento recebeu `@Version` na JPA. A anotação passa então a fornecer, 
- Em um projeto Flyway, `R__recriar_views.sql` pode ser executado novamente quando seu check
- Um projeto Spring Boot com dependências web deve necessariamente ser empacotado como WAR e
- Ao usar `@Mapper(componentModel = "spring")` em um mapper MapStruct e configurar o process
- Ao exibir uma definição OpenAPI com Swagger UI, a ferramenta passa a implementar os serviç
- Em aplicação Spring que utiliza gestão transacional declarativa padrão, uma `RuntimeExcept
- Uma classe Java anotada com CDI `@Inject` é gerenciada automaticamente pelo mesmo contêine
- Um cliente Eureka registra o nome lógico do serviço, host e porta no servidor de descobert
- O MapStruct evita o problema de inicialização lazy do Hibernate porque o mapper gerado sub
- De acordo com a API JPA 2.2, chamar `EntityManager.remove()` diretamente com uma instância
- A dependência Maven declarada com escopo `provided` é propagada como dependência transitiv
- Uma aplicação legada de Jakarta EE 8 pode utilizar `javax.servlet.http.HttpServlet` e `jav
- O escopo padrão `singleton` de um bean Spring assegura uma única instância dessa classe na
- Ao analisar uma configuração histórica com `@EnableZuulProxy` e rotas por `serviceId`, é a
- Em `pom.xml`, declarar a versão de uma biblioteca apenas na seção `dependencyManagement` f
```

## Pedido 32: Pneumologia (`medicina.pneumologia`, 225 tópicos de edital)

```
Matéria: medicina.pneumologia (Pneumologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Abordagem pneumológica, semiologia e métodos diagnósticos": 17
- "Asma brônquica: diagnóstico, controle e exacerbações": 17
- "DPOC, prevenção e tratamento do tabagismo": 17
- "Pneumonias e tuberculose pulmonar": 17
- "Doenças pleurais e tromboembolismo pulmonar": 16
- "Doenças intersticiais, neoplasias, insuficiência respiratória e sono": 16

Já existem (não repita):
- Em adulto com dispneia crônica, qual parâmetro espirométrico expressa diretamente a propor
- Uma pessoa apresenta baixa CVF e relação VEF1/CVF preservada. Qual exame confirma se exist
- Em avaliação de dispneia de início súbito e suspeita de TEP, qual afirmação sobre a radiog
- Um adulto apresenta sibilos episódicos, piora noturna e espirometria normal no dia da cons
- Segundo o manejo contemporâneo da asma do adulto, qual estratégia deve ser evitada como tr
- Qual das seguintes situações caracteriza sinal de gravidade em exacerbação de asma?
- Paciente com asma persistente recebe LABA sem corticoide inalatório por conta própria. Qua
- Na suspeita clínica de DPOC, qual resultado é compatível com a definição espirométrica clá
- Em doente com DPOC e hipoxemia durante exacerbação, sob risco de hipercapnia, qual alvo in
- Homem com DPOC sofre exacerbação, apresenta pH 7,28 e PaCO₂ 68 mmHg, consciente e cooperat
- Como se calcula a carga tabágica de quem fuma dois maços de cigarro ao dia há 15 anos?
- Qual medida possui papel central na redução de risco e na abordagem longitudinal de fumant
- Em paciente internado com suspeita de pneumonia adquirida na comunidade, hipotensão e nece
- Qual achado indica pneumonia adquirida na comunidade de maior gravidade, mesmo quando o es
- Paciente com tosse persistente, perda de peso e sudorese noturna procura unidade básica. N
- Qual é o esquema básico usual para tuberculose pulmonar sensível em adulto elegível?
- Em derrame pleural, qual conjunto isolado de resultados preenche um critério de Light para
- No derrame pleural unilateral de causa incerta, qual benefício específico apresenta a ultr
- Paciente com dispneia intensa e hipotensão após trauma torácico apresenta assimetria auscu
- Em pessoa estável com baixa probabilidade clínica pré-teste de TEP, qual resultado de exam
- Para TEP de alto risco com instabilidade hemodinâmica, qual opção pode estar indicada após
- Paciente com dispneia progressiva, tosse seca, estertores finos e TCAR com faveolamento ba
- Em investigação de possível câncer de pulmão, qual procedimento em geral fornece confirmaç
- Qual situação melhor caracteriza insuficiência respiratória hipercápnica?
- Qual exame é referência na investigação e caracterização da apneia obstrutiva do sono?
- A oximetria de pulso permite medir diretamente a PaCO₂ e, por isso, substitui a gasometria
- A presença de sibilância é sugestiva de obstrução de vias aéreas, mas a sua ausência não e
- Uma resposta negativa à prova broncodilatadora realizada em um único dia elimina definitiv
- Na avaliação do controle da asma, é necessário considerar tanto os sintomas atuais quanto 
- Antibióticos devem ser empregados rotineiramente em todas as exacerbações de asma, mesmo s
- Antes de aumentar etapas do tratamento da asma mal controlada, devem ser verificadas técni
- A demonstração de reversibilidade broncodilatadora exclui a DPOC e estabelece, isoladament
- O corticoide inalatório é obrigatório para todas as pessoas com DPOC, sem considerar eosin
- A reabilitação pulmonar pode ser componente importante do tratamento não farmacológico na 
- A exacerbação de DPOC pode ser desencadeada por viroses ou irritantes e ainda exige avalia
- Pneumonia comunitária em idoso pode ocorrer sem febre expressiva e apresentar alteração do
- Os escores CURB-65 e PSI substituem a avaliação de choque, necessidade de ventilação e oxi
- Prova tuberculínica ou IGRA detecta resposta imunológica ao bacilo, mas não distingue sozi
- O teste rápido molecular negativo em escarro exclui tuberculose pulmonar mesmo quando a pr
- Um derrame pleural é classificado como exsudato pelos critérios de Light quando pelo menos
- A diureticoterapia em paciente com insuficiência cardíaca pode concentrar constituintes do
- O tratamento de derrame pleural maligno sintomático recorrente é sempre pleurodese, mesmo 
- Em paciente estável com suspeita de TEP, D-dímero positivo isolado confirma o diagnóstico 
- Questionários como STOP-Bang servem para estimar risco de apneia obstrutiva do sono, não p
- Na fibrose pulmonar idiopática, a presença de sintomas e TC compatível autoriza corticoide
```

## Pedido 33: Acidente do trabalho (`direito-trabalho.acidente-trabalho`, 222 tópicos de edital)

```
Matéria: direito-trabalho.acidente-trabalho (Acidente do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito legal, equiparações e doenças ocupacionais": 29
- "2. CAT, investigação, conceito técnico e indicadores": 16
- "3. Prevenção, gestão de riscos e saúde do trabalhador": 23
- "4. Seguro de acidentes, RAT/FAP, benefícios e estabilidade": 17
- "5. Responsabilidade civil, indenizações e competência judicial": 15

Já existem (não repita):
- Durante a operação de um equipamento, uma trabalhadora sofre fratura que a incapacita temp
- Um trabalhador apresenta silicose associada à inalação de sílica no exercício da atividade
- Após um acidente fatal durante serviço, a empresa informa que fará a CAT apenas no primeir
- Empregado com condição clínica preexistente sofre agravamento incapacitante por esforço la
- Em oficina, há risco de contato com lâmina desprotegida. A organização dispõe de barreira 
- Uma unidade acumulou 250.000 horas-homem de exposição ao risco e registrou cinco acidentes
- Uma atividade econômica tem RAT básico de 2% e FAP igual a 1,5, conforme o enquadramento a
- Após receber benefício por incapacidade temporária acidentária, empregado retorna ao servi
- Empregado pede indenização de dano material causado por acidente ocupacional diretamente c
- Uma enfermidade não consta de lista administrativa de doenças ocupacionais, mas investigaç
- Após lesão ocupacional, o empregador se recusa a emitir CAT. O trabalhador procura médico 
- Uma unidade acumulou 100.000 HHT, com 25 dias perdidos e 15 dias debitados por acidentes r
- Um laudo previdenciário analisa correlação estatística entre códigos CID de uma doença e C
- Uma comissão interna registra riscos percebidos por trabalhadores, acompanha medidas e aux
- Uma empresa enquadrada em RAT básico de 3% recebe FAP de 0,5000. Qual alíquota ajustada de
- Uma empregada foi dispensada sem ter recebido B91. Após o fim do contrato, perícia reconhe
- Após acidente, perícia confirma sequela permanente que reduz a capacidade profissional do 
- Em atividade de transporte de valores, empregado sofre lesão durante ataque cuja exposição
- Durante viagem para executar ordem da empresa, fora do estabelecimento e do horário habitu
- Duas operações têm quatro acidentes com afastamento cada, mas a primeira acumula 200.000 H
- A investigação de corte em máquina identifica que a proteção foi retirada para cumprir met
- Uma avaliação associa CID de tendinopatia ao CNAE do estabelecimento, e o perito considera
- Empregado recupera capacidade de trabalhar após acidente, mas permanece com redução funcio
- A mesma pessoa propõe duas ações em razão de acidente: numa pede reparação civil do empreg
- Uma companhia sofreu queda no número bruto de acidentes após dobrar horas de trabalho, enq
- Uma trabalhadora tem lesão que reduziu temporariamente sua capacidade durante atividade a 
- Após um acidente ocupacional que exige atendimento médico, o empregado retorna ao serviço 
- Uma unidade substitui produto tóxico por outro menos perigoso e mantém ventilação coletiva
- Uma empresa apresenta RAT básico de 3% e FAP de 0,5. Sua contribuição ajustada, quanto a e
- Após cessação de benefício por incapacidade temporária acidentária de empregado, o art. 11
- Em ação indenizatória de acidente laboral movida por empregado contra empregador, a compet
- Uma trabalhadora desenvolve enfermidade agravada pela organização do trabalho, e exames de
- Um trabalhador sofreu corte com redução temporária da capacidade, e o empregador não emiti
- Uma operação registra três acidentes com afastamento em 150.000 horas-homem, mantendo crit
- Uma comissão interna participa do levantamento de riscos e acompanha plano preventivo. Pel
- Após lesão acidentária, consolida-se sequela que reduz a capacidade laboral habitual, mas 
- Doença ocupacional foi reconhecida por perícia judicial apenas após dispensa, com nexo con
- Em atividade normalmente exercida com exposição habitual a risco especial superior ao impo
- Num relatório sobre trabalho rural, uma doença endêmica foi adquirida em região de circula
- Um empregador reconhece que retirou resguardo de máquina para acelerar a operação. A inves
- Em uma unidade com 200.000 HHT, quatro acidentes com afastamento e 60 dias computados, a T
- Uma empresa mantém CAT e registros clínicos de acidentes, mas os dados oficiais de FAP inc
- Uma empresa realiza atividade de risco especial habitual e indeniza dano comprovadamente l
- Em acidente que causou incapacidade laboral, a vítima pede contra o empregador despesas de
- Uma ação pede contra o INSS concessão de benefício decorrente de acidente laboral. O fato 
```

## Pedido 34: Biblioteconomia: serviço de referência, usuários e fontes de informação (`biblioteconomia.servicos-referencia-fontes-informacao`, 222 tópicos de edital)

```
Matéria: biblioteconomia.servicos-referencia-fontes-informacao (Biblioteconomia: serviço de referência, usuários e fontes de informação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Gestão da informação, do conhecimento e fundamentos da biblioteconomia": 20
- "2. Serviço e entrevista de referência: atendimento e mediação": 19
- "3. Estudos de usuários, formação, circulação e disseminação": 21
- "4. Fontes de informação, obras de referência e documentação jurídica": 23
- "5. Pesquisa jurídica avançada e avaliação integrada dos serviços": 17

Já existem (não repita):
- Uma unidade organiza registros de consulta em um repositório e, paralelamente, realiza enc
- Um espaço de leitura mantém coleção valiosa em estantes inacessíveis, embora existam meios
- Em um levantamento de necessidades, percebe-se que públicos distintos procuram livros adeq
- Uma coleção especializada contém obras pouco consultadas porque seus temas não são encontr
- Após reorganizar sinalização e instrumentos de busca, o tempo necessário para encontrar um
- Durante atendimento remoto, uma pessoa solicita 'material sobre publicação judicial', sem 
- Depois de descobrir que uma pessoa deseja consultar uma norma específica, o atendimento pr
- Uma pessoa solicita a íntegra de um ato normativo específico. Para confirmar o teor origin
- Em busca por aviso federal de contratação publicado oficialmente, a pessoa dispõe de data 
- Um diagnóstico revela baixa frequência de moradores ao serviço, mas foram entrevistados ap
- Um serviço envia atualizações apenas a pessoas que indicaram interesse por legislação ambi
- Uma pessoa tem um livro emprestado e pede extensão do prazo; outra aguarda um exemplar ind
- Um ato aparece como 'disponibilizado' em um diário judicial eletrônico numa segunda-feira 
- Uma pessoa quer acompanhar publicações judiciais e receber comunicações que, por exigência
- Para estatísticas de assuntos mais procurados, uma biblioteca planeja guardar nome complet
- Ao construir um roteiro de pesquisa, uma pessoa encontra base que fornece autor, título, d
- Após uma oficina de bases jurídicas, participantes conseguem localizar documentos, mas não
- Uma pessoa encontra no LexML a referência de um ato cujo teor será citado em pesquisa. Qua
- Uma base contém 40 documentos relevantes para certo tema. A estratégia recupera 20 registr
- Uma pesquisa solicita o texto aplicável a um fato ocorrido anos antes de várias alterações
- Uma estratégia de busca usa 'acessibilidade AND contratos', mas perde documentos que empre
- Um serviço pesquisa somente pessoas que fizeram empréstimo no último mês e conclui que nen
- No atendimento de uma pesquisa sobre 'remoção de conteúdo', o pedido inicial é amplo e o t
- Um serviço de alertas inicia com perfil 'direito ambiental', mas envia continuamente docum
- Uma unidade divulga que dobrou o número de atendimentos e, por isso, declara ter duplicado
- Uma pessoa pede ajuda para localizar documentos, e o atendimento reformula o pedido com su
- Ao divulgar o catálogo, a equipe aplica a segunda lei de Ranganathan especificamente porqu
- Um estudo que entrevista pessoas que nunca utilizaram a biblioteca pode descobrir barreira
- O envio semanal do mesmo boletim geral para toda a lista de contatos constitui, por si só,
- Um catálogo descreve os documentos de determinado acervo, enquanto uma bibliografia pode r
- Para identificar o texto original de uma lei, uma reportagem que comenta seu impacto é con
- Em uma oficina, o aprendizado de critérios para verificar autoria, atualidade e pertinênci
- Uma pessoa procura edital judicial no DOU e recebe a orientação de que, por sua natureza, 
- Um ato foi disponibilizado em DJe numa terça-feira útil, sem feriados na semana. Pela regr
- Uma biblioteca pretende manter histórico nominal de consultas porque os dados foram inseri
- Um estudo combina registro do volume mensal de consultas com entrevistas sobre dificuldade
- Uma pesquisa recuperou 15 documentos, 12 dos quais são pertinentes. Por esse resultado, po
- Na organização da circulação, uma reserva pode colocar o usuário na fila de acesso a exemp
- Uma pesquisadora precisa demonstrar a redação de um dispositivo numa data histórica. Ao en
- Em base com 30 documentos pertinentes, uma consulta recupera 12 registros, nove relevantes
- Uma equipe formaliza em manual as experiências de atendimento obtidas em conversas e demon
- Uma pessoa procura jurisprudência sobre duas expressões sinônimas e restringe os resultado
- Dados pessoais obtidos em consultas e disponibilizados publicamente podem ser reutilizados
- Na consulta sobre uma comunicação judicial, afirma-se que a publicação no DJEN necessariam
- Um serviço encontra um precedente por meio de índice referencial e, antes de apresentá-lo 
```

## Pedido 35: SQL avançado (`informatica.sql-avancado`, 221 tópicos de edital)

```
Matéria: informatica.sql-avancado (SQL avançado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Dependências funcionais, formas normais e desnormalização": 16
- "2. Visões, índices e estruturas de acesso": 17
- "3. Triggers, funções, procedimentos e linguagens procedurais": 24
- "4. Transações, isolamento, bloqueios e anomalias de concorrência": 14
- "5. Tuning, planos de execução, estatísticas e diagnóstico sistêmico": 29

Já existem (não repita):
- Uma aplicação grava três telefones de cada cliente em uma única coluna textual, separados 
- Em ItemPedido(pedido, produto, nome_produto, quantidade), a chave candidata é (pedido, pro
- Uma tabela Empregado(id, setor_id, setor_nome) tem id como chave e as dependências id → se
- Em Alocacao(aluno, disciplina, docente), há chaves candidatas (aluno,disciplina) e (aluno,
- Um painel lê centenas de vezes por minuto o mesmo resumo de vendas, que pode ficar alguns 
- Uma view simples do PostgreSQL filtra pedidos de estado 'ABERTO' e admite alterações autom
- Uma tabela grande é frequentemente consultada por um intervalo de datas e os resultados de
- Um índice B-tree composto foi criado em (cliente_id, data_pedido). A consulta filtra igual
- Uma instrução UPDATE modifica 40 linhas e existe trigger FOR EACH ROW para registrar a mud
- Uma view une informações de duas tabelas e uma atualização dirigida a ela precisa ser dist
- Em um trigger T-SQL do SQL Server, um único UPDATE muda várias linhas e a lógica deve exam
- Um exemplo Oracle PL/SQL declara variável, executa comandos e trata uma exceção. Qual sequ
- Em PL/SQL, um SELECT INTO destinado a preencher uma variável escalar encontra mais de uma 
- Um programa PL/SQL precisa percorrer resultado com cursor explícito e atualizar informação
- Uma aplicação Oracle organiza rotinas relacionadas e precisa expor interface estável enqua
- Uma transação executa alteração A, estabelece SAVEPOINT s, executa alteração B, usa ROLLBA
- Duas transações em READ COMMITTED realizam leituras da mesma linha em momentos distintos. 
- Uma aplicação PostgreSQL utiliza REPEATABLE READ e duas transações concorrentes modificam 
- Uma equipe pretende comparar cardinalidade estimada e linhas reais processadas por uma con
- O plano de consulta estima 20 linhas numa junção, mas EXPLAIN ANALYZE mostra 200 mil linha
- Um índice PostgreSQL B-tree composto contém (regiao, cidade). Uma consulta filtra somente 
- Uma transação mantém bloqueio de registro A e aguarda B; outra mantém B e aguarda A. Nenhu
- Uma tabela recebe centenas de milhares de atualizações por hora. Após criar cinco índices 
- Um papel perdeu permissão SELECT diretamente concedida a ele, mas continua lendo a tabela 
- Uma base particionada por mês continua lenta ao consultar períodos de muitos anos, enquant
- Uma relação tem duas chaves candidatas e atende à 3FN formal; ainda assim, pode violar FNB
- Em uma tabela de matrícula, verifica-se que aluno_id e disciplina_id juntos determinam not
- Um resumo persistido em view materializada PostgreSQL pode refletir dados anteriores ao es
- Uma consulta realiza buscas por intervalo entre datas. Um índice hash é a estrutura geralm
- Um UPDATE altera vinte registros em tabela com trigger de atualização definido como FOR EA
- Um trigger AFTER UPDATE registra auditoria na mesma transação. Por executar depois do UPDA
- Um bloco anônimo PL/SQL declara uma variável com DECLARE, executa instruções entre BEGIN e
- Para tratar um erro no SQL Server, um procedimento T-SQL deve escrever EXCEPTION WHEN OTHE
- Um cursor explícito PL/SQL pode percorrer registros com OPEN, sucessivos FETCH e CLOSE; qu
- Uma transação altera um registro, marca SAVEPOINT s e em seguida altera outro. Ao executar
- Duas consultas realizadas pela mesma transação READ COMMITTED podem observar versões confi
- No PostgreSQL, definir READ UNCOMMITTED permite ler livremente linhas modificadas por outr
- Ao analisar consulta lenta, o responsável encontra no PostgreSQL custo estimado de 500 e t
- Em PostgreSQL, EXPLAIN ANALYZE aplicado diretamente a uma instrução UPDATE apenas simula o
- Depois de inserir milhões de registros com distribuição bastante diferente da anterior, a 
- Uma tabela com milhões de linhas recebe um índice único na coluna identificadora. A partir
- Duas transações verificam que pelo menos uma pessoa permanecerá em plantão e depois retira
- Uma permissão SELECT foi retirada de determinado papel por REVOKE, mas continua concedida 
- Para usar REFRESH MATERIALIZED VIEW CONCURRENTLY no PostgreSQL, além de a visão estar popu
- Uma aplicação apresenta lentidão por fila de aquisição de conexões. O aumento ilimitado do
```

## Pedido 36: Switching, roteamento e redes WAN (`informatica.switching-roteamento-wan`, 221 tópicos de edital)

```
Matéria: informatica.switching-roteamento-wan (Switching, roteamento e redes WAN)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ethernet, switching, VLAN e padrões IEEE": 18
- "2. Prevenção de loops, controle de acesso e segurança da LAN": 17
- "3. Roteamento de camada 3: OSPF e BGP": 17
- "4. Redes WAN: Frame Relay, ATM, MPLS e SD-WAN": 24
- "5. QoS, VoIP, VPN, gerenciamento e integração de serviços": 24

Já existem (não repita):
- 1. Um switch recebe na VLAN 30 um quadro unicast cujo MAC de destino ainda não está em sua
- 2. Dois segmentos de usuários devem continuar separados em domínios de broadcast, mas prec
- 3. Em uma captura de quadro Ethernet com marcação IEEE 802.1Q, qual estrutura de campos é 
- 4. Um inventário lista tecnologias de Ethernet. Qual associação entre emenda histórica IEE
- 5. Uma topologia de switches com links redundantes passou a apresentar quadros duplicados 
- 6. Uma rede adota IEEE 802.1X para negar acesso normal à LAN antes da validação de credenc
- 7. Um roteador recebe informações de topologia em LSAs, calcula caminhos pelo SPF e integr
- 8. Para trocar rotas entre sistemas autônomos, dois roteadores mantêm sessão BGP-4. Qual t
- 9. Um ambiente possui 60 VLANs e pretende distribuir grupos delas por caminhos físicos red
- 10. Um enlace PoE segue IEEE 802.3af. Um candidato interpreta a potência de 15,4 W como en
- 11. Duas rotas OSPF para o mesmo prefixo e mesma origem têm custos totais 18 e 27. Sem out
- 12. Um pacote destina-se a 192.0.2.130. A tabela de um roteador contém 192.0.2.0/24 e 192.
- 13. Ao contratar uma WAN legada, o requisito especifica células de tamanho fixo de 53 byte
- 14. Um roteador MPLS ingress recebe pacote IP sem rótulo e o insere em um LSP. Qual operaç
- 15. Uma VPN IP sobre MPLS permite a dois clientes usar o mesmo bloco IPv4 privado sem mist
- 16. Um fluxo de voz apresenta rajadas superiores ao contrato de saída. Deseja-se atrasar o
- 17. Em um projeto SD-WAN, o serviço overlay opera sobre dois acessos de provedores diferen
- 18. Uma rede recebe pacotes IPv4 com marca DSCP 46 em um domínio DiffServ. Qual interpreta
- 19. Dois caminhos BGP alcançam o mesmo prefixo. Um tem AS_PATH menor, mas o outro possui L
- 20. Um roteador de borda VPN MPLS separa VRFs de clientes, mas os enlaces de transporte pa
- 21. Um aplicativo remoto acessado via portal WebVPN autentica usuários em serviço LDAP. Qu
- 22. Em uma WAN Frame Relay, um circuito virtual apresenta DLCI 104 e CIR de 512 kb/s. Que 
- 23. Uma equipe usa SNMP para verificar erros de interface, mas uma videoconferência contin
- 24. Dois switches oferecem redundância; um está na revisão de redes em que RSTP agrupa est
- 25. Um equipamento anuncia prioridade 802.1p num trunk e outro classifica pacotes IP por D
- 26. Ao receber quadro de um host, um switch aprende o endereço MAC de origem e a porta de 
- 27. Um enlace trunk IEEE 802.1Q encaminha VLANs diferentes por meio de uma tag que substit
- 28. IEEE 802.1X foi concebido para calcular a árvore livre de ciclos entre switches e defi
- 29. OSPF organiza informações de estado de enlace, dissemina LSAs e executa o cálculo SPF 
- 30. Uma rede ATM transporta informação em células de 53 bytes, formadas por cabeçalho de 5
- 31. Uma rede MPLS L3VPN fornece necessariamente criptografia de ponta a ponta porque a apl
- 32. Em uma solução de qualidade de serviço, jitter expressa variação no atraso de entrega 
- 33. Em uma rede com STP clássico, a root bridge é eleita somente pelo menor endereço MAC, 
- 34. OSPF utiliza a área 0 como backbone e emprega ABRs para interligar áreas, o que favore
- 35. Em BGP-4, o atributo MED corresponde à medição contínua de latência do enlace e determ
- 36. Em um núcleo MPLS, o LSR pode decidir o encaminhamento com base no rótulo do topo da p
- 37. A implementação de SD-WAN dispensa a contratação e o funcionamento de links físicos po
- 38. SNMPv3 e LDAP desempenham a mesma função: ambos criam túneis VPN IPsec e distribuem ch
- 39. Um dispositivo usa VLAN ID 0 em um quadro 802.1Q. Isso é compatível com a indicação de
- 40. RSTP, associado historicamente à emenda IEEE 802.1w, mantém obrigatoriamente os cinco 
- 41. Em uma LAN Ethernet com hosts em portas full-duplex comutadas, a detecção de colisões 
- 42. Uma solução OSPFv3 mantém conceitos de áreas, disseminação de LSAs e cálculo SPF, mas 
- 43. Uma política SD-WAN pode preferir enlace de menor jitter para VoIP mesmo quando outro 
- 44. Ao reclassificar pacotes IP em uma fronteira DiffServ, o equipamento altera o campo PC
- 45. Em SNMPv3, autenticação e privacidade dependem do nível de segurança e da configuração
- O LACP, padronizado pelo IEEE, negocia automaticamente a agregação de várias portas física
- O protocolo que negocia dinamicamente a agregação de enlaces Ethernet entre dois switches 
```

## Pedido 37: Anestesiologia: avaliação pré-anestésica, anestesia geral e regional e via aérea (`medicina.anestesiologia`, 221 tópicos de edital)

```
Matéria: medicina.anestesiologia (Anestesiologia: avaliação pré-anestésica, anestesia geral e regional e via aérea)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação pré-anestésica, classificação ASA e jejum": 17
- "Farmacologia anestésica e anestesia geral": 17
- "Anestesia regional, técnicas locais e segurança": 17
- "Via aérea difícil, intubação e monitorização": 17
- "Anestesia pediátrica, obstétrica e geriátrica": 16
- "Complicações, recuperação e dor pós-operatória": 16

Já existem (não repita):
- Durante a consulta pré-anestésica, paciente com doença sistêmica grave, porém sem ameaça c
- A classificação ASA é definida exclusivamente pelo porte da operação e não leva em conta d
- Uma paciente precisa de procedimento de emergência e recebe registro ASA II E. A letra E s
- A presença do sufixo E na classificação ASA informa caráter emergencial da operação, sem c
- Adulto saudável está agendado para procedimento eletivo e pergunta sobre jejum. Consideran
- Um período adequado de jejum pré-operatório elimina totalmente a possibilidade de broncoas
- Paciente sem sintomas relevantes será submetido a cirurgia pequena. Qual princípio deve or
- A ausência de indicação clínica para exames laboratoriais pode justificar não solicitar ro
- Um paciente em uso de fármaco que pode retardar esvaziamento gástrico relata plenitude abd
- Bloqueadores neuromusculares podem produzir paralisia motora sem analgesia ou inconsciênci
- Na indução de anestesia geral com propofol, qual efeito NÃO deve ser presumido como compon
- O propofol tem ação analgésica suficiente para garantir, sozinho, anestesia de cirurgia do
- Dois anestésicos inalatórios são comparados por seus valores de concentração alveolar míni
- Uma razão quantitativa train-of-four inferior a 0,9 pode indicar recuperação neuromuscular
- Após receber rocurônio durante anestesia geral, o paciente fica imóvel. Qual afirmação é c
- O sugamadex é um antagonista seletivo habitual do cisatracúrio e, por isso, deve ser usado
- Ao final de cirurgia com remifentanil em infusão, qual plano reduz o risco de dor intensa 
- Na raquianestesia, o anestésico é administrado no espaço subaracnoideo; na técnica peridur
- Qual combinação de bloqueador neuromuscular e agente de reversão representa uma associação
- Uso de ultrassonografia durante bloqueio periférico exclui totalmente a possibilidade de t
- Na comparação entre anestesia raquidiana e peridural, qual proposição anatômica está corre
- Anticoagulantes diferentes sempre admitem o mesmo intervalo de suspensão para punção neuro
- O principal mecanismo farmacológico dos anestésicos locais usados em infiltração e bloquei
- Sinais de LAST podem começar diretamente por comprometimento cardiovascular, sem sintomas 
- Após bloqueio do plexo, paciente apresenta zumbido, convulsão e arritmia com instabilidade
- A capnografia substitui completamente a oximetria de pulso porque ambas medem a saturação 
- Paciente usa anticoagulante oral e necessita de bloqueio neuroaxial. Qual orientação é a m
- Mesmo com Mallampati aparentemente favorável, outros achados e antecedentes podem indicar 
- Durante infiltração extensa com anestésico local, qual conduta contribui para reduzir risc
- Na monitorização neurofisiológica intraoperatória, alterações de MEP devem ser interpretad
- Após introdução de tubo traqueal em paciente ventilado, qual recurso é essencial para conf
- Lactentes podem dessaturar mais rapidamente durante apneia devido à menor reserva de oxigê
- Em qual conjunto de circunstâncias a equipe deve considerar fortemente uma estratégia de i
- A analgesia neuroaxial de parto só é eticamente justificável depois que a parturiente alca
- Durante cirurgia de coluna com monitorização de potenciais evocados motores (MEP), a equip
- Na pessoa idosa, a fragilidade e a redução de reserva fisiológica podem influenciar titula
- Parturiente deseja analgesia neuroaxial durante trabalho de parto, sem contraindicação clí
- Na hipertermia maligna, o aumento inesperado de CO₂ expirado pode surgir antes da elevação
- Durante anestesia com agente volátil, ocorre aumento abrupto e inexplicado de ETCO₂, taqui
- A pontuação favorável em escala de Aldrete dispensa avaliação clínica da via aérea e equiv
```

## Pedido 38: Convenções da ONU sobre genocídio, refúgio e proteção contra discriminações, tortura e desaparecimento (`direito.convencoes-onu-direitos-humanos`, 220 tópicos de edital)

```
Matéria: direito.convencoes-onu-direitos-humanos (Convenções da ONU sobre genocídio, refúgio e proteção contra discriminações, tortura e desaparecimento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Genocídio: Convenção de 1948 e responsabilização no Brasil": 17
- "2. Refúgio: Convenção de 1951, Protocolo de 1967 e Lei nº 9.474/1997": 18
- "3. Discriminação racial: convenção, medidas especiais e comunicações": 25
- "4. Eliminação da discriminação contra a mulher e Protocolo Facultativo": 21
- "5. Tortura, prevenção e desaparecimento forçado: diferenças essenciais": 19

Já existem (não repita):
- Um grupo de pessoas é perseguido com intenção de destruí-lo por sua religião. Para o enqua
- Pessoas são forçadas a impedir nascimentos em um grupo étnico, com finalidade comprovada d
- Uma pessoa foge de perseguição estatal por opinião política, está fora de seu país e teme 
- Uma família foge de perseguição decorrente de fatos de 2024. Qual papel do Protocolo sobre
- Uma pessoa deixa seu país por grave e generalizada violação de direitos humanos, sem alega
- Uma regra de acesso a serviço parece neutra, mas produz restrição racial injustificada. Qu
- Uma mulher sofre restrição à capacidade civil em contrato apenas por seu sexo. Qual tratad
- Durante calamidade, autoridade tenta justificar tortura de detido por necessidade excepcio
- A administração institui ação especial temporária voltada a acelerar igualdade material de
- Quatro pessoas se associam com a finalidade de praticar genocídio nos termos da Lei nº 2.8
- Uma pessoa reconhecida como ameaça à segurança do país enfrenta risco substancial de tortu
- Uma pessoa pretende submeter comunicação individual por discriminação racial ao mecanismo 
- Grupo de mulheres, sob jurisdição de Estado Parte, apresenta petição escrita e identificad
- No Brasil, uma autoridade recebe pedido de proteção internacional de pessoa com fundado te
- Uma pessoa incita direta e publicamente a prática de genocídio por meio da imprensa, sem c
- Uma confissão foi obtida mediante tortura e o processo pretende utilizá-la como prova cont
- Agentes detêm pessoa, ocultam o local de custódia e recusam reconhecer a detenção, impedin
- Uma pessoa solicita refúgio e enfrenta processo de extradição fundado nos mesmos fatos que
- Um agente pratica tentativa de uma das condutas previstas na Lei nº 2.889/1956, sem alcanç
- Uma estrutura pública deseja prevenir tortura com inspeções periódicas de locais de privaç
- O Comitê recebe informações confiáveis de violações graves ou sistemáticas dos direitos pr
- Refugiado reconhecido foi condenado definitivamente por crime particularmente grave e repr
- Uma pessoa desapareceu após custódia clandestina; seus familiares sofreram danos diretos e
- Uma política protetiva racial foi desenhada para superar desigualdade concreta e prevê tér
- Autoridades executam medida destinada a impedir nascimentos de grupo religioso com intençã
- Durante conflito armado, alguém pratica uma das condutas do art. II da Convenção de 1948 c
- No julgamento de um caso de genocídio, o simples fato de as vítimas serem integrantes de o
- Uma pessoa foge por fundado temor de perseguição religiosa e está fora de seu país de naci
- Um solicitante de refúgio que entrou irregularmente no Brasil tem seu pedido necessariamen
- Uma prática aparentemente neutra restringe o exercício de direitos de pessoas de determina
- A CEDAW proíbe que o Estado promova qualquer medida temporária para acelerar igualdade mat
- A ordem de superior hierárquico não justifica tortura praticada em interrogatório, ainda q
- O Decreto nº 4.738/2003 promulgou a própria Convenção Internacional sobre a Eliminação da 
- Para o art. 1º, III, da Lei nº 9.474/1997, uma pessoa pode ter proteção de refúgio quando 
- Uma petição anônima é apresentada ao Comitê da CEDAW pelo Protocolo Facultativo, sem ident
- Pelo Protocolo Facultativo à CEDAW, o esgotamento dos recursos internos pode ser dispensad
- O art. 15 da Convenção contra a Tortura admite confissão obtida mediante tortura como prov
- O sistema criado pelo OPCAT admite inspeções periódicas a locais de privação de liberdade,
- O art. 2º da Convenção contra o Desaparecimento Forçado equipara qualquer prisão ilegal pu
- No regime do desaparecimento forçado, o art. 8º estabelece que, quando o Estado aplicar pr
- O § 2º do art. 33 da Convenção de Refugiados, relativo a segurança nacional e a condenado 
- O art. 24 da Convenção contra o Desaparecimento Forçado considera vítima tanto a pessoa de
- O procedimento de inquérito por violações graves ou sistemáticas da CEDAW e o procedimento
- Conforme o art. 12 da Convenção contra o Desaparecimento Forçado, indícios razoáveis de de
- A Convenção sobre Desaparecimento Forçado limita toda responsabilidade pelo ilícito ao aut
```

## Pedido 39: Cultura e Clima Organizacional (`gestao-pessoas.cultura-clima-organizacional`, 220 tópicos de edital)

```
Matéria: gestao-pessoas.cultura-clima-organizacional (Cultura e Clima Organizacional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. O que é cultura organizacional": 20
- "2. Níveis e elementos da cultura": 20
- "3. Funções, tipos e dimensões culturais": 20
- "4. Formação, socialização e mudança da cultura": 20
- "5. Clima organizacional e pesquisa de clima": 20

Já existem (não repita):
- Segundo Edgar Schein, o nível da cultura organizacional que corresponde às crenças inconsc
- Para Schein, os artefatos — como arquitetura, vestuário, linguagem e cerimônias — são o ní
- Uma empresa divulga em seu site que "valoriza a inovação e aceita o erro como parte do apr
- A cultura organizacional é um fenômeno compartilhado, aprendido e transmitido aos novos me
- Segundo Schein, a cultura organizacional se forma à medida que o grupo aprende a lidar com
- Na metáfora do iceberg aplicada à cultura organizacional, os aspectos informais, como perc
- Sobre a relação entre cultura organizacional e organização informal, é correto afirmar que
- Em uma empresa, todos conhecem a história de como o fundador, nos primeiros anos, dormia n
- Os tabus, como elemento da cultura organizacional, orientam o comportamento delimitando o 
- A cerimônia anual em que os melhores funcionários são premiados diante de toda a empresa é
- NÃO é considerada função da cultura organizacional:
- Uma cultura forte é aquela em que os valores centrais são intensamente aceitos e amplament
- O setor de pesquisa de uma empresa compartilha os valores centrais da organização, mas acr
- Na tipologia de Charles Handy, a cultura burocrática, baseada em regras, procedimentos e f
- No modelo de valores concorrentes de Cameron e Quinn, a cultura de adhocracia caracteriza-
- Nos estudos de Hofstede, a dimensão que mede o grau em que os membros de uma sociedade ace
- A cultura organizacional pode funcionar como barreira à diversidade e a fusões e aquisiçõe
- Segundo Robbins, o processo de socialização do novo membro compreende as etapas de:
- Os fundadores exercem grande influência na formação da cultura organizacional, imprimindo 
- A mudança da cultura organizacional tende a ocorrer com mais facilidade quando:
- O formalismo, traço apontado na cultura da administração pública brasileira, refere-se à d
- Clima organizacional pode ser definido como:
- Cultura e clima organizacional são conceitos sinônimos, pois ambos se referem aos valores 
- Em relação às diferenças entre cultura e clima organizacional, assinale a alternativa corr
- Constituem indicadores que podem revelar o clima de uma organização:
- A realização de pesquisa de clima sem devolução dos resultados aos participantes e sem imp
- A primeira etapa recomendada para uma pesquisa de clima organizacional é:
- A gestão do clima organizacional é responsabilidade exclusiva da área de gestão de pessoas
- Entre os fatores que influenciam o clima organizacional, incluem-se:
- O clima organizacional está diretamente relacionado à motivação e à satisfação dos membros
- Os valores expostos (ou crenças e valores assumidos), no modelo de Schein, correspondem a:
- Para Robbins, a cultura organizacional é um sistema de significados compartilhados pelos m
- Um órgão público em que as decisões, mesmo simples, dependem sempre do dirigente máximo, o
- Um gestor, ao ingressar numa nova organização, percebe o ambiente aberto, as roupas inform
- Uma organização cujos valores dominantes são a colaboração, o trabalho em equipe e um ambi
- Na dimensão de Hofstede denominada aversão à incerteza, sociedades com índice elevado tend
- A pesquisa de clima organizacional deve ser preferencialmente identificada, com o nome do 
- A seleção de pessoas com valores compatíveis, a socialização dos novos membros e as ações 
- Após uma fusão, duas empresas mantiveram o mesmo organograma, os mesmos uniformes e a mesm
- Uma pesquisa de clima revelou queda na satisfação de uma equipe logo após a troca de chefi
```

## Pedido 40: Conceitos, normas e fundamentos da auditoria (`auditoria.conceitos-normas`, 220 tópicos de edital)

```
Matéria: auditoria.conceitos-normas (Conceitos, normas e fundamentos da auditoria)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, evolução e modalidades de auditoria": 21
- "2. Estrutura conceitual dos trabalhos de asseguração": 20
- "3. Mapa das normas técnicas NBC TA e sua aplicação": 18
- "4. Ética, independência, gestão e revisão da qualidade": 17
- "5. Auditoria interna, perícia, responsabilidades e comunicação": 24

Já existem (não repita):
- Uma sociedade contrata profissional para auditar demonstrações contábeis e seu diretor afi
- Na contratação de auditoria independente, a administração pretende atribuir à firma audito
- Uma atividade examina controles internos, riscos de processos e eficiência operacional, re
- Em discussão societária, um especialista nomeado deve apurar haveres, responder a quesitos
- Um profissional de asseguração limitada conclui que nada chegou ao seu conhecimento que o 
- Uma entidade produz relatório de indicadores segundo critérios públicos, e profissional in
- Um profissional é contratado para ele próprio verificar diretamente se unidades de uma org
- Uma equipe precisa consultar a norma técnica que trata da responsabilidade do auditor inde
- Uma equipe documenta procedimentos aplicados, evidências obtidas e julgamentos que fundame
- Uma firma organiza seu sistema geral de gestão de qualidade, incluindo aceitação de client
- Uma auditoria relevante requer avaliação objetiva dos julgamentos significativos da equipe
- Um manual antigo aponta NBC PA 290 como principal referência de independência de auditoria
- Ao preparar um novo trabalho de auditoria, o profissional distingue planejar a auditoria d
- Um profissional consulta a norma sobre evidência de auditoria e outra sobre seleção de ite
- Ao final de auditoria financeira, a equipe precisa identificar a norma principal sobre for
- Uma equipe precisa comunicar responsáveis pela governança sobre escopo e assuntos relevant
- Num litígio, o especialista nomeado apresenta laudo técnico sobre matéria contábil e o esp
- Uma firma registrada para atuar no mercado sujeito à revisão externa de qualidade passa po
- Um auditor estabeleceu materialidade no planejamento e, após identificar riscos de distorç
- Após a data das demonstrações, ocorre evento que pode exigir tratamento no relatório. Em o
- Uma firma pretende auditar a efetividade de sistema contábil cuja implementação foi integr
- Durante trabalho independente, a equipe enfrenta suspeita de fraude intencional e, separad
- Ao avaliar relacionamento financeiro relevante com cliente de auditoria, um profissional d
- Um relatório de auditoria chama atenção para informação adequadamente divulgada e fundamen
- Um serviço contratado define procedimentos exatos sobre registros e solicita apenas que se
- Uma entidade assume a elaboração das demonstrações contábeis, enquanto o auditor independe
- Um relatório de auditoria interna contém recomendações para melhorar a segregação de funçõ
- Em trabalho de atestação, outra parte mensura ou avalia o objeto com base em critérios, e 
- Como a revisão de demonstrações oferece asseguração limitada, ela deve necessariamente uti
- Ao pesquisar normas técnicas, um profissional identifica que a NBC TA 230 trata de documen
- Uma firma afirma que, na lista atual de normas do CFC, a NBC PA 02 regula o sistema integr
- A lei estadunidense Sarbanes-Oxley, promulgada em 2002, constitui marco relevante na evolu
- Em trabalho de asseguração, os usuários previstos e a parte responsável devem corresponder
- A NBC TA 315 trata da identificação e avaliação dos riscos de distorção relevante, ao pass
- A NBC TA 560 disciplina continuidade operacional da entidade e a NBC TA 570 versa sobre ev
- Um profissional que presta auditoria independente precisa avaliar não somente sua independ
- Uma revisão de qualidade individual, realizada sob NBC PA 02, elimina a necessidade de cum
- O profissional de auditoria independente pode considerar trabalho desenvolvido por auditor
- Uma firma que precisa demonstrar cumprimento da estrutura ética considera suficiente aceit
- A documentação da auditoria precisa ser suficiente para que auditor experiente sem relação
- Se uma entidade já aplicou procedimentos previamente acordados, as constatações obtidas de
- Um assistente técnico indicado pela parte produz parecer pericial contábil fundamentado, e
- A NBC TA 705 e a NBC TA 706 são equivalentes porque o parágrafo de ênfase é espécie de opi
- Em trabalho de asseguração limitado, o risco é reduzido a um nível aceitável que pode ser 
- A revisão externa pelos pares pode ser tratada como auditoria das demonstrações da entidad
```

## Pedido 41: Equiparação salarial (`direito-trabalho.equiparacao-salarial`, 219 tópicos de edital)

```
Matéria: direito-trabalho.equiparacao-salarial (Equiparação salarial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Equiparação salarial e igualdade remuneratória": 23
- "2. Desvio e acúmulo de função, prescrição e decadência": 42
- "3. FGTS: depósitos, modalidades e prescrição": 10
- "4. CIPA, EPI, insalubridade e periculosidade": 15
- "5. Proteção do menor, aprendizagem e integração dos temas": 10

Já existem (não repita):
- Duas pessoas desempenham as mesmas tarefas, com igual produtividade e perfeição técnica, n
- Na comparação entre empregados do mesmo estabelecimento, qual é a diferença máxima de temp
- Uma empresa com quadro de carreira válido adota promoções apenas por merecimento, mediante
- Uma pessoa contratada para função de menor remuneração passa a exercer regularmente função
- Uma empresa calcula depósito do FGTS sobre remuneração mensal de R$ 2.000,00 de empregado 
- Um empregador organiza o depósito de FGTS referente à remuneração paga ou devida no mês an
- Um estabelecimento precisa organizar participação dos trabalhadores na prevenção de aciden
- Uma atividade foi caracterizada como insalubre em grau médio pelas condições técnicas pert
- Uma adolescente de 15 anos procura vínculo de trabalho compatível com a Constituição e a f
- Uma instituição verifica qual faixa de cota de aprendizagem deve observar para funções do 
- Dois empregados cumprem funções idênticas, com rendimento técnico equivalente, em unidades
- Uma sociedade privada com 120 empregados precisa cumprir a obrigação de transparência da L
- Uma empresa com obrigação de transparência deixa de publicar o relatório semestral, sem qu
- A inspeção identifica desigualdade remuneratória em empresa abrangida pelo Decreto nº 11.7
- Após comprovar desvio funcional, alguém pede diferenças salariais e, cumulativamente, muda
- Um vínculo empregatício terminou em 10 de janeiro de 2024. A reclamação sobre diferenças s
- Uma integrante suplente foi eleita pelos empregados para a CIPA e posteriormente despedida
- Um aprendiz matriculado em programa válido recebe remuneração-base de R$ 1.800,00 e tem co
- Na mesma função e no mesmo estabelecimento, empregado e paradigma têm produtividade e qual
- Uma pessoa tenta alcançar remuneração maior com paradigma que deixou a função anos antes d
- Comprovada discriminação salarial por raça e pagas as diferenças correspondentes, a empres
- Uma demanda de 2026 cobra ausência de depósitos do FGTS relativa a competências recentes, 
- Em atividade nociva, a empresa apenas entrega EPI e recolhe assinatura de recebimento, sem
- Um empregado exerce tarefas habituais do contrato e recebe atividade adicional ocasional c
- Uma pessoa em aprendizagem concluiu o ensino fundamental e recebe formação teórica integra
- Duas pessoas exercem idêntica função no mesmo estabelecimento e possuem igual produtividad
- Após a Reforma Trabalhista, qualquer quadro de carreira privado ainda deve ser previamente
- No regime atual do FGTS, a Lei nº 8.036/1990, art. 15, prevê depósito geral de oito por ce
- A Constituição permite emprego comum a partir de quatorze anos, bastando autorização escri
- Uma atividade técnica reconhecida como insalubre em grau máximo está sujeita ao percentual
- Se uma empresa identifica desigualdade salarial no relatório semestral, a diferença estatí
- Uma empresa privada com 150 empregados deve observar relatório semestral de igualdade sala
- O desvio de função, quando comprovado e apto a justificar diferenças salariais, determina 
- O prazo para ajuizar reclamação sobre créditos de relação de trabalho após a extinção do c
- O adicional de periculosidade tem, no regime geral do art. 193, § 1º, da CLT, alíquota de 
- Um contrato de aprendizagem válido tem recolhimento de FGTS de dois por cento da remuneraç
- A entrega de EPI com recibo, mesmo que o equipamento seja inadequado ao agente nocivo e o 
- Para lesões relativas a depósitos de FGTS exigíveis depois de 13 de novembro de 2014, o en
- Para instaurar inquérito judicial de falta grave de empregado estável suspenso, a CLT, art
- Comprovada discriminação por origem em desacordo com o art. 461 da CLT, o pagamento das di
- Trabalhadores com idêntica função em estabelecimentos empresariais distintos podem exigir 
- O mandato ordinário dos membros eleitos da CIPA tem duração de um ano, com possibilidade d
- Quando a mesma atividade enseja adicionais de insalubridade e de periculosidade, a CLT, ar
- Um aprendiz que concluiu ensino fundamental pode ter jornada de até oito horas diárias se 
- Uma pessoa com deficiência que completa 24 anos fica necessariamente impedida de celebrar 
```

## Pedido 42: História e Geografia do Amazonas (`amazonas.conhecimentos-estaduais`, 219 tópicos de edital)

```
Matéria: amazonas.conhecimentos-estaduais (História e Geografia do Amazonas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Povos indígenas, expedições e colonização": 20
- "2. Período pombalino, Rio Negro e Cabanagem": 20
- "3. Província, borracha e fronteiras": 20
- "4. Crise da borracha, século XX e Zona Franca": 20
- "5. Espaço natural, população e questões atuais": 20

Já existem (não repita):
- A expedição que desceu o rio Amazonas desde os Andes até o Atlântico, entre 1541 e 1542, f
- O nome do rio Amazonas está associado aos relatos de mulheres guerreiras feitos durante a 
- A expedição de Pedro Teixeira (1637–1639) destacou-se por:
- O Forte do Presépio, fundado pelos portugueses em 1616, deu origem à cidade de Manaus.
- Sobre a colonização da Amazônia nos séculos XVII e XVIII, é correto afirmar:
- Na época da conquista, a Amazônia abrigava sociedades indígenas diversas, de diferentes fa
- A Capitania de São José do Rio Negro, embrião do atual Amazonas, foi criada em:
- Manaus surgiu em torno do Forte de São José da Barra do Rio Negro, construído em 1669.
- O Diretório dos Índios, instituído no período pombalino:
- Os Tratados de Madri (1750) e de Santo Ildefonso (1777) redefiniram limites entre Portugal
- A Cabanagem, revolta que atingiu o Alto Amazonas, foi:
- Após a Independência, a antiga capitania do Rio Negro tornou-se a Comarca do Alto Amazonas
- A Província do Amazonas foi:
- No sistema de aviamento do ciclo da borracha:
- A seca de 1877 no Nordeste contribuiu para a migração de milhares de nordestinos para os s
- O Teatro Amazonas, símbolo da riqueza da borracha em Manaus, foi inaugurado em:
- O Acre foi incorporado ao Brasil pelo Tratado de Petrópolis, de 1903, após disputa com a B
- A riqueza da borracha permitiu em Manaus:
- A crise da borracha amazônica, a partir de 1912, deveu-se principalmente:
- A Batalha da Borracha, durante a Segunda Guerra Mundial, recrutou trabalhadores conhecidos
- A revolta ocorrida em Manaus em 1924 teve inspiração:
- O Clube da Madrugada foi um movimento de renovação literária e artística surgido em Manaus
- A Zona Franca de Manaus:
- A Zona Franca de Manaus foi criada em 1957 e reformulada em 1967.
- Após a Revolução de 1930, o Amazonas viveu a chamada Era dos Interventores, com governante
- O Amazonas é o maior estado brasileiro em área territorial.
- O Amazonas faz fronteira internacional com:
- O Amazonas possui 62 municípios.
- O ponto mais alto do Brasil, localizado no Amazonas, é o:
- No Amazonas, o rio Amazonas é chamado de Solimões até:
- Igapó é a área de floresta alagada por águas escuras, enquanto a várzea é alagada periodic
- São rios da bacia amazônica que atravessam o estado do Amazonas:
- A população do Amazonas distribui-se de forma equilibrada entre a capital e o interior, se
- A província petrolífera de Urucu, no município de Coari, destaca-se pela produção de:
- A hidrelétrica de Balbina, no Amazonas, é frequentemente citada pelos impactos ambientais 
- Sobre a questão indígena no Amazonas, é correto afirmar:
- O transporte no interior do Amazonas é predominantemente:
- Ordene cronologicamente: I – Província do Amazonas; II – Capitania de São José do Rio Negr
- O Encontro das Águas, próximo a Manaus, é o local onde as águas escuras do rio Negro e as 
- O Tratado de Petrópolis, de 1903, definiu a fronteira entre o Amazonas e a Venezuela no Pi
- A expedição de Ursúa e Aguirre, em 1560–1561, também percorreu o rio Amazonas e ficou marc
```

## Pedido 43: Otorrinolaringologia (`medicina.otorrinolaringologia`, 219 tópicos de edital)

```
Matéria: medicina.otorrinolaringologia (Otorrinolaringologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Anatomia, fisiologia e semiologia otorrinolaringológica": 17
- "2. Otites, perda auditiva, complicações e terapêutica otológica": 17
- "3. Zumbido, tontura, vertigem e síndromes vestibulares": 17
- "4. Rinites, rinossinusites, epistaxe e afecções nasais": 17
- "5. Faringe, amígdalas, distúrbios do sono e laringe": 16
- "6. Corpos estranhos, tumores de cabeça e pescoço e urgências": 16

Já existem (não repita):
- Em qual porção do ouvido se localiza a cóclea, responsável pela transdução de estímulos so
- Uma criança com febre e otalgia apresenta membrana timpânica apenas avermelhada, sem abaul
- Adulto com perda auditiva unilateral súbita há poucas horas, plenitude auricular e otoscop
- Em quadro típico de otite externa aguda difusa não complicada e membrana timpânica íntegra
- Paciente relata crises de vertigem de segundos ao virar-se na cama, sem déficit focal e se
- Em rinossinusite aguda inicialmente semelhante a resfriado, qual evolução favorece hipótes
- Adulto com epistaxe anterior sem instabilidade hemodinâmica procura orientação. Qual medid
- Na investigação de faringoamigdalite em adulto, qual combinação eleva a suspeita de estrep
- Profissional da voz refere rouquidão persistente sem melhora há cinco semanas. Não houve i
- Após uma pancada no nariz, adolescente apresenta obstrução e tumefação flutuante do septo 
- Qual conjunto descreve melhor a síndrome de Ménière?
- Uma criança apresenta secreção purulenta unilateral fétida pelo nariz sem quadro sistêmico
- Sobre a otite média com efusão, assinale a descrição correta.
- Pessoa com diabetes, otalgia noturna intensa e tecido de granulação no conduto persiste si
- Diante de disfonia progressiva associada a massa cervical persistente em pessoa tabagista,
- Em paciente com rinite alérgica persistente e obstrução nasal relevante, qual intervenção 
- Quais achados mais sugerem abscesso peritonsilar, em vez de faringite viral não complicada
- Em criança com estridor inspiratório desde os primeiros meses e piora ao alimentar-se, uma
- Paciente relata zumbido sincronizado ao pulso, predominantemente unilateral. Qual abordage
- Pessoa com rinossinusite apresenta proptose, diplopia e redução visual progressiva. Qual é
- Sobre a relação de nervos laríngeos e músculos intrínsecos, assinale a correta.
- No adulto com suspeita de apneia obstrutiva do sono, qual estratégia melhor define diagnós
- A rinossinusite é classificada como crônica quando:
- Em adulto com vertigem contínua aguda e incapacidade importante de caminhar, qual precauçã
- Qual afirmação sobre lesões benignas das pregas vocais é mais apropriada?
- A tuba auditiva comunica o ouvido médio com a nasofaringe, contribuindo para equalização d
- Dor à mobilização do pavilhão e à pressão do trago favorece diagnóstico de otite média com
- Otite média aguda pode ser diagnosticada de forma confiável apenas pela hiperemia da membr
- A antibioticoterapia sistêmica é rotineiramente a primeira escolha para toda otite externa
- Perda auditiva neurossensorial súbita deve ser considerada uma situação de urgência mesmo 
- Na VPPB do canal posterior, as manobras de reposicionamento canalicular podem ser terapêut
- A vertigem da neurite vestibular é classicamente acompanhada de perda auditiva neurossenso
- Em paciente com vertigem de início súbito, ataxia grave e disartria, o clínico deve afasta
- Zumbido pulsátil sincronizado ao batimento cardíaco é sempre manifestação fisiológica sem 
- Rinorreia espessa ou verde, isoladamente, diferencia com segurança rinossinusite bacterian
- Rinossinusite crônica exige sintomas persistentes por pelo menos doze semanas e evidência 
- Na epistaxe anterior inicial, a recomendação geral é manter o paciente sentado, com leve i
- A presença de tosse, coriza e rouquidão em faringite aguda costuma favorecer etiologia vir
- O ronco habitual é suficiente para diagnosticar apneia obstrutiva do sono e estabelecer su
- Disfonia sem melhora por quatro semanas ou associada a sinais de alarme justifica visualiz
- Estridor em criança com dificuldade para deglutir secreções e esforço respiratório deve se
- Hematoma septal após trauma nasal pode ser acompanhado sem urgência porque não oferece ris
- Massa cervical persistente em adulto, sem infecção evidente, pode exigir investigação de n
- A presença de paralisia facial associada a massa parotídea persistente deve aumentar a sus
- Bateria tipo botão impactada na cavidade nasal deve ser retirada com urgência, devido à po
```

## Pedido 44: Crimes contra o patrimônio (`direito-penal.crimes-contra-patrimonio`, 218 tópicos de edital)

```
Matéria: direito-penal.crimes-contra-patrimonio (Crimes contra o patrimônio)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Furto, qualificadoras, privilégios e alterações de 2026": 22
- "2. Roubo, extorsão e extorsão mediante sequestro": 19
- "3. Usurpação, dano e apropriação indébita": 19
- "4. Estelionato, fraude eletrônica e outras fraudes": 21
- "5. Receptação, imunidades patrimoniais e integração dos tipos": 19

Já existem (não repita):
- Em outubro de 2026, alguém subtrai discretamente um relógio alheio, sem violência ou quali
- Uma pessoa subtrai telefone alheio mediante ameaça de agressão imediata, invertendo a poss
- Um viajante encontra carteira perdida e resolve ficar com ela, sem devolvê-la ao dono ou e
- Uma pessoa anuncia um aparelho inexistente, convence o comprador a pagar e recebe o valor 
- Um comerciante adquire mercadoria que sabe provir de furto e a oculta em depósito para rev
- Um acusado primário subtrai bem de pequeno valor e o fato reúne requisitos para incidência
- Após tomar a bolsa sem contato com a vítima, alguém a ameaça imediatamente para evitar que
- Uma pessoa recebe notebook emprestado para uso por três dias e depois o vende a terceiro c
- Uma pessoa intencionalmente quebra a janela da casa alheia para prejudicar o dono, sem ten
- Em outubro de 2026, um agente pratica roubo simples mediante grave ameaça, sem resultado a
- Sob grave ameaça, a vítima é obrigada a realizar transferência bancária a favor do autor, 
- Um autor constrange a vítima por grave ameaça a ordenar uma transferência, mas o sistema i
- Em ação penal por dano simples do art. 163, caput, a pessoa ofendida deseja iniciar a pers
- A empresa desconta contribuições dos pagamentos de segurados e deixa de repassá-las ao sis
- Uma pessoa obteve fraudulentamente benefício previdenciário mediante documentos falsos, em
- Uma denúncia de estelionato por fato ocorrido após a vigência da Lei nº 15.397/2026 invoca
- No exercício de atividade comercial, alguém recebe e revende mercadoria que deve saber ser
- Um descendente subtrai sem violência bem de ascendente que tem 67 anos. Qual dispositivo a
- Um furto de bem de pequeno valor é praticado mediante rompimento de obstáculo por agente p
- Em roubo ocorrido em outubro de 2026, um autor emprega arma de fogo comum e não há uso res
- Em 2026, alguém subtrai combustível líquido de duto de transporte, nas condições do art. 1
- Em mercado clandestino, alguém adquire cães comprovadamente furtados para revenda, sabendo
- Pessoas mantêm vítima sequestrada por trinta horas para exigir resgate, sem lesão grave ne
- Uma pessoa cede voluntariamente sua conta bancária para nela transitarem recursos que sabe
- Duas pessoas, sem violência ou grave ameaça, invadem imóvel particular com propósito de es
- Em outubro de 2026, o furto simples do art. 155, caput, tem pena de reclusão de um a seis 
- Em um furto praticado após maio de 2026 durante o repouso noturno, a majorante do art. 155
- Uma pessoa toma bem móvel alheio com grave ameaça e obtém posse por poucos instantes antes
- No crime de apropriação indébita, a coisa é necessariamente subtraída clandestinamente de 
- O estelionato do art. 171 exige induzimento ou manutenção em erro por fraude, vantagem ilí
- A receptação do art. 180 torna-se atípica se o autor do furto que originou o bem não puder
- O furto privilegiado do art. 155, § 2º, pode coexistir com qualificadora objetiva, quando 
- No roubo praticado com arma branca, a pena é necessariamente aumentada de dois terços, mes
- Uma pessoa acha bem móvel perdido e, decidindo apropriar-se dele, não o entrega ao proprie
- A Lei nº 15.397/2026 preservou integralmente o art. 171, § 5º, e seus incisos, mantendo a 
- No furto simples cometido entre ascendentes e descendentes, a isenção de pena do art. 181 
- O art. 180, § 3º, prevê receptação culposa para situações específicas de aquisição ou rece
- Pelo art. 155, § 10, inserido pela Lei nº 15.517/2026, a subtração dos combustíveis e prod
- Na extorsão do art. 158, a consumação depende de o agente receber a vantagem indevida, mes
- A invasão de terreno alheio para esbulho possessório, sem violência ou ameaça, exige concu
- A obtenção de benefício previdenciário mediante fraude pelo próprio beneficiário e a fraud
- O art. 180-A, após a Lei nº 15.397/2026, alcança expressamente animal doméstico de origem 
- O roubo cometido pelo cônjuge durante a sociedade conjugal permanece isento de pena pelo a
- A Súmula 610 do STF admite latrocínio consumado quando se consuma a morte no contexto da t
- A aquisição consciente de produto de furto perde relevância penal como receptação quando a
```

## Pedido 45: Processo administrativo fiscal, consulta e execução fiscal (`direito-tributario.processo-administrativo-fiscal`, 217 tópicos de edital)

```
Matéria: direito-tributario.processo-administrativo-fiscal (Processo administrativo fiscal, consulta e execução fiscal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fiscalização, lançamento e impugnação no processo administrativo": 19
- "2. Julgamento administrativo, recursos, consulta e representação penal": 18
- "3. Execução fiscal: título, citação, garantia e medidas de satisfação": 24
- "4. Embargos, pré-executividade e prescrição intercorrente": 24
- "5. Ações tributárias, cautelar fiscal e articulação das defesas": 15

Já existem (não repita):
- Em outubro de 2026, uma sociedade foi intimada de auto de infração federal e pretende apre
- Depois de receber exigência formalizada, contribuinte protocola defesa que questiona fatos
- Uma fiscalização conclui que houve infração e prepara documento contendo descrição dos fat
- Após apresentar impugnação administrativa tempestiva contra lançamento federal, o interess
- Antes de adotar determinada operação, uma empresa descreve fato concreto e solicita interp
- No ajuizamento da execução fiscal, a Fazenda junta documento que identifica o crédito insc
- Uma pessoa é citada regularmente em execução fiscal para pagamento ou garantia da dívida. 
- Uma sociedade executada não dispõe de dinheiro imediato e propõe seguro garantia judicial 
- Após decisão desfavorável da DRJ no contencioso federal comum, o contribuinte pretende lev
- Acórdão do CARF conferiu à lei interpretação divergente daquela atribuída por outra turma 
- Empresa já intimada a cumprir obrigação relativa ao mesmo fato pretende apresentar consult
- Uma fiscalização identifica possível crime tributário abrangido pelo art. 83 da Lei 9.430/
- Após constituir garantia por fiança bancária em execução fiscal, a empresa deseja opor emb
- Em execução fiscal, a CDA revela prescrição pela leitura das próprias datas documentadas e
- A Fazenda tomou ciência de que o devedor não foi localizado e não há bens penhoráveis, mas
- O credor público demonstra que contribuinte está transferindo bens a terceiros para frustr
- Uma empresa propõe ação anulatória para discutir cobrança, mas não dispõe de depósito inte
- Um prazo processual administrativo abrangido pelo Decreto 70.235/1972 atravessa o período 
- Dois processos federais têm decisões de DRJ: um segue o rito contencioso geral, outro se e
- A execução está garantida por seguro garantia regularmente apresentado. O devedor sustenta
- O juiz pretende decretar indisponibilidade de bens pelo art. 185-A do CTN, embora a Fazend
- Uma sociedade encerrou irregularmente atividades em 2025; pessoa que não a administrava qu
- Contribuinte recorre administrativamente contra exigência federal e, depois, ajuíza ação p
- A Fazenda conseguiu cautelar fiscal preparatória e a exigência correspondente tornou-se ir
- A Fazenda pretende substituir a CDA por outra para trocar completamente a pessoa indicada 
- Uma impugnação tempestiva contra auto de infração federal instaura fase litigiosa do proce
- Em 2026, a impugnação geral contra lançamento federal permanece sujeita a trinta dias corr
- No contencioso administrativo federal comum, uma decisão da primeira instância pode ser qu
- Uma consulta sobre fato determinado apresentada à administração tributária é, por definiçã
- A inscrição regular em dívida ativa é documentada por CDA, e sua presunção de certeza e li
- Após a citação na execução fiscal, o prazo de trinta dias para apresentar embargos começa 
- Uma medida cautelar fiscal pode ser requerida para preservar bens diante de situação docum
- No Decreto 70.235/1972, a ausência de prazo expresso para ato das partes exige que se apli
- O art. 16, § 4º, do Decreto 70.235/1972 prevê apresentação da prova documental em regra ju
- O recurso especial à CSRF é cabível por simples inconformismo com qualquer decisão de prim
- Uma consulta tributária eficaz produz efeitos delimitados em relação à matéria consultada,
- Segundo a Súmula 393/STJ, a exceção de pré-executividade é a via apropriada para toda aleg
- O art. 9º da LEF admite fiança bancária e seguro garantia como instrumentos de garantia da
- O redirecionamento da execução por dissolução irregular pode atingir qualquer sócio que já
- A jurisprudência repetitiva do STJ indica que o ano de suspensão por ausência de localizaç
- A Súmula Vinculante 28 autoriza exigir depósito integral antes do protocolo de ação anulat
- Na cautelar fiscal preparatória, o prazo de sessenta dias para propor a execução, previsto
- O art. 185-A do CTN e a cautelar fiscal da Lei 8.397/1992 são o mesmo procedimento, ambos 
- Para delitos materiais do art. 1º, I a IV, da Lei 8.137/1990, a Súmula Vinculante 24 consi
- De acordo com a Súmula 392/STJ, a Fazenda pode substituir a CDA para corrigir erro formal 
```

## Pedido 46: Evolução e fisiologia de plantas e animais (`biologia.evolucao-fisiologia-plantas-animais`, 216 tópicos de edital)

```
Matéria: biologia.evolucao-fisiologia-plantas-animais (Evolução e fisiologia de plantas e animais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Evolução: teorias, mecanismos, especiação e evidências": 17
- "2. Plantas: grupos, ciclos de vida e inovações reprodutivas": 16
- "3. Fisiologia vegetal: nutrição, transporte, hormônios e respostas": 16
- "4. Diversidade animal: organização e fisiologia dos invertebrados": 34
- "5. Cordados e fisiologia comparada: integração dos sistemas": 17

Já existem (não repita):
- Em uma população de besouros, indivíduos com cor escura já existiam antes de o substrato f
- Ao comparar o membro anterior de um morcego com o de uma baleia, um estudante encontrou di
- Em um musgo, uma estrutura haploide produz gametas, enquanto outra, diploide, origina espo
- Uma planta terrestre de pequeno porte apresenta rizoides e um esporófito nutricionalmente 
- Uma araucária produz pólen e sementes apoiadas em estruturas reprodutivas, mas não forma o
- Uma planta iluminada continua consumindo oxigênio e liberando dióxido de carbono em suas m
- Ao acompanhar a subida da água absorvida pelas raízes até as folhas de uma árvore, um pesq
- Uma muda enfrenta déficit hídrico e reduz a abertura de seus estômatos para perder menos á
- Uma esponja marinha filtra partículas suspensas por meio da circulação de água no corpo. A
- Um animal aquático possui simetria radial, cavidade gastrovascular e tentáculos capazes de
- Uma tempestade eliminou aleatoriamente grande parte de uma população de lagartos. Os sobre
- Duas populações de sapos vivem na mesma região, porém uma se reproduz em janeiro e outra e
- Em uma samambaia adulta, esporângios presentes nas folhas formam estruturas haploides que 
- Um experimento compara uma samambaia com um pinheiro. Ambos possuem xilema e floema, mas s
- Em uma flor de angiosperma, dois núcleos espermáticos participam do processo de dupla fecu
- Uma árvore apresenta anel de remoção da casca que interrompe o floema, mas mantém parte fu
- Duas espécies de ambientes secos reduzem perdas hídricas por vias diferentes: uma concentr
- Um invertebrado apresenta exoesqueleto quitinoso, apêndices articulados e crescimento por 
- Um animal vermiforme possui tubo digestório com boca e ânus, pseudoceloma, cutícula e músc
- Ao comparar minhoca, planária e lombriga, uma pesquisadora precisa identificar o animal co
- Uma população diploide em equilíbrio de Hardy-Weinberg tem frequência de homozigotos reces
- Uma estudante acompanha a reprodução de uma angiosperma e verifica que, após a fecundação,
- Em um inseto, o sistema circulatório é aberto, mas os tecidos recebem oxigênio eficienteme
- Um animal marinho apresenta larva com simetria bilateral, adulto pentarradial e sistema de
- Ao contrastar os adultos de uma ave, um crocodiliano e um anfíbio típico, considera-se o t
- Ao comparar fósseis e sequências de DNA de organismos atuais, uma pesquisadora utiliza dua
- Uma lagarta exposta a novo predador altera seu comportamento durante a vida; como mudou in
- Em musgos que produzem gametas masculinos flagelados, a existência de um filme de água pod
- Em uma briófita como o musgo, a fase verde mais evidente é o esporófito diploide independe
- Em uma folha, o fechamento dos estômatos sob seca pode diminuir simultaneamente a transpir
- O açúcar sintetizado nas folhas de uma árvore chega aos frutos principalmente pelo xilema,
- Um caule jovem iluminado de apenas um lado pode curvar-se em direção à fonte luminosa porq
- Uma esponja se alimenta filtrando água e realiza a etapa inicial da digestão em cavidade g
- Ao examinar planárias, uma pesquisadora identifica simetria bilateral, três folhetos germi
- Um mexilhão utiliza uma rádula para raspar algas, estrutura que caracteriza sem exceções o
- Um modelo de população muito grande, com cruzamento ao acaso e sem seleção, migração ou mu
- Quando ocorre duplicação do conjunto cromossômico em uma linhagem vegetal, o estabelecimen
- Na dupla fecundação típica das angiospermas, a fusão de um núcleo espermático com a oosfer
- Em plantas C4, a abertura dos estômatos à noite constitui o mecanismo definidor de economi
- Um nematódeo cresce trocando periodicamente sua cutícula, e esse processo de ecdise o apro
- Como uma estrela-do-mar adulta possui simetria radial semelhante à de uma anêmona, ambos o
- Uma ascídia adulta séssil pode não exibir notocorda e cauda, embora sua larva tenha essas 
- O coração de um anfíbio adulto típico apresenta quatro câmaras inteiramente separadas, per
- Nos rins de mamíferos, a formação de urina envolve filtração glomerular e modificações tub
- Nas aves, os sacos aéreos são os principais locais de difusão de oxigênio para o sangue, e
```

## Pedido 47: Biodiversidade e patrimônio genético (`direito-ambiental.biodiversidade-patrimonio-genetico`, 215 tópicos de edital)

```
Matéria: direito-ambiental.biodiversidade-patrimonio-genetico (Biodiversidade e patrimônio genético)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Biodiversidade, Constituição e instrumentos de conservação": 22
- "2. Patrimônio genético, conhecimento tradicional e direitos dos detentores": 16
- "3. SisGen, acesso, remessa e repartição de benefícios": 13
- "4. Florestas, espécies ameaçadas e proteção territorial": 28
- "5. Biossegurança, governança, fiscalização e resolução de casos": 21

Já existem (não repita):
- Uma equipe propõe avaliar variabilidade genética dentro de uma população, número de espéci
- Uma pessoa proprietária de imóvel situado na Mata Atlântica questiona se a expressão patri
- Um programa mantém sementes em banco refrigerado fora do ambiente natural e outro protege 
- Pesquisadores planejam estudar informação genética humana e sustentam que o cadastro da Le
- Uma empresa lê em inventário cultural a utilização de casca vegetal por determinada comuni
- Uma comunidade tradicional identificável fornece conhecimento sobre propriedades de uma pl
- Apenas uma pessoa idosa preservou informação tradicional utilizada por sua comunidade. Par
- Duas comunidades trocam sementes e práticas de conservação entre si para benefício próprio
- Ao organizar colegiado de gestão do acesso ao patrimônio genético, uma norma interna deve 
- Uma pesquisa científica examina substâncias produzidas pelo metabolismo de microrganismo i
- Uma instituição realizou acesso enquadrado no art. 12 e vai apresentar pedido de patente r
- Uma equipe brasileira contrata laboratório no exterior para realizar sequenciamento como s
- Uma pessoa jurídica estrangeira quer pesquisar patrimônio genético no Brasil mediante parc
- Após pesquisar composto brasileiro, uma fabricante conclui produto acabado e programa inic
- Uma universidade pesquisou composto vegetal e licenciou resultados; outra empresa fabrica 
- Produto acabado sujeito à modalidade monetária de repartição por acesso ao patrimônio gené
- Uma empresa explora produto acabado derivado de conhecimento tradicional associado de orig
- Na exploração de produto oriundo de CTA de origem identificável, uma empresa pactua benefí
- Uma empresa de pequeno porte fabrica produto abrangido pela Lei nº 13.123/2015 e afirma qu
- Uma empresa desenvolve variedade vegetal mediante acesso enquadrado na Lei nº 13.123/2015 
- Imóvel rural situado em área de cerrado da Amazônia Legal precisa observar percentuais mín
- Duas solicitações pretendem remover vegetação secundária da Mata Atlântica: uma em estágio
- Empresa deseja exportar exemplares de espécie incluída nos anexos da CITES. Qual é a funçã
- Um empreendimento precisa de análise técnica de risco de organismo geneticamente modificad
- Uma pessoa jurídica comete infração administrativa da Lei nº 13.123/2015. Considerando som
- Uma política ambiental que conserve habitats, promova uso sustentável das espécies e distr
- Uma área particular situada na Floresta Amazônica brasileira passa ao domínio da União aut
- Uma pesquisa sobre patrimônio genético humano, embora possua aspectos éticos e científicos
- Uma comunidade identificável que detém saber tradicional sobre determinada espécie pode te
- Duas comunidades tradicionais que intercambiam conhecimentos e material de propagação em b
- O CGen, por coordenar a gestão do patrimônio genético, exerce também as competências técni
- Uma instituição nacional realizou pesquisa enquadrada no art. 12 da Lei nº 13.123/2015 e p
- Uma instituição que transfere a responsabilidade por amostra genética a laboratório estran
- Uma fabricante de produto abrangido pela Lei nº 13.123/2015 deve, como regra, indicar a mo
- Uma empresa fabrica apenas insumo intermediário derivado de acesso ao patrimônio genético 
- Na modalidade monetária de repartição por acesso ao patrimônio genético, a regra do art. 2
- Se o conhecimento tradicional associado for de origem não identificável, a Lei nº 13.123/2
- Uma microempresa enquadrada na hipótese de isenção do art. 17, § 5º, da Lei nº 13.123/2015
- Proprietário de imóvel em área de cerrado da Amazônia Legal deve reservar, como regra gera
- Ao analisar pedido de supressão na Mata Atlântica, a identificação de vegetação primária o
- A inclusão de espécie no Anexo II da CITES significa que toda exportação de exemplar fica 
- Instituição que desenvolve pesquisas com organismos geneticamente modificados enquadradas 
- O CNBS deve substituir a CTNBio na avaliação técnica ordinária de risco de cada organismo 
- No regime de infrações administrativas da Lei nº 13.123/2015, sanções como multa e embargo
- Uma empresa que pesquisa composto vegetal brasileiro, acessa conhecimento tradicional iden
```

## Pedido 48: Engenharia de dados: ingestão, pipelines, ETL e qualidade (`informatica.engenharia-dados-ingestao-qualidade`, 215 tópicos de edital)

```
Matéria: informatica.engenharia-dados-ingestao-qualidade (Engenharia de dados: ingestão, pipelines, ETL e qualidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, fontes e formatos de ingestão": 20
- "2. Pipelines, ETL/ELT, lotes, streaming e CDC": 20
- "3. Qualidade, integridade, governança e segurança": 25
- "4. Limpeza, transformação e preparo analítico": 18
- "5. Operação confiável, serviços gerenciados e casos integrados": 17

Já existem (não repita):
- Uma equipe integra pedidos de diversas fontes, valida registros, mantém cópia bruta e publ
- Um projeto extrai registros, aplica conversões no ambiente de integração e somente depois 
- Ao importar um arquivo CSV, códigos de produto como 00042 passaram a aparecer como 42. Que
- Um repositório analítico consulta poucas colunas em milhões de registros e procura reduzir
- Uma carga lê tabelas Oracle e SQL Server por conectores. Há diferenças de precisão tempora
- Uma fonte libera arquivos de vendas no fim do dia, e outro sistema envia transações contin
- Um sistema precisa construir uma cópia inicial de clientes e, diariamente, trazer apenas r
- Uma sincronização usa apenas WHERE updated_at > último_horário. Um registro é excluído fis
- Dos 200 registros importados, 170 apresentam um telefone preenchido, ainda sem verificação
- Um pipeline rejeita o CEP 'AB123' por violar a regra de cinco dígitos definida no contrato
- Após inspeção de 250 registros, 230 têm data de nascimento não nula. A regra de qualidade 
- Uma chamada grava a venda 78 no destino, mas a confirmação de leitura falha. O evento cheg
- Em uma ingestão, os produtores enviam 600 eventos por segundo e os consumidores tratam 450
- Dois eventos referentes à mesma conta são enviados com versões 31 e 32, mas a versão 32 ch
- Uma base remove nomes e troca CPF por códigos recuperáveis com uma tabela separada de corr
- Em um conjunto, os valores mínimo e máximo de uma variável são 10 e 30. Qual é o resultado
- Uma variável tem média 100 e desvio-padrão 20. Um registro apresenta valor 140. Qual resul
- Uma equipe migra uma tabela com carga inicial e depois passa a consumir CDC. Uma atualizaç
- Pedidos têm cabeçalhos: pedido 1 total 50 com dois itens, pedido 2 total 80 com um item. A
- Antes de dividir a base entre treino e teste, uma equipe calcula a média global e gera obs
- Um consumidor guarda offsets em checkpoint, mas, após gravar no banco externo, cai antes d
- Um pipeline Azure precisa copiar tabelas de um SQL Server acessível apenas na rede interna
- Uma arquitetura precisa hospedar microsserviços com estado replicado, mas utiliza outro se
- Uma equipe remove coluna obrigatória do evento e faz deploy sem testar os consumidores ant
- Em classificação de eventos raros, existem 980 casos comuns e 20 raros. Um modelo prevê ap
- Uma empresa extrai dados de um banco relacional em arquivo diário e recebe eventos de sens
- Um arquivo JSON com objetos aninhados é necessariamente uma tabela relacional e dispensa a
- Depois que uma coluna de receita mudou de definição, consultar a linhagem dos dados pode a
- Uma extração que usa TLS corretamente configurado já mantém os arquivos recebidos criptogr
- Antes de definir uma regra de deduplicação de clientes, uma equipe calcula quantidades de 
- No cadastro importado, um e-mail preenchido é considerado exato apenas porque possui o car
- Uma integração Oracle–SQL Server define explicitamente conversões decimais e fusos de data
- Ao carregar registros brutos em um ambiente analítico e executar as transformações princip
- Em uma tabela SQL Server configurada para CDC, as informações capturadas podem distinguir 
- Como Parquet é colunar, cada linha adicionada por evento em tempo real é obrigatoriamente 
- Em uma distribuição de salários com poucos valores extremamente altos, substituir ausentes
- Ao receber dois cadastros com nomes idênticos, a etapa de matching pode classificá-los com
- Um buffer de ingestão cresce continuamente quando chegam 900 mensagens por segundo e são p
- Em sincronização por timestamp, uma exclusão física de registro é sempre extraída na próxi
- No streaming com janelas por tempo de evento, a definição de atraso permitido pode exigir 
- Uma tabela pseudonimizada por códigos reversíveis pode ser tratada como anônima para qualq
- Ao registrar versão do código, período extraído, contagens, rejeições e identificador da e
- Se o checkpoint do consumidor guarda o offset de uma mensagem, qualquer escrita em banco e
- Uma solução pode usar Azure Data Factory para orquestrar cópias, Azure Databricks para pro
- Ao somar valores de pedidos depois de juntar cada cabeçalho à sua lista de itens, o result
```

## Pedido 49: Ataques, vulnerabilidades e frameworks de segurança (`informatica.ataques-vulnerabilidades-frameworks`, 215 tópicos de edital)

```
Matéria: informatica.ataques-vulnerabilidades-frameworks (Ataques, vulnerabilidades e frameworks de segurança)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ameaças, vulnerabilidades, exposição e risco cibernético": 21
- "2. Ataques a redes, protocolos, aplicações e redes sem fio": 30
- "3. Gestão de vulnerabilidades, endpoints e remediação": 16
- "4. NIST Cybersecurity Framework 1.1, evolução 2.0 e CIS Controls v8": 18
- "5. Segurança e privacidade por padrão: aplicação integrada e avaliação de casos": 15

Já existem (não repita):
- Uma aplicação ficou indisponível após grande volume de solicitações oriundas de milhares d
- A inspeção de um servidor identificou versão desatualizada de um componente, mas não encon
- Após receber mensagem de cobrança com link convincente, uma pessoa informa sua senha em pá
- Uma pessoa abre um registro de outro usuário ao trocar o identificador na URL, embora este
- Uma equipe revisa o NIST Cybersecurity Framework versão 1.1. Qual conjunto contém exatamen
- Em um ambiente corporativo, é preciso implantar ações contínuas de identificação e correçã
- Uma organização quer evitar que senha comum, aplicada a centenas de usuários, permita aces
- Após adoção de uma rede Wi-Fi corporativa, um dispositivo apresenta certificado de autenti
- Uma aplicação registra tentativas sucessivas de autenticação em uma única conta, com muita
- Um servidor DNS devolve um endereço não autorizado para domínio legítimo, embora o serviço
- Uma vulnerabilidade com escore técnico elevado está em laboratório isolado; outra, de esco
- Um sistema foi atualizado após boletim de falha, mas não há evidência de que a correção fo
- Uma equipe cria perfil atual e perfil-alvo de resultados de segurança conforme as necessid
- Uma organização quer reduzir ataques de injeção SQL em uma API que monta consultas a parti
- Em rede corporativa, um dispositivo autorizado conecta-se a ponto de acesso criado sem apr
- No NIST CSF versão 2.0, uma atividade de definir tolerância ao risco, responsabilidades e 
- Um analista precisa implantar detecção e investigação em estações, com telemetria e capaci
- Uma empresa observa sessões TLS válidas com um domínio que imita o nome de sua plataforma.
- Um dispositivo precisa resolver domínios com proteção contra adulteração da resposta, sem 
- Uma vulnerabilidade consta do catálogo KEV e atinge serviço crítico exposto. O gestor nota
- Uma instituição adotou CIS Controls v8 e precisa justificar a escolha de salvaguardas comp
- Uma equipe deseja avaliar os resultados de medidas de defesa simulando exploração controla
- Um fabricante entrega roteador com administração remota habilitada, senha universal e serv
- Em uma investigação, a equipe observa várias sondagens de portas vindas de endereço descon
- Um produto promete segurança e privacidade por padrão. Em seu primeiro acesso, todos os da
- Uma equipe observa indisponibilidade após fluxo intenso vindo de diversos dispositivos ext
- A equipe identificou uma porta TCP aberta; essa descoberta já demonstra que um invasor exe
- Em uma rede corporativa, um ponto de acesso que imita a identificação de rede legítima e i
- Na versão 1.1 do NIST Cybersecurity Framework, a função Govern constitui uma das cinco fun
- Uma equipe que aplica CIS Controls v8 deve reconhecer que o Control 7 trata de gestão cont
- Uma aplicação que apenas solicita senha no início da sessão, sem autorizar cada objeto con
- Depois de aprovar e implantar um patch, a equipe de segurança precisa verificar em quais a
- Uma resposta DNS autenticada por DNSSEC garante que o conteúdo HTTP obtido do endereço res
- Ao encontrar uma falha presente no catálogo de vulnerabilidades com exploração conhecida, 
- Um scanner sem credenciais sempre identifica mais detalhes de pacotes e atualizações insta
- Uma companhia utiliza perfis atual e desejado do NIST CSF para localizar lacunas de result
- Em rede sem fio, ocultar o SSID e utilizar filtro de endereços MAC oferecem autenticidade 
- O NIST CSF 2.0 mantém as funções Identify, Protect, Detect, Respond e Recover e explicita 
- A inclusão de uma CVE no catálogo KEV faz com que a Diretiva BOD 22-01 imponha, automatica
- Um fornecedor que elimina credenciais universais e desativa serviços desnecessários na con
- O emprego de EDR dispensa o gerenciamento de correções, porque todo comportamento nocivo p
- Uma configuração pode proteger dados pessoais contra acesso não autorizado e, mesmo assim,
- No CIS Controls v8, o Control 18 é responsável pela gestão contínua de vulnerabilidades, e
- Um controle temporário que isola sistema sem patch pode reduzir seu risco de exploração, e
- Os Implementation Tiers do NIST CSF equivalem aos grupos IG1, IG2 e IG3 do CIS Controls v8
```

## Pedido 50: Amostragem, correlação e teorema central do limite (`estatistica.amostragem-correlacao-tcl`, 214 tópicos de edital)

```
Matéria: estatistica.amostragem-correlacao-tcl (Amostragem, correlação e teorema central do limite)
Capítulos e quantas questões quero em cada um (total 100):
- "1. População, amostras, planos de seleção e descrição dos dados": 18
- "2. Distribuições conjuntas, covariância e correlação linear": 29
- "3. Distribuições amostrais, erro padrão, estimadores e dependência espacial": 18
- "4. Desigualdade de Tchebycheff e leis dos grandes números": 13
- "5. TCL, aproximação normal da binomial e interpretação da normal": 22

Já existem (não repita):
- Uma pesquisa pretende estimar a renda média de 8.000 domicílios, utilizando 200 domicílios
- Para representar áreas urbanas e rurais, um pesquisador separa a população nesses dois gru
- Quatro medições apresentam valores 2, 4, 4 e 10. Qual trio de medidas corresponde, respect
- Um diagrama de dispersão mostra pontos quase alinhados em reta descendente, de modo que va
- Medições independentes têm desvio padrão populacional 18. Qual é o erro padrão da média de
- Os dados 2, 4 e 6 são tratados como amostra de uma população. Qual é a variância amostral 
- Uma pesquisa de renda pretende representar toda a região, mas entrevista somente pessoas q
- Uma variável tem distribuição aproximadamente normal, média 100 e desvio padrão 10. Qual p
- A distribuição conjunta de X e Y binárias tem P(0,0)=0,10, P(0,1)=0,20, P(1,0)=0,30 e P(1,
- As observações pareadas são X=(1,2,3) e Y=(3,2,1). Qual é a covariância amostral usual, co
- Uma contagem binomial S será aproximada por variável normal contínua Y. Para calcular P(S≤
- A correlação de Pearson amostral entre X e Y é 0,6. Ao substituir Y por 4Y+7, mantendo os 
- Uma variável X é uniformemente distribuída nos valores -1, 0 e 1 e define-se Y=X². Qual af
- Uma população finita possui 100 unidades e o pesquisador observa todas por seleção sem rep
- Duas variáveis têm variâncias amostrais 4 e 9 e covariância amostral -3. Qual coeficiente 
- Um território possui estrato A com 90% das unidades e média verdadeira 100, e estrato B co
- Uma variável tem média 20 e desvio padrão 4, sem hipótese de normalidade. Que proporção mí
- Para medições i.i.d. integráveis de média populacional mu, uma análise distingue lei fraca
- Uma nuvem de pontos quase alinhados contém um único valor extremo muito afastado. O coefic
- Um levantamento ambiental coleta 1.000 pixels adjacentes de uma pequena mancha e calcula e
- Uma variável S tem distribuição binomial com n=100 e p=0,5. Para aproximar P(S≥60) por nor
- Um estimador de parâmetro apresenta viés igual a 2 e variância igual a 9. Qual é seu erro 
- Para variável de forma normal, qual comparação entre Tchebycheff e a regra empírica no int
- Duas variáveis discretas têm P(X=1,Y=1)=0,24 e P(X=1)=0,40. Qual é P(Y=1 dado X=1)?
- Variáveis X e Y são independentes, com E(X)=2, E(Y)=3 e variâncias estritamente positivas 
- Uma amostra de 200 unidades sorteadas de uma população de 5.000 unidades é subconjunto da 
- Uma pesquisa baseada exclusivamente em voluntários garante probabilidades de inclusão conh
- Para uma amostra com observações 1, 3 e 5, a variância amostral não viesada usual vale 8/3
- Uma coleção de observações com correlação de Pearson -0,9 apresenta associação linear nega
- Uma variável de média 30 e desvio padrão 5, mesmo sem distribuição normal, possui no máxim
- Se a população individual tem desvio padrão 20, a média de cem observações independentes e
- Uma variável exatamente normal apresenta aproximadamente 95,45% de sua distribuição entre 
- Um plano estratifica domicílios por região e seleciona o mesmo número em regiões com taman
- Dois levantamentos de mesma média, mediana e número de pontos terão igual qualidade para r
- Se X e Y são independentes e possuem momentos de segunda ordem finitos, a covariância entr
- Pela desigualdade de Tchebycheff, qualquer variável de variância finita deve ter exatament
- Sob condições clássicas de amostragem independente e média finita, a lei fraca dos grandes
- Uma contagem binomial com n=15 e p=0,02 pode receber aproximação normal de precisão garant
- Para observações independentes e identicamente distribuídas de esperança finita, a lei for
- Ao aproximar pela normal a probabilidade de uma binomial assumir exatamente o valor 50, us
- Toda amostra de tamanho 30 produz média com distribuição exatamente normal, mesmo se as ob
- Quando um boxplot sinaliza valor acima de Q3 mais 1,5 vezes a amplitude interquartil, a ex
- Para variável de média e variância finitas, Tchebycheff assegura probabilidade de pelo men
- Se a covariância populacional de duas variáveis com variâncias positivas é zero, a indepen
- Um estimador definido como média amostral mais 1/n pode ser enviesado em amostras finitas 
```

