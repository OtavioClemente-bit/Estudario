# Pedidos de questões extras, lote 5 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 29091 tópicos de edital.

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

## Pedido 1: Principais transtornos psiquiátricos (`psiquiatria.transtornos-mentais-principais`, 640 tópicos de edital)

```
Matéria: psiquiatria.transtornos-mentais-principais (Principais transtornos psiquiátricos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, classificação e avaliação clínica": 17
- "2. Transtornos psicóticos, humor e puerpério": 21
- "3. Ansiedade, trauma, obsessões e personalidade": 20
- "4. Neurodesenvolvimento, cognição e envelhecimento": 21
- "5. Sono, alimentação, substâncias e sexualidade": 21

Já existem (não repita):
- Uma pessoa de 78 anos, internada por infecção, passa a não conseguir manter a atenção, alt
- Uma pessoa relata duas semanas de perda marcante de interesse, tristeza, fadiga e prejuízo
- Durante entrevista, alguém afirma com absoluta convicção que mensagens secretas da televis
- Uma pessoa dorme apenas três horas por noite sem cansaço, apresenta grandiosidade, fala ac
- Uma pessoa teme contaminação, reconhece que o medo é excessivo e lava as mãos repetidas ve
- Após sofrer um evento extremamente ameaçador, uma pessoa revive a experiência como se esti
- Uma criança apresenta desatenção, impulsividade e prejuízo no aprendizado e em casa há vár
- Uma adolescente restringe intensamente a alimentação, apresenta peso significativamente ba
- Oito dias após o parto, uma pessoa passa a ouvir vozes, apresentar ideias delirantes e osc
- Uma pessoa desenvolve delírios e discurso desorganizado após período recente de uso intens
- Em acompanhamento longitudinal, uma pessoa apresenta psicose típica e episódios afetivos i
- Uma pessoa sofre episódios súbitos e inesperados de palpitação, tremores e medo intenso e,
- Uma pessoa apresenta há muitos anos instabilidade interpessoal, impulsividade, medo intens
- Em diferentes atendimentos, uma pessoa falsifica sintomas para ocupar a posição de pacient
- Após perder o emprego, uma pessoa desenvolve preocupação intensa e queda de rendimento lig
- Uma pessoa relata episódios de ingestão excessiva com perda de controle, seguidos regularm
- Uma pessoa adormecida grita e movimenta vigorosamente os braços enquanto relata, ao desper
- Uma pessoa com uso prolongado e intenso de álcool interrompe abruptamente o consumo e apre
- Um texto antigo agrupa condições ligadas à identidade de gênero entre transtornos mentais.
- Uma pessoa com esquizofrenia estabilizada recebe acompanhamento clínico e psicossocial. Um
- Uma pessoa apresenta dor crônica genuína e investigada; contudo, gasta grande parte do dia
- Uma pessoa com transtorno neurocognitivo maior conhecido, anteriormente estável, torna-se 
- Uma adolescente tem dificuldades intelectuais e importantes limitações de autonomia prátic
- Após iniciar substância estimulante, uma pessoa sem episódios prévios apresenta euforia, i
- Uma pessoa idosa tem declínio cognitivo flutuante, alucinações visuais recorrentes, rigide
- Uma pessoa com esquizofrenia apresenta retraimento social, pouca iniciativa e expressão fa
- Uma pessoa com TOC lava repetidamente as mãos para aliviar medo de contaminação. Como o ge
- Uma pessoa relata ouvir vozes inexistentes no ambiente e expressa convicção de perseguição
- Uma pessoa apresenta compulsões alimentares com perda de controle, sem condutas compensató
- Uma pessoa apresenta delírios e confusão poucos dias após o parto. Ainda que familiares at
- Após vivenciar um assalto, uma pessoa apresenta medo e insônia na noite seguinte. O simple
- Uma pessoa que sofreu violência grave apresenta recordações invasivas vividas como retorno
- Uma pessoa apresenta discurso extremamente desorganizado e comportamento bizarro persisten
- Uma pessoa tem ataques irresistíveis de sono durante o dia e episódios de perda transitóri
- Uma pessoa idosa tem piora de atenção em poucas horas durante quadro infeccioso. Por ser u
- Uma pessoa interrompe consumo prolongado e intenso de álcool e desenvolve tremores, sudore
- Em atualização de material antigo sobre identidade sexual, uma pessoa afirma que a CID-11 
- Uma pessoa com histórico de uso nocivo prolongado de álcool desenvolve grave dificuldade d
- Uma pessoa apresenta ataques de pânico repetidos, evita atividades por medo de novas crise
- Uma pessoa com transtorno psicótico inicia antipsicótico de uso continuado. Mesmo com melh
- Uma pessoa com doença crônica interrompe medicamento por efeitos adversos e dificuldade de
- Uma pessoa ronca intensamente, tem pausas respiratórias observadas durante o sono e sonolê
- Uma pessoa apresenta delírios apenas durante período de humor francamente maníaco, sem psi
- Uma pessoa falsifica deliberadamente achados clínicos com objetivo de obter vantagem finan
- Uma pessoa com transtorno bipolar tipo I está em episódio maníaco grave. Como antidepressi
```

## Pedido 2: Conceito e espécies de tributos (`direito-tributario.conceito-especies-tributos`, 639 tópicos de edital)

```
Matéria: direito-tributario.conceito-especies-tributos (Conceito e espécies de tributos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Tributo: conceito, natureza e classificações": 20
- "2. Impostos, taxas e preços públicos": 27
- "3. Contribuição de melhoria e empréstimos compulsórios": 24
- "4. Contribuições especiais, parafiscalidade e IBS/CBS": 17
- "5. Legislação tributária, aplicação e interpretação": 12

Já existem (não repita):
- Uma lei denomina contribuição uma cobrança anual exigida pelo simples fato de a pessoa pos
- Após atraso na entrega de declaração, uma pessoa recebe cobrança da quantia originalmente 
- Um resumo apresenta somente impostos, taxas e contribuições de melhoria e afirma reproduzi
- Um ente cobra valor pelo exercício regular de fiscalização sanitária dos estabelecimentos 
- Um imóvel é atendido por coleta domiciliar de resíduos, serviço obrigatório e efetivamente
- Após obra viária, cada terreno do entorno experimenta valorização imobiliária comprovada. 
- Diante de despesas extraordinárias decorrentes de calamidade pública, pretende-se institui
- Uma cobrança legal é destinada especificamente a atuação estatal sobre determinado setor d
- Uma lei municipal pretende financiar iluminação pública por meio de exação específica. Qua
- Ao comparar os tributos sobre bens e serviços introduzidos pela reforma constitucional, qu
- A fiscalização tributária aplica uma norma a ocorrência tributável iniciada antes da nova 
- Uma lei amplia a base econômica de imposto de modo a elevar a exigência, enquanto outro at
- A lei cria cobrança periódica para financiar a limpeza geral de vias e praças, sem serviço
- A obra pública custou R$ 600 mil; certo imóvel ganhou R$ 18 mil de valor em decorrência di
- Certa exação tem fato gerador ligado à atividade econômica e receita legalmente destinada 
- Uma pessoa recolheu tributo juridicamente repercutível no preço de venda e pede restituiçã
- A contribuição de melhoria será instituída após obra pública. Antes de sua cobrança, qual 
- Uma taxa de resíduos utiliza a área edificada do imóvel como um dos critérios de cálculo, 
- Uma pessoa que adere facultativamente a serviço público remunerado por contrato paga quant
- Um plano de investimento público é relevante e urgente, mas a União pretende financiá-lo c
- A lei atribui a entidade distinta do ente competente a arrecadação de contribuição de inte
- No âmbito da legislação tributária, uma autoridade tenta resolver lacuna por analogia para
- Lei nova prevê punição menos severa por infração tributária cometida antes de sua publicaç
- Lei específica concede isenção com condições expressas. Um contribuinte não enquadrado ped
- Uma contribuição instituída localmente destina-se ao custeio e expansão de sistemas de mon
- Uma pessoa aufere rendimentos e deve imposto sobre eles, enquanto multa adicional decorre 
- Uma taxa foi instituída pelo uso de iluminação nas ruas de um bairro, argumentando-se que 
- Em projeto de contribuição de melhoria, a obra pública valorizou vários imóveis. A cobranç
- Diante de guerra externa iminente, uma lei ordinária da União basta para instituir emprést
- Uma contribuição é juridicamente caracterizada pela finalidade de intervenção em setor eco
- Uma norma local determina que a cobrança chamada taxa de resíduos seja exigida pela varriç
- Uma taxa de fiscalização é devida pela atuação regular de polícia administrativa. O fato d
- Depois de instituir contribuição de melhoria por obra que gerou valorização comprovada, a 
- O contribuinte pagou tributo que comporta transferência do encargo financeiro, mas demonst
- Em comparação com o imposto sobre bens e serviços, a contribuição sobre bens e serviços é 
- Uma lei tributária remete a decreto sua execução administrativa, mas reserva à lei a criaç
- Uma taxa de serviço facultativamente contratado pode ser cobrada compulsoriamente só porqu
- Uma lei posterior reduz a penalidade por infração tributária ainda não definitivamente jul
- Uma contribuição parafiscal é arrecadada por entidade autorizada legalmente. Disso resulta
- Um ente pretende cobrar taxa de limpeza usando a mesma base integral de imposto imobiliári
- Ao examinar contribuinte submetido a multa tributária com dúvida sobre a natureza da infra
- Contribuição prevista no art. 149-A é instituída para iluminação e monitoramento de lograd
- Em ausência de regra legal expressa, a autoridade aplica equidade para afastar tributo já 
- Uma norma tributária infraconstitucional usa conceitos de direito privado para apurar seu 
- Uma contribuição social incidente sobre o lucro torna-se imposto sobre a renda simplesment
```

## Pedido 3: DFC, DVA e análise de demonstrações (`contabilidade.dfc-dva-analise`, 636 tópicos de edital)

```
Matéria: contabilidade.dfc-dva-analise (DFC, DVA e análise de demonstrações)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da análise contábil, padronização e comparações": 20
- "2. DFC: classificação, métodos e conciliação do caixa": 21
- "3. DVA: formação e distribuição da riqueza": 18
- "4. Índices de liquidez, capital, solvência e rentabilidade": 27
- "5. Prazos médios, ciclos financeiros e avaliação integrada": 14

Já existem (não repita):
- Uma empresa recebeu 240 de clientes por mercadorias vendidas e pagou 150 a fornecedores. C
- Uma entidade contrai empréstimo de 500, recebido integralmente em conta, para reforçar sua
- Uma companhia apresenta na DFC operacional recebimentos de clientes, pagamentos a forneced
- Uma entidade usa o método indireto e reconheceu despesa de depreciação de 30 no resultado.
- Ao preparar demonstração destinada a mostrar a riqueza criada no período e sua repartição 
- Uma sociedade é companhia aberta e prepara suas demonstrações anuais. Qual obrigação relat
- No balanço de uma entidade, estoques são 180 e ativo total é 900. Na análise vertical, qua
- Uma receita líquida passou de 200 no exercício anterior para 260 no atual. Qual foi o cres
- Uma empresa possui ativo circulante de 300 e passivo circulante de 150. Qual é sua liquide
- Uma DVA de empresa comercial apresenta receitas de 1.000, insumos adquiridos de terceiros 
- Uma entidade divulga ativo circulante de 200, realizável a longo prazo de 100, passivo cir
- Uma empresa possui ativo total de 800 e passivo exigível somado de 400. Qual o índice de s
- No balanço, passivo circulante é 180 e passivo não circulante é 420. Qual a composição do 
- Uma empresa obteve receita líquida de 600 e lucro líquido de 90 no exercício. Qual sua mar
- O lucro líquido de uma sociedade foi 100 e o patrimônio líquido médio, calculado a partir 
- O estoque médio é 60 e o custo anual dos produtos vendidos é 360. Adotando 360 dias, qual 
- Uma companhia apresenta prazo médio de estocagem de 50 dias, recebimento de vendas de 40 d
- Pelo método indireto, uma entidade teve lucro líquido de 120, depreciação de 30, aumento d
- O caixa inicial é 80. No período, FCO foi +90, FCI −70, FCF +50 e efeito cambial de reconc
- A DVA apurou 200 de valor adicionado total. A distribuição foi: pessoal 70, tributos 50, c
- Uma entidade declarou seguir a orientação de classificação do item 34A do CPC 03 (R2). Pag
- Uma empresa adquire máquina de 400, entregando ao vendedor um título de dívida sem pagar d
- Na DRE, receita líquida passa de 800 para 900 e CPV de 400 para 630. Qual combinação é cor
- Uma empresa tem margem líquida de 8% e giro do ativo de 1,5 vez, com bases coerentes de re
- A liquidez corrente de uma empresa subiu de 1,20 para 1,50, mas o aumento de ativo circula
- Ao analisar aplicações financeiras com liquidez imediata, risco insignificante de oscilaçã
- Uma companhia vendeu à vista máquina utilizada por vários anos na produção. Como obteve ga
- Uma sociedade elaborou DVA registrando salários, tributos, juros de dívidas e lucros retid
- Ao final do exercício, a Lei nº 6.404/1976 impõe a qualquer companhia fechada, independent
- Um ativo apresenta saldo de estoques de 120 e total do ativo de 600 na mesma data. A análi
- Uma receita com número-índice horizontal de 150, tendo o período anterior como base 100, c
- Duas DFCs elaboradas para os mesmos fatos, uma pelo método direto e outra pelo indireto, d
- Uma entidade recebe empréstimo em conta para financiar salários. Como a destinação futura 
- Um tributo sobre lucro é pago no período e não pode ser especificamente associado a fluxo 
- Na aquisição de equipamento por financiamento diretamente concedido pelo vendedor, sem mov
- Uma DVA com receitas de 900, insumos de 500, depreciação de 60 e receitas financeiras de 2
- Uma companhia reteve integralmente o lucro do exercício em reservas. Na DVA, essa parcela 
- Se ativo circulante é 400, realizável a longo prazo 100, passivo circulante 250 e não circ
- Uma empresa possui ativo total de 900, passivo exigível de 600 e patrimônio líquido de 300
- Uma máquina foi vendida à vista com ganho de 25 reconhecido no lucro do exercício. No méto
- Uma companhia fechada apurou patrimônio líquido exatamente igual a R$ 2.000.000,00 na data
- Uma empresa apresenta aumento de ROE após substituir parte de capital próprio por dívida, 
- Uma companhia calcula prazo médio de pagamento a fornecedores dividindo o saldo médio de f
- Uma sociedade tem prazo médio de estocagem de 40 dias, recebimento de 20 dias e pagamento 
- Uma entidade apresenta imobilização do patrimônio líquido de 120%. Por esse único percentu
```

## Pedido 4: Autenticação, controle de acesso, OAuth2 e SSO (`informatica.autenticacao-controle-acesso-sso`, 630 tópicos de edital)

```
Matéria: informatica.autenticacao-controle-acesso-sso (Autenticação, controle de acesso, OAuth2 e SSO)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Identidade, autorização e modelos de controle de acesso": 17
- "2. Métodos de autenticação, fatores, biometria e certificados": 18
- "3. OAuth 2.0, concessões, tokens e PKCE": 21
- "4. OpenID Connect, JWT, SSO e confiança federada": 23
- "5. Keycloak, integração de diretórios e desenvolvimento seguro": 21

Já existem (não repita):
- Uma analista efetua login com sucesso, mas a plataforma recusa a leitura de um relatório r
- Uma empresa atribui a função 'revisor' a quem pode examinar pedidos; ao mudar de área, a p
- Durante a ativação da conta, a pessoa informa sua senha e um código temporário produzido n
- Uma aplicação compara a impressão digital informada pelo dispositivo e avalia o índice de 
- Após autenticar-se em um portal central, uma pessoa acessa duas aplicações integradas sem 
- O criador de um diretório compartilha sua pasta com um colega e pode retirar esse comparti
- Documentos recebem níveis de classificação definidos centralmente; mesmo o autor do arquiv
- Na autenticação mútua por TLS, a aplicação cliente deve demonstrar o controle da chave cor
- Um administrador deseja manter em uma instalação do Keycloak usuários, papéis, grupos, ses
- Um aplicativo recebe um access token do OAuth 2.0 e conclui que esse token, sem convenção 
- Um serviço de terceiros pede autorização para ler arquivos de uma pessoa sem receber sua s
- Em uma integração com authorization code, o navegador retorna ao endereço registrado levan
- Um programador decodifica os três segmentos de um JWT assinado e conclui que os atributos 
- Uma API recebe JWT com assinatura válida, mas a hora atual já ultrapassou o instante indic
- Uma organização mantém objetos de usuário e grupos no Active Directory, e um sistema consu
- Uma universidade aceita a autenticação de uma aplicação pertencente a outra instituição, p
- Ao revisar uma aplicação, descobre-se que o botão 'Excluir' é oculto para usuários comuns,
- Um aplicativo nativo usa authorization code com PKCE e deseja produzir um verificador conf
- Um cliente público utiliza refresh tokens de longa duração. O projeto precisa reduzir o ap
- Ao enviar authorization code com PKCE S256, um servidor calcula o desafio a partir do veri
- Uma aplicação de agenda recebe um ID Token OIDC destinado a ela e pretende enviá-lo como b
- Um gateway verifica corretamente assinatura e exp de token, mas aceita um JWT emitido para
- Uma aplicação OIDC envia nonce numa solicitação de autenticação e recebe ID Token no fluxo
- Em um realm Keycloak, o grupo 'atendimento' recebe um papel do cliente 'chamados', e suas 
- Um projeto novo cita a RFC 6749 para justificar implicit grant como opção preferencial de 
- Após a contratação, uma identidade recebe conta e permissões; com a mudança de setor, suas
- Quando o proprietário de um arquivo compartilha sua leitura com outro usuário conforme as 
- Em autenticação baseada em certificado digital, apresentar o certificado público e realiza
- Se uma página exige uma senha e, na sequência, a resposta a uma pergunta secreta, o proced
- Uma aplicação que solicita acesso a uma API em nome do titular é chamada cliente, enquanto
- Um access token usado por uma aplicação OAuth 2.0 é necessariamente JWT assinado, de forma
- Em um realm do Keycloak, atribuir um mapeamento de papel a um grupo pode fazer seus membro
- Ao clicar em sair de uma aplicação federada, o usuário provoca, por definição, a revogação
- Um aplicativo público que utiliza PKCE pode gerar um code_verifier aleatório com 64 caract
- Uma aplicação OIDC que solicita autenticação pode omitir o escopo openid e ainda exigir, p
- Um processo automatizado, sem login de pessoa, pode obter access token via client credenti
- Se duas aplicações usam Single Sign-On e compartilham um provedor de identidade, o papel d
- Ao examinar JWT assinado por JWS compacto, o auditor consegue geralmente decodificar os da
- Embora a RFC 6749 descreva o implicit grant, sua descrição original basta para considerá-l
- Em uma API, validar a assinatura do JWT e seu exp sem conferir aud pode permitir que um to
- No OIDC, nonce e state são termos intercambiáveis para o mesmo campo obrigatório do protoc
- Um provedor de identidade pode utilizar LDAP para consultar usuários mantidos em AD DS; ai
- Pelo referencial NIST SP 800-63B-4, uma comparação biométrica isolada é reconhecida como a
- Ao configurar Keycloak para receber autenticação de outro provedor OIDC, o administrador r
- Uma role definida para determinado client no realm A é, por padrão, uma permissão comparti
```

## Pedido 5: MMC, MDC e divisibilidade (`matematica.mmc-mdc-divisibilidade`, 627 tópicos de edital)

```
Matéria: matematica.mmc-mdc-divisibilidade (MMC, MDC e divisibilidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Divisibilidade e fatoração": 46
- "2. MMC, MDC e aplicações": 54

Já existem (não repita):
- Qual é o menor múltiplo comum positivo de 6 e 8?
- Qual é o máximo divisor comum de 18 e 30?
- Qual número é divisível por 3 segundo a soma dos algarismos?
- Qual é a fatoração prima de 84?
- Quantos divisores positivos tem 72=2³×3²?
- Qual é o MDC de 48 e 72?
- Qual é o MMC de 9 e 12?
- Qual alternativa contém um número divisível por 4?
- Pelo critério de 5, qual número é divisível por 5?
- Qual é o MMC de 15 e 20?
- Qual é o MDC de 54 e 90?
- Um número é divisível por 6 quando satisfaz quais condições?
- Dois ciclos repetem-se a cada 14 e 21 dias. Se começam juntos, após quantos dias voltarão 
- Qual é o MDC de 252 e 105 pelo algoritmo de Euclides?
- Uma fita de 84 cm e outra de 120 cm serão cortadas em segmentos iguais, sem sobra, do maio
- Qual é a fatoração prima de 360?
- Qual é o MMC de 18, 24 e 30?
- Qual é o MDC de 96 e 144?
- Em um depósito há 96 itens de um tipo e 144 de outro. Qual o maior número de conjuntos idê
- Dois números positivos têm MDC 12 e MMC 420. Se um número é 60, qual é o outro?
- Qual inteiro é divisível por 11?
- Dois eventos repetem-se a cada 18 e 30 minutos. Começando juntos às 8h, quando coincidem p
- Qual é o MMC de 72 e 90?
- Qual é o MDC de 420 e 588?
- Um salão mede 630 cm por 450 cm. Deseja-se cobrir o piso com placas quadradas iguais do ma
- Qual é o menor inteiro positivo divisível por 12, 15 e 28?
- Dois números têm MDC 15 e MMC 900; um deles é 75. Qual é o outro?
- Um número N é divisível por 2, 3 e 5. Qual é o menor valor positivo possível de N?
- Certo ou errado: todo múltiplo positivo de 12 também é divisível por 3.
- Certo ou errado: 1 é um número primo porque divide todos os inteiros positivos.
- Certo ou errado: 5.832 é divisível por 9 porque a soma de seus algarismos é 18.
- Certo ou errado: o MMC de dois inteiros positivos é sempre pelo menos tão grande quanto ca
- Certo ou errado: o MDC de dois inteiros positivos pode ser maior que o menor deles.
- Certo ou errado: o maior divisor comum de 24 e 40 é 8.
- Certo ou errado: um inteiro divisível por 10 necessariamente termina em zero.
- Certo ou errado: para calcular o MDC por fatoração, usam-se todos os primos presentes em p
- Certo ou errado: se dois ciclos de 8 e 12 horas começam juntos, a primeira coincidência po
- Certo ou errado: como 36=2²×3², ele tem 9 divisores positivos.
- Certo ou errado: o MMC de 8 e 12 é 24 e seu MDC é 4; o produto deles é 96, igual a 8×12.
- Certo ou errado: se um inteiro é divisível por 2 e por 3, então é divisível por 6.
- Certo ou errado: o algoritmo de Euclides termina com MDC(105,252)=42, pois 252 deixa resto
- Certo ou errado: 2.904 é divisível por 11 porque a diferença entre somas alternadas de alg
- Certo ou errado: MDC(84,120)=12 e MMC(84,120)=840, portanto seu produto é 70.560.
- Certo ou errado: ao fatorar 360=2³×3²×5 e 504=2³×3²×7, seu MDC é 72.
- Certo ou errado: o menor múltiplo comum positivo de 9, 12 e 15 é 180.
- Certo ou errado: 438 é divisível por 6, pois termina em algarismo par e sua soma de algari
- Certo ou errado: o maior comprimento que divide exatamente fitas de 54 cm e 90 cm é 18 cm.
- Certo ou errado: o MDC de 126 e 210 é 42.
- Certo ou errado: como 17 não tem divisor primo entre 2 e sua raiz quadrada, 17 é primo.
- Certo ou errado: dois números positivos com MDC 12 e MMC 420 têm produto 5.040.
- Certo ou errado: para cobrir sem recortes um retângulo de 630 cm por 450 cm com quadrados 
- Certo ou errado: o menor inteiro positivo divisível por 12, 15 e 28 é 420.
- Certo ou errado: 2⁴×3² tem 20 divisores positivos.
- Certo ou errado: se três ciclos começam juntos e duram 8, 12 e 18 minutos, o próximo encon
```

## Pedido 6: Medicina do trabalho: fisiologia, agentes e agravos ocupacionais (`medicina.saude-trabalhador-agravos-ocupacionais`, 623 tópicos de edital)

```
Matéria: medicina.saude-trabalhador-agravos-ocupacionais (Medicina do trabalho: fisiologia, agentes e agravos ocupacionais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fisiologia do trabalho, carga e construção do nexo ocupacional": 22
- "2. Ergonomia, organização do trabalho, tecnologias e teletrabalho": 20
- "3. Agentes físicos, químicos e biológicos: higiene e toxicologia": 19
- "4. Agravos ocupacionais por sistemas e investigação clínica": 20
- "5. Epidemiologia ocupacional, vigilância e programas preventivos": 19

Já existem (não repita):
- Após identificar perigos em setor com poeiras, ruído e sobrecarga de tarefas, a equipe org
- Uma organização deseja estruturar consultas e exames relacionados aos riscos identificados
- Trabalhadores relatam exposição a vapores de solventes em uma sala. Para avaliação e contr
- Há queixas persistentes de dores em atividade com força manual, repetição e ritmo acelerad
- Ao analisar histórico de exposição registrado para período trabalhado desde fevereiro de 2
- Uma pessoa sustenta ferramenta pesada com os braços elevados durante minutos, quase sem mo
- Em atividade física intensa com alta umidade do ar, surgem sinais de sobrecarga térmica. Q
- Uma pessoa com asma conhecida fora do trabalho passa a piorar quando atende em ambiente co
- Em atividade com poeira nociva, há possibilidade técnica de substituir o material por outr
- Uma pessoa apresenta dermatite após usar novo desinfetante no serviço. Qual evidência é ma
- Ao revisar riscos psicossociais de equipe sobrecarregada em setembro de 2026, qual interpr
- Após exposição a solvente, mede-se um metabólito específico em amostra biológica. Como int
- Em setor ruidoso, a equipe quer analisar perda auditiva possível. Qual combinação de proce
- Pessoa apresenta lesões nas mãos após contato repetido com detergente alcalino. A pele sof
- Após longo período de manuseio de isolantes antigos contendo amianto, identifica-se mesote
- Em levantamento, os trabalhadores afastados por doença prolongada deixam de ser observados
- Acompanhamento de um ano registra 12 casos novos de agravo entre 200 expostos e 4 entre 20
- Após AET em processo de grande complexidade, surge dúvida sobre conservação do relatório. 
- Equipe revisa regulamento de teletrabalho e encontra no material referência a uma Resoluçã
- Após avaliar concentrações de agente químico, a equipe pretende usar exclusivamente a NR-9
- Trabalhadores com contato crônico com benzeno relatam alterações hematológicas. Qual inves
- Uma equipe pretende estimar dose de ruído reunindo valores em decibéis de atividades com n
- Uma pessoa recebe diagnóstico de neoplasia anos após ter encerrado atividade com exposição
- Investigadores selecionam pessoas com e sem uma doença e reconstruem retrospectivamente a 
- Equipe em rodízio irregular apresenta fadiga e perda de atenção durante madrugadas, apesar
- Em consulta, há dor nos ombros e relato de tarefa sustentada com braços elevados; embora q
- Após exposição ocupacional a ruído, a ausência de queixa auditiva no dia do exame permite 
- Durante avaliação de dores relacionadas ao trabalho, a repetição de um gesto precisa ser a
- No acompanhamento de pessoas que manipulam produto químico, o PCMSO da NR-7 substitui a ne
- Uma análise de registros de doença em determinado dia encontra 18 pessoas adoecidas, inclu
- Ao elaborar documentação previdenciária de período de trabalho iniciado em março de 2023, 
- Ao revisar atividades com pressão temporal e mensagens ininterruptas em outubro de 2026, a
- Quando uma pessoa apresenta metabólito de solvente no sangue, o achado demonstra necessari
- Pessoa com dermatite provocada por limpeza repetida de superfícies pode apresentar mecanis
- Ao investigar mesotelioma identificado vinte anos após cessar contato com amianto, a longa
- Em ambiente com alta carga térmica, a necessidade de prevenção deve considerar intensidade
- Para avaliar o efeito do trabalho sobre uma doença preexistente, é necessário demonstrar q
- Acompanhamento longitudinal em grupos definidos por exposição permite calcular incidência 
- Uma pesquisa selecionou trabalhadores com câncer e sem câncer e investigou exposições ante
- O relatório de Análise Ergonômica do Trabalho, quando realizado, deve permanecer à disposi
- Em avaliação de exposição a ruído, uma média aritmética simples entre níveis expressos em 
- Em investigação de alteração hematológica em pessoa exposta a benzeno, é pertinente estuda
- Por haver regras específicas de teletrabalho na Resolução nº 227/2016, a análise de condiç
- Quando três casos de doença ocorrem entre pessoas que passaram a usar o mesmo produto, o p
- Um estudo transversal que encontra maior prevalência de dor em pessoas expostas a ritmo ac
```

## Pedido 7: BI, data warehouse, OLAP, ETL e mineração de dados (`informatica.bi-mineracao-dados`, 621 tópicos de edital)

