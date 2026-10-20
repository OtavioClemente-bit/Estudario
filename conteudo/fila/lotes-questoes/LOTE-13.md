# Pedidos de questões extras, lote 13 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 7430 tópicos de edital.

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

## Pedido 1: Terapia intensiva: ventilação mecânica, choque, sepse e monitorização (`medicina.terapia-intensiva`, 162 tópicos de edital)

```
Matéria: medicina.terapia-intensiva (Terapia intensiva: ventilação mecânica, choque, sepse e monitorização)
Capítulos e quantas questões quero em cada um (total 100):
- "Organização da UTI e monitorização do paciente crítico": 17
- "Choque circulatório e drogas vasoativas": 17
- "Sepse, choque séptico e falência de múltiplos órgãos": 17
- "Insuficiência respiratória, SDRA e ventilação mecânica": 17
- "Sedação, distúrbios metabólicos e nutrição do paciente grave": 16
- "Prevenção de IRAS, paliatividade e diagnóstico de morte encefálica": 16

Já existem (não repita):
- Qual situação expressa a indicação mais característica de terapia intensiva em um adulto?
- Em relação ao cateter arterial e ao acesso venoso central, qual associação é correta?
- Um paciente apresenta hipotensão, extremidades frias e PVC elevada. Qual é a melhor interp
- Em paciente sob ventilação e em uso de vasopressores, qual achado torna inadequada a inter
- Qual conclusão é apropriada após manobra de elevação passiva das pernas que aumenta o volu
- Em um paciente com embolia pulmonar maciça e choque, qual mecanismo circulatório predomina
- No choque cardiogênico com congestão pulmonar e evidência ecográfica de disfunção ventricu
- No choque séptico em que há crescimento da dose de noradrenalina, qual associação farmacol
- Em suspeita de tamponamento cardíaco com choque, qual estratégia está mais alinhada ao mec
- Um paciente com infecção suspeita apresenta mudança aguda do estado mental e piora de funç
- No tratamento do choque séptico, qual droga é recomendada como vasopressor inicial para a 
- Conforme a SSC 2026, um adulto com **possível** sepse, mas sem choque, está estável e aind
- Sobre ressuscitação por líquidos em hipoperfusão induzida por sepse, qual leitura da SSC 2
- Um paciente com SDRA apresenta obesidade importante e edema generalizado. Como selecionar 
- Em ventilação controlada por volume, observa-se elevação importante da pressão de pico, se
- Qual indicação representa um uso clássico e bem fundamentado de VNI em adulto?
- Paciente recupera estabilidade, passa em TRE e apresenta tosse muito ineficaz com grande v
- Em paciente sem comunicação confiável, com suspeita de dor e delirium, qual associação de 
- Qual conjunto constitui medida adequada de prevenção de infecção urinária relacionada a ca
- Em paciente com lesão encefálica grave e coma, qual é o processo correto para confirmar mo
- A indicação de UTI deve considerar necessidade de suporte orgânico, potencial benefício, g
- Uma pressão venosa central baixa, isoladamente, confirma que determinado paciente receberá
- Todo paciente em choque precisa estar hipotenso, pois pressão arterial normal exclui neces
- No choque cardiogênico com edema pulmonar, administrar volume repetidamente sem verificar 
- Em um paciente com tamponamento e hipotensão, a titulação isolada de noradrenalina elimina
- O escore qSOFA, por si só, estabelece o diagnóstico de sepse e um resultado baixo descarta
- Na sepse provável ou confirmada sem choque, a diretriz SSC 2026 recomenda antimicrobianos 
- De acordo com a SSC 2026, a sugestão de volume inicial de cristaloide em choque séptico co
- A ausência de crescimento em hemoculturas não exclui sepse; as decisões clínicas dependem 
- Na Sepsis-3, os critérios clínicos consensuais do choque séptico combinam necessidade de v
- Na SDRA, utilizar volume corrente calculado com peso corporal predito, e não pelo peso cor
- A pressão de pico medida durante a inspiração é sempre idêntica à pressão de platô, mesmo 
- Na SDRA moderada ou grave, manobras prolongadas de recrutamento alveolar são recomendadas 
- Em exacerbação de DPOC com acidose respiratória hipercápnica, ventilação não invasiva pode
- Em paciente sedado e sem comunicação, RASS é escala específica para diagnóstico de deliriu
- Hipocalemia persistente pode exigir investigação e correção de hipomagnesemia concomitante
- A insulina com glicose pode reduzir rapidamente o potássio sérico na hipercalemia ao deslo
- Na prevenção de pneumonia associada à ventilação mecânica, reduzir exposição desnecessária
- A adoção de cuidados paliativos em UTI implica suspensão obrigatória de toda terapia ativa
- No Brasil, a confirmação de morte encefálica pode ser reduzida à tomografia com lesão exte
```

## Pedido 2: Prescrição e decadência administrativas (`direito-administrativo.prescricao-decadencia-administrativa`, 161 tópicos de edital)

```
Matéria: direito-administrativo.prescricao-decadencia-administrativa (Prescrição e decadência administrativas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, competências e espécies de prazo": 20
- "2. Decadência da autotutela e estabilização de atos": 20
- "3. Prescrição punitiva, intercorrente e executória": 20
- "4. Pretensões contra a Fazenda e trato sucessivo": 20
- "5. Processo administrativo, controle e jurisprudência": 20

Já existem (não repita):
- Uma autoridade federal identifica ilegalidade em ato que concedeu vantagem individual de b
- Um ato federal de janeiro de 2021 concede parcela mensal favorável, mas o primeiro pagamen
- Uma entidade federal apura infração instantânea ao poder de polícia, praticada em 2022. De
- Um processo sancionador de polícia federal permanece sem despacho ou julgamento por três a
- Uma pessoa busca cobrar judicialmente diferença financeira decorrente de ato isolado da Fa
- Uma pessoa recebe prestação mensal da Fazenda em valor menor que o devido, sem negativa do
- Uma decisão administrativa federal é comunicada regularmente à parte. Não há legislação es
- Uma parte recebe ciência formal de ato impugnável por mandado de segurança. Desconsiderada
- Uma infração funcional é imputada a integrante da estrutura administrativa federal. A auto
- Um ato federal favorável produz pagamentos mensais desde maio de 2020. Em abril de 2025, a
- Uma infração permanente de polícia federal começou em 2019 e cessou em setembro de 2023. N
- Durante apuração de infração federal de polícia, ocorre notificação válida do acusado. Um 
- Após regular processo de polícia federal, uma multa não tributária torna-se crédito defini
- Uma vantagem de pagamento continuado foi expressamente indeferida por ato específico da Fa
- Uma administração estadual pretende rever ato favorável; há controvérsia sobre o prazo, e 
- Um ato federal contém defeito sanável de competência, e sua correção não causa lesão ao in
- Depois de definitiva uma sanção administrativa federal, surgem documentos novos relevantes
- Uma pessoa apresenta reclamação administrativa relativa a fato contra a Fazenda, sem que e
- Uma credora protocola requerimento administrativo formal buscando reconhecimento de dívida
- Depois de definitivamente constituído um crédito de multa administrativa federal, a Admini
- O ato inicial de concessão de aposentadoria chega à corte de contas em agosto de 2020, par
- Duas pretensões de ressarcimento ao erário são comparadas: a primeira é fundamentada em at
- Em processo administrativo federal comum, uma parte é cientificada na segunda-feira, sem f
- Uma infração administrativa federal de polícia também constitui crime. Sem pressupor que h
- Uma ação de improbidade discute prazos após a Lei nº 14.230/2021. A parte sustenta que os 
- Uma pessoa obteve ato federal favorável sem má-fé e a Administração permaneceu inerte por 
- Uma vantagem mensal concedida pela Administração começa a ser paga dois meses após a ediçã
- Uma infração instantânea de polícia federal foi praticada e não se verificaram causas que 
- Um procedimento sancionador federal está em tramitação efetiva há quatro anos, com despach
- Uma pessoa ingressa em juízo buscando prestação contra a Fazenda sem disciplina especial. 
- Em processo administrativo federal comum, a parte recebe ciência de decisão, sem legislaçã
- Um ato favorável federal foi concedido mediante fraude consciente do destinatário e a má-f
- Durante ação punitiva de polícia federal, a autoridade efetua notificação formal válida do
- Uma sanção de multa federal resulta em crédito não tributário definitivamente constituído 
- Uma pessoa possui prestações mensais pagas a menor, sem decisão que negue o direito. A Súm
- Uma unidade federativa não possui lei específica para o prazo decadencial de revisão dos a
- Depois da decisão definitiva que aplicou sanção administrativa, surgem fatos novos relevan
- Uma pessoa quer impetrar mandado de segurança contra ato administrativo do qual teve ciênc
- Um processo de concessão inicial de pensão chega à corte de contas em 2021, embora o ato c
- Em processo administrativo federal sujeito à Lei nº 9.784/1999, um prazo em dias corre con
- Uma pretensão de ressarcimento ao erário se fundamenta unicamente em decisão definitiva de
- Uma ação de ressarcimento busca recompor dano ao erário por conduta dolosa tipificada na L
- Um procedimento disciplinar federal examina falta funcional. Por haver sanção administrati
- Uma autoridade federal formaliza dentro do quinquênio medida fundamentada que impugna a va
- Uma pretensão contra a Fazenda tem a prescrição interrompida no regime geral do Decreto nº
```

## Pedido 3: Sistema das Nações Unidas e os direitos humanos (`direito.sistema-nacoes-unidas-direitos-humanos`, 160 tópicos de edital)

```
Matéria: direito.sistema-nacoes-unidas-direitos-humanos (Sistema das Nações Unidas e os direitos humanos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Carta da ONU: princípios, órgãos e multilateralismo": 19
- "2. Declaração Universal, PIDCP e PIDESC": 17
- "3. Órgãos de tratados, EPU e procedimentos especiais": 24
- "4. Agências, programas e cooperação da ONU": 20
- "5. Implementação internacional, responsabilidades nacionais e limites": 20

Já existem (não repita):
- Ao estudar a Carta de 1945, uma pessoa procura o dispositivo que situa a promoção universa
- Um resumo lista os órgãos principais da ONU e acrescenta o Conselho de Direitos Humanos co
- Na Assembleia Geral, será votada uma questão importante, como a admissão de membro. Qual q
- Um relatório institucional precisa registrar a composição do Conselho de Segurança previst
- Um material afirma que a Declaração Universal de Direitos Humanos dependeu de ratificação 
- Uma pessoa consulta a DUDH para sustentar proteção à educação e ao desenvolvimento da pers
- Uma equipe examina os Pactos das Nações Unidas de 1966 e quer saber qual direito figura ex
- Um texto brasileiro cita Decreto nº 591/1992 como se fosse a promulgação do PIDCP e Decret
- Em votação substantiva do Conselho de Segurança, dez membros votam favoravelmente, quatro 
- Após reconhecer ameaça à paz, o Conselho de Segurança considera medidas obrigatórias não m
- Ao apresentar relatório sobre implementação do PIDCP, uma pessoa quer identificar o comitê
- Em estudo comparativo, afirma-se que existem dez órgãos convencionais, mas nove tratados n
- Uma avaliação cobre periodicamente a situação dos direitos humanos de todos os membros da 
- Uma relatoria independente recebe informação urgente sobre possível violação grave e solic
- Uma rede escolar busca cooperação internacional para educação, ciência e patrimônio cultur
- Uma análise sobre a proteção brasileira menciona dignidade humana como fundamento interno 
- Uma pessoa pretende submeter ao Comitê de Direitos Humanos denúncia individual por violaçã
- Uma revisão de fontes oficiais aponta nove tratados centrais e dez órgãos convencionais de
- Um relatório qualifica observações conclusivas de comitê convencional como sentenças da Co
- Em projeto interagências, é necessário escolher quem promove direitos de crianças e quem t
- Um caso envolve grave violação de direitos humanos no Brasil e risco de descumprimento de 
- Uma pessoa compara a posição de tratado de direitos humanos aprovado pelo rito do art. 5º,
- Uma pessoa tenta utilizar a Corte Internacional de Justiça como se fosse tribunal competen
- Um texto afirma que qualquer resolução da Assembleia Geral constitui obrigação coercitiva 
- Uma organização diz que, diante do PIDESC, a realização progressiva autoriza eliminar prog
- Ao preparar exposição sobre a Carta da ONU, é correto apontar que a cooperação internacion
- O Conselho de Direitos Humanos é um dos seis órgãos principais previstos originariamente n
- Em questões importantes da Assembleia Geral, como a admissão de novo membro, a Carta exige
- A Declaração Universal de 1948 é um tratado ratificado pelos Estados membros da ONU e só s
- A Declaração Universal abrange tanto direitos como vida e liberdade de expressão quanto ed
- O Decreto brasileiro nº 591/1992 promulgou o Pacto Internacional sobre Direitos Civis e Po
- O Comitê de Direitos Humanos é órgão composto por especialistas independentes que monitora
- Em matéria substantiva no Conselho de Segurança, abstenção de um membro permanente equival
- O Capítulo VII da Carta, em especial os arts. 39 e 41, permite que o Conselho de Segurança
- O Pacto Internacional sobre Direitos Econômicos, Sociais e Culturais autoriza o Estado a a
- Mesmo quando há emergência pública reconhecida nos termos do art. 4º do PIDCP, a proibição
- O Exame Periódico Universal é julgamento de uma petição individual por comitê de peritos e
- Agências especializadas da ONU, como OIT e OMS, mantêm personalidade e instrumentos consti
- UNICEF, PNUD e ACNUR são três agências especializadas idênticas à OIT em estrutura jurídic
- A aceitação de um tratado de direitos humanos pelo Estado não implica, por si, aceitar tod
- Por serem criados no âmbito do Conselho de Direitos Humanos, os procedimentos especiais po
- O art. 5º, § 3º, da Constituição permite equivalência de tratado de direitos humanos à eme
- Em grave violação de direitos humanos ocorrida no Brasil, qualquer pessoa pode instaurar d
- A Corte Internacional de Justiça, principal órgão judiciário da ONU, resolve litígios entr
- As observações conclusivas de órgão de tratado, as recomendações do EPU e as decisões do C
```

## Pedido 4: Uso dos Porquês e Dúvidas Frequentes de Grafia (`portugues.porques-duvidas-grafia`, 160 tópicos de edital)

```
Matéria: portugues.porques-duvidas-grafia (Uso dos Porquês e Dúvidas Frequentes de Grafia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Os quatro porquês": 25
- "2. A/há, mal/mau, onde/aonde e senão/se não": 25
- "3. Outros pares que caem em prova": 25
- "4. Método para resolver questões": 25

Já existem (não repita):
- Assinale a frase em que o termo destacado está corretamente grafado.
- Na frase "As dificuldades por que passamos foram enormes", a expressão "por que" está corr
- Complete corretamente: "Ele não disse ___, mas saiu irritado. Ninguém sabe o ___ de tanta 
- Em "Você chegou atrasado porque o trânsito estava ruim?", a grafia "porque" está correta, 
- Em "Torço por que tudo termine bem", a grafia separada se justifica porque:
- A palavra "porquê", quando substantivada, admite plural, como em "Ele quis saber os porquê
- Assinale a alternativa em que há ERRO no emprego dos porquês.
- No título "Por que investir em educação", a grafia separada é adequada, pois há ideia de i
- Em "Leve o casaco, porque a noite vai esfriar", o termo destacado tem valor:
- A frase "Não sei porquê ela desistiu" está correta, pois o termo destacado é substantivo.
- Assinale a alternativa que preenche corretamente as lacunas: "Ele saiu da empresa ___ dez 
- A expressão "há cinco anos atrás" é considerada redundante pela norma culta, pois "há" já 
- Assinale a frase correta quanto ao emprego de "acerca de", "há cerca de" e "a cerca de".
- Assinale a frase em que o emprego de "mal" ou "mau" está correto.
- Em "Mal terminou a palestra, o público começou a sair", a palavra "mal" funciona como conj
- Assinale a frase correta quanto ao uso de "onde" e "aonde".
- Pela norma culta, a frase "Vivemos uma época onde tudo é instantâneo" é adequada, pois "on
- Assinale a frase em que "senão" (junto) está corretamente empregado.
- Em "O projeto é excelente; o único senão é o custo", a palavra "senão" é substantivo, com 
- Em "Não agiu por maldade, senão por ingenuidade", o termo "senão" equivale a:
- A frase "Não restava outra saída se não aceitar o acordo" está correta, pois "se não" indi
- Assinale a frase correta.
- Em "Os demais candidatos aguardaram do lado de fora", "demais" é pronome, com sentido de "
- Assinale a frase em que o termo destacado está corretamente empregado.
- Em "Em vez de ir de carro, foi de bicicleta", a locução "em vez de" está adequada, pois in
- "A nova lei veio ___ reivindicações dos servidores, que a aprovaram com entusiasmo." Preen
- Na frase "O caminhão desgovernado foi ao encontro do poste", a locução foi empregada corre
- Assinale a frase em que o par de parônimos foi empregado corretamente.
- Em "O perigo de desabamento era iminente", o adjetivo significa "prestes a acontecer"; "em
- "A comissão decidiu ___ o edital, corrigindo o erro na data da prova, e o plenário ___ a d
- A frase "O juiz infringiu ao réu a pena prevista em lei" está correta, pois "infringir" si
- Assinale a frase em que a palavra destacada está corretamente empregada.
- Em "Por ora, o projeto está suspenso", a expressão significa "por enquanto" e está correta
- Assinale a frase correta quanto ao emprego de "a par" e "ao par".
- Na frase "Queria ir à festa, mais estava cansado", o emprego de "mais" está correto, pois 
- Assinale a alternativa em que todas as palavras ou expressões estão corretamente empregada
- Em "Descobriu-se a razão por que o sistema falhou", seria igualmente correto escrever "a r
- Assinale a frase em que "aonde" está corretamente empregado.
- Em "O servidor era eminente, e sua aposentadoria, iminente", os dois adjetivos foram empre
- "A ___ de uso do imóvel foi aprovada na ___ de ontem e encaminhada à ___ de protocolo." Pr
```

## Pedido 5: Sociologia do Direito: Ciência, Ideologia, Pluralismo, Eficácia e Função Simbólica (`direito.sociologia-do-direito`, 160 tópicos de edital)

```
Matéria: direito.sociologia-do-direito (Sociologia do Direito: Ciência, Ideologia, Pluralismo, Eficácia e Função Simbólica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Perspectivas sociológicas, ciência e ideologia": 20
- "2. Positivismo, marxismo e historicismo": 20
- "3. Fundamentos sociais, grupos e pluralismo jurídico": 20
- "4. Conflito, função simbólica e opinião pública": 20
- "5. Eficácia, efetividade e legitimidade": 20

Já existem (não repita):
- Em relação à dogmática jurídica, a sociologia do direito caracteriza-se por:
- A dogmática jurídica adota uma perspectiva interna, voltada ao dever-ser das normas, enqua
- Na tradição marxista, o direito é compreendido como:
- Ao apresentar como universais interesses de grupos dominantes, como na igualdade formal qu
- Para o positivismo normativista de Kelsen, a ciência do direito deve:
- Para Durkheim, nas sociedades de solidariedade orgânica, marcadas pela divisão do trabalho
- A Escola Histórica do Direito, cujo principal expoente é Savigny, sustenta que:
- O positivismo sociológico de Comte e Durkheim confunde-se com o positivismo jurídico de Ke
- Segundo Max Weber, a dominação baseada na crença na legalidade de normas estatuídas e no d
- Para Pachukanis, a forma jurídica, com sujeitos de direito livres e iguais, corresponde à 
- Para Weber, o direito moderno caracteriza-se sobretudo por ser:
- O que distingue o direito das demais formas de controle social, como a moral e os costumes
- A família e o grupo de amigos são exemplos de controle social informal e de grupos primári
- O pluralismo jurídico sustenta que:
- O conceito de direito vivo, entendido como o direito que efetivamente rege a vida social n
- No estudo sobre o direito de Pasárgada, Boaventura de Sousa Santos analisou a ordem jurídi
- O monismo jurídico admite, como direito, normas produzidas por grupos sociais independente
- São exemplos frequentemente citados de ordens normativas extraestatais, no debate sobre pl
- A relação entre conflito social e conflito jurídico pode ser descrita assim:
- Para as teorias do conflito, como as de Marx e Dahrendorf, o conflito social é motor de mu
- A norma editada para dar à população a impressão de que o Estado responde a um problema, e
- O compromisso dilatório, como forma de legislação simbólica, adia a solução de conflitos s
- A expressão constitucionalização simbólica, associada a Marcelo Neves, designa:
- Normas de função predominantemente simbólica não produzem efeito algum na sociedade.
- Em Habermas, a esfera pública é concebida como:
- A pressão da opinião pública após crimes de grande repercussão pode levar à edição de leis
- Segundo Cappelletti e Garth, as ondas renovatórias do acesso à justiça incluem, em sequênc
- A eficácia social, também chamada de efetividade, refere-se:
- Uma norma pode ser juridicamente válida e, ao mesmo tempo, socialmente ineficaz, como no c
- São fatores que influenciam a eficácia social das normas jurídicas, EXCETO:
- A tese de que a aceitação das decisões decorre da participação em procedimentos institucio
- Legalidade e legitimidade são conceitos equivalentes: todo ato conforme à lei é necessaria
- Para Habermas, as normas jurídicas são legítimas quando:
- A socialização, entendida como interiorização de normas e valores, ajuda a explicar por qu
- Para Althusser, o direito funciona simultaneamente como aparelho ideológico e repressivo d
- A pergunta 'por que determinada norma não é cumprida pela população?' é típica da:
- Georges Gurvitch é associado à ideia de direito social, produzido pelos próprios grupos so
- O autor que concebeu os fatos sociais como exteriores, gerais e coercitivos, a serem trata
- Na sociologia jurídica, o controle exercido por polícia, tribunais e normas estatais é cla
- A tipologia da legislação simbólica que compreende confirmação de valores sociais, legisla
```

## Pedido 6: Leis básicas em eletricidade (`ciencias.leis-basicas-eletricidade`, 159 tópicos de edital)

```
Matéria: ciencias.leis-basicas-eletricidade (Leis básicas em eletricidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Grandezas elétricas, materiais, Lei de Ohm e efeito Joule": 30
- "2. Circuitos resistivos CC, geradores, Kirchhoff e instrumentos": 23
- "3. Análise linear, equivalentes, capacitores e indutores": 15
- "4. Corrente alternada, fasores, impedância e potência": 14
- "5. Redes trifásicas, energia elétrica e segurança": 18

Já existem (não repita):
- Uma seção de um condutor é atravessada por 24 coulombs de carga em 8 segundos. Qual é a in
- Um resistor ôhmico ideal de 4 ohms é conectado a uma diferença de potencial de 20 V. Qual 
- Em circuito de bancada ideal, resistores de 3 ohms e 7 ohms estão em série, sem ramificaçõ
- Um resistor de 3 ohms conduz corrente contínua de 2 A. Qual potência térmica ele dissipa s
- Um equipamento de potência média constante igual a 500 W opera durante quatro horas. Qual 
- Num nó de circuito CC entram 8 A por um condutor e saem 3 A por outro ramo. Qual corrente 
- Uma bateria é modelada por força eletromotriz de 12 V e resistência interna de 1 ohm. Forn
- Para medir a diferença de potencial nos terminais de um resistor em um circuito didático, 
- Em uma atividade conceitual de medição da resistência de um componente, qual condição é co
- Uma tensão alternada senoidal tem frequência de 50 Hz. Qual é a duração de um ciclo comple
- Uma rede linear, vista de dois terminais, tem equivalente de Thévenin de 12 V em série com
- Em um ramo de rede linear, uma fonte independente contribui com 3 A no sentido escolhido e
- Um capacitor ideal de 10 microfarads tem tensão constante de 20 V entre suas placas. Qual 
- Dois fios têm mesma resistividade, temperatura e área de seção transversal. O segundo tem 
- Uma carga monofásica senoidal recebe 200 V eficazes, conduz 5 A eficazes e apresenta fator
- Num sistema trifásico equilibrado em estrela, a tensão eficaz entre duas linhas mede 380 V
- Em uma carga trifásica equilibrada conectada em triângulo, a corrente eficaz de cada fase 
- Um circuito RC ideal em série possui resistência de 2 quilohms e capacitância de 100 micro
- Um circuito RLC série em regime senoidal apresenta resistência de 6 ohms e reatância líqui
- O equivalente de Thévenin de uma rede linear consiste em fonte de 18 V em série com 3 ohms
- Uma carga senoidal apresenta tensão eficaz de 200 V, corrente eficaz de 5 A e fator de pot
- Uma carga trifásica equilibrada consome corrente eficaz de linha de 10 A, sob 400 V eficaz
- Um gerador CC com força eletromotriz de 12 V e resistência interna de 1 ohm alimenta resis
- Em um circuito RC ideal, um capacitor inicialmente descarregado começa a carregar-se rumo 
- Uma carga trifásica desequilibrada tem três impedâncias de fase distintas, com valores efi
- Quando uma carga de 20 coulombs atravessa a seção de um fio em quatro segundos, a corrente
- Em um circuito simples, a abertura de uma chave ideal e o curto-circuito direto dos termin
- Entre dois materiais homogêneos sob condições equivalentes, o que possui menor resistivida
- Uma lâmpada incandescente é necessariamente ôhmica durante todo aquecimento do filamento, 
- Dois resistores ideais de 6 ohms e 3 ohms ligados em paralelo têm resistência equivalente 
- A lei das tensões de Kirchhoff determina que, ao percorrer uma malha fechada, a soma dos m
- Se entram 7 A num nó de circuito estacionário e dele saem 2 A por um ramo, a lei das corre
- Um amperímetro ideal deve ser conectado em paralelo a uma fonte de tensão, pois sua resist
- No método de superposição para redes lineares, fontes independentes de tensão anuladas são
- Em rede resistiva linear com várias fontes, a potência total num resistor pode ser obtida 
- Num indutor ideal alimentado em corrente contínua após cessarem todos os transientes, a co
- Mantida a indutância, dobrar a frequência de uma fonte senoidal reduz à metade a reatância
- Num capacitor ideal, a tensão entre suas placas não pode sofrer salto instantâneo quando a
- Em RLC série ideal alimentado por senoide, quando reatância indutiva e capacitiva são igua
- Uma tensão senoidal pura com 100 V eficazes possui valor máximo de aproximadamente 141,4 V
- Uma carga monofásica senoidal com fator de potência 0,8 necessariamente converte 80% de to
- Em uma carga trifásica equilibrada ligada em estrela, a corrente eficaz de linha coincide 
- Em uma carga trifásica equilibrada ligada em triângulo, as correntes de linha e de fase po
- Para transportar uma mesma potência ativa com igual fator de potência e resistência de lin
- Como a nova redação da NR-10 foi publicada pela Portaria MTE nº 737/2026, suas disposições
```

## Pedido 7: Sistema Interamericano de Direitos Humanos: Comissão e Corte Interamericana (`direito.direitos-humanos-sistema-interamericano`, 158 tópicos de edital)

```
Matéria: direito.direitos-humanos-sistema-interamericano (Sistema Interamericano de Direitos Humanos: Comissão e Corte Interamericana)
Capítulos e quantas questões quero em cada um (total 100):
- "1. OEA, fontes, dois regimes de proteção e subsidiariedade": 29
- "2. Comissão: composição, petições, admissibilidade e medidas cautelares": 19
- "3. Corte Interamericana: jurisdição, composição e processo": 18
- "4. Sentenças, reparações, medidas provisórias e controle de convencionalidade": 16
- "5. Brasil no sistema: competência temporal, casos e execução": 18

Já existem (não repita):
- Um pesquisador compara a proteção regional antes e depois da Convenção Americana. Ele enco
- Uma questão compara a composição e os mandatos dos órgãos interamericanos. Qual afirmação 
- Uma organização não governamental reconhecida apresenta à Comissão petição denunciando vio
- Um requerente esgotou os recursos internos adequados, recebeu notificação da decisão defin
- Uma associação pretende impetrar diretamente na Corte um caso contencioso contra Estado qu
- Uma pessoa demonstra perigo imediato e grave de dano irreparável e pede proteção à CIDH an
- Após julgamento contencioso, um Estado alega que poderá interpor apelação ordinária contra
- Um estudo de caso pede associar a primeira sentença condenatória internacional contra o Br
- Um relatório apresenta a sede da Comissão como San José e a da Corte como Washington. Qual
- Após esgotar recursos internos, uma pessoa encaminha petição sete meses depois da notifica
- Dois Estados-partes desejam utilizar a comunicação interestatal perante a CIDH. Um deles a
- Após relatar violação grave à CIDH, Estado e representantes das vítimas procuram solução a
- Uma petição individual chega à fase contenciosa perante a Corte. O Estado requerido preten
- O caso foi submetido à Corte pela Comissão, e a pessoa lesada quer apresentar argumentos e
- Um Estado da OEA não aceitou a competência contenciosa da Corte, mas pede opinião sobre co
- A Corte reconhece violação e determina indenização, investigação independente e capacitaçã
- O Brasil foi demandado por fato instantâneo integralmente encerrado em 1996; não são apont
- Uma comunidade pede proteção urgente diante de ameaças extremamente graves e iminentes enq
- A Comissão adota relatório do art. 50, transmitido ao Estado. Passam-se três meses sem sol
- Uma sentença internacional fixa indenização, determina publicar o julgamento e promover in
- Um desaparecimento forçado teve início antes de 10/12/1998, mas a ausência de localização 
- Uma decisão doméstica desfavorável foi adotada após procedimento regular, sem que a petiçã
- Uma decisão contenciosa fixa indenização contra o Brasil. O Estado afirma que é impossível
- Ao preparar análise de jurisprudência brasileira, uma pessoa associa os casos Fazenda Bras
- Uma análise de controle de convencionalidade afirma que a Convenção Americana foi aprovada
- Uma pessoa pesquisa documentos do sistema e constata que a Declaração Americana de 1948 an
- Um Estado faz parte da OEA, mas não aderiu à Convenção Americana. Por sua participação na 
- A Comissão e a Corte possuem ambas sete integrantes, mas o mandato ordinário de seus membr
- Uma organização não governamental reconhecida em Estado-membro da OEA pode, nos termos do 
- Uma petição individual é apresentada após o esgotamento dos recursos internos efetivos, qu
- A Corte Interamericana pode receber diretamente de qualquer vítima uma demanda contenciosa
- Uma petição demonstra que não houve acesso efetivo a recurso interno apto a proteger o dir
- Ao receber denúncia individual, a Comissão poderá transformá-la em comunicação interestata
- Segundo o Regulamento da Corte interpretado após a OC-20/09, um Estado demandado em caso o
- Mesmo sem ser autora direta do caso perante a Corte, a vítima pode apresentar solicitações
- Uma pessoa pede à Comissão medidas cautelares diante de risco grave, urgente e irreparável
- A aceitação de competência contenciosa da Corte constitui pré-requisito necessário para qu
- Um relatório da Comissão elaborado pelo art. 50 é transmitido ao Estado interessado. Se a 
- Como o Brasil promulgou a Convenção Americana pelo Decreto nº 678/1992, sua declaração de 
- Em litígio abrangido pela competência da Corte, esta pode determinar medidas provisórias q
- Uma sentença da Corte foi notificada e a parte, após discordar do mérito, pretende recurso
- Uma condenação internacional determina indenização, publicação da decisão e investigação d
- O caso Gomes Lund trata exclusivamente de propriedade coletiva indígena e, por isso, não p
- Um Estado alega que os efeitos de uma sentença da Corte se resumem ao ressarcimento monetá
- No controle de convencionalidade, a Corte atua como quarta instância ordinária para substi
```

