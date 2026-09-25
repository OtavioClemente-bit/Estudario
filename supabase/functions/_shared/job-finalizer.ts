import type { AiFeature, AiJobStatus } from "./contracts.ts";
import type { ProviderResponse, ProviderUsage } from "./openai-provider.ts";

export interface TerminalAiTelemetry {
  feature: AiFeature;
  userPseudonym: string;
  modelVersion: string;
  promptVersion: string;
  schemaVersion: number;
  jobId: string;
  inputTokens: number | null;
  outputTokens: number | null;
  totalTokens: number | null;
  durationMs: number;
  terminalStatus: "SUCCEEDED" | "FAILED" | "EXPIRED" | "CANCELLED";
  timestamp: string;
}

export async function emitAiTerminalTelemetry(input: {
  feature: AiFeature; userId: string; modelVersion: string; promptVersion: string;
  schemaVersion: number; jobId: string; usage?: ProviderUsage | null;
  startedAt?: string | null; terminalStatus: TerminalAiTelemetry["terminalStatus"];
  now?: () => Date; sink?: (event: TerminalAiTelemetry) => void;
}): Promise<void> {
  try {
    const timestamp = (input.now ?? (() => new Date()))();
    const digest = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(`estudario-ai-telemetry-v1:${input.userId}`));
    const userPseudonym = Array.from(new Uint8Array(digest), (byte) => byte.toString(16).padStart(2, "0")).join("");
    const started = input.startedAt ? Date.parse(input.startedAt) : timestamp.getTime();
    const event: TerminalAiTelemetry = {
      feature: input.feature, userPseudonym, modelVersion: input.modelVersion,
      promptVersion: input.promptVersion, schemaVersion: input.schemaVersion, jobId: input.jobId,
      inputTokens: input.usage?.inputTokens ?? null, outputTokens: input.usage?.outputTokens ?? null,
      totalTokens: input.usage?.totalTokens ?? null,
      durationMs: Number.isFinite(started) ? Math.max(0, timestamp.getTime() - started) : 0,
      terminalStatus: input.terminalStatus, timestamp: timestamp.toISOString(),
    };
    (input.sink ?? ((value) => console.info(JSON.stringify(value))))(event);
  } catch { /* logging must not change a finalized job */ }
}
import type { BoundStorageSource } from "./storage-source.ts";

export interface CreateAiJobInput {
  userId: string;
  feature: AiFeature;
  idempotencyKey: string;
  requestFingerprint: string;
  requestPayload: Record<string, unknown>;
}

export interface CreatedAiJob {
  jobId: string;
  status: AiJobStatus;
  reservationId: string;
  quotaPeriod: string;
  quotaRemaining: number;
  reused: boolean;
  requestPayload: Record<string, unknown>;
}

export interface AiJobRecord {
  id: string;
  userId: string;
  feature: AiFeature;
  status: AiJobStatus;
  idempotencyKey: string;
  requestFingerprint: string;
  requestPayload: Record<string, unknown>;
  sourceObjectPath: string | null;
  sourceHash: string | null;
  sourceBytes: number | null;
  sourcePages: number | null;
  sourceFileCount: number | null;
  sourceMimeType: string | null;
  openaiResponseId?: string | null;
  providerExecutionStartedAt?: string | null;
  providerReconciledAt?: string | null;
  providerResultRecoverable?: boolean | null;
  promptVersion: string | null;
  schemaVersion: number | null;
  modelVersion: string | null;
  proposal: Record<string, unknown> | null;
  warnings: unknown[];
  errorCode: string | null;
  errorMessage: string | null;
  createdAt: string;
  updatedAt: string;
  finishedAt: string | null;
  source?: BoundStorageSource;
}

export interface AiJobStore {
  createOrGet(input: CreateAiJobInput): Promise<CreatedAiJob>;
  getJob(userId: string, jobId: string): Promise<AiJobRecord | null>;
  bindSource(userId: string, jobId: string, source: BoundStorageSource): Promise<AiJobRecord>;
  claimForProcessing(userId: string, jobId: string): Promise<AiJobRecord>;
  releaseReservation(userId: string, jobId: string): Promise<void>;
}

export interface CancellationReconciliation {
  responseId: string | null;
  providerStatus: string;
  resultRecoverable: boolean;
  errorCode?: string | null;
}

export interface AiJobCancellationStore {
  getJob(userId: string, jobId: string): Promise<AiJobRecord | null>;
  requestCancellation(userId: string, jobId: string): Promise<AiJobRecord>;
  cancelWithoutProvider(userId: string, jobId: string): Promise<AiJobRecord>;
  recordCancellationReconciliation(
    userId: string,
    jobId: string,
    reconciliation: CancellationReconciliation,
  ): Promise<AiJobRecord>;
  cancelAfterReconciliation(
    userId: string,
    jobId: string,
    code: string,
    message: string,
    terminalStatus?: "CANCELLED" | "FAILED",
  ): Promise<AiJobRecord>;
  finalizeCancellationSuccess(
    userId: string,
    jobId: string,
    response: ProviderResponse,
    proposal: Record<string, unknown>,
  ): Promise<AiJobRecord>;
}

