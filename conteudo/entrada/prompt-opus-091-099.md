# Pedido: BANCO 2 — matérias 091 a 099 (Opus)

Você vai escrever, à mão, os bancos das 9 matérias abaixo (091 a 099), 6 lotes de 50 questões cada (300 por matéria). As regras completas e as fichas estão neste arquivo.

## Como trabalhar
- Faça **um lote por resposta**, começando por **BANCO 091, lote 1**. No fim de cada resposta escreva só: "Próximo: BANCO 091, lote 2" (e assim por diante). Eu respondo "continue".
- Entregue cada lote como **arquivo JSON para baixar** com o nome `<id-da-materia>.banco2-<L>.json` (ex.: `direito-administrativo.concessao-permissao.banco2-1.json`). Se não puder criar arquivo, um único bloco ```json, sem nenhum texto fora dele.
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
# BANCO 091 — direito-administrativo.concessao-permissao
==================================================
**Matéria:** Direito Administrativo: Concessão e permissão de serviço público (id: `direito-administrativo.concessao-permissao`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Lei 8.987/1995 vigente: concessões e permissões, licitação, tarifa, encargos, intervenção, extinção, reversão de bens e distinções pertinentes sobre autorização.
Fica de fora (outras matérias tratam): Teoria geral completa de serviços públicos, direitos do usuário fora do necessário à adequação e regimes setoriais especiais não previstos na Lei 8.987.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Delegação: concessão, permissão e autorização.
- Concessão, permissão e autorização de serviços públicos.
- Lei nº 8.987/1995.
- Serviços públicos: Delegação.
- Formas de delegação de serviço público.
- Extinção, reversão dos bens.
- Concessão, permissão e autorização.
- Extinção da concessão de serviço público e reversão dos bens.
- Lei nº 8.987/1995 (Lei de Concessões).
- Serviços públicos: regulação, concessão, permissão e autorização do serviço público.
- Formas de prestação e meios de execução: Delegação.
- Permissão e autorização.
- Serviços públicos: concessão, permissão, autorização e delegação.
- Delegação de serviços públicos: concessão, permissão, autorização.
- Regime jurídico da concessão e da permissão de serviço público.
- Delegação.
- Delegação de serviços públicos: concessão, permissão e autorização.
- Serviços delegados.
- Serviços públicos: serviços delegados.
- Serviços públicos: permissão e autorização.
- Parceria Público-Privada: Lei nº 8.987/1995, que dispõe sobre o regime de concessão e permissão da prestação de serviços públicos e Lei nº 11.079/2004, que institui normas gerais para licitação e contratação de parceria público-privada no âmbito da administração pública.
- Delegação por concessão, permissão e autorização.
- Lei nº 8.987/1995 e alterações.
- Concessão e permissão de serviços públicos (Lei nº 8.987/1995 e suas alterações).
- Concessão, permissão, autorização e delegação.
- Delegação de serviços públicos por autorização, permissão e concessão.
- Permissão, concessão e autorização de serviços públicos.
- Serviços públicos: extinção da concessão de serviço público e reversão dos bens.
- Concessões e permissões de serviços públicos.
- Conceito, características.

==================================================
# BANCO 092 — direito-constitucional.direitos-sociais
==================================================
**Matéria:** Direito Constitucional: Direitos sociais (arts. 6º a 11) (id: `direito-constitucional.direitos-sociais`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Direitos sociais do art. 6º; direitos dos trabalhadores do art. 7º em visão geral; liberdade sindical, greve e participação laboral dos arts. 8º a 11; reserva do possível e mínimo existencial.
Fica de fora (outras matérias tratam): Direitos e garantias individuais do art. 5º, regimes legais trabalhistas detalhados e controvérsias processuais sobre dissídios coletivos.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Direitos sociais.
- Direitos e garantias fundamentais: Direitos sociais.
- Dos direitos sociais.
- Direitos e garantias fundamentais: Dos Direitos Sociais.
- Princípios constitucionais do trabalho.
- Direitos sociais e sua efetivação.
- Dos direitos e garantias fundamentais: dos direitos sociais.
- Direitos e deveres fundamentais: Direitos sociais, nacionalidade, cidadania e direitos políticos.
- Direitos e deveres fundamentais: Direitos sociais e sua efetivação.
- Direitos sociais, dignidade do trabalhador e proteção constitucional ao trabalho.
- Liberdade e organização sindical, contribuição sindical e direito de greve.
- Direitos sociais e sua efetivação, direito à saúde e Sistema Único de Saúde.
- Poder constituinte: Direitos sociais.
- Direitos e garantias fundamentais: direitos sociais e direitos políticos.
- Direitos sociais, nacionalidade e cidadania.
- Direitos sociais em espécie.
- Direitos sociais: conceito e classificação.
- Direitos dos trabalhadores (individuais e coletivos).
- Fundamentos constitucionais dos direitos sociais. Ordem social e direitos sociais. Direitos sociais e direitos econômicos. Conceito e classificação dos direitos sociais.
- Direitos sociais: direito à alimentação, à moradia, à saúde, à educação, ao trabalho, direitos constitucionais trabalhistas, do direito ao futuro.
- Organização sindical.
- Os direitos constitucionais dos trabalhadores.
- A Saúde e a Teoria dos Direitos Sociais.
- Saúde e a Teoria dos Direitos Sociais.
- Direitos sociais, econômicos, culturais e ambientais.
- Direitos sociais e sua relação com a dignidade do trabalhador.
- Proteção constitucional ao trabalho e princípios da dignidade humana e valorização do trabalho.
- Liberdade sindical, organização dos trabalhadores, contribuição sindical e autonomia das entidades sindicais.
- Direito de greve de trabalhadores e servidores públicos.

==================================================
# BANCO 093 — portugues.formacao-de-palavras
==================================================
**Matéria:** Língua Portuguesa: Formação de palavras (id: `portugues.formacao-de-palavras`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Morfemas e estrutura lexical (radical, afixos, vogal temática, desinência e elementos de ligação); derivação e suas modalidades; composição por justaposição e aglutinação; hibridismo; onomatopeias; siglas, acrônimos, abreviaturas, redução vocabular e outros processos produtivos; elementos greco-latinos e critérios de reconhecimento.
Fica de fora (outras matérias tratam): Paradigmas completos de flexão nominal e verbal, análise sintática, modalização discursiva em profundidade e reconstrução histórica exaustiva de etimologias.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Estrutura e formação de palavras.
- Elementos estruturais e processos de formação de palavras.
- Formas de abreviação.
- Estrutura e formação de palavras e formas de abreviação.
- Morfologia e estrutura das palavras.
- Formação de palavras.
- Morfologia: estrutura e formação de palavras.
- Processos de formação de palavras.
- Processos de formação palavras.
- Processo de formação de palavras.
- Estrutura e formação de palavras, abreviações e classes de palavras.
- Estrutura e formação de palavras e abreviações.
- Morfologia: estrutura e formação de palavras e vozes verbais e sua conversão.
- Processos de formação das palavras.
- Estrutura e formação de palavras, abreviação e classes de palavras.
- Estrutura e formação de palavras; formas de abreviação.
- Formação de palavras, abreviação e classes de palavras.
- Estrutura e formação de palavras, classes de palavras e modalizadores.
- Estrutura, formação e abreviação de palavras.
- Morfologia: estrutura e formação das palavras; radicais gregos e latinos.
- Formação, estrutura e classes de palavras e modalizadores.
- Morfologia: estrutura e formação das palavras (radical, afixos, desinências, vogais e consoantes de ligação).
- Processos de formação de palavras: derivação, composição, hibridismo, onomatopeia, siglas e abreviações.
- Morfologia: formação e classe de palavras.
- Estrutura do vocábulo: flexão dos vocábulos, seu valor e significação dentro de frases.
- Descrição linguística: unidades linguísticas: orações, sintagmas, palavras, morfemas.
- Estrutura de palavras.
- Formação de palavras, abreviações e classes gramaticais.
- Textos da esfera científica/acadêmica — Análise linguística: Identifique os processos corriqueiros de formação de palavras (afixos, derivações e composições) essenciais ao léxico técnico.
- Processos de formação de palavras e emprego das classes de palavras.

==================================================
# BANCO 094 — matematica.quantificadores-diagramas
==================================================
**Matéria:** Matemática: Quantificadores e diagramas (id: `matematica.quantificadores-diagramas`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Leitura de proposições categóricas com todo, algum e nenhum; relações de inclusão, interseção e disjunção entre conjuntos; tradução entre linguagem natural e diagramas de Venn; negação de enunciados quantificados; inferências válidas e inválidas.
Fica de fora (outras matérias tratam): Lógica de predicados com símbolos formais, demonstrações avançadas, quantificadores múltiplos com dependência de variáveis e cálculos de cardinalidade além de conjuntos finitos simples.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Diagramas lógicos.
- Lógica sentencial (ou proposicional): Diagramas lógicos.
- Diagramas lógicos e resolução de problemas.
- Lógica sentencial ou proposicional: diagramas lógicos.
- Diagramas lógicos (silogismos e relações de inclusão/exclusão).
- Diagramas de Venn e aplicações em lógica.
- Quantificadores lógicos: todo, algum, nenhum.
- Diagramas lógicos e lógica de primeira ordem.
- Diagramas lógicos, envolvendo as proposições categóricas.

==================================================
# BANCO 095 — administracao-geral.funcoes-administrativas
==================================================
**Matéria:** Administração Geral: Funções administrativas: planejar, organizar, dirigir e controlar (id: `administracao-geral.funcoes-administrativas`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Noções das teorias da administração (clássica, científica, relações humanas, burocrática, neoclássica, sistêmica e contingencial); processo administrativo e funções planejar, organizar, dirigir e controlar; planejamento estratégico, tático e operacional, missão, visão e análise SWOT; organização: estrutura formal e informal, organograma, departamentalização, centralização, descentralização, delegação, amplitude de controle, estruturas linear, funcional, linha-staff, matricial e em rede; direção: liderança, motivação (Maslow, Herzberg, McGregor, expectativa) e comunicação; controle: tipos, níveis e etapas; tomada de decisão: tipos, modelo racional, racionalidade limitada e heurísticas; cultura organizacional: níveis e elementos.
Fica de fora (outras matérias tratam): Gestão de pessoas (recrutamento, treinamento, avaliação de desempenho, competências), gestão de projetos e da qualidade, modelos de administração pública e governança, e organização da administração pública no direito administrativo, tratados em matérias próprias.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Cultura organizacional.
- Características básicas das organizações formais modernas: tipos de estrutura organizacional, natureza, finalidades e critérios de departamentalização.
- Funções da Administração: planejamento, organização, direção e controle.
- Funções de administração: planejamento, organização, direção e controle.
- Tipos de departamentalização: características, vantagens e desvantagens de cada tipo.
- Organização informal.
- Administração por objetivos.
- Evolução da administração: principais abordagens da administração (clássica até contingencial).
- Processo decisório.
- Controle: tipos, vantagens e desvantagens.
- Estrutura organizacional.
- Conceitos básicos em administração: eficiência, eficácia, efetividade, qualidade.
- Teorias da administração.
- Planejamento tático.
- Planejamento operacional.
- Controle: características.
- Motivação, liderança, comunicação, descentralização e delegação.
- Processo administrativo e funções de planejamento, organização, direção e controle.
- Planejamento: princípios e conceitos básicos, níveis estratégico, tático e operacional.
- Processo administrativo: Processo de planejamento.
- Evolução da administração.
- Comunicação.
- Processo Administrativo: planejamento, organização, direção e controle.
- Organização.
- Processo decisório: tipos de decisões.
- Processo de planejamento: Planejamento tático.
- Processo de planejamento: Planejamento operacional.
- Processo de planejamento: Administração por objetivos.
- Processo de planejamento: Processo decisório.
- Características básicas das organizações formais modernas: tipos de estrutura organizacional, natureza e finalidades.

==================================================
# BANCO 096 — direito-civil.pessoas-naturais
==================================================
**Matéria:** Direito Civil: Pessoas naturais: personalidade, capacidade e domicílio (id: `direito-civil.pessoas-naturais`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Início e término da personalidade civil, nascituro, comoriência, capacidade e emancipação, autonomia de pessoas com deficiência após Lei 13.146/2015, direitos da personalidade, ausência e morte presumida, domicílio das pessoas naturais (Código Civil, arts. 1º a 39 e 70 a 78).
Fica de fora (outras matérias tratam): Pessoas jurídicas, capacidade processual, detalhamento de sucessões além das etapas patrimoniais da ausência e regras especiais de competência processual.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Domicílio.
- Direitos da Personalidade.
- Pessoas naturais.
- Pessoas naturais: Domicílio.
- Personalidade.
- Pessoas naturais: Personalidade.
- Pessoas naturais: Capacidade.
- Pessoas naturais: Direitos da personalidade.
- Pessoas naturais: Conceito.
- Capacidade.
- Pessoas naturais: Ausência.
- Início da pessoa natural.
- Pessoas naturais: Início da pessoa natural.
- Ausência.
- Pessoas naturais: Nome civil.
- Pessoas naturais: Estado civil.
- Pessoas naturais: Existência.
- Personalidade e Capacidade.
- Pessoas naturais: nome.
- Domicílio Civil.
- Do domicílio.
- Pessoa natural.
- Nome.
- Pessoas naturais: personalidade, capacidade, direitos de personalidade.
- Da Pessoa Natural.
- Das Pessoas.
- Das pessoas naturais.
- Domicílio e residência.
- Pessoas.
- Pessoas naturais. Conceito. Início da pessoa natural. Personalidade. Capacidade.

==================================================
# BANCO 097 — informatica.analise-dados-ia
==================================================
**Matéria:** Informática: Análise de dados e inteligência artificial (id: `informatica.analise-dados-ia`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Ciclo de análise; mineração de dados; inteligência de negócios e visualização; big data; aprendizado de máquina supervisionado e não supervisionado; avaliação, sobreajuste, viés, explicabilidade e usos de inteligência artificial; transformação digital orientada por dados.
Fica de fora (outras matérias tratam): Programação detalhada de modelos, demonstrações matemáticas avançadas, treinamento de redes neurais em código e implantação em produto específico.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Conceitos de Inteligência Artificial, Análise de Dados e Big Data.
- Técnicas para pré-processamento de dados.
- Visualização e análise exploratória de dados.
- Regressão linear e logística, árvores de decisão, random forests, SVM e K-NN.
- Visualização de dados e storytelling.
- Ciência de dados.
- Modelo de referência CRISP-DM.
- Modelagem preditiva. Aprendizado de máquina. Mineração de texto.
- Manipulação, tratamento e visualização de dados.
- Aprendizado supervisionado: regressão, classificação, métricas, regularização e validação de modelos.
- Aprendizado não supervisionado, PCA, K-Means, misturas gaussianas e regras de associação.
- Aprendizado supervisionado: Regressão e Classificação.
- Métodos e etapas de análise de dados.
- Big Data.
- Conceitos básicos de inteligência artificial: aprendizado supervisionado, não supervisionado e por reforço.
- Tendências, projeções e analytics.
- Inteligência artificial, análise de dados e big data.
- Noções de análise de dados.
- Análise de Agrupamentos (Clusterização).
- Análise de agrupamentos (clusterização). Detecção de anomalias.
- Modelagem estatística e aprendizado de máquina: visualização e comunicação de resultados.
- Aprendizado supervisionado e não supervisionado.
- Processos de treinamento, validação e teste.
- Noções de Análise e Mineração de Dados: Estrutura e Organização dos Dados (dados estruturados e não estruturados), Coleta, Tratamento, Armazenamento e Visualização de dados.
- Noções de Inteligência Artificial e Aprendizado de Máquina: Compreensão básica das principais técnicas de aprendizado de máquina, como agrupamento (clustering), classificação, detecção de anomalias.
- Aprendizado não supervisionado: redução de dimensionalidade e PCA.
- K-Means, mistura de Gaussianas e regras de associação.
- Técnicas de classificação, regressão, agrupamento, redução de dimensionalidade e associação.
- Aprendizado não supervisionado, PCA, K-Means, mistura de Gaussianas e regras de associação.
- Métricas de avaliação, overfitting, underfitting, regularização e seleção de modelos.

==================================================
# BANCO 098 — portugues.funcoes-linguagem-teoria
==================================================
**Matéria:** Português: História da língua, níveis e funções da linguagem (id: `portugues.funcoes-linguagem-teoria`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Formação histórica do português e contato linguístico; língua, linguagem, fala e norma; variações e registros; elementos da comunicação; seis funções da linguagem; noções de discurso relatado e intertextualidade na leitura funcional de textos.
Fica de fora (outras matérias tratam): Morfologia e sintaxe detalhadas, redação oficial, regras ortográficas pormenorizadas e análise aprofundada de gêneros textuais.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Funções da linguagem.
- Elementos dos atos de comunicação.
- Os diversos níveis de linguagem.
- As funções da linguagem.
- Registros de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas.
- Linguagem e comunicação: situação comunicativa e variações linguísticas.
- Níveis de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas: Os diversos níveis de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas: As funções da linguagem.
- Discurso, registros, funções da linguagem e atos de comunicação.
- Funções da linguagem: Elementos dos atos de comunicação.
- Tipos de discurso, registros e funções da linguagem e elementos dos atos de comunicação.
- Elementos da comunicação e funções da linguagem.
- Elementos de comunicação.
- Comunicação, linguagem e variações linguísticas.
- Comunicação, linguagem, variações linguísticas, gêneros e tipologias textuais.
- Funções da linguagem e elementos dos atos de comunicação.
- Registros e funções da linguagem.
- Registros e funções da linguagem e elementos dos atos de comunicação.
- Tipos de discurso, registros e funções da linguagem.
- Tipos de discurso, registros e funções da linguagem e elementos da comunicação.
- Registros e funções da linguagem e atos de comunicação.
- Registros, funções da linguagem, discurso e comunicação.
- Registros, funções da linguagem e elementos dos atos de comunicação.
- Ordem direta e inversa, tipos de discurso, registros e funções da linguagem.
- Discurso, registros, funções da linguagem e elementos da comunicação.
- Teoria da linguagem: história da língua, níveis de linguagem e funções da linguagem.
- Níveis de linguagem, funções da linguagem e intertextualidade.
- Pragmática, significado contextual, níveis e funções da linguagem e intertextualidade.

==================================================
# BANCO 099 — direito-constitucional.ordem-social
==================================================
**Matéria:** Direito Constitucional: Ordem social na Constituição (id: `direito-constitucional.ordem-social`)

Siga as regras do arquivo 00-REGRAS.md (6 lotes de 50 questões A–E, 30% média e 70% difícil).

## Escopo
Princípios da ordem social; seguridade social (financiamento, saúde, previdência e assistência); educação, cultura e desporto; ciência, tecnologia e inovação; meio ambiente; família, criança, adolescente, jovem, pessoa idosa e direitos indígenas na Constituição (arts. 193 a 232).
Fica de fora (outras matérias tratam): Cálculos previdenciários, exame integral da legislação infraconstitucional, políticas setoriais fora da matéria e questões de processo orçamentário ou controle judicial não necessárias para distinguir direitos constitucionais.

## Tópicos dos editais (distribua as questões entre eles; somando os 6 lotes, cada tópico aparece em pelo menos 3 questões; em cada lote, dê mais peso aos primeiros, que são os mais cobrados, e varie os demais; ignore item claramente de outra matéria)
- Ordem social.
- Seguridade social.
- Meio ambiente.
- Ordem social: Seguridade Social.
- Ordem social: meio ambiente.
- Educação, cultura e desporto.
- Ordem social: base e objetivos da ordem social.
- Constituição Federal de 1988 (Artigos n° 205 a n° 214).
- Constituição Federal de 1988 (do art. 205 ao art. 214).
- Comunicação social.
- Família, criança, adolescente, idoso, indígenas.
- Ordem social: ciência e tecnologia.
- Ordem social: índios.
- Da Previdência Social.
- Ordem social e Previdência Social.
- Direitos dos Povos indígenas e das comunidades tradicionais.
- Direitos e interesses das populações indígenas.
- Da Ordem Social.
- Ciência, tecnologia e inovação.
- Família, criança, adolescente, jovem e idoso.
- Família, criança, adolescente e idoso.
- Ordem social: Disposições Gerais.
- Ordem social: Previdência Social.
- Ordem social: família, criança, adolescente, jovem e idoso.
- Índios.
- Da Ordem Social: Seguridade Social (Disposição Geral).
- Da Ordem Social: Base e objetivos da ordem social.
- Ordem social, base e objetivos.
- Base e objetivos da ordem social.
- Ciência e tecnologia.