## Pedido 8: Desigualdades, mercado de trabalho e previdência no Brasil (`economia.desigualdade-trabalho-previdencia-brasil`, 157 tópicos de edital)

```
Matéria: economia.desigualdade-trabalho-previdencia-brasil (Desigualdades, mercado de trabalho e previdência no Brasil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Desigualdade de renda, riqueza e indicadores distributivos": 19
- "2. Desigualdades socioeconômicas e diferenças regionais": 19
- "3. Mercado de trabalho, emprego, renda e informalidade": 17
- "4. Mudança da estrutura produtiva e novas tecnologias no trabalho": 19
- "5. Previdência social, demografia e sustentabilidade": 26

Já existem (não repita):
- Em uma residência com três moradores, a soma dos rendimentos mensais considerados pela pes
- Uma pessoa recebe remuneração mensal de R$ 5 mil e possui imóvel de R$ 400 mil, além de dí
- Duas localidades utilizam o mesmo conceito de rendimento para calcular o índice de Gini. A
- Uma cidade industrial apresenta PIB per capita elevado, e uma análise afirma que todos os 
- Uma pesquisa regional deseja empregar o Índice de Vulnerabilidade Social. Quais são as trê
- Para comparar condições de desenvolvimento humano de municípios, um pesquisador consulta o
- Em uma amostra hipotética, há 600 pessoas ocupadas, 60 desocupadas e 340 fora da força de 
- Em população hipotética de mil pessoas em idade de trabalhar, há 660 na força de trabalho.
- Uma pessoa gostaria de trabalhar e está disponível, mas desistiu de procurar por acreditar
- Para examinar tanto ocupações formais quanto informais, desocupação e rendimentos de pesso
- Uma pesquisa registra 400 pessoas ocupadas, das quais 160 se enquadram na definição metodo
- Em determinado mês, registros formais indicam 1.200 admissões e 900 desligamentos, sem out
- Uma série apresenta desocupação de 6% e taxa composta de subutilização de 15% para o mesmo
- Um contrato oferece aumento nominal de 8% durante período com inflação acumulada de 10%. O
- Uma economia hipotética distribui igual rendimento entre todos os seus habitantes. Como se
- O valor adicionado industrial de uma economia cresceu em termos reais, mas sua participaçã
- Uma unidade produtiva fabrica bens no valor de R$ 1.000 e utiliza R$ 400 em insumos interm
- Uma empresa instala sistema que automatiza tarefas repetitivas de determinada ocupação, pr
- Uma pessoa de baixa renda necessita de prestação assistencial e outra contribui para cober
- Uma comparação dos regimes previdenciários afirma que qualquer pessoa empregada em ativida
- Duas regiões têm a mesma renda média por habitante. Em uma, todos recebem renda semelhante
- Uma unidade industrial produz 10% mais unidades com 5% menos trabalhadores e preços mantid
- Um plano previdenciário registra receitas superiores às despesas neste ano, mas projeções 
- Uma análise diz que todas as regras da EC nº 103/2019 sobre requisitos de aposentadoria do
- Após atualizar o estoque de referência com registros anuais, uma série do Novo Caged apres
- Uma pessoa recebe salário mensal de R$ 3.000, enquanto possui imóveis e aplicações finance
- O índice de Gini de 0,60 demonstra que exatamente 60% da renda do território pertencem aos
- A taxa de participação no mercado de trabalho usa a força de trabalho no numerador e a pop
- Na metodologia de mercado de trabalho, toda pessoa sem emprego é automaticamente desocupad
- Segundo o art. 194 da Constituição, a seguridade social compreende um conjunto integrado d
- Duas regiões têm PIB per capita idêntico; portanto, necessariamente apresentam a mesma des
- No cálculo do saldo mensal de movimentações de emprego formal do Novo Caged, 500 admissões
- Pessoas desalentadas fazem parte, por definição, da população desocupada na taxa convencio
- Uma atividade industrial pode elevar sua produção em termos reais e, simultaneamente, redu
- Pelo art. 40 da Constituição, o RPPS de um ente que o mantenha inclui automaticamente qual
- Uma série histórica apresenta menor índice de Gini depois de ganhos proporcionais maiores 
- Ao se calcular renda domiciliar per capita, deve-se dividir o rendimento considerado exclu
- A dimensão infraestrutura urbana integra o IVS, enquanto o IDHM é construído a partir de l
- O aumento de salários nominais de 5% durante inflação de 8% assegura ganho de poder de com
- A revisão técnica do estoque de vínculos do Novo Caged pode alterar o nível da série histó
- Se a automatização eleva a produtividade aparente do trabalho, conclui-se necessariamente 
- No regime previdenciário, um resultado financeiro anual positivo pode coexistir com desequ
- A EC nº 103/2019 definiu aplicação imediatamente idêntica de todos os seus dispositivos ao
- A razão de dependência demográfica e a relação efetiva entre beneficiários da previdência 
- A taxa de desocupação de uma pesquisa e o saldo de emprego formal apurado administrativame
```

## Pedido 9: Ciência, tecnologia e inovação e propriedade intelectual (`legislacao.ciencia-tecnologia-inovacao-propriedade-intelectual`, 157 tópicos de edital)

```
Matéria: legislacao.ciencia-tecnologia-inovacao-propriedade-intelectual (Ciência, tecnologia e inovação e propriedade intelectual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constituição, conceitos e instrumentos do Marco Legal de CT&I": 18
- "2. Política Nacional de Inovação, ENCTI e financiamento pelo FNDCT": 22
- "3. ICTs, NITs, cooperação, licenciamento e participação do criador": 20
- "4. Direitos autorais, software, patentes, marcas e desenho industrial": 23
- "5. Prospecção tecnológica, inteligência competitiva e transferência de conhecimento": 17

Já existem (não repita):
- Uma entidade pública firma cooperação com empresa para compartilhar infraestrutura científ
- Uma associação privada sem fins lucrativos, constituída no Brasil, tem em seu estatuto pes
- Uma equipe aperfeiçoa processo industrial existente, incorpora funcionalidade e demonstra 
- Uma instituição deseja incentivar parques tecnológicos e incubadoras para aproximar pesqui
- Um projeto cooperativo de pesquisa será firmado por ICT e empresa. Segundo o art. 9º, § 2º
- Uma equipe consulta a norma que instituiu a Política Nacional de Inovação federal e trata 
- Uma publicação de 2025 descreveu como proposta a ENCTI 2024–2034. Para avaliar seu status 
- Uma instituição busca identificar quem exerce a Secretaria-Executiva do FNDCT, distinta de
- Ao atualizar sua governança, uma ICT pública precisa escolher a estrutura exigida pelo art
- Uma ICT licenciou criação protegida e obteve ganhos econômicos. Que intervalo de participa
- Uma ICT pública deseja licença exclusiva de invenção para empresa que não participou do de
- Uma empresa participou do desenvolvimento conjunto de tecnologia com ICT pública e solicit
- Uma ICT pública decide permitir a várias empresas explorar a mesma criação protegida. Qual
- Um núcleo reúne registros de patentes, identifica concorrentes, acompanha tendências e pro
- Uma empresa pretende registrar sinal distintivo de seus produtos e pergunta a duração do t
- Um pesquisador depositou pedido de patente de invenção em 2026. Qual prazo geral de vigênc
- Um designer cria forma ornamental nova de embalagem industrial e busca registro apropriado
- Uma pessoa publica programa de computador de sua autoria em 2026, sem registrá-lo. Qual di
- Uma instituição pretende explorar aperfeiçoamento próprio, mas há patente anterior de terc
- Um relatório contabiliza cinco depósitos em países diferentes de mesma invenção reivindica
- Uma ICT licenciou tecnologia com exclusividade a empresa, mas esta deixou de comercializar
- Uma ICT avalia ceder ao criador os direitos sobre desenvolvimento próprio, em vez de licen
- Após uma chamada de projetos, alguém propõe bloquear integralmente recursos vinculados ao 
- Um NIT prepara relatório sobre novo produto: há pedido de patente publicado, mas sem conce
- Uma ICT deseja adotar invenção oferecida por inventor independente. Qual prazo para o NIT 
- Uma ICT de direito privado sem fins lucrativos, que desenvolve pesquisa tecnológica em sua
- Uma instituição aperfeiçoa processo produtivo e melhora efetivamente seu desempenho. A ino
- O Fundo Nacional de Desenvolvimento Científico e Tecnológico é fundo especial de natureza 
- Um programa setorial adotou o Decreto 10.534/2020 como fundamento. O decreto aprovou espec
- Uma ICT pública pode manter NIT em associação com outras ICTs, pois o art. 16 da Lei 10.97
- Uma ICT pública deseja licenciar tecnologia sem exclusividade para três empresas diferente
- Uma parceria entre ICT e empresa pretende distribuir receitas futuras e definir uso dos di
- Uma marca é concedida em 2026, após depósito anterior. Pela Lei 9.279/1996, seu registro d
- Uma obra literária original, ainda sem registro facultativo, pode estar protegida por dire
- Uma equipe avalia se tecnologia patenteável pode ser comercializada sem atingir reivindica
- A Portaria MCTI nº 10.226/2026 aprovou a ENCTI 2024–2034 como referencial estratégico e pr
- Uma empresa codesenvolveu invenção com ICT pública e negocia licença exclusiva. Nessa hipó
- Em parceria tecnológica, as partes podem pactuar titularidade e compensação não financeira
- Uma ICT pública celebrou contrato de licenciamento de criação protegida e recebeu ganhos e
- A Lei 11.540/2007, art. 11, § 2º, permite limitação da execução da programação financeira 
- Após a ADI 5529 e a Lei 14.195/2021, uma patente de invenção ainda possui garantia geral d
- O registro de desenho industrial pode alcançar, nos termos do art. 108 da Lei 9.279/1996, 
- Um programa de computador original, jamais publicado, somente recebe proteção pela Lei 9.6
- Mesmo que uma ICT identifique reivindicações patenteáveis inéditas, é possível que a comer
- Na prospecção tecnológica, cinco pedidos da mesma família de patentes em cinco países repr
```

## Pedido 10: Periodontia: diagnóstico, classificação e terapia (`odontologia.periodontia`, 156 tópicos de edital)

```
Matéria: odontologia.periodontia (Periodontia: diagnóstico, classificação e terapia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Anatomia, histologia e fisiologia dos tecidos periodontais": 13
- "2. Biofilme, patogênese, histopatologia e epidemiologia": 23
- "3. Diagnóstico clínico, radiográfico e classificação periodontal": 20
- "4. Terapia periodontal básica, instrumentação e medicamentos": 23
- "5. Terapia cirúrgica, oclusão, prognóstico e manutenção": 21

Já existem (não repita):
- Ao revisar um esquema anatômico, uma estudante identifica o tecido responsável pela união 
- Em um exame clínico, a gengiva mostra edema e sangramento, mas não há perda de inserção at
- Em um dente com recessão gengival de 2 mm em relação à junção cemento-esmalte, a profundid
- No prontuário, foram registrados 18 sítios com sangramento entre 120 sítios sondados, sem 
- Durante a avaliação de molares, a sonda de Nabers entra parcialmente na região de furca, m
- Uma pessoa apresenta cálculo supra e subgengival e inflamação persistente. Qual intervençã
- Após controle do biofilme e instrumentação subgengival, o profissional agenda exame para v
- Ao planejar higiene de uma pessoa com gengivite por biofilme, considera-se o uso de clorex
- Depois de estabilizada a periodontite, uma pessoa solicita encerramento definitivo do acom
- Na documentação periodontal, registrou-se sondagem de 5 mm em sítio cuja margem gengival e
- Após tratamento, um paciente apresenta pouco sangramento, inserção previamente perdida e p
- Em um estudo clínico, três pacientes apresentam a mesma perda de inserção, mas diferentes 
- Em um molar com perda de suporte, a sonda atravessa completamente a furca de vestibular pa
- Um paciente fumante possui várias áreas com CAL aumentado, mas apresenta sangramento discr
- Ao discutir periodontite avançada, uma pessoa atribui a destruição óssea exclusivamente à 
- Ao comparar instrumentos, uma profissional pretende usar uma cureta Gracey em uma superfíc
- Durante uma avaliação, há desgaste excessivo e mobilidade em um dente com perda importante
- Um indivíduo tem perda interdental de inserção em dois dentes não adjacentes, mas uma das 
- Duas pessoas têm perda de inserção interdental máxima de 5 mm. Uma mantém função e outra a
- Após terapia mecânica, permanece uma bolsa de 7 mm com sangramento em dente com defeito in
- Um dente apresenta PS de 6 mm e gengiva aumentada 3 mm coronal à JCE, sem outro sítio acom
- Uma pessoa perdeu suporte periodontal no passado, não apresenta sítios sangrantes com PS d
- O exame de uma pessoa com diabetes revela periodontite e controle glicêmico inadequado. Na
- Na análise de um dente com furca classe III, uma radiografia não evidencia nitidamente a p
- Um tratamento regenerativo foi realizado em defeito intraósseo e, meses depois, a profundi
- Durante orientação de higiene, explica-se que o ligamento periodontal participa da proprio
- Em exame de gengiva inflamada, considera-se que o epitélio juncional é a camada que revest
- Em um paciente com sangramento gengival, a presença de cálculo pode dificultar a higieniza
- Em uma avaliação periodontal, a profundidade de sondagem mede a distância entre a junção c
- Ao realizar periodontograma, registrar sangramento e profundidade de sondagem em seis síti
- Durante a terapia de gengivite, prescrever um enxaguatório antisséptico elimina a necessid
- Em um paciente fumante com periodontite, pouco sangramento à sondagem não exclui destruiçã
- Ao encontrar 30 sítios com sangramento entre 100 examinados, um examinador classifica a ex
- No planejamento de periodontite, o estágio descreve sobretudo gravidade acumulada e comple
- Ao examinar um molar, verifica-se passagem completa da sonda pela furca recoberta por geng
- Após instrumentação subgengival, um caso mantém bolsa profunda com sangramento, apesar de 
- Em um paciente com periodontite, a presença de um único sítio com profundidade de sondagem
- Após estabilização da periodontite, a frequência das consultas de manutenção deve consider
- Um paciente apresenta mobilidade e perda de inserção em região posterior. Sem avaliar biof
- Na análise de imagens periódicas, a ausência de perda óssea adicional entre duas radiograf
- Em defeito intraósseo submetido a cirurgia regenerativa, a redução da profundidade de sond
- Uma lesão marginal com perda de inserção localizada junto a fratura radicular vertical dev
- Em acompanhamento de uma pessoa cuja periodontite foi estabilizada, a ausência atual de sa
- Em paciente com gengiva edemaciada, a margem localizada coronalmente à JCE pode aumentar a
- No uso clínico da classificação de 2018, determinar um grau C pela presença de necessidade
```

## Pedido 11: Justiça do Trabalho: organização e competência (`processo-trabalho.organizacao-competencia`, 156 tópicos de edital)

```
Matéria: processo-trabalho.organizacao-competencia (Justiça do Trabalho: organização e competência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura constitucional: Varas, TRTs e Tribunal Superior do Trabalho": 24
- "2. Competência material: art. 114 da Constituição e limites jurisprudenciais": 18
- "3. Competência territorial, funcional e conflitos de competência": 33
- "4. Juiz do Trabalho: investidura, direção processual e garantias de imparcialidade": 13
- "5. Responsabilidade por dano processual e integração das competências": 12

Já existem (não repita):
- Uma pessoa revisa a estrutura constitucional e afirma que os órgãos da Justiça do Trabalho
- Foi apresentado um projeto de nomeação de ministros do TST com composição inferior à exigi
- Uma Vara do Trabalho recebe reclamação individual e surge dúvida se o julgamento exige col
- Um empregado pede indenização por dano moral diretamente decorrente da relação de trabalho
- Uma pessoa pretende ingressar na carreira da magistratura do trabalho como primeiro cargo.
- Em audiência trabalhista, o juiz tenta a conciliação antes de prosseguir na solução do dis
- Um trabalhador contratado na cidade A prestou todo o serviço na cidade B e se mudou depois
- Um trabalhador relata ofensa sofrida em audiência, e o juiz precisa restaurar a ordem sem 
- Uma atualização de apostila afirma que para nomeação ao TST basta ter 34 anos e idade infe
- Uma ação individual foi apresentada no local de residência atual de agente comercial viaja
- O regimento organiza o TST em Tribunal Pleno, Órgão Especial, seções especializadas e Turm
- Uma pessoa pede liminar urgente no curso da ação trabalhista e demonstra plausibilidade do
- Dois juízes do trabalho pertencentes ao mesmo TRT declaram-se incompetentes para ação e re
- Um reclamado altera conscientemente a verdade de documento essencial para induzir o juízo 
- Uma causa trabalhista tem valor corrigido de R$ 50 mil e o juízo reconhece má-fé do litiga
- Um empregador realiza atividades em várias localidades fora do lugar da celebração contrat
- Um candidato à composição de TRT tem 32 anos e reúne os demais requisitos aplicáveis. Outr
- Um brasileiro contratado para trabalhar em filial no exterior ajuíza reclamação perante a 
- Um servidor celetista do Poder Público pede benefício previsto em norma administrativa, e 
- Durante processo, o juiz reconhece que é amigo íntimo de um dos litigantes e considera afa
- Uma testemunha omite intencionalmente fato essencial do julgamento, e o valor da causa é i
- Um juiz do trabalho de região A e outro de região B declaram-se ambos competentes pelo mes
- Na fase de organização da instrução, a Vara decide indeferir prova testemunhal redundante,
- O TST e um Tribunal de Justiça travam conflito de competência sobre a causa. Qual disposit
- Um empregado requer reparação diretamente ao empregador por acidente de trabalho. O reclam
- O Tribunal Superior do Trabalho é composto por vinte e sete ministros e dispõe de órgãos i
- A EC 122/2022 manteve a idade máxima de ingresso no TST e nos TRTs inferior a sessenta e c
- Segundo o art. 116 da Constituição, a jurisdição nas Varas do Trabalho é exercida por um j
- Quando a reclamação decorre da relação de trabalho, a Justiça do Trabalho também julga tod
- O art. 651, caput, da CLT estabelece como regra o foro do local de prestação dos serviços,
- Uma Vara do Trabalho pode conceder tutela de urgência com base na mera vontade da parte au
- O art. 764 da CLT orienta a conciliação em dissídios individuais e coletivos, sem transfor
- Na exceção de incompetência territorial, o art. 800 da CLT determina que a peça seja apres
- No caso de agente comercial viajante com subordinação definida a uma filial, o art. 651, §
- Dois juízos trabalhistas sujeitos a regiões distintas do sistema se consideram incompetent
- A CLT, art. 652, inclui entre as atribuições das Varas do Trabalho decidir a homologação d
- O juízo pode aplicar multa por litigância de má-fé somente contra a parte reclamante; o re
- Uma ação que impugna penalidade administrativa aplicada ao empregador por autoridade fisca
- O art. 114, IV, atribui à Justiça do Trabalho todo mandado de segurança impetrado contra a
- Pelo Tema 1.143, demanda de servidor celetista contra o Poder Público destinada a obter pa
- Uma ação indenizatória proposta por empregado diretamente contra empregador em razão de ac
- Conflito de competência entre o TST e um Tribunal de Justiça enquadra-se na competência or
- Se dois litigantes de má-fé participarem do processo, a CLT, art. 793-C, § 1º, determina r
- A CLT, art. 801, inclui inimizade pessoal e interesse particular na causa entre fundamento
- Na estrutura regimental do TST, a Seção de Dissídios Coletivos e a de Dissídios Individuai
```

## Pedido 12: Relatório de auditoria, opinião e eventos subsequentes (`auditoria.relatorio-opiniao`, 156 tópicos de edital)

```
Matéria: auditoria.relatorio-opiniao (Relatório de auditoria, opinião e eventos subsequentes)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Finalidade, evidência e estrutura do relatório do auditor independente": 19
- "2. Opinião com ressalva, adversa e abstenção: matriz de decisões": 22
- "3. Ênfase, outros assuntos, principais assuntos e outras informações": 17
- "4. Eventos subsequentes e continuidade operacional": 24
- "5. Revisões, asseguração, comunicação e julgamento integrado": 18

Já existem (não repita):
- A administração entregou demonstrações contábeis para auditoria independente. O diretor ac
- Uma equipe planeja emitir opinião sobre demonstrações de uma empresa e promete ao investid
- Em uma auditoria, uma distorção pequena em reais corresponde à omissão de restrição contra
- Durante a avaliação final, o auditor obteve evidências suficientes e apropriadas e conclui
- Uma empresa não corrigiu um erro material em estoques. Os efeitos desse erro são circunscr
- O auditor comprovou distorções relevantes que permeiam o balanço, o resultado e as notas, 
- Documentos essenciais de várias operações foram destruídos e não há procedimentos alternat
- Um litígio excepcional foi adequadamente contabilizado e descrito em nota; ele é fundament
- O relatório precisa explicar uma circunstância da auditoria das informações comparativas r
- Na auditoria de entidade listada, foram identificadas estimativas muito complexas que exig
- As demonstrações encerraram-se em 31/12; o relatório foi datado de 20/03. Um fato relevant
- Em abril, após datar relatório em março, mas antes de divulgar as demonstrações, o auditor
- Uma entidade apresenta fluxo de caixa incerto e dificuldades de refinanciamento, mas a bas
- Uma empresa encerrou definitivamente as atividades, porém preparou suas demonstrações como
- Uma companhia contrata revisão de suas informações intermediárias pelo próprio auditor das
- A administração omite passivo relevante e generalizado. O auditor conhece o valor devido, 
- O auditor não conseguiu assistir ao inventário da filial, mas aplicou testes alternativos 
- A equipe identificou estimativa com alto risco, comunicou-a à governança e concluiu que fo
- Um relatório anual descreve aumento expressivo das vendas, mas as demonstrações auditadas 
- Um relatório datado de março foi divulgado em abril. Em maio, o auditor descobre prova rel
- No encerramento, uma entidade possui risco material de continuidade, mas omite explicação 
- Em auditoria de grupo, um erro material foi corrigido integralmente antes da aprovação fin
- Há deficiências significativas nos controles, mas testes substantivos adequados demonstram
- Um profissional afirma que, como a revisão intermediária emprega indagações e análise, seu
- Um auditor examina demonstrações relativas a período iniciado em 01/01/2026 e estuda as no
- Ao emitir relatório sobre demonstrações de dezembro, o auditor concluiu que a administraçã
- Em trabalho com amostragem estatística, a segurança razoável prevista pela NBC TA 200 equi
- Uma empresa corrigiu integralmente erro relevante antes da emissão do relatório; após novo
- A equipe comprovou superavaliação relevante e generalizada de ativos e, por isso, deve abs
- Uma informação extraordinária está adequadamente apresentada em nota e é fundamental para 
- Um processo judicial material não foi registrado nem divulgado adequadamente. O auditor po
- Uma limitação na verificação de saldo material pôde ser superada por procedimentos alterna
- Os principais assuntos de auditoria destacados segundo a NBC TA 701 equivalem a opiniões a
- Uma entidade listada teve questões complexas comunicadas aos responsáveis pela governança.
- Entre 31/12 e a data do relatório, o auditor deve tratar todos os acontecimentos relevante
- O relatório foi datado e, antes de as demonstrações serem divulgadas, o auditor descobriu 
- Em entidade com incerteza relevante sobre continuidade, o uso da base é adequado e a divul
- A administração adotou base de continuidade apesar de evidências conclusivas de que a enti
- Em revisão das informações trimestrais pelo auditor da entidade, a conclusão de segurança 
- Pela NBC TA 720, qualquer narrativa financeira publicada no relatório anual passa automati
- Uma distorção material não generalizada foi comprovada, mas o auditor declara abstenção po
- Deficiências significativas de controles internos podem exigir comunicação por escrito à g
- Uma auditoria de período iniciado em 01/01/2026 deve adotar retroativamente todas as alter
- No relatório do auditor independente, a seção de outros assuntos tem a mesma finalidade da
- Mesmo quando uma nota adequadamente divulga matéria fundamental, o auditor deve distinguir
```

## Pedido 13: Biomas e gestão ambiental no Brasil (`geografia.biomas-gestao-ambiental-brasil`, 155 tópicos de edital)

```
Matéria: geografia.biomas-gestao-ambiental-brasil (Biomas e gestão ambiental no Brasil)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Biomas, domínios morfoclimáticos e fitofisionomias": 29
- "2. Ecossistemas aquáticos, zona costeira e impactos ambientais": 22
- "3. Manejo florestal, inventário, viveiros e restauração": 17
- "4. Unidades de conservação e instrumentos de gestão ambiental": 17
- "5. Mudanças climáticas, cooperação internacional e síntese aplicada": 15

Já existem (não repita):
- Em material didático, um mapa identifica Amazônia, Cerrado, Caatinga, Mata Atlântica, Pamp
- Em uma área do Cerrado, predomina vegetação herbácea, com poucas árvores; uma área próxima
- Depois de longo período seco, plantas de uma área da Caatinga perdem folhas, mas muitas re
- Em uma planície, a reprodução de peixes acompanha o avanço e o recuo das águas em diferent
- Um reservatório recebe esgoto e fertilizantes; há florescimento intenso de algas seguido d
- Ao planejar obras costeiras, uma equipe encontra vegetação sobre sedimentos lodosos com in
- Uma empresa pretende explorar madeira nativa seletivamente e apresenta documento com diret
- Uma área protegida permite organizar ocupação humana e uso de recursos de maneira compatív
- Uma política protege encostas contra secas extremas e constrói sistemas de armazenamento d
- Pesquisadores descrevem um trecho em que características de vegetação de Caatinga e Cerrad
- Um relatório chama o domínio das araucárias de sétimo bioma oficial brasileiro e considera
- Uma encosta desmatada passa a liberar sedimentos que se depositam no leito do rio, reduzin
- Um inventário mede parcelas permanentes repetidamente para calcular crescimento, mortalida
- Em projeto de recuperação, um viveiro pretende plantar apenas espécie exótica de crescimen
- Uma propriedade rural mantém área para proteção ecológica de nascentes e outra para conser
- Um estudo propõe ligar fragmentos florestais entre duas unidades de conservação para permi
- Para classificar contaminantes em rio, uma análise constata que determinada substância aum
- Uma estratégia ambiental pretende diminuir gases de efeito estufa associados à derrubada d
- Em uma bacia, o represamento altera a migração de peixes e a planície recebe cheias mais c
- Uma concessão de manejo florestal possui PMFS e subdivide área produtiva em UPA. Para o pe
- Após erosão, um projeto estabiliza o terreno com gramado exótico, embora antes houvesse ma
- Uma proposta classifica Reserva Particular do Patrimônio Natural como Uso Sustentável e pr
- Uma área de vegetação nativa foi isolada por rodovias e monoculturas, e as populações anim
- Um país se compromete com o Marco Global de Biodiversidade de Kunming-Montreal e alguém co
- Em monitoramento de rios em 2026, um parecer diz que apenas a listagem de peixes e inverte
- Uma paisagem com floresta de terra firme, áreas de várzea e igapós pode integrar o mesmo b
- Uma proposta de manejo afirma que campo limpo e cerradão são nomes equivalentes de uma mes
- O lançamento de nutrientes por esgoto e fertilizantes pode provocar eutrofização em lago, 
- Uma equipe ambiental descreve manguezais como florestas continentais de altitude que se fo
- Uma unidade classificada como Parque Nacional pertence, conforme art. 8º da Lei nº 9.985/2
- Ao avaliar mata ciliar de uma bacia, um estudo caracteriza a deposição de sedimentos em le
- Em uma área de transição entre Cerrado e Caatinga, a vegetação combina espécies e estrutur
- Um inventário florestal amostral, por selecionar parcelas representativas, dispensa consid
- O Inventário Florestal Nacional previsto no art. 71 da Lei nº 12.651/2012 produz informaçõ
- Pela Lei nº 9.985/2000, todas as unidades de conservação de Uso Sustentável permitem extra
- Uma política de restauração de áreas degradadas pode combinar viveiros de espécies nativas
- Como a Constituição define a Zona Costeira como patrimônio nacional, todas as restingas e 
- O plano ambiental de uma bacia propõe medir somente a turbidez da água e, se houver reduçã
- Na análise da perda de biodiversidade pantaneira, alterações do pulso de inundação provoca
- Em um plano de exploração florestal, o inventário a 100% de árvores comerciais de determin
- Uma propriedade rural com APP protetora de nascente pode manter também Reserva Legal, porq
- A meta internacional de conservação de 30% até 2030 do Marco Kunming-Montreal impõe, indep
- Ao definir resposta à mudança do clima, reduzir emissão de gases por desmatamento evitado 
- A avaliação de espécies ameaçadas de peixes e invertebrados aquáticos pode apoiar-se exclu
- Uma intervenção que aumenta o número de mudas plantadas, mas não acompanha sobrevivência, 
```

