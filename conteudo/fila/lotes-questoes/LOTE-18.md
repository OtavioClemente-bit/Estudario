# Pedidos de questões extras, lote 18 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 3409 tópicos de edital.

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

## Pedido 1: Direito Administrativo Sancionador (`direito-administrativo.direito-administrativo-sancionador`, 74 tópicos de edital)

```
Matéria: direito-administrativo.direito-administrativo-sancionador (Direito Administrativo Sancionador)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, fundamento e princípios": 25
- "2. Infrações, sanções, responsabilidade e dosimetria": 25
- "3. Prescrição e processo sancionador": 25
- "4. Consensualidade, instâncias e improbidade": 25

Já existem (não repita):
- As garantias do direito penal estendem-se ao direito administrativo sancionador, mas com t
- No direito administrativo sancionador, o princípio da tipicidade:
- A Lei 14.230/2021 passou a prever que se aplicam ao sistema da improbidade os princípios c
- O princípio do non bis in idem, no direito administrativo sancionador, impede que:
- No processo sancionador, o ônus de provar a ocorrência da infração e sua autoria é, em reg
- A retroatividade da norma sancionadora mais benéfica aplica-se ao direito administrativo s
- O princípio da pessoalidade, ou intranscendência, das sanções administrativas significa qu
- São campos de atuação do direito administrativo sancionador, entre outros:
- Na Lei 9.784/1999, as sanções aplicadas por autoridade competente terão natureza pecuniári
- Em matéria sancionadora, a responsabilidade é, em regra:
- A Lei 12.846/2013 prevê responsabilidade objetiva das pessoas jurídicas por atos lesivos à
- Segundo a jurisprudência do STJ, a responsabilidade administrativa ambiental, que resulta 
- Segundo a LINDB, na aplicação de sanções devem ser considerados, entre outros:
- A LINDB determina que as sanções aplicadas ao agente sejam levadas em conta na dosimetria 
- Pela LINDB, o agente público responde pessoalmente por suas decisões ou opiniões técnicas 
- A interdição ou a apreensão determinadas preventivamente, durante a apuração, para protege
- Na interpretação de normas sobre gestão pública, a LINDB manda considerar os obstáculos e 
- Pela Lei 9.873/1999, a ação punitiva da Administração Pública Federal, no exercício do pod
- Pela Lei 9.873/1999, incide a prescrição intercorrente no procedimento administrativo para
- Uma infração administrativa federal ambiental também configura crime. Quanto à prescrição 
- Na infração permanente ou continuada, o prazo da prescrição punitiva federal conta-se do d
- Segundo a Súmula Vinculante 5, a falta de defesa técnica por advogado no processo administ
- Segundo o STJ, é admitida a utilização de prova emprestada no processo administrativo disc
- Sobre a revisão dos processos administrativos de que resultem sanções, na Lei 9.784/1999, 
- No recurso administrativo previsto na Lei 9.784/1999, a decisão pode ser agravada, desde q
- A decisão sancionadora pode limitar-se a declarar concordância com fundamentos de parecere
- No controle judicial de sanções administrativas, cabe ao Judiciário verificar:
- O processo sancionador deve observar, entre outras etapas, a instauração formal, a acusaçã
- Entre os instrumentos consensuais disponíveis no campo sancionador, inclui-se:
- A LINDB autoriza a autoridade administrativa a celebrar compromisso com os interessados pa
- A absolvição criminal por insuficiência de provas impede a aplicação de sanção administrat
- A absolvição penal repercute na esfera administrativa quando fundada em:
- A falta residual, não abrangida pela absolvição criminal, pode ser objeto de punição admin
- Após a Lei 14.230/2021, a configuração do ato de improbidade administrativa exige:
- No Tema 1.199 da repercussão geral, o STF decidiu que o novo regime prescricional da Lei 1
- Sobre a relação entre as instâncias penal, civil e administrativa, é correto afirmar:
- Os acordos no campo sancionador dispensam transparência e controle, pois envolvem negociaç
- Duas pessoas concorreram para a mesma infração administrativa, uma como autora direta e ou
- A responsabilidade objetiva, no direito administrativo sancionador, é admitida apenas quan
- Um processo administrativo sancionador federal ficou sem nenhum despacho ou julgamento por
```

## Pedido 2: Eletroquímica (`ciencias.eletroquimica`, 74 tópicos de edital)

```
Matéria: ciencias.eletroquimica (Eletroquímica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Reações de oxirredução e número de oxidação": 28
- "2. Pilhas, potenciais e células de corrosão": 36
- "3. Eletrólise e leis de Faraday": 21
- "4. Reescrita de processos e como o tema é cobrado": 15

Já existem (não repita):
- Sobre reações de oxirredução, assinale a afirmação correta.
- Na reação Zn(s) + Cu²⁺(aq) → Zn²⁺(aq) + Cu(s), qual espécie atua como agente oxidante?
- Qual é o número de oxidação do cloro no íon clorato, ClO₃⁻?
- Na transformação Fe₂O₃ + 3CO → 2Fe + 3CO₂, qual átomo sofre redução?
- Para que uma célula eletroquímica de corrosão se mantenha, qual condição é necessária?
- Em uma célula eletrolítica, assinale a descrição correta dos eletrodos.
- Uma pilha padrão usa Zn²⁺/Zn (E°red = −0,76 V) e Cu²⁺/Cu (E°red = +0,34 V). Qual é o poten
- Qual massa de zinco é depositada quando se aplica 1,0 A durante 1 930 s a uma solução com 
- Em uma pilha galvânica em funcionamento, qual descrição do fluxo eletrônico está correta?
- Uma tubulação de aço enterrada é ligada eletricamente a um bloco de magnésio e ambos ficam
- Dois potenciais padrão de redução são fornecidos: Ag⁺/Ag = +0,80 V e Ni²⁺/Ni = −0,25 V. Pa
- Um estudante duplica a equação de oxidação de uma semirreação e calcula o potencial padrão
- Ao balancear MnO₄⁻ + Fe²⁺ → Mn²⁺ + Fe³⁺ em meio ácido, qual equação conserva átomos e carg
- Uma célula de corrosão se forma sobre uma chapa de ferro exposta a água aerada neutra. Qua
- Na eletrólise de NaCl fundido, quais produtos são formados nos eletrodos inertes?
- Uma corrente de 3,0 A atravessa uma solução de Cu²⁺ por 1 930 s. Considerando Cu²⁺ + 2e⁻ →
- Uma peça de ferro é recoberta por zinco. Qual afirmação descreve corretamente as duas form
- Na eletrólise de água acidificada, passam 1,93 A durante 1 000 s. Admitindo 2H⁺ + 2e⁻ → H₂
- O íon tiossulfato é S₂O₃²⁻. Considerando o NOX médio dos dois átomos de enxofre, qual valo
- Uma solução de Ag⁺ é eletrolisada por 2,0 A durante 48,25 min. Para Ag⁺ + e⁻ → Ag, use F=9
- Dados E°red(Fe³⁺/Fe²⁺)=+0,77 V e E°red(I₂/ I⁻)=+0,54 V, considere 2Fe³⁺ + 2I⁻ → 2Fe²⁺ + I₂
- Uma chapa de aço em contato elétrico com uma tira de cobre fica parcialmente imersa em águ
- Em uma solução aquosa concentrada de NaCl, com eletrodos inertes, qual conjunto de produto
- Em um banho eletrolítico fundido, Al³⁺ é reduzido a alumínio metálico. Com corrente de 9,6
- Uma chapa de ferro é revestida por estanho e sofre um risco que expõe o metal-base em pres
- Na reação Cl₂ + 2OH⁻ → Cl⁻ + ClO⁻ + H₂O, qual descrição da transformação do cloro está cor
- Duas células eletrolíticas recebem a mesma carga. Em uma deposita-se Cu (M=63,5 g·mol⁻¹, n
- O alumínio tem potencial padrão de redução bastante negativo, mas uma peça exposta ao ar p
- Uma célula consome MgCl₂ fundido e deve produzir 1,20 g de Mg. Use M(Mg)=24,0 g·mol⁻¹ e F=
- Para a corrosão padrão do ferro, use Fe²⁺ + 2e⁻ → Fe (E°red=−0,44 V) e O₂ + 2H₂O + 4e⁻ → 4
- Em uma substância simples como O₂(g), o número de oxidação de cada átomo de oxigênio é zer
- Em uma reação redox, o agente oxidante perde elétrons e sofre aumento de seu próprio númer
- Em uma pilha galvânica funcionando espontaneamente, o ânodo é negativo e é o local em que 
- Toda célula de corrosão exige dois metais diferentes ligados entre si para que surjam as r
- Uma corrente constante de 2,0 A durante 60 s transporta 1 200 C de carga elétrica.
- Em uma célula eletrolítica, a redução ocorre no cátodo negativo, ligado ao terminal negati
- Ao multiplicar por três os coeficientes de uma semirreação, deve-se multiplicar por três o
- Uma ponte salina permite migração de íons para conservar a eletroneutralidade das soluções
- Em uma célula eletrolítica, o ânodo é negativo porque libera elétrons pela reação de oxida
- Se a corrente e o tempo de eletrólise dobram simultaneamente, a carga total transferida qu
- Com eficiência faradaica igual a 100%, a massa depositada numa eletrólise é diretamente pr
- Na eletrólise aquosa, a espécie que sofre redução no cátodo é sempre o cátion do sal disso
- Sob uma gota salina com aeração diferencial, a região menos oxigenada pode ser anódica e c
- A película de óxido que passiva o alumínio prova que a oxidação do metal não é termodinami
- Na reação de cloro com hidróxido que forma cloreto e hipoclorito, o cloro molecular é redu
```

## Pedido 3: Concessões rodoviárias, engenharia de tráfego e planejamento de transportes (`transportes.concessoes-trafego-planejamento`, 74 tópicos de edital)

```
Matéria: transportes.concessoes-trafego-planejamento (Concessões rodoviárias, engenharia de tráfego e planejamento de transportes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regulação de concessões, PER e fiscalização": 22
- "2. Tarifas, equilíbrio e gestão da faixa de domínio": 18
- "3. Fundamentos de tráfego, capacidade e nível de serviço": 16
- "4. Sinalização, segurança viária e conservação operacional": 16
- "5. Sistemas de transportes, planejamento e avaliação de projetos": 28

Já existem (não repita):
- Durante inspeção, a equipe encontra no PER prazo e padrão de drenagem, mas a execução aind
- Uma auditoria avalia se o serviço de uma rodovia concedida oferece regularidade, continuid
- Uma tarifa contratual de R$ 20,00 recebe DBT de 5% por passagem elegível com TAG. Qual é o
- Em pesquisa de tráfego, uma seção apresenta densidade de 20 veículos/km/faixa e velocidade
- Em uma interseção são contados movimentos separados de conversão à esquerda, direita e pas
- Uma equipe instala cones e placas para uma frente de manutenção em faixa de rolamento. Que
- Após inspeção antes do período chuvoso, uma equipe limpa sarjetas e bueiros ainda funciona
- Um planejamento distingue a rede segundo os modos expressamente enumerados no art. 2º da L
- Um corredor utiliza caminhão no primeiro trecho e ferrovia no trecho principal. Qual eleme
- A concessionária registra índice contratual de inflação e solicita sua aplicação periódica
- Uma empresa pede acesso particular à rodovia concedida e apresenta desenho de entrada que 
- Um loteamento enfrenta reserva não edificável adjacente à faixa de domínio rodoviária. Qua
- Ao avaliar rodovia congestionada, a contagem de descarga registra somente 700 veículos/hor
- Uma manutenção noturna exige fechar parcialmente a pista e instalar desvio temporário. Qua
- Uma avaliação de investimento calcula custo monetário por sinistro evitado, sem converter 
- Uma pesquisa identifica pares origem-destino, mas o estudo precisa determinar quantas viag
- Um empreendimento entrega relatório de duplicação concluída no prazo, mas ensaios mostram 
- Em contrato que prevê Fator D, uma ampliação de capacidade não é entregue no marco pactuad
- No diagrama fundamental de uma rodovia saturada, a densidade sobe, mas o fluxo de descarga
- Uma faixa registra 1.200 veículos/h com velocidade espacial de 60 km/h. Em condição estaci
- Um trecho com muitos sinistros recebe placas novas, mas continua com deficiência de visibi
- Um usuário é autuado por desobedecer indicação cuja placa estava ilegível, conforme períci
- Uma rota ferroviária parece mais barata por tonelada, mas exige caminhão de acesso, dois t
- Uma proposta afirma que o lançamento do PNL 2050 autoriza imediatamente a construção de te
- Após conclusão de um trecho de recuperação, o responsável registra fotografias e medições,
- A variação anual de índice inflacionário contratualmente adotado caracteriza, por si só, r
- Uma contagem classificatória que separa automóveis, ônibus e caminhões é útil para compree
- Em uma obra noturna, a colocação de placas de advertência somente após o início do estreit
- O art. 2º da Lei nº 12.379/2011 admite classificação do Sistema Nacional de Viação por jur
- A conservação corretiva consiste principalmente em antecipar falhas, como realizar desobst
- Uma faixa de tráfego com densidade de 30 veículos/km/faixa e velocidade espacial de 40 km/
- Em uma rodovia com fila persistente a montante do gargalo, o fluxo medido a jusante, por e
- A reserva legal não edificável ao longo da faixa de domínio público rodoviária pode, nos t
- O desconto por passagem frequente e o desconto básico pelo uso de TAG têm a mesma regra fi
- Quando uma frente de obra termina e a pista volta à condição normal, a sinalização temporá
- Uma pesquisa domiciliar que pergunta onde cada viagem começa e termina equivale, sem proce
- Em contrato que prevê revisão quinquenal do programa de investimentos, a necessidade de an
- Uma análise de custo-eficácia deve monetizar integralmente todos os impactos antes de comp
- A fiscalização que encontra dois quilômetros de obra implantada e ensaios insatisfatórios 
- Se a velocidade média espacial cair pela metade e a densidade dobrar no mesmo segmento e p
- A existência de contrato de concessão não elimina o dever de compatibilizar obras com lice
- Uma carga movimentada por caminhão, trem e embarcação sob três contratos independentes se 
- Ao avaliar corredor rodoviário, o projeto de ampliação deve considerar efeitos sobre a red
- O PNL 2050, lançado em 2026, altera por si as condições dos contratos de concessão vigente
```

## Pedido 4: Atualidades: segurança pública, justiça criminal e sistema prisional (`atualidades.seguranca-justica-sistema-prisional`, 74 tópicos de edital)

```
Matéria: atualidades.seguranca-justica-sistema-prisional (Atualidades: segurança pública, justiça criminal e sistema prisional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Violência contemporânea, segurança pública e leitura dos indicadores": 15
- "2. Crime organizado, conflitos sociais, cidades e o caso da Bahia": 19
- "3. Políticas de segurança pública, cidadania, educação e saúde": 22
- "4. Sistema de justiça criminal, garantias e fluxos de responsabilização": 23
- "5. Sistema prisional, execução penal e respostas estruturais recentes": 21

Já existem (não repita):
- Um relatório informa 120 homicídios no município Alfa, com 200 mil habitantes, e 120 no mu
- Moradores de um bairro defendem a instalação de equipamentos de proteção, mas afirmam que 
- Uma notícia publicada em maio de 2026 comenta o Atlas da Violência 2026 e seus dados nacio
- Um grupo propõe integrar dados e ações de entes federativos para enfrentar crimes e preven
- Uma equipe investiga violência patrimonial, mas identifica que parte dos casos é tratada n
- Uma cidade amplia iluminação, rotas seguras e vínculos escolares em áreas com vulnerabilid
- Durante audiência sobre detenção em flagrante, alguém afirma que a captura por si só produ
- Uma pessoa condenada inicia o cumprimento da pena, e a administração precisa definir final
- Ao atualizar um painel de violência contra pessoas vulneráveis, uma equipe consulta o Mapa
- Depois de uma operação em determinada área, registros de roubo caíram 15%. Em bairros próx
- Uma reportagem compara 2024 e 2025 na Bahia e menciona 3.884 ocorrências de homicídio, lat
- Cinco pessoas dividem funções para explorar um mercado ilícito, mas a tipificação como org
- Ao revisar serviços oferecidos em uma unidade prisional, foi constatada ausência de atendi
- Uma proposta afirma reproduzir a estrutura do plano Pena Justa, mas combina soluções sobre
- Um analista deseja comparar a quantidade de pessoas custodiadas em estabelecimentos com o 
- A expansão do registro eletrônico de atendimentos de violência sexual levou a um aumento d
- Uma área apresenta queda de homicídios, mas moradores relatam expansão de extorsões e rest
- Um painel divulga 500 eventos de violência letal em uma região e 400 na seguinte. No segun
- Um programa de enfrentamento da violência reporta 1.000 atendimentos realizados, mas ainda
- No julgamento da ADPF 347, em outubro de 2023, reconheceram-se violações estruturais nas p
- Um levantamento registra número maior de notificações de violência sexual após abertura de
- Uma cidade tem unidades escolares fechadas por conflitos armados, episódios de coerção em 
- No Relipen, um leitor soma pessoas em estabelecimentos, em prisão domiciliar e em outras c
- O relatório de um projeto apresenta queda de 12% para 9% na proporção de vítimas que relat
- Um preso provisório com acesso insuficiente à assistência de saúde é excluído de um progra
- Ao organizar um conselho comunitário de proteção, uma prefeitura reconhece que a participa
- Duas cidades registraram 200 homicídios cada em um ano. Como o total de casos foi idêntico
- Uma nota técnica classifica os 42.590 homicídios informados pelo Atlas da Violência 2026 c
- Em um programa de cooperação federativa, a Lei nº 13.675/2018 pode fundamentar atuação coo
- Um diagnóstico sobre prisões que propõe melhorar a entrada de pessoas, os serviços interno
- Três indivíduos que praticam delitos patrimoniais em conjunto preenchem, somente pelo núme
- Uma notícia estadual informa que o conjunto de homicídios, latrocínios e lesões dolosas se
- Em um estudo territorial, a urbanização rápida basta para demonstrar causalmente o aumento
- O 19º ciclo do Relipen, publicado em maio de 2026, tem por período de referência o segundo
- Diante de uma prisão em flagrante, a realização de audiência de custódia em até 24 horas, 
- Em região que melhorou a notificação de violência em serviços de saúde, todos os novos reg
- Um programa prisional prevê assistência material, à saúde, jurídica, educacional, social e
- Ao comparar redução de homicídios com denúncias de extorsão praticada por redes criminosas
- Um analista apresenta os 42.590 homicídios do Atlas 2026 como soma de todos os óbitos de q
- Se uma unidade possui 500 vagas e 650 custodiados fisicamente presentes, a ocupação calcul
- Uma estratégia de enfrentamento de redes criminosas será necessariamente eficaz se elevar 
- Para aplicar o conceito de organização criminosa da Lei nº 12.850/2013 a grupo de quatro p
- Um indicador de vitimização caiu de 20% para 15%. A redução é de cinco pontos percentuais 
- Uma decisão de custódia que explicita os motivos para afastar medidas cautelares alternati
- O reconhecimento do estado de coisas inconstitucional na ADPF 347 implicou que os quatro e
```

## Pedido 5: Imuno-hematologia e banco de sangue (`laboratorio.imuno-hematologia-banco-sangue`, 73 tópicos de edital)

```
Matéria: laboratorio.imuno-hematologia-banco-sangue (Imuno-hematologia e banco de sangue)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos e sistemas de grupos sanguíneos": 17
- "Tipagem ABO/Rh, Coombs e investigação de anticorpos": 17
- "Provas pré-transfusionais e compatibilidade": 17
- "Doação, triagem laboratorial e rastreabilidade": 17
- "Hemocomponentes, hemoderivados, conservação e agência transfusional": 16
- "Reações transfusionais, investigação e hemovigilância": 16

Já existem (não repita):
- Na tipagem ABO direta de uma amostra, há aglutinação com anti-A e nenhuma com anti-B. Qual
- Em uma reação ABO reversa normal de indivíduo do grupo O adulto, qual resultado se espera?
- Qual técnica pesquisa anticorpos ou complemento já aderidos às hemácias circulantes de um 
- Na prova de compatibilidade eritrocitária maior, quais materiais entram em contato?
- Qual par contém, respectivamente, um hemocomponente e um hemoderivado?
- Qual é a condição rotineira de conservação do concentrado de hemácias não congelado previs
- Qual é o objetivo principal do teste NAT na qualificação infecciosa da doação?
- Um paciente desenvolve febre e hipotensão durante infusão de hemácias. Qual providência ve
- Uma tipagem direta indica grupo A, mas a reversa mostra reação intensa com hemácias A1 e B
- Paciente apresenta história documentada de anti-Jkᵃ, mas PAI atual negativa. Qual interpre
- A fase antiglobulina de uma PAI pelo método em tubo é negativa, mas as hemácias de control
- O laboratório identifica anti-K clinicamente significativo em receptor com necessidade de 
- Um candidato afirma que desleucocitação, lavagem e irradiação são sinônimos. Qual correção
- No fluxo de doação, uma amostra tem sorologia não reagente para HIV, mas NAT reagente no a
- Sobre a validade usual de plaquetas, qual afirmação traduz corretamente a regra brasileira
- Após transfusão, paciente apresenta dispneia aguda com sinais de congestão e balanço hídri
- Um resultado positivo com hemácias A1 na reversa de indivíduo tipado A na direta, sem reaç
- Um receptor foi transfundido há um mês e necessita nova transfusão. Qual regra descreve me
- Um paciente previamente aloimunizado chega com PAI positiva ainda sem identificação especí
- Paciente transfundido recentemente necessita fenotipagem para Duffy, Kidd e Kell. Qual lim
- Qual associação de prazo e produto se ajusta ao regulamento brasileiro de 2026, sem consid
- Qual conjunto melhor representa a triagem obrigatória de marcadores infecciosos de cada do
- Na diferenciação entre TACO e TRALI, qual estratégia é tecnicamente mais adequada?
- Um paciente tem anti-Fyᵃ documentado há anos. Qual proposição melhor protege esse receptor
- No sistema ABO, o indivíduo do grupo AB apresenta antígenos A e B em suas hemácias e, em c
- A tipagem sanguínea reversa é realizada ao se colocar hemácias do paciente em contato com 
- O antígeno D faz parte do sistema Rh e sua presença usualmente caracteriza a classificação
- A PAI é destinada principalmente à detecção de HIV, sífilis e hepatites virais no sangue d
- A lavagem de concentrados de hemácias e a desleucocitação são processos intercambiáveis, p
- O crioprecipitado contém componentes como fibrinogênio, fator XIII e fator de von Willebra
- A identificação inequívoca do paciente e a correspondência entre solicitação e amostra são
- Ao perceber sinais compatíveis com reação transfusional, deve-se concluir primeiro a bolsa
- Um TAD positivo, sem evidências clínicas e laboratoriais adicionais, é suficiente para con
- A presença documentada de aloanticorpo antieritrocitário clinicamente significativo contin
- Para um receptor do grupo A, transfundir plasma do grupo O é automaticamente seguro, pois 
- A fenotipagem sorológica realizada imediatamente depois de transfusão de hemácias pode ref
- Na Portaria GM/MS nº 11.685/2026, o prazo de até sete dias para certas plaquetas pode ser 
- Na triagem de doadores, NAT detecta material genético de agentes infecciosos e pode reduzi
- Uma amostra sem vínculo inequívoco com o receptor pode ser identificada após a coleta apen
- Uma reação febril suspeita após transfusão deve ser automaticamente classificada como não 
- Variantes de D parcial podem não expressar certos epítopos de D e, em determinados context
- O efeito de dose na identificação de anticorpos irregulares indica que algumas reações são
- Na presença de anti-K significativo, a simples ausência de reação com anti-D na retipagem 
- Segundo a atualização normativa de 2026, um paciente com transfusão nos três meses anterio
- A Portaria GM/MS nº 11.685/2026 inclui malária entre as infecções pesquisadas na qualifica
- O plasma comum definido pela portaria atual pode ser liberado rotineiramente para transfus
- Reações transfusionais simultâneas, quando fundamentadas por evidências de mais de um tipo
- Uma PAI positiva sem anticorpo identificado pode ser classificada como prova inconclusiva,
```

## Pedido 6: Estatuto do Servidor do Ceará (Lei nº 9.826/1974) (`legislacao.servidores-ceara-lei-9826`, 73 tópicos de edital)

```
Matéria: legislacao.servidores-ceara-lei-9826 (Estatuto do Servidor do Ceará (Lei nº 9.826/1974))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regime jurídico, provimento, posse, exercício e estágio": 20
- "2. Movimentação, reingresso, vacância e estabilidade": 20
- "3. Férias, licenças, autorizações e retribuição": 20
- "4. Direito de petição, deveres, proibições e sanções": 20
- "5. Suspensão preventiva, sindicância, inquérito e revisão": 20

Já existem (não repita):
- Segundo a Lei nº 9.826/1974, funcionário público civil é:
- Pela Lei nº 9.826/1974, não haverá posse nos casos de promoção, acesso e reintegração.
- De acordo com a Lei nº 9.826/1974, a posse ocorrerá no prazo de 30 dias da publicação do a
- Pela Lei nº 9.826/1974, poderá haver posse por procuração quando se tratar de funcionário 
- Na redação dada pela Lei nº 13.092/2001, o estágio probatório do Estatuto do Ceará é:
- Pela Lei nº 9.826/1974, o exercício funcional terá início no prazo de 30 dias, contados da
- Segundo a Lei nº 9.826/1974, a substituição de titular de cargo em comissão:
- Pela Lei nº 9.826/1974, reintegrado o funcionário, quem lhe houver ocupado o lugar será re
- Segundo a Lei nº 9.826/1974, havendo mais de um funcionário em disponibilidade concorrendo
- Pela Lei nº 9.826/1974, entre as condições essenciais para a reversão do aposentado por in
- Segundo a Lei nº 9.826/1974, a vacância do cargo resultará de:
- Pela Lei nº 9.826/1974, a estabilidade é incompatível com o cargo em comissão.
- Após a alteração de 2024, a Lei nº 9.826/1974 considera de efetivo exercício o afastamento
- Pela Lei nº 9.826/1974, o funcionário não poderá gozar, por ano, mais de dois períodos de 
- Segundo a Lei nº 9.826/1974, o funcionário não poderá permanecer em licença por prazo supe
- Pela Lei nº 9.826/1974, no curso da licença para tratamento de saúde, o funcionário que ex
- Segundo a Lei nº 9.826/1974, a licença para acompanhar o cônjuge, também servidor, mandado
- Pela redação atual da Lei nº 9.826/1974, o servidor poderá obter autorização de afastament
- No Estatuto do Ceará, cassada a autorização para trato de interesses particulares por inte
- Segundo a Lei nº 9.826/1974, a ajuda de custo concedida ao funcionário designado de ofício
- Pela Lei nº 9.826/1974, o funcionário que receber diária indevida será obrigado a restituí
- Segundo a Lei nº 9.826/1974, o direito de pedir reconsideração, exercido perante a autorid
- Pela Lei nº 9.826/1974, o pedido de reconsideração e o recurso do direito de petição não t
- Segundo a Lei nº 9.826/1974, o direito de pleitear na esfera administrativa prescreverá, s
- Pela Lei nº 9.826/1974, o ilícito administrativo é punível independentemente de acarretar 
- Com a redação dada pela Lei nº 17.507/2021, o direito ao exercício do poder disciplinar no
- Segundo a Lei nº 9.826/1974, verificada em inquérito a acumulação proibida e provada a boa
- São sanções disciplinares previstas no art. 196 da Lei nº 9.826/1974:
- Pela Lei nº 9.826/1974, a suspensão disciplinar não pode ultrapassar 90 dias e, por conven
- Segundo a Lei nº 9.826/1974, considera-se abandono de cargo a deliberada ausência ao servi
- Com a alteração da Lei nº 18.171/2022, o Estatuto do Ceará prevê demissão por crime comum 
- Segundo a Lei nº 9.826/1974, os chefes de unidades administrativas em geral são competente
- Pela Lei nº 9.826/1974, o funcionário demitido com a nota “a bem do serviço público” não p
- Segundo a Lei nº 9.826/1974, a suspensão preventiva do funcionário indiciado em inquérito 
- Pela Lei nº 9.826/1974, a sindicância será realizada por funcionário estável, no prazo máx
- Segundo a Lei nº 9.826/1974, as Comissões Permanentes de Inquérito Administrativo compõem-
- Pela Lei nº 9.826/1974, o inquérito administrativo será concluído no prazo máximo de 90 di
- Segundo a Lei nº 9.826/1974, da decisão da autoridade julgadora no inquérito administrativ
- Pela Lei nº 9.826/1974, constitui fundamento suficiente para a revisão do procedimento dis
- Segundo a Lei nº 9.826/1974, o funcionário que responde a inquérito administrativo:
- Pela Lei nº 9.826/1974, salvo disposição expressa em contrário, os prazos previstos no Est
```

## Pedido 7: Estequiometria (`ciencias.estequiometria`, 73 tópicos de edital)

