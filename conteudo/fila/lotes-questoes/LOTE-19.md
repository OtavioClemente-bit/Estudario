# Pedidos de questões extras, lote 19 de 24 (20/10/2026)

Mesmo esquema dos lotes anteriores. Conversa nova, cole a Mensagem 0 e depois um pedido por mensagem. Salve cada resposta como `<id>.questoes.json`. Matérias deste lote: 50, somando 2737 tópicos de edital.

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

## Pedido 1: Bioestatística aplicada à saúde (`saude-publica.bioestatistica`, 62 tópicos de edital)

```
Matéria: saude-publica.bioestatistica (Bioestatística aplicada à saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Variáveis, coleta de dados, amostragem e visualização": 17
- "2. Estatística descritiva, probabilidade e distribuição normal": 17
- "3. Inferência: intervalos, hipóteses, testes e modelos": 17
- "4. Tabelas 2×2 e medidas de associação": 17
- "5. Avaliação estatística de testes diagnósticos e curva ROC": 16
- "6. Tamanho de amostra, poder estatístico e roteiro de prova": 16

Já existem (não repita):
- Em um banco de dados de ambulatório, a variável grupo sanguíneo (A, B, AB, O) foi codifica
- Um pesquisador deseja representar a distribuição da concentração de glicose, medida contin
- Os tempos de atendimento, em minutos, foram 2, 3, 3, 4 e 18. Qual par representa, respecti
- Entre 50 profissionais, 15 declararam ter participado de treinamento. Qual é a frequência 
- Para estudar associação entre tabagismo (sim/não) e presença de tosse (sim/não) em grupos 
- A sensibilidade de um teste diagnóstico é calculada como:
- Em uma distribuição aproximadamente normal, qual proporção de observações fica entre a méd
- Para um risco de 20% no grupo exposto e de 10% no grupo não exposto, o risco relativo é:
- Um instrumento foi aplicado à mesma pessoa repetidamente e produziu valores quase idêntico
- Em uma população cuja média de um marcador é 100 unidades e o desvio-padrão populacional é
- Um teste de hipótese unilateral produziu p=0,03 e foi previamente fixado α=0,05. Qual inte
- Em 100 expostos, 30 adoeceram; em 100 não expostos, 10 adoeceram. Qual é a diferença absol
- Um serviço tem teste com sensibilidade e especificidade mantidas, mas passa a aplicá-lo em
- Para comparar uma medida de pressão arterial obtida antes e depois de uma intervenção nos 
- Em uma tabela 2×2, a=30, b=70, c=10 e d=90. Qual é a odds ratio da exposição para o desfec
- Em um estudo de precisão de média com variabilidade constante, deseja-se reduzir pela meta
- Um relatório apresenta IC95% para risco relativo igual a [0,80; 1,60]. Assumindo inferênci
- Um teste de rastreio em 200 indivíduos produziu VP=36, FN=4, FP=32 e VN=128. Qual o valor 
- No teste qui-quadrado de independência aplicado a tabela com 3 linhas e 4 colunas, quantos
- Em regressão linear com intercepto, foi obtido R²=0,64. Qual a interpretação estatística m
- Em um estudo caso-controle clássico, o pesquisador recrutou número pré-fixado de casos e c
- Ao reduzir o ponto de corte de um marcador em que valores mais altos indicam doença, qual 
- Uma amostra por conglomerados apresentou forte semelhança das respostas entre pessoas da m
- Para estimar uma proporção desconhecida em amostragem simples, considerando IC aproximado 
- A escala ordinal permite ordenar categorias, mas não assegura que a distância entre catego
- A amostra por conveniência torna-se probabilística quando contém mais da metade da populaç
- A mediana é obtida após ordenar os valores e tende a ser menos afetada por observações ext
- A variância e o desvio-padrão são expressos necessariamente na mesma unidade de medida da 
- O valor de p é a probabilidade de a hipótese nula ser verdadeira depois de observados os d
- Em uma comparação de riscos, um risco relativo igual a 1 corresponde à igualdade dos risco
- O valor preditivo negativo utiliza como denominador todas as pessoas cujo teste teve resul
- Duas categorias de eventos podem ser mutuamente exclusivas e ainda apresentar interseção d
- O erro-padrão da média descreve a dispersão dos indivíduos na amostra exatamente como faz 
- Um intervalo de confiança de 95% para uma diferença de médias que exclui zero é, em condiç
- Se p=0,20 em teste ao nível α=0,05, o resultado demonstra que as médias populacionais são 
- Na tabela 2×2 a=24, b=76, c=12 e d=88, o risco relativo dos expostos em relação aos não ex
- Mantida a sensibilidade e a especificidade do teste e reduzida a prevalência da condição, 
- Na curva ROC, o eixo horizontal geralmente mostra 1 menos a especificidade e o eixo vertic
- Na amostragem estratificada probabilística, é impossível estimar erro-padrão porque os sor
- O teorema central do limite garante que cada observação bruta de uma população assimétrica
- Em uma regressão linear simples, correlação de Pearson r=0 exclui necessariamente toda for
- Em uma tabela de contingência com contagens esperadas muito baixas, a aproximação pelo qui
- Quando um evento é comum, odds ratio e risco relativo são numericamente iguais por definiç
- Num cálculo inicial de tamanho para estimar proporção com mesma confiança e precisão, esco
```

## Pedido 2: APIs e integração de sistemas: REST, JSON, OpenAPI e webhooks (`informatica.apis-rest-integracao`, 61 tópicos de edital)

```
Matéria: informatica.apis-rest-integracao (APIs e integração de sistemas: REST, JSON, OpenAPI e webhooks)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos de APIs e protocolo HTTP": 17
- "Arquitetura REST e desenho de recursos": 17
- "Formatos JSON e XML e validação de contratos": 17
- "OpenAPI, autenticação e autorização": 17
- "Webhooks, eventos e alternativas de integração": 16
- "Segurança e operação de APIs": 16

Já existem (não repita):
- Uma API devolveu uma representação de aluno consultada pelo endpoint GET /alunos/42. Qual 
- Uma aplicação envia JSON no corpo e deseja receber JSON na resposta. Qual par de cabeçalho
- Qual recurso representa uma matrícula individual em um desenho de URI orientado a recursos
- Qual das alternativas é um documento JSON sintaticamente válido?
- Uma equipe quer publicar uma interface navegável de documentação já descrita em OpenAPI. Q
- No uso comum de APIs protegidas por OAuth 2.0, qual cabeçalho apresenta um access token be
- O sistema de pagamentos envia um POST ao sistema de pedidos quando uma cobrança é aprovada
- Um usuário autenticado altera /contas/10 para /contas/11 e visualiza conta que não lhe per
- Após o cliente enviar DELETE /arquivos/5, a API responde 204. Na segunda tentativa idêntic
- Um cliente envia um corpo JSON bem formado, mas usa Content-Type: application/xml, e o ser
- Uma atualização deve ser rejeitada caso outro operador já tenha alterado o cadastro desde 
- Em listagem de registros que recebe muitas inserções, a equipe quer navegação sequencial c
- Um JSON Schema contém properties.nome com type string, mas required inclui apenas id. Qual
- Uma equipe usa access token OAuth 2.0 estruturado como JWT. Qual medida permanece necessár
- Um provedor reenviou duas vezes um webhook PagamentoAprovado porque não recebeu resposta d
- Uma integração publica MatriculaConfirmada após gravar o registro, e outros sistemas reage
- Uma API apresenta CORS restritivo, mas endpoint permite ler documentos de terceiros a qual
- Um usuário comum envia {"nome":"Rui","isAdmin":true} e o backend grava todos os campos do 
- Uma API responde 202 e fornece /tarefas/900 como URI para consulta de andamento. O operado
- Um sistema envia o mesmo POST /cobrancas duas vezes após timeout e passa Idempotency-Key c
- Um esquema JSON Schema draft 2020-12 define type object, required ["id"], properties.id {t
- Uma plataforma aceita JWT assinado para api-a e o reutiliza na api-b, que confere apenas a
- Um consumidor de eventos precisa aplicar atualização em banco e confirmar mensagem do brok
- Uma API processa URL fornecida por cliente para buscar imagem e permite alcançar endereço 
- O cabeçalho Accept indica tipos de mídia que o cliente considera aceitáveis para a respost
- O código 201 Created sinaliza tipicamente a criação de um recurso em decorrência de requis
- Em REST, stateless significa que o servidor é proibido de manter banco de dados persistent
- Em JSON válido, literais booleanos são representados como true e false, em minúsculas.
- A especificação OpenAPI permite descrever endpoints, parâmetros e respostas de uma API HTT
- JWT é sempre um conteúdo cifrado que impede qualquer pessoa que possua o token de ler seus
- Webhook é uma forma de notificação orientada a eventos, normalmente enviada por HTTP a um 
- O uso de HTTPS dispensa verificar se um usuário autenticado tem permissão sobre cada objet
- Um DELETE pode ser idempotente mesmo que o status da segunda resposta seja diferente do st
- Ao responder 202 Accepted, o servidor garante que o trabalho assíncrono já foi concluído c
- A paginação por deslocamento pode gerar duplicidades ou omissões durante a navegação se o 
- Todo serviço HTTP que retorna JSON implementa, necessariamente, todas as restrições arquit
- Em JSON Schema, required indica a presença obrigatória de propriedades, enquanto propertie
- OAuth 2.0 estabelece framework para autorização delegada; seu access token não precisa, ob
- A entrega at-least-once de uma fila elimina a possibilidade de processamento duplicado de 
- Um evento MatriculaConfirmada descreve fato ocorrido; o comando ConfirmarMatricula express
- CORS é mecanismo de autenticação capaz de bloquear diretamente todo cliente não navegador 
- A falha de autorização por propriedade pode permitir gravar atributos sensíveis não autori
- O método PATCH possui garantia geral de idempotência pela semântica HTTP, assim como PUT.
- If-Match associado a ETag permite expressar condição para atualização e evitar sobrescrita
- No JSON Schema, additionalProperties: false pode impedir propriedades não previstas no esq
- Conferir apenas a assinatura de um JWT sempre basta para garantir que ele foi emitido para
- Webhooks que carregam eventId dispensam autenticação de origem, já que o identificador pro
- Uma API que aceita URL arbitrária para que o backend faça solicitações pode estar sujeita 
```

## Pedido 3: Leis especiais: crimes do parcelamento do solo, prerrogativas da advocacia, discriminação de pessoas com HIV e ação penal originária (`legislacao-penal-especial.parcelamento-solo-advocacia-hiv-acao-originaria`, 61 tópicos de edital)

```
Matéria: legislacao-penal-especial.parcelamento-solo-advocacia-hiv-acao-originaria (Leis especiais: crimes do parcelamento do solo, prerrogativas da advocacia, discriminação de pessoas com HIV e ação penal originária)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Crimes da Lei de Parcelamento do Solo Urbano (Lei nº 6.766/1979)": 25
- "2. Estatuto da Advocacia: princípios e direitos do advogado": 25
- "3. Advogado na investigação criminal, crime do art. 7º-B e disciplina": 25
- "4. Discriminação de pessoas com HIV e ação penal originária": 25

Já existem (não repita):
- Segundo a Lei nº 6.766/1979, dar início a loteamento do solo para fins urbanos sem autoriz
- O crime de loteamento irregular previsto no art. 50 da Lei nº 6.766/1979 só se consuma com
- O crime do art. 50 da Lei nº 6.766/1979 é qualificado quando cometido:
- Fazer afirmação falsa, em prospecto ou comunicação ao público, sobre a legalidade de lotea
- O oficial que registra loteamento ou desmembramento não aprovado pelos órgãos competentes 
- Pela Lei nº 6.766/1979, quem de qualquer modo concorre para os crimes de loteamento irregu
- Segundo o Estatuto da Advocacia, NÃO se inclui na atividade privativa de advocacia:
- O Estatuto da Advocacia estabelece hierarquia funcional dos magistrados sobre os advogados
- Afirmando urgência, o advogado pode atuar sem procuração, obrigando-se a apresentá-la no p
- No exercício da profissão, o advogado é inviolável por seus atos e manifestações, nos limi
- Quando o advogado for preso em flagrante por motivo ligado ao exercício da advocacia, o Es
- O advogado somente poderá ser preso em flagrante, por motivo de exercício da profissão, em
- O advogado tem direito de comunicar-se com seus clientes presos, pessoal e reservadamente,
- Pelo Estatuto da Advocacia, antes de sentença transitada em julgado, o advogado só pode se
- O advogado tem direito de ingressar livremente em delegacias e prisões, mesmo fora da hora
- A quebra da inviolabilidade do escritório de advogado investigado pela prática de crime ex
- Segundo o Estatuto da Advocacia, o advogado pode examinar autos de investigação em andamen
- A autoridade que conduz a investigação pode delimitar o acesso do advogado aos elementos d
- Impedir o advogado de assistir seu cliente investigado durante o interrogatório acarreta, 
- O fornecimento ao advogado de autos incompletos, com retirada de peças já incluídas no cad
- O crime previsto no art. 7º-B do Estatuto da Advocacia consiste em violar direitos ou prer
- A jurisdição disciplinar da OAB exclui a jurisdição comum, de modo que o advogado punido p
- O poder de punir disciplinarmente os inscritos na OAB compete, em regra, ao:
- A Lei nº 12.984/2014 pune a discriminação contra a pessoa vivendo com HIV ou com aids com 
- Segundo a Lei nº 12.984/2014, divulgar a condição de pessoa vivendo com HIV constitui crim
- Entre as condutas previstas como crime na Lei nº 12.984/2014, em razão da condição sorológ
- Negar emprego ou trabalho a uma pessoa em razão de ela viver com HIV é conduta tipificada 
- Recusar a matrícula de criança em creche por ser ela portadora do HIV configura, em tese, 
- Na ação penal originária regida pela Lei nº 8.038/1990, nos crimes de ação penal pública, 
- Na ação penal originária perante o STJ ou o STF, o relator funciona como juiz da instrução
- Na Lei nº 8.038/1990, oferecida a denúncia, o acusado é notificado para apresentar respost
- Na fase de recebimento da denúncia na ação penal originária, o Tribunal pode, desde logo, 
- Concluída a instrução na ação penal originária, a acusação e a defesa apresentam alegações
- No julgamento final da ação penal originária, a acusação e a defesa terão, sucessivamente,
- Estando o indiciado preso, o prazo para o Ministério Público oferecer denúncia na ação pen
- O advogado tem o direito de recusar-se a depor como testemunha sobre fato relacionado com 
- Conforme o Estatuto da Advocacia, a medida cautelar que importe violação do escritório do 
- São nulos, segundo o Estatuto da Advocacia, os atos privativos de advogado praticados por:
- Na ação penal originária regida pela Lei nº 8.038/1990, desde a Lei nº 13.964/2019, o Mini
- Segundo o Estatuto da Advocacia, no seu ministério privado, o advogado:
```

## Pedido 4: Legislação aduaneira (`direito-tributario.legislacao-aduaneira`, 61 tópicos de edital)

```
Matéria: direito-tributario.legislacao-aduaneira (Legislação aduaneira)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Controle aduaneiro, veículos e Siscomex": 24
- "2. Imposto de Importação: materialidade e apuração": 19
- "3. Imposto de Exportação: fato gerador, base e alíquotas": 19
- "4. Defesa comercial, antidumping, subsídios e consultas": 19
- "5. Regimes aduaneiros, FUNDAF e integração da fiscalização": 19

Já existem (não repita):
- Um navio vindo do exterior pretende descarregar mercadorias em um cais não alfandegado. O 
- Uma importadora registrou declaração abrangida pela cobrança da Taxa Siscomex, mas o II da
- Uma sociedade empresária compra máquinas produzidas no exterior e as faz ingressar no terr
- Uma empresa nacional exporta produto enquadrado em operação sujeita ao Imposto de Exportaç
- Uma indústria nacional pede medida antidumping após observar aumento de importações e perd
- Um lote estrangeiro será transportado, sob fiscalização, do porto de chegada até recinto a
- Ao estudar uma receita destinada a melhorar meios e atividades de fiscalização tributária 
- Uma empresa exporta produto nacional para comprador situado no exterior. Qual critério ger
- Uma empresa importa mercadorias sujeitas a alíquota ad valorem de II. A apuração aponta va
- Uma importadora precisa esclarecer formalmente a posição NCM de equipamento com funções di
- Uma empresa brasileira reimporta mercadoria nacional que havia sido exportada. Em qual aná
- Um condutor de veículo internacional deseja transbordar carga em local não habilitado e su
- Um material de treinamento afirma que a alíquota de 30% fixada no Decreto-Lei nº 1.578/197
- Produtor brasileiro requer providência porque produto importado foi beneficiado por subsíd
- O regulamento de determinada mercadoria importada estabelece, hipoteticamente, II específi
- Uma empresa tem autorização para armazenar mercadoria estrangeira em recinto alfandegado p
- Uma consulta formal eficaz trata da classificação de um equipamento na NCM. A empresa aleg
- Em investigação antidumping, produtores nacionais sofreram prejuízo tanto por importações 
- Uma exportação sujeita ao IE foi documentada e o tributo recolhido, mas a operação não se 
- Uma máquina ingressará no Brasil para prestação onerosa de serviços por prazo delimitado. 
- Uma proposta de ato do Executivo pretende elevar a alíquota do IE para produto sujeito à c
- Um importador declara mercadoria estrangeira que, comprovadamente, foi destruída sob contr
- Uma investigação aponta dumping e dano interno, mas demonstra que todo o prejuízo decorreu
- Um ato busca impor simultaneamente direito antidumping e compensatório para neutralizar ex
- Empresa com ato concessório de drawback importa insumo para produzir bens de exportação e 
- Uma aeronave procedente do exterior aterrissa em aeroporto alfandegado, onde a carga é apr
- Uma importadora obteve isenção do Imposto de Importação e, por essa razão, está automatica
- Uma empresa importa mercadorias com alíquota ad valorem e deve tomar o valor aduaneiro, ap
- Uma operação de saída de produto nacional ao exterior, ainda que não integre a relação de 
- A investigação que pretenda justificar direito antidumping definitivo por produto importad
- Uma mercadoria em trânsito aduaneiro entre porto e recinto interior pode ser vendida livre
- Um produto nacional ou nacionalizado vendido a comprador estrangeiro pode integrar a mater
- O FUNDAF constitui uma alíquota adicional do Imposto de Importação, exigível em cada desem
- Uma consulta de classificação fiscal de mercadoria pode ser pertinente quando a empresa te
- Na aplicação de medida compensatória, basta demonstrar que o preço do importado é inferior
- A autorização excepcional para ingresso de veículo internacional em ponto não alfandegado,
- Se uma mercadoria produzida no Brasil foi exportada e depois retorna, o Imposto de Importa
- Em operação de exportação sujeita ao IE, a alíquota efetiva pode ser inferior aos 30% fixa
- Como o II e o IE integram o art. 153 da Constituição, ambos dependem de noventa dias e do 
- Em investigação antidumping, efeitos de redução de demanda e de importações não investigad
- A investigação antidumping e a de subsídios representam o mesmo procedimento, pois ambas p
- No regime de admissão temporária para utilização econômica, a legislação pode exigir pagam
- O protocolo de consulta formal de classificação fiscal torna dispensável a entrega de qual
- Um produto estrangeiro de origem abrangida por ato antidumping pode demandar exame da medi
- Por ter sido instituído pelo Decreto-Lei nº 1.437/1975, o FUNDAF constitui hipótese de inc
```

## Pedido 5: Endoscopia digestiva (`medicina.endoscopia-digestiva`, 61 tópicos de edital)

```
Matéria: medicina.endoscopia-digestiva (Endoscopia digestiva)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, equipamentos, segurança e preparo": 20
- "Endoscopia alta, colonoscopia e métodos diagnósticos": 20
- "Hemostasia, polipectomia e ressecção endoscópica": 20
- "CPRE, vias biliares, ecoendoscopia e intestino delgado": 20
- "Gastrostomia, próteses, complicações e decisão clínica": 20

Já existem (não repita):
- Um médico precisa explicar ao paciente a principal utilidade de uma colonoscopia, em compa
- A endoscopia digestiva alta de rotina avalia o esôfago, o estômago e a porção proximal do 
- Durante o treinamento em reprocessamento, qual ação deve ocorrer antes da desinfecção de a
- A limpeza manual dos canais de um endoscópio flexível pode ser dispensada quando o desinfe
- O laudo da EDA descreve esôfago, estômago e bulbo duodenal. Qual conclusão é tecnicamente 
- Na colonoscopia, documentar a válvula ileocecal e o óstio apendicular ajuda a comprovar qu
- Uma úlcera gástrica apresenta sangramento ativo em jato no exame endoscópico. A classifica
- Uma cápsula endoscópica convencional permite retirar pólipos do jejuno com uma alça acopla
- Qual método é o mais apropriado quando se deseja visualizar mucosa de intestino delgado, s
- Na colonoscopia eletiva, administrar a preparação intestinal em dose fracionada pode melho
- Em uma colonoscopia de rastreamento, é identificado pólipo colorretal séssil de 4 mm sem s
- Forrest III corresponde à úlcera com sangramento arterial ativo em jato.
- Qual estrutura é diretamente alcançada para cateterismo durante uma CPRE biliar convencion
- A ecoendoscopia combina visão endoscópica e ultrassonografia e pode orientar punção de les
- Uma colonoscopia recebe Boston 2 no cólon direito, 3 no transverso e 1 no esquerdo. Qual é
- A gastrostomia endoscópica percutânea é sempre indicada quando um paciente deixa de aceita
- Na EDA, uma lesão superficial gástrica parece passível de ressecção endoscópica. Qual estr
- Na Escala de Boston, um preparo com notas 3, 3 e 1 é necessariamente adequado, pois o tota
- Uma úlcera duodenal com Forrest Ib mantém sangramento durante EDA. Entre as possibilidades
- Pacientes que usam antiagregantes ou anticoagulantes devem ter a medicação suspensa antes 
- Em paciente com colangite por obstrução biliar, a equipe define CPRE terapêutica. Qual é o
- Em úlcera com sangramento ativo, a injeção isolada de adrenalina é, em geral, suficiente c
- Paciente com sangramento digestivo baixo importante está estável após medidas iniciais. Qu
- Pólipos colorretais de 6 a 9 milímetros, sem sinais de invasão, são habitualmente tratados
- Uma paciente usa anticoagulante por indicação de alto risco tromboembólico e realizará pol
- A CPRE é hoje empregada preferencialmente para intervenções nas vias biliares ou pancreáti
- Uma lesão subepitelial gástrica precisa de caracterização de camadas e avaliação de linfon
- A retossigmoidoscopia normal exclui com segurança pólipos e tumores situados no cólon prox
- Após colocar uma gastrostomia endoscópica, a equipe orienta prevenção de buried bumper syn
- Uma complicação que deve ser avaliada especificamente após CPRE é pancreatite, cuja preven
- Na colonoscopia, pólipo adenomatoso não pediculado de 24 mm é bem delimitado e sem sinais 
- Em paciente com hemorragia digestiva baixa e instabilidade persistente, colonoscopia sem p
- Um pólipo colorretal grande apresenta área deprimida, padrão vascular irregular e suspeita
- A remoção fragmentada de lesão colorretal com suspeita de câncer superficial pode dificult
- Paciente submetido a CPRE tem alto risco de pancreatite pós-procedimento. Qual conjunto de
- Na suspeita de colite microscópica, ausência de alterações visíveis na colonoscopia elimin
- Sobre a inserção de prótese única na via biliar durante CPRE, assinale a afirmativa corret
- Na CPRE destinada apenas à colocação de uma única prótese biliar, a esfincterotomia prévia
- Mulher com diarreia aquosa persistente e colonoscopia macroscopicamente normal tem suspeit
- Após EMR a quente de lesão adenomatosa grande não pediculada no cólon direito, o fechament
- Um serviço recebe paciente com suspeita de obstrução intestinal por estenose de delgado e 
- Paciente com disfagia por estenose maligna esofágica não curável é avaliado para paliação.
- Após EMR de adenoma grande não pediculado no cólon direito, qual medida é especificamente 
- Após PEG, uma sonda desloca-se acidentalmente nos primeiros dias, antes da consolidação do
- Paciente está hipotenso e com hematoquezia intensa, apesar das medidas iniciais. Qual recu
- Qual conduta é mais coerente para uma PEG eletiva, em comparação com uma EDA diagnóstica s
```

## Pedido 6: Fisioterapia neurofuncional e traumato-ortopédica (`fisioterapia.neurofuncional-traumato-ortopedica`, 60 tópicos de edital)

```
Matéria: fisioterapia.neurofuncional-traumato-ortopedica (Fisioterapia neurofuncional e traumato-ortopédica)
Capítulos e quantas questões quero em cada um (total 100):
- "Cinesiologia, biomecânica e análise da marcha": 20
- "Fisioterapia neurofuncional: AVC, lesão medular e condições neuromotoras": 20
- "Traumato-ortopedia, amputações, órteses e próteses": 20
- "Cinesioterapia, eletroterapia e termoterapia: escolha e segurança": 20
- "Fisioterapia geriátrica, atenção domiciliar e ética profissional": 20

Já existem (não repita):
- Na marcha habitual em velocidade confortável, o apoio tende a ocupar maior parcela do cicl
- Comprimento da passada corresponde à distância entre dois contatos sucessivos de pés opost
- Na descida controlada de um degrau, o quadríceps pode atuar excentricamente para conter a 
- A observação de marcha em steppage comprova isoladamente lesão do nervo fibular comum.
- Espasticidade e fraqueza podem coexistir no mesmo segmento após acidente vascular cerebral
- A paralisia cerebral é definida pela progressão contínua da lesão encefálica durante toda 
- O treino funcional repetitivo pode ser parte da recuperação após AVC, respeitando estabili
- A ausência de marcha independente é suficiente para classificar uma lesão medular como com
- A presença de osteossíntese em uma fratura autoriza, por si só, descarga total imediata so
- Órtese e prótese são termos intercambiáveis, pois ambas substituem exclusivamente membros 
- Após reconstrução do LCA, a liberação para esporte pode exigir critérios de força e movime
- Em amputação transtibial, manter o joelho residual em flexão prolongada é estratégia padrã
- TENS é empregado principalmente como recurso de modulação da dor, não como sinônimo de tre
- A cinesioterapia passiva substitui integralmente o treinamento ativo para ganho de força m
- A FES pode ser sincronizada com uma atividade como a dorsiflexão na fase de balanço da mar
- Em paciente com neuropatia sensitiva importante, aplicações intensas de calor são seguras 
- Na fisioterapia geriátrica, prevenção de quedas envolve também o ambiente domiciliar e não
- A Resolução COFFITO nº 474/2016 permanece a referência vigente sem substituição para fisio
- O plano fisioterapêutico domiciliar deve considerar as metas e as barreiras da residência,
- A autorização verbal genérica de um paciente permite divulgar livremente imagens clínicas 
- Qual grandeza da biomecânica descreve o efeito rotacional de uma força em torno de um eixo
- Um paciente aumenta a distância horizontal de um peso em relação ao ombro, mantendo a forç
- Ao analisar uma marcha, observou-se queda da pelve do lado oposto à perna de apoio. A hipó
- Na descrição biomecânica de uma extensão de joelho sentado, com perna e pé movendo-se livr
- Uma fisioterapeuta compara parâmetros espaciais da marcha de um paciente. A relação concei
- Paciente após AVC apresenta dificuldade de transferir-se da cama para a cadeira. Qual abor
- Em pessoa com suspeita de disreflexia autonômica associada a lesão medular alta, a conduta
- Uma criança com paralisia cerebral tem nível funcional registrado na GMFCS. O instrumento 
- Durante avaliação de fraqueza em doença neuromuscular, qual par condição–achado é mais coe
- Em pessoa com AVC que apresenta ombro parético e subluxação, qual princípio deve orientar 
- A função típica de uma órtese tornozelo–pé em pessoa selecionada com pé caído é:
- Após reconstrução de LCA, uma pessoa deseja voltar a futebol com mudança de direção. Qual 
- Um paciente com amputação transtibial relata sensação não dolorosa de que os dedos do pé r
- Paciente em pós-operatório de fratura apresenta febre, edema e dor progressiva intensa na 
- Em pessoa com prótese transtibial, observam-se pontos de vermelhidão persistente após reti
- Qual recurso eletroterapêutico é mais diretamente associado à analgesia sensorial?
- Pessoa com pé caído recebe estímulo elétrico sincronizado ao balanço durante a marcha. O r
- Em planejamento de termoterapia para paciente com neuropatia e perda de sensibilidade térm
- Sobre as diferenças entre NMES e FES, assinale a formulação mais adequada.
- Um paciente com marca-passo solicita eletroterapia sobre o tórax por conta própria. Qual r
- Qual medida ambiental é coerente com prevenção de quedas de pessoa idosa em casa?
- A Resolução COFFITO que atualizou a atenção domiciliar da Fisioterapia em 2022 e revogou d
- Para idoso com histórico de quedas e marcha lenta, qual abordagem integra corretamente os 
- Uma empresa divulga antes/depois de pacientes acompanhados pela clínica sem termo formal e
- Em atendimento domiciliar, um cuidador pede que o fisioterapeuta prescreva programa sem av
```

