import { strict as assert } from "node:assert";
import {
  createAiSyllabusCancelHandler,
  type AiSyllabusCancelDependencies,
} from "../../functions/ai-syllabus-cancel/index.ts";
import type { AiJobRecord, AiJobCancellationStore, CancellationReconciliation } from "../../functions/_shared/job-finalizer.ts";
import { JobStoreError, SupabaseAiJobStore } from "../../functions/_shared/job-finalizer.ts";
import type { OpenAiProvider, ProviderResponse } from "../../functions/_shared/openai-provider.ts";

const USER_ID = "00000000-0000-0000-0000-0000000000a1";

const validOutput = JSON.stringify({
  schemaVersion: 1,
  promptVersion: "syllabus-v1",
  modelVersion: "gpt-6-luna",
  documentTitle: "Edital",
  subjects: [{
    name: "Direito",
    position: 0,
    suggestedPriority: "NORMAL",
    sourcePages: [1],
    topics: [{ name: "Constituição", position: 0, sourcePages: [1], children: [] }],
  }],
  warnings: [],
  ambiguities: [],
});

function job(overrides: Partial<AiJobRecord> = {}): AiJobRecord {
  return {
    id: "job-1",
    userId: USER_ID,
    feature: "SYLLABUS_GENERATION",
    status: "RESERVED",
    idempotencyKey: "idempotency-1",
    requestFingerprint: "fingerprint-1",
    requestPayload: {},
    sourceObjectPath: "user/source.pdf",
    sourceHash: "a".repeat(64),
    sourceBytes: 10,
    sourcePages: 1,
    sourceFileCount: 1,
    sourceMimeType: "application/pdf",
    openaiResponseId: null,
    providerExecutionStartedAt: null,
    providerReconciledAt: null,
    providerResultRecoverable: null,
    promptVersion: null,
    schemaVersion: null,
    modelVersion: null,
    proposal: null,
    warnings: [],
    errorCode: null,
    errorMessage: null,
    createdAt: "2026-09-24T12:00:00Z",
    updatedAt: "2026-09-24T12:00:00Z",
    finishedAt: null,
    ...overrides,
  };
}

class FakeCancellationStore implements AiJobCancellationStore {
  current: AiJobRecord;
  quota = "RESERVED";
  readonly events: string[] = [];
  reconciliationTerminal: AiJobRecord["status"] | null = null;

  constructor(initial: AiJobRecord) {
    this.current = structuredClone(initial);
  }

  async getJob(userId: string, jobId: string): Promise<AiJobRecord | null> {
    return this.current.userId === userId && this.current.id === jobId ? structuredClone(this.current) : null;
  }

  async requestCancellation(): Promise<AiJobRecord> {
    this.events.push("request");
    return structuredClone(this.current);
  }

  async cancelWithoutProvider(): Promise<AiJobRecord> {
    this.events.push("cancel-without-provider");
    if (this.current.providerExecutionStartedAt || this.current.openaiResponseId) {
      throw new JobStoreError("CANCELLATION_RECONCILIATION_REQUIRED", 409);
    }
    this.current.status = "CANCELLED";
    this.quota = "RELEASED";
    return structuredClone(this.current);
  }

  async recordCancellationReconciliation(_userId: string, _jobId: string, reconciliation: CancellationReconciliation): Promise<AiJobRecord> {
    this.events.push(`reconcile:${reconciliation.providerStatus}:${reconciliation.resultRecoverable}`);
    if (this.reconciliationTerminal) {
      this.current.status = this.reconciliationTerminal;
      this.quota = "CONSUMED";
      return structuredClone(this.current);
    }
    this.current.openaiResponseId = reconciliation.responseId ?? this.current.openaiResponseId;
    this.current.providerExecutionStartedAt = this.current.providerExecutionStartedAt ?? (reconciliation.responseId ? "started" : null);
    this.current.providerReconciledAt = "reconciled";
    this.current.providerResultRecoverable = reconciliation.resultRecoverable;
    return structuredClone(this.current);
  }

  async cancelAfterReconciliation(_userId: string, _jobId: string, _code: string, _message: string, terminalStatus: "CANCELLED" | "FAILED" = "CANCELLED"): Promise<AiJobRecord> {
    this.events.push("cancel-after-reconciliation");
    this.current.status = terminalStatus;
    this.quota = "RELEASED";
    return structuredClone(this.current);
  }

