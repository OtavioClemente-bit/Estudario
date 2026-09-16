# Plano de Estudos Adaptativo — Especificação de Design

## Objetivo

Adicionar ao aplicativo Meu Concurso um módulo local-first de planejamento hierárquico — Ano, Mês, Semana e Hoje — integrado ao edital, conteúdo `.estudo`, questões, revisões e progresso existentes. O sistema deve distribuir somente a capacidade líquida informada pelo usuário, replanejar apenas o futuro e preservar integralmente histórico e execução real.

## Escopo

O módulo inclui:

- vários planos por concurso, com um ativo e no máximo um Plano Mestre;
- planos anuais, mensais e semanais reais e versionáveis;
- disponibilidade simples e avançada em minutos líquidos;
- motor puro e determinístico de geração e replanejamento;
- execução separada da intenção planejada;
- quatro visões: Hoje, Semana, Mês e Ano;
- criação assistida, edição, duplicação, arquivamento e restauração;
- formato `.plano` v1 com CREATE, MERGE e REPLACE_FUTURE;
- exportação compacta de contexto para ChatGPT, sem API;
- previsão de término, métricas honestas e alertas do Plano Mestre;
- backup v5, migração Room 4→5 e testes de regressão.

Não fazem parte do escopo: API do ChatGPT/OpenAI, pagamentos, sincronização em nuvem, rede social, gamificação complexa ou reescrita de módulos não envolvidos.

## Arquitetura

### Domínio puro

`domain/planner` conterá modelos imutáveis, `StudyPlannerEngine`, `StudyPlanProgressCalculator`, `StudyPlanForecastCalculator` e `MasterPlanMonitor`. Essa camada não dependerá de Android, Room ou Compose.

A operação principal será:

```kotlin
fun plan(
    snapshot: StudyPlannerSnapshot,
    reason: ReplanReason,
): PlanningProposal
```

O motor recebe um snapshot completo e devolve uma proposta. Ele nunca lê nem persiste dados diretamente.

### Persistência

`data/local/planner` conterá entidades, relações e DAO do módulo. A migração 4→5 será somente aditiva. As tabelas atuais não serão removidas ou modificadas.

### Aplicação e transações

`data/planner` conterá:

- `StudyPlanSnapshotFactory`, que traduz Room e dados acadêmicos existentes para o domínio;
- `StudyPlanRepository`, que expõe planos e consultas persistidas;
- `StudyPlanApplicationService`, que valida e aplica propostas em transações;
- `StudyExecutionService`, único componente autorizado a registrar execução real.

### Transferência

`data/transfer/planner` conterá DTOs próprios, codec, validação, resolução de IDs e políticas de importação. DTOs `.plano` não serão entidades Room e não ditarão o schema interno.

### Apresentação

`ui/planner` conterá `StudyPlanViewModel`, assistente inicial, gerenciamento de planos e as quatro visões. Todas consumirão um único `ActivePlanUiState`. Nenhuma tela conhecerá detalhes do algoritmo.

### Fluxo de dados

```text
Room + edital + questões + revisões + execuções
                    ↓
          StudyPlanSnapshotFactory
                    ↓
            StudyPlannerEngine
                    ↓
            PlanningProposal
                    ↓
       StudyPlanApplicationService
                    ↓ transação validada
                   Room
                    ↓
           StudyPlanViewModel
                    ↓
          Hoje | Semana | Mês | Ano
```

## Navegação

A barra principal será:

**Início | Edital | Plano | Treinar | Mais**

“Erros” será movido para “Mais”, sem perda de funcionalidade. A Home atual será preservada e não receberá o planejador nesta entrega.

A aba Plano abrirá em Hoje e terá navegação secundária:

**Hoje | Semana | Mês | Ano**

## Planos ativo, Mestre e arquivado

- Vários planos podem pertencer ao mesmo concurso.
- Exatamente zero ou um plano pode estar ativo por concurso.
- Exatamente zero ou um plano pode ser Mestre por concurso.
- Um plano pode ser ativo e Mestre simultaneamente.
- Ativar outro plano desativa o anterior.
- Marcar outro como Mestre desmarca o anterior.
- Hoje, Semana, Mês e Ano sempre usam o plano ativo não arquivado.
- O Plano Mestre continua sendo a referência estratégica mesmo quando outro plano está ativo.
- Arquivar desativa o plano e remove sua marca de Mestre, mas preserva todos os dados.
- Planos arquivados permanecem consultáveis e restauráveis.
- Duplicação gera novos UUIDs para entidades do plano, mantém referências reais ao edital e não copia execuções.

A exclusividade será aplicada pelo serviço transacional e protegida no SQLite por gatilhos. Gatilhos também impedirão plano arquivado ativo e referências internas entre planos distintos.

