import { assert, assertEquals, assertNotEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { createAiSyllabusJobsHandler } from "./index.ts";
import type { AiJobStore, AiJobRecord } from "../_shared/job-finalizer.ts";

const validProposal = {
  schemaVersion: 1,
  promptVersion: "syllabus-v1",
  modelVersion: "gpt-6-luna",
  documentTitle: "Edital",
  subjects: [{
    name: "Direito",
    position: 0,
    suggestedPriority: "NORMAL",
    topics: [{ name: "Constituição", position: 0, children: [], sourcePages: [1] }],
    sourcePages: [1],
  }],
  warnings: [],
  ambiguities: [],
};

function record(userId = "user-1"): AiJobRecord {
  return {
    id: "job-1",
    userId,
    feature: "SYLLABUS_GENERATION",
    status: "SUCCEEDED",
    idempotencyKey: "idem-1",
    requestFingerprint: "a".repeat(64),
    requestPayload: { feature: "SYLLABUS_GENERATION" },
    sourceObjectPath: `${userId}/source.pdf`,
    sourceHash: "b".repeat(64),
    sourceBytes: 10,
    sourcePages: 1,
    sourceFileCount: 1,
    sourceMimeType: "application/pdf",
    proposal: validProposal,
    warnings: [],
    schemaVersion: 1,
    promptVersion: "syllabus-v1",
    modelVersion: "gpt-6-luna",
    errorCode: null,
    errorMessage: null,
    createdAt: "2026-09-24T12:00:00Z",
    updatedAt: "2026-09-24T12:01:00Z",
    finishedAt: "2026-09-24T12:01:00Z",
    providerExecutionStartedAt: "2026-09-24T12:00:10Z",
  } as AiJobRecord;
}

function dependencies(job: AiJobRecord): Parameters<typeof createAiSyllabusJobsHandler>[0] {
  const jobs = {
    async getJob(userId: string, jobId: string) {
      return userId === job.userId && jobId === job.id ? job : null;
    },
  } as unknown as AiJobStore;
  return {
    authenticate: async () => ({ userId: "user-1" }),
    storage: {} as never,
    jobs,
    limits: { maxBytes: 50, maxPages: 1, maxFiles: 1 },
    schedule: async () => {},
  };
}

Deno.test("GET returns only the authenticated owner's AiJob contract", async () => {
  const response = await createAiSyllabusJobsHandler(dependencies(record()))(
    new Request("https://example.test/functions/v1/ai-syllabus/jobs/job-1", {
      method: "GET",
      headers: { authorization: "Bearer supabase-jwt" },
    }),
  );

  assertEquals(response.status, 200);
  const body = await response.json();
  assertEquals(body.jobId, "job-1");
  assertEquals(body.status, "SUCCEEDED");
  assertEquals(body.proposal.documentTitle, "Edital");
  assertEquals(Object.keys(body).sort(), [
    "createdAt", "errorCode", "errorMessage", "feature", "finishedAt", "jobId",
    "modelVersion", "promptVersion", "proposal", "providerExecutionStartedAt", "schemaVersion",
    "status", "updatedAt", "warnings",
  ].sort());
  assertNotEquals(body.userId, "user-1");
  assert(body.idempotencyKey === undefined);
});

Deno.test("GET does not reveal another owner's job", async () => {
  const job = record("user-2");
  const response = await createAiSyllabusJobsHandler(dependencies(job))(
    new Request("https://example.test/functions/v1/ai-syllabus/jobs/job-1", {
      method: "GET",
      headers: { authorization: "Bearer supabase-jwt" },
    }),
  );

  assertEquals(response.status, 404);
});
