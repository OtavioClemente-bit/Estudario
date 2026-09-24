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

## Correção final da revisão

- CAS local perdido após ACK remoto agora é falha retryable: o runner tenta persistir `CAS_CONFLICT` com backoff e retorna `shouldRetry`, sem contar sincronização falsa. O teste unitário cobre `markSynced = false`.
- O RPC rejeita explicitamente árvores com tópico além da profundidade 64 antes de alterar root/subjects/topics/ledger; o erro é `INVALID_SYLLABUS` e a transação não deixa root parcial. O pgTAP monta uma árvore válida de profundidade 65 para impedir ACK de truncamento.
- O round-trip instrumentado compara o `PrivateSyllabus` fetched normalizado campo a campo: identidade remota, nomes, ordem, prioridades, todos os IDs externos/remotos, parents, metadata, package/schema versions e hash no metadata, além da restauração oficial. Não depende apenas de `canonicalPayload`.

## Verificação da correção final

- `npx --yes supabase db reset --workdir .` — PASS; migration aplicada localmente.
- `npx --yes supabase test db` — PASS; 5 arquivos, 191 testes, incluindo profundidade excedida e rollback sem root.
- `npx --yes deno test --no-config supabase/functions/user-syllabi/index_test.ts supabase/functions/user-syllabi/atomic_store_test.ts` — PASS; 4/4.
- `npx --yes deno check --no-config supabase/functions/user-syllabi/index.ts supabase/functions/user-syllabi/index_test.ts supabase/functions/user-syllabi/atomic_store_test.ts` — PASS.
- `npx --yes deno check --no-config supabase/functions/*/index.ts supabase/functions/*/*_test.ts` — baseline ainda falha em 3 linhas de `ai-syllabus-jobs/index_test.ts` (`captured.requestPayload` inferido como `never`); fora da Task 12 e não alterado.
- `:app:testDebugUnitTest --tests br.com.estudario.data.remote.RemoteSyllabusSyncWorkerTest --tests br.com.estudario.data.remote.RemoteSyllabusMapperTest --no-daemon` — PASS.
- `:app:compileDebugKotlin :app:compileDebugAndroidTestKotlin --no-daemon` — PASS.
- `:app:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.remote.PrivateSyllabusRepositoryTest" --no-daemon` — PASS; 2/2 instrumentados.
- `:app:testDebugUnitTest --no-daemon` — 346/347 PASS; 1 baseline failure em `CopyStyleTest.appSourceDoesNotContainTypographicDashes` por caracteres preexistentes em `app/src/main/java/br/com/estudario/ui/setup/InitialSetupScreen.kt`, fora da Task 12.
- `git diff --check` — PASS.

Configuração remota permanece fechada: sem deploy, URL/key, OAuth/OTP, credenciais reais, Google Drive ou OpenAI. Nenhuma alteração foi feita fora da Task 12 e a Task 13 não foi iniciada.

## Correção final da revisão de fc3436d

- `202609240012_private_syllabus_atomic_rpc.sql` voltou a ser histórico imutável. A migration incremental `202609240013_private_syllabus_atomic_rpc_depth_guard.sql` renomeia a implementação 012 para delegate privado e instala o novo `CREATE OR REPLACE FUNCTION` público com rejeição prévia de profundidade `>64`. O teste DB verifica o delegate legado, o guard e o rollback sem root parcial, cobrindo o upgrade incremental sem deploy/credenciais.
- O round-trip deixou de derivar o esperado do `RemoteSyllabusMapper`: o teste lê diretamente o pacote JSON oficial, calcula a identidade/IDs determinísticos no próprio oracle e compara fetched root, matérias e árvore completa. A prioridade de cada tópico/subtópico é preservada em `metadata.officialPriority`, restaurada pelo mapper e comparada junto com IDs remotos, external IDs, parents, nomes, ordem, metadata, prioridades de matéria, package/schema versions e hash; `canonicalPayload` é removido apenas da comparação de metadata.

## Verificação desta correção

- `npx --yes supabase db reset --workdir .` — PASS; aplicou 012 histórico e 013 incremental.
- `npx --yes supabase test db` — PASS; 5 arquivos, 193 testes.
- `npx --yes deno test --no-config supabase/functions/user-syllabi/index_test.ts supabase/functions/user-syllabi/atomic_store_test.ts` — PASS; 4/4.
- `npx --yes deno check --no-config supabase/functions/user-syllabi/index.ts supabase/functions/user-syllabi/index_test.ts supabase/functions/user-syllabi/atomic_store_test.ts` — PASS.
- `:app:testDebugUnitTest --tests br.com.estudario.data.remote.RemoteSyllabusMapperTest --tests br.com.estudario.data.remote.RemoteSyllabusSyncWorkerTest --no-daemon` — PASS.
- `:app:compileDebugKotlin :app:compileDebugAndroidTestKotlin --no-daemon` — PASS.
- `:app:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=br.com.estudario.data.remote.PrivateSyllabusRepositoryTest" --no-daemon` — PASS; 2/2 instrumentados.
- `git diff --check` — PASS antes do commit.

Risco residual: o check Deno global e a suíte unitária global continuam com os dois bloqueios baseline documentados anteriormente, fora da Task 12; os checks direcionados desta correção estão verdes.
