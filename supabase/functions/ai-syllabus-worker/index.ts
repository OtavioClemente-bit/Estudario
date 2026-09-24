import type { AiSyllabusProposal, AiWarning } from "../_shared/contracts.ts";
import { validateAiSyllabusProposal, type ProposalValidationLimits } from "../_shared/proposal-validator.ts";
import {
  createOpenAiProvider,
  OpenAiProviderError,
  type OpenAiProvider,
  type ProviderResponse,
  type ProviderUsage,
} from "../_shared/openai-provider.ts";
import { SYLLABUS_PROMPT_VERSION, SYLLABUS_SYSTEM_PROMPT, syllabusUserPrompt } from "../_shared/prompts/syllabus-v1.ts";
import { AI_SYLLABUS_PROPOSAL_SCHEMA, AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION } from "../_shared/schemas/ai-syllabus-proposal-v1.ts";
import { AI_SYLLABUS_SOURCE_BUCKET, SupabaseStorageSourceStore, type StorageSourceStore } from "../_shared/storage-source.ts";

export interface SyllabusWorkerJob {
  id: string;
  userId: string;
  status: "PROCESSING";
  sourceObjectPath: string;
  sourceHash: string;
  sourceBytes: number;
  sourcePages: number;
  sourceFileCount: number;
  openaiResponseId: string | null;
  providerExecutionStartedAt: string | null;
  leaseExpiresAt: string | null;
  retryCount: number;
  promptVersion?: string | null;
  modelVersion?: string | null;
}

export interface SyllabusWorkerStore {
  claimNext(now: Date, leaseSeconds: number): Promise<SyllabusWorkerJob | null>;
  persistResponseId(jobId: string, responseId: string): Promise<void>;
  reconcileProvider(jobId: string, recoverable: boolean): Promise<void>;
  markRetry(jobId: string): Promise<void>;
  finalizeSuccess(
    jobId: string,
    proposal: AiSyllabusProposal,
    warnings: AiWarning[],
    responseId: string | null,
    usage: ProviderUsage | null,
  ): Promise<void>;
  finalizeFailure(jobId: string, code: string, message?: string, terminalStatus?: "FAILED" | "EXPIRED"): Promise<void>;
  cleanupSource(jobId: string): Promise<void>;
}

export interface SyllabusWorkerDependencies {
  jobs: SyllabusWorkerStore;
  provider: OpenAiProvider;
  source(job: SyllabusWorkerJob): Promise<Uint8Array>;
  now?: () => Date;
  leaseSeconds?: number;
  maxRetries?: number;
  validationLimits?: ProposalValidationLimits;
  captureUsage: (jobId: string, usage: ProviderUsage | null) => Promise<void>;
}

function providerFailureCode(error: unknown): string {
  if (error instanceof OpenAiProviderError) {
    if (error.code === "OPENAI_TIMEOUT") return "PROVIDER_TIMEOUT";
    if (error.code === "OPENAI_API_KEY_MISSING") return "PROVIDER_NOT_CONFIGURED";
  }
  return "PROVIDER_ERROR";
}

function leaseExpired(job: SyllabusWorkerJob, now: Date): boolean {
  return job.leaseExpiresAt !== null && Date.parse(job.leaseExpiresAt) <= now.getTime();
}

async function finalizeFailure(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  code: string,
  message: string,
  terminalStatus: "FAILED" | "EXPIRED" = "FAILED",
): Promise<void> {
  await dependencies.jobs.reconcileProvider(job.id, false);
  await dependencies.jobs.finalizeFailure(job.id, code, message, terminalStatus);
  try {
    await dependencies.jobs.cleanupSource(job.id);
  } catch {
    // Cleanup is retriable housekeeping; it must not turn a terminal job into a second outcome.
  }
}

