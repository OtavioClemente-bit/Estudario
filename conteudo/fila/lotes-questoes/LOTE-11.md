# Pedidos de questões extras, lote 11 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 10292 tópicos de edital.

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

## Pedido 1: ICMS e impostos estaduais (IPVA e ITCMD) (`direito-tributario.icms-impostos-estaduais`, 214 tópicos de edital)

```
Matéria: direito-tributario.icms-impostos-estaduais (ICMS e impostos estaduais (IPVA e ITCMD))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Competência estadual, materialidade do ICMS e Lei Kandir": 20
- "2. Não cumulatividade, créditos, estorno e responsabilidade": 21
- "3. Interestaduais, DIFAL, convênios e convalidação de incentivos": 17
- "4. Combustíveis, IPVA e ITCMD na Constituição atualizada": 29
- "5. Revisão integrada: casos, exceções e transição ICMS–IBS": 13

Já existem (não repita):
- Uma sociedade comercial vende mercadorias e outra presta transporte entre dois municípios.
- Uma pessoa física importa equipamento para uso doméstico e não exerce comércio habitual. Q
- Uma empresa vende lote a cliente no exterior e pergunta se deve estornar, pela não incidên
- Em período de apuração, uma loja possui débito regular de ICMS de R$ 2.300 e créditos váli
- Uma distribuidora pretende apropriar integralmente, no primeiro mês, o ICMS creditável da 
- Uma indústria transfere mercadorias da matriz para filial de mesmo titular em outro Estado
- Numa venda interestadual a consumidor final que não é contribuinte do ICMS, o Estado de de
- Uma unidade federada deseja conceder incentivo de ICMS mediante redução de base de cálculo
- Uma pessoa adquire um veículo para uso pessoal e outra recebe imóvel como herança. Quais i
- Uma companhia oferece transporte aéreo interestadual de passageiros, hipótese específica a
- Uma fábrica compra matéria-prima tributada, produz mercadoria e a exporta diretamente. O s
- Um Estado pretende aprovar convênio excepcional da LC 160/2017 para regularizar incentivos
- Um convênio ordinário de concessão de isenção de ICMS foi publicado. Um Estado não edita d
- Um produtor vende combustível incluído no rol submetido ao regime da LC 192/2022. Ao anali
- Uma unidade federada pretende justificar alíquota de ICMS superior à alíquota geral para e
- Um comerciante paulista envia mercadoria nacional ordinária a comprador localizado na Bahi
- Uma empresa com dois estabelecimentos de mesma titularidade avalia tratar suas transferênc
- Uma indústria compra mercadorias para uso e consumo geral em 2026 e pretende compensar int
- Uma família possui automóvel de passageiros fabricado há 22 anos; uma transportadora possu
- Um doador reside no exterior e transmite gratuitamente títulos a beneficiário domiciliado 
- Uma mercadoria importada é desembaraçada e destinada a estabelecimento adquirente localiza
- Um estabelecimento adquire mercadoria com crédito regular para revenda, mas depois realiza
- Uma unidade federada institui crédito presumido de ICMS para determinado setor sem observa
- Uma indústria situada em um Estado vende bem a consumidor final contribuinte do ICMS, loca
- Durante a transição estabelecida pela EC 132/2023, uma empresa precisa identificar a natur
- Uma empresa de transporte presta serviço remunerado entre dois municípios do mesmo Estado.
- Uma pessoa física transporta mudança entre bairros de sua cidade por empresa contratada. A
- Em operação de importação destinada a consumidor que não negocia mercadorias habitualmente
- Uma mercadoria é exportada depois de a empresa obter créditos de ICMS nas etapas anteriore
- A redução da base de cálculo de ICMS que diminui a carga tributária integra, em regra, os 
- Para conceder novo benefício de ICMS pela disciplina ordinária da LC 24/1975, é suficiente
- A LC 194/2022 incluiu na Lei Kandir regra que reconhece combustíveis, energia elétrica, co
- Uma empresa compra material para seu uso e consumo geral em 2026 e pretende aproveitar o c
- Ao registrar máquina como ativo permanente, um contribuinte deve observar, para crédito de
- A EC 132/2023 determinou que o IPVA não poderia incidir sobre qualquer embarcação de lazer
- Em operação interestadual com mercadoria comum destinada a consumidor final não contribuin
- Depois da EC 132/2023, a transmissão por herança de bem móvel ou título é sempre tributada
- A LC 160/2017 estabelece que o convênio especial de regularização de benefícios pretéritos
- A monofasia do ICMS estabelecida pela LC 192/2022 significa que todos os combustíveis e to
- A Constituição, após a EC 137/2025, exclui do IPVA veículos terrestres de passageiros, cam
- A progressividade do ITCMD conforme o valor do quinhão, legado ou doação tornou-se vedada 
- Um contribuinte pretende aproveitar crédito de ICMS regularmente documentado seis anos apó
- Na substituição tributária para frente, o imposto antecipado não pode ser restituído quand
- O IBS previsto no art. 156-A da Constituição possui competência compartilhada entre Estado
- A Resolução do Senado nº 22/1989 prevê alíquota interestadual de 7% para mercadoria comum 
```

## Pedido 2: Produção audiovisual e telejornalismo (`comunicacao.producao-audiovisual-telejornalismo`, 214 tópicos de edital)

```
Matéria: comunicacao.producao-audiovisual-telejornalismo (Produção audiovisual e telejornalismo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Linguagem audiovisual e narrativa jornalística": 20
- "2. Pré-produção, produção e operação de câmera": 20
- "3. Iluminação, som direto e microfones": 21
- "4. Reportagem, telejornal em estúdio e streaming": 20
- "5. Montagem, softwares, correção e entrega": 19

Já existem (não repita):
- Uma reportagem sobre obra viária precisa mostrar o conjunto do cruzamento antes de aproxim
- Na edição de entrevista, é preciso cobrir com imagens do local uma narração gravada poster
- Antes de entrevistar moradores de área alagada, a equipe escolhe fontes, verifica acesso, 
- A gravação externa está excessivamente clara, mas a equipe deseja manter abertura ampla pa
- Num estúdio, a luz principal modela o rosto, mas a sombra do lado oposto está profunda. Qu
- Uma entrevista de rua exige que o repórter aproxime o equipamento alternadamente de sua pr
- Na mesa de corte de um telejornal, uma câmera já aparece na transmissão e outra está prepa
- Após a edição, o profissional exporta vídeo usando H.264 dentro de arquivo MP4. Qual assoc
- Duas câmeras filmaram diálogo frente a frente; a segunda foi colocada do lado oposto da li
- Uma reportagem filmada para televisão será adaptada para vídeo vertical. A entrevista orig
- Em gravação a 25 quadros por segundo, a equipe busca aparência usual de desfoque de movime
- Uma equipe grava 60 segundos com vídeo de 8 megabits por segundo, desconsiderando áudio e 
- O rosto da entrevistada está bem exposto, mas muda de tonalidade entre dois ângulos, um il
- Duas pessoas usam transmissores sem fio em entrevista, e o editor precisa tratar suas voze
- O entrevistado fala alto repentinamente; o gravador registra achatamento dos picos e som d
- O envio ao vivo apresenta frames descartados associados a upload instável, embora a CPU te
- O telejornal possui duas câmeras gravadas e um áudio guia comum; na edição deseja-se troca
- Um editor prepara uma versão curta de depoimento. O áudio da próxima cena começa enquanto 
- Para captar em local público uma entrevista com duas pessoas, um microfone direcional está
- Dois sinais com o mesmo enquadramento apresentam tonalidades diferentes na montagem. O edi
- Uma transmissão usa vídeo de 5 Mb/s e áudio de 0,2 Mb/s. A rede oferece upload medido de 5
- Na edição multicâmera do Final Cut Pro, o profissional precisa sincronizar ângulos gravado
- Um plano de reportagem de arquivo é inserido junto a imagens recém-gravadas do mesmo lugar
- Uma equipe utiliza softbox grande junto ao rosto de uma fonte. Em comparação com fonte apa
- Na entrega final de uma reportagem, um arquivo MP4 tem imagem normal, mas a fala fica apen
- Durante a cobertura de uma sessão, a equipe grava um plano aberto do ambiente para orienta
- Uma equipe só deve decidir os entrevistados e a lista de equipamentos quando estiver com o
- Em entrevista filmada, passar de f/8 para f/2.8, conservadas as demais condições relevante
- Em uma reportagem externa, a função principal da luz de preenchimento é criar um contorno 
- Se um depoimento saturou a entrada do gravador, baixar o fader da faixa no software recupe
- Em um programa ao vivo, o monitor program é associado ao sinal selecionado para transmissã
- No fluxo de edição, guardar o arquivo de projeto no computador dispensa fazer cópia dos ví
- Ao adaptar um vídeo 16:9 para publicação 9:16, é necessário revisar enquadramentos e legen
- Em gravação a 25 fps, o ajuste de 1/100 s tende a gerar mais desfoque por movimento do que
- Uma gravação externa com duas vozes em canais separados permite que o editor ajuste níveis
- Ao investigar queda de frames em streaming, o operador pode tratar a taxa de upload da red
- Num projeto multicâmera, alinhar tomadas por timecode, evento comum ou áudio compartilhado
- Em uma sequência J-cut, a imagem da próxima cena antecede o áudio que lhe pertence, enquan
- Em uma entrevista em local reverberante, aproximar adequadamente o microfone de quem fala 
- Dois arquivos MP4 exportados com resolução idêntica necessariamente empregam o mesmo codec
- Ao cruzar o eixo de uma conversa com movimento visível e novo plano de orientação, o edito
- Um canal de segurança gravado 10 dB abaixo do principal recupera com certeza a fala quando
- Uma equipe que grava matéria em resolução elevada pode editar com arquivos proxy leves e, 
- A versão final de uma reportagem que contém imagens de arquivo precisa receber exatamente 
- Uma transmissão pode ter upload nominal superior à soma das taxas de vídeo e áudio e ainda
```

## Pedido 3: Funções exponencial e logarítmica (`matematica.funcao-exponencial-logaritmica`, 214 tópicos de edital)

```
Matéria: matematica.funcao-exponencial-logaritmica (Funções exponencial e logarítmica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Função exponencial: definição, propriedades e gráficos": 22
- "2. Equações e inequações exponenciais": 23
- "3. Logaritmos, propriedades e função inversa": 21
- "4. Equações e inequações logarítmicas com condições de existência": 21
- "5. Aplicações de crescimento, decaimento e relação exponencial-logarítmica": 13

Já existem (não repita):
- Uma cultura matemática é representada por $$f(t)=3^t$$. Após dois períodos completos, qual
- Um gráfico representa $$f(x)=\left(\frac14\right)^x$$. Quando a variável real $$x$$ aument
- Uma variável deve satisfazer a expressão real $$\log_5(x-4)$$. Qual condição define corret
- Uma tabela solicita o resultado do logaritmo $$\log_2 16$$. Que valor deve ser registrado?
- O crescimento de uma grandeza é modelado por $$2^x=32$$. Qual valor real de $$x$$ satisfaz
- Um gráfico exibe a função exponencial básica $$f(x)=4^x$$ para todos os valores reais da v
- Uma tabela contém $$\log_7 1$$. Qual número deve ser usado, respeitando a definição de log
- Uma amostra possui inicialmente 200 unidades e conserva metade da quantidade após cada per
- Um indicador deve satisfazer $$2^{x+1}\le16$$. Como a base é crescente, qual é o conjunto 
- A condição de funcionamento de um modelo é $$\left(\frac13\right)^{x-2}>\frac19$$. Qual re
- Para que um número passe no teste $$\log_2(x+2)>2$$, qual condição sobre $$x$$ deve ser sa
- Considere $$\log_{1/2}(x-1)\ge-1$$. Qual conjunto reúne os valores de $$x$$ que respeitam 
- Uma incógnita aparece em $$9^{x-1}=27$$. Reescrevendo ambos os lados na base 3, qual valor
- Uma análise exige resolver $$\log_3(x-1)=\log_3(7-x)$$. Qual valor é a solução real admiss
- Uma população-modelo inicia com 100 indivíduos e cresce 20% a cada período, de forma compo
- No estudo de bases, uma pessoa precisa calcular $$\log_4 8$$. Qual igualdade permite obter
- Um modelo satisfaz $$2^{2x}-5\cdot2^x+4=0$$. Ao fazer $$u=2^x$$, quais são todas as soluçõ
- A condição de comparação é $$2^x>4^{x-1}$$. Depois de expressar as potências na base 2, qu
- Uma expressão só é aceita se $$\log_3(x-2)\le1$$. Qual intervalo leva em conta o domínio e
- Uma inequação usa base decrescente: $$\log_{1/2}(3-x)>1$$. Qual intervalo contém exatament
- Dado $$\log_2(x-1)+\log_2(x+1)=3$$, qual solução real satisfaz a equação e todas as condiç
- Uma amostra tem massa inicial de 80 g e meia-vida de duas horas. Quanto tempo decorre até 
- Uma análise produz $$\log_2(x^2-9)=4$$. Quais soluções reais são válidas após avaliar o do
- Um sistema exige $$\log_2(x+1)>\log_2(3-x)$$. Qual conjunto-solução considera os dois argu
- A comparação de dois modelos resulta em $$4^{x+1}\le8^{x-1}$$. Qual desigualdade final é e
- Uma função exponencial básica $$f(x)=5^x$$, com base admissível, assume valor $$1$$ quando
- Em uma tabela de $$f(x)=2^x$$, as entradas negativas produzem necessariamente saídas negat
- Para qualquer base real positiva e diferente de 1, o logaritmo de 1 nessa base vale 0, poi
- A expressão $$\log_2(-4)$$ tem valor $$-2$$ nos números reais, uma vez que $$2^{-2}=\frac1
- Uma quantidade representada por $$P(t)=100\cdot(0{,}8)^t$$ diminui à medida que $$t$$ aume
- Em uma avaliação numérica, o valor de $$\log_2 8$$ é 4, pois a base 2 precisa ser multipli
- No modelo $$2^x=16$$, a solução real é $$x=4$$, pois a base 2 elevada à quarta potência pr
- A solução de $$\log_3(x-1)\ge0$$ é o conjunto $$x\ge1$$, bastando permitir que o argumento
- Para base igual a $$\frac12$$, a condição $$\left(\frac12\right)^x\ge\left(\frac12\right)^
- Para $$u>0$$ e $$v>0$$, vale a igualdade $$\log_2(u+v)=\log_2u+\log_2v$$, pela propriedade
- As funções $$f(x)=3^x$$ e $$g(x)=\log_3 x$$ são inversas, e seus gráficos se refletem em r
- Se uma massa sofre duas meias-vidas sucessivas, a quantidade restante será zero, porque ca
- Na inequação $$\log_2(x-2)>1$$, o conjunto solução é $$x>4$$, pois a base cresce e o argum
- A identidade $$2^{x+1}=2^x+1$$ vale para todo real, porque a soma no expoente corresponde 
- A solução de $$\log_{1/2}(x+1)\ge1$$ é $$-1<x\le-\frac12$$, após respeitar o argumento pos
- A equação $$\log_2(x^2-9)=4$$ possui apenas a raiz $$x=5$$, porque a definição de logaritm
- A inequação $$\log_2(x+1)>\log_2(3-x)$$ possui conjunto-solução $$1<x<3$$, porque se exige
- A comparação $$4^{x+1}\le8^{x-1}$$ é equivalente a $$x\le5$$, pois ambas as bases são maio
- Uma população-modelo de 100 unidades cresce 10% por período. Para alcançar pelo menos 121 
- A expressão $$\log_3((x-1)(x+2))$$ somente existe quando ambos os fatores $$x-1$$ e $$x+2$
```

## Pedido 4: Máquinas marítimas, sistemas auxiliares e equipamentos do navio (`engenharia-naval.maquinas-sistemas-navio`, 213 tópicos de edital)

```
Matéria: engenharia-naval.maquinas-sistemas-navio (Máquinas marítimas, sistemas auxiliares e equipamentos do navio)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Instalações propulsoras e motores de combustão interna": 26
- "2. Sistemas de óleo, resfriamento, balanço térmico, ventilação e vapor": 27
- "3. Energia elétrica, ar comprimido e equipamentos de convés": 24
- "4. Segurança, salvatagem e MARPOL": 23

Já existem (não repita):
- Em grandes navios mercantes, como petroleiros e graneleiros, é comum o uso de motor diesel
- O componente da linha de eixos responsável por transmitir ao casco o empuxo produzido pelo
- O hélice de passo controlável permite variar o empuxo e inverter o sentido do movimento do
- No motor de ciclo Diesel, a ignição do combustível ocorre:
- O motor de dois tempos completa um ciclo de trabalho em:
- O turbocompressor dos motores marítimos é acionado pelos gases de escape e comprime o ar d
- Pela MARPOL, Anexo VI, desde 2020 o teor máximo de enxofre do combustível usado a bordo fo
- A propulsão diesel-elétrica, em que geradores alimentam motores elétricos de propulsão, é 
- No sistema de óleo combustível pesado, o equipamento que controla o aquecimento para que o
- No sistema de óleo combustível, o tanque de decantação, aquecido, permite que água e borra
- Em um sistema central de resfriamento, as camisas e os cabeçotes do motor são resfriados d
- No balanço térmico de um grande motor diesel marítimo de dois tempos, a parcela da energia
- NÃO é forma de recuperação do calor rejeitado pelo motor principal:
- Antes da descarga do sistema fixo de CO₂ na praça de máquinas, os ventiladores devem ser p
- Em navios petroleiros, o vapor gerado a bordo é tipicamente utilizado para:
- No circuito de vapor de bordo, o tanque de observação (cascata) permite detectar a contami
- Para colocar um gerador em paralelo com outro que já alimenta o barramento, NÃO é condição
- Pela SOLAS, o gerador de emergência deve ter partida automática e ser instalado fora da pr
- O gerador acionado pelo próprio motor principal, que aproveita sua alta eficiência para su
- As garrafas de ar de partida dos motores marítimos são tipicamente mantidas a pressões da 
- O desligamento preferencial (preferential trip) retira automaticamente cargas não essencia
- No aparelho de fundeio, o dispositivo que trava a amarra, aliviando o esforço sobre o moli
- Em navios petroleiros, a conexão das linhas de carga do navio com as mangueiras ou braços 
- Os guinchos de amarração, os cabeços e as buzinas são equipamentos de convés usados princi
- Pela MARPOL, Anexo I, o efluente do separador de água e óleo das águas de porão só pode se
- A borra retirada pelas purificadoras e o óleo separado no tratamento das águas de porão de
- Para a descarga de misturas oleosas provenientes da área de carga de petroleiros, a MARPOL
- Pela MARPOL, Anexo V, a descarga de plásticos no mar é proibida, inclusive em alto-mar, lo
- O sistema que mantém a atmosfera dos tanques de carga de petroleiros com baixo teor de oxi
- Em navios petroleiros, as baleeiras devem ser do tipo totalmente fechado e com proteção co
- O equipamento de salvatagem que, ativado em emergência, transmite via satélite a posição d
- As balsas salva-vidas infláveis podem ser equipadas com dispositivo de liberação hidrostát
- NÃO integra os meios fixos ou portáteis de combate a incêndio exigidos pela SOLAS a bordo:
- Os exercícios periódicos de abandono do navio e de combate a incêndio, com rol de chamada 
- Nos grandes motores diesel de dois tempos com cruzeta, o óleo de cilindro é injetado separ
- A ventilação da praça de máquinas tem como finalidades principais:
- Num grande motor diesel de dois tempos, depois do trabalho útil, a maior parcela da energi
- Se um gerador for ligado ao barramento fora de fase, sem sincronismo, pode haver corrente 
- A principal razão para usar hélice de passo controlável em conjunto com gerador de eixo é:
- O quadro elétrico principal recebe a energia dos geradores e a distribui aos consumidores 
```

## Pedido 5: Redes sem fio e VoIP (`informatica.redes-sem-fio-voip`, 213 tópicos de edital)

```
Matéria: informatica.redes-sem-fio-voip (Redes sem fio e VoIP)
Capítulos e quantas questões quero em cada um (total 100):
- "Redes sem fio: classificação e funcionamento do Wi-Fi": 16
- "Padrões IEEE 802.11, faixas e canais": 20
- "Segurança sem fio: WEP, WPA, WPA2, WPA3, 802.1X e ataques": 24
- "VoIP: voz em pacotes, protocolos e qualidade": 26
- "Como o tema costuma ser cobrado": 14

Já existem (não repita):
- Um celular conectado a um fone de ouvido sem fio, a poucos centímetros de distância, forma
- Em uma rede Wi-Fi no modo infraestrutura, as estações se comunicam por meio de um ponto de
- O nome de uma rede Wi-Fi, exibido na lista de redes disponíveis do celular, corresponde a 
- Ocultar o SSID, retirando o nome da rede dos beacons, é uma medida que, por si só, autenti
- Por que as redes 802.11 usam CSMA/CA, e não o CSMA/CD das redes Ethernet antigas, para con
- O mecanismo opcional RTS/CTS ajuda a reduzir colisões causadas pelo problema do terminal o
- Uma empresa instalou três pontos de acesso com o mesmo SSID e a mesma configuração de segu
- O Bluetooth opera na faixa de 5 GHz, o que o impede de sofrer ou causar interferência com 
- Bluesnarfing é o ataque em que alguém, explorando falhas do Bluetooth, obtém sem autorizaç
- Qual padrão IEEE recebeu da Wi-Fi Alliance o nome comercial Wi-Fi 6?
- O padrão 802.11ac (Wi-Fi 5) opera nas faixas de 2,4 GHz e 5 GHz, assim como o 802.11n.
- Na faixa de 2,4 GHz, com canais de 20 MHz, qual conjunto de canais costuma ser recomendado
- Em comparação com a faixa de 5 GHz, a faixa de 2,4 GHz costuma oferecer maior alcance e me
- Que tecnologia, introduzida no 802.11n, usa várias antenas transmitindo fluxos espaciais s
- Um auditório com centenas de celulares conectados sofre com lentidão. Qual recurso do 802.
- O Wi-Fi 6E corresponde ao 802.11ax operando também na faixa de 6 GHz, o que acrescenta can
- Uma rede tem um ponto de acesso configurado para irradiar apenas em 5 GHz. Qual dos client
- O padrão 802.11a opera na faixa de 2,4 GHz com taxa nominal de 54 Mbps e, por usar a mesma
- Entre os mecanismos listados, qual é a geração mais recente de segurança para redes Wi-Fi 
- O WEP é considerado inseguro, porque falhas no uso do RC4 e o vetor de inicialização curto
- Qual par associa corretamente o protocolo de segurança sem fio à criptografia que ele usa 
- No WPA2-Personal, todos os usuários compartilham uma mesma senha, enquanto o WPA2-Enterpri
- No controle de acesso 802.1X da rede sem fio de uma empresa, quais são os papéis do notebo
- Uma organização quer que, na rede sem fio, tanto o servidor quanto cada cliente se autenti
- Capturar o handshake de quatro vias de uma rede WPA2-Personal permite ao atacante testar s
- O WPS por PIN é recomendado como alternativa segura à senha WPA2, pois o PIN de oito dígit
- Num aeroporto, um atacante instala um ponto de acesso com o mesmo nome da rede oficial e s
- Um atacante envia quadros falsos de desautenticação em nome do ponto de acesso, derrubando
- Filtrar endereços MAC no ponto de acesso é insuficiente como única medida de segurança, po
- O padrão IEEE 802.1X é exclusivo das redes sem fio e não pode ser usado para controlar o a
- Qual protocolo transporta o áudio da conversa em uma chamada VoIP, levando número de sequê
- No VoIP a voz é digitalizada e enviada em pacotes por uma rede IP, ao contrário da telefon
- Em uma chamada SIP, qual requisição o telefone de quem liga envia para convidar o outro la
- Uma resposta SIP com código 180 Ringing indica que a chamada foi atendida e que a mídia já
- Por que o áudio de uma chamada VoIP normalmente trafega sobre UDP, e não sobre TCP?
- Uma chamada usa o codec G.711 (64 kbps) com um pacote a cada 20 ms. Cada pacote leva 160 b
- Com o codec G.729 (8 kbps), um pacote a cada 20 ms e 40 bytes de cabeçalhos IP, UDP e RTP 
- Na avaliação da qualidade de uma chamada VoIP, o que é o jitter?
- Aumentar bastante o tamanho do jitter buffer reduz o efeito do jitter sem nenhum custo par
- Sobre os protocolos de sinalização SIP e H.323, qual afirmação está correta?
- Qual equipamento permite ligar um telefone analógico comum a uma rede VoIP?
- O RTCP retransmite os pacotes de voz que o RTP identifica como perdidos pelo número de seq
- Uma sala de reuniões com dezenas de notebooks recentes tem a rede em 2,4 GHz lenta e conge
- Usuários relatam que, nas chamadas VoIP, a voz chega picotada nos horários de pico de uso 
- Qual código de resposta SIP indica sucesso, como quando o destinatário atende a chamada?
```

## Pedido 6: Química analítica: volumetria, gravimetria e análise instrumental (`quimica.analise-instrumental-gravimetria-volumetria`, 213 tópicos de edital)

```
Matéria: quimica.analise-instrumental-gravimetria-volumetria (Química analítica: volumetria, gravimetria e análise instrumental)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos analíticos, soluções, medidas e tratamento de resultados": 18
- "2. Titulometria, equilíbrios ácido-base e reações volumétricas": 21
- "3. Gravimetria, caracterização de sólidos e controle de impurezas": 19
- "4. Espectroscopias, eletroanálise, difração e equipamentos laboratoriais": 21
- "5. Cromatografia, calibração, validação e integridade dos dados": 21

Já existem (não repita):
- Um laboratório obtém cinco resultados muito próximos entre si para o teor de uma solução, 
- Ao preparar solução de NaOH para titular amostras ácidas, o laboratório deseja estabelecer
- Uma amostra aquosa contém sulfato, que será determinado gravimetricamente após precipitaçã
- Uma solução de ácido fraco é titulada com uma base forte à temperatura próxima de 25 °C. E
- Em uma placa de cromatografia de camada fina, a frente do solvente percorreu 8,0 cm e o ce
- Uma solução absorvente, medida nas mesmas condições e dentro da faixa linear, passa de 0,0
- Para medir potenciometricamente o pH, um equipamento utiliza um sistema combinado com duas
- Um tampão de ácido fraco e base conjugada possui concentrações iguais dessas espécies. Sob
- Um conjunto de medidas replicadas é usado para descrever sua dispersão amostral. Qual cálc
- Uma amostra de 20,0 mL de ácido monoprótico 0,100 mol/L consome 15,0 mL de NaOH até a equi
- O cloreto de uma amostra foi integralmente precipitado como 0,1433 g de AgCl, de massa mol
- Uma solução de Fe2+ é titulada com permanganato em meio suficientemente ácido, produzindo 
- Em uma titulação complexométrica de Ca2+ por EDTA em condições adequadas de pH, qual propo
- Um padrão tem absorbância de 0,800. Sua concentração é reduzida à metade, sem alterar o ca
- Após síntese química, é necessário medir traços de solventes orgânicos voláteis remanescen
- Dois lotes da mesma substância possuem fórmulas e composição iguais, mas padrões distintos
- Uma amostra exibe picos estreitos de difração em posições específicas, mas outra, de compo
- Uma mistura racêmica passa por HPLC de fase estacionária e fase móvel aquirais sem separaç
- Em amostra de matriz complexa, a mesma quantidade de analito produz sinal menor que em sol
- Uma base orgânica muito fraca não apresenta viragem satisfatória na titulação aquosa. Qual
- Cinco resultados independentes apresentam média 10,00 e desvio padrão amostral 0,20. Com t
- Uma matriz de produto contém traços de cádmio e chumbo que precisam ser avaliados separada
- Em HPLC, o tempo de retenção de um soluto é 6,0 min e o tempo morto é 2,0 min. Qual fator 
- Um procedimento mediu concentração média de 20,0 mg/L e desvio padrão de 0,12 mg/L. Qual d
- Uma análise de sulfato gera 0,23339 g de BaSO4 puro e seco. Considerando M(BaSO4)=233,39 g
- Ao ensaiar um ácido com uma base, a solução mostra a mudança de cor do indicador próxima à
- Uma sequência de resultados muito próximos prova, isoladamente, que não existe erro sistem
- Na gravimetria, a secagem ou calcinação do precipitado deve continuar até cumprir o critér
- Se uma amostra apresenta transmitância fracional de 0,10, sua absorbância é 0,10, porque a
- Um medidor de pH com eletrodo de vidro deve ser calibrado com tampões apropriados à faixa 
- Na cromatografia de camada fina, o fator Rf de uma mancha é calculado dividindo-se a distâ
- Em ensaio complexométrico com EDTA, um tampão pode ser indispensável, pois a protonação do
- Se uma curva de calibração produz coeficiente de determinação muito alto, está automaticam
- Em HPLC quantitativa, a área cromatográfica pode ser relacionada à concentração por curva 
- A absorção atômica mede primordialmente a absorção de radiação por moléculas intactas, raz
- Em precipitação gravimétrica, a digestão controlada pode reduzir a área superficial relati
- No preparo de solução padrão, duplicar o volume final sem acrescentar soluto duplica a con
- O controle de solventes residuais pode utilizar cromatografia gasosa e deve considerar ris
- Dois sólidos de mesma composição, um cristalino e outro amorfo, têm obrigatoriamente picos
- Para um composto volátil e termicamente estável presente em baixa concentração, a amostrag
- Em uma titulação de ácido fraco por base forte, o pH no ponto de equivalência deve ser pre
- A estratégia para impurezas elementares pode considerar possíveis fontes, exposição e capa
- Em validação analítica, um gráfico com resposta perfeitamente linear comprova seletividade
- Mantidas a variabilidade amostral e as condições de um intervalo bilateral de confiança, o
- Uma curva por adição de padrão, construída na matriz real, assegura por si só seletividade
```

