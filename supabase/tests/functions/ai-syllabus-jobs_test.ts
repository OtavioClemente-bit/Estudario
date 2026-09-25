import { strict as assert } from "node:assert";
import { PDFDocument } from "npm:pdf-lib@1.17.1";
import {
  createAiSyllabusJobsHandler,
  sourcePathForJob,
  type AiSyllabusJobsDependencies,
} from "../../functions/ai-syllabus-jobs/index.ts";
import type { AiFeature } from "../../functions/_shared/contracts.ts";
import {
  JobStoreError,
  SupabaseAiJobStore,
  type AiJobRecord,
  type AiJobStore,
  type CreateAiJobInput,
  type CreatedAiJob,
} from "../../functions/_shared/job-finalizer.ts";
import {
  SupabaseStorageSourceStore,
  StorageSourceError,
  type StorageObject,
  type StorageSourceStore,
} from "../../functions/_shared/storage-source.ts";

const USER_A = "00000000-0000-0000-0000-0000000000a1";
const USER_B = "00000000-0000-0000-0000-0000000000b1";
const FEATURE = "SYLLABUS_GENERATION" as const;

function pdf(pages: number, bytes = 0): Uint8Array {
  const objects = [
    `1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n`,
    `2 0 obj\n<< /Type /Pages /Kids [${Array.from({ length: pages }, (_, index) => `${index + 3} 0 R`).join(" ")}] /Count ${pages} >>\nendobj\n`,
    ...Array.from({ length: pages }, (_, index) =>
      `${index + 3} 0 obj\n<< /Type /Page /Parent 2 0 R >>\nendobj\n`
    ),
  ];
  const header = "%PDF-1.7\n";
  const render = (padding: number): Uint8Array => {
    const offsets: number[] = [];
    let cursor = new TextEncoder().encode(header).byteLength;
    for (const object of objects) {
      offsets.push(cursor);
      cursor += new TextEncoder().encode(object).byteLength;
    }
    const xref = `xref\n0 ${objects.length + 1}\n0000000000 65535 f \n${offsets.map((offset) => `${offset.toString().padStart(10, "0")} 00000 n \n`).join("")}`;
    const xrefOffset = cursor + padding;
    const trailer = `trailer\n<< /Size ${objects.length + 1} /Root 1 0 R >>\nstartxref\n${xrefOffset}\n%%EOF`;
    return new TextEncoder().encode(`${header}${objects.join("")}${" ".repeat(padding)}${xref}${trailer}`);
  };
  const base = render(0);
  return bytes > base.byteLength ? render(bytes - base.byteLength) : base;
}

function fakePageInsideStream(): Uint8Array {
  return new TextEncoder().encode(
    "%PDF-1.7\n1 0 obj\n<< /Length 12 >>\nstream\n/Type /Page\nendstream\nendobj\n%%EOF",
  );
}

function fakeDeclaredPagesOnly(): Uint8Array {
  return new TextEncoder().encode("%PDF-1.7\n/Type /Pages /Count 2\nnot a PDF structure\n%%EOF");
}

function pageObjectWithoutPageTree(): Uint8Array {
  return new TextEncoder().encode(
    "%PDF-1.7\n1 0 obj\n<< /Type /Page >>\nendobj\n%%EOF",
  );
}

class FakeStorage implements StorageSourceStore {
  readonly objects = new Map<string, StorageObject>();
  transientError: StorageSourceError | null = null;