async function processResponse(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  response: ProviderResponse,
): Promise<void> {
  if (["queued", "in_progress"].includes(response.status)) {
    await dependencies.jobs.reconcileProvider(job.id, true);
    if (job.retryCount >= (dependencies.maxRetries ?? 3)) {
      // An in-progress provider result is still recoverable. Keep the lease
      // durable and let the next poll retrieve it; never release quota here.
    } else {
      await dependencies.jobs.markRetry(job.id);
    }
    return;
  }

  if (response.status !== "completed") {
    await finalizeFailure(dependencies, job, "PROVIDER_RESULT_UNAVAILABLE", "The provider did not return a recoverable result");
    return;
  }

  let proposal: AiSyllabusProposal;
  try {
    proposal = await validateAiSyllabusProposal(response.outputText ?? "", dependencies.validationLimits);
  } catch (error) {
    const code = error instanceof Error && error.message.startsWith("EMPTY_OUTPUT") ? "EMPTY_OUTPUT" :
      error instanceof Error && error.message.startsWith("OUTPUT_LIMIT_EXCEEDED") ? "OUTPUT_LIMIT_EXCEEDED" : "SCHEMA_MISMATCH";
    await finalizeFailure(dependencies, job, code, "The provider output did not pass strict validation");
    return;
  }

  await dependencies.captureUsage(job.id, response.usage);
  await dependencies.jobs.finalizeSuccess(job.id, proposal, proposal.warnings, response.id, response.usage);
  try {
    await dependencies.jobs.cleanupSource(job.id);
  } catch {
    // The terminal proposal is already durable; cleanup can be retried by housekeeping.
  }
}

export async function processSyllabusJob(dependencies: SyllabusWorkerDependencies): Promise<boolean> {
  const now = dependencies.now ?? (() => new Date());
  const job = await dependencies.jobs.claimNext(now(), dependencies.leaseSeconds ?? 300);
  if (!job) return false;

  try {
    let response: ProviderResponse;
    if (job.openaiResponseId !== null) {
      response = await dependencies.provider.retrieve(job.openaiResponseId);
    } else {
      const bytes = await dependencies.source(job);
      response = await dependencies.provider.start({
        jobId: job.id,
        idempotencyKey: job.id,
        source: { filename: job.sourceObjectPath.split("/").pop() ?? "source.pdf", bytes },
        prompt: `${SYLLABUS_SYSTEM_PROMPT}\n\n${syllabusUserPrompt()}`,
        promptVersion: SYLLABUS_PROMPT_VERSION,
        schemaVersion: AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
        schema: AI_SYLLABUS_PROPOSAL_SCHEMA,
      });
      await dependencies.jobs.persistResponseId(job.id, response.id);
    }
    if (leaseExpired(job, now())) await dependencies.jobs.reconcileProvider(job.id, true);
    await processResponse(dependencies, job, response);
  } catch (error) {
    const code = providerFailureCode(error);
    if (job.retryCount < (dependencies.maxRetries ?? 3)) {
      await dependencies.jobs.markRetry(job.id);
    } else {
      // A timeout or 5xx does not prove that the provider did not create a
      // recoverable response. Keep the job PROCESSING and quota reserved.
      await dependencies.jobs.reconcileProvider(job.id, true);
    }
  }
  return true;
}

export async function runSyllabusWorker(dependencies: SyllabusWorkerDependencies, maxJobs = 1): Promise<number> {
  let processed = 0;
  for (let attempt = 0; attempt < maxJobs; attempt += 1) {
    if (!await processSyllabusJob(dependencies)) break;
    processed += 1;
  }
  return processed;
}

interface WorkerRuntimeEnvironment {
  supabaseUrl: string;
  serviceRoleKey: string;
  fetcher?: typeof fetch;
}

function workerHeaders(serviceRoleKey: string): HeadersInit {
  return {
    apikey: serviceRoleKey,
    authorization: `Bearer ${serviceRoleKey}`,
    accept: "application/json",
  };
}

function workerRow(value: unknown): Record<string, unknown> {
  if (Array.isArray(value) && value.length > 0 && typeof value[0] === "object" && value[0] !== null) return value[0] as Record<string, unknown>;
  if (typeof value === "object" && value !== null && !Array.isArray(value)) return value as Record<string, unknown>;
  throw new Error("AI_WORKER_DATA_UNAVAILABLE");
}

function stringField(row: Record<string, unknown>, key: string): string {
  if (typeof row[key] !== "string" || row[key].trim().length === 0) throw new Error("AI_WORKER_DATA_UNAVAILABLE");
  return row[key] as string;
}

function nullableStringField(row: Record<string, unknown>, key: string): string | null {
  return row[key] === null || row[key] === undefined ? null : stringField(row, key);
}

function numberField(row: Record<string, unknown>, key: string, fallback = 0): number {
  return typeof row[key] === "number" && Number.isSafeInteger(row[key]) ? row[key] as number : fallback;
}