## Pedido 7: Funções de que e se (`portugues.funcoes-que-se`, 213 tópicos de edital)

```
Matéria: portugues.funcoes-que-se (Funções de que e se)
Capítulos e quantas questões quero em cada um (total 100):
- "Usos e funções de que": 34
- "Usos e funções de se": 33
- "Distinção, concordância e análise em contexto": 33

Já existem (não repita):
- Em “A planilha que chegou ontem já foi conferida”, a palavra que retoma planilha e exerce 
- Na frase “A gerente informou que o arquivo foi enviado”, que é pronome relativo porque se 
- Em “O formulário de que precisamos está no balcão”, o relativo que é regido pela preposiçã
- Em “Não sabemos se a reunião começará cedo”, se é conjunção subordinativa condicional e ap
- Em “Se houver alteração no horário, a equipe será avisada”, a oração iniciada por se expre
- Em “Alugam-se salas para reuniões”, o verbo fica no singular porque o sujeito é indetermin
- Em “Precisa-se de pessoas com experiência”, o verbo permanece no singular e o se indetermi
- Em “As duas colegas se cumprimentaram”, a leitura recíproca equivale a dizer que uma cumpr
- Em “A testemunha que depôs foi ouvida novamente”, que exerce função de objeto direto de de
- Em “O aviso foi tão claro que ninguém pediu explicação”, que introduz uma consequência do 
- Em “O técnico que revisou o laudo trabalha aqui”, a oração relativa entre vírgulas é expli
- Em “Há um quê de urgência no bilhete”, quê é substantivo, como indica a presença do artigo
- Em “Trabalha-se com atenção neste setor”, se é partícula apassivadora e atenção é sujeito 
- Em “O relatório, que estava assinado, foi arquivado”, as vírgulas delimitam uma oração rel
- Em “É-se cuidadoso ao conferir valores”, o se pode indeterminar o sujeito e o verbo de lig
- Em “A pasta que foi enviada contém o original”, que retoma um nome anterior e introduz uma
- Considere “A recepcionista confirmou que o visitante chegou”. Como deve ser classificado o
- Na pergunta direta “Que arquivo foi atualizado?”, que acompanha o nome arquivo e solicita 
- Leia: “O crachá de que dependo está guardado”. Qual análise explica a preposição antes do 
- Em “A fila estava tão extensa que algumas pessoas voltaram depois”, que introduz uma relaç
- Analise “Os dois supervisores se elogiaram após a apresentação”. Qual leitura do pronome s
- Em “Divulgaram-se os novos horários”, qual análise respeita a concordância e a estrutura d
- Considere “Não ficou claro se o pedido foi recebido”. A oração introduzida por se completa
- Na frase “Vive-se com tranquilidade naquela rua”, que função desempenha se?
- Em “A analista que assinou o parecer enviou-o ao arquivo”, qual é a função sintática do pr
- Em “O setor a que me dirigi estava fechado”, por que a preposição a aparece antes do relat
- Leia “Que surpresa agradável!”, em que que acompanha o substantivo surpresa numa exclamaçã
- Em “É necessário que todos confiram os dados”, a oração iniciada por que funciona como suj
- Na frase “O relatório informa que houve atraso”, qual elemento é completado pela oração in
- Compare “Vendem-se exemplares” e “Precisa-se de exemplares”. Qual alternativa diferencia c
- Em “A funcionária se arrependeu do comentário”, qual análise evita atribuir ao pronome um 
- Em “Os comunicados que foram impressos chegaram cedo”, qual opção descreve corretamente o 
- A forma “quê” ocorre substantivada em “Ela demonstrou um quê de preocupação”. Qual alterna
- Em “Foi a recepcionista que localizou o envelope”, a retirada de que pode preservar a idei
- Em “A funcionária se feriu durante a montagem”, qual paráfrase explicita o valor reflexivo
- Em “Que longe fica a unidade de atendimento!”, que modifica longe e intensifica a distânci
- Em “Obedece-se às normas de segurança”, a preposição às sinaliza a regência do verbo. Qual
- No aviso “Se houver divergência entre os dados, o pedido será devolvido”, a devolução depe
- Em “A ata que redigimos foi aprovada”, que é pronome relativo e exerce função de objeto di
- Em “Se os dados coincidirem, o pedido seguirá para análise”, qual substituição conserva o 
- Em “Solicitou-se revisão dos cálculos”, revisão dos cálculos pode ser sujeito paciente, pe
- Em “O recado que a equipe recebeu chegou incompleto”, qual função sintática que exerce na 
- Na frase “Foi ontem que o sistema registrou a entrada”, a sequência foi ontem que focaliza
- Em “Os formulários devem-se preencher antes do atendimento”, a leitura pretendida é passiv
```

## Pedido 8: Cirurgia pediátrica (`medicina.cirurgia-pediatrica`, 213 tópicos de edital)

```
Matéria: medicina.cirurgia-pediatrica (Cirurgia pediátrica)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, avaliação e urgências do recém-nascido": 17
- "Trato digestivo: atresias, motilidade e abdome agudo": 17
- "Parede abdominal, hérnias e região inguinoescrotal": 17
- "Urologia cirúrgica, genitália e emergência escrotal": 17
- "Tórax, pescoço e tumores sólidos da infância": 16
- "Trauma pediátrico e cuidados perioperatórios": 16

Já existem (não repita):
- Em recém-nascido, vômito de coloração verde-escura impõe investigação urgente de possível 
- Na suspeita de atresia esofágica, a alimentação por via oral deve ser mantida para testar 
- O reparo da hérnia diafragmática congênita costuma ser precedido de estabilização cardiopu
- Radiografia abdominal sem alterações importantes exclui vólvulo de intestino médio em neon
- Na doença de Hirschsprung, o enema contrastado pode sugerir zona de transição, mas o diagn
- Na estenose hipertrófica do piloro, o padrão clássico é vômito verde bilioso acompanhado d
- A presença de perfuração intestinal ou peritonite contraindica tentativa habitual de reduç
- Todo divertículo de Meckel produz sangramento, sendo impossível que se manifeste como obst
- A gastrosquise costuma apresentar exteriorização intestinal paraumbilical sem saco de cobe
- Por possuir origem congênita, toda hérnia umbilical assintomática em lactente exige reparo
- Hérnia inguinal na criança é frequentemente indireta e se relaciona à persistência do proc
- Diante de massa inguinal dolorosa, irredutível e com sinais sistêmicos, a redução manual v
- Hidronefrose detectada no pré-natal é um achado de dilatação e, isoladamente, não prova ob
- Hipospádia deve ser seguida de circuncisão imediata, pois o prepúcio não participa das pos
- Suspeita clínica elevada de torção testicular justifica exploração cirúrgica urgente, sem 
- Todo grau de refluxo vesicoureteral pediátrico precisa de reimplante ureteral, ainda que n
- Cisto do ducto tireoglosso é uma massa geralmente mediana e pode mover-se com a deglutição
- O torcicolo muscular congênito exige cirurgia imediata em todos os recém-nascidos, porque 
- A amplificação de MYCN é um dos fatores biológicos usados na estratificação de risco do ne
- Para tumor de Wilms unilateral potencialmente ressecável, existe apenas uma sequência tera
- Na avaliação primária do trauma pediátrico, o controle de hemorragia catastrófica e as pri
- FAST/eFAST negativo em criança com trauma abdominal possui capacidade suficiente para excl
- Em lesão de baço pediátrica hemodinamicamente estável, tratamento não operatório com monit
- Após cirurgia digestiva de recém-nascido, a alta pode ser baseada apenas em cicatrização d
- Recém-nascido apresenta salivação abundante, engasgos e impossibilidade de avançar sonda o
- Ao nascer, lactente com diagnóstico antenatal de hérnia diafragmática apresenta insuficiên
- Sobre associação VACTERL em crianças com fístula traqueoesofágica, selecione a interpretaç
- Um neonato com vômito verde apresenta choque e piora abdominal rápida. Entre os passos aba
- Radiografia de neonato mostra duas estruturas gasosas dilatadas no andar superior, com pou
- Recém-nascido a termo não eliminou mecônio, apresenta distensão e vômitos. Exame contrasta
- Lactente com episódios de dor em cólica, palidez, vômitos e intervalos sem queixa apresent
- Criança com vômitos não biliosos em jato e desidratação apresenta alcalose metabólica hipo
- Qual combinação anatômica define mais precisamente gastrosquise?
- Um neonato com onfalocele íntegra está estável. Além do cuidado local e da avaliação do de
- Menino de seis meses tem abaulamento inguinal intermitente que aparece ao chorar e regride
- Assinale a comparação correta entre hidrocele isolada de lactente e hérnia inguinal.
- Ultrassonografia pré-natal identifica dilatação pielocalicial unilateral, com boa evolução
- Menino com meato uretral ventral anômalo e prepúcio dorsal em capuz será encaminhado ao es
- Adolescente refere dor testicular intensa e súbita, com náuseas e testículo elevado. A sus
- Recém-nascido do sexo masculino apresenta bexiga distendida, hidroureteronefrose bilateral
- Criança apresenta massa cervical mediana que se move à protrusão da língua. Após controle 
- Em massa adrenal pediátrica compatível com neuroblastoma, qual informação auxilia especial
- Com relação ao tumor de Wilms, qual afirmação representa corretamente a diversidade de pro
- Em criança com massa hepática suspeita de hepatoblastoma, qual abordagem é mais compatível
- Criança vítima de colisão chega pálida, taquicárdica e com extremidades frias, embora a pr
- Em trauma abdominal pediátrico, qual interpretação de ultrassom FAST sem líquido livre é a
- Menina estável, sem peritonite e com lesão esplênica fechada após queda, monitorada em hos
- Após extensa ressecção intestinal neonatal, a equipe detecta intolerância enteral prolonga
```

## Pedido 9: IBS e CBS: normas gerais e transição da reforma tributária (`direito-tributario.ibs-cbs-normas-gerais-transicao`, 212 tópicos de edital)

```
Matéria: direito-tributario.ibs-cbs-normas-gerais-transicao (IBS e CBS: normas gerais e transição da reforma tributária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura constitucional, incidência e imunidades": 16
- "2. Fato gerador, local, base e alíquotas": 15
- "3. Extinção, créditos, split payment e comércio exterior": 19
- "4. Bens de capital, cashback, cesta básica e regimes": 26
- "5. CGIBS, processo administrativo, repartição e transição": 24

Já existem (não repita):
- Uma empresa licenciou programa de computador mediante pagamento mensal. À luz da LC 214/20
- Um fabricante remete mercadorias de uma filial para outra, ambas do mesmo contribuinte. Qu
- Uma loja vende mercadoria física pela internet e entrega no endereço indicado pelo comprad
- Uma mercadoria custa R$ 1.000 e recebe no próprio documento fiscal um desconto de R$ 100, 
- Um fornecedor realizou operação com bens e o pagamento será feito por sistema eletrônico c
- Um contribuinte do regime regular comprou insumo com documento idôneo e houve extinção do 
- Uma indústria exportou produto para consumidor estrangeiro e possui créditos elegíveis de 
- Uma empresa do regime regular adquire máquina para seu ativo imobilizado, sem se enquadrar
- Uma família inscrita no CadÚnico cumpre requisitos do cashback e consome energia elétrica 
- Uma mercadoria destinada à alimentação humana aparece no Anexo I da LC 214/2025 e satisfaz
- Um município fixa sua parcela do IBS e o estado de destino fixa outra, ambas por lei espec
- Um serviço de manutenção física de equipamento móvel é realizado presencialmente em cidade
- Um contribuinte vende a prazo bem cujo fornecimento ocorrerá após sinal recebido. Qual com
- Uma família tem responsável inscrito no CadÚnico, com CPF regular, residência no País e re
- Uma prestação de serviço está abrangida por hipótese legal de redução de sessenta por cent
- Uma empresa brasileira presta serviço a pessoa estabelecida no exterior, mas seu resultado
- Uma entidade que acompanha a implantação pergunta quem edita o regulamento único e distrib
- Em janeiro de 2026, contribuinte apto ao regime de transição cumpre as obrigações acessóri
- A lei prevê suspensão do IBS/CBS na aquisição de determinado bem de capital, com posterior
- Um contribuinte quer aproveitar crédito de CBS de R$ 40 para abater débito de IBS de R$ 10
- Uma empresa fornece um equipamento e sua instalação como operação única. O serviço é condi
- Operação em 2030 permite comparar ICMS/ISS remanescentes à etapa de implementação do IBS. 
- Uma importadora sujeita ao regime regular internaliza mercadoria física e paga corretament
- Um adquirente examina operação suscetível simultaneamente a isenção e redução de alíquota 
- Uma família de baixa renda atende os requisitos legais em 2026 e quer saber a partir de qu
- Ao comparar dois tributos criados pela reforma, um aluno conclui que o IBS é de competênci
- Uma loja vende mercadoria por aplicativo a cliente em outro estado. Nessa situação, o art.
- No cumprimento de obrigações de CBS e IBS, uma empresa separa os débitos e os créditos de 
- Na compra de um item alimentício que não consta do Anexo I da LC 214/2025, o simples fato 
- Uma indústria do regime regular importa máquina produtiva, observando hipótese ordinária d
- Um contribuinte confunde as duas etapas da reforma e afirma que a CBS só começará a substi
- Uma empresa efetua exportação de produto e possui créditos de aquisições que atendem à lei
- Na compra de uma mercadoria, uma empresa utiliza desconto condicionado à obtenção de desem
- Uma empresa recolhe tributos na importação de bem material. O art. 65 da LC 214/2025 toma,
- Uma família reúne todos os requisitos do art. 113 e compra botijão de gás GLP de até 13 kg
- Um contribuinte identifica serviço que cumpre os requisitos legais para redução de alíquot
- Uma pessoa conclui que a Resolução CGIBS nº 6/2026, alterada pela nº 13/2026, instituiu a 
- Um sistema de pagamento eletrônico realiza recolhimento de IBS e CBS no instante da liquid
- Numa operação onerosa de transporte de passageiros iniciada em uma cidade e encerrada em o
- Um fornecedor disponibiliza bem e recebe adiantamento anterior à entrega. A existência de 
- Um contribuinte adquiriu equipamento de capital sob suspensão do art. 109 e deixou de inco
- Para aplicar o art. 7º-A, uma operação elegível tanto à isenção quanto a redução de alíquo
- Durante a implantação de 2026, as alíquotas teste de CBS de 0,9% e IBS de 0,1% significam 
- Uma importadora regular recolhe IBS e CBS na entrada de bem físico e cumpre os requisitos 
- Uma família com CPF regular e renda adequada está cadastrada no CadÚnico. Para o cálculo o
```

## Pedido 10: Geologia, relevo e solos (`geografia.geologia-relevo-solos`, 212 tópicos de edital)

```
Matéria: geografia.geologia-relevo-solos (Geologia, relevo e solos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura da Terra, litosfera e tectônica de placas": 21
- "2. Minerais, rochas, intemperismo e ciclo geológico": 18
- "3. Agentes do relevo e compartimentos geomorfológicos do Brasil": 26
- "4. Formação dos solos, horizontes, propriedades e SiBCS": 16
- "5. Hidrologia da paisagem, degradação e conservação do solo": 19

Já existem (não repita):
- Durante uma aula prática, uma amostra de rocha é retirada da crosta, enquanto outra vem da
- Ao interpretar registros sísmicos, pesquisadores observam ausência de transmissão direta d
- Um segmento de dorsal oceânica apresenta placas se afastando e ascensão de material mantél
- Uma cadeia montanhosa sul-americana apresenta fossas oceânicas próximas e vulcanismo ao lo
- Em uma amostra de granito são reconhecidos cristais de quartzo, feldspato e mica. Qual alt
- Duas rochas ígneas foram observadas: uma apresenta cristais grandes e originou-se em profu
- Um bloco de calcário sofre aumento de temperatura e pressão sem fusão completa, adquirindo
- Em afloramento, minerais se alteram quimicamente sem deslocamento imediato; depois a enxur
- Uma área de relevo brasileiro apresenta superfícies dissecadas, vales encaixados e perda d
- Num mapa físico, uma área localiza-se a 350 metros acima do nível do mar, porém mais baixa
- Em trecho meandrante de rio, observa-se erosão mais forte na margem externa da curva e dep
- Um atlas mostra limites da bacia hidrográfica amazônica e, em outro mapa, os contornos das
- Um perfil pedológico apresenta camada mineral superficial enriquecida em matéria orgânica,
- Dois perfis são descritos: o primeiro tem horizonte B latossólico profundo; o segundo poss
- Em área agrícola de encosta, observam-se sulcos causados por enxurradas após remoção da co
- Após chuvas, água atravessa a superfície do solo e parte continua descendo pelos poros até
- Um estudo de solos brasileiros publicado em 2026 deve adotar a edição mais recente do Sist
- Uma área possui solo profundo e predominantemente arenoso, com pequeno desenvolvimento de 
- Numa paisagem de camadas sedimentares inclinadas, o relevo mostra uma frente escarpada e u
- Um levantamento registra solo saturado por longo período, com cores acinzentadas e feições
- Uma floresta amazônica de terra firme possui vegetação exuberante, mas os solos minerais s
- Uma propriedade apresenta perda de solo por escoamento e assoreamento de curso d'água. É p
- Um estudante observa planícies inundáveis ao longo do Amazonas, mas também patamares, depr
- Dois perfis de mesma textura argilosa apresentam comportamentos hídricos diferentes: um te
- Em encosta agrícola, um Argissolo apresenta aumento significativo de argila no horizonte B
- No modelo composicional, a crosta, o manto e o núcleo são regiões diferentes; no modelo me
- Ondas sísmicas S atravessam o núcleo externo terrestre porque esse núcleo é sólido e possu
- O resfriamento do magma em profundidade pode originar granito intrusivo, enquanto a solidi
- A transformação de calcário em mármore exige fusão completa da rocha original, seguida de 
- Uma planície fluvial apresenta formas geralmente planas ou pouco onduladas e acumulação se
- O horizonte B do solo é obrigatoriamente a camada rochosa consolidada, e todos os perfis p
- Manter cobertura vegetal em encosta cultivada pode reduzir impacto direto das gotas de chu
- Por se situar predominantemente no interior da placa Sul-Americana, o Brasil não registra 
- O mesmo tipo de rocha pode originar sedimentos, sofrer metamorfismo sob condições apropria
- Depressões relativas brasileiras situam-se necessariamente abaixo do nível do mar, porque 
- O Sistema Brasileiro de Classificação de Solos em sua sexta edição, de 2025, mantém treze 
- Os Latossolos são definidos pelo horizonte B textural com incremento obrigatório de argila
- Em uma bacia hidrográfica, os divisores de águas superficiais delimitam a contribuição par
- A presença de densa vegetação de floresta amazônica comprova que os solos minerais de terr
- A mesma proporção de areia, silte e argila em dois solos não garante infiltração idêntica,
- Neossolos Quartzarênicos precisam ser rasos porque todo solo classificado como Neossolo po
- Na Amazônia, o contraste entre várzea periodicamente inundada e terra firme fora das cheia
- Um aquífero subterrâneo deve corresponder a grande caverna vazia cheia de água; a presença
- Em encosta desprotegida, sulcos erosivos podem exportar sedimentos ao rio, e a deposição d
- O plantio direto conservacionista consiste apenas em não revolver mecanicamente o solo; ma
```

## Pedido 11: Psicologia da saúde e equipes interdisciplinares (`psicologia.psicologia-da-saude`, 212 tópicos de edital)

```
Matéria: psicologia.psicologia-da-saude (Psicologia da saúde e equipes interdisciplinares)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Processo saúde-doença e experiência biopsicossocial do adoecimento": 21
- "2. Enfrentamento, adaptação psicológica e adesão ao tratamento": 18
- "3. Promoção, prevenção, reabilitação e níveis de atenção à saúde": 20
- "4. Equipes multidisciplinares, interdisciplinaridade e clínica ampliada": 21
- "5. Saúde do trabalhador, servidor federal e atuação da gestão de pessoas": 20

Já existem (não repita):
- Uma pessoa com hipertensão deixa de retornar às consultas porque trabalha em turnos e não 
- Após diagnóstico recente de condição crônica, um paciente procura ajustar o horário de tra
- Uma unidade de saúde aplica vacinação a pessoas elegíveis antes de apresentarem a doença-a
- Psicologia, enfermagem e serviço social discutem conjuntamente o cuidado, estabelecem meta
- Uma pessoa com sequelas motoras após acidente vascular cerebral faz fisioterapia e acompan
- Segundo o Decreto nº 6.833/2009, uma unidade do SIASS organiza assistência, promoção, acom
- Uma equipe da atenção primária mantém acompanhamento de pessoa com condição crônica, artic
- Uma paciente enfrenta dificuldades para seguir o tratamento por falta de transporte, efeit
- Uma equipe oferece rastreamento indicado a pessoas assintomáticas de determinada faixa etá
- Um profissional solicita exames invasivos sucessivos, sem indicação ou benefício provável,
- Uma mulher com doença crônica segue rigorosamente os medicamentos e consultas, mas mantém 
- Uma equipe constrói um PTS para pessoa com dor crônica. Após reuniões, há análise do conte
- Um gestor afirma que a PNSTT só contempla pessoas com emprego formal e carteira assinada; 
- Um serviço recebe pessoa com diabetes, demandas psicológicas e sociais. Uma equipe de refe
- Uma trabalhadora afastada por sofrimento psíquico retorna a ambiente em que jornadas exaus
- Uma instituição deseja melhorar a saúde de moradores mediante espaços públicos seguros par
- Uma equipe de saúde do trabalhador está elaborando relatório para orientar adaptações labo
- Uma pessoa com quadro crônico compreende a importância do tratamento e deseja segui-lo, ma
- Uma unidade de atenção primária acompanha pessoa com limitações após AVC e articula reabil
- Uma equipe classifica como falta de adesão o fato de uma usuária não comparecer a consulta
- Um setor de gestão de pessoas pede que a psicóloga do SIASS certifique, por conta própria,
- Uma instituição deseja medir qualidade de ações dirigidas à saúde do trabalhador e usa som
- Uma equipe de saúde mental reúne profissionais diferentes, mas cada um define metas sem co
- Uma equipe da APS realiza acolhimento, tratamento de condições comuns, reabilitação, reduç
- Em uma empresa, gestores oferecem palestras para controlar estresse, mas mantêm política d
- Uma pessoa com doença crônica encontra dificuldades de tratamento relacionadas à renda, ao
- Um serviço classifica vacinação preventiva de pessoas saudáveis como prevenção secundária,
- A Atenção Básica é reconhecida pela Portaria nº 2.436/2017 como principal porta de entrada
- A presença de psicologia, enfermagem e serviço social no mesmo serviço caracteriza necessa
- Segundo o Decreto nº 6.833/2009, o SIASS coordena e integra assistência, perícia oficial, 
- A psicologia da saúde utiliza abordagem biopsicossocial para substituir a investigação de 
- O Projeto Terapêutico Singular pode compreender análise ampliada, negociação de metas, def
- Uma pessoa com sequelas após acidente vascular cerebral participa de reabilitação coordena
- Uma equipe identifica horários de consulta incompatíveis com o trabalho de usuários e busc
- Uma organização responde a relatos de assédio e metas abusivas apenas oferecendo atendimen
- A PNAB define que a atenção básica pode desenvolver tratamento, reabilitação, redução de d
- Como a atuação psicológica ocorre em equipe multiprofissional, a Resolução CFP nº 06/2019,
- Uma política de saúde que melhora espaços públicos, mobilidade e oportunidades de atividad
- A PNSTT, instituída pela Portaria nº 1.823/2012, exclui pessoas sem vínculo empregatício f
- Um gestor sustenta que o relatório multiprofissional deve ser integralmente unificado, sem
- Uma pessoa em acompanhamento após adoecimento crônico realiza busca ativa de informações p
- No SIASS, a psicóloga que atende regularmente um servidor pode substituir a perícia oficia
- Uma pessoa tem doença crônica clinicamente controlada, mas relata solidão, estigma no empr
- A prevenção quaternária equivale ao quarto nível assistencial da rede e exige encaminhamen
- A priorização de trabalhadores em situações de maior vulnerabilidade pela PNSTT é compatív
```

## Pedido 12: Direito coletivo: organização sindical e negociação coletiva (`direito-trabalho.direito-coletivo`, 212 tópicos de edital)

```
Matéria: direito-trabalho.direito-coletivo (Direito coletivo: organização sindical e negociação coletiva)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Liberdade sindical, unicidade, categorias e Convenções da OIT": 31
- "2. Entidades sindicais: constituição, registro, administração, representação e proteção": 13
- "3. Contribuição sindical, liberdade individual e negociação coletiva": 12
- "4. Convenção e acordo coletivo: forma, duração, registro e relação entre instrumentos": 32
- "5. Negociado sobre legislado, direitos indisponíveis e condutas antissindicais": 12

Já existem (não repita):
- Trabalhadores de uma mesma categoria desejam criar segundo sindicato com representação sob
- Uma pessoa é contratada para emprego em empresa cuja categoria possui sindicato registrado
- Em um contrato de prestação de serviços, surge a necessidade de identificar a categoria ec
- Cinco sindicatos de atividades conexas desejam constituir entidade de grau superior para c
- Uma empresa realiza desconto anual da contribuição sindical de toda a categoria sem pedir 
- Sindicato profissional celebra instrumento normativo com sindicato representativo de empre
- Uma empresa e o sindicato profissional correspondente pretendem estabelecer regras de turn
- Uma convenção coletiva recém-negociada prevê validade de trinta meses. Qual regra da CLT s
- Uma pessoa exerce atividade de categoria diferenciada. Pretende obter vantagem de uma conv
- Um sindicato de empregados deseja participar de ação judicial em defesa de interesses indi
- Uma central sindical nacional coordena sindicatos filiados e participa de espaço tripartit
- Uma entidade em constituição registra seus atos associativos civis e requer reconhecimento
- Empregado eleito dirigente foi registrado como candidato durante contrato vigente e demiti
- Uma empresa recebe convocação legítima de sindicato profissional para discutir condições c
- Uma cláusula de instrumento coletivo prevê jornada regulada por banco de horas anual. Qual
- Uma convenção coletiva e um acordo coletivo válidos disciplinam de modo divergente uma mes
- Representantes assinaram um ACT e devem observar os prazos de depósito e início de vigênci
- Uma pessoa foi eleita dirigente de sindicato de categoria diferenciada, mas na empresa exe
- Uma entidade sindical possui diretoria de dez pessoas, mas requer estabilidade da CLT art.
- Uma empresa sustenta que, por não ter assinado CCT, pode recusar qualquer negociação solic
- Durante a vigência de ACT válido, uma empresa planeja reduzir o salário contratual por clá
- Uma CCT válida reduz o intervalo intrajornada de empregados com jornada de oito horas para
- Um sindicato e uma empresa negociam cláusula de redução do depósito mensal obrigatório do 
- Em ação cujo pedido é invalidar cláusula de uma convenção coletiva, a petição não incluiu 
- Uma convenção coletiva expira após 24 meses, sem nova norma assinada. A empresa sustenta q
- Um sindicato regularmente constituído pode defender interesses coletivos e individuais da 
- Uma entidade sindical pode ser livremente criada em base territorial de apenas um bairro, 
- No sistema da CLT, a categoria econômica refere-se aos interesses comuns dos empregadores 
- A Convenção nº 87 da OIT foi ratificada pelo Brasil e eliminou, por sua força jurídica int
- Cinco sindicatos podem organizar federação, observadas outras exigências do art. 534 da CL
- A contribuição sindical prevista nos arts. 578 e 579 da CLT continua sendo de desconto com
- Um instrumento firmado entre sindicato profissional e empresa, destinado a regular o traba
- Uma central sindical nacional, desde que atendidos requisitos legais, equivale juridicamen
- A condição de integrar uma categoria representada sindicalmente não implica dever de filia
- O dever de negociar do art. 616 da CLT obriga a empresa a aceitar a primeira pauta apresen
- Uma CCT regularmente aprovada contém cláusula que estabelece jornada e banco de horas anua
- Uma CCT válida pode produzir ultratividade automática por período ilimitado depois de expi
- O empregado eleito dirigente sindical possui proteção desde o registro de candidatura e, c
- A proteção sumulada de dirigente sindical alcança indistintamente todo integrante de diret
- Uma CCT e um ACT válidos abrangem a mesma relação trabalhista e divergem em cláusula negoc
- Uma cláusula de ACT que reduz depósitos mensais do FGTS pode prevalecer validamente sobre 
- No Tema 1046, admitiu-se a validade de limitações negociadas sobre direitos trabalhistas q
- Ao negociar redução de intervalo intrajornada para empregados com jornada de oito horas, s
- Se a ação tem por objeto invalidar cláusula de ACT, os sindicatos subscritores devem integ
- Uma empresa ameaça dispensar empregados caso mantenham filiação ao sindicato e sustenta qu
```

## Pedido 13: PLN, deep learning e visão computacional (`informatica.pln-deep-learning-visao-computacional`, 211 tópicos de edital)

