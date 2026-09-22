# Menu lateral global e desempenho baseado em dados — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Tornar o menu do Estudário acessível em toda tela completa, sem duplicar seu acionador, e fazer Desempenho apresentar avaliação explicável e filtrada do histórico real.
**Architecture:** Incremento sobre `2026-09-21-home-second-refactor.md`: manter um `ModalNavigationDrawer` e `Scaffold` globais em `MainNavigation`; as quatro áreas principais seguem como únicas abas inferiores. Criar avaliador de domínio puro e uma camada de estado fora do composable; reaproveitar os `StateFlow`s existentes no `AppViewModel` e entidades Room. Manter fontes de duração separadas quando houver possível sobreposição.
**Tech Stack:** Kotlin, Jetpack Compose Material 3 Navigation, AndroidX ViewModel/Flow, Room entities existentes, JUnit e Compose instrumentation.
**Spec:** `docs/superpowers/specs/2026-09-21-global-drawer-performance-design.md`

## Global Constraints

- Preservar a política de insets e a top/bottom bar implementadas pelo plano `2026-09-21-home-second-refactor.md`; não voltar a tratar `showBottom` como condição para o shell inteiro.
- Um só `ModalNavigationDrawer`, uma só ação de abrir menu do Estudário por tela; não introduzir um segundo hambúrguer nos cabeçalhos internos. Preservar back em rotas empilhadas e as quatro abas inferiores.
- Abertura por botão deve funcionar em todas as rotas completas. Gestos de borda podem ser desativados em rotas em que competem com leitura, quiz ou foco; o acesso visível por botão permanece.
- Avaliação determinística e auditável. Não criar score geral 0–100, não inferir retenção de `ReviewHistoryEntity`, não misturar durações possivelmente sobrepostas e não alterar eventos antigos.
- Usar janela local explícita para todo indicador filtrado. A sequência “até hoje” deve ser rotulada separadamente da janela selecionada.
- Preservar alterações preexistentes no working tree, especialmente `StudyPaceEvaluator.kt` não rastreado e os arquivos que o plano Home já esteja modificando.

## Review Focus

- Destinos secundários mantêm menu, ação de voltar e back stack; não há barra inferior em detalhes nem topo duplicado.
- Rota de calendário/notificação/quiz e gestos horizontais continuam funcionais; insets não deslocam conteúdo sob status/navigation bars.
- Em `topic/{id}`, o relógio/status bar não cobre o cabeçalho, a navegação do sistema não corta as ações/conteúdo final, e a lista ainda rola até o fim em viewport compacto.
- Limites de data local e janela anterior cobrem troca de mês, fim de dia e mudança de fuso.
- Tempo de sessões de estudo e de questões fica em subtotais, salvo prova explícita de que as durações não se sobrepõem.
- Plano parcial, pausado, não realizado e sem execução não produz aderência falsamente perfeita.
- Amostras abaixo de 10 tentativas mostram quantidade e “Poucos dados”; nenhuma recomendação conclui sobre retenção.

---