```
Matéria: informatica.bi-mineracao-dados (BI, data warehouse, OLAP, ETL e mineração de dados)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Business Intelligence e arquiteturas de dados analíticos": 17
- "2. Modelagem dimensional, fatos, dimensões e histórico": 24
- "3. Integração, ingestão, ETL/ELT e pipelines confiáveis": 15
- "4. OLAP, visualização e desempenho da análise multidimensional": 17
- "5. KDD, CRISP-DM, mineração e aprendizado de máquina": 27

Já existem (não repita):
- Um comércio precisa registrar milhares de compras individuais e, separadamente, comparar a
- Uma empresa reúne bases de diversas filiais, padroniza códigos de produto e preserva regis
- Uma equipe projeta tabela de vendas em que cada linha representa um item de pedido. A desc
- Uma plataforma extrai registros de diversos arquivos, corrige datas e códigos em área inte
- Um painel mostra receita por ano. O usuário abre o ano 2025 e passa a visualizar os quatro
- Uma analista tem dados sem categorias prévias e deseja encontrar grupos de consumidores co
- Uma organização possui tabelas de dados operacionais quase atuais, integradas para consult
- Uma dimensão Cliente muda de região e a empresa quer manter a região que constava na data 
- Um sistema precisa importar só alterações de pedidos ocorridas desde a última carga, inclu
- Uma empresa armazena os registros detalhados em mecanismo relacional e usa consultas sobre
- Um comitê de dados distribui a responsabilidade pelos conjuntos às equipes de seus domínio
- Um analista soma o saldo de cada conta em determinada data e, depois, pretende somar també
- Uma base analítica guarda Produto → Subcategoria → Categoria em três tabelas dimensionais 
- Após falha de rede, um arquivo diário de 10 mil registros é enviado novamente e a rotina i
- Uma consulta apresenta vendas somente para as regiões Norte e Sul, produtos A e B e meses 
- Em 100 transações, 40 possuem produto A e 15 possuem A e B. Para a regra A → B, a confianç
- Um algoritmo encontra conjuntos frequentes de produtos comprados em uma mesma transação e 
- Uma rede deseja cruzar os fatos Vendas e Devoluções por Data e Produto para comparar valor
- Uma consulta mensal costuma somar bilhões de linhas de vendas; o DW mantém visão pré-calcu
- Uma instituição recebe arquivos diários por SFTP, captura atualizações de cadastros por CD
- Um estabelecimento registra 200 compras: 80 incluem A, 50 incluem B e 20 incluem ambos. Pa
- Uma equipe precisa conciliar vendas históricas após mudança do código de cliente entre fon
- Uma empresa escolhe ELT porque a plataforma de destino possui processamento escalável, mas
- Um auditor analítico investiga uma compra muito acima do padrão. O time compara z-score, I
- Uma organização escolhe entre consolidar muitos fatos em tabela única ou manter fatos Vend
- Uma aplicação que registra pagamento de pedidos individualmente atende a uma carga tipicam
- Um data warehouse denominado não volátil não pode receber cargas incrementais ou correções
- Em um esquema estrela, uma tabela fato de vendas pode armazenar o valor vendido e chaves r
- Em um projeto ELT, as principais transformações são obrigatoriamente concluídas em motor s
- Ao selecionar apenas o ano de 2025 em um cubo com eixos Ano, Produto e Região, o usuário r
- K-means é algoritmo de classificação supervisionada cuja principal saída são rótulos previ
- Na modelagem de vendas, a definição de que cada linha registra um item de pedido é anterio
- No SCD tipo 1, a troca da região de um cliente gera obrigatoriamente nova linha histórica 
- Uma estratégia de ingestão por CDC pode transportar eventos de inserção, alteração e exclu
- O drill-through agrega cidade em estado e mês em trimestre, enquanto o roll-up abre docume
- Um índice bitmap pode ser útil em alguns ambientes analíticos para colunas de baixa cardin
- No processo CRISP-DM, a avaliação de modelos deve necessariamente ocorrer antes de qualque
- Uma regra A → B e sua inversa B → A possuem o mesmo suporte conjunto, mas suas confianças 
- Ao manter duas tabelas fato com grãos distintos, uma constelação que utiliza dimensões Dat
- Se um pipeline de ingestão por lotes possui agendamento e checkpoints, sua execução é auto
- Uma tabela mensal pré-agregada pode acelerar consultas de receita por região e mês, mas nã
- Em 100 compras, A ocorreu em 20, B em 50 e A∩B em 10. A regra A → B possui confiança de 50
- Uma abordagem de data mesh pode manter padrões compartilhados de qualidade e acesso ao mes
- Como uma pontuação de LOF ou Isolation Forest indica isolamento estatístico, qualquer tran
- Uma dimensão que adota SCD tipo 2 pode ter múltiplas linhas com a mesma chave natural e ch
```

## Pedido 8: Medicina Legal: traumatologia, tanatologia e sexologia forense (`medicina-legal.traumatologia-tanatologia`, 620 tópicos de edital)

```
Matéria: medicina-legal.traumatologia-tanatologia (Medicina Legal: traumatologia, tanatologia e sexologia forense)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Traumatologia mecânica: instrumentos, lesões e projéteis": 18
- "2. Energias não mecânicas, baropatias e asfixiologia": 22
- "3. Tanatologia: morte, fenômenos cadavéricos e cronotanatognose": 21
- "4. Perinecroscopia, necropsia, exumação e destino do cadáver": 19
- "5. Sexologia, aborto, infanticídio, lesões graves e violência vulnerável": 20

Já existem (não repita):
- Após ferimento com objeto pontiagudo dotado de um gume, observa-se solução de continuidade
- Uma pessoa apresenta mancha violácea sem coleção sanguínea delimitada, causada por infiltr
- Em ferimento por disparo a curta distância, pontos de pólvora ficam impregnados na pele e 
- Uma pessoa é encontrada com laço cervical tensionado por seu próprio peso, embora os pés a
- Paciente permanece em coma profundo com suporte ventilatório, e a equipe considera hipótes
- Um exame necroscópico médico-legal é agendado quatro horas após morte evidente e a equipe 
- Uma vítima ficou impossibilitada de exercer ocupações habituais por 36 dias após agressão 
- Durante atendimento a pessoa que relata violência sexual, exame genital não mostra ruptura
- Um ferimento por projétil de alta velocidade apresenta canal permanente estreito e grande 
- Mergulhador sobe rapidamente e apresenta dor articular e sintomas neurológicos relacionado
- Um indivíduo morre após compressão intensa do tórax que impede sua expansão, mas sem obstr
- Em osso craniano relativamente preservado, há perfuração transfixante com biselamento da t
- Um corpo é encontrado sem rigidez perceptível após intervalo desconhecido, em ambiente que
- Duas pessoas falecem no mesmo desastre, sendo impossível verificar quem morreu primeiro. Q
- Há suspeita relevante após sepultamento de uma pessoa e a autoridade determina novo exame 
- Uma mulher apresenta sangramento e vestígios de produto da concepção após perda gestaciona
- Uma criança apresenta equimoses em locais protegidos e explicação incompatível com seu des
- Após explosão em instalação fechada, uma vítima apresenta ruptura timpânica e contusão pul
- No exame de cadáver há mancha violácea em região dependente que se atenua com pressão e nã
- Peritos encontram cadáver com início de putrefação em local quente, rigidez ausente e livo
- Em morte violenta, as lesões externas permitem precisar a causa e não resta circunstância 
- Após morte com causa não esclarecida, familiares pedem cremação imediata e afirmam que ela
- Exame após agressão demonstra perda funcional parcial permanente de um dedo, mas sem inuti
- Pessoa atendida após relato de violência sexual tem exame sem lesões visíveis e swab negat
- Uma mulher é atendida em serviço de saúde com indícios de violência interpessoal. Qual dis
- Em ferimento por faca pontiaguda, a combinação da ponta penetrante com o gume permite clas
- Em ferimento por disparo próximo, fuligem superficial e grãos de pólvora incrustados na pe
- Uma pessoa encontrada parcialmente apoiada no solo pode sofrer enforcamento quando a força
- A determinação formal de morte encefálica pode ser feita pelo mesmo cirurgião responsável 
- Durante exame cadavérico, fotografar o corpo na posição em que foi encontrado é dever prev
- Lesão corporal que impede ocupações habituais por exatamente trinta dias preenche o requis
- O exame de pessoa que sofreu violência sexual pode ser normal quanto a rupturas genitais v
- Após subida rápida no mergulho, bolhas de gás circulantes causam sintomas neurológicos e a
- Em lesões por projéteis, o trajeto descreve o percurso interno do projétil nos tecidos, en
- Na determinação do intervalo pós-morte, equimose de cor amarelada demonstra necessariament
- A autoridade que determina exumação para exame cadavérico deve providenciar dia e hora pre
- No exame de recém-nascido morto, docimásia hidrostática pulmonar positiva confirma de modo
- Em suspeita de maus-tratos contra pessoa idosa, o art. 19 da Lei 10.741/2003 determina not
- Em morte causada por explosão, cortes por fragmentos de vidro são manifestações caracterís
- Quando a autópsia médico-legal é concluída, o CPP, art. 160, fixa em regra prazo máximo de
- Na classificação didática das fases da morte, a chamada morte aparente é caracterizada por
- Uma agressão provoca debilidade permanente de determinado sentido sem sua perda ou inutili
- Uma pessoa que relata violência sexual procura hospital do SUS sem boletim de ocorrência. 
- Na perícia de morte de recém-nascido, sinais de parto recente na mãe e resultado de docimá
- A notificação de violência sexual no sistema de saúde e a comunicação de indícios de violê
- O período após o parto em que o organismo da mulher retorna ao estado anterior à gestação,
- Pelo Código Civil, presumem-se concebidos na constância do casamento os filhos havidos por
- A inseminação artificial realizada com material genético do próprio marido ou companheiro 
- O estado puerperal, elemento do crime de infanticídio, exige que a mãe mate o próprio filh
```

## Pedido 9: Economia do Setor Público (`economia.economia-setor-publico`, 619 tópicos de edital)

```
Matéria: economia.economia-setor-publico (Economia do Setor Público)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estado, finanças públicas, eficiência e bem-estar": 30
- "2. Bens públicos, recursos comuns e falhas de mercado": 22
- "3. Tributação, equidade, incidência e eficiência": 13
- "4. Política fiscal, resultados, déficit e financiamento": 21
- "5. Dívida sustentável, efeitos econômicos e avaliação das políticas": 14

Já existem (não repita):
- Uma prefeitura financia iluminação de vias nas quais a presença de um pedestre pouco reduz
- Um sistema tributário amplia transferências líquidas a famílias de menor renda sem alterar
- Uma pessoa pode utilizar sinal de defesa coletiva independentemente de contribuir individu
- Pescadores utilizam livremente lago em que o peixe retirado por um deles não estará dispon
- Uma indústria emite poluentes que causam dano externo por unidade fabricada, sem internali
- O custo de uma campanha de vacinação é pago por indivíduos, mas também há redução de contá
- Num mercado competitivo, demanda é pouco sensível ao preço e oferta é bastante elástica. U
- Um governo obteve receitas primárias de 950 unidades monetárias e realizou despesas primár
- O governo registra déficit primário de 40 unidades e juros nominais de 60 unidades, sem ou
- Um projeto público custa 90 unidades hoje e rende benefício certo de 121 unidades daqui a 
- Duas distribuições de recursos são Pareto-eficientes, mas uma concentra praticamente toda 
- Uma oficina ruidosa e seus dois vizinhos podem negociar sem custos, medir danos e firmar c
- Antes de vender automóvel usado, seu proprietário conhece falhas relevantes que compradore
- Num mercado competitivo, as elasticidades em módulo são 0,5 para demanda e 1,5 para oferta
- Num sistema hipotético, a renda de 2.000 unidades paga zero até 1.000 e 20% apenas sobre a
- Um relatório demonstra que o resultado fiscal foi apurado com receitas primárias e despesa
- O saldo primário de um período foi superavitário em 70 unidades, enquanto os juros nominai
- Uma economia satisfaz competição perfeita, convexidade e demais pressupostos usuais dos te
- Uma unidade adicional de bem público puro oferece benefícios marginais individuais de 12, 
- Um analista propõe tributar mais fortemente produtos básicos com demanda inelástica, afirm
- A razão dívida/PIB inicial é 60%, juros reais são 5% e crescimento real é 2%. Sem ajustes 
- O estoque de dívida pública líquida subiu 150 unidades durante o ano. Um técnico afirma qu
- O governo troca redução tributária presente por emissão de dívida que será paga com tribut
- Uma rede de infraestrutura possui custo médio decrescente na faixa de demanda relevante. O
- Em economia aberta simplificada, uma decisão fiscal reduz o saldo público em 20 unidades, 
- Uma política financiada por gasto público melhora o acesso a serviço coletivo subofertado 
- Uma economia é Pareto-eficiente somente quando seus habitantes possuem rendas iguais, pois
- Uma plataforma de acesso restrito a assinantes, com capacidade suficiente para que o uso d
- Uma externalidade negativa de produção, não internalizada, faz o custo marginal social fic
- Se receitas primárias somam 500 unidades e despesas primárias 480, há superávit primário d
- Em mercado competitivo, um imposto entregue formalmente pelo comerciante necessariamente s
- Com direitos de propriedade definidos e barganha sem custos de transação, partes atingidas
- Num monopólio natural de rede, economias de escala tornam a atuação regulatória economicam
- Em mercado competitivo, demanda de elasticidade absoluta 0,4 e oferta de elasticidade 1,6 
- Em mercado competitivo sem externalidades, a arrecadação tributária integral de um imposto
- Uma recessão pode deteriorar o saldo fiscal mesmo sem medidas discricionárias, porque rece
- Senhoriagem e imposto inflacionário são conceitos exatamente idênticos em qualquer regime 
- Uma dívida captada por emissão de títulos é classificada como mobiliária; quando decorre d
- Se r e g forem constantes, r superar g e houver dívida positiva, um déficit primário persi
- Uma obra pode satisfazer o critério de compensação potencial de Kaldor-Hicks sem ser melho
- Para estimar a disposição social a pagar por uma unidade adicional de bem público puro, so
- Em modelo tributário simplificado de Ramsey, bases menos elásticas podem receber maiores a
- As metodologias de apuração fiscal acima e abaixo da linha devem produzir números rigorosa
- Um investimento público em logística pode reduzir custos privados e atrair novos projetos 
- Em inflação elevada, o atraso entre fato gerador e arrecadação tributária não corrigida pr
```

## Pedido 10: Lei 8.112/1990: regime disciplinar (`legislacao.lei-8112-1990-regime-disciplinar`, 616 tópicos de edital)

```
Matéria: legislacao.lei-8112-1990-regime-disciplinar (Lei 8.112/1990: regime disciplinar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Deveres funcionais e comunicação de irregularidades": 28
- "2. Proibições do art. 117 e enquadramento das condutas": 22
- "3. Acumulação de cargos e a atualização constitucional": 15
- "4. Responsabilidade civil, penal e administrativa": 19
- "5. Penalidades, consequências e prescrição": 16

Já existem (não repita):
- Uma chefia determina que determinado registro oficial seja deliberadamente alterado para e
- Durante atendimento, uma pessoa solicita dados que estão comprovadamente protegidos por si
- Um servidor toma conhecimento, no exercício de suas funções, de irregularidade que aparent
- Em unidade administrativa, um servidor mantém sob sua chefia imediata, em função de confia
- Uma servidora compra pequena participação de sociedade privada, sem exercer gestão, admini
- O responsável por material público emprega equipamentos da repartição em um empreendimento
- Dois vínculos ocupados por servidor estão entre as combinações permitidas na Constituição.
- Em 2026, um professor em vínculo público recebe proposta para ocupar outro cargo administr
- A mesma conduta funcional provoca prejuízo patrimonial, transgressão administrativa e susp
- Em julgamento criminal definitivo, a decisão declara de modo categórico que a conduta atri
- Em estudo da Lei 8.112/1990, um candidato precisa identificar todas as espécies de penalid
- Por reincidência em falta antes punida com advertência, avalia-se suspensão comum de servi
- Um servidor recusa, sem justificativa, inspeção médica determinada pela autoridade compete
- Uma advertência escrita foi aplicada e, nos três anos seguintes de efetivo exercício, não 
- O registro de frequência aponta ausência ininterrupta e intencional por 31 dias, sem qualq
- Uma pessoa faltou injustificadamente ao trabalho em 60 dias não consecutivos distribuídos 
- Notificado de acumulação ilegal, um servidor não faz opção nos dez dias iniciais, mas esco
- Um servidor acumula licitamente dois cargos efetivos e assume cargo em comissão. Ele demon
- Após causar dano patrimonial a terceiro em serviço, o servidor falece e deixa herdeiros qu
- Uma demissão enquadra-se em um dos incisos I, IV, VIII, X e XI do art. 132. O texto litera
- Um fato disciplinar, sem enquadramento também criminal, é punível em tese com suspensão. A
- Uma falta sujeita a demissão chega ao conhecimento da autoridade competente. Depois, ocorr
- Foi proposta suspensão de 45 dias por infração disciplinar. Sem discutir o rito, qual níve
- Em uma situação, atribuição oficial é confiada a pessoa externa à repartição sem previsão 
- Uma demissão é fundamentada em aplicação irregular de dinheiros públicos. Além da previsão
- Ao identificar possível abuso de poder, o servidor apresenta representação pela via hierár
- Um servidor recusa atualizar seus dados cadastrais após solicitação formal; por se tratar 
- Dois cargos públicos privativos de profissionais da saúde com profissões regulamentadas po
- Uma servidora condenada administrativamente por falta funcional deverá necessariamente sof
- A Administração aplica advertência a servidor que descumpriu dever funcional de maneira qu
- Em controle de sanção disciplinar, a autoridade utiliza o prazo de cinco anos do art. 131 
- Um servidor que conhece indícios de crime funcional comunica a autoridade competente, afas
- Um servidor investido em cargo efetivo participa como mero cotista de sociedade privada; e
- Dois vínculos públicos ocupados em entidades de esferas federativas diferentes também deve
- Quando se constata acumulação ilegal e a pessoa faz opção até o último dia da defesa, a bo
- Depois de se aposentar, o servidor tem apurada falta grave praticada quando ainda estava e
- Para reduzir a ausência de pessoal, a autoridade converte suspensão em multa, manda o serv
- Uma pessoa sem vínculo efetivo, nomeada em cargo em comissão, pratica infração que seria p
- Ao examinar acumulação de cargo de professor com cargo administrativo em 2026, a autoridad
- A autoridade considera a natureza da infração, seus danos, antecedentes e circunstâncias a
- A decisão criminal definitiva absolve servidor apenas por prova insuficiente para condená-
- Mesmo que o art. 137, parágrafo único, ainda contenha redação de proibição de retorno sem 
- Em investigação disciplinar, qualquer conversa entre colegas sobre um fato inaugura a pres
- Se a conduta funcional imputada ao servidor estiver também capitulada como crime, o art. 1
- Um servidor comete ausência injustificada e intencional por exatamente trinta dias consecu
```

## Pedido 11: Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção e ação popular (`processo-civil.remedios-constitucionais-mandado-seguranca-habeas`, 616 tópicos de edital)

```
Matéria: processo-civil.remedios-constitucionais-mandado-seguranca-habeas (Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção e ação popular)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Habeas corpus, habeas data e mandado de injunção": 21
- "2. Mandado de segurança individual e coletivo: Lei 12.016/2009": 28
- "3. Ação popular, ação civil pública e tutelas de urgência": 20
- "4. Microssistema de processo coletivo, coisa julgada e execução": 19
- "5. Recursos cíveis e métodos consensuais na Administração": 12

Já existem (não repita):
- Uma pessoa recebeu ameaça concreta de encarceramento ilegal e quer impedir que a ordem sej
- Alguém pediu acesso a informações próprias em registro de caráter público, recebeu negativ
- Uma norma constitucional depende de regulamentação inexistente e isso inviabiliza concreta
- Uma decisão de autoridade negou direito cujos fatos estão integralmente demonstrados por d
- Uma pessoa teve ciência formal de ato lesivo e pretende impetrar mandado de segurança repr
- Um eleitor pretende contestar ato lesivo ao patrimônio público e à moralidade administrati
- Um dano ambiental alcança moradores indeterminados unidos por circunstâncias de fato, e a 
- Uma sentença de primeiro grau em processo civil comum é impugnada pela parte vencida. Qual
- Uma condenação na ACP é pecuniária e visa reparar dano a bem difuso. Em regra, a indenizaç
- Uma pessoa pede administrativamente correção de dado incorreto em cadastro público, sem re
- Reconhecida mora regulamentar em mandado de injunção, o julgador pretende permitir exercíc
- Um juiz analisa liminar em mandado de segurança e alguém invoca os arts. 7º, § 2º, e 22, §
- Uma associação legalmente constituída e em funcionamento há três anos pretende defender di
- O autor cidadão de ação popular desiste e os editais legais são publicados para permitir o
- Em ACP, uma liminar impôs multa diária e o réu descumpriu a obrigação; ainda não houve trâ
- Uma demanda coletiva versa sobre consumidores com danos individuais de origem comum; algué
- Uma decisão judicial omitiu análise de ponto sobre o qual devia pronunciar-se. Qual recurs
- Em reclamação trabalhista, tutela provisória foi deferida por decisão interlocutória antes
- Em uma ação civil pública de repercussão nacional, a defesa pede que a sentença beneficie 
- Uma associação autora obteve sentença condenatória em ACP transitada em julgado, mas deixo
- Uma ação popular é julgada improcedente especificamente por falta de prova suficiente da l
- Uma parte dispõe de recurso administrativo contra determinado ato, dotado de efeito suspen
- Após liminar em MS, uma pessoa jurídica de direito público sustenta risco concreto de grav
- Uma entidade de classe ajuizou MI coletivo por omissão legislativa que afeta parte dos mem
- Durante contratação regida pela Lei 14.133/2021, surge disputa sobre cálculo de indenizaçã
- Diante de ameaça concreta à liberdade de locomoção causada por ordem ilegal ainda não exec
- A Súmula Vinculante 25 permite prisão civil do depositário infiel judicial, embora impeça 
- O habeas data é instrumento de acesso e retificação de informações sobre a pessoa do impet
- Direito líquido e certo autoriza mandado de segurança ainda que os fatos constitutivos dep
- A Constituição, art. 5º, LXXIII, permite a cidadão ajuizar ação popular contra ato lesivo 
- A ação civil pública da Lei 7.347/1985 só pode ser ajuizada pelo Ministério Público, pois 
- O mandado de injunção também pode enfrentar regulamentação parcial insuficiente, desde que
- Após a ADI 4.296, permanece condição obrigatória ouvir por setenta e duas horas a pessoa j
- Ao receber a inicial de mandado de segurança, o juiz determina a notificação da autoridade
- Na Lei 12.016/2009, a sentença de mandado de segurança pode impor honorários advocatícios 
- Em inquérito civil, o Ministério Público pode colher informações e provas para avaliar tut
- Direitos individuais homogêneos, por apresentarem origem comum, tornam-se juridicamente in
- O CPC permite embargos de declaração em cinco dias para sanar omissão, obscuridade, contra
- A impetração de mandado de segurança coletivo produz litispendência automática com todos o
- No Tema 1075, o STF reputou inconstitucional a limitação territorial da coisa julgada da a
- Na ACP, a multa estabelecida em liminar é imediatamente exigível mesmo quando o réu cumpri
- O art. 104 do CDC afasta litispendência automática entre a ação coletiva indicada na norma
- Quando o cidadão autor desiste de ação popular, a lei impede que outro cidadão ou o Minist
- A Lei 13.140/2015, art. 32, permite que entes federados criem câmaras de prevenção e resol
- A taxatividade mitigada fixada no Tema 988 do STJ transforma toda decisão interlocutória d
```

## Pedido 12: Higiene ocupacional e ergonomia (`seguranca-trabalho.higiene-ocupacional-ergonomia`, 602 tópicos de edital)

```
Matéria: seguranca-trabalho.higiene-ocupacional-ergonomia (Higiene ocupacional e ergonomia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos da higiene ocupacional e gerenciamento dos riscos": 19
- "2. Agentes físicos, medição e Normas de Higiene Ocupacional": 18
- "3. Agentes químicos, biológicos, ventilação e qualidade do ar": 19
- "4. Ergonomia, biomecânica, antropometria e organização do trabalho": 22
- "5. Avaliação ergonômica, AET e integração das medidas de prevenção": 22

Já existem (não repita):
- Durante a inspeção de uma oficina, identifica-se um recipiente aberto com solvente e traba
- Uma indústria atualiza sua documentação preventiva e precisa identificar qual programa mat
- No reconhecimento de uma área de moagem há partículas sólidas suspensas, calor intenso e o
- Em uma fábrica, o ruído varia entre períodos de operação e paradas. Para conhecer a exposi
- Um trabalhador opera ferramenta que vibra em suas mãos. O técnico busca a Norma de Higiene
- Em um laboratório, vapores são emitidos de uma bancada sempre que o frasco é aberto. Prete
- Um edifício de uso coletivo possui climatização artificial e necessita manter seus sistema
- Durante o projeto de uma bancada para pessoas de diferentes estaturas, a equipe decide usa
- Em um setor com movimentação repetida de caixas, a avaliação preliminar registrou fatores 
- A avaliação preliminar de uma atividade com produto químico revela vazamento visível e exp
- Em uma jornada com ruído contínuo de 90 dB(A), o técnico consulta a tabela do Anexo 1 da N
- Uma equipe irá avaliar partículas respiráveis geradas por lixamento e precisa distinguir c
- Um processo apresenta exposição a substância química cujo limite de tolerância aplicável, 
- No manuseio de material potencialmente infectante, não há resultado de concentração ambien
- Uma empresa realizou AEP e instalou dispositivos de auxílio no posto. Meses depois, o acom
- Na movimentação manual de caixas iguais, a força e a massa não mudam, mas uma caixa passa 
- Uma empresa encontra em um manual antigo a Resolução-RE nº 9/2003 para qualidade do ar e d
- Uma avaliação diária de vibração em mãos e braços produziu aren de 3,0 m/s². Segundo o Ane
- Uma pessoa permaneceu quatro horas a 85 dB(A) e duas horas a 90 dB(A), sem outras exposiçõ
- O resultado de amostragem de solvente foi obtido exclusivamente em um período de parada de
- Uma linha de montagem dobra sua velocidade, mantendo os mesmos produtos e máquinas. Surgem
- Um técnico avalia um trabalhador sob radiação térmica e atividade muscular intensa. A temp
- Uma empresa pequena, enquadrada em grau de risco 1, alega estar dispensada de todas as med
- A empresa planeja reduzir sintomas associados à repetição de tarefas. A proposta prevê gin
- Em relatório de exposição ao ruído, um analista aplica aumento de 3 dB para dobrar a dose 
- Em um setor com contato com ruído e vapores, a equipe inclui, no inventário de riscos do P
- Em uma inspeção de área administrativa, o mapa visual de riscos disponível pode substituir
- Uma instalação utiliza luxímetro para avaliar a iluminância de uma superfície de trabalho;
- Na substituição de um exaustor defeituoso, instalar um ventilador de recirculação comum as
- Em um posto de montagem, adaptar alturas e alcances às diferentes dimensões corporais dos 
- Após executar uma avaliação ergonômica preliminar, a empresa só precisa registrá-la caso t
- Durante amostragem de vapores com bomba individual, a verificação da vazão e do tempo de c
- Um trabalhador realiza jornada de oito horas sob ruído constante de 90 dB(A). Pela tabela 
- Em uma tarefa com partículas e vapores, a NR-9 admite que a análise preliminar determine c
- Um profissional identifica agente biológico em material contaminado, mas conclui que a ava
- Na investigação de sobrecarga em linha de produção, o ritmo imposto, as exigências de temp
- Uma organização sustenta que a AET somente é exigida quando uma avaliação preliminar ident
- A verificação de riscos ergonômicos associados ao ritmo de produção deve ser articulada co
- Numa avaliação de mãos e braços, a aceleração normalizada diária de 2,0 m/s² supera o níve
- No caso de dois períodos distintos de ruído contínuo em uma jornada, a análise conforme o 
- Uma coleta química realizada somente durante uma parada, sem verificar diferença para a pr
- Ao redesenhar uma bancada, um projeto pode usar limites antropométricos distintos para alc
- Em um edifício coletivo climatizado, a revogação da Resolução-RE nº 9/2003 pela RDC nº 886
- Uma AET identifica que a principal causa da sobrecarga não é a cadeira, mas o abasteciment
- Em 2026, a abordagem de fatores de risco psicossociais relacionados ao trabalho, incluída 
```

## Pedido 13: Bens públicos (`direito-administrativo.bens-publicos`, 598 tópicos de edital)

```
Matéria: direito-administrativo.bens-publicos (Bens públicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceito, classificação, domínio público e afetação": 26
- "2. Regime jurídico, aquisição, alienação e proteção patrimonial": 23
- "3. Vias, cemitérios, portos e formas de uso por terceiros": 22
- "4. Terrenos de marinha, acrescidos, foro, ocupação e laudêmio": 16
- "5. Gestão avançada, direitos reais de uso e casos controvertidos": 13

Já existem (não repita):
- Uma autarquia incorpora por registro um imóvel destinado a seu arquivo. Segundo o art. 98 
- Uma rua municipal aberta à circulação de pedestres e veículos é objeto de estudo de classi
- O município possui terreno vazio, regularmente registrado em seu nome, sem destinação a lo
- Uma escola municipal funciona em prédio próprio e a administração cogita vendê-lo sem cess
- Uma pessoa ocupa terreno dominical municipal por trinta anos e pede reconhecimento judicia
- Uma lei local institui cobrança para uso de instalação recreativa vinculada a um parque pú
- Uma prefeitura cede por ato precário a um ambulante espaço definido em praça para venda or
- Uma instalação funerária municipal organizada como cemitério público dispõe de jazigos con
- Em terminal de cargas de um porto público, o acesso ao pátio é limitado por requisitos de 
- Entidade pública pretende outorgar a particular um direito real resolúvel sobre terreno pa
- Após desafetação válida de terreno de uso especial, o município propõe venda por alienação
- Um município preenche requisitos civis para adquirir por usucapião um terreno que, até ent
- Um particular ocupou indevidamente lote público, construiu pequena edificação e, ao ser re
- Na demarcação de terreno de marinha da União, um técnico utiliza o Decreto-Lei 9.760/1946.
- Em margem de corrente navegável fora da influência das marés, discute-se o conceito legal 
- Um imóvel federal encontra-se regularmente sob regime de ocupação e seu terreno, excluídas
- Um terreno de marinha aforado tem valor de domínio pleno do solo de R$ 300.000,00, sem con
- Um foreiro vende onerosamente seu domínio útil de terreno da União cujo solo vale R$ 400.0
- Após transmissão onerosa de domínio útil de terreno da União, o adquirente pretende atuali
- A União autoriza, presentes requisitos patrimoniais, que foreiro adquira o domínio direto 
- Uma sociedade de economia mista presta serviço público essencial sem atuação concorrencial
- Uma praia marítima possui acesso habitual de moradores, mas empreendimento vizinho pretend
- A administração federal avalia vender imóvel dominical da União. Além das regras gerais de
- Um concessionário de direito real de uso recebeu terreno para edificação de interesse soci
- Pessoa falece sem herdeiros conhecidos, e determinado imóvel particular passa por arrecada
- Uma praça municipal destinada à circulação e à permanência coletiva é, nos termos do art. 
- Um lote municipal dominical, por não estar afetado a serviço, pode ser usucapido por ocupa
- Um prédio próprio de autarquia em uso para atendimento institucional é bem de uso especial
- O uso comum de um bem público deve necessariamente ser gratuito, pois o art. 103 do Código
- Para instalar estrutura temporária em praça, a administração pode valer-se de autorização 
- Para terrenos de marinha, o art. 2º do Decreto-Lei 9.760/1946 determina faixa de quinze me
- Aforamento de terreno da União pode atribuir ao particular domínio útil, mantendo com a Un
- A retirada legítima da destinação de uma escola pública pode converter seu imóvel em domin
- Bem público dominical, embora sujeito a alienação nas condições da lei, segue submetido à 
- A Lei 14.133/2021, art. 76, dispensa interesse público justificado e avaliação prévia semp
- Em cemitério público destinado a serviço funerário, a outorga de uso individual de um jazi
- A instalação operacional de um porto público deve ser classificada como bem dominical apen
- Conforme o art. 3º do Decreto-Lei 9.760/1946, acrescidos de marinha abrangem formações nat
- A taxa anual ordinária de ocupação de terreno da União, pelo art. 1º do DL 2.398/1987, é d
- O art. 3º do Decreto-Lei 2.398/1987, em transferência onerosa do domínio útil de terreno d
- Depois de transferir onerosamente o domínio útil de imóvel federal, o adquirente tem prazo
- O art. 7º do Decreto-Lei 271/1967 admite concessão de direito real de uso com finalidade e
- A orientação do STJ no REsp 2.173.088/DF permite usucapião de imóvel de sociedade de econo
- A pessoa jurídica de direito público pode adquirir imóvel anteriormente particular por usu
- A aquisição de imóvel pela administração por herança jacente se completa na data da morte 
```

