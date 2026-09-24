# Task 13 — AI syllabus review flow

## Escopo desta revisão

- Falhas não terminais após a criação do request agora preservam `requestId`, `idempotencyKey` e, quando já disponível, `jobId`; retry/restart chama `recover` da mesma identidade. Há cobertura também para o caso em que o request foi persistido antes do `jobId`.
- A aplicação local encaminha o callback de ACK da revisão para a configuração inicial; a transição persistida é `SYLLABUS_REVIEW`/`DIRECT_AI` e o overlay fecha somente pelo callback integrado.
- Recovery instrumentado usa `DataStoreAiJobRequestStore` e `DataStoreAiReviewSessionStore` reais, `HttpAiApiClient` com transporte fake, recriação do ViewModel, falha genérica pós-upload, polling e uma única chave/job.
- O picker integrado do Initial Setup aceita somente `application/pdf` e chama `takePersistableUriPermission` antes de guardar a origem.
- O seam anti-Drive injeta um fake que falha se lido; os testes capturam os headers de produção e verificam somente `Bearer <JWT Supabase>`, `apikey` e `Accept`.
- Nenhum backend, migration, endpoint, política server-side, URL/key real, OAuth/OTP real ou segredo foi adicionado.

## Arquivos alterados nesta revisão

- `app/src/main/java/br/com/estudario/ui/ai/AiReviewAccess.kt`
- `app/src/main/java/br/com/estudario/ui/ai/AiReviewEntryPoint.kt`
- `app/src/main/java/br/com/estudario/ui/ai/AiReviewModels.kt`
- `app/src/main/java/br/com/estudario/ui/ai/AiReviewScreen.kt`
- `app/src/main/java/br/com/estudario/ui/ai/AiReviewViewModel.kt`
- `app/src/main/java/br/com/estudario/ui/EstudarioApp.kt`
- `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- `app/src/main/java/br/com/estudario/ui/setup/InitialSetupViewModel.kt`
- `app/src/test/java/br/com/estudario/ui/ai/AiReviewAccessTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/ai/AiReviewDurableRecoveryTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/ai/AiReviewEntryPointTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/ai/AiReviewScreenTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/ai/AiReviewViewModelTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/setup/SetupSyllabusPersistenceTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/setup/SetupSyllabusStepsTest.kt`

## Verificação

Passaram:

- `:app:testDebugUnitTest` direcionado para AiAccess, recovery, jobs/API, repositório, PDF, proposal/mapper e sync — 30 testes.
- `:app:connectedDebugAndroidTest` para `AiReviewViewModelTest` — 7/7.
- `:app:connectedDebugAndroidTest` para `AiReviewDurableRecoveryTest` — 1/1.
- `:app:connectedDebugAndroidTest` para `AiReviewScreenTest` — 7/7.
- `:app:connectedDebugAndroidTest` para o novo picker integrado — 1/1.
- `:app:connectedDebugAndroidTest` para `SetupSyllabusPersistenceTest#appliedAiSyllabusMovesSetupToReviewStepForTheSameCompetition` — passou.
- `:app:testDebugUnitTest --tests br.com.estudario.ui.ai.AiReviewRecoveryTest :app:compileDebugKotlin :app:compileDebugAndroidTestKotlin --no-daemon` — passou.
- `:app:assembleDebug --no-daemon` — passou.
- `git diff --check` — passou.

### Fase 1: investigação dos três failures baseline