### Task 1: Manter uma navegação global e um único acionador

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/navigation/EstudarioDrawer.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/TopicDetailScreen.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/ui/InsetsNavigationTest.kt`

**Interfaces:** `MainNavigation` calcula separadamente `isMainDestination` (bottom bar) e `drawerGesturePolicy` (rotas com gesto compatível). `EstudarioTopBar` conserva a marca clicável como único acionador do drawer e aceita ação opcional de voltar/título em rotas empilhadas.

- [ ] **Step 1: Registrar os casos de regressão no teste**

Estender `PlannerNavigationTest`: validar quatro abas, abrir menu em `Plano`, navegar a partir do menu para `Desempenho`, `Ajustes`, `Notificações`, `Revisões espaçadas` e `Histórico e fontes`, e reabrir sem voltar à Home. Cobrir também drawer por botão nas rotas completas `topic/{id}`, `theory`, `quiz`, `review-session`, `queue`, `search`, `profile`, `badges`, `focus` e `errors`, com fixtures mínimas para argumentos obrigatórios. Validar em rota empilhada que voltar retorna ao destino anterior e a marca abre o mesmo drawer. Usar os nós semânticos e títulos reais de cada tela.
- [ ] **Step 2: Ajustar shell para separar drawer da navegação inferior**

Manter `showBottom = currentRoute in destinations.map { it.route }`, mas renderizar a top bar global para todas as rotas completas e habilitar o drawer por botão em qualquer uma. Derivar rota empilhada para ação de voltar sem substituir o acionador de menu. Auditar barras próprias das rotas: consolidar título/voltar na barra comum quando possível e preservar ações contextuais necessárias sem repetir marca nem criar um segundo acionador de menu. Manter `ModalNavigationDrawer` envolvendo o `Scaffold`/`NavHost`; limitar a `NavigationBar` às quatro rotas atuais. Implementar navegação de destino sem duplicar a rota atual e continuar usando `popUpTo("home")` com save/restore para abas.
- [ ] **Step 3: Tornar política de gesto explícita e verificar insets**

Permitir gesto do drawer nas telas compatíveis; desabilitá-lo nas rotas em que o conteúdo usa gesto de borda/horizontal (identificar em `NavHost`, especialmente teoria, quiz e foco). O botão de marca permanece sempre habilitado. Corrigir a responsabilidade dos insets por camada: a barra global reserva a região superior; o conteúdo de `topic/{id}` respeita a região inferior segura quando não há bottom bar, sem duplicar o inset superior. Atualizar `InsetsNavigationTest` para cobrir esse caso em viewport compacto com status/navigation insets explícitos: cabeçalho abaixo do relógio, ações visíveis, último conteúdo alcançável por rolagem acima da navegação. Atualizar também os testes para confirmar drawer visível em uma rota secundária e que a navegação inferior não aparece nela.
- [ ] **Step 4: Executar testes e commit seletivo**

```powershell
./gradlew.bat :app:compileDebugAndroidTestKotlin
./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.PlannerNavigationTest
./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.InsetsNavigationTest
```

Registrar se device/emulador indisponível. Quando disponível, conferir visualmente `topic/{id}` em aparelho compacto com barras do sistema visíveis e fontes padrão/ampliadas. `git diff --check`; stage apenas os arquivos deste task e commitar `feat: expose Estudario drawer across app screens`.

### Task 2: Definir contratos puros da avaliação de desempenho

**Files:**
- Create: `app/src/main/java/br/com/estudario/domain/performance/StudyPerformance.kt`
- Create: `app/src/test/java/br/com/estudario/domain/performance/StudyPerformanceEvaluatorTest.kt`

**Interfaces:** `StudyPerformanceEvaluator.evaluate(input: StudyPerformanceInput, period: StudyPerformancePeriod, today: LocalDate, zoneId: ZoneId): StudyPerformanceResult`. Entrada usa eventos simples em epoch millis, IDs reais, `PlanTaskStatus` e datas locais, para permitir JUnit sem Android/Room. Resultado inclui janela atual/anterior, dias/sessões, questões/acertos, revisões, tarefas, subtotais de tempo, matéria/tópico e recomendações justificadas.

```kotlin
enum class StudyPerformancePeriod(val days: Int?) { DAYS_7(7), DAYS_30(30), DAYS_90(90), ALL(null) }
data class PerformanceAttempt(val answeredAt: Long, val subjectId: Long?, val topicId: Long?, val correct: Boolean)
data class PerformanceReview(val reviewedAt: Long)
data class PerformanceSession(val completedAt: Long, val durationSeconds: Long)
data class PerformancePlannedTask(val planId: String, val scheduledDate: LocalDate, val status: PlanTaskStatus)
data class PerformanceExecution(val completedAt: Long, val taskId: String?, val actualMinutes: Int, val questionsDone: Int, val correctAnswers: Int)
data class StudyPerformanceInput(
    val attempts: List<PerformanceAttempt>, val reviews: List<PerformanceReview>,
    val studySessions: List<PerformanceSession>, val questionSessions: List<PerformanceSession>,
    val activePlanIds: Set<String>, val tasks: List<PerformancePlannedTask>,
    val executions: List<PerformanceExecution>,
)
data class WindowMetrics(
    val activeDays: Int, val studySessions: Int, val questionSessions: Int,
    val attempts: Int, val correctAttempts: Int, val reviews: Int,
    val plannedTasks: Int, val completedTasks: Int,
    val accuracyPercent: Int?, val taskCompletionPercent: Int?,
    val studyMinutes: Int, val questionMinutes: Int,
)
data class TopicPerformance(val topicId: Long, val attempts: Int, val correct: Int, val accuracyPercent: Int?, val hasSufficientSample: Boolean)
data class SubjectPerformance(val subjectId: Long, val attempts: Int, val correct: Int, val accuracyPercent: Int?, val hasSufficientSample: Boolean, val topics: List<TopicPerformance>)
data class PerformanceRecommendation(val message: String, val evidence: String)
data class StudyPerformanceResult(
    val period: StudyPerformancePeriod, val current: WindowMetrics, val previous: WindowMetrics?, val startInclusive: Instant?,
    val endExclusive: Instant, val dailyActivity: List<Int>, val subjects: List<SubjectPerformance>,
    val recommendations: List<PerformanceRecommendation>, val streakThroughToday: Int?,
)
```

- [ ] **Step 1: Escrever testes das fronteiras de período**

Com `today` e `ZoneId` injetados, testar datas imediatamente antes/no início/no fim de cada janela, mês/ano atravessado e UTC timestamp que cai no dia anterior local. Janela finita inclui hoje e os N−1 dias anteriores via `[startOfDay, tomorrowStart)`; comparação usa janela imediatamente anterior com o mesmo número de dias. “Tudo” termina amanhã à meia-noite e não tem janela anterior.

```kotlin
val endExclusive = today.plusDays(1).atStartOfDay(zoneId).toInstant()
val startInclusive = today.minusDays(days - 1L).atStartOfDay(zoneId).toInstant()
assertTrue(eventInstant >= startInclusive && eventInstant < endExclusive)
```
- [ ] **Step 2: Escrever testes das regras de evidência**

Cobrir tentativas corretas/incorretas, total zero, amostra 9 vs 10 por matéria e tópico, denominador vazio retornando percentual nulo, contagem de revisões sem inferir lembrança, dias ativos/sessões, e recomendações apenas quando os limiares e contagem mínima forem atendidos. Testar `Tudo` sem comparação e dados vazios/insuficientes como estados distintos.
- [ ] **Step 3: Implementar o avaliador puro**

`StudyPerformanceEvaluator.evaluate(input, period, today, zoneId)` filtra todas as fontes pela mesma janela antes de calcular indicadores. Acurácia usa tentativas respondidas (`QuestionAttemptEntity.answeredAt`); tópico/matéria usa associações reais da tentativa/pergunta. Revisão mede apenas quantidade de `ReviewHistoryEntity.reviewedAt`. A atividade diária marca um dia ativo se existir ao menos um evento elegível, sem somar fontes de eventos. Calcular a sequência atual em separado sobre todo o histórico até hoje e apresentar como “até hoje”, nunca como métrica do período. Emitir “Reforce esta matéria” apenas com 10+ respostas e acerto abaixo de 70%; recomendar rever carga apenas com 5+ tarefas e conclusão abaixo de 70%. Cada orientação inclui a contagem, taxa e período que dispararam a regra.
- [ ] **Step 4: Calcular aderência e duração sem inflar resultados**

Usar tarefas do plano ativo não arquivado, agendadas na janela; `PAUSADA` não entra no denominador, `NAO_REALIZADA` entra como não concluída e `CONCLUIDA` conta uma tarefa. Testar que denominador zero dá `taskCompletionPercent = null`, nunca 100%; `EM_ANDAMENTO` com execução parcial permanece não concluída. Não criar estado “cancelada” que o domínio não tem: tarefa removida não existe no histórico e deve ser descrita como indisponível para análise. Execuções parciais mostram minutos/questões registrados, mas não convertem sozinhas tarefa parcial em concluída. Retornar duração de sessões de estudo e de questões em subtotais distintos, sem somar os dois. Ignorar tempos negativos e proteger agregações de overflow.
- [ ] **Step 5: Rodar suite pura e commit**

```powershell
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.domain.performance.StudyPerformanceEvaluatorTest"
```

Commit seletivo: `feat: add evidence-based study performance evaluator`.

### Task 3: Adaptar fontes existentes a estado de desempenho observável

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/AppViewModel.kt`
- Create: `app/src/main/java/br/com/estudario/ui/screens/performance/StudyPerformanceUiState.kt`
- Create: `app/src/main/java/br/com/estudario/ui/screens/performance/StudyPerformanceInputMapper.kt`
- Modify: `app/src/main/java/br/com/estudario/data/local/planner/PlannerDao.kt`
- Create: `app/src/test/java/br/com/estudario/ui/screens/performance/StudyPerformanceInputMapperTest.kt`