## Pedido 14: Lei Anticorrupção e acordo de leniência (`direito-administrativo.improbidade-anticorrupcao`, 592 tópicos de edital)

```
Matéria: direito-administrativo.improbidade-anticorrupcao (Lei Anticorrupção e acordo de leniência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, sujeitos e alcance da Lei Anticorrupção": 22
- "2. Atos lesivos, sanções e programas de integridade": 21
- "3. Investigação e processo administrativo de responsabilização": 19
- "4. Acordo de leniência: requisitos, negociações e efeitos": 19
- "5. Responsabilização judicial, cadastros e prescrição": 19

Já existem (não repita):
- Uma sociedade contratou representante que ofereceu vantagem indevida para obter decisão fa
- Uma associação sem fins lucrativos, regularmente constituída, utiliza recursos para financ
- Em negociação de contrato público, uma empresa promete quantia indevida ao familiar de age
- No encerramento de um PAR, a autoridade examina as sanções administrativas possíveis para 
- Após instauração de PAR contra sociedade, a comissão comunica sua indiciação e concede opo
- Uma empresa firmou acordo de leniência válido, colaborou e cumpriu as obrigações pactuadas
- Duas empresas com sanções aplicadas em regimes distintos são consultadas em bases nacionai
- Ao instaurar processo contra empresa por fraude, autoridade designa apenas um servidor est
- Uma empresa é apurada por infração instantânea que somente veio ao conhecimento da adminis
- Uma sociedade incorporou outra que praticara ato lesivo antes da operação. Não há prova de
- Uma controladora e sua controlada estão vinculadas a ato lesivo praticado pela sociedade o
- Uma organização empresarial, sem faturamento utilizável no período legal, foi considerada 
- A autoridade máxima delegou a outra autoridade a instauração e o julgamento de um PAR. A p
- Uma organização adotou programa de integridade anterior à irregularidade, cuja efetiva apl
- Durante negociações federais de leniência, a proposta escrita da empresa é rejeitada. Post
- Uma empresa reparou voluntariamente parte do prejuízo causado, mas permanece sujeita ao pr
- Em auditoria, uma empresa apresenta código de ética e provas de treinamento, mas não possu
- Após proposta escrita de leniência admitida, a CGU e a empresa assinam memorando de entend
- Em ação judicial contra pessoa jurídica que fraudava reiteradamente contratações, prova-se
- A autoridade administrativa competente deixou de promover responsabilização de empresa ape
- Uma pessoa jurídica integrante de consórcio praticou ato lesivo no âmbito do contrato cons
- No cálculo regulamentar federal da multa de empresa já sancionada por ato lesivo, constata
- Uma sociedade brasileira paga vantagem indevida a representante sem remuneração de uma org
- Duas integrantes de grupo econômico foram beneficiadas por infração. Apenas uma assina aco
- A empresa descumpre injustificadamente acordo de leniência federal, cuja rescisão administ
- Uma sociedade oferece, por intermediário, vantagem indevida a pessoa relacionada a agente 
- Após a responsabilização objetiva de uma empresa, presume-se que seus administradores deva
- No julgamento administrativo de pessoa jurídica por ato lesivo, a autoridade pode aplicar 
- Depois de intimada no PAR, a pessoa jurídica investigada dispõe de 180 dias para apresenta
- Uma empresa sancionada administrativamente pela Lei nº 12.846/2013 poderá ter informações 
- Uma empresa que cumpriu seu acordo de leniência fica dispensada de reparar os danos apurad
- Uma pessoa jurídica brasileira praticou no exterior ato lesivo contra a administração de o
- A autoridade máxima de uma entidade pode delegar a competência para instaurar e julgar PAR
- Para assegurar o pagamento da multa e a reparação integral do dano, a representação judici
- Uma empresa que possui código de ética escrito faz jus automaticamente à subtração máxima 
- Se a personalidade jurídica foi utilizada abusivamente para encobrir atos lesivos e isso f
- Uma proposta de leniência rejeitada passa a constituir, pelo próprio ato de rejeição, reco
- Quando não é possível utilizar o faturamento bruto da pessoa jurídica como critério de mul
- Em ação judicial por fraude contratual isolada, a administração pede dissolução compulsóri
- Uma contratada altera fraudulentamente informações de custos para manipular o equilíbrio e
- Uma infração continuada cessa em determinada data, mas a administração só toma conheciment
- Um PAR instaurado por entidade do Executivo federal apresenta irregularidades de tramitaçã
- Diante de fraude em benefício de sociedade controlada, a controladora pode ser solidariame
- Comprovada omissão das autoridades competentes em promover a responsabilização administrat
- Uma vez assinado o memorando de entendimentos em negociação federal de leniência, o prazo 
```

## Pedido 15: Formação, suspensão e extinção do processo; julgamento conforme o estado do processo e saneamento (`processo-civil.formacao-suspensao-extincao`, 591 tópicos de edital)

```
Matéria: processo-civil.formacao-suspensao-extincao (Formação, suspensão e extinção do processo; julgamento conforme o estado do processo e saneamento)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Relação jurídica processual, pressupostos, procedimento e atos": 17
- "2. Formação da demanda, citação e suspensão do processo": 12
- "3. Extinção sem e com resolução do mérito": 22
- "4. Revelia, providências preliminares e julgamentos antecipados": 30
- "5. Saneamento, organização probatória e audiência de instrução": 19

Já existem (não repita):
- Uma pessoa protocola petição inicial em 2 de março e o réu só é validamente citado em 24 d
- Uma inicial foi protocolada e a pessoa demandada ainda não recebeu citação. Qual evento é 
- Uma demanda cível não possui procedimento especial previsto no CPC ou em lei específica. Q
- O juiz encontra irregularidade sanável na representação do autor logo no início da demanda
- Autor e réu convencionam suspender o processo para tentar uma negociação, sem enquadrament
- Uma sentença extingue demanda porque falta interesse processual ao autor, sem examinar se 
- Em audiência, o autor renuncia expressamente à pretensão material discutida e o juiz homol
- O réu deixa de contestar ação de direito disponível sem incidência de exceções e a inicial
- Depois de contestação, o autor é ouvido para responder a alegação de pagamento feita pelo 
- O réu foi citado e o processo ainda não chegou ao saneamento. O autor pretende mudar a cau
- O desfecho de cobrança depende do julgamento de ação sobre a existência da relação jurídic
- Durante suspensão por força maior, surge risco de perecimento de documento essencial à cau
- Após contestação, o autor abandona causa por 40 dias, é intimado pessoalmente e não reage 
- Após três sentenças extinguindo ações idênticas por abandono imputável ao autor, ele tenta
- Um réu revel foi citado e não contestou causa sobre direito indisponível. Que efeito decor
- Uma ação contém dois pedidos. O réu reconhece integralmente o primeiro e contesta o segund
- Após decisão de saneamento, parte pretende solicitar esclarecimento sobre a delimitação de
- O juiz detecta que representação do recorrente é irregular já no tribunal e concede prazo 
- No processo civil, mérito depende de questão sobre fato delituoso. O juiz suspende, mas ne
- Após o réu apresentar contestação, o autor pede homologação de desistência da ação antes d
- O juiz percebe que o réu alegou incompetência na contestação. O autor deve ser ouvido ante
- O julgamento antecipado parcial reconhece obrigação ilíquida e a parte contrária interpõe 
- O processo contém controvérsia técnica complexa, não cabendo decisão antecipada. Como o ju
- Em decisão de saneamento, o juiz determina prova testemunhal e fixa rol de dez pessoas par
- Finda a instrução de causa complexa, o juiz decide substituir debates orais por razões fin
- Uma ação é protocolada na secretaria judicial antes da citação do demandado; o processo se
- Uma causa tramita pelo procedimento comum e a parte argumenta que processo e procedimento 
- O juiz verifica incapacidade processual sanável e determina correção com prazo razoável an
- Autor e réu convencionam suspensão do processo por nove meses e sustentam que o CPC não li
- Após defesa, o juiz reconhece prescrição e resolve o mérito nos termos do art. 487, II, do
- O réu é revel em ação de direito indisponível e, por força do art. 344, todos os fatos ale
- Antes da citação, o autor decide aditar o pedido e a causa de pedir, sem concordância do r
- Durante suspensão por arguição de impedimento ou suspeição, o juiz pode realizar qualquer 
- Uma causa é suspensa por depender de verificação de fato delituoso, e nenhuma ação penal é
- O autor mantém a causa sem diligência de sua responsabilidade por quarenta dias e, após co
- Após contestação, o réu apresenta fato extintivo do direito material alegado na inicial. O
- O juiz julga antecipadamente apenas parcela incontroversa do pedido e a decisão deve ser i
- Em decisão de saneamento, o juiz delimita os fatos controvertidos, define a distribuição d
- Se o autor der causa a três sentenças por abandono, a perempção impedirá que volte a alega
- O réu revel comparece após encerrada a contestação, mas antes da prova testemunhal, e pass
- O juízo reconhece pedido como incontroverso e profere decisão parcial de mérito. Por exist
- A causa contém controvérsia fática e jurídica de complexidade relevante. Pelo art. 357, §3
- O juiz redistribui o ônus da prova depois de encerrada a instrução e dispensa justificar a
- Na audiência de instrução, o art. 361 prevê ordem preferencial para esclarecimentos de per
- Encerrada a instrução de causa complexa, o juiz substitui debates orais por razões finais 
```

## Pedido 16: Fundamentos do processo penal: princípios, sistemas e relação processual (`processo-penal.principios-sistemas-fundamentos`, 591 tópicos de edital)

```
Matéria: processo-penal.principios-sistemas-fundamentos (Fundamentos do processo penal: princípios, sistemas e relação processual)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Conceitos, finalidade e relação jurídica processual": 28
- "2. Fontes e aplicação da lei processual penal": 18
- "3. Princípios e direitos fundamentais do processo penal": 23
- "4. Sistemas processuais e juiz das garantias": 18
- "5. Pressupostos, condições da ação e validade processual": 13

Já existem (não repita):
- Em debate sobre o alcance do processo penal, alguém afirma que ele somente serve para conc
- Após o recebimento de uma acusação, surge discussão sobre a sequência de citação, defesa e
- Uma lei altera o modo de realização de audiências depois de um depoimento regularmente col
- Um acusado, mesmo ausente, não constituiu advogado durante a ação penal. Segundo o CPP, co
- O julgador pretende investigar diretamente e construir a tese acusatória, substituindo que
- Um processo penal tramita no território brasileiro e a defesa pede aplicação automática do
- Em audiência, o acusado optou por permanecer calado sobre os fatos. A acusação pretende tr
- Uma acusação é apresentada, mas carece de qualquer indício mínimo de autoria e materialida
- O juízo de instrução recebe os autos depois de a denúncia ter sido oferecida e encontra ca
- Um regulamento afirma que uma lei estadual pode criar livremente normas gerais de processo
- Um examinador descreve um sistema em que o mesmo sujeito concentra investigação, acusação 
- Durante julgamento, a acusação pede condenação somente com base em entrevistas registradas
- A defesa requer rejeição da denúncia por total ausência de elementos mínimos de autoria, e
- Em ação penal, foram demonstradas oportunidades suficientes para defesa técnica, mas o nov
- Um magistrado é designado especificamente depois do fato para julgar determinado acusado, 
- Após oferecida denúncia em um feito sujeito ao juiz das garantias, a acusação argumenta qu
- Em fase judicial, o juiz identifica dúvida relevante e ordena, nos limites legais, diligên
- Uma lei processual recente introduz uma exigência de realização de determinado ato, mas o 
- Um processo do tribunal do júri chega à fase investigativa. Com base na interpretação fixa
- O titular da acusação pede que o juiz criminal condene porque a denúncia foi recebida e ha
- Após a investigação, a acusação oferece uma queixa em feito submetido ao regime do juiz da
- Durante a investigação com pessoa presa, o prazo do inquérito esgota-se, mas existem razõe
- A acusação oferece peça incapaz de descrever minimamente qual conduta foi atribuída ao acu
- Uma sentença penal baseia-se decisivamente em prova ilícita e sua derivação direta. Qual d
- Em ação penal iniciada após a alteração do art. 310 do CPP pela Lei nº 15.358/2026, discut
- Após a denúncia ser apresentada, o acusado recebe citação regular; somente com essa citaçã
- Depois de uma lei procedimental nova entrar em vigor, uma audiência já realizada validamen
- Em processo por ação penal privada, a iniciativa da acusação pode pertencer ao legitimado 
- Diante da recusa do acusado em responder às perguntas incriminadoras, o juiz pode valorar 
- Ao apreciar pedido de interceptação apresentado durante uma investigação abrangida pelo ju
- Um acusado que está foragido pode ser julgado sem defensor técnico, porque não compareceu 
- Por possuir função de investigar, a autoridade responsável pela investigação pode ela próp
- Uma acusação sem suporte informativo mínimo de autoria pode ser rejeitada por falta de jus
- Quando uma diligência de investigação precisa permanecer temporariamente sigilosa para pre
- Uma vez que o CPP admite analogia no art. 3º, o juiz pode empregar essa técnica para criar
- Segundo o entendimento firmado nas ADIs sobre o juiz das garantias, sua competência ordiná
- Uma acusação pública foi proposta por sujeito sem legitimidade ativa; o defeito pode enqua
- Com a implantação do juiz das garantias, os autos do inquérito devem necessariamente perma
- Se a lei classificada como processual também alterar diretamente efeitos materiais da situ
- Em processo submetido ao juiz das garantias, a falta de conclusão do inquérito em uma únic
- A ausência completa de defensor durante o julgamento é nulidade absoluta; a deficiência de
- Como a Constituição admite publicidade processual, qualquer restrição da divulgação de dad
- Uma hipótese de processo de competência originária de tribunal, regida pela Lei nº 8.038/1
- Após a Lei nº 15.358/2026, a audiência de custódia por videoconferência em tempo real disp
- Para resolver alegação de nulidade, é pertinente identificar o direito atingido e o prejuí
```

## Pedido 17: Lei: vigência, aplicação no tempo e no espaço, integração e interpretação (`direito-civil.lei-vigencia-aplicacao-integracao-interpretacao`, 590 tópicos de edital)

```
Matéria: direito-civil.lei-vigencia-aplicacao-integracao-interpretacao (Lei: vigência, aplicação no tempo e no espaço, integração e interpretação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fontes, teoria da norma e introdução ao direito civil": 14
- "2. Vigência, revogação e obrigatoriedade": 14
- "3. Lei no tempo, antinomias e interpretação": 27
- "4. Integração das lacunas e métodos interpretativos": 17
- "5. Lei no espaço e direito internacional privado": 28

Já existem (não repita):
- Uma regra está em lei regularmente aprovada e publicada, mas a data prevista para início d
- Uma nova norma permite que particulares disponham diversamente de determinada condição con
- Uma lei federal não estabelece início de vigência nem regra especial de vacatio. No territ
- Uma lei revogou apenas três artigos de uma lei anterior, deixando o restante da disciplina
- Uma sentença judicial não admite mais recurso, mas lei posterior passou a disciplinar a ma
- Uma lei com vacatio expressa de dez dias foi publicada oficialmente em 10 de março. Consid
- Uma lei foi publicada com prazo de vigência futura. Antes do início, houve republicação of
- Uma lei posterior regula aspecto novo de uma matéria, mas não contraria regra geral anteri
- Uma pessoa concluiu validamente um negócio antes de reforma legal que passou a exigir form
- Duas normas do mesmo patamar formal parecem conflitar: uma trata contratos em geral e outr
- Uma regra clara pode ser lida de modo a alcançar situação não mencionada literalmente, mas
- Há verdadeira omissão legal sobre certa relação civil, mas existe dispositivo aplicável a 
- Em controvérsia sem disciplina específica, uma parte pede que o julgador decida apenas pel
- Um cidadão está domiciliado permanentemente no país X, embora tenha nacionalidade do país 
- Uma discussão trata dos direitos reais referentes a terreno situado no país Z. Qual conexã
- Um contrato entre pessoas domiciliadas em Estados diferentes foi concluído por proposta en
- Pessoa domiciliada no país P faleceu deixando bens em países P, Q e Brasil. Discute-se a l
- Uma sociedade constituída no país R pretende operar mediante filial no Brasil, sem qualque
- Em questão sobre prova de fato ocorrido no exterior, a lei do país do fato admite meio de 
- Uma sentença estrangeira precisa de homologação para eficácia no Brasil e a questão reprod
- A regra brasileira de conexão aponta para a lei do país X; essa lei estrangeira remete o c
- No Brasil, um bem deixado por falecido estrangeiro domiciliado no exterior será transmitid
- Dois brasileiros maiores e capazes, sem filhos menores ou incapazes do casal, procuram aut
- Uma disputa trata de imóvel situado no Brasil, mas os envolvidos são domiciliados no exter
- Um dispositivo antigo fala em reconhecimento de divórcio estrangeiro apenas após certo lap
- Uma lei publicada sem cláusula específica de vigência começa a vigorar no país, como regra
- Uma lei posterior revoga integralmente outra; a lei mais antiga sofre, nesse caso, derroga
- Uma decisão não admite mais recurso e o legislador aprova norma posterior com solução dife
- Um pedido judicial não encontra disposição literal aplicável; por isso o julgador pode se 
- Na LINDB, a regra de capacidade de uma pessoa domiciliada em país estrangeiro utiliza, em 
- Um contrato de direito privado prevê cláusula contrária a norma legal cogente aplicável. O
- Uma regra geral posterior e outra especial anterior são compatíveis e disciplinam hipótese
- Lei A foi revogada por B e, anos depois, B foi revogada por C sem regra de restauração. Ne
- A interpretação sistemática considera relações entre dispositivos do ordenamento para comp
- Uma pessoa afirma que ignorava a existência de lei publicada e vigente. O art. 3º da LINDB
- Uma lei brasileira com vacatio expressa de quinze dias teve publicação oficial em 1º de ab
- Uma lei posterior especial, mas de hierarquia inferior, pode afastar disposição constituci
- O art. 5º da LINDB orienta a aplicação da lei segundo seus fins sociais e exigências do be
- O julgador encontra lacuna civil e decide exclusivamente por equidade porque o art. 4º da 
- O contrato celebrado entre pessoas domiciliadas em países diversos é, segundo o art. 9º, §
- Ao apontar lei estrangeira como regente de relação privada, a LINDB permite acolher automa
- A sucessão por morte segue, em regra, a lei do domicílio do falecido, mas a capacidade par
- Uma demanda relativa a imóvel localizado em território brasileiro deverá ser necessariamen
- Sentença estrangeira de divórcio consensual simples pode produzir efeitos no Brasil indepe
- Uma lei estrangeira escolhida pelas regras de conexão da LINDB produzirá necessariamente e
```

## Pedido 18: Governo digital e Lei 14.129/2021 (`administracao-publica.governo-digital`, 589 tópicos de edital)

```
Matéria: administracao-publica.governo-digital (Governo digital e Lei 14.129/2021)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Governo eletrônico, maturidade digital e estratégias públicas": 32
- "2. Lei 14.129/2021: abrangência, princípios e conceitos essenciais": 14
- "3. Serviços digitais, plataformas, direitos do usuário e participação": 23
- "4. Dados abertos, interoperabilidade, ePING e acessibilidade": 17
- "5. Inovação pública, PGD, experimentação e participação responsável": 14

Já existem (não repita):
- Um órgão colocou na internet o mesmo formulário em PDF que antes entregava em papel; após 
- Uma solução comum de autenticação é reutilizada por dezenas de serviços, evitando criar um
- Um município decide aplicar integralmente as regras da Lei 14.129/2021 em sua administraçã
- Uma plataforma disponibiliza certidão automaticamente após autenticação e validação eletrô
- Uma pessoa entrega documento válido que comprova residência; no dia seguinte, o mesmo serv
- Uma administração criou portal no qual o cidadão solicita atendimento e acompanha as etapa
- Após reclamações sobre demora, os gestores decidem tornar públicos indicadores de duração,
- Uma pessoa deseja saber requisitos, documentos, prazo máximo e locais para manifestar recl
- Ao receber reclamação sobre interrupções frequentes de atendimento, uma estrutura administ
- Um município institui colegiado de pessoas usuárias para avaliar um serviço digital, propo
- Uma pessoa acessa gratuitamente plataforma digital para requerer serviço cuja prestação en
- Alguém possui conta gov.br bronze e quer assinar documento no Portal de Assinatura Eletrôn
- Uma base de dados com licença de reuso foi publicada em CSV, formato estruturado e não pro
- Duas equipes buscam padrões: uma quer integrar sistemas de órgãos distintos; outra quer me
- Uma unidade organiza atividades por plano de entregas e planos individuais, permitindo tan
- Uma administração reúne usuários, pesquisadores e equipes para testar protótipos de solici
- Uma autoridade regulatória autoriza empresa a testar tecnologia por período delimitado, so
- Um catálogo disponibiliza planilha com URIs estáveis para identificar objetos, mas não est
- Uma equipe pretende divulgar planilha de atendimentos que inclui dados protegidos por sigi
- Um setor desenvolve protótipo experimental e, sem observar instrumentos legais de contrata
- Um órgão quer criar cadastro próprio de pessoas cuja informação íntegra já está disponível
- Em programa federal de gestão, uma unidade define entregas e metas, enquanto cada particip
- Uma entidade reguladora permite teste temporário de solução inovadora, suspendendo exigênc
- O painel de um serviço informa 500 acessos ao portal e média de quinze minutos por sessão,
- Uma repartição transfere integralmente o atendimento de um serviço essencial ao aplicativo
- Ao colocar formulários eletrônicos em página institucional, uma administração pode avançar
- As seis dimensões do modelo de governo digital da OCDE restringem-se a interoperabilidade,
- Uma entidade pública municipal pode adotar os comandos da Lei 14.129/2021 por ato normativ
- O art. 14 da Lei 14.129 determina que o autosserviço digital substitua integralmente todos
- A Lei 14.129/2021 indica Base Nacional de Serviços Públicos, Cartas de Serviços ao Usuário
- O conselho de usuários criado nos termos da Lei 13.460/2017 tem natureza deliberativa e po
- A arquitetura ePING busca estabelecer padrões de interoperabilidade entre sistemas; o eMAG
- A expressão 'governo como plataforma', na Lei 14.129, corresponde à publicação obrigatória
- No art. 22 da Lei 14.129, o painel deve informar por serviço quantidade anual de solicitaç
- A Carta de Serviços pode deixar de divulgar requisitos, etapas e previsão de prazo máximo,
- Para acessar o Portal de Assinatura Eletrônica gov.br, a pessoa necessita de conta prata o
- Uma base pública com licença aberta e dados estruturados em CSV alcança automaticamente ci
- O Programa de Gestão e Desempenho organiza trabalho por entregas, podendo abranger modalid
- Os arts. 44 e 45 da Lei 14.129 autorizam laboratório de inovação a afastar, por simples de
- No PGD, o plano de entregas da unidade e o plano de trabalho do participante são instrumen
- Uma entidade reguladora que institui sandbox pode afastar normas editadas por outros Poder
- Pelo art. 40, § 2º, da Lei 14.129, a criação de nova base de dados deve considerar previam
- Na Lei 13.460, a participação em conselho de usuários constitui função remunerada e o cons
- A legislação de governo digital associa participação e controle social ao monitoramento da
- A sexta dimensão de governo digital da OCDE, proatividade, equivale a tornar públicas toda
```

## Pedido 19: Suspensão, extinção e exclusão do crédito tributário (`direito-tributario.suspensao-extincao-exclusao`, 589 tópicos de edital)

```
Matéria: direito-tributario.suspensao-extincao-exclusao (Suspensão, extinção e exclusão do crédito tributário)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Formação do crédito e suspensão da exigibilidade no CTN atualizado": 24
- "2. Extinção do crédito: pagamentos, acordos e decisões": 18
- "3. Pagamento tributário, imputação e consignação em detalhes": 17
- "4. Repetição do indébito: restituição, encargos e prazos próprios": 20
- "5. Exclusão: isenção, anistia e comparação integrada das três categorias": 21

Já existem (não repita):
- Um crédito tributário constituído tem sua cobrança impedida por uma liminar ainda vigente.
- Uma pessoa deseja suspender a exigibilidade pela hipótese clássica de depósito do montante
- O contribuinte aderiu regularmente a parcelamento tributário e ainda deve várias prestaçõe
- Após cobrança de tributo efetivamente devido, o devedor paga integralmente o crédito media
- Uma lei dispensa determinado tributo para situação prevista e outra lei anistia infrações 
- A legislação não estipulou vencimento específico de crédito constituído por lançamento e r
- Uma guia de tributo foi paga em valor superior ao legalmente exigido, e o contribuinte não
- Um tributo permite transferir seu encargo financeiro ao comprador. Quem o recolheu repasso
- O contribuinte obteve isenção prevista em lei e pergunta se está automaticamente dispensad
- Após a LC nº 236/2026, uma proposta de transação tributária foi formalmente aceita pela ad
- Há execução fiscal em curso e a parte institui arbitragem especial tributária. Ainda não e
- Uma pessoa entrega cheque para pagar tributo, mas o banco sacado não o resgata. De acordo 
- Duas cobranças tributárias estão vencidas perante a mesma pessoa jurídica de direito públi
- Duas entidades públicas distintas exigem tributo idêntico sobre um mesmo fato gerador, e o
- O sujeito passivo pretende compensar quantia que considera indevidamente cobrada, mas a ex
- Uma pessoa fez pagamento indevido e deseja restituição. Em regra, qual combinação identifi
- Uma lei concedeu anistia para penalidade por descumprimento formal anterior, sem dolo, fra
- Um acordo de mediação tributária foi formalizado sob a lei específica, mas falta cumpri-lo
- Um devedor apresenta impugnação tributária tempestiva, e a administração exige caução prév
- Existem débitos vencidos de obrigação própria: um referente a imposto e outro a taxa, pera
- O contribuinte pede restituição originalmente por tributo pago em excesso. Após decisão ad
- Um tributo pago indevidamente incluía juros e multa por atraso, mas também havia penalidad
- Uma isenção foi concedida por prazo determinado e mediante exigência de investimento previ
- Após liminar judicial anterior à fiscalização, foi efetuado lançamento para prevenir decad
- A lei de 2026 prevê que indébitos tributários sejam atualizados de acordo com parâmetro le
- Em caso de moratória legal regularmente concedida, a exigibilidade do crédito tributário f
- O simples ajuizamento de uma ação judicial para questionar tributo, sem concessão de limin
- Um contribuinte paga integralmente o crédito tributário em moeda corrente, extinguindo a d
- O pagamento da primeira de várias parcelas de um crédito tributário faz presumir quitadas 
- O art. 165 do CTN admite restituição total ou parcial de tributo recolhido indevidamente m
- A concessão de isenção tributária dispensa necessariamente o cumprimento de todas as obrig
- A LC nº 236/2026 acrescentou ao art. 151 hipóteses de suspensão envolvendo arbitragem espe
- No processo administrativo tributário, é lícito exigir depósito integral do crédito como c
- O cumprimento de acordo de mediação tributária está expressamente incluído entre as hipóte
- Na falta de disposição específica sobre juros do crédito tributário em atraso, o art. 161,
- Quando a autoridade se recusa injustificadamente a receber tributo devido, o sujeito passi
- Para restituição de tributo que comporta transferência do encargo financeiro, basta aprese
- A restituição proporcional de tributo indevido alcança juros e multas relacionados à parte
- A simples oferta de apólice de seguro garantia pelo devedor, sem aceitação do credor e sem
- Em arbitragem especial tributária instituída quando já existe execução fiscal, o art. 151,
- Um crédito tributário discutido judicialmente pelo sujeito passivo pode ser compensado ant
- Se, em crédito pago indevidamente, a decisão judicial determinou devolução e transitou em 
- Uma ação anulatória ajuizada contra decisão administrativa denegatória de restituição segu
- Na falta de regra legal em contrário, isenção tributária não se estende automaticamente a 
- Uma lei que concede anistia de infrações tributárias pode, pelo art. 180, abranger normalm
```