Os três failures foram reproduzidos isoladamente e nas suítes atuais com:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.setup.SetupSyllabusStepsTest#subjectProfileCanContinueWithNeutralDefaultsAndScrollsToEverySubject' --no-daemon
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.setup.SetupSyllabusStepsTest#addingAnotherSubjectKeepsPreviousChoicesAndUsesNeutralDefaults' --no-daemon
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.setup.SetupSyllabusPersistenceTest#reviewEditsPersistAndAdvancementRequiresAllCurrentSubjects' --no-daemon
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.setup.SetupSyllabusStepsTest' --no-daemon
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.ui.setup.SetupSyllabusPersistenceTest' --no-daemon
```

Resultados atuais: `SetupSyllabusStepsTest` teve 6 testes/2 failures e `SetupSyllabusPersistenceTest` teve 3 testes/1 failure. Os testes adicionais da Task 13 nas mesmas classes passaram.

Os mesmos comandos foram executados em um checkout temporário limpo de `febf2ef`, sem alterar este worktree. O baseline reproduziu os mesmos resultados: 2 failures em 5 testes de `SetupSyllabusStepsTest` e 1 failure em 2 testes de `SetupSyllabusPersistenceTest`.

Classificação e origem dos estados:

- `SetupSyllabusStepsTest.kt:144`: `difficulty_3_HARD` termina com `Selected = false`. `SubjectProfileStep` deriva a seleção de `snapshot.subjectDifficulties["3"]`, com `NORMAL` como default; a interação não produz a atualização esperada. O mesmo nó/estado ocorre no checkout limpo (`febf2ef`, linha 141). **Baseline; não é regressão da Task 13.**
- `SetupSyllabusStepsTest.kt:161`: o fixture já contém `subjectDifficulties["1"] = EASY`; `tunedSubjectCount()` retorna 1 e a UI renderiza `1 matéria(s) com resposta sua.`, não a mensagem neutra. O mesmo ocorre em `febf2ef` (linha 158). **Baseline; não é regressão da Task 13.**
- `SetupSyllabusPersistenceTest.kt:48`: `InitialSetupViewModel.advance()` só grava transições permitidas; `InitialSetupTransitions` define `SYLLABUS_REVIEW -> SUBJECT_PRIORITY`, então a solicitação direta para `PROFILE` é ignorada e o estado permanece `SYLLABUS_REVIEW`. O mesmo ocorre em `febf2ef`. **Baseline; não é regressão da Task 13.**

O diff `febf2ef..worktree` foi revisado. `SetupPlannerSteps.kt`, `PlannerWizard.kt` e `domain/setup/InitialSetup.kt` não foram alterados; os corpos dos três testes existentes também não foram alterados. A Task 13 não mascara nem corrige esses failures.

### Verificação final da Task 13

Após a atualização deste relatório, foram rerodados:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.ai.AiReviewAccessTest --tests br.com.estudario.ui.ai.AiReviewRecoveryTest --tests br.com.estudario.data.ai.AiApiClientTest --tests br.com.estudario.data.ai.AiJobRecoveryWorkerTest --tests br.com.estudario.data.ai.AiModelsSerializationTest --tests br.com.estudario.data.ai.AiSyllabusRepositoryTest --tests br.com.estudario.data.ai.PdfSourceReaderTest --tests br.com.estudario.domain.ai.AiSyllabusProposalValidatorTest --tests br.com.estudario.domain.ai.AiSyllabusToEstudoMapperTest --tests br.com.estudario.data.remote.RemoteSyllabusModelsSerializationTest --tests br.com.estudario.data.remote.RemoteSyllabusMapperTest --tests br.com.estudario.data.remote.RemoteSyllabusSyncWorkerTest --no-daemon
.\gradlew.bat :app:testDebugUnitTest --tests br.com.estudario.ui.ai.AiReviewRecoveryTest :app:compileDebugKotlin :app:compileDebugAndroidTestKotlin --no-daemon
.\gradlew.bat :app:assembleDebug --no-daemon
git diff --check
```

Todos passaram; os unitários direcionados reportaram 30 testes. Os testes instrumentados próprios da Task 13 também passaram no `Pixel_7` (`ANDROID_SERIAL=emulator-5554`): `AiReviewViewModelTest`, `AiReviewDurableRecoveryTest`, `AiReviewScreenTest`, `SetupSyllabusStepsTest#integratedAiAttachmentPickerRequestsOnlyPdf` e `SetupSyllabusPersistenceTest#appliedAiSyllabusMovesSetupToReviewStepForTheSameCompetition`. O segundo dispositivo conectado foi excluído da execução porque a instalação foi recusada pelo sistema (`INSTALL_FAILED_USER_RESTRICTED`); nenhum código ou expectativa foi alterado para contornar isso.

Bloqueios baseline reproduzidos, portanto não há alegação de suíte completa totalmente verde:

- `SetupSyllabusStepsTest#subjectProfileCanContinueWithNeutralDefaultsAndScrollsToEverySubject`: falha em `SetupSyllabusStepsTest.kt:144`, `difficulty_3_HARD` permanece `Selected = false` após o clique.
- `SetupSyllabusStepsTest#addingAnotherSubjectKeepsPreviousChoicesAndUsesNeutralDefaults`: falha em `SetupSyllabusStepsTest.kt:161`, o fixture contém `subjectDifficulties["1"] = EASY`, então a UI mostra `1 matéria(s) com resposta sua.` em vez de `Você pode seguir sem mexer em nada.`
- `SetupSyllabusPersistenceTest#reviewEditsPersistAndAdvancementRequiresAllCurrentSubjects`: falha em `SetupSyllabusPersistenceTest.kt:48`, espera `PROFILE`, mas o contrato atual da máquina de estados é `SYLLABUS_REVIEW -> SUBJECT_PRIORITY`.

Também houve uma tentativa inicial de instrumentação com `DeviceException: No connected devices!`; o AVD `Pixel_7` foi reiniciado localmente e os testes instrumentados acima foram executados depois disso.

## Correções desta rodada