- [ ] **Step 1: Confirmar fluxos disponíveis antes de adicionar acesso ao banco**

Reusar `AppViewModel.attempts`, `questions`, `subjects`, `topics`, `reviewHistory`, `studySessions`, `questionSessions` e `planExecutions`; `studyPlans` já existe como flow privado, então expor apenas os IDs ativos/não arquivados necessários. Acrescentar `@Query("SELECT * FROM plan_tasks") fun tasks(): Flow<List<PlanTaskEntity>>` ao `PlannerDao` e expor via `stateIn`. Não buscar o banco repetidamente dentro do composable.
- [ ] **Step 2: Criar adaptador e estado da tela**

Criar mapper puro `StudyPerformanceInputMapper.from(attempts: List<QuestionAttemptEntity>, questions: List<QuestionEntity>, subjects: List<SubjectEntity>, topics: List<TopicEntity>, reviewHistory: List<ReviewHistoryEntity>, studySessions: List<StudySessionEntity>, questionSessions: List<QuestionSessionEntity>, plans: List<StudyPlanEntity>, tasks: List<PlanTaskEntity>, executions: List<StudyTaskExecutionEntity>): StudyPerformanceInput`; para cada tentativa, achar `QuestionEntity` pelo `questionId`, derivar `topicId` e matéria via tópico, preservando `null` quando a associação não existe. Criar `StudyPerformanceUiState(period, result, loading, hasAnyActivity)` em arquivo próprio. No `AppViewModel`, expor `MutableStateFlow<StudyPerformancePeriod>` por `selectPerformancePeriod(period)` e combinar os flows para avaliar o mesmo snapshot quando dado ou filtro mudar. Estados: carregando, sem atividade, dados insuficientes e avaliação completa.
- [ ] **Step 3: Testar mapeamento das relações reais**

