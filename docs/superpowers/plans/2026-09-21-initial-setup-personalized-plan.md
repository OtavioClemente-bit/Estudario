# Plano inicial personalizado e completo — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Completar o onboarding de plano já existente com disponibilidade ajustável, dificuldade por matéria e rotas automática/IA alimentadas pelo mesmo edital real, sem declarar completo um cronograma que omita conteúdo.
**Architecture:** Plano incremental sobre `2026-09-21-initial-setup-experience.md`; manter o motor determinístico, o estado DataStore recuperável e a importação tipada `.plano`. Acrescentar um avaliador puro de cobertura e campos opcionais retrocompatíveis somente onde a configuração atual perde bloco/perfil. Nenhuma migração Room é necessária.
**Tech Stack:** Kotlin, Jetpack Compose Material 3, DataStore snapshot codec posicional existente, `JSONObject` do `.plano`, planner domain/services, JUnit e Compose instrumentation.
**Spec:** `docs/superpowers/specs/2026-09-21-initial-setup-personalized-plan-design.md`

## Global Constraints

- Este é um delta ao plano inicial já aprovado; não reimplementar aquisição/revisão do edital ou o restante do onboarding.
- Antes de alterar `InitialSetupScreen.kt`, `InitialSetupViewModel.kt` ou arquivos não rastreados de onboarding, inspecionar e preservar o trabalho já existente no working tree. Nunca substituir arquivos inteiros para resolver conflito.
- Não misturar dificuldade declarada com prioridade/evidência oficial do edital. Usar o maior nível apenas como prioridade efetiva de distribuição e prompt.
- Manter o esquema `.plano` versão 1 válido para arquivos antigos: novos campos opcionais têm defaults; não exigir reexportação nem criar migração Room.
- Não finalizar a configuração quando o rascunho de IA omitir algum tópico de origem ou uma matéria sem tópicos. Preservar texto importado e estado atual para correção/fallback.
- Não inventar tópicos, IDs, progresso ou metas. Se a duração até a prova não for viável no formato atual, informar o limite em vez de truncar o plano silenciosamente.

## Review Focus

- Codec do onboarding: snapshot antigo continua decodificando com dificuldade intermediária e disponibilidade intacta.
- Prioridade efetiva: comparações explícitas para todos os níveis de `PlanPriority`; prioridade oficial não é sobrescrita.
- Calendário IA: prova futura além de quatro semanas passa a ser o fim do horizonte; sem prova permanece ciclo de 4 semanas.
- Cobertura: comparar todo ID de tópico real, rejeitar matéria sem tarefa e não descartar proposta válida ao apresentar erro.
- `.plano`: round-trip de novos campos e leitura de fixtures antigas usando defaults de 50 minutos/`DO_ZERO`.
- UI: slider 0–1440 em passos de 15 com valor legível, total semanal e sem perda de acessibilidade/fonte ampliada.

---

### Task 1: Persistir dificuldade por matéria e introduzir etapa de onboarding