## Modelo Room

### Plano e auditoria

`StudyPlanEntity`:

- `id: String` UUID, chave primária;
- `competitionId: Long`, FK para concurso;
- nome, objetivo, datas inicial e de prova;
- `active`, `masterPlan`, `archived`;
- `revision: Long`;
- datas de criação e atualização.

`StudyPlanRevisionEntity`:

- chave composta `planId + revision`;
- `baseRevision`;
- motivo da alteração;
- `proposalId` opcional;
- resumo e data.

Qualquer alteração estrutural ou aplicação de proposta incrementa `revision` na mesma transação.

### Capacidade

`StudyAvailabilityEntity`:

- chave `planId + dayOfWeek`;
- modo SIMPLE ou ADVANCED;
- minutos líquidos disponíveis;
- indisponibilidade.

`StudyDayOverrideEntity`:

- chave `planId + epochDay`;
- minutos alternativos opcionais;
- indisponibilidade;
- bloqueio do dia.

O modo simples também será normalizado em sete registros diários para que o motor tenha uma única representação.

### Prioridades

`PlanSubjectEntity`:

- chave `planId + subjectId`;
- prioridade CRITICAL, HIGH, MEDIUM ou LOW;
- matéria pausada;
- manutenção mínima semanal;
- peso configurável opcional;
- nome curto capturado para preservação histórica.

### Hierarquia versionável

`AnnualPhaseEntity`, `MonthlyPlanEntity` e `WeeklyPlanEntity` terão:

- UUID próprio e `planId`;
- período explícito e ordem;
- objetivo, foco e critérios;
- metas de minutos, questões, discursivas e percentual;
- `validFromRevision` e `validUntilRevision` opcional.

Alterações estruturais encerram a versão vigente e criam outra. Relações auxiliares associam matérias e tópicos a cada nível.

### Tarefas

`PlanTaskEntity` terá:

- UUID e `planId`;
- vínculos opcionais com fase, mês e semana;
- `competitionId`, `subjectId` e `topicId` opcionais;
- nomes curtos capturados para histórico;
- data programada;
- tipo, minutos e questões planejados;
- prioridade, origem, observação e bloqueio;
- status;
- `replannedFromTaskId` opcional;
- revisão de criação e atualização.

Tipos iniciais:

- THEORY;
- QUESTIONS;
- REVIEW;
- ACTIVE_RECALL;
- FLASHCARDS;
- SIMULATION;
- DISCURSIVE.

Estados:

- PLANEJADA;
- EM_ANDAMENTO;
- CONCLUIDA;
- REPROGRAMADA;
- NAO_REALIZADA;
- PAUSADA.

`PlanTaskDependencyEntity` terá chave composta `taskId + dependsOnTaskId`. Autorreferência, ciclos e dependências entre planos serão rejeitados.

### Execução real

`StudyTaskExecutionEntity` será append-only e conterá:

- UUID;
- plano e tarefa de origem;
- concurso, matéria e tópico opcionais;
- início e término;
- minutos realizados;
- questões, acertos, observação e dificuldade percebida;
- data de criação.

Somente `StudyExecutionService` poderá inserir execução. Um registro fechado não será reescrito ou apagado pelo motor. Correções explícitas gerarão eventos compensatórios auditáveis.

O progresso será derivado da soma das execuções. Uma tarefa de 60 minutos com 25 realizados mantém os 25 minutos no histórico; o replanejamento cria uma tarefa sucessora de 35 minutos e nunca devolve os 25 minutos à demanda.

### Integridade dos vínculos

IDs numéricos locais serão usados internamente. No intercâmbio, referências ao edital usarão `externalId` estável. Se uma matéria ou tópico for removido, as FKs anuláveis usarão `ON DELETE SET NULL` e os nomes capturados permitirão consultar o histórico.

Índices cobrirão concurso, plano, data programada, status, matéria, tópico, períodos vigentes e consultas do plano ativo.

## Concorrência e versionamento otimista

`PlanningProposal` conterá `planId`, `proposalId` e `baseRevision`. Antes de aplicar qualquer mudança, o serviço executará uma atualização condicional da revisão. Se a revisão corrente diferir da revisão-base, nenhuma parte da proposta será aplicada; um novo snapshot será montado e o cálculo será refeito.

Na mesma transação serão revalidados:

- plano ativo e não arquivado;
- tarefas concluídas ou iniciadas depois do snapshot;
- bloqueios de dia e tarefa;
- vínculos com edital;
- validade das relações internas.

## Motor de planejamento

O snapshot conterá plano, revisão, horizonte, capacidade, exceções, hierarquia, prioridades, tópicos pendentes, revisões, desempenho, tarefas, execuções e bloqueios.

O planejamento seguirá esta ordem:

1. congelar histórico, tarefas concluídas e itens bloqueados;
2. calcular capacidade líquida por dia;
3. subtrair blocos bloqueados já programados;
4. transformar execuções parciais somente em saldo pendente;
5. reunir revisões, manutenção, tópicos novos, questões e discursivas;
6. ordenar demandas deterministicamente;
7. distribuir blocos respeitando capacidade e dependências;
8. registrar demandas que não couberam;
9. calcular métricas, previsão e alertas.

### Priorização

A pontuação considera:

- prioridade estratégica;
- revisão vencida;
- proximidade da prova;
- fraqueza moderada;
- tempo sem estudo;
- dependências liberadas;
- saturação recente;
- excesso sobre manutenção mínima.

Os pesos serão configuráveis e versionados. Empates serão resolvidos por data-limite, ordem da matéria, ordem hierárquica do tópico e ID estável.

Fraqueza somente contará após amostra mínima configurável e terá teto semanal. Uma matéria não poderá monopolizar indefinidamente o plano. A distribuição reservará primeiro manutenção mínima e depois distribuirá o restante pelos pesos, com teto padrão configurável de 40% da capacidade flexível por matéria.

### Replanejamento

O motor afeta somente tarefas futuras elegíveis. CONCLUIDA é terminal para o motor. Tarefas e dias bloqueados não são movidos.

Faltas não são empilhadas no dia seguinte. A demanda restante retorna à fila e é distribuída pelos próximos dias disponíveis dentro de um horizonte inicial de quatro semanas.

Eventos de recálculo incluem mudança de disponibilidade, falta, execução parcial, conclusão antecipada, pulo, matéria adicionada/pausada/removida, prioridade alterada, revisão pendente, edital atualizado, importação e troca do plano ativo.

Quando a demanda superar a capacidade, a proposta informa minutos necessários, disponíveis, déficit e itens não atendidos. Nenhuma hora fictícia será criada.

## Previsão

A previsão divide a demanda restante pela capacidade semanal efetivamente utilizável, considerando semanas parciais, dias indisponíveis, bloqueios, manutenção e revisões estimadas. A UI mostrará as premissas. Capacidade zero ou demanda indefinida não produzirá data artificial.

## Métricas

`StudyPlanProgressCalculator` calculará:

- progresso diário, semanal, mensal e da fase;
- minutos planejados e realizados;
- questões, acertos e percentual;
- aderência ao plano;
- déficit de capacidade.

Cobertura do edital, domínio e aderência ao planejamento permanecerão métricas diferentes. Horas realizadas não implicam conclusão de matéria.

## Plano Mestre

`MasterPlanMonitor` comparará a estratégia Mestre com o histórico real, mesmo quando outro plano estiver ativo. Matérias essenciais sem estudo pelo limite configurado gerarão avisos não bloqueantes com a última atividade e os dias transcorridos.

## Formato `.plano`

O formato será JSON UTF-8:

```json
{
  "format": "meu-concurso-plano",
  "version": 1,
  "planId": "uuid",
  "concurso": {
    "externalId": "trt-3-ti",
    "nome": "TRT-3 — Analista Judiciário — TI"
  },
  "nome": "Trilha Mestra 2026–2027",
  "objetivo": "...",
  "active": false,
  "masterPlan": true,
  "dataInicio": "2026-09-15",
  "dataProva": null,
  "configuracao": {},
  "prioridades": [],
  "fasesAnuais": [],
  "planosMensais": [],
  "planosSemanais": [],
  "tarefas": [],
  "metadata": {}
}
```

Datas usarão ISO-8601 e durações usarão minutos inteiros. A validação verificará formato, versão, UUIDs, datas, períodos, valores, enums, referências, ciclos, relações internas e unicidade.

Versões futuras desconhecidas, JSON inválido e dados inconsistentes serão rejeitados com mensagens específicas antes de qualquer persistência.

### Políticas de importação

CREATE cria um plano sem importar execução e não troca ativo/Mestre silenciosamente.

MERGE é recomendado. Preserva execução, tarefas concluídas e bloqueios locais; atualiza estratégia e planejamento futuro. Em conflito, histórico local prevalece.

REPLACE_FUTURE substitui apenas tarefas futuras elegíveis. Passado, execução, concluídas e bloqueadas permanecem intactos. A UI exigirá confirmação com resumo do impacto.

Estados ativo e Mestre importados são intenções sujeitas a confirmação explícita.

## Exportação de contexto

A exportação oferecerá JSON compacto e texto legível com concurso, objetivo, fase, período, capacidade, metas, progresso, prioridades, atrasos, fraquezas com tamanho da amostra, questões, acurácia, tarefas não realizadas, tarefas futuras relevantes, déficit e alertas.