Garantir que tentativa aponta à matéria/tópico corretos, execução conecta à tarefa somente por `taskId`, revisão não carrega campo de retenção inventado e fontes temporais não se somam. Incluir um caso com plano antigo arquivado para não contaminar aderência do plano ativo. Executar o teste puro do mapper:

```powershell
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.ui.screens.performance.StudyPerformanceInputMapperTest"
```
- [ ] **Step 4: Compilar e commitar**

Executar `./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.domain.performance.StudyPerformanceEvaluatorTest"` e `./gradlew.bat :app:compileDebugKotlin`; commitar apenas os arquivos tocados neste task com `feat: expose performance evaluation state`.

### Task 4: Construir a tela de Desempenho útil e honesta

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/screens/StatisticsScreen.kt`
- Create: `app/src/androidTest/java/br/com/estudario/ui/screens/StatisticsPresentationTest.kt`

- [ ] **Step 1: Definir testes Compose para conteúdo e empty states**

Dar `testTag` estável aos seletores de período e cobrir os estados principais com Compose:

```kotlin
compose.onNodeWithTag("performance-period-7").performClick()
compose.onNodeWithText("Poucos dados · 9 respostas").assertExists()
compose.onNodeWithText("Retenção").assertDoesNotExist()
```

Testar 7/30/90/Tudo selecionável, números com período e denominador claros, subtotais de tempo separados, revisões nomeadas “concluídas” sem texto de retenção, amostra suficiente com 10+, estado vazio instruindo qual atividade registra dados e recomendações acompanhadas da evidência usada.
- [ ] **Step 2: Substituir métricas locais ad hoc por `StudyPerformanceUiState`**

Manter o callback de navegação/back existente, mas remover cálculo no composable de atividade não filtrada e acurácia baseada no banco acumulado. Construir hierarquia legível: resumo do período; consistência/atividade diária; questões e evolução; matérias/tópicos com tamanho da amostra; revisões; plano previsto vs concluído; tempo por fonte; recomendações acionáveis. A comparação finita exibe variação contra período anterior; “Tudo” omite comparação. Rotular sequência atual como “até hoje”, fora do filtro.
- [ ] **Step 3: Testar acessibilidade e largura compacta**

Testar rolagem em 360 dp, nomes longos, fontes ampliadas, navegação no período por semântica e conteúdo em dark theme. Não fazer controles apenas por cor nem gráfico sem descrição semântica.
- [ ] **Step 4: Rodar testes e commit**

```powershell
./gradlew.bat :app:compileDebugAndroidTestKotlin
./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.screens.StatisticsPresentationTest
```

Reportar separadamente indisponibilidade de device. Commit seletivo: `feat: rebuild statistics around study evidence`.

### Task 5: Revisão de shell, período e apresentação em conjunto

**Files:**
- `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- `app/src/main/java/br/com/estudario/ui/navigation/EstudarioDrawer.kt`
- `app/src/main/java/br/com/estudario/ui/AppViewModel.kt`
- `app/src/main/java/br/com/estudario/data/local/planner/PlannerDao.kt`
- `app/src/main/java/br/com/estudario/domain/performance/StudyPerformance.kt`
- `app/src/main/java/br/com/estudario/ui/screens/performance/StudyPerformanceUiState.kt`
- `app/src/main/java/br/com/estudario/ui/screens/performance/StudyPerformanceInputMapper.kt`
- `app/src/main/java/br/com/estudario/ui/screens/StatisticsScreen.kt`
- `app/src/androidTest/java/br/com/estudario/ui/PlannerNavigationTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/InsetsNavigationTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/screens/StatisticsPresentationTest.kt`
- `app/src/test/java/br/com/estudario/domain/performance/StudyPerformanceEvaluatorTest.kt`
- `app/src/test/java/br/com/estudario/ui/screens/performance/StudyPerformanceInputMapperTest.kt`

