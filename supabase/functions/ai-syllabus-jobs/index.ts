import { parseAiJob, type AiFeature, type AiJob } from "../_shared/contracts.ts";
import {
  authenticateSupabaseRequest,
  AuthError,
  type AuthenticatedUser,
} from "../_shared/auth.ts";
import {
  JobStoreError,
  requestFingerprint,
  SupabaseAiJobStore,
  type AiJobRecord,
  type AiJobStore,
} from "../_shared/job-finalizer.ts";
import {
  StorageSourceError,
  SupabaseStorageSourceStore,
  validateAndBindStorageSource,
  type StorageSourceLimits,
  type StorageSourceStore,
} from "../_shared/storage-source.ts";

const SYLLABUS_FEATURE: AiFeature = "SYLLABUS_GENERATION";
const DEFAULT_LIMITS: StorageSourceLimits = {
  maxBytes: 50 * 1024 * 1024,
  maxPages: 500,
  maxFiles: 1,
};

export interface AiSyllabusJobsDependencies {
  authenticate: (request: Request) => Promise<AuthenticatedUser>;
  storage: StorageSourceStore;
  jobs: AiJobStore;
  limits: StorageSourceLimits;
  schedule: (jobId: string) => Promise<void>;
}

function jsonResponse(body: unknown, status = 200, headers: Record<string, string> = {}): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: {
      "content-type": "application/json; charset=utf-8",
      "cache-control": "no-store",
      ...headers,
    },
  });
}

function safeError(code: string, status: number, retryAfterSeconds?: number): Response {
  const messages: Record<string, string> = {
    AUTH_REQUIRED: "Authentication required",
    AUTH_INVALID: "Authentication required",
    AI_ACCESS_DENIED: "AI access is not available",
    INVALID_REQUEST: "Invalid request",
    INVALID_FEATURE: "Unsupported AI feature",
    SOURCE_NOT_BOUND: "Source must be uploaded and validated before processing",
    IDEMPOTENCY_KEY_CONFLICT: "Idempotency key conflicts with the source fingerprint",
    AI_RATE_LIMIT_EXCEEDED: "Too many syllabus attempts; retry later",
  };
  if (code === "AI_RATE_LIMIT_EXCEEDED") {
    const retry = Number.isSafeInteger(retryAfterSeconds) && retryAfterSeconds! >= 0 ? retryAfterSeconds! : 0;
    return jsonResponse({ error: { code, message: messages[code], retryAfterSeconds: retry } }, 429, { "Retry-After": String(retry) });
  }
  return jsonResponse({ error: { code, message: messages[code] ?? "AI job request could not be completed" } }, status);
}

function stablePathToken(value: string): string {
  let first = 2166136261;
  let second = 2654435761;
  for (const char of value) {
    first = Math.imul(first ^ char.charCodeAt(0), 16777619) >>> 0;
    second = Math.imul(second ^ char.charCodeAt(0), 2246822519) >>> 0;
  }
  return `${first.toString(16).padStart(8, "0")}${second.toString(16).padStart(8, "0")}`;
}

export function sourcePathForJob(userId: string, idempotencyKey: string): string {
  return `${userId}/${stablePathToken(`${userId}:${idempotencyKey}`)}.pdf`;
}

function integerEnvironment(name: string, fallback: number): number {
  const value = Number(Deno.env.get(name));
  return Number.isSafeInteger(value) && value > 0 ? value : fallback;
}

function runtimeLimits(): StorageSourceLimits {
  return {
    maxBytes: integerEnvironment("MAX_PDF_BYTES", DEFAULT_LIMITS.maxBytes),
    maxPages: integerEnvironment("MAX_PDF_PAGES", DEFAULT_LIMITS.maxPages),
    maxFiles: integerEnvironment("MAX_SOURCE_FILES", DEFAULT_LIMITS.maxFiles),
  };
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return value !== null && typeof value === "object" && !Array.isArray(value);
}

async function requestBody(request: Request): Promise<Record<string, unknown>> {
  try {
    const value = await request.json();
    if (!isRecord(value)) throw new Error();
    return value;
  } catch {
    throw new JobStoreError("INVALID_REQUEST", 400);
  }
}