```
Matéria: informatica.pln-deep-learning-visao-computacional (PLN, deep learning e visão computacional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de redes profundas e arquiteturas sequenciais": 20
- "2. Corpus, processamento morfológico e anotação linguística": 24
- "3. Representação textual, vetores e medidas de similaridade": 21
- "4. Aplicações de PLN, modelagem de tópicos e transformers": 18
- "5. Visão computacional, reconhecimento facial e avaliação integrada": 17

Já existem (não repita):
- Uma equipe reuniu relatórios de três períodos, com texto, idioma, data e anotação do assun
- Um buscador reduz ‘casinhas’ e ‘casas’ a radicais por regras simples, sem consultar a clas
- Em ‘Eu canto agora’ e ‘O canto está vazio’, pretende-se diferenciar o emprego verbal e nom
- No esquema BIO, a expressão ‘Maria Clara chegou’ contém uma entidade PESSOA de dois tokens
- Uma rede aplica o mesmo pequeno filtro aprendido a diferentes regiões de fotografias, gera
- Uma implementação Word2Vec recebe como entrada as palavras vizinhas de ‘documento’ e procu
- Em uma coleção, ‘protocolo’ aparece em quase todos os documentos, e ‘perícia’ em poucos. D
- Dois textos são representados pelos conjuntos de termos A={a,b,c} e B={b,c,d}. Qual é a si
- Um sistema identifica ‘Rita’ como PESSOA e ‘Empresa Lua’ como ORGANIZAÇÃO. Para produzir ‘
- Uma equipe quer comparar algoritmos Word2Vec. No experimento escolhido, usa-se a palavra c
- Dois documentos recebem vetores u=(1,1) e v=(3,3). A equipe considera a similaridade do co
- Uma matriz documento-termo com pesos não negativos será aproximada pelo produto de duas ma
- Um projeto modela cada documento como mistura de temas e cada tema como distribuição proba
- Um modelo deve processar sequências longas e controlar, a cada passo, quanto do estado de 
- Em fotos de rua, o modelo deve distinguir dois veículos sobrepostos e entregar o contorno 
- Uma caixa prevista ocupa área 80, a caixa de referência área 60 e a interseção entre elas 
- Para representar uma notícia inteira, um pesquisador calcula a média dos embeddings de sua
- Um anotador atribuiu B-ORG e I-ORG às duas palavras de uma entidade. O tokenizador do mode
- No corpus aparece ‘Ana mora em Recife’. O sistema retornou Ana/B-PESSOA e Recife/B-LOCAL, 
- Um transformer autoregressivo gera o quarto token de uma frase. O projetista permite que a
- Uma rede de Hopfield clássica, com pesos simétricos e atualização assíncrona apropriada, r
- Para uma classe rara, há 24 positivos reais. Um classificador sinaliza 20 positivos, dos q
- Dois textos, após normalização, geraram os conjuntos A={sol,mar,luz} e B={mar,luz,noite,ve
- Um reconhecimento automático de voz produziu ‘a sessão foi aberta’, mas a transcrição de r
- Uma CNN treinada com fotos será adaptada para reconhecer um novo tipo de objeto com poucas
- Um sistema de análise de avaliações trata a frase ‘não gostei’ como negativa. Remover a pa
- Uma aplicação que identifica ‘Paulo’ como pessoa está realizando, necessariamente, a mesma
- Um modelo de classificação de fotografias usa convoluções para compartilhar filtros espaci
- Ao converter uma gravação de voz em legenda textual, o pipeline descrito executa text-to-s
- Na rotulação BIO de ‘Rio de Janeiro’ como uma única entidade LOCAL, é possível anotar Rio/
- Uma representação documental booleana registra quantas vezes cada palavra apareceu, permit
- Em um projeto de análise temática, LSI e NMF produzem, por definição, exatamente as mesmas
- Ao comparar A=(1,0) e B=(0,1), obtêm-se distância de Manhattan igual a 2 e distância eucli
- Um analista usa a similaridade do cosseno em vetores TF-IDF. Se um documento possui vetor 
- Uma equipe reúne comentários em grupos de conteúdo semelhante, sem possuir previamente rót
- Na sumarização extrativa de uma ata, o sistema precisa gerar frases inéditas que nunca apa
- Uma arquitetura de tradução com transformer pode empregar codificador para representar a f
- Um modelo encontra caixas delimitadoras de pedestres numa fotografia. Esse resultado já co
- Em uma RNN treinada por retropropagação através do tempo, dependências distantes podem ser
- Na classificação de documentos muito desequilibrados, um sistema que acerta a classe major
- Um modelo treinado para verificação facial 1:1 compara uma amostra com a identidade declar
- No treinamento de um transformer que gera a próxima palavra, liberar acesso às palavras fu
- Uma rede de Hopfield com pesos simétricos, atualizações assíncronas apropriadas e função d
- Dois documentos têm conjuntos de termos disjuntos e não vazios. A similaridade de Jaccard 
- Ao avaliar um sistema que combina reconhecimento de fala, extração de entidades e identifi
```

## Pedido 14: Processo legislativo e Poder Legislativo estadual (`direito.processo-legislativo-estadual`, 211 tópicos de edital)

```
Matéria: direito.processo-legislativo-estadual (Processo legislativo e Poder Legislativo estadual)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos constitucionais e organização das Assembleias Legislativas": 17
- "Mesa Diretora, Plenário, lideranças, comissões e controle parlamentar": 17
- "Espécies normativas estaduais, iniciativa e reserva de competência": 17
- "Tramitação legislativa, quóruns, votação, sanção e veto": 17
- "Deputados estaduais: imunidades, impedimentos, decoro e perda de mandato": 16
- "Regimentos estaduais em editais: Rondônia, Roraima, Amazonas, Pará e Ceará": 16

Já existem (não repita):
- O Poder Legislativo estadual brasileiro é, em regra, unicameral, sendo exercido por Assemb
- O mandato de deputado estadual tem duração constitucional de cinco anos, independentemente
- Um estado com 14 deputados federais terá 42 deputados estaduais, pois o triplo se aplica a
- A Mesa Diretora e o Plenário possuem atribuições que podem ser diferentes, conforme a Cons
- A instalação de CPI estadual dispensa fato determinado, desde que um terço dos deputados a
- Uma comissão de mérito que emite parecer favorável transforma automaticamente o projeto em
- A lei complementar estadual diferencia-se da lei ordinária por tratar de matéria constituc
- Todo governador pode editar medida provisória porque a Constituição Federal prevê medidas 
- Decreto legislativo estadual, resoluções internas e leis ordinárias são sempre enviados à 
- Maioria absoluta de uma Assembleia composta por 24 deputados corresponde a 13 votos.
- Havendo 13 deputados presentes em uma Assembleia de 24, sete votos favoráveis podem bastar
- A sanção do governador convalida um projeto de lei iniciado por deputado quando a Constitu
- A imunidade material parlamentar abrange opiniões, palavras e votos relacionados ao exercí
- As imunidades parlamentares formais dos deputados estaduais são mera faculdade concedida p
- A proteção constitucional contra prisão do parlamentar abrangido pelo art. 53, § 2º, começ
- No Ceará, a Resolução nº 751/2022 é referência para o Regimento Interno da Assembleia Legi
- A Resolução Legislativa nº 469/2010 corresponde ao Regimento Interno da Assembleia Legisla
- A Resolução nº 02/2022 do Pará aprovou regimento interno da Assembleia e previu diretrizes
- O que expressa a unicameralidade do Poder Legislativo estadual?
- Um estado possui oito representantes na Câmara dos Deputados. Quantos deputados estaduais 
- Uma proposição trata de matéria de competência concorrente da União e dos estados. Assinal
- Considere uma unidade da federação representada por 19 deputados federais. Qual é o número
- A duração de uma legislatura estadual deve ser distinguida da sessão legislativa porque:
- A Mesa Diretora de uma Assembleia é corretamente caracterizada como:
- Qual é requisito constitucional para constituir CPI no âmbito estadual, segundo o parâmetr
- Uma CPI estadual recolheu documentos e ouviu autoridades. Qual providência é compatível co
- Após parecer favorável de uma comissão temática, uma proposta de lei estadual ainda poderá
- Uma Assembleia declara que toda CPI tem poder de decretar prisão preventiva diretamente. A
- Assinale a espécie normativa ordinariamente usada para alterar o texto da Constituição est
- O que distingue, principalmente, a lei complementar estadual da ordinária?
- Um governador deseja editar medida provisória estadual. Qual condição deve ser investigada
- A Assembleia aprova um projeto de resolução sobre organização de sua própria atividade. Em
- Projeto parlamentar versa sobre matéria reservada constitucionalmente à iniciativa do gove
- Composição total: 30 deputados. Qual é a maioria absoluta exigível?
- Num plenário de 24 deputados, quantos votos favoráveis representam três quintos do total?
- Compare presença e aprovação numa Assembleia com 24 integrantes. Qual alternativa define c
- Sobre veto parcial de projeto de lei estadual, no parâmetro constitucional aplicável, é co
- Numa Assembleia com 46 deputados, matéria dependente de maioria absoluta requer:
- Qual manifestação costuma integrar o núcleo da imunidade material do deputado estadual?
- Sobre o início da imunidade formal contra prisão do parlamentar abrangido pelo art. 53, § 
- Um deputado estadual pretende afirmar inviolabilidade por toda declaração privada, sem con
- Qual é o limite constitucional do subsídio do deputado estadual em relação ao dos deputado
- Qual par de ato normativo e Assembleia está corretamente associado?
- Na preparação para concurso da ALE/RO, o candidato encontra o Código de Ética e Decoro Par
- Qual conduta é mais segura quando edital estadual menciona regimento interno e suas altera
```

## Pedido 15: Macroeconomia: contas nacionais e produto (`economia.macroeconomia-contas-nacionais`, 210 tópicos de edital)

```
Matéria: economia.macroeconomia-contas-nacionais (Macroeconomia: contas nacionais e produto)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fluxo circular, sistema de contas nacionais e três óticas do PIB": 46
- "2. PIB nominal e real, índices de preços, inflação e emprego": 17
- "3. Poupança, investimento, renda de equilíbrio e política fiscal": 14
- "4. Moeda, autoridade monetária e vínculos com produto e preços": 11
- "5. Balanço de pagamentos BPM6, conta financeira e reservas internacionais": 12

Já existem (não repita):
- Uma empresa vende bens finais por 500 unidades monetárias após consumir insumos de outras 
- Em uma economia hipotética, C=500, I=150, G=200, X=100 e M=80. Qual é o PIB pela ótica da 
- Uma economia tem PIB de 1.200 e renda primária líquida recebida do exterior igual a −50. Q
- Um bem é produzido em 100 unidades a R$ 10 no ano-base e em 110 unidades a R$ 12 no períod
- O PIB nominal de certo período é 1.500, e o PIB real correspondente, em mesma unidade e re
- Uma pesquisa registra 720 pessoas ocupadas, 80 desocupadas e 200 fora da força de trabalho
- Em modelo simples de renda com propensão marginal a consumir igual a 0,75, preços fixos e 
- Uma conta monetária apresenta papel-moeda em poder do público de 200 e depósitos à vista d
- Em fluxo circular com governo e setor externo, uma economia observa poupança familiar, pag
- Em economia aberta simplificada, Y=1.000, T=200, C=600, G=150 e exportações líquidas=20. Q
- No modelo keynesiano fechado com tributo autônomo e propensão marginal a consumir 0,8, qua
- Uma cesta-base tem valor de 200 aos preços-base e de 250 aos preços atuais. Qual é o índic
- O índice de preços ao consumidor cresce 10% em janeiro e mais 10% em fevereiro, ambos em r
- Uma administração apresenta déficit primário de 40 e juros nominais apropriados de 60. Sob
- Nas estatísticas externas BPM6, uma economia recebe dividendos de investimento externo e r
- Uma economia apresenta aquisição líquida de ativos externos de 80 e incorrência líquida de
- Em um cálculo de índices, a cesta corrente custa 540 aos preços atuais e 450 aos preços-ba
- No BPM6 de uma economia hipotética, conta corrente tem saldo −60, conta capital +10 e cont
- As reservas internacionais em moeda estrangeira aumentaram em valor doméstico depois de al
- Em um exemplo de balanço monetário, o público detém 160 de papel-moeda, bancos possuem 40 
- Uma economia apresenta VAB a preços básicos de 800, impostos sobre produtos de 140, subsíd
- Uma economia fechada tem função de consumo C=100+0,6(Y−T), tributo proporcional T=0,25Y, i
- Uma pessoa afirma que, pela equação MV=PY, aumento de 20% na oferta de moeda necessariamen
- Em economia hipotética, o governo faz transferência monetária de 50 a famílias e compra se
- Uma economia tem superávit comercial de 30, déficit em serviços de 10, renda primária líqu
- Em fluxo circular ampliado, poupança, tributos e importações são vazamentos, enquanto inve
- Uma família recebe benefício monetário de transferência do governo e não realiza gasto ime
- Uma máquina importada de 100 é adquirida como investimento por empresa residente. Na ident
- Em economia com PIB de 900 e renda primária líquida recebida do exterior de −20, a Renda N
- Em uma economia de produto nominal 1.440 e produto real comparável de 1.200, o deflator im
- O índice de preços sobe 5% em um mês e 5% no seguinte. A inflação acumulada é exatamente 1
- Em uma força de trabalho com 450 pessoas ocupadas e 50 desocupadas, a taxa de desocupação 
- Uma analista usa quantidades do período-base ao calcular um índice de preços de Paasche, e
- Numa economia aberta simplificada, poupança nacional de 300 e investimento de 250 são comp
- Um setor público registra superávit primário de 20 e juros nominais de 70. Usando superávi
- No modelo keynesiano com preços constantes, gastos autônomos e propensão marginal a consum
- No balanço monetário, M1 coincide necessariamente com a base monetária restrita, pois ambo
- Uma elevação da taxa de juros, mantidos renda e outros fatores, pode reduzir a demanda por
- Sob BPM6, uma empresa residente recebe juros de aplicação externa e adquire ações de outra
- Em balanço de pagamentos BPM6, saldos hipotéticos são corrente −30, capital +5 e financeir
- Se o estoque de reservas internacionais aumenta 15 unidades por valorização cambial e 5 po
- Um índice de preços de Laspeyres é 144 e o de Paasche é 121 para os mesmos períodos e bens
- O resultado fiscal operacional coincide, por definição, com o resultado primário, pois amb
- Uma expansão de moeda de 8%, sob a hipótese adicional de velocidade de circulação e produt
- Quando o deflator do PIB cresce 12% em um período, o IPCA necessariamente também cresce 12
```

## Pedido 16: Microeconomia: teoria do consumidor e da produção (`economia.teoria-consumidor-producao`, 210 tópicos de edital)

```
Matéria: economia.teoria-consumidor-producao (Microeconomia: teoria do consumidor e da produção)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Preferências, utilidade e curvas de indiferença": 18
- "2. Restrição orçamentária e equilíbrio do consumidor": 22
- "3. Efeitos preço, renda, substituição e elasticidade": 19
- "4. Fatores de produção, produtividade e rendimentos marginais": 24
- "5. Rendimentos de escala, custos, preços e margens": 17

Já existem (não repita):
- Uma pessoa possui utilidade representada por U=XY e compara as cestas A=(2,6), B=(3,4), C=
- Uma família dispõe de 120 reais para X e Y. O preço de X é 6 reais e o de Y é 12 reais. Se
- Um consumidor enfrenta preço de X igual a 9 reais e de Y igual a 6 reais, com X no eixo ho
- Para um consumidor cujas preferências são representadas por U=2X+Y, qual descrição das cur
- Um consumidor tem preferências U=min(X,Y), preços iguais e renda suficiente para comprar s
- Uma pessoa passa a receber renda maior sem alteração dos preços e aumenta o consumo do bem
- Uma oficina produziu zero unidade com nenhum trabalhador, cinco com um trabalhador e treze
- Uma fábrica com quatro empregados e máquinas fixas produz 36 peças por dia. Qual é o produ
- Uma tecnologia gera 100 unidades com dado conjunto de trabalho e capital. Ao duplicar simu
- Uma pessoa dispõe de 140 reais; X custa 5 e Y custa 7. Um imposto específico de 2 reais po
- Uma consumidora maximiza U=XY, dispõe de 80 reais e enfrenta preços Px=4 e Py=2. Admitindo
- Em uma função Cobb–Douglas U=X^0,5Y^0,5, o orçamento é 200 reais, Px=10 e Py=5. Qual é a c
- Um estudo encontra que a demanda por um alimento diminui quando a renda das famílias cresc
- Ao analisar uma queda no preço de X, um pesquisador desenha uma reta compensada com os nov
- Uma empresa pode trocar pequenas quantidades de capital por trabalho mantendo o mesmo prod
- Uma fábrica mantém máquinas fixas. Com dois trabalhadores produz 18 peças e com três produ
- A tecnologia de uma firma é descrita por uma função Cobb–Douglas com expoente 0,5 para tra
- Uma pessoa tem preferências U=min(X,2Y), preços Px=3 e Py=2 e renda de 80 reais. Se consum
- Um consumidor tem U=XY, renda de 120 reais, preços iniciais Px=10 e Py=5. Depois, Px cai p
- O preço de uma mercadoria é 8 reais. Com repasse integral ao consumidor, compara-se um imp
- Uma empresa usa Q=L^0,6K^0,4, com insumos positivos e tecnologia constante. Qual afirmação
- Dois processos produtivos devem ser comparados pela elasticidade de substituição entre tra
- Em certo intervalo, a produtividade marginal do trabalho é de seis unidades de produto por
- Uma firma com poder de mercado escolhe ponto interior ótimo, apresenta custo marginal cons
- Um produto é vendido por 90 reais e seu custo unitário é 72 reais. Que par expressa corret
- Em um modelo com dois bens desejáveis, duas cestas na mesma curva de indiferença oferecem 
- Um aumento de renda, mantendo fixos os preços dos dois bens, altera a inclinação da reta o
- Um bem normal é aquele cuja quantidade demandada aumenta diante de um aumento da renda, ma
- Quando o produto marginal do trabalho é positivo, mas se reduz em relação ao trabalhador a
- Ao comparar duas configurações produtivas, uma empresa mantém o capital físico constante e
- Em uma função Cobb–Douglas, dois expoentes positivos garantem rendimentos crescentes de es
- Uma pessoa tem preferências U=XY e escolhe, com os dois bens positivos, uma cesta que maxi
- Se a renda e os preços dos dois bens forem todos multiplicados por três, o conjunto orçame
- Quando o preço de X aumenta e sua demanda reage, o efeito substituição compensado normalme
- Se um bem é inferior, qualquer aumento em seu preço necessariamente elevará a quantidade d
- Uma empresa encontra produtividade marginal do quarto trabalhador de cinco unidades e médi
- Se o salário por trabalhador é constante, a produtividade marginal do trabalho é decrescen
- Na análise de produção, aumentar proporcionalmente trabalho e capital e observar mais que 
- Uma consumidora com preferências por complementares perfeitos, representadas por curvas em
- Na decomposição de Slutsky de uma mudança no preço de X, o ajuste hipotético de renda é co
- Ao instituir imposto específico legalmente cobrado do vendedor, fica demonstrado que todo 
- No exemplo U=X^0,5Y^0,5, com preços inicialmente iguais, uma redução isolada do preço de X
- A verificação de rendimentos crescentes de escala em uma função de produção demonstra, sem
- Uma firma vende por 100 reais um produto de custo unitário 80 reais. A diferença de 20 rea
- Uma firma com poder de mercado e demanda negativamente inclinada deve, em seu ótimo interi
```

## Pedido 17: Tratamento de água e esgoto, análises de qualidade e competência de laboratórios (`engenharia-ambiental.tratamento-agua-esgoto-analises`, 210 tópicos de edital)

```
Matéria: engenharia-ambiental.tratamento-agua-esgoto-analises (Tratamento de água e esgoto, análises de qualidade e competência de laboratórios)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tratamento de água: etapas e produtos químicos": 17
- "2. Parâmetros e determinações analíticas": 17
- "3. Tratamento de esgotos: aeróbio e anaeróbio": 17
- "4. Coleta de amostras e análises bacteriológicas": 17
- "5. Legislação: potabilidade e enquadramento": 16
- "6. ISO/IEC 17025 e saúde pública": 16

Já existem (não repita):
- No tratamento convencional de água, a etapa em que se adiciona sulfato de alumínio ou clor
- A floculação deve ser realizada com agitação intensa e rápida, para favorecer o cresciment
- Entre as funções da aeração no tratamento de água, destaca-se:
- Na desinfecção com cloro, a espécie química com maior poder desinfetante, predominante em 
- A manutenção de cloro residual livre na água distribuída tem por finalidade proteger a águ
- A formação de trialometanos na água tratada está associada à reação do cloro com:
- O ensaio de laboratório usado para determinar a dose ótima de coagulante, simulando coagul
- Em uma mesma amostra de esgoto, a DQO é sempre igual ou maior que a DBO, porque inclui tam
- A medida padrão da demanda bioquímica de oxigênio (DBO) é realizada em incubação por:
- O excesso de nitrogênio e fósforo em corpos de água pode provocar:
- A turbidez é medida em unidades de turbidez (uT ou NTU) por turbidímetro, e partículas em 
- No tratamento de esgotos, o nível que remove sólidos grosseiros por grades e areia por des
- São vantagens dos processos anaeróbios de tratamento de esgoto, como o reator UASB:
- O sistema de lodos ativados é um processo aeróbio que utiliza tanque de aeração com biomas
- Nas lagoas facultativas de tratamento de esgoto, o oxigênio utilizado pelas bactérias aeró
- Os processos anaeróbios de tratamento de esgoto apresentam alta eficiência na remoção de n
- Os frascos para coleta de água clorada destinada a análise bacteriológica devem conter tio
- O indicador mais específico de contaminação fecal recente da água, cuja ausência em 100 mL
- O método da membrana filtrante expressa seus resultados em unidades formadoras de colônia 
- No método do substrato cromogênico, a presença de Escherichia coli é evidenciada pela ativ
- Para caracterizar o esgoto afluente a uma estação, cuja composição varia ao longo do dia, 
- A presença de bactérias do grupo coliforme total indica necessariamente contaminação de or
- A Portaria nº 2.914/2011 do Ministério da Saúde dispõe sobre:
- Nos normativos de potabilidade, o controle da qualidade da água cabe ao responsável pelo s
- Segundo a Resolução CONAMA nº 357/2005, as águas doces são classificadas em:
- Na Resolução CONAMA nº 357/2005, a classe 4 de águas doces é a mais restritiva, destinada 
- O enquadramento dos corpos de água, previsto na legislação, corresponde:
- As condições e padrões de lançamento de efluentes, inicialmente tratados na CONAMA nº 357/
- A norma ISO/IEC 17025 estabelece:
- Entre os requisitos técnicos da ISO/IEC 17025 estão a validação de métodos, a estimativa d
- No Brasil, a acreditação de laboratórios segundo a ISO/IEC 17025 é realizada pelo:
- A participação em ensaios de proficiência, isto é, comparações interlaboratoriais, é uma f
- É doença de transmissão hídrica, adquirida pela ingestão de água contaminada:
- O protozoário Cryptosporidium é altamente sensível ao cloro nas doses usuais de tratamento
- A leptospirose, frequentemente associada a enchentes, é transmitida principalmente pelo co
- A desinfecção da água por ozônio ou por radiação ultravioleta tem como limitação, em relaç
- Na decantação, os flocos formados nas etapas anteriores sedimentam no fundo dos tanques, o
- O tanque séptico (fossa séptica) é exemplo de unidade de tratamento anaeróbio de esgoto, m
- A queda acentuada do oxigênio dissolvido em um rio que recebe esgoto bruto decorre do cons
- O cloro residual em água tratada é usualmente determinado em campo por método colorimétric
```

## Pedido 18: Atos processuais, prazos e comunicação no processo penal (`processo-penal.atos-processuais-prazos`, 210 tópicos de edital)

```
Matéria: processo-penal.atos-processuais-prazos (Atos processuais, prazos e comunicação no processo penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Forma, sujeitos e validade dos atos processuais": 19
- "2. Lugar, tempo e contagem dos prazos processuais": 27
- "3. Citação: modalidades, requisitos e efeitos": 14
- "4. Intimação, notificação e processo eletrônico": 14
- "5. Casos integrados: termo inicial, suspensões e nulidades": 26

Já existem (não repita):
- Ao receber a denúncia, a pessoa encarregada da secretaria precisa comunicar formalmente a 
- Um acusado encontra-se recolhido em estabelecimento prisional, com localização conhecida. 
- O oficial cumpriu diligências e certificou que o acusado não foi encontrado, sem indicar q
- Uma pessoa recebeu intimação válida numa terça-feira. Não houve feriados ou suspensões e f
- Foi proferida decisão em ação penal com defensor nomeado. A secretaria avalia a forma de c
- Em processo comum sem acusado preso, sem medida urgente e fora de procedimento especial de
- Uma pessoa a ser citada foi localizada em município que está fora da jurisdição territoria
- Uma decisão foi disponibilizada no Diário da Justiça eletrônico numa segunda-feira comum, 
- A pessoa acusada foi citada por edital, deixou de comparecer e também não constituiu advog
- O oficial relata diligências sucessivas e atesta que o acusado se esconde deliberadamente 
- Após citação pessoal regular, o acusado deixou de comparecer à audiência sem justificativa
- No procedimento comum, a denúncia foi recebida e o acusado regularmente citado. A defesa d
- Em uma secretaria, três expedientes foram conclusos: uma decisão definitiva, uma decisão i
- Na última data de um prazo processual, a defesa protocola petição diretamente no sistema j
- Uma intimação foi remetida a destinatário credenciado em portal eletrônico. Ele não consul
- Uma sessão de julgamento começou em uma sexta-feira útil e seu prosseguimento alcançou um 
- Em procedimento do juizado especial criminal, foram feitas tentativas idôneas de citação p
- Um oficial realizou tentativa de citação por aplicativo em processo criminal. O destinatár
- O acusado compareceu antes da audiência para apontar defeito em sua citação. Demonstrou ta
- O defensor foi intimado pessoalmente em uma quarta-feira; o prazo recursal é de cinco dias
- Duas ações penais têm prazo em curso em 22 de dezembro: uma envolve acusado preso e ato li
- O defensor constituído foi intimado por publicação em comarca com órgão oficial, mas a pub
- Uma pessoa credenciada consultou intimação em portal no sábado, sem feriado na segunda-fei
- No último dia do prazo, o sistema oficial de processo eletrônico apresentou indisponibilid
- Uma parte recebeu intimação por oficial na segunda-feira e o mandado foi juntado na quinta
- O oficial entregou contrafé ao acusado, leu o mandado e certificou o cumprimento. Esses pr
- Ao receber intimação na quarta-feira, a defesa dispõe de prazo processual penal de três di
- Uma pessoa acusada está presa e pode ser localizada no estabelecimento de custódia. O art.
- O defensor foi nomeado pelo juízo em um processo penal. Para cientificá-lo da sentença, ba
- Uma decisão foi disponibilizada numa segunda-feira no Diário da Justiça eletrônico. Sem fe
- Citado pessoalmente, o acusado faltou injustificadamente à audiência. O art. 366 do CPP im
- O acusado está em lugar conhecido no exterior. A autoridade optou pela carta rogatória e r
- Em ação penal sem acusado preso nem urgência, um prazo de oito dias entrou no intervalo de
- Depois de adiar a instrução, o julgador fixa, na presença das partes e das testemunhas, di
- O oficial certificou que o acusado se esconde para impedir o cumprimento da citação. A con
- Em um processo eletrônico, a parte protocolou petição às 23h55 do último dia do prazo, com
- Uma intimação de portal foi consultada dois dias após o envio, mas a secretaria decidiu co
- O prazo do Ministério Público para manifestação conta, como regra do art. 800, § 2º, do te
- Num processo regido pela Lei nº 11.340/2006, o prazo recursal foi suspenso obrigatoriament
- Uma intimação pessoal foi cumprida na terça e o mandado chegou ao processo somente na sext
- Uma pessoa acusada não respondeu à mensagem recebida em número de telefone que ninguém ver
- A publicação da intimação do defensor constituído omitiu o nome do acusado. A irregularida
- A pessoa acusada compareceu antes de audiência para discutir falha de citação e demonstrou
- O sistema judicial ficou indisponível por motivo técnico no último dia do prazo de protoco
- Durante a suspensão geral anual do art. 798-A do CPP, uma medida urgente foi expressamente
```

## Pedido 19: Exame Psiquiátrico, Psicofarmacologia, Emergências e Tratamentos Biológicos (`psiquiatria.exame-psicofarmacologia-emergencias`, 210 tópicos de edital)

