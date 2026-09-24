import { assert, assertEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { processSyllabusJob, type SyllabusWorkerJob, type SyllabusWorkerStore } from "./index.ts";
import type { OpenAiProvider, ProviderResponse } from "../_shared/openai-provider.ts";

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
    retryCount: 0,
    ...overrides,
  };
}

function store(initial: SyllabusWorkerJob): SyllabusWorkerStore & { events: string[] } {
  const events: string[] = [];
  return {
    events,
    async persistResponseId(id, responseId) { events.push(`response:${id}:${responseId}`); },
    async finalizeSuccess(id) { events.push(`success:${id}`); },
    async finalizeFailure(id, code) { events.push(`failure:${id}:${code}`); },
    async markRetry(id) { events.push(`retry:${id}`); },
    async cleanupSource(id) { events.push(`cleanup:${id}`); },
    async reconcileProvider(id, recoverable) { events.push(`reconcile:${id}:${recoverable}`); },
    async claimNext() { return initial; },
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
  const usage: unknown[] = [];
  await processSyllabusJob({
    jobs,
    provider: provider({ id: "resp-1", status: "completed", outputText: validOutput, usage: { inputTokens: 10, outputTokens: 20, totalTokens: 30 } }),
    source: async () => new Uint8Array([1, 2, 3]),
    now: () => new Date("2026-09-24T12:01:00Z"),
    captureUsage: async (_jobId, value) => { usage.push(value); },
  });
  assertEquals(jobs.events[0], "response:job-1:resp-1");
  assertEquals(jobs.events[1], "success:job-1");
  assertEquals(usage.length, 1);
});

Deno.test("reconciles an expired lease before retrying a recoverable provider response", async () => {
  const jobs = store(job({ leaseExpiresAt: "2026-09-24T11:00:00Z", openaiResponseId: "resp-1" }));
  await processSyllabusJob({
    jobs,
    provider: provider({ id: "resp-1", status: "in_progress", outputText: null, usage: null }),
    source: async () => new Uint8Array([1]),
    now: () => new Date("2026-09-24T12:01:00Z"),
    captureUsage: async () => {},
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
    captureUsage: async () => {},
  });
  assert(jobs.events.some((event) => event === "failure:job-1:EMPTY_OUTPUT"));
  assert(!jobs.events.some((event) => event.startsWith("success:")));
});
