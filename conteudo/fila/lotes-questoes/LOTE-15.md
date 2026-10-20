# Pedidos de questões extras, lote 15 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 5603 tópicos de edital.

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

## Pedido 1: Intervalos, descanso semanal e trabalho noturno (`direito-trabalho.intervalos-descanso-noturno`, 122 tópicos de edital)

```
Matéria: direito-trabalho.intervalos-descanso-noturno (Intervalos, descanso semanal e trabalho noturno)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Intervalo intrajornada: duração, flexibilização e indenização": 28
- "2. Descanso entre jornadas e repouso semanal": 24
- "3. Remuneração do DSR: horistas, comissionistas e parcelas habituais": 18
- "4. Trabalho noturno urbano e rural: horários, hora reduzida e adicionais": 19
- "5. Casos integrados, marcos temporais e identificação das pegadinhas": 11

Já existem (não repita):
- Uma empregada urbana cumpre exatamente seis horas diárias de trabalho contínuo, sem regras
- Uma pessoa trabalha sete horas efetivas por dia, sem redução por norma coletiva válida ou 
- Em 2026, empregado com intervalo mínimo de sessenta minutos recebeu quarenta, sem redução 
- Convenção coletiva regula o intervalo alimentar em jornadas superiores a seis horas. Qual 
- O expediente terminou às 21h de segunda-feira. Qual o primeiro horário regular de retorno 
- Uma empresa elabora escala para garantir o descanso semanal remunerado na regra comum. Qua
- Empregado trabalha domingo em atividade autorizada, com folga compensatória semanal corret
- Um trabalhador rural atua na lavoura durante a noite. Qual é a faixa de horário legalmente
- Após novembro de 2017, empregado com salário-hora de R$ 24 recebeu trinta dos sessenta min
- Empregada usufruiu parcialmente intervalo alimentar de uma hora em 2016. Ao julgar a situa
- O expediente terminou às 23h de terça e recomeçou às 8h de quarta. Qual déficit houve no d
- Uma escala concedeu descanso semanal somente após o empregado completar sete dias consecut
- Uma vendedora recebe apenas comissões pelas vendas realizadas e sustenta direito à remuner
- Uma horista recebe R$ 20 por hora e trabalha jornada normal de seis horas, sem extras habi
- Um empregado mensalista já tem os repousos ordinários incluídos na remuneração mensal e pr
- Trabalhador urbano presta 210 minutos efetivos no período noturno, desconsiderados interva
- Uma pessoa empregada na pecuária trabalha das 19h às 23h, sem pausa no trecho apresentado.
- Após jornada que termina sábado às 20h, uma escala pretende compatibilizar a interjornada 
- Empregado por peça produziu R$ 900 no horário normal em cinco dias efetivos de uma semana,
- Uma trabalhadora em domicílio produziu artigos que lhe renderam R$ 1.500 na semana, sem pe
- Empregado urbano trabalha efetivamente das 22h às 5h, em exemplo matemático sem intervalo 
- Empregado urbano cumpriu integralmente jornada no período noturno e continuou trabalhando 
- Uma unidade de atividade pecuária e outra de lavoura remuneram trabalho noturno pela regra
- Empregado faltou injustificadamente ao trabalho no período de apuração e perdeu o requisit
- Empregado trabalhou em feriado por necessidade do serviço, sem compensação de folga e sem 
- Uma empregada cumpre diariamente cinco horas de trabalho contínuo e recebe quinze minutos 
- Uma jornada de exatamente seis horas exige uma hora de intervalo alimentar pelo art. 71, c
- Após a Reforma de 2017, a concessão parcial do intervalo intrajornada mínimo gera pagament
- O valor pago pela supressão parcial de intervalo intrajornada ocorrido em 2026 tem obrigat
- Um empregado que encerra jornada às 20h e volta às 7h do dia seguinte recebe onze horas co
- O art. 67 da CLT assegura descanso semanal de onze horas e exige que ele seja concedido ob
- Para trabalhador rural da lavoura, o adicional noturno mínimo é de vinte e cinco por cento
- O art. 611-A, III, permite ao empregador e a cada empregado reduzir individualmente o inte
- A remuneração do descanso semanal inclui horas extraordinárias habitualmente prestadas, co
- O empregado mensalista com DSR básico já incluído no salário não pode ter qualquer reflexo
- Uma empregada que recebe somente comissões tem direito à remuneração dos repousos semanais
- Na pecuária rural, cada hora noturna é contada como 52 minutos e 30 segundos, tal qual a h
- O trabalho urbano das 22h às 5h compreende 420 minutos reais que correspondem, para contag
- A concessão do repouso semanal somente após o sétimo dia consecutivo de trabalho é regular
- Se um empregado trabalha em feriado legal sem folga compensatória aplicável, a legislação 
- A falta injustificada que prejudica a remuneração do DSR pelo art. 6º da Lei nº 605/1949 a
- Ao organizar folga semanal em sequência com interjornada, a referência acumulada de trinta
- A Súmula nº 60, II, retira o adicional noturno das horas trabalhadas após as 5h mesmo quan
- A supressão de trinta minutos de intervalo alimentar em 2026, com salário-hora normal de R
- O horista que recebe R$ 18 por hora e trabalha normalmente seis horas por dia, sem extras 
```

## Pedido 2: Edição de imagem, vídeo, 3D e design gráfico (`informatica.edicao-imagem-video-design-grafico`, 121 tópicos de edital)

```
Matéria: informatica.edicao-imagem-video-design-grafico (Edição de imagem, vídeo, 3D e design gráfico)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Representação vetorial, desenhos e organização de objetos": 20
- "2. Edição raster, composição, máscaras e ajustes de imagem": 21
- "3. Edição de vídeo, áudio, linha do tempo e formatos": 17
- "4. Modelagem tridimensional, materiais, iluminação e animação": 19
- "5. Design gráfico, cor, diagramação e pré-impressão": 23

Já existem (não repita):
- Uma equipe produziu três formas e uma legenda para um símbolo. Deseja mover esses elemento
- Ao redesenhar a curva de um emblema, uma pessoa move as alças de um nó em um caminho Bézie
- Um arquivo SVG contém um ícone de linhas e uma fotografia inserida como imagem. Ao aumenta
- Uma imagem de interface contém textos pequenos, bordas nítidas e fundo transparente. A pri
- Em um retrato, deseja-se esconder provisoriamente o fundo sem apagar os pixels da camada o
- Uma fotografia foi exportada várias vezes para o formato JPEG com compressão. Que risco es
- Uma imagem possui largura de 1.800 pixels e será impressa sem reamostragem a 300 pixels po
- Uma montagem usa uma trilha para a entrevista, outra para música e uma faixa superior para
- Um vídeo foi entregue com extensão .mp4, mas o receptor precisa saber como decodificar o f
- Um cartaz será visto em monitor e depois enviado para impressão com tintas de processo. Qu
- Uma linha aberta precisa aparecer azul, com espessura de 4 unidades, mas sem uma área inte
- Uma fotografia apresenta sombras um pouco fechadas e meios-tons excessivamente claros. Bus
- Uma vinheta possui 12 segundos, mantendo taxa constante de 24 quadros por segundo. Quantos
- Uma exportação de vídeo usa bitrate variável, enquanto outra usa bitrate aproximadamente c
- Uma artista possui uma malha 3D de embalagem e deseja que o desenho bidimensional do rótul
- Em um modelo 3D, uma superfície aparece escura de modo inesperado mesmo com luzes adequada
- Uma página terá fotografias até a borda após o corte. O serviço de impressão solicita 3 mm
- Um projeto fotográfico usa cinco camadas, duas máscaras e texto editável. É necessário env
- Duas cenas têm igual resolução e FPS: uma é estática e outra possui movimento intenso. Sob
- Em um modelo de parede de tijolos, o relevo aparente muda conforme a luz, mas a silhueta v
- Uma identidade visual será compartilhada com pessoas que não possuem a fonte original. Des
- Uma foto de 1500 pixels de largura é colocada inicialmente em 5 polegadas e depois ampliad
- Um efeito luminosa deve surgir sobre uma imagem sem apagar os pixels do fundo. A camada su
- Uma peça impressa possui transparências, texto, fotos e cores sujeitas a gerenciamento. Ap
- Uma animação 3D possui dois keyframes da posição de um objeto, mas seu deslocamento deve c
- Um ícone criado com caminhos vetoriais é ampliado para uma placa de maior tamanho. Mantend
- Uma montagem de imagens contém máscaras e textos editáveis. Ao exportá-la para JPEG comum,
- Ao pintar uma máscara de camada com preto, uma pessoa revela integralmente os pixels da re
- Na edição não linear de uma entrevista, é possível reorganizar clipes na linha do tempo se
- Para adaptar uma arte destinada a monitor e a impressão, deve-se lembrar que o RGB descrev
- Ao receber um arquivo MP4, a pessoa já sabe qual algoritmo de compressão de vídeo foi empr
- Um logotipo em SVG inclui caminhos de texto convertidos em curvas e uma pequena textura PN
- Uma fotografia originalmente com 1.200 × 800 pixels recebe alteração de metadados de 72 pa
- Em uma imagem, a máscara de camada pode ocultar um cenário e permitir sua recuperação post
- Na exportação de um vídeo, selecionar bitrate variável obriga o uso de compressão sem perd
- Uma parede 3D usa normal map para simular rugosidade de tijolos. Por definição, esse mapa 
- Ao planificar as faces de uma embalagem virtual para aplicar uma textura, utiliza-se um ma
- Na preparação de uma página para refilo, os 3 mm de sangria devem ser reservados dentro da
- Em uma sequência, a fala da próxima cena começa enquanto a imagem da cena atual ainda está
- Em uma composição RGBA convencional, o valor mínimo do canal alfa representa opacidade tot
- Um folder terá tamanho final de 210 × 297 mm e sangria de 3 mm em cada um dos quatro lados
- Ao exportar uma peça com o ajuste PDF/X-4, torna-se desnecessário revisar fontes ausentes,
- Um título foi convertido em curvas antes de enviar o símbolo vetorial a outro computador. 
- Aumentar a subdivisão de uma malha tridimensional é suficiente para aumentar automaticamen
- Uma máscara de camada mantém gradações de cinza em torno dos fios de cabelo. Isso permite 
```

## Pedido 3: Mecânica automotiva: motores e sistemas (`mecanica.mecanica-automotiva-sistemas`, 121 tópicos de edital)

```
Matéria: mecanica.mecanica-automotiva-sistemas (Mecânica automotiva: motores e sistemas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Motores de combustão interna e ciclos": 20
- "2. Alimentação, admissão, ignição e lubrificação": 17
- "3. Arrefecimento e sistema elétrico": 23
- "4. Transmissão e sistemas de freios": 24
- "5. Suspensão, direção e diagnóstico integrado": 16

Já existem (não repita):
- Em uma oficina, um estudante acompanha o movimento de um pistão e quer identificar a funçã
- Uma animação mostra um cilindro de motor de quatro tempos passando da entrada de carga à s
- Um mecânico marca o virabrequim para acompanhar um ciclo completo de um cilindro em motor 
- Em um motor de ignição por centelha alimentado por gasolina, o combustível já está mistura
- Em teste com motor Diesel convencional, observa-se a injeção de combustível perto do fim d
- Um veículo apresenta suspeita de falta de combustível nos cilindros. No sistema eletrônico
- O líquido de arrefecimento retorna quente do motor e precisa ceder calor ao ambiente. Qual
- O veículo esteve parado por horas e exige corrente para dar partida. Depois que o motor fu
- Ao mudar de marcha em veículo manual, o motorista precisa interromper momentaneamente o ac
- Em uma curva, a roda motriz externa percorre trajeto maior que a interna. Qual peça da tra
- Ao aplicar força no pedal, um freio hidráulico transfere pressão do cilindro-mestre aos pi
- Durante uma inspeção, foram observadas sapatas revestidas pressionadas radialmente contra 
- Um veículo equipado com ABS entra em frenagem intensa em piso molhado, e a central identif
- Após passar sobre uma lombada, a carroceria de um veículo oscila repetidas vezes. Consider
- Dois veículos apresentam direção assistida, sendo um hidráulico e outro elétrico. Qual des
- Em um motor de injeção eletrônica, um sensor localizado no escapamento informa ao controle
- Um motor demora a alcançar a temperatura normal, mesmo após circulação prolongada em tempo
- A ficha de um motor Otto quatro-tempos mostra abertura antecipada da admissão antes do pis
- Em um cilindro, o volume disponível com o pistão no PMI é de 500 mL; no PMS restam 50 mL. 
- Ao comparar motores alternativos de dois e quatro tempos sob o critério da duração do cicl
- Um Diesel de injeção eletrônica common rail apresenta velas aquecedoras e combustível inje
- Uma resistência ideal de 6 ohms é ligada a uma fonte de 12 volts em um circuito de teste a
- Em uma transmissão ideal, uma redução de 4:1 faz o eixo de saída girar a um quarto da rota
- Em um freio hidráulico ideal, a pressão no circuito é de 2 MPa e a área útil de um pistão 
- Um veículo apresenta três ocorrências: temperatura cresce apenas em trânsito lento, vibraç
- Um técnico observa quatro deslocamentos completos de um pistão em motor de quatro tempos. 
- Ao analisar motor Otto convencional em funcionamento, um estudante conclui que a mistura d
- Em um motor alternativo de pistões, a biela transmite esforços entre pistão e virabrequim,
- Durante a revisão de um sistema de alimentação por injeção, afirma-se que o filtro de comb
- Num sistema de arrefecimento líquido, o radiador promove troca térmica com o ar externo, e
- Se o alternador de um veículo apresenta defeito durante uma viagem, o motor de partida ass
- Uma roda dispõe de freio a tambor com sapatas e lonas que pressionam a parte interna do ta
- No motor Diesel convencional, a bomba de alta pressão é responsável por gerar a centelha q
- Comparados apenas quanto ao número de cursos por ciclo, um motor alternativo de dois tempo
- Um veículo demora a alcançar a temperatura normal com a válvula termostática travada abert
- Em um motor com controle eletrônico de mistura, o sinal da sonda lambda fornece informação
- Na tecnologia common rail Diesel, o acumulador de alta pressão substitui a compressão do a
- Em câmbio manual sincronizado, o sincronizador facilita o engate ao compatibilizar velocid
- Ao examinar um motor de quatro tempos com injeção direta de gasolina, um aluno conclui que
- Em um sistema hidráulico ideal de freio, dois pistões recebem a mesma pressão de fluido. S
- Um veículo com ABS funcional que freia sobre cascalho possui garantia de distância de para
- Após uma partida a frio, um motor com termostática travada aberta pode levar mais tempo pa
- Ao comparar direção elétrica e hidráulica, conclui-se que a assistência elétrica não possu
- Um automóvel vibra em faixa de velocidade, mas não apresenta desvio direcional. O desbalan
- Num diagnóstico conjunto, se o motor aumenta de rotação sem avanço proporcional do veículo
```

## Pedido 4: Prevenção à lavagem de dinheiro (`conhecimentos-bancarios.prevencao-lavagem-dinheiro`, 121 tópicos de edital)

```
Matéria: conhecimentos-bancarios.prevencao-lavagem-dinheiro (Prevenção à lavagem de dinheiro)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lavagem de dinheiro, etapas e financiamento do terrorismo": 15
- "2. Pessoas obrigadas, reguladores, Coaf e responsabilidades": 15
- "3. Conheça seu cliente, beneficiário final, PEP e perfil de risco": 23
- "4. Registros, alertas, seleção, análise e comunicações": 23
- "5. Sigilo bancário, RIFs, fiscalização e consequências": 24

Já existem (não repita):
- Uma sociedade recebe em espécie recursos de origem ilícita e procura inseri-los em movimen
- Uma organização utiliza doações obtidas licitamente para sustentar grupo envolvido em ativ
- Uma instituição financeira envia comunicação de operação suspeita e o destinatário realiza
- Na abertura de conta de empresa, a instituição obtém seus dados e, depois, pesquisa ativid
- Uma empresa mantém conta bancária por meio de representante, mas pertence a outra sociedad
- Cliente classificado como pessoa exposta politicamente apresenta documentos lícitos relati
- Uma área bancária decide comunicar indícios ao Coaf; posteriormente, o órgão cruza informa
- Um banco recebe solicitação legalmente válida de autoridade competente e o setor de confor
- Uma pessoa obrigada comunica transação em espécie porque a operação se enquadra em hipótes
- Participante de mercado de valores mobiliários precisa estabelecer controles de PLD/FTP pr
- O setor de monitoramento encontra repetidas entradas de numerário sem explicação compatíve
- A instituição de relacionamento digital completa cadastro de cliente, mas, após mudança re
- Banco amplia operações para área geográfica com riscos distintos e lança novo produto por 
- Após analisar comunicações de instituições distintas, o Coaf identifica fundados indícios 
- Instituição obrigada deixa de implementar controles exigidos e a fiscalização identifica f
- Participante do mercado de valores mobiliários recebe pedido de abertura de relacionamento
- Um supervisor financeiro, no exercício legal de fiscalização, solicita informações protegi
- Investidor apresenta renda e origem patrimonial comprovadas, mas deseja comprar produto cu
- Um sistema aponta operação atípica. A equipe verifica documentação comercial consistente, 
- No processo de PLD/FTP, um dirigente procura saber que papel cabe ao Coaf na relação com p
- Em apuração de suposta lavagem, uma autoridade competente solicita dados de qualificação p
- Uma investidora residente no Brasil participa de sociedade cuja cadeia de controle envolve
- Em 2026, um analista analisa tentativa de sacar em espécie recursos de conta destinada a r
- Uma instituição sujeita a deveres de PLD/FTP descumpre regras de cadastro e comunicação. A
- Organização sem fins lucrativos recebe doações formalmente regulares, mas movimenta valore
- Uma pessoa utiliza entradas em espécie, transferências sucessivas e aquisição de ativos pa
- Uma comunicação feita por banco ao Coaf equivale a prova judicial definitiva da lavagem, d
- Uma área de conformidade recebe alerta de possível ocultação de recursos e verifica que o 
- Uma cliente classificada como pessoa exposta politicamente fornece documentação coerente s
- Um banco recebe ordem judicial válida relativa a dados financeiros de cliente e fornece as
- Em treinamento de PLD/FTP, orienta-se que o RIF é elaborado pelo banco comunicante e apena
- Após abrir conta de empresa e identificar representante, o banco analisa capacidade financ
- Uma corretora de valores sujeita à regulação da CVM pode desconsiderar a Resolução CVM nº 
- Uma instituição avalia alerta por transação atípica, verifica documentos e registra justif
- Um gerente sustenta que a LC nº 105/2001 impede que o Banco Central examine operações de i
- Investidor ligado indiretamente, por estrutura societária, a jurisdição indicada em listas
- Após a Resolução CVM nº 245/2026, qualquer investidor não residente passou a ser presumido
- Um banco remete comunicação por hipótese objetiva de operação em espécie e, separadamente,
- Uma instituição comunica operação suspeita ao Coaf e seu gerente informa ao cliente que o 
- Uma organização obrigada não possui política efetiva de PLD/FTP e descumpre reiteradamente
- Como uma movimentação aparece entre os exemplos da Carta Circular BCB nº 4.001/2020, o ban
- No tratamento de solicitações relacionadas a possível lavagem, dados cadastrais básicos e 
- A atualização de 2026 da Carta Circular BCB nº 4.001/2020 passou a mencionar tentativas de
- Uma instituição encaminha comunicação de suspeita de boa-fé ao Coaf, por canal apropriado 
- Em avaliação de cliente, a instituição confirma que um investimento corresponde à tolerânc
```

## Pedido 5: Epidemiologia clínica, revisão sistemática e avaliação de tecnologias em saúde (`saude-publica.epidemiologia-clinica-evidencias-ats`, 120 tópicos de edital)

```
Matéria: saude-publica.epidemiologia-clinica-evidencias-ats (Epidemiologia clínica, revisão sistemática e avaliação de tecnologias em saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de epidemiologia clínica, desfechos e medidas": 14
- "2. Desenhos de pesquisa e fases do desenvolvimento clínico": 19
- "3. Revisão sistemática, metanálise e qualidade do relato": 25
- "4. Sistema GRADE, níveis de certeza e força de recomendação": 21
- "5. Avaliação de tecnologias em saúde e evidências para decisões": 21

Já existem (não repita):
- Uma equipe investiga se um novo tratamento reduz internações de adultos com doença crônica
- Um levantamento encontrou 90 pessoas com determinada doença entre 3.000 residentes na data
- Em uma coorte, 24 de 240 expostos adoeceram; entre 300 não expostos, ocorreram 15 casos. Q
- Em um ano, o risco de um evento foi 20% sem tratamento e 10% com a intervenção. Qual o núm
- Um teste detectou 72 resultados positivos entre 80 participantes que realmente tinham a do
- Uma pesquisa separou pessoas com e sem exposição ocupacional e as acompanhou para observar
- Para pesquisar uma doença muito rara, pesquisadores selecionaram pacientes acometidos e co
- Uma pesquisadora gerou sequência aleatória de tratamento, manteve envelopes opacos para im
- Um medicamento ainda em desenvolvimento será administrado inicialmente a pequeno grupo, co
- Uma tecnologia já mostrou benefício preliminar, e agora será comparada ao cuidado padrão e
- Em ensaio com doença estável, cada participante receberá dois tratamentos em períodos suce
- Um ensaio planejado para demonstrar superioridade não encontrou diferença estatisticamente
- Uma equipe prepara revisão sistemática de intervenções, registrando motivos de exclusão de
- Uma metanálise mostra I² de 80% e resultados que divergem em magnitude e direção. Qual pro
- Ensaios randomizados sobre mortalidade apresentam grande perda de seguimento associada ao 
- Dois ensaios estimam redução de mortalidade de 20% para 12% e de 20% para 18%, respectivam
- Uma avaliação compara a terapia nova com a habitual por diferença de custos e por anos de 
- Em uma coorte de 100 expostos, 40 apresentam evento, contra 20 de 100 não expostos. A razã
- Um ensaio de superioridade apresenta RR de 0,85 para morte, com IC 95% de 0,65 a 1,11. Qua
- Ensaios randomizados em jovens sem comorbidades sustentam recomendação para idosos frágeis
- Em base assistencial, pacientes foram classificados como usuários de medicamento apenas ap
- Para prevenir infecções, 40 unidades assistenciais, e não pacientes, foram sorteadas entre
- Uma intervenção custa R$ 26.000 e produz 3,8 QALYs por pessoa; a alternativa custa R$ 20.0
- Uma equipe sustenta que uma autorização para comercializar medicamento faz nascer automati
- Uma revisão combina estudos de tratamentos radicalmente distintos para eventos medidos em 
- Em uma pesquisa que conta os casos existentes de asma em uma população na data de visita d
- Em estudo caso-controle que seleciona deliberadamente 100 casos e 100 controles, é possíve
- Durante o desenho de um ensaio randomizado, a ocultação da sequência de alocação procura i
- Um estudo de fase IV é, em regra, a primeira administração de medicamento experimental a s
- Uma revisão sistemática pode apresentar síntese narrativa quando os estudos incluídos dife
- Ao relatar um ensaio randomizado, seguir STROBE é mais apropriado que CONSORT, porque a pr
- Um resultado de ensaio randomizado pode receber certeza GRADE moderada ou baixa quando per
- Duas revisões têm I² próximo de zero; por isso, independentemente de diferenças na populaç
- O valor preditivo positivo de um teste tende a mudar quando a prevalência da doença muda, 
- Em estudos de avaliação econômica, análise de custo-minimização é indicada mesmo quando as
- Uma recomendação pode ser condicional mesmo quando a evidência relativa ao desfecho princi
- A presença de checklist PRISMA preenchido em uma revisão dispensa a avaliação do risco de 
- Uma tecnologia pode ter razão incremental de custo-efetividade favorável e, ainda assim, o
- Em coorte com eventos frequentes, o odds ratio pode ser interpretado numericamente como ri
- Se a incidência de um desfecho em um ano cai de 4% para 2% em ensaio válido, o NNT para ev
- Ao analisar prontuários, ajustar um modelo estatístico por idade e sexo garante, por defin
- Em estudo diagnóstico, um teste com sensibilidade de 90% e especificidade de 80% tem razão
- A classificação como fase IV significa que o estudo está necessariamente livre de randomiz
- Uma associação observacional de grande magnitude, com gradiente dose–resposta e possível c
- No processo de incorporação pública disciplinado pela Lei nº 8.080/1990, a existência de 1
```

## Pedido 6: Ensino de Língua Inglesa (didática) (`educacao.ensino-lingua-inglesa`, 120 tópicos de edital)

```
Matéria: educacao.ensino-lingua-inglesa (Ensino de Língua Inglesa (didática))
Capítulos e quantas questões quero em cada um (total 100):
- "BNCC e planejamento curricular em Língua Inglesa": 17
- "Inglês como língua franca e interculturalidade crítica": 17
- "Métodos de ensino, pós-método e metodologias ativas": 17
- "Letramentos, gêneros, multiletramentos e mídias": 17
- "Educação inclusiva, idades e turmas heterogêneas": 16
- "Avaliação da aprendizagem e tecnologias educacionais": 16

Já existem (não repita):
- Ao planejar uma campanha em inglês, a professora reúne leitura de cartazes, discussão de p
- No Ensino Fundamental, a BNCC apresenta habilidades específicas de Língua Inglesa principa
- Um docente afirma que a habilidade EF06LI03 pode ser observada quando alunos:
- Uma candidata encontra referência expressa ao DRC/MT no edital para docência de inglês. Qu
- Em um encontro de alunos de países distintos, todos usam inglês e precisam negociar inform
- Qual atividade trabalha a dimensão intercultural de forma crítica em aula de inglês?
- Uma aluna tem sotaque brasileiro e consegue resolver um mal-entendido por meio de reformul
- Um projeto de Ciências e Inglês usa dados ambientais internacionais para propor medidas na
- Qual característica é mais própria de uma tarefa comunicativa em língua inglesa?
- A perspectiva de pós-método mais adequadamente enfatiza:
- Em uma sequência, estudantes ouvem diálogo-modelo e repetem padrões várias vezes para auto
- Ao preparar aulas para crianças, adolescentes e adultos, qual orientação pedagógica é mais
- A professora solicita que a turma localize o preço de ingresso num anúncio escrito em ingl
- Uma turma interpreta um vídeo que combina narração, fotografias, legendas e gráficos. O ma
- Qual prática caracteriza melhor uma proposta de multiletramentos em inglês?
- Uma aula de leitura crítica compara duas notícias em inglês sobre o mesmo evento. Qual açã
- Numa turma de níveis variados, qual medida é mais inclusiva sem abandonar objetivos de apr
- Para que um estudante com deficiência visual interprete gráfico de uma campanha em inglês,
- Em um projeto de anúncios, todos recebem objetivo comunicativo comum, com modelos opcionai
- Segundo a perspectiva de educação inclusiva prevista na legislação brasileira, a escola de
- Na BNCC do Ensino Fundamental, os cinco eixos de inglês são articulados em situações de us
- A BNCC representa um plano de aula nacional detalhado que dispensa currículos de redes e p
- É correto estudar o DRC/MT considerando a etapa escolar e a versão pertinente, sem presumi
- Os códigos EF06LI a EF09LI devem ser transpostos literalmente para todo o Ensino Médio, po
- O inglês como língua franca permite trabalhar estratégias de reformulação e esclarecimento
- O conceito de inglês como língua franca exige a eliminação de todos os usos que não reprod
- Uma investigação de representações culturais e relações de poder em campanhas em inglês po
- Numa proposta plurilíngue, qualquer emprego estratégico do português em sala invalida a ap
- Repetição de padrões e diálogos modelares são características associadas historicamente ao
- Uma sequência baseada em tarefas proíbe, por definição, qualquer planejamento prévio e qua
- No pós-método, o professor toma decisões pedagógicas contextualizadas, com reflexão e crit
- Metodologias ativas tornam desnecessária a mediação do professor, uma vez que todo conteúd
- Scanning é uma estratégia de leitura útil para identificar informação específica em um anú
- A multimodalidade se limita ao uso de duas línguas escritas no mesmo texto, independenteme
- Os multiletramentos podem ser desenvolvidos em materiais impressos, desde que a proposta e
- Uma notícia com fotografia atraente torna desnecessária a checagem de autoria, data e evid
- Legendas, textos compatíveis com leitores de tela e descrições de imagens podem remover ba
- A melhor diferenciação para turmas heterogêneas consiste em fixar estudantes permanentemen
- A avaliação formativa utiliza evidências durante a aprendizagem para orientar feedback e m
- A aplicação de um quiz em aplicativo torna a avaliação automaticamente formativa, mesmo qu
```

## Pedido 7: Anatomia do Sistema Esquelético, Fisiologia Muscular, Circulação Sanguínea, Respiração… (`ciencias.anatomia-sistema-esqueletico-fisiologia-muscular`, 119 tópicos de edital)

