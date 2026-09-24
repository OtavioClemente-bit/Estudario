# Task 12 — sincronização e restauração lossless de syllabi privados

## Status

Implementada no worktree `C:\Users\otavi\.codex\worktrees\ia-api\vc-x20`. A configuração remota permanece fechada: sem URL/key reais, deploy, OAuth/OTP, Google Drive ou OpenAI.

## Implementação

- `supabase/functions/user-syllabi/index.ts` implementa list/read/upsert/delete autenticados e owner-scoped, com validação pelo contrato compartilhado, árvore completa, IDs externos, package/schema, posições, pais, prioridades e metadata.
- Upsert exige `Idempotency-Key`/`X-Mutation-Id`, valida `X-Payload-Hash`, rejeita conflito de payload e impede overwrite cross-account. A migration `202609240011_private_syllabus_mutations.sql` adiciona somente o ledger de idempotência da mutation; `202609240012_private_syllabus_atomic_rpc.sql` faz root+subjects+topics+ledger em uma transação com claim/lock atômico; a outbox existente continua sendo a única fila.
- `PrivateSyllabusRepository.kt` reutiliza o snapshot `.estudo` e o payload hash da outbox; baixa remote-only, restaura por codec oficial e mantém remote quando o usuário remove apenas o dado local.
- `RemoteSyllabusMapper.kt` preserva identidade remota, external IDs, ordem, prioridades, parents, metadata, package/schema e hash. O payload oficial canônico é retido para o round-trip exato, sem equivalência por nomes nem geração de IDs novos.
- `RemoteSyllabusSyncWorker.kt` usa WorkManager com rede conectada, backoff exponencial e `attemptToken`; somente um ACK `SYNCED` com identidade não nula, exata e hash correspondente conclui a linha. O CAS Room associa `CompetitionEntity.remoteSyllabusId`, grava a identidade na outbox e marca `SYNCED` na mesma transação. Falhas ficam retryable e preservam o snapshot/local.
- Cobertos local/offline, remote-only download, remote deletion explícita, reinstall/download e local deletion com retenção remota.

## Rodada de correção 1

- A Edge Function deixou de persistir upsert por REST parcial e chama exclusivamente o RPC transacional público. O RPC usa `SECURITY DEFINER`, owner/cross-account checks, advisory lock por owner/root, claim por `(owner_user_id, mutation_id)`, replay do ACK committed e conflito determinístico para hash diferente.
- O ACK Android agora exige `remoteSyllabusId != null` e igualdade com a identidade calculada/enviada. A transação DAO `markRemoteSyncSyncedAndAssociate` protege `attemptToken`, associa competição, atualiza a outbox e só então deixa o estado `SYNCED`.
- `ImportResult.competitionId` passou a ser a fonte real do target restaurado; o repository não usa `competition.id == competition.externalId`. O mapper registra `stableIdentity` explícita no snapshot remoto.
- O teste instrumentado foi substituído por um fluxo completo oficial: import real no Room com `id=41` e `externalId=competition-official`, payload/hash na outbox, duas sincronizações concorrentes pelo fake transacional, replay/conflito de hash, CAS attemptToken, fetch, deleção local, download e comparação de árvore/identidade/hash.

## TDD e verificação

- RED inicial: Deno não estava disponível como comando direto; o Gradle concorrente falhou por colisão de cache incremental (`Storage ... is already registered`). Após usar `npx deno`, JDK/SDK configurados, parar daemons e executar serialmente, os testes direcionados passaram.
- `npx --yes deno test --no-config supabase/functions/user-syllabi/index_test.ts` — 3/3 passaram.
- `npx --yes deno check --no-config supabase/functions/user-syllabi/index.ts supabase/functions/user-syllabi/index_test.ts` — passou.
- `:app:testDebugUnitTest --tests br.com.estudario.data.remote.RemoteSyllabusMapperTest --tests br.com.estudario.data.remote.RemoteSyllabusSyncWorkerTest --no-daemon` — BUILD SUCCESSFUL; 5 testes direcionados.
- `:app:compileDebugAndroidTestKotlin --no-daemon` — BUILD SUCCESSFUL.
- `npx --yes supabase db reset --workdir .` — BUILD SUCCESSFUL; migration RPC aplicada.
- `npx --yes supabase test db` — PASS; 5 arquivos, 189 testes.
- `npx --yes deno test --no-config supabase/functions/user-syllabi/index_test.ts supabase/functions/user-syllabi/atomic_store_test.ts` — 4/4 passaram, incluindo RPC-only, replay concorrente e conflito de hash.
- `npx --yes deno check --no-config supabase/functions/user-syllabi/index.ts supabase/functions/user-syllabi/index_test.ts supabase/functions/user-syllabi/atomic_store_test.ts` — passou.
- `:app:testDebugUnitTest --tests br.com.estudario.data.remote.RemoteSyllabusMapperTest --tests br.com.estudario.data.remote.RemoteSyllabusSyncWorkerTest --no-daemon` — BUILD SUCCESSFUL.
- `:app:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.remote.PrivateSyllabusRepositoryTest" --no-daemon` — BUILD SUCCESSFUL; 2/2 testes instrumentados.
- `git diff --check` — passou antes da revisão final.

## Bloqueios e limites observados

- Uma tentativa inicial de `supabase db reset --workdir .` retornou `LegacyDbSetupError: error running container: exit 1`; a repetição posterior iniciou os containers, aplicou a migration e permitiu executar os testes DB com PASS.
- A checagem de todas as funções Deno encontrou 3 erros baseline em `supabase/functions/ai-syllabus-jobs/index_test.ts` (`captured.requestPayload` inferido como `never`). A função da Task 12 foi checada isoladamente e está verde; o arquivo baseline não foi alterado.
- A checagem completa de funções Deno ainda mostra 3 erros baseline em `ai-syllabus-jobs/index_test.ts` (`captured.requestPayload` inferido como `never`); não pertencem à Task 12 e não foram alterados. Nenhum deploy, endpoint real ou credencial foi usado.

## Commit

- `feat: sync and restore private syllabi losslessly`
- Rodada 1: commit separado com a correção RPC/ACK/identidade/testes.