**Files:**
- Modify: `app/src/main/java/br/com/estudario/domain/setup/InitialSetup.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupViewModel.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Modify: `app/src/test/java/br/com/estudario/domain/setup/InitialSetupTest.kt`

**Interfaces:** `InitialSetupSnapshot.version` passa a defaultar em `2`; `subjectDifficulties: Map<String, SubjectDifficulty> = emptyMap()` é serializado no fim do formato atual. `SubjectDifficulty` tem `EASY`, `MEDIUM`, `HARD`. Etapa `SUBJECT_DIFFICULTY` fica entre `AVAILABILITY` e `PLAN_METHOD`.

```kotlin
enum class SubjectDifficulty { EASY, MEDIUM, HARD }
```

- [ ] **Step 1: Criar testes de domínio que falham primeiro**

Adicionar testes de encode/decode para mapas vazios e preenchidos, reinício com snapshot salvo, snapshot antigo sem campo final e filtro de seleções para IDs de matérias ainda existentes. Verificar também navegação forward/back: `AVAILABILITY -> SUBJECT_DIFFICULTY -> PLAN_METHOD`.

```kotlin
val value = InitialSetupSnapshot(subjectDifficulties = mapOf("materia-1" to SubjectDifficulty.HARD))
assertEquals(value, InitialSetupSnapshotCodec.decode(InitialSetupSnapshotCodec.encode(value)))
val legacy = listOf("1", "NOT_STARTED", "INTRO", "", "", "", "", "", "", "", "DO_ZERO", "120,120,120,120,120,120,0", "50", "AUTOMATIC", "", "").joinToString("|")
assertTrue(InitialSetupSnapshotCodec.decode(legacy).subjectDifficulties.isEmpty())
```

```powershell
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.domain.setup.InitialSetupTest"
```

Esperado antes da implementação: falha de compilação/teste pela ausência do novo campo/etapa; após Task 1, tudo passa. Acrescentar o campo ao final do codec posicional (não inserir no meio), elevar a versão para 2 e decodificar snapshots antigos com mapa vazio.

- [ ] **Step 2: Implementar mutação de seleção com IDs estáveis**

Adicionar `setSubjectDifficulty(subjectId: String, value: SubjectDifficulty)` ao `InitialSetupViewModel`; ao carregar o edital, preservar IDs que ainda existem e inicializar os novos como `MEDIUM`. Não persistir nomes como chave. Adicionar a transição e o `when` exhaustivo da tela; manter o botão de voltar usando `InitialSetupTransitions.previous`. Definir `CURRENT_VERSION = 2`, gravar essa constante em toda codificação mesmo após ler snapshot antigo, manter versão 1 legível, ler campo final ausente como mapa vazio e codificar cada chave com `encodeText` para proteger os separadores posicionais existentes.

```kotlin
InitialSetupStep.AVAILABILITY to InitialSetupStep.SUBJECT_DIFFICULTY
InitialSetupStep.SUBJECT_DIFFICULTY to InitialSetupStep.PLAN_METHOD
```

- [ ] **Step 3: Verificar e registrar**

Executar o teste focado e `git diff --check`; revisar que disponibilidade existente não muda ao decodificar snapshot anterior. Fazer commit seletivo deste task: `git add -- <quatro arquivos listados>` e `git commit -m "feat: persist subject difficulty in setup"`.

### Task 2: Unificar dados reais do edital e cálculo de prioridade efetiva

**Files:**
- Create: `app/src/main/java/br/com/estudario/domain/setup/SubjectPlanningPriority.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupViewModel.kt`
- Modify: `app/src/test/java/br/com/estudario/domain/setup/InitialSetupTest.kt`
- Create: `app/src/test/java/br/com/estudario/domain/setup/SubjectPlanningPriorityTest.kt`

**Interfaces:** `effectivePriority(official: PlanPriority, difficulty: SubjectDifficulty): PlanPriority` e função para converter cada `PlanPriority` em rank explícito. Nunca depender da ordem ordinal dos enums. `PlanPromptOptions` já tem `horizonWeeks`, `dayMinutes` e `priorities`; adicionar `blockMinutes` e `studyProfile` e corrigir a regra de fim.

```kotlin
private fun rank(p: PlanPriority) = when (p) {
    PlanPriority.LOW -> 1; PlanPriority.MEDIUM -> 2
    PlanPriority.HIGH -> 3; PlanPriority.CRITICAL -> 4
}
fun SubjectDifficulty.toPlanningPriority() = when (this) {
    SubjectDifficulty.EASY -> PlanPriority.LOW
    SubjectDifficulty.MEDIUM -> PlanPriority.MEDIUM
    SubjectDifficulty.HARD -> PlanPriority.HIGH
}
fun effectivePriority(official: PlanPriority, difficulty: SubjectDifficulty) =
    listOf(official, difficulty.toPlanningPriority()).maxBy(::rank)