```
Matéria: ciencias.anatomia-sistema-esqueletico-fisiologia-muscular (Anatomia do Sistema Esquelético, Fisiologia Muscular, Circulação Sanguínea, Respiração…)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organização corporal, células, histologia, ossos e articulações": 28
- "2. Fisiologia muscular, coração, vasos, sangue e respiração": 23
- "3. Digestão, metabolismo energético, função renal e equilíbrio ácido-base": 17
- "4. Sistema nervoso, controle endócrino, linfa e respostas imunes": 16
- "5. Reprodução, hereditariedade, embriologia, envelhecimento e integração sistêmica": 16

Já existem (não repita):
- Ao examinar uma imagem anatômica, uma estudante precisa identificar o plano que divide o c
- Uma amostra de tecido apresenta células muito próximas, com pequena quantidade de matriz e
- Em uma maquete da coluna vertebral adulta, foram identificadas as regiões cervicais, torác
- Ao identificar a articulação do joelho, um estudante observa cápsula, cavidade com líquido
- Em exame de lâmina, identificaram-se células musculares estriadas, ramificadas e conectada
- Uma hemácia sai do ventrículo direito e segue em direção aos pulmões. Qual vaso ela atrave
- Durante inspiração tranquila, um observador registra a movimentação do diafragma e a entra
- Em um estudo sobre digestão de gorduras, alguém afirma que a vesícula produz a bile libera
- Durante uma investigação fisiológica, observa-se que determinado hormônio aumenta a permea
- Em uma avaliação de reparo ósseo, foram observadas células depositando osteoide e outras r
- Uma fibra muscular esquelética é estimulada eletricamente, mas a liberação de cálcio do re
- Um hemograma revela alteração no número de plaquetas. Qual função do sangue tem relação ma
- Uma pesquisa acompanha a fase do ciclo cardíaco em que ventrículos se contraem e ejetam sa
- Após retenção de CO2 por ventilação insuficiente, uma turma avalia o efeito sobre o equilí
- Em atividade experimental, um estímulo doloroso no pé gera retirada reflexa antes de a pes
- Duas pessoas adquirem proteção imunológica: uma após vacinação e outra após receber antico
- Em ciclo ovariano espontâneo, observou-se concentração sustentada de estradiol no fim da f
- Uma célula germinativa humana conclui a meiose I sem erros de segregação e dá origem a dua
- No desenvolvimento humano, uma estrutura embrionária passa por gastrulação e forma três fo
- Durante exercício prolongado de intensidade moderada, parte dos ácidos graxos é oxidada em
- Após lesão cutânea, o sangramento é contido, células imunes removem detritos, fibroblastos
- Uma diminuição da perfusão renal ativa mecanismos hormonais destinados a ajudar a preserva
- Um potencial de ação alcança o terminal de um neurônio motor, e a fibra muscular recebe ne
- Ao comparar envelhecimento e treinamento, uma pesquisa encontra diminuição da força muscul
- Ao estudar estresse, uma turma compara resposta imediata de aceleração cardíaca e resposta
- Um professor utiliza a posição anatômica para descrever que o punho é distal ao cotovelo, 
- Ao examinar uma articulação sinovial, uma estudante conclui que ligamentos conectam os mús
- Em repouso, a expiração tranquila pode ocorrer predominantemente pelo relaxamento dos músc
- Após atravessar o glomérulo renal, o filtrado já possui obrigatoriamente composição idênti
- Uma pessoa que recebe imunoglobulinas prontas após exposição a um agente infeccioso adquir
- Em um ciclo ovariano espontâneo, a prolactina da neuro-hipófise provoca o pico ovulatório 
- Uma amostra com matriz extracelular abundante e condrócitos em lacunas pode corresponder a
- Ao segurar um halter imóvel com o cotovelo flexionado, há esforço muscular; logo, a contra
- O sangue que chega ao átrio esquerdo pelas veias pulmonares, em condições normais, passou 
- Um pesquisador descreve a maior parte da absorção de nutrientes no intestino grosso, pois 
- Após elevação sustentada da glicemia, a secreção de insulina pelas células beta pancreátic
- A transferência de IgG materna através da placenta representa imunidade ativa natural do f
- Na mitose somática humana típica, a segregação de cromátides-irmãs permite formar duas cél
- Quando a pressão arterial cai rapidamente ao levantar-se, barorreceptores participam de aj
- Uma redução de bicarbonato por distúrbio metabólico pode provocar aumento compensatório da
- Em uma análise de tecidos, células nervosas do SNP que levam comandos aos músculos esquelé
- Durante o desenvolvimento embrionário, a gastrulação forma os três folhetos germinativos, 
- A exposição embrionária a um possível teratógeno produz necessariamente a mesma malformaçã
- No reparo cutâneo, fibroblastos e colágeno podem produzir cicatriz fibrosa quando não é po
- Uma cultura celular apresenta falhas em pontos de checagem do ciclo, proliferação desregul
```

## Pedido 8: Biblioteconomia: automação, bibliotecas digitais e preservação digital (`biblioteconomia.automacao-bibliotecas-digitais-preservacao`, 119 tópicos de edital)

```
Matéria: biblioteconomia.automacao-bibliotecas-digitais-preservacao (Biblioteconomia: automação, bibliotecas digitais e preservação digital)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento da automação, sistemas integrados e bases especializadas": 20
- "2. RFID, recuperação distribuída e interoperabilidade entre sistemas": 16
- "3. Bibliotecas digitais, metadados e plataformas de publicação e descoberta": 28
- "4. Preservação digital, OAIS, ISO 16363 e identificadores persistentes": 18
- "5. Curadoria digital, governança, descoberta avançada e inteligência artificial": 18

Já existem (não repita):
- Uma biblioteca pretende implantar uma solução para gerenciar catalogação, empréstimos, dev
- Uma equipe precisa conferir rapidamente muitos exemplares nas estantes sem apontar um leit
- Na catalogação cooperativa, um sistema consulta outra biblioteca e recupera registros bibl
- Uma instituição deseja receber dissertações em PDF, descrevê-las, agrupá-las em coleções e
- Uma revista científica busca informatizar submissão de manuscritos, distribuição para aval
- Ao descrever um conjunto de objetos digitais, uma bibliotecária decide usar o Dublin Core 
- Um portal quer apresentar em uma única busca resultados provenientes do catálogo local, de
- Um agregador consulta periodicamente repositórios para obter registros descritivos e const
- Após mudança do servidor de um repositório, pretende-se conservar a referência estável dos
- Na troca do SIGB, foram importados corretamente os títulos, mas os empréstimos passaram a 
- Na implementação de catálogo jurídico, uma decisão aparece sem data e sem identificação de
- Uma biblioteca deseja oferecer consulta a registros remotos por requisições web e expressõ
- Um consórcio precisa colher dados de instituições, converter campos de seus esquemas difer
- Um produtor envia arquivos e metadados a um arquivo digital. O sistema prepara posteriorme
- Uma organização adota procedimentos inspirados no modelo OAIS e pretende demonstrar sua co
- Em uma auditoria, o arquivo recebido possui hash SHA-256 registrado. Meses depois, uma nov
- Na implementação de RFID, deseja-se reduzir exposição de dados pessoais e facilitar eventu
- Após ingresso de um documento em repositório aderente ao referencial OAIS, o responsável d
- Uma especificação de projeto mistura a exigência de interoperabilidade RFID, a descrição d
- Durante a revisão de metadados de um artigo, a equipe quer registrar licença e condições d
- Um agregador pretende colher conjuntos de registros de repositórios, mas seu gestor exige 
- Uma base jurídica reúne versões de normas e julgados sem distinguir data de publicação, ev
- Uma coleção possui arquivos cujo formato já não é interpretável pelos programas atuais, em
- Um serviço de inteligência artificial sugere resumo e identificador de uma decisão que ser
- Um projeto combina SIGB de acervo físico, repositório de teses, busca unificada e preserva
- Após identificar que a biblioteca precisa controlar reservas, devoluções e situações de ex
- Em inventário de acervo físico, a adoção de etiquetas RFID torna desnecessário integrar as
- Uma biblioteca pode utilizar Z39.50 para consultar e recuperar registros bibliográficos de
- Uma equipe pretende gerenciar as etapas de avaliação e edição de artigos utilizando DSpace
- Para construir um registro de objeto digital, o profissional pode utilizar os quinze eleme
- Um repositório que disponibiliza PDF em endereço estável fica dispensado de planejar prese
- Um catálogo jurídico com textos integrais pode exigir metadados que permitam separar legis
- O protocolo OAI-PMH 2.0 exige que o provedor de dados entregue, em cada operação ListRecor
- Na exposição de um acervo, o REPOX pode apoiar a importação e transformação de metadados o
- Ao aplicar OAIS, a instituição deve usar a sigla DIP para o pacote de submissão enviado pe
- A ISO 16363:2025 apresenta base para auditoria e certificação de repositórios digitais con
- Um artigo que recebeu DOI pode ser transferido entre plataformas sem que o serviço de regi
- Na gestão de materiais depositados sob embargo, é possível oferecer metadados pesquisáveis
- Uma biblioteca substituiu todos os códigos de barras por RFID e, por isso, pode gravar nom
- Na curadoria de fotografias históricas, a verificação periódica de hashes pode confirmar f
- Um repositório adotou DSpace e publicou declaração de alinhamento ao OAIS; esses dois fato
- Uma biblioteca digital usa reconhecimento de texto para permitir pesquisa em documentos or
- Um sistema de descoberta que apresenta resultados duplicados de uma obra deve resolver o p
- Uma instituição pode usar IA para sugerir descritores de um acervo, desde que implante rev
- A publicação de metadados de documentos em interface pública implica autorização para divu
```

## Pedido 9: Educação Fiscal e o Programa Nacional de Educação Fiscal (PNEF) (`administracao-publica.educacao-fiscal-pnef`, 119 tópicos de edital)

```
Matéria: administracao-publica.educacao-fiscal-pnef (Educação Fiscal e o Programa Nacional de Educação Fiscal (PNEF))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, características e papel social da educação fiscal": 20
- "2. Função social dos tributos e relação Estado-sociedade": 20
- "3. O PNEF: objetivos, GEF e grupos vinculados": 20
- "4. Competências dos órgãos no PNEF": 20
- "5. Educação fiscal na escola e no ensino superior": 20

Já existem (não repita):
- A educação fiscal pode ser definida como:
- A educação fiscal tem como foco exclusivo a arrecadação de tributos, não se ocupando da ap
- São características da educação fiscal, EXCETO:
- A educação fiscal contribui para a promoção da dignidade da pessoa humana ao mostrar que o
- Entre os papéis sociais da educação fiscal está:
- A função extrafiscal do tributo consiste em:
- O controle social do gasto público, estimulado pela educação fiscal, substitui o controle 
- São exemplos de instrumentos que favorecem a relação Estado-sociedade e o controle social,
- Exigir nota fiscal nas compras e recusar produtos de contrabando e pirataria são comportam
- A gestão democrática dos recursos públicos, tema da educação fiscal, envolve:
- A função redistributiva da tributação relaciona-se sobretudo à progressividade dos tributo
- O Programa Nacional de Educação Fiscal (PNEF) foi implementado por meio de:
- Entre os objetivos do PNEF previstos na Portaria Interministerial 413/2002 está sensibiliz
- NÃO consta entre os objetivos do PNEF previstos no art. 1º da Portaria Interministerial 41
- A implementação do PNEF foi atribuída ao:
- Pela Portaria 413/2002, o GEF é composto por representantes do Ministério da Educação, da 
- Segundo a Portaria 413/2002, a coordenação e a secretaria-executiva do PNEF e do GEF cabia
- Os grupos vinculados ao GEF no âmbito estadual, da Receita Federal e municipal são designa
- As deliberações do GEF e dos grupos a ele vinculados são tomadas por unanimidade.
- Compete ao GEF, segundo a Portaria 413/2002, desautorizar materiais e ações incompatíveis 
- Segundo a Portaria 413/2002, compete ao Ministério da Educação, entre outras atribuições:
- Pela Portaria 413/2002, cabe à Secretaria do Tesouro Nacional auxiliar na elaboração de ma
- No PNEF, a coordenação do grupo de educação fiscal no âmbito federal da Receita (GEFF) cab
- Às Secretarias de Fazenda estaduais compete institucionalizar e coordenar o Grupo de Educa
- Segundo a Portaria 413/2002, compete à ESAF:
- Os grupos municipais de educação fiscal (GEFM) são institucionalizados e coordenados pelas
- Entre as atribuições dos grupos vinculados ao GEF (GEFE, GEFF e GEFM) está:
- As regras da Portaria 413/2002 referentes aos Estados e às Secretarias de Fazenda aplicam-
- Na educação básica, a educação fiscal é tratada preferencialmente como:
- A formação de professores como disseminadores da educação fiscal é estratégia central do P
- No ensino superior, a educação fiscal pode ser desenvolvida na extensão universitária, por
- A indissociabilidade entre ensino, pesquisa e extensão, prevista na Constituição para as u
- É exemplo de pesquisa acadêmica na perspectiva da educação fiscal:
- A inserção da educação fiscal no ensino superior é relevante porque as universidades forma
- A educação fiscal, por ter caráter interdisciplinar, dialoga com áreas como ética, economi
- A afirmação de que os recursos arrecadados com tributos pertencem à sociedade, e não ao go
- A Escola de Administração Fazendária, que coordenava o PNEF, foi incorporada à Escola Naci
- Segundo a Portaria 413/2002, cada órgão integrante do GEF deve indicar:
- Entre os temas trabalhados nos materiais de formação do PNEF está:
- O caráter permanente da educação fiscal significa que ela:
```

## Pedido 10: Alteração do contrato de trabalho e jus variandi (`direito-trabalho.alteracao-contrato`, 117 tópicos de edital)

```
Matéria: direito-trabalho.alteracao-contrato (Alteração do contrato de trabalho e jus variandi)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípio da inalterabilidade contratual lesiva e modalidades de alteração": 14
- "2. Jus variandi, jus resistentiae e mudanças de função e jornada": 41
- "3. Reversão da função de confiança, gratificação e vantagens adquiridas": 14
- "4. Transferência de localidade, despesas e adicional": 16
- "5. Hipóteses especiais, jurisprudência e solução de casos": 15

Já existem (não repita):
- Uma atendente concordou por escrito em reduzir seu salário-base de R$ 3.500 para R$ 3.000,
- Em uma empresa, o supervisor passa a distribuir os mesmos serviços entre integrantes da eq
- Uma empresa altera regulamento interno para suprimir vantagem concedida aos empregados atu
- Uma empregada retorna da coordenação de confiança para o cargo efetivo de analista anterio
- Após assumir serviço em outro prédio no mesmo município, o empregado continua morando no e
- Uma sociedade decide transferir gerente para outra cidade com mudança de domicílio, invoca
- Um empregado muda provisoriamente de cidade por necessidade de serviço e passa a residir n
- Em uma transferência comum determinada pelo empregador, os custos decorrentes da mudança d
- Um trabalhador de uma loja passa do turno noturno para o diurno e deixa de executar trabal
- Uma trabalhadora de empresa pública pede remoção para acompanhar companheiro militar deslo
- Após o encerramento efetivo de uma filial, trabalhadores são encaminhados à unidade de out
- Um empregado de cargo de confiança foi transferido provisoriamente de cidade por necessida
- Uma empresa suprimiu horas extras prestadas habitualmente durante três anos e manteve a jo
- Uma empresa reduz durante a vigência da chefia o valor da gratificação de confiança, sem r
- Um empregado público pede transferência para acompanhar cônjuge servidor deslocado por int
- Um empregado ocupou função de confiança durante todo o período de 2018 a 2026 e foi revert
- O empregador transferiu, em caráter provisório, uma trabalhadora com salário de R$ 5.600 p
- A empresa oferece para um técnico a opção de ser transferido definitivamente a outra cidad
- Uma empregada pública busca a transferência do art. 469-A porque seu cônjuge foi deslocado
- Uma trabalhadora completou dez anos de gratificação de função em 2015 e foi revertida em 2
- Uma empresa reduz parcialmente as horas extras prestadas com habitualidade por dois anos. 
- Um contrato prevê mobilidade geográfica, e a empresa transfere o empregado por necessidade
- Um empregador reduziu a carga contratual semanal com consentimento escrito do empregado, m
- Uma empresa transfere definitivamente seu empregado para cidade distante e paga o frete. O
- Ao interpretar conjuntamente os arts. 469, 469-A e 470 da CLT, qual das proposições se man
- Um empregado aceita aditivo contratual com perda econômica indireta, embora sem diminuição
- Como o empregador dirige a prestação de serviços, pode atribuir unilateralmente tarefas in
- Após desempenhar função de confiança, uma empregada retorna ao cargo efetivo anterior. A r
- A simples mudança de unidade, ainda que não exija mudança de domicílio, constitui necessar
- O art. 470 da CLT atribui ao empregador as despesas da transferência comum, obrigação que 
- Sob a regra atual do art. 468, § 2º, o simples exercício de função de confiança por dez an
- Uma empresa pretende transferir sem anuência empregado com previsão contratual de mobilida
- O adicional de transferência previsto no art. 469, § 3º, destina-se apenas a empregados se
- Uma empregadora extingue de fato o estabelecimento onde alguém presta serviços. O art. 469
- A aplicação do art. 469-A dispensa a existência de filial no destino, desde que o cônjuge 
- A transferência especial a pedido de empregado da administração pública, prevista no art. 
- A mudança efetiva do turno noturno para o diurno preserva indefinidamente o adicional notu
- Se horas extraordinárias habituais prestadas por mais de um ano forem suprimidas parcialme
- Em transferência provisória com mudança de domicílio e adicional devido pelo art. 469, § 3
- Um empregado público formula pedido de transferência do art. 469-A para acompanhar companh
- Para a transferência de caráter provisório, a OJ 113 impõe que a própria lei estabeleça pr
- Um trabalhador já havia completado dez anos em função gratificada antes de 11/11/2017, mas
- O IRR 137 do TST afastou a indenização pela supressão de horas extras habituais quando as 
- A Súmula 51, I, do TST protege vantagens regulamentares incorporadas aos contratos dos tra
- O art. 469-A da CLT confere o mesmo direito especial a qualquer empregado privado que dese
```

## Pedido 11: Unidades de Medidas (`ciencias.unidades-medidas`, 117 tópicos de edital)

```
Matéria: ciencias.unidades-medidas (Unidades de Medidas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Grandezas físicas, SI, prefixos e medidas de comprimento, massa e tempo": 34
- "2. Área, volume, capacidade, ângulo, arco, potência e energia: conversões": 33
- "3. Vetores, cinemática linear e angular e movimento circular": 11
- "4. Dinâmica, forças, atrito, trabalho, energia e quantidade de movimento": 11
- "5. Rotação, equilíbrio, alavancas, gravitação, pressão e condução veicular": 11

Já existem (não repita):
- Um percurso reto foi medido como 2,4 km. Qual distância corresponde à medição expressa em 
- O comprimento de uma peça é 350 mm. Qual é a mesma medida expressa em metros?
- Uma superfície mede 3 m². Qual é sua área em centímetros quadrados?
- Um terreno plano possui área de 2,5 hectares. Quanto isso representa em metros quadrados?
- Um reservatório tem volume interno de 0,75 m³. Qual capacidade em litros corresponde a ess
- Um frasco contém 250 mL de líquido. Qual volume corresponde ao conteúdo expresso em centím
- Uma carga possui massa de 3,2 toneladas métricas. Qual é sua massa em quilogramas?
- Uma atividade dura 2 horas e 15 minutos. Qual o intervalo completo expresso em segundos?
- Um ângulo plano mede 90°. Quanto corresponde ao mesmo ângulo em radianos?
- Uma quantidade de energia de 2 kWh deve ser expressa em joules. Qual resultado utiliza cor
- Um levantamento indica 2,5 km² de superfície. Quantos metros quadrados estão contidos ness
- Uma caixa tem capacidade geométrica de 7.500 cm³. Qual é sua capacidade em litros?
- Um instrumento registra ângulo de 2° 30′. Quantos minutos de arco essa abertura possui?
- Um automóvel apresenta rapidez de 72 km/h. Qual é a rapidez equivalente em unidades do SI?
- Um equipamento de potência constante de 500 W funciona por 30 minutos. Qual energia consum
- Dois deslocamentos sucessivos medem 6 m para leste e 8 m para norte. Qual módulo do desloc
- Uma força resultante horizontal de 20 N atua sobre corpo de massa de 4 kg, em referencial 
- Uma força de 50 N forma ângulo de 60° com o eixo horizontal positivo. Qual componente hori
- Um veículo de massa constante aumenta a rapidez de 10 m/s para 20 m/s. Qual relação entre 
- Uma roda de raio 0,50 m gira com velocidade angular constante de 6 rad/s. Qual é a rapidez
- Uma alavanca ideal possui carga de 200 N a 0,20 m do fulcro. A força aplicada atua do outr
- Uma força normal de 400 N está distribuída uniformemente sobre área de 0,020 m². Qual é a 
- Em uma colisão unidimensional isolada, corpo de 2 kg a 3 m/s atinge corpo de 1 kg em repou
- Um veículo trafega a 72 km/h. Seu tempo de reação é 1,5 s; após a reação, freia com desace
- Dois corpos puntuais atraem-se gravitacionalmente. Se a distância entre seus centros dobra
- Um recipiente tem volume de 2 dm³. Como um decímetro cúbico corresponde a um litro, a capa
- Um piso com área de 1 m² possui 100 cm², porque há exatamente cem centímetros em cada metr
- Em uma planilha de tempos, o intervalo de 1 h e 30 min pode ser registrado como 1,5 h ou 5
- Uma massa de 5 kg pode ser expressa diretamente como peso de 5 N, sem informação adicional
- Um veículo percorre 72 km em uma hora mantendo essa rapidez média. O valor de 72 km/h é eq
- Uma leitura de 90° corresponde a π rad, porque tanto grau quanto radiano medem ângulos pla
- Um recipiente de 0,040 m³ tem capacidade geométrica de 40 litros, porque cada metro cúbico
- Dois deslocamentos de 3 m para leste e 4 m para norte produzem deslocamento resultante de 
- A potência de 1 kW mantida por duas horas transfere 2 kWh de energia, equivalentes a 7.200
- Um bloco em repouso tem coeficiente de atrito estático de 0,5 e força normal de 100 N. Por
- Se a força resultante horizontal sobre um corpo de 5 kg é de 15 N, sua aceleração horizont
- Duas pessoas percorrem os mesmos 500 m em linha reta em 100 s e 50 s. A que percorre em 50
- Uma alavanca em equilíbrio possui 100 N a 0,4 m do fulcro de um lado; uma força de 50 N a 
- Em movimento circular uniforme, a aceleração centrípeta é nula porque o módulo da velocida
- Uma força de 20 N aplicada perpendicularmente à extremidade de uma barra de 0,50 m, em rel
- Em um choque isolado no qual dois corpos permanecem unidos, tanto a quantidade de moviment
- Uma caixa de massa 10 kg submetida a gravidade de 9,8 m/s² tem peso aproximado de 98 N; a 
- Mantida uma desaceleração constante de frenagem e desprezadas as demais variáveis, dobrar 
- Um disco em torno de eixo fixo, com momento de inércia de 2 kg·m² e velocidade angular de 
- Uma força normal de 100 N distribuída sobre área de 0,01 m² exerce pressão média de 100 Pa
```

## Pedido 12: Oncologia: Prevenção, Tratamento e Cuidado ao Paciente (`saude.oncologia-cuidado-paciente`, 117 tópicos de edital)

```
Matéria: saude.oncologia-cuidado-paciente (Oncologia: Prevenção, Tratamento e Cuidado ao Paciente)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos básicos, prevenção e detecção precoce": 20
- "2. Modalidades de tratamento e quimioterapia": 20
- "3. Segurança, extravasamento e cuidados de enfermagem": 20
- "4. Emergências oncológicas": 20
- "5. Síndromes paraneoplásicas e cuidados paliativos": 20

Já existem (não repita):
- Assinale a característica que distingue uma neoplasia maligna de uma benigna.
- Os carcinomas são neoplasias malignas de origem epitelial, enquanto os sarcomas têm origem
- No sistema de estadiamento TNM, a letra N refere-se:
- O principal fator de risco evitável para o desenvolvimento de câncer é:
- A vacinação contra o HPV e contra a hepatite B são estratégias de prevenção primária de câ
- Segundo as diretrizes do Ministério da Saúde/INCA, o rastreamento do câncer do colo do úte
- O Ministério da Saúde recomenda a mamografia de rastreamento a cada dois anos para mulhere
- Sobre a detecção precoce do câncer de próstata, segundo o Ministério da Saúde/INCA:
- A quimioterapia aplicada antes da cirurgia, com o objetivo de reduzir o tamanho do tumor, 
- Os antineoplásicos clássicos atingem principalmente células em divisão rápida, o que expli
- Paciente em uso de ciclofosfamida em dose alta. Para prevenir cistite hemorrágica, além da
- Relacione a droga à toxicidade mais característica: doxorrubicina, cisplatina e bleomicina
- A vincristina pode ser administrada por via intratecal quando houver acometimento do siste
- Após metotrexato em dose alta, a medida usada para proteger as células normais da toxicida
- Sobre a manipulação segura de antineoplásicos, é correto afirmar:
- Durante a infusão periférica de doxorrubicina, o paciente refere ardor e há edema no local
- No extravasamento de alcaloides da vinca, como vincristina e vimblastina, recomenda-se com
- São drogas antineoplásicas classificadas como vesicantes:
- O nadir da mielossupressão provocada pela quimioterapia costuma ocorrer:
- Na prevenção de náuseas e vômitos induzidos por quimioterapia altamente emetogênica, usam-
- Entre as orientações ao paciente com mucosite oral por quimioterapia, inclui-se:
- O paciente submetido a radioterapia externa (teleterapia) torna-se radioativo e deve perma
- Constitui orientação adequada ao paciente em radioterapia, para prevenir radiodermite:
- Paciente no 10º dia após quimioterapia chega com temperatura de 38,6 °C e neutrófilos de 3
- A síndrome de lise tumoral caracteriza-se por hiperpotassemia, hiperfosfatemia, hiperurice
- O maior risco de síndrome de lise tumoral ocorre:
- Paciente com câncer de mama metastático refere dor dorsal progressiva, que piora ao deitar
- Edema de face e pescoço, circulação colateral na parede torácica e dispneia em paciente co
- No tratamento inicial da hipercalcemia da malignidade sintomática, a medida fundamental é:
- Fumante de 62 anos com carcinoma de pequenas células do pulmão apresenta sódio sérico de 1
- Síndromes paraneoplásicas são manifestações causadas por hormônios, peptídeos ou anticorpo
- Sobre a síndrome miastênica de Lambert-Eaton, assinale a correta.
- A tromboflebite migratória (síndrome de Trousseau) é uma manifestação paraneoplásica class
- A policitemia por produção de eritropoetina é uma síndrome paraneoplásica clássica do carc
- Sobre os cuidados paliativos, segundo a concepção da Organização Mundial da Saúde, é corre
- Pela escada analgésica da OMS, o paciente oncológico com dor intensa não controlada por op
- Ao iniciar morfina para dor oncológica, recomenda-se prescrever laxante de forma profiláti
- Paciente em cuidados paliativos perde a via oral, com dificuldade de acesso venoso. Uma al
- A quimioterapia adjuvante é administrada após o tratamento local do tumor, com o objetivo 
- Gestantes e lactantes não devem manipular quimioterápicos antineoplásicos, e as excretas d
```

## Pedido 13: Estática, hidrostática e gravitação (`fisica.estatica-hidrostatica-gravitacao`, 117 tópicos de edital)

```
Matéria: fisica.estatica-hidrostatica-gravitacao (Estática, hidrostática e gravitação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistemas de forças e equilíbrio dos corpos rígidos": 18
- "2. Momentos, binário, alavancas e centro de massa": 20
- "3. Hidrostática: densidade, pressão, Stevin e vasos comunicantes": 16
- "4. Princípio de Pascal, prensa hidráulica e empuxo": 18
- "5. Gravitação universal, campo gravitacional e leis de Kepler": 28

Já existem (não repita):
- Uma viga permanece imóvel sobre dois apoios após aplicação de várias forças verticais. Qua
- Uma chave de boca de 0,40 m recebe força de 50 N perpendicular à sua extremidade, com rota
- Duas forças horizontais contrárias, de 10 N cada, atuam em linhas paralelas separadas por 
- Um recipiente contém 3 kg de líquido homogêneo em volume de 0,003 m³. Qual é a massa espec
- Um ponto está a 2 m abaixo da superfície de água em repouso, com densidade 1000 kg/m³ e g=
- Uma prensa hidráulica ideal possui êmbolo de entrada de 2 cm² e saída de 10 cm². Se a forç
- Um bloco completamente imerso desloca 0,002 m³ de água, com densidade 1000 kg/m³ e g=10 m/
- Duas massas pontuais mantêm seus valores e passam da separação r para 2r. Como varia a for
- Em uma órbita elíptica ideal em torno de um astro, uma trajetória deve respeitar a primeir
- Uma viga horizontal leve de 6 m apoia-se nas extremidades A e B. Uma carga vertical de 300
- Duas massas pontuais de 2 kg e 4 kg estão nas posições x=0 m e x=9 m. Qual é a abscissa do
- Um mergulhador está a 4 m de profundidade em água de densidade 1000 kg/m³. Admitindo g=10 
- Em vasos comunicantes com líquidos imiscíveis, as pressões superiores são iguais. Um líqui
- Uma prensa hidráulica ideal tem áreas de 4 cm² e 20 cm². O êmbolo menor avança 10 cm, sem 
- Um bloco homogêneo de densidade 750 kg/m³ flutua livremente em água de densidade 1000 kg/m
- A que altitude acima de uma Terra esférica ideal o campo gravitacional vale um quarto de s
- Dois planetas orbitam o mesmo centro gravitacional dominante. O segundo tem semieixo maior
- Uma placa horizontal homogênea de 3 m tem peso de 120 N atuando no centro e recebe adicion
- Uma força horizontal de 80 N atua a 0,60 m acima do pivô de uma placa, gerando torque horá
- Um reservatório aberto contém água e um manômetro indica 60 kPa em ponto interno. Se g=10 
- Um bloco de volume 0,004 m³ tem massa 2 kg e flutua em água de densidade 1000 kg/m³, sob g
- Uma prensa hidráulica ideal eleva carga de 800 N no êmbolo de 40 cm² por 1 cm. O êmbolo me
- Um satélite descreve órbita circular ideal em torno do mesmo astro central. Seu raio orbit
- Duas massas se atraem com intensidade F. Em novo arranjo, cada massa é duplicada e a distâ
- Um planeta em órbita elíptica percorre região próxima do periastro e depois região próxima
- Uma estrutura plana em repouso, sujeita a forças externas, deve satisfazer simultaneamente
- Duas forças iguais, paralelas e contrárias, aplicadas em linhas distintas, possuem força r
- Em líquido homogêneo em repouso, a pressão manométrica a uma profundidade h abaixo de supe
- Na prensa hidráulica ideal, a força transmitida ao êmbolo de maior área é obrigatoriamente
- Um corpo parcialmente imerso em fluido em repouso sofre empuxo igual ao peso do volume de 
- Pela lei de gravitação universal, se a distância entre duas massas puntiformes dobra e as 
- Uma barra de peso desprezível tem carga pontual deslocada em direção a um de seus apoios; 
- O centro de massa de duas partículas de massas diferentes em posições distintas coincide c
- Em vasos comunicantes com o mesmo líquido, sem capilaridade relevante e com igual pressão 
- Dois pontos de profundidades diferentes em um mesmo líquido em repouso necessariamente apr
- Um objeto homogêneo com densidade de 800 kg/m³ flutua livremente em água de 1000 kg/m³, fi
- No modelo de Terra esférica, ao subir até altitude igual a um raio terrestre, um corpo pas
- Segundo a segunda lei de Kepler, o segmento que une um planeta ao astro central varre área
- Em uma viga plana em equilíbrio, se o momento total é nulo em um polo escolhido, já se pod
- Uma força cujo vetor posição em relação ao pivô é paralelo à própria força exerce momento 
- Na Lei de Stevin, a diferença de pressão entre dois pontos do mesmo líquido homogêneo em r
- O peso aparente de um corpo submerso, quando mantido imóvel por um fio de sustentação supe
- Em prensa hidráulica ideal, dobrar a força obtida na saída mediante aumento da razão de ár
- Um corpo em órbita circular ideal pode ter velocidade escalar constante, embora sua aceler
- Para corpos que orbitam o mesmo centro gravitacional dominante, se o semieixo maior é mult
```