export class JobStoreError extends Error {
  constructor(public readonly code: string, public readonly status: number, public readonly retryAfterSeconds?: number) {
    super(code);
    this.name = "JobStoreError";
  }
}

export async function requestFingerprint(payload: Record<string, unknown>): Promise<string> {
  const canonical = JSON.stringify(payload, Object.keys(payload).sort());
  const digest = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(canonical));
  return [...new Uint8Array(digest)].map((byte) => byte.toString(16).padStart(2, "0")).join("");
}

export interface SupabaseJobEnvironment {
  supabaseUrl: string;
  publishableKey: string;
  accessToken: string;
  serviceRoleKey: string;
  fetcher?: typeof fetch;
}

export class SupabaseAiJobStore implements AiJobStore {
  constructor(private readonly environment: SupabaseJobEnvironment) {}

  async createOrGet(input: CreateAiJobInput): Promise<CreatedAiJob> {
    const response = await this.rpc("create_or_get_ai_job_and_reserve_quota", {
      p_feature: input.feature,
      p_idempotency_key: input.idempotencyKey,
      p_request_fingerprint: input.requestFingerprint,
      p_request_payload: input.requestPayload,
    });
    const row = firstRow(response);
    const jobId = stringField(row, "job_id");
    const job = await this.getJob(input.userId, jobId);
    if (!job) throw new JobStoreError("AI_JOB_NOT_FOUND", 503);
    return {
      jobId,
      status: enumJobStatus(row.status),
      reservationId: stringField(row, "reservation_id"),
      quotaPeriod: stringField(row, "quota_period"),
      quotaRemaining: integerField(row, "quota_remaining"),
      reused: booleanField(row, "reused"),
      requestPayload: job.requestPayload,
    };
  }

  async getJob(userId: string, jobId: string): Promise<AiJobRecord | null> {
    const url = this.restUrl("ai_jobs");
    url.searchParams.set("select", "id,user_id,feature,status,idempotency_key,request_fingerprint,request_payload,source_object_path,source_hash,source_bytes,source_pages,source_file_count,source_mime_type,source_metadata,openai_response_id,provider_execution_started_at,provider_reconciled_at,provider_result_recoverable,prompt_version,schema_version,model_version,proposal,warnings,error_code,error_message,created_at,updated_at,finished_at");
    url.searchParams.set("id", `eq.${jobId}`);
    url.searchParams.set("user_id", `eq.${userId}`);
    url.searchParams.set("limit", "1");
    const response = await this.fetch(url, { headers: this.headers() });
    if (response.status === 404) return null;
    if (!response.ok) throw await storeError(response);
    const rows = await jsonArray(response);
    return rows.length === 0 ? null : parseJob(rows[0]);
  }

  async bindSource(userId: string, jobId: string, source: BoundStorageSource): Promise<AiJobRecord> {
    const row = firstRow(await this.rpc("bind_ai_job_source", {
      p_user_id: userId,
      p_job_id: jobId,
      p_source_object_path: source.path,
      p_source_mime_type: source.mimeType,
      p_source_hash: source.sourceHash,
      p_source_bytes: source.sourceBytes,
      p_source_pages: source.sourcePages,
      p_source_file_count: source.sourceFileCount,
      p_source_metadata: source.metadata,
    }, this.adminHeaders()));
    const job = parseJob(row);
    if (job.userId !== userId) throw new JobStoreError("AI_JOB_FORBIDDEN", 403);
    return job;
  }

  async claimForProcessing(userId: string, jobId: string): Promise<AiJobRecord> {
    const row = firstRow(await this.rpc("claim_ai_job", {
      p_job_id: jobId,
      p_lease_owner: `edge:${crypto.randomUUID()}`,
      p_lease_seconds: 300,
    }, this.adminHeaders()));
    const job = parseJob(row);
    if (job.userId !== userId) throw new JobStoreError("AI_JOB_FORBIDDEN", 403);
    return job;
  }

  async releaseReservation(userId: string, jobId: string): Promise<void> {
    const job = await this.getJob(userId, jobId);
    if (!job) return;
    await this.rpc("release_ai_job_reservation", {
      p_job_id: jobId,
      p_terminal_status: "CANCELLED",
      p_error_code: "SOURCE_VALIDATION_FAILED",
      p_error_message: "The source could not be validated",
    });
  }

