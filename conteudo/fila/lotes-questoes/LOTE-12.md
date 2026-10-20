# Pedidos de questões extras, lote 12 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 8778 tópicos de edital.

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

## Pedido 1: Clima, hidrografia e vegetação (`geografia.clima-hidrografia-vegetacao`, 194 tópicos de edital)

```
Matéria: geografia.clima-hidrografia-vegetacao (Clima, hidrografia e vegetação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tempo, clima, elementos e fatores climáticos": 20
- "2. Circulação geral da atmosfera, massas de ar e chuvas no Brasil": 22
- "3. Hidrosfera, ciclo da água e redes de drenagem": 20
- "4. Hidrografia amazônica: Acre, Pará e Tocantins-Araguaia": 22
- "5. Vegetação, biomas e integração das paisagens brasileiras": 16

Já existem (não repita):
- Uma pessoa consulta a temperatura de 32 °C registrada em uma tarde; outra analisa médias m
- Em estudo climático de duas cidades, são comparados valores de precipitação e a altitude d
- Uma estação registra máxima diária de 31 °C e mínima de 19 °C. Qual foi a amplitude térmic
- Ar úmido vindo do oceano alcança uma serra, sobe sua encosta, resfria-se e favorece conden
- Em mapa da circulação média planetária, identifica-se ascensão do ar nas baixas latitudes 
- Uma massa fria de origem oceânica polar avança pelo interior sul-americano e causa queda e
- Uma bacia fluvial recebe água de afluentes que convergem para um curso principal. Qual ele
- Um resíduo é lançado em determinado trecho de rio e transportado pela corrente em direção 
- Uma relação oficial dos grandes ambientes naturais do Brasil apresenta Amazônia, Cerrado, 
- No estudo das chuvas brasileiras, uma faixa móvel próxima ao Equador é formada pela conver
- Em período chuvoso, forma-se faixa persistente de nebulosidade que liga porções da Amazôni
- Um rio tem seção transversal de 20 m² e velocidade média de escoamento de 2 m/s. Qual vazã
- Uma análise de planejamento hídrico brasileiro utiliza a divisão oficial da ANA em regiões
- Um estudo de hidrografia do Acre acompanha um afluente que percorre trechos fronteiriços e
- No Acre, duas grandes drenagens têm nascentes em território peruano e, após atravessarem á
- Um mapa de rios paraenses destaca Xingu, Tapajós e Jari e pede verificar as respectivas ma
- Durante visita de campo, uma equipe identifica formações de savana, campos e matas de gale
- Uma massa de ar desloca-se horizontalmente em escala planetária no Hemisfério Sul. Em mode
- Uma cidade costeira e outra interiorana estão em latitudes e altitudes semelhantes. No mod
- Uma bacia urbana teve ampliação de superfícies impermeáveis e retirada de vegetação. Sob c
- Em um atlas, aparecem os rios Acre, Purus e Solimões. Qual sequência reproduz o caminho fl
- Em mapa amazônico, os rios Juruá, Purus, Tapajós e Xingu são apresentados como tributários
- Um trabalho compara duas áreas de floresta amazônica: uma sofre inundações periódicas por 
- Uma equipe encontra planície continental extensa com campos, savanas, vegetação aquática e
- Uma classificação oficial delimita o bioma Amazônia e outra identifica a Região Hidrográfi
- Uma observação de 30 °C durante a tarde descreve condição de tempo; para caracterizar o cl
- Temperatura, precipitação e pressão atmosférica são fatores climáticos geográficos, enquan
- Quando ar úmido é obrigado a ascender uma encosta, o resfriamento pode favorecer chuva oro
- A força aparente de Coriolis desvia movimentos horizontais de ar à direita no Hemisfério S
- Os rios Juruá e Purus têm suas nascentes em território peruano, atravessam áreas do Acre e
- O bioma Pantanal corresponde a floresta equatorial permanentemente seca e sua principal ca
- Uma série histórica pode indicar clima equatorial úmido mesmo que determinados meses receb
- A Zona de Convergência Intertropical e a Zona de Convergência do Atlântico Sul são denomin
- Bacias hidrográficas superficiais são organizadas por áreas drenadas e divisores de águas;
- Na divisão de regiões hidrográficas da ANA, o sistema Tocantins-Araguaia integra administr
- No rio Purus, os rios Acre e Iaco figuram como afluentes pela margem direita; o primeiro e
- No território paraense, Tapajós, Xingu e Jari chegam todos ao Amazonas pela margem direita
- O Cerrado pode apresentar formas savânicas com árvores espaçadas, campos e matas de galeri
- Em análise de clima no Brasil, uma entrada da massa polar atlântica pode causar friagem na
- Em carta hidrográfica, uma bacia superficial e um aquífero subterrâneo aparecem parcialmen
- No mapa dos biomas continentais, manguezal, restinga e várzea aparecem como três biomas ad
- O regime de chuvas influencia vazões, mas as ondas de cheia podem se deslocar ao longo dos
- Como Xingu e Tapajós deságuam na margem direita do Amazonas, ambos devem ser classificados
- Em partes da Caatinga, árvores perdem folhas durante a estação seca e depois retomam ativi
- Se um afluente entra pelo lado esquerdo de um mapa orientado para o norte, ele será obriga
```

## Pedido 2: Interação humano-computador, usabilidade e design centrado no usuário (`informatica.interacao-humano-computador-usabilidade`, 193 tópicos de edital)