  async getObject(_userId: string, path: string): Promise<StorageObject | null> {
    if (this.transientError) throw this.transientError;
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
  return handler(new Request("https://example.test/functions/v1/ai-syllabus-jobs", {
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

  assert.equal((await postCreate(handler, "conflict", { fileName: "a.pdf", mimeType: "application/pdf", sourceHash: "a".repeat(64), sourceBytes: 10 })).status, 201);
  const response = await postCreate(handler, "conflict", { fileName: "b.pdf", mimeType: "application/pdf", sourceHash: "b".repeat(64), sourceBytes: 11 });
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

Deno.test("preserves RESERVED and allows an idempotent retry after a transient Storage lookup failure", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const source = objectFor(USER_A, "transient-source");
  storage.objects.set(source.path, source);
  storage.transientError = new StorageSourceError("SOURCE_LOOKUP_UNAVAILABLE", 503);
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const first = await postCreate(handler, "transient-source", { ready: true });
  const firstBody = await first.json();

  assert.equal(first.status, 503);
  assert.equal(firstBody.error.code, "SOURCE_LOOKUP_UNAVAILABLE");
  assert.equal(jobs.releaseCalls.length, 0);
  assert.equal(jobs.records.get("job-1")?.status, "RESERVED");

  storage.transientError = null;
  const retry = await postCreate(handler, "transient-source", { ready: true });
  const retryBody = await retry.json();

  assert.equal(retry.status, 200);
  assert.equal(retryBody.jobId, "job-1");
  assert.equal(retryBody.sourceBound, true);
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

Deno.test("rejects Storage metadata without a non-null owner", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const source = objectFor(USER_A, "missing-owner");
  storage.objects.set(source.path, { ...source, ownerId: null });
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "missing-owner", { ready: true });
  const body = await response.json();

  assert.equal(response.status, 422);
  assert.equal(body.error.code, "SOURCE_METADATA_INVALID");
  assert.equal(jobs.releaseCalls.length, 1);
});

Deno.test("rejects Storage metadata owned by another account", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const source = objectFor(USER_A, "foreign-owner");
  storage.objects.set(source.path, { ...source, ownerId: USER_B });
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "foreign-owner", { ready: true });
  const body = await response.json();

  assert.equal(response.status, 422);
  assert.equal(body.error.code, "SOURCE_METADATA_INVALID");
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

Deno.test("accepts a valid object-stream PDF and counts its page tree", async () => {
  const document = await PDFDocument.create();
  document.addPage();
  document.addPage();
  const modernPdf = new Uint8Array(await document.save({ useObjectStreams: true }));
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const source = objectFor(USER_A, "modern-object-stream", modernPdf);
  storage.objects.set(source.path, source);
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "modern-object-stream", { ready: true });
  const body = await response.json();

  assert.equal(response.status, 201);
  assert.equal(body.sourceBound, true);
  assert.equal(jobs.records.get(body.jobId)?.sourcePages, 2);
});

Deno.test("rejects malformed PDF text that only declares a page count", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const source = objectFor(USER_A, "fake-declared-pages", fakeDeclaredPagesOnly());
  storage.objects.set(source.path, source);
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "fake-declared-pages", { ready: true });
  const body = await response.json();

  assert.equal(response.status, 422);
  assert.equal(body.error.code, "SOURCE_PAGE_COUNT_UNAVAILABLE");
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
  assert.equal(job?.sourceHash, "389cf62a0642737aa031a176ff6204943ae4961740d27d37b28e350ad5a21066");
  assert.deepEqual(job?.source?.metadata, {
    bucket: "ai-syllabus-sources",
    path: source.path,
    mimeType: "application/pdf",
    sizeBytes: source.body.byteLength,
    storageMetadata: source.metadata,
  });
  assert.deepEqual(jobs.events.slice(-2), [`create:${body.jobId}`, `bind:${body.jobId}`]);
});

Deno.test("does not count a page-looking token inside a PDF stream", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const source = objectFor(USER_A, "fake-page", fakePageInsideStream());
  storage.objects.set(source.path, source);
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "fake-page", { ready: true });
  const body = await response.json();

  assert.equal(response.status, 422);
  assert.equal(body.error.code, "SOURCE_PAGE_COUNT_UNAVAILABLE");
  assert.equal(jobs.releaseCalls.length, 1);
});

Deno.test("rejects a page object without trailer, xref, and page tree", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const source = objectFor(USER_A, "adversarial-pdf", pageObjectWithoutPageTree());
  storage.objects.set(source.path, source);
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await postCreate(handler, "adversarial-pdf", { ready: true });
  const body = await response.json();

  assert.equal(response.status, 422);
  assert.equal(body.error.code, "SOURCE_PAGE_COUNT_UNAVAILABLE");
  assert.equal(jobs.releaseCalls.length, 1);
});