## Pedido 14: Audiências, arquivamento, revelia e confissão (`processo-trabalho.audiencias`, 117 tópicos de edital)

```
Matéria: processo-trabalho.audiencias (Audiências, arquivamento, revelia e confissão)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura da audiência, notificação e organização dos atos": 15
- "2. Conciliação, comparecimento, preposto e apresentação da defesa": 18
- "3. Ausência do reclamante, arquivamento, custas e perempção": 25
- "4. Ausência da reclamada, revelia, confissão e limites à presunção": 27
- "5. Exceções, instrução e conclusão da audiência em casos integrados": 15

Já existem (não repita):
- Uma audiência trabalhista ordinária é marcada para as 17h de dia útil e se discute qual li
- Após protocolar uma reclamação, a secretaria precisa remeter a notificação e designar audi
- Uma pessoa reclamante comparece à abertura da audiência trabalhista. Em qual momento o jui
- Uma empresa escolhe preposto que conhece os fatos discutidos, mas não integra seu quadro d
- A conciliação é recusada e o reclamado opta pela defesa oral. Segundo o art. 847 da CLT, q
- Uma reclamante, devidamente cientificada, não comparece à audiência inicial e não há justi
- Uma pessoa beneficiária da justiça gratuita falta injustificadamente à audiência inicial. 
- Uma reclamada falta à audiência inicial, apesar de cientificada. Qual é a consequência ind
- Uma audiência foi designada para 9h. O juiz não compareceu até 9h15 e os presentes desejam
- Uma audiência marcada para 10h não se inicia injustificadamente até 10h30, embora as pesso
- A reclamada já ofereceu contestação pelo sistema eletrônico, e a parte autora deseja desis
- Uma empresa notificada da reclamação quer arguir incompetência territorial antes da audiên
- Uma pessoa reclamante teve duas reclamações consecutivas arquivadas porque não compareceu 
- Após defesa em audiência, a instrução foi adiada. A pessoa reclamante não comparece à cont
- Uma reclamada falta à audiência, mas seu advogado está presente e entrega contestação e do
- Após a instrução, o juiz concede fala final a cada litigante e renova a proposta de concil
- A empresa arguiu tempestivamente exceção de incompetência territorial, e o processo foi su
- Uma reclamada ausente enfrenta pedido fundado em afirmação de fato contrária a prova docum
- Uma pessoa reclamante falta à audiência em continuação, após contestação, apesar de intima
- Uma empresa falta à audiência, mas há dois reclamados e o segundo apresenta contestação do
- Um empregado distribui reclamação verbal, mas, sem força maior, deixa de comparecer em cin
- Uma parte faltou à sessão para depor, com intimação e cominação expressas. Há documentos a
- Em audiência, o juiz propõe composição na abertura, recebe defesa, instrui e concede razõe
- Uma pessoa reclamante apresentou dois processos arquivados consecutivamente por suas falta
- Em audiência de julgamento, a empresa comparece por preposto que conhece fatos, mas este n
- Um processo é pautado para audiência ordinária em dia útil, das 8h às 18h, com limite de c
- Uma audiência está marcada para 9h; o juiz não aparece até 9h15. Pelo art. 815, § 1º, os p
- Uma empresa escolhe preposto conhecedor dos fatos, embora não seja seu empregado. O art. 8
- Na audiência inicial, a parte reclamante falta sem justificativa, e a ré comparece. A CLT 
- Uma pessoa reclamante ausente à audiência inicial possui quinze dias para comprovar motivo
- Na audiência inicial, a parte reclamada falta, e o art. 844, caput, prevê somente arquivam
- Uma parte deseja oferecer defesa oral depois da primeira conciliação frustrada. O art. 847
- Uma sessão de audiência não começa, injustificadamente, após trinta minutos da hora previs
- Depois de apresentada contestação escrita por sistema eletrônico, a parte reclamante não p
- Uma pessoa reclamante que causou dois arquivamentos consecutivos por ausência à audiência 
- Uma parte reclamante deixa de comparecer à audiência de instrução adiada, depois de ter ha
- Uma reclamada falta à audiência, mas o advogado constituído comparece com contestação e do
- A empresa arguíu incompetência territorial por petição própria quatro dias após a notifica
- Depois da produção das provas, cada parte tem até vinte minutos para razões finais orais, 
- Em ação com mais de um reclamado, um deles contesta as alegações comuns enquanto o outro f
- Ao constatar que uma reclamante intimada com cominação faltou à sessão em que deveria depo
- Uma reclamação verbal é distribuída, mas o reclamante, sem força maior, deixa de apresentá
- Apresentada exceção de incompetência territorial antes da audiência, dentro dos cinco dias
- A parte autora ausente à audiência inicial sem justificativa comprovada pode sofrer custas
- Concluída a instrução, o art. 850 da CLT exige que as partes aceitem a segunda proposta de
```

## Pedido 15: Colonialismo, descolonização e Américas (`historia.colonialismo-descolonizacao-americas`, 116 tópicos de edital)

```
Matéria: historia.colonialismo-descolonizacao-americas (Colonialismo, descolonização e Américas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Colonialismo mercantil e imperialismo industrial": 18
- "2. A partilha afro-asiática e as respostas locais": 18
- "3. Descolonização, Bandung e não alinhamento": 20
- "4. Expansão dos Estados Unidos e formação das Américas": 20
- "5. Doutrina Monroe, pan-americanismo e integração regional": 24

Já existem (não repita):
- Um pesquisador encontra uma colônia cujo comércio externo deveria passar por agentes autor
- Na análise da resistência africana ao expansionismo europeu, uma turma estuda a vitória mi
- Em 1955, representantes de 29 países asiáticos e africanos reuniram-se na Indonésia para d
- Em um mapa do século XIX, o professor destaca uma grande área adquirida pelos Estados Unid
- Uma análise diplomática compara acordos americanos e identifica um tratado assinado no Rio
- Uma potência industrial busca controlar portos e recursos africanos no final do século XIX
- Após uma rebelião ampla em 1857, a administração territorial de vastas áreas do subcontine
- Um texto de 1960 afirma a autodeterminação dos povos e defende que o colonialismo chegue a
- Um documentário compara a liberdade de pessoas escravizadas durante a Guerra Civil estadun
- Em 1823, uma mensagem presidencial estadunidense rejeitou novas tentativas europeias de co
- Um estudante atribui a John A. Hobson a explicação de que a concentração de renda amplia a
- Um manual descreve a China do fim do século XIX como objeto de concessões e privilégios es
- Após participar de reuniões anticoloniais na década de 1950, um Estado comparece a uma cúp
- Ao comparar independências nas Américas, um historiador destaca um processo que rompeu a d
- Um pesquisador examina reuniões que antecederam a OEA e encontra uma conferência realizada
- A comparação entre colonialismo mercantil e imperialismo industrial mostra continuidades d
- Um ensaio contrapõe a experiência japonesa à indiana durante o século XIX. A primeira comb
- Após conquistar soberania em meados do século XX, um país continua dependente de financiam
- Um artigo afirma que, para certo teórico, a formação de monopólios, a fusão de interesses 
- Um livro sustenta que a Conferência de Berlim repartiu por completo a África e que, em seg
- Um participante de Bandung defendia cooperação entre países de regimes diferentes, e outro
- Diante de uma imagem de territórios que integravam o México antes da guerra de 1846–1848, 
- Ao comparar o Tratado de Montevidéu de 1960 com o de 1980, um pesquisador observa que um m
- Um estudo distingue as práticas dos Estados Unidos diante da crise de dívidas venezuelanas
- Durante a análise de instrumentos de 1947 e 1948, um texto considera equivalentes seguranç
- Ao estudar a ocupação de territórios coloniais nos séculos XVI e XVII, uma pesquisadora co
- Em uma exposição sobre a Índia, a revolta de 1857 foi apresentada como responsável pelo fi
- Ao organizar uma linha do tempo da descolonização, um grupo registrou corretamente que a C
- Em um mapa da expansão estadunidense, a compra da Louisiana, em 1803, foi corretamente ide
- Um levantamento documental observou que a Carta da OEA foi adotada em Bogotá em 1948, enqu
- Em comparação histórica, o imperialismo industrial de 1870–1914 foi descrito como processo
- Uma investigação sobre a China no século XIX distingue sua situação da Índia britânica, po
- Ao comparar as independências de Gana e da Argélia, um comentarista afirmou que ambas deco
- Uma análise da Guerra de Secessão registra que a Proclamação de Emancipação de 1863 não se
- Um pesquisador afirmou que a Doutrina Monroe, enunciada em 1823, já autorizava expressamen
- Ao estudar organizações latino-americanas, uma aluna afirmou que o Tratado de Montevidéu d
- Ao diferenciar instrumentos de Bogotá em 1948, um estudo indicou corretamente que o Pacto 
- Um comentário de história econômica distingue Hobson de Lenin ao afirmar que aquele ressal
- Uma síntese da expansão imperialista equiparou a trajetória indiana à japonesa, argumentan
- Ao interpretar a expressão Terceiro Mundo em textos da Guerra Fria, um pesquisador ressalt
- Um estudo sobre expansão territorial sustentou que a anexação do Texas em 1845 encerrou a 
- Um ensaio sobre política externa estadunidense afirma que a Doutrina Monroe de 1823 e o Co
- Uma pesquisa sobre descolonização concluiu que a Resolução 1514 (XV) das Nações Unidas, ap
- Ao comparar o Haiti de 1804 com o Brasil de 1822, uma estudante observou que as duas emanc
- Um comentário sobre o Tratado de Assunção afirmou que o MERCOSUL nasceu em 1980 com a ALAD
```

## Pedido 16: História da filosofia e ensino de filosofia (`filosofia.historia-filosofia`, 116 tópicos de edital)

```
Matéria: filosofia.historia-filosofia (História da filosofia e ensino de filosofia)
Capítulos e quantas questões quero em cada um (total 100):
- "Filosofia antiga: da phýsis à vida boa": 17
- "Filosofia medieval e moderna: fé, razão e sujeito": 17
- "Idealismo, materialismo e crítica dos valores": 17
- "Filosofias contemporâneas: experiência, linguagem e sociedade": 17
- "Ética, política, estética, comunicação e técnica": 16
- "Ensino de Filosofia no ensino médio: currículo e didática": 16

Já existem (não repita):
- No contexto dos pensadores de Mileto, o termo arkhé é empregado para designar principalmen
- Um professor apresenta perguntas sucessivas que levam a turma a perceber contradições em s
- Na alegoria da caverna, apresentada por Platão na República, o processo de libertação das 
- Segundo a ética de Aristóteles, o hábito e a prudência contribuem para a formação de:
- Em uma questão de concurso, o epicurismo foi caracterizado como busca da ataraxia. Esse te
- Um candidato afirma que, para Agostinho, o mal é uma substância que rivaliza com Deus de i
- Na moral kantiana, a distinção decisiva entre imperativos categóricos e hipotéticos é que:
- Os primeiros filósofos gregos investigaram a natureza e não formularam uma resposta única 
- A justa medida das virtudes éticas aristotélicas é necessariamente a média aritmética entr
- No estoicismo, a virtude é central e a distinção entre o que depende de nossas ações e o q
- Na filosofia medieval, os debates sobre fé e razão desenvolveram-se exclusivamente entre a
- A expressão Deus sive Natura em Espinosa está associada à concepção de substância única, n
- Uma discussão ética pode questionar uma prática legalmente permitida, pois legalidade e ju
- A Lei nº 14.945/2024 passou a explicitar Filosofia na área de Ciências Humanas e Sociais A
- Um edital pede que o candidato evite uma leitura mecânica da dialética de Hegel. Qual inte
- Em um texto sobre O Capital, um produto aparece como portador de valor independente das re
- A genealogia nietzschiana da moral permite investigar valores ao:
- Considere uma escola que utiliza registros constantes, vigilância, notas e classificação p
- Em uma leitura introdutória de Husserl, a palavra intencionalidade deve ser entendida como
- Um grupo de pesquisa analisa como entretenimento padronizado, publicidade e mercado influe
- Ao examinar a expressão de uma promessa em uma reunião, a filosofia dos atos de fala ressa
- A dúvida metódica cartesiana pretende instaurar uma investigação rigorosa dos fundamentos,
- O positivismo de Comte e o positivismo lógico do Círculo de Viena designam uma mesma escol
- Na perspectiva sartreana, a má-fé envolve o autoengano pelo qual o sujeito tenta escapar d
- A concepção foucaultiana de poder o define exclusivamente como capacidade repressiva do Es
- A morte de Deus em Nietzsche pode ser compreendida como diagnóstico de crise dos fundament
- Uma ética da responsabilidade inspirada em Hans Jonas exige considerar os efeitos distante
- Uma sequência didática interdisciplinar pode relacionar Filosofia, História e Sociologia s
- Uma assembleia decide uma regra coletiva apenas somando preferências privadas dos presente
- Uma empresa propõe enganar clientes porque a prática aumentaria as vendas, mas impede que 
- Diante da frase de que o ser humano é um sujeito isolado que depois passa a relacionar-se 
- Um enunciado reúne Comte, positivismo lógico e fenomenologia como sinônimos porque todos f
- Em uma aula sobre justiça distributiva, a professora pede que os alunos escolham princípio
- Uma equipe conclui que a reprodução digital de uma obra a transforma necessariamente em al
- Uma rede de ensino diz que a redação da Lei nº 14.945/2024 obriga todas as escolas brasile
- Na República, a defesa platônica de formação rigorosa dos governantes implica que qualquer
- O princípio de parcimônia associado a Ockham demonstra logicamente que, em qualquer disput
- O materialismo histórico de Marx pode investigar a eficácia própria de relações políticas 
- Na ética discursiva habermasiana, a validade de uma norma independe de qualquer possibilid
- A epoché fenomenológica pode ser entendida como suspensão metodológica de pressuposições d
- A valorização da Filosofia no Brasil e sua difusão por mídias digitais autorizam dispensar
- Ao planejar Filosofia no ensino médio, o docente deve considerar que a redação do art. 35-
```

## Pedido 17: Atividade de Inteligência e Sistema Brasileiro de Inteligência (`seguranca-publica.atividade-inteligencia-sisbin`, 115 tópicos de edital)

```
Matéria: seguranca-publica.atividade-inteligencia-sisbin (Atividade de Inteligência e Sistema Brasileiro de Inteligência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, funções e ramos da inteligência": 16
- "2. Produção do conhecimento, operações e contrainteligência": 12
- "3. Política e Estratégia Nacional de Inteligência": 32
- "4. SISBIN, ABIN, organização e inteligência de segurança pública": 17
- "5. Controles, accountability e questões avançadas": 23

Já existem (não repita):
- Uma unidade reúne registros de ameaças a serviços essenciais e prepara análise destinada a
- Uma equipe propõe medidas para evitar que um serviço estrangeiro obtenha arquivos sensívei
- Ao classificar os componentes fundamentais da atividade, uma equipe menciona inteligência,
- A análise de determinada ameaça chegou à autoridade somente depois de encerrada a decisão 
- Uma unidade afirma que o ato que instituiu o SISBIN também criou seu órgão central. Quais 
- Uma assessoria revisa relatório sobre a organização atual do SISBIN. Qual ato deve embasar
- Um analista classifica os fundamentos expressos do SISBIN ao examinar um programa público.
- Uma unidade produziu conhecimento sensível e precisa identificar quem exerce o controle pa
- Ao atualizar material de ensino, um instrutor procura o decreto que fixou a Política Nacio
- Um órgão debate a aprovação da Estratégia Nacional de Inteligência. Qual é a identificação
- Uma equipe recebeu solicitação de avaliação futura sobre interrupções recorrentes de servi
- Durante o ciclo de produção do conhecimento, a equipe examina a credibilidade de relatos r
- Uma estrutura federal com unidade especializada em inteligência integra o SISBIN para trat
- A direção de um órgão solicita acesso generalizado a registros mantidos por outro integran
- O Conselho Consultivo do SISBIN avaliará relatório de gestão e proposta de atualização da 
- Um memorando lista como pressupostos da PNI obediência às leis, atuação permanente e condu
- Um relatório demonstra que pessoa estrangeira tentou obter projeto tecnológico protegido, 
- Um parecer utiliza o Decreto nº 4.376/2002 como fundamento atual para classificação de int
- O dirigente de uma unidade estadual pede integração ao SISBIN. Considerando o Decreto nº 1
- Um órgão associado deseja ser reclassificado como dedicado no SISBIN. De acordo com o Decr
- O Conselho Consultivo do SISBIN fará reunião ordinária e seu presidente pede confirmação d
- Uma revisão jurídica discute acesso da CCAI a instalação sigilosa e preservação de documen
- Um assessor propõe invocar o art. 2º do Decreto nº 8.793/2016 para sustentar a coordenação
- Um setor apresenta ao usuário lista de eventos passados, sem formular inferências causais 
- Em uma operação de proteção de dados, o coordenador deseja documentar etapas para permitir
- Em situação de ameaça a uma instalação estratégica, a equipe produz relatório analítico pa
- Uma equipe afirma que operações de inteligência constituem um terceiro ramo fundamental, a
- Ao planejar medidas de proteção contra espionagem dirigida a documentos sensíveis, uma uni
- Ao preparar uma apostila atualizada, um instrutor indica o Decreto nº 4.376/2002 como o di
- Diante de proposta de modernização do SISBIN, uma equipe identifica a ABIN como órgão cent
- Na produção de conhecimento para o Executivo, o caráter sigiloso da atividade permite afas
- Em uma avaliação de riscos internos e externos, analistas incluem ataques cibernéticos, sa
- Uma unidade apresenta a ENINT como instrumento que fixa, por si só, os limites jurídicos d
- Após reunir informações, uma equipe verifica a credibilidade das fontes, interpreta os ele
- Uma unidade estadual que ingressa no SISBIN passa automaticamente a integrar a administraç
- O pedido de ingresso no SISBIN é avaliado considerando atribuições institucionais, sensibi
- Quando um órgão integrante pede acesso a todo o banco de dados de outro participante, a vi
- Um centro integrado do SISBIN reúne participantes de diferentes áreas para cooperação e co
- Uma comissão parlamentar que examina o funcionamento da inteligência fica proibida de aces
- No Conselho Consultivo do SISBIN, a presidência cabe à Casa Civil e a secretaria-executiva
- No procedimento de admissão de integrante federal, o art. 8º, § 2º, do Decreto nº 11.693/2
- Uma unidade propõe usar a edição de 2016 do regulamento estrutural da ABIN como regra vige
- Uma nota administrativa informa que a Lei nº 11.776/2008 criou somente as carreiras de Ofi
- Um núcleo de inteligência de segurança pública elabora análise de redes criminosas para au
- Em uma proposta de mudança da PNI, a equipe afirma que a CCAI poderá fixar a redação final
```

## Pedido 18: Estabilidade e garantias provisórias de emprego (`direito-trabalho.estabilidade-garantias`, 115 tópicos de edital)

```
Matéria: direito-trabalho.estabilidade-garantias (Estabilidade e garantias provisórias de emprego)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, estabilidade decenal, direito adquirido e falta grave": 19
- "2. Dirigente sindical e representante eleito da CIPA": 19
- "3. Acidente de trabalho e Comissão de Conciliação Prévia": 21
- "4. Dispensa protegida, inquérito, reintegração e indenização": 28
- "5. Força maior: requisitos, extinção, indenizações e Constituição": 13

Já existem (não repita):
- Um trabalhador foi admitido em 2010 e continua empregado na mesma empresa há dezesseis ano
- Uma empregada havia completado os requisitos da estabilidade decenal antes de 5 de outubro
- Um empregado protegido pela estabilidade decenal histórica é suspenso por acusação de falt
- Uma pessoa registra candidatura válida à direção sindical durante o contrato e depois é el
- Uma associação sindical possui doze integrantes na direção e doze suplentes. Para a estabi
- Em uma eleição interna de CIPA, uma trabalhadora foi escolhida como suplente da representa
- Um cipeiro eleito questiona judicialmente a dispensa, cuja motivação foi indicada como fin
- Depois da cessação de benefício por incapacidade temporária acidentário, um segurado pergu
- Uma Comissão de Conciliação Prévia empresarial elege representantes dos empregados. Pelo a
- Um dirigente sindical eleito é acusado de ato de improbidade e o empregador pretende efeti
- Um empregado somente registrou candidatura sindical durante seu aviso-prévio indenizado. Q
- Após a extinção do vínculo, exame e prova técnica demonstram doença ocupacional com nexo c
- Uma trabalhadora cumpria contrato de experiência e sofreu acidente laboral que reuniu os p
- Uma CCP instituída no âmbito da empresa terá oito membros titulares. Qual composição atend
- Um estabelecimento é efetivamente encerrado, e o empregado eleito membro da CIPA pede inde
- Uma garantia provisória foi violada na dispensa, mas o período protegido terminou antes do
- Um trabalhador propõe ação indenizatória somente após terminado o período de garantia de e
- Uma empresa invoca exclusivamente o art. 503 da CLT e anuncia corte salarial unilateral de
- A administração de uma empresa alega força maior depois de perda previsível causada por ma
- Um trabalhador com estabilidade decenal histórica perde seu emprego porque força maior com
- Um empregado sem estabilidade é despedido porque evento de força maior comprovado provocou
- Contrato a prazo determinado termina antes do vencimento porque força maior comprovada ext
- A empresa declarou falsa força maior para pagar indenizações reduzidas. Depois ficou prova
- Uma relação envolvendo empregado historicamente estável apresenta incompatibilidade grave 
- Um dirigente eleito pertence a categoria profissional diferenciada, mas trabalha na empres
- A estabilidade decenal preservada pelo art. 14 da Lei nº 8.036/1990 depende de direito já 
- A garantia de emprego do dirigente sindical nasce com a simples filiação do trabalhador a 
- Na estabilidade de integrante eleito da CIPA, a proteção constitucional termina no último 
- Uma trabalhadora recebeu benefício por incapacidade temporária acidentário e retornou ao t
- Na Comissão de Conciliação Prévia constituída no âmbito da empresa, o art. 625-B determina
- O empregador pode reduzir unilateralmente até 25% dos salários, sem acordo ou convenção co
- Um dirigente sindical é dispensado por falta grave reconhecida exclusivamente por investig
- Uma CIPA continua funcionando no estabelecimento, e integrante eleito é despedido por moti
- Para doença ocupacional reconhecida após a extinção do contrato, o Tema 125 do TST exige c
- A garantia acidentária do art. 118 da Lei nº 8.213/1991 pode alcançar empregados contratad
- Na Comissão de Conciliação Prévia empresarial, os representantes indicados pelo empregador
- Se o período de estabilidade provisória terminou antes da sentença, a Súmula 396, I, orien
- A OJ 399 da SBDI-1 afasta a conclusão de abuso do direito de ação pelo mero ajuizamento ap
- Uma empresa mantém o estabelecimento em atividade, mas demite o suplente eleito da CIPA e 
- O art. 501 da CLT exclui força maior decorrente de imprevidência do empregador e condicion
- O art. 502 da CLT permite pagar metade da indenização a empregado não estável quando a for
- Na extinção efetiva de estabelecimento por força maior comprovada, o art. 502, III, estabe
- O art. 503 da CLT, isoladamente, autoriza o empregador a reduzir por decisão própria em at
- Comprovada a falsa alegação de força maior, o art. 504 da CLT mantém a redução indenizatór
- O art. 497 da CLT prevê indenização em dobro ao empregado com estabilidade decenal históri
```

## Pedido 19: Dissídio individual: reclamação, notificação e valor da causa (`processo-trabalho.dissidio-individual`, 115 tópicos de edital)

```
Matéria: processo-trabalho.dissidio-individual (Dissídio individual: reclamação, notificação e valor da causa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Dissídio individual, distribuição e formas de reclamação": 23
- "2. Petição inicial escrita, pedidos e correção de defeitos": 15
- "3. Legitimidade para reclamar e jus postulandi": 22
- "4. Notificação, ciência das partes e comunicação processual": 22
- "5. Valor da causa, alçada e limite econômico do sumaríssimo": 18

Já existem (não repita):
- Uma pessoa pretende iniciar dissídio individual e pergunta se poderá expor sua pretensão o
- Uma reclamação individual é anunciada verbalmente e ainda não foi transcrita. Qual deve se
- Uma petição inicial escrita narra fatos e inclui pretensões salariais. Em relação ao conte
- A secretaria recebe e protocola uma reclamação e prepara a comunicação inicial ao reclamad
- Uma notificação é postada no endereço indicado e o reclamado alega desconhecimento sem apr
- Um trabalhador deseja formular pessoalmente reclamação em primeiro grau, sem representante
- Três empregados de um mesmo estabelecimento propõem reclamações sobre idêntico critério de
- Uma reclamação individual possui valor equivalente a 35 salários mínimos da data de ajuiza
- Uma ação tem valor de exatamente dois salários mínimos da data de ajuizamento. Segundo a L
- Uma petição escrita apresenta dois pedidos com valor e um terceiro sem qualquer delimitaçã
- Após a distribuição de uma reclamação verbal, a pessoa não comparece em cinco dias para re
- Depois de apresentada contestação por meio eletrônico, o reclamante deseja desistir unilat
- Em reclamação sujeita ao procedimento sumaríssimo, o endereço informado para o reclamado n
- Uma petição acumula três pedidos autônomos com valores de R$ 4.000, R$ 2.000 e R$ 3.000. S
- Uma inicial carece de documento indispensável e o defeito admite correção. Conforme a reda
- Uma reclamação especifica o objeto de cada pedido e apresenta valores como estimativas. Po
- Um sindicato propõe demanda em nome próprio na defesa de direitos individuais de integrant
- Em reclamação escrita, a secretaria remete a notificação 48 horas após o protocolo; a cart
- Um dissídio individual de 25 salários mínimos é ajuizado contra uma autarquia. Embora o va
- Uma questão transcreve o art. 852-B, I, e pede compará-lo literalmente com o art. 840, § 1
- Notificação postal foi enviada ao endereço empresarial informado nos autos. O reclamado af
- Após distribuição de reclamação verbal, a parte não consegue comparecer em cinco dias por 
- Uma causa ajuizada com valor equivalente a dois salários mínimos envolve discussão constit
- Um advogado ajuíza reclamação em nome de trabalhador e depois afirma ser ele próprio o tit
- Duas reclamações são ajuizadas no mesmo dia: uma, de 41 salários mínimos, entre particular
- Um empregado apresenta reclamação verbal, comparece para reduzi-la a termo dentro de cinco
- Três empregados de um mesmo estabelecimento questionam, em reclamações distintas, a aplica
- Depois da distribuição da reclamação verbal, o reclamante fica sem comparecer por sete dia
- Um trabalhador comparece pessoalmente para apresentar reclamação perante a instância de pr
- Uma causa trabalhista é ajuizada com valor igual a cinquenta salários mínimos. Julgue: ape
- Uma reclamação escrita é protocolada e a secretaria organiza o envio da segunda via ao rec
- Uma petição traz pedidos certos com valores individuais estimados. Julgue: se o valor apur
- Um sindicato propõe demanda na defesa de integrantes da categoria, invocando sua legitimaç
- A comunicação do reclamado é feita por registro postal no procedimento geral. Julgue: a Sú
- O reclamante deseja desistir da demanda depois de apresentada contestação eletrônica. Julg
- Uma reclamação está enquadrada no rito sumaríssimo e o autor errou o endereço do reclamado
- Uma reclamação com três pedidos cumulados de R$ 1.000, R$ 2.000 e R$ 4.000 atribui R$ 7.00
- Em processo com valor equivalente a exatamente dois salários mínimos na data de ajuizament
- Uma petição escrita não contém documento indispensável e a irregularidade é sanável. Julgu
- Uma demanda de 25 salários mínimos é ajuizada contra entidade autárquica, e o autor afirma
- Um reclamante ajuíza dois pedidos individualizados com valores estimados e mais um pedido 
- Um trabalhador atua pessoalmente em primeiro grau e deseja posteriormente interpor recurso
- Uma pessoa apresenta verbalmente sua reclamação e, após a distribuição, deixa de comparece
- Após o reclamado apresentar contestação eletrônica, o autor comunica desistência unilatera
- Uma controvérsia individual envolve duas pessoas do mesmo estabelecimento, com pedidos fun
```

