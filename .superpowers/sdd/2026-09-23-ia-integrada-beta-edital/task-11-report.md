# Task 11 — aplicação atômica do syllabus revisado e outbox

## Arquivos

- `app/src/main/java/br/com/estudario/data/syllabus/SyllabusApplicationService.kt`
- `app/src/androidTest/java/br/com/estudario/data/syllabus/SyllabusApplicationServiceTest.kt`
- `app/src/main/java/br/com/estudario/data/local/AppDao.kt`
- `app/src/main/java/br/com/estudario/data/transfer/EstudoPackageService.kt`

## Implementação

- `SyllabusApplicationService.applyReviewedSyllabus` valida alvo, draft e `sourceJobId`, faz binding do título real do edital selecionado e serializa pelo mapper/parser oficial `.estudo`.
- Toda aplicação local, substituição explícita, associação remota conhecida, hash SHA-256 e inserção da mutação `UPSERT` na outbox ocorrem no mesmo `database.withTransaction`.
- O alvo local mantém seu `id`, nome, identidade remota conhecida e demais campos de competição; não há criação de segundo edital.
- Conteúdo existente bloqueia a operação por padrão; `replaceExisting = true` é necessário para limpar e substituir a árvore.
- Deduplicação usa `sourceJobId` e a chave existente de `localSyllabusId + operation + payloadHash`; reaplicações não criam nova árvore nem nova linha.
- A mutação sempre nasce `PENDING`, preservando `attemptCount`, `attemptToken` e os estados da fila da Task 2. Nenhum sucesso remoto é presumido.
- `EstudoPackageService.importInTransaction` reutiliza exatamente o boundary oficial sem abrir uma segunda transação quando chamado pelo serviço.

## Testes e resultados

- RED TDD: `:app:compileDebugAndroidTestKotlin` falhou inicialmente porque o serviço e `ExistingSyllabusContentException` ainda não existiam; depois da implementação a compilação passou.
- `:app:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.syllabus.SyllabusApplicationServiceTest"` — `BUILD SUCCESSFUL`; 5/5 testes instrumentados.
- Casos instrumentados: aplicação bem-sucedida com identidade/remote association, PENDING sem ACK remoto, idempotência por job, bloqueio/substituição explícita e rollback após erro durante replacement.
- `:app:testDebugUnitTest --tests br.com.estudario.domain.ai.AiSyllabusProposalValidatorTest --tests br.com.estudario.domain.ai.AiSyllabusToEstudoMapperTest --tests br.com.estudario.data.transfer.EstudoPackageParserTest` — `BUILD SUCCESSFUL`.
- `:app:compileDebugAndroidTestKotlin` — `BUILD SUCCESSFUL`.
- Suíte JVM completa: `340 tests completed, 1 failed`; a única falha é `CopyStyleTest`, por travessões tipográficos preexistentes em `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`, arquivo fora desta Task.
- Suíte instrumentada completa: os 5 testes da Task 11 passaram; as 10 falhas restantes são preexistentes em backup/UI e não envolvem os arquivos alterados.
- `git diff --check` — passou.
- Nenhuma credencial, rede real, backend, UI ou arquivo da Task 12 foi alterado.

## Commit

- `feat: apply AI syllabus atomically with sync outbox`

## Riscos

- O ACK remoto e a transição para `SYNCED` continuam deliberadamente fora desta Task e serão tratados pela Task 12.
- A substituição explícita remove a árvore local existente e referências dependentes dentro da transação; falhas no import oficial restauram tudo por rollback Room.
- O ambiente mantém falhas baseline já existentes na suíte completa; os testes focados da Task 11 e os testes JVM das dependências relevantes estão verdes.