Deno.test("process is a short durable command and never calls a provider before committed binding", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const providerSpy = {
    calls: 0,
    start: async (jobId: string) => {
      const job = await jobs.getJob(USER_A, jobId);
      if (!job?.sourceHash || !job.sourceObjectPath) throw new Error("provider called before source binding");
      providerSpy.calls += 1;
      jobs.events.push(`provider:${jobId}`);
    },
  };
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs, {
    schedule: providerSpy.start,
  }));
  const createResponse = await postCreate(handler, "process-before-binding");
  const created = await createResponse.json();

  const processResponse = await handler(new Request(`https://example.test/functions/v1/ai-syllabus-jobs/${created.jobId}/process`, {
    method: "POST",
    headers: { Authorization: "Bearer supabase-jwt" },
  }));
  const processBody = await processResponse.json();

  assert.equal(processResponse.status, 409);
  assert.equal(processBody.error.code, "SOURCE_NOT_BOUND");
  assert.equal(providerSpy.calls, 0);
  assert.equal(jobs.records.get(created.jobId)?.status, "RESERVED");

  const source = objectFor(USER_A, "process-after-binding");
  storage.objects.set(source.path, source);
  const boundResponse = await postCreate(handler, "process-after-binding", { ready: true });
  const bound = await boundResponse.json();
  const process = await handler(new Request(`https://example.test/functions/v1/ai-syllabus-jobs/${bound.jobId}/process`, {
    method: "POST",
    headers: { Authorization: "Bearer supabase-jwt" },
  }));
  const processResult = await process.json();

  assert.equal(process.status, 202);
  assert.equal(processResult.status, "PROCESSING");
  assert.equal(providerSpy.calls, 1);
  assert.ok(jobs.events.indexOf(`bind:${bound.jobId}`) < jobs.events.indexOf(`claim:${bound.jobId}`));
  assert.ok(jobs.events.indexOf(`claim:${bound.jobId}`) < jobs.events.indexOf(`provider:${bound.jobId}`));
});

Deno.test("GET root returns 405 without reserving quota", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const response = await handler(new Request("https://example.test/functions/v1/ai-syllabus-jobs", {
    method: "GET",
    headers: { Authorization: "Bearer supabase-jwt" },
  }));

  assert.equal(response.status, 405);
  assert.equal(response.headers.get("allow"), "POST");
  assert.equal(jobs.createInputs.length, 0);
  assert.equal(jobs.records.size, 0);
});

Deno.test("GET process returns 405 without claiming or scheduling a job", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));
  const created = await postCreate(handler, "get-process");
  const body = await created.json();

  const response = await handler(new Request(`https://example.test/functions/v1/ai-syllabus-jobs/${body.jobId}/process`, {
    method: "GET",
    headers: { Authorization: "Bearer supabase-jwt" },
  }));

  assert.equal(response.status, 405);
  assert.equal(response.headers.get("allow"), "POST");
  assert.deepEqual(jobs.events, [`create:${body.jobId}`]);
  assert.equal(jobs.records.get(body.jobId)?.status, "RESERVED");
});

Deno.test("allows an idempotent retry to bind the existing job even when quota is no longer available", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const first = await postCreate(handler, "retry-after-upload");
  assert.equal(first.status, 201);
  const source = objectFor(USER_A, "retry-after-upload");
  storage.objects.set(source.path, source);
  const retry = await postCreate(handler, "retry-after-upload", { ready: true });
  const body = await retry.json();

  assert.equal(retry.status, 200);
  assert.equal(body.sourceBound, true);
});

