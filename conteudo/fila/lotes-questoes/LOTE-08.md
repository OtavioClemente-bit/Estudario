# Pedidos de questões extras, lote 8 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 16056 tópicos de edital.

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

## Pedido 1: Regime Próprio de Previdência dos Servidores (art. 40 da CF, EC nº 103/2019 e Lei nº 9.717/1998) (`direito-previdenciario.regime-proprio-servidores`, 359 tópicos de edital)

```
Matéria: direito-previdenciario.regime-proprio-servidores (Regime Próprio de Previdência dos Servidores (art. 40 da CF, EC nº 103/2019 e Lei nº 9.717/1998))
Capítulos e quantas questões quero em cada um (total 100):
- "1. O que é o RPPS e quem está nele": 20
- "2. Aposentadorias: regras permanentes": 20
- "3. Pensão, acumulação, tempo e teto": 20
- "4. Regras de transição da EC 103 (servidores da União)": 20
- "5. Fundos, controle e responsabilidade (Lei nº 9.717/1998)": 20

Já existem (não repita):
- Segundo o art. 40 da Constituição, com a redação da EC nº 103/2019, o regime próprio de pr
- Ao agente público ocupante, exclusivamente, de cargo em comissão declarado em lei de livre
- Após a EC nº 103/2019, quanto à existência de regimes próprios de previdência social, é co
- Pela Lei nº 9.717/1998, a contribuição do ente federativo ao regime próprio de seus servid
- O servidor titular de cargo efetivo de um Estado, filiado ao seu regime próprio, cedido a 
- Segundo a Lei nº 9.717/1998, os regimes próprios não podem conceder benefícios distintos d
- Pelo art. 40, § 1º, da Constituição, a aposentadoria compulsória do servidor ocorre:
- A aposentadoria por incapacidade permanente do servidor exige que ele seja insuscetível de
- No âmbito da União, a aposentadoria voluntária pela regra permanente da EC nº 103/2019 exi
- Pela regra permanente aplicável aos servidores federais após a EC nº 103/2019, além da ida
- Uma servidora federal se aposenta pela regra permanente com 30 anos de contribuição. Pelo 
- Os proventos de aposentadoria do regime próprio não poderão ser inferiores ao salário míni
- Admite-se, por lei complementar do respectivo ente, a fixação de idade e tempo de contribu
- A redução de cinco anos na idade mínima de aposentadoria aplica-se aos professores que com
- O servidor que completou as exigências para a aposentadoria voluntária e optou por permane
- Ressalvadas as aposentadorias decorrentes de cargos acumuláveis na forma da Constituição, 
- Sobre a contagem de tempo no regime próprio, a Constituição estabelece que:
- O teto remuneratório do art. 37, XI, da Constituição aplica-se à soma total dos proventos 
- Na União, após a EC nº 103/2019, a pensão por morte de servidor corresponde a uma cota fam
- Pela Constituição, a lei do ente que disciplina a pensão por morte deve tratar de forma di
- O regime de previdência complementar que os entes federativos devem instituir para seus se
- Incidirá contribuição sobre os proventos de aposentadorias e pensões do regime próprio que
- A regra de transição da EC nº 103/2019 com pedágio de 100% para servidores federais exige,
- Na regra de transição por pontos da EC nº 103/2019 para servidores federais, exigem-se, al
- Nas regras de transição da EC nº 103/2019, a integralidade e a paridade são reservadas, ob
- O servidor que, antes da entrada em vigor da EC nº 103/2019, já havia cumprido todos os re
- Ana, servidora federal desde 2010, tem 59 anos, 32 anos de contribuição, 14 anos de serviç
- Segundo a Lei nº 9.717/1998, é facultada aos entes a constituição de fundos previdenciário
- O descumprimento da Lei nº 9.717/1998 por Estados, Distrito Federal e Municípios implica, 
- O documento emitido pela União que atesta o cumprimento, pelos Estados, Distrito Federal e
- Pela Lei nº 9.717/1998, os dirigentes da unidade gestora do regime próprio devem ter forma
- Pelos prejuízos decorrentes de aplicação de recursos do regime próprio em desacordo com a 
- No caso de extinção de regime próprio, o ente federativo assume integralmente a responsabi
- As alíquotas de contribuição dos servidores ativos dos Estados, do Distrito Federal e dos 
- É vedada a adoção de requisitos ou critérios diferenciados para concessão de benefícios em
- Quanto ao reajuste dos benefícios do regime próprio concedidos sem paridade, a Constituiçã
- Na regra de transição com pedágio, se em 13/11/2019 faltavam a um servidor federal 4 anos 
- Pela Lei nº 9.717/1998, as contribuições e os recursos vinculados aos fundos previdenciári
- Os ocupantes de cargo efetivo que ingressaram no serviço público antes da instituição do r
- Nos Estados, no Distrito Federal e nos Municípios, a idade mínima para a aposentadoria vol
```

## Pedido 2: Classificações orçamentárias, estrutura programática e emendas (`afo.classificacoes-programas-emendas`, 356 tópicos de edital)

```
Matéria: afo.classificacoes-programas-emendas (Classificações orçamentárias, estrutura programática e emendas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e classificações orçamentárias": 20
- "2. Receita orçamentária: categorias e fontes": 20
- "3. Despesa orçamentária: função e natureza": 20
- "4. Estrutura programática e outros planos": 20
- "5. Emendas parlamentares e execução": 20

Já existem (não repita):
- Uma planilha informa qual unidade orçamentária recebeu dotação para manter determinado ser
- Ao montar demonstrativo de uma despesa federal, analista precisa identificar a esfera orça
- Um Município recebe R$ 300 mil pela venda de terreno de sua propriedade. Como classificar 
- O orçamento prevê pagamento de juros e de principal de dívida pública. Qual descrição da n
- Uma unidade mantém de forma contínua os serviços regulares de atendimento, sem prazo de en
- Ao registrar receita tributária, uma pessoa identifica o último dígito da natureza como in
- Uma despesa de comunicação social é executada em política de agricultura. A combinação da 
- Um ente recebe caução para garantir contrato, com obrigação de devolvê-la ao contratado ap
- Um gestor deseja conferir se determinada receita pode financiar uma atividade, em razão de
- A unidade realiza compra direta de computadores, sem transferir recursos a outro ente ou e
- Uma ação pública será realizada durante 18 meses para construir nova unidade de atendiment
- Uma ação pretende entregar 80 unidades habitacionais com dotação de R$ 16 milhões. Em rela
- Uma emenda individual ao projeto orçamentário de 2026 é analisada sob a redação constituci
- Um órgão planeja transferência obrigatória associada a emenda de bancada estadual. Qual li
- Durante a preparação do orçamento, uma proposta apresenta programa para reduzir evasão esc
- Um órgão recebe pedido para classificar aquisição de imóvel destinado especificamente à re
- No orçamento federal, uma transferência para outro ente financia serviços prestados por es
- Uma emenda à LOA propõe abrir nova despesa por anulação de outra dotação. Qual dotação não
- Uma emenda individual prevê repasse por transferência especial a município. Do total receb
- Uma emenda individual impositiva possui objeto definido, mas sua execução encontra impedim
- Uma bancada estadual apresenta nove emendas sem previsão de continuidade de obras, cada um
- Uma pessoa consulta a programação de 2022 e aplica mecanicamente a mesma denominação do PP
- Uma comissão permanente deseja apresentar emenda de despesa voltada a ação genérica sem ob
- Uma emenda individual foi paga por transferência especial, sem celebração de convênio. Ao 
- Uma ação de ampliação de cobertura atende a várias localidades, cada uma identificada por 
- Ao consultar a LOA federal, uma pessoa encontra os orçamentos fiscal, da seguridade social
- Em uma análise da execução federal, afirmou-se que a subfunção da despesa deve necessariam
- Uma prefeitura arrecadou recursos com tributos e registrou o ingresso na categoria de rece
- Ao receber depósito de garantia de contratado, com obrigação de restituir o valor, o ente 
- O pagamento de juros e encargos da dívida pública classifica-se no GND 2 e na categoria ec
- A modalidade de aplicação da despesa indica o bem ou serviço comprado e, por essa razão, s
- Uma ação de construção com prazo de início e término definidos e objetivo de ampliar a cap
- O subtítulo de uma ação orçamentária federal é uma nova unidade orçamentária que recebe pe
- A Lei Orçamentária Anual estima receitas e fixa despesas, enquanto o PPA e a LDO exercem p
- A aprovação de emenda individual impositiva obriga o desembolso integral da programação in
- Na codificação por natureza da receita do MTO 2022, o último dígito pode discriminar arrec
- Um órgão classifica como receita de capital a soma positiva entre receitas e despesas corr
- O MTO 2022 distinguia a programação quantitativa física, voltada às metas de entrega de be
- Como a ação de operação especial não produz bens ou serviços diretamente na União, é impos
- Para o regime constitucional de emendas individuais após a EC nº 126/2022, o percentual de
- Uma proposta de emenda de comissão aprovada em 2026 poderia, pela LC nº 210/2024, possuir 
- A LC nº 210/2024 prevê até oito emendas ordinárias por bancada estadual e trata separadame
- Quando uma transferência especial constitucional alcança município, o mínimo de 70% das de
- A classificação do MTO 2022 entre funções e subfunções é independente do desenho dos progr
- Uma emenda classificada como RP 6 comprova, por si só, a efetiva entrega do produto públic
```

## Pedido 3: Odontologia: saúde coletiva, biossegurança e diagnóstico (`odontologia.saude-coletiva-biosseguranca-diagnostico`, 356 tópicos de edital)

```
Matéria: odontologia.saude-coletiva-biosseguranca-diagnostico (Odontologia: saúde coletiva, biossegurança e diagnóstico)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Saúde coletiva, epidemiologia e prevenção odontológica": 23
- "2. Biossegurança, processamento e resíduos de serviços odontológicos": 20
- "3. Diagnóstico odontológico e exames complementares": 18
- "4. Diagnóstico de risco, plano de tratamento e continuidade do cuidado": 18
- "5. Auditoria e perícia odontológicas, ética e qualidade assistencial": 21

Já existem (não repita):
- Em um levantamento de saúde bucal, uma pessoa tem dois dentes permanentes cariados, um per
- Uma criança apresenta lesão branca de esmalte sem cavitação e risco de cárie elevado. Qual
- Durante consulta, um profissional coloca luvas, mas deixa de higienizar as mãos antes e de
- Um consultório descarta uma agulha usada após anestesia local. De acordo com a classificaç
- Uma pessoa relata dor dentária e o exame térmico produz resposta. O profissional pretende 
- Uma pessoa apresenta cavidades recorrentes e consumo frequente de açúcar. Ao elaborar plan
- Um serviço contratante analisa se um procedimento odontológico foi indicado, documentado e
- Uma clínica pretende usar estufa de calor seco para esterilizar instrumentais utilizados e
- Uma pessoa tem ferida de mucosa que persiste, com área endurecida e aumento gradual. Qual 
- Em um levantamento, quatro pessoas apresentam CPO-D individuais de 2, 4, 6 e 8. Qual é a m
- Um consultório utiliza autoclave assistida por bomba de vácuo. Ao começar o dia, qual test
- Uma clínica necessita definir o controle biológico de sua rotina de esterilização conforme
- Uma clínica mantém registros do monitoramento da esterilização por dois anos e os elimina 
- Uma pessoa apresenta gengiva aumentada e profundidade de sondagem de 5 mm. Qual informação
- Um serviço possui cinco radiografias periapicais semelhantes solicitadas sem mudança clíni
- Uma pessoa aceita um tratamento após receber explicação das alternativas, dos riscos relev
- Um auditor encontra cobrança de procedimento com descrição clínica incompleta. Sem prova d
- Em serviço que dispõe de esterilizador a vapor assistido por bomba de vácuo, um responsáve
- Um consultório usa caixas de luz ultravioleta para guardar brocas contaminadas e as declar
- Uma pesquisa comunitária compara dois bairros. O bairro A tem CPO-D médio maior e poucas l
- Uma radiografia periapical não revela alteração evidente, mas a pessoa tem dor persistente
- Em um serviço odontológico, exames radiológicos são realizados com aparelho cuja sala teve
- Um perito recebe fotografias de restauração atual e a alegação de que o defeito surgiu apó
- Uma clínica informa grande número de restaurações anuais e o utiliza como prova única de m
- Uma operadora contrata pessoa sem inscrição profissional odontológica para emitir julgamen
- Em levantamento com dentes permanentes, o CPO-D pode incluir dentes cariados, perdidos por
- No atendimento odontológico, o uso de luvas de procedimento permite dispensar higiene das 
- Uma agulha utilizada em anestesia odontológica deve ser gerenciada como perfurocortante, p
- Como o teste térmico pulpar mede diretamente perfusão dos vasos da polpa, uma resposta nor
- Uma lesão inicial de cárie em esmalte, ainda sem cavidade, pode receber abordagem não oper
- Se não houver registro de um procedimento no prontuário, o auditor está autorizado a concl
- Na RDC nº 1.002/2025, o art. 84 proíbe estufas e caixas de luz ultravioleta para esteriliz
- Conforme o art. 89 da RDC nº 1.002/2025, o monitoramento com indicador biológico e integra
- Um consultório odontológico com autoclave assistida por bomba de vácuo deve realizar Bowie
- O art. 92 da RDC nº 1.002/2025 permite descartar registros de monitoramento de esterilizaç
- Segundo a RDC nº 1.002/2025, a atualização do levantamento radiométrico de serviço odontol
- A profundidade de sondagem periodontal de 6 mm corresponde necessariamente a 6 mm de perda
- Na elaboração de plano para paciente com repetidas lesões de cárie, medidas de controle de
- Uma clínica informa que o pacote de esterilização ficou íntegro após uso anterior e por is
- Uma clínica possui dois esterilizadores e registra apenas a data da carga, omitindo lote e
- Quando uma pessoa apresenta úlcera de mucosa persistente, endurecida e de crescimento prog
- A Resolução CFO nº 235/2021 considera perícia e auditoria odontológicas atividades que req
- Um perito vê fotografia atual de restauração fraturada e pode, apenas pela imagem, conclui
- Uma unidade compara taxas de restaurações em dois territórios com necessidades muito difer
- A liberação da carga de instrumentos pela autoclave dispensa avaliação da limpeza prévia p
```

## Pedido 4: Passivo e patrimônio líquido (`contabilidade.passivo-patrimonio-liquido`, 355 tópicos de edital)

```
Matéria: contabilidade.passivo-patrimonio-liquido (Passivo e patrimônio líquido)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Passivo: conceito, classificação, fornecedores e outras obrigações": 20
- "2. Mensuração de dívidas, empréstimos e encargos financeiros": 17
- "3. Provisões, passivos contingentes e ativos contingentes": 13
- "4. Patrimônio líquido: capital, reservas, ajustes e ações em tesouraria": 27
- "5. Destinação do resultado, dividendos e integração dos lançamentos": 23

Já existem (não repita):
- Uma entidade recebeu mercadorias de R$ 24.000, pagáveis em 60 dias, e as manteve em estoqu
- No balanço, uma empresa apresenta ativos de R$ 840.000 e passivos exigíveis de R$ 510.000.
- Uma empresa recebeu R$ 70.000 de empréstimo bancário, sem encargos iniciais, e o dinheiro 
- Uma entidade sofreu processo por fato passado e, ao fechar o balanço, avaliou como prováve
- Uma ação contra a empresa decorre de evento passado, mas a perda foi avaliada como apenas 
- Sócios subscreveram capital de R$ 180.000 e integralizaram imediatamente R$ 130.000 em din
- Uma companhia transferiu R$ 12.000 de lucros disponíveis para uma reserva de lucros já exi
- Uma sociedade recomprou suas próprias ações por R$ 18.000 e ainda as mantinha na data de e
- Uma entidade reconheceu no fechamento R$ 20.000 de dividendos obrigatórios exigíveis, aind
- Um financiamento de R$ 120.000 será liquidado com R$ 35.000 de principal nos próximos 12 m
- Em dezembro, a entidade apropriou R$ 2.000 de juros de dívida ainda não pagos. Em janeiro,
- Uma provisão por garantia envolve 60 produtos sem reparo, 30 com reparo de R$ 80 e 10 com 
- Uma empresa é autora de ação judicial. A entrada de R$ 75.000 é classificada como provável
- Uma companhia emitiu ações e recebeu R$ 155.000, dos quais R$ 125.000 foram destinados ao 
- Na data do balanço, a companhia tinha obrigação presente de R$ 40.000 de dividendo obrigat
- Uma empresa havia reconhecido R$ 14.000 de dividendos a pagar no exercício anterior. Neste
- Uma entidade recebe R$ 36.000 de cliente antes de entregar bens ou prestar serviços. Não h
- Em orçamento interno, a administração prevê prejuízo operacional de R$ 60.000 no próximo a
- Uma indústria possui ciclo operacional normal de 18 meses e dívida com fornecedor diretame
- Uma obrigação contratual paga R$ 121.000 ao final de dois anos, sem fluxo intermediário, d
- Uma sociedade pretende separar R$ 30.000 de lucros para enfrentar redução futura de result
- A empresa adquiriu ações próprias por R$ 22.000 e, mais tarde, alienou exatamente essas aç
- Após 31 de dezembro, a administração propôs e os titulares aprovaram dividendo adicional d
- O PL inicial era R$ 320.000. Houve lucro de R$ 80.000, aporte em dinheiro de R$ 25.000, re
- Uma remuneração de capital foi deliberada como juros sobre capital próprio pagáveis: valor
- Uma obrigação presente de pagar por mercadorias já recebidas pode constituir passivo mesmo
- O recebimento de empréstimo bancário de R$ 90.000 eleva imediatamente o lucro em R$ 90.000
- Uma obrigação presente derivada de evento passado, com saída provável e estimativa confiáv
- Uma perda avaliada como remota exige reconhecimento integral de provisão passiva, porque o
- Quando a entidade constitui reserva de lucros por transferência de lucros disponíveis, sem
- Ações da própria entidade recompradas em bolsa devem ser classificadas no ativo circulante
- Se salários foram incorridos em dezembro e pagos em janeiro, reconhecê-los em dezembro com
- Quando a entidade amortiza R$ 10.000 de principal de empréstimo que já estava reconhecido,
- Uma perda judicial avaliada como possível, mas não provável, normalmente não gera provisão
- No CPC 25, uma entrada de benefícios econômicos apenas provável deve ser reconhecida como 
- O excedente pago por subscritores na emissão de ações, quando não destinado ao capital soc
- A destinação de lucro a reserva de lucros, por si só, exige lançamento de despesa na DRE, 
- Se na data do balanço o dividendo obrigatório constitui obrigação presente, seu reconhecim
- Um passivo cujo valor de liquidação poderá ser reajustado é automaticamente passivo contin
- Quando o efeito do dinheiro no tempo é material, o CPC 25 requer mensurar a provisão pelo 
- Uma obrigação operacional liquidável dentro do ciclo normal de produção, mesmo que esse ci
- Se ações em tesouraria adquiridas por R$ 9.000 forem vendidas posteriormente por R$ 11.000
- A ICPC 08 (R1) estabelece que tanto o dividendo obrigatório já exigível quanto o dividendo
- Em uma companhia com PL inicial de R$ 150.000, lucro de R$ 40.000, capital integralizado a
- Um dividendo adicional aprovado em fevereiro, sem obrigação presente em dezembro, deve obr
```

## Pedido 5: Garantias eleitorais, votação, apuração e diplomação (`direito-eleitoral.garantias-votacao-apuracao-diplomacao`, 355 tópicos de edital)

```
Matéria: direito-eleitoral.garantias-votacao-apuracao-diplomacao (Garantias eleitorais, votação, apuração e diplomação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Liberdade do voto, garantias pessoais e repressão à violência": 22
- "2. Transporte rural, contenção do abuso e condutas vedadas": 25
- "3. Atos preparatórios, mesa receptora e procedimento de votação": 19
- "4. Apuração, boletim de urna, totalização e controle do resultado": 17
- "5. Diplomação, registro sub judice e eleições suplementares": 17

Já existem (não repita):
- Durante a votação, um particular coloca correntes no portão de uma escola para impedir a e
- Uma eleitora ameaça denunciar que foi coagida a votar, e pede proteção dois dias antes da 
- Quatro dias antes do pleito, uma autoridade pretende deter eleitor por mera investigação n
- Um fiscal partidário, no exercício das atividades na seção, é alvo de ordem de prisão por 
- Moradores de comunidade rural solicitam transporte eleitoral gratuito até uma seção situad
- No domingo de votação, um ônibus de linha regular, não fretado, mantém a rota urbana usual
- Um chefe de repartição determina que servidor do Executivo faça atendimento em comitê elei
- Uma candidatura promete custear a reforma da casa de eleitor em troca de apoio eleitoral, 
- Nos três meses anteriores a uma eleição, a administração pretende autorizar campanha insti
- Antes da abertura de uma seção eletrônica, fiscais acompanham a emissão de relatório desti
- Eleitor apresenta documento válido, mas quer levar celular desligado para dentro da cabina
- Após encerramento da votação eletrônica, um fiscal compara dados divulgados por seção com 
- Terminada a eleição estadual, uma chapa eleita recebe a documentação formal de sua escolha
- Candidata alcança votação suficiente para vencer, mas seu registro está indeferido e há re
- Em pleito majoritário, decisão eleitoral afasta o registro do candidato eleito e seus voto
- O Ministério Público instaura procedimento preparatório eleitoral para investigar uso de m
- Em ano eleitoral, a administração continua programa social autorizado por lei e executado 
- Dez dias antes da eleição, candidata é procurada por policiais que desejam prendê-la por o
- Uma autoridade pretende nomear pessoa aprovada em concurso homologado antes de começar a j
- No primeiro semestre eleitoral, determinado ente empenha publicidade em valor acima de sei
- Candidato oferece tratamento odontológico em troca de voto, depois do registro de candidat
- Em vacância decorrente da desconstituição da eleição de titular de cargo executivo municip
- Quinze dias antes do pleito, a Justiça Eleitoral publica quadro de percursos rurais. Grupo
- Candidatura proporcional recebe votos enquanto seu registro está indeferido por decisão ai
- Um eleito é diplomado antes do último dia fixado para as cerimônias de 2026. A defesa adve
- Diante de ameaças dirigidas a eleitor por causa de seu voto, o presidente da mesa receptor
- Se o transporte rural gratuito não for disponibilizado no município, o eleitor automaticam
- Antes da chegada dos primeiros eleitores à urna, a mesa, após conferir material e integrid
- Um eleitor pode ingressar na cabina com telefone celular desde que o dispositivo esteja de
- Após ter registrado a candidatura, um concorrente promete emprego a uma eleitora para obte
- Nos três meses anteriores à eleição, candidato pode comparecer à inauguração de obra públi
- Candidato abordado por autoridade dez dias antes da eleição possui proteção contra prisão,
- Dentro dos três meses anteriores à eleição, pode ser nomeada pessoa aprovada em concurso p
- Uma localidade rural situada a apenas um quilômetro de sua mesa receptora tem direito ao t
- Depois de encerrada a seção eletrônica, fiscal consegue conferir o resultado apurado na ur
- Na apuração excepcional por cédulas, constatada diferença entre a quantidade de votantes e
- Ao examinar notícia de propaganda com uso de recursos públicos, o Ministério Público pode 
- O registro de um candidato proporcional foi indeferido por decisão recorrível; por isso, t
- Após a diplomação de candidato eleito em 2026, adversário propõe recurso contra expedição 
- Decisão posterior à diplomação altera a distribuição de cadeiras de eleição proporcional. 
- Se uma vacância simultânea na presidência e vice-presidência da República ocorrer a dezoit
- Candidata é ameaçada por mensagens que menosprezam sua condição de mulher para impedi-la d
- O registro de um candidato vencedor de eleição majoritária é invalidado, mas seus votos re
- Uma autoridade utiliza distribuição de benefícios sociais custeados pelo poder público par
- Na eleição geral de 2026, a diplomação das candidaturas eleitas deve ocorrer até 18 de dez
```

## Pedido 6: Citologia e genética (`biologia.citologia-genetica`, 353 tópicos de edital)

```
Matéria: biologia.citologia-genetica (Citologia e genética)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organização celular e composição química": 18
- "2. Membranas, organelas e processos celulares": 22
- "3. Ciclo celular, mitose, meiose e gametogênese": 19
- "4. Material genético, expressão, mutações e biotecnologia": 21
- "5. Hereditariedade mendeliana, grupos sanguíneos e padrões de herança": 20

Já existem (não repita):
- Em uma amostra microscópica observam-se DNA livre no nucleoide, ribossomos e ausência de c
- Uma célula mantém gradientes elétricos e utiliza fosfatos na formação de nucleotídeos. Qua
- Uma análise detecta três polímeros de glicose em tecidos: reserva em folhas, reserva em mú
- Um componente de membrana possui uma região polar e cadeias apolares, organizando-se em bi
- Após aquecimento, uma enzima perde atividade e altera seu dobramento, embora a cadeia de a
- Ao adicionar uma enzima a uma reação possível, a velocidade aumenta sem mudança da composi
- Uma molécula atravessa a membrana por canal específico, seguindo seu gradiente eletroquími
- Hemácias são colocadas em solução cuja concentração efetiva de solutos não penetrantes é m
- Uma proteína destinada à secreção precisa ser modificada, classificada e enviada em vesícu
- Células de um tecido apresentam elevada demanda de ATP e intensa respiração com oxigênio. 
- Uma célula somática humana completa a fase S e entra em G2 sem iniciar a separação das cro
- Um preparador observa que cromossomos homólogos migram para polos opostos, enquanto cromát
- Ao comparar a formação de gametas humanos, um estudante precisa explicar por que quatro es
- Uma fita molde de DNA possui a sequência 3'-TAC-5'. Qual transcrito correspondente é produ
- Uma alteração pontual substitui um códon de RNA por outro que especifica o mesmo aminoácid
- Uma alteração cromossômica consiste na retirada de um segmento e sua reinserção na mesma r
- Duas plantas heterozigotas Aa são cruzadas sob dominância completa. Qual a chance de um de
- No cruzamento AaBb × AaBb, com dominância completa e genes independentes, qual a probabili
- Um genitor tem sangue A com genótipo IAi; outro tem sangue B com genótipo IBi. Sob o model
- Uma gestante Rh-negativa não possui anticorpos anti-D e o feto é Rh-positivo. Qual afirmaç
- Uma condição recessiva ligada ao X é estudada em casal com mãe portadora XAXa e pai não af
- Dois genitores sem sinais de doença têm um descendente afetado por condição monogênica de 
- Na análise de paternidade, um suposto pai e uma criança coincidem em apenas um marcador ST
- Um laboratório amplifica um fragmento específico de DNA sem inserir esse fragmento em célu
- Em uma linhagem germinativa ocorre não disjunção que produz gameta com dois exemplares de 
- Uma pessoa apresenta deficiência de uma vitamina hidrossolúvel utilizada como precursora d
- Em um experimento com uma bactéria e um vírus, a ausência de núcleo organizado nas duas am
- Quando uma célula animal é transferida para meio efetivamente hipotônico, pode ocorrer ent
- Uma célula vegetal exposta a meio hipotônico comporta-se exatamente como uma hemácia, pois
- Em uma célula secretora, ribossomos associados ao retículo rugoso sintetizam proteínas que
- Ao entrar em G2 depois de completar a fase S, uma célula diploide já se tornou tetraploide
- Na prófase I da meiose, cromátides não irmãs de cromossomos homólogos podem trocar trechos
- Na ovogênese humana típica, o ovócito secundário liberado na ovulação já completou a meios
- Quando o ribossomo lê um RNA mensageiro, cada códon de três nucleotídeos funciona como uni
- A transcrição e a replicação são equivalentes porque ambas produzem obrigatoriamente duas 
- Para um caráter autossômico recessivo, dois genitores heterozigotos Aa podem produzir, em 
- Na segunda lei de Mendel, o resultado fenotípico 9:3:3:1 de AaBb × AaBb permanece garantid
- No cruzamento IAi × IBi, a descendência pode apresentar grupos sanguíneos A, B, AB e O, ca
- Um genitor de grupo AB e outro de grupo O podem ter descendente de grupo O pela segregação
- Para explicar a eritroblastose fetal por anti-D, é necessário considerar se houve sensibil
- Uma condição recessiva ligada ao X pode passar de pai afetado para filho XY mesmo quando a
- Uma substituição de base em região codificante pode ser silenciosa se o novo códon continu
- Uma inversão cromossômica e uma trissomia pertencem ambas à classe das mutações estruturai
- Em um exame de parentesco baseado em múltiplos STRs, a transmissão de um alelo materno e u
- Um organismo obtido por engenharia genética com alteração do próprio DNA, mas sem receber 
- Na fotossíntese, o oxigênio liberado para a atmosfera tem origem na:
- Na respiração celular aeróbia, a maior parte do ATP é produzida na cadeia respiratória, lo
- A fermentação lática, que ocorre em células musculares sob baixa disponibilidade de oxigên
- A quimiossíntese, realizada por bactérias como as nitrificantes, permite a produção de mat
```

## Pedido 7: Criminologia (`seguranca-publica.criminologia`, 352 tópicos de edital)

