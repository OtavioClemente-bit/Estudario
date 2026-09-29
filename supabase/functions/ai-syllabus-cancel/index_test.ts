import { assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  createAiSyllabusCancelHandler,
  extractAiSyllabusCancelJobId,
} from "./index.ts";
import {
  type AiJobCancellationStore,
  type AiJobRecord,
} from "../_shared/job-finalizer.ts";
import type {
  OpenAiProvider,
  ProviderResponse,
} from "../_shared/openai-provider.ts";

const proposal = {
  schemaVersion: 1,
  promptVersion: "syllabus-v1",
  modelVersion: "gpt-6-luna",
  documentTitle: "Edital",
  subjects: [{
    name: "Direito",
    position: 0,
    suggestedPriority: "NORMAL",
    topics: [{
      name: "Constituição",
      position: 0,
      children: [],
      sourcePages: [1],
    }],
    sourcePages: [1],
  }],
  warnings: [],
  ambiguities: [],
};

function job(overrides: Partial<AiJobRecord> = {}): AiJobRecord {
  return {
    id: "11111111-1111-4111-8111-111111111111",
    userId: "user-1",
    feature: "SYLLABUS_GENERATION",
    status: "PROCESSING",
    idempotencyKey: "idem",
    requestFingerprint: "a".repeat(64),
    requestPayload: {},
    sourceObjectPath: null,
    sourceHash: null,
    sourceBytes: null,
    sourcePages: null,
    sourceFileCount: null,
    sourceMimeType: null,
    openaiResponseId: null,
    providerStartOutcome: "NOT_STARTED",
    providerQuarantinedAt: null,
    providerExecutionStartedAt: null,
    providerReconciledAt: null,
    providerResultRecoverable: null,
    leaseExpiresAt: null,
    promptVersion: null,
    schemaVersion: null,
    modelVersion: null,
    proposal: null,
    warnings: [],
    errorCode: null,
    errorMessage: null,
    createdAt: "now",
    updatedAt: "now",
    finishedAt: null,
    ...overrides,
  };
}

function response(
  status: ProviderResponse["status"],
  id = "resp-1",
): ProviderResponse {
  return {
    id,
    status,
    outputText: status === "completed" ? JSON.stringify(proposal) : null,
    usage: null,
  };
}

function harness(
  initial: AiJobRecord,
  providerResponses: ProviderResponse[] = [],
  providerCancelRejects = false,
) {
  let current = initial;
  const calls: string[] = [];
  const jobs = {
    async getJob(userId: string, jobId: string) {
      return userId === current.userId && jobId === current.id ? current : null;
    },
    async requestCancellation() {
      calls.push("request");
      current = { ...current, cancellationRequestedAt: "now" } as AiJobRecord;
      return current;
    },
    async cancelWithoutProvider() {
      calls.push("cancelWithoutProvider");
      current = { ...current, status: "CANCELLED" } as AiJobRecord;
      return current;
    },
    async recordCancellationReconciliation(
      _userId: string,
      _jobId: string,
      input: { responseId: string | null },
    ) {
      calls.push(`record:${input.responseId ?? "none"}`);
      return current;
    },
    async cancelAfterReconciliation(
      _userId: string,
      _jobId: string,
      _code: string,
      _message: string,
      status = "CANCELLED",
    ) {
      calls.push("finalizeCancel");
      current = { ...current, status } as AiJobRecord;
      return current;
    },
    async finalizeCancellationSuccess() {
      calls.push("finalizeSuccess");
      current = { ...current, status: "SUCCEEDED" } as AiJobRecord;
      return current;
    },
  } as unknown as AiJobCancellationStore;
  const provider = {
    async retrieve() {
      calls.push("retrieve");
      const value = providerResponses.shift();
      if (!value) throw new Error("unexpected retrieve");
      return value;
    },
    async cancel() {
      calls.push("providerCancel");
      if (providerCancelRejects) throw new Error("transport unknown");
      const value = providerResponses.shift();
      if (!value) throw new Error("unexpected provider cancel");
      return value;
    },
  } as unknown as OpenAiProvider;
  const handler = createAiSyllabusCancelHandler({
    authenticate: async () => ({ userId: "user-1" }),
    jobs,
    provider,
  });
  return { handler, calls };
}

