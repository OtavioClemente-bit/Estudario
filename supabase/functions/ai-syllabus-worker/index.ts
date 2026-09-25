import type { AiSyllabusProposal, AiWarning } from "../_shared/contracts.ts";
import { emitAiTerminalTelemetry, type TerminalAiTelemetry } from "../_shared/job-finalizer.ts";
import { validateAiSyllabusProposal, type ProposalValidationLimits, type ProposalValidationOptions } from "../_shared/proposal-validator.ts";
import {
  createOpenAiProvider,
  OpenAiProviderError,
  resolveOpenAiModel,
  type OpenAiProvider,
  type ProviderResponse,
  type ProviderUsage,
} from "../_shared/openai-provider.ts";
import { SYLLABUS_PROMPT_VERSION, SYLLABUS_SYSTEM_PROMPT, syllabusUserPrompt } from "../_shared/prompts/syllabus-v1.ts";
import { AI_SYLLABUS_PROPOSAL_SCHEMA, AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION } from "../_shared/schemas/ai-syllabus-proposal-v1.ts";
import { AI_SYLLABUS_SOURCE_BUCKET, SupabaseStorageSourceStore, type StorageSourceStore } from "../_shared/storage-source.ts";

export interface Lease { owner: string; token: string; generation: number; }

export class LeaseLostError extends Error {
  constructor() { super("AI_JOB_LEASE_LOST"); this.name = "LeaseLostError"; }
}

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
  leaseOwner: string;
  leaseToken: string;
  leaseGeneration: number;
  processingDeadlineAt: string | null;
  retryCount: number;
  promptVersion?: string | null;
  modelVersion?: string | null;
}

export interface SyllabusWorkerStore {
  claimNext(now: Date, leaseSeconds: number, processingSeconds: number): Promise<SyllabusWorkerJob | null>;
  assertLease(jobId: string, lease: Lease): Promise<void>;
  markProviderStarted?(jobId: string, lease: Lease): Promise<void>;
  persistResponseId(jobId: string, responseId: string, lease: Lease): Promise<void>;
  reconcileProvider(jobId: string, lease: Lease, recoverable: boolean): Promise<void>;
  markRetry(jobId: string, lease: Lease): Promise<void>;
  captureUsage(jobId: string, lease: Lease, usage: ProviderUsage | null): Promise<void>;
  finalizeSuccess(jobId: string, lease: Lease, proposal: AiSyllabusProposal, warnings: AiWarning[], responseId: string | null): Promise<void>;
  finalizeFailure(jobId: string, lease: Lease, code: string, message: string, terminalStatus: "FAILED" | "EXPIRED" | "CANCELLED", providerReconciled: boolean): Promise<void>;
  cleanupSource(jobId: string, lease: Lease): Promise<void>;
}

export interface SyllabusWorkerDependencies {
  jobs: SyllabusWorkerStore;
  provider: OpenAiProvider;
  source(job: SyllabusWorkerJob): Promise<Uint8Array>;
  now?: () => Date;
  leaseSeconds?: number;
  maxRetries?: number;
  maxOutputTokens?: number;
  maxProcessingSeconds?: number;
  model?: string;
  validationLimits?: ProposalValidationLimits;
  telemetry?: (event: TerminalAiTelemetry) => void;
}

async function emitTerminalTelemetry(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  terminalStatus: TerminalAiTelemetry["terminalStatus"],
  usage: ProviderUsage | null = null,
  proposal: AiSyllabusProposal | null = null,
): Promise<void> {
  await emitAiTerminalTelemetry({
    feature: "SYLLABUS_GENERATION", userId: job.userId,
    modelVersion: proposal?.modelVersion ?? job.modelVersion ?? resolveOpenAiModel(dependencies.model),
    promptVersion: proposal?.promptVersion ?? job.promptVersion ?? SYLLABUS_PROMPT_VERSION,
    schemaVersion: proposal?.schemaVersion ?? AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
    jobId: job.id, usage, startedAt: job.providerExecutionStartedAt, terminalStatus,
    now: dependencies.now, sink: dependencies.telemetry,
  });
}

