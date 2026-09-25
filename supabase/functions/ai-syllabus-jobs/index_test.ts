import { assert, assertEquals, assertNotEquals } from "https://deno.land/std@0.224.0/assert/mod.ts";
import { createAiSyllabusJobsHandler } from "./index.ts";
import { JobStoreError, type AiJobStore, type AiJobRecord, type CreateAiJobInput } from "../_shared/job-finalizer.ts";

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
    new Request("https://example.test/functions/v1/ai-syllabus-jobs/job-1", {
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

Deno.test("routes hosted, local, and relative path formats to GET job", async () => {
  const paths = [
    "/functions/v1/ai-syllabus-jobs/job-1",
    "/ai-syllabus-jobs/job-1",
    "ai-syllabus-jobs/job-1",
  ];
  for (const path of paths) {
    const response = await createAiSyllabusJobsHandler(dependencies(record()))(
      new Request(`https://example.test/${path.replace(/^\//, "")}`, {
        method: "GET",
        headers: { authorization: "Bearer supabase-jwt" },
      }),
    );
    assertEquals(response.status, 200, path);
    assertEquals((await response.json()).jobId, "job-1", path);
  }
});

Deno.test("routes hosted and local process paths and rejects unknown paths", async () => {
  const paths = [
    "/functions/v1/ai-syllabus-jobs/job-1/process",
    "/ai-syllabus-jobs/job-1/process",
    "ai-syllabus-jobs/job-1/process",
  ];
  for (const path of paths) {
    const calls = { claim: 0 };
    const reserved = record();
    reserved.status = "RESERVED";
    const jobs = {
      async getJob() { return reserved; },
      async claimForProcessing() { calls.claim += 1; return { ...reserved, status: "PROCESSING" }; },
    } as unknown as AiJobStore;
    const response = await createAiSyllabusJobsHandler({
      authenticate: async () => ({ userId: "user-1" }),
      storage: {} as never,
      jobs,
      limits: { maxBytes: 50, maxPages: 1, maxFiles: 1 },
      schedule: async () => {},
    })(new Request(`https://example.test/${path.replace(/^\//, "")}`, {
      method: "POST",
      headers: { authorization: "Bearer supabase-jwt" },
    }));
    assertEquals(response.status, 202, path);
    assertEquals(calls.claim, 1, path);
  }

  const unknown = await createAiSyllabusJobsHandler(dependencies(record()))(new Request(
    "https://example.test/functions/v1/other-route/job-1",
    { method: "POST", headers: { authorization: "Bearer supabase-jwt" } },
  ));
  assertEquals(unknown.status, 404);
});

Deno.test("GET does not reveal another owner's job", async () => {
  const job = record("user-2");
  const response = await createAiSyllabusJobsHandler(dependencies(job))(
    new Request("https://example.test/functions/v1/ai-syllabus-jobs/job-1", {
      method: "GET",
      headers: { authorization: "Bearer supabase-jwt" },
    }),
  );

  assertEquals(response.status, 404);
});

Deno.test("GET root is rejected without reserving quota", async () => {
  let createCalls = 0;
  const jobs = {
    async createOrGet() {
      createCalls += 1;
      throw new Error("GET root must not create a job");
    },
    async getJob() { return null; },
  } as unknown as AiJobStore;
  const handler = createAiSyllabusJobsHandler({
    authenticate: async () => ({ userId: "user-1" }),
    storage: {} as never,
    jobs,
    limits: { maxBytes: 50, maxPages: 1, maxFiles: 1 },
    schedule: async () => { throw new Error("GET root must not schedule"); },
  });

  const response = await handler(new Request("https://example.test/functions/v1/ai-syllabus-jobs", {
    method: "GET",
    headers: { authorization: "Bearer supabase-jwt" },
  }));

  assertEquals(response.status, 405);
  assertEquals(response.headers.get("allow"), "POST");
  assertEquals(createCalls, 0);
});

Deno.test("GET process is rejected without claiming or starting processing", async () => {
  let claimCalls = 0;
  const jobs = {
    async getJob() { return record(); },
    async claimForProcessing() {
      claimCalls += 1;
      throw new Error("GET process must not claim a job");
    },
  } as unknown as AiJobStore;
  const handler = createAiSyllabusJobsHandler({
    authenticate: async () => ({ userId: "user-1" }),
    storage: {} as never,
    jobs,
    limits: { maxBytes: 50, maxPages: 1, maxFiles: 1 },
    schedule: async () => { throw new Error("GET process must not schedule"); },
  });

  const response = await handler(new Request("https://example.test/functions/v1/ai-syllabus-jobs/job-1/process", {
    method: "GET",
    headers: { authorization: "Bearer supabase-jwt" },
  }));

  assertEquals(response.status, 405);
  assertEquals(response.headers.get("allow"), "POST");
  assertEquals(claimCalls, 0);
});

Deno.test("POST fingerprint includes normalized MIME, SHA-256, and byte count", async () => {
  let captured: CreateAiJobInput | null = null;
  const reserved = record();
  reserved.status = "RESERVED";
  reserved.sourceObjectPath = null;
  reserved.sourceHash = null;
  reserved.sourceBytes = null;
  reserved.sourceMimeType = null;
  reserved.proposal = null;
  reserved.finishedAt = null;
  const jobs = {
    async createOrGet(input: CreateAiJobInput) {
      captured = input;
      return { jobId: reserved.id, status: reserved.status, reservationId: "reservation-1", quotaPeriod: "2026-09", quotaRemaining: 1, reused: false, requestPayload: input.requestPayload };
    },
    async getJob() { return reserved; },
  } as unknown as AiJobStore;
  const handler = createAiSyllabusJobsHandler({
    authenticate: async () => ({ userId: "user-1" }),
    storage: {} as never,
    jobs,
    limits: { maxBytes: 50, maxPages: 1, maxFiles: 1 },
    schedule: async () => {},
  });

  const response = await handler(new Request("https://example.test/functions/v1/ai-syllabus-jobs", {
    method: "POST",
    headers: { authorization: "Bearer supabase-jwt", "idempotency-key": "idem-1", "content-type": "application/json" },
    body: JSON.stringify({ source: { fileName: "edital.pdf", mimeType: "Application/PDF; charset=binary", sourceHash: "a".repeat(64), sourceBytes: 123 } }),
  }));

  assertEquals(response.status, 201);
  const requestPayload = (captured as CreateAiJobInput | null)?.requestPayload;
  assert(requestPayload);
  assertEquals(requestPayload.mimeType, "application/pdf");
  assertEquals(requestPayload.sourceHash, "a".repeat(64));
  assertEquals(requestPayload.sourceBytes, 123);
});

Deno.test("same idempotency key cannot reuse a bound job for a different source fingerprint", async () => {
  const bound = record();
  const calls = { bind: 0 };
  const jobs = {
    async createOrGet(input: CreateAiJobInput) {
      return { jobId: bound.id, status: bound.status, reservationId: "reservation-1", quotaPeriod: "2026-09", quotaRemaining: 1, reused: true, requestPayload: input.requestPayload };
    },
    async getJob() { return bound; },
    async bindSource() {
      calls.bind += 1;
      throw new JobStoreError("SOURCE_ALREADY_BOUND", 409);
    },
  } as unknown as AiJobStore;
  const handler = createAiSyllabusJobsHandler({
    authenticate: async () => ({ userId: "user-1" }),
    storage: {} as never,
    jobs,
    limits: { maxBytes: 50, maxPages: 1, maxFiles: 1 },
    schedule: async () => {},
  });

  const response = await handler(new Request("https://example.test/functions/v1/ai-syllabus-jobs", {
    method: "POST",
    headers: { authorization: "Bearer supabase-jwt", "idempotency-key": "idem-1", "content-type": "application/json" },
    body: JSON.stringify({ source: { fileName: "edital.pdf", mimeType: "application/pdf", sourceHash: "c".repeat(64), sourceBytes: 99 } }),
  }));

  assertEquals(response.status, 409);
  assertEquals((await response.json()).error.code, "IDEMPOTENCY_KEY_CONFLICT");
  assertEquals(calls.bind, 0);
});