```
Matéria: psiquiatria.exame-psicofarmacologia-emergencias (Exame Psiquiátrico, Psicofarmacologia, Emergências e Tratamentos Biológicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Classificação, exame psiquiátrico e exames complementares": 25
- "2. Psicofarmacologia": 25
- "3. Emergências psiquiátricas": 25
- "4. Tratamentos biológicos, psicoterapias e bioética": 25

Já existem (não repita):
- O DSM-5, em relação ao DSM-IV-TR, abandonou o sistema de avaliação multiaxial.
- Sobre os sistemas de classificação em psiquiatria, assinale a alternativa correta.
- No exame do estado mental, a crença falsa, mantida com convicção e irredutível à argumenta
- A alucinação é definida como uma percepção sem objeto e corresponde a uma alteração da sen
- A neuroimagem cerebral é o exame que confirma o diagnóstico de esquizofrenia e de depressã
- A investigação com exames complementares para afastar causas orgânicas é especialmente ind
- No exame do estado mental, o juízo crítico refere-se à capacidade do paciente de reconhece
- Os antipsicóticos de primeira geração, como o haloperidol, estão mais associados a:
- A discinesia tardia é um efeito adverso associado ao uso prolongado de antipsicóticos, sob
- A clozapina é indicada na esquizofrenia resistente e exige monitoramento regular do hemogr
- Os antipsicóticos de segunda geração estão livres de efeitos adversos metabólicos, como ga
- Os antidepressivos tricíclicos exigem cautela em pacientes com risco de suicídio porque, n
- Pacientes em uso de inibidores da monoaminoxidase (IMAO) devem evitar alimentos ricos em t
- Agitação, hiperreflexia, clônus, tremor e hipertermia em paciente que associou dois fármac
- Em paciente com transtorno bipolar, o uso de antidepressivo sem estabilizador do humor pod
- Sobre o lítio, é correto afirmar:
- Tremor grosseiro, ataxia, confusão mental, vômitos e diarreia em paciente em uso de lítio 
- O valproato deve ser evitado em mulheres em idade fértil sem contracepção eficaz, principa
- A carbamazepina é um inibidor enzimático que aumenta os níveis séricos dos fármacos admini
- A lamotrigina exige titulação lenta da dose em razão do risco de:
- A associação de benzodiazepínicos com álcool ou opioides aumenta o risco de depressão resp
- O antagonista utilizado para reverter a intoxicação por benzodiazepínicos é o:
- Febre, rigidez muscular intensa, alteração da consciência, instabilidade autonômica e CPK 
- No manejo da agitação psicomotora, a contenção física deve ser a primeira medida, antes de
- Perguntar diretamente ao paciente sobre ideação suicida aumenta o risco de que ele tente o
- Na avaliação do risco de suicídio, é considerado um dos fatores de risco mais fortes:
- O delirium caracteriza-se por:
- No paciente com dependência de álcool, a tiamina deve ser administrada antes da glicose pa
- A base do tratamento farmacológico da síndrome de abstinência alcoólica, inclusive para pr
- No paciente agitado, a investigação de causas orgânicas e de intoxicações é dispensável qu
- Restringir o acesso a meios letais e envolver a rede de apoio são medidas recomendadas no 
- A eletroconvulsoterapia é realizada sob anestesia geral e com relaxamento muscular, sendo 
- O efeito adverso mais relevante e característico da eletroconvulsoterapia é:
- A eletroconvulsoterapia é contraindicada de forma absoluta em todas as gestantes.
- Sobre a estimulação magnética transcraniana (EMT), é correto afirmar que:
- A terapia comportamental dialética (DBT) é uma abordagem com indicação destacada no:
- A terapia cognitivo-comportamental trabalha pensamentos automáticos, crenças e comportamen
- Em psiquiatria, a capacidade de decisão do paciente deve ser avaliada para cada escolha es
- São princípios clássicos da bioética aplicados à psiquiatria:
- Os inibidores seletivos da recaptação de serotonina (ISRS) têm efeito antidepressivo pleno
- Diferentemente do delirium, a demência caracteriza-se, em geral, por:
```

## Pedido 20: Dermatologia (`medicina.dermatologia`, 210 tópicos de edital)

```
Matéria: medicina.dermatologia (Dermatologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Estrutura da pele, semiologia e métodos diagnósticos": 17
- "Dermatoses inflamatórias: eczemas, urticária, acne e psoríase": 17
- "Infecções e infestações da pele": 17
- "Hanseníase: abordagem dermatoneurológica": 17
- "Doenças bolhosas, manifestações sistêmicas e neoplasias cutâneas": 16
- "Terapêutica dermatológica, prevenção e sinais de gravidade": 16

Já existem (não repita):
- A epiderme é avascular e recebe nutrientes por difusão a partir dos tecidos subjacentes.
- Mácula é uma lesão sólida e elevada da pele, enquanto pápula é mudança de cor sem relevo.
- O prurido e a xerose são características comuns da dermatite atópica, cujo tratamento bási
- A presença de comedões é típica da rosácea papulopustulosa e distingue essa doença da acne
- Crostas melicéricas em região perioral de criança são achado clássico de impetigo.
- A escabiose costuma cursar com prurido noturno e pode afetar mais de um membro de uma mesm
- O patch test é utilizado principalmente para investigar hipersensibilidade tardia em suspe
- Uma lesão urticariforme individual fixa e dolorosa durante quatro dias, deixando púrpura, 
- A dermatite de contato irritativa exige sensibilização prévia e é mediada obrigatoriamente
- Um quadro de herpes-zóster costuma produzir vesículas agrupadas em distribuição dermatomér
- Na hanseníase, baciloscopia negativa afasta com segurança a doença paucibacilar.
- A PQT-U utilizada no SUS para hanseníase inclui rifampicina, dapsona e clofazimina tanto n
- A evolução de uma lesão pigmentada é critério de alerta para melanoma, mesmo quando o diâm
- A imunofluorescência direta em dermatite herpetiforme apresenta tipicamente depósitos gran
- No pênfigo vulgar, a bolha costuma ser tensa porque a clivagem ocorre abaixo de toda a epi
- Em dermatofitose mascarada por corticosteroide, a apresentação pode perder a borda anular 
- A reação hansênica tipo 1 pode cursar com neurite e deterioração rápida da função nervosa,
- O achado de acantose nigricans em dobras indica obrigatoriamente neoplasia interna oculta.
- Para eczema facial, deve-se preferir corticosteroide tópico de alta potência por tempo pro
- O diagnóstico de melanoma se confirma apenas pela regra ABCDE, tornando histopatologia dis
- Paciente apresenta perda superficial da epiderme após ruptura de vesícula, sem acometiment
- Qual alteração predomina na acne comedoniana, mesmo na ausência de pústulas?
- Prurido noturno intenso, pápulas nos punhos e túneis interdigitais, com familiares sintomá
- Qual combinação de fármacos constitui a PQT-U para hanseníase preconizada pelo Ministério 
- Qual descrição clínica é mais sugestiva de carcinoma basocelular?
- Entre as estruturas abaixo, qual é o principal componente responsável pela função de barre
- Na dermatite seborreica do adulto, qual distribuição é mais característica?
- Uma lesão anular com borda ativa descamativa e clareamento central sugere principalmente:
- Qual alternativa descreve a lesão típica da psoríase em placas?
- Em suspeita de melanoma, o critério E da regra ABCDE corresponde a:
- Profissional com eczema nas mãos após lavagens repetidas, sem história de sensibilização a
- Em paciente com lesão sugestiva de dermatofitose, qual exame simples pode demonstrar hifas
- Homem idoso com prurido importante e bolhas tensas generalizadas, sem erosões orais marcan
- Paciente com dermatite atópica passa a apresentar erosões dolorosas monomórficas dissemina
- Indivíduo com psoríase relata rigidez matinal prolongada e edema difuso de um dedo. A medi
- Quanto à duração habitual da PQT-U, qual relação é correta em hanseníase operacionalmente 
- Em relação à lesão de herpes-zóster no território do primeiro ramo trigeminal, qual condiç
- Lesão muito pruriginosa em superfícies extensoras, associada à enteropatia sensível ao glú
- Diante de lesão cutânea de provável melanoma, em local anatomicamente favorável, qual cond
- Sobre reações hansênicas, assinale a alternativa correta.
- Eritema malar fotossensível que poupa sulcos nasolabiais, em paciente com manifestações si
- Qual padrão na imunofluorescência direta é esperado classicamente em penfigoide bolhoso?
- Indivíduo com placas eczematosas na face em uso crônico de corticoide de alta potência apr
- Pessoa com erupção extensa após fármaco recente, febre, dor cutânea e erosões em mucosas d
- Qual assertiva sobre lesão precursora e neoplasias cutâneas é mais precisa?
```

## Pedido 21: Sistema de custos no setor público (`contabilidade-publica.custos-setor-publico`, 209 tópicos de edital)

```
Matéria: contabilidade-publica.custos-setor-publico (Sistema de custos no setor público)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, finalidade e evolução normativa dos custos públicos": 26
- "2. Ambiente da informação, objeto de custo e qualidade dos dados": 31
- "3. Regime de competência, classificação e métodos de custeio": 12
- "4. Sistema federal, manuais e implantação do gerenciamento de custos": 19
- "5. Relatórios, indicadores, comparações e questões avançadas": 12

Já existem (não repita):
- Uma entidade quer justificar por que precisa manter sistema de custos mesmo prestando serv
- Uma unidade recebeu materiais em março, mas consumiu parte deles em maio. Para apurar o cu
- Uma unidade registra o custo de refeições fornecidas à população. Como classificar a própr
- A administração pretende estimar o custo de exames específicos, registrando reagentes util
- Ao revisar apostila que apresenta a NBC T 16.11 como norma atual, um estudante deve regist
- Um centro disponibiliza suporte de tecnologia a diversas unidades que prestam serviços ext
- Uma equipe analisa se a informação de custos poderá ser conferida por documentos de origem
- Uma unidade precisa acumular custos de atendimento prestado continuamente durante o ano. Q
- Uma unidade compra máquina de longa utilização para atender o público. Que procedimento me
- Uma entidade examina o manual federal aprovado em 2018 e sua relação com a versão mais rec
- Uma unidade iniciou seu modelo de custos com poucos dados confiáveis e quer reconhecer ape
- Uma entidade quer distribuir o consumo de energia de um edifício usado por duas unidades, 
- A entidade deseja comparar o custo de serviços finais similares de várias unidades, inclui
- Uma entidade quer divulgar indicadores de custos e discute a frequência indicada especific
- Uma equipe está definindo centros de responsabilidade e pretende associá-los à estrutura e
- Uma planilha inicial de custos foi rejeitada sob o argumento de que a norma exigiria softw
- Um serviço executado por equipe permanente tem despesas com vigilância contratada e materi
- Uma entidade tem custo de pessoal de R$ 240 mil e custos de materiais de R$ 60 mil, ambos 
- Um relatório mensal aumenta após inclusão da depreciação e dos serviços de suporte, mas a 
- Uma unidade com modelo avançado utiliza ABC para rastrear indiretos; outra, em estágio ini
- Uma instituição recebeu prédio cedido para prestação de serviços e não paga aluguel. Como 
- Um setor faz rateio de custo de limpeza entre áreas, mas descobre que a base utilizada pro
- Duas entidades medem custos finais semelhantes: uma inclui suporte e outra usa apenas pess
- A equipe discute a relação entre geração de informações de custo e integridade dos registr
- Em serviço de prestação continuada, o custo por entrega caiu após aumento de atendimentos,
- Uma unidade presta 500 atendimentos gratuitos e mantém controle de consumo de pessoal e in
- Uma entidade adquiriu materiais em junho, pagou a nota em julho e usou parte do estoque em
- Uma instituição mede separadamente os custos de cursos entregues ao público e de manutençã
- Uma equipe que utiliza a NBC T 16.11 como base principal de orientação em 2026 pode afirma
- Um sistema consolida os gastos de energia de diversas unidades e os aloca segundo critério
- Em serviço que gera entregas continuamente, a adoção do sistema de acumulação por ordem é 
- Um gestor precisa de relatório específico de custo de serviço, enquanto a população acompa
- Uma unidade decidiu publicar relatório de custos apenas a cada três anos e sustentou que a
- Em entidade com baixa maturidade de informação, optar inicialmente pelo custeio direto pod
- Uma unidade possui dados em planilhas com controle documental e rastreabilidade; mesmo ass
- Para atribuir custos a determinado atendimento, um analista identificou primeiro os insumo
- Uma unidade considera que o valor pago pela compra de máquina em dezembro representa integ
- A divulgação de relatórios de custos de entidades comparáveis exige observar diferenças op
- O relatório gerencial de duas unidades apresenta custos unitários menores após mudança de 
- A entidade recebeu serviços prestados por equipe cedida e recursos utilizados em suas ativ
- Em avaliação de custos de objetos intermediários entre unidades, a NBC TSP 34 torna compul
- Uma unidade pode obter custo unitário menor simplesmente ao atender volume maior, sem redu
- O Manual de Informações de Custos aprovado pela Portaria STN nº 518/2018 permanece a publi
- A geração da informação de custos é atribuição do profissional de contabilidade, mas os ge
- Uma entidade com poucos recursos pode deixar de iniciar qualquer sistema de custos, pois o
```

## Pedido 22: Psicologia do trabalho: saúde mental, assédio e riscos psicossociais (`psicologia.trabalho-saude-mental-assedio`, 209 tópicos de edital)

```
Matéria: psicologia.trabalho-saude-mental-assedio (Psicologia do trabalho: saúde mental, assédio e riscos psicossociais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Transformações do trabalho, organização, atividade e ergonomia": 21
- "2. Psicodinâmica, clínicas do trabalho, subjetivação e adoecimento": 25
- "3. Riscos psicossociais e avaliação: Resolução CFP 14/2023, NR-1 e NR-17": 17
- "4. Assédio moral, sexual, discriminação e proteção antirracista": 18
- "5. Estresse, avaliação psicológica, bem-estar e programas de prevenção": 19

Já existem (não repita):
- O procedimento prevê atendimento de dez minutos por pessoa, mas instabilidade do sistema e
- Uma equipe sofre com metas contraditórias, ausência de pausas e divisão desigual de tarefa
- Uma pessoa precisa conferir simultaneamente alertas, decidir prioridades e lembrar exceçõe
- Uma equipe relata sofrimento porque trabalhos complexos são avaliados apenas pelo número d
- Em grupo de trabalhadores, a análise de situações que impedem o 'trabalho bem feito' ampli
- A equipe apresenta medo constante diante de violência verbal dos usuários. Uma proposta pr
- A equipe pretende avaliar riscos psicossociais apenas em atividades presenciais e excluir 
- Uma psicóloga recebe relato de humilhações sistemáticas feitas por uma chefia em reuniões,
- Em um programa de saúde mental, decide-se revisar metas inatingíveis e ampliar autonomia a
- Uma pessoa relata exaustão, cinismo em relação ao trabalho e redução de eficácia após long
- Uma organização mede absenteísmo e conclui que esse indicador, isoladamente, prova que tod
- Uma organização realizou AEP e percebeu que as ações implementadas não reduziram queixas e
- Uma equipe está submetida a exigências elevadas e tem pouca autonomia para ordenar tarefas
- Após tratamento de saúde mental, uma trabalhadora retorna e necessita jornada previsível, 
- Uma psicóloga participa de equipe multiprofissional para avaliar riscos no trabalho. A coo
- Uma psicóloga observa que critérios de promoção formalmente idênticos exigem disponibilida
- Um programa organizacional oferece sessões de relaxamento, mas mantém jornadas extensas, m
- Uma profissional recebe demanda para produzir laudo de aptidão usando apenas um teste de p
- Após denúncias de comentários sexuais insistentes entre colegas de mesmo nível, alguém afi
- Em estudo participativo, trabalhadores mostram que adaptam regras contraditórias para pres
- O setor adotou uma nova plataforma digital; após três meses, trabalhadores relatam aumento
- Em uma instituição, denúncias de assédio aumentaram após a criação de canal seguro e confi
- A coordenação quer usar informações clínicas de atendimentos individuais para selecionar q
- Após queixa de assédio com assimetria de poder, uma gestão determina reunião obrigatória e
- Um setor registra subcarga de tarefas, isolamento social, falta de reconhecimento e sofrim
- Uma equipe tem manual de atendimento uniforme, mas precisa adaptar prioridades quando o si
- Depois de identificar metas incompatíveis com os recursos disponíveis, a organização ofere
- Uma organização cria grupos protegidos para discutir como executar atividades com qualidad
- Uma trabalhadora recebe cobranças claras, proporcionais e respeitosas sobre tarefa previst
- Uma unidade revisa metas e amplia pausas antes de observar adoecimento da equipe. Essas aç
- Em avaliação de riscos, o registro de ansiedade em parte da equipe demonstra, por si só, q
- Um serviço híbrido apresenta queixas de disponibilidade permanente no aplicativo de mensag
- Uma organização realizou avaliação ergonômica preliminar e verificou que ações adotadas fo
- Um servidor passa a executar horas extras para cumprir metas e deixa de participar de cuid
- O setor identificou alto risco psicossocial e decidiu elaborar o plano de ação do PGR com 
- Uma psicóloga identifica critérios internos de avaliação que excluem sistematicamente pess
- Por exigir superioridade hierárquica na descrição do art. 216-A do Código Penal, qualquer 
- Uma equipe com elevada pressão de prazo e reduzida margem de decisão apresenta maior risco
- Na realização de uma avaliação de risco psicossocial, a profissional identifica barreiras 
- A atualização do capítulo 1.5 da NR-1 que inclui expressamente fatores psicossociais relac
- Uma empresa descreve burnout como diagnóstico psiquiátrico independente na CID-11 e o este
- Em situações de DORT associadas a repetição de movimentos, pressão por metas, baixa autono
- Uma psicóloga acolhe relato de possível assédio e decide exigir reconciliação presencial i
- Um relatório sobre riscos psicossociais deve examinar características da atividade, gestão
- Como a Resolução CFP 31/2022 disciplina o SATEPSI, ela permite que qualquer escala informa
```

## Pedido 23: Código de Ética e resoluções do Conselho Federal de Psicologia (`psicologia.resolucoes-cfp-etica-profissional`, 209 tópicos de edital)

```
Matéria: psicologia.resolucoes-cfp-etica-profissional (Código de Ética e resoluções do Conselho Federal de Psicologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ética profissional, direitos humanos e sigilo": 17
- "2. Documentos psicológicos: finalidade, estrutura e limites": 18
- "3. Avaliação psicológica, SATEPSI e processos seletivos": 20
- "4. Antidiscriminação, orientação sexual e violências de gênero": 25
- "5. Psicoterapia, riscos psicossociais e integração das normas": 20

Já existem (não repita):
- Uma direção pede a íntegra das sessões psicológicas de uma pessoa cujo atendimento é pago 
- Uma psicóloga identifica conflito concreto entre confidencialidade e necessidade de proteç
- Durante trabalho com equipe multiprofissional, o profissional precisa registrar informaçõe
- Uma pessoa solicita apenas comprovação de comparecimento a três sessões, sem referência a 
- Ao concluir um processo formal de avaliação psicológica, uma psicóloga precisa comunicar r
- Uma entidade consulta profissional que não acompanhou certa pessoa, solicitando análise ci
- Uma profissional consulta a classificação oficial de testes antes de escolher instrumento 
- Em 2026, é preciso escolher a resolução específica para avaliação psicológica em concurso 
- Para selecionar pessoas para determinada atividade, uma equipe precisa definir previamente
- Um responsável solicita à psicóloga de adolescente a transcrição integral das confidências
- Uma escola solicita documento sobre intervenções psicológicas educacionais realizadas ao l
- Após exame de instrumento classificado como favorável no SATEPSI, um profissional deseja a
- Uma pessoa deseja avaliação psicológica para registro de arma de fogo por psicóloga que te
- Uma pessoa relata sofrimento causado por rejeição familiar à própria orientação sexual. Qu
- A direção de uma empresa encomenda técnica psicológica para associar determinado grupo étn
- Uma mulher trans relata violência doméstica e teme não ser acolhida por serviço psicológic
- Após numerosas queixas de assédio e sobrecarga em uma equipe, a direção quer classificar i
- Um juiz intima psicóloga que acompanhou uma pessoa e pede, além de informações sobre o ser
- Num serviço interdisciplinar, o documento reúne informações de vários profissionais e deta
- Em seleção de natureza pública, critérios psicológicos foram criados depois de conhecida a
- Uma avaliação psicológica para registro de arma foi emitida há 26 meses. A pessoa apresent
- Uma profissional identifica práticas racistas persistentes na instituição e recebe pedido 
- Uma equipe atende pessoas atingidas por violência de gênero e discute trabalho interventiv
- Em psicoterapia, profissional pretende gravar sessões de adolescente sem informar a pessoa
- Uma profissional precisa integrar avaliação psicológica em seleção pública com posterior d
- Em atendimento psicológico custeado por empresa, a profissional mantém resguardado o conte
- Diante de situação concreta que justifique quebra de sigilo, a psicóloga poderá enviar aut
- Para confirmar a presença de uma pessoa a encontros de atendimento, uma declaração limitad
- Um laudo psicológico pode apresentar conclusões de avaliação de pessoa que o profissional 
- Na escolha de teste psicológico, consultar se o instrumento está classificado como favoráv
- Em outubro de 2026, a CFP nº 02/2016 continua sendo a norma específica vigente para avalia
- Em reunião interdisciplinar sobre criança atendida, todos os profissionais podem consultar
- Uma avaliação psicológica concluída com procedimentos documentados pode fundamentar laudo;
- Num concurso público, a descrição prévia dos requisitos psicológicos deve decorrer de estu
- Profissional interessado economicamente na aprovação de uma pessoa para registro de arma p
- Diante de pedido para realizar técnica destinada a modificar a orientação sexual de pessoa
- Segundo a CFP nº 18/2002, somente o uso de técnicas diretamente discriminatórias constitui
- A CFP nº 08/2020 orienta atuação em violências de gênero com acolhimento de mulheres cis, 
- A CFP nº 13/2022, por tratar de psicoterapia, permite ao profissional informar aos respons
- Ao investigar frequentes afastamentos em local com assédio e sobrecarga, a avaliação de ri
- Um teste psicológico classificado como desfavorável no SATEPSI pode ser aplicado profissio
- Na avaliação psicológica para registro de arma, o prazo máximo de validade de dois anos se
- Na CFP nº 08/2020, o trabalho com autores de violência de gênero substitui a articulação d
- Ao planejar registro audiovisual de sessões, a psicóloga precisa informar a pessoa atendid
- Profissional designado para avaliação em concurso público pode observar apenas a CFP nº 08
```

## Pedido 24: Antropologia e teoria social clássica (`ciencias-sociais.antropologia-teoria-social`, 207 tópicos de edital)

```
Matéria: ciencias-sociais.antropologia-teoria-social (Antropologia e teoria social clássica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Indivíduo, sociedade, ordem social e explicação sociológica": 21
- "2. Escola sociológica francesa, dádiva e estruturalismo": 19
- "3. Antropologias britânica e estadunidense, cultura e etnografia": 20
- "4. Trocas, símbolos, linguagem, identidade e categorias de pensamento": 21
- "5. Estados nacionais, poder, ação coletiva e atores não estatais": 19

Já existem (não repita):
- Ao ingressar em uma comunidade, uma pessoa encontra uma língua, normas de convivência e sa
- Em uma coletividade, a divisão de atividades torna produtores de alimentos, transportadore
- Uma pesquisa sobre indústria procura explicar a desigualdade segundo a propriedade dos mei
- Uma pessoa participa de uma manifestação por convicção ética, mesmo prevendo prejuízo pess
- Uma jovem aprende regras iniciais de convivência no núcleo familiar e, anos depois, novas 
- Em cerimônia comunitária, uma pessoa entrega um objeto valioso, o destinatário o aceita e 
- Um estudo de ritos mostra que certos objetos são separados do uso cotidiano e tratados col
- Uma investigação compara versões de narrativas míticas e procura relações de oposição e tr
- Um pesquisador britânico descreve relações de parentesco como rede de direitos e obrigaçõe
- Uma antropóloga permanece por período prolongado em comunidade, aprende usos da língua e p
- Ao estudar uma comunidade com rituais incomuns para sua experiência, uma pesquisadora busc
- Em uma pesquisa etnográfica, duas pessoas fazem um movimento idêntico com o olho; em um ca
- Em determinado grupo, parentes oferecem alimento entre si sem registrar dívidas ou fixar e
- Duas comunidades mantêm fronteira étnica reconhecida, apesar de trocas frequentes de alime
- Um território politicamente unificado inclui populações de identidades nacionais diferente
- Um líder recebe obediência principalmente porque sua autoridade é tida como continuidade d
- Uma comunidade mantém celebrações transmitidas entre gerações, mas modifica músicas e prát
- Durante um rito de passagem, participantes permanecem temporariamente fora das posições us
- Moradores encontram regras que limitam suas ações, mas também organizam coletivamente uma 
- Uma associação apresenta relatórios e propostas em espaço de consulta internacional, mas n
- Uma prática coletiva foi criada após disputas históricas, mas atualmente contribui para in
- Uma etnografia descreve relações segmentares de parentesco que permitem alianças e resoluç
- Ao analisar o kula, uma pesquisadora combina descrição de viagens, circulação ritual de ob
- Duas pessoas doam a mesma quantia publicamente. Uma busca ampliar prestígio e oportunidade
- Um estudo reúne narrativas de coletivos migrantes, símbolos nacionais e transformações nas
- Uma língua e regras de convivência precedem a chegada de uma pessoa à comunidade e podem e
- Ao comparar proprietários e trabalhadores de uma indústria, a teoria de Marx define classe
- Uma cerimônia na qual dar um objeto cria obrigação de recebimento e retribuição pode ser a
- Ao estudar redes de parentesco e deveres entre grupos, Radcliffe-Brown seguiu a mesma defi
- Uma antropóloga procura reconstruir a história própria de uma comunidade, em vez de classi
- Em um sistema de dádivas cerimoniais, Mauss consideraria a prestação social completa somen
- O pesquisador que distingue um aceno ritual de um movimento involuntário semelhante está r
- Quando duas línguas dispõem de classificações diferentes de parentesco, essa diferença pro
- Uma pessoa que tenta obter vantagem significativa com contrapartida injustamente reduzida 
- Um Estado formado por populações com diferentes pertencimentos nacionais deixa, por esse ú
- Uma organização econômica altamente especializada pode apresentar solidariedade orgânica e
- O tipo ideal de dominação legal-racional em Weber corresponde à média estatística de todos
- O relativismo cultural usado como postura de pesquisa não obriga aprovar uma prática que f
- O estrutural-funcionalismo de Radcliffe-Brown tem por objeto principal estruturas inconsci
- A definição de patrimônio cultural imaterial do art. 2º da Convenção de 2003 admite que pr
- Pela definição sociológica do Estado moderno em Weber, a reivindicação de monopólio do uso
- Uma organização não governamental pode obter participação consultiva em temas relevantes a
- Na abordagem de Barth, a presença de trocas materiais e incorporação de costumes externos 
- Em análise de ritos de passagem inspirada em Turner, uma fase liminar pode envolver suspen
- Se determinada cerimônia hoje favorece a coesão de um grupo, sua função integradora atual 
```

## Pedido 25: Oncologia clínica (`medicina.oncologia-clinica`, 207 tópicos de edital)

```
Matéria: medicina.oncologia-clinica (Oncologia clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "Biologia do câncer, carcinogênese e marcadores": 17
- "Epidemiologia, prevenção e rastreamento no Brasil": 17
- "Diagnóstico, estadiamento TNM e decisão terapêutica": 17
- "Terapias antineoplásicas e medicina de precisão": 17
- "Emergências, síndromes paraneoplásicas e toxicidades": 16
- "Tumores prevalentes: mama, pulmão, próstata, colorretal e colo uterino": 16

Já existem (não repita):
- Em um laudo, observa-se proliferação epitelial atípica sem ultrapassar a membrana basal. Q
- Após sequenciamento tumoral, identifica-se ativação de uma via de sinalização proliferativ
- Um tumor colorretal demonstra deficiência de reparo de pareamento do DNA, com instabilidad
- Qual dos exemplos corresponde predominantemente a fator de risco infeccioso relacionado ao
- Marcadores preditivos estimam a possibilidade de resposta a uma terapia específica, enquan
- Um resultado sérico normal de CEA exclui câncer colorretal invasivo, porque esse antígeno 
- O desenvolvimento de uma neoplasia maligna pode envolver expansão clonal, instabilidade ge
- Todo tumor classificado como benigno ultrapassa obrigatoriamente a membrana basal e tem ca
- Na elaboração de orientação para rastreamento mamário populacional no SUS em outubro de 20
- Uma mulher assintomática de 55 anos realiza teste de DNA-HPV oncogênico como rastreamento 
- Pessoa assintomática de risco habitual, com 60 anos, apresenta FIT positivo no rastreament
- Homem de 58 anos sem sintomas pergunta se todo homem deve dosar PSA anualmente como políti
- Em oncologia, rastreamento refere-se à busca de doença em pessoas assintomáticas, enquanto
- O novo FIT incorporado para rastreamento do câncer colorretal no SUS em 2026 destina-se a 
- Uma pessoa vacinada contra HPV, com colo uterino e dentro da população-alvo, está dispensa
- A detecção de um câncer real, porém biologicamente indolente, que não produziria dano ao l
- Laudo clínico de neoplasia sólida informa cT2N1M0. Qual interpretação geral não extrapola 
- Paciente recebe quimioterapia pré-operatória e, em seguida, é operado. Como deve ser inter
- Após ressecção de carcinoma localizado, uma equipe recomenda terapia sistêmica para reduzi
- Em tumor sólido, a peça cirúrgica revela células pouco diferenciadas, mas as imagens não e
- A categoria N do sistema TNM descreve os linfonodos regionais, enquanto os linfonodos dist
- O grau de diferenciação histológica e o estágio tumoral são expressões equivalentes; porta
- A abordagem paliativa pode ser introduzida precocemente e coexistir com tratamento sistêmi
- A presença de N1 implica automaticamente M1 em qualquer carcinoma, porque comprometimento 
- Paciente com câncer de mama com receptores hormonais positivos discute tratamento que bloq
- Biópsia de adenocarcinoma pulmonar avançado revela alteração EGFR acionável. Qual lógica t
- Após uso de anti-PD-1, uma pessoa apresenta diarreia persistente e dor abdominal, sem expl
- Qual das modalidades utiliza radiação ionizante direcionada ao tumor ou a regiões de risco
- Anticorpos anti-HER2 e inibidores anti-PD-1 pertencem necessariamente à mesma classe funci
- A resposta à terapia-alvo costuma exigir caracterização molecular apropriada, mas a presen
- Na radioterapia, a toxicidade pode variar conforme o volume e órgão irradiados, a dose, o 
- Como a imunoterapia estimula defesa antitumoral, ela não causa inflamação de órgãos normai
- Após início de terapia para neoplasia de alta proliferação, surgem potássio e fósforo elev
- Paciente com câncer metastático refere dor vertebral progressiva e desenvolve fraqueza nas
- Pessoa em quimioterapia apresenta febre e suspeita de neutropenia. Qual a conduta inicial 
- Qual associação entre neoplasia e síndrome paraneoplásica é clássica e potencialmente cobr
- A síndrome da veia cava superior pode manifestar-se com edema facial, circulação venosa co
- Hipercalcemia maligna e síndrome de lise tumoral produzem obrigatoriamente o mesmo padrão 
- A ausência de hipotensão não exclui gravidade em neutropenia febril, pois a febre pode ser
- Miocardite, pneumonite e colite podem ocorrer como eventos imunomediados de inibidores de 
- Biópsia mamária confirma carcinoma invasivo com RE negativo, RP negativo e HER2 negativo. 
- Em câncer colorretal localizado no reto médio, com extensão local relevante e sem metástas
- Na avaliação de carcinoma do colo do útero localmente avançado, qual combinação é um pilar
- Em paciente com carcinoma pulmonar de não pequenas células avançado, qual medida tem papel
- No câncer de próstata localizado de risco selecionado, vigilância ativa pode ser opção, de
- O antígeno prostático específico (PSA), quando elevado, estabelece sozinho diagnóstico his
- No câncer colorretal, a pesquisa de deficiência de MMR ou instabilidade de microssatélites
- Tumores pulmonares de pequenas células e de não pequenas células têm necessariamente o mes
```