```
Matéria: ciencias.estequiometria (Estequiometria)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Mol, massas atômicas e moleculares e volume molar": 23
- "2. Fórmulas químicas, composição percentual e leis ponderais": 16
- "3. Equações químicas, conservação e padrões de reatividade": 20
- "4. Relações quantitativas: mol, massa, partículas e volume gasoso": 21
- "5. Reagente limitante, excesso e resolução de casos complexos": 20

Já existem (não repita):
- Uma amostra contém 0,50 mol de moléculas de água. Aproximando a constante de Avogadro por 
- Para um composto CaCO3, considere massas atômicas aproximadas Ca = 40, C = 12 e O = 16. Qu
- Considere a formação de água a partir de H2 e O2. Qual equação apresenta os menores coefic
- Num sistema fechado, 12 g de carbono reagem completamente com 32 g de oxigênio, formando a
- Um recipiente contém 2 mol de um gás ideal a 0 °C e 1 atm. Adote volume molar aproximado d
- Uma amostra pura de água contém 2 g de hidrogênio e 16 g de oxigênio. Qual razão em massa 
- Dois compostos são CO e CO2. Mantendo 12 g de carbono, combinam-se, respectivamente, 16 g 
- Ao interpretar a unidade de fórmula Ca(NO3)2, quantos átomos de oxigênio devem ser contabi
- Uma porção contém 36 g de água pura, cuja massa molar adotada é 18 g/mol. Quantos mol de m
- Na reação 2 H2 + O2 → 2 H2O, misturam-se 4 mol de H2 e 1 mol de O2, com transformação comp
- Um elemento hipotético tem dois isótopos de massas 10 u e 11 u, com abundâncias respectiva
- Uma substância contém somente carbono e hidrogênio. Uma amostra tem 24 g de C e 4 g de H. 
- Em uma amostra pura de CaCO3, use Ca = 40, C = 12 e O = 16. Qual é a porcentagem aproximad
- Para a reação Fe + O2 → Fe2O3, quais são os menores coeficientes inteiros positivos na ord
- Considere a transformação completa CaCO3 → CaO + CO2. Com massas Ca = 40, C = 12, O = 16, 
- Na síntese N2(g) + 3 H2(g) → 2 NH3(g), 30 L de H2 reagem com N2 suficiente. Todos os volum
- Uma lâmina de zinco é colocada em solução aquosa de sulfato de cobre(II), em condições usu
- Em CH4, 12 g de C combinam com 4 g de H; em H2O, 4 g de H correspondem a 32 g de O. Qual r
- Uma amostra contém 0,50 mol de unidades de fórmula Ca3(PO4)2. Quantos átomos de fósforo el
- Uma substância apresenta fórmula mínima CH2O e massa molar de 60 g/mol. Com C = 12, H = 1 
- Em Fe2O3 + 3 CO → 2 Fe + 3 CO2, fornecem-se 3 mol de Fe2O3 e 6 mol de CO. Suponha reação c
- Na reação Zn + 2 HCl → ZnCl2 + H2, há 65 g de Zn e 36,5 g de HCl; considere Zn = 65 g/mol 
- Uma amostra gasosa ideal de 2 mol é medida a 0 °C e pressão de 1 bar. Adote volume molar n
- Na reação 2 Mg + O2 → 2 MgO, misturam-se 12 g de Mg e 16 g de O2, com Mg = 24 e O = 16. Ap
- Para a reação Al2(SO4)3 + NaOH → Al(OH)3 + Na2SO4, quais são os menores coeficientes intei
- Uma amostra com 1 mol de moléculas de CO2 contém 2 mol de átomos de oxigênio, porque cada 
- Uma amostra com 0,50 mol de água tem necessariamente massa de 0,50 g, pois a grandeza mol 
- Num sistema fechado em que 5 g de uma substância se combinam integralmente com 8 g de outr
- Para balancear uma equação de formação de água, pode-se substituir H2O por H2O2, pois os í
- Dois gases ideais diferentes ocupam volumes iguais quando comparados na mesma temperatura 
- Duas substâncias com a mesma composição centesimal de carbono, hidrogênio e oxigênio devem
- Um elemento hipotético tem abundância de 75% de isótopo de massa 10 u e 25% de isótopo de 
- Pela equação 2 H2 + O2 → 2 H2O, a reação completa de 2 mol de H2 com 1 mol de O2 gera 3 mo
- Na decomposição balanceada CaCO3 → CaO + CO2, 50 g de carbonato puro, de massa molar 100 g
- Numa equação iônica completa, o balanceamento deve conservar somente os átomos; é aceitáve
- Na reação 2 CO(g) + O2(g) → 2 CO2(g), 10 L de O2 exigem 20 L de CO e produzem 20 L de CO2,
- A lei de Proust estabelece que toda amostra de água pura possui proporção em massa de hidr
- Comparando CO e CO2, ambos formados por carbono e oxigênio, 12 g de carbono combinam-se co
- A lei das proporções recíprocas de Richter-Wenzel exige comparar apenas dois compostos con
- Na reação Zn + 2 HCl → ZnCl2 + H2, a mistura inicial de 1 mol de Zn e 1 mol de HCl deixa 0
- Para um mol de gás ideal a 0 °C, é indiferente usar a pressão de 1 bar ou a de 1 atm, pois
- Em N2 + 3 H2 → 2 NH3, misturar 2 mol de N2 com 3 mol de H2, fazendo a reação avançar até o
- Na reação Fe2O3 + 3 CO → 2 Fe + 3 CO2, uma mistura de 1 mol de Fe2O3 e 2 mol de CO forma 2
- Duas substâncias moleculares podem apresentar a mesma fórmula mínima CH2O e ter fórmulas m
- Na síntese 2 H2 + O2 → 2 H2O, misturar 2 mol de H2 com 2 mol de O2 torna o oxigênio o reag
```

## Pedido 8: Mercado de câmbio e monetário (`conhecimentos-bancarios.mercado-cambio-monetario`, 73 tópicos de edital)

```
Matéria: conhecimentos-bancarios.mercado-cambio-monetario (Mercado de câmbio e monetário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema financeiro e arquitetura dos mercados": 19
- "2. Mercado de câmbio, instituições, taxas e remessas": 26
- "3. Contratos cambiais, comércio exterior e mercado interbancário": 21
- "4. Mercado monetário, taxas de juros, Selic, DI e tesouraria": 18
- "5. Política monetária convencional, não convencional e integração cambial": 16

Já existem (não repita):
- Uma empresa vende debêntures para captar recursos de investidores, enquanto sua tesouraria
- Uma viajante quer comprar US$ 300 em espécie em instituição autorizada, que anuncia duas t
- Uma cotação hipotética passa de R$ 5,00 para R$ 5,50 por dólar, sem alteração da unidade d
- Uma empresa exportadora necessita de reais para fabricar bens antes do embarque e pretende
- Durante reunião sobre juros, o colegiado monetário estabelece nova taxa básica de referênc
- Um banco registra obrigação de pagamento para esta tarde, embora mantenha carteira de ativ
- Um aplicativo apresenta taxa PTAX do dia e afirma que nenhuma instituição autorizada poder
- Uma empresa exportou bens e já entregou ao banco os documentos relativos ao embarque, busc
- Uma equipe compara indicadores monetários e deseja usar taxa calculada com depósitos inter
- Um texto afirma que o Banco Central pode receber depósitos remunerados a prazo, contratado
- Uma sociedade corretora de câmbio autorizada pretende fechar com cliente operação pronta d
- Instituição de pagamento autorizada, enquadrada no art. 29, III, oferece transferência cam
- Uma agência de turismo que não possuía autorização cambial anterior procura vender moeda e
- Um exportador fixa hoje taxa de câmbio para recebimento futuro, mas a moeda estrangeira se
- Uma equipe analisa dados da DU-E enviados por exportador à instituição responsável pela co
- Uma mesa bancária compra dólares de clientes exportadores e vende quantia inferior a impor
- Um título proporciona juro nominal de 10% no ano e os preços aumentam 4% no mesmo período.
- Uma curva de juros prefixados de instrumentos comparáveis apresenta taxa curta maior que a
- Para retirar reservas excedentes temporariamente, o Banco Central negocia títulos públicos
- Um banco central, com taxa básica perto de zero, adquire grandes volumes de ativos de venc
- Instituição afirma que a captação de depósitos voluntários remunerados no BCB e uma venda 
- Uma instituição de pagamento autorizada enquadrada no art. 29, III, planeja fechar câmbio 
- Banco registra operação de câmbio com outra instituição, sem câmara, e a entidade comprado
- Uma curva de juros de títulos comparáveis se inverte num cenário em que investidores passa
- O BCB mantém taxa básica positiva e decide anunciar trajetória provável dos juros condicio
- Uma companhia vende ações novas aos investidores para financiar expansão, enquanto outra t
- Ao observar aumento hipotético de R$ 5,00 para R$ 5,50 por dólar, uma analista conclui que
- Uma instituição autorizada oferece ao cliente cotação diversa da PTAX no fechamento de rem
- Em financiamento da produção exportável antes do embarque, a antecipação de reais apoiada 
- O Comitê de Política Monetária define a meta para a Selic; a Selic efetiva resulta das ope
- Uma instituição financeira, sem informar sua categoria de autorização cambial, pretende fa
- Uma importadora contrata hoje câmbio com liquidação para data futura. A instituição e a im
- Uma mesa bancária vende dólares a outra instituição para equilibrar sua posição cambial; p
- Uma curva prefixada apresenta rendimentos de 10% ao ano para o prazo curto e 8% para prazo
- O Banco Central anuncia operação compromissada de compra de títulos públicos com obrigação
- Um banco central comunica que manterá taxas baixas se a inflação projetada atender a uma c
- Uma instituição de pagamento autorizada sob o art. 29, III, da Resolução BCB nº 277/2022 p
- Uma instituição autorizada examina remessa para manutenção de estudante no exterior e soli
- Um fundo oferece remuneração de 100% da taxa DI e informa aos participantes que, por essa 
- Uma tesouraria distingue risco de liquidez de insolvência ao observar que ativos de longo 
- Num cenário hipotético de inflação acumulada de 5% e rentabilidade nominal de 5% no mesmo 
- No mercado interbancário cambial, uma compra e venda entre instituições pode ser liquidada
- Uma tesouraria conclui que a redução do recolhimento compulsório obrigará bancos a concede
- Para comparar quantitative easing e operação compromissada cotidiana, importa observar a e
- Por existir relação entre taxas domésticas e fluxos de capital, uma alta da Selic determin
```

## Pedido 9: IBS e CBS (LC 214/2025): regimes específicos, Comitê Gestor e transição (`direito-tributario.ibs-cbs-regimes-especificos-administracao`, 72 tópicos de edital)

```
Matéria: direito-tributario.ibs-cbs-regimes-especificos-administracao (IBS e CBS (LC 214/2025): regimes específicos, Comitê Gestor e transição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Combustíveis: incidência monofásica, base, sujeitos, EAC e créditos": 19
- "2. Serviços financeiros, planos de saúde e concursos de prognósticos": 22
- "3. Bens imóveis e sociedades cooperativas: reduções, fatos geradores e créditos": 25
- "4. Bares, restaurantes, hotelaria e limites das regras especiais": 16
- "5. Administração do IBS, CGIBS, avaliação e transição 2026–2033": 18

Já existem (não repita):
- Uma refinaria vende combustível abrangido pelo art. 172 da LC 214/2025 a distribuidora, qu
- Uma operação envolve 8.000 litros de combustível monofásico com alíquota hipotética de R$ 
- Um posto adquire combustível sujeito à tributação monofásica para revenda ao consumidor. Q
- Uma instituição bancária cobra tarifa regulamentada de manutenção de conta de depósito. Qu
- Uma operadora de plano de assistência à saúde aplica o art. 237 da LC 214/2025 sobre alíqu
- Uma operadora de concursos de prognósticos arrecada R$ 500 mil, paga R$ 320 mil em prêmios
- Um contribuinte sujeito ao regime imobiliário recebe aluguéis de imóvel comercial. Qual re
- Uma cooperativa optou pelo regime do art. 271 e recebe produtos de associado para posterio
- Um restaurante serve prato preparado, suco produzido no local e vinho. Qual fornecimento e
- Uma administração tributária propõe que cada município edite regulamento próprio para defi
- Em exemplo hipotético, a alíquota de um combustível fóssil é R$ 2,00 por unidade energétic
- Uma refinaria utiliza EAC na mistura obrigatória com gasolina A e analisa quem deve reter 
- Uma instituição financeira calcula a base específica deduzindo despesas financeiras admiti
- Uma operadora oferece planos de assistência à saúde para animais domésticos. O setor contá
- Plataforma de apostas recebe apostas virtuais e pretende dispensar identificação de usuári
- Pessoa física recebeu no ano anterior R$ 300 mil em aluguéis de apenas três imóveis distin
- Duas empresas contribuintes permutam imóveis e uma delas paga quantia complementar à outra
- Em exemplo sem atualização monetária, a venda de imóvel residencial novo tem base inicial 
- Hotel compra insumos tributados para suas operações e emite fatura de hospedagem a empresa
- Uma entidade de planos de assistência à saúde apura base negativa em determinado mês. Pelo
- Uma empresa compra diretamente de contribuinte do art. 176 combustível monofásico. A trans
- Uma entidade que presta serviços financeiros abrangidos pela regra geral do art. 189 proje
- Uma avaliação das vantagens tributárias dos regimes especiais deverá produzir a primeira a
- Uma empresa pretende receber compensação transitória por perda de incentivo de ICMS conced
- Contribuinte recolhe IBS e CBS no ano de 2026 e pretende destiná-los à compensação das con
- Uma empresa adquire combustível abrangido pela monofasia do art. 172. O valor do IBS e da 
- Uma indústria adquire combustível sujeito à monofasia para consumo em sua atividade econôm
- Banco presta serviço de manutenção de conta de depósito remunerado por tarifa regulamentad
- Empresa compra plano de saúde para seus empregados. Por utilizar esse serviço em sua ativi
- Uma lanchonete prepara bebida não alcoólica na própria cozinha e a fornece ao cliente. O f
- Uma empresa hospeda representantes em hotel. O art. 283 assegura crédito de IBS e CBS ao c
- Cooperativa optante pelo art. 271 recebe produção agrícola de associado. Em operação enqua
- Uma pessoa física loca dois imóveis e recebe valores superiores ao limite anual atualizado
- Operadora de apostas calcula sua receita própria para IBS e CBS. Nos termos do art. 245, d
- Uma operadora de plano de saúde para animais domésticos deve aplicar redução de alíquota d
- Adquirente direto de combustível monofásico liquida operação com mecanismo eletrônico de s
- Como o EAC participa da composição da gasolina comercializada no varejo, o art. 178 atribu
- Instituição financeira que recupera crédito anteriormente baixado como prejuízo e deduzido
- Contribuinte de serviço financeiro registra base negativa mensal em regime especial. O art
- Um contribuinte sujeito ao regime imobiliário recebe aluguel referente a determinado mês. 
- No regime de bens imóveis, o redutor social para alienação de residência nova é valor mone
- Pessoa sujeita ao regime regular loca imóvel residencial mobiliado por 60 dias contínuos. 
- Como bares, planos de assistência à saúde e hotéis constam do mesmo Título V, a LC 214/202
- O Conselho Superior do CGIBS apresenta 27 representantes estaduais/distritais e 27 municip
- A avaliação quinquenal dos regimes específicos realizada nos termos do art. 475 produz aut
```

## Pedido 10: Qualidade de Vida no Trabalho (`gestao-pessoas.qualidade-vida-trabalho`, 72 tópicos de edital)

```
Matéria: gestao-pessoas.qualidade-vida-trabalho (Qualidade de Vida no Trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e Modelos Clássicos de Qualidade de Vida no Trabalho": 20
- "2. Modelos Contemporâneos, Abordagem Biopsicossocial e Programas de QVT": 20
- "3. Saúde Ocupacional, Estresse, Síndrome de Burnout e Indicadores de Bem-Estar": 20
- "4. Ergonomia Aplicada, Clima Organizacional e QVT no Serviço Público": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- No estudo da evolução histórica da Qualidade de Vida no Trabalho (QVT), a criação formal d
- Um servidor público relata que, em sua instituição, as avaliações disciplinares seguem est
- De acordo com o modelo das dimensões básicas da tarefa de Hackman e Oldham (1975), o grau 
- Na abordagem Biopsicossocial e Organizacional (BPS-O) proposta por Ana Cristina Limongi-Fr
- Ao analisar o estresse no ambiente profissional, os pesquisadores distinguem a reação adap
- Um técnico administrativo passa a comparecer diariamente ao seu setor mesmo enfrentando fo
- Segundo a Associação Internacional de Ergonomia (IEA), o domínio de especialização ergonôm
- Na administração pública federal, o Subsistema Integrado de Atenção à Saúde do Servidor Pú
- Nas provas de concursos que cobram a relação entre Clima Organizacional e Qualidade de Vid
- No modelo de Richard Walton, a categoria 'compensação justa e adequada' avalia não apenas 
- Programas de QVT de natureza assistencialista caracterizam-se por intervir prioritariament
- A Síndrome de Burnout distingue-se do cansaço físico comum porque, além da exaustão emocio
- Na Ergonomia da Atividade, o trabalho prescrito corresponde à tarefa formalmente planejada
- A Política Nacional de Saúde do Trabalhador e da Trabalhadora (Portaria GM/MS nº 1.823/201
- Em questões de concursos sobre o modelo de William Westley, o sentimento de 'injustiça' de
- Uma instituição pública redesenhou as atribuições de seus auditores para que cada profissi
- No modelo de Hackman e Oldham (1975), as cinco dimensões essenciais do cargo atuam sobre t
- Durante o diagnóstico de QVT em uma autarquia, utilizando a abordagem da Ergonomia da Ativ
- Para que a implantação de um Programa de Qualidade de Vida no Trabalho no setor público su
- Considere o Modelo Demanda-Controle de estresse ocupacional proposto por Robert Karasek (1
- De acordo com o modelo quadrifásico do estresse validado no Brasil por Marilda Lipp (a par
- Uma instituição pública modernizou seu sistema eletrônico de controle de processos, inclui
- Em provas que abordam as Diretrizes Gerais de Promoção da Saúde do Servidor Público Federa
- No modelo de Werther e Davis (1983), a autonomia, a variedade de habilidades, a identidade
- De acordo com a sistematização histórica de Nadler e Lawler (1983), na fase compreendida e
- Sob a ótica da abordagem preventiva de QVT, práticas de promoção da saúde como ginástica l
- Na Psicodinâmica do Trabalho de Christophe Dejours, o sofrimento no trabalho não conduz in
- O estudo e a intervenção sobre regimes de turnos, horários flexíveis, trabalho cooperativo
- A realização periódica de pesquisas de clima organizacional garante, por si só, a elevação
- Em provas de concursos públicos, quando um estudo de caso relata que os trabalhadores de u
- Suponha que uma consultoria de gestão de pessoas tenha avaliado dois cargos (Cargo X e Car
- Compare os modelos clássicos de Richard Walton (1973), William Westley (1979) e Huse e Cum
- Analise a distinção epistemológica e metodológica entre a concepção hegemônica (assistenci
- Um órgão público estruturou sua Gestão da Qualidade de Vida no Trabalho (GQVT) com base no
- Na análise avançada dos fatores psicossociais de risco no trabalho, a literatura articula 
- Em um setor público de análise de processos, a norma interna determinava que cada servidor
- Considerando o arcabouço normativo vigente sobre atenção à saúde e segurança do trabalho n
- Nas provas avançadas que exigem a integração entre ergonomia, inclusão de Pessoas com Defi
- No modelo de Hackman e Oldham, a necessidade individual de crescimento (*Growth Need Stren
- Para Richard Walton, a relação entre produtividade organizacional e Qualidade de Vida no T
- Na concepção da Ergonomia da Atividade Aplicada à QVT (Ferreira), o mal-estar no trabalho 
- O fenômeno do presenteísmo é exclusivo do trabalho presencial físico nas repartições públi
- Para a escola da Ergonomia da Atividade, o objetivo técnico da análise ergonômica do traba
- Conforme a Política Nacional de Saúde do Trabalhador e da Trabalhadora (Portaria GM/MS nº 
- Em provas que cobram a distinção entre os indicadores de estresse e esgotamento no serviço
```

## Pedido 11: Incidente de desconsideração da personalidade jurídica (`processo-trabalho.desconsideracao-personalidade`, 72 tópicos de edital)

```
Matéria: processo-trabalho.desconsideracao-personalidade (Incidente de desconsideração da personalidade jurídica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Autonomia patrimonial e requisitos materiais da desconsideração": 16
- "2. Instauração, alcance temporal, citação e instrução do incidente": 16
- "3. Suspensão, tutela cautelar, decisão e recursos no processo do trabalho": 16
- "4. Teorias da desconsideração, modalidade inversa e Tema 1.232 do STF": 29
- "5. Aplicação em processos coletivos, acordos e resolução de casos complexos": 23

Já existem (não repita):
- Na execução de crédito trabalhista, foi demonstrado que a sociedade pagava repetidamente d
- O titular de crédito pretende alcançar bens particulares em razão de abuso da sociedade. C
- Uma parte ajuíza ação trabalhista contra sociedade e já na petição inicial pede desconside
- Na execução, foi instaurado incidente para examinar responsabilidade de sócio por fatos de
- Durante a fase de conhecimento, o juiz acolheu incidente de desconsideração por decisão in
- Uma decisão na fase de execução rejeitou o incidente, e o requerente pretende impugná-la s
- Em incidente originariamente instaurado em tribunal, o relator decidiu que os pressupostos
- Uma pessoa deve quantia pessoal e transferiu apartamento a uma sociedade controlada para d
- Após instauração do incidente durante execução, há risco comprovado de o sócio alienar bem
- Uma petição pede afastar a personalidade porque duas empresas possuem exatamente os mesmos
- O credor pede desconsideração com base no art. 50 porque a empresa foi usada propositalmen
- O requerente apresenta pedido de IDPJ no curso da execução e solicita que o juízo o conced
- Em controvérsia sobre desconsideração, alguém afirma que a teoria menor exige prova de con
- O processo do trabalho possui disciplina específica do IDPJ no art. 855-A da CLT. Qual con
- Após acolhimento do IDPJ, verifica-se que bem de pessoa atingida foi alienado em circunstâ
- Em processo coletivo trabalhista, há indícios de que o devedor ocultou bens na conta pesso
- Uma pessoa ajuíza execução trabalhista contra empresa que não participou do conhecimento, 
- Uma execução mira terceiro não participante do conhecimento, apontando transferência de pa
- A empresa devedora alegou que qualquer mudança de atividade econômica constitui desvio de 
- Um débito pessoal de participante societário foi ocultado por transferência simulada de be
- O juiz recebe petição de homologação de acordo extrajudicial e alguém invoca o prazo de 15
- Um exequente pretende atingir empresa que não participou da fase de conhecimento por supos
- Uma decisão na execução acolhe incidente e o sócio interpõe agravo de petição sem depósito
- Um dos sujeitos apresentados como responsável por dívida societária prova que não recebeu 
- Uma execução concluída definitivamente e com crédito satisfeito é invocada para reabrir di
- Uma sociedade é utilizada para pagar repetidamente dívidas privadas de seu sócio. Essa cir
- O art. 855-A da CLT afasta os arts. 133 a 137 do CPC porque o processo trabalhista possui 
- Uma parte solicita desconsideração em fase de execução trabalhista e demonstra inicialment
- Durante o conhecimento, a decisão interlocutória que rejeita o IDPJ admite imediatamente a
- Na desconsideração inversa, pretende-se, quando demonstrados os pressupostos de abuso, alc
- A simples insolvência da pessoa jurídica é uma das hipóteses expressas de confusão patrimo
- O incidente instaurado durante o processo suspende seu andamento, mas a CLT permite tutela
- Um pedido fundamentado de desconsideração é formulado desde a petição inicial da ação trab
- No incidente originário em tribunal, a decisão do relator sobre acolhimento de desconsider
- Para pedir IDPJ em causa coletiva, o Ministério Público está dispensado de demonstrar os p
- Acolhido o pedido de desconsideração, uma alienação de bens caracterizada como fraude de e
- As teorias maior e menor se distinguem pela direção do patrimônio atingido: a maior opera 
- Na fase executiva, uma decisão que acolhe o IDPJ pode ser impugnada por agravo de petição 
- A apresentação do pedido de desconsideração na inicial dispensa a citação de sócio ou da s
- Segundo o Tema 1.232 do STF, a execução trabalhista não pode ser direcionada a empresa que
- O Tema 1.232 do STF passou a permitir a revisão de execuções findas e de créditos já integ
- Uma petição de desconsideração fundamentada em abuso é apresentada na execução por parte l
- O incidente de desconsideração e o processo de homologação de acordo extrajudicial são dis
- Uma dívida pessoal foi ocultada por transferência indevida de bens à sociedade controlada 
- Acolhido o incidente, toda alienação anterior feita por qualquer sócio, ainda que regular 
```

## Pedido 12: Imunologia clínica e sorologia (`laboratorio.imunologia-clinica`, 72 tópicos de edital)

```
Matéria: laboratorio.imunologia-clinica (Imunologia clínica e sorologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da resposta imune, antígenos, anticorpos e complemento": 20
- "Técnicas imunológicas e interpretação dos resultados": 20
- "Sorologia de HIV, hepatites virais e sífilis": 20
- "Sorologia de toxoplasmose e rubéola": 20
- "Autoimunidade, imunodeficiências e citometria de fluxo": 20

Já existem (não repita):
- A resposta imune inata utiliza receptores de reconhecimento de padrões e pode atuar antes 
- Linfócitos T CD8+ reconhecem preferencialmente peptídeos associados ao MHC II, enquanto li
- A IgG é uma imunoglobulina com capacidade de atravessar a placenta, motivo pelo qual a sim
- O fragmento C3b é responsável primordialmente pela quimiotaxia intensa de neutrófilos, enq
- O exame CH50 avalia uma função global da via clássica do complemento e não corresponde sim
- Na reação de precipitação, a formação de redes insolúveis entre antígenos solúveis e antic
- Em um imunoensaio competitivo, maior concentração do analito produz obrigatoriamente maior
- A especificidade de um teste, mantida constante, determina sozinha o seu valor preditivo p
- Uma linha controle ausente em teste rápido imunocromatográfico torna o dispositivo inválid
- O teste laboratorial de HIV de quarta geração pode pesquisar simultaneamente antígeno p24 
- A contagem de linfócitos T CD4+ substitui a carga viral de HIV como medição direta da repl
- A vacinação isolada contra hepatite B induz, tipicamente, anti-HBs, mas não anti-HBc total
- Anti-HCV reagente comprova por si só infecção atual pelo vírus da hepatite C, mesmo sem pe
- Testes treponêmicos para sífilis costumam ser utilizados como marcadores quantitativos de 
- Na toxoplasmose, IgM reagente e IgG reagente determinam, sem exames adicionais, que a infe
- A alta avidez de IgG para toxoplasmose, quando pesquisada precocemente na gestação e inter
- A pesquisa rotineira de IgM de rubéola em gestantes assintomáticas é recomendada como rast
- Um FAN positivo, isoladamente, confirma diagnóstico de lúpus eritematoso sistêmico.
- O fator reumatoide pode ser detectado em infecções crônicas e em pessoas sem artrite reuma
- Na citometria de fluxo, SSC relaciona-se à granulosidade ou complexidade interna, enquanto
- Em um laboratório, deseja-se identificar uma proteína do complemento que favorece a fagoci
- Assinale a alternativa que descreve a função mais característica da IgA secretora.
- Do ponto de vista imunológico, qual célula exerce papel importante na apresentação de antí
- Uma amostra apresenta possível deficiência de atividade da via clássica do complemento. Qu
- Qual associação entre imunoglobulina e característica está adequada?
- Um ensaio mistura partículas recobertas por antígeno e soro do paciente, formando grumos v
- No ELISA competitivo típico, a amostra apresenta elevada concentração do analito. Qual com
- Um resultado sorológico reagente ocorre em população com prevalência muito baixa, mesmo us
- O laboratório investiga anticorpos antinucleares usando células HEp-2 e anticorpo secundár
- Um teste imunocromatográfico mostra traço na região de teste, porém a linha controle não s
- Um imunoensaio de quarta geração para HIV fornece reatividade inicial. Qual interpretação 
- Um paciente vacinado contra hepatite B tem HBsAg não reagente, anti-HBc total não reagente
- Qual marcador é especialmente útil para apoiar o diagnóstico de hepatite B aguda, com as r
- Pessoa com anti-HCV reagente e HCV-RNA não detectável, sem indícios de exposição recente, 
- Em seguimento sorológico de sífilis, o VDRL passou de 1:64 para 1:16, usando a mesma técni
- Diante de um teste não treponêmico para sífilis não reagente em paciente com manifestações
- Uma mulher com sorologia anti-Toxoplasma IgG e IgM reagentes recebe exame de avidez de IgG
- Gestante com sorologia inicial para toxoplasmose apenas após 20 semanas recebe IgG de alta
- Ao elaborar protocolo de rastreamento pré-natal de rubéola, uma unidade propõe IgM para to
- Uma mulher com suspeita de rubéola apresenta exantema recente e dados epidemiológicos comp
- Na investigação de possível doença autoimune, o laboratório informa FAN reagente em baixo 
- Qual achado define classicamente o fator reumatoide?
- Um paciente tem infecções bacterianas sinopulmonares de repetição. Qual conjunto de exames
- Em citometria, qual combinação auxilia a selecionar linfócitos T auxiliares entre os leucó
- A intensidade de fluorescência de CD4 em um citômetro de fluxo equivale automaticamente à 
```

## Pedido 13: Pacto Internacional dos Direitos Econômicos, Sociais e Culturais (PIDESC) (`direito.pacto-internacional-direitos-economicos-sociais`, 72 tópicos de edital)

