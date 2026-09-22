# Plano com vários editais

## Objetivo

Permitir que a pessoa monte um único plano de estudos usando as matérias e os tópicos de dois ou mais editais já cadastrados. O plano distribui o tempo entre todas as matérias selecionadas, mas preserva a origem de cada tarefa para que estudo, questões, desempenho e remoções continuem apontando para o edital correto.

## Experiência

No assistente “Criar plano sem IA”, a etapa “Para qual concurso?” passa a se chamar “Quais editais entram neste plano?”. Cada edital aparece como um cartão selecionável, com nome, quantidade de matérias e tópicos. O edital principal começa marcado; a pessoa pode marcar ou desmarcar qualquer edital que tenha ao menos uma matéria.

O botão Continuar só fica disponível com pelo menos um edital selecionado. A seguir, pesos, prévia e criação passam a considerar a união das matérias dos editais marcados. Cada matéria traz o nome do seu edital acima dela quando houver mais de um edital selecionado, evitando ambiguidade entre nomes iguais.

Um edital vazio aparece desabilitado, com uma explicação para completar o edital antes de incluí-lo. Um plano de um edital segue o fluxo atual sem mudanças perceptíveis.

## Dados e compatibilidade

`study_plans.competitionId` permanece como o edital âncora, escolhido pelo edital principal quando existe ou pelo primeiro edital marcado. Isso preserva os planos, chaves estrangeiras, exportações e consultas existentes.

Uma nova tabela `plan_competitions` registra todas as relações plano–edital com `planId`, `competitionId` e `position`. Ela usa chaves estrangeiras com remoção em cascata e é criada na migração 12→13. A migração preenche uma relação para cada plano existente usando o seu `competitionId`, portanto nenhum plano anterior fica sem edital.

`CreatePlanInput` recebe `competitionIds`. O serviço valida que a lista não está vazia, não contém repetidos e que todo `PlanSubjectInput` pertence a um dos editais selecionados. Na criação, ele grava o plano, as relações e as matérias dentro da mesma transação.

Ao duplicar um plano, as relações de edital também são copiadas. Ao excluir um edital, o comportamento atual de cascata remove planos que o usam como âncora; para planos combinados, a remoção precisa primeiro impedir a exclusão e pedir que a pessoa ajuste ou arquive o plano. Isso evita criar um plano com tarefas sem origem. Esse bloqueio será aplicado no repositório de editais e informado pela interface.

## Planejamento e tarefas

O `StudyPlanSnapshotFactory` passa a consultar matérias e tópicos de todos os editais ligados ao plano. O motor já trabalha por matéria e tópico, portanto recebe a lista unificada sem mudar suas regras de distribuição, alternância ou prioridades.

Ao aplicar uma proposta, cada tarefa recebe o `competitionId` da matéria à qual pertence, em vez de sempre receber o edital âncora. As execuções, histórico e desempenho continuam usando a origem real da tarefa.

Ativar ou definir um Plano Mestre combinado deve desativar ou remover o status de mestre de qualquer plano ativo que compartilhe um dos editais incluídos. Assim, a mesma matéria não recebe tarefas concorrentes de dois planos ativos. Para planos de um edital, o comportamento é idêntico ao atual.

## Apresentação

A tela do plano mostra “N editais incluídos” e oferece uma lista curta com os nomes. Cartões e tarefas mantêm o nome da matéria, acrescido do edital quando um mesmo nome aparece em mais de um edital selecionado. A tela de gerenciamento usa a lista de relações, não apenas o edital âncora, para explicar o escopo de cada plano.

## Importação e exportação

O formato `.plano` ganha uma versão que exporta a lista de editais participantes e a origem de cada matéria. Ao importar um formato anterior, o aplicativo cria a lista com apenas o edital âncora. Ao importar o novo formato, valida que todos os editais e matérias referenciados existem antes de gravar qualquer mudança.

## Erros e limites

- Não é possível criar um plano sem edital ou com edital sem matéria.
- Não é possível salvar uma matéria que pertença a um edital fora da seleção.
- Se um edital selecionado for removido durante a criação, o assistente o desmarca e pede uma nova confirmação antes de gerar o plano.
- Planos antigos e planos de um único edital continuam válidos e editáveis.

## Testes

- Migração 12→13 cria a relação de edital para todos os planos antigos.
- Serviço cria e duplica um plano com dois editais, mantendo as relações e a origem das tarefas.
- Ativação de plano combinado resolve conflitos com planos que compartilham edital.
- Assistente permite marcar vários editais, bloqueia edital vazio e inclui todas as matérias na prévia.
- Exportação e importação preservam a seleção de editais; formato antigo continua importável.
