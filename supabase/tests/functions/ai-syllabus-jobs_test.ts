import { strict as assert } from "node:assert";
import {
  createAiSyllabusJobsHandler,
  sourcePathForJob,
  type AiSyllabusJobsDependencies,
} from "../../functions/ai-syllabus-jobs/index.ts";
import type { AiFeature } from "../../functions/_shared/contracts.ts";
import {
  JobStoreError,
  type AiJobRecord,
  type AiJobStore,
  type CreateAiJobInput,
  type CreatedAiJob,
} from "../../functions/_shared/job-finalizer.ts";
import {
  type StorageObject,
  type StorageSourceStore,
} from "../../functions/_shared/storage-source.ts";

const USER_A = "00000000-0000-0000-0000-0000000000a1";
const USER_B = "00000000-0000-0000-0000-0000000000b1";
const FEATURE = "SYLLABUS_GENERATION" as const;

function pdf(pages: number, bytes = 0): Uint8Array {
  const body = `%PDF-1.7 ${Array.from({ length: pages }, () => "/Type /Page").join(" ")} %%EOF`;
  const encoded = new TextEncoder().encode(body);
  if (bytes <= encoded.length) return encoded;
  const padded = new Uint8Array(bytes);
  padded.set(encoded);
  return padded;
}

class FakeStorage implements StorageSourceStore {
  readonly objects = new Map<string, StorageObject>();

  async getObject(_userId: string, path: string): Promise<StorageObject | null> {
    return this.objects.get(path) ?? null;
  }
}

class FakeJobStore implements AiJobStore {
  readonly records = new Map<string, AiJobRecord>();
  readonly createInputs: CreateAiJobInput[] = [];
  readonly releaseCalls: string[] = [];
  readonly events: string[] = [];
  private sequence = 0;

  async createOrGet(input: CreateAiJobInput): Promise<CreatedAiJob> {
    this.createInputs.push(input);
    const existing = [...this.records.values()].find((job) =>
      job.userId === input.userId && job.feature === input.feature && job.idempotencyKey === input.idempotencyKey
    );
    if (existing) {
      if (existing.requestFingerprint !== input.requestFingerprint) {
        throw new JobStoreError("IDEMPOTENCY_KEY_CONFLICT", 409);
      }
      return {
        jobId: existing.id,
        status: existing.status,
        reservationId: `reservation-${existing.id}`,
        quotaPeriod: "1970-01-01",
        quotaRemaining: 0,
        reused: true,
        requestPayload: existing.requestPayload,
      };
    }
    const id = `job-${++this.sequence}`;
    const record: AiJobRecord = {
      id,
      userId: input.userId,
      feature: input.feature,
      status: "RESERVED",
      idempotencyKey: input.idempotencyKey,
      requestFingerprint: input.requestFingerprint,
      requestPayload: input.requestPayload,
      sourceObjectPath: null,
      sourceHash: null,
      sourceBytes: null,
      sourcePages: null,
      sourceFileCount: null,
      sourceMimeType: null,
    };
    this.records.set(id, record);
    this.events.push(`create:${id}`);
    return {
      jobId: id,
      status: record.status,
      reservationId: `reservation-${id}`,
      quotaPeriod: "1970-01-01",
      quotaRemaining: 0,
      reused: false,
      requestPayload: record.requestPayload,
    };
  }

  async getJob(userId: string, jobId: string): Promise<AiJobRecord | null> {
    const job = this.records.get(jobId);
    return job?.userId === userId ? { ...job } : null;
  }

  async bindSource(userId: string, jobId: string, source: NonNullable<AiJobRecord["source"]>): Promise<AiJobRecord> {
    const job = this.records.get(jobId);
    if (!job || job.userId !== userId) throw new JobStoreError("AI_JOB_NOT_FOUND", 404);
    if (job.status !== "RESERVED") throw new JobStoreError("AI_JOB_NOT_RESERVABLE", 409);
    if (job.sourceObjectPath !== null) {
      if (job.sourceObjectPath !== source.path || job.sourceHash !== source.sourceHash) {
        throw new JobStoreError("SOURCE_ALREADY_BOUND", 409);
      }
      return { ...job };
    }
    this.events.push(`bind:${jobId}`);
    job.sourceObjectPath = source.path;
    job.sourceHash = source.sourceHash;
    job.sourceBytes = source.sourceBytes;
    job.sourcePages = source.sourcePages;
    job.sourceFileCount = source.sourceFileCount;
    job.sourceMimeType = source.mimeType;
    job.source = source;
    return { ...job };
  }