  async finalizeCancellationSuccess(_userId: string, _jobId: string, response: ProviderResponse, _output: Record<string, unknown>): Promise<AiJobRecord> {
    this.events.push(`success:${response.id}`);
    this.current.status = "SUCCEEDED";
    this.current.openaiResponseId = response.id;
    this.current.providerExecutionStartedAt = "started";
    this.current.providerReconciledAt = "reconciled";
    this.current.providerResultRecoverable = true;
    this.quota = "CONSUMED";
    return structuredClone(this.current);
  }
}

function provider(retrieve: () => Promise<ProviderResponse>, cancel: () => Promise<ProviderResponse>): OpenAiProvider {
  return {
    async start() { throw new Error("start must not be called by cancellation"); },
    retrieve,
    cancel,
  };
}

function dependencies(store: FakeCancellationStore, openAi: OpenAiProvider): AiSyllabusCancelDependencies {
  return {
    authenticate: async () => ({ userId: USER_ID }),
    jobs: store,
    provider: openAi,
  };
}

async function cancel(handler: (request: Request) => Promise<Response>): Promise<Response> {
  return handler(new Request("https://example.test/ai-syllabus/jobs/job-1/cancel", {
    method: "POST",
    headers: { authorization: "Bearer supabase-jwt" },
  }));
}

function responseBody(response: Response): Promise<Record<string, unknown>> {
  return response.json() as Promise<Record<string, unknown>>;
}

function errorCode(body: Record<string, unknown>): string {
  return ((body.error as Record<string, unknown>).code as string);
}

Deno.test("cancels RESERVED atomically and releases quota before provider execution", async () => {
  const store = new FakeCancellationStore(job());
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(async () => { throw new Error(); }, async () => { throw new Error(); })));
  const response = await cancel(handler);
  assert.equal(response.status, 200);
  assert.equal((await responseBody(response)).status, "CANCELLED");
  assert.equal(store.quota, "RELEASED");
  assert.deepEqual(store.events, ["request", "cancel-without-provider"]);
});

Deno.test("does not use the pre-provider path when RESERVED has provider-start evidence", async () => {
  const store = new FakeCancellationStore(job({ providerExecutionStartedAt: "started" }));
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(async () => { throw new Error(); }, async () => { throw new Error(); })));
  const response = await cancel(handler);
  assert.equal(response.status, 202);
  assert.equal((await responseBody(response)).status, "RESERVED");
  assert.equal(store.current.status, "RESERVED");
  assert.equal(store.quota, "RESERVED");
  assert.deepEqual(store.events, ["request", "reconcile:unknown:true"]);
});

Deno.test("cancels PROCESSING without a response id only when no provider evidence exists", async () => {
  const store = new FakeCancellationStore(job({ status: "PROCESSING" }));
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(async () => { throw new Error(); }, async () => { throw new Error(); })));
  const response = await cancel(handler);
  assert.equal(response.status, 200);
  assert.equal((await responseBody(response)).status, "CANCELLED");
  assert.equal(store.quota, "RELEASED");
});

Deno.test("queries and cancels a provider response before releasing quota", async () => {
  const store = new FakeCancellationStore(job({ status: "PROCESSING", openaiResponseId: "resp-1", providerExecutionStartedAt: "started" }));
  const calls: string[] = [];
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(
    async () => { calls.push("retrieve"); return { id: "resp-1", status: "in_progress", outputText: null, usage: null }; },
    async () => { calls.push("cancel"); return { id: "resp-1", status: "cancelled", outputText: null, usage: null }; },
  )));
  const response = await cancel(handler);
  assert.equal(response.status, 200);
  assert.equal((await responseBody(response)).status, "CANCELLED");
  assert.deepEqual(calls, ["retrieve", "cancel"]);
  assert.equal(store.quota, "RELEASED");
});

Deno.test("lets a completed provider result win and consume quota", async () => {
  const store = new FakeCancellationStore(job({ status: "PROCESSING", openaiResponseId: "resp-1", providerExecutionStartedAt: "started" }));
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(
    async () => ({ id: "resp-1", status: "completed", outputText: validOutput, usage: null }),
    async () => { throw new Error("cancel must not run after completion"); },
  )));
  const response = await cancel(handler);
  assert.equal(response.status, 200);
  assert.equal((await responseBody(response)).status, "SUCCEEDED");
  assert.equal(store.quota, "CONSUMED");
});

Deno.test("releases quota as FAILED when a completed provider result is invalid", async () => {
  const store = new FakeCancellationStore(job({ status: "PROCESSING", openaiResponseId: "resp-1", providerExecutionStartedAt: "started" }));
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(
    async () => ({ id: "resp-1", status: "completed", outputText: "not-a-proposal", usage: null }),
    async () => { throw new Error("cancel must not run after completion"); },
  )));
  const response = await cancel(handler);
  assert.equal(response.status, 200);
  assert.equal((await responseBody(response)).status, "FAILED");
  assert.equal(store.quota, "RELEASED");
});

