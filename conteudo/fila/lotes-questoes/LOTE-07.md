# Pedidos de questões extras, lote 7 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 19634 tópicos de edital.

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

## Pedido 1: Enfermagem em urgência, emergência e trauma (`enfermagem.urgencia-emergencia-trauma`, 423 tópicos de edital)

```
Matéria: enfermagem.urgencia-emergencia-trauma (Enfermagem em urgência, emergência e trauma)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Organização da urgência e avaliação inicial": 19
- "2. Ressuscitação, via aérea e acessos em emergências": 23
- "3. Trauma e hemorragias: avaliação e intervenções": 19
- "4. Emergências clínicas e cuidados críticos": 19
- "5. Pediatria, situações especiais e transplantes": 20

Já existem (não repita):
- Na recepção de uma unidade de urgência, uma pessoa que acabou de chegar apresenta estridor
- Após colisão, um adulto está consciente, fala frases completas, mas apresenta sangramento 
- Durante ressuscitação de adulto, o monitor mostra complexos elétricos organizados, mas não
- Dois profissionais iniciam RCP de adulto sem via aérea avançada, após constatar apneia e a
- Em uma enfermaria, uma técnica de enfermagem encontra pessoa em PCR e dispõe de DEA, enqua
- Uma central de urgências organiza unidade móvel de suporte intermediário de vida. Qual com
- Em uma sala de emergência, o enfermeiro colhe dados, identifica respostas clínicas, define
- Um adulto consciente apresenta obstrução grave de via aérea por alimento, sem conseguir fa
- Durante atendimento de urgência, uma pessoa instável necessita de medicação imediata e a e
- Pessoa intubada sob ventilação mecânica apresenta alarme de pressão elevada, redução do vo
- Um adulto está em assistolia confirmada, recebe RCP e dispõe de acesso IV. No algoritmo av
- Após atropelamento, uma pessoa apresenta dor pélvica intensa, taquicardia e sinais de hipo
- Após trauma craniano, paciente abre os olhos à voz (E3), conversa confusamente (V4) e loca
- Paciente com sonolência e hipoventilação apresenta gasometria com pH 7,24, PaCO₂ 60 mmHg e
- Após ferimento penetrante no tórax, paciente apresenta entrada audível de ar pela lesão e 
- Um enfermeiro habilitado dispõe de máscara laríngea para pessoa em deterioração respiratór
- Paciente com disfunção renal aguda apresenta potássio muito elevado e alterações eletrocar
- Uma criança de 4 anos está em parada cardiorrespiratória. Dois profissionais de saúde real
- Um lactente de 8 meses, consciente, apresenta engasgo grave, tosse ineficaz e incapacidade
- Criança de 2 anos tem pulso palpável a 52/min, extremidades frias e rebaixamento. Mesmo ap
- Uma criança de 20 kg apresenta fibrilação ventricular na PCR e será desfibrilada pela prim
- Uma pessoa potencial doadora teve morte encefálica confirmada nos critérios aplicáveis e p
- Após mergulho autônomo, uma pessoa apresenta dor em articulações, parestesias e fraqueza p
- Uma pessoa com provável intoxicação por agrotóxico chega sonolenta, com salivação intensa 
- Na sala de emergência, duas pessoas estão em choque: uma sofreu grande hemorragia externa 
- Ao chegar a uma ocorrência de atropelamento em rodovia, uma equipe identifica tráfego não 
- Na recepção de serviço de urgência, a chegada de um paciente em ambulância lhe confere pri
- Uma pessoa adulta não responde, apresenta gasping e o profissional não identifica pulso ap
- Durante ressuscitação, a identificação de assistolia no monitor determina desfibrilação im
- Em paciente com trauma abdominal e taquicardia, a preservação da pressão arterial inicial 
- Após administrar uma intervenção em sala de emergência, o enfermeiro reavalia sinais e res
- Paciente sob ventilação mecânica apresenta alarme de pressão alta e secreção espessa. O en
- Um serviço implanta novo protocolo de administração de medicamentos na modalidade de supor
- Durante avaliação de paciente com convulsão, a equipe protege a pessoa contra lesões, obse
- Diante de suspeita de fratura pélvica instável e choque, a faixa de estabilização deve ser
- No contexto da Resolução Cofen 639/2020, o enfermeiro pode decidir isoladamente mudanças i
- Em PCR adulta com via aérea avançada já instalada, a equipe mantém compressões contínuas e
- Ao controlar sangramento grave de membro com torniquete, a equipe deve afrouxá-lo periodic
- Uma pessoa chega agitada, com fala desconexa e sudorese. Antes de atribuir o quadro exclus
- A confirmação de morte encefálica por médicos não integrantes da equipe de transplante, as
- Uma criança com pulso palpável respira de modo irregular e insuficiente. Até que se restab
- Após atividade física intensa em ambiente muito quente, paciente apresenta hipertermia e a
- Em um paciente com trauma de perna, suspeita de fratura da tíbia e acesso venoso periféric
- Um adolescente é retirado da água sem pulso e com apneia após submersão. Como o afogamento
- Depois de colisão, paciente apresenta taquicardia, pele fria e confusão, mas mantém pressã
```

## Pedido 2: Títulos de crédito (`direito-empresarial.titulos-credito`, 420 tópicos de edital)

```
Matéria: direito-empresarial.titulos-credito (Títulos de crédito)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Teoria geral, fontes normativas e classificação da circulação": 29
- "2. Endosso, cessão, aval, letra de câmbio e nota promissória": 20
- "3. Cheque, duplicata, aceite, sustação e escrituração": 18
- "4. Cédulas especializadas, títulos imobiliários e letra de arrendamento mercantil": 14
- "5. Protesto, prescrição, cobrança e substituição de títulos": 19

Já existem (não repita):
- Uma nota contém valor, vencimento e assinaturas cambiais. Qual ideia exprime a literalidad
- Um portador analisa o endosso em letra de câmbio regida pela Lei Uniforme e pretende aplic
- Uma pessoa emite livremente documento privado ao portador sem existir lei especial autoriz
- Uma nota à ordem é regularmente endossada no verso e entregue ao adquirente. O que complet
- Um título nominativo é transferido por endosso que identifica o adquirente. Para que tal t
- Em título regido apenas pela regra geral do Código Civil, há endosso sem cláusula de garan
- Um avalista garante apenas metade do valor de cheque, fazendo constar expressamente essa l
- Dois documentos de mesmo valor apresentam estruturas distintas: o primeiro ordena pagament
- Uma pessoa emite cheque em 1º de março com data futura de 15 de abril combinada entre as p
- Foram emitidos dois cheques: um no mesmo lugar do pagamento e outro em local distinto no p
- Um emitente comunica contraordem ao banco antes do encerramento do prazo de apresentação d
- Um comprador recebeu mercadorias efetivamente entregues de acordo com qualidade, quantidad
- Uma duplicata cartular regularmente emitida foi extraviada pelo vendedor. Que documento a 
- Uma sociedade vendedora opta por emitir duplicata mediante escrituração em sistema eletrôn
- Um financiamento industrial documenta-se por dois instrumentos previstos no Decreto-Lei nº
- Uma sociedade de arrendamento mercantil emite Letra de Arrendamento Mercantil nominativa e
- Um título de dívida foi apresentado à serventia competente para documentar formalmente ina
- Um cheque da mesma praça foi emitido em data conhecida. Para identificar o início do prazo
- Após prescrever a ação executiva cambial de um cheque, o portador pretende reclamar enriqu
- Um cheque perdeu força executiva, mas permanece como prova escrita do crédito contra seu e
- Uma nota promissória sem força executiva venceu no dia indicado e o credor pretende ação m
- Uma pessoa possui título ao portador dilacerado, mas ainda plenamente identificável. Outra
- Um cheque nominativo à ordem foi endossado depois de expirar seu prazo legal de apresentaç
- Um credor de duplicata quer preservar o direito de regresso contra endossantes e respectiv
- Uma operação de crédito formalizada em cédula bancária prevê promessa de pagamento sem pen
- Se um documento não possuir requisito indispensável a ser reconhecido como título de crédi
- Uma sociedade privada pode criar título de crédito ao portador sem autorização legislativa
- Em título à ordem submetido ao regime geral do Código Civil, a transferência por endosso c
- A nota promissória depende de aceite por terceiro sacado para tornar seu emitente obrigado
- A inexistência de fundos disponíveis na conta do emitente no momento de apresentação não t
- É admissível emitir duplicata mercantil para documentar qualquer dívida pessoal de valor d
- A Lei nº 7.357/1985 admite converter cruzamento geral de cheque em especial, mas veda a tr
- O art. 914 do Código Civil estabelece que todo endossante de letra de câmbio regida pela L
- O aval de cheque pode garantir parte do seu valor, pois o art. 29 da Lei nº 7.357/1985 per
- O cheque emitido para pagamento no mesmo lugar da emissão deve ser apresentado em sessenta
- O comprador que recebe uma duplicata não à vista deve, no prazo de dez dias de sua apresen
- A Cédula de Crédito Bancário exige constituição de garantia real em todos os casos, porque
- A Letra de Arrendamento Mercantil é título nominativo e endossável, emitido pelas sociedad
- Para cheque emitido na mesma praça, o prazo de seis meses da execução cambiária previsto n
- O art. 61 da Lei do Cheque prevê ação por enriquecimento injusto em dois anos, contados da
- Um cheque prescrito para fins de execução não pode instruir ação monitória, já que a perda
- Em ação monitória contra o emitente, a Súmula 503 conta cinco anos do dia seguinte à emiss
- O portador de duplicata que pretende conservar regresso contra endossantes pode deixar de 
- Pelo art. 26 da Lei nº 9.492/1997, o cancelamento do protesto pode ser requerido por inter
- O art. 909 do Código Civil dispõe que pagamento feito antes de o devedor ter ciência de aç
```

## Pedido 3: Segurança e medicina do trabalho: CIPA, insalubridade e periculosidade (`direito-trabalho.seguranca-medicina-trabalho`, 420 tópicos de edital)

```
Matéria: direito-trabalho.seguranca-medicina-trabalho (Segurança e medicina do trabalho: CIPA, insalubridade e periculosidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema normativo, prevenção e meio ambiente do trabalho": 21
- "2. CIPA: representação, atribuições e garantia provisória": 20
- "3. EPI, higiene ocupacional e responsabilidade preventiva": 17
- "4. Insalubridade: enquadramento, graus, base de cálculo e perícia": 21
- "5. Periculosidade, opção entre adicionais e resolução de casos complexos": 21

Já existem (não repita):
- Uma empresa deixa de instruir os empregados sobre precauções necessárias e dificulta a fis
- Uma organização prepara inventário de riscos ocupacionais e plano de ação para gerenciar a
- Ao revisar seu regimento de prevenção, a empresa mantém a designação antiga da CIPA e igno
- Uma empresa constitui CIPA e precisa preencher vagas de representantes dos empregados. Qua
- Uma empregada foi eleita para a CIPA e teve candidatura registrada regularmente. Considera
- Uma indústria desconta do salário o custo de luvas e protetores adequados que precisa forn
- A empresa pretende comercializar e utilizar respiradores considerados EPI pelo Anexo I da 
- Uma exposição é oficialmente classificada como insalubre em grau médio, sem questão contro
- Um trabalhador possui salário-base de R$ 2.800,00 e gratificação eventual de R$ 500,00, am
- Em discussão judicial sobre condições insalubres, o juízo precisa da caracterização técnic
- Na CIPA de uma empresa, dois empregados foram eleitos e a direção designou seus próprios r
- Um trabalhador foi eleito suplente da representação dos empregados na CIPA e sofreu dispen
- Após distribuição de protetores auditivos com recibos assinados, a empresa invoca a entreg
- Uma equipe realiza a higienização diária de instalações sanitárias abertas a público de gr
- Ao calcular adicional de insalubridade, um julgador pretende substituir por conta própria 
- A unidade em que trabalhava um representante eleito da CIPA é efetivamente extinta durante
- Uma interdição regularmente determinada diante de grave e iminente risco impede o trabalho
- Uma perícia identifica agente nocivo no trabalho, mas a atividade não se enquadra na relaç
- Um empregado está sujeito, em condições regulamentares, a risco perigoso e a agente insalu
- Após alteração legislativa de 2023, um estudo avalia riscos de atropelamentos e colisões e
- Durante fiscalização, uma máquina apresenta grave e iminente risco de lesão, exigindo para
- Uma empresa de transporte coletivo usa combustível em tanques originais e suplementares ce
- Um trabalhador entra diversas vezes por semana em área de risco regulamentada, permanecend
- Um instrumento coletivo prevê adicional de periculosidade de 18% para trabalhador exposto 
- Um laudo comprova contato com determinada substância, mas não esclarece a correspondência 
- Em uma empresa com empregados regidos pela CLT, a Portaria MTb nº 3.214/1978 é referência 
- Uma organização que remunera adicional de risco pode dispensar as medidas de prevenção pre
- Na CIPA, representantes titulares e suplentes dos empregados são eleitos por escrutínio se
- Empregado escolhido diretamente pela direção para representar o empregador na CIPA adquire
- Em atividade enquadrada como perigosa, o percentual ordinário do adicional previsto no art
- O Programa de Gerenciamento de Riscos atual é regido principalmente pela NR-9, enquanto a 
- Em estabelecimento sujeito à NR-5, as atribuições preventivas da CIPA incluem a dimensão d
- Uma ficha de fornecimento assinada pelo empregado demonstra, por si só, neutralização de a
- A exposição insalubre pode ser eliminada ou neutralizada por medidas de conservação do amb
- Por iniciativa judicial, o salário-base contratual pode ser fixado livremente como referên
- Uma organização atualizou seu PGR após 26 de maio de 2026 e incluiu, na análise de riscos 
- O estabelecimento em que trabalhava um suplente eleito da CIPA foi efetivamente extinto. P
- Um EPI adquirido durante a validade de seu CA deve ser fornecido respeitando as condições 
- Um laudo tecnicamente fundamentado reconhece agente nocivo não listado como atividade insa
- Uma trabalhadora higieniza instalações sanitárias de uso coletivo de grande circulação e c
- Um trabalhador exposto a agentes oficialmente insalubres e a condição perigosa regulamenta
- A exposição regular e intermitente de um empregado a risco perigoso, em condições regulame
- A exceção do art. 193, § 5º, da CLT relativa a tanques de combustível certificados de veíc
- Suscitada em juízo a caracterização de insalubridade ou periculosidade, o art. 195, § 2º, 
- Durante interdição de máquina em razão de grave e iminente risco, os salários dos empregad
```

## Pedido 4: Ação penal (`processo-penal.acao-penal`, 420 tópicos de edital)

```
Matéria: processo-penal.acao-penal (Ação penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direito de ação, condições, princípios e peças acusatórias": 26
- "2. Ação pública condicionada, representação e requisição": 21
- "3. Ação privada exclusiva, personalíssima e subsidiária da pública": 18
- "4. Decadência, renúncia, perdão e perempção": 17
- "5. Acordo de não persecução penal, controle da acusação e relações com a esfera civil": 18

Já existem (não repita):
- Uma infração penal está sujeita à ação pública incondicionada. Com suporte probatório míni
- Uma pessoa ofendida quer formalizar representação por crime cuja ação pública depende dess
- A vítima ajuíza uma queixa por crime de ação privada exclusiva e pretende manter o process
- Antes de oferecer queixa por infração de iniciativa privada, a vítima declara validamente 
- O investigado confessou formal e circunstancialmente uma infração sem violência, com pena 
- A peça acusatória descreve fato criminoso e suas circunstâncias, identifica o acusado e in
- A vítima conhece a autoria de crime condicionado à representação, sem regra especial, e de
- A vítima pretende ajuizar queixa em crime de ação privada personalíssima do art. 236 do Có
- Uma vítima ajuizou queixa privada e, durante o processo, oferece perdão ao querelado, que 
- Ao avaliar ANPP para infração sem violência ou grave ameaça, o membro do Ministério Públic
- Foi oferecida denúncia sem a representação exigida expressamente para aquela infração, emb
- Em julho de 2026, a ofendida toma conhecimento da autoria de infração que exige representa
- Passado o prazo ordinário para denúncia em ação pública, não houve apresentação da peça ne
- Em ação privada exclusiva com dois autores conhecidos e justa causa para ambos, a vítima r
- Uma infração sem violência tem pena mínima de dois anos e não se sujeita à transação penal
- O titular da ação privada exclusiva oferece queixa com procuração especial válida. Os auto
- Na hipótese de ação condicionada em contexto doméstico abrangido pelo art. 16 da Lei 11.34
- Em ação penal pública comum com investigado solto, o titular não promove denúncia nem ato 
- Em ação exclusivamente privada, falece o querelante. Nenhum dos sucessores habilitados com
- Processo iniciado antes da Lei 13.964/2019 ainda não transitou em julgado e pode preencher
- A peça acusatória identifica claramente os fatos, autor e classificação, mas os documentos
- Em crime complexo, a lei descreve como elemento integrante um fato que, isoladamente, tamb
- Uma queixa exclusivamente privada é ajuizada contra um dos dois coautores conhecidos, have
- Em processo de queixa exclusiva, o ofendido concede perdão a dois querelados; um aceita ex
- Investigado sem condenação anterior preenche a faixa de pena e não praticou violência físi
- Uma denúncia de ação pública pode ser rejeitada por falta de justa causa quando não há sup
- Em crime de ação pública condicionada, o ofendido torna-se autor da denúncia quando entreg
- Na ação penal de iniciativa privada exclusiva, o ofendido ou pessoa com legitimidade previ
- Quando a vítima recebe reparação civil pelo dano causado em crime de iniciativa privada, o
- Cumpridas integralmente as condições de acordo de não persecução penal homologado, o juiz 
- Depois de oferecer denúncia de ação pública, o Ministério Público pode desistir livremente
- Nos crimes praticados em âmbito de violência doméstica e familiar contra mulher em que a l
- Por força da regra geral de sucessão do CPP, art. 31, os descendentes podem apresentar que
- Oferecido perdão em autos de ação privada, o querelado é intimado para se manifestar em tr
- Para aferir o limite de pena mínima inferior a quatro anos para ANPP, o CPP, art. 28-A, §1
- Segundo a atualização do art. 16 da Lei 11.340/2006 feita em 2026, a audiência de retrataç
- Na ação penal pública em que o titular deixa transcorrer o prazo para denúncia, a queixa s
- A renúncia expressa ao direito de queixa feita antes do ajuizamento, em relação a um dos a
- Por força da Lei 15.438/2026, toda lesão corporal em violência doméstica contra mulher tor
- Se fatos que constituem elementos do tipo de um crime complexo também constituem delitos e
- Na ação penal privada subsidiária da pública, a negligência posterior do querelante produz
- A Súmula 714 do STF reconhece legitimidade concorrente por queixa do ofendido ou denúncia 
- Conforme o HC 185.913/DF julgado em 2024, acordo de não persecução penal é incompatível co
- Uma sentença penal condenatória transitada em julgado pode fundamentar execução civil de r
- Diante de ANPP integralmente descumprido, o CPP, art. 28-A, §13, obriga declarar extinta a
```

## Pedido 5: Contratos em espécie: compra e venda, doação, locação, empréstimo, prestação de serviço, mandato e fiança (`direito-civil.contratos-especie`, 419 tópicos de edital)

```
Matéria: direito-civil.contratos-especie (Contratos em espécie: compra e venda, doação, locação, empréstimo, prestação de serviço, mandato e fiança)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Compra e venda, permuta e cláusulas especiais": 24
- "2. Doação e locação de coisas": 23
- "3. Comodato, mútuo e prestação de serviço civil": 24
- "4. Mandato, procuração e poderes de representação": 17
- "5. Fiança, benefício de ordem, exoneração e jurisprudência": 12

Já existem (não repita):
- Duas pessoas ajustam a venda de um equipamento individualizado por preço certo em dinheiro
- Num contrato de compra e venda sem cláusula específica sobre despesas, comprador e vendedo
- Uma pessoa vende imóvel a um dos filhos sem consentimento expresso dos demais descendentes
- Um imóvel foi vendido com área contratada de 1.000 m² e medida posterior de 960 m². O comp
- Na venda de imóvel com cláusula de retrovenda, o alienante deseja reservar o direito de re
- Alguém entrega gratuitamente a outra pessoa relógio usado de pequeno valor, com transmissã
- Vencida uma locação civil por tempo certo, o locatário mantém a coisa e o locador não ofer
- Uma pessoa recebe gratuitamente uma bicicleta individualizada para uso e devolução da mesm
- Contrato contém cláusula de preferência para que antigo vendedor readquira o imóvel se o c
- Uma máquina é vendida com reserva de domínio; foi entregue ao comprador, ainda sem quitaçã
- Uma pessoa doa valor superior à parcela patrimonial de que poderia dispor em testamento no
- O doador descobre que o donatário praticou grave injúria contra ele e só depois identifica
- Imóvel urbano locado por prazo determinado foi vendido. O contrato possuía cláusula de vig
- Em 2026, uma pessoa empresta dinheiro para finalidade econômica sem estipular a taxa de ju
- Duas pessoas celebram prestação de serviço civil por prazo indeterminado, sem prazo inferí
- Uma procuração foi redigida com poderes gerais para administrar um imóvel, sem menção a al
- Um condômino vende fração de bem indivisível a estranho sem informar coproprietário intere
- Uma doadora estabelece que o bem retornará ao patrimônio de uma sobrinha caso o donatário 
- Prestador de serviço civil contratado até data certa é dispensado pelo tomador, sem justa 
- Em procuração 'em causa própria', mandante falece e herdeiro afirma que a morte extinguiu 
- A procuração proíbe expressamente substabelecimento; mesmo assim o mandatário transfere po
- Fiador acionado pelo credor requer benefício de ordem, mas a fiança escrita o qualifica ex
- Duas pessoas prestam fiança conjuntamente para o mesmo débito, sem reservar benefício de d
- Uma pessoa casada em regime que exige outorga presta fiança sem autorização do outro cônju
- Um fiador de obrigação civil sem prazo notifica sua exoneração. Outro, de locação urbana p
- Na venda de coisa móvel, antes da tradição os riscos ordinários de perecimento da coisa co
- A compra e venda entre cônjuges é proibida pelo Código Civil mesmo quando recai apenas sob
- As pretensões específicas decorrentes da divergência de área de imóvel prevista no art. 50
- O direito do vendedor à retrovenda de imóvel é personalíssimo, de modo que não pode ser ce
- Se o doador fixa prazo e o donatário, ciente, não responde, o silêncio pode importar aceit
- Em doação com cláusula de reversão, é válida a previsão de retorno do bem ao patrimônio de
- Na locação de coisas regida pelo Código Civil, ressalvada disposição contrária, benfeitori
- O comodato é modalidade onerosa de empréstimo de coisas fungíveis, pela qual o comodatário
- Se um mútuo de dinheiro não prevê prazo de restituição, aplica-se a regra supletiva de ao 
- Um contrato civil de prestação de serviço por seis anos é compatível com o art. 598 sempre
- Quem alicia prestador vinculado por contrato escrito a outra pessoa pode dever, nos termos
- Uma procuração com poderes gerais de administração habilita o mandatário a hipotecar e ven
- A cláusula comum de irrevogabilidade do mandato pode ensejar perdas e danos em caso de rev
- A fiança pode ser prestada verbalmente se houver duas testemunhas e, nesse caso, comporta 
- Para invocar benefício de ordem, fiador demandado deve fazê-lo até a contestação e nomear 
- Quando duas pessoas prestam fiança conjunta do mesmo débito, o Código Civil impõe benefíci
- Em contrato de locação, fiador não anui ao aditamento que acrescenta obrigações. A Súmula 
- Pela Súmula 268 do STJ, o fiador que não participou da relação processual na ação de despe
- Conforme a Súmula 549 do STJ, a proteção geral do bem de família comporta exceção que perm
- A Súmula 656 do STJ considera inválida a cláusula de prorrogação automática da fiança na r
```

## Pedido 6: Sanções administrativas e crimes em licitações (`direito-administrativo.sancoes-crimes-licitacoes`, 419 tópicos de edital)

```
Matéria: direito-administrativo.sancoes-crimes-licitacoes (Sanções administrativas e crimes em licitações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Infrações administrativas e tipificação do art. 155": 24
- "2. Sanções, dosimetria e processo de responsabilização": 24
- "3. Efeitos, recursos, reabilitação e controles das contratações": 11
- "4. Crimes em licitações: tipos atuais dos arts. 337-E a 337-P": 26
- "5. Direito intertemporal, Lei 8.666/1993 e integração das respostas": 15

Já existem (não repita):
- Uma empresa deixou de executar parte do objeto contratual, sem demonstração de grave dano 
- Diante de simples inexecução parcial do contrato enquadrada no art. 155, I, a Administraçã
- A empresa foi sancionada pelo art. 156, § 4º, e o contrato é executado em um município. Qu
- A declaração de inidoneidade foi corretamente aplicada a empresa responsável por documenta
- Em um processo de responsabilização foi aplicada multa administrativa nos termos do art. 1
- Para aplicar somente multa administrativa a um contratado, a autoridade deve facultar defe
- Uma sanção administrativa foi aplicada e deve ser divulgada nos cadastros nacionais previs
- Um participante interpõe recurso contra impedimento de licitar e contratar. Nos termos do 
- O responsável por uma contratação direta admite celebração fora das hipóteses legalmente p
- Para aplicação de impedimento, instaurou-se processo por comissão com um servidor estável 
- A Administração tomou conhecimento documentado de uma infração contratual e analisa a pres
- Foi aplicada declaração de inidoneidade e a pessoa sancionada deseja contestá-la administr
- Uma empresa foi impedida de licitar há 14 meses, reparou integralmente o dano, pagou multa
- Em fiscalização, foi detectada irregularidade com dano e possível ilícito penal; é necessá
- Um licitante recebe vantagem econômica para desistir da disputa e efetivamente deixa de pa
- Pessoa declarada inidônea é apenas admitida a participar de licitação, sem assinatura cont
- Uma denúncia se refere ao afastamento de licitante por grave ameaça. Comparando o regime p
- A comissão constatou conduta que simultaneamente configura infração da Lei nº 14.133/2021 
- Uma pessoa jurídica foi utilizada abusivamente para ocultar fraude licitatória e pretende-
- Um órgão decide pagar fatura de contrato fora da ordem cronológica de exigibilidade, consc
- Um projetista altera sondagens necessárias à definição do preço da contratação, em relevan
- Uma empresa é punida administrativamente por fraude documental e a decisão indica multa de
- A descrição histórica do art. 97 da Lei nº 8.666/1993 reunia admissão em licitação e contr
- Um fato que pode constituir crime de contratação direta ilegal ocorreu em fevereiro de 202
- Uma prova compara a revogação penal dos arts. 89 a 108 da Lei nº 8.666/1993 com a revogaçã
- Para a infração de simples inexecução parcial do art. 155, I, a Lei nº 14.133/2021 admite 
- O impedimento de licitar e contratar previsto no art. 156, § 4º, alcança a Administração d
- A multa administrativa do art. 156, § 3º, pode ser aplicada por qualquer das infrações do 
- Para aplicação de declaração de inidoneidade, o processo de responsabilização pode ser con
- O art. 162 da Lei nº 14.133/2021 prevê multa de mora pelo atraso injustificado na execução
- O crime de contratação direta ilegal do art. 337-E do Código Penal tem pena de detenção de
- O processo de responsabilização do art. 158 assegura defesa escrita em quinze dias úteis, 
- A instauração do processo de responsabilização do art. 158 suspende a prescrição administr
- Uma empresa que apresentou declaração falsa em licitação incorre em infração do art. 155, 
- O recurso administrativo contra impedimento de licitar e contratar, previsto no art. 166, 
- Os recursos e pedidos de reconsideração previstos nos arts. 166 e 167 têm efeito suspensiv
- No art. 337-M do Código Penal, admitir pessoa declarada inidônea à licitação e celebrar co
- O art. 170, § 4º, da Lei nº 14.133/2021 admite representação aos órgãos de controle por qu
- Uma empresa sancionada possui sócio minoritário sem poderes de gestão. Pela regra do art. 
- Conduta que é simultaneamente infração licitatória e ato lesivo da Lei nº 12.846/2013 deve
- Após três anos da declaração de inidoneidade, a reabilitação decorre automaticamente do de
- O art. 337-O, § 2º, do Código Penal prevê aplicação em dobro da pena do caput quando a omi
- Os crimes originalmente descritos nos arts. 89 a 98 da Lei nº 8.666/1993 permaneceram em v
- No modelo do art. 169 da Lei nº 14.133/2021, o assessoramento jurídico e a unidade de cont
- A multa criminal dos crimes licitatórios obedece ao limite administrativo máximo de 30% do
```

## Pedido 7: Tutela coletiva: ação civil pública, inquérito civil e compromisso de ajustamento de conduta (`processo-civil.tutela-coletiva-acp-inquerito-civil`, 416 tópicos de edital)