- [ ] **Step 1: Rodar verificações focadas**

```powershell
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.domain.performance.StudyPerformanceEvaluatorTest"
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.ui.screens.performance.StudyPerformanceInputMapperTest"
./gradlew.bat :app:compileDebugAndroidTestKotlin
./gradlew.bat :app:assembleDebug
```

- [ ] **Step 2: Rodar regressões instrumentadas**

Executar `PlannerNavigationTest`, `InsetsNavigationTest` e `StatisticsPresentationTest` pelo runner conectado. Checar abertura de drawer por botão em plano, detalhe/rota secundária e desempenho; back stack; apenas um acionador; bottom bar apenas nos quatro destinos; insets e gestos do quiz/foco.
- [ ] **Step 3: Revisar dados de ponta a ponta**

Verificar manualmente fixtures determinísticas para cada período e estado vazio. Confirmar que todas as métricas mudam ao trocar período, “Tudo” não compara janelas, a sequência está rotulada até hoje, planos pausados/arquivados não inflacionam denominador, revisão não é retenção e duração não é somada entre fontes.
- [ ] **Step 4: Conferir diff e informar evidências**

Rodar `git diff --check`, rever somente alterações deste escopo sem desfazer outros arquivos do usuário e informar comandos/resultados. Não declarar teste de device nem inspeção visual se não tiver ocorrido.