## Pedido 14: Medicina de família e comunidade (`medicina.medicina-familia-comunidade`, 155 tópicos de edital)

```
Matéria: medicina.medicina-familia-comunidade (Medicina de família e comunidade)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da Medicina de Família e Comunidade e atributos da APS": 17
- "Método clínico centrado na pessoa, comunicação e decisão compartilhada": 17
- "Abordagem familiar, ferramentas relacionais e cuidado domiciliar": 17
- "Promoção, prevenção quaternária e rastreamento baseado em evidências": 17
- "Acolhimento de queixas comuns, incerteza e segurança clínica": 16
- "Cuidado longitudinal, saúde do homem e da mulher e integração do cuidado": 16

Já existem (não repita):
- Uma usuária frequenta a mesma equipe há cinco anos, inclusive quando não apresenta queixas
- Quando a APS reúne o laudo da consulta especializada, orienta a pessoa e atualiza seu plan
- Uma equipe deseja compreender laços de parentesco e doenças relatadas em três gerações. Qu
- Para visualizar o apoio que uma família recebe de escola, vizinhos e serviços, a ferrament
- Uma pessoa sem sintomas realiza exame periódico para detectar possível doença em fase pré-
- Qual alternativa descreve melhor a prevenção quaternária?
- No método clínico centrado na pessoa, a pergunta 'Como esse problema mudou sua rotina?' pr
- Ao não identificar sinais de alarme em uma queixa recente, o médico opta por reavaliar. Qu
- A longitudinalidade pode existir mesmo quando a pessoa não está doente, pois se refere a v
- Integralidade significa que todos os exames e procedimentos devem ser realizados dentro da
- O genograma e o ecomapa são instrumentos complementares: o primeiro enfatiza relações fami
- No MCCP, a competência clínica é dispensável porque a opinião da pessoa substitui a avalia
- A prevenção quaternária inclui revisar prescrições e evitar exames sem indicação, com aten
- Acolhimento na APS corresponde apenas a marcar consultas pela ordem de chegada, independen
- A atenção à saúde do homem na APS deve abordar aspectos mentais, sexuais, reprodutivos e p
- Uma UBS atende prontamente sintomas novos, porém os médicos não recebem relatórios após os
- Um usuário pede antibiótico para tosse recente, embora a avaliação sugira causa viral sem 
- Família relata conflitos após alta de pessoa dependente. Qual uso de ferramenta melhor ate
- Mulher assintomática de 61 anos, sem alto risco conhecido, pergunta sobre o rastreio mamog
- Pessoa de 32 anos com colo uterino e vida sexual iniciada pergunta sobre teste primário na
- Homem de 57 anos, sem sintomas urinários, solicita PSA por campanha. O que melhor correspo
- Uma mulher relata cansaço há duas semanas, com exame inicial sem sinais alarmantes e alta 
- Pessoa idosa usa vários medicamentos prescritos em diferentes serviços e relata tontura. Q
- O primeiro contato implica acesso para cada nova necessidade de saúde, mas não significa q
- A entrevista motivacional é sinônimo exato do MCCP e substitui todos os seus componentes e
- O APGAR familiar é uma ferramenta de percepção de funcionamento familiar; isoladamente, nã
- O rastreamento, ao aumentar o tempo entre diagnóstico e morte, comprova por si só redução 
- Na implantação do teste de DNA-HPV como método primário, serviços que ainda não o oferecem
- Uma hipótese clínica inicial com baixa probabilidade de gravidade pode ser acompanhada na 
- Na saúde da mulher, o planejamento reprodutivo centrado na pessoa admite imposição de cont
- Uma pessoa mantém relação de confiança com a UBS por anos, mas, após cirurgia, não recebe 
- Em consulta, uma pessoa com alto risco de piora insiste em manter conduta que aumenta o ri
- Em visita domiciliar, o genograma mostra dois filhos adultos, mas nenhum consegue ajudar a
- Em programa de rastreamento, aumenta a sobrevida contada desde o diagnóstico, mas a idade 
- Paciente de 43 anos relata nódulo mamário palpável novo e afirma que o rastreamento popula
- Homem com episódios recorrentes de dor inespecífica apresenta exames prévios normais; hoje
- Uma mulher de 67 anos com multimorbidade, fragilidade e dificuldade de deslocamento recebe
- A equipe identifica que uma prescrição indicada é inacessível devido a custos e que a pess
- Uma equipe pode apresentar longitudinalidade satisfatória e, ao mesmo tempo, baixa coorden
- Pelo fato de o genograma revelar padrão de doenças em diferentes gerações, ele estabelece 
- Um teste com elevada sensibilidade é suficiente para justificar rastreamento universal, me
- Nas recomendações nacionais atualizadas em 2025, o rastreio mamográfico prioritário é bien
- Nas diretrizes de 2025, após teste primário de DNA-HPV negativo em pessoa elegível, a reco
- Safety-netting inclui orientar sinais de deterioração clínica, prazo de reavaliação e via 
- Em abordagem integral, a necessidade de rastreamento do colo uterino pode depender da pres
```

## Pedido 15: Teoria geral do direito: conceito, fontes e normas jurídicas (`direito.teoria-geral-direito-fontes-normas`, 154 tópicos de edital)

```
Matéria: direito.teoria-geral-direito-fontes-normas (Teoria geral do direito: conceito, fontes e normas jurídicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direito, moral e funções do ordenamento jurídico": 15
- "2. Direito objetivo, subjetivo e posições jurídicas": 27
- "3. Fontes do direito: legislação, costumes, precedentes e doutrina": 20
- "4. Estrutura das normas, validade, vigência e eficácia": 19
- "5. Princípios gerais, interpretação, integração e solução de antinomias": 19

Já existem (não repita):
- Uma pessoa afirma que determinada conduta socialmente reprovada constitui necessariamente 
- Um estudante compara o conjunto de regras sobre reparação de danos com a posição da vítima
- A mudança de hábitos de consumo impulsionou a criação de norma sobre contratos digitais. A
- Uma lei nacional foi oficialmente publicada sem cláusula especial de vigência. Qual regra 
- A legislação não prevê expressamente solução para caso particular e o juiz precisa decidir
- Uma regra empresarial é descumprida amplamente, mas foi produzida regularmente, entrou em 
- Uma pessoa viola direito de outra e passa a ser juridicamente exigível determinada prestaç
- Em aula prática, um contrato é interpretado conforme boa-fé e usos do lugar da celebração.
- Um professor diferencia jurisprudência consolidada de decisão singular proferida uma única
- Uma lei B revogou expressamente a lei A. Posteriormente, a lei C revogou B, sem disposição
- Duas empresas realizam negócio e a parte credora escolhe exercer direito em desconformidad
- Um processo envolve omissão legal e a parte solicita decisão por equidade independentement
- Uma parte invoca decisão anterior e o julgador cita apenas a ementa, sem examinar razão de
- O legislador publicou norma geral nova que convive com disciplina especial anterior, sem i
- Um órgão invalida ato administrativo mencionando apenas 'interesse público', sem estudar a
- Em uma demanda civil, o interessado possui posição que lhe permite alterar vínculo jurídic
- Uma parte sustenta que o legislador poderia dispensar distinção entre lei complementar e o
- Uma lei foi publicada com vacância de 20 dias. O ato oficial sofre republicação para corri
- Um negócio jurídico consumou-se validamente sob a lei anterior. Lei nova entra em vigor e 
- Uma prática contratual reiterada no mercado contraria disposição legal cogente. Uma parte 
- Em litígio complexo, o intérprete percebe que o caso não tem disciplina direta, mas existe
- Uma nova lei ordinária autoriza conduta que a Constituição proíbe diretamente. Quem resolv
- O juiz deixa de aplicar precedente invocado por parte, afirmando apenas discordar da concl
- Uma autoridade invalida contrato administrativo mencionando 'moralidade' sem indicar reper
- Uma nova lei altera requisitos de uma prestação ainda não implementada. O interessado tinh
- A reprovação moral de um comportamento pode existir sem que tal conduta configure ilícito 
- O direito objetivo corresponde à faculdade individual de exigir uma prestação, enquanto o 
- Transformações econômicas e pressões sociais que influenciam a criação de lei são classifi
- A regra geral do art. 1º da LINDB determina que a lei publicada sem cláusula própria de vi
- O art. 4º da LINDB admite integrar omissões legais por analogia, costumes e princípios ger
- Um direito subjetivo é sempre idêntico à pretensão do art. 189 do Código Civil, pois a pre
- Uma prática reiterada de mercado que contraria norma legal cogente revoga tacitamente essa
- Uma lei pode ser válida e vigente, mas apresentar baixo grau de observância social, sem qu
- Uma lei posterior que estabelece norma geral ao lado de disciplina especial anterior, sem 
- O CPC, art. 140, impede o juiz de recusar julgamento pela existência de lacuna ou obscurid
- Os princípios da legalidade e da moralidade administrativa do art. 37, caput, da Constitui
- Na análise de precedentes, o CPC, art. 489, § 1º, exige mais do que copiar a ementa: é nec
- Segundo o Código Civil, art. 187, uma pessoa não comete ato ilícito ao exercer direito sub
- Como o art. 9º da LC nº 95/1998 exige cláusula expressa de revogação, o art. 2º, § 1º, da 
- Uma lei publicada com vacância foi republicada para corrigir seu texto antes da entrada em
- Uma lei revogadora foi revogada sem cláusula de restauração da lei originária. Nesse caso,
- A teoria da validade jurídica e a análise da efetividade social respondem a perguntas dife
- Ao invocar o princípio da dignidade humana para invalidar ato administrativo, o decisor es
- O art. 926 do CPC exige jurisprudência estável, íntegra e coerente, e seu art. 927 relacio
- A analogia prevista no art. 4º da LINDB pode integrar lacuna por semelhança, enquanto inte
```

## Pedido 16: Política Nacional de Educação Ambiental (Lei 9.795/1999) (`educacao.politica-nacional-educacao-ambiental`, 153 tópicos de edital)

```
Matéria: educacao.politica-nacional-educacao-ambiental (Política Nacional de Educação Ambiental (Lei 9.795/1999))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, direito de todos, princípios e objetivos da Educação Ambiental": 20
- "2. A Política Nacional de Educação Ambiental: esfera de ação, linhas de atuação e educação formal e não formal": 20
- "3. Execução da PNEA, Órgão Gestor e regulamentação pelo Decreto 4.281/2002": 20
- "4. Diretrizes Curriculares Nacionais (DCNEA) e abordagem interdisciplinar e transversal na escola": 20
- "5. Como o tema costuma ser cobrado": 20

Já existem (não repita):
- Nos termos do art. 1º da Lei nº 9.795/1999, os processos de construção de valores sociais,
- De acordo com o art. 4º da Lei nº 9.795/1999, constitui um princípio básico da educação am
- Conforme o art. 9º da Lei nº 9.795/1999, a educação ambiental na educação escolar é desenv
- Sobre a implantação de disciplina específica de Educação Ambiental no currículo, o art. 10
- Nos termos do art. 2º do Decreto nº 4.281/2002, que regulamenta a Lei nº 9.795/1999, o Órg
- O Decreto nº 4.281/2002 criou o Comitê Assessor para auxiliar o Órgão Gestor da PNEA. Acer
- Segundo o art. 16 da Resolução CNE/CP nº 2/2012 (Diretrizes Curriculares Nacionais para a 
- Em uma questão de prova que apresenta o caso de uma escola que resume suas atividades ambi
- A Lei nº 14.926/2024 atualizou o art. 5º da Lei nº 9.795/1999, acrescentando o inciso VIII
- Na repartição de responsabilidades prevista no art. 3º da Lei nº 9.795/1999, incumbe espec
- O art. 8º da Lei nº 9.795/1999 estabelece que as atividades vinculadas à PNEA devem ser de
- Acerca da formação de profissionais e educadores na educação formal, os arts. 10, § 3º e 1
- De acordo com o art. 15 da Lei nº 9.795/1999, inserem-se entre as atribuições legais do ór
- Para fins de alocação de recursos públicos vinculados à PNEA, o art. 17 da Lei nº 9.795/19
- Nos termos do art. 14, inciso V da Resolução CNE/CP nº 2/2012 (DCNEA), o estímulo à consti
- Ao disciplinar a organização curricular da Educação Ambiental nas instituições de ensino, 
- Em provas que cobram a interface entre a Lei nº 9.795/1999 e o Decreto nº 4.281/2002, uma 
- Analise os itens a seguir à luz dos arts. 4º e 5º da Lei nº 9.795/1999: I. Garantia de dem
- Uma instituição privada de educação superior solicita ao Ministério da Educação a renovaçã
- No âmbito da educação ambiental não-formal (art. 13 da Lei nº 9.795/1999) e do desdobramen
- De acordo com o art. 3º do Decreto nº 4.281/2002, compete ao Órgão Gestor da Política Naci
- Sobre a composição do Comitê Assessor prevista no art. 4º do Decreto nº 4.281/2002, assina
- O art. 16, parágrafo único da Resolução CNE/CP nº 2/2012 (DCNEA) traz uma regra específica
- Considere uma questão de prova que avalia a integração de programas de educação ambiental 
- Em uma questão de prova com estudo de caso complexo, uma rede de ensino propõe articular t
- A educação ambiental é um componente essencial e permanente da educação nacional, devendo 
- Nos termos da Lei nº 9.795/1999, o incentivo à participação individual e coletiva na prese
- A Política Nacional de Educação Ambiental aplica-se exclusivamente às instituições educaci
- Como regra geral, a educação ambiental deve ser implantada como uma disciplina específica 
- Os Estados, o Distrito Federal e os Municípios, na esfera de sua competência e nas áreas d
- Segundo as Diretrizes Curriculares Nacionais para a Educação Ambiental (Resolução CNE/CP n
- Em provas de Certo/Errado, quando o enunciado afirma que nos cursos de pós-graduação é obr
- A concepção do meio ambiente em sua totalidade, considerando a interdependência entre o me
- Aos meios de comunicação de massa incumbe apenas veicular campanhas governamentais pagas s
- Entendem-se por educação ambiental não-formal as ações e práticas educativas voltadas à se
- O suporte técnico e administrativo necessário ao desempenho das atribuições do Órgão Gesto
- Na inclusão da Educação Ambiental em todos os níveis e modalidades de ensino, o art. 5º do
- Para a Resolução CNE/CP nº 2/2012, caso uma escola opte por inserir a Educação Ambiental p
- A linha de atuação relativa ao desenvolvimento de estudos, pesquisas e experimentações (ar
- Segundo o art. 19 da Lei nº 9.795/1999, apenas os programas federais de assistência técnic
- No Comitê Assessor regulamentado pelo art. 4º do Decreto nº 4.281/2002, os representantes 
- O Órgão Gestor da PNEA está proibido de convidar representantes de outros segmentos da soc
- Entre as diretrizes pedagógicas do art. 14 da Resolução CNE/CP nº 2/2012, inclui-se o apro
- Quando uma prova apresenta um caso em que a alocação de recursos públicos federais da PNEA
- Nas provas atualizadas após a Lei nº 14.926/2024, é incorreto afirmar que a Política Nacio
```

## Pedido 17: Previdência Complementar (LC nº 108/2001 e LC nº 109/2001) (`direito-previdenciario.previdencia-complementar`, 153 tópicos de edital)

```
Matéria: direito-previdenciario.previdencia-complementar (Previdência Complementar (LC nº 108/2001 e LC nº 109/2001))
Capítulos e quantas questões quero em cada um (total 100):
- "1. O regime na Constituição e as entidades": 20
- "2. Planos de benefícios, institutos e custeio": 20
- "3. Governança, transparência e operações vedadas": 20
- "4. Fiscalização, intervenção, liquidação e regime disciplinar": 20
- "5. Patrocinador público (LC 108/2001) e servidores": 20

Já existem (não repita):
- Segundo o art. 202 da Constituição e a LC nº 109/2001, o regime de previdência privada é:
- As contribuições do empregador, os benefícios e as condições contratuais previstos nos pla
- Sobre as entidades de previdência complementar, assinale a alternativa correta.
- As entidades fechadas de previdência complementar constituídas por instituidores devem ter
- Uma entidade fechada que congrega mais de um patrocinador e administra planos para diverso
- Pela LC nº 109/2001, a aplicação dos recursos correspondentes às reservas técnicas, provis
- Para a LC nº 109/2001, assistido é:
- Os planos de benefícios das entidades fechadas devem ser obrigatoriamente oferecidos a tod
- Maria deixou a empresa patrocinadora antes de cumprir os requisitos do benefício pleno e o
- Nos planos de entidades fechadas, não será admitida a portabilidade na inexistência de ces
- João teve sua remuneração reduzida, mas quer manter o nível de benefício previsto. Para is
- Em relação ao custeio dos planos das entidades fechadas, as contribuições extraordinárias 
- O resultado superavitário dos planos das entidades fechadas, ao final do exercício, será d
- No equacionamento do déficit de plano de entidade fechada, admite-se a redução do valor do
- A portabilidade de recursos de plano de entidade fechada para entidade aberta somente será
- Nas entidades fechadas, o estatuto deverá prever a representação dos participantes e assis
- Os membros da diretoria-executiva das entidades fechadas devem ter formação de nível super
- É vedado às entidades de previdência complementar realizar operações comerciais e financei
- Pela LC nº 109/2001, a concessão de benefício pela previdência complementar depende sempre
- Segundo a LC nº 109/2001, quanto às prestações de benefício não pagas nem reclamadas na ép
- Sobre a portabilidade de recursos entre planos de benefícios titulados pelo mesmo particip
- No caso de entidade aberta em dificuldades, o órgão fiscalizador pode nomear, por prazo de
- A fiscalização exercida pelo Estado sobre as entidades fechadas exime os patrocinadores e 
- Constitui hipótese que autoriza a decretação de intervenção em entidade de previdência com
- As entidades fechadas de previdência complementar não podem solicitar concordata e não est
- Na liquidação extrajudicial de entidade de previdência complementar, os participantes e as
- A decretação de intervenção ou de liquidação extrajudicial torna indisponíveis os bens dos
- Entre as penalidades administrativas previstas na LC nº 109/2001 está:
- Das decisões do órgão fiscalizador que aplicarem penalidades com base na LC nº 109/2001 ca
- Além dos administradores e conselheiros das entidades, a LC nº 109/2001 prevê a responsabi
- Segundo a LC nº 108/2001, a contribuição normal do patrocinador público para plano de bene
- Nos planos das entidades fechadas patrocinadas pelo Poder Público, exige-se carência mínim
- Nas entidades fechadas regidas pela LC nº 108/2001, o conselho deliberativo:
- Pela LC nº 108/2001, o mandato dos membros do conselho fiscal é de quatro anos, vedada a r
- Nos doze meses seguintes ao término do cargo, o ex-diretor de entidade fechada patrocinada
- É vedado à União, aos Estados, ao Distrito Federal e aos Municípios, e às suas entidades, 
- Quanto ao regime de previdência complementar dos servidores titulares de cargo efetivo, ap
- Segundo a LC nº 108/2001, o membro do conselho deliberativo só perderá o mandato em virtud
- Ao conselho deliberativo das entidades fechadas regidas pela LC nº 108/2001 compete, entre
- Os planos de benefícios das entidades abertas podem ser individuais, quando acessíveis a q
```

## Pedido 18: Cirurgia cardiovascular e cardiopatias congênitas (`medicina.cirurgia-cardiovascular`, 153 tópicos de edital)

```
Matéria: medicina.cirurgia-cardiovascular (Cirurgia cardiovascular e cardiopatias congênitas)
Capítulos e quantas questões quero em cada um (total 100):
- "Anatomia cirúrgica, circulação extracorpórea e proteção miocárdica": 17
- "Revascularização miocárdica e complicações mecânicas do infarto": 17
- "Cirurgia valvar, próteses e endocardite": 17
- "Aorta torácica: aneurismas, dissecção e reparo aberto ou endovascular": 17
- "Cardiopatias congênitas acianóticas: shunts e coarctação": 16
- "Cardiopatias cianóticas, circulação univentricular e paliação": 16

Já existem (não repita):
- Durante a revisão da anatomia coronária antes de uma cirurgia, o cirurgião define dominânc
- Durante procedimento com clampeamento da aorta, o time necessita preservar o miocárdio. Qu
- Em substituição valvar aórtica com insuficiência aórtica importante, observa-se distensão 
- Qual estrutura é especialmente vulnerável na dissecção e sutura próximas ao septo membrano
- Na CRM, qual combinação de enxerto e artéria-alvo é tradicionalmente associada a excelente
- Paciente com infarto recente evolui com choque e insuficiência mitral aguda exuberante ao 
- Após infarto transmural, a imagem mostra cavidade extracardíaca comunicante com o ventrícu
- Na escolha entre CRM e intervenção percutânea para doença coronária complexa, qual afirmaç
- Qual terapia anticoagulante é fundamental para um paciente com prótese valvar mecânica?
- Para insuficiência mitral degenerativa com anatomia reparável e alta probabilidade de dura
- Qual conjunto de condições constitui núcleo das indicações cirúrgicas na endocardite infec
- Uma angiotomografia revela dissecção que envolve a aorta ascendente e se prolonga até a ao
- Paciente apresenta dissecção aguda Stanford B sem sinais de ruptura, malperfusão, expansão
- Em aneurisma de raiz aórtica associado a valva inadequada para preservação, a equipe opta 
- Qual defeito interatrial costuma associar-se a drenagem venosa pulmonar anômala parcial e 
- Recém-nascido com coarctação crítica piora rapidamente após fechamento fisiológico do cana
- Adulto com CIV de longa data apresenta hipertensão pulmonar importante e shunt invertido, 
- Qual quarteto define a tetralogia de Fallot?
- Na d-transposição neonatal com mistura interatrial insuficiente e hipoxemia grave, que int
- Na paliação de ventrículo único, qual descrição caracteriza a etapa de Fontan?
- Na CEC convencional, o oxigenador de membrana exerce a função principal de realizar trocas
- A anticoagulação sistêmica com heparina é desnecessária quando se utiliza oxigenador de me
- A cardioplegia retrógrada pelo seio coronário garante proteção homogênea de todos os terri
- A ecocardiografia transesofágica intraoperatória pode contribuir para avaliar função ventr
- A cirurgia de revascularização do miocárdio elimina definitivamente a aterosclerose e torn
- Ruptura do septo interventricular após infarto pode gerar desvio agudo esquerda-direita, s
- O músculo papilar posteromedial é relativamente vulnerável à ruptura pós-infarto por conta
- A ruptura de parede livre ventricular após infarto é uma causa possível de hemopericárdio 
- Na escolha de prótese valvar, próteses biológicas e mecânicas apresentam a mesma durabilid
- Abscesso perivalvar e infecção não controlada podem fundamentar cirurgia durante a fase at
- Na técnica de Bentall, a valva aórtica nativa é obrigatoriamente preservada e os óstios co
- Na classificação Stanford, qualquer dissecção que envolva a aorta ascendente é tipo A, ain
- TEVAR é a estratégia padrão para toda dissecção aguda Stanford A, independentemente do aco
- Uma dissecção tipo B com malperfusão visceral ou ruptura deixa de ser considerada não comp
- O diâmetro isolado de um aneurisma torácico é suficiente para estabelecer, em qualquer pac
- A persistência do canal arterial no período pós-natal pode ocasionar shunt da aorta para a
- A presença de Qp/Qs maior que 1 determina o fechamento obrigatório de qualquer comunicação
- CIV perimembranosa apresenta relação anatômica com o sistema de condução e sua correção po
- Mesmo depois do reparo da coarctação de aorta, são possíveis hipertensão arterial, recoarc
- Na d-transposição das grandes artérias, a circulação pulmonar e a sistêmica formam circuit
- O Glenn bidirecional conecta principalmente a veia cava inferior às artérias pulmonares e 
- Após Fontan, a ausência de ventrículo subpulmonar torna a circulação especialmente sensíve
```

## Pedido 19: CTB: sistema, circulação e proteção dos pedestres (`legislacao-transito.ctb-sistema-normas-circulacao`, 152 tópicos de edital)

```
Matéria: legislacao-transito.ctb-sistema-normas-circulacao (CTB: sistema, circulação e proteção dos pedestres)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições gerais, direito ao trânsito seguro e estrutura do Sistema Nacional de Trânsito": 19
- "2. Competências do SNT: CONTRAN, entes executivos e artigo 20 do CTB": 24
- "3. Regras de circulação, preferências de passagem e manobras": 19
- "4. Sinalização, veículos especiais, classificação de vias e regras de velocidade": 19
- "5. Direitos dos pedestres, travessias, bicicletas e aplicação integrada": 19

Já existem (não repita):
- Um estacionamento pertencente a supermercado privado é aberto ao uso coletivo de clientes.
- Um município elabora plano de segurança para circulação de pedestres, motocicletas e anima
- Uma proposta nacional de padronização das placas de trânsito será regulamentada. Qual inte
- Uma equipe da Polícia Rodoviária Federal realiza patrulhamento ostensivo em estrada federa
- Dois carros se aproximam de rotatória sem sinalização específica de preferência; um já a p
- Em interseção comum sem semáforo, placa ou entrada de rodovia, dois veículos chegam simult
- Em cruzamento, um agente de trânsito ordena parada, embora o semáforo esteja verde para os
- Um pedestre pretende atravessar avenida em local sem faixa imediata, mas existe travessia 
- Uma ciclista desce da bicicleta e passa a empurrá-la ao atravessar regularmente a via. Com
- Uma minuta de resolução regulamentar do CONTRAN é submetida à consulta pública eletrônica 
- Em rodovia federal, uma equipe apura sinistro com danos materiais e elabora perícia admini
- Dois automóveis se aproximam de cruzamento sem placas ou semáforo. Apenas um dos fluxos ch
- Um automóvel deixa garagem particular para entrar em avenida enquanto um pedestre e outro 
- Motorista chega a semáforo vermelho e pretende converter à direita em cruzamento que possu
- Uma via urbana arterial não possui placa regulamentadora de velocidade. Qual limite máximo
- Um pedestre atravessava regularmente na faixa quando o semáforo mudou para verde aos veícu
- Pedestres caminham em estrada rural sem acostamento utilizável, sob condições seguras e se
- Um órgão municipal afirma que pode, sem ressalvas, exercer toda competência fiscalizatória
- Após sinistro em rodovia federal, uma equipe sustenta que a atribuição de realizar perícia
- Uma ambulância se aproxima de cruzamento em atendimento de urgência, com alarme sonoro e l
- Um veículo aproxima-se de interseção com sinal verde, mas observa fila estacionada adiante
- Uma rodovia de pista simples não tem placa de velocidade. Nela circula motocicleta em cond
- Em um cruzamento, sinalização vertical de parada e semáforo em funcionamento coexistem. Um
- Uma via urbana não possui passeio utilizável. Pedestres circulam pelos bordos da pista, em
- Uma bicicleta circula em via urbana sem ciclovia ou ciclofaixa utilizável; em outra rua, p
- Uma pessoa utiliza via urbana a pé, ao passo que outra conduz animal por estrada aberta ao
- O Conselho Nacional de Trânsito é o órgão executivo responsável pela colocação cotidiana d
- No art. 7º do CTB, as JARI integram o Sistema Nacional de Trânsito, embora sua função recu
- Uma empresa presta serviço em via federal. Por estar nesse espaço, qualquer de seus veícul
- Em avenida sinalizada, um agente manda interromper o tráfego durante operação especial. A 
- Um ciclista desmontado e empurrando bicicleta continua sendo tratado, pelo art. 68, § 1º, 
- O CTB determina que, em cruzamento comum sem sinalização e sem circunstância específica de
- Nas vias urbanas sem placa regulamentadora, a máxima supletiva de uma via coletora é 60 km
- Quando uma Polícia Militar exerce fiscalização de trânsito na hipótese prevista pelo art. 
- Condutor que pretende mudar de faixa e aciona antecipadamente a seta adquire preferência s
- Em via urbana na qual não haja passeio utilizável e tampouco proibição específica, o art. 
- Por ser veículo não motorizado, a bicicleta pode circular livremente em qualquer calçada u
- O art. 90 do CTB impede aplicar sanção por desrespeito a sinalização insuficiente ou incor
- Uma ambulância em atendimento prioritário pode atravessar qualquer cruzamento na velocidad
- Em rodovia de pista simples sem sinalização regulamentadora de velocidade, motocicleta est
- Um motorista tenta realizar ultrapassagem em interseção e sua proximidade porque não se ap
- Um órgão instala placa regulamentadora de velocidade máxima diferente daquela indicada na 
- Pedestre em travessia delimitada tem prioridade independentemente de qualquer sinalização 
- Em faixa sinalizada, o pedestre começou a travessia de modo regular e o foco depois mudou 
- Em via urbana sem passeio utilizável, o art. 68, § 2º, obriga pedestres a seguir no sentid
```

