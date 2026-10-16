# META: BANCO 052 completo (direito-administrativo.servico-publico)

Sua meta é entregar os **6 lotes do BANCO 052**, cada um com **50 questões** de múltipla escolha A–E, seguindo as REGRAS e a FICHA abaixo:

| Lote | Estilo | Arquivo |
|---|---|---|
| 1 | FGV | direito-administrativo.servico-publico.banco2-1.json |
| 2 | FCC | direito-administrativo.servico-publico.banco2-2.json |
| 3 | VUNESP | direito-administrativo.servico-publico.banco2-3.json |
| 4 | CESGRANRIO | direito-administrativo.servico-publico.banco2-4.json |
| 5 | INSTITUTOS | direito-administrativo.servico-publico.banco2-5.json |
| 6 | DESAFIO (100% difícil) | direito-administrativo.servico-publico.banco2-6.json |

Como trabalhar:
1. **Escreva cada questão à mão, uma por uma, sem código, script ou ferramenta de análise para montar o texto** (veja a seção "PROIBIDO montar questões por programa" nas regras). Gere um lote por vez, entregando cada um como **arquivo JSON separado** com o nome da tabela (se não puder criar arquivo, um bloco JSON por lote, com o nome do arquivo antes).
2. Ao terminar um lote, comece o seguinte, até completar o lote 6, sem pedir confirmação. Se um lote não couber com qualidade numa resposta, entregue em duas partes (25 + 25); prefira isso a encurtar ou automatizar.
3. Não repita casos, textos, personagens ou pontos de um lote para outro; ao longo dos 6 lotes, cubra todos os tópicos da ficha.
4. Pesquise na web para conferir lei, jurisprudência e documentação vigentes.
5. Antes de entregar cada lote, resolva cada questão como candidato e confirme que só uma alternativa é defensável e que a distribuição de dificuldade, gabarito e tipos está dentro das regras.
6. Se a resposta for cortar, feche o JSON do lote atual num ponto limpo e continue na resposta seguinte de onde parou, sem recomeçar.
7. No fim, liste os 6 arquivos entregues e quantas questões tem cada um.

########## REGRAS ##########
# BANCO 2 — Regras (valem para todas as matérias e lotes)

Você é autor de questões de concurso público no Brasil, no nível dos melhores cursinhos. Vai criar um BANCO DE QUESTÕES DIFÍCEIS para o aplicativo Estudário, sempre de **múltipla escolha com 5 alternativas (A a E)**. Não há Certo/Errado neste banco. Pesquise na web e use fontes oficiais (lei vigente, jurisprudência atual, documentação oficial).

Cada matéria tem uma ficha (escopo + tópicos dos editais). Quando eu pedir **"BANCO NNN, lote L"**, entregue só aquele lote, com **50 questões**, sem repetir casos, textos ou pontos já usados nos lotes anteriores da mesma matéria.

## PROIBIDO montar questões por programa
- **Escreva cada questão individualmente, como um professor escreveria à mão.** É proibido usar código, script, Python, planilha, ferramenta de análise de dados ou qualquer combinação automática para gerar, montar, embaralhar, completar ou "equilibrar" questões, alternativas, comentários ou explicações. Use ferramentas só para pesquisar fontes; o texto das questões sai da sua escrita, uma por uma.
- **Nada de banco de fatos reciclado:** cada questão tem fatos, personagens, números e situação **próprios**. É proibido reaproveitar o mesmo fato mudando só a frase de abertura ("Ao analisar os autos...", "No controle de um evento...", "Para aplicação direta da regra, considere:"). Em uma matéria inteira, um mesmo caso não pode aparecer em mais de uma questão.
- **Proibido texto-molde:** nenhuma frase de abertura, de fechamento de explicação ("Dica de prova: ...") ou de comentário pode se repetir mais de 2 vezes no lote. Comentários não seguem fórmula ("O erro específico está na tese de que... o caso exige aplicar o critério..."): cada um explica, com palavras próprias, o erro daquela alternativa.
- **O enunciado não entrega a resposta:** não liste no enunciado os fundamentos, artigos, súmulas ou temas que resolvem a questão ("Referências para análise: ...", "Bases normativas: ..."). Saber qual norma se aplica faz parte da questão.
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

########## FICHA ##########
==================================================
# BANCO 052 — direito-administrativo.servico-publico
==================================================
**Matéria:** Direito Administrativo: Serviço público: conceito, princípios e classificação (id: `direito-administrativo.servico-publico`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Conceito e titularidade do serviço público; princípios da continuidade, adequação e modicidade; classificação uti singuli e uti universi; Lei 13.460/2017 e direitos, deveres, manifestações, ouvidorias e Carta de Serviços do Usuário.
Fica de fora (outras matérias tratam): Concessão, permissão, autorização como formas de delegação, política tarifária detalhada, intervenção e extinção de contratos, que pertencem a tópico próprio.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Serviços públicos.
- Serviços públicos: conceito, classificação, regulamentação, formas e competência de prestação.
- Serviços públicos: Conceito.
- Classificação.
- Princípios.
- Serviços públicos: Elementos constitutivos.
- Serviços públicos: Formas de prestação e meios de execução.
- Serviços públicos: Classificação.
- Serviços públicos: Princípios.
- Serviço público.
- Direitos dos usuários de serviço público.
- Formas de prestação e meios de execução.
- Formas de prestação e meios de execução dos serviços públicos.
- Serviços públicos: conceito, elementos, prestação, delegação, classificação e princípios.
- Serviços públicos e direitos dos usuários: Lei nº 13.460/2017.
- Usuário do serviço público.
- Serviços públicos: conceito e princípios.
- Serviços públicos. Conceito. Elementos constitutivos.
- Classificação dos serviços públicos. Princípios.
- Serviços Públicos: conceito, classificação, regulamentação e controle; forma, meios e requisitos.
- Elementos constitutivos.
- Conceito de serviço público.
- Serviços públicos: conceito de serviço público.
- Serviços públicos: caracteres jurídicos.
- Serviços públicos: classificação e garantias.
- Serviços públicos: usuário do serviço público.
- Serviços públicos: conceito, elementos, prestação, execução, delegação, classificação, princípios e Lei nº 13.460/2018.
- Serviços públicos: conceito e elementos constitutivos.
- Serviços Públicos: conceitos, princípios, classificação, formas de prestação e extinção.
- Serviços públicos: conceito, elementos, formas de prestação, delegação, classificação e princípios.