```

- [ ] **Step 1: Fixar por teste a tabela de mapeamento**

Cobrir facilidade/intermediária/dificuldade cruzadas com prioridades oficiais baixa/média/alta. Resultado = maior rank; casos em que a prioridade oficial é alta permanecem altos mesmo com facilidade declarada. Confirmar por teste que o DTO/entidade oficial permanece igual.
- [ ] **Step 2: Construir uma única entrada de plano a partir do edital**

No ViewModel, obter matérias e tópicos ativos do concurso selecionado, garantir IDs externos estáveis usando o fluxo já existente, carregar progresso real de questões e associar cada `PlanSubjectInfo(id, name, topics, answered, accuracyPercent)` à árvore completa. A entrada comum para plano automático e IA deve fornecer: disponibilidade diária, `sessionMinutes`, perfil, data de prova e prioridade efetiva.

Para o motor automático, substituir `PlanSubjectInput(..., PlanPriority.MEDIUM, ...)` fixo pela prioridade efetiva sem alterar prioridade oficial armazenada. Para IA, preencher `PlanTopicInfo` por cada tópico real; não enviar `emptyList()` quando há edital.
- [ ] **Step 3: Testar input comum e regressões**

Adicionar testes de mapeamento de tópico/IDs e fallback médio para seleção ausente. Rodar:

```powershell
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.domain.setup.SubjectPlanningPriorityTest" --tests "br.com.estudario.domain.setup.InitialSetupTest"
```

Revisar ausência de writes à importância oficial. Commit seletivo dos arquivos do task: `git commit -m "feat: personalize plan priorities by subject"`.

### Task 3: Trocar opções fixas por sliders claros e acessíveis

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/ui/InitialSetupFlowTest.kt` (criar se ausente)

- [ ] **Step 1: Testar valor e leitura da disponibilidade**

No teste Compose, ajustar um dia por semântica/gesto para 0, 120 e 135 minutos; confirmar rótulos “Folga”, “2 h” e “2 h 15 min”, total semanal atualizado e que o outro dia não mudou. Garantir que dias e valores têm descrições acessíveis e são alcançáveis sem arrastar.
- [ ] **Step 2: Implementar controles e explicação de bloco**

Em `AvailabilityStep`, substituir chips 0/60/120/180 por `Slider(valueRange = 0f..1440f, steps = 95)` e arredondar `onValueChange` para múltiplos de 15 antes de chamar `setAvailability`. Extrair formatter puro com estes resultados fixos:

```kotlin
formatAvailabilityMinutes(0) == "Folga"
formatAvailabilityMinutes(120) == "2 h"
formatAvailabilityMinutes(135) == "2 h 15 min"
```

Implementação do controle deve preservar o contrato acessível do Material Slider:

```kotlin
Slider(
    value = minutes.toFloat(),
    onValueChange = { raw ->
        val snapped = ((raw / 15f).roundToInt() * 15).coerceIn(0, 1440)
        viewModel.setAvailability(dayIndex, snapped)
    },
    valueRange = 0f..1440f,
    steps = 95,
)
```

Mostrar valor da faixa em formato pt-BR e total semanal. Manter bloco como escolha separada; apresentar exatamente: “O bloco é o tamanho-base de cada tarefa do plano. Não é o total de estudo do dia; as tarefas usam múltiplos do bloco dentro do tempo disponível.” Usar rótulos `25 min`, `45 min` etc.
- [ ] **Step 3: Montar seleção de dificuldade por matéria**

Renderizar cartão por matéria com escolhas “Tenho facilidade”, “Intermediária”, “Tenho dificuldade”, seleção única visível e descrições sem truncar nomes extensos. Reusar o componente de seleção atual se houver; não criar ícones duplicados. Cobrir largura compacta e tamanho de fonte maior.
- [ ] **Step 4: Rodar teste visual automatizado e commit**

```powershell
./gradlew.bat :app:compileDebugAndroidTestKotlin
./gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.InitialSetupFlowTest
```