```
Matéria: processo-civil.tutela-coletiva-acp-inquerito-civil (Tutela coletiva: ação civil pública, inquérito civil e compromisso de ajustamento de conduta)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Interesses coletivos, cabimento, competência e legitimidade": 25
- "2. Inquérito civil e procedimento preparatório": 25
- "3. Compromisso de ajustamento de conduta": 25
- "4. Processo, sentença, coisa julgada e execução": 25

Já existem (não repita):
- Os interesses ou direitos difusos são transindividuais, de natureza indivisível, e têm com
- Os alunos de uma mesma escola particular questionam, em conjunto, o aumento ilegal das men
- Os direitos individuais homogêneos são indivisíveis e pertencem a pessoas indeterminadas, 
- Segundo o parágrafo único do art. 1º da Lei 7.347/1985, NÃO é cabível ação civil pública p
- A ação civil pública pode ter por objeto a condenação em dinheiro ou o cumprimento de obri
- Pela Lei 7.347/1985, a ação civil pública deve ser proposta:
- A Defensoria Pública tem legitimidade para propor ação civil pública, ao lado do Ministéri
- Para ajuizar ação civil pública, a associação deve, cumulativamente, estar constituída nos
- O requisito da pré-constituição da associação autora é absoluto e não pode ser dispensado 
- Em caso de desistência infundada ou abandono da ação civil pública por associação legitima
- Quando não intervier no processo como parte, o Ministério Público atuará obrigatoriamente 
- Segundo o art. 6º da Lei 7.347/1985, provocar a iniciativa do Ministério Público com infor
- O inquérito civil é procedimento de natureza unilateral e facultativa, e não constitui con
- Quanto à titularidade do inquérito civil, é correto afirmar que ele pode ser instaurado:
- A Resolução CNMP 23/2007 veda que o conhecimento de fatos por manifestação anônima leve à 
- O procedimento preparatório, instaurado para complementar informações antes do inquérito c
- O inquérito civil deve ser concluído no prazo de um ano, prorrogável pelo mesmo prazo quan
- Indeferido o pedido de instauração de inquérito civil, o representante pode interpor recur
- Ao requisitar de organismo público ou particular certidões, informações, exames ou perícia
- O art. 10 da Lei 7.347/1985 tipifica como crime, punido com reclusão de 1 a 3 anos e multa
- No inquérito civil vigora o sigilo como regra, de modo que a publicidade dos atos depende 
- Promovido o arquivamento do inquérito civil, os autos devem ser remetidos ao Conselho Supe
- Até a sessão do Conselho Superior do Ministério Público que examinará a promoção de arquiv
- Se o Conselho Superior do Ministério Público deixar de homologar a promoção de arquivament
- O desarquivamento do inquérito civil, diante de novas provas ou para investigar fato novo 
- Não oficiará nos autos do inquérito civil, do procedimento preparatório ou da ação civil p
- Os prazos dos procedimentos em trâmite no âmbito do inquérito civil, ressalvadas as exceçõ
- Pela Lei 7.347/1985, podem tomar compromisso de ajustamento de conduta dos interessados:
- O compromisso de ajustamento de conduta tem eficácia de título executivo judicial, razão p
- No compromisso de ajustamento de conduta, o Ministério Público pode fazer concessões que i
- Segundo a Resolução CNMP 179/2017, é correto afirmar sobre o compromisso de ajustamento de
- O compromisso de ajustamento de conduta pode prever medidas provisórias ou parciais; nesse
- Quando não for possível a reconstituição específica do bem lesado, as indenizações previst
- O órgão superior do Ministério Público deve dar publicidade ao extrato do compromisso de a
- Na ação civil pública, a multa cominada liminarmente ao réu:
- A requerimento de pessoa jurídica de direito público interessada, para evitar grave lesão 
- Nas ações regidas pela Lei 7.347/1985, os recursos têm sempre efeito suspensivo, por força
- Havendo condenação em dinheiro na ação civil pública, a indenização pelo dano causado reve
- Decorridos 60 dias do trânsito em julgado da sentença condenatória sem que a associação au
- Julgado improcedente o pedido de ação civil pública por insuficiência de provas, qualquer 
- Sobre a restrição dos efeitos da sentença da ação civil pública "aos limites da competênci
- Pelo art. 103 do CDC, na ação coletiva que versa sobre direitos individuais homogêneos, a 
- Nas ações civis públicas não há adiantamento de custas, emolumentos e honorários periciais
- Em caso de litigância de má-fé na ação civil pública, a associação autora e os diretores r
- O STF considera imprescritível a pretensão de reparação civil de dano ambiental, ainda que
- Na ação civil pública que tenha por objeto obrigação de fazer ou não fazer, o juiz determi
- Considerando o microssistema de tutela coletiva, assinale a afirmativa correta.
```

## Pedido 8: Estrutura conceitual, DMPL e pronunciamentos do CPC (`contabilidade.estrutura-conceitual-cpc`, 415 tópicos de edital)

```
Matéria: contabilidade.estrutura-conceitual-cpc (Estrutura conceitual, DMPL e pronunciamentos do CPC)
Capítulos e quantas questões quero em cada um (total 100):
- "1. CPC, CFC, princípios e convergência normativa": 29
- "2. CPC 00 (R2): objetivo, usuários e qualidades da informação": 15
- "3. Elementos, reconhecimento, mensuração e manutenção de capital": 15
- "4. DMPL, DLPA e resultado abrangente": 25
- "5. Mapa dos pronunciamentos e julgamento normativo": 16

Já existem (não repita):
- Uma empresa prepara relatórios financeiros gerais para informar possíveis financiadores de
- Uma entidade divulga informação útil por influenciar previsões e confirmar avaliações ante
- Uma empresa entrega relatório suficientemente completo, neutro e produzido por processo de
- Uma sociedade recebe integralização de capital de 100 mil unidades monetárias dos propriet
- Uma companhia decide transferir 30 mil unidades de lucros acumulados para reserva de lucro
- Um enunciado exige a antiga lista de princípios da Resolução CFC nº 750/1993. Qual alterna
- Uma entidade precisa distinguir o documento contábil técnico geral aplicável a entidades e
- Uma entidade precisa elaborar a demonstração que acompanha capital, reservas e demais cont
- Uma equipe busca no catálogo do CPC a norma cujo objeto central é a apresentação das demon
- Em relatório geral, uma questão judicial tem valor baixo em relação aos ativos, mas envolv
- Uma organização recebe equipamento para uso, mantém direito contratual de direcionar seus 
- Uma empresa compra bem por 120 unidades, mas o preço observável em transação ordenada entr
- O patrimônio líquido inicial era 250. Houve lucro 70, aporte efetivo 30, dividendo declara
- Uma companhia constata que despesa material de exercício anterior não foi registrada por e
- Uma entidade publica Demonstração de Lucros ou Prejuízos Acumulados e decide elaborar e pu
- Uma entidade tem processo complexo de estimativa, com premissas explicitadas e cálculo cor
- Em 2026, uma empresa precisa distinguir o documento voltado a instrumentos financeiros daq
- Uma diretoria afirma que a Estrutura Conceitual autoriza não aplicar regra clara de um pro
- Uma empresa pode deixar de existir nos próximos meses por decisão irrevogável de liquidaçã
- Uma empresa teve PL inicial de 500, lucro líquido de 80, outros resultados abrangentes neg
- No modelo conceitual, determinada entidade enfrenta saída econômica pouco provável, mas já
- No CPC 00 (R2), duas bases de mensuração são comparadas: uma usa premissas de participante
- Uma companhia altera política contábil com efeitos retrospectivos sobre reserva e lucros a
- Dois profissionais confundem documentos: um afirma que ITG e ICPC são nomes intercambiávei
- Uma companhia aberta elabora demonstrações para período iniciado em janeiro de 2026 e dese
- Uma empresa aplica informações contábeis de competência a uma venda entregue em dezembro e
- A antiga Resolução CFC nº 750/1993 continua vigente em 2026 como a principal estrutura con
- No CPC 00 (R2), relevância e representação fidedigna são características fundamentais, enq
- Um aumento do patrimônio líquido por aporte dos titulares de participação societária atend
- Numa companhia, a transferência de valores de reserva de lucros para capital social altera
- O CPC 47 trata de instrumentos financeiros e o CPC 48 de receita de contratos com clientes
- Uma entidade pode divulgar informação material cujo montante seja reduzido, se a natureza 
- A prudência do CPC 00 (R2) exige subavaliação deliberada de ativos e superavaliação de pas
- Uma obrigação presente de transferir recurso econômico por fato passado pode satisfazer co
- Para o CPC 00 (R2), o valor em uso de um ativo deve ser obrigatoriamente igual ao valor ju
- De acordo com a Lei nº 6.404/1976, art. 186, §2º, o conteúdo da DLPA pode ser incluído na 
- Uma companhia reconheceu lucro líquido de 40 e ganho de outros resultados abrangentes de 1
- As Leis nº 11.638/2007 e nº 11.941/2009 modificaram a Lei nº 6.404/1976, integrando o movi
- Quando um pronunciamento técnico específico determinar critério aplicável a uma transação,
- Uma mudança de estimativa decorrente de informação nova é conceitualmente diferente da des
- A demonstração do resultado abrangente pode ser apresentada unicamente como um bloco dentr
- Na manutenção de capital financeiro, a comparação dos patrimônios inicial e final para det
- O CPC 00 (R2) exige reconhecimento automático de todo direito econômico possível, bastando
- Uma DMPL pode apresentar reapresentação retrospectiva por erro material conforme CPC 23 se
- A publicação das Resoluções CVM nº 237 e nº 238 em dezembro de 2025 tornou o CPC 51 obriga
```

## Pedido 9: Termodinâmica, mecânica dos fluidos e transferência de calor (`engenharia-mecanica.termodinamica-fluidos-calor`, 413 tópicos de edital)

```
Matéria: engenharia-mecanica.termodinamica-fluidos-calor (Termodinâmica, mecânica dos fluidos e transferência de calor)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Propriedades e leis da termodinâmica": 19
- "2. Ciclos de potência, motores e refrigeração": 25
- "3. Bombas, ventiladores, compressores e turbinas: operação e potência": 19
- "4. Escoamentos, tubulações, semelhança e bocais compressíveis": 19
- "5. Condução, convecção, radiação e trocadores de calor": 18

Já existem (não repita):
- Em um reservatório rígido com gás em equilíbrio, o projetista registra massa, temperatura,
- Um cilindro fechado recebe 500 kJ de calor e produz 180 kJ de trabalho no mesmo processo, 
- Uma máquina térmica reversível ideal opera entre reservatórios a 600 K e 300 K. Qual é o m
- Ao comparar ciclos de ar-padrão de dois motores alternativos, a equipe analisa o modelo id
- Um sistema frigorífico remove 18 kW de um recinto e opera com COP de refrigeração igual a 
- Uma bomba eleva água com vazão de 0,02 m³/s e altura manométrica de 25 m. Considere ρ=1000
- Uma fábrica compara bomba centrífuga com bomba de deslocamento positivo sob variação de re
- Em tubo circular, água escoa com ρ=1000 kg/m³, velocidade média de 1 m/s, diâmetro de 0,02
- O gás atravessa bocal convergente até a garganta atingir Mach 1. Mantendo pressão e temper
- Uma placa plana de 0,1 m de espessura tem k=0,5 W/(m K), área de 10 m² e diferença térmica
- Vapor úmido está em mistura saturada, com qualidade x=0,40, entalpia do líquido saturado h
- Em compressor do ciclo Brayton, gás ideal entra a 300 K e sai a 400 K. São dados cp=1 kJ/(
- No ciclo Rankine ideal, o trabalho específico entregue pela turbina é 900 kJ/kg, o consumi
- Uma bomba de rotação variável fornece 20 m de altura e absorve 5 kW a 1000 rpm. Nas condiç
- Água percorre tubo com L=50 m, D=0,1 m, velocidade de 2 m/s e fator de Darcy f=0,02. Acres
- Um bocal convergente trabalha com gás ideal de γ=1,4 e pressão total de entrada igual a 50
- Uma superfície de 4 m² está a 100 °C e troca calor com ar a 80 °C. O coeficiente convectiv
- Um trocador em contracorrente apresenta diferenças terminais de 60 K e 20 K. Admitindo U=2
- Uma turbina a vapor adiabática recebe fluido com h1=3500 kJ/kg. Na pressão de saída, a ent
- Uma máquina cíclica recebe 600 kJ de uma fonte a 600 K, produz 200 kJ de trabalho e rejeit
- Em um rotor de turbina, a vazão é 5 kg/s e os produtos rVθ medidos na entrada e saída vale
- Um protótipo de canal com superfície livre tem comprimento característico 25 vezes o do mo
- Um trecho de tubulação metálica de 30 m é aquecido em 100 K. Com coeficiente de dilatação 
- Em trocador monofásico, a menor capacidade térmica da corrente é Cmin=2 kW/K, e as tempera
- Uma superfície cinzenta com emissividade 0,5 tem as mesmas temperaturas e área de uma supe
- Dois corpos que estão, separadamente, em equilíbrio térmico com um terceiro corpo também e
- Em mistura líquido-vapor saturada de substância pura simples compressível, pressão e tempe
- Em ciclo de compressão de vapor, o evaporador absorve calor do meio refrigerado e o conden
- Um refrigerador que absorve 12 kW e consome 3 kW tem COP de 0,25, pois o desempenho frigor
- Em tubo de seção circular com água incompressível e vazão permanente, reduzir pela metade 
- Em sucção de bomba, a ocorrência de NPSH disponível inferior ao requerido constitui condiç
- Numa turbina a gás de ciclo Brayton, a potência líquida de eixo é a potência de expansão n
- Para escoamento laminar plenamente desenvolvido em tubo circular, o fator de atrito de Fan
- Para ciclo Otto de ar-padrão com razão de calores específicos constante, elevar a razão de
- Para um fluido newtoniano em temperatura e pressão fixas, a viscosidade dinâmica deve nece
- Mantidas temperaturas terminais fisicamente compatíveis, trocador em contracorrente pode a
- Numa parede composta com camadas planas em série e convecção nas duas faces, é correto som
- Em bomba centrífuga geometricamente idêntica, com eficiência aproximadamente constante e c
- Em escoamento supersônico ideal num trecho divergente de bocal, o aumento gradual da área 
- Em estágio idealizado de turbina a vapor de impulso, a principal expansão ocorre nos bocai
- Ao elevar a temperatura do líquido de sucção, mantidas pressão absoluta na sucção e demais
- Para sistema fechado que passa por processo adiabático e reversível, a entropia permanece 
- Em ciclo Rankine, a regeneração por aquecimento da água de alimentação e o reaquecimento d
- No aquecimento transiente de um pequeno sólido, Bi=hLc/ks suficientemente baixo, usualment
- Em curva de 90 graus com escoamento permanente, as forças de reação dos suportes podem ser
```

## Pedido 10: Mudança do clima, energia e pagamento por serviços ambientais (`direito-ambiental.mudanca-clima-servicos-ambientais`, 412 tópicos de edital)

```
Matéria: direito-ambiental.mudanca-clima-servicos-ambientais (Mudança do clima, energia e pagamento por serviços ambientais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da mudança do clima e governança internacional": 14
- "2. Política Nacional sobre Mudança do Clima e seus instrumentos": 33
- "3. Mercado de carbono e política energética": 15
- "4. Pagamento por serviços ambientais": 13
- "5. Política Nacional de Resíduos Sólidos e integração ambiental": 25

Já existem (não repita):
- Uma cidade instala bombas e canais para diminuir perdas humanas e materiais durante enchen
- Ao elaborar um plano climático, uma equipe consulta o objetivo de temperatura do Acordo de
- Um parecer precisa localizar os princípios da Política Nacional sobre Mudança do Clima, in
- Uma pesquisa revisita o compromisso nacional voluntário previsto originalmente no art. 12 
- Uma empresa propõe energia eólica e afirma que a natureza renovável dispensa qualquer cons
- Moradores recuperam uma mata ciliar para favorecer a qualidade da água oferecida naturalme
- Uma cooperativa organiza o tratamento de resíduos sólidos urbanos e precisa observar a ord
- Após utilizar um equipamento eletrônico, o consumidor busca devolver o produto à cadeia de
- Diante de risco sério de dano climático com incerteza científica relevante sobre sua magni
- Uma entidade busca financiar adaptação a secas e avalia modalidades do Fundo Nacional sobr
- Uma indústria solicita informação sobre a lei que organizou o ambiente regulado de comérci
- Uma empresa compara uma Cota Brasileira de Emissões com um Certificado de Redução ou Remoç
- Um regulador apresenta os patamares do art. 30 da Lei nº 15.042/2024 antes de definir obri
- Uma instalação possui emissão de 12.000 tCO2e em um ano. Consideradas a implantação regula
- Uma associação pretende remunerar pessoas que restauram uma bacia hidrográfica com equipam
- Uma pessoa vende imóvel com contrato de PSA que obriga restaurar a vegetação nativa, com c
- Um município monta coleta seletiva baseada na separação mínima descrita no Decreto nº 10.9
- Dois países cooperam para transferir resultados de mitigação a fim de cumprir compromissos
- Uma instalação reporta exatamente 25.000 tCO2e em determinado ano, em atividade com metodo
- Um projeto pretende apoiar comunidade que conserva vegetação e estoca carbono, por ação co
- Um município assume, por termo firmado com o setor empresarial, etapas operacionais de log
- Um proprietário possui APP conservada, mas a área objeto do pretendido PSA está embargada 
- Município com 18.000 habitantes pretende adotar conteúdo simplificado em seu plano de resí
- Um estudo de transporte precisa seguir a transição metodológica da análise de ciclo de vid
- Um município com 70.000 habitantes no Censo 2010 possuía plano de resíduos e mecanismo de 
- Uma fábrica substitui parte do consumo de combustível fóssil por eletricidade de baixo car
- Ao estudar os acordos internacionais, um grupo conclui que o Protocolo de Quioto estabelec
- Na aplicação da PNMC, uma autoridade considera a participação cidadã e adota medidas climá
- Ao solicitar recursos do Fundo Nacional sobre Mudança do Clima para um projeto de adaptaçã
- Uma comunidade pactua prestação de melhorias sociais em troca da recuperação de uma área d
- Após a coleta seletiva, o gestor de resíduos pode encaminhar diretamente a aterro sanitári
- Em sistema de retorno pós-consumo de equipamentos eletrônicos, a cadeia de fabricantes, im
- Uma empresa publica inventário de gases de efeito estufa e anuncia que o documento, por si
- Um plano climático prevê proteção contra secas e, simultaneamente, inventários de emissões
- Uma indústria que emite exatamente 10.000 tCO2e ao ano já ultrapassou o primeiro limite do
- Um proprietário que conserva APP regular poderá ser considerado elegível a pagamento por s
- Ao revisar o plano municipal de gestão integrada de resíduos sólidos, o gestor fixa interv
- Um município mantém coleta seletiva de resíduos secos e orgânicos, enquanto empresas organ
- Dois países pretendem abater o mesmo resultado de redução de emissões em suas contribuiçõe
- Uma instalação que emite 26.000 tCO2e ao ano, enquadrada em atividade com metodologia cons
- Ao analisar uma usina de geração elétrica renovável, um parecer considera que, pela Lei nº
- Um contrato de PSA relativo à recuperação de vegetação nativa em imóvel particular pode ge
- Pelo conceito legal de rejeito, um município pode classificar antecipadamente como rejeito
- Um projeto de recuperação energética de resíduos sólidos urbanos pode ser admitido pela PN
- Uma entidade remunera a conservação de vegetação por contrato de PSA e sustenta que o cont
```

## Pedido 11: Responsabilidade tributária (`direito-tributario.responsabilidade-tributaria`, 411 tópicos de edital)

```
Matéria: direito-tributario.responsabilidade-tributaria (Responsabilidade tributária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sujeição passiva, solidariedade e substituição tributária": 17
- "2. Responsabilidade dos sucessores e operações estaduais e municipais": 21
- "3. Responsabilidade de terceiros e redirecionamento da execução fiscal": 25
- "4. Infrações tributárias, multas e denúncia espontânea": 25
- "5. Casos integrados, precedentes e técnica de resolução": 12

Já existem (não repita):
- Uma empresa é contribuinte de imposto pelo fato de realizar a operação tributada. A lei de
- Duas pessoas são solidariamente obrigadas por tributo porque compartilham interesse jurídi
- Em contrato de compra e venda, as partes estipulam que o comprador pagará todos os tributo
- A legislação estadual encarrega o fabricante de recolher ICMS relativo à venda futura pres
- Um imóvel é adquirido por arrematação em hasta pública, existindo crédito tributário imobi
- Uma pessoa compra um restaurante com empregados, clientela e instalações, mantendo sua exp
- O inventariante pratica omissão relevante em matéria tributária do espólio, e não é possív
- Um diretor celebra operação tributariamente irregular com excesso de poderes, gerando créd
- Antes de qualquer procedimento fiscal relacionado, um contribuinte revela infração desconh
- Na substituição do ICMS para frente, a base presumida de revenda foi R$ 1.000, mas a opera
- Uma pessoa compra estabelecimento comercial, prossegue a atividade e descobre que o aliena
- Uma pessoa recebe herança de R$ 30.000, e o falecido deixou obrigação tributária anterior 
- Após incorporação de sociedade, verifica-se multa tributária punitiva relacionada a fato o
- Uma sociedade deixou de funcionar no endereço fiscal declarado sem comunicar sua mudança a
- Uma empresa deixa de pagar imposto no vencimento, mas não se comprovam atos irregulares pe
- Uma pessoa passou a administrar empresa em 2024; o imposto inadimplido nasceu em 2022, e a
- Município pretende responsabilizar uma pessoa que tomou serviço pelo ISS de prestador dist
- Uma unidade produtiva isolada é adquirida em alienação judicial de recuperação de empresa 
- Uma pessoa administrava empresa quando surgiu tributo, mas se retirou regularmente dois an
- Uma lei posterior reduz de 30% para 10% multa por infração tributária, e o ato ainda não f
- A execução fiscal pretende alcançar administrador com base em conduta diretamente descrita
- Após comprovação de fraude, foi aplicada multa tributária qualificada sem reincidência e s
- A legislação de ICMS atribui a contribuinte situado depois da operação agrícola o recolhim
- Uma infração é definida em lei como dependente de dolo específico de empregado contra seu 
- Em uma venda de imóvel existe IPTU anterior e incidência de ITBI pela transmissão; já outr
- Uma pessoa sem relação direta com o fato gerador pode ser responsável pelo crédito tributá
- Havendo solidariedade por interesse comum no fato gerador, o art. 124 do CTN exige cobrar 
- Quando a lei atribui a um agente posterior recolher ICMS relativo a operação antecedente d
- O comprador de estabelecimento comercial que prossegue sua exploração fica livre de tribut
- O art. 134 do CTN relaciona a responsabilidade de terceiros listados à impossibilidade de 
- A simples falta de pagamento de tributo devido pela sociedade torna pessoalmente responsáv
- A compra privada de imóvel pode transferir ao adquirente créditos anteriores de IPTU, segu
- Uma pessoa recebe imóvel em arrematação judicial e, de acordo com o art. 130, parágrafo ún
- Na substituição progressiva de ICMS, a operação real com base inferior à presumida pode ge
- O alienante de estabelecimento que continua explorando atividade econômica leva o adquiren
- O CTN 129 admite que o crédito seja constituído depois da incorporação, se decorrer de obr
- O ISS é devido pelo tomador de qualquer serviço prestado no território municipal, ainda qu
- A Súmula 435 do STJ autoriza presumir dissolução irregular quando uma empresa deixa de fun
- Uma pessoa administrou uma empresa na data do fato gerador do tributo, mas se retirou regu
- Uma pessoa só passou a administrar a empresa após o fato gerador de tributo não pago e ain
- A Súmula 392 do STJ permite substituir a CDA para mudar livremente a pessoa do executado, 
- Pelo art. 136 do CTN, salvo disposição de lei em contrário, a responsabilização por infraç
- Como o art. 134 do CTN permite cobrança de terceiros, o seu parágrafo único os sujeita ind
- Uma lei posterior que comina penalidade tributária menor pode alcançar fato anterior ainda
- Um tributo por homologação foi declarado regularmente, não pago no vencimento e quitado de
```

## Pedido 12: Proteção da Vegetação: Código Florestal, Mata Atlântica e Florestas Públicas (`direito-ambiental.florestas-codigo-florestal-mata-atlantica`, 411 tópicos de edital)

```
Matéria: direito-ambiental.florestas-codigo-florestal-mata-atlantica (Proteção da Vegetação: Código Florestal, Mata Atlântica e Florestas Públicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Código Florestal: conceitos e Áreas de Preservação Permanente": 25
- "2. Reserva Legal, CAR, regularização e fogo": 25
- "3. Lei da Mata Atlântica": 25
- "4. Florestas públicas e concessão florestal": 25

Já existem (não repita):
- Segundo o Código Florestal, as encostas ou partes destas são APP quando a declividade for 
- Segundo o Código Florestal, a Área de Preservação Permanente é área protegida, coberta ou 
- Pelo art. 4º, I, do Código Florestal, a faixa mínima de APP para curso d'água natural com 
- Para cursos d'água com largura superior a 600 metros, o Código Florestal fixa APP marginal
- As áreas no entorno de nascentes e olhos d'água perenes, qualquer que seja sua situação to
- No entorno de lagos e lagoas naturais em zona urbana, a faixa mínima de APP é de:
- Os manguezais são considerados APP apenas na faixa de 100 metros a partir da linha de prea
- Segundo o art. 4º do Código Florestal, constitui APP:
- A obrigação de recompor a vegetação suprimida em APP tem natureza real e é transmitida ao 
- A intervenção ou a supressão de vegetação nativa em APP somente ocorrerá nas hipóteses de:
- No caso de supressão não autorizada de vegetação em APP realizada após 22 de julho de 2008
- Pelo Código Florestal, considera-se área rural consolidada aquela com ocupação antrópica p
- Em imóvel rural situado em área de florestas na Amazônia Legal, o percentual mínimo de Res
- Em imóvel rural localizado fora da Amazônia Legal, como no Nordeste, a Reserva Legal mínim
- Em imóvel situado em área de cerrado na Amazônia Legal, a Reserva Legal mínima é de 35%.
- A Reserva Legal não admite nenhuma forma de exploração econômica, devendo permanecer intoc
- O cômputo das APPs no cálculo do percentual da Reserva Legal é admitido, entre outras cond
- A área de Reserva Legal deve ser registrada no órgão ambiental competente por meio de insc
- O Cadastro Ambiental Rural (CAR) é:
- Nos imóveis rurais que detinham, em 22 de julho de 2008, área de até 4 módulos fiscais e v
- Segundo o Código Florestal, o uso de fogo na vegetação:
- Os Programas de Regularização Ambiental (PRAs) de posses e propriedades rurais são implant
- No fracionamento do imóvel rural, inclusive para assentamentos de reforma agrária, a Reser
- Integra o Bioma Mata Atlântica, segundo a Lei 11.428/2006:
- Na Lei da Mata Atlântica, o corte, a supressão e a exploração da vegetação seguem regime d
- A exploração eventual, sem propósito comercial, de espécies da flora nativa da Mata Atlânt
- O corte e a supressão da vegetação primária do Bioma Mata Atlântica somente serão autoriza
- A vegetação secundária da Mata Atlântica em estágio médio de regeneração pode ser suprimid
- É vedado o corte de vegetação primária ou nos estágios médio e avançado da Mata Atlântica 
- Segundo a Lei 11.428/2006, a supressão de vegetação primária ou secundária em estágio avan
- O regime especial de proteção da Lei da Mata Atlântica alcança a vegetação primária e a se
- Segundo a Lei 11.284/2006, florestas públicas são as florestas naturais ou plantadas, loca
- Antes da realização das concessões florestais, as florestas públicas ocupadas ou utilizada
- As licitações para concessão florestal são realizadas na modalidade:
- O prazo dos contratos de concessão florestal que incluem produtos é fixado de acordo com o
- A concessão florestal é a delegação onerosa do direito de praticar manejo florestal susten
- O Serviço Florestal Brasileiro (SFB), criado na estrutura do Ministério do Meio Ambiente, 
- Na Lei de Gestão de Florestas Públicas, serviços florestais são o turismo e outras ações o
- Integram a Amazônia Legal, segundo o Código Florestal:
- A vegetação situada em APP deve ser mantida pelo proprietário, possuidor ou ocupante a qua
```

## Pedido 13: Ação (`processo-civil.acao`, 409 tópicos de edital)

```
Matéria: processo-civil.acao (Ação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direito de ação, jurisdição, fundamentos e princípios processuais": 21
- "2. Condições, elementos, classificação, pedidos e cumulação": 40
- "3. Competência, conexão, continência e reunião de ações": 13
- "4. Processo, procedimento, sujeitos e pressupostos processuais": 14
- "5. Formação, suspensão, extinção e solução do mérito da ação": 12

Já existem (não repita):
- Uma pessoa ajuíza cobrança, mas, após examinar contrato e recibos, o juiz considera inexis
- Uma petição pretende tutela jurisdicional, mas as afirmações mostram ausência de qualquer 
- Uma pessoa propõe ação para que se declare inexistente dívida ainda não cobrada, diante de
- Duas ações entre mesmas partes têm pedidos e causas de pedir integralmente coincidentes; a
- Uma parte quer saber em qual momento se torna prevento o juízo para eventual reunião de pr
- Um litígio trata de matéria reservada por lei a juízo especializado e os litigantes preten
- Um menor tem direito alegado em ação, mas não possui capacidade para praticar sozinho os a
- Uma demanda é protocolada na secretaria do juízo e o réu ainda não recebeu citação válida.
- Autor e réu acordam suspender processo de conhecimento para tentar composição. Qual limite
- Uma inicial sustenta que determinada empresa é devedora do contrato. Após instrução comple
- Uma parte pede primeiro entrega do bem e, se essa tutela não for acolhida, condenação subs
- Autor formula pedidos de restituição de valor e reparação moral contra o mesmo réu, embora
- Em duas ações há causa de pedir comum, mas os pedidos não coincidem, e ambas continuam sem
- Duas ações têm mesmas partes e causa de pedir, mas o pedido de uma abrange integralmente o
- Uma lei processual nova altera formalidade de ato futuro durante ação em curso; o ato ante
- Parte não praticou um ato até o término do prazo processual e não apresenta justa causa. Q
- Antes da citação, a inicial apresenta cláusula de foro sem relação com residência das part
- Uma pessoa, em nome próprio, pede em juízo direito que a inicial reconhece ser exclusivame
- A autora formula pedido principal de cumprimento específico e, subsidiariamente, indenizaç
- Duas demandas não compartilham pedido nem causa de pedir, mas sua apreciação separada gera
- Ação com pedido de parcelas de janeiro a dezembro é proposta primeiro; depois, mesmas part
- Após citação, o réu verifica abusividade da cláusula de eleição de foro e quer opô-la. O q
- No tribunal, verificou-se representação irregular do recorrente e foi concedido prazo de c
- A inicial narra débito de aluguel. Após a citação, antes do saneamento, o autor pretende t
- Depois da contestação, autor deixa diligência de sua responsabilidade por 40 dias e, intim
- Uma pessoa tem sua cobrança julgada improcedente após regular instrução. Mesmo assim, houv
- O art. 17 do CPC exige, como três condições expressas para postular em juízo, legitimidade
- Para reconhecer ações idênticas no art. 337, §2º, o julgador compara partes, causa de pedi
- Em cobrança contratual comum, a competência territorial é necessariamente absoluta e não p
- Uma criança é titular de pretensão patrimonial e pode figurar como parte, embora deva atua
- A ação passa a existir somente quando o réu é validamente citado, pois o art. 312 do CPC e
- A lei processual nova alcança imediatamente atos futuros de processo já iniciado, preserva
- Ao aplicar teoria da asserção, o juiz só pode reconhecer legitimidade se o autor já demons
- Pedidos contra o mesmo réu podem ser cumulados em um processo mesmo sem conexão, desde que
- Duas ações possuem causa de pedir idêntica e pedidos distintos. Como não existe identidade
- Após a Lei nº 14.879/2024, o art. 63 do CPC exige que o foro de eleição se vincule a domic
- O autor comparece sem representação processual regular e o juiz deve imediatamente julgar 
- Por acordo, autor e réu podem suspender a tramitação por até seis meses, enquanto a suspen
- Continência entre ações com mesmas partes e causa de pedir exige reunião obrigatória em qu
- Mesmo sem compartilharem pedido ou causa de pedir, ações podem ser reunidas para julgament
- Uma parte pratica validamente ato processual e depois pretende exercitar novamente a mesma
- O órgão jurisdicional verifica que a matéria pode ser conhecida de ofício, mas pretende de
- Uma parte apresenta causa em juízo territorialmente incompetente e o juiz pode desconsider
- Depois de três sentenças de extinção por abandono imputável ao autor, fica vedada nova açã
- Um autor apresenta renúncia expressa à pretensão material e o juiz a homologa. A decisão é
```

