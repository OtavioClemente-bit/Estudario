# Onboarding, splash e fluxo de geração com IA — especificação de UX

> Este documento complementa `PROMPT_GERAR_ESTUDO.md` e `PLANO_FORMAT.md`. Ele
> não altera os formatos `.estudo`/`.plano`; descreve como a interface deve
> guiar o usuário do primeiro acesso até o uso recorrente do app.

## 0. Prioridade

A prioridade nº 1 é o usuário nunca ficar perdido: toda pessoa que baixa o
app deve, nos primeiros segundos, entender exatamente o que fazer. Onboarding
e explicação vêm antes de qualquer refinamento visual.

Ordem sugerida de implementação:

1. Explicação/onboarding guiado (item 4 abaixo).
2. Copiar/Compartilhar prompt em cada ponto de geração (itens 2 e 3).
3. Upload do edital com anexo automático no prompt (item 2).
4. Split de questões reais vs. autorais (item 3).
5. Plano de estudo em modo avançado (item 3).
6. Splash screen profissional (item 5) — nice-to-have, sem bloquear o resto.
7. Abrir `.estudo`/`.plano` direto no app via associação de arquivo (item 6).
8. Remover o carregamento de dados demonstrativos (item 7).

## 1. Fluxo geral do produto

```text
Edital → Tópico (gerar conteúdo com IA) → Plano de estudo → Estudo → Questões → Revisão
```

Cada uma das três primeiras etapas tem um ponto de geração de prompt para IA,
todos seguindo o mesmo padrão de interação (ver item 3).

## 2. Edital

- Além de colar o texto do conteúdo programático, o usuário pode **fazer
  upload do PDF (ou imagem) do edital**.
- Ao gerar o Prompt A, se houver um arquivo anexado, ele **vai junto** no
  compartilhamento/cópia (o prompt não depende só de texto colado).
- Import da estrutura resultante continua como hoje (Mais › Importar pacote
  `.estudo`).

## 3. Padrão de geração de prompt (vale para Edital, Tópico e Plano)

Todo ponto do app que gera um prompt para IA segue o mesmo padrão de
interface:

1. Um ícone de engrenagem (⚙) abre um menu/modal com as opções daquele
   contexto (ver abaixo).
2. Duas ações de saída, sempre lado a lado:
   - **Copiar** — copia o prompt (e referencia o arquivo anexado, se houver)
     para a área de transferência.
   - **Compartilhar** — abre o menu nativo de compartilhamento do Android
     com o prompt (e o arquivo, quando houver) prontos; o usuário escolhe na
     hora qual app de IA quer abrir no celular (ChatGPT, Gemini, Claude,
     etc.), sem precisar copiar e colar manualmente.
3. O app preenche automaticamente tudo que já sabe pelo contexto (concurso,
   matéria, tópico exatos) — o usuário só ajusta as opções específicas do
   pedido.

### 3.1 Tópico — gerar conteúdo (Prompt B)

Opções no modal antes de copiar/compartilhar:

- O que incluir: teoria, resumo, revisão rápida, bizus, pegadinhas, active
  recall, questões (checkboxes, todos marcados por padrão).
- Quantidade de questões, dividida em dois campos:
  - **questões inéditas/autorais** (a IA cria no estilo da banca);
  - **questões reais de prova anterior** (a IA busca/reproduz questões que
    realmente caíram, citando banca/órgão/ano — já suportado pelo formato
    `.estudo` via `questionSourceType`, `sourceId` e `sourceUrl`; só faltava
    expor isso no gerador de prompt).
- Dificuldade: fácil, média, difícil ou mista.
- Reimportação: mantém a lógica atual (mesmo `packageId` → Atualizar
  preserva progresso; Criar cópia guarda as duas versões).

### 3.2 Plano de estudo — gerar plano (Prompt C)

Dois modos:

- **Simples**: horas por dia (uma média), data da prova, objetivo.
- **Avançado** (plano "complexo" de verdade): horas disponíveis por dia da
  semana (não uma média única), períodos indisponíveis fixos, data da prova,
  objetivo, prioridade por matéria/tópico, distribuição entre tipos de
  tarefa (teoria, questões, revisão, active recall, simulado, discursiva),
  preferência de ciclo de revisão (D+1, D+7, D+30).