Se nenhum device estiver conectado, registrar a mensagem; não alegar teste instrumentado executado. Commit: `git commit -m "feat: clarify study pace and difficulty setup"`.

### Task 4: Alinhar prompt e cronograma de IA ao horizonte e à entrada completa

**Files:**
- Modify: `app/src/main/java/br/com/estudario/data/prompt/PromptBuilders.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupViewModel.kt`
- Modify: `app/src/test/java/br/com/estudario/data/prompt/PromptBuildersTest.kt`

- [ ] **Step 1: Acrescentar testes de horizonte e contexto**

Testar prova daqui a 12 semanas => `endDate == examDate`; sem prova => quatro semanas a partir do início; prova antes do fim de quatro semanas => prova é o fim. Confirmar no texto do prompt nomes/IDs de todos os tópicos, prioridades efetivas, bloco, disponibilidade diária e perfil. Garantir que prompts iguais produzem texto igual.
- [ ] **Step 2: Corrigir regra de horizonte e cache do prompt**

Atualizar `PlanPromptBuilder.endDate` com esta regra, e incluir teste para cada ramo. Em `PlanPromptOptions`, adicionar `blockMinutes: Int = 50` e `studyProfile: StudyProfile = StudyProfile.DO_ZERO`:

```kotlin
fun endDate(o: PlanPromptOptions): LocalDate =
    o.examDate ?: o.startDate.plusWeeks(4).minusDays(1)
```

Enviar os dados comuns montados na Task 2, tamanho do bloco e `studyProfile`; no construtor, imprimir o perfil escolhido como contexto de planejamento. Incluir no `remember`/chave de memoização disponibilidade, bloco, perfil, prioridades, IDs/títulos de tópico e data de prova, para evitar prompt desatualizado após alteração. Rejeitar data de prova anterior à data de início antes de abrir/importar a proposta.
- [ ] **Step 3: Testar e integrar**

```powershell
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.data.prompt.PromptBuildersTest"
```

O prompt deve explicar horizonte longo e não solicitar conteúdo fora dos IDs recebidos. Commit: `git commit -m "feat: build complete AI plan prompts from syllabus"`.

### Task 5: Preservar bloco e perfil em `.plano` mantendo compatibilidade