## Pedido 20: Endodontia (`odontologia.endodontia`, 112 tópicos de edital)

```
Matéria: odontologia.endodontia (Endodontia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Complexo dentinopulpar, microbiologia e diagnóstico endodôntico": 19
- "2. Anatomia interna, acesso, odontometria e terapias pulpares vitais": 19
- "3. Preparo químico-mecânico, medicação e obturação radicular": 17
- "4. Insucessos, retratamento, acidentes e cirurgia parendodôntica": 18
- "5. Traumatismos, farmacologia, clareamento interno e restauração": 27

Já existem (não repita):
- Uma restauração profunda foi realizada em molar cuja polpa reage à agressão produzindo den
- Em consulta, paciente relata dor ao tomar bebida gelada, que desaparece poucos segundos ap
- Um pré-molar doloroso à mastigação apresenta sensibilidade à percussão vertical; a radiogr
- Após tratamento endodôntico de primeiro molar superior, persistem sintomas e imagem sugest
- Ao realizar abertura coronária antes de instrumentar os canais, qual conduta simultaneamen
- Durante a limpeza do canal de dente necrosado, o profissional pretende empregar uma soluçã
- Depois de instrumentar um canal, há smear layer composta por resíduos orgânicos e inorgâni
- Após preparar e controlar a infecção de canais, o profissional opta por guta-percha combin
- Uma criança perdeu completamente um incisivo decíduo após queda e trouxe o dente em recipi
- Um incisivo permanente apresenta ausência de resposta ao frio e ao teste elétrico, sem dor
- Um molar responde ao frio com dor intensa que permanece após retirar o estímulo, além de s
- Incisivo permanente de ápice aberto sofreu exposição pulpar traumática recente; há sensibi
- Após radiografias periapicais em diferentes angulações, permanece dúvida importante sobre 
- Durante preparo de um canal curvo, a radiografia sugere forame deslocado em relação ao ápi
- Um canal apresenta forame amplo e maior risco de saída de irrigante para o periápice. Qual
- Dente com necrose e exsudação apical persistente permanece difícil de secar na primeira se
- Após alguns anos, um molar tratado apresenta nova lesão periapical, restauração infiltrada
- Dente com canal previamente bem obturado apresenta lesão apical persistente e pino longo c
- No exame de incisivo inferior que será tratado, a radiografia mostra imagem de um canal ún
- Uma lima de NiTi fratura na curva apical de canal com limpeza coronária já realizada. Qual
- Ao irrigar um incisivo com ápice aberto, ocorre dor súbita intensa, edema crescente e sang
- Um molar retratado há quatro meses apresenta ausência de sintomas e redução progressiva da
- Incisivo permanente com ápice fechado sofreu avulsão e foi reimplantado rapidamente; não e
- Incisivo permanente imaturo de ápice aberto foi avulsionado e reimplantado cedo; nos prime
- Incisivo tratado endodonticamente apresenta escurecimento sem evidência atual de infecção 
- Após uma restauração profunda, o dentista explica que a resposta pulpar pode envolver depo
- Após uma pancada em incisivo permanente, a resposta ao frio torna-se negativa no dia segui
- Um molar superior continua com lesão apical após tratamento prévio. A hipótese de um segun
- Durante a abertura coronária para pulpectomia, o isolamento com algodão e sucção de alta p
- Durante o preparo de canal necrosado, a irrigação com hipoclorito de sódio contribui tanto
- Após a instrumentação, o operador escolhe EDTA para dissolver restos de polpa orgânica no 
- Em incisivo permanente imaturo com exposição pulpar recente e tecido remanescente clinicam
- Na avaliação de um dente permanente com cárie profunda e exposição pulpar controlada, a ex
- Havendo dúvida clinicamente importante sobre reabsorção radicular que não se resolve em ra
- Durante odontometria de raiz curvada, a extremidade radiográfica da raiz coincide necessar
- Em canal infectado que não permite obturação adequada por exsudação persistente, o uso sel
- Em retratamento de molar com biofilme persistente, a colocação de guta-percha e cimento no
- Um molar com obturação radiograficamente satisfatória pode ser reinfectado se uma restaura
- Quatro meses após retratamento, a radiolucidez apical de um dente sem dor diminuiu, mas nã
- Em retratamento, uma perfuração detectada precocemente deve ser avaliada quanto à localiza
- Após avulsão de incisivo decíduo, a integridade da raiz e o tempo extra-alveolar inferior 
- Um incisivo permanente maduro avulsionado foi prontamente reimplantado sem fratura alveola
- Uma pessoa adulta imunocompetente apresenta pulpite irreversível sintomática, sem febre, m
- Ao realizar clareamento interno de um incisivo escurecido e corretamente tratado, a coloca
- Em molar extensamente destruído após tratamento de canal, a instalação de pino intrarradic
```

## Pedido 21: Intervenção psicológica em crise, suicídio, luto e dependência química (`psicologia.intervencao-crise-suicidio-luto`, 112 tópicos de edital)

```
Matéria: psicologia.intervencao-crise-suicidio-luto (Intervenção psicológica em crise, suicídio, luto e dependência química)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Crise psíquica, primeiros socorros psicológicos e emergências": 22
- "2. Avaliação do suicídio, prevenção e posvenção": 18
- "3. Trauma, estresse pós-traumático e luto": 21
- "4. Substâncias, recaída e comportamentos digitais": 25
- "5. Prevenção, classificação e organização do cuidado": 14

Já existem (não repita):
- Após enchente, uma pessoa está orientada, chora e relata que perdeu documentos. Não há ind
- Uma equipe de resposta a desastre adota os princípios observar, escutar e conectar. Em qua
- Uma pessoa em crise menciona pensamentos de morte. Na avaliação de segurança, como a equip
- Uma escola oferece apoio psicossocial e encaminhamento a estudantes após morte por suicídi
- Uma mulher enlutada manifesta tristeza e raiva alternadas, sem passar por uma sequência pr
- Uma equipe atende homem que retomou o padrão de consumo prejudicial de álcool após longo p
- Um adolescente joga videogame por várias horas nas férias, mas mantém interesses, relações
- Uma comunidade oferece a todos os moradores oficinas de promoção de vínculos e estratégias
- Em uma unidade de saúde, um trabalhador pede orientação porque familiar com transtorno men
- Após acidente de trabalho, uma pessoa passa dias angustiada, com insônia e sobressaltos. Q
- Uma profissional em atendimento de risco identifica ideação suicida e recebe pedido de seg
- Após violência coletiva, um grupo deseja obrigar todas as pessoas expostas a reconstruir d
- Ao planejar prevenção do suicídio, uma equipe propõe reduzir acesso a meios de alta letali
- Na CID-11, uma pessoa apresenta reexperiência do trauma no presente, evitação de lembrança
- Uma pessoa em recuperação de uso prejudicial de álcool consumiu bebida em evento isolado e
- Uma rede acompanha pessoa com problemas persistentes por álcool mediante atenção primária 
- Uma campanha preventiva destina-se a adolescentes expostos a desastre, considerados mais v
- Paciente em possível abstinência alcoólica apresenta confusão acentuada e episódio convuls
- Pessoa perdeu familiar oito meses atrás, apresenta saudade persistente e importante dificu
- A alta de pessoa após tentativa de suicídio é preparada apenas com pontuação baixa em esca
- A equipe avalia duas pessoas: uma com resposta pós-trauma e outra com ansiedade persistent
- No mesmo projeto de saúde mental há ações para todos os moradores, para sobreviventes de e
- Uma equipe propõe internação psiquiátrica involuntária sem relatório médico, embora exista
- Na organização da notificação prevista pela Lei 13.819/2019, uma instituição defende que a
- Uma paciente mantém sofrimento físico persistente, sem explicação suficiente na avaliação 
- Após um desastre, oferecer informações simples sobre abrigo e facilitar contato com pessoa
- Durante atendimento inicial, uma pessoa angustiada deve ser obrigada a narrar pormenores d
- Perguntar respeitosamente sobre ideação e planos suicidas pode integrar avaliação clínica 
- As cinco categorias de Kübler-Ross são etapas obrigatórias e sequenciais; o luto sem barga
- Uma proposta de prevenção primária universal pode oferecer atividades de promoção da saúde
- Uma tentativa de suicídio recente pode requerer acompanhamento mesmo depois de estabilizaç
- Na CID-11, o código 6C51 designa transtorno por jogos digitais e pode ser atribuído apenas
- O cuidado de pessoa com uso prejudicial de álcool pode articular atenção primária, CAPS AD
- A diferença entre prevenção seletiva e indicada reside no número de profissionais envolvid
- O transtorno de estresse pós-traumático na CID-11 contempla reexperiência no presente, evi
- Na Lei 13.819/2019, a automutilação sem ideação suicida fica fora da definição de violênci
- Na abordagem de pessoa com consumo problemático de álcool, redução de danos pode integrar 
- Uma crise psíquica após notícia de diagnóstico grave constitui, por si só, razão suficient
- Pessoa com sofrimento de luto persistente, prejuízo acentuado e expressão cultural de luto
- Ao oferecer posvenção em uma equipe após suicídio, a instituição deve exigir relatos públi
- A comunicação de internação psiquiátrica involuntária ao Ministério Público Estadual previ
- Uma pessoa com exposição prolongada a trauma pode apresentar sintomas de TEPT e dificuldad
- Após uma tentativa de suicídio, a obtenção de escore baixo em instrumento padronizado torn
- Na classificação de prevenção por público-alvo, intervenção indicada pode visar pessoas co
- Uma pessoa mantém controle prejudicado e perdas importantes associadas a jogos digitais, m
```

## Pedido 22: Direitos constitucionais dos trabalhadores (art. 7º) (`direito-trabalho.direitos-constitucionais-trabalhadores`, 112 tópicos de edital)

```
Matéria: direito-trabalho.direitos-constitucionais-trabalhadores (Direitos constitucionais dos trabalhadores (art. 7º))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direitos fundamentais trabalhistas: emprego, salário e participação": 22
- "2. Jornada, repouso, férias, família e aviso prévio": 25
- "3. Saúde, igualdade, trabalho de menores e prescrição": 19
- "4. Estabilidade do ADCT e negociação coletiva constitucional": 17
- "5. Trabalhadores domésticos: extensão de direitos e casos integrados": 17

Já existem (não repita):
- Um trabalhador urbano é dispensado sem justa causa e pergunta qual é a base da indenização
- Empregada recebe apenas comissões e em determinado mês elas não atingem o mínimo legal. Qu
- Um trabalhador cumpre a jornada ordinária constitucional e pretende saber o limite diário 
- Um empregado realizou serviço extraordinário em jornada comum. Qual acréscimo remuneratóri
- Uma empresa pretende conceder férias anuais remuneradas e pergunta qual adicional mínimo d
- Uma trabalhadora planeja o afastamento por maternidade e quer identificar a duração previs
- Adolescente de quinze anos pretende ingressar no mercado de trabalho e recebe proposta reg
- Uma família contrata cuidadora pessoal, remunerada, subordinada e sem finalidade lucrativa
- Uma empregada foi dispensada sem justa causa já grávida, embora a gravidez só tenha sido d
- O suplente eleito de comissão interna de prevenção de acidentes sofre dispensa arbitrária 
- Um empregado recebe adicional de periculosidade e deseja aposentadoria especial apenas por
- Empregado é dispensado sem justa causa e ajuíza ação por créditos contratuais dois anos e 
- Uma família dispensa empregada doméstica sem justa causa e pretende recolher multa tradici
- Uma pessoa contratada para cuidar de criança em residência trabalha regularmente no períod
- Uma convenção coletiva estabelece limitação de direito patrimonial legalmente negociável e
- Um empregado de longa permanência será desligado com aviso prévio proporcional. Qual exten
- Uma empregada foi dispensada na gravidez e busca reintegração somente quando já decorreram
- Um empregado eleito como suplente da CIPA foi despedido sem justa causa seis meses após o 
- Em outubro de 2026, trabalhador pergunta quando a Lei 15.371/2026 ampliará a licença-pater
- Após decisão da ADI 6309 em junho de 2026, alguém afirma que o adicional de insalubridade 
- Uma trabalhadora de dezessete anos é selecionada para atividade insalubre, e a empresa arg
- Uma empresa pretende aplicar regra negociada de redução salarial e sustenta que basta acor
- Uma empregada doméstica trabalha à noite na residência e pergunta se sua hora noturna corr
- Uma trabalhadora de baixa renda possui dependente apto à proteção legal e necessita de vag
- Um acordo coletivo limita parcela legalmente disponível sem explicitar contrapartida, mas 
- Uma empregada recebe remuneração variável inferior ao mínimo em determinado mês. O art. 7º
- Em proposta de acordo individual, o empregador pretende reduzir o salário contratual de to
- Um empregado que realiza horas extras deve receber adicional mínimo de cinquenta por cento
- A Constituição impõe repouso semanal remunerado invariavelmente aos domingos e não admite 
- Um jovem de quinze anos ingressa como aprendiz em atividade diurna e não insalubre; em pri
- Uma trabalhadora doméstica comparece a residência da mesma família por exatamente dois dia
- Uma empregada gestante pode ter direito à licença de 120 dias e à estabilidade até cinco m
- Uma empregada descobriu após a despedida sem justa causa que estava grávida antes da resci
- Um trabalhador ajuíza ação dentro de dois anos do fim do contrato e pleiteia créditos dos 
- Um empregado recebe adicional de periculosidade e afirma ter direito automático à aposenta
- Um empregado eleito como suplente da CIPA está no curso do mandato. A Súmula 339 do TST re
- Em 2026, o regime geral de licença-paternidade já garante automaticamente vinte dias a tod
- A LC 150/2015 prevê recolhimento mensal doméstico de 3,2% para compensação pela perda do e
- Uma convenção coletiva limita direito patrimonial negociável sem contrapartida específica 
- Em junho de 2026, o STF afastou na ADI 6309 a idade mínima do art. 19, §1º, I, da EC 103/2
- A gestante procura o Judiciário após terminar o período de estabilidade e, segundo a Súmul
- O art. 7º, XXVIII, assegura seguro contra acidentes do trabalho a cargo do empregador sem 
- A empregadora pode contratar como doméstico um aprendiz de dezesseis anos para atividade d
- A licença-maternidade constitucional de 120 dias e a estabilidade gestacional até cinco me
- A assistência gratuita a filhos e dependentes em creches e pré-escolas do art. 7º, XXV, só
```

## Pedido 23: Defensoria Pública: tutela coletiva e grupos vulneráveis (`direito.defensoria-tutela-vulneraveis`, 112 tópicos de edital)

```
Matéria: direito.defensoria-tutela-vulneraveis (Defensoria Pública: tutela coletiva e grupos vulneráveis)
Capítulos e quantas questões quero em cada um (total 100):
- "Acesso à justiça, ondas renovatórias e vulnerabilidade": 17
- "Tutela coletiva e instrumentos de atuação da Defensoria": 17
- "Proteção integral de crianças e adolescentes e direitos das mulheres": 17
- "População LGBTQIA+, pessoas em situação de rua e violência institucional": 17
- "Migrantes, refugiados e execução penal": 16
- "Regras de Brasília e sistema interamericano de direitos humanos": 16

Já existem (não repita):
- O acesso material à justiça pode não se realizar mesmo quando a pessoa consegue distribuir
- A segunda onda renovatória do acesso à justiça, na classificação clássica de Cappelletti e
- A terceira onda de Cappelletti e Garth considera a adequação de instituições, procedimento
- Assegurar o mesmo formulário exclusivamente digital a toda a população significa, por defi
- A legitimidade da Defensoria Pública para propor ação civil pública encontra previsão expr
- A ação civil pública da Defensoria somente é cabível se todos os integrantes do grupo bene
- Direitos individuais homogêneos são necessariamente indivisíveis e pertencem a titulares i
- A legitimidade para ajuizar ação civil pública autoriza automaticamente a Defensoria Públi
- A doutrina da proteção integral compreende crianças e adolescentes como sujeitos de direit
- O art. 141 do ECA assegura acesso de crianças e adolescentes à Defensoria Pública, ao Mini
- A Lei Maria da Penha prevê assistência jurídica exclusivamente após a ação penal, vedando 
- O respeito ao nome social e à privacidade é compatível com o atendimento jurídico de pesso
- A Resolução CNJ nº 425/2021 instituiu política judicial direcionada às pessoas em situação
- No âmbito criminal, a Resolução CNJ nº 348/2020 determina que a proteção de pessoas LGBTI 
- A Lei de Migração contempla o amplo acesso à justiça e prevê assistência jurídica integral
- A entrada irregular no território brasileiro impede a formulação de pedido de reconhecimen
- A Defensoria Pública é órgão da execução penal e pode atuar, na defesa dos necessitados, e
- As Regras de Brasília, atualizadas em 2018, são recomendações de acesso à justiça em situa
- Toda pessoa que considera seu direito violado pode apresentar diretamente à Corte Interame
- Os arts. 8º e 25 da Convenção Americana relacionam-se, respectivamente, com garantias judi
- Uma comunidade rural carece de advogados e não consegue custear a propositura de demandas 
- Uma cidade já mantém atendimento jurídico gratuito, mas usuários continuam sem entender o 
- Uma defensoria atende pessoa refugiada com deficiência auditiva e dificuldades com a língu
- Diversos consumidores sofreram descontos originados da mesma cobrança irregular, mas o val
- A Defensoria ajuíza ação civil pública por ausência de água potável em escola frequentada 
- A Defensoria apura falhas reiteradas de atendimento de vítimas de tortura e pretende medid
- Sobre os instrumentos judiciais de tutela coletiva disponíveis à Defensoria, assinale a af
- Uma criança foi afastada de sua família por dificuldades econômicas, sem avaliação de alte
- Segundo o art. 141 do Estatuto da Criança e do Adolescente, qual enunciado descreve corret
- Uma mulher relata violência psicológica e patrimonial cometida por companheiro e busca ate
- Uma rede de escolas apresenta falhas repetidas para acolher meninas vítimas de violência, 
- Uma unidade pública de atendimento exige que uma mulher trans utilize nome diverso do soci
- Uma pessoa em situação de rua não consegue obter informação sobre processo porque o órgão 
- Uma pessoa LGBTI privada de liberdade relata ameaças. Qual encaminhamento deve orientar a 
- Migrante sem situação documental regular pede orientação sobre direitos trabalhistas e ser
- Uma pessoa ingressou irregularmente no Brasil e declara temor de perseguição caso retorne 
- Durante visita a um estabelecimento penal, a Defensoria constata falta coletiva de acesso 
- Um examinador afirma que as Regras de Brasília são diretamente equivalentes a uma convençã
- Uma vítima pretende submeter denúncia internacional de violações de direitos humanos ao si
- Em reclamação sobre impossibilidade de defesa e inexistência de recurso efetivo contra vio
```

## Pedido 24: Protesto de títulos e outros documentos de dívida (Lei 9.492/1997) (`direito-notarial-registral.protesto-titulos-lei-9492`, 111 tópicos de edital)

```
Matéria: direito-notarial-registral.protesto-titulos-lei-9492 (Protesto de títulos e outros documentos de dívida (Lei 9.492/1997))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, competência, distribuição e apresentação": 25
- "2. Prazo, intimação, desistência, sustação e pagamento": 25
- "3. Registro, espécies de protesto, averbação e cancelamento": 25
- "4. Certidões, livros, emolumentos e responsabilidade": 25

Já existem (não repita):
- Segundo a Lei 9.492/1997, protesto é o ato formal e solene pelo qual se prova a inadimplên
- As certidões de dívida ativa da União, dos Estados, do Distrito Federal, dos Municípios e 
- Ao examinar um título apresentado para protesto, o tabelião verifica que ele está prescrit
- Os documentos apresentados ou distribuídos no horário regulamentar devem ser protocolizado
- A distribuição prévia dos títulos e documentos destinados a protesto é obrigatória em toda
- Tratando-se de cheque, o protesto poderá ser lavrado no lugar do pagamento ou do domicílio
- Sobre título emitido fora do Brasil, em moeda estrangeira, apresentado para protesto, é co
- Na proposta de solução negocial prévia ao protesto, enviada pela central nacional dos tabe
- As dúvidas do tabelião de protesto são resolvidas pelo juízo competente.
- Pela Lei 9.492/1997, o protesto será registrado dentro de:
- A intimação do devedor considera-se cumprida quando comprovada a sua entrega no endereço f
- Segundo o art. 15 da Lei 9.492/1997, a intimação será feita por edital quando o devedor:
- Enviada a intimação por meio eletrônico, se em três dias úteis não houver comprovação de r
- Quem fornecer, de má-fé, endereço incorreto do devedor ao tabelionato de protesto responde
- Antes da lavratura do protesto, o apresentante:
- Revogada a ordem judicial de sustação do protesto, é necessária nova intimação do devedor 
- O título cujo protesto foi sustado judicialmente:
- O pagamento do título apresentado para protesto deve ser feito diretamente no tabelionato 
- Quando o tabelionato aceita o pagamento do título por meio de cheque, a quitação dada:
- Segundo a Lei 9.492/1997, o protesto será tirado por falta de:
- O protesto por falta de aceite pode ser efetuado a qualquer tempo, antes ou depois do venc
- Não se pode tirar protesto por falta de pagamento de letra de câmbio contra o sacado não a
- Quando o sacado retém a duplicata enviada para aceite e não a devolve no prazo legal, o pr
- Somente poderão ser protestados para fins falimentares os títulos ou documentos de dívida 
- Não havendo prazo assinado, o termo inicial da incidência de juros, taxas e atualizações m
- A averbação de retificação de erros materiais no registro do protesto pode ser feita de of
- Quanto ao cancelamento do registro do protesto, assinale a afirmativa correta.
- O cancelamento do registro do protesto fundado em motivo diverso do pagamento do título se
- No protesto em que figurou apresentante por endosso-mandato, o cancelamento sem o título o
- O deferimento do processamento de concordata (hoje, recuperação judicial) impede o protest
- As certidões solicitadas ao tabelião de protesto devem ser expedidas no prazo máximo de:
- Os registros de protesto cujos cancelamentos tiverem sido averbados constam normalmente da
- Sempre que a homonímia puder ser verificada simplesmente pelo confronto do número do docum
- A certidão diária fornecida às entidades de proteção ao crédito, com a relação dos protest
- O prazo de arquivamento é de três anos para os livros de protocolo e de dez anos para os l
- As intimações e editais correspondentes a documentos protestados e as ordens de cancelamen
- Os tabeliães de protesto respondem objetivamente, independentemente de culpa, pelos prejuí
- Entre os serviços que a central nacional de serviços eletrônicos compartilhados dos tabeli
- Os emolumentos devidos aos tabeliães de protesto são fixados por lei estadual, e o tabeliã
- Considerando a Lei 9.492/1997, o atendimento ao público nos tabelionatos de protesto deve 
- Os devedores, assim compreendidos os emitentes de notas promissórias e cheques, os sacados
```

## Pedido 25: Cooperação jurídica internacional em matéria penal (`processo-penal.cooperacao-juridica-internacional-penal`, 111 tópicos de edital)

```
Matéria: processo-penal.cooperacao-juridica-internacional-penal (Cooperação jurídica internacional em matéria penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, soberania e vias de cooperação penal": 22
- "2. Cartas rogatórias, auxílio direto e atos probatórios": 24
- "3. Convenções de Viena, Palermo, Mérida e protocolos": 19
- "4. Nassau, San Luis, equipes conjuntas e recuperação de ativos": 19
- "5. Transferências, difusão vermelha e entrega ao tribunal internacional": 16

Já existem (não repita):
- Uma investigação instaurada no Brasil precisa de documentos guardados em outro país. A aut
- Uma autoridade estrangeira pede ao Brasil que tome depoimento de pessoa residente em terri
- Uma solicitação internacional chega por meio da autoridade central brasileira. Qual provid
- Uma decisão judicial estrangeira origina carta rogatória para cumprimento no Brasil, sujei
- Após o Superior Tribunal de Justiça conceder exequatur a carta rogatória passiva, é necess
- Uma autoridade judiciária de outro país expede ordem de citação de pessoa no Brasil; para 
- Um país pede ao Brasil a obtenção de documentos relacionados a investigação, sem solicitar
- Uma parte requer expedição de carta rogatória para ouvir testemunha no exterior, sem demon
- Investigadores de dois Estados precisam colher depoimentos, obter documentos e realizar bu
- Dois países pretendem criar estrutura investigativa coordenada para apurar atuação transna
- Em investigação de tráfico internacional de entorpecentes, pretende-se fundamentar assistê
- Um caso envolve facilitação ilícita de entrada de migrantes por via marítima para obtenção
- A investigação descreve recrutamento de pessoas por fraude com finalidade de exploração em
- Autoridades pretendem rastrear componentes e munições ligados a fabricação e circulação il
- No curso de apuração de corrupção, dois Estados discutem assistência na obtenção de docume
- Uma solicitação de assistência penal parte de país integrante do sistema interamericano e 
- Uma investigação regional solicita, com base no Protocolo de San Luis, documentos sobre co
- Autoridades de países participantes do Acordo-Quadro promulgado pelo Decreto 10.452/2020 d
- Em investigação de corrupção, valores foram identificados no exterior, mas ainda não exist
- Uma solicitação penal brasileira será enviada à Alemanha após a entrada em vigor do tratad
- Um brasileiro condenado definitivamente no exterior, com vínculo no Brasil, tem 14 meses d
- Uma pessoa definitivamente condenada no Brasil deseja cumprir o restante de sua pena no Es
- Uma pessoa procurada consta em difusão vermelha da Interpol e é localizada em país diferen
- Um Estado recebe solicitação de detenção e entrega expedida pelo Tribunal Penal Internacio
- Um pedido passivo de cooperação penal chega ao Brasil sem tratado específico, e sua execuç
- Uma autoridade estrangeira solicita coleta de prova no Brasil. Como o pedido parte do exte
- Ao receber carta rogatória passiva sujeita a exequatur, a autoridade central brasileira po
- Uma equipe examina a Convenção das Nações Unidas contra o Crime Organizado Transnacional, 
- Em caso de entrada irregular de migrantes facilitada por terceiros, o protocolo promulgado
- Um pedido de restituição de ativos oriundos de corrupção deve considerar o art. 57 da Conv
- Se uma autoridade brasileira pedir a outro país a realização de perícia, o pedido será cla
- A difusão vermelha da Interpol informa a existência de pessoa procurada para localização e
- Ao solicitar, em investigação penal, qualquer documento situado no exterior, a parte reque
- No Protocolo de San Luis, um pedido de assistência documental pode ser admitido embora o f
- O Protocolo Facultativo à Convenção Interamericana de Nassau autoriza os Estados participa
- Na falta de tratado de cooperação jurídica aplicável, admite-se em princípio a utilização 
- Quando um país pede que o Brasil assuma o cumprimento de pena imposta por sentença definit
- As investigações conjuntas previstas no art. 19 de Palermo devem respeitar a soberania do 
- A Convenção de Viena de 1988 autoriza diretamente as autoridades de um Estado a exercerem,
- Na transferência da execução de pena para o Brasil, o art. 100 da Lei 13.445/2017 exige, e
- Se a pessoa condenada no Brasil aceitar sua transferência para cumprir pena no exterior, o
- A entrega de pessoa ao Tribunal Penal Internacional segue instituto distinto da extradição
- A apreensão de bens no exterior, em investigação de corrupção, provoca sua divisão obrigat
- Para pedido de cooperação jurídica penal entre Brasil e Alemanha coberto por seu tratado b
- Após comunicação policial internacional indicando investigado procurado, a mera troca de i
```

## Pedido 26: História dos direitos humanos (`direito.historia-direitos-humanos`, 111 tópicos de edital)