```
Matéria: seguranca-publica.criminologia (Criminologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, método, objetos, controle social e vitimologia": 29
- "2. Escolas clássica e positivista e explicações individuais": 12
- "3. Teorias sociológicas: Chicago, anomia, aprendizagem e subculturas": 14
- "4. Etiquetamento, crítica, seletividade e tendências contemporâneas": 17
- "5. Prevenção primária, secundária, terciária e modelos de reação": 28

Já existem (não repita):
- Uma pesquisadora entrevista vítimas, observa rotinas comunitárias e compara registros para
- Uma comunidade desenvolve regras de convivência transmitidas pela família e pela escola, s
- Uma pesquisa detecta episódios de violência que os participantes afirmam não ter comunicad
- Uma pessoa assaltada sofre novos danos ao ser ridicularizada e culpabilizada durante o ate
- Um curso de história das ideias penais examina autores que defenderam limites à punição ar
- Um pesquisador histórico atribui a delinquência a características corporais inatas e procu
- Uma análise encontra fragilidade das redes de vizinhança, alta rotatividade de moradores e
- Uma pessoa aprende técnicas e justificativas para fraude mediante convivência próxima e co
- Um programa amplia educação e serviços básicos para toda a população, buscando atuar sobre
- Um jovem aceita como meta cultural o enriquecimento, mas passa a utilizar meios proibidos 
- Uma pessoa investiga o efeito de mudanças sociais aceleradas que enfraquecem as regras com
- Em uma escola, certos adolescentes praticam atos sem ganho financeiro, buscando reconhecim
- Uma estudante sofre rótulo público persistente após infração inicial e, diante de exclusão
- Uma pesquisa diferencia a criação legislativa de uma incriminação e a escolha concreta de 
- Uma cidade aplica medidas focalizadas de apoio e proteção a adolescentes identificados por
- Após cumprir sanção, um egresso passa a receber assistência de educação, trabalho e recons
- Em sessão proposta para responder a dano, a vítima informa que não deseja comparecer. A co
- Duas regiões possuem números oficiais semelhantes de roubos, mas pesquisas de vitimização 
- Um examinador apresenta a anomia durkheimiana como fragilidade da regulação social e a teo
- Em estudo ecológico, bairros com maior rotatividade residencial apresentam mais registros 
- Uma administração treina algoritmo de risco usando apenas pessoas anteriormente abordadas.
- Um gestor defende o uso intenso de sanções para pequenas infrações e afirma que essa polít
- Um relatório investiga por que infrações de agentes socialmente privilegiados têm menor ex
- Uma proposta combina reparação voluntária às vítimas, reinserção de pessoas egressas e red
- Participantes de procedimento restaurativo não chegam a acordo, após sessões voluntárias c
- Uma investigação criminológica que combina observação de campo, entrevistas e dados de pes
- Uma campanha que melhora o registro dos crimes faz necessariamente diminuir a cifra oculta
- A tradição da Escola Clássica associa legitimidade da punição a limites legais e proporcio
- Na teoria da associação diferencial de Sutherland, a conduta criminal é determinada por si
- Investimento geral em educação e fortalecimento dos serviços comunitários pode integrar a 
- Na visão criminológica de Lemert, desvio secundário designa exclusivamente o segundo crime
- Um bairro pode apresentar fragilidade de laços comunitários relacionada a riscos coletivos
- Na adaptação denominada ritualismo por Merton, o indivíduo mantém metas culturais de enriq
- Na abordagem de etiquetamento, a reação social pode alterar a trajetória de pessoas seleci
- Criminalização primária descreve a escolha concreta de pessoas a serem abordadas e process
- Um programa focalizado em pessoas sob risco elevado de vitimização, antes da ocorrência de
- O modelo restaurativo determina participação obrigatória das vítimas e dispensa manifestaç
- Uma pesquisa de vitimização pode revelar episódios que nunca chegaram ao registro oficial,
- Quando dois territórios registram quantidades distintas de delitos, é metodologicamente co
- A análise sociológica das cinco adaptações de Merton distingue rebelião de inovação: a reb
- Em abordagem atuarial, dados históricos de abordagens são suficientes para provar risco cr
- Uma teoria sobre desordem visível nos espaços e controle informal não equivale, por defini
- Se uma política comunitária reduz ocorrências no perímetro beneficiado, é possível conclui
- Uma iniciativa que oferece apoio a pessoa já vitimada para evitar repetição do dano e outr
- No procedimento restaurativo regulado pela Resolução nº 225/2016, o fracasso da composição
- O chamado direito penal do inimigo, formulado por Günther Jakobs, caracteriza-se por:
- A principal crítica dirigida ao direito penal do inimigo é a de que ele é incompatível com
- A expressão que designa a violência praticada por agentes públicos, especialmente policiai
- A cifra cinza da criminalidade corresponde às ocorrências registradas na polícia que não c
- A expressão cifra verde, em criminologia, designa:
- A situação em que a vítima de sequestro desenvolve simpatia e vínculo afetivo com o seques
- Na síndrome de Londres, o refém adota postura de confronto e hostilidade em relação ao cap
```

## Pedido 8: Planejamento, riscos, materialidade e controles internos (`auditoria.planejamento-risco-materialidade`, 348 tópicos de edital)

```
Matéria: auditoria.planejamento-risco-materialidade (Planejamento, riscos, materialidade e controles internos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Atividades preliminares, termos do trabalho e delimitação do escopo": 20
- "2. Materialidade, relevância, risco de auditoria e fraude": 30
- "3. Controles internos, COSO, COSO ERM e três linhas": 17
- "4. Matriz de planejamento, programa, riscos e respostas": 22
- "5. Documentação, comunicação e integração das decisões de auditoria": 11

Já existem (não repita):
- Ao iniciar exame de demonstrações de uma entidade com unidades em três localidades, a equi
- Antes de aceitar novo trabalho, a equipe verifica independência, continuidade de relaciona
- Um trabalho pergunta se transações foram registradas corretamente e delimita exame às fili
- Ao planejar exame de pagamentos, uma equipe relaciona pergunta de auditoria, critério, dad
- Ao considerar se distorções influenciariam decisões dos usuários das demonstrações, inclus
- Uma operação com estimativa complexa está sujeita a distorção relevante antes de qualquer 
- Ao examinar lançamentos, a equipe identifica alteração deliberada de documento para elevar
- Uma unidade adota código de conduta, reconciliação de contas, avaliação de riscos, comunic
- Uma organização mantém avaliação rotineira de conformidade na gestão e também função que p
- Antes de firmar trabalho de auditoria independente, a administração reconhece elaboração d
- Uma materialidade global foi estabelecida para demonstrações, e a equipe define limiar men
- Depois de identificar risco de distorção relevante elevado, a equipe mantém o nível aceitá
- O programa prevê inspeção de faturas, teste de corte no encerramento, responsável por cada
- Uma política interna prevê dupla aprovação de pagamentos, mas não há registros que demonst
- Na matriz de planejamento, consta pergunta sobre pagamento duplicado. Qual registro perten
- Para selecionar um objeto de auditoria pública, uma situação de baixo valor envolve suspen
- Há lançamentos manuais incomuns próximos ao encerramento, forte pressão por metas e possib
- Na auditoria de estoques, controles de inventário não funcionaram e há risco elevado na af
- Um mapa de riscos apresenta alta probabilidade de interrupção de serviço e baixo valor mon
- Uma equipe audita demonstrações de período iniciado em julho de 2026, após a publicação da
- A equipe precisa avaliar modelo complexo usado em estimativas e considera utilizar especia
- Em auditoria, uma distorção monetariamente pequena altera informação sobre descumprimento 
- Uma afirmação de receita apresenta incentivos fortes para manipulação, julgamentos subjeti
- Uma equipe reúne mil relatórios produzidos pelo mesmo sistema que contém falha de processa
- Durante execução, descobre-se migração de sistema que afeta corte de receitas e confiabili
- Na definição do trabalho de auditoria de demonstrações, a estratégia global fixa o alcance
- No planejamento, a definição do escopo corresponde ao mesmo que a formulação do objetivo: 
- Uma equipe prepara matriz de planejamento com questão, critério, fontes, técnicas de colet
- O risco de detecção é a suscetibilidade de uma transação conter distorção antes de qualque
- O controle interno descrito no COSO IC abrange ambiente de controle, avaliação de riscos, 
- O fato de um manual prever dupla autorização comprova, por si só, que o controle operou ef
- Uma distorção criada deliberadamente para inflar receita distingue-se de erro não intencio
- Ao elaborar carta de contratação para auditoria independente, é suficiente definir honorár
- Ao selecionar o exame de interrupção de serviço essencial, a equipe considera relevância s
- O estabelecimento de materialidade para execução em valor menor que o limiar global é inco
- Ao encontrar risco elevado de distorção em receita, o auditor planeja maior evidência pers
- Pela abordagem das três linhas, a função de auditoria interna pertence à primeira linha po
- No planejamento de testes de controles, a equipe distingue investigação de desenho e imple
- Em uma matriz de planejamento, registrar uma hipótese de possível achado equivale a compro
- Em auditoria de estimativas subjetivas, uma deficiência no processo de revisão pode elevar
- Como a NBC TA 240 (R2) foi publicada em setembro de 2026, tornou-se obrigatória para audit
- Em planejamento público baseado em riscos, a função de auditoria interna pode utilizar o m
- Na seleção de evidências, a coleta de numerosos relatórios extraídos de um mesmo sistema d
- Uma deficiência significativa de controle interno descoberta durante a auditoria deve ser 
- Quando a auditoria encontra novo risco relevante após a fase inicial, sua estratégia globa
```

## Pedido 9: Desenvolvimento seguro, OWASP e DevSecOps (`informatica.desenvolvimento-seguro-owasp-devsecops`, 345 tópicos de edital)

```
Matéria: informatica.desenvolvimento-seguro-owasp-devsecops (Desenvolvimento seguro, OWASP e DevSecOps)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos do desenvolvimento seguro e gestão da qualidade": 27
- "2. OWASP Top 10 e vulnerabilidades de aplicações": 18
- "3. Controles para Web, APIs e serviços": 16
- "4. Análise estática, dinâmica e testes de segurança": 21
- "5. DevSecOps, cadeia de suprimentos e modelos de maturidade": 18

Já existem (não repita):
- Uma plataforma valida o login de uma pessoa, mas permite visualizar pedidos de terceiros p
- Ao iniciar um sistema novo, a equipe propõe desabilitar contas privilegiadas não necessári
- Uma equipe precisa avaliar se a API responde em até dois segundos a 300 acessos simultâneo
- Um serviço inclui diretamente em SQL o nome informado em um formulário. A equipe pretende 
- Um comentário armazenado é inserido em uma página de modo que o navegador interprete marca
- Um aplicativo contém biblioteca de terceiro com versão conhecida e precisa identificar seu
- Após receber uma requisição, o código usa uma dependência simulada que devolve a resposta 
- Em projeto de segurança, a equipe precisa separar análise sobre código da investigação de 
- Ao elaborar um inventário de componentes diretos e transitivos de uma versão distribuída, 
- Uma página bancária usa cookie de sessão e aceita alteração de endereço por POST. Um site 
- Uma aplicação permite modificar o atributo `nivelDeAcesso` porque converte todo JSON receb
- Uma equipe quer avaliar práticas organizacionais de segurança em cinco funções de negócio 
- Na revisão de um serviço, foi solicitado um padrão com grupos denominados PO, PS, PW e RV,
- Um scanner detecta que um módulo próprio utiliza entrada não confiável em uma operação sen
- Um time precisa comparar uma classificação de riscos frequentes em aplicações web com uma 
- Uma API autentica com sucesso uma conta de perfil comum, mas aceita chamada para uma funçã
- Uma entrega automatizada gera um documento que descreve onde, com quais insumos e por qual
- Um serviço nega consulta quando a autorização está disponível, mas libera a leitura de dad
- Uma avaliação exige distinguir o risco A03:2021 do risco A03:2025. Qual associação preserv
- Em aplicação SPA, um desenvolvedor garante que cookies têm HttpOnly, mas insere texto vind
- Uma organização usa SCA e recebe alerta de vulnerabilidade em biblioteca transitiva. O pac
- Uma equipe protegeu o repositório com revisões, mas permite que a etapa de build baixe cód
- A equipe escolheu uma abordagem para orientar o desenvolvimento, mas precisa separar um mo
- Uma API interna usa TLS, autentica por token e tem CORS restritivo. Uma pessoa autenticada
- Um serviço aceita URL fornecida pelo cliente e busca seu conteúdo. Uma requisição pode faz
- Uma equipe usa cenários de abuso, registra limites de confiança e associa controles às ame
- Uma pessoa autenticada altera o número de um pedido e alcança dados alheios. A autenticaçã
- Um sistema usa consultas SQL parametrizadas. Para combater XSS no conteúdo exibido no nave
- Ao planejar a qualidade de um serviço, a equipe mede latência sob uma carga-alvo e depois 
- Um relatório de composição de software lista bibliotecas diretas, transitivas, versões e a
- Em um aplicativo, o uso de HTTPS impede que alguém com sessão válida envie requisições CSR
- Na classificação OWASP Web Top 10 de 2025, o risco A03 se refere a falhas na cadeia de sup
- Uma API adota um UUID aleatório no endereço de cada objeto. Como é improvável que alguém d
- Em revisão de código, um mock foi utilizado para verificar que o registrador de auditoria 
- Para validar que o servidor rejeita uma operação administrativa realizada por usuário comu
- Uma equipe trabalha com NIST SSDF SP 800-218 v1.1. As siglas PO, PS, PW e RV designam grup
- Um time obtém um SBOM completo da aplicação. Esse inventário basta para provar que o binár
- Uma revisão técnica utiliza o OWASP ASVS para definir requisitos verificáveis, enquanto us
- Uma política de CORS permite a origem de um site parceiro. Por esse fato isolado, a API de
- O fluxo de entrega verifica assinaturas de pacotes e obtém proveniência de build, além de 
- Em uma API com cookie de sessão, uma falha de XSS permite executar código no contexto da p
- Ao verificar uma aplicação, a equipe classifica como falso negativo uma falha real de auto
- Na lista OWASP API Security Top 10:2023, permitir escrita arbitrária do atributo interno d
- Uma revisão afirma que o BSIMM é uma norma prescritiva idêntica ao SAMM, com as mesmas qui
- Um serviço mantém autorizações corretas durante operação normal, mas, quando o verificador
- O OWASP Security Knowledge Framework é uma ferramenta de código aberto que, com base no AS
- O padrão da OWASP em que o Security Knowledge Framework se baseia para gerar listas de req
```

## Pedido 10: Saúde mental: RAPS, política nacional e internação psiquiátrica (`saude-publica.saude-mental-raps-internacao`, 344 tópicos de edital)

```
Matéria: saude-publica.saude-mental-raps-internacao (Saúde mental: RAPS, política nacional e internação psiquiátrica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Política de saúde mental, direitos e organização do SUS": 20
- "2. RAPS: componentes, CAPS e rede articulada": 21
- "3. Internações psiquiátricas: modalidades, requisitos e garantias": 21
- "4. Quadros clínicos: bipolaridade, psicoses, substâncias e personalidade": 18
- "5. Pós-COVID, deficiência, reabilitação e perícia em saúde mental": 20

Já existem (não repita):
- Uma pessoa com transtorno mental está estabilizada e pode manter tratamento no território.
- Pessoa permanece há muitos anos internada, sem suporte familiar consolidado e com dependên
- Uma família procura serviço que atenda, no território, criança com sofrimento psíquico int
- Um serviço territorial de atenção psicossocial deve prestar acolhimento noturno e funciona
- Uma pessoa aceita ser internada e assina declaração formal na admissão. De acordo com a Le
- Uma internação psiquiátrica foi determinada diretamente pelo juiz competente após avaliaçã
- Após internação psiquiátrica involuntária, o responsável técnico do estabelecimento deve i
- Pessoa com transtorno por uso de álcool apresenta abstinência grave com convulsões após ce
- Uma pessoa apresenta humor elevado, energia excessiva, redução da necessidade de sono e co
- Uma pessoa com deficiência física e sofrimento mental persistente precisa de reabilitação 
- Uma pessoa com sofrimento mental relevante também necessita de atenção clínica contínua pa
- Uma pessoa chega à atenção primária com sofrimento psíquico persistente. A equipe encaminh
- Após longa internação psiquiátrica, uma pessoa não conta com moradia familiar e poderá viv
- Durante internação psiquiátrica, ocorre transferência inesperada da pessoa para outro esta
- Uma pessoa dependente de drogas foi internada involuntariamente para desintoxicação, sob o
- Uma pessoa recém-hospitalizada por desintoxicação foi encaminhada a comunidade terapêutica
- Uma pessoa relata ouvir vozes após vários dias de privação de sono e uso de estimulantes. 
- Uma pessoa apresenta irritabilidade e episódios de conflito interpersonal ocasionais, mas 
- Uma equipe usa estimativa internacional de aumento de 25% de ansiedade e depressão no prim
- Uma pessoa com diagnóstico anterior de transtorno bipolar apresenta estabilidade atual e d
- Uma internação sem consentimento foi autorizada após laudo circunstanciado e pedido famili
- Uma pessoa com transtorno decorrente do uso de drogas apresenta necessidade hospitalar jus
- No atendimento inicial de pessoa com alucinações e desorientação flutuante após infecção, 
- Após COVID-19, uma pessoa relata fadiga incapacitante, sono irregular, lentificação cognit
- Uma perícia examina se pessoa com diagnóstico psiquiátrico compreendia o caráter ilícito e
- Uma pessoa com sofrimento mental estabilizado prefere seguimento na unidade territorial. A
- Uma pessoa há dez anos hospitalizada deve receber alta administrativa sem plano de transiç
- Uma pessoa é acompanhada em CAPS III durante episódio de crise. O funcionamento dessa moda
- Quando o familiar solicita internação sem o consentimento da pessoa, a modalidade é compul
- Uma pessoa em abstinência de álcool apresenta convulsão. A condição deve ser tratada como 
- Em atendimento compartilhado entre atenção primária e CAPS, o encaminhamento formal exting
- Uma pessoa com longo histórico de hospitalização e ausência de moradia pode ser encaminhad
- Em internação psiquiátrica involuntária, o responsável técnico tem até 24 horas para comun
- Uma pessoa apresenta alucinações durante intoxicação por substâncias. A equipe deve invest
- A estimativa de aumento global de 25% na ansiedade e depressão durante o primeiro ano da p
- Pessoa internada involuntariamente por indicação psiquiátrica, sem relação específica com 
- O art. 23-A, § 7º, da Lei 11.343/2006 exige comunicação de internação e alta em até 72 hor
- Uma pessoa com episódio maníaco apresenta delírios e grande prejuízo funcional. Esse quadr
- Uma pessoa apresenta padrão persistente de instabilidade relacional e impulsividade. A sim
- Em pessoa com uso prejudicial de drogas que ainda não aceita abstinência total, intervençõ
- Pessoa com deficiência intelectual e sofrimento psicótico grave necessita de habilitação f
- Uma pessoa com fadiga, falhas de memória e ansiedade após COVID-19 deve receber apenas tra
- Em perícia para capacidade de desempenho de atividade habitual, a avaliação do funcionamen
- Uma avaliação administrativa sobre sanidade mental relativa a conduta passada possui o mes
- O diagnóstico de transtorno bipolar, por si, demonstra incapacidade permanente para qualqu
```

## Pedido 11: Vigilância sanitária e regulação sanitária (`saude-publica.vigilancia-sanitaria-regulacao`, 342 tópicos de edital)

```
Matéria: saude-publica.vigilancia-sanitaria-regulacao (Vigilância sanitária e regulação sanitária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, precaução e Sistema Nacional de Vigilância Sanitária": 21
- "2. Regularização de produtos, empresas e verificação de situação sanitária": 20
- "3. Boas práticas, certificação, biossegurança e ciclo de vida": 19
- "4. Ensaios clínicos, benefício-risco e governança regulatória": 19
- "5. Infrações, cautelares, termo de compromisso e fiscalização responsiva": 21

Já existem (não repita):
- Em uma cidade, a vigilância identificou problemas de esterilização em um serviço de saúde 
- Uma autoridade local questiona se pode inspecionar estabelecimentos porque a Anvisa coorde
- Um novo insumo mostra sinal experimental plausível de dano grave, mas as evidências ainda 
- Um fabricante pretende introduzir dispositivo médico de classe IV no mercado brasileiro. C
- Ao fiscalizar uma empresa, a equipe verificou AFE vigente, mas não localizou o registro ex
- Uma fabricante de alimentos foi informada de que sua categoria consta do anexo que exige c
- Durante inspeção, observou-se que uma fábrica cumpre requisitos materiais de BPF, embora n
- No planejamento de inspeção de fábrica de medicamentos, que regulamento oferece diretrizes
- Um patrocinador apresentou o programa de desenvolvimento de um medicamento e o protocolo d
- Após autuação sanitária, uma empresa pretende apresentar defesa administrativa contra os f
- Uma inspeção constata fabricante de dispositivo médico certificado por auditoria MDSAP. Qu
- Após coletar amostra de produto suspeito para análise fiscal, uma equipe considerou indisp
- Um estudo clínico de medicamento tem análise regulatória enquadrada na RDC nº 945/2024. Na
- Uma autoridade elabora norma de rastreabilidade e, anos depois, pretende verificar se a me
- Uma empresa autuada propôs termo de compromisso para corrigir irregularidades previstas na
- Uma empresa firmou termo de compromisso para corrigir falhas documentais; durante a execuç
- A autoridade detectou lotes suspeitos em depósito, mas a mera coleta para análise não demo
- Um serviço regulado apresenta dados pós-mercado que sugerem eventos adversos graves em par
- Uma clínica pretende utilizar equipamento que contém agente biológico manipulado sob condi
- Uma empresa possui BPF bem documentadas, mas apenas teste final de lote é usado para verif
- Uma regulação emergencial entrou em vigor com dispensa de AIR expressamente fundada em urg
- Uma empresa pede autorização para dispositivo classe II e sustenta que a notificação a lib
- Durante inspeção em fábrica que recebeu insumo adulterado, a equipe deve planejar fiscaliz
- Um fabricante deseja celebrar termo de compromisso após receber multa já definitivamente a
- Após confirmação inicial de risco grave, um estabelecimento foi interditado cautelarmente 
- A vigilância de um município inspeciona restaurante com falhas de armazenamento. Essa ativ
- Ao identificar produto alimentar submetido ao regime de comunicação de início à vigilância
- Uma empresa que fabrica medicamentos deve observar os requisitos de boas práticas de fabri
- Em uma pesquisa com medicamento para registro, a avaliação regulatória da Anvisa substitui
- Uma autoridade sanitária lavra auto de infração e notifica o interessado. A Lei nº 6.437/1
- A Agência Nacional de Vigilância Sanitária, como coordenadora do SNVS, substitui todas as 
- Uma fabricante de dispositivos médicos classes I e II utiliza notificação e, por isso, fic
- Uma fábrica certificada de dispositivo médico substituiu seu processo crítico sem verifica
- O mecanismo de reliance previsto nos procedimentos de ensaios clínicos transfere integralm
- Ao decidir sobre alteração normativa sanitária, a autoridade pode realizar consulta públic
- Na Lei nº 6.437/1977, a interdição cautelar de estabelecimento destinada a permitir anális
- Depois de receber notificação de multa sanitária, o interessado pode pagar em vinte dias c
- A RDC nº 850/2024 ampliou indistintamente para quatro anos o prazo de validade de todos os
- Um termo de compromisso foi firmado após requerimento protocolizado por fabricante. As san
- Uma empresa descumpriu cláusula de termo de compromisso e alegou que somente sentença judi
- Uma empresa fiscalizada possui CBPF, mas apresentou evidências de falha recente de esteril
- Um estudo observacional sem desenvolvimento de medicamento para registro deve obrigatoriam
- Em investigação de lote suspeito de adulteração, a amostra representativa normalmente é di
- Se a empresa detém autorização de funcionamento vigente, a consulta de regularidade de pro
- O artigo 12 do Decreto nº 10.411/2020 determina ARR em até três anos da entrada em vigor p
```

## Pedido 12: Crimes Hediondos e Tortura (`legislacao-penal-especial.crimes-hediondos-tortura`, 342 tópicos de edital)

```
Matéria: legislacao-penal-especial.crimes-hediondos-tortura (Crimes Hediondos e Tortura)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais, taxatividade e crimes equiparados": 19
- "2. Rol legal dos crimes hediondos e alterações recentes": 19
- "3. Efeitos jurídicos: fiança, regimes, progressão e prazos": 19
- "4. Lei de Tortura: tipos penais, omissão e penalidades": 32
- "5. Integração dos regimes legais e resolução de casos complexos": 11

Já existem (não repita):
- Um fato provocou intensa comoção, mas a descrição penal não está no catálogo legal. Qual c
- Uma pessoa inicia tentativa de extorsão mediante sequestro, mas a ação é interrompida ante
- Durante uma revisão, há dúvida se a tortura depende de inserção literal no art. 1º da Lei 
- A defesa de investigado por crime hediondo pede liberdade provisória sem fiança. Qual prem
- Em um caso de extorsão mediante sequestro, sem lesão ou morte, qual a consequência classif
- Uma pessoa utiliza violência para obter confissão de terceiro e causa sofrimento físico. Q
- Alguém sob autoridade do responsável pela custódia sofre intenso sofrimento mediante violê
- Uma pessoa com dever funcional de apurar atos de tortura deliberadamente se omite diante d
- Um delito previsto no art. 2º da Lei nº 8.072/1990 enseja prisão temporária. Qual prazo es
- Em matéria de epidemia, qual fato integra o rol do art. 1º, VII, da Lei nº 8.072/1990?
- Em roubo, foi utilizada apenas arma branca, sem restrição da liberdade nem resultado lesiv
- Um furto ocorre com artefato explosivo que causa perigo comum. Qual dispositivo justifica 
- Ao estudar alterações recentes, a pessoa identifica dois tipos autônomos inseridos no rol 
- Uma lesão corporal dolosa gravíssima é cometida nas dependências de estabelecimento de ens
- Sobre a reforma do art. 1º, parágrafo único, VII, em 2026, qual afirmação interpreta adequ
- Há investigação por posse ilegal de arma de fogo de uso restrito, sem outras circunstância
- Em 2026, foi acrescentado tipo de tortura envolvendo sofrimento reiterado da mulher no con
- Uma situação de tortura ocorreu no exterior e a vítima é brasileira. Qual regra de incidên
- Na sentença por delito hediondo, o juiz pretende fixar regime fechado só porque a lei clas
- Em fato praticado depois da Lei nº 15.358/2026, o condenado primário responde por crime he
- Uma associação atua em ilícitos variados, mas não se comprova direcionamento a delito hedi
- Ao praticar tortura, o autor provoca resultado morte na hipótese do art. 1º, § 3º, da Lei 
- Uma pessoa pratica tortura contra vítima maior de 60 anos, preenchendo o tipo penal. Qual 
- Um fato ocorreu antes da criação da figura autônoma de vicaricídio em 2026. Qual solução r
- Na redação de 2026 da LEP, para reincidente na prática de crime hediondo ou equiparado com
- Uma tentativa de estupro cuja modalidade está no art. 1º da Lei nº 8.072/1990 pode receber
- Uma conduta violenta sem correspondência em qualquer delito do rol poderá ser hedionda se 
- A extorsão mediante sequestro na forma do art. 159, caput, já está expressamente prevista 
- Quando a tortura é cometida entre particulares, sem participação de agente público, a Lei 
- Nos delitos referidos pelo art. 2º da Lei nº 8.072/1990, a prisão temporária pode durar 30
- Por ser inafiançável o crime hediondo, nenhuma situação autoriza a avaliação judicial de l
- Submeter pessoa presa a sofrimento físico ou mental mediante ato não previsto em lei pode 
- A pessoa que, podendo e devendo apurar tortura, omite-se nas condições do art. 1º, § 2º, s
- Roubo com emprego de arma de fogo expressamente referido no art. 157, § 2º-A, I, integra o
- A epidemia descrita apenas no art. 267, caput, sem resultado morte, consta literalmente do
- A condenação pelo crime de tortura acarreta perda de cargo, função ou emprego público e in
- A tortura discriminatória do art. 1º, I, c, abrange expressamente qualquer discriminação p
- A tortura cometida contra vítima maior de 60 anos admite causa de aumento de um sexto até 
- A tipificação acrescentada pelo art. 1º, III, da Lei nº 9.455/1997 em 2026 autoriza puniçã
- O art. 2º da Lei nº 9.455/1997 contempla tortura praticada fora do Brasil se a vítima for 
- A redação do art. 1º, parágrafo único, VII, da Lei nº 8.072/1990, em 2026, tornou hediondo
- Para fatos sujeitos à nova disciplina de 2026, a hipótese geral de primário condenado por 
- O texto do art. 2º, § 1º, da Lei nº 8.072/1990 impõe, conforme interpretação constituciona
- Os crimes de domínio social estruturado e favorecimento ao domínio social estruturado fora
- A hipótese atual do art. 112, VIII, da LEP prevê 70% para reincidente em hediondo com resu
```

## Pedido 13: Lei de Abuso de Autoridade (Lei 13.869/2019) (`direito-administrativo.abuso-autoridade`, 340 tópicos de edital)