## Pedido 20: Competências comportamentais e mindset digital (`gestao-pessoas.competencias-comportamentais-digitais`, 151 tópicos de edital)

```
Matéria: gestao-pessoas.competencias-comportamentais-digitais (Competências comportamentais e mindset digital)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Comunicação assertiva e inteligência emocional": 18
- "2. Autodesenvolvimento, gestão do tempo e mindset de crescimento": 32
- "3. Colaboração, divergência construtiva e liderança": 16
- "4. Problemas complexos, visão sistêmica e intraempreendedorismo": 16
- "5. Mentalidade digital, trabalho à distância e aprendizagem contínua": 18

Já existem (não repita):
- Em uma reunião, Lara percebe que recebeu dados incompletos e precisa solicitar uma correçã
- Diante de relato confuso sobre falha em um sistema, uma pessoa quer verificar se compreend
- Após receber crítica à apresentação, Diego sente irritação, identifica a origem do incômod
- Em debate sobre atendimento, Júlia demonstra que compreendeu o receio de um colega, mas ap
- Depois de errar uma tarefa nova, uma pessoa deseja aplicar o mindset de crescimento. Qual 
- Uma equipe recebe uma solicitação com prazo curto, mas baixo impacto estratégico, enquanto
- Um setor processa muitos requerimentos rapidamente, mas a maioria precisa ser corrigida po
- Para dominar uma nova ferramenta, Marina organiza o estudo, testa conhecimentos sem consul
- Um responsável deseja orientar alguém que apresentou slides com dados sem fonte. Qual reto
- A coordenadora entrega a elaboração de relatório a uma integrante experiente, estabelece r
- Em discussão de projeto, dois integrantes discordam sobre critérios de análise e utilizam 
- Sem exercer chefia formal, Pedro define metas semanais, registra desvios e busca feedback 
- O atraso em um serviço resulta de regras de triagem, falhas de integração, demanda variáve
- Uma unidade reduz em 30% o próprio tempo de atendimento, mas transfere novas exigências pa
- Ao detectar duplicação de cadastros, uma funcionária propõe validação automática, testa um
- Após implantar uma ferramenta, a equipe nota queda simultânea em reclamações e espera médi
- Duas equipes compartilham um procedimento e aumentam a capacidade conjunta, mas disputam u
- Uma instituição substitui papel por formulário eletrônico, mantendo exigências redundantes
- Uma equipe distribuída precisa revisar texto técnico sem urgência de debate simultâneo, pr
- Uma pessoa sustenta que a existência do Programa de Gestão e Desempenho federal assegura a
- Após automatizar registros, o indicador de tarefas encerradas sobe, mas crescem erros e pe
- Ao testar um novo fluxo de atendimento, uma equipe planeja a intervenção, executa piloto, 
- Uma organização oferece certificados em grande quantidade, mas as pessoas não aplicam cont
- Uma profissional alterna a cada minuto entre produzir parecer técnico, responder mensagens
- Em equipe remota, uma chefia mede disponibilidade pelo ícone on-line, exige respostas imed
- Em conversa sobre tarefa atrasada, uma pessoa descreve o atraso e solicita novo prazo sem 
- Ao reconhecer o nervosismo antes de uma reunião, uma pessoa deve eliminar completamente es
- Uma profissional que analisa sua dificuldade, pratica nova estratégia de apresentação e so
- Uma atividade com prazo para hoje é necessariamente mais importante que um projeto estraté
- Uma equipe pode colaborar de modo produtivo mesmo quando seus integrantes sustentam interp
- Uma pessoa só demonstra autoliderança quando possui autoridade formal para delegar ativida
- Uma pessoa que identifica problema de atendimento e testa melhoria dentro da própria organ
- Um gestor que explica as razões de uma decisão, mas não confirma a compreensão das pessoas
- Ao receber uma crítica baseada em evidências, reconhecer a perspectiva do interlocutor e a
- No mindset de crescimento, elogiar repetidamente o esforço, mesmo quando a estratégia é in
- Ao delegar análise de documento, uma líder que especifica o produto final e combina marcos
- No mapeamento de um processo, a redução do tempo gasto em uma etapa comprova, isoladamente
- Uma avaliação de solução baseada em dados deve verificar qualidade da informação, hipótese
- Em trabalho remoto, a quantidade de horas em que o indicador de presença permanece verde s
- Uma inovação que melhora o indicador de produtividade de uma área, mas piora o tempo e o c
- Como o paradigma da abundância incentiva ganhos compartilhados, sua adoção dispensa critér
- Um serviço que usa inteligência artificial para recomendar decisões deve manter avaliação 
- Em atividades distribuídas, a comunicação assíncrona tem a propriedade de resolver imediat
- No desenvolvimento de competências, combinar prática real, feedback e revisão de estratégi
- A existência do Decreto nº 11.072/2022 e da IN Conjunta nº 24/2023 implica que a escolha d
```

## Pedido 21: Auditoria das contas do balanço e do resultado (`auditoria.auditoria-balanco-contas`, 150 tópicos de edital)

```
Matéria: auditoria.auditoria-balanco-contas (Auditoria das contas do balanço e do resultado)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Demonstrações, afirmações e planejamento dos testes": 25
- "2. Ativo circulante: caixa, recebíveis e estoques": 21
- "3. Ativo não circulante: longo prazo, investimentos, imobilizado e intangível": 26
- "4. Passivos, patrimônio líquido e subvenções": 14
- "5. Receitas, despesas, corte, integração e conclusão dos testes": 14

Já existem (não repita):
- Ao revisar as demonstrações de uma sociedade, um auditor identifica risco de erro relevant
- Uma empresa mantém no razão uma máquina que foi alienada antes do fechamento, mas não deu 
- Um auditor possui centenas de comprovantes internos para sustentar saldo material, mas ain
- Para examinar R$ 400 mil mantidos em conta bancária, a administração oferece uma carta ela
- Em 31 de dezembro, um lote de mercadorias custa R$ 50 mil, pode ser vendido por R$ 56 mil 
- A entidade informa que cliente de R$ 80 mil pagou R$ 30 mil logo após o encerramento. Para
- Uma investidora detém participação de 30% em empresa sobre a qual exerce influência signif
- Uma máquina disponível para uso durante todo o ano custou R$ 90 mil, tem valor residual es
- Uma sociedade gasta R$ 70 mil em pesquisas preliminares de um software, sem demonstrar ain
- Durante o fechamento anual, o auditor suspeita que mercadorias recebidas em dezembro tiver
- Uma ação judicial decorre de fato passado, gera obrigação presente, tem perda considerada 
- Uma empresa recebe subvenção para custear despesas de treinamento de dois exercícios; exis
- Em 29 de dezembro, cliente paga antecipadamente por mercadoria que ficará sob controle do 
- Uma contagem revela estoque final superavaliado em R$ 25 mil, sem outras distorções. Na fó
- A assembleia de uma sociedade aprova aporte em dinheiro de R$ 300 mil, mas a escrituração 
- Uma participação sujeita a equivalência patrimonial representa 25% do capital da investida
- Um equipamento apresenta valor contábil de R$ 150 mil, valor em uso de R$ 125 mil e valor 
- Em 30 de dezembro, a entidade faturou bens a cliente, reconheceu receita e custo e baixou 
- No encerramento, uma ação judicial configura obrigação apenas possível, com saída de recur
- Na contagem de dezembro, são encontrados R$ 90 mil de mercadorias entregues por fornecedor
- Uma obrigação bancária vence em três anos, mas a entidade descumpriu cláusula que a tornou
- Cliente paga R$ 120 mil por doze meses de serviço que se iniciam em 1º de janeiro; a entid
- O auditor não pôde presenciar a contagem física relevante por circunstância imprevista, ma
- Uma entidade tem contrato de empréstimo concedido a terceiro, vencível em 18 meses, sem re
- Em auditoria de recebíveis comerciais, existem várias faturas sem atraso, mas histórico de
- Uma companhia aberta prepara balanço, demonstração do resultado, demonstração dos fluxos d
- Ao selecionar uma nota fiscal contabilizada e procurar seu documento de suporte, o auditor
- Ao conferir o saldo de empréstimo e os encargos contratados, o auditor solicita confirmaçã
- Uma mercadoria custa R$ 80, pode ser vendida por R$ 90 e exigirá R$ 15 para conclusão e ve
- Um equipamento de R$ 60 mil, valor residual de R$ 12 mil e vida útil de seis anos pode ger
- Uma marca adquirida é classificada corretamente como intangível de vida útil indefinida; p
- Para investigar fornecedores omitidos, o auditor examina comprovantes de recebimento de be
- Ao auditar a venda de serviço ainda não executado, o profissional encontra nota fiscal emi
- Um crédito de empréstimo a terceiro vence em dezoito meses e não se realiza no ciclo opera
- Imóvel mantido exclusivamente para auferir aluguel, sem uso na produção nem na administraç
- Uma subvenção condicionada à realização de despesas em dois exercícios, cuja concessão e c
- Uma ação judicial classificada como perda apenas possível, com saída não remota e avaliaçã
- Ao examinar capital subscrito e integralizado, o auditor confronta atos societários e comp
- Se o estoque final foi superavaliado em R$ 40 mil, mantendo constantes estoque inicial e c
- Por circunstância imprevista, o auditor não acompanha a contagem física relevante na data 
- Um conjunto de mercadorias de terceiros aparece no depósito sob contrato de consignação. C
- Após a data do balanço, uma decisão judicial traz evidência adicional sobre obrigação já e
- Um banco confirma saldo positivo de depósito. Dessa confirmação o auditor pode concluir, s
- Um ativo está registrado por R$ 500 mil; seu valor em uso é R$ 430 mil e o valor justo líq
- O auditor encaminha pedido de confirmação positiva a cliente relevante e não recebe respos
```

## Pedido 22: Gestão patrimonial e contábil na Lei 4.320/1964 (`orcamento-publico.gestao-patrimonial-contabil`, 149 tópicos de edital)

```
Matéria: orcamento-publico.gestao-patrimonial-contabil (Gestão patrimonial e contábil na Lei 4.320/1964)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Objeto, princípios e estrutura da contabilidade pública": 23
- "2. Escrituração, sistemas de contas e PCASP": 16
- "3. Bens, inventário, dívidas e avaliação patrimonial": 21
- "4. Fatos contábeis, variações patrimoniais e serviços industriais": 19
- "5. Demonstrações da Lei 4.320/1964 e aplicação gerencial": 21

Já existem (não repita):
- Uma unidade pública recebe valores, realiza pagamentos e mantém equipamentos sob guarda de
- Um órgão identifica imóveis, equipamentos, créditos a receber e contas com fornecedores. Q
- Uma compra à vista de equipamento foi registrada por débito no imobilizado e crédito em ca
- Uma unidade recebe oito computadores novos e registra número patrimonial, características 
- A contabilidade mantém conta consolidada de bens móveis e imóveis enquanto cada unidade co
- Um almoxarifado adquire 10 unidades por R$ 20 cada e mais 30 por R$ 40 cada. Sem saídas, q
- Na escrituração de obrigações, o responsável identifica Restos a Pagar e depósitos de terc
- O relatório anual compara receitas previstas com arrecadadas e despesas autorizadas com re
- Ao organizar o PCASP, uma contadora encontra contas de ativo, passivo, VPD e VPA. Quais cl
- Uma equipe separa registros de aprovação da programação e de execução das dotações no PCAS
- Um crédito vencido de multa administrativa não tributária foi apurado como líquido e certo
- Uma entidade assumiu empréstimo para financiar obra pública, exigível em dezoito meses. Qu
- Um fundo especial legalmente instituído encerra o exercício com saldo positivo, sem dispos
- Um serviço público de caráter industrial é prestado diretamente por uma entidade pública, 
- Uma unidade compra equipamento por R$ 15.000, entrega imediata e pagamento à vista; não há
- Um equipamento destinado ao serviço público gera depreciação mensal reconhecida antes de q
- Uma entidade registra novo prédio adquirido por compra, antes de depreciação e de outros a
- Ao fechar o exercício, uma entidade inscreve Restos a Pagar correspondentes a despesas orç
- Uma entidade inicia o exercício com R$ 120 mil em espécie. Durante o período recebe efetiv
- Uma análise usa a redação do art. 105 da Lei nº 4.320/1964 e não a apresentação por circul
- O relatório patrimonial do exercício informa VPA de R$ 740 mil e VPD de R$ 680 mil, sem ou
- Uma entidade arrecadou R$ 960 mil de receitas orçamentárias e empenhou R$ 880 mil de despe
- Uma máquina tem valor contábil líquido de R$ 20 mil e é vendida por R$ 23 mil, recebidos à
- Um equipamento custou R$ 60 mil, tem valor residual previsto de R$ 6 mil e vida útil estim
- Um demonstrativo tradicional utiliza ativo financeiro/permanente segundo o art. 105, enqua
- O art. 85 da Lei nº 4.320/1964 determina que os serviços de contabilidade permitam acompan
- Uma unidade pública compra uma impressora e a paga à vista. Como ocorreu saída de dinheiro
- A classe 3 do PCASP corresponde às variações patrimoniais diminutivas, e a classe 4 corres
- A Lei nº 4.320/1964 exige no art. 94 apenas registros agregados de bens permanentes, dispe
- Um almoxarifado compra lotes de tamanhos diferentes a preços distintos. Para avaliação dos
- Dívida ativa e dívida flutuante possuem o mesmo sentido: ambas representam exclusivamente 
- O Balanço Orçamentário, previsto no art. 102 da Lei nº 4.320/1964, permite confrontar prev
- Ao reconhecer serviço de limpeza já prestado e ainda não pago, a entidade deve postergar o
- Um depósito de terceiro recebido em caixa com obrigação de devolução pode aumentar simulta
- As classes 7 e 8 do PCASP correspondem, respectivamente, aos controles de aprovação e de e
- O levantamento geral dos bens móveis e imóveis exige cotejar inventário analítico das unid
- Pela Lei nº 4.320/1964, o serviço público industrial que não está constituído como empresa
- Uma entidade reconhece serviço recebido em dezembro, mas só o pagará no exercício seguinte
- O parágrafo único do art. 103 da Lei nº 4.320/1964 determina que a inscrição de Restos a P
- No art. 105 da Lei nº 4.320/1964, o ativo financeiro compreende, além de numerário, crédit
- O patrimônio público foi alterado pela substituição de crédito a receber por dinheiro, em 
- Uma entidade mantém prédio utilizado no serviço público. O fato de não destiná-lo à venda 
- Pela Lei nº 4.320/1964, o saldo positivo de fundo especial deve ser usado sem dotação no e
- Com VPA de R$ 500 mil e VPD de R$ 530 mil, sem outras alterações do resultado, a DVP indic
- O art. 106, II, da Lei nº 4.320/1964 determina que todos os bens móveis e imóveis sejam av
```

## Pedido 23: Crimes contra a propriedade imaterial e a organização do trabalho (`direito-penal.crimes-propriedade-imaterial-organizacao-trabalho`, 149 tópicos de edital)

```
Matéria: direito-penal.crimes-propriedade-imaterial-organizacao-trabalho (Crimes contra a propriedade imaterial e a organização do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Propriedade intelectual, direitos autorais e art. 184": 23
- "2. Programas de computador e propriedade industrial": 24
- "3. Liberdade de trabalho, paralisações e sabotagem": 21
- "4. Fraude trabalhista, aliciamento e trabalho escravo": 21
- "5. Procedibilidade, prova, competência e casos integrados": 11

Já existem (não repita):
- Uma pessoa divulga, sem autorização, a reprodução de texto protegido, sem intuito de lucro
- Uma pessoa reproduz sem autorização centenas de exemplares de livro protegido, com a inten
- Uma plataforma oferece lucrativamente obras protegidas para seleção individual e acesso so
- Uma criação de software é publicada em 2020 e seu titular não promove registro. Qual regra
- Uma pessoa viola direito autoral sobre programa de computador sem circunstância de reprodu
- Uma fábrica reproduz em seus produtos sinal distintivo registrado por concorrente, em form
- Por grave ameaça, uma pessoa obriga trabalhadora a ingressar em associação profissional co
- Durante abandono coletivo de trabalho com violência contra máquinas, o processo precisa ve
- Trabalhadores são submetidos a jornada exaustiva e condições degradantes graves, embora co
- Um colecionador compra legalmente uma edição impressa de romance e pretende comercializar 
- Uma pessoa adquire legitimamente licença de programa e mantém uma única cópia de segurança
- Em uma loja são encontradas cópias de programa reproduzidas em violação de direito autoral
- Uma pequena empresa sofre reprodução ilícita de sua marca registrada. A defesa invoca o ar
- Mediante grave ameaça, participantes de um movimento obrigam o responsável por estabelecim
- Uma pessoa usa grave ameaça para obrigar empresário a deixar de comprar matéria-prima de c
- Um grupo participa de suspensão coletiva das atividades e causa interrupção de serviço de 
- Um contratante retém os documentos de identificação de prestadora de serviços para impedi-
- Uma entidade pública é diretamente prejudicada por violação autoral praticada em modalidad
- Foram apreendidas milhares de mídias suspeitas de reprodução ilícita e o exame técnico est
- Uma pessoa desenvolve aplicativo fora de suas tarefas, sem usar equipamentos, dados ou seg
- Um grupo ocupa estabelecimento agrícola e destrói equipamentos com o propósito comprovado 
- Uma agência fictícia promete empregos em outro país e recruta trabalhadores por meio de in
- Trabalhadores são recrutados em município distinto do local da obra, dentro do Brasil; o r
- A exploração laboral que preenche o art. 149 é praticada contra adolescente e há prova dos
- Uma empresa lança mão de fraude contábil para negar direito trabalhista; outra submete pes
- Uma pessoa mantém expostos para venda discos com cópias não autorizadas, visando lucro. Co
- Em uma disputa sobre cópia de software, a defesa sustenta que o programa não está protegid
- Uma trabalhadora é compelida, por grave ameaça, a não exercer sua profissão em determinado
- Em paralisação pacífica de trabalhadores que não praticaram violência contra pessoas ou co
- Uma pessoa recruta trabalhadores com promessas fraudulentas de emprego para levá-los a paí
- Uma pessoa é mantida em trabalho degradante com jornadas exaustivas, mas sem retenção físi
- Uma pessoa copia trechos de obra intelectual protegida e outra reproduz literalmente o tex
- Uma plataforma comercial disponibiliza obras protegidas para seleção sob demanda sem autor
- Quem adquire cópia legítima de software pode reproduzir um exemplar para salvaguarda, nos 
- Uma pessoa utiliza grave ameaça para obrigar trabalhador a assinar contrato de emprego. A 
- Um comerciante obriga empregados a comprar em determinado estabelecimento, gerando dívida 
- Em aliciamento de trabalhadora gestante de uma localidade brasileira para outra, a fração 
- Uma pessoa continua a exercer atividade após decisão administrativa que expressamente a im
- Em apreensão de centenas de mídias falsificadas, a prova da materialidade de violação auto
- Em caso de violação autoral praticada em prejuízo de entidade pública expressamente enumer
- Ao revisar alterações em software, um autor afirma que todos os direitos morais das obras 
- Quatro empregados abandonam coletivamente o trabalho e, no curso da paralisação, alguns pr
- Trabalhadores submetidos a condições degradantes e jornadas exaustivas não podem ser consi
- Um comerciante é ameaçado gravemente para não comprar produto agrícola de determinado prod
- Uma pessoa é investigada por comercializar cópias ilícitas de programa de computador. A aç
```

## Pedido 24: Convenção Americana sobre Direitos Humanos (`direito.convencao-americana-sobre-direitos-humanos`, 149 tópicos de edital)

```
Matéria: direito.convencao-americana-sobre-direitos-humanos (Convenção Americana sobre Direitos Humanos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura e deveres dos Estados (arts. 1º–2º)": 26
- "2. Direitos civis, políticos e garantias (arts. 3º–25)": 25
- "3. Direitos progressivos, suspensão e interpretação (arts. 26–32)": 25
- "4. Comissão, Corte e como o tema é cobrado (arts. 33–82)": 24

Já existem (não repita):
- A Convenção Americana, conhecida como Pacto de São José, foi assinada em qual data e cidad
- Uma pessoa estrangeira encontra-se sob a jurisdição de Estado Parte. Qual afirmação corres
- Um direito protegido pela Convenção ainda não está garantido por lei ou outra medida inter
- Para fins da Convenção Americana, qual pessoa é titular dos direitos reconhecidos?
- Sobre o direito à vida e a pena de morte, assinale a alternativa correta segundo o art. 4º
- Uma pessoa privada de liberdade é submetida a tratamento degradante. Qual princípio do art
- Um Estado Parte enfrenta emergência que ameaça sua segurança e pretende suspender obrigaçõ
- Um empregador exige trabalho forçado de pessoa que não está cumprindo pena. Qual alternati
- Uma acusada é julgada sem intérprete, sem tempo para preparar defesa e sem poder interroga
- Uma pessoa praticou fato que não era delito; depois, lei posterior tornou a conduta crimin
- Uma pessoa sem recursos pretende denunciar violação da Convenção perante a Comissão Intera
- Um indivíduo apresenta à Corte Interamericana, sem participação da Comissão, uma demanda c
- A petição foi apresentada sete meses após notificação da decisão interna definitiva, sem d
- Em Estado Parte, os recursos internos não oferecem devido processo e a pessoa não teve ace
- Uma lei posterior reduz a pena de delito já praticado, enquanto outra cria tipo penal novo
- Após reconhecer violação em caso submetido, a Corte considera risco extremo e urgente de d
- Uma pessoa estrangeira é expulsa coletivamente e enviada a país onde sua vida corre risco 
- Dois Estados Partes não reconheceram por declaração específica a competência da Comissão p
- Um preso político está em Estado Parte que declarou emergência. A autoridade pretende susp
- Um Estado Parte quer denunciar a Convenção após quatro anos de vigência e pretende afastar
- Um Estado depositou o décimo instrumento de ratificação da Convenção. Qual regra do art. 7
- A Corte profere sentença contenciosa contra Estado Parte que reconheceu sua competência. O
- Um Estado sustenta que os direitos econômicos, sociais, educacionais, científicos e cultur
- Um intérprete propõe ler a Convenção de modo a reduzir uma garantia reconhecida em lei int
- Durante emergência, o Estado suspende obrigações e comunica a decisão apenas seis meses de
- Uma lei restringe direito convencional sem indicar interesse geral nem relação entre a med
- Um Estado Parte afirma que qualquer pessoa pode encaminhar diretamente denúncia contencios
- Um candidato foi impedido de concorrer exclusivamente por sua idade. Qual afirmação corres
- Uma propriedade é desapropriada por utilidade pública, com indenização conforme a lei. Com
- Uma pessoa foi definitivamente absolvida e, depois, processada novamente pelos mesmos fato
- Para fins da Convenção Americana, pessoa é todo ser humano.
- O art. 1º obriga os Estados Partes a respeitar os direitos apenas de seus cidadãos que est
- O art. 2º pode exigir medidas de outra natureza, além de providências legislativas, para t
- A Convenção Americana entrou em vigor internacionalmente no próprio dia de sua assinatura,
- A proteção contra a pena de morte inclui a proibição de executá-la enquanto estiver penden
- Toda confissão assinada por pessoa acusada é válida, mesmo que tenha sido obtida por coerç
- Durante o processo, a pessoa acusada de delito tem direito de recorrer da sentença condena
- A Convenção garante direito de resposta ou retificação a quem for atingido por informação 
- O art. 23 permite restringir direitos políticos por qualquer motivo que a legislação inter
- A Corte Interamericana pode emitir pareceres sobre interpretação da Convenção e de outros 
- Um Estado federal pode deixar de cumprir a Convenção nas matérias de competência de suas e
- O art. 29 impede interpretar a Convenção para excluir direitos e garantias inerentes à pes
- A denúncia prevista no art. 78 produz efeito imediato e elimina a responsabilidade do Esta
- A lei pode subordinar o uso e gozo da propriedade ao interesse social, e a privação por ut
- O art. 16 autoriza restringir a liberdade de associação de integrantes das forças armadas 
```

## Pedido 25: Planejamento, Pesquisa, Gestão Social e Assessoria em Serviço Social (`servico-social.planejamento-pesquisa-gestao-social`, 148 tópicos de edital)

```
Matéria: servico-social.planejamento-pesquisa-gestao-social (Planejamento, Pesquisa, Gestão Social e Assessoria em Serviço Social)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento: conceitos, níveis e modalidades": 25
- "2. Projetos sociais: elaboração, monitoramento e avaliação": 25
- "3. Gestão social, terceiro setor e assessoria": 25
- "4. Dimensão investigativa e pesquisa social": 25

Já existem (não repita):
- Pela Lei 8.662/1993, prestar assessoria e consultoria a órgãos públicos e entidades em mat
- No Serviço Social, o planejamento é compreendido como processo contínuo e político, que en
- Considerando os níveis do planejamento social, assinale a sequência que vai do mais amplo 
- O projeto é o nível mais abrangente do planejamento, reunindo as diretrizes gerais de uma 
- A modalidade de planejamento que reconhece a existência de atores com interesses diferente
- O planejamento normativo parte da ideia de uma realidade estável e previsível, centrada na
- O planejamento participativo, coerente com o projeto ético-político do Serviço Social, car
- O diagnóstico social deve limitar-se a descrever a situação encontrada, sem interpretar os
- Os momentos clássicos do processo de planejamento, que não são etapas estanques, incluem:
- Em um projeto social, a meta corresponde à quantificação do objetivo em um prazo determina
- O acompanhamento contínuo das ações durante a execução de um projeto, com a finalidade de 
- A avaliação realizada antes da implantação de um projeto, para analisar sua viabilidade, é
- O critério de avaliação que mede o grau de alcance dos objetivos e metas propostos em um p
- Um projeto que atingiu todas as metas de atendimento, mas não alterou as condições de vida
- A eficiência de um projeto social refere-se ao impacto que ele produz na vida dos usuários
- Sobre indicadores sociais utilizados no monitoramento e na avaliação, é correto afirmar qu
- A avaliação participativa envolve os próprios sujeitos do projeto, como usuários e equipe,
- Em um projeto social, o item que explica por que a intervenção é necessária, a partir do d
- Indicadores de impacto avaliam mudanças mais amplas e duradouras na realidade da população
- Na perspectiva crítica presente no Serviço Social, o crescimento do chamado terceiro setor
- A gestão social de políticas públicas, na perspectiva do Serviço Social, deve ser orientad
- Na assessoria, o assistente social acompanha de forma sistemática uma equipe ou instituiçã
- A consultoria é, em geral, uma intervenção mais pontual sobre uma questão específica, que 
- Segundo a Lei 8.662/1993, constitui atribuição privativa do assistente social:
- Elaborar, coordenar, executar e avaliar planos, programas e projetos no âmbito de atuação 
- A supervisão direta de estagiários de Serviço Social é atribuição privativa do assistente 
- No debate crítico do Serviço Social, o discurso do terceiro setor:
- O assistente social que atua na gestão de políticas sociais deve restringir-se à execução 
- A dimensão investigativa é constitutiva do exercício profissional do assistente social e n
- A abordagem de pesquisa voltada à compreensão de significados, valores e experiências dos 
- As abordagens qualitativa e quantitativa são incompatíveis entre si e não podem ser combin
- Na pesquisa em Serviço Social envolvendo seres humanos, NÃO é exigência ética:
- A atitude investigativa no trabalho cotidiano do assistente social permite:
- Entre as etapas de uma pesquisa social, a definição do universo, da amostra e das técnicas
- A devolução dos resultados da pesquisa aos sujeitos que dela participaram é coerente com o
- Técnica típica da abordagem quantitativa em pesquisa social é:
- O Planejamento Estratégico Situacional organiza o processo de planejamento em momentos, en
- O programa, como nível do planejamento, caracteriza-se por:
- A análise de indicadores sociais pelo assistente social pode contribuir para revelar desig
- A avaliação ex post de um projeto social tem como foco principal:
```

## Pedido 26: Férias (`direito-trabalho.ferias`, 148 tópicos de edital)