Deno.test("uses the Storage metadata RPC and Storage API, never the storage schema REST endpoint", async () => {
  const body = pdf(1);
  const path = sourcePathForJob(USER_A, "storage-api");
  const requests: Request[] = [];
  const store = new SupabaseStorageSourceStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async (input, init) => {
      const request = new Request(input, init);
      requests.push(request);
      if (request.url.includes("/rpc/get_ai_syllabus_source_metadata")) {
        return new Response(JSON.stringify([{
          bucket_id: "ai-syllabus-sources",
          name: path,
          owner: USER_A,
          metadata: { mimetype: "application/pdf", size: body.byteLength },
        }]), { status: 200, headers: { "content-type": "application/json" } });
      }
      return new Response(body.buffer as ArrayBuffer, { status: 200, headers: { "content-type": "application/pdf" } });
    },
  }, 50_000);

  const object = await store.getObject(USER_A, path);

  assert.equal(object?.ownerId, USER_A);
  assert.equal(requests[0].url.includes("/rest/v1/storage.objects"), false);
  assert.equal(requests[0].url.includes("/rest/v1/rpc/get_ai_syllabus_source_metadata"), true);
  assert.equal(requests[0].headers.get("authorization"), "Bearer service-role-key");
  assert.equal(requests[0].headers.get("apikey"), "publishable-key");
  assert.deepEqual(await requests[0].clone().json(), { p_user_id: USER_A, p_path: path });
  assert.equal(new URL(requests[1].url).pathname, `/storage/v1/object/ai-syllabus-sources/${path}`);
  assert.equal(requests[1].headers.get("authorization"), "Bearer supabase-jwt");
});

Deno.test("uses a Supabase secret key only as apikey for metadata while keeping the user JWT for download", async () => {
  const body = pdf(1);
  const path = sourcePathForJob(USER_A, "storage-secret-key");
  const requests: Request[] = [];
  const store = new SupabaseStorageSourceStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "sb_secret_backend-key",
    fetcher: async (input, init) => {
      const request = new Request(input, init);
      requests.push(request);
      if (request.url.includes("/rpc/get_ai_syllabus_source_metadata")) {
        return new Response(JSON.stringify([{
          bucket_id: "ai-syllabus-sources",
          name: path,
          owner: USER_A,
          metadata: { mimetype: "application/pdf", size: body.byteLength },
        }]), { status: 200, headers: { "content-type": "application/json" } });
      }
      return new Response(body.buffer as ArrayBuffer, { status: 200, headers: { "content-type": "application/pdf" } });
    },
  }, 50_000);

  await store.getObject(USER_A, path);

  assert.equal(requests[0].headers.get("apikey"), "sb_secret_backend-key");
  assert.equal(requests[0].headers.get("authorization"), null);
  assert.equal(requests[1].headers.get("apikey"), "publishable-key");
  assert.equal(requests[1].headers.get("authorization"), "Bearer supabase-jwt");
});

Deno.test("uses an explicitly configured legacy service-role JWT for backend-only metadata RPCs", async () => {
  const body = pdf(1);
  const path = sourcePathForJob(USER_A, "storage-legacy-jwt");
  const requests: Request[] = [];
  const store = new SupabaseStorageSourceStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "sb_secret_backend-key",
    serviceRoleJwt: "legacy-service-role-jwt",
    fetcher: async (input, init) => {
      const request = new Request(input, init);
      requests.push(request);
      if (request.url.includes("/rpc/get_ai_syllabus_source_metadata")) {
        return new Response(JSON.stringify([{
          bucket_id: "ai-syllabus-sources",
          name: path,
          owner: USER_A,
          metadata: { mimetype: "application/pdf", size: body.byteLength },
        }]), { status: 200 });
      }
      return new Response(body.buffer as ArrayBuffer, { status: 200 });
    },
  }, 50_000);

  await store.getObject(USER_A, path);
  assert.equal(requests[0].headers.get("apikey"), "legacy-service-role-jwt");
  assert.equal(requests[0].headers.get("authorization"), "Bearer legacy-service-role-jwt");
  assert.equal(requests[1].headers.get("authorization"), "Bearer supabase-jwt");
});

Deno.test("maps the source metadata RPC not-found error to SOURCE_NOT_FOUND", async () => {
  const store = new SupabaseStorageSourceStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async () => new Response(JSON.stringify({ code: "P0001", message: "SOURCE_NOT_FOUND" }), { status: 400 }),
  }, 50_000);

  await assert.rejects(
    store.getObject(USER_A, sourcePathForJob(USER_A, "rpc-missing")),
    (error: unknown) => error instanceof StorageSourceError && error.code === "SOURCE_NOT_FOUND" && error.status === 404,
  );
});