## Pedido 20: Sucessões (`direito-civil.sucessoes`, 588 tópicos de edital)

```
Matéria: direito-civil.sucessoes (Sucessões)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Abertura da sucessão, herança, vocação e exclusão": 24
- "2. Sucessão legítima, concorrência, legítima e representação": 20
- "3. Sucessão testamentária: formas, capacidade, codicilo e limites": 18
- "4. Legados, acrescer, substituições e petição de herança aplicada": 16
- "5. Inventário, colação, sonegados, dívidas e partilha": 22

Já existem (não repita):
- João morreu em 15 de março deixando dois filhos e bens não inventariados. Em que momento s
- Helena, domiciliada habitualmente na cidade A, faleceu durante viagem à cidade B. Para o l
- Um herdeiro declara verbalmente, diante de parentes, que renuncia à herança, mas não compa
- Um testamento foi lavrado em 2014, mas seu autor faleceu em 2025. Qual marco temporal rege
- Após a morte de pessoa sem sucessor conhecido, o patrimônio foi arrecadado e começou a ser
- Uma pessoa faleceu sem testamento e deixou descendentes, ascendentes e um irmão. Qual clas
- O sobrevivente tem direito a 200 mil de meação de bens comuns; a metade pertencente ao fal
- Rafael morreu deixando um filho vivo e dois netos filhos de outro filho premorto. Na ausên
- Lívia, com 17 anos e pleno discernimento, deseja lavrar testamento observando as solenidad
- Bruna comprovou ser herdeira, mas outro sucessor aparente recebeu e passou a possuir a tot
- Um companheiro sobrevivente disputa herança com parentes colaterais, sem descendentes nem 
- Uma pessoa faleceu sem descendentes, deixando cônjuge sobrevivente e apenas seu pai como a
- O falecido deixou herdeiros necessários e patrimônio líquido de R$ 900 mil para cálculo da
- Um testador sabe ler e entrega ao tabelião, perante duas testemunhas, documento escrito e 
- Uma pessoa escreve, data e assina instruções sobre funeral e destinação de roupas de peque
- Havendo herdeiros necessários, o testador destinou a terceiros 70% do patrimônio líquido s
- No testamento, o autor atribuiu expressamente um automóvel individualizado a Luísa, sem in
- Após a morte do autor da herança, transitou em julgado condenação penal de um sucessor por
- Um testamento prevê fideicomisso em favor de neto já nascido quando o testador morreu. Qua
- O testador nomeou Ana e Bento conjuntamente, pela mesma disposição, herdeiros da quota dis
- Durante viagem marítima nas condições do art. 1.888, o viajante fez testamento especial e 
- Um filho renunciou formalmente à herança, sendo o único herdeiro legítimo da sua classe, e
- Os interessados desejam escritura pública de inventário, embora haja herdeiro menor. Todos
- Um pai doou em vida R$ 100 mil a um de seus filhos como antecipação da legítima e depois f
- Na partilha, um herdeiro omitiu deliberadamente bem hereditário em seu poder. Outros suces
- Um herdeiro recebe o patrimônio por transmissão decorrente da morte, mas a indivisão da he
- O herdeiro pode renunciar validamente à sua participação apenas por mensagem particular en
- A lei sucessória aplicada à vocação hereditária é, em regra, aquela em vigor na data da mo
- Havendo descendentes sucessíveis, os colaterais recebem imediatamente a mesma quota que el
- O testamento público ordinário, observado o art. 1.864 do Código Civil, é lido na presença
- O codicilo admite transferir qualquer imóvel valioso, desde que seu autor assine e date o 
- O herdeiro aparente que vendeu onerosamente bem da herança a terceiro de boa-fé pode ter a
- Os filhos de herdeiro que renunciou formalmente à herança representam obrigatoriamente o r
- A indenização e o cumprimento de dívidas do falecido devem respeitar as forças da herança;
- A companheira sobrevivente deve receber na herança fração menor que a do cônjuge em situaç
- Na ausência de descendentes, um cônjuge sobrevivente que concorre com apenas um ascendente
- O testamento particular ordinário exige duas testemunhas, enquanto o cerrado exige três te
- Se o testador alienou posteriormente parte de coisa certa objeto de legado, a disposição p
- Para o prazo atual de instauração judicial do inventário, deve prevalecer o texto de trint
- Um inventário consensual com testamento pode, em circunstâncias delimitadas, seguir por es
- Qualquer divergência aritmética involuntária nas declarações do inventário faz o herdeiro 
- O testamento poderá romper-se se sobrevier descendente sucessível que o testador não tinha
- Se o beneficiário de substituição fideicomissária já tiver nascido na abertura da sucessão
- Quando os bens do espólio têm valor igual ou inferior a mil salários mínimos, a legislação
- A partilha amigável entre herdeiros maiores e capazes será nula se os quinhões forem desig
```

## Pedido 21: Hematologia e hemoterapia: anemias, coagulação, neoplasias hematológicas e transfusão (`medicina.hematologia-hemoterapia`, 586 tópicos de edital)

```
Matéria: medicina.hematologia-hemoterapia (Hematologia e hemoterapia: anemias, coagulação, neoplasias hematológicas e transfusão)
Capítulos e quantas questões quero em cada um (total 100):
- "Hematopoese, interpretação clínica do hemograma e anemias por deficiência ou inflamação": 17
- "Anemias megaloblásticas, hemolíticas, hereditárias, da infância e falência medular": 17
- "Hemostasia, distúrbios plaquetários, hemofilias, von Willebrand e CIVD": 17
- "Trombofilias, trombose e princípios da anticoagulação": 17
- "Leucemias, linfomas, mieloma múltiplo e neutropenia febril": 16
- "Hemoterapia, indicação de hemocomponentes e reações transfusionais": 16

Já existem (não repita):
- Mulher com menstruações intensas apresenta fadiga, microcitose, RDW alto e ferritina reduz
- Em paciente com artrite inflamatória ativa, Hb baixa, ferro sérico reduzido e ferritina el
- Um adulto tem VCM muito baixo, anemia discreta, contagem de eritrócitos relativamente alta
- Qual estratégia inicial melhor integra a interpretação de anemia com VCM normal e suspeita
- Paciente tem anemia macrocítica, glossite, parestesias e perda de sensibilidade vibratória
- Em investigação de anemia com icterícia, reticulocitose, LDH elevada e haptoglobina baixa,
- Criança com triagem positiva para HbS apresenta dor aguda, febre e novo infiltrado pulmona
- Pessoa com pancitopenia e reticulócitos muito baixos apresenta medula óssea hipocelular, s
- No processo de adesão plaquetária inicial após lesão vascular, qual interação tem papel pa
- Criança apresenta hemartroses recorrentes e deficiência de fator IX confirmada. Qual entid
- Paciente com epistaxe e menorragia desde a adolescência apresenta plaquetas numericamente 
- Paciente com sepse grave evolui com sangramento em punções, plaquetas em queda, TP prolong
- Sobre a tríade de Virchow na formação de trombose venosa, assinale a combinação correta.
- Paciente com trombose venosa usa varfarina e necessita acompanhamento farmacodinâmico habi
- Em qual cenário clínico a substituição indiscriminada de varfarina por anticoagulante oral
- Paciente sob heparina apresenta redução importante de plaquetas acompanhada por nova tromb
- Leucocitose persistente com desvio granulocítico, basofilia e presença de BCR::ABL1 direci
- Uma pessoa apresenta dor óssea, anemia, hipercalcemia, piora da função renal e componente 
- Adulto apresenta adenomegalia persistente e sintomas B. Qual abordagem permite classificar
- Paciente após quimioterapia tem neutropenia acentuada e febre, sem foco aparente no exame 
- Qual hemocomponente tem como principal objetivo aumentar a capacidade de transporte de oxi
- Durante transfusão de concentrado de hemácias, paciente desenvolve febre, dor lombar e hip
- Paciente com insuficiência cardíaca recebe transfusão e evolui com dispneia, hipertensão, 
- Uma pessoa com coagulopatia de consumo apresenta sangramento e hipofibrinogenemia relevant
- Na avaliação de anemia em lactente, qual princípio impede erro de classificação?
- Sobre a fase fibrinolítica da hemostasia, assinale a proposição conceitualmente correta.
- Em paciente com blastos anormais, sangramento disseminado e forte suspeita de leucemia pro
- Na seleção de hemocomponentes para transfusão, qual afirmação sobre compatibilidade ABO es
- A eritropoetina, produzida principalmente pelos rins, participa do estímulo à eritropoese.
- Ferro sérico baixo, isoladamente, diferencia com segurança anemia ferropriva de anemia da 
- Uma ferritina elevada em paciente com infecção ativa exclui absolutamente depleção de ferr
- Deficiência de folato e deficiência de vitamina B12 podem produzir padrão hematológico meg
- Talassemias são definidas primordialmente pela diminuição quantitativa da síntese de uma c
- Na anemia aplásica, pancitopenia costuma associar-se à reticulocitose intensa, indicando h
- Na anemia hemolítica autoimune por anticorpos quentes, o Coombs direto positivo precisa se
- Hemofilia A corresponde à deficiência do fator VIII, enquanto a hemofilia B está ligada à 
- O fator de von Willebrand participa da adesão plaquetária e da estabilização do fator VIII
- Um TTPa dentro do intervalo de referência elimina a possibilidade de doença de von Willebr
- A CIVD, por definição, exclui tromboses microvasculares se o paciente apresenta sangrament
- A dabigatrana é inibidor direto da trombina, ao passo que apixabana e rivaroxabana inibem 
- O anticoagulante lúpico pode prolongar TTPa em ensaios laboratoriais e, paradoxalmente, as
- O resultado de proteína C, proteína S e antitrombina pode ser interpretado sem cautela dur
- A identificação de BCR::ABL1 é central no diagnóstico da leucemia mieloide crônica e orien
- A leucemia aguda é descartada se a contagem global de leucócitos no hemograma estiver norm
- Na leucemia promielocítica aguda com forte suspeita clínica, o risco de coagulopatia grave
- A transfusão de concentrado de plaquetas tem como objetivo primário corrigir a capacidade 
- Na hemovigilância brasileira, reações transfusionais imediatas incluem as ocorridas durant
- TACO e TRALI são expressões equivalentes para sobrecarga volêmica e respondem ao mesmo mec
```

## Pedido 22: Limitações ao poder de tributar (`direito-tributario.limitacoes-poder-tributar`, 585 tópicos de edital)

```
Matéria: direito-tributario.limitacoes-poder-tributar (Limitações ao poder de tributar)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Competência, legalidade, isonomia e capacidade contributiva": 21
- "2. Anterioridade, irretroatividade e demais proibições constitucionais": 24
- "3. Imunidades gerais, reciprocidade e CTN": 25
- "4. Imunidades específicas de ITR, exportação, ITBI e contribuições": 18
- "5. IBS, CBS, aplicação prática do CTN e revisão jurisprudencial": 12

Já existem (não repita):
- Um município pretende criar taxa de inspeção por decreto, sem lei instituidora, afirmando 
- A autoridade municipal edita decreto para corrigir a base do IPTU conforme índice oficial 
- Lei tributária é publicada em dezembro e institui imposto sem exceção constitucional às du
- O governo federal aumenta validamente a alíquota do IPI no regime autorizado por lei. Qual
- Um Estado estuda cobrar imposto apenas sobre caminhões que cruzam sua divisa, para dificul
- Uma instituição de educação sem fins lucrativos cumpre o art. 14 do CTN e usa aluguel de p
- Uma editora oferece livro eletrônico para leitores digitais próprios, com funções acessóri
- Proprietário explora sozinho gleba de 25 hectares situada fora das regiões especiais da Le
- Uma associação educacional sem fins lucrativos solicita imunidade de impostos, mas distrib
- Lei publicada em dezembro de 2026 aumenta alíquota do imposto de renda, sem aplicação retr
- Uma indústria destina seus produtos industrializados a compradores no exterior. Em relação
- Imóvel avaliado em R$ 800 mil é transferido para integralizar capital social de R$ 500 mil
- Entidade beneficente certificada invoca o art. 195, § 7º, para afastar automaticamente a C
- Uma lei federal prevê incentivo fiscal para reduzir desigualdade socioeconômica regional, 
- Um serviço público essencial é prestado por sociedade de economia mista delegatária, sem d
- Contribuinte sofreu mudança retroativa de critério jurídico que a fiscalização vinha adota
- Lei aumenta simultaneamente a base do IPTU em termos reais e sua alíquota. A Administração
- Uma autarquia possui imóvel vinculado a suas finalidades essenciais e outro empregado em e
- Um município pretende cobrar ITBI de empresa imobiliária pela integralização de imóvel em 
- Um parecer sustenta que o CTN, art. 15, III, por si permite novo empréstimo compulsório pa
- O contribuinte importa máquinas e deseja invocar a imunidade geral de exportações de IBS e
- Uma organização beneficente certificada realiza fornecimentos potencialmente imunes a IBS/
- Proprietário rural tem gleba de 45 hectares em município do Polígono das Secas; explora a 
- Lei estadual tributa mercadoria em alíquota maior exclusivamente por ter vindo de outra un
- Uma entidade religiosa presta assistência por organização beneficente própria e invoca imu
- Um decreto federal aumenta a alíquota do imposto de importação nos limites e condições de 
- Uma lei publicada em dezembro majora imposto sujeito à anterioridade anual e à noventena. 
- Uma universidade sem fins lucrativos que atende o art. 14 do CTN aplica aluguéis de imóvel
- O art. 150, VI, da Constituição garante que instituições sem fins lucrativos imunes ao IPT
- Uma gleba de 20 hectares em município sujeito ao limite geral é explorada pelo proprietári
- Uma editora vende e-book e também comercializa tablets multifuncionais. A Súmula Vinculant
- Um município cobra pedágio pela utilização de via conservada pelo poder público. A ressalv
- Duas empresas com rendimentos e atividades materialmente equivalentes são tributadas de ma
- Os arts. 9º a 15 do CTN devem ser lidos conforme a Constituição de 1988 e suas emendas, de
- Um fisco altera sua interpretação jurídica sobre lançamentos anteriormente efetuados e pre
- Uma lei nacional estabelece cobrança diferenciada em razão da procedência de mercadorias, 
- A legislação de IBS/CBS estabelece imunidade das exportações de bens e serviços. Por isso 
- Uma entidade beneficente certificada atua em saúde e atende as exigências da LC nº 187/202
- Pelo art. 150, § 1º, da Constituição, o imposto de renda está dispensado da anterioridade 
- O Tema 796 do STF limita a imunidade do ITBI ao capital social efetivamente integralizado,
- No julgamento do Tema 1.348, o STF já havia proclamado tese final até 10 de outubro de 202
- Uma empresa pública delegatária presta serviço público essencial, não distribui lucros a a
- O art. 149-B, parágrafo único, da Constituição, determina que IBS e CBS observem o art. 19
- Uma empresa pretende obter imunidade de IPI sobre produtos industrializados destinados ao 
- Quando um município dispensa imposto por lei ordinária, e quando a Constituição veda incid
```

## Pedido 23: Governança de TI: ITIL 4 e COBIT (`informatica.governanca-ti`, 585 tópicos de edital)

```
Matéria: informatica.governanca-ti (Governança de TI: ITIL 4 e COBIT)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Governança, gestão e normas de referência": 22
- "2. ITIL 4: valor, princípios e arquitetura": 18
- "3. ITIL 4: práticas, melhoria contínua e comparação com v3": 23
- "4. COBIT 2019: princípios, componentes e desenho": 20
- "5. COBIT 2019: os 40 objetivos, capacidade e integração": 17

Já existem (não repita):
- Um comitê analisa se o investimento em atendimento digital entregará benefícios compatívei
- Uma organização quer adotar uma norma de requisitos auditáveis para estabelecer e melhorar
- Um sistema executa corretamente todas as funções de consulta, mas fica indisponível em hor
- Uma equipe identifica a necessidade de considerar pessoas, tecnologias, fornecedores e flu
- Depois de uma falha no portal, a equipe busca recuperar a consulta rapidamente, ainda que 
- Uma pessoa solicita acesso a uma ferramenta de uso frequente. O pedido integra um catálogo
- No COBIT 2019, um grupo avalia benefícios de investimentos, orienta decisões e acompanha o
- Uma organização planeja medir a capacidade dos processos COBIT 2019 usando a escala de des
- Ao comparar COBIT 5 e COBIT 2019, uma equipe encontra números distintos para a estrutura c
- Um projeto é executado em ciclos curtos com retorno frequente de quem utiliza a aplicação.
- Ao planejar melhorias, a equipe já definiu a visão do serviço. Segundo o modelo de melhori
- Uma alteração rotineira é de baixo risco, compreendida e previamente autorizada por proced
- Durante uma implantação, o pacote de software foi movido para produção, mas a nova função 
- Uma organização precisa acompanhar cumprimento de exigências impostas externamente ao seu 
- A equipe ITIL 4 deseja construir um fluxo de valor que atenda solicitações rotineiras sem 
- Ao classificar conceitos do ITIL 4, um analista registra melhoria contínua como componente
- Um material apresenta estratégia, desenho, transição, operação e melhoria contínua como es
- Durante a revisão de políticas, um grupo enumera seis princípios para o sistema de governa
- Duas organizações apresentam riscos, tamanhos, modelos de fornecedores e estratégias difer
- Uma direção quer traduzir a necessidade de satisfação de usuários em metas corporativas, m
- A direção fixou o apetite a riscos e verifica se a exposição está otimizada. A equipe técn
- Uma falha operacional recorrente exige restauração imediata e correção definitiva em momen
- Um diagnóstico encontrou falhas na continuidade de serviços, no gerenciamento de fornecedo
- Uma equipe compara os modelos centrais COBIT 5 e COBIT 2019, buscando três objetivos que s
- Um relatório declara que um processo apresenta capacidade nível 4 no COBIT 2019 e conclui 
- Ao contratar um serviço digital, a área demandante participa da definição de resultados e 
- Uma instituição que pretende estabelecer requisitos verificáveis para o sistema de gestão 
- Uma equipe que mede os processos existentes antes de decidir o que reformular está aplican
- Diante de uma interrupção de sistema, a primeira finalidade do gerenciamento de incidentes
- Uma instituição que avalia competências de equipes, bases de dados, contratos com terceiro
- No COBIT 2019, APO, BAI, DSS e EDM reúnem objetivos de gestão, enquanto MEA é o único domí
- Ao desenhar um fluxo de tratamento de chamados, uma organização pode combinar atividades d
- Uma mudança emergencial necessária para recuperar uma funcionalidade crítica pode ser exec
- Uma organização pode utilizar recomendações de práticas do ITIL 4 e, separadamente, buscar
- Um portal que apresenta todos os recursos funcionais esperados demonstra garantia adequada
- Uma equipe que trata práticas ITIL 4 como combinações de recursos humanos, informações, fo
- Uma alteração classificada como mudança padrão, mesmo sendo pré-autorizada e de baixo risc
- Ao revisar COBIT 2019, uma organização observa que processos, estruturas, informações, pes
- Na cascata de objetivos do COBIT 2019, as necessidades das partes interessadas são ligadas
- Duas instituições que compartilham o mesmo modelo de referência COBIT 2019 podem definir p
- No COBIT 2019, o objetivo APO12 assegura a otimização de riscos como função de governança,
- Uma nova aplicação foi tecnicamente implantada em produção antes de sua funcionalidade ser
- A versão ITIL 4 manteve estratégia, desenho, transição, operação e melhoria contínua como 
- Uma organização que necessita gerenciar avaliações independentes e outras atividades de as
- Como o COBIT 5 já possuía 40 objetivos de governança e gestão, a principal alteração do CO
```

## Pedido 24: Ordem econômica e financeira na Constituição (arts. 170 a 192) (`direito-constitucional.ordem-economica-financeira`, 585 tópicos de edital)

```
Matéria: direito-constitucional.ordem-economica-financeira (Ordem econômica e financeira na Constituição (arts. 170 a 192))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e princípios da ordem econômica": 31
- "2. Intervenção estatal, serviços públicos e regulação econômica": 26
- "3. Recursos minerais, monopólios e proteção econômica e ambiental": 14
- "4. Política urbana, agricultura, reforma agrária e usucapião": 17
- "5. Sistema Financeiro Nacional e integração da ordem constitucional": 12

Já existem (não repita):
- Uma associação comercial sustenta que a ordem econômica constitucional deve valorizar a in
- Uma norma estabelece critérios ambientais diferentes para dois processos industriais confo
- Uma pessoa pretende iniciar atividade econômica e pergunta se a Constituição exige autoriz
- Um investidor pretende conhecer o fundamento constitucional geral da disciplina de investi
- Uma apostila apresenta como vigente o antigo art. 171 da Constituição, relativo à diferenc
- Uma entidade estatal pretende produzir mercadorias em mercado concorrencial. Segundo o art
- Um plano econômico nacional estabelece prioridades a serem observadas pela administração p
- Um município pretende transferir a prestação de serviço público a particular por concessão
- Uma empresa combina condutas para excluir concorrentes e elevar artificialmente ganhos. Qu
- Uma autoridade estabelece padrões de segurança e fiscaliza seu cumprimento por empresas pr
- Um proprietário de fazenda encontrou minério e alega que a jazida lhe pertence por estar s
- O Estado pretende contratar uma empresa privada para explorar atividade petrolífera listad
- Uma fábrica pretende instalar operação potencialmente causadora de significativa degradaçã
- Um ente federativo pretende reduzir obrigações administrativas de empresas de pequeno port
- A população de uma cidade passa de 19.500 para 21.000 habitantes. Qual exigência constituc
- O município enquadra imóvel urbano subutilizado em área de plano diretor e aplica medidas 
- Uma família mantém por cinco anos, sem oposição, posse como dona de área urbana de 230 m² 
- Um imóvel rural descumpre função social e é objeto de desapropriação federal para reforma 
- Uma propriedade rural apresenta grau de utilização da terra de 85% e grau de eficiência na
- Uma pessoa possui duas propriedades rurais, sendo uma delas pequena, e invoca apenas o por
- Uma fazenda atinge indicadores econômicos de produção, mas viola normas de proteção ambien
- O poder público pretende conceder a particular 2.700 hectares de terras públicas, para emp
- Uma família sem outro imóvel exerce posse rural sem oposição sobre área privada de 45 hect
- Um projeto afirma que a Constituição determina a disciplina integral do Sistema Financeiro
- Um material sobre a LC 179/2021 atribui a fixação de metas monetárias ao Banco Central e s
- Uma lei que favorece empresa de pequeno porte brasileira com sede e administração no País 
- O parágrafo único do art. 170 exige autorização administrativa para qualquer nova atividad
- Um plano nacional de desenvolvimento pode orientar empresas particulares sem convertê-las 
- Uma delegação de serviço público por permissão dispensa licitação, porque o art. 175 exige
- A empresa concessionária de lavra pode adquirir a propriedade do produto extraído sem se t
- Como a União detém monopólio constitucional sobre atividades petrolíferas do art. 177, a p
- Ao sancionar conduta de exclusão deliberada de concorrentes, aplica-se a orientação do art
- O art. 179 reserva tratamento favorecido de simplificação apenas a grandes empresas, desde
- O incentivo ao turismo como fator de desenvolvimento social e econômico é atribuição const
- Uma cidade com exatamente vinte mil habitantes enquadra-se, apenas por esse número, na exi
- Uma pessoa que ocupa como seu imóvel urbano privado de 240 m² para moradia, por cinco anos
- Uma família pode adquirir por usucapião especial rural uma área pública de trinta hectares
- Na desapropriação para reforma agrária, os títulos da dívida agrária preservam valor real 
- A Lei 8.629/1993 considera produtiva a propriedade rural que tenha GUT de 79% e GEE de 110
- Uma propriedade rural pode alcançar índices de produtividade e ainda suscitar questionamen
- A concessão de 2.500 hectares exatos de terra pública exige aprovação prévia do Congresso 
- Os títulos de domínio ou concessão de uso recebidos por beneficiários da reforma agrária s
- A norma vigente do art. 190 da Constituição proíbe a toda pessoa física ou jurídica estran
- O art. 192, após a EC 40/2003, prevê regulamentação do Sistema Financeiro Nacional por lei
- Conforme a LC 179/2021, o objetivo fundamental do Banco Central é fomentar o pleno emprego
```

## Pedido 25: Organização dos Poderes e separação das funções (`direito-constitucional.organizacao-poderes`, 584 tópicos de edital)

```
Matéria: direito-constitucional.organizacao-poderes (Organização dos Poderes e separação das funções)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Unidade do poder estatal, separação e Estado Democrático de Direito": 23
- "2. Funções típicas e atípicas dos três Poderes": 20
- "3. Freios e contrapesos: controle recíproco sem subordinação": 25
- "4. Independência institucional, garantias e autonomia": 16
- "5. Limites da interferência, reserva de iniciativa e casos integrados": 16

Já existem (não repita):
- Ao elaborar uma cartilha, um servidor afirma que o Estado possui três soberanias independe
- Após uma divergência institucional, afirma-se que só haverá harmonia se um Poder deixar de
- Em um exercício, deve-se classificar o ato pelo qual o Legislativo contrata serviços para 
- Uma regra interna permite ao Poder Judiciário organizar suas rotinas administrativas. Qual
- Uma proposta de emenda pretende subordinar integralmente as decisões de um Poder à autoriz
- Uma administração regulamenta uma lei editada pelo Legislativo, dentro dos limites autoriz
- Um projeto aprovado é remetido ao Executivo, que o considera contrário ao interesse públic
- Um órgão do Legislativo pretende fiscalizar gastos executados pelo Poder Executivo do mesm
- Ao organizar suas atividades, o Judiciário elabora regimento interno compatível com normas
- Um ato administrativo ameaça direito de particular e a autoridade alega que nenhuma decisã
- Em um parecer, consta que uma emenda altera a coordenação entre Poderes sem suprimir a ind
- O Executivo veta apenas duas palavras de um inciso de projeto já aprovado. Considerando o 
- O Legislativo pretende sustar ato regulamentar que criou obrigação não autorizada pela lei
- Um pedido de aumento de subsídio se fundamenta apenas em equiparação a outro grupo, sem le
- Uma proposta orçamentária do Judiciário excede os limites da lei de diretrizes orçamentári
- Uma lei, iniciada no Legislativo, gera gastos de implementação, mas não altera estrutura, 
- Em determinado mês, recursos duodecimais destinados ao Judiciário são prometidos somente p
- Uma autoridade diz que os recursos recebidos em duodécimos podem ser livremente transferid
- Em processo de remoção de integrante da magistratura, a autoridade alega que a inamovibili
- Em disputa salarial, o Judiciário reconhece parcela que lei vigente assegura ao requerente
- O Executivo deixa transcorrer o prazo de 15 dias úteis sem se manifestar sobre projeto env
- Foi apresentado projeto legislativo que cria uma unidade da administração executiva e rede
- Um órgão de controle externo presta auxílio técnico ao Legislativo na fiscalização finance
- Uma reforma tenta eliminar toda possibilidade de apreciação judicial de lesão a direito ca
- Um repasse regular gerou saldo financeiro não utilizado no fim do exercício. Segundo art. 
- Um grupo de representantes afirma que a Constituição reparte o exercício de competências e
- Em debate institucional, foi sustentado que, por serem independentes, os Poderes não podem
- Um setor do Legislativo realiza atividades de gestão de seus serviços internos; essa condu
- Uma comissão denomina de jurisdição típica a edição de regimento interno de órgão judicial
- Uma proposta de emenda destinada a suprimir o núcleo da independência entre os Poderes enc
- Recebido o projeto, o Executivo dispõe de quinze dias corridos para vetá-lo, conforme o ar
- O veto parcial de apenas metade das palavras de uma alínea é válido se o Executivo alegar 
- Um ato normativo executivo que ultrapasse os limites do poder regulamentar pode ser sustad
- Por garantir autonomia financeira ao Judiciário, a Constituição dispensa sua proposta orça
- O art. 168 prevê entrega de recursos em duodécimos até o dia 20 de cada mês aos destinatár
- Uma norma de iniciativa legislativa cria gasto acessório sem interferir na estrutura de ór
- A autonomia de instituições constitucionalmente protegidas transforma cada uma delas em no
- A vitaliciedade de integrante da magistratura no primeiro grau é adquirida após três anos 
- A garantia de inamovibilidade admite remoção por motivo de interesse público, observadas a
- O veto executivo impede em definitivo qualquer reação do Legislativo, mesmo quando há maio
- Em análise de legalidade, uma decisão judicial pode controlar lesão a direito sem que isso
- Se recursos duodecimais gerarem saldo financeiro, o art. 168, § 2º, permite conservá-lo in
- A Súmula Vinculante 37 proíbe a criação judicial de aumento de remuneração por simples iso
- O art. 49, V, autoriza sustação de qualquer ato administrativo executivo que desagrade ao 
- A repartição funcional é compatível com julgamentos político-constitucionais no Legislativ
```

## Pedido 26: Conceitos, patrimônio e contas (`contabilidade.conceitos-patrimonio-contas`, 584 tópicos de edital)