```
Matéria: direito.pacto-internacional-direitos-economicos-sociais (Pacto Internacional dos Direitos Econômicos, Sociais e Culturais (PIDESC))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Origem, autodeterminação e deveres gerais — artigos 1º a 5º": 21
- "2. Trabalho, sindicatos, previdência e família — artigos 6º a 10": 20
- "3. Vida digna, saúde, educação e cultura — artigos 11 a 15": 20
- "4. Relatórios, cooperação e salvaguardas — artigos 16 a 25": 19
- "5. Vigência, aplicação federativa, emendas e questões integradas — artigos 26 a 31": 20

Já existem (não repita):
- Um material de estudo afirma que o PIDESC foi promulgado no Brasil por um decreto editado 
- Um país sustenta que a autodeterminação prevista no art. 1º pertence somente a cidadãos in
- Diante de dificuldades orçamentárias, um Estado Parte afirma que somente adotará medidas d
- Em programa de inclusão laboral, é assegurada a possibilidade de a pessoa ganhar a vida po
- Uma política social pretende reconhecer previdência social, inclusive seguro social, como 
- O Estado debate um programa de melhoria do nível de vida que inclua alimentação, vestiment
- Uma proposta educacional assegura ensino primário obrigatório e gratuito a todas as pessoa
- O governo de um Estado Parte precisa apresentar informações sobre medidas adotadas e progr
- Um Estado pretende limitar determinado direito do PIDESC por ato administrativo genérico, 
- Uma associação sindical pretende formar federação nacional e estabelecer vínculos com orga
- Em um projeto para proteção de mães trabalhadoras, é assegurada licença anterior e posteri
- Um Estado, ao aderir ao tratado, ainda não assegura educação primária obrigatória e gratui
- Uma política educacional decide oferecer ensino superior a todas as pessoas com o mesmo ac
- Durante a redação de relatório de implementação, um Estado pretende descrever fatores que 
- Em reunião internacional, discute-se a atribuição do art. 22: comunicar questões dos relat
- Um Estado federativo aceita o PIDESC e depois pretende excluir determinadas unidades regio
- País em desenvolvimento decide a extensão de certos direitos econômicos do Pacto a residen
- Uma norma interna protege certo direito fundamental mais amplamente que o texto expresso d
- Uma empresa estabelece remuneração desigual entre trabalhadores que desempenham atividade 
- Uma família deseja matricular a criança em escola não criada pelo Estado e receber formaçã
- O Conselho Econômico e Social recebe relatórios de Estados e agências sobre direitos do Pa
- O texto do PIDESC possui duas salvaguardas na Parte IV. Uma preserva responsabilidades da 
- Um Estado ratifica o PIDESC após já depositado o trigésimo quinto instrumento. Segundo o a
- Estados Partes negociam emenda ao PIDESC. Houve apoio de um terço à conferência e maioria 
- Uma pessoa compara o texto português publicado no Brasil com os textos igualmente autêntic
- Ao elaborar um material sobre o PIDESC, uma equipe organiza o tratado em cinco partes: art
- Um Estado Parte entende que pode privar um povo de seus próprios meios de subsistência par
- Uma política de emprego promove orientação e formação técnico-profissional, buscando pleno
- Uma associação de trabalhadores consulta o art. 8º e conclui que ele nega o direito de gre
- Em ações contra doenças infecciosas, o Estado adota prevenção e tratamento de doenças epid
- Uma instituição de ensino afirma que o art. 13 do Pacto protege apenas a educação oferecid
- Ao ser questionado sobre administração federativa, um Estado signatário reconhece que o ar
- Para implementar direitos sociais, um Estado decide deixar de apresentar medidas próprias 
- Uma lei protege mais amplamente determinado direito fundamental do que a redação específic
- Uma proposta de promoção profissional utiliza critérios de prestígio familiar e preferênci
- No enfrentamento da fome, um programa internacional busca difundir educação nutricional e 
- Um Estado não assegurava ensino primário gratuito quando aderiu ao PIDESC. O art. 14 exige
- Ao compilar o relatório de direitos sociais, o Estado menciona dificuldades materiais e fa
- Segundo o art. 21 do PIDESC, o Conselho Econômico e Social deve encaminhar individualmente
- Um Estado deposita sua ratificação após o trigésimo quinto instrumento do PIDESC e calcula
- No procedimento de emenda do art. 29, basta um terço dos Estados Partes aceitar o texto ap
- Segundo o art. 8º, item 2, o Pacto não impede restrições legais ao exercício dos direitos 
- Ao participar de negociação internacional, um Estado considera que o art. 25 do PIDESC aut
- Em projeto científico, pesquisadores reivindicam liberdade de pesquisa e proteção de inter
- Para fins de interpretação do art. 31 do PIDESC, o texto português promulgado pelo Decreto
```

## Pedido 14: Regime Jurídico Único dos Servidores do Pará (Lei 5.810/1994) (`legislacao.servidores-para-lei-5810`, 71 tópicos de edital)

```
Matéria: legislacao.servidores-para-lei-5810 (Regime Jurídico Único dos Servidores do Pará (Lei 5.810/1994))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Provimento, concurso, posse, exercício e estágio probatório": 20
- "2. Carreira, movimentação, vacância, jornada e estabilidade": 20
- "3. Férias, licenças e direito de petição": 20
- "4. Remuneração, vantagens e acumulação": 20
- "5. Deveres, penalidades e processo disciplinar": 20

Já existem (não repita):
- Segundo a Lei 5.810/1994, são formas de provimento de cargo público, EXCETO:
- As disposições da Lei 5.810/1994 aplicam-se aos servidores dos Poderes Executivo, Legislat
- Nos concursos regidos pela Lei 5.810/1994, em caso de empate na classificação, tem preferê
- Na Lei 5.810/1994, as provas de concurso são avaliadas de zero a dez pontos, e aos títulos
- Pela Lei 5.810/1994, a posse ocorrerá no prazo de:
- Pela Lei 5.810/1994, o candidato pode renunciar à posse no prazo legal, sendo-lhe garantid
- O servidor do Pará preso em flagrante ou pronunciado por crime comum será afastado do exer
- Sobre o estágio probatório na Lei 5.810/1994, assinale a correta.
- Na Lei 5.810/1994, a promoção do servidor estável obedece aos critérios de antiguidade e m
- A movimentação do servidor efetivo para outro cargo de igual denominação em outro órgão, m
- A reversão voluntária do servidor aposentado, no Pará, exige interesse da Administração fu
- A duração da jornada diária de trabalho do servidor regido pela Lei 5.810/1994, salvo jorn
- O horário especial para o servidor que tenha sob seus cuidados pessoa com deficiência perm
- O art. 67 da Lei 5.810/1994 prevê que o servidor adquire estabilidade ao completar dois an
- É vedada a exoneração, suspensão ou demissão do servidor sindicalizado a partir do registr
- O servidor do Pará que opera direta e permanentemente com raios X ou substâncias radioativ
- A licença por motivo de doença em pessoa da família, na Lei 5.810/1994, é concedida com re
- Pela Lei 5.810/1994, com a redação de 2023, a perícia médica para licença de saúde será di
- A licença à servidora gestante, na Lei 5.810/1994, é de:
- A licença-paternidade do servidor paraense é de dez dias consecutivos, contados da apresen
- Sobre a licença-prêmio na Lei 5.810/1994, é correto afirmar:
- A licença para tratar de interesses particulares, no Pará, pode ser concedida ao servidor 
- As folgas premiais previstas na Lei 5.810/1994 são concedidas:
- No direito de petição da Lei 5.810/1994, o prazo para decidir é de trinta dias, e, não hav
- O direito de requerer, na Lei 5.810/1994, quanto a atos de demissão e de cassação de apose
- O adicional por tempo de serviço, na Lei 5.810/1994, é devido:
- O serviço extraordinário, na Lei 5.810/1994, é pago com acréscimo de cinquenta por cento s
- A gratificação de interiorização, prevista na Lei 5.810/1994, é devida aos servidores que:
- As consignações facultativas em folha de pagamento do servidor paraense não podem exceder 
- Sobre diárias e ajuda de custo na Lei 5.810/1994, assinale a correta.
- O texto do art. 162 da Lei 5.810/1994 admite, com compatibilidade de horários, a acumulaçã
- São penas disciplinares previstas no art. 183 da Lei 5.810/1994:
- A pena de suspensão, na Lei 5.810/1994, não excederá noventa dias e, havendo conveniência 
- Configura inassiduidade habitual, punível com demissão na Lei 5.810/1994, a falta ao servi
- A ação disciplinar, na Lei 5.810/1994, prescreve em cento e oitenta dias quanto à repreens
- Sobre o Termo de Ajustamento Disciplinar (TAD) na Lei 5.810/1994, assinale a correta.
- No processo disciplinar da Lei 5.810/1994, o afastamento preventivo do servidor, como medi
- O processo disciplinar da Lei 5.810/1994 é conduzido por comissão de três servidores estáv
- Havendo dois ou mais indiciados no processo disciplinar regido pela Lei 5.810/1994, o praz
- Na revisão do processo disciplinar regida pela Lei 5.810/1994, julgada procedente, declara
- Pela Lei 5.810/1994, é competente para aplicar a pena de suspensão superior a trinta dias:
```

## Pedido 15: Tabela periódica e propriedades periódicas (`ciencias.tabela-periodica`, 71 tópicos de edital)

```
Matéria: ciencias.tabela-periodica (Tabela periódica e propriedades periódicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Evolução histórica, lei de Moseley e organização moderna": 23
- "2. Grupos, períodos, blocos e famílias de elementos": 29
- "3. Configuração eletrônica, periodicidade e tendências de tamanho": 21
- "4. Energia de ionização, eletronegatividade e caráter metálico": 14
- "5. Propriedades aperiódicas, análise integrada e exceções": 13

Já existem (não repita):
- Um estudante encontra uma classificação antiga em que cloro, bromo e iodo formam um conjun
- Em uma exposição histórica, foi descrita a tentativa de colocar elementos em sequência por
- Em uma tabela elaborada no século XIX, seu autor deixou espaços para elementos ainda não e
- Em uma apresentação, afirma-se que a ordem moderna dos elementos deriva da investigação do
- Um conjunto contém flúor, cloro, bromo e iodo. No arranjo usual da tabela, a qual grupo e 
- O hélio, Z = 2, apresenta configuração fundamental 1s². Qual afirmação concilia sua posiçã
- Um estudante compara o raio atômico de Li, Na e K, todos do grupo 1, em condições comparáv
- Um átomo neutro de sódio perde um elétron e passa a Na⁺. O que ocorre, em termos gerais, c
- Uma atividade pergunta qual propriedade expressa a tendência de um átomo atrair os elétron
- Um texto didático compara propriedades periódicas com outras que não exibem ciclos eletrôn
- A tabela mostra o argônio antes do potássio, apesar de a massa atômica média do Ar ser mai
- O átomo neutro de sódio, Z = 11, tem configuração 1s² 2s² 2p⁶ 3s¹. Que localização segue d
- Um exercício pede a configuração fundamental real do cromo neutro, Z = 24, sabendo que há 
- Uma amostra contém silício, enxofre e ferro. Um relatório precisa classificar corretamente
- Os íons O²⁻, F⁻, Na⁺ e Mg²⁺ apresentam dez elétrons cada. Em uma comparação coerente de se
- Os dados experimentais do NIST mostram primeira energia de ionização do Be próxima de 9,32
- Um material informa que a primeira energia de ionização do nitrogênio é aproximadamente 14
- Uma comparação entre sólidos metálicos usa calor específico por unidade de massa e a aprox
- Um texto contrapõe duas etapas da ciência: uma tabela com lacunas e previsões no século XI
- Um historiador compara quatro formas de organização: tríades, oitavas, tabela com previsão
- Um elemento neutro tem Z = 17 e configuração 1s² 2s² 2p⁶ 3s² 3p⁵. Qual descrição reúne cor
- Um átomo neutro de sódio possui uma primeira e uma segunda energia de ionização. Ao retira
- Um estudante compara os raios iônicos de F⁻ e Cl⁻, ânions do mesmo grupo com cargas iguais
- Uma atividade pede comparar índices de refração de substâncias compostas por elementos dif
- Um quadro comparativo contrapõe caráter metálico e eletronegatividade em elementos represe
- Ao comparar as propostas históricas, atribui-se a Döbereiner a ideia de tríades de element
- Na apresentação atual da tabela periódica, há dezoito períodos horizontais e sete grupos v
- O hélio pertence ao grupo 18 por suas propriedades de gás nobre, embora possua configuraçã
- Entre átomos neutros comparáveis de um mesmo grupo representativo, o raio atômico tende a 
- O peso atômico padrão, entendido como valor relacionado à média ponderada dos isótopos em 
- A lei das oitavas de Newlands foi formulada a partir de número atômico obtido em raios X, 
- O ferro neutro apresenta configuração aproximada [Ar] 3d⁶ 4s². Na formação de Fe²⁺, a remo
- Os íons Na⁺ e Mg²⁺ possuem dez elétrons, logo apresentam necessariamente o mesmo raio, já 
- A eletronegatividade descreve a capacidade de um átomo atrair elétrons de ligação, e o flú
- Ao avançar da esquerda para a direita em um período, a primeira energia de ionização cresc
- A primeira energia de ionização corresponde à energia necessária para retirar um elétron d
- O calor específico por unidade de massa de qualquer elemento sólido é determinado exclusiv
- A contribuição de Moseley, a partir de estudos de raios X por volta de 1913, fundamentou a
- Como o hidrogênio costuma aparecer acima dos metais alcalinos no grupo 1, ele deve ser cla
- Ao comparar o átomo neutro de cloro e o ânion Cl⁻ em condições coerentes, espera-se aument
- O oxigênio possui primeira energia de ionização superior à do nitrogênio por conter mais p
- O caráter metálico tende, em linhas gerais, a aumentar ao descer um grupo e a deslocar-se 
- A eletronegatividade de Pauling e a primeira energia de ionização representam a mesma medi
- O índice de refração depende de características do meio e da luz, como comprimento de onda
- Como a massa atômica média aumenta de forma necessariamente estrita com cada novo elemento
```

## Pedido 16: Teletrabalho e trabalho intermitente (`direito-trabalho.teletrabalho-intermitente`, 70 tópicos de edital)

```
Matéria: direito-trabalho.teletrabalho-intermitente (Teletrabalho e trabalho intermitente)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teletrabalho: conceito, regimes de prestação e jornada": 23
- "2. Formalização, transição, despesas, prioridades e saúde no teletrabalho": 21
- "3. Teletrabalho na prática: controle, comunicações e situações-limite": 20
- "4. Trabalho intermitente: contrato, convocação, resposta e inatividade": 18
- "5. Pagamento, direitos do intermitente e confronto dos dois regimes": 18

Já existem (não repita):
- Uma empregada executa tarefas com computador em casa três dias por semana e comparece à em
- Uma pessoa contratada para trabalhar remotamente recebe por entregas de tarefas, sem regim
- Um vínculo inicialmente presencial será alterado para teletrabalho por proposta aceita pel
- Um empregador determina que trabalhador atualmente remoto volte a prestar serviços presenc
- Uma empresa pretende definir quem fornece notebook, mantém a conexão e reembolsa despesas 
- Antes de iniciar atividade em casa, o empregador comunica por escrito medidas de prevenção
- Uma vaga admite execução remota, e a empresa compara candidatos empregados com e sem filho
- Um estabelecimento contrata empregado subordinado para períodos alternados de serviços e i
- Uma pessoa com contrato intermitente deve ser chamada para um período de serviços. Qual an
- Um empregado intermitente recebe convocação eficaz numa segunda-feira, com jornada e antec
- Um empregado intermitente não responde à convocação dentro de um dia útil após recebê-la. 
- Um prestador intermitente aceita serviço com remuneração prevista de R$ 400 e, sem justo m
- Um contratado intermitente permanece duas semanas sem convocação e presta serviços a outro
- Após dois dias de prestação intermitente, a empresa prepara recibo contendo apenas o valor
- Após doze meses de vínculo intermitente, o empregado solicita férias. Qual regra específic
- Um empregado está lotado em unidade de Recife, mas trabalha remotamente desde sua residênc
- Uma pessoa admitida no Brasil opta por teletrabalhar a partir de outro país. Segundo o art
- Uma pessoa optou por teletrabalhar em cidade distante da localidade indicada no contrato. 
- Depois de encerrada a jornada remota controlada, um empregado acessa espontaneamente aplic
- Um estabelecimento oferece atividades de formação que podem ser realizadas fora de suas in
- Em contrato intermitente escrito, o salário mínimo horário hipotético é R$ 9 e os demais e
- O recibo de trabalho intermitente discrimina remuneração e férias proporcionais, mas reúne
- O empregado intermitente presta serviços em dois períodos no mesmo mês e recebe as parcela
- Uma empregada remota relata adoecimento musculoesquelético e a empresa argumenta que o ter
- Após notícias sobre ações judiciais, um contratante sustenta que o regime intermitente foi
- Uma pessoa trabalha remotamente com tecnologias digitais dois dias por semana e comparece 
- A CLT exclui da disciplina da duração do trabalho qualquer empregado em teletrabalho, inde
- O retorno do teletrabalho ao presencial por determinação patronal admite transição de pelo
- O notebook fornecido e o reembolso de internet no teletrabalho integram necessariamente o 
- O empregador deve instruir expressamente empregados remotos acerca da prevenção de doenças
- O contrato intermitente dispensa a forma escrita desde que o aplicativo utilizado nas conv
- A convocação para prestação intermitente deverá informar a jornada e ocorrer pelo menos tr
- Em contrato intermitente, a falta de resposta no dia útil posterior ao recebimento equival
- A recusa de convocação por trabalhador intermitente não descaracteriza sua subordinação co
- Um trabalhador intermitente recusa regularmente uma convocação recebida; a simples recusa 
- Durante os intervalos de inatividade do contrato intermitente, a pessoa pode prestar servi
- O pagamento de férias proporcionais com um terço ao final de cada prestação intermitente s
- O art. 452-A, § 8º, determina depósitos e recolhimentos pertinentes com base nos valores p
- No contrato intermitente, cada período de inatividade deverá ser remunerado como disponibi
- Para o empregado em teletrabalho lotado em determinado estabelecimento, o art. 75-B, § 7º,
- Se uma pessoa admitida no Brasil optar por realizar teletrabalho fora do território nacion
- O uso espontâneo de um aplicativo corporativo após o expediente não constitui necessariame
- A prioridade do art. 75-F para vagas de teletrabalho restringe-se às mães biológicas com f
- Na investigação de adoecimento associado a trabalho em casa, avaliação ergonômica das cond
- O julgamento conjunto das ADIs 5826, 5829 e 6154, concluído em dezembro de 2024, declarou 
```

## Pedido 17: Dano extrapatrimonial e responsabilidade civil trabalhista (`direito-trabalho.dano-moral-responsabilidade`, 70 tópicos de edital)

```
Matéria: direito-trabalho.dano-moral-responsabilidade (Dano extrapatrimonial e responsabilidade civil trabalhista)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Dano extrapatrimonial, bens protegidos e titularidade": 27
- "2. Quantificação, cumulatividade e interpretação constitucional": 27
- "3. Assédio moral, assédio sexual e igualdade no ambiente de trabalho": 13
- "4. Responsabilidade civil trabalhista: culpa, risco e nexo causal": 21
- "5. Prova, avaliação psicológica e prevenção institucional": 12

Já existem (não repita):
- Uma empresa divulgou sem autorização informações íntimas de uma empregada em comunicado pr
- Uma empresa teve sua marca comercial ilicitamente associada a falsificações em conflito li
- Uma trabalhadora apresenta pedido de indenização pela ofensa à sua honra e, separadamente,
- Uma pessoa alega assédio moral porque recebeu uma advertência profissional reservada, fund
- Uma organização com Cipa está estruturando programa de prevenção de assédio e de outras vi
- Uma chefia humilha reiteradamente um trabalhador diante dos colegas, com insultos e exposi
- Uma trabalhadora recebeu mensagens sexualizadas insistentes de colega sem autoridade de pr
- Um empregado ajuíza ação por humilhações laborais e o réu alega que a Justiça do Trabalho 
- Uma empresa recebe denúncia de assédio e o responsável interno conclui que a contratação d
- Uma trabalhadora com último salário contratual de R$ 2.500 pede indenização por ofensa cla
- Um juiz fixa indenização superior à referência de cinquenta salários em ofensa gravíssima,
- Uma empresa alega que o sofrimento moral próprio de familiar de trabalhador é necessariame
- Uma chefia publicou retratação espontânea após divulgar comentário vexatório sobre integra
- Supervisor, durante suas atribuições, dirige insultos discriminatórios a subordinado e pro
- Uma empresa com Cipa publica normas e instala canal de denúncias de assédio, mas deixa de 
- Uma empresa comete ilícito que gera despesa comprovada de R$ 4.000 e ofensa indenizável à 
- O dano alegado inclui sintomas psicológicos após episódios relatados de assédio. Um laudo 
- Uma pessoa jurídica sofreu ofensa extrapatrimonial em razão de ato de trabalhador com salá
- Duas situações são comparadas: na primeira, preposto ofende trabalhador no exercício do tr
- Em processo por assédio, documentos empresariais sobre canal de denúncias estão sob contro
- Após ato ofensivo que atingiu uma pessoa e seus familiares de maneira distinta, o empregad
- Após indenização por ofensa à honra, ocorre outra lesão envolvendo as mesmas partes. A dec
- Uma empregada é hostilizada por colegas sem posição de chefia e o episódio se repete por m
- Um juiz analisa pedido fundado em humilhação repetida, com danos materiais comprovados e l
- Um laudo psicológico identifica sofrimento relevante, mas declara não ser possível atribuí
- Os arts. 223-A a 223-G da CLT disciplinam danos extrapatrimoniais decorrentes da relação d
- A proteção extrapatrimonial de uma pessoa jurídica pelo art. 223-D da CLT exige prova de d
- Os parâmetros numéricos do art. 223-G, § 1º, da CLT são, respectivamente, três, cinco, vin
- A retratação espontânea do autor da ofensa, prevista no art. 223-G da CLT, extingue obriga
- A conduta de colegas do mesmo nível hierárquico pode configurar assédio moral horizontal q
- O art. 23 da Lei nº 14.457/2022 prevê, para empresas com Cipa, treinamento preventivo apen
- Ao julgar conjuntamente as ADIs 6050, 6069 e 6082, o STF preservou a possibilidade de inde
- Após a decisão das ADIs 6050, 6069 e 6082, o juiz trabalhista está constitucionalmente pro
- A reparação por dano extrapatrimonial pode ser cumulada com indenização por dano patrimoni
- A inexistência de diagnóstico psicológico torna juridicamente impossível reparar ofensa ún
- Os arts. 932, III, e 933 do Código Civil podem fundamentar responsabilidade do empregador 
- Para a responsabilidade objetiva do art. 927, parágrafo único, do Código Civil, é dispensa
- Um ato sexualmente ofensivo praticado uma única vez pode atingir bem extrapatrimonial do t
- O art. 223-G, § 2º, da CLT manda calcular a referência da indenização por ofensa a pessoa 
- Conforme o art. 223-G, § 3º, da CLT, a reincidência entre partes idênticas permite ao juiz
- Quando o dano extrapatrimonial decorre de comportamento de preposto no trabalho, basta afi
- O art. 818 da CLT admite, nos casos previstos e mediante decisão fundamentada, redistribui
- A Lei nº 14.457/2022 determina que a apresentação de denúncia ao canal interno substitui o
- A Súmula nº 392 do TST e o art. 114, VI, da Constituição relacionam-se à competência da Ju
- Uma perícia psicológica que evidencia sofrimento obriga o juiz a reconhecer como verdadeir
```

## Pedido 18: Legislação urbanística municipal: uso e ocupação do solo, obras e posturas (`legislacao-municipal.uso-ocupacao-solo-obras-posturas`, 70 tópicos de edital)

```
Matéria: legislacao-municipal.uso-ocupacao-solo-obras-posturas (Legislação urbanística municipal: uso e ocupação do solo, obras e posturas)
Capítulos e quantas questões quero em cada um (total 100):
- "Competência municipal, zoneamento e categorias de uso": 20
- "Índices urbanísticos, volumetria e proteção territorial": 20
- "Parcelamento do solo, loteamentos e desmembramentos": 20
- "Código de obras: licenças, execução, demolições e fiscalização": 20
- "Código de posturas: higiene urbana, logradouros e atividades": 20

Já existem (não repita):
- A lei de zoneamento trata, entre outros temas, da compatibilidade de atividades com porçõe
- A permissão para uso comercial de um terreno substitui automaticamente as exigências de al
- O coeficiente de aproveitamento mede, em regra, a razão entre área construída computável e
- Todo pavimento superior é necessariamente não computável na taxa de ocupação e computável 
- A existência de nova via pública para servir lotes de uma gleba indica, em regra, a modali
- No desmembramento, podem ser criadas novas ruas desde que o número de lotes resultantes se
- A classificação de uma área como de expansão urbana não elimina a necessidade de verificar
- O alvará de construção e o habite-se costumam corresponder a etapas distintas: autorização
- O habite-se é suficiente, por si só, para comprovar propriedade e substituir o registro im
- Instalações provisórias de canteiro, como tapumes e andaimes, continuam sujeitas a cuidado
- Por definição, embargo e interdição são termos equivalentes e têm sempre o mesmo objeto: i
- A constatação de perigo iminente pode justificar medidas administrativas urgentes, respeit
- O código de posturas pode disciplinar a limpeza de ruas, praças e logradouros, além de fei
- Uma feira autorizada pelo município deixa de precisar cumprir condições de limpeza e acess
- A ocupação de calçada por mesas comerciais pode depender de autorização específica, mesmo 
- A existência de um recuo frontal exigido em lei é irrelevante quando o projeto atende ao c
- A taxa de permeabilidade se confunde obrigatoriamente com o percentual não ocupado pela pr
- Uma licença para demolir uma edificação pode coexistir com exigências de proteção de imóve
- As exigências de publicidade externa e anúncios pertencem necessariamente e exclusivamente
- Sem identificação de município ou norma local, não é correto afirmar que um mesmo recuo ob
- Um técnico recebe consulta sobre qual atividade pode funcionar em determinada zona. Qual v
- Um restaurante está em zona que admite serviços, mas pretende instalar mesas sobre a faixa
- Em um lote de 600 m², a projeção computável é 210 m². Qual a taxa de ocupação, para fins d
- A área construída computável de um empreendimento totaliza 900 m² em lote de 450 m². Seu c
- Qual afirmação descreve corretamente o gabarito edilício?
- Projeto cria uma via para atender a novos lotes originados de uma gleba. A modalidade fede
- Ao descrever desmembramento, qual requisito distingue essa modalidade de loteamento?
- Uma área figura no mapa como de expansão urbana. Qual conclusão é juridicamente mais segur
- Um empreendimento teve seu projeto urbanístico aprovado, mas ainda não realizou registro n
- Qual documento tem como finalidade usual autorizar o início de uma obra sujeita a licencia
- Ao final de obra residencial sujeita a licença, o documento municipal usualmente associado
- Um tapume invade parcialmente o passeio público para proteger obra em andamento. Qual anál
- Após constatar obra em execução sem observar o projeto e com risco relevante, a administra
- Edificação apresenta risco imediato à permanência de pessoas em seu interior. Qual providê
- Proprietário deseja demolir edificação próxima a bem cultural protegido. Qual procedimento
- Uma obra apresenta cumprimento do CA, mas desrespeita afastamento lateral obrigatório. Qua
- Em lote de 500 m², a norma hipotética exige 25% de permeabilidade. Qual área mínima result
- Uma construção possui 40% de projeção computável e 20% de superfície permeável computável.
- Uma oficina permitida no zoneamento passa a produzir ruído excessivo e tráfego incompatíve
- Qual matéria é mais característica de um código de posturas do que de uma lei de parcelame
- Um comerciante possui licença de funcionamento e deseja instalar um letreiro saliente sobr
- O fiscal vê lixo acumulado em terreno vazio com água parada e potencial risco à vizinhança
- Uma banca afirma que embargo e interdição possuem efeitos idênticos. Qual resposta disting
- Uma prefeitura exige aprovação de projeto de parcelamento, mas o interessado sustenta que 
- O mesmo lote respeita a taxa de ocupação e o uso permitido, mas está em área sujeita a ris
```

## Pedido 19: Forense e perícia digital (`informatica.forense-pericia-digital`, 70 tópicos de edital)

```
Matéria: informatica.forense-pericia-digital (Forense e perícia digital)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da perícia digital, preservação e cadeia de custódia": 27
- "2. Dados voláteis, aquisição física e lógica, imagens e verificação": 24
- "3. Sistemas de arquivos, arquivos apagados, metadados e cronologia": 16
- "4. Logs do Windows e correlação de vestígios locais e remotos": 15
- "5. Antiforense, sanitização, limites jurídicos e relatório pericial": 18

Já existem (não repita):
- Um laboratório organizou a investigação digital em coleta, exame, análise e elaboração de 
- Após identificar um dispositivo relevante, a equipe precisa registrar sua posição, conexõe
- Uma estação está ligada e mantém processos ativos e conexões correntes que podem se perder
- Foi realizada exportação de arquivos visíveis e suas pastas, sem copiar setores não alocad
- Uma imagem de disco adquirida apresenta SHA-256 idêntico em duas verificações feitas sobre
- Em um volume formatado com FAT, registros de um arquivo apagado podem ser examinados a par
- Ao buscar fragmentos de imagens em espaço não alocado, o exame identifica assinaturas e es
- Em análise de um volume NTFS, o examinador precisa localizar registros de arquivos e atrib
- No Visualizador de Eventos do Windows, um evento do canal Security possui ID 4624 e campos
- Ao pesquisar tentativas malsucedidas de autenticação em um equipamento Windows, foram iden
- Dois arquivos obtidos de locais diferentes apresentam o mesmo SHA-256. O examinador deseja
- Ao reconstruir cronologia, foi lido um timestamp de modificação às 16h00 UTC e outro regis
- Um arquivo de auditoria Security contém evento 4688 associado a determinado identificador 
- Em exame de logs, identificou-se evento Security 1102 e, posteriormente, ausência de regis
- Um programa mostra hash SHA-256 de um arquivo em representação hexadecimal convencional. Q
- Uma imagem de blocos adquirida de mídia compatível inclui regiões marcadas livres pelo sis
- Um provedor de conexão à internet mantém registros de conexão dentro da obrigação de guard
- Um vestígio já foi coletado, acondicionado e transportado, chegando à unidade destinatária
- Uma estação ativa contém dados de RAM considerados relevantes, mas a obtenção exigirá exec
- Em um SSD com TRIM ativo, um arquivo foi excluído e já não consta das estruturas de diretó
- Um serviço em nuvem oferece exportação autorizada de registros de auditoria, mas não permi
- Uma aplicação comercial na internet mantém registros de acesso nos termos do art. 15, capu
- Um procedimento de investigação penal envolve tratamento de dados pessoais para finalidade
- Uma organização deseja retirar SSDs de operação contendo informações sensíveis. Qual avali
- Houve pedido cautelar de preservação de registros a um provedor. Quem examinou o caso afir
- O CPP, art. 158-B, apresenta dez etapas de cadeia de custódia, iniciadas por reconheciment
- Quando duas cópias apresentam o mesmo hash SHA-256, fica tecnicamente comprovado que a mes
- A memória RAM de equipamento ligado pode conter processos ativos e conexões transitórias q
- A cópia convencional de uma pasta contendo arquivos visíveis equivale necessariamente a um
- A Master File Table do NTFS armazena registros e atributos associados aos arquivos, podend
- O file carving recupera arquivos apagados somente quando há uma entrada MFT íntegra com o 
- Slack space corresponde a espaço residual dentro de uma unidade de alocação atribuída a ar
- Em sistemas Unix e ext, o campo ctime indica necessariamente a data original de criação do
- Um evento Security 4624 registra logon bem-sucedido no computador de destino, mas não comp
- O evento Security 4688 é registrado em qualquer instalação de Windows para todos os proces
- O Marco Civil diferencia guarda de registros de conexão por um ano, no art. 13, e guarda d
- A ata notarial de conteúdo eletrônico prevista no CPC, art. 384, autentica de modo definit
- O evento Security 1102 documenta limpeza do log de auditoria do Windows; sua ocorrência de
- A presença de criptografia de disco em uma mídia comprova tentativa deliberada de destruir
- A orientação NIST SP 800-88 Revisão 2, de 2025, trata sanitização como processo de tornar 
- A desmagnetização por degauss é método universal de sanitização de SSD, pois memória flash
- Um conjunto de logs exportado por provedor de nuvem pode constituir aquisição lógica com e
- Toda falha documental constatada na cadeia de custódia determina, por resultado técnico de
- O art. 10, § 3º, do Marco Civil ressalva a obtenção legalmente prevista de certos dados ca
- Na linha do tempo de eventos, a ausência de determinado registro no canal Security basta p
```

