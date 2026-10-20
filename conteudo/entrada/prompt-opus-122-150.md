# Pedido: BANCO 2 — matérias 122 a 150 (Opus)

Você vai escrever, à mão, os bancos das 29 matérias abaixo (122 a 150), 6 lotes de 50 questões cada (300 por matéria). As regras completas e as fichas estão neste arquivo.

## Como trabalhar
- Faça **um lote por resposta**, começando por **BANCO 122, lote 1**. No fim de cada resposta escreva só: "Próximo: BANCO 122, lote 2" (e assim por diante). Eu respondo "continue".
- Entregue cada lote como **arquivo JSON para baixar** com o nome `<id-da-materia>.banco2-<L>.json` (ex.: `direito-administrativo.procedimento-licitatorio.banco2-1.json`). Se não puder criar arquivo, um único bloco ```json, sem nenhum texto fora dele.
- Escreva cada questão você mesmo. Não use código para gerar, embaralhar ou equilibrar.

## Erros que você (Opus) costuma cometer — confira antes de entregar
1. **Contar errado:** os rascunhos saem com 42–46 questões. Conte: são exatamente 50.
2. **Correta mais longa:** tende a detalhar a correta mais que as erradas. A correta deve ser a mais longa em no máximo 16 das 50 e a mais curta em no máximo 16. Escreva-a com tamanho médio.
3. **Gabarito:** cada letra de A a E correta entre 8 e 12 vezes.
4. Dificuldade: 15 MEDIA e 35 DIFICIL (lote 6: 50 DIFICIL).
5. "topico" copiado exatamente de um tópico da ficha.
6. Nada de distrator absurdo; nenhum número de lei, artigo, prazo ou súmula de que você não tenha certeza (explique o conteúdo sem o número).

# BANCO 2 — Regras (valem para todas as matérias e lotes)

Você é autor de questões de concurso público no Brasil, no nível dos melhores cursinhos. Vai criar um BANCO DE QUESTÕES DIFÍCEIS para o aplicativo Estudário, sempre de **múltipla escolha com 5 alternativas (A a E)**. Não há Certo/Errado neste banco. Pesquise na web e use fontes oficiais (lei vigente, jurisprudência atual, documentação oficial).

Cada matéria tem uma ficha (escopo + tópicos dos editais). Quando eu pedir **"BANCO NNN, lote L"**, entregue só aquele lote, com **50 questões**, sem repetir casos, textos ou pontos já usados nos lotes anteriores da mesma matéria.


## ATENÇÃO — os 7 erros que mais reprovam lotes (leia antes de tudo)
1. **Entregue SEMPRE em JSON**, num único bloco de código ```json (ou arquivo .json para baixar), no formato do fim deste arquivo. Nada de texto, tabela, markdown ou lista fora do JSON. Se a resposta não começar com `{` dentro do bloco, o lote é descartado.
2. **Exatamente 50 questões por lote.** Se não couber, entregue 25 em JSON válido e fechado, escreva só "continua" e, quando eu disser "continue", entregue as outras 25 (numeração seguindo). Nunca 26, nunca 75.
3. **A correta não pode ser a alternativa "equilibrada".** É proibido o padrão em que a correta diz "avaliar conforme os critérios", "considerar o contexto", "coordenar X e Y", "depende do caso", enquanto as erradas dizem "dispensa", "único", "substitui", "sempre", "obrigatoriamente". Em pelo menos 15 das 50 questões a correta deve ser **categórica** (afirmação firme, com "deve", "é vedado", "sempre", um número, um nome técnico) e as erradas devem ser **moderadas e cautelosas**, com o erro escondido no conteúdo.
4. **Nada de distrator absurdo ou cômico** (máquina de escrever, cabo de fibra trocado por script, desfragmentador que guarda processos, tensão elétrica da fonte). Cada errada precisa parecer certa para quem estudou pela metade.
5. **Caso concreto de verdade:** personagem com nome, órgão (tribunal, prefeitura, autarquia), números, datas, sistemas, valores. Proibido abrir a maioria das questões com "Uma organização..." ou "Uma equipe...".
6. **Explicação sem molde:** a frase final de dica não pode começar igual ("Na prova, ...", "Ao resolver, ...", "Para identificar...") em mais de 2 questões do lote. Varie de verdade.
7. **Tamanho:** enunciado de 250 a 600 caracteres na maioria; as 5 alternativas com tamanhos parecidos; a correta é a mais longa em no máximo 16 questões e a mais curta em no máximo 16. Gabarito: cada letra correta de 8 a 12 vezes.

Antes de enviar, conte você mesmo: nº de questões, quantas vezes cada letra é a correta, quantas vezes a correta é a mais longa e a mais curta. Se algum número estiver fora, corrija antes de enviar.
## PROIBIDO montar questões por programa
- **Escreva cada questão individualmente, como um professor escreveria à mão.** É proibido usar código, script, Python, planilha, ferramenta de análise de dados ou qualquer combinação automática para gerar, montar, embaralhar, completar ou "equilibrar" questões, alternativas, comentários ou explicações. Use ferramentas só para pesquisar fontes; o texto das questões sai da sua escrita, uma por uma.
- **Nada de banco de fatos reciclado:** cada questão tem fatos, personagens, números e situação **próprios**. É proibido reaproveitar o mesmo fato mudando só a frase de abertura ("Ao analisar os autos...", "No controle de um evento...", "Para aplicação direta da regra, considere:"). Em uma matéria inteira, um mesmo caso não pode aparecer em mais de uma questão.
- **Proibido texto-molde:** nenhuma frase de abertura, de fechamento de explicação ("Dica de prova: ...") ou de comentário pode se repetir mais de 2 vezes no lote. Comentários não seguem fórmula ("O erro específico está na tese de que... o caso exige aplicar o critério..."): cada um explica, com palavras próprias, o erro daquela alternativa.
- **O enunciado não entrega a resposta:** não liste no enunciado os fundamentos, artigos, súmulas ou temas que resolvem a questão, com nenhum rótulo ("Referências para análise", "Referências aplicáveis", "Bases normativas", "Fundamentos", "Considere o Tema X" etc.). O fundamento aparece só na explicação e nos comentários. Exceção: quando a própria questão pede para interpretar um dispositivo ou súmula citado, transcreva-o, mas sem indicar qual alternativa ele resolve.
- **A correta não pode ser a "moderada" nem a mais curta com frequência:** em cada lote, a correta é a mais curta em no máximo 1/3 das questões. Palavras absolutas ("toda", "qualquer", "integralmente", "somente", "sempre", "presunção absoluta") devem aparecer também em alternativas CORRETAS (há regras que são mesmo absolutas), e as erradas também devem ser formuladas de modo moderado e plausível, com o erro escondido no detalhe, não no tom. Saber qual norma se aplica faz parte da questão.
- **A distribuição não precisa ser exata:** os percentuais de tipo, dificuldade e gabarito são aproximados. Lotes com números idênticos entre si (mesma quantidade de cada tipo em todos) são sinal de montagem automática e serão recusados.
- Se não der para escrever as 50 questões com qualidade numa resposta, entregue menos (por exemplo, 25) e continue na resposta seguinte. É melhor 25 boas que 50 montadas.

## Os 6 lotes de cada matéria (50 questões cada)

| Lote | Estilo | Como é a prova |
|---|---|---|
| 1 | FGV | Caso concreto longo e bem amarrado; cobra aplicação e interpretação; alternativas erradas são aplicações plausíveis, mas equivocadas, do mesmo instituto. Português com textos densos e perguntas de sentido fino. |
| 2 | FCC | Mais literal e técnica; cobra o texto da norma ou o conceito com o detalhe decisivo (prazo, quórum, competência, exceção, número); alternativas muito parecidas entre si. |
| 3 | VUNESP | Enunciado objetivo, casos curtos do dia a dia do órgão; cobra regra e exceção; português com muita gramática aplicada ao texto. |
| 4 | CESGRANRIO | Enunciado direto, uma regra por questão, mas com distratores que exigem saber o detalhe; em exatas e TI, cálculo e cenário prático. |
| 5 | INSTITUTOS (IBFC, AOCP, IDECAN, FUNDATEC, FUMARC) | Mistura de literalidade e caso curto; uso frequente de assertivas I a IV e de "assinale a INCORRETA". |
| 6 | DESAFIO | O mais difícil possível, sem estilo de banca: questões que derrubariam candidatos já aprovados; combina dois ou mais pontos do conteúdo por questão. |

Cada questão leva o campo "estilo" com o nome da coluna Estilo do lote: FGV, FCC, VUNESP, CESGRANRIO, INSTITUTOS ou DESAFIO.

## Dificuldade (o centro deste banco)
- Distribuição por lote: **30% MEDIA e 70% DIFICIL**. Nenhuma FACIL. No lote 6, 100% DIFICIL.
- **MEDIA:** aplica a regra a um caso concreto ou cobra um detalhe (prazo, competência, exceção); exige estudo atento.
- **DIFICIL de verdade:** erra quem estudou só o básico. Use estas técnicas, variando entre elas:
  - **exceção da exceção** ou regra especial que afasta a geral;
  - **jurisprudência** (STF, STJ, TST, TCU, conforme a área) que contraria a leitura literal da lei;
  - **caso com duas ou mais regras combinadas**, em que a resposta depende da ordem certa de aplicação;
  - **caso em que a resposta intuitiva está errada** (o candidato que "sente" a resposta erra);
  - **conceitos vizinhos** que se confundem de verdade (ex.: decadência × prescrição; coordenada explicativa × subordinada causal; IDS × IPS; nulidade × anulabilidade);
  - **troca de UMA palavra decisiva** numa alternativa quase toda correta ("pode"/"deve", "apenas"/"também", "e"/"ou", prazo, quórum);
  - **cálculo com etapa enganosa** (base errada, juros sobre juros, unidade trocada), em exatas e contabilidade;
  - **novidade legislativa ou jurisprudencial recente** (confira na web a vigência).
- **Teste antes de marcar DIFICIL:** se um candidato que estudou só o básico acertaria em poucos segundos, a questão não é difícil. Reescreva-a.
- A dificuldade vem do raciocínio, **nunca do tamanho do texto**: questão difícil pode ser curta.

## Tipos de questão (varie dentro de cada lote)
Em cada lote de 50, use aproximadamente:
- 45% **caso concreto** ou situação-problema ("Pedro, servidor do órgão X, ...") com uma única resposta correta;
- 15% **"assinale a INCORRETA"** (comando em maiúsculas; só uma alternativa falsa);
- 15% **assertivas** I, II, III e IV (às vezes V), com alternativas que são combinações ("Apenas I e III.", "Apenas II, III e IV.");
- 10% **associação de colunas** ou **sequência correta** (ex.: numere a segunda coluna de acordo com a primeira; ordene as etapas), com 5 sequências possíveis nas alternativas;
- 10% **conceito ou literalidade com detalhe decisivo**;
- 5% **interpretação de jurisprudência, súmula, dado, gráfico, tabela ou trecho de código**, conforme a matéria.

