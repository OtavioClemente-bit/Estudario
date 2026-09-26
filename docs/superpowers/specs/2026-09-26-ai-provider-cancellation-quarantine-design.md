# IA: diagnóstico do provider, cancelamento e quarentena de execução

**Status:** proposta escrita para revisão; nenhuma alteração de produto foi iniciada.

**Objetivo:** tornar seguro e operacional o ciclo provider/worker/cancelamento antes de reativar o cron. Falhas após o início potencial da chamada OpenAI não podem provocar um novo `POST /responses` às cegas nem deixar a reserva de quota presa sem procedimento administrativo de saída.

**Job de referência:** `ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9`. O job real não será reparado nem alterado manualmente. A migration pode aplicar a ele o backfill interno, controlado e idempotente especificado abaixo para introduzir os novos invariantes. Não será terminalizado, não terá quota consumida/liberada, não receberá `response_id` inventado nem reconciliação inventada. Esse backfill não é reparo manual. O cron permanece pausado.

**Revisão formal:** esta versão incorpora os requisitos de linearização entre cancelamento e início do provider, backfill dos jobs legados, overloads de claim, deadlines de processamento e idempotência/auditoria administrativa. O plano de implementação permanece pendente de nova aprovação formal desta especificação.

## Restrições aprovadas

- Preservar `gpt-6-luna`, `AI_DEFAULT_MODEL`, `OPENAI_API_KEY`, Android, autenticação, RLS, Storage, idempotência, quotas, worker e contratos públicos, exceto os ajustes internos necessários abaixo.
- Nenhuma RPC backend/admin poderá ser executada por `anon` ou `authenticated`; operações de worker e resolução ficam restritas a `service_role` (administração direta continua possível pelo papel administrativo do banco).
- Não reativar o cron, alterar manualmente/reparar o job real, inventar `response_id` ou marcar cancelamento como confirmado sem evidência. O backfill interno da migration descrito nesta especificação é permitido e não constitui reparo manual.
- Erros públicos permanecem genéricos. Nenhum segredo, JWT, chave OpenAI, PDF, base64, prompt, header `Authorization` ou conteúdo privado será registrado.
- Não se presume suporte a replay idempotente do `POST /responses`: a referência oficial consultada não documenta uma garantia para `Idempotency-Key`. Até existir contrato oficial/teste que a estabeleça, o worker não repetirá `provider.start()` após qualquer resultado de entrega incerto. [Create a model response — OpenAI API Reference](https://developers.openai.com/api/reference/cli/resources/responses/methods/create)

## 1. Modelo de estado

O status do job continua sendo `RESERVED`, `PROCESSING`, `SUCCEEDED`, `FAILED`, `EXPIRED` ou `CANCELLED`; os estados terminais e as transições públicas não mudam. O ciclo de entrega ao provider passa a ter estado interno próprio.

### Estado interno da tentativa do provider

Campo proposto: `provider_start_outcome`, com valores controlados:

| Valor | Significado | Pode iniciar novo `POST /responses` automaticamente? |
| --- | --- | --- |
| `NOT_STARTED` | Nenhuma tentativa marcada | Sim, se não houver cancelamento |
| `IN_FLIGHT` | Tentativa lógica marcada duravelmente antes de invocar o transporte; ainda sem resultado persistido | Não |
| `NOT_SENT` | O cliente provou que falhou antes de invocar/enviar o request | Não neste job; pode encerrar e liberar reserva com segurança |
| `PROVIDER_REJECTED` | Uma resposta HTTP não-2xx foi recebida | Não; a resposta não prova, por si só, que nenhuma Response foi criada |
| `TRANSPORT_AMBIGUOUS` | Timeout, abort, conexão interrompida ou transporte sem resposta conclusiva | Não |
| `RESPONSE_AMBIGUOUS` | Houve resposta potencialmente aceita, mas não foi possível obter/persistir um `response_id` utilizável | Não |
| `ACCEPTED` | Um `response_id` válido foi persistido | Nunca iniciar outra criação; somente recuperar/cancelar esse ID |

Campo canônico proposto: `provider_quarantined_at timestamptz NULL`.

**Definição única de quarentena:** `provider_quarantined_at IS NOT NULL`. O worker, a claim SQL, o cancelamento, as regras de release de quota e os testes usam esse marcador explícito, não inferem quarentena de combinações implícitas.

Após a migration, a fonte semântica canônica para decisão de entrega e reconciliação é a tupla `provider_start_outcome` + `provider_quarantined_at` + `openai_response_id`. `provider_execution_started_at` permanece como evidência temporal/auditável de que a tentativa lógica foi marcada; isoladamente, **não prova** que a OpenAI recebeu ou criou uma Response e não pode decidir, por si só, retry, cancelamento definitivo, bloqueio de finalização ou release de quota. Assim, `IN_FLIGHT` seguido de erro local comprovado antes de `fetch` pode transicionar para `NOT_SENT` e finalizar/liberar com segurança mesmo com `provider_execution_started_at` preenchido. O plano deverá inventariar todos os consumidores dessas colunas, em especial `cancel_ai_job_without_provider`, `worker/preProviderDefinitive`, finalização/release, endpoint de cancelamento, reconciliação e qualquer SQL que trate o timestamp como prova de envio.

Adicionar `LEGACY_AMBIGUOUS` ao domínio de `provider_start_outcome` para representar registros históricos cuja execução pode ter começado, mas cuja categoria de falha não foi persistida. Não converter esse estado em `TRANSPORT_AMBIGUOUS` ou `RESPONSE_AMBIGUOUS`: a evidência histórica não permite distinguir os dois.

### Backfill obrigatório da migration

O backfill classifica os registros existentes sem reexecutar nem reconciliar o provider:

| Condição preexistente | `provider_start_outcome` | `provider_quarantined_at` | `provider_result_recoverable` |
| --- | --- | --- | --- |
| `openai_response_id IS NOT NULL` | `ACCEPTED` | `NULL` | preservar evidência existente; o ID é a referência recuperável |
| `openai_response_id IS NULL` e `provider_execution_started_at IS NULL` | `NOT_STARTED` | `NULL` | `NULL` |
| Não terminal, sem response ID, com `provider_execution_started_at IS NOT NULL` | `LEGACY_AMBIGUOUS` | preencher com timestamp do servidor, preservando se já existir | `NULL` (desconhecido; não afirmar recuperabilidade sem response ID/evidência) |
| Terminal, sem response ID, com `provider_execution_started_at IS NOT NULL` | `LEGACY_AMBIGUOUS` | `NULL` (estado terminal não entra na quarentena operacional ativa) | `NULL` |

O backfill só escreve o novo estado interno e a evidência de quarentena necessária. Não muda `status`, reserva/consumo/liberação de quota, `openai_response_id`, timestamps de reconciliação ou resultado do usuário. Em especial, o job `ca9588a7-ee0e-45bb-9fb8-5b602ef1d3d9` permanece `PROCESSING`, com sua reserva `RESERVED`, sem ID anexado e sem reconciliação inventada. Nenhum backfill consome nem libera quota.

Invariantes:

- `IN_FLIGHT` marca o job como em quarentena atomicamente antes de chamar a OpenAI. Isso cobre queda do worker entre o marcador e o request.
- Uma resposta `ACCEPTED` com `response_id` persistido limpa a quarentena na mesma RPC que grava o ID. Se o provider devolver 2xx com ID válido, mas a persistência inicial falhar, o worker conserva esse ID em memória e tenta a RPC backend de recuperação de persistência definida abaixo; nunca chama `provider.start()` novamente.
- `PROVIDER_REJECTED`, `TRANSPORT_AMBIGUOUS` e `RESPONSE_AMBIGUOUS` mantêm a quarentena e a reserva.
- `NOT_SENT` é terminalizado atomicamente, nunca em duas etapas. A mesma RPC lease-bound valida status/lease/outcome, registra `NOT_SENT` e reconciliação negativa, escolhe `CANCELLED` se `cancellation_requested_at IS NOT NULL` ou `FAILED` caso contrário, libera a reserva, limpa lease e quarentena, e torna o job terminal. Não existe estado durável `PROCESSING + NOT_SENT + provider_quarantined_at IS NULL`; a transação inteira confirma ou reverte. Depois de `NOT_SENT`, o worker nunca chama provider novamente para esse job.
- Resolução administrativa válida limpa a quarentena sob lock transacional, vinculando a decisão à auditoria.
- `provider_result_recoverable` só significa resultado realmente recuperável confirmado; não será definido como `true` apenas porque uma chamada ficou incerta.

### Invariantes persistentes do banco

A migration deve expressar estes invariantes por CHECK/constraints e/ou RPCs transacionais com cobertura pgTAP, sem criar combinação intermediária durável inválida:

- `NOT_STARTED`: `openai_response_id IS NULL` e sem quarentena ativa.
- `IN_FLIGHT`, `TRANSPORT_AMBIGUOUS`, `PROVIDER_REJECTED`, `RESPONSE_AMBIGUOUS` ou `LEGACY_AMBIGUOUS` em job não terminal: quarentena obrigatória enquanto não resolvido.
- `ACCEPTED`: `openai_response_id IS NOT NULL` e sem quarentena de criação.
- `NOT_SENT`: só pode existir associado a terminalização atômica; nunca pode permanecer em job `PROCESSING` com quarentena nula. Em caso de falha transacional, estado e quota revertem ao estado anterior em quarentena.
- Jobs terminais não são elegíveis nem à claim normal nem à claim de reconciliação.
- O backfill deve satisfazer estas regras na própria migration: `LEGACY_AMBIGUOUS` não terminal recebe quarentena; `ACCEPTED` tem ID e não tem quarentena; `NOT_STARTED` não tem ID/quarentena; nenhum backfill muda status/reserva/quota.

```text
NOT_STARTED
    └─ marcar antes do HTTP → IN_FLIGHT + provider_quarantined_at
          ├─ response_id persistido → ACCEPTED + limpar quarentena
          ├─ falha comprovadamente antes do envio → RPC única NOT_SENT + terminalização + RELEASE
          ├─ HTTP não-2xx → PROVIDER_REJECTED + manter quarentena
          ├─ timeout/transporte → TRANSPORT_AMBIGUOUS + manter quarentena
          ├─ resposta sem ID → RESPONSE_AMBIGUOUS + manter quarentena
          └─ ID válido recebido, persistência inicial falha → persistência de recuperação do mesmo ID; se falhar/morrer, manter quarentena

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

1. A RPC normal de claim não seleciona jobs com `cancellation_requested_at IS NOT NULL` nem `provider_quarantined_at IS NOT NULL`. Continua `service_role`-only e lease-bound. Essa condição vale para **cada overload existente que permaneça executável**, não apenas para a assinatura usada pelo worker atual. A implementação deve inventariar assinaturas e ACLs; atualizar todos os overloads retidos ou revogar/remover o overload legado somente depois de provar que não há consumidor. Nenhum overload recebe grant para `anon`/`authenticated`.
2. `mark_ai_job_provider_execution_started` é o **ponto de linearização** entre pedido de cancelamento e início lógico do provider. Sob o mesmo lock transacional da linha do job usado por `request_ai_job_cancellation`, deve validar `status = PROCESSING`, lease válido (owner/token/generation e validade temporal), `cancellation_requested_at IS NULL`, `openai_response_id IS NULL` e estado de provider sem início/quarentena incompatível. Só então grava `IN_FLIGHT` e `provider_quarantined_at` atomicamente. O worker não pode usar uma verificação em memória como autoridade para iniciar.
3. Se `request_ai_job_cancellation` obtiver o lock primeiro e persistir o pedido, o `mark...started` subsequente falha com resultado/código estável de cancelamento pendente e **nenhuma invocação de `provider.start()`/novo envio pode começar**. Se `mark...started` obtiver o lock e confirmar primeiro, a tentativa lógica está ordenada antes do cancelamento; o pedido posterior segue reconciliação/cancelamento e nunca autoriza um segundo `provider.start()`. O adapter pode então provar que falhou antes do `fetch`, produzindo `NOT_SENT`; essa evidência permite finalização segura apesar do marco temporal já preenchido.
4. Depois da claim/assert lease, o worker revalida o estado retornado e nunca inicia source/provider normal se o cancelamento tiver sido solicitado. A validação de memória é defesa adicional; o RPC de linearização é a autoridade que fecha a corrida.
5. Sem início do provider e sem response ID, o cancelamento local já existente pode terminalizar e liberar a reserva somente quando o lease/estado permitir.
6. Com `response_id`, o endpoint de cancelamento continua sendo a rota de reconciliação: retrieve; se ativo, cancel; se completed, preservar/finalizar resultado válido; se terminal sem resultado, reconciliar e então cancelar/liberar; se provider/transporte continuar inconclusivo, retornar pendente sem release.
7. Com quarentena e sem `response_id`, o endpoint registra/retorna pendência segura (HTTP 202), sem marcar cancelamento confirmado nem liberar quota. A exclusão da claim normal não remove a rota do endpoint de cancelamento ou da resolução administrativa.
8. Se o cancelamento chegar depois da claim e houver ID conhecido, o worker não inicia/reinicia provider normal; ele encaminha para a mesma lógica de retrieve/cancel/reconcile usada pelo endpoint, sem criar nova Response. Se a reconciliação não puder terminar, mantém estado pendente.

### Persistência de recuperação do `response_id` conhecido

O ID retornado por uma resposta 2xx válida é evidência mais forte que `RESPONSE_AMBIGUOUS`: nesse caso o worker conhece o ID, ainda que a gravação inicial tenha falhado. Deve existir uma rota RPC de recuperação backend (`service_role`-only), idempotente e separada do caminho que depende do lease normal ativo. Sob lock da linha do job, ela exige `status = PROCESSING`, quarentena compatível (`IN_FLIGHT` ou `RESPONSE_AMBIGUOUS`), e `openai_response_id IS NULL` ou já igual ao ID recebido. Persiste o mesmo ID, define `provider_start_outcome = ACCEPTED`, limpa a quarentena e aplica a política de `processing_deadline_at` descrita abaixo. Repetição do mesmo ID é sucesso idempotente; ID diferente já persistido é conflito; job terminal é rejeitado. Não inicia Response, não consome/libera quota e não exige lease ativo, pois justamente recupera após sua expiração. Se `cancellation_requested_at` existir, a gravação continua permitida, mas não libera o job para processamento normal: aciona a rota backend de reconciliação/cancelamento abaixo. Se a persistência de recuperação falhar ou o processo morrer, o job continua em quarentena para resolução administrativa.

### Continuação backend para cancelamento pendente

`ATTACH_RESPONSE_ID` em job com `cancellation_requested_at IS NOT NULL` deve deixar trabalho pendente determinístico, sem depender de o Android repetir a chamada após o 202 original. O executor concreto será o `ai-syllabus-worker` existente: em cada invocação, após autenticação backend, verifica primeiro a claim de reconciliação; se não houver item, segue para a claim de geração normal. A fila/claim separada é `service_role`-only e seleciona somente jobs `PROCESSING` com cancelamento solicitado e `openai_response_id` conhecido. Cada item recebe lease exclusivo com token/generation/expiração (ou mecanismo transacional equivalente), impedindo dois workers de executar retrieve/cancel simultaneamente. O executor faz exclusivamente retrieve/cancel/reconcile do ID conhecido e **nunca** `provider.start()`; não é a claim normal de geração. A resolução administrativa e a persistência de recuperação de ID conhecido devem deixar o item durável e claimable de forma idempotente. Reconciliar usa deadline próprio, distinto de `processing_deadline_at`, com bounded retry/backoff; expiração do lease torna o item novamente pendente, não libera quota. Falha de transporte ou retrieve/cancel ambíguo mantém `PROCESSING`, reconciliação pendente e reserva `RESERVED`. Resultado terminal confirmado finaliza e aplica o efeito de quota uma única vez. Essa solução reutiliza o cron/worker já existente quando futuramente reativado; esta especificação não o reativa nem altera sua configuração.

### `processing_deadline_at` e quarentena

Tempo passado em quarentena não pode tornar imediatamente expirado um response ID recuperado depois. Tanto `ATTACH_RESPONSE_ID` quanto a persistência de recuperação do ID conhecido, sob lock, associam o ID, gravam `ACCEPTED`, limpam `provider_quarantined_at` e definem `processing_deadline_at = NULL`. A próxima claim/reconciliação estabelece um novo deadline limitado a partir do estado recuperado; não reutiliza o deadline que venceu durante a quarentena. Se `cancellation_requested_at IS NOT NULL`, não tornar o job elegível à claim normal: disparar a claim/fila separada de reconciliação backend definida acima, que trata diretamente o ID. Isso não altera o status nem a reserva por si só.

## 4. Resolução administrativa da quarentena

Não haverá UI administrativa nesta entrega. Uma RPC transacional backend/admin-only, proposta como `resolve_ai_job_provider_quarantine(...)`, será a saída operacional auditável.

Cada chamada inclui `resolution_id UUID`, único na auditoria, decisão e payload normalizado. Calcular e armazenar um fingerprint determinístico dos campos semânticos normalizados da decisão/payload, incluindo `response_id` ou status do provider quando aplicável, referência do operador e referência de evidência. Repetir o mesmo `resolution_id` com o mesmo fingerprint retorna o resultado original sem nova mutação. Reutilizar o ID com fingerprint/decisão diferente retorna conflito e não altera job, quota nem auditoria original. A chave não é apenas única por job: sua política de unicidade deve permitir identificar replay e impedir colisão entre jobs (UUID global único).

Auditoria registra: `job_id`, `resolution_id`, decisão, fingerprint, identidade técnica do chamador (role/backend autenticado no contexto da RPC), `operator_reference`/ator humano quando fornecido por caller confiável, `evidence_reference` allowlisted e curta, e timestamp do servidor. Não inferir identidade humana de `auth.uid()` para chamada feita com service role. Validar `operator_reference` com charset allowlist e comprimento máximo de 128 caracteres; `evidence_reference` com charset allowlist seguro para referência opaca (sem URL arbitrária/creds) e máximo de 256 caracteres. Ausente é permitido quando a política operacional não a exige, mas as decisões que dependem de evidência devem exigi-la conforme regra da RPC. Não armazenar body bruto do provider, prompt, PDF, JWT, chaves ou headers.

Decisões aceitas:

- `CONFIRM_NOT_CREATED`: ator confirma com evidência que nenhuma execução/Response pode ser recuperada. Só aceita job em quarentena sem ID; registra reconciliação negativa e terminaliza (`CANCELLED` se há cancelamento solicitado; caso contrário `FAILED`), liberando a reserva atomicamente.
- `ATTACH_RESPONSE_ID`: associa um ID recuperado externamente a um job em quarentena, limpa a quarentena e preserva o job/reserva em `PROCESSING`. Com cancelamento pendente, seguir retrieve/cancel; sem cancelamento, o worker poderá retrieve o ID. Não cria job nem consome quota.
- `CONFIRM_TERMINAL_NO_RESULT`: ator fornece estado terminal confirmado pelo provider (`failed`, `cancelled`, `expired` ou `incomplete`), sem resultado recuperável. O mapeamento local é exato e não depende de cancelamento pendente: provider `failed` → job `FAILED`; `cancelled` → `CANCELLED`; `expired` → `EXPIRED`; `incomplete` → `FAILED` com código interno `PROVIDER_INCOMPLETE` (não existe proposta bem-sucedida para finalizar). Todos esses casos registram reconciliação e liberam a reserva atomicamente. O eventual pedido de cancelamento permanece na auditoria, mas não converte um fato terminal diferente confirmado pelo provider.

Auditoria proposta em `ai_job_provider_resolution_audit`: os campos definidos acima, mais status/ID de provider quando aplicáveis (somente ID e estado, nunca body). Tabela sem acesso runtime direto por `anon`/`authenticated`; escrita via RPC `SECURITY DEFINER`. Lock na linha do job serializa resolução com worker/cancelamento; decisões incompatíveis posteriores não podem mudar jobs terminais. A RPC deve verificar replay por `resolution_id`/fingerprint antes de tentar aplicar nova transição, mantendo a operação idempotente em concorrência. Ao executar `ATTACH_RESPONSE_ID` com cancelamento pendente, a mesma transação também registra/enfileira a continuação de reconciliação backend durável, sem depender do cliente.

**Precedência contra worker/lease ativo:** decisões destrutivas (`CONFIRM_NOT_CREATED` e `CONFIRM_TERMINAL_NO_RESULT`) não podem terminalizar/liberar quota enquanto houver executor potencialmente ativo. A RPC locka o job e exige lease de processamento ausente ou expirado, salvo a exceção abaixo. Com lease ainda válido, retorna `PENDING/CONFLICT` estável sem alteração de job, auditoria decisória ou quota; a operação pode ser tentada de novo com o mesmo `resolution_id` após expiração, desde que nenhum resultado tenha sido gravado. Exceção: `CONFIRM_TERMINAL_NO_RESULT` pode prosseguir com lease válido somente quando apresenta evidência terminal do próprio provider para o mesmo response ID já conhecido e o mapeamento terminal desta especificação; o fato terminal torna irrelevante qualquer resposta concorrente para aquela Response. `CONFIRM_NOT_CREATED` nunca tem essa exceção. `ATTACH_RESPONSE_ID` pode concorrer com worker: mesmo ID converge idempotentemente, ID divergente conflita sem sobrescrever, e o worker ao retomar deve observar estado terminal/accepted e não substituir evidência.

Ordenamentos obrigatórios: (A) resolução destrutiva após lease expirado pode aplicar a decisão comprovada; (B) resolução destrutiva com lease ativo é pendente/rejeitada sem mutação de quota, exceto a exceção estrita de provider-terminal acima; (C) resposta do worker e `ATTACH_RESPONSE_ID` com mesmo ID convergem, IDs diferentes geram conflito sem sobrescrita.

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
- Atualizar `mark_ai_job_provider_execution_started` para ser ponto de linearização sob lock compartilhado com cancelamento, validar status/lease/cancelamento/ID/estado e gravar `IN_FLIGHT` + quarentena antes da rede; atualizar `persist_ai_job_provider_response` para gravar `ACCEPTED` e limpar quarentena atomicamente.
- Nova RPC lease-bound para registrar `NOT_SENT`/`PROVIDER_REJECTED`/`TRANSPORT_AMBIGUOUS`/`RESPONSE_AMBIGUOUS`, sem liberar quota por categoria incerta.
- Implementar `NOT_SENT` como operação única lease-bound que grava outcome/reconciliação negativa, terminaliza conforme cancelamento, libera quota e limpa lease/quarentena atomicamente; não expor uma RPC que apenas limpe quarentena para posterior terminalização.
- Inventariar todas as assinaturas de `claim_ai_syllabus_worker_job`; garantir em cada overload mantido a exclusão de cancelamento/quarentena; revogar/remover overload legado somente se a busca de consumidores confirmar que está sem uso. Manter todos backend-only.
- Fazer backfill idempotente de `provider_start_outcome`, `provider_quarantined_at` e `provider_result_recoverable` segundo a tabela acima, sem qualquer transição pública/terminal e sem efeito de quota.
- Adicionar RPC backend idempotente para persistir o mesmo `response_id` conhecido após falha/expiração do lease normal, com as precondições, conflito, proteção terminal e semântica de quota especificadas acima.
- Adicionar claim/fila separada backend-only, com lease/token/generation e deadline/retry próprios, para continuar cancelamento/reconciliação após `ATTACH_RESPONSE_ID` ou recuperação de persistência com cancelamento pendente; o `ai-syllabus-worker` existente reivindica essa fila primeiro em cada invocação e, sem item de reconciliação, segue para geração normal. Nunca executar provider start nessa rota; a configuração do cron não muda e ele permanece pausado.
- Atualizar `ATTACH_RESPONSE_ID` e a persistência de recuperação para limpar o deadline vencido e assegurar nova janela limitada na próxima reconciliação; cancelamento pendente continua fora da claim normal.
- Atualizar release/finalização para impedir release comum de job em quarentena; RPC administrativa limpa/reconcilia e chama release dentro da mesma transação.
- Adicionar constraints/invariantes persistentes para combinações válidas de `provider_start_outcome`, status, ID e quarentena, incluindo proibição de `PROCESSING + NOT_SENT + quarantine NULL`; claims de geração e reconciliação excluem terminais.
- Criar tabela de auditoria privada e a RPC administrativa idempotente de resolução; revogar runtime público/autenticado e conceder somente ao papel backend previsto.
- Preservar todos os overloads e grants não explicitamente afetados por esta mudança. Um overload legado de claim pode ser revogado/removido somente após prova de ausência de consumidor e com cobertura pgTAP. Não alterar scheduler/cron (permanece pausado), modelo, bucket, auth, RLS de usuário ou contratos públicos.

## 7. Plano de testes e critérios de aceitação

### Provider (Deno)

- fallback permanece `gpt-6-luna`; `AI_DEFAULT_MODEL` ainda prevalece.
- metadata enviada no POST `/responses` contém apenas correlação interna não sensível, como `estudario_job_id` (UUID) e `feature = SYLLABUS_GENERATION`, opcionalmente versão de prompt/schema. Não contém `user_id`, email, nome, edital, PDF, prompt, JWT ou segredo. Teste confirma a allowlist e ausência de sentinelas de PII/segredo/arquivo. Se o provider não suportar busca programática por metadata, documentar que ela serve como evidência de busca manual em dashboard/ops; não inventar endpoint de pesquisa.
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
- resposta válida + falha da persistência inicial + persistência de recuperação bem-sucedida grava o mesmo ID; replay do mesmo ID é idempotente, ID diferente dá conflito, job terminal rejeita e lease expirado nunca causa outro POST.
- `NOT_SENT` transaciona outcome, reconciliação negativa, terminalização, release e limpeza de lease/quarentena como uma unidade; simular erro/crash antes do commit deixa estado anterior protegido em quarentena, e após commit o job é terminal e quota foi liberada uma única vez. Nunca há estado durável reclamável `PROCESSING + NOT_SENT + quarantine NULL`.
- POST inclui metadata de correlação allowlisted e sem PII/segredos/conteúdo do arquivo.
- `ATTACH_RESPONSE_ID` com cancelamento pendente aciona a continuação backend; teste prova que ela só chama retrieve/cancel/reconcile do ID e nunca `provider.start()`, e que ambiguidade mantém reserva e estado pendente.

### Banco/pgTAP

- claim exclui cancelamentos e quarentenas; claims e RPCs de worker continuam inacessíveis a `anon`/`authenticated`.
- cada overload de claim que permanecer executável exclui cancelamento e quarentena e mantém ACL backend-only.
- backfill produz `NOT_STARTED`, `ACCEPTED` e `LEGACY_AMBIGUOUS` corretamente, normaliza `provider_result_recoverable` legado para desconhecido quando não há ID, e não consome/libera quota nem altera status/reserva.
- backfill interno controlado do job legado de referência é permitido: define apenas estado/quarentena/evidência previstos, mantendo PROCESSING e reserva RESERVED; não conta como reparo manual.
- teste concorrente determinístico cobre ambos os ordenamentos: cancelamento obtém lock antes de mark-start (mark falha e start não é chamado); mark-start obtém lock antes do cancelamento (grava quarentena e cancelamento posterior só reconcilia, sem segundo start).
- `NOT_SENT` após `provider_execution_started_at` estar preenchido ainda pode terminalizar/liberar corretamente, pois o timestamp isolado não bloqueia a transição.
- resolução destrutiva com lease ativo é rejeitada/pending sem mutação de quota; após expiração pode resolver. Provider-terminal com mesmo response ID só usa a exceção formal definida; `CONFIRM_NOT_CREATED` não pode usá-la.
- corrida worker-response x `ATTACH_RESPONSE_ID`: mesmo ID converge idempotentemente; ID divergente gera conflito e não sobrescreve evidência.
- iniciar marca `IN_FLIGHT`/quarentena; persistir ID limpa ambos atomicamente.
- persistência de recuperação aceita o mesmo ID sem lease ativo, rejeita ID divergente/job terminal e não altera quota; cancelamento pendente enfileira continuação de reconciliação backend.
- release comum falha para quarentena.
- auditoria e RPC administrativa não são executáveis por `PUBLIC`, `anon` ou `authenticated`; `service_role` tem somente a execução prevista.
- `CONFIRM_NOT_CREATED` e `CONFIRM_TERMINAL_NO_RESULT` terminalizam e liberam uma vez; repetição com mesma resolução é idempotente; conflito de chave/decisão falha sem mutação.
- `ATTACH_RESPONSE_ID` preserva `PROCESSING`, reserva, job e chave; posteriormente permite a rota normal de retrieve ou cancelamento.
- `ATTACH_RESPONSE_ID` após o deadline antigo vencer limpa/resetta o deadline; sem cancelamento, a próxima claim/reconciliação recebe janela nova e não expira imediatamente; com cancelamento, a claim normal continua excluída e a rota de cancelamento reconcilia o ID.
- `ATTACH_RESPONSE_ID` com cancelamento pendente aciona automaticamente a rota backend durável de reconciliação; ela nunca chama start e, se retrieve/cancel ficar ambíguo, mantém PROCESSING e quota RESERVED.
- dois workers disputando o mesmo item de reconciliação: exatamente um lease ganha; o executor faz retrieve/cancel/reconcile e zero chamadas start. Retrieve ambíguo mantém reserva; provider terminal finaliza uma vez; o worker invocado pelo cron existente drena reconciliação antes da claim de geração normal.
- constraints rejeitam `NOT_STARTED` com ID/quarentena, estado ambíguo não-terminal sem quarentena, `ACCEPTED` sem ID/com quarentena e `PROCESSING + NOT_SENT + quarantine NULL`; backfill termina satisfazendo constraints; terminais não entram em nenhuma claim.
- `resolution_id` replay idêntico retorna o resultado original; replay divergente dá conflito sem mutação. Testar também concorrência de resolução administrativa contra worker/cancelamento.
- erros, chamadas duplicadas e concorrência não consomem quota; somente finalização de sucesso consome uma vez.
- `CONFIRM_TERMINAL_NO_RESULT` mapeia exatamente `failed→FAILED`, `cancelled→CANCELLED`, `expired→EXPIRED` e `incomplete→FAILED/PROVIDER_INCOMPLETE`, liberando uma única reserva.

### Execução final

- Deno completo com type-check.
- `npx supabase db reset` e `npx supabase test db` completos.
- testes focados provider, worker, cancelamento, resolução administrativa e grants.
- `git diff --check`.
- Revisão de secrets/logging e confirmação de que branch/config não reativa o cron.

## Fora do escopo

Alteração manual ou tentativa de reparo do job real (exceto o backfill interno controlado explicitamente permitido); reativação do cron; painel administrativo; alteração de API pública Android; nova política de quota; troca de modelo; mudanças de Auth, RLS, Storage, Google Drive, prompt de negócio ou grants de cliente.