## Pedido 20: Dissídios coletivos e sentença normativa (`processo-trabalho.dissidio-coletivo`, 69 tópicos de edital)

```
Matéria: processo-trabalho.dissidio-coletivo (Dissídios coletivos e sentença normativa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conflitos coletivos, espécies, competência e legitimidade": 17
- "2. Comum acordo, poder normativo e instauração do procedimento": 12
- "3. Sentença normativa, vigência e extensão de seus efeitos": 30
- "4. Ação de cumprimento e revisão da sentença normativa": 29
- "5. Recurso ordinário, efeito suspensivo e integração dos institutos": 12

Já existem (não repita):
- Uma representação coletiva pede que sejam fixadas novas condições de reajuste e benefício 
- Depois de negociações frustradas, uma entidade representativa pretende instaurar dissídio 
- Há controvérsia coletiva sobre o significado de cláusula normativa já vigente, sem pedido 
- Um tribunal regional julga representação destinada a estabelecer novas condições coletivas
- Ao fixar condições em sentença normativa, o tribunal define o período de sua vigência. Qua
- A sentença normativa já estabeleceu adicional coletivo, mas determinado empregador não o p
- Uma sentença normativa vigora há dezoito meses e as circunstâncias econômicas que motivara
- Há recurso ordinário contra sentença normativa regional, mas ainda não foi concedido efeit
- Representantes empresariais faltam reiteradamente, sem justificativa, às reuniões coletiva
- Uma representação regular sobre conflito coletivo foi recebida e protocolada. Segundo a re
- Um dissídio econômico inclui apenas uma fração dos empregados de uma empresa. O tribunal p
- Busca-se ampliar decisão sobre novas condições a toda a categoria profissional dentro da j
- Após julgamento de dissídio coletivo regional, a parte pretende exigir cláusula normativa,
- Foi concedido efeito suspensivo ao recurso ordinário contra sentença normativa e publicada
- Uma decisão normativa está vigente há dezesseis meses, e mudança superveniente tornou cond
- A decisão normativa regional foi julgada, mas seu acórdão não havia sido publicado nos vin
- Em determinado estabelecimento, não existe sindicato que represente a categoria profission
- Há greve em atividade essencial com possibilidade concreta de lesão ao interesse público. 
- Uma decisão sobre condições de trabalho deve ser estendida a toda categoria no território 
- O recurso ordinário questiona somente a cláusula de auxílio prevista em sentença normativa
- Uma controvérsia sobre novas condições coletivas abrange estabelecimentos em áreas submeti
- Uma parte interpôs recurso ordinário baseado em certidão porque o acórdão regional não sai
- O efeito suspensivo de sentença normativa foi deferido e publicado, mas o recurso ordinári
- Uma sentença normativa foi proferida diretamente no âmbito originário da seção normativa s
- Uma parte requer efeito suspensivo ao recurso ordinário de sentença normativa e questiona 
- Uma categoria solicita a criação de nova condição coletiva de trabalho, e a ação possui na
- Uma associação de trabalhadores pede a interpretação de cláusula coletiva existente, mas a
- Um tribunal regional estabelece, em sentença normativa, condições gerais para a categoria 
- Ao fixar a vigência da sentença normativa, um tribunal declara que o art. 868, parágrafo ú
- O empregador deixa de cumprir cláusula salarial fixada em decisão coletiva. A ação de cump
- Um recurso ordinário foi interposto contra sentença normativa regional, e a simples interp
- Uma sentença normativa vigora há dezoito meses e fatos econômicos supervenientes tornaram 
- Um empregador invoca a ausência de anuência expressa do outro lado para impedir o dissídio
- Ao organizar representação coletiva, um grupo sem sindicato profissional verifica que o ar
- Uma decisão normativa abrange apenas parte dos empregados de uma empresa. Pelo art. 868, o
- Pretende-se estender nova condição de trabalho a toda a categoria da jurisdição. O art. 87
- Uma sentença normativa regional já foi julgada, e passaram-se vinte dias sem trânsito em j
- Publicado o deferimento de efeito suspensivo a recurso ordinário de sentença normativa, se
- No procedimento de revisão previsto nos arts. 873 a 875 da CLT, o juízo singular responsáv
- O recurso contra sentença normativa regional recebeu efeito suspensivo limitado à cláusula
- Publicado o ato de concessão de efeito suspensivo contra sentença normativa, o art. 9º da 
- Uma sentença normativa é julgada pela seção normativa superior em competência originária, 
- Segundo o IRDR 1 julgado em 2025, uma única convocação negocial não respondida é, independ
- Uma entidade sindical ajuíza revisão de sentença normativa depois de um ano e meio e demon
- Se o acórdão regional não for publicado nos vinte dias após julgamento, a lei autoriza rec
```

## Pedido 21: Democratização brasileira, justiça de transição e responsabilidade do Estado em direitos humanos (`direito.democratizacao-responsabilidade-estado-direitos-humanos`, 69 tópicos de edital)

```
Matéria: direito.democratizacao-responsabilidade-estado-direitos-humanos (Democratização brasileira, justiça de transição e responsabilidade do Estado em direitos humanos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Regime de 1964, abertura e transição democrática": 20
- "2. Justiça de transição: memória, verdade e reparação": 20
- "3. Responsabilidade civil do Estado por violações": 20
- "4. Responsabilidade internacional do Brasil": 20
- "5. Federalização de graves violações (IDC)": 20

Já existem (não repita):
- O Ato Institucional nº 5, de dezembro de 1968, ficou marcado, entre outras medidas, por:
- A Lei nº 6.683/1979, conhecida como Lei da Anistia, foi sancionada no contexto da abertura
- A campanha das Diretas Já, em 1984, resultou na aprovação da emenda que restabeleceu imedi
- A expressão "abertura lenta, gradual e segura" está associada ao processo de distensão pol
- Em resposta ao passado autoritário, a Constituição de 1988 estabelece que a tortura é crim
- A Constituição de 1988 considera inafiançável e imprescritível a ação de grupos armados co
- Após a redemocratização, o Brasil ratificou a Convenção Americana sobre Direitos Humanos e
- A Constituição de 1988 adota a prevalência dos direitos humanos como princípio que rege o 
- São eixos tradicionalmente apontados da justiça de transição:
- A Lei nº 9.140/1995 reconheceu como mortas pessoas desaparecidas em razão de participação 
- A Comissão Nacional da Verdade, criada pela Lei nº 12.528/2011:
- No julgamento da ADPF 153, em 2010, o Supremo Tribunal Federal decidiu que a Lei da Anisti
- No caso Gomes Lund e outros (Guerrilha do Araguaia) vs. Brasil, a Corte Interamericana con
- No caso Herzog e outros vs. Brasil (2018), a Corte Interamericana qualificou a tortura e m
- O eixo "Direito à Memória e à Verdade" integra o terceiro Programa Nacional de Direitos Hu
- A Comissão Nacional da Verdade tinha competência para aplicar sanções penais aos agentes r
- Nos termos do art. 37, § 6º, da Constituição, a responsabilidade civil do Estado pelos dan
- Pela teoria do risco administrativo, a responsabilidade objetiva do Estado admite excluden
- Quanto às pessoas sob custódia estatal, o STF reconhece que o Estado:
- Na ADPF 347, o STF reconheceu o estado de coisas inconstitucional do sistema prisional bra
- A vítima de dano causado por policial no exercício da função deve, em regra, propor a ação
- Perante o direito internacional dos direitos humanos, o Brasil pode afastar sua responsabi
- A primeira condenação do Brasil pela Corte Interamericana de Direitos Humanos ocorreu no c
- No caso Maria da Penha, a responsabilização do Brasil por negligência e tolerância à violê
- Para que um caso seja levado à Comissão Interamericana, exige-se, em regra:
- No caso Favela Nova Brasília vs. Brasil (2017), a Corte Interamericana tratou de:
- As garantias de não repetição, como capacitação de agentes e mudanças legislativas, integr
- O incidente de deslocamento de competência (IDC) para a Justiça Federal, nas hipóteses de 
- Para o STJ, a simples gravidade do crime contra os direitos humanos basta para deferir o i
- O primeiro incidente de deslocamento de competência deferido pelo STJ referiu-se ao assass
- O incidente de deslocamento de competência foi introduzido na Constituição pela Emenda Con
- A finalidade do incidente de deslocamento de competência, segundo o texto constitucional, 
- A Lei de Tortura (Lei nº 9.455/1997), editada após a redemocratização, integra o conjunto 
- A Lei nº 10.559/2002 regulamentou:
- A promulgação da Constituição de 1988 é considerada o marco jurídico da transição democrát
- O contraste entre a ADPF 153 e o caso Gomes Lund é frequentemente citado como exemplo da t
- A Constituição de 1988 foi promulgada em:
- O período de investigação atribuído à Comissão Nacional da Verdade pela Lei nº 12.528/2011
- O instrumento constitucional que permite deslocar para a Justiça Federal a investigação de
- A Lei da Anistia de 1979 é considerada por parte da doutrina um marco da transição negocia
```

## Pedido 22: Cinemática: descrição dos movimentos (`fisica.cinematica`, 69 tópicos de edital)

```
Matéria: fisica.cinematica (Cinemática: descrição dos movimentos)
Capítulos e quantas questões quero em cada um (total 100):
- "Referencial, deslocamento e velocidade": 28
- "Movimento uniforme e movimento variado": 22
- "Queda livre e lançamentos": 22
- "Vetores e movimento circular": 28

Já existem (não repita):
- Um móvel passa da posição x=40 m para x=180 m em uma linha orientada. Qual é seu deslocame
- Uma pessoa percorre 300 m em 60 s, sempre no mesmo sentido. Qual é sua rapidez média?
- Um móvel em movimento uniforme parte de x₀=20 m com velocidade 6 m/s. Onde estará após 8 s
- A velocidade de um móvel aumenta de 0 para 20 m/s em 5 s, com aceleração constante. Qual é
- Um objeto cai do repouso por 45 m. Adote g=10 m/s² e despreze o ar. Qual é a rapidez ao at
- Um móvel tem velocidade inicial 4 m/s e aceleração constante 2 m/s² durante 3 s. Qual desl
- Um corredor percorre 120 m em 20 s, mantendo o sentido do movimento. Qual é a velocidade m
- A velocidade passa de 5 m/s para 25 m/s em 10 s. Supondo aceleração constante, qual é o va
- Um corpo cai do repouso de uma altura de 80 m. Adote g=10 m/s². Quanto tempo dura a queda?
- Um ponto completa 2 voltas por segundo em movimento circular uniforme. Qual é o período?
- Um ponto gira a 5 rad/s em uma circunferência de raio 2 m. Qual é sua rapidez tangencial?
- Uma partícula percorre uma circunferência de raio 10 m em 4 s por volta. Qual é sua rapide
- Um deslocamento tem componentes perpendiculares de 3 m no eixo x e 4 m no eixo y. Qual é s
- Um móvel parte do repouso com aceleração constante de 3 m/s². Qual será sua velocidade apó
- Um veículo mantém 72 km/h durante 20 minutos. Qual distância percorre nesse intervalo?
- Um automóvel percorre 60 km a 60 km/h e retorna os mesmos 60 km a 40 km/h. Qual é sua rapi
- Uma bola é lançada verticalmente para cima a 30 m/s. Adote g=10 m/s². Qual é sua velocidad
- Um corpo cai do repouso durante 3 s, com g=10 m/s². Qual é a distância percorrida e qual é
- Dois carros aproximam-se em sentidos opostos, com velocidades de 60 km/h e 80 km/h. Qual é
- Um movimento circular uniforme tem velocidade angular ω=4 rad/s. Qual é o período de uma v
- Uma pessoa caminha 80 m para leste e depois 30 m para oeste em linha reta. Qual é seu desl
- Um veículo percorre 150 km em 3 h sem mudar de sentido. Qual é sua rapidez média?
- Um móvel parte da posição −12 m e segue com velocidade constante de −3 m/s. Qual posição o
- A velocidade de um corredor muda de 2 m/s para 34 m/s em 4 s, com aceleração constante. Qu
- Um móvel tem velocidade inicial +10 m/s e aceleração constante −2 m/s². O que ocorre nos p
- Num gráfico velocidade-tempo, um trecho horizontal está em v = 5 m/s. Qual grandeza é nula
- Um objeto é lançado verticalmente para cima. No ponto mais alto, desprezando o ar, qual af
- Em um lançamento horizontal ideal, qual componente da velocidade permanece constante?
- Dois objetos são lançados horizontalmente da mesma altura e ao mesmo tempo, um com velocid
- Dois deslocamentos perpendiculares têm módulos 28,8 m e 38,4 m. Qual é aproximadamente o m
- No movimento circular periódico, que grandeza indica o tempo necessário para completar uma
- Mantendo o raio fixo, a rapidez de um corpo em movimento circular uniforme dobra. Como mud
- Um passageiro está sentado em um ônibus que segue em linha reta. Em relação ao próprio ôni
- Um móvel parte do repouso e tem aceleração constante de 4 m/s². Qual velocidade atinge apó
- Um corpo cai do repouso por 2 s sob gravidade aproximada de 10 m/s², sem resistência do ar
- Um ponto completa 5 voltas em 10 s. Qual é sua frequência média nesse intervalo?
- Em um gráfico posição-tempo, uma reta tem inclinação negativa constante. Como se caracteri
- Uma viagem tem deslocamento total igual a zero, embora o veículo tenha percorrido vários q
- No modelo de queda livre sem resistência do ar, dois objetos soltos da mesma altura ao mes
- Uma partícula move-se numa circunferência com rapidez constante. Qual explicação justifica
- A distância percorrida por um móvel pode ser maior que o módulo de seu deslocamento.
- Se o deslocamento total é nulo, então a distância percorrida também precisa ser nula.
- A velocidade média vetorial utiliza o deslocamento, e não necessariamente a distância tota
- Uma inclinação positiva no gráfico posição-tempo significa que a rapidez do móvel está aum
- No movimento uniforme retilíneo, a velocidade permanece constante ao longo do tempo.
- A aceleração positiva sempre faz o módulo da velocidade aumentar.
- A área algébrica sob o gráfico velocidade-tempo corresponde ao deslocamento no intervalo r
- Em movimento com aceleração constante, a velocidade varia linearmente com o tempo.
- No ponto mais alto de um lançamento vertical ideal, a aceleração gravitacional deixa de at
- Desprezada a resistência do ar, a componente horizontal de um lançamento horizontal perman
- No lançamento oblíquo ideal, a componente vertical da velocidade inicial não interfere no 
- Dois objetos lançados horizontalmente da mesma altura e ao mesmo tempo, com condições vert
- Grandezas vetoriais são descritas apenas por um valor numérico e uma unidade.
- A soma de dois vetores perpendiculares tem módulo igual à soma simples de seus módulos.
- No movimento circular uniforme, o módulo da velocidade é constante, mas seu vetor muda de 
- A aceleração centrípeta é paralela à velocidade tangencial em todo movimento circular unif
- Se a rapidez dobrar e o raio permanecer igual, a aceleração centrípeta fica duas vezes mai
- O período é o tempo necessário para completar um ciclo, e a frequência informa ciclos por 
- Um corpo pode estar em repouso em relação a um referencial e em movimento em relação a out
- A conversão de km/h para m/s é feita multiplicando o valor por 3,6.
```

## Pedido 23: Funções inorgânicas (`ciencias.funcoes-inorganicas`, 68 tópicos de edital)

```
Matéria: ciencias.funcoes-inorganicas (Funções inorgânicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Funções inorgânicas e modelos": 17
- "2. Ácidos": 17
- "3. Bases": 17
- "4. Sais e neutralização": 17
- "5. Óxidos e classificação": 16
- "6. Como o tema costuma ser cobrado": 16

Já existem (não repita):
- Qual íon caracteriza o aumento de acidez numa solução aquosa segundo Arrhenius?
- Um composto contém cátions Ca²⁺ e ânions Cl⁻. Qual é a fórmula mínima eletricamente neutra
- A reação HCl + NaOH pode ser representada, em proporção apropriada, formando água e cloret
- Em classificação usual, dióxido de carbono é um exemplo de
- Qual nome corresponde à substância H₂SO₄ em nomenclatura escolar corrente?
- Um óxido que reage tanto com ácidos quanto com bases fortes, em condições apropriadas, é c
- A amônia pode elevar a concentração de OH⁻ em água por reação com o solvente. No modelo de
- Um ácido cuja solução está diluída pode continuar sendo classificado como forte quando apr
- Para nomear um composto iônico formado pelo cátion potássio e pelo ânion nitrato, usa-se a
- Uma substância classificada como base de Arrhenius precisa necessariamente ter o grupo OH 
- No modelo de Arrhenius, uma substância é considerada ácida em água quando aumenta a concen
- Uma base de Arrhenius em água aumenta a concentração de
- A fórmula NaOH representa um hidróxido que, ao se dissociar em água, fornece íons
- Na reação simplificada entre íons H⁺ e OH⁻ em solução aquosa, o produto formado é
- O composto CO₂ é classificado, em abordagem introdutória, como
- Força de um ácido e concentração da solução descrevem a mesma propriedade e podem ser usad
- A neutralidade elétrica de uma fórmula de sal exige que as cargas dos cátions e ânions se 
- Todo composto cuja fórmula contém hidrogênio é automaticamente um ácido de Arrhenius.
- O nome escolar de KOH é hidróxido de potássio.
- Um indicador ácido-base pode apresentar cores diferentes conforme o meio e a faixa de tran
- A amônia pode aumentar OH⁻ em água ao reagir com moléculas do solvente, mesmo sem apresent
- Óxido de cálcio é exemplo usual de óxido básico e pode reagir com água formando hidróxido 
- A fórmula de um sal pode ser escrita sem considerar as cargas dos íons, pois qualquer prop
- O nome ácido clorídrico corresponde à solução aquosa de HCl no uso escolar comum.
- Um sal pode conservar hidrogênio em seu ânion sem que isso o transforme automaticamente nu
- Um óxido anfótero pode apresentar reação com ácidos e também com bases fortes em condições
- Neutralização é simplesmente diluir uma solução com água, sem que haja interação entre áci
- Um óxido de não metal pode ser classificado como ácido em abordagem introdutória, mas exis
- Em uma neutralização, a equação química precisa conservar a quantidade de cada tipo de áto
- Hidróxido de cálcio contém dois grupos hidróxido por fórmula, mas esse número não é por si
- Todo óxido formado por um metal reage rapidamente com água para produzir uma base forte e 
- HCl e NaOH em proporções apropriadas podem reagir formando água e um sal, segundo a neutra
- A força de um ácido é determinada exclusivamente pela massa da amostra colocada no recipie
- Na fórmula de um sal, as cargas de espécies poliatômicas precisam ser consideradas como un
- Um óxido neutro não apresenta o comportamento ácido-base simples atribuído a óxidos ácidos
- O grupo OH em uma fórmula garante que o composto seja uma base forte e totalmente dissocia
- Um indicador pode apresentar transição de cor num intervalo de pH, de modo que sua respost
- A fórmula mínima de um composto iônico pode ser verificada somando as cargas de todos os í
- Um óxido ácido pode reagir com uma base formando sal e água, sem que isso signifique que o
- Uma substância molecular pode atuar como base de Arrhenius em água se sua reação com o sol
- Neutralizar quantidades quaisquer de ácido e base sempre produz uma solução exatamente neu
- Nomenclatura de hidróxidos pode exigir indicar estado de oxidação quando um metal forma ma
- A classificação de um óxido como básico, ácido, anfótero ou neutro deve considerar comport
```

## Pedido 24: Tribunais Regionais Federais e juízes federais (`direito-constitucional.trf-juizes-federais`, 68 tópicos de edital)

```
Matéria: direito-constitucional.trf-juizes-federais (Tribunais Regionais Federais e juízes federais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura da Justiça Federal, TRFs, seções e comparação com Minas Gerais": 22
- "2. Competências originárias e recursais dos TRFs e limites militares": 19
- "3. Competências materiais dos juízes federais: art. 109, I a XI": 21
- "4. Juízes estaduais, competência delegada, TJMG e Justiça Militar": 17
- "5. Incidente de deslocamento em direitos humanos e revisão integrada": 21

Já existem (não repita):
- Ao elaborar um mapa dos órgãos previstos na Seção IV do capítulo do Poder Judiciário, um e
- Um decreto de nomeação de integrante de TRF deve observar o mínimo de membros e a faixa et
- Durante a composição de um tribunal regional federal, discute-se o requisito temporal das 
- Para aumentar o acesso à justiça numa região extensa, um TRF considera justiça itinerante 
- Em uma ação federal comum que tramita em Minas Gerais, o litigante pretende identificar o 
- Uma comarca de Minas Gerais reúne três municípios e funciona na Justiça Estadual. Segundo 
- Um segurado reside em comarca sem vara federal, situada a 85 km de município sede de vara 
- Uma infração é classificada pela lei penal como crime militar sujeito à Justiça Militar da
- Um habeas corpus questiona ato apontado como coator praticado por juiz federal de primeira
- Uma pessoa pretende impetrar mandado de segurança contra ato de autoridade administrativa 
- Um juiz de direito julga validamente, por competência federal delegada, ação previdenciári
- Duas apelações são interpostas em Belo Horizonte: uma decorre de sentença de vara federal,
- Uma autarquia federal integra o polo passivo de uma ação de acidente do trabalho. O demand
- Em Minas Gerais, ação contesta ato disciplinar aplicado a policial militar estadual. Qual 
- Uma pessoa domiciliada no Brasil propõe causa contra um Estado estrangeiro; em outra ação,
- Uma violação grave de direitos humanos gera risco de responsabilização internacional e inc
- Dois juízes federais vinculados ao mesmo tribunal regional federal divergem sobre quem dev
- O município A integra comarca mineira que compreende outros dois municípios. A área é tamb
- Em ação por benefício pecuniário previdenciário, a comarca de domicílio do segurado não po
- Uma infração penal praticada a bordo de aeronave é enquadrada em hipótese legal de crime m
- Uma publicação atribui as apelações federais de Minas ao TJMG porque sua sede coincide com
- O TRF competente prepara lista de comarcas aptas ao exercício de competência previdenciári
- Em imputação por crime comum, um juiz integrante da Justiça Militar federal está abrangido
- A União ajuíza ação como autora contra pessoa domiciliada em Minas; em caso distinto, pess
- Um homicídio de defensor de direitos humanos é grave, mas há investigação local efetiva e 
- O art. 106 da Constituição identifica os Tribunais Regionais Federais e os juízes federais
- Para nomeação a TRF, o art. 107 exige brasileiro com mais de 35 e menos de 65 anos, além d
- Em Minas Gerais, TJMG e TRF6 são tribunais distintos; o segundo integra a Justiça Federal 
- Mandado de segurança contra ato de juiz federal tem competência originária de outro juiz f
- Uma causa previdenciária pecuniária pode, nos critérios do art. 109, § 3º, e do art. 15, I
- A participação de sociedade de economia mista federal no processo equivale, para todos os 
- Nos crimes militares definidos em lei, a competência prevista para a Justiça Militar da Un
- A existência de justiça itinerante é faculdade dos TRFs prevista no art. 107, § 2º, enquan
- O art. 108, II, prevê competência recursal do TRF tanto para causas decididas por juízes f
- Em Minas Gerais, uma comarca estadual formada por dois municípios é automaticamente uma su
- A Lei 14.226/2021 criou o TRF6, sediado em Belo Horizonte e com jurisdição em todo o terri
- Na composição do TRF, o quinto constitucional do art. 107, I, pode ser preenchido por qual
- Entre as competências do art. 109, X, figuram causas relativas à nacionalidade e à natural
- Mesmo quando a comarca do segurado está a exatamente 70 km de município sede de vara feder
- O art. 109, § 5º, permite que o PGR suscite IDC perante o STJ durante o inquérito, e não a
- A ressalva da Justiça Militar no art. 109, IV, é afastada automaticamente sempre que a inf
- Quando a União é autora, o art. 109, § 1º, aponta a seção do domicílio da outra parte; qua
- Como TRF6 e TJMG possuem sede na capital mineira, a Constituição determina que as competên
- Pelo parágrafo único do art. 110, nos Territórios Federais, a jurisdição e as atribuições 
- A jurisprudência do STJ considera suficiente a existência de grave crime contra direitos h
```

## Pedido 25: Embargos à execução, impugnação e embargos de terceiro (`processo-trabalho.embargos-execucao`, 68 tópicos de edital)

```
Matéria: processo-trabalho.embargos-execucao (Embargos à execução, impugnação e embargos de terceiro)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Embargos à execução na CLT: garantia, prazo, matérias e exceções": 23
- "2. Impugnação dos cálculos e da sentença de liquidação: prazos e preclusão": 27
- "3. Embargos de terceiro: legitimidade, prazo, competência e procedimento": 18
- "4. Embargos de terceiro: defesa restrita, decisão, efeitos e conflitos": 17
- "5. Agravo de petição, delimitação e prosseguimento do incontroverso": 15

Já existem (não repita):
- Em execução trabalhista com patrimônio já penhorado, o executado pretende comprovar pagame
- Uma empresa executada não garantiu a execução e pretende apresentar embargos sem penhora. 
- Após elaboração da conta de liquidação, o juízo abre prazo às partes para apontar erros em
- Durante execução de uma dívida trabalhista, um imóvel de pessoa que não integra o processo
- Uma pessoa apresenta embargos de terceiro contra constrição sobre seu automóvel; a parte a
- O devedor agrava de decisão na execução trabalhista depois de ter seus embargos julgados. 
- O executado apresenta agravo de petição questionando uma única rubrica de R$ 12 mil da exe
- O juiz reconhece suficientemente a posse de terceiro sobre bem bloqueado e recebe seus emb
- Em incidente de embargos de terceiro, o pedido de liberação de bem é acolhido ao final. Qu
- Uma entidade filantrópica é executada e pretende opor embargos à execução sem ter feito de
- Na defesa do executado, uma testemunha foi arrolada e o juiz entendeu indispensável seu de
- O valor de uma conta de liquidação é contestado pelo exequente apenas com a frase 'há erro
- Uma conta calculou 18 meses de certa verba, embora a sentença a tenha reconhecido por apen
- Um cônjuge que não participou da execução trabalhista demonstra que a medida constritiva a
- Uma pessoa estranha ao processo identifica ameaça de penhora sobre imóvel que possui. Em q
- Embargos de terceiro são apresentados em execução depois de arrematação, transcorridos ape
- Uma constrição é ordenada na execução e atinge propriedade de não participante. Ao distrib
- Uma execução foi garantida e os cálculos foram impugnados no prazo comum do art. 879, §2º.
- Um terceiro titular de direito real de garantia não foi intimado da medida de expropriação
- Uma pessoa sofreu constrição por desconsideração da personalidade jurídica de cujo inciden
- Em embargos de terceiro, o bem foi objeto de ordem constritiva cumprida por carta, e o juí
- O terceiro demonstra posse suficiente e requer reintegração provisória, mas não pode supor
- Uma empresa executada impugna no agravo apenas R$ 25 mil de uma execução de R$ 90 mil, ide
- Um exequente interpõe agravo de petição exclusivamente para aumentar seu crédito reconheci
- O agravo de petição do executado não foi recebido, e ele apresenta agravo de instrumento p
- Depois de garantida a execução trabalhista, o devedor pode oferecer embargos em cinco dias
- Uma empresa executada poderá usar os embargos do art. 884 para rediscutir o valor do salár
- Com a redação atual do art. 879, §2º, da CLT, a conta de liquidação aberta à discussão dev
- Os embargos de terceiro dos arts. 674 a 681 do CPC somente podem ser ajuizados por pessoa 
- Havendo prova suficiente de posse de terceiro sobre bem constrito, o juiz pode aplicar a t
- O agravo de petição previsto no art. 897, a, da CLT é interposto em cinco dias, pois acomp
- O art. 884, §6º, dispensa entidades filantrópicas e dirigentes abrangidos da exigência de 
- Na liquidação trabalhista, o art. 879, §3º, concede às partes privadas dez dias sucessivos
- A sentença de liquidação pode ser impugnada pelo executado nos embargos à penhora, cabendo
- Um cônjuge que protege a própria meação de bem constrito em execução contra outra pessoa n
- Nos embargos de terceiro, o art. 677 do CPC exige prova sumária de posse ou domínio e da c
- O art. 680 do CPC autoriza o embargado, na demanda do credor com garantia real, a contesta
- O agravo de petição que identifica justificadamente rubrica e valor controvertidos permite
- Em execução, os embargos de terceiro podem ser apresentados até cinco dias depois da arrem
- Se a constrição foi praticada em cumprimento de carta e o deprecante já indicou o bem espe
- A tutela concedida com base no art. 678 do CPC cancela definitivamente a penhora e extingu
- No art. 677, §4º, do CPC, além do beneficiário do ato constritivo, também integra o polo p
- A exigência de delimitação dos valores no agravo de petição é aplicada uniformemente a tod
- O art. 884, §5º, da CLT prevê hipótese de inexigibilidade de título judicial fundado em no
- O agravo de instrumento contra a decisão que não recebeu agravo de petição suspende automa
```