## Pedido 14: Criminalística: fundamentos da perícia (`criminalistica.fundamentos-pericia`, 409 tópicos de edital)

```
Matéria: criminalistica.fundamentos-pericia (Criminalística: fundamentos da perícia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Criminalística, perícia e valor dos vestígios": 25
- "2. Local de crime, levantamentos e vestígios de mortes": 29
- "3. Cadeia de custódia: dez etapas, fases e consequências": 19
- "4. Documentoscopia, grafoscopia e falsificações": 12
- "5. Balística forense, confronto e interpretação de disparos": 15

Já existem (não repita):
- Após encontrar uma porta com marcas de ferramenta, um examinador deve diferenciar a noção 
- Uma infração deixou danos físicos numa fechadura e o investigado confessou o fato. Sobre a
- No local de arrombamento, a primeira equipe delimita o acesso para evitar entrada de curio
- Uma equipe encontra fibras muito pequenas aderidas a uma superfície próxima de uma janela 
- Durante exame de assinatura manuscrita contestada, deseja-se comparar hábitos gráficos com
- Em exame balístico, uma peça encontrada no chão conserva a espoleta percutida e marcas de 
- Ao elaborar relatório técnico após exame de um local, foram formulados quesitos e solicita
- Em uma investigação, um fragmento de tinta pode ter sido depositado antes dos fatos, embor
- Na documentação de um local, foram fotografadas a posição original de um fragmento e suas 
- Ao chegar a um local suspeito de morte violenta, encontram-se objeto perfurante junto ao c
- Em exame criminal não existe perito oficial disponível. Foram identificados profissionais 
- Uma amostra foi isolada, fixada, coletada, embalada e transportada à unidade pericial. Qua
- No levantamento de furto com rompimento de porta, há marcas de instrumento e fragmentos de
- Um perito autorizado rompe lacre para analisar um vestígio e precisa acondicioná-lo novame
- A análise de documento demonstrou papel e assinatura autênticos, mas há suspeita de que o 
- Dois estojos foram apreendidos em locais diferentes e comparados a estojos obtidos em disp
- Numa mesma investigação, foram coletadas fibras de roupa e partículas de vidro de duas sal
- Um vestígio foi analisado após transporte com falha na anotação de horário, mas há outros 
- Uma cena contém cadáver junto a uma escada, sangue no piso e objetos deslocados. A hipótes
- A análise de resíduos nas mãos de uma pessoa encontrou partículas compatíveis com disparo,
- Em análise de uma assinatura contestada, dispõe-se de uma única amostra antiga, pequena e 
- Um exame de laboratório consumirá parcialmente material biológico limitado. Antes de proce
- Em exame de possível morte por suspensão, uma corda está próxima do cadáver. O laudo de lo
- Em um local de disparo, o exame revelou vários estojos concentrados junto à parede e projé
- Um exame de possível falsificação encontra substituição de fotografia num documento previa
- Em investigação de arrombamento, o conjunto de marcas e danos materiais encontrado na port
- Uma pessoa confessa a prática de delito que deixou vestígios físicos disponíveis; por isso
- Em busca de sinais de rompimento, a proteção da porta e do ambiente contíguo evita alteraç
- Uma equipe descreve e fotografa uma marca de ferramenta antes de removê-la, realizando a e
- Uma assinatura manuscrita questionada pode ser submetida a comparação grafoscópica com esc
- Em exame de munição, projétil é o conjunto completo que inclui estojo, espoleta, carga pro
- Em um local com fibras minúsculas aderidas a uma vestimenta, a origem de cada amostra deve
- Na falta de perito oficial, a autoridade solicita que apenas um assistente técnico de uma 
- Em uma ocorrência na qual socorristas moveram objetos para salvar uma vítima, é necessário
- Em uma central de custódia, o material somente precisa de registro na primeira entrada; ac
- Um documento aparentemente verdadeiro pode conter declaração ideologicamente falsa, ainda 
- Uma perícia de eficiência numa arma e uma perícia de confronto balístico respondem necessa
- Uma amostra que chegou ao laboratório pode estar no início da chamada fase interna da cade
- Em levantamento de possível suicídio, a presença de um instrumento ao alcance da vítima pe
- Após análise de uma amostra em laboratório, o material remanescente deve retornar à centra
- Uma fibra encontrada na vestimenta é classificada como vestígio verdadeiro apenas porque s
- Uma falha pontual no transporte de vestígio não torna automaticamente inválida toda a prov
- A recuperação de diversos estojos após disparos permite, por si só, determinar a posição e
- Em documento que contém assinatura genuína e informação falsa, a conclusão grafoscópica de
- Em local de morte suspeita com laço e objetos deslocados, os sinais ambientais já bastam p
```

## Pedido 15: Vozes verbais (`portugues.vozes-verbais`, 405 tópicos de edital)

```
Matéria: portugues.vozes-verbais (Vozes verbais)
Capítulos e quantas questões quero em cada um (total 100):
- "Reconhecimento das vozes verbais": 34
- "Transposição, concordância e manutenção do sentido": 33
- "Como o tema costuma ser cobrado": 33

Já existem (não repita):
- Em A equipe revisou o relatório, qual análise está correta?
- A voz passiva de Os técnicos arquivaram as fichas é:
- Em Publicam-se editais, qual termo é sujeito paciente?
- Em Precisa-se de material resistente, o se é:
- Qual frase apresenta voz reflexiva no sentido de agir sobre si?
- Julgue: Em As janelas foram fechadas pelo zelador, janelas é sujeito paciente e pelo zelad
- Julgue: Em Trabalha-se muito neste laboratório, muito é sujeito paciente plural e exige tr
- Julgue: Em Os colegas se cumprimentaram, a leitura usual pode ser recíproca: um cumpriment
- Julgue: Em Foram entregues as encomendas, o sujeito posposto encomendas controla o plural 
- Julgue: O verbo intransitivo chegar forma passiva sintética em Chegaram-se cedo ao local.
- Julgue: Em A diretora divulgará o resultado, a passiva correspondente é O resultado será d
- Julgue: Em Necessita-se de assinaturas, assinaturas é sujeito paciente e a concordância no
- Julgue: Em A proposta foi aceita pela gerente, há evento passivo e gerente é o agente expr
- Julgue: Em As propostas estão sendo examinadas, o sujeito paciente é plural e o particípio
- Julgue: Em Os participantes se reuniram, o se é necessariamente apassivador porque a forma
- Em A secretaria enviou a confirmação aos inscritos, qual análise da passiva A confirmação 
- Qual alternativa contém passiva sintética com sujeito paciente plural?
- A transposição que preserva o modo de Talvez a equipe conclua o relatório é:
- Em A equipe deve conferir os dados, qual passiva preserva modalidade e concordância?
- Em As salas foram organizadas pelos funcionários, qual termo controla a concordância?
- Qual transformação de Não divulgaram os resultados evita inventar quem praticou a ação?
- Em O documento estava assinado quando chegou, qual análise é mais adequada ao contexto?
- Qual oração não admite passiva canônica na leitura apresentada?
- Em A pesquisadora se elogiou após a palestra, qual paráfrase conserva a reflexividade?
- Em Os formulários se rasgaram no transporte, sem contexto adicional, que leitura deve ser 
- Qual opção mantém leitura recíproca de As colegas se apoiaram durante a mudança?
- Em Divulgaram-se os comunicados e os horários, qual concordância segue a análise de passiv
- A passiva que preserva o futuro em A comissão revisará os contratos é:
- Em Foram oferecidos aos inscritos dois horários, qual termo controla a concordância verbal
- Qual alternativa analisa corretamente Precisa-se de profissionais e Solicitaram-se profiss
- A conversão passiva correta de A equipe está examinando as propostas é:
- Em A gerente talvez aprove os pedidos, qual forma passiva preserva subjuntivo e concordânc
- Em As regras foram comunicadas aos usuários, qual análise do complemento aos usuários está
- Qual frase admite passiva canônica sem mudar a regência apresentada?
- Qual reescrita preserva a negação e o futuro do pretérito de O conselho não aprovaria a mu
- Julgue: Em Os pedidos foram analisados pelos técnicos, pedidos é sujeito paciente e pelos 
- Julgue: Em Obedece-se às normas, normas é sujeito paciente e o verbo deveria concordar no 
- Julgue: A passiva de Os funcionários não entregaram os documentos pode ser Os documentos n
- Julgue: Em A criança se vestiu, a construção necessariamente indica que outra pessoa vesti
- Julgue: Se o agente não aparece em A sala foi limpa, a ativa A equipe limpou a sala preser
- Julgue: Em O documento estava assinado ao meio-dia, a forma com estar pode descrever estad
```

## Pedido 16: Noções de Arquivologia (`administracao-publica.nocoes-arquivologia`, 404 tópicos de edital)

```
Matéria: administracao-publica.nocoes-arquivologia (Noções de Arquivologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, princípios e tipos de documentos arquivísticos": 27
- "2. Protocolo, tramitação, classificação e métodos de arquivamento": 26
- "3. Ciclo vital, avaliação, temporalidade e destinação legal": 18
- "4. Acondicionamento, armazenamento, conservação e preservação": 16
- "5. Descrição básica, instrumentos de pesquisa, acesso e casos integrados": 13

Já existem (não repita):
- Duas unidades administrativas produzem relatórios sobre o mesmo tema. No arquivo central, 
- Um arquivo guarda uma ata de reunião de comissão. Na identificação documental, como difere
- Um expediente reúne requerimento inicial, comprovantes, parecer e decisão referentes à mes
- Uma correspondência chega acompanhada de três anexos, mas apenas dois são encontrados. Qua
- Uma unidade utiliza um plano que distribui os documentos conforme as funções e atividades 
- Um documento teve sua tramitação encerrada, mas continua sendo consultado diversas vezes p
- Um conjunto já não é utilizado com frequência e aguarda aplicação da tabela para saber se 
- Um lote deixa o arquivo corrente e segue ao intermediário. Outro, avaliado como de guarda 
- Um conjunto de folhas permanentes apresenta fragilidade e presença de fitas adesivas antig
- Uma coleção de processos é guardada por número de registro, mas os usuários localizam o nú
- Uma unidade organiza expedientes pelas datas de produção, do mais antigo ao mais recente. 
- Um arquivo escolhe organizar documentos pelas localidades de origem, agrupando primeiro po
- Após o prazo aprovado na tabela, pretende-se eliminar documentos públicos avaliados como d
- Uma tabela hipotética prevê dois anos de guarda corrente e três anos intermediários, conta
- Ao preparar um instrumento de acesso, uma equipe deseja apresentar visão geral dos fundos 
- O depósito recebeu plantas e mapas de grandes dimensões, alguns frágeis nas dobras. Qual e
- Uma instituição pública encerra suas atividades e deixa um conjunto documental acumulado n
- Um conjunto pertence a pessoa jurídica privada e contém registros relevantes para a histór
- Uma equipe deve organizar fotos de obras, plantas cartográficas, gravações sonoras e ofíci
- Uma equipe constata que um relatório administrativo foi digitalizado, embora seu original 
- Uma unidade pretende consultar as regras de classificação das atividades-meio do Poder Exe
- Uma instituição quer assegurar que o sistema digital represente funções de classificação, 
- Uma secretaria vai encaminhar uma pasta numerada a um setor distante, mas pretende permiti
- A CPAD encontra documentos sem previsão na tabela de temporalidade e avalia a possibilidad
- Um arquivo permanente enfrenta umidade elevada, poeira e risco de infiltração. A equipe co
- Um usuário busca primeiro conhecer fundos existentes, depois identificar séries de um fund
- Dois ofícios de setores distintos apresentam a mesma configuração formal, mas nasceram de 
- Depois de protocolado e distribuído um documento, o controle de sua movimentação deixa de 
- Um processo já encerrou sua tramitação, mas é consultado frequentemente para subsidiar nov
- Uma peça de valor histórico permanente pode ser eliminada diretamente pelo setor de guarda
- Guardar documentos em caixas adequadas e dispor essas caixas em estantes estáveis constitu
- Ordenar documentos pelo município de origem caracteriza método ideográfico, porque nomes d
- Em sistema numérico, um índice nominal pode remeter do nome do interessado ao número da pa
- Uma tabela de temporalidade determina prazo de três anos a partir do encerramento do proce
- Para eliminar documentos públicos integrantes do SINAR, a Comissão Permanente de Avaliação
- Uma fotografia digital, uma gravação sonora e um texto eletrônico possuem o mesmo gênero d
- Para informar os fundos disponíveis, pode-se produzir um guia; para detalhar uma série, um
- Se um documento eletrônico tiver cópia de segurança diária, estarão atendidos todos os req
- Os arquivos privados podem ser formados por documentos recebidos e produzidos por pessoas 
- Em acervo de manuscritos frágeis, a aplicação de fita adesiva de uso comum constitui resta
- Nas regras de eliminação do SINAR após a Resolução CONARQ 44/2020, documentos digitais e n
- A Resolução CONARQ 14/2001 ainda deve ser aplicada como instrumento obrigatório e atual da
- Na gestão de documentos digitais, o e-ARQ Brasil versão 2 pode orientar sistemas que traba
- Uma unidade publica documentos parcialmente sigilosos e oculta suas partes protegidas. Por
- Mesmo que documentos de produtores diferentes estejam em uma exposição temática conjunta, 
- Uma entidade assume que arquivos públicos devem ser conservados unicamente por uma tempera
```

## Pedido 17: Impostos por espécie e reforma tributária do consumo (`direito-tributario.impostos-especie`, 402 tópicos de edital)

```
Matéria: direito-tributario.impostos-especie (Impostos por espécie e reforma tributária do consumo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Impostos federais: comércio exterior, renda, industrialização e operações financeiras": 31
- "2. Impostos estaduais e distritais: ICMS, IPVA e ITCMD": 12
- "3. Impostos municipais: IPTU, ITBI, ISS e demais espécies de receita tributária": 31
- "4. IBS, CBS e Imposto Seletivo: incidência, crédito e administração": 14
- "5. Reforma tributária do consumo: calendário, conflitos e resolução de casos": 12

Já existem (não repita):
- Uma empresa adquire produto estrangeiro para ingressar no território brasileiro. O departa
- Uma pessoa recebe rendimentos de seu trabalho e também realiza transferências entre duas c
- Uma fábrica conclui industrialização de um produto e promove sua saída do estabelecimento 
- Uma instituição celebra operação de câmbio para aquisição de moeda estrangeira. Entre as c
- Uma empresa presta transporte remunerado de cargas entre dois Municípios do mesmo Estado. 
- Uma pessoa registra em seu patrimônio um automóvel de passeio regularmente sujeito à tribu
- Uma pessoa transmite gratuitamente, por doação, um terreno a um parente. Qual imposto inci
- Uma residência está localizada em zona urbana legalmente definida e sujeita a tributação m
- Duas pessoas celebram compra e venda de imóvel, culminando em transmissão onerosa de propr
- Uma empresa presta, de forma remunerada, um serviço previsto na lista anexa à LC 116/2003.
- Um profissional sediado no Brasil presta serviço constante da lista da LC 116/2003 a clien
- Uma empresa presta serviço listado, sem exceção de local prevista na LC 116/2003. O estabe
- Uma pessoa recebe salário com imposto de renda retido na fonte e apresenta declaração anua
- Uma pessoa jurídica apura IR pelo lucro real. Na escrituração, registra despesas e receita
- A reforma tributária criou o IBS, destinado a incidir sobre operações com bens e serviços 
- A União pretende identificar a nova exação de caráter contributivo que incide sobre bens e
- Um setor de controle estuda a instituição e as competências do Comitê Gestor do IBS depois
- Em outubro de 2026, um estudante afirma que a EC 132/2023 extinguiu, em sua publicação, IC
- Uma empresa loca veículo sem motorista, sem outra prestação autônoma de serviço. O Municíp
- Uma empresa planeja os efeitos previstos para 2027 sobre o IPI. Qual interpretação da EC 1
- Duas pessoas firmam instrumento particular de promessa de compra e venda de apartamento; a
- O Município atribui por lei ao tomador de determinado serviço listado a responsabilidade p
- Uma exportadora realiza operações abrangidas pela disciplina comum de IBS e CBS e adquire 
- O proprietário de determinada aeronave agrícola suscita o tratamento de IPVA aplicável apó
- Uma pessoa física residente no Brasil recebe de pessoa física rendimentos sujeitos às regr
- Uma pessoa jurídica retém imposto de renda sobre rendimento pago a uma pessoa física. A re
- Um exportador remete ao exterior produtos nacionais. Essa saída configura, pelo art. 19 do
- O ICMS pode alcançar prestação onerosa de transporte intermunicipal, enquanto a mera prest
- A propriedade de terreno rural sujeito ao ITR é, por definição, fato gerador de IPTU munic
- O art. 5º da LC 116/2003 define o prestador de serviço como contribuinte do ISS, embora o 
- Uma empresa exporta mercadorias e, por estar sujeita à não cumulatividade, deve em regra c
- Em um serviço de execução de obra enquadrado em exceção do art. 3º da LC 116/2003, pode se
- Uma contribuição municipal destinada ao custeio de iluminação pública deve ser classificad
- Na transmissão por doação de imóvel situado no Brasil, o ente competente para o ITCMD é id
- A transição constitucional prevê que o ICMS e o ISS foram extintos em 2029, primeiro ano d
- O art. 156-A, § 1º, da Constituição admite incidência do IBS sobre importações de bens mat
- Receitas de operações sujeitas a IRPJ pelo lucro presumido são apuradas obrigatoriamente p
- Uma empresa entrega concreto preparado durante o trajeto para obra por empreitada. A Súmul
- Como o contrato de promessa de compra e venda de imóvel costuma ser oneroso, sua mera assi
- A não incidência do IBS sobre exportações não impede, por si, que o exportador mantenha e 
- A LC 227/2026 instituiu o Comitê Gestor do IBS e converteu o imposto em tributo federal, p
- Para bens móveis recebidos por doação de pessoa domiciliada no exterior, a LC 227/2026 pre
- IBS e CBS são impostos de mesma competência municipal, de modo que uma operação sujeita ao
- A reforma previu alteração do IPI a partir de 2027, com redução geral de suas alíquotas e 
- O Imposto Seletivo previsto no art. 153, VIII, da Constituição é contribuição municipal vo
```

## Pedido 18: Lei 8.112/1990: direitos e vantagens (`legislacao.lei-8112-1990-direitos-vantagens`, 400 tópicos de edital)

```
Matéria: legislacao.lei-8112-1990-direitos-vantagens (Lei 8.112/1990: direitos e vantagens)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Vencimento, remuneração, descontos e reposições ao erário": 23
- "2. Indenizações, gratificações e adicionais": 20
- "3. Férias e licenças: requisitos, duração e remuneração": 20
- "4. Afastamentos, concessões e contagem de tempo de serviço": 18
- "5. Direito de petição: reconsideração, recurso e prescrição": 19

Já existem (não repita):
- Uma pessoa recebe R$ 4.200 de valor básico legal e R$ 800 de vantagem permanente prevista 
- Uma pessoa falta um dia inteiro ao trabalho, sem apresentar qualquer motivo justificável. 
- Uma transferência de exercício, no interesse do serviço, envolve mudança permanente de dom
- Em viagem a serviço, a pessoa se afasta eventualmente da sede por um dia, sem pernoitar e 
- Uma pessoa trabalha excepcionalmente além da jornada normal, em situação temporária autori
- Um servidor completa seu primeiro período aquisitivo de férias sob a Lei nº 8.112/1990. Qu
- A pessoa solicita parcelamento dos 30 dias de férias ordinárias, e há interesse da Adminis
- Depois de cinco anos de efetivo exercício, uma pessoa pede afastamento remunerado para cur
- Uma pessoa solicita ausência sem prejuízo por seu casamento. Qual duração está expressamen
- Uma licença para assistência indispensável a familiar, autorizada por perícia oficial, alc
- Uma pessoa efetiva fora do estágio probatório requer licença sem vencimentos para resolver
- Uma pessoa comparece para doação de sangue e pede ausência do serviço sem qualquer prejuíz
- Uma pessoa com deficiência tem necessidade de horário especial comprovada por junta médica
- No interesse do serviço, uma pessoa efetiva deseja afastar-se para cursar mestrado stricto
- Uma unidade de gestão de pessoas deve converter dias de serviço computável em anos, confor
- Uma pessoa protocola pedido de reconsideração dirigido à mesma autoridade que decidiu inic
- Após ciência de decisão administrativa desfavorável, uma pessoa pretende interpor recurso 
- No processamento de uma folha, descobre-se pagamento indevido efetuado no mês imediatament
- Em local com exposição habitual que enseja simultaneamente os adicionais legais de insalub
- Uma hora normal de serviço vale R$ 40. A pessoa presta uma hora extraordinária que também 
- Uma pessoa que opera direta e permanentemente com substâncias radioativas recebe programaç
- Uma pessoa escolhida como candidata em convenção partidária ainda não teve sua candidatura
- Um servidor assume mandato legislativo municipal e demonstra compatibilidade integral entr
- Ao elaborar assentamento de tempo, a unidade funcional examina licença remunerada por doen
- Uma pessoa quer requerer revisão de ato que afeta crédito decorrente de sua relação funcio
- Uma pessoa possui vencimento básico fixado em lei e recebe vantagem pecuniária permanente 
- Uma pessoa falta ao trabalho sem qualquer motivo justificável, mas o art. 44 permite que m
- No interesse do serviço, uma pessoa muda permanentemente de domicílio para nova sede e pre
- Uma pessoa recebeu diárias, mas a viagem de serviço foi cancelada. Por se tratar de indeni
- O adicional noturno estatutário incide sobre serviço entre 22h e 5h, com acréscimo de 25% 
- O art. 77 permite que a pessoa fragmente os trinta dias de férias em qualquer número de et
- Na gratificação natalina, uma fração de exercício igual a quinze dias é computada como mês
- Uma nova licença da mesma espécie começa quarenta dias após o fim da anterior. Para o art.
- A licença motivada por doença de familiar pode ser concedida, com requisitos legais e no m
- A licença para tratar de interesses particulares do art. 91 é remunerada e de concessão au
- Para afastamento de doutorado stricto sensu no País, o art. 96-A, § 2º, exige vínculo efet
- A apuração do tempo de serviço estatutário é feita em dias, que, conforme art. 101, devem 
- Uma autoridade indefere pedido de reconsideração, que foi dirigido à própria autoridade au
- Uma pessoa recorre de decisão funcional e informa que o simples protocolo suspende obrigat
- Uma reconsideração tempestiva e cabível interrompe a prescrição do direito de requerer, e,
- Por analogia ao casamento, o falecimento de qualquer parente até o quarto grau garante oit
- Uma pessoa está de férias quando sobrevém calamidade pública, justificando interrupção reg
- Uma pessoa recebe simultaneamente adicionais legais de insalubridade e de periculosidade p
- Uma pessoa solicita horário especial por ter filho com deficiência e comprova a necessidad
- No direito de petição, todas as pretensões prescrevem em cento e vinte dias, inclusive aqu
```

## Pedido 19: Bancos NoSQL e Big Data (`informatica.nosql-big-data`, 400 tópicos de edital)

```
Matéria: informatica.nosql-big-data (Bancos NoSQL e Big Data)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos NoSQL, modelos de dados e bancos em memória": 28
- "2. Distribuição, CAP, replicação e garantias de consistência": 13
- "3. Big Data, armazenamento distribuído e serviços em nuvem": 24
- "4. Hadoop e Spark: processamento massivo e paralelo": 19
- "5. Engenharia de dados, streaming, pipelines e arquiteturas de consumo": 16

Já existem (não repita):
- Uma aplicação de registros de sensores precisa distribuir dados entre máquinas e acomodar 
- Uma coleção MongoDB guarda pedidos com o campo status. Qual operação identifica os documen
- Um serviço mantém sessões por um minuto no Redis e deseja expirar cada chave automaticamen
- Um sistema precisa descobrir pessoas conectadas por relacionamentos tipados, inclusive per
- Uma aplicação móvel precisa manter dados locais em um arquivo, realizando consultas relaci
- Uma equipe utiliza Redis para dados importantes e quer recuperação após reinício. Qual opç
- Uma coleção cresce além da capacidade de armazenamento de um nó e deseja-se dividir seus d
- Duas partes de um cluster deixam de se comunicar e recebem solicitações de escrita. De aco
- Após uma atualização, uma réplica ainda devolve o valor antigo, mas todas convergem após c
- Uma arquitetura distribuída avalia latência das leituras mesmo sem partições de rede. Qual
- Em um modelo simplificado com N=5 réplicas fixas, quais valores de leitura R e escrita W g
- Um projeto recebe terabytes históricos, eventos em alta frequência e formatos diferentes, 
- Um coletor recebe JSON com campos opcionais, fotografias e linhas cadastrais rigidamente d
- Uma equipe quer guardar vídeos e arquivos JSON na nuvem, cada um com identificador e metad
- Um serviço de banco documental gerenciado em nuvem executa atualizações de infraestrutura 
- Uma plataforma preserva arquivos brutos, cria tabelas governadas para análise e aplica reg
- Em um cluster HDFS, o cliente precisa localizar blocos de um arquivo e depois transferir s
- Um cálculo MapReduce conta ocorrências de termos em arquivos enormes. Como ocorre o proces
- Uma arquitetura Hadoop precisa coordenar recursos de aplicações, consultar arquivos por SQ
- Uma aplicação Spark cria r2 = rdd.filter(predicado) e depois chama r2.count(). Sem cache p
- Um processamento Spark usa map para padronizar eventos e depois groupByKey para agregar po
- Uma plataforma carrega eventos brutos no lake e aplica limpeza dentro da infraestrutura an
- Um tópico Kafka possui quatro partições e um grupo com seis consumidores ativos, sem atrib
- Uma equipe precisa agendar extrações diárias com dependências e retentativas, enviar event
- Um sistema quer unificar o processamento em um fluxo de eventos retido, permitindo reexecu
- Ao montar cache de autenticação, uma aplicação usa Redis como base chave-valor e recupera 
- Ao escolher SQLite para persistir informações locais de um aplicativo, a equipe estará uti
- Ao armazenar registros de clientes com conjuntos distintos de campos em uma coleção MongoD
- Por operar com dados em RAM, uma instância Redis com AOF habilitado não tem qualquer meio 
- Em um sistema com três réplicas do mesmo registro, a redundância resulta de replicação; di
- Durante leitura de um arquivo no HDFS, os bytes são servidos normalmente pelo NameNode, en
- Em um cluster que privilegia aceitar requisições nos dois lados de uma partição de rede, p
- Em cinco réplicas, exigir duas respostas de leitura e três de escrita assegura interseção 
- Para guardar vídeos brutos em um data lake, uma arquitetura pode utilizar objetos com meta
- Quando um repositório passa da camada bruta para a camada tratada, todos os dados de orige
- Contratar uma base documental gerenciada reduz tarefas de manutenção de infraestrutura, ma
- No estudo de Big Data, a equipe que caracteriza centenas de formatos distintos está medind
- Um DataFrame Spark representa dados distribuídos com esquema e colunas nomeadas, podendo s
- Em um RDD não armazenado em cache, a chamada count() apenas registra transformação preguiç
- Dois eventos com a mesma chave, enviados a uma mesma partição Kafka, podem ser consumidos 
- Para evitar releitura, Kafka apaga obrigatoriamente cada evento assim que um membro do pri
- Um fluxo de tarefas que executa uma ingestão diária, espera validação e só então publica d
- Em uma arquitetura ELT, os dados são obrigatoriamente depurados na origem antes do carrega
- Uma arquitetura Kappa pode reaplicar o mesmo processamento de fluxo a eventos antigos, des
- Migrar um pipeline para serviços gerenciados e usar Kafka elimina a possibilidade de entre
```

## Pedido 20: Rio Grande do Norte: história e aspectos geoeconômicos (`rio-grande-do-norte.historia-aspectos-geoeconomicos`, 399 tópicos de edital)

