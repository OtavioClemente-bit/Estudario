# Questões do Plano e Histórico do Foco — Plano de Implementação

> **For implementers:** execute cada tarefa neste checkout, seguindo TDD; não reverta alterações que já estavam no checkout.

**Goal:** Fazer baterias de questões do plano abrirem questões salvas e se concluírem com XP uma vez, corrigir sua duração estimada, dar acesso próprio ao histórico do foco e suavizar o indicador do botão de foco.

**Architecture:** O plano estima baterias com os minutos por questão sem arredondá-las para o bloco de teoria. O botão da tarefa consulta questões salvas e abre o quiz escopado à matéria/tópico; ao terminar, o quiz registra a execução pelo serviço existente, que protege a conclusão e o XP contra duplicação. Um destino dedicado reutiliza os dados de sessões de foco no menu lateral, e o ícone ativo usa um único contorno sutil.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation Compose, Room, JUnit, Compose UI tests.

**Spec:** Desenho aprovado pelo usuário nesta conversa em 2026-09-22.

## Global Constraints

- Trabalhar no checkout atual e preservar as alterações locais preexistentes.
- Concluir uma tarefa do plano pelo fluxo transacional existente e nunca conceder XP de tarefa duas vezes.
- Mostrar somente questões já persistidas para o tópico/matéria planejados.
- Manter o indicador de foco dentro do formato do botão, sem halo externo.

## Review Focus

- Zero questões salvas no escopo: avisar e manter a tarefa pendente; há teste unitário do filtro vazio e o fluxo de UI foi compilado.
- Menos questões salvas que a quantidade planejada: usar as disponíveis e registrar o total real no quiz.
- Quiz abandonado: a conclusão só é chamada no encerramento explícito do quiz.
- Quiz finalizado ou callback repetido: o serviço transacional conclui uma vez; há teste instrumentado compilado para essa proteção.
- Histórico vazio ou foco ativo: destino separado com resumo, estado ativo e lista de sessões; os testes Compose foram compilados.

---

### Task 1: Corrigir a duração estimada das baterias

**Files:**
- Modify: `app/src/main/java/br/com/estudario/domain/planner/StudyPlanBlueprint.kt`
- Modify: `app/src/main/java/br/com/estudario/data/planner/StudyPlanApplicationService.kt`
- Create: `app/src/main/java/br/com/estudario/domain/planner/QuestionTaskDurationCorrection.kt`
- Test: `app/src/test/java/br/com/estudario/domain/planner/StudyPlanBlueprintTest.kt`
- Test: `app/src/test/java/br/com/estudario/domain/planner/QuestionTaskDurationCorrectionTest.kt`

**Interfaces:** as tarefas `QUESTIONS` usam `questions * config.minutesPerQuestion` diretamente; arredondamento em blocos continua para teoria e atividades que representam sessões completas.

- [x] **Step 1: Add failing test** — adicione caso para 15 questões, 2 min/questão e bloco de 50; a demanda de questões deve ser 30 minutos.
- [x] **Step 2: Verify RED** — rode `gradlew.bat testDebugUnitTest --tests br.com.estudario.domain.planner.StudyPlanBlueprintTest`; confirme que a duração atual arredonda para 50.
- [x] **Step 3: Implement** — retire `StudyMethod.toBlocks` dos cálculos de questões e corrija estimativas antigas ainda pendentes ao abrir o Plano.
- [x] **Step 4: Verify GREEN** — `StudyPlanBlueprintTest` e `QuestionTaskDurationCorrectionTest` passaram.

### Task 2: Iniciar e concluir questões do plano pelo quiz

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/planner/PlanScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/planner/StudyPlanViewModel.kt`
- Modify: `app/src/main/java/br/com/estudario/data/planner/StudyExecutionService.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/screens/QuizScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Test: `app/src/test/java/br/com/estudario/ui/planner/PlannedQuestionSelectionTest.kt`
- Test: `app/src/androidTest/java/br/com/estudario/data/planner/StudyPlanApplicationServiceTest.kt`

**Interfaces:** `QuizConfig` recebe `planTaskId: String?`; a rota do quiz preserva-o; o encerramento recebe os dados reais da sessão e chama uma conclusão forçada e única da tarefa.

- [x] **Step 1: Add failing tests** — filtre questões por tópico/matéria e cubra conclusão única no serviço.
- [x] **Step 2: Verify RED** — os testes de seleção e conclusão falharam antes das implementações correspondentes.
- [ ] **Step 3: Implement** — filtre questões por tópico ou matéria; avise quando não houver nenhuma; marque a tarefa iniciada ao abrir; ao finalizar, grave sessão e execução, force `CONCLUIDA` independentemente do tempo e replaneje.
- [x] **Step 3: Implement** — filtre questões por tópico ou matéria; avise quando não houver nenhuma; marque a tarefa iniciada ao abrir; ao finalizar, grave sessão e execução, force `CONCLUIDA` independentemente do tempo e replaneje.
- [ ] **Step 4: Verify GREEN** — testes unitários passaram e os instrumentados compilaram; execução em dispositivo ficou indisponível porque o Android recusou a instalação do APK de teste e o AVD local permaneceu offline.

### Task 3: Histórico no menu e contorno discreto do foco

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/navigation/EstudarioDrawer.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/navigation/FocusNavigationIcon.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- Create: `app/src/main/java/br/com/estudario/ui/focus/FocusHistoryScreen.kt`
- Test: `app/src/androidTest/java/br/com/estudario/ui/FocusNavigationTest.kt`

**Interfaces:** o drawer recebe a ação `onFocusHistory`; a rota `focus-history` mostra resumo e lista de sessões persistidas; o estado ativo do botão desenha um único contorno primário de baixa intensidade.

- [x] **Step 1: Add failing tests** — confirme o destino próprio no drawer e cubra a tela e o indicador ativo nos testes de navegação.
- [ ] **Step 2: Verify RED** — o teste unitário do drawer acusou a ausência do callback antes da implementação; os testes Compose dependem de dispositivo.
- [x] **Step 3: Implement** — adicione a tela/rota e a entrada do menu; remova os dois anéis pulsantes e mantenha só uma borda sutil colada ao círculo do botão.
- [ ] **Step 4: Verify GREEN** — teste unitário do drawer passou e os testes Compose compilaram; execução ficou indisponível por falta de dispositivo instalável.

### Task 4: Verificação final

- [x] Rode `gradlew.bat testDebugUnitTest`.
- [x] Compile os testes instrumentados com `gradlew.bat compileDebugAndroidTestKotlin`.
- [ ] Rode os testes instrumentados de plano e foco em dispositivo autorizado.
- [x] `git diff --check` sem erros; alterações preexistentes foram preservadas.