## Pedido 26: Telessaúde, Telemedicina e Protocolos Clínicos e Diretrizes Terapêuticas (PCDT) (`medicina.telessaude-pcdt`, 68 tópicos de edital)

```
Matéria: medicina.telessaude-pcdt (Telessaúde, Telemedicina e Protocolos Clínicos e Diretrizes Terapêuticas (PCDT))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Telessaúde na Lei 8.080 (Lei 14.510/2022)": 34
- "2. Telemedicina: ética e prática (Resolução CFM 2.314/2022)": 33
- "3. PCDT na Lei 8.080 (Lei 12.401/2011)": 33

Já existem (não repita):
- A telessaúde, nos termos da Lei 8.080/1990 com a redação da Lei 14.510/2022, abrange a pre
- A Lei 14.510/2022, além de incluir a telessaúde na Lei Orgânica da Saúde:
- Entre os princípios da telessaúde, inclui-se o direito de o paciente recusar o atendimento
- Não é princípio da telessaúde previsto na Lei 8.080:
- Pela lei, o profissional de saúde tem liberdade e independência para decidir sobre utiliza
- Os atos do profissional de saúde praticados por telessaúde:
- A normatização ética relativa à prestação de serviços por telessaúde compete:
- Ato normativo que pretenda restringir a prestação de serviços de telessaúde deve demonstra
- No atendimento por telessaúde, aplicam-se as normas de proteção de dados pessoais e o deve
- O atendimento por telessaúde segue padrões normativos e éticos próprios, menos rigorosos q
- Na Resolução CFM 2.314/2022, a troca de informações e opiniões entre médicos, com ou sem a
- O telediagnóstico consiste na emissão de laudo ou parecer de exames por médico, a distânci
- O acompanhamento a distância de parâmetros de saúde ou doença de um paciente, por meio de 
- A telecirurgia é o procedimento cirúrgico realizado por equipamento robótico manipulado re
- Sobre o registro do atendimento por telemedicina, é correto afirmar que:
- Receitas, atestados e laudos emitidos por telemedicina devem permitir a identificação do m
- Na telemedicina, diante de um caso em que o exame físico seja indispensável, a conduta ade
- O atendimento por telemedicina dispensa o consentimento do paciente, por se tratar de moda
- A modalidade que avalia o paciente a distância para direcioná-lo ao tipo adequado de assis
- Na perícia médica, avaliações a distância ou por análise documental dependem de previsão n
- Na medicina do trabalho, o uso da telessaúde:
- O acompanhamento de doenças crônicas por telemedicina deve incluir consultas presenciais p
- Segundo a Lei 8.080/1990, com a redação da Lei 12.401/2011, o Protocolo Clínico e Diretriz
- Os critérios estabelecidos nos PCDTs devem ser seguidos pelos gestores do SUS.
- Os medicamentos ou produtos indicados nos PCDTs devem ser aqueles avaliados quanto a:
- Na falta de Protocolo Clínico e Diretriz Terapêutica para uma doença, a dispensação de med
- A incorporação, a exclusão ou a alteração de tecnologias no SUS e a constituição ou altera
- A Conitec decide de forma definitiva e vinculante a incorporação de medicamentos ao SUS, s
- O processo administrativo de incorporação de tecnologia no SUS deve ser concluído em prazo
- O relatório da Conitec deve considerar evidências científicas sobre eficácia, acurácia, ef
- É vedado, em todas as esferas de gestão do SUS:
- A assistência terapêutica integral no SUS inclui a dispensação de medicamentos cuja prescr
- A consulta pública é etapa do processo de incorporação de tecnologias e de elaboração ou a
- Sobre a atualização dos PCDTs, é correto afirmar que:
- Um paciente apresenta prescrição de medicamento sem registro na Anvisa e solicita sua disp
- A teleconsultoria, na Resolução CFM 2.314/2022, é a consultoria mediada por tecnologia ent
- A Lei 14.510/2022 permite que o paciente seja obrigado a aceitar o atendimento remoto quan
- Na Resolução CFM 2.314/2022, a consulta médica remota, com médico e paciente em locais dif
- O órgão que assessora o Ministério da Saúde na incorporação de tecnologias e na constituiç
- A telessaúde, na Lei 8.080, é exercida observando-se, entre outros princípios:
```

## Pedido 27: Cálculos trabalhistas (`direito-trabalho.calculos-trabalhistas`, 68 tópicos de edital)

```
Matéria: direito-trabalho.calculos-trabalhistas (Cálculos trabalhistas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fontes, princípios, parcelas salariais e método de cálculo": 11
- "2. Jornada, horas extras, trabalho noturno e repouso semanal": 10
- "3. Férias, décimo terceiro, rescisão e FGTS": 10
- "4. Teletrabalho e impactos sobre jornada, contratos e despesas": 11
- "5. Súmulas, orientações jurisprudenciais e precedentes constitucionais": 58

Já existem (não repita):
- Uma pessoa recebe R$ 2.200 mensais, trabalha 44 horas semanais e presta quatro horas extra
- Em contrato de 40 horas semanais, sem regime especial de categoria e salário mensal de R$ 
- O trabalho urbano foi prestado integralmente das 22h às 5h, com 420 minutos reais. Para ap
- Uma pessoa possui remuneração-base de R$ 3.000 para férias integrais de trinta dias, sem p
- Um empregado trabalhou nove meses integrais de determinado ano e tinha remuneração-base de
- Uma remuneração mensal com incidência integral de FGTS soma R$ 2.500 e não há outras parce
- O contrato termina por dispensa imotivada, e a base atualizada legalmente considerada para
- Uma pessoa exerce trabalho remoto sob registro efetivo de jornada. Diante do art. 62, III,
- Para cálculo de oito horas extras a 50%, o salário-base é R$ 2.200 e o adicional fixo sala
- Em determinado mês, horas extras habituais pagas totalizaram R$ 500; o caso informa 25 dia
- Um trabalhador urbano prestou oito horas noturnas fictas, cujo salário-hora diurno é R$ 15
- Um empregado tem remuneração de férias proporcionais de R$ 2.400 como base e completou set
- Uma admissão ocorreu em 17 de agosto e o emprego permaneceu até 31 de dezembro do mesmo an
- No desligamento por acordo do art. 484-A da CLT, o aviso indenizado integral seria R$ 3.00
- Após a Reforma Trabalhista, a empresa paga mensalmente comissão por vendas ordinárias e pr
- Uma trabalhadora passou do teletrabalho para presencial por determinação patronal. O empre
- Em reclamação sobre férias gozadas no prazo concessivo, o pagamento ocorreu depois do limi
- Um cálculo de 2025 considera horas extras habituais, o DSR aumentado por elas e seus refle
- Em ação relativa à vantagem salarial, pretende-se usar o salário mínimo como indexador e s
- Um empregado em teletrabalho por produção recebe R$ 230 de reembolso comprovadamente devid
- Uma pessoa admitida em 18 de agosto trabalhou sem afastamentos até dezembro. A base de cál
- Após cinco anos completos de vínculo, uma dispensa imotivada exige aviso-prévio proporcion
- Um julgamento sobre contribuições sociais relacionadas à ação trabalhista discute execução
- Um contrato remoto alterna comparecimento presencial habitual para reuniões e execução ext
- Após a extinção do contrato, um pedido de recolhimento de depósitos de FGTS jamais realiza
- Empregado mensalista de 44 horas semanais com salário de R$ 2.200 tem hora normal de R$ 10
- O adicional noturno urbano de pelo menos 20% substitui a hora reduzida de 52 minutos e 30 
- Com remuneração mensal de R$ 3.000 e férias integrais de 30 dias, o valor bruto das férias
- Para empregado admitido em 18 de agosto, os 14 dias trabalhados nesse mês são suficientes 
- No regime de depósitos mensais gerais do FGTS, a alíquota é de 8% sobre a remuneração enqu
- Em regra, o teletrabalhador que executa serviços por jornada controlável está excluído do 
- As horas extras habitualmente prestadas integram o cálculo da remuneração do repouso seman
- A Súmula 264 do TST determina calcular a hora extra sempre sobre o salário-base isolado, m
- Com o art. 457, §§ 2º e 4º, da CLT, prêmio genuíno concedido por desempenho superior ao or
- No pedido de demissão antes de completar doze meses de contrato, férias proporcionais são 
- No acordo de extinção previsto no art. 484-A da CLT, as demais verbas trabalhistas permane
- A passagem do regime presencial para teletrabalho pode ser imposta unilateralmente pelo em
- Para empregado em teletrabalho, as disposições contratuais sobre equipamentos e reembolso 
- O art. 74, § 2º, da CLT continua exigindo controle de jornada em estabelecimentos com mais
- A OJ 394 da SDI-1, no entendimento alterado em 2023, admite repercussão do DSR majorado po
- Como a Resolução TST nº 225/2025 não afetou enunciados relativos a férias, a Súmula 450 pe
- A Súmula Vinculante 22 reconhece competência da Justiça do Trabalho para indenizações por 
- A Súmula Vinculante 53 autoriza execução de ofício de contribuições previdenciárias em qua
- A Súmula Vinculante 1 impede desconsiderar, sem exame das circunstâncias concretas, a vali
- A Súmula Vinculante 4 autoriza o julgador, sempre que reputar inadequado o salário mínimo 
```

## Pedido 28: Legislação dos bombeiros e policiais militares do DF (`legislacao.bombeiros-pm-df`, 68 tópicos de edital)

```
Matéria: legislacao.bombeiros-pm-df (Legislação dos bombeiros e policiais militares do DF)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estatuto e natureza jurídica do bombeiro militar distrital": 18
- "2. Deveres, ética, cargos e prerrogativas estatutárias": 18
- "3. Organização básica: Lei nº 8.255/1991 e Decreto nº 7.163/2010": 34
- "4. Estrutura distrital: Decreto nº 31.817/2010 e mudanças de 2026": 19
- "5. Efetivo, ingresso, promoções e remuneração dos militares do DF": 11

Já existem (não repita):
- Um relatório institucional afirma que o CBMDF é organizado e mantido pela União e pergunta
- Dois militares ingressam, respectivamente, como oficial e praça. A administração precisa i
- Durante a organização de uma operação de emergência, uma unidade planeja estratégias, outr
- Uma proposta de reorganização trata do Comando-Geral e dos órgãos de direção-geral e setor
- Ao distribuir tarefas operacionais, identifica-se o órgão de execução de mais alto escalão
- Uma comissão analisa os critérios legais para promover bombeiros militares do DF. Qual opç
- Um militar do DF consulta a Lei nº 10.486/2002 para saber de que parcelas se compõe a remu
- Um militar da ativa mantém quotas de sociedade limitada, sem participar da gerência. O que
- O ato de lotação atribui a um militar obrigações próprias de posição prevista na estrutura
- Em 2026, a administração pretende regulamentar novas unidades de execução operacional, sem
- Uma equipe está revisando a classificação das diretorias no organograma regulado pelo Decr
- Após a atualização do Decreto nº 31.817/2010 em julho de 2026, uma chefia precisa apontar 
- A unidade responsável por estudos de política, estratégia e doutrina era situada em uma es
- O Comandante-Geral pretende distribuir efetivo entre organizações operacionais e administr
- Um bombeiro pratica ato extraordinário de coragem em missão e tem o mérito reconhecido em 
- Um militar de carreira solicita inclusão em Quadro de Acesso apesar de estar em licença pa
- A Lei nº 10.486/2002 enumera auxílio-moradia e auxílio-fardamento e, separadamente, define
- A legislação de 2026 acrescentou atribuições ambientais ao CBMDF. Em análise de possível a
- Após as mudanças distritais de 2026, um plano de treinamento prevê atuação de centro subor
- Uma ocorrência de grande complexidade demanda coordenação temporária, sem necessidade de c
- Um relatório soma os 9.703 militares do efetivo de carreira do art. 65 da Lei nº 12.086/20
- Em 2026, uma leitura do art. 69 da Lei nº 12.086/2009 identifica inciso V acrescido pela L
- Um bombeiro foi preterido em promoção e teve reconhecido, em recurso favorável, o direito 
- Uma planilha remuneratória de 2026 incorpora o adicional de tempo de serviço como percentu
- Em uma revisão da Lei nº 8.255/1991, uma equipe atribui ao Alto Comando deliberação admini
- O CBMDF é organizado e mantido pela União, mas subordinado ao Governador do DF, e sua cond
- No ato de conceder grau hierárquico a uma praça, o Comandante-Geral deve expedir Carta Pat
- Uma seção que mantém equipamentos e viaturas atende a necessidades materiais da corporação
- O art. 10-B da Lei nº 8.255/1991 atribui ao Governador do DF competência regulamentar sobr
- A Lei nº 12.086/2009 prevê, para o CBMDF, critérios de promoção por antiguidade, merecimen
- No art. 1º da Lei nº 10.486/2002, auxílio-moradia e auxílio-fardamento aparecem como espéc
- Dois militares em atividade têm o mesmo posto, mas datas distintas de promoção. A precedên
- Um militar da ativa, por ser quotista de empresa limitada, pode assumir sua gerência sem r
- A distinção normativa entre o Decreto federal nº 7.163/2010 e o Decreto distrital nº 31.81
- Após a alteração de 2026, o Centro de Estudos de Política, Estratégia e Doutrina permanece
- Um militar de carreira que atingiu requisitos de antiguidade, mas está de licença para int
- A Lei nº 15.395/2026 revogou o art. 62 da Lei nº 10.486/2002 e restabeleceu a aquisição de
- Um bombeiro é designado para incumbência temporária não catalogada como cargo. O Estatuto 
- O art. 65 da Lei nº 12.086/2009 fixa 9.703 bombeiros militares de carreira, e o parágrafo 
- Um bombeiro praticou ato extraordinário relevante e foi promovido por bravura após apuraçã
- Na versão vigente em outubro de 2026 do Decreto nº 31.817/2010, o Centro de Treinamento Op
- Para uma ocorrência excepcional, o art. 21, § 7º, do Decreto nº 31.817/2010 admite instala
- Segundo o art. 10-A, § 4º, da Lei nº 8.255/1991, a substituição eventual do Comandante-Ger
- O parágrafo único do art. 8º-A da Lei nº 8.255/1991 atribui ao Governador a regulamentação
- Por instituir reajustes para 2026, a Lei nº 15.395/2026 passou a tratar o auxílio-moradia 
```

## Pedido 29: Vegetação (`minas-gerais.vegetacao`, 68 tópicos de edital)

```
Matéria: minas-gerais.vegetacao (Vegetação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Biomas e fitogeografia de Minas Gerais": 25
- "2. Cerrado, Mata Atlântica e vegetação de altitude": 25
- "3. Fragmentação, conservação e restauração ambiental": 25
- "4. Reescrita e como o tema Vegetação é cobrado": 25

Já existem (não repita):
- Em uma leitura da vegetação mineira, qual enunciado diferencia corretamente bioma de fitof
- Qual síntese corresponde melhor à distribuição vegetal de Minas Gerais?
- Ao representar a transição entre Cerrado e Mata Atlântica, qual interpretação é adequada?
- Qual característica ajuda a reconhecer uma formação savânica do Cerrado sem confundi-la co
- Em um campo rupestre de serra, qual conjunto de condições é compatível com a vegetação des
- Uma vereda é identificada em paisagem de Cerrado. Qual descrição evita uma generalização i
- Qual relação entre corredor vegetado e remanescentes está correta?
- Ao comparar reflorestamento e restauração ecológica, qual afirmação é a mais precisa?
- Uma área muito alterada perdeu fontes próximas de sementes e sofreu compactação do solo. Q
- Em uma área de campo nativo, uma proposta prevê plantio denso de árvores para elevar a cob
- Uma mancha de mata cresce perto de uma área aberta na zona de contato entre biomas. Qual i
- Uma mata ripária é removida e a margem passa a receber mais sedimentos. Qual interpretação
- Em um projeto para área degradada, há remanescentes próximos e banco de sementes no solo, 
- Qual reescrita preserva a ideia de que mapas sintetizam limites segundo escala, enquanto t
- Uma equipe encontra espécies de campo e de mata em manchas próximas, em relevo variado. Qu
- Considere uma encosta serrana com rocha exposta, solo raso e forte variação entre faces úm
- Um remanescente alongado é cercado por lavoura e sofre vento, calor e entrada de espécies 
- Uma intervenção é proposta em remanescente campestre alterado por máquinas, com espécies n
- Uma estrada nova aproxima visualmente dois fragmentos, mas remove a faixa de vegetação que
- Ao recuperar uma área de vereda drenada, qual medida conceitual evita escolher uma interve
- Uma propriedade contém fragmentos florestais, campo nativo e uma nascente. Qual plano arti
- Uma questão reescreve a frase «a restauração considera o ecossistema de referência» como «
- Em um ecótono, observam-se espécies associadas a formações vizinhas. Qual alternativa expl
- Qual opção associa corretamente a proteção de remanescentes e a recuperação de área degrad
- Uma encosta com mata estacional, campo de altitude e brejo é descrita em mapa municipal. Q
- Um programa escolhe regeneração natural em área próxima a fragmento conservado, com solo p
- Uma questão troca a expressão «campo de altitude pode ocorrer em mosaico com matas» por «c
- Bioma e fitofisionomia são conceitos relacionados, mas descrevem escalas e aspectos difere
- Toda formação do Cerrado em Minas Gerais é uma floresta densa com dossel fechado.
- Qualquer terreno elevado de Minas deve ser classificado como campo rupestre, independentem
- A fragmentação pode afetar conectividade e bordas, mas seus efeitos variam conforme a pais
- Reflorestamento e restauração ecológica significam sempre plantar árvores até formar uma f
- Matas ripárias podem proteger margens e contribuir para a conexão entre habitats terrestre
- Todo fogo é benéfico ao Cerrado e deve ser frequente para manter qualquer formação vegetal
- Corredores vegetados eliminam todas as pressões e garantem a sobrevivência de cada espécie
- A expansão agrícola e urbana pode reduzir remanescentes e alterar a conectividade da veget
- A regeneração natural é sempre preferível, mesmo sem sementes próximas e com pressões que 
- Limites cartográficos entre biomas podem representar padrões regionais sem excluir transiç
- A fragmentação por uma estrada só afeta a vegetação se todas as espécies do remanescente f
- A restauração ecológica deve considerar a fitofisionomia de referência e as condições que 
- Campos nativos são necessariamente áreas degradadas e devem ser convertidos em matas para 
- Em campos rupestres, perturbações aparentemente pequenas podem afetar populações de espéci
```

## Pedido 30: Eletrostática (`fisica.eletrostatica`, 67 tópicos de edital)

```
Matéria: fisica.eletrostatica (Eletrostática)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Carga elétrica, materiais e processos de eletrização": 16
- "2. Lei de Coulomb, superposição e campo elétrico": 27
- "3. Fluxo, Lei de Gauss, simetria e condutores": 19
- "4. Potencial elétrico, trabalho e energia potencial": 24
- "5. Dielétricos, condições de contorno e capacitores ideais": 14

Já existem (não repita):
- Uma esfera metálica neutra perde quatro elétrons durante uma eletrização. Qual interpretaç
- Duas pequenas esferas condutoras idênticas e isoladas apresentam cargas de +10 nC e -2 nC.
- Um bastão negativo aproxima-se de uma esfera metálica neutra apoiada em isolante, sem enco
- Duas cargas puntiformes fixas interagem no vácuo. Mantendo os valores das cargas, a distân
- Num ponto há campo elétrico uniforme de 200 N/C orientado para a direita. Uma partícula co
- Duas cargas positivas iguais ocupam as extremidades de um segmento, sem outras cargas próx
- Uma superfície gaussiana fechada contém carga líquida de +6 nC. O que determina a Lei de G
- Um bloco metálico isolado alcança equilíbrio eletrostático após receber carga. Qual propri
- Uma carga pontual positiva produz potencial V em certo ponto, com referência nula no infin
- Um capacitor ideal de placas paralelas tem área A e separação d, no vácuo. Se a área dobra
- Um bastão negativamente carregado é mantido próximo de esfera condutora inicialmente neutr
- Duas forças elétricas simultâneas de 3 N para leste e de 4 N para norte atuam sobre uma me
- Em torno de um fio retilíneo idealmente infinito, com densidade linear de carga uniforme, 
- Compare o módulo do campo de uma folha isolante infinita uniformemente carregada ao campo 
- Uma esfera maciça isolante, de raio R, possui carga volumétrica positiva uniforme. Compara
- Uma partícula de carga -2 μC desloca-se de A, a 10 V, até B, a 30 V, sob ação eletrostátic
- Cargas pontuais +Q e -Q de mesmo módulo são colocadas simetricamente em torno do ponto P. 
- Uma distribuição de cargas livres é cercada por uma superfície fechada dentro de dielétric
- Dois dielétricos lineares compartilham interface plana sem carga livre superficial. Os cam
- Um capacitor de placas paralelas recebe carga, é desconectado da fonte e preenchido integr
- Uma carga pontual +q é introduzida numa cavidade interna de condutor metálico inicialmente
- Um material dielétrico responde de modo não linear e anisotrópico ao campo aplicado. Qual 
- Uma única carga pontual +Q está dentro de uma esfera gaussiana fechada, mas fora de seu ce
- Um capacitor ideal de placas paralelas permanece ligado a uma fonte de tensão constante. U
- Uma carga +Q está em x=-a e outra carga -Q em x=+a. No ponto x=0, coloca-se uma pequena ca
- Duas peças inicialmente neutras, isoladas do ambiente e atritadas entre si, adquirem carga
- Uma esfera neutra é atraída por um bastão carregado. Disso se conclui obrigatoriamente que
- Duas cargas pontuais mantêm distância fixa no vácuo. Se a intensidade de uma delas dobrar 
- O vetor campo produzido por uma carga pontual negativa aponta radialmente para fora dessa 
- O potencial produzido por várias cargas pontuais em um mesmo ponto é a soma algébrica de s
- Na eletrização por indução com um bastão negativo, se o bastão for afastado enquanto o con
- No ponto médio entre cargas +Q e -Q de módulos iguais, os campos individuais têm mesmo sen
- Uma superfície gaussiana fechada envolve carga líquida nula. Consequentemente, o campo elé
- Um condutor carregado que atingiu equilíbrio eletrostático pode apresentar potencial não n
- No campo eletrostático de cargas fixas, o trabalho realizado pela força elétrica para leva
- Uma gaussiana fechada envolve duas cargas, +3 nC e -3 nC. Independentemente das posições i
- No interior de uma esfera isolante maciça, de densidade volumétrica de carga uniforme, o c
- Para um fio ideal infinitamente longo com densidade linear de carga uniforme, uma superfíc
- Uma casca condutora isolada, inicialmente com carga total +2q, recebe uma carga -q numa ca
- Em uma interface eletrostática entre dielétricos sem carga livre superficial, a componente
- Entre dois dielétricos sem carga livre na interface, o campo tangencial necessariamente de
- Ao preencher integralmente com dielétrico linear um capacitor ideal mantido sob tensão con
- Ao inserir dielétrico que triplica a capacitância de um capacitor ideal isolado após ser c
- No ponto médio entre duas cargas pontuais iguais e positivas, o campo elétrico pode ser ze
- Um condutor metálico em equilíbrio eletrostático, carregado positivamente e isolado no vác
```

## Pedido 31: Proteção a vítimas, testemunhas e réus colaboradores (Lei 9.807/1999) (`legislacao-penal-especial.protecao-vitimas-testemunhas-lei-9807`, 67 tópicos de edital)

```
Matéria: legislacao-penal-especial.protecao-vitimas-testemunhas-lei-9807 (Proteção a vítimas, testemunhas e réus colaboradores (Lei 9.807/1999))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Programas de proteção: destinatários, critérios e estrutura": 31
- "2. Ingresso, medidas de proteção, alteração de nome, exclusão e duração": 31
- "3. Proteção aos réus colaboradores e disposições gerais": 38

Já existem (não repita):
- A Lei 9.807/1999 prevê medidas de proteção a vítimas e testemunhas de crimes que estejam c
- Segundo a Lei 9.807/1999, as medidas de proteção serão prestadas, na forma de programas es
- Os programas de proteção podem ser realizados mediante convênios, acordos, ajustes ou term
- NÃO é critério expressamente considerado pela Lei 9.807/1999 para conceder a proteção:
- A proteção pode ser estendida ao cônjuge ou companheiro, ascendentes, descendentes e depen
- Estão excluídos dos programas de proteção da Lei 9.807/1999:
- O ingresso no programa de proteção pode ser determinado pelo juiz independentemente da con
- Toda admissão no programa ou exclusão dele, segundo a Lei 9.807/1999, deve ser:
- Cada programa de proteção é dirigido por um conselho deliberativo, em cuja composição have
- As deliberações do conselho deliberativo do programa de proteção são tomadas por:
- As medidas e providências relacionadas aos programas de proteção serão adotadas, executada
- A Lei 9.807/1999 instituiu, no âmbito do Ministério da Justiça, o Programa Federal de Assi
- NÃO tem legitimidade para encaminhar ao órgão executor a solicitação de ingresso no progra
- Em caso de urgência, a vítima ou testemunha pode ser colocada provisoriamente sob a custód
- Para instruir o pedido de ingresso, o órgão executor pode solicitar documentos e exames so
- Entre as medidas de proteção previstas na Lei 9.807/1999, está:
- Quando a pessoa protegida for servidor público ou militar, a Lei 9.807/1999 admite a suspe
- A ajuda financeira mensal prevista na Lei 9.807/1999:
- Quando entender necessário, o conselho deliberativo pode solicitar ao Ministério Público q
- A alteração do nome completo da pessoa protegida, em casos excepcionais, é feita mediante 
- Concedida a alteração de nome completo, o juiz determinará a averbação no registro origina
- A alteração de nome completo prevista na Lei 9.807/1999 pode estender-se aos familiares pr
- Cessada a coação ou ameaça que deu causa à alteração do nome, o protegido:
- A exclusão da pessoa protegida do programa pode ocorrer a qualquer tempo, por solicitação 
- A proteção oferecida pelos programas da Lei 9.807/1999 terá duração máxima de:
- Uma testemunha ameaçada que já cumpre pena por outro crime pode ingressar no programa de p
- Segundo o art. 13 da Lei 9.807/1999, o perdão judicial pode ser concedido ao acusado que c
- O perdão judicial do art. 13 da Lei 9.807/1999 pode ser concedido pelo juiz de ofício ou a
- Conforme o art. 14 da Lei 9.807/1999, o indiciado ou acusado que colaborar voluntariamente
- Diferentemente do perdão judicial do art. 13, a redução de pena do art. 14 da Lei 9.807/19
- NÃO figura entre os resultados da colaboração mencionados no art. 13 da Lei 9.807/1999 par
- Estando sob prisão temporária, preventiva ou em decorrência de flagrante, o colaborador se
- No caso de cumprimento de pena em regime fechado pelo colaborador, a Lei 9.807/1999 autori
- Têm prioridade na tramitação o inquérito e o processo criminal em que figure indiciado, ac
- Segundo o art. 19-A da Lei 9.807/1999, o depoimento das pessoas incluídas nos programas de
- A União pode utilizar estabelecimentos especialmente destinados ao cumprimento de pena de 
- Uma vítima de extorsão mediante sequestro passou a ser ameaçada após depor contra os autor
- A execução das atividades necessárias ao programa de proteção fica a cargo de um dos órgão
- Na Lei 9.807/1999, a decisão sobre o ingresso da pessoa no programa de proteção ou sobre s
- A alteração de nome do protegido, prevista na Lei 9.807/1999, tramita perante o juiz compe
- Quanto ao colaborador da Lei 9.807/1999, assinale a alternativa correta.
```

## Pedido 32: Garantias, FGC, Pix e Open Finance (`conhecimentos-bancarios.garantias-fgc-pix`, 66 tópicos de edital)

