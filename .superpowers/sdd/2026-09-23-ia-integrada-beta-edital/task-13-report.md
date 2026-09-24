# Task 13 — AI syllabus review flow

## Escopo implementado

- Gate Android fail-closed com estados loading, não autenticado, beta/feature/cota negados e pronto, mantendo o edital-alvo visível.
- Borda Android read-only de `GET /functions/v1/ai-access`, usando somente JWT da sessão Supabase, sem token Google Drive, mutação de cota ou política beta duplicada.
- Picker de PDF ligado ao start; target e origem persistidos antes do start; single-flight com `Mutex`; retomada por request/job/idempotency sem novo job em timeout/restart.
- Editor estruturado recursivo para matérias, tópicos e subtópicos, com contagens, edição, adição, remoção, warnings/páginas e confirmação explícita de substituição.
- Aplicação delegada ao `SyllabusApplicationService`, exibindo `PENDING` até ACK observado na outbox.
- Instância singleton de `AiAccessRepository` no `EstudarioApplication`, pronta para reutilização futura.

## Arquivos alterados

- `app/src/main/java/br/com/estudario/ui/ai/AiReviewAccess.kt`
- `app/src/main/java/br/com/estudario/ui/ai/AiReviewEntryPoint.kt`
- `app/src/main/java/br/com/estudario/ui/ai/AiReviewModels.kt`
- `app/src/main/java/br/com/estudario/ui/ai/AiReviewScreen.kt`
- `app/src/main/java/br/com/estudario/ui/ai/AiReviewViewModel.kt`
- `app/src/main/java/br/com/estudario/EstudarioApplication.kt`
- `app/src/main/java/br/com/estudario/ui/AppViewModel.kt`
- `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`
- `app/src/test/java/br/com/estudario/ui/ai/AiReviewAccessTest.kt`
- `app/src/test/java/br/com/estudario/ui/ai/AiReviewRecoveryTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/ai/AiReviewScreenTest.kt`
- `app/src/androidTest/java/br/com/estudario/ui/ai/AiReviewViewModelTest.kt`

## Verificação

- `:app:testDebugUnitTest` direcionado para ai-access, recovery, jobs/API, repositório, mapper/validator e sync Tasks 9–12 — PASS.
- `:app:connectedDebugAndroidTest` para `AiReviewScreenTest` — PASS, 6/6.
- `:app:connectedDebugAndroidTest` para `AiReviewViewModelTest` — PASS, 5/5.
- `:app:testDebugUnitTest --tests br.com.estudario.ui.ai.AiReviewRecoveryTest :app:compileDebugKotlin :app:compileDebugAndroidTestKotlin --no-daemon` — PASS.
- `:app:assembleDebug --no-daemon` — PASS.
- `git diff --check` — PASS.

Os testes de ai-access cobrem acesso autenticado/liberado, beta negado, feature desabilitada, cota disponível/esgotada, configuração fechada, JWT ausente/expirado, HTTP inválido, resposta inválida, timeout/offline e ausência de uso de token Google Drive. Fixtures usam apenas valores sintéticos.

## Pendências e riscos

- O transporte Supabase/Auth concreto permanece fechado pela configuração existente; os testes usam fakes e não adicionam URL, chave, OAuth/OTP, conta beta ou segredo.
- O contrato existente de `DefaultAiSyllabusRepository` recebe a origem, enquanto o boundary Android passa o `targetId` e persiste o target para aplicação/recovery; nenhum backend/Task 9–12 foi alterado.
- O ACK remoto depende do worker/outbox existente; a UI fica em `PENDING` até observar estado não pendente.

Commit: separate commit `feat: add AI syllabus review flow`