```
Matéria: direito-trabalho.ferias (Férias)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direito às férias, período aquisitivo, faltas e perda do direito": 26
- "2. Concessão, aviso, época, fracionamento e dobra": 24
- "3. Férias coletivas: alcance, duração e novos empregados": 13
- "4. Remuneração, terço constitucional, médias e abono pecuniário": 25
- "5. Férias na rescisão, cálculos proporcionais e jurisprudência": 12

Já existem (não repita):
- Durante o período aquisitivo, um empregado registrou exatamente cinco faltas injustificada
- Uma trabalhadora acumulou sete faltas injustificadas no mesmo ciclo anual, sem outras ocor
- Uma pessoa iniciou contrato celetista em janeiro e quer saber quanto dura, em regra, o per
- Depois de completar os doze meses necessários à aquisição, o empregado ainda não gozou fér
- O descanso semanal remunerado de um empregado acontece no domingo. O empregador pretende i
- Empregado com direito a trinta dias concorda em usufruir três blocos de férias de 14, 8 e 
- A empresa comunica por escrito a um empregado as férias individuais que começariam dez dia
- Uma empresa pretende dividir férias coletivas de determinado setor em dois períodos de 11 
- A empresa planeja férias coletivas e deve informar as datas ao órgão competente, além de c
- Um empregado com 30 dias de férias adquiridos pede regularmente conversão do máximo admiti
- Empregado deseja exercer a faculdade de converter parte de suas férias individuais em abon
- Trabalhador com remuneração mensal fixa de R$ 3.000 desfruta 30 dias de férias sem adicion
- Uma empresa concedeu férias regulares com início em 20 de setembro. Em regra, até qual dia
- Uma empregada recebeu prestações previdenciárias por auxílio-doença por sete meses, embora
- Dois familiares trabalham no mesmo estabelecimento e solicitam férias no mesmo período. O 
- Uma empresa adota férias coletivas de vinte dias. Um empregado contratado há seis meses te
- Uma empregada é remunerada por comissões e vai tirar férias em novembro. Para calcular a p
- Um empregado usufruiu todos os trinta dias de férias dentro do período concessivo, mas rec
- Um empregado tinha direito a trinta dias; gozou vinte dias dentro do período concessivo e 
- Um trabalhador teve vinte e quatro dias de férias por se enquadrar na segunda faixa de fal
- Um empregado permaneceu 31 dias em licença remunerada durante seu período aquisitivo, e vo
- Na rescisão por justa causa, o empregado tem férias integrais adquiridas de ciclo anterior
- Um empregado pediu demissão com apenas oito meses de serviço e não havia completado períod
- Empregado pretende reclamar judicialmente remuneração de férias não concedidas. Ao identif
- Uma empresa quer substituir parte das férias coletivas de um setor por abono pecuniário. Q
- Um empregado completou doze meses de contrato com quatro faltas injustificadas; em regra, 
- O empregador pode descontar individualmente sete dias das férias de trinta dias por sete f
- Com concordância do empregado e observadas as demais restrições, é admissível fracionar tr
- A comunicação escrita de férias individuais entregue com quinze dias de antecedência satis
- As férias coletivas podem abranger apenas um setor da empresa e ser concedidas em dois per
- O terço constitucional das férias e o abono pecuniário são a mesma vantagem: ambos corresp
- Para férias coletivas, o art. 139, §§2º e 3º, da CLT exige, como regra, antecedência mínim
- A empresa pode dividir férias coletivas em três períodos de quatorze, oito e oito dias, de
- Empregado admitido há menos de doze meses participa de férias coletivas e, conforme o art.
- Nas férias individuais, o empregador pode converter unilateralmente um terço dos dias de d
- O art. 145 da CLT determina que férias e eventual abono sejam pagos até dois dias antes do
- Mesmo quando todo o gozo ocorreu no período concessivo, o atraso isolado no pagamento das 
- Quando a remuneração é paga por comissão, o art. 142, §3º, prevê média do montante percebi
- O empregado que recebeu benefício previdenciário por doença exatamente durante seis meses 
- Licença remunerada superior a trinta dias dentro do período aquisitivo pode retirar o dire
- Após a Reforma Trabalhista, o art. 134, §2º, da CLT continua impedindo o fracionamento de 
- Quando apenas parte dos dias de férias é usufruída depois do prazo concessivo, a Súmula 81
- A dispensa por justa causa elimina tanto as férias proporcionais do ciclo incompleto quant
- O trabalhador que pede demissão antes de completar doze meses de serviço conserva direito 
- Na proporcionalidade rescisória do art. 146, parágrafo único, da CLT, a fração de quatorze
```

## Pedido 27: Teoria da Constituição, neoconstitucionalismo e jurisdição constitucional (`direito-constitucional.teoria-constituicao-neoconstitucionalismo-jurisdicao`, 147 tópicos de edital)

```
Matéria: direito-constitucional.teoria-constituicao-neoconstitucionalismo-jurisdicao (Teoria da Constituição, neoconstitucionalismo e jurisdição constitucional)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constitucionalismo, história constitucional e transformações do Estado": 20
- "2. Democracia, soberania popular e supremacia da Constituição": 18
- "3. Norma constitucional, interpretação, integração e direito intertemporal": 20
- "4. Neoconstitucionalismo, pós-positivismo e constitucionalismo além do Estado": 20
- "5. Jurisdição constitucional, precedentes e judicialização de direitos": 22

Já existem (não repita):
- Uma pesquisa histórica identificou uma Constituição brasileira outorgada, com regime monár
- Um historiador identifica o primeiro texto constitucional republicano do país, que rompeu 
- Ao analisar a evolução do constitucionalismo social no Brasil, uma estudante procura o tex
- Um quadro compara a Constituição de 1934 com a que veio em seguida, identificando nesta co
- Uma exposição histórica destaca a Constituição promulgada em 5 de outubro, conhecida pela 
- Em uma aula, alguém distingue o poder responsável por inaugurar uma ordem constitucional d
- Um material de formação afirma que, segundo a Constituição, o povo pode exercer o poder po
- Uma comunidade será consultada antes da adoção de determinada decisão política de interess
- Uma proposta de emenda constitucional está em discussão. Para ser aprovada regularmente, q
- Durante a vigência de estado de defesa, um grupo defende tramitar emenda constitucional, a
- Uma iniciativa de reforma pretende eliminar a periodicidade do voto, mantendo a escolha di
- Uma pessoa pretende exercer profissão para a qual a Constituição admite qualificações fixa
- O texto constitucional traça programa institucional cuja operacionalização completa depend
- Ao longo do tempo, intérpretes reconhecem significado constitucional novo para determinado
- Uma lei editada antes da Constituição de 1988 disciplina assunto compatível com a nova ord
- Em discussão teórica, sustenta-se que uma regra da Constituição revogada permanece no novo
- Um texto sustenta que o constitucionalismo social ampliou deveres estatais prestacionais, 
- Um estudo descreve problemas de direitos humanos examinados simultaneamente por ordens jur
- Uma convenção internacional de direitos humanos foi aprovada em dois turnos em cada Casa, 
- Uma lei comporta duas leituras razoáveis: uma preserva sua compatibilidade com a Constitui
- Uma família solicita judicialmente vaga pública de creche para criança de dois anos. Consi
- Após comprovar deficiência grave em serviço público essencial, uma decisão judicial preten
- Em caso novo, a decisão anterior é citada com páginas inteiras de considerações laterais, 
- Uma parte invoca precedente relativo a serviço inteiramente inexistente. No novo processo,
- Uma política pública restringe pouco a liberdade individual, mas deixa sem proteção efetiv
- Ao comparar a Constituição de 1824 à de 1891, um pesquisador afirma que a primeira era imp
- Um texto sobre a soberania popular conclui que a Constituição de 1988 permite seu exercíci
- Em proposta de revisão da Constituição, uma pessoa observa que a aprovação de emenda exige
- Ao classificar norma constitucional que admite restrição legal a exercício profissional, u
- Um parecer sobre lei anterior à Constituição de 1988 sustenta que, sendo seu conteúdo mate
- Em estudo de teoria constitucional, atribui-se ao neoconstitucionalismo a tese obrigatória
- Uma proposta de mudança constitucional pretende eliminar a forma federativa, embora respei
- Um estudo classifica a Constituição de 1988 como rígida e conclui que, por isso, seu texto
- Uma decisão interpreta dispositivo à luz de outras garantias constitucionais para evitar c
- Após a promulgação de nova Constituição, um parecer presume que todas as normas da Constit
- Um estudo sobre o novo constitucionalismo latino-americano destaca plurinacionalidade, int
- Um pesquisador conclui que o transconstitucionalismo determina reconhecer hierarquia super
- Uma discussão acadêmica distingue a chegada de demanda de saúde ao Judiciário da avaliação
- Em litígio que revela grave deficiência estrutural de serviço essencial, uma decisão aplic
- Uma ação busca vaga de pré-escola para criança de cinco anos. A tese do Tema 548 permite r
- Uma decisão considera um precedente obrigatório apenas por ter encontrado frase semelhante
- Um colegiado aplica precedente pertinente ao novo caso e identifica expressamente a razão 
- Uma pessoa sustenta que, por ter sido incluída formalmente no texto de 1988, qualquer emen
- Ao avaliar política que deixa grupo vulnerável sem proteção efetiva, um estudo afirma que 
- Em uma audiência, alguém afirma que a transformação do significado interpretativo da Const
```

## Pedido 28: Crimes contra o sistema financeiro, o mercado de capitais e a economia popular (`legislacao-penal-especial.crimes-sistema-financeiro-economia-popular`, 147 tópicos de edital)

```
Matéria: legislacao-penal-especial.crimes-sistema-financeiro-economia-popular (Crimes contra o sistema financeiro, o mercado de capitais e a economia popular)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Crimes contra o Sistema Financeiro Nacional: Lei 7.492/1986": 30
- "2. Mercado de capitais e crimes contra a ordem econômica": 19
- "3. Economia popular e crimes contra as relações de consumo": 16
- "4. Crimes falimentares, propriedade industrial e programas de computador": 22
- "5. Finanças públicas, Estado Democrático de Direito e competência penal": 13

Já existem (não repita):
- Uma empresa passa a oferecer custódia e intermediação de ativos virtuais de terceiros. Ao 
- Ao apurar a administração de uma instituição financeira, identificam-se, em um caso, fraud
- Uma sociedade oferece serviços de câmbio ao público sem possuir a autorização exigida para
- Uma pessoa obtém financiamento com documentos falsos, sem qualquer informação de que depoi
- Um agente realiza operação de câmbio não autorizada para promover a evasão de divisas do B
- Operadores criam negociações simuladas para sustentar artificialmente a cotação de determi
- Antes da divulgação de fato relevante, um investidor negocia valores mobiliários utilizand
- Uma pessoa exerce gratuitamente atividade de assessor de investimento, sujeita a registro 
- Uma rede de participantes recebe promessa de remuneração que depende principalmente da ent
- Um fornecedor deixa de incluir advertência ostensiva de periculosidade na embalagem de pro
- Antes de obter recuperação judicial, dirigente oculta deliberadamente bens da empresa para
- A fraude descrita no art. 168 da Lei 11.101 ocorreu antes da decisão judicial de recuperaç
- O Ministério Público deixa transcorrer a oportunidade legal de acusação por crime faliment
- Uma pessoa imita marca registrada, de modo a causar confusão, sem elementos referentes ao 
- Uma empresa reproduz programa de computador protegido, sem autorização, em cópias destinad
- Nos dois últimos quadrimestres do último ano do mandato, uma autoridade assume obrigação c
- Grupo emprega grave ameaça com propósito de abolir o Estado Democrático de Direito, impedi
- Um grupo tenta, mediante violência, depor o governo legitimamente constituído, sem que a n
- Profissional com acesso sigiloso a fato relevante de companhia transmite informação a terc
- Empresa recebe carga de combustível por preço incompatível com seu valor e em circunstânci
- Em investigação de crime da Lei 7.492 praticado em coautoria, um participante confessa esp
- Dirigente de empresa em recuperação mantém deliberadamente contas paralelas às exigidas pe
- A violação comercial de programa de computador causou também perda de arrecadação tributár
- Dois delitos do capítulo de crimes contra instituições democráticas são praticados no mesm
- Uma investigação identifica apenas pirâmide de recrutamento com promessas irreais de retor
- Em ação penal por gestão fraudulenta de instituição financeira, a acusação foi promovida p
- Uma instituição financeira recebeu financiamento oficial destinado à construção de unidade
- Operações artificiais coordenadas para aumentar o volume aparente de negociação de ações, 
- Pessoa atua gratuitamente como assessora de investimento, sem registro legalmente obrigató
- Após disponibilizar produto ao público, fornecedor descobre risco à saúde e deixa de comun
- Autoridade ordena emissão de títulos da dívida pública sem criação por lei. O enquadrament
- Atos fraudulentos contra credores praticados antes da decretação da falência podem integra
- Uma imitação de marca registrada se enquadra no art. 189 da Lei 9.279/1996. O art. 199 det
- Pessoa comercializa cópias ilegais de programa de computador, causando perda de arrecadaçã
- Com a mudança promovida pela Lei 15.348/2026, o art. 1º, II, da Lei 8.176 continua incrimi
- Quem, em atividade comercial, mantém em depósito combustível que sabe proveniente de crime
- Comerciante primário recebe combustível em circunstâncias objetivas de origem criminosa pr
- Em caso de pirâmide financeira de recrutamento sem atividade típica de instituição finance
- Fornecedor que promove anúncio enganoso só pode responder pelo art. 67 do CDC quando algum
- O art. 25, § 2º, da Lei 7.492 admite redução de um a dois terços para coautor ou partícipe
- Um governante autoriza obrigação nos dois últimos quadrimestres do ano anterior ao último 
- Em reunião pacífica, cidadãos criticam intensamente os poderes constitucionais e reivindic
- O art. 359-M-B do CP, acrescentado pela Lei 15.402/2026, concede redução de um terço a doi
- No art. 4º da Lei 7.492, gestão temerária e fraudulenta recebem enquadramentos e faixas de
- Nos crimes falimentares da Lei 11.101, uma decisão de recuperação torna o próprio juízo em
```

## Pedido 29: Virtualização e ferramentas VMware (`informatica.virtualizacao-vmware`, 147 tópicos de edital)

```
Matéria: informatica.virtualizacao-vmware (Virtualização e ferramentas VMware)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da virtualização, hipervisores e isolamento": 15
- "2. ESXi e vCenter Server: gestão, mobilidade e disponibilidade": 18
- "3. VMware NSX e Cloud Director: rede virtual e multitenancy": 20
- "4. vRealize: automação, orquestração, logs e operações": 26
- "5. Harbor, Clair, segurança e integração operacional": 21

Já existem (não repita):
- Um administrador instala uma camada de virtualização diretamente em um servidor físico, se
- Uma equipe compara contêineres Linux com máquinas virtuais convencionais e deseja distingu
- Um operador pretende conservar a capacidade de restaurar dados mesmo que o datastore princ
- Uma infraestrutura possui dez hosts ESXi e deseja administrar VMs, inventário e permissões
- Antes de atualizar um host ESXi que está saudável, a equipe deseja realocar uma VM ligada 
- Um servidor físico falha abruptamente em cluster que possui recurso e capacidade de recupe
- Dois hosts de um cluster apresentam cargas muito diferentes e a equipe quer recomendações 
- A equipe precisa limitar o tráfego leste-oeste entre grupos de VMs que compartilham o ambi
- Um provedor deseja oferecer datacenters virtuais a clientes separados por organizações e c
- Os arquivos de uma VM ligada precisam ser transferidos do datastore A para B, sem mudar pr
- Um administrador ajusta uma VM para prioridade relativa de CPU em momentos de contenção, s
- Uma rede NSX utiliza segmentos lógicos conectando VMs em hosts distintos sobre um tecido I
- Uma organização conectou redes de aplicação a gateways lógicos do NSX e precisa alcançar r
- No VMware Cloud Director, o provedor estabelece capacidade de infraestrutura para várias o
- Usuários autorizados solicitam uma VM padronizada por portal, com aprovação e regras de qu
- Após implantar uma VM, uma automação precisa chamar API de inventário, obter endereço de r
- Um cluster registra tendência de saturação de CPU, enquanto há necessidade separada de pes
- Uma equipe exige duas características diferentes: recuperar VMs após perda física de host 
- Uma política NSX deve acompanhar VMs de uma categoria de aplicação mesmo quando novos inte
- O provedor utiliza Cloud Director para atender três organizações; cada uma deve poder aces
- Uma solicitação de VM aparece em catálogo de autosserviço, mas a etapa de registrar o equi
- Uma equipe opera imagens de contêiner num registro e quer analisar bibliotecas vulneráveis
- Um manual antigo do Harbor descreve Clair como scanner integrado. A implantação atual está
- Uma imagem recebe relatório sem CVEs em uma execução do scanner, mas sua aplicação pode co
- Um serviço de contêiner roda numa VM ESXi. É preciso: bloquear conexão não autorizada entr
- Uma empresa executa máquinas virtuais em ESXi sobre um servidor físico; o ESXi é classific
- Um cluster utiliza vCenter Server para administrar dez hosts ESXi. Nesse caso, é o vCenter
- Em uma plataforma virtualizada, a configuração de oito vCPUs para uma VM não comprova que 
- Se uma VM possui snapshot recente no mesmo datastore e esse datastore é totalmente perdido
- Uma VM ligada pode, respeitadas as condições de compatibilidade e conectividade, ter sua e
- O DRS possui como função principal reiniciar automaticamente uma VM cujo host sofreu pane 
- Em um projeto NSX, um segmento overlay depende da conectividade proporcionada pela rede un
- A criação de duas organizações no Cloud Director obriga a reservar um servidor ESXi físico
- No VMware Cloud Director, um Org vDC representa recursos virtualizados disponibilizados a 
- Um técnico deseja pesquisar falhas de autenticação textuais em logs coletados de hosts. A 
- Um item de catálogo de vRealize Automation pode acionar um workflow de vRealize Orchestrat
- Um registro Harbor armazena imagens de contêiner; por esse motivo, ele também é o hipervis
- Um servidor Red Hat Quay pode ser configurado para analisar imagens por Clair, que compara
- Um administrador precisa retirar de operação um host ESXi que já falhou abruptamente. Ness
- Políticas do NSX Distributed Firewall podem ser associadas a grupos dinâmicos baseados em 
- Um ambiente Cloud Director publica um catálogo para duas organizações. Como os modelos são
- Em uma infraestrutura vSphere, uma VM pode apresentar problemas de capacidade detectados p
- A partir do Harbor 2.2, Clair permaneceu o único scanner padrão integrado, e o uso de Triv
- Uma imagem de contêiner apresenta vulnerabilidade conhecida em scanner Clair; mesmo após b
- Quando o mesmo sistema usa VMware Cloud Director para multitenancy, ESXi para executar VMs
```

## Pedido 30: Formação econômica do Brasil até 1980 (`economia.formacao-economica-brasil-ate-1980`, 146 tópicos de edital)

```
Matéria: economia.formacao-economica-brasil-ate-1980 (Formação econômica do Brasil até 1980)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Modelo agroexportador, café, moeda e crise de 1929": 18
- "2. Industrialização de 1930 a 1945 e substituição de importações": 22
- "3. Pós-guerra, Plano de Metas e desafios do crescimento": 20
- "4. Plano Trienal, PAEG e reformas de 1962 a 1967": 21
- "5. Milagre econômico, I PND, choque do petróleo e II PND": 19

Já existem (não repita):
- Uma região expandiu a produção de café para exportação, construiu ferrovias e viu surgir o
- Em uma discussão histórica sobre a circulação monetária, um grupo sustenta que os meios de
- Uma exposição descreve a expansão das emissões bancárias, a criação de empresas e a especu
- No começo do século XX, produtores de café enfrentavam superprodução e queda de preço. Uma
- Após 1929, houve queda das receitas de exportação e dificuldade de importar manufaturas. N
- Uma fábrica nacional passa a produzir tecidos que eram comprados do exterior, mas continua
- Uma atividade industrial necessita de aço como insumo, e o Estado investe em siderurgia in
- Um plano de desenvolvimento previu ações em energia, transportes, alimentação, indústria d
- Na industrialização acelerada dos anos 1950, investimentos públicos em energia foram combi
- Em 1952, foi instituída uma organização para apoiar financiamentos de longo prazo destinad
- Um estudo conclui que a defesa da renda cafeeira, apesar da crise externa, sustentou consu
- Um programa de 1963 buscava conter a inflação sem abandonar crescimento e reformas fiscal,
- Uma revisão histórica afirma que o Banco Central substituiu estrutura monetária anterior, 
- Uma política de 1964 procura reduzir gradualmente a inflação e, ao mesmo tempo, reorganiza
- Entre 1968 e 1973, cresceram intensamente a produção de veículos, o consumo financiado e o
- Um documento referente a 1972–1974 propõe investimentos em transportes, comunicações e int
- Após a elevação do preço internacional do petróleo, um programa brasileiro buscou produzir
- Um pesquisador compara os efeitos de uma desvalorização cambial sobre dois agentes em 1905
- Uma empresa nacional substitui importações de eletrodomésticos; a escala de produção cresc
- O governo compra parte do café excedente durante uma queda de exportações; exportadores ma
- Ao comparar os cinco grupos do Plano de Metas com os investimentos industriais realizados,
- Um economista afirma que a correção monetária introduzida no contexto das reformas de 1964
- Em 1975, diante da vulnerabilidade energética externa, o país manteve grandes investimento
- Uma análise identifica crescimento expressivo da indústria de bens duráveis em 1971, acomp
- Considere a sequência: criação da Sumoc, fundação do BNDE, Plano de Metas, lançamento do P
- Uma companhia ferroviária construída para transportar café até o porto também reduz o cust
- Um professor explica que o Encilhamento foi uma política de compra e retenção de café inst
- Depois de 1929, uma empresa brasileira que fabricava alimentos industrializados encontrou 
- Em uma aula sobre o Plano de Metas, o estudante aponta Brasília como um dos cinco grupos s
- Um programa para 1963–1965, formulado por Celso Furtado ao fim de 1962, buscava compatibil
- Um quadro histórico atribui ao II PND, vigente em 1972–1974, a principal resposta industri
- Uma queda acentuada do preço externo do café pode reduzir divisas disponíveis e limitar im
- Uma fábrica expandiu a substituição de importações de tecidos nos anos 1930 e, por isso, a
- Uma expansão de montadoras apoiada por capital estrangeiro e infraestrutura estatal nos an
- Um texto atribui à Lei nº 4.380/1964 a criação simultânea do BNH, do CMN e do Banco Centra
- Ao estudar a década de 1960, uma pessoa identifica no PAEG um programa de combate gradual 
- Ao comparar os anos 1968–1973 com 1963, um analista diz que o milagre econômico foi a exec
- Uma comparação entre o I PND e o II PND conclui que o primeiro se associa à integração nac
- Um estudo de 1906 argumenta que a compra de excedentes cafeeiros reduzia de forma permanen
- Em uma comparação de interpretações sobre a crise de 1929, observa-se que a preservação de
- Na trajetória de industrialização, reduzir importações de bens finais assegura, por identi
- Na análise do Plano de Metas, a ampliação da capacidade nacional de montar veículos é comp
- Uma avaliação do PAEG afirma que a introdução de correção monetária e reorganização do sis
- Um relatório sobre o II PND observa que grandes investimentos em energia e insumos básicos
- Em 1980, a maior capacidade brasileira de produzir insumos industriais significava que o s
```

## Pedido 31: INTOSAI, ISSAI e independência das EFS (`controle-externo.intosai-issai`, 146 tópicos de edital)

```
Matéria: controle-externo.intosai-issai (INTOSAI, ISSAI e independência das EFS)
Capítulos e quantas questões quero em cada um (total 100):
- "1. INTOSAI, IFPP e princípios fundamentais da auditoria pública": 17
- "2. Declarações de Lima e do México: independência das EFS": 28
- "3. Transparência, accountability, ética e gestão da qualidade": 21
- "4. ISSAI 100, 200 e 400: auditoria financeira e de conformidade": 18
- "5. ISSAI 300 e 3000: auditoria operacional, evidência e relatórios": 16

Já existem (não repita):
- Uma EFS prepara manual e deseja distinguir princípios institucionais, padrões de auditoria
- Ao citar a Declaração de Lima como marco histórico das instituições de controle externo, u
- Um estudo aponta que a Declaração do México detalha condições para a independência institu
- A entidade auditada impede o acesso da EFS a documentos necessários para verificar a desti
- Uma EFS deseja criar regras públicas sobre seu mandato, métodos, relatórios, integridade e
- Um programa institucional busca mostrar como as auditorias fortalecem responsabilização, r
- Uma equipe identifica auditor, entidade responsável, usuários do relatório, objeto e crité
- Um auditor recebe oferta de vantagem da entidade fiscalizada para suavizar achados e busca
- A equipe foi encarregada de avaliar se demonstrações financeiras consolidadas apresentam d
- Uma auditoria investiga se programa público atende às metas e se utiliza adequadamente os 
- O dirigente de entidade auditada exige retirar um achado do relatório antes de autorizar s
- Uma EFS emite recomendações para corrigir falhas sistêmicas, mas não possui mecanismo para
- Após publicar crítica ao orçamento governamental, a EFS sofre bloqueio retaliatório de rec
- Uma EFS contrata consultoria para apoiar análise de dados e pretende deixar de observar tr
- Uma EFS solicita avaliação por especialistas externos sem vínculo com as entidades auditad
- Uma EFS quer reformar seu procedimento de revisão, adotando objetivos de qualidade, avalia
- Uma equipe examina contratos para verificar cumprimento de normas de contratação e condiçõ
- Em um trabalho, a entidade fiscalizada prepara informação de conformidade e o auditor aval
- Uma equipe verifica se determinado serviço foi prestado com menor gasto de recursos por at
- Ao planejar auditoria operacional, o auditor formula objetivos e perguntas, mas não define
- Em auditoria financeira, um gestor exige que o relatório assegure ausência de toda fraude,
- Uma auditoria de conformidade foi planejada para verificar normas de uso de recursos, mas 
- Uma equipe propõe como objetivo operacional apenas 'avaliar a qualidade geral da atuação p
- Um relatório operacional conclui que a queda no número de atendimentos foi causada pela tr
- Uma EFS afirma em manual que a adesão ao IFPP transfere diretamente à INTOSAI poder de imp
- Um manual profissional identifica INTOSAI-P como categoria de princípios, ISSAI como categ
- A Declaração de Lima, adotada em 1977, é atualmente identificada como ISSAI 10 e reúne os 
- O princípio 4 da Declaração do México exige que a EFS disponha de acesso irrestrito às inf
- O Código de Ética da INTOSAI está atualmente numerado como ISSAI 200 e disciplina cinco va
- Uma auditoria que examina a relação entre recursos empregados, serviços prestados e alcanc
- A adoção de consultoria externa para executar parte de uma auditoria afasta da EFS os prin
- A Declaração do México considera a existência de recursos financeiros, humanos e materiais
- Na auditoria do setor público, a ISSAI 100 classifica todo trabalho como atestação, porque
- Os princípios da ISSAI 400 são pertinentes a auditoria que verifica se transações e operaç
- A ISSAI 3000 é um guia de orientações sem requisitos de auditoria operacional, ao passo qu
- O princípio 6 da Declaração do México protege a liberdade da EFS para definir conteúdo, mo
- Pela INTOSAI-P 20, a EFS torna-se dispensada de relatar a economicidade e a eficiência de 
- Os doze princípios da INTOSAI-P 12 incluem tanto o fortalecimento da accountability das en
- Uma equipe que revela dados confidenciais sem fundamento, mas cumpre corretamente prazos e
- A ISSAI 140 revisada em 2024 compreende gestão da qualidade como sistema institucional con
- Uma conclusão de auditoria financeira, mesmo baseada em evidência suficiente e apropriada,
- Uma auditoria de conformidade pode adotar critérios derivados de leis, regulamentos e outr
- Na auditoria operacional, economicidade corresponde necessariamente ao alcance das metas f
- A ISSAI 3000 exige que objetivos de auditoria operacional se relacionem a economicidade, e
- Uma EFS que apresenta recomendações operacionais acompanhadas de dados quantitativos fica 
```

## Pedido 32: Jurisprudência dos Tribunais Superiores em Direito Penal (`direito-penal.jurisprudencia-tribunais-superiores`, 145 tópicos de edital)

```
Matéria: direito-penal.jurisprudencia-tribunais-superiores (Jurisprudência dos Tribunais Superiores em Direito Penal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Parte Geral I: Princípios Penais, Aplicação da Lei e Teoria do Crime": 20
- "2. Parte Geral II: Teoria da Pena, Dosimetria, Regime Inicial, Concurso e Prescrição": 20
- "3. Parte Especial I: Crimes contra a Pessoa, Patrimônio e Dignidade Sexual": 20
- "4. Parte Especial II: Crimes contra a Fé Pública e contra a Administração Pública": 20
- "5. Legislação Penal Especial e Como o Tema Costuma Ser Cobrado": 20

Já existem (não repita):
- De acordo com o entendimento sumulado do Superior Tribunal de Justiça acerca do princípio 
- Acerca da aplicação da lei penal no tempo aos crimes permanentes e aos crimes continuados,
- Sobre a dosimetria da pena e a fixação do regime inicial de cumprimento, assinale a altern
- No que tange à prescrição penal, segundo as súmulas do STF e do STJ, assinale a opção corr
- A respeito dos crimes contra o patrimônio na jurisprudência consolidada do Superior Tribun
- Sobre os crimes contra a dignidade sexual e o entendimento firmado pelo Superior Tribunal 
- Considerando a jurisprudência dos Tribunais Superiores sobre os crimes contra a fé pública
- A respeito da aplicação da Lei Maria da Penha (Lei nº 11.340/2006) segundo as súmulas do S
- De acordo com a jurisprudência consolidada do STF e do STJ, o princípio da insignificância
- Segundo entendimento sumulado do Superior Tribunal de Justiça, a reincidência penal pode s
- Conforme a jurisprudência sumulada do Superior Tribunal de Justiça, quando o crime de fals
- De acordo com a Súmula 511 do Superior Tribunal de Justiça, é possível o reconhecimento da
- Segundo entendimento sumulado do Superior Tribunal de Justiça, a competência para processa
- O tráfico de drogas na modalidade privilegiada, previsto no art. 33, § 4º, da Lei nº 11.34
- Nos termos da Súmula 600 do Superior Tribunal de Justiça, para a configuração da violência
- Determinado servidor público municipal subtraiu, para uso particular em sua residência, tr
- A respeito do entendimento do STF no Tema 506 de Repercussão Geral (RE 635.659) e da juris
- Em sentença condenatória pela prática de seis furtos simples cometidos nas mesmas condiçõe
- Mévia, reincidente, foi condenada pela prática de três crimes em continuidade delitiva. Pa
- Caio, primário e de bons antecedentes, ingressou durante a madrugada (repouso noturno), me
- Tício falsificou a assinatura em um único cheque encontrado na rua e utilizou a cártula ex
- Abordado em uma fiscalização rodoviária pela Polícia Rodoviária Federal, um motorista apre
- João, primário e de bons antecedentes, foi preso em flagrante no interior de um ônibus int
- No que concerne à jurisprudência do STJ sobre o Estatuto do Desarmamento (Lei nº 10.826/20
- Segundo a jurisprudência do Superior Tribunal de Justiça firmada em sede de recurso repeti
- De acordo com a jurisprudência do STJ, para que o réu faça jus à atenuante da confissão es
- Conforme a Súmula 415 do Superior Tribunal de Justiça, o período de suspensão do prazo pre
- No crime de roubo circunstanciado pelo emprego de arma branca e pelo concurso de duas pess
- De acordo com a Súmula 24 do Superior Tribunal de Justiça, aplica-se ao crime de esteliona
- O crime de descaminho (art. 334 do CP) possui natureza de crime formal, razão pela qual su
- Segundo a Súmula 536 do Superior Tribunal de Justiça, a suspensão condicional do processo 
- Analise as situações hipotéticas abaixo à luz da jurisprudência dominante do STF e do STJ 
- Roberto responde a ação penal por crime de roubo majorado. Ao proferir a sentença condenat
- Acerca do entendimento sumulado e repetitivo do STF e do STJ sobre regime inicial de cumpr
- Lucas, aproveitando-se da confiança depositada por seu patrão de longa data, que lhe entre
- Sobre os crimes de extorsão, roubo, estelionato e estupro de vulnerável na jurisprudência 
- A respeito dos crimes contra a Administração Pública e contra a fé pública na jurisprudênc
- Mário agrediu fisicamente sua ex-namorada, com quem nunca chegou a residir sob o mesmo tet
- A respeito do entendimento consolidado do STF e do STJ sobre os crimes previstos na Lei de
- Nos crimes contra o meio ambiente, a jurisprudência do STF e do STJ veda em caráter absolu
- Segundo a jurisprudência consolidada do STJ (Tema 585), quando concorrem na segunda fase d
- De acordo com a Súmula 191 do Superior Tribunal de Justiça, a decisão de pronúncia constit
- Segundo tese firmada pelo Superior Tribunal de Justiça em sede de recurso repetitivo (Tema
- Conforme tese firmada pelo Superior Tribunal de Justiça no Tema 1121 dos Recursos Repetiti
- Segundo a jurisprudência consolidada do STF e do STJ, no crime de moeda falsa (art. 289 do
- No crime de embriaguez ao volante (art. 306 do Código de Trânsito Brasileiro), a jurisprud
```