## Pedido 7: Função exponencial e logaritmos (`matematica.exponencial-logaritmos`, 60 tópicos de edital)

```
Matéria: matematica.exponencial-logaritmos (Função exponencial e logaritmos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Potências de expoente real e fundamentos da exponenciação": 15
- "2. Função exponencial, gráfico e modelos de crescimento e decaimento": 18
- "3. Logaritmos: definição, sistemas, propriedades e mudança de base": 31
- "4. Função logarítmica: domínio, inversa, gráficos e transformações": 15
- "5. Equações e inequações exponenciais e logarítmicas": 21

Já existem (não repita):
- Em uma atividade de cálculo, é solicitado o valor de 2^5. Qual resultado corresponde à pot
- Uma planilha aplica a potência 5^(−2) para calcular um fator de correção. Qual valor dever
- Uma função é definida por f(x)=3^x. Qual é o valor de f(0) utilizado para marcar seu gráfi
- Uma quantidade é modelada por f(x)=(1/2)^x, com x real. Qual descrição corresponde ao comp
- A expressão log₂(8) aparece em uma tabela. Qual expoente deve substituir seu valor?
- Um gráfico corresponde à função g(x)=log₃(x). Qual ponto obrigatoriamente pertence à curva
- Deseja-se calcular log₅(25). Que interpretação permite encontrar o resultado diretamente?
- Uma variável x satisfaz a equação 2^x=16. Qual é seu valor real?
- Qual domínio real deve ser atribuído à expressão log₂(x−5), considerada como função de x?
- Uma cultura começa com 80 unidades e dobra a cada período completo. Qual será a quantidade
- Duas potências satisfazem 8^x=4^(x+1). Qual solução resulta da escrita das bases como potê
- Uma expressão contém log₃(27)−log₃(3). Qual é o valor da diferença?
- Uma amostra tem 200 gramas e perde 20% de sua massa a cada período. Qual massa resta após 
- Em uma transformação algébrica, deseja-se representar log₄(8) em base dois. Qual equivalên
- Deseja-se resolver a desigualdade 2^(x−1)>8. Qual conjunto solução decorre da monotonia da
- Uma função é definida por h(x)=log₂(x+4). Qual é a assíntota vertical de seu gráfico?
- A expressão log_(1/2)(x)>−2 deve ser resolvida nos reais. Qual alternativa apresenta seu c
- Uma equação algébrica envolve 4^x−5·2^x+4=0. Quais soluções reais devem ser mantidas após 
- Um problema conduz à equação log₂(x−1)+log₂(x−3)=3. Qual é a solução real depois de respei
- Uma desigualdade é escrita 3^(2x−1)≤1/27. Qual conjunto de soluções reais é correto?
- Considere log_(1/3)(2x−1)<log_(1/3)(x+2). Qual solução respeita as condições de existência
- Um modelo satisfaz 2^x=3, sem solução inteira. Qual expressão exata determina x por mudanç
- Uma função tem a forma f(x)=log₃(x²−x−2). Qual conjunto descreve corretamente o domínio re
- Um cálculo apresenta a equação 2^x+2^(x+1)=24. Qual o valor real de x?
- Um problema exige resolver log₂(x²−5x+6)=1 nos reais. Qual conjunto de soluções satisfaz o
- Uma função exponencial fundamental de base cinco possui domínio igual ao conjunto dos núme
- A igualdade 3^(x+y)=3^x+3^y decorre da propriedade de soma de expoentes aplicada a uma bas
- O logaritmo de um número positivo pode ser negativo; em particular, log₂(1/8)=−3.
- A função f(x)=log₃(x) está definida para x=0 porque o gráfico do logaritmo atravessa o eix
- A função f(x)=4^x é estritamente crescente, pois sua base é maior que um.
- Em log₁₀(100), o resultado é cem porque o logaritmo devolve o argumento quando a base é de
- Uma quantidade inicial de 100 unidades cresce 10% por período. Depois de dois períodos, se
- A igualdade log₂(6)=log₂(2)+log₂(4) exemplifica a propriedade do logaritmo de uma soma de 
- Pela mudança de base, log₄(16) pode ser escrito como log₂(16)/log₂(4), resultando em dois.
- Na inequação (1/2)^x>(1/2)^3, a resposta é x>3, pois bases iguais permitem conservar o sen
- Para a função f(x)=log₂(x²−9), o domínio real é a união dos intervalos x<−3 e x>3.
- A equação 2^(x+1)=8 tem solução x=4, porque a exponencial de base dois é crescente.
- O logaritmo de quatro na base um meio vale menos dois, porque (1/2)^(−2)=4.
- Na equação log₂(x−1)+log₂(x−3)=3, as raízes algébricas x=−1 e x=5 são ambas soluções, pois
- A equação 9^x=27^(x−1) possui solução x=3, obtida reescrevendo as bases como potências de 
- A identidade log₂(x²)=2log₂(x) é válida para qualquer número real x diferente de zero, por
- O conjunto solução de log₂(x−3)<1 é o intervalo aberto 3<x<5.
- Para base logarítmica 1/3, a desigualdade log_(1/3)(x−1)≥0 tem como conjunto solução x≥2.
- Na equação 4^x−5·2^x+4=0, a substituição t=2^x leva a raízes positivas t=1 e t=4, correspo
- A equação log₂(x²−5x+6)=1 possui apenas x=4 como solução, pois x=1 torna o logaritmando ne
```

## Pedido 8: Acessibilidade e inclusão no Poder Judiciário (Resoluções CNJ 230/2016 e 401/2021) (`legislacao.resolucoes-cnj-acessibilidade`, 60 tópicos de edital)

```
Matéria: legislacao.resolucoes-cnj-acessibilidade (Acessibilidade e inclusão no Poder Judiciário (Resoluções CNJ 230/2016 e 401/2021))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Evolução normativa, alcance e dever institucional": 55
- "2. Conceitos estruturantes e identificação de barreiras": 12
- "3. Atendimento, comunicação e participação processual": 11
- "4. Inclusão no trabalho, acompanhamento e adaptações": 11
- "5. Unidades, comissões e governança da inclusão": 11

Já existem (não repita):
- Ao revisar uma cartilha sobre acessibilidade, uma equipe constata que o material apresenta
- Um órgão reformou sua entrada física e afirma que não precisa adequar o portal de acompanh
- Um parecer utiliza somente o diagnóstico clínico para definir se determinada pessoa poderá
- Durante a implementação de atendimento remoto, a administração entende que a política incl
- Uma nota interna descreve a política atual como decorrente exclusivamente da Resolução CNJ
- Ao atualizar a cartilha institucional, uma equipe afirma que a Resolução CNJ nº 401/2021 r
- Um órgão sustenta que a Resolução nº 401/2021 alcança apenas pessoas presentes fisicamente
- Para decidir sobre a inclusão de determinada pessoa, a administração avalia impedimentos d
- Uma unidade técnica conclui que basta atender às disposições da antiga Resolução nº 230/20
- Ao criar um formulário judicial que já nasce utilizável por pessoas com diferentes necessi
- Em atendimento, um funcionário dirige todas as perguntas ao acompanhante e ignora a pessoa
- Uma pessoa precisa de forma específica de comunicação em ato judicial, embora o serviço te
- Um sistema exige operações impossíveis por leitor de tela; o aviso sobre a falha existe ap
- Uma diretriz interna sustenta que só há discriminação se for provada a intenção explícita 
- Ao encontrar escada sem alternativa acessível na entrada de uma edificação, a administraçã
- Um portal planejado de modo acessível para vários públicos expressa desenho universal; ain
- Uma instituição recusa uma adaptação necessária e adequada, produzindo exclusão de pessoa 
- Uma aplicação digital pode apresentar barreira tecnológica por impedir uso de recurso assi
- Uma pessoa surdocega participará de ato processual e solicita apoio especializado para a c
- Uma pessoa com deficiência é parte em processo e pergunta se possui direito à prioridade p
- Em audiência com pessoa surda usuária de Libras, um setor sugere que ela custeie o profiss
- Acompanhante de uma pessoa com deficiência pede prioridade em processo judicial do qual é 
- Um ato on-line terá pessoa com deficiência auditiva, e a plataforma admite vídeo mas não o
- Em um processo com pessoa surdocega, a equipe judiciária considera a nomeação ou o uso de 
- Um acompanhante exige tramitação prioritária em processo próprio somente por acompanhar pe
- Ao custear os recursos de interpretação previstos na resolução, a administração pode exigi
- Um órgão pode ter prédio acessível e mesmo assim descumprir sua política de inclusão se o 
- Ao avaliar necessidades de adaptação funcional de pessoa com deficiência, a administração 
- Um órgão mantém cadastro de profissionais com deficiência, mas nunca pergunta sobre mudanç
- Uma pessoa do quadro auxiliar encontra impedimentos para realizar atividades em sistema in
- Depois de instalar um elevador, uma administração afirma ter cumprido integralmente a incl
- Uma pessoa relata mudança nas tarefas e incompatibilidade do recurso assistivo antes utili
- Como o cadastro funcional é instrumento estatístico, um órgão está dispensado de verificar
- No acompanhamento de profissional com deficiência, a administração examina acesso ao local
- Profissionais do quadro auxiliar são incluídos na política institucional de acessibilidade
- Uma administração instala piso adequado e, por isso, considera dispensáveis capacitação em
- Na organização da política inclusiva, uma equipe precisa identificar quem coordena ações e
- Uma comissão avalia propostas de políticas para eliminar barreiras institucionais e orient
- Um órgão cria comissão de inclusão, mas elimina a unidade permanente responsável por artic
- Relatório de acessibilidade identifica falhas recorrentes no portal, sem encaminhamento ou
- Uma proposta prevê que a unidade informe indicadores e apoie ajustes, enquanto a comissão 
- Ao definir o fluxo interno de inclusão, a administração prevê unidade permanente para apoi
- Uma comissão entende que, por existir estrutura estratégica, não há necessidade de unidade
- Uma unidade elabora relatório sobre ações inclusivas, acompanha indicadores e articula áre
- Após contabilizar reclamações sobre um sistema inacessível, a administração encerra o trab
```

## Pedido 9: Microbiologia clínica: bacteriologia e micologia (`laboratorio.microbiologia-clinica`, 60 tópicos de edital)

```
Matéria: laboratorio.microbiologia-clinica (Microbiologia clínica: bacteriologia e micologia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fase pré-analítica: coleta, transporte e microscopia": 17
- "2. Meios de cultura, semeadura e identificação inicial": 17
- "3. Bacteriologia: cocos, bacilos e amostras clínicas": 17
- "4. Antibiograma e mecanismos de resistência bacteriana": 17
- "5. Micologia clínica: leveduras e fungos filamentosos": 16
- "6. Controle de qualidade e interpretação responsável": 16

Já existem (não repita):
- Uma amostra de secreção identificada apenas como “ferida”, sem sítio anatômico exato, pode
- Toda amostra destinada à cultura bacteriana deve ser mantida refrigerada até o processamen
- Na coloração de Gram, cocos roxos vistos em cachos constituem uma pista de estafilococos, 
- Um esfregaço muito espesso ou descolorido em excesso nunca interfere no resultado do métod
- A baciloscopia com Ziehl–Neelsen positiva demonstra álcool-ácido resistência, mas não dife
- Ágar MacConkey é classicamente um meio enriquecido destinado a visualizar hemólise alfa e 
- O ágar chocolate fornece fatores de crescimento que auxiliam a recuperação de microrganism
- Catalase positiva em cocos Gram-positivos pode orientar para estafilococos, mas não consti
- O crescimento de estafilococo coagulase negativo em hemocultura deve ser sempre descartado
- Na hemocultura, o volume de sangue inoculado é variável importante para a sensibilidade do
- A contagem de 100.000 UFC/mL deve ser adotada isoladamente como critério diagnóstico absol
- Coprocultura bacteriana convencional identifica, por definição, vírus entéricos, protozoár
- No teste de disco-difusão, o tamanho do halo precisa ser comparado a ponto de corte apropr
- A concentração inibitória mínima de um antibiótico corresponde necessariamente à dose diár
- Nas categorias atuais do BrCAST, a letra I corresponde a sensível aumentando exposição, nã
- A detecção de resistência a carbapenêmicos por antibiograma é prova suficiente de produção
- Fungos leveduriformes podem se reproduzir por brotamento, enquanto fungos filamentosos apr
- Candida recuperada em hemocultura e Candida isolada em escarro têm sempre o mesmo signific
- A identificação de Candida auris pode exigir revisão do método de identificação e comunica
- O controle de qualidade do antibiograma pode ser ignorado quando o isolado clínico apresen
- Ensaio de proficiência e controle interno possuem objetivos complementares: o primeiro ava
- Na coleta de material de abscesso profundo para cultura, qual conduta melhor preserva a po
- Uma amostra mostra bacilos avermelhados após Ziehl–Neelsen. A conclusão laboratorial imedi
- Em um Gram bem executado, uma população de bacilos aparece predominantemente rosada. Isso 
- Um laboratório precisa recuperar bactéria fastidiosa dependente de fatores de crescimento 
- Ao semear bacilos Gram-negativos em MacConkey, observam-se colônias rosadas. O que esse re
- No procedimento de isolamento de uma amostra polimicrobiana, por que fazer estriamento por
- Cocos Gram-positivos em cachos foram isolados. Qual conjunto de achados favorece Staphyloc
- Um paciente com prótese valvar apresenta febre e várias hemoculturas, obtidas em conjuntos
- Na investigação de bacteremia em adulto, qual decisão favorece maior sensibilidade da hemo
- Uma urocultura de jato médio informa flora bacteriana mista em múltiplos morfotipos; não h
- Para investigar diarreia com suspeita de Campylobacter, o laboratório deve considerar prin
- Uma placa de TSA por disco-difusão apresenta halo de inibição de 23 mm. O que é necessário
- Qual alternativa descreve corretamente a concentração inibitória mínima (CIM)?
- Uma enterobactéria tem resistência a cefalosporinas e carbapenêmicos. Qual raciocínio iden
- Sobre a categoria I conforme a convenção atual do BrCAST, assinale a descrição correta.
- A alteração de proteínas de ligação à penicilina, como PBP2a, é mecanismo especialmente as
- Ao receber raspado de unha com suspeita de dermatofitose, qual estratégia inicial é pertin
- Em material tecidual de pessoa imunossuprimida, foram vistas hifas largas, paucisseptadas 
- Ao isolar Candida auris suspeita em uma unidade hospitalar, a equipe deve priorizar:
- Ao controlar novo lote de ágar MacConkey, que característica deve ser verificada, além da 
- Em uma rodada de controle do antibiograma, uma cepa de referência produz halo fora do inte
```

## Pedido 10: Controle de qualidade de produtos farmacêuticos e controle microbiológico (`farmacia.controle-qualidade-microbiologico`, 59 tópicos de edital)

```
Matéria: farmacia.controle-qualidade-microbiologico (Controle de qualidade de produtos farmacêuticos e controle microbiológico)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Garantia da qualidade, boas práticas, amostragem e avaliação estatística": 30
- "2. Ensaios farmacopeicos e particularidades dos produtos": 19
- "3. Controle microbiológico dos produtos não estéreis": 19
- "4. Água farmacêutica, endotoxinas e potência microbiológica": 18
- "5. Produtos estéreis, teste de esterilidade e métodos alternativos": 14

Já existem (não repita):
- Em uma fábrica, a equipe organiza POPs, treinamento e investigação de desvios, enquanto ou
- No recebimento de insumo farmacêutico, o documento estabelece identidade, ensaios e faixas
- O laboratório encontra teor de fármaco abaixo da faixa aprovada para o lote. Sem causa ana
- Ao elaborar procedimento de qualidade em outubro de 2026, é necessário identificar a ediçã
- Um extrato vegetal será utilizado em fitoterápico, mas o fornecedor informa apenas o nome 
- Um laboratório deseja amplificar uma sequência de RNA obtida de amostra para diagnóstico. 
- No controle de creme não estéril, solicita-se contagem de bactérias aeróbias e, separadame
- Antes de analisar amostras, a equipe inocula microrganismos de referência no meio e confir
- Uma água usada em laboratório apresenta condutividade satisfatória, mas houve crescimento 
- Um creme com conservante apresenta resultado sem crescimento, porém o laboratório não demo
- Um gráfico de controle de pH revela ponto além do limite de controle, mas ainda dentro da 
- Na documentação de qualidade, uma equipe usa os termos hemocomponente e hemoderivado como 
- No circuito de distribuição de água purificada, pontos de baixa circulação passam a aprese
- Em diagnóstico molecular, a reação exibe sinal de amplificação na amostra e também no cont
- A potência de determinado antibiótico é avaliada por inibição de microrganismo indicador c
- Uma solução parenteral cumpre o ensaio de esterilidade, mas o ensaio específico detecta en
- Um kit sorológico apresenta boa capacidade de detectar concentrações muito baixas do anali
- Após resultado sem crescimento em amostras de um lote parenteral, a gestão declara demonst
- Uma linha asséptica apresenta melhora após ajuste de fluxo de operadores. A empresa deseja
- Um radiofármaco de meia-vida muito curta é administrado antes do término da incubação do t
- Na avaliação de uma técnica rápida baseada em PCR, detecta-se DNA de microrganismo após tr
- Uma amostra contém ação bacteriostática intensa. A equipe testou a promoção de crescimento
- Um lote de biológico tem teor de proteína total conforme, mas apresenta aumento de agregad
- Em outubro de 2026, ao revisar o marco técnico de hemoterapia, o gestor cita apenas a Port
- Ao avaliar liberação de produto estéril, um lote apresenta ensaio final sem crescimento, m
- Na revisão de um lote, a equipe de controle de qualidade realiza ensaios e verifica especi
- Em exame de teor abaixo da especificação, o responsável pode repetir sucessivamente a anál
- Em outubro de 2026, o estudo dos métodos e monografias nacionais deve considerar a 8ª ediç
- Por não haver obrigação de esterilidade para um xarope oral comum, a fabricação de tal pro
- Um teste de diagnóstico que identifica corretamente grande proporção das pessoas com a con
- O sistema de água para injetáveis apresentou condutividade dentro da especificação; por is
- O laboratório confirma crescimento de cepas de desafio no meio de cultura preparado sem pr
- Um conservante é neutralizado em método de contagem, mas o próprio neutralizante mata os m
- No controle de hemocomponentes em outubro de 2026, é necessário considerar a RDC nº 34/201
- Depois de comprovada esterilidade de preparação parenteral, considera-se desnecessário ver
- No ensaio de endotoxinas por LAL, as modalidades gel-clot, turbidimétrica e cromogênica ob
- Uma amostra de antibiótico produz uma zona de inibição maior que outra, e o analista conve
- Ao simular o enchimento asséptico com meio de cultura, o processo pode ser desafiado quant
- Em um lote estéril, nenhum frasco da amostra ensaiada apresentou crescimento. Esse fato de
- Após tratamento que inviabilizou uma população bacteriana, um ensaio de PCR ainda identifi
- Em qPCR para detecção de contaminante, uma amplificação específica no controle negativo de
- Em série histórica de processo, um resultado de teor mantém-se na especificação do produto
- Na produção de radiofármaco de meia-vida curta, a impossibilidade de concluir teste de est
- Um método microbiológico rápido pretende substituir ensaio tradicional. Para essa aplicaçã
- Em três lotes sucessivos, a biocarga de água aumenta continuamente sem ultrapassar o limit
```

## Pedido 11: Técnico e auxiliar em saúde bucal (`odontologia.tecnico-auxiliar-saude-bucal`, 59 tópicos de edital)

```
Matéria: odontologia.tecnico-auxiliar-saude-bucal (Técnico e auxiliar em saúde bucal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Legislação profissional, competências e supervisão": 17
- "2. Consultório, ergonomia, instrumentação e equipamentos": 17
- "3. Biossegurança, esterilização e resíduos odontológicos": 17
- "4. Materiais odontológicos e radiologia básica": 17
- "5. Promoção, prevenção, trabalho em equipe e registros": 16
- "6. Primeiros socorros e resposta a intercorrências": 16

Já existem (não repita):
- Em uma UBS, o TSB solicita supervisão de uma atividade clínica que foi designada para si. 
- Um edital pergunta qual atividade expressamente atribuída ao TSB, mas não ao ASB, pode oco
- Ao organizar um inquérito de saúde bucal, a coordenação pede que o TSB assuma a função de 
- Qual situação demonstra atuação do ASB dentro das competências legais e sem substituição d
- Sobre divulgação de serviços próprios por ASB e TSB, qual alternativa traduz a proibição e
- No atendimento a quatro mãos, qual medida é mais coerente com a ergonomia e a prevenção de
- Um equipamento de fotopolimerização passa por verificação programada de potência antes de 
- Após cair ao chão, uma pinça utilizada na bandeja deverá ser:
- A equipe observa que o motor do equipo apresenta ruído incomum e aquecimento. A providênci
- Uma auxiliar afirma que a mudança de cor da fita externa em todos os pacotes comprova este
- A que grupo de RSS pertence, em regra, uma agulha hipodérmica utilizada em atendimento odo
- Qual procedimento integra a etapa que deve ocorrer antes da esterilização de instrumental 
- Em qual momento a RDC nº 222/2018 orienta segregar o resíduo de serviço de saúde?
- Uma autoclave encerra um ciclo com pacotes visivelmente molhados. Qual a atitude segura?
- Em uma clínica, o ASB alega poder realizar tomada radiográfica por saber revelar filmes. Q
- Qual afirmativa sobre radiografia odontológica digital é correta?
- O ASB prepara uma mistura de gesso para confeccionar modelo odontológico. Qual conduta fav
- Para sistemas de radiologia odontológica intraoral, qual instrumento regulatório da Anvisa
- Uma equipe propõe escovação supervisionada, registro de participação e avaliação de result
- Em um registro de gestão, o ASB informa corretamente os procedimentos realizados e ajuda a
- Uma campanha apresenta muitos kits distribuídos e conclui, sem outros dados, que houve red
- Uma família relata dificuldades para comprar escovas e chegar à UBS. Qual postura educativ
- Durante atendimento odontológico, a paciente perde a consciência e não respira normalmente
- Um paciente apresenta convulsão no consultório. Qual medida inicial é segura?
- Paciente de atendimento odontológico apresenta dificuldade respiratória súbita e sinais co
- O registro no sistema profissional previsto em lei não dispensa a inscrição do TSB ou ASB 
- O ASB pode atuar autonomamente em procedimentos clínicos quando o cirurgião-dentista deleg
- Sob delegação do cirurgião-dentista, o TSB pode supervisionar o trabalho de ASB, sem adqui
- Como o ASB pode manipular materiais odontológicos, a lei autoriza que ele realize a inserç
- O trabalho a quatro mãos depende de comunicação entre profissionais e favorece a organizaç
- Manutenção corretiva é a atividade planejada para prevenir falhas antes de qualquer defeit
- Instrumental que perdeu condição asséptica durante a preparação deve passar por processame
- O uso de luvas por profissionais de saúde torna desnecessária a higienização das mãos entr
- A segregação dos resíduos odontológicos ocorre preferencialmente no local e no momento de 
- Todo material descartado em uma unidade odontológica deve ser classificado como resíduo do
- Na liberação de pacotes esterilizados, a integridade e a secagem das embalagens integram o
- A competência do ASB para processar filme radiográfico também lhe confere, por si só, comp
- Exames com sensores digitais continuam exigindo indicação apropriada, radioproteção e cuid
- A IN Anvisa nº 95/2021 trata especificamente da qualidade e segurança de sistemas de radio
- O TSB pode colaborar com levantamento epidemiológico, mas a lei exclui sua participação na
- A equipe de saúde bucal na APS pode desenvolver ações coletivas de escovação supervisionad
- A distribuição de kits de escovação, isoladamente, comprova que a incidência de cárie dent
- Na presença de paciente inconsciente sem respiração normal, a equipe deve reconhecer possí
- Durante convulsão, introduzir objeto rígido entre os dentes evita aspiração e protege o pa
- Uma emergência médica no consultório permite ao ASB prescrever medicamentos se o profissio
```

## Pedido 12: Reações químicas (`ciencias.reacoes-quimicas`, 58 tópicos de edital)

```
Matéria: ciencias.reacoes-quimicas (Reações químicas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Evidências, equações e conservação da massa": 31
- "2. Tipos de reação e leitura de padrões": 32
- "3. Oxirredução e combustão": 20
- "4. Aplicações, reescrita e cobrança do tema": 17

Já existem (não repita):
- Em um recipiente fechado, 12 g de carbono reagem completamente com 32 g de oxigênio e form
- Durante a mistura de duas soluções transparentes, aparece um sólido que se deposita no fun
- A equação 2H₂ + O₂ → 2H₂O representa qual padrão de reação pela organização de reagentes e
- Na equação CaCO₃(s) → CaO(s) + CO₂(g), qual classificação decorre diretamente do padrão es
- Considere Fe + CuSO₄ → FeSO₄ + Cu. Qual descrição combina o tipo de reação com o papel do 
- Em uma reação redox, a espécie X perde elétrons e passa de NOX +1 para +3. Qual processo o
- A reação HCl(aq) + NaOH(aq) → NaCl(aq) + H₂O(l) é frequentemente classificada como neutral
- Ao combinar duas soluções iônicas, forma-se o sólido insolúvel BaSO₄. Qual fenômeno e padr
- Na combustão completa do metano, CH₄ + 2O₂ → CO₂ + 2H₂O. Usando massas molares aproximadas
- Uma pastilha efervescente reage em água e libera bolhas. Qual conclusão respeita o limite 
- Uma amostra de 8,0 g reage em frasco aberto e forma 5,0 g de sólido, além de um gás que es
- Na reação 2Mg + O₂ → 2MgO, qual interpretação da conservação de átomos e do balanceamento 
- No íon sulfato SO₄²⁻, considere O com NOX −2. Qual NOX do enxofre satisfaz a carga total d
- Qual equação está balanceada para a formação de água a partir de hidrogênio e oxigênio?
- O monóxido de carbono reage com oxigênio: 2CO + O₂ → 2CO₂. Se 56 g de CO reagem completame
- No processo Fe₂O₃ + 3CO → 2Fe + 3CO₂, o ferro passa de NOX +3 a 0. Qual papel redox descre
- Uma combustão de combustível rico em carbono ocorre com suprimento insuficiente de oxigêni
- Uma espécie recebe elétrons de outra durante uma reação. Qual afirmação identifica correta
- Ao balancear uma equação, por que não se deve mudar o índice de uma fórmula química para i
- Uma amostra de 24 g reage em sistema fechado e forma 15 g de produto sólido e um único pro
- Na combustão completa do etano, 2C₂H₆ + 7O₂ → 4CO₂ + 6H₂O. Quantos mol de CO₂ são formados
- Uma solução incolor torna-se azul após contato com um sólido metálico; também se forma out
- Um metal M é oxidado a M²⁺ e íons X²⁺ são reduzidos a X(s). Qual relação redox e classific
- Um recipiente inicialmente contém 10 g de reagentes. Após reação em sistema fechado, a bal
- Qual afirmação diferencia corretamente decomposição, síntese e dupla troca sem usar apenas
- Em C(s) + O₂(g) → CO₂(g), quais elementos mudam de NOX e qual mudança sofre o oxigênio?
- Na decomposição 2KClO₃ → 2KCl + 3O₂, 245 g de reagente produzem 149 g de KCl. Qual massa d
- Uma equipe observa fumaça escura e calor em um incêndio com material orgânico e pouco supr
- Na reação 2Al + 3Cl₂ → 2AlCl₃, qual espécie é oxidada e qual é reduzida, a partir dos NOX?
- Uma reação em recipiente aberto consome 10 g de sólido e 20 g de gás do ar, formando 25 g 
- Em uma reação química comum realizada em recipiente fechado, a massa total dos reagentes é
- Em uma reação redox, uma espécie pode perder elétrons sem que outra espécie os receba.
- Um átomo de enxofre em substância simples tem NOX zero, ainda que o elemento também possa 
- A formação de bolhas em um líquido prova, em qualquer situação, que uma reação química pro
- Uma equação que mostra dois reagentes combinando-se para formar um produto segue o padrão 
- Na combustão completa de um hidrocarboneto com oxigênio suficiente, os produtos usuais são
- Quando um recipiente aberto perde massa durante a liberação de gás, isso demonstra que áto
- O agente redutor recebe elétrons e sofre redução, enquanto o agente oxidante doa elétrons 
- Se uma dupla troca em solução forma um sólido pouco solúvel, esse sólido pode aparecer com
- Ao balancear uma equação, os índices das fórmulas podem ser alterados livremente desde que
- Na reação 2Fe + 3Cl₂ → 2FeCl₃, cada Fe passa de NOX zero para +3 e perde três elétrons por
- Na equação N₂ + 3H₂ → 2NH₃, há 2 átomos de nitrogênio e 6 de hidrogênio em cada lado.
- Em uma combustão incompleta, a quantidade limitada de oxigênio garante que todo carbono te
- Uma reação que aumenta o NOX de uma espécie e diminui o NOX de outra não pode ser oxirredu
- Se 18 g de produto sólido e 14 g de produto gasoso são os únicos produtos de uma reação fe
```

