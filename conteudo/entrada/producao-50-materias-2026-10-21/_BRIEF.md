# Converter as matérias do Codex (PR 3) em matérias do app Estudário

Você recebe ranks. Para cada rank N, a base está em `conteudo/entrada/producao-50-materias-2026-10-21/NN-*/TEORIA.md` e `QUESTOES.md`
(rascunho do Codex, com fontes oficiais citadas). Os tópicos reais de edital estão em `_topicos.json` (chave = rank).

Regras do projeto (AGENTS.md): você escreve TUDO à mão (nenhum script gera, completa ou embaralha texto); nunca invente número de
lei, artigo, prazo, valor ou dado; se não tiver certeza, explique o conteúdo sem o número. Não faça commit.

## Passo 1: conferir a fonte
Antes de escrever, abra a norma/fonte oficial citada no rascunho (WebFetch/WebSearch: planalto, sinj.df.gov.br, sites de ALE, TJ,
prefeitura, IBGE etc.). Confira os pontos de prova (prazos, percentuais, órgãos, composição, datas). Corrija o que o rascunho
errou. Se a fonte oficial não abrir de jeito nenhum e o rascunho disser "objeto a confirmar" sem base sólida, NÃO escreva a
matéria: informe no relatório o motivo.

## Passo 2: escrever a matéria no formato do app
Grave `conteudo/entrada/materias-pr3/<id>.json` (um arquivo por matéria, inteiro de uma vez, com Write). Formato e quantidades:
`conteudo/fila/PEDIDOS-MATERIAS-LOCAIS-2026-10-21.md`, seção "Mensagem 0" (siga TODOS os campos e limites). Além disso:
- id: use o prefixo do lugar já usado na biblioteca: `distrito-federal.`, `roraima.`, `rondonia.`, `espirito-santo.`, `santa-catarina.`,
  `minas-gerais.`, `ceara.`, `alagoas.`, `mato-grosso.`, `paraiba.`; para município use o prefixo do ESTADO + nome da cidade
  (ex.: `roraima.boa-vista-pccr-geral`, `rondonia.ji-parana-historia-geografia`, `paraiba.campina-grande-historia`), ou
  `saude-publica.` / `educacao.` / `legislacao.` para normas nacionais (LC 141, Portarias do MS, Decreto 9.235, PROEJA).
  Confira que o id não existe em `conteudo/materias/<prefixo>/`.
- Estilo "aula" de cursinho caro: explique cada sigla e termo, dê exemplo concreto, diga "por que" a regra existe, tabelas
  comparativas, "Pegadinha:" e "Resumo para a prova" ao fim de cada capítulo. Teoria total 15.000 a 30.000 caracteres.
- Questões: 40 a 60; aproveite e melhore as Certo/Errado boas do rascunho (comentário completo, ≥150 caracteres) e ESCREVA
  múltipla escolha A–E (≥15) no estilo da banca do edital; "section" = título exato do capítulo.
- aliases: o título + os tópicos de `_topicos.json` desse rank copiados exatamente, MENOS os genéricos que serviriam para
  qualquer lugar (ex.: "Divisão política.", "Hidrografia."), que não podem virar apelido.
- sources: as fontes oficiais que você realmente abriu, com url e accessedAt "2026-10-22".

## Passo 3: validar
`deno run --allow-read --allow-env C:/Users/otavi/AppData/Local/Temp/claude/C--Users-otavi-Documents-Codex-2026-09-15-vc-x20/439b4cfa-e8a4-49ca-825a-3448b42eeed9/scratchpad/conferir-arquivo.ts <arquivo.json>`
deve dizer OK; corrija o que ele apontar (à mão).

## Relatório final
Para cada rank: id criado (ou motivo de não ter criado), tamanho da teoria, nº de questões (C/E e ME), e as correções que você fez
em relação ao rascunho do Codex (o que estava errado na fonte).
