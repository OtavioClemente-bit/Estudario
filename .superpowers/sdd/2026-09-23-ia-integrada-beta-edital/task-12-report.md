# Task 12 — sincronização e restauração lossless de syllabi privados

## Status

Implementada no worktree `C:\Users\otavi\.codex\worktrees\ia-api\vc-x20`. A configuração remota permanece fechada: sem URL/key reais, deploy, OAuth/OTP, Google Drive ou OpenAI.

## Implementação

- `supabase/functions/user-syllabi/index.ts` implementa list/read/upsert/delete autenticados e owner-scoped, com validação pelo contrato compartilhado, árvore completa, IDs externos, package/schema, posições, pais, prioridades e metadata.
- Upsert exige `Idempotency-Key`/`X-Mutation-Id`, valida `X-Payload-Hash`, rejeita conflito de payload e impede overwrite cross-account. A migration `202609240011_private_syllabus_mutations.sql` adiciona somente o ledger de idempotência da mutation; a outbox existente continua sendo a única fila.
- `PrivateSyllabusRepository.kt` reutiliza o snapshot `.estudo` e o payload hash da outbox; baixa remote-only, restaura por codec oficial e mantém remote quando o usuário remove apenas o dado local.
- `RemoteSyllabusMapper.kt` preserva identidade remota, external IDs, ordem, prioridades, parents, metadata, package/schema e hash. O payload oficial canônico é retido para o round-trip exato, sem equivalência por nomes nem geração de IDs novos.
- `RemoteSyllabusSyncWorker.kt` usa WorkManager com rede conectada, backoff exponencial e `attemptToken`; somente um ACK `SYNCED` com identidade/hash correspondentes conclui a linha. Falhas ficam retryable e preservam o snapshot/local.
- Cobertos local/offline, remote-only download, remote deletion explícita, reinstall/download e local deletion com retenção remota.

## TDD e verificação

- RED inicial: Deno não estava disponível como comando direto; o Gradle concorrente falhou por colisão de cache incremental (`Storage ... is already registered`). Após usar `npx deno`, JDK/SDK configurados, parar daemons e executar serialmente, os testes direcionados passaram.
- `npx --yes deno test --no-config supabase/functions/user-syllabi/index_test.ts` — 3/3 passaram.
- `npx --yes deno check --no-config supabase/functions/user-syllabi/index.ts supabase/functions/user-syllabi/index_test.ts` — passou.
- `:app:testDebugUnitTest --tests br.com.estudario.data.remote.RemoteSyllabusMapperTest --tests br.com.estudario.data.remote.RemoteSyllabusSyncWorkerTest --no-daemon` — BUILD SUCCESSFUL; 5 testes direcionados.
- `:app:compileDebugAndroidTestKotlin --no-daemon` — BUILD SUCCESSFUL.
- `git diff --check` — passou antes da revisão final.

## Bloqueios e limites observados

- `supabase db reset --workdir .` não pôde iniciar: `LegacyDbSetupError: error running container: exit 1`; portanto o teste SQL local não foi executado neste ambiente sem container.
- A checagem de todas as funções Deno encontrou 3 erros baseline em `supabase/functions/ai-syllabus-jobs/index_test.ts` (`captured.requestPayload` inferido como `never`). A função da Task 12 foi checada isoladamente e está verde; o arquivo baseline não foi alterado.
- Testes instrumentados foram compilados, mas não executados nesta rodada por indisponibilidade confirmada do banco/container local; nenhum deploy ou credencial foi usado.

## Commit

- `feat: sync and restore private syllabi losslessly`