## Pedido 13: Busca pessoal, busca domiciliar e abordagem (`processo-penal.busca-pessoal-abordagem`, 58 tópicos de edital)

```
Matéria: processo-penal.busca-pessoal-abordagem (Busca pessoal, busca domiciliar e abordagem)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Bases constitucionais, tipos de busca e finalidades probatórias": 14
- "2. Busca pessoal, fundada suspeita e jurisprudência dos tribunais superiores": 30
- "3. Inviolabilidade domiciliar, mandados, consentimento e horários": 30
- "4. Formalização da diligência, algemas, garantias e limites do uso da força": 13
- "5. Ilicitude probatória, perícias, cadeia de custódia e casos integrados": 13

Já existem (não repita):
- Uma diligência tem por objetivo encontrar arma escondida junto ao corpo de pessoa investig
- Após efetuar prisão válida, uma equipe precisa pesquisar se a pessoa traz consigo arma pro
- Um investigador pretende ingressar coercitivamente, à noite e sem mandado, em casa onde há
- Uma pessoa calma foi presa regularmente; não há reação, fuga provável ou risco à integrida
- Em crime que deixou vestígios materiais, o investigado confessou o ocorrido. Qual disposiç
- Uma equipe pretende buscar na residência armas e documentos ligados à infração investigada
- A diligência com mandado domiciliar encontra o objeto buscado, e os executores concluem o 
- O morador pergunta se a ordem judicial de busca domiciliar, sem outra exceção constitucion
- Uma infração deixa vestígios; não há perito oficial disponível. Qual previsão do art. 159,
- Uma revista pessoal é realizada após denúncia anônima que mencionava veículo, pessoa e hor
- Depois de entrar em imóvel sem mandado e sem consentimento, a equipe encontra substância i
- A equipe apresenta um mandado que nomeia apenas genericamente um conjunto de residências d
- Após diligência domiciliar com resultado negativo, o morador pede explicações sobre a razã
- Uma mulher é submetida a revista pessoal. Está disponível agente feminina, sem risco de re
- Uma equipe cumpre mandado domiciliar às 23h, sem anuência livre do morador nem indicação d
- Uma pessoa autoriza ingresso residencial depois de receber ameaça grave para abrir a porta
- Um vestígio foi recolhido em busca legal, mas depois passou por vários responsáveis sem re
- A parte pede a admissão de documento descoberto por busca pessoal feita apenas por impress
- A investigação apreende material numa revista ilegal, mas já existia procedimento independ
- Agentes são chamados para prestar socorro imediato a morador desmaiado em imóvel fechado d
- Durante busca domiciliar legitimamente autorizada, a autoridade procura arma expressamente
- Uma análise afirma que, em 2026, a simples afetação dos Temas 1438, 1439 e 1441 pelo STJ t
- Durante custódia, um detido oferece resistência ativa e tenta agredir terceiro; os agentes
- Uma busca domiciliar foi integralmente cumprida, mas um documento pertencente ao defensor 
- Depois de apreensão de objeto, o responsável o fotografa na posição original, recolhe-o e 
- A busca pessoal e a busca domiciliar são categorias distintas expressamente previstas no a
- Conforme o art. 244 do CPP, a busca pessoal depende de mandado judicial mesmo quando reali
- A Constituição admite ingresso em domicílio sem consentimento do morador para prestar soco
- A existência de mandado judicial para busca domiciliar autoriza seu cumprimento coercitivo
- A Súmula Vinculante 11 exige justificativa escrita da excepcionalidade do uso de algemas d
- Se uma infração deixou vestígios materiais e existe confissão formal do investigado, a con
- Uma informação anônima detalhada foi corroborada previamente por vigilância que confirmou 
- No RHC 158.580/BA, o encontro de droga durante revista fundada somente em impressão subjet
- O art. 243 do CPP exige que o mandado de busca indique tão precisamente quanto possível a 
- A assinatura de declaração de consentimento ao fim de uma entrada em casa, mesmo após amea
- Em diligência domiciliar, o CPP prevê auto circunstanciado assinado com duas testemunhas p
- O art. 249 do CPP impede que homem execute busca em mulher em qualquer hipótese, mesmo qua
- A Lei 13.869/2019, art. 22, § 1º, III, menciona cumprimento de mandado de busca domiciliar
- A afetação dos Temas Repetitivos 1438, 1439 e 1441 em 2026 já equivaleu, por si só, a deci
- No Tema 280 do STF, a licitude do ingresso forçado sem mandado, inclusive à noite, depende
- O art. 157 do CPP impõe inutilização automática de qualquer prova independente da busca il
- Em falta de perito oficial, o art. 159, § 1º, do CPP prevê exame por duas pessoas idôneas 
- A fixação de um vestígio por fotografia e sua posterior embalagem em lacre numerado são um
- A reprodução simulada do art. 7º do CPP pode verificar a possibilidade de uma infração ter
- O art. 22 da Lei 13.869/2019 transforma toda entrada domiciliar irregular em crime de abus
```

## Pedido 14: Arquitetura naval: nomenclatura, hidrostática, estabilidade e arranjo (`engenharia-naval.arquitetura-naval-estabilidade`, 58 tópicos de edital)

```
Matéria: engenharia-naval.arquitetura-naval-estabilidade (Arquitetura naval: nomenclatura, hidrostática, estabilidade e arranjo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Nomenclatura, dimensões e coeficientes de forma": 24
- "2. Flutuação, hidrostática, pesos e centros": 28
- "3. Estabilidade transversal, intacta e em avaria": 22
- "4. Linha de carga, arqueação, estrutura e arranjo": 26

Já existem (não repita):
- Olhando-se do navio para a proa, o lado direito da embarcação denomina-se boreste, e o esq
- A parte do casco que permanece imersa na água é denominada:
- Os conveses situados abaixo do convés principal do navio são chamados de cobertas.
- O tubo por onde passa a amarra da âncora, ligando o convés ao costado, chama-se:
- As quilhas de bolina são aletas instaladas ao longo do bojo, cuja principal finalidade é r
- A distância vertical medida da linha de base (quilha) até a linha d'água em que o navio fl
- Um navio com calado a ré maior que o calado a vante está:
- O coeficiente de bloco é a razão entre o volume deslocado e o produto do comprimento, da b
- O coeficiente prismático (Cp) relaciona-se com os coeficientes de bloco (Cb) e de seção me
- Um navio flutua em equilíbrio quando seu peso é igual ao empuxo e o centro de gravidade e 
- Um navio com deslocamento constante passa de água salgada (1,025 t/m³) para água doce (1,0
- Uma barcaça retangular com 50 m de comprimento, 10 m de boca e 2 m de calado flutua em águ
- A 1ª regra de Simpson para cálculo de áreas exige número par de intervalos entre as ordena
- Pela 1ª regra de Simpson, a área sob uma curva com ordenadas 2, 4 e 6 m, igualmente espaça
- Um navio tem área do plano de flutuação de 2.000 m² e flutua em água salgada de densidade 
- Um navio de 10.000 t de deslocamento tem um peso de 50 t movido transversalmente por 10 m 
- Uma carga suspensa pelo guindaste de bordo atua, para efeito de estabilidade, como se esti
- O porte bruto (deadweight) de um navio corresponde a:
- A altura metacêntrica transversal é dada por GM = KB + BM − KG, e o equilíbrio é estável, 
- Para uma barcaça retangular de boca B e calado T, o raio metacêntrico transversal BM vale:
- Uma barcaça retangular tem 12 m de boca, 4 m de calado e KG de 4 m. Sua altura metacêntric
- Para pequenos ângulos de inclinação, o braço de endireitamento pode ser aproximado por GZ 
- Na curva de estabilidade estática (GZ × ângulo de inclinação), a área sob a curva até dete
- O efeito de superfície livre de um tanque parcialmente cheio reduz a altura metacêntrica e
- Se um tanque retangular parcialmente cheio for dividido por duas anteparas longitudinais e
- Segundo os critérios gerais de estabilidade intacta do IS Code 2008, a altura metacêntrica
- Pelos critérios gerais do IS Code 2008, a área sob a curva de braços de endireitamento até
- Um navio com altura metacêntrica muito grande tende a ser "duro", com balanço rápido e des
- Na análise de estabilidade em avaria, a permeabilidade de um compartimento é:
- No método da perda de flutuabilidade, usado na análise de avaria, o deslocamento do navio 
- No teste de inclinação, desloca-se transversalmente um peso de 20 t por 10 m em um navio d
- Durante o teste de inclinação, os tanques devem estar, de preferência, parcialmente cheios
- A finalidade principal do teste de inclinação é determinar:
- A Convenção Internacional sobre Linhas de Carga, de 1966, estabelece a borda livre mínima 
- Na marca de borda livre, a linha de carga que fica mais alta no costado, permitindo o maio
- Segundo a Convenção Internacional sobre Arqueação de Navios, de 1969, a arqueação bruta é 
- Na Convenção de Linhas de Carga, os navios tipo A, projetados para transportar apenas carg
- As chapas transversais colocadas no fundo do navio, entre a quilha e o bojo, que ligam o c
- O sistema de cavernamento longitudinal, com muitos reforços longitudinais apoiados em giga
- No aparelho de fundeio, o equipamento de eixo horizontal que, por meio da coroa de Barboti
- Pela MARPOL, Anexo I, os petroleiros devem ter casco duplo e tanques de lastro segregado, 
- O peso leve de um navio corresponde ao navio completo, pronto para operar, sem carga, comb
```

## Pedido 15: Transportes e economia brasileira (`geografia.transportes-economia-brasil`, 57 tópicos de edital)

```
Matéria: geografia.transportes-economia-brasil (Transportes e economia brasileira)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Rede brasileira de transportes: modais, rodovias e evolução histórica": 29
- "2. Corredores logísticos, rede urbana e divisão inter-regional do trabalho": 18
- "3. Industrialização, desconcentração e reorganização do espaço brasileiro": 15
- "4. Espaço agrário, fronteiras agrícolas e complexos agroindustriais": 19
- "5. Comércio exterior, integração global e articulação entre produção e território": 19

Já existem (não repita):
- Um mapa destaca a BR-040 partindo de Brasília em direção ao Sudeste. Segundo a classificaç
- Uma empresa precisa entregar mercadorias em centenas de pequenos estabelecimentos afastado
- Um carregamento de soja sai de Mato Grosso pela BR-163 e chega a Miritituba, no Pará. Qual
- Um relatório apresenta a BR-101, BR-230 e BR-364. Qual associação corresponde à nomenclatu
- Uma pesquisa descreve a hierarquia urbana pela atração de serviços, decisões empresariais 
- Durante a industrialização por substituição de importações, o Brasil procurou ampliar a fa
- O nome MATOPIBA aparece em um mapa de avanço de lavouras de grãos no Cerrado. A quais unid
- Uma análise do comércio de bens de um país apresenta exportações de 120 unidades monetária
- Uma região concentra plantio de soja, armazéns, indústria de óleo vegetal, transportadoras
- Dois estudos atribuem participações diferentes ao transporte rodoviário brasileiro: um con
- Uma cooperativa de Goiás quer transportar grãos por uma ferrovia interiorana conectada a o
- A REGIC 2018 identificou quinze metrópoles brasileiras, cinco delas na Região Sudeste. O q
- Uma indústria transfere sua fábrica da metrópole paulista para um município do interior, m
- Uma análise da indústria na Amazônia apresenta o Polo Industrial de Manaus como unidade de
- O Censo Agropecuário de 2017 registrou cerca de 77% dos estabelecimentos como familiares, 
- Um documento confunde Mercosul e BRICS como se fossem blocos de integração regional de idê
- Uma companhia estrangeira adquire participação duradoura em fábrica no Brasil, participa d
- Uma rodovia federal foi concedida e recebeu investimentos, mas um trecho próximo apresenta
- Um estudo compara ferrovias relacionadas ao escoamento de café no começo do século XX com 
- Uma cooperativa de grãos considera usar rodovia até Miritituba e, em seguida, hidrovia e n
- Uma fábrica de máquinas em São Paulo fornece equipamentos a produtores de Mato Grosso, cuj
- Um texto atribui a expansão da soja no Cerrado apenas à fertilidade natural dos solos e à 
- Uma indústria nacional mantém projeto e pesquisa na metrópole, fabrica componentes em três
- A exportação brasileira de soja, petróleo e minério é descrita como se envolvesse somente 
- Uma política pretende elevar a competitividade de exportação de grãos do interior sem aban
- Ao consultar a classificação do DNIT, um estudante verifica que BR-101 e BR-163 são rodovi
- Uma planilha classifica a BR-364 como rodovia transversal por atravessar parte do territór
- Na análise de distribuição de bens a pequenos estabelecimentos, a flexibilidade e a capila
- Uma carga de soja segue por caminhão do Mato Grosso até Miritituba e depois por barcaça. E
- O Plano de Metas, entre 1956 e 1961, ajudou a impulsionar a indústria automobilística e as
- No Censo Agropecuário 2017, os estabelecimentos de agricultura familiar representavam cerc
- A pesquisa REGIC 2018 do IBGE, ao identificar metrópoles, capitais regionais e centros loc
- A Ferrovia Norte-Sul conecta o interior ao sistema portuário brasileiro sem precisar de ou
- Uma planta fabril deixa a capital paulista e se instala em Campinas, mantendo decisões emp
- A concentração industrial histórica no Sudeste demonstra que as regiões Norte, Nordeste, S
- A categoria MATOPIBA foi delimitada a partir de municípios de Maranhão, Tocantins, Piauí e
- O avanço agropecuário em áreas amazônicas pode ocorrer sem alterações nas relações fundiár
- Uma unidade familiar produz leite, fornece a uma cooperativa e integra a indústria de lati
- A participação rodoviária de 65% registrada em um levantamento histórico pode ser aplicada
- Os corredores que conectam produção centro-oestina a portos do Norte, Sul e Sudeste podem 
- A Zona Franca de Manaus exemplifica área de exportação extrativa, sem complexo fabril de e
- O emprego de pesquisa genética, correção de solos, mecanização e logística nas exportações
- A balança comercial de bens de um país corresponde à soma de investimentos estrangeiros di
- Uma montadora utiliza projeto externo, peças brasileiras e importadas e montagem em outro 
- Como o Brasil exporta mercadorias primárias, sua economia não mantém atividades industriai
```

## Pedido 16: Patologia (anatomia patológica) (`medicina.patologia-medicina-laboratorial`, 57 tópicos de edital)

```
Matéria: medicina.patologia-medicina-laboratorial (Patologia (anatomia patológica))
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos, adaptação e lesão celular": 17
- "Inflamação, imunopatologia, distúrbios vasculares e reparo": 17
- "Neoplasias, carcinogênese e diagnóstico oncológico": 17
- "Fluxo do laboratório, histotécnica, colorações e imuno-histoquímica": 17
- "Citopatologia esfoliativa e aspirativa": 16
- "Patologia por sistemas, raciocínio diagnóstico e necropsia clínica": 16

Já existem (não repita):
- Paciente com hipertensão crônica apresenta cardiomiócitos de maior volume, sem aumento rel
- Metaplasia é transformação adaptativa do tipo celular diferenciado e, por si só, não equiv
- Após oclusão arterial, uma área do córtex cerebral torna-se amolecida e perde sua arquitet
- A ocorrência de picnose, cariorrexe e cariólise descreve, respectivamente, condensação, fr
- Em uma valva cardíaca degenerada, observam-se depósitos de cálcio, apesar de níveis sérico
- Apoptose exige ruptura precoce da membrana plasmática e é acompanhada, em condições usuais
- Qual alternativa diferencia melhor isquemia tecidual de hipóxia isolada?
- Gangrena é denominação clinicomorfológica usada em necrose extensa, e não um padrão micros
- A histologia de uma lesão cutânea persistente mostra macrófagos epitelioides organizados, 
- Em geral, exsudato inflamatório é mais rico em proteínas do que transudato produzido princ
- Um paciente apresenta trombose venosa profunda após imobilização prolongada. Qual conjunto
- Granuloma e tecido de granulação são sinônimos histológicos, porque ambos são formados pre
- Em biópsia renal, a imunofluorescência demonstra depósitos granulares de imunoglobulinas e
- Na cicatrização por segunda intenção, a perda tecidual maior tende a exigir mais tecido de
- Na lâmina de uma área pulmonar, há dilatação vascular por impedimento da drenagem venosa, 
- Toda inflamação granulomatosa com necrose caseosa é diagnóstico histológico definitivo de 
- Uma biópsia de epitélio mostra atipias em toda a espessura, mas a membrana basal permanece
- Um tumor bem diferenciado e de baixo grau não pode apresentar metástases, pois grau histol
- Em peça oncológica, observa-se tecido tumoral exatamente no limite de ressecção avaliado h
- Carcinoma designa, em regra, neoplasia maligna epitelial, enquanto sarcoma designa neoplas
- Ao revisar um laudo, o patologista identifica Ki-67 elevado. Que interpretação é metodolog
- A extensão tumoral diretamente contígua ao órgão vizinho é, conceitualmente, metástase, ai
- Em um tumor maligno, a perda acentuada de diferenciação acompanha pleomorfismo nuclear, mi
- Um marcador preditivo ajuda a estimar possibilidade de resposta a determinado tratamento, 
- Um frasco com biópsia possui identificação de paciente divergente da requisição, embora o 
- Na rotina histológica com hematoxilina-eosina, a hematoxilina evidencia usualmente núcleos
- Na investigação de tumor indiferenciado, um painel de anticorpos é empregado em cortes tec
- Todo fragmento recebido em anatomia patológica deve ser colocado imediatamente em formalin
- Durante operação, o cirurgião solicita avaliação rápida de margem tumoral. Qual técnica po
- Um marcador imuno-histoquímico positivo, isoladamente e sem controles adequados, define se
- Em biópsia, o patologista deseja pesquisar fungos em tecido. Qual coloração especial é tra
- Na maioria das biópsias em parafina, falhas de fixação e autólise podem comprometer interp
- Em punção aspirativa de nódulo tireoidiano com padrão folicular, qual limitação deve ser d
- Amostra citológica classificada como insatisfatória ou não diagnóstica deve ser considerad
- Um laudo de citologia cervical descreve HSIL. Qual interpretação preserva a distinção entr
- A avaliação de esfregaço por Papanicolau costuma exigir fixação adequada e rápida para min
- Em derrame pleural com suspeita de malignidade, por que a preparação de bloco celular pode
- Uma citologia urinária negativa exclui com sensibilidade absoluta qualquer tumor urotelial
- Na análise comparativa entre citopatologia e histopatologia, qual informação é mais direta
- A revisão de casos discordantes entre citologia e histologia integra práticas de qualidade
- Na biópsia cutânea de uma dermatose eczematosa, há edema intercelular na epiderme, separan
- Na doença de lesões mínimas, a microscopia óptica renal pode ser quase normal, enquanto a 
- O exame de pulmão de paciente com insuficiência respiratória grave revela membranas hialin
- A cirrose hepática corresponde unicamente a depósito focal de colágeno, mesmo sem nódulos 
- Em biópsia cutânea, observa-se bolha intraepidérmica por perda de adesão entre queratinóci
- A necropsia clínica e a perícia médico-legal possuem finalidade e fluxo necessariamente id
- Na avaliação clínica de óbito, encontra-se embolia pulmonar extensa após trombose venosa p
- Na necropsia, autólise e putrefação podem simular ou obscurecer alterações teciduais; por 
```

## Pedido 17: Ministério Público do Trabalho no processo (`processo-trabalho.ministerio-publico-trabalho`, 57 tópicos de edital)

```
Matéria: processo-trabalho.ministerio-publico-trabalho (Ministério Público do Trabalho no processo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Missão constitucional e atuação do MPT como parte e fiscal da ordem jurídica": 17
- "2. Organização: Procurador-Geral, Colégio e Conselho Superior do MPT": 24
- "3. Câmara, Corregedoria, carreira, lotação e prerrogativas funcionais": 24
- "4. Ação civil pública, interesses transindividuais e intervenção nos processos coletivos": 23
- "5. Inquérito civil, recomendação, TAC e solução extrajudicial coletiva": 12

Já existem (não repita):
- Em fábrica com descumprimento reiterado de normas de proteção de centenas de trabalhadores
- Em reclamação trabalhista individual sem hipótese específica de intervenção obrigatória, h
- Na organização interna, um estudante inclui o cargo de Procurador-Geral do Trabalho como q
- Foi concluída lista tríplice para escolha do Procurador-Geral do Trabalho. Quem formalment
- Ao conferir a composição do Conselho Superior do MPT, qual distribuição corresponde litera
- Dois integrantes divergem sobre qual unidade do MPT deve receber investigação coletiva esp
- Em inspeção correcional, o assunto é a conduta funcional de integrantes do MPT e a realiza
- O MPT investiga descumprimentos coletivos e deseja celebrar ajuste que, se descumprido, pe
- Em processo no segundo grau trabalhista, a parte recorrente é uma pessoa jurídica de direi
- Num processo em que o MPT opinou como fiscal da ordem jurídica, é proferida decisão contrá
- Há vacância definitiva no exercício da chefia do MPT. O Vice-PGT havia sido designado para
- Uma deliberação ordinária do Conselho Superior termina empatada. Como o art. 97, § 1º, tra
- No preenchimento da Câmara de Coordenação e Revisão, o PGT e o Conselho Superior apresenta
- O Conselho Superior elaborou lista tríplice para Corregedor-Geral. Qual sequência de escol
- Uma associação legitimada ajuíza ação civil pública por lesão transindividual ligada a rel
- Diversos trabalhadores possuem diferenças remuneratórias divisíveis decorrentes de uma prá
- O MPT quer formalmente persuadir empresa a cessar exigência de exames discriminatórios, se
- Há greve em atividade essencial e indícios concretos de lesão ao interesse público. Qual c
- Em escolha do PGT, não há quantidade suficiente de pessoas elegíveis com mais de cinco ano
- O PGT pretende autorizar atuação excepcional de Procurador do Trabalho perante órgão judic
- Em razão de afastamento de Subprocurador-Geral do Trabalho por 45 dias, cogita-se convocar
- Em ação civil pública coletiva proposta por associação legitimada, esta abandona injustifi
- Durante inquérito civil, o MPT requisita exames técnicos para instruir possível ação colet
- O MPT celebra TAC para corrigir uma lesão coletiva, prevendo obrigações mensuráveis e comi
- A empresa assina TAC parcial sobre segurança de máquinas, mas a investigação envolve també
- Uma investigação aponta lesão coletiva a direitos sociais de trabalhadores, e o MPT consid
- Como o Ministério Público possui interesse na ordem jurídica, o art. 83, II, impõe sua man
- A carreira do MPT inicia no cargo de Procurador do Trabalho, alcança o cargo de Procurador
- O Procurador-Geral do Trabalho é nomeado pelo Conselho Superior, mediante votação entre to
- O Conselho Superior do MPT possui dez integrantes: o Procurador-Geral, o Vice-Procurador-G
- A Câmara de Coordenação e Revisão do MPT é responsável por fiscalizar a conduta funcional 
- O TAC previsto no art. 5º, § 6º, da Lei nº 7.347/1985 pode valer como título executivo ext
- O MPT, ao atuar exclusivamente como fiscal da ordem jurídica em processo trabalhista, está
- O Procurador-Geral do Trabalho exerce funções institucionais do MPT junto ao Plenário do T
- Em vacância do cargo de Procurador-Geral do Trabalho, o Vice-Procurador-Geral substitui au
- A Câmara de Coordenação e Revisão do MPT possui três integrantes, com um indicado pelo Pro
- Em ação civil pública trabalhista proposta por associação legitimada, a intervenção do Min
- A abertura de inquérito civil pode preceder ação civil pública, mas a Lei nº 7.347/1985 nã
- Em greve de atividade essencial com possibilidade concreta de lesão ao interesse público, 
- A escolha do Procurador-Geral do Trabalho parte de lista tríplice do Colégio e termina com
- O Corregedor-Geral do MPT tem direito a voto nas sessões do Conselho Superior, pois a fisc
- No caso de afastamento de Subprocurador-Geral do Trabalho por 45 dias, o art. 110 permite 
- Os interesses individuais homogêneos definidos no art. 81 do CDC são necessariamente indiv
- Uma recomendação ministerial é ato extrajudicial motivado e persuasivo e não se confunde c
- A assinatura de TAC parcial obriga o MPT a arquivar de imediato toda a investigação restan
```

## Pedido 18: Boas Práticas de Fabricação e validação (`farmacia.boas-praticas-fabricacao-validacao`, 57 tópicos de edital)

```
Matéria: farmacia.boas-praticas-fabricacao-validacao (Boas Práticas de Fabricação e validação)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos das BPF e sistema farmacêutico da qualidade": 18
- "2. Qualificação de instalações, equipamentos, sistemas e utilidades": 13
- "3. Validação dos processos produtivos e estado de controle": 19
- "4. Validação de limpeza e de procedimentos analíticos": 17
- "5. Plano mestre, protocolos, relatórios, revalidação e revisão periódica": 33

Já existem (não repita):
- Uma fabricante analisa o teor dos comprimidos, mas ignora desvios de higiene, treinamento 
- Uma equipe prepara documento contendo o modo aprovado de realizar a limpeza de equipamento
- Um novo misturador foi instalado e deve ser verificado quanto à conformidade de componente
- Uma estufa já instalada passa por testes de alarmes, temperatura mínima e máxima permitida
- Uma instalação passa em testes de montagem e operação, mas falta demonstrar funcionamento 
- Uma linha nova terá sua capacidade de produzir lotes uniformes demonstrada antes da produç
- Na validação da limpeza de uma linha compartilhada, os equipamentos estão visualmente bril
- Uma avaliação de método apresenta medidas muito próximas entre si, mas consistentemente af
- Na preparação de qualificação de equipamento, há estudo de aceitação na fábrica do fornece
- No controle da produção, decide-se usar validação concorrente antes de completar todo o pr
- O protocolo tradicional prevê três lotes consecutivos, mas o processo exibe variabilidade 
- No processamento de comprimidos, um aumento na velocidade de mistura influencia a distribu
- Na linha compartilhada, o protocolo de limpeza avalia retirada de resíduos de princípio at
- No estudo de limpeza de peças de aço e polímero, utiliza-se swab cujo rendimento é diferen
- Para avaliar linearidade de método quantitativo sujeito à RDC nº 166/2017, o protocolo apr
- Um método apresenta resultados semelhantes em laboratório de origem e de destino, sob cond
- Em uma linha que sofreu troca significativa de matérias-primas, o sistema deve verificar s
- Uma equipe conclui que a existência da expressão 'validação retrospectiva' no glossário da
- Um protocolo de validação previa teor entre 98 e 102%, mas o resultado foi 97,6%. Durante 
- Uma equipe executa limpeza de equipamento compartilhado e repete ciclos de lavagem e análi
- Um procedimento quantitativo de doseamento teve seis preparações independentes na concentr
- Uma planta pretende definir número universal e fixo de lotes, sem observar variabilidade d
- No método de teor, estudos mostram r = 0,995, mas resíduos apresentam padrão curvilíneo e 
- Uma empresa concluiu a validação de processo há dois anos; agora um parâmetro apresenta te
- Ao preparar a Revisão da Qualidade do Produto, a equipe seleciona apenas os laudos aprovad
- A revisão dos registros de produção, a investigação dos desvios e o monitoramento dos forn
- Após instalar uma nova prensa, testar o funcionamento no limite superior de velocidade e o
- A validação prospectiva de processo, definida no art. 3º, XXX, da IN nº 138/2022, ocorre a
- Em validação de limpeza de uma linha de comprimidos, a comprovação de superfícies visualme
- A qualificação de desempenho objetiva demonstrar que o equipamento ou sistema executa suas
- A RDC nº 166/2017 define precisão como proximidade ao valor verdadeiro e exatidão como dis
- O Plano Mestre de Validação pode reunir política, responsabilidades, situação de sistemas 
- Embora a IN nº 138/2022 apresente uma definição de validação retrospectiva em seu art. 3º,
- Em validação tradicional, o número de lotes e de amostras deve ser justificado por risco e
- Durante a limpeza de um misturador, repetidas lavagens seguidas de novas análises até a ob
- A análise da linearidade sujeita à RDC nº 166/2017 deve usar pelo menos cinco concentraçõe
- No estudo da precisão intermediária de método analítico, a RDC nº 166/2017 exige obrigator
- Uma mudança relevante no tamanho do lote pode requerer avaliação de impacto sobre parâmetr
- Uma empresa conclui testes FAT no fornecedor e afirma que isso dispensa automaticamente QI
- A Revisão da Qualidade do Produto normalmente deve ser realizada e documentada anualmente,
- No estudo de linearidade, a obtenção de r = 0,995 permite aprovar diretamente método cuja 
- Em estudo de limpeza, a taxa de recuperação da coleta por swab deve ser demonstrada nos ma
- Como a IN nº 138/2022 define validação retrospectiva, é possível adotá-la para substituir 
- A IN nº 138/2022 admite prosseguimento condicional à etapa seguinte de qualificação quando
- A verificação continuada de processos se encerra quando a validação inicial termina e não 
```