```
Matéria: rio-grande-do-norte.historia-aspectos-geoeconomicos (Rio Grande do Norte: história e aspectos geoeconômicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Capitania do Rio Grande: conquista, colonização e resistências": 20
- "2. Século XIX: secas, saúde pública, imprensa e lutas pela liberdade": 20
- "3. República e século XX: cangaço, 1930, Segunda Guerra e programa espacial": 20
- "4. Sociedade potiguar: populações tradicionais, salineiros e mineração do Seridó": 20
- "5. Geoeconomia contemporânea: agropecuária, litoral, petróleo, turismo e renováveis": 20

Já existem (não repita):
- A fundação de Natal, em 1599, relaciona-se à consolidação da ocupação portuguesa no entorn
- A chamada Guerra dos Bárbaros foi uma única batalha naval travada entre portugueses e hola
- O Motim das Mulheres, em Mossoró, ocorreu no século XIX e contestou procedimentos de recru
- A data de 30 de setembro de 1883 simboliza a abolição local da escravidão em Mossoró, ante
- O ataque do bando de Lampião a Mossoró, em 1927, terminou com o êxito dos cangaceiros na o
- A importância de Parnamirim na Segunda Guerra Mundial esteve associada a rotas e operações
- A carcinicultura é a criação de camarões, atividade distinta da captura pesqueira extrativ
- No período colonial, a concessão de uma capitania hereditária assegurava imediatamente a o
- A pecuária contribuiu para a interiorização colonial do Rio Grande e intensificou disputas
- A crise das secas oitocentistas pode ser explicada satisfatoriamente apenas pela medição d
- Durante a Revolução de 1930, Juvenal Lamartine permaneceu no comando do Rio Grande do Nort
- O Centro de Lançamento da Barreira do Inferno surgiu em 1965, décadas depois do papel estr
- A atividade salineira potiguar envolve condições naturais costeiras favoráveis à evaporaçã
- O cultivo irrigado de frutas torna a agricultura do Oeste potiguar independente de fontes 
- O nome Castelo Ceulen, associado à fortificação potiguar no período holandês, permite conc
- O dízimo colonial pode ser analisado como mecanismo fiscal de cobrança sobre a produção, e
- A mobilização de Mossoró em 1875 deve ser caracterizada como protesto contra a execução de
- A presença documentada de pessoas quilombolas no RN em 2022 permite rejeitar a ideia de qu
- Na mineração do Seridó, a exploração histórica da scheelita esteve ligada ao tungstênio e 
- A implantação de parques eólicos e solares assegura, por si só, fornecimento de eletricida
- Qual elemento geográfico explica a localização estratégica do Forte dos Reis Magos no iníc
- O acontecimento de Mossoró diretamente associado à data de 30 de setembro de 1883 é:
- Na história da imprensa do Rio Grande do Norte, qual periódico se tornou referência partic
- Durante a Segunda Guerra Mundial, a expressão Trampolim da Vitória ficou associada princip
- Na tradição mineral do Seridó potiguar, o minério associado historicamente à Mina Brejuí é
- Qual atividade econômica consiste na criação de camarões para comercialização?
- Qual associação entre evento e local está correta?
- O que diferencia diretamente a energia eólica da energia solar fotovoltaica?
- Ao estudar a Guerra dos Bárbaros, qual interpretação considera mais adequadamente a agênci
- A grande seca de 1877–1879 deve ser analisada, em uma questão que discute saúde pública, p
- Sobre o Forte dos Reis Magos e a tomada holandesa, indique a interpretação historicamente 
- Qual alternativa expressa corretamente a distinção entre fiscalidade e apropriação da terr
- Uma professora afirma que o Motim das Mulheres ocorreu em Mossoró, em 1875, durante uma Gu
- O movimento de 1930 no Rio Grande do Norte caracterizou-se por:
- Qual consequência possível da expansão das salinas merece análise além dos indicadores de 
- Assinale a relação geoeconômica correta no Rio Grande do Norte.
- Qual afirmação sobre a chamada Guerra dos Bárbaros deve orientar uma interpretação crítica
- Em uma análise da economia colonial da capitania, o recolhimento de dízimos por agentes ou
- Um historiador compara notícia de jornal do século XIX, relato memorialístico posterior e 
- Uma questão afirma: “A abolição de 1883 em Mossoró instituiu liberdade legal em todos os m
- Uma comissão precisa classificar dois marcos de Parnamirim: Parnamirim Field e CLBI. Qual 
- Após a deposição de Juvenal Lamartine em 1930, a sucessão de governos provisórios e interv
- O estudo conjunto de quilombolas potiguares e do Censo 2022 permite concluir que:
- Uma análise econômica da scheelita extraída em Currais Novos observa períodos de expansão 
- Uma matéria afirma que um parque eólico com 100 MW instalados entrega necessariamente 100 
```

## Pedido 21: Neurologia clínica (`medicina.neurologia`, 397 tópicos de edital)

```
Matéria: medicina.neurologia (Neurologia clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "Neuroanatomia e neurofisiologia clínica": 17
- "Semiologia, exame neurológico e alteração da consciência": 17
- "Cefaleias e doenças cerebrovasculares": 17
- "Epilepsias, estado de mal epiléptico e diagnósticos diferenciais": 17
- "Demências, parkinsonismo e distúrbios do movimento": 16
- "Neuroimunologia, doenças periféricas, infecções e tumores do SNC": 16

Já existem (não repita):
- Em paciente com desmielinização do sistema nervoso central, qual célula é responsável pela
- Uma pessoa perde predominantemente vibração e senso de posição articular após lesão de uma
- Um infarto cortical apresenta hemiparesia com predomínio na perna contralateral e abulia. 
- No exame, há dismetria ao teste dedo-nariz, tremor de intenção e marcha de base alargada, 
- A maior parte das fibras do trato corticoespinal cruza na região das pirâmides bulbares; p
- O GABA exerce papel inibitório importante no SNC, enquanto o glutamato é o principal neuro
- Hiperreflexia, fasciculações e atrofia muscular intensa compõem, necessariamente, uma sínd
- A lesão do hemisfério dominante no território da artéria cerebral média pode cursar com af
- Um indivíduo compreende ordens simples, porém articula palavras com imprecisão muscular e 
- No reflexo fotomotor, qual par de nervos constitui, respectivamente, as vias aferente e ef
- Fraqueza progressiva dos membros, arreflexia e parestesias em bota após gastroenterite sug
- Paciente acordado, capaz de mover verticalmente os olhos e compreender ordens, mas sem mov
- No teste de Romberg, piora pronunciada do equilíbrio ao fechar os olhos pode sugerir compr
- A escala de coma de Glasgow combina respostas ocular, verbal e motora, mas a intubação e a
- A ausência de rigidez de nuca, isoladamente, exclui meningite bacteriana em pessoas idosas
- O delirium costuma ter instalação aguda e curso flutuante, com prejuízo da atenção, podend
- Mulher apresenta episódios de cefaleia incapacitante, fotofobia, náuseas e fenômenos visua
- Qual apresentação exige prioritariamente investigação de causa secundária potencialmente g
- Paciente com hemiparesia súbita é avaliado com tomografia craniana sem contraste inicial, 
- Em prevenção secundária de AVCi associado à fibrilação atrial, qual classe é habitualmente
- Triptanos podem ser prescritos indistintamente a qualquer pessoa com cefaleia pulsátil, se
- Na abordagem convencional do AVC isquêmico, trombólise intravenosa em pessoas elegíveis é 
- A trombectomia mecânica tem, necessariamente, a mesma janela temporal da trombólise intrav
- Cefaleia, vômitos e hipertensão podem aumentar a suspeita de AVC hemorrágico, porém não di
- Homem apresenta automatismos oroalimentares, olhar fixo e prejuízo transitório da responsi
- Pessoa apresenta duas crises compatíveis com epilepsia, mas o EEG interictal realizado ent
- Em crise tônico-clônica sustentada compatível com estado de mal convulsivo, qual classe co
- Após crise focal motora observada, paciente apresenta fraqueza unilateral que regride grad
- Uma convulsão provocada por hipoglicemia aguda basta, isoladamente, para estabelecer o dia
- Uma aura olfatória estereotipada antes de abalos bilaterais pode representar manifestação 
- Em paciente com coma persistente após cessarem os abalos motores, não há utilidade para o 
- Estado de mal convulsivo pode ser reconhecido operacionalmente por crise prolongada em tor
- Qual característica diferencia principalmente demência de comprometimento cognitivo leve?
- Qual conjunto clínico é especialmente sugestivo de demência com corpos de Lewy?
- No exame de um paciente com suspeita de síndrome parkinsoniana, qual achado é essencial ao
- Qual característica favorece tremor essencial em vez do padrão motor clássico da doença de
- Delirium agudo sobreposto a demência prévia deve levar à busca de precipitantes potencialm
- A doença de Alzheimer e todas as outras demências exigem perda de memória episódica como p
- A ausência de tremor de repouso exclui doença de Parkinson, pois o tremor é obrigatório em
- Levodopa busca melhorar sintomas motores da doença de Parkinson, mas não é considerada, is
- Adulto jovem apresenta dor à movimentação ocular e perda visual monocular subaguda, associ
- Paciente com Guillain-Barré progride com fraqueza ascendente, arreflexia e disautonomia. Q
- Mulher apresenta ptose e diplopia variáveis ao longo do dia, piora com uso repetido da mus
- Em quadro com febre, cefaleia intensa, alteração da consciência e suspeita de meningite ba
- Na síndrome de Guillain-Barré, a dissociação albuminocitológica no LCR está obrigatoriamen
- Esclerose múltipla é doença desmielinizante predominantemente central; a síndrome de Guill
- Cefaleia matinal isolada é sinal patognomônico de tumor cerebral e permite dispensar exame
- Em encefalite suspeita por herpes simples, alteração de comportamento, crises e rebaixamen
```

## Pedido 22: Nutrição: alimentação normal, avaliação nutricional, dietoterapia e políticas de alimentação (`nutricao.dietoterapia-avaliacao-politicas`, 396 tópicos de edital)

```
Matéria: nutricao.dietoterapia-avaliacao-politicas (Nutrição: alimentação normal, avaliação nutricional, dietoterapia e políticas de alimentação)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da nutrição normal e planejamento dietético": 17
- "Nutrição nos ciclos de vida: gestação, lactação, infância e envelhecimento": 17
- "Avaliação do estado nutricional, inquéritos e vigilância alimentar": 17
- "Dietoterapia em doenças crônicas, digestivas e oncologia": 17
- "Terapia nutricional oral, enteral e parenteral: princípios e segurança": 16
- "PNAN, PNAE, Guia Alimentar e segurança alimentar e nutricional": 16

Já existem (não repita):
- Na avaliação das leis de Escudero, um cardápio com calorias suficientes, porém sem fontes 
- Um plano fornece 180 g de carboidratos, 90 g de proteínas e 70 g de lipídios. Qual é seu V
- Uma equipe quer estimar risco de ingestão habitual inadequada de determinado mineral em um
- Uma gestante apresenta ganho de peso diferente do esperado no acompanhamento pré-natal. Qu
- Qual orientação corresponde ao Guia Alimentar para Crianças Brasileiras Menores de 2 Anos?
- Uma idosa perde peso, tem fraqueza e dificuldades para mastigar, embora o IMC permaneça ac
- Para examinar possível comprometimento crônico do crescimento linear de uma criança, qual 
- Um paciente de 72 kg mede 1,60 m. Qual é aproximadamente seu IMC?
- Um estudo aplicou recordatório de 24 horas uma única vez a cada participante. Qual limitaç
- Um paciente hospitalizado apresenta albumina baixa e inflamação sistêmica. Qual interpreta
- Uma UBS observa elevado percentual de excesso de peso em relatórios do Sisvan. Qual interp
- Qual recomendação é mais coerente com o tratamento nutricional da hipertensão arterial?
- Em paciente com LDL elevado, qual alteração alimentar tende a contribuir para controle lip
- Uma pessoa com diabetes tipo 2 utiliza medicação com risco de hipoglicemia. Qual planejame
- Duas pessoas com doença renal crônica têm estágios e tratamentos diferentes; uma faz diáli
- Na dietoterapia de paciente com cirrose e sarcopenia, qual estratégia é mais adequada?
- Em paciente com doença celíaca confirmada, a intervenção alimentar central consiste em:
- Paciente com câncer sofre perda de peso progressiva, mucosite e ingestão reduzida. Qual co
- Paciente disfágico não consegue comer adequadamente, porém apresenta intestino funcionante
- Pessoa com desnutrição importante reinicia alimentação após longo período de ingestão míni
- Qual é a interpretação correta da Resolução CD/FNDE nº 4/2026 para aquisição de produtos d
- No âmbito do PNAE, qual agente é responsável pela elaboração técnica dos cardápios?
- Em auditoria de compras do PNAE de 2026, qual combinação reproduz os limites por grau de p
- As vitaminas A, D, E e K pertencem ao grupo das vitaminas lipossolúveis.
- O limite superior tolerável de ingestão, denominado UL, deve ser utilizado como meta mínim
- As quatro leis clássicas de Escudero incluem quantidade, qualidade, harmonia e adequação.
- Uma dieta pode suprir a necessidade energética e, ainda assim, ser inadequada devido à bai
- Segundo a orientação brasileira, até seis meses de vida o aleitamento materno exclusivo ad
- A partir de seis meses, a alimentação complementar deve ser introduzida com manutenção do 
- O ganho ponderal da gestante deve ser julgado apenas pelo mesmo ponto de corte de IMC usad
- Na pessoa idosa, sarcopenia e desnutrição podem estar presentes mesmo quando o IMC não ind
- Uma medida isolada de peso da criança é suficiente para diagnosticar déficit de cresciment
- O escore-z de um indicador antropométrico expressa a distância do valor observado em relaç
- Um relatório do Sisvan de pessoas atendidas na Atenção Primária é obrigatoriamente represe
- A ferritina pode estar aumentada em processos inflamatórios, motivo pelo qual a avaliação 
- No tratamento nutricional do diabetes tipo 2, a exclusão absoluta de todos os alimentos co
- A restrição indiscriminada de proteínas é recomendada para todos os pacientes em hemodiáli
- Para reduzir LDL-colesterol, a qualidade das gorduras consumidas e a presença de fibras so
- É tecnicamente adequado prescrever dieta sem glúten a todos os pacientes com intolerância 
- Nutrição enteral e nutrição parenteral diferem principalmente pela via de fornecimento de 
- Pacientes sob risco de síndrome de realimentação requerem planejamento de progressão da of
- O Guia Alimentar para a População Brasileira recomenda que alimentos ultraprocessados cons
- O Conselho de Alimentação Escolar substitui o nutricionista responsável técnico na elabora
- A Resolução CD/FNDE nº 4/2026 exige que ao menos 45% dos recursos financeiros federais do 
- Segurança alimentar e nutricional resume-se à ausência de microrganismos patogênicos no al
```

## Pedido 23: Resistência dos materiais e análise estrutural (`engenharia.resistencia-materiais-analise-estrutural`, 393 tópicos de edital)

```
Matéria: engenharia.resistencia-materiais-analise-estrutural (Resistência dos materiais e análise estrutural)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Equilíbrio, vínculos e análise de estruturas": 18
- "2. Tensões, deformações e comportamento dos materiais": 21
- "3. Esforços em vigas, flexão e propriedades geométricas": 23
- "4. Torção, estado plano de tensões e critérios de falha": 19
- "5. Flambagem, hiperestaticidade e segurança estrutural": 19

Já existem (não repita):
- Uma viga plana está apoiada em uma articulação fixa na extremidade esquerda e em um rolete
- Uma treliça ideal tem barras articuladas e recebe forças concentradas apenas em seus nós. 
- Uma barra prismática de seção 250 mm² sofre força axial centrada de tração de 50 kN. Qual 
- Duas barras têm comprimento, seção e tração iguais, mas o módulo de elasticidade da barra 
- Em uma viga, há um trecho sem cargas distribuídas, mas com força cortante constante e não 
- Uma seção retangular de viga mantém sua base b, mas a altura h aumenta para 2h. Em torno d
- Uma viga simplesmente apoiada de 6 m suporta carga uniformemente distribuída de 4 kN/m ao 
- Um eixo maciço circular transmite torque sem outras solicitações relevantes. Em qual posiç
- Uma coluna ideal biarticulada tem comprimento L, módulo E e inércia I. Se seu comprimento 
- Uma barra metálica de 2 m é aquecida uniformemente em 30 °C e pode se expandir livremente.
- Uma treliça plana apresenta m + r = 2j, mas duas barras e apoios foram posicionados de mod
- Uma seção retangular maciça de viga, com área A, está submetida a força cortante V. No mod
- Em determinado ponto de uma chapa delgada em estado plano de tensões, mediram-se σx = 60 M
- Duas barras prismáticas de materiais distintos estão ligadas em série, em eixo comum, e tr
- Uma viga biapoiada com rigidez EI constante recebe q uniformemente distribuído. Se seu vão
- Uma coluna biarticulada ideal é transformada em um balanço vertical engastado na base e li
- Uma barra isotrópica elástica sofre alongamento longitudinal de 0,002, com coeficiente de 
- Uma viga contínua apoiada em três pontos verticais é carregada em dois vãos. Deseja-se det
- Em ponto sujeito apenas a cisalhamento puro de 40 MPa, deseja-se aplicar o critério de von
- Um eixo circular maciço, homogêneo e de comprimento fixo tem seu diâmetro duplicado; torqu
- Um pilar comprimido por força axial N = 500 kN apresenta deslocamento lateral de 10 mm na 
- Uma viga prismática com as duas extremidades perfeitamente engastadas recebe q = 12 kN/m u
- Uma coluna prismática recebe compressão axial N = 100 kN aplicada com excentricidade de 0,
- Um ponto apresenta σx = 100 MPa, σy = 80 MPa e τxy = 0, com tensão fora do plano σz = 0. C
- Uma viga satisfaz as verificações de resistência e estabilidade adotadas, mas sua flecha s
- Uma viga plana apoiada por articulação fixa e rolete horizontal possui três componentes de
- Uma barra tracionada no regime elástico linear terá seu alongamento duplicado quando apena
- Em um trecho de viga sujeito a carregamento transversal uniformemente distribuído e sem fo
- Um eixo com seção retangular maciça sob torção pode ser analisado diretamente por τ = Tr/J
- Uma viga com resistência adequada ao estado-limite último ainda pode apresentar deslocamen
- Uma treliça ideal composta por barras biarticuladas que recebem todas as cargas nos nós ex
- Uma barra isotrópica alongada elasticamente, com superfícies laterais livres e coeficiente
- Em uma viga simplesmente apoiada submetida a q constante, dobrar o vão e reduzir q pela me
- Em uma chapa sob σx = σy = 40 MPa e τxy = 20 MPa, as duas tensões principais no plano são 
- Em eixo circular vazado de diâmetros externo D e interno d, o momento polar geométrico é J
- Ao substituir apoios biarticulados ideais por dois engastes perfeitos de uma coluna sem al
- Uma peça de concreto sob carga mantida por período prolongado pode desenvolver deformação 
- Aplicar uma carga vertical pontual no interior de viga contínua provoca necessariamente um
- Em estado plano com σx = 60 MPa, σy = 20 MPa e τxy = 0, o cisalhamento máximo absoluto tri
- No estado de cisalhamento puro de 50 MPa, a tensão equivalente de von Mises é também 50 MP
- Em viga prismática, perfeitamente engastada em ambas as pontas e carregada uniformemente e
- No método das forças aplicado a estrutura hiperestática, uma reação redundante pode ser re
- Quando a tensão máxima de serviço permanece abaixo da resistência estática de uma peça met
- Na verificação de uma estrutura sob ações permanentes e diversas ações variáveis, a escolh
- Em pilar comprimido e esbelto, basta verificar que N/A está abaixo do limite de escoamento
```

## Pedido 24: Compliance, integridade e governança corporativa (`conhecimentos-bancarios.compliance-integridade-governanca`, 391 tópicos de edital)

```
Matéria: conhecimentos-bancarios.compliance-integridade-governanca (Compliance, integridade e governança corporativa)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Governança corporativa, controles e Lei das Estatais": 18
- "2. Compliance, programas de integridade e proteção ao denunciante": 23
- "3. Códigos institucionais, estatutos, autorregulação e responsabilidade socioambiental": 26
- "4. Convenções anticorrupção e cooperação internacional": 14
- "5. Segurança cibernética, computação em nuvem e controles bancários": 19

Já existem (não repita):
- Durante a revisão de uma política bancária, a equipe adota a 6ª edição do Código das Melho
- A diretoria de uma instituição pretende atribuir à auditoria interna a execução diária das
- Um relatório descreve o Banco do Brasil como empresa pública de capital integralmente esta
- Uma empresa estatal atualiza seu Código de Conduta e Integridade. O que o art. 9º, § 1º, d
- Na integração de novos empregados da CAIXA, uma equipe precisa apresentar os cinco valores
- Um projeto de crédito da CAIXA terá efeitos sobre comunidades e recursos naturais. Qual in
- Uma instituição aderiu ao Sistema de Autorregulação Bancária e recebeu pergunta sobre a re
- Um relatório investiga mecanismos de recuperação de ativos desviados por corrupção e busca
- O setor de segurança de um banco descreve os três objetivos centrais de sua política ciber
- Durante o planejamento da PRSAC de uma instituição regulada, a direção busca o ato do CMN 
- O diretor-presidente de uma estatal bancária é suspeito de irregularidade e deixa de deter
- Uma equipe compara o programa de integridade de uma empresa com o Decreto nº 11.129/2022. 
- Uma sociedade de economia mista exige orientação sobre quantidade de membros do conselho d
- Ao examinar o Estatuto Social do Banco da Amazônia e a página oficial de suas políticas, u
- Uma empresa brasileira pretende pagar vantagem indevida a funcionário de governo estrangei
- A instituição prepara seu calendário anual de cibersegurança. Até quando deve apresentar o
- Ao investigar fraudes que envolvem estrutura estável de quatro participantes, países disti
- Uma estatal redige Código de Conduta com canal de denúncias, mas prevê treinamento de empr
- Após incidente com um provedor de nuvem contratado, o banco sustenta que transferiu toda a
- Uma instituição deseja classificar adesão à autorregulação conforme níveis do sistema divu
- Conselho de estatal aprova mandato unificado de três anos, permitindo quatro reconduções c
- Uma política de privacidade de ouvidoria invoca prazo de cem anos para restringir elemento
- Analista do Banco da Amazônia confunde o código de ética com o estatuto social e sustenta 
- A política cibernética de banco aprovado pelo conselho prevê revisão documental e de plano
- Uma empresa decide avaliar somente a ocorrência de pagamentos indevidos e ignora preventiv
- Na definição de política de governança de um banco, a responsabilidade cotidiana pelos con
- Uma sociedade de economia mista instala canal de denúncias, mas determina que somente os e
- A apresentação dos valores éticos da CAIXA deve mencionar respeito, honestidade, compromis
- Por estabelecer padrões de conduta aceitos voluntariamente, o sistema de autorregulação ba
- Uma análise de risco em negócios internacionais pode utilizar a Convenção da OCDE promulga
- Ao elaborar política de segurança cibernética, uma instituição pode substituir o dever de 
- Quando houver suspeita de envolvimento do diretor-presidente em irregularidade, o estatuto
- Ao aplicar o Decreto nº 11.129/2022 a uma empresa, é suficiente verificar se ela publicou 
- Uma instituição classifica como risco climático físico a possibilidade de enchente afetar 
- Um analista compara os decretos de promulgação e conclui que o Decreto nº 5.015/2004 promu
- Na Resolução CMN nº 4.893/2021, a política cibernética e o plano de ação e resposta a inci
- A Resolução CMN nº 4.893/2021 determina que a política de segurança cibernética seja aprov
- O Código de Conduta Ética divulgado pelo Banco da Amazônia fundamenta suas orientações em 
- Na aplicação da Lei nº 13.608/2018, a proteção do informante exige que toda declaração rec
- Uma sociedade de economia mista deverá possuir Comitê de Auditoria Estatutário em sua estr
- A regra atual do Sistema de Autorregulação Bancária classifica como nível II a instituição
- A atualização da Resolução CMN nº 4.893/2021 pela Resolução nº 5.274/2025 ampliou procedim
- Uma instituição que contrata serviço relevante de computação em nuvem no exterior fica dis
- A Convenção de Palermo, promulgada pelo Decreto nº 5.015/2004, define em seu art. 2º grupo
- A Lei nº 13.303/2016, art. 8º, VII, exige revisão da política de transações com partes rel
```

## Pedido 25: Geometria analítica (`matematica.geometria-analitica`, 390 tópicos de edital)

```
Matéria: matematica.geometria-analitica (Geometria analítica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Plano cartesiano, distâncias, ponto médio e alinhamento": 45
- "2. Estudo da reta: equações, conversões, interseções e ângulos": 14
- "3. Distâncias de ponto a reta, retas paralelas e inequações planas": 14
- "4. Circunferência: centro, raio, equações e posições relativas": 14
- "5. Cônicas no plano: elipse, hipérbole e parábola": 13

Já existem (não repita):
- Em um mapa cartesiano, dois marcos são A=(1,2) e B=(5,5). Qual é a distância euclidiana en
- Um segmento tem extremidades A=(−2,4) e B=(6,0). Qual ponto divide esse segmento em duas p
- Um ponto P possui coordenadas (−3,4). Sem alterar a orientação usual dos eixos, em qual qu
- Uma reta é descrita por 2x−y+3=0. Qual é sua equação reduzida, com y isolado no primeiro m
- Uma reta tem equação 2x+3y−12=0. Quais são, respectivamente, seus interceptos com os eixos
- Qual é a distância entre o ponto P=(0,0) e a reta de equação 3x+4y−5=0?
- A circunferência x²+y²−4x+6y−12=0 possui qual centro e qual raio?
- A elipse x²/25+y²/16=1 tem centro na origem e eixo maior horizontal. Qual é a excentricida
- Em uma planta, A=(1,1), B=(3,3) e C=(5,5) representam três marcos. Qual conclusão segue do
- Três vértices de um terreno triangular são A=(0,0), B=(6,0) e C=(2,4). Qual é a área do te
- Duas rotas retilíneas são dadas por r: y=2x+1 e s: y=−x+7. Qual é o ponto de interseção?
- Uma reta tem coeficiente angular −2. Para que outra reta não vertical seja perpendicular a
- Duas retas passam pela origem: a primeira é horizontal e a segunda tem equação y=x. Qual é
- Um corredor é delimitado pelas retas paralelas 3x+4y−5=0 e 3x+4y+10=0. Qual distância perp
- A região do plano é dada por 2x+y≤4. Quais das afirmações descrevem corretamente o ponto P
- Uma parábola tem equação x²=12y, vértice na origem e eixo vertical. Quais são foco e diret
- Uma circunferência de centro O=(0,0) e raio 5 é confrontada com a reta 3x+4y−25=0. Qual é 
- O ponto P divide internamente o segmento de A=(−2,1) a B=(10,7) na razão AP:PB=1:2. Quais 
- Uma reta passa por A=(−1,2) e B=(3,0). Qual equação geral com coeficientes inteiros mínimo
- Dado P=(3,−1) e r: 2x−y+4=0, qual é a distância exata entre o ponto e a reta?
- Duas circunferências têm raios 5 e 3 e centros (0,0) e (8,0). Como se classificam suas pos
- A elipse (x−1)²/25+(y+2)²/9=1 tem eixo maior horizontal. Quais são as coordenadas de seus 
- Para a hipérbole x²/16−y²/9=1, com centro na origem, quais são seus focos e suas assíntota
- A parábola (y−2)²=−8(x−3) tem vértice V=(3,2). Quais são o foco e a diretriz, respectivame
- Duas retas são r: 2x+2y−2=0 e s: x+y+3=0. Qual é a menor distância entre elas?
- Um ponto P=(−2,−3), com ambas as coordenadas negativas, está no terceiro quadrante do plan
- O ponto médio do segmento de extremidades A=(2,0) e B=(8,4) é M=(5,4), pois a ordenada do 
- Os pontos A=(0,0), B=(2,2) e C=(4,4) são alinhados, uma vez que o determinante dos vetores
- A reta vertical x=2 pode ser expressa na forma reduzida y=mx+n tomando m=0, pois não há va
- A reta 3x+2y−12=0 pode ser escrita como x/4+y/6=1, pois cruza os eixos em (4,0) e (0,6).
- O ponto O=(0,0) está a uma distância de 10 unidades da reta 3x+4y−10=0, porque o módulo do
- Na circunferência (x−2)²+(y+3)²=16, o centro é (2,−3) e o raio tem comprimento quatro unid
- Duas retas não verticais de coeficientes angulares 2 e −1/2 são paralelas, porque o produt
- O triângulo cujos vértices são A=(0,0), B=(4,0) e C=(1,3) possui área igual a seis unidade
- Multiplicar a desigualdade 2x+y≤4 por −1 produz a condição equivalente −2x−y≤−4, preservan
- A reta y=3 é secante à circunferência x²+y²=25, intersectando-a nos pontos (−4,3) e (4,3).
- Na elipse x²/36+y²/20=1, o parâmetro focal é c=√56, porque se devem somar os denominadores
- A parábola y²=−12x tem vértice na origem, foco (−3,0) e diretriz x=3, com abertura para a 
- Na equação x²+y²−6x+2y+1=0, o raio vale √11, pois basta somar as parcelas quadráticas do c
- As retas y=x e y=−x são perpendiculares entre si, pois seus coeficientes angulares têm pro
- Para a hipérbole x²/9−y²/16=1, a distância do centro a cada foco é obtida pela raiz de 9 m
- Uma reta secante a uma circunferência de raio cinco fica a três unidades do centro. O comp
- Na elipse horizontal x²/25+y²/9=1, as diretrizes são as retas x=±5/4, pois a posição da di
- A região plana definida simultaneamente por x≥0, y≥0 e x+y≤4 é um triângulo fechado cuja á
- A parábola (x−2)²=−4(y+1) tem vértice (2,−1), foco (2,0) e diretriz y=−2, com concavidade 
```

## Pedido 26: Gestão de segurança da informação: ISO/IEC 27001 e 27002 (`informatica.seguranca-normas-iso27001`, 389 tópicos de edital)