```
Matéria: direito.historia-direitos-humanos (História dos direitos humanos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Antecedentes históricos, limitação do poder e teorias dos direitos": 23
- "2. Declarações revolucionárias e lutas por inclusão": 23
- "3. Pós-guerra, Carta da ONU, Declaração Universal e Pactos de 1966": 18
- "4. Sistema interamericano e pluralidade de vias de proteção": 18
- "5. Constituição de 1988 e consolidação brasileira dos direitos humanos": 18

Já existem (não repita):
- Ao estudar os limites medievais ao poder monárquico, uma turma identifica documento firmad
- Uma linha do tempo coloca, em 1689, um instrumento inglês que reforçou limites ao poder da
- Um texto revolucionário de 1789 proclamou direitos de liberdade e igualdade na França. A q
- Uma pesquisa identifica o tratado fundador da organização mundial constituída após a Segun
- Uma exposição histórica precisa identificar a Declaração Universal dos Direitos Humanos. Q
- Em uma revisão de história internacional, solicita-se o ano de adoção dos dois Pactos de d
- Um quadro comparativo pergunta qual documento regional das Américas foi aprovado em 1948 a
- Um texto constitucional brasileiro apresenta a dignidade da pessoa humana como fundamento 
- Um pesquisador afirma que a DUDH, em dezembro de 1948, entrou em vigor como tratado ratifi
- Uma análise da declaração francesa de 1789 compara a linguagem universal do texto com os d
- Uma linha histórica do constitucionalismo social apresenta duas constituições frequentemen
- Uma cronologia registra datas distintas de entrada em vigor internacional dos Pactos adota
- Um material distingue os direitos cobertos pelos dois Pactos de 1966, mas procura uma disp
- Uma ficha histórica apresenta a Convenção Americana sobre Direitos Humanos e precisa disti
- Ao organizar decretos brasileiros de 1992, um pesquisador deve associar cada número ao tra
- Uma nota didática sobre a conferência mundial de 1993 pretende apresentar sua conclusão ce
- Em uma explicação da Constituição de 1988, uma estudante afirma que o rol de direitos do t
- Ao aplicar a Emenda Constitucional nº 45/2004, uma decisão examina a equivalência de trata
- Um quadro confunde a data de adesão brasileira à Convenção Americana com o momento em que 
- Um estudo sobre o nascimento dos deveres internacionais de direitos humanos sustenta que o
- Uma comparação afirma que a Declaração Americana de Direitos e Deveres do Homem é o própri
- Em um estudo dos direitos incluídos na DUDH, um resumo afirma que a proclamação de 1948 se
- Um texto defende que, por não ser tratado de ratificação, a DUDH não teve influência na cr
- Uma discussão distingue a função política de uma declaração de direitos e a exigibilidade 
- Em uma questão sobre progressividade, um estudante interpreta o PIDESC como autorização de
- A Magna Carta de 1215 integra os antecedentes da limitação do poder, embora não seja corre
- A Declaração dos Direitos do Homem e do Cidadão foi proclamada em 1791 por Olympe de Gouge
- A Carta das Nações Unidas foi assinada em 26 de junho de 1945 e entrou em vigor em 24 de o
- A Declaração Universal dos Direitos Humanos foi adotada como tratado multilateral em 10 de
- Os dois Pactos internacionais de direitos humanos foram adotados em 16 de dezembro de 1966
- A Declaração Americana dos Direitos e Deveres do Homem foi aprovada depois da Convenção Am
- Na Constituição brasileira de 1988, a dignidade da pessoa humana figura no art. 1º, III, e
- A linguagem universal da declaração francesa de 1789 correspondeu ao exercício imediato e 
- A chamada Carta Internacional dos Direitos Humanos abrange a Declaração Universal e os doi
- O art. 2º, item 1, do PIDESC prevê realização progressiva de direitos e, por esse motivo, 
- A Declaração Americana de Direitos e Deveres do Homem, aprovada em 1948, antecede a Declar
- O reconhecimento brasileiro da competência contenciosa da Corte Interamericana ocorreu aut
- O art. 5º, § 1º, da Constituição de 1988 afirma a aplicação imediata das normas definidora
- A Emenda Constitucional nº 45/2004 estabeleceu que qualquer tratado internacional de direi
- Na Convenção Americana, a passagem da afirmação de direitos a deveres estatais aparece nos
- A teoria das gerações dos direitos humanos estabelece juridicamente que a segunda dimensão
- Os Decretos brasileiros nº 591 e 592, ambos de 1992, referem-se à promulgação dos Pactos a
- No dia da promulgação da Constituição de 1988 já vigorava a redação do art. 5º, § 3º, que 
- A presença da autodeterminação dos povos no art. 1º de ambos os Pactos de 1966 demonstra q
- As constituições do México e de Weimar são associadas ao constitucionalismo social, respec
```

## Pedido 27: Acre: história, geografia e economia (`acre.historia-geografia-economia`, 111 tópicos de edital)

```
Matéria: acre.historia-geografia-economia (Acre: história, geografia e economia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ocupação, povos indígenas e economia da borracha": 21
- "2. Revolução Acreana, tratados e formação político-territorial": 17
- "3. Território, regiões, população e urbanização": 21
- "4. Natureza amazônica: relevo, clima, rios, solos e zoneamento": 16
- "5. Transformações produtivas, conflitos sociais, cultura e indicadores": 25

Já existem (não repita):
- Ao preparar uma exposição sobre a ocupação do Acre, um pesquisador afirma que os primeiros
- Um arquivo reúne notícias de migração nordestina para extrair látex entre o fim do século 
- Em um seringal, trabalhadores recebem ferramentas e alimentos a crédito, entregando borrac
- Uma linha do tempo distingue os combates conduzidos por Plácido de Castro e a formalização
- Uma turma compara as concessões pactuadas em 1903 para a incorporação do Acre. Qual conjun
- Na revisão dos limites ocidentais, o estudante identifica um tratado assinado em 8 de sete
- Em um painel cívico constam os marcos de 1903 e de 1962. Qual afirmação descreve corretame
- Uma escola pretende montar um mapa de localização do Acre e indicar seus vizinhos nacionai
- Um cadastro deve registrar a composição político-municipal e a sede do governo estadual ac
- Uma atividade pede identificar corretamente a rede hidrográfica à qual se associa o rio Ac
- Em uma revisão da Lei Complementar acreana nº 126/2003, é necessário classificar Capixaba 
- Ao confrontar mapas regionais de 1990 e de 2017, uma analista percebe recortes e denominaç
- Uma planilha deve informar a população estadual conforme o Censo 2022, sem utilizar projeç
- Após uma passagem de massa de ar frio, moradores de Rio Branco registram queda temporária 
- Um estudo territorial invoca o Zoneamento Ecológico-Econômico do Acre para avaliar estrada
- Uma ação pública busca concentrar serviços nos três municípios da Regional do Purus, nos t
- Um documento histórico informa que o Acre foi administrado inicialmente por departamentos 
- Um gráfico de mercado mostra queda das exportações de borracha amazônica nas primeiras déc
- Um projeto escolar aproxima literatura e história acreana, selecionando uma narrativa publ
- Na década de 1970, famílias seringueiras organizam barreiras humanas para impedir a derrub
- Um mapa municipal anterior à atualização de limites difere de outro que adota a redefiniçã
- Uma avaliação compara a população do Acre no Censo 2022, de 830.018 pessoas, com a de Rio 
- Um estudo associa três fenômenos: crise da borracha do início do século XX, mobilização ex
- Uma proposta de expansão de pastagem está em área de floresta com comunidades tradicionais
- Um boletim de 2023 informa que serviços, incluindo administração pública, representavam 74
- Um relatório de formação territorial apresenta o Tratado de Ayacucho, de 1867, como o inst
- Ao narrar a história do Acre, é correto afirmar que comunidades indígenas ocupavam a regiã
- Uma coleção de documentos de 1903 descreve o Acre como estado federado já dotado da condiç
- Em um mapa brasileiro, o Acre situa-se na Região Norte, confronta Peru e Bolívia e possui 
- Ao registrar os resultados do Censo 2022, uma equipe utilizou 364.756 habitantes como popu
- Um estudo sobre migração para os seringais pode distinguir a fase de grande expansão no fi
- Em seu planejamento regional, o governo pode tratar as cinco regionais da Lei Complementar
- Uma equipe de planejamento territorial considera simultaneamente solos, recursos hídricos,
- Na descrição das formas de relevo, a presença de floresta ombrófila obriga a classificar o
- Ao analisar o avanço da pecuária no Acre nas décadas de 1970 e 1980, um historiador relaci
- Um contrato de seringal do período gomífero registra adiantamento de gêneros, lançamento d
- Para aplicar a regionalização prevista na LC estadual nº 126/2003, deve-se incluir Capixab
- Uma região geográfica imediata instituída no recorte estatístico de 2017 possui autonomia 
- Com base no Censo 2022, em que o Acre contava 830.018 pessoas e Rio Branco 364.756, pode-s
- Quando uma área aparece no ZEE como favorável a determinada atividade produtiva, o mapa te
- Um plano que menciona povos Huni Kuin, Ashaninka, Jaminawa e Yawanawá e prevê ações de ges
- A floresta úmida do Acre comprova que todos os seus solos são naturalmente férteis e dispe
- No boletim estadual referente a 2023, a participação de 36,4% dos serviços não públicos, s
- O romance O empate, de Florentina Esteves, publicado em 1993, é fonte estatística oficial 
- Para reconstruir as fronteiras acreanas, é pertinente distinguir o Tratado de Ayacucho de 
```

## Pedido 28: Teorias sociológicas e ensino de sociologia (`sociologia.teorias-sociologicas-ensino`, 111 tópicos de edital)

```
Matéria: sociologia.teorias-sociologicas-ensino (Teorias sociológicas e ensino de sociologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Sociologia como ciência e teorias clássicas": 17
- "Sociologia contemporânea: Bourdieu, Giddens, Bauman e Elias": 17
- "Cultura, socialização, estratificação e desigualdades": 17
- "Trabalho, economia, organizações e produção social do espaço": 17
- "Movimentos sociais, Estado, cidadania e pensamento social brasileiro": 16
- "Sociologia da educação, BNCC e metodologias de ensino": 16

Já existem (não repita):
- Ao propor estudo de normas de pontualidade impostas independentemente da vontade de cada e
- Em uma questão sobre o predomínio de especializações complementares na sociedade moderna, 
- Uma pessoa segue determinada regra religiosa porque a considera correta, mesmo prevendo pe
- Qual das situações representa mais claramente dominação legal-racional em Weber?
- Na explicação marxiana da exploração capitalista, qual formulação de mais-valia é adequada
- No quadro de Bourdieu, um diploma universitário é exemplo de qual modalidade de capital cu
- A tese segundo a qual regras são ao mesmo tempo condições e resultados das práticas cotidi
- No vocabulário de Norbert Elias, redes mutáveis de interdependência entre pessoas constitu
- Se uma pesquisa compara a ocupação de mães e pais à ocupação de seus filhos adultos, mede 
- Quando uma prefeitura substitui atividades industriais por investimentos imobiliários e mo
- Uma cooperativa escolhe fornecedores por relações duradouras de confiança, embora o preço 
- Qual noção melhor descreve a diferenciação histórica entre direitos civis, políticos e soc
- O chamado homem cordial, em Sérgio Buarque de Holanda, refere-se principalmente à:
- Uma questão associa a exclusão econômica e racial após a abolição à crítica do mito da dem
- Um professor identifica ideias prévias dos alunos, acompanha produções e ajusta a sequênci
- Sobre a posição curricular da Sociologia no ensino médio após a Lei 14.945/2024, está corr
- Qual atividade mais se aproxima do ensino sociológico orientado por estranhamento e desnat
- Uma comunidade obedece a um líder por acreditar em capacidades extraordinárias que lhe são
- Uma indústria aumenta produtividade e diminui a parcela da jornada necessária à reprodução
- Ao afirmar que disposições adquiridas tornam certos gostos e práticas mais prováveis, sem 
- Uma descrição de vínculos profissionais instáveis, escolhas individualizadas e sensação de
- Para identificar mobilidade social relativa, um pesquisador deve priorizar:
- Uma empresa de entregas controla o acesso a tarefas por pontuações opacas enquanto anuncia
- Uma marcha pública organizada em torno de direitos habitacionais, com reuniões contínuas e
- Um edital usa a expressão democracia racial para afirmar que, após a abolição, inexistem b
- Uma proposta de ensino articula dados locais, conceitos sociológicos e um debate sobre pol
- Um professor avalia a análise de dados por rubrica pública, atribuindo peso a pertinência 
- Uma rede melhora o número total de matrículas, mas persiste elevado abandono entre alunos 
- Ao analisar o estabelecimento de padrões de autocontrole em processos históricos de longa 
- Em pesquisa escolar, alunas são convidadas a relatar, nominalmente, experiências pessoais 
- Na crítica de Bourdieu e Passeron à reprodução escolar, o mecanismo central envolve:
- Em uma comparação entre municípios, estudantes percebem que renda média maior acompanha ma
- Para Durkheim, a existência de sanções formais ou informais contra o descumprimento de reg
- Em Weber, basta várias pessoas realizarem a mesma ação simultaneamente para que haja ação 
- A solidariedade orgânica é associada por Durkheim à diferenciação de funções e à interdepe
- Para Marx, capital e trabalho assalariado são posições socialmente constituídas nas relaçõ
- O tipo ideal em Weber equivale à descrição fiel da média estatística observada em uma popu
- Bourdieu distingue capital cultural incorporado, objetivado e institucionalizado, e um dip
- A violência simbólica de Bourdieu exige necessariamente agressão física explícita para que
- A dualidade da estrutura em Giddens admite que práticas reiteradas reproduzam regras e rec
- Ao falar em modernidade líquida, Bauman afirma que Estado, classes sociais e instituições 
- No estudo sociológico da estratificação, diferenças de renda não esgotam desigualdades de 
- A mobilidade social intrageracional é medida exclusivamente pela comparação da profissão d
- A análise interseccional investiga a articulação de posições como raça, classe e gênero em
- Trabalho doméstico e de cuidado não remunerado não pode ser objeto da Sociologia do Trabal
- Taylorismo e fordismo são equivalentes em todos os aspectos: ambos se definem exclusivamen
- A sociologia econômica pode investigar como vínculos de confiança e redes de contatos infl
- Para Weber, a definição sociológica de Estado envolve a reivindicação bem-sucedida do mono
- Na interpretação de Sérgio Buarque, o homem cordial se define pela gentileza permanente e 
- Florestan Fernandes considerou que o encerramento jurídico da escravidão bastou para elimi
- A BNCC do ensino médio apresenta Ciências Humanas e Sociais Aplicadas como área integrada 
- Uma avaliação formativa, por definição, proíbe devolutivas durante a sequência didática e 
```

## Pedido 29: Conciliação prévia, renúncia, transação e arbitragem (`direito-trabalho.conciliacao-previa-renuncia-transacao-arbitragem`, 111 tópicos de edital)

```
Matéria: direito-trabalho.conciliacao-previa-renuncia-transacao-arbitragem (Conciliação prévia, renúncia, transação e arbitragem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Autocomposição e Comissões de Conciliação Prévia": 25
- "2. Prazos, termo de conciliação e efeitos liberatórios": 25
- "3. Renúncia, transação e limites dos direitos trabalhistas": 23
- "4. Arbitragem individual, coletiva e negociação coletiva": 16
- "5. Integração dos institutos, casos de prova e controle de validade": 11

Já existem (não repita):
- Uma empresa pretende criar Comissão de Conciliação Prévia para tratar divergências individ
- Uma trabalhadora pretende ajuizar reclamação sem antes procurar a comissão de conciliação 
- Após optar pela CCP, um empregado apresenta controvérsia formalizada por escrito. Qual é o
- Uma empregada optou por levar reivindicação à CCP e não obteve conciliação. Qual efeito o 
- Em comissão regularmente constituída, as partes firmam conciliação genuína, sem ressalvas 
- Diante de divergência real sobre a quantidade de horas extras devidas, empregado e emprega
- Um empregado recebe remuneração superior a duas vezes o teto dos benefícios do RGPS e conc
- Uma empresa e um ex-empregado apresentam petição conjunta para homologar acordo extrajudic
- Após a negociação coletiva fracassar, as partes consideram submeter o conflito a árbitros.
- Uma CCP empresarial terá representantes titulares e suplentes dos empregados escolhidos re
- Na mesma localidade existem CCP sindical e empresarial para a categoria. Um empregado deci
- Para uma pretensão trabalhista, restavam 40 dias de prazo prescricional quando o empregado
- Empregado e empregador firmaram perante o sindicato profissional termo anual que discrimin
- As partes apresentaram petição conjunta de homologação de acordo extrajudicial. Qual é o p
- Uma petição conjunta de homologação extrajudicial especificou direitos controvertidos, mas
- Um empregado tem diploma superior e salário exatamente igual a duas vezes o teto do RGPS. 
- As partes escreveram no contrato, antes de surgir qualquer controvérsia, que eventuais con
- Instrumento coletivo negociado estabelece limitação de vantagem trabalhista não absolutame
- Uma pessoa recebe salário exatamente igual ao dobro do teto do RGPS, concluiu curso superi
- No desligamento, a empresa conduz o empregado à CCP e lhe apresenta documento que apenas r
- Um empregador não pagou verbas rescisórias tempestivamente. Posteriormente, apresenta acor
- Uma empregada escolhe propor ação trabalhista diretamente. A parte contrária sustenta que 
- Em controvérsia coletiva, a negociação direta se frustrou e as entidades envolvidas decidi
- Em arbitragem validamente instituída, o árbitro profere sentença condenatória. À luz dos a
- Uma empresa com mais de duzentos empregados institui representação interna de trabalhadore
- Uma empresa instituiu Comissão de Conciliação Prévia com quatro integrantes, dois indicado
- Existindo CCP na localidade, a pessoa que pretenda ajuizar reclamação trabalhista deve pri
- Uma comissão empresarial concedeu mandato de um ano a seus representantes, admitindo uma r
- A provocação de CCP suspende a prescrição e, após fracassar a tentativa, a contagem recome
- Em uma controvérsia real, empregado e empregador apresentam concessões recíprocas para enc
- Uma trabalhadora apresenta acordo extrajudicial para homologação judicial com o empregador
- Se uma comissão empresarial e uma comissão sindical coexistem na mesma localidade para a m
- Um termo anual de quitação firmado perante sindicato profissional, nos termos do art. 507-
- O representante dos empregados na CCP empresarial, titular ou suplente, conta com proteção
- O juiz encarregado de analisar acordo extrajudicial submetido à homologação deve fazê-lo e
- Após a negociação coletiva fracassar, entidades participantes elegem árbitros para decidir
- Um empregador propõe alteração contratual que reduz benefício assegurado ao empregado. A a
- A Lei nº 9.307/1996 distingue cláusula compromissória, relativa a conflitos futuros de um 
- Um empregado com remuneração exatamente igual a duas vezes o teto do RGPS e diploma superi
- Em conciliação genuína perante CCP, o termo assinado pode constituir título executivo extr
- O Tema 1.046 de repercussão geral autoriza acordos e convenções coletivos a restringir dir
- Uma petição conjunta para homologar acordo extrajudicial pode suspender o prazo prescricio
- Uma sentença arbitral condenatória proferida em arbitragem validamente instituída somente 
- Na celebração de acordo extrajudicial para homologação, a assistência por advogados distin
- O art. 625-E estabelece que a eficácia liberatória geral do termo da CCP protege inclusive
```

## Pedido 30: Engenharia florestal: sementes, viveiros e silvicultura, implantação de povoamentos, dendrometria, inventário florestal, tecnologia da madeira, economia florestal e sistemas agroflorestais (`engenharia-florestal.silvicultura-mensuracao-madeira`, 111 tópicos de edital)

```
Matéria: engenharia-florestal.silvicultura-mensuracao-madeira (Engenharia florestal: sementes, viveiros e silvicultura, implantação de povoamentos, dendrometria, inventário florestal, tecnologia da madeira, economia florestal e sistemas agroflorestais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sementes, viveiros e produção de mudas": 17
- "2. Implantação e condução de povoamentos florestais": 17
- "3. Dendrometria e crescimento": 17
- "4. Inventário florestal": 17
- "5. Tecnologia da madeira": 16
- "6. Economia florestal, biomassa e sistemas agroflorestais": 16

Já existem (não repita):
- Sementes recalcitrantes, como as de araucária e ingá, toleram a secagem a baixos teores de
- Sementes de uma leguminosa florestal com tegumento duro e impermeável não germinam mesmo e
- A rustificação de mudas florestais consiste em reduzir gradualmente o sombreamento, a irri
- Na silvicultura clonal do eucalipto, a técnica de propagação mais empregada atualmente em 
- É característica de muda florestal de boa qualidade para plantio:
- O controle de formigas cortadeiras em plantios florestais deve ser iniciado apenas após o 
- Um povoamento de eucalipto implantado no espaçamento de 3 m entre linhas e 2 m entre plant
- O cultivo mínimo no preparo do solo para plantios florestais caracteriza-se pela subsolage
- Em um plantio de pinus destinado à produção de madeira serrada de alta qualidade, a operaç
- A condução da brotação das cepas após o corte raso de um eucaliptal, aproveitando o sistem
- No Brasil, o diâmetro à altura do peito (DAP) é convencionalmente medido a 1,30 m acima do
- Uma árvore com circunferência à altura do peito (CAP) de 94,2 cm tem DAP de aproximadament
- A área basal de uma árvore com DAP de 30 cm é de aproximadamente:
- Considerando área basal de 0,0707 m², altura total de 25 m e fator de forma de 0,5, o volu
- Na cubagem rigorosa pelo método de Smalian, o volume de uma seção com áreas transversais d
- O volume empilhado de lenha, expresso em estéreos, é numericamente igual ao volume sólido 
- Um povoamento de eucalipto apresenta produção de 280 m³/ha aos 7 anos de idade. Seu increm
- A idade técnica de corte de um povoamento, que maximiza a produção média anual, correspond
- O índice de sítio, usado para classificar a capacidade produtiva de um local, baseia-se na
- No método de Bitterlich, a área basal por hectare é obtida multiplicando-se o número de ár
- Em um inventário por amostragem aleatória simples, 16 parcelas forneceram desvio padrão de
- Mantidos os demais fatores, a redução do erro admissível de um inventário florestal, de 20
- A amostragem estratificada é vantajosa em relação à aleatória simples quando:
- Na amostragem estratificada, a alocação ótima de Neyman distribui as parcelas entre os est
- No manejo florestal sustentável de florestas nativas, o planejamento da exploração em cada
- Abaixo do ponto de saturação das fibras, em torno de 28 a 30% de umidade, a madeira que pe
- Quanto à contração da madeira durante a secagem, a ordem decrescente de magnitude é:
- Para fabricar mourões tratados em autoclave, prefere-se madeira com grande proporção de al
- No ensaio de flexão estática, o módulo de elasticidade (MOE) expressa a rigidez da madeira
- Em uma serraria, são exemplos de equipamentos auxiliares, usados após o desdobro primário 
- A serra de fita é muito usada no desdobro principal de toras por apresentar lâmina fina, o
- O método de preservação de madeira considerado mais eficiente, por promover penetração pro
- A secagem da madeira ao ar livre é mais rápida que a secagem em estufa e permite atingir u
- São defeitos de secagem da madeira:
- Um projeto florestal prevê receita líquida única de R$ 1.210,00 no final do segundo ano. À
- Para comparar a viabilidade de dois projetos florestais com rotações de durações diferente
- A taxa interna de retorno (TIR) de um projeto é a taxa de desconto que torna o seu valor p
- Na valoração econômica de uma floresta, a regulação hídrica e o estoque de carbono são exe
- No uso da madeira como biomassa para energia, o poder calorífico útil é favorecido por men
- Um sistema que combina, na mesma área, árvores de eucalipto, pastagem e criação de gado de
- Nos sistemas silvipastoris, as mudas de árvores devem ser protegidas do gado nos primeiros
```

## Pedido 31: Engenharia de avaliações e perícias (`engenharia.avaliacoes-pericias`, 110 tópicos de edital)

```
Matéria: engenharia.avaliacoes-pericias (Engenharia de avaliações e perícias)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, conceitos de valor e engenharia legal": 17
- "2. Métodos de avaliação e seleção da abordagem": 17
- "3. Pesquisa de mercado, homogeneização, fundamentação e precisão": 17
- "4. Depreciação e avaliações urbanas, rurais e de empreendimentos": 17
- "5. Desapropriações, servidões e indenizações": 16
- "6. Vistorias, perícias judiciais, laudos e responsabilidade profissional": 16

Já existem (não repita):
- Um avaliador encontrou custo histórico de R$ 300 mil e anúncio atual de R$ 460 mil para um
- Para um laudo de imóvel urbano, qual associação à série ABNT NBR 14653 está correta?
- Qual conjunto descreve corretamente o conteúdo da engenharia legal?
- Dois imóveis com a mesma área têm preços de anúncio diferentes. Qual condição deve ser ver
- Um avaliador reuniu vendas verificadas de apartamentos comparáveis, ajustando diferenças d
- No método evolutivo para um imóvel edificado, qual relação didática é adequada?
- Qual situação é exemplo típico de aplicação do método involutivo?
- Um prédio gera renda líquida sustentável de R$ 80 mil/ano. Em modelo de perpetuidade com t
- Uma pesquisa contém a mesma oferta copiada em cinco portais. Como tratar esses registros?
- Um comparável é inferior em localização ao imóvel avaliando. Ao trazer seu preço à condiçã
- O que descreve mais precisamente o grau de fundamentação de uma avaliação?
- Na pesquisa de mercado, a regressão estatística é empregada para:
- Qual exemplo caracteriza sobretudo obsolescência funcional?
- Uma edificação de 10 anos foi integralmente mantida; outra de 6 anos está bastante deterio
- Na avaliação de fazenda com instalações e culturas, qual distinção é indispensável?
- Um laudo emprega custo novo da construção e depreciação linear didática. O que ainda deve 
- Uma faixa de acesso foi desapropriada e o remanescente ficou sem entrada para veículos. O 
- Sobre servidão administrativa incidente em terreno particular, é correto afirmar:
- Em uma desapropriação parcial, antes da intervenção o bem valia R$ 900 mil; após, o remane
- Em desapropriação rural para reforma agrária, qual formulação respeita a ressalva constitu
- Nos termos do CPC, qual conjunto descreve a prova pericial?
- Qual conjunto é exigido pelo art. 473 do CPC no laudo pericial?
- Um profissional concluiu um laudo de valor sem indicar fonte de dados, data-base ou hipóte
- Sobre a ART em avaliações e perícias no âmbito profissional do Sistema Confea/Crea, assina
- O preço pedido em um anúncio imobiliário pode diferir tanto do preço efetivamente transaci
- Uma avaliação técnica pode prescindir da data de referência sempre que o imóvel possuir ma
- Na engenharia legal, a vistoria pode fornecer dados fáticos que serão examinados em poster
- O método comparativo direto é caracterizado por projetar receitas de loteamento hipotético
- No método da capitalização da renda, é relevante distinguir receita bruta do fluxo líquido
- Métodos de custo e avaliação do valor de mercado são conceitualmente idênticos, pois todo 
- No método evolutivo, o fator de comercialização é necessariamente igual a 1 para evitar di
- A existência de muitos anúncios copiados de uma mesma origem garante uma amostra estatisti
- Em homogeneização, o sinal e o sentido de cada ajuste devem ser interpretados segundo a co
- O grau de precisão normativo pode ser presumido igual ao grau de fundamentação, porque amb
- Um dado de mercado excepcionalmente alto deve ser descartado automaticamente, mesmo sem in
- O método linear simplificado de depreciação pode ser utilizado didaticamente, mas não disp
- Em avaliação rural, a mera igualdade da área em hectares implica equivalência de valor por
- Uma servidão administrativa deve ser sempre indenizada pelo valor integral da propriedade 
- Na desapropriação parcial, a perda de acessibilidade do remanescente pode representar efei
- O regime constitucional de indenização da desapropriação para reforma agrária é necessaria
- Nos termos do CPC, o laudo pericial pode omitir a indicação do método utilizado se apresen
- A apresentação de quesitos pelas partes e a atuação de assistentes técnicos integram o con
- O registro de ART dispensa a confirmação de que a atividade está incluída nas atribuições 
- Se o profissional não conseguiu acesso à edificação, pode relatar no laudo que efetuou vis
```

## Pedido 32: Crimes de trânsito (`legislacao-penal-especial.crimes-transito`, 110 tópicos de edital)

```
Matéria: legislacao-penal-especial.crimes-transito (Crimes de trânsito)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições gerais, penalidades e fronteira entre crime e infração": 20
- "2. Homicídio e lesão culposos na direção de veículo automotor": 20
- "3. Omissão de socorro, afastamento do local e embriaguez ao volante": 20
- "4. Competição não autorizada, condução sem habilitação e entrega da direção": 20
- "5. Fraude, sanções especiais e resolução de casos integrados": 20

Já existem (não repita):
- Uma pessoa sem habilitação dirige em via pública e provoca perigo concreto ao entrar na co
- Proprietário consciente de que o amigo nunca obteve CNH entrega-lhe a direção do carro. O 
- Um condutor causa, por imprudência na direção de veículo automotor, a morte de pedestre, s
- Motorista envolvido em sinistro com pessoa ferida se afasta para não ser identificado nem 
- Um condutor participa de corrida não autorizada em via pública, colocando pedestres em sit
- Motorista é abordado durante condução com sinais consistentes de alteração psicomotora por
- Em sinistro com vítima, o condutor prontamente presta socorro integral à pessoa atingida. 
- Condutor não consegue prestar assistência direta por justa causa após sinistro, mas tem co
- Motorista trafega em velocidade incompatível com a segurança junto à entrada de escola, cr
- Pessoa dirige sem habilitação em via pública, porém nenhum fato indica perigo de dano. Que
- Um motorista apresenta medição de etilômetro de 0,34 mg/L de ar alveolar, em teste válido 
- Motorista sob influência comprovada de álcool causa culposamente a morte de passageiro enq
- Pessoa com capacidade psicomotora alterada por álcool provoca culposamente fratura classif
- Em lesão corporal culposa de trânsito, condutor trafegava a 112 km/h onde a velocidade máx
- Após sinistro com vítima, o motorista não presta socorro, embora a assistência seja feita 
- Motorista colabora com apuração, permanece no local do sinistro e opta por não responder a
- Após sinistro com vítima, uma pessoa altera artificialmente a posição de objetos para enga
- Proprietário entrega automóvel a pessoa não habilitada, que dirige sem criar perigo de dan
- Durante exibição automobilística não autorizada em via pública e geradora de risco, o moto
- Em crime de trânsito ligado à lesão culposa, o motorista ultrapassou em 45 km/h o limite d
- Após condenação definitiva por crime de trânsito, o réu é intimado a entregar sua CNH em 4
- Condutor se omite inteiramente no socorro após sinistro, e laudo demonstra que a vítima so
- Em condenação por crime do art. 311, o juiz conclui que é cabível substituir a pena privat
- Durante exercício profissional de transporte de passageiros, um condutor culposamente caus
- Motorista recusa etilômetro; não há outro dado de alteração psicomotora, embora a fiscaliz
- Condutor flagrado dirigindo com capacidade psicomotora alterada por álcool pode realizar o
- Uma pessoa sem CNH dirige em via pública e não gera nenhum perigo de dano. Ainda assim, fi
- Se o motorista não pode prestar socorro diretamente por justa causa após sinistro com víti
- Todo afastamento físico do motorista do local de sinistro constitui automaticamente crime 
- Participar ao volante de corrida automobilística não autorizada em via pública, gerando ri
- O proprietário que entrega veículo a pessoa não habilitada só pode cometer o delito do art
- O motorista que, em sinistro de trânsito com vítima, presta socorro pronto e integral não 
- Nas ressalvas do art. 291, § 1º, III, basta ultrapassar a velocidade regulamentada em cinq
- O homicídio culposo na direção de veículo automotor, sem a hipótese especial de álcool do 
- A lesão corporal culposa leve cometida ao dirigir com capacidade psicomotora alterada é, p
- O art. 306, § 2º, admite que a alteração psicomotora do motorista seja verificada por exam
- A recusa do condutor a ser submetido a teste de alcoolemia, prevista no art. 165-A do CTB,
- A fraude do art. 312 pode estar configurada quando a cena de sinistro com vítima é alterad
- O condenado que deixa de entregar a CNH em 24 horas após a simples lavratura do auto admin
- Para configurar o art. 311, a incompatibilidade da velocidade deve ser avaliada em local s
- Uma exibição de habilidade ao volante realizada com autorização da autoridade competente p
- O STF, no Tema 907, considerou constitucional o art. 305 do CTB, preservando o direito ao 
- O art. 312-B permite substituir automaticamente todas as penas de reclusão dos arts. 302, 
- O art. 302, § 1º, IV, prevê aumento no homicídio culposo se, no exercício de sua profissão
- Se uma vítima recebe atendimento imediato de pedestres, o art. 304 impede categoricamente 
```