```
Matéria: conhecimentos-bancarios.garantias-fgc-pix (Garantias, FGC, Pix e Open Finance)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Garantias pessoais: fiança, fiança bancária e aval": 20
- "2. Garantias reais: penhor mercantil, hipoteca e alienação fiduciária": 19
- "3. Fundo Garantidor de Créditos e Sistema de Pagamentos Brasileiro": 16
- "4. Arranjos de pagamento, Pix e canais bancários digitais": 25
- "5. Open Finance, Drex e integração de serviços digitais": 20

Já existem (não repita):
- Uma empresa contrata empréstimo e terceiro assina compromisso escrito para satisfazer a ob
- Um título de crédito contém assinatura de terceiro no espaço destinado à garantia de pagam
- Uma indústria oferece máquinas operacionais e matérias-primas como garantia, mantendo os b
- Para financiar a expansão, uma pessoa constitui garantia real sobre imóvel de que continua
- Um financiamento transfere ao credor propriedade resolúvel do veículo até o pagamento da d
- Um investidor compara CDB de instituição associada e cotas de fundo de investimento comerc
- Duas instituições precisam liquidar obrigações por lançamentos operação a operação, de mod
- Um usuário envia Pix para conta em outra instituição e a operação é liquidada em contas pr
- Uma cliente deseja fazer Pix, mas ainda não cadastrou chave vinculada à conta. Segundo a d
- Uma empresa oferece matérias-primas no penhor mercantil em contrato particular. Para a con
- Fiador demandado em cobrança invoca benefício de ordem e aponta bens livres e suficientes 
- Uma plataforma oferece depósito de poupança, CDB, debênture e cotas de fundo. Qual dupla e
- Uma empresa organiza regras para cartões, outra disponibiliza instrumentos ao pagador e um
- Uma empresa presta exclusivamente serviços de pagamento na modalidade autorizada e não pos
- Uma cliente agenda hoje transferência Pix para data futura. Qual leitura respeita o concei
- Uma instituição B solicita receber extratos que cliente mantém em A, pelo Open Finance. Ap
- Em um projeto-piloto, a transferência de ativo digital é condicionada à transferência simu
- Em discussão sobre aval, alguém afirma que a invalidade da obrigação do avalizado necessar
- Em contrato de fiança, o fiador declara expressamente que responderá como devedor solidári
- Na recuperação judicial de empresa que deu equipamento em alienação fiduciária, questiona-
- Uma câmara do SPB apura obrigações recíprocas de participantes, reduzindo débitos e crédit
- Um investidor compra debênture e cotas de fundo por plataforma de instituição associada ao
- Um industrial constituiu penhor sobre máquinas que permanecem em funcionamento. O contrato
- O cliente autoriza banco B a receber dados de seu financiamento no banco A pelo Open Finan
- Uma proposta afirma que o Drex já é a infraestrutura necessária para toda transferência Pi
- Uma pessoa se obriga por escrito a satisfazer dívida de terceiro se o devedor não cumprir 
- Uma assinatura de aval em título de crédito equivale a hipoteca do imóvel do avalista, por
- Na hipoteca regularmente constituída sobre imóvel, a titularidade da propriedade pode perm
- Cotas de fundos de investimento adquirem garantia ordinária do FGC sempre que compradas no
- O Pix é meio de pagamento instantâneo que pode ser utilizado em diferentes dias e horários
- No Pix, DICT é o sistema de liquidação bruta em tempo real que movimenta as Contas PI, enq
- Um empresário constitui penhor industrial sobre máquinas com contrato registrado no Regist
- Na alienação fiduciária imobiliária da Lei nº 9.514/1997, o contrato transfere propriedade
- O STR efetua liquidação bruta em tempo real operação por operação, sem admitir saldos a de
- Uma ordem Pix entre instituições é liquidada no SPI, mas, depois da liquidação, pode ser a
- Ao compartilhar dados financeiros pelo Open Finance, o usuário pode escolher as instituiçõ
- Uma instituição iniciadora de pagamento pelo Open Finance precisa necessariamente manter a
- Em um teste Drex de entrega contra pagamento, a transferência de ativo e a contraprestação
- No Código Civil, aval e fiança são garantias inteiramente equivalentes: a autonomia do art
- Na recuperação judicial, o art. 49, § 3º, da Lei nº 11.101/2005 reconhece posição específi
- Uma debênture oferecida por instituição associada ao FGC adquire garantia ordinária por re
- O SPI liquida pagamentos Pix interinstitucionais de forma bruta e contínua em Contas PI, s
- No Open Finance, a autorização do cliente para consultar saldos em determinada receptora a
- Tokenização em um projeto como o Drex pode representar direitos sobre ativos e permitir op
- Uma instituição afirma que, como o Pix é operacional, o Piloto Drex já se encontra igualme
```

## Pedido 33: História da física e física moderna (`fisica.historia-fisica-moderna`, 66 tópicos de edital)

```
Matéria: fisica.historia-fisica-moderna (História da física e física moderna)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Cosmologia antiga, Aristóteles e as origens da física": 19
- "2. Revolução científica, mecânica clássica e seus limites": 16
- "3. Relatividade especial, espaço-tempo e transformações de Lorentz": 18
- "4. Massa, energia e origem da teoria quântica da radiação": 23
- "5. Dualidade onda-partícula, efeito fotoelétrico e matéria quântica": 24

Já existem (não repita):
- Uma exposição sobre modelos cosmológicos afirma que Ptolomeu organizou posições planetária
- Um modelo estabelece que o planeta descreve uma elipse e que o Sol ocupa um dos focos dess
- Um carrinho desliza sem atrito significativo em pista horizontal e a força resultante sobr
- Um relatório histórico associa o aparecimento de uma teoria unificada da eletricidade e do
- Na análise histórica da radiação de corpo negro, qual contribuição representou a ruptura i
- Uma experiência ilumina um metal com radiação e observa elétrons emitidos somente acima de
- Um observador mede a velocidade relativa entre dois referenciais inerciais como 0,6c. Qual
- Um relógio acompanha os dois eventos usados para medir o intervalo entre seus próprios tiq
- Considere um corpo de massa de repouso 2 g. Adotando c≈3×10⁸ m/s, qual energia de repouso 
- Dois eventos são simultâneos em S, mas ocorrem em locais diferentes do eixo x. S' se move 
- Uma barra tem comprimento próprio de 10 m e se move paralelamente ao próprio eixo a 0,8c e
- Uma partícula de massa de repouso m desloca-se com velocidade 0,6c em um referencial. Qual
- Um corpo negro ideal aumenta sua temperatura de 3.000 K para 6.000 K. Que alteração ocorre
- Para uma radiação de frequência 5×10¹⁴ Hz, use h≈4,14×10⁻¹⁵ eV·s. Qual é a energia aproxim
- Uma superfície possui função trabalho de 3 eV e recebe fótons de 2 eV, em regime usual de 
- Uma partícula material apresenta comprimento de onda de de Broglie de 0,40 nm. Mantendo vá
- Em discussão sobre queda livre sem resistência do ar, dois corpos de massas diferentes são
- Em S, o evento A ocorre em x=0, t=0, e B em x=c×1 s, t=1 s. Um referencial S' move-se a 0,
- Dois flashes são simultâneos em S, mas separados por Δx=2c·s ao longo de x. Um referencial
- Um objeto move-se com velocidade +0,8c em S. O referencial S' move-se a +0,6c em relação a
- Uma transformação de um sistema reduz sua massa de repouso total em 1 miligrama e converte
- Um metal apresenta função trabalho de 2 eV. A iluminação monocromática fornece fótons de 5
- No regime não relativístico, um elétron e um próton têm a mesma energia cinética, embora s
- Um corpo negro ideal apresenta comprimento de onda do pico espectral em λ igual a 580 nm. 
- Um feixe monocromático ilumina metal acima da frequência de corte. A intensidade dobra sem
- Em uma atividade sobre cosmologia antiga, alguém afirma que a defesa de modelos geocêntric
- Uma explicação histórica atribui a Aristóteles a ideia de que, sem forças externas, um cor
- Em uma linha do tempo, a lei da gravitação universal com força inversamente proporcional a
- Em referencial inercial, um carrinho de massa constante apresenta aceleração proporcional 
- Um observador calcula o fator de Lorentz para uma velocidade relativa de 0,6c e obtém 1,25
- Uma barra em repouso com o medidor tem comprimento próprio de 5 metros; outro medidor, par
- Mesmo para velocidades próximas da luz, a transformação galileana t'=t reproduz exatamente
- Uma régua possui 10 metros de comprimento próprio e se move longitudinalmente a 0,8c. O ob
- O fóton possui massa de repouso nula, mas pode transportar energia e momento linear, de mo
- Dois feixes de luz têm a mesma frequência, mas um possui intensidade maior. Nesse feixe, c
- Se a temperatura absoluta de um corpo negro ideal dobra, o comprimento de onda do pico esp
- A quantização introduzida por Planck na radiação térmica antecedeu a explicação de Einstei
- Uma partícula tem seu momento linear dobrado e, de acordo com de Broglie, seu comprimento 
- Dois eventos separados por um sinal luminoso que percorre no vácuo Δx=cΔt possuem interval
- Um corpo material pode deslocar-se a 0,8c em relação a um referencial inercial e apresenta
- Uma partícula possui fator de Lorentz igual a dois. Sua energia cinética relativística, ne
- No regime não relativístico, um elétron e um próton com a mesma velocidade possuem necessa
- Em emissão fotoelétrica usual, fótons de 5 eV incidentes sobre metal de função trabalho 3 
- Se o comprimento de onda de um fóton no vácuo é duplicado, sua frequência também duplica e
- Ao preparar estados quânticos para medir posição e momento de uma partícula na mesma direç
```

## Pedido 34: Legislação da Polícia Civil do DF (`legislacao.policia-civil-df`, 66 tópicos de edital)

```
Matéria: legislacao.policia-civil-df (Legislação da Polícia Civil do DF)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais e articulação das normas da PCDF": 11
- "2. Formação das carreiras, cargos, ingresso e evolução funcional": 25
- "3. Regime jurídico, jornada de trabalho e direitos": 41
- "4. Regime disciplinar vigente e processo administrativo": 13
- "5. Regimento Interno, estrutura orgânica e competências": 10

Já existem (não repita):
- Uma unidade prepara nota explicativa sobre a PCDF e precisa identificar o ente que a organ
- Em apresentação sobre a atividade investigativa no Distrito Federal, surge dúvida sobre a 
- Um relatório histórico precisa mencionar a norma que criou a Carreira Policial Civil do DF
- Candidato aprovado para cargo da carreira policial pergunta em qual classe ocorre o ingres
- Ao revisar a Lei nº 15.047/2024, uma comissão elenca possíveis penalidades. Qual conjunto 
- Um servidor consulta qual norma aprovou o regimento interno vigente da PCDF, depois da rev
- Uma autoridade analisa pedido de TAC formulado para infração disciplinar sujeita a suspens
- Um processo ordinário disciplinar foi instaurado e precisa de comissão permanente. Quantos
- Em sessão do Conselho Superior da PCDF, verifica-se presença de conselheiros suficientes. 
- Interessado na chefia da PCDF pergunta qual requisito subjetivo é imposto ao ocupante do c
- Durante revisão de um processo, identifica-se proposta de punir hoje com base no art. 43 d
- Uma unidade de pessoal compara remoção ex officio com remoção a pedido, para efeito de aju
- Alguém afirma que a carga de 30 horas semanais da LC distrital nº 840/2011 é suficiente pa
- Uma pessoa aprovada para Delegado possui bacharelado em Direito e completará três anos de 
- Na análise de prescrição de três infrações disciplinares — advertência, suspensão e demiss
- Foi instaurado procedimento disciplinar sumário para apurar abandono de cargo, sem necessi
- Durante reunião do Conselho Superior, nove conselheiros estão presentes, incluindo o Presi
- Uma notícia sobre a Lei nº 15.395/2026 afirma que a PCDF extinguiu, por fusão já sancionad
- Uma avaliação do Regimento Interno consulta o texto compilado da Resolução nº 08/2025 e en
- Servidor pratica várias infrações da mesma natureza em continuidade, com conexão de tempo,
- Em dez anos contados da primeira condenação, servidor acumula quatro infrações administrat
- Servidor quer celebrar novo TAC por infração leve, mas celebrou instrumento anterior há de
- Após a instauração de PAD acusatório, a comissão discute por quanto tempo o art. 112 da Le
- Uma proposta de reorganização da PCDF afirma que, por força da manutenção federal, o Diret
- Em recurso administrativo referente a penalidade de suspensão, dúvida recai sobre a atuaçã
- O fato de a União organizar e manter a PCDF não impede que o Regimento Interno vigente est
- Para interpretar a estrutura atual da PCDF, um relatório de 2026 pode utilizar a Resolução
- A Lei nº 9.264/1996 desmembrou a carreira policial civil originalmente criada pelo Decreto
- Para ingresso no cargo de Delegado da PCDF, o art. 5º, § 1º, da Lei nº 9.264/1996 admite a
- Advertência, suspensão, demissão e cassação de aposentadoria são as quatro sanções discipl
- O art. 57 da LC distrital nº 840/2011 fixa trinta horas semanais e, por isso, dispensa a c
- Em situação de remoção a pedido ou por conveniência disciplinar, a Lei nº 4.878/1965, art.
- A Lei nº 15.047/2024 conservou a tipificação de transgressões dos arts. 41 a 60 da Lei nº 
- Na Lei nº 15.047/2024, o TAC é previsto para condutas puníveis com advertência ou suspensã
- Para apurar abandono de cargo sob rito sumário, a Lei nº 15.047/2024 estabelece conclusão 
- O art. 33 do Regimento Interno aprovado pela Resolução nº 08/2025 exige presença de ao men
- No Conselho Superior da PCDF, o Presidente participa com voto ordinário em cada deliberaçã
- A Lei nº 15.047/2024, art. 15, § 1º, contempla demissão por contumácia em caso de quatro o
- Os dispositivos da Lei nº 15.395/2026 que pretendiam transformar Agente, Escrivão e Agente
- A instauração de processo administrativo disciplinar acusatório interrompe uma única vez a
- Quatro acusações de infrações puníveis com suspensão no intervalo de dez anos, ainda sem j
- A Lei nº 14.735/2023, em regra de alcance nacional, prevê que a jornada mensal de efetivo 
- O art. 62 da Lei nº 4.878/1965 demonstra que qualquer regra da LC distrital nº 840/2011 de
- Segundo os arts. 31 e 34 do Regimento da Resolução nº 08/2025, o Conselho Superior se reún
- Pelo Regimento de 2025, a Corregedoria-Geral da PCDF é unidade de controle interno diretam
```

## Pedido 35: Hemodinâmica e cardiologia intervencionista (`medicina.hemodinamica-cardiologia-intervencionista`, 66 tópicos de edital)

```
Matéria: medicina.hemodinamica-cardiologia-intervencionista (Hemodinâmica e cardiologia intervencionista)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da hemodinâmica e cateterismo cardíaco": 17
- "Cineangiocoronariografia, fisiologia e imagem intracoronária": 17
- "Intervenção coronária percutânea, balões e stents": 17
- "Angioplastia primária e estratégia invasiva nas síndromes coronarianas agudas": 17
- "Intervenções estruturais e cardiopatias congênitas por cateter": 16
- "Radioproteção, contraste e complicações em hemodinâmica": 16

Já existem (não repita):
- Cateterismo cardíaco e cineangiocoronariografia são procedimentos relacionados, mas o prim
- Em uma intervenção coronária percutânea por síndrome coronariana aguda, a punção femoral é
- A imagem da cineangiocoronariografia define diretamente a composição histológica da placa,
- O stent coronariano farmacológico associa suporte mecânico a liberação local de fármaco an
- A TAVI é uma forma de implante de prótese valvar aórtica por cateter, cuja indicação exige
- O tempo de fluoroscopia expressa integralmente a dose de radiação recebida pelo paciente, 
- A pressão de oclusão da artéria pulmonar, quando validamente registrada, é uma estimativa 
- O índice iFR deve obrigatoriamente ser obtido durante hiperemia farmacológica máxima, exat
- IVUS pode orientar dimensionamento do stent e identificar subexpansão, oferecendo informaç
- O fluxo coronariano TIMI 3 ao final da angioplastia assegura ausência de lesão microvascul
- A angioplastia de resgate é uma intervenção que pode ser indicada após evidências de falha
- Uma comunicação interatrial ostium secundum com anatomia favorável e repercussão hemodinâm
- Em paciente com shunt esquerda-direita, uma relação Qp/Qs acima de 1 pode refletir fluxo p
- FFR de 0,78, obtida com técnica válida e hiperemia adequada em estenose intermediária de p
- No infarto com supradesnivelamento do ST acompanhado de choque cardiogênico e doença multi
- O fechamento por dispositivo de uma comunicação interatrial deve ser feito mesmo quando a 
- Elevação de creatinina após cineangiocoronariografia não comprova, isoladamente, que o con
- Após cateterismo femoral, hipotensão e dor lombar podem sinalizar hemorragia retroperitone
- Durante uma aula, pede-se identificar uma atividade típica do cateterismo cardíaco direito
- Ao explicar a cineangiocoronariografia a um estudante, qual definição está correta?
- Qual alternativa identifica corretamente uma característica típica do stent farmacológico 
- Paciente com IAM com supradesnivelamento do ST é encaminhado diretamente para reperfusão p
- Qual das seguintes técnicas é usada para substituir a valva aórtica por prótese implantada
- Ao revisar conceitos de radioproteção em sala de hemodinâmica, assinale a medida coerente 
- O que caracteriza o acesso radial usado com frequência nas intervenções coronarianas conte
- Qual método é particularmente útil para avaliar área de lúmen e expansão de stent dentro d
- Após procedimento com stent coronariano, qual descrição corresponde à trombose de stent?
- No planejamento de fechamento transcateter de comunicação interatrial, qual subtipo costum
- Uma coronariografia mostra estenose de grau intermediário em paciente estável, sem documen
- Ao comparar IVUS e OCT no planejamento de ICP complexa, qual afirmação é adequada?
- Após angioplastia de coronária muito calcificada, a imagem demonstra expansão insuficiente
- Após fibrinólise de IAM com supra, persistem achados consistentes com falha de reperfusão.
- Paciente com infarto, choque cardiogênico e doença coronariana multiarterial vai para angi
- Antes de uma TAVI transfemoral, qual exame costuma ter papel central no planejamento do an
- Um paciente com estenose mitral reumática foi considerado para comissurotomia percutânea. 
- Em uma sala de hemodinâmica, qual indicador expressa de forma integrada o kerma e a área d
- Uma paciente refere alergia alimentar a camarão, mas nunca apresentou reação ao contraste 
- Durante uma ICP com trombo, o fluxo epicárdico volta ao padrão TIMI 3, mas permanecem sina
- Durante cateterismo direito há pressão pulmonar média aumentada. Para caracterizar resistê
- Durante estudo de estenose coronariana, FFR de 0,79 foi obtida com hiperemia adequada; uma
- O operador observa deriva da pressão do fio intracoronário depois de medir FFR em lesão in
- Em ICP de bifurcação, há risco de ocluir ramo lateral de importância clínica. Qual princíp
- No controle final de um stent, a OCT evidencia má aposição importante e a angiografia suge
- Doente com IAM com supra está em choque e apresenta artéria culpada ocluída e lesões grave
- Em caso de CIA associada a hipertensão pulmonar severa e fisiologia de Eisenmenger, um col
- No fechamento percutâneo de PCA em prematuro de muito baixo peso, qual risco anatômico rel
- Após punção femoral para coronariografia, o paciente apresenta hipotensão e dor em flanco,
- Após uma intervenção longa, a equipe dispõe apenas do tempo de fluoroscopia e deseja avali
```

## Pedido 36: Cardiologia e hematologia pediátricas (`medicina.cardiologia-hematologia-pediatrica`, 66 tópicos de edital)

```
Matéria: medicina.cardiologia-hematologia-pediatrica (Cardiologia e hematologia pediátricas)
Capítulos e quantas questões quero em cada um (total 100):
- "Semiologia cardiológica pediátrica e sopros": 20
- "Cardiopatias congênitas e insuficiência cardíaca infantil": 20
- "Doença de Kawasaki e febre reumática": 20
- "Anemias e doença falciforme na infância": 20
- "Hemostasia, leucemias e transplante de células-tronco hematopoéticas": 20

Já existem (não repita):
- Lactente de três meses apresenta sudorese durante as mamadas, taquipneia e ganho ponderal 
- Uma criança assintomática tem sopro musical, sistólico, discreto, na borda esternal esquer
- Em recém-nascido aparentemente saudável, qual afirmação é correta sobre o rastreamento de 
- Qual achado, em comparação aos demais, é sinal de alarme em uma criança com sopro?
- Criança com sopro sistólico em foco pulmonar e desdobramento fixo de B2: qual defeito cong
- Ao fechamento do canal arterial, um neonato evolui com pulsos femorais muito fracos e sina
- Em um lactente com CIV ampla, os sintomas de hiperfluxo pulmonar surgem mais claramente ap
- Qual combinação corresponde à tetralogia de Fallot?
- Neonato com transposição das grandes artérias apresenta cianose intensa. O principal deter
- Pré-escolar com febre persistente, hiperemia conjuntival bilateral não purulenta, fissuras
- Qual afirmação sobre Kawasaki incompleta é a mais adequada?
- Escolar apresenta poliartrite migratória de grandes articulações semanas após faringite, c
- A profilaxia secundária da febre reumática tem como finalidade principal:
- Em criança com microcitose e suspeita de deficiência de ferro, qual afirmação sobre a ferr
- Criança com anemia importante apresenta reticulócitos muito reduzidos. Esse resultado favo
- Qual alternativa diferencia corretamente traço falciforme e doença falciforme?
- Lactente com HbSS, palidez abrupta, prostração, taquicardia e aumento súbito do baço. A co
- Paciente pediátrico com doença falciforme apresenta febre, tosse, queda da saturação e nov
- Na criança com doença falciforme e infecção por parvovírus B19, queda abrupta de hemoglobi
- Criança com hemartroses espontâneas, TTPa prolongado, TP normal e plaquetas preservadas. Q
- No estudo de pancitopenia e suspeita de infiltração medular, que exame fornece melhor info
- Qual achado sustenta a suspeita de leucemia aguda em criança com dores ósseas, palidez e p
- Em leucemia linfoblástica aguda pediátrica, a doença residual mensurável (DRM) tem utilida
- No transplante alogênico para leucemia de alto risco, qual enunciado expressa o efeito enx
- Menino com hematomas profundos e história familiar sugestiva apresenta TTPa normal. Qual é
- Todo sopro cardíaco auscultado na infância indica cardiopatia estrutural e requer restriçã
- A presença de sopro diastólico em criança deve levantar suspeita de lesão cardíaca e justi
- Na avaliação de cardiopatia congênita crítica, a oximetria normal do recém-nascido elimina
- Uma comunicação interatrial pode produzir desdobramento fixo da segunda bulha cardíaca.
- Na comunicação interventricular ampla, a queda pós-natal da resistência vascular pulmonar 
- A coarctação crítica neonatal pode se manifestar com choque após fechamento do canal arter
- Na tetralogia de Fallot, a cianose é explicada exclusivamente por anemia ferropriva.
- A transposição das grandes artérias pode causar cianose importante já no período neonatal 
- A doença de Kawasaki pode causar alterações coronarianas e necessita seguimento cardiológi
- Na suspeita de Kawasaki incompleta, a ausência de quatro sinais mucocutâneos clássicos tor
- Febre reumática aguda é consequência da disseminação direta de estreptococos pelo miocárdi
- A profilaxia secundária da febre reumática visa evitar novos surtos e reduzir progressão d
- Ferritina dentro do intervalo de referência exclui deficiência de ferro em uma criança com
- Reticulocitopenia importante diante de anemia grave sugere resposta medular inadequada.
- O traço falciforme (HbAS) é, por definição, igual à doença falciforme HbSS em manifestaçõe
- Febre em criança com doença falciforme exige atenção imediata pelo risco de infecção bacte
- Sequestro esplênico agudo na doença falciforme é sugerido por aumento rápido do baço assoc
- Síndrome torácica aguda na doença falciforme pode cursar com novo infiltrado pulmonar, feb
- Hemofilia A decorre de deficiência de fator IX e hemofilia B, de fator VIII.
- Hemofilias A e B geralmente cursam com TP normal e TTPa prolongado, mas um TTPa normal não
- Petéquias isoladas são manifestação mais característica de hemofilia grave do que hemartro
- Mielograma e biópsia osteomedular são procedimentos equivalentes e sempre fornecem a mesma
- O hemograma sem leucocitose não exclui leucemia aguda na infância.
- Na leucemia linfoblástica aguda da infância, a doença residual mensurável pode influenciar
- O efeito enxerto-contra-leucemia, após TCTH alogênico, significa necessariamente lesão gra
- O transplante autólogo utiliza células-tronco hematopoéticas do próprio paciente, enquanto
```

## Pedido 37: Operações de crédito e financiamento bancário (`conhecimentos-bancarios.operacoes-credito-financiamento`, 65 tópicos de edital)

```
Matéria: conhecimentos-bancarios.operacoes-credito-financiamento (Operações de crédito e financiamento bancário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Corporate finance, concessão de crédito e capital de giro": 24
- "2. Vendor, compror, hot money, conta garantida e desconto de títulos": 23
- "3. Financiamento à importação, exportação, ACC, ACE e Exim": 18
- "4. Repasses do BNDES e modalidades de crédito rural": 18
- "5. Recuperação de crédito, inadimplência e integração dos produtos": 17

Já existem (não repita):
- Uma fabricante vende mercadorias com pagamento em 90 dias. A própria fabricante contrata l
- Uma rede varejista contrata limite bancário para que diversos fornecedores recebam à vista
- Uma exportadora recebe adiantamento em reais de contrato de câmbio para comprar matéria-pr
- Um exportador já embarcou o produto e entrega as cambiais à instituição financeira para re
- Uma indústria possui limite de conta garantida de R$ 180.000 e utiliza R$ 65.000. Desconsi
- Uma empresa precisa de empréstimo para cobrir falta pontual de caixa durante poucos dias, 
- Uma produtora rural solicita financiamento para comprar sistema de irrigação que deve serv
- Um produtor obtém financiamento para aquisição de sementes e fertilizantes que serão usado
- Uma empresa desconta duplicata de valor nominal R$ 50.000 que vencerá em 60 dias. A taxa h
- Uma empresa solicita a instituição credenciada financiamento indireto com recursos do BNDE
- Após exportar bem nacional, uma indústria cede ao financiador título representativo de cré
- Em projeto de exportação de serviços nacionais, o financiador firma contrato diretamente c
- Uma microempresa contrata crédito bancário documentado por Cédula de Crédito Bancário, sem
- Uma importadora brasileira recorre a financiamento para pagar máquina produzida no exterio
- Cooperativa precisa financiar despesas de armazenagem e operações relacionadas à colocação
- Uma empresa recebe R$ 99.500 líquidos de financiamento de um único período e deverá quitar
- Em uma operação vendor, o fabricante recebe antecipadamente do banco, mas o comprador não 
- Uma duplicata foi emitida por venda mercantil efetiva e não recebeu aceite. O banco detent
- Um banco possui duplicata comercial vencida em 10 de maio de 2024, aceita pelo sacado. Con
- Uma empresa tomadora apresenta recuperação judicial, e o credor bancário possui garantia f
- Um produtor rural pretende financiar industrialização de itens agropecuários na própria pr
- Uma exportadora quer produzir bens nacionais destinados à venda externa e consulta especif
- Uma prestadora brasileira de serviços exportáveis recebe adiantamento em reais de contrato
- Duas empresas assinam conta garantida de R$ 300 mil e hot money de R$ 100 mil, respectivam
- Uma empresa estuda financiar compra de equipamento importado com linha externa e, separada
- Uma distribuidora deseja organizar limite bancário para que seus fornecedores recebam ante
- Uma empresa contratou R$ 200 mil em conta garantida e sacou somente R$ 25 mil; logo, possu
- Uma exportadora obtém adiantamento em moeda nacional antes de embarcar o produto objeto de
- Uma agroindústria rural financia sementes para a safra atual. Pela finalidade do crédito r
- Uma instituição credenciada pode negar proposta de financiamento indireto do BNDES por ris
- Uma Cédula de Crédito Bancário sem aval ou garantia imobiliária deixa de ser título execut
- Uma companhia recebe financiamento para pagar importação de equipamento estrangeiro; a ope
- Em desconto comercial simples, duplicata nominal de R$ 30 mil com taxa de 1% por um períod
- Um vendedor usa vendor para receber antecipadamente vendas a prazo, e o contrato prevê reg
- Em financiamento BNDES Exim Pós-embarque na modalidade Supplier Credit, o banco concede di
- No crédito rural, uma operação destinada a financiar etapas de venda da produção agropecuá
- A análise de crédito corporativo deve considerar o valor da garantia como substituto da ca
- Uma exportadora de serviços obtém antecipação em reais de contrato de câmbio somente depoi
- Um banco que desconta duplicata não aceita perde definitivamente o direito de executar o t
- Uma companhia desconta duplicatas com cláusula de regresso expressamente pactuada. O fato 
- No financiamento indireto típico do BNDES, a instituição credenciada pode avaliar e recusa
- Pelo art. 28 da Lei nº 10.931/2004, a Cédula de Crédito Bancário somente possui força de t
- Quanto à pretensão executiva da duplicata, o art. 18 da Lei nº 5.474/1968 estabelece prazo
- Um produtor rural pretende financiar industrialização na própria propriedade com apenas 40
- Uma empresa endividada ajuíza pedido de recuperação judicial. A cobrança bancária exige ex
```

## Pedido 38: Economia ambiental e dos recursos hídricos (`economia.economia-ambiental-recursos-hidricos`, 65 tópicos de edital)

```
Matéria: economia.economia-ambiental-recursos-hidricos (Economia ambiental e dos recursos hídricos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Economia do meio ambiente e teoria da poluição": 17
- "2. Padrões ambientais e instrumentos econômicos de proteção": 17
- "3. Recursos renováveis e valor econômico da água": 22
- "4. Valoração ambiental e pagamentos por serviços ambientais": 22
- "5. Cobrança pelo uso da água e análise custo-benefício de projetos": 22

Já existem (não repita):
- Uma indústria tem custo marginal privado de 30 unidades monetárias para produzir a próxima
- Um regulamento limita em cada descarga industrial a massa diária de contaminante, enquanto
- A exploração de um recurso renovável retira 9 unidades por ano, enquanto sua regeneração l
- Um estudo usa preços de imóveis semelhantes para estimar quanto a qualidade da água de um 
- Uma família recebe conta mensal pelo tratamento e fornecimento de água encanada. O pagamen
- Uma política fixa em 100 toneladas a soma das emissões anuais e permite comércio de autori
- Uma entidade deseja medir quanto pessoas pagariam, em cenário descrito, para evitar desapa
- Em situação de escassez de água, o art. 1º, III, da Lei nº 9.433/1997 indica prioridade pa
- Uma estação enfrenta prejuízo ambiental marginal estimado em 18 unidades por tonelada de e
- Uma bacia apresenta custos marginais de abatimento de 10 por tonelada na instalação A e 30
- Considere um estoque renovável de crescimento logístico com taxa intrínseca 0,4 por ano e 
- Uma indústria capta 120 metros cúbicos de água e devolve 90 ao mesmo sistema, com hipótese
- Uma pesquisa hipotética de melhoria da qualidade de rio encontrou disposição média a pagar
- Uma área rural recebe remuneração pactuada voluntariamente por manter vegetação que reduz 
- Em um exercício, uma captação de 2.000 metros cúbicos está sujeita apenas a preço unitário
- Um projeto exige investimento inicial de 100 e produz benefício líquido de 110 daqui a um 
- Duas instalações precisam abater juntas 16 toneladas. Seus custos marginais de abatimento 
- Um curso de água recebe danos difusos que atingem milhares de usuários, com difícil identi
- Duas pessoas atribuem valor à preservação de lago. A primeira deseja manter a possibilidad
- Na avaliação do destino dos recursos arrecadados pela cobrança do uso da água, um parecer 
- Um estoque biológico segue crescimento logístico com capacidade de suporte K. Um gestor pr
- Dois projetos hídricos mutuamente exclusivos têm benefícios e custos já expressos em valor
- Em situação normal, um metro cúbico adicional rende benefício líquido marginal de 9 na ind
- Um projeto remunerou conservação de vegetação, mas a região já apresentava proteção estáve
- Um mercado de licenças mantém constante a soma das emissões da bacia, mas transfere autori
- Uma fábrica provoca custos de despoluição suportados por moradores, mas não os inclui em s
- Uma política define um imposto fixo por tonelada de poluente emitida. Por esse motivo, ela
- Um aquífero apresenta capacidade de recarga natural, mas captações persistentes superam es
- Ao entrevistar moradores sobre quanto pagariam por recuperação hipotética de rio, uma pesq
- Em situação de escassez, a Lei nº 9.433/1997 prioriza consumo humano e dessedentação anima
- Uma pessoa valoriza a sobrevivência de uma espécie sem desejar vê-la nem obter proveito di
- Um projeto custa 100 hoje e produz benefício líquido de 110 em um ano. Descontando o benef
- Duas instalações cumprem cada uma seu padrão individual de emissão. Isso assegura, por def
- Na Lei nº 9.433/1997, a fixação de valores da cobrança pode considerar volume e regime de 
- Um contrato voluntário de conservação de mata ciliar paga ao provedor do serviço ambiental
- Num modelo logístico de crescimento de recurso renovável, a maior reposição anual ocorre n
- Uma pesquisa obtém valorização de imóveis causada pela melhora da qualidade de rio e també
- Uma indústria consegue usar o mesmo volume bruto de água após aumentar o reuso interno. Pa
- O art. 22 da Lei nº 9.433/1997 autoriza aplicar metade da arrecadação da cobrança em despe
- Uma meta agregada de abatimento é distribuída entre duas fontes cujos custos marginais são
- No modelo logístico, o rendimento máximo sustentável é obtido em estoque igual à metade da
- Uma entrevista apresenta cenário de recuperação de habitat e pergunta disposição a pagar m
- Dois projetos mutuamente exclusivos apresentam, na mesma base temporal, VPL de 50 e 150. A
- Uma outorga de captação foi emitida para um empreendimento. Isso não representa alienação 
- Uma intervenção hídrica apresenta VPL social positivo após monetização de benefícios. Diss
```