function idempotencyKey(request: Request): string {
  const key = request.headers.get("idempotency-key")?.trim() ?? "";
  if (key.length === 0 || key.length > 255) throw new JobStoreError("INVALID_IDEMPOTENCY_KEY", 400);
  return key;
}

function sourceInput(body: Record<string, unknown>): Record<string, unknown> {
  if (body.source === undefined) return {};
  if (!isRecord(body.source)) throw new JobStoreError("INVALID_REQUEST", 400);
  return body.source;
}

function sourcePath(userId: string, key: string, source: Record<string, unknown>): string {
  const generated = sourcePathForJob(userId, key);
  if (source.objectPath === undefined) return generated;
  if (typeof source.objectPath !== "string" || source.objectPath !== generated) {
    throw new StorageSourceError("SOURCE_PATH_INVALID", 400);
  }
  return generated;
}

function clientMime(source: Record<string, unknown>): string | null {
  if (source.mimeType === undefined) return null;
  if (typeof source.mimeType !== "string") throw new JobStoreError("INVALID_REQUEST", 400);
  return source.mimeType.split(";", 1)[0].trim().toLowerCase();
}

function clientSourceHash(source: Record<string, unknown>): string | null {
  if (source.sourceHash === undefined) return null;
  if (typeof source.sourceHash !== "string" || !/^[0-9a-f]{64}$/i.test(source.sourceHash)) {
    throw new JobStoreError("INVALID_REQUEST", 400);
  }
  return source.sourceHash.toLowerCase();
}

function clientSourceBytes(source: Record<string, unknown>): number | null {
  if (source.sourceBytes === undefined) return null;
  if (typeof source.sourceBytes !== "number" || !Number.isSafeInteger(source.sourceBytes) || source.sourceBytes < 1) {
    throw new JobStoreError("INVALID_REQUEST", 400);
  }
  return source.sourceBytes;
}

function assertBoundSourceMatches(job: AiJobRecord, source: Record<string, unknown>): void {
  if (job.sourceObjectPath === null) return;
  const requested = {
    mimeType: clientMime(source),
    sourceHash: clientSourceHash(source),
    sourceBytes: clientSourceBytes(source),
  };
  const boundMime = job.sourceMimeType?.split(";", 1)[0].trim().toLowerCase() ?? null;
  if (boundMime !== requested.mimeType || job.sourceHash !== requested.sourceHash || job.sourceBytes !== requested.sourceBytes) {
    throw new JobStoreError("IDEMPOTENCY_KEY_CONFLICT", 409);
  }
}

async function releaseQuietly(dependencies: AiSyllabusJobsDependencies, userId: string, jobId: string): Promise<void> {
  try {
    await dependencies.jobs.releaseReservation(userId, jobId);
  } catch {
    // The original validation error is safer than exposing a secondary database detail.
  }
}