O app monta o prompt do `.plano` já refletindo os campos preenchidos no modo
escolhido.

## 4. Onboarding guiado (primeira abertura)

Ao abrir o app pela primeira vez (flag `first_launch` persistida, ex. em
DataStore), depois do splash, mostrar um tour curto guiando a pessoa pelas
ações principais — não uma tela de texto solta, e sim algo que aponta para
os elementos reais da interface (spotlight/coach mark) ou um carrossel de
3–4 passos, por exemplo:

1. "Comece por aqui: importe ou cole o edital do seu concurso."
2. "Toque na engrenagem de um tópico para gerar teoria, resumo e questões
   com IA."
3. "Monte seu plano de estudo com base nas horas que você tem disponíveis."
4. "Acompanhe seu progresso e as revisões programadas aqui."

Requisitos:

- Botão "Pular" sempre visível.
- Curto — o objetivo é orientar a primeira ação, não explicar tudo de uma
  vez.
- Deve ficar **acessível depois**, a qualquer momento, em algo como
  **Mais › Como usar** — repetindo o tour ou mostrando o mesmo passo a passo
  em texto, para quem pulou ou quer relembrar.

## 5. Splash screen

Tela de carregamento inicial com cara de app profissional: logo/nome do
Estudário, sem depender de um delay artificial fixo — aparece enquanto o
app inicializa de fato (Splash Screen API do Android 12+, com fallback
consistente em versões anteriores) e some assim que a home estiver pronta.

## 6. Abrir arquivo direto no app

`.estudo` e `.plano` gerados e salvos no celular devem abrir diretamente no
Estudário ao serem tocados (associação de arquivo/intent-filter), sem
precisar ir manualmente em Mais › Importar. Fecha o ciclo: gerou na IA →
salvou → tocou → importou.

## 7. Remover dados demonstrativos

Remover a opção/tela de carregar o conjunto de dados fictícios de
demonstração. Fica só o fluxo real (importar o próprio edital), reforçado
pelo onboarding do item 4.

## 8. Estado implementado (v2.2.0)

- **Guias separados por contexto** (`ui/tour/AppTour.kt`), navegados por setas (anterior/próximo),
  sem exigir toque nos botões reais:
  - *Primeiros passos* — abre na primeira execução: Edital, criar concurso, montar com IA, importar.
  - *Conteúdo das matérias* — abre depois do primeiro edital importado: ✨ por matéria/tópico,
    importar conteúdo, revisões espaçadas e fila de estudos.
  - *Plano*, *Treinar* e *Mais* (revisões, fila, desempenho, caderno de erros) — abrem na primeira
    visita à aba. Todos podem ser repetidos em Mais › Como usar o app. Vistos ficam em `seen_tours`.
- **Geradores de prompt sem edição de texto** (`data/prompt/PromptBuilders.kt` +
  `ui/prompt/`): edital (cargo, banca, anexo do PDF, escopo, detalhe), conteúdo por matéria
  (seleção de tópicos) ou por tópico (blocos, profundidade, nº/estilo/dificuldade de questões,
  fonte anexada) e plano (datas, horizonte, minutos por dia, metas, método, prioridade por
  matéria, tópicos e desempenho). Ações: **Copiar**, **Enviar para a IA** (folha de
  compartilhamento com o anexo) e, na volta, **Colar resposta** ou **Escolher arquivo**.
  O esqueleto JSON do conteúdo é gerado com os IDs reais e validado por teste com o parser.
- **Abrir/compartilhar com o app**: `ACTION_VIEW` e `ACTION_SEND` (arquivo ou texto) para
  JSON/octet-stream/text. O texto é limpo (BOM, cercas ```json, texto antes/depois) e o tipo
  é detectado mesmo sem o campo `format` (caso do `.estudo` v2). Em `.plano`, IDs que não são
  UUID são convertidos em UUIDs determinísticos no escopo do plano.