## Pedido 33: Séries temporais e números-índice (`estatistica.series-temporais-numeros-indices`, 109 tópicos de edital)

```
Matéria: estatistica.series-temporais-numeros-indices (Séries temporais e números-índice)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Relativos, índices simples, bases e deflacionamento": 25
- "2. Índices ponderados de Laspeyres, Paasche, Fisher e testes": 27
- "3. Séries temporais: componentes, variações e médias móveis": 19
- "4. Estacionariedade, autocovariância, ACF e análise espectral": 16
- "5. Suavização exponencial, ARIMA introdutório e avaliação de previsões": 13

Já existem (não repita):
- O preço de um serviço era 20 unidades no período-base e passou a 25 no período atual. Com 
- Um índice de vendas passa de 100 para 108, mantida a base do primeiro período. Qual taxa p
- Uma série tem índices 100, 120 e 150 com base no primeiro ano. Ao escolher o segundo ano c
- O preço de um produto aumenta 10% num mês e diminui 10% no seguinte. Qual variação acumula
- Uma receita nominal é 330 unidades e o índice de preços do mesmo período vale 110, na base
- Duas mercadorias tiveram preços alterados, e o analista deseja calcular o índice de preços
- Um estudo usa a cesta efetivamente consumida no período atual para ponderar tanto os preço
- Uma série de atendimentos apresenta valores 6, 9 e 15 nos três meses mais recentes. Qual m
- Em cinco anos consecutivos, as vendas de um comércio aumentam sempre em dezembro e recuam 
- Um salário nominal cresce 10% e um índice adequado de preços também aumenta 10% no mesmo i
- Para dois itens, os preços iniciais são 2 e 4, os finais 3 e 5 e as quantidades-base 2 e 1
- Dois produtos têm preços iniciais 2 e 4, preços finais 3 e 5 e quantidades atuais 1 e 2. Q
- Uma cesta possui preços atuais 3 e 5; quantidades-base 2 e 1; quantidades atuais 1 e 2. Qu
- Um índice publicado tem valor 150 no último período e 120 no ano escolhido como nova base.
- Em um estudo com dois períodos, Laspeyres de preços resulta em 121 e Paasche de preços em 
- Um relatório cita IPCA e INPC para comparar o custo de vida de populações distintas. Qual 
- Os atendimentos mensais sobem de 80 para 100. Quais são, respectivamente, as variações abs
- Uma série de vendas apresenta oscilações sazonais de 20 unidades com nível 100 e de 40 uni
- Um índice satisfaz a propriedade de reversão do tempo e vale 125 ao comparar o período 1 c
- Uma série cresce 20% entre o ano 0 e o ano 1 e cai 10% entre o ano 1 e o ano 2. Com o ano 
- Laspeyres de preços vale 144 e Paasche de preços vale 100, ambos comparando dois períodos 
- A remuneração nominal sobe 20%, enquanto o índice pertinente de preços sobe 50%. Qual é a 
- Uma série oscila em torno de média constante, apresenta variância constante e sua autocova
- Na suavização exponencial simples, o nível anterior vale 40, a observação atual é 80 e o p
- Uma série é representada conceitualmente por ARIMA(1,1,0). Qual descrição respeita as posi
- Um preço passou do índice 100 para 115 em referência fixa. O resultado representa aumento 
- Se o preço de um produto aumenta 20% e, no período seguinte, cai 20%, seu preço final coin
- Um índice registra 125 no período que se tornará a nova base e 150 no período posterior. A
- Uma despesa nominal de 200 unidades deflacionada por índice de preços 125, na base 100, re
- Em um índice de preços de Laspeyres, os preços de dois momentos são comparados com a mesma
- No índice de preços de Paasche, as quantidades do período-base são usadas no numerador e n
- Para observações consecutivas de 100, 120 e 140, uma média móvel simples retrospectiva de 
- Uma elevação de demanda observada todo mês de dezembro durante vários anos deve ser classi
- No estudo introdutório de previsão, o método de Holt acrescenta uma componente de tendênci
- A autocorrelação de defasagem zero de uma série com variância positiva é zero, pois não ex
- Aplicar primeira diferença aos níveis 10, 13 e 16 produz valores 3 e 3 e reduz em uma unid
- Uma série fracamente estacionária precisa assumir exatamente o mesmo valor em todas as dat
- O índice ideal de Fisher de preços, como média geométrica de Laspeyres e Paasche, satisfaz
- O índice ideal de Fisher satisfaz necessariamente a circularidade para quaisquer três perí
- O índice de quantidades de Laspeyres mantém os preços do período-base como ponderadores e 
- Na especificação habitual do teste ADF, a hipótese nula afirma que a série é estacionária 
- Em ARIMA(p,d,q), o parâmetro d expressa a ordem de diferenciação não sazonal usada para mo
- O componente MA(q) em ARIMA corresponde exatamente ao cálculo aritmético de médias móveis 
- Para série mensal com padrão anual, uma especificação SARIMA pode utilizar período sazonal
- Para os erros de previsão 3 e 4, o RMSE é inferior ao MAE, porque elevar erros positivos a
```

## Pedido 34: ISS, IPTU, ITBI e tributos municipais (`direito-tributario.iss-tributos-municipais`, 109 tópicos de edital)

```
Matéria: direito-tributario.iss-tributos-municipais (ISS, IPTU, ITBI e tributos municipais)
Capítulos e quantas questões quero em cada um (total 100):
- "Competência municipal e sistema tributário local": 17
- "ISS: incidência, serviços, territorialidade e cálculo": 17
- "ISS: responsabilidade, fiscalização e Simples Nacional": 17
- "IPTU: materialidade, cálculo e ordenação urbana": 17
- "ITBI: transmissão, base de cálculo e fiscalização": 16
- "Taxas, contribuições municipais e reforma para o IBS": 16

Já existem (não repita):
- A competência do município para instituir IPTU não decorre da repartição de receitas tribu
- O Imposto sobre Serviços pode incidir sobre qualquer serviço economicamente útil, ainda qu
- Na regra geral da LC 116/2003, o ISS pertence ao município do estabelecimento prestador, r
- O prestador de serviços é o contribuinte do ISS, embora determinadas hipóteses legais atri
- A base de cálculo ordinária do ISS é o preço do serviço e não o lucro líquido do prestador
- O fato gerador do IPTU pressupõe necessariamente que o imóvel seja ocupado diretamente por
- A contribuição de melhoria pode ser exigida por obra pública com valorização imobiliária, 
- O ITBI incide sobre o recebimento de imóvel por herança, uma vez que se trata de transmiss
- A emissão de nota fiscal indicando alíquota do ISS do Simples Nacional elimina, em qualque
- A LC 116/2003 prevê que serviço importado possa gerar ISS, com regra de localização e resp
- O valor venal que serve de base ao IPTU é necessariamente o valor de mercado usado no ITBI
- A COSIP prevista no art. 149-A da Constituição pode ser instituída por lei municipal e cob
- A partir de 2026, a reforma tributária extinguiu o ISS em todo o território nacional e tra
- Para exigir IPTU progressivo no tempo, basta editar lei com alíquotas mais altas para terr
- O conceito de estabelecimento prestador, para ISS, pode alcançar unidade econômica temporá
- Uma lei municipal que estabeleça retenção de ISS para qualquer tomador, sem delimitação le
- Segundo o STJ no Tema 1113, o município pode impor base de cálculo do ITBI por referência 
- O teto de 15% previsto no Estatuto da Cidade é limite nacional de qualquer alíquota de IPT
- Em 2030, segundo o regime de transição do ISS, as alíquotas desse imposto deverão correspo
- Após a EC 132/2023, a contribuição do art. 149-A restringe-se exclusivamente a custear a o
- Um Município pretende instituir tributo sobre propriedade urbana por lei local. Qual é o i
- Uma clínica presta serviço inserido na lista da LC 116/2003. A base de cálculo ordinária d
- Qual percentual representa a alíquota máxima geral do ISS fixada na LC 116/2003, antes da 
- Para fins de IPTU, qual é a base de cálculo indicada pelo CTN?
- O serviço público de iluminação de ruas, que beneficia a coletividade indistintamente, pod
- Uma empresa executa obra de construção civil enquadrada no subitem 7.02 da LC 116, em muni
- Na venda onerosa de imóvel localizado no Município A, por vendedor domiciliado em B a comp
- Em regra, qual fato permite cobrar contribuição de melhoria municipal?
- Uma pessoa física recebe imóvel por doação gratuita. O tributo tipicamente correspondente 
- Sobre a retenção de ISS, a interpretação adequada da LC 116/2003 é:
- Uma EPP do Simples Nacional emite nota de serviço sujeito a retenção de ISS. Qual cuidado 
- Qual proposição descreve corretamente a exportação de serviços para efeito do ISS?
- Em um cadastro urbano, a prefeitura calcula o IPTU incorporando o valor dos móveis e eletr
- Em situação de IPTU progressivo no tempo, uma lei municipal estabelece acréscimo superior 
- Um contribuinte informa R$ 500 mil como valor de sua transação imobiliária, e a prefeitura
- No CTN, a taxa de serviço pressupõe qual característica do serviço público?
- Sobre a jurisprudência do STF referente ao local do ISS sobre planos de saúde e certas ati
- Um município pretende atualizar a base de cálculo do IPTU por ato do Executivo. Qual condi
- Qual conjunto descreve corretamente a transição das alíquotas do ISS entre 2029 e 2032?
- Uma prefeitura recebe pedido de ITBI por cessão onerosa de direitos de aquisição imobiliár
- Uma lei municipal cria taxa anual para financiar a iluminação pública indistinta das ruas.
- Qual é a alternativa compatível com o regime de contribuição de melhoria após pavimentação
- Um auditor compara as bases do IPTU e do ITBI de um mesmo imóvel. Qual fundamento jurídico
- Um fiscal encontra retenção válida de ISS sobre receita de prestadora optante pelo Simples
- Considere o ano de 2026 e uma questão sobre criação do IBS. Qual proposição preserva corre
```

## Pedido 35: História das Relações Internacionais: das guerras mundiais à Guerra Fria (`relacoes-internacionais.historia-guerras-guerra-fria`, 108 tópicos de edital)

```
Matéria: relacoes-internacionais.historia-guerras-guerra-fria (História das Relações Internacionais: das guerras mundiais à Guerra Fria)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Congresso de Viena, Concerto Europeu e rivalidades imperialistas": 17
- "2. Primeira Guerra Mundial, Revolução Russa e ordem de Versalhes": 25
- "3. Entreguerras, fascismo, nazismo e Segunda Guerra Mundial": 21
- "4. Bipolaridade, confrontos indiretos e descolonização": 17
- "5. Distensão, fim do socialismo real e ordem pós-Guerra Fria": 20

Já existem (não repita):
- Uma delegação diplomática estuda a reorganização da Europa depois das guerras napoleônicas
- Após Viena, potências europeias recorrem a reuniões diplomáticas para acomodar crises e im
- Em análise dos acordos de Bismarck, um estudante identifica um tratado defensivo assinado 
- Um arquivo diplomático de 1887 menciona tratado secreto de neutralidade entre Alemanha e R
- Um relatório descreve a Conferência de Berlim de 1884–1885 como parte da expansão imperial
- Após o atentado de Sarajevo, um pesquisador avalia por que uma crise austro-sérvia se tran
- Uma proposta de paz apresentada em janeiro de 1918 defendia, no último de seus quatorze po
- Um quadro histórico reúne duas revoluções russas em 1917, mas informa que ambas foram lide
- Ao comparar armistício e tratado de paz, uma equipe precisa identificar o instrumento de 2
- Ao reconstruir a criação da Liga das Nações, um texto afirma que o presidente Wilson foi u
- Ao descrever o fortalecimento de Stalin, um material associa a industrialização planificad
- Uma análise aproxima o fascismo italiano do nazismo alemão e precisa preservar a diferença
- Um documento diplomático de 1938 registra concessões territoriais alemãs nos Sudetos, real
- Uma cronologia aponta a data de 1º de setembro de 1939 como início convencional da guerra 
- Ao examinar as Leis de Nuremberg promulgadas em 1935, uma pesquisadora precisa relacioná-l
- Um pesquisador encontra documentos da conferência monetária realizada em julho de 1944, ai
- Ao examinar a nova organização mundial concebida durante a Segunda Guerra, um estudante co
- Em parecer sobre o início da bipolaridade, uma equipe vincula uma doutrina de apoio contra
- Um documento classifica a OTAN, fundada em 1949, e o Pacto de Varsóvia, de 1955, como duas
- Diante de fotografias do Muro de Berlim, uma equipe afirma que ele foi construído no mesmo
- Um historiador descreve os acordos de limitação nuclear dos anos 1970 e afirma que a diste
- Uma pesquisa distingue a Conferência Afro-Asiática de Bandung e a primeira cúpula do Movim
- Em debate sobre independências africanas, alguém afirma que descolonização significou tran
- Um relatório coloca a queda do Muro de Berlim, a reunificação alemã e a dissolução da URSS
- Ao interpretar o Plano Marshall, um pesquisador aponta reconstrução econômica e estratégia
- Um pesquisador diferencia a reorganização de 1815 da unificação alemã. O Congresso de Vien
- Ao listar as alianças existentes na Europa em 1882, um material identifica a Tríplice Alia
- Um estudante atribui ao assassinato em Sarajevo o papel de estopim da Primeira Guerra, sem
- Uma tabela aponta o Tratado de Brest-Litovsk como acordo assinado pela Alemanha derrotada 
- Uma cronologia relaciona a Marcha sobre Roma, em 1922, à chegada de Mussolini ao poder na 
- Um artigo afirma que o Plano Marshall foi criado após a queda do Muro de Berlim em 1989 pa
- Uma investigação sobre o sistema bismarckiano conclui que a não renovação do Tratado de Re
- Ao relatar a guerra de 1914–1918, um texto afirma que a Itália participou desde o primeiro
- Na avaliação da ordem criada em 1919, a Liga das Nações buscava promover cooperação e segu
- Um estudo do período de 1933–1939 descreve o Acordo de Munique como tratado firmado por Hi
- No estudo das instituições do pós-guerra, um pesquisador diferencia Bretton Woods da confe
- Um relatório classifica o Comecon como força militar criada em 1955 para substituir a OTAN
- Em seminário sobre a descolonização, aponta-se que a Resolução 1514 (XV) da Assembleia Ger
- Um estudo da Crise dos Mísseis de outubro de 1962 afirma que ela foi encerrada por guerra 
- Ao analisar o regime de Stalin e a política externa soviética, é correto reconhecer que a 
- Um grupo descreve o Movimento dos Não Alinhados como uma terceira aliança militar única, v
- Ao avaliar o fim da Guerra Fria, um relatório reconhece que o Tratado INF, de 1987, foi in
- Uma síntese histórica afirma que a reunificação da Alemanha em 1990 significou extinção si
- Um estudo observa que a intensificação da globalização após 1991 não significa sua origem 
- Segundo os modelos de interpretação das relações internacionais, o realismo se concentra n
```

## Pedido 36: Política externa brasileira: regiões, países e agenda global (`relacoes-internacionais.politica-externa-brasileira-agenda-global`, 108 tópicos de edital)

```
Matéria: relacoes-internacionais.politica-externa-brasileira-agenda-global (Política externa brasileira: regiões, países e agenda global)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da política externa e Mercosul": 18
- "2. América do Sul, Argentina e relações hemisféricas": 19
- "3. União Europeia, CPLP e cooperação Sul-Sul": 27
- "4. Multilateralismo, paz, direitos humanos e desarmamento": 19
- "5. Fome, finanças internacionais, mar, espaço e Antártida": 17

Já existem (não repita):
- Representantes brasileiros discutem mediação de conflito entre países vizinhos e propõem n
- Em exposição histórica sobre a fundação do Mercosul, foi solicitado o instrumento e a data
- Após sua fundação, o Mercosul adotou protocolo que redefiniu a estrutura institucional e r
- Ao comparar Mercosul e União Europeia, uma análise menciona a forma habitual de decisão do
- Um projeto conecta ferrovia e rodovia brasileiras a portos e corredores de país vizinho, a
- Brasil e Argentina mantêm estrutura conjunta para verificar uso pacífico de materiais nucl
- Em estudo institucional da União Europeia, distingue-se quem propõe legislação e quem norm
- Um país participa de foro que valoriza a língua portuguesa, promove concertação político-d
- Ao analisar a situação jurídica da Bolívia no Mercosul após agosto de 2024, um texto afirm
- Um programa brasileiro de 2026 reúne corredores rodoviários, ferrovias, portos e linhas el
- Após imposição de tarifas norte-americanas, o Brasil apresentou pedido de consultas no sis
- Uma revisão em outubro de 2026 examina o acordo comercial interino entre Mercosul e União 
- País em desenvolvimento solicita formalmente ao Brasil apoio para adaptar políticas públic
- Uma crise internacional leva à discussão de sanções multilaterais e segurança coletiva sob
- Em estudo sobre desarmamento latino-americano, menciona-se tratado de 1967 que estabeleceu
- Na presidência brasileira do G20, foi lançada iniciativa internacional de combate à fome e
- Ao analisar a posição do Brasil sobre a questão israelense-palestina, uma pessoa confunde 
- Diante de ameaça à paz, o Conselho de Segurança discute interromper relações econômicas e,
- Um relatório internacional examina discriminação baseada em raça, gênero e religião e pret
- Uma proposta de cooperação antiterrorista cita o instrumento da ONU adotado em 2001 que cr
- Na discussão sobre reforma de governança econômica, propõe-se ampliar representatividade n
- Um navio de pesquisa cruza a zona econômica exclusiva de país costeiro, sem explorar recur
- Uma expedição científica na Antártida utiliza apoio logístico militar em pesquisa ambienta
- Uma empresa pretende reclamar soberania estatal sobre órbita utilizada por seus satélites,
- Um programa com país africano reúne especialistas brasileiros, financiamento de terceiro p
- Em reunião diplomática com países latino-americanos, o Brasil propõe cooperação econômica,
- Em material histórico de integração regional, afirma-se que o Protocolo de Olivos, de 2002
- Brasil e Argentina desenvolveram mecanismo bilateral de transparência nuclear. A ABACC, cr
- Após a criação da Comunidade dos Países de Língua Portuguesa em 1996, Timor-Leste e Guiné 
- A cooperação Sul-Sul pode partir de demanda de país interessado, com compartilhamento de c
- Ao discutir resposta internacional a conflito armado, um estudo atribui à Assembleia Geral
- Num debate sobre segurança alimentar, o ODS 2 da Agenda 2030 é associado à erradicação da 
- Uma autoridade afirma que, por prever mercado comum, o Tratado de Assunção prova que o Mer
- Projeto brasileiro formalizado em 2026 prevê rotas logísticas sul-americanas e retoma expe
- Em julho de 2026, o Brasil solicitou consultas na OMC a respeito de medidas tarifárias nor
- No funcionamento institucional da União Europeia, Parlamento Europeu e Conselho da União E
- A aplicação provisória desde maio de 2026 do acordo comercial interino UE–Mercosul signifi
- Na Carta das Nações Unidas, o art. 41 admite medidas coercitivas sem o uso de forças armad
- No debate sobre o mar, o direito do Estado costeiro sobre a zona econômica exclusiva, que 
- Na agenda de desarmamento, o Tratado de Tlatelolco de 1967 e a ABACC criada em 1991 possue
- No enfrentamento ao terrorismo, a Resolução 1373/2001 do Conselho de Segurança equivale à 
- No sistema financeiro internacional, as quotas de participação no FMI influenciam poder de
- Uma empresa opera satélites durante anos e sustenta que a ocupação prolongada da órbita co
- Na análise de um projeto de pesquisa antártica com equipamentos militares usados apenas pa
- No combate à fome, a Aliança Global lançada na presidência brasileira do G20 em novembro d
```

## Pedido 37: Direito de greve (`direito-trabalho.greve`, 108 tópicos de edital)

```
Matéria: direito-trabalho.greve (Direito de greve)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamento, deliberação e requisitos": 24
- "2. Serviços essenciais e limites do movimento": 30
- "3. Abuso, contrato e greve no serviço público": 23
- "4. Reescrita e como o tema costuma ser cobrado": 23

Já existem (não repita):
- À luz da Lei 7.783/1989, assinale a alternativa correta sobre o conceito de greve.
- Na hipótese geral do art. 3º, frustrada a negociação, qual antecedência mínima de aviso é 
- Em categoria profissional organizada, a assembleia que delibera sobre a paralisação é conv
- O prazo mínimo de comunicação da greve em atividade essencial é de:
- Qual alternativa descreve uma necessidade inadiável da comunidade segundo a Lei 7.783/1989
- Como a participação em greve repercute no contrato de trabalho, observadas as condições le
- Assinale a alternativa correta sobre a falta de entidade sindical na categoria interessada
- Sobre as necessidades inadiáveis durante paralisação em serviço essencial, assinale a INCO
- Na ausência de acordo sobre equipes necessárias para evitar deterioração irreversível, qua
- Uma comissão especialmente eleita representa trabalhadores em negociação e na Justiça do T
- Assinale a alternativa correta sobre os meios de persuasão dos grevistas.
- Assinale a afirmação correta acerca da deliberação sindical sobre a greve.
- Uma paralisação afeta parcialmente apenas determinado setor da empresa. Qual conclusão dec
- Em empresa de distribuição de energia, o sindicato notificou a direção 72 horas antes, mas
- Analise os efeitos de uma greve validamente deflagrada sobre as obrigações do período e as
- Uma greve continua após a celebração de acordo, mas os trabalhadores reivindicam o cumprim
- Servidor civil municipal participa de greve enquanto não existe lei específica disciplinan
- Uma entidade sindical em atividade não essencial observou assembleia estatutária e aviso d
- Trabalhadores de uma atividade essencial notificaram empregador e usuários com 72 horas e 
- Durante acordo coletivo vigente, fatos imprevistos alteram substancialmente a relação de t
- Uma greve começou regularmente, mas permaneceu após decisão da Justiça do Trabalho sobre a
- Em paralisação na rede hospitalar, os trabalhadores respeitam as 72 horas de aviso e mantê
- Um empregado adere à greve; durante a paralisação, a empresa rescinde seu contrato e contr
- Após deliberação de greve em setor essencial, dirigentes sustentam que basta avisar os usu
- Em determinado serviço, a interrupção pode ameaçar a segurança da população, mas os grevis
- Servidores civis e militares de uma mesma unidade anunciam paralisação conjunta. A adminis
- O empregador fecha deliberadamente o estabelecimento para enfraquecer reivindicações e, du
- No curso de uma greve, empregados praticam ato ilícito que também pode constituir crime. Q
- Em atividade essencial, o sindicato comunica a decisão com 72 horas, mas empregador e trab
- Após reivindicações não solucionadas, qualquer das partes ou o Ministério Público do Traba
- Em greve de serviço essencial, a assembleia foi regular e o aviso de 72 horas alcançou usu
- Em mandado de injunção sobre greve de servidores civis, o STF reconheceu omissão legislati
- A Lei 7.783/1989 admite que a suspensão coletiva seja total ou parcial, desde que presente
- Fora das atividades essenciais, o aviso previsto no art. 3º deve ser feito com antecedênci
- Na falta de sindicato, a assembleia dos trabalhadores interessados pode deliberar e consti
- A suspensão contratual durante a greve significa que o vínculo de emprego fica automaticam
- A necessidade inadiável corresponde à situação cuja falta de atendimento coloque em perigo
- A empresa pode constranger os empregados a comparecer ao trabalho, desde que não impeça a 
- Em serviço essencial, o aviso mínimo de 72 horas é dirigido aos empregadores e aos usuário
- A classificação de uma atividade como essencial determina que toda a operação permaneça em
- Durante a greve, as relações obrigacionais são regidas pelo acordo, convenção, laudo arbit
- A manutenção da greve depois de acordo ou decisão trabalhista é sempre abusiva, inclusive 
- Para qualquer greve, inclusive a realizada em atividade não essencial, a comunicação deve 
- Enquanto não editada a lei específica prevista no art. 37, VII, da Constituição, o STF det
- Como a greve suspende o contrato, o art. 7º permite a rescisão e a contratação de substitu
- Se não houver acordo sobre equipes do art. 9º, o empregador pode contratar diretamente os 
- O lockout que busca frustrar negociações é proibido, mas os trabalhadores não têm direito 
```

## Pedido 38: Execução trabalhista: espécies e Fazenda Pública (`processo-trabalho.execucao`, 107 tópicos de edital)

```
Matéria: processo-trabalho.execucao (Execução trabalhista: espécies e Fazenda Pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Títulos, legitimidade, competência e iniciativa da execução": 14
- "2. Execução definitiva, provisória, liquidação e incidentes essenciais": 29
- "3. Prestações sucessivas, vencimentos e efetividade": 21
- "4. Execução contra a Fazenda Pública, RPV e precatórios": 16
- "5. Massa falida, recuperação judicial e coordenação de competências": 20

Já existem (não repita):
- Uma decisão trabalhista transitou em julgado, e o devedor não satisfez o pagamento determi
- Um compromisso de ajuste de conduta firmado perante o Ministério Público do Trabalho prevê
- Após a Reforma Trabalhista, ambas as partes estão representadas por advogado em processo c
- Uma sentença condenatória está pendente de recurso sem efeito suspensivo. Pelo art. 899 da
- Uma obrigação mensal reconhecida por sentença deverá ser cumprida durante exatamente seis 
- O título estabelece prestação mensal sem fixar data final. A execução foi iniciada após tr
- A condenação definitiva é contra uma autarquia de direito público submetida ao art. 100 da
- Uma decisão condena ente de direito público a valor enquadrado no limite legal de pequeno 
- Uma ação trabalhista apurou crédito de empresa que teve falência decretada. Qual tratament
- Uma massa falida possui crédito trabalhista comum de 200 salários mínimos de um mesmo cred
- A sentença trabalhista depende da apuração de diferenças de remuneração definidas por parâ
- A conta foi elaborada e tornada líquida em execução trabalhista. O juízo abre prazo para i
- Em execução ordinária contra empresa privada, após garantia do juízo, o executado pretende
- Ao instaurar incidente de desconsideração da personalidade jurídica em execução trabalhist
- No processo do trabalho, a execução de contribuições sociais decorrentes do objeto de cond
- Uma lei local fixa o pequeno valor de determinado ente dentro dos parâmetros constituciona
- Um precatório judicial transitado em julgado deve ser incluído no orçamento conforme o art
- Uma execução trabalhista decorre de sentença ilíquida. O credor solicita que a liquidação 
- O exequente descumpriu determinação judicial no curso da execução, regularmente comunicada
- Em execução por obrigação de prestações mensais sem data final, ajuizada em maio, o título
- Em execução trabalhista contra ente público de direito público, a parte afirma que a natur
- Uma autarquia pública não exploradora de atividade econômica interpõe recurso na Justiça d
- Foi instaurado incidente de desconsideração durante a execução trabalhista e uma decisão i
- Uma empresa em recuperação judicial foi condenada a diferenças salariais anteriores ao ped
- A empresa teve recuperação judicial deferida e o processo envolve créditos sujeitos ao reg
- Um termo de ajuste de conduta firmado perante o Ministério Público do Trabalho pode ser ex
- Segundo a redação vigente do art. 878 da CLT, o juiz pode iniciar de ofício a execução ord
- Uma condenação trabalhista sujeita a recurso sem efeito suspensivo pode ser objeto de exec
- Nas prestações sucessivas com tempo final determinado, a falta de uma parcela permite exec
- A requisição de pequeno valor depende de o débito estar no teto legal aplicável ao ente pú
- A decretação da falência transfere à Justiça comum a competência de julgar a existência de
- O art. 879 da CLT admite liquidação por cálculos, arbitramento ou artigos, sem permitir al
- Depois de elaborada a conta de liquidação, o art. 879, § 2º, da CLT prevê prazo comum de o
- Garantida a execução ou penhorados bens no regime comum, o art. 884 da CLT concede oito di
- Na execução de obrigações periódicas por tempo indeterminado, o art. 892 da CLT determina 
- Com a EC nº 136/2025, o art. 100, § 5º, da Constituição manteve 2 de abril como corte de a
- Nas hipóteses abrangidas pelo Decreto-Lei nº 779/1969, art. 1º, a Fazenda Pública dispõe d
- Quando sociedade empresária está em recuperação judicial, a habilitação de todo crédito tr
- A instauração de incidente de desconsideração da personalidade jurídica em execução trabal
- No incidente de desconsideração instaurado durante a fase executiva trabalhista, cabe agra
- Se um ente público tem dívida superior ao limite de RPV, o credor pode fracionar livrement
- Pelo art. 83, I, da Lei nº 11.101/2005, a preferência classificatória dos créditos derivad
- A penhora de bem empresarial sujeito ao concurso falimentar pela Justiça do Trabalho é eta
- A recuperação judicial prevê, para créditos e execuções sujeitos ao regime de suspensão do
- O princípio da menor onerosidade permite ao executado impor qualquer meio menos gravoso, m
```