```
Matéria: informatica.interacao-humano-computador-usabilidade (Interação humano-computador, usabilidade e design centrado no usuário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de IHC, usabilidade, ergonomia e cognição": 26
- "2. Heurísticas, arquitetura de informação e interfaces web": 22
- "3. Acessibilidade digital: WCAG, eMAG e tecnologia assistiva": 15
- "4. Avaliação de interfaces, pesquisa com usuários e métricas": 18
- "5. Design centrado, métodos ágeis e interação multimodal": 19

Já existem (não repita):
- Em teste de portal, uma pessoa consegue concluir corretamente uma solicitação, mas gasta t
- Após clicar para enviar um formulário, a página permanece imóvel durante vários segundos e
- Um avaliador identifica que a mesma figura de lupa abre busca numa tela e elimina registro
- Uma equipe deseja avaliar a acessibilidade de uma página conforme WCAG 2.2. Quais são os q
- Uma pessoa deseja descobrir como participantes agrupam opções de um catálogo antes de defi
- Ao testar uma nova interface, cinco participantes concluem a tarefa, mas o grupo diz que o
- Um sistema apresenta botão 'Desfazer' logo após um item ser removido por engano, permitind
- Uma interface reúne comandos por toque, fala e teclado para que a pessoa possa fazer a mes
- Uma página tem texto pequeno em cinza claro sobre fundo branco, com razão de contraste de 
- Uma pessoa usa teclado para alcançar um botão representado por um ícone, mas ele não receb
- Uma revisão realizada por especialistas compara cada tela de um protótipo às dez heurístic
- Um relatório de pesquisa apresenta SUS igual a 80 e afirma que o indicador demonstra suces
- Ao avaliar um formulário, a equipe decide pedir que usuários reais tentem emitir comprovan
- Uma equipe usa Double Diamond para redesenhar o cadastro. Após entrevistas, sintetiza a ne
- Uma equipe entrega quatro telas novas e anuncia que isso já comprova melhoria de UX. De ac
- Um ambiente de projeto usa Delegation Poker para definir participação da equipe nas decisõ
- Durante avaliação de página com botão criado em elemento genérico, alguém adiciona `role=b
- Uma interface usa alvo de 20 × 20 pixels CSS para ação de baixo risco, com espaço livre su
- Um avaliador sustenta que o eMAG 3.1 adota a mesma organização em níveis A, AA e AAA da WC
- Em teste com doze pessoas, nove concluem tarefa. A interface também recebeu SUS 82. Que en
- Uma equipe quer descobrir se mudar o texto de um botão aumenta conclusões reais, em vez de
- Ao planejar pesquisa qualitativa, alguém afirma que cinco participantes garantem margem de
- Uma avaliação encontra dificuldades para usuários de voz em ambiente ruidoso e para pessoa
- Na evolução de interface, foram construídos protótipos antes de entrevistar usuários, e ob
- Um formulário usa placeholder para o único nome de campo; após digitar, a pessoa esquece q
- Uma pessoa realiza tarefa corretamente e em pouco tempo, mas relata grande insatisfação co
- O conceito de experiência do usuário é idêntico ao de usabilidade e pode ser medido comple
- Uma interface que informa ao usuário que o arquivo foi salvo aplica o princípio de visibil
- Uma avaliação heurística exige, como etapa indispensável, observar participantes represent
- As WCAG organizam as orientações por perceptível, operável, compreensível e robusto, assoc
- Ao montar um menu, o card sorting serve principalmente para medir o tempo necessário para 
- Um serviço que aceita comandos por voz ou toque, com respostas visuais coerentes, utiliza 
- Em uma página, todo texto comum com contraste de 3:1 em relação ao fundo atende, sem depen
- Uma interface que depende apenas de um marcador vermelho para indicar erro pode criar barr
- O eMAG 3.1 classifica suas recomendações nos mesmos níveis A, AA e AAA da WCAG e, por isso
- Uma pesquisa relata SUS igual a 88 e sucesso observado em sete de dez tentativas. É metodo
- Na abordagem Lean UX, entregar novos componentes de interface já constitui outcome mensura
- O Double Diamond intercala exploração e convergência: primeiro aprofunda a compreensão do 
- Pela WCAG 2.2, critério 2.5.8 (AA), um alvo interativo medindo 20 × 20 pixels CSS está nec
- A simples atribuição de `role=button` a um elemento genérico não lhe confere, por si, foco
- A recomendação de aproximadamente cinco participantes em certas rodadas qualitativas de us
- No percurso cognitivo, avaliadores examinam, passo a passo, se uma pessoa com certo conhec
- Em Management 3.0, o Delegation Poker organiza decisões em dez níveis correspondentes aos 
- Segundo a Lei nº 13.146/2015, art. 63, a acessibilidade de sítios de internet alcançados p
- Um serviço de agendamento com interface gestual cumpre integralmente acessibilidade apenas
```

## Pedido 3: Lei de lavagem de dinheiro: aspectos penais e processuais (`legislacao-penal-especial.lavagem-dinheiro`, 191 tópicos de edital)

```
Matéria: legislacao-penal-especial.lavagem-dinheiro (Lei de lavagem de dinheiro: aspectos penais e processuais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tipo penal, infração antecedente e fases da lavagem": 18
- "2. Tipificação equiparada, dolo, autolavagem e penas": 19
- "3. Autonomia processual, competência, citação e obtenção de dados": 21
- "4. Medidas assecuratórias, alienação antecipada e administração patrimonial": 24
- "5. Efeitos da condenação, cooperação internacional e jurisprudência aplicada": 18

Já existem (não repita):
- Ao investigar patrimônio registrado em nome de terceiro, verificou-se que o valor provenie
- Após a Lei nº 12.683/2012, valores oriundos de contravenção penal passam por operação dolo
- Um investigado responde por lavagem de valores cujo antecedente está sendo apurado em proc
- Uma questão pede a sanção básica do caput do art. 1º da Lei nº 9.613/1998, sem majorantes 
- Na apuração de lavagem, o autor alega que a quantia nunca ingressou em empresa regular, em
- O autor de fraude recebe diretamente seu proveito e depois registra os valores sob contrat
- Uma lavagem é cometida por meio da utilização de ativo virtual. Considerando o art. 1º, §4
- Na representação da autoridade policial para constrição de bens vinculados à lavagem, qual
- Uma denúncia versa sobre lavagem associada a infração antecedente da competência da Justiç
- Um investigado recebe citação por edital em ação de lavagem, não comparece nem constitui a
- A polícia, com fundamento apenas no art. 17-B da Lei nº 9.613/1998, solicita informação se
- Um veículo apreendido por ligação com lavagem apresenta forte desvalorização durante guard
- Durante a alienação antecipada de bem avaliado em 200 mil reais, qual é o lance mínimo per
- Uma condenação por lavagem fixa pena privativa de liberdade de quatro anos. Qual período c
- Após indiciamento de servidor público por lavagem, a administração afirma que o art. 17-D 
- Após pedido de autoridade estrangeira e sem tratado aplicável, o governo solicitante prome
- Segundo o art. 1º, §5º, um participante espontaneamente presta informações úteis à localiz
- Em condenação por lavagem proferida no âmbito da Justiça Estadual, qual destinatário do pe
- Em acusação por lavagem, o agente produziu contratos simulados após receber propina, mas s
- Uma denúncia atribui lavagem a parente do autor de fraude, mas não narra movimentação atri
- Um processo de alienação antecipada de bens constritos foi iniciado em autos próprios. O m
- O juiz pretende determinar medidas assecuratórias de bens em nome de interposta pessoa, li
- Uma autoridade estrangeira solicita cautelar patrimonial no Brasil sem tratado, havendo pr
- Um órgão lê o art. 17-B e entende que dados cadastrais e conteúdo bancário podem ser requi
- Em condenação de lavagem que tramita na Justiça do Distrito Federal, o julgador destina to
- Uma denúncia descreve ocultação da propriedade de imóvel obtido com valores de contravençã
- A prática de lavagem somente se consuma quando os recursos ilícitos completam, sucessivame
- Um investigado transferiu dolosamente bens derivados de corrupção para titular fictício, c
- A Lei nº 9.613/1998 estabelece modalidade culposa de lavagem para quem deixa de conferir d
- Se a lavagem é praticada de modo reiterado, por intermédio de organização criminosa ou por
- Em ação penal por lavagem, o acusado citado por edital não comparece nem constitui advogad
- Um bem sujeito a medida assecuratória corre risco relevante de depreciação. A Lei nº 9.613
- O processo de lavagem independe do julgamento do antecedente, mas a denúncia deve trazer i
- A extinção da punibilidade da infração antecedente impede o recebimento da denúncia de lav
- A autoridade policial e o Ministério Público podem, com fundamento no art. 17-B, acessar s
- A redução por colaboração espontânea do art. 1º, §5º, constitui aumento de pena de um a do
- O art. 4º-A, §3º, permite leilão antecipado por valor mínimo de 75% da avaliação, depois d
- A mera transferência de recursos por banco privado torna toda ação de lavagem da competênc
- Segundo entendimento sintetizado pelo STJ em Jurisprudência em Teses nº 166, a lavagem pra
- Quando lavagem e antecedente têm o mesmo autor, a consunção é obrigatória ainda que haja c
- A Lei nº 15.358/2026 alterou a regra de destinação de bens perdidos por lavagem, distingui
- A decisão do STF na ADI 4911 confirmou a constitucionalidade do afastamento automático de 
- Na ausência de tratado ou convenção, a promessa de reciprocidade pode permitir cautelar po
- Segundo o art. 4º-B, o juiz pode adiar execução de medida assecuratória quando a diligênci
- Uma pessoa investe recursos em criptomoeda por motivo lícito e com origem documental compr
```

## Pedido 4: ECA: política de atendimento, Justiça da Infância e acesso à justiça (`legislacao.eca-politica-atendimento-justica-infancia`, 191 tópicos de edital)

```
Matéria: legislacao.eca-politica-atendimento-justica-infancia (ECA: política de atendimento, Justiça da Infância e acesso à justiça)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação histórica e fundamentos internacionais da proteção integral": 19
- "2. Política de atendimento, municipalização e conselhos de direitos": 16
- "3. Entidades, programas, acolhimento e fiscalização": 16
- "4. Acesso à justiça, competência, procedimentos e defesa": 21
- "5. Tutela coletiva, SINASE, vagas e proteção contra a violência": 28

Já existem (não repita):
- Uma comissão propõe restringir serviços de proteção apenas a crianças abandonadas, usando 
- Em audiência sobre uma pessoa de 17 anos, surge dúvida sobre sua classificação etária. Pel
- Uma gestão decide que a prioridade absoluta apenas assegura atendimento preferencial na re
- Uma criança pretende manifestar sua opinião em procedimento administrativo que pode afetar
- Uma equipe quer utilizar instrumento internacional dedicado especialmente à prevenção da d
- Um município quer implementar atendimento de crianças em risco em colaboração com redes es
- Em reforma municipal, propõe-se substituir o Conselho dos Direitos por órgão exclusivament
- Uma criança necessita de acesso urgente a serviço de saúde, mas a equipe local afirma que 
- Um município cria fundo vinculado ao Conselho de Direitos, mas questiona se esse mecanismo
- Um plano local incorpora os eixos de promoção, defesa e controle dos direitos da infância.
- Uma entidade municipal mantém programa de acolhimento e pretende inscrevê-lo perante o órg
- Uma associação possui registro ainda vigente, mas seu programa não é reavaliado há três an
- Uma entidade não governamental recebeu registro municipal para funcionar. Segundo o ECA, q
- Em inspeção, constatou-se violação grave em entidade de atendimento não governamental. Qua
- Uma criança permanece acolhida e a equipe afirma que a revisão do seu caso acompanha a rea
- Uma família sem recursos pede advogado para proteger judicialmente os direitos de adolesce
- Adolescente teria praticado ato infracional em cidade distinta do domicílio familiar. Para
- Duas pessoas responsáveis discutem guarda sem descrição de ameaça ou violação dos direitos
- A autoridade edita portaria proibindo genericamente adolescentes em qualquer espetáculo pú
- Em procedimento do ECA, o Ministério Público defende contagem de dias úteis e prazo em dob
- Uma associação de proteção da infância pretende ajuizar ação civil coletiva. Qual requisit
- Um serviço de semiliberdade encaminha pedido para alocar adolescente em unidade. Na discip
- Adolescente recebeu prestação de serviços à comunidade, mas o município alega inexistência
- Uma criança relata violência à equipe de assistência. Para planejar proteção, a profission
- Responsáveis comunicam o desaparecimento de criança e encontram orientação para aguardar u
- Uma criança de dez anos atendida em unidade pública é titular dos direitos previstos no EC
- A Convenção sobre os Direitos da Criança de 1989 foi promulgada pelo Brasil por meio da Le
- O interesse superior da criança, previsto no art. 3º da Convenção de 1989, deve ser consid
- Como as Regras de Havana versam sobre prevenção da delinquência juvenil, elas substituem a
- O dever de municipalizar o atendimento pode coexistir com financiamento e coordenação real
- Um Conselho Tutelar passa a decidir o orçamento do fundo municipal por ser o órgão deliber
- Um Conselho Municipal dos Direitos da Criança e do Adolescente pode ser classificado como 
- Uma rede local adota a Resolução CONANDA nº 113/2006 como fundamento exclusivo da gestão d
- A inscrição de programas de atendimento no conselho municipal e o registro prévio de entid
- O limite ordinário de dezoito meses de permanência em acolhimento institucional obriga o j
- A fiscalização das entidades de atendimento prevista no art. 95 do ECA envolve Judiciário,
- A cassação do registro é medida expressamente listada no art. 97 do ECA para entidades gov
- Uma entidade privada que possui registro válido por quatro anos continua sujeita à reavali
- Por aplicar subsidiariamente o processo civil, o art. 152 do ECA assegura ao Ministério Pú
- Uma ação judicial de competência da Justiça da Infância e Juventude é, em regra, isenta de
- Uma portaria da Justiça da Infância pode proibir, de modo genérico e sem avaliação individ
- Nas ações de proteção de direitos coletivos da infância, o foro do local da ação ou omissã
- O Plano Individual de Atendimento do SINASE dispensa participação do adolescente e de seus
- A Lei nº 13.431/2017 diferencia escuta especializada feita pela rede para finalidade prote
- Como a Central de Vagas é disciplinada por ato do CNJ, sua coordenação é competência do ju
```

## Pedido 5: Solos, culturas e mecanização agrícola (`agropecuaria.culturas-solos-mecanizacao`, 190 tópicos de edital)

```
Matéria: agropecuaria.culturas-solos-mecanizacao (Solos, culturas e mecanização agrícola)
Capítulos e quantas questões quero em cada um (total 100):
- "Diagnóstico, física, química e conservação dos solos": 17
- "Fertilidade, calagem, gessagem e adubação racional": 17
- "Culturas de grãos, olericultura e fruticultura": 17
- "Irrigação e manejo da água na produção": 17
- "Mecanização agrícola: máquinas, regulagem e desempenho": 16
- "Extensão rural, assistência técnica e desenvolvimento sustentável": 16

Já existem (não repita):
- A textura do solo corresponde, principalmente, à proporção relativa de areia, silte e argi
- Uma gleba com alta disponibilidade química de nutrientes deve necessariamente pertencer à 
- A coleta de subamostras em talhões muito distintos, seguida de mistura numa única amostra 
- O sistema plantio direto envolve, além de restrição do revolvimento, cobertura permanente 
- O gesso agrícola e o calcário são produtos perfeitamente intercambiáveis, pois ambos neutr
- Em um fertilizante rotulado 10-20-10, os números representam, em geral, percentuais de N, 
- Uma análise superficial que indique acidez permite dispensar qualquer avaliação da subsupe
- Em adubação verde com gramíneas de palhada de alta relação carbono/nitrogênio, pode ocorre
- A olericultura compreende a produção de hortaliças, incluindo espécies exploradas comercia
- A densidade ideal de plantas de milho é uma constante fixa para qualquer cultivar e ambien
- A escolha do arranjo de um pomar perene deve considerar o crescimento futuro da copa e da 
- Um milímetro de lâmina de água distribuído uniformemente sobre um hectare corresponde ao v
- Quando a lâmina líquida requerida é de 20 mm e a eficiência de aplicação considerada é de 
- Alta uniformidade de distribuição de água não garante, por si só, elevada eficiência de ap
- A capacidade de campo efetiva de uma operação mecanizada tende a ser menor que a capacidad
- O lastro máximo de um trator produz sempre o menor consumo de combustível e a menor compac
- A mesma máquina pode apresentar capacidades de campo efetivas diferentes em glebas de tama
- Na legislação federal brasileira, ATER é tratada apenas como consultoria técnica eventual,
- O número de visitas técnicas realizadas é um indicador de execução ou processo, mas, sozin
- O diagnóstico rural participativo deve excluir os saberes dos agricultores, pois somente o
- Em um talhão, o técnico deseja identificar a propriedade que pode ser alterada pelo tráfeg
- Uma equipe pretende coletar amostras para recomendar adubação em quatro glebas com solos e
- Em área argilosa, a análise química indica nutrientes adequados, porém há erosão em encost
- Qual combinação caracteriza os fundamentos do sistema plantio direto, em vez de simplesmen
- Uma recomendação agronômica identifica a necessidade de corrigir a acidez do solo antes do
- Um calcário apresenta PRNT diferente do considerado na recomendação agronômica original. N
- Para uma área serão aplicados 250 kg/ha de fertilizante 08-20-12. Qual é a quantidade nomi
- Uma cultura apresenta palhada de gramínea com elevada relação carbono/nitrogênio, recém-in
- Uma área de soja dispõe de baixo histórico de fixação biológica de nitrogênio e será semea
- Na classificação agronômica das hortaliças, a alface é mais frequentemente explorada comer
- Uma propriedade cultiva milho em ambientes com disponibilidades hídricas e níveis de ferti
- Uma cooperativa está elaborando projeto de implantação de pomar de manga irrigado para ope
- No manejo de água do feijoeiro, que períodos merecem atenção especial diante de déficit hí
- Para uma área de um hectare, a evapotranspiração da cultura foi estimada em 4 mm ao longo 
- Num exercício simplificado, a cultura necessita de lâmina líquida de 24 mm e o sistema tem
- Um sistema distribui água com uniformidade elevada, mas o operador frequentemente supera a
- Uma propriedade com pomar avalia implantar gotejamento. Qual cuidado operacional é especia
- Uma semeadora trabalha com largura útil de 4 metros e velocidade de 5 km/h, sem considerar
- Uma operação tem CCT de 2,5 ha/h e eficiência de campo de 80%. Qual é a capacidade efetiva
- Em um trator com patinagem elevada, consumo crescente e sinais de compactação, qual medida
- Durante a manutenção de um implemento acionado por cardã, qual é a conduta correta para re
- Considerando a Lei nº 12.188/2010, como se caracteriza a Assistência Técnica e Extensão Ru
- Uma equipe de ATER pretende definir prioridades em comunidade com produtores diversos. Qua
- Um programa de extensão realizou cem visitas técnicas e informa ter elevado a renda dos ag
- Em uma ação de assistência técnica e extensão rural sobre irrigação e adubação, qual estra
```

## Pedido 6: Duração do trabalho: jornada, horas extras e compensação (`direito-trabalho.jornada-horas-extras`, 190 tópicos de edital)

```
Matéria: direito-trabalho.jornada-horas-extras (Duração do trabalho: jornada, horas extras e compensação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Limites constitucionais, jornada normal e turnos de revezamento": 16
- "2. Controle de ponto, tempo à disposição e deslocamentos": 15
- "3. Horas extras, adicional, prorrogação e tempo parcial": 20
- "4. Compensação de jornada, banco de horas e escala 12×36": 34
- "5. Exceções do art. 62, teletrabalho e aplicação integrada": 15

Já existem (não repita):
- Uma pessoa contratada no regime geral de jornada pergunta quais são os limites normais est
- Uma hora normal vale R$ 20 e, sem compensação válida, uma hora extraordinária deve ser pag
- Uma pessoa alterna turnos matutinos e noturnos em regime efetivo de revezamento, sem norma
- Uma unidade emprega 24 trabalhadores e precisa organizar o controle dos horários. Segundo 
- Após a alteração do art. 58, § 2º, da CLT, uma pessoa gasta cinquenta minutos em transport
- Um contrato de tempo parcial prevê 30 horas semanais. Qual das alternativas descreve a pos
- Uma empresa deseja estabelecer banco de horas com compensação em até seis meses apenas med
- Uma organização deseja instituir jornada de doze horas de trabalho por trinta e seis de de
- Uma pessoa contratada para trabalho remoto presta tarefas por produção, com modalidade con
- No mesmo dia, um empregado registra entrada quatro minutos antecipada e saída sete minutos
- Por escolha própria, um trabalhador chega vinte minutos antes para estudar na sala de desc
- Uma equipe realiza banco de horas cujo período de compensação é de dez meses. Qual forma d
- Uma pessoa se desliga com seis horas extraordinárias positivas no banco, não compensadas a
- Uma compensação apresenta defeito formal, mas as horas prestadas além da duração diária fo
- Um empregado parcial contratado por 26 horas realiza mais quatro horas naquela semana. Que
- Um diretor exerce efetivos poderes de gestão, mas sua remuneração total supera em 30% o sa
- Uma norma coletiva fixa oito horas em turnos ininterruptos e um parecer cita a Súmula 423 
- Uma empresa estabelece teletrabalho por jornada, das 9h às 18h, com marcações eletrônicas 
- Uma empregadora deseja compensar horas dentro do mesmo mês por ajuste individual tácito. A
- Em um banco de horas corretamente instituído, há prestação habitual de horas extraordinári
- Após interrupção do trabalho por força maior, a empresa propõe recuperação de tempo perdid
- Um vendedor trabalha externamente, mas deve registrar início e fim de atendimentos em apli
- Uma empresa mantém controle de jornada por exceção em vez de registrar todos os horários n
- Com regime parcial de até 26 horas semanais, uma pessoa realiza cinco horas suplementares 
- Em compensação formalmente irregular, a semana efetiva atingiu 47 horas para trabalhador s
- Uma pessoa em contrato regido pela jornada comum trabalha oito horas diárias e quarenta e 
- Em regra, hora extraordinária sem compensação deve receber adicional mínimo de 25% sobre a
- Um estabelecimento possui vinte e dois trabalhadores. Pelo art. 74, § 2º, da CLT, em regra
- Após a reforma de 2017, o trajeto da residência até o posto de trabalho é automaticamente 
- O regime de trabalho a tempo parcial de até trinta horas semanais, definido no art. 58-A, 
- Uma escala de doze horas de trabalho por trinta e seis de descanso pode ser instituída por
- Uma marcação ocorre quatro minutos antes do início e outra quatro minutos após o fim do ho
- Uma empresa tem quatro estabelecimentos com sete trabalhadores em cada um. Somando vinte e
- Se a rescisão ocorrer antes da compensação integral do saldo positivo de horas extraordiná
- Em compensação formalmente irregular, a existência de 47 horas trabalhadas na semana de um
- Um banco de horas foi constituído regularmente e registra prestação habitual de horas extr
- A condição de vendedor externo, anotada no contrato, basta para afastar o regime de duraçã
- Uma pessoa exerce poderes reais de direção e recebe salário de confiança pelo menos quaren
- Em turno ininterrupto de revezamento, a Constituição fixa seis horas, mas a negociação col
- Uma escala 12×36 pactuada por acordo individual escrito tem previsão expressa no art. 59-A
- Depois da Lei nº 14.442/2022, o art. 62, III, exclui indiscriminadamente todo empregado em
- O art. 74, § 4º, da CLT admite registro de ponto por exceção à jornada regular com acordo 
- Se a troca de uniforme é exigida nas instalações por determinação do empregador, o período
- Na recuperação do tempo perdido por interrupção acidental ou força maior, o art. 61, § 3º,
- As Súmulas 366 e 423 permanecem vigentes e devem ser aplicadas em 2026 como enunciados atu
```

## Pedido 7: Ética e estatuto jurídico da magistratura (`direito.etica-estatuto-magistratura`, 190 tópicos de edital)

```
Matéria: direito.etica-estatuto-magistratura (Ética e estatuto jurídico da magistratura)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais, garantias e estatuto da magistratura": 13
- "2. Código de Ética: independência, imparcialidade, publicidade e comportamento": 27
- "3. Deveres, vedações, prerrogativas e gestão do trabalho judicial": 13
- "4. Responsabilidade disciplinar, civil e criminal e sanções em 2026": 26
- "5. Administração judicial, modernização, dados e integridade": 21

Já existem (não repita):
- Um juiz de primeiro grau iniciou a carreira há dezoito meses e pergunta quando adquirirá v
- Uma magistrada vitalícia sofre procedimento disciplinar por conduta grave. Qual exigência 
- Um integrante da magistratura pretende manter outra função remunerada, compatível com horá
- Depois de se aposentar, um juiz quer exercer advocacia perante o tribunal do qual se afast
- Um juiz recebe pressão de terceiro interessado para alterar resultado de processo que está
- Uma parte pede audiência individual com magistrado. O art. 9º do Código de Ética admite es
- Um julgador participa de debate televisivo e é questionado sobre processo ainda pendente d
- Na inspeção de uma unidade, são encontrados atrasos de decisões e falhas de fiscalização s
- Uma parte sofreu dano e demonstra que o juiz, no exercício funcional, agiu deliberadamente
- Uma unidade judicial programa análise de resultados de seu planejamento estratégico vigent
- Uma magistrada é convidada a administrar empresa na qual possui cotas sociais. Qual leitur
- Uma parte afirma que audiência concedida separadamente a advogada adversária configura, po
- Um juiz é criticado por interpretação jurídica reformada em recurso, sem notícia de fraude
- Em outubro de 2026, uma pessoa compara o art. 42 da LOMAN com o art. 3º atualizado da Reso
- Um PAD contra magistrado chega ao julgamento em outubro de 2026. Considerando a Resolução 
- Durante investigação de infração funcional ligada a violência contra mulher, a defesa pret
- Uma pessoa requer que juiz determine providência devida e, sem justo motivo, o requeriment
- Uma administração pretende avaliar promoção por merecimento contando apenas sentenças prof
- Em 10 de outubro de 2026, gestor compara as estratégias nacionais do Judiciário de 2021–20
- Um tribunal recebe representação por demora injustificada de magistrado e também recurso c
- Depois de condenação administrativa à disponibilidade com proposta de perda do cargo em tr
- Na sistemática aprovada em agosto de 2026, o CNJ confirma decisão de disponibilidade com p
- Uma decisão administrativa do tribunal aplica disponibilidade com proposta de perda do car
- Uma comissão avalia a Resolução nº 135/2011 conforme a redação publicada em setembro de 20
- Um tribunal adota ferramenta automatizada para selecionar processos e pretende medir suces
- Um juiz de primeiro grau com dois anos completos de exercício pode adquirir vitaliciedade,
- A inamovibilidade constitucional garante que nenhum magistrado possa ser removido por inte
- O Código de Ética da Magistratura exige tanto independência diante de pressões externas qu
- Um juiz está dispensado do dever de urbanidade em relação a testemunhas e servidores porqu
- A Lei Orgânica da Magistratura prevê, em seu art. 41, proteção às opiniões e ao teor das d
- A regra de transparência do Código de Ética permite publicar dados de processos protegidos
- A LOMAN permite participação societária do magistrado na condição de acionista ou quotista
- A simples reforma de uma sentença por tribunal superior comprova, por si só, dolo do julga
- O art. 20 da Resolução CNJ nº 135/2011 prevê julgamento de processo administrativo discipl
- A Estratégia Nacional do Poder Judiciário 2027–2032 substituiu, desde julho de 2026, os co
- No regime da Resolução CNJ nº 135/2011, atualizado pela Resolução nº 693/2026, a disponibi
- A nova redação do art. 3º da Resolução CNJ nº 135/2011, introduzida em agosto de 2026, man
- Quando determinada decisão regional aplica disponibilidade com proposta de perda do cargo 
- Nos procedimentos disciplinares que apurem violência contra mulher, a autoridade responsáv
- O requisito de dez dias do parágrafo único do art. 143 do CPC refere-se à ausência de apre
- O art. 21 da Resolução CNJ nº 135/2011 exige voto de dois terços de todos os membros para 
- Para aferir merecimento, a Resolução CNJ nº 106/2010 considera também a qualidade do desem
- Como a independência judicial é princípio do Código de Ética, o CNJ não pode fiscalizar de
- Embora a Resolução CNJ nº 692/2026 tenha sido publicada durante o ciclo 2021–2026, ela dis
- A divulgação de estatísticas de produtividade pela administração judiciária autoriza publi
```

## Pedido 8: Administração financeira: planejamento e alavancagem (`administracao-geral.administracao-financeira-alavancagem`, 189 tópicos de edital)

```
Matéria: administracao-geral.administracao-financeira-alavancagem (Administração financeira: planejamento e alavancagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos do planejamento financeiro e orçamento de caixa": 20
- "2. Capital de giro, ciclo financeiro e decisões de curto prazo": 19
- "3. Planejamento de longo prazo, financiamento e avaliação de investimentos": 19
- "4. Alavancagem operacional, custos fixos e ponto de equilíbrio": 21
- "5. Alavancagem financeira e total, risco e decisões integradas": 21

Já existem (não repita):
- Uma empresa começa o mês com R$ 15.000, prevê recebimentos de R$ 25.000 e pagamentos de R$
- Ao comparar o orçamento original de vendas com o desempenho do trimestre, a administração 
- Uma companhia adquire à vista uma máquina que será usada durante vários anos. Pela classif
- O ativo circulante de uma organização soma R$ 200.000 e o passivo circulante, R$ 150.000. 
- Uma loja apresenta prazo médio de estocagem de 25 dias, recebimento de clientes em 40 dias
- Um produto é vendido por R$ 90, com custo e despesa variáveis de R$ 55 por unidade. Qual m
- Uma fábrica tem custos e despesas operacionais fixos de R$ 18.000 e margem unitária de R$ 
- São vendidas 1.000 unidades a R$ 80 cada; cada unidade gera gastos variáveis de R$ 50 e há
- Uma atividade apresenta margem de contribuição total de R$ 45.000 e EBIT positivo de R$ 15
- A empresa possui R$ 100.000 em contas a receber e R$ 60.000 em estoques, além de R$ 40.000
- Uma empresa inicia a semana com R$ 12.000, espera receber R$ 28.000 e pagar R$ 35.000. Sua
- Uma empresa mantém PME de 30 dias e PMR de 40 dias, mas renegocia o PMP de 20 para 35 dias
- Uma organização utiliza 60% de capital próprio, com custo de 12%, e 40% de dívida a 8%, co
- Uma aplicação exige R$ 100.000 hoje e retorna R$ 60.000 ao final de cada um dos próximos d
- Um negócio vende 800 unidades; seu ponto de equilíbrio operacional é de 600 unidades. Qual
- Uma empresa tem EBIT de R$ 60.000 e juros fixos de R$ 15.000, sem outros encargos financei
- Em determinado nível de produção, o GAO é 2,5 e o GAF é 1,2. Sob o modelo em que ambos os 
- No mês, uma empresa reconhece receita de R$ 100.000 em vendas a prazo, cuja cobrança será 
- Um investimento de R$ 1.000.000 gera EBIT anual de R$ 120.000 e é financiado com R$ 600.00
- No ponto de equilíbrio operacional exato, a margem de contribuição total equivale aos gast
- Uma instalação A tem fixos de R$ 12.000 e margem unitária de R$ 30; a B tem fixos de R$ 30
- Um fornecedor oferece '2/10, líquido 30': há desconto de 2% para pagamento em dez dias ou 
- Dois projetos têm payback simples de dois anos. Após esse marco, o primeiro produz fluxos 
- Uma empresa apresenta MC de R$ 40.000, EBIT de R$ 20.000 e juros fixos de R$ 5.000. Mantid
- Uma avaliação considera fluxos de caixa livres já após os pagamentos da dívida aos credore
- Uma projeção de caixa com recebimentos de R$ 30.000, pagamentos de R$ 24.000 e saldo inici
- Uma receita de venda a prazo, contabilizada em setembro e com pagamento esperado em novemb
- Uma empresa com ativo circulante de R$ 90.000 e passivo circulante de R$ 70.000 apresenta 
- O ciclo financeiro equivale à soma de estocagem, recebimento e pagamento a fornecedores, p
- Em modelo de produto único, com preço de R$ 70 e gasto variável unitário de R$ 45, a marge
- No ponto de equilíbrio operacional, a ausência de EBIT implica necessariamente grau de ala
- No modelo dinâmico de capital de giro, se CCL e NCG são calculados com classificações cons
- No cálculo tradicional do WACC com dedutibilidade fiscal integral dos juros, a parcela de 
- Para um investimento convencional, VPL positivo à taxa mínima escolhida indica que o valor
- O payback simples considera o custo de oportunidade em cada ano e todos os recebimentos po
- Com EBIT de R$ 100.000 e juros fixos de R$ 25.000, sem outros encargos fixos relevantes, a
- Se os graus operacional e financeiro forem respectivamente 2 e 1,5, a alavancagem total se
- Uma empresa pode ter GAO elevado por custos operacionais fixos e GAF igual a 1 no modelo s
- O EBITDA representa exatamente o caixa operacional disponível para pagar empréstimos, pois
- Num modelo linear com GAO igual a 2 e custos fixos e unitários inalterados, aumento de 10%
- Uma organização eleva o prazo médio de pagamento a fornecedores de 25 para 45 dias, manten
- Uma automação que aumenta gastos fixos e reduz gasto variável unitário pode aumentar a ala
- O financiamento por dívida sempre aumenta a rentabilidade dos proprietários, mesmo que o r
- Sob o modelo sem outros encargos fixos, MC de R$ 60 mil, EBIT de R$ 30 mil e juros de R$ 1
- Cobertura de juros de quatro vezes, calculada como EBIT dividido por despesa de juros, com
```

## Pedido 9: Brasil: território, Estado e divisão regional (`geografia.brasil-territorio-estado`, 188 tópicos de edital)

```
Matéria: geografia.brasil-territorio-estado (Brasil: território, Estado e divisão regional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estado, nação, soberania, território e fronteiras": 21
- "2. Formação histórica e expansão do território brasileiro": 22
- "3. Organização político-administrativa e escalas do poder público": 17
- "4. Regionalização do IBGE: Grandes Regiões e redes urbanas": 21
- "5. Desigualdades territoriais e planejamento regional brasileiro": 19

Já existem (não repita):
- Uma comunidade preserva idioma, memória e costumes próprios, mas seus integrantes vivem di
- Em reunião sobre políticas públicas, alguém afirma que um município brasileiro tem soberan
- Uma equipe de pesquisa precisa reconhecer os componentes político-administrativos autônomo
- O mapa político atual mostra Tocantins separado de Goiás. Em qual das cinco Grandes Regiõe
- Um mapa exibe o Distrito Federal com Goiás, Mato Grosso e Mato Grosso do Sul, agrupando-os
- Um estudante compara mapas do espaço colonial português e do Brasil contemporâneo. Qual tr
- No estudo de um acordo de 1903, um atlas histórico registra que uma área antes sob soberan
- Um pesquisador encontra estudo estatístico recente que utiliza 'Regiões Geográficas Imedia
- Uma política territorial pretende enfrentar diferenças de acesso a serviços entre dois mun
- Em mapa da Amazônia Legal, o Mato Grosso e parte do Maranhão aparecem no recorte. Como int
- Uma análise do Tratado de Madri, de 1750, cita o princípio de 'uti possidetis'. Qual noção
- Um projeto de desmembramento estadual é proposto por representantes de parte da população.
- Dois municípios mantêm fluxo diário de estudantes e consumidores em direção a um centro qu
- Uma capital apresenta altos indicadores econômicos, mas municípios de sua própria região c
- Um grupo de planejamento consulta o Decreto nº 11.962/2024 para definir a tipologia territ
- Uma nota pública trata da PNDR instituída pelo Decreto nº 11.962/2024. Qual formulação res
- Uma pesquisadora compara o território brasileiro do Império com a estrutura federativa atu
- Um documento de planejamento delimita a faixa de fronteira terrestre brasileira como área 
- Um projeto de município novo pretende formalizar seu desmembramento. Segundo o art. 18, § 
- Uma região de atuação da Sudene inclui municípios de Minas Gerais e Espírito Santo. Que co
- Uma região reúne vários centros locais que recorrem a uma cidade de maior alcance para ser
- Uma política regional quer reduzir dependência excessiva de uma metrópole, fortalecendo ce
- Um atlas descreve a expansão agrícola para o interior de um estado sem alterar os limites 
- Um relatório histórico afirma que o atual Tocantins se originou do norte de Goiás e que a 
- Um estudo compara renda média das cinco Grandes Regiões e conclui que municípios de determ
- Uma coletividade pode formar uma nação por laços históricos e culturais sem possuir um Est
- Segundo a Constituição de 1988, as cinco Grandes Regiões do IBGE, além dos estados e munic
- Tocantins, embora tenha sido criado a partir de parte do território de Goiás, integra a Re
- Mato Grosso do Sul pertence à Grande Região Sul, pois a denominação oficial do estado dete
- O Tratado de Petrópolis, de 1903, integra a história de formação territorial do Brasil por
- O Distrito Federal pode ser dividido em municípios autônomos caso a lei orgânica distrital
- Desigualdades entre municípios da mesma Grande Região são classificadas como intrarregiona
- A Amazônia Legal coincide integralmente com a Região Norte do IBGE, pois suas políticas te
- Na regionalização apresentada pelo IBGE em 2017, as Regiões Geográficas Imediatas se relac
- O Tratado de Madri, de 1750, restabeleceu sem alterações a linha de Tordesilhas, fazendo d
- A Constituição estabelece que territórios federais integram a União, não figurando entre o
- A PNDR prevista no Decreto nº 11.962/2024 atua somente em escala macrorregional, sendo as 
- A área de atuação da Sudene alcança, além dos estados nordestinos, municípios especificado
- A criação de um novo estado, segundo o art. 18, § 3º, pode ser realizada por lei estadual 
- O Decreto nº 11.962/2024 estabelece que a tipologia referencial da PNDR use o recorte das 
- Como o estado do Maranhão pertence à Região Nordeste, nenhuma parcela de seu território in
- A divisão do IBGE de 2017 reorganizou recortes sub-regionais para divulgação e análise, ma
- A faixa de fronteira prevista na Constituição corresponde obrigatoriamente a uma área de 1
- A formação de uma rede policêntrica de cidades é compatível com a PNDR, pois permite distr
- Como os estados brasileiros possuem autonomia política, cada um deles exerce soberania int
```

## Pedido 10: Informática em saúde (`informatica.sistemas-saude-ti`, 187 tópicos de edital)

```
Matéria: informatica.sistemas-saude-ti (Informática em saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos dos sistemas de informação em saúde e uso de dados": 17
- "Prontuário eletrônico, PEC e estratégia e-SUS APS": 17
- "Bases da APS, produção ambulatorial e hospitalar": 17
- "Vigilância epidemiológica: SINAN, SIM e SINASC": 17
- "Interoperabilidade, RNDS e arquitetura da informação": 16
- "LGPD, sigilo, segurança e governança de dados em saúde": 16

Já existem (não repita):
- Um Sistema de Informação em Saúde reúne processos, pessoas e tecnologias, não se limitando
- A contagem mensal de atendimentos é sempre equivalente ao número de cidadãos atendidos, po
- A completude é uma dimensão da qualidade do dado relacionada ao preenchimento dos campos e
- A informatização de uma unidade de saúde elimina automaticamente a subnotificação e todos 
- O prontuário eletrônico pode registrar evoluções, prescrições, exames e encaminhamentos de
- A Coleta de Dados Simplificada é equivalente ao prontuário clínico completo e dispensa qua
- Ter um prontuário eletrônico em funcionamento garante, por si, interoperabilidade semântic
- O Ministério da Saúde apresenta o Siaps como substituto do Sisab na organização dos dados 
- O e-Gestor APS é, essencialmente, uma aplicação de registro integral de evolução clínica q
- A Autorização de Internação Hospitalar é tradicionalmente associada ao SIH/SUS, enquanto o
- O SINAN registra apenas casos laboratorialmente confirmados, sendo vedada a notificação de
- A Declaração de Óbito é o documento básico de entrada para o SIM.
- O SINASC toma como evento central o nascimento de criança viva e utiliza a Declaração de N
- A taxa de mortalidade infantil pode ser construída combinando óbitos de menores de um ano 
- O uso de uma API padronizada dispensa regras organizacionais, responsabilidades e fundamen
- A RNDS constitui uma infraestrutura nacional voltada ao compartilhamento padronizado e seg
- Pseudonimização e anonimização significam, necessariamente, que a identidade do titular ja
- Como dados pessoais de saúde são sensíveis, a LGPD exige consentimento em todas as hipótes
- Pela Lei nº 13.787/2018, completar vinte anos do último registro obriga o estabelecimento 
- Uma planilha pública com diagnósticos nominais pode violar princípios de finalidade e nece
- Uma secretaria de saúde quer usar registros de consultas, notificações e internações para 
- Uma UBS ainda usa fichas para parte dos registros e deseja diferenciar o módulo CDS do PEC
- Uma equipe lançou atendimentos no prontuário e transmitiu um lote, mas os dados não consta
- Considerando a atualização oficial da infraestrutura de dados da APS, como se caracteriza 
- Um gestor estadual deseja acessar sistemas e informações gerenciais da APS conforme seu pe
- Qual associação entre sistema e instrumento de produção hospitalar é usualmente cobrada em
- Uma auditoria pretende examinar predominantemente a produção de procedimentos ambulatoriai
- Em uma unidade assistencial, surge suspeita de um agravo cuja regra vigente exige notifica
- Um serviço precisa consolidar óbitos e estudar suas causas para vigilância epidemiológica.
- O planejamento materno-infantil exige identificar nascidos vivos e características associa
- Para calcular a mortalidade infantil em um município, a equipe obtém óbitos de menores de 
- Dois softwares transmitem o mesmo exame com códigos padronizados, unidade de medida e inte
- Um desenvolvedor prepara integração de resultados laboratoriais com a RNDS. Qual padrão co
- Uma clínica passa a usar uma solução conectada à RNDS. Qual conclusão é defensável?
- Um sistema consegue enviar dados via API, mas o receptor interpreta valores laboratoriais 
- Um hospital trata dados de saúde para atender obrigação legal aplicável, sem pedir consent
- Uma base substitui nomes por códigos, mas mantém chave secreta que permite recuperar ident
- Uma instituição pretende digitalizar e eliminar prontuários de papel. Qual providência se 
- Uma unidade percebe que uma conta de acesso compartilhada foi usada para visualizar prontu
- Uma prova apresenta: I. SIH/SUS se associa à produção hospitalar; II. SINASC usa a DNV; II
```

## Pedido 11: Regimento interno e código de ética de TRTs (`processo-trabalho.regimento-interno-codigo-etica-trt`, 187 tópicos de edital)

```
Matéria: processo-trabalho.regimento-interno-codigo-etica-trt (Regimento interno e código de ética de TRTs)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Natureza dos regimentos e estrutura da Justiça do Trabalho": 12
- "2. Regimento da 3ª Região: composição, competências e procedimentos": 30
- "3. Regimento da 24ª Região: direção, colegiados e recursos": 12
- "4. Regimento da 17ª Região e comparação entre modelos regionais": 16
- "5. Códigos de ética da 3ª e da 24ª Regiões: deveres, vedações e apuração": 30

Já existem (não repita):
- Uma pessoa estuda a estrutura do TRT da 3ª Região e encontra informações desencontradas so
- Durante reunião, um integrante pergunta quem compõe o Tribunal Pleno da 3ª Região. Qual re
- Ao preparar uma eleição interna, a secretaria precisa identificar a composição do Órgão Es
- Um material de consulta informa que o TRT da 24ª Região possui oito cadeiras. Qual distrib
- Em 2026, alguém utiliza um regimento de 2011 para estudar a 17ª Região. Qual referência de
- Uma pessoa recém-admitida na 3ª Região consulta o código ético vigente em outubro de 2026.
- A Presidência da 24ª Região constitui sua Comissão de Ética. Qual formação segue a norma p
- Um servidor da 3ª Região identifica conflito entre interesse privado e atribuição funciona
- Na 3ª Região, uma Turma reúne sua composição completa de quatro desembargadores para um ju
- O Presidente da 3ª Região prepara uma sessão plenária ordinária e precisa comunicar gabine
- Na 24ª Região, um processo eletrônico em PJe-JT chega ao fim de seu prazo recursal. Como a
- Após a eleição de direção da 24ª Região em ano par, uma pessoa confunde a data da posse co
- Na 3ª Região, a pessoa encarregada da governança de uma contratação identifica risco de fa
- Uma autoridade gerencial da 24ª Região mantém participação de 6% no capital de uma institu
- A Comissão de Ética da 24ª Região examina uma representação contra servidor. Qual afirmaçã
- Em uma sessão do Tribunal Pleno da 3ª Região, discute-se o quórum de instalação e a regra 
- Na 24ª Região, um processo administrativo sem relator designado chega a julgamento. Qual r
- Uma pessoa da 3ª Região recebe de interessado em sua atuação um item com valor comercial d
- Uma descrição da 3ª Região foi produzida antes da reestruturação prevista no Ato Regimenta
- O setor de protocolo da 24ª Região recebe autos recursais em PJe-JT. Um servidor pretende 
- Na eleição da 24ª Região, um integrante argumenta que o prazo de sessenta dias do art. 10 
- A Comissão de Ética da 24ª Região deve apreciar assunto que envolve primo de primeiro grau
- Um pesquisador encontra informação de que o regimento da 17ª Região foi aprovado em setemb
- Um projeto de resolução interna pretende atribuir ao Órgão Especial da 3ª Região competênc
- Uma pessoa compara os recursos disciplinados no regimento consolidado da 24ª Região. Qual 
- Uma pessoa que consulta o Regimento da 3ª Região verifica que a sede do tribunal fica em B
- Ao distribuir competências na 24ª Região, uma proposta atribuiu funções ao seu Órgão Espec
- Uma servidora da 24ª Região identifica, durante o serviço, ato lesivo à Administração e co
- Em setembro de 2026, o Código de Ética da 3ª Região permaneceu disciplinado pela Resolução
- Na 24ª Região, o Presidente acumula as atribuições do Corregedor Regional, enquanto o Vice
- A Turma da 3ª Região que possui quatro desembargadores deverá reunir a participação dos qu
- Na 3ª Região, pelo menos um terço dos membros do Tribunal Pleno pode requerer convocação d
- No PJe-JT da 24ª Região, o art. 175 determina a baixa automática à origem três dias úteis 
- Uma estagiária da 3ª Região foi informada de que, embora não ocupe cargo efetivo, o Código
- Na 24ª Região, o Presidente pode designar três servidores ocupantes exclusivamente de carg
- No regimento da 24ª Região, o Tribunal Pleno reúne a totalidade dos seus desembargadores, 
- Na convocação de sessão plenária da 3ª Região, comunicação aos gabinetes com cinco dias co
- Um integrante efetivo e estável da Comissão de Ética da 24ª Região pode cumprir mandato de
- Durante exame de conduta na 24ª Região, a Comissão de Ética poderá conhecer denúncia anôni
- O servidor da 3ª Região que reconhece circunstância capaz de afetar sua imparcialidade dev
- No Código de Ética da 3ª Região de 2026, a instauração de procedimento disciplinar por pos
- Em processo administrativo do Pleno da 24ª Região sem relator designado, o Presidente prof
- Um estudo sobre processo de 2026 na 17ª Região cita o art. 42 da RA nº 8/2011 como norma v
- A inclusão da Seção Especializada em Uniformização de Jurisprudência na organização da 3ª 
- Uma autoridade gerencial da 24ª Região que detenha exatamente cinco por cento do capital d
```

## Pedido 12: Segurança de Infraestrutura de Rede: Firewall, IDS, IPS, SIEM, ZTNA, PAM, VPN, Web Proxy e NGAV (`informatica.seguranca-infraestrutura-rede-ferramentas`, 185 tópicos de edital)

```
Matéria: informatica.seguranca-infraestrutura-rede-ferramentas (Segurança de Infraestrutura de Rede: Firewall, IDS, IPS, SIEM, ZTNA, PAM, VPN, Web Proxy e NGAV)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Firewall e web proxy": 25
- "2. IDS e IPS": 25
- "3. VPN e ZTNA": 25
- "4. SIEM, PAM e NGAV": 25

Já existem (não repita):
- O firewall stateful mantém uma tabela de conexões e permite o tráfego de retorno de sessõe
- A característica que distingue um firewall de nova geração (NGFW) de um firewall stateful 
- A política mais segura para um firewall é permitir todo o tráfego e bloquear apenas o que 
- A sub-rede separada por firewall, onde ficam servidores que precisam ser acessados pela in
- Nas listas de regras de firewall, as regras costumam ser avaliadas em ordem, e a primeira 
- O proxy que fica na frente dos servidores web, recebendo as requisições externas e fazendo
- O web proxy encaminhado permite filtrar o acesso dos usuários por URLs e categorias de sit
- Para inspecionar o conteúdo de tráfego HTTPS, o web proxy precisa:
- O firewall de filtro de pacotes sem estado analisa cada pacote isoladamente, sem considera
- Sobre IDS e IPS, é correto afirmar que:
- Por atuar em linha, um IPS mal configurado pode bloquear tráfego legítimo e afetar a dispo
- O método de detecção que compara o tráfego com padrões de ataques conhecidos e tem dificul
- A detecção por anomalia pode identificar ataques desconhecidos, mas tende a gerar mais fal
- Um ataque real que passa sem ser detectado pelo IDS caracteriza:
- O HIDS é executado no próprio host e monitora eventos como alterações de arquivos, registr
- Para que um NIDS analise o tráfego sem ficar no caminho dele, costuma-se utilizar:
- O IDS, por analisar o conteúdo do tráfego, dispensa o uso de firewall na rede.
- No IPsec, o protocolo que oferece confidencialidade, além de integridade e autenticação, é
- A VPN site a site interliga redes inteiras, como matriz e filiais, por meio de gateways na
- No túnel dividido (split tunneling) de uma VPN:
- Uma limitação clássica da VPN de acesso remoto é que, após a conexão, o usuário frequentem
- A principal diferença do ZTNA em relação à VPN tradicional é que o ZTNA:
- No ZTNA, as aplicações ficam ocultas da internet e só são visíveis para usuários e disposi
- No ZTNA, a decisão de conceder acesso considera, entre outros fatores:
- A VPN SSL/TLS é bastante usada no acesso remoto por navegador ou por cliente leve, enquant
- A ferramenta que coleta logs de diversas fontes, normaliza os dados e correlaciona eventos
- O SIEM, por si só, bloqueia automaticamente os ataques que identifica, substituindo o fire
- A correlação de eventos no SIEM permite, por exemplo:
- O PAM é a solução voltada ao controle de contas com privilégios elevados, como administrad
- É recurso típico de uma solução de PAM:
- O acesso privilegiado just-in-time concede permissões elevadas apenas pelo tempo necessári
- O NGAV se diferencia do antivírus tradicional por:
- O NGAV é voltado principalmente à prevenção no endpoint, enquanto o EDR enfatiza a detecçã
- Um ataque sem arquivo (fileless), que usa ferramentas legítimas do sistema, tende a ser ma
- O SIEM pode receber como uma de suas fontes os alertas gerados por IDS e IPS.
- Uma organização quer impedir que funcionários acessem sites de jogos e registrar a navegaç
- Um administrador precisa de uma solução que bloqueie em tempo real ataques conhecidos no t
- No IPsec, o modo túnel encapsula o pacote IP inteiro, sendo comum em VPNs entre gateways.
- A ferramenta que intermedeia o acesso dos usuários à web, podendo bloquear categorias de s
- O IPS pode atuar tanto na rede (NIPS) quanto no host (HIPS), e o mesmo vale para o IDS.
```

## Pedido 13: Alienação fiduciária, condomínio edilício e SFH (`direito-civil.garantias-alienacao-fiduciaria-condominio-sfh`, 185 tópicos de edital)

```
Matéria: direito-civil.garantias-alienacao-fiduciaria-condominio-sfh (Alienação fiduciária, condomínio edilício e SFH)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Alienação fiduciária móvel e Decreto-Lei nº 911/1969": 17
- "2. Alienação fiduciária imobiliária: registro, mora e leilões": 21
- "3. Condomínio edilício: partes, convenção, quóruns e síndico": 17
- "4. Incorporação imobiliária e patrimônio de afetação": 17
- "5. Sistema Financeiro da Habitação, SFI e integração dos institutos": 28

Já existem (não repita):
- Uma consumidora financia um automóvel e continua utilizando-o, embora tenha transferido pr
- Após execução de liminar de busca e apreensão de automóvel com alienação fiduciária, o com
- Uma instituição quer constituir propriedade fiduciária sobre apartamento oferecido como ga
- Após registrar título fiduciário de imóvel, a empresa explica ao devedor como a posse se d
- Um proprietário pretende alienar um corredor de uso comum do edifício separadamente do apa
- Um condomínio recém-instituído pretende aprovar sua convenção original. Qual mínimo de sub
- Antes de vender frações ideais vinculadas a apartamentos que ainda serão construídos, a in
- Dois contratos financiam imóveis: um está voltado à casa própria em sistema habitacional; 
- Um credor comprova que enviou notificação extrajudicial ao endereço indicado no contrato f
- Executada liminar de busca e apreensão de automóvel, o devedor optou por apresentar contes
- Credor de financiamento fiduciário imobiliário constatou inadimplemento e o contrato não f
- Averba-se a consolidação da propriedade fiduciária imobiliária e não houve quitação. Qual 
- Uma empresa pretende mudar a destinação de unidades de residencial para comercial e altera
- O síndico não convocou a assembleia anual para prestação de contas, apesar de obrigação ex
- A incorporadora registrou seu memorial, mas deseja separar ativos da obra do restante do p
- Uma operação imobiliária comercial usa alienação fiduciária da Lei 9.514, mas não é financ
- O financiamento de veículo foi retomado por liminar, e o devedor paga integralmente o débi
- Uma instituição vende diretamente veículo fiduciário recuperado por inadimplemento, não ha
- Financiamento para aquisição de imóvel residencial do próprio devedor, fora do sistema de 
- A propriedade fiduciária de apartamento já se consolidou no credor e ainda não foi realiza
- Uma pessoa adquire apartamento com débitos antigos de cotas condominiais, incluindo juros 
- As cotas condominiais de determinado mês foram pagas em atraso. A convenção é omissa quant
- O incorporador está insolvente, mas um empreendimento seu tinha patrimônio de afetação con
- Uma construtora entregou relatórios à Comissão de Representantes e movimenta receitas da i
- Uma operação usa dinheiro de linha habitacional para aquisição de residência e constitui a
- No financiamento fiduciário de veículo, o devedor fiduciante é, em regra, possuidor direto
- Com a execução da liminar de busca e apreensão de automóvel, o devedor pode pagar somente 
- Para alienação fiduciária imobiliária, o registro do contrato no Registro de Imóveis compe
- Depois de intimado pelo registro de imóveis para purgar mora em garantia imobiliária, o fi
- A convenção original do condomínio edilício deve receber a subscrição de titulares de ao m
- O síndico do condomínio edilício deve ser titular de uma unidade no edifício e não pode se
- A alienação de frações ideais vinculadas a futuras unidades de incorporação exige prévio r
- A ação de busca e apreensão do Decreto-Lei 911 exige comprovação de que o próprio devedor 
- Na alienação fiduciária de imóvel, a falta de prazo contratual de carência resulta em praz
- O credor que consolida propriedade fiduciária imobiliária pode vender livremente o imóvel 
- A alteração da destinação de edifício ou unidade em condomínio edilício exige aprovação de
- O proprietário que compra unidade edilícia com débitos condominiais do alienante não respo
- O patrimônio de afetação imobiliária é facultativo ao incorporador e constitui-se por aver
- O Sistema Financeiro Imobiliário, previsto na Lei 4.380/1964, é destinado principalmente à
- No Decreto-Lei 911, pagar a integralidade da dívida dentro dos cinco dias contados da exec
- Depois da Lei 14.711/2023, leilão imobiliário que não satisfaça a dívida extingue obrigato
- O devedor cuja propriedade fiduciária imobiliária já foi consolidada em nome do credor pod
- Em condomínio edilício, a multa por inadimplemento de cota pode chegar a dez por cento do 
- Na falência do incorporador, patrimônio de afetação regularmente constituído não integra a
- O contrato de compra de residência pelo SFH não admite propriedade fiduciária imobiliária 
```

## Pedido 14: Saúde coletiva: trabalho em saúde, educação permanente e redes de atenção (`saude-publica.saude-coletiva-trabalho-vivo-redes`, 185 tópicos de edital)

```
Matéria: saude-publica.saude-coletiva-trabalho-vivo-redes (Saúde coletiva: trabalho em saúde, educação permanente e redes de atenção)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Saúde coletiva brasileira e micropolítica do trabalho vivo em ato": 17
- "2. Educação permanente em saúde e quadrilátero da formação": 17
- "3. Apoio matricial, clínica ampliada e projeto terapêutico singular": 17
- "4. Redes de Atenção à Saúde, coordenação e continuidade do cuidado": 17
- "5. Saúde mental coletiva, território, cuidado em liberdade e interdisciplinaridade": 16
- "6. Saúde digital, proteção de dados e participação social no SUS": 16

Já existem (não repita):
- No referencial de Merhy, acolhimento, vínculo e responsabilização integram o campo das tec
- Por definição, um aparelho de ultrassonografia é tecnologia leve-dura, pois seu uso exige 
- O trabalho vivo em ato pode reconfigurar rotinas e instrumentos previamente produzidos sem
- Um serviço é usuário-centrado sempre que aumenta a quantidade de procedimentos realizados 
- A educação permanente em saúde procura relacionar aprendizagem e análise dos problemas enc
- No quadrilátero da formação de Ceccim e Feuerwerker, os vértices são ensino, assistência h
- Um treinamento técnico pode compor uma estratégia de educação permanente quando decorre de
- Contar apenas os certificados emitidos é suficiente para comprovar transformação do proces
- O apoio matricial procura ampliar a capacidade das equipes de referência mediante comparti
- A mera transferência de uma pessoa à especialidade, sem diálogo ou acompanhamento, já cara
- O PTS admite revisão de metas e responsabilidades quando a situação clínica ou as condiçõe
- Na organização das redes de atenção, a APS exerce papel prioritário de coordenação do cuid
- A contrarreferência consiste exclusivamente em devolver uma solicitação recusada ao serviç
- A presença de sistema de regulação, por si só, assegura integração das RAS, independenteme
- O cuidado em liberdade e a atuação comunitária são orientações fundamentais da saúde menta
- A desinstitucionalização estará completa se uma pessoa deixar o hospital psiquiátrico, ain
- A redução de danos pode integrar o PTS ainda quando a pessoa não adota abstinência como me
- Informações sobre a saúde de pessoa identificada são dados pessoais sensíveis para fins de
- A LGPD exige consentimento expresso do paciente como única base possível para todo tratame
- As deliberações registradas no relatório de uma conferência de saúde podem orientar políti
- Uma profissional acolhe uma pessoa com receio do diagnóstico, escuta suas prioridades e pa
- Ao analisar o ato de interpretar um resultado laboratorial com base em fisiopatologia e ep
- Uma unidade passa a cumprir rígidos tempos de atendimento, ignorando dificuldades dos usuá
- Em uma análise comparativa, qual enunciado expressa melhor a centralidade do usuário na pr
- Qual conjunto contém, sem substituições, os quatro vértices do quadrilátero da formação em
- Após identificar erros recorrentes nas altas, duas equipes analisam casos, testam nova com
- Um hospital oferece 40 cursos obrigatórios no ano, sem identificar problemas do trabalho o
- Em discussão de EPS, representantes dos usuários apontam barreiras de horário não percebid
- Qual ação ilustra mais diretamente apoio matricial em saúde mental?
- Em um projeto terapêutico singular, qual sequência expressa os quatro momentos didáticos f
- Uma pessoa com multimorbidade não consegue seguir condutas porque falta transporte. Em clí
- Sobre a equipe de referência e os apoiadores matriciais, é adequado afirmar que:
- O retorno à UBS de informações clínicas do especialista, permitindo seguir o tratamento, e
- A APS é descrita, nas RAS, como porta de entrada preferencial. Qual interpretação está cor
- Um município dispõe de UBS, ambulatório e hospital, mas não há fluxos pactuados nem retorn
- Um usuário com doença crônica passa pelo pronto atendimento e retorna à equipe territorial
- No cuidado em saúde mental coletiva, a abordagem territorial significa principalmente:
- Uma pessoa em sofrimento intenso procura espontaneamente um CAPS. Qual afirmação correspon
- Em um PTS relacionado ao uso problemático de álcool, a pessoa não deseja abstinência imedi
- Uma equipe propõe alta hospitalar de pessoa há muitos anos internada sem providências de m
- Qual conjunto descreve boas práticas diante de prontuários eletrônicos com dados sensíveis
- Um município justifica uso de dados sensíveis apenas com a frase “é para a saúde”. Que aná
- A RNDS promove intercâmbio padronizado de dados entre sistemas. Qual conclusão é correta?
- Sobre a 17ª Conferência Nacional de Saúde e seu relatório, a interpretação adequada é:
- Em um conselho local, usuários e profissionais discutem falhas de acesso a consultas e enc
```

## Pedido 15: Regulamentos federais de contratação: SRP, credenciamento e INs (`direito-administrativo.regulamentos-federais-contratacao-irs`, 183 tópicos de edital)

```
Matéria: direito-administrativo.regulamentos-federais-contratacao-irs (Regulamentos federais de contratação: SRP, credenciamento e INs)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei nº 14.133/2021, planejamento, PNCP e transição": 19
- "2. Sistema de registro de preços e Decreto nº 11.462/2023": 21
- "3. Credenciamento, inexigibilidade e Decreto nº 11.878/2024": 19
- "4. INs SEGES nº 65/2021 e nº 73/2022: preço e disputa eletrônica": 21
- "5. IN nº 5/2017, parcerias, padronização e sustentabilidade": 20

Já existem (não repita):
- Um contrato decorrente de licitação foi assinado e deverá ser divulgado no PNCP para adqui
- Uma ata federal de registro de preços foi divulgada no PNCP e se pretende manter seus preç
- Uma rede de prestadores recebe inscrições para atender simultaneamente pessoas em condiçõe
- Uma pesquisa dispõe de três preços válidos e comparáveis: R$ 100, R$ 110 e R$ 120. Qual mé
- Em contratação de serviços sob a Lei nº 14.133/2021, a equipe discute se ainda pode empreg
- O gerenciador do registro de preços planeja divulgar a intenção de registro para que outro
- Uma contratação por inexigibilidade é formalizada diretamente e deve ser divulgada no PNCP
- Durante a fiscalização de serviço com dedicação exclusiva de mão de obra, deseja-se confer
- Uma contratação federal de bem comum será licitada eletronicamente com julgamento pelo mai
- Em um chamamento, os beneficiários do serviço escolherão livremente, entre os prestadores 
- Uma ata registra 200 unidades de um item para gerenciador e participantes. Sem exceção esp
- Há 150 unidades registradas de material para gerenciador e participantes. Admitidas várias
- Em pesquisa de preços de computadores, existem valores em sistema oficial, contratações pú
- Uma parceria envolve transferência voluntária de recursos federais mediante instrumento de
- Ao atualizar material sobre credenciamento, percebe-se que o art. 79 da Lei nº 14.133 cont
- Um não participante obteve anuência do fornecedor e autorização do gerenciador para aderir
- Em licitação eletrônica pelo menor preço, foi escolhido modo de disputa aberto da IN SEGES
- No planejamento de uma ata destinada a um único contratante, não haverá outros participant
- Após pesquisa diligente, uma contratação obtém apenas dois preços comparáveis. O responsáv
- Um edital eletrônico escolhe modo aberto e fechado, com fase aberta seguida de aviso de fe
- Por urgência reconhecida, um contrato foi assinado e a prestação precisa começar imediatam
- Uma prestadora executa serviços com mão de obra dedicada fora do imóvel do contratante, se
- Uma prova apresenta quatro hipóteses atuais de credenciamento do art. 79 e atribui a regul
- Uma aquisição frequente envolve obra de engenharia padronizada, sem complexidade técnica e
- Um contrato celebrado validamente sob o regime licitatório anterior permanece em execução 
- Uma ata de registro de preços, regularmente formada, pode servir a futuras contratações se
- Uma pessoa atendeu a todos os requisitos do edital de credenciamento e recebeu a condição 
- Uma pesquisa reúne preços comparáveis de R$ 80, R$ 90 e R$ 100. A mediana é R$ 90 e é um d
- O contrato de contratação direta foi assinado na terça-feira. O prazo ordinário de divulga
- Em uma contratação federal, o credenciamento e o sistema de registro de preços devem ser c
- A IN nº 5/2017 estabelece que os trabalhadores da empresa contratada passam a ter vínculo 
- A adesão de um não participante a uma ata federal exige demonstração de preço de mercado c
- Se uma ata possui 100 unidades de item originalmente registradas, cinco aderentes podem ad
- Num credenciamento paralelo, se nem todos os habilitados puderem ser convocados para a mes
- Na IN SEGES/ME nº 65/2021, a pesquisa direta com três fornecedores tem prioridade normativ
- Em certame federal com julgamento por menor preço, a IN SEGES/ME nº 73/2022 admite forma p
- O Decreto nº 11.531/2023 aplica-se também, sem ressalva, a todo termo de fomento e termo d
- Em contrato urgente, o art. 94, § 1º, da Lei nº 14.133 admite eficácia desde a assinatura,
- Em 2026, o art. 79 da Lei nº 14.133 prevê comércio eletrônico no Sicx, e o Decreto nº 11.8
- Uma contratação direta federal foi assinada após procedimento de credenciamento. A publica
- O gerenciador de registro de preços deve, em regra, divulgar intenção de registro por ao m
- No modo aberto e fechado da IN SEGES/ME nº 73/2022, após quinze minutos a sessão encerra t
- Quando o preço estimado é formado exclusivamente pelo parâmetro do art. 5º, I, da IN nº 65
- Um contrato firmado validamente antes da vigência da Lei nº 14.133 pode continuar submetid
- A existência de serviço de engenharia padronizado, sem complexidade operacional e com dema
```

## Pedido 16: Arrendamentos, subvenções e pronunciamentos específicos do CPC (`contabilidade.arrendamento-subvencoes-cpc-especificos`, 183 tópicos de edital)

```
Matéria: contabilidade.arrendamento-subvencoes-cpc-especificos (Arrendamentos, subvenções e pronunciamentos específicos do CPC)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Arrendamentos: identificação, mensuração e apresentação": 22
- "2. Subvenções, assistência governamental e saldos históricos": 19
- "3. Imóveis para investimento, venda de ativos e ativos biológicos": 16
- "4. Valor presente, empréstimos, eventos e mensurações específicas": 26
- "5. Receitas, seguros, concessões e relato integrado": 17

Já existem (não repita):
- Uma indústria contrata por quatro anos o uso de empilhadeira de número de série especifica
- Uma sociedade aluga máquinas e, como arrendadora, conserva substancialmente riscos e benef
- Uma entidade contrata por dez meses uma pequena impressora sem opção de compra. Deseja uti
- Uma fábrica tem promessa de receber auxílio público de R$ 300 mil, condicionado à conclusã
- Uma entidade recebe subvenção ligada à compra de máquina produtiva. A subvenção atende aos
- No balanço de uma companhia ainda há saldo identificado como ativo diferido referente a 31
- Uma sociedade possui prédio alugado a terceiros para auferir renda, sem intenção de venda 
- Uma máquina está disponível para venda imediata, a venda é altamente provável e o CPC 31 é
- Uma produtora rural acompanha videiras destinadas a fornecer uvas durante vários anos e ta
- Após a data do balanço, um cliente entra em falência. Evidências indicam que sua insolvênc
- Uma avaliadora encontra preço de mercado ativo, acessível na data de mensuração, para ação
- Uma empresa vende a prazo por R$ 121 mil, recebíveis em dois anos. A taxa efetiva aplicáve
- Uma entidade constrói instalação complexa que levará 18 meses para estar pronta. Durante a
- Uma despesa é contabilizada no ano atual, mas só poderá ser deduzida da base tributável qu
- Uma companhia patrocina plano de aposentadoria que promete benefício futuro calculado pelo
- Uma entidade vende equipamento com serviço de manutenção distinto, ambos aproveitáveis sep
- Uma seguradora vende proteção anual com transferência significativa de risco de seguro e r
- Uma concessionária constrói e opera infraestrutura cujo concedente regula serviço e tarifa
- Uma administradora afirma que o relato integrado deve transformar o capital humano e o cap
- Um arrendatário controla equipamento por cinco anos e paga aluguel fixo mais 3% de suas ve
- Uma entidade reconheceu subvenção de R$ 150 mil destinada a cobrir despesas futuras. Antes
- Uma rede aliena apenas um veículo operacional, mas mantém a mesma linha de negócios e a me
- Uma entidade que mensura imóveis locados pelo modelo do valor justo decide ocupar parte de
- Uma entidade possui preço indicativo de imóvel inferido por avaliação que usa premissas re
- Uma entidade concluiu praticamente todas as atividades necessárias para colocar máquina co
- Uma locadora disponibiliza à cliente uma máquina identificada por número de série durante 
- Uma transportadora pode substituir livremente o veículo durante todo o contrato, dispõe de
- Uma empresa recebe ajuda pública para compensar gastos de qualificação ao longo de dois pe
- Uma sociedade tinha reserva de reavaliação constituída antes das mudanças de 2007. O art. 
- Uma entidade mantém terreno para valorização e posterior alienação eventual, sem desenvolv
- Uma máquina foi corretamente classificada como mantida para venda em 30 de junho. Mesmo ap
- Em atividade pecuária, gado vivo destinado à produção é ativo biológico, enquanto o leite 
- Uma entidade reconhece direito de R$ 110 mil a receber em um ano, com componente financeir
- Uma companhia inicia construção de planta industrial que levará tempo substancial para ope
- Após a data do balanço, uma fábrica sofre incêndio inesperado, sem evidência de dano ou ca
- Uma entidade reconhece contabilmente obrigação dedutível apenas quando liquidada em exercí
- Uma entidade mantém plano de benefício definido e registra ganhos atuariais de remensuraçã
- Uma prestadora fornece ao cliente um serviço contínuo cujo benefício é recebido e consumid
- Uma seguradora emite grupo de contratos onerosos, mas decide reuni-los a contratos lucrati
- Uma entidade recebe do governo direito de uso de terreno sem desembolso, condicionado à in
- Um contrato de arrendamento de oito meses contém opção de compra do equipamento pelo arren
- Em concessão cujo concedente regula serviços e conserva interesse residual na infraestrutu
- Uma organização prepara relato integrado e apresenta apenas indicadores financeiros. Afirm
- Uma empresa classifica equipamento como mantido para venda, mas circunstâncias excepcionai
- Uma entidade adota o modelo do valor justo para propriedade para investimento e registra a
```

## Pedido 17: Medicina Legal: introdução, perícias e documentos médico-legais (`medicina-legal.introducao-pericias-documentos`, 181 tópicos de edital)

```
Matéria: medicina-legal.introducao-pericias-documentos (Medicina Legal: introdução, perícias e documentos médico-legais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, evolução e áreas da medicina legal": 20
- "2. Perícia, peritos, assistentes técnicos e ética": 16
- "3. Corpo de delito, exames complementares e preservação": 15
- "4. Documentos médico-legais, prontuários, atestados e ética documental": 34
- "5. Modelos de laudos, quesitos e perícias administrativas": 15

Já existem (não repita):
- Uma equipe de investigação pede a um médico que esclareça tecnicamente a origem e a extens
- Um médico perito estuda deveres éticos de imparcialidade e sigilo em sua atuação profissio
- Em investigação de agressão, médico examina pessoalmente lesões ainda presentes no braço d
- Em localidade com perito oficial habilitado e portador de diploma superior, a autoridade r
- Em investigação de dano material, a autoridade encontra objetos com marcas relacionadas à 
- Em um caso com vestígios preservados, o investigado confessa a prática do fato e propõe di
- Uma instituição solicita documento para comprovar que paciente compareceu a consulta, sem 
- Familiares recebem Declaração de Óbito preenchida pelo médico e, em seguida, buscam docume
- Um perito descreve minuciosamente os achados de exame e responde aos quesitos, entregando 
- Uma pessoa requer acesso aos dados de atendimentos e exames mantidos por hospital em regis
- Não existe perito oficial disponível para exame técnico de vestígios em determinada locali
- A defesa indica profissional para examinar material utilizado por perito oficial, pretende
- Os peritos solicitam prorrogação do tempo de elaboração de laudo em caso excepcional devid
- Peritos encontram sinais inequívocos de morte e justificam tecnicamente antecipação de aut
- No primeiro exame, não foi possível concluir se lesão incapacitou a pessoa para ocupações 
- Dois peritos designados apresentam conclusões incompatíveis sobre um exame. A autoridade r
- Uma parte solicita que perito oficial compareça à audiência para responder a quesitos e es
- Há simultaneamente exame de vestígios relacionados a violência doméstica contra mulher e o
- Um hospital pretende eliminar prontuários após decorridos cinco anos do último atendimento
- Médico assistente de paciente pretende atuar também como seu perito isento para preencher 
- Em infração que normalmente deixa vestígios, a cena foi modificada irreversivelmente e os 
- Em exame de sexologia forense realizado depois de algum tempo, não há lesões genitais demo
- Um óbito sem causa natural esclarecida chega a serviço de verificação de óbitos; no exame,
- Um médico emite atestado de afastamento e o paciente solicita expressamente inclusão do di
- Uma perícia criminal envolve aspectos médicos, genéticos e de engenharia, cada qual com qu
- Durante estudo de medicina legal, afirma-se que a tanatologia aborda fenômenos relacionado
- Uma autoridade pede perícia criminal havendo um perito oficial com diploma superior. O CPP
- Um crime deixa marcas visíveis em ferramenta utilizada no fato. Esses sinais materiais pod
- Uma pessoa obtém documento que apenas confirma sua presença em consulta, mas sem recomenda
- Os peritos responsáveis por exame judicial elaboram documento escrito com descrição dos ac
- Um exame de lesões corporais poderia ser realizado hoje, mas a autoridade decide dispensá-
- Em perícia retrospectiva, o examinador pode analisar documentação antiga para responder a 
- Ao identificar morte natural sem diagnóstico esclarecido em município com SVO e sem suspei
- Um médico perito encontra questão que excede sua habilitação e informa no laudo a limitaçã
- Ao elaborar atestado médico de afastamento, o profissional deve indicar o diagnóstico ou c
- No exame de lesão realizado para esclarecer incapacidade para ocupações habituais superior
- Ao constatar indícios inequívocos de morte, os peritos realizam autópsia antes de seis hor
- A defesa pede esclarecimentos ao perito oficial em audiência e encaminha intimação e quesi
- Peritos apresentam conclusões divergentes e a autoridade nomeia terceiro, que também disco
- Ao desenvolver laudo necroscópico, o examinador deve separar a causa médica de morte, fund
- No exame forense relacionado a possível ato sexual, a ausência de lesões visíveis prova ci
- Uma instituição pretende eliminar prontuários em papel após vinte anos do último registro.
- Um médico assistente pretende preencher formulário que constitui perícia oficial para obte
- Em avaliação administrativa de capacidade para o trabalho, uma junta oficial pode chegar a
- Ao realizar perícia de corpo de delito em pessoa viva, o médico pode atuar dentro de sala 
```

## Pedido 18: Termologia (`fisica.termologia`, 180 tópicos de edital)

```
Matéria: fisica.termologia (Termologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Temperatura, escalas, calorimetria e mudanças de fase": 27
- "2. Dilatação térmica e comportamento anômalo da água": 16
- "3. Propagação do calor: condução, convecção e radiação": 16
- "4. Gases ideais, energia interna, trabalho, entalpia e atmosfera": 19
- "5. Segunda lei, entropia, máquinas térmicas e Carnot": 22

Já existem (não repita):
- Uma amostra está a 20 °C e precisa ter sua temperatura registrada na escala absoluta do SI
- Em um calorímetro ideal, 200 g de água líquida elevam a temperatura em 10 °C. Adote calor 
- Deseja-se fundir completamente 0,10 kg de gelo já a 0 °C. Considere calor latente de fusão
- Uma chapa metálica homogênea, livre para se dilatar, contém orifício circular. Após aqueci
- Um reservatório contém água líquida pura entre 0 e 4 °C, sob pressão próxima à atmosférica
- Uma colher metálica permanece com a extremidade imersa em sopa quente, enquanto o cabo tam
- Durante um dia ensolarado, o continente se aquece mais que a água costeira, e o ar quente 
- Uma parede plana tem área de 2 m², espessura de 0,10 m, condutividade 0,5 W/(m·K) e difere
- Uma haste livre de comprimento inicial 2,0 m possui coeficiente de dilatação linear igual 
- Um frasco contém 1.000 mL de líquido. Os coeficientes volumétricos aproximados do líquido 
- Misturam-se 100 g de água a 80 °C com 100 g de água a 20 °C, em calorímetro ideal isolado 
- Um bloco de gelo de 150 g já está a 0 °C. Adote calor latente de fusão de 80 cal/g e despr
- Duas camadas planas atravessadas pelo mesmo fluxo térmico têm resistências de 0,2 e 0,3 K/
- Duas superfícies iguais de mesma emissividade emitem radiação térmica idealizada. A temper
- Um gás se expande a pressão constante de 200.000 Pa, aumentando seu volume em 0,003 m³. Qu
- Um gás recebe 500 J de calor e realiza 200 J de trabalho externo. Adote trabalho realizado
- Um mol de gás ideal ocupa 0,030 m³ sob pressão de 100.000 Pa. Use R = 8,31 J/(mol·K). Qual
- Dois mols de gás ideal são aquecidos 20 K a pressão constante, com capacidade térmica mola
- Uma parcela de ar não saturado sobe adiabaticamente 1.000 m, partindo de 27,0 °C. Use grad
- Uma máquina reversível de Carnot opera entre 600 K e 300 K. Qual rendimento térmico máximo
- Uma máquina ideal de Carnot recebe 2.000 J por ciclo da fonte a 900 K e descarrega calor à
- Uma geladeira retira 600 J de seu compartimento frio em um ciclo, consumindo 200 J de trab
- Um reservatório recebe reversivelmente 800 J de calor à temperatura constante de 400 K. Qu
- Uma máquina real recebe 1.200 J de uma fonte a 600 K, rejeita calor a outra a 300 K e prod
- Uma fonte a 500 K alimenta um motor térmico reversível que rejeita calor a 400 K. Ao compa
- Dois corpos separados estão, cada um, em equilíbrio térmico com um mesmo terceiro corpo. P
- O aumento de temperatura de 10 °C para 30 °C corresponde à variação de 293,15 K, porque to
- Um recipiente ideal e isolado contém dois corpos trocando calor, sem trabalho externo nem 
- Durante a fusão de substância pura a pressão constante e com sólido e líquido em equilíbri
- Em expansão térmica livre de barra homogênea, a variação linear de comprimento é proporcio
- Ao aquecer uniformemente chapa metálica com um pequeno orifício circular, o diâmetro do or
- Em um lago de água doce, o gelo tende a formar-se primeiro na superfície em condições comu
- Na transferência de calor por radiação térmica, a presença de ar ou outro meio material é 
- Em parede plana homogênea sob condução estacionária, ao dobrar a espessura e preservar áre
- Em duas camadas condutivas em série, a resistência térmica total é menor que cada resistên
- Uma superfície com área e emissividade constantes que duplica sua temperatura em kelvin em
- Em um gás ideal, a condição adiabática exige tanto ausência de calor transferido quanto te
- A entalpia de um sistema simples é definida por sua energia interna somada ao produto da p
- Em atmosfera saturada, o gradiente adiabático úmido é exatamente 9,8 °C/km em qualquer tem
- O ciclo motor reversível de Carnot entre duas temperaturas contém quatro etapas: expansão 
- Como uma máquina de Carnot é ideal e reversível, ela converte integralmente em trabalho to
- Uma geladeira que retira 900 J do compartimento frio consumindo 300 J de trabalho possui C
- Em qualquer processo espontâneo, a entropia de cada subsistema individual tem necessariame
- Nos modelos elementares de ciclos térmicos, a adição de calor do Otto idealizado ocorre a 
- Entre reservatórios a 800 K e 200 K, o rendimento reversível máximo de Carnot é 25%, já qu
```

## Pedido 19: Direito Internacional Privado: direito estrangeiro e cooperação jurídica civil (`direito-internacional.dip-direito-estrangeiro-cooperacao-civil`, 180 tópicos de edital)

```
Matéria: direito-internacional.dip-direito-estrangeiro-cooperacao-civil (Direito Internacional Privado: direito estrangeiro e cooperação jurídica civil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fontes, regras de conexão e limites à aplicação do direito estrangeiro": 21
- "2. Cooperação jurídica civil, autoridades centrais e obtenção de provas": 21
- "3. Homologação de decisões estrangeiras, exequatur e execução": 19
- "4. Restituição internacional de crianças e cooperação em alimentos": 21
- "5. Apostila, comunicações internacionais, jurisdição digital e casos integrados": 18

Já existem (não repita):
- Uma brasileira domiciliada há anos no país X apresenta em processo nacional documento sobr
- Um tribunal brasileiro deve definir a lei incidente sobre propriedade de terreno localizad
- Um juiz conhece os fatos do processo, mas não dispõe do texto atualizado da lei estrangeir
- Uma autoridade estrangeira solicita ao Brasil informação sobre processo civil, sem apresen
- Credora obtém sentença civil estrangeira definitiva contra devedor com bens no Brasil e pr
- Um documento público estrangeiro será apresentado no Brasil e está sujeito à Convenção da 
- Um responsável leva criança de 15 anos para outro Estado contratante contra direito de gua
- Uma ação civil estrangeira necessita de oitiva de testemunha localizada em outro Estado pa
- Uma pessoa de 19 anos busca, em contexto coberto pela Convenção da Haia de 2007, alimentos
- Regra brasileira de conexão indica direito do país A, cuja norma de conflitos remete a que
- Sem tratado com determinado Estado, o Brasil recebe pedido de cooperação civil acompanhado
- Em auxílio direto passivo, a medida pedida no Brasil exige ordem judicial para produzir pr
- A parte questiona a homologação de sentença estrangeira por discordar da valoração de test
- Uma decisão estrangeira interlocutória determina a conservação urgente de determinado bem 
- Após remoção ilícita de criança, o procedimento de restituição é iniciado 14 meses depois.
- Uma autoridade central estrangeira solicita ao Brasil esclarecimento sobre o estado de um 
- Uma plataforma oferece serviço digital a consumidor domiciliado no Brasil; o servidor fica
- Tribunal brasileiro verifica que reconhecimento de decisão estrangeira afrontaria coisa ju
- Uma sentença estrangeira de divórcio consensual, sem capítulos autônomos de alimentos ou g
- A autoridade estrangeira quer obter documentos de processo em curso no Brasil e não há pro
- Após cumprimento de visita internacional, a criança é retida em outro Estado. O responsáve
- Credor de alimentos residente no país A deseja acionar mecanismo convencional perante o pa
- Em ação sobre direitos reais de imóvel situado no Brasil, parte apresenta sentença estrang
- Para citar réu situado no exterior mediante Convenção da Haia de 1965, parte pretende usar
- Em cooperação jurídica civil, pedido estrangeiro veio pela autoridade central e traz tradu
- Uma pessoa domiciliada em país estrangeiro comparece a processo brasileiro sobre capacidad
- Uma autoridade estrangeira pede ao Brasil informação processual não sigilosa e sem decisão
- Sentença estrangeira definitiva foi homologada no Brasil. O STJ será o juízo responsável p
- Em procedimento de retorno regido pela Haia de 1980, a decisão que manda restituir a crian
- Uma certidão pública de país participante da Convenção de 1961 recebe apostila. Essa certi
- Uma criança com dezessete anos completos é removida para outro Estado. A Convenção da Haia
- Uma lei estrangeira escolhida pela norma brasileira faz nova remissão a direito de terceir
- Um Estado sem tratado pede cooperação civil ao Brasil. Por inexistir instrumento convencio
- Recebida sentença estrangeira eficaz no país de origem, a homologação poderá desconsiderar
- Uma criança retida ilicitamente apresenta prova convincente de integração ao novo meio, e 
- Em processo de alimentos entre Estados participantes da Convenção de 2007, a autoridade ce
- Documentos de cooperação civil recebidos por autoridade central com tradução portuguesa re
- Em contratação eletrônica transfronteiriça, o país onde se encontra o servidor que hospeda
- O pedido estrangeiro de auxílio direto destinado a medida judicial de produção de prova no
- Uma sentença estrangeira possui capítulos autônomos, dos quais apenas um afronta limite à 
- Na Haia de 1980, seis semanas sem decisão de restituição fazem surgir presunção definitiva
- Em cooperação de alimentos sob a Convenção de 2007, o Protocolo sobre Lei Aplicável desemp
- Uma sentença estrangeira de alimentos foi encaminhada por autoridade central com documenta
- A Haia de 1980 admite que oposição da criança, considerada sua idade e maturidade, seja av
- A Convenção da Haia de 1970, promulgada pelo Decreto nº 9.039/2017, tornou obrigatória a c
```

## Pedido 20: Contratos Empresariais: comissão, agência, representação, concessão, franquia, leasing e faturização (`direito-empresarial.contratos-empresariais`, 180 tópicos de edital)

```
Matéria: direito-empresarial.contratos-empresariais (Contratos Empresariais: comissão, agência, representação, concessão, franquia, leasing e faturização)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Visão geral e contrato de comissão": 20
- "2. Agência, distribuição e representação comercial": 20
- "3. Concessão comercial (Lei Ferrari)": 20
- "4. Franquia empresarial (Lei nº 13.966/2019)": 20
- "5. Arrendamento mercantil (leasing) e faturização (factoring)": 20

Já existem (não repita):
- Segundo o Código Civil, no contrato de comissão, o comissário realiza a compra ou venda de
- No contrato de comissão, as pessoas com quem o comissário contratar têm ação direta contra
- Constando do contrato de comissão a cláusula del credere, o comissário:
- Não havendo instruções diversas do comitente, presume-se o comissário autorizado a concede
- Sobre os direitos do comissário, assinale a alternativa correta.
- No contrato de comissão, se o comissário morrer ou não puder concluir o negócio por força 
- Pelo Código Civil, o contrato de agência distingue-se da distribuição porque nesta:
- Salvo ajuste, o proponente não pode constituir, ao mesmo tempo, mais de um agente na mesma
- No contrato de agência por tempo indeterminado, qualquer das partes poderá resolvê-lo medi
- Salvo ajuste, o agente ou distribuidor tem direito à remuneração correspondente aos negóci
- Segundo a Lei nº 4.886/1965, exerce a representação comercial autônoma:
- No contrato de representação comercial, é vedada a inclusão de cláusula del credere.
- Pela Lei nº 4.886/1965, prevendo o contrato de representação a exclusividade de zona ou se
- O representante comercial adquire o direito às comissões quando do pagamento dos pedidos o
- Na rescisão do contrato de representação comercial fora dos casos de justo motivo, a inden
- A denúncia sem causa justificada de contrato de representação por tempo indeterminado que 
- Constitui motivo justo para a rescisão do contrato de representação comercial pelo represe
- Pela Lei nº 4.886/1965, considera-se por prazo indeterminado todo contrato de representaçã
- No caso de falência ou recuperação judicial do representado, as importâncias devidas ao re
- A Lei nº 6.729/1979 (Lei Ferrari) disciplina a concessão comercial entre:
- Na concessão comercial de veículos, o concessionário é proibido de vender fora de sua área
- Pela Lei Ferrari, o contrato de concessão comercial:
- É livre o preço de venda do concessionário ao consumidor, cabendo ao concedente fixar o pr
- Se o concessionário der causa à rescisão do contrato de concessão comercial de veículos, p
- Na concessão comercial de veículos, são vedadas a prática de atos pelos quais o concedente
- Segundo a Lei nº 13.966/2019, o sistema de franquia empresarial:
- A Circular de Oferta de Franquia deverá ser entregue ao candidato a franqueado, no mínimo,
- Descumprido o prazo de entrega da Circular de Oferta de Franquia, o franqueado poderá:
- A Circular de Oferta de Franquia deve conter, entre outras informações, os balanços e demo
- Quando o franqueador subloca ao franqueado o ponto comercial da franquia:
- Os contratos de franquia internacional podem ser escritos em língua estrangeira sem traduç
- Segundo a Lei nº 6.099/1974, considera-se arrendamento mercantil o negócio jurídico realiz
- Os contratos de arrendamento mercantil devem conter prazo, valor das contraprestações por 
- A empresa que vende um bem de seu ativo a uma arrendadora e o recebe de volta em arrendame
- Segundo a Súmula 293 do STJ, a cobrança antecipada do valor residual garantido (VRG) desca
- No contrato de arrendamento mercantil, ainda que haja cláusula resolutiva expressa, para c
- No contrato de faturização (factoring), segundo o entendimento do STJ, o faturizador pode 
- Sobre a faturização (fomento mercantil), assinale a alternativa correta.
- Na concessão comercial de veículos, o concedente que não prorrogar o contrato ajustado por
- Salvo autorização expressa, o representante comercial não poderá conceder abatimentos, des
```

## Pedido 21: Rendas, séries uniformes e pagamentos (`matematica-financeira.rendas-series-pagamentos`, 177 tópicos de edital)

```
Matéria: matematica-financeira.rendas-series-pagamentos (Rendas, séries uniformes e pagamentos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fluxos de caixa, juros compostos e equivalência de capitais": 21
- "2. Séries uniformes postecipadas: valor presente, futuro e prestação": 29
- "3. Séries antecipadas, diferidas, carência e perpetuidades": 13
- "4. Séries variáveis, gradientes e fluxos irregulares": 26
- "5. Planos alternativos, valor atual, taxa implícita e verificações avançadas": 11

Já existem (não repita):
- Uma pessoa receberá R$ 110 ao final de um mês. Sob juros compostos de 10% ao mês, qual cap
- Duas prestações de R$ 100 serão pagas nos fins dos meses 1 e 2. A taxa efetiva é de 10% me
- Três depósitos de R$ 100 ocorrem ao fim dos meses 1, 2 e 3 e rendem 10% ao mês. Qual monta
- Uma compra prevê duas prestações iguais de R$ 100, com a primeira no ato e a segunda daqui
- Um compromisso será quitado por duas parcelas postecipadas de R$ 121, nos meses 1 e 2. A t
- Dois planos para quitar a mesma obrigação são comparados a 10% ao mês: A paga R$ 200 hoje;
- Uma instituição oferece remuneração efetiva de 10% ao mês com capitalização composta. Qual
- Uma renda de R$ 100 mensais continuará indefinidamente, com a primeira parcela ao fim do m
- Duas parcelas de R$ 121 vencerão apenas nos meses 3 e 4, à taxa de 10% mensais. Qual valor
- O valor financiado é R$ 210 e será liquidado por duas prestações iguais ao fim dos meses 1
- Um plano prevê recebimentos de R$ 100 no mês 1 e R$ 100 no mês 3, sem pagamento no mês 2. 
- Uma renda geométrica paga R$ 110 no mês 1, R$ 121 no mês 2 e R$ 133,10 no mês 3. Se o desc
- Dois depósitos iguais de R$ 100 ocorrem hoje e no início do mês 2, isto é, em t=0 e t=1. A
- Uma compra exige entrada de R$ 50 hoje, prestações de R$ 121 nos meses 1 e 2 e pagamento e
- Plano A prevê pagar R$ 200 hoje. Plano B prevê R$ 110 no mês 1 e R$ 121 no mês 2. Comparan
- Uma renda aritmética paga R$ 100, R$ 120 e R$ 140 nos meses 1, 2 e 3. Qual seu valor prese
- Duas prestações de R$ 121 nos meses 1 e 2 serão substituídas por quitação única no fim do 
- Uma dívida de R$ 1.000 seria liquidada por n prestações postecipadas constantes de R$ 100,
- Uma série variável contém R$ 110 ao fim do mês 1 e R$ 115,50 ao fim do mês 2. Descontando 
- Um fluxo irregular deposita R$ 100 ao final do mês 1 e R$ 200 ao final do mês 2, à taxa de
- Uma perpetuidade mensal constante de R$ 100 só inicia seus pagamentos no final do mês 3. C
- Uma renda perpétua paga R$ 110 no mês 1 e aumenta 5% a cada mês. O desconto efetivo é de 1
- Um plano paga R$ 110 em t=1 e R$ 133,10 em t=3. Sob taxa de 10% ao mês, qual pagamento úni
- Duas prestações postecipadas de R$ 100 vencem nos meses 1 e 2. Qual será o VP aproximado s
- Uma renda aritmética tem parcelas de R$ 100, R$ 120 e R$ 140 no final dos meses 1, 2 e 3. 
- Uma prestação mensal postecipada imediata vence no fim do primeiro mês e não na data da co
- Duas séries com o mesmo valor de cada parcela, a mesma quantidade de pagamentos e juros po
- Sob taxa efetiva composta de 10% mensal, pagar R$ 100 hoje é financeiramente equivalente a
- Uma renda perpétua de R$ 100 mensais, com taxa de 10% mensal e primeira parcela hoje, poss
- Uma dívida com valor presente de R$ 210, sob taxa de 10% mensal, pode ser substituída por 
- Na comparação financeira de dois planos, somar suas prestações nominalmente é suficiente p
- A Calculadora do Cidadão diferencia a simulação de depósitos mensais regulares, com depósi
- Duas prestações de R$ 121 previstas somente para os meses 3 e 4, à taxa de 10% mensal, pos
- Três depósitos de R$ 100 feitos ao fim dos meses 1, 2 e 3, com capitalização composta de 1
- Se um contrato utiliza juros compostos efetivos de 10% mensal, a taxa efetiva de dois mese
- A 10% ao mês, receber R$ 110 no mês 1 e R$ 133,10 no mês 3, sem pagamento intermediário, p
- Uma remuneração composta de 10% efetivos mensais corresponde exatamente a 120% efetivos an
- Uma renda com três parcelas de R$ 110, R$ 121 e R$ 133,10 nos meses 1, 2 e 3, descontadas 
- Uma dívida de R$ 1.000 será quitada por prestações uniformes postecipadas de R$ 100 à taxa
- Dois fluxos equivalentes a uma taxa efetiva composta dada continuam equivalentes quando a 
- Uma perpetuidade crescente de prestações positivas com primeira parcela no mês 1 possui va
- Uma obrigação que prevê entrada hoje, prestações mensais e parcela residual adicional pode
- Um fluxo que contém pagamentos nos meses 1, 3 e 5 pode ser avaliado pelo fator postecipado
- O caso de apenas uma prestação serve para conferir fórmulas: se ela vence em t=1, seu VP é
- Mantidos os mesmos pagamentos futuros positivos e as datas de vencimento, aumentar a taxa 
```

## Pedido 22: Avaliação atuarial, regimes financeiros e reservas matemáticas (`direito-previdenciario.avaliacao-atuarial-rpps`, 176 tópicos de edital)

```
Matéria: direito-previdenciario.avaliacao-atuarial-rpps (Avaliação atuarial, regimes financeiros e reservas matemáticas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da avaliação atuarial e hipóteses": 14
- "2. Modalidades de planos e regimes financeiros": 20
- "3. Reservas matemáticas, custos e duração do passivo": 22
- "4. Resultado atuarial, déficit e plano de equacionamento": 24
- "5. Documentos atuariais, fluxos e controle dos resultados": 20

Já existem (não repita):
- Um RPPS encerrou o exercício com receitas superiores às despesas de benefícios, mas a aval
- Na preparação da avaliação anual, foi preciso fixar uma data única para cadastro, compromi
- O estudo de um regime precisa estimar a expectativa de vida dos beneficiários para projeta
- Um plano estabelece no regulamento a fórmula que determinará a aposentadoria programada e 
- Um participante sabe o percentual de contribuições, mas o benefício programado dependerá d
- O cálculo das aposentadorias programadas de determinado RPPS deve seguir a regra do art. 3
- Em setembro, a morte de um segurado ativo origina pensão que será paga por vários anos. No
- Em segregação legalmente estabelecida, o Fundo em Repartição apresenta receita corrente us
- Ao separar provisões, o estudo identificou obrigações referentes a aposentadorias que já e
- Um estudo distingue o financiamento ordinário de benefícios futuros dos recursos extras pa
- Um RPPS registra passivo atuarial de R$ 120 milhões e ativos garantidores de R$ 90 milhões
- Um estudo informa média dos prazos futuros de pagamentos de benefícios, líquidos das contr
- Uma avaliação documenta a metodologia, os regimes, as fórmulas e os métodos utilizados no 
- A unidade gestora precisa transmitir à supervisão informações padronizadas sobre a massa, 
- Foi proposto equacionamento de déficit mediante alíquotas suplementares, sem qualquer lei 
- Uma administração pretende contabilizar como ativo garantidor qualquer bem de sua propried
- A avaliação dos benefícios programados adotou Crédito Unitário Projetado. A classificação 
- Uma massa de segurados foi segregada legalmente. A gestão planeja admitir novos ingressant
- Os compromissos de um plano foram reestimados após maior longevidade projetada, sem mudanç
- Uma avaliação contém valores presentes de pagamentos líquidos de R$ 100 no ano 1 e R$ 300 
- Um regime propôs majoração do custeio com base em avaliação focal de 31/12/2025. Considera
- Um estudo altera método de financiamento e deseja reduzir imediatamente o custeio, sem aná
- Um laudo classificou como ativos garantidores recursos de fundo para oscilação de riscos e
- Um ente propôs plano de custeio atuarialmente suficiente, mas sem demonstrar sua capacidad
- Um RPPS com receitas superiores às despesas neste ano pode apresentar déficit atuarial qua
- A avaliação anual de um RPPS pode fixar como data focal a assinatura do relatório, ainda q
- Uma tábua biométrica utilizada para estimar mortalidade e sobrevivência dos beneficiários 
- Em um plano de contribuição definida, o valor final do benefício programado é previamente 
- O regime de capitalização prevê formação gradual de reservas para financiar aposentadorias
- No RPPS, o custo suplementar é a denominação dada às despesas administrativas ordinárias, 
- A reserva matemática relativa a benefícios concedidos deve considerar obrigações associada
- Um ente que possui imóveis sem destinação ao RPPS pode classificá-los como ativos garantid
- A duração do passivo utiliza os valores presentes dos fluxos líquidos dos benefícios para 
- Se o passivo atuarial for R$ 95 milhões e os ativos garantidores forem R$ 105 milhões, a c
- Os fluxos atuariais devem permitir acompanhar a formação de reservas e fundamentar os cálc
- Quando a avaliação identifica déficit atuarial, a proposta de equacionamento torna-se juri
- A segregação de massas prevista no art. 58 admite Fundo em Repartição fechado, destinando 
- Se o método de financiamento do RPPS for alterado, a gestão poderá dispensar justificativa
- A revisão das hipóteses biométricas e financeiras deve observar aderência à massa e parâme
- Em repartição de capitais de cobertura, a arrecadação anual limita-se ao valor das prestaç
- No resultado atuarial dos compromissos avaliados em capitalização, a Portaria nº 1.467/202
- Uma proposta de majoração de custeio derivada da avaliação com data focal de 31/12/2025 po
- O DRAA é instrumento padronizado de informação sobre os resultados atuariais e não substit
- A aprovação técnica do cálculo atuarial torna desnecessária a demonstração de viabilidade 
- Um RPPS constatou que a projeção anual de receitas e despesas não converge, a valor presen
```

## Pedido 23: Direito do consumidor: crimes de consumo, superendividamento e Sistema Nacional de Defesa (`direito-consumidor.crimes-consumo-superendividamento-sndc`, 176 tópicos de edital)

```
Matéria: direito-consumidor.crimes-consumo-superendividamento-sndc (Direito do consumidor: crimes de consumo, superendividamento e Sistema Nacional de Defesa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais, princípios e ambiente digital": 16
- "2. Infrações penais: segurança, informação, oferta e publicidade": 19
- "3. Crimes de cobrança, cadastros, garantia e responsabilização penal": 20
- "4. Superendividamento, crédito responsável e repactuação": 15
- "5. Sistema Nacional de Defesa, sanções e convenções de consumo": 30

Já existem (não repita):
- Uma pessoa compra equipamento para utilização pessoal e reclama da falta de informação sob
- Uma pessoa solicita acesso aos dados de consumo mantidos a seu respeito; o responsável blo
- Após colocar lote de produtos no mercado, o fornecedor descobre perigo relevante e deixa d
- Uma pessoa natural de boa-fé não consegue pagar suas dívidas de consumo vencidas e futuras
- Duas associações de consumidores e fornecedores formalizam convenção escrita sobre reclama
- Uma loja virtual disponibiliza contratação fora de estabelecimento físico, com recebimento
- Ao executar reparação de aparelho, uma oficina substitui componentes por peças usadas sem 
- Ao apurar infração de consumo, a autoridade considera gravidade, vantagem auferida e condi
- O fornecedor publica anúncio apto a induzir compradores a comportamento perigoso à saúde, 
- Uma instituição financeira oferece crédito ao consumidor e informa taxas e encargos, mas p
- Um fornecedor passa a conhecer risco grave somente depois de distribuir produto e recebe d
- O credor, devidamente chamado à audiência coletiva de repactuação, não comparece nem envia
- Em processo por superendividamento sem acordo integral, o juiz cita credores remanescentes
- Um consumidor aceita orçamento para reparo, mas a oficina utiliza peças usadas sem autoriz
- Um anúncio enganoso afeta consumidores. Após processo administrativo, a autoridade determi
- Na conciliação por superendividamento, a pessoa natural pretende incluir crédito rural, fi
- Uma associação de fornecedores assina e registra convenção coletiva de consumo. Depois do 
- Uma campanha informa característica de produto de modo falso por falha culposa comprovada 
- Um estabelecimento permite público acima do máximo fixado pela autoridade administrativa e
- O juiz homologa plano consensual por superendividamento. Depois da quitação integral das o
- Não houve acordo com determinado credor, e o juiz pretende impor plano judicial compulsóri
- Uma pessoa registra reclamação no órgão público local de defesa do consumidor e propõe aud
- A multa administrativa é aplicada a fornecedor e, paralelamente, discute-se crime de consu
- Um fornecedor praticou crime previsto no CDC contra consumidor de 65 anos durante operação
- Uma entidade civil de defesa do consumidor pretende atuar no processo penal referente a cr
- Uma pessoa física adquire produto para uso pessoal e sofre falta de transparência contratu
- Em compra celebrada pela internet fora de estabelecimento comercial, o prazo de arrependim
- Uma fabricante omite advertência ostensiva exigida sobre periculosidade de seu produto, ai
- Um prestador de serviços usa afirmações falsas para expor devedor ao ridículo durante cobr
- Uma pessoa natural de boa-fé possui parcelas de consumo ainda não vencidas que tornam impo
- Como o SNDC reúne instituições públicas e privadas, qualquer associação civil integrante d
- Uma publicidade digital apresenta produto com informação técnica não comprovada. O CDC con
- Um gerente de empresa é acusado de crime previsto no CDC somente porque consta como admini
- Um consumidor procura a repactuação do art. 104-A para renegociar dívidas decorrentes de c
- No plano judicial compulsório do art. 104-B, a primeira parcela da dívida remanescente dev
- Em uma fiscalização, a autoridade competente aplica duas sanções administrativas compatíve
- Uma associação de consumidores e entidade de fornecedores assinam convenção sobre garantia
- Durante a oferta de crédito, um intermediário deixa de esclarecer encargos e omite avaliaç
- Quando o fornecedor reconhece informação cadastral inexata sobre consumidor, a omissão do 
- Um juiz analisa plano compulsório do art. 104-B e permite que as prestações posteriores à 
- Um juiz nomeia administrador para auxiliar o plano judicial compulsório, e o art. 104-B de
- Uma pessoa pretende responsabilizar criminalmente administradores por publicidade de consu
- Uma instituição aprova plano consensual de superendividamento para pessoa natural de boa-f
- Uma autoridade aplica sanção administrativa por publicidade enganosa e informa a ocorrênci
- Em oferta digital de crédito, o fornecedor anuncia contratação sem consulta a serviços de 
```

## Pedido 24: Contratos típicos complementares do Código Civil (`direito-civil.contratos-tipicos-complementares`, 174 tópicos de edital)

```
Matéria: direito-civil.contratos-tipicos-complementares (Contratos típicos complementares do Código Civil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Contrato estimatório e contratação eletrônica": 15
- "2. Depósito e transporte: guarda, restituição e responsabilidade": 26
- "3. Comissão, agência, distribuição e corretagem": 22
- "4. Transação, compromisso, constituição de renda e jogo": 25
- "5. Fiança, seguro privado e integração das regras": 12

Já existem (não repita):
- Uma editora entrega 80 livros a uma livraria, autorizando a venda e prevendo devolução dos
- Uma pessoa deixa sua câmera fotográfica com uma amiga para que seja guardada até a sua vol
- Em transporte remunerado, um passageiro sofre lesão em acidente causado exclusivamente pel
- Uma empresa encarrega intermediário de concluir vendas em seu próprio nome, mas por conta 
- Um profissional independente aproxima vendedor e comprador para uma venda específica, sem 
- Duas pessoas discutem o pagamento de dívida privada. Uma aceita redução do valor e a outra
- Uma pessoa oferece garantia pessoal escrita para pagamento da dívida de um conhecido, caso
- Uma loja digital vende um produto a consumidor que realizou a compra fora do estabelecimen
- Uma pessoa institui, por contrato, o dever de pagar quantia mensal a terceiro, a título gr
- Uma rede recebe por consignação cinquenta aparelhos para venda ou devolução. Um incêndio i
- Durante enchente, uma família entrega objetos a um vizinho para guarda emergencial, sem co
- Um comissário vende mercadorias em nome próprio por conta de uma empresa. A compradora fic
- Agente comercial, sem cláusula diversa, promove produtos numa zona definida. O proponente 
- Um imóvel é vendido diretamente pelo proprietário durante a vigência de corretagem com exc
- Um credor e um devedor celebram transação sobre obrigação patrimonial privada, com concess
- Fiador de obrigação civil assumida sem limitação de tempo notifica o credor de sua intençã
- Uma pessoa deseja solucionar litígio já instaurado sobre crédito patrimonial privado por d
- Em 2026, um parecer sobre contrato de seguro privado pretende fundamentar suas regras gera
- Uma loja conserva móveis recebidos em consignação para venda, ainda sem pagar o preço de e
- Agência celebrada por prazo indeterminado exigiu investimentos expressivos. Após curto per
- Uma escritura institui renda mensal vitalícia em favor de beneficiário vivo, que, vinte di
- Fiador de contrato civil é demandado e pretende invocar benefício de ordem. Na assinatura,
- Uma dívida nasce de jogo não enquadrado entre as modalidades legalmente permitidas. O perd
- Em transporte de mercadoria, uma informação falsa no documento entregue ao transportador p
- Duas pessoas celebram contrato privado por assinatura eletrônica sem certificado ICP-Brasi
- Uma empresa recebe móveis de particular para guarda remunerada, e as partes estipulam expr
- Uma loja recebeu mercadorias em contrato estimatório e perdeu os bens por incêndio inevitá
- Um agente promove continuamente negócios para determinada empresa, por retribuição e sem r
- Um intermediário compra equipamentos em nome do comitente e vincula este diretamente peran
- Uma pessoa dá carona a colega por mera cortesia, sem remuneração direta ou vantagem indire
- Um consumidor contrata produto pela internet. Como o Decreto nº 7.962/2013 assegura atendi
- Um depositário deixa de restituir coisa exigida pelo depositante. O art. 652 do Código Civ
- O responsável por transporte de carga pode rejeitar mercadoria com embalagem inadequada, m
- Havendo cláusula de corretagem com exclusividade escrita, o corretor tem direito à remuner
- Partes transigem sobre controvérsia patrimonial privada e, posteriormente, uma delas desco
- Duas pessoas pretendem submeter à arbitragem uma disputa sobre estado de filiação, pois co
- Contrato de renda periódica favorece dois beneficiários sem indicar suas quotas. Se um del
- Um prêmio regularmente prometido para competição esportiva submetida às prescrições legais
- Contrato de comissão prevê cláusula del credere apenas para parte das operações de crédito
- Um contrato de agência em determinada zona contém cláusula expressa que afasta a exclusivi
- Fiador de obrigação civil por prazo indeterminado notifica o credor da exoneração. Como o 
- Bagagem deixada em hospedaria é equiparada a depósito necessário. O hospedeiro, porém, pod
- A celebração eletrônica de um negócio elimina a necessidade de escritura pública em todas 
- Ao contrário da fiança, que garante débito alheio por escrito, o contrato de seguro privad
- Um corretor diligente aproximou vendedor e comprador, mas o contrato de corretagem se ence
```

## Pedido 25: Jornalismo, assessoria de imprensa e editoração (`comunicacao.jornalismo-assessoria-editoracao`, 173 tópicos de edital)

```
Matéria: comunicacao.jornalismo-assessoria-editoracao (Jornalismo, assessoria de imprensa e editoração)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, gêneros e redação jornalística": 17
- "Noticiabilidade, pauta, apuração e checagem": 17
- "Assessoria de imprensa e relacionamento com a mídia": 17
- "Ética, direitos fundamentais e responsabilidade jornalística": 17
- "Editoração, revisão, processo gráfico e fotojornalismo": 16
- "Jornalismo digital: produção, edição e circulação da notícia": 16

Já existem (não repita):
- A pirâmide invertida organiza a notícia priorizando, em geral, os elementos de maior relev
- O lead é inválido sempre que deixa de responder, em uma única frase, às seis perguntas tra
- Um editorial apresenta a posição opinativa institucional de um veículo e não se confunde c
- A proeminência de uma personagem pode contribuir para a noticiabilidade, mas não dispensa 
- Informação proveniente de órgão oficial fica dispensada de verificação adicional no proces
- O release é uma comunicação preparada por assessoria de imprensa para oferecer fatos ou pa
- Clipping se reduz, por definição, à contagem de matérias publicadas sobre uma organização.
- A identificação de fotomontagem em conteúdo noticioso pode ser necessária para evitar que 
- Uma reportagem sempre se distingue da notícia pelo maior número de palavras, independentem
- Reproduções do mesmo comunicado em cinco portais não constituem, por si, cinco confirmaçõe
- No acompanhamento de denúncia, ouvir a pessoa acusada equivale a conceder a ela poder de v
- Na assessoria de imprensa, o mailing deve ser formado sem atenção à editoria ou pertinênci
- O conteúdo de um press kit pode reunir fichas técnicas, contatos, fotografias creditadas e
- O Manual de Jornalismo da EBC constitui, por sua natureza, uma lei federal geral de obediê
- O ordenamento constitucional protege simultaneamente a liberdade de expressão e direitos c
- Revisão ortográfica de uma matéria substitui o procedimento de checagem factual de nomes, 
- Na impressão, sangria e margem de segurança são conceitos equivalentes, pois ambos se situ
- O emprego de hiperlinks em notícia digital pode facilitar consulta a documentos, mas não c
- O tratamento de dados exclusivamente para finalidade jornalística possui exceção específic
- Uma correção factual relevante realizada em matéria digital deve preferencialmente ser ide
- Uma redação recebeu a informação de que uma escola abrirá novas vagas amanhã. Qual abertur
- Qual das opções descreve com precisão um editorial jornalístico?
- Uma pauta privilegia risco de contaminação da água que atinge diversos bairros, embora não
- Qual prática é compatível com a boa checagem de um dado de saúde divulgado em boletim?
- Após divulgar um novo serviço, a assessoria envia um texto com contato, dados e declaração
- Uma equipe precisa acompanhar a presença de sua instituição na imprensa e avaliar a precis
- Na composição de uma revista impressa, a atividade de distribuir texto, imagens e legendas
- Um veículo publica em sua página uma matéria com links para documentos de origem. Qual con
- Em uma entrevista, a fonte responde de maneira vaga sobre orçamento. O melhor procedimento
- Ao preparar release sobre obra pública, qual recurso mais aumenta sua utilidade jornalísti
- Uma pessoa pública tem detalhes íntimos irrelevantes para a investigação expostos em repor
- Sobre a objetividade jornalística, assinale a formulação mais adequada.
- Qual etapa é especialmente voltada a detectar problemas introduzidos na composição de pági
- Ao preparar material colorido para impressão convencional, é especialmente pertinente veri
- Uma notícia ainda está em desenvolvimento. A redação confirmou o fato central, mas não a c
- Um repórter verifica denúncia por quatro textos aparentemente distintos e descobre que tod
- Uma fonte pede sigilo por risco grave e oferece informação relevante sobre contrato públic
- Um assessor pressiona repórter para divulgar integralmente o release sob ameaça de excluir
- A respeito de imagem documental, qual tratamento é eticamente mais adequado?
- Uma reportagem online registra incorretamente a quantidade de vagas e já foi republicada. 
- Em reportagem baseada em dados, uma taxa sobe de 10% para 12%. Como expressar precisamente
- Ao apresentar versões conflitantes sobre um estudo, a reportagem dispõe de um conjunto de 
- Ao selecionar o tratamento de dados exclusivamente jornalístico, qual proposição é juridic
- Em uma página diagramada, um fundo colorido alcança a borda do papel, enquanto o número de
- Uma manchete digital foi otimizada para busca, mas promete cura definitiva não sustentada 
- Qual proposta de acesso torna um gráfico essencial mais inclusivo numa reportagem digital?
```

## Pedido 26: Power BI e visualização de dados (`informatica.power-bi-visualizacao`, 172 tópicos de edital)

```
Matéria: informatica.power-bi-visualizacao (Power BI e visualização de dados)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de BI e preparação de dados com Power Query": 14
- "2. Modelagem semântica, relacionamentos e DAX básico": 15
- "3. Análise exploratória e escolha correta dos gráficos": 24
- "4. Relatórios, dashboards, storytelling e interatividade": 25
- "5. Plataformas de análise, comparação de ferramentas e projeto de painel": 22

Já existem (não repita):
- Uma tabela possui registros de janeiro e fevereiro em consultas separadas, com as mesmas c
- Uma coluna importada contém identificadores como '00128' e '00245'. Esses códigos não repr
- Um painel precisa exibir total de atendimentos diferente conforme o serviço selecionado nu
- Uma análise pretende comparar quantidades de solicitações de cinco tipos distintos de serv
- Um arquivo apresenta tempos de atendimento em minutos, e a análise precisa mostrar quantos
- Uma equipe criou um relatório no Power BI Desktop e pretende reunir blocos de várias págin
- Em um modelo estrela, a tabela de serviços guarda uma linha por código, enquanto a tabela 
- Uma ferramenta de visualização permite construir worksheets com dimensões e medidas e reun
- Em uma seleção padrão no Qlik Sense, um valor de campo fica verde com marca de seleção. Qu
- Em uma amostra de tempos `5, 6, 7, 8, 44` minutos, a média é 14 e a mediana é 7. Que concl
- Um relatório precisa mostrar a proporção de solicitações concluídas sobre o total, atualiz
- Um box plot possui Q1 = 12 e Q3 = 20, empregando a convenção dos limites a 1,5 vezes a amp
- Duas grandezas quantitativas são apresentadas como pares de pontos, e a nuvem cresce da es
- Uma consulta contém código de serviço, e outra guarda códigos únicos com descrições. A int
- Uma base tem dois atendimentos de uma mesma pessoa e três de pessoas distintas, totalizand
- Um gráfico de barras compara 98 e 100 ocorrências, mas inicia o eixo quantitativo em 97 pa
- Em apresentação, as reclamações sobem de 10 para 15, mas o total de atendimentos cresce de
- Uma analista seleciona uma categoria no Qlik Sense e observa que outro valor aparece em ci
- Um conjunto contém tempos de duração contínuos e pretende-se comparar a forma de sua distr
- Um relatório Power BI tem três páginas, segmentadores e detalhamento. O gestor deseja resu
- Uma base de 50 unidades apresenta Q1 = 10 e Q3 = 22. Pela regra usual de 1,5 AIQ, observou
- Uma consulta contém o texto 'N/A' na coluna Valor e uma etapa de mudança para decimal prod
- Uma distribuição foi dividida em faixas de largura 5 e 20 minutos, e um gráfico mostra fre
- No Data Studio, um relatório necessita de uma métrica que divida receita por atendimentos.
- Um dashboard Metabase possui um filtro de região criado na página, porém um cartão de perg
- Uma consulta de Power Query contém uma etapa que transforma texto monetário em número deci
- Em uma modelagem estrela, dimensões são empregadas prioritariamente para somar valores tra
- Uma série de quantidades mensais pode ser apresentada por gráfico de linhas para evidencia
- Um histograma de duração dos atendimentos atribui cada coluna ao nome de um serviço, pois 
- Um dashboard do Power BI Service é uma página única que pode reunir tiles originados de di
- Em um painel do Qlik Sense, um campo apresentado em cinza-escuro na convenção padrão compr
- Uma base registra várias consultas por pessoa. COUNTROWS de sua tabela fato pode superar D
- Uma coluna calculada do Power BI usa DAX e tem necessariamente o mesmo comportamento de me
- Se Q1 = 14 e Q3 = 22, a amplitude interquartil é 8, e valores acima de 34 superam a cerca 
- Ao comparar categorias com gráfico de barras, iniciar o eixo vertical acima de zero preser
- Uma apresentação usa o título 'Mediana caiu 15%' e informa o período e a população analisa
- Como dashboards do Metabase podem usar filtros conectados aos cartões, os dashboards técni
- No Tableau, uma dimensão costuma organizar os dados em categorias e uma medida costuma for
- Um valor observado acima do limite Q3 + 1,5 × AIQ deve ser apagado automaticamente, pois a
- Num modelo estrela, recomenda-se avaliar cuidadosamente relações bidirecionais, pois camin
- A operação Acrescentar no Power Query mantém necessariamente a quantidade original de linh
- Uma medida `Total = SUM(Fato[Valor])` soma 300 no conjunto completo e 100 ao escolher dete
- Uma forte associação positiva observada em gráfico de dispersão comprova que aumentar uma 
- No estado associativo padrão do Qlik Sense, verde indica valor selecionado, branco indica 
- Num esquema estrela, uma dimensão com chave duplicada pode ser mesclada à fato sem qualque
```

## Pedido 27: Estatuto do Desarmamento (`legislacao-penal-especial.estatuto-desarmamento`, 171 tópicos de edital)

```
Matéria: legislacao-penal-especial.estatuto-desarmamento (Estatuto do Desarmamento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura do Estatuto, Sinarm, aquisição, registro e posse": 20
- "2. Porte de arma de fogo: proibição, exceções e autorizações": 19
- "3. Crimes de posse, omissão de cautela, porte, disparo e uso restrito": 22
- "4. Comércio ilegal, tráfico internacional e causas de aumento": 22
- "5. Questões penais, jurisprudência, entrega e revisão integrada": 17

Já existem (não repita):
- Uma pessoa mantém em casa arma de uso permitido regularmente registrada. Ao saber que o ce
- Após a venda autorizada de uma arma, surgem dúvidas sobre o papel do Sinarm. Qual atividad
- Um interessado preencheu os requisitos de compra de arma de uso permitido e protocola requ
- Uma pessoa com 23 anos, sem nenhuma condição enquadrável nas ressalvas legais, comprova ap
- Uma pessoa residente em área rural possui CRAF válido e mantém a arma em uma dependência a
- Alguém afirma que a legislação vigente aboliu toda possibilidade de porte de arma de fogo 
- Uma autorização comum de porte foi deferida com indicação de validade temporal e de certa 
- Uma pessoa detentora de autorização de porte é abordada e encontra-se em estado de embriag
- A pessoa autorizada a realizar deslocamento específico com arma desmuniciada apresenta gui
- A pessoa que integra determinada hipótese especial do art. 6º alega que todas as categoria
- Uma arma de uso permitido, sem o registro exigível, é encontrada na residência de seu prop
- O responsável deixa de adotar cautelas necessárias, e uma pessoa de 17 anos se apodera de 
- Uma pessoa transporta em via pública, sem autorização, munição de uso permitido. Qual indi
- Uma pessoa realiza disparo em direção a via pública; a investigação mostra que o tiro não 
- Uma arma de fogo classificada como de uso proibido é possuída sem autorização, em hipótese
- Uma pessoa mantém esquema habitual de venda clandestina de munições em sua residência, sem
- Uma operação sem autorização promove a entrada de acessórios de armas no território nacion
- A pessoa pratica comércio ilegal de munição de uso restrito, com tipicidade do art. 17. Qu
- Uma pessoa reincidente específica em crimes dessa natureza pratica a conduta descrita no a
- Em outubro de 2026, analisa-se crime do art. 12 diretamente ligado ao comércio ilícito de 
- Uma questão afirma que a lei proíbe, sem exceção processual, qualquer fiança nos crimes do
- Uma pessoa entrega espontaneamente a arma sob sua posse irregular, mediante recibo, preenc
- A apreensão recai sobre arma desmuniciada, mas periciada como apta ao disparo. A defesa su
- Uma pessoa possui poucas munições desacompanhadas de arma. Há indícios de reiteração delit
- Material antigo afirma que o Decreto nº 9.847/2019 foi integralmente revogado pelo Decreto
- O Sinarm possui abrangência nacional e desempenha atribuições de cadastro de transferência
- O certificado de registro de arma de fogo constitui, por si mesmo, autorização suficiente 
- Segundo o art. 4º, § 6º, a concessão ou recusa fundamentada da autorização de compra deve 
- O art. 28 proíbe aquisição de arma de fogo para menores de 25 anos, sem qualquer ressalva 
- O art. 6º estabelece proibição geral de porte de arma de fogo, ressalvando hipóteses previ
- A autorização de porte prevista no art. 10 possui necessariamente validade vitalícia e abr
- Abordado em estado de embriaguez, o titular de porte tem a eficácia dessa autorização auto
- O porte de trânsito legalmente documentado equivale a autorização de porte pessoal sem res
- A posse irregular de arma de fogo de uso permitido no interior da residência, quando atend
- O crime de omissão de cautela do art. 13 pressupõe que o menor de 18 anos tenha efetuado a
- A pena do art. 14, relativa a condutas ilegais com arma, acessório ou munição de uso permi
- A disciplina do art. 16, § 2º, mantém a faixa de três a seis anos de reclusão mesmo quando
- A omissão empresarial de comunicação de extravio do art. 13, parágrafo único, sujeita-se à
- A definição do art. 15 exige disparo ou acionamento nas hipóteses espaciais previstas, ind
- O comércio ilegal do art. 17 pode ocorrer em residência quando houver exercício de comérci
- O tráfico internacional previsto no art. 18 pressupõe a comprovação de venda lucrativa em 
- O aumento de metade do art. 19 incide nos crimes dos arts. 17 e 18 quando envolverem armas
- O art. 20 aumenta de dois terços a pena de todo crime do Estatuto, sempre que exista reinc
- A Lei nº 15.358/2026 acrescentou o art. 21-A, com aumento de dois terços em hipóteses defi
- Por força da ADI 3.112, as proibições automáticas de fiança dos arts. 14 e 15 e de liberda
```

## Pedido 28: BNCC Computação, pensamento computacional e cultura digital (`educacao.bncc-computacao-cultura-digital`, 171 tópicos de edital)

```
Matéria: educacao.bncc-computacao-cultura-digital (BNCC Computação, pensamento computacional e cultura digital)
Capítulos e quantas questões quero em cada um (total 100):
- "Marcos normativos e os três eixos da BNCC Computação": 20
- "Pensamento computacional, algoritmos e programação": 20
- "Mundo digital: dados, dispositivos, redes e sistemas": 20
- "Cultura e cidadania digital: ética, segurança e informação": 20
- "Robótica educacional, avaliação e saberes digitais docentes": 20

Já existem (não repita):
- A BNCC Computação organiza suas aprendizagens em pensamento computacional, mundo digital e
- A Resolução CNE/CEB nº 1/2022 foi editada para substituir integralmente a BNCC por um curr
- Os quatro eixos legais da Política Nacional de Educação Digital coincidem em número e deno
- É possível integrar os três eixos da BNCC Computação em uma mesma atividade sem apagar seu
- Dividir a organização de uma feira em logística, inscrições e comunicação é exemplo de dec
- Abstração consiste em retirar aleatoriamente todos os dados de um problema, mesmo quando n
- Um algoritmo pode ser representado por instruções escritas ou fluxograma e estudado sem ex
- Encontrar e corrigir uma condição lógica inadequada durante o teste de um programa constit
- Hardware designa componentes físicos do sistema, enquanto software designa programas e ins
- A internet é apenas outro nome para a Web, de maneira que todo serviço da internet obrigat
- Um byte contém oito bits, e a interpretação dos bits depende de convenções de representaçã
- A gravação automática de dados em nuvem elimina a necessidade de considerar permissões de 
- Na avaliação de uma notícia on-line, número elevado de compartilhamentos é prova suficient
- A proteção de dados de crianças e adolescentes exige considerar o melhor interesse dos tit
- Qualquer imagem publicada em rede social pode ser incorporada a material didático e redist
- A cidadania digital envolve participação responsável, respeito à diversidade e reflexão so
- Em robótica educacional, a avaliação pode considerar decisões de projeto, testes, depuraçã
- A única forma de desenvolver pensamento computacional na Educação Infantil é ensinar lingu
- As três dimensões do Referencial de Saberes Digitais Docentes possuem necessariamente os m
- Uma atividade de robótica com kits caros não é, por si só, evidência de aprendizagem mais 
- Uma escola pretende identificar o eixo principal de uma tarefa em que estudantes avaliam a
- Qual instrumento estabeleceu normas sobre Computação na Educação Básica em complemento à B
- Ao comparar a BNCC Computação e a Política Nacional de Educação Digital, qual alternativa 
- Em uma reunião pedagógica, afirma-se que o mundo digital, a cultura digital e o pensamento
- A coordenação precisa distinguir os termos empregados por documentos oficiais. Qual enumer
- Para organizar o transporte escolar, uma turma separou a tarefa em definição de rotas, con
- Na criação de um modelo para comparar rotas de ônibus por duração e acessibilidade, estuda
- Um algoritmo autoriza empréstimo somente se o livro está disponível e a pessoa está abaixo
- Uma programação em blocos deveria mover um personagem três casas, mas move infinitamente p
- Uma professora solicita que os alunos encontrem regularidades em tabelas de horários para 
- Estudantes relacionam os elementos de um computador a suas funções. Qual afirmação está te
- Em aula sobre codificação, foram apresentados os cartões binários de valores 16, 8, 4, 2 e
- Qual alternativa diferencia adequadamente internet e Web?
- Em um sistema automatizado de rega, um sensor de umidade é conectado a um controlador e a 
- Antes de publicar fotos de estudantes identificados em um mural aberto, a escola avalia a 
- Uma postagem sobre suposta mudança de calendário escolar foi compartilhada milhares de vez
- Ao produzir um podcast, uma turma deseja utilizar música encontrada em uma plataforma digi
- Uma ferramenta de IA oferece uma resposta detalhada, mas sem indicar de onde vêm os fatos.
- Durante um projeto de carrinho robótico, o protótipo falhou em parte dos testes, mas os al
- Uma professora deseja realizar atividade introdutória de algoritmo sem computadores. Qual 
- O Referencial de Saberes Digitais Docentes do MEC organiza competências de professores em 
- Um gestor quer melhorar o ensino de Computação sem aumentar desigualdades. Que decisão ate
- Em um fluxograma, a instrução “enquanto houver fichas não verificadas, conferir a próxima 
- Durante uma investigação, o professor explica que um dado só se torna informação útil quan
- Em uma produção colaborativa, qual decisão concilia cidadania digital e acessibilidade?
```

## Pedido 29: Constituição Federal de 1988 (Direitos Fundamentais) (`direito.constituicao-federal-1988-direitos-fundamentais`, 170 tópicos de edital)

```
Matéria: direito.constituicao-federal-1988-direitos-fundamentais (Constituição Federal de 1988 (Direitos Fundamentais))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direitos individuais e coletivos: liberdades, igualdade e proteção constitucional": 41
- "2. Garantias processuais, remédios constitucionais e tratados de direitos humanos": 26
- "3. Administração pública do art. 37 e militares estaduais do art. 42": 11
- "4. Emendas constitucionais, órgãos do Judiciário e Justiça estadual": 10
- "5. Forças Armadas, serviço militar e segurança pública": 12

Já existem (não repita):
- Durante reunião pacífica e sem armas em praça pública, os organizadores comunicaram previa
- Uma associação legalmente constituída enfrenta decisão de suspensão compulsória de ativida
- Uma pessoa sofre ameaça concreta de prisão ilegal, com risco à liberdade de locomoção, e p
- Uma pessoa deseja conhecer e corrigir informação sobre si mantida em banco de dados de car
- Uma autarquia fará seleção de pessoal para cargo efetivo e propõe substituir a prova por e
- Um concurso para cargo público prevê validade original de 24 meses e uma única prorrogação
- Foi proposta emenda constitucional e há dúvida sobre a votação necessária. Qual quórum e p
- Uma lista dos órgãos do Poder Judiciário omite o Conselho Nacional de Justiça sob o argume
- Uma ocorrência exige identificar a organização constitucionalmente responsável por polícia
- Uma comissão recebeu projeto de tratado de direitos humanos e quer saber qual aprovação lh
- Tratado internacional de direitos humanos foi ratificado e internalizado sem a aprovação q
- Uma pessoa é indicada como depositário infiel e pretende-se decretar sua prisão civil com 
- Uma pessoa em processo administrativo recebe penalidade sem ter acesso à possibilidade de 
- Em 2026, uma servidora ocupante de cargo de professora pretende acumular outro cargo públi
- Uma pessoa sem cargo efetivo foi designada para função de confiança permanente de assessor
- Uma proposta de emenda constitucional foi rejeitada em determinada sessão legislativa e se
- Em tempo de paz, uma pessoa alistada alega convicção religiosa para não desempenhar ativid
- Uma associação de direitos humanos pretende impetrar mandado de segurança coletivo em favo
- Poder público precisa utilizar temporariamente terreno particular por iminente perigo públ
- Durante vigência de estado de defesa, parlamentares propõem aprovar emenda à Constituição 
- Um estado cujo efetivo militar é superior a 20 mil integrantes pretende criar Tribunal de 
- Uma pessoa militar estadual pratica crime militar contra civil, sem se tratar de caso subm
- Um documento confere ao Exército poder próprio para arbitrar conflitos entre Poderes const
- Uma análise atribui a apuração de infrações penais comuns estaduais às polícias militares,
- Um cidadão busca anular ato lesivo à moralidade administrativa por instrumento constitucio
- Uma reunião pacífica, sem armas, em local aberto ao público independe de autorização, mas 
- A dissolução compulsória de associação pode ser determinada por ato administrativo fundame
- O habeas corpus é cabível quando alguém sofre ou se acha ameaçado de coação ilegal em sua 
- O art. 37 exige concurso de provas ou de provas e títulos para qualquer função de confianç
- A Constituição exige que a proposta de emenda seja votada em dois turnos na Câmara e em do
- Os corpos de bombeiros militares exercem, por definição do art. 144, § 5º, a função consti
- O art. 5º, § 2º, contém cláusula de abertura que admite direitos e garantias decorrentes d
- Segundo o STF, tratados internacionais de direitos humanos incorporados sem o rito do art.
- O art. 37, § 6º, prevê responsabilidade civil da pessoa jurídica de direito público pelo d
- A partir da EC nº 138/2025, o art. 37, XVI, b, continua limitando a acumulação de cargo de
- O art. 42 qualifica membros das polícias militares e dos corpos de bombeiros militares com
- O art. 60, § 3º, exige que uma emenda aprovada seja submetida à sanção ou veto do Presiden
- O Conselho Nacional de Justiça está expressamente incluído no elenco constitucional de órg
- O art. 5º, XI, permite entrada forçada em casa mediante mandado judicial em qualquer horár
- O art. 5º, LXXIX, assegura direito à proteção dos dados pessoais, inclusive em meios digit
- O art. 60, § 4º, impede propostas tendentes a abolir direitos e garantias individuais, mas
- Pelo art. 125, § 5º, crimes militares estaduais cometidos contra civis são julgados singul
- A atribuição constitucional de garantir poderes constitucionais transforma as Forças Armad
- O art. 143, § 2º, isenta mulheres e eclesiásticos do serviço militar obrigatório em tempo 
- A polícia ferroviária federal, referida no art. 144, § 3º, tem por atribuição o patrulhame
```

## Pedido 30: Processo Penal Militar (`processo-penal-militar.processo-penal-militar`, 170 tópicos de edital)

```
Matéria: processo-penal-militar.processo-penal-militar (Processo Penal Militar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei processual penal militar, polícia judiciária e IPM": 19
- "2. Ação penal, denúncia, partes, prazos e Conselhos de Justiça": 21
- "3. Competência da Justiça Militar da União e dos Estados": 16
- "4. Prova, questões prejudiciais, exceções e incidentes": 22
- "5. Prisões, menagem, liberdade provisória, cautelares e recursos": 22

Já existem (não repita):
- O comandante pretende delegar a instauração de IPM contra oficial, embora disponha de ofic
- Um órgão de acusação dispõe de documentos suficientes para provar fato e autoria de possív
- O Ministério Público recebe autos de investigação com acusado preso e precisa oferecer den
- Uma acusação foi oferecida ao juízo militar, mas ainda não recebeu decisão de recebimento.
- Um oficial que não é oficial-general responde, na Justiça Militar da União, a crime milita
- Militar estadual é acusado de homicídio doloso contra vítima civil. A qual órgão constituc
- Uma militar estadual é acusada de crime militar contra civil sem natureza dolosa contra a 
- Acusado pede menagem por crime cuja pena privativa de liberdade máxima prevista é de três 
- O Ministério Público deixa de incluir parte dos dados exigidos pelo art. 77 na denúncia. A
- A Justiça Militar da União recebe denúncia contra civil e militar acusados conjuntamente, 
- Num processo penal militar, a defesa suscita incompetência do juízo após qualificação do a
- Houve dúvida concreta sobre imputabilidade penal do acusado, e o juiz ordenou perícia médi
- A defesa apresenta documento cuja falsidade pode ser decisiva para absolvição; outro depoi
- O juízo pretende decretar prisão preventiva militar fundamentando-se exclusivamente no fat
- Um acusado recebeu menagem para permanecer na cidade-sede do juízo, mas foi condenado e pr
- Uma decisão judicial não recebe denúncia no processo penal militar. Qual combinação descre
- Um oficial-general das Forças Armadas responde por crime militar de competência da JMU. Qu
- Militar das Forças Armadas é acusado de crime militar doloso contra a vida de civil durant
- Acusado opõe exceção de suspeição do juiz e, simultaneamente, exceção de incompetência. Qu
- O juiz identifica questão séria e fundada de estado civil, da qual depende julgamento pena
- Um investigado apresenta doença mental apenas depois do fato imputado, tornando-o momentan
- Um acusado reincidente requer menagem, estando o crime dentro do máximo de pena de quatro 
- O juiz concede menagem a militar para permanecer em local designado, mas o beneficiário o 
- A defesa recorre de decisão que negou menagem; pretende interpor RSE cinco dias após a int
- A acusação apresenta confissão obtida fora de ambiente judicial, mas inconciliável com per
- Uma autoridade militar apura suposto crime militar e encaminha elementos ao Ministério Púb
- O CPPM exige a instauração de IPM para toda denúncia, inclusive quando autoria e fato estã
- No procedimento penal militar, o processo tem início com o recebimento da denúncia pelo ju
- O Conselho Permanente de Justiça da União é competente em regra para julgar oficiais, ao p
- A Constituição atribui ao Tribunal do Júri o julgamento de crime doloso contra a vida de c
- Todo civil acusado de crime militar pode ser processado e julgado pela Justiça Militar est
- Para a prisão preventiva militar, a prova do fato e os indícios suficientes de autoria do 
- O Ministério Público Militar recebe autos com investigado solto e dispõe, pelo art. 79 do 
- Os Conselhos Especial e Permanente na Justiça Militar da União possuem juiz federal da Jus
- Uma questão prejudicial civil de difícil solução que não envolve estado civil suspende aut
- Na hipótese de incompetência do juízo, a parte contrária deve ter vista para manifestação 
- Quando surge suspeita de doença mental do acusado, o laudo médico do incidente de insanida
- A menagem do acusado em residência ou cidade, mesmo que acompanhada de restrição de desloc
- Interposição do recurso em sentido estrito militar e apresentação de suas razões possuem o
- A ausência de perícia médica pode ser suprida pela decretação de medida de segurança provi
- A Justiça Militar da União pode processar crime militar doloso contra a vida de civil prat
- O CPPM estabelece que a confissão espontânea, quando realizada perante autoridade competen
- O Conselho Permanente de Justiça federal tem, em regra, funcionamento trimestral, e o Cons
- O art. 267 do CPPM determina que a menagem concedida deve permanecer em vigor até o trânsi
- Embora o art. 308 do CPPM contenha redação histórica sobre silêncio, o direito constitucio
```

## Pedido 31: Boas Práticas para Serviços de Alimentação (RDC Anvisa 216/2004) e técnica dietética (`regulacao-sanitaria.boas-praticas-servicos-alimentacao`, 169 tópicos de edital)

```
Matéria: regulacao-sanitaria.boas-praticas-servicos-alimentacao (Boas Práticas para Serviços de Alimentação (RDC Anvisa 216/2004) e técnica dietética)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Âmbito, edificação e higienização": 27
- "2. Água, pragas, resíduos e manipuladores": 25
- "3. Recepção, armazenamento, preparo e conservação": 24
- "4. Documentação e técnicas de pré-preparo e preparo": 24

Já existem (não repita):
- A RDC Anvisa nº 216/2004 aplica-se, entre outros, a restaurantes, lanchonetes, cozinhas in
- Estão excluídos do âmbito da RDC 216/2004:
- Higienização é a operação que compreende duas etapas:
- As instalações sanitárias e os vestiários podem ter comunicação direta com a área de prepa
- O lavatório exclusivo para as mãos na área de manipulação deve dispor de:
- Os produtos saneantes usados na higienização devem ser regularizados na Anvisa e aplicados
- O fluxo de produção deve ser ordenado, linear e sem cruzamentos, para evitar a contaminaçã
- Nas aberturas externas das áreas de armazenamento e preparação de alimentos, a RDC 216 exi
- O reservatório de água do serviço de alimentação deve ser higienizado em intervalo máximo 
- O gelo utilizado em alimentos ou bebidas deve ser fabricado com água potável e mantido em 
- Quando as medidas preventivas não forem suficientes, o controle químico de vetores e praga
- Os coletores de resíduos das áreas de preparação devem ter tampa acionada sem contato manu
- Um manipulador apresenta diarreia. De acordo com a RDC 216, ele deve:
- O uniforme do manipulador pode ser usado no trajeto entre a residência e o local de trabal
- Segundo a RDC 216, os manipuladores devem lavar cuidadosamente as mãos, entre outros momen
- Os manipuladores devem usar cabelos presos e protegidos por redes ou toucas, não sendo per
- Durante a manipulação de alimentos, NÃO é conduta proibida ao manipulador:
- Visitantes que entrem nas áreas de preparação devem cumprir os mesmos requisitos de higien
- As matérias-primas e os ingredientes devem ser armazenados sobre paletes, estrados ou prat
- Na recepção das matérias-primas, devem ser inspecionados, entre outros aspectos:
- Na cocção, a temperatura no centro geométrico do alimento deve atingir, no mínimo:
- O óleo usado em fritura não deve ultrapassar 180 °C e deve ser substituído quando houver a
- É forma de descongelamento admitida pela RDC 216:
- Alimentos descongelados que não foram utilizados podem ser recongelados para uso posterior
- Após a cocção, o alimento preparado mantido aquecido deve permanecer a temperatura:
- No resfriamento de alimento preparado, a temperatura deve ser reduzida de 60 °C para 10 °C
- O prazo máximo de consumo de um alimento preparado e conservado sob refrigeração a 4 °C ou
- Os alimentos congelados devem ser mantidos a temperatura igual ou inferior a −18 °C.
- A medida que mais contribui para evitar a contaminação cruzada no preparo é:
- A RDC 216 exige quatro Procedimentos Operacionais Padronizados (POPs). NÃO é um deles o PO
- O responsável pelas atividades de manipulação pode ser o proprietário ou um funcionário ca
- O Manual de Boas Práticas descreve as operações realizadas pelo estabelecimento, incluindo
- Para hortaliças que serão consumidas cruas, a sequência correta de higienização é:
- Foram compradas 10 kg de batata, que renderam 8 kg após o descascamento. O fator de correç
- São métodos de cocção por calor úmido:
- O branqueamento de vegetais consiste em imersão rápida em água fervente seguida de resfria
- O índice de cocção do arroz é menor que 1, pois o arroz perde peso ao cozinhar.
- Para preservar as vitaminas hidrossolúveis dos vegetais cozidos, recomenda-se:
- A faixa de temperatura entre cerca de 5 °C e 60 °C é considerada zona de perigo, por favor
- Brasear e ensopar, técnicas em que o alimento é dourado e depois cozido em pouco líquido, 
```

## Pedido 32: Recursos eleitorais (`direito-eleitoral.recursos-eleitorais`, 169 tópicos de edital)

```
Matéria: direito-eleitoral.recursos-eleitorais (Recursos eleitorais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos dos recursos eleitorais, prazos e efeitos": 29
- "2. Recursos ordinário e especial: cabimento e admissibilidade": 16
- "3. Embargos de declaração, decisões finais e jurisprudência": 14
- "4. Ação rescisória eleitoral e efeitos de decisões sobre mandatos": 28
- "5. Fidelidade partidária, perda do mandato e revisão de julgados": 13

Já existem (não repita):
- Após sentença em processo eleitoral para o qual não há prazo recursal especial, a parte pr
- Uma decisão eleitoral cassa o registro de candidatura, e a parte interpõe recurso ordinári
- Em representação eleitoral fundada diretamente no art. 96 da Lei nº 9.504/1997, a parte re
- Um acórdão omitiu o exame de argumento relevante em processo eleitoral sem rito especial d
- Uma decisão de mérito do TSE declarou inelegibilidade e transitou em julgado. O interessad
- Em recurso especial eleitoral, o recorrente pede que se reavaliem depoimentos para alterar
- Uma parte quer rescindir sentença eleitoral de primeiro grau, transitada há poucos dias, s
- Uma pessoa foi eleita pelo sistema majoritário e muda voluntariamente de partido durante o
- Um partido toma conhecimento formal da desfiliação de detentor de mandato proporcional. Na
- Durante período eleitoral sujeito a contagem contínua, um candidato afirma que todo recurs
- Em eleição estadual, acórdão regional reconhece inelegibilidade e rejeita o registro da ca
- No especial fundado em divergência interpretativa entre tribunais eleitorais, a parte junt
- A parte interpôs tempestivamente embargos de declaração contra acórdão eleitoral, restando
- A presidência de tribunal regional inadmite recurso especial eleitoral por ausência de pre
- Uma parte apresenta recurso especial eleitoral contra decisão de instância ordinária que a
- Depois de esgotado o prazo de recurso em determinada fase, uma parte invoca o art. 259 do 
- Ciente do resultado de julgamento eleitoral, a parte interpõe recurso antes de publicado f
- Parlamentar proporcional deixa seu partido e passa a legenda recém-criada, sem outros fato
- Acórdão do TSE transitado em julgado mantém indeferimento de candidatura somente por falta
- Durante instrução de processo eleitoral, juiz indefere prova, mas ainda não há sentença de
- Duas decisões eleitorais são recorridas: a primeira trata de representação do art. 41-A po
- Acórdão de ação eleitoral cassa mandato e declara inelegibilidade. Contra ele é interposto
- O tribunal regional rejeita recurso especial por dois fundamentos independentes: ausência 
- Detentor de mandato proporcional pretende migrar para outra legenda. Faltam quarenta dias 
- Acórdão regional fundamenta decisão apenas na interpretação de norma partidária interna. A
- Se a lei eleitoral não estabelecer prazo especial, o recurso apresentado contra decisão pu
- O recurso especial eleitoral pode ser utilizado para reexaminar livremente depoimentos qua
- Os embargos de declaração podem ser utilizados para apontar obscuridade e omissão identifi
- Ação rescisória eleitoral pode ser ajuizada contra qualquer sentença transitada em julgado
- A perda de mandato por desfiliação partidária não se aplica a pessoas eleitas pelo sistema
- Em todo processo eleitoral, a oposição de embargos de declaração obedece ao prazo de três 
- O recurso ordinário que ataca decisão de perda do mandato eletivo recebe, na hipótese do a
- A Súmula-TSE 28 admite demonstrar divergência jurisprudencial mediante mera transcrição de
- A ação por perda de mandato decorrente de desfiliação partidária sem justa causa é de comp
- A existência de fundamento constitucional permite que a parte protocole recurso eleitoral 
- Em recurso extraordinário cabível contra decisão do TSE, o prazo de interposição é de três
- Por se tratar de controle constitucional, o recurso extraordinário eleitoral pode ser apre
- Os embargos de declaração manifestamente protelatórios podem ensejar multa de até dois sal
- O acórdão regional que apenas confirma ou nega medida liminar pode ser combatido imediatam
- Para o ano eleitoral de 2026, o calendário estabeleceu, como regra pertinente aos processo
- A anuência expressa e válida do partido de origem é irrelevante para a fidelidade partidár
- A decisão regional que implica cassação de mandato em eleição municipal não ingressa, pelo
- Para ajuizar ação por infidelidade partidária, o Ministério Público Eleitoral e os interes
- Na ação de perda do cargo por desfiliação, a Res.-TSE 22.610/2007 prevê citação do mandatá
- Uma condenação definitiva no TSE por arrecadação ou gasto ilícito do art. 30-A da Lei 9.50
```

## Pedido 33: Plano Nacional de Educação (PNE) (`educacao.plano-nacional-educacao`, 169 tópicos de edital)

```
Matéria: educacao.plano-nacional-educacao (Plano Nacional de Educação (PNE))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Base Constitucional (Art. 214 da CF/88), Vigência Decenal e Diretrizes do PNE": 20
- "2. Metas de Acesso, Universalização e Qualidade na Educação Básica": 20
- "3. Diversidade, Modalidades, Educação Profissional e Ensino Superior": 20
- "4. Valorização Profissional, Gestão Democrática, Financiamento, Monitoramento e Planos Subnacionais": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- O art. 214 da Constituição Federal estabelece que a lei instituirá o Plano Nacional de Edu
- No que se refere às metas para a Educação Infantil no Plano Nacional de Educação, o tratam
- A meta do Plano Nacional de Educação voltada ao Ensino Fundamental de nove anos abrange a 
- Sobre a meta de Educação em Tempo Integral prevista no Plano Nacional de Educação, assinal
- Acerca da Educação Especial na perspectiva inclusiva dentro do Plano Nacional de Educação,
- Para a Educação de Jovens e Adultos (EJA), o Plano Nacional de Educação estabelece meta es
- No eixo de valorização dos profissionais da educação, o Plano Nacional de Educação estabel
- Qual órgão federal de pesquisa é responsável por publicar, a cada dois anos ao longo da vi
- Em provas de concursos públicos sobre o Plano Nacional de Educação, é recorrente a cobranç
- O art. 214 da Constituição Federal inclui expressamente, entre os objetivos do Plano Nacio
- O Plano Nacional de Educação constitui um plano de governo de duração quadrienal, cuja vig
- Na estrutura da Lei nº 13.005/2014, a meta de alfabetização estabelece que todas as crianç
- A meta de Educação Profissional Técnica de Nível Médio do Plano Nacional de Educação deter
- O Plano Nacional de Educação assegura que todos os professores da educação básica possuam 
- Em provas sobre o PNE, se um item afirmar que os estados e municípios estão proibidos de e
- Ao comparar o rol de objetivos do art. 214 da Constituição Federal com as dez diretrizes d
- Um candidato está estudando para um concurso público em outubro de 2026 e analisa a evoluç
- Uma equipe técnica analisa os indicadores demográficos de jovens de 15 a 17 anos em determ
- No tocante à redução das desigualdades educacionais e à elevação da escolaridade da popula
- Sobre a expansão e a qualidade da Educação Superior no Plano Nacional de Educação, assinal
- O financiamento da educação no Plano Nacional de Educação articula a meta macroeconômica d
- A execução do Plano Nacional de Educação e o cumprimento de suas metas são objeto de monit
- Em uma questão de estudo de caso, narra-se que uma prefeitura pretende nomear diretores de
- Enquanto o inciso V do art. 214 da Constituição Federal menciona a 'promoção humanística, 
- Para que uma escola pública seja contabilizada na meta de Educação em Tempo Integral do PN
- Na meta referente à alfabetização de jovens e adultos (15 anos ou mais), o Plano Nacional 
- A meta de Educação Especial do PNE proíbe qualquer convênio do poder público com instituiç
- O Plano Nacional de Educação estabelece a meta de valorizar os profissionais do magistério
- De acordo com a governança do PNE, as Conferências Nacionais de Educação devem ocorrer sem
- Em provas que abordam a relação entre o PNE e o orçamento público, é correto afirmar que o
- Analise as disposições constitucionais e legais sobre a articulação do Sistema Nacional de
- Considere que um município com 120 escolas públicas e 40.000 estudantes na educação básica
- O fomento à qualidade da educação básica em todas as etapas e modalidades, com melhoria do
- No que tange à expansão das matrículas na Educação Profissional Técnica de Nível Médio e n
- As estratégias do Plano Nacional de Educação voltadas aos planos de carreira e à valorizaç
- Ao elaborar ou adequar seus Planos Estaduais, Distrital e Municipais de Educação em conson
- Em questões avançadas sobre o financiamento da educação no Plano Nacional de Educação, cob
- Uma questão de concurso apresenta um caso longo em que o governador de um estado edita um 
- A Emenda Constitucional nº 59/2009 foi responsável por alterar o caput do art. 214 da Cons
- Segundo a Lei do Plano Nacional de Educação, a superação das desigualdades educacionais re
- Para aferir o cumprimento das metas de redução das desigualdades educacionais e de acesso 
- Na meta de qualificação do corpo docente da educação superior prevista no PNE, exige-se qu
- De acordo com a Lei nº 13.005/2014, o Sistema Nacional de Avaliação da Educação Básica (Si
- O Fórum Nacional de Educação (FNE), além de integrar as instâncias de monitoramento contín
- Nas estratégias de planos de carreira do PNE, prevê-se a meta de que, na rede pública de e
```

## Pedido 34: Direito Internacional Público: Estado, tratados e responsabilidade (`direito-internacional.estado-tratados-responsabilidade`, 168 tópicos de edital)

```
Matéria: direito-internacional.estado-tratados-responsabilidade (Direito Internacional Público: Estado, tratados e responsabilidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estado, povo, território e jurisdição": 15
- "2. Mobilidade, asilo e cooperação jurídica internacional": 21
- "3. Tratados: celebração, consentimento, validade e vigência": 15
- "4. Tratados: reservas, interpretação, registro e extinção": 14
- "5. Responsabilidade internacional, integração e segurança coletiva": 35

Já existem (não repita):
- Uma comunidade ocupa território delimitável, possui residentes permanentes, governo própri
- Um município conta moradores estrangeiros, nacionais residentes e nacionais temporariament
- Uma embarcação estrangeira navega ao largo de uma baía, e o capitão invoca passagem inocen
- Uma liderança política estrangeira pede proteção em território brasileiro, alegando perseg
- Dois Estados celebram por escrito um 'protocolo de cooperação' que institui deveres regido
- Uma pessoa brasileira naturalizada é reclamada por Estado estrangeiro por crime comum prat
- Um Estado edita lei que alcança fraudes contra seus cidadãos praticadas no exterior. Em se
- Um Estado assina tratado sujeito à ratificação, mas, antes de ratificá-lo, pratica ação de
- Uma conferência internacional adota o texto de convenção por votação. Os delegados informa
- Em ação civil, autoridade estrangeira requer no Brasil a obtenção de prova. Não há tratado
- Uma cidadã portuguesa reside habitualmente no Brasil há três anos, possui estatuto de igua
- Um tratado multilateral prevê que entrará em vigor noventa dias após o quinto depósito de 
- Ao aderir a convenção, determinado Estado declara não aplicar obrigação central que consti
- Autoridade estrangeira solicita cumprimento de decisão judicial de produção de prova que p
- Uma autoridade estatal viola obrigação internacional ao atuar funcionalmente além das orde
- Após mudança política em uma região, um país envia socorro humanitário e negocia retirada 
- O Estado X formula reserva admissível a disposição de tratado multilateral. Y apresenta ob
- Ao contestar tratado em vigor, um Estado sustenta que a assinatura violou regra interna so
- Dois Estados descumprem acordo bilateral: um deles viola obrigação essencial, e o outro pr
- Tratado estabelece fronteira entre dois países. Anos depois, profunda mudança econômica le
- Uma norma imperativa de direito internacional geral surge depois da entrada em vigor de tr
- Três Estados celebram acordo de fiscalização ambiental. Um quarto Estado, que não aderiu, 
- Um Estado comete violação de obrigação assumida para proteção de interesse coletivo da com
- Para suspender obrigação internacional, governo aponta crise de abastecimento e invoca est
- Após ruptura democrática em Estado integrante do sistema interamericano, iniciativas diplo
- Em uma federação, cada província possui legislativo próprio. Apesar dessa autonomia intern
- Uma cidade registra estrangeiros residentes em seu censo. Esses estrangeiros passam a inte
- O Brasil recebe pedido de extradição de cidadão brasileiro nato por fato praticado no exte
- Uma pessoa perseguida politicamente que ingressa no Brasil recebe automaticamente asilo te
- A autoridade brasileira pode impor condições de entrada e permanência aos portugueses titu
- Um tratado exige dez instrumentos de ratificação para vigorar. A assinatura e a adoção do 
- Um Estado mantém tratado em vigor, mas sua legislação posterior muda de orientação. Essa a
- Dois Estados ratificam tratado válido e vigente, mas não o registram internacionalmente. P
- O cumprimento no Brasil de carta rogatória fundada em decisão jurisdicional estrangeira de
- Como Estado possui jurisdição para investigar crimes contra seus nacionais no exterior, se
- Um ministro afirma publicamente que 'talvez o país estude uma restrição futura', sem demon
- Um Estado formulou reserva válida a convenção multilateral e, anos depois, pretende retirá
- Um acordo é concluído entre três países para criar obrigações ambientais, e o texto diz qu
- Um governo alega vício constitucional de competência na conclusão de tratado. Pelo art. 46
- Particulares praticam agressões em território estatal sem orientação oficial. Por ter ocor
- Um Estado mantém a prática ilícita mesmo depois de pagar indenização pelas vítimas anterio
- Uma obrigação é qualificada como erga omnes porque é devida à comunidade internacional. De
- Diante de ameaça à paz, o Conselho de Segurança pode considerar medidas sem emprego de for
- Um bloco regional aprova norma comum sobre comércio, e um de seus membros possui procedime
- Um governo considera ameaçado seu funcionamento democrático e solicita assistência no âmbi
```

## Pedido 35: Crescimento, desenvolvimento e ciclos econômicos (`economia.crescimento-desenvolvimento-ciclos`, 168 tópicos de edital)

```
Matéria: economia.crescimento-desenvolvimento-ciclos (Crescimento, desenvolvimento e ciclos econômicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Crescimento de longo prazo: Harrod-Domar e Solow-Swan": 20
- "2. Contabilidade do crescimento, produtividade e inovação endógena": 20
- "3. Escolhas intertemporais: consumo, poupança, investimento e setor externo": 25
- "4. Ciclos econômicos, expectativas e rigidezes nominais": 20
- "5. Desenvolvimento econômico, estruturalismo e transformação produtiva": 15

Já existem (não repita):
- Em determinado país, o PIB real aumentou 3% em um ano, enquanto a população aumentou 5%. Q
- Após aumento permanente da taxa de poupança, uma economia é analisada pelo modelo básico d
- Uma economia obedece à versão elementar Harrod-Domar, sem depreciação, com poupança igual 
- No modelo de Solow com progresso técnico aumentador de trabalho, uma economia se encontra 
- Uma sociedade pretende selecionar, no modelo neoclássico, o estoque de capital estacionári
- Na contabilidade do crescimento Cobb-Douglas, o PIB real cresceu 5%, o capital 6%, o traba
- Uma empresa eleva o produto por trabalhador ao fornecer equipamentos melhores, mantendo co
- Uma política estimula pesquisa e difusão de conhecimento, e o modelo estudado permite que 
- O mercado atribui a uma empresa valor de 300 unidades, enquanto a reposição de seu capital
- Uma economia registra poupança nacional de 18% do PIB e investimento de 23% do PIB, com co
- Um analista utiliza expectativas adaptativas com coeficiente de revisão de 0,5. A previsão
- Uma simulação apresenta choques de produtividade, preços flexíveis e escolhas de horas tra
- Um choque monetário altera temporariamente a produção porque muitas empresas reajustam seu
- Uma região tem amplo contingente de trabalhadores em agricultura tradicional, enquanto sua
- Os preços internacionais da cesta exportada por uma economia primário-exportadora caíram e
- Uma cidade concentra empregos qualificados, atrai fornecedores e amplia serviços, enquanto
- No modelo de Solow Cobb-Douglas, a participação do capital na produção é 0,30, e o objetiv
- Na decomposição Cobb-Douglas com retornos constantes à escala, o PIB cresce 6%, o estoque 
- Uma família tem rendas reais de 100 no período 1 e 110 no período 2, pode emprestar e toma
- Uma firma tem Q médio de 1,6, mas custos de instalação elevados, ativos intangíveis mal me
- Duas economias compartilham a mesma tecnologia Cobb-Douglas e a mesma taxa de poupança e d
- Uma nação financia por vários anos investimentos superiores à poupança nacional mediante e
- Uma economia de baixa renda necessita de indústria têxtil, energia, transporte e consumo m
- Uma equipe observa erros de previsão que não são previsíveis com a informação disponível a
- Um investimento em siderurgia cria demanda por mineração e eletricidade e permite a expans
- Uma economia tem progresso tecnológico positivo e permanece em estado estacionário no capi
- Em uma simulação de ciclos reais que emprega preços flexíveis e choque de produtividade, a
- Mantida relação incremental capital-produto igual a quatro, a versão simples de Harrod-Dom
- Uma empresa instalou máquinas e elevou sua produção por empregado. Por definição contábil,
- No benchmark da teoria do Q de Tobin, a valorização de mercado de ativos produtivos acima 
- Se a poupança nacional equivale a 15% do PIB e o investimento a 20%, o país registra super
- Duas sociedades apresentam PIB por habitante semelhante, mas diferentes indicadores de esc
- Uma economia escolhe o estoque de capital que maximiza o produto por trabalhador efetivo e
- Uma família com mercados de crédito acessíveis compara o consumo de dois períodos a partir
- Ao constatar Q médio de 1,3 para uma empresa com forte restrição de crédito, pode-se deduz
- Com previsão adaptativa anterior de inflação de 3%, inflação efetiva anterior de 7% e coef
- Uma autoridade divulga previsões racionais de inflação. Isso implica que seus resultados o
- Em interpretação do modelo dual de Lewis, um setor moderno pode contratar mão de obra vind
- A tese de Prebisch estabelece, como identidade universal, que o índice de preços das expor
- No Solow com progresso tecnológico exógeno, um aumento permanente da taxa de crescimento d
- A contabilidade do crescimento prova que todo resíduo positivo de produtividade resulta ca
- Um país com investimento maior que poupança nacional e déficit de conta corrente pode fina
- Uma política de grande impulso que sincroniza investimentos em setores complementares repr
- Em um modelo novo-keynesiano com inflação prospectiva e rigidez de preços, alterações no h
- Na hipótese da renda permanente, um recebimento inesperado mas transitório altera obrigato
```

## Pedido 36: Simples Nacional (Lei Complementar nº 123/2006) (`direito-tributario.simples-nacional-lc-123`, 168 tópicos de edital)

```
Matéria: direito-tributario.simples-nacional-lc-123 (Simples Nacional (Lei Complementar nº 123/2006))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, conceito de ME e EPP e gestão": 20
- "2. Tributos abrangidos, opção e vedações": 20
- "3. Cálculo, MEI e recolhimento": 20
- "4. Exclusão do Simples Nacional": 20
- "5. Fiscalização, contencioso e processo judicial": 20

Já existem (não repita):
- Segundo a LC nº 123/2006, considera-se microempresa a pessoa jurídica ou empresário que au
- Para a LC nº 123/2006, a receita bruta compreende o produto da venda de bens e serviços, o
- No caso de início de atividade no próprio ano-calendário, o limite de receita bruta para e
- O Comitê Gestor do Simples Nacional (CGSN), vinculado ao Ministério da Fazenda, é composto
- Segundo a LC nº 123/2006, o Simples Nacional deve observar, entre outros, os princípios da
- Pela LC nº 123/2006, toda nova obrigação que atinja as microempresas e empresas de pequeno
- O Simples Nacional implica o recolhimento mensal, mediante documento único de arrecadação,
- O recolhimento pelo Simples Nacional exclui a incidência do FGTS e da contribuição previde
- NÃO está abrangido pelo recolhimento unificado do Simples Nacional, devendo seguir a legis
- Segundo a LC nº 123/2006, a opção pelo Simples Nacional:
- A opção pelo Simples Nacional implica aceitação de sistema de comunicação eletrônica, cuja
- Segundo o art. 17 da LC nº 123/2006, NÃO poderá recolher tributos na forma do Simples Naci
- É vedada a opção pelo Simples Nacional à empresa que realize cessão ou locação de mão de o
- Entre as atividades vedadas ao Simples Nacional pelo art. 17 da LC nº 123/2006 está:
- A microempresa que se dedique à prestação de serviços não vedados expressamente pela LC nº
- Para determinar a alíquota nominal aplicável no Simples Nacional, o contribuinte utiliza:
- Na repartição do valor apurado no Simples Nacional, o percentual efetivo máximo destinado 
- Segundo a LC nº 123/2006, considera-se Microempreendedor Individual (MEI), entre outros re
- O MEI recolhe os tributos abrangidos pelo Simples Nacional em valores fixos mensais, indep
- NÃO poderá optar pela sistemática de recolhimento do MEI o empresário que:
- Na hipótese de a microempresa ou empresa de pequeno porte possuir filiais, o recolhimento 
- Na retenção na fonte do ISS de empresa optante pelo Simples Nacional, se o serviço for pre
- Os valores do Simples Nacional a serem restituídos ou compensados são acrescidos de juros 
- A exclusão do Simples Nacional mediante comunicação, por opção da empresa, produzirá efeit
- Quando a empresa que não está no ano de início de atividade ultrapassar em mais de 20% o l
- No ano-calendário de início de atividade, a empresa que ultrapassar em mais de 20% o limit
- A empresa excluída do Simples Nacional por possuir débito sem exigibilidade suspensa ou ir
- Segundo a LC nº 123/2006, a alteração de dados no CNPJ equivale à comunicação obrigatória 
- É hipótese de exclusão de ofício do Simples Nacional:
- Será excluída de ofício a empresa em que se constate, durante o ano-calendário, que o valo
- Nas hipóteses de exclusão de ofício dos incisos II a XII do art. 29, a exclusão produz efe
- Para fins de exclusão de ofício, considera-se prática reiterada a ocorrência, em dois ou m
- A competência para fiscalizar o cumprimento das obrigações principais e acessórias do Simp
- As autoridades fiscais competentes para fiscalizar o Simples Nacional podem efetuar o lanç
- A competência para autuar a empresa optante pelo Simples Nacional por descumprimento de ob
- O contencioso administrativo relativo ao Simples Nacional é de competência dos órgãos julg
- Segundo a LC nº 123/2006, os processos judiciais relativos a impostos e contribuições abra
- Em procedimento de fiscalização do Simples Nacional, iniciada a ação fiscal, ela poderá ab
- Segundo a LC nº 123/2006, a empresa de pequeno porte é aquela que aufere, em cada ano-cale
- A exclusão do Simples Nacional, segundo a LC nº 123/2006, pode ser feita:
```

## Pedido 37: Realidade do Pará e Questão Agrária e Minerária (`para.conhecimentos-estaduais`, 168 tópicos de edital)

```
Matéria: para.conhecimentos-estaduais (Realidade do Pará e Questão Agrária e Minerária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação histórica: da conquista à Cabanagem": 20
- "2. Borracha, integração nacional e grandes projetos": 20
- "3. Geografia física e regional": 20
- "4. Sociedade, povos, cultura, economia e política": 20
- "5. Questão agrária, minerária e conflitos territoriais": 20

Já existem (não repita):
- A fundação de Belém, em 1616, esteve ligada à construção do Forte do Presépio, cujo objeti
- No período colonial, o Estado do Maranhão (depois Grão-Pará e Maranhão) era administrativa
- A economia colonial da Amazônia paraense baseava-se principalmente:
- Entre as medidas pombalinas no Grão-Pará estão a expulsão dos jesuítas, a criação do Diret
- O Pará aderiu à Independência do Brasil em:
- Sobre a Cabanagem (1835–1840), assinale a alternativa correta.
- A Cabanagem foi uma revolta restrita à cidade de Belém, sem participação de populações do 
- No ciclo da borracha, o sistema de aviamento caracterizava-se por:
- O Theatro da Paz e o mercado de ferro do Ver-o-Peso são marcas da Belle Époque de Belém, f
- O declínio do ciclo da borracha amazônica, a partir da década de 1910, deveu-se principalm
- Nas décadas de 1960 e 1970, a ocupação do Pará foi estimulada por:
- O minério de ferro extraído na Serra dos Carajás é escoado pela Estrada de Ferro Carajás a
- Relacione corretamente os empreendimentos do Pará aos rios em que estão:
- O complexo industrial de Barcarena, com as empresas Albrás e Alunorte, destaca-se na produ
- Serra Pelada, nos anos 1980, ficou conhecida como um dos maiores garimpos de ouro a céu ab
- Em extensão territorial, o Pará é:
- O Pará faz fronteira internacional com o Suriname e a Guiana.
- NÃO faz divisa com o Pará o estado de:
- A Ilha do Marajó é considerada a maior ilha fluviomarinha do mundo e é conhecida pela cria
- A cidade paraense situada no encontro dos rios Tapajós e Amazonas, centro regional do Baix
- Sobre a natureza do Pará, assinale a correta.
- O chamado "inverno amazônico" corresponde, no Pará, ao período de temperaturas mais baixas
- O primeiro território quilombola titulado no Brasil, em 1995, foi o da comunidade Boa Vist
- Kayapó, Munduruku, Tembé e Parakanã são exemplos de povos indígenas presentes no territóri
- O Círio de Nazaré, maior manifestação religiosa do Pará, ocorre em Belém:
- São manifestações ou elementos típicos da cultura paraense:
- O principal produto da pauta de exportações do Pará é:
- O Pará é o maior produtor nacional de açaí e tem um dos maiores rebanhos bovinos do país.
- No plebiscito de 2011 sobre a divisão do Pará, a população:
- Belém foi a cidade-sede da COP30, a Conferência das Nações Unidas sobre Mudança do Clima, 
- O Massacre de Eldorado dos Carajás, ocorrido em 17 de abril de 1996, consistiu:
- A missionária Dorothy Stang, assassinada em 2005 em Anapu, atuava na defesa de assentament
- A grilagem de terras, frequente na história fundiária do Pará, consiste em:
- Contribuiu para a complexidade fundiária do Pará a federalização de terras ao longo das ro
- O principal vetor do desmatamento no "arco do desmatamento" do sul e sudeste do Pará tem s
- O garimpo ilegal de ouro na bacia do Tapajós provoca, entre outros impactos:
- A desoneração do ICMS sobre exportações de produtos primários, conhecida como Lei Kandir, 
- Sobre os conflitos territoriais no Pará, assinale a alternativa correta.
- A chacina de Pau D'Arco, em 2017, foi uma ação de grileiros contra uma aldeia indígena iso
- São respostas institucionais aos conflitos territoriais no Pará:
```

## Pedido 38: Educação em saúde e educação popular em saúde (`saude.educacao-em-saude`, 167 tópicos de edital)

```
Matéria: saude.educacao-em-saude (Educação em saúde e educação popular em saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos e concepções da educação em saúde": 20
- "Paulo Freire e Política Nacional de Educação Popular em Saúde": 20
- "Comunicação em saúde e metodologias educativas": 20
- "Educação na Atenção Primária, nas escolas e participação comunitária": 20
- "Planejamento, monitoramento e avaliação de ações educativas": 20

Já existem (não repita):
- A entrega de panfletos pode ser recurso de uma ação educativa, mas sua distribuição isolad
- Em educação em saúde, autonomia significa que o usuário é o único responsável por superar 
- A educação permanente em saúde tem como foco característico a aprendizagem dos trabalhador
- Na perspectiva problematizadora, a equipe deve renunciar ao conhecimento científico para e
- A problematização figura entre os princípios da Política Nacional de Educação Popular em S
- A intersetorialidade e os diálogos multiculturais compõem um dos eixos estratégicos da PNE
- Amorosidade, na PNEPS-SUS, dispensa escuta, fundamentação técnica e compromisso com os dir
- Na perspectiva freireana, práxis articula reflexão e ação transformadora, em movimento que
- O uso de expressões técnicas é suficiente para demonstrar credibilidade, mesmo quando os p
- Pedir à pessoa que relate, com suas palavras, o que fará em casa pode servir para verifica
- Uma roda de conversa é necessariamente participativa quando todos os presentes permanecem 
- As metodologias ativas permitem que os participantes analisem problemas e tomem parte na c
- A comunicação de riscos deve afirmar certeza absoluta sobre eventos ainda incertos para ev
- O Programa Saúde na Escola constitui estratégia de articulação entre políticas e serviços 
- No PSE, o diagnóstico local e o contexto da escola são dispensáveis quando a secretaria de
- Grupos de Trabalho Intersetoriais do PSE favorecem planejamento e gestão compartilhados en
- Participação comunitária se esgota na presença física de moradores em eventos previamente 
- O número de encontros realizados é indicador de processo, mas, isoladamente, não demonstra
- A aferição de satisfação imediatamente após uma oficina é prova suficiente de impacto dura
- Uma avaliação educativa consistente pode combinar indicadores quantitativos, relatos dos p
- Após constatar baixa adesão a orientações de autocuidado, uma equipe quer organizar uma at
- A característica que melhor diferencia a educação em saúde dialógica da mera difusão de in
- Uma ação educativa atribui a famílias a culpa por não obterem água tratada, desconsiderand
- Ao distinguir educação permanente em saúde de uma oficina para usuários, assinale o enunci
- Uma usuária recebe recomendações, mas relata que não consegue segui-las em razão da jornad
- Conforme a PNEPS-SUS, qual dos conjuntos abaixo contém apenas princípios expressamente pre
- A expressão freireana educação bancária é usada, sobretudo, para criticar:
- Assinale o par cuja classificação na PNEPS-SUS está correta.
- O conceito de práxis, na educação popular de inspiração freireana, corresponde a:
- Ao elaborar uma intervenção inspirada na PNEPS-SUS, qual prática expressa mais claramente 
- Uma pessoa idosa pede que o profissional esclareça uma orientação com termos mais simples.
- Uma equipe coloca cadeiras em círculo, mas só o profissional fala e proíbe intervenções. O
- Em oficina sobre leitura de orientações de cuidados, qual estratégia corresponde melhor ao
- Durante evento de risco ambiental ainda em investigação, qual conteúdo deve constar de com
- Ao orientar um cuidador após atendimento, a técnica de retorno da explicação deve ser util
- Qual ação corresponde à lógica do Programa Saúde na Escola?
- Ao organizar uma atividade no âmbito do PSE, a decisão sobre o tema deve considerar priori
- Em discussão sobre os GTIs do PSE, é correto afirmar que esses grupos:
- Uma unidade convida moradores apenas para validar uma decisão já concluída. Qual modificaç
- Em atividade educativa escolar, qual situação mais claramente exige articulação intersetor
- Em uma avaliação de oficina, qual indicador mede mais diretamente um resultado de aprendiz
- Uma equipe deseja classificar os indicadores de projeto educativo. Qual é um indicador de 
- Qual instrumento é mais apropriado para compreender por que parte do grupo não consegue co
- Após comparar questionários antes e depois de um grupo, a equipe encontrou melhor desempen
- Uma avaliação participativa identifica que a oficina foi bem recebida, mas não atingiu par
```

## Pedido 39: Geopolítica e geografia econômica mundial (`geografia.geopolitica-geografia-economica-mundial`, 166 tópicos de edital)

```
Matéria: geografia.geopolitica-geografia-economica-mundial (Geopolítica e geografia econômica mundial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Espaço geográfico, território e teorias geopolíticas": 19
- "2. Capitalismo, pós-guerra, sistemas e desenvolvimento": 23
- "3. Comércio, serviços, trabalho e divisão internacional": 20
- "4. Integração regional, Mercosul e finanças externas": 17
- "5. Poder mundial, conflitos e desafios socioambientais": 21

Já existem (não repita):
- Duas cidades cresceram após a construção de um corredor ferroviário, e empresas passaram a
- Um povo mantém vínculos linguísticos e históricos, embora seus integrantes residam sob a a
- Uma análise sobre rotas oceânicas, bases navais e a capacidade de uma potência proteger se
- Um país cresceu 4% em termos reais, mas sua renda ficou mais concentrada e o acesso a sane
- Após a guerra, uma instituição auxiliou países com desequilíbrios de pagamentos externos, 
- Uma empresa projeta equipamentos em um país, compra chips de outro e realiza a montagem fi
- Uma economia exportou 80 bilhões em mercadorias e importou 95 bilhões no mesmo ano. Consid
- Uma pessoa viaja a outro país e contrata ali hospedagem e passeios turísticos. No GATS, a 
- Quatro países assinaram um tratado em 1991 para constituir um mercado comum sul-americano.
- Um bloco elimina boa parte das tarifas internas e aplica uma tarifa externa comum, embora 
- Uma pesquisa classifica países pelo IDH, enquanto outra utiliza renda nacional bruta per c
- Uma economia paga 24 bilhões ao ano entre juros e amortização de dívida externa e recebe 2
- Uma empresa situada no Brasil contrai um empréstimo em reais com um banco não residente. P
- Uma região recebe centros de dados, terminais automatizados e cabos de alta capacidade, en
- Uma empresa financeira abre uma filial permanente em outro país para atender consumidores 
- Uma aula sobre a ordem monetária do pós-guerra menciona o anúncio de 1971 que encerrou a c
- Um país vende sobretudo produtos primários e passa a receber preços maiores por suas expor
- Uma potência busca controlar corredores litorâneos da Eurásia para limitar a projeção de o
- Um país amplia exportações industriais pela montagem de componentes importados, mas suas e
- Uma economia registra saldo de bens de +18, saldo de serviços de −9, renda primária de −14
- Ao comparar os vínculos de Bolívia e Venezuela com o Mercosul em 2026, uma análise precisa
- Uma empresa sediada fora do país envia especialistas por três semanas para executar pessoa
- Uma economia tinha serviço da dívida de 20 bilhões e exportações de 200 bilhões. No ano se
- Em votação substantiva no Conselho de Segurança, uma proposta recebeu 14 votos favoráveis 
- Um plano climático combina eletrificação do transporte com barreiras de contenção em áreas
- Em um município, a instalação de um porto e de vias de acesso modifica usos do solo e ampl
- Uma comunidade com idioma e tradições comuns só pode ser considerada nação quando também d
- A Organização Mundial do Comércio foi criada na conferência de Bretton Woods de 1944, ao l
- Na nova divisão internacional do trabalho, pesquisa, fabricação de componentes, montagem e
- O Tratado de Assunção, assinado em 1991 por Argentina, Brasil, Paraguai e Uruguai, constit
- Em política climática, a construção de barreiras contra enchentes constitui mitigação, enq
- A elevação do PIB per capita de um país, isoladamente, comprova simultaneamente queda da d
- O aumento do número de serviços de tecnologia, logística e finanças numa economia pode ele
- Na classificação econômica da dívida externa, a denominação em dólar é condição suficiente
- Uma empresa que estabelece uma filial permanente no exterior para prestar seguros utiliza 
- Uma análise do Mercosul em 2026 pode tratar a Venezuela como participante plenamente habil
- Um território com fibras ópticas, portos automatizados e serviços avançados pode integrar-
- A meta coletiva do Acordo de Paris é manter a elevação da temperatura global bem abaixo de
- Ao formar uma união aduaneira, dois países eliminam de imediato qualquer possibilidade de 
- Se o preço médio das exportações de um país sobe enquanto os preços de suas importações e 
- Uma economia que adota empresas estatais, planejamento de infraestrutura e crédito público
- Em discussão do Conselho de Segurança da ONU sobre medida substantiva, o voto negativo de 
- Um país paga serviço anual de dívida externa de 12 bilhões e exporta 120 bilhões; após que
- Uma consultoria transmitida digitalmente do país de origem ao cliente situado no exterior 
- Famílias que deixam suas casas por um conflito armado, mas permanecem dentro das fronteira
```

## Pedido 40: Interceptação telefônica e telemática (`legislacao-penal-especial.interceptacao-telefonica`, 166 tópicos de edital)

```
Matéria: legislacao-penal-especial.interceptacao-telefonica (Interceptação telefônica e telemática)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constituição, sigilo e modalidades de gravação": 21
- "2. Requisitos, legitimados e decisão judicial": 19
- "3. Prazos, execução, sigilo e inutilização": 16
- "4. Prova fortuita, compartilhamento e comunicação digital": 19
- "5. Captação ambiental, infrações penais e integração normativa": 25

Já existem (não repita):
- Durante apuração de crimes punidos com reclusão, investigadores precisam captar chamadas e
- Uma participante grava no próprio aparelho uma ligação da qual toma parte, sem avisar a ou
- Uma investigação dispõe de depoimento suficiente e documento autêntico apto a demonstrar o
- A autoridade policial apresentou requerimento regular de interceptação, com descrição dos 
- Em caso de interceptação deferida judicialmente, qual órgão conduz os procedimentos e como
- O juiz autorizou interceptação de ligações em investigação, fixando o período máximo permi
- Em investigação penal, comunicações foram captadas licitamente e diversos trechos revelara
- A investigação demanda captação do áudio de uma conversa ocorrida presencialmente numa sal
- Uma decisão judicial autorizou interceptação telefônica para investigar fatos punidos apen
- Interceptação autorizada por quinze dias revelou continuidade de um esquema. Passado o pri
- Os investigadores não conseguem identificar integralmente o titular de uma linha, mas desc
- A necessidade de interceptar surge em circunstância excepcional e a autoridade apresenta p
- No curso da instrução processual penal, o representante do Ministério Público requer inter
- Um pedido pretende captar ambientalmente conversas sobre infração cuja pena máxima é de ci
- Após autorização judicial, a autoridade policial encaminhou resultado da interceptação ao 
- Uma interceptação foi executada sob sigilo e seu material permanece separado dos autos pri
- Em investigação digital, uma diligência intercepta mensagens enquanto são transmitidas por
- Durante apuração inicial, os elementos indicavam competência do juízo que autorizou interc
- Na instrução, a acusação juntou somente a degravação de trechos selecionados. A defesa ped
- Interceptação regularmente autorizada para apurar um delito revelou fortuitamente notícia 
- Uma pessoa participa da conversa captada no ambiente e registra ela própria o diálogo. Inv
- Uma autoridade judicial determina captação ilícita com finalidade não autorizada em lei, a
- Captação ambiental foi autorizada por quinze dias e há pedido de renovação porque a ativid
- Uma pessoa registrou diálogo ambiental do qual participou sem ciência prévia da polícia. N
- Um enunciado tenta aplicar à interceptação telefônica o requisito de pena máxima superior 
- Em investigação criminal, uma ordem judicial fundamentada, submetida a segredo de justiça,
- Uma equipe policial pode iniciar interceptação telefônica sem ordem judicial, desde que co
- Durante transmissão de comunicação por aplicativo, terceiro capta o fluxo telemático media
- Há indícios robustos de autoria de fato punido somente com detenção. Como o requisito de i
- O pedido de interceptação é apresentado com demonstração de necessidade. O juiz deve decid
- Depois de duas autorizações de quinze dias cada, novas renovações judicialmente motivadas 
- Terminada uma interceptação regularmente autorizada, a autoridade policial encaminha ao ju
- Depois da denúncia, a defesa não pode acessar os áudios completos porque os autos de inter
- Gravação sem interesse para a prova pode ser inutilizada por decisão judicial requerida pe
- Uma renovação de interceptação telefônica pode ser deferida por despacho padronizado que a
- Para captação ambiental judicialmente solicitada, o art. 8º-A, § 1º, exige descrição circu
- Para deferir interceptação telefônica, o art. 2º da Lei nº 9.296/1996 exige que a infração
- O art. 10-A, § 1º, expressamente afasta o crime de captação ambiental não autorizada na hi
- Se o investigador extrai de aparelho apreendido um histórico já armazenado de mensagens, a
- Interceptação originalmente lícita pode servir como prova emprestada em outro procedimento
- A descoberta posterior de que outro juízo era competente torna automaticamente nula uma in
- A autorização judicial formal não impede, por si só, a incidência do art. 10 quando a inte
- Por permitir instalação noturna mediante operação disfarçada, o art. 8º-A, § 2º, também au
- Para renovar captação ambiental por novo período de quinze dias, o art. 8º-A, § 3º, exige 
- Embora a defesa tenha acesso às gravações integrais e possa contraditar os trechos selecio
```

## Pedido 41:  fundamentos, princípios e sistemas eleitorais (`direito-eleitoral.fundamentos-sistemas-eleitorais`, 166 tópicos de edital)

```
Matéria: direito-eleitoral.fundamentos-sistemas-eleitorais ( fundamentos, princípios e sistemas eleitorais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, autonomia e fontes do Direito Eleitoral": 28
- "2. Princípios constitucionais e interpretação eleitoral": 21
- "3. Sistemas eleitorais majoritário e proporcional": 16
- "4. Quocientes, votação mínima e distribuição das sobras": 16
- "5. Organização territorial eleitoral e integração das regras": 19

Já existem (não repita):
- Uma equipe define o ramo jurídico que disciplina a formação da representação política e a 
- Em debate sobre participação popular, uma estudante diferencia sufrágio e voto. Qual alter
- Uma lei publicada oito meses antes de uma eleição modifica regras relevantes do processo e
- Um município possui 180 mil eleitores regularmente inscritos e realizará eleição para pref
- Ao explicar a eleição para o Senado, uma pessoa procura o sistema e o fundamento constituc
- Em disputa proporcional com 100 mil votos válidos e dez vagas, uma comissão calcula o quoc
- O QE calculado é 10.000; um partido obteve 25 mil votos válidos. Qual o quociente partidár
- Uma pessoa confunde circunscrição eleitoral com divisão de cartório. Nas eleições municipa
- Num mesmo prédio escolar há várias salas com urnas e mesas de recepção de votos. Como se d
- Uma consulta em tese sobre interpretação de regra eleitoral é apresentada à Justiça Eleito
- Uma candidatura obtém 50 mil votos válidos entre 100 mil votos válidos totais em disputa q
- Um município registra exatamente 200 mil eleitores no pleito para prefeito. A regra de seg
- Um partido recebeu 20 mil votos de candidatos e 5 mil votos de legenda em pleito proporcio
- Com QE igual a 12.000, partido com QP igual a três dispõe de apenas duas candidaturas acim
- Em eleição proporcional, termina a etapa de sobras limitada aos filtros de 80% e 20%, rest
- Uma pessoa afirma que deputados federais são escolhidos por circunscrição nacional, por in
- A equipe de apuração precisa aplicar o arredondamento do QE. Com 120.005 votos válidos e 1
- Uma agremiação recebeu 28.000 votos e já obteve duas cadeiras; outra recebeu 18.000 votos 
- Nenhum partido atinge um QE inteiro. Uma pessoa defende eleger diretamente os candidatos m
- Um instituto de pesquisa divulga que a cidade tem 250 mil habitantes e 190 mil eleitores. 
- Um partido recebe 7.900 votos, com QE igual a 10.000. Na primeira etapa de sobras, mantém 
- No desempate entre candidaturas de mesmo partido, com idêntica votação nominal, a comissão
- Para definir representação dos Estados na Câmara dos Deputados, um parecer afirma que cada
- Em uma eleição, agremiações buscam atuar juntas no pleito proporcional como coligação temp
- Uma lei estadual estabelece nova forma de contar votos válidos apenas nas eleições legisla
- Uma jovem de 17 anos já está regularmente alistada. Segundo a Constituição, o exercício do
- Uma lei que altera o processo eleitoral somente passa a existir juridicamente um ano após 
- Em eleição para vereador, os votos de legenda válidos podem integrar a base de cálculo dos
- Na eleição para senador, a distribuição de cadeiras por Estado é determinada pela divisão 
- No território de uma unidade federativa, uma única zona eleitoral pode abranger partes de 
- Uma pessoa analfabeta pode votar facultativamente e, por isso, cumpre automaticamente o re
- Um município com 300 mil eleitores poderá ter segundo turno para prefeito se nenhuma candi
- No cálculo do QP, uma fração de 0,9 é arredondada para a unidade seguinte pela mesma regra
- Na atribuição inicial de cadeiras conforme o QP, a candidatura deve ter votação nominal ig
- Uma coligação partidária criada para disputar vagas de vereador pode receber votos de lege
- Segundo o Código Eleitoral, art. 86, na eleição presidencial a circunscrição é o país, enq
- Como uma resolução eleitoral é editada para executar a legislação, ela pode alterar direta
- O Tribunal Superior Eleitoral pode regulamentar a execução da lei eleitoral, mas essa comp
- Ao distribuir sobras na etapa posterior ao esgotamento do filtro de 80% partidário e 20% i
- Para o cálculo das maiores médias, o denominador considera as vagas já conquistadas pela a
- Mesmo quando ninguém alcança o quociente eleitoral, a regra atual permite substituir a ele
- Para uma unidade federativa que elege 14 deputados federais, a regra constitucional sobre 
- O sistema proporcional brasileiro atribui necessariamente uma cadeira à pessoa mais votada
- A representação senatorial é de três cadeiras por Estado e Distrito Federal, com mandato d
- Em uma eleição municipal, a circunscrição para vereador pode ser reduzida a um bairro semp
```

## Pedido 42: Alergologia clínica e imunodeficiências (`medicina.reumatologia-imunologia-alergia`, 166 tópicos de edital)

```
Matéria: medicina.reumatologia-imunologia-alergia (Alergologia clínica e imunodeficiências)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos imunológicos e investigação alergológica": 17
- "Asma alérgica, rinite e alergias respiratórias e ocupacionais": 17
- "Urticária e angioedema: fenótipos e tratamento": 17
- "Anafilaxia, venenos de insetos e emergências alérgicas": 17
- "Alergia alimentar, medicamentos e hipersensibilidade ao látex": 16
- "Erros inatos da imunidade e imunoterapia com alérgenos": 16

Já existem (não repita):
- Paciente com espirros ao limpar a casa tem IgE específica positiva para ácaros, mas relata
- Teste de puntura positivo a um aeroalérgeno, por si só, comprova que o alérgeno causa os s
- Mulher apresenta eczema recorrente 48 horas após contato com componentes metálicos de biju
- Em investigação de possível alergia alimentar imediata, qual atitude evita interpretações 
- O teste de contato epicutâneo é especialmente útil na investigação de dermatite alérgica d
- Homem teve reação sistêmica durante cirurgia e realizou coleta de triptase várias horas de
- A IgE total normal descarta alergia alimentar IgE-mediada e torna desnecessários os demais
- O método de escolha para estudar qualquer reação medicamentosa é sempre um painel inespecí
- Adulto com rinite alérgica persistente apresenta obstrução nasal, prurido e prejuízo do so
- Rinite alérgica pode coexistir com asma, razão pela qual sintomas nasais e brônquicos deve
- Jovem com asma usa apenas salbutamol quando sente chiado e relata duas exacerbações recent
- O controle atual de sintomas de asma e o risco futuro de exacerbações são dimensões que po
- Trabalhador de padaria apresenta tosse e sibilância que pioram durante a semana, melhoram 
- Uma paciente tem rinite importante apesar de monoterapia intranasal bem utilizada. Qual af
- Toda combinação de corticoide inalatório com broncodilatador de longa ação é intercambiáve
- Melhora de sintomas respiratórios nas férias e piora ao retornar ao trabalho é evidência c
- Paciente relata habões pruriginosos que aparecem e desaparecem, cada um em poucas horas, h
- Urticária crônica é definida por habões ou angioedema recorrentes que persistem por mais d
- Qual abordagem inicial é mais compatível com a diretriz internacional de urticária de 2026
- O uso contínuo de corticoide sistêmico é a conduta de primeira linha recomendada para cont
- Homem em uso de enalapril há três anos apresenta edema de língua sem prurido nem urticária
- Paciente tem edema episódico de lábios e parede intestinal, sem urticária, com história fa
- Angioedema relacionado a inibidor da enzima conversora da angiotensina pode começar após a
- C1-inibidor com concentração normal sempre exclui angioedema hereditário relacionado a C1-
- Paciente desenvolve urticária difusa, chiado e hipotensão minutos após picada de vespa. Qu
- Na anafilaxia, a adrenalina intramuscular é a medida medicamentosa inicial de escolha e nã
- Após antibiótico intravenoso, uma paciente apresenta estridor e queda de pressão, sem lesõ
- A ausência de manifestações cutâneas impede o diagnóstico clínico de anafilaxia, mesmo apó
- Adulto sofreu anafilaxia após picada de abelha e, na avaliação especializada, demonstrou s
- Mulher foi tratada por anafilaxia com duas doses de adrenalina e apresenta asma com contro
- Uma triptase não elevada em episódio suspeito de anafilaxia alimentar basta para descartar
- A imunoterapia com veneno de himenópteros é intervenção preventiva possível após reação si
- Criança tem urticária e vômitos repetidos minutos após ingerir castanha em duas ocasiões. 
- Intolerância à lactose e alergia às proteínas do leite de vaca são conceitos intercambiáve
- Paciente relata diarreia isolada após antibiótico, sem exantema, urticária ou sintomas res
- O diagnóstico de alergia alimentar pode necessitar de provocação oral supervisionada quand
- Paciente rotulado alérgico à penicilina teve exantema leve e remoto, sem lesões mucosas, f
- Mulher apresentou necrólise epidérmica tóxica atribuída a medicamento no passado. Qual ati
- História isolada de diarreia após amoxicilina comprova alergia a todos os betalactâmicos e
- Síndrome de Stevens-Johnson atribuída a um fármaco é indicação usual de provocação oral pa
- Uma criança apresenta repetidas pneumonias bacterianas, otites e sinusites, com dificuldad
- Deficiências predominantes de anticorpos podem produzir infecções respiratórias bacteriana
- Adulto com rinite alérgica moderada/grave persistente tem teste positivo para ácaro clinic
- Teste positivo para alérgeno, sem sintomas relacionados à exposição, é indicação suficient
- Lactente apresenta candidíase persistente, diarreia grave, falha de crescimento e infecçõe
- Paciente tem asma atualmente descontrolada e teste positivo para pólen, pretendendo inicia
- Lactente com suspeita de imunodeficiência combinada grave precisa de investigação urgente 
- A imunoterapia subcutânea com aeroalérgenos deve ser iniciada imediatamente em qualquer pa
```

## Pedido 43: Zootecnia e produção animal (`agropecuaria.zootecnia-producao-animal`, 165 tópicos de edital)

```
Matéria: agropecuaria.zootecnia-producao-animal (Zootecnia e produção animal)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da produção animal e instalações": 17
- "Bovinocultura de leite e de corte": 17
- "Suinocultura e avicultura de corte e postura": 17
- "Ovinocaprinocultura e piscicultura": 17
- "Nutrição, alimentação animal e forragicultura": 16
- "Reprodução, genética, sanidade e biosseguridade": 16

Já existem (não repita):
- Uma fazenda contabilizou ganho de 180 kg de peso vivo a partir de 360 kg de ração consumid
- Qual indicador é mais indicado para comparar o ganho de peso de bovinos pesados em interva
- Na maternidade de uma granja tecnificada, matrizes e leitões recém-nascidos apresentam nec
- Em bovinocultura de corte, como se denomina a fase que acompanha o animal após a desmama, 
- No monitoramento da qualidade do leite, qual medida tem relação direta com o acompanhament
- Um novilho chega ao abate com peso vivo de 500 kg e peso de carcaça de 275 kg. Qual é seu 
- A CBT do tanque de leite caiu após melhora do resfriamento, mas a CCS permaneceu elevada. 
- Qual sequência apresenta corretamente as fases do suíno depois da gestação?
- Qual comparação entre frango de corte e poedeira comercial está correta?
- Um produtor pretende chamar de “orgânico” todo frango criado com acesso a piquete, sem con
- Em uma maternidade suína houve aumento de leitões esmagados e sinais de frio no abrigo. Qu
- Como se denomina a estrutura flutuante telada, permeável à água, em que os peixes ficam co
- Uma piscicultura contém 1.200 peixes vivos de peso médio 0,5 kg e utiliza, neste exemplo, 
- Um criador de ovinos registrou baixa resposta após vermifugações indiscriminadas repetidas
- Após noite quente e nublada, peixes ficam ofegantes na superfície ao amanhecer. Qual variá
- Um animal recebe 20 kg de silagem com 35% de matéria seca. Quantos quilogramas de matéria 
- Sobre processos de conservação de plantas forrageiras, qual associação está correta?
- Um ingrediente A tem 10% de proteína e B tem 40%. Pelo quadrado de Pearson, para mistura-a
- Sobre a inseminação artificial em tempo fixo (IATF), qual conclusão é tecnicamente adequad
- Um candidato afirma que herdabilidade de 0,40 indica que 40% da massa de qualquer animal s
- Na definição usual de conversão alimentar como ração consumida dividida por ganho de peso,
- Criação extensiva é, por definição, criação sem controle sanitário, sem registros e sem qu
- Para comparar mortalidade entre dois lotes, a unidade temporal e o número de animais de re
- Aumentar a lotação de uma pastagem, mesmo quando falta forragem na seca, garante aumento s
- A fase de cria em bovinos de corte envolve reprodução e desenvolvimento do bezerro até a d
- Refrigerar o leite elimina toda contaminação presente e torna desnecessária a limpeza dos 
- Rendimento de carcaça e proporção de carne desossada comercializável são indicadores neces
- Um rebanho de leite pode apresentar produção elevada por vaca e, ainda assim, ter menor ma
- A creche suína é uma fase tipicamente posterior à maternidade, na qual o leitão se adapta 
- Pintinhos de corte recém-alojados costumam precisar de mais aquecimento do que frangos em 
- O sistema todos dentro/todos fora consiste em misturar continuamente lotes de idades difer
- Na produção de ovos comerciais, indicadores relevantes incluem percentual de postura, mass
- Tanques-rede estão sujeitos às condições do reservatório que os contém, incluindo circulaç
- Em água de piscicultura, pH elevado pode ampliar a fração de amônia não ionizada, aumentan
- Todo suplemento mineral destinado a bovinos pode ser fornecido a ovinos sem avaliar o teor
- Vermifugar ovinos e caprinos continuamente, sem monitoramento e sempre com o mesmo produto
- A matéria seca é uma base apropriada para comparar alimentos com umidade diferente, evitan
- O quadrado de Pearson determina automaticamente todos os aminoácidos, minerais e energia a
- A inseminação artificial em tempo fixo assegura que todas as fêmeas inseminadas ficarão ge
- PNCEBT, PNSA e PNSS correspondem a programas sanitários distintos, respectivamente ligados
```

## Pedido 44: Lei 8.666/1993 e pregão (Lei 10.520/2002): regime licitatório anterior à Lei 14.133/2021 (`direito-administrativo.lei-8666-1993-pregao-10520`, 164 tópicos de edital)

```
Matéria: direito-administrativo.lei-8666-1993-pregao-10520 (Lei 8.666/1993 e pregão (Lei 10.520/2002): regime licitatório anterior à Lei 14.133/2021)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Contexto, princípios e modalidades": 25
- "2. Procedimento, habilitação, contratação direta e recursos": 25
- "3. Contratos administrativos na Lei 8.666": 25
- "4. Pregão (Lei 10.520/2002)": 25

Já existem (não repita):
- Com a Lei nº 14.133/2021, a Lei nº 8.666/1993 foi revogada, mas os contratos firmados com 
- São modalidades de licitação previstas na Lei nº 8.666/1993:
- Na tomada de preços, podem participar os interessados devidamente cadastrados ou que atend
- Sobre a modalidade convite, prevista na Lei nº 8.666/1993, é correto afirmar que:
- Pelos valores atualizados pelo Decreto nº 9.412/2018, uma obra de engenharia estimada em R
- Na Lei nº 8.666/1993, o prazo mínimo entre a publicação do edital e o recebimento das prop
- A modalidade de licitação destinada à escolha de trabalho técnico, científico ou artístico
- A Lei nº 8.666/1993 permitia à Administração criar novas modalidades de licitação ou combi
- O leilão, na Lei nº 8.666/1993, é a modalidade usada para a venda de bens móveis inservíve
- O princípio que obriga a Administração e os licitantes a observarem as regras fixadas no e
- Na Lei nº 8.666/1993, a comissão de licitação deve ser composta por, no mínimo, três membr
- Para a habilitação nas licitações da Lei nº 8.666/1993, exige-se dos interessados document
- Em licitação por menor preço, uma empresa de pequeno porte apresentou proposta 8% superior
- A revogação de licitação, na Lei nº 8.666/1993, pode ocorrer por razões de interesse públi
- Na Lei nº 8.666/1993, o prazo para recurso contra o julgamento das propostas é, em regra, 
- No regime da Lei nº 8.666/1993, a habilitação dos licitantes, em regra, ocorria antes do j
- Com os valores atualizados em 2018, a licitação era dispensável, por valor, para compras e
- Nos casos de emergência ou calamidade pública, a Lei nº 8.666/1993 autorizava a dispensa d
- A diferença essencial entre licitação dispensável e inexigível é que:
- Segundo a Lei nº 8.666/1993, é inexigível a licitação para a contratação de serviços de pu
- É hipótese de inexigibilidade de licitação prevista na Lei nº 8.666/1993:
- Quando nenhum interessado comparece à licitação (licitação deserta) e ela não puder ser re
- São cláusulas exorbitantes previstas na Lei nº 8.666/1993, que conferem prerrogativas à Ad
- Os contratos de prestação de serviços a serem executados de forma contínua podiam ser pror
- Em contrato de reforma de um edifício, regido pela Lei nº 8.666/1993, a Administração pode
- A ocorrência de fato imprevisível, ou previsível de consequências incalculáveis, que torne
- Quanto às garantias contratuais da Lei nº 8.666/1993, é correto afirmar que:
- Pela inexecução total ou parcial do contrato, a Lei nº 8.666/1993 previa, entre as sanções
- A declaração de inidoneidade para licitar ou contratar com a Administração, na Lei nº 8.66
- O contratado podia suspender a execução do contrato, no regime da Lei nº 8.666/1993, em ca
- No sistema de registro de preços da Lei nº 8.666/1993, a existência de preços registrados 
- O pregão, instituído pela Lei nº 10.520/2002, destina-se à aquisição de:
- No pregão, a fase de habilitação ocorre após o julgamento das propostas, examinando-se ape
- No pregão presencial, as propostas escritas foram: R$ 100.000, R$ 105.000, R$ 108.000, R$ 
- O prazo mínimo entre a publicação do aviso do pregão e a apresentação das propostas é de:
- No pregão, o licitante que deixar de manifestar, imediata e motivadamente, ao final da ses
- Na Lei nº 10.520/2002, é vedada a exigência de:
- Pela Lei do Pregão, quem, convocado dentro do prazo de validade da proposta, não celebrar 
- No pregão, após a fase de lances, o pregoeiro pode negociar diretamente com o licitante qu
- Uma questão de prova pergunta qual é o prazo de recurso no convite segundo a Lei nº 8.666/
```

## Pedido 45: Conflito de interesses e nepotismo na administração federal (`etica.conflito-interesses-nepotismo`, 164 tópicos de edital)

```
Matéria: etica.conflito-interesses-nepotismo (Conflito de interesses e nepotismo na administração federal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conflito de interesses: conceito, alcance e informação privilegiada": 25
- "2. Condutas vedadas, quarentena e deveres após o desligamento": 26
- "3. CEP, CGU, consultas preventivas e Código da Alta Administração": 21
- "4. Nepotismo no Executivo federal: vedações, parentesco e exceções": 14
- "5. Súmula Vinculante 13, controvérsias e prevenção articulada": 14

Já existem (não repita):
- Agente que participa de decisões administrativas mantém interesse privado diretamente rela
- Pessoa em função pública obtém informação ainda não divulgada sobre futura decisão governa
- Um agente do Executivo federal, sem função de alta direção, afirma que nenhuma restrição d
- Um ocupante de cargo abrangido pelo art. 2º entrou em licença e pretende prestar consultor
- Ex-dirigente deixou o cargo há oito meses e ainda conserva informação privilegiada não púb
- Agente não incluído no art. 2º, I a IV, possui dúvida concreta sobre atividade privada e p
- Duas pessoas em chefias federais combinam nomear, cada uma em seu setor, um familiar comis
- Uma nomeação para função de confiança envolve a irmã da autoridade de chefia. Qual é o gra
- Autoridade vinculada à alta administração deixou a função antes de concluída apuração étic
- Ex-dirigente, dispensado há três meses, recebeu proposta para tornar-se conselheiro de soc
- O agente deseja apresentar consulta sobre uma atividade privada ainda indefinida e envia p
- Uma consulta tramitou pela unidade competente no SeCI. A análise preliminar reconheceu ris
- Pessoa já nomeada para função comissionada torna-se familiar da chefia depois da nomeação,
- Familiar de autoridade de referência pretende realizar estágio no mesmo órgão após seleção
- Autoridade do alto escalão assumiu a função há uma semana e prepara informação sobre situa
- Uma pessoa recebe proposta de trabalho privado durante o exercício de cargo abrangido pelo
- Um dirigente afirma que pode receber presente caro de empresa interessada em decisão sua d
- Uma pessoa usou informação pública para estruturar treinamento, mas agora, sem procuração 
- O gestor conheceu empresa com relação relevante durante seu cargo federal e, dois meses ap
- No rito de competência da CGU, um pedido de autorização de atividade privada permaneceu se
- Dirigente superior e antigo colega discutem o Código de Conduta. Um afirma que o art. 15 i
- Servidor federal efetivo de qualificação adequada foi designado para função de confiança e
- Detentor de função comissionada participa de demanda para contratação direta de sociedade 
- Uma chefia estadual nomeia, em função de confiança administrativa, pessoa que é parente de
- Uma notícia sobre nomeação familiar para cargo político afirma que o Tema 1.000 do STF já 
- Um agente que mantém interesse particular em decisão submetida a sua atuação pode vivencia
- A Lei 12.813/2013 considera privilegiada apenas informação formalmente classificada como s
- O agente sujeito ao art. 5º da Lei 12.813 continua obrigado a observar suas vedações de co
- O impedimento de seis meses do art. 6º, II, faz cessar, ao seu término, a proibição de div
- Segundo a Lei 12.813, compete à Comissão de Ética Pública examinar conflitos de ocupantes 
- Dois irmãos são parentes de terceiro grau colateral e, por esse motivo, deixam de estar no
- O ajuste para troca de nomeações comissionadas de familiares entre chefias pode violar o D
- Agente público de qualquer nível do Executivo federal deve, por força do art. 10 da Lei 12
- Pelo art. 5º, IV, da Lei 12.813, atuar como intermediário de interesses privados perante e
- A Portaria Interministerial 333/2013 prevê que o silêncio na análise preliminar da unidade
- O art. 17 do Código de Conduta da Alta Administração Federal distingue advertência para au
- O art. 4º do Decreto 7.203/2010 admite que qualquer ressalva prevista nos seus incisos afa
- A Súmula Vinculante 13 abrange a administração direta e indireta dos três Poderes e de tod
- A divulgação de informação privilegiada obtida no cargo, quatro anos após o desligamento, 
- Um dirigente abrangido pelo art. 2º, I a IV, deve divulgar diariamente agenda de compromis
- O art. 15 do Código de Conduta da Alta Administração, ao citar quatro meses, impede a apli
- No Decreto 7.203/2010, a seleção de estagiário familiar pode enquadrar-se na ressalva do a
- O art. 3º, § 3º, do Decreto 7.203/2010 proíbe contratar diretamente empresa administrada p
- O entendimento sobre cargos políticos exige cautela porque, até 10/10/2026, o andamento of
- A nomeação de primo para função comissionada entra diretamente no conceito de familiar até
```

## Pedido 46: Desenvolvimento mobile e front-end com frameworks JavaScript (`informatica.desenvolvimento-mobile-frontend-js`, 164 tópicos de edital)

```
Matéria: informatica.desenvolvimento-mobile-frontend-js (Desenvolvimento mobile e front-end com frameworks JavaScript)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos mobile: Android, iOS, ciclo de vida e arquitetura": 30
- "2. React Native: componentes, estado e integração nativa": 17
- "3. Angular e Angular Material: arquitetura e componentes": 18
- "4. AngularJS, Vue.js e interfaces reativas": 17
- "5. Bootstrap, DHTML, AJAX e integração segura no front-end": 18

Já existem (não repita):
- Uma aplicação Android precisa receber eventos do sistema sobre mudanças na condição da bat
- Ao abrir uma tela interna específica, um aplicativo Android informa diretamente a classe d
- Uma tela React Native apresenta o nome de uma pessoa e precisa envolver o texto em um comp
- Um componente Angular precisa executar o método salvar quando a pessoa aciona um botão. Qu
- Um formulário Vue 3 precisa associar o conteúdo digitado a uma variável reativa de texto, 
- Em uma página Bootstrap 5, uma linha tem dois elementos com a classe col-md-6. O que acont
- Uma aplicação web recebe uma resposta HTTP 404 de fetch('/api/item') com conectividade nor
- Um sistema legado usa ng-app, ng-repeat e $scope para atualizar listas. Qual tecnologia es
- Um aplicativo precisa mostrar milhares de entradas em React Native sem montar imediatament
- Um projeto Angular recente apresenta @Component com imports locais, sem NgModule próprio. 
- Uma Activity Android entrou em segundo plano, e o sistema encerrou o processo para liberar
- Um componente React Native recebe uma prop valor e precisa alterar sua própria contagem ao
- Em um projeto Angular, a equipe precisa adicionar campo e seletor visual aderentes ao Mate
- Um componente Vue 3 alterna frequentemente a exibição de um painel cujos elementos devem p
- Uma página altera classes CSS de elementos já carregados e realiza requisição assíncrona q
- Um aplicativo UIKit para iPad exibe duas janelas da mesma aplicação e uma delas deixa o pr
- Um serviço Angular é anotado com @Injectable({ providedIn: 'root' }) e consumido por compo
- Um componente Vue 3 utiliza const busca = ref(''); dentro de <script setup>. Para alterar 
- Uma lista React Native exibe itens que podem ser reordenados após sincronização. Qual deci
- Um projeto Angular usa HttpClient e outro usa fetch nativo do navegador. Qual distinção co
- Um front-end embutiu texto livre de pessoa usuária diretamente em innerHTML para atualizar
- Em Vue 3, um valor filtrado deriva exclusivamente de uma lista e de uma variável de pesqui
- Uma equipe insere plugin JavaScript de modal Bootstrap 5 em um componente Vue que controla
- Uma aplicação Android necessita ler dados fornecidos por outro aplicativo, com acesso medi
- Um sistema AngularJS antigo utiliza $http e ng-model; a equipe pretende manter a aplicação
- Um aplicativo Android pode abrir uma Activity por Intent explícito, informando diretamente
- No React Native, o componente View representa literalmente uma div do DOM e deve receber o
- No template do Angular moderno, o uso de [disabled]="ocupado" associa dinamicamente uma pr
- A diretiva v-if do Vue 3 preserva o elemento no DOM e muda somente sua propriedade CSS dis
- O Bootstrap 5 emprega grade responsiva em doze colunas e permite usar classes como row e c
- Uma página que emprega AJAX só pode transmitir documentos XML, pois JSON tornaria a comuni
- Um ContentProvider Android pode disponibilizar dados estruturados, e outro componente os a
- Ao receber props, um componente funcional React Native pode modificá-las diretamente para 
- No Angular atual, a injeção de dependências permite fornecer serviços a classes por proved
- A partir do Angular 19, o padrão standalone impede o uso de NgModules e torna inválida qua
- Em Vue 3, uma expressão computed para filtrar dados locais pode ser apropriada quando seu 
- Uma chamada fetch que recebe status HTTP 500 rejeita necessariamente a Promise, sem criar 
- Em React Native, o uso de FlatList com keyExtractor associado a identificador estável é ad
- A declaração de uma permissão perigosa no AndroidManifest.xml dispensa a verificação e a s
- A API Pressable do React Native distingue onPressIn, onPressOut e onPress, possibilitando 
- No Angular, HttpClient fornece exatamente o mesmo tipo de retorno de fetch, uma Promise de
- A expressão reativa const total = ref(0) de Vue 3 permite ler e alterar total.value no cód
- Em Bootstrap 5, a presença de jQuery como dependência obrigatória assegura que os plugins 
- O ciclo de vida baseado em UIScene permite que interfaces de uma mesma aplicação iOS possu
- O término do suporte oficial ao AngularJS em janeiro de 2022 demonstra que sua migração pa
```

## Pedido 47: Oftalmologia (`medicina.oftalmologia`, 163 tópicos de edital)

```
Matéria: medicina.oftalmologia (Oftalmologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Anatomia ocular, semiologia e erros de refração": 20
- "Olho vermelho e doenças do segmento anterior": 20
- "Glaucoma, catarata e terapêutica cirúrgica": 20
- "Retina, vítreo, urgências e trauma ocular": 20
- "Oftalmopediatria, prevenção e doenças sistêmicas": 20

Já existem (não repita):
- Um adulto míope apresenta visão borrada para longe sem outras alterações oculares. Qual ti
- Em um exame de rotina, pergunta-se qual parte do olho participa diretamente da acomodação 
- Um paciente tem perda progressiva da visão próxima associada ao envelhecimento, com distân
- Uma paciente apresenta prurido ocular intenso bilateral, lacrimejamento, história de rinit
- Um usuário de lentes de contato procura assistência com olho vermelho, fotofobia, dor e in
- Em relação à uveíte anterior, qual achado de lâmpada de fenda oferece suporte direto à inf
- Qual combinação é a mais útil para avaliar suspeita de glaucoma crônico, sem depender de u
- Um adulto apresenta dor ocular intensa, halos, vômitos e olho vermelho de início súbito. Q
- Um paciente relata turvação visual lenta e indolor, ofuscamento e dificuldade para dirigir
- Um diabético apresenta novos vasos no disco óptico e hemorragia vítrea. Qual classificação
- Um paciente refere fotopsias, súbito aumento de moscas volantes e cortina escura periféric
- Um trabalhador sofre respingo de produto alcalino no olho. Qual medida deve começar antes 
- O exame neonatal revela reflexo branco unilateral. Qual condição grave deve ser excluída d
- Uma criança apresenta desvio do olho esquerdo para dentro. O termo técnico desse alinhamen
- Em uma criança com anisometropia significativa e baixa visão funcional de um olho, qual pr
- Durante a avaliação de um defeito campimétrico bitemporal, qual região da via visual deve 
- Um paciente com hiperemia setorial refere dor profunda importante e história de doença aut
- Sobre o glaucoma de tensão normal, qual afirmação é adequada?
- No descolamento de retina secundário à formação de membranas fibrovasculares em diabético,
- Diante de objeto perfurante ainda encravado no globo, qual cuidado pré-hospitalar é mais a
- Qual relação entre doença sistêmica e manifestação ocular é classicamente reconhecida?
- Na prescrição esferocilíndrica, qual parâmetro identifica a orientação do meridiano sem po
- Qual conduta é coerente com acompanhamento de glaucoma de ângulo aberto que continua a pro
- Um paciente hipertenso apresenta estreitamento arteriolar e cruzamentos arteriovenosos pat
- A córnea é a principal superfície refrativa do olho, enquanto o cristalino contribui para 
- Na hipermetropia não acomodada, os raios luminosos focalizam à frente da retina, sendo cor
- A melhora de visão com orifício estenopeico pode sugerir componente refrativo, mas não dis
- Prurido bilateral costuma favorecer conjuntivite alérgica, enquanto dor intensa com fotofo
- Em usuário de lente de contato com dor e infiltrado corneano, o uso empírico de corticoide
- Células e flare no humor aquoso, observados à lâmpada de fenda, são achados compatíveis co
- Episclerite e esclerite são clinicamente equivalentes, pois ambas causam invariavelmente d
- A pressão intraocular elevada, por si só, é suficiente para confirmar glaucoma, mesmo sem 
- O glaucoma primário de ângulo aberto pode evoluir inicialmente sem sintomas evidentes, jus
- Olho vermelho com dor intensa, halos e náuseas pode indicar glaucoma agudo por fechamento 
- O tratamento com colírios dissolve a opacidade estabelecida do cristalino e substitui a ci
- A retinopatia diabética proliferativa é caracterizada por neovascularização, que pode comp
- Edema macular diabético só pode ocorrer depois de instalada retinopatia diabética prolifer
- O descolamento de retina regmatogênico envolve rotura retiniana por onde o líquido alcança
- A perda visual em cortina acompanhada de fotopsias pode ser observada sem urgência desde q
- Diante de queimadura química ocular, a irrigação imediata precede a identificação completa
- Se houver suspeita de perfuração ocular, deve-se medir rapidamente a PIO e comprimir o glo
- Leucocoria é sinal de alarme pediátrico porque pode ser manifestação de retinoblastoma, me
- Ambliopia é, por definição, paralisia de um músculo ocular extrínseco, sendo corrigida ape
- A ausência de alterações no teste neonatal do reflexo vermelho dispensa todas as avaliaçõe
- Espondiloartrites podem associar-se a uveíte anterior, e síndrome de Sjögren pode associar
```

## Pedido 48: Serviço Social: estudo social, perícia e campo sociojurídico (`servico-social.pericia-estudo-social-sociojuridico`, 162 tópicos de edital)

```
Matéria: servico-social.pericia-estudo-social-sociojuridico (Serviço Social: estudo social, perícia e campo sociojurídico)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Instrumentalidade, competências e fundamentos do trabalho social": 19
- "2. Entrevista, visita, registro e intervenção individual ou coletiva": 15
- "3. Estudo social, perícia, relatório, laudo e parecer": 19
- "4. Famílias, pessoas idosas, deficiência e autismo": 22
- "5. Campo sociojurídico, mediação, rede e limites profissionais": 25

Já existem (não repita):
- Ao planejar o atendimento de uma família com dificuldade de acesso a serviços, uma profiss
- Durante uma perícia judicial, solicitou-se à profissional habilitada a elaboração de laudo
- Uma pessoa relata dificuldade para acessar um benefício, e o atendimento consiste em orien
- Durante entrevista social, a pessoa afirma que não recebeu atendimento em determinado serv
- Uma visita domiciliar é planejada para compreender barreiras de acesso e condições de conv
- Uma profissional articula atendimento em saúde, assistência e educação, acompanha os encam
- Após diversas entrevistas e análise documental, uma profissional prepara texto técnico que
- Ao receber um laudo social em processo civil, a autoridade competente deve atribuir-lhe qu
- Após a nomeação de perito em processo civil, a parte deseja apresentar quesitos, indicar a
- Um documento pericial apresenta somente juízo de valor acerca da conduta da pessoa examina
- Uma audiência de instrução está marcada para ocorrer e o perito deve entregar seu laudo co
- Em documento técnico produzido com outras especialidades, a contribuição social será apres
- Uma equipe multiprofissional reúne diferentes profissionais para avaliar situação de vulne
- Em estudo sobre convivência familiar, identifica-se escassez material, mas nenhum dado con
- Em um atendimento com pessoas de 62, 78 e 83 anos, discute-se o Estatuto da Pessoa Idosa e
- Em escola regular, família de pessoa com TEA solicita suporte individual para participação
- Ao preparar material normativo sobre deficiência, uma equipe quer diferenciar a política n
- Em prova pericial civil, a parte indica assistente técnico e suscita suspeição deste por t
- Em uma discussão sobre elaboração do laudo, alguém cita a Resolução CFESS nº 493/2006 como
- Uma pessoa com impedimento de longo prazo enfrenta barreiras no transporte, na comunicação
- Um conflito sobre atendimento social contém ponto técnico de menor complexidade. Considera
- Em conflito familiar envolvendo ameaças documentadas e forte dependência econômica, propõe
- Uma equipe produz documento conjunto e solicita que a contribuição de Serviço Social subsc
- Na elaboração de estudo social para acesso a serviço assistencial, sugere-se exposição púb
- Na avaliação social de pessoa idosa que vive sozinha, há autonomia preservada, rede inform
- Ao definir o procedimento de uma visita institucional, a profissional examina acessibilida
- No atendimento social, a realização de uma entrevista transforma toda informação relatada 
- Uma família recebe orientações sobre direitos sociais e acesso a serviços, sem necessidade
- Em visita domiciliar, a inexistência de móveis novos é prova suficiente de negligência fam
- Uma conclusão sobre contexto socioeconômico pode integrar laudo social, desde que vinculad
- Em documento conjunto, a participação em equipe interdisciplinar dispensa a identificação 
- Para a Lei nº 12.764/2012, art. 1º, § 2º, a pessoa com transtorno do espectro autista é co
- Em processo civil, depois de nomear o perito, a autoridade concede prazo de vinte dias às 
- Em um atendimento multiprofissional, cabe preservar as atribuições próprias da profissão e
- O Decreto nº 5.296/2004 regulamenta diretamente a Lei nº 7.853/1989 como ato de criação da
- Na avaliação de uma pessoa com deficiência, quando necessária nos termos do art. 2º, § 1º,
- Na produção de um laudo, é tecnicamente suficiente relatar impressões pessoais do perito, 
- Uma pessoa com 81 anos é contemplada pela prioridade especial prevista no Estatuto da Pess
- Em processo civil, o assistente técnico indicado por uma das partes fica sujeito ao mesmo 
- Um relatório de atendimento informa que não foi possível realizar determinada visita e que
- A Resolução CFESS nº 493/2006 permanece vigente em outubro de 2026, apesar da publicação d
- Diante de ameaça e desigualdade acentuada de poder entre participantes de conflito familia
- Na análise de uma família, o art. 23 do ECA autoriza concluir pela perda do poder familiar
- No campo sociojurídico, a avaliação social pode fundamentar e subsidiar uma decisão, mas a
- Em avaliação social de pessoa com TEA matriculada em classe comum, a simples existência do
```

## Pedido 49: Cirurgia plástica, torácica e de cabeça e pescoço (`medicina.cirurgia-plastica-toracica`, 162 tópicos de edital)

```
Matéria: medicina.cirurgia-plastica-toracica (Cirurgia plástica, torácica e de cabeça e pescoço)
Capítulos e quantas questões quero em cada um (total 100):
- "Cirurgia plástica: enxertos, retalhos e princípios reconstrutivos": 17
- "Queimaduras, reparação de feridas e reconstrução funcional": 17
- "Cirurgia torácica: pleura, pneumotórax, derrames e trauma torácico": 17
- "Cirurgia torácica oncológica, técnicas e avaliação de risco": 17
- "Cabeça e pescoço: anatomia, tireoide, glândulas salivares e anomalias": 16
- "Oncologia de cabeça e pescoço, reconstrução e complicações": 16

Já existem (não repita):
- Paciente com leito de granulação bem vascularizado precisa de cobertura cutânea extensa, s
- Após reconstrução microcirúrgica, o retalho apresenta aumento de volume e tonalidade arrox
- Em ferida profunda com tendão exposto e ausência de paratendão viável, qual princípio orie
- Uma cicatriz elevada ultrapassa progressivamente os limites da ferida original após cirurg
- Em cálculo inicial da superfície corporal queimada em adulto, qual alteração cutânea é usu
- Paciente com queimadura circunferencial profunda de membro passa a apresentar sinais de pe
- A equipe avalia o risco de contratura tardia após enxertia de área profunda próxima a gran
- A respeito da profundidade de queimaduras, assinale a descrição mais compatível com espess
- Adulto com pneumotórax espontâneo primário, estável, sem dor relevante ou dispneia importa
- Toracocentese de investigação encontra razão proteína pleural/sérica de 0,58, razão LDH pl
- Paciente com piora hemodinâmica súbita e forte suspeita clínica de pneumotórax hipertensiv
- Doente com empiema pleural apresenta coleção infectada e septada, além de sintomas sistêmi
- No planejamento de cirurgia curativa para carcinoma pulmonar de não pequenas células, qual
- Um candidato à lobectomia apresenta linfonodo mediastinal hipercaptante no PET-CT, e a con
- Durante ressecção pulmonar por VATS, a equipe identifica sangramento que demanda melhor ex
- Em câncer pulmonar inicial selecionado, qual procedimento é ressecção sublobar anatômica, 
- Após parotidectomia, um paciente apresenta assimetria facial ao sorrir e fechar os olhos. 
- Em relação aos tumores tireoidianos, qual afirmação diferencia o carcinoma medular das neo
- Criança apresenta massa cervical mediana que se desloca com a protrusão da língua. Confirm
- Paciente adulto apresenta massa cervical lateral cística, inicialmente descrita como prová
- Qual sítio de carcinoma epidermoide da cabeça e pescoço tem associação causal particularme
- Após grande ressecção mandibular segmentar por tumor, a equipe planeja reconstrução com os
- Após radioterapia de cabeça e pescoço, paciente desenvolve xerostomia e destruição dentári
- Um paciente tratado de neoplasia de cavidade oral apresenta dor e fraqueza na elevação do 
- A sobrevivência inicial de um enxerto cutâneo depende da interface com leito vascularizado
- Um retalho livre recebe esse nome porque é transplantado sem necessidade de restabelecer o
- Enxertos de espessura total podem ser vantajosos em defeitos selecionados nos quais menor 
- Uma cicatriz hipertrófica é definida pelo crescimento além dos limites da lesão inicial, e
- Na estimativa de superfície corporal queimada, o eritema epidérmico isolado deve ser sempr
- A lesão elétrica pode provocar dano profundo proporcionalmente maior do que sugere o aspec
- Escarotomia e fasciotomia constituem o mesmo procedimento, pois ambas são incisões destina
- O cuidado reconstrutivo do queimado inclui prevenção de contraturas por acompanhamento cic
- No pneumotórax espontâneo primário de um adulto estável e minimamente sintomático, a simpl
- Pelos critérios de Light, um derrame só é exsudativo quando a razão de proteínas, a razão 
- A presença de septações no espaço pleural em uma infecção pode indicar drenagem dificultad
- Em paciente com instabilidade e sinais clínicos fortes de pneumotórax hipertensivo, deve-s
- O estadiamento anatômico do câncer pulmonar é suficiente para declarar um paciente apto a 
- Em candidato a ressecção de carcinoma pulmonar não pequenas células, VEF1 e DLCO previstos
- A escolha de acesso por videotoracoscopia permite dispensar avaliação sistemática de linfo
- Um PET-CT com linfonodo mediastinal suspeito pode motivar investigação histológica antes d
- A lesão do nervo laríngeo recorrente durante cirurgia tireoidiana pode ocasionar alteração
- Na parotidectomia, a preservação do nervo acessório espinal é a principal medida destinada
- Nódulo tireoidiano deve ser avaliado por contexto clínico, TSH e ultrassonografia, com pun
- Uma massa lateral cística no pescoço de adulto pode ser atribuída com segurança a remanesc
- O HPV de alto risco é igualmente o principal fator causal comprovado para todos os tumores
- Na reconstrução após ressecção extensa de câncer oral, a restauração de deglutição, fala e
- A xerostomia pós-radioterapia de cabeça e pescoço diminui o risco de cárie por reduzir per
- Em carcinoma laríngeo, preservação de órgão pode ser objetivo terapêutico em pacientes sel
```

## Pedido 50: Instrumentação, automação industrial, hidráulica e pneumática (`engenharia.instrumentacao-automacao-hidraulica-pneumatica`, 162 tópicos de edital)

```
Matéria: engenharia.instrumentacao-automacao-hidraulica-pneumatica (Instrumentação, automação industrial, hidráulica e pneumática)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos de medição industrial e grandezas de processo": 17
- "Sensores, transdutores, transmissores e comunicação de campo": 17
- "Elementos finais, válvulas e estratégias de controle": 17
- "CLP, programação Ladder, SCADA e integração industrial": 17
- "Sistemas hidráulicos e pneumáticos: componentes e circuitos": 16
- "Automação predial, projetos integrados e interpretação de provas": 16

Já existem (não repita):
- Um manômetro de pressão relativa indica 200 kPa em local onde a pressão atmosférica é 100 
- Um transmissor linear mede 50–150 kPa e envia 4–20 mA. Para 75 kPa, sem saturação, qual co
- Uma tubulação está totalmente cheia e o escoamento tem velocidade média de 2 m/s numa seçã
- Uma equipe utiliza termopar para medir alta temperatura. Qual providência é compatível com
- Uma indústria precisa apenas detectar se o reservatório chegou a um limite superior, para 
- Para confirmar a presença de uma peça metálica sem contato mecânico, pode-se utilizar tipi
- Um transmissor inteligente possui saída 4–20 mA e também fornece diagnósticos ao configura
- Ao descrever uma instalação Modbus RTU sobre rede RS-485, qual afirmação distingue correta
- Em uma legenda usual de instrumentação, a identificação PT refere-se tipicamente a:
- Um sensor apresenta leituras muito próximas umas das outras, mas sistematicamente 4 °C aci
- Em uma malha PID, qual componente acumula o erro ao longo do tempo e pode ajudar a elimina
- Uma válvula pneumática recebe comando elétrico de um CLP; a posição real da haste é realim
- Para uma válvula de processo acionada eletricamente, o projeto exige posição segura após p
- Em uma válvula de controle de líquido, parte do fluido vaporiza na região de baixa pressão
- Um termostato liga a refrigeração acima de 27 °C e a desliga abaixo de 24 °C. Qual vantage
- Em lógica Ladder elementar, dois contatos condicionantes em série, com valores verdadeiros
- Um sensor produz pulso de poucos milissegundos, mas a entrada digital de determinado CLP s
- Ao implementar supervisão de uma estação de bombeamento, qual conjunto de recursos é carac
- Um material de concurso cita IL como linguagem definida em norma antiga de CLP. Consideran
- Uma estação de engenharia pretende executar uma varredura ativa de rede em sistema industr
- Um cilindro ideal possui área útil de 20 cm² sob diferença de pressão de 5 bar. Qual a for
- Num cilindro de haste simples, a mesma diferença de pressão efetiva atua alternadamente na
- A notação 5/2 em uma válvula direcional pneumática indica:
- Um projetista monta uma unidade de tratamento de ar de instrumento. Qual afirmação sobre o
- Em edifício comercial, a integradora precisa trocar dados de iluminação e climatização de 
- Calibração pode estabelecer a relação entre indicações de um instrumento e valores de refe
- Uma pressão manométrica de −20 kPa significa necessariamente que a pressão absoluta é nega
- Em escoamento com seção completamente preenchida, a vazão volumétrica pode ser obtida mult
- O radar de nível industrial baseia-se obrigatoriamente na propagação de ondas acústicas, a
- Em uma malha linear de 4–20 mA, a corrente de 4 mA corresponde normalmente ao limite infer
- O posicionador de uma válvula pneumática substitui integralmente o controlador da variável
- Em Ladder elementar, dois contatos em série representam a operação OU, de modo que basta u
- Em sistemas de medição de temperatura por RTD, a utilização de ligação a três ou quatro fi
- Todo transmissor que utiliza HART deve dispensar completamente o sinal analógico de 4–20 m
- Independentemente de atuador e condições de falha, a sigla fail-open garante que qualquer 
- A ação proporcional isolada de um controlador PID elimina necessariamente todo erro de reg
- Um SCADA pode exibir grandezas fornecidas por uma RTU, registrar tendências e disponibiliz
- Uma bomba hidráulica de deslocamento positivo produz pressão idêntica em qualquer situação
- Em um tanque fechado com gás sob pressão variável, o nível determinado por pressão hidrost
- Em uma instrumentação de dois fios compatível, a mesma malha elétrica de 4–20 mA pode forn
- O termo derivativo de um controlador PID é sempre imune ao ruído de alta frequência porque
- A decisão de selecionar uma válvula de controle fail-close para aquecimento independe do p
- A designação de uma válvula direcional como 4/3 identifica quatro vias e três posições, ma
- Por utilizar um protocolo interoperável, qualquer dispositivo BACnet pode ser integrado se
- Uma implementação anti-windup visa reduzir o acúmulo indevido da ação integral enquanto a 
```