## Pedido 33: Educação de jovens e adultos e educação do campo (`educacao.eja-educacao-campo`, 145 tópicos de edital)

```
Matéria: educacao.eja-educacao-campo (Educação de jovens e adultos e educação do campo)
Capítulos e quantas questões quero em cada um (total 100):
- "EJA como direito: fundamentos, público e diretrizes vigentes": 17
- "Paulo Freire e a prática pedagógica da EJA": 17
- "Educação do campo: história, conceitos, sujeitos e legislação": 17
- "Educação escolar quilombola: território, memória e participação": 17
- "Pedagogia da alternância: tempos, instrumentos e avaliação": 16
- "Educação de jovens e adultos em situação de privação de liberdade": 16

Já existem (não repita):
- A EJA integra a educação básica como modalidade destinada a assegurar escolarização a quem
- As funções reparadora, equalizadora e qualificadora são tradicionalmente utilizadas para e
- Pela regra nacional vigente, o ingresso na EJA do ensino fundamental depende de idade míni
- No pensamento freiriano, a educação bancária atribui ao estudante a posição passiva de rec
- A educação do campo restringe-se, por definição legal, aos prédios escolares situados em p
- Na educação escolar quilombola, as escolas que recebem estudantes quilombolas fora das com
- A Resolução CNE/CEB nº 3/2025 revogou expressamente a Resolução CNE/CEB nº 1/2021 sobre di
- Em 2026, cursos de EJA do ensino fundamental podem ser integralmente ofertados como EaD, d
- A norma nacional de 2025 estabelece para os anos finais do ensino fundamental da EJA carga
- Para Paulo Freire, reconhecer a leitura do mundo dispensa o ensino sistemático da leitura 
- A adaptação do calendário escolar às fases do ciclo agrícola e às condições climáticas enc
- A Pedagogia da Alternância dispensa registros pedagógicos do Tempo Comunidade porque este 
- A Resolução CNE/CEB nº 2/2010 fixa diretrizes nacionais para educação de jovens e adultos 
- A regra de 2025 para a EJA dos anos iniciais do ensino fundamental estabelece carga mínima
- Na regulação nacional de 2025, a oferta de EJA por EaD é admitida exclusivamente no ensino
- A práxis freiriana é concebida como reflexão apartada de qualquer ação, pois a intervenção
- O Decreto nº 7.352/2010 inclui quilombolas e pescadores artesanais entre as populações do 
- A Resolução CNE/CEB nº 8/2012 determina que a educação quilombola suprima as referências à
- Na EJA organizada por alternância, Tempo Comunidade pode incluir pesquisa e práticas socia
- A matrícula de pessoa privada de liberdade na EJA implica, por si só, remição suficiente p
- Uma escola municipal pretende abrir uma turma para pessoas adultas que interromperam o ens
- Um professor inicia a alfabetização de adultos perguntando quais problemas de abasteciment
- Uma escola urbana atende quase exclusivamente famílias de pescadores artesanais e ribeirin
- Uma rede deseja organizar projeto pedagógico para estudantes de comunidades quilombolas ma
- Um programa organiza períodos de estudo na escola e na comunidade, com projetos previament
- Uma pessoa presa pede matrícula na EJA do estabelecimento penal. A equipe escolar deve ent
- Uma banca pergunta a idade mínima para inscrição em exames da EJA destinados à conclusão d
- Qual par expressa adequadamente a noção freiriana de práxis?
- Qual política nacional foi contemplada expressamente pelo Decreto nº 7.352/2010 ao lado da
- Uma escola estadual mantém texto de regimento de 2023 autorizando a EJA fundamental integr
- Na avaliação de uma turma heterogênea de EJA, o professor pretende reconhecer competências
- Na preparação de uma sequência de alfabetização, o professor investiga o vocabulário e as 
- Uma rede do campo pretende fechar uma escola e deslocar todos os alunos para município dis
- Uma professora afirma que a educação escolar quilombola consiste em reproduzir danças e co
- Durante o Tempo Comunidade, uma turma documenta a coleta de dados sobre qualidade da água.
- Uma pessoa privada de liberdade é transferida entre unidades prisionais durante o ano leti
- Qual alternativa sintetiza a relação adequada entre direito ao currículo comum e especific
- Uma questão contrapõe as cargas horárias mínimas nacionais da EJA geral e apresenta difere
- Uma candidata emancipada de dezessete anos pretende realizar exame de conclusão da EJA do 
- Em uma prova, a pedagogia freiriana é apresentada como técnica de escolha de um vocabulári
- Uma banca descreve agricultores familiares, trabalhadores assalariados rurais, ribeirinhos
- Em pesquisa de história oral com estudantes quilombolas, um professor pretende divulgar gr
- O coordenador quer computar como Tempo Comunidade qualquer dia em que o estudante trabalho
- O projeto de uma escola prisional condiciona a alfabetização exclusivamente à avaliação de
- No RS, uma questão pede leitura combinada da Resolução CEEd nº 376/2023 e das diretrizes n
```

## Pedido 34: Lei nº 4.657, de 4 de setembro de 1942: LINDB, arts. 1º a 6º (`direito.lei-4657-setembro-1942`, 144 tópicos de edital)

```
Matéria: direito.lei-4657-setembro-1942 (Lei nº 4.657, de 4 de setembro de 1942: LINDB, arts. 1º a 6º)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Vigência, vacatio legis e correções do texto: art. 1º": 19
- "2. Continuidade, revogação, coexistência e repristinação: art. 2º": 20
- "3. Obrigatoriedade e integração de lacunas: arts. 3º e 4º": 20
- "4. Aplicação orientada aos fins sociais e ao bem comum: art. 5º": 19
- "5. Eficácia da lei no tempo e situações protegidas: art. 6º": 22

Já existem (não repita):
- Uma lei ordinária é oficialmente publicada sem cláusula específica de entrada em vigor. Se
- Uma lei publicada oficialmente informa expressamente que entrará em vigor na data de sua p
- O texto de uma lei em vacância é republicado oficialmente para corrigir erro antes de sua 
- Uma lei já está em vigor quando se publica oficialmente correção de seu texto. Qual tratam
- Uma lei de vigência indeterminada está sendo aplicada há muitos anos sem disciplina poster
- Uma lei posterior afirma expressamente que determinados artigos de outra lei ficam revogad
- A lei B revoga expressamente a lei A. Anos depois, a lei C revoga a lei B e nada dispõe so
- Uma pessoa descumpre lei já vigente e alega que desconhecia a obrigação porque não costuma
- Ao decidir controvérsia, o juiz verifica verdadeira omissão da lei, sem disciplina textual
- Uma lei vigente contém expressão ambígua, e a solução exige compreender o objetivo de prot
- Em um conflito temporal, um contrato foi validamente concluído conforme a lei então vigent
- Uma pessoa completou, sob lei anterior, todos os requisitos de aquisição de um direito, cu
- Uma sentença foi proferida ontem e ainda cabe recurso legalmente previsto contra seu conte
- Uma lei brasileira foi publicada oficialmente e seu cumprimento em determinado Estado estr
- Uma lei nova contém disciplina específica complementar a regras gerais antigas; ambas pode
- Uma lei foi publicada no dia 1º de um mês de 30 dias com cláusula 'entra em vigor após dec
- Uma lei posterior não menciona revogação expressa, mas regula inteiramente a mesma matéria
- Uma lei publicada em 2026 prevê vantagem após cinco requisitos cumulativos. Uma pessoa com
- Uma regra penal superveniente é mais benéfica ao acusado por fato anterior; outra mudança 
- Um examinador reproduz suposta regra ainda vigente no §2º do art. 1º da LINDB, atribuindo-
- Lei A estabelece regra geral para uma atividade. Lei B posterior acrescenta dever especial
- Um conflito não é disciplinado de forma expressa, mas existe norma que regula situação jur
- Uma lei vigente tem texto com duas leituras possíveis. O julgador escolhe aquela que melho
- Uma pessoa preencheu todos os requisitos legais de direito antes da alteração normativa, m
- Em demanda já decidida por pronunciamento judicial definitivo, a nova lei altera a discipl
- Uma lei publicada oficialmente sem regra especial de vigência começa, em regra, a vigorar 
- Se o texto de uma lei for republicado para correção antes da entrada em vigor, conserva-se
- O art. 2º, §1º, admite que lei posterior revogue a anterior por declaração expressa, incom
- Uma pessoa pode deixar de cumprir lei já vigente quando demonstra que desconhecia a public
- Diante de omissão da lei, o art. 4º da LINDB indica analogia, costumes e princípios gerais
- A interpretação baseada nos fins sociais da lei e no bem comum somente é admitida pelo art
- Uma lei publicada oficialmente prevê prazo de vacância de 20 dias. Pela LC nº 95/1998, art
- Quando uma lei B revoga A e depois lei C revoga B sem prever restauração, a lei A volta au
- Uma norma posterior estabelece disciplina especial compatível com lei geral já existente, 
- A analogia e a interpretação extensiva são técnicas idênticas: em ambas, o intérprete obri
- A proteção ao direito adquirido do art. 6º, §2º, pode alcançar direito já incorporado ao p
- Qualquer sentença judicial proferida sob lei anterior caracteriza coisa julgada para o art
- Uma lei nova incide de imediato nas situações que lhe são submetidas após sua entrada em v
- Como a LINDB possui regra geral de efeito imediato, a Constituição impede a retroatividade
- Uma pessoa completou todos os requisitos de certo direito antes da vigência da lei modific
- A correção de texto de lei já vigente deve ser sempre tratada como simples ajuste administ
- Se o legislador revoga expressamente apenas três dispositivos de diploma antigo, sem alcan
- O art. 5º permite que o juiz desconsidere texto legal inequívoco e situação protegida como
- Uma lei nova torna-se vigente antes de determinada pessoa completar a condição que lhe dar
- Pela redação atualmente compilada da LINDB, o art. 1º, §2º, contém regra válida sobre a fo
```

## Pedido 35: Manutenção de microcomputadores, periféricos e nobreaks (`informatica.manutencao-microcomputadores`, 144 tópicos de edital)

```
Matéria: informatica.manutencao-microcomputadores (Manutenção de microcomputadores, periféricos e nobreaks)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Arquitetura do microcomputador, firmware e inicialização": 14
- "2. Manutenção preventiva, segurança e diagnóstico por software": 16
- "3. Alimentação elétrica, fontes chaveadas e nobreaks": 26
- "4. Monitores, impressoras, interfaces e computadores portáteis": 27
- "5. Manutenção corretiva avançada, soldagem e recuperação de dados": 17

Já existem (não repita):
- Um computador acende, apresenta a identificação do fabricante e, logo depois, informa que 
- Ao ampliar o armazenamento, uma pessoa percebe que a placa aceita módulos no formato M.2. 
- Durante a substituição de uma memória RAM em computador desligado, o profissional pretende
- Em uma sala com quedas ocasionais da rede elétrica, é necessário conservar o computador li
- Uma impressora laser monocromática apresenta aviso de falta de toner. Qual componente deve
- Um setor deseja reduzir desligamentos por aquecimento em seus computadores, que ainda func
- Dois notebooks têm portas USB-C visualmente idênticas, mas apenas um transmite imagem para
- Após a tela de fabricante desaparecer, o computador permanece na mensagem de erro do carre
- Uma estação apresenta travamentos aleatórios e suspeita de erros em RAM. Qual procedimento
- Um nobreak apresenta placa de identificação com limites de 1200 VA e 720 W. Deseja-se cone
- Ao ler documentação de duas gerações de computadores, o profissional encontra ponte norte/
- Uma unidade de armazenamento em uso informa alerta S.M.A.R.T. e erros de leitura, embora c
- Durante atendimento a um notebook, observa-se elevação da tampa inferior e deslocamento do
- Um ambiente precisa reduzir ao mínimo a interrupção de fornecimento de energia à carga dur
- Uma impressora imprime normalmente seu relatório interno, mas nenhum trabalho enviado pelo
- Em uma bancada há componentes retangulares soldados diretamente na superfície da placa e u
- Após apresentar funcionamento estável, um computador perdeu capacidade de inicializar após
- Uma placa com SSD M.2 SATA foi substituída por modelo M.2 PCIe/NVMe do mesmo comprimento, 
- Um disco magnético produz ruídos anormais, demora a responder e guarda a única cópia de da
- Depois de vários ciclos térmicos, um componente BGA apresenta comportamento intermitente. 
- Um nobreak on-line de dupla conversão sinaliza modo bypass e mantém o equipamento conectad
- Um notebook aceita energia por USB-C, mas não carrega com determinado adaptador, embora o 
- Uma estação desktop desliga abruptamente apenas quando CPU e placa gráfica trabalham junta
- Um notebook inicializa, reproduz sons normalmente e exibe a área de trabalho em monitor ex
- Após reparo de um computador que apresentava travamentos, o técnico observa um único boot 
- Ao encontrar dissipadores obstruídos por poeira em computadores ainda operacionais, a equi
- Um SSD com conector M.2 pode ser substituído por qualquer outro SSD M.2 de igual comprimen
- Um computador emite três bipes durante a partida. Sem consultar fabricante nem modelo, é t
- Antes de reinstalar o sistema em estação que guarda documentos, a equipe confere uma cópia
- Em impressora laser com cilindro de imagem e cartucho de toner separados, a substituição d
- Quando falta energia da concessionária, um nobreak off-line em operação comum mantém a car
- Um SSD recebe aviso de falha previsto por S.M.A.R.T., mas ainda passa em teste curto. É ap
- A execução de diagnóstico S.M.A.R.T. em unidade que não abre arquivos substitui a necessid
- Ao comparar duas gerações de placa-mãe, constata-se que parte das funções atribuídas à ant
- Durante a instalação de RAM, a ausência de faísca visível descarta risco de descarga eletr
- Ao surgir falha de injetores em impressora a jato de tinta, a primeira investigação deverá
- Um nobreak marcado 1500 VA e 900 W pode suportar carga de 850 W com potência aparente de 1
- O fato de dois notebooks possuírem a mesma porta USB-C não assegura iguais possibilidades 
- Em computador estável, a atualização mais recente de BIOS deve ser aplicada em toda manute
- Ao avaliar um circuito integrado com contatos em matriz sob o encapsulamento, o profission
- Um chip BGA apresenta falha interna confirmada. Substituir suas esferas de solda por rebal
- A criação de unidade USB de recuperação do Windows garante que todos os arquivos pessoais 
- Em nobreak de linha interativa, um estabilizador automático de tensão pode compensar varia
- Ao detectar bateria de notebook estufada, a equipe pode perfurá-la cuidadosamente para ali
- Se um nobreak on-line passa ao modo bypass, a carga pode permanecer alimentada por caminho
```

## Pedido 36: Estilística, vícios de linguagem e qualidades da expressão (`portugues.estilistica-vicios-linguagem`, 144 tópicos de edital)

```
Matéria: portugues.estilistica-vicios-linguagem (Estilística, vícios de linguagem e qualidades da expressão)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estilística, expressividade e adequação de registro": 41
- "2. Vícios lexicais, gramaticais e sonoros": 15
- "3. Redundância, prolixidade, clichês e concisão": 16
- "4. Ambiguidade, paralelismo e estrutura do período": 15
- "5. Revisão estilística aplicada e convenções da escrita": 13

Já existem (não repita):
- Um aviso técnico mantém linguagem formal até a frase final: 'A previsão de entrega está ad
- Em discurso de mobilização, a frase 'Queremos dados, queremos respostas, queremos soluções
- Uma mensagem entre amigos emprega 'Me manda o link quando puder?'. Não há exigência de reg
- Um relatório usa 'A equipe procedeu à efetivação da coleta de dados'. Pretende-se simplifi
- Uma narrativa situada no século XVIII descreve um personagem que 'recebeu uma notificação 
- Durante a revisão de um comunicado, surge a grafia 'excessão' no lugar de 'exceção'. Qual 
- No registro formal, alguém escreve 'Houveram várias reclamações naquela semana'. Qual expl
- Um texto afirma que certa medida 'retificou os prejuízos materiais', embora queira dizer q
- Ao ler uma frase em voz alta, um revisor percebe encontro de sons entre palavras que produ
- Um texto técnico dirigido a especialistas emprega um estrangeirismo de sentido preciso, ex
- Numa instrução neutra, lê-se: 'O participante deverá retornar de volta ao ponto inicial'. 
- Uma instrução estabelece 'O cadastro pode ser atualizado até sexta-feira'. Qual substituiç
- Uma equipe anuncia: 'Vamos pensar fora da caixa e agregar valor à experiência'. Qual alter
- Um comunicado afirma apenas que 'o resultado será divulgado em breve'. Um revisor quer mel
- No trecho 'Foi realizada a elaboração do documento pela equipe', qual alternativa melhora 
- No enunciado 'Marina avisou a Paula que seu relatório estava pronto', o objetivo é indicar
- Compare 'Apenas os inscritos receberão a senha' com 'Os inscritos receberão apenas a senha
- Uma lista apresenta as ações 'conferir os dados, revisão das cópias e que o resultado seja
- Em relatório, uma oração aparece isolada como período completo: 'Embora os resultados tenh
- Em 'O coordenador apresentou ao técnico a revisão do relatório na sala', a expressão 'na s
- Um período informa: 'Os participantes, entregaram os formulários até ontem'. Qual avaliaçã
- Uma instrução prevê: 'Se houver vaga, o participante poderá enviar o pedido até quinta-fei
- Um manual explica um conceito estatístico a especialistas. Um revisor recomenda substituir
- Um texto afirma: 'A assistente informou à supervisora que sua assinatura faltava no formul
- Uma nota deve informar com clareza que parte dos pedidos foi aceita e parte foi recusada. 
- Em uma crônica humorística, a alternância deliberada entre tom solene e expressão popular 
- Numa conversa informal, o emprego da forma reduzida 'pra' constitui necessariamente barbar
- Ao reescrever um aviso, trocar um termo técnico indispensável por uma palavra vaga aumenta
- Em um anúncio persuasivo, a repetição 'É simples, é rápido, é seguro' pode organizar o rit
- Na correção de uma frase formal, substituir 'Houveram alterações' por 'Houve alterações' r
- O emprego de 'excessão' em lugar de 'exceção' exemplifica solecismo, porque qualquer desvi
- Se uma sequência de palavras produz, na leitura em voz alta, aproximação sonora involuntar
- Em uma frase que usa 'retificar prejuízos' querendo dizer 'compensar prejuízos', o defeito
- Em mensagem técnica dirigida a público especializado, a origem estrangeira de um termo, ai
- Uma reescrita concisa de 'O participante deverá retornar de volta ao ponto inicial' é 'O p
- O pleonasmo deve ser considerado vicioso em todas as situações, inclusive quando a repetiç
- Um comunicado que troca 'vamos agregar valor à experiência' por 'o cadastro terá duas etap
- Na frase 'Lia observou a colega com o telescópio', a expressão final pode indicar instrume
- As frases 'Apenas os inscritos recebem o aviso' e 'Os inscritos recebem apenas o aviso' sã
- A sequência 'conferir a documentação, analisar os resultados e encaminhar a resposta' apre
- Em um relatório informativo, a oração isolada 'Embora os dados estejam completos.' constit
- A troca de 'O pedido poderá ser enviado até sexta-feira' por 'O pedido será enviado na sex
- Em 'Os interessados, receberam a resposta', a vírgula separa indevidamente o sujeito do pr
- Ao transformar 'Se o prazo for prorrogado, o documento poderá ser entregue depois' em 'O d
- Ao revisar um texto, a consulta a um vocabulário ortográfico pode esclarecer grafias, mas 
```

## Pedido 37: Zoneamento ambiental e proteção do patrimônio cultural (`direito-ambiental.zoneamento-patrimonio-cultural`, 143 tópicos de edital)

```
Matéria: direito-ambiental.zoneamento-patrimonio-cultural (Zoneamento ambiental e proteção do patrimônio cultural)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Zoneamento ambiental e Zoneamento Ecológico-Econômico": 25
- "2. Zoneamento urbano e zoneamento agrícola de risco climático": 21
- "3. Zoneamento costeiro e Plano Nacional de Gerenciamento Costeiro": 18
- "4. Tombamento e sítios arqueológicos e pré-históricos": 20
- "5. Poder de polícia ambiental e integração das proteções": 16

Já existem (não repita):
- Uma indústria solicita instalar unidade em região descrita por ZEE como ecologicamente frá
- Em planejamento de um território, qual ato normativo inclui expressamente o zoneamento amb
- Uma equipe municipal analisa a revisão do plano diretor e pergunta qual prazo mínimo de pe
- Um produtor rural consulta calendário por cultura e município para escolher a época de sem
- Um município litorâneo avalia a orientação de usos territoriais em zona costeira, respeita
- Durante escavação em lote particular foram encontrados vestígios cerâmicos de interesse ar
- Um proprietário recebeu notificação para tombamento compulsório federal e deseja apresenta
- Um proprietário de imóvel tombado pretende restaurar sua fachada. Qual restrição federal é
- Qual diploma define, no art. 78, o poder de polícia como atividade administrativa que limi
- Uma prefeitura prepara a atualização do plano diretor e propõe analisar somente os bairros
- Ao elaborar um ZEE, a equipe apresenta mapa final sem diagnóstico, cenários ou diretrizes.
- Um empreendimento urbano precisa de EIV por seu efeito sobre a paisagem e também se enquad
- Um município costeiro adota regra de proteção mais restritiva que diretriz geral do planej
- Em procedimento federal de tombamento, o proprietário recebeu notificação, mas a inscrição
- Um empreendimento turístico pretende construir muro que bloqueará o único acesso habitual 
- Uma empresa encontra vestígios arqueológicos em terreno de sua propriedade e sustenta que 
- A fiscalização ambiental identifica infração cometida por empreendimento com autorização a
- Uma proposta de ZEE local utiliza apenas mapa na escala nacional de apresentação 1:5.000.0
- Um projeto situado em faixa costeira suscita debate sobre a exigência de EIA/RIMA. Um agen
- O dono de um bem tombado não dispõe de recursos para obras de conservação e comunica o fat
- Uma empresa fará pesquisa e escavação arqueológica em terreno particular e obteve consenti
- Em propriedade rural existe sítio com reminiscências históricas de antigo quilombo. Alega-
- Um edifício novo, em terreno não tombado, reduzirá a visibilidade de imóvel tombado proteg
- Durante fiscalização conjunta, dois entes lavram autos sobre a mesma infração de atividade
- Uma equipe delimita a zona costeira exclusivamente pelo limite da faixa de areia e exclui 
- Uma autoridade adota o ZEE para orientar a instalação de empreendimento público, pois o ar
- Em revisão do plano diretor, o município dispensa debates e divulgação de documentos, pois
- Uma consulta ao ZARC identifica menor risco de perda por clima adverso para determinada cu
- Um empreendimento fecha o acesso à praia ao público e sustenta que a autorização municipal
- Ao receber notificação inicial de tombamento compulsório, o proprietário deve considerar q
- Ao encontrar peças arqueológicas em solo particular, o proprietário passa a ser automatica
- Na elaboração de ZEE regional, incluir dados sobre fragilidade do solo, corredores ecológi
- Um município solicita somente Estudo de Impacto de Vizinhança para empreendimento sujeito 
- Plano municipal de gerenciamento costeiro pode ser instituído observando normas do PNGC, e
- O Decreto-Lei nº 25/1937 permite que o proprietário realize pintura de bem tombado sem aut
- Na inspeção de empreendimento ambientalmente licenciado, outro ente federativo pode atuar 
- Para regular o uso de área costeira, a autoridade entende que o ZEEC elimina a aplicação d
- Ao examinar obra na vizinhança de bem tombado, a autoridade patrimonial pode controlar con
- No licenciamento costeiro em 2026, pode-se exigir EIA/RIMA de todo empreendimento exclusiv
- Em área reconhecida como antiga comunidade quilombola, documentos e sítios com reminiscênc
- O proprietário de terreno com jazida arqueológica pode remeter diretamente ao exterior qua
- Uma autoridade ambiental identifica iminência de degradação durante fiscalização de ativid
- A previsão do art. 78 do CTN permite exercer poder de polícia com desvio de finalidade des
- Uma obra em região costeira precisa considerar restrições urbanísticas e ambientais, bem c
- O Zoneamento Agrícola de Risco Climático autoriza, em qualquer área com período de plantio
```

## Pedido 38: Partes, procuradores, jus postulandi e honorários (`processo-trabalho.partes-procuradores`, 143 tópicos de edital)

```
Matéria: processo-trabalho.partes-procuradores (Partes, procuradores, jus postulandi e honorários)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Capacidade, legitimidade e representação em juízo": 16
- "2. Jus postulandi, procuração e atuação profissional": 22
- "3. Substituição processual, litisconsórcio e situações empresariais": 19
- "4. Assistência judiciária e gratuidade: regras e decisão de 2026": 22
- "5. Honorários, sucumbência e distinções práticas": 21

Já existem (não repita):
- Uma pessoa de 17 anos, com crédito salarial a reclamar, está sem representante legal dispo
- Em audiência trabalhista, a empresa envia uma pessoa conhecedora dos fatos que não integra
- Um empregado plenamente capaz apresenta pessoalmente sua reclamação a uma Vara do Trabalho
- Sem advogado, uma trabalhadora deseja interpor recurso de competência do Tribunal Superior
- Em audiência, o advogado pede verbalmente que conste sua constituição como procurador, com
- Uma entidade sindical ajuíza ação em nome próprio para proteger direitos individuais de in
- Uma trabalhadora pede assistência jurídica ao sindicato de sua categoria, mas não é associ
- Para reclamação ajuizada em outubro de 2026, a pessoa recebe R$ 4.000 mensais e requer gra
- Em ação trabalhista, o advogado busca honorários de sucumbência. A faixa percentual estabe
- Uma sociedade apresenta contestação assinada por pessoa sem poderes de representação, defe
- Quatro empregados da mesma empresa apresentam reclamações com identidade de matéria. O agr
- Ao analisar demanda relacionada a uma empresa cuja falência foi decretada, o magistrado pr
- Uma sociedade em recuperação judicial continua administrada por seus dirigentes, sem decis
- Em processo ajuizado em outubro de 2026, o reclamante recebe R$ 6.500 por mês e pede justi
- Uma sentença acolhe alguns pedidos autônomos e rejeita outros, caracterizando sucumbência 
- Um trabalhador conclui que receber patrocínio sindical lhe assegura automaticamente isençã
- Antes da realização de perícia, o juízo considera exigir que uma parte deposite integralme
- Em demanda envolvendo relação jurídica indivisível, o julgamento deve ter conteúdo uniform
- Uma ação trabalhista foi ajuizada em 10 de setembro de 2026. Ao apreciar a gratuidade, o j
- Uma beneficiária de gratuidade perdeu pedido autônomo e foi condenada a honorários sucumbe
- O juiz reconhece a falta de representante adequado para pessoa incapaz, e os interesses do
- Sindicato presta assistência prevista na Lei 5.584/1970 e um profissional requer que honor
- Em processo ajuizado em outubro de 2026, a parte ganha R$ 4.800 mensais, mas possui patrim
- Uma sentença em ação trabalhista fixa honorários sobre o proveito econômico de R$ 60.000 e
- Empregado ajuíza ação coletiva por direitos da categoria por meio do sindicato, e uma bene
- Uma pessoa maior e capaz pode ser parte de reclamação trabalhista, mas sua capacidade proc
- Uma trabalhadora de 16 anos pode ajuizar pessoalmente demanda porque o art. 792 da CLT per
- Se um empregado capaz apresenta reclamação pessoal perante a Vara, o art. 791 da CLT ofere
- Uma empresa pode comparecer por preposto conhecedor dos fatos apenas quando provar que ess
- Um sindicato que ajuíza ação em nome próprio para a proteção de direitos de integrantes de
- Um trabalhador não associado ao sindicato de sua categoria não pode receber a assistência 
- Uma ação ajuizada em outubro de 2026 por pessoa que recebe R$ 4.500 mensais está abrangida
- A ADC 80 fixou que quem recebe mais de R$ 5 mil mensais jamais pode alcançar gratuidade de
- Uma sociedade com falência decretada passa a ter a massa representada em juízo pelo admini
- Em julgamento parcialmente favorável a ambas as posições, o art. 791-A, § 3º, da CLT autor
- O litisconsórcio necessário do CPC, art. 114, depende de imposição legal ou da natureza da
- O recebimento de crédito judicial por beneficiário da gratuidade autoriza, por si só, desc
- Conforme o art. 791-A, § 5º, da CLT, também podem ser devidos honorários advocatícios de s
- A decisão da ADC 80 incide, por sua modulação, sobre toda reclamação ainda não julgada em 
- O CPC, art. 72, I, prevê a nomeação de curador especial quando o incapaz não tiver represe
- A revogação do art. 16 da Lei 5.584/1970 pela Lei 13.725/2018 também eliminou o direito de
- Se forem fixados honorários de sucumbência a cargo do beneficiário da gratuidade, o credor
- Como a ADC 80 afastou o percentual de 40% do teto previdenciário do art. 790, § 3º, o julg
- O processamento de demanda trabalhista para apuração de crédito de empregado pode permanec
- Honorários contratuais ajustados entre cliente e advogado são a mesma verba sucumbencial d
```