**Files:**
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanDtos.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanCodec.kt`
- Modify: `app/src/main/java/br/com/estudario/data/transfer/planner/StudyPlanTransferService.kt`
- Modify: `app/src/test/java/br/com/estudario/data/transfer/planner/StudyPlanCodecTest.kt`
- Modify: `app/src/androidTest/java/br/com/estudario/data/transfer/planner/StudyPlanTransferServiceTest.kt`

- [ ] **Step 1: Escrever teste de fixture antiga e round-trip novo**

Ler fixture válida `.plano` sem `blocoMinutos`/`perfil`; esperar 50 e `DO_ZERO`. Codificar/decodificar configuração com valores selecionados e preservar campos em importação para `StudyPlanEntity`.
- [ ] **Step 2: Acrescentar propriedades opcionais**

Adicionar `blockMinutes: Int = 50` e `profile: StudyProfile = DO_ZERO` ao `PlanConfigurationDto`; ler `configuracao.blocoMinutos` com default 50 e `configuracao.perfil` ausente como `DO_ZERO`, rejeitando enum presente desconhecido pelo `StudyPlanValidationException` já usado no codec. Emitir `.put("blocoMinutos", ...)` e `.put("perfil", ...)` em `encode`; validar `blockMinutes` no intervalo 15..180 sem alterar a versão 1.
- [ ] **Step 3: Persistir no plano importado e verificar**

Ao importar, repassar perfil/bloco para entidade/`StudyMethodConfig`; não alterar entidades nem versão do arquivo. Executar testes unitários de codec e instrumentação disponível do serviço:

```powershell
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.data.transfer.planner.StudyPlanCodecTest"
./gradlew.bat :app:compileDebugAndroidTestKotlin
```

Commit: `git commit -m "fix: preserve study method in plan transfers"`.

### Task 6: Recusar cobertura incompleta na finalização do onboarding

**Files:**
- Create: `app/src/main/java/br/com/estudario/domain/setup/PlanCoverageValidator.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupViewModel.kt`
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Create: `app/src/test/java/br/com/estudario/domain/setup/PlanCoverageValidatorTest.kt`

- [ ] **Step 1: Especificar validador puro por IDs**

Criar `validate(sourceSubjects, importedPlan, dayMinutes): PlanCoverageResult` comparando cada ID de tópico da fonte com `topicExternalId` nas tarefas. Para matéria sem tópicos, exigir ao menos uma tarefa referindo seu `subjectExternalId` válido. O validador compara a soma de minutos de tarefas por data com disponibilidade daquele dia da semana (índice `DayOfWeek.value - 1`) e aponta datas acima da capacidade, além de tópicos/matérias ausentes. Referências quebradas e estrutura JSON já são responsabilidade de `StudyPlanCodec`; não duplicar validação genérica. Não contar apenas fase/metas como cobertura de tópico.

```kotlin
data class PlanCoverageResult(
    val missingTopics: List<MissingTopic>,
    val missingSubjects: List<String>,
    val overCapacityDates: List<LocalDate>,
) { val isComplete: Boolean get() = missingTopics.isEmpty() && missingSubjects.isEmpty() && overCapacityDates.isEmpty() }
data class MissingTopic(val subjectId: String, val topicId: String, val subjectName: String, val topicName: String)
```
- [ ] **Step 2: Cobrir aceitação e omissões por teste**

Testar cobertura completa, um tópico omitido, vários tópicos omitidos, matéria sem tópico coberta/omitida, ID desconhecido e calendário/hora acima da capacidade. O erro deve conter listas determinísticas de matéria/tópico ausentes.
- [ ] **Step 3: Colocar validação antes de importar**

No fluxo de setup, validar o `.plano` contra o edital atualmente selecionado antes de chamar importação, ativação ou avançar etapa. Em falha, manter `lastValidPlanId`, texto do arquivo e seleções; mostrar ausências e ações “Importar outro arquivo” e “Usar plano automático”. Não mudar a validação global de arquivos importados fora do onboarding.
- [ ] **Step 4: Verificar regressão do import e commit**

Rodar `:app:testDebugUnitTest` para `PlanCoverageValidatorTest` e testes de codec, mais `:app:compileDebugKotlin`. Commit: `git commit -m "fix: require complete syllabus coverage in setup"`.

### Task 7: Revisão final, regressões e verificação integral

**Files:**
- Modify: `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- Modify: testes setup e prompt listados acima

- [ ] **Step 1: Completar resumo real**

Em `PlanReviewStep`, mostrar disponibilidade por dia e total semanal, tamanho do bloco com explicação, perfil, escolhas por matéria e prioridades efetivas relevantes, horizonte, matérias/tópicos cobertos e primeira tarefa real. Preservar acesso para voltar a cada etapa. Não mostrar “plano completo” até a validação passar.
- [ ] **Step 2: Rodar suíte focada e build**

```powershell
./gradlew.bat :app:testDebugUnitTest --tests "br.com.estudario.domain.setup.*" --tests "br.com.estudario.data.prompt.PromptBuildersTest" --tests "br.com.estudario.data.transfer.planner.StudyPlanCodecTest"
./gradlew.bat :app:assembleDebug
./gradlew.bat :app:compileDebugAndroidTestKotlin
```

Expectativa: comandos finalizam com código 0. Se o padrão Gradle não aceitar pacote curinga, executar os testes nomeados individualmente. Rodar instrumentação de onboarding quando houver emulador; reportar ausência de device separadamente.
- [ ] **Step 3: Conferir diff e commits**

Verificar `git diff --check`, review do diff seletivo, estado do codec antigo e que alterações alheias já presentes não foram staged/revertidas. Reportar build, testes unitários, teste em device (se executado) e limitações reais.