## Pedido 19: Supervisão, orientação e coordenação pedagógica (`educacao.gestao-supervisao-orientacao`, 57 tópicos de edital)

```
Matéria: educacao.gestao-supervisao-orientacao (Supervisão, orientação e coordenação pedagógica)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos históricos, organização profissional e legislação": 20
- "Coordenação e supervisão: acompanhamento do trabalho pedagógico": 20
- "Orientação educacional: desenvolvimento integral, escolhas e proteção": 20
- "Formação continuada na escola e conselho de classe": 20
- "Articulação escola–família–comunidade e permanência escolar": 20

Já existem (não repita):
- Qual ação corresponde mais diretamente ao trabalho típico da coordenação pedagógica?
- Uma supervisora acompanha reuniões de professores e deseja atuar sob enfoque formativo. Qu
- Na escola, a principal contribuição da orientação educacional é
- Segundo o art. 64 da LDB, a formação para supervisão e orientação educacional na educação 
- Qual é o uso pedagógico esperado do conselho de classe?
- Quanto à relação escola–família, a LDB estabelece que
- A respeito da Lei nº 5.564/1968 e do Decreto nº 72.846/1973, assinale a proposição juridic
- Um aluno procura a orientadora para discutir cursos e profissões, mas sua família exige qu
- Uma escola pretende organizar formação docente sobre práticas de leitura. Qual plano tem m
- Conforme a distinção entre arts. 8º e 9º do Decreto nº 72.846/1973, é correto afirmar que
- Durante conselho de classe, uma professora afirma que um estudante não aprende porque a fa
- Uma escola identifica faltas reiteradas e possível risco social. A orientação deve priorit
- A Orientação Pedagógica da Orientação Educacional (2019) referida nos editais da SEEDF é
- Em observação de aula, um supervisor pretende publicar uma classificação nominal dos docen
- Uma orientadora recebe relato de violência e o aluno solicita sigilo absoluto mesmo diante
- Um edital municipal cobra o papel do conselho de classe e não apresenta regimento local. Q
- À luz das atribuições constantes do Decreto nº 72.846/1973, assinale o exemplo que melhor 
- No planejamento de uma ação intersetorial, identificou-se risco de evasão ligado a transpo
- Em encontro de formação continuada, o coordenador apresenta um modelo de ensino como soluç
- Uma equipe afirma que já cumpriu seu dever com aluno em afastamento prolongado porque envi
- A coordenação pedagógica pode promover planejamento colaborativo sem retirar do professor 
- A supervisão pedagógica deve, em qualquer rede de ensino, limitar-se ao controle de presen
- O serviço de orientação educacional pode desenvolver ações individuais e coletivas voltada
- A orientação educacional é uma atividade exclusivamente clínica que substitui a psicoterap
- A formação continuada situada pode partir de problemas concretos da escola e incluir estud
- É correto tratar o conselho de classe como espaço destinado exclusivamente à divulgação pú
- O art. 64 da LDB prevê formação para supervisão e orientação educacional na educação básic
- O art. 8º do Decreto nº 72.846/1973 enumera somente atribuições comuns à direção financeir
- Na orientação vocacional, deve-se possibilitar reflexão sobre trajetórias e interesses, se
- Um coordenador pode determinar que a análise das práticas docentes seja substituída perman
- A articulação da escola com famílias e comunidade é incumbência institucional na LDB, e os
- A comunicação ao Conselho Tutelar, quando legalmente obrigatória, dispensa a escola de qua
- O documento intitulado Orientação Pedagógica da Orientação Educacional (2019) constitui re
- As normas de organização do conselho de classe são idênticas em todas as redes brasileiras
- A devolutiva de uma observação pedagógica é mais coerente com supervisão formativa quando 
- A condição de coordenar a orientação vocacional permite ao pedagogo aplicar indiscriminada
- Ao acolher suspeita de violência, a equipe pode ter de compartilhar informações estritamen
- Segundo o Decreto nº 72.846/1973, a integração escola–família–comunidade não integra as co
- Na análise de abandono escolar, investigar barreiras de transporte, acolhimento e relações
- Mesmo quando há risco atual para uma possível vítima de violência, a mediação de encontro 
```

## Pedido 20: Kernel Linux, módulos e drivers (`informatica.linux-kernel-drivers`, 57 tópicos de edital)

```
Matéria: informatica.linux-kernel-drivers (Kernel Linux, módulos e drivers)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Arquitetura do sistema operacional e compilação do kernel": 13
- "2. Módulos Linux: ativação, dependências e desenvolvimento": 16
- "3. Inicialização: bootloader, initramfs, rootfs e distribuição embarcada": 23
- "4. Drivers Linux: registro, concorrência e depuração": 13
- "5. Serviços, aplicativos e comparação com desenvolvimento Windows": 35

Já existem (não repita):
- Uma equipe deseja incluir suporte a um dispositivo diretamente na imagem do kernel Linux, 
- Após compilar novo kernel, o administrador executa `uname -r` antes de reiniciar a máquina
- Um arquivo `sensor.ko` depende de outro módulo já instalado. Qual comando normalmente deve
- Ao preparar uma imagem embarcada, uma pessoa identifica o componente responsável por selec
- O projeto precisa registrar no arquivo de configuração do kernel as opções escolhidas na f
- Um técnico executa `lsmod` e não encontra um driver que está funcionando no equipamento. Q
- Uma distribuição Linux mínima usa ambiente temporário para localizar e montar o armazename
- Ao investigar logs internos do Linux, alguém deseja consultar mensagens emitidas pelo núcl
- Um serviço Linux gerenciado por systemd deve ser iniciado imediatamente, sem alterar neces
- Uma configuração de kernel antiga deve ser aproveitada em versão nova, aceitando automatic
- Um desenvolvedor deseja compilar módulo externo contra o kernel em execução. Qual comando 
- Um pacote Buildroot foi construído para uma placa. Onde ficam tipicamente os artefatos fin
- Um módulo foi inserido com sucesso, porém um periférico USB correspondente não funciona. Q
- Um sistema embarcado constrói código para ARM64 em host x86-64. Qual combinação correspond
- Uma unidade systemd contém `After=rede.service`, sem `Wants=` nem `Requires=` para essa un
- Um fabricante pretende descrever recursos de uma placa ARM, como endereços e interrupções,
- Um driver Linux recebe evento em contexto atômico e precisa proteger estrutura compartilha
- O bootloader carregou o kernel, mas este não consegue acessar um dispositivo de armazename
- Um módulo externo foi preparado apenas com `make modules_prepare`; o núcleo de destino usa
- Um desenvolvedor ajustou opção de kernel como `n` e tenta usar `modprobe recurso`. O códig
- Um driver é removido enquanto uma workqueue ainda tem função agendada que acessará uma est
- Uma equipe testa aplicativo Linux ARM64 gerado com compilador cruzado, mas o executável de
- Uma falha ocorre apenas no uso de driver quando `probe` requisita IRQ não descrita correta
- Uma equipe compara imagem Linux feita por Buildroot com projeto que usa Yocto. Qual distin
- Um software Windows controla dispositivo por driver KMDF. Ao verificar sua instalação, o a
- Em um computador Linux, um programa de usuário pode acessar operações do sistema por chama
- Um administrador que executa `modinfo radio` insere automaticamente o módulo radio no kern
- Em um equipamento Linux embarcado, a existência de bootloader funcional dispensa a presenç
- Quando um driver é embutido no núcleo, sua funcionalidade pode estar ativa mesmo que o arq
- Um rootfs criado por Buildroot pode integrar aplicativos de usuário e arquivos de configur
- No Windows, o framework KMDF é destinado a drivers em modo usuário, enquanto UMDF identifi
- Um sistema que executa `make modules_install` já terá selecionado e iniciado, sem outras p
- Ao preparar kernel novo a partir de `.config` antigo, `make olddefconfig` pode aplicar val
- Um módulo compilado para kernel de arquitetura diferente tende a funcionar se o administra
- Um módulo Linux pode ser carregado com êxito e ainda não operar um dispositivo específico 
- Em uma unidade systemd, a indicação isolada `After=armazenamento.service` obriga o gerenci
- Em projeto Linux embarcado, um DTB pode descrever os periféricos e recursos de uma platafo
- Um desenvolvedor que cria driver Windows com WDF pode usar `DriverEntry` e callbacks de ev
- Um driver Linux que usa spinlock pode fazer espera bloqueante por I/O dentro da seção crít
- Um módulo Linux declarado por `module_init` pode ter inicialização executada na partida qu
- Quando `CONFIG_MODVERSIONS=y`, executar somente `make modules_prepare` na árvore do kernel
- Um executável ARM64 dinamicamente ligado pode falhar no rootfs da placa mesmo sendo produz
- O rastreamento de chamadas de sistema realizado por `strace` substitui integralmente o uso
- Em uma imagem Linux com sistema raiz armazenado em dispositivo cujo driver é módulo, a sim
- No Windows, criar o registro de um serviço com `CreateService` não equivale necessariament
```

## Pedido 21: Organização judiciária e regimentos de tribunais (`direito.organizacao-judiciaria-regimentos-tribunais`, 57 tópicos de edital)

```
Matéria: direito.organizacao-judiciaria-regimentos-tribunais (Organização judiciária e regimentos de tribunais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Constituição, estrutura do Judiciário e carreiras da União": 21
- "2. Superior Tribunal de Justiça: regimento e código de conduta": 19
- "3. Tribunal Superior do Trabalho: composição e órgãos": 16
- "4. Justiça Militar da União e Justiça do Distrito Federal": 33
- "5. Justiça Eleitoral regional, integração de regimentos e resolução de casos": 11

Já existem (não repita):
- Uma pessoa consulta o art. 96, I, a, da Constituição e conclui que os tribunais podem elab
- Um relatório afirma que a Primeira Seção do STJ reúne a Terceira e a Quarta Turmas e é esp
- Um processo coletivo trabalhista é examinado sob as regras internas do TST. A SDC é a estr
- Uma acusação contra militar que não é oficial deve ser julgada pelo Conselho Especial da J
- Uma atualização cadastral de servidores da Justiça do DF tem como referência organizatória
- Em um edital atualizado, o cargo de Técnico Judiciário pode exigir ensino médio como escol
- A estrutura regimental do STJ inclui três Seções e seis Turmas, sendo duas Turmas associad
- O fato de o RITST ter sido aprovado pela Resolução Administrativa nº 1.937/2017 permite de
- O art. 25 da Lei nº 8.457/1992 distingue instalação e funcionamento de Conselho com maiori
- O texto da Portaria GPR nº 1.107/2009, mesmo sem leitura das emendas posteriores, constitu
- Uma comissão administrativa pretende modificar por regimento a composição constitucional d
- Em estudo sobre o quadro permanente do Judiciário da União, é solicitado identificar as tr
- Um candidato utiliza apostila anterior a 2022 para afirmar que o Técnico Judiciário perman
- Ao distribuir um feito de direito privado no STJ, um servidor examina qual conjunto de Tur
- Em sessão de capacitação, afirma-se que Plenário e Corte Especial do STJ são órgãos com co
- Uma unidade do STJ revisa procedimentos éticos e pretende identificar o ato que estabelece
- Um conflito coletivo trabalhista precisa ser analisado segundo a divisão funcional dos órg
- Uma questão solicita a quantidade de integrantes do Tribunal Superior do Trabalho prevista
- Na organização do STM, quantos ministros são militares e quantos são civis, segundo a Cons
- Uma parte confunde o TJDFT com um tribunal regional federal porque a União organiza e mant
- Ao conferir a composição do TRE-RS, uma equipe deve aplicar a regra constitucional para os
- Um tribunal deseja atualizar sua regra sobre distribuição interna, enquanto outra proposta
- Uma servidora passa ao padrão seguinte dentro da mesma classe e outra muda para classe sub
- Uma questão de direito público foi vinculada às Turmas especializadas do STJ. Qual par int
- Um interessado sugere que a escolha do relator no STJ fique diretamente a cargo da parte, 
- Um processo individual trabalhista envolve divergência jurisprudencial e uma equipe cogita
- Ao estudar as funções do TST, uma pessoa classifica a Corregedoria-Geral como uma Turma re
- Ao interpretar uma regra histórica da Justiça Militar da União, um aluno aplica denominaçã
- Uma prova pergunta qual é a distribuição dos dez integrantes militares do STM pelas três F
- Uma equipe apresenta a Portaria GPR nº 1.107/2009 como a versão mais atual do Regimento In
- Na composição do TRE-RS, dois integrantes são escolhidos entre advogados com as qualificaç
- Um estudo do TRE-RS utiliza texto anterior às mudanças de 2025 sobre a escolha de juízes d
- Uma pessoa consulta o RISTJ para descobrir prazo específico de expediente alterado recente
- Uma pessoa considera que o Código de Conduta do STJ atribui à comissão ética poder para al
- Um magistrado atua em crime militar da União e uma questão sustenta que o simples vínculo 
- Uma unidade distribui gratificações considerando a Lei nº 15.292/2025. O AQ de doutorado p
- Ao organizar um processo, o relator do STJ considera que o art. 65-B torna automática a in
- Uma chefia orienta servidores com a Resolução STJ/GP nº 38/2023, considerando relevantes a
- No TRE-RS, uma ausência eventual de juiz titular impõe a convocação de substituto, mesmo q
- Na Justiça Militar da União, um Conselho Especial constituído para determinado processo é 
- Segundo a Lei nº 8.457/1992 atualizada, o juiz federal da Justiça Militar deixa de presidi
- A União tem atribuição constitucional de organizar e manter o Poder Judiciário do DF e Ter
- O art. 316 do RISTJ trata da designação do relator de recursos repetitivos e da participaç
- Em regra constitucional, os tribunais regionais eleitorais possuem sete membros, número di
- O Regimento Interno do TJDFT, por ter sido historicamente aprovado em 2009, pode afastar p
```

## Pedido 22: Legislação da Polícia Civil de Pernambuco (`legislacao.policia-civil-pernambuco`, 57 tópicos de edital)

```
Matéria: legislacao.policia-civil-pernambuco (Legislação da Polícia Civil de Pernambuco)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Segurança Pública na Constituição de Pernambuco": 20
- "2. Estatuto Policial (Lei nº 6.425/1972): regime, ingresso e vantagens": 20
- "3. Estatuto Policial: deveres, transgressões e penas": 20
- "4. Estatuto Policial: competência e processo disciplinar": 20
- "5. PCCV (LC nº 137/2008) e Delegado de Polícia (LC nº 317/2015)": 20

Já existem (não repita):
- Segundo a Constituição de Pernambuco, a Polícia Civil é dirigida por:
- Segundo a Constituição de Pernambuco, incumbem à Polícia Civil, privativamente e ressalvad
- O órgão responsável pelas perícias criminais e médico-legais, previsto na Constituição de 
- De acordo com a Constituição de Pernambuco, a direção do órgão setorial de identificação c
- Pela Constituição de Pernambuco, o cargo de Delegado de Polícia Civil é privativo de bacha
- Na Constituição de Pernambuco, cabe ao Governador do Estado, assessorado por um órgão cole
- Segundo a Constituição de Pernambuco, a Polícia Penal, vinculada ao órgão administrador do
- Pela Constituição de Pernambuco, os municípios são obrigados a constituir guardas municipa
- A Lei nº 6.425/1972, Estatuto dos Funcionários Policiais Civis de Pernambuco, dispõe que, 
- Pela Lei nº 6.425/1972, a função policial fundamenta-se na hierarquia e na disciplina e é 
- Quanto ao requisito de idade para inscrição no concurso, a Lei nº 6.425/1972 exige que o c
- Segundo a Lei nº 6.425/1972, a remoção por conveniência da disciplina deve ser expressamen
- Sobre a gratificação de função policial prevista na Lei nº 6.425/1972, assinale a alternat
- A gratificação de função policial é fixada entre os limites mínimo de 100% e máximo de 150
- Para tomar posse em cargo policial civil, a Lei nº 6.425/1972 exige, entre outros requisit
- São deveres do funcionário policial previstos no art. 30 da Lei nº 6.425/1972, EXCETO:
- Assinale a alternativa que apresenta apenas penas disciplinares previstas na Lei nº 6.425/
- Na Lei nº 6.425/1972, a pena de suspensão não excederá trinta dias e será aplicada em caso
- A detenção disciplinar prevista na Lei nº 6.425/1972:
- Por conveniência do serviço, a suspensão pode ser convertida em multa de cinquenta por cen
- Na Lei nº 6.425/1972, a repreensão é sempre aplicada por escrito e destina-se às faltas de
- Segundo a Lei nº 6.425/1972, a destituição de função tem por fundamento:
- Constitui hipótese de demissão prevista na Lei nº 6.425/1972:
- É causa agravante de falta disciplinar, na Lei nº 6.425/1972, o haver sido cometida em con
- O policial que, sem razão de serviço, mantém relações de amizade ou se exibe em público co
- Pela Lei nº 6.425/1972, o policial responde civil, penal e administrativamente, e as instâ
- Segundo a Lei nº 6.425/1972, compete privativamente ao Governador aplicar a pena de:
- Considere as competências disciplinares da Lei nº 6.425/1972 e associe cada autoridade ao 
- A suspensão preventiva prevista na Lei nº 6.425/1972 pode ser imposta por até trinta dias 
- De acordo com a Lei nº 6.425/1972, o processo disciplinar deve preceder a aplicação de:
- Pela Lei nº 6.425/1972, a sindicância é instaurada quando as irregularidades não se revela
- A Comissão Permanente de Disciplina, responsável pelo inquérito disciplinar na Lei nº 6.42
- O Conselho Superior de Polícia, previsto na Lei nº 6.425/1972, é órgão consultivo, normati
- No Plano de Cargos, Carreiras e Vencimentos da Polícia Civil de Pernambuco (LC nº 137/2008
- Pela LC nº 137/2008, o concurso para os cargos da Polícia Civil realiza-se em duas etapas,
- Sobre a estrutura do PCCV da Polícia Civil de Pernambuco, é correto afirmar que cada cargo
- Segundo a LC nº 137/2008, haverá progressão vertical automática por tempo de serviço para 
- NÃO concorre à progressão na carreira, segundo a LC nº 137/2008, o servidor:
- A LC nº 137/2008 veda a cessão de policiais civis, salvo para cargo em comissão compatível
- Segundo a Lei Complementar nº 317/2015, o ingresso no cargo de Delegado de Polícia de Pern
- A Lei Complementar nº 317/2015 estabelece que o Delegado de Polícia só poderá ser removido
- Contra decisão sobre seu enquadramento ou progressão no PCCV, o servidor pode apresentar p
```

## Pedido 23: Legislação mineral e regulação da mineração (`direito-minerario.legislacao-mineral-anm`, 56 tópicos de edital)

```
Matéria: direito-minerario.legislacao-mineral-anm (Legislação mineral e regulação da mineração)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Domínio mineral, ANM e governança regulatória": 22
- "2. Código de Mineração, pesquisa, licenciamento e lavra garimpeira": 19
- "3. Garimpeiros, cooperativas, gemas e metais preciosos": 19
- "4. CFEM e Código de Águas Minerais": 22
- "5. Licenciamento ambiental, recuperação e segurança de barragens": 18

Já existem (não repita):
- O proprietário de uma fazenda encontra ouro sob a superfície e afirma que, por ser dono do
- Uma pessoa estuda a lei que criou a ANM após a extinção do antigo DNPM. Como deve ser cara
- O interessado recebeu autorização de pesquisa mineral e já pretende iniciar lavra industri
- Uma empresa deseja extrair areia destinada à construção civil e busca a modalidade minerár
- Uma pessoa obtém permissão de lavra garimpeira, mas não possui licença ambiental. Alega qu
- Uma cooperativa de garimpeiros possui título válido e vende o ouro extraído ao primeiro ad
- Uma mineradora vende areia de uso imediato na construção civil. A receita bruta é R$ 100.0
- Um empreendimento capta e envasa água mineral para venda. Qual diploma normativo define ág
- Uma área com barragem de rejeitos está acima de comunidade densamente povoada, mas inspeçã
- Uma pessoa física possui PLG de 40 hectares e pretende obter outra, de 30 hectares, em 202
- Uma cooperativa recebeu PLG e pretende transferi-la mediante contrato assinado apenas por 
- Uma empresa comercializa minério de ferro em operação ordinária sujeita à CFEM. Consideran
- Uma empresa de extração industrial de calcário recebe cobrança da CFEM sobre venda e argum
- Uma comunidade situada em município sem extração mineral é atravessada por ferrovia usada 
- Uma empresa possui fonte com características físicas específicas e pretende conhecer a cla
- Um empreendedor pretende altear nova barragem de rejeitos pelo método a montante, alegando
- Uma empresa de mineração recebe da ANM requisição de documentos pertinentes a vistoria nas
- Após 2019, uma consulta pública sobre governança da ANM traz texto original de sua lei cri
- O titular de PLG foi intimado a apresentar projeto de pesquisa porque a ANM considerou ind
- Uma cooperativa com PLG afirma que a Constituição, art. 174, lhe garante acesso imediato a
- Em operação de venda de minério de ouro fora da PLG, a receita bruta foi R$ 500.000 e trib
- Uma exploração balneária utiliza água mineral e cobra preço discriminado por banho. Ao apu
- Um empreendimento já existente utiliza barragem de rejeitos construída a montante e não co
- Um relatório elaborado em outubro de 2026 afirma que a Resolução ANM nº 220/2025 já revogo
- Uma cooperativa recebe CFEM referente a exploração de ouro e requer que o município produt
- Em fazenda privada onde existe jazida de nióbio, a União é titular do recurso mineral, mas
- Uma empresa mineradora entende que a ANM é empresa pública federal e que sua Diretoria pos
- Uma pessoa requer título minerário para pesquisa de minério em área livre. O art. 11, a, d
- Um titular de permissão garimpeira extraindo diamantes alega que não precisa de título par
- Uma empresa vende ouro sob operação convencional de mineração. A alíquota geral da CFEM pr
- Uma fonte capta água mineral classificada pelo Decreto-Lei nº 7.841/1945. Como se trata de
- Uma empresa com título válido de exploração mineral ocasionou degradação ambiental na lavr
- Em 2026, uma pessoa física pode somar dez permissões de lavra garimpeira de cinquenta hect
- O titular de PLG intimado pela ANM a apresentar projeto de pesquisa deverá observar os nov
- Uma cooperativa pretende ceder sua PLG por decisão unilateral do presidente. Como a assemb
- Um município sem mina, mas atravessado por mineroduto utilizado no escoamento da produção,
- Uma jazida garimpável encontra-se em zona urbana. Se a empresa possui licença ambiental, o
- Uma operação de mineração sujeita a licenciamento ambiental é examinada conforme LC nº 140
- Um relatório de barragens elaborado em outubro de 2026 trata a Resolução ANM nº 220/2025 c
- A Lei nº 12.334/2010, art. 2º-A, proíbe construção ou alteamento de barragem de mineração 
- Após a atualização da Lei nº 13.540/2017, a empresa pode deduzir de modo geral fretes, seg
- Na disciplina do Código de Águas Minerais, classificação química das águas e classificação
- O regime geral de agências previsto na Lei nº 9.986/2000, com alterações de 2019, determin
- Uma cooperativa que reúne garimpeiros recebe prioridade constitucional na organização da a
- Uma pessoa que obteve autorização municipal de extração de areia para a construção pode di
```

## Pedido 24: Sentença e coisa julgada (`processo-trabalho.sentenca-coisa-julgada`, 56 tópicos de edital)

```
Matéria: processo-trabalho.sentenca-coisa-julgada (Sentença e coisa julgada)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Sentença, coisa julgada e liquidação": 67
- "2. Dissídios coletivos: extensão, cumprimento e revisão": 11
- "3. Execução trabalhista: títulos, modalidades e regimes especiais": 11
- "4. Citação, garantia, penhora e defesa na execução: como o tema costuma ser cobrado": 11

Já existem (não repita):
- Assinale a alternativa correta sobre as formas de liquidação previstas na CLT.
- Em processo trabalhista foi interposto recurso sem efeito suspensivo contra decisão conden
- Após requerimento de execução de obrigação em dinheiro, o executado recebe mandado de cita
- A execução foi garantida por depósito e não houve pagamento direto da obrigação. Em regra,
- Em execução trabalhista, o imóvel residencial da família foi indicado à penhora por dívida
- Uma sentença fixa prestações sucessivas, primeiro por período certo e, em outro pedido, po
- Uma reclamação trabalhista transitou em julgado, mas ambas as partes estão representadas p
- Em dissídio coletivo, apenas parte dos empregados de uma empresa participou da controvérsi
- Um sindicato pede a extensão de decisão coletiva a toda a categoria profissional compreend
- Após 14 meses de vigência de uma decisão normativa, fatos econômicos supervenientes tornar
- Na conta de liquidação, o credor incluiu adicional reconhecido em sentença para todo o con
- Uma servidora obteve sentença trabalhista contra ente público para receber quantia certa. 
- O executado oferece seguro-garantia e nomeia um imóvel, embora disponha de dinheiro em dep
- Durante a execução, é penhorado um equipamento pertencente a terceiro que não integra o pr
- No cumprimento de sentença que impõe pagamento à Fazenda Pública, a memória apresentada pe
- Empregados ajuízam reclamação contra sociedade cuja falência foi decretada. Qual é a divis
- Uma ex-empregada obtém sentença que liquida seu crédito contra empresa cuja falência já fo
- O título reconhece diferenças salariais, mas o número de dias efetivamente trabalhados em 
- Depois de garantida a execução, o exequente discorda de um critério de cálculo homologado 
- Um empregado doméstico pretende executar crédito trabalhista contra o único imóvel residen
- Uma decisão ainda está sujeita a recurso sem efeito suspensivo. O credor pede que bem penh
- Uma empresa não cumpre termo de conciliação firmado perante comissão de conciliação prévia
- Um empregador solicita extensão de decisão normativa para os empregados da mesma categoria
- O executado foi citado, deixou passar 48 horas sem pagar e também não ofereceu garantia. A
- Em diligência de penhora, o oficial encontra imóvel residencial da entidade familiar e, na
- Um terceiro possuidor de máquina penhorada apresenta embargos três dias depois da arremata
- Após arrematação e remição em uma execução trabalhista, o executado afirma que as custas d
- Após avaliação judicial, não houve licitante em leilão de bem penhorado, e o exequente não
- Uma condenação pecuniária trabalhista transitada em julgado foi imposta a um município. O 
- Em dissídio coletivo, apenas uma fração dos empregados da empresa participou da controvérs
- A sentença trabalhista ilíquida pode ser quantificada por cálculo, arbitramento ou artigos
- A CLT permite execução provisória até a penhora quando o recurso pendente não tem efeito s
- Após a citação para pagar ou garantir a execução em dinheiro, a CLT concede cinco dias par
- Garantida a execução ou penhorados os bens, o executado dispõe de cinco dias para embargos
- A impenhorabilidade do imóvel residencial da entidade familiar é inaplicável na Justiça do
- Se a quantificação depende de avaliação técnica pela natureza do objeto, o arbitramento po
- A revisão de decisão normativa pode ser requerida imediatamente após sua publicação, desde
- No cumprimento de sentença contra a Fazenda Pública, o credor deve apresentar demonstrativ
- Decretada a falência, o juízo falimentar substitui a Justiça do Trabalho na apuração do va
- Na redação atual da CLT, o juiz pode promover execução de ofício mesmo quando todas as par
- Se a liquidação exige prova de fato novo apenas para quantificar o direito reconhecido, es
- Qualquer pessoa que tenha interesse econômico indireto no resultado pode opor embargos de 
- A dívida trabalhista contra a Fazenda Pública, uma vez definitivamente reconhecida, é sati
- As custas da execução trabalhista são, em regra, responsabilidade do exequente e devem ser
- Na extensão de decisão coletiva dentro da empresa prevista no art. 868 da CLT, o tribunal 
```

## Pedido 25: Economia regional, urbana e do trabalho (`economia.economia-regional-politicas`, 56 tópicos de edital)