Deno.test("does not mislabel an unexpected metadata RPC failure as SOURCE_METADATA_INVALID", async () => {
  const store = new SupabaseStorageSourceStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async () => new Response(JSON.stringify({ message: "upstream unavailable" }), { status: 500 }),
  }, 50_000);

  await assert.rejects(
    store.getObject(USER_A, sourcePathForJob(USER_A, "rpc-error")),
    (error: unknown) => error instanceof StorageSourceError && error.code === "SOURCE_LOOKUP_UNAVAILABLE" && error.status === 503,
  );
});

Deno.test("maps Storage download 5xx and timeout failures to SOURCE_LOOKUP_UNAVAILABLE", async () => {
  const path = sourcePathForJob(USER_A, "download-error");
  const metadataResponse = () => new Response(JSON.stringify([{
    bucket_id: "ai-syllabus-sources",
    name: path,
    owner: USER_A,
    metadata: { mimetype: "application/pdf" },
  }]), { status: 200, headers: { "content-type": "application/json" } });

  const serverFailureStore = new SupabaseStorageSourceStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async (input) => input.toString().includes("get_ai_syllabus_source_metadata")
      ? metadataResponse()
      : new Response("storage unavailable", { status: 502 }),
  }, 50_000);
  await assert.rejects(
    serverFailureStore.getObject(USER_A, path),
    (error: unknown) => error instanceof StorageSourceError && error.code === "SOURCE_LOOKUP_UNAVAILABLE" && error.status === 503,
  );

  const timeoutStore = new SupabaseStorageSourceStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async (input) => {
      if (input.toString().includes("get_ai_syllabus_source_metadata")) return metadataResponse();
      throw new TypeError("fetch failed");
    },
  }, 50_000);
  await assert.rejects(
    timeoutStore.getObject(USER_A, path),
    (error: unknown) => error instanceof StorageSourceError && error.code === "SOURCE_LOOKUP_UNAVAILABLE" && error.status === 503,
  );
});

Deno.test("persists source binding through the owner-checked RPC, not a client UPDATE", async () => {
  const requests: Request[] = [];
  const store = new SupabaseAiJobStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async (input, init) => {
      const request = new Request(input, init);
      requests.push(request);
      return new Response(JSON.stringify({
        id: "00000000-0000-0000-0000-0000000000c1",
        user_id: USER_A,
        feature: FEATURE,
        status: "RESERVED",
        idempotency_key: "key",
        request_fingerprint: "fingerprint",
        request_payload: {},
        source_object_path: sourcePathForJob(USER_A, "key"),
        source_hash: "a".repeat(64),
        source_bytes: 123,
        source_pages: 2,
        source_file_count: 1,
        source_mime_type: "application/pdf",
        source_metadata: { bucket: "ai-syllabus-sources" },
        warnings: [],
        created_at: "2026-09-24T12:00:00Z",
        updated_at: "2026-09-24T12:00:00Z",
      }), { status: 200, headers: { "content-type": "application/json" } });
    },
  });

  await store.bindSource(USER_A, "00000000-0000-0000-0000-0000000000c1", {
    path: sourcePathForJob(USER_A, "key"),
    mimeType: "application/pdf",
    sourceHash: "a".repeat(64),
    sourceBytes: 123,
    sourcePages: 2,
    sourceFileCount: 1,
    metadata: { bucket: "ai-syllabus-sources" },
  });

  assert.equal(requests.length, 1);
  assert.equal(requests[0].method, "POST");
  assert.equal(new URL(requests[0].url).pathname, "/rest/v1/rpc/bind_ai_job_source");
  const body = await requests[0].json();
  assert.equal(body.p_job_id, "00000000-0000-0000-0000-0000000000c1");
  assert.equal(body.p_user_id, USER_A);
  assert.equal(requests[0].headers.get("apikey"), "service-role-key");
  assert.equal(requests[0].headers.get("authorization"), "Bearer service-role-key");
});