  async claimForProcessing(userId: string, jobId: string): Promise<AiJobRecord> {
    const job = this.records.get(jobId);
    if (!job || job.userId !== userId) throw new JobStoreError("AI_JOB_NOT_FOUND", 404);
    if (job.sourceObjectPath === null || job.sourceHash === null) throw new JobStoreError("SOURCE_NOT_BOUND", 409);
    if (job.status !== "RESERVED") throw new JobStoreError("AI_JOB_NOT_RESERVABLE", 409);
    this.events.push(`claim:${jobId}`);
    job.status = "PROCESSING";
    return { ...job };
  }

  async releaseReservation(userId: string, jobId: string): Promise<void> {
    const job = this.records.get(jobId);
    if (!job || job.userId !== userId) return;
    this.releaseCalls.push(jobId);
    this.events.push(`release:${jobId}`);
    job.status = "CANCELLED";
  }
}

function objectFor(userId: string, key: string, body = pdf(2)): StorageObject {
  return {
    bucketId: "ai-syllabus-sources",
    path: sourcePathForJob(userId, key),
    ownerId: userId,
    mimeType: "application/pdf",
    sizeBytes: body.byteLength,
    metadata: { mimetype: "application/pdf", size: body.byteLength },
    body,
  };
}

function dependencies(
  userId: string,
  storage: FakeStorage,
  jobs: FakeJobStore,
  overrides: Partial<AiSyllabusJobsDependencies> = {},
): AiSyllabusJobsDependencies {
  return {
    authenticate: async () => ({ userId }),
    policy: { getAccess: async () => ({ canUse: true }) },
    storage,
    jobs,
    limits: { maxBytes: 50_000, maxPages: 10, maxFiles: 1 },
    schedule: async (jobId: string) => {
      jobs.events.push(`schedule:${jobId}`);
    },
    ...overrides,
  };
}

async function postCreate(
  handler: (request: Request) => Promise<Response>,
  key: string,
  source: Record<string, unknown> = {},
  feature: AiFeature = FEATURE,
): Promise<Response> {
  return handler(new Request("https://example.test/ai-syllabus/jobs", {
    method: "POST",
    headers: { "content-type": "application/json", "Idempotency-Key": key, Authorization: "Bearer supabase-jwt" },
    body: JSON.stringify({ feature, source }),
  }));
}

Deno.test("creates a RESERVED job with a deterministic user-scoped upload path", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "create-job");
  const body = await response.json();

  assert.equal(response.status, 201);
  assert.equal(body.status, "RESERVED");
  assert.equal(body.sourceBound, false);
  assert.equal(body.uploadPath, sourcePathForJob(USER_A, "create-job"));
  assert.equal(jobs.records.size, 1);
  assert.equal(jobs.releaseCalls.length, 0);
});

Deno.test("same idempotency key and fingerprint reuses one job and reservation", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const first = await postCreate(handler, "same-key");
  const second = await postCreate(handler, "same-key");
  const firstBody = await first.json();
  const secondBody = await second.json();

  assert.equal(first.status, 201);
  assert.equal(second.status, 200);
  assert.equal(secondBody.jobId, firstBody.jobId);
  assert.equal(jobs.records.size, 1);
  assert.equal(jobs.createInputs.length, 2);
});

Deno.test("rejects conflicting reuse of an idempotency key", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  assert.equal((await postCreate(handler, "conflict", { fileName: "a.pdf" })).status, 201);
  const response = await postCreate(handler, "conflict", { fileName: "b.pdf" });
  const body = await response.json();

  assert.equal(response.status, 409);
  assert.equal(body.error.code, "IDEMPOTENCY_KEY_CONFLICT");
  assert.equal(jobs.records.size, 1);
});

Deno.test("isolates jobs and generated Storage paths between accounts", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handlerA = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));
  const handlerB = createAiSyllabusJobsHandler(dependencies(USER_B, storage, jobs));

  const responseA = await postCreate(handlerA, "same-account-key");
  const responseB = await postCreate(handlerB, "same-account-key");
  const bodyA = await responseA.json();
  const bodyB = await responseB.json();

  assert.notEqual(bodyA.jobId, bodyB.jobId);
  assert.match(bodyA.uploadPath, new RegExp(`^${USER_A}/`));
  assert.match(bodyB.uploadPath, new RegExp(`^${USER_B}/`));
  assert.equal((await jobs.getJob(USER_B, bodyA.jobId)), null);
});

Deno.test("returns a safe error and releases quota when an explicitly ready source is absent", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "missing-source", { ready: true });
  const body = await response.json();

  assert.equal(response.status, 404);
  assert.equal(body.error.code, "SOURCE_NOT_FOUND");
  assert.equal(jobs.releaseCalls.length, 1);
});

Deno.test("rejects a foreign or malformed Storage path before source download", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "foreign-source", {
    ready: true,
    objectPath: `${USER_B}/foreign.pdf`,
  });
  const body = await response.json();

  assert.equal(response.status, 400);
  assert.equal(body.error.code, "SOURCE_PATH_INVALID");
  assert.equal(storage.objects.size, 0);
  assert.equal(jobs.releaseCalls.length, 0);
});