- `AiReviewEntryPoint` agora mantém a fronteira do seletor injetável para testes, restringe o seletor de produção a `application/pdf`, chama `InitialSetupAiPdfSource.persist` no callback real do `OpenDocument` antes de guardar/iniciar e mostra erro seguro sem iniciar o job quando `takePersistableUriPermission` falha.
- O callback de aplicação local (`onLocalApplied`) foi separado do callback de ACK (`onSyncAck`). `PENDING` dispara a transição integrada para `SYLLABUS_REVIEW` e fechamento do overlay; `SYNCED` apenas informa o ACK e não reaplica o edital. O callback local é protegido contra repetição durante a mudança `PENDING -> SYNCED`.
- O teste Compose agora aciona edição e remoção de tópico aninhado, verificando IDs e hierarquia dos pais; o teste de aplicação parte de `PENDING` e verifica a transição antes do ACK.
- `AiReviewEntryPointTest` aciona o callback capturado pelo picker usado no entrypoint, verifica persistência antes do start e verifica que uma falha de permissão não chama o repositório de jobs.

### Evidência da rodada atual

Passaram:

```text
:app:compileDebugAndroidTestKotlin — BUILD SUCCESSFUL
:app:testDebugUnitTest --tests br.com.estudario.ui.ai.AiReviewRecoveryTest :app:compileDebugKotlin :app:compileDebugAndroidTestKotlin --no-daemon — BUILD SUCCESSFUL
:app:connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=br.com.estudario.ui.ai.AiReviewScreenTest" --no-daemon — BUILD SUCCESSFUL nos dois dispositivos; 8/8 por dispositivo
:app:connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=br.com.estudario.ui.ai.AiReviewEntryPointTest" --no-daemon — BUILD SUCCESSFUL nos dois dispositivos; 2/2 por dispositivo
:app:connectedDebugAndroidTest --project-prop "android.testInstrumentationRunnerArguments.class=br.com.estudario.ui.ai.AiReviewViewModelTest" --no-daemon — BUILD SUCCESSFUL nos dois dispositivos
adb -s emulator-5554 shell am instrument -w -r -e class br.com.estudario.ui.ai.AiReviewDurableRecoveryTest br.com.estudario.test/androidx.test.runner.AndroidJUnitRunner — OK (1 test)
:app:testDebugUnitTest --tests br.com.estudario.ui.ai.AiReviewAccessTest --tests br.com.estudario.ui.ai.AiReviewRecoveryTest --tests br.com.estudario.data.ai.AiApiClientTest --tests br.com.estudario.data.ai.AiJobRecoveryWorkerTest --tests br.com.estudario.data.ai.AiModelsSerializationTest --tests br.com.estudario.data.ai.AiSyllabusRepositoryTest --tests br.com.estudario.data.ai.PdfSourceReaderTest --tests br.com.estudario.data.remote.RemoteSyllabusMapperTest --tests br.com.estudario.data.remote.RemoteSyllabusModelsSerializationTest --tests br.com.estudario.data.remote.RemoteSyllabusSyncWorkerTest --tests br.com.estudario.data.remote.SupabaseAuthRepositoryTest --tests br.com.estudario.data.remote.SupabaseClientConfigTest --tests br.com.estudario.data.remote.SupabaseSessionStoreTest --no-daemon — BUILD SUCCESSFUL
```

O primeiro `connectedDebugAndroidTest` da classe de recovery também tentou executar no aparelho físico `KF85CQ4LT8FIOFCI`, mas esse aparelho bloqueou a instalação com `INSTALL_FAILED_USER_RESTRICTED` e `Failed to uninstall package ... DELETE_FAILED_INTERNAL_ERROR`. A recuperação foi executada diretamente no `emulator-5554` com APKs instalados com sucesso; não considero o aparelho físico verde.

Os três testes baseline foram executados diretamente no `emulator-5554` sem alteração de código ou expectativa:

- `SetupSyllabusStepsTest`: `Tests run: 6, Failures: 2`; falhas em `:144` (`difficulty_3_HARD` não selecionado) e `:161` (fixture mostra `1 matéria(s) com resposta sua.`).
- `SetupSyllabusPersistenceTest`: `Tests run: 3, Failures: 1`; falha em `:48` (`expected:<PROFILE> but was:<SYLLABUS_REVIEW>`).

Esses resultados continuam classificados como baseline de `febf2ef`, não como regressão desta rodada. Os corpos e expectativas dos três testes permanecem intactos.

## Estado do commit

O commit separado desta entrega foi criado após a verificação final da suíte específica da Task 13. Os três failures baseline acima permanecem sem alteração. Os artefatos não relacionados já não rastreados (`node_modules/`, `package-lock.json`, `package.json`, `supabase/.branches/`, `supabase/.temp/`) foram preservados.

As correções desta rodada permanecem restritas ao commit separado atual da Task 13; nenhum trabalho da Task 14 foi iniciado.

## Riscos e pendências

- Os três bloqueios acima são testes de setup existentes fora do fluxo IA alterado; corrigi exclusivamente os seis findings da Task 13 e não alterei a máquina de estados nem a implementação do planner para mascará-los.
- A configuração Supabase/Auth continua fechada; todos os testes usam fakes/fixtures sintéticos.
- Não avancei para Task 14.