function request() {
  return new Request(
    "https://example.test/functions/v1/ai-syllabus-cancel/11111111-1111-4111-8111-111111111111",
    { method: "POST", headers: { authorization: "Bearer jwt" } },
  );
}

Deno.test("pre-provider cancellation uses the atomic local cancellation path", async () => {
  const { handler, calls } = harness(job({ status: "RESERVED" }));
  const result = await handler(request());
  assertEquals(result.status, 200);
  assertEquals(calls, ["request", "cancelWithoutProvider"]);
});

Deno.test("already terminalized NOT_SENT cancellation is idempotent and does not contact provider", async () => {
  const { handler, calls } = harness(
    job({ status: "CANCELLED", providerStartOutcome: "NOT_SENT" }),
  );
  const result = await handler(request());
  assertEquals(result.status, 200);
  assertEquals((await result.json()).status, "CANCELLED");
  assertEquals(calls, []);
});

Deno.test("audit timestamp alone does not divert a NOT_STARTED job from local cancellation", async () => {
  const { handler, calls } = harness(
    job({
      providerExecutionStartedAt: "old-audit-time",
      providerStartOutcome: "NOT_STARTED",
    }),
  );
  const result = await handler(request());
  assertEquals(result.status, 200);
  assertEquals(calls, ["request", "cancelWithoutProvider"]);
});

Deno.test("quarantine without response ID remains pending without false reconciliation or quota release", async () => {
  const { handler, calls } = harness(
    job(
      {
        providerExecutionStartedAt: "started",
        providerStartOutcome: "TRANSPORT_AMBIGUOUS",
        providerQuarantinedAt: "quarantined",
      },
    ),
  );
  const result = await handler(request());
  assertEquals(result.status, 202);
  assertEquals((await result.json()).status, "PROCESSING");
  assertEquals(calls, ["request"]);
});

Deno.test("active generation lease prevents provider actions even when a response ID is known", async () => {
  const { handler, calls } = harness(
    job({
      openaiResponseId: "resp-1",
      providerStartOutcome: "ACCEPTED",
      leaseExpiresAt: "2099-01-01T00:00:00Z",
    }),
    [response("in_progress")],
  );
  const result = await handler(request());
  assertEquals(result.status, 202);
  assertEquals(calls, ["request"]);
});

Deno.test("known response active then pending cancellation records ambiguity and retains job", async () => {
  const { handler, calls } = harness(
    job({
      openaiResponseId: "resp-1",
      providerStartOutcome: "ACCEPTED",
      providerExecutionStartedAt: "started",
    }),
    [response("in_progress"), response("in_progress")],
  );
  const result = await handler(request());
  assertEquals(result.status, 202);
  assertEquals(calls, [
    "request",
    "retrieve",
    "providerCancel",
    "record:resp-1",
  ]);
});

Deno.test("known response completed during cancellation preserves successful result", async () => {
  const { handler, calls } = harness(
    job({
      openaiResponseId: "resp-1",
      providerStartOutcome: "ACCEPTED",
      providerExecutionStartedAt: "started",
    }),
    [response("completed")],
  );
  const result = await handler(request());
  assertEquals(result.status, 200);
  assertEquals((await result.json()).status, "SUCCEEDED");
  assertEquals(calls, ["request", "retrieve", "finalizeSuccess"]);
});

Deno.test("known response terminal without result is reconciled then cancelled", async () => {
  const { handler, calls } = harness(
    job({
      openaiResponseId: "resp-1",
      providerStartOutcome: "ACCEPTED",
      providerExecutionStartedAt: "started",
    }),
    [response("cancelled")],
  );
  const result = await handler(request());
  assertEquals(result.status, 200);
  assertEquals((await result.json()).status, "CANCELLED");
  assertEquals(calls, [
    "request",
    "retrieve",
    "record:resp-1",
    "finalizeCancel",
  ]);
});