## Pedido 39: Análise multivariada e de dados categorizados (`estatistica.analise-multivariada-categorica`, 143 tópicos de edital)

```
Matéria: estatistica.analise-multivariada-categorica (Análise multivariada e de dados categorizados)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Variáveis categóricas, distribuições e tabelas de contingência": 16
- "2. Qui-quadrado, Fisher e medidas de associação": 16
- "3. Modelos categóricos e análise de correspondência": 16
- "4. Análise multivariada: covariância, PCA e fatores": 26
- "5. Agrupamento, MANOVA e seleção dos métodos": 26

Já existem (não repita):
- Uma tabela reúne 40 pessoas do grupo A, das quais 30 tiveram resposta positiva. Qual é o p
- Em tabela de 100 pessoas, a primeira linha soma 50 e a coluna de respostas favoráveis soma
- Um estudo cruza duas classificações em tabela com duas linhas e três colunas. Quantos grau
- Com qui-quadrado calculado igual a 6,25 e valor crítico de 3,84 para um grau de liberdade 
- Em uma tabela binária, o evento ocorreu em 30 de 40 expostos e em 20 de 40 não expostos. Q
- Um relatório apresenta p-valor pequeno para uma tabela e deseja medir a intensidade da ass
- Uma tabela 2 por 2 tem poucos registros e frequências esperadas muito baixas. Qual procedi
- Uma base tem dez medidas contínuas fortemente correlacionadas e o objetivo é criar poucas 
- Uma pesquisa registra transporte, horário e preferência, todos em categorias nominais, e d
- Em estudo de adesão sim/não, deseja-se ajustar a probabilidade de adesão por idade e exper
- Dois tratamentos parecem ter desempenho diferente no agregado, mas a direção da comparação
- No grupo exposto ocorreram 20 eventos entre 100 indivíduos; no não exposto, 10 entre 100. 
- Uma PCA é aplicada a alturas em centímetros e rendas em milhares de unidades monetárias. Q
- Um conjunto de contagens é organizado por três classificações e pretende-se modelar associ
- Uma base contém grupos previamente classificados em duas categorias e vários preditores co
- Em um conjunto com três medidas contínuas, não existem classes conhecidas, mas procura-se 
- Um indicador multivariado deve medir afastamento de observação em relação a um centro, con
- Quatro variáveis padronizadas apresentam autovalores 2,4; 1,0; 0,4; 0,2 na PCA pela matriz
- Em regressão logística sem interação, um preditor apresenta coeficiente estimado aproximad
- Uma pesquisa encontra associação marginal entre atividade e resultado, mas identifica inde
- Duas modalidades de treinamento são comparadas ao mesmo tempo por tempo médio e precisão q
- Em uma tabela 2 por 2 com margens fixas e amostra muito pequena, qual caracterização estat
- Uma célula tem observado igual a 14 e esperado igual a 10 sob independência. Qual contribu
- Duas classes conhecidas apresentam dispersões distintas nos preditores numéricos. Deseja-s
- Uma base apresenta renda numérica, idade, tipo de atendimento nominal e satisfação ordinal
- Em um questionário, as opções ônibus, bicicleta e carro são identificadas pelos códigos 1,
- Em tabela com 30 respostas positivas no grupo A, 40 pessoas no grupo A e 60 positivas no c
- Para o teste de independência qui-quadrado usual em uma tabela com duas linhas e duas colu
- Quando o evento ocorre em 30% dos expostos e 10% dos não expostos, tanto o risco relativo 
- Uma tabela de independência pode apresentar frequência esperada não inteira, pois a expect
- Uma PCA construída apenas com medidas contínuas descobre automaticamente as classes verdad
- Se a resposta é aprovação sim ou não e há vários preditores explicativos, a regressão logí
- Uma pesquisa com transporte nominal, horário categorizado e modalidade nominal usa ACM; co
- Em uma tabela de contagens, acrescentar interação entre fatores a um modelo log-linear pod
- Um estudo deseja identificar construtos latentes responsáveis por covariância entre indica
- Um estudo utiliza distância de Mahalanobis com matriz de covariâncias invertível, levando 
- Para usar o teste exato de Fisher em uma tabela 2 por 2, é condição necessária que cada fr
- Uma comparação de resultados de duas intervenções exibe aparente vantagem de uma delas den
- Uma análise com k-means utiliza obrigatoriamente classes-alvo previamente conhecidas para 
- Dois grupos são comparados simultaneamente pelas médias de três medidas quantitativas corr
- Uma PCA baseada em matriz de correlações válida e de variáveis não constantes pode produzi
- Num modelo logístico binário sem interação da covariável em análise, o coeficiente corresp
- Se o total de uma linha é 40 e a tabela possui 100 observações, a frequência esperada para
- Para classificar observações em grupos conhecidos com distribuições normais e covariâncias
- Em um estudo observacional, a rejeição da hipótese nula de independência pelo qui-quadrado
```

## Pedido 40: Medicina veterinária: zoonoses, clínica, produção animal e ética profissional (`medicina-veterinaria.clinica-zoonoses-producao`, 142 tópicos de edital)

```
Matéria: medicina-veterinaria.clinica-zoonoses-producao (Medicina veterinária: zoonoses, clínica, produção animal e ética profissional)
Capítulos e quantas questões quero em cada um (total 100):
- "Zoonoses e vigilância em saúde pública": 17
- "Microbiologia, parasitologia e diagnóstico veterinário": 17
- "Clínica médica de pequenos e grandes animais": 17
- "Produção de ruminantes, suínos e aves": 17
- "Medicamentos veterinários: uso racional e fiscalização": 16
- "Bem-estar animal e ética médico-veterinária": 16

Já existem (não repita):
- Na investigação de um caso humano de leishmaniose visceral em área urbana, qual elo é cent
- Um profissional de saúde avalia pessoa mordida por mamífero potencialmente infectado por r
- Após enchente, trabalhadores entram em contato com lama contaminada por urina de roedores.
- Um rebanho leiteiro apresenta aumento de abortamentos e manipulação de placentas sem prote
- Quanto à toxoplasmose, assinale a explicação epidemiológica correta.
- Ao receber gato com nódulos ulcerados e histórico de arranhaduras em tutor com lesões cutâ
- Um laboratório isola microrganismo álcool-ácido resistente de bovino com emagrecimento crô
- Sobre resultado negativo de cultura bacteriana obtida após início de antibiótico, a interp
- Qual opção define adequadamente um antibiograma na prática veterinária?
- Na parasitologia, a infecção por *Dipylidium caninum* relaciona-se tipicamente a qual elo 
- Gato macho tenta urinar repetidamente, vocaliza e não elimina urina. Qual prioridade clíni
- Em cadela não castrada, febre, apatia e ultrassonografia com distensão uterina sem corrime
- Um equino apresenta cólica intensa, frequência cardíaca elevada e dor persistente apesar d
- Na avaliação de mastite subclínica de vacas leiteiras, qual abordagem é mais útil?
- Um bovino apresenta timpanismo ruminal significativo. Qual apresentação é mais característ
- Na bovinocultura de corte, a fase destinada principalmente ao crescimento após o desmame, 
- Em leitões recém-nascidos, qual conjunto de práticas tem maior relação com redução de perd
- Em uma granja de frangos, ventilação insuficiente, cama úmida e odor de amônia sugerem:
- Um medicamento aplicado a vaca em lactação possui período de carência para leite. Isso sig
- Ao ocorrer suspeita de reação adversa após fármaco veterinário, a conduta tecnicamente cor
- Sobre o conceito de bem-estar animal, qual afirmação melhor o representa?
- Um estabelecimento apresenta médico-veterinário apenas no cadastro, sem supervisão real de
- A vacinação antirrábica animal é uma medida preventiva, mas não elimina a necessidade de a
- Na leishmaniose visceral, basta que uma pessoa acaricie um cão infectado para que ocorra a
- Felinos são os hospedeiros definitivos de Toxoplasma gondii, mas a prevenção humana também
- A esporotricose felina é transmitida aos seres humanos exclusivamente pela ingestão de lei
- Uma sorologia reagente equivale, em todos os contextos e doenças, à demonstração de micror
- O exame coproparasitológico pode apresentar sensibilidade variável, porque a eliminação de
- Bactérias do gênero Brucella são classificadas como vírus neurotrópicos, motivo pelo qual 
- O resultado do antibiograma deve ser integrado à espécie animal, à distribuição do medicam
- O risco de alterações eletrolíticas potencialmente graves justifica considerar obstrução u
- Em cadelas com piometra, a inexistência de corrimento vaginal é suficiente para afastar a 
- A mastite subclínica pode estar presente em vacas cujo leite não apresenta alteração macro
- A expressão cólica equina designa etiologia única e benigna, dispensando avaliação de freq
- No gado de corte, a fase de cria está ligada à produção e ao desmame de bezerros, enquanto
- Na suinocultura, a biosseguridade é equivalente à administração de vacinas, independenteme
- Na avicultura, a avaliação de conforto térmico e ventilação pode afetar simultaneamente be
- Se um medicamento veterinário possui registro regular, sua aplicação em qualquer espécie e
- O período de carência pode variar conforme a apresentação, espécie, via e produto alimenta
- Evento adverso observado após uso de medicamento estabelece, por si só, prova definitiva d
- As cinco liberdades de bem-estar incluem oportunidade de expressar comportamento natural, 
- As Unidades de Vigilância de Zoonoses têm como atribuição ordinária realizar atendimento c
```

## Pedido 41: Proteção da fauna, manejo do fogo e legislação ambiental especial (`direito-ambiental.fauna-fogo-legislacao-especial`, 141 tópicos de edital)

```
Matéria: direito-ambiental.fauna-fogo-legislacao-especial (Proteção da fauna, manejo do fogo e legislação ambiental especial)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos biológicos, taxonomia e tutela constitucional dos animais": 19
- "2. Lei 5.197/1967 e proteção penal contra agressões à fauna": 22
- "3. Lei 14.944/2024: Política Nacional de Manejo Integrado do Fogo": 20
- "4. Lei 12.305/2010 e Decreto 10.936/2022: resíduos sólidos": 21
- "5. Código de Mineração, Código de Águas Minerais e integração dos regimes": 18

Já existem (não repita):
- Em levantamento de fauna, uma espécie ocorre naturalmente em várias regiões de um continen
- Um estabelecimento mantém animal doméstico em condições cruéis, argumentando que a espécie
- Uma pessoa destrói ninhos naturais de aves silvestres, sem capturar nenhum exemplar adulto
- Uma pessoa pratica maus-tratos contra um cão. Considerado o tipo específico introduzido em
- Uma equipe planeja fogo monitorado para fins de conservação de determinada vegetação, com 
- Agricultores familiares organizam conjuntamente uma queima em duas pequenas propriedades r
- Um plano de resíduos escolhe entre reciclar material recuperável e enviá-lo como rejeito a
- Após recuperação e tratamentos tecnológica e economicamente viáveis, sobra material sem op
- Uma empresa fabrica pneus e deve organizar retorno após uso, independentemente da coleta u
- Uma pessoa compra imóvel rural e identifica jazida no subsolo, concluindo que a escritura 
- Durante fiscalização, verifica-se perseguição não autorizada de exemplar silvestre nativo.
- Comunidade tradicional pratica uso do fogo vinculado a sua cultura e subsistência, sob a h
- Uma empresa privada elabora plano de manejo integrado do fogo para sua propriedade, inclui
- Um município implanta coleta seletiva separando resíduos secos dos orgânicos, mas mistura 
- Uma fonte captada possui composição normal e satisfaz requisitos de potabilidade da região
- Ao compor o Comitê Nacional de Manejo Integrado do Fogo, a proposta reserva 20% das vagas 
- Uma empresa oferece incineração de resíduos industriais alegando ser queima prescrita pela
- Uma companhia inicia logística reversa de aparelhos eletroeletrônicos e pretende exigir qu
- Um requerente pretende avaliar uma jazida por levantamentos geológicos, sondagens e ensaio
- Uma equipe revisa o art. 32 da Lei 9.605/1998 sobre maus-tratos a animais. A vítima é um g
- Dois planos de manejo integrado do fogo são elaborados: um por entidade pública que admini
- Em região rural, um produtor pretende queima agrossilvipastoril e uma comunidade pretende 
- Uma empresa sustenta que, por haver responsabilidade compartilhada do consumidor, a devolu
- Uma autarquia pretende extrair substância de emprego imediato na construção civil para obr
- Em empreendimento de exploração mineral, são identificados ninho silvestre, resíduo indust
- Uma classificação científica distingue espécies nativas, endêmicas e exóticas; uma espécie
- Segundo a Lei 5.197/1967, os ninhos e criadouros naturais de fauna silvestre deixam de rec
- Uma equipe programa queima para fins de pesquisa ou conservação com metas previstas em pla
- A Lei 12.305/2010 determina que resíduos recicláveis sejam dispostos em aterro antes de se
- Conforme os arts. 20, IX, e 176 da Constituição, jazidas e recursos minerais pertencem à U
- Na Lei 9.605/1998, a pena especial de reclusão de dois a cinco anos para maus-tratos a cãe
- Segundo a Lei 14.944/2024, a composição do Comitê Nacional de Manejo Integrado do Fogo dev
- Um plano de manejo integrado do fogo elaborado por empresa privada fica dispensado de apro
- Pelo Decreto 10.936/2022, os sistemas de logística reversa podem ser implementados por aco
- O Código de Águas Minerais considera água de composição normal e apenas potável como neces
- Um animal silvestre de espécie não ameaçada é mantido irregularmente em residência. O art.
- O art. 225, § 7º, da Constituição libera qualquer prática desportiva com animais da vedaçã
- Na hipótese específica do art. 33 da Lei 14.944/2024, o uso tradicional e adaptativo do fo
- A autorização da Lei 14.944/2024 para queimas controladas rurais também permite empregar f
- O art. 48 da Lei 14.944/2024 exclui expressamente do seu campo de aplicação a queima de re
- A responsabilidade compartilhada da PNRS implica que o consumidor, após utilizar uma bater
- O art. 58 do Decreto 10.936/2022 prevê o fornecimento anual, por meio eletrônico e conform
- O art. 2º do Código de Mineração reduz todos os regimes de aproveitamento mineral a autori
- O art. 4º do Código de Águas Minerais contempla aproveitamento comercial de fontes situada
- Em um imóvel com jazida e animais silvestres, o título autorizativo mineral afasta a aplic
```

## Pedido 42: Geriatria e gerontologia (`medicina.geriatria-gerontologia`, 141 tópicos de edital)

```
Matéria: medicina.geriatria-gerontologia (Geriatria e gerontologia)
Capítulos e quantas questões quero em cada um (total 100):
- "Envelhecimento: bases biológicas, epidemiologia e distinção entre senescência e doença": 17
- "Avaliação geriátrica ampla, funcionalidade e plano individual de cuidados": 17
- "Grandes síndromes geriátricas: quedas, continência, imobilidade e iatrogenia": 17
- "Cognição, delirium, demências e saúde mental na velhice": 17
- "Polifarmácia, farmacologia, fragilidade, sarcopenia e nutrição": 16
- "Cuidados paliativos, direitos e políticas de saúde da pessoa idosa": 16

Já existem (não repita):
- Em uma prova de geriatria, qual afirmação diferencia corretamente senescência e senilidade
- Qual instrumento é classicamente utilizado para avaliar atividades instrumentais da vida d
- O que representa a estratégia mais adequada para prevenir quedas recorrentes em uma pessoa
- Qual apresentação aponta mais fortemente para delirium em comparação com demência de evolu
- No paciente geriátrico, qual é a função dos critérios de Beers?
- O Estatuto da Pessoa Idosa aplica-se a pessoas a partir de qual idade?
- Qual achado NÃO deve ser interpretado automaticamente como alteração fisiológica inevitáve
- Uma pessoa idosa necessita de ajuda para banho, mas decide de forma esclarecida sobre sua 
- Na avaliação de um paciente de 77 anos, qual associação instrumento–domínio está correta?
- Paciente com urgência miccional súbita perde urina antes de alcançar o banheiro, mesmo and
- Uma idosa hospitalizada, com doença de Alzheimer prévia, apresenta sonolência e desatenção
- A equipe identifica um comprimido iniciado para tratar tontura surgida após aumento de ant
- Em relação ao fenótipo de fragilidade de Fried, assinale o conjunto de achados apropriado.
- Em serviço de saúde, como deve ser aplicada a prioridade especial das pessoas maiores de 8
- Após queda no domicílio sem fratura, homem de 79 anos relata tontura ao levantar. Qual eta
- Qual formulação melhor descreve cuidados paliativos na pessoa idosa?
- Homem de 86 anos com sarcopenia tem creatinina sérica aparentemente baixa. Qual interpreta
- Uma paciente nunca administrou as contas domésticas por divisão de tarefas, mas realiza co
- Ao aplicar o CAM em idoso após cirurgia, qual combinação sustenta o padrão de triagem posi
- Uma pessoa de 81 anos usa benzodiazepínico há anos, apresenta quedas e deseja reduzir a me
- Idosa dependente apresenta perdas urinárias após início de delirium e dificuldade de chega
- Idosa consciente de 84 anos compreende benefícios e riscos de intervenção e a recusa. Qual
- Qual combinação diferencia melhor sarcopenia de fragilidade em avaliação geriátrica?
- Um plano territorial de saúde da pessoa idosa orientado pela PNSPI deve priorizar qual est
- A idade cronológica é suficiente para determinar o grau de funcionalidade de qualquer pess
- O índice de Katz é utilizado para avaliar atividades básicas da vida diária, como banho e 
- Quedas recorrentes em idosos devem ser investigadas, mesmo quando não causam fraturas apar
- O delirium só pode ser diagnosticado quando há agitação psicomotora intensa.
- Polifarmácia é necessariamente sinônimo de prescrição inadequada em toda pessoa idosa.
- O Estatuto da Pessoa Idosa assegura proteção às pessoas com 60 anos ou mais.
- Cuidados paliativos devem excluir qualquer terapia dirigida à doença de base.
- A autonomia de uma pessoa idosa pode estar preservada mesmo quando ela necessita de ajuda 
- A incontinência urinária funcional pode ocorrer quando o paciente não consegue chegar ao b
- O surgimento súbito de desatenção em pessoa com demência conhecida dispensa investigação, 
- Critérios STOPP/START podem auxiliar tanto na identificação de prescrições inadequadas qua
- Em pessoa idosa com pouca massa muscular, creatinina sérica aparentemente normal pode coex
- SARC-F, quando positivo, estabelece isoladamente o diagnóstico definitivo de sarcopenia e 
- O IVCF-20 pode contribuir para identificar vulnerabilidade clínico-funcional, mas não subs
- Um rastreio cognitivo abaixo do esperado é, por si só, suficiente para confirmar síndrome 
- Repouso prolongado e contenção física constituem as principais medidas preventivas de qued
- Desprescrição é uma retirada planejada e monitorada de tratamento, podendo exigir redução 
- A presença de fragilidade prova, necessariamente, que a pessoa está dependente para todas 
- Uma pessoa idosa de 85 anos que compreende adequadamente determinada decisão terapêutica p
- A prioridade especial dos maiores de 80 anos permite ultrapassar qualquer paciente em emer
- Na interpretação de uma escala de AIVD, a não realização histórica de uma atividade por ra
```

## Pedido 43: Biblioteconomia: desenvolvimento de coleções e gestão de unidades de informação (`biblioteconomia.colecoes-gestao-unidades-informacao`, 140 tópicos de edital)

```
Matéria: biblioteconomia.colecoes-gestao-unidades-informacao (Biblioteconomia: desenvolvimento de coleções e gestão de unidades de informação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e política de desenvolvimento de coleções": 15
- "2. Avaliação de coleções, Conspectus, desbaste e intercâmbio": 17
- "3. Aplicação institucional da política e da seleção ao acervo especializado": 16
- "4. Organização, planejamento, inventário e automação de unidades": 32
- "5. Serviço de referência, estudos de usuários, marketing e avaliação": 20

Já existem (não repita):
- Uma biblioteca decidiu que um novo manual especializado atende à sua missão. O setor ainda
- Após mudanças no perfil da comunidade, uma unidade quer estabelecer critérios estáveis par
- Uma editora oferece gratuitamente vinte exemplares de uma obra alheia ao perfil de uma bib
- No diagnóstico Conspectus, um setor tem hoje cobertura de nível 2 e pretende atingir nível
- Em uma proposta de compra, a equipe destaca formação científica e experiência da autora. O
- Por baixa consulta, um exemplar é transferido da estante aberta a um depósito e permanece 
- Uma biblioteca propõe retirar definitivamente exemplares duplicados, sem demanda, após exa
- Duas bibliotecas concordaram em trocar conjuntos de publicações que lhes interessam, alter
- Uma coleção tem manuais introdutórios, mas não literatura especializada suficiente para su
- Uma obra de memória local circulou apenas uma vez em cinco anos. A equipe cogita seu desca
- Uma equipe consulta sugestões de leitores e resenhas para formar lista de possíveis aquisi
- Ao avaliar uma base eletrônica, a unidade analisa se o suporte será acessível ao público v
- Uma unidade possui muitas obras sobre conceitos gerais, mas nenhuma sobre uma aplicação re
- A comissão recebe proposta para aumentar acervo de modo racional e equilibrado, priorizand
- Foi lançada nova edição de manual já disponível. A equipe percebe mudança de capa e pagina
- O catálogo informa 420 exemplares disponíveis, mas a conferência nas estantes localiza 414
- Uma coleção possui um título publicado em três exemplares idênticos. Para uma estatística 
- Uma rede estabeleceu como direção institucional expandir acesso em quatro anos; a coordena
- Na migração para novo sistema, todos os títulos são importados, mas desaparecem vínculos c
- Uma unidade divulga serviço para universitários atuais, porém a pesquisa mostra muitos mor
- Uma pessoa solicita um título antigo, mas revela precisar de orientações atuais e verificá
- No planejamento de uma unidade pública, os dados de circulação são altos, mas entrevistas 
- A unidade respondeu 180 solicitações de 200 recebidas em um mês. Para avaliar a taxa de at
- Uma biblioteca lançou campanha para promover serviço de referência. Houve mais visitas, po
- Uma biblioteca especializada tem duas edições equivalentes de manual, baixo uso de uma cóp
- Após conhecer as necessidades de seus usuários, uma unidade seleciona um livro e só depois
- Ao receber uma doação de livros sem custos de compra, a biblioteca pode incorporá-los sem 
- Ao mapear uma coleção por áreas temáticas, a equipe pode usar Conspectus para registrar su
- Ao atribuir nível 5 de Conspectus à coleção de história regional, a equipe certifica que c
- Em uma biblioteca, o deslocamento controlado de obra pouco usada da estante pública para u
- Uma biblioteca que retirou temporariamente duplicatas da estante para conferência já concl
- Ao atender alguém que pediu uma obra sem saber indicar seu assunto com precisão, o serviço
- Uma biblioteca elaborou manual de empréstimos e devoluções; por isso, tornou desnecessária
- Na aplicação da IN STJ/GP nº 4/2017, a equipe pode usar catálogos de editoras, listas de i
- Pela IN STJ/GP nº 4/2017, a cobertura de novas abordagens temáticas é critério intrínseco 
- Diante de reimpressão recente de manual já adquirido, uma biblioteca pode comparar o conte
- Após localizar fisicamente todos os itens que constam do catálogo, a biblioteca pode concl
- Em projeto de automação bibliotecária, a validação da migração deve verificar não apenas r
- Uma biblioteca aplica os critérios de seleção da IN STJ/GP nº 4/2017 como se fossem obriga
- Antes de descartar exemplar de obra rara sem empréstimos recentes, a equipe deve ponderar 
- Quando a biblioteca A recebe por empréstimo temporário um livro da biblioteca B e o devolv
- Em análise de dois softwares, a presença de módulos de catalogação e circulação não elimin
- Uma biblioteca que recebe poucas consultas sobre assunto especializado pode concluir que a
- Depois de divulgar novo atendimento, a biblioteca deve acompanhar procura, qualidade das r
- No período em que respondeu 180 das 200 solicitações recebidas, a unidade comprovou taxa d
```

## Pedido 44: Ensino de língua portuguesa e literatura (`educacao.ensino-lingua-portuguesa-literatura`, 139 tópicos de edital)

```
Matéria: educacao.ensino-lingua-portuguesa-literatura (Ensino de língua portuguesa e literatura)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Concepções de linguagem, sujeito, texto e gramática": 19
- "2. Leitura, escuta, compreensão e fluência leitora": 22
- "3. Produção textual, operadores discursivos e oralidade": 19
- "4. Semiótica, multimodalidade, multiletramentos e letramento digital": 22
- "5. Ensino de literatura, planejamento e avaliação das práticas de linguagem": 18

Já existem (não repita):
- Uma turma analisa quem assina um aviso, a quem ele se dirige e por que escolhe determinada
- Uma reportagem apresenta um relato de acontecimentos e uma explicação de causas. Como clas
- Após ler um conto, um estudante afirma que a personagem estava com medo e aponta gestos e 
- Ao preparar uma carta de solicitação, uma estudante reorganiza argumentos após perceber qu
- Uma atividade pede leitura em voz alta de um poema escrito, enquanto outra organiza entrev
- Um estudante reconhece quase todas as palavras, mas lê sem pausas expressivas e pouco comp
- Em um infográfico, cores, ícones, números e legendas funcionam conjuntamente para comunica
- Uma turma estuda um romance apenas por datas de publicação e biografias, sem ler trechos o
- Em um artigo, lê-se: 'Os dados indicam melhora; porém, a desigualdade entre regiões persis
- Um estudante usa traços de sua variedade regional em conversa e precisa aprender convençõe
- Antes de ler uma reportagem, a turma prevê o tema pelo título; durante a leitura, encontra
- Ao planejar um abaixo-assinado, estudantes definem destinatário, pedido, justificativas e 
- Em um debate registrado em áudio, uma pausa antes da resposta e um tom irônico alteram o s
- Uma turma recebe mensagem viral acompanhada de imagem e link. Antes de republicar, identif
- Uma aluna escreve 'A equipe divulgou os resultados. Os pesquisadores explicaram os dados',
- Após uma primeira versão de resenha, o retorno indica que há resumo do enredo, mas faltam 
- Uma turma compara um conto original a uma adaptação em quadrinhos. Na adaptação, o narrado
- Em uma atividade com anúncios, a turma identifica o pronome 'você', a fotografia de uma fa
- Uma professora propõe leitura guiada repetida a estudantes que reconhecem palavras com esf
- Uma produção coletiva apresenta as frases 'A pesquisa foi revisada. Portanto, surgiram dúv
- Um projeto pede criar narrativas em diferentes plataformas, comparar repertórios culturais
- Em sequência de resenhas, a primeira produção revelou que estudantes confundiam recontar a
- Uma turma lê comentários online em que a mesma frase aparece como elogio numa comunidade e
- Para avaliar um debate sobre uso de espaços públicos, uma rubrica observa quantidade de fa
- Um planejamento integra leitura de texto literário, discussão de imagens, criação de podca
- Ao comparar dois bilhetes com igual conteúdo informativo, a turma observa que a relação en
- Ao identificar uma passagem narrativa dentro de uma notícia, o estudante deve concluir que
- Na avaliação de leitura em voz alta, precisão e entonação podem ser observadas separadamen
- Na produção de uma carta de reclamação, a revisão deve ocorrer somente após a publicação e
- A leitura expressiva de um poema escrito constitui atividade de oralização, enquanto uma a
- O caráter multimodal de uma mensagem depende de sua circulação na internet; cartazes impre
- Uma roda de leitura literária que analisa imagens poéticas e sustenta interpretações em pa
- Durante o estudo de uma carta formal, o ensino da norma-padrão exige considerar a fala reg
- Ao trocar 'porém' por 'portanto' em um parágrafo argumentativo, um estudante pode manter f
- Se o estudante localiza corretamente a data mencionada em uma reportagem, já se pode concl
- Antes de compartilhar um vídeo usado em aula, os estudantes conferem autoria, data, cortes
- A Lei nº 14.533/2023 instituiu uma política restrita à capacitação técnica em programação,
- Na produção de uma resenha, selecionar informações da obra e justificar uma avaliação para
- Quando a produção inicial revela ausência de tese num artigo de opinião, a avaliação forma
- Em qualquer texto, empregar conectivos corretos garante coerência, mesmo quando as afirmaç
- Em um projeto sobre memória local, a produção de entrevistas, a análise de sua transcrição
- Quando uma adaptação audiovisual preserva o enredo de um conto, a análise literária do tex
- Uma proposta de multiletramentos pode examinar grafite local, notícia impressa e vídeo de 
- Em leitura de editorial, um estudante explica a posição defendida com evidências do texto,
- A avaliação de podcast autoral pode observar qualidade da pesquisa, roteiro, articulação e
```