function leaseOf(job: SyllabusWorkerJob): Lease {
  if (!job.leaseOwner || !job.leaseToken || !Number.isSafeInteger(job.leaseGeneration)) throw new LeaseLostError();
  return { owner: job.leaseOwner, token: job.leaseToken, generation: job.leaseGeneration };
}

function errorCode(error: unknown): string {
  if (error instanceof OpenAiProviderError) {
    if (error.code === "OPENAI_API_KEY_MISSING") return "OPENAI_API_KEY_MISSING";
    if (error.code === "OPENAI_TIMEOUT") return "PROVIDER_TIMEOUT";
    return error.code;
  }
  const message = error instanceof Error ? error.message : String(error);
  return /^[A-Z][A-Z0-9_]{2,63}$/.test(message) ? message : "PROVIDER_ERROR";
}

function preProviderDefinitive(code: string, providerStarted: boolean, job: SyllabusWorkerJob): boolean {
  if (providerStarted || job.openaiResponseId !== null || job.providerExecutionStartedAt !== null) return false;
  return code === "OPENAI_API_KEY_MISSING" || code === "SOURCE_NOT_FOUND" || code === "SOURCE_HASH_MISMATCH" || code.startsWith("SOURCE_");
}

function deadlineExceeded(job: SyllabusWorkerJob, now: Date): boolean {
  return job.processingDeadlineAt !== null && Date.parse(job.processingDeadlineAt) <= now.getTime();
}

function terminalProviderStatus(status: ProviderResponse["status"]): boolean {
  return status === "failed" || status === "cancelled" || status === "expired" || status === "incomplete";
}

async function cleanupBestEffort(dependencies: SyllabusWorkerDependencies, job: SyllabusWorkerJob, lease: Lease): Promise<void> {
  try { await dependencies.jobs.cleanupSource(job.id, lease); } catch { /* cleanup store persists a retryable pending record */ }
}

async function finalizeFailure(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  lease: Lease,
  code: string,
  status: "FAILED" | "EXPIRED" | "CANCELLED",
  providerReconciled: boolean,
  usage: ProviderUsage | null = null,
): Promise<void> {
  await cleanupBestEffort(dependencies, job, lease);
  await dependencies.jobs.finalizeFailure(job.id, lease, code, "The AI job did not complete", status, providerReconciled);
  await emitTerminalTelemetry(dependencies, job, status, usage);
}

function validationOptions(dependencies: SyllabusWorkerDependencies): ProposalValidationOptions {
  return {
    ...dependencies.validationLimits,
    expected: {
      schemaVersion: AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
      promptVersion: SYLLABUS_PROMPT_VERSION,
      modelVersion: resolveOpenAiModel(dependencies.model),
    },
  };
}

async function processResponse(
  dependencies: SyllabusWorkerDependencies,
  job: SyllabusWorkerJob,
  lease: Lease,
  response: ProviderResponse,
  now: Date,
): Promise<void> {
  await dependencies.jobs.assertLease(job.id, lease);
  if (deadlineExceeded(job, now)) {
    if (response.status === "queued" || response.status === "in_progress") {
      const cancellation = await dependencies.provider.cancel(response.id);
      await dependencies.jobs.assertLease(job.id, lease);
      if (terminalProviderStatus(cancellation.status)) await finalizeFailure(dependencies, job, lease, "PROCESSING_DEADLINE_EXCEEDED", "EXPIRED", false, cancellation.usage);
      else await dependencies.jobs.reconcileProvider(job.id, lease, true);
      return;
    }
    await finalizeFailure(dependencies, job, lease, "PROCESSING_DEADLINE_EXCEEDED", "EXPIRED", false, response.usage);
    return;
  }
  if (response.status === "queued" || response.status === "in_progress") {
    await dependencies.jobs.reconcileProvider(job.id, lease, true);
    if (job.retryCount < (dependencies.maxRetries ?? 3)) await dependencies.jobs.markRetry(job.id, lease);
    return;
  }
  if (terminalProviderStatus(response.status)) {
    await finalizeFailure(dependencies, job, lease, "PROVIDER_RESULT_UNAVAILABLE", "FAILED", false, response.usage);
    return;
  }

  let proposal: AiSyllabusProposal;
  try {
    proposal = await validateAiSyllabusProposal(response.outputText ?? "", validationOptions(dependencies));
  } catch (error) {
    const code = error instanceof Error && error.message.startsWith("EMPTY_OUTPUT") ? "EMPTY_OUTPUT" :
      error instanceof Error && error.message.startsWith("OUTPUT_LIMIT_EXCEEDED") ? "OUTPUT_LIMIT_EXCEEDED" :
      error instanceof Error && error.message.startsWith("VERSION_MISMATCH") ? "VERSION_MISMATCH" : "SCHEMA_MISMATCH";
    await finalizeFailure(dependencies, job, lease, code, "FAILED", false, response.usage);
    return;
  }
  await dependencies.jobs.captureUsage(job.id, lease, response.usage);
  await cleanupBestEffort(dependencies, job, lease);
  await dependencies.jobs.finalizeSuccess(job.id, lease, proposal, proposal.warnings, response.id);
  await emitTerminalTelemetry(dependencies, job, "SUCCEEDED", response.usage, proposal);
}