```
Matéria: direito-administrativo.abuso-autoridade (Lei de Abuso de Autoridade (Lei 13.869/2019))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Âmbito de aplicação, sujeitos ativos e finalidade específica": 20
- "2. Ação penal, condenação e autonomia das responsabilidades": 20
- "3. Proteção da liberdade, da custódia e da defesa": 20
- "4. Domicílio, provas, investigação e prerrogativas": 20
- "5. Crimes decisórios e síntese para resolução de casos": 20

Já existem (não repita):
- Uma pessoa exerce função pública temporária e sem remuneração. Após agir para beneficiar t
- Uma decisão jurídica fundamentada foi reformada por entendimento interpretativo diferente,
- Em conduta funcional tipificada, a prova demonstrou que a pessoa agiu apenas por capricho 
- Uma vítima pretende ajuizar diretamente queixa principal por crime previsto na Lei nº 13.8
- Após condenação por abuso de autoridade, sem reincidência em outro crime dessa natureza, d
- Foi aplicada, como pena restritiva substitutiva, suspensão do exercício da função, com per
- O responsável pela custódia deixa de entregar, na prisão, a nota de culpa contendo o motiv
- Durante repouso noturno, um preso declara aceitar interrogatório, mas não se encontra devi
- Um mandado de busca domiciliar é cumprido às 22h, sem circunstância excepcional de socorro
- Há notícia de crime de abuso de autoridade, mas a acusação pública deixa transcorrer o pra
- Em investigação, a pessoa encarregada toma conhecimento da decisão do interrogado de perma
- Um procedimento desnecessário faz vítima de crime violento reviver repetidamente a violênc
- Na mesma sentença, analisa-se obrigação de reparação e possível inabilitação para função p
- Ao cumprir a legislação de acesso aos autos, o responsável separa peças de diligências fut
- Uma pessoa determina abertura de apuração sem qualquer indício de ilícito. Em comparação, 
- Uma gravação alheia à prova pretendida é divulgada e expõe a intimidade de uma pessoa inve
- O agente causa demora excessiva em processo colegiado após requerer vista, com o propósito
- Uma autoridade bloqueia ativos acima do estimado para satisfazer dívida, mas corrige de im
- Em investigação sem prazo definido, uma pessoa responsável mantém o procedimento parado se
- O responsável pela investigação informa o andamento e, antes de concluídas as apurações e 
- Um agente recebe informação de que a prova é ilicitamente obtida e, sabendo disso previame
- A decisão penal reconheceu que determinado ato fora praticado em estrito cumprimento de de
- No mesmo espaço de confinamento, permanecem pessoas presas de ambos os sexos; em outro epi
- A pessoa responsável impõe obrigação sem qualquer suporte legal expresso e, em outra situa
- Uma pessoa investigada sofre continuidade de persecução administrativa mesmo após o respon
- Durante designação transitória e sem remuneração, uma pessoa pode ser considerada agente p
- Se a interpretação jurídica de um agente for posteriormente rejeitada por outra instância,
- Os crimes da Lei nº 13.869/2019 são de ação penal pública incondicionada, admitida queixa 
- A inabilitação para função pública é efeito automático da primeira condenação por abuso de
- O art. 5º permite, como restritiva substitutiva, suspensão do exercício funcional por 1 a 
- A nota de culpa prevista no art. 12, parágrafo único, III, deve ser entregue à pessoa pres
- O constrangimento descrito no art. 13 envolve preso ou detento submetido a violência, grav
- O consentimento de preso para interrogatório policial durante repouso noturno dispensa a a
- O art. 15-A prevê aumento de dois terços quando o agente permite a intimidação de vítima d
- O art. 22, § 1º, III, considera tipificada a execução de mandado de busca e apreensão domi
- O ingresso em imóvel destinado a prestar socorro não configura o crime do art. 22, conform
- O art. 27 permite a instauração de procedimento investigatório sem qualquer indício sempre
- O art. 32 ressalva do acesso às peças de diligências em curso ou futuras cujo sigilo seja 
- Para o art. 36, a fixação de bloqueio financeiro em qualquer valor superior ao da dívida, 
- O art. 31 pode incidir quando, não existindo prazo de conclusão fixado, uma investigação é
- No art. 25, parágrafo único, o uso de prova em desfavor de investigado exige conhecimento 
- No art. 37, basta a existência de pedido de vista em processo colegiado para configurar cr
- O art. 38 alcança atribuição pública prematura de culpa pelo responsável por investigações
- A independência das instâncias do art. 7º permite rediscutir livremente a existência do fa
- O art. 19, parágrafo único, deixa de alcançar a autoridade judiciária que, sabendo do impe
```

## Pedido 14: Crédito tributário e lançamento (`direito-tributario.credito-lancamento`, 337 tópicos de edital)

```
Matéria: direito-tributario.credito-lancamento (Crédito tributário e lançamento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Origem, natureza e disciplina geral do crédito tributário": 24
- "2. Competência, conteúdo, natureza e efeitos do lançamento": 15
- "3. Modalidades: lançamento de ofício e por declaração": 23
- "4. Homologação, pagamento antecipado e declaração constitutiva": 20
- "5. Alteração, retificação e revisão: arts. 145, 146 e 149": 18

Já existem (não repita):
- Uma venda realiza o fato gerador previsto na lei, e o lançamento só é concluído meses depo
- Uma alteração legal afeta somente as garantias de determinado crédito tributário. De acord
- Ao efetuar lançamento, uma autoridade identifica o sujeito passivo, determina matéria trib
- Uma autoridade municipal calcula e notifica tributo imobiliário usando dados do cadastro o
- Em um tributo, a legislação exige que o contribuinte antecipe o pagamento, sem exame prévi
- Uma autoridade encontra valor do bem omitido em declaração indispensável ao lançamento e, 
- Uma empresa apresenta declaração fiscal reconhecendo débito determinado, mas deixa de paga
- Após notificação regular de lançamento, o contribuinte apresenta impugnação com provas de 
- Uma fiscalização é concluída depois de ter entrado em vigor alíquota maior que a existente
- Após operação tributável, entrou em vigor norma que ampliou poderes de investigação da aut
- Em lançamento por declaração, o particular pretende retificar informações para reduzir tri
- Contribuinte entrega declaração indispensável ao lançamento; a autoridade solicita esclare
- Uma declaração contém erro de soma detectável pela simples comparação dos campos que a int
- O sujeito passivo recolheu antecipadamente o tributo conforme o regime de homologação, mas
- Um lançamento já foi regularmente notificado, mas uma decisão submetida a recurso oficial 
- Após lançamento baseado em informações disponíveis, aparece documento que comprova fato re
- A administração conhecia todos os fatos ao lançar, mas posteriormente decide adotar nova i
- Contribuinte informa valor de bem sem documentos confiáveis. A autoridade, em procedimento
- A administração descobre erro de fato comprovado em elemento de declaração obrigatória apó
- Uma declaração reconhece débito de R$ 40.000, mas exame documental encontra operações omit
- O fato gerador ocorreu com base tributável expressa em moeda estrangeira, e o câmbio subiu
- A LC 236/2026 alterou o art. 146 do CTN e uma sentença arbitral especial modifica critério
- O lançamento anterior foi realizado por autoridade que deliberadamente omitiu formalidade 
- Em lançamento preventivo destinado a resguardar o direito de lançar, a exigibilidade do cr
- Contribuinte antecipou parte do tributo sujeito a homologação e outros fatos foram omitido
- Após a ocorrência de fato gerador tributário, nasce a obrigação principal, enquanto o lanç
- A natureza do crédito tributário pode ser distinta da obrigação principal que o originou, 
- O lançamento compreende verificar o fato gerador, determinar a matéria tributável, calcula
- Quando o contribuinte presta informações e a autoridade constitui o crédito a partir delas
- No lançamento por homologação, a legislação atribui ao sujeito passivo o dever de antecipa
- O arbitramento da base tributável disciplinado pelo art. 148 do CTN é a quarta modalidade 
- Após lançamento regularmente notificado, a impugnação do sujeito passivo pode ser uma das 
- O art. 144 do CTN obriga a autoridade a empregar a lei material vigente no dia da notifica
- Em lançamento por declaração, erro aritmético que pode ser apurado pela leitura da própria
- O contribuinte pode reduzir livremente o tributo por retificação de declaração do art. 147
- Conforme Súmula 436 do STJ, a declaração apresentada pelo contribuinte com reconhecimento 
- O lançamento regularmente notificado pode ser alterado por decisão discricionária não fund
- A falta de apresentação da declaração exigida pela legislação tributária é hipótese do art
- Um lançamento anterior considerou todos os fatos corretamente, mas a autoridade quer revê-
- Na redação dada pela LC 236/2026 ao art. 146 do CTN, a sentença arbitral proferida no âmbi
- Se posteriormente se descobre fato antes desconhecido que pode justificar revisão de ofíci
- O art. 144, § 1º, admite aplicar à atividade de lançamento novos processos de fiscalização
- O lançamento anterior feito por autoridade que omitiu formalidade essencial comprovada pod
- A LC 236/2026 previu no art. 142, §§ 2º e 3º, situação em que lançamento preventivo não co
- A declaração que constitui crédito nos termos da Súmula 436 impede por completo o fisco de
```

## Pedido 15: Terapia ocupacional: fundamentos e prática clínica (`terapia-ocupacional.fundamentos-pratica`, 336 tópicos de edital)

```
Matéria: terapia-ocupacional.fundamentos-pratica (Terapia ocupacional: fundamentos e prática clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, modelos e avaliação ocupacional": 20
- "AVD, AIVD, brincar, lazer e trabalho": 20
- "Bases motoras, neurofuncionais e tecnologia assistiva": 20
- "Intervenções na infância, adolescência e saúde mental": 20
- "Inclusão, prática interprofissional e ética": 20

Já existem (não repita):
- Uma avaliação terapêutico-ocupacional considera a ocupação desejada, as características da
- No MOHO, a habituação envolve hábitos e papéis, enquanto a volição contempla interesses e 
- Preparar a própria refeição é exemplo típico de AVD básica, enquanto levar o alimento à bo
- O brincar pode constituir tanto uma ocupação importante da infância quanto uma oportunidad
- A tecnologia assistiva abrange somente dispositivos eletrônicos sofisticados, ficando os e
- A autonomia pessoal pode ser preservada mesmo quando alguém necessita de ajuda física de t
- Na CIF, o desempenho no ambiente habitual e a capacidade de executar uma tarefa em condiçõ
- Na análise de atividade, sequência de etapas, demandas motoras, cognitivas, sociais e cara
- A independência em AVD somente é reconhecida se o indivíduo executar a tarefa sem qualquer
- Em uma ocupação que exige alcance e preensão, a avaliação biomecânica pode contribuir para
- No cuidado de saúde mental, o PTS é uma sequência rígida de oficinas determinada exclusiva
- A avaliação de uma órtese inclui seu efeito sobre a pele, o conforto, a função pretendida 
- Em paciente após AVC, a negligência espacial unilateral pode interferir na alimentação mes
- Pelo modelo PEO, modificar o ambiente não pode melhorar o desempenho ocupacional enquanto 
- A reabilitação psicossocial pode incluir trabalho, lazer, vínculos comunitários e circulaç
- Uma órtese substitui integralmente a estrutura corporal ausente, enquanto uma prótese se l
- O Código de Ética de TO estabelece deveres de sigilo, informação adequada e atuação respon
- Por respeito à individualidade, o terapeuta ocupacional pode prescrever qualquer procedime
- Uma pessoa procura TO porque não consegue mais tomar banho de modo seguro após internação.
- Qual componente do MOHO melhor explica padrões estáveis de rotina e papéis sociais?
- Uma criança tem dificuldade de acionar um brinquedo convencional. Qual intervenção express
- Um dispositivo externo é utilizado para sustentar e posicionar o punho durante tarefas. Es
- Qual das situações representa AIVD?
- A respeito da autonomia de uma pessoa que utiliza cuidador para transferências, a melhor a
- Na CIF, qual item corresponde diretamente a fator ambiental?
- Em qual serviço da RAPS a TO pode integrar equipe multiprofissional de cuidado territorial
- Uma pessoa com tremor consegue beber água com copo leve, mas derrama líquido ao usar recip
- No preparo do almoço, o usuário consegue picar alimentos, mas esquece a panela no fogo. Qu
- Pessoa com limitação física usa controle ambiental para acender luzes sem ajuda humana. O 
- Para uma adolescente que deseja voltar às atividades escolares após lesão neurológica, qua
- Um trabalhador retorna após lesão na mão. Qual análise é compatível com TO?
- Uma criança mostra forte interesse por brincar de faz de conta, mas tem dificuldade motora
- Durante a adaptação de uma órtese, surge vermelhidão persistente e dor. A resposta profiss
- Uma pessoa com depressão pretende retomar o contato com colegas por meio de oficina cultur
- Um adulto após AVC não percebe objetos à esquerda durante a refeição, embora tenha força s
- Qual formulação melhor expressa uma intervenção coerente com o modelo Pessoa–Ambiente–Ocup
- No atendimento de adolescente com deficiência, a família deseja escolher sozinha metas que
- Em um CAPS, uma oficina artística foi planejada sem perguntar ao usuário sobre interesse o
- Profissional pretende divulgar imagem identificável de usuário atendido. Qual princípio de
- Em demência, pessoa mantém prazer ao dobrar roupas familiares, mas se perde em sequências 
- Uma pessoa com amputação quer voltar a cozinhar. Qual distinção entre recursos deve nortea
- Na atenção domiciliar, usuário enfrenta escadas, banheiro inacessível e isolamento. Qual c
- A equipe identifica que um usuário precisa de reabilitação e inclusão escolar. Qual enunci
```

## Pedido 16: Eletrônica analógica, digital e de potência (`engenharia-eletrica.eletronica-analogica-digital-potencia`, 336 tópicos de edital)