## Pedido 45: Tutela processual do meio ambiente (`direito-ambiental.tutela-processual-ambiental`, 138 tópicos de edital)

```
Matéria: direito-ambiental.tutela-processual-ambiental (Tutela processual do meio ambiente)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Acesso coletivo à Justiça e ação civil pública ambiental": 26
- "2. Inquérito civil, TAC, competência e coisa julgada": 17
- "3. Tutela inibitória, urgência, evidência e prova ambiental": 21
- "4. Responsabilidade, reparação integral e processos civil e penal": 17
- "5. Povos tradicionais, participação processual e efetividade institucional": 19

Já existem (não repita):
- Uma comunidade pretende propor ação civil pública para impedir o despejo irregular de resí
- Uma associação ambiental está constituída há dois anos e tem proteção do meio ambiente exp
- Um cidadão eleitor quer impugnar judicialmente ato administrativo lesivo a uma área de pre
- Após receber denúncia sobre água contaminada, o Ministério Público instaura procedimento p
- Órgão público legitimado pactua com poluidor a cessação dos despejos, monitoramento e mult
- Uma fábrica poluiu um córrego de um único município. Sem peculiaridade de competência fede
- A empresa planeja descartar rejeitos em área protegida. Ainda não ocorreu poluição, mas há
- Diante de desmatamento em execução, relatórios técnicos indicam direito plausível e risco 
- Em processo ambiental há prova documental suficiente dos fatos e a defesa não apresenta el
- Encerrada a investigação de suposta poluição, o Ministério Público promove fundamentadamen
- Uma associação pede informações para preparar ACP, enquanto o Ministério Público requisita
- Uma ACP busca que empresa restaure mata ciliar e também indenize degradação residual que a
- Décadas após a destruição de vegetação nativa, busca-se em juízo reparação civil do dano e
- Moradores denunciam emissão de substância química, cujos registros técnicos estão com a em
- Em ACP de abrangência regional, a defesa sustenta que a sentença beneficiará apenas pessoa
- Uma ACP ambiental é proposta por associação legitimada, sem Ministério Público como autor.
- Após constatação de crime tipificado na Lei nº 9.605/1998, vítimas discutem se precisam ap
- Uma comunidade indígena ajuíza demanda para proteger rio necessário à sua subsistência. A 
- Instituição científica apresenta estudos sobre contaminação e pede participar de ACP como 
- Uma ação civil pública envolve dano de abrangência regional, com efeitos em vários municíp
- Documentos indicam construção irregular iminente em área sensível e risco de dano antes da
- ONG ambiental criada há seis meses propõe ACP para evitar dano grave e irreversível. Há fi
- Em crime ambiental de menor potencial ofensivo, discute-se proposta de aplicação imediata 
- Imóvel rural já degradado é vendido a outro possuidor, que alega não ter sido causador do 
- Após firmar TAC de recuperação florestal, empresa sustenta que o compromisso elimina futur
- Uma cidadã regularmente alistada propõe ação popular contra autorização administrativa les
- Qualquer pessoa natural pode ajuizar ação civil pública ambiental apenas por se considerar
- Diante de notícias de descarte irregular, o Ministério Público pode instaurar inquérito ci
- Um termo de ajustamento de conduta ambiental é mera promessa privada sem força executiva a
- O juiz pode conceder tutela específica para impedir prática iminente de ilícito ambiental 
- Toda ação judicial ambiental autoriza tutela de urgência sem demonstração do perigo de dan
- Para instruir a petição inicial da ação civil pública, o interessado pode requerer certidõ
- A requisição de perícia formulada pelo Ministério Público em inquérito civil pode fixar pr
- Promovido fundamentadamente o arquivamento do inquérito civil, os autos deverão ser remeti
- Mesmo depois do julgamento do Tema 1075, o art. 16 da Lei nº 7.347/1985 limita validamente
- Em ação ambiental, a restauração de vegetação e a indenização por dano residual podem ser 
- A responsabilidade civil por dano ambiental exige, em qualquer caso, prova de dolo ou culp
- Nas infrações penais previstas na Lei nº 9.605/1998, a ação penal é pública incondicionada
- Na tutela da evidência prevista no art. 311, IV, do CPC, o juiz pode decidir liminarmente 
- A Constituição assegura a indígenas, comunidades e organizações legitimidade para defender
- A consulta a povos interessados prevista na Convenção nº 169 da OIT pode ser substituída p
- Na ação de degradação ambiental em que a empresa detém os registros de emissão, o entendim
- A assinatura de TAC de recuperação ecológica impede, por si só, o ajuizamento de ação pena
- O adquirente de imóvel ambientalmente degradado pode estar sujeito à obrigação de recupera
- A ausência de inquérito civil prévio impede que o Ministério Público proponha ACP ambienta
```

## Pedido 46: Militares dos Estados, do Distrito Federal e dos Territórios: art. 42 (`direito-constitucional.militares-estados-art-42`, 138 tópicos de edital)

```
Matéria: direito-constitucional.militares-estados-art-42 (Militares dos Estados, do Distrito Federal e dos Territórios: art. 42)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Art. 42: natureza militar, abrangência e hierarquia": 22
- "2. Remissões do § 1º, patentes e regime especial": 19
- "3. Greve, sindicalização, filiação partidária e elegibilidade": 21
- "4. Acumulação de cargos: art. 42, § 3º, e art. 37, XVI": 19
- "5. Patentes, perda do posto, pensões e casos integrados": 19

Já existem (não repita):
- Uma pessoa consulta o art. 42 para saber quais instituições são constitucionalmente identi
- Uma exposição jurídica atribui a formação institucional das duas corporações do art. 42 a 
- Um oficial de corporação militar estadual precisa identificar quem confere sua patente seg
- Uma comissão revisa os direitos de integrantes das corporações do art. 42 e discute se pod
- Uma norma interna discute a filiação político-partidária de militar ainda em atividade. Qu
- Um resumo constitucional procura a emenda que reestruturou a condição dos militares estadu
- Um grupo estuda a disciplina de pensionistas de militares estaduais e quer saber o que est
- Um militar alistável conta oito anos de serviço e pretende disputar eleição. Qual providên
- Um militar alistável possui doze anos de serviço e é eleito. Segundo o art. 14, § 8º, II, 
- Uma proposta de acumulação remunerada por militar estadual será examinada à luz da EC 101/
- Um material antigo afirma que a acumulação de um cargo de professor com outro exige a natu
- Uma pessoa descreve a acumulação autorizada pelo art. 37, XVI, c, e diz que basta um dos v
- Um militar estadual toma conhecimento de que o art. 42, § 1º, remete a outras partes da Co
- Uma hipótese envolvendo corporação militar estadual é analisada após a EC 138/2025: acumul
- Um estudo compara militar estadual e integrante civil da segurança quanto à greve. Qual fo
- Um militar estadual da ativa toma posse em cargo civil permanente, fora de hipótese consti
- Um militar assume função civil temporária não eletiva na forma da lei, sem enquadramento e
- Um oficial estadual foi condenado definitivamente a pena privativa de liberdade de três an
- Uma lei regional disciplina ingresso, remuneração, estabilidade e inatividade de integrant
- Um parecer sustenta que, por força do art. 142, § 3º, VIII, apenas a alínea c do art. 37, 
- Um militar estadual ocupa atividade militar compatível com docência pública e pretende acu
- Uma hipótese descreve oficial condenado a pena privativa de liberdade exatamente igual a d
- Uma prova transcreve o art. 42, § 2º, na versão anterior à EC 41/2003 e o aplica a pension
- O Tema 541 do STF é citado para justificar a proibição de greve de profissionais civis que
- Uma questão apresenta art. 42, § 3º, como autorização irrestrita para militar estadual man
- As polícias militares e os corpos de bombeiros militares estão incluídos no art. 42 da Con
- Os integrantes das Forças Armadas e das polícias civis integram a mesma categoria de milit
- O art. 42, § 1º, determina que os postos e patentes dos oficiais estaduais sejam conferido
- A vedação de greve dos militares estaduais depende de lei regional que escolha entre autor
- O militar em serviço ativo não pode filiar-se a partido político, de acordo com o art. 142
- O art. 42, § 2º, na redação constitucional vigente, disciplina pensões militares por simpl
- A EC 101/2019 acrescentou ao art. 42 o § 3º, que aplica aos militares estaduais o art. 37,
- Em 2026, o art. 37, XVI, b, permite acumular um cargo de professor somente com outro técni
- Ao militar alistável com menos de dez anos de serviço, o art. 14, § 8º, I, determina afast
- Militar alistável com mais de dez anos de serviço e eleito para mandato deve passar à inat
- Um cargo de professor pode ser acumulado, em hipótese constitucional, com outro de qualque
- A referência do art. 142, § 3º, VIII, à alínea c do art. 37, XVI, impede que o art. 42, § 
- Em caso de acumulação autorizável ao militar estadual, compatibilidade de horários e preva
- A alínea c do art. 37, XVI, permite acumular qualquer vínculo militar com atividade civil 
- O Tema 541 do STF veda greve de policiais civis e de outros servidores atuantes diretament
- Oficial com condenação criminal transitada em julgado a pena privativa de liberdade superi
- O art. 142, § 3º, III, prevê agregação e limitações específicas para militar da ativa que,
- O art. 142, § 2º, aplicado pelo art. 42, § 1º, autoriza concluir que todas as punições dis
- O militar estadual alistável, mesmo sujeito à proibição de filiação partidária enquanto na
- A aprovação da EC 138/2025 permitiu a qualquer militar estadual acumular duas funções públ
```

## Pedido 47: Direito agrário e terras públicas (`direito-administrativo.direito-agrario-terras-publicas`, 137 tópicos de edital)

```
Matéria: direito-administrativo.direito-agrario-terras-publicas (Direito agrário e terras públicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação histórica das terras públicas e fundamentos do direito agrário": 22
- "2. Terras devolutas, titularidade, faixa de fronteira e discriminação": 26
- "3. Cadastro, matrícula, georreferenciamento e regularização fundiária": 14
- "4. Função social, Estatuto da Terra e política agrícola": 20
- "5. Reforma agrária, desapropriação e destinação pública": 18

Já existem (não repita):
- Em estudo histórico, uma área foi classificada como devoluta sob o regime imperial de 1850
- Pesquisadores comparam a repartição de terras devolutas nas primeiras constituições republ
- Uma área pertencente a ente público está desafetada de utilização coletiva ou serviço espe
- Uma família explora diretamente imóvel rural e obtém dali sustento e progresso social, usa
- Em levantamento agrário, um documento usa módulo rural para caracterizar propriedade famil
- Uma gleba identificada como devoluta é indispensável à defesa de fronteiras, nos termos de
- Uma fazenda privada regularmente titulada está a 80 km de uma fronteira terrestre. O fato 
- Uma pessoa reside e cultiva durante vinte anos área comprovadamente pertencente ao patrimô
- Uma administração pretende separar, numa mesma gleba, parcelas pertencentes ao patrimônio 
- O procedimento administrativo de discriminação federal será instaurado por comissão especi
- Um ente público pretende conceder a pessoa privada terra de 3.000 hectares, fora de progra
- Uma pessoa apresenta apenas CCIR atualizado para demonstrar que possui domínio privado inc
- Dois cadastros ambientais se sobrepõem sobre uma área rural. Um dos declarantes sustenta q
- Uma área rural foi corretamente georreferenciada por profissional habilitado e teve seus l
- Ocupante de área federal na Amazônia Legal deseja regularizar sua situação e pergunta quai
- Uma propriedade rural é economicamente produtiva, mas há comprovação de desrespeito a norm
- A avaliação técnica de imóvel indica grau de utilização da terra de 82% e grau de eficiênc
- Dois imóveis rurais têm, respectivamente, 4 e 12 módulos fiscais. Sem tratar de produtivid
- Pessoa possui um imóvel rural de 3 módulos fiscais e nenhum outro imóvel rural. Discute-se
- Um plano público deseja estimular agricultura por meio de seguro, assistência técnica, cré
- Em desapropriação constitucional de imóvel rural para reforma agrária, o laudo separa preç
- A União publicou decreto declarando imóvel rural de interesse social para reforma agrária.
- Na ação especial de desapropriação para reforma agrária, os autos estão suficientemente in
- Beneficiário da distribuição de imóvel rural em programa de reforma agrária recebeu título
- Um conflito envolve três glebas: uma pública de domínio ainda a discriminar, outra particu
- Uma gleba rural integrante do patrimônio público não pode ser adquirida por usucapião apen
- Uma área devoluta estadual será considerada propriedade federal apenas porque está situada
- Em uma análise histórica, a Lei nº 601/1850 é identificada como marco de disciplina das te
- Um cadastro ambiental rural preenchido e deferido equivale, por sua natureza jurídica, a r
- Uma fazenda que atende aos índices técnicos de produtividade ainda deve ser examinada, par
- O art. 187 da Constituição restringe política agrícola à compra de terras e à instalação d
- Na desapropriação por interesse social para reforma agrária, a Constituição prevê indeniza
- Para discriminar terras devolutas federais, a Lei nº 6.383/1976 admite apenas processo jud
- Uma matrícula regularmente aberta, um CCIR emitido e um CAR ativo podem coexistir para o m
- Uma área rural em faixa de fronteira de 150 km, mesmo que tenha título particular legítimo
- O art. 4º da Lei nº 8.629/1993 classifica a pequena propriedade rural como aquela com área
- Um imóvel rural com grau de utilização de 90% e eficiência de exploração de 98% pode ser c
- A Lei nº 11.952/2009 e o Decreto nº 10.592/2020 tratam, nas áreas e condições abrangidas, 
- A Constituição determina que alienação ou concessão de qualquer terra pública superior a 2
- Na desapropriação de imóvel rural por interesse social para reforma agrária, a ação deve s
- A LC nº 76/1993 permite que a contestação da desapropriação por reforma agrária seja apres
- Os títulos de domínio ou concessões de uso entregues a beneficiários de distribuição de im
- Na discriminação de devolutas federais, o processo judicial é usado exclusivamente depois 
- Na desapropriação agrária do art. 184 da Constituição, os títulos da dívida agrária utiliz
- Em litígio envolvendo área comprovadamente pública, a posse produtiva pelo prazo de cinco 
```

## Pedido 48: Defesa da concorrência (`direito-economico.defesa-concorrencia`, 137 tópicos de edital)

```
Matéria: direito-economico.defesa-concorrencia (Defesa da concorrência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais e Sistema Brasileiro de Defesa da Concorrência": 22
- "2. Infrações à ordem econômica e abuso de posição dominante": 25
- "3. Sanções, responsabilização, acordos e reparação civil": 15
- "4. Atos de concentração e controle preventivo": 20
- "5. Defesa comercial: dumping, subsídios e salvaguardas": 18

Já existem (não repita):
- Empresas estrangeiras combinam divisão de clientes em reunião realizada fora do Brasil, ma
- Uma equipe elabora organograma da autarquia concorrencial e precisa identificar seus três 
- Duas empresas concorrentes acertam antecipadamente preços a cobrar em seus pontos de venda
- Uma empresa participa de mercado relevante com 20% das vendas. A equipe jurídica examina o
- Uma sociedade empresária é condenada por infração concorrencial e o cálculo parte do fatur
- Duas empresas de grupos independentes, com faturamento brasileiro de R$ 780 milhões e R$ 9
- Após fusão sujeita a notificação, as partes querem integrar preços e equipes antes da deci
- Um exportador vende produto ao Brasil por US$ 85, embora o valor normal comparável do prod
- Uma indústria sofre prejuízo grave causado por crescimento repentino das importações, sem 
- Duas empresas de grupos diferentes faturaram no Brasil R$ 940 milhões e R$ 60 milhões no a
- Uma rede conquistou grande participação ao reduzir custos com inovação, sem estratégia de 
- O administrador participou de infração cometida por sua empresa. Para aplicação da multa p
- Uma empresa propõe termo de compromisso de cessação de prática enquanto responde a process
- Duas empresas, com R$ 800 milhões e R$ 90 milhões de faturamento, celebram contrato de con
- Uma autoridade investiga importações com preço de exportação inferior ao valor normal, mas
- Duas empresas acordaram operação notificável e apresentaram argumentos de melhora de quali
- Aumento abrupto de importações, sem investigação de dumping, provoca prejuízo grave à indú
- Uma concentração notificável é submetida à análise e as partes solicitam extensão do perío
- Uma empresa não atingiu os limiares obrigatórios quando adquiriu outra, mas sua operação f
- Vítimas de cartel propõem reparação civil após conclusão do processo administrativo. Parte
- Um processo administrativo de apuração concorrencial permanece sem despacho ou julgamento 
- Uma empresa propõe leniência por cartel já investigado, cumprindo os requisitos, mas a Sup
- Uma associação empresarial promove reunião em que todos os concorrentes informam seus preç
- Uma empresa recebe subsídio específico de seu governo estrangeiro e suas exportações ao Br
- Após aprovação de aquisição, descobre-se que a decisão se baseou em informações falsas e p
- A defesa da concorrência instituída pela Lei nº 12.529/2011 protege a coletividade, não se
- As funções de investigação de condutas e de julgamento sancionador são concentradas obriga
- A combinação de preços futuros entre empresas concorrentes pode caracterizar cartel previs
- Uma empresa com 20% de participação no mercado relevante comete automaticamente abuso de p
- A aquisição de controle empresarial pode constituir ato de concentração, e, preenchidos os
- Direitos antidumping são sanções pela realização de fusões societárias, pois o Decreto nº 
- A responsabilidade do administrador por multa pessoal de infração concorrencial exige comp
- Para os critérios financeiros atuais de concentração, basta que um único grupo fature R$ 7
- O art. 36, § 3º, da Lei nº 12.529/2011 lista condutas exemplificativas que devem ser inter
- A simples apresentação de proposta de termo de compromisso de cessação de prática suspende
- O Cade pode requisitar a submissão de concentração abaixo dos limiares obrigatórios, desde
- A aplicação de salvaguarda exige, como pressuposto indispensável, prova de que exportadore
- Para aferir cartel, a coincidência de preços de dois concorrentes deve ser analisada com o
- Quando é comprovada reincidência de infração concorrencial, o art. 37, § 1º, determina que
- Conforme o art. 88, § 9º, da Lei nº 12.529/2011, a dilação do prazo-base de análise pode o
- A Lei nº 14.470/2022 retirou a possibilidade de ação civil de reparação por cartel quando 
- O art. 88, § 6º, permite autorização de concentração potencialmente restritiva diante de o
- O Decreto nº 1.751/1995 continua sendo a referência regulamentar principal e vigente para 
- O art. 85, §§ 6º, 9º e 10, diferencia a proposta de TCC do compromisso celebrado: a primei
- Uma concentração abaixo dos limites financeiros de notificação obrigatória está definitiva
```

## Pedido 49: Vendas, marketing e experiência do cliente (`gestao.vendas-marketing-experiencia-cliente`, 136 tópicos de edital)

```
Matéria: gestao.vendas-marketing-experiencia-cliente (Vendas, marketing e experiência do cliente)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Valor percebido, proposta de valor e posicionamento": 20
- "2. Segmentação, comportamento do consumidor e decisão de compra": 19
- "3. Marketing de serviços e gestão da experiência do cliente": 25
- "4. Vendas consultivas, negociação e relacionamento": 14
- "5. Marketing digital, leads, inbound, copywriting e avaliação de resultados": 22

Já existem (não repita):
- Uma clínica preservou a mensalidade, mas reduziu o tempo de espera e tornou o agendamento 
- Uma empresa anuncia que seu sistema envia alertas automáticos de vencimento. Para apresent
- Uma plataforma agrupa usuários segundo frequência de compra e hábito de renovar assinatura
- Uma equipe quer organizar perfis conforme estilo de vida, preferência por sustentabilidade
- Um horário de atendimento odontológico ficou ocioso ontem, embora haja clientes interessad
- O resultado de uma consultoria muda muito conforme o profissional escalado. Qual medida li
- Uma empresa atende por telefone, loja e aplicativo, mas o cliente precisa repetir toda a h
- Um vendedor recebe a objeção 'o plano é caro' e deseja agir de forma consultiva. Qual prim
- Uma página recebe cem visitantes anônimos; vinte deixam contatos para obter uma demonstraç
- Uma empresa publica guias úteis que respondem dúvidas de consumidores antes da contratação
- Uma pesquisa registrou 45% de notas 9–10, 30% de notas 7–8 e 25% de notas 0–6. Segundo a m
- Uma equipe construiu uma persona com entrevistas, perfil típico, dúvidas e objeções, enqua
- Uma campanha personaliza mensagens usando dados pessoais comuns e considera a hipótese de 
- Durante uma aula ao vivo, a participação dos alunos modifica ritmo e exemplos enquanto o s
- Uma organização implantou CRM e deseja medir se a estratégia ultrapassou a simples implant
- Após a resolução de um problema, uma empresa pergunta 'quanto esforço você precisou fazer 
- Na negociação, uma compradora diz 'preciso de desconto', mas revela que o objetivo é respe
- Uma campanha teve 2.000 visitantes, 200 leads e 30 vendas, todas atribuídas a esses leads.
- Uma loja quer usar o gatilho de escassez e anunciar 'últimas duas unidades' quando ainda p
- Uma equipe de marketing definiu um lead como qualificado por perfil e engajamento. A equip
- Duas soluções têm preço igual. A solução X possui mais recursos, mas exige configuração co
- Uma empresa cruza região de residência e histórico de recompra para escolher onde lançar u
- Após reformular o aplicativo, uma empresa observou CES melhor, porém NPS estável. Qual int
- Um comprador pede redução de preço, mas declara que não utilizará o suporte avançado inclu
- Uma campanha de conteúdo atrai cadastros para receber dicas técnicas. A equipe decide reut
- Uma loja mantinha o preço de um produto e passou a informar claramente prazo de entrega e 
- Ao anunciar um produto 'com entrega em 24 horas', uma empresa descreveu exclusivamente um 
- Uma empresa classifica os clientes conforme frequência de uso do aplicativo. Nesse caso, e
- A partir de uma única entrevista, uma equipe criou uma persona imaginária sem investigar c
- Uma sala de atendimento não utilizada na terça-feira pode ser estocada como capacidade de 
- Uma academia utiliza treinamento e padrões de atendimento para diminuir diferenças de qual
- Uma empresa recebe comentários em redes sociais, chat e telefone, mas transfere cada solic
- Uma vendedora investiga as necessidades do cliente antes de indicar um plano com menor núm
- Um cadastro de CRM contém reclamação anterior e canal preferido informado pelo cliente. Qu
- Ao sugerir uma capa protetora para complementar um equipamento adquirido, o vendedor prati
- Em um site, dez pessoas fornecem contato para solicitar orçamento. Elas podem ser classifi
- Como determinada empresa pagou anúncios para divulgar um guia gratuito, sua estratégia nec
- Um anúncio utiliza avaliações verificáveis de consumidores e destaca prazo promocional efe
- Uma pesquisa com 60% de promotores e 20% de detratores resulta em NPS de 80 pontos, pois a
- Para um consumidor que teme custos inesperados, demonstrar antecipadamente preços totais e
- Uma plataforma gravada oferece avaliações assíncronas e reduz contato simultâneo entre usu
- Em uma negociação, o fornecedor conhece sua alternativa viável caso não haja acordo e util
- Em ações de personalização digital, o tratamento de dados pessoais somente será lícito se 
- Um teste de títulos em duas versões distribuídas aleatoriamente a públicos comparáveis, co
- Uma campanha teve taxa de cliques elevada, mas quase nenhum cliente novo. Ainda assim, o C
```

## Pedido 50: Java: fundamentos da linguagem (`informatica.java-fundamentos`, 136 tópicos de edital)

```
Matéria: informatica.java-fundamentos (Java: fundamentos da linguagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Da compilação à JVM: execução, memória e diagnóstico": 18
- "2. Tipos, literais, operadores e controle de fluxo": 32
- "3. Arrays, String, classes, herança e polimorfismo": 16
- "4. Exceções, coleções e fundamentos de APIs": 18
- "5. Java 8 a 21: recursos da linguagem e compatibilidade": 16

Já existem (não repita):
- Uma pessoa escreve a classe pública `Ola` no arquivo `Ola.java` e deseja executar seu méto
- Ao investigar uma aplicação Java com objetos criados por `new` e chamadas recursivas, que 
- Um programa declara `char letra='A'; String palavra="A"; boolean ativo=true;`. Qual interp
- O método executa `double valor = 9 / 4; System.out.print(valor);`. Qual saída ocorre na fo
- Considere `int x=0; boolean ok = false && (++x>0); System.out.print(x);`. O que será impre
- Um programa cria `int[] notas={5,7,9};` e em seguida imprime `notas.length` e `notas[1]`. 
- O código faz `String s="casa"; s.toUpperCase(); System.out.print(s);`. Qual saída é produz
- Uma classe chamada `Pessoa` declara `public void Pessoa(){}` e nenhum outro construtor. Qu
- Considere `final ArrayList<String> nomes=new ArrayList<>();` e, na linha seguinte, `nomes.
- Uma variável `Animal a` referencia um objeto `Cachorro`, e `Cachorro` sobrescreve o método
- Um método executa operação que pode lançar `IOException` e não contém bloco que a trate. Q
- Um `HashMap<String,Integer> mapa` recebe `put("x",2)` e depois `put("x",5)`. Qual estado f
- Uma pessoa adiciona duas vezes a String `"sol"` em `HashSet<String>` e depois adiciona `"l
- Uma aplicação em execução precisa ser identificada e, depois, ter suas pilhas de threads a
- Considere, em Java 11, `String s=" ";`. Qual par de resultados de `s.isBlank()` e `s.isEmp
- Um programa compilado para Java 11 contém `var n=10;` dentro de um método e depois tenta `
- Em Java, execute `int x=2+3*4; int y=x++; System.out.print(x+":"+y);`. Qual sequência é im
- Considere `int x=1; switch(x){case 1: x+=2; case 2: x*=3; break; default: x=0;} System.out
- Uma classe declara métodos sobrecarregados `String f(Object x){return "O";}` e `String f(S
- O código contém `Integer n=null; int k=n;` num método e o bloco é executado. Qual comporta
- Uma pessoa compara os marcos de versões Java. Qual sequência associa corretamente funciona
- Em Java 21, declara-se `record Ponto(int x, int y) {}` sem membros adicionais. Que acesso 
- Um projeto utiliza `sealed interface Resultado permits Sucesso, Falha {}`. Considerando re
- Um trecho é preparado especificamente para Java 21: `int r=switch(obj){case String s -> s.
- Um diagnóstico utiliza `System.gc()` numa JVM e observa Metaspace em implementação HotSpot
- No fluxo tradicional do Java, o comando `javac Arquivo.java` pode gerar bytecode em arquiv
- Em um método, a declaração `int x; System.out.print(x);` compila, pois variáveis locais pr
- Em `do { x++; } while (x < 0);`, o corpo executa ao menos uma vez se o programa atingir a 
- Para quaisquer duas referências não nulas de `String`, o operador `==` verifica igualdade 
- Um `ArrayList<Integer>` permite acesso por índice, mas, por possuir tamanho fixo após a cr
- Um campo declarado `private` em uma classe Java não pode ser acessado diretamente por códi
- Uma `ArrayList<String>` admite duas posições diferentes com a mesma String `"A"`, preserva
- Se um `try` possui `catch(Exception e)` antes de `catch(IOException e)`, as duas cláusulas
- Em Java, `throw new IllegalArgumentException()` lança uma instância de exceção no fluxo, a
- Em aplicação Java com objetos não alcançáveis, a chamada `System.gc()` garante que a JVM r
- Em `HashSet<String>`, inserir novamente o mesmo texto por `add` não aumenta o número de el
- Uma classe em Java 11 declara `private var total=10;` como campo de instância. A inferênci
- Desde a estabilização de switch expressions, é possível usar uma expressão `switch` para p
- `IOException` e `NullPointerException` integram a categoria de exceções unchecked, pois am
- No Java 21, uma lambda pode fornecer a implementação de método abstrato único de uma inter
- Em Java, declarar `catch(Exception e)` antes de `catch(IOException e)` num mesmo try produ
- Considere `byte b=1; b+=2;` e, em trecho alternativo, `byte c=1; c=c+2;`. O primeiro pode 
- Uma declaração `record Caixa(ArrayList<String> itens) {}` torna profundamente imutável a l
- Virtual threads tornaram-se funcionalidade permanente no Java 21 e podem compartilhar thre
- A ferramenta `jps` garante que todo identificador local exibido seja exatamente o PID do s
```