## Pedido 39: Lei de Liberdade Econômica e Regime Jurídico Emergencial (Leis 13.874/2019 e 14.010/2020) (`direito-civil.liberdade-economica-regime-emergencial`, 65 tópicos de edital)

```
Matéria: direito-civil.liberdade-economica-regime-emergencial (Lei de Liberdade Econômica e Regime Jurídico Emergencial (Leis 13.874/2019 e 14.010/2020))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Declaração de Direitos de Liberdade Econômica: princípios, direitos e garantias (Lei 13.874/2019)": 20
- "2. Abuso do poder regulatório e Análise de Impacto Regulatório (Arts. 4º, 4º-A e 5º da Lei 13.874/2019)": 20
- "3. Impactos da Lei 13.874/2019 no Código Civil: pessoa jurídica, negócios jurídicos, contratos e fundos": 20
- "4. Regime Jurídico Emergencial e Transitório - RJET (Lei 14.010/2020)": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- A Lei nº 13.874/2019 instituiu a Declaração de Direitos de Liberdade Econômica e fixou dir
- Entre os princípios norteadores da Declaração de Direitos de Liberdade Econômica previstos
- Constitui direito de toda pessoa, natural ou jurídica, desenvolver atividade econômica de 
- Uma padaria pretende funcionar aos domingos e feriados sem recolher taxa municipal extraor
- Embora o art. 1º, § 3º, da Lei nº 13.874/2019 afaste a aplicação dos arts. 1º a 4º ao Dire
- Apresentados todos os elementos necessários à instrução do processo para obtenção de ato p
- Um servidor público efetivo de uma autarquia ambiental protocola, em nome próprio, requeri
- Em sede de estudos de impacto ou outras liberações de atividade econômica no direito urban
- Para que uma atividade econômica de baixo risco seja dispensada de atos públicos de libera
- O art. 4º da Lei nº 13.874/2019 impõe à administração pública o dever de evitar o abuso do
- De acordo com o art. 5º da Lei nº 13.874/2019, as propostas de edição e de alteração de at
- Configura abuso do poder regulatório, salvo estrito cumprimento a previsão explícita em le
- Um órgão regulador pretende editar ato normativo restringindo o uso e o exercício da publi
- No tocante à proteção da inovação tecnológica frente à regulação estatal, o art. 4º, incis
- A exigência de realização prévia de Análise de Impacto Regulatório (AIR) prevista no art. 
- Determinada secretaria municipal, inconformada com a dispensa de alvará de funcionamento p
- As vedações ao abuso do poder regulatório listadas no art. 4º da Lei nº 13.874/2019 dirige
- Nos termos do art. 5º, parágrafo único, da Lei nº 13.874/2019, a realização da Análise de 
- Com a redação dada pela Lei nº 13.874/2019 ao art. 50 do Código Civil, o abuso da personal
- A Lei nº 13.874/2019 alterou o art. 1.052 do Código Civil para disciplinar uma importante 
- Nos termos do art. 49-A do Código Civil, a pessoa jurídica não se confunde com os seus sóc
- A respeito da interpretação dos negócios jurídicos disciplinada no art. 113, §§ 1º e 2º, d
- Sobre o regime dos contratos civis e empresariais estabelecido nos arts. 421 e 421-A do Có
- Para a caracterização da confusão patrimonial nos termos do art. 50, § 2º, inciso II, do C
- A Lei nº 13.874/2019 introduziu os arts. 1.368-C a 1.368-F no Código Civil para disciplina
- Segundo o art. 113, § 1º, inciso V, do Código Civil, a interpretação do negócio jurídico d
- Se o fundo de investimento com limitação de responsabilidade dos cotistas não possuir patr
- A Lei nº 14.010/2020 (RJET) instituiu normas de caráter transitório e emergencial para a r
- No âmbito do Direito de Família, o art. 15 da Lei nº 14.010/2020 estabeleceu regra emergen
- Para os fins da Lei nº 14.010/2020, considera-se 20 de março de 2020, data da publicação d
- Quanto às relações de consumo no período emergencial, o art. 8º da Lei nº 14.010/2020 susp
- A respeito da resilição, resolução e revisão dos contratos no âmbito da Lei nº 14.010/2020
- Segundo o art. 5º da Lei nº 14.010/2020, a realização de assembleia geral de pessoa jurídi
- Analise a disciplina das sucessões e dos condomínios edilícios na Lei nº 14.010/2020 (arts
- Nos termos do art. 9º da Lei nº 14.010/2020, a vedação à concessão de liminar para desocup
- De acordo com o art. 3º, § 1º, da Lei nº 14.010/2020, a suspensão emergencial dos prazos p
- Em provas de concursos públicos que cobram conjuntamente a Lei nº 13.874/2019 e a Lei nº 1
- Nas questões que abordam os requisitos para a desconsideração da personalidade jurídica ap
- Em negócios jurídicos empresariais paritários, é garantida a livre estipulação das partes 
- Duas sociedades empresárias celebraram contrato de fornecimento de matéria-prima pelo qual
- Compare os marcos temporais estabelecidos na Lei nº 14.010/2020 (RJET) e assinale a altern
- As dúvidas de interpretação do direito civil, empresarial, econômico, urbanístico e tribut
- Um fundo de investimento constituído sob a égide dos arts. 1.368-C a 1.368-F do Código Civ
- A desconsideração da personalidade jurídica prevista no art. 50 do Código Civil exige que 
- Para os fins da Lei nº 14.010/2020, quando um contrato empresarial regido pelo Código Civi
```

## Pedido 40: Praça, leilão, arrematação e remição (`processo-trabalho.praca-leilao-arrematacao`, 65 tópicos de edital)

```
Matéria: processo-trabalho.praca-leilao-arrematacao (Praça, leilão, arrematação e remição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Avaliação e preparação da alienação": 37
- "2. Lances, preço vil e arrematação": 34
- "3. Adjudicação, remição e custas": 15
- "4. Aplicação e como o tema costuma ser cobrado": 14

Já existem (não repita):
- Qual parâmetro subsidiário caracteriza preço vil quando o juiz não fixou preço mínimo?
- O exequente requer adjudicação de bem penhorado. Qual piso deve observar em regra?
- Qual providência caracteriza remição da execução pelo executado antes da alienação?
- Quem responde pelas custas próprias da execução trabalhista, pagas ao final?
- O edital fixa preço mínimo judicial superior à metade da avaliação. Qual oferta não deve s
- Qual distinção entre adjudicação e arrematação está correta?
- Qual informação do edital ajuda a aferir se um lance pode ser considerado vil?
- Uma avaria relevante surgiu no bem depois da avaliação e antes da venda. Qual providência 
- A CLT prevê sinal do arrematante correspondente a qual parcela do lance?
- Qual regra de pagamento se aplica em princípio ao arrematante no CPC, salvo decisão judici
- Quando pode ocorrer remição da execução segundo o art. 826 do CPC?
- Qual alternativa descreve corretamente a função do edital na venda judicial?
- A avaliação indica determinado valor, mas o juiz fixa e publica mínimo superior a 50%. Um 
- O exequente pretende adjudicar bem avaliado acima do crédito e oferece valor igual à avali
- O executado oferece pagar apenas o principal nominal depois da avaliação, deixando juros e
- O edital foi publicado com antecedência, mas não identifica adequadamente o bem nem as con
- Um lance é exatamente igual ao mínimo judicial publicado no edital. A vedação do art. 891 
- Em venda judicial, o maior lance é inferior ao mínimo divulgado no edital. A regra do maio
- O bem foi avaliado por oficial e o valor ficou consideravelmente acima do crédito e acessó
- Um executado pede remição depois que o bem foi arrematado e a carta já foi assinada, ofere
- O executado alega que as custas da execução devem ser pagas pelo arrematante junto com o p
- Um bem indivisível pertence em parte a coproprietário alheio à execução. Qual elemento dev
- A ordem judicial fixa preço mínimo e determina leilão eletrônico. Qual afirmação preserva 
- A pessoa que pretende remir a execução deposita integralmente o débito atualizado antes da
- O edital informa que o pagamento será parcelado conforme proposta admitida judicialmente. 
- Qual alternativa melhor distingue o sinal do arrematante do pagamento para remir a execuçã
- O valor de avaliação parece incompatível com bens semelhantes e há pedido fundamentado de 
- A arrematação foi feita pelo maior lance admissível e o adquirente oferece apenas o sinal,
- Certo ou errado: sem preço mínimo fixado pelo juiz, o CPC considera vil o lance inferior a
- Certo ou errado: a adjudicação pelo exequente pode ser requerida por preço inferior ao da 
- Certo ou errado: o executado pode remir a execução antes de adjudicados ou alienados os be
- Certo ou errado: as custas da execução trabalhista são de responsabilidade do executado e 
- Certo ou errado: a avaliação do bem e o edital de leilão são o mesmo ato processual, com f
- Certo ou errado: a CLT prevê sinal de 20% do valor do lance para garantia da arrematação.
- Certo ou errado: se o juiz fixou mínimo no edital, basta que o lance supere 50% da avaliaç
- Certo ou errado: a adjudicação é uma venda judicial em disputa aberta de lances entre inte
- Certo ou errado: a oferta de pagamento parcial do débito, antes do leilão, basta para remi
- Certo ou errado: a CLT prevê que a arrematação seja anunciada por edital com antecedência 
- Certo ou errado: o maior lance é válido mesmo se ficar abaixo do mínimo judicial publicado
- Certo ou errado: o pagamento integral da dívida atualizada pode permitir remição antes de 
- Certo ou errado: a avaliação pode ser revista se houver erro ou alteração relevante que to
- Certo ou errado: o sinal de 20% entregue pelo arrematante corresponde ao pagamento integra
- Certo ou errado: o CPC admite que a adjudicação pelo exequente ocorra por preço inferior à
- Certo ou errado: o valor do lance deve respeitar o mínimo judicial divulgado, e o parâmetr
- Certo ou errado: a remição pode ser obtida por promessa de pagamento parcelado formulada d
- Certo ou errado: custas da execução trabalhista e preço da arrematação são despesas distin
```

## Pedido 41: Pacto Internacional dos Direitos Civis e Políticos (PIDCP) (`direito.pacto-internacional-direitos-civis-politicos`, 65 tópicos de edital)

```
Matéria: direito.pacto-internacional-direitos-civis-politicos (Pacto Internacional dos Direitos Civis e Políticos (PIDCP))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Origem, autodeterminação, obrigações estatais e derrogações": 20
- "2. Vida, integridade, liberdade, igualdade e proteção de pessoas privadas de liberdade": 20
- "3. Justiça, garantias processuais, legalidade e reconhecimento jurídico": 20
- "4. Consciência, expressão, reunião, família, cidadania e minorias": 20
- "5. Comitê de Direitos Humanos, relatórios, conciliação e disposições finais": 20

Já existem (não repita):
- Um estudo de direitos humanos apresenta cinco datas e pede o ato que promulgou o PIDCP no 
- Uma comunidade de território não autônomo reivindica determinar livremente sua condição po
- Uma pessoa tem direito reconhecido no Pacto violado por agente que atuava oficialmente, ma
- Durante emergência oficialmente proclamada que ameaça a existência nacional, o Estado cons
- Um pesquisador avalia a pena de morte segundo o texto original do PIDCP, independentemente
- Uma pessoa detida por imputação penal permanece sem apresentação à autoridade judicial, e 
- Uma pessoa é processada penalmente em idioma que não compreende e não dispõe de dinheiro p
- Uma comunidade realiza encontro público e pacífico para expressar reivindicações. Qual art
- Uma pessoa consulta o PIDCP para saber qual instância foi criada para analisar relatórios 
- Um Estado afirma que o art. 4º autoriza suspender o art. 8º integralmente porque a proibiç
- Uma pessoa presa foi informada do motivo, mas não teve acesso a tribunal para revisar a le
- Um estrangeiro está legalmente no território de Estado Parte e enfrenta expulsão sem chanc
- Uma pessoa foi condenada definitivamente com pena grave; lei posterior estabelece pena mai
- Uma comunidade religiosa deseja praticar ritos em espaço público, mas existe norma legal e
- Um discurso público promove ódio religioso e convoca participantes a agredir um grupo. Que
- Uma pessoa requer igual proteção da lei contra discriminação, embora não seja cidadã do pa
- O Estado Parte deverá apresentar seu primeiro relatório geral ao Comitê. Qual prazo e enca
- Uma condenação penal definitiva foi anulada após descoberta de fato novo que demonstrou ca
- Uma entidade pública adota regra segundo a qual todos os direitos de cidadania mencionados
- Em situação excepcional prevista no art. 4º, o Estado suspendeu temporariamente uma obriga
- Na eleição e funcionamento do Comitê de Direitos Humanos, um manual confunde mandato, naci
- Dois Estados Partes cogitam submeter ao Comitê acusação de descumprimento do Pacto por um 
- Uma questão submetida sob o art. 41 permanece sem solução satisfatória, e os Estados inter
- Após os primeiros trinta e cinco instrumentos de ratificação ou adesão, um Estado aderiu a
- Uma emenda ao PIDCP recebeu aprovação da Assembleia Geral e aceitação por dois terços dos 
- O Pacto foi aprovado internamente pelo Decreto Legislativo nº 226/1991, e o Decreto nº 592
- O art. 4º do PIDCP autoriza suspender obrigações durante qualquer dificuldade econômica, a
- O art. 7º do PIDCP proíbe não apenas a tortura, mas também submeter alguém a experiências 
- Pelo art. 9º do Pacto, a prisão preventiva de pessoas aguardando julgamento constitui regr
- O art. 11 do PIDCP veda a prisão de pessoa apenas porque ela não pôde cumprir obrigação co
- O art. 25 do PIDCP assegura expressamente, em seus próprios termos, o direito de votar e s
- O art. 14, §3º, assegura ao acusado tempo e meios para defesa, possibilidade de interrogar
- A liberdade de religião do art. 18 integra os direitos não suspensíveis do art. 4º, §2º, e
- O art. 10 estabelece separação entre processados e condenados, ressalvadas circunstâncias 
- O art. 14, §5º, reconhece ao condenado revisão da condenação e pena por instância superior
- Os arts. 21 e 22 distinguem a reunião pacífica da associação; o art. 22 também admite rest
- Os membros do Comitê de Direitos Humanos são dezoito representantes diplomáticos vinculado
- O art. 40 obriga Estado Parte a apresentar o primeiro relatório até um ano desde a vigênci
- No art. 41, o Comitê recebe comunicação de um Estado contra outro independentemente de amb
- Se o Estado destinatário de comunicação interestatal do art. 41 não resolver satisfatoriam
- A comissão de conciliação ad hoc prevista no art. 42 possui dezoito integrantes e pode ser
- O art. 24 garante à criança proteção sem discriminação e determina registro imediato após 
- O art. 49 estabelece que, alcançado o trigésimo quinto depósito internacional, os Estados 
- O art. 50 aplica o PIDCP às unidades constitutivas dos Estados federativos sem limitação n
- Segundo o art. 51, emenda aprovada e aceita pelos dois terços necessários passa a obrigar,
```

## Pedido 42: Cinética química (`ciencias.cinetica-quimica`, 64 tópicos de edital)

```
Matéria: ciencias.cinetica-quimica (Cinética química)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Medidas de velocidade e estequiometria das reações": 18
- "2. Leis experimentais, ordens e etapas elementares": 22
- "3. Colisões, estado de transição, catalisadores e ambiente": 26
- "4. Temperatura, energia de ativação e equação de Arrhenius": 16
- "5. Decomposição de medicamentos e estudos de estabilidade": 18

Já existem (não repita):
- Em um recipiente de volume constante, a concentração de A cai de 0,80 para 0,56 mol/L entr
- Na transformação 2A+B→3C, em volume constante e sem reações paralelas relevantes, A desapa
- O gráfico de concentração de um produto em função do tempo é curvo, e se deseja a velocida
- Em uma reação cuja lei experimental é velocidade=k[A]², os demais fatores permanecem fixos
- Ensaios de uma reação mostram que dobrar [A] dobra a velocidade e dobrar [B] a quadruplica
- Em determinada faixa, a decomposição de uma substância apresenta taxa constante mesmo quan
- Uma mistura reativa apresenta choques frequentes, mas apenas pequena fração se transforma 
- Uma reação atinge seus produtos mais rapidamente após adicionar catalisador, em temperatur
- Um pesquisador aplica a equação de Arrhenius à velocidade de decomposição medida a 25 °C. 
- Um lote tinha teor inicial de 100 mg/L. Um cálculo de estabilidade informa t90=8 meses. O 
- Em três ensaios, [A]=0,10 e [B]=0,10 resultam em v=0,004; dobrar somente A dá v=0,008; dob
- Uma hidrólise apresenta lei v=k[A][H2O]. A água está em grande excesso e sua concentração 
- Um mecanismo propõe uma etapa elementar em que uma entidade de A colide com uma entidade d
- Um pó metálico reage com solução ácida mais rapidamente após ser triturado, mantendo massa
- Em um reator gasoso rígido e isotérmico, adiciona-se um gás inerte sem alterar as quantida
- No perfil energético de uma etapa elementar, uma configuração crítica situada na região de
- No gráfico de Arrhenius simples, o eixo vertical é ln(k) e o horizontal é 1/T, com T em ke
- Uma solução sofre degradação de ordem zero com teor inicial 100 mg/L e constante de perda 
- Uma lei experimental sob temperatura fixa é v=k[A][B]², com concentrações em mol/L e veloc
- Para uma etapa cuja Ea é 50 kJ/mol, estime pela equação de Arrhenius a razão k(310 K)/k(30
- A degradação de um composto aumenta em meio muito ácido e muito básico, mas fica mais lent
- Uma formulação apresenta dados acelerados a 40 °C, mas o perfil de degradantes muda em rel
- Uma substância degrada-se por primeira ordem, com constante de velocidade de 0,020 mês⁻¹. 
- Num modelo simples de segunda ordem, a concentração inicial é 1,0 mol/L e k=0,20 L mol⁻¹ m
- Propõe-se mecanismo A→I (lento), seguido de I+B→P (rápido), sem acúmulo relevante de I nas
- Num ensaio de volume constante, a concentração de um reagente diminui 0,15 mol/L em 5 s; a
- Para a reação global 2A+B→C, a lei experimental de velocidade deve ser necessariamente v=k
- Em uma lei experimental v=k[A], dobrar [A] sem alterar temperatura ou outras condições dup
- Como o catalisador reduz a barreira cinética de uma rota, ele modifica necessariamente a c
- No modelo de Arrhenius, a temperatura deve ser expressa na escala absoluta, e a constante 
- Em condições fixas, a adição de um gás inerte a um recipiente rígido e isotérmico aumenta 
- Em uma reação bimolecular elementar entre A e B, a molecularidade contabiliza duas entidad
- No gráfico de energia de uma etapa, o complexo ativado deve ser interpretado como intermed
- Se a velocidade experimental depende de [A][B], mas B permanece em grande excesso e quase 
- A concentração de um produto aumenta de modo curvilíneo com o tempo; por isso, a velocidad
- Num modelo simples de decomposição de primeira ordem com k constante, a meia-vida calculad
- Ao trocar um solvente menos polar por outro mais polar, toda reação em solução necessariam
- Se a regressão de ln(k) sobre 1/T se ajusta à forma simples de Arrhenius com inclinação -E
- Na teoria do estado de transição, a entalpia de ativação e a energia de ativação de Arrhen
- Para degradação de ordem zero, o t90 teórico dobra quando o teor inicial dobra, mantendo a
- Em degradação de primeira ordem, t90 corresponde ao mesmo tempo que a meia-vida, pois ambo
- A diretriz ICH Q1E admite extrapolação limitada e cientificamente justificada de dados de 
- Como a ICH Q1A(R2) apresenta o par 40 °C e 75% de umidade relativa para estudos acelerados
- Uma reação global pode ter lei experimental de primeira ordem em A mesmo que B participe d
- Uma constante de velocidade numérica igual a 0,1 pode ser comparada diretamente entre reaç
```

## Pedido 43: Função modular (`matematica.funcao-modular`, 64 tópicos de edital)

```
Matéria: matematica.funcao-modular (Função modular)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Valor absoluto e distância": 20
- "2. Gráficos e transformações": 20
- "3. Equações, inequações e análise por casos": 20
- "4. Como o tema costuma ser cobrado": 20
- "5. Conferência de resultados e domínio": 20

Já existem (não repita):
- Calcule o valor numérico da expressão |−7|+|3|.
- Qual é a distância entre os números −4 e 5 na reta real?
- Resolva |2x−6|=8 e calcule a soma das duas soluções reais.
- A solução de |x−3|<2 é um intervalo aberto. Qual é o comprimento desse intervalo?
- A função f(x)=|x+4|−3 tem gráfico em V. Qual é a abscissa do vértice?
- Para f(x)=|2x−8|+1, calcule f(3).
- Resolva |3x+6|=12 e indique a maior das duas soluções.
- Quantos números inteiros pertencem ao intervalo definido por |x−1|≤3?
- Para f(x)=|x−5|, qual é o valor de f(2)?
- Em y=|2x+4|, para qual valor de x o gráfico cruza o eixo horizontal?
- Quantas soluções de |x+1|=4 pertencem ao intervalo fechado −5≤x≤5?
- Quantos valores inteiros de x satisfazem |2x−4|<6?
- Para f(x)=|x−2|+3, calcule f(−1).
- O gráfico y=|x| encontra a reta y=4 em dois pontos. Qual é a distância horizontal entre es
- Calcule |0−3|+|0+1|.
- Resolva |x−2|=|x+4|. Qual é a solução real?
- A função f(x)=|2x+3| cruza o eixo horizontal em que abscissa?
- Quantos inteiros satisfazem |x|≤2?
- Para g(x)=|x−2|+|x+1|, calcule g(3).
- Quantos números inteiros no intervalo fechado −6≤x≤8 satisfazem |x−2|>3?
- Qual é o conjunto solução de |3x-4|=2?
- Qual é o valor de |-7|?
- Para x=-3, qual é o valor de |x|?
- Qual é a solução da equação |x|=4?
- Qual expressão representa a distância entre os reais x e 5?
- Resolva a equação modular |x-2|=3 e indique todas as soluções reais.
- Qual é o vértice do gráfico y=|x-4|?
- Para c>0, a inequação |A(x)|<c equivale a qual condição?
- Qual é a imagem da função f(x)=|x| nos reais?
- Que gráfico resulta de y=|x|+2 em relação a y=|x|?
- Resolva a equação |2x+1|=5 e indique as soluções reais.
- Qual é o conjunto solução de |x-1|>2?
- Para a função y=|2x-6|, em que abscissa ocorre o vértice?
- Qual é a solução de |x+2|≤1?
- A função f(x)=|x-3|+1 tem qual valor mínimo?
- Quantas soluções reais tem |x|=-2?
- Para f(x)=|g(x)|, o que ocorre com os trechos do gráfico de g que ficam abaixo do eixo x?
- Resolva |2x-1|=|x+2|.
- No gráfico de y=|x+1|-2, qual é o vértice?
- A equação |x-2|=0 possui qual solução?
- A definição de |x| por casos usa x para x≥0 e -x para x<0.
- O valor absoluto de qualquer número real é negativo quando o número original é negativo.
- A distância entre dois reais a e b pode ser expressa por |a-b|.
- Para todo real x, |x|=x, inclusive quando x<0.
- A função f(x)=|x| é simétrica em relação ao eixo vertical.
- O gráfico y=|x-4| tem vértice na origem, pois o número 4 está dentro do módulo.
- As funções |g(x)| e g(|x|) sempre têm o mesmo gráfico.
- A equação |A(x)|=c não possui solução quando c<0.
- Para c>0, |A(x)|>c equivale a A(x)<-c ou A(x)>c.
- Para c>0, |A(x)|<c equivale a A(x)<-c ou A(x)>c.
- A função |ax+b|, com a diferente de zero, troca a expressão usada no ponto x=-b/a.
- Se |x-a|≤r e r≥0, a solução pode ser escrita como a-r≤x≤a+r.
- Ao resolver uma equação modular por casos, as raízes devem ser conferidas no domínio e nas
- Para resolver |A(x)|=c com c≥0, basta considerar o caso A(x)=c e ignorar A(x)=-c.
- Na inequação |A(x)|≤c, com c>0, a solução de A(x) deve ficar dentro do intervalo fechado [
- A equação |x|=0 possui infinitas soluções reais, pois o módulo é sempre não negativo.
- Para quaisquer reais x e y, vale |x+y|≤|x|+|y|.
- A propriedade |xy|=|x|+|y| vale para todos os reais x e y.
- O gráfico de y=g(|x|) pode ser obtido usando o ramo de g para x≥0 e refletindo-o no eixo v
- Em y=|g(x)|, as partes do gráfico de g abaixo do eixo horizontal são refletidas no eixo ve
```

## Pedido 44: Fisioterapia cardiorrespiratória e em terapia intensiva (`fisioterapia.cardiorrespiratoria-terapia-intensiva`, 64 tópicos de edital)

```
Matéria: fisioterapia.cardiorrespiratoria-terapia-intensiva (Fisioterapia cardiorrespiratória e em terapia intensiva)
Capítulos e quantas questões quero em cada um (total 100):
- "Bases anatomofisiológicas cardiorrespiratórias ao longo da vida": 17
- "Avaliação cinesiofuncional e monitorização cardiorrespiratória": 17
- "Higiene brônquica, expansão pulmonar, oxigenoterapia e gases": 17
- "Ventilação mecânica invasiva, não invasiva e liberação do suporte": 17
- "Reabilitação pulmonar, cardíaca e fisiologia do exercício": 16
- "Fisioterapia nas UTIs adulta, pediátrica e neonatal": 16

Já existem (não repita):
- Na fisiologia pulmonar, qual estrutura é diretamente responsável pela produção de surfacta
- Um paciente com broncoespasmo apresenta dificuldade predominante de passagem do fluxo aére
- No ventilador, volume corrente de 400 mL e frequência de 15 ciclos/minuto produzem qual ve
- Em um doente com áreas pulmonares perfundidas, porém praticamente não ventiladas, a altera
- Um paciente está em oxigenoterapia com SpO₂ aparentemente satisfatória. Qual variável não 
- Durante avaliação, o profissional precisa distinguir alterações de pH e ventilação alveola
- Na análise de curvas ventilatórias, o fluxo expiratório permanece negativo quando começa a
- Em paciente passivo sob ventilação, a pressão de pico aumenta, mas a pressão de platô perm
- Qual das condições abaixo torna a aspiração de via aérea artificial mais justificável do q
- Entre dispositivos convencionais de oxigenoterapia, qual oferece concentração inspirada ma
- Paciente estável, com secreção pouco eliminada e tosse preservada, precisa de recurso que 
- Em unidade neonatal, qual princípio deve orientar a escolha entre higiene brônquica, aspir
- Em ventilação mecânica, a sigla PEEP representa qual parâmetro?
- Na comparação entre CPAP e VNI em dois níveis, qual afirmação descreve corretamente o CPAP
- Após melhora da causa da insuficiência respiratória, adulto está estável para avaliação de
- Conforme diretriz AARC de 2024 para adultos, qual afirmação sobre o índice de respiração r
- Em adulto com obstrução expiratória importante e auto-PEEP, qual ajuste conceitual pode re
- Qual medida usa diferença entre pressão de platô e PEEP total, quando tecnicamente mensurá
- Qual componente integra um programa estruturado de reabilitação pulmonar para adultos com 
- Na reabilitação cardíaca de uma pessoa que usa betabloqueador, qual estratégia complementa
- Ao avaliar resultados da reabilitação pulmonar, qual achado demonstra ganho funcional mesm
- Na UTI adulta, antes de colocar um paciente ventilado em sedestação, o fisioterapeuta deve
- Recém-nascido prematuro utiliza suporte respiratório e apresenta flutuação de saturação. Q
- Em criança internada por bronquiolite sem secreção clinicamente retida, qual afirmativa so
- Os pneumócitos tipo II produzem surfactante que auxilia a estabilidade alveolar.
- Uma relação V/Q alterada só pode ocorrer por perfusão reduzida, nunca por ventilação reduz
- Para o mesmo volume corrente e a mesma frequência, o aumento do espaço morto pode diminuir
- O oxímetro de pulso substitui a gasometria arterial quando se deseja medir PaCO₂.
- A presença de artefatos de movimento e má perfusão periférica pode comprometer a leitura d
- Em paciente sob ventilação mecânica, aumento de pressão de pico sempre comprova queda de c
- A aspiração traqueal deve ser realizada em intervalos fixos mesmo na ausência de sinais de
- O oxigênio medicinal favorece a combustão, o que exige cuidados com fontes de ignição.
- A máscara de Venturi permite entrega de FiO₂ mais controlada que o cateter nasal convencio
- A aplicação de técnicas de higiene brônquica em recém-nascidos deve ser automática, pois i
- PEEP é a pressão positiva mantida ao final da expiração e não representa a FiO₂ ofertada.
- A aprovação em um TRE dispensa avaliação da tosse e da capacidade de proteger vias aéreas 
- A diretriz AARC de 2024 estabelece o índice de respiração rápida e superficial como condiç
- A diferença entre IPAP e EPAP na VNI bilevel é parte da assistência inspiratória, diferent
- Reabilitação pulmonar resume-se à realização de exercícios respiratórios isolados em todos
- A percepção de esforço pode complementar os dados de frequência cardíaca ao prescrever exe
- Em adultos com doença intersticial pulmonar, diretriz ATS de 2023 recomenda reabilitação p
- A mobilização na UTI requer avaliação de estabilidade e monitorização antes e durante a at
- A baixa idade gestacional torna irrelevantes as metas de oxigenação: toda concentração alt
- Na bronquiolite pediátrica, a inexistência de retenção de secreção não obriga a realizar f
```