```
Matéria: informatica.seguranca-normas-iso27001 (Gestão de segurança da informação: ISO/IEC 27001 e 27002)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Princípios, ativos e estrutura de governança da segurança": 27
- "2. SGSI e ISO/IEC 27001:2022: requisitos, riscos e auditoria": 16
- "3. Processo de riscos: identificação, matriz, tratamento e aceitação": 19
- "4. ISO/IEC 27002:2022: temas, controles, versões e incidentes": 21
- "5. Continuidade de negócios, RTO, RPO e melhoria do SGSI": 17

Já existem (não repita):
- Uma equipe descobre que um relatório reservado foi consultado por pessoa sem autorização, 
- Um serviço mantém funcionamento normal, mas um valor de pagamento é alterado silenciosamen
- Uma instituição quer certificar seu sistema de gestão de segurança e consulta duas normas 
- Antes de implantar o SGSI, uma organização identifica interessados, exigências pertinentes
- Após avaliar riscos, uma equipe deve registrar os controles necessários, suas justificativ
- Em treinamento sobre a ISO/IEC 27002:2022, alguém pergunta a quantidade e os quatro temas 
- Uma organização avalia o grau de sensibilidade de documentos e, em seguida, aplica rótulos
- Um SGSI aprovado passa à execução dos processos e à implementação do plano de tratamento d
- A gestão acompanha tempo de revogação de acessos, resultados de auditoria e revisão do SGS
- Após auditoria do SGSI, a alta direção examina métricas, mudanças de contexto, suficiência
- Uma matriz interna define risco como probabilidade multiplicada por impacto, ambos de 1 a 
- Uma equipe reduziu o risco de exposição indevida de 20 para 8 pontos. O critério aprovado 
- Ao atualizar o inventário de controles, uma equipe encontra dois conjuntos históricos: 114
- Um SGSI organiza controles por atributos como preventivo, detectivo ou corretivo e pelo co
- Uma organização pretende estruturar um sistema de gestão de informações de privacidade com
- Um sistema registra dezenas de tentativas incomuns de autenticação. Antes de declarar inci
- Um serviço essencial possui meta RTO de 3 horas e RPO de 20 minutos. Qual interpretação de
- Uma organização mapeia dependências de aplicações, provedores externos e infraestrutura, t
- No tratamento de riscos, a equipe propõe um controle específico desenvolvido internamente,
- Uma pessoa desenvolveu sozinha um procedimento de revisão de permissões e foi designada pa
- A organização adota matriz de probabilidade × impacto, de 1 a 5. Um risco parte de 5 × 4 e
- Ao revisar controles antigos, uma equipe procura pares de controles reconhecidos como novo
- Durante revisão do contexto do SGSI, discute-se a Emenda 1:2024 da ISO/IEC 27001. Uma orga
- No cenário de risco 'divulgação indevida de registros', foram anotados ativo: base de clie
- O serviço tem RTO de 2 horas e RPO de 15 minutos. Após uma interrupção, volta em 1 hora e 
- Durante uma interrupção, usuários autorizados deixam de conseguir consultar informações es
- Uma equipe de segurança registrou todos os equipamentos no inventário e concluiu que a cla
- Ao estruturar o SGSI, uma organização usa a ISO/IEC 27001:2022 para requisitos e a ISO/IEC
- Uma organização utiliza a ISO/IEC 27001:2022 e exclui integralmente a cláusula 9, alegando
- No inventário de informações, o responsável classifica um documento por sensibilidade e a 
- Uma equipe definiu RTO de quatro horas para um serviço. Dessa decisão decorre que quatro h
- Um plano de riscos registra a exposição antes dos controles e, depois de implantá-los, rea
- Uma organização usa uma matriz de cinco níveis de impacto e conclui que a ISO/IEC 27001 ex
- Ao implantar política de segurança, uma organização obtém aprovação da direção, comunica a
- Durante auditoria de segurança, a direção decidiu que uma revisão gerencial de indicadores
- Depois de repetidas tentativas incomuns de acesso, o responsável avalia sinais e contexto 
- Durante resposta a incidente, a equipe pode apagar integralmente os registros de acesso an
- Ao atualizar referências de auditoria, uma equipe constata que a edição 2022 da ISO/IEC 27
- Uma instituição adota ISO/IEC 27701:2025 e afirma que essa edição, como a de 2019, só pode
- Na preparação da Declaração de Aplicabilidade, a organização pode incluir controles adicio
- Após falha recorrente em revisão de acessos, a equipe restabelece as permissões corretas e
- Um serviço com RTO de sessenta minutos e RPO de cinco minutos é restabelecido após cinquen
- Uma cópia de segurança foi restaurada com sucesso, de modo que a organização concluiu ter 
- Uma organização limita o acesso por função em sua política e acompanha separadamente conta
- Uma empresa transfere contratualmente parte das consequências financeiras de um incidente 
```

## Pedido 27: Assédio, discriminação e perspectiva de gênero no Judiciário (`direito.assedio-discriminacao-genero-judiciario`, 389 tópicos de edital)

```
Matéria: direito.assedio-discriminacao-genero-judiciario (Assédio, discriminação e perspectiva de gênero no Judiciário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, ambientes de trabalho e Convenção 190 da OIT": 18
- "2. Proteção contra discriminação e legislação federal": 13
- "3. Políticas de prevenção na administração pública e legislação estadual": 15
- "4. Políticas do Poder Judiciário e da Justiça do Trabalho": 21
- "5. Julgamento com perspectiva de gênero e equidade institucional": 33

Já existem (não repita):
- Durante uma reunião, chefia submete semanalmente um empregado a humilhações e o exclui de 
- Uma funcionária recebe insistentes mensagens de teor sexual de colega da mesma posição hie
- Uma candidata recebe exigência de exame de gravidez como condição para ingresso na relação
- Após ser despedido por motivo discriminatório, um empregado busca os efeitos legais previs
- Um trabalhador é pessoalmente ofendido em razão de sua cor. Qual dispositivo passou a trat
- Ao atualizar uma cartilha, uma equipe registra que a Convenção 190 da OIT já estava incorp
- Uma pessoa com 62 anos pede a proteção do Estatuto da Pessoa Idosa. A administração indefe
- Gestores municipais querem identificar norma que estabelece programa de prevenção ao asséd
- Após uma capacitação sobre prevenção de violência sexual, o setor de pessoal pergunta quan
- O setor responsável pelo programa instituído pelo Decreto 12.122/2024 deve registrar as qu
- Após a publicação do plano federal do Decreto 12.122/2024, o órgão deve instituir seu plan
- Em contrato federal de serviço contínuo, com dedicação exclusiva de mão de obra e 40 colab
- Uma empresa terceirizada fornece serviços a órgão federal e seu empregado é acusado de ass
- Um colegiado estadual quer conhecer a classificação expressa de modalidades de assédio na 
- Em 2026, um trabalhador terceirizado informa que as diretrizes de proteção da Resolução CN
- Ao identificar um caso de assédio sexual grave, a autoridade consulta a disciplina de grav
- Uma comissão regional elabora quadro das resoluções CSJT de 2023. Qual par associa correta
- Uma pessoa investiga a política local de prevenção na 7ª Região e sua atualização de 2024.
- Uma unidade do Judiciário considera o Protocolo para Julgamento com Perspectiva de Gênero 
- Em julgamento de relato de violência de gênero, uma decisão descarta a narrativa exclusiva
- Uma decisão registra que marcadores de gênero, raça e deficiência podem interagir no acess
- Um tribunal avalia os dois colegiados mencionados na Resolução CNJ 492/2023. Qual divisão 
- A equipe de integridade informa que a Resolução CNJ 351/2020 não possui aplicação possível
- O órgão de Pernambuco analisa duas regras da Lei 13.314/2007 atualizada. Qual afirmativa d
- Um contratante federal exige que candidatas comprovem diretamente à empresa sua condição d
- O registro de que uma chefia insultou um servidor por sua origem e o impediu de participar
- O simples fato de a vítima e o autor de assédio sexual administrativo ocuparem cargos de m
- O art. 1º da Lei 10.741/2003 garante proteção à pessoa com 60 anos de idade, ainda que ela
- A Lei 9.029/1995 permite que empresa exija exame de gravidez na admissão quando a candidat
- O art. 1º da Lei 14.583/2023 determina a difusão institucional de direitos fundamentais e 
- A Lei 14.540/2023 reserva sua aplicação exclusivamente a órgãos da administração pública f
- O Decreto 12.122/2024 prevê o sigilo de dados pessoais de envolvidos em denúncias perante 
- No Decreto 12.122/2024, a administração pode encaminhar a denúncia contra trabalhador terc
- O prazo de 120 dias para instituir planos setoriais no Decreto 12.122/2024 é contado da pu
- A obrigação de conservar por cinco anos, na Lei 14.540/2023, refere-se precisamente aos re
- Em contrato federal com 18 colaboradores em dedicação exclusiva, o Decreto 11.430/2023, ap
- O sistema cearense de prevenção e combate ao assédio moral prevê Comissão Central e Comiss
- Na classificação da Lei pernambucana 13.314/2007, assédio vertical ascendente parte de sub
- A Resolução CNJ 671/2026 retirou da Resolução 351/2020 a proteção de pessoas terceirizadas
- O art. 17, § 2º, da Resolução CNJ 351/2020, na redação dada pela Resolução 538/2023, consi
- A Resolução CSJT 368/2023 instituiu a política de prevenção da violência e do assédio na J
- Uma unidade da 8ª Região pode substituir o estudo da Resolução TRT8 065/2025 pela Resoluçã
- Aplicar a perspectiva de gênero significa obrigatoriamente acolher a narrativa de uma part
- Uma pessoa pode enfrentar barreiras conjugadas de raça, gênero e deficiência; o olhar inte
- Em outubro de 2026, a aprovação da Convenção 190 pela Câmara bastava para declará-la ratif
```

## Pedido 28: Linux: conceitos, diretórios, permissões e comandos (`informatica.linux`, 387 tópicos de edital)