## Pedido 26: Manejo e conservação ambiental (`biologia.manejo-conservacao-ambiental`, 206 tópicos de edital)

```
Matéria: biologia.manejo-conservacao-ambiental (Manejo e conservação ambiental)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Recuperação de áreas degradadas e restauração ecológica": 32
- "2. Estrutura populacional e manejo de fauna silvestre": 17
- "3. Ecologia da paisagem e conectividade": 17
- "4. Manejo florestal sustentável e valoração ambiental": 17
- "5. Aquecimento global, sequestro de carbono e conservação integrada": 17

Já existem (não repita):
- Após eliminar o pastejo, um terreno apresenta muitas plântulas nativas e sementes oriundas
- Um projeto devolve funções a um ecossistema degradado, porém sua condição final difere da 
- Durante inventário, pesquisadores estimam 150 animais numa área de 50 hectares de habitat 
- Uma espécie desapareceu de parte de sua distribuição histórica e um programa pretende rest
- Um mapa mostra manchas florestais unidas por faixa verde, mas a espécie estudada não utili
- Qual é a finalidade principal de uma zona de amortecimento prevista na Lei nº 9.985/2000 a
- Uma equipe planeja manejo de produtos florestais respeitando capacidade de regeneração e m
- A floresta protege margens de rios e reduz erosão. Na classificação da Lei nº 14.119/2021,
- Uma floresta guarda grande quantidade de carbono, mas seu estoque cresce lentamente de um 
- Um programa reduz risco de inundação mediante restauração de margens de rios diante de eve
- Uma área minerada apresenta sulcos de erosão, solo instável e quase nenhuma regeneração ve
- Um plantio registra sobrevivência de 92% das mudas no terceiro mês, mas grande infestação 
- Duas populações possuem 200 indivíduos cada, mas uma reúne apenas poucos adultos que contr
- Para reintroduzir animais, um grupo pretende soltar diretamente espécimes de semiliberdade
- Um fragmento florestal passou a apresentar maior proporção de borda em relação à área de i
- Três manchas florestais abrigam populações locais, com extinções pontuais e recolonizações
- Uma floresta manejada terá duas colheitas de produtos na mesma unidade de área, separadas 
- Um estudo atribui à floresta apenas a receita potencial da madeira, embora ela também regu
- Uma área apresentou incremento líquido comprovado de 12 toneladas de carbono armazenado. Q
- Um projeto de proteção reduz o desmatamento na sua área, porém desloca parte da mesma ativ
- Um programa de recuperação registra 95% de mudas sobreviventes, mas a área continua isolad
- Uma estimativa por marcação e recaptura pressupõe capturabilidade semelhante, mas os anima
- Uma passagem vegetada entre fragmentos aumenta a circulação de uma espécie, porém facilita
- Um estudo de valoração soma o custo de instalar equipamento para tratar água e a totalidad
- Uma floresta perderia cobertura sob o cenário de referência. O projeto evitou parte dessa 
- Em área degradada, qualquer recuperação ambiental somente pode ser reconhecida quando o ec
- Numa área com plântulas de espécies nativas, o isolamento de gado e o controle de invasora
- Em um levantamento de mamíferos, o número de indivíduos observados numa trilha equivale ne
- Em uma paisagem fragmentada, o deslocamento efetivo de determinada espécie entre manchas c
- Na Lei nº 14.119/2021, o sequestro de carbono é serviço ecossistêmico classificado como pr
- Uma soltura que acrescenta indivíduos a uma população silvestre da mesma espécie já estabe
- Um conjunto de árvores que conecta unidades protegidas em imagem aérea garante, por sua co
- O ciclo de corte do manejo florestal refere-se ao período entre dois momentos de colheita 
- A floresta que acumula grande estoque de carbono necessariamente apresenta sequestro líqui
- A melhora de estabilidade do solo e da função hídrica é resultado ecológico relevante de r
- Um projeto de revegetação forma cobertura homogênea de espécie exótica, mas não demonstra 
- Duas populações possuem abundância igual, mas diferentes proporções de adultos férteis; po
- A soltura de animal mantido em semiliberdade dispensa avaliação sanitária, genética e comp
- Uma faixa contínua de vegetação pode promover dispersão para algumas espécies e, ao mesmo 
- O objetivo principal da zona de amortecimento do art. 2º, XVIII, da Lei nº 9.985/2000 é ga
- Segundo a Lei nº 14.119/2021, a atividade de uma comunidade que conserva floresta pode con
- Um ciclo longo entre cortes comprova, por si só, manejo florestal sustentável mesmo quando
- A redução comprovada de desmatamento em relação à linha de base pode representar emissões 
- Uma tonelada de carbono elementar e uma tonelada de CO₂ representam quantidades quimicamen
- Uma intervenção com remoção de carbono demonstrada deve ser examinada quanto à adicionalid
```

## Pedido 27: Doenças infecciosas e parasitárias: IST/HIV, tuberculose, hanseníase, arboviroses e parasitoses (`medicina.doencas-infecciosas-parasitarias`, 205 tópicos de edital)

```
Matéria: medicina.doencas-infecciosas-parasitarias (Doenças infecciosas e parasitárias: IST/HIV, tuberculose, hanseníase, arboviroses e parasitoses)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos de microbiologia, parasitologia e raciocínio infectológico": 17
- "Infecções sexualmente transmissíveis, HIV/aids e hepatites virais": 17
- "Tuberculose, hanseníase e infecções bacterianas de relevância coletiva": 17
- "Arboviroses: dengue, zika, chikungunya e febre amarela": 17
- "Malária, leishmanioses, doença de Chagas e parasitoses intestinais": 16
- "Infecções do sistema nervoso central e doenças hídricas ou alimentares": 16

Já existem (não repita):
- Uma cultura positiva sem sinais clínicos nem evidência de invasão tecidual pode representa
- No estudo do ciclo da esquistossomose mansônica, qual exposição explica a infecção humana?
- Um exame sorológico negativo realizado imediatamente após exposição infecciosa exclui de f
- Qual dos seguintes agentes é um protozoário de importância médica?
- Na comparação entre exames de sífilis, qual teste é usado quantitativamente para monitorar
- Uma pessoa chega ao serviço 20 horas após exposição sexual relevante ao HIV e pergunta sob
- Uma paciente apresenta anti-HCV reagente, mas RNA do HCV não detectável. Qual interpretaçã
- Qual vírus depende da presença do vírus da hepatite B para completar seu ciclo?
- Em adulto com suspeita de tuberculose pulmonar, qual recurso laboratorial integra a invest
- Antes de oferecer tratamento de infecção latente da tuberculose a um contato, qual etapa é
- Uma pessoa apresenta placa hipocrômica com perda de sensibilidade dolorosa e térmica e esp
- Qual associação representa a poliquimioterapia única usada na hanseníase pelo SUS?
- Em pessoa com suspeita de dengue, que sinal indica necessidade de reavaliação imediata por
- Uma gestante apresenta exantema pruriginoso, conjuntivite não purulenta e febre baixa após
- Na dengue, o risco de choque por extravasamento plasmático costuma merecer vigilância espe
- A artralgia intensa que pode persistir por meses é uma pista especialmente característica 
- Qual exame microscópico é oficialmente adotado no Brasil como referência para pesquisa de 
- Paciente com febre prolongada, emagrecimento, baço aumentado e pancitopenia vive em área d
- Sobre Taenia solium, qual circunstância se relaciona diretamente ao desenvolvimento de neu
- Qual antiparasitário é principal referência no tratamento etiológico da doença de Chagas p
- Em suspeita forte de meningite bacteriana grave, qual princípio orienta a conduta?
- Qual padrão do líquor favorece meningite bacteriana típica, sem ser exclusivo?
- Em um surto de diarreia aquosa intensa com sinais de choque, qual prioridade inicial é apr
- Qual via de transmissão está diretamente relacionada à hepatite A e a várias gastroenterit
- Vírus dependem da maquinaria celular do hospedeiro para a replicação; antibióticos antibac
- Na cadeia de transmissão de uma zoonose, os termos vetor e reservatório são sinônimos, poi
- Um teste com sensibilidade elevada torna irrelevante a probabilidade pré-teste, permitindo
- A PrEP é uma estratégia utilizada antes de possíveis exposições ao HIV em pessoa sem infec
- Após tratamento adequado da sífilis, todo teste treponêmico obrigatoriamente se torna não 
- A mensagem indetectável = intransmissível diz respeito à prevenção da transmissão sexual d
- A principal via de transmissão da tuberculose pulmonar contagiosa é a inalação de aerossói
- A infecção latente pelo bacilo da tuberculose deve ser considerada equivalente à doença pu
- Reações hansênicas podem ocorrer mesmo durante ou após a conclusão da poliquimioterapia e,
- A presença de dor articular intensa e persistente é particularmente sugestiva de chikungun
- Na dengue, o desaparecimento da febre exclui risco de extravasamento plasmático, permitind
- A febre amarela silvestre envolve vetores e primatas não humanos; eliminar macacos não con
- A leishmaniose tegumentar pode causar úlceras em pele e mucosas, enquanto a leishmaniose v
- A transmissão vetorial clássica da doença de Chagas ocorre diretamente pela saliva do barb
- Strongyloides stercoralis pode persistir por autoinfecção; o uso de corticoides em pessoa 
- Nas doenças diarreicas agudas, a avaliação do estado de hidratação orienta a prioridade te
- A apresentação típica da meningite bacteriana inclui líquor com neutrófilos predominantes,
- Todo contato familiar de uma pessoa com qualquer meningite, inclusive fúngica e parasitári
```

## Pedido 28: Contratações de Soluções de TIC (IN SGD/ME 94/2022) (`informatica.contratacoes-tic-in-94`, 205 tópicos de edital)

```
Matéria: informatica.contratacoes-tic-in-94 (Contratações de Soluções de TIC (IN SGD/ME 94/2022))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, vedações e alinhamento estratégico": 34
- "2. Planejamento: equipe, ETP e Termo de Referência": 33
- "3. Seleção do fornecedor, gestão do contrato e riscos": 33

Já existem (não repita):
- A IN SGD/ME 94/2022 disciplina as contratações de soluções de TIC pelos órgãos e entidades
- A Equipe de Planejamento da Contratação prevista na IN 94/2022 é composta pelos integrante
- Segundo a IN 94/2022, pode ser contratado mais de uma solução de TIC em um único contrato,
- A gestão de processos de TIC e a gestão de segurança da informação não podem ser objeto de
- Quando a avaliação ou o apoio à fiscalização da solução de TIC for objeto de contratação, 
- Segundo o art. 5º da IN 94/2022, é vedado adotar a métrica homem-hora para aferição de esf
- A IN 94/2022 veda indicar, no edital ou no contrato, pessoas expressamente nominadas para 
- É permitido aceitar autodeclarações de exclusividade apresentadas pela própria empresa pro
- As contratações de soluções de TIC devem estar em consonância com:
- O PMC-TIC é o preço máximo de compra adotado pelos órgãos do SISP para os itens dos Catálo
- As fases do processo de contratação de soluções de TIC, segundo o art. 8º da IN 94/2022, s
- A execução de todas as etapas do Planejamento da Contratação é obrigatória inclusive nos c
- O documento que detalha a necessidade da área requisitante e é utilizado para registrar a 
- Os papéis de integrantes da Equipe de Planejamento da Contratação podem ser livremente acu
- Segundo a IN 94/2022, o Estudo Técnico Preliminar da Contratação é realizado:
- Na análise comparativa de custos do ETP, a IN 94/2022 exige o cálculo dos custos totais de
- As soluções consideradas inviáveis na análise do ETP:
- A análise comparativa de soluções no ETP deve considerar, entre outros aspectos, a existên
- O Termo de Referência, na IN 94/2022, é elaborado:
- O contrato que prever a operação continuada de sistemas estruturantes de tecnologia da inf
- A elaboração do ETP é dispensada para contratações que mantenham todas as condições de edi
- No Termo de Referência, a Equipe de Planejamento deve avaliar a viabilidade de parcelar a 
- No DFD, o grau de prioridade da contratação deve ser classificado como:
- A fase de Seleção do Fornecedor inicia-se com o encaminhamento do Termo de Referência à ár
- A Equipe de Planejamento da Contratação é destituída:
- O papel de Gestor do Contrato pode ser acumulado com o de Fiscal Técnico quando o contrato
- O servidor designado como gestor ou fiscal do contrato de TIC:
- Na reunião inicial do contrato, registrada em ata, o representante legal da contratada apr
- O encaminhamento formal de demandas à contratada, por Ordens de Serviço ou de Fornecimento
- Segundo a IN 94/2022, o Termo de Recebimento Provisório é confeccionado e assinado pelo:
- Cabe ao Gestor do Contrato confeccionar e assinar o Termo de Recebimento Definitivo e auto
- Verificar a manutenção da necessidade, economicidade e oportunidade da contratação é atrib
- Verificar as regularidades fiscais, trabalhistas e previdenciárias para fins de pagamento 
- O inteiro teor do contrato de TIC e seus termos aditivos deve ser publicado em sítio eletr
- Para fins de prorrogação, o Gestor do Contrato deve encaminhar a documentação à área admin
- Contratos com itens dos catálogos padronizados e valores acima do PMC-TIC, cuja renegociaç
- Entre as atividades de transição e encerramento contratual estão a revogação de perfis de 
- Na IN 94/2022, o tratamento de riscos pode envolver as opções, não mutuamente exclusivas, 
- Nas licitações com itens dos Catálogos de Soluções de TIC com Condições Padronizadas, é ve
- Os produtos de software resultantes de serviços de desenvolvimento contratados devem ser:
```

## Pedido 29: Servidores web e de aplicação, proxy e balanceamento (`informatica.servidores-web-aplicacao`, 205 tópicos de edital)

