# Task 11 — aplicação atômica do syllabus revisado e outbox

## Arquivos

- `app/src/main/java/br/com/estudario/data/syllabus/SyllabusApplicationService.kt`
- `app/src/androidTest/java/br/com/estudario/data/syllabus/SyllabusApplicationServiceTest.kt`
- `app/src/main/java/br/com/estudario/data/local/AppDao.kt`
- `app/src/main/java/br/com/estudario/data/local/RemoteSyllabusSyncError.kt`
- `app/src/main/java/br/com/estudario/data/local/Entities.kt`
- `app/src/main/java/br/com/estudario/data/local/AppDatabase.kt`
- `app/src/main/java/br/com/estudario/data/StudyRepository.kt`
- `app/src/androidTest/java/br/com/estudario/data/local/RemoteSyllabusSyncDaoTest.kt`
- `app/src/androidTest/java/br/com/estudario/data/local/RemoteSyllabusSyncMigrationTest.kt`
- `app/schemas/br.com.estudario.data.local.AppDatabase/18.json`
- `app/src/main/java/br/com/estudario/data/transfer/EstudoPackageService.kt`

## Implementação

- `SyllabusApplicationService.applyReviewedSyllabus` valida alvo, draft e `sourceJobId`, faz binding do título real do edital selecionado e serializa pelo mapper/parser oficial `.estudo`.
- Toda aplicação local, substituição explícita, associação remota conhecida, hash SHA-256 e inserção da mutação `UPSERT` na outbox ocorrem no mesmo `database.withTransaction`.
- O alvo local mantém seu `id`, nome, identidade remota conhecida e demais campos de competição; não há criação de segundo edital.
- Conteúdo existente bloqueia a operação por padrão; `replaceExisting = true` é necessário para limpar e substituir a árvore.
- A linha existente da outbox agora armazena `payloadJson`, o snapshot canônico serializado pelo formato oficial `.estudo`, além do hash. O snapshot é gravado junto do `PENDING`, sobrevive a restart e pode ser reconstruído sem rede ou credenciais.
- Deduplicação por `sourceJobId` compara o hash antes de qualquer mutação: mesmo hash retorna o snapshot persistido; hash diferente lança `SyllabusSourceJobConflictException` sem alterar árvore, target, associação ou outbox. A chave existente de `localSyllabusId + operation + payloadHash` continua evitando duplicatas de payload.
- A migration incremental `17→18` adiciona `payloadJson` com default vazio para linhas legadas; snapshots legados são preenchidos de forma idempotente quando a mesma aplicação é reapresentada.
- A mutação sempre nasce `PENDING`, preservando `attemptCount`, `attemptToken` e os estados da fila da Task 2. Nenhum sucesso remoto é presumido.
- Replacement explícito marca atomicamente as mutações anteriores `PENDING`/`FAILED` do mesmo target e `remoteSyllabusId` como `FAILED/SUPERSEDED`, troca o `attemptToken` e mantém o snapshot para auditoria. Consultas de pending/failed e requeue ignoram superseded; CAS antigo não consegue concluir nem reencaminhar a linha.
- `EstudoPackageService.importInTransaction` reutiliza exatamente o boundary oficial sem abrir uma segunda transação quando chamado pelo serviço.
- O teste de rollback injeta uma falha depois do clear/import real, dentro do `database.withTransaction`, e compara target/associação, subjects/topics e outbox com o snapshot anterior.

## Testes e resultados

- RED TDD: `:app:compileDebugAndroidTestKotlin` falhou inicialmente nas novas expectativas de `payloadJson`, migration e conflito; depois da implementação a compilação passou.
- `:app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.syllabus.SyllabusApplicationServiceTest` — `BUILD SUCCESSFUL`; 8/8 testes instrumentados.
- `:app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.local.RemoteSyllabusSyncDaoTest` — `BUILD SUCCESSFUL`; 10/10 testes instrumentados.
- `:app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.local.RemoteSyllabusSyncMigrationTest` — `BUILD SUCCESSFUL`; migration 14→18, 1/1 teste.
- `:app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.RemoteSyllabusAssociationTest` — `BUILD SUCCESSFUL`; 1/1 teste do repository.
- Casos instrumentados: aplicação com identidade/associação remota, snapshot canônico persistido após restart, PENDING sem ACK remoto, idempotência por job, conflito sem mutação, bloqueio/substituição explícita, rollback após mutação local real, round-trip DAO e migration incremental.
- Caso adicional desta rodada: replacement com outbox antiga `PENDING` e `FAILED`, preservação de ambos os `payloadJson`, exclusão da antiga do retry e rejeição de completion/requeue com `attemptToken` antigo.
- `:app:testDebugUnitTest --tests br.com.estudario.domain.ai.AiSyllabusProposalValidatorTest --tests br.com.estudario.domain.ai.AiSyllabusToEstudoMapperTest --tests br.com.estudario.data.transfer.EstudoPackageParserTest` — `BUILD SUCCESSFUL`.
- `:app:compileDebugAndroidTestKotlin` — `BUILD SUCCESSFUL`.
- `git diff --check` — passou.
- Nenhuma credencial, rede real, backend, UI ou arquivo da Task 12 foi alterado.

## Commit

- `fix: harden Task 11 atomic apply outbox` (commit separado do baseline `595d4da`)
- `fix: supersede stale Task 11 outbox mutations` (commit separado desta rodada)

## Riscos

- O ACK remoto e a transição para `SYNCED` continuam deliberadamente fora desta Task e serão tratados pela Task 12.
- A substituição explícita remove a árvore local existente e referências dependentes dentro da transação; falhas no import oficial restauram tudo por rollback Room.
- Linhas de outbox anteriores à migration 17→18 recebem `payloadJson = ''`; na reaplicação idempotente com o mesmo hash o serviço faz backfill do snapshot candidato oficial antes de devolver o resultado canônico. Conflitos por hash diferente não fazem esse backfill.
- O modelo continua usando a fila existente e a migration `17→18`; `SUPERSEDED` é um código de erro seguro no estado `FAILED`, sem apagar evidência histórica.
- O ambiente mantém falhas baseline já existentes na suíte completa; os testes focados desta correção estão verdes.