async function createJob(
  request: Request,
  dependencies: AiSyllabusJobsDependencies,
  user: AuthenticatedUser,
): Promise<Response> {
  const key = idempotencyKey(request);
  const body = await requestBody(request);
  if (body.feature !== undefined && body.feature !== SYLLABUS_FEATURE) return safeError("INVALID_FEATURE", 400);
  const source = sourceInput(body);
  const path = sourcePath(user.userId, key, source);
  const mimeType = clientMime(source);
  const sourceHash = clientSourceHash(source);
  const sourceBytes = clientSourceBytes(source);
  const payload = {
    feature: SYLLABUS_FEATURE,
    sourcePath: path,
    fileName: typeof source.fileName === "string" ? source.fileName : null,
    mimeType,
    sourceHash,
    sourceBytes,
  } satisfies Record<string, unknown>;
  const fingerprint = await requestFingerprint(payload);

  let created;
  try {
    created = await dependencies.jobs.createOrGet({
      userId: user.userId,
      feature: SYLLABUS_FEATURE,
      idempotencyKey: key,
      requestFingerprint: fingerprint,
      requestPayload: payload,
    });
  } catch (error) {
    if (error instanceof JobStoreError) return safeError(error.code, error.status, error.retryAfterSeconds);
    return safeError("AI_JOB_DATA_UNAVAILABLE", 503);
  }

  let job = await dependencies.jobs.getJob(user.userId, created.jobId);
  if (!job) return safeError("AI_JOB_NOT_FOUND", 503);
  assertBoundSourceMatches(job, source);
  const ready = source.ready === true || source.objectPath !== undefined;
  if (ready && job.status === "RESERVED" && job.sourceObjectPath === null) {
    try {
      const bound = await validateAndBindStorageSource(
        user.userId,
        path,
        dependencies.storage,
        dependencies.limits,
        mimeType,
      );
      if (sourceHash !== null && sourceHash !== bound.sourceHash || sourceBytes !== null && sourceBytes !== bound.sourceBytes) {
        await releaseQuietly(dependencies, user.userId, created.jobId);
        throw new JobStoreError("IDEMPOTENCY_KEY_CONFLICT", 409);
      }
      job = await dependencies.jobs.bindSource(user.userId, created.jobId, bound);
    } catch (error) {
      if (error instanceof StorageSourceError) {
        if (error.code !== "SOURCE_LOOKUP_UNAVAILABLE") {
          await releaseQuietly(dependencies, user.userId, created.jobId);
        }
        return safeError(error.code, error.status);
      }
      if (error instanceof JobStoreError) {
        if (error.code === "SOURCE_NOT_FOUND") {
          await releaseQuietly(dependencies, user.userId, created.jobId);
        }
        return safeError(error.code, error.status);
      }
      return safeError("SOURCE_BINDING_FAILED", 503);
    }
  }

  const status = created.reused ? 200 : 201;
  return jsonResponse({
    jobId: job.id,
    status: job.status,
    uploadPath: path,
    sourceBound: job.sourceObjectPath !== null && job.sourceHash !== null,
    sourceHash: job.sourceHash,
    sourceBytes: job.sourceBytes,
    sourcePages: job.sourcePages,
    sourceFileCount: job.sourceFileCount,
    sourceMimeType: job.sourceMimeType,
  }, status);
}

async function processJob(
  request: Request,
  dependencies: AiSyllabusJobsDependencies,
  user: AuthenticatedUser,
  jobId: string,
): Promise<Response> {
  if (!jobId || jobId.includes("/")) return safeError("INVALID_REQUEST", 400);
  let job;
  try {
    job = await dependencies.jobs.getJob(user.userId, jobId);
  } catch (error) {
    return error instanceof JobStoreError ? safeError(error.code, error.status) : safeError("AI_JOB_DATA_UNAVAILABLE", 503);
  }
  if (!job) return safeError("AI_JOB_NOT_FOUND", 404);
  if (job.status === "PROCESSING") return jsonResponse({ jobId, status: "PROCESSING" }, 202);
  if (job.status !== "RESERVED") return safeError("AI_JOB_NOT_RESERVABLE", 409);
  if (job.sourceObjectPath === null || job.sourceHash === null) return safeError("SOURCE_NOT_BOUND", 409);

  try {
    const processing = await dependencies.jobs.claimForProcessing(user.userId, jobId);
    try {
      await dependencies.schedule(processing.id);
    } catch {
      // PROCESSING plus its lease is the durable handoff; scheduling is only an accelerator.
    }
    return jsonResponse({ jobId: processing.id, status: "PROCESSING" }, 202);
  } catch (error) {
    return error instanceof JobStoreError ? safeError(error.code, error.status) : safeError("AI_JOB_DATA_UNAVAILABLE", 503);
  }
}

function publicJob(job: AiJobRecord): AiJob {
  return parseAiJob({
    jobId: job.id,
    feature: job.feature,
    status: job.status,
    schemaVersion: job.schemaVersion,
    promptVersion: job.promptVersion,
    modelVersion: job.modelVersion,
    proposal: job.proposal,
    warnings: job.warnings,
    errorCode: job.errorCode,
    errorMessage: job.errorMessage,
    createdAt: job.createdAt,
    updatedAt: job.updatedAt,
    finishedAt: job.finishedAt,
    providerExecutionStartedAt: job.providerExecutionStartedAt ?? null,
  });
}

async function getJob(
  dependencies: AiSyllabusJobsDependencies,
  user: AuthenticatedUser,
  jobId: string,
): Promise<Response> {
  if (!jobId || jobId.includes("/")) return safeError("INVALID_REQUEST", 400);
  let job: AiJobRecord | null;
  try {
    job = await dependencies.jobs.getJob(user.userId, jobId);
  } catch (error) {
    return error instanceof JobStoreError ? safeError(error.code, error.status) : safeError("AI_JOB_DATA_UNAVAILABLE", 503);
  }
  if (!job) return safeError("AI_JOB_NOT_FOUND", 404);
  try {
    return jsonResponse(publicJob(job));
  } catch {
    return safeError("AI_JOB_DATA_UNAVAILABLE", 503);
  }
}