```
Matéria: informatica.servidores-web-aplicacao (Servidores web e de aplicação, proxy e balanceamento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos dos servidores web e de aplicação": 22
- "2. Apache, Nginx, IIS, Tomcat e WildFly": 30
- "3. Proxy direto e reverso, API gateway, CDN e TLS": 16
- "4. Balanceamento, cluster e alta disponibilidade": 18
- "5. Administração de serviços e diagnóstico operacional": 14

Já existem (não repita):
- Uma aplicação entrega imagens locais e encaminha a pesquisa de produtos a outro processo H
- Uma aplicação Java utiliza Servlet e Jakarta Pages, com implantação típica em WAR. Qual pl
- Uma instituição obriga seus navegadores a usar serviço intermediário para autenticar e fil
- Uma equipe quer selecionar três instâncias equivalentes em sequência rotativa, sem usar o 
- Um portal é acessível ao digitar seu IP, mas o nome cadastrado não resolve no computador d
- Um host virtual do Apache HTTP Server deve publicar documentos guardados em `/srv/site`. Q
- Um IIS deve encaminhar `/vendas` e `/compras` a aplicações distintas por regras HTTP. Qual
- O Nginx precisa encaminhar `/pedidos` a outro servidor que fala HTTP na porta 8080. Qual d
- Uma conexão HTTPS termina no proxy e o proxy chama o backend por HTTP. Que afirmação descr
- Uma instância deixou de responder ao endpoint de prontidão e deve ser retirada automaticam
- O Nginx foi configurado para enviar scripts PHP a um processo PHP-FPM por um socket FastCG
- Uma aplicação mantém a sessão na memória local do nó escolhido. Deseja-se reduzir a troca 
- Um site atende várias regiões e pretende aproximar cópias cacheáveis de imagens dos usuári
- Um distribuidor decide com base em endereço IP e porta TCP, sem considerar URL nem cabeçal
- O proxy Nginx devolveu 504 ao aguardar uma origem que demorou além do tempo configurado. Q
- Em Nginx: `location /api/ { proxy_pass http://origem/v1/; }`. Para a requisição comum `/ap
- O Apache HTTP Server utiliza `ProxyPass` e o backend envia um cabeçalho de redirecionament
- Um nó de aplicação falha; o balanceador envia usuários a outro nó, mas os carrinhos guarda
- Uma aplicação atrás de proxy TLS gera redirecionamentos para `http://`, apesar de os clien
- Quatro instâncias estão saudáveis, mas toda entrada passa por um único balanceador sem red
- Um intermediário não pode decifrar o conteúdo HTTP transportado sob TLS, mas deve escolher
- Uma aplicação utiliza serviços corporativos de Jakarta EE além de Servlet. Como comparar a
- O health check TCP considera um backend apto porque sua porta está aberta, mas todas as tr
- Um serviço entrega documentos privados autenticados. A equipe pretende permitir cache comp
- Um gateway recebe `X-Forwarded-For` arbitrário de clientes que se conectam diretamente a e
- Se um servidor web serve uma página HTML local e, para um caminho específico, encaminha um
- Em uma implantação Java, Apache HTTP Server e Tomcat são denominações intercambiáveis para
- No Nginx, um grupo definido por `upstream` pode ser apontado por `proxy_pass` para encamin
- Uma vez que o DNS resolva o domínio `portal.exemplo`, ele também pode ler o caminho `/rela
- Um proxy reverso pode publicar aplicações internas sob um domínio externo, sem exigir que 
- O WildFly é uma ferramenta restrita a servir arquivos HTML estáticos e não fornece ambient
- Apesar de a porta TCP 443 ser a porta habitual de HTTPS, um servidor pode oferecer HTTPS e
- Uma infraestrutura com cinco servidores de aplicação se torna altamente disponível apenas 
- No IIS, o Application Request Routing associado a URL Rewrite pode encaminhar requisições 
- Uma CDN e um balanceador interno de aplicações são rigorosamente a mesma tecnologia, pois 
- Na seleção least connections, a quantidade de conexões ativas é critério de decisão, enqua
- Ao enviar scripts a PHP-FPM, `proxy_pass` e `fastcgi_pass` do Nginx utilizam o mesmo proto
- No Apache HTTP Server, `BalancerMember` pode declarar um membro de um conjunto gerenciado 
- Um proxy que termina HTTPS e encaminha HTTP puro ao backend mantém criptografado também o 
- Se o balanceador utiliza sticky session e o backend mantém a sessão somente na memória loc
- Um intermediário em TLS passthrough pode inspecionar livremente caminhos e corpos HTTP ain
- Um API gateway pode integrar roteamento, autenticação e limitação de taxa de requisições, 
- Uma resposta 502 emitida pelo proxy demonstra, isoladamente, que o banco de dados da aplic
- Quando a aplicação confia em `X-Forwarded-For`, a política de segurança deve considerar qu
- Uma CDN que usa TLS com os navegadores dispensa controles de cache para documentos pessoai
```

## Pedido 30: Filosofia e sociologia jurídica (`direito.filosofia-sociologia-juridica`, 205 tópicos de edital)

```
Matéria: direito.filosofia-sociologia-juridica (Filosofia e sociologia jurídica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito de Direito, moral, justiça e teoria política": 33
- "2. Jusnaturalismo, positivismos, princípios e integridade": 17
- "3. Hermenêutica, lógica do razoável e limites da dedução": 17
- "4. Sociologia jurídica: Weber, Durkheim, Marx, validade e eficácia": 19
- "5. Conflitos sociais, soluções não judiciais e síntese crítica": 14

Já existem (não repita):
- Uma regra publicada segundo o procedimento institucional é muito criticada por seu conteúd
- Um programa distribui recursos educacionais segundo critério relevante de necessidade, enq
- Um pesquisador descreve norma inferior cujo fundamento é encontrado em uma autorização nor
- Uma instituição identifica quais regras integram seu sistema pela prática dos responsáveis
- Em uma mediação entre vizinhos, o terceiro imparcial ajuda a construir entendimento sem de
- Um estudo identifica autoridade reconhecida por fidelidade pessoal a líder considerado ext
- Em determinada sociedade, a crescente divisão do trabalho cria interdependências e amplia 
- Uma decisão utiliza expressão legislativa ambígua. O intérprete compara linguagem, posição
- Um grupo precisa escolher princípios de convivência sem saber quais integrantes serão rico
- Uma norma foi regularmente instituída e segue em vigor, mas quase ninguém a observa. Qual 
- Uma regra de reconhecimento oficialmente aceita inclui requisito de compatibilidade com um
- Ao apreciar conflito difícil, um intérprete afirma que deve continuar a história jurídica 
- Uma decisão se apoia apenas na palavra 'segurança' para invalidar uma medida, sem discutir
- Um intérprete considera a finalidade protetiva de uma regra, a excepcionalidade da situaçã
- Dois países têm leis semelhantes, mas grande diferença na capacidade de grupos sociais ace
- Um contrato prevê mediação prévia. Um participante comparece ao primeiro encontro e decide
- Um crítico do formalismo descreve diferenças recorrentes entre regras anunciadas e decisõe
- Uma política pública é defendida por elevar a soma de bem-estar coletivo, enquanto outra é
- Um projeto institucional garante renda total maior, mas restringe liberdade de expressão d
- Uma crítica identifica o 'direito como integridade' com simples ponderação subjetiva de va
- Após omissão normativa genuína em conflito privado, o intérprete decide enfrentar a lacuna
- Uma pessoa afirma que a análise sociológica de um dever jurídico se esgota verificando se 
- Duas partes querem mediar controvérsia sobre direito indisponível que, no caso, admite tra
- Uma teoria identifica o Estado como organização duradoura, mas afirma que sua estrutura te
- Um participante entende que a teoria de Kelsen não poderia distinguir norma válida de norm
- Uma norma pode integrar validamente o ordenamento jurídico e, ao mesmo tempo, ser objeto d
- Na teoria de Hart, regras secundárias destinam-se unicamente a prescrever comportamentos c
- A justiça corretiva, na distinção aristotélica, pode ser mobilizada para justificar a repa
- Segundo Durkheim, a solidariedade orgânica decorre sobretudo da semelhança intensa entre t
- Na mediação regulada pela Lei nº 13.140/2015, o terceiro imparcial auxilia a solução conse
- O positivismo inclusivo rejeita qualquer possibilidade de critério moral integrar a valida
- Para Weber, dominação tradicional, carismática e racional-legal são tipos ideais de legiti
- A lógica do razoável de Recaséns Siches autoriza o intérprete a deixar de apresentar funda
- Uma investigação sobre por que duas regiões observam de maneira muito diferente a mesma re
- O art. 20 da LINDB permite decidir com fundamento isolado em valores jurídicos abstratos, 
- O ideal do romance em cadeia, em Dworkin, destaca que a interpretação deve ser fiel o bast
- Na mediação com cláusula contratual, a presença na primeira reunião implica obrigação de p
- Na teoria de Rawls, o princípio da diferença integra o segundo princípio de justiça e oper
- Para Kelsen, a norma fundamental é um dispositivo textual necessariamente promulgado pela 
- Uma crítica à escola da exegese pode reconhecer utilidade do silogismo jurídico e, ainda a
- Como a sociologia de Durkheim vincula direito repressivo à solidariedade mecânica, conclui
- Um termo final de mediação com acordo pode constituir título executivo extrajudicial e, se
- A arbitragem prevista no art. 3º, § 1º, do CPC é modalidade de autocomposição na qual o ár
- O conflito entre uma liberdade individual e pretenso benefício coletivo mostra que o utili
- Se partes vulneráveis aceitam um acordo formalmente voluntário, a sociologia jurídica fica
```

## Pedido 31: CGU e auditoria interna do Poder Executivo federal (`controle-externo.cgu-controle-interno-federal`, 204 tópicos de edital)

```
Matéria: controle-externo.cgu-controle-interno-federal (CGU e auditoria interna do Poder Executivo federal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Controle interno federal: fundamentos, finalidades e organização": 20
- "2. CGU: natureza, competências, estrutura e evolução normativa": 18
- "3. Correição, ouvidoria e integridade: três sistemas distintos": 27
- "4. Referencial Técnico: independência, avaliação, consultoria e riscos": 19
- "5. MOT: planejamento, execução, achados, relatório e monitoramento": 16

Já existem (não repita):
- Uma unidade federal vai avaliar metas do plano plurianual, execução de programas e garanti
- Ao preparar uma avaliação da execução de recursos federais, a equipe consulta a Lei nº 10.
- Diferentes unidades de controle interno divergem sobre um procedimento comum. Qual instânc
- Um responsável pelo controle interno conhece irregularidade identificada durante fiscaliza
- Em estudo sobre a atual organização federal, é preciso citar a lei que define as áreas de 
- Uma análise técnica busca avaliar a gestão e a execução de política pública pela vertente 
- Um cidadão comunica insatisfação com o atendimento recebido em serviço público federal. Qu
- Após surgirem indícios de conduta funcional irregular, é necessária atuação do Sistema de 
- Um plano institucional cita como norma vigente de integridade o Decreto nº 10.756/2021. Qu
- Uma equipe de integridade compara a definição do órgão central e das unidades setoriais no
- Uma unidade setorial do SCI recebe orientação técnica do órgão central. O dirigente conclu
- Uma entidade privada executou ação financiada com verba federal. A equipe de auditoria dis
- Em processo instaurado pela CGU, discute-se a competência do Ministro para aplicar suspens
- Uma Controladoria Regional da União executará ações em seu estado e precisa observar sua i
- Uma usuária propõe que um serviço federal ofereça formulário mais simples, sem relatar irr
- Uma autarquia prepara documento que organiza medidas de integridade para o próximo período
- Uma unidade de auditoria foi solicitada a orientar o desenho de controles de contratação s
- No planejamento anual, uma unidade possui pouco pessoal e deve escolher entre avaliar sist
- Um auditor ajudou diretamente a aprovar e implantar um controle, que depois pretende avali
- No MOT de 2017, uma equipe registra riscos e questões de auditoria, mas precisa vincular o
- Uma norma exige dupla aprovação de pagamento, mas amostra comprova autorização simples dev
- Durante fiscalização, a equipe apresenta três depoimentos convergentes, mas omite os regis
- Uma UAIG revisa os relatórios ao final, mas não avalia a gestão da atividade, a supervisão
- Um gestor discorda de achado documentado e apresenta posteriormente registros novos, enqua
- Uma unidade afirma que recomendação foi integralmente implementada porque o gestor enviou 
- Uma equipe do SCI examina se os recursos de um programa foram aplicados legalmente e se ho
- Uma comissão interna pretende substituir o controle externo no julgamento de contas federa
- Um relatório anual menciona a CCCI como colegiado responsável por favorecer integração e h
- Um curso atualizado descreve o Decreto nº 9.681/2019 como a norma regimental atualmente ap
- Ao receber proposta de simplificar o atendimento, a ouvidoria pode registrá-la como sugest
- Em 2026, um programa de integridade federal deve usar o Decreto nº 10.756/2021 como fundam
- Um auditor deve aprovar, em nome da gestão, cada pagamento que depois examinará, pois a fu
- Uma unidade do Sitai prepara plano de integridade para determinado período e o submete à a
- Uma unidade correcional entende que a redação original do Decreto nº 5.480/2005 permanece 
- Em uma auditoria, a matriz de planejamento vincula questões aos critérios, às fontes de in
- Se um sistema deixou de exigir autorização dupla, a condição do achado é a norma que exige
- Uma unidade de auditoria precisa avaliar a eficácia dos controles de compras. No modelo da
- A atividade de auditoria interna governamental prevista na IN SFC nº 3/2017 pode prestar t
- Um planejamento de auditoria deve priorizar o objeto com maior volume nominal de gastos em
- Ao verificar achado relativo a compras, a equipe identifica exigência de três cotações, co
- Uma unidade considera seu PGMQ completo porque há revisão editorial dos relatórios, embora
- No Sitai, uma autarquia pode ter unidade setorial responsável pela gestão de integridade, 
- Um relatório de auditoria contém achado bem comprovado, mas o gestor discorda. Pelo MOT, a
- O envio de ofício de aceite de recomendação encerra por si só o monitoramento de auditoria
- Na elaboração do relatório, a equipe avalia evidências contrárias à hipótese original e re
```

## Pedido 32: Crimes contra a família, o sentimento religioso e a paz pública (`direito-penal.crimes-familia-religiao-paz-publica`, 204 tópicos de edital)

```
Matéria: direito-penal.crimes-familia-religiao-paz-publica (Crimes contra a família, o sentimento religioso e a paz pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sentimento religioso e respeito aos mortos: arts. 208 a 212": 22
- "2. Crimes contra o casamento e o estado de filiação: arts. 235 a 243": 17
- "3. Assistência familiar, poder familiar, tutela e curatela: arts. 244 a 249": 17
- "4. Paz pública: incitação, apologia, associação e milícia": 32
- "5. Serviço postal, telegramas e integração dos tipos penais": 12

Já existem (não repita):
- Durante cerimônia religiosa, uma pessoa desliga intencionalmente o equipamento de som para
- Uma pessoa profana urna funerária, mas não remove o corpo nem perturba cerimônia de desped
- Uma pessoa legalmente casada contrai um segundo casamento civil sem que o primeiro tenha s
- Um adulto registra uma criança efetivamente nascida como se fosse seu filho biológico, emb
- Responsável com recursos deixa, sem justificativa, de prover a subsistência de filho menor
- Pessoa convence adolescente a fugir do lugar em que reside por determinação da guardiã, se
- Em transmissão aberta ao público, um indivíduo conclama espectadores a praticar determinad
- Três pessoas formam grupo estável com finalidade específica de praticar vários crimes patr
- Uma pessoa fabrica selo postal falso para utilizá-lo no pagamento de remessas. Qual é a de
- Sem autorização, uma pessoa abre e lê correspondência fechada endereçada ao vizinho. Qual 
- Um agente agride participante de cerimônia religiosa para interromper a celebração. Além d
- Um contraente ocultou impedimento matrimonial diverso de casamento anterior. O consorte en
- Com a intenção específica de impedir o exercício de direito sucessório, uma pessoa deixa f
- Visando lucro, um responsável entrega seu filho menor a terceiro, sabendo da situação de p
- Alguém subtrai criança da guarda judicial legítima. Dias depois, devolve-a sem maus-tratos
- Em manifestação pacífica, participantes defendem publicamente a mudança de uma lei penal, 
- Pessoa que não faz parte de grupo criminoso estável solicita a um de seus integrantes a pr
- Após receber de boa-fé um selo alterado, seu possuidor descobre a alteração e, ciente dela
- Uma pessoa ofende em tom de deboche uma autoridade religiosa durante conversa privada, sem
- Uma pessoa casada celebra segundo casamento. Posteriormente, sentença anula o primeiro vín
- Uma pessoa registra como seu o filho de outra e sustenta ter atuado por motivo de reconhec
- Pessoa solvente abandona voluntariamente o emprego para frustrar pagamento de pensão alime
- Quatro pessoas mantêm associação estável para cometer crimes, com participação de adolesce
- Colecionador reproduz peça filatélica valiosa, fazendo constar visivelmente em sua face qu
- Autoridade administrativa conhece crime relacionado ao serviço postal. A equipe cogita agu
- Durante ritual religioso, alguém perturba intencionalmente a celebração. A modalidade de p
- A ocultação de um cadáver, sem violação de sepultura ou urna funerária, é necessariamente 
- Após a Lei nº 11.106/2005, o adultério deixou de figurar como delito no art. 240 do Código
- Se existe criança efetivamente nascida, mas o agente a registra falsamente como filho próp
- O art. 246 do Código Penal tipifica deixar, sem justa causa, de prover à instrução primári
- Se houver violência empregada para impedir enterro, o art. 209 prevê aumento obrigatório d
- Pessoa solteira que sabe do casamento anterior do parceiro e, mesmo assim, contrai casamen
- No art. 236, a ocultação do casamento anterior constitui o impedimento típico, e a persecu
- O art. 245, § 2º, alcança quem auxilia envio de menor ao exterior com objetivo de lucro, m
- O genitor privado judicialmente da guarda está isento da incidência do art. 249 quando sub
- O parágrafo único do art. 286 também alcança a incitação pública à animosidade entre as Fo
- Segundo a ADPF 187, uma reunião pública pacífica para defender a descriminalização de cond
- Para incidir no art. 243, além de deixar filho próprio ou alheio em instituição de assistê
- Se o registro de filho de outrem como próprio for motivado por reconhecida nobreza, o art.
- O art. 244 menciona o ascendente inválido ou maior de 60 anos entre os beneficiários da pr
- O acordo de três pessoas para executar uma única infração em ocasião determinada configura
- Após a Lei nº 15.245/2025, a pessoa que solicita crime a integrante de associação criminos
- Para aplicar o art. 288-A ao custeador de milícia particular destinada à prática de crimes
- No art. 40 da Lei nº 6.538/1978, a sonegação ou destruição de correspondência alheia indev
- Pelo art. 44 da Lei nº 6.538/1978, a simples existência de pessoa jurídica relacionada a c
```

## Pedido 33: Marco Civil da Internet e delitos informáticos (`legislacao.marco-civil-internet`, 204 tópicos de edital)

```
Matéria: legislacao.marco-civil-internet (Marco Civil da Internet e delitos informáticos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, princípios e conceitos do Marco Civil da Internet": 14
- "2. Direitos dos usuários, neutralidade e tratamento de tráfego": 20
- "3. Sigilo, conservação e requisição de registros e dados": 19
- "4. Responsabilidade dos provedores e interpretação constitucional": 19
- "5. Delitos informáticos, segurança digital e integração normativa": 28

Já existem (não repita):
- Uma pessoa troca dados pela internet e utiliza um serviço de mensagens. Para classificar c
- Um contrato de acesso à internet contém cláusula sobre interrupção do serviço por inadimpl
- Um operador de rede reduz temporariamente tráfego para mitigar ataque de negação de serviç
- Uma empresa fornece conexão residencial e precisa cumprir a guarda ordinária dos registros
- Uma plataforma econômica organizada guarda os horários e endereços IP usados para acessar 
- Uma autoridade administrativa detém competência legal expressa e solicita a um provedor a 
- Uma empresa apenas fornece a conexão utilizada por terceiro que publicou conteúdo ofensivo
- Uma pessoa constata divulgação não autorizada de vídeo íntimo privado e notifica adequadam
- Um invasor acessa dispositivo de uso alheio, mesmo desconectado da internet, com o propósi
- Após a Lei nº 14.155/2021, qual pena corresponde ao caput do art. 154-A do Código Penal, c
- Um investigador precisa preservar registros de conexão antes de formular pedido de acesso 
- Um prestador de conexão pretende armazenar, em sua infraestrutura de acesso, a relação das
- Uma pessoa pede ao juiz registros de acesso a uma aplicação para instruir ação civil, indi
- Uma plataforma recebe reclamação sobre postagem inédita que supostamente atinge a honra de
- Uma pessoa invadiu dispositivo de uso alheio nos termos do art. 154-A e causou prejuízo ec
- Uma invasão de dispositivo resulta na obtenção de comunicações eletrônicas privadas. Qual 
- Uma plataforma divulga anúncios pagos com conteúdo ilícito gerado por terceiros. Qual efei
- Uma plataforma recebe comunicação sobre cópia literalmente idêntica de postagem já declara
- Uma pessoa solicita registros por via judicial e o provedor alega que uma requisição admin
- Uma empresa de acesso oferece velocidade preferencial, mediante contrato, aos pacotes de a
- Um provedor de aplicações sem atividade profissional econômica recebe ordem judicial para 
- Uma invasão tipificada no art. 154-A, § 3º, possibilita a obtenção de segredo comercial, p
- Uma pessoa invade dispositivo de concessionária de serviço público com finalidade e circun
- Após divulgação indevida de dados, uma aplicação requer ao seu usuário aceite para guardar
- Uma plataforma de mensagens privadas interpessoais é chamada a responder por conteúdo envi
- Uma pessoa utiliza uma plataforma de vídeos pelo celular. Para o art. 5º do Marco Civil da
- Uma provedora de conexão preserva por seis meses o registro da hora de início, duração e I
- Um serviço de conexão reduz tráfego devido a ataque que compromete a estabilidade de sua r
- Uma autoridade pede cautelarmente a preservação dos registros de uma conexão. Esse pedido 
- Uma pessoa exige esclarecimento sobre cláusulas de gerenciamento de tráfego que podem afet
- Um usuário divulga ofensa em rede social. Por ter transmitido os pacotes da postagem, o pr
- Uma pessoa invade computador de uso alheio para obter dados sem autorização. A tipicidade 
- Uma empresa de aplicações, organizada e com fins econômicos, afirma que o art. 15 manda re
- Um autor demonstra indícios de ilícito e explica por que os registros de determinado mês s
- Uma plataforma deixa de retirar vídeo íntimo privado depois de notificação idônea da pesso
- Uma aplicação guarda registros de acesso a outra aplicação, sem consentimento prévio do us
- Para o crime do art. 154-A, a Lei nº 14.155/2021 manteve como pena básica detenção de três
- Uma empresa insere cláusula de foro estrangeiro exclusivo em contrato de adesão sobre serv
- Uma plataforma divulga anúncio pago com ilícito evidente. Após a tese final dos Temas 987 
- Após invasão de dispositivo, o agente obtém segredos industriais e os vende. A pena da for
- Um serviço de mensagens interpessoais privadas é obrigado a monitorar previamente o teor i
- Uma autoridade administrativa dotada de competência legal requer a filiação de usuário, ma
- Um serviço de acesso oferece preço reduzido ao usuário desde que seus pacotes recebam prio
- Um programa é produzido e difundido com o objetivo específico de permitir invasões de disp
- Um crime do art. 154-A é praticado contra concessionária de serviços públicos. Mesmo nessa
```

## Pedido 34: Administração municipal: organização, carreira e posturas (`legislacao-municipal.administracao-municipal`, 204 tópicos de edital)

```
Matéria: legislacao-municipal.administracao-municipal (Administração municipal: organização, carreira e posturas)
Capítulos e quantas questões quero em cada um (total 100):
- "Organização administrativa municipal e competências locais": 17
- "Planos de cargos, carreiras e gestão de pessoal municipal": 17
- "Código de posturas e fiscalização de atividades locais": 17
- "Código tributário municipal: noções e aplicação prática": 17
- "Processo administrativo municipal e atendimento ao cidadão": 16
- "Controle interno, Câmara de Vereadores e ética funcional": 16

Já existem (não repita):
- Uma secretaria municipal, como órgão da administração direta, integra a pessoa jurídica Mu
- Em um organograma, a unidade denominada Secretaria Municipal de Fazenda geralmente se clas
- A simples criação de departamentos dentro da Secretaria de Obras caracteriza descentraliza
- Uma Prefeitura criou duas diretorias dentro da Secretaria de Obras, sem instituir novas pe
- A competência municipal para assuntos de interesse local autoriza o Município a afastar um
- Qual assertiva melhor descreve o alcance da autonomia municipal?
- Uma autarquia municipal é uma pessoa jurídica de direito público criada por lei específica
- Uma autarquia municipal distingue-se de uma secretaria principalmente porque:
- A conclusão de pós-graduação, por si só, faz surgir progressão remuneratória para todos os
- Um servidor efetivo concluiu curso superior e pediu mudança imediata de padrão. A providên
- Planos de cargos e carreiras podem estabelecer atribuições, requisitos de ingresso, padrõe
- Qual item costuma integrar um plano municipal de cargos e carreiras?
- Em todo PCCR municipal, a expressão “classe” designa obrigatoriamente progressão horizonta
- Um edital cita “classes, níveis e padrões” sem definir o sentido desses termos. Qual concl
- A função de confiança é reservada a ocupantes de cargo efetivo e se destina a atribuições 
- A respeito da função de confiança municipal, assinale a alternativa correta.
- O Código de Posturas Municipal pode disciplinar o uso de logradouros e aspectos de limpeza
- O Código de Posturas municipal tem como objeto mais característico:
- O pagamento de taxa municipal de fiscalização de estabelecimento afasta automaticamente a 
- Um comerciante pagou taxa de licença, mas instalou mesas além da área permitida no passeio
- A possibilidade de interdição de uma atividade pelo fiscal deve observar a base normativa,
- Um agente lavrou auto por descarte irregular de resíduos em área pública. Na análise de va
- Os prazos de defesa contra autos de infração e os valores das multas do Código de Posturas
- Sobre a relação entre Código de Posturas, Código de Obras e normas urbanísticas, assinale 
- Uma multa aplicada por infração ao Código de Posturas não é tributo, porque sua finalidade
- Em matéria tributária municipal, qual espécie se vincula à propriedade predial e territori
- O IPTU é um imposto municipal relacionado à propriedade predial e territorial urbana, nos 
- Um Município pretende exigir uma cobrança pela utilização potencial de serviço público esp
- A existência de um Código Tributário Municipal permite à Prefeitura instituir novo imposto
- Uma lei municipal instituiu tributo denominado “imposto sobre salários pagos na cidade”. O
- Uma taxa pode ter por pressuposto o exercício regular do poder de polícia ou a utilização,
- A valorização imobiliária decorrente de obra pública pode fundamentar, nos limites constit
- A Lei 9.784/1999 regula diretamente o processo administrativo federal, e seus prazos não d
- A lei federal 9.784/1999 é usada nesta matéria como:
- O contraditório e a ampla defesa tornam constitucionalmente impossível qualquer providênci
- Uma pessoa solicita cópia de contrato de limpeza urbana e o Município identifica dados pes
- O pedido de acesso a contrato municipal deve ser tratado à luz da LAI, e a proteção de dad
- Uma pessoa contesta a decisão municipal que indeferiu seu pedido de autorização. A medida 
- O recurso administrativo e o requerimento inicial possuem necessariamente a mesma finalida
- Após autuação municipal, a defesa cita prazo previsto em regulamento federal destinado exc
- O controle externo sobre a administração municipal é exercido pela Câmara Municipal, com a
- No sistema de fiscalização previsto pelo art. 31 da Constituição, a Câmara Municipal desem
- A Emenda Constitucional 139/2026 passou a admitir livre criação de novos Tribunais de Cont
- À luz da alteração constitucional de maio de 2026 sobre órgãos de contas, assinale a opção
- O parecer prévio sobre as contas anuais do Prefeito somente deixa de prevalecer por decisã
- O parecer prévio emitido sobre as contas anuais do Prefeito poderá deixar de prevalecer po
- A ética funcional permite utilizar dados cadastrais municipais para favorecer familiar, de
- Um servidor foi orientado a esconder fiscalização relativa à empresa de seu parente. Qual 
```

## Pedido 35: Proteção vegetal (entomologia, fitopatologia, plantas daninhas, MIP, receituário e embalagens) e fitotecnia (calagem, adubação, olericultura, fruticultura, colheita e mecanização) (`agronomia.protecao-vegetal-fitotecnia`, 203 tópicos de edital)

```
Matéria: agronomia.protecao-vegetal-fitotecnia (Proteção vegetal (entomologia, fitopatologia, plantas daninhas, MIP, receituário e embalagens) e fitotecnia (calagem, adubação, olericultura, fruticultura, colheita e mecanização))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Entomologia, fitopatologia e plantas daninhas": 20
- "2. Manejo integrado, controle e resistência": 20
- "3. Receituário agronômico e destinação de embalagens": 20
- "4. Fitotecnia geral, calagem, adubação, olericultura e fruticultura": 20
- "5. Colheita, pós-colheita, comercialização e mecanização": 20

Já existem (não repita):
- Os ácaros, como o ácaro-rajado, pertencem à classe dos insetos, pois possuem três pares de
- Inseto cujo desenvolvimento passa pelas fases de ovo, larva, pupa e adulto, como as maripo
- Insetos de aparelho bucal sugador, como a mosca-branca e a cigarrinha-do-milho, além de su
- Segundo o conceito do triângulo da doença, a ocorrência de doença em plantas depende da in
- As pústulas com esporos observadas na face inferior das folhas de soja atacadas pela ferru
- O vazio sanitário da soja, adotado em Mato Grosso e em outros estados, consiste em:
- Raízes de soja com engrossamentos arredondados (galhas) indicam ataque de nematoides do gê
- A seleção de populações de buva, capim-amargoso e caruru resistentes ao glifosato está ass
- O período da cultura em que ela deve ser mantida livre de plantas daninhas para não haver 
- Espécies exóticas invasoras, como certos capins africanos no Cerrado, podem ocupar áreas n
- No manejo integrado de pragas, o nível de controle corresponde a uma densidade populaciona
- O pano de batida é uma técnica de amostragem usada no monitoramento de lagartas e percevej
- A liberação de vespinhas Trichogramma, que parasitam ovos de lagartas, e a aplicação de ba
- O uso de feromônios sexuais em armadilhas para monitorar ou para causar confusão no acasal
- Nas lavouras com plantas Bt, a área de refúgio deve ser cultivada com:
- Para retardar a resistência de pragas e patógenos a agrotóxicos, recomenda-se aplicar semp
- No manejo ecológico de pragas, é recomendável:
- A receita agronômica para a compra de agrotóxico deve ser emitida por profissional legalme
- Entre as informações que devem constar da receita agronômica, NÃO se inclui:
- O intervalo de segurança (carência) de um agrotóxico corresponde ao número de dias:
- Após esvaziar uma embalagem rígida de agrotóxico miscível em água, o agricultor deve:
- Embalagens flexíveis ou que continham produtos não miscíveis em água não devem passar pela
- No sistema de logística reversa de embalagens vazias de agrotóxicos, cabe ao fabricante:
- Plantas de metabolismo C4, como milho, cana-de-açúcar e sorgo, tendem a apresentar maior e
- São pilares do sistema plantio direto:
- O Zoneamento Agrícola de Risco Climático (ZARC) indica, por município, cultura e tipo de s
- A calagem em solos ácidos tem como efeitos:
- O gesso agrícola corrige a acidez do solo de forma semelhante ao calcário, elevando o pH d
- Pelo método da saturação por bases, a necessidade de calagem é calculada por:
- Em solos tropicais, o fósforo é muito móvel e se perde facilmente por lixiviação, por isso
- Na enxertia de citros, o porta-enxerto é responsável principalmente por:
- São exemplos de frutos climatéricos, que continuam a amadurecer após a colheita:
- A propagação vegetativa, como estaquia, enxertia e micropropagação, produz plantas genetic
- A hidroponia é um sistema de cultivo sem solo, em que as plantas recebem os nutrientes por
- Grãos de milho colhidos com umidade elevada devem ser secos antes do armazenamento, até um
- A aeração de grãos armazenados em silos tem a finalidade de manter a temperatura da massa 
- A Cédula de Produto Rural (CPR) é um instrumento de comercialização e financiamento em que
- Contribuem para aumentar o risco de deriva na pulverização de agrotóxicos:
- A calibração do pulverizador é dispensável quando se usa produto de baixa toxicidade, pois
- A compactação do solo causada pelo tráfego de máquinas agrícolas é agravada quando:
- O subsolador é o implemento indicado para romper camadas compactadas situadas em profundid
```

## Pedido 36: Psiquiatria forense, toxicologia e perícias especiais (`medicina-legal.psiquiatria-forense-toxicologia`, 203 tópicos de edital)

```
Matéria: medicina-legal.psiquiatria-forense-toxicologia (Psiquiatria forense, toxicologia e perícias especiais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de psiquiatria forense e perícia": 18
- "2. Imputabilidade penal, capacidade civil e alienação mental": 20
- "3. Toxicologia forense, dependência e embriaguez": 24
- "4. Aborto e perícias médico-legais especiais": 16
- "5. Saúde mental no trabalho, interconsulta e equipes": 22

Já existem (não repita):
- Após uma agressão, foi apresentada perícia com diagnóstico antigo de transtorno psicótico 
- Em um hospital, uma pessoa apresenta desorientação, desatenção e flutuação da consciência.
- Laudo documenta que, no instante do fato, uma doença mental tornou o acusado inteiramente 
- Um laudo conclui que o acusado, por perturbação da saúde mental, tinha a capacidade de aut
- Uma pessoa adulta com deficiência intelectual solicita abertura de conta bancária e compre
- Uma sentença estabelece curatela proporcional a atos patrimoniais e negociais de pessoa co
- Em investigação de conduta ocorrida ontem, há resultado positivo para metabólito urinário 
- Um laboratório encontra substância psicoativa que tem uso medicamentoso regulado. Para cla
- Uma pessoa consumiu voluntariamente grande quantidade de álcool e praticou conduta típica.
- Foi demonstrado que alguém recebeu substância embriagante sem seu conhecimento e, por isso
- Uma substância intoxicante foi administrada por acidente comprovado. No momento do fato, a
- Durante uma ação penal, documentos recentes produzem dúvida fundamentada sobre a integrida
- No incidente de insanidade do acusado, os peritos necessitam ampliar o prazo do exame além
- Após coleta de amostra de sangue em caso suspeito de intoxicação, o recipiente é remetido 
- Uma triagem toxicológica por imunoensaio foi positiva para classe de medicamentos com conh
- Uma mulher provoca a interrupção de sua própria gestação, fora das hipóteses legais de não
- Uma pessoa provoca aborto em gestante que rejeitou expressamente o procedimento. Qual disp
- Uma gestante maior e capaz consente expressamente que terceiro provoque aborto, sem elemen
- Uma gestante tem complicação vital progressiva, e a avaliação clínica fundamentada conclui
- Gestante adulta relata gravidez decorrente de estupro e manifesta consentimento para abort
- Após confirmação clínica de anencefalia fetal, uma equipe precisa distinguir o fundamento 
- Uma organização implanta o gerenciamento de riscos ocupacionais segundo o capítulo 1.5 da 
- Uma paciente hospitalizada desenvolve desorientação após cirurgia, e a equipe solicita int
- Trabalhador com transtorno ansioso em tratamento apresenta sintomas controlados, mas exerc
- Após um fato investigado, o acusado desenvolve transtorno mental incapacitante apenas mese
- Uma perícia afirma que, embora o acusado tenha diagnóstico psiquiátrico, o art. 26 do Códi
- Um acusado possuía diagnóstico de esquizofrenia e utilizava medicação regularmente. Essa i
- Uma pessoa adulta com deficiência intelectual apresenta dificuldades para algumas decisões
- Alguém ingeriu álcool voluntariamente antes de praticar uma infração. A constatação de emb
- Ao estudar substância psicoativa potencialmente causadora de dependência, o perito deve di
- A presença de sangramento e perda gestacional, sem outros elementos, permite à perícia con
- Diante de dúvida fundamentada sobre a integridade mental do acusado, o art. 149 do CPP per
- Em incidente de insanidade, o exame pericial regulado pelo art. 150, § 1º, do CPP não pode
- Após recolher material biológico para investigar intoxicação, o registro da coleta, do lac
- Um metabólito foi detectado em urina dias após a exposição. O resultado permite estimar di
- Uma equipe informa que não existe outro meio de salvar a vida da gestante além da interrup
- Um terceiro provoca aborto após consentimento válido de gestante adulta. Na falta de exceç
- Na hipótese de anencefalia fetal adequadamente diagnosticada, o entendimento da ADPF 54 af
- Ao examinar pessoa com síndrome de abstinência após reduzir o consumo habitual, o perito d
- No âmbito da NR-1 com redação vigente desde 26 de maio de 2026, fatores de risco psicossoc
- Para cumprir a previsão de riscos psicossociais da NR-1, a organização precisa diagnostica
- Uma equipe multiprofissional identifica sintomas depressivos controlados em trabalhador. P
- Em infração que deixa vestígios materiais, o CPP permite substituir integralmente o exame 
- Em análise médico-administrativa, o uso da expressão alienação mental exige verificar o se
- Quando o laudo demonstra que o acusado perdeu integralmente a capacidade de entendimento a
- Os cáusticos que produzem escaras secas e endurecidas, por desidratação dos tecidos, como 
- A vitriolagem consiste no lançamento de substância cáustica, geralmente no rosto da vítima
- O emprego de veneno qualifica o crime de homicídio no Código Penal, exigindo a doutrina, e
- Entre os riscos agudos associados ao uso de MDMA (ecstasy), destaca-se:
- Os solventes inalantes, como cola e lança-perfume, atuam como depressores do sistema nervo
- Os barbitúricos, como o fenobarbital, caracterizam-se por:
- As novas substâncias psicoativas, como canabinoides e catinonas sintéticos, têm sua fórmul
```

## Pedido 37: Descontos simples e compostos (`matematica-financeira.descontos`, 202 tópicos de edital)

```
Matéria: matematica-financeira.descontos (Descontos simples e compostos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Valor nominal, valor atual e desconto comercial simples": 21
- "2. Desconto racional simples, taxa efetiva e capitais equivalentes": 21
- "3. Desconto racional composto e equivalência por capitalização": 21
- "4. Desconto comercial composto e conversão entre taxas": 17
- "5. Capitais equivalentes, desconto bancário e problemas integrados": 20

Já existem (não repita):
- Um título vence por R$ 5.000, mas sua antecipação hoje rende R$ 4.700. Qual é o desconto f
- Uma duplicata de R$ 4.000 é antecipada por três meses sob desconto comercial simples de 2%
- Uma obrigação de R$ 1.000 é descontada comercialmente, por fora, à taxa simples de 5% por 
- Um título de R$ 1.100 vence em um mês e será antecipado racionalmente pelo regime simples,
- Um título nominal de R$ 1.200 é antecipado por um período sob desconto racional simples à 
- Um título de R$ 2.000 vence em 45 dias e sofre desconto comercial simples de 2% ao mês. Ad
- Uma obrigação de R$ 10.000 será descontada comercialmente por fora à taxa simples de 10% a
- Um título de R$ 1.000 é antecipado por dois meses; o valor atual comercial simples foi R$ 
- Um título de R$ 2.400 a vencer em dois meses é descontado racionalmente a juros simples de
- Um título tem nominal de R$ 1.000 e é antecipado por R$ 950, sem tarifa. Independentemente
- Uma empresa antecipa por dois meses título de R$ 10.000 a desconto comercial simples de 3%
- Um título de R$ 10.000 é descontado racionalmente a juros de 25% por um período. Qual quan
- Uma nota de R$ 12.100 vence após dois períodos e é trazida ao presente por juros compostos
- Uma obrigação de R$ 10.000 sofre desconto comercial composto de 10% por período, faltando 
- Um título sofre desconto comercial composto de 20% em um período. Qual taxa de juros compo
- Uma antecipação comercial simples usa taxa de desconto de 10% por período durante dois per
- Um título de R$ 1.200 vence após dois meses e é descontado racionalmente sob juros simples
- Uma instituição divulga desconto comercial composto de 10% ao mês. Qual taxa bimestral de 
- Um título de R$ 1.331 vence em três períodos, sendo a taxa de juros compostos de 10% por p
- Um banco desconta comercialmente título de R$ 20.000 a 2% mensais simples por dois meses e
- Um título foi descontado comercialmente pelo regime simples a 5% por período durante quatr
- Um valor nominal de R$ 10.000 foi descontado comercialmente no regime composto por dois pe
- O valor atual de um título é R$ 1.000 e seu nominal após dois períodos será R$ 1.440, cons
- Duas obrigações vencem em um e dois meses por R$ 1.100 e R$ 1.210, respectivamente. Em jur
- Para antecipar por dois períodos um nominal de R$ 1.000, usam-se quatro modalidades distin
- Em desconto comercial simples, a taxa periódica é aplicada ao valor nominal do título, e o
- Um título com valor nominal de R$ 2.000 e atual de R$ 1.800 concede desconto financeiro de
- A modalidade racional simples trata o valor atual como base de incidência dos juros de ant
- Ao antecipar um título por 45 dias com taxa de desconto mensal e mês comercial de 30 dias,
- No desconto racional composto, o nominal de um título é trazido ao presente por divisão pe
- Uma taxa de juros compostos de 10% ao mês equivale a exatamente 20% ao bimestre, pois bast
- Uma taxa comercial composta de desconto de 20% por período é financeiramente equivalente a
- Um título de R$ 1.200, antecipado racionalmente no regime simples por dois meses a juros d
- No desconto comercial composto de 10% ao mês por dois meses, o valor atual corresponde a 8
- Um título de R$ 1.000 descontado comercialmente por fora sob taxa simples de 10% mensal du
- No desconto racional composto, converter taxa efetiva mensal de 2% em taxa anual exige pre
- No desconto comercial simples de 10% por período durante dois períodos, a taxa de juros si
- Um título sujeito a desconto comercial composto de 20% por período durante dois períodos t
- Um título de R$ 10.000 descontado comercialmente no regime simples à taxa de 10% por perío
- Duas obrigações de R$ 1.100 em um mês e R$ 1.210 em dois meses equivalem, juntas, a R$ 2.0
- Se um banco anuncia desconto comercial simples e cobra tarifa adicional, a taxa efetiva da
- Para mesmo nominal, mesma taxa numérica positiva e prazo de dois períodos, o desconto raci
- No desconto racional simples de um título com nominal positivo, o valor atual torna-se neg
- Uma taxa de desconto comercial composto de 10% ao mês é equivalente a 19% ao bimestre, por
- No desconto comercial simples, a taxa de juros simples equivalente ao desconto anunciado d
```

## Pedido 38: Produtos e serviços bancários (`conhecimentos-bancarios.produtos-servicos-bancarios`, 201 tópicos de edital)

```
Matéria: conhecimentos-bancarios.produtos-servicos-bancarios (Produtos e serviços bancários)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Contas, depósitos, cheques e aplicações de renda fixa": 22
- "2. Pagamentos, cobranças, cartões e canais bancários": 22
- "3. Crédito ao consumidor, operações empresariais e leasing": 21
- "4. Crédito rural, investimentos e produtos de proteção": 23
- "5. Consórcios, contratos, serviços bancários e decisões integradas": 12

Já existem (não repita):
- Marina possui R$ 600 de saldo em conta e limite contratado de cheque especial de R$ 2.000.
- Ao orientar novo cliente sobre uma aplicação representativa de depósito a prazo, a institu
- Uma pessoa quer pagar instantaneamente um prestador de serviço, em fim de semana, utilizan
- Uma loja entrega mercadoria e o comprador realiza a compra em cartão de crédito, para quit
- Um trabalhador contrata empréstimo com parcelas descontadas diretamente de remuneração, se
- Uma fábrica precisa de financiamento para adquirir torno industrial que operará por vários
- Uma agricultora solicita recursos para aquisição de sementes e fertilizantes destinados à 
- Uma associação de consumidores reúne cotas mensais e prevê aquisição de automóveis por cré
- Uma pessoa compara PGBL e VGBL como soluções de acumulação para renda futura. Qual distinç
- O proprietário de pequena empresa dispõe de duplicata a receber em 45 dias e obtém recurso
- Em contrato de uso de equipamento, uma instituição compra o bem escolhido pelo usuário e m
- Uma pessoa compra título de capitalização com sorteios e supõe que todas as mensalidades s
- Uma empresa recebe boletos de clientes por serviço de cobrança simples e pretende aumentar
- Em oferta de financiamento de eletrodoméstico, a instituição apresenta valor financiado, p
- Uma cooperativa precisa distinguir crédito rural para plantar milho daquele destinado a co
- Um investidor pretende fazer portabilidade direta de reserva acumulada em VGBL para PGBL, 
- Em conta conjunta, um cheque emitido por apenas um dos titulares foi devolvido por insufic
- Uma sociedade financeira oferece letra de câmbio para captação e outra instituição oferece
- Uma administradora de consórcios inclui no contrato taxa de administração, fundo comum, re
- No arrendamento mercantil financeiro de uma máquina, o usuário arca com manutenção e manif
- Uma empresa recebe pagamento de clientes por boleto, usa conta garantida para pagar fornec
- Um cliente investe em CDB anunciado como pós-fixado, com vencimento determinado, e supõe q
- Uma instituição anuncia 'CDC para automóvel' e outra 'empréstimo pessoal'. Ambos liberam v
- Um segurado de plano patrimonial sofre dano não descrito entre riscos cobertos e exige ind
- Uma cooperativa financia compra de matéria-prima para beneficiamento agroindustrial realiz
- Uma cliente abre conta de depósitos eletronicamente e o atendente afirma que, para identif
- Um cliente compra cotas de fundo de investimento em aplicativo bancário e conclui que real
- Uma pessoa habilita o débito automático de sua conta de energia e recebe, a cada período, 
- Uma compra feita em cartão de débito é registrada como utilização de limite de crédito com
- Uma empresa adquire equipamentos para ampliar sua capacidade produtiva por financiamento d
- Na compra de um título de capitalização com sorteios, todo o pagamento periódico é incorpo
- Um grupo de consórcio prevê contemplações por sorteio e lances, e a taxa de administração 
- Em uma conta conjunta, a segunda devolução por falta de fundos de cheque assinado por um c
- Um cliente contrata CDB com vencimento e carência para resgate; a simples classificação co
- Após o encerramento da oferta regular do DOC, as instituições passaram a classificar toda 
- Uma indústria antecipa recursos de duplicatas de clientes antes do vencimento e assume as 
- Uma empresa com necessidade de liquidez por dois dias contrata hot money, cuja classificaç
- Uma pessoa contrata plano VGBL e outra PGBL. Embora os produtos tenham finalidade de acumu
- Uma pessoa ingressa em consórcio e recebe a garantia de contemplação apenas por ter pago a
- Uma sociedade arrendadora compra máquina indicada pelo cliente, conserva a titularidade e 
- Para comprar LCI, uma cliente deve participar diretamente de condomínio de imóveis, pois o
- Um boleto com QR Code Pix adequado pode ser pago por esse arranjo instantâneo; contudo, a 
- Em serviço de cobrança caucionada, o recebível pode servir de garantia de operação de créd
- Uma atividade rural obtém recursos para aquisição de equipamento destinado a diversos cicl
- Uma pessoa que paga integralmente a fatura do cartão de crédito tem, só por haver comprado
```

## Pedido 39: Regimento Interno da Câmara, Regimento Comum e Código de Ética (`processo-legislativo.regimento-camara-congresso`, 201 tópicos de edital)

```
Matéria: processo-legislativo.regimento-camara-congresso (Regimento Interno da Câmara, Regimento Comum e Código de Ética)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Câmara dos Deputados: disposições preliminares, Mesa e órgãos": 18
- "2. Sessões plenárias, Ordem do Dia e procedimento de debates": 18
- "3. Deputados, mandato, licenças, vacância e economia interna": 22
- "4. Regimento Comum: sessões conjuntas, PEC e processo orçamentário": 25
- "5. Código de Ética, Conselho de Ética e responsabilização parlamentar": 17

Já existem (não repita):
- Após problema estrutural no Palácio do Congresso, a direção pretende realizar sessão em ou
- Ao preparar organograma da Mesa da Câmara dos Deputados, um assessor precisa indicar sua c
- Na eleição da Mesa não se alcançou maioria absoluta de votos no primeiro escrutínio, embor
- Uma sessão da Câmara foi convocada exclusivamente para homenagem especial, com previsão de
- Um parlamentar entrega carta de renúncia à Mesa da Câmara, mas o documento ainda não foi l
- O responsável pela pauta da Câmara verifica os presentes antes de começar a apreciar matér
- Ao elaborar agenda de reuniões, um servidor identifica o horário regimental das sessões de
- A Câmara avalia alteração na sua estrutura administrativa por meio de proposição legislati
- Na organização da sessão conjunta de posse do Presidente e Vice-Presidente da República, é
- Não há data legal previamente estabelecida para sessão conjunta do Congresso. A secretaria
- O cidadão compara o período da sessão legislativa ordinária com a realização de reuniões p
- Uma nota técnica afirma que o Código de Ética da Câmara foi criado pela Resolução nº 2/201
- Um deputado formaliza reclamação sobre irregularidade em serviço administrativo e, após en
- Ao definir competências de execução do orçamento da Câmara, o setor administrativo pergunt
- Uma apostila antiga descreve votação de PEC em sessão conjunta segundo arts. 72 a 84 do Re
- Durante sessão da Câmara, um parlamentar perturba o andamento dos trabalhos e recebe censu
- Em assunto reservado, requer-se que sessão plenária da Câmara ocorra sem público. Segundo 
- No julgamento de conduta consistente em revelar documento oficial sigiloso, o Conselho de 
- O Presidente do Senado recebe a mensagem presidencial com o projeto de lei orçamentária e 
- Três agremiações pretendem criar bloco parlamentar na Câmara, mas não sabem o percentual m
- Uma representação no Conselho de Ética discute perda de mandato, enquanto outra versa sobr
- Uma sessão plenária da Câmara foi interrompida uma vez pelo Presidente por quarenta minuto
- O Congresso realizará solenidade de posse presidencial, mas parte dos parlamentares está a
- Uma representação por conduta cuja sanção é suspensão de prerrogativas foi recebida para i
- Uma deputada pretende reassumir mandato após deixar cargo de Ministra de Estado, mas um as
- Em razão de força maior, a Mesa da Câmara delibera realizar uma sessão em edifício diferen
- Na eleição da Mesa da Câmara, o Regimento exige maioria simples de votos no primeiro escru
- Uma sessão da Câmara convocada para debates gerais na segunda-feira, sem Ordem do Dia e co
- O Código de Ética e Decoro Parlamentar da Câmara foi instituído originalmente pela Resoluç
- Uma proposta de reestruturação de serviços administrativos da Câmara necessita de parecer 
- A renúncia escrita entregue à Mesa é irretratável desde o protocolo, mesmo que não tenha s
- Pelo Regimento Comum, as sessões conjuntas sem data legalmente fixada são convocadas pelo 
- Em caso de reunião conjunta do Congresso para promulgação de emenda constitucional, o Regi
- No Código de Ética da Câmara, a suspensão de prerrogativas regimentais e a suspensão do ex
- Uma sessão da Câmara interrompida pelo Presidente pode ser suspensa novamente quantas veze
- No tratamento de reclamação sobre irregularidade nos serviços administrativos da Câmara, a
- Nas sessões solenes conjuntas do Congresso, o Regimento Comum exige a presença de maioria 
- Em situação de prisão em flagrante por crime inafiançável de Deputado, o art. 250 do RICD 
- A Resolução nº 25/2001, ao instituir o Código de Ética, manteve a eficácia integral dos ar
- O relator de representação disciplinar destinada à perda de mandato dispõe, para a instruç
- Para perda de mandato por decoro, o Código de Ética mantém a votação secreta por maioria s
- O Regimento Comum prevê que a mensagem encaminhando o projeto de lei orçamentária seja rec
- A prestação anual de contas da Câmara relativa ao exercício anterior é enviada ao Tribunal
- O deputado que se desvincula da bancada perde em regra o direito a funções regimentais atr
- No procedimento de suspensão de prerrogativas regimentais previsto no art. 13 do Código, a
```

## Pedido 40: Legislação marítima, carreira do aquaviário e prevenção da poluição (`marinha-mercante.legislacao-maritima-poluicao`, 200 tópicos de edital)

```
Matéria: marinha-mercante.legislacao-maritima-poluicao (Legislação marítima, carreira do aquaviário e prevenção da poluição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Autoridade Marítima, águas jurisdicionais e LESTA": 15
- "2. Carreira do aquaviário e trabalho a bordo": 35
- "3. Cargas perigosas, Lei 9.966/2000 e MARPOL": 20
- "4. Poluição aquaviária: tipos, prevenção e órgãos": 30

Já existem (não repita):
- No Brasil, o Comandante da Marinha é designado Autoridade Marítima, atuando por meio da Di
- O mar territorial brasileiro compreende uma faixa de:
- Na zona econômica exclusiva, que se estende até 200 milhas náuticas, o Brasil tem direitos
- Segundo a Lei 9.537/1997 (LESTA), aquaviário é:
- Na LESTA, a tripulação de segurança corresponde à quantidade máxima de pessoas autorizadas
- Pela LESTA, o comandante, para garantir a segurança das pessoas, da embarcação e da carga,
- NÃO está entre os poderes atribuídos pela LESTA ao comandante da embarcação:
- Todas as pessoas a bordo da embarcação, segundo a LESTA, estão sujeitas à autoridade do co
- São penalidades previstas na LESTA para as infrações às suas normas:
- Na LESTA, o comandante também pode ser denominado mestre, arrais ou patrão.
- Segundo o RLESTA, os aquaviários são classificados em grupos. NÃO é um desses grupos:
- A Caderneta de Inscrição e Registro (CIR) identifica o aquaviário, comprova sua inscrição 
- O documento da embarcação que relaciona os tripulantes embarcados, com suas funções e as d
- A formação e o aperfeiçoamento dos aquaviários são realizados pelo Ensino Profissional Mar
- A ascensão de categoria na carreira do marítimo depende, entre outros requisitos, de:
- A NR-30 trata da segurança e saúde no trabalho aquaviário e prevê, nas embarcações, o Grup
- A convenção internacional que trata de jornada, descanso, alojamento, alimentação, saúde e
- Os aquaviários são segurados obrigatórios da previdência social, e o trabalho a bordo é re
- As categorias de moço de convés e de moço de máquinas são, na carreira do marítimo, catego
- As mercadorias perigosas transportadas em embalagens, a bordo de navios, seguem principalm
- Na classificação de mercadorias perigosas, a classe 3 corresponde a:
- A segregação entre mercadorias perigosas de classes incompatíveis é uma das medidas de seg
- A Lei 9.966/2000 classifica as substâncias nocivas ou perigosas em quatro categorias, send
- A Lei 9.966/2000 prevê planos de emergência individuais para portos, plataformas e instala
- São responsáveis pelo cumprimento da Lei 9.966/2000, cada um em sua esfera:
- O Anexo da MARPOL que trata da prevenção da poluição por lixo dos navios é o:
- Pelo Artigo VI da MARPOL, um navio pode ser inspecionado no porto de outro Estado-Parte pa
- No Livro de Registro de Óleo, a Parte I destina-se às operações da praça de máquinas de to
- Em petroleiros de óleo cru, a técnica de limpeza de tanques que usa o próprio petróleo da 
- A MARPOL permite o lançamento de plásticos no mar quando o navio estiver a mais de 12 milh
- A água de lastro pode transportar espécies exóticas invasoras entre regiões, motivo pelo q
- As tintas anti-incrustantes com compostos organoestânicos, como o TBT, foram proibidas por
- Durante uma transferência de óleo combustível a bordo, uma medida preventiva básica para e
- O SOPEP é o plano de emergência de bordo para poluição por óleo, que orienta a tripulação 
- NÃO é exemplo de poluição aquaviária causada por navios:
- Pela legislação ambiental brasileira, o poluidor responde pela reparação do dano ambiental
- O órgão federal de meio ambiente que licencia e fiscaliza atividades como plataformas de p
- A emissão de óxidos de enxofre e de nitrogênio pelos motores dos navios é regulada pelo An
- A zona contígua, onde o Estado pode fiscalizar questões aduaneiras, fiscais, de imigração 
- Em caso de impedimento do comandante, a LESTA determina que ele seja substituído:
- Quanto à Lei 9.966/2000, assinale a alternativa correta.
```

## Pedido 41: Avaliação psicológica, psicodiagnóstico e psicometria (`psicologia.avaliacao-psicologica-psicometria`, 197 tópicos de edital)

```
Matéria: psicologia.avaliacao-psicologica-psicometria (Avaliação psicológica, psicodiagnóstico e psicometria)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da avaliação psicológica e do psicodiagnóstico": 17
- "2. Entrevistas, observação e seleção de instrumentos": 24
- "3. Medida psicológica, escores e fidedignidade": 25
- "4. Validade, elaboração e análise de itens psicológicos": 18
- "5. Integração dos resultados, documentos e decisões responsáveis": 16

Já existem (não repita):
- Uma profissional recebe pedido para compreender por que um estudante passou a ter queda de
- Durante uma avaliação, são usados entrevista, anamnese e observação estruturada, sem aplic
- Ao investigar o histórico de uma pessoa, a psicóloga utiliza roteiro comum de tópicos, mas
- Em um grupo normativo, uma pessoa recebeu escore no percentil 80 em uma tarefa de memória.
- Ao revisar um instrumento, a pesquisadora verifica se suas perguntas abrangem todas as dim
- Uma equipe considera usar um relatório fisioterapêutico e uma entrevista psicológica na me
- Um profissional coleta relato de sono insuficiente e observa demora na execução de uma tar
- Uma escala apresenta resultados bastante semelhantes entre duas aplicações próximas, em co
- Uma profissional encontra um teste com parecer favorável no SATEPSI, mas pretende aplicá-l
- Após avaliação, uma psicóloga precisa documentar procedimentos, análise fundamentada e con
- Na construção de escala de estresse, um item diz: 'Sou paciente e durmo bem'. Por que esse
- Em uma prova de 100 pessoas, 85 acertam determinado item. Qual interpretação do índice clá
- Um inventário apresenta alta consistência interna e a equipe conclui que ele mede corretam
- Um formulário de avaliação não privativo, com apoio científico, é usado junto à entrevista
- Uma pesquisadora correlaciona escores obtidos hoje com desempenho observado seis meses dep
- Uma avaliação usa observação estruturada em que dois avaliadores classificam independentem
- Um teste antigo deixou de ter parâmetros psicométricos atualizados e consta como desfavorá
- Uma escala de avaliação mostra desempenho quase idêntico entre homens e mulheres. A pesqui
- Uma pessoa apresenta escore elevado em escala de triagem para sintomas depressivos. A cond
- A mesma bateria produz escores idênticos em aplicações repetidas, mas seus itens avaliam v
- Na construção de inventário, a equipe analisa o agrupamento dos itens em fatores e verific
- Um instrumento de personalidade possui normas somente para adultos, mas seu escore será in
- Uma equipe decide aplicar novamente o mesmo teste após três dias, afirmando que toda reava
- Dois grupos de desempenho alto e baixo acertam determinado item em proporções praticamente
- Em uma avaliação, a psicóloga usa procedimento informatizado e modifica as instruções de u
- Uma avaliação psicológica profissional pode combinar entrevistas e registros de observação
- O psicodiagnóstico é definido pela correção de testes psicológicos, sendo o histórico clín
- Na entrevista semiestruturada, o profissional pode utilizar um roteiro comum e aprofundar 
- Quando um resultado é apresentado no percentil 70, isso informa que o avaliado acertou nec
- A precisão teste-reteste examina a estabilidade dos resultados entre aplicações, respeitad
- No sistema clássico de análise de itens, uma proporção de acertos igual a 0,90 indica que 
- Segundo a Resolução CFP nº 06/2019, o laudo psicológico decorre de processo de avaliação p
- Um relatório multiprofissional cientificamente fundamentado pode ser a única base de uma c
- Uma entrevista pode empregar perguntas abertas para explorar o relato e perguntas fechadas
- Um instrumento com aplicação informatizada prevista no manual está automaticamente autoriz
- A precisão interavaliadores trata da consistência das classificações de diferentes observa
- O alfa de Cronbach elevado torna desnecessário examinar estrutura interna e relações exter
- Uma escala que cobre apenas uma pequena parte do domínio definido pode apresentar problema
- A guarda mínima de documentos e materiais que fundamentam os serviços psicológicos, previs
- Na teoria clássica dos testes, um item respondido corretamente por 80% dos participantes t
- O parâmetro de dificuldade na TRI corresponde necessariamente à mesma proporção de acertos
- Conforme as orientações do SATEPSI, não há prazo profissional único para toda reaplicação 
- Se um teste foi considerado favorável em determinado ano, seu uso profissional posterior p
- Mesmo em um teste com sensibilidade e especificidade adequadas, o valor preditivo positivo
- A igualdade aproximada das médias de escores de dois grupos elimina a necessidade de exami
```

## Pedido 42: Educação especial e inclusiva (`educacao.educacao-especial-inclusiva`, 197 tópicos de edital)

```
Matéria: educacao.educacao-especial-inclusiva (Educação especial e inclusiva)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, público-alvo e marcos normativos": 20
- "AEE, sala de recursos, profissionais e colaboração": 20
- "Deficiências, TEA e altas habilidades: implicações pedagógicas": 20
- "Libras, Braille, acessibilidade curricular e tecnologia assistiva": 20
- "Estudo de caso, PAEE, PEI e situações de prova": 20

Já existem (não repita):
- Uma coordenadora afirma que a finalidade do AEE é substituir as aulas regulares dos estuda
- Em relação ao público-alvo da política federal de educação especial inclusiva, qual conjun
- Em qual situação o trabalho do AEE assume caráter suplementar?
- Qual medida é mais adequada para tornar acessível um gráfico em aula de geografia a uma al
- O que melhor descreve a Língua Brasileira de Sinais?
- Uma escola recebeu estudante com TEA e informou que só providenciará o AEE após laudo médi
- Em uma turma com aluno com deficiência física, qual providência respeita o princípio inclu
- Para estudante com altas habilidades em ciências, qual proposta tende a ser mais coerente?
- Ao elaborar o estudo de caso, a equipe observa barreiras, potencialidades e recursos. Que 
- Qual é a diferença mais apropriada entre a sala de recursos multifuncionais e a classe com
- Estudante com baixa visão relata desconforto com folhas ampliadas. Qual é a melhor respost
- Qual papel cabe ao profissional de apoio escolar na regulamentação atual?
- Uma professora propõe uma avaliação oral para aluno com limitação motora de escrita, mante
- Na educação bilíngue de surdos disciplinada pela LDB, qual organização linguística é refer
- Na seleção de tecnologia assistiva para comunicação, qual critério deve orientar a escola?
- Uma equipe deseja atualizar planejamento de estudante autista após mudanças de rotina esco
- Uma criança usa comunicação alternativa com símbolos para responder questões. Qual interpr
- Considere estudante com altas habilidades em matemática e dificuldade persistente na produ
- Qual leitura dos Decretos nº 12.686/2025 e nº 12.773/2025 é tecnicamente adequada?
- Um sistema de ensino exige que todas as pessoas cegas realizem exclusivamente atividades o
- Uma escola particular cobra taxa adicional obrigatória por recursos de inclusão previstos 
- Em relação ao Plano Educacional Individualizado, qual prática revela monitoramento pedagóg
- Em reunião escolar, a família exige que o aluno com TEA seja retirado permanentemente de a
- Uma aluna surda sinalizante recebe intérprete, mas não encontra materiais didáticos acessí
- O AEE tem caráter pedagógico e deve dialogar com as práticas da classe comum.
- A presença de deficiência física permite concluir automaticamente que o estudante possui d
- Libras é uma língua de modalidade visual-espacial, com estrutura própria.
- O Sistema Braille tem células compostas por seis pontos para leitura e escrita tátil.
- Todo estudante com altas habilidades ou superdotação necessariamente apresenta excelência 
- A sala de recursos multifuncionais é espaço possível para a organização do AEE, mas não su
- A escola pode condicionar o AEE, em qualquer situação, à apresentação de laudo médico com 
- Um aluno autista pode necessitar de apoios de comunicação e de previsibilidade sem apresen
- O profissional de apoio escolar pode substituir integralmente o docente regente, desde que
- Na educação bilíngue de surdos prevista na LDB, Libras é primeira língua e português escri
- Usar um recurso digital com leitor de tela basta, por si só, para assegurar aprendizagem d
- Em uma avaliação adaptada, é possível manter o objetivo curricular e flexibilizar o meio p
- O Decreto nº 12.773/2025 alterou a disciplina federal da PNEEI e explicitou os instrumento
- A avaliação biopsicossocial, quando disponível, deve ser o único fundamento do estudo de c
- O direito a profissional de apoio escolar decorre automaticamente da mera matrícula de qua
- Um PEI pode prever metas educacionais mensuráveis e revisão dos apoios sem transformar o d
- Na seleção de recursos para baixa visão, o uso obrigatório de Braille é regra universal ap
- A tecnologia assistiva pode abranger adaptações simples de baixo custo e estratégias de co
```

## Pedido 43: Fazenda Pública em juízo, precatórios e requisições de pequeno valor (`processo-civil.fazenda-publica-em-juizo-precatorios`, 197 tópicos de edital)

```
Matéria: processo-civil.fazenda-publica-em-juizo-precatorios (Fazenda Pública em juízo, precatórios e requisições de pequeno valor)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fazenda Pública em juízo: representação, prazos e honorários": 16
- "2. Remessa necessária, tutela provisória e suspensão de decisões": 22
- "3. Prescrição quinquenal e ações contra a Fazenda Pública": 24
- "4. Cumprimento de sentença, execução e requisição de pequeno valor": 19
- "5. Precatórios, preferências, atualização, ordem cronológica e regime de 2025": 19

Já existem (não repita):
- Uma autarquia estadual é intimada pessoalmente para apresentar manifestação cujo prazo ger
- Uma sentença líquida condena município que não é capital ao pagamento de oitenta salários 
- Uma pessoa pretende cobrar judicialmente dívida comum contra município, sem regra especial
- Um título judicial definitivo contra autarquia federal reconhece crédito de quarenta salár
- Na elaboração da proposta orçamentária para precatórios apresentados em 2026, qual data de
- Em cumprimento de sentença pecuniária, o credor apresenta cálculo contra a Fazenda. Qual p
- Uma liminar impõe medida capaz de comprometer gravemente serviço público essencial, confor
- Em ação contra a Fazenda, a condenação líquida equivale a trezentos salários mínimos. Como
- O credor inicia cumprimento de sentença contra a Fazenda, destinado a precatório; a execut
- Uma sentença líquida condena estado ao pagamento de exatamente quinhentos salários mínimos
- Em demanda relativa a parcelas mensais que a Fazenda deixou de pagar parcialmente, não hou
- O exequente apresenta execução fundada em contrato que constitui título extrajudicial cont
- Uma decisão condena a Fazenda ao pagamento de verbas de natureza alimentar. Entre os credo
- Após sentença líquida, a Fazenda é intimada para cumprir obrigação de pagar quantia. O cre
- Em ação civil pública, o juiz considera liminar contra ente público e não está caracteriza
- Uma pessoa apresenta reclamação administrativa de dívida líquida contra ente público e a a
- Precatório federal foi expedido após a EC nº 136/2025, fora de débito tributário específic
- Um estado apresenta estoque de precatórios em mora equivalente a 20% da receita corrente l
- Credor de oitenta salários mínimos contra a União deseja receber por RPV e concorda em des
- Sentença ilíquida desfavorável a município capital baseia-se integralmente em entendimento
- Um edital faz referência ao art. 101 da Constituição como dispositivo do regime especial d
- Uma pretensão contra autarquia sofre interrupção válida quando transcorrido apenas um ano 
- Em cumprimento judicial contra a Fazenda, a executada impugna por excesso de execução, mas
- Em tutela provisória, a parte requer liminar para compensar créditos tributários com tribu
- Uma condenação não tributária contra município resulta em requisitório expedido após agost
- Uma autarquia federal recebe intimação pessoal eletrônica em ação cível. Na ausência de pr
- Uma sentença certa e líquida de noventa salários mínimos condena município não capital, se
- Credor pretende ajuizar ação contra a Fazenda por dívida surgida há quatro anos, sem causa
- Contra o pagamento de quantia certa por autarquia estadual em título judicial, o CPC deter
- Um precatório foi apresentado em janeiro de 2026. Segundo a redação do art. 100, § 5º, dad
- No cumprimento de sentença contra a Fazenda, o art. 535 do CPC fixa trinta dias para impug
- Em pedido de suspensão fundado em grave lesão à saúde pública, a pessoa jurídica de direit
- Uma condenação contra estado alcança trezentos salários mínimos. Os honorários previstos n
- Uma sentença ilíquida foi proferida contra município, mas está fundada integralmente em en
- A parte interpôs agravo de instrumento contra liminar e simultaneamente a pessoa pública p
- Uma pessoa mantém cobrança mensal contra ente público sem ato que tenha negado o fundo do 
- Em execução de título extrajudicial contra município, a citação da Fazenda para embargar s
- Credor de RPV válida contra município recebe a requisição após encerrada a fase de defesa.
- Após a EC 136/2025, a Constituição impôs a estados, Distrito Federal e municípios teto anu
- Um requisitório federal não tributário foi expedido após a alteração constitucional de 202
- Na prescrição quinquenal, interrupção válida após um ano do prazo faz o credor ter apenas 
- Em execução de sentença contra a Fazenda, a impugnação alegou excesso sem indicar o valor 
- Uma sentença líquida de cem salários mínimos contra município que não é capital dispensa r
- Um crédito alimentício de titular de setenta anos ultrapassa o triplo da RPV. O art. 100, 
- Uma suspensão de liminar fundada em grave lesão à economia pública somente poderá ser exam
```

## Pedido 44: Contrato individual de trabalho (`direito-trabalho.contrato-individual-trabalho`, 197 tópicos de edital)

```
Matéria: direito-trabalho.contrato-individual-trabalho (Contrato individual de trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação do vínculo, modalidades, intermitência e contratos afins": 43
- "2. Poderes do empregador, alteração, transferência, suspensão e interrupção": 23
- "3. Condições especiais, saúde, acidentes e dano extrapatrimonial": 11
- "4. Irrenunciabilidade, invalidade, negociação coletiva e efeitos contratuais": 12
- "5. Extinção, justa causa, aviso-prévio e despedidas coletivas": 11

Já existem (não repita):
- Uma pessoa presta serviços pessoais e remunerados, de modo não eventual e sob direção cont
- Uma pessoa física presta trabalho não eventual, recebe contraprestação e executa pessoalme
- Uma empresa deseja celebrar contrato de experiência inicialmente por 45 dias e prorrogá-lo
- Um trabalhador foi contratado por escrito em regime intermitente e recebe convocações para
- Convocado regularmente em trabalho intermitente, um empregado não respondeu ao chamado den
- Um empregador apresenta alteração contratual com consentimento escrito do empregado, mas a
- Por real necessidade do serviço, um empregado é transferido provisoriamente para localidad
- Um trabalhador deixa de comparecer por três dias consecutivos em razão de casamento, nas c
- Uma serventia extrajudicial contrata escrevente para prestar serviços como empregado. Qual
- Após repetidos descuidos relevantes e comprovados na execução das tarefas, o empregador av
- Uma contratação por prazo determinado enquadrada no art. 443, § 2º, da CLT foi prorrogada 
- Um empregado intermitente recebeu convocação para prestar serviços com apenas dois dias co
- Um serviço transitório justifica a contratação por prazo determinado, sem regime especial 
- Um empregado recebeu suspensão disciplinar de 35 dias consecutivos. Qual consequência é pr
- Uma estudante realiza estágio com matrícula regular, termo de compromisso tripartite, ativ
- Uma empresa contrata prestadora de serviços para atividade ligada ao seu objeto principal,
- Instrumento coletivo estabelece regras de jornada dentro dos limites constitucionais, enqu
- Ao arbitrar indenização por ofensa moral trabalhista, o julgador identifica gravidade que 
- Um contrato de prazo determinado válido foi rompido antecipadamente pelo empregador, sem j
- Um vínculo por prazo indeterminado termina por acordo válido entre empregado e empregador.
- Uma empresa prepara dispensa coletiva de empregados e oferece ao sindicato profissional op
- Uma convenção coletiva limita direito trabalhista não absolutamente indisponível, sem espe
- Um empregado com cinco anos completos de serviços na mesma empresa é dispensado sem justa 
- Empregado cuja remuneração supera duas vezes o teto dos benefícios do RGPS pretende inclui
- O empregador descumpre gravemente obrigações essenciais e o empregado busca rescisão indir
- Uma pessoa começa a trabalhar por ajuste verbal, com pessoalidade, subordinação, não event
- A exclusividade é requisito legal indispensável à relação de emprego, de modo que a presta
- Na relação intermitente, a recusa de uma convocação é compatível com a manutenção da subor
- Denominar o ajuste como 'autônomo' impede reconhecimento de emprego, ainda que a prestação
- A divulgação não autorizada de segredo da empresa pode enquadrar-se na hipótese de justa c
- A simples alteração de local de trabalho dentro da mesma cidade, sem exigir mudança necess
- A reversão de empregado de função de confiança para cargo efetivo anterior é admitida pelo
- O empregador pode suspender unilateralmente o contrato por quatro meses para qualificação,
- Atividade efetivamente enquadrada nas hipóteses regulamentadas de periculosidade pode dar 
- Toda atividade insalubre gera obrigatoriamente adicional de 40%, pois a CLT adota percentu
- Um acordo coletivo pode disciplinar jornada dentro de limites constitucionais, conforme ar
- A cláusula compromissória de arbitragem individual do art. 507-A da CLT pode ser imposta u
- Em contrato por prazo determinado rompido sem justa causa antecipadamente pelo empregador,
- A culpa recíproca do art. 484 da CLT e o acordo de extinção do art. 484-A constituem a mes
- Para empregado dispensado sem justa causa com cinco anos completos de serviço na mesma emp
- Na extinção do contrato de emprego, o pagamento das parcelas rescisórias pode ser efetuado
- Os parâmetros numéricos para dano extrapatrimonial do art. 223-G, § 1º, da CLT orientam a 
- Conforme o art. 22 da Lei nº 8.213/1991, a comunicação de acidente do trabalho pode aguard
- Segundo a Súmula 212 do TST, quando negados a prestação de serviços e o despedimento na hi
- O Tema 638 do STF condiciona toda dispensa em massa à autorização prévia do sindicato; sem
```

## Pedido 45: Macroeconomia keynesiana, IS-LM e oferta e demanda agregadas (`economia.macroeconomia-is-lm-demanda-agregada`, 196 tópicos de edital)

```
Matéria: economia.macroeconomia-is-lm-demanda-agregada (Macroeconomia keynesiana, IS-LM e oferta e demanda agregadas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Demanda efetiva, produto de equilíbrio e multiplicador keynesiano": 20
- "2. Curva IS, investimento, poupança e política fiscal": 19
- "3. Curva LM, equilíbrio IS-LM e eficácia das políticas": 19
- "4. Demanda agregada, oferta agregada e produto potencial": 25
- "5. Curva de Phillips, expectativas e síntese de políticas macroeconômicas": 17

Já existem (não repita):
- Na economia fechada, o consumo é C = 40 + 0,75(Y − T), com T = 20, I = 40 e G = 30. Preços
- Uma economia fechada apresenta propensão marginal a consumir de 0,8, imposto fixo e invest
- Em uma cruz keynesiana com consumo marginal de 0,75, o imposto fixo aumenta 20 e as compra
- Consumo marginal de 0,8 e demais parâmetros fixos caracterizam uma curva IS. O governo amp
- Uma economia fechada sem variação não planejada dos estoques tem C = 20 + 0,6(Y−T), T = 20
- Em um exercício, a IS está escrita como r = 8 − 0,01Y, com juros medidos nas unidades do m
- O mercado monetário tem oferta real M/P = 160 e demanda L = 0,4Y − 80r. Se a renda é 800, 
- Num modelo IS-LM com oferta nominal de moeda fixa e demanda real por moeda crescente na re
- Uma elevação da taxa de juros reduz investimento planejado, sem mudar gasto público, tribu
- Dadas IS: Y = 1000 − 100r e LM: r = 0,01Y − 3, qual combinação satisfaz simultaneamente os
- Na cruz keynesiana, uma economia possui c = 0,5 e tributos fixos. A tributação aumenta 40,
- Uma economia fechada apresenta Y = 500, consumo C = 300 e compras públicas G = 100. Qual i
- Um choque eleva o custo de energia de produtores, sem alteração inicial na DA. No modelo D
- Em um modelo DA-OA de curto prazo com oferta agregada ascendente, uma expansão da demanda 
- Na equação de Phillips, inflação esperada vale 4%, desemprego observado 4%, desemprego nat
- Numa armadilha de liquidez clássica, a LM pode ser aproximada como horizontal. O governo a
- Uma economia aberta importa parte significativa de cada aumento da renda. Comparada à econ
- Em IS-LM tradicional, o nível de preços cresce e a oferta nominal de moeda não muda; renda
- Uma economia começa no cruzamento de IS descendente e LM crescente. Os tributos fixos aume
- Uma economia tem IS: Y = 1200 − 200r e LM: r = 0,005Y − 1. G cresce 20 e, com c = 0,8, o i
- Considere IS: Y = 1200 − 200r e demanda real de moeda L = 0,5Y − 100r. A oferta real de mo
- Na Phillips aumentada por expectativas, a inflação esperada sobe dois pontos percentuais, 
- Uma economia fechada tem c = 0,75, preços e investimento dados e impostos fixos. O governo
- Uma economia tem IS perfeitamente vertical porque investimento é insensível aos juros; a L
- Uma inovação permanente eleva a produtividade e a quantidade de bens que a economia conseg
- Uma fábrica vende menos do que produziu e acumula estoques não planejados. Na cruz keynesi
- Com consumo marginal de 0,8 e impostos fixos, aumentar compras governamentais em 10 e trib
- Num modelo em que apenas a taxa de juros sobe, sem alteração das funções de consumo, inves
- Quando a oferta nominal de moeda e o nível geral de preços dobram simultaneamente, com dem
- Uma economia fechada com propensão marginal a consumir de 0,2, tributo fixo e investimento
- Uma queda isolada do nível geral de preços, com oferta nominal de moeda fixa, deve ser rep
- Com função de demanda por moeda crescente na renda e decrescente nos juros, uma renda maio
- Se famílias pretendem elevar a poupança enquanto investimento planejado e compras públicas
- Na curva de Phillips de curto prazo, aumento de inflação esperada, com desemprego, taxa na
- Um aumento súbito dos custos de energia, sem choque inicial de demanda, desloca a oferta a
- Considere uma IS descendente derivada de investimento I = I0 − br. Aumentar o parâmetro b,
- Na armadilha de liquidez clássica, com LM horizontal, um aumento de G desloca integralment
- Em IS-LM convencional com IS descendente e LM crescente, expandir a oferta real de moeda m
- O índice geral de preços aumenta 3% neste ano, após crescer 8% no anterior. É correto afir
- Em economia fechada com consumo marginal igual a 0,6, impostos fixos e juros dados, a elev
- Considere uma IS inicialmente Y = 1200 − 200r e LM crescente r = 0,005Y − 1. Se G aumenta 
- Uma melhoria tecnológica permanente amplia o produto potencial e reduz os custos unitários
- Na Phillips com expectativas ajustáveis, uma política que mantenha permanentemente o desem
- No IS-LM convencional com oferta nominal de moeda fixa, uma redução isolada de P desloca a
- Em uma curva de Phillips aumentada por expectativas, um choque adverso de custos represent
```

## Pedido 46: Imunização: Programa Nacional de Imunizações, calendário vacinal e rede de frio (`saude-publica.imunizacao-pni`, 196 tópicos de edital)

```
Matéria: saude-publica.imunizacao-pni (Imunização: Programa Nacional de Imunizações, calendário vacinal e rede de frio)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos imunológicos e organização do PNI": 20
- "Calendário nacional: crianças, adolescentes e atualização de esquemas": 20
- "Adultos, idosos, gestantes e imunização ocupacional": 20
- "Rede de frio, preparo e administração segura": 20
- "Contraindicações, ESAVI e imunobiológicos especiais": 20

Já existem (não repita):
- A imunização passiva artificial fornece anticorpos prontos e, por si só, não estabelece me
- Toda vacina é uma forma de imunização passiva, porque entrega anticorpos já produzidos ao 
- O PNI foi formulado em 1973 para coordenar ações de vacinação que antes eram mais fragment
- A vacina BCG protege sobretudo contra formas graves e disseminadas da tuberculose na infân
- O imunizante contra rotavírus humano, no calendário infantil, é aplicado pela via intramus
- A vacina tríplice viral protege contra sarampo, caxumba e rubéola.
- A meningocócica ACWY protege contra todos os sorogrupos possíveis de Neisseria meningitidi
- Uma excursão de temperatura deve ser documentada mesmo quando os frascos preservam aparênc
- No calendário de 2026, a dTpa está indicada em cada gestação a partir da 20ª semana.
- Segundo o calendário de 2026, a vacina contra VSR na gestante deve ser aplicada exclusivam
- Na rede de frio, o uso da faixa de +2 °C a +8 °C elimina a necessidade de verificar exigên
- O ESAVI exige comprovação de nexo causal entre o imunobiológico e o quadro clínico antes d
- É possível que um erro de imunização não produza sintoma clínico perceptível e, mesmo assi
- Toda infecção respiratória leve, sem comprometimento do estado geral, é contraindicação ab
- Trabalhadores de saúde devem ter seu histórico vacinal avaliado com atenção ao risco ocupa
- Se uma série de hepatite B estiver incompleta há anos, a regra é descartar doses documenta
- A aplicação de uma vacina não viva é sempre contraindicada durante a gestação, independent
- Na avaliação de ESAVI, intensidade dos sintomas e classificação de gravidade para vigilânc
- Um paciente com asplenia pode precisar de avaliação individual nos CRIE para vacinas e esq
- Na profilaxia de acidente biológico ocupacional, a existência de uma vacina contra hepatit
- Qual opção expressa corretamente a diferença entre eficácia e efetividade vacinal?
- Uma pessoa recebe imunoglobulina após exposição relevante. Qual é o mecanismo esperado?
- Entre as vacinas listadas, qual se destaca como vacina viva atenuada?
- Em uma sala, chega recém-nascido sem registro de vacinação. Qual dupla constitui prioridad
- Qual associação entre vacina e doença-alvo está correta?
- Na preparação para administrar BCG a criança elegível, qual é a via correta?
- Como deve ser interpretada a sigla MenACWY?
- Uma criança apresenta atraso de doses documentadas. Qual atitude respeita o princípio de r
- A equipe recebe questionamento sobre HPV na rotina de 2026. Qual informação é compatível c
- Sobre a vacinação de rotina contra dengue no calendário público de 2026, assinale a descri
- No calendário infantil de 2026, qual descrição corresponde ao uso de poliomielite na rotin
- Em relação à tríplice viral de trabalhadores da saúde, qual conduta é mais adequada?
- A gestante solicita proteção do bebê contra coqueluche. Qual indicação se ajusta ao calend
- A vacina materna contra vírus sincicial respiratório, no calendário de 2026, é oferecida c
- Um serviço revisa vacina contra febre amarela para idoso não vacinado. Qual postura é apro
- Uma unidade deseja descrever sua rede de frio. Qual alternativa expressa a relação correta
- A equipe detecta temperatura inadequada na câmara científica após falha de energia. O que 
- Sobre a faixa +2 °C a +8 °C em sala de vacinação, qual interpretação é tecnicamente pruden
- Qual medida reduz perda técnica sem comprometer a segurança da vacinação?
- Um profissional registra dor e tontura logo após a vacinação, sem diagnóstico definido. Qu
- Uma pessoa é internada após imunização e ainda se desconhece o nexo causal. Qual ação segu
- Um paciente com imunossupressão intensa precisa atualizar vacinas. Qual alternativa é mais
- Um serviço confirma anafilaxia imediatamente após dose anterior de vacina específica. Qual
- Qual situação melhor ilustra indicação de avaliação em CRIE?
- Após acidente com perfurocortante, um trabalhador sem comprovação de vacinação contra hepa
```

## Pedido 47: Probabilidade avançada e distribuições discretas (`estatistica.probabilidade-avancada`, 196 tópicos de edital)

```
Matéria: estatistica.probabilidade-avancada (Probabilidade avançada e distribuições discretas)
Capítulos e quantas questões quero em cada um (total 100):
- "Como o tema costuma ser cobrado": 11
- "Condicional, independência e probabilidade total": 45
- "Teorema de Bayes e testes diagnósticos": 21
- "Variáveis discretas e distribuições": 12
- "Cálculo, interpretação e escolha do modelo": 11

Já existem (não repita):
- P(A∩B)=0,18 e P(B)=0,30. Qual é P(A|B)?
- A e B são independentes, com P(A)=0,40 e P(B)=0,25. Qual a probabilidade de ambos ocorrere
- P(A)=0,45, P(B)=0,35 e P(A∩B)=0,15. Qual é P(A∪B)?
- Uma condição tem prevalência de 2%; o teste tem sensibilidade de 90% e falso positivo de 5
- X assume 0, 1 e 2 com probabilidades 0,20, 0,50 e 0,30. Qual é E(X)?
- Uma variável Bernoulli tem probabilidade de sucesso p=0,40. Qual sua variância?
- Em três ensaios binomiais independentes, p=0,50. Qual a probabilidade de exatamente dois s
- Quatro ensaios independentes têm probabilidade de sucesso 0,10 cada. Qual a chance de pelo
- X segue Poisson com λ=2 por intervalo. Qual a probabilidade aproximada de zero ocorrências
- Em grupo de 10 itens, 3 são defeituosos. Retiram-se 2 sem reposição. Qual a probabilidade 
- Uma urna tem 5 bolas vermelhas e 3 azuis. Sem reposição, a primeira bola foi vermelha. Qua
- A prevalência é 5%, sensibilidade 80% e falso positivo 10%. Aproximadamente, qual a chance
- X é binomial com n=8 e p=0,30. Qual sua esperança?
- Uma binomial tem n=10 e p=0,20. Qual a variância?
- X segue Poisson com λ=3. Qual a probabilidade aproximada de no máximo uma ocorrência?
- A chance de sucesso em cada tentativa independente é 0,25. Qual a chance de o primeiro suc
- Há 12 itens, dos quais 4 defeituosos. Retiram-se 3 sem reposição. Qual a probabilidade de 
- X assume 0, 1 e 2 com probabilidades 0,20, 0,50 e 0,30. Qual a variância?
- X é binomial com n=4 e p=0,50. Qual a probabilidade de pelo menos dois sucessos?
- Prevalência é 1%, sensibilidade 95% e falso positivo 4%. Qual a chance aproximada da condi
- Qual condição caracteriza independência entre A e B?
- A e B são mutuamente exclusivos e ambos têm probabilidade positiva. Qual conclusão decorre
- Qual propriedade vale para variável Bernoulli com chance de sucesso p?
- Quando P(B)>0, em qual situação P(A|B)=P(A)?
- Qual descrição representa o teorema de Bayes?
- Quando P(B)>0, P(A|B) é P(A∩B) dividida por P(B).
- Se A e B são independentes e P(B)>0, então P(A|B)=P(A).
- Eventos mutuamente exclusivos com probabilidades positivas também são independentes.
- A esperança de uma variável discreta precisa ser um valor possível observado no experiment
- O modelo binomial usual supõe número fixo de ensaios independentes e probabilidade constan
- Na distribuição de Poisson, média e variância são iguais ao parâmetro λ.
- Uma variável binomial pode assumir qualquer número real negativo de sucessos.
- Na amostragem sem reposição de população finita, os resultados sucessivos podem depender d
- Para P(B)>0, P(A|B) é P(A) dividida por P(B), sem considerar a interseção.
- Bayes atualiza a probabilidade de uma hipótese após evidência usando o prior e a chance de
- A variância de uma variável aleatória nunca pode ser negativa.
- Se eventos A e B são independentes, então nunca podem ocorrer juntos.
- Se as esperanças existem, E(X+Y)=E(X)+E(Y), mesmo sem independência entre X e Y.
- Para qualquer constante a, Var(aX)=aVar(X), sem elevar a ao quadrado.
- A igualdade P(A∩B)=P(A)P(B) vale para qualquer par de eventos, mesmo dependentes.
- Um teste com sensibilidade de 95% implica que 95% dos resultados positivos correspondem a 
- Se p=0,20 em uma Bernoulli, sua variância é zero porque há apenas dois resultados possívei
- A fórmula P(A∪B)=P(A)+P(B) vale sem ajuste para todos os eventos, inclusive quando se sobr
- Em ensaios binomiais, a probabilidade de pelo menos um sucesso pode ser obtida por 1 menos
- A esperança de uma binomial com n ensaios e probabilidade p é np.
```

## Pedido 48: Proteção do navio e das instalações portuárias (Código ISPS) (`marinha-mercante.protecao-navio-isps`, 196 tópicos de edital)

```
Matéria: marinha-mercante.protecao-navio-isps (Proteção do navio e das instalações portuárias (Código ISPS))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ameaças e política de proteção marítima": 18
- "2. Definições, planos e documentos": 22
- "3. Responsabilidades sobre proteção": 35
- "4. Equipamentos e sistemas de proteção": 25

Já existem (não repita):
- No contexto marítimo, o termo proteção (security) refere-se à prevenção de atos ilícitos i
- O Código ISPS foi adotado pela IMO em 2002, como resposta aos atentados de 11 de setembro 
- O Código ISPS tem uma Parte A, de requisitos obrigatórios, e uma Parte B, com orientações 
- O Código ISPS aplica-se, em viagens internacionais, a navios de carga com arqueação bruta 
- No Código ISPS, o nível de proteção 3 corresponde a uma situação:
- O nível de proteção a ser adotado por uma instalação portuária é estabelecido pelo governo
- No Brasil, a implementação do Código ISPS nas instalações portuárias é coordenada:
- NÃO constitui ameaça típica de proteção (security) ao transporte marítimo:
- A interface navio-porto, durante operações de carga, abastecimento e embarque de pessoas, 
- A avaliação de proteção do navio, que identifica ameaças, vulnerabilidades e medidas exist
- O plano de proteção do navio (SSP) é documento sigiloso, deve ser aprovado pela Administra
- A Declaração de Proteção (DoS) é:
- O Certificado Internacional de Proteção do Navio (ISSC) tem validade de até:
- Uma organização de proteção reconhecida (RSO) pode ser autorizada pelo governo contratante
- Entre as medidas previstas no plano de proteção do navio está o estabelecimento de áreas d
- Quanto ao manuseio de informações relacionadas à proteção, é correto afirmar que:
- O registro de treinamentos e exercícios de proteção, de incidentes e de mudanças de nível 
- O oficial designado pela Companhia, subordinado ao comandante, responsável por executar e 
- O Oficial de Proteção da Companhia (CSO) é responsável por garantir a realização da avalia
- Pelo Código ISPS, a autoridade e a responsabilidade finais sobre a segurança e a proteção 
- O Oficial de Proteção da Instalação Portuária (PFSO) é responsável por desenvolver, implem
- Entre as atribuições dos governos contratantes no Código ISPS, está:
- Todos os tripulantes, e não apenas os que têm tarefas de proteção, devem receber formação 
- Um tripulante observa uma pessoa desconhecida circulando próximo à praça de máquinas, sem 
- A coordenação da proteção de toda a frota de uma empresa, com a ligação entre os navios e 
- O Sistema de Alerta de Proteção do Navio (SSAS), quando acionado:
- O SSAS deve poder ser ativado de pelo menos dois pontos do navio, sendo um deles no passad
- O alerta do SSAS é transmitido a outros navios da área para que prestem auxílio imediato.
- A iluminação do convés, do costado e da escada de portaló, sobretudo à noite, contribui pa
- NÃO é limitação operacional típica dos equipamentos de proteção do navio:
- O teste periódico do SSAS deve ser coordenado previamente com a autoridade competente e a 
- Constitui medida de controle de acesso ao navio em porto:
- Em áreas de risco de pirataria, medidas como vigias reforçados, barreiras de concertina, m
- Os rádios VHF e UHF portáteis da equipe de proteção, os telefones internos e a comunicação
- Assinale a alternativa que relaciona corretamente a sigla e seu significado no Código ISPS
- Quem designa o Oficial de Proteção do Navio (SSO)?
- Ao chegar a um porto que opera no nível de proteção 2, um navio que está no nível 1 deve:
- O nível de proteção 2 implica medidas adicionais por um período, em razão de risco aumenta
- Além do Código ISPS, o Capítulo XI-2 da SOLAS exige, entre as medidas de proteção dos navi
- O nível de proteção 3 só pode ser declarado quando o alvo específico da ameaça tiver sido 
```

## Pedido 49: População e urbanização no Brasil (`geografia.populacao-urbanizacao-brasil`, 195 tópicos de edital)

```
Matéria: geografia.populacao-urbanizacao-brasil (População e urbanização no Brasil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Distribuição populacional, povoamento e diversidade brasileira": 19
- "2. Crescimento, indicadores e transição demográfica": 18
- "3. Migrações internas, imigração e transformações regionais": 19
- "4. Urbanização, metropolização e rede urbana brasileira": 26
- "5. Dinâmica intraurbana, desigualdades e desafios das metrópoles": 18

Já existem (não repita):
- Dois municípios têm respectivamente 100 mil habitantes em 100 km² e 200 mil habitantes em 
- Um território registrou 800 nascimentos e 550 óbitos no ano, desconsideradas migrações. Qu
- Uma pessoa mora em município periférico e se desloca diariamente para trabalhar na metrópo
- Duas cidades crescem em direção uma à outra até que suas manchas edificadas se tornem cont
- Em uma pirâmide etária, a proporção de crianças diminui ao longo das décadas e os grupos i
- Uma família sai definitivamente de uma propriedade rural e passa a residir em cidade próxi
- Um município recebe influência comercial, educacional e hospitalar de uma cidade que organ
- Um bairro passa a reunir edifícios mais altos e maior aproveitamento construtivo dos lotes
- Um residente de cidade média procura emprego e serviços especializados sem deslocar-se à p
- Em localidade com 1.200 nascimentos, 800 óbitos, 500 chegadas e 700 saídas durante o ano, 
- Uma comunidade possui 200 moradores de 0 a 14 anos, 600 entre 15 e 64 e 100 com 65 anos ou
- Um estudo histórico relata crescimento populacional acelerado no Brasil durante parte do s
- Depois de viver muitos anos no Sudeste, uma pessoa nascida no Nordeste volta a residir per
- Uma região metropolitana definida institucionalmente possui municípios com intenso movimen
- Um mapa mostra São Paulo no topo da hierarquia da REGIC 2018, com o Rio de Janeiro e Brasí
- Uma antiga área residencial popular recebe novos empreendimentos, valorização expressiva e
- Uma região periférica passa a concentrar universidades, comércio especializado e empregos,
- Uma população de 120 mil cresce para 126 mil num período e para 129 mil no período seguint
- Um bairro tem proporção crescente de idosos e queda relativa de crianças, enquanto outro r
- Um pesquisador pretende descrever a formação demográfica brasileira como resultado de imig
- Uma cidade amazônica concentra elevada população em pequeno núcleo urbano, enquanto seu mu
- Um morador nasceu na Bahia e reside em Goiás há décadas. Sem outra informação, o estudo de
- Em mapa de centralidades, uma cidade com menos habitantes oferece hospitais especializados
- Um plano urbano promove crescimento de edifícios e valorização de terrenos próximos a tran
- Um município cresceu com bairros dispersos sobre áreas rurais, elevando custos de infraest
- Um estado pode ter densidade média baixa e abrigar uma metrópole de elevada concentração p
- O saldo migratório de um município é calculado somando todos os imigrantes e emigrantes, p
- Na história demográfica brasileira, a queda da mortalidade em ritmo diferente da fecundida
- O trabalhador que viaja diariamente entre município de residência e município de emprego n
- O encontro físico das áreas edificadas de dois municípios configura conurbação, mesmo quan
- A verticalização de uma área, caracterizada pelo crescimento de edifícios altos, demonstra
- A redução da taxa de fecundidade pode coexistir com aumento do total de habitantes, porque
- A razão de dependência etária corresponde à proporção de pessoas efetivamente desempregada
- No processo brasileiro de imigração histórica, italianos se dirigiram tanto a áreas cafeei
- Os grandes fluxos internos de trabalhadores para o Sudeste durante a industrialização impe
- A REGIC identifica redes urbanas segundo funções de gestão e relações de busca de bens e s
- Uma região metropolitana criada institucionalmente será necessariamente uma cidade única c
- A criação de centros secundários de emprego, comércio e serviços em diferentes pontos de u
- O retrato demográfico de 2022 mostra participação maior de idosos no Brasil do que em 2010
- O estudo da população brasileira pode usar categorias de cor ou raça autodeclaradas e comp
- O retorno de migrantes nordestinos às localidades de origem demonstra que São Paulo deixou
- A cidade considerada global por suas conexões internacionais de finanças e serviços avança
- A inclusão de vários municípios em região metropolitana implica que redes de saneamento, t
- A expansão urbana por novos loteamentos periféricos, distante dos empregos e equipamentos 
- O bônus demográfico representa aumento temporário do peso das idades potencialmente ativas
```

## Pedido 50: Git e controle de versão (`informatica.git-versionamento`, 195 tópicos de edital)

```
Matéria: informatica.git-versionamento (Git e controle de versão)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Controle de versão, gerência de configuração e Subversion": 26
- "2. Repositório Git, áreas, estados e comandos cotidianos": 17
- "3. Merge, rebase, conflitos, reset, revert e recuperação": 16
- "4. Remotos, colaboração, branches e baselines de release": 25
- "5. Versionamento semântico, builds e integração contínua introdutória": 16

Já existem (não repita):
- Uma equipe deseja registrar quem alterou o código e recuperar versões anteriores sem depen
- Uma desenvolvedora clonou um projeto Git e ficou temporariamente sem internet. Precisa reg
- No SVN, foi realizado um commit que alterou apenas o arquivo `a.txt` e gerou a revisão 52.
- No Git, a estudante modificou `relatorio.md` e quer preparar o conteúdo atual para o próxi
- Uma equipe aprova um conjunto identificado de código, testes e documentação que servirá co
- Uma desenvolvedora quer saber quais arquivos estão modificados, novos ou preparados no ind
- Uma cópia de trabalho do SVN ficou na revisão 80 enquanto colegas publicaram a revisão 81.
- Após confirmar alterações no repositório Git local, uma colaboradora precisa enviá-las à b
- Um arquivo de credenciais já foi confirmado no histórico. A equipe adiciona seu nome ao `.
- Um arquivo foi adicionado ao index e recebeu uma segunda alteração ainda não adicionada. A
- A branch `main` aponta A, e `feature` avançou a A–B–C sem commits novos em main. A equipe 
- Depois de realizar o commit C, a pessoa quer retirar esse commit da ponta da branch, mante
- Uma biblioteca pública compatível com a versão 4.2.9 ganhou funcionalidade nova que preser
- A equipe deseja criar uma referência para a release com autor, data e mensagem da anotação
- Um projeto hospeda sua implementação no SVN seguindo o padrão `trunk/branches/tags`. Foi c
- O relatório de uma release precisa demonstrar quais solicitações aprovadas, versões de doc
- Duas branches tiveram commits divergentes. Em main, a pessoa executa `git merge --squash f
- Uma correção urgente já foi publicada e utilizada por vários colaboradores, mas contém err
- Uma pessoa executa `git reset --mixed HEAD~1` após confirmar uma edição. Qual é o efeito s
- Um pacote release foi marcado por tag em commit C, mas a auditoria encontrou executável co
- A branch privada feature possui commits F1 e F2 baseados em A. Main recebeu M1 depois de A
- Uma equipe deseja garantir que `git merge feature` só avance a branch principal quando for
- Uma pipeline simples possui `build-job` e dois `test-job`, definidos em `.gitlab-ci.yml`. 
- Um colaborador deseja propor alteração a um repositório hospedado ao qual não possui permi
- Uma organização recebeu proposta de mudar a API de `3.6.8` removendo parâmetro obrigatório
- Uma equipe confirma em SVN alterações de três arquivos em um único commit. O sistema deve 
- Depois de executar `git add src/app.js`, a alteração já estará publicada no repositório re
- Executar `git clone URL` em projeto Git equivale a criar diretório sem histórico, que só p
- No repositório Git, `git log` permite consultar commits registrados e seus metadados, enqu
- Uma tag de release Git normalmente aponta para uma referência específica e não acompanha a
- O uso de Git garante, sem configuração adicional, que cada `git commit` execute testes aut
- Uma baseline aprovada pode incluir versão de código, documentos, parâmetros de build e reg
- Após executar `svn commit` e receber revisão nova, a cópia de trabalho é automaticamente a
- Depois de preparar uma edição com `git add`, a pessoa pode usar `git restore --staged cami
- O comando `git stash push`, usado sem opções, inclui automaticamente qualquer arquivo novo
- Se main aponta para A e feature para o descendente C, sem divergência na main, um merge pa
- O GitHub Flow exige como estrutura fixa a branch `develop` permanente e branches `release`
- Pela convenção SemVer, de `1.9.6` para uma funcionalidade nova compatível com a API existe
- `git fetch origin` integra imediatamente as novas alterações de `origin/main` à branch mai
- Durante um merge, duas branches alteraram de maneira incompatível um mesmo trecho. A equip
- `git reset --hard HEAD~1` e `git revert HEAD` têm resultado histórico idêntico: ambos cria
- Depois de reposicionar a branch por reset, uma pessoa pode consultar `git reflog` para ide
- Pelo SemVer 2.0.0, acrescentar `+build.5` a `2.4.1` aumenta a precedência da versão em rel
- Um pacote compilado de commit distinto daquele registrado na tag da baseline aprovada repr
- `git merge --squash feature` cria automaticamente commit de merge com dois pais, preservan
```

