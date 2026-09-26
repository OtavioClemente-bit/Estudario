# IA: diagnóstico do provider, cancelamento e quarentena de execução

**Status:** proposta escrita para revisão; nenhuma alteração de produto foi iniciada.

**Objetivo:** tornar seguro e operacional o ciclo provider/worker/cancelamento antes de reativar o cron. Falhas após o início potencial da chamada OpenAI não podem provocar um novo `POST /responses` às cegas nem deixar a reserva de quota presa sem procedimento administrativo de saída.

**Job de referência:** `ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9`. O job real não será alterado durante desenvolvimento, testes, migration local ou deploy desta task. O cron permanece pausado.

## Restrições aprovadas

- Preservar `gpt-6-luna`, `AI_DEFAULT_MODEL`, `OPENAI_API_KEY`, Android, autenticação, RLS, Storage, idempotência, quotas, worker e contratos públicos, exceto os ajustes internos necessários abaixo.
- Nenhuma RPC backend/admin poderá ser executada por `anon` ou `authenticated`; operações de worker e resolução ficam restritas a `service_role` (administração direta continua possível pelo papel administrativo do banco).
- Não reativar o cron, alterar manualmente o job real, inventar `response_id` ou marcar cancelamento como confirmado sem evidência.
- Erros públicos permanecem genéricos. Nenhum segredo, JWT, chave OpenAI, PDF, base64, prompt, header `Authorization` ou conteúdo privado será registrado.
- Não se presume suporte a replay idempotente do `POST /responses`: a referência oficial consultada não documenta uma garantia para `Idempotency-Key`. Até existir contrato oficial/teste que a estabeleça, o worker não repetirá `provider.start()` após qualquer resultado de entrega incerto. [Create a model response — OpenAI API Reference](https://developers.openai.com/api/reference/cli/resources/responses/methods/create)

## 1. Modelo de estado

O status do job continua sendo `RESERVED`, `PROCESSING`, `SUCCEEDED`, `FAILED`, `EXPIRED` ou `CANCELLED`; os estados terminais e as transições públicas não mudam. O ciclo de entrega ao provider passa a ter estado interno próprio.

### Estado interno da tentativa do provider

Campo proposto: `provider_start_outcome`, com valores controlados:

| Valor | Significado | Pode iniciar novo `POST /responses` automaticamente? |
| --- | --- | --- |
| `NOT_STARTED` | Nenhuma tentativa marcada | Sim, se não houver cancelamento |
| `IN_FLIGHT` | Marcado duravelmente antes do envio; ainda sem resultado persistido | Não |
| `NOT_SENT` | O cliente provou que falhou antes de invocar/enviar o request | Não neste job; pode encerrar e liberar reserva com segurança |
| `PROVIDER_REJECTED` | Uma resposta HTTP não-2xx foi recebida | Não; a resposta não prova, por si só, que nenhuma Response foi criada |
| `TRANSPORT_AMBIGUOUS` | Timeout, abort, conexão interrompida ou transporte sem resposta conclusiva | Não |
| `RESPONSE_AMBIGUOUS` | Houve resposta potencialmente aceita, mas não foi possível obter/persistir um `response_id` utilizável | Não |
| `ACCEPTED` | Um `response_id` válido foi persistido | Nunca iniciar outra criação; somente recuperar/cancelar esse ID |

Campo canônico proposto: `provider_quarantined_at timestamptz NULL`.

**Definição única de quarentena:** `provider_quarantined_at IS NOT NULL`. O worker, a claim SQL, o cancelamento, as regras de release de quota e os testes usam esse marcador explícito, não inferem quarentena de combinações implícitas.

Invariantes:

- `IN_FLIGHT` marca o job como em quarentena atomicamente antes de chamar a OpenAI. Isso cobre queda do worker entre o marcador e o request.
- Uma resposta `ACCEPTED` com `response_id` persistido limpa a quarentena na mesma RPC que grava o ID.
- `PROVIDER_REJECTED`, `TRANSPORT_AMBIGUOUS` e `RESPONSE_AMBIGUOUS` mantêm a quarentena e a reserva.
- `NOT_SENT` limpa a quarentena somente quando o adapter provar que nenhum request foi enviado; o worker então pode encerrar como `FAILED` ou `CANCELLED`, liberando a reserva na transação de finalização.
- Resolução administrativa válida limpa a quarentena sob lock transacional, vinculando a decisão à auditoria.
- `provider_result_recoverable` só significa resultado realmente recuperável confirmado; não será definido como `true` apenas porque uma chamada ficou incerta.

```text
NOT_STARTED
    └─ marcar antes do HTTP → IN_FLIGHT + provider_quarantined_at
          ├─ response_id persistido → ACCEPTED + limpar quarentena
          ├─ falha comprovadamente antes do envio → NOT_SENT + limpar quarentena
          ├─ HTTP não-2xx → PROVIDER_REJECTED + manter quarentena
          ├─ timeout/transporte → TRANSPORT_AMBIGUOUS + manter quarentena
          └─ resposta sem ID/persistência inconclusiva → RESPONSE_AMBIGUOUS + manter quarentena

Quarentena
    ├─ administrador confirma ausência de execução recuperável → FAILED/CANCELLED + RELEASE
    ├─ administrador anexa response_id recuperado → ACCEPTED + seguir retrieve/cancel
    └─ administrador confirma provider terminal sem resultado → FAILED/CANCELLED + RELEASE
```

## 2. Classificação e observabilidade

`OpenAiProviderError` passa a carregar uma categoria interna, estágio, status HTTP opcional, `error.type`, `error.code`, request ID validado do header e mensagem segura. A API pública Android continua recebendo apenas códigos/mensagens genéricos já contratados.

- `NOT_SENT`: falha local anterior ao envio demonstrável, como ausência de chave detectada antes da rede ou erro local de serialização antes de `fetch`.
- `PROVIDER_REJECTED`: qualquer resposta HTTP não-2xx. Registrar status e campos estruturados permitidos. 4xx/5xx não serão considerados prova automática de ausência de execução/Response; permanecem em quarentena até resolução administrativa.
- `TRANSPORT_AMBIGUOUS`: timeout, abort ou exceção de transporte depois de iniciar o `fetch`; manter quarentena.
- `RESPONSE_AMBIGUOUS`: status 2xx ou potencial aceitação seguida de JSON inválido, ID ausente/inválido ou persistência inconclusiva; não repetir criação.
- `ACCEPTED`: resposta válida com ID persistido; polling/retrieve pode ser repetido porque não cria uma nova geração.

O diagnóstico interno será emitido como evento estruturado e allowlisted: `jobId`, etapa (`start`, `retrieve`, `cancel`), categoria, status HTTP, `error.type/code`, request ID validado e mensagem padronizada segura. O corpo bruto do provider e a mensagem original não serão logados, pois podem refletir dados do request. Não incluir `Authorization`, key, prompt, bytes do PDF, base64 nem input do usuário. HTTP 400, 401/403, 429 e 500 terão testes distintos; não serão achatados em um único erro de transporte.

O adapter não troca o modelo nem a seleção atual: `resolveOpenAiModel()` permanece com prioridade para opção explícita, `AI_DEFAULT_MODEL` e fallback `gpt-6-luna`.

## 3. Cancelamento e worker

1. A RPC normal de claim não seleciona jobs com `cancellation_requested_at IS NOT NULL` nem `provider_quarantined_at IS NOT NULL`. Continua `service_role`-only e lease-bound.
2. Depois da claim/assert lease, o worker revalida o estado retornado e nunca inicia source/provider normal se o cancelamento tiver sido solicitado. Isso cobre corrida entre claim e pedido de cancelamento.
3. Sem início do provider e sem response ID, o cancelamento local já existente pode terminalizar e liberar a reserva somente quando o lease/estado permitir.
4. Com `response_id`, o endpoint de cancelamento continua sendo a rota de reconciliação: retrieve; se ativo, cancel; se completed, preservar/finalizar resultado válido; se terminal sem resultado, reconciliar e então cancelar/liberar; se provider/transporte continuar inconclusivo, retornar pendente sem release.
5. Com quarentena e sem `response_id`, o endpoint registra/retorna pendência segura (HTTP 202), sem marcar cancelamento confirmado nem liberar quota. A exclusão da claim normal não remove a rota do endpoint de cancelamento ou da resolução administrativa.
6. Se o cancelamento chegar depois da claim e houver ID conhecido, o worker não inicia/reinicia provider normal; ele encaminha para a mesma lógica de retrieve/cancel/reconcile usada pelo endpoint, sem criar nova Response. Se a reconciliação não puder terminar, mantém estado pendente.

## 4. Resolução administrativa da quarentena

Não haverá UI administrativa nesta entrega. Uma RPC transacional backend/admin-only, proposta como `resolve_ai_job_provider_quarantine(...)`, será a saída operacional auditável.

Decisões aceitas:

- `CONFIRM_NOT_CREATED`: ator confirma com evidência que nenhuma execução/Response pode ser recuperada. Só aceita job em quarentena sem ID; registra reconciliação negativa e terminaliza (`CANCELLED` se há cancelamento solicitado; caso contrário `FAILED`), liberando a reserva atomicamente.
- `ATTACH_RESPONSE_ID`: associa um ID recuperado externamente a um job em quarentena, limpa a quarentena e preserva o job/reserva em `PROCESSING`. Com cancelamento pendente, seguir retrieve/cancel; sem cancelamento, o worker poderá retrieve o ID. Não cria job nem consome quota.
- `CONFIRM_TERMINAL_NO_RESULT`: ator fornece estado terminal confirmado pelo provider (`failed`, `cancelled`, `expired` ou `incomplete`), sem resultado recuperável. Registra reconciliação, terminaliza conforme cancelamento pendente e libera a reserva atomicamente.

Auditoria proposta em `ai_job_provider_resolution_audit`: job, UUID de resolução idempotente, decisão, status/ID de provider quando aplicáveis, ator administrativo, referência de evidência e timestamp. Tabela sem acesso runtime direto por `anon`/`authenticated`; escrita via RPC `SECURITY DEFINER`. O identificador de resolução é único por job: replay com mesmos argumentos devolve o resultado já registrado; mesmo ID com decisão/payload diferente dá conflito. Lock na linha do job serializa resolução com worker/cancelamento. Decisões incompatíveis posteriores não podem mudar jobs terminais.

A RPC revoga `PUBLIC`, `anon` e `authenticated`; concede execução explicitamente só a `service_role` para Data API. Não há abertura de RPCs atuais para cliente.

## 5. Quota e idempotência

- Quarentena mantém a reserva em `RESERVED`; não incrementa `successful_count` nem transforma o resultado incerto em consumo.
- Não existe liberação automática por tempo/lease. Lease expirado apenas torna o job elegível para a rota operacional, nunca comprova que a OpenAI não executou.
- `ATTACH_RESPONSE_ID` mantém a mesma reserva. Só a finalização normal de proposta válida pode consumi-la, uma única vez.
- `CONFIRM_NOT_CREATED` e `CONFIRM_TERMINAL_NO_RESULT` executam terminalização e `RELEASE` na mesma transação, com idempotência por chave da resolução e proteções contra release/consumo duplo.
- Nenhum retry automático troca job ou idempotency key. Um erro `NOT_SENT` pode encerrar/liberar o job; eventual nova tentativa do usuário continua usando o fluxo existente de novo job/idempotency key. Replay do mesmo POST só será considerado em mudança futura após contrato oficial e testes de semântica idêntica.

## 6. Migrations e RPCs previstas

Uma migration aditiva é necessária; não há configuração/deploy automático nesta etapa. Usar `npx supabase migration new` ao iniciar implementação, conforme convenção da CLI.

- Colunas/checks para `provider_start_outcome` e `provider_quarantined_at`, sem expor diagnóstico privado no DTO Android.
- Atualizar `mark_ai_job_provider_execution_started` para gravar `IN_FLIGHT` + quarentena antes da rede; atualizar `persist_ai_job_provider_response` para gravar `ACCEPTED` e limpar quarentena atomicamente.
- Nova RPC lease-bound para registrar `NOT_SENT`/`PROVIDER_REJECTED`/`TRANSPORT_AMBIGUOUS`/`RESPONSE_AMBIGUOUS`, sem liberar quota por categoria incerta.
- Atualizar `claim_ai_syllabus_worker_job` para ignorar cancelamento/quarentena, mantendo assinatura e ACL `service_role`-only.
- Atualizar release/finalização para impedir release comum de job em quarentena; RPC administrativa limpa/reconcilia e chama release dentro da mesma transação.
- Criar tabela de auditoria privada e a RPC administrativa idempotente de resolução; revogar runtime público/autenticado e conceder somente ao papel backend previsto.
- Preservar overloads existentes e demais grants. Não alterar scheduler, cron, modelo, bucket, auth, RLS de usuário ou contratos públicos.

## 7. Plano de testes e critérios de aceitação

### Provider (Deno)

- fallback permanece `gpt-6-luna`; `AI_DEFAULT_MODEL` ainda prevalece.
- 400, 401, 403, 429 e 500: diagnóstico inclui status/campos seguros e mantém mensagem pública genérica; nenhuma resposta não-2xx vira `NOT_SENT` automaticamente.
- HTTP 2xx com JSON inválido/ID ausente classifica resposta ambígua.
- timeout, abort e erro de transporte classificam separadamente e não vazam chave, Authorization, prompt, bytes/base64 ou conteúdo-sentinela do PDF.
- erro pre-network demonstrável classifica `NOT_SENT`.

### Worker/cancelamento (Deno)

- cancelamento observado antes do provider gera zero chamadas a `start`.
- response ID conhecido segue retrieve/cancel/reconcile; provider concluído preserva resultado.
- `markProviderStarted` seguido de timeout, erro de transporte, HTTP rejeitado ou resposta inválida sem ID não gera segundo `start`, retry_count de geração nem novo job/idempotency key.
- estado `IN_FLIGHT` persistido antes de queda simulada é reconhecido como quarentena na próxima leitura.
- job normal sem cancelamento/quarentena continua processando e leases permanecem obrigatórios.
- erro ao persistir ID depois de resposta aceita não chama `start` novamente; tenta somente persistir/reconciliar o mesmo ID e, se inconclusivo, fica em quarentena.

### Banco/pgTAP

- claim exclui cancelamentos e quarentenas; claims e RPCs de worker continuam inacessíveis a `anon`/`authenticated`.
- iniciar marca `IN_FLIGHT`/quarentena; persistir ID limpa ambos atomicamente.
- release comum falha para quarentena.
- auditoria e RPC administrativa não são executáveis por `PUBLIC`, `anon` ou `authenticated`; `service_role` tem somente a execução prevista.
- `CONFIRM_NOT_CREATED` e `CONFIRM_TERMINAL_NO_RESULT` terminalizam e liberam uma vez; repetição com mesma resolução é idempotente; conflito de chave/decisão falha sem mutação.
- `ATTACH_RESPONSE_ID` preserva `PROCESSING`, reserva, job e chave; posteriormente permite a rota normal de retrieve ou cancelamento.
- erros, chamadas duplicadas e concorrência não consomem quota; somente finalização de sucesso consome uma vez.

### Execução final

- Deno completo com type-check.
- `npx supabase db reset` e `npx supabase test db` completos.
- testes focados provider, worker, cancelamento, resolução administrativa e grants.
- `git diff --check`.
- Revisão de secrets/logging e confirmação de que branch/config não reativa o cron.

## Fora do escopo

Alteração manual ou tentativa de reparo do job real; reativação do cron; painel administrativo; alteração de API pública Android; nova política de quota; troca de modelo; mudanças de Auth, RLS, Storage, Google Drive, prompt de negócio ou grants de cliente.