## Pedido 39: Hidrografia (`minas-gerais.hidrografia`, 107 tópicos de edital)

```
Matéria: minas-gerais.hidrografia (Hidrografia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Rede de drenagem, bacias e nascentes": 25
- "2. Bacia do São Francisco e Rio das Velhas": 25
- "3. Doce, Jequitinhonha, Grande e Paraná": 25
- "4. Relações espaciais e como a IDECAN cobra": 25

Já existem (não repita):
- Em uma vertente, dois cursos começam em lados opostos de uma crista. Qual interpretação ex
- Um curso menor desemboca em um rio de maior hierarquia, e ambos seguem até uma saída comum
- Qual afirmação define corretamente uma bacia hidrográfica?
- No vocabulário de drenagem, o que indica a expressão jusante?
- Qual relação hidrográfica está correta para o território mineiro?
- Qual descrição do Rio Doce corresponde à sua trajetória geral?
- Assinale a alternativa INCORRETA sobre a formação do Rio Paraná na região de Minas Gerais.
- Uma equipe compara impactos a montante e a jusante em uma bacia. Qual interpretação é tecn
- Qual reescrita preserva a relação entre o Rio das Velhas e a bacia do São Francisco?
- Uma nascente se localiza perto de um divisor de águas. Para determinar a bacia a que perte
- Sobre a bacia do Jequitinhonha, assinale a afirmação correta.
- Uma carta mostra um curso que nasce em Minas, atravessa áreas mineiras e capixabas e termi
- Qual conclusão respeita as diferenças entre bacia hidrográfica e unidade político-administ
- Assinale a alternativa INCORRETA a respeito da posição do Rio das Velhas.
- Aponte a única sequência correta entre a unidade de drenagem e seus cursos associados.
- Qual fator explica por que uma intervenção em cabeceiras pode ter efeitos em outros trecho
- Um mapa regional registra o Rio Grande e o Paranaíba convergindo na fronteira. O que se fo
- Considere que uma área mineira envia água para o Rio das Velhas, que depois alcança o São 
- Uma análise compara o Rio Doce e o Rio Jequitinhonha. Qual afirmação distingue corretament
- Uma chuva cai sobre os dois lados de uma serra e a carta mostra cursos que chegam a sistem
- Uma proposta de gestão delimita a bacia do Rio Doce apenas dentro de Minas Gerais. Qual re
- Na leitura de um esquema sem escala, o Rio Grande aparece antes de sua confluência com o P
- Uma notícia relata alteração na qualidade da água de um tributário do São Francisco. Qual 
- Assinale a alternativa INCORRETA sobre bacias que drenam Minas Gerais.
- Uma questão descreve uma região de planejamento dentro de uma bacia e pergunta se ela equi
- Se um item afirma que o Rio das Velhas pertence à bacia do São Francisco, mas não é sinôni
- Um atlas destaca três grandes sistemas hidrográficos em Minas. Qual explicação é consisten
- Qual alternativa apresenta uma interpretação coerente de uma sub-bacia inserida na rede do
- A água de uma cabeceira do Rio Grande segue até a confluência com o Paranaíba. Qual afirma
- Ao resumir o papel das nascentes mineiras, qual frase evita uma generalização indevida?
- O Rio das Velhas é tributário do São Francisco.
- Uma bacia hidrográfica é definida pelos limites dos municípios atravessados pelo rio princ
- O Rio Grande e o Rio Paranaíba se unem para formar o Rio Paraná.
- Jusante é a direção que conduz das partes baixas para as cabeceiras do rio.
- O Rio Doce atravessa Minas Gerais e segue para o Atlântico pelo estado do Espírito Santo.
- O Rio Jequitinhonha e o Rio das Velhas pertencem à mesma bacia porque ambos percorrem Mina
- A área de uma sub-bacia pode integrar, em escala maior, a área contribuinte de um rio prin
- A distância em linha reta até o litoral basta para identificar o exutório de qualquer nasc
- A bacia do Rio Doce tem trechos que envolvem mais de um estado.
- O termo montante indica o trecho de um rio situado em direção à sua nascente.
- Como o Rio das Velhas deságua no São Francisco, ambos devem ser classificados como o mesmo
- Uma ação na cabeceira pode reduzir erosão local e beneficiar trechos a jusante, embora não
- O Rio Jequitinhonha é um afluente do Rio Doce antes de ambos chegarem ao oceano.
- Uma divisa política pode coincidir com parte de uma bacia, mas não é por isso que define t
- Toda água que nasce em Minas Gerais pertence ao sistema do Paraná porque o Rio Grande part
```

## Pedido 40: Carreira dos técnico-administrativos em educação (`legislacao.carreira-tecnico-administrativos-educacao`, 107 tópicos de edital)

```
Matéria: legislacao.carreira-tecnico-administrativos-educacao (Carreira dos técnico-administrativos em educação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, abrangência e princípios do PCCTAE": 20
- "2. Estrutura dos cargos, atribuições e ingresso": 20
- "3. Progressão por mérito e aceleração por capacitação": 20
- "4. Incentivo à Qualificação e percentuais de titulação": 20
- "5. Reconhecimento de saberes e competências e integração dos institutos": 20

Já existem (não repita):
- Uma pessoa aprovada para integrar a carreira técnico-administrativa das instituições feder
- Um quadro de pessoal deve apresentar os níveis de classificação dos cargos do PCCTAE, conf
- Uma instituição anuncia provimento de cargo integrante do PCCTAE e precisa indicar a forma
- Uma pessoa completou o interstício de efetivo exercício e obteve o resultado mínimo exigid
- Um integrante do plano pretende acelerar seu desenvolvimento por cursos compatíveis com o 
- Uma pessoa possui escolaridade formal superior àquela exigida no ingresso e pede vantagem 
- Um cadastro precisa distinguir os padrões remuneratórios, em vigor a partir de janeiro de 
- Um integrante do nível de classificação D cumpriu cinco anos de exercício e procura a carg
- Ao comparar a norma original com as alterações de 2025, um relatório identifica qual descr
- Com a tabela de Incentivo à Qualificação de 2025, uma pessoa com doutorado válido, superio
- Em 2026, uma instituição analisa memorial de saberes profissionais pelo RSC-PCCTAE. Qual f
- Uma análise da Lei 15.367/2026 diferencia o limite de beneficiários do RSC da alíquota de 
- Após protocolar requerimento de RSC-PCCTAE, uma pessoa consulta o prazo legal para análise
- Uma orientação institucional sobre o RSC-PCCTAE pretende indicar quando a modalidade foi i
- Um processo seletivo para ingresso no PCCTAE prevê prova escrita e uma etapa de formação. 
- Uma pessoa do nível E completou cinco anos de efetivo exercício e apresenta ações de desen
- Depois de obter uma concessão de RSC-PCCTAE, uma pessoa deseja apresentar novo requeriment
- Uma pessoa já percebe IQ e, no novo processo de RSC, comprova pontuação bastante elevada. 
- Uma progressão anterior a 1º de janeiro de 2025 possuía saldo de tempo ainda não considera
- Um requerimento de RSC foi analisado e concedido somente após o prazo de 120 dias contado 
- Uma pessoa que recebe IQ de mestrado concluiu posteriormente doutorado válido. Ao comparar
- Uma pessoa ingressou em cargo que já exige graduação e apresenta apenas esse mesmo diploma
- Um material antigo oferece percentuais de IQ diferentes conforme relação direta ou indiret
- Um exercício compara as duas denominações de cargo previstas no art. 7º-B e os respectivos
- Um relatório de 2026 afirma que o RSC-PCCTAE tem seis níveis e o último pode fundamentar I
- Uma pessoa recém-empossada no PCCTAE verifica que sua carreira tem regras especiais na Lei
- Uma ficha de enquadramento informa que o PCCTAE possui seis níveis de classificação, de I 
- Uma instituição organiza o ingresso em cargo do PCCTAE no padrão inicial, por concurso de 
- Um integrante da carreira completa doze meses de efetivo exercício e adquire progressão po
- Uma planilha elaborada com a regra vigente desde janeiro de 2025 representa cada nível de 
- Para o nível de classificação D, a tabela de aceleração por capacitação do Anexo III-A exi
- Uma pessoa apresenta graduação formal válida superior à escolaridade mínima de ingresso de
- Ao aplicar a legislação vigente em 2026, uma instituição considera suficiente o interstíci
- Um integrante do nível D concluiu dois eventos válidos de 60 horas cada, compatíveis com s
- Uma pessoa conclui especialização de 360 horas e mestrado, ambos válidos, e solicita cumul
- O RSC-PCCTAE, instituído com efeitos a partir de abril de 2026, consiste em via alternativ
- O limite de 75% do art. 12-C, § 1º, determina que o RSC-PCCTAE seja concedido automaticame
- A comissão competente pelo RSC-PCCTAE tem até 120 dias, contados do protocolo do requerime
- Quando o RSC-PCCTAE é concedido em prazo regular, o art. 12-H determina que seus efeitos f
- Uma pessoa já contemplada por RSC-PCCTAE deverá observar intervalo de três anos depois da 
- Uma pessoa aposentada sem vínculo ativo em efetivo exercício apresenta memorial e, por ate
- Na estrutura da Lei 11.091/2005 após a reforma de 2025, Técnico em Educação e Analista em 
- Uma pessoa que recebe IQ de 25% e obtém pontuação bastante alta no RSC pode avançar direta
- As atribuições gerais da carreira incluem atividades técnicas e especializadas de pesquisa
- No pedido de aceleração por capacitação, a mesma ação formativa de 120 horas pode ser reap
```

## Pedido 41: Avaliação de empresas, custo de capital e renda fixa (`financas.avaliacao-empresas-renda-fixa`, 106 tópicos de edital)

```
Matéria: financas.avaliacao-empresas-renda-fixa (Avaliação de empresas, custo de capital e renda fixa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fluxos de caixa, avaliação de empresas e indicadores operacionais": 18
- "2. Custo de capital, financiamento e estrutura ótima": 18
- "3. Dividendos, múltiplos, EVA, MVA e valor para o acionista": 26
- "4. Renda fixa, títulos públicos e estrutura a termo de juros": 21
- "5. Duration, convexidade, imunização e gestão avançada da carteira": 17

Já existem (não repita):
- Uma empresa tem EBIT de 200, alíquota operacional de 25%, depreciação de 20, Capex de 50 e
- Um avaliador projetou fluxos livres que sobram aos acionistas depois dos pagamentos aos cr
- Uma firma espera fluxo livre de 120 no próximo ano, com crescimento perpétuo de 4% e desco
- Uma avaliação encontra valor operacional da firma de 1.000, dívida financeira de 300 e cai
- Uma companhia apresenta EBIT de 60 e depreciação e amortização de 20. Desconsiderando outr
- No CAPM, uma ação tem beta de 1,2; taxa sem risco de 5% e prêmio esperado do mercado de 6%
- Com estrutura-alvo de 60% de patrimônio a custo de 15% e 40% de dívida a 10%, e benefício 
- Uma expansão elevou contas a receber em 80 e estoques em 30; fornecedores aumentaram 50. Q
- Ao financiar sua expansão, uma firma equilibra o benefício da dedução de juros contra cust
- Uma firma registrou NOPAT de 120 e emprega capital investido de 1.000 a WACC de 10%. Quant
- O valor de mercado do patrimônio é 200, e o lucro líquido atribuível aos acionistas no per
- Numa mensuração coerente de ativos operacionais, o valor de mercado dos direitos sobre uma
- Uma companhia com estatuto omisso quanto ao dividendo obrigatório pretende definir sua dis
- Uma empresa reinveste para crescer, mas os novos projetos oferecem retorno sobre capital d
- Um conjunto de empresas comparáveis apresenta EV/EBITDA normalizado de 8 vezes. A firma av
- Um título sem cupons pagará 1.000 ao final de dois anos. A taxa anual efetiva de mercado é
- Ao classificar títulos federais brasileiros, uma pessoa procura especificamente um instrum
- Em capitalização anual, as taxas spot de um e dois anos são, respectivamente, 8% e 10%. Qu
- Um título zero-cupom vence em três anos e rende 10% efetivos ao ano. Sob a convenção anual
- Uma carteira vale 10 milhões e tem duration modificada 4 e convexidade 25. Para aumento pa
- Uma firma tem dívida de juros flutuantes e deseja mitigar o risco de alta da taxa de refer
- Uma curva soberana mostra taxas de curto prazo acima das taxas longas. Qual interpretação 
- Um fundo pretende imunizar obrigação de valor presente 5 milhões e duration 6. Qual condiç
- Um título privado perde preço, embora toda a curva soberana permaneça estável. A percepção
- A proposição de irrelevância da política de dividendos é invocada por analista para defend
- Uma aplicação prefixada promete pagamento nominal fixo em dois anos. Se a taxa exigida pel
- Uma empresa apresenta EBITDA positivo de 200 e despesas de expansão e giro de 250. Por iss
- Uma empresa projeta o fluxo livre residual aos acionistas depois de dívidas e reinvestimen
- Duas taxas spot anuais, de um e dois anos, permitem inferir uma taxa forward para o segund
- Se o NOPAT de uma companhia supera o produto do WACC pelo capital investido, o EVA desse p
- Um projeto tem risco operacional muito maior que o das atividades tradicionais da companhi
- Uma ampliação de operações aumenta estoques e clientes a prazo em 90 e fornecedores em 30.
- No WACC, o benefício fiscal da dedutibilidade dos juros sobre a dívida autoriza reduzir ta
- O aumento do spread de crédito de uma debênture pode reduzir seu preço mesmo que a taxa li
- Uma companhia com grande dívida apresenta P/L de 12 vezes e EV/EBITDA de 6 vezes. Como amb
- Uma empresa opta por remunerar acionistas via JCP. A avaliação financeira de sua decisão d
- Ao projetar FCFF por cinco anos e calcular a perpetuidade a partir do sexto, o avaliador j
- Uma curva a termo embute forward de dois anos superior às taxas curtas observadas. Isso de
- Para um título convencional de fluxos positivos, a inclusão de convexidade positiva na apr
- Uma instituição iguala a duration de seus ativos à de seus passivos, mas o valor presente 
- No trade-off de estrutura de capital, o endividamento pode inicialmente criar benefício fi
- No CAPM clássico, o beta de uma ação mede diretamente a possibilidade de inadimplência da 
- Em cenário hipotético de mercado de capitais perfeito, sem impostos ou outros atritos e at
- Uma nota prefixada paga cupom de 8% sobre o nominal, mas foi comprada com ágio. O yield at
- Uma empresa deve juros flutuantes e contrata swap para pagar taxa fixa e receber a mesma r
```

## Pedido 42: Tribunais e juízes do trabalho e CSJT (`direito-constitucional.tribunais-juizes-trabalho-csjt`, 106 tópicos de edital)

```
Matéria: direito-constitucional.tribunais-juizes-trabalho-csjt (Tribunais e juízes do trabalho e CSJT)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Órgãos, TST, ENAMAT e CSJT": 34
- "2. TRTs, varas e juízes do trabalho": 22
- "3. Competência da Justiça do Trabalho": 22
- "4. Reescrita e como o tema costuma ser cobrado": 22

Já existem (não repita):
- Assinale a alternativa correta sobre a composição do TST.
- Assinale a alternativa INCORRETA sobre os requisitos pessoais dos Ministros do TST.
- Quem nomeia os Ministros do TST e qual aprovação é exigida?
- Qual alternativa descreve corretamente o quinto constitucional do TST?
- Sobre a ENAMAT e o CSJT, assinale a alternativa correta.
- O art. 111 identifica quais órgãos como integrantes da Justiça do Trabalho?
- Assinale a alternativa correta quanto à composição dos TRTs.
- Para vaga de carreira em TRT, assinale a alternativa que mantém os critérios constituciona
- Sobre a Justiça Itinerante nos TRTs, qual afirmação está correta?
- Qual afirmação distingue corretamente os §§ 1º e 2º do art. 115?
- Em comarca não abrangida pela jurisdição de Vara do Trabalho, qual solução pode ser adotad
- Qual é o objeto reservado à lei pelo art. 113?
- Assinale a alternativa correta sobre a competência geral do art. 114, I.
- Qual hipótese está expressamente incluída na competência da Justiça do Trabalho?
- Assinale a alternativa correta sobre os remédios constitucionais previstos no art. 114, IV
- Entre quais órgãos a Justiça do Trabalho julga conflitos de competência, conforme art. 114
- A Justiça do Trabalho pode processar ação de indenização por dano decorrente da relação de
- Qual alternativa corresponde ao art. 114, VII?
- Sobre a execução de contribuições sociais pela Justiça do Trabalho, assinale a alternativa
- Em greve de atividade essencial, quando pode o MPT ajuizar dissídio coletivo?
- Qual alternativa descreve corretamente outras controvérsias do art. 114, IX?
- Frustrada a negociação coletiva, o que prevê o art. 114, § 1º?
- Quando a controvérsia envolve servidor estatutário e Poder Público, qual conclusão segue a
- Sobre o dissídio coletivo econômico do art. 114, § 2º, assinale a alternativa correta.
- A composição dos TRTs contém a seguinte reserva de vagas:
- Qual é a extensão das decisões vinculantes do CSJT prevista no art. 111-A, § 2º, II?
- Nas Varas do Trabalho, como se exerce a jurisdição?
- Os Ministros do TST oriundos da magistratura de carreira dos TRTs são indicados por qual ó
- Qual é o destino do recurso contra decisão de juiz de direito no exercício de jurisdição t
- Em relação ao dissídio coletivo de greve, qual afirmação está correta?
- As decisões do CSJT têm efeito vinculante no âmbito da Justiça do Trabalho de primeiro e s
- A idade de 35 anos completos atende ao requisito para ser Ministro do TST.
- A ENAMAT tem, entre suas funções, regulamentar cursos oficiais para ingresso e promoção na
- Os TRTs compõem-se de, no mínimo, sete juízes.
- O art. 113 determina diretamente o número de Ministros de cada TRT e a localização de toda
- A Justiça Itinerante nos TRTs é facultativa, enquanto as Câmaras regionais são obrigatória
- Ações de representação sindical entre sindicatos e trabalhadores estão abrangidas pelo art
- Mandado de segurança, habeas corpus e habeas data cabem à Justiça do Trabalho quando o ato
- A Justiça do Trabalho julga causas entre Poder Público e servidor estatutário por força da
- O MPT pode suscitar dissídio coletivo em qualquer greve, ainda que não seja atividade esse
- Após frustrada negociação coletiva, as partes podem eleger árbitros.
- Nas Varas do Trabalho, a jurisdição é exercida por um juiz singular.
- Em dissídio coletivo econômico, a Justiça do Trabalho pode afastar livremente as condições
- A Justiça do Trabalho julga ação sobre penalidade administrativa imposta a empregador por 
- O juiz de direito que atua em comarca sem jurisdição trabalhista encaminha recurso de sua 
```

## Pedido 43: Relevo (`minas-gerais.relevo`, 106 tópicos de edital)

```
Matéria: minas-gerais.relevo (Relevo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planaltos, chapadas e compartimentos do relevo": 25
- "2. Serras e cadeias montanhosas de Minas Gerais": 25
- "3. Depressões, vales e relevo cárstico": 25
- "4. Morfodinâmica fluvial, reescrita e como o tema é cobrado": 25

Já existem (não repita):
- Qual critério descreve melhor um planalto em uma classificação geomorfológica?
- Qual descrição é compatível com uma chapada?
- Uma serra associada à paisagem de Belo Horizonte e ao Quadrilátero Ferrífero é a
- Qual processo está diretamente ligado à formação de formas cársticas em rochas carbonática
- Em um rio, o que tende a ocorrer com parte dos sedimentos quando a energia do fluxo diminu
- Qual definição de depressão geomorfológica está correta?
- Por que a forma do relevo pode influenciar atividades econômicas em Minas Gerais?
- Uma chapada tem topo relativamente plano e bordas escarpadas. Qual processo pode contribui
- Qual alternativa relaciona de modo plausível uma serra mineira à sua importância ambiental
- Qual sequência representa a relação mais comum entre erosão fluvial, transporte e sediment
- Qual associação entre formas cársticas está correta?
- Uma estrada em encosta concentra água pluvial e remove solo de uma vertente. Qual efeito s
- Por que a mineração em áreas serranas pode exigir avaliação geomorfológica?
- Um trecho de vale contém um canal, margens erosivas e terraços acima do leito atual. Qual 
- Na interpretação econômica de chapadas e planaltos, qual afirmação evita determinismo geog
- Qual relação entre estrutura geológica e relevo serrano é plausível?
- Um topo plano e elevado é cortado por vales encaixados. Qual leitura evita confundir forma
- Uma região calcária apresenta dolinas, sumidouros e cavernas ligadas por condutos. Qual in
- Qual alternativa explica a importância de conservar vegetação em vertentes e margens de ri
- Como a erosão diferencial ajuda a explicar cristas e vales em uma serra?
- Uma depressão está situada entre superfícies de planalto, mas permanece em altitude positi
- Uma curva de rio apresenta erosão em uma margem e deposição na margem oposta. Qual explica
- Qual conclusão sobre a Serra do Espinhaço, a Serra do Curral e a Serra da Mantiqueira é ma
- Após desmatamento de encosta, aumenta o sedimento no canal a jusante. Qual sequência causa
- Qual combinação de fatores pode influenciar a capacidade de um rio transportar partículas?
- Um empreendimento em terreno cárstico prevê escavação e armazenamento de rejeitos perto de
- Uma alternativa afirma que toda área montanhosa é economicamente improdutiva por causa da 
- Uma planície aluvial local aparece dentro de um compartimento regional de planalto. Qual a
- Um vale largo apresenta terraços, enquanto um trecho próximo é estreito e encaixado. O que
- Qual reescrita preserva o sentido de que rios podem erodir, transportar e depositar sedime
- A classificação de planalto considera o predomínio de processos erosivos, sem excluir acum
- Uma depressão geomorfológica precisa estar abaixo do nível do mar para receber essa denomi
- Erosão fluvial, transporte e deposição podem ocorrer em trechos diferentes de um mesmo sis
- Toda chapada é uma cavidade subterrânea produzida pela dissolução de calcário.
- A Serra do Curral é uma referência geomorfológica associada à paisagem de Belo Horizonte.
- A sedimentação ocorre somente na foz e nunca aparece em trechos interiores de um rio.
- A remoção de vegetação em encosta pode ampliar o escoamento erosivo, mas o efeito depende 
- Lapiás, dolinas e cavernas são termos diferentes para uma única depressão superficial.
- Uma chapada pode reunir topo relativamente plano, bordas escarpadas e drenagens que entalh
- A circulação em terrenos cársticos pode conectar águas superficiais a condutos e reservató
- O relevo pode condicionar atividades econômicas sem determinar sozinho o uso adequado de c
- A ocorrência de um rio em vale impede erosão das margens e deposição de sedimentos em outr
- A resistência desigual das rochas pode contribuir para a manutenção de cristas e para o re
- Um compartimento classificado como planalto exclui a presença de vales e depósitos fluviai
- A expansão urbana, obras e desmatamento não podem alterar o caminho do escoamento nem a of
```

## Pedido 44: Auditoria de sistemas de tecnologia da informação (`informatica.auditoria-sistemas-ti`, 105 tópicos de edital)

```
Matéria: informatica.auditoria-sistemas-ti (Auditoria de sistemas de tecnologia da informação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, riscos e ciclo da auditoria de sistemas": 17
- "2. Controles gerais de TI e controles de aplicação": 21
- "3. ITAF, ISSAI 5300 e GUID 5100: normas e condução do trabalho": 21
- "4. Análise de dados em auditoria de finanças e políticas públicas": 22
- "5. Auditoria de bancos de dados, configurações e relatórios": 19

Já existem (não repita):
- Uma equipe vai avaliar alterações de favorecidos em sistema de pagamentos ocorridas de jan
- Um processo permite que a mesma pessoa cadastre e aprove fornecedores, aumentando a exposi
- Ao verificar controles de TI, uma equipe identifica que contas permanecem habilitadas após
- Em uma aplicação de pagamentos, uma regra impede concluir uma ordem cujo identificador e c
- Um relatório técnico de auditoria cita a ISSAI 5300 como orientação internacional ainda vi
- Uma política exige que todo lote enviado contenha relatório de registros aceitos, rejeitad
- Em uma auditoria de desembolsos, um cruzamento encontrou dez linhas com a mesma chave de n
- Uma equipe compara uma tabela de pagamentos à esquerda com uma tabela de cadastros à direi
- No PostgreSQL, o parâmetro log_statement foi configurado com o valor ddl. Qual classe de c
- Uma política prevê aprovação segregada antes de cada implantação. O auditor recebeu a polí
- O auditor examina se falhas de um sistema são recuperadas em quatro horas e se a perda de 
- Em auditoria de um sistema de concessões, será empregado referencial que orienta planejame
- Para verificar se todos os pagamentos de um semestre foram extraídos, o auditor obtém 5.00
- Um teste dirigido selecionou 40 transações entre as de maior risco e encontrou oito incons
- Em determinado sistema, um controle automático bloqueia pagamento acima do limite de alçad
- Para auditar SELECT e INSERT em determinada tabela, um administrador criou uma database au
- Um relatório de conciliação mostra 970 linhas aceitas e 30 rejeitadas em lote original de 
- Uma auditoria de implantação de software examinou apenas a configuração atual da produção 
- Um trabalho usa a GUID 5100 e o ITAF para estruturar testes de auditoria de sistemas. A eq
- Uma auditoria cruza pagamentos de 2023 com cadastro de beneficiários extraído somente em 2
- Uma auditoria examina uma amostra de registros com seleção baseada nos maiores valores e e
- O banco apresenta parâmetro de logging ativado e arquivo de logs com eventos administrativ
- Uma instituição adotou CIS Controls v8 e seu safeguard 8.10. A equipe deseja concluir que 
- A área responsável afirma que determinado programa teve impacto positivo porque o número d
- Um relatório financeiro de banco de dados totaliza corretamente 2 milhões, mas os favoreci
- Ao avaliar um sistema de pagamentos, identificar a regra de aprovação prevista e confrontá
- Uma política de senha aprovada e arquivada comprova, por si só, que todos os acessos a um 
- A revogação de acesso quando uma pessoa deixa de utilizar o sistema é exemplo de controle 
- Um bloqueio automático que impede a gravação de uma transação indevida é classificado como
- Uma consulta LEFT JOIN entre pagamentos e cadastros pode conservar linhas de pagamentos se
- No PostgreSQL, configurar log_statement=ddl assegura o registro de todo comando SELECT com
- Em uma análise de riscos de pagamentos, foi classificada como risco de controle a possibil
- Ao revisar uma migração de aplicação, a equipe identifica código implantado diretamente em
- A ISSAI 5300 continua relacionada como pronunciamento vigente, e a GUID 5100 foi retirada 
- O ITAF, em sua quinta edição, é referencial profissional de auditoria de tecnologia que tr
- Uma rotina auditou todos os registros presentes no arquivo extraído de pagamentos. Essa co
- Em uma auditoria de banco, é adequado comparar permissões efetivas de usuários e papéis ao
- Em SQL Server, a existência de uma database audit specification recém-criada é evidência s
- Num trabalho sobre a efetividade de serviço público, dados de aumento de atendimentos pode
- Em análise de pagamentos, a soma financeira reconciliada garante que os favorecidos e os i
- Uma seleção de registros feita para concentrar pagamentos atípicos pode sustentar exame ap
- O Safeguard 8.10 dos CIS Controls v8 institui, por si só, obrigação legal brasileira de re
- Uma pessoa com privilégios máximos no banco consegue alterar os dados auditados e apagar a
- Numa auditoria de continuidade, a mensagem automática ‘backup concluído’ demonstra que a a
- Ao relatar uma possível duplicidade identificada por script, a equipe documentou critérios
```

## Pedido 45: Materiais de construção e estruturas de concreto armado, protendido, pré-moldado e aço (`engenharia.estruturas-concreto-aco-materiais`, 105 tópicos de edital)