```
Matéria: economia.economia-regional-politicas (Economia regional, urbana e do trabalho)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da economia regional e teorias de localização": 20
- "Desenvolvimento regional e políticas territoriais": 20
- "Economia urbana, rede de cidades e organização espacial": 20
- "Indicadores socioeconômicos e avaliação territorial": 20
- "Mercado de trabalho regional e políticas de emprego": 20

Já existem (não repita):
- No modelo clássico de von Thünen, o custo de transportar produtos agrícolas até o mercado 
- A teoria de Weber explica a localização industrial exclusivamente pelo tamanho populaciona
- Um quociente locacional superior a um indica que determinado setor ocupa proporção maior d
- O FNO, o FNE e o FCO são fundos constitucionais de financiamento associados, respectivamen
- O PIB per capita municipal corresponde necessariamente ao rendimento médio recebido pelos 
- Na PNAD Contínua, a taxa de desocupação utiliza a força de trabalho, formada por pessoas o
- Um trabalhador que exerce atividade informal é necessariamente classificado como desocupad
- Na teoria dos lugares centrais, serviços de maior limiar de demanda e maior alcance tendem
- A abertura de uma rodovia entre um polo industrial e uma região periférica assegura descon
- O estudo REGIC do IBGE considera relações funcionais e fluxos entre cidades, de modo que h
- Dois municípios com índices de Gini idênticos têm obrigatoriamente a mesma renda média e a
- O saldo mensal de admissões menos desligamentos do Novo Caged é uma medida de fluxo líquid
- A política de intermediação de mão de obra do SINE pode reduzir assimetrias de informação 
- Uma área urbana com preços imobiliários menores necessariamente implica menor custo total 
- A interpretação de QL = 2 como evidência de que o setor regional exporta exatamente o dobr
- Quando uma política de qualificação é seguida de aumento do emprego dos egressos, já se id
- A diferença entre economias de localização e economias de urbanização está em que as prime
- Uma redução da taxa de desocupação pode ocorrer sem aumento do número de ocupados, caso pe
- O efeito competitivo da decomposição shift-share mede, por construção, o impacto causal de
- A aplicação de fundos regionais pode elevar a produção de uma cidade-polo sem reduzir desi
- Uma empresa precisa escolher local para sua fábrica considerando principalmente fretes de 
- Qual indicador é mais adequado para comparar a intensidade de desigualdade na distribuição
- Qual alternativa identifica corretamente as três dimensões do IDHM?
- O principal objetivo da intermediação de mão de obra realizada pelo SINE é:
- A REGIC, pesquisa do IBGE, é empregada sobretudo para:
- Em um município com 500 pessoas de 14 anos ou mais, há 280 ocupadas, 20 desocupadas e 200 
- Qual é a associação correta entre fundo constitucional e espaço geográfico central de atua
- Um saldo positivo de 150 vínculos no Novo Caged em certo período expressa, primariamente:
- Em determinada região, a indústria de calçados concentra 18% do emprego local; no país, o 
- Um governo implantará hospital regional de alta complexidade. Qual critério econômico-espa
- Uma cidade industrial passa a registrar congestionamento crescente e aluguéis empresariais
- Um consórcio quer medir a melhora efetiva do acesso a emprego após nova linha de ônibus, e
- Uma prefeitura anuncia programa de capacitação e deseja distinguir monitoramento de impact
- Em uma economia local, a principal indústria emprega trabalhadores residentes, mas compra 
- Qual distinção metodológica caracteriza corretamente a PNAD Contínua e o Novo Caged?
- A política de desenvolvimento regional inclui criar cadeias produtivas locais vinculadas a
- Duas regiões têm idêntico QL industrial. A região X usa maquinário sofisticado, enquanto Y
- Um município recebe trabalhadores de outras cidades diariamente e oferece serviços especia
- Uma obra logística reduz o custo de transportar bens do centro para a periferia. Apesar di
- Uma prefeitura paga subsídio por cada contratação formal de uma firma. Parte das vagas já 
- Uma taxa de desocupação cai de 12% para 9%, mas o nível de ocupação permanece estável e a 
- Considere a implantação de uma zona econômica especial em área de baixo desenvolvimento. Q
- Uma comparação mostra maior renda média nas cidades que receberam universidades. Por que i
- Para avaliar uma política que incentiva polo industrial, há crescimento do PIB regional e 
- Uma pesquisa pretende delimitar mercado de trabalho metropolitano em que muitos residentes
```

## Pedido 26: Realidade étnica, social, histórica, geográfica, cultural, política e econômica de Goiás (`goias.conhecimentos-estaduais`, 56 tópicos de edital)

```
Matéria: goias.conhecimentos-estaduais (Realidade étnica, social, histórica, geográfica, cultural, política e econômica de Goiás)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Do ouro à agropecuária: Goiás colonial e imperial": 20
- "2. República, Goiânia e transformações do século XX": 20
- "3. Geografia física e população": 20
- "4. Economia: agronegócio, mineração e indústria": 20
- "5. Cultura popular e patrimônio": 20

Já existem (não repita):
- A descoberta de ouro em Goiás, na década de 1720, está associada à bandeira de Bartolomeu 
- A primeira capital de Goiás, sede do governo desde o período colonial até a década de 1930
- Com o esgotamento do ouro de aluvião no fim do século XVIII, a economia goiana do século X
- A mineração goiana do século XVIII utilizou largamente mão de obra escravizada de origem a
- A comunidade Kalunga, considerada o maior território quilombola do Brasil, localiza-se no:
- Entre os povos indígenas que habitavam o território goiano e foram afetados pela colonizaç
- Na Primeira República, a política goiana foi marcada pelo coronelismo e pelo domínio de ol
- A chegada da estrada de ferro ao sul de Goiás, nas primeiras décadas do século XX, teve co
- O líder político responsável pela construção de Goiânia e pela transferência da capital, a
- A transferência oficial da capital de Goiás para Goiânia ocorreu em 1942, mesmo ano do lan
- A construção de Goiânia e a criação da Colônia Agrícola Nacional de Goiás, em Ceres, relac
- O estado do Tocantins foi criado pela Constituição Federal de 1988 a partir do desmembrame
- A construção de Brasília, inaugurada em 1960, teve sobre Goiás o efeito de:
- A revolta de Trombas e Formoso, nos anos 1950, foi um movimento de posseiros que resistira
- O acidente radiológico ocorrido em Goiânia em 1987, considerado o maior do Brasil, envolve
- O projeto urbanístico inicial de Goiânia foi elaborado pelo arquiteto e urbanista Attílio 
- O clima predominante em Goiás é o tropical semiúmido, com duas estações bem definidas: ver
- A vegetação original predominante no território goiano é:
- O território goiano é drenado pelas bacias hidrográficas:
- O rio Araguaia marca boa parte da divisa entre Goiás e o Mato Grosso.
- A Chapada dos Veadeiros, conhecida por cachoeiras, formações rochosas e parque nacional, l
- O Distrito Federal está encravado no território goiano, sendo envolvido por Goiás em quase
- Sobre a população goiana, segundo o Censo 2022, é correto afirmar que:
- O crescimento populacional de Goiás no século XX dependeu fortemente de migrações, atraída
- A região goiana que historicamente apresenta os piores indicadores sociais e menor dinamis
- A modernização da agricultura em Goiás, intensificada a partir dos anos 1970, apoiou-se na
- Entre as consequências sociais da modernização agrícola em Goiás, destaca-se:
- O município goiano conhecido pelo polo farmacêutico instalado em seu distrito agroindustri
- A região de Catalão destaca-se pela mineração de fosfato e nióbio e pela presença de indús
- A rodovia BR-153, que atravessa Goiás no sentido norte-sul, integra o eixo rodoviário conh
- A Ferrovia Norte-Sul atravessa o território goiano e conta com plataforma multimodal em An
- O centro histórico da Cidade de Goiás foi reconhecido pela UNESCO como Patrimônio Mundial.
- As Cavalhadas, encenação das batalhas entre cristãos e mouros durante a Festa do Divino Es
- A Procissão do Fogaréu, realizada na Semana Santa, é tradição característica da:
- A Romaria do Divino Pai Eterno, uma das maiores festas religiosas do Brasil, realiza-se no
- A poetisa da Cidade de Goiás, autora de Poemas dos Becos de Goiás e Estórias Mais, conheci
- O escritor regionalista Bernardo Élis foi o primeiro goiano eleito para a Academia Brasile
- Ao longo de sua história, Goiás teve como capitais, em ordem cronológica:
- O sudoeste goiano, com municípios como Rio Verde e Jataí, é uma das regiões mais dinâmicas
- Em meados do século XVIII, Goiás foi separada de São Paulo e passou a constituir capitania
```

## Pedido 27: Convenção sobre os Direitos das Pessoas com Deficiência (Decreto nº 6.949/2009) (`direito.convencao-direitos-pessoas-com-deficiencia`, 55 tópicos de edital)

```
Matéria: direito.convencao-direitos-pessoas-com-deficiencia (Convenção sobre os Direitos das Pessoas com Deficiência (Decreto nº 6.949/2009))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Incorporação, propósito, conceito e definições": 20
- "2. Princípios, obrigações gerais, igualdade e grupos específicos": 20
- "3. Acessibilidade, capacidade legal e direitos civis": 20
- "4. Direitos sociais, políticos e culturais": 20
- "5. Monitoramento, Comitê, cláusulas finais e Protocolo Facultativo": 20

Já existem (não repita):
- A Convenção sobre os Direitos das Pessoas com Deficiência e seu Protocolo Facultativo fora
- Segundo a Convenção, pessoas com deficiência são aquelas que têm impedimentos de longo pra
- Nos termos do art. 2 da Convenção, a discriminação por motivo de deficiência:
- Para a Convenção, adaptação razoável significa as modificações e os ajustes necessários e 
- A concepção de produtos, ambientes, programas e serviços a serem usados, na maior medida p
- Para a Convenção, o conceito de língua abrange as línguas faladas e de sinais e outras for
- NÃO consta entre os princípios gerais expressos no art. 3 da Convenção:
- De acordo com a Convenção, as medidas específicas necessárias para acelerar ou alcançar a 
- Quanto aos direitos econômicos, sociais e culturais, a Convenção prevê que cada Estado Par
- Pela Convenção, na elaboração de leis e políticas para aplicá-la, os Estados Partes realiz
- Sobre a aplicação da Convenção em relação a outras normas, é correto afirmar que:
- Em todas as ações relativas às crianças com deficiência, segundo a Convenção, deve receber
- A Convenção reconhece que mulheres e meninas com deficiência estão sujeitas a múltiplas fo
- Segundo o art. 9 da Convenção, as medidas de acessibilidade:
- Pelo art. 12 da Convenção, os Estados Partes reconhecerão que as pessoas com deficiência g
- As salvaguardas exigidas pela Convenção para as medidas relativas ao exercício da capacida
- Para assegurar o efetivo acesso à justiça, a Convenção determina a capacitação apropriada 
- Segundo o art. 14 da Convenção, quanto à liberdade e à segurança da pessoa com deficiência
- A Convenção admite que pessoas com deficiência sejam submetidas a experimentos médicos ou 
- Pelo direito à vida independente e à inclusão na comunidade (art. 19), a Convenção assegur
- Segundo a Convenção, em nenhum caso uma criança será separada dos pais sob alegação de def
- Quanto às informações destinadas ao público em geral, a Convenção determina que sejam forn
- Em relação à educação, a Convenção determina que os Estados Partes assegurem:
- A Convenção prevê a facilitação do aprendizado da língua de sinais e a promoção da identid
- No campo da saúde, a Convenção determina que os Estados Partes:
- A Convenção reconhece o direito das pessoas com deficiência ao trabalho de sua livre escol
- Entre as medidas previstas no art. 27 da Convenção para promover o trabalho das pessoas co
- Para garantir a livre expressão de vontade das pessoas com deficiência como eleitoras, a C
- Segundo a Convenção, os Estados Partes devem assegurar que a legislação de proteção dos di
- O direito a padrão de vida e proteção social adequados (art. 28) inclui assegurar às pesso
- Sobre a implementação e o monitoramento nacionais (art. 33), é correto afirmar que:
- O Comitê sobre os Direitos das Pessoas com Deficiência foi constituído inicialmente por 12
- Quanto aos relatórios dos Estados Partes, a Convenção estabelece que:
- A Convenção entrou em vigor no trigésimo dia após o depósito do vigésimo instrumento de ra
- O Protocolo Facultativo à Convenção reconhece a competência do Comitê para:
- Pelo Protocolo Facultativo, o Comitê considerará inadmissível a comunicação anônima, bem c
- No procedimento de investigação do Protocolo Facultativo, diante de informação confiável d
- Recebida uma comunicação admissível, o Comitê a levará confidencialmente ao conhecimento d
- Segundo a Convenção, o Protocolo Facultativo entra em vigor, sujeito à entrada em vigor da
- Pela Convenção, as crianças com deficiência serão registradas imediatamente após o nascime
```

## Pedido 28: Bioquímica clínica e urinálise (`laboratorio.bioquimica-clinica-urinalise`, 55 tópicos de edital)

```
Matéria: laboratorio.bioquimica-clinica-urinalise (Bioquímica clínica e urinálise)
Capítulos e quantas questões quero em cada um (total 100):
- "Fase pré-analítica e fundamentos da bioquímica laboratorial": 17
- "Metabolismo glicídico e perfil lipídico": 17
- "Função renal, proteínas e avaliação hepática": 17
- "Eletrólitos, equilíbrio ácido-base e marcadores cardíacos": 17
- "Urinálise: exame físico, químico e sedimentoscopia": 16
- "Espectrofotometria, automação e interpretação integrada": 16

Já existem (não repita):
- O prolongamento do contato entre células sanguíneas e plasma ou soro não separado pode pro
- Uma amostra colhida inadvertidamente em tubo com EDTA potássico pode apresentar potássio f
- Em adultos não gestantes, HbA1c igual ou superior a 6,5% é faixa diagnóstica de diabetes, 
- O colesterol não HDL resulta da subtração do HDL-colesterol ao colesterol total.
- A creatinina sérica, quando situada dentro do intervalo de referência, descarta a possibil
- Os campos físico, químico e microscópico compõem a urinálise, mas o exame isolado não subs
- A absorbância é diretamente proporcional à concentração do soluto em quaisquer concentraçõ
- A hemólise pode contribuir para aumento espúrio da atividade de LDH e da concentração de p
- Hemoglobina glicada expressa exatamente a glicemia da hora da coleta e, por isso, é apropr
- O clearance de creatinina de urina coletada durante tempo determinado pode superestimar a 
- A elevação isolada de AST indica, de maneira específica e definitiva, necrose de hepatócit
- Nitrito ausente na tira reagente exclui infecção urinária, pois todos os patógenos urinári
- Na acidose metabólica primária, costuma existir redução do bicarbonato e queda compensatór
- Uma troponina elevada acima do limite do ensaio, sem avaliação seriada nem sinais de isque
- A bilirrubina não conjugada, por estar fortemente ligada à albumina, não costuma aparecer 
- Em amostras com hiperlipidemia muito acentuada, o sódio pode parecer baixo pelo método de 
- Cilindros hemáticos urinários são formados exclusivamente nas vias urinárias baixas e afas
- Se a albumina plasmática estiver muito baixa, um hiato aniônico medido aparentemente norma
- O controle interno processado no analisador pode dispensar completamente a calibração, por
- Para urina coletada durante 24 horas, a perda de uma micção pode comprometer os resultados
- Um analista nota glicemia inesperadamente baixa em sangue que permaneceu horas sem separaç
- Na classificação laboratorial habitual de adultos não gestantes, qual resultado de glicemi
- Para dosar sódio e potássio de uma amostra, qual situação pré-analítica mais indica a nece
- O colesterol total de uma pessoa é 200 mg/dL e o HDL-colesterol, 55 mg/dL. Qual é seu cole
- Ao avaliar lesão hepatocelular, qual marcador é, em geral, mais associado a hepatócitos do
- Durante o exame microscópico de urina, qual achado sugere fortemente que o sangramento tem
- Qual grandeza corresponde a −log₁₀ da transmitância em espectrofotometria?
- Qual combinação de variáveis é usada diretamente para classificar um distúrbio ácido-base 
- O laboratório relata troponina acima do percentil 99, sem dados de ECG ou medidas seriadas
- Em uma pessoa com CT 230 mg/dL, HDL 50 mg/dL e triglicerídeos 150 mg/dL, calcule LDL-c pel
- Ao liberar resultado de creatinina para uma pessoa idosa com baixa massa muscular, qual me
- Uma amostra de urina tem reação positiva para sangue na fita, mas não apresenta hemácias i
- Em uma dosagem espectrofotométrica linear, padrão a 100 mg/dL apresenta A=0,20 e amostra A
- Em painel hepático, FA e GGT estão elevadas de modo concordante, com ALT discretamente aum
- No exame químico urinário, nitrito negativo, com sintomas de disúria e leucocitúria, deve 
- Gasometria apresenta pH 7,29, HCO₃⁻ 14 mmol/L e PaCO₂ 29 mmHg. Qual distúrbio primário é m
- Qual intervenção diminui principalmente o risco de queda espúria de glicose causada pelo m
- Que condição reduz especialmente a confiabilidade da HbA1c como estimativa da exposição gl
- A concentração urinária de creatinina é 120 mg/dL, a plasmática é 1,5 mg/dL e foram colhid
- Um adulto apresenta sódio 138, cloreto 100 e bicarbonato 14 mmol/L. Qual hiato aniônico se
- Em pessoa com triglicerídeos de 650 mg/dL, deseja-se determinar LDL-colesterol. Qual é o p
- Uma paciente tem pH 7,24, bicarbonato 12 mmol/L e PaCO₂ medida 40 mmHg. Pela fórmula de Wi
- A medida de sódio é baixa no ISE indireto, mas normal em método direto, em paciente com li
- Sobre interpretação de troponina cardíaca de alta sensibilidade, qual situação atende melh
- Uma amostra fotométrica apresentou turbidez intensa e foi processada mesmo após alarme de 
```

## Pedido 29: Constituição do Estado de São Paulo (`sao-paulo.constituicao-estadual`, 55 tópicos de edital)

```
Matéria: sao-paulo.constituicao-estadual (Constituição do Estado de São Paulo)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, Poder Legislativo e processo legislativo": 20
- "2. Fiscalização, Tribunal de Contas e Poder Executivo": 20
- "3. Judiciário, Justiça Militar e funções essenciais à Justiça": 20
- "4. Administração Pública e servidores civis e militares": 20
- "5. Segurança pública, tributação e finanças": 20

Já existem (não repita):
- Segundo a Constituição do Estado de São Paulo, incluem-se entre os bens do Estado, além do
- A Assembleia Legislativa do Estado de São Paulo reúne-se anualmente, independentemente de 
- As comissões parlamentares de inquérito da Assembleia Legislativa paulista são criadas a r
- Segundo a Constituição Paulista, pode propor emenda à Constituição do Estado:
- A proposta de emenda à Constituição do Estado de São Paulo é aprovada se obtiver, em dois 
- A iniciativa popular de projetos de lei no Estado de São Paulo exige subscrição de, no mín
- É de iniciativa privativa do Governador o projeto de lei que fixe ou modifique o efetivo d
- Recebido um projeto de lei aprovado pela Assembleia, o Governador do Estado de São Paulo, 
- Se a Assembleia não deliberar em 45 dias sobre projeto do Governador para o qual ele tenha
- É competência exclusiva da Assembleia Legislativa paulista destituir o Procurador-Geral de
- O Tribunal de Contas do Estado de São Paulo é composto por:
- Para ser nomeado Conselheiro do Tribunal de Contas do Estado de São Paulo, exige-se, entre
- O parecer prévio do Tribunal de Contas sobre as contas anuais prestadas pelo Governador do
- Em caso de impedimento simultâneo do Governador e do Vice-Governador do Estado de São Paul
- Sem licença da Assembleia Legislativa, o Governador e o Vice-Governador não podem ausentar
- Nas infrações penais comuns, o Governador do Estado de São Paulo é processado e julgado:
- O mandato do Governador do Estado de São Paulo é de quatro anos, permitida uma reeleição p
- Compete privativamente ao Governador do Estado, entre outras atribuições:
- O Tribunal de Justiça Militar do Estado de São Paulo compõe-se de sete juízes, divididos e
- Na Justiça Militar estadual paulista, compete ao juiz de Direito do juízo militar processa
- O oficial da Polícia Militar paulista só perderá o posto e a patente se for julgado indign
- São órgãos do Poder Judiciário do Estado de São Paulo, segundo a Constituição Estadual:
- O Procurador-Geral de Justiça do Estado de São Paulo é nomeado pelo Governador a partir de
- Segundo a Constituição Paulista, o Procurador-Geral do Estado:
- Segundo o art. 111 da Constituição do Estado de São Paulo, a administração pública obedece
- NÃO é princípio expressamente previsto no art. 111 da Constituição Paulista:
- O servidor público civil paulista tem direito ao adicional por tempo de serviço, concedido
- Pela Constituição Paulista, as certidões requeridas aos órgãos da administração para defes
- O servidor público estadual demitido por ato administrativo que for absolvido pela Justiça
- Segundo a Constituição do Estado de São Paulo, são servidores públicos militares estaduais
- O servidor público estadual tem assegurado o direito de remoção para igual cargo ou função
- Pela redação atual da Constituição Paulista, a segurança pública é exercida por meio dos s
- A Polícia Civil do Estado de São Paulo, dirigida por delegados de polícia de carreira bach
- O Comandante-Geral da Polícia Militar do Estado de São Paulo é nomeado pelo Governador:
- A Superintendência da Polícia Técnico-Científica do Estado de São Paulo é dirigida, altern
- Segundo a Constituição Paulista, o Estado entrega aos Municípios:
- A parcela do ICMS pertencente aos Municípios paulistas é distribuída, no mínimo, em 65% na
- Os recursos correspondentes às dotações orçamentárias do Legislativo, do Judiciário, do Mi
- As emendas individuais dos Deputados ao projeto de lei orçamentária paulista são aprovadas
- São impostos de competência do Estado de São Paulo, previstos na Constituição Estadual:
```

## Pedido 30: Ecocardiografia (`medicina.ecocardiografia`, 54 tópicos de edital)

```
Matéria: medicina.ecocardiografia (Ecocardiografia)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos físicos, aquisição transtorácica, transesofágica e técnicas avançadas": 17
- "Doppler e quantificação da função sistólica e da hemodinâmica": 17
- "Função diastólica, átrios, deformação miocárdica e sincronia": 17
- "Valvopatias, próteses e doenças da aorta por ecocardiografia": 17
- "Cardiomiopatias, pericárdio e ecocardiografia em urgências": 16
- "Cardiopatias congênitas do adulto, eco de estresse e comunicação do laudo": 16

Já existem (não repita):
- Na aquisição do ecocardiograma transtorácico de adulto com janela acústica profunda, qual 
- Um cardiologista deseja avaliar a veia cava inferior e o septo interatrial em um adulto em
- Em investigação de suspeita de trombo no apêndice atrial esquerdo com ETT inconclusivo, qu
- Durante ecocardiografia tridimensional multibatimento de um paciente com fibrilação atrial
- O desaparecimento aparente de uma porção do septo interatrial no corte bidimensional, sem 
- A avaliação prévia de doença esofágica e de risco de complicações relacionadas à sedação i
- O soro fisiológico agitado e os agentes comerciais intensificadores de ultrassom são tecni
- Em estenose aórtica com jato de alta velocidade, qual modalidade captura o pico sem aliasi
- Um jato possui velocidade máxima de 4,0 m/s, com velocidade proximal desprezível. Pela equ
- O método biplanar de Simpson forneceu volume diastólico final de 120 mL e volume sistólico
- Na avaliação sistólica de um paciente com suspeita de disfunção do VD, como deve ser inter
- O Doppler pulsado tem a vantagem de localizar a profundidade da amostra, mas pode apresent
- Uma fração de ejeção preservada elimina a possibilidade de alteração contrátil miocárdica 
- Se o Doppler contínuo não registra jato tricúspide mensurável, está demonstrado que a pres
- Na avaliação da função diastólica pelo ecocardiograma, qual conduta corresponde à atualiza
- Paciente tem e' mitral reduzido, suspeita de pressões elevadas e relação E/A aparentemente
- Em dois ecocardiogramas tecnicamente comparáveis, o GLS do VE mudou de −20% para −12%. Qua
- Indivíduo apresenta dispneia ao esforço, exame basal não conclusivo para aumento de pressõ
- A razão E/e' é estimativa indireta relacionada a pressões de enchimento e não pode ser tom
- Dilatação do átrio esquerdo, por si só, comprova elevação das pressões de enchimento do VE
- Índices ecocardiográficos isolados de dissincronia mecânica são suficientes para indicar t
- Qual conjunto costuma ser compatível com estenose aórtica grave no cenário adequado, deven
- O Doppler colorido exibe pequeno jato regurgitante mitral excêntrico junto à parede atrial
- Na equação da continuidade para área valvar aórtica, qual medida de entrada torna a estima
- Paciente portador de prótese aórtica tem gradiente Doppler aumentado. Qual interpretação i
- A velocidade máxima de um jato aórtico, pela Bernoulli simplificada, fornece o gradiente m
- Na insuficiência mitral secundária, o mecanismo pode envolver remodelamento ventricular ou
- Ecocardiograma transtorácico sem imagem de flap intimal em sua janela acessível exclui, de
- Paciente hipotenso apresenta derrame pericárdico e colapso diastólico de câmaras direitas 
- Qual padrão integrado favorece pericardite constritiva em vez de uma cardiomiopatia restri
- Em suspeita de cardiomiopatia hipertrófica, qual investigação ecocardiográfica é especialm
- No ecocardiograma focado de paciente em choque, observa-se VD dilatado e septo interventri
- Padrão de preservação apical relativa do strain longitudinal pode sugerir cardiopatia infi
- Derrame pericárdico pequeno de instalação muito rápida pode produzir repercussão hemodinâm
- Em ventilação mecânica, a interpretação das variações respiratórias da VCI e dos fluxos ca
- Adulto com comunicação interatrial e shunt predominante da esquerda para a direita apresen
- Qual sequela merece atenção especial no acompanhamento ecocardiográfico de adulto após cor
- Em eco de estresse indicado para isquemia miocárdica, qual achado é característico de resp
- Paciente com estenose aórtica de área reduzida, baixo gradiente e FEVE deprimida será subm
- O aparecimento de microbolhas de soro fisiológico agitado no coração esquerdo pode ajudar 
- Se o eco de estresse foi interrompido antes de obtenção de imagens interpretáveis e sem al
- Na padronização do laudo ecocardiográfico, é apropriado diferenciar medidas efetivamente o
```

## Pedido 31: Crimes militares em espécie (`direito-penal-militar.crimes-militares-especie`, 53 tópicos de edital)

```
Matéria: direito-penal-militar.crimes-militares-especie (Crimes militares em espécie)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Estrutura dos crimes militares e motim e revolta": 16
- "2. Desrespeito a superior e insubordinação": 16
- "3. Insubmissão, deserção e casos especiais": 30
- "4. Abandono de posto, embriaguez e crimes no serviço": 21
- "5. Peculato, concussão e distinções na Administração Militar": 17

Já existem (não repita):
- Durante deslocamento planejado, vários militares reúnem-se e decidem negar conjuntamente u
- Um grupo de militares realiza as condutas coletivas descritas no art. 149 e todos os parti
- Um subordinado dirige manifestação desrespeitosa a superior na presença de outro militar, 
- Um militar compreende ordem regular de superior referente à atividade de serviço e intenci
- Um convocado deixa de comparecer ao ato de incorporação no dia e prazo marcados, sem ter s
- Um militar incorporado deixa a unidade sem licença e permanece ausente por mais de oito di
- Durante plantão, a pessoa designada como sentinela abandona o ponto antes da rendição, sem
- Militar comparece já embriagado ao local onde deverá prestar serviço e está efetivamente n
- Responsável por guardar bens móveis em razão de comissão militar apropria-se deles e passa
- Agente em razão de sua função exige de particular pagamento indevido para deixar de exerce
- Vários militares combinam cometer motim, mas um participante denuncia o ajuste antes do in
- Subordinado desrespeita o comandante da unidade na presença de outro militar, preenchendo 
- Militar não se apresenta ao local designado após terminadas férias e deixa transcorrer o p
- Militar que praticou deserção do art. 187 apresenta-se voluntariamente no sexto dia contad
- Militar integrante da tripulação não se apresenta no momento da partida da aeronave e comp
- Militar responsável pelo controle de equipamentos, por negligência demonstrada, facilita s
- Um condenado por peculato exclusivamente culposo do art. 303, § 3º, repara o dano antes da
- Um oficial comparece embriagado ao serviço, mas não consumiu bebida naquele dia dentro de 
- Militar sem posse funcional de determinado equipamento utiliza acesso privilegiado de sua 
- Em situação de deserção especial, um oficial deixa de apresentar-se na partida da unidade 
- Após consumar deserção comum, militar apresenta-se voluntariamente no vigésimo dia contado
- Militar recebe, em razão do cargo, bem móvel particular custodiado pela unidade e passa a 
- Após condenação por peculato culposo, a reparação integral do dano ocorre somente depois d
- Dois ou mais militares apenas concertam a prática futura de motim, sem executar a reunião 
- Agente que exerce função militar exige pagamento indevido antes de assumir formalmente uma
- Militares que se reúnem para recusar conjuntamente ordem de superior, nas circunstâncias d
- Para caracterizar a revolta prevista no art. 149, parágrafo único, do CPM, é necessário qu
- No art. 160 do Código Penal Militar, a conduta descrita é desrespeitar superior diante de 
- O crime de insubmissão do art. 183 exige que a pessoa já tenha sido oficialmente incorpora
- A deserção comum do art. 187 requer ausência de militar sem licença por período superior a
- O abandono de posto do art. 195 somente se consuma depois de decorridos mais de oito dias 
- Apresentar-se embriagado para prestar serviço enquadra-se na segunda modalidade do art. 20
- O art. 303, caput, limita o objeto material do peculato militar a bens móveis públicos e e
- No peculato-furto do art. 303, § 2º, o agente pode não ter posse prévia do bem, desde que 
- Na concussão militar do art. 305, basta solicitar vantagem indevida sem caráter de exigênc
- Para o crime de deserção comum do art. 187, o art. 189, I, prevê redução da metade da pena
- O art. 190 trata a falta de apresentação no momento da partida de aeronave ou navio apenas
- O § 4º do art. 303 pode extinguir a punibilidade no peculato culposo se a reparação do dan
- O parágrafo único do art. 160 prevê aumento de um terço quando o militar desrespeitado é c
- O parágrafo único do art. 152 isenta de pena quem, tendo participado do ajuste para motim,
- No art. 190, § 3º, a deserção especial de oficial recebe aumento de um terço, enquanto o s
- O art. 303, § 1º, do CPM prevê aumento de um terço se o objeto da apropriação ou do desvio
- O militar que recebe, por erro de terceiro e no exercício da função, valor do qual se apro
- O art. 188, IV, do CPM inclui entre os casos assimilados à deserção a obtenção de exclusão
- A recusa individual a ordem regular de serviço dada por superior constitui motim do art. 1
```