Deno.test("rejects unsupported MIME metadata and does not bind the job", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));
  const source = objectFor(USER_A, "bad-mime");
  storage.objects.set(source.path, { ...source, mimeType: "text/plain", metadata: { mimetype: "text/plain" } });

  const response = await postCreate(handler, "bad-mime", { ready: true });
  const body = await response.json();

  assert.equal(response.status, 415);
  assert.equal(body.error.code, "SOURCE_MIME_UNSUPPORTED");
  assert.equal(jobs.records.get("job-1")?.sourceObjectPath, null);
  assert.equal(jobs.releaseCalls.length, 1);
});

Deno.test("enforces server-side byte, page, and file limits", async () => {
  const cases = [
    { key: "too-many-bytes", body: pdf(1, 101), limits: { maxBytes: 100, maxPages: 10, maxFiles: 1 }, code: "SOURCE_TOO_LARGE" },
    { key: "too-many-pages", body: pdf(3), limits: { maxBytes: 50_000, maxPages: 2, maxFiles: 1 }, code: "SOURCE_TOO_MANY_PAGES" },
    { key: "too-many-files", body: pdf(1), limits: { maxBytes: 50_000, maxPages: 10, maxFiles: 0 }, code: "SOURCE_TOO_MANY_FILES" },
  ] as const;

  for (const item of cases) {
    const storage = new FakeStorage();
    const jobs = new FakeJobStore();
    const source = objectFor(USER_A, item.key, item.body);
    storage.objects.set(source.path, source);
    const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs, { limits: item.limits }));
    const response = await postCreate(handler, item.key, { ready: true });
    const body = await response.json();

    assert.equal(response.status, 413);
    assert.equal(body.error.code, item.code);
    assert.equal(jobs.releaseCalls.length, 1);
  }
});

Deno.test("binds exact path, server SHA-256, counts, and metadata before processing", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const source = objectFor(USER_A, "bound-source", pdf(2));
  storage.objects.set(source.path, source);
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "bound-source", { ready: true });
  const body = await response.json();
  const job = jobs.records.get(body.jobId);

  assert.equal(response.status, 201);
  assert.equal(body.sourceBound, true);
  assert.equal(job?.sourceObjectPath, source.path);
  assert.equal(job?.sourceBytes, source.body.byteLength);
  assert.equal(job?.sourcePages, 2);
  assert.equal(job?.sourceFileCount, 1);
  assert.match(job?.sourceHash ?? "", /^[0-9a-f]{64}$/);
  assert.deepEqual(jobs.events.slice(-2), [`create:${body.jobId}`, `bind:${body.jobId}`]);
});

Deno.test("process is a short durable command and never calls a provider before committed binding", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));
  const createResponse = await postCreate(handler, "process-before-binding");
  const created = await createResponse.json();
  let providerCalls = 0;

  const processResponse = await handler(new Request(`https://example.test/ai-syllabus/jobs/${created.jobId}/process`, {
    method: "POST",
    headers: { Authorization: "Bearer supabase-jwt" },
  }));
  const processBody = await processResponse.json();

  assert.equal(processResponse.status, 409);
  assert.equal(processBody.error.code, "SOURCE_NOT_BOUND");
  assert.equal(providerCalls, 0);
  assert.equal(jobs.records.get(created.jobId)?.status, "RESERVED");

  const source = objectFor(USER_A, "process-after-binding");
  storage.objects.set(source.path, source);
  const boundResponse = await postCreate(handler, "process-after-binding", { ready: true });
  const bound = await boundResponse.json();
  const process = await handler(new Request(`https://example.test/ai-syllabus/jobs/${bound.jobId}/process`, {
    method: "POST",
    headers: { Authorization: "Bearer supabase-jwt" },
  }));
  const processResult = await process.json();

  assert.equal(process.status, 202);
  assert.equal(processResult.status, "PROCESSING");
  assert.equal(providerCalls, 0);
  assert.ok(jobs.events.indexOf(`bind:${bound.jobId}`) < jobs.events.indexOf(`claim:${bound.jobId}`));
  assert.ok(jobs.events.indexOf(`claim:${bound.jobId}`) < jobs.events.indexOf(`schedule:${bound.jobId}`));
});

Deno.test("requires the syllabus feature and a Supabase JWT", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const invalidFeature = await postCreate(handler, "invalid-feature", {}, "PLAN_GENERATION");
  assert.equal(invalidFeature.status, 400);

  const noAuth = await handler(new Request("https://example.test/ai-syllabus/jobs", { method: "POST" }));
  assert.equal(noAuth.status, 401);
});