```
Matéria: informatica.linux (Linux: conceitos, diretórios, permissões e comandos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Linux, software livre, licenças, distribuições e interfaces": 20
- "2. Hierarquia, caminhos, links, sistemas de arquivos, montagem e inicialização": 19
- "3. Navegação, arquivos, buscas, manuais, redirecionamento e pipe": 21
- "4. Usuários, grupos, permissões, umask e controle de acesso": 19
- "5. Pacotes, processos e noções práticas de administração Linux": 21

Já existem (não repita):
- Uma instalação permite iniciar programas, gerenciar memória e dispositivos e executar proc
- Uma empresa vende cópias de software e permite que usuários estudem, alterem e redistribua
- Uma pessoa trabalha em sessão Linux e deseja distinguir a raiz do sistema do diretório pes
- Ao listar um diretório, arquivos iniciados por ponto não apareceram no resultado padrão. Q
- Uma pessoa precisa consultar o diretório atual sem alterar localização, criar arquivo ou l
- Uma estrutura `projetos/ano/relatorios` não existe e deve ser criada em uma única chamada 
- O arquivo `dados.txt` já existe e deve continuar com o conteúdo original; deseja-se criar 
- Em `ls -l`, um arquivo apresenta `-rwxr-x---`. Quais permissões são expressas pela forma o
- Um administrador deseja alterar o proprietário de `relatorio.txt` para `lia` e seu grupo p
- Em máquina Debian, uma pessoa deseja atualizar a lista de versões disponíveis nos repositó
- Uma biblioteca é licenciada sob LGPL; uma aplicação separada a utiliza, respeitadas as con
- Um laboratório usa SLES e precisa identificar a família de pacotes e o gerenciador de repo
- Uma pessoa está em `/home/lia` e executa `cd documentos`, existindo a pasta `documentos` n
- Foi criado `ln -s /dados/relatorio link.txt`. Depois, o arquivo original foi removido, man
- É preciso localizar arquivos com nomes terminados em `.log` em uma árvore, sem depender de
- Um comando gera relatório e a saída deve ser acrescentada ao final de `historico.txt`, pre
- Num shell convencional, usa-se `umask 027` antes de criar arquivo com base 666 e diretório
- Um registro de conta apresenta o nome de usuário, UID, GID, diretório pessoal e shell; has
- Em sistema que utiliza systemd, um serviço está parado e deve iniciar agora e também autom
- Um processo de PID conhecido deve receber sinal padrão de encerramento para poder realizar
- Uma aplicação usa software com GPL em processo separado na mesma máquina, sem incorporação
- Dois nomes são hard links de um mesmo arquivo regular no mesmo sistema de arquivos. Um dos
- Um usuário tem permissão de escrita sobre o conteúdo de `relatorio.txt`, mas não possui pe
- Uma instalação tem `PV` físico, `VG` formado com esse PV e `LV` criado do grupo. Uma aplic
- Uma instalação RHEL apresenta falha de acesso apesar dos bits rwx permitirem a operação. H
- Uma distribuição GNU/Linux pode funcionar sem GNOME ou outro ambiente gráfico; o kernel e 
- Uma instalação Linux normalmente coloca arquivos de configuração do sistema em `/dev`, enq
- Se `anotacao.txt` já contém texto, a execução comum de `touch anotacao.txt` atualiza marca
- Uma conta Linux recebe permissão `644` sobre um arquivo regular. Esse modo concede leitura
- Em uma família Debian/Ubuntu, `apt install nome` consulta as fontes configuradas e pode re
- Um programa foi distribuído gratuitamente, mas seus usuários não podem consultar o código-
- Em sistema com a estrutura usual Linux, `/proc` pode expor informações virtuais sobre proc
- Executar `grep -n 'erro' relatorio.txt` procura arquivos cujo nome contenha a palavra 'err
- Para arquivo criado com modo-base 666, sob `umask 022` e sem ACLs que interfiram, o result
- Se um serviço systemd foi habilitado com `systemctl enable exemplo.service`, então necessa
- Em um projeto que distribui biblioteca sob LGPL, respeitar suas condições de redistribuiçã
- CentOS Stream, Oracle Linux e RHEL são necessariamente a mesma distribuição binária, com a
- Em um Linux com hierarquia compatível com FHS, `/bin` pode ser implementado como link para
- Um link simbólico apontando para `/dados/base` continuará obrigatoriamente acessando os da
- No comando `find /home -type f -name '*.txt'`, o padrão entre aspas chega ao próprio `find
- Um arquivo regular tem permissão `rw-` para determinado usuário, mas o usuário não possui 
- Em uma instalação convencional, `umask 027` tende a produzir diretórios com modo 750 e arq
- Para instalar pacote local `.deb`, `dpkg -i pacote.deb` resolve automaticamente todas as d
- Ao executar `kill -9 PID` em processo de usuário, envia-se SIGKILL, sinal não capturável p
- Em crontab pessoal, a expressão `0 7 * * * comando` determina uma execução a cada sete min
- No iptables, a cadeia INPUT trata os pacotes destinados ao próprio host, e a cadeia FORWAR
- No iptables, o alvo que descarta o pacote silenciosamente, sem avisar a origem, é:
```

## Pedido 29: Estoques e ativo imobilizado (`contabilidade.estoques-ativo-imobilizado`, 387 tópicos de edital)

```
Matéria: contabilidade.estoques-ativo-imobilizado (Estoques e ativo imobilizado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estoques, custos de aquisição e inventários": 18
- "2. Critérios de custeio: preço específico, PEPS, média e UEPS": 17
- "3. CMV, CPV, CSP e valor realizável líquido": 16
- "4. Ativo imobilizado, depreciação e exaustão": 27
- "5. Baixa, impairment e ativos intangíveis": 22

Já existem (não repita):
- Uma empresa de comércio compra computadores idênticos: parte será revendida e um deles ser
- Uma revendedora usa inventário periódico e apresenta estoque inicial de R$ 4.000, compras 
- Uma aquisição inclui R$ 900 de tributos que, segundo o enunciado, geram créditos integralm
- Mercadorias custaram R$ 8.000, com frete de compra de R$ 300 adicional e desconto comercia
- Uma entidade mantém registros permanentes de estoque, mas no encerramento faz contagem par
- Um depósito tem dez unidades a R$ 10 e recebe mais dez a R$ 14. Após venda de doze unidade
- No mesmo depósito, dez unidades custavam R$ 10 e outras dez chegaram a R$ 14 antes de vend
- Na comparação exclusivamente didática de dez unidades a R$ 10 e dez a R$ 14, com saída de 
- Em controle por média móvel, existem cinco unidades a R$ 8 e entram cinco a R$ 12. Vendem-
- Uma empresa comercializa peças artísticas únicas com etiquetas que identificam os respecti
- O estoque inicial de uma loja é R$ 8.000; compras brutas R$ 25.000, devoluções R$ 2.000, d
- Uma matéria-prima caiu de preço, mas será utilizada em produtos acabados cuja venda deverá
- Um lote foi contabilizado ao custo de R$ 5.000. O preço esperado de venda caiu para R$ 4.8
- Um item custou R$ 100 e sofreu redução ao VRL de R$ 70. No exercício seguinte, o VRL compr
- Uma indústria apresenta estoque inicial de produtos acabados de R$ 15.000, custo da produç
- Uma máquina de uso produtivo tem preço líquido de R$ 50.000, transporte R$ 2.000, instalaç
- Um equipamento foi comprado em março, chegou em abril, ficou instalado e pronto para funci
- Equipamento pronto para uso custa R$ 120.000, tem valor residual de R$ 20.000 e vida útil 
- Após depreciações anteriores, a máquina apresenta valor contábil R$ 70.000. Ao fim do exer
- Uma reserva mineral tem direito de exploração contabilizado com base exaurível de R$ 900.0
- Um equipamento tem valor contábil R$ 110.000, valor em uso R$ 85.000 e valor justo líquido
- Uma máquina que custou R$ 100.000 tem depreciação acumulada R$ 35.000 e perda acumulada po
- Uma máquina depende de software exclusivo sem o qual não funciona. Outro software, comerci
- Uma entidade realiza pesquisa científica e, depois, inicia fase de desenvolvimento de prod
- Um direito intangível atende aos requisitos de reconhecimento, mas a análise dos fatores r
- Em uma revendedora que usa inventário periódico, o custo de mercadorias vendidas pode ser 
- Para mercadorias homogêneas e intercambiáveis, PEPS e UEPS são igualmente admitidos pelo C
- O frete necessário para transportar mercadorias adquiridas ao local de armazenamento, se d
- Uma entidade que atualiza entradas e saídas continuamente está dispensada de realizar cont
- Quando se vende mercadoria mantida em estoque permanente, o reconhecimento da receita é di
- Se uma indústria fabricar muito abaixo da capacidade normal, todos os custos indiretos fix
- Custos das mercadorias vendidas, dos produtos vendidos e dos serviços prestados representa
- A média ponderada móvel é recalculada em cada venda mesmo sem novas entradas de mercadoria
- Com preços de aquisição crescentes, o PEPS tende a manter no estoque final camadas de cust
- Uma mercadoria com custo de R$ 500 e preço provável de venda de R$ 480, com gastos necessá
- Uma máquina comprada para utilização duradoura pode incluir transporte e instalação no cus
- Terreno de vida útil ordinariamente ilimitada deve ser depreciado na mesma proporção que o
- Uma máquina instalada em janeiro ficou pronta para uso em fevereiro e só operou em abril. 
- Ao revisar vida útil e valor residual do imobilizado, a entidade deve recalcular retrospec
- Um direito de exploração mineral com base exaurível de R$ 600.000 e reserva estimada de 20
- Uma máquina em depreciação linear, temporariamente ociosa embora pronta para uso, tem sua 
- Um ativo com valor contábil de R$ 150.000, valor em uso de R$ 130.000 e valor justo líquid
- Gastos realizados na fase de pesquisa de um projeto podem ser capitalizados como intangíve
- Quando uma empresa vende uma unidade industrial personalizada cujo custo está individualme
- Uma licença intangível de vida útil indefinida deve ser amortizada pelo prazo máximo arbit
```

## Pedido 30: Métodos adequados de solução de conflitos: conciliação, mediação e arbitragem (`direito-processual-civil.mediacao-arbitragem`, 386 tópicos de edital)

```
Matéria: direito-processual-civil.mediacao-arbitragem (Métodos adequados de solução de conflitos: conciliação, mediação e arbitragem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema multiportas, autotutela, autocomposição e heterocomposição": 20
- "2. CPC: conciliadores, mediadores, cadastros e política pública": 16
- "3. Audiência do CPC e procedimento da Lei de Mediação": 31
- "4. Arbitragem: convenção, árbitros, procedimento e urgência": 16
- "5. Sentença arbitral, invalidação e consensualidade administrativa": 17

Já existem (não repita):
- Duas pessoas discutem o pagamento de reparo, sem facilitador, e decidem dividir a despesa.
- Um contrato empresarial prevê arbitragem para divergências futuras. Quando surge o conflit
- Um proprietário pretende buscar judicialmente toda discordância contratual, alegando que a
- Vizinhos com histórico de conflitos procuram um facilitador para reorganizar a comunicação
- Após um desacordo pontual sobre entrega de mercadoria, um facilitador sugere opções de par
- Ao preparar sessão judicial, um mediador explica que os interessados poderão estabelecer r
- As partes concordam em utilizar mediador de sua confiança, não inscrito no cadastro do tri
- Conciliador atuou na última audiência de um conflito e pretende, três meses depois, ser ad
- Um centro atende interessados antes e depois da propositura de ações e ainda oferece orien
- Após alteração normativa de 2026, um tribunal revê os fluxos das sessões e o registro de a
- O juiz vai designar audiência de conciliação após receber petição inicial apta, sem improc
- O autor declara na inicial que não deseja acordo. O réu, no entanto, ainda não expressou s
- Em processo com sessões consensuais sucessivas, a última terminou sem composição. De acord
- Pais e responsáveis discutem uma obrigação familiar indisponível que admite transação e en
- Uma pessoa graduada há três anos, capacitada em escola reconhecida, apresenta-se para medi
- Uma sociedade convida outra a mediação extrajudicial. Não há resposta durante trinta dias 
- Dois sócios inserem no contrato uma convenção para submeter à arbitragem disputas que poss
- Em contrato com cláusula arbitral, uma das partes alega nulidade do instrumento contratual
- Antes de instituída a arbitragem, uma parte obtém tutela de urgência judicial. A medida é 
- Uma arbitragem é instaurada mediante pedido em janeiro; os árbitros aceitam suas nomeações
- Uma entidade pública inclui convenção arbitral em contrato para controvérsia patrimonial d
- Uma sentença arbitral nacional foi notificada. A parte aponta contradição e, depois, tenta
- Dois brasileiros submetem disputa a tribunal arbitral que profere sentença em outro país. 
- Uma câmara administrativa recebe pedido de composição sobre reequilíbrio econômico-finance
- Em uma controvérsia coletiva, o órgão ministerial incentiva negociação, mediação e prática
- Ao orientar pessoas em conflito contratual, um profissional afirma que o CPC incentiva a c
- Uma pessoa que recebe proposta de acordo pode exigir o pagamento pretendido por força próp
- Um mediador convocado para controvérsia entre vizinhos poderá impor a solução que reputar 
- Depois de uma sessão, um conciliador se recusa a revelar proposta confidencial produzida p
- Um conciliador cadastrado que também é advogado pode atuar como patrono de processos nos m
- Uma entidade federativa cria uma câmara para facilitar composição entre órgãos administrat
- Após a Resolução CNJ 672/2026, uma sessão consensual pré-processual realizada em CEJUSC de
- Em ação que admite autocomposição, a declaração de desinteresse formulada exclusivamente p
- Réu e autor comparecem à terceira e última sessão de conciliação, mas não há acordo. Em re
- Duas sociedades pactuam cláusula de mediação; uma comparece à primeira reunião e comunica 
- Em discussão sobre direito indisponível que admite transação, qualquer acordo de mediação 
- Um candidato à mediação judicial concluiu curso superior há mais de dois anos e obteve cap
- Durante mediação, uma parte relata indícios de crime de ação pública. O mediador afirma qu
- Em mediação extrajudicial complexa, o convite não respondido em trinta dias considera-se r
- Em contrato com cláusula compromissória, a declaração de nulidade do contrato principal ac
- As partes indicam inicialmente dois árbitros, os quais concordam em designar um terceiro. 
- Uma tutela urgente foi concedida judicialmente antes de instituída a arbitragem; após a in
- Mesmo que as partes fixem expressamente prazo de doze meses, uma sentença arbitral apresen
- Uma sentença arbitral omite um pedido. A parte pretende ajuizar ação de nulidade para disc
- Uma sentença arbitral é proferida fora do Brasil entre partes brasileiras, e outro conflit
```

## Pedido 31: Desenho técnico e CAD/BIM (`engenharia.desenho-tecnico-cad`, 385 tópicos de edital)

```
Matéria: engenharia.desenho-tecnico-cad (Desenho técnico e CAD/BIM)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Linguagem gráfica: vistas, cortes, linhas e cotagem": 29
- "2. Escalas, formatos e precisão de representação": 21
- "3. AutoCAD: geometria, coordenadas, camadas e layouts": 17
- "4. Revit: modelagem paramétrica e documentação integrada": 17
- "5. BIM: processos colaborativos, interoperabilidade e aplicação": 16

Já existem (não repita):
- Em uma folha de desenho técnico, a aresta posterior de uma peça não é visível na vista fro
- Uma sala tem comprimento real de 4 m e será representada em planta na escala 1:50. Qual se
- Uma equipe vai construir a geometria real de um pavimento no AutoCAD e posteriormente impr
- Em projeto no Revit, a largura foi definida como propriedade de tipo de uma família de por
- Uma equipe constrói modelo 3D visualmente sofisticado e não mantém atributos, revisões nem
- Uma peça de 18 mm será desenhada na escala 3:1, com ampliação para mostrar detalhes. Qual 
- Um desenhista deseja controlar separadamente a visibilidade de eixos e cotas, sem apagar a
- Uma equipe utiliza o Revit para gerar uma tabela de portas com largura, tipo e pavimento. 
- A mesma prancha mostra planta a 1:100 e detalhe construtivo a 1:20. Um ponto do detalhe me
- Um arquivo CAD contém linhas que formam uma linha de tubulação. Para criar uma entidade co
- No AutoCAD, o ponto anterior é (120,80) e o operador digita @30,25 como próximo ponto cart
- Um projeto exige representar a vista lateral direita de um objeto no terceiro diedro. Onde
- Uma folha PDF foi reduzida uniformemente a 80% depois de exportada com escala numérica 1:5
- Uma porta do Revit está com elevação incorreta em um único ambiente, embora as demais do m
- Um projeto foi desenhado no AutoCAD com blocos de mobiliário e uma referência externa de i
- O coordenador solicita exportar modelo de arquitetura para interoperar com software de out
- Em uma prancha de fabricação, a equipe precisa destacar material atravessado por plano sec
- Uma planta em escala 1:75 apresenta segmento de 64 mm. O mesmo comprimento real será apres
- Uma nota criada como texto anotativo aparece na viewport 1:100 mas não na viewport 1:50, a
- Uma tabela de quantitativos no Revit informa menor área de piso do que a esperada. O levan
- Uma contratação pública de obra de engenharia declara ser obrigatório o uso exclusivo de u
- Um modelo IFC foi aberto em outro programa e algumas relações e propriedades não aparecera
- Um componente é representado a 1:5 em desenho técnico. O comprimento real é 125 mm, mas a 
- Um projeto de instalações utiliza dois arquivos Revit coordenados. Na revisão, o analista 
- Uma equipe associa atividades de cronograma a elementos do modelo e vincula custos a quant
- Um objeto representado em escala 1:100 ocupa no desenho um comprimento cem vezes menor que
- A vista superior é posicionada acima da frontal no método de primeiro diedro, enquanto a m
- Em fluxo CAD com layouts, é apropriado representar a geometria real no model space e exibi
- No AutoCAD, o comando OFFSET é usado principalmente para ajustar o tamanho de um viewport 
- No Revit, um parâmetro de instância que controla propriedade específica de uma porta pode 
- A Modelagem da Informação da Construção, definida no Decreto nº 11.888/2024, consiste some
- Uma folha A3 tem dimensões nominais de 297 × 420 mm, mas o formato físico por si só não de
- Uma equipe abre imagem do desenho ampliada a 125% e mede dimensões com régua diretamente n
- A hachura de material atravessado por plano de corte serve a função diferente de uma linha
- No AutoCAD, uma coordenada relativa @25,10 indica o ponto absoluto (25,10), independenteme
- A escolha de campos e filtros numa tabela do Revit influencia os elementos e parâmetros ap
- Ao editar uma propriedade de tipo de família no Revit, a mudança repercute só na instância
- O IFC é padrão aberto capaz de descrever identidade, propriedades e relações de elementos 
- Se o usuário aplica SCALE com fator 2 a uma parede de 5.000 unidades no model space do Aut
- Em modelo Revit com tabelas parametrizadas, uma alteração da dimensão de tipo pode modific
- Uma exportação IFC concluída sem erro de sistema comprova que todos os atributos nativos, 
- Uma planta a 1:40 foi impressa com redução uniforme para metade do tamanho original; sem m
- Segundo o art. 19, § 3º, da Lei nº 14.133/2021, todo contrato público, inclusive aquisição
- Um projeto BIM associa dados de planejamento temporal aos elementos do modelo e outros dad
- Em uma prancha no primeiro diedro, a posição de vistas pode ser lida com convenção de terc
```

## Pedido 32: Extinção da punibilidade e prescrição (`direito-penal.extincao-punibilidade-prescricao`, 384 tópicos de edital)

```
Matéria: direito-penal.extincao-punibilidade-prescricao (Extinção da punibilidade e prescrição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Punibilidade, causas extintivas e efeitos jurídicos": 22
- "2. Extinção da punibilidade além da prescrição": 22
- "3. Prescrição punitiva: tabela e modalidades": 17
- "4. Termos iniciais, suspensão, interrupção e prescrição executória": 23
- "5. Reduções, penas especiais, concurso de crimes e cálculos avançados": 16

Já existem (não repita):
- Em processo por delito patrimonial, o acusado morreu antes da sentença, mas outro particip
- Foi aprovada lei que perdoa fatos criminosos praticados em período delimitado, abrangendo 
- Uma lei posterior elimina a incriminação de certa conduta, sem que ela continue punível po
- Em crime sujeito à ação privada, a pessoa ofendida descobre quem é o autor em 10 de março,
- Em hipótese legal de perdão judicial, o juiz reconhece seus pressupostos e extingue a puni
- Um delito tem pena máxima prevista de exatamente quatro anos de reclusão. Sem redutores ne
- Em infração com pena máxima de exatamente oito anos e sem causas modificadoras, o prazo de
- O agente inicia a execução de um delito, mas é impedido de prosseguir antes da consumação.
- A denúncia foi recebida em 1º/1/2020 e a sentença publicada em 1º/1/2025. Pena aplicada de
- Após sentença condenatória publicada em 1º/1/2020, foi aplicada pena de dois anos, sem rec
- Um condenado reincidente questiona o prazo da prescrição da pretensão punitiva de crime pe
- Em um delito, multa foi prevista cumulativamente com pena privativa de liberdade. Qual cri
- Agente tinha vinte anos na data de crime patrimonial não abrangido pela ressalva do art. 1
- Durante período legal de suspensão condicional do processo, beneficiário cumpre condições 
- Uma denúncia foi oferecida em 1º/3, mas recebida judicialmente em 1º/5 do mesmo ano. Qual 
- Uma condenação transitou em julgado para a acusação em 2024 e para a defesa em 2025, sem r
- Vítima de crime contra a dignidade sexual tinha 12 anos no fato e ainda não havia sido pro
- Crime praticado em 2018 teve denúncia recebida em 2020 e sentença condenatória publicada e
- Sentença condenatória recorrível foi publicada, e um acórdão posterior apenas confirmou su
- Uma pessoa recebe condenação por dois delitos em concurso material, com penas concretas de
- Em condenação por continuidade delitiva, pena-base individual para cada fato é de dois ano
- Em acordo de não persecução penal celebrado e ainda não integralmente cumprido nem rescind
- Em crime envolvendo violência sexual contra mulher cometido em 2026, agente tinha vinte an
- Condenado reincidente, em caso submetido ao Tema 788, recebeu pena definitiva de três anos
- Ao comparar infrações inafiançáveis e imprescritíveis, um estudante conclui que todas as h
- Após o falecimento comprovado de um acusado durante processo criminal, reconheceu-se causa
- Em crime sujeito a queixa ou representação praticado em 2026 no contexto de violência domé
- Uma lei federal concedeu indulto individual a condenado e, assim, exerceu competência típi
- Em crime permanente cuja situação ilícita durou vários dias, a prescrição punitiva se inic
- Em infração cuja única sanção penal prevista é multa, o prazo de prescrição estabelecido n
- Uma denúncia oferecida antes do decurso completo do prazo prescricional interrompe a conta
- Em ação penal exclusivamente privada, deixar o querelante de promover seu andamento durant
- Em crime cometido em 2022, a prescrição retroativa calculada com pena concretamente aplica
- Agente com 71 anos na data da sentença, condenado por crime patrimonial fora da ressalva l
- Se um acusado é reincidente, deve-se obrigatoriamente acrescentar um terço ao prazo da pre
- Antes de qualquer condenação, o julgador supôs que, se houver sentença, a pena será de um 
- Quando ocorre causa suspensiva do art. 116 do CP, todo o tempo de prescrição já transcorri
- Houve pronúncia em processo por crime doloso contra a vida; posteriormente, a conduta foi 
- A vedação de redução etária do art. 115 introduzida pela Lei nº 15.160/2025 pode retroagir
- Após trânsito em julgado da condenação, se a pessoa condenada está presa por motivo distin
- Um acórdão que confirma integralmente sentença condenatória sem alterar a pena não interro
- Para condenação cujo trânsito para acusação ocorreu em 2024 e para defesa em 2025, sem ext
- Duas infrações em concurso material receberam penas de seis e quatro anos; o art. 119 do C
- Em ação penal exclusivamente privada, a renúncia ao direito de queixa e o perdão do ofendi
- Em delito contra a dignidade sexual cometido contra criança, se a ação penal já tiver sido
```

## Pedido 33: Reumatologia Clínica (`medicina.clinica-reumatologia`, 384 tópicos de edital)

```
Matéria: medicina.clinica-reumatologia (Reumatologia Clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Abordagem das artrites, artrite séptica, gota e osteoartrite": 20
- "2. Artrite reumatoide e espondiloartrites": 20
- "3. Lúpus, síndrome antifosfolípide e doenças do tecido conjuntivo": 20
- "4. Vasculites": 20
- "5. Sarcoidose e fibromialgia": 20

Já existem (não repita):
- Homem de 45 anos chega com dor, calor e edema no joelho direito há 24 horas, febre de 38,5
- Na artrite séptica não gonocócica do adulto, o agente etiológico mais frequente é o Staphy
- Um líquido sinovial com 85.000 leucócitos/mm³ e predomínio de neutrófilos sugere, em prime
- À microscopia de luz polarizada, os cristais de urato monossódico da gota aparecem:
- Paciente em uso regular de alopurinol, com ácido úrico controlado, apresenta crise de poda
- No tratamento de manutenção da gota com hipouricemiante, a meta habitual é manter o ácido 
- Mulher de 68 anos, obesa, com dor nos joelhos que piora ao caminhar, rigidez matinal de 15
- Sobre a artrite reumatoide, assinale a alternativa correta.
- Pelos critérios de classificação ACR/EULAR 2010, a artrite reumatoide é classificada quand
- No tratamento da artrite reumatoide recém-diagnosticada, a droga modificadora de doença co
- Antes de iniciar um anti-TNF em paciente com artrite reumatoide, é fundamental:
- A síndrome de Felty é caracterizada pela associação de artrite reumatoide, esplenomegalia 
- Homem de 26 anos com lombalgia há 8 meses, de início insidioso, pior pela manhã e à noite,
- Sobre as espondiloartrites, é correto afirmar:
- A artrite reativa costuma surgir dias a semanas após infecção genitourinária ou intestinal
- No lúpus eritematoso sistêmico, o autoanticorpo de maior especificidade é o:
- A hidroxicloroquina é recomendada para praticamente todos os pacientes com lúpus eritemato
- Mulher de 25 anos com eritema malar, artralgias, úlceras orais, proteinúria de 2 g/24 h, F
- Sobre a síndrome antifosfolípide, assinale a correta.
- Na esclerose sistêmica, o anticorpo anticentrômero associa-se à forma difusa e à doença pu
- Paciente com esclerose sistêmica difusa, em uso recente de prednisona em dose alta, evolui
- Mulher de 52 anos com olhos secos e boca seca há dois anos, aumento de parótidas e anti-Ro
- Fraqueza muscular proximal e simétrica, CPK elevada, heliótropo e pápulas de Gottron suger
- O lúpus induzido por fármacos, associado a medicamentos como hidralazina e procainamida, c
- Pela classificação de Chapel Hill, são vasculites de grandes vasos:
- Mulher de 72 anos com cefaleia temporal nova há 3 semanas, claudicação de mandíbula, dor e
- A arterite de Takayasu acomete principalmente mulheres jovens e se manifesta com diminuiçã
- Sobre a poliarterite nodosa clássica, assinale a correta.
- Homem de 40 anos com sinusite crônica, perfuração de septo nasal, nódulos pulmonares cavit
- A granulomatose eosinofílica com poliangiite (antiga Churg-Strauss) caracteriza-se por asm
- Criança de 6 anos, após infecção de vias aéreas, apresenta púrpura palpável em membros inf
- Na doença de Kawasaki, a principal complicação a ser prevenida e o tratamento de escolha s
- Mulher de 30 anos com febre, eritema nodoso em pernas, artrite de tornozelos e adenopatia 
- Na sarcoidose, a hipercalcemia ocorre porque os macrófagos dos granulomas produzem calcitr
- Sobre a sarcoidose, assinale a alternativa correta.
- Mulher de 44 anos com dor difusa há um ano, fadiga, sono não reparador e dificuldade de co
- Os critérios atuais de classificação da fibromialgia exigem obrigatoriamente a presença de
- Relacione corretamente o autoanticorpo à doença: anti-Jo-1, anticentrômero e anti-CCP asso
- A doença por depósito de pirofosfato de cálcio acomete tipicamente joelhos e punhos de ido
- Na poliangiite microscópica, a apresentação clássica como síndrome pulmão-rim combina hemo
- A instabilidade atlantoaxial (C1-C2) é uma complicação da artrite reumatoide de longa evol
```

## Pedido 34: Machine learning com Python e MLOps (`informatica.machine-learning-python-mlops`, 384 tópicos de edital)

```
Matéria: informatica.machine-learning-python-mlops (Machine learning com Python e MLOps)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Python, preparação de dados e álgebra linear": 19
- "2. Aprendizado estatístico, modelos e avaliação": 22
- "3. Redes neurais, CNN, visão computacional e linguagem natural": 20
- "4. Processamento distribuído, aprendizado federado e atualização bayesiana": 20
- "5. Ciclo MLOps: implantação, governança e monitoramento": 19

Já existem (não repita):
- Uma tabela tem índice textual formado por 'Norte' e 'Sul'. Para recuperar a linha identifi
- Uma aplicação multiplica duas matrizes NumPy de forma `(2,2)` e pretende obter o produto m
- Uma equipe deseja preencher idades faltantes pela mediana e avaliar o modelo em observaçõe
- Para comparar a distribuição de uma variável contínua por grupos, uma pessoa pretende usar
- Em um notebook, uma variável é redefinida em uma célula executada fora da sequência visíve
- Um conjunto registra atributos dos clientes e a etiqueta 'cancelou' ou 'não cancelou'. Qua
- Uma aplicação estima a probabilidade de um registro pertencer à classe positiva por meio d
- Em 100 observações, um detector assinalou 20 positivas, das quais 15 eram realmente positi
- Uma regressão com numerosos atributos correlacionados deve permitir que alguns coeficiente
- Uma base sem rótulos deve ser dividida em k grupos por minimização de distâncias quadrátic
- Um classificador obteve precisão de 0,80 e revocação de 0,50 na classe de interesse. Qual 
- Um projeto usa `StandardScaler` dentro de validação cruzada com cinco dobras. Onde o escal
- Um vetor `v=(6,8)` representa dois atributos numéricos. Seus valores de norma L1 e norma L
- Uma matriz de dados com 200 linhas e 30 colunas será fatorada para redução de posto, sem s
- Em um projeto tabular, árvores são ajustadas sequencialmente para reduzir a função de perd
- Uma condição ocorre em 1% da população. Um teste tem sensibilidade de 90% e taxa de falso 
- Uma camada convolucional recebe imagem de 32×32 pixels e usa filtro 3×3, stride 2, padding
- Uma arquitetura visual tem duas entradas, ramos paralelos e uma operação que combina seus 
- Durante a inferência em PyTorch, uma rede com dropout deve usar comportamento de avaliação
- Uma equipe transforma notícias em vetores TF-IDF e deseja identificar nomes de pessoas e o
- Uma análise em PySpark manipula centenas de milhões de registros e pretende contar linhas 
- Dispositivos treinam localmente e enviam atualizações para agregação em servidor. A equipe
- Um modelo estima taxas de defeito para vários locais com poucas observações em alguns dele
- Vários treinamentos usam diferentes taxas de aprendizado e atributos. É necessário registr
- Após uma mudança de processo, a distribuição de atributos de entrada permanece semelhante,
- Uma tabela de observações contém valores ausentes. Em pandas, `df.dropna()` pode remover l
- Para dois arrays NumPy de mesma forma, o operador `*` executa por padrão multiplicação mat
- Um projeto possui observações rotuladas como fraude e operação legítima. Treinar um modelo
- Em classificação binária, precisão é dada por TP/(TP+FN), enquanto revocação corresponde a
- Para um vetor real (3,4), a norma L1 é 7 e a norma L2 é 5.
- O método KMeans utiliza os rótulos verdadeiros para calcular a função de perda e, por essa
- Uma rede convolucional pode compartilhar os mesmos pesos de um filtro em diferentes posiçõ
- Uma equipe calcula PCA e padronização sobre todos os registros antes de executar validação
- A decomposição em valores singulares de uma matriz retangular é viável mesmo quando não se
- Na regressão Ridge, a penalidade L2 é conhecida por produzir coeficientes exatamente nulos
- Uma operação `collect()` aplicada a um DataFrame distribuído em PySpark pode trazer os reg
- Em PyTorch, chamar `model.eval()` é suficiente para impedir a criação do grafo de autodife
- No MLOps, registrar o conjunto de dados e sua versão, parâmetros, métricas e artefatos fav
- Um detector de objetos, por identificar visualmente classes em uma imagem, pode ser avalia
- Em uma atualização bayesiana sequencial com hipóteses e dependências corretamente especifi
- Em aprendizado federado, a não transferência dos exemplos brutos implica que um servidor a
- Em um classificador de classe positiva rara, a acurácia de 98% pode coexistir com revocaçã
- O surgimento de novas categorias em um campo de entrada comprova, por si só, a ocorrência 
- Em Dask, operações sobre arrays e dataframes podem construir grafos de tarefas sem computa
- Um procedimento automático que retreina e substitui o modelo em produção assim que detecta
```

## Pedido 35: Lei Maria da Penha (`legislacao-penal-especial.maria-da-penha`, 383 tópicos de edital)

```
Matéria: legislacao-penal-especial.maria-da-penha (Lei Maria da Penha)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Âmbito de proteção e definição legal de violência doméstica": 19
- "2. Formas de violência e enquadramento dos fatos": 19
- "3. Atuação policial, acolhimento e medidas imediatas": 20
- "4. Concessão, conteúdo e duração de medidas protetivas": 20
- "5. Descumprimento, monitoração eletrônica e consequências processuais": 22

Já existem (não repita):
- Uma mulher é agredida por integrante do espaço doméstico permanente, sem parentesco. O art
- Um ex-parceiro agride mulher com quem manteve relação íntima, embora nunca tenham morado j
- Uma pessoa pratica violência contra mulher em núcleo familiar constituído por afinidade. C
- Uma mulher é vítima de violência doméstica em relação homoafetiva abrangida pelo art. 5º. 
- A violência doméstica e familiar contra a mulher foi reconhecida pelos pressupostos da lei
- Uma vítima relata socos e empurrões no ambiente doméstico. Qual forma do art. 7º está mais
- Uma mulher é sistematicamente isolada e ameaçada para perder autonomia. Qual modalidade le
- Em contexto de relação íntima, o agressor força prática sexual não desejada mediante ameaç
- O agressor destrói instrumentos de trabalho e retém documentos pessoais da ofendida. Que v
- Um agressor ataca dependente da mulher com a finalidade de atingir emocionalmente a ofendi
- Uma equipe pretende repetir diversas entrevistas desnecessárias e colocar vítima frente ao
- Uma mulher em risco de vida necessita sair com dependentes para abrigo seguro. Qual provid
- Uma ofendida solicita medida protetiva na delegacia. Em quanto tempo deve o expediente apa
- Não há sede de comarca nem delegado disponível e existe risco atual ao bem jurídico proteg
- Município não é sede de comarca e delegado aplica monitoração eletrônica imediata por risc
- O juiz recebe expediente contendo pedido protetivo apresentado pela ofendida. Qual prazo l
- Uma ofendida solicita proteção urgente sem existir inquérito policial instaurado. Pode hav
- Uma medida protetiva precisa impedir mensagens do agressor à ofendida. O art. 22 autoriza 
- Para proteção, a própria ofendida é afastada do lar em situação de risco. Ela perde automa
- O agressor usa procuração outorgada pela vítima para desviar bens. Qual medida do art. 24 
- Após descumprimento anterior, juiz avalia monitoração e dispositivo para alertar a vítima.
- Um agressor descumpre medida judicial protetiva e responde pelo art. 24-A. Qual pena atual
- Um agressor viola perímetro eletrônico judicial e descumpre medida protetiva. Qual fração 
- Agressor preso em flagrante por descumprir medida protetiva pede fiança. Quem pode concedê
- A mulher em ação condicionada manifesta expressamente desejo de retratar-se antes do receb
- A Lei Maria da Penha aplica-se às situações do art. 5º independentemente da causa ou da mo
- Para que a Lei Maria da Penha incida, a agressão precisa ocorrer dentro da residência, já 
- Pessoas esporadicamente agregadas ao espaço de convívio permanente também integram a unida
- O art. 6º da Lei Maria da Penha só qualifica como violação de direitos humanos a violência
- A violência psicológica pode configurar-se sem nenhuma ofensa física, como na vigilância c
- A violência vicária, incluída pela Lei nº 15.384/2026, exige que a pessoa atingida seja fi
- Impedir a mulher, mediante coação, de usar qualquer método contraceptivo é forma de violên
- A violência moral, na Lei Maria da Penha, abrange qualquer conduta que cause dano emociona
- No atendimento policial, a lei garante que a mulher, seus familiares e testemunhas não ten
- Por ser a parte interessada, a própria ofendida pode ser encarregada de entregar ao agress
- Desde a Lei nº 15.411/2026, o afastamento imediato do agressor previsto no art. 12-C cabe 
- Em município sede de comarca, se não houver delegado disponível no momento, qualquer polic
- As medidas protetivas de urgência vigoram enquanto persistir risco à integridade da ofendi
- O juiz só pode conceder medida protetiva de urgência depois da manifestação do Ministério 
- Entre as medidas de proteção à ofendida, o juiz pode conceder auxílio-aluguel, com valor f
- A suspensão da posse ou a restrição do porte de armas do agressor não pode ser determinada
- Na monitoração eletrônica do art. 22, a ruptura do perímetro de exclusão pelo agressor dev
- Só há o crime do art. 24-A se a medida protetiva descumprida tiver sido decretada por juiz
- Nos crimes praticados no contexto de violência doméstica e familiar contra a mulher, o pra
- Ao crime de ameaça praticado contra a mulher em contexto de violência doméstica aplica-se 
```

## Pedido 36: Defesa do Estado e das Instituições Democráticas (`direito-constitucional.defesa-estado-instituicoes`, 381 tópicos de edital)

```
Matéria: direito-constitucional.defesa-estado-instituicoes (Defesa do Estado e das Instituições Democráticas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sistema constitucional de crises e conselhos consultivos": 32
- "2. Estado de defesa: hipóteses, restrições, prazo e controle": 17
- "3. Estado de sítio: autorização, duração, medidas e fiscalização": 17
- "4. Forças Armadas e estrutura constitucional da segurança pública": 17
- "5. Guardas municipais, segurança viária e integração das garantias": 17

Já existem (não repita):
- Após identificar grave ameaça institucional, o Presidente pede parecer sobre estado de def
- Uma questão descreve seis pessoas escolhidas em três grupos de dois, para integrar um cons
- Uma reunião trata da utilização estratégica de uma área de fronteira e da exploração de re
- O Presidente recebe parecer desfavorável de ambos os conselhos em cenário que entende pree
- Ao comparar os Conselhos da República e de Defesa Nacional, um relatório descreve sua comp
- Após calamidade natural de grandes proporções em região delimitada, a autoridade cogita me
- O Presidente decreta estado de defesa por 30 dias e, persistindo as causas, pretende esten
- Durante estado de defesa, a autoridade mantém detido por 12 dias alguém preso por crime co
- Um decreto de defesa impõe restrições e usa temporariamente bens e serviços públicos para 
- Decretado o estado de defesa, o Presidente o submete em 24 horas. O Congresso recebe o ato
- Diante de comoção grave de repercussão nacional, o Presidente pretende decretar estado de 
- Uma crise nacional configurada no art. 137, I, leva ao estado de sítio autorizado. Qual li
- Durante estado de sítio por comoção nacional, o decreto prevê busca e apreensão domiciliar
- No recesso parlamentar, o Presidente solicita autorização para decretar estado de sítio. C
- Terminada a vigência de um estado de sítio, surgem indícios de medidas ilícitas e question
- Em uma comparação entre instituições militares nacionais e segurança pública ordinária, qu
- Um caso envolve patrulhamento ostensivo de rodovias federais, e não investigação criminal.
- Em um conflito institucional, determinada autoridade afirma que o art. 142 autoriza as For
- Uma investigação versa sobre infração penal comum de competência estadual, sem atribuição 
- Um enunciado afirma que, por serem forças auxiliares e reserva do Exército, as polícias mi
- Uma administração local analisa criar guarda municipal para proteger bens e prestar segura
- Um projeto de mobilidade reúne educação de trânsito, engenharia viária e fiscalização nas 
- Lei municipal prevê guarda realizando policiamento ostensivo e comunitário, sem assumir in
- Uma questão afirma que segurança viária do art. 144, § 10, é atribuição federal exclusiva.
- Um decreto de crise é encerrado, e um Município quer ampliar sua guarda para investigar cr
- Ao preparar consulta presidencial relativa ao estado de sítio, a assessoria encaminha o te
- Na escolha dos seis cidadãos para o Conselho da República, a Constituição exige brasileiro
- O Conselho de Defesa Nacional pode opinar sobre guerra e paz e propor critérios de uso de 
- Em crise institucional, a manifestação favorável do Conselho da República equivale à autor
- O chefe do Executivo decreta estado de defesa por 45 dias, sem prorrogação, alegando que a
- No estado de defesa decretado por calamidade, é possível ocupação e uso temporário de bens
- Preso alguém por crime contra o Estado durante estado de defesa, a comunicação imediata ao
- Se o Congresso rejeita o decreto de estado de defesa, o regime excepcional cessa imediatam
- Comprovada comoção grave de repercussão nacional, o Presidente pode pedir autorização prév
- No sítio decorrente de guerra ou agressão armada estrangeira, a Constituição fixa prazo or
- No sítio fundado em comoção nacional do art. 137, I, o art. 139 permite restringir imprens
- A Mesa do Congresso designa comissão de sete parlamentares para acompanhar apenas o estado
- Em patrulhamento ostensivo de ferrovia federal, a Constituição atribui essa finalidade à p
- O art. 144 diferencia a polícia ostensiva das polícias militares e a investigação e políci
- A expressão garantia dos poderes constitucionais, no art. 142, confere às Forças Armadas c
- Por força do art. 144, § 6º, a condição de força auxiliar e reserva do Exército não exclui
- A Constituição permite ao Município constituir guarda municipal para proteção de bens, ser
- O Tema 656 autorizou policiamento ostensivo e comunitário municipal e, por consequência, t
- A segurança viária do art. 144, § 10, compreende educação, engenharia e fiscalização de tr
- Como a ADPF 995 reconheceu as guardas no sistema de segurança pública, o controle externo 
```

## Pedido 37: Inovação e excelência nos serviços públicos (`administracao-publica.inovacao-excelencia-servicos-publicos`, 381 tópicos de edital)

```
Matéria: administracao-publica.inovacao-excelencia-servicos-publicos (Inovação e excelência nos serviços públicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Inovação pública: conceitos, tipos e valor público": 25
- "2. Design thinking, desenho de serviços e lean startup": 19
- "3. Excelência: GesPública, MEGP e FNQ": 19
- "4. Participação, coordenação e sistemas federais": 19
- "5. Transformação digital, IA e avaliação de resultados": 18

Já existem (não repita):
- Uma equipe criou um dispositivo de orientação por voz, mas o manteve em laboratório, sem t
- Um serviço passou a consultar diretamente informações já disponíveis em base pública autor
- Uma equipe pretende compreender o motivo pelo qual pessoas desistem do requerimento antes 
- Ao redesenhar um serviço, a equipe deseja ver os contatos com usuários e as atividades int
- Um grupo testou, em pequena escala, o envio de lembretes para reduzir faltas a atendimento
- Um material de treinamento afirma que o programa GesPública continua juridicamente institu
- Uma equipe consulta o Modelo de Excelência em Gestão Pública de 2014 e precisa identificar
- Um gestor afirma que seu serviço foi eficiente porque produziu mais atendimentos com a mes
- Duas unidades integrantes do mesmo governo passam a coordenar procedimentos de atendimento
- Um canal público recebe reclamações e sugestões para aperfeiçoar serviços, enquanto um col
- Uma equipe anuncia a digitalização de um procedimento, mas mantém documentos repetidos, et
- Um plano estadual busca estabelecer protocolo comum com um município para reduzir divergên
- Uma organização apresenta como referência o MEG 22 da Fundação Nacional da Qualidade. Sua 
- Um painel mostra 40 mil visitas ao portal e 800 requerimentos concluídos, mas o relatório 
- Para cumprir a Lei nº 13.460/2017, uma entidade revisa sua Carta de Serviços ao Usuário. Q
- Uma iniciativa reúne pessoas de universidades, empresas e unidades públicas para testar so
- Uma unidade responsável por serviços gerais precisa reconhecer o sistema federal que organ
- Um serviço digital nacional pretende servir pessoas com deficiência visual e com baixa con
- Após testar um serviço em grupo reduzido, a equipe observa que usuários compreendem a prop
- Uma análise apresenta três resultados: menos custo por pedido, cumprimento da meta de 10 m
- Um colegiado criado nos termos dos arts. 18 a 22 da Lei nº 13.460/2017 deseja impor decisã
- Uma equipe prepara acordo de troca de dados entre sistemas para prestar serviço integrado.
- Uma aplicação de IA recomenda prioridade de atendimento a partir de histórico produzido po
- Uma avaliação anual constatou piora na satisfação e no cumprimento de prazos de um serviço
- Uma iniciativa apresenta desenvolvimento de tecnologia em parceria com instituição de pesq
- Uma equipe identificou uma melhoria concreta na forma de receber documentos, implantou o n
- Um projeto de design thinking somente pode ser considerado válido quando a equipe fixa a s
- Ao analisar o modelo público de excelência de 2014, uma equipe pode examinar as dimensões 
- Um município e um estado compartilham responsabilidades por um serviço, mas sua articulaçã
- A falta de acessibilidade de um portal pode comprometer a excelência do serviço, mesmo que
- Em um piloto de lean startup, a escolha de métricas e hipóteses pode ser adiada até depois
- Uma política de inovação pode reunir pesquisa, empresas e instituições públicas, uma vez q
- O Decreto nº 5.378/2005 continua sendo o fundamento normativo vigente de instituição do Ge
- Conselhos de usuários estruturados pela Lei nº 13.460/2017 podem acompanhar a prestação do
- O simples aumento de solicitações concluídas comprova efetividade de um programa, mesmo qu
- Ao desenvolver um serviço digital, a administração deve considerar as necessidades de aces
- O SIORG está relacionado à administração de recursos de tecnologia da informação, enquanto
- O MEG 22 da Fundação Nacional da Qualidade e o MEGP 2014 possuem a mesma estrutura formal 
- Uma pesquisa de satisfação anual pode integrar a avaliação de serviços exigida pelo art. 2
- Quando representantes da sociedade participam de um fórum sobre redesenho de políticas, a 
- A qualidade de um sistema de inteligência artificial aplicado ao atendimento não deve ser 
- Os sistemas estruturadores federais referem-se apenas a softwares de execução, motivo pelo
- Um serviço com expressiva redução de tempo pode apresentar melhoria de eficiência e, simul
- Ao testar uma inovação em pequena escala, uma equipe pode considerar satisfatório o result
- Um projeto que simplifica etapas, reduz emissões e mantém opções acessíveis de atendimento
```

## Pedido 38: Microsserviços, API gateway e mensageria (PDPJ-Br) (`informatica.microsservicos-api-gateway-mensageria`, 377 tópicos de edital)

```
Matéria: informatica.microsservicos-api-gateway-mensageria (Microsserviços, API gateway e mensageria (PDPJ-Br))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos de microsserviços e padrões arquiteturais": 12
- "2. APIs RESTful, JSON, gateway e descoberta de serviços": 17
- "3. Spring, segurança, resiliência e operação distribuída": 14
- "4. Mensageria, RabbitMQ, eventos e webhooks": 31
- "5. Persistência, auditoria, migrações e integração de ponta a ponta": 26

Já existem (não repita):
- Uma aplicação separou módulos de atendimento, cobrança e avisos em processos implantáveis 
- Um serviço de relatórios precisa exibir os dados de pagamentos mantidos por outro serviço.
- Após o evento `CadastroValidado`, um serviço envia aviso e outro cria uma projeção local, 
- Uma reserva foi confirmada localmente; depois, o serviço de pagamento recusou a cobrança. 
- Uma aplicação possui módulos bem delimitados, com chamadas locais, porém todos são entregu
- Um cliente deseja consultar o estado da solicitação 42 sem pedir alteração do recurso. Qua
- Uma API aceitou um arquivo para verificação assíncrona, criou uma tarefa e ainda não concl
- Uma aplicação recebe documentos no formato JSON e recusa entradas fora da gramática. Qual 
- Um cliente chama `/api/cobrancas/10`; o serviço de cobranças possui três instâncias com IP
- Uma integração fornece uma URL para receber POST quando o processamento remoto terminar. E
- Quatro réplicas devem carregar propriedades distintas para homologação e produção sem dupl
- Após autenticação centralizada, um usuário tenta excluir um registro cuja leitura foi auto
- O serviço externo começa a falhar repetidamente; novos pedidos ao mesmo destino agravam a 
- Um POST cria cobrança no banco, mas o cliente perde a resposta e repete a requisição. Qual
- Uma requisição percorre gateway e três serviços, mas os logs locais não mostram onde se co
- Um exchange `fanout` possui duas filas vinculadas, cada uma com seu próprio consumidor. Ap
- Um exchange do tipo `topic` possui binding `solicitacao.*.concluida`. Qual routing key cor
- Um produtor recebeu `publisher confirm`; minutos depois, uma mensagem foi reentregue porqu
- Um consumidor encontra repetidamente um evento com esquema inválido, falha e retorna a men
- Um receptor de webhook grava a notificação, mas a resposta HTTP se perde. O emissor reenvi
- Um serviço confirma o pedido no banco, mas o envio do evento falha depois do commit local.
- É preciso recuperar o valor anterior do campo `status` de uma entidade após sucessivas alt
- Em produção, `V1__criar_tabela.sql` já foi aplicada. Uma pessoa altera o SQL de V1 para ad
- Testes locais usam `jdbc:h2:mem:teste`, enquanto o serviço de produção utiliza PostgreSQL.
- Uma API precisa expor DTOs sem vazar diretamente entidades persistidas, e a equipe pretend
- Em uma arquitetura orientada por DDD, o contexto delimitado estabelece o alcance em que um
- Se uma aplicação possui dez controladores REST distribuídos em pacotes diferentes, ela já 
- Numa saga, o cancelamento de uma reserva depois da falha de pagamento pode ser modelado co
- A propriedade dos dados em microsserviços somente é respeitada quando cada serviço utiliza
- Na semântica HTTP, GET destina-se à obtenção de representação e é considerado método segur
- Ao responder 202 Accepted, a API assegura ao cliente que todos os efeitos negociais já for
- No JSON, `{"ativo":true,"lotes":[1,2],"observacao":null}` constitui um objeto válido, porq
- O registry Eureka deve executar sozinho a autorização de cada operação exposta ao cliente 
- Spring Boot simplifica a inicialização e configuração de aplicações do ecossistema Spring,
- OAuth 2.0 e OpenID Connect são especificações idênticas de autenticação: por isso um acces
- Um sistema que aplica retry após timeout em operação de escrita deve considerar que o serv
- A coleta de logs isolados, sem propagação de contexto, substitui integralmente o tracing d
- No RabbitMQ, um exchange fanout encaminha cada publicação para as filas a ele vinculadas; 
- O publisher confirm do RabbitMQ equivale ao consumer acknowledgement e somente é emitido d
- O padrão outbox registra, na mesma transação local, os dados de negócio e a intenção de pu
- Num binding de exchange topic, o curinga `*` corresponde a zero ou mais palavras separadas
- Se o endpoint de webhook receber novamente o mesmo evento porque a resposta da primeira en
- A biblioteca Flyway acompanha automaticamente todas as versões anteriores dos valores de c
- Quando o Hibernate Envers audita determinada entidade, é possível manter registros de revi
- Todo banco H2 é necessariamente armazenado apenas em arquivo e conserva as tabelas depois 
```

## Pedido 39: Trigonometria (`matematica.trigonometria`, 376 tópicos de edital)

```
Matéria: matematica.trigonometria (Trigonometria)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ângulos, radianos e razões trigonométricas no triângulo": 14
- "2. Círculo unitário, sinais, identidades e gráficos periódicos": 31
- "3. Operações com arcos: adição, subtração, duplo, metade e produtos": 13
- "4. Equações e inequações trigonométricas no círculo": 28
- "5. Leis dos senos e dos cossenos em triângulos quaisquer": 14

Já existem (não repita):
- Uma medição angular foi anotada como 150°. Qual é sua representação exata em radianos?
- Em uma pista circular de raio 6 m, um percurso corresponde a um arco central de π/3 rad. Q
- Uma escada e o solo formam triângulo retângulo de catetos 5 m e 12 m. Para o ângulo agudo 
- No círculo unitário, a extremidade do arco de 150° encontra-se no segundo quadrante. Qual 
- Um ponto no segundo quadrante do círculo unitário tem cosseno −3/5. Usando a relação funda
- Um sensor segue a função y = 3 sen(2x − π) + 1. Qual par descreve a amplitude e o período 
- Uma rampa reta mede 8 m de comprimento e sobe 4 m em relação à horizontal. Qual é seu ângu
- Na decomposição do arco 75° como 45° + 30°, qual é o valor exato de sen 75° obtido pela fó
- Um ângulo de 75° é expresso como soma de 45° e 30°. Qual forma exata resulta para cos 75°?
- Para um arco agudo α, são conhecidos sen α = 3/5 e cos α = 4/5. Qual é o valor correto de 
- Um ângulo x pertence ao intervalo 0 ≤ x < 2π. Em que conjunto de arcos sua ordenada no cír
- No cálculo de cos(45° − 30°), aplica-se a identidade de diferença de arcos. Qual é o valor
- Uma coordenada horizontal do ciclo é negativa. Para x no intervalo 0 ≤ x < 2π, qual conjun
- A inclinação de uma reta forma ângulo de 75° com o eixo horizontal. Usando tangente da som
- Em 0 ≤ x < 2π, deseja-se selecionar todos os arcos para os quais tan x ≥ 1. Qual conjunto 
- Para certo arco α, sabe-se que sen α = 3/5. Qual valor tem cos(2α), independentemente do s
- No ciclo unitário com 0 ≤ x < 2π, qual intervalo representa todos os pontos em que sen x ≤
- Dois arcos agudos α e β têm sen α = 3/5, cos α = 4/5, sen β = 5/13 e cos β = 12/13. Qual é
- Um sinal periódico é positivo quando sen(2x) > 0. Quais são os intervalos de x em 0 ≤ x < 
- Um arco mede 240°, e o problema solicita o cosseno de sua metade, isto é, do arco de 120°.
- Considere x em 0 ≤ x < 2π e a inequação sen x · cos x ≥ 0. Qual conjunto de arcos represen
- Em uma simplificação algébrica, aparece a expressão 2 cos(5x) cos(2x). Qual transformação 
- Em triângulo ABC, o lado a oposto a A = 30° mede 10 m. Se o ângulo B = 45°, quanto mede o 
- Uma equação contém a soma sen(3x) + sen(x). Qual fatoração é obtida pela identidade de tra
- Duas trilhas retilíneas partem do mesmo ponto, medem 5 km e 7 km e formam ângulo de 60°. Q
- Um giro de 180° corresponde a π radianos, e o mesmo ponto do ciclo é alcançado após um gir
- Em triângulo retângulo de lados 3, 4 e 5, o seno do ângulo oposto ao cateto de comprimento
- Se um ponto do círculo de raio unitário está associado ao ângulo θ, sua coordenada horizon
- Na posição angular de π/2 rad, a tangente do arco vale 1 porque o seno é igual a 1 e o âng
- Um ponto localizado no terceiro quadrante do ciclo unitário tem seno e cosseno negativos, 
- A função cosseno é ímpar, pois para qualquer ângulo real x vale cos(−x) = −cos x, segundo 
- Em triângulo retângulo com catetos medindo 3 m e 4 m, a hipotenusa mede 5 m, e a tangente 
- Em uma oscilação representada por y = 2 sen(4x), o período fundamental da função em radian
- No intervalo 0 ≤ x < 2π, a equação sen x = 1/2 possui exatamente as soluções x = π/6 e x =
- Como as coordenadas do círculo unitário têm módulo máximo 1, a relação fundamental da trig
- Em um triângulo ABC não degenerado, se a, b e c são lados opostos respectivamente a A, B e
- A função tangente tem período fundamental 2π, assim como as funções seno e cosseno, pois t
- Duas arestas de um terreno medem 8 m e 5 m e delimitam entre si ângulo de 30°. A área do t
- Em 0 ≤ x < 2π, a desigualdade cos(x − π/3) ≥ 1/2 tem conjunto de soluções [0, 2π/3), com e
- Para quaisquer arcos reais a e b, a fórmula de adição do cosseno é cos(a + b) = cos a cos 
- No intervalo 0 ≤ x < 2π, a inequação tan x ≤ 0 admite os intervalos [π/2, π] e [3π/2, 2π),
- Ao aplicar a fórmula de meio arco para cos(240°/2), a raiz fornece módulo 1/2; o sinal cor
- Em triângulo ABC com A = 30°, lado oposto a = 5 e lado b = 8, a lei dos senos conduz neces
- Quando dois lados de um triângulo medem 5 e 7 unidades e formam ângulo de 60°, a lei dos c
- Se dois lados de um triângulo medem 5 e 7 unidades e o ângulo entre eles é 120°, a distânc
```

## Pedido 40: Matrizes e determinantes (`matematica.matrizes-determinantes`, 376 tópicos de edital)

```
Matéria: matematica.matrizes-determinantes (Matrizes e determinantes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Notação, ordem, tipos de matrizes e transposta": 18
- "2. Operações, produtos matriciais e transformações": 16
- "3. Determinantes: ordem 2, ordem 3, Sarrus e Laplace": 26
- "4. Propriedades dos determinantes e matriz invertível": 25
- "5. Matriz inversa, adjunta, Cramer e aplicações integradas": 15

Já existem (não repita):
- Uma tabela matricial A tem três linhas e duas colunas, com entradas organizadas por linhas
- Um inventário é representado por matriz que possui duas linhas e quatro colunas. Qual desc
- A matriz quadrada de ordem 2 é definida pela lei aij = i + 2j, com índices a partir de 1. 
- Uma matriz de registros possui ordem 2 × 3. Após transposição, que ordem passa a ter a tab
- Duas matrizes A e B, ambas 2 × 2, possuem primeiras linhas (1,4) e (3,2) e segundas linhas
- A matriz A possui primeira linha (2,−3) e segunda linha (1,4). Ao multiplicá-la por escala
- Uma matriz de quantidades A tem ordem 2 × 3 e uma de preços B tem ordem 3 × 4. Qual é a or
- A matriz A tem linhas (3,2) e (1,4). Qual é o determinante de A segundo a regra de ordem 2
- Um modelo matricial 3 × 3 é triangular superior e tem diagonal principal (2,−1,5). Qual é 
- Uma matriz quadrada A de ordem 2 tem determinante zero. Qual afirmação descreve corretamen
- Duas matrizes A e B possuem dimensões adequadas para AB. Qual expressão representa a trans
- Em um cálculo de custos, A tem primeira linha (2,1,3) e segunda (0,4,2). A primeira coluna
- Considere a matriz de ordem 3 com linhas (1,2,0), (3,1,4) e (2,0,1). Aplicando Sarrus com 
- Uma matriz A possui linhas (2,0,1), (3,1,0), (4,2,1). Na expansão de Laplace pela primeira
- Uma matriz quadrada A possui determinante −7. A matriz B é obtida trocando a primeira e a 
- Uma matriz A de ordem 3 tem determinante −2. Um novo modelo utiliza B=3A, multiplicando to
- Para matrizes quadradas A e B da mesma ordem, det(A)=4 e det(B)=−3. Qual será det(AB) pela
- Uma matriz A, real, possui duas linhas e três colunas. Considere AAᵀ e AᵀA. Qual afirmação
- Para a matriz A com linhas (2,1) e (3,2), qual alternativa fornece a inversa correta, veri
- Uma matriz quadrada A é invertível e satisfaz det(A)=−4. Qual determinante corresponde à s
- Um modelo usa x+2y=7 e 3x+y=8. Aplicando a regra de Cramer, com matriz de coeficientes A, 
- Uma matriz de coeficientes 2 × 2 tem determinante zero. Antes de analisar os termos indepe
- A matriz A(t) tem linhas (t,2) e (3,t). Para quais valores reais de t ela deixa de possuir
- Se A é uma matriz quadrada real de ordem 2 e det(A)=−3, qual valor obrigatoriamente possui
- Duas transformações de vetores coluna são A=[[2,0],[0,1]] e B=[[0,1],[1,0]]. Aplicando pri
- Na matriz de ordem 3 × 2 com linhas (1,4), (0,5) e (2,9), o elemento a31 vale 2, pois os í
- Uma matriz de ordem 2 × 3 pode ser somada diretamente a uma matriz de ordem 3 × 2, desde q
- A transposta de uma matriz com quatro linhas e duas colunas possui duas linhas e quatro co
- Toda matriz diagonal tem todas as entradas da diagonal principal diferentes de zero, porqu
- Para A=[[5,2],[3,1]], o determinante é −1, pois o produto da diagonal principal é 5 e o da
- Mesmo que duas matrizes quadradas A e B tenham AB e BA definidos, a igualdade AB=BA é cons
- Se A é 3 × 2 e B é 2 × 5, o produto AB existe e apresenta ordem 3 × 5, embora o produto BA
- Para uma matriz real retangular A de ordem 2 × 3, o produto AAᵀ não pode ser simétrico por
- A regra de Sarrus permite calcular determinante 3 × 3 somando três produtos de diagonais e
- Para calcular o determinante de uma matriz 4 × 4, basta repetir suas três primeiras coluna
- Na expansão de Laplace, o cofator da entrada situada na segunda linha e terceira coluna po
- Quando uma matriz quadrada tem determinante zero, necessariamente todas as suas entradas s
- Uma matriz triangular inferior de ordem 3 tem diagonal principal composta por −2, 3 e 4. I
- Uma matriz quadrada com determinante 5 é transformada pela troca de duas linhas uma única 
- Se A é matriz de ordem 3 com determinante 3, a matriz 2A tem determinante 24, pois multipl
- Para matrizes quadradas A e B de ordem 2, sempre vale det(A+B)=det(A)+det(B), de modo anál
- Duas matrizes quadradas A e B de mesma ordem possuem det(A)=−2 e det(B)=5. Nesse caso, det
- Se A, B e C são matrizes quadradas da mesma ordem e AB=AC, a igualdade B=C decorre necessa
- Em um sistema quadrado com matriz A invertível, a regra de Cramer encontra uma incógnita s
- Se o determinante da matriz dos coeficientes de um sistema quadrado é zero, o sistema nece
```

## Pedido 41: Angiologia e cirurgia vascular (`medicina.angiologia-cirurgia-vascular`, 376 tópicos de edital)

```
Matéria: medicina.angiologia-cirurgia-vascular (Angiologia e cirurgia vascular)
Capítulos e quantas questões quero em cada um (total 100):
- "Avaliação vascular e doença arterial periférica": 17
- "Isquemia aguda do membro e trauma vascular": 17
- "Aneurismas da aorta e periféricos e doença carotídea": 17
- "Insuficiência venosa crônica, varizes e trombose superficial": 17
- "Trombose venosa profunda e tromboembolismo pulmonar": 16
- "Linfedema, pé diabético e acessos vasculares": 16

Já existem (não repita):
- Homem tabagista apresenta dor em panturrilha sempre após caminhar aproximadamente dois qua
- Na avaliação do membro inferior direito, as pressões sistólicas pediosa e tibial posterior
- Paciente relata dor e frialdade súbitas no pé, com diminuição de pulsos e parestesias. Que
- A respeito do aneurisma de artéria poplítea, qual complicação merece especial atenção?
- Na classificação CEAP da doença venosa crônica, o estágio clínico C6 corresponde a:
- Qual achado ultrassonográfico é central para o diagnóstico de trombose venosa profunda em 
- Edema crônico que envolve o dorso do pé, com espessamento cutâneo e sinal de Stemmer posit
- Em portador de fístula arteriovenosa para hemodiálise, qual achado exige avaliação rápida 
- Mulher com diabetes e insuficiência renal tem úlcera distal, pulsos reduzidos e ITB 1,52. 
- Na classificação Rutherford para isquemia aguda, o paciente tem perda sensitiva além dos d
- Homem apresenta massa pulsátil inguinal dolorosa após cateterismo arterial. Qual estratégi
- Paciente tem varizes recorrentes e edema, com suspeita de refluxo em safena e obstrução pr
- Paciente com dor e cordão endurecido ao longo de veia safena próxima à junção safenofemora
- Em paciente com probabilidade clínica improvável para TVP e D-dímero de alta sensibilidade
- Pessoa com diabetes apresenta ulceração plantar sem queixa de dor, perda de sensibilidade 
- Paciente com doença venosa crônica e úlcera maleolar apresenta pulsos fracos e pé frio. An
- Paciente com claudicação limitante tem ITB 0,74, recebe tratamento clínico otimizado e exe
- Paciente com isquemia aguda passou por reperfusão após longa obstrução e apresenta perna m
- Sobre vigilância após reparo endovascular de aneurisma de aorta abdominal (EVAR), assinale
- Paciente sofreu amaurose fugaz ipsilateral há uma semana e apresenta estenose carotídea im
- Uma pessoa com varizes graves apresenta úlcera venosa cicatrizada, refluxo safeno e obstru
- Pessoa com alta probabilidade clínica de TVP obtém ultrassonografia proximal inicial negat
- Paciente com edema do membro superior e circulação venosa colateral possui FAV para hemodi
- Vítima de trauma penetrante femoral apresenta hemorragia pulsátil profusa e instabilidade 
- O índice tornozelo-braquial é calculado para cada membro inferior com a maior pressão sist
- Claudicação intermitente arterial típica surge ao caminhar e costuma melhorar após repouso
- Na isquemia aguda do membro, déficit motor progressivo pode indicar risco imediato de perd
- A ultrassonografia de aorta abdominal não tem utilidade na detecção ou vigilância de aneur
- Na classificação CEAP, C5 corresponde à úlcera venosa cicatrizada e C6 à úlcera venosa ati
- D-dímero positivo isolado confirma trombose venosa profunda, independentemente da probabil
- Perda de sensibilidade protetora no pé diabético pode permitir ulcerações indolores.
- Em uma fístula arteriovenosa funcionante para hemodiálise, frêmito palpável costuma reflet
- Em diabéticos com calcificação medial, um ITB muito alto pode não excluir doença arterial 
- Na isquemia aguda classificada como Rutherford IIb, é aceitável postergar por muitas horas
- Aneurismas poplíteos podem causar isquemia aguda por trombose local ou embolização distal,
- A presença de sopro carotídeo isolado determina o grau exato de estenose e torna obrigatór
- A trombose venosa superficial pode alcançar junções com o sistema profundo e, por isso, às
- No ultrassom para TVP, a presença de algum sinal de fluxo colorido, sozinha, exclui trombo
- Em linfedema puro, o uso contínuo de diuréticos é, em regra, o pilar do tratamento de long
- Uma úlcera venosa exige avaliação de perfusão arterial antes de compressão intensa quando 
- No diagnóstico de isquemia crônica ameaçadora do membro, dor em repouso, ulceração ou gang
- Paciente com sinais de isquemia aguda irreversível, anestesia profunda e paralisia fixa de
- Após reparo endovascular de aneurisma aórtico, endoleak tipo II pode ocorrer por fluxo ret
- Em estenose carotídea importante, antecedente recente de amaurose fugaz ipsilateral é irre
- No paciente com doença venosa crônica, refluxo superficial e obstrução profunda pós-trombó
- Se a probabilidade clínica de TVP for alta, ultrassonografia proximal negativa uma única v
- Um paciente com tromboembolismo pulmonar hemodinamicamente estável necessariamente precisa
- No pé diabético com escara seca isquêmica estável, o desbridamento agressivo de rotina ant
```

## Pedido 42: Endocrinologia e metabologia (`medicina.endocrinologia`, 374 tópicos de edital)

```
Matéria: medicina.endocrinologia (Endocrinologia e metabologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Diabetes mellitus: diagnóstico, classificação e tratamento": 17
- "2. Emergências e complicações do diabetes": 17
- "3. Tireoide: fisiologia, disfunções, nódulos e urgência": 17
- "4. Adrenais, hipotálamo e hipófise: eixos e síndromes": 17
- "5. Obesidade, dislipidemias e risco cardiometabólico": 16
- "6. Metabolismo mineral, osteoporose e endocrinologia do ciclo de vida": 16

Já existem (não repita):
- Paciente com sede intensa, poliúria e emagrecimento apresenta glicemia plasmática casual d
- Qual alteração descreve melhor o mecanismo do diabetes mellitus tipo 1 na maioria dos paci
- Em uma pessoa acordada com diabetes e glicose capilar de 62 mg/dL, capaz de deglutir com s
- Em adulto com TSH persistentemente elevado e T4 livre abaixo da referência, qual hipótese 
- Qual hormônio é produzido principalmente pela zona glomerulosa do córtex da suprarrenal?
- Para um adulto com peso de 90 kg e altura de 1,50 m, qual é aproximadamente o índice de ma
- Em paciente com hipocalcemia, fósforo alto e paratormônio inadequadamente baixo, qual diag
- Qual é a finalidade prioritária da triagem neonatal para hipotireoidismo congênito?
- Adulto com DM1 chega com vômitos, respiração profunda, cetonemia elevada e acidose metaból
- Idoso com DM2, infecção, desidratação profunda, hiperglicemia muito intensa e ausência de 
- Paciente com tireotoxicose e tireoide dolorosa após quadro viral apresenta evidência de li
- Em um nódulo tireoidiano com TSH baixo, antes da decisão sobre punção, qual investigação p
- Pessoa com hiponatremia, hiperpigmentação, hipotensão e hipercalemia apresenta cortisol ba
- Hipertenso resistente, mesmo com potássio normal, será investigado para hiperaldosteronism
- Em pessoa com LDL-c persistentemente elevado e risco cardiovascular alto, qual é a estraté
- Mulher pós-menopausa com fratura vertebral após trauma mínimo tem DXA sem T-score ≤−2,5. Q
- Pessoa em uso de inibidor de SGLT2 tem náuseas, taquipneia, beta-hidroxibutirato elevado e
- Paciente com lesão hipofisária tem T4 livre reduzido e TSH dentro da referência. Relata ta
- Mulher com Graves apresenta febre alta, confusão, taquiarritmia e insuficiência cardíaca a
- Gestante com antecedente de tratamento prolongado com glicocorticoide interrompe o medicam
- Homem tem aumento do tamanho dos anéis, prognatismo, cefaleia e sudorese excessiva. Qual e
- Mulher com amenorreia e galactorreia tem prolactina moderadamente elevada. Qual avaliação 
- Paciente apresenta cálcio elevado de modo persistente e PTH dentro da faixa laboratorial n
- Adolescente com aceleração da velocidade de crescimento, avanço de idade óssea e desenvolv
- O DM1 pode ocorrer após a infância e costuma exigir reposição de insulina por deficiência 
- Uma hemoglobina glicada de 6,1%, isoladamente, já atende ao critério diagnóstico usual de 
- Hipoglicemia grave é definida pela necessidade de ajuda de outra pessoa, mesmo que um valo
- No hipotireoidismo primário manifesto, o padrão laboratorial mais frequente é TSH baixo e 
- A tireotoxicose pode ocorrer sem aumento atual da síntese hormonal pela tireoide, como nas
- A zona fasciculada do córtex adrenal produz principalmente aldosterona, enquanto a zona gl
- O IMC é calculado dividindo o peso em quilogramas pela altura em metros elevada ao quadrad
- PTH age no rim para aumentar a excreção de fosfato e favorecer retenção de cálcio, de modo
- Em adulto sem sintomas ou crise hiperglicêmica inequívoca, apenas um resultado de glicemia
- O uso de insulina basal no DM1 visa cobrir necessidades metabólicas também em períodos sem
- Na CAD, normalizar a glicemia plasmática é suficiente para suspender a insulinoterapia int
- Anticorpos estimuladores do receptor de TSH podem causar produção tireoidiana excessiva na
- Na insuficiência adrenal secundária por lesão hipofisária, a deficiência mineralocorticoid
- A hipocalemia não é condição indispensável ao diagnóstico de hiperaldosteronismo primário.
- Na avaliação da dislipidemia, hipotireoidismo não tratado é uma causa secundária potencial
- Na hipocalcemia por hipoparatireoidismo, o PTH costuma estar claramente elevado em respost
- Na CAD, um potássio sérico inicial de valor normal afasta depleção corporal de potássio e 
- No estado hiperglicêmico hiperosmolar, a ureia contribui para a osmolaridade total, mas nã
- No hipotireoidismo central, ajustar levotiroxina apenas para normalizar o TSH é abordagem 
- A crise tireotóxica é definida exclusivamente por um limiar fixo e muito elevado de T4 liv
- Na suspeita forte de crise adrenal com instabilidade hemodinâmica, a coleta prévia de exam
- No feocromocitoma que exige preparo farmacológico, o betabloqueador isolado antes do bloqu
- Em adulto com hipercalcemia confirmada, um PTH na faixa normal pode ser inadequadamente el
- Na gravidez, a supressão leve do TSH no primeiro trimestre prova hipertireoidismo de Grave
```

## Pedido 43: Saneamento básico: abastecimento de água, esgoto, drenagem e resíduos (técnico) (`engenharia.saneamento-abastecimento-esgotamento`, 373 tópicos de edital)

```
Matéria: engenharia.saneamento-abastecimento-esgotamento (Saneamento básico: abastecimento de água, esgoto, drenagem e resíduos (técnico))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento do saneamento, população de projeto e viabilidade": 18
- "2. Captação, adução, recalque, reservação, distribuição e obras": 21
- "3. Tratamento de água, potabilidade e instalações prediais": 21
- "4. Coleta, transporte, tratamento e disposição de esgotos": 18
- "5. Drenagem, resíduos sólidos, indicadores, operação e saúde": 22

Já existem (não repita):
- Uma localidade com 5.000 habitantes consome, em média, 120 L/hab.dia. Desconsiderando outr
- Em comunidade abastecida por poço profundo, a água apresenta ferro elevado e possibilidade
- No esquema de abastecimento, uma tubulação conduz água já tratada da ETA ao reservatório d
- No estudo de consumo, uma vazão média diária precisa ser ajustada para o dia de maior dema
- Uma ETA convencional recebe água superficial com sólidos coloidais e microrganismos. Qual 
- Em sistema com cloração por cloro livre, qual residual mínimo deve ser mantido na distribu
- Uma amostra de água retirada de ponto de consumo apresentou Escherichia coli detectável em
- Um loteamento adotou rede separadora absoluta para esgoto sanitário. Qual encaminhamento d
- Na elaboração de plano de resíduos, qual é a primeira prioridade de gestão definida no art
- A vazão média de um setor é 20 L/s. Para fins de cálculo, adotam-se K1=1,20 e K2=1,50. Qua
- Uma ETE apresentou DBO de 250 mg/L no afluente e 50 mg/L no efluente, com medições compará
- Em balanço simplificado, entraram 2.000 m³ de água na distribuição e 1.700 m³ foram contab
- Uma ETE precisa diminuir matéria orgânica em processo anaeróbio com produção potencial de 
- Uma rede de esgoto deve atravessar por baixo de um canal, sem dispor de cota para manter o
- Duas alternativas de estação atendem à mesma vazão e padrão sanitário: uma tem menor inves
- Para uma pequena bacia, adota-se Q=0,278×C×i×A, com C=0,60, intensidade de 100 mm/h e área
- Um projeto prevê estrutura que armazena temporariamente água pluvial e a libera depois, re
- Uma cidade planeja abastecer 18.000 moradores a 150 L/hab.dia. Com K1=1,20, qual volume de
- Uma elevatória passou a emitir ruído de cavitação depois do rebaixamento do nível de água 
- Em controle de filtração rápida convencional, uma ETA apresenta 95% das amostras mensais c
- Em contrato de serviço público de água e esgoto, um planejamento cita o art. 11-B da Lei n
- Uma estação possui tratamento secundário por UASB, mas o efluente ainda apresenta risco mi
- Um relatório informa 900 m³ de esgoto tratado e 1.000 m³ coletados; estimam-se 1.500 m³ ge
- Em prédio com reservatório para chuva destinada a descargas sanitárias, há proposta de int
- Após chuva intensa, coletores de esgoto transbordam, enquanto bocas de lobo estão cheias d
- Em uma cidade com desníveis, o reservatório de distribuição pode equalizar diferenças entr
- Ao construir poço para abastecimento público, basta comprovar vazão suficiente, pois águas
- Em ETA convencional, a floculação favorece o crescimento dos agregados após a desestabiliz
- No separador absoluto implantado em bairro novo, a tubulação pública de esgoto sanitário f
- Na execução de rua pavimentada, sarjetas e bocas de lobo compõem a microdrenagem, recolhen
- Em plano de coleta seletiva, encaminhar recicláveis diretamente ao aterro é a primeira pri
- Uma rede opera com água potável mas apresenta Escherichia coli detectada em amostra de 100
- Uma vazão média de 25 L/s, submetida a K1=1,2 e K2=1,4, resulta em vazão máxima horária de
- Ao projetar elevatória, a diferença geométrica entre os níveis de sucção e recalque corres
- Em ETE, o tanque de lodos ativados normalmente opera com suprimento de oxigênio para suste
- Uma lagoa facultativa utilizada no tratamento de esgotos é caracterizada por condições ana
- Uma bacia de detenção implantada junto a um loteamento pode reduzir a vazão de pico a jusa
- Um relatório que informa que 100% do esgoto coletado recebeu tratamento demonstra, por ess
- Com a rede coletora em declive insuficiente para superar determinado obstáculo, um sifão i
- No método racional, uma chuva associada a período de retorno de 20 anos apresenta chance d
- Se a DBO afluente a uma ETE for 300 mg/L e a efluente 60 mg/L, a eficiência de remoção por
- Em sistema com água de chuva destinada apenas a descargas sanitárias, uma interligação dir
- Na avaliação de perdas, uma diferença entre volume de entrada e consumo autorizado contabi
- Pelo art. 11-B da Lei nº 11.445/2007, a meta de 90% até 31 de dezembro de 2033 refere-se a
- Em sistema com filtração rápida, resultado de turbidez de 0,8 uT em parte de até 5% das am
```

## Pedido 44: Legislação da Ebserh: lei de criação, estatuto, regimento, código de ética e regulamento de pessoal (`legislacao.ebserh`, 372 tópicos de edital)

```
Matéria: legislacao.ebserh (Legislação da Ebserh: lei de criação, estatuto, regimento, código de ética e regulamento de pessoal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Lei nº 12.550/2011: natureza, finalidade, contratos e pessoal": 17
- "2. Estatuto Social: assembleia, administração, fiscalização e governança": 17
- "3. Regimento Interno: Administração Central e hospitais-filiais": 17
- "4. Código de Ética e Conduta: princípios, conflitos e denúncias": 17
- "5. Regulamento de Pessoal: ingresso, direitos, deveres e disciplina": 16
- "6. Integração das normas e estratégia de resolução de questões": 16

Já existem (não repita):
- Quanto à natureza da Empresa Brasileira de Serviços Hospitalares, assinale a alternativa c
- Uma instituição pública federal de ensino pretende firmar contrato com a Ebserh. Qual cláu
- Sobre contratação de pessoal permanente na Ebserh, qual alternativa expressa a regra geral
- A lei prevê dispensa de licitação em determinada hipótese relacionada à atuação da Ebserh.
- Qual órgão se relaciona primordialmente à execução da gestão institucional da Ebserh?
- Ao comparar as instâncias previstas no Estatuto Social, qual descrição corresponde ao Cons
- Assinale a alternativa que distingue corretamente um comitê estatutário da auditoria inter
- Na organização regimental da rede, a expressão “Administração Central” refere-se principal
- Um hospital universitário federal é gerido pela Ebserh e integra a rede como filial. Qual 
- A respeito de estatuto, regimento e contrato com universidade, marque a associação correta
- Um empregado participa de avaliação de fornecedor em que trabalha um familiar próximo. Seg
- Sobre sigilo de dados de pacientes no ambiente da Ebserh, é correto afirmar que:
- Uma pessoa apresenta notícia fundamentada de possível transgressão ética na rede. Qual é o
- Em um edital para emprego permanente da Ebserh, consta a exigência de concurso público. Qu
- Acerca do procedimento disciplinar aplicável a empregados da Ebserh, assinale a opção adeq
- O regime de pessoal da Ebserh deve ser distinguido do regime estatutário dos servidores fe
- Ao estudar o conjunto de documentos institucionais da Ebserh, um candidato quer identifica
- Qual alternativa relaciona corretamente o instrumento à matéria que ele regula?
- A banca informa que uma versão nova do Regimento alterou siglas de unidades administrativa
- Considere as seguintes situações: I — contrato com universidade; II — consulta sobre denún
- Por ser vinculada ao Ministério da Educação, a Ebserh fica dispensada de observar as orien
- A personalidade jurídica de direito privado da Ebserh não elimina a fiscalização interna d
- A assinatura de contrato com instituição federal de ensino transforma automaticamente todo
- A possibilidade legal de ressarcimento por despesas de atendimento a beneficiários de plan
- No esquema de governança da Ebserh, o Conselho Fiscal é essencialmente órgão de execução d
- O Conselho Consultivo e a Diretoria Executiva possuem a mesma finalidade e exercem indisti
- Os comitês de assessoramento e a Auditoria Interna integram a governança, mas não devem se
- O Estatuto Social pode detalhar competências e composição dos órgãos da Ebserh, desde que 
- Na Rede Ebserh, Administração Central e hospitais universitários federais geridos pela emp
- O Regimento Interno de um hospital universitário pode revogar a Lei nº 12.550/2011 e conve
- Quando o edital exige a nomenclatura de um setor de gestão de pessoas, é prudente consulta
- É possível existir conflito de interesses mesmo sem comprovação de que o empregado recebeu
- A obrigação de proteger a confidencialidade de informações recebidas no trabalho termina s
- O recebimento de denúncia pelos canais institucionais dispensa qualquer apuração, pois a f
- Os empregados permanentes da Ebserh, embora admitidos mediante concurso público, estão suj
- Segundo a lógica do Regulamento de Pessoal, uma penalidade disciplinar não deve ser aplica
- A Lei nº 8.112/1990 substitui integralmente a CLT no vínculo dos empregados da Ebserh porq
- A lei de criação, o Estatuto Social e o Regimento Interno tratam de temas relacionados, ma
- Sempre que houver atualização do Código de Ética, ela poderá afastar o dever de cumprir a 
- Uma questão que mistura contratos com universidades, sanções trabalhistas e conflitos de i
```

## Pedido 45: Competência tributária (`direito-tributario.competencia-tributaria`, 371 tópicos de edital)

```
Matéria: direito-tributario.competencia-tributaria (Competência tributária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, características e exercício da competência tributária": 27
- "2. Indelegabilidade, capacidade ativa, arrecadação e parafiscalidade": 22
- "3. Competências da União, estados, Distrito Federal e municípios": 20
- "4. Competência comum, residual, extraordinária e limites constitucionais": 17
- "5. Repartição de receitas, não exercício e situações integradas de prova": 14

Já existem (não repita):
- Um município pretende criar, por lei própria, imposto sobre renda de seus residentes, aleg
- Uma lei municipal pretende atribuir a outra pessoa política o poder de criar e alterar o I
- Um estado contrata instituição financeira particular apenas para receber pagamentos de tri
- Em determinado estado, decidiu-se não instituir tributo estadual constitucionalmente permi
- Uma cidade deseja instituir taxa relacionada a serviço público específico e divisível de s
- Uma lei estadual institui cobrança sobre a propriedade de veículos automotores, dentro do 
- Um imóvel urbano está sujeito ao imposto sobre propriedade predial e territorial urbana in
- Na implantação da reforma sobre consumo, pergunta-se a quem pertence constitucionalmente a
- Uma empresa paga ITR sobre imóvel rural situado em determinado município que não optou por
- Um município aderiu ao regime constitucional que permite fiscalizar e cobrar ITR sobre imó
- Projeto de lei federal pretende criar novo imposto residual com base já própria de imposto
- Uma calamidade pública gera despesas extraordinárias e o governo federal analisa tributo t
- Uma lei estadual quer criar contribuição para custeio da iluminação pública de seus municí
- No fechamento do mês, uma empresa paga ICMS estadual. Qual fração geral da arrecadação est
- O gestor distrital afirma que o Distrito Federal só pode instituir impostos tipicamente es
- Uma lei municipal pretende tributar por IPTU patrimônio protegido por imunidade constituci
- O governo federal recebeu R$ 500 milhões de imposto criado validamente pela competência re
- Uma proposta de contribuição destinada a nova fonte de financiamento da seguridade social 
- Diante de guerra externa, a União edita imposto extraordinário que incide sobre materialid
- No ano de 2026, uma análise afirma que, por força da reforma tributária, ICMS e ISS já for
- Um município recebeu todo o IR retido na fonte sobre remunerações pagas por ele e por suas
- No rateio aos municípios dos 25% de ICMS estadual, uma proposta adota 65% pelo valor adici
- Um município contrata banco para receber tributos e outro ente público para exercer funçõe
- Uma empresa questiona se a existência da CBS e do IBS significa que ambos são impostos ins
- Ao elaborar quadro de impostos federais, um estudante excluiu Imposto Seletivo, afirmando 
- O CTN, art. 7º, distingue a competência indelegável de instituir tributo da atribuição leg
- Se a União não instituir imposto federal previsto na Constituição, um estado poderá exercê
- Os impostos IPVA e ITCMD são de competência dos estados e do Distrito Federal, ao passo qu
- Um banco contratado para receber tributos estaduais passa a exercer competência tributária
- O imposto sobre propriedade territorial rural continua sendo federal mesmo se município op
- Por serem instituíveis por qualquer ente, taxas são cobradas mesmo quando o ente não exerc
- O art. 154, I, admite imposto residual da União mediante lei complementar, observadas a nã
- O Imposto Seletivo do art. 153, VIII, e o IBS do art. 156-A são ambos impostos de competên
- O art. 158, III, destina aos municípios 50% do produto de IPVA nas condições territoriais 
- A instituição de novo imposto residual e a de imposto extraordinário de guerra estão subme
- O Distrito Federal acumula competências típicas estaduais e municipais para impostos, conf
- O art. 158, II, atribui 100% da receita do ITR a todo município onde houver imóvel rural, 
- A competência plena do art. 6º do CTN se exerce ressalvadas as limitações constitucionais;
- No ano de 2026, como ICMS e ISS já não compõem o sistema, qualquer questão de competência 
- A Constituição atribui aos estados e ao DF 20% do produto dos impostos federais instituído
- Para distribuir a parcela municipal de ICMS, o art. 158, § 1º, exige 75% no mínimo segundo
- A contribuição prevista no art. 149-A passou a compreender, além das atividades de ilumina
- No modelo constitucional da reforma, o Comitê Gestor do IBS pode editar leis municipais e 
- O art. 154, II, permite impostos extraordinários em caso ou iminência de guerra externa, i
- Segundo o CTN, art. 7º, § 2º, atribuições administrativas de fiscalização tributária confe
```

## Pedido 46: Procedimento licitatório, anulação, revogação e recursos (`direito-administrativo.procedimento-licitatorio`, 368 tópicos de edital)

```
Matéria: direito-administrativo.procedimento-licitatorio (Procedimento licitatório, anulação, revogação e recursos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sequência das fases, inversão e publicidade do procedimento": 15
- "2. Planejamento: ETP, termo de referência e projetos": 20
- "3. Julgamento, habilitação, impedimentos e atos intermediários": 19
- "4. Encerramento: saneamento, revogação, anulação e homologação": 29
- "5. Impugnações, recursos, reconsideração e efeito suspensivo": 17

Já existem (não repita):
- Uma unidade inicia licitação pelo rito comum e precisa registrar a ordem legal das fases, 
- Antes de divulgar o edital, a Administração considera vantajoso verificar a habilitação an
- Em licitação planejada sob forma presencial, o responsável alega que o processo não precis
- Na elaboração do ETP, a equipe pretende incluir apenas os itens expressamente mínimos do a
- Uma equipe já comparou alternativas e decidiu adquirir equipamentos. Agora precisa detalha
- Uma cidadã que não pretende apresentar proposta encontra irregularidade em edital e deseja
- Um edital exige demonstração da capacidade jurídica, técnica, fiscal/social/trabalhista e 
- Depois de publicado o edital, ocorreu fato novo comprovado que retirou a utilidade da cont
- Após o julgamento, a autoridade constata regra ilegal insanável que contaminou a classific
- Uma unidade federal analisa se precisa elaborar ETP em hipótese expressamente contemplada 
- Uma equipe tem ETP comprovando viabilidade de solução e inicia a descrição das condições d
- Em certame com habilitação antecipada, a comissão pretende exigir certidões de regularidad
- Um edital prevê prova de conceito para demonstrar interoperabilidade do sistema licitado. 
- Uma empresa elaborou o projeto básico da obra e pretende disputar a execução do mesmo obje
- Duas propostas ficaram empatadas no julgamento e não há hipótese especial que altere o pro
- A autoridade superior pretende revogar licitação por fato superveniente, mas os interessad
- Uma decisão motivada anulou a licitação. Um participante, intimado do ato, quer impugná-lo
- Na habilitação antecipada prevista no edital, uma licitante discorda do julgamento posteri
- Uma autoridade recebe recurso do art. 165, I, e não modifica sua decisão. Que prazos regem
- Após interposição de recurso de três dias úteis contra decisão de habilitação, o concorren
- Um ato administrativo foi objeto de recurso cabível nos termos da Lei nº 14.133/2021 e ain
- Após decisão administrativa de aplicar impedimento de licitar e contratar, o sancionado pr
- Uma entidade aplicou declaração de inidoneidade do art. 156, IV. A parte pretende contesta
- Um recurso demonstra erro em ato isolado de julgamento sem comprometer os atos preparatóri
- Uma obra será contratada pelo regime de contratação integrada. Na preparação, qual documen
- No rito comum da Lei nº 14.133/2021, as fases de divulgação do edital e apresentação das p
- A autoridade pode antecipar a habilitação após a divulgação do edital por simples escolha 
- A realização eletrônica da licitação é preferencial; se a forma presencial for legitimamen
- Um estudo técnico preliminar somente cumpre a Lei nº 14.133/2021 se reproduzir integralmen
- O ETP deve apresentar, no mínimo, a necessidade da contratação, estimativas de quantidades
- O termo de referência e o estudo técnico preliminar cumprem a mesma função, de modo que a 
- Mesmo em procedimento com habilitação antes do julgamento, a regularidade fiscal será exig
- A empresa autora do projeto básico pode concorrer à execução de obra correspondente sem an
- No empate entre propostas sujeitas à regra geral do art. 60, a disputa final entre empatad
- A referência de inexequibilidade inferior a 75% do valor orçado, prevista no art. 59, § 4º
- Antes de anular ou revogar a licitação, a autoridade deve assegurar prévia manifestação do
- A revogação da licitação pode ser fundada apenas em preferência pessoal que já existia ant
- A impugnação do edital por irregularidade pode ser apresentada por qualquer pessoa até trê
- Uma decisão de revogação licitatória é recorrível no prazo de três dias corridos, pois o a
- As contrarrazões ao recurso comum do art. 165 têm o mesmo prazo de sua interposição e come
- Em julgamento e habilitação, os recursos serão apreciados em duas fases autônomas, mesmo a
- Para sanção de impedimento de licitar e contratar, o art. 166 prevê recurso de quinze dias
- A declaração de inidoneidade admite recurso hierárquico comum em três dias úteis e não com
- O recurso e o pedido de reconsideração previstos na Lei nº 14.133/2021 possuem efeito susp
- Ao anular licitação por ilegalidade insanável de uma etapa, o art. 71, § 1º, exige elimina
```

## Pedido 47: Urologia e nefrologia: doenças do trato geniturinário, insuficiência renal e transplante (`medicina.urologia-nefrologia-transplante`, 367 tópicos de edital)

```
Matéria: medicina.urologia-nefrologia-transplante (Urologia e nefrologia: doenças do trato geniturinário, insuficiência renal e transplante)
Capítulos e quantas questões quero em cada um (total 100):
- "Anatomia, semiologia e infecções geniturinárias": 17
- "Litíase, obstrução e urgências urológicas": 17
- "Próstata, tumores geniturinários e cirurgia oncológica": 17
- "Lesão renal aguda, doença renal crônica e glomerulopatias": 17
- "Distúrbios renais, equilíbrio ácido-base e diálise": 16
- "Transplante renal, imunologia e Lei de Transplantes": 16

Já existem (não repita):
- Um homem apresenta hematúria, edema e hipertensão. Qual achado urinário reforça a origem g
- Mulher sem sintomas urinários realizou exame de rotina e apresentou bacteriúria. Em que si
- Paciente com disúria crônica, piúria e uroculturas convencionais repetidamente negativas a
- Adulto não gestante chega com dor cólica no flanco irradiada para virilha e suspeita de cá
- Homem com cálculo ureteral obstrutivo apresenta febre, hipotensão e hidronefrose. Qual seq
- Adolescente com dor escrotal abrupta, vômitos e testículo elevado procura emergência. Qual
- Homem apresenta ereção muito dolorosa por seis horas, sem estímulo sexual, com corpos cave
- Após trauma pélvico, o paciente apresenta sangue no meato uretral e incapacidade de urinar
- Em qual região prostática a hiperplasia benigna costuma surgir, provocando sintomas obstru
- Homem com PSA elevado, sem diagnóstico histológico, solicita confirmação imediata de cânce
- Qual achado diferencia de maneira central tumor vesical não músculo-invasivo de tumor vesi
- Adulto jovem com nódulo sólido testicular indolor e ultrassom suspeito de tumor germinativ
- Paciente com pequeno tumor renal localizado, de anatomia favorável, e função renal preserv
- Após choque hipovolêmico, a creatinina de um adulto aumenta 0,4 mg/dL em 48 horas. Qual in
- Paciente mantém relação albumina/creatinina urinária de 450 mg/g em exames separados por q
- Paciente com edema, hipertensão, urina avermelhada e cilindros hemáticos tem qual síndrome
- Paciente com hipertensão resistente e episódios recorrentes de edema pulmonar abrupto tem 
- Paciente com DRC apresenta potássio muito elevado e alterações eletrocardiográficas. Qual 
- Qual situação é indicação clássica de avaliar terapia dialítica urgente, independentemente
- Após morte encefálica corretamente constatada, familiares recusam doar órgãos. Qual interp
- Paciente com acidose metabólica hiperclorêmica, cálculo renal recorrente e dificuldade de 
- No transplante renal, o que melhor descreve rejeição hiperaguda em um receptor com anticor
- Uma pessoa capaz deseja doar um rim gratuitamente para amigo sem vínculo conjugal ou paren
- Em uma gasometria com acidemia, HCO3- reduzido e ânion gap elevado, qual alternativa repre
- Sobre condutas descritas como crimes na Lei nº 9.434/1997, qual situação se enquadra na ve
- Coágulos urinários e hematúria macroscópica demonstram obrigatoriamente glomerulonefrite.
- Um resultado de nitrito negativo no EAS exclui infecção urinária bacteriana.
- Em paciente sem sintomas urinários, tratar bacteriúria assintomática de rotina não é recom
- A tuberculose geniturinária exige sintomas pulmonares associados para ser considerada no d
- A associação de uropatia obstrutiva e infecção sistêmica exige controle de foco com drenag
- Em torção testicular com alta suspeita clínica, a ultrassonografia Doppler deve ser obriga
- No priapismo isquêmico, a isquemia prolongada dos corpos cavernosos pode causar fibrose e 
- Após trauma pélvico com sangue no meato, é seguro forçar sonda uretral até vencer eventual
- Cálculos urinários de ácido úrico podem não aparecer em radiografia simples, embora geralm
- Sintomas urinários por hiperplasia prostática benigna são proporcionais, de modo fixo, ao 
- A invasão do detrusor é um divisor relevante na classificação e no tratamento do carcinoma
- Uma massa sólida testicular indolor pode representar neoplasia maligna e deve ser investig
- Nefrectomia radical é sempre superior à nefrectomia parcial para qualquer tumor renal loca
- A LRA pode ser diagnosticada pela queda do débito urinário, ainda que a creatinina não ten
- A DRC pode existir com TFG acima de 60 mL/min/1,73m² quando há marcador de lesão renal per
- Síndrome nefrótica é caracterizada classicamente por proteinúria maciça, hipoalbuminemia e
- A presença de hipertensão renovascular implica indicação obrigatória de angioplastia ou ci
- Hipercalemia grave pode ser perigosa mesmo sem alterações eletrocardiográficas evidentes e
- Na hipercalemia, insulina com glicose elimina diretamente potássio do organismo, dispensan
- A indicação de diálise aguda é determinada exclusivamente pelo valor absoluto da creatinin
- No transplante renal, uma prova cruzada positiva relevante pode indicar anticorpos pré-for
- A Lei nº 9.434/1997 admite a comercialização de órgãos humanos se a pessoa doadora for mai
- O diagnóstico de morte encefálica para fins de doação deve ser realizado conforme protocol
- Na doação entre vivos destinada a pessoa não abrangida pelas hipóteses legais de parentesc
```

## Pedido 48: Crimes contra a incolumidade pública e a fé pública (`direito-penal.crimes-incolumidade-fe-publica`, 365 tópicos de edital)

```
Matéria: direito-penal.crimes-incolumidade-fe-publica (Crimes contra a incolumidade pública e a fé pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Perigo comum: incêndio, explosão, inundação e desastres": 17
- "2. Segurança de serviços, saúde pública e paz pública": 17
- "3. Moeda falsa, papéis públicos e petrechos de falsificação": 22
- "4. Falsidade documental, identidade e previdência social": 23
- "5. Fraudes em certames, concursos de normas e revisão comparativa": 21

Já existem (não repita):
- Uma pessoa provoca deliberadamente incêndio que se propaga e ameaça efetivamente as casas 
- Um engenho de dinamite é colocado intencionalmente em local com pessoas e bens expostos a 
- Durante fiscalização, verifica-se que alguém mantém, sem licença, substância explosiva des
- Durante calamidade por inundação, uma pessoa oculta intencionalmente equipamento destinado
- Em fato praticado após a alteração de 2026, alguém interrompe dolosamente serviço telefôni
- Uma pessoa infringe determinação pública específica destinada a impedir propagação de doen
- Durante reunião aberta ao público, uma pessoa incentiva explicitamente outras a cometer de
- Três pessoas estabelecem associação estável com finalidade específica de cometer crimes fu
- Uma pessoa recebe de boa-fé cédula falsificada, acreditando ser verdadeira; depois constat
- Uma pessoa fabrica documento oficial materialmente inautêntico, apto a enganar, reproduzin
- Uma pessoa tenta pagar uma compra com cédula cuja falsidade grosseira é perceptível a olho
- Em uma atividade comercial, alguém falsifica materialmente selo destinado a controle tribu
- Uma empresa inclui em folha de pagamento enviada para prova previdenciária uma pessoa que 
- Alguém preenche documento público autêntico com informação deliberadamente falsa sobre fat
- Uma pessoa apresenta, sabendo da fraude, documento público materialmente falsificado por t
- Durante procedimento de seleção pública, alguém divulga indevidamente material ainda sigil
- A divulgação indevida de conteúdo sigiloso de exame previsto no art. 311-A resulta comprov
- Em 2026, uma associação criminosa estável tem três integrantes e utiliza armas para sua fi
- Um crime doloso de perigo comum do capítulo dos arts. 250 a 259 causa, como consequência a
- Uma pessoa sabe, desde o momento da aquisição, que várias cédulas são falsas e as guarda p
- Em investigação de falsificação material, foi fabricado testamento particular inautêntico.
- Uma declaração ideologicamente falsa é inserida em documento particular autêntico para cri
- Alguém destrói deliberadamente, em benefício próprio, documento público verdadeiro sobre o
- Um documento público materialmente falso é apresentado conscientemente a uma instituição d
- Uma pessoa investida em função pública facilita deliberadamente acesso de candidatos não a
- Durante incêndio intencional que efetivamente expôs imóveis vizinhos a perigo, ninguém sof
- Para configurar o art. 253 do CP, relativo à posse não autorizada de substância explosiva,
- O art. 269 do CP tipifica especificamente a omissão do médico em notificar à autoridade pú
- O art. 287 do CP, sobre apologia de crime ou criminoso, exige associação estável de três o
- A falsificação de papel-moeda de curso legal no estrangeiro também é abrangida pelo art. 2
- Se um incêndio é praticado culposamente, o art. 250, § 2º, determina a mesma pena de reclu
- Se de um crime doloso de perigo comum resulta morte nas condições do art. 258 do CP, a pen
- Na redação de 2026, interromper serviço telefônico nos termos do art. 266 do CP permanece 
- A redação do art. 288, § 2º, incluída em 2025, prevê a mesma pena do caput para quem solic
- Conforme a Súmula 73 do STJ, utilizar papel-moeda grosseiramente falsificado configura nec
- Para fins penais do art. 297 do CP, o testamento particular e os livros mercantis equipara
- Segundo o art. 298, parágrafo único, do CP, o cartão de crédito ou de débito é equiparado,
- O art. 297, § 4º, do CP alcança, nas condições legais, a omissão de remuneração, identific
- A Súmula 546 do STJ determina que o crime de uso de documento público falso é julgado com 
- Médico que, no exercício da profissão, fornece atestado falso pode incidir no art. 302 do 
- Uma pessoa utiliza como própria identificação verdadeira pertencente a terceiro, sem falsi
- Ao divulgar indevidamente questões ainda sigilosas de certame abrangido pelo art. 311-A, c
- Se um particular facilita acesso de pessoa não autorizada a gabarito sigiloso de exame púb
- O art. 293, § 4º, do CP possui disciplina própria para quem recebeu de boa-fé papel públic
- O art. 288-A do Código Penal, sobre milícia privada, impõe a mesma pena de um a três anos 
```

## Pedido 49: Sistemas operacionais: conceitos (processos, threads, memória e arquivos) (`informatica.sistemas-operacionais-conceitos`, 363 tópicos de edital)

```
Matéria: informatica.sistemas-operacionais-conceitos (Sistemas operacionais: conceitos (processos, threads, memória e arquivos))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, arquiteturas e entrada e saída": 21
- "2. Processos, threads e escalonamento": 19
- "3. Concorrência, sincronização e deadlocks": 16
- "4. Gerenciamento de memória e substituição de páginas": 23
- "5. Organização de arquivos e sistemas de arquivos": 21

Já existem (não repita):
- Uma aplicação solicita ao sistema gravar um texto sem manipular diretamente os registrador
- Um aplicativo tenta executar diretamente uma instrução privilegiada de configuração de mem
- Um processo em modo usuário solicita a leitura de um arquivo, exigindo intervenção control
- Uma controladora transfere um bloco entre dispositivo e memória principal com pouca partic
- Em um computador de uma única CPU, um sistema alterna rapidamente aplicações para que usuá
- Após ser retirado da CPU, um processo precisará retomar a execução com registradores e est
- Um processo filho concluiu sua execução, mas o pai ainda não recolheu seu estado final. Qu
- Duas threads de um mesmo processo atualizam dados globais e executam rotinas diferentes. Q
- Quatro processos prontos recebem a CPU em ordem circular, cada um limitado a um quantum an
- Três processos chegam juntos, com rajadas de 4, 2 e 3 unidades, nessa ordem. Em FCFS não p
- Uma tarefa chega em t=2, executa 6 unidades de CPU no total e termina em t=13, sem outras 
- P1 chega em t=0 com 8 unidades de CPU. P2 chega em t=2 com 3 unidades. Sob SRTF, sem custo
- Duas tarefas ficam travadas: A detém R1 e aguarda R2; B detém R2 e aguarda R1, sem liberaç
- Um conjunto de tarefas precisa controlar o acesso simultâneo a três conexões intercambiáve
- Um módulo encapsula dados compartilhados, operações que os acessam com exclusão e condiçõe
- Na ordem de inspeção, as lacunas de memória livre têm 25 KiB, 18 KiB e 10 KiB. Uma solicit
- Um processo acessa a página virtual 3 de tamanho 4.096 bytes, deslocamento 212, mapeada no
- Uma referência à memória não encontra tradução na TLB, mas a tabela de páginas indica pági
- Três páginas estão na memória: A chegou primeiro, depois B e C; após essas entradas, A foi
- Em uma simulação de substituição, aumentar a quantidade de quadros disponíveis provoca mai
- Um processo acessa página virtual válida, porém ausente da RAM; o sistema seleciona um qua
- Um arquivo de grande porte cresce repetidamente, mas novos blocos livres surgem dispersos.
- Em um sistema de arquivos com inodes, dois nomes são hard links para o mesmo arquivo. Um n
- Um arquivo único de 5 GiB precisa ser gravado em mídia com 20 GiB livres. Uma cópia falha 
- Após queda de energia durante gravação em ext4 configurado no modo usual de journaling, a 
- Uma aplicação solicita a gravação de um documento ao SO; o núcleo coordena acesso ao dispo
- Um periférico executa transferência por DMA; nessa modalidade, o controlador deixa de pode
- Um processo apto a executar aguarda receber CPU, enquanto outro espera a conclusão de uma 
- Duas threads pertencentes ao mesmo processo executam simultaneamente; por serem threads di
- Um arquivo possui dois hard links no mesmo sistema de arquivos; a remoção de um nome não t
- Em um sistema preemptivo, se uma tarefa que estava executando perde a CPU pelo término de 
- Em um programa, duas threads executam a sequência ler-contador, somar um e gravar-contador
- O algoritmo do banqueiro encontra um estado inseguro após examinar uma solicitação de recu
- Com páginas de 4.096 bytes, o endereço lógico 9.000 está na página 2, com deslocamento de 
- A consulta a uma página residente na RAM pode sofrer falha na TLB, mas uma falha na TLB si
- Um sistema adota ordem global R1 antes de R2 para todas as tarefas que precisam de ambos o
- Em um grafo de alocação com vários exemplares disponíveis de determinado recurso, a simple
- Em round robin com quantum de 2 e processos P1=5, P2=3, ambos chegados em zero e inicialme
- Um SJF não preemptivo e um SRTF produzirão necessariamente a mesma sequência de execução q
- Na segmentação paginada, o programa pode manter segmentos lógicos distintos, cada um divid
- Três quadros contêm páginas carregadas na ordem 1, 2, 3; a página 1 é acessada novamente e
- Um link simbólico que armazena caminho para arquivo de outro sistema de arquivos pode cont
- Um arquivo de 5 GiB não cabe como arquivo único no FAT32, mas basta ativar a extensão de n
- Uma aplicação com muitas faltas de página gasta mais tempo trocando conteúdo com armazenam
- Em um volume formatado com ReFS, a presença de mecanismos de resiliência permite afirmar q
```

## Pedido 50: Fisioterapia: avaliação, recursos terapêuticos e áreas clínicas (`fisioterapia.avaliacao-recursos-areas-clinicas`, 362 tópicos de edital)

```
Matéria: fisioterapia.avaliacao-recursos-areas-clinicas (Fisioterapia: avaliação, recursos terapêuticos e áreas clínicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Avaliação cinético-funcional, semiologia e provas de força": 21
- "2. Exercícios terapêuticos e recursos físicos com segurança": 20
- "3. Ortopedia, traumatologia, órteses e próteses": 19
- "4. Neurologia, neuropediatria e fisioterapia geriátrica": 21
- "5. Reabilitação cardiorrespiratória, saúde pélvica e obstetrícia": 19

Já existem (não repita):
- Durante avaliação de extensão do joelho, uma pessoa contrai o quadríceps de forma palpável
- Na avaliação de flexores do cotovelo, o paciente completa a amplitude com o antebraço sust
- Uma pessoa consegue elevar o braço pela amplitude completa contra gravidade, mas não supor
- Após entrevista, uma paciente relata dor no quadril, dificuldade para subir escadas e impo
- Em uma piscina terapêutica, um paciente com dor ao apoiar o peso do corpo realiza marcha c
- Uma pessoa realiza extensão de joelho sentada com tornozelo livre e resistência ajustável.
- Uma pessoa com dor recebe correntes por eletrodos superficiais para aliviar sintomas, sem 
- Um paciente com paresia dos dorsiflexores tropeça durante a fase de balanço, mas conserva 
- Um adulto com osteoartrite de joelho necessita plano conservador de longo prazo. Segundo a
- Durante descida lenta da cadeira até sentar, a pessoa controla a flexão de quadris e joelh
- Uma pessoa com fenômeno de Raynaud importante é encaminhada para aplicação de bolsa gelada
- Um paciente com suspeita recente de trombose venosa profunda e edema unilateral doloroso s
- Após AVC, um paciente tem fraqueza e dificuldade para se levantar de cadeira. A equipe pre
- Uma criança com paralisia cerebral é classificada pelo GMFCS. O familiar pergunta se o nív
- Uma idosa com histórico de duas quedas, fraqueza de membros inferiores e receio de caminha
- Uma paciente relata perda involuntária de urina ao tossir, mas não descreve desejo súbito 
- Uma mulher com incontinência urinária de esforço inicia treinamento do assoalho pélvico su
- Uma pessoa com DPOC apresenta dispneia aos esforços e produção de pouca secreção. Pretende
- Em pós-AVC, um paciente apresenta alta resistência ao movimento passivo rápido do tornozel
- Uma pessoa com osteoartrite de quadril solicita substituir exercício por terapia manual is
- Uma mulher apresenta prolapso de órgão pélvico sintomático que não ultrapassa mais de um c
- Uma paciente com amputação transtibial começa a usar prótese e sente dor focal no encaixe,
- Em reabilitação cardíaca, durante exercício supervisionado, uma pessoa desenvolve dor torá
- Uma criança com paralisia cerebral apresenta dificuldades de transferir-se em casa e parti
- Um paciente apresenta SpO₂ de 96% em repouso, mas sonolência progressiva e suspeita de ret
- Durante teste muscular manual, a pessoa completa movimento de flexão do joelho contra a gr
- Durante reavaliação da mobilidade, um paciente obtém 56 pontos na Escala de Equilíbrio de 
- Em hidroterapia, o empuxo pode reduzir o peso aparente sustentado pelas articulações, enqu
- Um paciente com amputação transtibial necessita de apoio e substituição do segmento ausent
- Após AVC, o treino repetido de levantar-se de uma cadeira pode integrar um programa de for
- A perda de urina associada à tosse caracteriza tipicamente incontinência de urgência, cujo
- A avaliação de um paciente com amplitude ativa de ombro reduzida, mas amplitude passiva qu
- Em pessoa com osteoartrite de joelho, a NICE NG226 recomenda rotineiramente ultrassom tera
- No teste de caminhada de seis minutos, a distância percorrida pode acompanhar capacidade f
- Uma pessoa com fenômeno de Raynaud e sensibilidade protetora comprometida constitui indica
- Em pessoas idosas, a OMS recomenda combinar atividades que enfatizem equilíbrio funcional 
- O GMFCS, ao classificar função motora grossa de pessoas com paralisia cerebral, utiliza ci
- A diretriz NICE NG210 recomenda considerar treinamento supervisionado do assoalho pélvico 
- Um paciente possui marca-passo cardíaco implantado e deseja usar TENS por conta própria pr
- Após amputação transtibial, o aumento de dor focal no encaixe durante a descarga de peso d
- A recomendação NICE NG236 para reabilitação após AVC considera treinamento de força incomp
- Um paciente com suspeita de hipercapnia pode apresentar oximetria periférica relativamente
- Em mulher com incontinência urinária mista, a NICE NG123 recomenda substituir de rotina o 
- Um idoso apresenta lentidão no TUG e histórico de quedas. Para planejar prevenção, a avali
- Em gestante clinicamente estável, a presença de incontinência urinária de esforço torna ob
```