```
Matéria: contabilidade.conceitos-patrimonio-contas (Conceitos, patrimônio e contas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Contabilidade: conceito, objeto, finalidade, usuários e técnicas": 23
- "2. Patrimônio, equação fundamental e situações líquidas": 23
- "3. Contas, débito, crédito e plano de contas": 22
- "4. Atos administrativos e fatos contábeis: permuta, modificação e mistura": 17
- "5. Receita, despesa, ganho, perda e apuração do resultado": 15

Já existem (não repita):
- Uma associação comunitária sem finalidade lucrativa precisa apresentar seus recursos, dívi
- Uma entidade possui caixa, estoque, valores a receber de clientes e obrigações por mercado
- Em determinada data, uma empresa apresenta ativos de R$ 90.000 e passivos exigíveis de R$ 
- Uma empresa recebe R$ 7.000 em sua conta bancária pela quitação de direito já registrado c
- Uma sociedade apenas aprova internamente a contratação futura de um serviço; não houve pre
- Uma empresa compra, à vista, mercadorias por R$ 5.000, sem custos adicionais e sem vendê-l
- Um proprietário deposita R$ 20.000 na conta da empresa para integralizar capital subscrito
- Um serviço de R$ 6.000 foi integralmente prestado em setembro, mas o cliente pagará apenas
- Ao organizar seu plano de contas, uma empresa redige, além da lista de contas, orientações
- Ao avaliar relatórios contábeis, uma entidade identifica o dirigente financeiro e o fornec
- Uma sociedade apresenta ativos de R$ 130.000 e obrigações exigíveis de R$ 160.000. Na repr
- Uma máquina custa R$ 80.000 e tem depreciação acumulada reconhecida de R$ 18.000. Sem outr
- Uma empresa realiza uma operação que reduz o saldo bancário em R$ 10.500: R$ 10.000 quitam
- No fechamento de dezembro, uma empresa identifica uma conta de Despesa de Aluguel com sald
- Uma entidade vende, à vista, por R$ 14.000, uma máquina cujo valor contábil é R$ 11.000, s
- Uma empresa inicia o mês com ativos de R$ 70.000 e passivos de R$ 30.000. Reconhece receit
- A contabilidade registra compras a prazo, elabora balanços anuais, calcula índices de liqu
- No início do período, uma sociedade tem ativo de R$ 200.000 e passivo de R$ 180.000. Depoi
- Uma empresa firma contrato de manutenção futura por R$ 12.000 e paga antecipadamente todo 
- No encerramento, foram identificados saldos de receitas R$ 40.000, ganhos R$ 5.000, despes
- Em um dia, a empresa recebe R$ 14.000 antecipados de cliente por serviço não prestado, rec
- Uma empresa formaliza seu plano de contas e informa que os códigos 1.01, 2.01 e 4.01 foram
- Uma pessoa compra 100% das quotas de uma entidade por R$ 150.000 usando recursos particula
- Uma loja compra estoque por R$ 8.000 e, em operação posterior, vende-o à vista por R$ 11.0
- O PL inicial era R$ 50.000. Durante o ano, a empresa reconheceu receitas de R$ 38.000, des
- Uma empresa apenas assina proposta de fornecimento futuro, sem entrega, pagamento nem obri
- Um fornecedor que analisa demonstrações para decidir se concede mercadorias a prazo é exem
- Ao debitar a conta Fornecedores para registrar o pagamento de obrigação já contabilizada, 
- Se uma entidade apresenta ativo de R$ 60.000 e passivo exigível de R$ 75.000, sua situação
- Uma sociedade que recebe R$ 40.000 de empréstimo bancário reconhece receita de R$ 40.000, 
- O plano de contas pode conter elenco organizado, códigos e orientações sobre a função e o 
- Ao receber R$ 9.000 de cliente cujo crédito já estava registrado, a sociedade aumenta Caix
- O registro de mercadorias compradas à vista, ainda não vendidas, reduz imediatamente o PL 
- Em balanço patrimonial esquemático em T, o ativo é exposto à esquerda e as obrigações exig
- A quitação de empréstimo de R$ 10.000 acrescido de juros de R$ 400 incorridos na data é in
- Um serviço concluído em dezembro e faturado para pagamento no janeiro seguinte pode gerar 
- Ao vender por R$ 18.000 um equipamento de valor contábil de R$ 14.000, sem custos adiciona
- Para encerrar uma conta de Receita de Serviços de saldo credor de R$ 25.000, a entidade a 
- Ao receber R$ 10.000 de um cliente por serviço que será integralmente prestado no exercíci
- Uma obrigação de R$ 8.000 é integralmente liquidada mediante pagamento de R$ 7.200, com de
- Quando o patrimônio líquido é negativo em R$ 25.000, todos os recursos reconhecidos no ati
- Ao reconhecer R$ 600 de depreciação do período, debitando despesa e creditando depreciação
- Pelo CPC 00 (R2), um ganho de R$ 3.000 na venda eventual de equipamento não pode integrar 
- De um adiantamento de cliente de R$ 5.000 anteriormente registrado no passivo, a entidade 
- Uma entidade reconhece R$ 32.000 de receitas e R$ 26.000 de despesas. Após encerrar essas 
```

## Pedido 27: Relações de consumo: consumidor, fornecedor e responsabilidade (`direito-consumidor.relacoes-consumo`, 583 tópicos de edital)

```
Matéria: direito-consumidor.relacoes-consumo (Relações de consumo: consumidor, fornecedor e responsabilidade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais, Política Nacional e sujeitos da relação": 31
- "2. Direitos básicos, prova, prevenção, serviços e contratos": 23
- "3. Responsabilidade pelo fato do produto e do serviço e desconsideração": 18
- "4. Vícios, soluções, decadência, prescrição e arrependimento": 15
- "5. Comércio eletrônico, precedentes e solução integrada de casos": 13

Já existem (não repita):
- Uma pessoa compra fogão para cozinhar na própria residência, sem intenção de revenda ou in
- Uma empresa fornece crédito remunerado a clientes e argumenta que serviços financeiros não
- Um pedestre não participa da compra de uma bateria, mas é ferido quando o produto explode 
- Em ação de consumo, o juiz aprecia provas e reconhece verossimilhança das alegações do aut
- Uma máquina deixa de funcionar após a compra, mas não causa ferimentos nem danos externos.
- Uma geladeira durável apresenta vício aparente no ato da entrega. Qual prazo do art. 26 do
- Um cliente contrata um produto em comércio eletrônico e recebe o item há dois dias. Está í
- Um serviço de reparo é prestado de modo inadequado e não resolve o problema contratado. Qu
- Uma microempresa compra equipamento integrado à sua atividade produtiva e requer aplicação
- Um produto inseguro causa dano, e o fabricante prova uma das situações excludentes do art.
- Um supermercado deixa peixe perecível sem refrigeração adequada; clientes adoecem após a c
- Uma consumidora compra fogão cujo defeito só se torna perceptível meses depois do recebime
- A loja virtual recebe reclamação eletrônica e confirma imediatamente seu recebimento. Nos 
- Em transporte aéreo internacional, cliente ajuíza pedido de danos morais por extravio de b
- Um contrato de plano de saúde é administrado por entidade de autogestão. Qual ressalva dev
- Uma consumidora apresenta vício de qualidade em produto essencial recém-entregue. Qual sol
- Uma sociedade fornecedora é utilizada de modo que sua personalidade constitui obstáculo ef
- Uma geladeira apresenta vício de fabricação, sem lesão externa. O fabricante é conhecido e
- Duas partes desejam alterar contratualmente o prazo ordinário de reparo de vício em produt
- Um consumidor apresentou reclamação comprovada por vício aparente dentro dos 90 dias, mas 
- Uma loja virtual vende aparelho acompanhado de seguro acessório. O comprador exerce tempes
- Uma empresa concessionária suspende serviço público individualizado por inadimplemento de 
- Fraudador obtém empréstimo mediante identidade falsa em operação bancária, decorrendo dano
- Um laboratório presta serviço com defeito que causa lesão, e o profissional liberal respon
- Uma pessoa sofre lesão por defeito de segurança do produto e conhece o dano e o fabricante
- A Constituição Federal prevê defesa do consumidor como dever estatal no art. 5º, XXXII, e 
- Conforme o CDC, produto é somente um objeto móvel e material, razão pela qual imóvel adqui
- O art. 4º, I, do CDC reconhece vulnerabilidade do consumidor no mercado, e o inciso III in
- A inversão judicial do ônus da prova prevista no art. 6º, VIII, ocorre automaticamente em 
- Uma pessoa que adquire computador para uso pessoal é consumidora destinatária final, sem n
- O art. 18 do CDC impede que o consumidor reclame ao comerciante pelo vício do produto quan
- Se o consumidor contrata fora do estabelecimento comercial, o art. 49 do CDC prevê prazo d
- Por força do art. 14, § 4º, do CDC, a responsabilidade pessoal de profissional liberal exi
- A reclamação comprovadamente formulada pelo consumidor ao fornecedor dentro do prazo legal
- A norma do art. 22 do CDC obriga continuidade do serviço público essencial e, por isso, im
- Em contrato eletrônico, o fornecedor deve permitir ao consumidor identificar e corrigir er
- A Súmula 608 do STJ determina que o CDC rege contratos de plano de saúde inclusive quando 
- A Súmula 479 do STJ atribui responsabilidade objetiva a instituições financeiras pelos dan
- Um contrato de adesão que prevê prazo de 150 dias para sanar vício de produto dispensa man
- No fato do produto, fabricante identificado responde objetivamente por defeito de seguranç
- O Tema 210 do STF, após sua atualização em 2023, permite limitar com base nas Convenções d
- O art. 28, § 5º, do CDC permite considerar a desconsideração quando a personalidade jurídi
- Um fornecedor descobre risco grave de um produto já vendido, mas deve aguardar reclamação 
- Em compra online com arrependimento tempestivo, o Decreto 7.962/2013 determina rescisão de
- Na prestação de serviço com vício de qualidade, as opções do art. 20 do CDC somente ficam 
```

## Pedido 28: Procedimentos, evidências, amostragem e papéis de trabalho (`auditoria.procedimentos-evidencias`, 583 tópicos de edital)

```
Matéria: auditoria.procedimentos-evidencias (Procedimentos, evidências, amostragem e papéis de trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Planejamento, riscos e programa de auditoria": 14
- "2. Procedimentos, testes de controle e procedimentos substantivos": 23
- "3. Evidências, confiabilidade, conciliações e achados": 20
- "4. Amostragem, seleção de itens e avaliação de resultados": 20
- "5. Papéis de trabalho, supervisão e qualidade da execução": 23

Já existem (não repita):
- Uma entidade exige que cada pagamento seja aprovado por duas pessoas antes da transferênci
- Após obter diversas planilhas internas sem conciliação, a equipe afirma que a grande quant
- Para verificar um controle mensal de conciliação, o auditor refaz independentemente o proc
- Uma solicitação escrita pede que o cliente confirme diretamente ao auditor seu saldo ou in
- Ao examinar uma despesa registrada, o auditor compara a quantia contabilizada à nota, ao c
- Antes de começar os testes de pagamentos, a equipe registra a questão de auditoria, o crit
- O auditor examina um contrato assinado para verificar cláusulas de prazo e reajuste. Qual 
- Uma equipe compara o consumo mensal de energia por unidade operacional com o gasto registr
- Há suspeita de despesas efetuadas, mas não reconhecidas. A equipe escolhe notas fiscais re
- Na visita a um depósito, o auditor presencia a contagem dos materiais realizada pelos empr
- O saldo bancário do razão diverge do extrato por depósitos em trânsito e pagamentos ainda 
- Ao usar planilha de vendas produzida pela entidade em procedimento analítico, o auditor de
- A equipe pretende confiar em controle automático de validação de cadastros para reduzir te
- Uma análise identificou falta de autorização prévia em pagamentos e registrou, separadamen
- Uma solicitação de confirmação externa positiva de saldo não foi respondida. Que providênc
- Ao revisar um teste, a pessoa supervisora encontra apenas a anotação '30 itens conferidos,
- Uma equipe afirma que selecionou amostra estatística porque escolheu, por julgamento, quar
- Em teste de detalhes por amostragem, algumas faturas selecionadas apresentam valores conta
- Para investigar possíveis irregularidades nos maiores contratos, o auditor examina somente
- Uma população possui poucos lançamentos muito elevados e numerosos pagamentos pequenos. A 
- Em amostra, um erro é atribuído a evento excepcional e a equipe quer retirá-lo da projeção
- Uma amostra foi selecionada corretamente, mas a equipe aplicou teste que não verifica o ob
- O relatório preliminar aponta prejuízo efetivo porque um processo possuía apenas uma propo
- O auditor escolhe uma amostra de pagamentos de janeiro a dezembro, mas a base extraída omi
- Após evidência contraditória sobre pagamentos, a equipe pretende manter a conclusão inicia
- Durante a contagem de materiais, o auditor acompanha a equipe que executa a operação. O pr
- Ao inspecionar fisicamente um veículo registrado no imobilizado, o auditor obtém, pela sim
- Uma equipe detalha no programa de auditoria os procedimentos que pretende executar, as fon
- A suficiência das evidências diz respeito à sua confiabilidade, ao passo que a adequação e
- Diante da necessidade de conferir a multiplicação de quantidade por preço em uma nota fisc
- Quando a equipe examina todos os pagamentos do período, executa uma amostragem estatística
- O exame de faturas para verificar seus valores pode ser teste substantivo de detalhes, enq
- Uma solicitação de confirmação negativa encaminhada a cliente ficou sem resposta; por isso
- Para empregar dados extraídos do sistema auditado em revisão analítica, o auditor deve con
- Ao elevar o risco avaliado de distorção, a equipe pode compensá-lo automaticamente reunind
- Mesmo que um controle esteja formalmente descrito, a equipe que pretende confiar em sua ef
- Se três pagamentos selecionados não apresentam a autorização prevista, a equipe deve trata
- Ao receber informação de terceiro aparentemente independente que contradiz documentos da e
- Uma matriz de achados que registra descumprimento de cláusula contratual pode declarar dan
- Em seleção de amostra de testes de detalhes, o auditor deve projetar à população pertinent
- A seleção intencional dos vinte maiores lançamentos, por ser baseada em critério objetivo,
- O tratamento de distorção identificada como anomalia exige elevado grau de certeza de sua 
- O exame integral de todos os itens elimina, além do risco de amostragem, os riscos de exec
- Os papéis de trabalho devem permitir que um auditor experiente sem ligação anterior ao tra
- Uma relação inesperada em procedimento analítico substantivo comprova, por si só, a existê
```

## Pedido 29: Obrigação tributária e fato gerador (`direito-tributario.obrigacao-fato-gerador`, 581 tópicos de edital)

```
Matéria: direito-tributario.obrigacao-fato-gerador (Obrigação tributária e fato gerador)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Obrigação principal, obrigação acessória e penalidades": 22
- "2. Hipótese de incidência, fato gerador e interpretação": 20
- "3. Sujeitos ativo e passivo, contribuinte e responsável": 22
- "4. Solidariedade e capacidade tributária passiva": 16
- "5. Domicílio tributário, DTE e integração das regras": 20

Já existem (não repita):
- Uma empresa realizou operação tributável e deve recolher R$ 8.000 previstos na legislação.
- Uma loja não entrega declaração exigida pela legislação para fiscalização, embora não exis
- Uma lei estabelece que certa operação econômica, realizada dentro do território competente
- Em contrato sujeito a condição suspensiva, a condição é implementada meses após a assinatu
- Uma pessoa participa diretamente do fato descrito como tributável e a lei a indica para pa
- Em cobrança dirigida a dois devedores efetivamente solidários, um deles exige que o outro 
- Um adolescente possui bem que se enquadra em hipótese tributável. Pode a menoridade civil,
- Uma pessoa natural não elegeu domicílio tributário e possui residência habitual conhecida.
- Um negócio jurídico está sujeito a condição resolutória que pode extinguir seus efeitos fu
- Uma norma hipotética incide sobre propriedade de bem localizado em determinado território 
- Uma pessoa obteve receita que se enquadra em fato legal tributável, mas o negócio subjacen
- Uma pessoa compra imóvel e o contrato determina que ela reembolse tributo atribuído por le
- O território de uma pessoa jurídica pública é desmembrado, constituindo outra entidade. An
- Dois devedores solidários devem R$ 12.000 e um deles paga integralmente a prestação. Qual 
- Uma unidade comercial opera regularmente de fato, mas seu ato constitutivo não foi registr
- Uma pessoa elege endereço fiscal sem estrutura real, dificultando comprovadamente fiscaliz
- Uma sociedade tem sede em cidade distinta daquela em que seu estabelecimento realizou oper
- A legislação estabelece multa de 90% do tributo para infração ordinária, sem fraude dolosa
- Uma pessoa apresenta declaração fiscal com atraso e recebe multa. Também resta pendente a 
- Um ato foi praticado para dissimular a ocorrência de fato tributável, e a autoridade prete
- Em negócio oneroso hipotético, a lei define fato ocorrido na conclusão jurídica definitiva
- Uma lei designa expressamente uma terceira pessoa para pagar tributo, apesar de ela não te
- Um de dois devedores solidários recebe isenção concedida pessoalmente a ele. O débito comu
- Em outubro de 2026, uma questão reproduz como direito vigente a expressão proposta na LC 2
- Uma pessoa jurídica recebe intimação de ato administrativo por Domicílio Tributário Eletrô
- Uma pessoa deixa de emitir documento fiscal exigido para controle de operações. O dever or
- Pelo art. 113, § 1º, o pagamento de multa tributária não pode ser objeto de obrigação prin
- Em regra-matriz de incidência, o critério espacial localiza a ocorrência tributável, ao pa
- Quando um negócio tributável se sujeita a condição suspensiva, o art. 117, I, considera-o 
- A pessoa que possui relação pessoal e direta com a situação constitutiva do fato gerador p
- Dois devedores solidários podem opor benefício de ordem, exigindo que o credor público exe
- O art. 126 do CTN admite capacidade tributária passiva de pessoa natural civilmente incapa
- Um contrato entre particulares estipula quem arcará economicamente com imposto. Pelo art. 
- A autoridade pode recusar domicílio tributário escolhido quando a eleição dificultar a fis
- Em obrigação tributária referente a situação jurídica, o art. 116, II, considera ocorrido 
- Se um dos solidários paga integralmente o crédito, o art. 125, I, estabelece que o pagamen
- A Lei Complementar nº 236/2026 inseriu no CTN a regra de que qualquer multa isolada, mesmo
- O art. 127-A, incluído em setembro de 2026, permite intimações administrativas tributárias
- O art. 118 do CTN determina que a invalidade civil de ato concreto extingue automaticament
- O art. 116, parágrafo único, pressupõe atos destinados a dissimular ocorrência do fato ger
- Um dos solidários recebe benefício de isenção concedido pessoalmente a ele. A regra geral 
- Na obrigação tributária principal, o responsável não se confunde com contribuinte: o prime
- A LC nº 236/2026 fez entrar em vigor o texto proposto para o art. 124, I, que exige cumula
- Uma sociedade sem registro formal, mas caracterizada como unidade econômica profissional, 
- O art. 127-A estabelece que todas as intimações tributárias são exclusivamente eletrônicas
```

## Pedido 30: Concessão e permissão de serviço público (`direito-administrativo.concessao-permissao`, 580 tópicos de edital)

```
Matéria: direito-administrativo.concessao-permissao (Concessão e permissão de serviço público)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Prestação de serviços públicos, delegação, permissão e autorização": 31
- "2. Outorga, licitação, contrato, prazo, tarifa e equilíbrio econômico-financeiro": 16
- "3. Execução, encargos, fiscalização, subconcessão e intervenção": 17
- "4. Extinção da concessão, caducidade, encampação e reversão": 23
- "5. Parcerias público-privadas, Programa de Parcerias de Investimentos e comparação final": 13

Já existem (não repita):
- Uma delegação de serviço público será licitada para empresa ou consórcio, com prestação po
- O ente titular pretende delegar prestação precária a pessoa física selecionada por licitaç
- Um ato autoriza particular a exercer atividade sob condições revogáveis, sem estabelecer c
- Uma secretaria distribui internamente o atendimento entre departamentos, sem constituir ou
- Um contrato obriga o operador a prestar serviço com regularidade, continuidade, cortesia e
- Antes de publicar edital de delegação, o poder concedente precisa formalizar justificativa
- Uma licitação de concessão adota como critério de julgamento o preço tarifário ofertado pe
- O concedente deseja atribuir exclusividade a um delegatário por entender inviável a concor
- Após a proposta vencedora, surge encargo legal que encarece comprovadamente o serviço. Não
- Uma concessão comum precisará prever as condições de extensão do prazo no seu instrumento 
- O edital prevê exploração comercial de lojas em terminal como receita acessória. Na defini
- A operadora pretende transferir a outra empresa a exploração de parte do serviço concedido
- Os controladores de uma concessionária vendem o controle societário sem obter anuência pré
- Após falhas severas, o concedente decreta intervenção na operação. Que prazos do art. 33 d
- O concedente quer retomar uma concessão durante sua vigência, sem falta do operador, em ra
- A concessionária persiste em falhas contratuais, embora intimada detalhadamente e com opor
- Ao expirar o prazo, parte do investimento em equipamento reversível ainda não está amortiz
- O poder concedente deixa de cumprir obrigações contratuais e a concessionária deseja resci
- Um serviço concedido será remunerado por tarifa do usuário e contraprestação pecuniária co
- Uma PPP destina-se à prestação continuada de serviços de infraestrutura dos quais a Admini
- Uma proposta é apresentada como PPP por R$ 9 milhões, com serviço disponível por dez anos,
- Um projeto estruturado como PPP prevê contrato de 33 anos e prorrogação contratual de cinc
- Um consórcio vence licitação para PPP. Antes de assinar, as partes discutem constituir uma
- Um projeto de concessão comum é qualificado no Programa de Parcerias de Investimentos. Que
- O contrato de PPP prevê pagamento público integral antes de qualquer serviço estar disponí
- A autorização de serviço público, em sua formulação doutrinária típica, é descrita como at
- A autorização precária para instalar barraca em praça pública constitui necessariamente au
- O art. 175 da Constituição admite prestação direta e delegação por concessão ou permissão,
- Diferentemente da concessão, a permissão de serviço público prevista na Lei 8.987/1995 é d
- O corte por inadimplemento de usuário, quando preenchidos os demais requisitos do art. 6º,
- Se o poder concedente entender conveniente atribuir exclusividade, o art. 16 da Lei 8.987/
- Quando o edital de concessão prevê receitas acessórias ligadas ao serviço, o parágrafo úni
- Em concessões comuns abrangidas apenas pela Lei 8.987/1995, o prazo máximo geral é de 35 a
- A concessionária pode contratar terceiros para manutenção ou atividades complementares do 
- A subconcessão, expressamente autorizada pelo concedente e prevista no contrato, pode ser 
- Uma intervenção em concessão foi declarada por decreto com indicação de interventor e obje
- Ainda que a intervenção não tenha respeitado seus pressupostos legais, o poder concedente 
- Conforme o art. 27 da Lei 8.987/1995, a transferência de concessão ou de controle societár
- Se o poder concedente quiser retomar a concessão por interesse público durante o prazo, po
- A rescisão da concessão por iniciativa da concessionária, diante de descumprimento contrat
- Uma concessão comum remunerada por tarifas, sem contraprestação pecuniária do parceiro púb
- O art. 2º, § 4º, da Lei 11.079/2004 impede PPP cujo contrato valha menos de R$ 10 milhões,
- O art. 9º da Lei 11.079/2004 permite que a Administração Pública detenha a maioria do capi
- Uma concessão comum qualificada no PPI mantém seu regime jurídico básico, pois a Lei 13.33
- A autorização de atividade mencionada em legislação setorial deve receber, sem exame do di
```

## Pedido 31: Escrituração e operações contábeis (`contabilidade.escrituracao-operacoes`, 580 tópicos de edital)

```
Matéria: contabilidade.escrituracao-operacoes (Escrituração e operações contábeis)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Plano de contas, natureza dos saldos e partidas dobradas": 22
- "2. Diário, Razão, documentação, balancete e conciliação": 33
- "3. Compras, vendas, caixa versus competência e descontos": 18
- "4. Juros, variações, folha, tributos e provisões": 14
- "5. Depreciação, baixa, conciliação final e apuração do resultado": 13

Já existem (não repita):
- Uma empresa recebe R$ 8.000 de cliente por venda reconhecida no mês anterior. No recebimen
- Os sócios integralizam R$ 25.000 em dinheiro no início das atividades. Qual lançamento rep
- Ao organizar os registros mensais, a equipe deseja consultar todos os lançamentos associad
- Um serviço de R$ 4.500 foi integralmente prestado em março e só será recebido em maio. Qua
- Mercadorias contabilizadas no estoque por R$ 6.000 são vendidas por R$ 9.000 a prazo, em i
- Uma empresa compra estoque a prazo por R$ 3.000, com comprovante hábil. Qual registro pres
- No fechamento de abril, empréstimo gerou R$ 350 de juros ainda não pagos. Como reconhecer 
- No balancete, débitos somam R$ 48.000 e créditos também R$ 48.000. Que conclusão pode ser 
- A empresa reconhece R$ 600 de depreciação do equipamento no mês, sem desembolso. Qual par 
- No fechamento, salários brutos são R$ 10.000; retenções dos empregados totalizam R$ 1.400,
- Uma compra de mercadorias tem preço de tabela de R$ 5.000 e desconto comercial incondicion
- Em dezembro, uma empresa paga R$ 3.600 por três meses futuros de aluguel, com apropriação 
- Uma empresa efetua milhares de pequenos registros semelhantes fora da sede e quer lançá-lo
- Extrato bancário marca R$ 9.000; depósito de R$ 1.500 já escriturado ainda está em trânsit
- Há obrigação presente surgida de evento passado, saída de recursos provável e melhor estim
- O saldo de um direito monetário a receber em moeda estrangeira aumenta de R$ 12.000 para R
- Empresa encerra junho com aluguel incorrido de R$ 2.500, ainda não pago. Em julho, quita e
- Máquina custa R$ 42.000, tem valor residual estimado de R$ 6.000 e vida útil de seis anos.
- Equipamento custa R$ 20.000, possui depreciação acumulada de R$ 12.000 e é vendido à vista
- Uma empresa lança D Estoques R$ 9.000 e C Fornecedores R$ 9.000 referente a máquina compra
- Ao encerrar o exercício, Receita de Serviços apresenta crédito de R$ 70.000 e Despesas têm
- Venda a prazo de R$ 16.000, custo R$ 9.000, sofre devolução de R$ 4.000 de mercadorias cuj
- A entidade contrata obrigação com correção monetária: principal devido de R$ 15.000 aument
- Uma equipe calcula obrigações mensais de décimo terceiro com remuneração-base constante de
- No período, entidade prestou R$ 40.000 em serviços, recebeu R$ 25.000; incorreu R$ 12.000 
- Uma companhia recebe R$ 20.000 de empréstimo bancário e registra D Bancos e C Empréstimos 
- Uma empresa registra, no Razão, todos os fatos por ordem cronológica geral e utiliza o Diá
- Em dezembro a entidade conclui um serviço de R$ 5.000 e o cliente paga somente em janeiro.
- Após comprar mercadoria a prazo de R$ 3.000 para revenda, uma empresa deve debitar Despesa
- Ao fim do mês, encargos de juros de R$ 200 foram incorridos e ainda não pagos. O lançament
- Uma máquina tem custo de R$ 10.000 e depreciação acumulada de R$ 4.000. Ao aliená-la por R
- Um plano de contas apresenta Clientes como conta sintética e Clientes Alfa como analítica,
- O balancete de R$ 30.000 de débitos e R$ 30.000 de créditos garante que nenhuma transação 
- Uma venda a prazo de mercadorias no inventário permanente requer registro da receita e out
- Uma compra de R$ 10.000 traz abatimento comercial incondicional de R$ 1.000, sem outros co
- Em folha de R$ 8.000 brutos com R$ 1.000 de retenções dos empregados, o registro pode debi
- Em obrigação trabalhista surgida ao longo dos meses, a apropriação de décimo terceiro deve
- Depósito de R$ 2.000 já contabilizado em Bancos, ainda não refletido no extrato por trânsi
- Uma empresa mantém uma obrigação presente de evento passado, com saída provável de recurso
- Uma entidade enfrenta demanda judicial com obrigação apenas possível, não considerada saíd
- Uma máquina de custo R$ 50.000, valor residual R$ 5.000 e vida útil de nove anos, sob méto
- Após encerrar receita de R$ 60.000 e despesas de R$ 45.000 em ARE, uma entidade deve debit
- Em reconciliação de saldos, tarifa de R$ 50 aparece no extrato, mas não nos livros. O ajus
- Uma empresa presta R$ 20.000 em serviços, recebe R$ 10.000, incorre R$ 7.000 de despesa at
- Uma empresa recebe R$ 9.000 antecipadamente em novembro por serviço que somente será prest
```

## Pedido 32: Intervenção do Estado na propriedade (`direito-administrativo.intervencao-estado-propriedade`, 578 tópicos de edital)