Nas assertivas e associações, o comentário de cada alternativa diz quais itens estão certos ou errados e por quê.

## Textos-base (português, inglês e interpretação em geral)
- Use **textos de 200 a 400 palavras** (crônica, artigo de opinião, reportagem, editorial, texto técnico ou literário escrito por você), e não trechos de duas ou três frases.
- Cada texto sustenta **4 ou 5 questões**, cada uma explorando um ponto diferente (ideia central, inferência, coesão, sentido de palavra no contexto, reescrita, tipologia, efeito de sentido). Repita o texto completo no "statement" de cada questão do grupo, para cada uma funcionar sozinha, e indique a linha ou o trecho a que a questão se refere quando for o caso.
- Em um lote de 50, pelo menos 30 questões vêm em grupos sobre textos longos; as demais podem usar trechos curtos ou frases isoladas (ex.: reescrita, pontuação, paráfrase).
- **Nunca** escreva "fictício", "fictícia" ou "texto inventado": apresente o texto como numa prova ("Leia o texto a seguir").
- Varie a abertura dos casos: **no máximo 3 questões por lote** com o molde "alguém resumiu ou interpretou o texto de certo modo; qual leitura o texto autoriza?".

## Sem pistas de chute
- **Palavras absolutas e de nuance distribuídas por igual:** "sempre", "nunca", "somente", "qualquer", "comprova", "elimina", "integralmente" devem aparecer também em alternativas CORRETAS, e "pode", "em regra", "distingue", "convive", "relativiza" também em alternativas ERRADAS. O candidato não pode acertar escolhendo a alternativa mais moderada ou descartando a mais categórica.
- A correta não pode ser sistematicamente a mais "equilibrada" ou a que repete palavras do enunciado; ao menos parte dos distratores deve reaproveitar vocabulário do texto.
- A questão difícil exige **voltar ao texto** (ou à regra) para decidir entre duas alternativas fortes; se dá para responder sem ler o texto-base, reescreva.

## Regras de qualidade
1. Regra precisa, com casos concretos; **nunca invente norma, número, súmula ou dado**. Na dúvida sobre um número, cobre o conteúdo sem o número.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. **Alternativas homogêneas:** as cinco da mesma categoria da resposta (se a resposta é um prazo, as cinco são prazos plausíveis; se é um instituto, cinco institutos vizinhos). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou que se elimina sem saber o conteúdo. Cada distrator é o erro de quem estudou mal.
4. Alternativas com tamanho parecido; a correta **não pode ser a mais longa em mais de 1/3** das questões, nem a mais curta em mais de 1/3.
5. Gabarito espalhado: cada letra de A a E aparece entre 8 e 12 vezes em cada lote de 50.
6. Cada alternativa tem "comentario" próprio (1 a 2 frases: por que está certa ou qual o erro específico). A "explanation" geral (mínimo 150 caracteres) explica a regra que resolve a questão **sem citar letras** e termina com uma dica de como reconhecer esse tipo de questão na prova.
7. Cada questão traz "pegadinha": uma frase dizendo qual armadilha faz o candidato errar.
8. Casos **diferentes entre si e entre lotes**; varie personagens, órgãos, números e o começo dos enunciados. Proibido frase-molde repetida.
9. Texto neutro: não escreva nome de banca, órgão real de concurso ou cargo específico, nem a palavra "banca" (use "a prova").
10. Antes de entregar, **resolva cada questão como candidato**: uma única alternativa defensável e o gabarito certo. Se duas forem defensáveis, reescreva.

## Formato de entrega
Um bloco JSON válido (se puder, como arquivo `<materia>.banco2-L.json`, onde L é o lote):
```json
{
  "materia": "<id da matéria>",
  "lote": 1,
  "estilo": "FGV",
  "questions": [
    { "topico": "<texto exato de um tópico da ficha>", "estilo": "FGV", "tipo": "CASO", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "DIFICIL",
      "options": [
        { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" },
        { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" },
        { "key": "C", "text": "...", "correct": false, "comentario": "..." },
        { "key": "D", "text": "...", "correct": false, "comentario": "..." },
        { "key": "E", "text": "...", "correct": false, "comentario": "..." }
      ],
      "explanation": "...", "pegadinha": "a armadilha em uma frase" }
  ]
}
```
- "format" é sempre "MULTIPLE_CHOICE", com exatamente 5 alternativas (A a E) e uma única correta.
- "difficulty": "MEDIA" ou "DIFICIL".
- "tipo": "CASO", "INCORRETA", "ASSERTIVAS", "ASSOCIACAO", "CONCEITO" ou "INTERPRETACAO".
- Se a resposta ficar grande demais e for cortar, pare num ponto limpo, feche o JSON e avise quantas faltam; eu peço o resto.