## Pedido 45: Processo Judicial Eletrônico (PJe) (`processo-trabalho.processo-judicial-eletronico`, 63 tópicos de edital)

```
Matéria: processo-trabalho.processo-judicial-eletronico (Processo Judicial Eletrônico (PJe))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Informatização processual, assinatura e credenciamento": 21
- "2. Protocolos, publicações, intimações e contagem de prazos": 21
- "3. Documentos digitais, consulta aos autos e indisponibilidade": 21
- "4. PJe-JT e a Resolução CSJT 185/2017 na rotina processual": 19
- "5. Juízo 100% Digital: opção, oposição, retratação e exceções": 18

Já existem (não repita):
- Uma ação trabalhista será ajuizada eletronicamente. O interessado pergunta se a Lei 11.419
- Um usuário credenciado precisa assinar uma petição eletrônica e consulta as formas de iden
- Uma petição foi enviada efetivamente ao sistema judicial às 23h50 do último dia do prazo, 
- Um ato foi disponibilizado no Diário da Justiça eletrônico numa segunda-feira sem feriados
- Uma intimação foi enviada a portal eletrônico cadastrado, mas não houve consulta ao seu co
- Uma intimação é consultada pela parte cadastrada num sábado, sem feriado na segunda-feira 
- Um processo recebe arquivo produzido desde a origem em meio digital e juntado com garantia
- Uma parte não consegue digitalizar contrato em papel devido ao grande volume e comunica o 
- Em ação trabalhista, a parte demandante seleciona Juízo 100% Digital ao distribuir o feito
- Uma empresa recebe a primeira notificação em reclamação trabalhista marcada como Juízo 100
- Uma informação foi disponibilizada no diário eletrônico na segunda-feira, sem feriados. Qu
- Uma intimação é disponibilizada no portal cadastrado de profissional habilitado. Além do p
- Durante a tramitação, documento em papel foi digitalizado e regularmente juntado. A parte 
- O último dia de um prazo cai numa quinta-feira de expediente. Relatório oficial indica ind
- No dia de vencimento de prazo, o relatório oficial informa falha do PJe entre 23h45 e 23h5
- Uma parte avalia se o PJe deixou de funcionar ou se falhou apenas a sua conexão doméstica.
- As partes aderiram ao Juízo 100% Digital, mas uma inspeção essencial exige presença física
- Após a adesão válida ao Juízo 100% Digital, uma parte deseja mudar de opção antes da sente
- Parte cadastrada recebe intimação no portal e consulta seu conteúdo em domingo. A comunica
- No último dia de um prazo contado em dias, o PJe apresenta indisponibilidade oficial somen
- Um profissional junta por digitalização contrato físico relevante à instrução e quer desca
- Uma unidade pretende digitalizar autos que ainda tramitam em papel. Antes da conversão, pa
- Está programada manutenção do PJe que pode gerar indisponibilidade. Qual antecedência míni
- Ao aceitar o Juízo 100% Digital, uma parte pretende transferir sua ação para unidade de ou
- Em processo eletrônico sem segredo de justiça, advogado cadastrado, mas não vinculado prev
- Uma reclamação trabalhista pode tramitar eletronicamente com base na Lei 11.419/2006, pois
- A assinatura eletrônica prevista na Lei 11.419/2006 somente é válida quando baseada em cer
- Petição efetivamente transmitida ao sistema judicial às 23h55 do último dia de prazo é tem
- Se um ato judicial é disponibilizado no Diário da Justiça eletrônico na segunda-feira útil
- Se não houver consulta ao conteúdo da intimação enviada a portal próprio, considera-se aut
- O simples aviso por e-mail de que existe intimação em portal cadastrado substitui a consul
- Documento que nasceu eletrônico e foi juntado ao processo com garantia de origem e do sign
- Uma vez que o documento em papel é digitalizado e juntado aos autos, seu detentor pode des
- A indisponibilidade do PJe deve constar de relatório do serviço e ser divulgada, integrand
- Uma falha oficialmente registrada no PJe durante os últimos cinco minutos do prazo, entre 
- No processo trabalhista, a parte demandada pode opor-se à adoção inicial do Juízo 100% Dig
- No Juízo 100% Digital, uma inspeção probatória presencial inviabiliza a continuidade do pr
- Na tramitação de ato pelo portal cadastrado, a parte consulta a intimação num sábado. Embo
- Pelo art. 11 da Resolução CNJ 185/2013, uma falha oficial do PJe que ocorra apenas das 2h 
- A comunicação de manutenção programada capaz de causar indisponibilidade do PJe deve ser f
- No ato de digitalizar processo originalmente físico, a Lei 11.419/2006 concede prazo de de
- A Lei 11.419/2006, art. 10, § 2º, trata da indisponibilidade técnica do sistema judicial e
- A adoção do Juízo 100% Digital modifica a competência territorial e material da unidade, r
- A retratação da escolha pelo Juízo 100% Digital pode ocorrer uma única vez até a sentença 
- A existência de autos no PJe significa que o Juízo 100% Digital foi automaticamente adotad
```

## Pedido 46: Processo estrutural e litígios estruturais (`processo-civil.processo-estrutural`, 63 tópicos de edital)

```
Matéria: processo-civil.processo-estrutural (Processo estrutural e litígios estruturais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Problema, litígio e processo estrutural": 34
- "2. Características e decisões estruturais": 33
- "3. Políticas públicas, STF e atores do processo": 33

Já existem (não repita):
- Problema estrutural é um estado de desconformidade, normalmente contínuo, relacionado ao f
- O litígio estrutural caracteriza-se por ser:
- A doutrina aponta como marco de origem das structural injunctions o caso Brown v. Board of
- Para existir problema estrutural, é indispensável a prática de um ato ilícito isolado e id
- É exemplo típico de situação que pode dar origem a um processo estrutural:
- A finalidade do processo estrutural é substituir, de forma progressiva e monitorada, um es
- A doutrina admite traços estruturais também em processos que não são ações coletivas típic
- Segundo a doutrina, o processo estrutural costuma desenvolver-se em duas fases, que são:
- No processo estrutural, o juiz pode valer-se de medidas executivas atípicas, com fundament
- A flexibilidade procedimental do processo estrutural pode ser viabilizada, entre outros me
- No processo estrutural, a regra da estabilização da demanda impede o juiz de considerar fa
- A participação ampliada no processo estrutural costuma ocorrer por meio de:
- A decisão estrutural costuma fixar o estado de coisas a ser alcançado e determinar que o r
- No processo estrutural, o papel do juiz se aproxima mais do de:
- As decisões de implementação no processo estrutural têm caráter provisório e podem ser rev
- A consensualidade no processo estrutural apoia-se no dever, previsto no CPC, de o Estado e
- Ao interpretar o pedido em processo estrutural, a doutrina recomenda:
- No Tema 698 da repercussão geral, o STF entendeu que a intervenção do Poder Judiciário em 
- Segundo a tese do Tema 698 do STF, a decisão judicial sobre políticas públicas deve, como 
- Na ADPF 347, o STF reconheceu o estado de coisas inconstitucional do sistema prisional bra
- O estado de coisas inconstitucional caracteriza-se por:
- O Ministério Público e a Defensoria Pública destacam-se como legitimados para provocar a a
- Os Tribunais de Contas podem contribuir para processos estruturais fornecendo dados técnic
- Entre as críticas dirigidas aos processos estruturais, aponta-se:
- A cláusula da reserva do possível pode ser invocada pelo Poder Público para afastar integr
- Em ação civil pública sobre a falta crônica de vagas em creches de um Município, a técnica
- O diálogo institucional entre Judiciário, Administração, órgãos de controle e sociedade ci
- Assinale a afirmativa correta sobre o processo estrutural no direito brasileiro.
- A chamada execução negociada, típica do processo estrutural, consiste na construção conjun
- A nomeação de interventor ou a criação de comitê de monitoramento pode ser utilizada pelo 
- No processo estrutural, a decisão que certifica o problema e fixa as metas a alcançar é ch
- O processo estrutural dispensa o contraditório, já que busca o interesse público.
- Litígios ambientais de grande escala, como a recuperação de uma bacia hidrográfica degrada
- No modelo estrutural, os prazos e as metas costumam ser escalonados, admitindo cumprimento
- A técnica do estado de coisas inconstitucional, adotada pelo STF na ADPF 347, tem como ref
- A definição técnica das medidas para superar o problema estrutural deve, preferencialmente
- Os processos estruturais são incompatíveis com a celebração de acordos entre o Ministério 
- Relatórios periódicos de cumprimento, com indicadores verificáveis, são instrumentos usuai
- O dispositivo do CPC que autoriza o juiz a determinar todas as medidas indutivas, coerciti
- Em processo estrutural sobre o sistema de saúde de um estado, o juiz percebe, durante a im
```

## Pedido 47: Engenharia de alimentos: operações unitárias, embalagens e processos (`engenharia.engenharia-alimentos-operacoes`, 63 tópicos de edital)

```
Matéria: engenharia.engenharia-alimentos-operacoes (Engenharia de alimentos: operações unitárias, embalagens e processos)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, balanços e operações mecânicas": 20
- "Secagem, evaporação e tratamentos térmicos": 20
- "Refrigeração, congelamento e armazenamento": 20
- "Embalagens, materiais e rotulagem de alimentos": 20
- "Layout, qualidade, segurança e sustentabilidade industrial": 20

Já existem (não repita):
- Em regime estacionário, sem reações nem perdas, a soma das vazões mássicas de entrada é ig
- Um lote com 20% de umidade em base úmida apresenta exatamente 20% de umidade em base seca.
- A homogeneização de uma emulsão pode empregar intensos esforços hidrodinâmicos para reduzi
- A filtração por meio poroso tem sua vazão obrigatoriamente aumentada à medida que a torta 
- Uma indústria recebe 1.000 kg de polpa com 30% de água em base úmida e a seca até 12% de á
- Em uma linha de bebidas, deseja-se separar partículas finas em suspensão aplicando um camp
- Qual variável é especialmente importante para escolher um misturador para creme alimentíci
- Uma fábrica quer reduzir o tamanho médio de grãos secos por impacto repetido. Qual equipam
- Após filtração por membrana, o fluxo do permeado diminui em pressão semelhante e a limpeza
- A atividade de água expressa disponibilidade termodinâmica de água e não pode ser inferida
- Todo alimento em secagem convectiva apresenta necessariamente uma fase longa e bem definid
- Para um mesmo líquido, reduzir a pressão absoluta geralmente diminui sua temperatura de eb
- A pasteurização industrial tem como objetivo obrigatório eliminar todos os esporos bacteri
- Alimentam-se 200 kg/h de solução com 15% de sólidos não voláteis a um evaporador. O produt
- Ao especificar a tecnologia para obtenção de leite em pó a partir de alimento líquido atom
- Um processo térmico possui valor D de 2 minutos para um microrganismo nas condições especi
- Qual é o principal objetivo energético de empregar múltiplos efeitos na concentração por e
- Uma empresa deseja preservar o vigor germinativo de sementes durante a retirada de água. A
- No ciclo de compressão de vapor, o evaporador absorve calor do ambiente refrigerado e o co
- Congelar um alimento elimina obrigatoriamente todos os microrganismos viáveis e faz o prod
- A temperatura do ar de uma câmara frigorífica, isoladamente, comprova que o centro de toda
- A aeração de grãos pode buscar uniformização térmica e prevenção de focos de deterioração,
- Um sistema remove 90 kW de calor da câmara frigorífica e consome 30 kW de potência no comp
- Aparecem perdas expressivas de líquido durante o descongelamento de um produto congelado l
- No estoque de alimentos com datas de vencimento diferentes, a regra que prioriza a expediç
- Qual parcela deve ser contemplada no dimensionamento de carga térmica de uma câmara de con
- Um silo apresenta pontos quentes e condensação apesar de os grãos terem sido inicialmente 
- Boa barreira do filme ao oxigênio é suficiente para garantir conservação do alimento mesmo
- A migração de substâncias presentes em materiais de contato com alimentos é assunto de ava
- Uma embalagem anunciada como biodegradável é automaticamente aprovada para contato com tod
- A embalagem secundária geralmente agrupa unidades de venda, enquanto a primária pode estar
- Para uma aplicação com alta exigência de proteção contra luz e gases, qual característica 
- Uma embalagem plástica destinada a contato direto com alimentos contém resina reciclada. Q
- Qual falha pode permitir alteração da atmosfera interna apesar do uso de material com baix
- Ao desenvolver um rótulo para produto alimentício embalado, qual decisão técnica é adequad
- Por que embalar um alimento sob vácuo não dispensa a avaliação dos perigos microbiológicos
- A separação entre fluxos de matéria-prima potencialmente contaminada e de produto pronto p
- A verificação demonstra previamente que um tratamento térmico consegue reduzir determinado
- O sistema APPCC pode prescindir de programas de Boas Práticas de Fabricação, pois todo per
- Recuperar calor e reduzir perdas de produto são ações potenciais de sustentabilidade, desd
- Em uma planta que processa alimentos crus e depois envasa alimento pronto para consumo, qu
- Qual alternativa descreve corretamente a diferença entre perigo e risco no APPCC?
- Em um sistema APPCC, uma etapa foi definida como PCC. Que elemento é essencial ao seu func
- Uma fábrica apresentou amostra microbiológica conforme, mas descobriu que o sensor de um P
- Uma indústria quer reduzir o impacto ambiental da etapa de evaporação de alimentos sem per
```

## Pedido 48: Resíduos sólidos: Política Nacional e gestão (`meio-ambiente.residuos-solidos-pnrs`, 62 tópicos de edital)

```
Matéria: meio-ambiente.residuos-solidos-pnrs (Resíduos sólidos: Política Nacional e gestão)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da PNRS e hierarquia de gestão": 17
- "Responsabilidade compartilhada, logística reversa e planos": 17
- "Classificação, coleta seletiva, catadores e valorização": 17
- "Resíduos de serviços de saúde e PGRSS": 17
- "Resíduos da construção civil e gestão de entulhos": 16
- "Aterros, encerramento de lixões e regras do Amazonas": 16

Já existem (não repita):
- Na PNRS, a disposição final ambientalmente adequada em aterro é uma etapa prioritária ante
- Um material descartado pode ser resíduo sólido e ainda não ser rejeito.
- A responsabilidade compartilhada pelo ciclo de vida implica atribuições individualizadas e
- Segundo a codificação da coleta seletiva da CONAMA 275/2001, o azul indica papel e papelão
- Os resíduos classificados como Grupo E na RDC Anvisa 222/2018 incluem materiais perfurocor
- Resíduos de concreto limpo de uma demolição são, em regra, exemplos de Classe A na classif
- Lixão a céu aberto e aterro sanitário licenciado são expressões técnicas equivalentes.
- A logística reversa corresponde exclusivamente ao encaminhamento de resíduos domiciliares 
- Uma planilha que informa apenas toneladas recolhidas pela coleta seletiva comprova, por si
- Todo resíduo produzido em estabelecimento assistencial de saúde deve ser classificado no G
- Na redação atualizada da CONAMA 307, o gesso integra a Classe B dos resíduos da construção
- A coleta seletiva é caracterizada pela coleta de materiais que já foram segregados segundo
- O Plano Municipal de Gestão Integrada de Resíduos Sólidos substitui automaticamente o PGRS
- Uma medida de encerramento de lixão deve considerar também os trabalhadores que dependiam 
- Na PNRS, disposição final ambientalmente adequada e destinação final ambientalmente adequa
- Uma bateria com invólucro plástico pode ser encaminhada indistintamente ao coletor vermelh
- Resíduos de serviços de saúde exclusivamente do Grupo D podem, nas condições previstas pel
- Na classificação de resíduos da construção civil, embalagens vazias de tintas imobiliárias
- O Decreto estadual do Amazonas nº 50.890/2024 trata da logística reversa pós-consumo e art
- A Lei estadual amazonense nº 8.142/2026 acrescentou à PERS/AM obrigações ligadas à contenç
- Um município quer aplicar a PNRS na origem da geração de resíduos. Qual medida está no top
- Qual alternativa apresenta o sentido de logística reversa na PNRS?
- Uma campanha educativa adota o padrão de cores CONAMA 275. Qual cor identifica o vidro?
- Uma clínica descarta agulhas utilizadas em procedimento. Qual grupo de RSS descreve o risc
- Qual material de obra é exemplo típico de RCC Classe A, conforme a CONAMA 307?
- A finalidade central da disposição final ambientalmente adequada, na terminologia da PNRS,
- Na RDC 222/2018, papel administrativo limpo e sem risco específico tende a integrar qual g
- Que consequência ambiental é típica da disposição inadequada de resíduos em lixões?
- Uma prefeitura aprovou seu PMGIRS e concluiu que uma indústria local já não precisa de seu
- Uma empresa afirma que reutilizou caixas retornáveis sem transformá-las. Esse processo cor
- Na triagem de RCC de uma reforma aparecem gesso, sucata metálica e concreto sem contaminaç
- Em uma unidade de saúde, determinado resíduo contém substância química perigosa e não apre
- O serviço de coleta recolheu 100 toneladas de recicláveis, mas 25 toneladas foram rejeitos
- Qual conjunto representa instrumentos ou mecanismos característicos da PNRS?
- A prefeitura montou lixeiras coloridas, mas mistura todas as frações no mesmo caminhão e n
- Em relação ao aterro controlado, a assertiva tecnicamente adequada é:
- O órgão ambiental exige demonstrar que 40 toneladas de plástico pós-consumo realmente fora
- Um edital usa a palavra rejeito para descrever plástico limpo separado e vendido a recicla
- Uma UBS possui apenas resíduos do Grupo D. A respeito do PGRSS, qual interpretação da RDC 
- Após demolição, uma construtora separou embalagens vazias com apenas filme seco de tinta i
- Uma prefeitura decide encerrar um lixão por meio de cobertura com terra e desloca a coleta
- O relatório de uma indústria indica resíduos perigosos de processo e resíduos comuns. Qual
- Uma unidade sanitária pretende descartar rejeito com radionuclídeos sujeitos ao controle r
- Uma construtora vence licitação para obra pública estadual no Amazonas e pretende deixar e
- No sistema de logística reversa do Amazonas, o que o Decreto estadual nº 50.890/2024 acres
```

## Pedido 49: Ciências da religião e ensino religioso (`religiao.ciencias-religiao-ensino-religioso`, 62 tópicos de edital)

```
Matéria: religiao.ciencias-religiao-ensino-religioso (Ciências da religião e ensino religioso)
Capítulos e quantas questões quero em cada um (total 100):
- "Ciências da Religião: objeto, métodos e fenomenologia": 17
- "Tradições abraâmicas: judaísmo, cristianismo e islamismo": 17
- "Matrizes afro-brasileiras, indígenas e tradições orientais": 17
- "Laicidade, liberdade religiosa e direitos fundamentais": 17
- "BNCC, didática do Ensino Religioso e avaliação": 16
- "Capelania, assistência religiosa e ética do cuidado": 16

Já existem (não repita):
- As Ciências da Religião podem estudar comunidades de fé sem exigir que o pesquisador aceit
- No vocabulário acadêmico, mito é necessariamente uma história comprovadamente falsa, razão
- Rudolf Otto e Mircea Eliade são referências históricas da fenomenologia da religião, embor
- Uma descrição êmica utiliza prioritariamente categorias externas à comunidade estudada, en
- A teologia e as Ciências da Religião podem examinar textos e práticas semelhantes, embora 
- O Talmude corresponde à primeira parte do Tanakh, composta pelos cinco livros tradicionalm
- O Concílio Vaticano II é associado a reflexões católicas sobre liturgia, ecumenismo e libe
- Na tradição islâmica, sunna e Alcorão são expressões absolutamente sinônimas e designam o 
- Candomblé e Umbanda possuem necessariamente o mesmo culto, a mesma estrutura ritual e uma 
- Na análise de cosmologias indígenas, identificar o povo e o contexto das práticas reduz o 
- A doutrina budista de anatta implica, em todas as suas escolas, a existência obrigatória d
- O caráter laico do Estado brasileiro significa que autoridades públicas devem adotar insti
- A Constituição prevê o ensino religioso de matrícula facultativa nos horários normais das 
- A Constituição veda que os entes federativos estabeleçam cultos ou igrejas, mas prevê uma 
- Após a ADI 4439, o Supremo Tribunal Federal passou a exigir que todo ensino religioso nas 
- A BNCC trabalha o Ensino Religioso com as unidades Identidades e alteridades, Manifestaçõe
- Para avaliar adequadamente a aprendizagem em Ensino Religioso, é legítimo atribuir maior n
- Todas as três unidades temáticas do Ensino Religioso precisam ocupar idêntica carga horári
- A legislação federal de assistência religiosa em hospitais e prisões impõe compatibilizaçã
- Por prever capelães militares, a lei do SARFA estabelece uma confissão religiosa oficial e
- Em uma pesquisa sobre rituais locais, qual procedimento privilegia uma abordagem êmica?
- O conceito de hierofania, empregado por Mircea Eliade, refere-se principalmente a qual ide
- Uma pesquisadora compara narrativas orais de comunidades distintas. Qual conduta tem maior
- Ao explicar o termo mito para uma turma, o professor deve destacar que ele pode ser entend
- Qual contraste entre Ciências da Religião e Teologia é metodologicamente mais adequado?
- Na organização das escrituras judaicas, a Torá corresponde, em sentido estrito, a:
- Em formulação clássica do islamismo sunita, qual prática integra os cinco pilares?
- A respeito do Concílio Vaticano II, assinale a afirmação historicamente adequada.
- Em uma prova sobre práticas católicas, qual definição distingue corretamente a Liturgia da
- Uma aula aborda o islamismo e deseja evitar generalizações. Qual afirmação é mais precisa?
- O professor pretende tratar religiões afro-brasileiras sem apagar sua diversidade. Qual di
- Em um projeto sobre espiritualidades indígenas, qual abordagem evita a essencialização?
- Qual conjunto sintetiza corretamente as Quatro Nobres Verdades em formulação budista cláss
- Em textos introdutórios sobre tradições hinduístas, como se deve compreender karma?
- Uma atividade escolar propõe reconstituir rito reservado de uma comunidade tradicional sem
- Uma pessoa internada solicita assistência espiritual. Qual previsão constitucional é diret
- Qual combinação corresponde ao art. 33 da LDB sobre ensino religioso em escolas públicas?
- Após a ADI 4439, uma banca afirma que o STF tornou o Ensino Religioso necessariamente não 
- Uma candidata invoca crença religiosa para afastar obrigação imposta a todos e também recu
- Uma escola registra hostilidade a alunos ligados a religiões afro-brasileiras. Qual respos
- Ao classificar um trabalho sobre reconhecimento de pertencimentos e respeito entre colegas
- Qual instrumento de avaliação corresponde melhor aos objetivos do Ensino Religioso na BNCC
- Numa turma plural, o professor pretende comparar Pessach, Páscoa e Ramadã. Qual planejamen
- Um líder religioso solicita ingresso em hospital para atendimento de paciente consciente. 
- Qual descrição está mais próxima da finalidade legal do Serviço de Assistência Religiosa n
```

## Pedido 50: PHP, Ajax e jQuery (`informatica.php-ajax-xml-jquery`, 62 tópicos de edital)

```
Matéria: informatica.php-ajax-xml-jquery (PHP, Ajax e jQuery)
Capítulos e quantas questões quero em cada um (total 100):
- "1. PHP: sintaxe, tipos, operadores e estruturas de programação": 19
- "2. PHP para aplicações web: requisições, sessões, persistência e segurança": 19
- "3. Ajax e jQuery: comunicação assíncrona, DOM e eventos": 21
- "4. XML: estrutura, validade, namespaces, XPath e PHP": 23
- "5. XSLT: regras de transformação e integração entre tecnologias": 18

Já existem (não repita):
- Um arquivo PHP declara `$preco = 8;` e depois executa `echo $preco;`. Onde ocorre a interp
- Um script PHP compara o número inteiro 7 com a string numérica '7', primeiro com `==` e de
- Uma página HTML envia `<form method='post'>` com um campo `name='email'`. Qual superglobal
- Um desenvolvedor usa `$('#mensagem').text('Pronto')` em página com jQuery. O que identific
- Um documento XML contém um elemento `raiz` que abre, mas é fechado com `</outro>`. Qual av
- Uma transformação XSLT deve copiar para a saída o valor textual do elemento `titulo` selec
- Uma rotina JavaScript recebe resposta HTTP 404 ao executar fetch, embora a conexão tenha f
- Um aplicativo PHP precisa consultar usuário por identificador informado em formulário. Qua
- Uma folha XSLT seleciona os elementos `<livro>` de um catálogo por `/catalogo/livro[1]`. Q
- O código PHP define `$quantidade = 0;` e executa `echo $quantidade ?? 10;`. O que será exi
- Um programa PHP recebe `Content-Type: application/json` com corpo `{ 'nome': 'Bia' }` após
- Em `$.ajax({ url: '/itens', method: 'GET', dataType: 'json' })`, o desenvolvedor deseja co
- Uma lista jQuery recebe novos botões `.remover` depois de a página carregar. Qual inscriçã
- O XML `<pedido><valor>abc</valor></pedido>` possui raiz única e tags corretas, mas um XSD 
- Um arquivo XML usa `<item xmlns='urn:loja' codigo='4'/>`. Como a regra de namespace padrão
- Uma transformação XSLT dispõe de um template específico para o elemento `produto`. Qual in
- Um script PHP imprime o nome fornecido em formulário dentro de um parágrafo HTML. Qual pro
- Em PHP, `$x = null; $y = 0;`. Uma rotina verifica `isset($x)`, `isset($y)`, `empty($y)` e 
- Um sistema PHP monta consulta `ORDER BY` com nome de coluna fornecido pelo usuário. A equi
- Em jQuery, `$.ajax({ url:'/api', dataType:'json' }).done(fn).fail(err)`. O servidor devolv
- Um script jQuery recebe de terceiros a string `<img src=x onerror=...>` e precisa exibi-la
- O XML possui `<p:item xmlns:p='urn:loja'/>` e outro documento possui `<q:item xmlns:q='urn
- Em um XSLT 1.0, o contexto atual é um elemento `<livro>` contendo `<titulo>Algoritmos</tit
- Uma aplicação realiza pesquisa no navegador para servidor de origem diferente; a requisiçã
- Uma folha XSLT 1.0 define template `match='/'` e outra regra `match='livro'`. O primeiro c
- Um script PHP contém `$cor = 'azul';` e `$Cor = 'verde';`. Ao executar `echo $cor;`, a saí
- Em PHP, a expressão `'Ana' + 'Silva'` é a forma prevista para concatenar os dois textos e 
- Uma requisição enviada por formulário HTML com method GET pode disponibilizar o valor de u
- Quando um objeto XMLHttpRequest alcança readyState 4, o protocolo garante que o servidor d
- A expressão jQuery `$('.ativo')` seleciona elementos identificados pela classe CSS `ativo`
- Um documento XML com dois elementos superiores independentes, `<a></a><b></b>`, está bem f
- Uma variável PHP definida com zero pode fazer `isset($valor)` e `empty($valor)` retornarem
- Ao receber um corpo JSON com Content-Type application/json, o PHP converte automaticamente
- A instrução `$.ajax({url:'/dados', dataType:'json'})` informa à biblioteca o tipo esperado
- Um evento de clique registrado com `$('.botao').on('click', fn)` na carga inicial da págin
- No XPath 1.0, `/loja/produto[1]` identifica o primeiro elemento produto filho de loja no c
- Um XML bem formado que contém `<valor>abc</valor>` é necessariamente válido perante qualqu
- Em uma transformação XSLT, a instrução `xsl:for-each` pode percorrer nós selecionados por 
- Um prepared statement PDO com marcador `:ordem` pode vincular de forma segura qualquer nom
- Uma resposta HTTP 200 com corpo incompatível com JSON pode levar o jqXHR de `$.ajax({dataT
- Em XML com namespace padrão declarado por `xmlns='urn:itens'`, tanto os elementos não pref
- Duas etiquetas XML com prefixos diferentes, mas associadas ao mesmo URI de namespace e com
- Quando XSLT executa `xsl:value-of select='titulo'`, o elemento titulo e todos os seus atri
- Em uma folha XSLT 1.0 aplicada a documento XML completo, o padrão `match='/'` corresponde 
- Se um servidor remoto exige preflight CORS, um script jQuery pode contornar autorização ne
```