Deno.test("ambiguous retrieve stays pending and does not release reservation", async () => {
  const { handler, calls } = harness(
    job({
      openaiResponseId: "resp-1",
      providerStartOutcome: "ACCEPTED",
      providerExecutionStartedAt: "started",
    }),
    [],
  );
  const result = await handler(request());
  assertEquals(result.status, 202);
  assertEquals(calls, ["request", "retrieve", "record:resp-1"]);
});

Deno.test("ambiguous provider cancellation stays pending and records the known response ID", async () => {
  const { handler, calls } = harness(
    job({
      openaiResponseId: "resp-1",
      providerStartOutcome: "ACCEPTED",
      providerExecutionStartedAt: "started",
    }),
    [response("in_progress")],
    true,
  );
  const result = await handler(request());
  assertEquals(result.status, 202);
  assertEquals(calls, [
    "request",
    "retrieve",
    "providerCancel",
    "record:resp-1",
  ]);
});

Deno.test("runtime-relative path reaches terminal cancellation idempotently", async () => {
  const id = "d46b1061-cabe-4e7b-b984-80b457d4d1a3";
  const terminalJob = job({
    id,
    status: "SUCCEEDED",
    openaiResponseId: "resp-existing",
  });
  const { handler, calls } = harness(terminalJob);
  const result = await handler(
    new Request(
      `https://example.test/ai-syllabus-cancel/${id}/?source=runtime`,
      { method: "POST", headers: { authorization: "Bearer jwt" } },
    ),
  );
  assertEquals(result.status, 200);
  assertEquals((await result.json()).status, "SUCCEEDED");
  assertEquals(calls, []);
});

Deno.test("extracts only supported cancel function paths and UUIDs", () => {
  const id = "d46b1061-cabe-4e7b-b984-80b457d4d1a3";
  assertEquals(extractAiSyllabusCancelJobId(`/ai-syllabus-cancel/${id}`), id);
  assertEquals(
    extractAiSyllabusCancelJobId(`/functions/v1/ai-syllabus-cancel/${id}`),
    id,
  );
  assertEquals(extractAiSyllabusCancelJobId(`/ai-syllabus-cancel/${id}/`), id);
});

Deno.test("unknown cancel paths, missing IDs, invalid UUIDs, and extra segments are not found", async () => {
  const id = "d46b1061-cabe-4e7b-b984-80b457d4d1a3";
  const terminalJob = job({ id, status: "SUCCEEDED" });
  const { handler, calls } = harness(terminalJob);
  for (
    const pathname of [
      "/ai-syllabus-cancel/",
      "/ai-syllabus-cancel/not-a-uuid",
      `/ai-syllabus-cancel/${id}/extra`,
      `/other-function/${id}`,
      `/functions/v1/other-function/${id}`,
    ]
  ) {
    const result = await handler(
      new Request(`https://example.test${pathname}`, {
        method: "POST",
        headers: { authorization: "Bearer jwt" },
      }),
    );
    assertEquals(result.status, 404, pathname);
    assertEquals((await result.json()).error.code, "NOT_FOUND", pathname);
  }
  assertEquals(calls, []);
});

Deno.test("external gateway-prefixed path remains supported for terminal jobs", async () => {
  const id = "d46b1061-cabe-4e7b-b984-80b457d4d1a3";
  const { handler, calls } = harness(job({ id, status: "SUCCEEDED" }));
  const result = await handler(
    new Request(
      `https://example.test/functions/v1/ai-syllabus-cancel/${id}?source=gateway`,
      { method: "POST", headers: { authorization: "Bearer jwt" } },
    ),
  );
  assertEquals(result.status, 200);
  assertEquals((await result.json()).status, "SUCCEEDED");
  assertEquals(calls, []);
});