```
Matéria: direito-administrativo.intervencao-estado-propriedade (Intervenção do Estado na propriedade)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Função social, limites administrativos e zoneamento": 18
- "2. Desapropriação: pressupostos, espécies, fases e indenização": 24
- "3. Servidão administrativa, requisição e ocupação temporária": 27
- "4. Tombamento: patrimônio cultural, efeitos e indenização": 13
- "5. Desapropriação indireta, retrocessão e distinções finais": 18

Já existem (não repita):
- Um município estabelece limite geral de três pavimentos para imóveis residenciais situados
- Uma enchente provoca risco iminente à população, e autoridade competente utiliza temporari
- Uma empresa delegatária instala linha de transmissão sobre faixa delimitada de imóvel rura
- Um terreno não edificado vizinho a uma ponte em construção é usado provisoriamente para gu
- Um casarão de reconhecido valor histórico é tombado, permanecendo na titularidade da famíl
- Uma desapropriação ordinária é declarada para construir hospital público. Qual é, em regra
- O poder público declara, por decreto, determinado imóvel de utilidade pública, mas não cel
- Uma propriedade urbana é abrangida por parâmetros de zoneamento constantes do plano direto
- A autoridade recebe comunicação de proprietário de imóvel tombado sem recursos para execut
- Uma área é declarada de interesse social com fundamento na Lei nº 4.132/1962. Qual prazo e
- Uma obra viária incluirá imóveis contíguos indispensáveis e outras zonas valorizadas extra
- Uma concessionária pretende promover desapropriação necessária à execução de serviço deleg
- Um decreto de utilidade pública caducou após cinco anos sem acordo nem ajuizamento. A auto
- O município desapropria imóvel para escola, mas decide utilizá-lo como unidade de saúde pú
- Proprietário recebe notificação do início de tombamento, mas a inscrição no Livro do Tombo
- Uma pessoa pretende pintar a fachada e restaurar partes de um imóvel tombado, alegando que
- Um vizinho projeta painel publicitário que encobre a vista de prédio tombado. Ele afirma q
- Sem instaurar desapropriação regular, o poder público constrói estrada sobre imóvel partic
- Uma autarquia desapropriou um imóvel, que posteriormente ficou sem destinação de interesse
- Uma área privada foi incorporada de fato à instalação permanente de equipamento público, s
- Uma desapropriação para reforma agrária é analisada segundo o art. 184 da Constituição. Co
- Uma desapropriação por utilidade pública alcançará bens pertencentes a município, promovid
- Na ação expropriatória fundada no DL nº 3.365/1941, o proprietário apresenta contestação q
- Um proprietário de bem tombado alega que o antigo art. 22 do DL nº 25/1937 obriga, hoje, a
- O proprietário sem recursos comunica necessidade de conservação de imóvel tombado. Reconhe
- A Constituição assegura o direito de propriedade no art. 5º, XXII, e impõe sua função soci
- Uma regra municipal que impõe recuo mínimo a todos os terrenos de uma zona constitui desap
- Diante de perigo público iminente, a autoridade pode requisitar bem particular para atendi
- Uma linha de transmissão instalada permanentemente em faixa de imóvel privado caracteriza,
- O tombamento de casarão de valor cultural normalmente preserva a propriedade particular, m
- A indenização por desapropriação comum obedece, em todas as hipóteses constitucionais, ao 
- Decreto de utilidade pública que não resulta em acordo nem ação de desapropriação por cinc
- Na Lei nº 4.132/1962, o prazo para efetivar a desapropriação por interesse social e inicia
- O tombamento provisório de bem particular, iniciado pela notificação, equipara-se em regra
- O proprietário de imóvel tombado pode demolir o edifício sem autorização, desde que manten
- O art. 4º do Decreto-Lei nº 3.365/1941 permite que a desapropriação por zona compreenda ár
- O art. 36 do Decreto-Lei nº 3.365/1941 admite ocupação temporária de terreno não edificado
- O antigo art. 22 do Decreto-Lei nº 25/1937, revogado em 2015, continua impondo a cada prop
- Uma estrada foi construída por ocupação irregular de imóvel particular. Para a pretensão d
- Uma área desapropriada para escola passa a abrigar hospital público. Essa alteração caract
- O Decreto-Lei nº 3.365/1941, art. 3º, admite que concessionários e outros agentes legalmen
- Se imóvel tombado é vendido a novo proprietário, o tombamento perde eficácia imediatamente
- Uma desapropriação de imóvel rural por interesse social para fins de reforma agrária pode 
- A mera edição de declaração de utilidade pública transfere imediatamente o domínio ao ente
- O proprietário de bem tombado que não dispõe de recursos para obras necessárias de conserv
```

## Pedido 33: Recuperação judicial, extrajudicial e falência (`direito-empresarial.falencia-recuperacao`, 574 tópicos de edital)

```
Matéria: direito-empresarial.falencia-recuperacao (Recuperação judicial, extrajudicial e falência)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Crise empresarial, princípios e âmbito da lei": 17
- "2. Recuperação judicial: requisitos, processamento e verificação de créditos": 19
- "3. Plano, assembleia, recuperação especial e extrajudicial": 23
- "4. Falência: pedido, decretação, efeitos e ações": 22
- "5. Atos ineficazes, liquidação, créditos e extinção de obrigações": 19

Já existem (não repita):
- Uma indústria possui equipamentos e imóveis avaliados acima do passivo, mas não dispõe de 
- Ao estudar a recuperação de diferentes entidades em dificuldades, uma consultora identific
- Uma sociedade mantém sede formal em cidade A, mas concentra direção econômica, grandes con
- Uma empresa atua regularmente há um ano e onze meses e deseja pedir recuperação judicial o
- Um empresário obteve concessão de recuperação judicial há três anos e agora enfrenta nova 
- Após análise dos documentos, o juiz defere o processamento de uma recuperação judicial. Qu
- O processamento da recuperação foi deferido em 1º de março. Qual é a regra geral de duraçã
- Uma loja solicitou recuperação em maio. Há uma dívida contratada em abril com vencimento p
- Após o edital inicial da recuperação, uma credora percebe divergência de valor e pretende 
- Em recuperação judicial com o devedor ainda à frente da empresa, o administrador judicial 
- Uma sociedade apresentou documentação regular e teve o processamento da recuperação deferi
- Na votação do plano de recuperação, a classe de credores com garantia real apresenta maior
- Em assembleia, um plano não obteve aprovação regular de todas as classes. A concessão judi
- Uma empresa de pequeno porte optou expressamente pelo plano especial desde a petição inici
- Uma empresa busca homologar recuperação extrajudicial para alcançar também credores não si
- Credor tem título executivo protestado de 45 salários mínimos, sem justificativa jurídica 
- Um credor empresário, regularmente inscrito, e outro sem domicílio no Brasil avaliam pedir
- Uma sociedade contesta pedido de falência por impontualidade e pretende depositar valor pa
- Uma sentença decretou a falência de comerciante e fixou o termo legal. Qual limite de retr
- Um equipamento pertencente a terceiro foi arrecadado com bens do falido, sem transferência
- Uma empresa com execução fiscal em curso pede recuperação. O processo fazendário aponta pe
- A falência de sociedade limitada foi decretada. Credores pedem que, pela simples quebra, s
- Dentro do termo legal, o devedor quitou antecipadamente dívida ainda não vencida; em outro
- Na liquidação, há proposta para vender toda a empresa, proposta para alienar filial e outr
- Realizado todo o ativo da massa, pagaram-se mais de 25% dos créditos quirografários, mas a
- Uma sociedade empresária tem passivo elevado, mas permanece gerando fluxo de caixa e possu
- Uma instituição financeira em dificuldades pode pedir recuperação judicial com base direta
- Um credor possui título contra empresa em recuperação que já existia no dia do ajuizamento
- Em recuperação judicial ordinária, a nomeação de administrador judicial pelo art. 52 impli
- Credores de classe trabalhista e de microempresas, quando deliberam sobre plano judicial, 
- A devedora apresentou plano judicial em 60 dias, mas o juiz decidiu aguardar a homologação
- Uma pequena sociedade empresária optou na petição inicial pelo plano especial; sua propost
- Ao preparar recuperação extrajudicial, a devedora incluiu créditos tributários no plano e 
- Um credor demonstra que executou dívida líquida contra empresário, que não pagou, não depo
- A decretação de falência impede a continuidade dos contratos bilaterais ainda não concluíd
- Citada em pedido de falência por impontualidade de títulos protestados, uma sociedade depo
- Decretada a falência em outubro, o juízo fixou termo legal retroativo por 120 dias contado
- Fornecedor vendeu mercadoria a crédito, entregou-a dentro dos quinze dias anteriores ao pe
- Para tornar ineficaz perante a massa o pagamento de dívida ainda não vencida realizado den
- Um credor identifica alienação anterior à quebra realizada com conluio entre devedor e adq
- Na realização do ativo, a Lei nº 11.101/2005 admite apenas leilão presencial ou eletrônico
- Na falência, após a satisfação dos extraconcursais segundo a lei, o saldo do crédito com g
- O prazo de três anos para extinção de obrigações do falido, introduzido pela reforma, come
- Mesmo sem suspensão automática da execução fiscal pela recuperação judicial, pode haver co
- Quando o pedido de falência é julgado improcedente, o recurso previsto na Lei nº 11.101/20
```

## Pedido 34: Crimes contra a Administração Pública (`direito-administrativo.crimes-administracao-publica`, 573 tópicos de edital)

```
Matéria: direito-administrativo.crimes-administracao-publica (Crimes contra a Administração Pública)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sujeitos e estrutura dos crimes funcionais": 19
- "2. Peculato e infrações sobre bens e registros": 19
- "3. Concussão, excesso de exação e corrupções": 19
- "4. Prevaricação, condescendência, advocacia e sigilo": 19
- "5. Condutas de particulares e confronto avançado dos tipos": 24

Já existem (não repita):
- Uma pessoa exerce temporariamente uma função pública sem receber remuneração. Em apuração 
- Um responsável pela guarda de valores de terceiros depositados sob custódia administrativa
- Um funcionário não tem a guarda de equipamentos, mas utiliza uma chave de acesso decorrent
- Durante atendimento obrigatório, o responsável impõe a entrega de determinada quantia inde
- Um funcionário pede pagamento clandestino em razão de suas atribuições. A pessoa procurada
- Um particular promete quantia a funcionário para que retarde um ato de ofício. A promessa 
- Um responsável deixa deliberadamente de praticar ato obrigatório para prejudicar pessoa co
- Ao saber que um subordinado praticou infração funcional, a pessoa responsável deixa de res
- Pessoa investida em função pública utiliza sua posição para patrocinar, perante a Administ
- Uma pessoa concorre culposamente para peculato cometido por terceiro. Após a condenação, m
- Uma pessoa entrega pagamento superior ao devido por engano espontâneo. Quem recebe a quant
- No procedimento de arrecadação, funcionário exige tributo efetivamente devido, porém deter
- Após aceitar promessa de vantagem indevida vinculada à função, o funcionário efetivamente 
- Sem promessa ou recebimento de vantagem indevida, um funcionário pratica ato contra o deve
- Pessoa contratada por empresa conveniada executa, para a Administração, atividade pública 
- Com conhecimento de que um banco de dados administrativo é restrito, funcionário empresta 
- Dois episódios ocorrem: no primeiro, funcionário autorizado exclui registro correto para c
- Um particular, sem investidura funcional, assume de fato o exercício de atividade pública 
- Para impedir a execução de ato administrativo legal, uma pessoa ameaça seriamente o execut
- Um intermediário exige dinheiro alegando que consegue influenciar decisão de funcionário, 
- Após consumir combustível público em benefício privado, agente devolve dinheiro equivalent
- Um funcionário exige contribuição social que deveria saber indevida, recebe o valor como s
- No julgamento de crime previsto no capítulo dos funcionários, comprova-se que o autor exer
- Uma verba pública possui destinação estabelecida em lei. Quem administra seus recursos, se
- Um funcionário solicita vantagem para facilitar determinada providência relacionada materi
- Uma pessoa exerce sem remuneração função pública por período limitado e, durante essa ativ
- Funcionário subtrai objeto de depósito usando acesso especial da função, mas sem ter posse
- Pessoa encarregada da guarda funcional de objeto pertencente a particular pode praticar pe
- Um funcionário comete peculato doloso e restitui integralmente a quantia antes do trânsito
- Um funcionário impõe pagamento indevido por causa da função e a pessoa abordada se recusa 
- A solicitação de vantagem por funcionário somente caracteriza corrupção passiva quando o d
- No art. 319, não basta verificar atraso indevido de ato de ofício: para caracterizar preva
- Um funcionário deixa de comunicar infração praticada por amigo que não é seu subordinado. 
- O interesse privado patrocinado por funcionário perante a Administração pode ser legítimo 
- O funcionário que intencionalmente empresta senha restrita para terceiro não autorizado co
- Na cobrança de tributo efetivamente devido, utilizar meio vexatório e mais gravoso que o a
- Um particular oferece vantagem a funcionário para obter retardo de ato de ofício e recebe 
- Quem recebe dinheiro no exercício da função por equívoco de outra pessoa e, conhecendo o e
- O art. 313-A, sobre alterações indevidas em dados administrativos por funcionário autoriza
- Na aplicação do art. 327, § 2º, a função de direção em empresa pública pode ensejar aument
- Uma pessoa apenas ignora ordem legal de funcionário, sem violência nem ameaça. Essa ausênc
- Uma pessoa negocia vantagem a pretexto de influir em ato de funcionário e insinua que part
- O aumento do art. 317, § 1º, decorre automaticamente da aceitação de promessa de vantagem,
- No art. 313-B, a modificação não autorizada de sistema ou programa de informática por func
- A devolução posterior de dinheiro público desviado intencionalmente exclui necessariamente
```

## Pedido 35: Pessoa com deficiência: acessibilidade, avaliação e leis esparsas (`legislacao.pessoa-com-deficiencia-acessibilidade-leis-esparsas`, 571 tópicos de edital)

```
Matéria: legislacao.pessoa-com-deficiencia-acessibilidade-leis-esparsas (Pessoa com deficiência: acessibilidade, avaliação e leis esparsas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, Convenção e integração social": 17
- "2. Acessibilidade, barreiras e atendimento prioritário": 38
- "3. Transporte, cão-guia, símbolos e integração": 21
- "4. Autismo, visão monocular, avaliação biopsicossocial e CIF": 12
- "5. Aposentadoria da pessoa com deficiência e aplicação integrada": 12

Já existem (não repita):
- Uma pessoa tem impedimento sensorial duradouro e enfrenta barreiras de comunicação que dif
- Uma pessoa doou sangue recentemente e pede atendimento prioritário na mesma fila em que ag
- Uma gestante apresenta dificuldade de deslocamento temporária e solicita recursos de acess
- Um local oferece atendimento acessível a pessoas com deficiência auditiva e precisa identi
- Em veículo destinado a serviço convencional interestadual, uma empresa precisa cumprir o D
- Uma pessoa com deficiência visual chega a estabelecimento aberto ao público acompanhada de
- Uma unidade de atendimento sustenta que a pessoa com transtorno do espectro autista soment
- Uma pessoa com TEA procura revalidar sua Carteira de Identificação da Pessoa com Transtorn
- Um segurado homem foi reconhecido com deficiência grave durante todo o período exigido e a
- Uma mulher tem deficiência moderada reconhecida e pergunta sobre o tempo contributivo bási
- Ao revisar referências legislativas sobre visão monocular, uma pessoa encontra a anotação 
- Uma praça pública apresenta piso interrompido por obstáculos fixos, enquanto a porta de um
- Em turma regular, estudante com TEA apresenta necessidade demonstrada de apoio para partic
- Um estabelecimento de ensino recusa matrícula em razão de TEA. Qual sanção específica está
- Uma instituição privada condiciona a inscrição de estudante com deficiência ao pagamento d
- Em balcão sem guichê reservado, uma pessoa com deficiência chega enquanto já ocorre atendi
- Uma pessoa com deficiência solicita alteração pontual em procedimento que a impede de aces
- Um relatório apresenta o IFBrM como classificação diagnóstica equivalente à CID e afirma q
- Em requerimento assistencial, uma pessoa tem documentação diagnóstica, mas enfrenta limita
- Uma segurada teve períodos de contribuição sem deficiência e, depois, anos com deficiência
- Um homem de 60 anos tem 15 anos de contribuição, mas comprovou condição de pessoa com defi
- Uma pessoa pretende comprovar tempo com deficiência anterior à vigência da LC nº 142/2013.
- Uma pessoa trabalhou em condições prejudiciais à saúde e, no mesmo intervalo, estava recon
- Uma unidade planeja aplicar o art. 6º da Resolução CSJT nº 386/2024: oferece palestras sem
- Um serviço disponibiliza acesso físico adequado, mas a única forma de orientação é um áudi
- Em um atendimento aberto ao público, uma pessoa com 60 anos completos pode invocar a prior
- Uma pessoa que realizou doação de sangue há 150 dias apresenta o comprovante e exige, por 
- Um estabelecimento aberto ao público que ofereça serviço a pessoas com deficiência auditiv
- Uma empresa de transporte exclusivamente municipal deve conceder, por força direta da Lei 
- Uma pessoa com transtorno do espectro autista é considerada pessoa com deficiência para to
- Em avaliação da deficiência, é possível substituir a análise de limitações de atividade e 
- Uma pessoa com TEA que renova sua Ciptea após cinco anos deve manter o mesmo número de ide
- Em classe comum de ensino regular, a Lei nº 12.764/2012 exige acompanhante especializado p
- Uma pessoa com visão monocular procura a norma específica de classificação de deficiência 
- O Decreto nº 5.296/2004 regulamenta apenas a Lei nº 10.098/2000, de modo que a Lei nº 10.0
- Uma pessoa propõe que os princípios da Convenção promovam a eliminação de barreiras inform
- O art. 8º, I, da Lei nº 7.853/1989 permite cobrar acréscimo de matrícula de estudante com 
- A adoção de mecanismos de diálogo social com escuta direta de pessoas com deficiência é aç
- Uma mulher com 55 anos e 15 anos de contribuição preenche automaticamente o art. 3º, IV, d
- A prova de tempo na condição de pessoa com deficiência anterior à vigência da LC nº 142/20
- Um mesmo período de contribuição pode receber simultaneamente a redução temporal relativa 
- Na classificação do art. 4º, V, do Decreto nº 3.298/1999, deficiência múltipla corresponde
- O Decreto nº 11.487/2023 determinou que o IFBrM fosse imediatamente utilizado como nota de
- Um doador de sangue com comprovante emitido há 100 dias tem prioridade legal após os demai
- Como o Decreto nº 5.296/2004 foi publicado antes da Lei nº 14.626/2023, o rol de prioridad
```

## Pedido 36: Prisões e liberdade provisória (`processo-penal.prisoes-liberdade-provisoria`, 570 tópicos de edital)

```
Matéria: processo-penal.prisoes-liberdade-provisoria (Prisões e liberdade provisória)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos das prisões processuais e das medidas cautelares": 15
- "2. Prisão em flagrante e audiência de custódia": 23
- "3. Prisão preventiva: fundamentos, hipóteses e revisão": 19
- "4. Prisão temporária, domiciliar e cautelares diversas": 28
- "5. Fiança, liberdade provisória e solução de casos complexos": 15

Já existem (não repita):
- Uma pessoa presencia furto sendo executado e pretende impedir que o autor fuja. Conforme o
- Após roubo, um suspeito é perseguido imediatamente por testemunhas e alcançado minutos dep
- Ao controlar prisão em flagrante, o juiz verifica vício grave de legalidade da captura. Qu
- Uma infração permanece em execução por vários dias, sem cessação da conduta permanente. Qu
- O juiz recebe um pedido de preventiva durante investigação. Qual regra expressa do art. 31
- Um suspeito é investigado por crime da lista da Lei nº 7.960/1989. Qual é, em regra, o pra
- A prisão preventiva perde o fundamento porque a testemunha ameaçada já foi protegida e não
- O juiz precisa impor medida menos gravosa e suficiente para impedir a comunicação de inves
- Em infração afiançável cuja pena privativa de liberdade máxima é de três anos, quem pode c
- Uma pessoa é encontrada pouco depois de um furto com os bens subtraídos e outros objetos q
- No curso de processo, o Ministério Público pede prisão preventiva por delito doloso com pe
- No encerramento do prazo da prisão temporária, a unidade de custódia não recebeu comunicaç
- Em pedido de prisão temporária representado pela autoridade policial, qual providência ant
- Ao analisar prisão preventiva de uma investigada gestante por fato sem violência ou grave 
- Para crime doloso punido com pena privativa de liberdade máxima de exatamente quatro anos,
- Ao fixar fiança judicial em infração afiançável com pena máxima de seis anos, o magistrado
- A autoridade judicial avalia captura realizada após induzimento por agentes que criaram si
- Um investigado tem notícia de condenação ainda não definitiva por crime diverso. O juiz in
- Em outubro de 2026, uma audiência de custódia ocorre por videoconferência, mas falha de co
- Em apuração de crime de pequeno potencial ofensivo não incluído no rol da Lei 7.960, o sus
- O magistrado cogita preventiva por conduta sexual contra adolescente que se enquadra no no
- O juiz analisa pedido de preventiva com provas de violência reiterada, participação em org
- A prisão preventiva não é reexaminada ao completar noventa dias, mas decisão anterior cont
- Em crime abrangido pelo art. 2º da Lei 8.072/1990, a prisão temporária é decretada fundame
- Uma pessoa presa em flagrante por crime inafiançável não apresenta perigo cautelar que jus
- Durante uma infração penal em andamento, uma testemunha intervém e detém o autor até a che
- Um policial encontra investigado logo depois de delito com objetos indicativos de autoria.
- Na análise de flagrante legal, caso o juiz afaste os fundamentos da preventiva, poderá con
- Na investigação de delito doloso com pena máxima de cinco anos, o juiz pode decretar prisã
- Uma pessoa é recolhida cautelarmente a sua residência, devendo solicitar permissão judicia
- Em crime comum sujeito à prisão temporária, o prazo inicial é de trinta dias, porque o pra
- Pessoa presa em flagrante deve ter a prisão e o local imediatamente comunicados ao juiz, a
- Depois de decretada prisão temporária e transcorrido seu prazo, o responsável pela custódi
- A autoridade policial pode arbitrar fiança em infração afiançável cuja pena privativa de l
- O art. 313, I, do CPP admite preventiva por si só em qualquer delito culposo cuja pena máx
- Ao constatar que uma cautelar de comparecimento periódico deixou de ser necessária, a auto
- No pedido de prisão temporária por representação policial, o juiz deve decidir sem ouvir o
- A suspensão do exercício de função pública pode ser cautelar diversa da prisão quando houv
- Em prisão preventiva por crime doloso com máximo de seis anos, o decurso do prazo de noven
- Após a Lei 15.487/2026, o inciso VI do art. 313 passou a prever hipóteses relacionadas a c
- O mero fato de uma pessoa não informar endereço fixo satisfaz, isoladamente, as condições 
- Em situação de força maior, a redação de 2026 do art. 310, § 13, permite audiência de cust
- O inciso V do art. 319 estabelece prisão domiciliar integral durante dia e noite para o in
- Um juiz pode examinar a concessão de liberdade provisória sem fiança a pessoa economicamen
- A Lei 15.272/2025 tornou automática a preventiva em flagrantes com violência ou grave amea
```

## Pedido 37: Imputabilidade e concurso de pessoas (`direito-penal.imputabilidade-concurso-pessoas`, 566 tópicos de edital)

```
Matéria: direito-penal.imputabilidade-concurso-pessoas (Imputabilidade e concurso de pessoas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Culpabilidade, imputabilidade e causas de exclusão": 17
- "2. Doença mental, menoridade e exame pericial": 18
- "3. Emoção, paixão e embriaguez: exceções do art. 28": 18
- "4. Concurso de pessoas: autoria, coautoria e participação": 25
- "5. Elementares, circunstâncias e cooperação dolosamente distinta": 22

Já existem (não repita):
- Uma pessoa portadora de doença mental comete fato típico e ilícito. A perícia demonstra qu
- Uma pessoa praticou fato descrito como crime quando tinha 17 anos e 10 meses, mas só foi i
- Uma pessoa que havia bebido voluntariamente até se embriagar pratica um delito. Não há evi
- Duas pessoas executam tarefas combinadas e essenciais de uma única subtração: uma rompe o 
- Uma pessoa conhecia a intenção criminosa de outra e forneceu ferramenta que efetivamente f
- A e B combinam uma infração, mas ninguém inicia sua execução e não há tipo autônomo que pu
- Uma pessoa apresenta perturbação de saúde mental que, no instante do fato, diminuiu signif
- Uma pessoa afirma que agiu por intenso ciúme, mas provas indicam plena compreensão do cará
- Uma pessoa deseja ajudar a executar crime e participa com contribuição efetiva. O julgador
- Uma pessoa ingere substância sem saber de seu efeito porque ela foi introduzida furtivamen
- Uma pessoa conhece e participa da prática de crime próprio cujo tipo exige condição funcio
- Duas pessoas atacam a mesma vítima em momentos próximos, cada qual desconhecendo totalment
- Durante investigação surge dúvida fundada sobre a sanidade mental do investigado. O exame 
- A auxilia B a retirar um bem sem violência; o plano comum era subtração simples. B, em dec
- Uma pessoa inteiramente capaz de compreender o fato pratica conduta proibida sob coação mo
- Diante da prática de crime, fica comprovado que a pessoa se embriagou deliberadamente ante
- Um adulto conscientemente induz adolescente inimputável à prática de fato típico e ilícito
- A e B participam de delito próprio praticado por B. A conhece a qualidade pessoal exigida 
- A pessoa era plenamente capaz quando praticou o fato, mas desenvolveu doença mental incapa
- A participa voluntariamente de subtração sem violência, mas B executa roubo com violência 
- A pratica fato típico acreditando inevitavelmente que a conduta era permitida. Seu discern
- Em empreendimento criminoso conjunto, A organiza a cooperação e dirige os demais, ao passo
- Uma pessoa ingere, por caso fortuito, substância de efeitos análogos ao álcool. A intoxica
- A pessoa adulta utiliza conscientemente outra inteiramente incapaz de compreender a ilicit
- A atua movido por motivo íntimo individual que agrava sua situação; B participa consciente
- Uma pessoa era inteiramente incapaz de compreender a ilicitude ao tempo do fato em razão d
- Uma pessoa praticou o fato aos 17 anos, mas completou 18 antes da sentença. Por ocasião do
- Uma pessoa se embriaga voluntariamente e depois pratica infração. Somente por demonstrar q
- Duas pessoas executam conscientemente tarefas coordenadas para a prática de um mesmo delit
- Uma pessoa participa de delito e presta auxílio consciente, mas secundário em sua relevânc
- A e B planejam delito, mas ninguém inicia atos executórios, e não existe figura autônoma q
- Uma pessoa tem capacidade mental integral no dia da infração, mas desenvolve transtorno in
- Uma intoxicação completa ocorreu por força maior e deixou o agente inteiramente incapaz no
- A participa de crime próprio praticado por B, conhecendo a qualidade pessoal de B que inte
- Duas pessoas atacam a mesma vítima, cada qual sem conhecer a ação da outra. A coincidência
- Uma pessoa era penalmente capaz, mas agiu sob coação moral irresistível praticada por outr
- A auxilia conscientemente B em crime menos grave, mas B pratica outro mais grave. Embora A
- O art. 28, I, prevê que a paixão isolada exclui a imputabilidade quando o sentimento é int
- A pessoa que executou o fato era menor de 18 anos. Outra pessoa adulta a induziu conscient
- A pessoa A possui motivo íntimo especial para praticar delito e B colabora sem conhecer ou
- Uma pessoa com perturbação mental teve comprometimento parcial da capacidade ao tempo do f
- O partícipe desejava unicamente crime menos grave e não podia prever a violência introduzi
- A pessoa organiza e dirige concretamente cooperação para crime que se realiza. A pena pode
- Uma pessoa com plena capacidade mental acredita inevitavelmente que certo comportamento é 
- A pessoa foi intoxicada por força maior, não se tornou inteiramente incapaz, mas perdeu pa
```

## Pedido 38: Ética, democracia e exercício da cidadania (`etica.cidadania-democracia`, 566 tópicos de edital)

```
Matéria: etica.cidadania-democracia (Ética, democracia e exercício da cidadania)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos éticos da democracia e da cidadania": 20
- "2. Soberania popular e mecanismos de participação política": 20
- "3. Direitos e deveres: informação, expressão e controle social": 20
- "4. Ética pública, participação em serviços e responsabilidade": 20
- "5. Cidadania ativa, pluralismo e solução de casos complexos": 20

Já existem (não repita):
- Em reunião comunitária, a maioria aprova impedir uma minoria de usar determinado serviço a
- Uma pessoa afirma que cidadania se manifesta somente no comparecimento às eleições periódi
- Antes de editar uma medida administrativa relevante, realiza-se consulta formal ao eleitor
- Após a adoção de um ato legislativo, o eleitorado é convocado para ratificá-lo ou rejeitá-
- Uma estudante de 17 anos pergunta se seu alistamento eleitoral e seu voto são obrigatórios
- Uma pessoa solicita dados de despesas públicas e recebe exigência de explicar sua motivaçã
- O pedido de informação pública não pôde ser atendido imediatamente. Qual é a combinação or
- No atendimento ao público, uma pessoa recebe tratamento prioritário por amizade com o resp
- Um grupo de usuários pede participação regular na avaliação de serviço público. Que instru
- Uma ouvidoria recebeu manifestação e deve encaminhar a decisão administrativa final ao usu
- Um projeto de lei federal por iniciativa popular reuniu 1% do eleitorado nacional, mas ass
- Um cidadão decide contestar ato lesivo à moralidade administrativa mediante a ação previst
- Uma unidade divulga relatórios e indicadores sem que alguém tenha feito pedido específico.
- Uma campanha institucional usa símbolos e retratos para promover pessoalmente o responsáve
- A administração pretende selecionar representantes para conselho de usuários mediante conv
- Em avaliação de qualidade, o gestor sustenta que satisfação e cumprimento de prazos não de
- Uma servidora federal procura saber se o Decreto nº 1.171/1994 aprova um código ético diri
- Uma consulta pública recebe contribuições de grupos com visões distintas. A gestão argumen
- Uma pessoa apresenta petição administrativa para denunciar abuso e a unidade condiciona o 
- Uma cidade abre participação digital sobre mobilidade, mas a plataforma impede acesso de p
- Uma ouvidoria solicita esclarecimentos à unidade responsável, mas um parecer confunde esse
- Uma iniciativa popular federal reúne os percentuais exigidos, mas seu projeto trata simult
- Uma autoridade sustenta que pode divulgar identificação detalhada de usuários de serviço a
- Uma campanha digital obtém dez mil manifestações idênticas para uma decisão, enquanto uma 
- Uma pessoa apresenta pedido de informação, identifica-se, descreve o objeto e se recusa a 
- Após vencer uma eleição, representantes passam a exercer todo o poder político sem ligação
- Uma consulta formal realizada antes de determinada medida legislativa, para que o eleitora
- Uma pessoa com 71 anos permanece obrigada a alistar-se e a votar porque a Constituição man
- Uma unidade recebe pedido de dados de interesse público e não pode condicionar seu recebim
- Conselhos de usuários da Lei nº 13.460/2017 têm competência geral para aprovar o orçamento
- Um projeto de lei de iniciativa popular federal com assinatura de 1% do eleitorado naciona
- Uma campanha administrativa utiliza o nome e retrato do responsável pelo serviço para prom
- Uma manifestação à ouvidoria deve receber decisão final em vinte dias, com possibilidade d
- A manifestação de uma opinião divergente em consulta pública integra o pluralismo democrát
- Os conselhos de usuários devem possuir representatividade e pluralidade, mas a escolha de 
- Uma consulta popular posterior a um ato administrativo relevante, voltada à sua ratificaçã
- Na iniciativa popular de lei federal, a lei determina que o projeto se limite a um assunto
- No processo administrativo federal, uma decisão que beneficia pessoa próxima da gestão dev
- Por ser regra geral, a publicidade administrativa impõe divulgar integralmente informações
- A ação popular prevista no art. 5º, LXXIII, da Constituição permite controle judicial de a
- A liberdade de manifestação do pensamento assegurada no art. 5º, IV, da Constituição garan
- Uma gestão pode adotar procedimentos simplificados para ampliar o acesso a uma consulta pú
- A avaliação de serviços prevista no art. 23 da Lei nº 13.460/2017 limita-se à medição de p
- Uma pessoa deve poder criticar serviço público, solicitar suas informações e propor melhor
- O Código de Ética aprovado pelo Decreto nº 1.171/1994 constitui, por si só, o regime disci
```