export async function processSyllabusJob(dependencies: SyllabusWorkerDependencies): Promise<boolean> {
  const now = dependencies.now ?? (() => new Date());
  const job = await dependencies.jobs.claimNext(now(), dependencies.leaseSeconds ?? 300, dependencies.maxProcessingSeconds ?? 900);
  if (!job) return false;
  const lease = leaseOf(job);
  let providerStarted = job.providerExecutionStartedAt !== null || job.openaiResponseId !== null;
  try {
    await dependencies.jobs.assertLease(job.id, lease);
    if (deadlineExceeded(job, now()) && !providerStarted) {
      await finalizeFailure(dependencies, job, lease, "PROCESSING_DEADLINE_EXCEEDED", "EXPIRED", false);
      return true;
    }
    let response: ProviderResponse;
    if (job.openaiResponseId !== null) {
      response = await dependencies.provider.retrieve(job.openaiResponseId);
      await dependencies.jobs.assertLease(job.id, lease);
    } else {
      const bytes = await dependencies.source(job);
      await dependencies.jobs.assertLease(job.id, lease);
      if (deadlineExceeded(job, now())) {
        await finalizeFailure(dependencies, job, lease, "PROCESSING_DEADLINE_EXCEEDED", "EXPIRED", false);
        return true;
      }
      if (dependencies.jobs.markProviderStarted) await dependencies.jobs.markProviderStarted(job.id, lease);
      const providerStartedAt = now().toISOString();
      response = await dependencies.provider.start({
        jobId: job.id,
        idempotencyKey: job.id,
        source: { filename: job.sourceObjectPath.split("/").pop() ?? "source.pdf", bytes },
        systemPrompt: SYLLABUS_SYSTEM_PROMPT,
        userPrompt: syllabusUserPrompt(),
        promptVersion: SYLLABUS_PROMPT_VERSION,
        schemaVersion: AI_SYLLABUS_PROPOSAL_SCHEMA_VERSION,
        schema: AI_SYLLABUS_PROPOSAL_SCHEMA,
        model: resolveOpenAiModel(dependencies.model),
        background: true,
        store: true,
        maxOutputTokens: dependencies.maxOutputTokens,
      });
      providerStarted = true;
      job.providerExecutionStartedAt = providerStartedAt;
      await dependencies.jobs.persistResponseId(job.id, response.id, lease);
    }
    await processResponse(dependencies, job, lease, response, now());
  } catch (error) {
    if (error instanceof LeaseLostError) return true;
    const code = errorCode(error);
    if (preProviderDefinitive(code, providerStarted, job)) {
      await finalizeFailure(dependencies, job, lease, code, "FAILED", false);
      return true;
    }
    try {
      await dependencies.jobs.assertLease(job.id, lease);
      await dependencies.jobs.reconcileProvider(job.id, lease, true);
      if (job.retryCount < (dependencies.maxRetries ?? 3)) await dependencies.jobs.markRetry(job.id, lease);
    } catch (leaseError) {
      if (!(leaseError instanceof LeaseLostError)) throw leaseError;
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

interface WorkerRuntimeEnvironment { supabaseUrl: string; serviceRoleKey: string; fetcher?: typeof fetch; }

function workerHeaders(serviceRoleKey: string): HeadersInit {
  return { apikey: serviceRoleKey, authorization: `Bearer ${serviceRoleKey}`, accept: "application/json" };
}

function row(value: unknown): Record<string, unknown> {
  if (Array.isArray(value) && value.length > 0 && typeof value[0] === "object" && value[0] !== null) return value[0] as Record<string, unknown>;
  if (typeof value === "object" && value !== null && !Array.isArray(value)) return value as Record<string, unknown>;
  throw new Error("AI_WORKER_DATA_UNAVAILABLE");
}

function stringField(value: Record<string, unknown>, key: string): string {
  if (typeof value[key] !== "string" || value[key].trim().length === 0) throw new Error("AI_WORKER_DATA_UNAVAILABLE");
  return value[key] as string;
}

function nullableString(value: Record<string, unknown>, key: string): string | null {
  return value[key] === null || value[key] === undefined ? null : stringField(value, key);
}

function integerField(value: Record<string, unknown>, key: string): number {
  if (typeof value[key] !== "number" || !Number.isSafeInteger(value[key])) throw new Error("AI_WORKER_DATA_UNAVAILABLE");
  return value[key] as number;
}

function parseJob(value: Record<string, unknown>): SyllabusWorkerJob {
  return {
    id: stringField(value, "id"), userId: stringField(value, "user_id"), status: "PROCESSING",
    sourceObjectPath: stringField(value, "source_object_path"), sourceHash: stringField(value, "source_hash"),
    sourceBytes: integerField(value, "source_bytes"), sourcePages: integerField(value, "source_pages"), sourceFileCount: integerField(value, "source_file_count"),
    openaiResponseId: nullableString(value, "openai_response_id"), providerExecutionStartedAt: nullableString(value, "provider_execution_started_at"),
    leaseExpiresAt: nullableString(value, "lease_expires_at"), leaseOwner: stringField(value, "lease_owner"), leaseToken: stringField(value, "lease_token"),
    leaseGeneration: integerField(value, "lease_generation"), processingDeadlineAt: nullableString(value, "processing_deadline_at"), retryCount: integerField(value, "retry_count"),
    promptVersion: nullableString(value, "prompt_version"), modelVersion: nullableString(value, "model_version"),
  };
}

export class SupabaseSyllabusWorkerStore implements SyllabusWorkerStore {
  private readonly fetcher: typeof fetch;
  private readonly workerOwner: string;
  constructor(private readonly environment: WorkerRuntimeEnvironment, private readonly storage: StorageSourceStore, workerOwner = `worker:${crypto.randomUUID()}`) {
    this.fetcher = environment.fetcher ?? fetch;
    this.workerOwner = workerOwner;
  }
  async claimNext(_now: Date, leaseSeconds: number, processingSeconds: number): Promise<SyllabusWorkerJob | null> {
    const value = await this.rpc("claim_ai_syllabus_worker_job", { p_lease_owner: this.workerOwner, p_lease_token: crypto.randomUUID(), p_lease_seconds: leaseSeconds, p_processing_seconds: processingSeconds });
    if (value === null || (Array.isArray(value) && value.length === 0)) return null;
    return parseJob(row(value));
  }
  async assertLease(jobId: string, lease: Lease): Promise<void> { await this.rpc("assert_ai_job_lease", { p_job_id: jobId, ...lease }); }
  async markProviderStarted(jobId: string, lease: Lease): Promise<void> { await this.rpc("mark_ai_job_provider_execution_started", { p_job_id: jobId, ...lease }); }
  async persistResponseId(jobId: string, responseId: string, lease: Lease): Promise<void> { await this.rpc("persist_ai_job_provider_response", { p_job_id: jobId, p_response_id: responseId, ...lease }); }
  async reconcileProvider(jobId: string, lease: Lease, recoverable: boolean): Promise<void> { await this.rpc("record_ai_job_provider_reconciliation", { p_job_id: jobId, p_recoverable: recoverable, ...lease }); }
  async markRetry(jobId: string, lease: Lease): Promise<void> { await this.rpc("increment_ai_job_retry", { p_job_id: jobId, ...lease }); }
  async captureUsage(jobId: string, lease: Lease, usage: ProviderUsage | null): Promise<void> { await this.rpc("record_ai_job_usage", { p_job_id: jobId, p_input_tokens: usage?.inputTokens ?? null, p_output_tokens: usage?.outputTokens ?? null, p_total_tokens: usage?.totalTokens ?? null, ...lease }); }
  async finalizeSuccess(jobId: string, lease: Lease, proposal: AiSyllabusProposal, warnings: AiWarning[], responseId: string | null): Promise<void> { await this.rpc("finalize_ai_job_success_with_lease", { p_job_id: jobId, p_proposal: proposal, p_warnings: warnings, p_openai_response_id: responseId, p_prompt_version: proposal.promptVersion, p_schema_version: proposal.schemaVersion, p_model_version: proposal.modelVersion, ...lease }); }
  async finalizeFailure(jobId: string, lease: Lease, code: string, message: string, terminalStatus: "FAILED" | "EXPIRED" | "CANCELLED", providerReconciled: boolean): Promise<void> { await this.rpc("finalize_ai_job_failure_with_lease", { p_job_id: jobId, p_terminal_status: terminalStatus, p_error_code: code, p_error_message: message, p_provider_reconciled: providerReconciled, ...lease }); }
  async cleanupSource(jobId: string, lease: Lease): Promise<void> {
    const cleanup = row(await this.rpc("prepare_ai_job_source_cleanup", { p_job_id: jobId, ...lease }));
    if (cleanup.status === "DELETED") return;
    const path = stringField(cleanup, "source_object_path");
    try {
      const response = await this.fetcher(`${this.environment.supabaseUrl.replace(/\/$/, "")}/storage/v1/object/${AI_SYLLABUS_SOURCE_BUCKET}/${path}`, { method: "DELETE", headers: workerHeaders(this.environment.serviceRoleKey) });
      if (!response.ok && response.status !== 404) throw new Error("AI_SOURCE_CLEANUP_FAILED");
      await this.rpc("complete_ai_job_source_cleanup", { p_job_id: jobId, ...lease });
    } catch (error) {
      await this.rpc("fail_ai_job_source_cleanup", { p_job_id: jobId, p_error: error instanceof Error ? error.message : "AI_SOURCE_CLEANUP_FAILED", ...lease });
      throw error;
    }
  }
  async cleanupPendingSources(limit = 10): Promise<void> {
    for (let index = 0; index < limit; index += 1) {
      const owner = `${this.workerOwner}:cleanup`, token = crypto.randomUUID();
      const value = await this.rpc("claim_ai_job_source_cleanup", { p_lease_owner: owner, p_lease_token: token, p_lease_seconds: 300 });
      if (value === null || (Array.isArray(value) && value.length === 0)) return;
      const cleanup = row(value), jobId = stringField(cleanup, "job_id"), path = stringField(cleanup, "source_object_path");
      const cleanupLease = { p_lease_owner: owner, p_lease_token: token, p_lease_generation: integerField(cleanup, "lease_generation") };
      try {
        const response = await this.fetcher(`${this.environment.supabaseUrl.replace(/\/$/, "")}/storage/v1/object/${AI_SYLLABUS_SOURCE_BUCKET}/${path}`, { method: "DELETE", headers: workerHeaders(this.environment.serviceRoleKey) });
        if (!response.ok && response.status !== 404) throw new Error("AI_SOURCE_CLEANUP_FAILED");
        await this.rpc("complete_ai_job_source_cleanup", { p_job_id: jobId, ...cleanupLease });
      } catch (error) {
        await this.rpc("fail_ai_job_source_cleanup", { p_job_id: jobId, p_error: error instanceof Error ? error.message : "AI_SOURCE_CLEANUP_FAILED", ...cleanupLease });
      }
    }
  }
  private async rpc(name: string, body: Record<string, unknown>): Promise<unknown> {
    const response = await this.fetcher(`${this.environment.supabaseUrl.replace(/\/$/, "")}/rest/v1/rpc/${name}`, { method: "POST", headers: { ...workerHeaders(this.environment.serviceRoleKey), "content-type": "application/json" }, body: JSON.stringify(body) });
    if (!response.ok) {
      let providerMessage = "";
      try {
        const payload = await response.clone().json() as Record<string, unknown>;
        providerMessage = [payload.code, payload.message, payload.error].filter((value) => typeof value === "string").join(" ");
      } catch { /* retain the safe generic error */ }
      if (response.status === 409 || response.status === 412 || /AI_JOB_LEASE_LOST|LEASE_LOST/.test(providerMessage)) throw new LeaseLostError();
      throw new Error("AI_WORKER_DATA_UNAVAILABLE");
    }
    return await response.json();
  }
}

function environmentNumber(name: string, fallback: number): number { const value = Number(Deno.env.get(name)); return Number.isSafeInteger(value) && value > 0 ? value : fallback; }
function runtimeEnvironment(): WorkerRuntimeEnvironment {
  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim(), serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!supabaseUrl || !serviceRoleKey) throw new Error("AI_WORKER_NOT_CONFIGURED");
  return { supabaseUrl, serviceRoleKey };
}
function runtimeDependencies(): SyllabusWorkerDependencies & { runtimeStore: SupabaseSyllabusWorkerStore } {
  const environment = runtimeEnvironment();
  const storage = new SupabaseStorageSourceStore({ supabaseUrl: environment.supabaseUrl, publishableKey: environment.serviceRoleKey, accessToken: environment.serviceRoleKey, serviceRoleKey: environment.serviceRoleKey }, environmentNumber("MAX_PDF_BYTES", 50 * 1024 * 1024));
  const runtimeStore = new SupabaseSyllabusWorkerStore(environment, storage);
  return {
    runtimeStore, jobs: runtimeStore,
    provider: createOpenAiProvider({ background: true, model: resolveOpenAiModel(), maxOutputTokens: environmentNumber("MAX_OUTPUT_TOKENS", 4096), timeoutMs: environmentNumber("OPENAI_TIMEOUT_MS", 30_000), store: true }),
    source: async (job) => {
      const object = await storage.getObject(job.userId, job.sourceObjectPath);
      if (!object) throw new Error("SOURCE_NOT_FOUND");
      const buffer = new ArrayBuffer(object.body.byteLength); new Uint8Array(buffer).set(object.body);
      const digest = await crypto.subtle.digest("SHA-256", buffer);
      const hash = [...new Uint8Array(digest)].map((byte) => byte.toString(16).padStart(2, "0")).join("");
      if (hash !== job.sourceHash) throw new Error("SOURCE_HASH_MISMATCH");
      return object.body;
    },
    leaseSeconds: environmentNumber("AI_WORKER_LEASE_SECONDS", 300), maxRetries: environmentNumber("AI_MAX_RETRIES", 3),
    maxOutputTokens: environmentNumber("MAX_OUTPUT_TOKENS", 4096), maxProcessingSeconds: environmentNumber("MAX_PROCESSING_SECONDS", 900), model: resolveOpenAiModel(),
    validationLimits: {
      maxSubjects: environmentNumber("MAX_SUBJECTS", 100),
      maxTopics: environmentNumber("MAX_TOPICS", 2000),
      maxTopicDepth: environmentNumber("MAX_TOPIC_DEPTH", 8),
    },
  };
}

if (import.meta.main) {
  Deno.serve(async (request) => {
    if (request.method !== "POST") return new Response(null, { status: 405, headers: { allow: "POST" } });
    try {
      const environment = runtimeEnvironment(), bearer = request.headers.get("authorization")?.replace(/^Bearer\s+/i, "").trim();
      if (bearer !== environment.serviceRoleKey) return Response.json({ error: { code: "AI_WORKER_UNAUTHORIZED", message: "AI worker authorization required" } }, { status: 401 });
      const dependencies = runtimeDependencies(); await dependencies.runtimeStore.cleanupPendingSources();
      const processed = await runSyllabusWorker(dependencies, environmentNumber("AI_WORKER_BATCH_SIZE", 1));
      return Response.json({ processed }, { headers: { "cache-control": "no-store" } });
    } catch { return Response.json({ error: { code: "AI_WORKER_UNAVAILABLE", message: "AI worker is unavailable" } }, { status: 503 }); }
  });
}
