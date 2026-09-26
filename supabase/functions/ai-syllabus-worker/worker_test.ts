import { assert, assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { LeaseLostError, processSyllabusJob, type Lease, type SyllabusWorkerJob, type SyllabusWorkerStore } from "./index.ts";
import type { OpenAiProvider, ProviderResponse } from "../_shared/openai-provider.ts";
import type { TerminalAiTelemetry } from "../_shared/job-finalizer.ts";
import { OpenAiProviderError } from "../_shared/openai-provider.ts";

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

function job(overrides: Partial<SyllabusWorkerJob> = {}): SyllabusWorkerJob {
  return {
    id: "job-1",
    userId: "user-1",
    status: "PROCESSING",
    sourceObjectPath: "user-1/source.pdf",
    sourceHash: "a".repeat(64),
    sourceBytes: 10,
    sourcePages: 1,
    sourceFileCount: 1,
    openaiResponseId: "resp-1",
    providerExecutionStartedAt: "2026-09-24T12:00:00Z",
    leaseExpiresAt: "2026-09-24T12:05:00Z",
    leaseOwner: "worker-test",
    leaseToken: "lease-token-test",
    leaseGeneration: 1,
    processingDeadlineAt: "2026-09-24T13:00:00Z",
    retryCount: 0,
    ...overrides,
  };
}

function store(initial: SyllabusWorkerJob): SyllabusWorkerStore & { events: string[] } {
  const events: string[] = [];
  return {
    events,
    async claimNext() { return initial; },
    async assertLease() {},
    async recordProviderStartOutcome(id, _lease, outcome) { events.push(`provider-outcome:${id}:${outcome}`); },
    async persistResponseId(id, responseId) { events.push(`response:${id}:${responseId}`); },
    async finalizeSuccess(id) { events.push(`success:${id}`); },
    async finalizeFailure(id, _lease, code) { events.push(`failure:${id}:${code}`); },
    async markRetry(id) { events.push(`retry:${id}`); },
    async cleanupSource(id) { events.push(`cleanup:${id}`); },
    async reconcileProvider(id, _lease, recoverable) { events.push(`reconcile:${id}:${recoverable}`); },
    async captureUsage(id, _lease, usage) { events.push(`usage:${id}:${usage?.totalTokens ?? "null"}`); },
  };
}

function provider(response: ProviderResponse): OpenAiProvider {
  return {
    async start() { return response; },
    async retrieve() { return response; },
    async cancel() { return response; },
  };
}

Deno.test("persists response id before finalizing a valid result and captures usage", async () => {
  const jobs = store(job({ openaiResponseId: null, providerExecutionStartedAt: null }));
  await processSyllabusJob({
    jobs,
    provider: provider({ id: "resp-1", status: "completed", outputText: validOutput, usage: { inputTokens: 10, outputTokens: 20, totalTokens: 30 } }),
    source: async () => new Uint8Array([1, 2, 3]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(jobs.events[0], "response:job-1:resp-1");
  assertEquals(jobs.events[1], "usage:job-1:30");
  assertEquals(jobs.events[2], "cleanup:job-1");
  assertEquals(jobs.events[3], "success:job-1");
});

Deno.test("reconciles an expired lease before retrying a recoverable provider response", async () => {
  const jobs = store(job({ leaseExpiresAt: "2026-09-24T11:00:00Z", openaiResponseId: "resp-1" }));
  await processSyllabusJob({
    jobs,
    provider: provider({ id: "resp-1", status: "in_progress", outputText: null, usage: null }),
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assert(jobs.events.includes("reconcile:job-1:true"));
  assert(jobs.events.includes("retry:job-1"));
});

Deno.test("fails terminally on empty provider output without publishing a proposal", async () => {
  const jobs = store(job());
  await processSyllabusJob({
    jobs,
    provider: provider({ id: "resp-1", status: "completed", outputText: "", usage: null }),
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assert(jobs.events.some((event) => event === "failure:job-1:EMPTY_OUTPUT"));
  assert(!jobs.events.some((event) => event.startsWith("success:")));
});

Deno.test("emits structured safe telemetry only after terminal success or failure", async () => {
  const events: TerminalAiTelemetry[] = [];
  const success = store(job());
  await processSyllabusJob({
    jobs: success,
    provider: provider({ id: "resp-1", status: "completed", outputText: validOutput, usage: { inputTokens: 10, outputTokens: 20, totalTokens: 30 } }),
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
    telemetry: (event) => events.push(event),
  });
  assertEquals(events.length, 1);
  assertEquals(events[0].terminalStatus, "SUCCEEDED");
  assertEquals(events[0].feature, "SYLLABUS_GENERATION");
  assertEquals(events[0].jobId, "job-1");
  assertEquals(events[0].totalTokens, 30);
  assert(events[0].userPseudonym !== "user-1");
  assert(!JSON.stringify(events[0]).includes("Constituição"));
  assert(!JSON.stringify(events[0]).includes("source.pdf"));
  const failed = store(job());
  await processSyllabusJob({
    jobs: failed, provider: provider({ id: "resp-1", status: "completed", outputText: "", usage: null }),
    source: async () => new Uint8Array([1]), now: () => new Date("2026-09-24T12:01:00Z"),
    telemetry: (event) => events.push(event),
  });
  assertEquals(events[1].terminalStatus, "FAILED");
  assertEquals(events[1].totalTokens, null);
});

Deno.test("retains provider usage for terminal provider and proposal validation failures", async () => {
  const events: TerminalAiTelemetry[] = [];
  for (const output of [
    { status: "failed" as const, outputText: null },
    { status: "completed" as const, outputText: "invalid proposal" },
  ]) {
    const jobs = store(job());
    await processSyllabusJob({
      jobs,
      provider: provider({ id: "resp-1", ...output, usage: { inputTokens: 11, outputTokens: 7, totalTokens: 18 } }),
      source: async () => new Uint8Array([1]),
      now: () => new Date("2026-09-24T12:01:00Z"),
      telemetry: (event) => events.push(event),
    });
  }
  assertEquals(events.length, 2);
  for (const event of events) {
    assertEquals(event.terminalStatus, "FAILED");
    assertEquals(event.inputTokens, 11);
    assertEquals(event.outputTokens, 7);
    assertEquals(event.totalTokens, 18);
  }
});

Deno.test("local NOT_SENT after mark-start uses one atomic terminal operation", async () => {
  const jobs = store(job({ openaiResponseId: null, providerExecutionStartedAt: null }));
  jobs.markProviderStarted = async () => { jobs.events.push("marked"); };
  (jobs as SyllabusWorkerStore & { finalizeNotSent: (id: string, lease: Lease, code: string) => Promise<void> }).finalizeNotSent = async (id, _lease, code) => { jobs.events.push(`not-sent:${id}:${code}`); };
  let starts = 0;
  await processSyllabusJob({
    jobs,
    provider: { ...provider({ id: "unused", status: "failed", outputText: null, usage: null }),
      async start() { starts++; throw new OpenAiProviderError("OPENAI_API_KEY_MISSING", "NOT_SENT"); } },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(starts, 1);
  assertEquals(jobs.events.filter((event) => event.startsWith("not-sent:")), ["not-sent:job-1:OPENAI_API_KEY_MISSING"]);
  assert(!jobs.events.some((event) => event.startsWith("failure:") || event.startsWith("retry:") || event.startsWith("reconcile:")));
});

Deno.test("known response ID is recovered after lease-bound persistence fails", async () => {
  const jobs = store(job({ openaiResponseId: null, providerExecutionStartedAt: null }));
  let starts = 0;
  jobs.markProviderStarted = async () => {};
  jobs.persistResponseId = async () => { jobs.events.push("persist-failed"); throw new LeaseLostError(); };
  (jobs as SyllabusWorkerStore & { recoverResponseId: (id: string, responseId: string) => Promise<void> }).recoverResponseId = async (id, responseId) => { jobs.events.push(`recover:${id}:${responseId}`); };
  await processSyllabusJob({
    jobs,
    provider: { ...provider({ id: "resp-recovered", status: "in_progress", outputText: null, usage: null }),
      async start() { starts++; return { id: "resp-recovered", status: "in_progress", outputText: null, usage: null }; } },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(starts, 1);
  assertEquals(jobs.events.slice(0, 2), ["persist-failed", "recover:job-1:resp-recovered"]);
  assert(!jobs.events.some((event) => event.startsWith("retry:") || event.startsWith("failure:")));
});

Deno.test("persists provider delivery classes without retrying or releasing quarantine", async () => {
  for (const outcome of ["PROVIDER_REJECTED", "TRANSPORT_AMBIGUOUS", "RESPONSE_AMBIGUOUS"] as const) {
    const jobs = store(job({ openaiResponseId: null, providerExecutionStartedAt: null }));
    jobs.markProviderStarted = async () => {};
    const outcomes: string[] = [];
    (jobs as SyllabusWorkerStore & { recordProviderStartOutcome: (id: string, lease: Lease, outcome: string) => Promise<void> }).recordProviderStartOutcome = async (_id, _lease, value) => { outcomes.push(value); };
    let starts = 0;
    await processSyllabusJob({
      jobs,
      provider: { ...provider({ id: "unused", status: "failed", outputText: null, usage: null }),
        async start() { starts++; throw new OpenAiProviderError("OPENAI_PROVIDER_ERROR", outcome); } },
      source: async () => new Uint8Array([1]),
      now: () => new Date("2026-09-24T12:01:00Z"),
    });
    assertEquals(starts, 1, `${outcome} cannot replay generation`);
    assertEquals(outcomes, [outcome], `${outcome} is persisted exactly once`);
    assert(!jobs.events.some((event) => event.startsWith("retry:") || event.startsWith("failure:")));
  }
});

Deno.test("cancellation winning mark-start prevents provider call and retry", async () => {
  const jobs = store(job({ openaiResponseId: null, providerExecutionStartedAt: null, providerStartOutcome: "NOT_STARTED", providerQuarantinedAt: null }));
  jobs.markProviderStarted = async () => { throw new Error("AI_JOB_CANCELLATION_PENDING"); };
  let starts = 0;
  await processSyllabusJob({
    jobs,
    provider: { ...provider({ id: "unused", status: "failed", outputText: null, usage: null }),
      async start() { starts++; throw new Error("unexpected start"); } },
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
  });
  assertEquals(starts, 0);
  assertEquals(jobs.events, []);
});

Deno.test("measures first-attempt telemetry duration from provider start", async () => {
  const jobs = store(job({ openaiResponseId: null, providerExecutionStartedAt: null }));
  const events: TerminalAiTelemetry[] = [];
  jobs.markProviderStarted = async () => {};
  const moments = [0, 1, 2, 3, 6, 7].map((seconds) => new Date(`2026-09-24T12:00:0${seconds}Z`));
  let tick = 0;
  await processSyllabusJob({
    jobs,
    provider: provider({ id: "resp-1", status: "completed", outputText: validOutput, usage: null }),
    source: async () => new Uint8Array([1]),
    now: () => moments[Math.min(tick++, moments.length - 1)],
    telemetry: (event) => events.push(event),
  });
  assertEquals(events.length, 1);
  assertEquals(events[0].durationMs, 4000);
});