function parseWorkerJob(row: Record<string, unknown>): SyllabusWorkerJob {
  return {
    id: stringField(row, "id"),
    userId: stringField(row, "user_id"),
    status: "PROCESSING",
    sourceObjectPath: stringField(row, "source_object_path"),
    sourceHash: stringField(row, "source_hash"),
    sourceBytes: numberField(row, "source_bytes"),
    sourcePages: numberField(row, "source_pages"),
    sourceFileCount: numberField(row, "source_file_count"),
    openaiResponseId: nullableStringField(row, "openai_response_id"),
    providerExecutionStartedAt: nullableStringField(row, "provider_execution_started_at"),
    leaseExpiresAt: nullableStringField(row, "lease_expires_at"),
    retryCount: numberField(row, "retry_count"),
    promptVersion: nullableStringField(row, "prompt_version"),
    modelVersion: nullableStringField(row, "model_version"),
  };
}

export class SupabaseSyllabusWorkerStore implements SyllabusWorkerStore {
  private readonly paths = new Map<string, string>();
  private readonly fetcher: typeof fetch;

  constructor(
    private readonly environment: WorkerRuntimeEnvironment,
    private readonly storage: StorageSourceStore,
    private readonly leaseOwner = `worker:${crypto.randomUUID()}`,
  ) {
    this.fetcher = environment.fetcher ?? fetch;
  }

  async claimNext(_now: Date, leaseSeconds: number): Promise<SyllabusWorkerJob | null> {
    const response = await this.rpc("claim_ai_syllabus_worker_job", {
      p_lease_owner: this.leaseOwner,
      p_lease_seconds: leaseSeconds,
    });
    if (response === null || (Array.isArray(response) && response.length === 0)) return null;
    const job = parseWorkerJob(workerRow(response));
    this.paths.set(job.id, job.sourceObjectPath);
    return job;
  }

  async persistResponseId(jobId: string, responseId: string): Promise<void> {
    await this.patch(jobId, {
      openai_response_id: responseId,
      provider_execution_started_at: new Date().toISOString(),
    });
  }

  async reconcileProvider(jobId: string, recoverable: boolean): Promise<void> {
    await this.rpc("record_ai_job_provider_reconciliation", { p_job_id: jobId, p_recoverable: recoverable });
  }

  async markRetry(jobId: string): Promise<void> {
    await this.rpc("increment_ai_job_retry", { p_job_id: jobId });
  }

  async finalizeSuccess(jobId: string, proposal: AiSyllabusProposal, warnings: AiWarning[], responseId: string | null, _usage: ProviderUsage | null): Promise<void> {
    await this.rpc("finalize_ai_job_success", {
      p_job_id: jobId,
      p_proposal: proposal,
      p_warnings: warnings,
      p_openai_response_id: responseId,
      p_prompt_version: proposal.promptVersion,
      p_schema_version: proposal.schemaVersion,
      p_model_version: proposal.modelVersion,
    });
  }

  async finalizeFailure(jobId: string, code: string, message?: string, terminalStatus: "FAILED" | "EXPIRED" = "FAILED"): Promise<void> {
    await this.rpc("finalize_ai_job_failure", {
      p_job_id: jobId,
      p_terminal_status: terminalStatus,
      p_error_code: code,
      p_error_message: message ?? "The AI job did not complete",
    });
  }

  async recordUsage(jobId: string, usage: ProviderUsage | null): Promise<void> {
    await this.rpc("record_ai_job_usage", {
      p_job_id: jobId,
      p_input_tokens: usage?.inputTokens ?? null,
      p_output_tokens: usage?.outputTokens ?? null,
      p_total_tokens: usage?.totalTokens ?? null,
    });
  }

  async cleanupSource(jobId: string): Promise<void> {
    const path = this.paths.get(jobId);
    if (!path) return;
    const url = `${this.environment.supabaseUrl.replace(/\/$/, "")}/storage/v1/object/${AI_SYLLABUS_SOURCE_BUCKET}/${path}`;
    const response = await this.fetcher(url, { method: "DELETE", headers: workerHeaders(this.environment.serviceRoleKey) });
    if (!response.ok && response.status !== 404) throw new Error("AI_SOURCE_CLEANUP_FAILED");
    this.paths.delete(jobId);
  }