export function createAiSyllabusJobsHandler(dependencies: AiSyllabusJobsDependencies): (request: Request) => Promise<Response> {
  return async (request: Request): Promise<Response> => {
    if (!request.headers.get("authorization")) return safeError("AUTH_REQUIRED", 401);
    if (request.method !== "POST" && request.method !== "GET") return jsonResponse({ error: { code: "METHOD_NOT_ALLOWED", message: "Only GET and POST are supported" } }, 405, { allow: "GET, POST" });

    let user: AuthenticatedUser;
    try {
      user = await dependencies.authenticate(request);
    } catch (error) {
      if (error instanceof AuthError) return safeError(error.code, error.status);
      return safeError("AUTH_UNAVAILABLE", 503);
    }

    const routePath = extractRoutePath(request);
    if (routePath === null) return safeError("NOT_FOUND", 404);
    const processMatch = routePath.match(/^\/([^/]+)\/process$/);
    const getMatch = routePath.match(/^\/([^/]+)$/);
    try {
      const rootPath = routePath === "";
      if (rootPath && request.method !== "POST") return jsonResponse({ error: { code: "METHOD_NOT_ALLOWED", message: "POST is required for job creation" } }, 405, { allow: "POST" });
      if (processMatch && request.method !== "POST") return jsonResponse({ error: { code: "METHOD_NOT_ALLOWED", message: "POST is required to process a job" } }, 405, { allow: "POST" });
      if (getMatch && request.method !== "GET") return jsonResponse({ error: { code: "METHOD_NOT_ALLOWED", message: "GET is required to read a job" } }, 405, { allow: "GET" });
      if (rootPath) return await createJob(request, dependencies, user);
      if (getMatch) return await getJob(dependencies, user, getMatch[1]);
      if (processMatch) return await processJob(request, dependencies, user, processMatch[1]);
      return safeError("NOT_FOUND", 404);
    } catch (error) {
      if (error instanceof JobStoreError || error instanceof StorageSourceError) return safeError(error.code, error.status);
      return safeError("AI_JOB_UNAVAILABLE", 503);
    }
  };
}

function extractRoutePath(request: Request): string | null {
  const pathname = new URL(request.url).pathname.replace(/\/+$/, "") || "/";
  const prefixes = [
    "/functions/v1/ai-syllabus-jobs",
    "/ai-syllabus-jobs",
    "ai-syllabus-jobs",
  ];
  for (const prefix of prefixes) {
    if (pathname === prefix) return "";
    if (pathname.startsWith(`${prefix}/`)) return pathname.slice(prefix.length);
  }
  return null;
}

function runtimeDependencies(request: Request): AiSyllabusJobsDependencies {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
  const publishableKey = (Deno.env.get("SUPABASE_ANON_KEY") ?? Deno.env.get("SUPABASE_PUBLISHABLE_KEY"))?.trim();
  const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  const accessToken = request.headers.get("authorization")?.replace(/^Bearer\s+/i, "").trim() ?? "";
  if (!supabaseUrl || !publishableKey || !serviceRoleKey) throw new AuthError("AUTH_UNAVAILABLE", 503);
  return {
    authenticate: authenticateSupabaseRequest,
    storage: new SupabaseStorageSourceStore({ supabaseUrl, publishableKey, accessToken, serviceRoleKey }, runtimeLimits().maxBytes),
    jobs: new SupabaseAiJobStore({ supabaseUrl, publishableKey, accessToken, serviceRoleKey }),
    limits: runtimeLimits(),
    schedule: async () => {
      // The persisted PROCESSING lease is the durable queue consumed by the worker/Cron in Task 7.
    },
  };
}

async function handleAiSyllabusJobs(request: Request): Promise<Response> {
  try {
    return await createAiSyllabusJobsHandler(runtimeDependencies(request))(request);
  } catch (error) {
    if (error instanceof AuthError) return safeError(error.code, error.status);
    return safeError("AI_JOB_UNAVAILABLE", 503);
  }
}

if (import.meta.main) Deno.serve(handleAiSyllabusJobs);