Deno.test("claims processing with backend credentials and still rejects another owner's job", async () => {
  const requests: Request[] = [];
  const store = new SupabaseAiJobStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "user-a-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async (input, init) => {
      const request = new Request(input, init);
      requests.push(request);
      return new Response(JSON.stringify({
        id: "00000000-0000-0000-0000-0000000000c1",
        user_id: USER_B,
        feature: FEATURE,
        status: "PROCESSING",
        idempotency_key: "key",
        request_fingerprint: "fingerprint",
        request_payload: {},
        warnings: [],
        created_at: "2026-09-24T12:00:00Z",
        updated_at: "2026-09-24T12:00:00Z",
      }), { status: 200, headers: { "content-type": "application/json" } });
    },
  });

  await assert.rejects(
    store.claimForProcessing(USER_A, "00000000-0000-0000-0000-0000000000c1"),
    (error: unknown) => error instanceof JobStoreError && error.code === "AI_JOB_FORBIDDEN" && error.status === 403,
  );
  assert.equal(requests.length, 1);
  assert.equal(new URL(requests[0].url).pathname, "/rest/v1/rpc/claim_ai_job");
  assert.equal(requests[0].headers.get("apikey"), "service-role-key");
  assert.equal(requests[0].headers.get("authorization"), "Bearer service-role-key");
  assert.notEqual(requests[0].headers.get("authorization"), "Bearer user-a-jwt");
});

Deno.test("normalizes SOURCE_NOT_FOUND from the real binding RPC path to HTTP 404", async () => {
  const store = new SupabaseAiJobStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async () => new Response(JSON.stringify({ code: "P0001", message: "SOURCE_NOT_FOUND" }), { status: 400 }),
  });

  await assert.rejects(
    store.bindSource(USER_A, "00000000-0000-0000-0000-0000000000c1", {
      path: sourcePathForJob(USER_A, "rpc-missing"),
      mimeType: "application/pdf",
      sourceHash: "a".repeat(64),
      sourceBytes: 123,
      sourcePages: 2,
      sourceFileCount: 1,
      metadata: { bucket: "ai-syllabus-sources" },
    }),
    (error: unknown) => error instanceof JobStoreError && error.code === "SOURCE_NOT_FOUND" && error.status === 404,
  );
});

Deno.test("requires the syllabus feature and a Supabase JWT", async () => {
  const storage = new FakeStorage();
  const jobs = new FakeJobStore();
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, storage, jobs));

  const invalidFeature = await postCreate(handler, "invalid-feature", {}, "PLAN_GENERATION");
  assert.equal(invalidFeature.status, 400);

  const noAuth = await handler(new Request("https://example.test/functions/v1/ai-syllabus-jobs", { method: "POST" }));
  assert.equal(noAuth.status, 401);
});

Deno.test("rate rejection has stable 429 body and nonnegative retry headers", async () => {
  const jobs = new FakeJobStore();
  jobs.createOrGet = async () => { throw new JobStoreError("AI_RATE_LIMIT_EXCEEDED", 429, 37); };
  const handler = createAiSyllabusJobsHandler(dependencies(USER_A, new FakeStorage(), jobs));
  const response = await postCreate(handler, "fourth");
  assert.equal(response.status, 429);
  assert.equal(response.headers.get("Retry-After"), "37");
  const body = await response.json();
  assert.equal(body.error.code, "AI_RATE_LIMIT_EXCEEDED");
  assert.equal(body.error.retryAfterSeconds, 37);
  assert.equal(JSON.stringify(body).includes(USER_A), false);
});

Deno.test("database rate error details map to stable retry metadata", async () => {
  const store = new SupabaseAiJobStore({
    supabaseUrl: "http://127.0.0.1:54321",
    publishableKey: "publishable-key",
    accessToken: "supabase-jwt",
    serviceRoleKey: "service-role-key",
    fetcher: async () => new Response(JSON.stringify({ code: "P0001", message: "AI_RATE_LIMIT_EXCEEDED", details: "retry_after_seconds=19" }), { status: 400 }),
  });
  await assert.rejects(store.createOrGet({
    userId: USER_A, feature: FEATURE, idempotencyKey: "fourth", requestFingerprint: "f".repeat(64), requestPayload: {},
  }), (error: unknown) => error instanceof JobStoreError && error.status === 429 && error.retryAfterSeconds === 19);
});