## Pedido 39: Lei 8.112/1990: visão geral do regime jurídico (`legislacao.lei-8112-1990-visao-geral`, 560 tópicos de edital)

```
Matéria: legislacao.lei-8112-1990-visao-geral (Lei 8.112/1990: visão geral do regime jurídico)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Âmbito, conceitos e ingresso no serviço público": 20
- "2. Movimentação, vacância e vida funcional": 20
- "3. Licenças, afastamentos, seguridade e disciplina": 20
- "4. Regime disciplinar, responsabilidades, penalidades e PAD": 20
- "5. Disposições finais, arquitetura, reescrita e resolução": 20

Já existem (não repita):
- Assinale a síntese compatível com o art. 1º da Lei 8.112/1990.
- Considerando o art. 2º, assinale a definição legal de servidor.
- Qual fato marca a investidura em cargo público federal regido pela Lei 8.112?
- Aponte a alternativa que reúne apenas formas de provimento previstas no art. 8º.
- Assinale a alternativa correta sobre a validade do concurso, à luz do art. 12.
- Em qual alternativa a distinção entre remoção e redistribuição está correta?
- De acordo com os arts. 13 e 15 da Lei nº 8.112/1990, os prazos regulares para que o nomead
- Qual forma de provimento derivado consiste no retorno à atividade de servidor aposentado p
- Segundo os arts. 236 e 238 da Lei nº 8.112/1990, o Dia do Servidor Público e a regra geral
- Assinale a alternativa que descreve corretamente exoneração e demissão.
- Um cargo ocupado passa a outro órgão do mesmo Poder, atendidos os requisitos legais. Qual 
- Qual afirmação sobre vencimento e remuneração está de acordo com os arts. 40 e 41?
- Sobre a responsabilidade nas esferas civil, penal e administrativa, assinale a correta.
- Qual situação corresponde à repercussão prevista no art. 126?
- Assinale a alternativa que apresenta somente penalidades do art. 127.
- Aponte a descrição correta da função da Lei 8.112/1990 no sistema jurídico.
- A Administração desloca uma servidora, sem mudar seu quadro; em outro ato, transfere um ca
- Um servidor sofre apuração disciplinar e também responde por dano ao erário. Qual leitura 
- Considerando a arquitetura da lei, qual associação entre título e assunto está correta?
- Um servidor em estágio probatório protocola pedido de licença na unidade de gestão de pess
- A respeito das vantagens pecuniárias previstas no Título III da Lei nº 8.112/1990 (arts. 4
- Qual leitura conjunta dos arts. 7º, 13 e 15 evita confundir os atos de ingresso?
- Um servidor é absolvido porque o juízo penal reconheceu que o fato imputado não existiu. A
- Uma absolvição penal reconhece dúvida relevante, sem declarar inexistente o fato ou negar 
- A autoridade precisa cobrir afastamento de titular de função de direção. Qual capítulo da 
- Qual conjunto apresenta apenas assuntos do Título III da Lei 8.112?
- Uma comissão conclui que a promoção preenche os requisitos para provimento do novo cargo. 
- Em uma reescrita, “as esferas são independentes” é substituída por “nenhuma decisão de uma
- Qual alternativa contém apenas hipóteses que o art. 33 relaciona à vacância, sem classific
- Sobre o campo de aplicação, uma fundação pública federal afirma que seu servidor civil est
- Uma servidora muda de sede por remoção a pedido. Qual conclusão deve ser evitada ao resumi
- Ao resumir o Título IV, um candidato afirma que deveres funcionais e penalidades pertencem
- Em qual caso a leitura literal de um dispositivo deve prevalecer sobre uma generalização f
- Um item associa a posse à investidura, o exercício ao desempenho das atribuições e o estág
- Qual alternativa situa adequadamente o Título VI e evita confundi-lo com o regime discipli
- A respeito do regime disciplinar, das proibições do art. 117, das penalidades e dos limite
- Certo ou errado: a Lei 8.112/1990 define servidor como a pessoa legalmente investida em ca
- Certo ou errado: a investidura em cargo público ocorre com o início do exercício, depois d
- Certo ou errado: a Lei 8.112 aplica-se, por força de seu art. 1º, a todos os servidores ci
- Certo ou errado: a remoção tem como objeto o deslocamento do servidor no âmbito do mesmo q
- Certo ou errado: exoneração figura entre as penalidades disciplinares do art. 127.
- Certo ou errado: os cargos públicos federais, acessíveis a todos os brasileiros, são criad
- Certo ou errado: a idade mínima exigida como requisito básico para a investidura em cargo 
- Certo ou errado: para o primeiro período aquisitivo de férias serão exigidos 12 meses de e
- Certo ou errado: sem qualquer prejuízo, poderá o servidor ausentar-se do serviço por 8 dia
- Certo ou errado: é dever absoluto do servidor público federal cumprir todas as ordens eman
- Certo ou errado: na hipótese de o candidato nomeado não tomar posse no prazo legal de 30 d
- Certo ou errado: a remuneração corresponde ao vencimento do cargo acrescido das vantagens 
- Certo ou errado: as responsabilidades civil, penal e administrativa podem cumular-se, mas 
- Certo ou errado: qualquer absolvição criminal, inclusive por falta de provas, afasta neces
- Certo ou errado: promoção e readaptação aparecem na lei como formas de provimento e também
- Certo ou errado: redistribuição é o deslocamento do servidor, a pedido ou de ofício, dentr
- Certo ou errado: a reintegração é a reinvestidura do servidor estável no cargo anteriormen
- Certo ou errado: a licença concedida dentro de 60 dias do término de outra da mesma espéci
- Certo ou errado: os prazos prescricionais da ação disciplinar previstos no art. 142 da Lei
- Certo ou errado: a investidura em cargo público ocorre com a nomeação, enquanto a posse é 
- Certo ou errado: a exoneração e a aposentadoria são hipóteses de vacância, e nenhuma delas
- Certo ou errado: o art. 126 afasta a responsabilidade administrativa quando a sentença pen
- Certo ou errado: a Lei 8.112 proíbe a prestação gratuita de serviços, ressalvando os casos
- Certo ou errado: todo servidor sujeito à Lei 8.112 possui vínculo estatutário mesmo quando
- Certo ou errado: nos casos de afastamentos ou impedimentos legais do titular de cargo ou f
- Certo ou errado: ao servidor público federal é proibido atuar como procurador ou intermedi
```

## Pedido 40: Código de Ética do servidor público civil federal (`etica.codigo-etica-servidor-federal`, 557 tópicos de edital)

```
Matéria: etica.codigo-etica-servidor-federal (Código de Ética do servidor público civil federal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, alcance e regras deontológicas": 25
- "2. Deveres fundamentais e conduta positiva": 25
- "3. Vedações e situações de risco ético": 24
- "4. Comissões de ética e Sistema de Gestão da Ética": 13
- "5. Denúncia, apuração, decisões e consequências": 13

Já existem (não repita):
- Uma pessoa executa atividade temporária e não remunerada, por vínculo contratual com a adm
- Ao analisar pedido, um agente identifica alternativa formalmente admissível, mas destinada
- Uma pessoa procura informação verdadeira sobre seu requerimento, mas a resposta pode contr
- Em atendimento, servidores deixam usuários por horas em fila sem providência, apesar de so
- Sobre as relações entre vida privada e conduta ética funcional, a afirmação correta é:
- Diante de duas providências legítimas em um atendimento, qual critério atende diretamente 
- Uma chefia pressiona a equipe a conceder vantagem indevida a terceiro. Segundo o inciso XI
- Em uma inspeção legitimamente instaurada, a unidade é solicitada a apresentar registros do
- Alguém sugere vantagem ao usuário para acelerar um procedimento que deveria ser realizado 
- Um agente usa informação interna privilegiada para beneficiar amigo em processo ainda não 
- Uma pessoa altera o teor de documento que deveria remeter para análise administrativa. A i
- Uma comissão local recebe notícia de desvio ético atribuído a integrante de outra comissão
- A administração precisa constituir comissão de ética local segundo o Decreto nº 6.029/2007
- Uma pessoa é investigada por falta ética. A comissão a notificou para manifestação escrita
- A comissão concluiu a instrução e reconheceu falta ética de servidor abrangido pelo Código
- Foi formulada denúncia não identificada contendo fatos específicos e documentos que indica
- Após ciência da decisão final em Processo de Apuração Ética, o investigado pretende pedir 
- Uma ação foi praticada observando toda a forma legal, mas visando a interesse pessoal estr
- Uma apuração ética está em curso e o investigado requer vista dos autos antes mesmo de not
- Concluída uma investigação, a comissão prepara a divulgação de seu resultado. Conforme o a
- A chefia foi informada de possível ilícito disciplinar identificado durante apuração de in
- A unidade deseja celebrar Acordo de Conduta Pessoal e Profissional (ACPP) em Procedimento 
- A comissão identificou omissão no código aplicável e dúvida sobre a legalidade da solução 
- Uma pessoa sob investigação prestou serviços em período de licença e afirma estar automati
- Uma notícia informa que houve fato passível de censura e eventual falta disciplinar. A com
- Um colaborador eventual, mesmo sem retribuição financeira, pode enquadrar-se no conceito d
- Basta que um ato observe as formalidades legais para que esteja necessariamente de acordo 
- A orientação de divulgar atos administrativos como regra deve ser conciliada com as hipóte
- O servidor pode adiar sem motivo a prestação de contas quando isso facilite a execução de 
- Respeitar a hierarquia não impede que o servidor represente contra comprometimento indevid
- A vedação de receber vantagem pelo exercício da missão não abrange a mera sugestão de bene
- A proteção ética da reputação alheia impede que a solidariedade entre colegas seja usada c
- Para incidir a proibição de uso indevido de informação privilegiada, é indispensável que t
- Pelo Decreto nº 6.029/2007, a Comissão de Ética Pública é composta por sete integrantes de
- As comissões locais e a Comissão de Ética Pública são integradas pelo mesmo número de titu
- Entre as atribuições das comissões locais estão orientar dirigentes e servidores, apurar c
- Um dos deveres da comissão local é recusar julgamento de caso omisso até a aprovação forma
- Ao receber documentos probatórios novos após a manifestação inicial do investigado, a comi
- Durante o procedimento ético, a chancela de reservado impede que o próprio investigado con
- O Decreto nº 6.029/2007 prevê a divulgação de ementas das decisões com omissão do nome dos
- A constatação de indícios de ilícito penal durante a apuração ética obriga a comissão a ap
- Nos termos da Resolução CEP nº 10/2008, denúncia sem identificação, acompanhada de indício
- O Acordo de Conduta Pessoal e Profissional pode substituir qualquer apuração referente às 
- A sanção ética de censura pode coexistir com o encaminhamento de fatos possivelmente disci
- Como o inciso XIX do Anexo do Decreto nº 1.171/1994 previa recurso ao ministro, ele contin
```

## Pedido 41: Normas Regulamentadoras (NRs) de segurança e saúde no trabalho (`seguranca-trabalho.normas-regulamentadoras`, 551 tópicos de edital)

```
Matéria: seguranca-trabalho.normas-regulamentadoras (Normas Regulamentadoras (NRs) de segurança e saúde no trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "Sistema de NRs, NR-1, NR-4, NR-5 e NR-6": 20
- "NR-7, NR-9, NR-10, NR-11 e NR-12: saúde, exposições e equipamentos": 20
- "NR-15, NR-16, NR-17 e NR-18: adicionais, ergonomia e construção": 20
- "NR-23, NR-24, NR-26 e NR-32: instalações, comunicação e saúde": 20
- "NR-33 e NR-35: atividades críticas e integração entre normas": 20

Já existem (não repita):
- As NRs complementam a disciplina de segurança e saúde da CLT e não são um conjunto de reco
- O PGR da NR-1 deve possuir, no mínimo, inventário de riscos ocupacionais e plano de ação.
- A NR-4 trata da constituição e do dimensionamento do SESMT.
- A CIPA constitui um serviço composto exclusivamente por médicos e engenheiros do trabalho.
- A NR-10 estabelece requisitos relativos à segurança em instalações e serviços com eletrici
- A NR-24 está relacionada às condições sanitárias e de conforto nos locais de trabalho.
- Desde maio de 2026, o GRO da NR-1 passou a contemplar expressamente fatores de riscos psic
- O Certificado de Aprovação permite utilizar um único modelo de EPI em qualquer atividade, 
- Na disciplina atual, o PGR é previsto pela NR-1, enquanto a NR-9 se ocupa da avaliação e d
- A mera colocação de uma máquina em estado de parada por botão operacional comprova, por si
- A NR-11 pode exigir atenção à circulação e às saídas ao avaliar o armazenamento de materia
- A NR-17 limita a ergonomia à altura das cadeiras e mesas, excluindo a organização do traba
- Em serviços de saúde, a NR-32 abrange apenas exposição a agentes biológicos, sem tratar de
- A NR-18, voltada à indústria da construção, afasta automaticamente a aplicação da NR-35 em
- Risco psicossocial relacionado ao trabalho, na NR-1, equivale necessariamente a diagnóstic
- O PCMSO da NR-7 deve dialogar com os riscos ocupacionais identificados, em vez de adotar n
- A existência de botão de parada de emergência autoriza a supressão de proteções fixas ou m
- A caracterização de insalubridade pela NR-15 e o enquadramento de periculosidade pela NR-1
- A PET de espaço confinado pode ser substituída indistintamente por um aviso verbal de supe
- A definição geral de trabalho em altura da NR-35 pressupõe simultaneamente diferença de ní
- Uma equipe precisa localizar a norma que regula o programa médico ocupacional. Qual é a re
- Em qual alternativa a sigla está associada à norma pertinente?
- A respeito da proteção individual, identifique o exemplo que caracteriza EPI.
- Uma empresa quer consultar a NR sobre instalações sanitárias e locais para refeições. Qual
- O objeto central da NR-26 é:
- Qual norma concentra regras setoriais para trabalhadores dos serviços de saúde?
- Uma escavação e a organização de um canteiro de obra remetem diretamente a qual NR?
- Em qual norma procurar disciplina específica de transporte, movimentação e armazenagem de 
- A caracterização normativa de atividades e operações insalubres está associada a:
- No PGR, qual par de elementos documentais deve ser encontrado, no mínimo?
- Qual conduta é a mais adequada à NR-10 antes de uma intervenção em circuito que se afirma 
- Em uma máquina com acesso a partes cortantes, a empresa pretende manter apenas um botão de
- Em uma questão que descreve exposição ocupacional a agentes químicos, com necessidade de a
- O enunciado menciona AEP e AET na análise de um posto de trabalho. Qual norma está sendo a
- Um hospital observa acidentes com materiais perfurocortantes. A norma setorial prioritária
- Durante vistoria, caixas obstruem rota de saída de uma área de armazenagem. Qual análise é
- Numa prova, a alternativa descreve um adicional relacionado a risco acentuado em atividade
- Sobre a mudança da NR-1 efetiva em 26 de maio de 2026, qual resposta é a mais adequada?
- Uma questão contrapõe CIPA e SESMT. Qual proposição traduz adequadamente a diferença?
- Uma equipe fará limpeza dentro de reservatório que preenche os critérios de espaço confina
- Uma tarefa ocorre em plataforma a 2,3 m do piso com possibilidade de queda. Qual conclusão
- Uma organização chama toda tarefa com produtos químicos de atividade perigosa e concede ad
- Numa unidade de saúde, um fornecedor propõe resolver toda exposição química apenas com pla
- Um trabalhador realizará manutenção com risco de queda usando cinturão, mas sem ponto de a
- O edital afirma que AET deve obrigatoriamente ser elaborada para qualquer atividade, sem a
```

## Pedido 42: Execução orçamentária: descentralização de créditos e suprimento de fundos (`afo.descentralizacao-creditos-suprimento-fundos`, 546 tópicos de edital)

```
Matéria: afo.descentralizacao-creditos-suprimento-fundos (Execução orçamentária: descentralização de créditos e suprimento de fundos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Execução orçamentária, estágios da despesa e acompanhamento": 20
- "2. Programação, contingenciamento e sistemas de informação": 27
- "3. Descentralização de créditos: provisão, destaque e TED": 17
- "4. Suprimento de fundos, CPGF, limites e prestação de contas": 16
- "5. TED, retenções tributárias, controles e casos integrados": 20

Já existem (não repita):
- Uma unidade autorizou despesa de manutenção e emitiu documento que vincula parte da dotaçã
- Um fornecedor entregou equipamentos, e a unidade confere contrato, termo de recebimento e 
- No acompanhamento de um contrato, registraram-se R$ 80 mil empenhados, R$ 50 mil liquidado
- Uma unidade central autoriza uma unidade regional pertencente ao mesmo órgão a executar pa
- Uma equipe precisa identificar os registros federais de empenhos, liquidações e pagamentos
- Ao final de um bimestre, verificou-se que a arrecadação projetada comprometerá as metas fi
- Uma unidade precisa custear despesa excepcional de pequeno vulto que não admite processame
- Um servidor responde regularmente por dois suprimentos de fundos ainda não encerrados. Sur
- Em suprimento de fundos da administração federal, o servidor pretende efetuar saque em esp
- Após a publicação do orçamento anual federal, uma equipe prepara o ato que compatibilizará
- Uma unidade recebeu de órgão federal distinto a competência para empenhar crédito de deter
- Em 2026, duas unidades federais planejam descentralizar R$ 200.000 para atividade de inter
- Uma unidade descentralizada apresenta proposta de alteração de TED para aumentar o prazo, 
- A execução de um TED terminou antes do vencimento de sua vigência. A unidade descentraliza
- Um ato de suprimento federal envolve compras em geral, sem situação excepcional de autoriz
- Para movimentar um suprimento de fundos, uma unidade federal pretende abrir uma conta banc
- Uma unidade paga serviço prestado por pessoa jurídica e apura tributo a reter. Para concil
- Uma unidade tinha R$ 900 mil autorizados, mas sofre limitação de empenho de R$ 180 mil apó
- Perto do encerramento do exercício, uma unidade descentralizada mantém crédito de TED não 
- Duas descentralizações federais de R$ 150 mil, destinadas ao mesmo objeto de interesse rec
- Um TED de obra chegou ao limite ordinário de 60 meses. A execução atrasou por determinação
- Ao liquidar serviço de R$ 40.000, uma unidade calcula retenção tributária hipotética de R$
- O único servidor disponível para realizar compra eventual é responsável pelo estoque do ma
- Em 31 de dezembro, um suprido ainda dispõe de saldo sob sua responsabilidade e já aplicou 
- Uma unidade descentralizada não apresentou relatório de cumprimento do objeto de TED em 12
- Após receber e aceitar formalmente a execução parcial de um contrato, a unidade pode liqui
- Em determinada unidade, a emissão de empenho equivale ao desembolso do dinheiro e demonstr
- Na execução federal, o SIAFI auxilia o registro e controle orçamentário, financeiro e patr
- Se duas unidades gestoras pertencem ao mesmo órgão federal, a movimentação de crédito entr
- Uma despesa eventual de pronto pagamento pode justificar suprimento de fundos quando inviá
- O servidor que recebeu suprimento de fundos fica dispensado de comprovar as compras quando
- Ao identificar previsão de receita insuficiente ao término de um bimestre, um Poder deve a
- Uma unidade que recebeu crédito de outro órgão por destaque poderá usar esse mesmo crédito
- Na hipótese de dispensa de celebração de TED, as notas de movimentação de crédito e de pro
- Como o limite de R$ 243.535,08 para dispensa do TED entrou em vigor em 2026, esse valor nã
- O regime de suprimento de fundos admite uma segunda concessão ao servidor que responde por
- Uma unidade pode dividir aquisições anuais de um mesmo item em várias compras pequenas com
- Na liquidação de contratação sujeita a retenção, a unidade deve examinar os requisitos tri
- Um TED concluiu seu objeto, e o relatório final foi encaminhado apenas 175 dias depois. Co
- Assinado o TED, os partícipes devem providenciar publicação do extrato no sítio eletrônico
- Com a alteração de 2026, a obrigação de designar gestores titulares e suplentes do TED pas
- Se a unidade descentralizadora identifica indícios de irregularidade na execução do TED, d
- A utilização de CPGF torna facultativa a prestação de contas do suprido quando todas as co
- Um estado pode instituir sistemas próprios de planejamento e execução financeira; a Lei nº
- Na hipótese de denúncia ou rescisão de TED, o prazo de 30 dias para devolver créditos e re
```

## Pedido 43: Garantias, administração tributária e certidões (`direito-tributario.garantias-administracao-tributaria`, 541 tópicos de edital)

```
Matéria: direito-tributario.garantias-administracao-tributaria (Garantias, administração tributária e certidões)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Garantias, fraude à execução e preferências do crédito tributário": 27
- "2. Fiscalização tributária, formalização, cooperação e limites": 15
- "3. Sigilo fiscal, informações bancárias, cadastros e trocas de dados": 15
- "4. Dívida ativa, inscrição, controle de legalidade e cobrança em linhas gerais": 26
- "5. Certidões fiscais, regularidade, repetição do indébito e atualização de procedimentos": 17

Já existem (não repita):
- Uma sociedade tem débito tributário e o setor jurídico indaga quais bens respondem pelo cr
- Após a inscrição regular de crédito tributário em dívida ativa, o devedor aliena imóvel se
- Em discussão sobre a preferência do crédito tributário, uma credora alega que seu crédito 
- Uma entidade imune ao tributo fiscalizado recusa qualquer apresentação de documentos alega
- A Fazenda planeja uma inspeção em estabelecimento em outubro de 2026. Além da documentação
- Para cobrar imposto em atraso, a autoridade determina fechar o estabelecimento até o pagam
- Uma autoridade fiscal pretende analisar extratos de instituição financeira em procedimento
- O contribuinte possui imposto vencido, definitivamente constituído e regularmente inscrito
- Um devedor tributário citado não paga, não apresenta bens à penhora e nenhum bem penhoráve
- Um procurador sustenta que, em conflito entre créditos tributários de diferentes entes, a 
- A fiscalização detecta divergência documental em empresa e oferece canal para regularizaçã
- Uma pessoa solicita divulgar, além dos dados de inscrição em dívida ativa, todo o arquivo 
- A autoridade tributária intima terceiro a prestar informações sobre negócio de contribuint
- Duas Fazendas pretendem compartilhar, mediante convênio, estrutura para fiscalização, lanç
- Antes da emissão da CDA, o contribuinte questiona a certeza, a liquidez e a exigibilidade 
- Ao preparar inscrição tributária, a repartição deixa de mencionar o fundamento legal espec
- Um contribuinte tem parcelamento tributário regularmente deferido, com exigibilidade suspe
- Na falência, pretende-se afirmar que multa tributária e principal do tributo possuem idênt
- Em diligência presencial, a equipe leva acompanhamento policial por simples rotina, sem in
- Após trânsito em julgado de decisão definitiva com efeito vinculante na esfera judicial so
- Uma Fazenda estadual devidamente regulamentada instaura procedimento fiscal e demonstra in
- Um órgão precisa encaminhar créditos já exigíveis e definitivamente constituídos para insc
- Ao corrigir CDA, pretende-se aproveitar a substituição para modificar o próprio devedor in
- Uma multa administrativa de natureza não tributária foi regularmente inscrita em dívida at
- Uma pessoa precisa praticar ato indispensável para evitar caducidade de direito, mas falta
- Uma dívida de imposto pode ser cobrada sobre bens e rendas do sujeito passivo, mas o art. 
- O art. 185 do CTN presume fraude em qualquer venda realizada depois do mero aviso de fisca
- Uma associação beneficiada por imunidade tributária pode ser fiscalizada quanto à legislaç
- Reter mercadorias de estabelecimento até o pagamento de tributo vencido constitui, segundo
- O art. 6º da LC nº 105/2001 admite exame de registros bancários quando houver processo adm
- Se os dados econômicos de contribuinte foram legitimamente obtidos por fiscalização, o art
- A multa administrativa de natureza não tributária pode integrar dívida ativa da Fazenda Pú
- Mesmo antes de o devedor ser citado, o art. 185-A do CTN autoriza a Fazenda a tornar admin
- Um aviso de autorregularização anterior à lavratura de auto de infração pode concretizar o
- Depois da LC nº 236/2026, o procedimento fiscal presencial pode começar sem documento prév
- Quando terceiro é intimado a prestar informações com base no art. 197 do CTN, a obrigação 
- A existência de inscrição em dívida ativa autoriza a Fazenda a divulgar também todas as in
- O art. 201, § 2º, acrescido pela LC nº 236/2026, trata o controle de certeza, liquidez e e
- A ordem União, Estados, Distrito Federal e Municípios para pagamento preferencial de crédi
- O art. 194-A, acrescentado pela LC nº 236/2026, vincula a administração a decisão definiti
- Nas diligências fiscais em estabelecimento, o art. 196, § 4º, do CTN admite acompanhamento
- O entendimento do STF no Tema 225 admite acesso do fisco a informações financeiras sem ord
- A Súmula 392 do STJ permite à Fazenda substituir CDA para trocar o sujeito passivo da exec
- Uma pessoa com tributo constituído, mas ainda não vencido, pode obter certidão positiva co
- O art. 165-A, introduzido em 2026, determina que o indébito tributário seja atualizado por
```

## Pedido 44: Benefícios do RGPS (`direito-previdenciario.beneficios-rgps`, 537 tópicos de edital)

```
Matéria: direito-previdenciario.beneficios-rgps (Benefícios do RGPS)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Beneficiários, filiação, inscrição, carência e qualidade de segurado": 23
- "2. Incapacidade, auxílio-acidente e prestações familiares": 14
- "3. Aposentadorias, segurado especial, tempo e contagem recíproca": 19
- "4. Pensão por morte, acumulações, prestações e reabilitação": 18
- "5. Melhor benefício, renda inicial, reajuste, decadência e revisão": 26

Já existem (não repita):
- Uma pessoa começa a trabalhar como empregada, mas descobre que o registro cadastral foi fe
- Um segurado morre deixando companheira, mãe e irmão adulto sem invalidez. A dependência e 
- Uma segurada contribuinte individual, filiada e com qualidade de segurada comprovada, soli
- Uma pessoa deixou atividade abrangida pelo RGPS e não voltou a contribuir. Não se aplicam 
- Uma trabalhadora empregada está temporariamente incapacitada para a atividade habitual por
- Após consolidação de lesão acidental, um trabalhador empregado retoma a atividade, porém a
- Uma pessoa de baixa renda deixa dependentes e é recolhida à prisão em regime fechado. Sua 
- Um homem iniciou filiação ao RGPS em 2022 e busca aposentadoria programada pelas regras ge
- Uma mulher que exerceu atividade rural em economia familiar comprovou as condições específ
- Uma segurada com qualidade preservada tem apenas quatro contribuições válidas e sofre acid
- Um empregado deixou de trabalhar após 126 contribuições mensais sem perda da qualidade; nã
- Uma pessoa teve perda da qualidade de segurado e retornou ao RGPS. Requer benefício por in
- Uma mãe e seu recém-nascido permaneceram internados por complicações médicas relacionadas 
- Um segurado especial pede aposentadoria rural por idade. Exerceu atividade em regime de ec
- Uma pessoa trabalhou por dez anos vinculada ao RGPS e ingressou depois em regime próprio, 
- Um segurado falece deixando cônjuge habilitado. Não havia dezoito contribuições nem casame
- Uma mulher concluiu todos os requisitos de aposentadoria em outubro de 2019, mas só pediu 
- Uma pensão por morte concedida após a EC 103/2019 tem dois dependentes habilitados, nenhum
- Um homem já filiado ao RGPS antes de novembro de 2019 requer aposentadoria programada pela
- Uma pessoa recebeu benefício previdenciário mensal pela primeira vez há mais de dez anos e
- Um segurado pleiteia pela primeira vez aposentadoria cujos requisitos já completara há doz
- Um segurado recebe auxílio-acidente indenizatório e pretende usá-lo, sozinho e sem ativida
- Um segurado trabalhou concomitantemente sob vínculos distintos e pretende utilizar a mesma
- Uma aposentada ingressa com pedido para corrigir parcelas de benefício vencidas há muitos 
- Uma pessoa conferiu a média de salários contributivos e quis saber o valor efetivamente pa
- Uma pessoa segurada falece deixando cônjuge e pais. Confirmada a dependência do cônjuge, a
- Uma segurada facultativa, com qualidade preservada, precisa recolher pelo menos dez contri
- Uma pessoa contribuiu exclusivamente como facultativa e interrompeu os recolhimentos. Em r
- No caso de prisão em regime fechado de segurado de baixa renda, o auxílio-reclusão é pago 
- Após acidente de qualquer natureza, um empregado apresenta sequela consolidada que reduz s
- A pensão por morte exige carência de dezoito contribuições mensais, de modo que a ausência
- Um segurado perdeu qualidade e voltou a contribuir. Para benefício por incapacidade sujeit
- Uma pessoa recebeu auxílio-acidente por sequela e interrompeu toda contribuição. Somente o
- Um aposentado recebe abono anual na forma do art. 40 da Lei 8.213/1991. Tal prestação inte
- Um segurado aposentado pelo RGPS pretende obter e acumular segunda aposentadoria do mesmo 
- Um empregado de baixa renda tem filho de doze anos e apresenta documentação exigida. O sal
- Uma trabalhadora rural em regime de economia familiar pode obter aposentadoria rural com b
- O segurado submetido a reabilitação profissional pode receber meios de readaptação para ou
- Para uma pensão decorrente de óbito após a reforma, o requerimento do filho menor de dezes
- Em contagem recíproca, o período já utilizado para concessão de aposentadoria em um regime
- Um homem filiado ao RGPS pela primeira vez em 2021 pode aposentar-se pela regra comum do a
- Quem completou os requisitos de aposentadoria antes da EC 103/2019 e requereu depois pode 
- A Lei 15.222/2025 prorrogou o salário-maternidade para toda internação de sete dias do rec
- A preservação do valor real das prestações do RGPS e o reajuste periódico previsto em lei 
- Quando uma pessoa solicita a primeira concessão de benefício cujo direito já estava adquir
```