Deno.test("lets a late completion returned by provider cancellation win", async () => {
  const store = new FakeCancellationStore(job({ status: "PROCESSING", openaiResponseId: "resp-1", providerExecutionStartedAt: "started" }));
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(
    async () => ({ id: "resp-1", status: "in_progress", outputText: null, usage: null }),
    async () => ({ id: "resp-1", status: "completed", outputText: validOutput, usage: null }),
  )));
  const response = await cancel(handler);
  assert.equal(response.status, 200);
  assert.equal((await responseBody(response)).status, "SUCCEEDED");
  assert.equal(store.quota, "CONSUMED");
});

Deno.test("does not overwrite a worker success that wins while cancellation is reconciling", async () => {
  const store = new FakeCancellationStore(job({ status: "PROCESSING", openaiResponseId: "resp-1", providerExecutionStartedAt: "started" }));
  store.reconciliationTerminal = "SUCCEEDED";
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(
    async () => ({ id: "resp-1", status: "failed", outputText: null, usage: null }),
    async () => { throw new Error("cancel must not run after the worker wins"); },
  )));
  const response = await cancel(handler);
  assert.equal(response.status, 200);
  assert.equal((await responseBody(response)).status, "SUCCEEDED");
  assert.equal(store.events.includes("cancel-after-reconciliation"), false);
});

Deno.test("keeps PROCESSING recoverable when provider state is unknown", async () => {
  const store = new FakeCancellationStore(job({ status: "PROCESSING", openaiResponseId: "resp-1", providerExecutionStartedAt: "started" }));
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(
    async () => { throw new Error("provider unavailable"); },
    async () => { throw new Error("provider unavailable"); },
  )));
  const response = await cancel(handler);
  assert.equal(response.status, 202);
  assert.equal((await responseBody(response)).status, "PROCESSING");
  assert.equal(store.quota, "RESERVED");
});

Deno.test("repeated cancellation of a terminal job is deterministic and does not call provider", async () => {
  const store = new FakeCancellationStore(job({ status: "CANCELLED" }));
  let calls = 0;
  const handler = createAiSyllabusCancelHandler(dependencies(store, provider(
    async () => { calls += 1; throw new Error(); },
    async () => { calls += 1; throw new Error(); },
  )));
  const response = await cancel(handler);
  assert.equal(response.status, 200);
  assert.equal((await responseBody(response)).status, "CANCELLED");
  assert.equal(calls, 0);
});

Deno.test("uses the server role for provider reconciliation and success finalization RPCs", async () => {
  const authHeaders: string[] = [];
  const row = {
    id: "job-1",
    user_id: USER_ID,
    feature: "SYLLABUS_GENERATION",
    status: "PROCESSING",
    idempotency_key: "idempotency-1",
    request_fingerprint: "fingerprint-1",
    request_payload: {},
    source_object_path: "user/source.pdf",
    source_hash: "a".repeat(64),
    source_bytes: 10,
    source_pages: 1,
    source_file_count: 1,
    source_mime_type: "application/pdf",
    source_metadata: {},
    openai_response_id: "resp-1",
    provider_execution_started_at: "started",
    provider_reconciled_at: "reconciled",
    provider_result_recoverable: false,
    prompt_version: null,
    schema_version: null,
    model_version: null,
    proposal: null,
    warnings: [],
    error_code: null,
    error_message: null,
    created_at: "2026-09-24T12:00:00Z",
    updated_at: "2026-09-24T12:01:00Z",
    finished_at: null,
  };
  const store = new SupabaseAiJobStore({
    supabaseUrl: "https://supabase.example.test",
    publishableKey: "publishable-key",
    accessToken: "user-token",
    serviceRoleKey: "service-role-key",
    fetcher: async (_input, init) => {
      authHeaders.push(String(new Headers(init?.headers).get("authorization")));
      return Response.json([row]);
    },
  });
  await store.recordCancellationReconciliation(USER_ID, "job-1", {
    responseId: "resp-1",
    providerStatus: "cancelled",
    resultRecoverable: false,
  });
  await store.finalizeCancellationSuccess(USER_ID, "job-1", {
    id: "resp-1",
    status: "completed",
    outputText: validOutput,
    usage: null,
  }, JSON.parse(validOutput));
  assert.deepEqual(authHeaders, ["Bearer service-role-key", "Bearer service-role-key"]);
});