==================================================
# BANCO 122 — direito-administrativo.procedimento-licitatorio
==================================================
**Matéria:** Direito Administrativo: Procedimento licitatório, anulação, revogação e recursos (id: `direito-administrativo.procedimento-licitatorio`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Fases e inversão de fases do art. 17; anulação e revogação pelo art. 71; recursos e pedidos de reconsideração dos arts. 165 a 168 da Lei 14.133/2021.
Fica de fora (outras matérias tratam): Contratação direta, modalidades em profundidade, critérios de julgamento e habilitação além do necessário para compreender as fases, execução e sanções contratuais.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Licitação: Procedimento.
- Procedimento licitatório.
- Anulação, revogação e recursos administrativos.
- Licitação: procedimento, revogação e anulação.
- Noções de Licitações e Contratos (Lei nº 14.133/2021 e suas alterações): Estudo Técnico Preliminar.
- Noções de Licitações e Contratos (Lei nº 14.133/2021 e suas alterações): Termo de Referência.
- Procedimento, revogação e anulação.
- Disposições doutrinárias: Procedimento.
- Disposições doutrinárias: Anulação e revogação.
- Licitação e contratos administrativos: Procedimento.
- Licitação: anulação, revogação e recursos administrativos.
- Habilitação.
- Julgamento.
- Fases do procedimento licitatório.
- Anulação e revogação da licitação.
- Adjudicação, homologação e formalização contratual.
- Anulação, revogação, recursos, sanções e crimes em licitações e contratos.
- Lei nº 14.133/2021: procedimento.
- Procedimento, revogação e anulação da licitação.
- Noções de Licitações e Contratos (Lei nº 14.133/2021): Estudo Técnico Preliminar.
- Noções de Licitações e Contratos (Lei nº 14.133/2021): Termo de Referência.
- Procedimento.
- Licitações e contratos administrativos: Elaboração de projetos básicos para contratação de bens e serviços.
- Habilitação e impedimentos.
- Licitações e contratos. 22.1 § 1º do Art. 65 e Art. 69 da Lei nº 14.133/2021.
- Licitação de obras públicas: Procedimentos.
- Licitação de obras públicas: Revogação e anulação.
- Licitação de obras públicas: Objeto da licitação, homologação e adjudicação.
- Procedimentos da licitação.
- Artefatos de Planejamento das Contratações: estudo técnico preliminar (ETP), termo de referência e projeto básico.

==================================================
# BANCO 123 — portugues.termos-da-oracao
==================================================
**Matéria:** Língua Portuguesa: Termos da oração (id: `portugues.termos-da-oracao`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Identificação e análise do sujeito e do predicado, objetos direto e indireto, complemento nominal, adjuntos adnominal e adverbial, predicativo, aposto e vocativo, com distinção entre termos ligados ao verbo, ao nome e à oração.
Fica de fora (outras matérias tratam): Período composto em profundidade, classificação completa das orações subordinadas e estudo isolado da regência verbal e nominal.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Organização sintática das frases: termos e orações.
- Morfossintaxe.
- Sintaxe.
- Ordem direta e inversa.
- Termos da oração.
- Sintaxe: termos da oração e relações sintático-semânticas estabelecidas entre termos, orações, períodos ou parágrafos (período simples e período composto por coordenação e por subordinação).
- Análise morfossintática.
- Organização sintática, termos e orações e ordem direta e inversa.
- Termos essenciais, integrantes e acessórios da oração e vocativo.
- Termos da oração, vocativo, crase e pronomes: emprego, formas de tratamento e colocação.
- Sintaxe: funções sintáticas e suas relações no período simples e no período composto.
- Sintaxe e construção frasal.
- Organização sintática das frases: termos e orações; ordem direta e inversa.
- Análise sintática: termos essenciais, integrantes e acessórios da oração.
- Termos essenciais, integrantes e acessórios da oração, vocativo e crase.
- Termos essenciais, integrantes e acessórios da oração, vocativo, crase e pronomes.
- Sintaxe: funções sintáticas e suas relações nos períodos simples e compostos.
- Língua portuguesa: modalidade culta usada contemporaneamente no Brasil: Morfossintaxe.
- Sintaxe: termos essenciais, integrantes e acessórios da oração.
- Sintaxe: reconhecimento dos termos da oração.
- Análise morfossintática de período simples.
- Funções sintáticas.
- Termos e orações e ordem direta e inversa.
- Termos da oração e relações sintático-semânticas entre orações, períodos e parágrafos.
- Textos da esfera jornalística/publicitária — Análise linguística: Analise a estruturação sintática de sentenças nos textos, observando como a ordem dos termos (sujeito, verbo, complemento) reflete o foco jornalístico (em manchetes, por exemplo).

==================================================
# BANCO 124 — direito-administrativo.sancoes-crimes-licitacoes
==================================================
**Matéria:** Direito Administrativo: Sanções administrativas e crimes em licitações (id: `direito-administrativo.sancoes-crimes-licitacoes`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Infrações administrativas dos arts. 155 a 163 da Lei nº 14.133/2021; advertência, multa, impedimento de licitar e contratar, declaração de inidoneidade, processo sancionador, seus efeitos, prescrição, cadastros, mora e reabilitação; crimes dos arts. 337-E a 337-P do Código Penal.
Fica de fora (outras matérias tratam): Regras gerais de licitação, habilitação e execução contratual fora do necessário para compreender as infrações delimitadas; crimes de outros capítulos, responsabilização civil geral e sanções de outras leis, salvo conexão expressa dos arts. 155 a 163.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Sanções e procedimento sancionatório.
- Crimes em licitações e contratos administrativos.
- Elaboração e fiscalização de contratos: Definição e aplicação de penalidades e sanções administrativas.
- Licitação: sanções.
- Definição e aplicação de penalidades e sanções administrativas.
- Crimes e sanções penais na licitação (Lei nº 14.133/2021 e suas alterações).
- Sanções penais.
- Crimes em licitações e contratos administrativos (Lei nº 14.133/2021 e suas alterações).
- Disposições doutrinárias: Sanções administrativas.
- Sanções e meios de controle.
- Crimes nas licitações e contratos da Administração Pública.
- Infrações e sanções administrativas.
- Crimes da Lei de Licitações e Contratos.
- Crimes relativos à licitação (Lei nº 8.666, de 21/6/1993).
- Licitações e Contratos Administrativos.
- Crimes e sanções em licitações.
- Legislação penal especial: licitações.
- Leis Federais n. 8.666/1993.
- Leis Federais n. 14.133/2021.
- Responsabilidade administrativa por infrações.
- Lei nº 8.666/1993 (crimes nas licitações e contratos da administração pública).
- Lei nº 8.666/1993 e suas alterações (Crimes nas licitações e contratos da administração pública).
- Penalidades e sanções administrativas por inexecução contratual.
- Crimes previstos na Lei nº 14.133/2021 e suas alterações (Lei de licitações).
- Licitação pública - Lei nº 14.133/2021: Das irregularidades.
- Lei nº 14.133/2021 (Crimes em licitações e contratos administrativos).
- Fraude na Execução do Contrato.
- Crimes e sanções penais na licitação (Lei nº 8.666/1993 e Lei nº 14.133/2021).
- Licitações e contratos administrativos: Definição e aplicação de penalidades e sanções administrativas.
- Infrações contratuais e sanções em contratos com a administração pública.

==================================================
# BANCO 125 — direito-constitucional.defesa-estado-instituicoes
==================================================
**Matéria:** Direito Constitucional: Defesa do Estado e das Instituições Democráticas (id: `direito-constitucional.defesa-estado-instituicoes`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Sistema constitucional de crises; Conselhos da República e de Defesa Nacional; estados de defesa e sítio (pressupostos, consulta, autorização, duração, restrições, controles e efeitos); noções institucionais das Forças Armadas; organização constitucional da segurança pública, atribuições, subordinação, guardas municipais e segurança viária.
Fica de fora (outras matérias tratam): Intervenção federal em profundidade; regime detalhado das Forças Armadas e serviço militar; regime dos militares estaduais e normas infraconstitucionais de segurança pública.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Defesa do Estado e das instituições democráticas.
- Defesa do Estado e das instituições democráticas: Segurança pública.
- Poder executivo: Conselho da República e de Defesa Nacional.
- Defesa do Estado e das instituições democráticas: estado de defesa, estado de sítio, Forças Armadas e segurança pública.
- Conselho da República e de Defesa Nacional.
- Estado de defesa e estado de sítio.
- Poder Executivo: Do Conselho da República e do Conselho de Defesa Nacional.
- Conselho da república.
- Conselho de defesa nacional.
- Defesa do Estado e das instituições democráticas: Organização da segurança pública.
- Conselho da República e Conselho de Defesa Nacional.
- Defesa do Estado e das instituições democráticas: segurança pública; organização da segurança pública.
- Estado de Exceção.
- Estado de defesa.
- Estado de sítio.
- Defesa do Estado e das instituições democráticas, segurança pública e organização da segurança pública.
- Defesa do Estado e das instituições democráticas: Estado de defesa e estado de sítio.
- Conselho da República e Conselho de Defesa.
- Segurança pública: Organização da segurança pública.
- Constituição Federal: defesa do Estado e das instituições democráticas.
- Segurança Pública conforme o artigo 144 da Constituição Federal.
- Funções essenciais à Justiça, defesa do Estado e instituições democráticas.
- Sistema constitucional das crises.
- Organização da Segurança Pública.
- Defesa do Estado e das instituições democráticas: Atribuições constitucionais da Polícia Judiciária.
- Atribuições constitucionais da Polícia Judiciária.
- Funções essenciais à justiça: Defesa do Estado e das instituições democráticas.
- Segurança pública e artigo 144 da Constituição Federal.
- Constituição Federal: defesa do Estado, instituições democráticas e segurança pública.
- Defesa do Estado e das instituições democráticas; segurança pública; forças armadas.

==================================================
# BANCO 126 — direito-civil.pessoas-juridicas
==================================================
**Matéria:** Direito Civil: Pessoas jurídicas (id: `direito-civil.pessoas-juridicas`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Conceito, classificação e espécies de pessoas jurídicas, entes despersonalizados, constituição, registro, capacidade e administração, domicílio e responsabilidade, associações e fundações privadas, desconsideração da personalidade jurídica pelo art. 50 do Código Civil e extinção.
Fica de fora (outras matérias tratam): Pessoas naturais, modalidades de sociedades empresárias em espécie, rito processual da desconsideração da personalidade jurídica e direitos reais.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Pessoas jurídicas.
- Desconsideração da personalidade jurídica.
- Pessoas jurídicas: Disposições gerais.
- Pessoas jurídicas: Constituição.
- Pessoas jurídicas: Extinção.
- Associações.
- Fundações.
- Pessoas jurídicas: Desconsideração da personalidade jurídica.
- Pessoas jurídicas: Conceito e elementos caracterizadores.
- Pessoas jurídicas: Capacidade e direitos da personalidade.
- Pessoas jurídicas: Sociedades de fato.
- Pessoas jurídicas: Associações.
- Pessoas jurídicas: Fundações.
- Teoria da desconsideração da personalidade jurídica.
- Pessoas jurídicas: domicílio.
- Pessoas jurídicas: Grupos despersonalizados.
- Pessoas jurídicas: Responsabilidade da pessoa jurídica e dos sócios.
- Das Pessoas Jurídicas.
- Desconsideração inversa.
- Sociedades de fato, grupos despersonalizados, associações.
- Constituição.
- Pessoas jurídicas: sociedades, fundações.
- Pessoas jurídicas: responsabilidade.
- Sociedades, fundações.
- Extinção.
- Pessoas jurídicas. Disposições gerais. Constituição. Extinção.
- Pessoas jurídicas: sociedades de fato, grupos despersonalizados, associações.
- Pessoa jurídica.
- Extinção da pessoa jurídica.
- Domicílio da pessoa jurídica.

==================================================
# BANCO 127 — direito-civil.contratos
==================================================
**Matéria:** Direito Civil: Contratos em geral (id: `direito-civil.contratos`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Princípios, função social, boa-fé e interpretação; classificação; formação e negócios com terceiros; contratos aleatórios e preliminares; vícios redibitórios; evicção; extinção e resolução por onerosidade excessiva.
Fica de fora (outras matérias tratam): Contratos típicos específicos, direitos reais, obrigações em geral e responsabilidade extracontratual.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Contratos em geral.
- Contratos.
- Contratos: Disposições gerais.
- Contratos: Princípios.
- Contratos: Extinção.
- Contratos: Interpretação.
- Contratos: Classificação.
- Contratos: Contratos em geral.
- Princípios.
- Contratos em geral: disposições gerais.
- Dos contratos em geral e em espécie.
- Dos vícios redibitórios e da Evicção.
- Resolução por onerosidade excessiva.
- Evicção.
- Classificação dos contratos.
- Extinção dos contratos.
- Disposições gerais.
- Dos contratos em geral.
- Contratos. Princípios.
- Contratos em geral. Disposições gerais.
- Contratos em geral: princípios contratuais.
- Formação, validade, interpretação, efeitos e extinção dos contratos.
- Revisão e resolução contratual.
- Arras.
- Teoria geral dos contratos.
- Extinção do contrato.
- Vícios redibitórios.
- Contrato preliminar.
- Contratos mercantis: características.
- Interpretação dos contratos.

==================================================
# BANCO 128 — direito-civil.responsabilidade-civil
==================================================
**Matéria:** Direito Civil: Responsabilidade civil (id: `direito-civil.responsabilidade-civil`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Atos ilícitos, abuso de direito, excludentes de ilicitude, responsabilidade subjetiva e objetiva, nexo causal, dano material, moral e estético, quantificação da indenização, excludentes do nexo, responsabilidade por fato de terceiro, do animal e da coisa.
Fica de fora (outras matérias tratam): Responsabilidade civil do Estado, responsabilidade nas relações de consumo e trabalhistas como temas autônomos, inadimplemento contratual e prescrição.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Responsabilidade civil.
- Responsabilidade civil objetiva e subjetiva.
- Atos ilícitos.
- Dano material.
- Obrigação de indenizar.
- Da responsabilidade civil.
- Dano moral.
- Responsabilidade civil: Obrigação de indenizar.
- Responsabilidade civil: Indenização.
- Da obrigação de indenizar.
- Responsabilidade contratual e extracontratual.
- Da indenização.
- Responsabilidade civil objetiva e subjetiva, obrigação de indenizar e dano material.
- Fatos jurídicos: Atos jurídicos ilícitos.
- Responsabilidade civil objetiva e subjetiva: Obrigação de indenizar.
- Responsabilidade civil objetiva e subjetiva: Dano material.
- Nexo causal.
- Atos jurídicos lícitos e ilícitos, requisitos de configuração do ato ilícito e excludentes.
- Responsabilidade civil, obrigação de indenizar e novo direito de danos, inclusive reflexos na imputação e no nexo causal.
- Responsabilidade por fato de outrem.
- Responsabilidade por fato da coisa.
- Dano moral e material.
- Indenização.
- Responsabilidade civil, obrigação de indenizar e indenização.
- Atos jurídicos ilícitos; abuso de direito.
- Responsabilidade civil: obrigação de indenizar; indenização.
- Responsabilidade objetiva e subjetiva.
- Caso fortuito ou de força maior, fato de terceiro, fato do credor e ausência de culpa.
- Responsabilidade civil e preferências e privilégios creditórios.
- Obrigações por atos ilícitos.

==================================================
# BANCO 129 — direito-penal.crimes-incolumidade-fe-publica
==================================================
**Matéria:** Direito Penal: Crimes contra a incolumidade pública e a fé pública (id: `direito-penal.crimes-incolumidade-fe-publica`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Noções sobre crimes de perigo comum, incêndio, explosão e outras condutas perigosas, falsificação de moeda, falsidade documental, uso de documento falso e proteção penal da fé pública.
Fica de fora (outras matérias tratam): Crimes contra a vida e o patrimônio em geral, delitos funcionais e legislação especial que não esteja relacionada ao recorte.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Crimes contra a fé pública.
- Crimes contra a incolumidade pública.
- Crimes contra a Fé Pública: falsidade de títulos e outros papéis públicos.
- Falsidade documental.
- Fraudes em certames de interesse público.
- Crimes contra a fé pública. Falsidade de títulos e outros papéis públicos; falsidade documental; fraudes em certames de interesse público.
- Crimes contra a fé-pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a incolumidade pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a paz pública.
- Crimes em espécie previstos no Código Penal: Crimes contra a fé pública.
- Dos crimes contra a fé pública: da falsidade documental.
- Dos crimes contra a incolumidade pública.
- Crime: crimes contra a incolumidade pública.
- Crime: crimes contra a fé pública.
- Crimes contra a incolumidade pública e a paz pública.
- Crimes contra a fé pública de interesse da Administração Pública.
- Penas: Crimes contra a incolumidade pública.
- Penas: Crimes contra a paz pública.
- Penas: Crimes contra a fé pública.
- Crimes contra a fé pública em detrimento do INSS.
- Parte Especial: crimes contra a fé pública.
- Crimes contra a fé pública: falsidade de títulos, papéis públicos e documentos.
- Crimes contra a fé pública: falsidade de títulos e papéis públicos, falsidade documental e fraudes em certames.
- Crimes em espécie do Código Penal: Crimes contra a incolumidade pública.
- Crimes em espécie do Código Penal: Crimes contra a paz pública.
- Crimes em espécie do Código Penal: Crimes contra a fé pública.
- Crimes contra o patrimônio, contra a incolumidade pública e contra a fé pública.
- Crimes contra a fé pública e falsidade documental.
- Dos crimes contra a fé pública e delitos das fraudes em certames de interesse público.
- Crimes contra a incolumidade pública. Crimes contra a paz pública.

==================================================
# BANCO 130 — direito.declaracao-universal-direitos-humanos
==================================================
**Matéria:** Noções de Direitos Humanos e Legislação: Declaração Universal dos Direitos Humanos (id: `direito.declaracao-universal-direitos-humanos`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Preâmbulo e arts. 1º a 30 da Declaração Universal dos Direitos Humanos: princípios, direitos civis e políticos, direitos econômicos, sociais e culturais, deveres e regras de interpretação.
Fica de fora (outras matérias tratam): Tratados posteriores, sistemas regionais de proteção, declarações distintas e análise de casos concretos de direito interno.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos e Princípios de Yogyakarta +10.
- Declaração Universal dos Direitos Humanos (Resolução 217-A (III) - da Assembleia Geral das Nações Unidas, 1948).
- A Declaração Universal dos Direitos do Homem (ONU).
- Declaração Universal dos Direitos Humanos e Agenda 2030 da ONU.
- Fontes dos Direitos Humanos: Declaração Universal dos Direitos Humanos.
- Direitos civis, políticos, econômicos e culturais.
- Declaração Universal dos Direitos Humanos de 1948.
- Resolução nº 217-A (III) 1948 - Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos, adotada pela Resolução nº 217 da Assembleia Geral da ONU.
- Direitos civis, políticos, econômicos, sociais e culturais.
- Instrumentos internacionais e Declaração Universal dos Direitos Humanos.
- Fontes, instrumentos internacionais e Declaração Universal dos Direitos Humanos.
- Declaração Universal dos Direitos Humanos e Código de Conduta para Funcionários Responsáveis pela Aplicação da Lei.
- Declaração Universal dos Direitos Humanos – adotada pela Assembleia Geral das Nações Unidas em 10 de dezembro de 1948.
- Tratados Internacionais de Proteção aos Direitos Humanos: Declaração Universal dos Direitos Humanos (1948).
- A Declaração Universal dos Direitos Humanos e o Direito Interno Brasileiro.
- Declaração Universal dos Direitos Humanos, proclamada pela Resolução nº 217A (III) da Assembleia Geral das Nações Unidas, de 10 de dezembro de 1948.
- Sistema Global de Proteção dos Direitos Humanos: Declaração Universal dos Direitos Humanos (1948).
- Declaração Universal dos Direitos Humanos, proclamada pela Assembleia Geral das Nações Unidas, de 10 de dezembro de 1948.
- Declaração Universal dos Direitos Humanos (1948).
- Declaração Universal dos Direitos Humanos (ONU).
- Declaração Universal.
- Fontes e instrumentos internacionais, Declaração Universal e Convenção Americana sobre Direitos Humanos.

==================================================
# BANCO 131 — direito.definicao-conceito-direitos-humanos
==================================================
**Matéria:** Noções de Direitos Humanos e Legislação: Definição e conceito de direitos humanos (id: `direito.definicao-conceito-direitos-humanos`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Conceito, fundamento na dignidade humana, titularidade, características, princípios, dimensões didáticas, reconhecimento internacional e proteção constitucional brasileira dos direitos humanos.
Fica de fora (outras matérias tratam): Estudo detalhado de tratados temáticos, sistemas internacionais de petições, regras processuais de tribunais e direitos de grupos específicos, salvo referências breves necessárias à compreensão.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Teoria geral dos Direitos Humanos.
- Direitos Humanos e Direitos Fundamentais.
- Teoria geral dos direitos fundamentais.
- Os fundamentos filosóficos dos Direitos Humanos.
- Fontes, classificação, princípios, características e gerações de direitos humanos.
- Direitos humanos.
- Teoria geral dos direitos humanos: Conceitos, terminologia, estrutura normativa, fundamentação.
- Conceito, terminologia, estrutura normativa, fundamentação.
- Conceito e fundamentação.
- Direitos Humanos na CRFB/88.
- Teoria geral dos direitos humanos: evolução histórica, conceito, classificações e características.
- Evolução histórica, conceito, classificações e características dos direitos humanos.
- Teoria geral dos Direitos Humanos: Direitos humanos e direitos fundamentais.
- Teoria geral dos Direitos Humanos: Gerações ou dimensões dos direitos fundamentais.
- Fontes dos Direitos Humanos.
- Teoria geral dos direitos humanos: conceito, terminologia, eficácia vertical e horizontal e características.
- Gerações de direitos humanos.
- Direitos humanos no ordenamento brasileiro e na Constituição Federal de 1988.
- Conceitos, terminologia, estrutura normativa, fundamentação.
- Direitos humanos: conceito.
- Direitos humanos: abrangência.
- Direitos humanos, direitos fundamentais e dimensões dos direitos fundamentais.
- Tratados internacionais no Brasil, natureza jurídica, incorporação e controles de constitucionalidade e convencionalidade.
- Teoria geral, conceitos, terminologia, eficácia, características, gerações e afirmação histórica dos direitos humanos.
- Direitos humanos e responsabilidade do Estado, no ordenamento brasileiro e na Constituição.
- Direitos humanos, discriminação e exclusão social, inclusive situação de grupos vulneráveis.
- Direitos humanos, discriminação e exclusão social de grupos minoritários e vulneráveis.
- Direitos Humanos: Conceito, princípios essenciais e gerações de direitos humanos.
- Definição e conceito de direitos humanos.
- Teoria geral dos direitos humanos: conceitos, terminologia, estrutura normativa e fundamentação.

==================================================
# BANCO 132 — controle-externo.tribunais-contas
==================================================
**Matéria:** Controle externo e administração pública: Controle externo, tribunais de contas e tomada de contas especial (id: `controle-externo.tribunais-contas`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Fiscalização contábil, financeira, orçamentária, operacional e patrimonial; relação entre Legislativo e tribunais de contas; competências, processos de contas e tomada de contas especial.
Fica de fora (outras matérias tratam): Ritos internos específicos de cada corte, prazos processuais locais, valores de alçada e análise de caso concreto ou de imputação individual.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Controle pelos Tribunais de Contas.
- Tribunais de Contas: funções, natureza jurídica e eficácia das decisões.
- Controle da atividade financeira do Estado: espécies e sistemas.
- Tomadas e prestações de contas.
- Controle externo.
- Procedimentos em processos de prestação de contas da Administração Pública Federal.
- Tribunal de Contas da União (TCU), Tribunais de Contas dos Estados e do Distrito Federal.
- Sistemas de controle externo.
- Ministério Público de Contas.
- Tribunais de Contas.
- Controle da administração pública: controle pelos tribunais de contas.
- Controle legislativo e pelos Tribunais de Contas.
- O Poder Legislativo e os Tribunais de Contas.
- Tribunais de contas: Natureza jurídica.
- Conceito, abrangência e espécies.
- Controles: conformidade e avaliação de políticas públicas.
- Controle de constitucionalidade exercido pelos Tribunais de Contas.
- Competências constitucionais dos Tribunais de Contas: emissão de parecer prévio, julgamento de contas, apreciação da legalidade atos de pessoal, poder geral de cautela, dever de representação, apreciação de denúncias, auditorias e inspeções.
- Precedentes do STF - Supremo Tribunal Federal sobre competências constitucionais dos Tribunais de Contas.
- O princípio do devido processo legal aplicado aos Tribunais de Contas.
- Tomada de contas.
- Prestação e tomada de contas.
- Controle externo no Brasil.
- Contas de governo, contas de gestão e tomada de contas especial.
- Tribunais de Contas: TCU, tribunais estaduais e do Distrito Federal; natureza, competência, jurisdição e organização do TCE-PA.
- Tribunal de Contas da União: natureza, competência e jurisdição.
- Tribunal de Contas da União: natureza, competência e jurisdição: Organização.
- Julgamento e fiscalização.
- Controle interno e externo da contabilidade pública.
- E controle externo da arrecadação tributária pelos Tribunais de Contas.

==================================================
# BANCO 133 — processo-civil.acao
==================================================
**Matéria:** Direito Processual Civil: Ação (id: `processo-civil.acao`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Natureza jurídica e elementos da ação; interesse e legitimidade; teorias, carência, pedidos e cumulação; jurisdição, competência e critérios gerais; conexão, continência; processo, procedimento e pressupostos; sujeitos, capacidades, representação; formação, suspensão e extinção processual.
Fica de fora (outras matérias tratam): Intervenção de terceiros, substituição processual aprofundada, litisconsórcio em detalhe, atos e prazos processuais como tema próprio, nulidades em profundidade, recursos e procedimentos especiais.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Jurisdição e competência.
- Ação.
- Condições da ação.
- Pressupostos processuais.
- Jurisdição e ação: conceito, natureza e características.
- Ação: Classificação.
- A Ação.
- A Ação: Conceito, natureza, elementos e características.
- A Ação: Condições da ação.
- A Ação: Classificação.
- Processo e procedimento.
- Jurisdição e ação.
- Ação: Conceito, natureza, elementos e características.
- Da jurisdição e da ação: conceito, natureza e características.
- Das condições da ação.
- Direito de ação.
- Concurso e cumulação de ações.
- Ação: Condições da ação.
- Ação: Condições da ação, Classificação.
- Ação: conceito, natureza jurídica, teorias, condições, identificação e classificação.
- Elementos da ação.
- Classificação das ações.
- Classificação e critérios identificadores.
- Jurisdição, ação e processo.
- Ação: Condições.
- Ação: Conexão e continência.
- Ação: Concurso e cumulação.
- Jurisdição e ação: condições da ação.
- Processo: conceito, natureza jurídica, teorias e pressupostos processuais.
- Ação: conceito, natureza, elementos, características, condições, classificação, conexão, continência, concurso e cumulação.

==================================================
# BANCO 134 — portugues.variacao-linguistica-norma-linguistica
==================================================
**Matéria:** Língua Portuguesa: Variação linguística e norma linguística (id: `portugues.variacao-linguistica-norma-linguistica`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Variação geográfica, social, situacional, histórica e de modalidade; adequação ao contexto; conceito de norma linguística; distinções entre norma-padrão, norma culta e usos efetivos; prestígio, preconceito e mudança linguística.
Fica de fora (outras matérias tratam): Análise sintática detalhada, classificação morfológica, ortografia e história externa da língua sem relação direta com variação e norma.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Linguística: variação linguística, norma linguística.
- Norma culta.
- Norma padrão.
- Registros de linguagem.
- Variação linguística: norma culta.
- Norma-padrão da língua portuguesa.
- Variação linguística: norma padrão.
- Variação linguística.
- Níveis de linguagem, adequação linguística, análise de atos oficiais.
- Uso da língua em diversos contextos sociais.
- A variação linguística: as diversas modalidades do uso da língua adequada às várias situações de comunicação.
- Variação linguística e suas implicações para o ensino de Língua Portuguesa.
- Língua portuguesa: modalidade culta usada contemporaneamente no Brasil.
- ANÁLISE LINGUÍSTICA: norma culta e variedades linguísticas.
- A relação entre a oralidade e a escrita.
- A linguagem da Internet.
- Textos da esfera pública/oficial — Análise linguística: Avalie a adequação e a necessidade do uso da norma de referência do português brasileiro em virtude da formalidade exigida pelo contexto.
- Textos da esfera jornalística/publicitária — Análise linguística: Identifique a adequação à variedade linguística e as eventuais transgressões estilísticas aceitas na publicidade para se aproximar do público-alvo.
- Textos da esfera literária — Análise linguística: Compreenda o uso literário de expressões coloquiais e variedades regionais na fala das personagens, reconhecendo o distanciamento intencional do texto em relação à norma de referência.
- A ênfase é na adequação sociocomunicativa da linguagem.
- Textos da esfera da vida cotidiana — Análise linguística: Analise usos da oralidade e inovações linguísticas expressivas (gírias, abreviações digitais, entre outros recursos lexicais) próprios da escrita espontânea interpessoal.

==================================================
# BANCO 135 — raciocinio-logico.analise-situacoes
==================================================
**Matéria:** Raciocínio Lógico e Matemático: Compreensão e análise lógica de situações (id: `raciocinio-logico.analise-situacoes`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Sequências numéricas, alfabéticas e figurais descritas verbalmente; analogias e classificação; conceitos e discriminação de elementos; calendário, relógio, intervalos, direções, rotações, vistas e planificações; problemas cotidianos com dados suficientes e conclusões lógicas.
Fica de fora (outras matérias tratam): Demonstrações formais avançadas, geometria analítica, cálculo, probabilidade avançada e problemas sem informação suficiente para uma conclusão única.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Problemas de lógica e raciocínio.
- Compreensão e análise da lógica de uma situação, utilizando as funções intelectuais: raciocínio verbal, raciocínio matemático, raciocínio sequencial, orientação espacial e temporal, formação de conceitos, discriminação de elementos.
- Compreensão e elaboração da lógica das situações por meio de: raciocínio verbal, raciocínio matemático, raciocínio sequencial, orientação espacial e temporal, formação de conceitos, discriminação de elementos.
- Raciocínios verbal, matemático, sequencial, espacial e temporal.
- Compreensão e análise da lógica de uma situação, utilizando as funções intelectuais: raciocínio verbal, raciocínio matemático, raciocínio sequencial, reconhecimento de padrões, orientação espacial e temporal, formação de conceitos, discriminação de elementos.
- Raciocínio sequencial.
- Orientação espacial e temporal.
- Raciocínio verbal, matemático, sequencial, espacial e temporal.
- Compreensão e análise da lógica de uma situação, utilizando as funções intelectuais: raciocínio verbal, raciocínio matemático, raciocínio sequencial, reconhecimento de padrões, orientação espacial e temporal, formação de conceitos, discriminação de elementos; compreensão de dados apresentados em gráficos e tabelas.
- Compreensão e elaboração da lógica das situações por meio de: raciocínio verbal; raciocínio matemático; raciocínio sequencial; orientação espacial e temporal; formação de conceitos; discriminação de elementos.
- Analogias, inferências, deduções e conclusões.
- Raciocínio verbal, matemático, sequencial, espacial e temporal e formação de conceitos.
- Raciocínio verbal, matemático, sequencial, orientação espacial e temporal, formação de conceitos e discriminação de elementos.
- Compreensão e elaboração da lógica das situações por meio de: raciocínio verbal.
- Raciocínio matemático.
- Compreensão e análise lógica de situações-problema.
- Compreensão e análise da lógica de uma situação.
- Raciocínio verbal.
- Formação de conceitos.
- Discriminação de elementos.
- Operações lógicas e resolução de problemas.
- Raciocínio lógico analítico.
- Reconhecimento de padrões, orientação espacial e temporal, conceitos e discriminação de elementos.
- Raciocínio lógico e resolução de situações-problema.
- Raciocínio verbal, matemático e sequencial; orientação espacial e temporal; formação de conceitos e discriminação de elementos.
- Análise lógica de situações: raciocínio verbal, matemático, sequencial, espacial e temporal; formação de conceitos e discriminação de elementos.
- Relações lógicas e dedução de informações e raciocínios verbal, matemático, sequencial, espacial e temporal.
- Raciocínio verbal, matemático e sequencial e orientação espacial e temporal.
- Formação de conceitos e discriminação de elementos.
- Competências Gerais: Compreensão, análise e resolução de situações-problema concretas, abstratas ou hipotéticas, deduzindo novas informações a partir das informações e relações fornecidas.

==================================================
# BANCO 136 — direito-administrativo.bens-publicos
==================================================
**Matéria:** Direito Administrativo: Bens públicos (id: `direito-administrativo.bens-publicos`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Classificação em bens de uso comum do povo, de uso especial e dominicais; afetação e desafetação; inalienabilidade relativa, impenhorabilidade e imprescritibilidade; autorização, permissão e concessão de uso.
Fica de fora (outras matérias tratam): Licitações e contratos em geral, desapropriação e intervenção na propriedade, salvo distinções indispensáveis à alienação e ao uso dos bens.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Bens Públicos.
- Bens públicos: regime jurídico, classificação, administração, aquisição e alienação, formas de utilização por terceiros.
- Bens públicos: classificação e caracteres jurídicos.
- Bens públicos: Regime jurídico.
- Domínio público.
- Vias públicas, cemitérios públicos e portos.
- Bens públicos: natureza jurídica do domínio público.
- Terrenos de marinha e seus acrescidos.
- Bens públicos: Aquisição e alienação dos bens públicos.
- Bens públicos: Formas de utilização dos bens públicos pelos particulares.
- Domínio público: conceito e classificação dos bens públicos.
- Administração, utilização e alienação dos bens públicos.
- Imprescritibilidade, impenhorabilidade e não oneração dos bens públicos.
- Natureza jurídica do domínio público.
- Utilização dos bens públicos: autorização, permissão e concessão de uso, ocupação, aforamento, concessão de domínio pleno.
- Aquisição e alienação dos bens públicos.
- Formas de utilização dos bens públicos pelos particulares.
- Formas de utilização dos bens públicos por terceiros.
- Regime jurídico dos bens públicos.
- Patrimônio público: bens de uso comum, bens de uso especial, bens dominicais.
- Bens públicos: utilização dos bens públicos, autorização, permissão e concessão de uso, ocupação, aforamento, concessão de domínio pleno.
- Bens públicos e distinção dos bens particulares.
- Bens públicos: espécies, prerrogativas, vedações.
- Gestão dos bens públicos.
- Bens públicos, espécies, classificações.
- Bens público em espécie.
- Regimes jurídicos.
- Aquisição e alienação.
- Uso de bem público por particular.
- Bens públicos: classificação, regime jurídico, bens da União e legislação patrimonial.

==================================================
# BANCO 137 — economia.economia-setor-publico
==================================================
**Matéria:** Economia: Economia do Setor Público (id: `economia.economia-setor-publico`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Funções alocativa, distributiva e estabilizadora do governo; bens públicos e recursos comuns; externalidades e instrumentos corretivos; déficit e dívida pública; princípios e efeitos econômicos da tributação.
Fica de fora (outras matérias tratam): Regras jurídicas específicas, percentuais legais de repartição ou limites fiscais, alíquotas vigentes e dados atuais de dívida ou orçamento.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- O papel do Estado e a atuação do governo nas finanças públicas.
- Objetivos, metas, abrangência e definição de Finanças Públicas.
- Externalidades.
- Visão clássica das funções do Estado e evolução das funções do Governo.
- Falhas de mercado, bens públicos e externalidades; papel do Governo.
- Objetivos da política fiscal e políticas alocativas, distributivas e de estabilização.
- Financiamento dos gastos públicos, tributação, equidade e tipos de tributos.
- Déficit público e financiamento do déficit.
- Resultado Fiscal do Governo (NFSP): resultado primário e resultado nominal.
- Noções de Economia do Setor Público: equilíbrio competitivo e eficiência econômica.
- Noções sobre teoremas de bem-estar.
- Formas e dimensões da intervenção da administração na economia.
- Conceitos de déficit e dívida pública.
- As necessidades públicas e as formas de atuação dos governos.
- Política fiscal, tributos e gastos do governo.
- Déficit público.
- O papel do Estado e a atuação do governo nas finanças públicas: Formas e dimensões da intervenção da administração na economia.
- O conceito de Ótimo de Pareto.
- Resultado Fiscal do Governo (Necessidade de Financiamento do Setor Público – NFSP): Resultado Primário e Resultado Nominal.
- Noções sobre economia do setor público.
- Efeitos da atuação do Estado na economia.
- Políticas alocativas, distributivas e de estabilização.
- Política tributária: como os impostos influem nas decisões de consumo, poupança e gasto.
- A função estabilizadora do sistema tributário: a política fiscal e estabilizadores automáticos.
- Falhas de Mercado: Externalidades e ineficiência de mercado.
- Externalidades positivas e negativas.
- Soluções privadas para o problema das externalidades.
- Teorema de Coase.
- Custos de Transação e os limites das soluções privadas ao problema das externalidades.
- Políticas Públicas para as externalidades: Regulamentação.

==================================================
# BANCO 138 — orcamento-publico.conceitos
==================================================
**Matéria:** Orçamento Público: Conceitos (id: `orcamento-publico.conceitos`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Conceito e funções do orçamento público; princípios orçamentários; orçamento-programa e seus objetivos; estrutura constitucional dos orçamentos; elaboração, discussão, emendas, votação e aprovação da proposta orçamentária.
Fica de fora (outras matérias tratam): Estudo detalhado do conteúdo, vigência, prazos e metas próprios do PPA, da LDO e da LOA; execução orçamentária, créditos adicionais e classificação detalhada de receitas e despesas.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Princípios orçamentários.
- Conceitos e papel do orçamento público; evolução do orçamento no Brasil.
- O orçamento público no Brasil.
- Métodos, técnicas e instrumentos do orçamento público.
- Orçamento na Constituição Federal.
- Ciclo orçamentário: elaboração da proposta, discussão, votação e aprovação da lei de orçamento.
- Emendas parlamentares ao Orçamento.
- Orçamento público no Brasil: Estrutura programática.
- Orçamento-programa.
- Planejamento no orçamento-programa.
- Estrutura programática.
- Orçamento público: elaboração, acompanhamento e fiscalização.
- Normas legais aplicáveis ao orçamento público.
- Prática de elaboração de orçamento público.
- Direito Financeiro Orçamento na Constituição de 1988.
- Processo de aprovação da proposta orçamentária.
- Orçamento-Programa: conceitos e objetivos.
- Proposta orçamentária: elaboração, discussão, votação e aprovação.
- Conceito.
- Orçamento na Constituição Federal e Lei de Diretrizes Orçamentárias.
- Orçamento na Constituição Federal, LDO e LOA.
- Bases constitucionais das finanças públicas.
- Orçamentos Públicos.
- Estrutura, princípios e normas constitucionais orçamentárias.
- Elaboração da Lei Orçamentária.
- Natureza jurídica do orçamento.
- Planejamento Governamental, Orçamento Público e Controle.
- Métodos, técnicas e instrumentos do orçamento público; normas legais aplicáveis.
- Sistemas e processos orçamentários.
- A prática brasileira do orçamento-programa.

==================================================
# BANCO 139 — informatica.engenharia-software-ageis
==================================================
**Matéria:** Informática: Engenharia de software e métodos ágeis (id: `informatica.engenharia-software-ageis`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Conceito de engenharia de software; atividades genéricas do processo; modelos de processo (cascata, incremental, prototipação, espiral, Processo Unificado/RUP); engenharia de requisitos (requisitos funcionais e não funcionais, elicitação, especificação, validação e gerenciamento, histórias de usuário); UML (diagramas estruturais e comportamentais, casos de uso com include e extend, classes, sequência, atividades e estados); testes de software (níveis, caixa-preta e caixa-branca, regressão, verificação e validação, TDD); Manifesto Ágil; Scrum segundo o Guia de 2020; Kanban; XP; noções de DevOps, integração e entrega contínuas.
Fica de fora (outras matérias tratam): Gerenciamento de projetos pelo PMBOK, ITIL e COBIT, métricas como pontos de função em detalhe, CMMI e MPS.BR em detalhe, SAFe e outros frameworks de escala, ferramentas específicas de DevOps (Docker, Kubernetes, Jenkins) e programação em linguagens específicas.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Engenharia de requisitos.
- Metodologias Ágeis.
- Engenharia de requisitos: Produto mínimo viável (MVP).
- Engenharia de requisitos: Gestão de backlog.
- Qualidade: Teste unitário.
- Qualidade: Teste de integração.
- Análise e projeto orientados a objetos.
- Técnicas de priorização, de estimativas (Análise de Pontos de Função, Story Points).
- Análise e projeto.
- Engenharia de software: Unified Modeling Language (UML).
- Metodologias ágeis para o desenvolvimento de software: Scrum, XP, Lean.
- Engenharia de software: Desenvolvimento orientado a testes (TDD).
- Engenharia de software: Testes automatizados.
- Processos de desenvolvimento de software.
- Técnicas de validação de requisitos.
- Técnicas de Elicitação de Requisitos.
- Metodologias de desenvolvimento de software.
- UML: visão geral, modelos e diagramas.
- Kanban.
- Qualidade de software.
- Análise de requisitos, especificação, ambientes de testes, homologação, produção e suporte.
- Engenharia de Software: ciclo de vida do software.
- Noções sobre desenvolvimento e manutenção de sistemas e aplicações.
- Noções sobre metodologias de análise, projeto e desenvolvimento de sistemas.
- Engenharia de requisitos, gestão de backlog e produto mínimo viável.
- Gerenciamento de produtos de software por métodos Scrum, Kanban, XP e Lean.
- Desenvolvimento orientado a testes (TDD).
- Especificação de requisitos.
- Scrum.
- Metodologias ágeis, lean manufacturing e Scrum.

==================================================
# BANCO 140 — matematica.juros-simples-compostos
==================================================
**Matéria:** Matemática e Raciocínio Lógico: Juros simples e compostos (id: `matematica.juros-simples-compostos`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Capital, juros, montante, taxa e prazo em regimes simples e compostos; conversão de taxa e período; cálculos diretos, determinação de variável e comparação entre regimes.
Fica de fora (outras matérias tratam): Descontos simples ou compostos, equivalência avançada de taxas, séries uniformes, amortização, inflação e aportes periódicos.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Juros simples.
- Juros compostos.
- Juros simples e compostos.
- Capitalização.
- Juros simples e compostos: capitalização e descontos.
- Cálculo financeiro.
- Juros simples: montante e juros.
- Juros compostos: montante e juros.
- Capitalização contínua.
- Conceitos gerais: valor do dinheiro no tempo, valor presente, valor futuro, juro, taxa de juro, prazo da operação.
- Juros simples: cálculo do montante, dos juros, da taxa, do principal e do prazo.
- Juros compostos: cálculo do montante, dos juros, da taxa, do principal e do prazo.
- Juros simples e compostos: cálculos e aplicações.
- Matemática financeira aplicada: Juros simples.
- Matemática financeira aplicada: Juros compostos.
- Juros compostos. Montante e juros. Taxa real e taxa efetiva. Taxas equivalentes. Capitais equivalentes.
- Montante e juros.
- Conceito de juros e regimes de capitalizações.
- Capitalização simples: cálculo de juros e montantes.
- Capitalização composta: cálculo de juros e montantes.
- Matemática Financeira / Estatística Matemática Financeira: Regimes de capitalização em juros simples e compostos.
- Montante, juros e número de períodos.
- Juros simples e compostos: capitalização e desconto.
- Juros simples e juros compostos.
- Valor do dinheiro no tempo, capital, juros, taxas, capitalização e carência.
- Juros simples e compostos: montante, juros, taxas, principal e prazo.
- Juros simples e compostos, capitalização, descontos e taxas de juros.
- Juros simples e compostos, capitalização, desconto e taxas nominais, efetivas, equivalentes, reais e aparentes.
- Matemática financeira: juros simples e compostos e precificação e operações com títulos públicos federais.
- Juros simples: capitalização.

==================================================
# BANCO 141 — raciocinio-logico.relacoes-arbitrarias
==================================================
**Matéria:** Raciocínio Lógico e Matemático: Estrutura lógica de relações e dedução (id: `raciocinio-logico.relacoes-arbitrarias`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Modelagem de relações por tabelas, associação entre pessoas, funções, locais e objetos, ordenação linear e circular, posições, verdades e mentiras, proposições, conectivos, tabelas-verdade, negações, equivalências, contrapositiva, quantificadores, diagramas e validade de argumentos.
Fica de fora (outras matérias tratam): Cálculo de probabilidade, análise combinatória avançada e lógica matemática formal além do necessário para interpretar premissas e deduzir conclusões.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios.
- Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios; dedução de novas informações das relações fornecidas e avaliação das condições usadas para estabelecer a estrutura daquelas relações.
- Deduzir novas informações das relações fornecidas e avaliar as condições usadas para estabelecer a estrutura daquelas relações.
- Dedução de novas informações das relações fornecidas e avaliação das condições usadas para estabelecer a estrutura daquelas relações.
- Dedução de novas informações das relações fornecidas e avaliação das condições usadas para estabelecer a estrutura daquelas relações Compreensão de dados apresentados em gráficos e tabelas.
- Relações arbitrárias e dedução de novas informações.
- Estrutura lógica de relações arbitrária entre pessoas, lugares, objetos ou eventos fictícios.
- Dedução de novas informações daquelas relações.
- Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios; deduzir novas informações das relações fornecidas e avaliar as condições usadas para estabelecer a estrutura daquelas relações.
- Raciocínio lógico e estruturas lógicas.
- Relações arbitrárias, dedução, gráficos e tabelas.
- Relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios e dedução de novas informações.
- Medidas e relações lógicas com dedução de informações.
- Estruturas lógicas de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios.
- Raciocínio Lógico: Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios.
- Estruturas lógicas e dedução.
- Dedução de novas informações a partir das relações fornecidas.
- Avaliação das condições utilizadas para estabelecer a estrutura lógica das relações apresentadas.
- Estrutura lógica de relações arbitrárias entre pessoas, lugares, objetos ou eventos fictícios;
- Proposições, conectivos, equivalências, quantificadores e predicados.
- Estrutura lógica de relações arbitrárias e dedução de novas informações.
- Relações arbitrárias, dedução de informações e avaliação das condições lógicas.
- Relações arbitrárias e dedução de informações e avaliação de condições lógicas.
- Relações arbitrárias, dedução de informações e condições lógicas.
- Relações arbitrárias, dedução de informações e avaliação de condições lógicas.
- Relações arbitrárias, dedução de informações e avaliação de condições.
- Relações lógicas e dedução de novas informações.
- Relações lógicas arbitrárias e dedução de novas informações.
- Estruturas de relações, dedução, raciocínio verbal, matemático e sequencial.
- Relações lógicas arbitrárias, dedução de informações e avaliação de condições.

==================================================
# BANCO 142 — processo-civil.remedios-constitucionais-mandado-seguranca-habeas
==================================================
**Matéria:** Direito Processual Civil: Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção e ação popular (id: `processo-civil.remedios-constitucionais-mandado-seguranca-habeas`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Habeas corpus, mandado de segurança individual e coletivo, habeas data, mandado de injunção, ação popular, ação civil pública, recursos cíveis em geral e meios extrajudiciais de solução de conflitos na Administração Pública.
Fica de fora (outras matérias tratam): Procedimentos penais específicos, recursos de legislação especial não indicados e estudo exaustivo de arbitragem privada.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Ação civil pública.
- Habeas corpus, mandado de segurança, mandado de injunção e habeas data.
- Mandado de segurança individual e coletivo.
- Mandado de segurança.
- Procedimentos especiais: mandado de segurança, ação popular, ação civil pública, ação de improbidade administrativa.
- Habeas Corpus.
- Mandado de Segurança Coletivo.
- Mandado de segurança, mandado de injunção, ação popular, habeas data e habeas corpus.
- Remédios constitucionais: habeas data, habeas corpus, mandado de segurança, ação popular e mandado de injunção.
- Ações diversas: Mandado de segurança.
- Ações diversas: Ação civil pública.
- Direitos e garantias fundamentais: habeas corpus, mandado de segurança, mandado de injunção e habeas data.
- Processo coletivo e tutela de direitos difusos, coletivos e individuais homogêneos.
- Ações coletivas.
- Mandado de segurança, ação popular, ação civil pública, improbidade, mandado de injunção e habeas data.
- Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção, Ação popular.
- Procedimentos extrajudiciais de solução de conflitos na Administração Pública.
- Ação civil pública (Lei nº 7.347/1985 e alterações).
- Ação civil pública (Lei nº 7.347/1985 e suas alterações).
- Processos nos tribunais, recursos, mandado de segurança, ação popular, ação civil pública e improbidade.
- Remédios constitucionais: habeas-corpus, mandado de segurança.
- Ações específicas: Ação civil pública.
- – Legislação Extravagante: Lei nº 7.347/85 (Ação civil pública).
- Lei nº 12.016/2009 (Mandado de Segurança).
- Ação civil pública, ação popular e ação de improbidade administrativa.
- Habeas corpus, habeas data e mandado de injunção.
- Mandado de segurança, mandado de injunção, habeas data e mandado de segurança coletivo.
- Processo civil no controle de constitucionalidade, ações constitucionais e declaração incidental de inconstitucionalidade.
- Tutela judicial ambiental, ação popular, ação civil pública, mandados constitucionais e tutela de urgência.
- O processo civil e o controle judicial dos atos administrativos: mandado de segurança; Ação popular; Ação civil pública; Ação de improbidade administrativa.

==================================================
# BANCO 143 — matematica.porcentagem
==================================================
**Matéria:** Matemática e Raciocínio Lógico: Porcentagem e variação percentual (id: `matematica.porcentagem`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Porcentagem, frações e decimais; variação percentual e pontos percentuais; fatores sucessivos; lucro e prejuízo com bases explícitas.
Fica de fora (outras matérias tratam): Juros, capitalização e demais matemática financeira com remuneração do capital pelo tempo.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Porcentagem.
- Porcentagens.
- Porcentagem e problemas envolvendo regra de três simples, cálculos de porcentagem, acréscimos e descontos.
- Razões e proporções: Porcentagens.
- Porcentagem e problemas.
- Porcentagem e juros.
- Cálculos com porcentagens.
- Porcentagem; problemas.
- Números, operações, porcentagem, juros e proporcionalidade.
- Números reais e operações, porcentagem e juros.
- Conjuntos, números, porcentagem, juros e proporcionalidade.
- Porcentagem e variações percentuais.
- Matemática financeira: Porcentagem.
- Matemática financeira: Descontos e acréscimos.
- Porcentagem e proporcionalidade.
- Percentagem, variação percentual e operações sobre mercadorias.
- Divisão proporcional: Porcentagens.
- Matemática financeira: percentagens.

==================================================
# BANCO 144 — direito-civil.obrigacoes
==================================================
**Matéria:** Direito Civil: Direito das obrigações (id: `direito-civil.obrigacoes`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Modalidades de dar, fazer, não fazer, alternativas e facultativas; divisibilidade e solidariedade; cessão de crédito, assunção de dívida; pagamento e modos especiais de extinção; inadimplemento, mora, perdas e danos, juros legais e cláusula penal.
Fica de fora (outras matérias tratam): Contratos em geral, responsabilidade civil extracontratual e prescrição como matérias autônomas.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Obrigações.
- Transmissão das obrigações.
- Obrigações: Características.
- Adimplemento pelo pagamento.
- Inadimplemento das obrigações – disposições gerais e mora.
- Adimplemento e extinção das obrigações.
- Inadimplemento das obrigações.
- Preferências e privilégios creditórios.
- Obrigações solidárias.
- Direito das obrigações.
- Obrigações de execução instantânea, diferida e continuada.
- Obrigações puras e simples, condicionais, a termo e modais.
- Obrigações de dar.
- Atos unilaterais.
- Obrigações de fazer e de não fazer.
- Obrigações divisíveis e indivisíveis.
- Obrigações líquidas e ilíquidas.
- Obrigações principais e acessórias.
- Do Direito das Obrigações.
- Obrigações civis e naturais, obrigações de meio, de resultado e de garantia.
- Características.
- Cláusula penal.
- Obrigações: transmissão das obrigações.
- Obrigações: adimplemento e extinção das obrigações.
- Obrigações: inadimplemento das obrigações.
- Obrigações alternativas.
- Enriquecimento sem causa.
- Mora.
- Do inadimplemento das obrigações.
- Obrigações: modalidades, transmissão, adimplemento, extinção, inadimplemento e atos unilaterais.

==================================================
# BANCO 145 — direito-constitucional.competencias-intervencao
==================================================
**Matéria:** Direito Constitucional: Repartição de competências e intervenção (id: `direito-constitucional.competencias-intervencao`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Competências materiais exclusivas da União (art. 21); competências legislativas privativas da União e delegação a Estados (art. 22); competências administrativas comuns (art. 23); competências legislativas concorrentes e regras de suplementação (art. 24); competências remanescentes dos Estados (art. 25); competências municipais de interesse local e suplementação (art. 30); competências do Distrito Federal (art. 32, § 1º); intervenção federal e estadual, hipóteses, iniciativa, decreto e controle (arts. 34 a 36).
Fica de fora (outras matérias tratam): Organização dos Poderes, processo legislativo geral, repartição tributária e competências específicas de órgãos ou políticas fora dos arts. 21 a 30 e das regras de intervenção dos arts. 34 a 36.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Organização do Estado: Intervenção federal.
- Organização do Estado: Intervenção dos estados nos municípios.
- Das competências da União, dos Estados e dos Municípios.
- Intervenção federal.
- O Processo Legislativo na Constituição da República de 1988: competências constitucionais exclusivas, concorrentes e privativas no ato de legislar.
- Ação Direta de Inconstitucionalidade Interventiva.
- Intervenção dos estados nos municípios.
- Repartição de competências.
- Intervenção do Estado nos Municípios.
- Intervenção.
- Divisão de competências entre os entes federados.
- Intervenção federal nos estados.
- Intervenção nos Municípios.
- Intervenção federal e estadual.
- Organização do Estado, repartição de competências, federalismo e intervenção.
- Intervenção federal e intervenção dos Estados nos Municípios.
- Organização político-administrativa: competências da União, estados e municípios.
- Organização político administrativa: federação brasileira, competências da União, estados e municípios, intervenção federal.
- Da intervenção.
- Repartição de competências na Federação e suas técnicas.
- A repartição de competência na Constituição de 1988.
- Intervenção federal nos municípios.
- Estado federal, repartição de competências e Federação brasileira.
- Organização político-administrativa do Estado: competências da União, Estados, Distrito Federal e Municípios.
- Sistemas de repartição de competência.
- Discriminação de competência na Constituição de 1988.
- União: competência.
- Atribuições, competências e relações entre esferas governamentais no regime federativo.
- Competências e relações entre esferas de governo no regime federativo.
- Atribuições, competências e relações entre esferas de governo no regime federativo na CF/88.

==================================================
# BANCO 146 — legislacao.igualdade-racial-crimes-preconceito
==================================================
**Matéria:** Legislação: Igualdade racial e crimes de preconceito (id: `legislacao.igualdade-racial-crimes-preconceito`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Estatuto da Igualdade Racial (Lei nº 12.288/2010) e Lei nº 7.716/1989: igualdade de oportunidades, direitos étnicos, discriminação e preconceito por raça, cor, etnia, religião ou procedência nacional, crimes de preconceito, injúria racial e interpretação constitucional sobre homotransfobia.
Fica de fora (outras matérias tratam): Debates sociológicos sem conexão com as leis indicadas, discriminações por motivos fora do recorte, legislação local e procedimentos penais não relacionados diretamente aos dispositivos estudados.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Estatuto da Igualdade Racial.
- Lei nº 12.288/2010 (Estatuto da Igualdade Racial).
- Lei nº 7.716/1989 (crimes resultantes de preconceitos de raça ou de cor).
- Estatuto de igualdade racial (Lei n.º 12.288/2010).
- Estatuto de igualdade racial (Lei nº 12.288/2010 e suas alterações).
- Crimes resultantes de preconceito de raça ou de cor (Lei nº 7.716/1989).
- Lei Federal n° 12.288/2010 - Estatuto da Igualdade Racial.
- Lei Federal nº 7.716/1989 - Define os crimes resultantes de preconceito de raça ou de cor.
- Lei Federal nº 7.437/1985 - Lei Caó.
- Lei Federal nº 7.716/1989 - Crimes resultantes de preconceito de raça, cor, etnia, religião ou procedência nacional.
- Lei nº 7.716/1989 e suas alterações (Crimes resultantes de preconceitos de raça ou de cor).
- Estatuto da Igualdade Racial (Lei federal nº 12.288, de 20 de julho de 2010).
- Lei federal nº 7.716, de 5 de janeiro de 1989 (define os crimes resultantes de preconceito de raça ou de cor) e Lei federal nº 9.459, de 13 de maio de 1997 (tipificação dos crimes resultantes de preconceito de raça ou de cor).
- Lei Caó (Lei federal nº 7.437, de 20 de dezembro de 1985).
- Ações afirmativas.
- Legislação Especial: Crimes resultantes de preconceitos de raça ou de cor (Lei nº 7.716/1989 e Lei nº 14.532/2023).
- Diversidade étnico-racial.
- Estatuto Nacional da Igualdade Racial. Lei nº 10.973/2004.
- Lei nº 7.716/1989 e suas alterações (preconceito de raça ou cor).
- Lei 7.716/1989 (Crimes resultantes de preconceitos de raça ou cor).
- Lei 12.288/2010 (Estatuto da Igualdade Racial).
- Crimes resultantes de preconceito de raça ou de cor (Lei nº 7.716/1989 e suas alterações).
- Crimes de preconceito (Lei nº 7.716/1989 e suas alterações).
- Conceitos Fundamentais do Racismo, Sexismo, Intolerância Religiosa, LGBTQIA+fobia.
- Crime: Lei nº 7.716/1989 e alterações (crimes resultantes de preconceitos de raça ou de cor).
- Legislação penal especial: racismo.
- Crime de preconceito (Lei nº 7.716, de 5/1/1989).
- Estatuto Nacional da Igualdade Racial.
- Lei nº 7.716/1989: crimes resultantes de preconceito de raça ou de cor.
- Leis Federais n. 7.716/1989.

==================================================
# BANCO 147 — matematica.graficos-tabelas-mapas-escalas
==================================================
**Matéria:** Matemática: Leitura de gráficos, tabelas, mapas e escalas (id: `matematica.graficos-tabelas-mapas-escalas`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Leitura e comparação de tabelas e gráficos; frequências e percentuais apresentados; séries estatísticas e temporais; leitura de mapas e plantas; escala numérica e gráfica; conversão entre medidas e cálculo de distâncias reais ou representadas.
Fica de fora (outras matérias tratam): Estatística inferencial, cartografia técnica avançada, projeções cartográficas complexas e elaboração de pesquisas amostrais.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Compreensão de dados apresentados em gráficos e tabelas.
- Leitura e interpretação de gráficos (histogramas, setores, infográficos) e tabelas.
- Interpretação de gráficos e tabelas.
- Compreensão de dados em gráficos e tabelas.
- Gráficos e tabelas.
- Interpretação de gráficos e tabelas (dados estatísticos).
- Séries estatísticas.
- Interpretação de dados em gráficos e tabelas.
- Relação entre grandezas: tabelas e gráficos.
- Análise de gráficos e tabelas (interpretação de dados).
- Interpretação de gráficos.
- Representações em gráficos e tabelas (linhas, colunas, setores e histogramas).
- Gráficos, tabelas, problemas aritméticos, geométricos e matriciais.
- Leitura de dados apresentados em gráficos e tabelas.
- Construção e interpretação de gráficos e tabelas.
- Estatística e representações gráficas: leitura e interpretação de tabelas e gráficos (barras, setores, linhas).
- Relação entre grandezas, tabelas e gráficos.
- Interpretação de figuras, desenhos, mapas, gráficos, tabelas, séries e plantas; escalas.
- Análise e interpretação de desenhos, mapas, gráficos, tabelas, séries estatísticas, séries temporais e plantas; utilização de escalas.
- Figuras planas, desenhos, mapas e plantas; escalas; figuras espaciais, projeções, planificações e cortes.
- Gráficos (histogramas, setores, infográficos).
- Noções de estatística: Gráficos.
- Noções de estatística: Tabelas.
- Representação de dados em gráficos e tabelas.
- Quadros, tabelas e gráficos: tipos e interpretação.

==================================================
# BANCO 148 — processo-civil.sujeitos-processo
==================================================
**Matéria:** Direito Processual Civil: Sujeitos do processo (id: `processo-civil.sujeitos-processo`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Partes e procuradores (capacidade, deveres, litigância de má-fé, ato atentatório, despesas, honorários, gratuidade, procuração, sucessão); litisconsórcio; intervenção de terceiros (assistência, denunciação da lide, chamamento ao processo, desconsideração da personalidade jurídica, amicus curiae); juiz (poderes, deveres, responsabilidade, impedimento e suspeição); auxiliares da justiça; Ministério Público, Defensoria Pública e advocacia pública no processo civil.
Fica de fora (outras matérias tratam): Jurisdição e competência, ação e suas condições, atos e prazos processuais em geral, petição inicial, procedimento comum e recursos, tratados em matérias próprias.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Sujeitos do processo.
- Intervenção de terceiros.
- Litisconsórcio e assistência.
- Sujeitos do processo: Capacidade processual e postulatória.
- Sujeitos do processo: Procuradores.
- Sujeitos do processo: Sucessão das partes e dos procuradores.
- Partes e Procuradores.
- Auxiliares da Justiça.
- Litisconsórcio.
- Intervenção de terceiros: oposição, nomeação à autoria, denunciação à lide e chamamento ao processo.
- Da intervenção de terceiros.
- Sujeitos do processo: Deveres das partes e procuradores.
- Poderes, deveres e responsabilidade do juiz.
- Sujeitos do processo: Litisconsórcio.
- Juiz.
- O juiz.
- Partes e procuradores: capacidade processual e postulatória.
- Amicus curiae.
- Substituição processual.
- Sujeitos do processo: Partes e procuradores.
- Deveres e substituição das partes e procuradores.
- Litisconsórcio e intervenção de terceiros.
- Magistratura.
- Do Ministério Público.
- Advocacia.
- Das partes e procuradores: da capacidade processual e postulatória.
- Do litisconsórcio e da assistência.
- Do Juiz.
- Impedimento e suspeição.
- Capacidade processual e postulatória.

==================================================
# BANCO 149 — informatica.arquitetura-software
==================================================
**Matéria:** Informática: Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto (id: `informatica.arquitetura-software`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Conceito de arquitetura de software, componentes, conectores e atributos de qualidade; estilos arquiteturais (cliente-servidor, ponto a ponto, duto e filtro, orientada a eventos, serverless em noções); arquitetura em camadas (apresentação, negócio e dados), camadas lógicas x físicas, camadas estritas x relaxadas, vantagens e desvantagens; MVC, fluxo entre Model, View e Controller, MVC na web e front controller, MVC x três camadas, MVP e MVVM; noções de arquitetura hexagonal e Clean Architecture; monólito, SOA e microsserviços (características, comunicação síncrona e assíncrona, banco por serviço, API gateway, service discovery, circuit breaker, saga, consistência eventual, strangler fig, lei de Conway); padrões de projeto GoF (propósito, escopo, os 23 padrões de criação, estruturais e comportamentais, com exemplos em Java) e noções de GRASP.
Fica de fora (outras matérias tratam): Fundamentos e pilares da orientação a objetos e princípios SOLID (em informatica.orientacao-objetos); UML, modelos de processo e métodos ágeis (em informatica.engenharia-software-ageis); HTTP, métodos, códigos de resposta e APIs em detalhe (em informatica.python-r-api); SOAP, WSDL e UDDI em detalhe; Docker, Kubernetes e ferramentas de nuvem; frameworks específicos (Spring, Java EE/Jakarta EE) e padrões Java EE; DDD em profundidade.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Arquitetura de software.
- Arquitetura de software: Interoperabilidade de sistemas.
- Arquitetura de software: Arquitetura orientada a serviços.
- Arquitetura orientada a serviços: Web services.
- Arquitetura de software: Arquitetura orientada a objetos.
- Arquitetura de software: Arquitetura de aplicações para ambiente web.
- Arquitetura de aplicações para ambiente web: Servidor de aplicações.
- Noções de Arquitetura SOA (Service Oriented Architecture).
- Padrões de projeto.
- Noções de Arquitetura Cliente-Servidor.
- Engenharia de software: Padrões de projeto e SOLID.
- SOA e Web Services: UDDI, WSDL e SOAP.
- Desenho de arquitetura de soluções.
- Camadas de Aplicação, processos, frontend, backend.
- Arquitetura cliente-servidor multicamadas.
- Design Patterns.
- Arquitetura MVC.
- SOA e web services: conceitos básicos e aplicações.
- Padrões de desenvolvimento e reuso.
- Arquitetura de software. Interoperabilidade de sistemas.
- Design de software: arquitetura hexagonal, microsserviços (orquestração de serviços e API gateway) e containers.
- Arquitetura.
- Arquiteturas de integração, SOA, Webservices e REST.
- Padrão MVC.
- Arquitetura de desenvolvimento da Plataforma Digital do Poder Judiciário (PDPJ-Br): a) Arquitetura distribuída de microsserviços: API RESTful;
- Arquitetura Limpa (Clean Architecture).
- Padrão MVC (Model-View-Controller) aplicado à web.
- Arquiteturas em camadas, baseada em serviços, microsserviços (orquestração de serviços e API gateway), orientação a eventos, cliente-servidor, serverless.
- Padrões: GoF.
- Padrões: GRASP.

==================================================
# BANCO 150 — lingua-inglesa.gramatica-para-leitura
==================================================
**Matéria:** Língua Inglesa: Gramática inglesa para leitura (id: `lingua-inglesa.gramatica-para-leitura`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Estrutura da frase, artigos, classes e formação de palavras; concordância essencial; tempos e aspectos verbais; modais e condicionais; voz passiva; pronomes e referentes; conectores; comparativos e superlativos; preposições; inferências de leitura e falsos cognatos frequentes.
Fica de fora (outras matérias tratam): Conversação, produção de textos extensos, fonética avançada e vocabulário especializado de área profissional.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Itens gramaticais relevantes para a compreensão dos conteúdos semânticos.
- Itens gramaticais relevantes para compreensão de conteúdos semânticos.
- Conhecimento e uso das formas contemporâneas da linguagem inglesa.
- Itens gramaticais relevantes para a compreensão de conteúdos semânticos.
- Aspectos gramaticais básicos para a compreensão de textos.
- Vocabulário fundamental e aspectos gramaticais básicos para a compreensão de textos.
- Tempos e modos verbais.
- Uso de preposições, conjunções, pronomes e modais.
- Voz passiva, discurso direto e indireto.
- Aspectos sintático-gramaticais: artigos definidos e indefinidos, tempos e modos verbais, preposições, conjunções, pronomes, modais, concordância nominal e verbal, formação e classe de palavras.
- Expressões idiomáticas, subordinação e coordenação, voz passiva, discurso direto e indireto.
- Itens gramaticais relevantes à compreensão semântica.
- Aspectos sintático-gramaticais relevantes à compreensão de texto: artigos definidos e indefinidos.
- Formação e classe de palavras.
- Relações de subordinação e coordenação.
- Itens gramaticais relevantes à compreensão dos conteúdos semânticos.
- Vocabulário e morfossintaxe da língua inglesa.
- Itens gramaticais relevantes para compreensão semântica e formas contemporâneas do inglês.
- Domínio do vocabulário e da estrutura da língua.
- Itens gramaticais relevantes para compreensão semântica.
- Itens gramaticais relevantes para a compreensão semântica.
- Itens gramaticais relevantes para a compreensão de conteúdos semânticos em língua inglesa.
- Aspectos sintático-gramaticais: artigos definidos e indefinidos, tempos e modos verbais, preposições, conjunções, pronomes e modais.
- Concordância nominal e verbal, formação e classe de palavras, expressões idiomáticas, subordinação e coordenação, voz passiva, discurso direto e indireto.
- Estruturas gramaticais: adjectives, adverbs, nouns, articles, conjunctions, modals, prepositions, pronouns, verb tenses, passive voice e wh-questions.
- Elementos de referência.
- Itens gramaticais relevantes para compreensão dos conteúdos semânticos.
- Vocabulário e estruturas gramaticais necessários à compreensão de textos.
- Itens gramaticais relevantes para a compreensão dos conteúdos semânticos (inglês).
- Aspectos gramaticais relevantes à compreensão de texto.