## Pedido 45: Lei 8.112/1990: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição (`legislacao.lei-8112-1990-provimento`, 533 tópicos de edital)

```
Matéria: legislacao.lei-8112-1990-provimento (Lei 8.112/1990: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Disposições preliminares, investidura e concurso público": 30
- "2. Nomeação, posse, exercício e deslocamento de sede": 17
- "3. Estágio probatório, avaliação e estabilidade": 18
- "4. Readaptação, reversão, reintegração, recondução e aproveitamento": 16
- "5. Vacância, remoção, redistribuição e substituição": 19

Já existem (não repita):
- Um edital anuncia seleção sob a Lei nº 8.112/1990. Qual conjunto descreve corretamente o â
- Uma pessoa foi aprovada, chamada para apresentar documentos e nomeada, mas ainda não tomou
- Na preparação de um edital, pretende-se acrescentar requisito de habilitação específica em
- O edital de um concurso estabeleceu validade inicial de dezoito meses. Após esse prazo, co
- Uma norma pretende permitir, mediante simples procedimento interno, o ingresso definitivo 
- Uma pessoa se oferece para prestar serviço gratuito de rotina na administração federal. A 
- Como a Lei nº 8.112/1990 admite provas e títulos, a seleção para cargo efetivo pode adotar
- O art. 5º, § 3º, admite, conforme as normas legais pertinentes, que instituições federais 
- Como o art. 5º, § 2º, prevê reserva de até vinte por cento das vagas, cada concurso regido
- Um ato de nomeação foi publicado e a pessoa aprovada ainda precisa tomar posse. Qual prazo
- Uma pessoa foi empossada regularmente, mas não iniciou as atribuições no prazo geral aplic
- Uma pessoa foi designada para função de confiança enquanto estava em afastamento legal. Se
- Um servidor removido deverá trabalhar em outro município. Não está em licença ou afastamen
- Na publicação do ato de provimento, uma servidora já se encontrava em licença expressament
- A posse pode ser formalizada por procuração específica, desde que observadas as demais exi
- A promoção interrompe o tempo de exercício do servidor, reiniciando-se a contagem apenas q
- Se a mudança do exercício para outro município decorrer de redistribuição e o servidor est
- Se o titular de função de confiança estiver afastado, a pessoa designada para assumir essa
- Uma avaliação de servidor em estágio probatório segue os cinco fatores do art. 20 da Lei n
- Uma pessoa nomeada após a publicação do Decreto nº 12.374/2025 inicia seu primeiro exercíc
- Em procedimento integralmente regido pelo Decreto nº 12.374/2025, a avaliação de desempenh
- Em uma equipe com pelo menos três pares habilitados, as notas do estágio probatório serão 
- Uma pessoa estável tomou posse em outro cargo efetivo, após aprovação em concurso. Ao fina
- O início da contagem do estágio probatório de quem ingressa em cargo efetivo segundo o Dec
- Conforme o Decreto nº 12.374/2025, basta obter média de oitenta pontos nos ciclos avaliati
- Pelo Decreto nº 12.374/2025, o tempo de serviço em cargo público anterior, mesmo com a mes
- As regras procedimentais do Decreto nº 12.374/2025 aplicam-se obrigatoriamente, de forma i
- Uma inspeção médica identifica limitação funcional permanente, mas reconhece capacidade pa
- Uma decisão administrativa invalidou demissão anteriormente imposta a servidor estável. Qu
- Uma pessoa aposentada voluntariamente pede retorno, havendo interesse administrativo. Qual
- Na reorganização administrativa, um servidor estável encontra-se em disponibilidade e surg
- A demissão de um servidor estável foi invalidada e o cargo, ainda existente, estava provid
- A reversão pressupõe que a pessoa estava aposentada antes de retornar à atividade, ao pass
- A readaptação por limitação física ou mental somente poderá ocorrer se houver cargo vago n
- Se a pessoa estiver em disponibilidade e deixar de entrar em exercício após aproveitamento
- O servidor estável que ingressa em novo cargo e não supera o estágio probatório desse carg
- Uma pessoa tomou posse em outro cargo público que não admite acumulação com o anterior. Qu
- Uma servidora é deslocada de uma unidade para outra dentro do mesmo quadro, sem mudança de
- Um servidor solicita remoção para acompanhar companheira, também servidora pública, desloc
- Há necessidade de transferir um cargo de provimento efetivo atualmente vago para outra ent
- Uma pessoa designada previamente substitui a chefia por trinta e cinco dias consecutivos e
- Promoção e readaptação estão simultaneamente previstas como formas de provimento no art. 8
- Na redistribuição de cargo efetivo, a lei permite a movimentação para entidade integrante 
- O pedido de remoção por motivo de saúde de dependente que vive às expensas do servidor req
- A substituição prevista no art. 38 somente se efetiva quando o titular estiver em licença,
```

## Pedido 46: CPC: normas fundamentais, aplicação e direito intertemporal (`processo-civil.normas-fundamentais-cpc`, 530 tópicos de edital)

```
Matéria: processo-civil.normas-fundamentais-cpc (CPC: normas fundamentais, aplicação e direito intertemporal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Processo civil, Constituição e acesso à justiça": 21
- "2. Boa-fé, cooperação, paridade e eficiência processual": 15
- "3. Contraditório efetivo, não surpresa, publicidade e motivação": 18
- "4. Ordem cronológica e aplicação da norma processual no espaço": 17
- "5. Aplicação temporal, transição do CPC/1973 e casos avançados": 29

Já existem (não repita):
- Após ajuizar ação de cobrança, Marina pergunta se precisará requerer individualmente toda 
- Um consumidor percebe ameaça concreta de cobrança indevida, embora ainda não tenha sofrido
- Em disputa patrimonial disponível, duas pessoas cogitam escolher arbitragem e tentar conci
- Um processo terminou com reconhecimento do crédito, mas ainda é necessária atividade para 
- Uma testemunha e uma parte trocam informações para apresentar ao processo versão conscient
- O magistrado verifica defeito corrigível na petição que, se persistir, levará à extinção s
- Na tramitação de uma ação, o juiz deseja assegurar possibilidades equilibradas de manifest
- Sem ouvir previamente o réu, o juiz concede medida urgente necessária para prevenir dano i
- Ao organizar sentenças de processos comuns aptos a julgamento, a unidade jurisdicional con
- Em litígio com fatos já discutidos, a sentença invoca dispositivo legal diferente dos arti
- Em ação com discussão complexa, o juiz pretende decidir com base em fundamento de ordem pú
- Duas partes plenamente capazes convencionam distribuição de ônus processuais em causa que 
- Juiz e partes estabelecem conjuntamente calendário para atos processuais, com datas defini
- Após inclusão de processo na lista cronológica, uma parte apresenta requerimento simples q
- Um processo civil tramita no Brasil e envolve contrato submetido, no mérito, à lei estrang
- Em processo administrativo regulado por diploma próprio, surge ponto procedimental sem reg
- Em ação iniciada antes de uma reforma processual, um ato foi validamente praticado sob a l
- Uma decisão antecipada é pretendida com base no art. 311, IV, do CPC, sem oitiva da parte 
- Uma sentença é anulada, mas o processo já está apto a novo julgamento e não exige mais dil
- Uma decisão foi publicada em 16/03/2016, mas a parte protocolou o recurso em abril do mesm
- Em processo iniciado em 2014, uma decisão foi publicada em 21/03/2016 e, posteriormente, f
- Uma prova pericial foi requerida em fevereiro de 2016, mas o exame físico só foi realizado
- Uma ação sob procedimento sumário do CPC/1973 foi proposta em 2015 e ainda não tinha sente
- A sentença de um processo iniciado em 2014 resolve questão prejudicial e a parte afirma qu
- O julgador decide contra uma parte citando um precedente, mas não relaciona os fundamentos
- Uma pessoa ajuíza pedido preventivo para impedir lesão iminente a direito; a falta de dano
- Em processo civil, o princípio da duração razoável assegura julgamento rápido mesmo que se
- Durante um processo, um auxiliar que participa de sua tramitação também se submete ao deve
- Ao decidir pedido de tutela provisória de urgência, o juiz precisa ouvir previamente a par
- Em julgamento de apelação, o tribunal pode adotar fundamento determinante jamais submetido
- Em processo pendente durante mudança legislativa, um ato regularmente praticado sob a lei 
- Uma causa foi concluída para julgamento antes de outra, mas envolve embargos de declaração
- Em causa de direitos que admitem autocomposição, partes plenamente capazes podem convencio
- O juiz reconhece urgência especial e altera a sequência de julgamentos sem registrar motiv
- Em demanda cível no Brasil que envolve contrato regido por direito material estrangeiro, a
- Ao julgar pedido com fundamento jurídico relevante não debatido, o magistrado pode dispens
- Uma parte invoca cooperação para exigir que seu adversário reconheça a procedência da dema
- Um procedimento administrativo contém regra própria para vários atos, mas omite disciplina
- Uma ação foi proposta em 2015 e ainda estava pendente na entrada em vigor do CPC/2015. A r
- Uma prova documental foi requerida em fevereiro de 2016 e produzida em abril de 2016. Como
- Para aferir admissibilidade recursal na transição entre códigos, uma decisão publicada em 
- Uma execução contra devedor insolvente ajuizada depois de 18/03/2016 deve seguir obrigator
- Num processo iniciado em 2014, a aplicação do art. 503, § 1º, do CPC/2015 a uma questão pr
- Num processo, juiz e partes fixam calendário para audiência e providência probatória. Após
- Uma decisão deixa de enfrentar argumento capaz de afastar sua conclusão, mas apresenta eme
```

## Pedido 47: Controle interno, COSO, gestão de riscos e governança (`controle-externo.controle-interno-coso`, 527 tópicos de edital)

```
Matéria: controle-externo.controle-interno-coso (Controle interno, COSO, gestão de riscos e governança)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos constitucionais, controles administrativos e controladoria": 21
- "2. COSO Controle Interno: cubo, cinco componentes e 17 princípios": 15
- "3. COSO ERM, ISO 31000 e tratamento de riscos em matriz": 28
- "4. Instrução Normativa MP/CGU nº 1/2016 e controles da gestão pública": 19
- "5. Governança, compliance, três linhas e auditoria independente": 17

Já existem (não repita):
- Uma unidade de controle acompanha o cumprimento das metas do plano plurianual, a execução 
- Uma mesma pessoa cadastra fornecedor, autoriza contratação, confere recebimento e libera p
- Um sistema impede que seja liberado pagamento sem confirmação de recebimento do objeto con
- A chefia afirma que a adoção de controles internos de alto custo garante eliminação de qua
- A alta administração define prioridades, orienta decisões e acompanha resultados de políti
- Ao aplicar o COSO Controle Interno 2013 em processos de prestação de serviços, uma equipe 
- Um gestor descreve risco de interrupção de serviço antes de considerar geradores, procedim
- Um órgão aplica orientações da ISO 31000:2018 ao organizar identificação, análise, avaliaç
- Uma instituição publicou código de ética, mas não treinou equipes nem estruturou canal de 
- Uma unidade de auditoria interna avalia controles de contratação, comunica deficiências e 
- Uma unidade mantém pagamentos regulares, mas produz relatórios com valores inconsistentes,
- Uma equipe analisa os 17 princípios do COSO 2013. Para incluir explicitamente fraude na av
- Um procedimento prevê dupla aprovação de pagamentos e apresenta fluxo formal adequado. Amo
- Uma unidade adota matriz ilustrativa em que risco é calculado por probabilidade multiplica
- Um material de treinamento enumera ambiente interno, definição de objetivos, identificação
- Uma organização revisa estratégia e desempenho sob a orientação do COSO ERM 2017. Que estr
- Uma análise cita cinco componentes para controles internos e oito para a gestão de riscos 
- Uma unidade técnica define padrões de risco, orienta gestores, compara mapas e questiona c
- Ao instituir o Comitê de Governança, Riscos e Controles, a direção pretende atribuir-lhe a
- Um órgão percebe que empregados assinam código de conduta, mas não conhecem o canal de den
- Uma equipe constrói mapa de riscos de contratações. A política aceita escore máximo 8; o r
- Responsável pelo controle interno toma conhecimento de ilegalidade relevante e omite comun
- Uma entidade contratou seguro contra dano de equipamento, manteve controles de prevenção e
- Um parecer de outubro de 2026 descreve como atuais as normas profissionais do IIA lançadas
- Uma controladoria comunica relatórios de desempenho, orienta gestores e mantém mecanismo p
- O sistema de controle interno de cada Poder deve apoiar o controle externo no exercício de
- A criação de uma controladoria governamental retira dos gestores a responsabilidade pelos 
- O COSO Controle Interno 2013 organiza os objetivos de controle em operações, informação ou
- Risco residual é, conforme a IN Conjunta MP/CGU nº 1/2016, a exposição existente antes de 
- Comprometimento da alta direção, código de conduta, treinamento e canais de denúncia são m
- Em uma organização com auditoria interna, esta pode tornar-se proprietária formal de todos
- Na ISO 31000:2018, comunicação e consulta, monitoramento e análise crítica integram o proc
- O art. 17 da IN Conjunta MP/CGU nº 1/2016 estabelece que qualquer revisão da política de g
- Um mapa de riscos adota expressamente escore como produto da probabilidade pelo impacto. U
- As atividades de controle constituem, no COSO de 2013, o único componente necessário para 
- No COSO Controle Interno de 2013, o princípio 11 refere-se ao desenvolvimento de controles
- Uma autarquia adotou a ISO 31000:2018 e afirma que basta obter um certificado ISO 31000 pa
- Uma equipe sugere controle que custa mais do que a perda provável mitigável. A análise de 
- Se um órgão mantém controles documentados, o risco residual de cada evento necessariamente
- Pelo art. 23, § 2º, da IN Conjunta MP/CGU nº 1/2016, o Comitê de Governança, Riscos e Cont
- No Modelo das Três Linhas, um auditor interno que autoriza rotineiramente os pagamentos a 
- O IIA atualizou o Modelo das Três Linhas em 2026, ressaltando coordenação e colaboração en
- A existência de programa formal de compliance, com código de conduta e treinamento anual, 
- O Decreto nº 9.203/2017, art. 3º, elenca capacidade de resposta, integridade, confiabilida
- O modelo COSO ERM 2017 conserva como sua estrutura vigente os oito componentes do ERM 2004
```

## Pedido 48: Redes: modelos, endereçamento e protocolos (`informatica.redes-protocolos`, 527 tópicos de edital)

```
Matéria: informatica.redes-protocolos (Redes: modelos, endereçamento e protocolos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Camadas OSI/TCP-IP, Ethernet, VLAN e agregação de enlaces": 26
- "2. IPv4, IPv6, sub-redes e encaminhamento IP": 20
- "3. Roteamento, ARP, ICMP, NAT e noções de IPsec": 18
- "4. TCP, UDP, portas, DNS e DHCP": 21
- "5. Aplicações, gerenciamento, diretórios e diagnóstico integrado": 15

Já existem (não repita):
- Uma aplicação envia conteúdo por uma conexão TCP. Na identificação das unidades de dados d
- Um switch Ethernet recebe um quadro destinado a uma interface na mesma VLAN. Qual campo do
- Duas VLANs compartilham um enlace entre switches que transporta quadros marcados. Qual pad
- Dois equipamentos possuem dois enlaces Ethernet físicos e desejam formar uma conexão lógic
- Uma rede Ethernet usa switches e conexões full duplex dedicadas. Sobre o mecanismo de coli
- Um host possui endereço `192.168.10.70/27`. Qual é o endereço de rede correspondente, calc
- Um endereço IPv4 deve ser reconhecido como privado pela RFC 1918. Entre os apresentados, q
- Um endereço IPv6 é apresentado como `2001:db8:0:0:0:0:0:1`. Qual é uma abreviação sintatic
- Uma tabela contém rotas `10.0.0.0/8` e `10.20.0.0/16`, ambas alcançáveis, além da rota pad
- Uma sub-rede comum utiliza prefixo IPv4 `/26` e endereço `10.20.30.130`. Qual par indica o
- Uma rede usa RIP v2 para anunciar rotas e possui um destino alcançado por 16 saltos na mét
- Em um segmento Ethernet multiacesso com OSPFv2, deseja-se reduzir a quantidade de adjacênc
- Um roteador estabelece uma sessão de BGP-4 com um vizinho de outro sistema autônomo, usand
- Um computador com IPv4 precisa enviar um pacote para endereço da mesma sub-rede, mas conhe
- Dois computadores privados, `192.168.1.10` e `192.168.1.11`, estabelecem sessões simultâne
- Uma comunicação necessita sigilo do conteúdo transportado por IPsec. Foram considerados os
- Um cliente estabelece sessão TCP convencional com servidor web sobre IPv4. Qual sequência 
- Um receptor TCP envia ACK com número `5001` após receber em ordem bytes de sequência até `
- Uma conexão TCP apresenta janela anunciada do receptor de 8 KiB, mas janela de congestiona
- Uma equipe configura DNS e determina que o serviço aceitará consultas UDP, mas bloqueará q
- Um cliente DHCPv4 recém-iniciado ainda não possui endereço IPv4, e o servidor encontra-se 
- Um servidor FTP foi configurado para modo passivo. O cliente estabelece controle pela port
- Um gerente de rede consulta variáveis de agente SNMP e deve receber alarmes assíncronos em
- Uma solução precisa integrar compartilhamento de arquivos entre sistemas e consultar um di
- Uma captura de tráfego mostra uma sessão web moderna utilizando QUIC sobre UDP 443, embora
- Na pilha TCP/IP de quatro camadas, protocolos como DNS e HTTP são classificados na aplicaç
- Uma rede Ethernet comutada full duplex deve utilizar CSMA/CD para detectar colisões em tod
- Um dispositivo IPv6 pode utilizar multicast para descoberta de vizinhos, pois IPv6 não for
- Os endereços `172.20.1.5` e `172.32.1.5` pertencem igualmente ao bloco privado 172.16.0.0/
- Em uma consulta DNS, o registro A associa nome a endereço IPv4 e o registro AAAA associa n
- Ao reunir duas interfaces físicas de 1 Gbit/s em grupo LACP, um único fluxo TCP passa obri
- Um trunk IEEE 802.1Q pode transportar VLANs distintas, enquanto LACP forma grupos de enlac
- Uma ligação IPv4 ponto a ponto configurada como `/31` precisa obrigatoriamente descartar o
- Quando uma máquina IPv4 envia pacote para rede remota usando gateway, o ARP normalmente re
- RIP v1 fornece a máscara de sub-rede em cada anúncio, permitindo CIDR e VLSM com as mesmas
- Em OSPFv2, roteadores de uma área divulgam LSAs e calculam rotas com métrica de custo. Em 
- Como UDP não estabelece conexão, seus datagramas são entregues obrigatoriamente em ordem e
- Na configuração inicial usual de cliente DHCPv4, Discover e Request partem do cliente que 
- DNS pode utilizar TCP 53 apenas para transferir zonas inteiras; consultas ordinárias estão
- Dois anúncios BGP de mesmo prefixo são comparados por política. Atributos como LOCAL_PREF 
- IPsec usa AH para confidencialidade de carga útil e ESP apenas para autenticar endereços E
- O campo de janela anunciado no TCP informa capacidade de recepção, enquanto a janela de co
- Uma trap SNMP é enviada espontaneamente pelo gerente ao agente na porta UDP 161; o agente 
- Uma execução de traceroute pode explorar a diminuição do TTL em IPv4 ou Hop Limit em IPv6 
- No IPsec em modo túnel, ESP protege exclusivamente a carga TCP sem encapsular pacote IP in
- O NTP sincroniza os relógios dos equipamentos pela rede e é importante para a coerência de
- No Syslog, as mensagens recebem um nível de severidade em que o valor 0 corresponde a:
```

## Pedido 49: STF, CNJ e STJ (`direito-constitucional.stf-cnj-stj`, 526 tópicos de edital)

```
Matéria: direito-constitucional.stf-cnj-stj (STF, CNJ e STJ)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Posição constitucional, estrutura e composição do STF": 13
- "2. Competências do STF e repercussão geral": 14
- "3. Súmula vinculante e reclamação constitucional": 16
- "4. Conselho Nacional de Justiça: composição e atribuições": 40
- "5. Superior Tribunal de Justiça: organização e competências": 17

Já existem (não repita):
- Uma alteração legislativa pretende definir a composição do Supremo Tribunal Federal como d
- Uma comissão avalia a exigência constitucional de idade para a nomeação de ministro do Sup
- Durante curso de formação, uma pessoa afirma que o Conselho Nacional de Justiça é composto
- Uma reclamação pretende que determinado órgão refaça a interpretação jurídica da sentença 
- Após indicação de pessoa com notável saber jurídico e reputação ilibada para o Supremo Tri
- Uma apresentação sobre a composição do STJ deve explicar como o art. 104 distribui as orig
- Em ausência do Presidente do STF, a presidência do Conselho Nacional de Justiça deverá ser
- Foi proposta ação declaratória de constitucionalidade referente a um ato normativo. Para q
- Uma parte pretende homologar no Brasil decisão judicial estrangeira. Após a EC nº 45/2004,
- Em recurso extraordinário, o recorrente sustenta questão constitucional com repercussão so
- Após reiteradas decisões sobre matéria constitucional, o STF cogita editar enunciado de ob
- Processo disciplinar contra integrante do Judiciário foi julgado definitivamente há oito m
- Durante reunião do CNJ, surge dúvida sobre qual integrante exerce, por previsão constituci
- Foi publicada súmula vinculante do STF sobre interpretação constitucional de norma determi
- Tribunal estadual julga válida determinada lei do próprio estado, embora contestada diante
- A composição constitucional do CNJ prevê atuação do Procurador-Geral da República e do Pre
- O plenário aprovou enunciado de súmula vinculante e a secretaria organiza a publicação pre
- Ao aprovar súmula vinculante, o STF conclui que a adoção imediata de seus efeitos ameaça a
- Tribunal superior decidiu em instância única mandado de segurança e denegou a ordem. Uma p
- Um tribunal regional federal denega habeas corpus em última instância, e a defesa avalia r
- Em causa decidida pelo tribunal de justiça, o acórdão atribuiu à lei federal interpretação
- Uma causa de valor equivalente a 600 salários mínimos chega ao STJ por recurso especial, c
- Acórdão foi publicado em 15 de setembro de 2026, e contra ele será interposto recurso espe
- Uma parte afirma que, por decisão isolada do relator, o STJ poderia declarar irrelevante a
- Após a reforma previdenciária de 2019, um relatório interno do CNJ lista poderes sancionat
- Uma síntese institucional registra que o STF possui onze ministros e exerce, principalment
- Ao explicar a estrutura do Judiciário, um expositor afirma que o Conselho Nacional de Just
- Ao preparar um quadro comparativo, uma pessoa indica que a Constituição prevê para o STJ u
- Uma proposta de nomeação ao STF considera suficiente a aprovação da indicação presidencial
- Um ato administrativo estadual contraria enunciado de súmula vinculante aplicável, regular
- Uma parte pode solicitar ao CNJ a reforma do mérito de sentença desfavorável, pois o Conse
- Acórdão em última instância contraria diretamente dispositivo da Constituição. Se preenchi
- Uma ação declaratória de constitucionalidade de lei estadual foi protocolada diretamente n
- Na composição dos quinze membros do CNJ, dois assentos pertencem a advogados indicados pel
- O Procurador-Geral da República e o Presidente do Conselho Federal da OAB, por oficiarem p
- O mandado de segurança contra ato de ministro de Estado, quando enquadrado nas regras cons
- Tribunal estadual validou ato de governo local contestado em face de lei federal. Por se t
- Processo disciplinar de magistrado julgado há dez meses pode, em tese, ser revisto pelo CN
- Quando se apresenta ao STF proposta de edição de súmula vinculante, todos os processos que
- A Lei nº 15.484/2026 acrescentou ao CPC o art. 1.035-A, exigindo no recurso especial tópic
- Ainda em outubro de 2026, a relevância da questão federal nos recursos especiais não pode 
- Em recurso especial relativo a ação penal, a Constituição prevê presunção de relevância da
- Segundo a redação atual do art. 103-B, § 4º, III, o CNJ pode impor diretamente a aposentad
- Em pedido de extradição formulado por Estado estrangeiro, a Constituição atribui ao Suprem
- Recurso especial fundado em divergência na interpretação de lei federal pode ser interpost
```

## Pedido 50: Estruturas de dados e algoritmos (`informatica.estruturas-dados-algoritmos`, 525 tópicos de edital)

```
Matéria: informatica.estruturas-dados-algoritmos (Estruturas de dados e algoritmos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Algoritmos, Portugol, tipos, controle e depuração": 30
- "2. Tipos abstratos, recursão e complexidade assintótica": 18
- "3. Vetores, listas, pilhas, filas e hashing introdutório": 18
- "4. Árvores, percursos e algoritmos de busca": 19
- "5. Ordenação: execução, estabilidade e custo": 15

Já existem (não repita):
- No pseudocódigo, `x <- 4; x <- x + 3; escreval(x)`, qual valor será exibido?
- Em um cadastro, cada pessoa possui idade inteira, nome em caracteres e condição ativa lógi
- Considere `i <- 5; enquanto i < 5 faca escreval(i); i <- i+1; fimenquanto`. Quantas vezes 
- Uma função `triplo(n)` recebe n por valor e retorna `3*n`. Se a=5 e b recebe `triplo(a)`, 
- Em uma pilha vazia, inserem-se A, depois B e depois C. Que item é removido primeiro em uma
- Uma fila vazia recebe os clientes A, B e C, nessa ordem. Quem será atendido pela próxima o
- Para localizar chave em vetor já ordenado, um algoritmo inspeciona elemento central e elim
- Numa árvore de busca com chaves distintas, qual percurso visita seus elementos em ordem cr
- O trecho `para i de 1 ate 4 faca soma <- soma + i` parte de soma=0. Qual resultado aparece
- Uma função recursiva `f(n)` retorna 1 se n=0; caso contrário retorna n*f(n-1). Quanto vale
- Um algoritmo executa dois laços completos de n iterações, com trabalho constante no corpo 
- Um vetor dinâmico dobra sua capacidade quando lotado e insere novos itens no fim. Qual aná
- Uma lista simplesmente ligada fornece diretamente uma referência ao nó P. Deseja-se inseri
- Uma fila circular de capacidade 5 usa índices de 0 a 4 e atualiza posição pelo resto de `(
- Uma tabela utiliza `h(chave)=chave mod 7`. As chaves 10 e 17 devem ser inseridas. Que situ
- Uma árvore tem raiz 8, filhos 3 e 12; o nó 3 tem filhos 1 e 6, e 12 tem filho direito 14. 
- Uma BST simples recebeu chaves 1, 2, 3, 4 e 5 nessa sequência, sem rotações. Qual é o cust
- Em árvore AVL, um nó apresenta altura 3 à esquerda e altura 1 à direita, usando a mesma co
- Um heap máximo está armazenado num vetor a partir do índice zero. Em que índices ficam os 
- Uma busca binária percorre [2,4,7,9,12,15,19] com índices 0 a 6, usando meio inferior. Qua
- O bubble sort crescente compara vizinhos em [5,1,4,2] e troca sempre que o item esquerdo é
- Três registros aparecem como [2A,2B,1], onde 2A e 2B têm chaves iguais. A primeira troca d
- Uma coleção de registros deve ser ordenada preservando a sequência original de chaves igua
- Um quicksort sempre escolhe o primeiro item como pivô e recebe dados crescentes, usando pa
- É necessário ordenar um vetor grande com garantia O(n log n) no pior caso e memória auxili
- Em um teste de mesa, a instrução `x <- x + 1` significa avaliar o valor atual de x, somar 
- Num pseudocódigo do tipo `enquanto condicao faca`, o corpo é obrigatoriamente executado um
- Um vetor permite acesso à posição válida conhecida em tempo constante na representação ind
- Uma lista simplesmente encadeada permite, em geral, acessar o elemento de índice k em Θ(1)
- Uma estrutura que insere no fim e remove na frente atende à disciplina FIFO, ao contrário 
- Toda árvore binária com dois filhos na raiz é uma árvore binária de busca, já que a presen
- Um algoritmo de busca sequencial pode localizar uma chave em vetor desordenado, examinando
- Uma função recursiva que utiliza caso-base correto tem espaço auxiliar Θ(1) em qualquer ex
- Se um algoritmo é Θ(n), a afirmação de que também possui limite superior O(n²) é verdadeir
- Uma lista circular é uma estrutura obrigatoriamente ordenada, pois a ligação do último nó 
- Um deque permite inserir e retirar nas duas extremidades; uma fila tradicional pode ser im
- Uma função hash adequada garante que duas chaves diferentes nunca recebam o mesmo índice, 
- Em heap máximo, o maior elemento está na raiz e pode ser consultado sem percorrer toda a e
- Uma busca binária convencional pode ser aplicada com garantia de correção diretamente a qu
- Na definição clássica de AVL, a diferença absoluta entre as alturas das subárvores esquerd
- No selection sort padrão por trocas, a ordenação é estável porque a primeira ocorrência de
- Na intercalação de merge sort, escolher o elemento da metade esquerda em empates de chave 
- O quicksort clássico por partições pode ter custo Θ(n²) quando os pivôs geram repetidament
- Ao inserir em vetor dinâmico com capacidade dobrada a cada realocação, uma inserção indivi
- Como heapsort e merge sort têm pior caso O(n log n), os dois são necessariamente estáveis 
```