Não incluirá enunciados, teorias, resumos, livros, flashcards ou outro conteúdo extenso do `.estudo`.

## Assistente inicial

O fluxo perguntará:

1. concurso e objetivo;
2. datas inicial e de prova;
3. modo simples ou avançado;
4. disponibilidade por dia;
5. prioridades e manutenção;
6. metas de questões e discursivas;
7. revisão de capacidade e déficits.

O plano será persistido somente na confirmação final. Nenhuma estratégia TRT ficará hardcoded.

## Experiência de uso

### Hoje

Mostra resumo planejado/realizado, déficit e cartões ordenados com matéria, caminho do tópico, tipo, minutos, questões e progresso. Oferece Iniciar, Concluir, Reprogramar e Pular. Conclusão registra tempo, questões, acertos, dificuldade e observação. Tocar no tópico abre `TopicDetailScreen` existente.

### Semana

Mostra capacidade, blocos, previsto/realizado, questões, acertos, conclusão, estados e bloqueios de cada dia. Carga manual excedente permanece visível como incompatibilidade.

### Mês

Mostra focos, manutenção, tópicos, capacidade, metas, realização e aderência.

### Ano

Mostra fases versionadas, períodos, objetivos, matérias, metas, critérios, progresso e previsão.

### Gerenciamento

Sem plano ativo, a aba oferece criar, importar, selecionar ou consultar arquivados. O menu permite ativar, marcar como Mestre, duplicar, arquivar, restaurar, exportar e editar.

A UI reutilizará Material 3, tema, cartões, chips, títulos, métricas e espaçamentos existentes. Estados terão texto e ícone, não apenas cor.

## Arquivos e compatibilidade

`MainActivity` identificará `format` antes de encaminhar arquivos. `.estudo`, `.plano` e backup manterão fluxos separados. O backup avançará para v5, incluirá o planejador e continuará aceitando backups anteriores.

## Estratégia de testes

### Domínio

- redução de 4h para 2h diárias;
- aumento de disponibilidade;
- falta diária;
- conclusão antecipada;
- execução parcial 25/60;
- dia e tarefa bloqueados;
- matéria crítica e manutenção;
- limite de monopolização;
- capacidade insuficiente;
- redistribuição sem bola de neve;
- dependências;
- fraqueza com amostra mínima;
- previsão;
- total semanal;
- determinismo.

### Persistência

- exclusividade de ativo e Mestre;
- incremento e conflito de revisão;
- rejeição atômica de proposta obsoleta;
- arquivamento;
- duplicação sem execução;
- preservação de histórico;
- vínculo e remoção de tópico;
- migração 4→5;
- rollback em erro.

### Transferência

- `.plano` v1 válido;
- JSON, formato e versão inválidos;
- UUIDs e dependências inválidas;
- vínculos resolvidos e ausentes;
- CREATE, MERGE e REPLACE_FUTURE;
- histórico prevalecendo;
- saldo parcial;
- bloqueios preservados.

### Regressão

- `.estudo` v1/v2;
- atualização de conteúdo com histórico;
- questões e progresso;
- roteamento de arquivos;
- migração do banco v4;
- exclusão de concurso;
- restauração de backups anteriores.

### UI e entrega

Testes Compose cobrirão navegação, estado vazio, ações de Hoje, déficit, arquivamento, abertura de tópico e prévia de importação. A validação final executará testes unitários, testes instrumentados quando houver dispositivo, lint e `assembleDebug`.

## Sequência incremental

1. Disponibilizar JDK 17 e registrar a linha de base.
2. Implementar domínio puro e motor por TDD.
3. Adicionar Room v5 e migração.
4. Implementar serviços transacionais e execução.
5. Implementar `.plano` e contexto.
6. Implementar ViewModel e assistente.
7. Implementar Hoje.
8. Implementar Semana, Mês e Ano.
9. Integrar navegação e mover Erros para Mais.
10. Executar regressões, lint, build e validação final.

## Critério de conclusão

O módulo somente estará concluído quando funcionar o fluxo:

```text
CRIAR PLANO
→ DEFINIR TEMPO DISPONÍVEL
→ GERAR SEMANA
→ VER HOJE
→ CONCLUIR ESTUDO
→ ATUALIZAR PROGRESSO
→ ALTERAR DISPONIBILIDADE
→ REPROGRAMAR O FUTURO
→ EXPORTAR CONTEXTO
→ IMPORTAR NOVO .plano
→ MESCLAR SEM PERDER HISTÓRICO
```

## Restrição ambiental conhecida

A linha de base não inicia com o Java 25.0.1 atualmente selecionado: Gradle/Kotlin falha ao interpretar essa versão. O projeto continuará direcionado a Java 17 e deverá ser validado com um JDK 17 compatível.
