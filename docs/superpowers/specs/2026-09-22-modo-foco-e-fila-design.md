# Modo foco e fila de estudos

**Data:** 2026-09-22
**Status:** desenho aprovado para revisão do documento

## Objetivo

Dar ao modo foco um lugar claro na navegação, registrar cada sessão com duração e matérias estudadas, manter a sessão ativa quando o app for fechado e impedir que o cronômetro conclua matérias ou tarefas. A Home também deve respeitar a fila de estudos antes de mostrar uma tarefa do plano.

## Decisões confirmadas

- O modo foco será o destino central da navegação inferior: Início, Edital, Foco, Plano e Treinar.
- Sem sessão ativa, a tela apresenta uma lista simples das matérias cadastradas. A pessoa pode escolher uma ou mais matérias para identificar o conteúdo daquela sessão. Também pode iniciar uma sessão livre, sem matéria selecionada.
- Durante uma sessão, a tela mostra o cronômetro e as matérias escolhidas. O foco mede tempo; não inicia questões automaticamente, não muda o estado da matéria, não conclui uma tarefa do plano e não altera a fila.
- Ao fechar e reabrir o app, a sessão continua ativa usando o horário original de início. A notificação persistente continua oferecendo acesso ao cronômetro e a ação de encerrar.
- O botão Foco tem um indicador discreto enquanto houver uma sessão ativa. A sessão continua acessível pela tela central e pela notificação.
- O histórico do foco será próprio, detalhado por sessão, com horário, duração, matérias e origem. O registro da sessão só entra uma vez, mesmo se o encerramento for acionado mais de uma vez.
- Sessões iniciadas pelo destino Foco são sessões livres e podem dar 1 XP por cada 10 minutos completos acumulados, até 10 XP por dia. O XP só é contabilizado depois de encerrar a sessão. Sessões iniciadas dentro de uma matéria ou tarefa do plano não dão XP de foco; o XP vem da conclusão explícita da matéria ou tarefa.
- A Home mostra primeiro o item não pausado mais antigo da fila. Depois que a pessoa concluir esse tópico, ele sai da fila e o próximo item disponível passa a ser o atual. Tarefas do plano assumem o destaque quando não houver itens disponíveis na fila.
- Encerrar o foco não abre nem salva automaticamente a conclusão de uma tarefa. A pessoa permanece no destino Foco, com a navegação inferior disponível, e pode ir ao Início ou concluir a tarefa separadamente.

## Diagnóstico do fluxo atual

O estado de uma sessão ativa já fica persistido em `AppPreferences`, e o app já mantém uma notificação com ação de encerramento e uma rede de segurança do WorkManager. Ao encerrar sessões livres, porém, `FocusSessionManager` grava em `study_sessions` por `StudyRepository.logStudySession`. Esse método também muda o estado do tópico para `EM_ESTUDO`, enquanto `AppViewModel` transforma toda linha de `study_sessions` em atividade que rende XP de tópico. O histórico do cronômetro e a conclusão da matéria ficam, portanto, misturados.

Nas sessões ligadas a tarefas do plano, o término guarda minutos para abrir o diálogo de execução automaticamente. A rota de foco fica fora das quatro abas atuais, e o encerramento de uma tarefa navega diretamente para Plano. A Home escolhe sua missão olhando apenas as tarefas do plano; ela não consulta a fila. O cartão “Próximo estudo” na tela da fila também depende da posição visual, mesmo quando o primeiro item está pausado.

## Experiência proposta

### Navegação e seleção

A navegação inferior terá cinco destinos, com Foco no centro. O destino Foco funciona como uma tela normal do app, sem abrir uma janela modal. Quando não houver sessão ativa, a pessoa vê as matérias cadastradas em uma lista simples, pode marcar várias e inicia a sessão com um botão claro. A seleção só identifica o que será estudado; não conclui nem altera o conteúdo cadastrado. Sem matérias cadastradas, a tela explica como adicionar conteúdo pelo Edital e ainda permite iniciar uma sessão livre.