## Pedido 32: Proteção jurídica da Amazônia (`direito-ambiental.protecao-juridica-amazonia`, 53 tópicos de edital)

```
Matéria: direito-ambiental.protecao-juridica-amazonia (Proteção jurídica da Amazônia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Base constitucional e conceito de Amazônia Legal": 25
- "2. Código Florestal na Amazônia Legal": 25
- "3. Florestas públicas, concessão florestal e Fundo Amazônia": 25
- "4. Regularização fundiária, tutela penal e competência": 25

Já existem (não repita):
- Segundo a Constituição Federal, a Floresta Amazônica brasileira é patrimônio nacional, e s
- NÃO integra o rol de patrimônio nacional do art. 225, § 4º, da Constituição Federal:
- Por ser patrimônio nacional, a Floresta Amazônica é bem da União, e os crimes ambientais n
- São indisponíveis as terras devolutas ou arrecadadas pelos Estados, por ações discriminató
- Preservar as florestas, a fauna e a flora é, segundo a Constituição Federal, competência:
- Pelo Código Florestal, integram a Amazônia Legal, entre outros, os Estados do Acre, Pará, 
- Segundo o Código Florestal, compõem a Amazônia Legal, além de sete Estados inteiros:
- A Amazônia Legal é um conceito político-administrativo que não coincide com o bioma Amazôn
- A Superintendência do Desenvolvimento da Amazônia (SUDAM), instituída pela Lei Complementa
- As terras indígenas, muito presentes na Amazônia, são bens da União destinadas à posse per
- No Código Florestal, o percentual mínimo de Reserva Legal de imóvel rural situado em área 
- Um imóvel rural situado em área de cerrado dentro da Amazônia Legal deve manter Reserva Le
- Um imóvel rural na Amazônia Legal tem 600 ha de floresta e 400 ha de campos gerais. A área
- Em caso de fracionamento do imóvel rural, inclusive para assentamentos de reforma agrária,
- Nos imóveis em área de floresta da Amazônia Legal, o poder público estadual, ouvido o Cons
- Quando o Município tiver mais de 50% de sua área ocupada por unidades de conservação de do
- Quando indicado pelo Zoneamento Ecológico-Econômico estadual, o poder público federal pode
- Após a implantação do Cadastro Ambiental Rural, a supressão de novas áreas de vegetação na
- Os proprietários que suprimiram vegetação nativa respeitando os percentuais de Reserva Leg
- Segundo o Código Florestal, NÃO estão sujeitos à constituição de Reserva Legal:
- A Lei 11.284/2006 dispõe sobre a gestão de florestas públicas para produção sustentável, i
- Para a Lei 11.284/2006, florestas públicas são:
- Segundo a Lei 11.284/2006, a gestão de florestas públicas para produção sustentável compre
- Antes da realização das concessões florestais, as florestas públicas ocupadas ou utilizada
- A concessão florestal, prevista na Lei 11.284/2006, é outorgada diretamente pelo poder con
- Sobre o Serviço Florestal Brasileiro, é correto afirmar que ele:
- O Fundo Amazônia é gerido pelo BNDES, que aplica as doações recebidas em espécie em projet
- Os recursos do Fundo Amazônia podem ser usados no desenvolvimento de sistemas de monitoram
- As aplicações do Fundo Amazônia são feitas na forma de empréstimos reembolsáveis a juros s
- NÃO figura entre as áreas que podem ser apoiadas pelo Fundo Amazônia, segundo o Decreto 6.
- A Lei 11.952/2009 dispõe sobre a regularização fundiária das ocupações em terras situadas 
- Na Lei 11.952/2009, a ocupação exercida somente por interposta pessoa é chamada de:
- Pelo art. 50-A da Lei 9.605/1998, desmatar, explorar economicamente ou degradar floresta, 
- Não é crime, nos termos do art. 50-A da Lei 9.605/1998, a conduta de desmatar floresta em 
- No crime do art. 50-A da Lei 9.605/1998, se a área explorada for superior a 1.000 hectares
- Destruir ou danificar floresta considerada de preservação permanente, mesmo que em formaçã
- Um crime de desmatamento ocorrido em propriedade particular na Amazônia, sem atingir bens 
- O Fundo Amazônia é administrado pelo seguinte órgão ou entidade:
- Assinale a alternativa que relaciona corretamente a norma e seu objeto.
- A Lei 11.952/2009 permite que uma mesma pessoa natural ou jurídica seja beneficiada com a 
```

## Pedido 33: Saúde suplementar: planos de saúde, ANS, Lei 9.656/1998 e auditoria médica (`saude-publica.saude-suplementar-planos-auditoria`, 52 tópicos de edital)

```
Matéria: saude-publica.saude-suplementar-planos-auditoria (Saúde suplementar: planos de saúde, ANS, Lei 9.656/1998 e auditoria médica)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Mercado, operadoras e regulação": 25
- "2. Direitos do beneficiário: carências, cobertura e prazos": 25
- "3. Tabelas, faturamento e auditoria médica": 25
- "4. Gestão, tecnologias e interfaces com o setor público": 25

Já existem (não repita):
- O beneficiário de plano privado de saúde mantém o direito de ser atendido pelo SUS.
- A Agência Nacional de Saúde Suplementar (ANS) foi criada pela:
- A ANS é autarquia sob regime especial vinculada ao Ministério da Saúde, com competência pa
- A modalidade de operadora em que a própria empresa ou entidade administra o plano para seu
- A UNIDAS é a entidade que representa e apoia as instituições de autogestão em saúde.
- A administradora de benefícios contrata planos coletivos em nome de pessoas jurídicas e as
- O plano coletivo contratado por sindicato ou associação profissional em favor de seus memb
- O plano-referência compreende assistência ambulatorial e hospitalar com obstetrícia, com a
- Segundo a Lei 9.656/1998, o prazo máximo de carência para parto a termo é de:
- A carência máxima para atendimentos de urgência e emergência é de 24 horas.
- Para doença preexistente declarada pelo beneficiário, a operadora pode aplicar cobertura p
- A operadora pode recusar o ingresso no plano de pessoa portadora de doença preexistente ou
- Após a Lei 14.454/2022, a cobertura de tratamento não incluído no rol da ANS pode ser exig
- O rol de procedimentos da ANS é a referência básica de cobertura obrigatória e é atualizad
- Pelas regras de garantia de atendimento da ANS, consultas em pediatria, clínica médica, ci
- Nos planos individuais e familiares, o percentual máximo de reajuste anual é definido pela
- O ressarcimento ao SUS ocorre quando:
- Segundo entendimento do STJ, o Código de Defesa do Consumidor não se aplica aos contratos 
- A portabilidade de carências permite ao beneficiário mudar de plano sem cumprir novas carê
- A classificação usada como referência de honorários médicos, elaborada pela AMB e entidade
- O padrão TISS refere-se à troca eletrônica de informações entre operadoras e prestadores, 
- As publicações Brasíndice e Simpro são usadas na saúde suplementar como referência de:
- A auditoria que acompanha a internação enquanto ela ocorre, geralmente no próprio hospital
- Glosa é a recusa total ou parcial do pagamento de um item da conta apresentada pelo presta
- A auditoria analítica caracteriza-se por:
- O médico auditor pode modificar livremente a prescrição do médico assistente, sem necessid
- O modelo de remuneração em que o prestador recebe um valor fixo por beneficiário vinculado
- Havendo divergência entre o profissional assistente e a operadora sobre a indicação de um 
- A sinistralidade de uma operadora é calculada pela razão entre:
- Na análise de custo-utilidade, os resultados das alternativas são medidos em anos de vida 
- Quando duas alternativas terapêuticas produzem resultados equivalentes e a escolha recai s
- No SUS, a incorporação de novas tecnologias em saúde é assessorada pela:
- A Anvisa é o órgão responsável pelo registro de medicamentos e materiais, o que repercute 
- Os NAT-Jus são núcleos de apoio técnico que oferecem subsídios científicos aos magistrados
- A sigla RENAME, usada na assistência farmacêutica, designa a:
- Programas de promoção da saúde e de gerenciamento de doenças crônicas são estratégias de g
- A Lei 14.454/2022 tornou obrigatória a cobertura de qualquer tratamento prescrito, mesmo s
- Entre as segmentações assistenciais dos planos de saúde NÃO se inclui:
- Na cobertura parcial temporária, durante o prazo de suspensão, ficam fora da cobertura, pa
- A coparticipação é o valor pago pelo próprio beneficiário, além da mensalidade, a cada uti
```

## Pedido 34: Farmácia hospitalar e farmácia clínica (`farmacia.farmacia-hospitalar-clinica`, 52 tópicos de edital)

```
Matéria: farmacia.farmacia-hospitalar-clinica (Farmácia hospitalar e farmácia clínica)
Capítulos e quantas questões quero em cada um (total 100):
- "Organização da farmácia hospitalar, governança e padronização": 20
- "Distribuição de medicamentos e gestão segura dos estoques": 20
- "Farmacotécnica hospitalar, unitarização e nutrição parenteral": 20
- "Informação sobre medicamentos e cuidado farmacêutico clínico": 20
- "Farmacovigilância, farmacoepidemiologia e garantia da qualidade": 20

Já existem (não repita):
- Ao reestruturar a farmácia de um hospital, qual proposta traduz melhor sua função instituc
- A farmácia hospitalar reúne responsabilidades administrativas, logísticas e clínico-assist
- Uma CFT compara dois antibióticos com preços diferentes, mas perfis de segurança e resulta
- Para padronização hospitalar, a CFT deve optar obrigatoriamente pelo produto que ofereça o
- Um hospital instala farmácia satélite junto à UTI. A conduta organizacional mais adequada 
- Uma farmácia satélite pode agilizar o atendimento de unidades críticas, mas permanece suje
- Após inclusão de um novo medicamento na lista hospitalar, a equipe detecta duplicidades de
- A aprovação de um medicamento pela CFT elimina a necessidade de acompanhar sua utilização 
- Para verificar se a inclusão de fármaco restrito está funcionando, qual indicador traz inf
- A farmácia entrega caixas de analgésicos para uma enfermaria, sem identificação por pacien
- Na distribuição individualizada, os medicamentos são separados segundo a prescrição de cad
- Qual alternativa diferencia corretamente distribuição individualizada de dose unitária?
- A simples reembalagem de comprimidos em doses únicas significa que o hospital implantou um
- O consumo de um medicamento é 12 frascos/dia, o prazo de reposição é de 7 dias e a seguran
- A classificação ABC, tomada isoladamente, não informa a criticidade assistencial de um med
- Dois lotes do mesmo medicamento estão íntegros: o primeiro recebido vence em oito meses e 
- Em medicamentos com diferentes vencimentos, PEPS e PVPS são estratégias conceitualmente id
- Uma curva ABC aponta antibiótico de baixo custo como item C, mas o hospital não pode ficar
- Uma prescrição prevê 45 mg de medicamento disponível como solução de 15 mg/mL. Qual volume
- A preparação de nutrição parenteral requer avaliação farmacêutica da prescrição e controle
- Durante avaliação de NP com cálcio e fosfato, a principal preocupação físico-química é:
- A ausência de turvação em uma bolsa de nutrição parenteral comprova a impossibilidade de p
- Na liberação de uma preparação estéril, qual conjunto de medidas reflete o controle de qua
- A dose de 30 mg pode ser obtida com 3 mL de uma solução a 10 mg/mL, desde que a formulação
- A equipe sugere acrescentar medicamento não previsto a uma bolsa de nutrição parenteral já
- Ao reconstituir um medicamento hospitalar, deve-se manter automaticamente a data de valida
- Ao preparar medicamento antineoplásico perigoso, qual princípio deve nortear a seleção de 
- Um profissional solicita ao CIM evidência sobre administrar comprimido de liberação prolon
- Um serviço de informação sobre medicamentos deve registrar a pergunta recebida, avaliar a 
- Durante admissão hospitalar, paciente relata uso contínuo de anticonvulsivante ausente da 
- Conciliação medicamentosa consiste em transcrever, sem verificação, a lista de medicamento
- Qual situação corresponde principalmente a uma interação farmacodinâmica?
- Suspensão deliberada e documentada de um medicamento domiciliar na admissão pode configura
- A equipe recebe alerta informatizado de interação moderada em paciente estável. Qual ação 
- A identificação eletrônica de uma possível interação medicamentosa torna desnecessárias a 
- Ao preparar orientação de alta, qual atividade representa continuidade do cuidado farmacêu
- Uma enfermeira suspeita de reação medicamentosa grave, mas a relação causal ainda é incert
- Uma suspeita de evento adverso relacionado a medicamento pode ser notificada ao VigiMed an
- Um hospital passou de 12 para 30 notificações após capacitação em segurança. Qual inferênc
- A quantidade de relatos espontâneos de reações adversas, dividida pelo total de leitos do 
- Em estudo hospitalar, usuários do fármaco X têm maior frequência de lesão renal aguda, mas
- A farmacoepidemiologia pode estudar padrões de uso de medicamentos e associação entre expo
- Um setor recebe frasco com lacre violado, sem que o medicamento tenha sido administrado. Q
- Um aumento nas notificações após treinamento da equipe demonstra, por si só, que houve aum
- Em auditoria, um hospital pretende comparar consumo de antimicrobianos entre setores de ta
```

## Pedido 35: Estatuto dos Servidores Públicos Civis de Santa Catarina (Lei 6.745/1985) (`santa-catarina.estatuto-servidores-lei-6745`, 52 tópicos de edital)

```
Matéria: santa-catarina.estatuto-servidores-lei-6745 (Estatuto dos Servidores Públicos Civis de Santa Catarina (Lei 6.745/1985))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Abrangência, ingresso e estágio probatório": 25
- "2. Vida funcional: exercício, remoção, jornada e movimentação": 25
- "3. Direitos: tempo de serviço, férias, licenças e remuneração": 25
- "4. Petição, deveres, regime disciplinar e reingresso": 25

Já existem (não repita):
- O Estatuto da Lei nº 6.745/1985 aplica-se aos servidores públicos civis dos três Poderes d
- Segundo o Estatuto catarinense, o ato de designação para função de confiança vigora a part
- Pelo Estatuto catarinense, o concurso público será precedido de três publicações de edital
- A posse no cargo deve ocorrer no prazo de:
- Pela redação atual do Estatuto catarinense, o estágio probatório tem duração de três anos 
- A contagem do período de estágio probatório é suspensa quando o servidor se afasta a qualq
- Segundo o Estatuto catarinense, a estabilidade diz respeito ao cargo ocupado pelo servidor
- O período de trânsito concedido ao funcionário para mudança de local de trabalho e residên
- O funcionário em estágio probatório pode ser removido a pedido para outra localidade por m
- Na remoção por interesse do serviço fundada na necessidade de pessoal, a escolha recairá p
- Segundo o Estatuto catarinense, a prestação de serviço extraordinário não pode ultrapassar
- Considera-se trabalho noturno, pelo Estatuto catarinense, o prestado entre 22 horas e 6 ho
- As faltas ao serviço por motivos particulares, segundo o Estatuto catarinense:
- A readaptação funcional, prevista no Estatuto catarinense, implica mudança de cargo e pode
- O deslocamento motivado de cargo de provimento efetivo, ocupado ou vago, para outro órgão 
- Pela redação dada em 2021, as férias do servidor catarinense podem ser gozadas em até três
- Os períodos de férias não usufruídos em atividade pelo servidor catarinense são:
- A licença para tratamento de saúde, com remuneração e mediante inspeção do órgão médico of
- Na licença por motivo de doença em pessoa da família, a remuneração é integral até três me
- Pela redação vigente, a licença para tratamento de interesses particulares pode ser conced
- Após cada quinquênio de serviço público estadual, o servidor efetivo faz jus a três meses 
- A licença com remuneração para o funcionário que se candidatar a cargo eletivo vai:
- A licença por mudança compulsória de domicílio do cônjuge ou companheiro é concedida ao fu
- As gratificações previstas no art. 85 do Estatuto catarinense, como a de função de confian
- A gratificação pela prestação de serviços em locais insalubres ou com risco de vida é conc
- O funcionário que comparecer ao serviço com atraso máximo de até 30 minutos ou se retirar 
- As reposições e indenizações devidas pelo funcionário à Fazenda Pública Estadual são desco
- Segundo o Estatuto catarinense, o requerimento ou representação do funcionário deve ser so
- O direito de pleitear na esfera administrativa prescreve em cinco anos quanto aos atos de 
- São penas disciplinares previstas no Estatuto dos Servidores de Santa Catarina:
- Pelo Estatuto catarinense, considera-se inassiduidade intermitente a ausência ao serviço, 
- A demissão qualificada incompatibiliza o ex-funcionário para o exercício de cargo ou empre
- No Estatuto catarinense, a indisciplina ou insubordinação e a impontualidade são infrações
- A ação disciplinar prescreve em dois anos para os fatos punidos com repreensão, suspensão 
- São circunstâncias atenuantes da pena disciplinar no Estatuto catarinense, entre outras:
- Verificada acumulação proibida de cargos e provada a boa-fé, o funcionário será obrigado a
- A prisão administrativa do responsável por dinheiros e valores da Fazenda Estadual, em cas
- O reingresso no serviço público do funcionário aposentado, quando insubsistentes os motivo
- Os prazos previstos no Estatuto catarinense são contados em dias úteis, computando-se o di
- O salário-família previsto no Estatuto catarinense corresponde, por dependente, a:
```

## Pedido 36: Oscilações simples, amortecidas e forçadas (`ciencias.oscilacoes-simples-amortecidas-forcadas`, 51 tópicos de edital)

```
Matéria: ciencias.oscilacoes-simples-amortecidas-forcadas (Oscilações simples, amortecidas e forçadas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Movimento harmônico simples, energia e osciladores ideais": 27
- "2. Vibrações mecânicas, amortecimento, excitação e ressonância": 35
- "3. Ondas elásticas, acústica e efeito Doppler": 12
- "4. Fluidos em repouso e movimento: pressão, Bernoulli e perdas": 14
- "5. Calor, dilatação, termodinâmica, combustão e concreto sob fogo": 12

Já existem (não repita):
- Uma massa de 2 kg presa a uma mola horizontal ideal de 200 N/m oscila sem atrito. Qual é a
- Uma massa em MHS atinge a posição de maior alongamento positivo da mola. Nesse instante, q
- Um pêndulo simples tem comprimento quadruplicado, sem alteração da gravidade e com pequena
- No mesmo sistema massa–mola ideal, aumenta-se a amplitude de oscilação de 0,10 m para 0,20
- Um equipamento é representado por uma massa que pode deslocar-se somente na horizontal, li
- Um sistema massa–mola–amortecedor apresenta razão de amortecimento ζ=0,25 e recebe um desl
- Uma suspensão recebe amortecedor ajustado ao valor crítico do modelo linear de um grau de 
- Uma máquina aplica força harmônica a uma massa com mola e amortecedor. Depois de cessarem 
- Uma máquina de massa 1 kg sobre mola de 100 N/m recebe amortecedor viscoso de coeficiente 
- Um isolador mecânico é avaliado por transmissibilidade de força do modelo viscoso clássico
- Uma sirene parada em ar imóvel emite 1000 Hz. Um observador dirige-se a ela a 34 m/s, e a 
- Uma fonte sonora de 1000 Hz aproxima-se de observador parado a 34 m/s, em ar com som a 340
- Uma fonte que emite 1000 Hz se afasta a 34 m/s de observador em repouso. Considere v=340 m
- Um reservatório aberto contém água com massa específica de 1000 kg/m³. A 3 m de profundida
- Um tubo horizontal transporta água incompressível sem derivações. A segunda seção possui u
- Um corpo desloca 0,002 m³ de água de massa específica 1000 kg/m³. Use g=10 m/s². Qual é o 
- Água passa de uma região horizontal com v1=2 m/s a outra com v2=4 m/s. Despreze perdas; us
- Em escoamento laminar completamente desenvolvido de fluido newtoniano em tubo circular, do
- Água com ρ=1000 kg/m³, μ=0,001 Pa·s e velocidade média 2 m/s percorre tubo de diâmetro 0,0
- Uma tubulação tem f_D=0,02, comprimento 100 m, diâmetro 0,10 m e velocidade 2 m/s. Com g=1
- Uma fonte de 1000 Hz aproxima-se de observador a 34 m/s, enquanto ele também se aproxima d
- Fonte e observador deslocam-se juntos a 34 m/s no mesmo sentido do ar em repouso, mantendo
- Uma fonte sonora aproxima-se de observador fixo com velocidade próxima à do som. O denomin
- Uma barra livre de 2 m tem coeficiente de dilatação linear 12×10⁻⁶ K⁻¹. Após aquecer 50 K 
- Uma máquina de Carnot ideal opera entre reservatórios a 600 K e 300 K. Qual limite de rend
- Uma mola ideal submete a massa a força restauradora proporcional ao afastamento e em senti
- Quando uma massa no MHS passa pela posição de equilíbrio, sua aceleração tem módulo máximo
- Uma massa ligada a mola e amortecedor viscoso, com 0<ζ<1, pode cruzar repetidamente o equi
- Um oscilador criticamente amortecido tem necessariamente grande número de oscilações antes
- Uma corda fixa em ambas as extremidades pode formar ondas estacionárias com nós nas extrem
- No mesmo ar à mesma temperatura, dobrar a frequência emitida por uma fonte sonora dobra a 
- Em um recipiente aberto com líquido estático e homogêneo, a pressão manométrica a uma prof
- Num sistema massa–mola–amortecedor de um grau de liberdade submetido a força senoidal, a r
- Para amortecimento pequeno e força periódica próxima da frequência natural, a amplitude da
- Aumentar amortecimento viscoso melhora necessariamente o isolamento de força em qualquer f
- Em tubulação horizontal sem bombas, de escoamento estacionário, ideal e incompressível, a 
- Em escoamento estacionário de água incompressível num tubo sem vazamento, a velocidade dev
- No modelo laminar de Hagen–Poiseuille, mantendo iguais fluido, comprimento e diferença de 
- Uma variação de temperatura de 25 °C corresponde a 298,15 K de variação, porque é necessár
- Uma estrutura modelada por duas coordenadas de deslocamento independentes pode apresentar 
- Em um sistema de vários graus de liberdade, qualquer força harmônica excita com igual inte
- Em escoamento interno por tubo circular, o número de Reynolds pode ser calculado pela razã
- A equação de Bernoulli ideal pode ser aplicada a escoamento real viscoso com bomba e perda
- Sob aquecimento rápido, o concreto pode apresentar fissuração e desplacamento associados a
- Uma máquina térmica cíclica ideal pode converter em trabalho a totalidade do calor recebid
```

## Pedido 37: Pronomes relativos e funções sintáticas (`portugues.pronomes-relativos-funcoes`, 51 tópicos de edital)

```
Matéria: portugues.pronomes-relativos-funcoes (Pronomes relativos e funções sintáticas)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Reconhecimento dos relativos e identificação do antecedente": 20
- "2. Funções sintáticas de que, quem e o qual": 20
- "3. Cujo, onde, aonde e quanto: posse, lugar e quantidade": 20
- "4. Regência, preposições e crase antes dos relativos": 20
- "5. Análise integrada: referências ambíguas, reescritas e funções especiais": 20

Já existem (não repita):
- Em 'A pesquisadora que elaborou o relatório participou da reunião', qual é a função sintát
- Na frase 'O documento que a secretária revisou foi arquivado', qual papel 'que' desempenha
- Uma revisora compara 'O plano que foi aprovado' com 'Confirmaram que o plano seria aprovad
- Em 'A colega a quem entreguei o manuscrito respondeu hoje', qual análise da expressão 'a q
- Uma professora reescreve 'A artista cujas telas foram expostas recebeu homenagem'. O que d
- Uma aluna escreveu 'A cidade aonde moro tem muitas praças'. Qual reescrita atende melhor à
- Ao revisar 'O livro que gosto está na estante', deseja-se preservar a regência formal do v
- Na frase 'O bairro onde cresci foi revitalizado', qual função sintática exerce o relativo 
- O texto formal registra 'A questão a qual me referi permanece aberta'. Qual forma corrige 
- Em 'A decisão de que tenho conhecimento foi publicada', qual palavra efetivamente selecion
- Na passagem 'O jornalista por quem a reportagem foi redigida concedeu entrevista', qual é 
- Na frase 'O cientista cujos dados foram divulgados discordou da análise', que papel desemp
- Uma revisora deseja escolher a forma correta para 'O artigo ___ a equipe discordou foi ree
- Em 'Tudo quanto solicitei foi entregue', qual é a função do relativo 'quanto' em relação a
- Uma frase apresenta 'O destino ___ nos dirigimos fica na costa'. Qual relativo completa co
- No período 'A regra à qual a equipe obedeceu foi modificada', por que se escreve 'à qual' 
- Em 'Ele continua sendo o amigo que sempre foi', a expressão relativa contém sujeito elípti
- Em 'A cientista a cujas pesquisas me referi foi convidada', uma questão pede a função de '
- Considere 'A autora a cuja obra aludi apresentou novo livro'. Qual reescrita preserva a re
- No exemplo 'O pesquisador falou com o filho do diretor que receberá o prêmio', o redator d
- Uma revisora avalia a expressão 'Os resultados de que tenho convicção'. Qual justificativa
- Uma prova compara 'A jornalista por quem o texto foi escrito' com 'A jornalista com quem c
- Na construção 'Foram convidados tantos quantos chegaram ao local', qual a função sintática
- Duas frases são confrontadas: 'A aluna a quem sou favorável' e 'A aluna a quem enviei o pa
- O texto formal diz 'O trajeto onde seguimos até a serra'. O verbo seguir, nesse contexto, 
- Na frase 'A professora que chegou cedo iniciou a aula', o pronome relativo 'que' retoma pr
- Em 'A professora afirmou que chegaria cedo', 'que' é pronome relativo com antecedente prof
- Em 'O relatório que li ontem ficou pronto', o relativo 'que' exerce função de objeto diret
- Em 'A compositora cujo canções ouvimos', o pronome 'cujo' concorda corretamente com compos
- Na construção 'A cidade onde nasci', o relativo 'onde' retoma cidade e pode ser substituíd
- Em 'O parecer de que gostei', a sequência 'de que' contém preposição dispensável na norma-
- No enunciado 'A pesquisadora a quem enviei os dados confirmou o recebimento', a quem funci
- Em 'O fato de que tenho conhecimento', a expressão 'de que' é objeto indireto de tenho, po
- Na frase 'A artista cujas pinturas foram vendidas', o pronome cujas exerce adjunto adnomin
- Na norma-padrão, a expressão 'A professora cuja a pesquisa foi reconhecida' está corretame
- A sequência 'A norma à qual obedeci' contém crase porque a preposição a, regida por obedec
- Na construção 'O local aonde moro', o uso de aonde é motivado pela regência de morar, que 
- Em 'Tudo quanto foi prometido será examinado', o relativo quanto pode ser analisado como s
- Em 'A autora a cujo livro me referi', o pronome cujo exerce objeto indireto de referi, já 
- No período 'A cronista por quem a resenha foi escrita participou do evento', por quem repr
- Em 'A pesquisadora à cuja tese aludi', o acento grave é exigido porque aludir rege a e cuj
- Na oração 'O colega que ele sempre foi', a função de que é predicativo do sujeito, pois el
- A expressão 'a fase onde a proposta foi debatida' é necessariamente impossível em qualquer
- Em 'O orientador conversou com o filho do escritor que foi premiado', substituir que por o
- Como relativo sempre exige um substantivo expresso antes dele, a construção 'Quem estuda a
```

## Pedido 38: Criminologia contemporânea: correntes, mídia, violências e sistema de justiça criminal (`seguranca-publica.criminologia-contemporanea`, 51 tópicos de edital)

