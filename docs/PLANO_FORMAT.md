# Formato `.plano` — versão 1

O `.plano` é um JSON UTF-8 separado do conteúdo `.estudo`. Ele descreve estratégia e planejamento; não inclui teorias, resumos ou enunciados de questões.

## Raiz

- `format`: sempre `meu-concurso-plano`.
- `version`: sempre `1`.
- `planId`: UUID estável do plano.
- `concurso`: `externalId` estável e nome de exibição.
- `nome`, `objetivo`, `dataInicio` e `dataProva` opcional.
- `active` e `masterPlan`: intenções que exigem confirmação ao importar.
- `baseRevision`: revisão local opcional usada para detectar conflito.
- `configuracao`: capacidade e metas gerais.
- `prioridades`, `fasesAnuais`, `planosMensais`, `planosSemanais` e `tarefas`.
- `metadata`: pares curtos de texto para autoria e rastreabilidade.

## Tempo

`configuracao.modo` aceita `SIMPLE` ou `ADVANCED`. `dias` usa números ISO de 1 (segunda) a 7 (domingo), `minutos` líquidos e `indisponivel`. Intervalos não entram na capacidade.

## Referências ao edital

Matérias e tópicos usam `externalId`, nunca o ID numérico local do Room. IDs criados pelo mecanismo oficial do Meu Concurso são válidos. A importação nunca deriva ou fabrica um ID a partir do nome apenas para aceitar o arquivo.

Quando o `externalId` está ausente, o app pode resolver pelo nome normalizado se houver exatamente um registro compatível no contexto correto. Quando o arquivo informa um ID que não existe, uma correspondência por nome é tratada como divergência e exige revisão explícita. Ambiguidades, vínculos fora do concurso ou da matéria correta e registros locais sem ID oficial impedem a importação até serem resolvidos.

## Tarefas

Tipos: `THEORY`, `QUESTIONS`, `REVIEW`, `ACTIVE_RECALL`, `FLASHCARDS`, `SIMULATION`, `DISCURSIVE`.

Estados: `PLANEJADA`, `EM_ANDAMENTO`, `CONCLUIDA`, `REPROGRAMADA`, `NAO_REALIZADA`, `PAUSADA`.

Prioridades: `CRITICAL`, `HIGH`, `MEDIUM`, `LOW`.

Origens: `ENGINE`, `MANUAL`, `IMPORTED`, `REVIEW_SCHEDULE`, `MASTER_PLAN`.

Cada tarefa possui UUID, data ISO-8601, minutos, questões, bloqueio e dependências por UUID. Dependências ausentes, autorreferentes ou cíclicas invalidam o arquivo.

## Importação

O arquivo pode ser escolhido dentro do app ou aberto/compartilhado por Downloads, Files, Drive, navegador, e-mail ou mensageiro. A identificação final ocorre em runtime usando nome exibido, MIME e assinatura JSON; extensão e MIME isolados não são considerados prova do formato.

- **Criar:** cria outro plano e nunca importa histórico de execução.
- **Mesclar:** recomendado; preserva concluídas, execuções, parcial e bloqueios locais.
- **Substituir futuro:** substitui somente tarefas futuras elegíveis.

Histórico local sempre prevalece. Nenhum modo apaga execução acadêmica.

## Validação

O app rejeita JSON inválido, formato/versão desconhecidos, UUIDs duplicados, datas incoerentes, valores negativos, enums desconhecidos e dependências inválidas antes de gravar qualquer dado.