Quando já houver uma sessão ativa, abrir Foco mostra a sessão existente em vez de criar outra. O botão central exibe um anel animado de forma lenta e discreta, com um estado acessível equivalente a “Foco ativo”. Ao tocar, a tela abre o cronômetro corrente. O indicador não pisca rapidamente.

Os atalhos existentes dentro de uma matéria ou de uma tarefa do plano continuarão iniciando o cronômetro com essa origem identificada. O destino Foco e a notificação continuam disponíveis durante a sessão. Encerrar a sessão registra seu tempo e mantém a conclusão da matéria ou tarefa como ação separada.

### Histórico dedicado

Uma nova tabela `focus_sessions` será a fonte do histórico de foco. Cada registro terá um identificador estável, horário de início e fim, duração em segundos, lista de IDs das matérias, título e origem (`LIVRE`, `MATERIA` ou `PLANO`), com os IDs de tópico ou tarefa quando existirem. A tela Foco mostrará as sessões mais recentes e, em cada linha, data, duração, matérias e origem. Um resumo pode exibir tempo total e quantidade de sessões. Se uma sessão ativa antiga ainda não tiver identificador ao atualizar o app, será atribuído um identificador estável derivado do horário de início; a origem será inferida dos IDs de tarefa e tópico já salvos.

Enquanto o cronômetro roda, o estado ativo continua no DataStore e inclui o identificador da sessão, a origem e as matérias selecionadas. Ao encerrar, o app calcula o tempo real com base nos horários. Ele insere a linha do histórico usando o identificador como chave única antes de limpar o estado ativo. Uma repetição do encerramento ou uma retomada depois de interrupção não cria outra linha. Se a gravação falhar, a sessão permanece ativa para que a pessoa tente encerrar de novo.

A notificação, o botão na tela e o fechamento automático da rede de segurança chamam o mesmo encerramento idempotente. A restauração do Não Perturbe e a remoção da notificação continuam após o registro do histórico e a limpeza da sessão ativa.

### XP e conclusão explícita

Sessões livres acumulam XP a partir do total de minutos livres encerrados no dia local: 1 XP por 10 minutos completos, até 10 XP por dia. O limite é aplicado sobre a soma diária, em vez de arredondar cada sessão separadamente. Sessões vinculadas a matéria ou plano não entram nessa conta. O XP do foco só passa a aparecer no histórico de progresso depois do encerramento.

O histórico dedicado continua alimentando os minutos e a contagem de sessões nas estatísticas e nos dias ativos do calendário. A atividade de foco não fecha sozinha a meta diária nem a sequência, seguindo a regra atual; ela deixa de ser tratada como conclusão de tópico para fins de XP. A tela do tópico continua identificando apenas conclusões como “Estudo concluído”.

A conclusão de tópico continua sendo registrada como atividade de tópico e rende seu XP uma única vez. Para isso, a rotina de conclusão deve distinguir foco de conclusão: sessões de foco não podem satisfazer a verificação de “já concluído”. Um tópico em `EM_ESTUDO` pode ser concluído explicitamente; `ESTUDADO` e `REVISANDO` continuam protegidos contra recompensa repetida. Desmarcar e concluir de novo não paga XP se já houver um registro de conclusão anterior.

A conclusão de tarefa do plano permanece no fluxo explícito de execução já existente. O fim do cronômetro não abre nem salva a execução automaticamente. A pessoa pode ir à aba Plano e confirmar a atividade quando quiser; a recompensa é daquela execução, sem XP extra de foco.

### Home e fila

A seleção do estudo atual será uma regra única: primeiro, o item não pausado e não concluído de menor posição na fila; se não houver, a tarefa elegível do plano pela ordem atual. Enquanto houver itens ativos na fila, o destaque e a continuidade da Home mostrarão itens da fila, sem substituir o estudo atual pelo próximo bloco do plano. Ao concluir um tópico, a transação atual remove esse item e registra o evento de conclusão; a Home então reflete o próximo item. Encerrar o foco não consome a fila.