```
Matéria: seguranca-publica.criminologia-contemporanea (Criminologia contemporânea: correntes, mídia, violências e sistema de justiça criminal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Criminologia cultural e criminologia queer": 17
- "2. Criminologia feminista e criminologia racial": 17
- "3. Criminologia clínica": 17
- "4. Mídia, tecnologia e criminalidade de massa e organizada": 17
- "5. Bullying, assédio moral e stalking": 16
- "6. Reconhecimento, imaginário punitivo e sistema de justiça criminal": 16

Já existem (não repita):
- A criminologia cultural, associada a autores como Jeff Ferrell, Keith Hayward e Jock Young
- O conceito de edgework, utilizado pela criminologia cultural, refere-se à busca voluntária
- A ideia de que o crime oferece atrações emocionais imediatas, como excitação e sensação de
- A criminologia queer critica a heteronormatividade presente na lei, nas instituições de co
- Em 2019, ao julgar a ADO 26 e o MI 4.733, o STF decidiu que condutas homofóbicas e transfó
- Para a criminologia cultural, o controle penal e suas imagens também são fenômenos cultura
- A obra considerada marco da criminologia feminista, por criticar o androcentrismo e os est
- Segundo a criminologia feminista brasileira, em especial Vera Regina Pereira de Andrade, o
- O conceito de necropolítica, usado pela criminologia racial para analisar a violência esta
- Para a teoria do racismo estrutural, o racismo se resume a atos individuais e conscientes 
- A expressão "genocídio da juventude negra" e a leitura do sistema penal brasileiro como pr
- A criminologia feminista estuda a mulher tanto como autora quanto como vítima de crimes, a
- O modelo tradicional de criminologia clínica caracteriza-se por:
- Na Lei de Execução Penal, a classificação dos condenados para orientar a individualização 
- A criminologia clínica de inclusão social, proposta por Alvino Augusto de Sá, desloca o fo
- Entre as críticas dirigidas ao modelo tradicional de criminologia clínica, destaca-se a de
- A Lei nº 14.843/2024 voltou a exigir o exame criminológico para a progressão de regime, re
- A criminologia clínica aplica conhecimentos de psiquiatria, psicologia e serviço social ao
- O conceito de pânico moral, que descreve reações sociais desproporcionais a grupos apresen
- Para Zaffaroni, a criminologia midiática constrói uma realidade do crime que divide a soci
- Nos termos da Lei nº 12.850/2013, considera-se organização criminosa a associação estrutur
- A criminalidade de massa caracteriza-se por crimes comuns e numerosos, como furtos e roubo
- Em relação ao uso de novas tecnologias no controle do crime, como reconhecimento facial e 
- O enfrentamento eficaz da criminalidade organizada, segundo a literatura criminológica, de
- O crime de perseguição (stalking), previsto no art. 147-A do Código Penal, exige que a con
- O crime de perseguição (art. 147-A do CP) é de ação penal pública incondicionada.
- A pena do crime de perseguição é aumentada de metade quando cometido:
- O assédio sexual, tipificado no art. 216-A do Código Penal, distingue-se do assédio moral 
- A Lei nº 13.185/2015, que instituiu o Programa de Combate à Intimidação Sistemática (bully
- A Lei nº 14.811/2024 inseriu no Código Penal o crime de intimidação sistemática, cuja form
- O assédio moral no trabalho está previsto como tipo penal geral e autônomo no Código Penal
- Ao criar o crime de perseguição, a Lei nº 14.132/2021 revogou:
- Na teoria do reconhecimento de Axel Honneth, a esfera do direito, ligada à igualdade juríd
- Luiz Eduardo Soares descreve a invisibilidade social do jovem pobre como fator que pode le
- Em Vigiar e punir, Michel Foucault descreve a passagem histórica:
- A leitura da modernidade como tensão entre regulação e emancipação, usada para analisar o 
- A expressão "cultura do controle", que descreve o declínio do ideal ressocializador e a as
- A criminalização secundária, na terminologia da criminologia crítica, corresponde:
- A cifra oculta (ou negra) da criminalidade corresponde aos crimes que, embora praticados, 
- Do ponto de vista criminológico, uma investigação técnica e qualificada pela polícia judic
- Loïc Wacquant, em Punir os pobres, sustenta que, nas últimas décadas, houve redução do Est
```

## Pedido 39: Bioquímica: metabolismo e biologia molecular (`bioquimica.metabolismo-biologia-molecular`, 50 tópicos de edital)

```
Matéria: bioquimica.metabolismo-biologia-molecular (Bioquímica: metabolismo e biologia molecular)
Capítulos e quantas questões quero em cada um (total 100):
- "Biomoléculas, bioenergética e cinética enzimática": 20
- "Metabolismo dos carboidratos e respiração celular": 20
- "Lipídios, aminoácidos e integração metabólica": 20
- "Sinalização celular e controle das vias metabólicas": 20
- "Fluxo da informação genética e técnicas moleculares": 20

Já existem (não repita):
- Qual componente molecular constitui a base das reservas de glicogênio em animais?
- Os triacilgliceróis armazenam energia, enquanto fosfolipídios participam da constituição d
- No modelo clássico de Michaelis–Menten, qual situação define diretamente o parâmetro Km?
- Uma enzima altera o valor de ΔG°′ de uma reação, tornando espontâneo um processo originalm
- Ao comparar a enzima sem inibidor com a mesma enzima sob inibição competitiva reversível, 
- Na inibição não competitiva pura, Vmax diminui sem modificação do Km aparente no modelo cl
- Uma catalisadora aumenta a velocidade de uma reação reversível sem modificar as concentraç
- Km fornece uma medida universal e direta da constante de dissociação de qualquer par enzim
- Em células eucarióticas, qual etapa central do metabolismo da glicose ocorre no citosol?
- A glicólise forma dois ATP líquidos por glicose, pois consome dois ATP inicialmente e prod
- Ao final da glicólise de uma molécula de glicose, sem incluir outras vias, qual é o balanç
- A fermentação láctica tem como finalidade redox regenerar NAD⁺, permitindo continuidade da
- Um bloqueio do transporte respiratório de elétrons reduz a reoxidação de NADH. Qual conseq
- O complexo II da cadeia respiratória mitocondrial bombeia prótons para o espaço intermembr
- Qual substância atua como aceptor final dos elétrons na fosforilação oxidativa mitocondria
- A cada volta do ciclo de Krebs, a oxidação de um acetil-CoA gera três NADH, um FADH₂, um G
- Na β-oxidação mitocondrial de um ácido graxo saturado de número par, qual unidade carbonad
- A síntese de ácidos graxos utiliza predominantemente NADPH como fonte de equivalentes redu
- Na oxidação completa de uma molécula de palmitato (C16:0), quantos ciclos de β-oxidação e 
- Malonil-CoA promove diretamente a entrada de acil-CoA de cadeia longa na mitocôndria ao at
- Por que a via das pentoses-fosfato é importante para síntese de ácidos graxos?
- Nos animais, leucina e lisina são classificadas como aminoácidos exclusivamente cetogênico
- Qual mecanismo ajuda a impedir que síntese e β-oxidação de ácidos graxos ocorram intensame
- A ureia é sintetizada diretamente pelo complexo IV da cadeia respiratória como destino dos
- Uma célula libera substância que altera a atividade de células vizinhas, sem exigir circul
- Na sinalização parácrina, o mensageiro age predominantemente sobre células próximas ao loc
- Um receptor de membrana ativa uma proteína Gs que aumenta a atividade da adenilato ciclase
- Receptores acoplados à proteína G necessariamente possuem atividade tirosina quinase intrí
- Qual combinação corresponde ao mecanismo de um receptor típico de insulina e a uma via de 
- A hidrólise de GTP ligado à subunidade alfa de proteína G pode contribuir para encerrar a 
- Após ativação de uma cascata de sinalização, qual evento contribui diretamente para seu de
- A fosforilação de uma proteína sempre aumenta sua atividade catalítica, independentemente 
- Durante a replicação de DNA, em qual direção a DNA-polimerase sintetiza a fita nova?
- Na tradução, o ribossomo lê os códons do mRNA no sentido 5′→3′ e sintetiza polipeptídeo.
- Qual evento constitui processamento típico de pré-mRNA eucariótico destinado à tradução?
- A replicação semiconservativa do DNA produz moléculas filhas que mantêm uma fita antiga e 
- Uma amostra contém RNA e o pesquisador deseja quantificar a abundância relativa de determi
- Na PCR convencional, a DNA-polimerase termoestável transcreve diretamente RNA em DNA sem n
- Um laboratório pretende detectar a presença de uma proteína específica, e não seu gene ou 
- O Northern blot identifica principalmente proteínas específicas por ligação de anticorpos 
```

## Pedido 40: Normas do serviço público federal (`legislacao.servico-publico-federal-normas`, 50 tópicos de edital)

```
Matéria: legislacao.servico-publico-federal-normas (Normas do serviço público federal)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Programa de Gestão e Desempenho: fundamento, instituição e adesão": 18
- "2. Modalidades, teletrabalho e consequências financeiras do PGD": 18
- "3. Lei nº 8.027/1990: destinatários, deveres e faltas leves ou intermediárias": 19
- "4. Infrações graves, acumulação, responsabilidade e prescrição na Lei nº 8.027/1990": 19
- "5. Plano de Carreiras e Cargos de Magistério Federal — Lei nº 12.772/2012": 26

Já existem (não repita):
- Uma unidade federal implantou metas e controle de entregas, mas manteve os participantes t
- Uma servidora afirma que, após demonstrar alto desempenho, conquistou direito automático e
- O número de interessados em ingressar no PGD é maior que o número de vagas. Qual critério 
- Um servidor aderiu ao PGD mediante plano firmado com sua chefia. Qual conjunto de informaç
- Uma pessoa foi autorizada a executar integralmente suas atividades em teletrabalho. Quem c
- O PGD de uma unidade foi revogado, e participante em teletrabalho precisa retornar à ativi
- Uma chefia ordena que servidor altere registro público para encobrir desvio. Qual dever es
- Um servidor saiu da repartição durante o expediente sem autorização de seu superior imedia
- Docente federal passou do nível 2 para o nível 3 dentro da mesma classe, após cumprir o in
- Uma autoridade pretende delegar a edição da portaria que institui o PGD, alegando que o ar
- Servidor em PGD remoto escolheu morar em cidade distinta da sede e solicita diárias para d
- Participante do PGD trabalhou remotamente das 22h às 2h a pedido comprovado da administraç
- Um gestor pretende aplicar a penalidade do art. 4º da Lei nº 8.027/1990 a quem retirou doc
- Um servidor sem justificativa faltou por 20 dias não consecutivos ao longo de seis meses. 
- Uma falta administrativa prevista na Lei nº 8.027/1990 é punível com suspensão e não corre
- Docente da carreira de Magistério Superior na classe A, último nível, busca promoção à cla
- Uma docente do EBTT teve RSC-III reconhecido e solicita promoção para Titular dispensando 
- Uma unidade admitiu servidores efetivos, empregados públicos em exercício e estagiários ao
- Um servidor efetivo, já aprovado no estágio probatório, deseja residir no exterior em tele
- Uma chefia decide aplicar multa em vez de suspensão prevista no art. 4º da Lei nº 8.027/19
- Um servidor deixou de comparecer injustificadamente por 31 dias consecutivos. Qual enquadr
- Uma infração demissível descrita na Lei nº 8.027/1990 não configura crime. Qual prazo pres
- Professor do Magistério Superior cumpre 24 meses no último nível da classe C, possui douto
- Dois docentes cumprem 24 meses no último nível da classe B e têm avaliações favoráveis. O 
- Docente federal em dedicação exclusiva solicita participação remunerada em palestra esporá
- Um setor pode adotar o PGD na modalidade presencial, pois o Decreto nº 11.072/2022 não res
- A adesão ao PGD assegura ao servidor a permanência no programa, sendo vedada a sua revogaç
- Para aderir ao PGD, o agente e sua chefia imediata devem firmar plano de trabalho que cont
- Pelo Decreto nº 11.072/2022, o custeio da infraestrutura física e tecnológica do teletraba
- O art. 2º, IV, da Lei nº 8.027/1990 impõe cumprimento das ordens superiores, ressalvando a
- A passagem de docente do nível 2 para o nível 3 dentro da mesma classe denomina-se promoçã
- A portaria instituidora do PGD precisa indicar a antecedência mínima de convocação presenc
- O servidor residente no exterior em teletrabalho pode aplicar automaticamente o prazo de 3
- A Lei nº 8.027/1990 prevê suspensão de até 90 dias para quem, sem autorização prévia por e
- Para a Lei nº 8.027/1990, ausência injustificada de 20 dias consecutivos é suficiente para
- A Lei nº 12.772/2012, na redação de 2025, prevê 36 meses no último nível da classe A, mais
- No EBTT, RSC-III somado ao mestrado serve para suprir o doutorado exigido para promoção à 
- A Lei nº 12.772/2012 prevê regime regular de 40 horas semanais com dedicação exclusiva e d
- No Decreto nº 11.072/2022, as competências do art. 3º podem ser subdelegadas livremente ap
- O participante em teletrabalho integral não faz jus aos adicionais de insalubridade, peric
- A Lei nº 8.027/1990 pune com advertência escrita o comércio de bens praticado dentro da re
- O art. 8º da Lei nº 8.027/1990 admite responsabilização civil, penal e administrativa pelo
- Após a Lei nº 15.141/2025, as classes do Magistério Superior permanecem A, B, C, D e E, se
- No Magistério Superior, a promoção à classe D/Titular envolve doutorado, avaliação e memor
- No regime de dedicação exclusiva da Lei nº 12.772/2012, toda retribuição eventual por pale
```

## Pedido 41: Medicina nuclear e radioterapia (`saude.medicina-nuclear-radioterapia`, 50 tópicos de edital)

```
Matéria: saude.medicina-nuclear-radioterapia (Medicina nuclear e radioterapia)
Capítulos e quantas questões quero em cada um (total 100):
- "Fundamentos da medicina nuclear, radiofármacos e cintilografia": 20
- "PET-CT, correlação anatômico-funcional e aplicações clínicas": 20
- "Radioproteção e segurança em serviços de medicina nuclear": 20
- "Radioterapia: radiobiologia, teleterapia, braquiterapia e modalidades": 20
- "Planejamento, imobilização, urgências e efeitos adversos": 20

Já existem (não repita):
- Em exame de medicina nuclear, a grandeza que informa o número de desintegrações por segund
- Uma cintilografia planar é realizada por meio da:
- Um dos radioisótopos amplamente empregados na preparação de radiofármacos diagnósticos par
- O componente da imagem híbrida PET-CT que fornece informação anatômica por atenuação de ra
- O princípio de proteção ocupacional que emprega redução do período de exposição, afastamen
- Na radioterapia externa convencional com acelerador linear, ao terminar a aplicação, o pac
- Uma técnica com fontes seladas inseridas no alvo ou muito próximas dele é denominada:
- Um paciente em radioterapia de cabeça e pescoço utiliza máscara termoplástica principalmen
- Na interpretação de um PET-CT com 18F-FDG, a presença de foco hipercaptante deve ser enten
- Um serviço deseja obter cortes funcionais tridimensionais a partir de projeções com fótons
- Um radiofármaco tem meia-vida física e eliminação biológica. Para decaimentos exponenciais
- Na radioproteção de pacientes que receberão radiofármacos, o cuidado mais apropriado é:
- Em um plano de radioterapia externa, o volume criado para contemplar margens de incerteza 
- A distinção correta entre IMRT e IGRT é:
- Na braquiterapia HDR com sistema de pós-carregamento remoto, ao finalizar sessão habitual 
- No contexto da radioterapia, o histograma dose-volume (DVH) serve sobretudo para:
- Paciente com PET-CT de controle após tratamento apresenta hipercaptação em região operada 
- Em serviço de medicina nuclear, uma profissional usa luvas durante manipulação de emissor 
- O valor padronizado de captação (SUV) do PET é mais corretamente definido como:
- Um planejamento tem igual DVH global para determinado órgão de risco em duas alternativas,
- Diante de suspeita de compressão medular por neoplasia e piora rápida da força nas pernas,
- Em radioterapia estereotáxica, o termo radiocirurgia intracraniana indica:
- Um serviço estuda irradiação corporal total para condicionamento em situação selecionada. 
- Ao combinar quimioterapia radiossensibilizante com radioterapia, deve-se considerar princi
- A medicina nuclear diagnóstica pode investigar função de órgãos por meio da biodistribuiçã
- O gray é a unidade usada para expressar número de desintegrações nucleares por segundo.
- Na cintilografia, é possível registrar fótons provenientes do radiofármaco administrado ao
- O PET baseia-se tipicamente na detecção coincidente de dois fótons originados da aniquilaç
- É adequado afirmar que todo foco de captação de 18F-FDG corresponde necessariamente a tumo
- A tríade tempo, distância e blindagem é útil para reduzir exposição externa de trabalhador
- Na teleterapia convencional com acelerador linear, o paciente necessariamente mantém mater
- O SPECT/CT combina informação funcional de emissão com referência anatômica obtida por tom
- A meia-vida efetiva de um radiofármaco é sempre maior que sua meia-vida física por incluir
- Os limites de dose aplicáveis ao público e aos profissionais são empregados como teto tera
- A orientação de amamentação após medicina nuclear deve considerar o radionuclídeo e o radi
- IMRT e IGRT são expressões equivalentes e descrevem exclusivamente a modulação de intensid
- O PTV corresponde a um volume geométrico usado para considerar incertezas sobre a cobertur
- Na braquiterapia HDR com fonte temporária recolhida pelo aparelho, o paciente retém obriga
- Uma captação de FDG baixa é suficiente para excluir toda doença neoplásica independentemen
- Em PET-CT, mudanças no intervalo entre administração e aquisição podem influenciar compara
- A radioterapia de corpo inteiro pode demandar planejamento específico de dose e acompanham
- Se dois planos têm o mesmo DVH de um órgão, eles obrigatoriamente apresentam idêntica dist
- Déficit neurológico progressivo em paciente com suspeita de compressão medular neoplásica 
- O termo radiocirurgia estereotáxica obriga a existência de incisão cirúrgica no mesmo proc
- Os efeitos tardios da radioterapia podem ocorrer meses ou anos após tratamento e depender 
```

## Pedido 42: Musicoterapia (`saude.musicoterapia`, 50 tópicos de edital)

```
Matéria: saude.musicoterapia (Musicoterapia)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos, história, bases científicas e identidade profissional": 17
- "2. Modelos, abordagens e métodos de intervenção": 17
- "3. Avaliação musicoterapêutica, planejamento e evolução clínica": 17
- "4. Saúde mental, desenvolvimento humano e contextos comunitários": 17
- "5. Neurologia, reabilitação, atenção hospitalar e gerontologia": 16
- "6. Ética, legislação e responsabilidade profissional no Brasil": 16

Já existem (não repita):
- O atendimento musicoterapêutico caracteriza-se exclusivamente pela execução tecnicamente c
- Ritmo, melodia, timbre, dinâmica e silêncio podem ser considerados elementos ou recursos r
- O método composicional descrito por Bruscia pode envolver a criação de uma letra de canção
- A avaliação musicoterapêutica inicial pode considerar histórico musical, formas de comunic
- Uma canção reconhecida por pessoa idosa com demência comprova, por si só, recuperação glob
- A Lei nº 14.842/2024 dispõe sobre a atividade profissional de musicoterapeuta no Brasil.
- Na musicoterapia criativa de Nordoff-Robbins, improvisar pressupõe abrir mão de metas clín
- No modelo de Benenzon, a identidade sonora (ISO) deve ser tomada como conceito teórico que
- O diagnóstico musicoterapêutico pode organizar hipóteses clínicas próprias da área, mas nã
- A classificação de uma sessão como receptiva impede que o paciente expresse emoções ou rea
- A presença da musicoterapia na PNPIC implica obrigação automática de oferta diária da prát
- Em saúde mental, uma sessão grupal pode compor o projeto terapêutico sem substituir automa
- O registro 'participou bastante' é suficiente, isoladamente, para demonstrar uma mudança c
- A lei federal vigente tornou expressamente privativas as avaliações musicoterapêuticas ini
- O art. 3º da Lei nº 14.842/2024 admite, como uma de suas hipóteses legais, graduação estra
- Em paciente com hipersensibilidade auditiva, aumentar o volume gradualmente, mesmo após re
- A formulação 'aumento de turnos musicais na sessão' descreve necessariamente causalidade e
- O prazo da hipótese de pós-graduação lato sensu no art. 3º, III, da Lei nº 14.842/2024 est
- O código ético difundido pela UBAM e a Lei nº 14.842/2024 são instrumentos idênticos em hi
- No cuidado paliativo, trabalhar vínculos e reminiscências musicais pode ser pertinente mes
- Uma unidade organiza encontros de música ambiente no saguão, sem avaliação dos participant
- Na tipologia dos métodos de Bruscia, criar espontaneamente frases sonoras em diálogo, sem 
- A associação correta entre autor e abordagem musicoterapêutica é:
- Qual registro é mais útil como linha de base em um plano para ampliar interação musical?
- Em uma criança com TEA e sensibilidade intensa a sons agudos, a primeira conduta de planej
- Em serviço de saúde mental, qual resultado pode constituir objetivo plausível de musicoter
- Qual norma federal dispõe especificamente sobre a atividade profissional de musicoterapeut
- Uma idosa escolhe canção ligada à sua história de vida e relata lembrança afetiva. A inter
- Um terapeuta utiliza canção conhecida, deixa lacunas na letra e aguarda o usuário completa
- Na análise de um vídeo clínico, o profissional identifica que espelhou os sons produzidos 
- Na revisão de um plano, a meta 'melhorar a socialização' deve ser aperfeiçoada prioritaria
- Ao escrever relatório de intervenção compartilhada com fisioterapia, o musicoterapeuta obs
- Em atividade comunitária, qual situação mais claramente aponta para um processo de musicot
- Sobre emprego de música no cuidado de uma pessoa com Parkinson, assinale a proposição adeq
- Segundo a Lei nº 14.842/2024, assinale hipótese expressamente prevista para exercício da p
- Um hospital solicita que o terapeuta divulgue vídeos identificáveis de sessões em rede soc
- Um avaliador afirma que, por haver 'música receptiva', o paciente é necessariamente passiv
- A respeito da identidade sonora no modelo de Benenzon, qual interpretação é metodologicame
- Durante atendimento, a pessoa interrompe a atividade e refere sensação dolorosa diante de 
- Qual comparação normativa está correta?
- Na Lei nº 14.842/2024, a hipótese prevista para pessoa com experiência profissional anteri
- No plano terapêutico de adolescente com ansiedade, a equipe propõe reprodução obrigatória 
- Um parecer descreve mudança comportamental após três sessões, mas não informa linha de bas
- No texto da Lei nº 14.842/2024, o dispositivo proposto para qualificar certos procedimento
- Sobre o art. 3º, III, da Lei nº 14.842/2024, qual formulação corresponde ao requisito temp
```

## Pedido 43: Trabalho por plataformas, trabalho escravo e fiscalização (`direito-trabalho.temas-contemporaneos-fiscalizacao`, 49 tópicos de edital)

```
Matéria: direito-trabalho.temas-contemporaneos-fiscalizacao (Trabalho por plataformas, trabalho escravo e fiscalização)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Trabalho por plataformas: autonomia, subordinação e vínculo de emprego": 21
- "2. Trabalho em condição análoga à de escravo: elementos, consequências e proteção": 21
- "3. Força maior trabalhista, encerramento de atividades e fato do príncipe": 20
- "4. Inspeção do trabalho, atribuições, dupla visita e situações especiais": 19
- "5. Auto de infração, defesa, recursos e casos integrados": 19

Já existem (não repita):
- Uma plataforma organiza entregas, paga por tarefa e estabelece metas e sanções por ordens 
- Um empregador submete trabalhador a condições gravemente degradantes, mas permite que ele 
- Uma pessoa é resgatada em 2026 de condição análoga à escravidão em ação de fiscalização e 
- Uma empresa invoca força maior para justificar fechamento de unidade causado por manutençã
- Em inspeção trabalhista, constatou-se violação legal sem ocorrência de dupla visita nem pr
- Um estabelecimento com oito empregados é inspecionado e constatada falta de registro de em
- O representante empresarial recusa assinar um auto de infração regularmente lavrado. Qual 
- Uma empresa recebe um auto de infração e deseja apresentar defesa administrativa. Qual pra
- Motorista trabalha por plataforma que aceita prestação não exclusiva. A empresa sustenta q
- Um trabalhador foi submetido a jornada de intensidade extrema, com privação reiterada de d
- Uma trabalhadora doméstica foi encontrada em situação com indícios de escravidão em residê
- Uma indústria invoca o art. 502 da CLT para pagar indenização excepcional após perda parci
- Na primeira inspeção de empresa recém-inaugurada, a fiscalização orienta sobre obrigação e
- A autoridade fiscal lavrou auto fora do estabelecimento por motivo justificado e declarou 
- Uma empresa já apresentou defesa do auto e foi posteriormente notificada de decisão que lh
- Uma pessoa recusa ofertas de plataforma sem sanção e estabelece seus horários; a empresa r
- Uma empresa sofre dificuldade financeira e decide, sem acordo ou convenção coletiva, corta
- A inspeção pretende verificar trabalho doméstico dentro de residência. O empregador nega a
- Uma criança é submetida a condições de trabalho suficientemente degradantes para caracteri
- Em fiscalização, constata-se situação de grave e iminente risco à vida dos trabalhadores. 
- Uma autoridade estatal determina fechamento de atividade econômica por ato que realmente i
- Um empregador toma documentos pessoais de empregado para mantê-lo no alojamento, vigiando 
- Um contrato por prazo determinado é encerrado antecipadamente porque força maior comprovad
- Após lavrar auto de infração, a pessoa responsável pela fiscalização percebe um erro de de
- Pessoa resgatada recebeu a sexta parcela do benefício específico e, oito meses depois, é i
- Uma empresa usa sistema digital para transmitir ordens e fiscalizar entregas pessoais remu
- Para caracterizar redução a condição análoga à escravidão pelo art. 149 do Código Penal, é
- A Lei nº 15.455/2026 alterou o art. 2º-C da Lei nº 7.998/1990, passando a assegurar seis p
- Queda previsível de faturamento causada por decisões comerciais mal planejadas configura n
- Em inspeção que constata infração e não se enquadra em dupla visita nem procedimento espec
- Uma empresa recebe auto de infração e dispõe de trinta dias para sua primeira defesa admin
- A recusa do representante empresarial em assinar auto de infração não retira, por si só, o
- Um estabelecimento com nove empregados recebe sua primeira fiscalização, que constata ausê
- Uma empresa é notificada da decisão administrativa que aplicou multa por infração trabalhi
- O art. 442-B da CLT considera emprego toda prestação autônoma contínua ou exclusiva, porqu
- O art. 149, § 1º, do Código Penal alcança a vigilância ostensiva e a apropriação de docume
- Quando ocorre grave e iminente risco para integridade física dos empregados, o RIT impõe p
- O art. 628-A da CLT permite utilizar Domicílio Eletrônico Trabalhista para cientificar emp
- Em processo de multa administrativa, a empresa dispõe de dez dias para recorrer contados d
- A Lei nº 15.455/2026 acrescentou ao art. 11-A, § 2º, da Lei nº 10.593/2002 exceção à dupla
- Uma empresa que comprova dificuldades econômicas por evento extraordinário pode reduzir un
- Uma autoridade policial toma conhecimento de indícios de exploração análoga à escravidão d
- O reconhecimento de repercussão geral do Tema 1291 em março de 2024 equivale à fixação de 
- Se a fiscalização lavra auto de infração e posteriormente identifica erro, o art. 629, § 2
- A fiscalização documenta condições degradantes suficientes para enquadramento no art. 149 
```

## Pedido 44: Mineração: geologia econômica, prospecção, legislação e impactos, lavra a céu aberto e subterrânea, perfuração e desmonte, segurança, beneficiamento de minérios e rejeitos (`engenharia-minas.lavra-desmonte-beneficiamento`, 49 tópicos de edital)