  async requestCancellation(userId: string, jobId: string): Promise<AiJobRecord> {
    return this.cancellationJob(userId, await this.rpc("request_ai_job_cancellation", { p_job_id: jobId }));
  }

  async cancelWithoutProvider(userId: string, jobId: string): Promise<AiJobRecord> {
    return this.cancellationJob(userId, await this.rpc("cancel_ai_job_without_provider", { p_job_id: jobId }));
  }

  async recordCancellationReconciliation(
    userId: string,
    jobId: string,
    reconciliation: CancellationReconciliation,
  ): Promise<AiJobRecord> {
    return this.cancellationJob(userId, await this.rpc("record_ai_job_cancellation_reconciliation", {
      p_job_id: jobId,
      p_openai_response_id: reconciliation.responseId,
      p_provider_status: reconciliation.providerStatus,
      p_provider_result_recoverable: reconciliation.resultRecoverable,
      p_error_code: reconciliation.errorCode ?? null,
    }, this.adminHeaders()));
  }

  async cancelAfterReconciliation(userId: string, jobId: string, code: string, message: string, terminalStatus: "CANCELLED" | "FAILED" = "CANCELLED"): Promise<AiJobRecord> {
    const row = firstRow(await this.rpc("release_ai_job_reservation", {
      p_job_id: jobId,
      p_terminal_status: terminalStatus,
      p_error_code: code,
      p_error_message: message,
    }));
    return this.cancellationJob(userId, row);
  }

  async finalizeCancellationSuccess(
    userId: string,
    jobId: string,
    response: ProviderResponse,
    proposal: Record<string, unknown>,
  ): Promise<AiJobRecord> {
    return this.cancellationJob(userId, await this.rpc("finalize_ai_job_success_after_cancellation", {
      p_job_id: jobId,
      p_proposal: proposal,
      p_warnings: Array.isArray(proposal.warnings) ? proposal.warnings : [],
      p_openai_response_id: response.id,
      p_prompt_version: typeof proposal.promptVersion === "string" ? proposal.promptVersion : null,
      p_schema_version: typeof proposal.schemaVersion === "number" ? proposal.schemaVersion : null,
      p_model_version: typeof proposal.modelVersion === "string" ? proposal.modelVersion : null,
    }, this.adminHeaders()));
  }

  private cancellationJob(userId: string, value: unknown): AiJobRecord {
    const job = parseJob(firstRow(value));
    if (job.userId !== userId) throw new JobStoreError("AI_JOB_FORBIDDEN", 403);
    return job;
  }

  private restUrl(table: string): URL {
    return new URL(`${this.environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/${table}`);
  }

  private headers(): HeadersInit {
    return {
      apikey: this.environment.publishableKey,
      authorization: `Bearer ${this.environment.accessToken}`,
      accept: "application/json",
    };
  }

  private fetch(input: RequestInfo | URL, init?: RequestInit): Promise<Response> {
    return (this.environment.fetcher ?? fetch)(input, init);
  }

  private async rpc(name: string, body: Record<string, unknown>, headers = this.headers()): Promise<unknown> {
    const response = await this.fetch(`${this.environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/rpc/${name}`, {
      method: "POST",
      headers: { ...headers, "content-type": "application/json" },
      body: JSON.stringify(body),
    });
    if (!response.ok) throw await storeError(response);
    try {
      return await response.json();
    } catch {
      throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
    }
  }

  private adminHeaders(): HeadersInit {
    return {
      apikey: this.environment.serviceRoleKey,
      authorization: `Bearer ${this.environment.serviceRoleKey}`,
      accept: "application/json",
    };
  }
}

function parseJob(value: Record<string, unknown>): AiJobRecord {
  const sourcePath = nullableString(value, "source_object_path");
  const sourceHash = nullableString(value, "source_hash");
  const source = sourcePath && sourceHash
    ? {
      path: sourcePath,
      mimeType: nullableString(value, "source_mime_type") ?? "application/pdf",
      sourceHash,
      sourceBytes: nullableInteger(value, "source_bytes") ?? 0,
      sourcePages: nullableInteger(value, "source_pages") ?? 0,
      sourceFileCount: nullableInteger(value, "source_file_count") ?? 0,
      metadata: objectField(value, "source_metadata"),
    }
    : undefined;
  return {
    id: stringField(value, "id"),
    userId: stringField(value, "user_id"),
    feature: value.feature as AiFeature,
    status: enumJobStatus(value.status),
    idempotencyKey: stringField(value, "idempotency_key"),
    requestFingerprint: stringField(value, "request_fingerprint"),
    requestPayload: objectField(value, "request_payload"),
    sourceObjectPath: sourcePath,
    sourceHash,
    sourceBytes: nullableInteger(value, "source_bytes"),
    sourcePages: nullableInteger(value, "source_pages"),
    sourceFileCount: nullableInteger(value, "source_file_count"),
    sourceMimeType: nullableString(value, "source_mime_type"),
    openaiResponseId: nullableString(value, "openai_response_id"),
    providerExecutionStartedAt: nullableString(value, "provider_execution_started_at"),
    providerReconciledAt: nullableString(value, "provider_reconciled_at"),
    providerResultRecoverable: nullableBoolean(value, "provider_result_recoverable"),
    promptVersion: nullableString(value, "prompt_version"),
    schemaVersion: nullableInteger(value, "schema_version"),
    modelVersion: nullableString(value, "model_version"),
    proposal: nullableObject(value, "proposal"),
    warnings: arrayField(value, "warnings"),
    errorCode: nullableString(value, "error_code"),
    errorMessage: nullableString(value, "error_message"),
    createdAt: stringField(value, "created_at"),
    updatedAt: stringField(value, "updated_at"),
    finishedAt: nullableString(value, "finished_at"),
    source,
  };
}