Na tela Fila, “Próximo estudo” marca o primeiro item elegível não pausado, não necessariamente a primeira linha quando itens pausados estão presentes. Quando todos os itens estiverem pausados, nenhum será rotulado como próximo. Uma conclusão antiga ou item sem tópico válido não deve bloquear a seleção do próximo item elegível.

## Dados, migração e backup

O banco Room atual está na versão 13. A implementação criará uma migração 13→14 para `focus_sessions`, com índice por data de conclusão e chave primária pelo identificador da sessão. A seleção ativa no DataStore ganhará os campos necessários, mantendo compatibilidade com as chaves atuais.

Sessões antigas identificadas por `study_sessions.notes = 'Modo foco'` serão convertidas para o histórico dedicado e removidas de `study_sessions`, preservando horários, duração, tópico e matéria conhecidos. Registros migrados sem um tópico receberão origem livre; registros ligados a tópico receberão origem matéria. Títulos ausentes serão apresentados como “Sessão de foco”. Assim esses períodos continuam no histórico de tempo e deixam de ser contados como XP de tópico concluído. Estados antigos `EM_ESTUDO` permanecem visíveis e poderão ser concluídos explicitamente após a mudança.

O formato do backup será incrementado para incluir sessões de foco. A restauração continuará aceitando versões anteriores, tratando a lista de foco como vazia quando o campo não existir. Uma sessão em andamento será mantida nas preferências locais e não será transformada em sessão encerrada durante exportação.

## Componentes afetados

- `FocusSessionPrefs`, `AppPreferences` e `FocusSessionManager`: seleção, origem, identificador e encerramento recuperável.
- Entidades/DAO/`AppDatabase`: tabela e migração do histórico dedicado.
- `AppViewModel` e `ProgressEngine`: fluxo do histórico e XP de foco sem XP acidental de tópico.
- `StreakEngine` e o mapeamento de desempenho: preservar contagem de sessões/minutos e dias ativos, separando-os das conclusões que pagam XP de tópico.
- `EstudarioApp` e `FocusScreen`: quinto destino central, seleção, reabertura e estado ativo.
- `HomeScreen` e modelos da Home: preferência da fila sobre o plano.
- `QueueScreen` e `StudyRepository`: primeiro item elegível, remoção ao concluir e conclusão de tópico idempotente.
- `BackupService`: exportação/restauração compatível do novo histórico.

## Critérios de aceitação

1. Se o app for fechado durante uma sessão e reaberto antes do limite de segurança atual de quatro horas, mostra a mesma sessão ativa e o tempo decorrido correto. Ao atingir o limite, o encerramento automático grava a sessão no histórico.
2. A tela Foco lista as matérias existentes, aceita uma ou mais seleções e permite começar sem matéria.
3. O indicador do destino central e a notificação deixam claro que o foco continua ativo.
4. Encerrar pela tela, notificação ou fechamento automático cria no máximo um registro com duração e matérias corretas; falha ao gravar preserva a sessão ativa.
5. Uma sessão livre paga apenas a regra diária de XP de foco; uma sessão iniciada em matéria/plano não paga XP de foco.
6. Encerrar o foco não muda o estado de tópico, tarefa ou fila. Concluir tópico/tarefa depois continua explícito e não concede XP repetido.
7. Com fila ativa, a Home mostra o próximo tópico não pausado; depois de concluí-lo, mostra o próximo da fila. Sem fila ativa, mostra a missão do plano.
8. A restauração de backups antigos continua funcionando; backups novos preservam o histórico de foco.
9. Estatísticas continuam mostrando sessões e minutos de foco; o foco aparece como atividade no calendário sem concluir a meta diária por si só.

## Fora de escopo

- Criar questões, resumos ou conteúdo novo automaticamente ao iniciar o foco.
- Bloquear outros aplicativos ou instalar serviço de acessibilidade.
- Alterar a lógica de replanejamento de tarefas do plano além de retirar a conclusão automática pelo cronômetro.
- Recalcular XP de tarefas, questões ou revisões sem relação com este fluxo.