```
Matéria: engenharia-minas.lavra-desmonte-beneficiamento (Mineração: geologia econômica, prospecção, legislação e impactos, lavra a céu aberto e subterrânea, perfuração e desmonte, segurança, beneficiamento de minérios e rejeitos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Geologia econômica, prospecção, legislação e impactos": 25
- "2. Planejamento e métodos de lavra": 25
- "3. Perfuração, desmonte e segurança": 25
- "4. Beneficiamento de minérios e rejeitos": 25

Já existem (não repita):
- Minério é a rocha ou agregado mineral do qual se pode extrair, com aproveitamento econômic
- O teor mínimo que um bloco de minério deve apresentar para ser lavrado e processado com lu
- Todo recurso mineral medido em uma pesquisa geológica é automaticamente classificado como 
- Na fase de pesquisa mineral, a técnica que permite recuperar amostras contínuas e cilíndri
- Depósitos aluvionares, como os de ouro em leitos e planícies de rios, são exemplos de depó
- Segundo a Constituição Federal, as jazidas e demais recursos minerais:
- A Constituição Federal determina que aquele que explorar recursos minerais fica obrigado a
- A extração de areia e brita para uso imediato na construção civil pode ser feita pelo regi
- A drenagem ácida de mina ocorre principalmente quando:
- No garimpo de ouro, o uso de mercúrio na amalgamação contamina rios e peixes, e o metal te
- No Brasil, a legislação de segurança de barragens passou a proibir a construção e o alteam
- A drenagem ácida de mina é um impacto que se encerra logo após o fim da lavra, pois depend
- A relação estéril/minério (REM) indica a quantidade de estéril que precisa ser removida po
- Uma mina tem reserva lavrável de 50 milhões de toneladas e escala de produção de 5 milhões
- Em uma mina a céu aberto, o patamar horizontal deixado entre bancadas sucessivas, que reté
- Um corpo mineral tabular e horizontal, situado em rocha competente a centenas de metros de
- O método de corte e enchimento é seletivo e adequado a minérios de alto valor em rochas po
- O block caving é um método de lavra subterrânea indicado para corpos pequenos, de alto teo
- No dimensionamento da frota de transporte de uma mina a céu aberto, o número de caminhões 
- No plano de fogo de uma bancada, a distância entre a linha de furos e a face livre denomin
- A subperfuração consiste em perfurar abaixo do nível do piso da bancada, com o objetivo de
- O trecho superior do furo, preenchido com material inerte e sem explosivo, tem a função de
- O uso de retardos na detonação, iniciando os furos em sequência, contribui para reduzir a 
- Em uma pedreira, furos de desmonte estão parcialmente cheios de água. Nessas condições, o 
- Blocos de rocha de grandes dimensões resultantes de um desmonte mal dimensionado, que não 
- Ultralançamento é o arremesso de fragmentos de rocha além da área prevista no desmonte, e 
- A norma regulamentadora que trata especificamente da segurança e saúde ocupacional na mine
- Em minas subterrâneas de carvão, o metano liberado pelas camadas representa risco de explo
- Entre os riscos à saúde dos trabalhadores da mineração, a inalação prolongada de poeira co
- São equipamentos típicos de britagem primária, que recebem os blocos maiores vindos da min
- Em um plano de fogo, aumentar o afastamento além do adequado tende a reduzir a fragmentaçã
- O principal objetivo da cominuição no beneficiamento é promover a liberação dos minerais ú
- Em um circuito fechado de moagem, o hidrociclone tem a função de:
- Jigues, espirais e mesas vibratórias são equipamentos de concentração que exploram princip
- A flotação separa os minerais com base nas propriedades de suas superfícies: reagentes col
- A separação de magnetita de minerais de ganga como o quartzo é feita de forma eficiente po
- Uma usina recebe minério com teor de 2% de metal, produz concentrado com 20% e rejeito com
- No beneficiamento, aumentar o teor do concentrado sempre aumenta também a recuperação do m
- Como alternativa às barragens convencionais, os rejeitos podem ser desaguados em filtros e
- Rejeitos de mineração podem ser aproveitados, por exemplo, por reprocessamento para recupe
```

## Pedido 45: Pensões especiais e benefícios previdenciários específicos (`direito-previdenciario.pensoes-especiais-beneficios-assistenciais`, 48 tópicos de edital)

```
Matéria: direito-previdenciario.pensoes-especiais-beneficios-assistenciais (Pensões especiais e benefícios previdenciários específicos)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Talidomida, seringueiros e ex-combatentes": 24
- "2. Caruaru, Césio-137 e reparação por hanseníase": 21
- "3. Zika e anistia política: pensão versus reparação": 22
- "4. Seguro-defeso do pescador artesanal e atualizações": 15
- "5. Aposentadoria da pessoa com deficiência, CTC e contagem recíproca": 18

Já existem (não repita):
- Após receber a pensão da síndrome da talidomida, pessoa beneficiária passou a exercer ativ
- Uma pessoa comprovou que foi recrutada para produzir borracha na Amazônia durante a Segund
- Um ex-combatente enquadrado na Lei nº 8.059/1990 faleceu e há dependentes aptos à reversão
- Um familiar pede prestação especial após falecimento causado por contaminação em hemodiáli
- Uma vítima de acidente com Césio-137 recebia a pensão especial da Lei nº 9.425/1996. Após 
- Pessoa submetida a isolamento compulsório domiciliar por hanseníase em 1984 requer pensão 
- Uma pessoa tem deficiência permanente decorrente de síndrome congênita associada ao vírus 
- Um anistiado político não consegue comprovar vínculo com atividade laboral no período de p
- Pescador artesanal enquadrado no regime de segurado especial fica impedido de capturar sua
- Uma mulher segurada do RGPS tem deficiência grave reconhecida e vinte anos de contribuição
- Uma pessoa recebia pensão por síndrome da talidomida e passou a receber benefício previden
- Dependente carente de seringueiro do esforço de guerra pretende comprovar atividade histór
- Duas pessoas habilitadas recebem cotas da pensão de ex-combatente. Uma delas perde o direi
- Uma vítima fatal do episódio de Caruaru deixou cônjuge e descendente que comprovam a habil
- Em ação por danos do acidente com Césio-137, a União é condenada a indenizar pessoa já con
- Filha de pessoa submetida a isolamento compulsório por hanseníase foi efetivamente separad
- Uma criança com deficiência permanente decorrente de síndrome congênita associada ao Zika 
- Anistiado político comprovou vínculo com atividade laboral e obteve reparação mensal. Post
- Um segurado especial da pesca solicita seguro-defeso relativo a período iniciado em janeir
- Pescadora artesanal regularmente cadastrada tem requerimento de seguro-defeso recusado por
- Segurada apresenta 18 anos de contribuição com deficiência grave e outros 6 anos anteriore
- Homem de 60 anos, com 15 anos de contribuição e deficiência leve comprovada durante todos 
- Uma pessoa manteve dois vínculos no mesmo intervalo de janeiro a dezembro, um associado ao
- Um segurado quer averbar em RPPS quatro anos de RGPS prestados ao mesmo ente que depois o 
- Um segurado indeniza período anterior à obrigatoriedade de filiação previdenciária para fi
- Uma pessoa apresenta atestado emitido por junta médica oficial confirmando deficiência fís
- Dois relatos testemunhais coerentes sobre atividade na Amazônia durante a Segunda Guerra M
- Um ex-combatente enquadrado na Lei nº 8.059/1990 recebe benefício previdenciário. A exceçã
- A Lei nº 9.422/1996 dirige a pensão especial de Caruaru prioritariamente à própria vítima 
- O cônjuge de vítima do Césio-137 não herda a pensão personalíssima da Lei nº 9.425/1996 pe
- Se os proprietários da instituição de Caruaru forem condenados judicialmente ao pagamento 
- Uma pessoa comprovou que foi separada da mãe porque esta sofreu isolamento compulsório por
- O filho separado dos genitores por isolamento compulsório e habilitado pelo art. 1º-A da L
- Para comprovar a deficiência permanente decorrente de síndrome congênita associada ao Zika
- A regra de cumulação da pensão especial por síndrome congênita do Zika autoriza expressame
- O regime do anistiado político admite, pelo art. 1º, III, da Lei nº 10.559/2002, a contage
- Uma reparação econômica em prestação única prevista na Lei nº 10.559/2002 pode ser somada 
- Em pedido de seguro-defeso relativo a janeiro de 2026, a habilitação pertence ao MTE na di
- Um pescador artesanal que se dedica a duas espécies submetidas a defesos diferentes no mes
- A Lei nº 15.399/2026 prevê solicitação de registro biométrico e inscrição no CadÚnico aos 
- Na fraude comprovada relacionada à obtenção do seguro-defeso, a Lei nº 10.779/2003 prevê i
- No cálculo de aposentadoria de pessoa com deficiência da LC nº 142/2013, o fator previdenc
- Na modalidade de aposentadoria por idade da LC nº 142/2013, completar 55 anos como mulher 
- Uma pessoa que já usou determinado período para obter aposentadoria em um regime não pode 
- A Lei nº 15.363/2026 extinguiu a indenização previdenciária de períodos anteriores à obrig
```

## Pedido 46: Judicialização do direito à saúde (`saude-publica.judicializacao-direito-saude`, 48 tópicos de edital)

```
Matéria: saude-publica.judicializacao-direito-saude (Judicialização do direito à saúde)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Direito à saúde, políticas públicas e limites do controle judicial": 15
- "2. Prova, medicina baseada em evidências e apoio técnico": 36
- "3. Medicamentos não incorporados, sem registro e precedentes vinculantes": 15
- "4. Orçamento, equidade, avaliação econômica e sustentabilidade do SUS": 19
- "5. Tutelas individuais e coletivas, decisões urgentes e solução estrutural": 15

Já existem (não repita):
- Uma administração pública organiza vacinação, prevenção e acesso ao atendimento. Qual fund
- Uma pessoa obtém decisão sobre prestação de saúde, e discute-se qual ente deverá executá-l
- Uma tecnologia foi autorizada pela regulação sanitária, porém não aparece nas listas de di
- Uma pessoa pede tutela urgente para medicamento, apresentando elementos de seu direito e r
- A parte apresenta apenas receita de determinado medicamento ainda não incorporado. Conside
- Uma intervenção funciona em ensaio controlado, mas os resultados em serviços cotidianos va
- Uma avaliação informa gasto adicional de R$ 2 milhões anuais para disponibilizar determina
- Uma cidade apresenta desabastecimento reiterado de medicamento padronizado para centenas d
- Uma ação pede medicamento sem registro sanitário no país. Há aprovação em agência estrange
- Uma pesquisa mostra risco de evento de 12% sem tratamento e 8% com tratamento. Qual é a re
- Uma empresa pleiteia incorporação de nova tecnologia ao SUS. Qual conjunto de avaliações e
- Uma parte pretende provar necessidade de fármaco registrado, não incorporado, juntando rel
- Para medicamento registrado e não incorporado, o Tema 6 prevê consulta técnica antes da de
- Um processo administrativo de incorporação de tecnologia tramita pelo procedimento dos art
- Um tratamento custa R$ 60 mil a mais do que a alternativa e produz dois anos de vida ajust
- Uma pessoa solicita medicamento não incorporado, mas já tem acesso a alternativa prevista 
- Uma pessoa demanda medicamento registrado, mas não incorporado, com custo anual correspond
- Uma política custará R$ 12 mil adicionais por usuário a cada ano, alcançando 500 dos mil e
- Uma decisão judicial concede fármaco não incorporado porque a prescrição diz 'imprescindív
- Uma ação coletiva demonstra fila crônica de procedimentos incorporados, falha de encaminha
- Um laudo relata necessidade clínica e custos elevados, mas a pessoa não fez pedido pela vi
- Uma análise usa somente resultados de um ensaio com população muito diferente da que receb
- Uma região recebe milhares de pedidos idênticos e compra produtos emergencialmente a preço
- Em pedido registrado não incorporado, a parte dispõe de ensaios clínicos de alto nível, ma
- Uma política pública que prioriza vacinação e controle de fatores de risco realiza dever r
- A universalidade de acesso às ações de saúde determina que qualquer medicamento prescrito 
- Medicamento com registro sanitário válido pode continuar fora das listas de dispensação do
- A apresentação de receita de medicamento não incorporado comprova sozinha todos os requisi
- No art. 300 do CPC, uma medida urgente depende de elementos que evidenciem probabilidade d
- Uma estimativa de R$ 10 milhões para implantar uma tecnologia em determinado ano, consider
- Quando muitos usuários sofrem com falha reiterada de distribuição de tratamento já incorpo
- O Tema 793 reconhece solidariedade federativa na saúde, mas sua tese também menciona direc
- O Tema 500 permite impor judicialmente medicamentos experimentais ao Estado, desde que ten
- A avaliação de tecnologias descrita no art. 19-Q, § 2º, da Lei nº 8.080/1990 deve consider
- Na hipótese do Tema 6, apresentar prova científica de alto nível dispensa demonstrar a inc
- A redução de eventos indesejados de 10% para 5% significa diminuição absoluta de 5 pontos 
- A alegação genérica de insuficiência orçamentária, sem documentos e sem exame da prestação
- O prazo do art. 19-R da Lei nº 8.080/1990 é de até 180 dias, mas pode ser ampliado indefin
- Na situação definida pelo Tema 1.234, o valor anual de tratamento conforme PMVG na alíquot
- A autorização por agência estrangeira reconhecida é suficiente, isoladamente, para exigir 
- Um laudo clínico detalhado e uma revisão sistemática podem cumprir funções diferentes: o p
- No Tema 6, a presença de urgência clínica autoriza dispensar a análise do ato de não incor
- Em análise orçamentária simplificada, 800 usuários efetivos com custo adicional anual de R
- Uma ação coletiva voltada a corrigir falhas persistentes no acesso deve ignorar dados de c
- Um estudo pretende negar todos os pedidos judiciais de prestação já incorporada utilizando
```

## Pedido 47: Contabilidade de Partidos Políticos e Campanhas Eleitorais (`contabilidade.contabilidade-partidaria-eleitoral`, 48 tópicos de edital)

```
Matéria: contabilidade.contabilidade-partidaria-eleitoral (Contabilidade de Partidos Políticos e Campanhas Eleitorais)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Fundamentos e contabilidade dos partidos": 34
- "2. Receitas de campanha: fontes permitidas e vedadas": 33
- "3. Gastos, sobras e prestação de contas de campanha": 33

Já existem (não repita):
- Os partidos políticos são pessoas jurídicas de direito privado e prestam contas à Justiça 
- A prestação de contas partidária e eleitoral concretiza, sobretudo, o princípio da:
- A escrituração contábil do partido deve permitir conhecer a origem das receitas e a destin
- A escrituração contábil dos partidos políticos é feita:
- O partido deve movimentar recursos em contas bancárias distintas conforme a natureza, como
- Entre as destinações mínimas obrigatórias dos recursos do Fundo Partidário, inclui-se a de
- O partido político presta contas anualmente à Justiça Eleitoral até 30 de junho do ano seg
- No julgamento das contas, não é resultado possível:
- A desaprovação das contas partidárias pode resultar na devolução de valores aplicados irre
- São receitas típicas de um partido político, entre outras:
- Recursos do Fundo Partidário aplicados irregularmente devem ser devolvidos ao erário.
- A doação de pessoa física a campanha eleitoral está limitada a:
- Pessoas jurídicas podem doar a campanhas eleitorais desde que respeitem o limite de 2% do 
- Recursos recebidos em campanha sem identificação adequada do doador devem ser:
- A cessão de um imóvel para comitê de campanha, sem pagamento, é doação estimável em dinhei
- Para movimentar os recursos da campanha, o candidato deve:
- Cada doação recebida na campanha, em dinheiro ou estimável, exige a emissão de recibo elei
- É fonte vedada de recursos de campanha:
- O financiamento coletivo (vaquinha virtual) é admitido nas campanhas, por entidades cadast
- Um doador com rendimentos brutos de R$ 100.000,00 no ano anterior à eleição doou R$ 15.000
- O candidato pode usar recursos próprios na campanha sem qualquer limite e sem passar pela 
- Recursos recebidos de fonte vedada durante a campanha devem ser:
- O FEFC é fonte pública de recursos de campanha, repassada pelos partidos aos candidatos co
- As sobras financeiras de campanha provenientes de recursos privados devem ser:
- As sobras de recursos do Fundo Especial de Financiamento de Campanha devem ser devolvidas 
- Os gastos eleitorais estão sujeitos a limites fixados para cada cargo e devem ser comprova
- A prestação de contas de campanha é feita:
- Somente os candidatos eleitos estão obrigados a prestar contas de campanha.
- A consequência típica de o candidato não prestar contas de campanha é:
- A prestação de contas de campanha deve ser assinada por profissional de contabilidade habi
- Irregularidades graves na arrecadação ou nos gastos de campanha podem levar, em ação própr
- Os gastos de campanha pagos com recursos do FEFC ou do Fundo Partidário:
- Durante a campanha, há prestações de contas parciais, com divulgação de informações, além 
- Na contabilidade de campanha, a principal função das demonstrações e da prestação de conta
- Os documentos comprobatórios da escrituração partidária podem ser descartados logo após o 
- A falta de prestação de contas anual do partido:
- Doações de pessoas físicas a campanhas devem ser feitas preferencialmente por transferênci
- Para verificar se uma doação de pessoa física excedeu o limite legal, a Justiça Eleitoral 
- Na campanha, a cessão gratuita de serviços de um profissional de design para criar o mater
- Os recursos públicos que partidos e candidatos podem receber incluem:
```

## Pedido 48: História e Geografia de Santa Catarina (`santa-catarina.conhecimentos-estaduais`, 48 tópicos de edital)

```
Matéria: santa-catarina.conhecimentos-estaduais (História e Geografia de Santa Catarina)
Capítulos e quantas questões quero em cada um (total 100):
- "1. Povos indígenas e ocupação colonial": 20
- "2. Século XIX: República Juliana, imigrantes e Federalista": 20
- "3. Século XX: Contestado, nacionalização e desenvolvimento": 20
- "4. Relevo, clima, vegetação e rios": 20
- "5. Regiões geoeconômicas, economia e cultura": 20

Já existem (não repita):
- Nossa Senhora do Desterro, núcleo que deu origem a Florianópolis, foi fundada por volta de
- São Francisco do Sul, no norte do litoral, é considerada a povoação mais antiga de Santa C
- Laguna, fundada em 1676, serviu de base para a expansão portuguesa em direção ao territóri
- A criação da capitania de Santa Catarina em 1738 e a construção de fortalezas na Ilha, com
- São heranças da imigração açoriana no litoral catarinense, EXCETO:
- A imigração açoriana para Santa Catarina ocorreu principalmente entre 1748 e 1756, para po
- Em 1777, a Ilha de Santa Catarina foi ocupada por tropas espanholas e devolvida a Portugal
- Os kaingang e os xokleng, que habitavam o planalto e os vales catarinenses, pertencem ao t
- A República Juliana, proclamada em 1839 durante a Revolução Farroupilha, teve como sede a 
- Anita Garibaldi, conhecida como heroína de dois mundos, nasceu em Santa Catarina, na regiã
- Associe a cidade à colônia ou ao grupo imigrante de origem. I. Joinville II. Blumenau III.
- As colônias alemãs de Santa Catarina, como Blumenau e Joinville, baseavam-se em grandes la
- A imigração italiana em Santa Catarina, a partir da década de 1870, concentrou-se principa
- O nome Florianópolis, adotado pela capital catarinense em 1894, homenageia:
- Após a retomada de Desterro pelas forças do governo, em 1894, houve fuzilamentos de presos
- Sobre a Guerra do Contestado (1912–1916), assinale a alternativa correta.
- Entre os fatores do Contestado estão a construção da ferrovia São Paulo–Rio Grande e as co
- O acordo de limites de 1916 entre Paraná e Santa Catarina deixou a maior parte da área con
- A campanha de nacionalização, que proibiu o uso público do alemão e do italiano e fechou e
- A ponte Hercílio Luz, símbolo de Florianópolis, foi inaugurada em 1926 e ligou a Ilha de S
- O Vale do Itajaí sofreu grandes enchentes que marcaram a história do estado. Assinale a al
- Santa Catarina faz limite com quais unidades e países?
- O rio Peperi-Guaçu marca a fronteira de Santa Catarina com a Argentina, e os rios Pelotas 
- Sobre a hidrografia catarinense, é correto afirmar que:
- A bacia do rio Itajaí-Açu é a maior bacia hidrográfica situada inteiramente em território 
- As cidades catarinenses conhecidas pelos invernos mais rigorosos, com geadas e ocasionalme
- No relevo catarinense, a Serra do Mar predomina no norte do estado, e a Serra Geral, com e
- Assinale a associação correta entre formação vegetal e sua localização em Santa Catarina.
- Santa Catarina possui 295 municípios.
- Associe a região geoeconômica catarinense à sua principal vocação. I. Sul (Criciúma) II. O
- Florianópolis, por ser a capital, é também a cidade mais populosa de Santa Catarina.
- A região catarinense em que se destacam a pecuária, a madeira e a produção de maçã, com ci
- A agroindústria de suínos e aves, apoiada em pequenas propriedades e cooperativas, é a bas
- Entre os portos catarinenses que escoam a produção do estado, NÃO se inclui:
- A Oktoberfest de Blumenau, uma das maiores festas de tradição alemã do país, expressa a he
- A maricultura de ostras e mexilhões, destaque da economia catarinense, concentra-se:
- A economia de Santa Catarina caracteriza-se por forte concentração na região metropolitana
- O governador que, a partir de 1739, mandou construir fortalezas na entrada da baía da Ilha
- A cidade de Joinville, maior do estado, desenvolveu-se principalmente como polo de qual at
- A cidade catarinense que deu nome à colônia fundada em 1850 por um farmacêutico alemão no 
- Considere a cronologia da história catarinense e assinale a ordem correta.
```

## Pedido 49: Regime Jurídico Único dos Servidores de Alagoas (Lei 5.247/1991) (`alagoas.regime-juridico-unico-lei-5247`, 48 tópicos de edital)

```
Matéria: alagoas.regime-juridico-unico-lei-5247 (Regime Jurídico Único dos Servidores de Alagoas (Lei 5.247/1991))
Capítulos e quantas questões quero em cada um (total 100):
- "1. Ingresso, provimento, exercício e vacância": 25
- "2. Remuneração, vantagens e férias": 25
- "3. Licenças, afastamentos, tempo de serviço e petição": 25
- "4. Deveres, proibições, acumulação e penalidades": 25

Já existem (não repita):
- A Lei 5.247/1991 institui o Regime Jurídico Único dos servidores públicos civis do Estado 
- Às pessoas com deficiência, a Lei 5.247/1991 reserva, nos concursos para cargos cujas atri
- Segundo a Lei 5.247/1991, NÃO é forma derivada de provimento de cargo público:
- Pela Lei 5.247/1991, não se abrirá novo concurso enquanto houver candidato aprovado em con
- De acordo com a Lei 5.247/1991, haverá posse apenas nos casos de provimento de cargo por:
- A posse por procurador, na Lei 5.247/1991, fica condicionada à apresentação de instrumento
- O retorno à atividade do servidor aposentado por invalidez, quando a junta médica declara 
- Na reintegração, se o cargo anteriormente ocupado tiver sido extinto, o servidor ficará em
- Se o servidor em disponibilidade não entrar em exercício no prazo legal após o aproveitame
- A remoção a pedido do servidor para acompanhar cônjuge, companheiro ou dependente enfermo 
- O deslocamento do servidor, com o respectivo cargo, para quadro de pessoal de outro órgão 
- O substituto de ocupante de cargo de direção ou chefia faz jus à respectiva gratificação n
- Segundo a Lei 5.247/1991, o servidor perde a parcela de remuneração diária proporcional a 
- O vencimento, a remuneração e o provento do servidor alagoano não podem ser objeto de arre
- O servidor em débito com o erário que for demitido ou exonerado terá, para quitar o débito
- As indenizações pagas ao servidor (ajuda de custo, diárias e transporte) incorporam-se ao 
- A ajuda de custo devida ao servidor que passa a ter exercício em nova sede, com mudança pe
- As diárias do servidor que se afasta da sede a serviço:
- A gratificação natalina corresponde a 1/12 da remuneração de dezembro por mês de exercício
- O adicional por tempo de serviço do servidor efetivo de Alagoas é pago à razão de:
- O servidor que fizer jus aos adicionais de insalubridade e de periculosidade receberá ambo
- O serviço noturno, prestado entre 22 horas de um dia e 5 horas do seguinte, tem o valor-ho
- O servidor que opera direta e permanentemente com raio-x ou substâncias radioativas goza d
- A licença por motivo de doença em pessoa da família é concedida sem prejuízo da remuneraçã
- Após cada quinquênio de efetivo exercício, o servidor estável poderá, no interesse da Admi
- Sobre a licença para tratar de interesses particulares na Lei 5.247/1991, é correto afirma
- A licença para desempenho de mandato classista é concedida sem prejuízo da remuneração, at
- O servidor alagoano investido no mandato de vereador, havendo compatibilidade de horários:
- O afastamento do servidor para estudo ou missão no exterior, autorizado pelo Chefe do Pode
- Pela Lei 5.247/1991, o servidor pode ausentar-se do serviço, sem prejuízo da remuneração, 
- O prazo para interposição de pedido de reconsideração ou de recurso, no direito de petição
- O direito de requerer prescreve em 5 anos quanto aos atos de demissão e de cassação de apo
- O recurso administrativo previsto no direito de petição do servidor alagoano tem efeito su
- Entre os deveres do servidor previstos no art. 118 da Lei 5.247/1991, inclui-se:
- O servidor alagoano pode atuar como procurador junto a repartições públicas quando se trat
- Pela Lei 5.247/1991, o servidor que acumula licitamente dois cargos efetivos e é investido
- A responsabilidade administrativa do servidor será afastada no caso de absolvição criminal
- A pena de suspensão na Lei 5.247/1991 não pode exceder:
- As penalidades de advertência e de suspensão terão seus registros cancelados, se o servido
- Segundo a Lei 5.247/1991, configura abandono de cargo a ausência intencional do servidor a
- Verificada em processo disciplinar a acumulação proibida e provada a boa-fé, o servidor pe
- Pela Lei 5.247/1991, a ação disciplinar prescreve em:
- As denúncias sobre irregularidades no serviço público estadual de Alagoas serão apuradas m
```

## Pedido 50: Números complexos (`matematica.numeros-complexos`, 47 tópicos de edital)

```
Matéria: matematica.numeros-complexos (Números complexos)
Capítulos e quantas questões quero em cada um (total 100):
- "Panorama: escolha da representação": 21
- "1. Forma algébrica e operações": 21
- "2. Conjugado, módulo e divisão": 19
- "3. Forma trigonométrica, potências e raízes": 21
- "4. Como o tema costuma ser cobrado": 18

Já existem (não repita):
- Calcule (3+2i)+(−5+4i) e escreva o resultado na forma algébrica.
- Efetue (7−3i)−(2+5i).
- Calcule o produto (2+3i)(4−i), usando i²=−1.
- Calcule i elevado à 37ª potência.
- Para z=4−3i, calcule z+z̄, em que z̄ é o conjugado de z.
- Determine o módulo do número complexo z=5+12i.
- Calcule (3−4i)(3+4i), reconhecendo os fatores como conjugados.
- Escreva o inverso multiplicativo de 1+2i na forma a+bi.
- Divida (4+2i) por (1−i) e apresente o quociente na forma algébrica.
- Se 2x+3i=8+(y−1)i, com x e y reais, determine x+y.
- Calcule i elevado à 58ª potência.
- Determine o módulo do número complexo −8+15i.
- Um complexo tem forma trigonométrica z=4(cos 60°+i sen 60°). Qual é sua parte imaginária?
- Quantas raízes complexas distintas tem a equação z²=−16?
- Calcule a quarta potência do número complexo 1+i.
- Calcule (6+8i)/(2i) e escreva o resultado como a+bi.
- Use a propriedade do módulo do produto para calcular |(3+4i)(1−i)|.
- Uma das raízes cúbicas de 8 tem argumento 2π/3. Escreva essa raiz na forma algébrica.
- O número z=−√3+i está no segundo quadrante. Determine seu módulo e seu argumento principal
- Calcule [2(cos π/3+i sen π/3)]³ usando a fórmula de De Moivre.
- Qual é o valor de i²?
- Qual é a parte real de z=4−3i?
- Qual é a soma de (1+i) e (2−3i)?
- Qual é o conjugado de 5+2i?
- Qual é o módulo de 3+4i?
- Qual é o resultado de (1+i)²?
- Qual é o resultado de (2+2i)/(1+i)?
- Qual número é o inverso multiplicativo de i?
- Se z+(2−i)=3+4i, qual é z?
- Qual é o produto de (2+i)(1−3i)?
- Quanto vale i elevado a 2025?
- Qual é o quadrado do módulo de 1−i?
- Qual é o valor de (3+i)/(1−i)?
- Quais são as soluções complexas de z²=−4?
- Qual é um argumento de −√3+i?
- Quais são as três raízes cúbicas da unidade?
- Quais são as soluções de z²=4i?
- Se (1−i)z=2, qual é z?
- Quais são as raízes de x²−2x+5=0?
- Se z+z̄=6, qual é a parte real de z?
- A unidade imaginária i satisfaz i²=−1.
- A parte real de 4−3i é −3.
- O conjugado de a+bi é a−bi para reais a e b.
- O módulo de 3+4i é 5.
- As potências positivas de i se repetem em ciclos de quatro.
- A soma de dois números complexos é comutativa.
- Os números complexos podem ser representados como pontos do plano de coordenadas reais.
- Para z=a+bi, vale z+z̄=2a.
- Para todo complexo z, z·z̄=|z|².
- O módulo de um número complexo pode ser negativo quando a parte real é negativa.
- Todo complexo não nulo possui inverso multiplicativo.
- Existe uma ordem compatível com as operações que permite comparar quaisquer dois complexos
- Um argumento de um complexo não nulo pode ser substituído por outro que difira dele por 2π
- O número zero possui um argumento único no plano complexo.
- Na multiplicação de complexos não nulos, os módulos são multiplicados e os argumentos são 
- Na divisão de dois complexos não nulos, os argumentos são somados.
- As raízes n-ésimas de um complexo não nulo têm argumentos igualmente espaçados no círculo.
- Dividir por zero é permitido em números complexos desde que o numerador também seja zero.
- Se z₁z₂=0 para complexos z₁ e z₂, pelo menos um dos fatores é zero.
- Para quaisquer complexos z₁ e z₂, |z₁z₂|=|z₁||z₂|.
- As soluções complexas de z²+1=0 são i e −i.
- Um complexo não nulo tem infinitos argumentos que diferem entre si por múltiplos inteiros 
```