function firstRow(value: unknown): Record<string, unknown> {
  if (Array.isArray(value) && value.length > 0 && isObject(value[0])) return value[0];
  if (isObject(value)) return value;
  throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
}

function isObject(value: unknown): value is Record<string, unknown> {
  return value !== null && typeof value === "object" && !Array.isArray(value);
}

async function jsonArray(response: Response): Promise<Record<string, unknown>[]> {
  try {
    const value = await response.json();
    if (!Array.isArray(value)) throw new Error();
    return value.filter(isObject);
  } catch {
    throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
  }
}

async function storeError(response: Response): Promise<JobStoreError> {
  let code = "AI_JOB_DATA_UNAVAILABLE";
  let retryAfterSeconds: number | undefined;
  try {
    const body = await response.json() as Record<string, unknown>;
    const message = typeof body.message === "string" ? body.message : typeof body.error === "string" ? body.error : "";
    if (/^[A-Z][A-Z0-9_]{2,63}$/.test(message)) code = message;
    if (code === "AI_RATE_LIMIT_EXCEEDED" && typeof body.details === "string") {
      const match = /^retry_after_seconds=(\d+)$/.exec(body.details);
      if (match) {
        const parsed = Number(match[1]);
        if (Number.isSafeInteger(parsed)) retryAfterSeconds = parsed;
      }
    }
  } catch {
    // Keep the response safe when PostgREST does not return JSON.
  }
  const status = code === "AI_RATE_LIMIT_EXCEEDED"
    ? 429
    : code === "SOURCE_NOT_FOUND"
    ? 404
    : code === "IDEMPOTENCY_KEY_CONFLICT" || code === "IDEMPOTENCY_CONFLICT"
    ? 409
    : response.status >= 400 && response.status < 500
    ? response.status
    : 503;
  return new JobStoreError(code, status, retryAfterSeconds);
}

function stringField(row: Record<string, unknown>, key: string): string {
  if (typeof row[key] !== "string" || row[key].trim().length === 0) throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
  return row[key] as string;
}

function nullableString(row: Record<string, unknown>, key: string): string | null {
  return row[key] === null || row[key] === undefined ? null : stringField(row, key);
}

function integerField(row: Record<string, unknown>, key: string): number {
  if (typeof row[key] !== "number" || !Number.isInteger(row[key])) throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
  return row[key] as number;
}

function nullableInteger(row: Record<string, unknown>, key: string): number | null {
  return row[key] === null || row[key] === undefined ? null : integerField(row, key);
}

function nullableBoolean(row: Record<string, unknown>, key: string): boolean | null {
  return row[key] === null || row[key] === undefined ? null : booleanField(row, key);
}

function booleanField(row: Record<string, unknown>, key: string): boolean {
  if (typeof row[key] !== "boolean") throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
  return row[key] as boolean;
}

function objectField(row: Record<string, unknown>, key: string): Record<string, unknown> {
  if (!isObject(row[key])) throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
  return row[key];
}

function nullableObject(row: Record<string, unknown>, key: string): Record<string, unknown> | null {
  if (row[key] === null || row[key] === undefined) return null;
  return objectField(row, key);
}

function arrayField(row: Record<string, unknown>, key: string): unknown[] {
  if (!Array.isArray(row[key])) throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
  return row[key];
}

function enumJobStatus(value: unknown): AiJobStatus {
  if (["RESERVED", "PROCESSING", "SUCCEEDED", "FAILED", "EXPIRED", "CANCELLED"].includes(String(value))) {
    return value as AiJobStatus;
  }
  throw new JobStoreError("AI_JOB_DATA_UNAVAILABLE", 503);
}