  private async patch(jobId: string, body: Record<string, unknown>): Promise<void> {
    const url = new URL(`${this.environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/ai_jobs`);
    url.searchParams.set("id", `eq.${jobId}`);
    url.searchParams.set("status", "eq.PROCESSING");
    const response = await this.fetcher(url, {
      method: "PATCH",
      headers: { ...workerHeaders(this.environment.serviceRoleKey), "content-type": "application/json", prefer: "return=minimal" },
      body: JSON.stringify(body),
    });
    if (!response.ok) throw new Error("AI_WORKER_DATA_UNAVAILABLE");
  }

  private async rpc(name: string, body: Record<string, unknown>): Promise<unknown> {
    const response = await this.fetcher(`${this.environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/rpc/${name}`, {
      method: "POST",
      headers: { ...workerHeaders(this.environment.serviceRoleKey), "content-type": "application/json" },
      body: JSON.stringify(body),
    });
    if (!response.ok) throw new Error("AI_WORKER_DATA_UNAVAILABLE");
    return await response.json();
  }
}

function runtimeEnvironment(): WorkerRuntimeEnvironment {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim();
  const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!supabaseUrl || !serviceRoleKey) throw new Error("AI_WORKER_NOT_CONFIGURED");
  return { supabaseUrl, serviceRoleKey };
}

async function sha256Hex(bytes: Uint8Array): Promise<string> {
  const input = new ArrayBuffer(bytes.byteLength);
  new Uint8Array(input).set(bytes);
  const digest = await crypto.subtle.digest("SHA-256", input);
  return [...new Uint8Array(digest)].map((byte) => byte.toString(16).padStart(2, "0")).join("");
}

function runtimeDependencies(): SyllabusWorkerDependencies {
  const environment = runtimeEnvironment();
  const storage = new SupabaseStorageSourceStore({
    supabaseUrl: environment.supabaseUrl,
    publishableKey: environment.serviceRoleKey,
    accessToken: environment.serviceRoleKey,
  }, environmentNumber("MAX_PDF_BYTES", 50 * 1024 * 1024));
  const jobs = new SupabaseSyllabusWorkerStore(environment, storage);
  return {
    jobs,
    provider: runtimeProvider(),
    source: async (job) => {
      const object = await storage.getObject(job.userId, job.sourceObjectPath);
      if (!object) throw new Error("SOURCE_NOT_FOUND");
      if (await sha256Hex(object.body) !== job.sourceHash) throw new Error("SOURCE_HASH_MISMATCH");
      return object.body;
    },
    leaseSeconds: environmentNumber("AI_WORKER_LEASE_SECONDS", 300),
    maxRetries: environmentNumber("AI_MAX_RETRIES", 3),
    validationLimits: {
      maxSubjects: environmentNumber("MAX_SUBJECTS", 100),
      maxTopics: environmentNumber("MAX_TOPICS", 2000),
      maxTopicDepth: environmentNumber("MAX_TOPIC_DEPTH", 20),
    },
    captureUsage: async (jobId, usage) => {
      await jobs.recordUsage(jobId, usage);
    },
  };
}

function environmentNumber(name: string, fallback: number): number {
  const value = Number(Deno.env.get(name));
  return Number.isSafeInteger(value) && value > 0 ? value : fallback;
}

function runtimeProvider(): OpenAiProvider {
  return createOpenAiProvider({
    background: ["1", "true", "yes", "on"].includes((Deno.env.get("AI_OPENAI_BACKGROUND") ?? "").toLowerCase()),
    timeoutMs: environmentNumber("OPENAI_TIMEOUT_MS", 30_000),
  });
}

if (import.meta.main) {
  Deno.serve(async (request) => {
    if (request.method !== "POST") return new Response(null, { status: 405, headers: { allow: "POST" } });
    try {
      const environment = runtimeEnvironment();
      const authorization = request.headers.get("authorization")?.replace(/^Bearer\s+/i, "").trim();
      if (!authorization || authorization !== environment.serviceRoleKey) {
        return Response.json({ error: { code: "AI_WORKER_UNAUTHORIZED", message: "AI worker authorization required" } }, { status: 401 });
      }
      const processed = await runSyllabusWorker(runtimeDependencies(), environmentNumber("AI_WORKER_BATCH_SIZE", 1));
      return Response.json({ processed }, { status: 200, headers: { "cache-control": "no-store" } });
    } catch {
      return Response.json({ error: { code: "AI_WORKER_UNAVAILABLE", message: "AI worker is unavailable" } }, { status: 503 });
    }
  });
}