```
Matéria: engenharia-eletrica.eletronica-analogica-digital-potencia (Eletrônica analógica, digital e de potência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de circuitos, transitórios e regime senoidal": 18
- "2. Diodos, transistores e amplificação analógica": 21
- "3. Amplificadores operacionais e circuitos de interface": 19
- "4. Lógica digital, CMOS e conversão AD/DA": 25
- "5. Eletrônica de potência e conversores": 17

Já existem (não repita):
- Uma rede resistiva linear é analisada a partir de dois terminais, após a desconexão da car
- Uma fonte de Thévenin de 20 V possui resistência interna de 5 Ω. Que valor de carga resist
- Em um RC série ideal, de resistência 1 kΩ e capacitância 100 µF, aplica-se um degrau. Qual
- Um capacitor ideal de 100 µF é submetido a uma senóide de 50 Hz. Qual impedância aproximad
- Um diodo zener é empregado como referência simples de tensão. Qual afirmação descreve o ar
- Em região ativa direta, um TBJ apresenta ganho de corrente β igual a 100 e corrente de bas
- Um MOSFET de canal N de enriquecimento será usado como chave de baixa resistência. Em qual
- Um amplificador operacional inversor ideal usa `Rin = 10 kΩ`, `Rf = 30 kΩ` e recebe entrad
- Um circuito precisa reter o bit presente na entrada D em cada borda de subida do sinal de 
- Uma interface informa o valor decimal 45 em um registrador binário sem sinal. Qual sequênc
- Uma função digital é descrita pela negação do produto lógico entre A e B. Qual expressão e
- Um ADC ideal unipolar de 8 bits mede uma faixa de 0 a 2,56 V. Adotando passo `Vref/2^N`, q
- Um conversor buck ideal opera em condução contínua com entrada de 24 V e ciclo ativo de 25
- Uma ponte monofásica de quatro diodos ideal alimenta um filtro a partir de rede CA de 60 H
- Uma entrada ruidosa oscila próximo ao limiar de decisão. Deseja-se que a tensão de comutaç
- Um inversor CMOS mantém capacitância comutada, tensão de alimentação e atividade constante
- Um operacional deve fornecer senóide com pico de 1 V a 100 kHz. Qual slew rate mínimo apro
- Uma rede senoidal linear apresenta impedância de Thévenin `Zth = 4+j3 Ω`. Sob hipótese de 
- Um capacitor ideal tem capacitância C e tensão inicial de 3 V. Utilizando a convenção pass
- No estágio emissor comum, acrescenta-se resistor não desacoplado ao emissor, mantendo a po
- Um circuito RLC série natural tem `R = 20 Ω`, `L = 1 H` e `C = 0,01 F`. Como se classifica
- Um amplificador diferencial apresenta ganho diferencial de 1.000 e ganho de modo comum de 
- Uma família lógica especifica `VOH(min)=4,4 V`, `VIH(min)=3,5 V`, `VIL(max)=1,5 V` e `VOL(
- Um boost ideal em condução contínua recebe 12 V e trabalha com ciclo de trabalho de 40%. Q
- Um buck ideal em condução contínua opera com `Vin=20 V`, `Vo=10 V`, `D=0,5`, `L=100 µH` e 
- Ao substituir por Thévenin uma rede linear que alimenta uma resistência, o projetista mant
- Em um regulador zener simples com fonte de 15 V e diodo de 5,1 V, o resistor série pode se
- Um sistema de contagem digital precisa guardar um estado entre dois pulsos de relógio. A p
- Um operacional sem rede de realimentação, alimentado por tensões finitas, pode ser calcula
- Em um buck ideal operando de modo contínuo, uma entrada de 30 V e ciclo de trabalho de 0,4
- Um TBJ usado como chave saturada com corrente de base de 1 mA e β nominal de 100 conduz ne
- Um capacitor de 10 µF apresenta 4 V imediatamente antes da comutação de um circuito com re
- Ao escolher carga `RL=Rth` para uma fonte resistiva de Thévenin, obtêm-se simultaneamente 
- Durante uma transição de nível em circuito CMOS, o acionamento da capacitância de entrada 
- Em uma chave MOSFET, a tensão de limiar `VGS(th)` garantirá, por si só, a corrente nominal
- Um estágio inversor ideal, com entrada positiva de 0,2 V e relação `Rf/Rin = 5`, tem saída
- Um sinal de 600 Hz amostrado a 1.000 amostras por segundo pode ter aliasing; instalar apen
- Uma ponte retificadora monofásica ideal alimentada por tensão de 50 Hz retifica ambos os s
- Um circuito RC começa com 6 V armazenados no capacitor. Ao analisar a resposta por Laplace
- Em um RLC série com `R=4 Ω`, `L=1 H` e `C=0,25 F`, a resposta natural está criticamente am
- Em uma medição entre dois circuitos com potenciais de referência diferentes, usar amplific
- Em portas CMOS com capacitância efetiva fixa, reduzir pela metade a tensão de alimentação 
- Um ADC ideal de 12 bits com faixa de 0 a 4,096 V tem passo de 1 mV; portanto, sob arredond
- Em uma interface entre dois domínios de relógio, amostrar dado assíncrono próximo à borda 
- Um boost com entrada de 15 V e ciclo ativo de 50% necessariamente produz 30 V, independent
```

## Pedido 17: Conhecimentos sobre o Estado de Alagoas (`alagoas.conhecimentos-estaduais`, 332 tópicos de edital)

```
Matéria: alagoas.conhecimentos-estaduais (Conhecimentos sobre o Estado de Alagoas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação histórica e emancipação política": 21
- "2. Geografia física, paisagens e rio São Francisco": 20
- "3. Organização político-administrativa e rede municipal": 18
- "4. Economia: cana, indústria, serviços, comércio e turismo": 22
- "5. Cultura popular, memória e patrimônio histórico-cultural": 19

Já existem (não repita):
- Um estudante encontrou a informação de que Alagoas foi separada administrativamente de Per
- Um roteiro histórico compara a primeira sede administrativa alagoana com a capital contemp
- Em uma atividade sobre resistência à escravidão, a turma deve identificar o município em q
- Ao sair da faixa úmida oriental em direção ao interior de Alagoas, um viajante observa tra
- Uma pesquisa localiza um polo de comércio e serviços do interior associado ao Agreste alag
- Um levantamento precisa informar quantos municípios compõem Alagoas, sem confundir esse to
- Em uma aula sobre economia colonial, a expansão de engenhos e o comércio portuário em Alag
- Um festival reúne grupos de Guerreiro, Coco de Roda e Pastoril. Essas apresentações exempl
- Uma linha do tempo mostra 1817, 1821, 1822 e 1889. Em que momento Alagoas foi elevada à co
- Para explicar o crescimento histórico de Maceió e a escolha de nova capital em 1839, um te
- Um projeto ambiental compara o litoral oriental com o Sertão alagoano. Qual associação ent
- Em uma visita de estudo ao interior, um grupo pretende conhecer uma cidade histórica às ma
- Ao analisar documentos populacionais, uma pessoa compara o Censo de 2022 com uma estimativ
- O planejamento de uma pesquisa utiliza a divisão regional de 2017 e pretende indicar as re
- Uma pesquisa econômica deseja distinguir atividade primária, transformação agroindustrial 
- Uma agência de viagens reúne visitas a centros históricos e passeios fluviais no mesmo pro
- Um relatório patrimonial procura identificar exatamente os três municípios alagoanos com c
- Uma exposição distingue a casa de Graciliano Ramos, as rendas artesanais e o Guerreiro. Qu
- Uma cronologia registra a queda do principal núcleo de resistência de Palmares e a morte d
- Um mapa tem dois destaques: o sistema Mundaú-Manguaba e os cânions do São Francisco. Qual 
- Uma análise contrapõe a Zona da Mata açucareira e localidades irrigadas no vale do São Fra
- Um estudo apresenta o total de 102 municípios e a regionalização de 2017 em duas intermedi
- Um estudo da história urbana busca ligar a primeira capital, a transferência de sede e o p
- Uma professora compara a preservação do centro de Piranhas com a continuidade de grupos de
- Um roteiro de turismo cultural visita Serra da Barriga, um conjunto urbano tombado no Baix
- Uma exposição de história regional informa que a emancipação alagoana em relação a Pernamb
- Em um mapa das capitais históricas, Maceió aparece como a sede inicial da Capitania de Ala
- Ao descrever o interior de Alagoas, um pesquisador associa o Sertão a ambientes mais secos
- Um cadastro territorial registra onze municípios em Alagoas, porque o número coincide com 
- Uma pesquisa sobre a formação produtiva estadual associa a expansão dos engenhos e a circu
- Ao reconhecer as tradições culturais alagoanas, uma exposição classifica Guerreiro e Coco 
- Para localizar a Serra da Barriga, um material de estudo indica União dos Palmares, municí
- Um texto histórico situa a elevação de Alagoas à condição de província no mesmo ano de tra
- Em um estudo territorial, a distinção entre Litoral, Zona da Mata, Agreste e Sertão é usad
- Um levantamento regional classifica Maceió e Arapiraca como os dois únicos municípios autô
- Uma análise das atividades econômicas estaduais considera compatíveis a importância histór
- Em um roteiro de memória cultural, os conjuntos históricos de Penedo e Piranhas são classi
- Ao comparar o Censo 2022 com uma estimativa demográfica de 2026, o pesquisador identifica 
- Um painel histórico identifica a destruição do núcleo principal de Palmares em 1695 e a mo
- Um estudo ambiental localiza o complexo Mundaú-Manguaba junto à paisagem costeira, enquant
- Uma proposta de reorganização territorial declara que a categoria estatística de região ge
- Um diagnóstico econômico identifica produção de cana e açúcar no leste estadual e expansão
- Uma curadoria cultural afirma que o fato de a Serra da Barriga integrar um circuito turíst
- Um pesquisador reconhece que o mesmo destino alagoano pode reunir patrimônio arquitetônico
- Uma cronologia afirma que a criação da Capitania de Alagoas em 1817 dependeu da proclamaçã
```

## Pedido 18: Sistema Financeiro Nacional (`conhecimentos-bancarios.sistema-financeiro-nacional`, 331 tópicos de edital)

```
Matéria: conhecimentos-bancarios.sistema-financeiro-nacional (Sistema Financeiro Nacional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura do SFN, conceito de instituição financeira e entidades supervisoras": 17
- "2. Bancos públicos e privados, investimento, desenvolvimento e cooperativismo": 24
- "3. Financeiras, arrendamento mercantil e financiamento imobiliário": 20
- "4. Mercado de capitais, corretoras, distribuidoras e competências de supervisão": 25
- "5. Seguros, previdência complementar e visão integrada da arquitetura financeira": 14

Já existem (não repita):
- Uma empresa realiza intermediação profissional de recursos de terceiros e guarda valores d
- Em relatório sobre o SFN, três entidades exercem funções diferentes: uma estabelece diretr
- Uma dúvida sobre as diretrizes gerais da política da moeda e do crédito é encaminhada ao a
- Uma instituição financeira é fiscalizada quanto à solidez, à liquidez e à observância das 
- Uma pessoa abre conta corrente e realiza depósitos à vista numa instituição especializada 
- Uma instituição privada organiza participação societária temporária e financiamento para e
- Uma iniciativa de infraestrutura busca recursos de longo prazo e apoio à expansão de ativi
- Uma pessoa ingressa numa entidade de crédito formada por associados que são, simultaneamen
- Uma sociedade anônima oferece crédito para aquisição de motocicletas, sem oferecer conta c
- Uma empresa utiliza equipamento que pertence à instituição arrendadora, pagando contrapres
- Uma sociedade anônima especializada em financiamento habitacional atualmente opera princip
- Uma entidade habitacional recebe depósitos de poupança cujos titulares se tornam associado
- Um investidor encaminha uma ordem de compra de ações a intermediária habilitada que execut
- Uma distribuidora de títulos e valores mobiliários habilitada pretende operar diretamente 
- Uma instituição recebe sanção administrativa aplicada pelo BCB e pretende recorrer à segun
- Uma instituição organiza a subscrição de debêntures e presta assessoria financeira a empre
- Um projeto regional de infraestrutura busca linha financiada por fundo constitucional do N
- Um conglomerado cooperativo cria instituição bancária controlada por cooperativas centrais
- Uma lista de instituições públicas inclui BB, Caixa, BNDES, Banco do Nordeste e Banco da A
- Uma empresa busca recursos de longo prazo no âmbito nacional para projeto de inovação, e o
- Uma financeira especializada em crédito ao consumidor pretende abrir contas correntes com 
- Um material sobre sociedades de crédito imobiliário afirma que, porque historicamente podi
- Uma associação de poupança e empréstimo recebe depósitos de pessoas interessadas em financ
- Ao revisar a captação de um banco de investimento, alguém cita o art. 5º da Resolução CMN 
- Uma distribuidora e uma corretora participam da colocação de valores mobiliários e executa
- Uma empresa pública que tenha como atividade a intermediação profissional de recursos fina
- Uma pessoa física exerce habitualmente atividade legalmente definida de intermediação fina
- No desenho institucional do SFN, o CMN figura como agente normativo, o BCB como supervisor
- Um banco de investimento oferece a seus clientes contas correntes de livre movimentação po
- Uma operação de leasing pode prever que a instituição arrendadora adquira bem escolhido pe
- Uma sociedade de crédito, financiamento e investimento pode abrir contas correntes à vista
- O BNDES utiliza recursos institucionais, entre eles o FAT, no financiamento do desenvolvim
- Um banco cooperativo controlado por cooperativas centrais perde a natureza de instituição 
- Uma sociedade de crédito imobiliário possui natureza de sociedade anônima, integra a ativi
- Por supervisionar ofertas públicas de ações e outros valores mobiliários, a CVM é também a
- Uma empresa contrata banco de investimento para organizar subscrição de valores mobiliário
- Uma associação de poupança e empréstimo, por atuar no financiamento habitacional, possui o
- Uma corretora habilitada executa compras de ações em bolsa por conta de investidores e pod
- Uma distribuidora de valores mobiliários habilitada está proibida de operar diretamente em
- Em uma associação de poupança e empréstimo, pessoa que deposita recursos para formar poupa
- Uma sociedade de arrendamento mercantil que concede o uso de máquina por prazo contratado 
- A Susep fiscaliza sociedades seguradoras e entidades abertas de previdência complementar s
- Uma entidade fechada de previdência complementar que administra plano restrito a empregado
- Diante de grave situação financeira de instituição sujeita a esse regime, o Banco Central 
- Depois de julgar recurso relativo a sanção bancária, o Conselho de Recursos do Sistema Fin
```

## Pedido 19: Parcerias público-privadas (Lei 11.079/2004) (`direito-administrativo.parcerias-publico-privadas`, 330 tópicos de edital)

```
Matéria: direito-administrativo.parcerias-publico-privadas (Parcerias público-privadas (Lei 11.079/2004))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, natureza e modalidades de PPP": 30
- "2. Requisitos, vedações e diretrizes da contratação": 18
- "3. Contrato, remuneração, riscos e garantias": 18
- "4. Sociedade de propósito específico e estrutura financeira": 17
- "5. Licitação, consulta pública e controle da PPP": 17

Já existem (não repita):
- Um contrato de longo prazo é tratado como PPP, conforme a Lei nº 11.079/2004. Qual combina
- Uma via explorada por concessionária recebe tarifas dos usuários e, também, pagamentos per
- Em projeto de prestação de serviços, a Administração será usuária indireta, com obras aces
- Em contrato rodoviário, usuários pagam tarifa e o poder público não realiza contraprestaçã
- Um projeto de PPP tem valor contratual global estimado em R$ 9,8 milhões e duração de quin
- Um serviço enquadrável como PPP será prestado por apenas quatro anos, em contrato de R$ 30
- Uma PPP foi projetada para trinta anos de vigência e admite prorrogação de mais dez anos. 
- Um edital pretende empregar PPP apenas para a construção de um prédio, sem prestação poste
- Uma proposta de parceria entrega ao particular o exercício de poder de polícia e a aplicaç
- O parceiro público financiará 74% da remuneração de concessionária patrocinada. Antes de c
- Durante a estruturação da PPP, as partes precisam tratar de força maior, fato do príncipe 
- Uma cláusula define atualização automática por fórmula e a Administração deseja impedi-la 
- O contrato prevê pagamento maior ao operador quando supera indicadores mensuráveis de disp
- Em PPP nova, pretende-se aportar recursos públicos para aquisição de bens reversíveis dura
- O contrato admite a entrega de um módulo já utilizável antes do restante do serviço. Quand
- Para garantir obrigações pecuniárias públicas, uma proposta oferece seguro-garantia. Segun
- Em licitação de PPP, o vencedor propõe criar a sociedade de propósito específico somente s
- Uma instituição financeira controlada pelo Poder Público adquire maioria do capital votant
- Um edital de PPP publicado sob a redação atual do art. 10 prevê escolher a modalidade de l
- Uma minuta de PPP foi submetida à consulta pública por vinte dias e a coleta de sugestões 
- Para licitação de PPP cujo objeto necessite de licenciamento ambiental, qual providência c
- Um edital de PPP foi publicado num exercício e o contrato será assinado em exercício finan
- Ao preparar a PPP, pretende-se julgar propostas pelo menor valor a ser pago pelo poder púb
- Em certame de PPP com propostas escritas seguidas de lances orais, o edital limita os lanc
- O edital de PPP prevê analisar a habilitação somente após classificar as propostas. O prim
- A Administração contrata projeto em que os usuários pagam tarifa e existe contraprestação 
- Um contrato em que a Administração é usuária indireta dos serviços está fora da concessão 
- Uma proposta no valor de exatamente R$ 10 milhões não viola, somente por esse valor, a pro
- Uma PPP para serviço prestado por quatro anos é admissível se o contrato tiver valor de ci
- Se o contrato de PPP previr duração inicial de 32 anos e prorrogação de três, estará, quan
- A execução isolada de obra pública pode ser o objeto único de PPP desde que o particular f
- Uma concessão remunerada por tarifas, sem contraprestação pecuniária do parceiro público a
- O contrato de concessão patrocinada no qual 70% exatos da remuneração privada forem pagos 
- Em PPP, a matriz contratual pode contemplar caso fortuito e força maior na repartição de r
- Um contrato de PPP pode prever pagamento integral da contraprestação relativa ao serviço a
- Um projeto pode oferecer garantia de pagamento público por fundo especial instituído em le
- A SPE pode ser constituída após a celebração do contrato se o edital estabelecer uma data 
- A sociedade de propósito específico pode assumir a forma de companhia aberta, observadas a
- O edital de PPP somente poderá empregar concorrência, uma vez que o diálogo competitivo co
- Antes da licitação, a consulta pública da minuta deve assegurar pelo menos trinta dias par
- O art. 11 determina obrigatoriamente arbitragem sediada no exterior e em idioma definido l
- O FGP poderá ser acionado, nas condições legais, após quinze dias de vencido crédito líqui
- O FGP deve admitir como aceita tacitamente qualquer fatura emitida e não aceita após trint
- O edital pode restringir lances em viva voz a licitantes cuja proposta escrita seja até vi
- Na inversão de fases autorizada pelo art. 13, a inabilitação do primeiro classificado dete
```

## Pedido 20: Patologia clínica: técnicas de laboratório, hematologia, bioquímica, urinálise, parasitologia e microbiologia (`medicina.patologia-clinica-laboratorio`, 328 tópicos de edital)

```
Matéria: medicina.patologia-clinica-laboratorio (Patologia clínica: técnicas de laboratório, hematologia, bioquímica, urinálise, parasitologia e microbiologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Laboratório: vidraria, microscopia, soluções, esterilização e fotometria": 17
- "2. Hematologia e coagulação": 17
- "3. Imuno-hematologia e imunologia": 17
- "4. Bioquímica, urinálise e líquido cefalorraquidiano": 17
- "5. Parasitologia clínica": 16
- "6. Microbiologia clínica": 16

Já existem (não repita):
- No laboratório clínico, a vidraria mais indicada para preparar uma solução com volume fina
- Utilizando-se ocular de 10× e objetiva de imersão de 100×, o aumento total obtido no micro
- Diluindo-se 10 mL de uma solução de concentração 2 mol/L até o volume final de 50 mL, a co
- Desinfecção e esterilização são sinônimos, pois ambas eliminam todas as formas de vida mic
- O método de esterilização por calor úmido, sob pressão, utilizado habitualmente a cerca de
- O soro é obtido a partir de sangue coletado sem anticoagulante e, por isso, não contém fib
- Segundo a Lei de Lambert-Beer, que fundamenta a fotometria, a absorbância de uma solução é
- O anticoagulante de escolha para a realização do hemograma é:
- Nos testes de coagulação com citrato de sódio, a proporção recomendada é de nove partes de
- Uma anemia microcítica e hipocrômica, com VCM e HCM reduzidos, sugere principalmente:
- O aumento de eosinófilos no hemograma está classicamente associado a parasitoses e a proce
- O tempo de tromboplastina parcial ativada (TTPa) avalia a via:
- A velocidade de hemossedimentação (VHS), cujo método clássico é o de Westergren, é marcado
- Os corantes de Leishman, Giemsa e Wright, utilizados na coloração do esfregaço sanguíneo, 
- Um indivíduo do grupo sanguíneo O possui em seu plasma, naturalmente:
- Na tipagem ABO, a prova direta testa o soro do paciente com hemácias A e B conhecidas, e a
- O teste de Coombs direto detecta:
- A prova cruzada maior, realizada antes de transfusões, consiste em testar o soro do recept
- A reação sorológica em que um antígeno particulado, como hemácias ou partículas de látex, 
- No ensaio ELISA, a detecção da reação antígeno-anticorpo baseia-se em um marcador enzimáti
- Em relação ao desempenho de testes sorológicos, a sensibilidade corresponde à capacidade d
- A hemólise de uma amostra de sangue pode elevar falsamente a dosagem de:
- Para a dosagem de glicemia de jejum, recomenda-se jejum de oito horas e coleta em tubo com
- Para a coleta de urina de 24 horas, o procedimento correto é:
- A presença de cilindros hemáticos no sedimento urinário sugere glomerulonefrite.
- O resultado positivo para nitrito na fita reagente de urina sugere a presença de:
- O líquido cefalorraquidiano normal apresenta aspecto:
- No acidente de punção lombar, o sangue presente no líquor tende a diminuir nos tubos colet
- O método parasitológico mais indicado para a pesquisa de larvas de Strongyloides stercoral
- O método de Graham, com fita adesiva aplicada na região perianal, é indicado para pesquisa
- O método de Kato-Katz destaca-se por:
- O método de Faust, baseado em centrífugo-flutuação em sulfato de zinco, é especialmente in
- A coprocultura e o exame parasitológico de fezes são sinônimos, pois ambos buscam ovos e l
- Na pesquisa de sangue oculto nas fezes, o método imunológico, em comparação com o do guaia
- Na coloração de Gram, a etapa que efetivamente diferencia bactérias gram-positivas de gram
- Na coloração de Gram, as bactérias gram-negativas aparecem coradas em roxo, por reterem o 
- A coloração de Ziehl-Neelsen é utilizada para evidenciar bacilos álcool-ácido resistentes,
- O ágar MacConkey é um meio de cultura:
- A prova da catalase é utilizada, rotineiramente, para diferenciar:
- O ágar Sabouraud é o meio clássico para isolamento de micobactérias, enquanto o Löwenstein
```

## Pedido 21: Recursos Hídricos: Política Nacional e SINGREH (Lei 9.433/1997) (`direito-ambiental.recursos-hidricos-lei-9433`, 327 tópicos de edital)

```
Matéria: direito-ambiental.recursos-hidricos-lei-9433 (Recursos Hídricos: Política Nacional e SINGREH (Lei 9.433/1997))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Águas na Constituição e fundamentos da Política Nacional": 20
- "2. Instrumentos: planos, enquadramento e sistema de informações": 20
- "3. Outorga e cobrança": 20
- "4. Poder Público e Sistema Nacional de Gerenciamento (SINGREH)": 20
- "5. Organizações civis, infrações e penalidades": 20

Já existem (não repita):
- Segundo a Constituição Federal, as águas subterrâneas, fluentes, emergentes e em depósito,
- Compete privativamente à União legislar sobre águas, cabendo-lhe também instituir o sistem
- São fundamentos da Política Nacional de Recursos Hídricos, EXCETO:
- Em situações de escassez, o uso prioritário dos recursos hídricos, segundo a Lei 9.433/199
- A unidade territorial para implementação da Política Nacional de Recursos Hídricos e atuaç
- Entre as diretrizes gerais de ação da Política Nacional de Recursos Hídricos está a gestão
- São instrumentos da Política Nacional de Recursos Hídricos, EXCETO:
- Os Planos de Recursos Hídricos são elaborados por bacia hidrográfica, por Estado e para o 
- Integra o conteúdo mínimo dos Planos de Recursos Hídricos:
- O enquadramento dos corpos de água em classes visa assegurar às águas qualidade compatível
- São princípios básicos do Sistema de Informações sobre Recursos Hídricos:
- A compensação a municípios consta da lista de instrumentos da Lei 9.433/1997, mas o artigo
- Está sujeito a outorga pelo Poder Público, segundo a Lei 9.433/1997:
- A extração de água de aquífero subterrâneo para consumo final ou insumo de processo produt
- A outorga de direito de uso de recursos hídricos far-se-á por prazo não excedente a:
- A outorga pode ser suspensa, parcial ou totalmente, em caso de ausência de uso por três an
- Sobre a outorga de direitos de uso de recursos hídricos, assinale a correta.
- A outorga e a utilização de recursos hídricos para geração de energia elétrica estão subor
- São objetivos da cobrança pelo uso de recursos hídricos, EXCETO:
- Serão cobrados os usos de recursos hídricos sujeitos a outorga.
- Os valores arrecadados com a cobrança pelo uso de recursos hídricos serão aplicados:
- A aplicação dos valores da cobrança no pagamento de despesas de implantação e custeio admi
- Integram o Sistema Nacional de Gerenciamento de Recursos Hídricos, EXCETO:
- Entre os objetivos do SINGREH está arbitrar administrativamente os conflitos relacionados 
- No Conselho Nacional de Recursos Hídricos, o número de representantes do Poder Executivo F
- Compete ao Conselho Nacional de Recursos Hídricos arbitrar, em última instância administra
- Compete aos Comitês de Bacia Hidrográfica, no âmbito de sua área de atuação:
- A instituição de Comitês de Bacia Hidrográfica em rios de domínio da União será efetivada 
- Nos Comitês de Bacia Hidrográfica de bacias cujos territórios abranjam terras indígenas, d
- Das decisões dos Comitês de Bacia Hidrográfica caberá recurso ao Conselho Nacional ou aos 
- A criação de uma Agência de Água é condicionada:
- As Agências de Água exercem a função de secretaria executiva do respectivo ou respectivos 
- Na implementação da Política Nacional de Recursos Hídricos, cabe aos Poderes Executivos do
- Constitui infração às normas de utilização de recursos hídricos, segundo a Lei 9.433/1997:
- Pela Lei 9.433/1997, com a redação dada em 2020, a multa por infração às normas de uso de 
- Quando a infração causar prejuízo ao serviço público de abastecimento de água ou riscos à 
- O embargo definitivo, previsto como penalidade na Lei 9.433/1997, pode vir acompanhado da 
- São consideradas organizações civis de recursos hídricos, para fins da Lei 9.433/1997, EXC
- A cobrança pelo uso de recursos hídricos tem natureza de imposto, sujeitando-se às limitaç
- Segundo a Lei 9.433/1997, os usos de recursos hídricos considerados insignificantes:
```

## Pedido 22: Gastroenterologia e hepatologia (`medicina.gastroenterologia-hepatologia`, 327 tópicos de edital)

```
Matéria: medicina.gastroenterologia-hepatologia (Gastroenterologia e hepatologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Doença do refluxo, dispepsia, úlcera péptica e H. pylori": 17
- "Hemorragia digestiva: reconhecimento, estratificação e abordagem": 17
- "Doenças inflamatórias intestinais, intestino irritável e câncer colorretal": 17
- "Pancreatites e doenças da vesícula e das vias biliares": 17
- "Hepatites, avaliação hepática e insuficiência hepática": 16
- "Cirrose, hipertensão portal e suas complicações": 16

Já existem (não repita):
- Paciente com pirose recorrente e regurgitação, sem anemia, emagrecimento nem disfagia, pro
- A ausência de esofagite visível na endoscopia digestiva alta exclui o diagnóstico de doenç
- Em paciente com dor epigástrica e uso habitual de anti-inflamatórios, qual conjunto reúne 
- Para documentar erradicação do Helicobacter pylori, o desaparecimento da dor epigástrica s
- Após tratamento de H. pylori, um paciente deseja saber se a bactéria permanece ativa. Qual
- Em paciente com perda de peso inexplicada, anemia e dispepsia progressiva, a investigação 
- Segundo informações do INCA sobre prevenção e detecção precoce, qual afirmação sobre cânce
- Hematoquezia intensa pode ocorrer em hemorragia digestiva alta de grande volume, apesar de
- Um paciente chega com hematêmese volumosa, hipotensão e extremidades frias. Qual é a prior
- Em hemorragia ulcerosa com sangramento ativo, adrenalina injetada isoladamente é considera
- Em cirrótico com hematêmese e suspeita clínica de ruptura de varizes esofágicas, qual comb
- A hemoglobina medida logo após o início de uma hemorragia grave pode ainda estar relativam
- Idoso apresenta episódios repetidos de hematoquezia indolor e anemia, e a colonoscopia mos
- Identificar hemorroidas ao exame clínico elimina a necessidade de investigar anemia ferrop
- Colonoscopia revela inflamação segmentar no íleo e cólon, associada a fístula perianal e e
- A retocolite ulcerativa tende a comprometer o reto e a estender-se continuamente pelo cólo
- Mulher jovem refere dor abdominal relacionada à evacuação, alteração da forma das fezes e 
- Calprotectina fecal elevada de modo persistente, sangue nas fezes e emagrecimento são acha
- Em retocolite ulcerativa leve a moderada de distribuição distal, qual estratégia medicamen
- A colectomia pode resolver a inflamação colônica da retocolite ulcerativa, enquanto ressec
- Indivíduo tem anemia ferropriva, perda ponderal e mudança recente do hábito intestinal. Qu
- O diagnóstico de pancreatite aguda pode ser firmado quando pelo menos dois dos três critér
- Paciente com pancreatite aguda leve melhora clinicamente, não vomita e manifesta apetite. 
- A pancreatite aguda classificada como grave é definida pela presença de falência orgânica 
- Paciente com dor no hipocôndrio direito, febre, icterícia e sinais de sepse apresenta dila
- A CPRE é a investigação diagnóstica inicial obrigatória de qualquer dor no hipocôndrio dir
- Paciente com história de pancreatite crônica apresenta esteatorreia, emagrecimento e defic
- A presença de álcool na história clínica permite atribuir automaticamente a pancreatite ag
- Um profissional avalia sorologia de indivíduo vacinado contra hepatite B, sem história de 
- Um teste anti-HCV positivo, por si só, comprova infecção ativa e torna desnecessário avali
- Paciente tem HBsAg não reagente, anti-HBs reagente e anti-HBc total reagente. Qual interpr
- ALT e AST elevadas medem diretamente a capacidade do fígado de sintetizar fatores de coagu
- Em pessoa sem cirrose prévia, qual quadro satisfaz a definição clínica clássica de insufic
- A hepatite D pode replicar-se independentemente da presença de vírus da hepatite B, razão 
- Paciente em investigação de icterícia apresenta fosfatase alcalina e gama-GT com elevação 
- Ascite, hemorragia varicosa e encefalopatia são manifestações de descompensação da cirrose
- Pessoa com cirrose chega ao hospital com ascite recente, discreta confusão e sem febre. Qu
- Contagem de polimorfonucleares de 300 células/mm³ no líquido ascítico é compatível com PBE
- Um paciente cirrótico apresenta desorientação e asterixis após quadro de constipação e inf
- O escore Child-Pugh inclui bilirrubina, albumina, parâmetros de coagulação, ascite e encef
- Em ascite com albumina sérica de 3,0 g/dL e albumina do líquido ascítico de 1,4 g/dL, qual
- A vigilância de carcinoma hepatocelular em pessoas cirróticas elegíveis pode incluir ultra
```

## Pedido 23: Análise de investimentos: VPL, TIR e payback (`matematica-financeira.analise-investimentos`, 325 tópicos de edital)

```
Matéria: matematica-financeira.analise-investimentos (Análise de investimentos: VPL, TIR e payback)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fluxo de caixa, valor presente e equivalência financeira": 20
- "2. TMA e valor presente líquido (VPL)": 16
- "3. Taxa interna de retorno e conflitos de decisão": 39
- "4. Payback simples e descontado": 13
- "5. Inflação e comparação integrada de alternativas": 12

Já existem (não repita):
- Uma atividade exige compra imediata de equipamento e gera recebimentos líquidos anuais. Qu
- Uma aplicação pagará R$ 121 ao final de dois anos. Considerando desconto composto de 10% a
- Um projeto está sendo comparado com a melhor aplicação alternativa de retorno exigido de 1
- Um investimento de R$ 100 hoje devolve R$ 110 ao fim de um ano. Sua TMA é 10% anual. Qual 
- Uma investidora aplica R$ 200 hoje a 10% ao ano, em juros compostos. Qual montante terá ex
- Um projeto apresenta saída de R$ 100 hoje e recebimento único de R$ 125 após um ano. Qual 
- Uma taxa efetiva de 1% ao mês é aplicada durante 12 meses com juros compostos. Qual taxa a
- Um projeto independente apresenta VPL de R$ 35 calculado à TMA adequada. Como deve ser int
- Um equipamento custa R$ 120 e gera entrada líquida constante de R$ 40 ao fim de cada um do
- Um projeto exige retorno real de 10% ao ano, enquanto a inflação esperada é 5% no mesmo in
- Uma empresa receberá R$ 100 ao final de cada um dos próximos dois anos. À taxa anual de 10
- Um projeto registra FC0=-100, FC1=55 e FC2=60,50. Qual taxa efetiva anual zera o VPL desse
- Um investimento inicial de R$ 200 produz caixa líquido de R$ 110 no fim do primeiro ano e 
- Um projeto custa R$ 100 e recebe R$ 66 no ano 1 e R$ 48,40 no ano 2. TMA de 10% ao ano. Qu
- Uma aplicação rende 12% nominais ao ano, enquanto os preços sobem 5% no ano. Qual foi a ta
- Duas alternativas são excludentes, ambas com duração de um ano. A: -100 e +150; B: -1.000 
- Um projeto convencional mantém os mesmos valores e datas, mas a TMA empregada aumenta de 8
- Dois projetos independentes, sem interferências entre si, apresentam VPLs de R$ 20 e R$ 35
- Um projeto exige R$ 100 hoje, devolve R$ 110 no ano 1 e recupera R$ 20 de capital de giro 
- Um fluxo não convencional apresenta FC0=-100, FC1=+230 e FC2=-132, nas mesmas unidades mon
- Dois projetos excludentes têm prazo de um ano e TMA de 20%. A: desembolso de 100 e retorno
- Há orçamento máximo de R$ 200. A exige R$ 100 e tem VPL 30; B exige R$ 100 e tem VPL 25; C
- Um investimento rende 1% efetivo ao mês por seis meses. Qual é o ganho acumulado efetivo a
- Para FC0=-100, FC1=+230 e FC2=-132, um avaliador adota TMA de 15% ao período. Qual procedi
- Um projeto projeta entrada de R$ 100 em moeda constante ao fim de um ano; inflação esperad
- Um projeto custa R$ 100 na data zero e devolve R$ 110 ao fim de um ano. Com TMA de 10% anu
- Um investimento de R$ 100 com entrada de R$ 110 no ano 1 tem payback simples calculado pel
- Se uma alternativa rende no máximo 8% ao ano, a taxa mínima exigida de 8% ao ano pode serv
- Uma aplicação que rende 1% ao mês em capitalização composta oferece exatamente 12% efetivo
- Com taxa real exigida de 10% ao ano e inflação anual esperada de 5%, uma taxa nominal de 1
- Se um investimento de R$ 100 retorna o capital em um ano pelo payback simples, o VPL será 
- Dois projetos independentes têm VPLs de R$ 20 e R$ 35 em bases coerentes. Caso possam ser 
- Na projeção de um novo equipamento, a despesa contábil de depreciação deve ser lançada com
- Para desembolso de R$ 100, receitas de R$ 66 ao fim do ano 1 e R$ 48,40 ao fim do ano 2 e 
- Ao elevar a TMA de 8% para 12%, mantendo todos os fluxos de um projeto idênticos, sua TIR 
- Um investimento com VPL exatamente igual a zero, à taxa exigida, recupera o capital em val
- Duas entradas de R$ 110 em t=0 e t=1 têm, à taxa de 10% ao período, o mesmo valor presente
- O projeto com fluxos -100, +230 e -132, em três datas anuais sucessivas, admite duas TIRs 
- Um projeto A gera VPL de R$ 25 e TIR de 50%; o projeto B excludente gera VPL de R$ 166,67 
- Para entrada real de R$ 100 em um ano, inflação prevista de 5% e retorno real exigido de 1
- Um projeto custa R$ 120 e recebe R$ 50 somente no encerramento de cada ano. Como os fluxos
- Um ganho acumulado de 21% em dois anos corresponde à taxa efetiva anual de 10% quando ocor
- Um fluxo com sinais -,+,- terá obrigatoriamente uma única TIR positiva, pois todo investim
- Uma carteira tem três projetos indivisíveis de VPL positivo, mas orçamento limitado; a som
- Dois projetos com idêntico prazo de payback simples necessariamente possuem o mesmo VPL, p
```

## Pedido 24: Salário, remuneração e salário mínimo (`direito-trabalho.salario-remuneracao`, 320 tópicos de edital)

```
Matéria: direito-trabalho.salario-remuneracao (Salário, remuneração e salário mínimo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Distinção entre salário e remuneração, gorjetas e reflexos": 21
- "2. Parcelas não salariais, prêmios e salário in natura": 14
- "3. Modalidades salariais, intangibilidade e formas de pagamento": 22
- "4. Salário mínimo, piso salarial, irredutibilidade e FGTS": 29
- "5. Décimo terceiro salário e articulação das garantias salariais": 14

Já existem (não repita):
- Em um restaurante, o empregado recebe R$ 2.300 de salário contratual e R$ 400 de gorjetas 
- Uma vendedora recebe R$ 1.600 fixos e R$ 500 de comissões de vendas contratadas, pagas pel
- Uma empresa paga a remuneração correspondente ao mês de junho. Sem hipótese especial, qual
- Para um empregado mensalista, qual valor do salário mínimo nacional está fixado para o ano
- Uma empregada trabalhou 15 dias computáveis em certo mês do ano para fins de gratificação 
- O empregador fornece uniformes obrigatórios para uso no serviço e cobra nenhuma quantia do
- Uma empresa paga R$ 700 a título de auxílio-alimentação por instrumento não convertido em 
- Um mensalista recebeu depósito salarial regular em conta bancária aberta em seu nome com c
- Um trabalhador recebe salário exclusivamente por comissões e, no mês, apura apenas R$ 1.10
- Em dezembro, uma pessoa possui remuneração-base mensal de R$ 3.600 e adquiriu oito avos de
- A empresa deseja incluir gorjetas na base de horas extraordinárias de atendente, alegando 
- Um empregador paga verba eventual, por liberalidade, a grupo que superou comprovadamente o
- Em determinado período, o empregador fornece habitação como autêntico salário-utilidade. S
- O empregado causou dano culposo por descuido comprovado e o empregador deseja descontá-lo 
- O empregador extingue contrato após transação de vendas efetuada em prestações sucessivas,
- Uma empresa pretende reduzir de R$ 3.000 para R$ 2.700 o salário-base de empregado por sim
- Um empregado deseja receber o adiantamento da gratificação de Natal juntamente com férias 
- Em ação sobre gorjetas distribuídas aos empregados, discute-se se elas integram as bases d
- Um empregado recebe parte do mínimo em utilidades salariais e o empregador pretende pagar 
- A remuneração mensal legal para FGTS em uma situação ordinária é de R$ 3.500, sem exclusõe
- Um empregador paga mensalmente quantia chamada de prêmio, mas ela corresponde à comissão o
- Um contrato estipula R$ 3.000 mensais com cláusula afirmando que esse montante já quita, d
- A gratificação natalina de um empregado com salário variável foi calculada em dezembro com
- Uma categoria possui piso válido superior ao salário mínimo nacional. O empregador sustent
- Uma pessoa recebe gorjetas e também auxílio-alimentação genuíno não pago em dinheiro. Ao a
- Um estabelecimento recebe gorjetas entregues diretamente por clientes aos empregados. Mesm
- Uma comissão contratual paga pelo empregador a cada venda concluída é classificada pelo ar
- Um empregado que recebe salário mensal possui direito à quitação desse salário, em regra, 
- Em 2026, o salário mínimo mensal nacional permaneceu em R$ 1.518,00, pois o Decreto nº 12.
- Um período de exatamente quinze dias computáveis de trabalho em determinado mês pode gerar
- A primeira parcela da gratificação natalina deve ser obrigatoriamente paga no mesmo dia a 
- Uma pessoa que recebe comissão de vendas paga pelo empregador e gratificação legal possui 
- A assistência médica, hospitalar e odontológica concedida pelo empregador integra obrigato
- O art. 458, § 3º, da CLT fixa limites diferentes para utilidades salariais: até vinte e ci
- Se um empregado provoca dano culposo à empresa sem cláusula ou acordo que permita desconto
- A Lei nº 8.036/1990, art. 15, estabelece depósito ordinário de FGTS sobre base remuneratór
- O empregado com remuneração integralmente variável pode receber montante mensal inferior a
- Para cálculo de décimo terceiro com salário variável, o Decreto nº 10.854/2021 estabelece 
- A Súmula 354 do TST, reafirmada pelo Tema 234 em 2025, determina que gorjetas integram ind
- Uma quantia paga todo mês sob título de prêmio não se enquadra automaticamente na exclusão
- Uma venda geradora de comissão foi concluída durante o vínculo, porém o pagamento parcelad
- A irredutibilidade do art. 7º, VI, da Constituição possui ressalva ligada a convenção ou a
- Um empregado que completou 12/12 do décimo terceiro pode receber o saldo bruto apenas em j
- Para cálculo do FGTS ordinário, a Lei nº 8.036/1990, art. 15, inclui a gratificação natali
- O empregado que pede, pela primeira vez, o adiantamento do décimo terceiro em agosto tem, 
```

## Pedido 25: Macroeconomia: moeda, inflação e política monetária (`economia.macroeconomia-moeda-inflacao`, 319 tópicos de edital)

```
Matéria: economia.macroeconomia-moeda-inflacao (Macroeconomia: moeda, inflação e política monetária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Moeda, agregados e contas do sistema monetário": 24
- "2. Bancos comerciais, criação de moeda e demanda monetária": 18
- "3. Inflação, índices de preços, juros e crescimento": 24
- "4. Instrumentos e transmissão da política monetária": 18
- "5. Curvas IS-LM, política fiscal e análise conjunta": 16

Já existem (não repita):
- Uma família utiliza dinheiro para comprar alimentos, comparar dois preços e reservar parte
- Ao apurar o agregado monetário de maior liquidez, uma tabela apresenta 500 em notas nas mã
- Ao apresentar a série brasileira de agregados amplos, uma pessoa pergunta onde se incorpor
- Uma pessoa recebe empréstimo de 4.000 diretamente como crédito em sua conta à vista. Qual 
- Em um modelo simplificado, a proporção de reservas é de 20%, sem espécie retida pelo públi
- A taxa anual de inflação recua de 9% para 3%, sem que o índice de preços diminua. A denomi
- O nível de um índice de preços cresce de 125 para 135. Sem alteração da cesta ou método, a
- A autoridade monetária compra títulos de uma instituição financeira, creditando reservas n
- Uma economia tem consumo dependente da renda disponível e investimento negativamente relac
- Uma aplicação rende 10% em termos nominais enquanto o nível geral de preços cresce 5%. Qua
- Uma tabela fictícia informa M2 de 900, quotas elegíveis de fundos de 140 e compromissadas 
- Uma autoridade eleva a fração de reservas exigidas de 10% para 20% em um exemplo com multi
- Em um exercício de demanda real por moeda, maior renda eleva saldos desejados e juros maio
- Uma empresa compara um empréstimo com juro nominal de 12% e inflação esperada de 7%. Que t
- Um aumento inesperado no preço de insumo essencial eleva custos empresariais e reduz a pro
- Um contrato é atualizado por inflação de 5% no primeiro período e 4% no segundo, sem novas
- Após elevação da taxa básica, os bancos encarecem empréstimos e empresas adiam projetos fi
- Uma autoridade monetária compra 50 em títulos que estavam no ativo de um banco e paga por 
- No modelo com retenção de espécie, a razão papel-moeda/depósitos é 0,20, a reserva obrigat
- No mercado de bens fechado, o consumo é 40 mais 0,75 da renda disponível, impostos fixos s
- Em exercício de LM, a demanda real por moeda é metade de Y menos dez vezes o juro nominal.
- Em economia de preços fixos e expectativas constantes, ocorrem simultaneamente aumento aut
- Uma expansão de gastos públicos com oferta real de moeda inalterada eleva a renda e a taxa
- Uma série hipotética informa M3 de 2.400 e 200 em títulos públicos federais elegíveis, alé
- Em modelo de demanda por moeda com grande sensibilidade aos juros, a LM fica quase horizon
- Uma pessoa mantém parte de seu patrimônio em cédulas e o restante em conta de depósitos à 
- Ao analisar o balanço monetário, um estudante inclui reservas bancárias em M1, por conside
- Ao exibir todos os preços de sua loja em uma mesma unidade monetária, um comerciante utili
- Em uma carteira de investimentos, o ativo de maior rendimento esperado é, por definição, o
- Quando um banco concede crédito e lança o valor na conta à vista de seu cliente, podem cre
- Quando a inflação anual cai de 8% para 2%, o nível médio de preços necessariamente diminui
- Em operação de mercado aberto, venda de títulos pela autoridade monetária com pagamento em
- No conceito brasileiro de M3, as cotas de qualquer fundo de investimento, inclusive fundos
- Se determinado índice de preços passa de 100 para 110 e a comparação usa a mesma cesta met
- O multiplicador de depósitos igual ao inverso da fração de reservas permite prever exatame
- Se uma aplicação rende 6% nominalmente e a inflação efetiva é de 9%, o rendimento real da 
- No IS-LM convencional, elevar a renda, com oferta real de moeda inalterada, desloca toda a
- No modelo de mercado de bens com preços fixos, aumento autônomo de G desloca a curva IS pa
- Em uma compra de títulos da carteira de um banco liquidada com novas reservas, o aumento d
- No multiplicador monetário com reserva obrigatória, reserva excedente e espécie mantida pe
- Se a política fiscal e a política monetária forem ambas expansionistas no modelo IS-LM usu
- Ao contratar uma aplicação para o próximo ano, a pessoa pode estimar o juro real esperado 
- No modelo keynesiano de economia fechada com tributo autônomo e propensão marginal a consu
- Quando a inflação reduz o poder de compra de saldos monetários mantidos sem remuneração co
- Na representação IS-LM que distingue corretamente os mecanismos, a IS responde ao juro nom
```

## Pedido 26: HTML, CSS, HTTP e APIs REST (`informatica.html-css-web`, 315 tópicos de edital)

```
Matéria: informatica.html-css-web (HTML, CSS, HTTP e APIs REST)
Capítulos e quantas questões quero em cada um (total 100):
- "1. HTML5, formulários, semântica, usabilidade e acessibilidade": 22
- "2. CSS3: seletores, cascata, modelo de caixa e layout responsivo": 25
- "3. HTTP, HTTPS, cache e infraestrutura web moderna": 15
- "4. APIs REST, JSON, XML, SOAP e GraphQL": 21
- "5. DOM, AJAX, CORS, WebSocket, SPA e PWA: integração prática": 17

Já existem (não repita):
- Uma página possui menu global, conteúdo principal e notícias independentes. Qual elemento 
- Um cartão de interface possui display flex e flex-direction: column. Qual propriedade dist
- Uma API usa GET para recuperar dados e POST para criar registros. Qual dos dois é definido
- Uma aplicação precisa transmitir objeto com identificador numérico e situação booleana. Qu
- Uma página em https://app.exemplo solicita por fetch dados de https://api.exemplo. O naveg
- Um formulário possui input id="email" e name="contato". Que marcação de rótulo cria associ
- Em auditoria de acessibilidade, uma página só responde ao mouse e oculta foco do teclado. 
- Uma galeria precisa de alinhamento em linhas e colunas independentes e utiliza grid-templa
- Uma integração utiliza mensagens XML com Envelope e Body e um documento contratual que des
- Um chat requer que servidor e navegador enviem mensagens espontaneamente pela mesma conexã
- Um portal é considerado fácil de usar pelo público sem deficiência, mas botões não recebem
- Duas regras normais do mesmo autor e mesma camada atingem o elemento p class="alerta" id="
- Um elemento usa box-sizing: content-box, width: 180px, padding: 10px em cada lado e borda 
- Uma API responde 401 ao cliente sem credencial válida e 403 a outro cliente identificado, 
- Navegador mantém ETag de uma representação e envia If-None-Match. A versão no servidor per
- Uma equipe encontrou o arquivo WSDL de um serviço SOAP e pretende usá-lo para gerar client
- Um documento XML possui uma raiz e tags corretamente aninhadas, mas seu campo idade contém
- Um serviço GraphQL declara um esquema tipado e recebe consulta apenas dos campos nome e ci
- Um sistema utiliza gRPC sobre HTTP/2 para comunicação entre serviços e precisa transmitir 
- Um script usa fetch para obter `/dados`, mas o servidor responde HTTP 404 normalmente. Qua
- Uma regra CSS normal sem camada define `p { color: red }`; outra regra normal na camada `@
- Uma API afirma seguir REST, mas exige que o cliente envie identificador de sessão implícit
- Um XML bem-formado deve ser verificado contra restrições de tipos e depois transformado em
- Um aplicativo de chat inicia a troca WebSocket clássica sobre HTTP/1.1 e recebe `101 Switc
- Uma aplicação já funciona como SPA e a equipe quer torná-la instalável e oferecer parte de
- Um formulário contém `label for="usuario"` e `input id="usuario"`. A correspondência entre
- Em CSS, ao utilizar `box-sizing: border-box`, a largura definida pela propriedade width in
- Uma API recebe duas requisições DELETE idênticas para remover o mesmo recurso. O fato de a
- Em JSON, objetos podem empregar comentários de linha e vírgula após o último par chave-val
- Uma interface atualiza a lista de mensagens por chamadas assíncronas à API, sem recarregar
- Uma aplicação recebe `Set-Cookie` com atributo `HttpOnly`. Esse atributo garante criptogra
- No CSS, uma declaração `display: none` retira a caixa do layout, diferentemente de `visibi
- Um documento XML com apenas uma raiz, tags bem aninhadas e atributos entre aspas é necessa
- Em API web, `Cache-Control: no-cache` não é sinônimo de `no-store`: o primeiro exige reval
- Uma resposta da API com `Access-Control-Allow-Origin: *` pode ser exposta livremente a um 
- Em um portal, o CSS posiciona um elemento com `position: absolute`. A caixa deixa o fluxo 
- Em HTTP/2, o uso de frames binários e streams multiplexados modifica o significado de GET:
- Ao receber documento SOAP 1.2, um processador encontra Envelope e Body em XML, sem Header.
- Uma equipe utiliza XSLT para transformar XML em HTML e XSD para validar tipos. No processo
- Uma aplicação utiliza GraphQL e fornece `query`, `mutation` e `subscription`. O esquema ti
- No navegador, DHTML corresponde a uma linguagem padronizada distinta de HTML, CSS e JavaSc
- Um serviço apresenta vários microsserviços por trás de proxy reverso e balanceador de carg
- Ao criar uma SPA, a equipe deve excluir histórico de navegação e gerenciamento de foco, po
- Um portal adotou elementos HTML nativos, suporte a teclado, foco visível e alternativas te
- Uma PWA que registra manifest e service worker garante todas as operações offline sem nece
```

## Pedido 27: Tratados de direitos humanos no ordenamento brasileiro (`direito.tratados-direitos-humanos-ordenamento-brasileiro`, 314 tópicos de edital)

```
Matéria: direito.tratados-direitos-humanos-ordenamento-brasileiro (Tratados de direitos humanos no ordenamento brasileiro)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tratados de direitos humanos e incorporação no Brasil": 18
- "2. Hierarquia normativa dos tratados e jurisprudência constitucional": 17
- "3. Interpretação dos tratados e colisão entre direitos humanos": 33
- "4. Controle de constitucionalidade e controle de convencionalidade": 17
- "5. Sistema Interamericano, diálogo entre cortes e casos avançados": 15

Já existem (não repita):
- Uma pessoa invoca direito humano assegurado por tratado vigente para o Brasil, mas não rep
- O Brasil negocia um tratado de proteção à infância. Antes da tramitação parlamentar, é nec
- Após aprovar o texto de uma convenção de direitos humanos pelo procedimento parlamentar pe
- Um tratado internacional de direitos humanos foi regularmente incorporado, sem aprovação p
- Para obter equivalência constitucional, um tratado de direitos humanos precisa de aprovaçã
- Uma pessoa foi ameaçada de prisão civil por descumprimento de obrigação contratual de depo
- Diante de cláusula ambígua em tratado multilíngue de direitos humanos, a pessoa intérprete
- Uma interpretação de tratado permitiria restringir direito reconhecido de forma mais ampla
- Uma pessoa pretende denunciar violação da Convenção Americana por meio de petição individu
- Uma convenção foi aprovada pelo Parlamento e ratificada pelo Executivo; a secretaria regis
- Um tratado de direitos humanos foi incorporado antes de 2004, sem aprovação por três quint
- Foi promulgado tratado de direitos humanos previamente aprovado pela Câmara e pelo Senado 
- Dois textos igualmente autênticos de tratado de direitos humanos apresentam divergência se
- Uma restrição à liberdade de expressão é defendida para proteger a privacidade de terceiro
- Lei infraconstitucional autoriza prática que contraria tratado de direitos humanos incorpo
- Uma pessoa apresenta petição à Comissão Interamericana seis meses e alguns dias após ciênc
- Um relatório afirma que decisão da Corte Interamericana contra o Brasil deve ser observada
- O Estado afirma em uma controvérsia internacional que não pôde cumprir o tratado porque su
- Uma lei brasileira de 2025 contraria cláusula de tratado de direitos humanos promulgado em
- Um ato normativo federal é questionado por ofender diretamente a Convenção sobre os Direit
- Um intérprete invoca o princípio pro persona para sustentar publicação integral de dados í
- Diante de grave emergência pública, propõe-se suspensão temporária de garantias, incluindo
- Um caso submetido ao sistema regional discute fatos exclusivamente ocorridos em 1995 no Br
- Uma decisão contenciosa foi proferida contra outro Estado, e parecer consultivo interpreta
- Uma vítima apresenta petição à Comissão Interamericana sem esgotar recursos internos porqu
- Uma pessoa identifica garantia reconhecida em tratado de direitos humanos de que o Brasil 
- Após assinar uma convenção internacional que exige ratificação, a autoridade brasileira co
- Uma convenção de direitos humanos foi aprovada por maioria absoluta em um turno em cada Ca
- Um intérprete que usa boa-fé, sentido comum dos termos, contexto e objeto e finalidade de 
- Diante de ordem de prisão civil de depositário infiel baseada em legislação ordinária, a d
- Uma vítima de violação à Convenção Americana pode protocolar diretamente ação contenciosa 
- Lei brasileira posterior incompatível com tratado de direitos humanos supralegal incorpora
- Em controvérsia internacional, a alegação de que uma lei doméstica impôs o descumprimento 
- Quando existe texto convencional claro interpretado de boa-fé conforme contexto e finalida
- Ao escolher entre duas interpretações convencionais juridicamente plausíveis, um intérpret
- Uma emergência pública reconhecida nos termos da Convenção Americana permite suspender tod
- Quando uma lei ordinária conflita com tratado de direitos humanos supralegal, o juiz pode 
- O controle de convencionalidade só pode ser realizado pela Corte Interamericana, pois juíz
- O Tratado de Marraqueche, aprovado pelo Decreto Legislativo nº 261/2015 e promulgado pelo 
- Mesmo depois de reconhecida a ilicitude da prisão civil do depositário infiel, o texto do 
- O reconhecimento brasileiro da jurisdição obrigatória da Corte Interamericana, promulgado 
- Se o Brasil figura como parte em caso contencioso decidido pela Corte Interamericana, há c
- Uma petição internacional contra o Brasil deve ser recusada automaticamente sempre que não
- Se duas restrições são igualmente adequadas para proteger a intimidade e uma provoca menor
- Em tratado autenticado em três idiomas, o art. 33 da Convenção de Viena estabelece que, na
```

## Pedido 28: Questões e processos incidentes e medidas assecuratórias (`processo-penal.questoes-incidentes-medidas-assecuratorias`, 313 tópicos de edital)

```
Matéria: processo-penal.questoes-incidentes-medidas-assecuratorias (Questões e processos incidentes e medidas assecuratórias)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Questões prejudiciais, exceções e conflitos de jurisdição": 13
- "2. Restituição de coisas apreendidas e efeitos civis do delito": 29
- "3. Sequestro, hipoteca legal, arresto e alienação antecipada": 22
- "4. Falsidade documental, insanidade mental e garantias incidentais": 18
- "5. Perdimento, ações patrimoniais e resolução integrada de casos": 18

Já existem (não repita):
- Em processo penal, a existência do crime depende de controvérsia séria e fundada sobre o e
- Uma parte pretende suscitar formalmente uma das exceções arroladas no art. 95 do CPP. Qual
- Um relógio lícito apreendido deixou de interessar à investigação; a titularidade do requer
- Após investigação, são encontrados indícios veementes de que um imóvel foi comprado com pr
- Durante um processo, a defesa argui por escrito falsidade de contrato juntado aos autos. S
- Diante de dúvida relevante acerca da integridade mental de uma pessoa acusada, o juiz dete
- Dois juízos afirmam simultaneamente ser competentes para processar o mesmo fato penal. No 
- Um ofendido pretende executar, no juízo cível, a obrigação de indenizar reconhecida em sen
- Em busca de assegurar indenização à vítima, pretende-se gravar imóvel lícito do investigad
- Um veículo sujeito a constrição judicial sofre depreciação acelerada. Aplicando-se o art. 
- Sobre uma questão cível distinta do estado civil e determinante da infração, já existe açã
- Uma coisa foi apreendida em poder de terceiro de boa-fé. Outra pessoa pede devolução, mas 
- Depois de concluída a diligência de sequestro patrimonial, a acusação deixa de propor ação
- Para resguardar futura hipoteca legal, é inicialmente arrestado um imóvel. Em qual prazo s
- Uma parte pretende alegar, no mesmo processo, suspeição do juiz e incompetência do juízo, 
- Um terceiro recebeu mediante pagamento bem sequestrado e apresenta embargos, alegando aqui
- O exame médico-legal conclui que o acusado já apresentava incapacidade relevante no moment
- Uma questão cível difícil, distinta de estado civil e com ação cível em curso, leva a pedi
- No procedimento de especialização de hipoteca legal, o investigado oferece caução integral
- Em alienação antecipada pelo CPP, o primeiro leilão não atinge a avaliação judicial. O juí
- Concluído sequestro de imóvel obtido com proventos criminosos, transcorreram 65 dias sem q
- Em caso de condenação, pretende-se a perda ampliada do patrimônio incompatível com rendime
- O acusado foi definitivamente absolvido por sentença irrecorrível, mas seus bens continuam
- A defesa suscita incidente de falsidade por procurador com poderes específicos; após instr
- Os bens gravados por hipoteca e arresto não bastam para cobrir todas as obrigações resulta
- Em ação penal dependente de questão séria de estado civil, o juiz pode determinar de ofíci
- Quando a questão cível prejudicial não versa sobre estado civil, o juiz criminal é obrigad
- Apresentada exceção de incompetência de juízo, o processo penal principal fica necessariam
- Uma pessoa pede a restituição de objeto lícito apreendido, e o direito à devolução está cl
- Com indícios veementes de que imóvel foi adquirido com proventos da infração, o sequestro 
- A alienação antecipada de um veículo que perde valor, realizada para evitar depreciação, i
- Em incidente de restituição com dúvida acerca do direito do reclamante, o delegado poderá 
- Se a pessoa responsável não tiver imóveis, ou se forem insuficientes para assegurar a resp
- A hipoteca legal sobre imóvel lícito e o sequestro de imóvel adquirido com proventos do cr
- Dois juízos sustentam ser competentes para a mesma infração. No conflito positivo instaura
- No incidente de falsidade documental, o juiz pode proceder de ofício à verificação da fals
- Comprovada doença mental iniciada após a infração, a ação penal prossegue obrigatoriamente
- Sem prejuízo da execução civil da sentença penal definitiva, o ofendido pode propor ação c
- Segundo o art. 130 do CPP, os embargos do acusado contra sequestro devem ter decisão defin
- No procedimento do art. 112 do CPP, impedimento ou incompatibilidade legal de perito pode 
- A decisão do incidente de falsidade que reconhece autenticação irregular impede, por força
- A absolvição penal torna obrigatória a improcedência de qualquer ação de reparação civil, 
- O Código Penal permite perda de bens equivalentes ao produto ou proveito do crime quando t
- Indeferida a suspensão da ação penal no caso do art. 93 do CPP, o dispositivo prevê recurs
- Após sentença condenatória transitada em julgado que decreta perdimento, o art. 133 do CPP
```

## Pedido 29: Cirurgia geral: avaliação perioperatória, abdome agudo, hérnias e vias biliares (`medicina.cirurgia-geral-abdome-perioperatorio`, 311 tópicos de edital)

```
Matéria: medicina.cirurgia-geral-abdome-perioperatorio (Cirurgia geral: avaliação perioperatória, abdome agudo, hérnias e vias biliares)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação pré-operatória e estratificação do risco": 17
- "Cuidados perioperatórios, metabolismo, fluidos e infecção": 17
- "Abdome agudo não traumático: síndromes e investigação": 17
- "Apendicite, vesícula, colédoco e pancreatite": 17
- "Hérnias, obstrução intestinal e hemorragia digestiva": 16
- "Cirurgia pediátrica frequente e vigilância de complicações": 16

Já existem (não repita):
- Paciente assintomático, com boa capacidade funcional, realizará procedimento eletivo de ba
- A classificação ASA de estado físico é utilizada na assistência perioperatória para:
- Na checagem de cirurgia segura proposta pela OMS, a melhor prática é:
- Qual dos achados é típico da resposta metabólica ao estresse de uma operação de grande por
- Após operação abdominal, um paciente apresenta perdas gástricas persistentes por sonda. Qu
- Em que situação a antibioticoprofilaxia cirúrgica é melhor descrita?
- Paciente de 78 anos com fibrilação atrial desenvolve dor abdominal súbita e muito intensa,
- Sobre abdome agudo perfurativo, qual assertiva está correta?
- Adulto com dor que migrou do umbigo para fossa ilíaca direita e sinais compatíveis com ape
- Paciente com dor persistente no hipocôndrio direito, febre e suspeita de colecistite calcu
- Mulher com coledocolitíase suspeita apresenta bilirrubina total elevada e enzimas colestát
- Homem com dor epigástrica típica irradiada para dorso e lipase quatro vezes acima do limit
- Na pancreatite aguda sem sinais de infecção, qual plano nutricional é mais adequado?
- No exame de hérnia inguinal, a hérnia direta se caracteriza por:
- Paciente com história de cirurgia abdominal apresenta obstrução delgada por aderências, se
- Após hemostasia endoscópica para úlcera duodenal, ocorre ressangramento. Qual sequência é 
- Lactente com vômitos verdes (biliosos) progressivos e distensão abdominal deve ter como pr
- Sobre prepúcio infantil, qual conduta está alinhada a diretrizes de urologia pediátrica?
- Criança tem episódios de choro e dor abdominal em cólica, alternando com períodos de melho
- Menino com dor testicular súbita, testículo elevado e náuseas apresenta forte suspeita de 
- Em pessoa com hérnia dolorosa irredutível, vômitos e alteração de cor da pele sobre a tume
- Cirurgia abdominal eletiva limpa, sem implantes e sem fatores especiais de risco: qual afi
- Na colecistite aguda calculosa, em pessoa que pode ser operada, a estratégia definitiva ge
- Diante de pós-operatório com evisceração, a medida adequada enquanto se organiza revisão c
- Uma paciente com pancreatite biliar leve, recuperada clinicamente durante a internação, se
- O risco perioperatório deve integrar características do paciente e da cirurgia; a idade cr
- A administração de teste de esforço a todos os pacientes, independentemente do tipo de cir
- A classificação ASA descreve o estado físico global, mas não informa, isoladamente, probab
- A presença de dreno cirúrgico, mesmo com ferida fechada e sem infecção, é justificativa su
- A infecção de órgão ou espaço pode ocorrer em estrutura manipulada no procedimento mesmo s
- Perdas persistentes por sonda gástrica podem causar alcalose metabólica hipoclorêmica; por
- No adulto estável, o volume estimado para manutenção intravenosa equivale obrigatoriamente
- Em obstrução intestinal, a eliminação de pequenas quantidades de fezes no início do quadro
- Ausência de leucocitose exclui apendicite aguda, sobretudo quando o paciente apresenta dor
- Apendicite não complicada pode receber manejo não operatório com antibióticos em pacientes
- A tríade de Charcot, quando presente, é sugestiva de colangite, mas sua ausência não permi
- Na estratificação ASGE, bilirrubina total superior a 4 mg/dL, sem dilatação da via biliar,
- A pancreatite aguda pode ser diagnosticada com dor característica e lipase pelo menos três
- Antibioticoterapia profilática deve ser prescrita para toda pancreatite aguda, inclusive q
- Uma hérnia encarcerada é necessariamente uma hérnia estrangulada, pois a impossibilidade d
- A hérnia inguinal indireta se localiza lateral aos vasos epigástricos inferiores e percorr
- A obstrução por aderências com evidência de peritonite ou suspeita de isquemia deve contin
- Na hemorragia digestiva alta não varicosa, a falha da hemostasia endoscópica pode motivar 
- A não retratilidade do prepúcio sem sintomas em crianças pequenas obriga circuncisão imedi
- Na suspeita clínica forte de torção testicular, o ultrassom Doppler não deve atrasar a ava
```

## Pedido 30: Hidrologia e obras hidráulicas (`engenharia.hidrologia-obras-hidraulicas`, 309 tópicos de edital)

```
Matéria: engenharia.hidrologia-obras-hidraulicas (Hidrologia e obras hidráulicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ciclo hidrológico, meteorologia e balanço hídrico": 21
- "2. Hidrogramas, medição de vazões e análise de extremos": 22
- "3. Hidráulica de canais, condutos e travessias": 18
- "4. Barragens, vertedouros, reservatórios e aproveitamentos hidráulicos": 24
- "5. Abastecimento, esgotamento sanitário e integração operacional": 15

Já existem (não repita):
- Uma bacia de 3 km² recebeu precipitação uniforme de 12 mm. Desprezando perdas apenas para 
- Uma seção de canal possui área molhada de 4 m² e velocidade média de 1,5 m/s. Qual vazão p
- Em balanço anual simplificado, uma bacia recebeu 1.000 mm de chuva, apresentou evapotransp
- Um pluviômetro registrou 24 mm acumulados em 3 horas. Para caracterizar somente a intensid
- Uma estação convencional receberá água superficial com turbidez e partículas em suspensão.
- Em uma área com déficit de água no solo, a demanda evaporativa atmosférica é elevada. Que 
- No estudo de disponibilidade de um rio foi estimada Q95 = 4 m³/s com base na curva de perm
- Após chuva intensa numa encosta impermeabilizada, verifica-se aumento rápido da descarga n
- Uma estação mede níveis de rio continuamente, mas estima vazões por curva-chave. Qual proc
- Um aproveitamento hidrelétrico apresenta queda elevada e vazão relativamente pequena. Qual
- A velocidade média e a profundidade hidráulica em um canal fornecem número de Froude de 1,
- Um canal retangular possui largura de fundo de 3 m e profundidade de água de 1 m. Qual é s
- Adotando máximas anuais independentes e condições estacionárias, qual é a probabilidade an
- Um reservatório recebe 3 m³/s e libera 2 m³/s, sem outras entradas ou perdas no intervalo 
- Uma adutora pressurizada apresenta perdas de energia por atrito ao longo do comprimento. Q
- Em rio com vazão de 2 m³/s, uma amostra representativa indica concentração média de sedime
- Para projetar a drenagem de uma bacia sem séries de vazão, foram encontrados dados de baci
- Uma precipitação uniforme de 30 mm incide em 2 km² e uma estimativa simplificada admite co
- Um dique protegerá área urbana durante 10 anos e foi dimensionado para evento de retorno d
- Uma turbina opera com vazão de 5 m³/s, queda líquida de 40 m, rendimento global de 80%, de
- Em dois trechos de canal geometricamente idênticos, com mesma área, raio hidráulico e decl
- Durante 10 horas, afluem a um reservatório 3 m³/s; saem 2 m³/s pelo vertedouro e 0,2 m³/s 
- Em projeto de bueiro, a seção escolhida suporta vazão calculada por escoamento uniforme, m
- Uma tubulação pressurizada apresenta perda distribuída de 1,2 m e duas singularidades com 
- Uma ETE de lagoas facultativas recebe esgoto afluente e o projeto considera remoção de DBO
- Uma estação registrou precipitação de 1 mm sobre uma bacia de 1 km². O volume bruto corres
- Uma cheia com retorno estimado de 100 anos, registrada neste mês, torna impossível nova oc
- Um projeto dispõe de grande queda hidráulica e vazão relativamente pequena. Nesse cenário,
- Ao calcular o perímetro molhado de um canal retangular, o projetista deve adicionar ao fun
- Numa estação de tratamento de esgotos, a etapa preliminar pode utilizar grades e desarenad
- Um estudo adota Q95 igual a 3 m³/s. É correto afirmar que essa é a vazão máxima de uma che
- Durante campanha hidrométrica, a utilização de ADCP pode fornecer perfis de velocidade e a
- Em reservatório de geometria irregular, o aumento do nível em 1 m gera acréscimo de volume
- A retenção de sedimentos a montante de uma barragem pode diminuir, ao longo do tempo, a ca
- Uma residência instala tanque séptico com digestão anaeróbia. Esse tanque, por definição, 
- Em canal com escoamento aproximadamente uniforme e superfície livre, a equação de Manning 
- Uma bacia teve extensa impermeabilização e mudanças de padrões de chuva. Para estimar a ch
- Para cheia de tempo de retorno 20 anos, a probabilidade anual de excedência é 5%, supondo 
- Para propagar um hidrograma ao longo de um trecho de rio pelo método de Muskingum, é sufic
- Em uma adutora, a troca de comando para fechamento rápido de válvula pode desencadear tran
- Em sistema de bombeamento, uma redução adicional da pressão absoluta na sucção diminui a p
- Com Q = 10 m³/s, queda líquida de 25 m, densidade da água de 1.000 kg/m³, g = 9,81 m/s² e 
- Em um canal retangular com largura de 2 m e profundidade de 1 m, raio hidráulico e profund
- Na gestão de barragem sujeita à Política Nacional de Segurança de Barragens, o Plano de Se
- Em encosta, o escoamento por excesso de saturação só pode ocorrer se a intensidade da prec
```

## Pedido 31: Aprendizagem organizacional, educação corporativa e gestão do conhecimento (`gestao-pessoas.aprendizagem-educacao-corporativa`, 308 tópicos de edital)

```
Matéria: gestao-pessoas.aprendizagem-educacao-corporativa (Aprendizagem organizacional, educação corporativa e gestão do conhecimento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Aprendizagem individual e teorias de aprendizagem": 17
- "2. Aprendizagem organizacional e gestão do conhecimento": 31
- "3. Educação corporativa, EaD e desenho instrucional": 21
- "4. Desenvolvimento gerencial, autodesenvolvimento e metodologias ágeis": 16
- "5. Avaliação de treinamento e Política Nacional de Desenvolvimento de Pessoas": 15

Já existem (não repita):
- Uma pessoa concluiu uma palestra sobre feedback e respondeu corretamente a um questionário
- Em uma oficina, o participante recebe devolutivas imediatas e reconhecimento ao executar c
- Uma equipe reorganizou a escala de trabalho após registrar atrasos, preservando a mesma re
- Durante um programa de sucessão, uma pessoa experiente acompanha outra na execução diária 
- Na elaboração de um curso, a equipe começa examinando o desempenho esperado, a experiência
- Um curso digital usa videoconferência ao vivo para debates e fórum com respostas distribuí
- Uma instituição quer formar pessoas que conduzam reuniões mais objetivas e acompanhem seus
- Após uma oficina, os participantes elogiaram o material, a utilidade percebida e a conduçã
- Uma equipe instala um repositório com centenas de procedimentos, mas os documentos não são
- Durante o diagnóstico, verifica-se que os profissionais dominam uma tarefa, mas falham por
- Em uma avaliação de conhecimentos, uma equipe foi convidada a recuperar de memória decisõe
- Uma pessoa experiente transforma seus critérios informais de decisão em um roteiro escrito
- Uma equipe testa um módulo reduzido de capacitação com usuários reais, corrige dificuldade
- Em uma trilha de desenvolvimento, uma pessoa acompanha reuniões de uma facilitadora experi
- Durante o acompanhamento de um curso, a equipe identifica que participantes com baixa cone
- Uma equipe elabora para o próximo exercício o documento de planejamento das necessidades d
- Uma equipe percebe que sucessivos ajustes em uma escala falham. Ela investiga a premissa d
- Uma organização verificou que participantes de um curso de diagnóstico acertam casos simul
- Um programa de aprendizagem introduziu novos roteiros de atendimento e uma melhoria tecnol
- O desenho de um curso prevê a competência 'resolver conflitos em conversas difíceis', mas 
- Em uma instituição, integrantes de diferentes áreas reúnem relatórios e manuais já documen
- Na implementação de um programa gerencial, uma equipe usa um quadro visual para limitar ta
- Uma equipe planeja uma ação de desenvolvimento prevista no PDP e decide avaliá-la apenas p
- Uma participante deseja estudar uma habilidade e propõe plano próprio; a instituição dispo
- A unidade responsável pelo desenvolvimento recebe uma demanda de curso porque os resultado
- Em uma capacitação, um participante observa uma demonstração feita por profissional experi
- Uma instituição considera comprovada a mudança de comportamento de seus trabalhadores porq
- Quando uma pessoa experiente acompanha outra na execução de tarefas e compartilha percepçõ
- Em um ambiente virtual, uma reunião por videoconferência em tempo real é atividade assíncr
- Uma equipe de desenho instrucional levanta o perfil do público e as lacunas de desempenho 
- Após perceber queda de produtividade provocada por falhas do equipamento, uma unidade deve
- Uma unidade identifica necessidades de desenvolvimento alinhadas aos objetivos institucion
- Uma instituição classificou como aprendizagem de ciclo duplo a decisão de modificar apenas
- Após um treinamento sobre conversas difíceis, a observação de reuniões realizadas semanas 
- Uma equipe que combina manuais e relatórios escritos para organizar nova base de procedime
- Durante o planejamento de um programa educacional, definir objetivos observáveis e os crit
- Uma equipe de gestão considera que um quadro Kanban substitui automaticamente o diagnóstic
- Em um projeto de capacitação com prazos definidos e entregas específicas, uma retrospectiv
- Como o PDP prevê ações de desenvolvimento, o relatório anual de execução tem função idênti
- Uma pessoa elaborou plano de autodesenvolvimento e recebeu orientação de colega experiente
- Uma unidade afirma ter comprovado que seu curso gerou redução de erros, embora tenha troca
- Um curso virtual corporativo pode combinar conteúdos gravados, fóruns, sessões ao vivo e a
- Uma equipe usa o Decreto nº 9.057/2017 como fundamento ainda vigente para regular cursos s
- Após sistematizar lições aprendidas, uma equipe modifica o procedimento de atendimento e i
- Uma avaliação aplicou tarefas práticas antes e depois do curso e constatou ganho de domíni
```

## Pedido 32: Ginecologia: anatomia, doenças benignas e malignas, climatério e contracepção (`medicina.ginecologia`, 308 tópicos de edital)

```
Matéria: medicina.ginecologia (Ginecologia: anatomia, doenças benignas e malignas, climatério e contracepção)
Capítulos e quantas questões quero em cada um (total 100):
- "Anatomia ginecológica, fisiologia menstrual e sangramento uterino anormal": 17
- "Endometriose, miomas, distopias genitais e distúrbios urinários": 17
- "Doença inflamatória pélvica, vulvovaginites e tuberculose genital": 17
- "Mama e neoplasias ginecológicas: lesões precursoras e rastreamento": 17
- "Climatério, contracepção, planejamento familiar e infertilidade": 16
- "Atendimento à violência sexual e integração clínica para concursos": 16

Já existem (não repita):
- No procedimento de histerectomia, o ureter cruza inferiormente a artéria uterina nas proxi
- A queda brusca de FSH na fase folicular é o evento hormonal diretamente responsável pela r
- Na classificação PALM–COEIN, coagulopatia e disfunção ovulatória pertencem ao grupo de cau
- Em mulher em idade reprodutiva com sangramento uterino anormal, a ausência de náuseas é su
- Uma ultrassonografia pélvica normal exclui com segurança todas as formas de endometriose, 
- Leiomiomas submucosos podem causar sangramento uterino anormal e repercussões na fertilida
- Prolapso uterino e incontinência urinária de esforço correspondem obrigatoriamente ao mesm
- A perda urinária que ocorre imediatamente após forte desejo súbito de urinar caracteriza c
- O diagnóstico de DIP exige obrigatoriamente resultado positivo para gonococo ou clamídia a
- Candidíase vulvovaginal costuma cursar com prurido e pH vaginal preservado, diferentemente
- Na tricomoníase, o tratamento de parcerias sexuais é irrelevante, pois se trata de desequi
- A tuberculose genital feminina pode ocasionar infertilidade por comprometimento tubário me
- A recomendação populacional atual de mamografia de rastreamento no SUS prioriza mulheres a
- O resultado positivo do teste molecular de HPV oncogênico equivale, por definição, ao diag
- O sangramento vaginal ocorrido após a menopausa deve ser investigado, mesmo quando a pacie
- A dosagem isolada de CA-125, feita anualmente em pessoas assintomáticas de risco habitual,
- Para confirmar retrospectivamente a menopausa natural, utilizam-se 12 meses consecutivos d
- Em paciente com útero preservado, é correto prescrever estrogênio sistêmico isolado por te
- A assistência de saúde à pessoa após violência sexual pode ser condicionada à apresentação
- Uma pessoa atendida 96 horas após violência sexual ainda necessita avaliação clínica, supo
- Durante histerectomia, o cirurgião precisa identificar a estrutura que passa inferiormente
- Uma paciente apresenta sangramento intenso desde a menarca, epistaxes frequentes e hematom
- Qual evento é responsável pelo rompimento do folículo dominante em um ciclo ovulatório típ
- Paciente em idade fértil procura atendimento com sangramento uterino intenso, tontura e do
- No exame do colo, qual região é particularmente importante para o surgimento de lesões pre
- Pessoa com dismenorreia progressiva, dispareunia profunda e dificuldade para engravidar te
- Uma paciente com mioma e anemia ferropriva apresenta lesão que distorce a cavidade uterina
- Paciente relata exteriorização de volume vaginal e dificuldade de esvaziamento vesical. Qu
- Qual é a medida de primeira linha mais adequada para perda urinária aos esforços em pacien
- Em mulher com incontinência mista e prolapso anterior, qual afirmação orienta uma avaliaçã
- Paciente sexualmente ativa tem dor hipogástrica, febre baixa e dor à mobilização cervical;
- Na investigação de corrimento vaginal com odor de peixe e pH elevado, a microscopia eviden
- Após confirmar tricomoníase em uma paciente, qual conduta de prevenção de reinfecção é nec
- Mulher com infertilidade tubária, amenorreia e antecedente epidemiológico relevante para t
- Sobre retirada de dispositivo intrauterino em paciente diagnosticada com DIP, assinale a c
- Em mulher assintomática de risco habitual com 60 anos, qual periodicidade de mamografia de
- Após resultado negativo no teste de DNA-HPV oncogênico em pessoa de risco habitual incluíd
- Uma pessoa de 43 anos apresenta nódulo mamário endurecido e persistente. Qual raciocínio d
- Após menopausa, paciente procura atendimento por pequeno episódio de sangramento vaginal. 
- Qual afirmação sobre tumores de ovário em pacientes assintomáticas de risco habitual está 
- Mulher de 52 anos apresenta amenorreia há 14 meses, sem gravidez ou outra causa identificá
- Ao considerar terapia hormonal sistêmica para fogachos em paciente com útero intacto e sem
- Qual situação constitui alerta de contraindicação para métodos contraceptivos combinados q
- Sobre a esterilização voluntária após as alterações da Lei nº 14.443/2022, qual alternativ
- Casal procura atendimento por ausência de gestação após um ano de relações regulares sem m
- Uma pessoa relata violência sexual ocorrida há 12 horas. Qual atitude inicial é compatível
- Uma exposição sexual com indicação de PEP ao HIV foi avaliada 36 horas após o evento. Qual
- Qual das alternativas caracteriza adequadamente a notificação sanitária após atendimento à
- Paciente retorna quatro dias após violência sexual e não recebeu atendimento prévio. Sobre
- Uma paciente apresenta dor pélvica súbita intensa, atraso menstrual e instabilidade hemodi
```

## Pedido 33: Intangível, redução ao valor recuperável e provisões (`contabilidade.intangivel-impairment-provisoes`, 307 tópicos de edital)

```
Matéria: contabilidade.intangivel-impairment-provisoes (Intangível, redução ao valor recuperável e provisões)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ativo intangível: definição, identificabilidade e reconhecimento": 17
- "2. Pesquisa, desenvolvimento, vida útil e baixa de intangíveis": 18
- "3. Redução ao valor recuperável: indícios, cálculo e registro": 17
- "4. Unidades geradoras de caixa, goodwill e reversão": 17
- "5. Provisões, contingências e aplicações do CPC 25": 31

Já existem (não repita):
- Uma entidade adquiriu direito de uso exclusivo de tecnologia por contrato, sem poder vendê
- Uma empresa comprou software separado do hardware por R$ 72.000, pagou R$ 5.000 por instal
- Uma organização gastou R$ 40.000 para investigar alternativas técnicas ainda incertas de u
- Uma licença custou R$ 100.000, tem vida útil definida de cinco anos e residual zero. Ficou
- Uma marca adquirida de terceiro foi avaliada com vida útil indefinida, com justificativa e
- Um ativo possui valor contábil de R$ 130.000, valor justo líquido de alienação de R$ 105.0
- Na avaliação de uma UGC com goodwill de R$ 30.000 e demais ativos de R$ 170.000, o valor r
- Uma empresa tem obrigação atual decorrente de ação judicial, perda provável e valor mensur
- Uma equipe documentou viabilidade técnica, intenção de concluir, capacidade de uso, recurs
- Uma sociedade gastou R$ 50.000 na pesquisa, R$ 30.000 no desenvolvimento antes de reunir t
- Uma licença de R$ 96.000, residual zero e vida útil de quatro anos ficou disponível para u
- Uma unidade produtiva tem valor contábil de R$ 260.000, valor justo estimado em R$ 240.000
- Em 31 de dezembro não se verificaram indícios de perda. Qual item ainda exige teste anual 
- Uma UGC possui goodwill de R$ 25.000, máquina de R$ 90.000 e licença de R$ 60.000. O recup
- Uma demanda judicial contra a entidade tem saída apenas possível e estimativa de R$ 120.00
- Uma empresa ajuizou ação de cobrança cuja entrada de R$ 70.000 é classificada como prováve
- Uma entidade registra R$ 220.000 de gastos de projeto próprio. Apenas R$ 75.000 foram inco
- Uma empresa tem software com custo de R$ 150.000, amortização acumulada de R$ 90.000 e per
- No encerramento, um ativo apresenta valor contábil de R$ 205.000, valor justo de R$ 190.00
- Uma UGC apresenta goodwill de R$ 20.000, ativo A de R$ 80.000 e ativo B de R$ 40.000. Valo
- Uma licença custou R$ 100.000, com vida útil original de cinco anos e residual zero. Após 
- Uma empresa vende 1.000 equipamentos, com garantia: 60% sem custo, 30% exigirão reparo de 
- Uma obrigação presente exigirá desembolso de R$ 121.000 em dois anos. O efeito financeiro 
- Uma obrigação provável e mensurável exige provisão de R$ 100.000. Seguradora reembolsará R
- Um contrato tem benefícios futuros esperados de R$ 95.000 e custo de cumprimento de R$ 140
- Uma licença controlada pode ser intangível identificável por decorrer de direito contratua
- Uma entidade pode capitalizar os gastos de pesquisa de projeto próprio quando os administr
- Uma patente com vida útil definida e consumo uniforme começa a ser amortizada quando está 
- Se um ativo tem justo líquido de alienação de R$ 80.000 e valor em uso de R$ 110.000, o va
- Em uma UGC com goodwill contabilizado, a perda por desvalorização deve ser alocada primeir
- A existência de processo judicial com obrigação atual e saída somente possível impõe recon
- Uma entrada judicial classificada como provável, mas ainda não praticamente certa, deve se
- Uma organização capitaliza como intangível um programa sem o qual a máquina específica não
- Se não for possível separar fase de pesquisa da fase de desenvolvimento em projeto interno
- Uma marca gerada internamente pode ser contabilizada como ativo intangível com base em ava
- Intangível de vida útil indefinida deve ser testado anualmente quanto à perda de recuperab
- Quando o valor recuperável de uma UGC aumenta em exercício posterior, é permitido reverter
- Uma provisão de processo trabalhista já não demanda saída provável de recursos segundo evi
- Uma empresa pode usar saldo provisionado para demandas judiciais para reduzir despesa de p
- Após perda de impairment em ativo intangível de vida definida, a quota futura de amortizaç
- Uma licença teve perda por impairment de R$ 20.000 no exercício anterior. No novo exercíci
- No impairment de UGC, mesmo após reduzir o goodwill, cada outro ativo tem piso individual:
- Em provisão com ressarcimento securitário praticamente certo de R$ 70.000 sobre obrigação 
- Para um contrato oneroso, a entidade avalia o custo inevitável entre cumprir e encerrar o 
- Uma administração aprovou reservadamente reestruturação futura com treinamento e publicida
```

## Pedido 34: Agravos prevalentes por especialidade: oftalmologia, otorrinolaringologia, neurologia, dermatologia, alergia e imunologia, neoplasias e genética (`medicina.agravos-prevalentes-especialidades`, 306 tópicos de edital)

```
Matéria: medicina.agravos-prevalentes-especialidades (Agravos prevalentes por especialidade: oftalmologia, otorrinolaringologia, neurologia, dermatologia, alergia e imunologia, neoplasias e genética)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Oftalmologia: refração, discromatopsias e infecções oculares": 17
- "2. Otorrinolaringologia: perda auditiva, otites e vertigens": 17
- "3. Neurologia: doenças mais prevalentes": 17
- "4. Dermatologia: doenças infecciosas e não infecciosas": 17
- "5. Alergias e doenças do sistema imunológico": 16
- "6. Neoplasias mais frequentes e doenças hereditárias": 16

Já existem (não repita):
- Na miopia, a imagem de objetos distantes forma-se antes da retina, e a correção óptica é f
- Trabalhador de 46 anos, sem queixas visuais prévias, passa a afastar os documentos para co
- O astigmatismo decorre, em geral, de curvatura irregular da córnea e é corrigido com lente
- Sobre as discromatopsias congênitas mais comuns, é correto afirmar que:
- Paciente com olho vermelho, secreção aquosa, sensação de areia e linfonodo pré-auricular p
- O tracoma é uma ceratoconjuntivite crônica causada por Chlamydia trachomatis, associada a 
- O calázio é uma infecção bacteriana aguda e muito dolorosa da glândula palpebral, enquanto
- Operário de obra sofre respingo de argamassa com cal no olho. A conduta imediata mais adeq
- Olho vermelho acompanhado de dor intensa, baixa súbita da visão e halos ao redor das luzes
- No teste de Weber, em paciente com perda auditiva condutiva à direita, o som:
- No teste de Rinne, o resultado negativo, em que a condução óssea é percebida melhor que a 
- A perda auditiva induzida por ruído ocupacional (PAIR) caracteriza-se por:
- A PAIR continua a progredir mesmo depois que o trabalhador deixa definitivamente de ser ex
- A presbiacusia é uma perda auditiva neurossensorial bilateral relacionada ao envelheciment
- Nadador apresenta otalgia intensa que piora com a tração do pavilhão auricular e edema do 
- A otite externa maligna, ou necrotizante, é uma infecção grave que acomete principalmente 
- Os agentes bacterianos mais comuns da otite média aguda são:
- Paciente refere crises de vertigem rotatória de segundos de duração ao deitar-se ou virar 
- A doença de Ménière caracteriza-se por crises de vertigem, zumbido, perda auditiva flutuan
- O acidente vascular cerebral hemorrágico é o tipo mais frequente, e o tabagismo é o princi
- Durante uma crise convulsiva tônico-clônica no ambiente de trabalho, a conduta correta inc
- Mulher de 30 anos tem crises de cefaleia unilateral, pulsátil, de forte intensidade, que p
- A cefaleia do tipo tensional é a forma mais comum de cefaleia primária e costuma ser bilat
- É sinal de alarme em paciente com cefaleia, indicando investigação de causa secundária:
- A doença de Parkinson caracteriza-se por tremor de repouso, rigidez e bradicinesia, decorr
- Digitadora refere formigamento noturno no polegar, indicador e dedo médio da mão direita, 
- A doença de Alzheimer é a causa mais comum de demência e costuma iniciar-se com comprometi
- A esclerose múltipla é uma doença desmielinizante do sistema nervoso central, mais comum e
- Sobre as dermatites de contato ocupacionais, é correto afirmar que:
- O exame indicado para identificar o agente responsável por uma dermatite de contato alérgi
- Mancha hipocrômica no braço com diminuição da sensibilidade térmica e dolorosa, em pacient
- A escabiose, causada pelo ácaro Sarcoptes scabiei, provoca prurido que piora à noite e les
- Placa eritematosa, quente e dolorosa na perna, com bordas elevadas e bem delimitadas, febr
- O melanoma é o tipo mais frequente de câncer de pele, enquanto o carcinoma basocelular é o
- A pitiríase versicolor, causada por fungos do gênero Malassezia, manifesta-se por manchas 
- Segundo a classificação de Gell e Coombs, a anafilaxia e a rinite alérgica são exemplos de
- Na anafilaxia, o tratamento de primeira escolha é a adrenalina por via intramuscular, admi
- Associe o mecanismo ao exemplo: hipersensibilidade por imunocomplexos (tipo III) correspon
- A asma ocupacional costuma melhorar nos períodos de afastamento do trabalho, como férias, 
- Infecções graves, recorrentes ou por germes incomuns devem levar à suspeita de imunodefici
- Segundo as estimativas do INCA, o tipo de câncer mais incidente no Brasil é o câncer de pe
- Associe corretamente o agente ao câncer relacionado:
- O Ministério da Saúde recomenda o rastreamento do câncer de mama por mamografia em mulhere
- A vacinação contra o HPV e contra a hepatite B são exemplos de prevenção primária do cânce
- A principal causa evitável de câncer, relacionada a tumores de pulmão, boca, laringe, esôf
- Um casal de portadores sadios do alelo da anemia falciforme, doença autossômica recessiva,
- Nas doenças com herança recessiva ligada ao X, como a hemofilia, um pai afetado transmite 
- Doença que aparece em várias gerações seguidas, afeta homens e mulheres e tem risco de 50%
- A síndrome de Down resulta da trissomia do cromossomo 21, e seu risco aumenta com a idade 
- Os cariótipos das síndromes de Turner e de Klinefelter são, respectivamente:
- Na herança mitocondrial, a doença é transmitida exclusivamente pela mãe, porque as mitocôn
- Trabalhador com deficiência de glicose-6-fosfato desidrogenase (G6PD) tem maior risco de:
- A triagem neonatal (teste do pezinho) permite o diagnóstico precoce de doenças como fenilc
```

## Pedido 35: Obstetrícia: pré-natal, parto e intercorrências da gestação (`medicina.obstetricia`, 305 tópicos de edital)

```
Matéria: medicina.obstetricia (Obstetrícia: pré-natal, parto e intercorrências da gestação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fisiologia da gestação, diagnóstico e pré-natal": 17
- "2. Hipertensão, diabetes, doenças clínicas e trombofilias": 17
- "3. Hemorragias obstétricas e doença trofoblástica gestacional": 17
- "4. Infecções materno-fetais e prevenção da prematuridade": 17
- "5. Trabalho de parto, partograma, analgesia e vitalidade fetal": 16
- "6. Puerpério, emergências pós-parto e aleitamento materno": 16

Já existem (não repita):
- Qual distinção sobre doença trofoblástica gestacional é correta para fins de concurso?
- Após 28 semanas, uma mulher sem hipertensão prévia apresenta PA elevada e plaquetopenia, m
- Na cardiotocografia intraparto, surgem desacelerações repetidas durante taquissistolia por
- A recomendação brasileira de aleitamento exclusivo até seis meses pressupõe não ofertar ág
- A presença de desacelerações da frequência cardíaca fetal na cardiotocografia sempre confi
- Sobre hepatite B na gravidez, qual ação neonatal se destaca após nascimento de bebê expost
- Gestante no terceiro trimestre apresenta sangramento vermelho-vivo e pouco doloroso. Há su
- Uma gestante recebe diagnóstico de sífilis durante o pré-natal. Qual tratamento tem papel 
- Gestante hipertensa apresenta dor abdominal intensa, útero hipertônico e frequência cardía
- Após mola hidatiforme, o seguimento do hCG permite reconhecer persistência da doença trofo
- A corticoterapia antenatal tem o mesmo objetivo da tocólise e, por isso, ambos os tratamen
- Após parto vaginal, a mulher apresenta sangramento abundante e útero flácido à palpação. Q
- Na rotina nacional de rastreamento do diabetes gestacional, em mulher sem diabetes prévio 
- Uma paciente com atraso menstrual e teste urinário positivo procura consulta inicial. Qual
- No parto vaginal cefálico, o que significa rotação interna da apresentação?
- A sífilis na gestação pode produzir perda gestacional e infecção congênita, motivo pelo qu
- Paciente estável tem teste positivo, ultrassom transvaginal inicial inconclusivo e dor pél
- Uma parturiente com 3 cm de dilatação solicita analgesia neuraxial; a avaliação clínica e 
- O partograma auxilia a acompanhar progressão do trabalho de parto, mas uma taxa uniforme d
- Sangramento vaginal no fim da gravidez com suspeita de placenta prévia contraindica toque 
- Em gestação única com colo uterino curto identificado em situação de risco definida, qual 
- O diagnóstico de pré-eclâmpsia é impossível na ausência de proteinúria, mesmo que a mulher
- A elevação do volume plasmático proporcionalmente maior que o aumento de eritrócitos pode 
- Gestante apresenta contrações regulares antes de 37 semanas e mudança cervical documentada
- Sobre aleitamento materno, qual é a recomendação geral do Ministério da Saúde?
- Em paciente com hipertensão gestacional grave, surgem dor em hipocôndrio direito, elevação
- O encaminhamento ao pré-natal de alto risco extingue a responsabilidade da Atenção Primári
- A definição da data provável do parto deve ser refeita a cada ultrassonografia, pois a bio
- No manejo da eclâmpsia, o sulfato de magnésio tem função anticonvulsivante e não deve ser 
- No terceiro trimestre, uma gestante apresenta creatinina sérica semelhante à de uma adulta
- Gestante com DUM incerta apresenta ultrassonografia de primeiro trimestre confiável e outr
- Após esvaziamento uterino de mola hidatiforme, qual conduta permite detectar neoplasia tro
- No diabetes gestacional, basta controlar a glicemia durante a gravidez; o estado metabólic
- A gestante com perda de líquido e suspeita de ruptura pré-termo de membranas deve ser aval
- Em gestante de alto risco com suspeita de insuficiência placentária, qual achado pertence 
- Gestante internada por pré-eclâmpsia apresenta sinais de iminência de eclâmpsia. Qual fárm
- Um ultrassom sem hematoma retroplacentário afasta definitivamente descolamento prematuro d
- Uma gestante RhD negativa, não sensibilizada, entrega o resultado de parto de filho RhD po
- Qual ação resume melhor o acompanhamento pré-natal de risco habitual na rede pública?
- Puérpera com sangramento persistente tem útero firme e bem contraído. Qual investigação de
- Gestante com perda gestacional isolada apresenta anticorpo antifosfolípide reagente em uma
- Qual é a melhor interpretação do partograma durante parto de risco habitual?
```

## Pedido 36: Engenharia de requisitos, maturidade de processos e pontos de função (`informatica.engenharia-requisitos-maturidade-apf`, 303 tópicos de edital)

```
Matéria: informatica.engenharia-requisitos-maturidade-apf (Engenharia de requisitos, maturidade de processos e pontos de função)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Engenharia de requisitos: elicitação, classificação e qualidade": 21
- "2. Gestão, rastreabilidade, validação e estimativas de software": 20
- "3. CMMI v3.0: capacidade, maturidade e melhoria de desempenho": 18
- "4. MPS.BR: MR-MPS-SW:2024, níveis e avaliação": 17
- "5. Análise de Pontos de Função: tipos, complexidade, contagem e aplicação": 24

Já existem (não repita):
- Uma equipe recebeu a solicitação “o sistema deve ser rápido” e precisa transformá-la em re
- Um aplicativo deve permitir que a pessoa consulte pedidos anteriores e visualize o valor t
- Em um projeto, surgiram necessidades contraditórias de duas áreas sobre o prazo de retençã
- Após implementação tecnicamente conforme uma especificação, pessoas usuárias percebem que 
- Uma matriz liga cada requisito a sua origem, componentes alterados e casos de teste corres
- Um projeto tem tamanho estimado de 150 PF e produtividade histórica de 8 pessoas-hora por 
- Uma avaliação caracteriza projetos planejados, executados, medidos e controlados, mas sem 
- Uma organização segue o MR-MPS-SW e pretende avançar desde o nível inicial da escala até o
- Na análise de pontos de função, uma aplicação mantém diretamente o cadastro lógico de clie
- Uma aplicação tem um ALI simples e um AIE médio, previamente classificados conforme o IFPU
- Uma demanda de negócio foi aprovada como baseline de requisitos. Depois, surgiu mudança qu
- Uma estimativa PERT adota duração otimista de 4 dias, mais provável de 7 dias e pessimista
- Uma empresa informou possuir nível 3 de capacidade CMMI em uma área de prática. Qual infer
- Um conjunto de projetos opera com padrões compartilhados, mas não apresenta desempenho con
- Uma organização apresenta avaliação no MR-MPS-SW:2023 e sustenta que ela permanece válida 
- Em uma aplicação, o cadastro de fornecedores é atualizado por outro sistema; a solução em 
- Uma transação recebe dados e mantém um cadastro lógico interno. Outra apenas recupera dado
- Um sistema calcula índice de crescimento a partir de dados internos e produz relatório com
- Um inventário funcional, já corretamente classificado, contém dois ALI simples, um AIE méd
- Uma estimativa tem 100 PF não ajustados e um enunciado histórico fornece explicitamente fa
- Uma equipe deseja estimar tamanho nas fases iniciais usando Simple Function Points (SFP), 
- Uma aplicação tem 80 PF, outra tem 80 PF, mas a segunda exige disponibilidade crítica e al
- Uma organização exibe padrões corporativos bem documentados, mas conclui nível 4 de maturi
- Uma avaliação MPS-SW:2024 encontra processos com gestão quantitativa consolidada, mas aind
- Um sistema tem três tabelas físicas para o mesmo grupo lógico de dados e expõe duas telas 
- Um requisito que determina o cadastro de novas solicitações pelo sistema é classificado co
- A validação verifica principalmente se o código atende literalmente à especificação existe
- Pontos de função representam tamanho funcional lógico, ao passo que pessoas-hora expressam
- O CMMI v3.0 considera nível 3 de capacidade em uma área de prática prova automática de mat
- O MR-MPS-SW organiza níveis crescentes de maturidade pelas letras G, F, E, D, C, B e A, se
- Na análise de pontos de função IFPUG, toda tabela física corresponde necessariamente a um 
- Um requisito de desempenho expresso como “responder em até dois segundos para 95% das cons
- A formação de uma baseline de requisitos impede modificações posteriores, mesmo quando tod
- Uma estimativa PERT com valores otimista 3, mais provável 6 e pessimista 15 dias resulta e
- No CMMI v3.0, maturidade nível 4 significa apenas usar um modelo único de documento em tod
- A versão MR-MPS-SW:2024 foi desenvolvida considerando compatibilidade com o CMMI v3.0, mas
- Na APF tradicional, uma Saída Externa simples vale 3 PF e uma Consulta Externa simples val
- Uma Saída Externa complexa conta 7 PF não ajustados e uma Entrada Externa média conta 4 PF
- Um produto que passa nos testes derivados de sua especificação necessariamente atende às n
- O CMMI v3.0 incorpora orientações de gestão de dados, segurança e pessoas além dos domínio
- Conforme o Comunicado 01/2024, avaliações MR-MPS-SW:2023 podem permanecer válidas após 1º 
- No IFPUG, a complexidade de um ALI depende de DET e RET, ao passo que transações são class
- A técnica Simple Function Points usa exatamente ALI, AIE, EE, SE e CE com os mesmos pesos 
- Se dois projetos têm mesmo número de pontos de função, diferentes requisitos de segurança 
- No modelo federal consolidado de contratação de software de 2026, fatores arbitrários de p
```

## Pedido 37: Ensino de Arte (artes visuais, música, teatro, dança) (`educacao.ensino-arte-musica`, 303 tópicos de edital)

```
Matéria: educacao.ensino-arte-musica (Ensino de Arte (artes visuais, música, teatro, dança))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Arte no currículo: LDB, BNCC e diretrizes de 2026": 17
- "2. História do ensino de Arte e concepções pedagógicas": 17
- "3. Artes visuais e história da arte geral e brasileira": 17
- "4. Música na escola: escuta, criação e cultura sonora": 17
- "5. Teatro e dança: corpo, cena, movimento e educação": 16
- "6. Didática, interculturalidade, artes integradas e avaliação": 16

Já existem (não repita):
- Pela LDB, o ensino de Arte, com atenção às expressões regionais, é componente curricular o
- O art. 26, § 6º, da LDB enumera cinco linguagens artísticas, incluindo Artes Integradas.
- Na BNCC do Ensino Fundamental, Artes Integradas constitui unidade temática voltada às arti
- As dimensões criação, crítica, estesia, expressão, fruição e reflexão devem ser desenvolvi
- O código EF15AR identifica habilidades de Arte relativas ao bloco do 1º ao 5º ano do Ensin
- Em música, um som mais agudo é necessariamente mais intenso do que um som grave.
- A Semana de Arte Moderna de 1922 é marco importante do modernismo brasileiro, mas não expl
- A Abordagem Triangular determina obrigatoriamente que a contextualização venha antes da le
- A resolução nacional publicada em agosto de 2026 exigiu a oferta imediata e simultânea de 
- Na resolução de 2026, destinar exatamente 25% da jornada docente a projetos integradores é
- Em uma proposta de livre expressão, a valorização da espontaneidade impede o professor de 
- A tendência pedagógica tradicional no ensino de Arte historicamente valorizou modelos, exe
- A experiência brasileira de Educação Artística associada à legislação de 1971 foi frequent
- Uma aprendizagem teatral válida exige uma peça escrita, memorização integral do texto e ap
- Ritmo e pulso são sinônimos: ambos indicam exclusivamente a velocidade da execução musical
- Ensinar dança de maneira inclusiva envolve reconhecer modos diversos de movimento e evitar
- Interculturalidade no ensino de Arte consiste apenas em expor objetos de culturas diferent
- As diretrizes de 2026 preveem revisão das normas de seleção e contratação para que, gradua
- Arte contemporânea é, por definição, produção feita exclusivamente com equipamentos digita
- Na BNCC, fruição equivale a apreciar algo como bonito e concordar com o juízo estético do 
- Uma coordenadora quer resumir a estrutura de Arte do Ensino Fundamental na BNCC para a equ
- Qual conjunto representa as seis dimensões do conhecimento em Arte na BNCC?
- No contexto da Abordagem Triangular, qual prática melhor representa a ação de contextualiz
- Uma professora trabalha cores e formas por meio de brincadeiras, exploração de tintas e ge
- Uma turma compara a mesma nota tocada em violão e flauta e identifica diferença perceptiva
- Assinale a situação que caracteriza aprendizagem artística, e não apenas uso instrumental 
- Em um roteiro didático, alunos observam instalações contemporâneas, pesquisam seu contexto
- Qual período artístico é especialmente associado a pesquisas de luz e cor em cenas da vida
- Uma escola quer rever sua organização de Arte à luz da Resolução CNE/CEB nº 5/2026; qual d
- Qual enunciado diferencia de forma adequada a pedagogia tradicional e a livre expressão no
- Numa atividade de escuta, a turma usa passos regulares e sobrepõe padrões de palmas longas
- Qual afirmação sobre o barroco colonial brasileiro está mais adequada?
- Para evitar uma abordagem superficial da diversidade cultural em Arte, um projeto deveria
- Qual das situações exemplifica melhor a unidade temática Artes Integradas com objetivos es
- Considerando a expressão corporal na escola, assinale a opção correta.
- A inclusão de artistas e mestres de culturas populares em um projeto escolar, conforme a r
- Ao avaliar trabalhos visuais feitos por estudantes com diferentes repertórios e condições 
- Um item afirma que a música deixou de integrar a obrigatoriedade legal após a Lei nº 13.27
- Um docente propõe investigar fotografias de artistas indígenas contemporâneos, reconhecend
- Em uma prova, uma alternativa declara que a Semana de 1922 lançou o modernismo e incluiu a
- Diante do enunciado “A Estesia é uma avaliação externa da beleza da obra segundo critério 
- Ao planejar uma sequência de teatro sobre narrativas do bairro, o professor quer evitar es
- Uma escola propõe que todas as práticas artísticas sejam avaliadas somente pela precisão t
- Assinale a alternativa que traduz adequadamente as diferenças entre os termos legais e os 
- Uma professora expõe grafites, gravuras e pinturas, convida estudantes a analisar autoria,
- Um estudante surdo participa da composição de uma paisagem sonora escolar; qual estratégia
```

## Pedido 38: Metodologias ativas e técnicas de gestão da sala de aula (`educacao.metodologias-ativas-gestao-sala-aula`, 303 tópicos de edital)

```
Matéria: educacao.metodologias-ativas-gestao-sala-aula (Metodologias ativas e técnicas de gestão da sala de aula)
Capítulos e quantas questões quero em cada um (total 100):
- "Metodologias ativas: princípios e desenho pedagógico": 17
- "Sala de aula invertida, rotação por estações e ensino híbrido": 17
- "Aprendizagem baseada em problemas, projetos e STEAM": 17
- "Planejamento para aprender e verificação de compreensão": 17
- "Gestão da sala, questionamento e participação equitativa": 16
- "Avaliação formativa, rubricas, devolutivas e arremate": 16

Já existem (não repita):
- Uma aula pode promover aprendizagem ativa mesmo quando incorpora uma breve explicação dire
- A simples formação de grupos garante protagonismo e aprendizagem colaborativa, ainda que s
- No planejamento reverso de uma sequência ativa, a escolha da tecnologia deve anteceder a d
- A adaptação de suportes e linguagens a estudantes com necessidades distintas pode coexisti
- Em uma aula de Geografia, a professora quer que cada estudante defenda uma intervenção par
- Qual é a função mais precisa de planejar em dobro na preparação de uma aula?
- Uma equipe precisa organizar debates sobre uso de energia. O que melhor caracteriza cooper
- Ao selecionar uma atividade digital para turma com acesso doméstico desigual, a conduta pe
- Na sala de aula invertida, o estudo preparatório pode ocorrer por texto impresso ou áudio,
- Uma escola que usa computador para projetar slides em aulas integralmente expositivas já c
- Na rotação por estações classificada como modelo de ensino híbrido, pode haver estações de
- Na rotação individual, todos os estudantes precisam cumprir obrigatoriamente o mesmo conju
- Uma professora enviou um vídeo para casa, mas a aula seguinte apenas repete a gravação sem
- No desenho clássico de rotação por estações do ensino híbrido, qual arranjo atende à carac
- Ao implantar sala invertida em comunidade com conectividade limitada, qual medida responde
- Em qual situação a expressão laboratório rotacional é utilizada com mais precisão?
- Na aprendizagem baseada em problemas, o produto final precisa ser sempre uma maquete físic
- Uma proposta pode desenvolver projeto interdisciplinar sem ser necessariamente STEAM, pois
- Em STEAM, acrescentar uma atividade decorativa de pintura ao final de um experimento basta
- Os alunos investigam o aumento de temperatura em bairros e entregam uma explicação baseada
- Qual prática evita que um projeto vire apenas elaboração de cartaz e apresentação?
- Em projeto STEAM sobre iluminação pública, qual ação corresponde melhor ao papel formativo
- Ao comparar ensino baseado em problemas e em projetos, assinale a afirmação tecnicamente m
- Um grupo cria um protótipo para reduzir calor em um ponto de ônibus. Qual condição melhor 
- Planejar para o erro significa antecipar equívocos plausíveis e preparar perguntas ou repr
- Perguntar se a classe entendeu e receber respostas afirmativas de alguns voluntários forne
- Em uma cultura do erro consistente, acolher a tentativa do estudante é compatível com pedi
- Uma turma afirma que 1/8 é maior que 1/6 porque oito é maior que seis. Qual intervenção me
- Após explicar fontes históricas, o professor pergunta «entenderam?», e a turma silencia. Q
- No planejamento de uma sequência, qual descrição é mais compatível com cultura do erro?
- Ao preparar uma aula, qual registro é exemplo de planejamento em dobro associado a uma ver
- O tempo de espera após uma pergunta pode ampliar a qualidade das respostas e permitir que 
- Na técnica certo é certo, o professor deve considerar plenamente correta qualquer resposta
- A expressão sem escapatória pode ser operacionalizada por pista, modelagem de um colega e 
- Ao formular uma pergunta complexa, um professor pretende ampliar a participação antes de e
- Um aluno explica corretamente uma regra. O professor pede que a aplique a caso novo e iden
- Qual enunciado representa elogio preciso associado a evidência observável?
- Qual prática concretiza expectativas visíveis durante trabalho em grupos?
- Uma rubrica analítica costuma explicitar critérios de desempenho separados e níveis descri
- Uma atividade só tem função formativa quando é corrigida por nota numérica e registrada no
- Um bilhete de saída que pede aplicação de um conceito em caso novo pode funcionar como arr
- Durante projeto interdisciplinar, a professora elabora uma tabela com critérios de argumen
- Um docente coleta bilhetes de saída e identifica erro recorrente. Qual ação transforma ess
- Ao devolver um texto argumentativo, qual orientação é exemplo mais claro de feedback acion
- Ao avaliar protótipos STEAM, qual decisão preserva o vínculo entre rubrica, processo inves
```

## Pedido 39: Radiologia e diagnóstico por imagem (médico) (`medicina.radiologia-diagnostico-imagem`, 301 tópicos de edital)

```
Matéria: medicina.radiologia-diagnostico-imagem (Radiologia e diagnóstico por imagem (médico))
Capítulos e quantas questões quero em cada um (total 100):
- "Raciocínio por modalidade e ultrassonografia diagnóstica": 17
- "Imagem do tórax e das doenças cardiopulmonares": 17
- "Abdome, pelve e ultrassonografia por órgãos": 17
- "Neurorradiologia e imagem musculoesquelética": 17
- "Mama, radiologia pediátrica e ultrassonografia obstétrica": 16
- "Contrastes, radiologia intervencionista e elaboração de laudos": 16

Já existem (não repita):
- Na avaliação de nódulo superficial de tireoide, qual transdutor costuma oferecer melhor re
- Em RM de encéfalo, qual associação sustenta restrição verdadeira da difusão em um foco de 
- Radiografia mostra opacidade no lobo inferior direito com desvio de fissura e elevação do 
- Qual descrição define vidro fosco na tomografia computadorizada de tórax?
- Em ultrassom, foco hiperecogênico intravesicular móvel que produz sombra posterior sugere 
- Na suspeita de obstrução biliar com ultrassom inconclusivo, qual método demonstra a anatom
- Após trauma craniano, uma coleção extra-axial biconvexa, limitada por suturas cranianas, r
- Atleta com dor progressiva na perna e radiografia inicial negativa, mas suspeita persisten
- Uma mamografia recebeu categoria BI-RADS 0. Qual interpretação é correta?
- Pessoa de 27 anos relata novo nódulo palpável de mama, sem outros dados de alto risco. Qua
- Após infusão de contraste iodado, paciente desenvolve broncoespasmo grave, hipotensão e co
- Em uma coleção abdominal profunda cuja drenagem percutânea foi cogitada, qual critério é e
- Durante US de tendão superficial, observa-se faixa hipoecoica que desaparece quando o oper
- Em adulto com suspeita clínica de embolia pulmonar, baixa probabilidade pré-teste e D-díme
- Paciente com pancreatite aguda leve clinicamente definida, sem sinais de complicação nas p
- Paciente com dor abdominal intensa desproporcional ao exame e forte suspeita de isquemia m
- Lesão cerebral com realce periférico em anel e restrição central importante à difusão, aco
- Pessoa com neoplasia conhecida desenvolve paraparesia progressiva e disfunção esfincterian
- Questão solicita a recomendação de rastreamento mamográfico populacional do Ministério da 
- Criança com suspeita de apendicite e probabilidade clínica intermediária está estável. Qua
- Após TC com contraste, creatinina sobe em paciente internado com múltiplas possíveis causa
- Em paciente com doença renal avançada que precisa de RM contrastada, qual princípio é mais
- Ao interpretar lesão focal hepática, em qual cenário o sistema LI-RADS tem aplicação diagn
- Paciente com dor pélvica súbita e ovário aumentado apresenta vascularização identificável 
- Em ultrassom torácico de paciente em ventilação mecânica, não se observa deslizamento pleu
- A tomografia computadorizada usa radiação ionizante, enquanto a ressonância magnética e a 
- O reforço acústico posterior é o mesmo artefato que a sombra acústica, aparecendo de forma
- O desaparecimento do contorno cardíaco direito por opacidade pulmonar adjacente pode ajuda
- Uma ultrassonografia que não visualizou adequadamente o pâncreas por interposição gasosa p
- A categoria BI-RADS 6 corresponde a malignidade conhecida, já demonstrada por biópsia, e n
- A radiologia intervencionista inclui procedimentos de biópsia e drenagem guiados por image
- Uma radiografia de tórax sem alterações é suficiente para excluir embolia pulmonar aguda e
- O achado de linhas B bilaterais ao ultrassom pulmonar indica síndrome intersticial, mas nã
- Na ultrassonografia da vesícula biliar, espessamento isolado da parede é específico para c
- A tomografia sem contraste pode ser útil para cálculo ureteral, e a ausência de hidronefro
- Hematoma subdural clássico é uma coleção extra-axial em crescente que pode atravessar sutu
- A recomendação brasileira de rastreamento populacional de mama para assintomáticas impede 
- Em reação anafilática grave ao contraste, a administração de anti-histamínico isolado deve
- Na ultrassonografia, a ausência de sinal ao Doppler colorido de uma veia acessível à compr
- Em pacientes sem fatores que os enquadrem na população-alvo do sistema, aplicar LI-RADS a 
- A presença de DWI hiperintensa, sem análise do ADC, basta para diagnosticar com segurança 
- O hipersinal intrassubstancial de menisco na RM caracteriza obrigatoriamente ruptura menis
- Na investigação de torção ovariana, a manutenção de fluxo ao Doppler não exclui torção par
- Lesão renal aguda observada após TC com contraste é, pela mera sequência temporal, prova d
- A emissão eletrônica de um laudo com achado crítico, como grande hemorragia intracraniana 
```

## Pedido 40: Psicoterapia e psicopatologia clínica (`psicologia.psicoterapia-psicopatologia-clinica`, 299 tópicos de edital)

```
Matéria: psicologia.psicoterapia-psicopatologia-clinica (Psicoterapia e psicopatologia clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da psicoterapia e principais abordagens teóricas": 19
- "2. Avaliação psicopatológica: ansiedade, depressão e diagnósticos diferenciais": 24
- "3. Transtornos de personalidade e estruturas clínicas na tradição psicanalítica": 23
- "4. Psicoterapia individual, grupal e intervenções em problemas específicos": 20
- "5. Dependência química, tabagismo, redução de danos e prevenção": 14

Já existem (não repita):
- Uma pessoa em psicoterapia relata medo de falar em reuniões e a crença de que qualquer hes
- Uma paciente com depressão deixou de realizar atividades que antes lhe traziam satisfação.
- Durante acompanhamento psicodinâmico, uma pessoa passa a esperar que o terapeuta a abandon
- Uma terapeuta utiliza escuta empática, autenticidade e atitude de aceitação da pessoa, dis
- Uma pessoa apresenta humor deprimido ou perda de interesse durante quase todo o dia, por t
- Após crise súbita de taquicardia, uma pessoa pergunta se já tem transtorno do pânico. O ep
- Uma pessoa sofre preocupações persistentes e excessivas sobre trabalho, família, contas e 
- Em atendimento grupal, os participantes relatam alívio ao reconhecer dificuldades semelhan
- Uma pessoa deseja controlar o uso de álcool, mas está ambivalente: relata alívio imediato 
- Um paciente evita encontros porque teme ser ridicularizado; outro evita atravessar pontes 
- Uma escala de rastreio depressivo indica pontuação baixa, mas durante a entrevista a pesso
- Um estudante deseja distinguir perfeccionismo rígido e controle interpessoal persistentes 
- Um relatório clínico usa a CID-11 para avaliar padrões persistentes de disfunção da person
- Uma equipe adota referencial psicanalítico estrutural e discute neurose, psicose e pervers
- Uma profissional organiza psicoterapia em grupo para pessoas com ansiedade e informa que o
- Um paciente relata aumento de consumo de álcool para alcançar o mesmo efeito e forte desej
- Uma pessoa com uso prejudicial de álcool procura atendimento, mas ainda não deseja abstinê
- Uma pessoa chega com depressão recorrente e refere períodos anteriores de vários dias com 
- Uma paciente com diagnóstico prévio de personalidade borderline apresenta autolesões recor
- Em laudo baseado em diagnóstico descritivo, uma pessoa apresenta alucinações após consumo 
- Uma terapeuta aplica TCC para ansiedade social e organiza uma apresentação curta sem os co
- Uma pessoa apresenta sintomas depressivos, sofrimento interpessoal persistente e dificulda
- Uma pessoa em tratamento para dependência de álcool volta a consumir após conflito familia
- Uma pessoa com uso nocivo de álcool recebe atendimento breve de vinte minutos com devoluti
- Uma pessoa com dependência de substância e dificuldade de obter abstinência procura serviç
- Na terapia cognitivo-comportamental, identificar previsões e testar interpretações mediant
- A consideração positiva incondicional, na abordagem centrada na pessoa, obriga o terapeuta
- Na avaliação de depressão, humor deprimido ou perda de interesse persistente por pelo meno
- Um único ataque de pânico desencadeado por condição clínica conhecida é suficiente para di
- Na psicoterapia em grupo, a troca de experiências e a coesão podem contribuir para os resu
- Redução de danos corresponde à exigência de abstinência completa antes do primeiro atendim
- A ativação comportamental para depressão pode utilizar programação gradual de tarefas liga
- Pelo modelo dimensional da CID-11, qualquer pessoa que apresente um traço de perfeccionism
- Na psicanálise estrutural, recalque, foraclusão e desmentido são conceitos teóricos associ
- Um grupo terapêutico garante a confidencialidade integral de todas as falas porque o profi
- Para consumo nocivo de álcool sem dependência estabelecida, uma intervenção breve com devo
- Entrevista motivacional é técnica de confrontação direta que procura vencer a resistência 
- No planejamento de cuidado, a presença de mania ou hipomania prévia deve ser investigada e
- Na ACT, aceitar pensamentos e emoções desagradáveis significa resignar-se aos riscos exter
- O trabalho com transtorno de personalidade e desregulação emocional pode requerer plano es
- A ocorrência de alucinações após consumo de substância basta para diagnosticar esquizofren
- Em intervenção para ansiedade social, reduzir comportamentos de segurança durante uma expe
- A presença simultânea de tolerância e fissura em pessoa que usa álcool autoriza, isoladame
- Uma pessoa que retorna ao consumo após conflito familiar pode beneficiar-se da análise de 
- Na atenção psicossocial territorial, a redução de danos impede estabelecer metas de abstin
```

## Pedido 41: Precedentes, IRDR, IAC e jurisprudência vinculante (`processo-civil.precedentes-irdr-iac`, 299 tópicos de edital)

```
Matéria: processo-civil.precedentes-irdr-iac (Precedentes, IRDR, IAC e jurisprudência vinculante)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Precedentes, fundamentação, distinção e superação": 22
- "2. IRDR: cabimento, legitimidade, admissão e suspensão": 20
- "3. IRDR: participação, tese, aplicação, revisão e recursos": 19
- "4. IAC e a atuação uniformizadora dos tribunais superiores": 20
- "5. Súmulas vinculantes, reclamação e aplicação dos precedentes": 19

Já existem (não repita):
- Ao decidir duas ações com questão jurídica idêntica, o tribunal pretende cumprir o dever d
- Um acórdão resolve disputa contratual por fundamento jurídico indispensável e acrescenta c
- Uma parte invoca tese firmada em precedente, mas o novo processo possui característica fát
- Centenas de processos contêm a mesma controvérsia unicamente jurídica e decisões divergent
- Uma pessoa pretende suscitar IRDR porque há divergência acerca de mesma questão de direito
- Um IRDR já foi admitido e processos com a questão jurídica discutida tramitam na área do t
- Durante suspensão por IRDR, uma pessoa demonstra necessidade urgente de tutela no processo
- Após o julgamento de IRDR, novos processos e ações pendentes sobre idêntica questão jurídi
- Uma remessa necessária envolve questão jurídica relevante de grande repercussão social sem
- Após reiteradas decisões constitucionais, pretende-se aprovar súmula vinculante. Qual quór
- Diversos processos discutem cláusula idêntica, porém um recurso sobre essa questão já foi 
- Um IRDR permanece pendente por mais de um ano, e o relator não profere nova decisão fundam
- Em IRDR, o requerente desiste do processo originário e o Ministério Público não foi autor 
- Durante instrução de IRDR, interessados pedem juntada de documentos e o Ministério Público
- Câmaras de um mesmo tribunal apresentam interpretações divergentes sobre questão de direit
- Um IAC foi julgado pelo colegiado regimental, com fixação de entendimento sobre questão re
- Depois de sessão que aprovou enunciado de súmula vinculante, é necessário publicá-lo nos v
- Foi apresentada proposta de revisão de súmula vinculante e vários processos discutem a mes
- Uma apostila reproduz o art. 927 do CPC sem a atualização de agosto de 2026 e pede que o l
- Uma sentença deixa de seguir precedente especificamente invocado pela parte, alegando disc
- Depois de julgamento de IRDR, entende-se necessário rever a tese sem que tenha sido interp
- Do mérito de IRDR surge recurso especial cabível, cuja questão é decidida pelo tribunal su
- Ao admitir IAC, um relator afirma que a mera instauração impõe suspensão geral de todos os
- Uma decisão administrativa contraria súmula vinculante, mas a pessoa diretamente interessa
- Em outubro de 2026, uma parte deseja reclamar por descumprimento de acórdão proferido em r
- Em processo que discute a mesma questão jurídica de precedente, o julgador deve identifica
- O art. 928 do CPC considera o incidente de assunção de competência espécie de julgamento d
- Havendo vários processos sobre mesma questão unicamente de direito e risco à isonomia e à 
- Durante o IRDR, a desistência do processo pelo requerente acarreta extinção necessária do 
- O CPC permite ao juiz ou relator suscitar IRDR por ofício ao presidente do tribunal e às p
- Ao admitir IRDR, o relator deve determinar automaticamente a suspensão de todos os process
- Um IRDR não é julgado no prazo de um ano. Na ausência de decisão fundamentada do relator e
- No IRDR, os demais interessados dispõem, cada um, de trinta minutos de sustentação oral se
- Após julgamento do IRDR, a tese aplica-se também aos casos futuros sobre idêntica questão 
- A relevância social de uma questão jurídica afasta a exigência de repetição no IRDR, pois 
- Uma divergência relevante de direito está surgindo entre turmas do mesmo tribunal. O art. 
- A admissão de IAC produz, pelo próprio art. 947, suspensão geral obrigatória dos processos
- Contra o mérito do IRDR cabe, conforme o tema, recurso especial ou extraordinário com efei
- Pelo art. 986 do CPC, qualquer parte de processo repetido pode exigir autonomamente a revi
- Uma súmula vinculante exige aprovação por dois terços dos membros após decisões reiteradas
- O município que não figura como parte em processo pode propor diretamente edição de súmula
- Se uma pessoa pretende reclamar contra ato administrativo por violação de súmula vinculant
- Uma reclamação contra decisão já transitada em julgado deve ser admitida porque sua finali
- A Lei nº 15.484/2026 acrescentou ao art. 927, III-A, os acórdãos de REsp submetidos à rele
- Como o art. 927 do CPC manda observar os recursos repetitivos, seu descumprimento permite,
```

## Pedido 42: Análise e gerenciamento de riscos em saúde e ambiente (`saude-publica.analise-gerenciamento-risco-sanitario`, 299 tópicos de edital)

```
Matéria: saude-publica.analise-gerenciamento-risco-sanitario (Análise e gerenciamento de riscos em saúde e ambiente)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Risco, perigo, incerteza, erro e precaução": 20
- "2. Danos, causalidade e avaliação científica do risco": 20
- "3. Avaliação da exposição, planejamento e priorização": 19
- "4. Gerenciamento, ações corretivas e escolhas coletivas": 21
- "5. Comunicação, monitoramento e revisão de riscos": 20

Já existem (não repita):
- Em uma instalação fechada há substância reconhecidamente tóxica, mas ainda não se verifico
- Uma equipe percebe que um sensor descalibrado indica sistematicamente valores superiores a
- Após indícios plausíveis de contaminação potencialmente grave de poços, ainda faltam análi
- Em estudo sobre solvente detectado em um solo, os pesquisadores inicialmente examinam se e
- Após aumento de queixas respiratórias em um bairro industrial, a equipe quer verificar se 
- Para investigar exposição a contaminante presente na água consumida, uma equipe precisa de
- Após instalar tratamento em um sistema de abastecimento, a gestão reavalia a ameaça que pe
- Em relatório, constam risco estimado e alternativas técnicas para reduzir uma exposição. A
- Uma comunidade recebe um alerta sanitário, mas relata não ter acesso à medida de proteção 
- Duas regiões registraram 40 agravos relacionados à água; uma possui 2.000 residentes e out
- Uma avaliação usa cenário conservador de ingestão para crianças expostas a um poluente. Co
- Em amostras de solo, o laboratório registra substância como ‘abaixo do limite de detecção’
- Uma intervenção remove o descarte contaminante, mas a população continua consumindo peixe 
- Um plano de resposta prevê entrega de água segura e monitoramento de doença de longa latên
- A gestão compara duas medidas para reduzir doença ambiental: uma tem menor custo imediato,
- Um serviço interrompe prática insegura, revisa seus protocolos e passa a auditar rotineira
- Ao divulgar suspeita de risco alimentar, a autoridade ainda não confirmou a fonte. Qual me
- Após suspeita de efeito grave de novo contaminante, a análise de amostras é limitada e as 
- Após mutirão de busca ativa, notificações de agravo ambiental duplicaram, mas a população 
- Em um território, a concentração de poluente em água foi reduzida; paralelamente, moradore
- Um relatório indica forte evidência de toxicidade de certo composto, porém dados frágeis s
- A retirada de uma população de área contaminada reduz imediatamente a exposição, mas inter
- Uma autoridade selecionou risco ambiental com baixa chance de ocorrência estimada, porém d
- Na avaliação de uma intervenção, foram distribuídos filtros a todas as famílias, mas não s
- Ao revisar medida sanitária provisória, surgem dados confiáveis que reduzem a estimativa d
- Em depósito de produto tóxico isolado, a equipe reconheceu capacidade de causar dano mesmo
- Uma avaliação que ainda não dispõe de amostras suficientes para estimar a dose dos morador
- Se moradores apresentam padrões naturalmente diferentes de consumo de água, essa variabili
- Diante de suspeita plausível de contaminação grave da água, medidas de precaução só podem 
- Uma análise de risco por exposição química pode compreender identificação do perigo, avali
- O aparecimento de sintomas em moradores após o funcionamento de uma atividade industrial é
- Em área contaminada, estudar apenas a concentração no poço pode ser insuficiente quando pa
- Quando uma substância não é detectada por método laboratorial, a exposição correspondente 
- Uma matriz qualitativa que cruza probabilidade e gravidade pode orientar a triagem de risc
- Ao usar valores conservadores de ingestão na avaliação de crianças, o analista pode aprese
- Quando moradores explicam que não conseguem executar orientação sanitária por falta de alt
- Em um episódio de exposição ambiental, publicar um comunicado oficial basta para comprovar
- Depois da implantação de uma barreira de proteção, pode permanecer risco residual, que exi
- Em qualquer avaliação sanitária, o risco pode ser calculado exatamente pela multiplicação 
- Se a água contaminada foi comprovada e a fonte está identificada, interromper o fornecimen
- Se dois modelos toxicológicos fornecem estimativas diferentes, a equipe deve registrar os 
- Ao acompanhar intervenção contra contaminante de efeito tardio, a redução imediata da conc
- Um programa de busca ativa de adoecimentos pode comparar diretamente números brutos anteri
- Ao escolher entre restrição temporária, mitigação na fonte e realocação, a decisão pode ex
- Depois da interdição sanitária, a revisão do plano deixa de ser necessária, porque a decis
```

## Pedido 43: Serviço Social: história, ética profissional e projeto ético-político (`servico-social.etica-projeto-etico-politico`, 298 tópicos de edital)

```
Matéria: servico-social.etica-projeto-etico-politico (Serviço Social: história, ética profissional e projeto ético-político)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Emergência, institucionalização e fundamentos históricos do Serviço Social": 18
- "2. Reconceituação, renovação profissional e construção do projeto ético-político": 21
- "3. Código de Ética: princípios, direitos, deveres, sigilo e relações profissionais": 23
- "4. Lei de regulamentação, competências, atribuições privativas e formação": 18
- "5. Campos sócio-ocupacionais, instrumentalidade e desafios contemporâneos": 20

Já existem (não repita):
- Uma equipe afirma que o Serviço Social brasileiro se tornou profissão regulamentada simple
- Uma pesquisadora prepara uma linha do tempo das primeiras iniciativas formativas da profis
- Em aula sobre reconceituação, uma estudante sustenta que o movimento latino-americano dos 
- Uma instituição anuncia que seu projeto ético-político profissional exige filiação de todo
- Uma pessoa formada em curso reconhecido pretende exercer legalmente a profissão em determi
- Durante acompanhamento familiar, a equipe solicita compartilhar dados privados com outras 
- Uma profissional emite parecer técnico sobre matéria própria de Serviço Social após invest
- Uma supervisora de formação apresenta as Diretrizes Curriculares da ABEPSS de 1996. Qual c
- Ao comparar Araxá, Teresópolis e o Congresso da Virada, uma pesquisadora precisa diferenci
- Uma chefia determina que o atendimento seja condicionado à participação em campanha eleito
- Uma organização aplica jornada regular de quarenta horas semanais a assistentes sociais vi
- Em estágio de Serviço Social, um aluno recebe supervisão somente de profissional de outra 
- Uma instituição mantém orientação interna que cita a Resolução CFESS nº 493/2006 como norm
- Durante atendimento, a profissional toma conhecimento de situação muito grave, potencialme
- Após migrar os registros de atendimentos para sistema eletrônico, uma equipe precisa rever
- A equipe realiza levantamentos socioeconômicos de usuários para orientar o acesso a benefí
- Em ação judicial, a profissional é convocada como testemunha para relatar fatos sigilosos 
- Um supervisor de campo trabalha trinta horas semanais e pretende receber cinco estagiários
- Uma pessoa atendida reivindica participar da decisão sobre encaminhamento. A profissional 
- Uma iniciativa institucional afirma que o projeto ético-político se materializa apenas pel
- Ao analisar a edição consolidada do Código de Ética de 1993, uma estudante afirma que o pr
- Uma reclamação sobre exercício profissional é dirigida ao CFESS, e a equipe confunde suas 
- Uma organização avalia o serviço exclusivamente pelo número mensal de atendimentos, embora
- Um currículo separa teoria social, realidade brasileira e técnica profissional em blocos s
- Uma plataforma disponibiliza atendimento remoto, porém registra áudio automaticamente e im
- Um relatório histórico situa a institucionalização do Serviço Social brasileiro antes da r
- Ao apresentar os documentos de Araxá (1967) e Teresópolis (1970), um professor os identifi
- Uma equipe que estuda o projeto ético-político considera liberdade, direitos humanos, demo
- Uma pessoa graduada em Serviço Social pode iniciar o exercício profissional imediatamente 
- Ao distinguir atribuições profissionais, uma equipe reconhece que o art. 5º, IV, da Lei nº
- O Código de Ética de 1993 considera a participação do usuário incompatível com a autoridad
- Uma instituição aplica a antiga Resolução CFESS nº 493/2006 como se ainda fosse a regra vi
- Uma coordenação distingue o Código de 1986 do de 1993 e reconhece o primeiro como parte da
- Em trabalho multiprofissional, o art. 16 do Código de Ética permite divulgação integral de
- Uma equipe reconhece que o trabalho profissional possui dimensões teórico-metodológica, ét
- Uma graduação usa a Resolução CNE/CES nº 15/2002 como fundamento para dispensar, em seu pr
- Os estudos socioeconômicos para fins de benefícios e serviços sociais estão no art. 5º, IV
- Uma chefia tenta impor a elaboração de relatório técnico deliberadamente falso. A existênc
- A reconceituação latino-americana e a renovação brasileira podem ser estudadas como proces
- Uma instituição informa que a Resolução CFESS nº 1.098/2025 revogou a nº 493/2006 e passou
- Ao apresentar registros sobre direitos e necessidades de uma família em reunião multiprofi
- Uma profissional considera a equidade e justiça social incompatíveis com regras públicas d
- Um estudo social destinado a subsidiar decisão pública deve explicitar fundamentos, evidên
- No exercício profissional, a ampla autonomia assegurada pelo art. 2º do Código de Ética si
- A legislação profissional reconhece como atribuição privativa a supervisão direta de estag
```

## Pedido 44: Ecologia e ciclos biogeoquímicos (`biologia.ecologia-ciclos`, 297 tópicos de edital)

```
Matéria: biologia.ecologia-ciclos (Ecologia e ciclos biogeoquímicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos ecológicos, habitat, nicho e dinâmica populacional": 17
- "2. Relações ecológicas, sucessão e biomas brasileiros": 18
- "3. Cadeias, teias, pirâmides, produtividade e contaminação trófica": 22
- "4. Ciclos da água, carbono e nitrogênio": 22
- "5. Fósforo, enxofre, alterações humanas e síntese dos ciclos": 21

Já existem (não repita):
- Em uma lagoa são observadas três espécies de peixes, duas de algas e várias bactérias que 
- Duas espécies de aves vivem na mesma mata; uma captura insetos à noite e outra busca fruto
- Uma população de anfíbios possuía 150 indivíduos; num período, ocorreram 40 nascimentos, 1
- Uma tempestade intensa destrói parte de uma floresta independentemente da lotação anterior
- Em cultivo experimental, duas espécies de plantas competem por fósforo escasso, e cada uma
- Após abandono de pastagem, arbustos e árvores começam a colonizar o local, que ainda possu
- Um mapa nacional representa os biomas terrestres e, separadamente, o Sistema Costeiro-Mari
- Na sequência algas → zooplâncton → peixe pequeno → peixe grande, como deve ser interpretad
- Uma floresta incorpora CO₂ à matéria orgânica dos produtores por processo dependente de lu
- Produtores de uma área fixam 1.800 kcal/m²/ano de energia e consomem 700 kcal/m²/ano em su
- Uma árvore sustenta centenas de insetos herbívoros, e a pirâmide de números fica invertida
- Em lago, um peixe predador apresenta concentração de metilmercúrio maior que um peixe que 
- Um exercício considera eficiência trófica de 10% em duas transferências. Partindo de produ
- A urbanização impermeabilizou parte de uma bacia hidrográfica e reduziu sua área de infilt
- Uma amostra de solo apresenta transformação microbiana de amônio primeiro em nitrito e dep
- Uma área recebe fosfato pela alteração natural de minerais, e parte segue por erosão até s
- Uma população de peixes atinge abundância próxima a K no modelo logístico simples. O que s
- Uma população segue exatamente dN/dt = rN(1 − N/K), com r e K positivos constantes. Em que
- Duas espécies aparentadas convivem em uma floresta, mas uma explora insetos pequenos nos g
- Uma comunidade aquática apresenta 8 g/m² de fitoplâncton e 18 g/m² de zooplâncton num inst
- Um predador de longa vida possui concentração elevada de certo radionuclídeo, mas não fora
- Após adicionar fertilizante nitrogenado a um solo encharcado, parte do nitrato é transform
- Após expansão de atividade industrial que emite SO₂, uma região registra deposição atmosfé
- Depois de aporte elevado de nutrientes a lago, há floração de algas e, semanas depois, bai
- Uma área desmatada substitui árvores maduras por pastagem, enquanto ocorre queima de bioma
- Em determinada área e período, todos os indivíduos de uma única espécie de borboleta const
- Uma espécie de morcego que vive em caverna e se alimenta de insetos possui como habitat a 
- Após corte de vegetação de área agrícola onde permanecem solo, raízes e sementes, a recupe
- Numa cadeia vegetal → herbívoro → carnívoro, as setas indicam que energia é reciclada do c
- No ciclo da água, infiltração corresponde à entrada de água no solo, enquanto evaporação é
- Os seis biomas terrestres brasileiros oficialmente delimitados pelo IBGE incluem o Sistema
- Uma população com 90 indivíduos ocupa 3 hectares, enquanto outra de 90 indivíduos ocupa 9 
- Duas espécies que compartilham o mesmo ambiente devem apresentar o mesmo nicho ecológico, 
- Uma pirâmide de biomassa pode apresentar produtores com estoque instantâneo menor que o do
- A produtividade primária líquida de um ecossistema corresponde à produtividade bruta somad
- Uma amostra de solo apresenta oxidação biológica de amônio em nitrito e nitrato, enquanto 
- O ciclo do fósforo possui, como reservatório central no modelo ecológico básico, o gás P₂ 
- No Pantanal, mudanças sazonais no nível de inundação condicionam distribuição de organismo
- No modelo logístico com r e K constantes, a taxa absoluta de crescimento é maior exatament
- Um peixe pode apresentar bioacumulação de metilmercúrio ao longo da vida, e a biomagnifica
- Em lago eutrofizado, após morrerem grandes massas de algas, a respiração dos decompositore
- O desmatamento pode alterar simultaneamente fluxos de carbono, infiltração, evapotranspira
- O enxofre se comporta exatamente como o fósforo, sem etapa atmosférica relevante; por isso
- Mesmo que um lago apresente aumento do estoque instantâneo de zooplâncton acima do de fito
- Riqueza de espécies e equitabilidade são sinônimos: duas áreas que contenham as mesmas dez
```

## Pedido 45: Comunicação digital e produção de conteúdo (`comunicacao.comunicacao-digital-midias`, 297 tópicos de edital)

```
Matéria: comunicacao.comunicacao-digital-midias (Comunicação digital e produção de conteúdo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teorias da comunicação e ecossistema digital": 22
- "2. Planejamento editorial, mídias digitais e imagem institucional": 21
- "3. Linguagem SEO, acessibilidade e governança do conteúdo": 19
- "4. Podcasts, roteiros audiovisuais e peculiaridades dos veículos": 19
- "5. Eventos, gestão de projetos de comunicação e avaliação integrada": 19

Já existem (não repita):
- Ao publicar vídeo educativo, uma equipe descobre que a locução ficou incompreensível por f
- Uma chamada de campanha afirma: 'Confirme sua participação até amanhã pelo formulário indi
- Uma publicação em rede social gera 8.000 exibições, mas a plataforma estima 5.200 contas d
- Uma publicação precisa ser encontrada em buscas por um procedimento atualizado. Qual práti
- Uma pessoa vai disponibilizar episódio de áudio exclusivamente falado e pré-gravado em pág
- Uma equipe prepara entrevistas, vinheta e gravação de um episódio. Qual etapa reúne checag
- Um vídeo usa texto sobreposto para identificar a pessoa entrevistada e sua função. Na ling
- Um evento terá horários, responsáveis, roteiro de falas, recursos audiovisuais e plano par
- Uma equipe recebe 300 inscrições para um encontro e registra 210 pessoas presentes. Qual é
- Uma campanha registra 12.000 impressões, 240 cliques em link e 36 inscrições concluídas. Q
- Uma publicação frequente faz crescer a discussão sobre orçamento cultural, mas os grupos d
- Uma instituição publicou página útil, mas há três versões repetidas com endereços diferent
- Uma página utiliza imagem apenas como moldura visual, sem conteúdo informativo, e outra im
- Uma equipe publica série de entrevistas de áudio em feed RSS e percebe que dois episódios 
- Um vídeo apresenta dados essenciais em gráfico, enquanto a locução apenas afirma 'observe 
- Uma campanha em cinco plataformas repete literalmente o mesmo vídeo, sem oferecer novas pe
- Durante inscrições em evento gratuito, o formulário pede muitos dados pessoais sem vínculo
- Uma instituição tem página pública indexada e tenta removê-la dos resultados apenas coloca
- Um gestor recebe 120 respostas de satisfação, das quais 108 são positivas, entre 400 parti
- Uma equipe inclui música comercial de terceiro na vinheta de podcast e, para se justificar
- Uma pessoa de edição recebe entrevista de 40 minutos e seleciona falas breves. A montagem 
- Uma campanha dirigida a pessoas com dificuldades de acessar formulários tem muitas visuali
- Uma estratégia prevê cinco publicações idênticas e outra propõe reportagem em página, entr
- Uma transmissão institucional ao vivo prevê fala, gráficos e perguntas do público remoto. 
- Durante crise, circularam informações erradas sobre local e horário de um encontro. A equi
- Uma peça de áudio apresenta ruído de gravação que impede compreender parte do depoimento. 
- O número de impressões de uma campanha equivale ao de pessoas distintas alcançadas, porque
- A descrição textual alternativa de uma imagem informativa deve transmitir seu sentido rele
- Em roteiro audiovisual de duas colunas, a coluna de vídeo contém locução e trilha, enquant
- Uma equipe de evento pode usar um rundown para registrar sequência de atividades, horários
- Um podcast que disponibiliza áudio gravado com capa e resumo de duas linhas oferece, por e
- Uma estratégia apresenta o mesmo tema em reportagem de página, episódio de áudio e demonst
- No planejamento de comunicação, produzir vinte publicações por mês demonstra necessariamen
- A meta description pode contribuir para o resumo de resultado apresentado em busca, mas o 
- Quando um episódio de podcast usa RSS, cada item de mídia precisa apenas de título e capa,
- O tratamento de dados de inscrição em evento deve ser compatível com uma finalidade identi
- A divulgação pública de música protegida em plataforma digital permite seu uso integral em
- Em comunicação digital, a medição de alcance permite determinar automaticamente quantas pe
- Em uma produção digital, a narrativa pode circular em vários meios sem que cada um reprodu
- Adicionar sitemap a um portal é suficiente para garantir que todas as páginas listadas sej
- Uma equipe de acessibilidade que atende vídeo ao vivo com fala precisa considerar legendas
- Uma instituição divulgou música protegida em vinheta e identificou compositor e intérprete
- Uma série distribuída por RSS deve distinguir o arquivo de áudio do feed que fornece metad
- Em evento com 500 inscrições e 350 presenças, houve comparecimento de 30%, pois o total de
- Ao decidir reduzir a coleta de inscrições de um evento a nome e contato necessários para c
```

## Pedido 46: Legislação ambiental de Mato Grosso: Código Ambiental (LC 38/1995), CAR e PRA (LC 592/2017) e políticas estaduais (`mato-grosso.legislacao-ambiental-estadual`, 296 tópicos de edital)

```
Matéria: mato-grosso.legislacao-ambiental-estadual (Legislação ambiental de Mato Grosso: Código Ambiental (LC 38/1995), CAR e PRA (LC 592/2017) e políticas estaduais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. LC 38/1995: princípios, sistema estadual, CONSEMA e FEMAM": 25
- "2. LC 38/1995: instrumentos, licenciamento e EIA/RIMA": 25
- "3. LC 592/2017: Programa de Regularização Ambiental e CAR": 25
- "4. Outras normas ambientais de Mato Grosso": 25

Já existem (não repita):
- A LC 38/1995 institui o Código Ambiental do Estado de Mato Grosso e estabelece as bases no
- O Sistema Estadual do Meio Ambiente de Mato Grosso, sob gestão da SEMA, é composto, entre 
- O CONSEMA é composto paritariamente por 9 representantes do Poder Público, 9 da sociedade 
- As decisões do CONSEMA são formalizadas em:
- Segundo a LC 38/1995, compete ao CONSEMA decidir, como última instância administrativa em 
- Quando a matéria em deliberação implicar ação conjunta para preservação do Pantanal, o CON
- O FEMAM é presidido pelo Secretário de Estado do Meio Ambiente e tem um Diretor Executivo 
- Os recursos do FEMAM provenientes da cobrança pelo uso da água devem ser aplicados:
- Entre as competências da SEMA previstas na LC 38/1995 está exercer o poder de polícia ambi
- NÃO figura entre os instrumentos da Política Estadual do Meio Ambiente previstos no art. 1
- A inscrição no Cadastro Técnico Estadual de atividades potencialmente poluidoras, com reno
- A licença que, na fase preliminar do planejamento, aprova a localização e a concepção do e
- A Licença Ambiental Única (LAU), prevista na LC 38/1995, autoriza:
- Pela LC 38/1995, a Licença de Operação tem prazo de validade mínimo de 3 e máximo de 6 ano
- A renovação da Licença de Operação deve ser requerida com antecedência mínima de:
- A Licença Prévia expedida pela SEMA depende do expresso assentimento das Prefeituras Munic
- Para o registro de loteamento, a LC 38/1995 exige que o cartório verifique:
- Pela LC 38/1995, servidores públicos da administração direta e indireta do Estado podem in
- Entre as atividades que, pela LC 38/1995, dependem de EIA/RIMA está a implantação de:
- Nos casos em que se exige EIA/RIMA, a Licença Prévia concedida deve, como condição de vali
- A SEMA pode suspender ou cancelar uma licença quando houver:
- O Estado e os Municípios devem elaborar o Relatório da Qualidade do Meio Ambiente a cada d
- O SIMCAR, criado pela LC 592/2017, é:
- Pela LC 592/2017, o CAR é registro eletrônico obrigatório para todos os imóveis rurais.
- Segundo a LC 592/2017, o CAR fica na condição de Cancelado quando:
- Após a adesão ao PRA e enquanto cumprido o termo de compromisso, o proprietário não pode s
- O termo de compromisso firmado no PRA, segundo a LC 592/2017:
- Pela LC 592/2017, o pousio é a interrupção temporária de usos agrícolas, pecuários ou silv
- Área rural consolidada é a área de imóvel rural com ocupação antrópica preexistente a 22 d
- Para a inscrição no CAR, a propriedade do imóvel rural é comprovada por certidão da matríc
- Licenças e autorizações que dependam de supressão de vegetação nativa em imóvel rural fica
- O Sistema Estadual de Unidades de Conservação de Mato Grosso (SEUC) foi instituído pela:
- A Política Estadual de Mudanças Climáticas de Mato Grosso foi instituída pela Lei Compleme
- A Resolução CONSEMA 74/2025 trata:
- O Decreto Estadual 336/2007 disciplina a outorga de direito de uso dos recursos hídricos e
- A Política Estadual de Gestão e Proteção à Bacia do Alto Paraguai está ligada principalmen
- O Decreto Estadual 1.031/2017 regulamenta a LC 592/2017 quanto ao PRA, ao SIMCAR e aos pro
- Mato Grosso abrange três biomas, o que dá centralidade ao CAR e ao controle do desmatament
- A licença concedida com a aprovação do Plano de Manejo Florestal Sustentável, cuja validad
- Os empreendimentos industriais, comerciais e de mineração sujeitos a licenciamento devem c
```

## Pedido 47: Prescrição e decadência no Código Civil (`direito-civil.prescricao-decadencia`, 295 tópicos de edital)

```
Matéria: direito-civil.prescricao-decadencia (Prescrição e decadência no Código Civil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Pretensão, prescrição, decadência e exercício dos direitos": 35
- "2. Prazos prescricionais e reconhecimento da prescrição": 15
- "3. Impedimento, suspensão, interrupção e efeitos sobre terceiros": 18
- "4. Decadência legal e convencional: renúncia e prazos do Código Civil": 15
- "5. Prescrição perante a Fazenda Pública, imprescritibilidade e questões integradas": 17

Já existem (não repita):
- Uma dívida venceu, não foi paga e tornou-se exigível. O credor passou a poder exigir a pre
- Uma pessoa quer anular contrato celebrado em situação de coação, exercendo poder de desfaz
- Credor e devedor estipulam em contrato particular que a prescrição de qualquer cobrança re
- Depois de consumada a prescrição de uma obrigação, o devedor assume de modo inequívoco a d
- O julgador de uma demanda civil constata prescrição pelas informações existentes e deve ap
- Uma pretensão civil sujeita ao regime geral não possui prazo prescricional menor fixado em
- O proprietário de imóvel urbano exige pagamento de aluguéis vencidos, sem outra questão ju
- Uma pessoa pretende cobrar uma prestação alimentar mensal vencida, isoladamente considerad
- Um crédito líquido e exigível está previsto em instrumento particular assinado e não se en
- Uma professora encerrou prestação profissional e pretende cobrar honorários pelo serviço, 
- Uma pretensão civil é exercitável entre duas pessoas que permanecem casadas e cuja socieda
- Um menor de quinze anos é titular de pretensão civil, mas transcorre tempo sem que haja co
- Contrato prevê pagamento em data futura ainda não alcançada, sem antecipação de vencimento
- A pretensão civil deriva diretamente de fato que deve ser apurado no juízo criminal, com o
- A prescrição é suspensa por razão pessoal em favor de um entre vários credores solidários.
- Durante a contagem de prazo prescricional, o devedor envia declaração inequívoca reconhece
- Um credor obteve interrupção válida de prescrição de acordo com o art. 202 do Código Civil
- Uma obrigação tem dois devedores solidários. A prescrição é validamente interrompida contr
- Uma pessoa exerce ação puramente declaratória para verificar vínculo de estado pessoal; ou
- Um negócio anulável está sujeito à decadência fixada diretamente em lei, e a parte benefic
- Particular exige pagamento de quantia da Fazenda Pública municipal por obrigação vencida, 
- Uma cobrança contra a Fazenda sujeita ao prazo de cinco anos foi validamente interrompida 
- Negócio foi celebrado em janeiro sob coação, que terminou em abril. Sem causas legais exce
- Uma pessoa recebe bem móvel e descobre de imediato vício redibitório aparente à verificaçã
- Na venda de um imóvel, o vendedor reserva validamente direito de retrovenda com restituiçã
- Uma dívida civil vencida e não paga pode originar pretensão do credor, e o art. 189 do Cód
- Na disciplina do Código Civil, é permitido que partes plenamente capazes reduzam por contr
- O art. 205 do Código Civil prevê prazo prescricional de dez anos quando a lei não tiver es
- Para cobrança de aluguéis de prédios urbanos, o art. 206, § 3º, I, do Código Civil estabel
- Uma pretensão de receber prestação alimentar vencida tem prazo de dois anos desde o vencim
- Os três anos previstos no art. 206, § 3º, V, para reparação civil alcançam necessariamente
- Na obrigação cujo prazo de pagamento ainda não venceu, o art. 199, II, do Código Civil imp
- Pelo Código Civil, a interrupção da prescrição pode ocorrer indefinidamente, sempre que o 
- O art. 202, VI, do Código Civil reconhece como causa de interrupção da prescrição um ato i
- Quando o prazo de decadência é fixado em lei, a parte a quem ele favorece pode renunciar v
- Na decadência convencional, a parte favorecida pode alegá-la em qualquer grau de jurisdiçã
- Uma pessoa contratou sob coação que cessou meses após a celebração; o art. 178, I, estabel
- Em regra, o prazo de decadência para redibir ou obter abatimento por vício redibitório é d
- Uma vítima de antiga ofensa à honra pretende receber indenização em dinheiro e sustenta qu
- Um pedido administrativo formal de pagamento de dívida líquida contra a Fazenda pode suspe
- Se prescrição quinquenal contra a Fazenda foi interrompida validamente após apenas um ano,
- Uma ação de ressarcimento ao erário fundada na prática de ato doloso efetivamente tipifica
- Segundo o Tema 666 do STF, qualquer ação de reparação de danos ao erário decorrente de ilí
- Uma pretensão de ressarcimento ao erário que se funda em decisão de Tribunal de Contas est
- A possibilidade de requerer declaração judicial de nulidade absoluta de um negócio faz com
```

## Pedido 48: Consórcios Públicos (`direito-administrativo.consorcios-publicos`, 293 tópicos de edital)

```
Matéria: direito-administrativo.consorcios-publicos (Consórcios Públicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Federalismo cooperativo e natureza dos consórcios públicos": 19
- "2. Protocolo de intenções, ratificação e organização": 19
- "3. Contrato de rateio, contrato de programa e prestação de contas": 21
- "4. Serviços públicos, concessões, permissões e parcerias": 19
- "5. Bens públicos, fiscalização e ciclo de vida consorcial": 22

Já existem (não repita):
- Dois Municípios desejam organizar uma pessoa jurídica para prestar serviços de interesse c
- Um grupo de Municípios pretende aderir a um consórcio após subscrever protocolo de intençõ
- Três Municípios já consorciados devem transferir recursos orçamentários para manter despes
- Uma concessão de transporte é remunerada por tarifas dos usuários e por contraprestação pe
- Uma praça de acesso geral permanece aberta à população e é mantida por consórcio público. 
- Um Estado decide integrar um consórcio de direito público. Qual efeito organizacional resu
- Após a assinatura do protocolo de intenções, os participantes desejam definir o funcioname
- Dois entes celebram contrato de programa para organizar a execução de serviço em gestão as
- Uma concessionária deve cumprir condições de qualidade do serviço prestado à população. Qu
- Um ente consorciado pretende retirar-se e recuperar automaticamente máquinas anteriormente
- A União quer ingressar em consórcio composto por Municípios situados em dois Estados, mas 
- Um ente ratifica protocolo de intenções três anos após subscrevê-lo e não havia lei anteri
- Um consórcio público de direito privado vai adquirir equipamentos de terceiro estranho aos
- Em projeto de prestação de serviços ao próprio Poder Público, uma PPP é estudada por sete 
- Um Município mantém imóvel público sem destinação a serviço ou uso coletivo e decide alien
- No protocolo de intenções, cinco entes pretendem estabelecer votos diferentes na assemblei
- Quatro entes aprovam em assembleia o encerramento do consórcio; apenas três conseguem apro
- Município participa de consórcio para realizar atividade de saneamento, mas quer atribuir 
- Um parceiro público pretende contratar PPP por quatro anos, valor de R$ 30 milhões e prest
- O representante legal do consórcio tem suas contas fiscalizadas por Tribunal de Contas com
- Cinco Municípios constituíram consórcio; um deles havia aprovado lei específica de partici
- Após a Lei 14.662/2023, um consórcio com sete participantes quer modificar cláusula do con
- Um Município deixa de prever na lei orçamentária dotação suficiente para obrigação de rate
- Após aviso prévio, uma concessionária pretende iniciar na sexta-feira o corte do serviço p
- A extinção de consórcio foi aprovada regularmente, mas ainda não houve atribuição definiti
- Dois Municípios decidiram criar associação pública para realizar objetivos de interesse co
- Após assinarem um protocolo de intenções, os entes devem considerar constituído e dotado d
- Consórcio público de direito privado deverá observar disciplina pública para licitações, c
- No financiamento do consórcio, o contrato de programa constitui o instrumento geral para e
- A Lei 11.079/2004 considera concessão patrocinada a modalidade em que à tarifa paga pelos 
- Por se tratar de consórcio constituído por contrato entre entes federativos, o protocolo d
- A entrega de recursos financeiros ao consórcio por contrato de rateio não autoriza sua des
- De acordo com o regime atual da Lei 11.107/2005, basta que a maioria dos consorciados rati
- Uma concessão administrativa pode ter como usuária direta ou indireta a administração públ
- Como a entidade consorcial passa pelo controle de contas ligado ao seu representante legal
- Um Município ratifica o protocolo de intenções passados três anos da subscrição. Ainda que
- Se seis entes compõem um consórcio, a alteração do contrato aprovada pela assembleia depen
- O consórcio público pode ser contratado com dispensa de licitação pela administração diret
- A lei do saneamento básico permite que novos contratos de programa substituam concessão li
- Consórcio formado por dois entes é dissolvido após saída regular de um deles; essa retirad
- A exclusão de ente por falta de dotação suficiente para o contrato de rateio dispensa susp
- No regime civil dos bens públicos, praça e prédio administrativo conservam inalienabilidad
- Na PPP administrativa, por ser a administração a usuária do serviço, o contrato pode durar
- Se a assembleia geral aprova a extinção do consórcio e ainda não identifica definitivament
- A Lei 11.107/2005 exige que o contrato de rateio anual possua vigência limitada às dotaçõe
```

## Pedido 49: SIAFI, SIOP, SICONFI e sistemas de administração financeira (`afo.siafi-siop-siconfi`, 292 tópicos de edital)

```
Matéria: afo.siafi-siop-siconfi (SIAFI, SIOP, SICONFI e sistemas de administração financeira)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organização sistêmica das finanças públicas e funções dos sistemas": 29
- "2. SIAFI: execução, documentos, Conta Única e recolhimentos": 23
- "3. Segurança, usuários e conformidade no SIAFI": 20
- "4. SIOP, programação e acompanhamento orçamentário": 15
- "5. SICONFI, Matriz de Saldos Contábeis e Tesouro Gerencial": 13

Já existem (não repita):
- 1. Uma unidade federal precisa examinar o registro de empenhos, liquidações e pagamentos d
- 2. Ao preparar a proposta de Lei Orçamentária Anual, uma equipe precisa lançar programação
- 3. Uma entidade federativa pretende transmitir informações contábeis detalhadas, extraídas
- 4. Uma unidade já possui autorização orçamentária e registra o compromisso de despesa com 
- 5. Após comprovação de entrega e liquidação, uma unidade realizará pagamento de fornecedor
- 6. Uma pessoa recolhe valor devido por serviço público federal mediante documento de arrec
- 7. Uma equipe deseja identificar onde são mantidas, na sistemática constitucional, as disp
- 8. Ao examinar lançamentos feitos por uma unidade, um servidor confere se cada operação re
- 9. Um gestor precisa extrair relatório cruzando períodos e naturezas de despesa já registr
- 10. Ao estudar as competências da Lei nº 10.180/2001, uma equipe identifica atividades de 
- 11. Uma prefeitura já dispõe de sistema contábil próprio e precisa enviar saldos para a co
- 12. O sistema identifica que determinado usuário pode consultar saldos, mas não lançar pag
- 13. Durante a execução, uma unidade comprova que um fornecedor realizou o serviço previsto
- 14. Uma unidade recebeu limite orçamentário suficiente para contratar, mas o caixa disponí
- 15. Em auditoria, uma pessoa confunde a verificação mensal das credenciais dos usuários co
- 16. Uma unidade identificou a necessidade de reforçar dotação já existente e registra a pr
- 17. Um relatório de transparência apresenta resultado fiscal referente a dois meses, publi
- 18. Uma unidade federal emitiu NE, mas pretende pagar integralmente antes de verificar a e
- 19. Uma entidade municipal de pequeno porte precisa preparar o RGF. A equipe afirma que o 
- 20. Uma equipe analisa a consistência dos demonstrativos contábeis e, separadamente, a doc
- 21. A unidade responsável pelo planejamento recebeu meta para instalar equipamentos e comp
- 22. Um analista recebe arquivo com saldos mensais de diversos entes e pretende avaliar a c
- 23. Ao conferir segurança no SIAFI, o gestor identifica usuários habilitados com perfil in
- 24. Em um fluxo integrado, uma equipe planeja ação, empenha despesas e depois extrai indic
- 25. Uma despesa expressamente autorizada será custeada por suprimento de fundos porque o p
- 26. Em determinada unidade federal, o registro e o acompanhamento de empenhos e pagamentos
- 27. Após uma compra pública, a emissão da nota de empenho comprova a entrega do objeto e a
- 28. Uma pessoa recolhe taxa federal por GRU; esse documento é a modalidade ordinária de pa
- 29. Uma equipe pretende produzir relatórios analíticos com dados provenientes do SIAFI. O 
- 30. Ao transmitir a Matriz de Saldos Contábeis, um ente federativo encaminha dados estrutu
- 31. A conformidade de operadores consiste na conferência da nota fiscal e do atesto que fu
- 32. Quando uma dotação é incluída no orçamento federal, a programação financeira fica auto
- 33. O acompanhamento de metas físicas no SIOP pode indicar entregas de uma ação orçamentár
- 34. Segundo a sistemática geral da LRF, o RREO é divulgado ao final de cada quadrimestre e
- 35. No regime geral federal, a Conta Única do Tesouro é mantida no Banco Central e contrib
- 36. O Sistema de Contabilidade Federal, previsto na Lei nº 10.180/2001, tem por finalidade
- 37. A classificação de uma operação como pagamento em ordem bancária transfere para o SICO
- 38. O suprimento de fundos do art. 68 da Lei nº 4.320/1964 torna desnecessária a formaliza
- 39. A pessoa que responde pela conformidade de registros de gestão não substitui, por essa
- 40. A abertura de crédito suplementar é utilizada, pela Lei nº 4.320/1964, quando a despes
- 41. Na apreciação de dados fiscais da Federação, divergências entre MSC e RREO podem deman
- 42. Para acesso ao SIAFI, a existência de cadastro individual dispensa a habilitação para 
- 43. O envio da DCA e da MSC ao SICONFI atende a funções informacionais distintas: a primei
- 44. A adoção de um meio digital de recolhimento de receitas, como o PagTesouro, desloca pa
- 45. Mesmo com fonte e dotação orçamentárias identificadas, o exame de relatório de despesa
```

## Pedido 50: Windows Server, Active Directory e serviços de servidor (`informatica.windows-server-active-directory`, 290 tópicos de edital)

```
Matéria: informatica.windows-server-active-directory (Windows Server, Active Directory e serviços de servidor)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Active Directory, identidade, LDAP e políticas de grupo": 24
- "2. Windows Server, PowerShell, atualizações e acesso remoto": 22
- "3. Alta disponibilidade, virtualização, consolidação e RAID": 17
- "4. Red Hat Enterprise Linux e serviços fundamentais de rede": 15
- "5. Servidores web, proxy reverso e servidores de aplicação": 22

Já existem (não repita):
- Uma rede corporativa precisa autenticar usuários e reunir computadores, contas e grupos so
- A administração pretende delegar a gestão de usuários de um departamento sem criar outro d
- Um sistema precisa consultar atributos de usuários mantidos no serviço de diretório. Qual 
- Sem imposição, filtros ou bloqueios, políticas conflitantes estão vinculadas ao domínio e 
- Uma instituição deseja sincronizar e aprovar atualizações primeiro em máquinas de testes e
- Um técnico precisa executar um comando PowerShell em computadores remotos já preparados pa
- Uma organização quer disponibilizar uma sessão de desktop de servidor aos usuários, transm
- Um conjunto de quatro discos de 1 TB é configurado em RAID 5 convencional. Qual capacidade
- Um servidor Linux utiliza systemd como gerenciador de serviços. Qual comando configura o s
- Um portal público usa Nginx na frente de um serviço interno, encaminhando requisições HTTP
- Em uma floresta com dois domínios, uma aplicação precisa localizar objetos pertencentes a 
- Uma política de domínio foi marcada como Enforced; uma OU subordinada bloqueia herança e d
- Uma equipe quer que usuários externos acessem aplicativos publicados por RDS através de HT
- Um usuário perde a conexão com sua sessão RDS, que permanece ativa em um dos vários hosts.
- Dois nós de cluster dispõem de um voto cada e uma testemunha independente possui mais um v
- Dois discos pertencentes ao mesmo par espelhado de um RAID 10 com quatro discos falham ao 
- Um administrador migra rotinas de serviços de RHEL 6 para RHEL 7. Qual mudança de gestão d
- Uma aplicação Java originalmente implantada no Tomcat 9 é migrada para Tomcat 10. A inicia
- Um sistema de aplicação JBoss EAP 7 possui quatro instâncias e exige configuração centrali
- Uma empresa deseja publicar serviço interno em Apache HTTP Server, ajustando redirecioname
- Em um ambiente com GPOs conflitantes, o vínculo de domínio é Enforced, a OU filha usa Bloc
- O WSUS de uma filial está configurado em modo de réplica de um servidor upstream. A equipe
- Um host físico executa seis VMs. Após defeito do próprio host, todas ficam indisponíveis, 
- Uma aplicação Linux falha ao abrir um arquivo. As permissões tradicionais permitem acesso 
- Um usuário externo acessa portal HTTPS com Nginx que repassa para Tomcat em porta interna.
- Ao criar uma OU chamada Projetos dentro de um domínio existente, o administrador pode nela
- Ao iniciar o serviço sshd em um servidor RHEL 7 com `systemctl start sshd`, o administrado
- Uma filial com relógios divergentes entre servidores pode usar NTP para sincronizar horári
- Um arranjo RAID 0 com três unidades de armazenamento permanece operacional, sem perda de d
- Uma empresa que disponibiliza desktop remoto por RDP e outra que fornece acesso à rede por
- No Nginx empregado como proxy reverso, a diretiva `server_name` é a responsável por encami
- Em uma floresta do AD com dois domínios, o catálogo global permite localizar informações s
- Se uma OU estiver com Block Inheritance, as configurações de uma GPO superior com vínculo 
- Uma implantação de RDS pode usar RD Session Host para hospedar sessões e RD Connection Bro
- Um servidor WSUS upstream foi escolhido para administração centralizada. Mesmo em modo de 
- Em um cluster com dois nós votantes e uma testemunha que também vota, a disponibilidade de
- Uma VM protegida por RAID no próprio host físico dispensa cópias de segurança, porque o ar
- Em RHEL 7, uma negativa de acesso pelo SELinux em modo enforcing pode ocorrer mesmo quando
- Uma aplicação legada que importa `javax.servlet` poderá ser movida para Tomcat 10 sem ajus
- Um administrador utiliza `ProxyPassReverse` no Apache HTTP Server para ajustar cabeçalhos 
- Como JBoss EAP 7 possui opção managed domain, a instância configurada nesse modo se torna 
- Em uma floresta com vários domínios, os sites do AD podem representar sub-redes e orientar
- O uso de RDS com RD Gateway elimina a necessidade de RD Session Host em uma arquitetura qu
- Uma estratégia que consolida dez servidores físicos em VMs pode reduzir o número de hosts,
- Em um servidor RHEL 7 com SELinux em permissive, a política MAC impede efetivamente todo a
```