```
Matéria: engenharia.estruturas-concreto-aco-materiais (Materiais de construção e estruturas de concreto armado, protendido, pré-moldado e aço)
Capítulos e quantas questões quero em cada um (total 100):
- "Materiais de construção: propriedades, dosagem e controle tecnológico": 17
- "Concreto armado: dimensionamento de vigas, lajes e pilares e durabilidade": 17
- "Concreto protendido e concreto pré-moldado: projeto, detalhamento e montagem": 17
- "Estruturas de aço e madeira: perfis, ligações, estabilidade e proteção": 17
- "Execução estrutural: fôrmas, escoramentos, esquadrias e inspeção": 16
- "Pontes e integração dos sistemas estruturais: noções para concursos": 16

Já existem (não repita):
- Uma viga contínua recebe carregamento gravitacional. Junto a um apoio interno há momento f
- Um aparelho de apoio móvel de ponte pode permitir deslocamento longitudinal e, simultaneam
- A resistência característica fck é, por definição, o valor da resistência do primeiro corp
- No armazenamento e na aplicação de ligantes asfálticos e outros materiais betuminosos, qua
- Ao detalhar ligação parafusada de chapas em aço estrutural, uma verificação necessária, al
- O cobrimento nominal de armaduras em concreto armado é definido primordialmente consideran
- Por ser um material não combustível, o aço estrutural mantém invariáveis a resistência e o
- Em viga simplesmente apoiada com carga vertical gravitacional, a região central usualmente
- Para blocos e telhas cerâmicas, resultados de absorção de água e presença de poros podem s
- Uma empresa fabrica viga pré-moldada e quer liberar sua movimentação. Qual documento técni
- Sobre a verificação de um pilar esbelto de concreto armado, assinale a afirmação correta.
- Toda peça pós-tracionada emprega obrigatoriamente cordoalhas não aderentes ao concreto.
- A pré-tração caracteriza-se pelo tensionamento das armaduras ativas antes da concretagem e
- Em uma ponte rodoviária, a função estrutural dos aparelhos de apoio é principalmente:
- Na obra, foram previstos 180 kg de água e 360 kg de cimento por metro cúbico. Sem consider
- Uma equipe inspeciona blocos cerâmicos com absorção acima do limite especificado para o pr
- No concreto armado, os estados limites de serviço são irrelevantes quando a segurança ao c
- A flambagem pode governar a capacidade resistente de barras de aço comprimidas antes que o
- Ao projetar uma ligação entre pilar e viga pré-moldados, um engenheiro deve verificar:
- Duas vigas de madeira idênticas geometricamente são instaladas com diferentes orientações 
- O comportamento mecânico dos materiais betuminosos pode depender tanto da temperatura como
- O ensaio de abatimento do tronco de cone avalia a consistência de concreto fresco apropria
- Todo pilar de concreto armado pode ser dimensionado exclusivamente para compressão centrad
- Durante concretagem de uma laje, o responsável técnico deve avaliar o sistema de fôrmas e 
- Sobre um concreto destinado a ambiente de exposição agressiva, assinale a prática tecnicam
- Em uma ligação parafusada, a resistência dos parafusos é a única verificação necessária, i
- Fluência e retração do concreto, assim como relaxação do aço, podem produzir perdas de pro
- Ao planejar concretagem de parede alta com concreto fresco, qual atitude é tecnicamente ma
- Qual sequência corresponde à pré-tração de uma peça de concreto protendido?
- No estudo de aglomerantes para argamassas e concretos, qual alternativa identifica uma pro
- Quanto maior a quantidade de água adicionada ao concreto, maior será necessariamente sua r
- A capacidade de uma viga pré-moldada em sua posição definitiva dispensa o cálculo de solic
- A pressão lateral do concreto fresco em fôrmas verticais independe de velocidade de lançam
- Numa viga mista aço–concreto, qual componente costuma viabilizar a transmissão de cisalham
- Em peça protendida, qual alternativa apresenta exclusivamente mecanismos possíveis de perd
- O comportamento mecânico da madeira é influenciado pela direção das fibras e pelo teor de 
- A umidade superficial dos agregados pode alterar a relação água/cimento efetiva da mistura
- Ao comparar duas argamassas de revestimento com resistências elevadas, um fiscal observa d
- Em um pórtico de aço com pilares comprimidos, qual fenômeno pode reduzir a capacidade de u
- Após instalar uma janela de correr, surgem infiltrações na interface entre o marco e a alv
- As fôrmas moldam o concreto fresco, enquanto o sistema de cimbramento e escoramento suport
- A retirada de escoras deve ocorrer sempre no mesmo número fixo de dias, sem considerar res
- Um laboratório apresenta resultados de abatimento e de compressão axial de corpos de prova
- Uma obra pretende usar pós-tração aderente em vigas. Qual característica é compatível com 
- O desempenho de estanqueidade de uma esquadria depende do sistema e de sua instalação; não
- A verificação à punção é especialmente relevante em lajes diretamente apoiadas sobre pilar
- Para o dimensionamento de pontes rodoviárias, considerar apenas peso próprio e revestiment
- Em laje lisa de concreto armado apoiada diretamente em pilares, a verificação local que me
- A infiltração por juntas danificadas de uma ponte pode acelerar deterioração junto a apare
- No projeto de tabuleiro de ponte sujeito ao tráfego, qual conjunto de solicitações é coere
```

## Pedido 46: Matéria, misturas e métodos de separação (`ciencias.materia-misturas-separacao`, 105 tópicos de edital)

```
Matéria: ciencias.materia-misturas-separacao (Matéria, misturas e métodos de separação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Matéria e propriedades": 17
- "2. Estados físicos e mudanças de estado": 22
- "3. Substâncias puras e misturas": 14
- "4. Métodos de separação de misturas": 19
- "5. Soluções, solubilidade e dispersões": 14
- "6. Como o tema costuma ser cobrado": 14

Já existem (não repita):
- Uma mistura contém 20 g de sólido insolúvel, 30 g de soluto dissolvido e 100 g de água. Ap
- Em uma decantação, 190 g de fase líquida estão disponíveis junto a um sólido sedimentado. 
- A solubilidade de um sólido é 20 g por 100 g de água. Adicionam-se 35 g desse sólido a 150
- Uma amostra tem massa 120 g e volume 40 cm³. Qual é sua densidade?
- Um líquido tem densidade 0,80 g/mL. Qual é a massa de 250 mL desse líquido?
- Uma peça de material tem massa 270 g e densidade 0,90 g/mL. Qual é seu volume?
- Um corpo de massa 180 g ocupa 200 cm³ e é colocado em um líquido de densidade 0,95 g/cm³. 
- A solubilidade de um sólido é 36 g por 100 g de água a certa temperatura. Ao adicionar 110
- Dissolvem-se 15 g de um sólido em 50 g de solvente, formando uma solução saturada. Qual so
- Uma solução de massa total 200 g contém 12% de soluto em massa. Qual massa de soluto está 
- Misturam-se 20 g de sal a 180 g de água, sem perda de material. Qual é a porcentagem em ma
- Uma solução contém 40 g de soluto por litro. Se 250 mL dessa solução forem evaporados comp
- Uma operação de destilação recupera 85% de 400 mL de solvente de uma mistura. Qual volume 
- Em uma dissolução fracionada, 45 g de um componente são dissolvidos seletivamente e depois
- Uma mistura contém 30 g de um sólido-alvo. Após a separação, foram recuperados 27 g desse 
- A solubilidade de um sólido é 50 g por 100 g de solvente. Em 300 g de solvente, adicionam-
- Dissolvem-se 25 g de soluto em 175 g de solvente. Qual é a porcentagem em massa de soluto 
- Pelo deslocamento de água, um sólido de massa 75 g desloca o nível de 50 mL para 80 mL. Qu
- Uma mistura contém areia insolúvel, sal e água. A amostra inicial tem 200 g; a filtração r
- A solubilidade de um soluto a certa temperatura é 25 g por 100 g de água. Uma amostra tem 
- Uma mistura de areia, sal e água contém três componentes e, antes de separar, apresenta qu
- Para separar uma mistura de álcool e água por diferenças de volatilidade, o método escolar
- Em uma curva de aquecimento de substância pura a pressão constante, qual interpretação cab
- Julgue a afirmação: para obter cristais de um sólido dissolvido numa solução aquosa, pode-
- Julgue a afirmação: uma substância simples é formada por átomos de apenas um elemento quím
- Julgue a afirmação: uma amostra de areia e limalha de ferro pode ser separada por imantaçã
- Uma amostra de 250 g ocupa 100 cm³. Sua densidade média vale
- Julgue a afirmação: durante a fusão de uma substância pura a pressão constante, a temperat
- Julgue a afirmação: a decantação pode separar uma mistura heterogênea de sólido sedimentad
- Julgue a afirmação: dissolver sal em água é necessariamente uma transformação química porq
- Uma amostra contém água líquida e gelo em equilíbrio. Quantos componentes e quantas fases 
- Para separar limalha de ferro de areia seca, o método mais direto explora a diferença de
- Uma mistura homogênea pode conter mais de uma substância, apesar de apresentar apenas uma 
- Uma propriedade geral, como massa, costuma bastar para distinguir uma substância de todas 
- A evaporação pode ser usada para deixar no recipiente um soluto não volátil que estava dis
- A filtração comum separa sal completamente dissolvido em água, retendo os íons no papel de
- Ebulição e evaporação são formas de vaporização, mas diferem pelas condições e pela região
- Um sistema com água e óleo imiscíveis forma duas fases líquidas distinguíveis após repouso
- Na destilação, o vapor recolhido é condensado para recuperar uma fração mais volátil da mi
- Uma solução saturada, sob temperatura e pressão definidas, já alcançou o limite de soluto 
- Uma amostra tem massa 120 g e volume 40 cm³. Sua densidade média é
- Uma amostra pura de substância composta difere de uma substância simples porque
- Para separar dois líquidos imiscíveis que formam camadas, uma escolha apropriada é
- Um coloide pode parecer uniforme a olho nu e ainda dispersar um feixe de luz por causa das
- Na fusão de uma substância pura sob pressão constante, a energia absorvida durante o patam
- Na dissolução, as partículas do soluto deixam de existir e são convertidas necessariamente
- Uma mistura formada por areia, água e sal dissolvido contém três componentes e duas fases 
- A centrifugação pode acelerar a separação de partículas sólidas suspensas num líquido ao f
- A composição de uma mistura pode variar de uma amostra para outra sem que cada mistura dei
- Uma mistura de líquidos miscíveis com pontos de ebulição diferentes pode ser separada por 
- A massa de uma amostra é uma propriedade extensiva, pois depende da quantidade de matéria 
- Uma mistura heterogênea precisa apresentar componentes sólidos visíveis; líquidos em camad
- Substância pura sempre ferve a uma temperatura fixa, qualquer que seja a pressão externa a
- Na levigação, uma corrente de líquido pode arrastar partículas menos densas enquanto partí
- Uma solução supersaturada contém menos soluto dissolvido do que a solução saturada nas mes
- Uma amostra visualmente uniforme é necessariamente substância pura e não pode conter solut
- A cromatografia pode separar componentes de uma mistura porque eles apresentam afinidades 
- A destilação simples pode ser útil para separar um soluto não volátil de um solvente, reco
- No patamar de mudança de estado de uma substância pura sob pressão constante, a energia ab
- Peneiração é apropriada quando componentes sólidos apresentam tamanhos de partículas difer
- Um sistema com duas fases de água, uma líquida e outra sólida, contém duas substâncias com
```

## Pedido 47: SPED, documentos fiscais eletrônicos, eSocial e EFD-Reinf (`contabilidade-tributaria.sped-documentos-fiscais-eletronicos`, 105 tópicos de edital)

```
Matéria: contabilidade-tributaria.sped-documentos-fiscais-eletronicos (SPED, documentos fiscais eletrônicos, eSocial e EFD-Reinf)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos do SPED e Escrituração Contábil Digital": 17
- "2. Escriturações fiscais: ECF, EFD ICMS/IPI e EFD-Contribuições": 20
- "3. NF-e, NFC-e, ECF e emissão de documentos": 21
- "4. CT-e, MDF-e, transporte e controle fiscal de circulação": 16
- "5. eSocial, EFD-Reinf, DCTFWeb, MIT e DARF numerado": 26

Já existem (não repita):
- Uma empresa deseja identificar qual instrumento foi instituído para unificar recepção, val
- Uma sociedade está preparando para transmissão os livros Diário e Razão e os balancetes di
- Uma companhia pretende saber se o envio de arquivo fiscal validado pelo programa comprova 
- Uma pessoa jurídica calcula ajustes fiscais que influenciam a base de IRPJ e CSLL, sem pre
- Uma indústria reúne registros de entradas, saídas, inventário e apuração relativos ao ICMS
- Um atacadista emite arquivo XML autorizado para documentar venda de mercadorias a outra em
- Um estabelecimento varejista credenciado precisa emitir documento fiscal eletrônico ao con
- O destinatário de uma mercadoria recebe o DANFE impresso. Para identificar o documento fis
- Uma empresa transportadora presta serviço rodoviário para levar carga pertencente a um cli
- A equipe contábil de uma empresa consulta orientações oficiais de 2026 para apresentar a E
- Uma empresa que encerrou sua existência por incorporação em agosto examina o prazo especia
- Uma sociedade submetida a situação normal de apresentação da ECF quer distinguir seu prazo
- Uma empresa está escriturando operações que influenciam PIS/Pasep e Cofins, como receitas 
- Uma NFC-e de R$ 12.000,00 será emitida a consumidor final em operação regulada pelo Ajuste
- Durante auditoria de procedimentos históricos do Distrito Federal, surgiu a afirmação de q
- Um contribuinte que emite CT-e encerrou o carregamento do veículo e vai iniciar a viagem a
- Uma empresa possui veículo próprio para transportar mercadorias que vendeu, sem prestar tr
- Uma empresa não obrigada automaticamente à ECD por enquadrar-se em regra de dispensa cogit
- Uma pessoa jurídica foi extinta em setembro e precisa entregar a ECF correspondente ao eve
- Uma transportadora fará uma única viagem com descarregamento de mercadorias em dois estado
- Uma empresa contratou transportadora para carregar mercadoria coberta por NF-e. O sistema 
- Uma nota de serviços com retenções previdenciária, IRRF, CSLL, PIS e Cofins foi emitida e 
- Uma empresa corrigiu evento da EFD-Reinf depois do fechamento, mas a DCTFWeb original já h
- Uma empresa informa folha no eSocial e serviços tomados na EFD-Reinf, e tem tributos adici
- Um grupo empresarial possui matriz e três filiais e está consolidando débitos declarados p
- Uma empresa emitiu documento fiscal válido, mas deixou de transmiti-lo na escrituração per
- Uma sociedade deseja cumprir a ECD e, para isso, prepara somente a apuração do lucro tribu
- No varejo, uma operação abrangida pelas normas da NFC-e pode ser documentada por arquivo e
- Uma transportadora apresentou o DACTE impresso. Esse documento auxiliar, por ter acompanha
- Uma empresa comercial informa na EFD ICMS/IPI documentos de entradas e saídas e registros 
- Ao admitir empregado, o empregador deve informar essa ocorrência na EFD-Reinf como evento 
- O Decreto Distrital nº 26.529/2006 continua vigente como base normativa atual do Livro Fis
- Uma NF-e emitida em XML, conforme leiaute técnico e após credenciamento do emissor, precis
- Uma pessoa jurídica optante pelo Simples Nacional está, apenas por adotar esse regime, obr
- Um consumidor solicita identificação em NFC-e referente a operação abaixo de R$ 10.000,00.
- Uma sociedade sem fatos a informar à EFD-Reinf em determinado período deve transmitir fech
- Uma matriz com filiais consolida informações de folha e retenções. Na DCTFWeb, a entrega e
- A empresa que transmitiu DCTFWeb pode recolher seu FGTS pela mesma parcela de FGTS incluíd
- Uma empresa tem livros contábeis digitais e uma declaração com ajustes tributários de lucr
- Uma transportadora emite MDF-e modelo 58 para a viagem. Por esse motivo, o conhecimento de
- Uma nota de serviços foi emitida e creditada em abril, mas paga em maio. A EFD-Reinf pode 
- Uma sociedade informou contribuições pela EFD-Reinf e precisa declarar outros tributos não
- Após transmitir DCTFWeb, uma empresa identifica retenção previdenciária errada na EFD-Rein
- Uma sociedade sofreu cisão em fevereiro do ano corrente. Segundo a orientação oficial de 2
- Uma companhia escriturou integralmente PIS/Pasep e Cofins na EFD-Contribuições e concluiu 
```

## Pedido 48: Processo no Tribunal de Contas: sanções, cautelares e deliberações (`controle-externo.processo-tribunal-contas-sancoes-cautelares`, 105 tópicos de edital)

```
Matéria: controle-externo.processo-tribunal-contas-sancoes-cautelares (Processo no Tribunal de Contas: sanções, cautelares e deliberações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos do controle externo e organização dos tribunais de contas": 16
- "2. Modalidades processuais, formalização, atos e instrução": 25
- "3. Julgamento, tipos de deliberação e incidentes de uniformização": 24
- "4. Responsabilização, sanções e medidas cautelares": 14
- "5. Recursos, pedido de rescisão, prazos e prescrição": 21

Já existem (não repita):
- Um relatório de controle aponta que o TCU auxiliou a fiscalização externa da União. À luz 
- Após receber as contas anuais do Presidente da República, o Tribunal deve praticar o ato d
- Ao organizar um material sobre a estrutura constitucional do Tribunal de Contas da União, 
- Uma tomada de contas identifica pagamento sem contraprestação e quantifica possível prejuí
- Em ajuste financiado com recursos federais, não foi comprovada a aplicação de parte dos va
- A instrução encontrou falha de natureza formal nos demonstrativos anuais, sem dano à Fazen
- Antes de decidir sobre a regularidade de contas, o relator determina diligência complement
- Um acórdão definitivo imputou débito após defesa e quantificação de prejuízo. O que a Cons
- Desastre natural comprovadamente alheio à conduta do gestor destruiu documentação essencia
- Uma decisão definitiva contém ponto essencial omitido no dispositivo, e o interessado pret
- Responsável pretende contestar decisão de julgamento de contas por razões de mérito e fazê
- Quatro anos após uma decisão definitiva de contas, surge documento novo capaz de modificar
- Um responsável questiona decisão de fiscalização inserida nas hipóteses do art. 48 da Lei 
- Gestor foi julgado em débito de R$ 80 mil, atualizado, e a decisão cogita aplicar multa vi
- Durante auditoria, o responsável sonega documentos requisitados, prejudicando o trabalho t
- Diante de grave infração cometida por responsável, o colegiado avalia aplicar inabilitação
- Após comprovação de fraude em licitação, pretende-se declarar a inidoneidade do licitante 
- Há indícios de que responsável possa ocultar bens suficientes ao ressarcimento durante a a
- Constatada ilegalidade em contrato administrativo federal, o Tribunal assina prazo para co
- Um interessado regularmente citado não apresenta defesa. A instrução registra ausência de 
- Em processo de controle externo que não trata de registro de aposentadoria, o responsável 
- Um processo de contas está paralisado por mais de três anos, pendente de julgamento, sem a
- Credor público pretende cobrar judicialmente débito inscrito em acórdão definitivo do Trib
- Em julgamento colegiado, ministros identificam decisões anteriores divergentes sobre quest
- A lei orgânica de um tribunal de contas estadual prevê pedido de rescisão, enquanto a lei 
- Durante fiscalização de gastos federais, a análise alcança legalidade, legitimidade e econ
- Ao receber as contas anuais presidenciais, o TCU deverá julgá-las definitivamente em sesse
- Em planejamento de sessão plenária, registra-se que o Tribunal de Contas da União possui n
- Uma tomada de contas especial somente pode ser aberta depois de processo criminal com denú
- Um gestor devidamente citado não comparece nem apresenta defesa. O art. 12, § 3º, da Lei n
- Ao determinar citação e diligência antes do julgamento, o relator profere decisão definiti
- Falha meramente formal, sem dano ao erário, pode conduzir a contas regulares com ressalva,
- Em razão de força maior alheia à vontade do responsável, a prova desapareceu e a avaliação
- Após trancamento de contas iliquidáveis, novos documentos suficientes podem ensejar desarq
- Uma consulta sobre interpretação de regra de controle pode produzir resposta normativa que
- Na formalização de acórdão de contas, relatório, fundamentação e dispositivo são elementos
- Quando a corte julga um responsável em débito, a multa do art. 57 da Lei nº 8.443/1992 ser
- A Lei nº 8.443/1992 autoriza afastamento temporário de responsável quando haja indícios su
- Ao interpor recurso de revisão contra decisão definitiva, o interessado obtém efeito suspe
- Em fiscalização alcançada pelo art. 48 da Lei nº 8.443/1992, cabe pedido de reexame com ef
- Os embargos de declaração apresentados em dez dias, no processo de contas federal, interro
- Diante de divergência entre decisões anteriores do Tribunal, o art. 91 do Regimento Intern
- A disciplina de prescrição da Resolução-TCU nº 344/2022 inclui os processos de apreciação 
- Um título executivo formado por decisão de tribunal de contas não transforma a pretensão j
- Se o regimento de uma corte estadual mencionar pedido de rescisão, ele deverá seguir obrig
```

## Pedido 49: Legislação e ética farmacêutica (`farmacia.legislacao-etica-farmaceutica`, 104 tópicos de edital)

```
Matéria: farmacia.legislacao-etica-farmaceutica (Legislação e ética farmacêutica)
Capítulos e quantas questões quero em cada um (total 100):
- "Marco legal do comércio e controle sanitário": 17
- "Farmácia como estabelecimento de saúde e responsabilidade técnica": 17
- "Medicamentos sujeitos a controle especial e receituários": 17
- "Escrituração, SNGPC, SNCR e dispensação segura": 17
- "Genéricos, similares e intercambialidade": 16
- "Código de Ética Farmacêutica e situações aplicadas": 16

Já existem (não repita):
- A Lei Federal nº 5.991/1973 disciplina o controle sanitário do comércio de medicamentos, i
- Após a Lei nº 13.021/2014, a Lei nº 5.991/1973 deixou integralmente de integrar o regime d
- O Decreto Federal nº 74.170/1974 regulamenta a Lei nº 5.991/1973 e deve ser interpretado c
- Uma drogaria regular, desde que tenha farmacêutico no local, pode manipular rotineiramente
- A regularidade comercial da empresa, por si só, substitui a autorização e o licenciamento 
- A Lei nº 13.021/2014 caracteriza a farmácia como unidade de prestação de serviços vinculad
- Basta designar um farmacêutico como responsável técnico no cadastro: sua presença física o
- O proprietário pode desautorizar orientações técnicas do farmacêutico quando a dispensação
- A responsabilidade técnica implica atuação efetiva, mas uma irregularidade de terceiro não
- A Portaria SVS/MS nº 344/1998 utiliza listas que podem ser alteradas por atualizações ofic
- As listas A3 e B1 se referem a psicotrópicos e, por essa razão, exigem necessariamente a m
- A alteração dos modelos impressos em 2026 invalidou automaticamente todas as notificações 
- No controle especial, a apresentação de receita em papel permanece possível mesmo após a e
- Fotografia de uma receita física enviada por aplicativo de mensagens equivale, por si só, 
- O SNGPC acompanha a movimentação de medicamentos sujeitos a controle especial e de antimic
- O registro de utilização de uma notificação eletrônica no SNCR elimina automaticamente a o
- Em uma divergência de estoque de medicamento controlado, criar uma saída fictícia para aju
- O regime de prescrição e retenção de antimicrobianos não deve ser automaticamente confundi
- Todo similar registrado pode substituir qualquer genérico que contenha o mesmo princípio a
- Um similar declarado intercambiável pela Anvisa pode substituir o medicamento de referênci
- O nome comercial impresso na embalagem prova que se trata de medicamento de referência, nã
- A autonomia profissional permite ao farmacêutico decidir de forma fundamentada pela não di
- O Código de Ética Farmacêutica admite que o profissional empreste seu nome para cobrir for
- A apuração de infração ético-disciplinar pode dispensar contraditório e ampla defesa se o 
- Uma drogaria apresenta licença comercial e CNPJ, mas não possui a licença sanitária exigív
- Qual par normativo estabelece a base tradicional do controle sanitário do comércio de medi
- Em que situação o estabelecimento exerce atividade típica de farmácia com manipulação?
- Quanto à documentação de uma farmácia, assinale a afirmação adequada.
- O que melhor expressa a transformação promovida pela Lei nº 13.021/2014?
- O proprietário exige a venda imediata de medicamento de controle especial com receituário 
- Uma farmácia mantém registro de RT, mas funciona por longos períodos com apenas caixas e a
- Qual atividade é manifestação típica da assistência farmacêutica, e não simples ato de ven
- Um laboratório produz um novo receituário físico controlado após 18 de maio de 2026. Qual 
- A qual opção se associa, em linhas gerais, a lista B1 da Portaria nº 344/1998?
- Uma questão afirma que a lista C2 está vinculada, em regra, a:
- O que é correto afirmar sobre os modelos antigos impressos antes da vigência obrigatória d
- Um medicamento da lista C1 é apresentado com receita comum quando a norma exige Receita de
- Em auditoria, é preciso diferenciar SNCR e SNGPC. Qual par está correto?
- Na conferência mensal, o estoque físico de controlados diverge da escrituração. Qual condu
- Um auditor observa retenção de receita para antimicrobiano e conclui que o produto pertenc
- Um prescritor produz uma notificação eletrônica controlada em sistema integrado. Qual alte
- Qual definição distingue corretamente medicamento similar de genérico?
- Qual das opções descreve substituição admitida, atendidos os demais requisitos técnicos e 
- Um medicamento similar e um genérico foram individualmente comparados ao mesmo referência.
- Qual consulta é mais adequada para confirmar se um similar é intercambiável com determinad
- O Código de Ética Farmacêutica adotado como referência vigente foi aprovado por qual ato?
- Um profissional considera uma prescrição tecnicamente insegura, discute a situação e docum
- Em processo ético, a comissão recebe notícia de possível falha do farmacêutico responsável
- Qual situação caracteriza violação ética mais evidente no exercício da responsabilidade té
- O farmacêutico conhece informação sensível de um paciente e é solicitado a divulgá-la em p
```

## Pedido 50: Fiscalização ambiental (`meio-ambiente.fiscalizacao-ambiental`, 104 tópicos de edital)

```
Matéria: meio-ambiente.fiscalizacao-ambiental (Fiscalização ambiental)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Poder de polícia ambiental, competências e planejamento": 20
- "2. Infrações, sanções e medidas acautelatórias no Decreto nº 6.514/2008": 20
- "3. Vistoria, inspeção, autos e relatório técnico": 20
- "4. Processo sancionador, prescrição e dosimetria de multas": 20
- "5. Queimadas, incêndios florestais e conexão com crimes ambientais": 20

Já existem (não repita):
- A infração administrativa ambiental pode decorrer tanto de ação quanto de omissão contrári
- Por integrar o Sisnama, qualquer servidor de uma prefeitura pode lavrar auto de infração a
- A finalidade preventiva da fiscalização é compatível com ações educativas e planejamento d
- O Decreto federal nº 6.514/2008 disciplina infrações e sanções administrativas ambientais 
- A advertência é etapa necessariamente anterior à aplicação de qualquer multa ambiental fed
- A apreensão de bens utilizados em infração ambiental produz automaticamente, no instante d
- O embargo, quando juridicamente cabível, pode ter finalidade de impedir a continuidade da 
- O termo interdição, utilizado na rotina fiscal, dispensa indicar se a providência juridica
- Em vistoria baseada em alerta de satélite, o foco de calor é indício suficiente, por si só
- Relatório que apresenta localização, método de medição, provas, limitações e nexo entre fa
- A nulidade de todo auto de infração é consequência necessária de qualquer erro de enquadra
- Uma fotografia com indicação de local e data substitui obrigatoriamente a descrição do fat
- O contraditório e a ampla defesa devem ser assegurados no processo administrativo sanciona
- Pelo Decreto nº 6.514/2008, transcorrido o prazo regulamentar de julgamento sem decisão, o
- O prazo de vinte dias para defesa, previsto no procedimento federal do Decreto nº 6.514/20
- Na esfera federal, a Lei nº 9.873/1999 prevê, em regra, prescrição punitiva quinquenal e h
- A autoridade julgadora deve fixar o valor da multa sempre no limite máximo, pois o porte e
- A TCFA é tributo vinculado ao exercício do poder de polícia e não se confunde com multa ap
- A Lei Estadual nº 7.910/2025, do Amazonas, prioriza ações preventivas, tecnologia de monit
- A Lei nº 14.944/2024 instituiu a Política Nacional de Manejo Integrado do Fogo, o que impl
- Uma mesma ocorrência pode exigir medidas administrativas, reparação civil e apuração de cr
- Uma equipe estadual pretende estudar quais ocorrências fiscalizar primeiro. Qual critério 
- Entre as opções abaixo, qual documento dá início à formalização da imputação de uma infraç
- Um agente constata atividade capaz de ampliar imediatamente uma degradação. Qual providênc
- Sobre a competência fiscalizatória ambiental no Brasil, assinale a afirmação correta.
- Um carregamento de produto florestal é apreendido em operação. Qual conclusão é juridicame
- Qual das opções descreve adequadamente a função de uma advertência no Decreto nº 6.514/200
- Na vistoria, dois arquivos de imagem mostram margens diferentes do mesmo curso d’água, sem
- Para o procedimento sancionador federal disciplinado no Decreto nº 6.514/2008, o prazo ord
- Um candidato identifica artigo que fixa faixa de multa e pergunta como escolher um valor. 
- Em uma fiscalização, uma empresa apresenta boleto de TCFA quitado e alega que isso a autor
- Conforme a Lei Estadual nº 7.910/2025, do Amazonas, constitui diretriz da fiscalização amb
- Sobre o relatório de fiscalização, indique a prática mais adequada em caso de medição de á
- A defesa aponta que o auto descreve corretamente a conduta, mas cita número errado de disp
- Sobre o recurso administrativo referente à sanção de multa no Decreto nº 6.514/2008, assin
- A Administração Federal deixou um processo sancionador pendente de despacho ou julgamento 
- Em exercício hipotético, uma norma prevê multa de R$ 2.000 por hectare de determinada infr
- Um terreno apresenta vestígios de queimada e histórico de vários proprietários. Qual é a a
- Qual é a diferença essencial entre multa ambiental administrativa e reparação civil do dan
- Durante incêndio florestal, o órgão recebe mapa de focos de calor e fotos de moradores. Qu
- A Lei nº 14.944/2024 apresenta como linha de atuação da Política Nacional de Manejo Integr
- Uma denúncia refere lançamento de efluente, e a equipe precisa justificar eventual sanção.
```

